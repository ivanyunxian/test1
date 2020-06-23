package com.supermap.intelligent.model;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "WFI_PROCESS", schema = "BDC_MRPC")
public class BDC_MRPC_WFI_PROCESS {

	private String Proinst_Id;
	private String Prolsh;
	private String Division_Code;
	private Date Createtime;
	private Date Modifytime;
	private String Versionno;
	private String Actinst_Id;
	private String Staff_Id;
	private Date Actinst_Start;
	private Date Actinst_End;
	private String Actinst_Name;
	private Date Actinst_Willfinish;
	private int Actinst_Status;
	private Date Hangup_Time;
	private String Hangup_Staff_Name;
	private Date Hangdowm_Time;
	private String Staff_Name;
	private String ywlsh;
	private  String bhyy;
	private String Actinst_Msg;
	private String shyj;

	public BDC_MRPC_WFI_PROCESS() {
		super();
	}

	@Column(name = "BHYY", length = 1000)
	public String getBhyy() {
		return bhyy;
	}

	public void setBhyy(String bhyy) {
		this.bhyy = bhyy;
	}

	@Column(name = "shyj", length = 2000)
	public String getShyj() {
		return shyj;
	}

	public void setShyj(String shyj) {
		this.shyj = shyj;
	}

	@Column(name = "ACTINST_MSG", length = 2000)
	public String getActinst_Msg() {
		return Actinst_Msg;
	}

	public void setActinst_Msg(String Actinst_Msg) {
		this.Actinst_Msg = Actinst_Msg;
	}

	@Column(name = "ywlsh")
	public String getYwlsh() {
		return ywlsh;
	}

	public void setYwlsh(String ywlsh) {
		this.ywlsh = ywlsh;
	}

	@Column(name = "prolsh")
	public String getProlsh() {
		return Prolsh;
	}

	public void setProlsh(String prolsh) {
		this.Prolsh = prolsh;
	}

	@Column(name = "division_code")
	public String getDivision_code() {
		return Division_Code;
	}

	public void setDivision_code(String division_code) {
		this.Division_Code = division_code;
	}

	@Column(name = "create_time")
	public Date getCreatetime() {
		return Createtime;
	}

	public void setCreatetime(Date createtime) {
		this.Createtime = createtime;
	}

	@Column(name = "modifytime")
	public Date getModifytime() {
		return Modifytime;
	}

	public void setModifytime(Date modifytime) {
		this.Modifytime = modifytime;
	}

	@Column(name = "versionno")
	public String getVersionno() {
		return Versionno;
	}

	public void setVersionno(String versionno) {
		this.Versionno = versionno;
	}

	@Id
	@Column(name = "ACTINST_ID", length = 400)
	public String getActinst_Id() {
		if (Actinst_Id == null) {
			Actinst_Id = UUID.randomUUID().toString().replace("-", "");
		}
		return Actinst_Id;
	}

	public void setActinst_Id(String Actinst_Id) {
		this.Actinst_Id = Actinst_Id;
	}

	@Column(name = "HANGUP_TIME", length = 7)
	public Date getHangup_Time() {
		return Hangup_Time;
	}

	public void setHangup_Time(Date Hangup_Time) {
		this.Hangup_Time = Hangup_Time;
	}

	@Column(name = "HANGDOWM_TIME", length = 7)
	public Date getHangdowm_Time() {
		return Hangdowm_Time;
	}

	public void setHangdowm_Time(Date Hangdowm_Time) {
		this.Hangdowm_Time = Hangdowm_Time;
	}

	@Column(name = "HANGUP_STAFF_NAME", length = 400)
	public String getHangup_Staff_Name() {
		return Hangup_Staff_Name;
	}

	public void setHangup_Staff_Name(String Hangup_Staff_Name) {
		this.Hangup_Staff_Name = Hangup_Staff_Name;
	}

	@Column(name = "PROINST_ID", length = 400)
	public String getProinst_Id() {
		return Proinst_Id;
	}

	public void setProinst_Id(String Proinst_Id) {
		this.Proinst_Id = Proinst_Id;
	}

	@Column(name = "STAFF_ID", length = 400)
	public String getStaff_Id() {
		return Staff_Id;
	}

	public void setStaff_Id(String Staff_Id) {
		this.Staff_Id = Staff_Id;
	}

	@Column(name = "ACTINST_START", length = 7)
	public Date getActinst_Start() {
		return Actinst_Start;
	}

	public void setActinst_Start(Date Actinst_Start) {
		this.Actinst_Start = Actinst_Start;
	}

	@Column(name = "ACTINST_END", length = 7)
	public Date getActinst_End() {
		return Actinst_End;
	}

	public void setActinst_End(Date Actinst_End) {
		this.Actinst_End = Actinst_End;
	}

	@Column(name = "ACTINST_WILLFINISH", length = 7)
	public Date getActinst_Willfinish() {
		return Actinst_Willfinish;
	}

	public void setActinst_Willfinish(Date Actinst_Willfinish) {
		this.Actinst_Willfinish = Actinst_Willfinish;
	}

	@Column(name = "ACTINST_NAME", length = 1200)
	public String getActinst_Name() {
		return Actinst_Name;
	}

	public void setActinst_Name(String Actinst_Name) {
		this.Actinst_Name = Actinst_Name;
	}

	@Column(name = "ACTINST_STATUS")
	public Integer getActinst_Status() {
		return Actinst_Status;
	}

	public void setActinst_Status(Integer Status) {
		this.Actinst_Status = Status;
	}

	/**
	 * @return the staff_Name
	 */
	@Column(name = "STAFF_NAME", length = 400)
	public String getStaff_Name() {
		return Staff_Name;
	}

	/**
	 * @param staff_Name
	 *            the staff_Name to set
	 */
	public void setStaff_Name(String staff_Name) {
		Staff_Name = staff_Name;
	}

}
