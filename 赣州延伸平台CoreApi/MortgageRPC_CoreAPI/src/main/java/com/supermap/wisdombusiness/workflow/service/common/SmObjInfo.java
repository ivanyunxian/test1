package com.supermap.wisdombusiness.workflow.service.common;

import java.util.List;

public class SmObjInfo {
	
	private String Desc;
	private String ID;
	private String Name;
	@SuppressWarnings("unused")
	private String Text;
	private String Confirm;
	private String Routeid;
	private List<SmObjInfo> Children;
	
	private String file_number;
	
	
	public SmObjInfo(String desc, String iD, String name) {
		super();
		Desc = desc;
		ID = iD;
		Name = name;
	}
	public SmObjInfo()
	{}
	/**
	 * @return the desc
	 */
	public String getDesc() {
		return Desc;
	}
	/**
	 * @param desc the desc to set
	 */
	public void setDesc(String desc) {
		Desc = desc;
	}
	/**
	 * @return the iD
	 */
	public String getID() {
		return ID;
	}
	/**
	 * @param iD the iD to set
	 */
	public void setID(String iD) {
		ID = iD;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return Name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		Name = name;
	}
	/**
	 * @return the children
	 */
	public List<SmObjInfo> getChildren() {
		return Children;
	}
	/**
	 * @param children the children to set
	 */
	public void setChildren(List<SmObjInfo> children) {
		Children = children;
	}
	/**
	 * @return the text
	 */
	public String getText() {
		return Name;
	}
	/**
	 * @param text the text to set
	 */
	public void setText(String text) {
		Text = text;
	}
	public String getConfirm() {
		return Confirm;
	}
	public void setConfirm(String confirm) {
		Confirm = confirm;
	}
	public String getRouteid() {
		return Routeid;
	}
	public void setRouteid(String routeid) {
		Routeid = routeid;
	}
	public String getFile_number() {
		return file_number;
	}
	public void setFile_number(String file_number) {
		this.file_number = file_number;
	}
   

}
