package com.supermap.realestate.registration.ViewClass;

import java.util.Date;

/**
 * 
 * @Description:项目信息
 * @author 刘树峰
 * @date 2015年6月22日 下午2:48:48
 * @Copyright SuperMap
 */
public class ProjectInfo {
	private String xzqhdm;

	public String getXzqhdm() {
		return xzqhdm;
	}

	public void setXzqhdm(String xzqhdm) {
		this.xzqhdm = xzqhdm;
	}

	private String currentuser;

	/**
	 * 流程编号
	 */
	private String project_id;

	/**
	 * 项目编号
	 */
	private String xmbh;

	/**
	 * 项目名称
	 */
	private String xmmc;

	/**
	 * 登记类型
	 */
	private String djlx;

	/**
	 * 登记类型名称
	 */
	private String djlxmc;

	/**
	 * 创建时间
	 */
	private Date createTime;

	/**
	 * 权利类型名称
	 */
	private String qllxmc;

	/**
	 * 权利类型
	 */
	private String qllx;

	/**
	 * 受理类型
	 */
	private String sllx1;

	/**
	 * 受理类型2
	 */
	private String sllx2;

	/**
	 * 受理人员
	 */
	private String slry;

	/**
	 * 受理时间
	 */
	private Date slsj;
	
	/**
	 * 证书是否显示宗地信息
	 */
	private Boolean zdbtn;
	
	/**
	 * 登簿时间
	 */
	private Date djsj;

	public Date getDjsj() {
		return djsj;
	}

	public void setDjsj(Date djsj) {
		this.djsj = djsj;
	}

	/**
	 * 不动产单元类型
	 */
	private String bdcdylx;
	/**
	 * 基准流程编码
	 */
	private String baseworkflowcode;

	/**
	 * 项目是否只读
	 */
	private boolean readonly = false;

	/**
	 * 房屋信息是否可编辑
	 */
	private boolean houseedit = false;

	private String ywlsh="";
	
	private String fcywh="";
	
	public String getFcywh() {
		return fcywh;
	}

	public void setFcywh(String fcywh) {
		this.fcywh = fcywh;
	}

	/**
	 * 是否显示数据上报按钮(0:不显示；1：显示(自动上报)；2：显示（手动上报）)
	 */
	private String showDataReportBtn = "0";
	
	/**
	 * 是否启用新图形系统
	 */
	private boolean  isEnableNewGis=false;
	
	public boolean getIsEnableNewGis() {
		return isEnableNewGis;
	}

	public void setIsEnableNewGis(boolean isenablenewgis) {
		this.isEnableNewGis = isenablenewgis;
	}
	
	/**
	 * 是否启用单房标红-柳州
	 */
	private boolean isEnableRedFlag=false;
	public boolean getIsEnableRedFlag() {
		return isEnableRedFlag;
	}

	public void setIsEnableRedFlag(boolean isEnableRedFlag) {
		this.isEnableRedFlag = isEnableRedFlag;
	}

	public void setEnableNewGis(boolean isEnableNewGis) {
		this.isEnableNewGis = isEnableNewGis;
	}

	/**
	 * 选择单元时是否显示楼盘表信息
	 */
	private boolean showBuildingTable =false;
	public boolean isShowBuildingTable() {
		return showBuildingTable;
	}

	public void setShowBuildingTable(boolean showBuildingTable) {
		this.showBuildingTable = showBuildingTable;
	}

	/**
	 * 业务流水号
	 */
	public String getYwlsh() {
		return ywlsh;
	}

	/**
	 * 业务流水号
	 */
	public void setYwlsh(String ywlsh) {
		this.ywlsh = ywlsh;
	}

	/**
	 * @return houseedit
	 */
	public boolean getHouseedit() {
		return houseedit;
	}

	private String sfhbzs = "0";

	public String getSfhbzs() {
		return sfhbzs;
	}

	public void setSfhbzs(String sfhbzs) {
		this.sfhbzs = sfhbzs;
	}

	/**
	 * @param houseedit
	 *            要设置的 houseedit
	 */
	public void setHouseedit(boolean houseedit) {
		this.houseedit = houseedit;
	}

	/**
	 * 土地信息是否可编辑
	 */
	private boolean landedit = false;

	/**
	 * @return landedit
	 */
	public boolean getLandedit() {
		return landedit;
	}

	/**
	 * @param landedit
	 *            要设置的 houseedit
	 */
	public void setLandedit(boolean landedit) {
		this.landedit = landedit;
	}

	private String selectorname;

	/**
	 * @return selectorname
	 */
	public String getSelectorname() {
		return selectorname;
	}

	/**
	 * @param selectorname
	 *            要设置的 selectorname
	 */
	public void setSelectorname(String selectorname) {
		this.selectorname = selectorname;
	}

	public void setBdcdylx(String bdcdylx) {
		this.bdcdylx = bdcdylx;
	}

	public String getCurrentuser() {
		return currentuser;
	}

	public void setCurrentuser(String currentuser) {
		this.currentuser = currentuser;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getDjlxmc() {
		return djlxmc;
	}

	public void setDjlxmc(String djlxmc) {
		this.djlxmc = djlxmc;
	}

	public String getQllxmc() {
		return qllxmc;
	}

	public void setQllxmc(String qllxmc) {
		this.qllxmc = qllxmc;
	}

	public boolean isReadonly() {
		return readonly;
	}

	public void setReadonly(boolean readonly) {
		this.readonly = readonly;
	}

	public String getProject_id() {
		return project_id;
	}

	public String getBdcdylx() {
		return this.bdcdylx;
	}

	public void setProject_id(String project_id) {
		this.project_id = project_id;
	}

	public String getXmbh() {
		return xmbh;
	}

	public void setXmbh(String xmbh) {
		this.xmbh = xmbh;
	}

	public String getXmmc() {
		return xmmc;
	}

	public void setXmmc(String xmmc) {
		this.xmmc = xmmc;
	}
	
	public Boolean getZdbtn() {
		return zdbtn;
	}

	public void setZdbtn(Boolean zdbtn) {
		this.zdbtn = zdbtn;
	}

	public String getDjlx() {
		return djlx;
	}

	public void setDjlx(String djlx) {
		this.djlx = djlx;
	}

	public String getQllx() {
		return qllx;
	}

	public void setQllx(String qllx) {
		this.qllx = qllx;
	}

	public String getSllx1() {
		return sllx1;
	}

	public void setSllx1(String sllx1) {
		this.sllx1 = sllx1;
	}

	public String getSllx2() {
		return sllx2;
	}

	public void setSllx2(String sllx2) {
		this.sllx2 = sllx2;
	}

	public String getSlry() {
		return slry;
	}

	public void setSlry(String slry) {
		this.slry = slry;
	}

	public Date getSlsj() {
		return slsj;
	}

	public void setSlsj(Date slsj) {
		this.slsj = slsj;
	}
	

	public String getShowDataReportBtn() {
		return showDataReportBtn;
	}

	public void setShowDataReportBtn(String showDataReportBtn) {
		this.showDataReportBtn = showDataReportBtn;
	}

	private String dyfs = "";

	public String getDyfs() {
		return dyfs;
	}

	public void setDyfs(String dyfs) {
		this.dyfs = dyfs;
	}
	
	private String unitpageid = "";

	public String getUnitpageid() {
		return unitpageid;
	}

	public void setUnitpageid(String unitpageid) {
		this.unitpageid = unitpageid;
	}
	
	private String rightpageid = "";

	public String getRightpageid() {
		return rightpageid;
	}

	public void setRightpageid(String rightpageid) {
		this.rightpageid = rightpageid;
	}

	public String getBaseworkflowcode() {
		return baseworkflowcode;
	}

	public void setBaseworkflowcode(String baseworkflowcode) {
		this.baseworkflowcode = baseworkflowcode;
	}

	/**
	 * 是否已发送公告
	 */
	private boolean sendNotice = false;

	public boolean isSendNotice() {
		return sendNotice;
	}

	public void setSendNotice(boolean sendNotice) {
		this.sendNotice = sendNotice;
	}
	
	private String sfdb;

	public String getSfdb() {
		return sfdb;
	}

	public void setSfdb(String sfdb) {
		this.sfdb = sfdb;
	}
	private String wlsh;
	/**
	 * 外网流水号
	 */
	public String getWlsh() {
		return wlsh;
	}

	/**
	 * 外网流水号
	 */
	public void setWlsh(String wlsh) {
		this.wlsh = wlsh;
	}

	private Integer house_status;

	public Integer getHouse_status() {
		return house_status;
	}

	public void setHouse_status(Integer house_status) {
		this.house_status = house_status;
	}

}
