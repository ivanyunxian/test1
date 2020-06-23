package com.supermap.realestate.registration.service;

import java.util.List;
import java.util.Map;

import com.supermap.realestate.registration.model.xmlmodel.Message;

public interface XMLService {
    /**
     * 调用协同共享的接口获取xml的内容
     * @作者 Ouzhanrong
     * @创建时间 2015年8月8日下午3:00:46
     * @param ywh 业务号
     */
    public List<Message> readXMLFromXTGX(String ywh) throws Exception;
    
    /**
     * 把数据保存到数据库中
     * @作者 Ouzhanrong
     * @创建时间 2015年8月8日下午4:03:01
     * @param message
     * @param xmbh
     * @return 
     */
    public boolean saveMessage2DB(Message message, String xmbh);
    
    public boolean getXMLFromXTGX(String ywh, String xmbh)throws Exception;
    
    public Map<String,String> getLXAndID(String qlrmc,String qzh);

    public Map<String, Object> getMCAndZH(String htbh);

    public boolean CFDYFromShareDB(String bdcdyid1, String bdcdylx, String ywh,
    			String xmbh) throws Exception;

    public String pdAutoCreate(String xmbh);

}
