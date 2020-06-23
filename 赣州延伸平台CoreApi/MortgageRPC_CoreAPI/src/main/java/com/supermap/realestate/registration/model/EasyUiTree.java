package com.supermap.realestate.registration.model;

import java.io.Serializable;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@SuppressWarnings("serial")
@XStreamAlias("easyuitree")
public class EasyUiTree implements Serializable {
	private static final long serialVersionUID = 1L;
	private String id ;
	private String pid ; 
	
	private String text ;
    /// <summary>
    /// 'open' 或 'closed'，默认是 'open'。
    /// 如果为'closed'的时候，将不自动展开该节点。
    /// </summary>
	private String state ;
	private boolean ischecked ;// replace checked
	private String attributes ;
	private List<EasyUiTree> children ; 
	
    public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public boolean isIschecked() {
		return ischecked;
	}
	public void setIschecked(boolean ischecked) {
		this.ischecked = ischecked;
	}
	public String getAttributes() {
		return attributes;
	}
	public void setAttributes(String attributes) {
		this.attributes = attributes;
	}
	public List<EasyUiTree> getChildren() {
		return children;
	}
	public void setChildren(List<EasyUiTree> children) {
		this.children = children;
	}
	
}
