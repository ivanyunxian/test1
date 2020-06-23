package com.supermap.realestate.registration.model.xmlmodel;

import com.thoughtworks.xstream.annotations.XStreamAlias;
@XStreamAlias("Message")
public class MessageW {
    @XStreamAlias("Head")
    private Head head;
    @XStreamAlias("Data")
    private DataW data;
    
    public Head getHead() {
        return head;
    }
    public void setHead(Head head) {
        this.head = head;
    }
    public DataW getData() {
        return data;
    }
    public void setData(DataW data) {
        this.data = data;
    }
    
}
