package com.supermap.realestate.registration.model.Sms;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

public class Smsinfo {
    private String phone;//手机号码1，多个号码之间用逗号间隔 
    public String getPhone() {  
        return phone;  
    }  
    public void setPhone(String phone) {  
        this.phone = phone;  
    }  
    
    private String content;//短信内容  
    public String getContent() {  
        return content;  
    }  
    public void setContent(String content) {  
        this.content = content;  
    }  
    
    private String ssid;//此处对应节点内属性名称  
    public String getSsid() {  
        return ssid;  
    }  
    public void setSsid(String ssid) {  
        this.ssid = ssid;  
    }  
}
