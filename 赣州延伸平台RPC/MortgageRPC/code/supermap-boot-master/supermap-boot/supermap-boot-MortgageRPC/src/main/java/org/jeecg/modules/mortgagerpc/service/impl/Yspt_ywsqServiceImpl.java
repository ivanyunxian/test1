package org.jeecg.modules.mortgagerpc.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.jeecg.common.exception.SupermapBootException;
import org.jeecg.common.system.base.service.ISystemDictService;
import org.jeecg.common.system.util.HttpClientUtil;
import org.jeecg.common.system.util.StringHelper;
import org.jeecg.common.util.AsyncTaskConfig;
import org.jeecg.common.util.ConstValue;
import org.jeecg.common.util.ConstValueMrpc;
import org.jeecg.modules.mortgagerpc.entity.Bdc_shyqzd;
import org.jeecg.modules.mortgagerpc.entity.Bdc_dy;
import org.jeecg.modules.mortgagerpc.entity.Bdc_fsql;
import org.jeecg.modules.mortgagerpc.entity.Bdc_last_ql;
import org.jeecg.modules.mortgagerpc.entity.Bdc_ql;
import org.jeecg.modules.mortgagerpc.entity.Bdc_qldy;
import org.jeecg.modules.mortgagerpc.entity.Bdc_sqr;
import org.jeecg.modules.mortgagerpc.entity.Bdc_zrz;
import org.jeecg.modules.mortgagerpc.entity.Houseshis;
import org.jeecg.modules.mortgagerpc.entity.Shyqzdhis;
import org.jeecg.modules.mortgagerpc.entity.Wfi_materclass;
import org.jeecg.modules.mortgagerpc.entity.Wfi_materdata;
import org.jeecg.modules.mortgagerpc.entity.Wfi_prodef;
import org.jeecg.modules.mortgagerpc.entity.Wfi_proinst;
import org.jeecg.modules.mortgagerpc.service.IBdc_shyqzdService;
import org.jeecg.modules.mortgagerpc.service.IBdc_zrzService;
import org.jeecg.modules.mortgagerpc.service.IShyqzdhisService;
import org.jeecg.modules.mortgagerpc.service.ISys_configService;
import org.jeecg.modules.mortgagerpc.service.IWfi_materclassService;
import org.jeecg.modules.mortgagerpc.service.IWfi_materdataService;
import org.jeecg.modules.mortgagerpc.service.IYspt_ywsqService;
import org.jeecg.modules.mortgagerpc.task.AsyncTaskService;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.entity.YsptEnterprise;
import org.jeecg.modules.system.mapper.YsptEnterpriseMapper;
import org.jeecg.modules.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import cn.hutool.core.util.RandomUtil;

@Service
public class Yspt_ywsqServiceImpl extends ServiceImpl<YsptEnterpriseMapper, YsptEnterprise>
		implements IYspt_ywsqService {

	@Autowired
	IWfi_materclassService wfi_materclassService;

	@Autowired
	IWfi_materdataService wfi_materdataService;

	@Autowired
	ISys_configService sys_configService;

	@Autowired
	private ISysUserService sysUserService;
	
	@Autowired
	private YsptEnterpriseMapper ysptEnterpriseMapper;
	
	@Autowired
	private IShyqzdhisService shyqzdhisService;
	
	@Autowired
	private ISystemDictService systemDictService;
	
	@Autowired
	private IBdc_shyqzdService Bdc_shyqzdService;
	
	@Autowired
	private IBdc_zrzService bdc_zrzService;
	

	@Override
	public void submitProject(String enterpriseid, String type) {
		// 获取企业数据 企业 企业关联用户等
		YsptEnterprise enterprise = getById(enterpriseid);
		String jsonString = "";
		List<Bdc_shyqzd> bdcshyqzd = new ArrayList<Bdc_shyqzd>();
		List<SysUser> userList = new ArrayList<SysUser>();
		//type =1 未全部提交 2为人员提交 3为宗地提交
		if(type!=null && type.equals("1")) {
			userList = sysUserService.list(new QueryWrapper<SysUser>().eq("enterprise_id", enterpriseid).eq("STATUS", "2"));
			List<Wfi_materclass> wfiMaterclassList = wfi_materclassService
					.list(new QueryWrapper<Wfi_materclass>().eq("PROLSH", enterpriseid));
			List<Wfi_materdata> wfiMaterdataList = wfi_materdataService
					.list(new QueryWrapper<Wfi_materdata>().eq("PROLSH", enterpriseid));
			bdcshyqzd = Bdc_shyqzdService
					.list(new QueryWrapper<Bdc_shyqzd>().eq("enterpriseid",enterpriseid).ne("STATUS", "1"));
			boolean canEdit = canEdit(enterprise);
			if (canEdit) {
				throw new SupermapBootException("该企业信息已经提交，不能重复提交");
			}
			// 获取完整申报信息
			jsonString = getJSONString(type,enterprise, userList, wfiMaterclassList, wfiMaterdataList,bdcshyqzd);
		}else if(type.equals("2")) {
			userList = sysUserService.list(new QueryWrapper<SysUser>().eq("enterprise_id", enterpriseid).eq("shzt", "0"));
			//还得把未提交的用户改成已提交状态
			boolean canEdit = canEdit(enterprise);
			if (canEdit) {
				throw new SupermapBootException("该企业信息已经提交，不能重复提交");
			}
			jsonString = getJSONString(type,enterprise, userList, new ArrayList<Wfi_materclass>(), new ArrayList<Wfi_materdata>(),new ArrayList<Bdc_shyqzd>());
		}else if(type.equals("3")) {
			bdcshyqzd = Bdc_shyqzdService
					.list(new QueryWrapper<Bdc_shyqzd>().eq("enterpriseid",enterpriseid).eq("STATUS", "-1"));
			//还得把未提交的宗地从-1改成0
			boolean canEdit = canEdit(enterprise);
			if (canEdit) {
				throw new SupermapBootException("该企业信息已经提交，不能重复提交");
			}
			// 获取完整申报信息
			jsonString = getJSONString(type,enterprise, new ArrayList<SysUser>(), new ArrayList<Wfi_materclass>(), new ArrayList<Wfi_materdata>(),bdcshyqzd);
		}

		// 获取申报接口地址
		String enterpriseSbUrl = sys_configService.getConfigByKey("enterpriseSbUrl");
		if (StringHelper.isEmpty(enterpriseSbUrl)) {
			throw new SupermapBootException("未配置业务申报接口地址，请联系管理员进行配置！");
		}
		// 异步调用申报接口，使用线程池管理
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AsyncTaskConfig.class);
		AsyncTaskService taskService = context.getBean(AsyncTaskService.class);
		taskService.AsyncDeclareEnterprise(enterprise,jsonString, enterpriseSbUrl,userList,bdcshyqzd,type);
//		context.close();
	}

	public String getJSONString(String type,YsptEnterprise enterprise, List<SysUser> userList,
			List<Wfi_materclass> wfiMaterclassList, List<Wfi_materdata> wfiMaterdataList, List<Bdc_shyqzd> bdcshyqzds) {
		JSONObject jsonObject = new JSONObject();
		Map<String, Object> map = new HashMap<String, Object>();
		jsonObject.put("requestcode", type);
		jsonObject.put("requestseq", System.currentTimeMillis());
		map.put("ENTERPRISEID", enterprise.getId());
		map.put("ENTERPRISENAME", enterprise.getEnterpriseName());
		map.put("ENTERPRISECODE", enterprise.getEnterpriseCode());
		map.put("ENTERPRISEADDRESS", enterprise.getEnterpriseAddress());
		map.put("FRDBXM", enterprise.getFrdbxm());
		map.put("FRDBZJHM", enterprise.getFrdbzjhm());
		map.put("REGISTERNAME", enterprise.getRegisterName());
		map.put("REGISTERZJHM", enterprise.getRegisterZjhm());
		map.put("REGISTERPHONE", enterprise.getRegisterPhone());
		
		// 申请人
        JSONArray array_user = new JSONArray();
        for (SysUser user : userList) {
            Map<String, Object> map_user = new HashMap<String, Object>();
            map_user.put("NAME", user.getRealname());
            map_user.put("USERNAME", user.getUsername());
            map_user.put("PHONE", user.getPhone());
            map_user.put("ZJHM", user.getZjh());
            array_user.add(map_user);
        }
        map.put("SQRLIST", array_user);
        // 资料目录实例
        map.put("MATERCLASS", wfiMaterclassList);
        // 资料文件
        map.put("MATERDATA", wfiMaterdataList);
        
        map.put("ZDLIST", bdcshyqzds);

        jsonObject.put("data", map);
		return jsonObject.toJSONString();

	}

	public boolean canEdit(YsptEnterprise enterprise) {
		String status = enterprise.getStatus();
		if(StringHelper.isEmpty(status)) {
			return true;
		}
		if (status.equals("10")) {
			return true;
		}
		return false;
	}

	@Override
	public IPage<Map> projectlist(Page<Map> page, HttpServletRequest req) {
		Map<String, String> param = new HashMap<String, String>();
		param.put("enterpriseName", StringHelper.trim(req.getParameter("enterpriseName")));
		param.put("bhYzm", StringHelper.trim(req.getParameter("bhYzm")));
		return page.setRecords(ysptEnterpriseMapper.projectlist(page, param));
	}

	@Override
	public JSONObject searchBdcdyh(String bdcdyh) {
		 String coreQueryUrl = sys_configService.getConfigByKey("coreQueryUrl");
	        if(StringHelper.isEmpty(coreQueryUrl)) {
	            throw new SupermapBootException("未配置接口查询服务地址，请联系管理员进行配置");
	        }
	        String jsonstr = HttpClientUtil.requestPost( "{\n" +
	                "\"requestcode\":"+ ConstValueMrpc.RequestcodeEnum.DY.Value+ ",\n" +
	                "\"requestseq\": \"yyyyMMddHHmmssffffff\",\n" +
	                "\"data\": [{\n" +
	                "\"xzqdm\": \"450200\",\n" +
	                "\"bdcdyh\": \""+bdcdyh+"\"\n" +
	                "}]\n" +
	                "}", coreQueryUrl);
	        if(jsonstr == null) {
	            throw new SupermapBootException("接口返回数据为空，请检查权证号检索接口服务是否异常");
	        }
	        JSONObject jsonObject = JSONObject.parseObject(jsonstr);
	        return jsonObject;
	}

	@Override
	public void houseSearch(Map<String, String> param) {
		String enterpriseid = param.get("enterpriseid");
        String coreQueryUrl = sys_configService.getConfigByKey("coreQueryUrl");
        if(StringHelper.isEmpty(coreQueryUrl)) {
            throw new SupermapBootException("未配置宗地核验接口地址，请联系管理员进行配置");
        }
//        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String format = sdf.format(new Date());
        String numbers = RandomUtil.randomNumbers(6);
        String timeflag = format + numbers;
        // 请求JSON数据组装
        JSONObject object = new JSONObject();
        object.put("requestcode", ConstValueMrpc.RequestcodeEnum.ZD.Value);
        object.put("requestseq", timeflag);
        JSONArray dataArray = new JSONArray();
        Map<String, Object> map = new HashMap<>();
        map.put("bdcdyh", param.get("bdcdyh"));
        // 内部调用，使用机构代码为90
        map.put("jgdm", "90");
        map.put("xzqdm", "");
        JSONArray qlrArray = new JSONArray();
        Map<String, Object> qlrMap = new HashMap<>();
        qlrMap.put("qlrmc", param.get("qlrmc"));
        qlrMap.put("zjhm", param.get("qlrzjh"));
        qlrArray.add(qlrMap);
        map.put("qlr", qlrArray);
        dataArray.add(map);
        object.put("data", dataArray);

        String jsonstr = HttpClientUtil.requestPost(object.toJSONString(), coreQueryUrl);

        if(StringHelper.isEmpty(jsonstr)) {
            throw new SupermapBootException("宗地核验接口暂时无法访问，请联系管理员排查。");
        }
        JSONObject jsonObject = JSONObject.parseObject(jsonstr);
        if(jsonObject == null) {
            throw new SupermapBootException("宗地核验接口异常，返回为空，请联系管理员排查");
        }

        //解析接口返回的数据，并保存到宗地核验记录表中，前端显示宗地核验直接读表，方便做分页及记录
        //先将该流水号上一次核验出来的数据设置为已失效
        Shyqzdhis his = new Shyqzdhis();
        his.setZdzt(3);
        shyqzdhisService.update(his, new UpdateWrapper<Shyqzdhis>().eq("ENTERPRISEID",enterpriseid));

        JSONArray datalist = jsonObject.getJSONArray("data");
        if(datalist == null) {
            throw new SupermapBootException("查无宗地信息，请检查数据是否正确");
        }
        for(int i=0;i<datalist.size();i++) {
            JSONObject data = datalist.getJSONObject(i);
            JSONArray zdlist = data.getJSONArray("zdlist");
            if(zdlist == null || zdlist.isEmpty()) {
                throw new SupermapBootException("查无宗地信息，请检查数据是否正确");
            }
            JSONArray qlrlist = data.getJSONArray("qlrlist");
            for(int j=0;j<zdlist.size();j++) {
                JSONObject zd = zdlist.getJSONObject(j);
                Shyqzdhis shyqzdhis = new Shyqzdhis();
                shyqzdhis.setBdcdyh(zd.getString("bdcdyh"));
                shyqzdhis.setEnterpriseid(enterpriseid);
                shyqzdhis.setZl(zd.getString("zl"));
//                shyqzdhis.setPzmj(zd.getDouble("pzmj"));
                shyqzdhis.setZdmj(zd.getDouble("zdmj"));
                shyqzdhis.setCreatetime(new Date());
                shyqzdhis.setOperator("ivan");
                JSONArray cflist = zd.getJSONArray("cflist");
                if(cflist!=null && cflist.size()>0) {
                    shyqzdhis.setCfzt("1");
                }
                JSONArray dylist = zd.getJSONArray("dylist");
                if(dylist!=null && dylist.size()>0) {
                    shyqzdhis.setDyzt("1");
                }
                JSONArray yylist = zd.getJSONArray("yylist");
                if(yylist!=null && yylist.size()>0) {
                    shyqzdhis.setYyzt("1");
                }

                JSONObject json = new JSONObject();
                json.put("zd", zd);
                json.put("qlrlist", qlrlist);
                json = systemDictService.transForJson(json);
                shyqzdhis.setZdclob(json.toJSONString());
                shyqzdhisService.save(shyqzdhis);
            }
        }
		
	}

	@Override
	public void selectzd(JSONObject zdsdata) {
		String enterpriseid = zdsdata.getString("enterpriseid");
        JSONArray zdlist = zdsdata.getJSONArray("zdlist");
        YsptEnterprise enterprise = getOne(new QueryWrapper<YsptEnterprise>().eq("ID", enterpriseid));
        if(enterprise == null) {
            throw new SupermapBootException("未找到流水号为"+enterpriseid+"的企业信息，请联系管理员核查");
        }

        for(int i = 0;i < zdlist.size(); i++) {
            JSONObject zd = zdlist.getJSONObject(i);
            JSONObject zdclob = zd.getJSONObject("zdclob");
            JSONObject housedeail = zdclob.getJSONObject("zd");
            
            Shyqzdhis his = new Shyqzdhis();
            his.setZdzt(2);
            shyqzdhisService.update(his, new UpdateWrapper<Shyqzdhis>().eq("id",zd.getString("id")).eq("enterpriseid",enterpriseid));

            Bdc_shyqzd bdcshyqzd = new Bdc_shyqzd();
            bdcshyqzd.setId(StringHelper.getUUID());
            bdcshyqzd.setBdcdyh(zd.getString("bdcdyh"));
            bdcshyqzd.setEnterpriseid(enterpriseid);
            bdcshyqzd.setZl(zd.getString("zl"));
            bdcshyqzd.setZdmj(StringHelper.getDouble(zd.getString("zdmj")));
            bdcshyqzd.setZdclob(zdclob.toJSONString());
            bdcshyqzd.setStatus("-1");//未提交
            bdcshyqzd.setCreatetime(new Date());
            bdcshyqzd.setOperator("ivan");
            Bdc_shyqzdService.save(bdcshyqzd);

        }
		
	}

	@Override
	public void removehouse(String zdid, String enterpriseid) {
		if(StringHelper.isEmpty(zdid)) {
            throw new SupermapBootException("宗地id不能为空");
        }

        List<Bdc_shyqzd> bdcshyqzds = Bdc_shyqzdService.list(new QueryWrapper<Bdc_shyqzd>().eq("id", zdid).eq("enterpriseid",enterpriseid));
        if(bdcshyqzds.size()==0) {
            throw new SupermapBootException("找不到宗地信息");
        }
        for(Bdc_shyqzd zd : bdcshyqzds) {
        	Bdc_shyqzdService.removeById(zd.getId());
            //房源表中被标记为已选择的恢复为未选择
        	Shyqzdhis his = new Shyqzdhis();
            his.setZdzt(1);
            shyqzdhisService.update(his, new UpdateWrapper<Shyqzdhis>().eq("bdcdyh",zd.getBdcdyh()).eq("enterpriseid",enterpriseid).eq("zdzt","2"));
            //要移除关联的栋  待处理
            bdc_zrzService.remove(new QueryWrapper<Bdc_zrz>().eq("zdid", zdid));
        }
	}

}
