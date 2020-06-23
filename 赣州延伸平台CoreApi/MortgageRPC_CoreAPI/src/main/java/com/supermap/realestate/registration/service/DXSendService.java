package com.supermap.realestate.registration.service;

import com.supermap.wisdombusiness.web.Message;

import java.net.ConnectException;
import java.net.MalformedURLException;
import java.util.Map;

public interface DXSendService {
	
	/**
	 * 获取短信发送项目
	 * @author liangc
	 * @date 2018-09-05 16:45:30
	 * @param queryvalues
	 * @param page
	 * @param rows
	 * @return
	 */
	public Message getdxsendquery(Map<String, String> queryvalues, Integer page,Integer rows);
	/**
	 * 获取短信推送详情
	 */
	public Message getDxtsInfo(Map<String, String> queryvalues, Integer page,Integer rows);

	/**
	 * 单条发送短信
	 * @date 2018-09-05 16:46:30
	 * @author liangc
	 * @param xmbh
	 * @return
	 * @throws MalformedURLException 
	 * @throws ConnectException 
	 */
	public Message dxsend(String xmbh,String sqrid,String cxfs) throws ConnectException, MalformedURLException;

	public Message pldxsend(String sqrids, String cxfs) throws ConnectException, MalformedURLException;
	
	/**
	 * 发送短信给网签和税务
	 * @date 2019-03-12 16:46:30
	 * @author joe
	 * @param file_number
	 * @return
	 * @throws MalformedURLException 
	 * @throws ConnectException 
	 */
	public void dxsendForHouseAndTax(String file_number,String ywh);
}
