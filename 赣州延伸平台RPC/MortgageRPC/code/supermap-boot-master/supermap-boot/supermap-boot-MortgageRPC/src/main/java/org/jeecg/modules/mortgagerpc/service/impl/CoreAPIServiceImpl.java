package org.jeecg.modules.mortgagerpc.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.exception.SupermapBootException;
import org.jeecg.common.exception.SupermapBootExceptionHandler;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.common.system.util.HttpClientUtil;
import org.jeecg.common.system.util.StringHelper;
import org.jeecg.common.util.*;
import org.jeecg.modules.mapper.Wfi_proinstMapper;
import org.jeecg.modules.mortgagerpc.entity.*;
import org.jeecg.modules.mortgagerpc.service.*;
import org.jeecg.common.util.ConstValueMrpc.MrpccodingEnum;
import org.jeecg.common.util.ConstValueMrpc.RequestcodeEnum;
import org.jeecg.modules.mortgagerpc.util.CoreAPIJsonCheckUtil;
import org.jeecg.modules.system.entity.SysDepart;
import org.jeecg.modules.system.entity.SysDepartConfig;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.mapper.SysDepartMapper;
import org.jeecg.modules.system.service.ISysDepartConfigService;
import org.jeecg.modules.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @ClassName CoreAPIServiceImpl
 * @Description TODO
 * @Author notebao
 * @Date 2019/9/2 14:52
 */
@Slf4j
@Service
public class CoreAPIServiceImpl implements ICoreAPIService {
    //token失效时间
    private final static long TIME_OUT = 50 * 60 * 1000;

    private final static long TIME_AHEAD = 5 * 60 * 1000;

    @Autowired
    ISysUserService sysUserService;

    @Autowired
    ISys_configService sys_configService;

    @Autowired
    IWfi_prodefService wfi_prodefService;

    @Autowired
    Wfi_proinstMapper proinstMapper;

    @Autowired
    private IBdc_sqrService bdc_sqrService;

    @Autowired
    private IBdc_qldyService bdc_qldyService;

    @Autowired
    IBdc_fsqlService bdc_fsqlService;

    @Autowired
    IBdc_dyService bdc_dyService;

    @Autowired
    IBdc_qlService bdc_qlService;

    @Autowired
    IWfi_materclassService wfi_materclassService;

    @Autowired
    IWfi_materdataService wfi_materdataService;

    @Autowired
    IWfi_proinstService wfi_proinstService;

    @Autowired
    SysDepartMapper sysDepartMapper;

    @Autowired
    IBdc_zsService bdc_zsService;

    @Autowired
    ISysDepartConfigService sysDepartConfigService;

    @Autowired
    ISysBaseAPI sysBaseAPI;

    @Override
    public String applicationToken(String requestJson) {
        String requestcode=RequestcodeEnum.TOKEN.Value;
        String decryDate = RSACoder.decryptRequest(requestJson);
        if(MrpccodingEnum.FAIL.Value.equals(decryDate)){
            return RSACoder.resultsJson(decryDate, requestcode, null, "","");
        }
        //校验json格式
        Result checkjson = CoreAPIJsonCheckUtil.checkJson(decryDate);
        if(!checkjson.isSuccess()) {
            return RSACoder.resultsJson(MrpccodingEnum.FAIL.Value, requestcode, null, "",checkjson.getMessage());
        }
        try {
            JSONObject requestDate=JSONObject.parseObject(decryDate);
            JSONObject data = requestDate.getJSONObject("data");
            //1. 校验用户是否有效
            SysUser sysUser = sysUserService.getUserByName(data.getString("username"));
            Result<JSONObject> result = sysUserService.checkUserIsEffectiveNoLog(sysUser);
            if(!result.isSuccess()) {
                return RSACoder.resultsJson(MrpccodingEnum.AUTHENTICATION.Value, requestcode, null, "","");
            }
            //2. 校验用户名或密码是否正确
            String userpassword = PasswordUtil.encrypt(data.getString("username"), data.getString("appcode"), sysUser.getSalt());
            String syspassword = sysUser.getPassword();
            if (!syspassword.equals(userpassword)) {
                return RSACoder.resultsJson(MrpccodingEnum.AUTHENTICATION.Value, requestcode, null, "","");
            }

            List<SysDepart> sysDeparts = sysDepartMapper.queryUserDeparts(sysUser.getId());
            if(sysDeparts.isEmpty() || sysDeparts.size()>1) {
                throw new SupermapBootException("申报的账号隶属部门有且仅能有一个，请检查账号的部门配置");
            }

            String jgdm = sysDeparts.get(0).getJgdm();
            JSONObject json=new JSONObject(2);
            json.put("timeout", TIME_OUT);
            json.put("token", JwtUtils.createToken(data.getString("username")+","+(!StringHelper.isEmpty(jgdm)?jgdm:data.getString("jgdm")), data.getString("appcode"), new Date(), TIME_OUT));
            return RSACoder.resultsJson(MrpccodingEnum.SUCCESS.Value, requestcode, json, "","");
        } catch (Exception e) {
            e.printStackTrace();
            return RSACoder.resultsJson(MrpccodingEnum.OTHERERRORS.Value, requestcode, null, "","");
        }
    }

    @Override
    public String finalResultModule(String requestJson, HttpServletRequest request) {
        String decryDate = RSACoder.decryptRequest(requestJson);
        if(MrpccodingEnum.FAIL.Value.equals(decryDate)){
            return RSACoder.resultsJson(MrpccodingEnum.FAIL.Value, "", null, "","");
        }
        String requestcode = "";
        try {
            JSONObject jsonObject = JSONObject.parseObject(decryDate);
            requestcode = jsonObject.getString("requestcode");
            //校验json格式
            Result checkjson = CoreAPIJsonCheckUtil.checkJson(decryDate);
            if(!checkjson.isSuccess()) {
                return RSACoder.resultsJson(MrpccodingEnum.FAIL.Value, requestcode, null, "",checkjson.getMessage());
            }

            if(RequestcodeEnum.FW.Value.equals(requestcode)){
                //从token中获取配置的机构接口类型代码
                String token = request.getParameter("token");
                Result<Claims> claimsResult = JwtUtils.verifyToken(token);
                Claims claims = claimsResult.getResult();
                String usermsg = claims.getId();
                if(!StringHelper.isEmpty(usermsg)) {
                    String[] split = usermsg.split(",");
                    if(split.length>1){
                        String jgdm = split[1];
                        JSONArray dataArry = jsonObject.getJSONArray("data");
                        for(int i=0;i<dataArry.size();i++) {
                            JSONObject data = dataArry.getJSONObject(i);
                            data.put("jgdm", jgdm);
                        }
                    }
                }
            }

            String coreQueryUrl = sys_configService.getConfigByKey("coreQueryUrl");
            if(StringHelper.isEmpty(coreQueryUrl)) {
                log.error("未配置查询接口地址，请联系管理员进行配置");
                return RSACoder.resultsJson(MrpccodingEnum.ERROR.Value, requestcode, null, "","未配置查询接口地址，请联系管理员进行配置");
            }
            String httpresult = HttpClientUtil.requestPost(jsonObject.toJSONString(), coreQueryUrl);
            return RSACoder.encryptByPrivateKeyBase64(httpresult);
        }catch (Exception e){
            e.printStackTrace();
            return RSACoder.resultsJson(MrpccodingEnum.OTHERERRORS.Value, requestcode, null, "","");
        }
    }

    @Override
    public String declareProject(String requestJson, HttpServletRequest request) {
        String token = request.getParameter("token");
        if (StringHelper.isEmpty(token)) {
            return RSACoder.resultsJson(MrpccodingEnum.REQUIRED.Value, "", null, "", "令牌（TOKEN）不能为空!");
        }
        String decryDate = RSACoder.decryptRequest(requestJson);
        if(MrpccodingEnum.FAIL.Value.equals(decryDate)){
            return RSACoder.resultsJson(MrpccodingEnum.FAIL.Value, "", null, "",MrpccodingEnum.FAIL.Name);
        }
        Map<Object, String> map = new HashMap<>();
        String requestcode = "";
        try {
            JSONObject jsonObject = JSONObject.parseObject(decryDate);
            requestcode = jsonObject.getString("requestcode");
            //校验json格式
            Result checkjson = CoreAPIJsonCheckUtil.checkJson(decryDate);
            if(!checkjson.isSuccess()) {
                map.put("state", MrpccodingEnum.FAIL.Value);
                map.put("result", checkjson.getMessage());
                return RSACoder.resultsJson(MrpccodingEnum.FAIL.Value, requestcode, map, "",checkjson.getMessage());
            }
            //解析数据
            JSONObject analysisData = AnalysisData(jsonObject);
            //创建项目
            Wfi_proinst proinst = createMrpcProject(analysisData, token);
            //创建成功，调用核心API创建业务接口
            wfi_proinstService.submitProject(proinst.getProlsh(), ConstValueMrpc.YWLY.JRJG.Value);
            //返回
            map.put("state", "0");
            map.put("result", "申报成功，请耐心等待审核");
            return RSACoder.resultsJson(MrpccodingEnum.SUCCESS.Value, requestcode, map, "","");
        } catch (SupermapBootException s) {
            //手动抛出的异常
            s.printStackTrace();
            return RSACoder.resultsJson(MrpccodingEnum.OTHERERRORS.Value, requestcode, null, "",s.getMessage());
        } catch (Exception e){
            e.printStackTrace();
            return RSACoder.resultsJson(MrpccodingEnum.OTHERERRORS.Value, requestcode, null, "","");
        }
    }

    /***************************************** 解析数据 start *****************************************/
    /**
     * @Author taochunda
     * @Description 解析抵押申报业务JSON串（金融机构传入），格式示例详见：【北京超图信息-互联网+不动产抵押登记风险防控平台接口标准v1.3.2.docx】
     * @Date 2019-09-06 10:14
     * @Param [jsonObject]
     * @return com.alibaba.fastjson.JSONObject
     **/
    public static JSONObject AnalysisData(JSONObject jsonObject) {
        JSONObject object = new JSONObject();
        object.put("requestcode", jsonObject.getString("requestcode"));
        object.put("requestseq", jsonObject.getString("requestseq"));
        JSONObject dataJson = jsonObject.getJSONObject("data");
        Map<String, Object> data = new HashMap<>();
        //基本信息
        data = getBasicInfo(data, dataJson);
        //申请人集合（权利人、义务人、抵押权人）
        List<Map> sqrList = new ArrayList<Map>();
        //权利人集合
        String qlrlist = dataJson.getString("qlrlist");
        if(!StringHelper.isEmpty(qlrlist)){
            sqrList = getQlrList(sqrList, qlrlist);
        }
        //义务人集合
        String ywrlist = dataJson.getString("ywrlist");
        if(!StringHelper.isEmpty(ywrlist)){
            sqrList = getYwrList(sqrList, ywrlist);
        }
        //抵押权人集合
        String dyqrlist = dataJson.getString("dyqrlist");
        if(!StringHelper.isEmpty(dyqrlist)){
            sqrList = getDyqrList(sqrList, dyqrlist);
        }
        //权利信息
        JSONObject ql = dataJson.getJSONObject("ql");
        Map<String, Object> qlInfo = new HashMap<>();
        if (ql != null && !ql.isEmpty()) {
            qlInfo = getQlInfo(ql);
        } else {
            JSONObject bghql = dataJson.getJSONObject("bghql");
            if (bghql != null && !bghql.isEmpty()) {
                qlInfo = getQlInfo(bghql);
            }
        }

        //抵押单元集合
        String dylist = dataJson.getString("dylist");
        List<Map> dyList = new ArrayList<Map>();
        if(!StringHelper.isEmpty(dylist)){
            dyList = getDyList(dylist);
        }
        //附件集合
        String promater = dataJson.getString("materclass");
        List<Map> promaterList = new ArrayList<Map>();
        if(!StringHelper.isEmpty(promater)){
            promaterList = getPromaterList(promater);
        }

        data.put("sqrlist", sqrList);
        data.put("ql", qlInfo);
        data.put("dylist", dyList);
        data.put("promater", promaterList);
        object.put("data", data);

        return object;
    }

    public static Map<String, Object> getBasicInfo(Map<String, Object> data, JSONObject jsonObject) {
        data.put("ywlsh", jsonObject.getString("ywlsh"));
        data.put("dyhth", jsonObject.getString("dyhth"));
        data.put("sbrymc", jsonObject.getString("sbrymc"));
        data.put("sbryzjh", jsonObject.getString("sbryzjh"));
        data.put("sbrylxdh", jsonObject.getString("sbrylxdh"));
        data.put("sbjgmc", jsonObject.getString("sbjgmc"));
        data.put("sbjgzjh", jsonObject.getString("sbjgzjh"));
        data.put("xzqdm", jsonObject.getString("xzqdm"));
        data.put("jgdm", jsonObject.getString("jgdm"));
        return data;
    }

    public static Map<String, Object> getQlInfo(JSONObject jsonObject) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("zqse", jsonObject.getString("zqse"));
        map.put("djyy", jsonObject.getString("djyy"));
        map.put("dyfs", jsonObject.getString("dyfs"));
        map.put("dyqlqssj", jsonObject.getString("dyqlqssj"));
        map.put("dyqljssj", jsonObject.getString("dyqljssj"));
        map.put("fj", jsonObject.getString("fj"));
        map.put("djlx", jsonObject.getString("djlx"));
        map.put("qllx", jsonObject.getString("qllx"));
        return map;
    }

    public static List<Map> getQlrList(List<Map> sqrList, String qlrlist) {
        JSONArray jsonArray = JSONArray.parseArray(qlrlist);
        for (int i = 0; i < jsonArray.size(); i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            JSONObject jsonObject = JSONObject.parseObject(StringHelper.formatObject(jsonArray.get(i)));
            map.put("sqrmc", jsonObject.getString("qlrmc"));
            map.put("sqrzjh", jsonObject.getString("qlrzjh"));
            map.put("sqrzjzl", jsonObject.getString("qlrzjzl"));
            map.put("sqrlxdh", jsonObject.getString("qlrlxdh"));
            map.put("dlrmc", jsonObject.getString("dlrmc"));
            map.put("dlrzjh", jsonObject.getString("dlrzjh"));
            map.put("dlrzjzl", jsonObject.getString("dlrzjzl"));
            map.put("dlrlxdh", jsonObject.getString("dlrlxdh"));
            map.put("frmc", jsonObject.getString("frmc"));
            map.put("frzjh", jsonObject.getString("frzjh"));
            map.put("frzjzl", jsonObject.getString("frzjzl"));
            map.put("frlxdh", jsonObject.getString("frlxdh"));
            map.put("gyfs", jsonObject.getString("gyfs"));
            map.put("qlbl", jsonObject.getString("qlbl"));
            map.put("sqrlb", ConstValue.SQRLB.JF.Value);
            map.put("sqrlx", jsonObject.getString("qlrlx"));
            map.put("sxh", jsonObject.getString("sxh"));
            map.put("bdcqzh", jsonObject.getString("bdcqzh"));
            sqrList.add(map);
        }
        return sqrList;
    }

    public static List<Map> getYwrList(List<Map> sqrList, String ywrlist) {
        JSONArray jsonArray = JSONArray.parseArray(ywrlist);
        for (int i = 0; i < jsonArray.size(); i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            JSONObject jsonObject = JSONObject.parseObject(StringHelper.formatObject(jsonArray.get(i)));
            map.put("sqrmc", jsonObject.getString("ywrmc"));
            map.put("sqrzjh", jsonObject.getString("ywrzjh"));
            map.put("sqrzjzl", jsonObject.getString("ywrzjzl"));
            map.put("sqrlxdh", jsonObject.getString("ywrlxdh"));
            map.put("dlrmc", jsonObject.getString("dlrmc"));
            map.put("dlrzjh", jsonObject.getString("dlrzjh"));
            map.put("dlrzjzl", jsonObject.getString("dlrzjzl"));
            map.put("dlrlxdh", jsonObject.getString("dlrlxdh"));
            map.put("frmc", jsonObject.getString("frmc"));
            map.put("frzjh", jsonObject.getString("frzjh"));
            map.put("frzjzl", jsonObject.getString("frzjzl"));
            map.put("frlxdh", jsonObject.getString("frlxdh"));
            map.put("gyfs", jsonObject.getString("gyfs"));
            map.put("qlbl", jsonObject.getString("qlbl"));
            map.put("sqrlx", jsonObject.getString("qlrlx"));
            map.put("sxh", jsonObject.getString("sxh"));
            map.put("bdcqzh", jsonObject.getString("bdcqzh"));
            map.put("sqrlb", ConstValue.SQRLB.YF.Value);
            sqrList.add(map);
        }
        return sqrList;
    }

    public static List<Map> getDyqrList(List<Map> sqrList, String dyqrlist) {
        JSONArray jsonArray = JSONArray.parseArray(dyqrlist);
        for (int i = 0; i < jsonArray.size(); i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            JSONObject jsonObject = JSONObject.parseObject(StringHelper.formatObject(jsonArray.get(i)));
            map.put("sqrmc", jsonObject.getString("dyqrmc"));
            map.put("sqrzjh", jsonObject.getString("dyqrzjh"));
            map.put("sqrzjzl", jsonObject.getString("dyqrzjzl"));
            map.put("sqrlxdh", jsonObject.getString("dyqrlxdh"));
            map.put("dlrmc", jsonObject.getString("dlrmc"));
            map.put("dlrzjh", jsonObject.getString("dlrzjh"));
            map.put("dlrzjzl", jsonObject.getString("dlrzjzl"));
            map.put("dlrlxdh", jsonObject.getString("dlrlxdh"));
            map.put("frmc", jsonObject.getString("frmc"));
            map.put("frzjh", jsonObject.getString("frzjh"));
            map.put("frzjzl", jsonObject.getString("frzjzl"));
            map.put("frlxdh", jsonObject.getString("frlxdh"));
            map.put("gyfs", jsonObject.getString("gyfs"));
            map.put("qlbl", jsonObject.getString("qlbl"));
            map.put("sqrlx", jsonObject.getString("qlrlx"));
            map.put("sxh", jsonObject.getString("sxh"));
            map.put("sqrlb", ConstValue.SQRLB.DYQR.Value);
            sqrList.add(map);
        }
        return sqrList;
    }

    public static List<Map> getDyList(String dylist) {
        List<Map> list = new ArrayList<Map>();
        JSONArray jsonArray = JSONArray.parseArray(dylist);
        for (int i = 0; i < jsonArray.size(); i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            JSONObject jsonObject = JSONObject.parseObject(StringHelper.formatObject(jsonArray.get(i)));

            //单元信息
            map.put("bdcdyh", jsonObject.getString("bdcdyh"));
            map.put("zl", jsonObject.getString("zl"));
            map.put("dgzqse", jsonObject.getDouble("dgzqse"));
            list.add(map);
        }
        return list;
    }

    public static List<Map> getPromaterList(String promater) {
        List<Map> list = new ArrayList<Map>();
        JSONArray jsonArray = JSONArray.parseArray(promater);
        for (int i = 0; i < jsonArray.size(); i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            JSONObject jsonObject = JSONObject.parseObject(StringHelper.formatObject(jsonArray.get(i)));
            String material_name = jsonObject.getString("material_name");
            String material_count = jsonObject.getString("material_count");
            String material_desc = jsonObject.getString("material_desc");
            String materdata = jsonObject.getString("materdata");

            JSONArray array = JSONArray.parseArray(materdata);
            List<Map> map_materdata = new ArrayList<Map>();
            for (int j = 0; j < array.size(); j++) {
                Map<String, Object> info = new HashMap<String, Object>();
                JSONObject object = JSONObject.parseObject(StringHelper.formatObject(array.get(i)));
                String file_id = object.getString("file_id");
                String file_name = object.getString("file_name");
                String file_postfix = object.getString("file_postfix");
                String file_index = object.getString("file_index");
                String file_url = object.getString("file_url");
                info.put("file_id", file_id);
                info.put("file_name", file_name);
                info.put("file_postfix", file_postfix);
                info.put("file_index", file_index);
                info.put("file_url", file_url);
                map_materdata.add(info);
            }

            map.put("material_name", material_name);
            map.put("material_count", material_count);
            map.put("material_desc", material_desc);
            map.put("materdata", map_materdata);
            list.add(map);
        }
        return list;
    }
    /***************************************** 解析数据 end *****************************************/

    /***************************************** 抵押平台创建项目 start *****************************************/
    @Transactional(rollbackFor = Exception.class)
    public Wfi_proinst createMrpcProject(JSONObject declareJsonObj, String token) {

        Result<Claims> claimsResult = JwtUtils.verifyToken(token);
        //从token里获取用户账号信息，这里无需校验，方法请求时已通过aop进行校验
        //usermsg是username,jgdm拼接的
        String usermsg = claimsResult.getResult().getId();
        String[] split = usermsg.split(",");
        SysUser sysUser = sysUserService.getUserByName(split[0]);

        String requestcode = declareJsonObj.getString("requestcode");
        Wfi_prodef prodef = wfi_prodefService.getOne(new QueryWrapper<Wfi_prodef>().eq("PRODEFCLASS_ID", requestcode).eq("SFQY", ConstValue.SF.YES.Value));
        if(prodef == null) {
            throw new SupermapBootException("找不到业务流程信息");
        }

        JSONObject datajson = declareJsonObj.getJSONObject("data");

        Wfi_proinst newproinst = new Wfi_proinst();
        //通过存储过程获取业务号
        //iYear number,sDisCode varchar2,sBH varchar2,M_BHTYPE NVARCHAR2, lsh  out varchar2
        Map param = new HashMap<>();
        String yeartime = StringHelper.FormatDateOnType(new Date(),"yyyy");
        param.put("iYear",Integer.parseInt(yeartime));
        param.put("sDisCode",prodef.getDivisionCode());
        param.put("sBH","1");
        param.put("M_BHTYPE","1");
        param.put("lsh","0");
        proinstMapper.querypromaxid(param);
        String prolsh = param.get("lsh").toString();
        newproinst.setProdefId(prodef.getProdefId());
        newproinst.setDjlx(prodef.getDjlx());
        newproinst.setProinstId(UUID.randomUUID().toString().toLowerCase());
        newproinst.setAcceptor(sysUser.getUsername());
        newproinst.setShzt(-1);
        newproinst.setCreatDate(new Date());
        newproinst.setDivisionCode(prodef.getDivisionCode());
        newproinst.setDivisionName("");
        newproinst.setInstanceType(1);
        newproinst.setQllx(prodef.getQllx());
        newproinst.setLsh("");
        newproinst.setProlsh(prolsh);
        newproinst.setUserId(sysUser.getId());
        newproinst.setProinstStart(new Date());
        newproinst.setWfProdefid(prodef.getProdefId());
        newproinst.setWfProdefname(prodef.getProdefName());
        newproinst.setYwly(ConstValueMrpc.YWLY.JRJG.Value);
        newproinst.setOrgLsh(datajson.getString("ywlsh"));


        JSONObject qljson = datajson.getJSONObject("ql");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Bdc_ql ql = new Bdc_ql();
        ql.setProlsh(prolsh);
        ql.setProinstId(newproinst.getProinstId());
        ql.setCreateTime(new Date());
        ql.setFj(qljson.getString("fj"));
        ql.setDylx(qljson.getString("dyfs"));
        ql.setCzfs(ConstValue.CZFS.GTCZ.Value);
//        ql.setDypgjz(StringHelper.getDouble(qljson.getString("dypgjz")));
        ql.setDyje(StringHelper.getDouble(qljson.getString("zqse")));
        ql.setDjlx(prodef.getDjlx());
        ql.setQllx(prodef.getQllx());
        try {
            if (!StringHelper.isEmpty(qljson.getString("dyqlqssj"))) {
                ql.setQlqssj(sdf.parse(qljson.getString("dyqlqssj")));
            }
            if (!StringHelper.isEmpty(qljson.getString("dyqljssj"))) {
                ql.setQljssj(sdf.parse(qljson.getString("dyqljssj")));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        JSONArray dylist = datajson.getJSONArray("dylist");
        String sqrstr = "";
        String zlstr = "";
        for(int i = 0;i < dylist.size(); i++) {
            JSONObject house = dylist.getJSONObject(i);

            Bdc_dy bdcdy = new Bdc_dy();
            bdcdy.setId(StringHelper.getUUID());
            bdcdy.setBdcdyh(house.getString("bdcdyh"));
            bdcdy.setProlsh(prolsh);
            bdcdy.setZl(house.getString("zl"));
            bdcdy.setQlxz(house.getString("qlxz"));
            bdcdy.setFwyt(house.getString("fwyt"));
            bdcdy.setFwjg(house.getString("fwjg"));
            if(house.getLong("jgsj") != null) {
                bdcdy.setJgsj(new Date(house.getLong("jgsj")));
            }
            bdcdy.setYcjzmj(StringHelper.getDouble(house.getString("ycjzmj")));
            bdcdy.setYctnjzmj(StringHelper.getDouble(house.getString("yctnjzmj")));
            bdcdy.setMj(StringHelper.getDouble(house.getString("scjzmj")));
            bdcdy.setTnjzmj(StringHelper.getDouble(house.getString("sctnjzmj")));
            bdcdy.setMjdw(house.getString("mjdw"));
            bdc_dyService.save(bdcdy);
            if(i<2) {
                zlstr += bdcdy.getZl() +",";
            }

            // 附属权利，每个单元对应一条附属权利（根据 Bdc_qldy 表关联）
            Bdc_fsql fsql = new Bdc_fsql();
            fsql.setFsqlid(StringHelper.getUUID());
            fsql.setProlsh(prolsh);
            fsql.setCreateTime(new Date());
            ConstValue.BDCDYLX bdcdylx = ConstValue.BDCDYLX.initFrom(prodef.getDylx());
            fsql.setDywlx(StringHelper.getDYBDCLXfromBDCDYLX(bdcdylx));

            //构建权利关系，以申请人为主，延伸至权利，单元，附属权利
            String dyr = "";
            JSONArray sqrlist = datajson.getJSONArray("sqrlist");

            for(int j=0;j<sqrlist.size();j++) {
                JSONObject sqrobj = sqrlist.getJSONObject(j);
                if(j<2) {
                    sqrstr += sqrobj.getString("setSqrxm") +",";
                }
                Bdc_sqr sqr = new Bdc_sqr();
                sqr.setSqrxm(sqrobj.getString("sqrmc"));
                sqr.setZjlx(sqrobj.getString("sqrzjzl"));
                sqr.setZjh(sqrobj.getString("sqrzjh"));
                sqr.setLxdh(sqrobj.getString("sqrlxdh"));
//            sqr.setTxdz(sqrobj.getString("sqrmc"));
                sqr.setFddbr(sqrobj.getString("frmc"));
                sqr.setFddbrzjlx(sqrobj.getString("frzjzl"));
                sqr.setFddbrzjhm(sqrobj.getString("frzjh"));
                sqr.setDlrxm(sqrobj.getString("dlrmc"));
                sqr.setDlrzjlx(sqrobj.getString("dlrzjzl"));
                sqr.setDlrzjhm(sqrobj.getString("dlrzjh"));
                sqr.setDlrlxdh(sqrobj.getString("dlrlxdh"));
                sqr.setSqrlb(sqrobj.getString("sqrlb"));
                sqr.setSqrlx(sqrobj.getString("sqrlx"));
                sqr.setGyfs(sqrobj.getString("gyfs"));
                sqr.setBdcqzh(sqrobj.getString("bdcqzh"));
                sqr.setCreateTime(new Date());
                sqr.setProlsh(prolsh);
                sqr.setId(StringHelper.getUUID());
                bdc_sqrService.save(sqr);

                if(ConstValue.SQRLB.YF.Value.equals(sqrobj.getString("sqrlb"))) {
                    if (StringHelper.isEmpty(dyr)) {
                        dyr = sqrobj.getString("sqrmc");
                    } else {
                        dyr += "," + sqrobj.getString("sqrmc");
                    }
                }

                Bdc_qldy qldy = new Bdc_qldy();
                qldy.setProlsh(prolsh);
                qldy.setDyid(bdcdy.getId());
                qldy.setQlid(ql.getQlid());
                qldy.setSqrid(sqr.getId());
                qldy.setFsqlid(fsql.getFsqlid());
                qldy.setCreatetime(new Date());
                bdc_qldyService.save(qldy);
            }
            sqrstr = sqrstr.substring(0,sqrstr.length()-1);
            fsql.setDyr(dyr);
            bdc_fsqlService.save(fsql);
        }

        newproinst.setProjectName(sqrstr+"-"+zlstr+"-"+prodef.getProdefName());
        //创建资料目录
        JSONArray promater = datajson.getJSONArray("promater");

        for(int i = 0;i< promater.size();i++) {
            JSONObject promaterObj = promater.getJSONObject(i);
            Wfi_materclass wfi_materclass = new Wfi_materclass();
            wfi_materclass.setDivisionCode(datajson.getString("xzqdm"));
//            wfi_materclass.setFileindex(materclass.getFileindex());
            wfi_materclass.setMatedesc(promaterObj.getString("material_desc"));
            wfi_materclass.setProlsh(prolsh);
            wfi_materclass.setCount(StringHelper.getInt(promaterObj.getString("material_count")));
            wfi_materclass.setName(promaterObj.getString("material_name"));
            wfi_materclass.setProcodeid(prodef.getProdefId());
            //todo 待银行提供接口下载附件
            wfi_materclassService.save(wfi_materclass);
        }
        wfi_proinstService.save(newproinst);
        bdc_qlService.save(ql);

        return newproinst;
    }

    /***************************************** 抵押平台创建项目 end
     * @return*****************************************/


    @Override
    public Map<String, Object> applicationTokenFromDepart(String userid) {
        Map<String, Object> map = new HashMap<>();
        List<SysDepart> sysDeparts = sysDepartMapper.queryUserDeparts(userid);
        if(sysDeparts.isEmpty() || sysDeparts.size()>1) {
            throw new SupermapBootException("申报的账号隶属部门有且仅能有一个，请检查账号的部门配置");
        }

        SysDepart sysDepart = sysDeparts.get(0);
        Date now = new Date();
        Date validtime = sysDepart.getValidtime();

        //未获取token,或者token过期，则重新获取，未避免token在双方系统时间差内出现无效的情况，在有效时间结束前五分钟进行获取token
        if(StringHelper.isEmpty(sysDepart.getToken()) || (validtime!=null && ( validtime.getTime() - TIME_AHEAD < now.getTime() )) ) {
            //获取机构配置的接口地址和用户名，密码
            List<SysDepartConfig> list = sysDepartConfigService.list(new QueryWrapper<SysDepartConfig>().eq("DEPT_ID", sysDepart.getId()));
            if (list == null || list.isEmpty()) {
                throw new SupermapBootException("未配置金融机构授权接口，请联系管理员进行配置");
            }
            Map<String, SysDepartConfig> configMap = list.stream().filter(e -> !StringHelper.isEmpty(e.getType())).collect(Collectors.toMap(e -> e.getType(), o -> o));
            SysDepartConfig config = configMap.get(RequestcodeEnum.DEPT_TOKEN.Value);
            if(config == null) {
                throw new SupermapBootException("未配置金融机构授权接口，请联系管理员进行配置");
            }
            map.put("configMap", configMap);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            String format = sdf.format(new Date());
            String numbers = RandomUtil.randomNumbers(6);
            String timeflag = format + numbers;

            String data = "{\n" +
                    "\"requestcode\":"+RequestcodeEnum.DEPT_TOKEN.Value+ ",\n" +
                    "\"requestseq\":"+timeflag+",\n" +
                    "\"data\": {\n" +
                    "\"xzqdm\":"+sysDepart.getDivisionCode()+ ",\n" +
                    "\"username\":"+config.getUsername()+ ",\n" +
                    "\"appcode\":"+config.getPassword()+"\n"+
                    "}\n" +
                    "}";
            try {
                //抵押平台用私钥进行加密，银行方用抵押平台提供的公钥进行解密
                String jsonstr = HttpClientUtil.requestPost( RSACoder.encryptByPrivateKeyBase64(data), config.getValue());
                if(StringHelper.isEmpty(jsonstr)) {
                    throw new SupermapBootException("获取机构授权token失败");
                }
                //银行方用抵押平台提供的公钥进行加密，抵押平台用私钥进行解密
                String result = RSACoder.decryptByPrivateKeyBase64(jsonstr);
                JSONObject tokenjson = JSONObject.parseObject(result);
                String code = tokenjson.getString("code");
                if(!MrpccodingEnum.SUCCESS.Value.equals(code)) {
                    throw new SupermapBootException("获取机构授权token失败："+tokenjson.getString("msg"));
                }
                JSONObject tokendata = tokenjson.getJSONObject("data");
                sysDepart.setToken(tokendata.getString("token"));
                Long time = tokendata.getLong("timeout");
                Date valid = new Date(now.getTime() + time);
                sysDepart.setValidtime(valid);
                sysDepartMapper.updateById(sysDepart);
                map.put("token", tokendata.getString("token"));
                return map;

            } catch (SupermapBootException s){
                throw s;
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            //获取机构配置的接口地址和用户名，密码
            List<SysDepartConfig> list = sysDepartConfigService.list(new QueryWrapper<SysDepartConfig>().eq("DEPT_ID", sysDepart.getId()));
            if (list == null || list.isEmpty()) {
                throw new SupermapBootException("未配置金融机构授权接口，请联系管理员进行配置");
            }
            Map<String, SysDepartConfig> configMap = list.stream().filter(e -> !StringHelper.isEmpty(e.getType())).collect(Collectors.toMap(e -> e.getType(), o -> o));
            map.put("configMap", configMap);
            map.put("token", sysDepart.getToken());
            return map;
        }
        return map;
    }

    @Override
    public String DBCallBack(HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        Wfi_proinst proinst = null;
        try {
            String prolsh = request.getParameter("prolsh");
            if (StringHelper.isEmpty(prolsh)) {
                return RSACoder.resultsJsonNoRsa(MrpccodingEnum.REQUIRED.Value, null, null, "抵押平台流水号不能为空！");
            }
            proinst = wfi_proinstService.getOne(new QueryWrapper<Wfi_proinst>().eq("PROLSH", prolsh));
            if (proinst == null) {
                return RSACoder.resultsJsonNoRsa(MrpccodingEnum.NOINFORMATION.Value, null, null, "未获取到对应业务信息！");
            }
            Wfi_prodef prodef = wfi_prodefService.getById(proinst.getWfProdefid());
            List<Bdc_zs> zsList = bdc_zsService.list(new QueryWrapper<Bdc_zs>().eq("PROLSH", prolsh));
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            String format = sdf.format(new Date());
            String numbers = RandomUtil.randomNumbers(6);
            jsonObject.put("requestcode", prodef.getProdefclassId());
            jsonObject.put("requestseq", format + numbers);
            JSONArray jsonArray = new JSONArray();
            for (Bdc_zs zs : zsList) {
                if (!StringHelper.isEmpty(zs.getDbjg())) {
                    boolean isjson = StringHelper.isjson(zs.getDbjg());
                    if (isjson) {
                        Map<String, Object> map = new HashMap<String, Object>();
                        JSONObject object = JSONObject.parseObject(zs.getDbjg());
                        map.put("xzqdm", proinst.getDivisionCode());
                        map.put("jgdm", null);
                        map.put("ywlsh", proinst.getOrgLsh());
                        map.put("djywlsh", proinst.getLsh());
                        map.put("bdcdyh", object.getString("bdcdyh"));
                        map.put("bdcdylx", object.getString("bdcdylx"));
                        map.put("dyqr", object.getString("dyqr"));
                        map.put("dyqrzjlx", object.getString("zjzl"));
                        map.put("dyqrzjh", object.getString("zjhm"));
                        map.put("dyr", object.getString("dyr"));
                        map.put("dyrzjlx", object.getString("dyrzjzl"));
                        map.put("dyrzjh", object.getString("dyrzjhm"));
                        map.put("dyfs", object.getString("dyfs"));
                        map.put("djlx", object.getString("djlx"));
                        map.put("djyy", object.getString("djyy"));
                        map.put("zl", object.getString("zjjzwzl"));
                        map.put("dyfw", object.getString("zjjzwdyfw"));
                        map.put("zqse", object.getString("bdbzzqse"));
                        map.put("zwqx", object.getString("zwlxqx"));
                        map.put("bdczmh", object.getString("bdcdjzmh"));
                        map.put("djsj", object.getString("djsj"));
                        map.put("dbr", object.getString("dbr"));
                        map.put("fj", object.getString("fj"));
                        map.put("zxdyyy", object.getString("zxdyyy"));
                        map.put("zxsj", object.getString("zxsj"));
                        map.put("zxdbr", object.getString("zxdbr"));
                        map.put("zxfj", object.getString("zxfj"));
                        jsonArray.add(map);
                    }
                }
            }
            jsonObject.put("data", jsonArray);
            if (!jsonArray.isEmpty()) {
                Map<String, Object> map = applicationTokenFromDepart(proinst.getUserId());
                String token = StringHelper.formatObject(map.get("token"));
                if (StringHelper.isEmpty(token)) {
                    proinst.setHdzt(ConstValueMrpc.HDZT.TOKEN_ERROR.Value);
                    throw new SupermapBootException("获取金融机构授权token失败-流水号：" + proinst.getProlsh());
                }
                if (StringHelper.isEmpty(map.get("configMap"))) {
                    throw new SupermapBootExceptionHandler("获取推送业务登簿结果接口失败-流水号：" + proinst.getProlsh());
                }
                Map<String, SysDepartConfig> configMap = (Map<String, SysDepartConfig>) map.get("configMap");
                SysDepartConfig config = configMap.get(RequestcodeEnum.DBJGTS.Value);
                if (config == null) {
                    throw new SupermapBootExceptionHandler("未配置推送业务登簿结果接口，请联系管理员进行配置-流水号：" + proinst.getProlsh());
                }
                String jsonstr = HttpClientUtil.requestPost(RSACoder.encryptByPrivateKeyBase64(jsonObject.toJSONString()), config.getValue() + "?token=" + token);
                if (StringHelper.isEmpty(jsonstr)) {
                    throw new SupermapBootExceptionHandler("接口暂时无法访问-流水号：" + proinst.getProlsh());
                }
                String result = RSACoder.decryptByPrivateKeyBase64(jsonstr);
                JSONObject resultObject = JSONObject.parseObject(result);
                if (resultObject.isEmpty()) {
                    throw new SupermapBootExceptionHandler("接口响应为空-流水号：" + proinst.getProlsh());
                }
                String code = resultObject.getString("code");
                String msg = resultObject.getString("msg");
                if (ConstValueMrpc.MrpccodingEnum.SUCCESS.Value.equals(code)) {
                    // 推送业务登簿结果成功
                    proinst.setHdzt(ConstValueMrpc.HDZT.DBJGTS_SUCCESS.Value);
                    sysBaseAPI.addLog(ConstValueMrpc.HDZT.DBJGTS_SUCCESS.Name + "-流水号：" + proinst.getProlsh(), CommonConstant.INTERFACE_TYPE_3, null);
                    // 推送成功后更新业务状态
                    SysDepartConfig sysDepartConfig = configMap.get(RequestcodeEnum.GXYWZT.Value);
                    String desc = "更新业务状态【已登簿】";
                    UpdateBusinessStatus(proinst, prodef.getProdefclassId(), sysDepartConfig, token, desc);
                } else {
                    // 失败
                    throw new SupermapBootExceptionHandler(msg + "-流水号：" + proinst.getProlsh());
                }
            } else {
                // 证书信息或登簿结果信息为空
                throw new SupermapBootExceptionHandler("证书信息或登簿结果信息为空-流水号：" + proinst.getProlsh());
            }
            wfi_proinstService.updateById(proinst);
        } catch (SupermapBootException e) {
            if (proinst != null) {
                wfi_proinstService.updateById(proinst);
            }
            sysBaseAPI.addLog(e.getMessage(), CommonConstant.INTERFACE_TYPE_3, null);
        } catch (SupermapBootExceptionHandler e) {
            if (proinst != null) {
                proinst.setHdzt(ConstValueMrpc.HDZT.DBJGTS_ERROR.Value);
                wfi_proinstService.updateById(proinst);
            }
            sysBaseAPI.addLog(e.getMessage(), CommonConstant.INTERFACE_TYPE_3, null);
        } catch (Exception e) {
            if (proinst != null) {
                proinst.setHdzt(ConstValueMrpc.HDZT.DBJGTS_ERROR.Value);
                wfi_proinstService.updateById(proinst);
            }
            sysBaseAPI.addLog(e.getMessage(), CommonConstant.INTERFACE_TYPE_3, null);
            e.printStackTrace();
        }

        return RSACoder.resultsJsonNoRsa(MrpccodingEnum.SUCCESS.Value, null, null, null);
    }

    /**
     * @Author taochunda
     * @Description 更新业务状态信息接口
     * @Date 2019-09-10 10:52
     * @Param [resultObject, updateBusinessStatusUrl]
     * @return boolean
     **/
    private boolean UpdateBusinessStatus(Wfi_proinst proinst, String requestcode, SysDepartConfig sysDepartConfig, String token, String desc) {
        boolean flag = false;
        JSONObject object = new JSONObject();
        try {
            if (sysDepartConfig == null) {
                throw new Exception("未配置更新业务状态信息接口，请联系管理员进行配置-流水号：" + proinst.getProlsh());
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            String format = sdf.format(new Date());
            String numbers = RandomUtil.randomNumbers(6);
            object.put("requestcode", requestcode);
            object.put("requestseq", format + numbers);
            List<Map> data = new ArrayList<Map>();
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("ywlsh", proinst.getOrgLsh());
            map.put("xzqdm", proinst.getDivisionCode());
            map.put("jgdm", null);
            map.put("ywhj", "登簿");
            map.put("shzt", "已登簿");
            map.put("shyj", "");
            map.put("blryxm", proinst.getAcceptor());
            map.put("blrylxdh", null);
            map.put("bz", null);
            data.add(map);
            object.put("data", data);
            String jsonstr = HttpClientUtil.requestPost(RSACoder.encryptByPrivateKeyBase64(object.toJSONString()), sysDepartConfig.getValue() + "?token=" + token);
            if (StringHelper.isEmpty(jsonstr)) {
                throw new Exception("接口暂时无法访问-流水号：" + proinst.getProlsh());
            }
            String result = RSACoder.decryptByPrivateKeyBase64(jsonstr);
            JSONObject jsonObject = JSONObject.parseObject(result);
            if (jsonObject.isEmpty()) {
                throw new Exception("接口响应为空-流水号：" + proinst.getProlsh());
            }
            String code = jsonObject.getString("code");
            if (ConstValueMrpc.MrpccodingEnum.SUCCESS.Value.equals(code)) {
                flag = true;
                proinst.setHdzt(ConstValueMrpc.HDZT.GXYWZT_SUCCESS.Value);
                wfi_proinstService.updateById(proinst);
                sysBaseAPI.addLog(desc + "信息成功！-流水号：" + proinst.getProlsh(), CommonConstant.INTERFACE_TYPE_3, null);
            } else {
                throw new Exception("-流水号：" + proinst.getProlsh());
            }
        } catch (Exception ex) {
            proinst.setHdzt(ConstValueMrpc.HDZT.GXYWZT_ERROR.Value);
            wfi_proinstService.updateById(proinst);
            sysBaseAPI.addLog(desc + "信息失败！" + ex.getMessage(), CommonConstant.INTERFACE_TYPE_3, null);
            ex.printStackTrace();
        }
        wfi_proinstService.updateById(proinst);
        return flag;
    }
}
