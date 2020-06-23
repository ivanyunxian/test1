package com.supermap.wisdombusiness.synchroinline.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/* 受理项目 */
@Entity
@Table(name = "Pro_proinst")
public class Pro_proinst
{
	@Column
	@Id
	protected String id;
	@Column
	protected String name;
	@Column
	protected String prodefid;
	@Column
	protected String prodefcode;
	@Column
	protected Date prostart;
	@Column
	protected Date proend;
	@Column
	protected double status;
	@Column
	protected String prolsh;
	@Column
	protected String areacode;
	@Column
	protected String areaname;
	@Column
	protected String user_id;
	@Column
	protected String user_name;
	@Column
	protected String bdcdyh;
	@Column
	protected String bdczl;
	@Column
	protected String bdcqzmh;
	@Column
	protected double fwsyq_mj;
	@Column
	protected String fwsyq_yt;
	@Column
	protected String extend_data;
	@Column
	protected int shzt;
	@Column
	protected int tbzt;
	@Column
	protected String djlx;
	@Column
	protected String qllx;
	@Column
	protected String xmlx;
	@Column
	protected String wf_prodefid;
	@Column
	protected String lsh;
	@Column
	protected String wf_prodefname;
	@Column
    protected String ywlx;//业务类型，0个人，1企业
	/**
     * 权利信息
     */
    @Column
	protected Date qlqssj;
	@Column
	protected Date qljssj;
	@Column
	protected String dyfs;
	@Column
	protected double zgzqse;
	@Column
	protected double bdbzzqse;
	
	@Column
	protected String sfhbzs;//是否合并证书

	@Column
	protected String sfjsbl;//是否即时办理 1- 是 2-已办理
	@Column
	protected Date qt_qlqssj;
	@Column
	protected Date qt_qljssj;
	@Column
	protected String qt_sfhbzs;//其他是否合并证书

	@Column
	protected String sqbm;//交易申请编码

	@Column
	protected String jyywlx;//交易业务类型

	@Column
	protected Date tjsj;//提交时间
	
	@Column
	protected Integer send_msg;
	
	public Integer getSend_msg() {
		return send_msg;
	}

	public void setSend_msg(Integer send_msg) {
		this.send_msg = send_msg;
	}

	public String getJyywlx() {
		return jyywlx;
	}

	public void setJyywlx(String jyywlx) {
		this.jyywlx = jyywlx;
	}

	public String getSqbm() {
		return sqbm;
	}

	public void setSqbm(String sqbm) {
		this.sqbm = sqbm;
	}

	public String getSfjsbl() {
		return sfjsbl;
	}

	public void setSfjsbl(String sfjsbl) {
		this.sfjsbl = sfjsbl;
	}

	// 编号 ID
	public String getId()
	{
		return id;
	}

	public void setId(String value)
	{
		this.id = value;
	}

	// 流程名称 NAME
	public String getName()
	{
		return name;
	}

	public void setName(String value)
	{
		this.name = value;
	}

	// 流程ID CODE
	public String getProdefid()
	{
		return prodefid;
	}

	public void setProdefid(String value)
	{
		this.prodefid = value;
	}

	// 流程编码
	public String getProdefcode()
	{
		return prodefcode;
	}

	public void setProdefcode(String value)
	{
		this.prodefcode = value;
	}

	// 开始时间 PROSTART
	public Date getProstart()
	{
		return prostart;
	}

	public void setProstart(Date value)
	{
		this.prostart = value;
	}

	// 截止时间 PROEND
	public Date getProend()
	{
		return proend;
	}

	public void setProend(Date value)
	{
		this.proend = value;
	}

	// 状态 STATUS
	public double getStatus()
	{
		return status;
	}

	public void setStatus(double value)
	{
		this.status = value;
	}

	// 流水号 流水号
	public String getProlsh()
	{
		return prolsh;
	}

	public void setProlsh(String value)
	{
		this.prolsh = value;
	}

	// 行政区代码 行政区划编码
	public String getAreacode()
	{
		return areacode;
	}

	public void setAreacode(String value)
	{
		this.areacode = value;
	}

	// 行政区名称 行政区划名称
	public String getAreaname()
	{
		return areaname;
	}

	public void setAreaname(String value)
	{
		this.areaname = value;
	}

	// 申请受理用户ID
	public String getUser_id()
	{
		return user_id;
	}

	public void setUser_id(String value)
	{
		this.user_id = value;
	}

	// 申请受理用户姓名
	public String getUser_name()
	{
		return user_name;
	}

	public void setUser_name(String value)
	{
		this.user_name = value;
	}

	// 不动产单元号
	public String getBdcdyh()
	{
		return bdcdyh;
	}

	public void setBdcdyh(String value)
	{
		this.bdcdyh = value;
	}

	// 不动产坐落
	public String getBdczl()
	{
		return bdczl;
	}

	public void setBdczl(String value)
	{
		this.bdczl = value;
	}

	// 不动产权证/证明号
	public String getBdcqzmh()
	{
		return bdcqzmh;
	}

	public void setBdcqzmh(String value)
	{
		this.bdcqzmh = value;
	}

	// 房屋所有权面积
	public double getFwsyq_mj()
	{
		return fwsyq_mj;
	}

	public void setFwsyq_mj(double value)
	{
		this.fwsyq_mj = value;
	}

	// 房屋所有权用途 填写房屋用途字典编码
	public String getFwsyq_yt()
	{
		return fwsyq_yt;
	}

	public void setFwsyq_yt(String value)
	{
		this.fwsyq_yt = value;
	}

	// 扩展数据 其他扩展数据使用JSON对象进行存储
	public String getExtend_data()
	{
		return extend_data;
	}

	public void setExtend_data(String value)
	{
		this.extend_data = value;
	}

	// 审核状态 0：未审核，10：权籍调查审核通过，11：权籍调查审核驳回，20：项目受理审核通过，21：项目受理审核驳回
	public int getShzt()
	{
		return shzt;
	}

	public void setShzt(int value)
	{
		this.shzt = value;
	}

	// 数据同步状态 0：内外网数据一致，1：外网数据已经变化，内网需要更新，2：内网数据已经变化，外网需要更新（针对前置机库，外网库状态不影响）
	public int getTbzt()
	{
		return tbzt;
	}

	public void setTbzt(int value)
	{
		this.tbzt = value;
	}

	/**
	 * @return the djlx
	 */
	public String getDjlx()
	{
		return djlx;
	}

	/**
	 * @param djlx
	 *            the djlx to set
	 */
	public void setDjlx(String djlx)
	{
		this.djlx = djlx;
	}

	/**
	 * @return the qllx
	 */
	public String getQllx()
	{
		return qllx;
	}

	/**
	 * @param qllx
	 *            the qllx to set
	 */
	public void setQllx(String qllx)
	{
		this.qllx = qllx;
	}

	/**
	 * @return the xmlx
	 */
	public String getXmlx()
	{
		return xmlx;
	}

	/**
	 * @param xmlx
	 *            the xmlx to set
	 */
	public void setXmlx(String xmlx)
	{
		this.xmlx = xmlx;
	}

	/**
	 * @return the wf_prodefid
	 */
	public String getWf_prodefid()
	{
		return wf_prodefid;
	}

	/**
	 * @param wf_prodefid the wf_prodefid to set
	 */
	public void setWf_prodefid(String wf_prodefid)
	{
		this.wf_prodefid = wf_prodefid;
	}

	/**
	 * @return the lsh
	 */
	public String getLsh()
	{
		return lsh;
	}

	/**
	 * @param lsh the lsh to set
	 */
	public void setLsh(String lsh)
	{
		this.lsh = lsh;
	}

	/**
	 * @return the wf_prodefname
	 */
	public String getWf_prodefname()
	{
		return wf_prodefname;
	}

	/**
	 * @param wf_prodefname the wf_prodefname to set
	 */
	public void setWf_prodefname(String wf_prodefname)
	{
		this.wf_prodefname = wf_prodefname;
	}

	public String getYwlx() {
		return ywlx;
	}

	public void setYwlx(String ywlx) {
		this.ywlx = ywlx;
	}

	public Date getQlqssj() {
		return qlqssj;
	}

	public void setQlqssj(Date qlqssj) {
		this.qlqssj = qlqssj;
	}

	public Date getQljssj() {
		return qljssj;
	}

	public void setQljssj(Date qljssj) {
		this.qljssj = qljssj;
	}

	public String getDyfs() {
		return dyfs;
	}

	public void setDyfs(String dyfs) {
		this.dyfs = dyfs;
	}

	public double getZgzqse() {
		return zgzqse;
	}

	public void setZgzqse(double zgzqse) {
		this.zgzqse = zgzqse;
	}

	public double getBdbzzqse() {
		return bdbzzqse;
	}

	public void setBdbzzqse(double bdbzzqse) {
		this.bdbzzqse = bdbzzqse;
	}

	public String getSfhbzs() {
		return sfhbzs;
	}

	public void setSfhbzs(String sfhbzs) {
		this.sfhbzs = sfhbzs;
	}

	public Date getQt_qlqssj() {
		return qt_qlqssj;
	}

	public void setQt_qlqssj(Date qt_qlqssj) {
		this.qt_qlqssj = qt_qlqssj;
	}

	public Date getQt_qljssj() {
		return qt_qljssj;
	}

	public void setQt_qljssj(Date qt_qljssj) {
		this.qt_qljssj = qt_qljssj;
	}

	public String getQt_sfhbzs() {
		return qt_sfhbzs;
	}

	public void setQt_sfhbzs(String qt_sfhbzs) {
		this.qt_sfhbzs = qt_sfhbzs;
	}

	public Date getTjsj() {
		return tjsj;
	}

	public void setTjsj(Date tjsj) {
		this.tjsj = tjsj;
	}
}
