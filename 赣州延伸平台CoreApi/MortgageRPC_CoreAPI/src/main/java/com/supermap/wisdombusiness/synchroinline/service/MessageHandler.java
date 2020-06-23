package com.supermap.wisdombusiness.synchroinline.service;

/**
 * 短信消息处理器接口
 * @author pukx
 * by 2017年7月4日
 */
public interface MessageHandler
{
	/**
	 * 消息发送
	 * @param xmbh 项目编号
	 * @throws Exception 发送失败则抛出异常
	 */
	void send(String xmbh) throws Exception;
	
	/**
	 * 批量消息发送
	 * @param xmbhs 项目编号集合
	 * @return 返回发送成功的记录数
	 * @throws Exception 抛出未知异常
	 */
	int send(String[] xmbhs) throws Exception;
	
	/**
	 * 测试消息发送
	 * @param tel 电话
	 * @param msg 消息内容
	 * @throws Exception
	 */
	void test(String tel,String msg) throws Exception;
}
