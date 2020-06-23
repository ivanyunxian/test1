package com.supermap.realestate.registration.util;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.supermap.realestate.registration.model.BDCS_XMXX;
import com.supermap.realestate.registration.util.ConstValue.SF;
import com.supermap.wisdombusiness.web.ResultMessage;

/**
 * 
 * @Description:登记系统自定义拦截器
 * @author diaoliwei
 * @date 2015年6月14日 下午10:12:22
 * @Copyright SuperMap
 */
public class RegisterInterceptor implements HandlerInterceptor {

	/**
	 * 进入Controller前验证
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		HandlerMethod handlerMethod = (HandlerMethod) handler;
		Method method = handlerMethod.getMethod();
		RequestMapping mappingannotation = method.getAnnotation(RequestMapping.class);
		return valideSFDB( request,  response,  handler,  method,  mappingannotation);
	}

	private boolean valideSFDB(HttpServletRequest request, HttpServletResponse response, Object handler, Method method, RequestMapping mappingannotation) {
		AccessRequired.UnBoardRequired annotation = method.getAnnotation(AccessRequired.UnBoardRequired.class);
		if (annotation != null) {
			String matchValue = "xmbh";
			if (annotation.value() != null && annotation.value().length > 0) {
				matchValue = annotation.value()[0];
			}
			if (mappingannotation != null) {
				String[] mappingvalues = mappingannotation.value();
				if (mappingvalues != null && mappingvalues.length > 0) {
					String value = mappingvalues[0];
					String valuenew = value.replaceAll("\\{" + matchValue + "}", "([^/]*)");
					valuenew = valuenew.replaceAll("\\{[^/]*\\}", "[^/]*");
					if (value.contains(matchValue)) {
						String path = request.getRequestURI();
						valuenew = ".*" + valuenew;
						Pattern pattern = Pattern.compile(valuenew);
						Matcher m = pattern.matcher(path);
						while (m.find())// 
						{
							String xmbh = m.group(1);
							if (!StringHelper.isEmpty(xmbh)) {
								BDCS_XMXX xmxx = Global.getXMXXbyXMBH(xmbh);
								if (xmxx != null) {
									if(SF.YES.Value.equals(xmxx.getSFDB()))
									{
										ResultMessage msg=new ResultMessage();
										try {
											response.getWriter().write("{\"success\":\"false\",\"msg\":\"项目已经登簿，不能再进行修改！\"}");
										} catch (IOException e) {
											e.printStackTrace();
										}
										return false;
									}
								}
							}
						}
					}
				}
			}
		}
		return true;
	}

	/**
	 * Controller的方法调用之后执行
	 */
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
		// System.out.println("Controller的方法调用之后执行,视图的渲染之前执行,preHandle返回true时执行");
	}

	/**
	 * preHandle返回true时执行
	 */
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		// System.out.println("渲染了视图执行,主要作用是用于清理资源的,preHandle返回true时执行");
	}
}
