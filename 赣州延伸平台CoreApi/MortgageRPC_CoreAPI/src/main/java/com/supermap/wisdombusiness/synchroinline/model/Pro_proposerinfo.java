package com.supermap.wisdombusiness.synchroinline.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/* 申请人信息 */
@Entity
@Table(name = "Pro_proposerinfo")
public class Pro_proposerinfo
{
	@Column
	@Id
	protected String sqr_id;
	@Column
	protected String sqr_name;
	@Column
	protected String sqr_zjlx;
	@Column
	protected String sqr_zjh;
	@Column
	protected String sqr_tel;
	@Column
	protected String sqr_adress;
	@Column
	protected String sqr_qt_fr_name;
	@Column
	protected String sqr_qt_dlr_name;
	@Column
	protected String sqr_qt_zjlx;
	@Column
	protected String sqr_qt_zjh;
	@Column
	protected String sqr_qt_tel;
	@Column
	protected String sqr_qt_adress;
	@Column
	protected String sqr_gyfs;
	@Column
	protected String sqr_gyfe;
	@Column
	protected int sqr_sxh;
	@Column
	protected int sqr_lx;
	@Column
	protected String user_id;
	@Column
	protected String proinst_id;
	@Column
	protected String fwxx_id;
	/*申请人头像*/
	@Column
	protected String sqr_pic1;
	@Column
	protected String sqr_pic2;
	@Column
	protected String sqr_qt_pic1;
	@Column
	protected String sqr_qt_pic2;

	/*法人相关字段*/
	@Column
	protected String sqr_qt_fr_zjh;
	@Column
	protected String sqr_qt_fr_zjlx;
	@Column
	protected String sqr_qt_fr_tel;

	@Column
	protected String sfcqr;//是否产权人

	@Column
	protected String sqrjzh;//申请人旧证号（企业三证合一前使用的证号）

	public String getSqrjzh() {
		return sqrjzh;
	}

	public void setSqrjzh(String sqrjzh) {
		this.sqrjzh = sqrjzh;
	}

	public String getSfcqr() {
		return sfcqr;
	}

	public void setSfcqr(String sfcqr) {
		this.sfcqr = sfcqr;
	}


	// 编号 ID
	public String getSqr_id()
	{
		return sqr_id;
	}

	public void setSqr_id(String value)
	{
		this.sqr_id = value;
	}

	// 申请人姓名
	public String getSqr_name()
	{
		return sqr_name;
	}

	public void setSqr_name(String value)
	{
		this.sqr_name = value;
	}

	// 证件类型
	public String getSqr_zjlx()
	{
		return sqr_zjlx;
	}

	public void setSqr_zjlx(String value)
	{
		this.sqr_zjlx = value;
	}

	// 证件号
	public String getSqr_zjh()
	{
		return sqr_zjh;
	}

	public void setSqr_zjh(String value)
	{
		this.sqr_zjh = value;
	}

	// 电话
	public String getSqr_tel()
	{
		return sqr_tel;
	}

	public void setSqr_tel(String value)
	{
		this.sqr_tel = value;
	}

	// 地址
	public String getSqr_adress()
	{
		return sqr_adress;
	}

	public void setSqr_adress(String value)
	{
		this.sqr_adress = value;
	}

	// 法人姓名
	public String getSqr_qt_fr_name()
	{
		return sqr_qt_fr_name;
	}

	public void setSqr_qt_fr_name(String value)
	{
		this.sqr_qt_fr_name = value;
	}

	// 代理人姓名
	public String getSqr_qt_dlr_name()
	{
		return sqr_qt_dlr_name;
	}

	public void setSqr_qt_dlr_name(String value)
	{
		this.sqr_qt_dlr_name = value;
	}

	// 法人/代理人证件类型
	public String getSqr_qt_zjlx()
	{
		return sqr_qt_zjlx;
	}

	public void setSqr_qt_zjlx(String value)
	{
		this.sqr_qt_zjlx = value;
	}

	// 法人/代理人证件号
	public String getSqr_qt_zjh()
	{
		return sqr_qt_zjh;
	}

	public void setSqr_qt_zjh(String value)
	{
		this.sqr_qt_zjh = value;
	}

	// 法人/代理人电话
	public String getSqr_qt_tel()
	{
		return sqr_qt_tel;
	}

	public void setSqr_qt_tel(String value)
	{
		this.sqr_qt_tel = value;
	}

	// 法人/代理人地址
	public String getSqr_qt_adress()
	{
		return sqr_qt_adress;
	}

	public void setSqr_qt_adress(String value)
	{
		this.sqr_qt_adress = value;
	}

	// 共有方式
	public String getSqr_gyfs()
	{
		return sqr_gyfs;
	}

	public void setSqr_gyfs(String value)
	{
		this.sqr_gyfs = value;
	}

	// 顺序号
	public int getSqr_sxh()
	{
		return sqr_sxh;
	}

	public void setSqr_sxh(int value)
	{
		this.sqr_sxh = value;
	}

	// 申请人类型 0：权利人，1：义务人
	public int getSqr_lx()
	{
		return sqr_lx;
	}

	public void setSqr_lx(int value)
	{
		this.sqr_lx = value;
	}

	// 用户编号
	public String getUser_id()
	{
		return user_id;
	}

	public void setUser_id(String value)
	{
		this.user_id = value;
	}

	// 受理项目编号
	public String getProinst_id()
	{
		return proinst_id;
	}

	public void setProinst_id(String value)
	{
		this.proinst_id = value;
	}

	/**
	 * @return the sqr_gyfe
	 */
	public String getSqr_gyfe()
	{
		return sqr_gyfe;
	}

	/**
	 * @param sqr_gyfe the sqr_gyfe to set
	 */
	public void setSqr_gyfe(String sqr_gyfe)
	{
		this.sqr_gyfe = sqr_gyfe;
	}

	public String getFwxx_id() {
		return fwxx_id;
	}

	public void setFwxx_id(String fwxx_id) {
		this.fwxx_id = fwxx_id;
	}

	public String getSqr_pic1() {
		return sqr_pic1;
	}

	public void setSqr_pic1(String sqr_pic1) {
		this.sqr_pic1 = sqr_pic1;
	}

	public String getSqr_pic2() {
		return sqr_pic2;
	}

	public void setSqr_pic2(String sqr_pic2) {
		this.sqr_pic2 = sqr_pic2;
	}

	public String getSqr_qt_pic1() {
		return sqr_qt_pic1;
	}

	public void setSqr_qt_pic1(String sqr_qt_pic1) {
		this.sqr_qt_pic1 = sqr_qt_pic1;
	}

	public String getSqr_qt_pic2() {
		return sqr_qt_pic2;
	}

	public void setSqr_qt_pic2(String sqr_qt_pic2) {
		this.sqr_qt_pic2 = sqr_qt_pic2;
	}

	public String getSqr_qt_fr_zjh() {
		return sqr_qt_fr_zjh;
	}

	public void setSqr_qt_fr_zjh(String sqr_qt_fr_zjh) {
		this.sqr_qt_fr_zjh = sqr_qt_fr_zjh;
	}

	public String getSqr_qt_fr_zjlx() {
		return sqr_qt_fr_zjlx;
	}

	public void setSqr_qt_fr_zjlx(String sqr_qt_fr_zjlx) {
		this.sqr_qt_fr_zjlx = sqr_qt_fr_zjlx;
	}

	public String getSqr_qt_fr_tel() {
		return sqr_qt_fr_tel;
	}

	public void setSqr_qt_fr_tel(String sqr_qt_fr_tel) {
		this.sqr_qt_fr_tel = sqr_qt_fr_tel;
	}
}