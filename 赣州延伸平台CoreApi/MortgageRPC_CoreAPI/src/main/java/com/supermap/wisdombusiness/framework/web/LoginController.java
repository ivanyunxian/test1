package com.supermap.wisdombusiness.framework.web;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.supemap.mns.client.CloudHttp;
import com.supemap.mns.model.Message;
import com.supermap.realestate.registration.util.ConfigHelper;
import com.supermap.realestate.registration.util.ConstValue;
import com.supermap.realestate.registration.util.GetProperties;
import com.supermap.realestate.registration.util.Global;
import com.supermap.realestate.registration.util.YwLogUtil;
import com.supermap.wisdombusiness.framework.model.SystemModule;
import com.supermap.wisdombusiness.framework.model.User;
import com.supermap.wisdombusiness.framework.model.User.UserStatus;
import com.supermap.wisdombusiness.framework.service.SystemModuleService;
import com.supermap.wisdombusiness.framework.service.UserService;
import com.supermap.wisdombusiness.utility.Helper;
import com.supermap.wisdombusiness.utility.StringHelper;
import com.supermap.wisdombusiness.utility.ValidateCode;
import com.supermap.wisdombusiness.workflow.dao.CommonDao;
import com.supermap.wisdombusiness.workflow.service.common.Common;
import com.supermap.wisdombusiness.workflow.service.wfi.SmStaff;

/**
 * 
 * @Description:登录controller
 * @author chenhl
 * @date 2015年7月10日 下午12:56:29
 * @Copyright SuperMap
 */
@Controller
@RequestMapping("/framework/login")
public class LoginController {
	private static CloudHttp httpother = new CloudHttp();
	// 跳转地址
	private final String prefix = "/framework/login/";

	// 模块管理service
	@Autowired
	private SystemModuleService moduleService;

	// 用户service
	@Autowired
	private UserService userService;
	
	@Autowired
	private CommonDao commonDao;
	@Autowired
	private SmStaff smStaff;
	/**
	 * 用户登录
	 * 
	 * @作者 chenhl
	 * @创建时间 2015年7月10日下午12:51:37
	 * @param currUser
	 * @param session
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/index", method = RequestMethod.POST)
	public String login(User currUser, HttpSession session, HttpServletRequest request, Model model) {
		Subject user = SecurityUtils.getSubject();
		Boolean flag = false;
		String password=currUser.getPassword();
		String modelUrl = prefix + "index";
		Cookie[] cookies = request.getCookies();
		if("1".equals(ConfigHelper.getNameByValue("cnchecked"))){
			//乌鲁木齐key登录
			String loginName = currUser.getLoginName();
			User cuser = userService.findByLoginName(loginName);
			if(!"1".equals(cuser.getIdentifyCn())){
				if (cookies != null&&cookies.length > 0) {
					String userCN = "";
					for (int i = 0; i < cookies.length; i++) {
						if (cookies[i].getName().equals("KOAL_CERT_SERIAL_NUMBER1")){
							userCN = cookies[i].getValue();
							break;
						}
					}
					if(StringHelper.isNotNull(userCN)){
						User cUser = userService.findUserByCN(userCN);
						String relPassWord = userService.getPassWord(loginName, password);
						if(StringHelper.isNotNull(relPassWord)){
							UsernamePasswordToken token = new UsernamePasswordToken(cUser.getLoginName(), relPassWord);
							token.setRememberMe(true);
							try {
								user.login(token);
								return "redirect:/app/framework/login/main";
							} catch (Exception e) {
								token.clear();
							}
						}
					}
				}
				model.addAttribute("msg", "未能读取到用户信息！");
				return modelUrl;
			}
		}
		String relPassWord = userService.getPassWord(currUser.getLoginName(),currUser.getPassword());
		UsernamePasswordToken token = new UsernamePasswordToken(currUser.getLoginName(), relPassWord);
		token.setRememberMe(true);
		try {
			user.login(token);
			User use=smStaff.getCurrentWorkStaff();
			String isLand=use.getIsLand();
			//session.setAttribute("uname", use.getLoginName());
//			if(isLand!=null&&isLand.equals("true")){
//			token.clear();
//			model.addAttribute("msg", "账号已登录,请勿重复登录!");
//			return modelUrl;
//		}
			
			System.out.println("session里面的ip+++++"+use.getAuserIp());
			if (userService.findByLoginName(currUser.getLoginName()).getStatus() == UserStatus.INVALID) {
				model.addAttribute("msg", "账号已经停用!");
				YwLogUtil.addYwLog("登录失败,账号已经停用!", ConstValue.SF.NO.Value, ConstValue.LOG.LOGIN);
			} else {
				if(use.getAuserIp()!=null&&!use.getAuserIp().equals("")){
					String[] ips = use.getAuserIp().split(",");
					String ip = Common.getIp(request);
				    System.out.println("这是本机获取到的Ip-------"+ip);
					for(String index:ips){
						System.out.println("这是数据库里的Ip&&&&&&"+index);
						if(index.equals(ip)||index==ip){
							modelUrl = "redirect:/app/framework/login/main";
							flag= true;
							break;
						}else{
							continue;
						}
					}
					if(!flag){
						session.setAttribute("uname", null);
						model.addAttribute("msg", "禁止此机登录");
						return modelUrl;
					}
				}
				Pattern pattern = Pattern.compile("^(?=.*[0-9])(?=.*[a-zA-Z])(?![0-9A-Za-z]+$)(.{6,10})$");
				String str=ConfigHelper.getNameByValue("passwordchecked");
				 if(!pattern.matcher(password).matches()&&str!=null&&str.equals("1")){
					 model.addAttribute("dangermsg", "您的密码存在安全隐患，请修改密码");
					 return modelUrl;
				 };
				modelUrl = "redirect:/app/framework/login/main";
				YwLogUtil.addYwLog("登录成功", ConstValue.SF.YES.Value, ConstValue.LOG.LOGIN);
			}
			return modelUrl;
		} catch (UnknownAccountException e) {
			token.clear();
			model.addAttribute("msg", "账号不存在!");
			return modelUrl;
		} catch (IncorrectCredentialsException e) {
			token.clear();
			model.addAttribute("msg", "用户名/密码错误!");
			return modelUrl;
		} catch (ExcessiveAttemptsException e) {
			token.clear();
			model.addAttribute("msg", "账户错误次数过多,暂时禁止登录!");
			YwLogUtil.addYwLog("登录失败,账户错误次数过多,暂时禁止登录!", ConstValue.SF.NO.Value, ConstValue.LOG.LOGIN);
			return modelUrl;
		} catch (Exception e) {
			token.clear();
			model.addAttribute("msg", "未知错误!");
			YwLogUtil.addYwLog("登录未知错误!", ConstValue.SF.NO.Value, ConstValue.LOG.UNKNOWN);
			return modelUrl;
		}
	}

	/**
	 * 退出登录
	 * 
	 * @作者 chenhl
	 * @创建时间 2015年7月10日下午12:53:29
	 * @return
	 */
	@RequestMapping(value = "/logout")
	public String logout() {
		/*String staffid=smStaff.getCurrentWorkStaffID();
		 user=userService.findById(staffid);
		user.setIsLand("false");
		commonDao.update(user);
		commonDao.flush();*/
		Subject currentUser = SecurityUtils.getSubject();
		if (SecurityUtils.getSubject().getSession() != null) {
			currentUser.logout();
		}
		return "redirect:/app/framework/login/index";
	}

	/**
	 * 跳转到登录页面
	 * 
	 * @作者 chenhl
	 * @创建时间 2015年7月10日下午12:54:00
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String loginIndex(HttpServletRequest request, HttpServletResponse response,Model m) throws UnsupportedEncodingException {
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			if (cookies.length != 0) {//获取cookie
				for (int i = 0; i < cookies.length; i++) {
					if (cookies[i].getName().equals("KOAL_CERT_SERIAL_NUMBER1")){
						User cUser = userService.findUserByCN(cookies[i].getValue());
						if(null!=cUser){
							request.setAttribute("loginName", cUser.getUserName());
						}
					}
				}
			}
		}
		String modelUrl = prefix + "index";
		String userstr = request.getParameter("staffcode");
		if(userstr!=null&&!userstr.equals("")){
			userstr = new String(userstr.getBytes("iso8859-1"), "utf-8");
		}
		if (userstr == null) {
			
			Subject user = SecurityUtils.getSubject();
			User curruser=userService.findByLoginName(userstr);
			if(curruser!=null){
				UsernamePasswordToken token = new UsernamePasswordToken(userstr, curruser.getPassword());
				token.setRememberMe(true);
				try {
					user.login(token);

					return "redirect:/app/framework/login/main";
				} catch (UnknownAccountException e) {
					token.clear();

					return modelUrl;
				} catch (IncorrectCredentialsException e) {
					token.clear();

					return modelUrl;
				} catch (ExcessiveAttemptsException e) {
					token.clear();

					YwLogUtil.addYwLog("登录失败,账户错误次数过多,暂时禁止登录!", ConstValue.SF.NO.Value, ConstValue.LOG.LOGIN);
					return modelUrl;
				} catch (Exception e) {
					token.clear();
					YwLogUtil.addYwLog("登录未知错误!", ConstValue.SF.NO.Value, ConstValue.LOG.UNKNOWN);
					return modelUrl;
				}
			}
			else{
				return modelUrl;
			}
			
			
		} else {
			return modelUrl;
		}

	}

	@RequestMapping(value = "/index2", method = RequestMethod.GET)
	public String loginIndex2(HttpServletRequest request, HttpServletResponse response) {

		String modelUrl = prefix + "index";
		Subject user = SecurityUtils.getSubject();
		UsernamePasswordToken token = new UsernamePasswordToken("admin", StringHelper.encryptMD5("123456"));
		token.setRememberMe(true);
		try {
			user.login(token);
			modelUrl = "redirect:/app/framework/login/main";
			return modelUrl;
		} catch (UnknownAccountException e) {
			token.clear();
			return modelUrl;
		} catch (IncorrectCredentialsException e) {
			token.clear();
			return modelUrl;
		} catch (ExcessiveAttemptsException e) {
			token.clear();
			return modelUrl;
		} catch (Exception e) {
			token.clear();
			return modelUrl;
		}
	}

	/**
	 * 登录成功后跳转main页面
	 * 
	 * @作者 chenhl
	 * @创建时间 2015年7月10日下午12:55:16
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/main", method = RequestMethod.GET)
	public String loginMain(HttpServletRequest request, HttpServletResponse response, Model model) {
		Subject user = SecurityUtils.getSubject();
		User u = (User) user.getPrincipal();
		
		//String isLand=u.getIsLand();
//		if(isLand!=null&&isLand.equals("true")){
//			return prefix + "index";
//		}else{
//			u.setIsLand("true");
//			commonDao.update(u);
//			commonDao.flush();
//		}
		if (u != null) {
//			 String msg = (String) request.getSession().getAttribute("dangermsg");
//			if(msg!=null&&!msg.equals("")){
//				model.addAttribute("msg", msg);
//			}
			
			model.addAttribute("username", userService.findById(u.getId()).getUserName());
			model.addAttribute("userid", u.getId());
		} else {
			return prefix + "index";
		}
		return prefix + "main";
	}

	/**
	 * 生成验证码
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = "/validateCode")
	public void validateCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setHeader("Cache-Control", "no-cache");
		String verifyCode = ValidateCode.generateTextCode(ValidateCode.TYPE_NUM_ONLY, 4, null);
		request.getSession().setAttribute("validateCode", verifyCode);
		response.setContentType("image/jpeg");
		BufferedImage bim = ValidateCode.generateImageCode(verifyCode, 90, 30, 3, true, Color.WHITE, Color.BLACK, null);
		ImageIO.write(bim, "JPEG", response.getOutputStream());
	}

	/**
	 * 根据登陆用户获取模块
	 * 
	 * @param id
	 * @param request
	 * @param response
	 * @author diaoliwei
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 */
	@RequestMapping(value = "/{sysid}/moduletree", method = RequestMethod.POST)
	public @ResponseBody List<SystemModule> getSysModuleTree(@PathVariable("sysid") String id, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		Subject user = SecurityUtils.getSubject();
		User u = (User) user.getPrincipal();
		if (u != null) {
			List<SystemModule> modules = moduleService.getSysModulesByUser(u, id);
			return modules;
		} else {
			response.setContentType("text/html");
			request.getRequestDispatcher("/index").forward(request, response);
			return null;
		}
	}

	/**
	 * 跳转修改密码页面
	 * 
	 * @作者 chenhl
	 * @创建时间 2015年7月10日下午12:55:16
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/updatepd", method = RequestMethod.GET)
	public String updatepd(HttpServletRequest request, HttpServletResponse response, Model model) {
		String str=ConfigHelper.getNameByValue("passwordchecked");
		if(str!=null&&str.equals("1")){
			model.addAttribute("passwordchecked","新密码必须包含数字字母和特殊字符长度为6-10位");
		}
		return prefix + "updateInfo";
	}

	@RequestMapping(value = "/userInfo", method = RequestMethod.GET)
	public String userInfopd(HttpServletRequest request, HttpServletResponse response, Model model) {

		return prefix + "UserInfo";
	}
	/**
	 * 关闭窗口时修改用户登录状态
	 * 
	 * @param request
	 * @param response
	 * @return 
	 * @return
	 */
	@RequestMapping(value = "/closewindow", method = RequestMethod.GET)
	public void updatUser(HttpServletRequest request, HttpServletResponse response) {
		String staffid=smStaff.getCurrentWorkStaffID();
		User user=userService.findById(staffid);
		if(user!=null){
			user.setIsLand("false");
			commonDao.saveOrUpdate(user);
			commonDao.flush();
		}
		
	}
	
	/**
	 * 为四川提供通过门户单点登录支持
	 * @param session
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/ssoLogin", method = RequestMethod.GET)
	public String ssoLogin(HttpSession session, HttpServletRequest request) 
	{
		String modelUrl="redirect:/app/framework/login/index";
		//传递过来的令牌
		String token = request.getParameter("token");
		//传递过来当前登录门户的用户id
		String uid = request.getParameter("uid");
		Subject userSubject = SecurityUtils.getSubject();
		if(uid!=null && !uid.isEmpty())
		{
			User user= userService.findById(uid);
			if(user!=null)
			{
				UsernamePasswordToken uptoken = new UsernamePasswordToken(user.getLoginName(), user.getPassword());
				uptoken.setRememberMe(true);
				userSubject.login(uptoken);
				modelUrl = "redirect:/app/framework/login/main";
			}
		}
		return modelUrl;
	}
	
}
