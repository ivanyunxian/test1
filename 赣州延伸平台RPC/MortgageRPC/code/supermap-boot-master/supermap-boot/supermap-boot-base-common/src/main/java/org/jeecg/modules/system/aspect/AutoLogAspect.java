package org.jeecg.modules.system.aspect;

import java.lang.reflect.Method;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.system.util.StringHelper;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.*;
import org.jeecg.common.util.ConstValueMrpc.MrpccodingEnum;
import org.jeecg.common.util.ConstValueMrpc.RequestcodeEnum;
import org.jeecg.modules.system.entity.SysLog;
import org.jeecg.modules.system.service.ISysLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;


/**
 * 系统日志，切面处理类
 * 
 * @Author scott
 * @email jeecgos@163.com
 * @Date 2018年1月14日
 */
@Slf4j
@Aspect
@Component
public class AutoLogAspect {
	@Autowired
	private ISysLogService sysLogService;
	
	@Pointcut("@annotation(org.jeecg.common.aspect.annotation.AutoLog)")
	public void logPointCut() { 
		
	}

	@Around("logPointCut()")
	public Object around(ProceedingJoinPoint point) throws Throwable {

		//保存日志
		Object result = saveSysLog(point);

		return result;
	}

	private Object saveSysLog(ProceedingJoinPoint joinPoint) throws Throwable {
		Object resultObj = null;
		SysLog sysLog = new SysLog();
		long beginTime = System.currentTimeMillis();
		try {

			MethodSignature signature = (MethodSignature) joinPoint.getSignature();
			Method method = signature.getMethod();

			AutoLog syslog = method.getAnnotation(AutoLog.class);
			if(syslog != null){
				//注解上的描述,操作日志内容
				sysLog.setLogContent(syslog.value());
				sysLog.setLogType(syslog.logType());
			}

			//请求的方法名
			String className = joinPoint.getTarget().getClass().getName();
			String methodName = signature.getName();
			sysLog.setMethod(className + "." + methodName + "()");

			//获取request
			HttpServletRequest request = SpringContextUtils.getHttpServletRequest();
			//设置IP地址
			sysLog.setIp(IPUtils.getIpAddr(request));

			//获取登录用户信息
			LoginUser sysUser = (LoginUser)SecurityUtils.getSubject().getPrincipal();
			if(sysUser!=null){
				sysLog.setUserid(sysUser.getUsername());
				sysLog.setUsername(sysUser.getRealname());

			}

			String resultdata = "";
			//接口调用时，对请求参数及返回结果报文进行保存，并验证token及用户数据如果验证不通过，直接返回结果了，不再进行接口调用
			if(CommonConstant.INTERFACE_TYPE_3 == sysLog.getLogType()) {
				//获取请求参数
				Object[] params = joinPoint.getArgs();
				for (Object param : params) {
					if (param instanceof String) {
						String requestRsaStr = (String) param;
						String decryDate = RSACoder.decryptRequest(requestRsaStr);
						if(MrpccodingEnum.FAIL.Value.equals(decryDate)){
							sysLog.setRequestParam("请求报文解析失败");
							//传入的参数都解密不成功，不用理了，接口逻辑处理会返回错误信息
							continue;
						}
						sysLog.setRequestParam(decryDate);
						JSONObject requestDate=JSONObject.parseObject(decryDate);
						String requestcode = requestDate.getString("requestcode");
						if(StringHelper.isEmpty(requestcode)){
							continue;
						}

						if(RequestcodeEnum.TOKEN.Value.equals(requestcode)) {
							JSONObject data = requestDate.getJSONObject("data");
							sysLog.setUsername(data.getString("username"));
						} else {
							//除了获取token接口，其他接口均验证token，并从token中获取用户信息保存日志
							String token = request.getParameter("token");
							if(StringHelper.isEmpty(token)) {
								resultdata = RSACoder.resultsJsonNoRsa( MrpccodingEnum.REQUIRED.Value, "", null, "");
								sysLog.setResponsedata(resultdata);
								sysLog.setResultstate(MrpccodingEnum.REQUIRED.Name);
								//执行时长(毫秒)
								long time = System.currentTimeMillis() - beginTime;
								//耗时
								sysLog.setCostTime(time);
								sysLog.setCreateTime(new Date());
								//保存系统日志
								sysLogService.save(sysLog);
								return resultdata;
							}

							//验证token不通过的情况
							Result<Claims> claimsResult = JwtUtils.verifyToken(token);
							if (!MrpccodingEnum.SUCCESS.Value.equals(claimsResult.getMessage())) {
								resultdata = RSACoder.resultsJsonNoRsa(claimsResult.getMessage(), RequestcodeEnum.TOKEN.Value, null, "");
								sysLog.setResponsedata(resultdata);
								sysLog.setResultstate(ConstValueMrpc.MrpccodingEnum.initFrom(claimsResult.getMessage()).Name);
								//执行时长(毫秒)
								long time = System.currentTimeMillis() - beginTime;
								//耗时
								sysLog.setCostTime(time);
								sysLog.setCreateTime(new Date());
								//保存系统日志
								sysLogService.save(sysLog);
								return resultdata;
							}
							//验证通过后，记录一下用户信息啥的
							Claims claims = claimsResult.getResult();
							String usermsg = claims.getId();
							if(!StringHelper.isEmpty(usermsg)) {
								String[] split = usermsg.split(",");
								sysLog.setUsername(split[0]);
								sysLog.setUserid(split.length>1?split[1]:"");
							}
						}

						sysLog.setRequestType(requestcode);
					}
				}

				try {
					//执行方法
					resultObj = joinPoint.proceed();

					if(resultObj != null ) {
						String result = RSACoder.decryptByPublicKeyBase64(StringHelper.formatObject(resultObj));
						sysLog.setResponsedata(result);
						JSONObject jsonObject = JSONObject.parseObject(result);
						sysLog.setResultstate(ConstValueMrpc.MrpccodingEnum.initFrom(jsonObject.getString("code")).Name);
					}
				}catch (Exception e) {
					//执行完成，不抛出异常，免得异常捕获那里又执行一遍
				}

			} else {

				try{
					resultObj = joinPoint.proceed();
					//普通操作请求的参数
					Object[] args = joinPoint.getArgs();
					String params = JSONObject.toJSONString(args);
					sysLog.setRequestParam(params);
				}catch (Exception e){
					//执行完成，不抛出异常，免得异常捕获那里又执行一遍
				}

			}
		} catch (Exception e) {
			//防止代码报错导致无法执行往后的方法
			resultObj = joinPoint.proceed();
		} catch (Throwable e) {
			//执行 joinPoint.proceed() 才会强制抛出的异常
		}
		//执行时长(毫秒)
		long time = System.currentTimeMillis() - beginTime;
		//耗时
		sysLog.setCostTime(time);
		sysLog.setCreateTime(new Date());
		//保存系统日志
		try {
			sysLogService.save(sysLog);
		} catch (Exception e) {
			log.info(e.getMessage(), e);
		}
		return resultObj;
	}
}
