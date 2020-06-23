package org.jeecg.modules.system.controller;

import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.jeecg.common.api.vo.Result;
import org.jeecg.common.exception.SupermapBootException;
import org.jeecg.common.system.util.JwtUtil;
import org.jeecg.modules.system.entity.SysDepart;
import org.jeecg.modules.system.entity.YsptEnterprise;
import org.jeecg.modules.system.service.IRegisterService;
import org.jeecg.modules.system.service.ISysDepartService;
import org.jeecg.modules.system.util.TecentTextHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import cn.hutool.core.lang.UUID;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName PlatformRegisterController
 * @Description 延伸平台企业注册功能
 * @Author ivan
 * @Date 2019/9/2 14:45
 */
@Slf4j
@Api(tags="延伸平台企业注册接口")
@RestController
@RequestMapping("/yspt/enterprise")
public class PlatformRegisterController {

	@Autowired
	private IRegisterService registerService;
	@Autowired
	private ISysDepartService sysDepartService;
	
	/**
	 * 
	 * @param phonenum
	 * @param request
	 * @param session
	 * @Description 延伸平台企业注册时发送短信
	 * @return
	 */
	@RequestMapping(value = "/sendtextmsg", method = RequestMethod.POST)
	public Result<String> sendTextMsg(@RequestBody JSONObject phonenum,HttpServletRequest request,HttpSession session) {
		Result<String> result = new Result<String>();
		String verifyCode = String
                .valueOf(new Random().nextInt(899999) + 100000);
		//重置密码 584394  注册584443
		JSONObject json = TecentTextHelper.sendMsg(phonenum.getString("phone"), "584443", "赣州市不动产商品房系统", verifyCode);
		if(((JSONObject)JSONObject.parse(json.getJSONArray("SendStatusSet").get(0).toString())).getString("Code").equals("Ok")) {
			result.setSuccess(true);
			result.setMessage("成功发送");
			result.setResult(verifyCode);
			session.setAttribute("authCode", verifyCode);
		}else {
			result.setSuccess(false);
			result.setMessage("发送失败");
		}
		return result;
	}
	
	/**
	 * 
	 * @param obj
	 * @param request
	 * @Description 延伸平台企业对外接口发送短信
	 * @return
	 */
	@RequestMapping(value = "/coreapisendmsg", method = RequestMethod.POST)
	public Result<String> coreApiSendTextMsg(@RequestBody JSONObject obj,HttpServletRequest request) {
		Result<String> result = new Result<String>();
		//注册584443 驳回599296 成功599688
		JSONObject json = TecentTextHelper.sendMsg(obj.getString("phone"), obj.getString("textcode"), "赣州市不动产商品房系统", obj.getString("code"));
		if(((JSONObject)JSONObject.parse(json.getJSONArray("SendStatusSet").get(0).toString())).getString("Code").equals("Ok")) {
			result.setSuccess(true);
			result.setMessage("成功发送");
			result.setResult(obj.getString("code"));
		}else {
			result.setSuccess(false);
			result.setMessage("发送失败");
		}
		return result;
	}
	
	/**
	 * 
	 * @param codejson
	 * @param request
	 * @param session
	 * @Description 延伸平台企业页面验证短信
	 * @return
	 */
	@RequestMapping(value = "/verifytextcode", method = RequestMethod.POST)
	public Result<String> verifyTextCode(@RequestBody JSONObject codejson,HttpServletRequest request,HttpSession session) {
		Result<String> result = new Result<String>();
		String code = codejson.getString("code");
		String realcode =(String) session.getAttribute("authCode");
		if(!code.equalsIgnoreCase(realcode)) {
			result.setSuccess(false);
			result.setMessage("验证码不正确");
		}else {
			result.setSuccess(true);
			result.setMessage("验证通过");
		}
		return result;
	}
	
	/**
	 * 添加新数据 添加用户新建的企业对象数据,并保存到数据库
	 * @author ivan
	 * @param enterprise
	 * @return
	 */
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public Result<YsptEnterprise> add(@RequestBody YsptEnterprise enterprise, HttpServletRequest request) {
		Result<YsptEnterprise> result = new Result<YsptEnterprise>();
		try {
			registerService.saveEnterpriseData(enterprise);
			SysDepart oldSysDepart = sysDepartService.getOne(new QueryWrapper<SysDepart>().eq("enterprise_id",enterprise.getId()));
			if(oldSysDepart==null|| oldSysDepart.getId()==null) {
				//保存部门 每个部门对应一个企业
				SysDepart sysDepart = new SysDepart();
				sysDepart.setDepartName(enterprise.getEnterpriseName());
				sysDepart.setDeptType("2");//默认企业中心
				sysDepart.setDeptZjh(enterprise.getEnterpriseCode());
				sysDepart.setDeptZjlx("7");//默认营业执照
				sysDepart.setJgdm("09");//默认不动产
				sysDepart.setEnterpriseId(enterprise.getId());
				sysDepart.setId(UUID.randomUUID().toString().replace("-", ""));
				sysDepart.setCreateBy("ivan");
				sysDepartService.saveDepartData(sysDepart, "ivan");
			}
			result.success("添加成功！");
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			result.error500("操作失败");
		}
		return result;
	}
	
	/**
	 * @Description 延伸平台企业获取企业信息
	 * @param request
	 * @param response
	 * @return
	 */
	@GetMapping(value = "/getenterprise")
    public  Result<YsptEnterprise> getMaterlist(HttpServletRequest request, HttpServletResponse response) {
        Result<YsptEnterprise> result = new Result<YsptEnterprise>();
        try {
            String id=request.getParameter("id");
            YsptEnterprise enterprise = registerService.getById(id);
            result.setSuccess(true);
            result.setResult(enterprise);
            result.setMessage("获取成功");

            return result;
        } catch(SupermapBootException e) {
            log.error(e.getMessage(), e);
            result.setSuccess(false);
            result.setMessage("获取数据失败:"+e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            result.setSuccess(false);
            result.setMessage("获取数据失败");
        }
        return result;

    }
	
	/**
	 * 添加新数据 添加用户新建的企业对象数据,并保存到数据库
	 * 
	 * @param enterprise
	 * @return
	 */
	/**
	  *  校验用户账号是否唯一<br>
	  *  可以校验其他 需要检验什么就传什么。。。
    *
    * @param sysUser
    * @return
    */
   @RequestMapping(value = "/checkOnlyUser", method = RequestMethod.GET)
   public Result<Boolean> checkOnlyUser(YsptEnterprise enterpriseparam) {
       Result<Boolean> result = new Result<>();
       //如果此参数为false则程序发生异常
       result.setResult(true);
       try {
           //通过传入信息查询新的用户信息
    	   YsptEnterprise enterprise = registerService.getOne(new QueryWrapper<YsptEnterprise>(enterpriseparam));
           if (enterprise != null) {
               result.setSuccess(false);
               result.setMessage("用户账号已存在");
               return result;
           }

       } catch (Exception e) {
           result.setSuccess(false);
           result.setMessage(e.getMessage());
           return result;
       }
       result.setSuccess(true);
       return result;
   }

	/**
	 * 编辑数据 编辑企业的部分数据,并保存到数据库
	 * 
	 * @param enterprise
	 * @return
	 */
	@RequestMapping(value = "/edit", method = RequestMethod.PUT)
	public Result<YsptEnterprise> edit(@RequestBody YsptEnterprise enterprise, HttpServletRequest request) {
		String username = JwtUtil.getUserNameByToken(request);
		enterprise.setUpdateBy(username);
		Result<YsptEnterprise> result = new Result<YsptEnterprise>();
		YsptEnterprise enterpriseEntity = registerService.getById(enterprise.getId());
		if (enterpriseEntity == null) {
			result.error500("未找到对应实体");
		} else {
			boolean ok = registerService.updateEnterpriseDataById(enterprise, username);
			// TODO 返回false说明什么？
			if (ok) {
				result.success("修改成功!");
			} else {
				result.success("修改失败!");
			}
		}
		return result;
	}
	
	 /**
     *   通过id删除
    * @param id
    * @return
    */
   @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
   public Result<YsptEnterprise> delete(@RequestParam(name="id",required=true) String id) {

       Result<YsptEnterprise> result = new Result<YsptEnterprise>();
       YsptEnterprise enterprise = registerService.getById(id);
       if(enterprise==null) {
           result.error500("未找到对应实体");
       }else {
           boolean ok = registerService.delete(id);
           if(ok) {
               result.success("删除成功!");
           }
       }
       return result;
   }
}
