package com.supermap.realestate_gx.registration.service;

import java.util.Map;

import com.supermap.wisdombusiness.web.Message;

public interface Xf2QfService {
	
	/**
	 * //liangc 现房转期房
	 * @param bdcdyid
	 * @param bdcdyh
	 * @return
	 */
	public Message putxf2qf(String bdcdyid,String bdcdyh);
	
	/**
	 * //获取现房信息 liangc
	 * @param queryvalues
	 * @param page
	 * @param rows
	 * @return
	 */
	public Message getxf(Map<String, String> queryvalues, Integer page,Integer rows);
	
	
	
	/**
	 * liangc 批量现房转期房
	 * @param bdcdyids
	 * @return
	 */
	public Message plputxf2qf(String bdcdyids);
	
}
