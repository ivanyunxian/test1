package org.jeecg.modules.mortgagerpc.task;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.exception.SupermapBootException;
import org.jeecg.common.exception.SupermapBootExceptionHandler;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.common.system.util.HttpClientUtil;
import org.jeecg.common.system.util.StringHelper;
import org.jeecg.common.util.ConstValueMrpc;
import org.jeecg.common.util.RSACoder;
import org.jeecg.common.util.SpringContextUtils;
import org.jeecg.modules.mortgagerpc.entity.*;
import org.jeecg.modules.mortgagerpc.mongo.serviece.MongoServerce;
import org.jeecg.modules.mortgagerpc.service.*;
import org.jeecg.modules.system.entity.SysDepartConfig;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.entity.YsptEnterprise;
import org.jeecg.modules.system.service.ISysDepartConfigService;
import org.jeecg.modules.system.service.ISysUserService;
import org.jeecg.modules.system.util.TecentTextHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @ClassName AsyncTaskService
 * @Description 异步任务处理类
 * @Author taochunda
 * @Date 2019-08-30 14:27
 */
@Slf4j
@Service
public class AsyncTaskService {

    static IWfi_proinstService wfi_proinstService;

    static IBdc_dyService bdc_dyService;

    static IBdc_qlService bdc_qlService;

    static IBdc_sqrService bdc_sqrService;

    static IBdc_qldyService bdc_qldyService;

    static IWfi_materclassService wfi_materclassService;

    static IWfi_materdataService wfi_materdataService;

    static IBdc_fsqlService bdc_fsqlService;

    static IWfi_slxmshService wfi_slxmshService;

    static MongoServerce mongoserverce;

    static ISysBaseAPI sysBaseAPI;

    static ISys_configService sys_configService;

    static ICoreAPIService coreAPIService;

    static ISysDepartConfigService sysDepartConfigService;
    
    static IYspt_ywsqService yspt_ywsqService;
    
    static IBdc_shyqzdService bdc_shyqzdService;
    
    static ISysUserService sysUserService;
    
    

    static {
        if (wfi_proinstService == null) {
            wfi_proinstService = SpringContextUtils.getBean(IWfi_proinstService.class);
        }
        if (bdc_dyService == null) {
            bdc_dyService = SpringContextUtils.getBean(IBdc_dyService.class);
        }
        if (bdc_qlService == null) {
            bdc_qlService = SpringContextUtils.getBean(IBdc_qlService.class);
        }
        if (bdc_sqrService == null) {
            bdc_sqrService = SpringContextUtils.getBean(IBdc_sqrService.class);
        }
        if (bdc_qldyService == null) {
            bdc_qldyService = SpringContextUtils.getBean(IBdc_qldyService.class);
        }
        if (wfi_materclassService == null) {
            wfi_materclassService = SpringContextUtils.getBean(IWfi_materclassService.class);
        }
        if (wfi_materdataService == null) {
            wfi_materdataService = SpringContextUtils.getBean(IWfi_materdataService.class);
        }
        if (bdc_fsqlService == null) {
            bdc_fsqlService = SpringContextUtils.getBean(IBdc_fsqlService.class);
        }
        if (wfi_slxmshService == null) {
            wfi_slxmshService = SpringContextUtils.getBean(IWfi_slxmshService.class);
        }
        if (sysBaseAPI == null) {
            sysBaseAPI = SpringContextUtils.getBean(ISysBaseAPI.class);
        }
        if (sys_configService == null) {
            sys_configService = SpringContextUtils.getBean(ISys_configService.class);
        }
        if (coreAPIService == null) {
            coreAPIService = SpringContextUtils.getBean(ICoreAPIService.class);
        }
        if (sysDepartConfigService == null) {
            sysDepartConfigService = SpringContextUtils.getBean(ISysDepartConfigService.class);
        }
        if (yspt_ywsqService == null) {
        	yspt_ywsqService = SpringContextUtils.getBean(IYspt_ywsqService.class);
        }
        if (bdc_shyqzdService == null) {
        	bdc_shyqzdService = SpringContextUtils.getBean(IBdc_shyqzdService.class);
        }
        if (sysUserService == null) {
        	sysUserService = SpringContextUtils.getBean(ISysUserService.class);
        }
    }

    /**
     * @Author taochunda
     * @Description 异步调用申报信息接口
     * @Date 2019-08-30 11:06
     * @Param [proinst, jsonString, declareUrl]
     * @return void
     **/
    @Async
    @Transactional(rollbackFor = Exception.class)
    public void AsyncDeclare(Wfi_proinst proinst, String jsonString, String declareUrl) {
        try {
            if (proinst == null) {
                return;
            }

            String result = HttpClientUtil.requestPost(jsonString, declareUrl);
            if (StringHelper.isEmpty(result)) {
                throw new Exception("申报接口暂时无法访问，请联系管理员");
            }
            JSONObject jsonObject = JSONObject.parseObject(result);
            if (jsonObject.isEmpty()) {
                proinst.setShzt(11);
                Wfi_slxmsh slxmsh = createSlxmsh(proinst, "申报接口响应为空，请联系管理员核查该项目是否成功申报");
                wfi_slxmshService.save(slxmsh);
                wfi_proinstService.updateById(proinst);
                return ;
            }
            Boolean state = jsonObject.getBoolean("state");
            String msg = jsonObject.getString("msg");
            if (!state) {
                proinst.setShzt(11);
                Wfi_slxmsh slxmsh = createSlxmsh(proinst, msg);
                wfi_slxmshService.save(slxmsh);
                wfi_proinstService.updateById(proinst);
                return ;
            }
            // 修改审核状态，添加审核意见记录
            proinst.setShzt(20);
            Wfi_slxmsh slxmsh = createSlxmsh(proinst, "预审通过");
            wfi_slxmshService.save(slxmsh);
        } catch (Exception e) {
            proinst.setShzt(-11);
            Wfi_slxmsh slxmsh = createSlxmsh(proinst, e.getMessage());
            wfi_slxmshService.save(slxmsh);
            e.printStackTrace();
        }
        wfi_proinstService.updateById(proinst);
    }

    /**
     * @Author taochunda
     * @Description 异步调用申报信息接口，调用类型为ConstValueMrpc.InvokeType.JRJGDY.Value时使用
     * @Date 2019-09-06 17:24
     * @Param [proinst, jsonString, declareUrl]
     * @return void
     **/
    @Async
    @Transactional(rollbackFor = Exception.class)
    public void AsyncDeclareProject(String requestcode, Wfi_proinst proinst, String jsonString, String declareUrl) {
        JSONObject resultObject = new JSONObject();
        resultObject.put("requestcode", requestcode);
        resultObject.put("requestseq", System.currentTimeMillis());
        List<Map> data = new ArrayList<Map>();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("ywlsh", proinst.getOrgLsh());
        map.put("xzqdm", proinst.getDivisionCode());
        map.put("jgdm", null);
        map.put("ywhj", "初审");
        map.put("shzt", "通过");
        map.put("shyj", "材料完整，同意办理");
        map.put("blryxm", proinst.getAcceptor());
        map.put("blrylxdh", null);
        map.put("bz", null);
        String desc = "";
        try {
            String result = HttpClientUtil.requestPost(jsonString, declareUrl);
            if (StringHelper.isEmpty(result)) {
                throw new SupermapBootExceptionHandler("申报接口暂时无法访问，请联系管理员");
            }
            JSONObject jsonObject = JSONObject.parseObject(result);
            if (jsonObject.isEmpty()) {
                throw new SupermapBootExceptionHandler("申报接口响应为空，请联系管理员核查该项目是否成功申报");
            }
            Boolean state = jsonObject.getBoolean("state");
            String msg = jsonObject.getString("msg");
            if (!state) {
                // 申报失败
                throw new SupermapBootExceptionHandler(msg);
            }
            // 修改审核状态，添加审核意见记录
            proinst.setShzt(20);
            Wfi_slxmsh slxmsh = createSlxmsh(proinst, "预审通过");
            wfi_slxmshService.save(slxmsh);
            wfi_proinstService.updateById(proinst);
            // 更新业务状态信息，调用银行接口
            data.add(map);
            resultObject.put("data", data);
            desc = "更新业务状态【预审通过】";
            UpdateBusinessStatus(resultObject, proinst, desc);
        } catch (SupermapBootException e) {
            // 一般的配置错误在这里捕获，不进行业务数据删除
            log.info(e.getMessage(), e);
            sysBaseAPI.addLog("更新业务状态信息失败-流水号：" + proinst.getProlsh() + " 详情：" + e.getMessage(), CommonConstant.INTERFACE_TYPE_3, null);
        } catch (SupermapBootExceptionHandler e) {
            // 申报失败，更新业务状态并删除业务数据
            log.info(e.getMessage(), e);
            map.put("shzt", "驳回");
            map.put("shyj", e.getMessage());
            data.add(map);
            resultObject.put("data", data);
            desc = "更新业务状态【审核驳回】";
            boolean b = UpdateBusinessStatus(resultObject, proinst, desc);
            if (b) {
                // 申报失败删除项目
                deleteProject(proinst);
            }
        } catch (Exception e) {
            // 其他异常，更新业务状态并删除业务数据
            map.put("shzt", "驳回");
            map.put("shyj", e.getMessage());
            data.add(map);
            resultObject.put("data", data);
            desc = "更新业务状态【审核驳回】";
            boolean b = UpdateBusinessStatus(resultObject, proinst, desc);
            if (b) {
                // 申报失败删除项目
                deleteProject(proinst);
            }
            e.printStackTrace();
        }
    }

    /**
     * @Author taochunda
     * @Description 更新业务状态信息接口
     * @Date 2019-09-10 10:52
     * @Param [resultObject, updateBusinessStatusUrl]
     * @return boolean
     **/
    private boolean UpdateBusinessStatus(JSONObject resultObject, Wfi_proinst proinst, String desc) {
        boolean flag = false;
        String jsonstr = null;
        try {
            Map<String, Object> map = coreAPIService.applicationTokenFromDepart(proinst.getUserId());
            String token = StringHelper.formatObject(map.get("token"));
            if (StringHelper.isEmpty(token)) {
                throw new Exception("获取金融机构授权token失败-流水号：" + proinst.getProlsh());
            }
            if (StringHelper.isEmpty(map.get("configMap"))) {
                throw new Exception("获取更新业务状态信息接口失败-流水号：" + proinst.getProlsh());
            }
            Map<String, SysDepartConfig> configMap = (Map<String, SysDepartConfig>) map.get("configMap");
            SysDepartConfig config = configMap.get(ConstValueMrpc.RequestcodeEnum.GXYWZT.Value);
            if(config == null) {
                throw new Exception("未配置更新业务状态信息接口，请联系管理员进行配置-流水号：" + proinst.getProlsh());
            }
            jsonstr = HttpClientUtil.requestPost(RSACoder.encryptByPrivateKeyBase64(resultObject.toJSONString()), config.getValue() + "?token=" + token);
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

    private Wfi_slxmsh createSlxmsh(Wfi_proinst proinst, String shyj) {
        Wfi_slxmsh slxmsh = new Wfi_slxmsh();
        slxmsh.setProlsh(proinst.getProlsh());
        slxmsh.setShry("系统自动审核");
        slxmsh.setShsj(new Date());
        slxmsh.setShyj(shyj);
        slxmsh.setShzt(proinst.getShzt());
        return slxmsh;
    }

    /**
     * @Author taochunda
     * @Description 删除业务数据
     * @Date 2019-09-10 10:54
     * @Param [wfi_proinst]
     * @return void
     **/
    @Transactional(rollbackFor = Exception.class)
    void deleteProject(Wfi_proinst wfi_proinst) {
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
        wfi_proinstService.deleteProject(wfi_proinst);
    }

	public void AsyncDeclareEnterprise(YsptEnterprise enterprise, String jsonString, String declareUrl,
			List<SysUser> userList, List<Bdc_shyqzd> bdcshyqzd, String type) {
		try {
			if (enterprise == null) {
	            return;
	        }

	        String result = HttpClientUtil.requestPost(jsonString, declareUrl);
	        if (StringHelper.isEmpty(result)) {
	            throw new Exception("申报接口暂时无法访问，请联系管理员");
	        }
	        JSONObject jsonObject = JSONObject.parseObject(result);
	        if (jsonObject.isEmpty()) {
	        	//创建项目失败 默认发送短信20200101
	        	if(type.equals("1")) {
					enterprise.setStatus("20");
					enterprise.setBhYzm("20200101");
		        	enterprise.setMsg("系统自动驳回");
				}else if(type.equals("2")) {//用户提交 只改变用户状态
					if(userList!=null ) {
		        		for(SysUser user :userList) {
		        			user.setShzt("30");
		        			sysUserService.updateById(user);
		        		}
		        	}
				}else if(type.equals("3")) {//宗地提交 只改变宗地状态
					if(bdcshyqzd!=null) {
		        		for(Bdc_shyqzd zd :bdcshyqzd) {
		        			zd.setStatus("2");
		        			bdc_shyqzdService.updateById(zd);
		        		}
		        	}
				}
	        	yspt_ywsqService.updateById(enterprise);
	            return ;
	        }
	        Boolean state = jsonObject.getBoolean("state");
	        String msg = jsonObject.getString("msg");
	        if (!state) {
	        	//创建项目失败 默认发送短信20200102
	        	if(type.equals("1")) {
					enterprise.setStatus("20");
					enterprise.setBhYzm("20200101");
		        	enterprise.setMsg("系统自动驳回");
				}else if(type.equals("2")) {//用户提交 只改变用户状态
					if(userList!=null ) {
		        		for(SysUser user :userList) {
		        			user.setShzt("30");
		        			sysUserService.updateById(user);
		        		}
		        	}
				}else if(type.equals("3")) {//宗地提交 只改变宗地状态
					if(bdcshyqzd!=null) {
		        		for(Bdc_shyqzd zd :bdcshyqzd) {
		        			zd.setStatus("2");
		        			bdc_shyqzdService.updateById(zd);
		        		}
		        	}
				}
	        	yspt_ywsqService.updateById(enterprise);
	            return ;
	        }
	        if(type.equals("1")) {//全部提交 所有状态都需要改变
	        	if(userList!=null ) {
	        		for(SysUser user :userList) {
	        			user.setShzt("10");
	        			sysUserService.updateById(user);
	        		}
	        	}
	        	if(bdcshyqzd!=null) {
	        		for(Bdc_shyqzd zd :bdcshyqzd) {
	        			zd.setStatus("0");
	        			bdc_shyqzdService.updateById(zd);
	        		}
	        	}
	        	// 修改审核状态，添加审核意见记录 应该是已提交
		        enterprise.setStatus("10");
		        enterprise.setMsg(msg);
	        }else if(type.equals("2")) {//用户提交 只改变用户状态
	        	if(userList!=null ) {
	        		for(SysUser user :userList) {
	        			user.setShzt("10");
	        			sysUserService.updateById(user);
	        		}
	        	}
	        }else if(type.equals("3")) {//宗地提交 只改变宗地状态
	        	if(bdcshyqzd!=null) {
	        		for(Bdc_shyqzd zd :bdcshyqzd) {
	        			zd.setStatus("0");
	        			bdc_shyqzdService.updateById(zd);
	        		}
	        	}
	        }
		}catch(Exception ex) {
			//异常驳回  默认发送短信 20200201
			if(type.equals("1")) {
				enterprise.setStatus("20");
				enterprise.setBhYzm("20200101");
	        	enterprise.setMsg("系统自动驳回");
			}else if(type.equals("2")) {//用户提交 只改变用户状态
				if(userList!=null ) {
	        		for(SysUser user :userList) {
	        			user.setShzt("30");
	        			sysUserService.updateById(user);
	        		}
	        	}
			}else if(type.equals("3")) {//宗地提交 只改变宗地状态
				if(bdcshyqzd!=null) {
	        		for(Bdc_shyqzd zd :bdcshyqzd) {
	        			zd.setStatus("2");
	        			bdc_shyqzdService.updateById(zd);
	        		}
	        	}
			}
        	ex.printStackTrace();
        	
		}
		yspt_ywsqService.updateById(enterprise);
	}
	
/*	public static String sendMsg(YsptEnterprise enterprise,String verifyCode) {
		verifyCode = (verifyCode==null || verifyCode.equals(""))?"nomessage":verifyCode;
//		String verifyCode = String
//	            .valueOf(new Random().nextInt(899999) + 100000);
		try {
			//584443 注册  要换成审核结果
			TecentTextHelper.sendMsg(enterprise.getRegisterPhone(), "584443", "赣州市不动产商品房系统", verifyCode);
		}catch(Exception ex) {
			ex.printStackTrace();
			return "nomessage";
		}
		
		return verifyCode;
	}*/
	
	
	
}
