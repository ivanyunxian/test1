package com.supermap.realestate.registration.service;

import com.supermap.wisdombusiness.web.Message;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public interface SFMXTJService {

	/**
	 * @Description: 收费统计数据获取
	 * @Title: GetSFTJ
	 * @Author: zhaomengfan
	 * @Date: 2017年6月27日下午12:52:11
	 * @param param
	 * @return Message
	 */
	Message GetSFTJ(Map<String, String> param);

	/**
	 * @Description: 获取查询界面的下拉框内容
	 * @Title: GetCombobox
	 * @Author: zhaomengfan
	 * @Date: 2017年6月27日下午5:32:22
	 * @return
	 * @return List<Message>
	 */
	List<Message> GetCombobox();
	public Message getZXJFTJ(HttpServletRequest request);
	public Message ModifyPJZT(HttpServletRequest request);
	public Message RepealPJZT(HttpServletRequest request);
}
