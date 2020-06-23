package com.supermap.wisdombusiness.workflow.service.common;

import java.util.List;
public class TreeInfo {
	private String id;  
    private String text; 
    private Boolean checked = false;
    private String type;
    private String state;
    private String preid;
    private Integer childCount;
    private String desc;
    public String getPreid() {
		return preid;
	}

	public void setPreid(String preid) {
		this.preid = preid;
	}

	public Integer getChildCount() {
		return childCount;
	}

	public void setChildCount(Integer childCount) {
		this.childCount = childCount;
	}

	private boolean isParent=true;
    
    public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
    public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
    public Boolean getChecked() {
		return checked;
	}

	public void setChecked(Boolean checked) {
		this.checked = checked;
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	/**
	 * @return the isParent
	 */
	public boolean getisParent() {
		return isParent;
	}

	/**
	 * @param isParent the isParent to set
	 */
	public void setisParent(boolean isParent) {
		this.isParent = isParent;
	}

	/**
	 * @return the desc
	 */
	public String getDesc() {
		return desc;
	}

	/**
	 * @param desc the desc to set
	 */
	public void setDesc(String desc) {
		this.desc = desc;
	}

	public List<TreeInfo> children; //子节点
	
	public TreeInfo parent;
	
}
