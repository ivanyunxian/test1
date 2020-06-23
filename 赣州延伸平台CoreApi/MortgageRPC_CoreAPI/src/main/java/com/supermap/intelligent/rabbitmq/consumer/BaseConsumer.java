package com.supermap.intelligent.rabbitmq.consumer;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;
import com.supermap.intelligent.model.LOG_RABBITMQ;
import com.supermap.intelligent.service.IntelligentCoreMQService;

public class BaseConsumer {
	@Autowired
	private IntelligentCoreMQService intelligentCoreMQService;
	private static final ObjectMapper MAPPER =  new ObjectMapper();
	private ExecutorService threadPool = Executors.newFixedThreadPool(6);

	public Logger logger = Logger.getLogger(BaseConsumer.class);
	
	public void dJBReceivedMessage(String msg) {
		 try {
			 JsonNode jsonNode = MAPPER.readTree(msg);
			 String xmbh = jsonNode.get("xmbh").asText();
			 logger.info("开始消费登簿信息！------msg:"+msg);
			 intelligentCoreMQService.dJBReceivedMessage(msg);
		 } catch (Exception e) {
		 	e.printStackTrace();
		 }
	}

	/**
	 * Rabbit MQ消息队列接收接口：该接口接收电子证照消息队列记录
	 * 
	 * @param msg
	 */
	public void electronicCertificateReceivedMessage(final String msg) {
		threadPool.execute(new Runnable(){
			@Override
			public void run(){
				try {
					 logger.info("电子证照接收到消费消息："+msg);
					 JsonNode jsonNode = MAPPER.readTree(msg);
					 String ywlsh = jsonNode.get("ywlsh").asText();
					 intelligentCoreMQService.getCertReceivedMessage(msg);
					 logger.info("创建电子证照结果code："+msg);
					 //2、日志
				 } catch (Exception e) {
					  logger.error(e);
				 }
			}
		});
	}

	@RabbitHandler
	public void sHJDReceivedMessage(final String msg) {
		threadPool.execute(new Runnable() {
			@Override
			public void run() {
				try {
					logger.info("开始消费办件进度信息----" + msg);
					 intelligentCoreMQService.sHJDReceivedMessage(msg);
				} catch (Exception e) {
					e.printStackTrace();
				}

			}

		});
	}

	@RabbitHandler
	public void PassBackReceivedMessage(final String msg) {
		threadPool.execute(new Runnable() {
			@Override
			public void run() {
				try {
					logger.info("开始消费登记系统驳回信息！------msg:"+msg);
					intelligentCoreMQService.PassBackReceivedMessage(msg);
				} catch (Exception e) {
					e.printStackTrace();
				}

			}

		});
	}
	
	@RabbitHandler
	public void PassBackEnterpriseMessage(final String msg) {
		threadPool.execute(new Runnable() {
			@Override
			public void run() {
				try {
					logger.info("开始消费登记系统驳回信息！------msg:"+msg);
					intelligentCoreMQService.PassBackEnterpriseMessage(msg);
				} catch (Exception e) {
					e.printStackTrace();
				}

			}

		});
	}
	
	@RabbitHandler
	public void PassOverEnterpriseMessage(final String msg) {
		threadPool.execute(new Runnable() {
			@Override
			public void run() {
				try {
					logger.info("开始消费登记系统驳回信息！------msg:"+msg);
					intelligentCoreMQService.PassOverEnterpriseMessage(msg);
				} catch (Exception e) {
					e.printStackTrace();
				}

			}

		});
	}

}
