package com.supermap.realestate.registration.web;

import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.supermap.realestate.registration.model.LOG_ACCESS;
import com.supermap.realestate.registration.service.ExteriorApiService;
import com.supermap.realestate.registration.util.ConfigHelper;
import com.supermap.realestate.registration.util.ConstValue;
import com.supermap.realestate.registration.util.ProjectHelper;
import com.supermap.realestate.registration.util.RequestHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import com.supermap.wisdombusiness.workflow.service.common.DateUtil;



/**
 * @description ：提供给外部系统调用的
 * @ClassName ：ExteriorApiController
 * @author ：huangpeifeng
 * @date : 20180528
 */
@Controller
@RequestMapping("/exteriorApi")
public class ExteriorApiController {
	
	@Autowired
	private ExteriorApiService exteriorApiService;
	
	@Autowired
	private CommonDao dao;
	
	private final String asDJXT = "DJXT_"; //登记系统标识
	
	private Map<String, String> cacheUser = Collections.synchronizedMap(new HashMap<String, String>());//缓存正确的登陆用户
	
	private Set<String> cacheNoLockedUsers = Collections.synchronizedSet(new HashSet<String>());//缓存没被锁的用户,每天清空
	
	/**
	 * 根据单元号生成依赖值、单元类型生成幢和户的不动产单元号
	 * 
	 * @author huangpeifeng
	 * @date 20180528
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "/getBDCDYH", method = RequestMethod.POST)
	public @ResponseBody Map<String, String> GetBDCDYH(HttpServletRequest request, HttpServletResponse response) 
			throws UnsupportedEncodingException {
		
		Map<String, String> mapResult = new HashMap<String, String>();
		String logaccesscontrol = ConfigHelper.getNameByValue("LOGACCESSCONTROL");
		if("3".equals(logaccesscontrol)){
			mapResult.put("SUCCESS", "false");
			mapResult.put("MSG", "接口已关闭,请联系管理员!");	
			return mapResult;
		}
		LOG_ACCESS logaccess = new LOG_ACCESS();
		StringBuilder derMsg = new StringBuilder();
		String userName = RequestHelper.getParam(request, "USERNAME");// 用户名
		String passWord = RequestHelper.getParam(request, "PASSWORD");// 密码
		String hostname = RequestHelper.getParam(request, "HOSTNAME");// 计算机名
		String mac = RequestHelper.getParam(request, "MAC");// mac
		String relyOnValue = RequestHelper.getParam(request, "RELYONVALUE");// 单元号生成依赖值
		String dylx = RequestHelper.getParam(request, "DYLX");// 单元类型
		boolean logflag = "1".equals(logaccesscontrol);
		int locknumber = 10;//锁定数量,超过这个数量就锁定
		int lockdays = 7;//锁定天数
		//检查参数是否符合要求
		if(StringHelper.isEmpty(userName)){
			derMsg.append("USERNAME参数为空!");
		}
		if(StringHelper.isEmpty(passWord)){
			derMsg.append("PASSWORD参数为空!");
		}else{
			passWord = com.supermap.wisdombusiness.utility.StringHelper.encryptMD5(passWord);
		}
		if(StringHelper.isEmpty(dylx)){
			derMsg.append("DYLX参数为空!");
		}else{
			if(!"03".equals(dylx) && !"04".equals(dylx)){
				derMsg.append("目前只支持生成幢和户的不动产单元号,请确认DYLX参数是否正确!");
			}
		}
		if(StringHelper.isEmpty(relyOnValue)){
			derMsg.append("relyOnValue参数为空!");
		}else{
			if(relyOnValue.trim().length()==28){
				String regEx = "";
				if("03".equals(dylx)){
					regEx = "^[1-9]\\d{11}[A-Z]{2}\\d{5}[W]\\d{8}$";			
				}else if("04".equals(dylx)){
					regEx = "^[1-9]\\d{11}[A-Z]{2}\\d{5}[F]\\d{4}[0]{4}$";
				}
				if(!regEx.isEmpty()){
					Pattern pattern = Pattern.compile(regEx);
					Matcher matcher = pattern.matcher(relyOnValue);
					if(!matcher.find()){
						derMsg.append("relyOnValue参数格式不正确,请按照不动产单元号的格式!");
					}
				}	
			}else{
				derMsg.append("relyOnValue值长度不对,正确是28位");
			}
		}
		if(StringHelper.isEmpty(mac)){
			derMsg.append("MAC参数为空!");
		}else{
			String regEx = "^[A-Za-z\\d]{2}[-:][A-Za-z\\d]{2}[-:][A-Za-z\\d]{2}[-:][A-Za-z\\d]{2}[-:][A-Za-z\\d]{2}[-:][A-Za-z\\d]{2}$";
			Pattern pattern = Pattern.compile(regEx);
			Matcher matcher = pattern.matcher(mac);
			if(!matcher.matches()){
				derMsg.append("Mac参数格式不正确!");
			}
		}
		if(derMsg.length()>0){
			mapResult.put("SUCCESS", "false");
			mapResult.put("MSG", derMsg.toString());
			if(logflag){
				logaccess.setACCESSNAME(userName);
				logaccess.setPASSWORD(passWord);
				logaccess.setACCESSPARAM("DYLX:"+dylx+",relyOnValue:"+relyOnValue);
				logaccess.setACCESSTIME(new Date());
				logaccess.setLGMAC(mac);
				logaccess.setLGMACHINE(hostname);
				logaccess.setUSELESSCAUSE(derMsg.toString());
				logaccess.setSFCG(ConstValue.SF.NO.Value);
				logaccess.setYXBZ(ConstValue.SF.YES.Value);
				dao.save(logaccess);
				dao.flush();
			}
			return mapResult;
		}
		//检查用户是否被锁,先从缓存读取数据,没有再根据数据库判断
		boolean lockflag = true;
		String strdate = StringHelper.FormatDateOnType(new Date(), "yyyyMMdd");
		String tempUserName = asDJXT+strdate+userName;
		try{
			Iterator<String> ita = cacheNoLockedUsers.iterator();
			while (ita.hasNext()) {
				String value = ita.next();
				if(value!=null && value.indexOf(strdate)==-1){//根据日期判断实现每天清空
					cacheNoLockedUsers.clear();
				}
				break;
			}	
			Iterator<String> itb = cacheNoLockedUsers.iterator();
			while (itb.hasNext()) {
				String value = itb.next();
				if(value!=null && value.equals(tempUserName)){
					lockflag = false; 
					break;
				}	
			}
		}catch(Exception e){		
		}		
		if(lockflag){
			lockflag = exteriorApiService.isLockedUser(userName, locknumber);
		}	
		if(lockflag){
			mapResult.put("SUCCESS", "false");
			mapResult.put("MSG", userName+"验证错误次数超过"+locknumber+"次,已被锁定"+lockdays+"天!");
			if(logflag){
				logaccess.setACCESSNAME(userName);
				logaccess.setPASSWORD(passWord);
				logaccess.setACCESSPARAM("DYLX:"+dylx+",relyOnValue:"+relyOnValue);
				logaccess.setACCESSTIME(new Date());
				logaccess.setLGMAC(mac);
				logaccess.setLGMACHINE(hostname);
				logaccess.setUSELESSCAUSE("请求用户验证错误次数超过"+locknumber+"次,已被锁定"+lockdays+"天!");
				logaccess.setSFCG(ConstValue.SF.NO.Value);
				logaccess.setYXBZ(ConstValue.SF.YES.Value);
				dao.save(logaccess);
			}
			dao.flush();
			return mapResult;
		}else{
			try{
				cacheNoLockedUsers.add(tempUserName);
			}catch(Exception e){
			}		
		}
		//验证用户是否有效,先从缓存读取数据,没有再根据数据库判断
		boolean isExistsUser = false;
		try{
			if(passWord.equals(cacheUser.get(asDJXT+userName))){
				isExistsUser = true;
			}
		}catch(Exception e){
		}	
		if(!isExistsUser){
			if(exteriorApiService.isExistsUserDJXT(userName, passWord)){
				isExistsUser = true;
				try{
					cacheUser.put(asDJXT+userName, passWord);
				}catch(Exception e){
				}	
			}
		}
		if(!isExistsUser){//更新验证用户错误次数
			Map<String, String> mapUsername = new HashMap<String, String>();		
			String strlog = "ACCESSNAME=:userName AND USELESSCOUNT<>0 AND NVL(USELESSCOUNT,99)!=99 AND YXBZ='1'"
					+ " AND SFCG='0' AND TO_CHAR(ACCESSTIME,'yyyy-MM-dd')='"+DateUtil.getDate()+"'";
			mapUsername.put("userName", userName);
			List<LOG_ACCESS> logs = dao.getDataList(LOG_ACCESS.class, strlog, mapUsername);
			if(logs!=null && logs.size()>0){
				int errorcount = 0;
				for(LOG_ACCESS log : logs){
					errorcount = locknumber-(log.getUSELESSCOUNT()+1);
					if(log.getUSELESSCOUNT()+1>=locknumber){
						log.setALLOWACCESSTIME(DateUtil.addDay(new Date(),lockdays));
						try{
							Iterator<String> itc = cacheNoLockedUsers.iterator();
							while (itc.hasNext()) { 
								String value = itc.next();
								if(value!=null && value.equals(tempUserName)){
									itc.remove();
								}	
							}
						}catch(Exception e){	
						}		
					}
					log.setUSELESSCOUNT(log.getUSELESSCOUNT()+1);
					dao.update(log);
				}
				if(errorcount<=0){
					mapResult.put("MSG", userName+"用户已被锁定!");
				}else{
					mapResult.put("MSG", "用户名或密码错误,请重新输入!错误"+locknumber+"次将被锁定,还剩"+errorcount+"次!");
				}
			}else{
				LOG_ACCESS userlog = new LOG_ACCESS();
				userlog.setACCESSNAME(userName);
				userlog.setPASSWORD(passWord);
				userlog.setACCESSPARAM("DYLX:"+dylx+",relyOnValue:"+relyOnValue);
				userlog.setACCESSTIME(new Date());
				userlog.setLGMAC(mac);
				userlog.setLGMACHINE(hostname);
				userlog.setUSELESSCOUNT(1);
				userlog.setUSELESSCAUSE("用户名或密码错误");
				userlog.setSFCG(ConstValue.SF.NO.Value);
				userlog.setYXBZ(ConstValue.SF.YES.Value);
				dao.save(userlog);
				mapResult.put("MSG", "用户名或密码错误,请重新输入!错误"+locknumber+"次将被锁定,还剩"+(locknumber-1)+"次!");
			}
			mapResult.put("SUCCESS", "false");
			dao.flush();
			return mapResult;
		}
		//生成不动产单元号
		String dyname = "";
		if("03".equals(dylx)){
			dyname = "自然幢";
			relyOnValue = relyOnValue.substring(0, 19);
		}else if("04".equals(dylx)){
			dyname = "户";
			relyOnValue = relyOnValue.substring(0, 24);
		}		
		String bdcdyh = ProjectHelper.CreatBDCDYH(relyOnValue, dylx);
		if(!StringHelper.isEmpty(bdcdyh)){
			mapResult.put("BDCDYH", bdcdyh);
			mapResult.put("SUCCESS", "true");
			mapResult.put("MSG", "获取"+dyname+"不动产单元号成功!");
			if(logflag){
				logaccess.setACCESSNAME(userName);
				logaccess.setPASSWORD(passWord);
				logaccess.setACCESSPARAM("DYLX:"+dylx+",relyOnValue:"+relyOnValue);
				logaccess.setACCESSTIME(new Date());
				logaccess.setLGMAC(mac);
				logaccess.setLGMACHINE(hostname);
				logaccess.setRETURNRESULT("BDCDYH："+bdcdyh);
				logaccess.setUSELESSCAUSE("获取"+dyname+"不动产单元号成功!");
				logaccess.setSFCG(ConstValue.SF.YES.Value);
				logaccess.setYXBZ(ConstValue.SF.YES.Value);
				dao.save(logaccess);
			}
		}else{
			mapResult.put("SUCCESS", "false");
			mapResult.put("MSG", "relyOnValue参数有误,查不到数据!");
			if(logflag){
				logaccess.setACCESSNAME(userName);
				logaccess.setPASSWORD(passWord);
				logaccess.setACCESSPARAM("DYLX:"+dylx+",relyOnValue:"+relyOnValue);
				logaccess.setACCESSTIME(new Date());
				logaccess.setLGMAC(mac);
				logaccess.setLGMACHINE(hostname);
				logaccess.setUSELESSCAUSE("relyOnValue参数有误,查不到数据!");
				logaccess.setSFCG(ConstValue.SF.NO.Value);
				logaccess.setYXBZ(ConstValue.SF.YES.Value);
				dao.save(logaccess);
			}
		}		
		
		dao.flush();
		
		return mapResult;
	}
}
