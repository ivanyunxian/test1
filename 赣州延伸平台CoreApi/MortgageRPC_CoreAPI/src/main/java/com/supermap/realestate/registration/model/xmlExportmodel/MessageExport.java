package com.supermap.realestate.registration.model.xmlExportmodel;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("Message")
public class MessageExport {
	@XStreamAlias("Head")
	private HeadExport head;
	@XStreamAlias("Data")
	private DataExport data;

	public HeadExport getHead() {
		return head;
	}

	public void setHead(HeadExport head) {
		this.head = head;
	}

	public DataExport getData() {
		return data;
	}

	public void setData(DataExport data) {
		this.data = data;
	}
	
}
