package com.supermap.realestate.registration.model.Sms;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

@XStreamAlias("sms")
public class Sms {
	@XStreamAsAttribute
    private String ver;//sms版本号
    public String getVer() {  
        return ver;  
    }  
    public void setVer(String ver) {  
        this.ver = ver;  
    }  
    private Client client = new Client();
	
	public Client getClient() {  
        return client;  
    }  
    public void setClient(Client client) {  
        this.client = client;  
    }
    @XStreamAlias("sms_info")
    private Smsinfo smsinfo = new Smsinfo();
	
	public Smsinfo getSms_info() {  
        return smsinfo;  
    }  
    public void setSms_info(Smsinfo smsinfo) {  
        this.smsinfo = smsinfo;  
    }  
}
