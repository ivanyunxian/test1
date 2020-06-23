package com.supermap.realestate.registration.service.Sender;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import com.alibaba.fastjson.JSON;

public class MQSender {

    private JmsTemplate jmsTemplate;

    /**
     * 向指定队列发送消息
     */
    public void sendMessage(Destination destination, final String msg) {
	jmsTemplate.send(destination, new MessageCreator() {
	    public Message createMessage(Session session) throws JMSException {
		return session.createTextMessage(msg);
	    }
	});
    }

    /**
     * 向默认队列发送消息
     */
    public void sendMessage(final String msg) {
	jmsTemplate.send(new MessageCreator() {
	    public Message createMessage(Session session) throws JMSException {
		return session.createTextMessage(msg);
	    }
	});

    }

    /**
     * 向默认队列发送消息
     */
    public void sendMessage(final ShareMessage shareMessage) {
	jmsTemplate.send(
		new MessageCreator() {
	    public Message createMessage(Session session) throws JMSException {
		return session.createTextMessage(JSON
			.toJSONString(shareMessage));
	    }
	});

    }

    

    public void setJmsTemplate(JmsTemplate jmsTemplate) {
	this.jmsTemplate = jmsTemplate;
    }

}
