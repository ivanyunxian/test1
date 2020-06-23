package org.jeecg.modules.mortgagerpc.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.shiro.SecurityUtils;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.exception.SupermapBootException;
import org.jeecg.common.system.base.service.ISystemDictService;
import org.jeecg.common.system.util.HttpClientUtil;
import org.jeecg.common.system.util.StringHelper;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.AsyncTaskConfig;
import org.jeecg.common.util.ConstValue;
import org.jeecg.common.util.ConstValueMrpc;
import org.jeecg.modules.mortgagerpc.entity.*;
import org.jeecg.modules.mapper.Wfi_proinstMapper;
import org.jeecg.modules.mortgagerpc.mongo.serviece.MongoServerce;
import org.jeecg.modules.mortgagerpc.service.*;
import org.jeecg.modules.mortgagerpc.task.AsyncTaskService;
import org.jeecg.modules.system.entity.SysUser;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sun.media.jfxmedia.logging.Logger;

import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description: 项目信息
 * @Author: jeecg-boot
 * @Date:   2019-07-29
 * @Version: V1.0
 */
@Service
public class Wfi_proinstServiceImpl extends ServiceImpl<Wfi_proinstMapper, Wfi_proinst> implements IWfi_proinstService {

    @Autowired
    Wfi_proinstMapper proinstMapper;

    @Autowired
    IBdc_dyService bdc_dyService;

    @Autowired
    IBdc_qlService bdc_qlService;

    @Autowired
    IWfi_prodefService wfi_prodefService;

    @Autowired
    private IBdc_sqrService bdc_sqrService;

    @Autowired
    private IWfi_prodefService getWfi_prodefService;

    @Autowired
    private IBdc_qldyService bdc_qldyService;

    @Autowired
    ISystemDictService systemDictService;

    @Autowired
    IWfd_materclassService wfd_materclassService;

    @Autowired
    IWfi_materclassService wfi_materclassService;

    @Autowired
    IWfi_materdataService wfi_materdataService;

    @Autowired
    ISys_configService sys_configService;

    @Autowired
    IHouseshisService houseshisService;

    @Autowired
    IBdc_fsqlService bdc_fsqlService;

    @Autowired
    MongoServerce mongoserverce;

    @Autowired
    IBdc_last_qlService bdc_last_qlService;

    @Autowired
    private IBdcs_sfdyService iBdcs_sfdyService;

    @Override
    public IPage<Map> projectlist(Page<Map> page, HttpServletRequest request) {
        Map<String, String> param = new HashMap<String,String>();
        param.put("prolsh", StringHelper.trim(request.getParameter("prolsh"))) ;
        param.put("shzt", StringHelper.trim(request.getParameter("shzt"))) ;
        param.put("projectName", StringHelper.trim(request.getParameter("projectName"))) ;
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        param.put("userid",sysUser.getId());

        return page.setRecords(proinstMapper.projectlist(page, param));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveproject(JSONObject projectDataJson) throws Exception {
        String prolsh = projectDataJson.getString("prolsh");//申请流水号
        Wfi_proinst wfi_proinst = getOne(new QueryWrapper<Wfi_proinst>().eq("prolsh", prolsh));
        if(wfi_proinst ==  null) {
            throw new SupermapBootException("未找到对应的项目信息-流水号："+prolsh);
        }
        boolean canEdit = canEdit(wfi_proinst);
        if(canEdit) {
            throw new SupermapBootException("该项目已经提交，不能重复操作");
        }
        JSONObject deptjson = projectDataJson.getJSONObject("dept");//申请机构信息
        JSONObject qljson = projectDataJson.getJSONObject("ql");//申请权利信息
        JSONArray dyarrys = projectDataJson.getJSONArray("dyarrys");//单元列表

        wfi_proinst.setDepartid(deptjson.getString("deptid"));
        wfi_proinst.setSfhbzs(qljson.getString("sfhbzs"));

        //插入前先删掉旧数据
        bdc_qlService.remove(new QueryWrapper<Bdc_ql>().eq("prolsh", prolsh));
        bdc_dyService.remove(new QueryWrapper<Bdc_dy>().eq("prolsh", prolsh));

        Wfi_prodef prodef = wfi_prodefService.getById(wfi_proinst.getWfProdefid());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Bdc_ql ql = new Bdc_ql();
        ql.setProlsh(prolsh);
        ql.setProinstId(wfi_proinst.getProinstId());
        ql.setCreateTime(new Date());
        ql.setFj(qljson.getString("fj"));
        ql.setCzfs(qljson.getString("czfs"));
        ql.setDylx(qljson.getString("dylx"));
        ql.setDypgjz(StringHelper.getDouble(qljson.getString("dypgjz")));
        ql.setDyje(StringHelper.getDouble(qljson.getString("dyje")));
        ql.setDjlx(prodef.getDjlx());
        ql.setQllx(prodef.getQllx());
        if(!StringHelper.isEmpty(qljson.getString("qlsj"))) {
            ql.setQlqssj(sdf.parse(qljson.getString("qlqssj")));
            ql.setQljssj(sdf.parse(qljson.getString("qljssj")));
        }
        bdc_qlService.save(ql);

        for (int j = 0; j < dyarrys.size(); j++) {
            JSONObject dy = JSON.parseObject(dyarrys.get(j).toString());
            Bdc_dy bdc_dy = new Bdc_dy();
            bdc_dy.setBdcdyh(dy.getString("bdcdyh"));
            bdc_dy.setCreatetime(new Date());
            bdc_dy.setZl(dy.getString("zl"));
            bdc_dy.setMj(StringHelper.getDouble(dy.getString("mj")));
            bdc_dy.setProlsh(prolsh);
            bdc_dy.setDgbdbzzqse(StringHelper.getDouble(dy.getString("dgbdbzzqse")));
            bdc_dyService.save(bdc_dy);
        }

        saveOrUpdate(wfi_proinst);

    }


    @Override
    public Map<String, Object> getProjectMessage(String prolsh, String dyid) {
        JSONObject map = new JSONObject();
        Wfi_proinst wfi_proinst = getOne(new QueryWrapper<Wfi_proinst>().eq("PROLSH", prolsh));
        if (wfi_proinst == null) {
            throw new SupermapBootException("未找到对应的项目信息-流水号：" + prolsh);
        }
        Wfi_prodef prodef = getWfi_prodefService.getById(wfi_proinst.getWfProdefid());
        //根据关系表查出各种信息,目前这里就只有权利及附件的信息
        List<Bdc_ql> bdcqls = bdc_qlService.getQlBydyid(prolsh, dyid);
        // 根据关系表查出附属权利
        List<Bdc_fsql> bdcFsqls = bdc_fsqlService.getFsqlBydyid(prolsh, dyid);

        List<Wfi_materclass> Wfi_materclass = wfi_materclassService.getMaterlist(prolsh);

        Bdc_last_ql lastql = bdc_last_qlService.getOne(new QueryWrapper<Bdc_last_ql>().eq("PROLSH", prolsh).eq("DYID", dyid));

        SysUser sysUser = proinstMapper.getSysuser(wfi_proinst.getUserId());
        map.put("proinst", wfi_proinst);
        map.put("prodef",prodef);
        map.put("bdcql", bdcqls);
        map.put("bdcfsql", bdcFsqls);
        map.put("lastql", lastql);//抵押变更前权利
        map.put("materclass", Wfi_materclass);
        return map;
    }

    @Override
    public void submitProject(String prolsh) {
        submitProject(prolsh, ConstValueMrpc.YWLY.DYPT.Value);
    }

    /**
     * 提交项目，在此进行数据完整性校验
     * 有异常抛出SupermapBootException
     * @param prolsh
     */
    @Override
    public void submitProject(String prolsh, String type) {
        if (StringHelper.isEmpty(prolsh)) {
            throw new SupermapBootException("未获取到项目流水号-流水号：" + prolsh);
        }
        Wfi_proinst proinst = getOne(new QueryWrapper<Wfi_proinst>().eq("PROLSH", prolsh));
        List<Bdc_sqr> sqrs = bdc_sqrService.list(new QueryWrapper<Bdc_sqr>().eq("PROLSH", prolsh));
        List<Bdc_dy> bdcdys = bdc_dyService.list(new QueryWrapper<Bdc_dy>().eq("PROLSH", prolsh));
        List<Bdc_ql> bdcqls = bdc_qlService.list(new QueryWrapper<Bdc_ql>().eq("PROLSH", prolsh));
        List<Bdc_fsql> fsqls = bdc_fsqlService.list(new QueryWrapper<Bdc_fsql>().eq("PROLSH", prolsh));

        List<Wfi_materclass> wfiMaterclassList = wfi_materclassService.list(new QueryWrapper<Wfi_materclass>().eq("PROLSH", prolsh));
        List<Wfi_materdata> wfiMaterdataList = wfi_materdataService.list(new QueryWrapper<Wfi_materdata>().eq("PROLSH", prolsh));
        //收费项根据单元个数进行改造
        List<Bdcs_sfdy> bdcs_sfdys = iBdcs_sfdyService.list(new QueryWrapper<Bdcs_sfdy>().inSql("id", "SELECT SFDY_ID ID FROM BDC_MRPC.BDCS_SFGX WHERE YWLSH = '"+prolsh+"'"));
        List<Bdc_qldy> bdcqldys = bdc_qldyService.list(new QueryWrapper<Bdc_qldy>().eq("prolsh", proinst.getProlsh()));
        //单元数量
        BigDecimal dycount = new  BigDecimal(Integer.toString(bdcdys.size()));
        for(Bdcs_sfdy bdcs_sfdy:bdcs_sfdys) {
        	BigDecimal sfjs=new BigDecimal("0");
        	if(bdcs_sfdy.equals("f0e8837a8adb46488145ea6580c72619")) {
        		int gbfzzcount=0;
        		 List<Bdc_sqr> qlrs = bdc_sqrService.list(new QueryWrapper<Bdc_sqr>().eq("PROLSH", prolsh).eq("sqrlb", "1"));
        		 Map<String, Bdc_sqr> qlrsMap = qlrs.stream().collect(Collectors.toMap(e -> e.getId(), o -> o));
        		 for(Bdc_ql ql:bdcqls) {
                 	int qlrcount =0;
                 	for(Bdc_qldy qldy:bdcqldys) {
                 		//循环判断：1.对应分别持证的权利 2.申请人在权利人列表中
                 		if(ql.getQlid().equals(qldy.getQlid())&&qlrsMap.containsKey(qldy.getSqrid())) {
                 			qlrcount++;
                 		}
                 	}
                 	if(qlrcount>1) {
                 		gbfzzcount=gbfzzcount+qlrcount-1;
                 	}
                 }
        		 sfjs =new BigDecimal(Integer.toString(gbfzzcount)).multiply(bdcs_sfdy.getSfjs());
        	}else {
        		sfjs= bdcs_sfdy.getSfjs().multiply(dycount);
        	}
             
             bdcs_sfdy.setSfjs(sfjs);
        }

        boolean canEdit = canEdit(proinst);
        if(canEdit) {
            throw new SupermapBootException("该项目已经提交，不能重复提交");
        }

        if (proinst == null) {
            throw new SupermapBootException("未找到对应的项目信息-流水号：" + prolsh);
        }
        if (sqrs.isEmpty()) {
            throw new SupermapBootException("未找到对应的申请人信息-流水号：" + prolsh);
        }
        if (bdcdys.isEmpty()) {
            throw new SupermapBootException("未找到对应的单元信息-流水号：" + prolsh);
        }
        if (bdcqls == null||bdcqls.size()<=0) {
            throw new SupermapBootException("未找到对应的权利信息-流水号：" + prolsh);
        }
        if (StringHelper.isEmpty(proinst.getWfProdefid())) {
            throw new SupermapBootException("获取流程ID异常，请联系管理员-流水号：" + prolsh);
        }

        Wfi_prodef prodef = wfi_prodefService.getById(proinst.getWfProdefid());
        if (prodef == null) {
            throw new SupermapBootException("未找到对应的流程信息-流水号：" + prolsh);
        }

        // 核验申报信息必填项
        checkProjectInfo(proinst, sqrs, bdcdys, bdcqls, prodef, wfiMaterclassList, wfiMaterdataList);

        // 获取完整申报信息
        String jsonString = getJSONString(proinst, sqrs, bdcdys, bdcqls, prodef, wfiMaterclassList, wfiMaterdataList, fsqls, bdcs_sfdys,bdcqldys);

        // 获取申报接口地址
        String declareUrl = sys_configService.getConfigByKey("declareUrl");
        if(StringHelper.isEmpty(declareUrl)) {
            throw new SupermapBootException("未配置业务申报接口地址，请联系管理员进行配置！");
        }
        // 异步调用申报接口，使用线程池管理
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AsyncTaskConfig.class);
        AsyncTaskService taskService = context.getBean(AsyncTaskService.class);
        if (ConstValueMrpc.YWLY.DYPT.Value.equals(type)) {
            taskService.AsyncDeclare(proinst, jsonString, declareUrl);
        } else if (ConstValueMrpc.YWLY.JRJG.Value.equals(type)) {
            taskService.AsyncDeclareProject(prodef.getProdefclassId(), proinst, jsonString, declareUrl);
        }

        // 申报成功，修改审核状态（-1：未提交，0：待审核，11：审核驳回，20：初审通过，30：已登簿）
        proinst.setShzt(0);
        saveOrUpdate(proinst);

    }

    public String getJSONString(Wfi_proinst proinst, List<Bdc_sqr> bdc_sqrs, List<Bdc_dy> bdcdys, List<Bdc_ql> bdcqls, Wfi_prodef prodef, List<Wfi_materclass> wfiMaterclassList, List<Wfi_materdata> wfiMaterdataList, List<Bdc_fsql> fsqls, List<Bdcs_sfdy> bdcs_sfdys, List<Bdc_qldy> bdcqldys) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("requestcode", prodef.getProdefclassId());
        jsonObject.put("requestseq", System.currentTimeMillis());
        Map<String, Object> map = new HashMap<String, Object>();
        // 项目主体
        map.put("YWLSH", proinst.getProlsh());
        map.put("QXDM", proinst.getDivisionCode());
        map.put("DJLX", proinst.getDjlx());
        map.put("QLLX", proinst.getQllx());
        map.put("YWLCMC", proinst.getProjectName());
        map.put("SFHBZS", proinst.getSfhbzs());
        map.put("REMARKS", proinst.getRemarks());
        // 流程主体
        map.put("BDCDYLX", prodef.getDylx());
        map.put("YWLCID", prodef.getProdefCode());
        // 申请人
        
        Map<String, Bdc_fsql> fsqlMap = fsqls.stream().collect(Collectors.toMap(e -> e.getFsqlid(), o -> o));
        Map<String, Bdc_ql> qlMap = bdcqls.stream().collect(Collectors.toMap(e -> e.getQlid(), o -> o));
        Map<String, Bdc_dy> bdcDyMap = bdcdys.stream().collect(Collectors.toMap(e -> e.getId(), o -> o));
        JSONArray array_sqr = new JSONArray();
        for (Bdc_sqr sqr : bdc_sqrs) {
            Map<String, Object> map_sqr = new HashMap<String, Object>();
            map_sqr.put("SQRMC", sqr.getSqrxm());
            map_sqr.put("SQRZJH", sqr.getZjh());
            map_sqr.put("SQRZJZL", sqr.getZjlx());
            map_sqr.put("SQRLXDH", sqr.getLxdh());
            map_sqr.put("DLRMC", sqr.getDlrxm());
            map_sqr.put("DLRZJH", sqr.getDlrzjhm());
            map_sqr.put("DLRZJZL", sqr.getDlrzjlx());
            map_sqr.put("DLRLXDH", sqr.getDlrlxdh());
            map_sqr.put("FRMC", sqr.getFddbr());
            map_sqr.put("FRZJH", sqr.getFddbrzjhm());
            map_sqr.put("FRZJZL", sqr.getFddbrzjlx());
            map_sqr.put("FRLXDH", "");
            map_sqr.put("GYFS", sqr.getGyfs());
            map_sqr.put("QLBL", sqr.getQlbl());
            map_sqr.put("SQRLB", sqr.getSqrlb());
            map_sqr.put("SQRLX", sqr.getSqrlx());
            map_sqr.put("SXH", "");
            map_sqr.put("BDCQZH", sqr.getBdcqzh());
            for(Bdc_qldy qldy:bdcqldys) {
            	if(qldy.getSqrid().equals(sqr.getId())) {
            		if(bdcDyMap.containsKey(qldy.getDyid())) {
            map_sqr.put("BDCDYH", bdcDyMap.get(qldy.getDyid()).getBdcdyh());
            		}
            		
            	}
            }
            array_sqr.add(map_sqr);
        }
        map.put("SQRLIST", array_sqr);

        // 抵押单元,
        JSONArray array_unit = new JSONArray();
        List<String> dyid = new ArrayList<String>();
        for (Bdc_qldy qldy : bdcqldys) {
            Map<String, Object> unit = new HashMap<String, Object>();
            if (dyid.contains(qldy.getDyid())) {
                continue;
            }
            if (bdcDyMap.containsKey(qldy.getDyid())) {
                dyid.add(qldy.getDyid());
                unit.put("BDCDYH", bdcDyMap.get(qldy.getDyid()).getBdcdyh());
                unit.put("ZL", bdcDyMap.get(qldy.getDyid()).getZl());
                unit.put("DGBDBZZQSE", bdcDyMap.get(qldy.getDyid()).getDgbdbzzqse());
            }
            // 由于附属权利与单元是一一对应，所以附属权利相关字段也放到单元节点
            if (fsqlMap.containsKey(qldy.getFsqlid())) {
                unit.put("DYR", fsqlMap.get(qldy.getFsqlid()).getDyr());
                unit.put("DGBDBZZQSE", fsqlMap.get(qldy.getFsqlid()).getDgbdbzzqse());
                unit.put("ZGZQQDSS", fsqlMap.get(qldy.getFsqlid()).getZgzqqdss());
                unit.put("ZJJZWDYFW", fsqlMap.get(qldy.getFsqlid()).getZjjzwdyfw());
                unit.put("ZWR", fsqlMap.get(qldy.getFsqlid()).getZwr());
                unit.put("ZWRZJH", fsqlMap.get(qldy.getFsqlid()).getZwrzjh());
                unit.put("FWXZ", fsqlMap.get(qldy.getFsqlid()).getFwxz());
            }
         // 由于权利与单元是一一对应，所以权利相关字段也放到单元节点
            if (qlMap.containsKey(qldy.getQlid())) {
                unit.put("DYFS", qlMap.get(qldy.getQlid()).getDylx());
                unit.put("CZFS", qlMap.get(qldy.getQlid()).getCzfs());
                unit.put("ZQSE", qlMap.get(qldy.getQlid()).getDyje());
                unit.put("DYPGJZ", qlMap.get(qldy.getQlid()).getDypgjz());
                unit.put("FJ", qlMap.get(qldy.getQlid()).getFj());
                unit.put("DJYY", qlMap.get(qldy.getQlid()).getDjyy());
                unit.put("ZXDJYY", qlMap.get(qldy.getQlid()).getZxdyyy());
                unit.put("ZXFJ", qlMap.get(qldy.getQlid()).getZxfj());
                unit.put("ZQDW", qlMap.get(qldy.getQlid()).getZqdw());
                unit.put("ZSBS", qlMap.get(qldy.getQlid()).getZsbs());
                unit.put("TDSHYQR", qlMap.get(qldy.getQlid()).getTdshyqr());
                unit.put("HTH", qlMap.get(qldy.getQlid()).getHth());
                unit.put("QDJG", qlMap.get(qldy.getQlid()).getQdjg());
                unit.put("QLQSSJ", qlMap.get(qldy.getQlid()).getQlqssj());
                unit.put("QLJSSJ", qlMap.get(qldy.getQlid()).getQljssj());
            }
            array_unit.add(unit);
        }
        /*for (Bdc_dy bdcdy : bdcdys) {
            Map<String, Object> unit = new HashMap<String, Object>();
            unit.put("BDCDYH", bdcdy.getBdcdyh());
            unit.put("ZL", bdcdy.getZl());
            unit.put("DGBDBZZQSE", bdcdy.getDgbdbzzqse());
            unit.put("DYQLQSSJ", bdcql.getQlqssj());
            unit.put("DYQLJSSJ", bdcql.getQljssj());
            array_unit.add(unit);
        }*/
        map.put("DYDYLIST", array_unit);

        //登记收费项
        map.put("DJSFS", bdcs_sfdys);

        // 资料目录实例
        map.put("MATERCLASS", wfiMaterclassList);
        // 资料文件
        map.put("MATERDATA", wfiMaterdataList);

        jsonObject.put("data", map);

        return jsonObject.toJSONString();
    }

    public void checkProjectInfo(Wfi_proinst proinst, List<Bdc_sqr> sqrs, List<Bdc_dy> bdcdys, List<Bdc_ql> bdcqls, Wfi_prodef prodef, List<Wfi_materclass> wfiMaterclassList, List<Wfi_materdata> wfiMaterdataList) {
        String prolsh = proinst.getProlsh();
        if (StringHelper.isEmpty(proinst.getDjlx())) {
            throw new SupermapBootException("获取登记类型异常，请联系管理员-流水号：" + prolsh);
        }
        if (StringHelper.isEmpty(proinst.getQllx())) {
            throw new SupermapBootException("获取权利类型异常，请联系管理员-流水号：" + prolsh);
        }
        if (StringHelper.isEmpty(proinst.getDivisionCode())) {
            throw new SupermapBootException("获取区划代码异常，请联系管理员-流水号：" + prolsh);
        }
        if (StringHelper.isEmpty(prodef.getProdefCode())) {
            // 对应登记系统 prodef_id
            throw new SupermapBootException("获取流程编码异常，请联系管理员-流水号：" + prolsh);
        }
        if (StringHelper.isEmpty(prodef.getProdefclassId())) {
            // 具体办理业务的业务编码，如：2001、2002
            throw new SupermapBootException("获取业务编码异常，请联系管理员-流水号：" + prolsh);
        }
        if (StringHelper.isEmpty(prodef.getDylx())) {
            // 不动产单元类型
            throw new SupermapBootException("获取单元类型异常，请联系管理员-流水号：" + prolsh);
        }

        if (!wfiMaterclassList.isEmpty()) {
            // 必要材料目录集合
            List<Wfi_materclass> materclassList = wfiMaterclassList.stream().filter(e -> "1".equals(e.getRequired()))
                    .collect(Collectors.toList());
            // 所有已上传文件目录ID分组集合
            Set<String> strings = wfiMaterdataList.stream().collect(Collectors.groupingBy(e -> e.getMaterinstId())).keySet();
            if (!materclassList.isEmpty()) {
                String name = "";
                if (strings.isEmpty()) {
                    name = materclassList.stream().map(e -> e.getName()).collect(Collectors.joining("、"));
                } else {
                    // 已上传文件的目录ID不包含必要目录ID，则是未上传
                    name = materclassList.stream().filter(e -> !strings.contains(e.getId())).map(e -> e.getName()).collect(Collectors.joining("、"));
                }
                if (!StringHelper.isEmpty(name)) {
                    throw new SupermapBootException(String.format("附件资料未上传：【%s】是必须的", name));
                }
            }
        }

        // 根据业务类型校验节点具体必填项，目前只是简单校验一下申请人
        String prodefclassId = prodef.getProdefclassId();
        if (prodefclassId.contains(ConstValueMrpc.RequestcodeEnum.DYDJ.Value)) {
            checkSqr(sqrs, false);
        } else if (prodefclassId.contains(ConstValueMrpc.RequestcodeEnum.DYBG.Value)) {
            checkSqr(sqrs, true);
        } else if (prodefclassId.contains(ConstValueMrpc.RequestcodeEnum.DTZY.Value)) {
            checkSqr(sqrs, false);
        } else if (prodefclassId.contains(ConstValueMrpc.RequestcodeEnum.DYZX.Value)) {
            checkSqr(sqrs, true);
        } else if (prodefclassId.contains(ConstValueMrpc.RequestcodeEnum.XJSPFZYDY.Value)) {
            checkSqr(sqrs, false);
        } else if (prodefclassId.contains(ConstValueMrpc.RequestcodeEnum.CLFZYDY.Value)) {
            checkSqr(sqrs, false);
        } else if(prodefclassId.contains(ConstValueMrpc.RequestcodeEnum.SPFZY.Value)){
        	checkSqr(sqrs, false);
        }else{
            throw new SupermapBootException("该业务类型不支持，请联系管理员-流水号：" + proinst.getProlsh());
        }
    }

    private void checkSqr(List<Bdc_sqr> sqrs, boolean flag) {
        for (Bdc_sqr sqr : sqrs) {
            if (StringHelper.isEmpty(sqr.getSqrxm())) {
                throw new SupermapBootException("申请人姓名不能为空");
            }
            if (StringHelper.isEmpty(sqr.getZjh())) {
                throw new SupermapBootException(String.format("申请人【%s】证件号 是必须的", sqr.getSqrxm()));
            }
            if (flag && ConstValue.SQRLB.JF.Value.equals(sqr.getSqrlb())) {
                if (StringHelper.isEmpty(sqr.getBdcqzh())) {
                    throw new SupermapBootException(String.format("权利人【%s】不动产权证号/证明 是必须的", sqr.getSqrxm()));
                }
            }
            if (StringHelper.isEmpty(sqr.getZjlx())) {
                throw new SupermapBootException(String.format("申请人【%s】证件类型 是必须的", sqr.getSqrxm()));
            }
            if (StringHelper.isEmpty(sqr.getGyfs())) {
                throw new SupermapBootException(String.format("申请人【%s】共有情况 是必须的", sqr.getSqrxm()));
            }
        }
    }

    @Override
    public boolean canEdit(Wfi_proinst wfi_proinst) {
        Integer shzt = wfi_proinst.getShzt();
        if (shzt != -1 || shzt != 11) {
            return false;
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteProject(Wfi_proinst wfi_proinst) {
        bdc_qlService.remove(new QueryWrapper<Bdc_ql>().eq("prolsh", wfi_proinst.getProlsh()));
        bdc_dyService.remove(new QueryWrapper<Bdc_dy>().eq("prolsh", wfi_proinst.getProlsh()));
        bdc_sqrService.remove(new QueryWrapper<Bdc_sqr>().eq("prolsh", wfi_proinst.getProlsh()));
        bdc_fsqlService.remove(new QueryWrapper<Bdc_fsql>().eq("prolsh", wfi_proinst.getProlsh()));
        bdc_qldyService.remove(new QueryWrapper<Bdc_qldy>().eq("prolsh", wfi_proinst.getProlsh()));
        List<Wfi_materdata> wfiMaterdataList = wfi_materdataService.list(new QueryWrapper<Wfi_materdata>().eq("PROLSH", wfi_proinst.getProlsh()));
        for (Wfi_materdata materdata : wfiMaterdataList) {
            mongoserverce.delete(materdata.getMongoid());
        }
        wfi_materdataService.remove(new QueryWrapper<Wfi_materdata>().eq("prolsh", wfi_proinst.getProlsh()));
        wfi_materclassService.remove(new QueryWrapper<Wfi_materclass>().eq("prolsh", wfi_proinst.getProlsh()));
        removeById(wfi_proinst.getProinstId());
    }

    @Override
    public void houseSearch(Map<String, String> param) {

        String prolsh = param.get("prolsh");
        String coreQueryUrl = sys_configService.getConfigByKey("coreQueryUrl");
        if(StringHelper.isEmpty(coreQueryUrl)) {
            throw new SupermapBootException("未配置房源核验接口地址，请联系管理员进行配置");
        }
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String format = sdf.format(new Date());
        String numbers = RandomUtil.randomNumbers(6);
        String timeflag = format + numbers;
        // 请求JSON数据组装
        JSONObject object = new JSONObject();
        object.put("requestcode", ConstValueMrpc.RequestcodeEnum.FW.Value);
        object.put("requestseq", timeflag);
        JSONArray dataArray = new JSONArray();
        Map<String, Object> map = new HashMap<>();
        map.put("bdcqzh", param.get("bdcqzh"));
        // 内部调用，使用机构代码为90
        map.put("jgdm", "90");
        map.put("xzqdm", sysUser.getDivisionCode());
        JSONArray qlrArray = new JSONArray();
        Map<String, Object> qlrMap = new HashMap<>();
        qlrMap.put("qlrmc", param.get("qlrmc"));
        qlrMap.put("zjhm", param.get("qlrzjh"));
        qlrArray.add(qlrMap);
        map.put("qlr", qlrArray);
        dataArray.add(map);
        object.put("data", dataArray);
        map.put("username", sysUser.getUsername());
        String jsonstr = HttpClientUtil.requestPost(object.toJSONString(), coreQueryUrl);

        if(StringHelper.isEmpty(jsonstr)) {
            throw new SupermapBootException("房源核验接口暂时无法访问，请联系管理员排查。");
        }
        JSONObject jsonObject = JSONObject.parseObject(jsonstr);
        if(jsonObject == null) {
            throw new SupermapBootException("房源核验接口异常，返回为空，请联系管理员排查");
        }

        //解析接口返回的数据，并保存到房源核验记录表中，前端显示房源核验直接读表，方便做分页及记录
        //先将该流水号上一次核验出来的数据设置为已失效
        Houseshis his = new Houseshis();
        his.setFyzt(3);
        houseshisService.update(his, new UpdateWrapper<Houseshis>().eq("PROLSH",prolsh));

        JSONArray datalist = jsonObject.getJSONArray("data");
        if(datalist == null) {
            throw new SupermapBootException("查无房源信息，请检查数据是否正确");
        }
        for(int i=0;i<datalist.size();i++) {
            JSONObject data = datalist.getJSONObject(i);
            JSONArray houselist = data.getJSONArray("houselist");
            if(houselist == null || houselist.isEmpty()) {
                throw new SupermapBootException("查无房源信息，请检查数据是否正确");
            }
            JSONArray qlrlist = data.getJSONArray("qlrlist");
            JSONArray msrlist = data.getJSONArray("msrlist");
            JSONArray cqlist = data.getJSONArray("cqlist");
            for(int j=0;j<houselist.size();j++) {
                JSONObject house = houselist.getJSONObject(j);
                Houseshis househis = new Houseshis();
                househis.setBdcdyh(house.getString("bdcdyh"));
                househis.setProlsh(prolsh);
                househis.setZl(house.getString("zl"));
                househis.setYcjzmj(house.getDouble("ycjzmj"));
                househis.setYctnjzmj(house.getDouble("yctnjzmj"));
                househis.setScjzmj(house.getDouble("scjzmj"));
                househis.setSctnjzmj(house.getDouble("sctnjzmj"));
                househis.setFttdmj(house.getDouble("fttdmj"));
                househis.setZdmj(house.getDouble("zdmj"));
                househis.setCreatetime(new Date());
                househis.setOperator(sysUser.getRealname());
                JSONArray cflist = house.getJSONArray("cflist");
                if(cflist!=null && cflist.size()>0) {
                    househis.setCfzt("1");
                }
                JSONArray dylist = house.getJSONArray("dylist");
                if(dylist!=null && dylist.size()>0) {
                    househis.setDyzt("1");
                }
                JSONArray yylist = house.getJSONArray("yylist");
                if(yylist!=null && yylist.size()>0) {
                    househis.setYyzt("1");
                }

                JSONObject json = new JSONObject();	
                json.put("house", house);
                json.put("qlrlist", qlrlist);
                json.put("msrlist",msrlist);	
                json.put("cqlist",cqlist);
                json = systemDictService.transForJson(json);
                househis.setHouseclob(json.toJSONString());
                houseshisService.save(househis);
            }
        }
//            JSONObject resultObject = systemDictService.transForJson(jsonObject);

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String createProject(JSONObject projectData) {
        String prodefid = projectData.getString("prodefid");
        JSONArray qlrlist = projectData.getJSONArray("qlrlist");
        JSONArray dylist = projectData.getJSONArray("dylist");

        Wfi_prodef prodef = wfi_prodefService.getById(prodefid);
        if(prodef == null){
            throw new SupermapBootException("找不到所选的流程信息，请联系管理员核查");
        }
        if(qlrlist.size()==0) {
            throw new SupermapBootException("无法找到单元所属权利人信息，请联系管理员核查房源信息接口及数据是否异常");
        }
        if(dylist.size()==0) {
            throw new SupermapBootException("无法获取当前选择的单元");
        }


        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
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
        newproinst.setYwly(ConstValueMrpc.YWLY.DYPT.Value);

        String qlrstr = "";
        for(int i=0;i<qlrlist.size();i++) {
            JSONObject qlr = qlrlist.getJSONObject(i);
            if(i<2) {
                qlrstr += qlr.getString("qlrmc") +",";
            }
            //房源携带的权利人，作为本次登记的义务人
            Bdc_sqr sqr = new Bdc_sqr();
            sqr.setProlsh(prolsh);
            sqr.setCreateTime(new Date());
            sqr.setSqrxm(qlr.getString("qlrmc"));
            sqr.setZjlx(qlr.getString("zjlx"));
            sqr.setZjh(qlr.getString("zjh"));
            sqr.setLxdh(qlr.getString("lxdh"));
            sqr.setTxdz(qlr.getString("txdz"));
            sqr.setBdcqzh(qlr.getString("bdcqzh"));
            sqr.setGyfs(qlr.getString("gyfs"));
            sqr.setSqrlb("2");
            bdc_sqrService.save(sqr);
        }
        qlrstr = qlrstr.substring(0,qlrstr.length()-1);

//        //一般的登记，银行作为权利人存放，不过在组合流程中，属于抵押权人，这个后续支持组合流程时再适配
//        Map<String, String> userDepart = proinstMapper.getUserDepart(sysUser.getDeptId());
//        if(userDepart != null ) {
//            Bdc_sqr qlr = new Bdc_sqr();
//            qlr.setSqrlb("1");
//            qlr.setSqrxm(userDepart.get("NAME"));
//            qlr.setZjlx(userDepart.get("ZJLX"));
//            qlr.setZjh(userDepart.get("ZJH"));
//            qlr.setLxdh(userDepart.get("NAME"));
//            qlr.setTxdz(userDepart.get("TXDZ"));
//            qlr.setDlrxm(sysUser.getRealname());
//            qlr.setZjlx(sysUser.getZjlx());
//            qlr.setZjh(sysUser.getZjh());
//            //共有方式默认为单独所有
//            qlr.setGyfs("0");
//            qlr.setCreateTime(new Date());
//            qlr.setProlsh(prolsh);
//            bdc_sqrService.save(qlr);
//        }


        String zlstr = "";
        for(int i=0;i<dylist.size();i++) {
            JSONObject dy = dylist.getJSONObject(i);
            if(i<2) {
                zlstr += dy.getString("zl") +",";
            }

            Bdc_dy bdcdy = new Bdc_dy();
            bdcdy.setBdcdyh(dy.getString("bdcdyh"));
            bdcdy.setProlsh(prolsh);
            bdcdy.setZl(dy.getString("zl"));
            bdcdy.setQlxz(dy.getString("qlxz"));
            bdcdy.setFwyt(dy.getString("fwyt"));
            bdcdy.setFwjg(dy.getString("fwjg"));
//            bdcdy.setJgsj(dy.getString("JGSJ"));确定时间格式先
            bdcdy.setYcjzmj(StringHelper.getDouble(dy.getString("ycjzmj")));
            bdcdy.setYctnjzmj(StringHelper.getDouble(dy.getString("yctnjzmj")));
            bdcdy.setMj(StringHelper.getDouble(dy.getString("scjzmj")));
            bdcdy.setTnjzmj(StringHelper.getDouble(dy.getString("sctnjzmj")));
            bdcdy.setMjdw(dy.getString("mjdw"));
            bdc_dyService.save(bdcdy);

        }
        zlstr = zlstr.substring(0,zlstr.length()-1);

        newproinst.setProjectName(qlrstr+"-"+zlstr+"-"+prodef.getProdefName());

        //生成项目资料列表
        List<Wfd_materclass> materclasslist = wfd_materclassService.list(new QueryWrapper<Wfd_materclass>().eq("procodeid", prodefid));
        for(Wfd_materclass materclass : materclasslist) {
            Wfi_materclass wfi_materclass = new Wfi_materclass();
            wfi_materclass.setDivisionCode(StringHelper.formatObject(materclass.getDivisionCode()));
            wfi_materclass.setFileindex(materclass.getFileindex());
            wfi_materclass.setMatedesc(StringHelper.formatObject(materclass.getMatedesc()));
            wfi_materclass.setProlsh(prolsh);
            wfi_materclass.setRequired(materclass.getRequired());
            wfi_materclass.setName(materclass.getName());
            wfi_materclass.setProcodeid(materclass.getProcodeid());
            wfi_materclassService.save(wfi_materclass);
        }

        saveOrUpdate(newproinst);

        return newproinst.getProlsh();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveProjectForStep2(JSONObject projectDataJson) throws Exception {

        String prolsh = projectDataJson.getString("prolsh");//申请流水号
        String code = projectDataJson.getString("code");
        Wfi_proinst wfi_proinst = getOne(new QueryWrapper<Wfi_proinst>().eq("prolsh", prolsh));
        if(wfi_proinst ==  null) {
            throw new SupermapBootException("未找到对应的项目信息-流水号："+prolsh);
        }
        wfi_proinst.setDivisionCode(code);
        boolean canEdit = canEdit(wfi_proinst);
        if(canEdit) {
            throw new SupermapBootException("该项目已经提交，不能重复操作");
        }
        String fsqlid = projectDataJson.getString("fsqlid");
        String dyid = projectDataJson.getString("dyid");
        //申请权利信息
        JSONObject qljson = projectDataJson.getJSONObject("ql");
        //申请附属权利信息
        JSONObject fsqljson = projectDataJson.getJSONObject("fsql");
        //插入前先删掉旧数据
       // bdc_qlService.remove(new QueryWrapper<Bdc_ql>().eq("prolsh", prolsh));
        if(StringHelper.isEmpty(fsqlid)) {
        	 throw new SupermapBootException("相关附属权利信息未找到，请联系系统管理员");
        }
        Bdc_fsql fsql= bdc_fsqlService.getById(fsqlid);
        Bdc_ql ql =bdc_qlService.getById(fsql.getQlid());
        if(ql==null) {
        	 if(StringHelper.isEmpty(fsqlid)) {
            	 throw new SupermapBootException("相关权利信息未找到，请联系系统管理员");
            }
        }
        Wfi_prodef prodef = wfi_prodefService.getById(wfi_proinst.getWfProdefid());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        ql.setProlsh(prolsh);
        ql.setProinstId(wfi_proinst.getProinstId());
        ql.setCreateTime(new Date());
        //附记
        ql.setFj(qljson.getString("fj"));
        ql.setCzfs(qljson.getString("czfs"));
        ql.setDjlx(prodef.getDjlx());
        ql.setQllx(qljson.getString("qllx"));
        ql.setDjyy(qljson.getString("djyy"));
        ql.setQdjg(qljson.getDouble("qdjg"));
        ql.setZqdw(qljson.getString("zqdw"));
        ql.setTdshyqr(qljson.getString("tdshyqr"));
        ql.setHth(qljson.getString("hth"));
        ql.setQlqssj(sdf.parse(qljson.getString("qlqssj")));
        ql.setQljssj(sdf.parse(qljson.getString("qljssj")));
        bdc_qlService.saveOrUpdate(ql);

//        // 重新构造单元权利关系，目前关系设计逻辑--》同一项目所有单元对应同一权利，每个单元对应多个权利人
//        List<Bdc_qldy> qldyList = bdc_qldyService.list(new QueryWrapper<Bdc_qldy>().eq("prolsh", prolsh));
//        if (!qldyList.isEmpty()) {
//            for (Bdc_qldy qldy : qldyList) {
//                qldy.setQlid(ql.getQlid());
//                bdc_qldyService.updateById(qldy);
//            }
//        }

        // 更新附属权利
        if (!StringHelper.isEmpty(fsqlid)) {
            if (fsql != null) {
               fsql.setFwxz(fsqljson.getString("fwxz"));
                bdc_fsqlService.updateById(fsql);
            }
        }

        saveOrUpdate(wfi_proinst);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updataBdcDy(JSONObject projectjson) {
        JSONArray dylist = projectjson.getJSONArray("dylist");
        String prolsh = projectjson.getString("prolsh");
        JSONArray qlrlist = projectjson.getJSONArray("qlrlist");

        Wfi_proinst proinst = getOne(new QueryWrapper<Wfi_proinst>().eq("prolsh", prolsh));
        Wfi_prodef prodef = wfi_prodefService.getById(proinst.getProdefId());

        bdc_dyService.remove(new QueryWrapper<Bdc_dy>().eq("prolsh", prolsh));
        bdc_sqrService.remove(new QueryWrapper<Bdc_sqr>().eq("prolsh", prolsh));
        bdc_qldyService.remove(new QueryWrapper<Bdc_qldy>().eq("prolsh", prolsh));
        bdc_qlService.remove(new QueryWrapper<Bdc_ql>().eq("prolsh", prolsh));

        String zlstr = "";
        for(int i=0;i<dylist.size();i++) {
            JSONObject dy = dylist.getJSONObject(i);
            if(i<2) {
                zlstr += dy.getString("zl") +",";
            }
            Bdc_dy bdcdy = new Bdc_dy();
            bdcdy.setBdcdyh(dy.getString("bdcdyh"));
            bdcdy.setProlsh(prolsh);
            bdcdy.setZl(dy.getString("zl"));
            bdcdy.setQlxz(dy.getString("qlxz")); 
            bdcdy.setFwyt(dy.getString("fwyt"));
            bdcdy.setFwjg(dy.getString("fwjg"));
//            bdcdy.setJgsj(dy.getString("JGSJ"));确定时间格式先
            bdcdy.setYcjzmj(StringHelper.getDouble(dy.getString("ycjzmj")));
            bdcdy.setYctnjzmj(StringHelper.getDouble(dy.getString("yctnjzmj")));
            bdcdy.setMj(StringHelper.getDouble(dy.getString("scjzmj")));
            bdcdy.setTnjzmj(StringHelper.getDouble(dy.getString("sctnjzmj")));
            bdcdy.setMjdw(dy.getString("mjdw"));
            bdc_dyService.save(bdcdy);
        }
        zlstr = zlstr.substring(0,zlstr.length()-1);

        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

        String qlrstr = "";
        for(int i=0;i<qlrlist.size();i++) {
            JSONObject qlr = qlrlist.getJSONObject(i);
            if(i<2) {
                qlrstr += qlr.getString("qlrmc") +",";
            }
            //房源携带的权利人，作为本次登记的义务人
            Bdc_sqr sqr = new Bdc_sqr();
            sqr.setProlsh(prolsh);
            sqr.setCreateTime(new Date());
            sqr.setSqrxm(qlr.getString("qlrmc"));
            sqr.setZjlx(qlr.getString("zjlx"));
            sqr.setZjh(qlr.getString("zjh"));
            sqr.setLxdh(qlr.getString("lxdh"));
            sqr.setTxdz(qlr.getString("txdz"));
            sqr.setBdcqzh(qlr.getString("bdcqzh"));
            sqr.setGyfs(qlr.getString("gyfs"));
            sqr.setSqrlb("2");
            bdc_sqrService.save(sqr);
        }
        qlrstr = qlrstr.substring(0,qlrstr.length()-1);

        proinst.setProjectName(qlrstr+"-"+zlstr+"-"+prodef.getProdefName());
        saveOrUpdate(proinst);

        //一般的登记，银行作为权利人存放，不过在组合流程中，属于抵押权人，这个后续支持组合流程时再适配
        Map<String, String> userDepart = proinstMapper.getUserDepart(sysUser.getDeptId());
        if(userDepart != null ) {
            Bdc_sqr qlr = new Bdc_sqr();
            qlr.setSqrlb("1");
            qlr.setSqrxm(userDepart.get("NAME"));
            qlr.setZjlx(userDepart.get("ZJLX"));
            qlr.setZjh(userDepart.get("ZJH"));
            qlr.setLxdh(userDepart.get("NAME"));
            qlr.setTxdz(userDepart.get("TXDZ"));
            qlr.setDlrxm(sysUser.getRealname());
            qlr.setZjlx(sysUser.getZjlx());
            qlr.setZjh(sysUser.getZjh());
            qlr.setSqrlb("1");
            qlr.setCreateTime(new Date());
            qlr.setProlsh(prolsh);
            bdc_sqrService.save(qlr);
        }
    }

    @Override
    public JSONObject getProjectProgress(String prolsh) {
        String coreQueryUrl = sys_configService.getConfigByKey("coreQueryUrl");
        if(StringHelper.isEmpty(coreQueryUrl)) {
            throw new SupermapBootException("未配置进度查询接口地址，请联系管理员进行配置");
        }
        String jsonstr = HttpClientUtil.requestPost( "{\n" +
                "\"requestcode\":"+ ConstValueMrpc.RequestcodeEnum.JD.Value+ ",\n" +
                "\"requestseq\": \"yyyyMMddHHmmssffffff\",\n" +
                "\"data\": [{\n" +
                "\"xzqdm\": \"450200\",\n" +
                "\"ywlsh\": \""+prolsh+"\"\n" +
                "}]\n" +
                "}", coreQueryUrl);
        if(jsonstr == null) {
            throw new SupermapBootException("接口返回数据为空，请检查进度查询接口是否异常");
        }
        JSONObject jsonObject = JSONObject.parseObject(jsonstr);
        return jsonObject;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void selecthouse(JSONObject housesdata) {
        String prolsh = housesdata.getString("prolsh");
        JSONArray houselist = housesdata.getJSONArray("houselist");
        Wfi_proinst proinst = getOne(new QueryWrapper<Wfi_proinst>().eq("PROLSH", prolsh));
        if(proinst == null) {
            throw new SupermapBootException("未找到流水号为"+prolsh+"的项目信息，请联系管理员核查");
        }
        Wfi_prodef prodef = wfi_prodefService.getById(proinst.getWfProdefid());
        if (prodef == null) {
            throw new SupermapBootException("未找到对应的流程信息-流水号：" + prolsh);
        }
        for(int i = 0;i < houselist.size(); i++) {
            JSONObject house = houselist.getJSONObject(i);
            JSONObject houseclob = house.getJSONObject("houseclob");
            JSONObject housedeail = houseclob.getJSONObject("house");
            JSONArray dylist = houseclob.getJSONArray("dylist");
            JSONArray cflist = houseclob.getJSONArray("cflist");
            JSONArray cqlist = houseclob.getJSONArray("cqlist");
            if((dylist!=null&&dylist.size()>0)||(cflist!=null&&cflist.size()>0)) {
           	 throw new SupermapBootException("单元号为:"+house.getString("bdcdyh")+"的单元已被抵押或查封，不允许申报。");
           }
            Bdc_dy bdcdy = new Bdc_dy();
            bdcdy.setId(StringHelper.getUUID());
            bdcdy.setBdcdyh(house.getString("bdcdyh"));
            bdcdy.setProlsh(prolsh);
            bdcdy.setZl(house.getString("zl"));
            bdcdy.setQlxz(housedeail.getString("qlxz"));
            bdcdy.setFwyt(housedeail.getString("fwyt"));
            bdcdy.setFwjg(housedeail.getString("fwjg"));
            bdcdy.setSzc(housedeail.getString("szc"));
            bdcdy.setFh(housedeail.getString("fh"));
            bdcdy.setZcs(Integer.parseInt(housedeail.getString("zcs")));
            bdcdy.setFwbm(housedeail.getString("fwbm"));
            bdcdy.setFwcb("8300");
            bdcdy.setFwxz(housedeail.getString("fwxz"));
            bdcdy.setFwtdyt(housedeail.getString("fwtdyt"));
            if(housedeail.getLong("jgsj") != null) {
                bdcdy.setJgsj(new Date(housedeail.getLong("jgsj")));
            }
            bdcdy.setYcjzmj(StringHelper.getDouble(house.getString("ycjzmj")));
            bdcdy.setYctnjzmj(StringHelper.getDouble(house.getString("yctnjzmj")));
            bdcdy.setMj(StringHelper.getDouble(house.getString("scjzmj")));
            bdcdy.setTnjzmj(StringHelper.getDouble(house.getString("sctnjzmj")));
            bdcdy.setFttdmj(StringHelper.getDouble(house.getString("fttdmj")));
            bdcdy.setMjdw(housedeail.getString("mjdw"));
            bdcdy.setDytdmj(StringHelper.getDouble(house.getString("zdmj")));
            bdcdy.setHouseclob(houseclob.toJSONString());
            bdc_dyService.save(bdcdy);

            // 附属权利，每个单元对应一条附属权利（根据 Bdc_qldy 表关联）
            Bdc_fsql fsql = new Bdc_fsql();
            Bdc_ql ql = new Bdc_ql();
            ql.setQlid(StringHelper.getUUID());
            ql.setQlid(UUID.randomUUID().toString().replaceAll("-", ""));
            for(i=0;i<cqlist.size();i++) {
            	JSONObject cq = cqlist.getJSONObject(i);
            	if(cq!=null) {
            		if(cq.getLong("QLQSSJ")!=null) {
            			ql.setQlqssj(new Date(cq.getLong("QLQSSJ")));
            		}
            		if(cq.getLong("QLJSSJ")!=null) {
            			ql.setQljssj(new Date(cq.getLong("QLJSSJ")));
            		}
            	}
            }
            ql.setCzfs("02");
            ql.setQllx("4");
            ql.setZqdw("1");
            ql.setProinstId(proinst.getProinstId());
            ql.setProlsh(prolsh);
            String djyyDemo = sys_configService.getConfigByKey("djyyDemo");
            if(djyyDemo!=null) {
            	ql.setDjyy(djyyDemo);
            }
            fsql.setFsqlid(StringHelper.getUUID());
            fsql.setProlsh(prolsh);
            fsql.setCreateTime(new Date());
            fsql.setFwxz(housedeail.getString("fwxz"));
            ConstValue.BDCDYLX bdcdylx = ConstValue.BDCDYLX.initFrom(housedeail.getString("bdcdylx"));
            fsql.setDywlx(StringHelper.getDYBDCLXfromBDCDYLX(bdcdylx));
            // 保存上一手权利信息或赋值权利信息
            //Bdc_ql ql = saveBdcLastQl(proinst, housedeail.getJSONArray("dylist"), prodef, fsql, bdcdy);

            //标记一下房源表，该单元已被选择
            Houseshis his = new Houseshis();
            his.setFyzt(2);
            houseshisService.update(his, new UpdateWrapper<Houseshis>().eq("id",house.getString("id")).eq("prolsh",prolsh));

            //构建权利关系，以申请人为主，延伸至权利，单元，附属权利
            JSONArray qlrlist = houseclob.getJSONArray("qlrlist");
            JSONArray msrlist = houseclob.getJSONArray("msrlist");
            String dyr = "";
            String tdshyqr="";   
            for(int j=0;j<qlrlist.size();j++) {
                JSONObject qlr = qlrlist.getJSONObject(j);
                //循环该单元的权利人，保存起来，如果该权利人已经保存过了（多单元同权利人的情况），直接拿已保存的权利人出来构建权利单元关系表
                String qlrmc = StringHelper.formatObject(qlr.getString("qlrmc"));
                if (StringHelper.isEmpty(dyr)) {
                    dyr = qlrmc;
                } else if(!dyr.contains(qlrmc)) {
                    dyr += "," + qlrmc;
                }
                String zjh = StringHelper.formatObject(qlr.getString("zjh"));
//                Bdc_sqr ywr = bdc_sqrService.getOne(new QueryWrapper<Bdc_sqr>().eq("prolsh", prolsh).eq("sqrxm", qlrmc).eq("zjh", zjh));
                //boot中不再进行去重操作
//                if(ywr == null) {
                    //房源携带的权利人，作为本次登记的义务人
                    Bdc_sqr sqr = new Bdc_sqr();
                    sqr.setId(StringHelper.getUUID());
                    sqr.setProlsh(prolsh);
                    sqr.setCreateTime(new Date());
                    sqr.setSqrxm(qlr.getString("qlrmc"));
                    sqr.setZjlx(qlr.getString("zjlx"));
                    sqr.setZjh(qlr.getString("zjh"));
                    sqr.setLxdh(qlr.getString("lxdh"));
                    sqr.setTxdz(qlr.getString("txdz"));
                    sqr.setBdcqzh(qlr.getString("bdcqzh"));
                    sqr.setGyfs(qlr.getString("gyfs"));
                    sqr.setSqrlb(ConstValue.SQRLB.YF.Value);
                    sqr.setSqrlx(qlr.getString("qlrlx"));
                    bdc_sqrService.save(sqr);
//                }
                Bdc_qldy qldy = new Bdc_qldy();
                qldy.setProlsh(prolsh);
                qldy.setDyid(bdcdy.getId());
                qldy.setSqrid(sqr.getId());
                qldy.setFsqlid(fsql.getFsqlid());
                qldy.setCreatetime(new Date());
                 qldy.setQlid(ql.getQlid());
                bdc_qldyService.save(qldy);
            }
            Boolean gyflag =false;
            if(msrlist.size()>1) {
            	gyflag =true;
            }
            for(int k=0;k<msrlist.size();k++) {
                JSONObject msr = msrlist.getJSONObject(k);
                //循环该单元的权利人，保存起来，如果该权利人已经保存过了（多单元同权利人的情况），直接拿已保存的权利人出来构建权利单元关系表
                String msrmc = StringHelper.formatObject(msr.getString("QLRMC"));
                if (StringHelper.isEmpty(tdshyqr)) {
                	tdshyqr = msrmc;
                } else if(!dyr.contains(msrmc)) {
                	tdshyqr += "," + msrmc;
                }
               
                String msrzjh = StringHelper.formatObject(msr.getString("ZJH"));
//                Bdc_sqr newqlr = bdc_sqrService.getOne(new QueryWrapper<Bdc_sqr>().eq("prolsh", prolsh).eq("sqrxm", msrmc).eq("zjh", msrzjh));
//                if(newqlr == null) {
                    Bdc_sqr sqr = new Bdc_sqr();
                    sqr.setId(StringHelper.getUUID());
                    sqr.setProlsh(prolsh);
                    sqr.setCreateTime(new Date());
                    sqr.setSqrxm(msr.getString("QLRMC"));
                    sqr.setZjlx(msr.getString("ZJZL"));
                    sqr.setZjh(msr.getString("ZJH"));
                    sqr.setLxdh(msr.getString("LXDH"));
                    //sqr.setTxdz(qlr.getString("txdz"));
                    //sqr.setBdcqzh(qlr.getString("bdcqzh"));
                    //sqr.setGyfs(qlr.getString("gyfs"));
                    if(gyflag) {
                    	sqr.setGyfs("1");
                    }else {
                    	sqr.setGyfs("0");
                    }
                    sqr.setSqrlb(ConstValue.SQRLB.JF.Value);
                    sqr.setSqrlx(msr.getString("QLRLX"));
                    ql.setHth(msr.getString("CASENUM"));
                    ql.setQdjg(msr.getDouble("FDCJYJG"));
                    bdc_sqrService.save(sqr);
//                    newqlr = sqr;
//                }
                Bdc_qldy qldy = new Bdc_qldy();
                qldy.setProlsh(prolsh);
                qldy.setDyid(bdcdy.getId());
                qldy.setSqrid(sqr.getId());
                qldy.setFsqlid(fsql.getFsqlid());
                qldy.setCreatetime(new Date());
                qldy.setQlid(ql.getQlid());
                bdc_qldyService.save(qldy);
            }
           
            if(bdcdy.getFwyt().equals("905")) {
            ql.setTdshyqr(tdshyqr);
            }
            fsql.setDyr(dyr);
            //插入附记
            String fj="";
            String fjdemo="";
            if(housedeail.getString("fwyt")!=null&&housedeail.getString("fwyt").equals("905")) {
            	fjdemo="fjDemoCw";
            }else {
            	fjdemo="fjDemo";
            }
             fj = sys_configService.getConfigByKey(fjdemo);
             if(fj!=null&&!fj.isEmpty()) {
            	 ql.setFj(fj.replace("{0}", dyr));
             }
            fsql.setQlid(ql.getQlid());
            ql.setFsqlid(fsql.getFsqlid());
            bdc_fsqlService.save(fsql);
            bdc_qlService.save(ql);
        }

    }

    private Bdc_ql saveBdcLastQl(Wfi_proinst proinst, JSONArray dylist, Wfi_prodef prodef, Bdc_fsql fsql, Bdc_dy bdcdy) {
        if(!prodef.getProdefclassId().contains(ConstValueMrpc.RequestcodeEnum.DYBG.Value) && !prodef.getProdefclassId().contains(ConstValueMrpc.RequestcodeEnum.DYZX.Value)) {
            return null;
        }
        if (dylist.isEmpty()) {
            throw new SupermapBootException("选中单元无抵押权信息，无法添加");
        }
//        if (dylist.size() > 1) {
//            throw new SupermapBootException("选中单元抵押权信息存在多条，请尝试用证明号核验");
//        }
        Bdc_ql ql = bdc_qlService.getOne(new QueryWrapper<Bdc_ql>().eq("PROLSH", proinst.getProlsh()));
        JSONObject dy = JSON.parseObject(dylist.get(0).toString());
        if(prodef.getProdefclassId().contains(ConstValueMrpc.RequestcodeEnum.DYBG.Value)) {
            Bdc_last_ql lastQl = new Bdc_last_ql();
            lastQl.setId(StringHelper.getUUID());
            lastQl.setProlsh(proinst.getProlsh());
            lastQl.setDyid(bdcdy.getId());
            lastQl.setCzfs(dy.getString("czfs"));
            lastQl.setDivisionCode(prodef.getDivisionCode());
            String dyfs = dy.getString("dyfs");
            Double dyje = 0.0;
            lastQl.setDyfs(dyfs);
            if (ConstValue.DYFS.YBDY.Value.equals(dyfs)) {
                dyje = StringHelper.getDouble(dy.getDouble("bdbzzqse"));
            } else {
                dyje = StringHelper.getDouble(dy.getDouble("zgzqse"));
            }
            lastQl.setDyje(dyje);
            lastQl.setDypgjz(StringHelper.getDouble(dy.getString("dypgjz")));
            lastQl.setDyr(dy.getString("dyr"));
            lastQl.setDywlx(dy.getString("dywlx"));
            lastQl.setDjyy(dy.getString("djyy"));
            lastQl.setFj(dy.getString("fj"));
            try {
                lastQl.setQlqssj(StringHelper.FormatByDate(dy.getString("dyqlqssj")));
                lastQl.setQljssj(StringHelper.FormatByDate(dy.getString("dyqljssj")));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            lastQl.setSfhbzs(dy.getString("sfhbzs"));
            lastQl.setZgzqqdss(dy.getString("zgzqqdss"));
            lastQl.setZjjzwdyfw(dy.getString("zjjzwdyfw"));
            lastQl.setZl(dy.getString("zjjzwzl"));
            lastQl.setZxdyyy(dy.getString("zxdyyy"));
            lastQl.setZxfj(dy.getString("zxfj"));

            bdc_last_qlService.save(lastQl);
        } else if (prodef.getProdefclassId().contains(ConstValueMrpc.RequestcodeEnum.DYZX.Value)) {
            fsql.setZwr(dy.getString("zwr") );
            fsql.setZjjzwdyfw(dy.getString("zjjzwdyfw"));
            fsql.setZgzqqdss(dy.getString("zgzqqdss"));
            if (ql == null) {
                ql = new Bdc_ql();
                ql.setQlid(UUID.randomUUID().toString().replaceAll("-", ""));
                ql.setProlsh(proinst.getProlsh());
                ql.setCreateTime(new Date());
                ql.setProinstId(proinst.getProinstId());
            }
            ql.setCzfs(dy.getString("czfs"));
            String dyfs = dy.getString("dyfs");
            Double dyje = 0.0;
            if (ConstValue.DYFS.YBDY.Value.equals(dyfs)) {
                dyje = StringHelper.getDouble(dy.getDouble("bdbzzqse"));
            } else {
                dyje = StringHelper.getDouble(dy.getDouble("zgzqse"));
            }
            ql.setDylx(dyfs);
            ql.setDypgjz(StringHelper.getDouble(dy.getString("dypgjz")));
            ql.setDyje(dyje);
            ql.setDjlx(prodef.getDjlx());
            ql.setQllx(prodef.getQllx());
            ql.setDjyy(dy.getString("djyy"));
            ql.setFj(dy.getString("fj"));
            ql.setDywlx(dy.getString("dywlx"));
            ql.setZqdw(dy.getString("zqdw"));
            try {
                ql.setQlqssj(StringHelper.FormatByDate(dy.getString("dyqlqssj")));
                ql.setQljssj(StringHelper.FormatByDate(dy.getString("dyqljssj")));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            bdc_qlService.saveOrUpdate(ql);
        }
        // 保存抵押权人为权利人
        String qlrmc = StringHelper.formatObject(dy.getString("dyqr"));
        String zjh = StringHelper.formatObject(dy.getString("zjh"));
        Bdc_sqr sqr = bdc_sqrService.getOne(new QueryWrapper<Bdc_sqr>().eq("prolsh", proinst.getProlsh()).eq("sqrxm", qlrmc).eq("zjh", zjh));
        if(sqr == null) {
            //抵押权人，作为本次登记的权利人
            sqr = new Bdc_sqr();
            sqr.setId(StringHelper.getUUID());
            sqr.setProlsh(proinst.getProlsh());
            sqr.setCreateTime(new Date());
            sqr.setSqrxm(qlrmc);
            sqr.setZjlx(dy.getString("zjlx"));
            sqr.setZjh(zjh);
            sqr.setLxdh(dy.getString("lxdh"));
            sqr.setTxdz(dy.getString("txdz"));
            sqr.setBdcqzh(dy.getString("bdcqzh"));
            sqr.setGyfs(dy.getString("gyfs"));
            sqr.setSqrlb(ConstValue.SQRLB.JF.Value);
            sqr.setSqrlx(dy.getString("qlrlx"));
            LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
            sqr.setDlrxm(sysUser.getRealname());
            sqr.setDlrzjlx(sysUser.getZjlx());
            sqr.setDlrzjhm(sysUser.getZjh());
            bdc_sqrService.save(sqr);
            // 构建关系
            Bdc_qldy qldy = new Bdc_qldy();
            qldy.setProlsh(proinst.getProlsh());
            qldy.setDyid(bdcdy.getId());
            qldy.setSqrid(sqr.getId());
            qldy.setFsqlid(fsql.getFsqlid());
            qldy.setCreatetime(new Date());
            if (ql != null) {
                qldy.setQlid(ql.getQlid());
            }
            bdc_qldyService.save(qldy);
        }

        return ql;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removehouse(String houseid, String prolsh) {
        if(StringHelper.isEmpty(houseid)) {
            throw new SupermapBootException("单元id不能为空");
        }

        List<Bdc_dy> bdcdys = bdc_dyService.list(new QueryWrapper<Bdc_dy>().eq("id", houseid).eq("prolsh",prolsh));
        if(bdcdys.size()==0) {
            throw new SupermapBootException("找不到单元信息");
        }
        for(Bdc_dy dy : bdcdys) {
            bdc_dyService.removeById(dy.getId());
            bdc_last_qlService.remove(new QueryWrapper<Bdc_last_ql>().eq("dyid", dy.getId()));           
            //房源表中被标记为已选择的恢复为未选择
            Houseshis his = new Houseshis();
            his.setFyzt(1);
            houseshisService.update(his, new UpdateWrapper<Houseshis>().eq("bdcdyh",dy.getBdcdyh()).eq("prolsh",prolsh).eq("fyzt","2"));
            List<Bdc_qldy> qldys = bdc_qldyService.list(new QueryWrapper<Bdc_qldy>().eq("dyid", dy.getId()).eq("prolsh", prolsh));
            for(Bdc_qldy qldy : qldys) {
            	//现根据权利id删除权利
            	 bdc_qlService.remove(new QueryWrapper<Bdc_ql>().eq("qlid",qldy.getQlid()));
                //看看该单元的申请还是不是其他单元的申请人，只是该单元的申请人就连申请人一起删除
                List<Bdc_qldy> otherqldys = bdc_qldyService.list(new QueryWrapper<Bdc_qldy>().ne("dyid", dy.getId()).eq("prolsh", prolsh).eq("sqrid",qldy.getSqrid()));
                if(otherqldys.size() == 0) {
                    bdc_sqrService.remove(new QueryWrapper<Bdc_sqr>().eq("id", qldy.getSqrid()).eq("prolsh",prolsh));
                }
                if (!StringHelper.isEmpty(qldy.getFsqlid())) {
                    bdc_fsqlService.remove(new QueryWrapper<Bdc_fsql>().eq("fsqlid", qldy.getFsqlid()).eq("prolsh",prolsh));
                }
                bdc_qldyService.removeById(qldy.getId());
            }
        }

    }

    @Override
    public void setNameAndInitDYQR(String prolsh) {
        Wfi_proinst wfi_proinst = getOne(new QueryWrapper<Wfi_proinst>().eq("prolsh", prolsh));

        if(wfi_proinst ==  null) {
            throw new SupermapBootException("未找到对应的项目信息-流水号："+prolsh);
        }
        Wfi_prodef prodef = wfi_prodefService.getById(wfi_proinst.getProdefId());

        List<Bdc_dy> dylist = bdc_dyService.list(new QueryWrapper<Bdc_dy>().eq("prolsh", prolsh));
        List<Bdc_sqr> sqrs = bdc_sqrService.list(new QueryWrapper<Bdc_sqr>().eq("prolsh", prolsh));
        String zlstr = "";
        for(int i=0;i<dylist.size();i++) {
            Bdc_dy dy = dylist.get(i);
            if(i<2) {
                zlstr += dy.getZl() +",";
            }
        }
        zlstr = zlstr.substring(0,zlstr.length()-1);

        String qlrstr = "";
        for(int i=0;i<sqrs.size();i++) {
            Bdc_sqr qlr = sqrs.get(i);
            if(i<2) {
                qlrstr += qlr.getSqrxm() +",";
            }
        }
        qlrstr = qlrstr.substring(0,qlrstr.length()-1);
        wfi_proinst.setProjectName(qlrstr+"-"+zlstr+"-"+prodef.getProdefName());
        saveOrUpdate(wfi_proinst);

        // 过滤抵押变更与抵押注销流程
        if(prodef.getProdefclassId().contains(ConstValueMrpc.RequestcodeEnum.DYBG.Value) || prodef.getProdefclassId().contains(ConstValueMrpc.RequestcodeEnum.DYZX.Value)) {
            return;
        }
//        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        //一般的登记，银行作为权利人存放，不过在组合流程中，属于抵押权人，这个后续支持组合流程时再适配
//        Map<String, String> userDepart = proinstMapper.getUserDepart(sysUser.getDeptId());
//        Bdc_sqr bank = bdc_sqrService.getOne(new QueryWrapper<Bdc_sqr>().eq("prolsh", prolsh).eq("sqrxm", userDepart.get("NAME")).eq("sqrlb", "1"));
//
//        if(bank == null ) {
//            Bdc_sqr qlr = new Bdc_sqr();
//            qlr.setSqrxm(userDepart.get("NAME"));
//            qlr.setZjlx(userDepart.get("ZJLX"));
//            qlr.setZjh(userDepart.get("ZJH"));
//            qlr.setLxdh(userDepart.get("LXDH"));
//            qlr.setTxdz(userDepart.get("TXDZ"));
//            qlr.setFddbr(userDepart.get("FDDBR"));
//            qlr.setFddbrzjlx(userDepart.get("FDDBRZJLX"));
//            qlr.setFddbrzjhm(userDepart.get("FDDBRZJHM"));
//            qlr.setDlrxm(sysUser.getRealname());
//            qlr.setDlrzjlx(sysUser.getZjlx());
//            qlr.setDlrzjhm(sysUser.getZjh());
//            qlr.setSqrlb(ConstValue.SQRLB.JF.Value);
//            qlr.setSqrlx("2");
//            qlr.setGyfs(ConstValue.GYFS.DYSY.Value);
//            qlr.setCreateTime(new Date());
//            qlr.setProlsh(prolsh);
//            qlr.setId(StringHelper.getUUID());
//
//            bdc_sqrService.save(qlr);
//            bank = qlr;
//        }
//        //把银行的关系干掉再重新添加
//        bdc_qldyService.remove(new QueryWrapper<Bdc_qldy>().eq("prolsh",prolsh).eq("sqrid",bank.getId()));
//        List<Bdc_dy> bdcdys = bdc_dyService.list(new QueryWrapper<Bdc_dy>().eq("prolsh", prolsh));
//        for(Bdc_dy dy : bdcdys) {
//            Bdc_qldy qldy = new Bdc_qldy();
//            qldy.setProlsh(prolsh);
//            qldy.setDyid(dy.getId());
//            qldy.setSqrid(bank.getId());
//            qldy.setCreatetime(new Date());
//            bdc_qldyService.save(qldy);
//        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String createProject(String wfi_prodefid,String code) {
        Wfi_prodef wfi_prodefEntity = wfi_prodefService.getById(wfi_prodefid);
        if(wfi_prodefEntity == null){
            throw new SupermapBootException("找不到业务流程信息");
        }
        LoginUser sysUser = (LoginUser)SecurityUtils.getSubject().getPrincipal();
        Wfi_proinst newproinst = new Wfi_proinst();
        //通过存储过程获取业务号
        Map param = new HashMap<>();
        String yeartime = StringHelper.FormatDateOnType(new Date(),"yyyy");
        param.put("iYear",yeartime);
        param.put("sDisCode",code);
        param.put("sBH","1");
        param.put("M_BHTYPE","1");
        param.put("lsh","0");
        proinstMapper.querypromaxid(param);
        newproinst.setProdefId(wfi_prodefEntity.getProdefId());
        newproinst.setDjlx(wfi_prodefEntity.getDjlx());
        newproinst.setProinstId(UUID.randomUUID().toString().toLowerCase());
        newproinst.setAcceptor(sysUser.getUsername());
        newproinst.setShzt(-1);
        newproinst.setCreatDate(new Date());
        newproinst.setProinstStart(new Date());
        newproinst.setDivisionCode(code);
        newproinst.setDivisionName("");
        newproinst.setInstanceType(1);
        newproinst.setProdefName(wfi_prodefEntity.getProdefName());
        newproinst.setQllx(wfi_prodefEntity.getQllx());
        newproinst.setLsh("");
        newproinst.setProlsh(param.get("lsh").toString());
        newproinst.setUserId(sysUser.getId());
        newproinst.setProjectName(wfi_prodefEntity.getProdefName());
        newproinst.setWfProdefid(wfi_prodefEntity.getProdefId());
        newproinst.setWfProdefname(wfi_prodefEntity.getProdefName());
        newproinst.setYwly(ConstValueMrpc.YWLY.DYPT.Value);

        //生成项目资料列表
        List<Wfd_materclass> materclasslist = wfd_materclassService.list(new QueryWrapper<Wfd_materclass>().eq("procodeid", wfi_prodefid));
        for(Wfd_materclass materclass : materclasslist) {
            Wfi_materclass wfi_materclass = new Wfi_materclass();
            wfi_materclass.setDivisionCode(StringHelper.formatObject(materclass.getDivisionCode()));
            wfi_materclass.setFileindex(materclass.getFileindex());
            wfi_materclass.setMatedesc(StringHelper.formatObject(materclass.getMatedesc()));
            wfi_materclass.setProlsh(newproinst.getProlsh());
            wfi_materclass.setRequired(materclass.getRequired());
            wfi_materclass.setName(materclass.getName());
            wfi_materclass.setProcodeid(materclass.getProcodeid());
            wfi_materclass.setEcert(materclass.getEcert());
            wfi_materclass.setEcertCode(materclass.getEcertCode());
            wfi_materclassService.save(wfi_materclass);
        }
        save(newproinst);
        return newproinst.getProlsh();
    }


    //已办箱：分页查询
    @Override
    public IPage<Map> projectlistendbox(Page<Map> page, HttpServletRequest request) {
        Map<String, String> param = new HashMap<String,String>();
        param.put("prolsh", StringHelper.trim(request.getParameter("prolsh"))) ;
        param.put("type", StringHelper.trim(request.getParameter("type"))) ;
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        param.put("userid",sysUser.getId());
        return page.setRecords(proinstMapper.projectlistendbox(page, param));
    }

    @Override
    public Map<String, Object> getRecipientData(String prolsh) {
        JSONObject map = new JSONObject();
        //获取到流程信息
        Wfi_proinst wfi_proinst = getOne(new QueryWrapper<Wfi_proinst>().eq("PROLSH", prolsh));
        map.put("prolsh", prolsh);
        if (wfi_proinst == null) {
            throw new SupermapBootException("未找到对应的项目信息-流水号：" + prolsh);
        }
        Wfi_prodef prodef = getWfi_prodefService.getById(wfi_proinst.getWfProdefid());
        if (prodef == null) {
            throw new SupermapBootException("未找到对应的流程信息-流水号：" + prolsh);
        }
        SysUser sysUser = proinstMapper.getSysuser(wfi_proinst.getUserId());
        if (sysUser == null) {
            throw new SupermapBootException("未找到当前登录用户-userid：" + wfi_proinst.getUserId());
        }
        List<Bdc_dy> bdcdys = bdc_dyService.list(new QueryWrapper<Bdc_dy>().eq("prolsh", prolsh));
        StringBuilder zls = new StringBuilder();
        if (bdcdys.size() > 0) {
            zls.append(bdcdys.get(0).getZl());
            if (bdcdys.size() > 1) {
                zls.append("等" + bdcdys.size() + "个");
            }
        }
        List<Bdc_sqr> sqrs = bdc_sqrService.list(new QueryWrapper<Bdc_sqr>().eq("PROLSH", prolsh));
        List<Wfi_materclass> Wfi_materclass = wfi_materclassService.getMaterlist(prolsh);

        map.put("user",sysUser);
        map.put("proinst", wfi_proinst);
        map.put("prodef", prodef);
        map.put("bdczl",zls);
        map.put("sqrs",sqrs);
        map.put("materclass", Wfi_materclass);
        return map;
    }

    private Wfi_prodef getProDefInfo(String prolsh) {
        if (StringHelper.isEmpty(prolsh)) {
            throw new SupermapBootException("流水号不能为空");
        }
        Wfi_proinst proinst = getOne(new QueryWrapper<Wfi_proinst>().eq("PROLSH", prolsh));
        if(proinst == null) {
            throw new SupermapBootException("未找到流水号为"+prolsh+"的项目信息，请联系管理员核查");
        }
        Wfi_prodef prodef = wfi_prodefService.getById(proinst.getWfProdefid());
        if (prodef == null) {
            throw new SupermapBootException("未找到对应的流程信息-流水号：" + prolsh);
        }
        return prodef;
    }

    @Override
    public JSONObject searchBdcqzh(String bdcqzh) {
        String coreQueryUrl = sys_configService.getConfigByKey("coreQueryUrl");
        System.out.println("地址+"+coreQueryUrl);
        if(StringHelper.isEmpty(coreQueryUrl)) {
            throw new SupermapBootException("未配置接口查询服务地址，请联系管理员进行配置");
        }
        String jsonstr = HttpClientUtil.requestPost( "{\n" +
                "\"requestcode\":"+ ConstValueMrpc.RequestcodeEnum.QZ.Value+ ",\n" +
                "\"requestseq\": \"yyyyMMddHHmmssffffff\",\n" +
                "\"data\": [{\n" +
                "\"xzqdm\": \"450200\",\n" +
                "\"bdcqzh\": \""+bdcqzh+"\"\n" +
                "}]\n" +
                "}", coreQueryUrl);
        if(jsonstr == null) {
            throw new SupermapBootException("接口返回数据为空，请检查权证号检索接口服务是否异常");
        }
        JSONObject jsonObject = JSONObject.parseObject(jsonstr);
        return jsonObject;
    }

    @Override
    public JSONObject getProjectByProlsh(String prolsh) {
        JSONObject map = new JSONObject();
        Wfi_proinst wfi_proinst = getOne(new QueryWrapper<Wfi_proinst>().eq("PROLSH", prolsh));
        if (wfi_proinst == null) {
            throw new SupermapBootException("未找到对应的项目信息-流水号：" + prolsh);
        }
        Wfi_prodef prodef = getWfi_prodefService.getById(wfi_proinst.getWfProdefid());
        //根据关系表查出各种信息,目前这里就只有权利及附件的信息
        List<Bdc_ql> bdcqls = bdc_qlService.list(new QueryWrapper<Bdc_ql>().eq("PROLSH", prolsh));
        // 根据关系表查出附属权利
        List<Bdc_fsql> bdcFsqls = bdc_fsqlService.list(new QueryWrapper<Bdc_fsql>().eq("PROLSH", prolsh));
        List<Bdc_dy> dys = bdc_dyService.list(new QueryWrapper<Bdc_dy>().eq("PROLSH", prolsh));

        List<Bdc_sqr> qlrlist = bdc_sqrService.list(new QueryWrapper<Bdc_sqr>().eq("PROLSH", prolsh).eq("SQRLB", ConstValue.SQRLB.JF.Value));
        List<Bdc_sqr> ywrlist = bdc_sqrService.list(new QueryWrapper<Bdc_sqr>().eq("PROLSH", prolsh).eq("SQRLB", ConstValue.SQRLB.YF.Value));

        SysUser sysUser = proinstMapper.getSysuser(wfi_proinst.getUserId());
        map.put("proinst", wfi_proinst);
        map.put("prodef",prodef);
        map.put("bdcql", bdcqls);
        map.put("bdcfsql", bdcFsqls);
        map.put("dylist", dys);
        map.put("qlrlist", qlrlist);
        map.put("ywrlist", ywrlist);
        map.put("sysUser", sysUser);
        return map;
    }

}
