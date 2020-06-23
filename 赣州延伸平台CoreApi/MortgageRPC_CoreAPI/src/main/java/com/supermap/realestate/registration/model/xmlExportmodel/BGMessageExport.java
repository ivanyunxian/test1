package com.supermap.realestate.registration.model.xmlExportmodel;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("Message")
/**
 * 变更数据模型
 * @author 胡加红
 *
 */
public class BGMessageExport {
	@XStreamAlias("Head")
	private HeadExport head;
	/**
	 * 变更前数据
	 */
	@XStreamAlias("BGQData")
	private BGDataExport bgqdata;
	/**
	 * 变更后数据
	 */
	@XStreamAlias("BGHData")
	private BGDataExport bghdata;
	public HeadExport getHead() {
		return head;
	}

	public void setHead(HeadExport head) {
		this.head = head;
	}

	public BGDataExport getBGQData() {
		return bgqdata;
	}

	public void setBGQData(BGDataExport bgqdata) {
		this.bgqdata = bgqdata;
	}
	
	public BGDataExport getBGHData() {
		return bghdata;
	}

	public void setBGHData(BGDataExport bghdata) {
		this.bghdata = bghdata;
	}
}
