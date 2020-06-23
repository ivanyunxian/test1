package com.supermap.realestate.registration.model.Sms;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

@XStreamAlias("svc_init")
public class SmsMessage {
	@XStreamAsAttribute
    private String ver;//svc_init版本号
    public String getVer() {  
        return ver;  
    }  
    public void setVer(String ver) {  
        this.ver = ver;  
    }  
    private Sms sms = new Sms();
	
	public Sms getSms() {  
        return sms;  
    }  
    public void setSms(Sms sms) {  
        this.sms = sms;  
    }
    
}
