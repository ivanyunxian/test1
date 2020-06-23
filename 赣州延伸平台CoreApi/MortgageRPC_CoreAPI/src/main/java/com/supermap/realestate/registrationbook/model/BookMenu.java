package com.supermap.realestate.registrationbook.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.supermap.wisdombusiness.web.ui.tree.State;
import com.supermap.wisdombusiness.web.ui.tree.Tree;

/**
 * @author WUZHU
 *登记薄索引菜单对象，树形结构
 */
public class BookMenu {
	
	private static final long serialVersionUID = 3471803583069167148L;
	
	private String iconCls;
	
	private State state;
	
	private boolean checked;
	
	/**  ID */
	private String id;
	
	/**  Text */
	private String text;

	private String parentid;

	private String typeStr;

	@SuppressWarnings("rawtypes")
	public List children;

	private String type;
    
	private Map<String,String> attributes;
	
	public String getIconCls() {
		return iconCls;
	}

	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
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

	public String getParentid() {
		return parentid;
	}

	public void setParentid(String parentid) {
		this.parentid = parentid;
	}

	public String getTypeStr() {
		return typeStr;
	}

	public void setTypeStr(String typeStr) {
		this.typeStr = typeStr;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	@SuppressWarnings("rawtypes")
	public List getChildren() {
		if (children == null)
			children = new ArrayList<Tree>();
		return children;
	}

	@SuppressWarnings("rawtypes")
	public void setChildren(List children) {
		this.children = children;
	}

	public Map<String,String> getAttributes() {
		if (attributes == null)
		attributes = new HashMap<String, String>();
		return attributes;
	}

	public void setAttributes(Map<String,String> attributes) {
		this.attributes = attributes;
	}
	
//	public Map<String,String> getAttributes() {
//		if (attributes == null)
//			attributes = new ArrayList<Map<String,String>>();
//		return attributes;
//	}
//
//	public void setAttributes(List attributes) {
//		this.attributes = attributes;
//	}

}
