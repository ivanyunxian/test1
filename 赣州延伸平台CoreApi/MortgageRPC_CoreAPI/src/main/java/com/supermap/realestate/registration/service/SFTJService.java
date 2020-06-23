package com.supermap.realestate.registration.service;

import java.util.List;
import java.util.Map;

import com.supermap.wisdombusiness.web.Message;

public interface SFTJService {

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
	
}
