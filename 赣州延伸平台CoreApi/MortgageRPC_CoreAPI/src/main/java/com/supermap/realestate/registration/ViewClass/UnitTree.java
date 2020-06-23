package com.supermap.realestate.registration.ViewClass;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @Description:单元列表树
 * @author 刘树峰
 * @date 2015年6月15日 下午3:01:12
 * @Copyright SuperMap
 */
public class UnitTree {

	/**
	 * 不动产单元ID
	 */
	private String id;
	
	/**
	 * 不动产单元ID
	 */
	private String bdcdyid;
	
	/**
	 * 登记单元ID
	 */
	private String djdyid;
	
	/**
	 * 坐落
	 */
	private String text;
	
	/**
	 * 不动产单元类型
	 */
	private String bdcdylx;
	
	/**
	 * 坐落
	 */
	private String zl;
	
	/**
	 * 来源
	 */
	private String ly;
	
	/**
	 * 宗地来源
	 */
	private String zdly;
	
	public String getZdly() {
		return zdly;
	}

	public void setZdly(String zdly) {
		this.zdly = zdly;
	}

	public String getZrzly() {
		return zrzly;
	}

	public void setZrzly(String zrzly) {
		this.zrzly = zrzly;
	}

	/**
	 * 自然幢来源
	 */
	private String zrzly;
	
	private double mj;
	
	private double fttdmj;
	
	public double getFttdmj() {
		return fttdmj;
	}

	public void setFttdmj(double fttdmj) {
		this.fttdmj = fttdmj;
	}

	/** 
	 * @return mj 
	 */
	public double getMj() {
		return mj;
	}

	/** 
	 * @param mj 要设置的 mj 
	 */
	public void setMj(double mj) {
		this.mj = mj;
	}

	/**
	 * 自然幢不动产单元ID，户单元才有
	 */
	private String zrzbdcdyid;
	
	/**
	 * 层ID，户单元才有
	 */
	private String cid;
	
	/**
	 * 宗地不动产单元ID，户单元才有
	 */
	private String zdbdcdyid;
	
	/**
	 * 逻辑幢不动产单元ID
	 */
	private String ljzbdcdyid;
	
	/**
	 * 权利ID
	 */
	private String qlid;
	
	/**
	 * 抵押权利ID(组合登记时用的)
	 */
	private String diyqqlid;
	
	/**
	 * 原抵押权利ID(组合登记时用的)
	 */
	private String olddiyqqlid;
	

	/**
	 * 抵押权利ID(组合登记ZY007用的)
	 */
	private String diyqqlid_2;
	
	/**
	 * 原抵押权利ID(组合登记ZT007用的)
	 */
	private String olddiyqqlid_2;
	
	/**
	 * 宗地不动产权证号（宗地信息中要显示出宗地不动产权证号）
	 */
	private String zdbdcqzh;
	public String getZdbdcqzh() {
		return zdbdcqzh;
	}

	public void setZdbdcqzh(String zdbdcqzh) {
		this.zdbdcqzh = zdbdcqzh;
	}

	public String getOlddiyqqlid() {
		return olddiyqqlid;
	}

	public void setOlddiyqqlid(String olddiyqqlid) {
		this.olddiyqqlid = olddiyqqlid;
	}
	
	public String getOlddiyqqlid2() {
		return olddiyqqlid_2;
	}

	public void setOlddiyqqlid2(String olddiyqqlid_2) {
		this.olddiyqqlid_2 = olddiyqqlid_2;
	}

	/**
	 * 原来的所有权/使用权ID
	 * 只有来源于现状的单元才有值
	 */
	private String oldqlid;
	
	/**
	 * lyqlid 当前权利的的lyqlid 
	 */
	private String lyqlid;
	
	/**
	 * 原来的bdcdyid
	 * 只有界址不变的更正登记才有值
	 */
	private String oldbdcdyid;
	
	public String getOldbdcdyid() {
		return oldbdcdyid;
	}

	public void setOldbdcdyid(String oldbdcdyid) {
		this.oldbdcdyid = oldbdcdyid;
	}

	/* bdcqzh */
	private String bdcqzh;
	
	public String getBdcqzh() {
		return bdcqzh;
	}

	public void setBdcqzh(String bdcqzh) {
		this.bdcqzh = bdcqzh;
	}
	/**
	 * 房号 2015年8月3日刘树峰添加
	 */
	private String fh;
	
	private List<UnitTree> children;
	
	public String getOldqlid() {
		return oldqlid;
	}

	public void setOldqlid(String oldqlid) {
		this.oldqlid = oldqlid;
	}
	
	public String getlyqlid() {
		return lyqlid;
	}

	public void setlyqlid(String lyqlid) {
		this.lyqlid = lyqlid;
	}

	public String getQlid() {
		return qlid;
	}

	public void setQlid(String qlid) {
		this.qlid = qlid;
	}
	
	public String getDIYQQlid() {
		return diyqqlid;
	}

	public void setDIYQQlid2(String qlid) {
		this.diyqqlid_2 = qlid;
	}
	
	public String getDIYQQlid2() {
		return diyqqlid_2;
	}

	public void setDIYQQlid(String qlid) {
		this.diyqqlid = qlid;
	}

	public UnitTree() {
		this.children = new ArrayList<UnitTree>();
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

	public List<UnitTree> getChildren() {
		return children;
	}

	public void add(UnitTree tree) {
		this.children.add(tree);
	}

	public String getDjdyid() {
		return djdyid;
	}

	public void setDjdyid(String djdyid) {
		this.djdyid = djdyid;
	}

	public String getBdcdylx() {
		return bdcdylx;
	}

	public void setBdcdylx(String bdcdylx) {
		this.bdcdylx = bdcdylx;
	}

	public String getZl() {
		return zl;
	}

	public void setZl(String zl) {
		this.zl = zl;
	}

	public String getLy() {
		return ly;
	}

	public void setLy(String ly) {
		this.ly = ly;
	}

	public void setChildren(List<UnitTree> children) {
		this.children = children;
	}

	public String getZrzbdcdyid() {
		return zrzbdcdyid;
	}

	public void setZrzbdcdyid(String zrzbdcdyid) {
		this.zrzbdcdyid = zrzbdcdyid;
	}

	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}

	public String getZdbdcdyid() {
		return zdbdcdyid;
	}

	public void setZdbdcdyid(String zdbdcdyid) {
		this.zdbdcdyid = zdbdcdyid;
	}

	public String getLjzbdcdyid() {
		return ljzbdcdyid;
	}

	public void setLjzbdcdyid(String ljzbdcdyid) {
		this.ljzbdcdyid = ljzbdcdyid;
	}

	public String getBdcdyid() {
		return bdcdyid;
	}

	public void setBdcdyid(String bdcdyid) {
		this.bdcdyid = bdcdyid;
	}

	public String getFh() {
		return fh;
	}
	
	public void setFh(String fh) {
		this.fh = fh;
	}
	
	private String bdcdyh;
	
	public String getBdcdyh() {
		return bdcdyh;
	}

	public void setBdcdyh(String bdcdyh) {
		this.bdcdyh = bdcdyh;
	}
	
	private String dyh;
	
	public String getDyh() {
		return dyh;
	}

	public void setDyh(String dyh) {
		this.dyh = dyh;
	}
	
	private String zrzh;
	
	public String getZrzh() {
		return zrzh;
	}

	public void setZrzh(String zrzh) {
		this.zrzh = zrzh;
	}
	
	private double qsc;
	
	public double getQsc() {
		return qsc;
	}

	public void setQsc(double qsc) {
		this.qsc = qsc;
	}
}
