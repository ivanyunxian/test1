package com.supermap.wisdombusiness.workflow.service.message;

import org.springframework.stereotype.Service;

import com.supemap.mns.client.CloudQueue;
import com.supemap.mns.client.MNSClient;
import com.supemap.mns.model.Message;
import com.supemap.mns.model.QueueMeta;
import com.supermap.realestate.registration.util.GetProperties;

@Service("messageService")
public class MessageService {
	private boolean IsOpenMessage = false;
	private MNSClient client = null;
	private static String QUEUE_NAME = "workflow";

	public void MessageService() {
		String open = GetProperties.getConstValueByKey("messagecenter");
		if (open != null && !open.equals("")) {
			IsOpenMessage = Boolean.getBoolean(open);
		}
	}

	public void putMessage(String msg) {
		QueueMeta meta1 = new QueueMeta();
		meta1.setQueueName(QUEUE_NAME);
		CloudQueue queue1 = this.client.createQueue(meta1);
		Message message = new Message();
		message.setMessageBody(msg);
		Message message2 = queue1.putMessage(message);

	}

}
