package com.supermap.realestate.registration.model.Sms;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

@XStreamAlias("client")
public class Client {
    private String id;//MAS分配编号
    public String getId() {  
        return id;  
    }  
    public void setId(String id) {  
        this.id = id;  
    }  
    
    private String pwd;//MAS分配密钥
    public String getPwd() {  
        return pwd;  
    }  
    public void setPwd(String pwd) {  
        this.pwd = pwd;  
    }  
    
    private String serviceid;//此处对应节点内属性名称  
    public String getServiceid() {  
        return serviceid;  
    }  
    public void setServiceid(String serviceid) {  
        this.serviceid = serviceid;  
    }  
}
