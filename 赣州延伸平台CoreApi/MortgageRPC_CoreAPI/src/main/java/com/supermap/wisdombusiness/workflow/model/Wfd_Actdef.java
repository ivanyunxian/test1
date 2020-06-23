
/**
 * 
 * 代码生成器自动生成[WFD_ACTDEF]
 * 
 */

package com.supermap.wisdombusiness.workflow.model;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "WFD_ACTDEF",schema = "BDC_WORKFLOW")
public class Wfd_Actdef {

	private String Actdef_Id;
	private String Prodef_Id;
	private String Actdef_Name;
	private String Actdef_Type;
	private String Actdef_Desc;
//	private Integer Actdef_Time;
	private Double Actdef_Time;
	private String Actdef_Dept;
	private Integer Acttype_Id;
	private Integer Actdef_Status;
	private Integer Pre_Actcount;
	private String Role_Id;
	private String Role_Name;
	private Integer Act_Order;
	private Integer Canlogoutornot;
	private Integer Isautopass;
	private Integer Israndom;
	private Double Randomvalue;
	private Integer Canrejectornot;
	private String Actdef_Positionx;
	private String Actdef_Positiony;
	private String Actdef_Dept_Id;
	private String Deal_Type;
	private Integer Send_Type;
	private Integer  Rejectpassback;//REJECTPASSBACK
	private Integer  Uploadfile;//Uploadfile
	private Integer Codeal;
	//转出范围控制
	private String  TurnOutRange;
	private Integer isReturnAct;
	//自定义按钮权限
	private String BtnPermission;
	//自定义参数设置
	private String CustomeParamsSet;
	
	private String isMoveItem;
	
	private Integer  AutoLock;
	
	private String LockTime;
	
	
	private String PassUrl;
	
	@Column(name = "CUSTOMEPARAMSET", length=200)
	public String getCustomeParamsSet() {
		return CustomeParamsSet;
	}

	public void setCustomeParamsSet(String customeParamsSet) {
		CustomeParamsSet = customeParamsSet;
	}

	@Column(name = "TURNOUT_RANGE", length=200)
	public String getTurnOutRange() {
		return TurnOutRange;
	}

	public void setTurnOutRange(String turnOutRange) {
		TurnOutRange = turnOutRange;
	}

	@Column(name = "DEAL_TYPE")
	public String getDeal_Type() {
		return Deal_Type;
	}

	public void setDeal_Type(String deal_Type) {
		Deal_Type = deal_Type;
	}
	@Column(name = "PASSOVER_TYPE")
	public Integer getPassover_Type() {
		return Passover_Type;
	}

	public void setPassover_Type(Integer passover_Type) {
		Passover_Type = passover_Type;
	}

	private Integer Passover_Type;

	@Id
	@Column(name = "ACTDEF_ID", length = 400)
	public String getActdef_Id() {
		if (Actdef_Id == null)
			Actdef_Id = UUID.randomUUID().toString().replace("-", "");
		return Actdef_Id;
	}

	public void setActdef_Id(String Actdef_Id) {
		this.Actdef_Id = Actdef_Id;
	}

	@Column(name = "PRODEF_ID", length = 400)
	public String getProdef_Id() {
		return Prodef_Id;
	}

	public void setProdef_Id(String Prodef_Id) {
		this.Prodef_Id = Prodef_Id;
	}

	@Column(name = "ACTDEF_NAME", length = 600)
	public String getActdef_Name() {
		return Actdef_Name;
	}

	public void setActdef_Name(String Actdef_Name) {
		this.Actdef_Name = Actdef_Name;
	}

	@Column(name = "ACTDEF_TYPE", length = 200)
	public String getActdef_Type() {
		return Actdef_Type;
	}

	public void setActdef_Type(String Actdef_Type) {
		this.Actdef_Type = Actdef_Type;
	}

	@Column(name = "ACTDEF_DESC", length = 600)
	public String getActdef_Desc() {
		return Actdef_Desc;
	}

	public void setActdef_Desc(String Actdef_Desc) {
		this.Actdef_Desc = Actdef_Desc;
	}

	@Column(name = "ACTDEF_TIME")
	public Double getActdef_Time() {
		return Actdef_Time;
	}

	public void setActdef_Time(Double Actdef_Time) {
		this.Actdef_Time = Actdef_Time;
	}

	@Column(name = "ACTDEF_DEPT", length = 600)
	public String getActdef_Dept() {
		return Actdef_Dept;
	}

	public void setActdef_Dept(String Actdef_Dept) {
		this.Actdef_Dept = Actdef_Dept;
	}

	@Column(name = "ACTTYPE_ID")
	public Integer getActtype_Id() {
		return Acttype_Id;
	}

	public void setActtype_Id(Integer Acttype_Id) {
		this.Acttype_Id = Acttype_Id;
	}

	@Column(name = "ACTDEF_STATUS")
	public Integer getActdef_Status() {
		return Actdef_Status;
	}

	public void setActdef_Status(Integer Actdef_Status) {
		this.Actdef_Status = Actdef_Status;
	}

	@Column(name = "PRE_ACTCOUNT")
	public Integer getPre_Actcount() {
		return Pre_Actcount;
	}

	public void setPre_Actcount(Integer Pre_Actcount) {
		this.Pre_Actcount = Pre_Actcount;
	}

	@Column(name = "ROLE_ID", length = 400)
	public String getRole_Id() {
		return Role_Id;
	}

	public void setRole_Id(String Role_Id) {
		this.Role_Id = Role_Id;
	}

	@Column(name = "ACT_ORDER")
	public Integer getAct_Order() {
		return Act_Order;
	}

	public void setAct_Order(Integer Act_Order) {
		this.Act_Order = Act_Order;
	}

	@Column(name = "CANLOGOUTORNOT")
	public Integer getCanlogoutornot() {
		return Canlogoutornot;
	}

	public void setCanlogoutornot(Integer Canlogoutornot) {
		this.Canlogoutornot = Canlogoutornot;
	}

	@Column(name = "ISAUTOPASS")
	public Integer getIsautopass() {
		return Isautopass;
	}

	public void setIsautopass(Integer Isautopass) {
		this.Isautopass = Isautopass;
	}

	@Column(name = "ISRANDOM")
	public Integer getIsrandom() {
		return Israndom;
	}

	public void setIsrandom(Integer Israndom) {
		this.Israndom = Israndom;
	}

	@Column(name = "RANDOMVALUE", precision = 22, scale = 2)
	public Double getRandomvalue() {
		return Randomvalue;
	}

	public void setRandomvalue(Double Randomvalue) {
		this.Randomvalue = Randomvalue;
	}

	@Column(name = "CANREJECTORNOT")
	public Integer getCanrejectornot() {
		return Canrejectornot;
	}

	public void setCanrejectornot(Integer Canrejectornot) {
		this.Canrejectornot = Canrejectornot;
	}

	@Column(name = "ACTDEF_POSITIONX", length = 20)
	public String getActdef_Positionx() {
		return Actdef_Positionx;
	}

	public void setActdef_Positionx(String Actdef_Positionx) {
		this.Actdef_Positionx = Actdef_Positionx;
	}

	@Column(name = "ACTDEF_POSITIONY", length = 10)
	public String getActdef_Positiony() {
		return Actdef_Positiony;
	}

	public void setActdef_Positiony(String Actdef_Positiony) {
		this.Actdef_Positiony = Actdef_Positiony;
	}

	@Column(name = "ACTDEF_DEPT_ID", length = 400)
	public String getActdef_Dept_Id() {
		return Actdef_Dept_Id;
	}

	public void setActdef_Dept_Id(String Actdef_Dept_Id) {
		this.Actdef_Dept_Id = Actdef_Dept_Id;
	}

	/**
	 * @return the role_Name
	 */
	@Column(name = "ROLE_NAME", length = 400)
	public String getRole_Name() {
		return Role_Name;
	}

	/**
	 * @param role_Name the role_Name to set
	 */
	public void setRole_Name(String role_Name) {
		Role_Name = role_Name;
	}
	
	@Column(name = "SEND_TYPE")
	public Integer getSend_Type() {
		if(Send_Type==null){
			return 0;
		}
		return Send_Type;
	}

	public void setSend_Type(Integer send_Type) {
		Send_Type = send_Type;
	}
	@Column(name = "REJECTPASSBACK")
	public Integer getRejectpassback() {
		return Rejectpassback;
	}

	public void setRejectpassback(Integer rejectpassback) {
		Rejectpassback = rejectpassback;
	}
	@Column(name = "UPLOADFILE")
	public Integer getUploadfile() {
		return Uploadfile;
	}

	public void setUploadfile(Integer uploadfile) {
		Uploadfile = uploadfile;
	}
	@Column(name = "CODEAL")
	public Integer getCodeal() {
		return Codeal;
	}

	public void setCodeal(Integer codeal) {
		Codeal = codeal;
	}
	@Column(name = "ISRETURNACT")
	public Integer getIsReturnAct() {
		return isReturnAct;
	}

	public void setIsReturnAct(Integer isReturnAct) {
		this.isReturnAct = isReturnAct;
	}
	
	@Column(name = "BTNPERMISSION")
	public String getBtnPermission() {
		return BtnPermission;
	}
   
	public void setBtnPermission(String btnPermission) {
		BtnPermission = btnPermission;
	}
	@Transient
	public String getIsMoveItem() {
		return isMoveItem;
	}

	public void setIsMoveItem(String isMoveItem) {
		this.isMoveItem = isMoveItem;
	}
	
	@Column(name = "AUTOLOCK")
	public Integer getAutoLock() {
		return AutoLock;
	}

	public void setAutoLock(Integer autoLock) {
		AutoLock = autoLock;
	}
	
	@Column(name = "LOCKTIME")
	public String getLockTime() {
		return LockTime;
	}

	public void setLockTime(String lockTime) {
		LockTime = lockTime;
	}

	@Column(name = "PASSURL" , length=200)
	public String getPassUrl() {
		return PassUrl;
	}

	public void setPassUrl(String passUrl) {
		PassUrl = passUrl;
	}

	
	
}
