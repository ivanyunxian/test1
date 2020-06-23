package com.supermap.wisdombusiness.web.ui.tree;

import java.util.ArrayList;
import java.util.List;

import com.supermap.realestate.registration.ViewClass.ZS;

/**
 * ComboTree Data Structure
 * 
 * @author chenhl
 * */
public class Tree extends BaseTree {

	private static final long serialVersionUID = -5606895454250797400L;
	/**
	 * ID
	 * */
	private String id;
	/**
	 * Text
	 * */
	private String text;

	private String parentid;

	private String typeStr;
	private boolean isParent;
	private Integer flag;
	private Double qsc;
	private String dyh;
	private String fh;
	//发证次数
	private Long fzcs;
	//zsbh
	private String zsbh;
	//bdcqzshxh
	private String qzhxh;
	
	public Double getQsc() {
		return qsc;
	}

	public void setQsc(Double qsc) {
		this.qsc = qsc;
	}

	public String getDyh() {
		return dyh;
	}

	public void setDyh(String dyh) {
		this.dyh = dyh;
	}

	public String getFh() {
		return fh;
	}

	public void setFh(String fh) {
		this.fh = fh;
	}

	public boolean getisParent() {
		return isParent;
	}

	public void setisParent(boolean isParent) {
		this.isParent = isParent;
	}

	/** 
	 * @return tag1 
	 */
	public String getTag1() {
		return tag1;
	}

	/** 
	 * @param tag1 要设置的 tag1 
	 */
	public void setTag1(String tag1) {
		this.tag1 = tag1;
	}

	/** 
	 * @return tag2 
	 */
	public String getTag2() {
		return tag2;
	}

	/** 
	 * @param tag2 要设置的 tag2 
	 */
	public void setTag2(String tag2) {
		this.tag2 = tag2;
	}

	private String tag1;
	
	private String tag2;
	
	private String tag3;
	private String tag4;
	public String getTag4() {
		return tag4;
	}

	public void setTag4(String tag4) {
		this.tag4 = tag4;
	}

	private String bdcqzh;
	public String getBdcqzh() {
		return bdcqzh;
	}

	public void setBdcqzh(String bdcqzh) {
		this.bdcqzh = bdcqzh;
	}

	public ZS getZsform() {
		return zsform;
	}

	public void setZsform(ZS zsform) {
		this.zsform = zsform;
	}

	private ZS zsform;

	/** 
	 * @return tag3 
	 */
	public String getTag3() {
		return tag3;
	}

	/** 
	 * @param tag3 要设置的 tag3 
	 */
	public void setTag3(String tag3) {
		this.tag3 = tag3;
	}

	@SuppressWarnings("rawtypes")
	public List children;

	private boolean checked;
	private String type;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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

	public boolean getChecked() {
		return checked;
	}

	@Override
	public void setChecked(boolean checked) {
		this.checked = checked;
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

	public String getParentid() {
		if (parentid == null)
			parentid = "";
		return parentid;
	}

	public void setParentid(String parentid) {
		this.parentid = parentid;
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

	@SuppressWarnings("unchecked")
	public void addChild(Tree obj) {
		this.children.add(obj);
	}

	public Integer getFlag() {
		return flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

	
	public Long getFzcs() {
		return fzcs;
	}
	
	public void setFzcs(Long fzcs) {
		this.fzcs = fzcs;
	}

	public String getZsbh() {
		return zsbh;
	}

	public void setZsbh(String zsbh) {
		this.zsbh = zsbh;
	}

	public String getQzhxh() {
		return qzhxh;
	}

	public void setQzhxh(String qzhxh) {
		this.qzhxh = qzhxh;
	}
	
	private String zrzh;
	

	public String getZrzh() {
		return zrzh;
	}

	public void setZrzh(String zrzh) {
		this.zrzh = zrzh;
	}

}