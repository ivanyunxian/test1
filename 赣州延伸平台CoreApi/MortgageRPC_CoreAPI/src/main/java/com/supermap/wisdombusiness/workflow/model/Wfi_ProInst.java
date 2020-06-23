/**
 * 
 * 代码生成器自动生成[WFI_PROINST]
 * 
 */

package com.supermap.wisdombusiness.workflow.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "WFI_PROINST",schema="BDC_WORKFLOW")
public class Wfi_ProInst {

	private String Proinst_Id;
	private String Prodef_Id;
	private String Prodef_Name;
	private Integer Transation_Type;
	private String Operation_Type;
	private String Proinst_Code;
	private Date Proinst_Start;
	private Date Proinst_End;
	private Date Proinst_Willfinish;
	private Integer Proinst_Status;
	private String Project_Name;
	private String File_Number;
	private Date Creat_Date;
	private String Acceptor;
	private String District_Id;
	private String Staff_Id;
	private String Staff_Distid;
	private Integer Status;
	private Integer Urgency;
	private Integer Instance_Type;
	private String  Remarks;
	private Integer Istransfer;
	private Date Transfer_Time;
	private String Prolsh;
	private Double Proinst_Time;
	private Integer Proinst_Weight;
	private String Batch;
	private String Ywh;
	//冗余当前活动的信息：
	private String Actdef_Type;
	private String Actinst_Id;
	private Integer IsApplyHangup;
	private Integer Statusext;
	private String Operation_Type_Nact;
	private String Staff_Id_Nact;
	private Integer Actinst_Status;
	private Date Actinst_Willfinish;
	private String Actinst_Name;
	private String Msg;
	private Integer Codeal;
	private Date  ACTINST_START;
	private Date  ACTINST_END;
	private String Staff_Name_Nact;
	private String AreaCode;
	private Integer FromInline;
	private String IsMail;
	private String MailNumber;
	private Integer GD_Status;
	private Integer ISSFCT;
	private String GXZLBM;
	private String IsPushed;
	private String WLSH;
	private String sfjslb;
	private String YWLY;

	@Column(name="YWLY")
	public String getYWLY() {return YWLY;}

	public void setYWLY(String YWLY) {this.YWLY = YWLY;}

	@Column(name = "SFJSBL")
	public String getSfjslb() {
		return sfjslb;
	}

	public void setSfjslb(String sfjslb) {
		this.sfjslb = sfjslb;
	}

	@Column(name = "WLSH", length = 100)
	public String getWLSH() {
		return WLSH;
	}

	public void setWLSH(String wLSH) {
		WLSH = wLSH;
	}

	public Integer getGD_Status() {
		return GD_Status;
	}

	public void setGD_Status(Integer gD_Status) {
		GD_Status = gD_Status;
	}

	@Id
	@Column(name = "PROINST_ID", length = 400)
	public String getProinst_Id() {
		if (Proinst_Id == null)
			Proinst_Id = UUID.randomUUID().toString().replace("-", "");
		return Proinst_Id;
	}

	public void setProinst_Id(String Proinst_Id) {
		this.Proinst_Id = Proinst_Id;
	}

	@Column(name = "PRODEF_ID", length = 400)
	public String getProdef_Id() {
		return Prodef_Id;
	}

	public void setProdef_Id(String Prodef_Id) {
		this.Prodef_Id = Prodef_Id;
	}

	@Column(name = "PRODEF_NAME", length = 600)
	public String getProdef_Name() {
		return Prodef_Name;
	}

	public void setProdef_Name(String Prodef_Name) {
		this.Prodef_Name = Prodef_Name;
	}

	@Column(name = "TRANSATION_TYPE")
	public Integer getTransation_Type() {
		return Transation_Type;
	}

	public void setTransation_Type(Integer Transation_Type) {
		this.Transation_Type = Transation_Type;
	}

	@Column(name = "OPERATION_TYPE", length = 600)
	public String getOperation_Type() {
		return Operation_Type;
	}

	public void setOperation_Type(String Operation_Type) {
		this.Operation_Type = Operation_Type;
	}

	@Column(name = "PROINST_CODE", length = 600)
	public String getProinst_Code() {
		return Proinst_Code;
	}

	public void setProinst_Code(String Proinst_Code) {
		this.Proinst_Code = Proinst_Code;
	}

	@Column(name = "PROINST_START", length = 7)
	public Date getProinst_Start() {
		return Proinst_Start;
	}

	public void setProinst_Start(Date Proinst_Start) {
		this.Proinst_Start = Proinst_Start;
	}

	@Column(name = "PROINST_END", length = 7)
	public Date getProinst_End() {
		return Proinst_End;
	}

	public void setProinst_End(Date Proinst_End) {
		this.Proinst_End = Proinst_End;
	}

	@Column(name = "PROINST_WILLFINISH", length = 7)
	public Date getProinst_Willfinish() {
		return Proinst_Willfinish;
	}

	public void setProinst_Willfinish(Date Proinst_Willfinish) {
		this.Proinst_Willfinish = Proinst_Willfinish;
	}

	@Column(name = "PROINST_STATUS")
	public Integer getProinst_Status() {
		return Proinst_Status;
	}

	public void setProinst_Status(Integer Proinst_Status) {
		this.Proinst_Status = Proinst_Status;
	}

	@Column(name = "PROJECT_NAME", length = 1000)
	public String getProject_Name() {
		return Project_Name;
	}

	public void setProject_Name(String Project_Name) {
		this.Project_Name = Project_Name;
	}

	@Column(name = "FILE_NUMBER", length = 100)
	public String getFile_Number() {
		return File_Number;
	}

	public void setFile_Number(String File_Number) {
		this.File_Number = File_Number;
	}

	@Column(name = "CREAT_DATE", length = 7)
	public Date getCreat_Date() {
		return Creat_Date;
	}

	public void setCreat_Date(Date Creat_Date) {
		this.Creat_Date = Creat_Date;
	}

	@Column(name = "ACCEPTOR", length = 100)
	public String getAcceptor() {
		return Acceptor;
	}

	public void setAcceptor(String Acceptor) {
		this.Acceptor = Acceptor;
	}

	@Column(name = "DISTRICT_ID", length = 100)
	public String getDistrict_Id() {
		return District_Id;
	}

	public void setDistrict_Id(String District_Id) {
		this.District_Id = District_Id;
	}

	@Column(name = "STAFF_ID", length = 100)
	public String getStaff_Id() {
		return Staff_Id;
	}

	public void setStaff_Id(String Staff_Id) {
		this.Staff_Id = Staff_Id;
	}

	@Column(name = "STAFF_DISTID", length = 100)
	public String getStaff_Distid() {
		return Staff_Distid;
	}

	public void setStaff_Distid(String Staff_Distid) {
		this.Staff_Distid = Staff_Distid;
	}

	@Column(name = "STATUS")
	public Integer getStatus() {
		return Status;
	}

	public void setStatus(Integer Status) {
		this.Status = Status;
	}

	@Column(name = "URGENCY")
	public Integer getUrgency() {
		if(Urgency!=null){
			return Urgency;
		}else{
			return 1;
		}
		
	}

	public void setUrgency(Integer Urgency) {
		this.Urgency = Urgency;
	}

	@Column(name = "INSTANCE_TYPE")
	public Integer getInstance_Type() {
		return Instance_Type;
	}

	public void setInstance_Type(Integer Instance_Type) {
		this.Instance_Type = Instance_Type;
	}

	/**
	 * @return the remarks
	 */
	@Column(name = "REMARKS")
	public String getRemarks() {
		return Remarks;
	}

	/**
	 * @param remarks the remarks to set
	 */
	public void setRemarks(String remarks) {
		Remarks = remarks;
	}

	/**
	 * @return the istransfer
	 */
	@Column(name = "ISTRANSFER")
	public Integer getIstransfer() {
		return Istransfer;
	}

	/**
	 * @param istransfer the istransfer to set
	 */
	public void setIstransfer(Integer istransfer) {
		Istransfer = istransfer;
	}

	/**
	 * @return the transfer_Time
	 */
	@Column(name = "TRANSFER_TIME", length = 7)
	public Date getTransfer_Time() {
		return Transfer_Time;
	}

	/**
	 * @param transfer_Time the transfer_Time to set
	 */
	public void setTransfer_Time(Date transfer_Time) {
		Transfer_Time = transfer_Time;
	}
	@Column(name = "PROLSH", length = 100)
	public String getProlsh() {
		return Prolsh;
	}

	public void setProlsh(String prolsh) {
		Prolsh = prolsh;
	}
	@Column(name = "PROINST_TIME")
	public Double getProinst_Time() {
		return Proinst_Time;
	}

	public void setProinst_Time(Double proinst_Time) {
		Proinst_Time = proinst_Time;
	}
	@Column(name = "PROINST_WEIGHT")
	public Integer getProinst_Weight() {
		if(Proinst_Weight!=null){
			return Proinst_Weight;
		}else{
			return 0;
		}
		
	}

	public void setProinst_Weight(Integer proinst_Weight) {
		Proinst_Weight = proinst_Weight;
	}
	@Column(name = "BATCH")
	public String getBatch() {
		return Batch;
	}

	public void setBatch(String batch) {
		Batch = batch;
	}
	@Column(name = "YWH", length = 100)
	public String getYwh() {
		return Ywh;
	}

	public void setYwh(String ywh) {
		Ywh = ywh;
	}
	@Column(name = "ACTDEF_TYPE")
	public String getActdef_Type() {
		return Actdef_Type;
	}

	public void setActdef_Type(String actdef_Type) {
		Actdef_Type = actdef_Type;
	}
	@Column(name = "ACTINST_ID")
	public String getActinst_Id() {
		return Actinst_Id;
	}

	public void setActinst_Id(String actinst_Id) {
		Actinst_Id = actinst_Id;
	}
	@Column(name = "ISAPPLYHANGUP")
	public Integer getIsApplyHangup() {
		return IsApplyHangup;
	}

	public void setIsApplyHangup(Integer isApplyHangup) {
		IsApplyHangup = isApplyHangup;
	}
	@Column(name = "STATUSEXT")
	public Integer getStatusext() {
		return Statusext;
	}

	public void setStatusext(Integer statusext) {
		Statusext = statusext;
	}
	@Column(name = "OPERATION_TYPE_NACT")
	public String getOperation_Type_Nact() {
		return Operation_Type_Nact;
	}

	public void setOperation_Type_Nact(String operation_Type_Nact) {
		Operation_Type_Nact = operation_Type_Nact;
	}
	@Column(name = "STAFF_ID_NACT")
	public String getStaff_Id_Nact() {
		return Staff_Id_Nact;
	}
	
	public void setStaff_Id_Nact(String staff_Id_Nact) {
		Staff_Id_Nact = staff_Id_Nact;
	}
	@Column(name = "ACTINST_STATUS")
	public Integer getActinst_Status() {
		return Actinst_Status;
	}

	public void setActinst_Status(Integer actinst_Status) {
		Actinst_Status = actinst_Status;
	}
	@Column(name = "ACTINST_WILLFINISH")
	public Date getActinst_Willfinish() {
		return Actinst_Willfinish;
	}

	public void setActinst_Willfinish(Date actinst_Willfinish) {
		Actinst_Willfinish = actinst_Willfinish;
	}
	@Column(name = "ACTINST_NAME")
	public String getActinst_Name() {
		return Actinst_Name;
	}

	public void setActinst_Name(String actinst_Name) {
		Actinst_Name = actinst_Name;
	}
	@Column(name = "MSG")
	public String getMsg() {
		return Msg;
	}

	public void setMsg(String msg) {
		Msg = msg;
	}
	@Column(name = "CODEAL")
	public Integer getCodeal() {
		return Codeal;
	}
	public void setCodeal(Integer codeal) {
		if((Integer)codeal==null){
			codeal=0;
		}
		Codeal = codeal;
	}
	@Column(name = "ACTINST_START")
	public Date getACTINST_START() {
		
		return ACTINST_START;
	}

	public void setACTINST_START(Date aCTINST_START) {
		ACTINST_START = aCTINST_START;
	}
	@Column(name = "ACTINST_END")
	public Date getACTINST_END() {
		return ACTINST_END;
	}

	public void setACTINST_END(Date aCTINST_END) {
		ACTINST_END = aCTINST_END;
	}
	
	@Column(name = "STAFF_NAME_NACT")
	public String getStaff_Name_Nact() {
		return Staff_Name_Nact;
	}

	public void setStaff_Name_Nact(String staff_Name_Nact) {
		Staff_Name_Nact = staff_Name_Nact;
	}

	@Column(name = "AREACODE")
	public String getAreaCode() {
		return AreaCode;
	}

	public void setAreaCode(String areaCode) {
		AreaCode = areaCode;
	}

	@Column(name = "FROMINLINE")
	public Integer getFromInline() {
		return FromInline;
	}

	public void setFromInline(Integer fromInline) {
		FromInline = fromInline;
	}
	@Column(name = "ISMAIL")
	public String getIsMail() {
		return IsMail;
	}

	public void setIsMail(String isMail) {
		IsMail = isMail;
	}
	@Column(name = "MAILNUMBER")
	public String getMailNumber() {
		return MailNumber;
	}

	public void setMailNumber(String mailNumber) {
		MailNumber = mailNumber;
	}

	@Column(name = "ISSFCT")
	public Integer getISSFCT() {
		return ISSFCT;
	}

	public void setISSFCT(Integer iSSFCT) {
		ISSFCT = iSSFCT;
	}
	@Column(name = "GXZLBM")
	public String getGXZLBM() {
		return GXZLBM;
	}

	public void setGXZLBM(String gXZLBM) {
		GXZLBM = gXZLBM;
	}
	@Column(name = "ISPUSHED")
	public String getIsPushed() {
		return IsPushed;
	}

	public void setIsPushed(String isPushed) {
		IsPushed = isPushed;
	}

}
