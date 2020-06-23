package com.supermap.realestate.registration.model.xmlmodel;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("Message")
public class Message {

    @XStreamAlias("Head")
    private Head head;
    @XStreamAlias("Data")
    private Data data;

    public Head getHead() {
	return head;
    }

    public void setHead(Head head) {
	this.head = head;
    }

    public Data getData() {
	return data;
    }

    public void setData(Data data) {
	this.data = data;
    }
}
