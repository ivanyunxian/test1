package com.supermap.intelligent.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.supermap.intelligent.model.JsonMessage;
import com.supermap.intelligent.util.ResultData;


public interface IntelligentCoreMQService {

	JsonMessage sHJDReceivedMessage(String string);

	public ResultData dJBReceivedMessage(String mqMsg);

	public ResultData pushZSToMRPC(String ywly, String wlsh) throws JsonProcessingException;

	JsonMessage PassBackReceivedMessage(String string);

	JsonMessage getCertReceivedMessage(String msg);

	JsonMessage PassBackEnterpriseMessage(String msg);

	JsonMessage PassOverEnterpriseMessage(String msg);
	
	

}
