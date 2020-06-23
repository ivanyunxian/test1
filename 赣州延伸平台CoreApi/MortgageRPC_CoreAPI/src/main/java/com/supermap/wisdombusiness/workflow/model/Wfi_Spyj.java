
/**
 * 
 * 代码生成器自动生成[WFI_SPYJ]
 * 
 */

package com.supermap.wisdombusiness.workflow.model;

import java.util.UUID;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "WFI_SPYJ",schema = "BDC_WORKFLOW")
public class Wfi_Spyj {

	private String Spyj_Id;
	private String Spdy_Id;
	private String Actinst_Id;
	private String Splx;
	private String Spyj;
	private String Spr_Name;
	private String Spr_Id;
	private byte[] Sign;
	private Date Spsj;
	private Integer Status;
	private String Proinst_Id;
	private String SIGNFLAG;
	private String SIGNID;
	private String SIGNJG;
	//增加签名日期
	private Date SIGNDATE;
	
	private String Spid_Bath;
	private String BDCDYH;

	@Column(name = "BDCDYH",length = 30)
	public String getBDCDYH() {
		return BDCDYH;
	}

	public void setBDCDYH(String bDCDYH) {
		BDCDYH = bDCDYH;
	}

	@Column(name = "SIGNDATE",length = 7)
	public Date getSIGNDATE() {
		return SIGNDATE;
	}

	public void setSIGNDATE(Date sIGNDATE) {
		SIGNDATE = sIGNDATE;
	}
	
	@Id
	@Column(name = "SPYJ_ID", length = 400)
	public String getSpyj_Id() {
		if (Spyj_Id == null)
			Spyj_Id = UUID.randomUUID().toString().replace("-", "");
		return Spyj_Id;
	}

	public void setSpyj_Id(String Spyj_Id) {
		this.Spyj_Id = Spyj_Id;
	}

	@Column(name = "SPDY_ID", length = 200)
	public String getSpdy_Id() {
		return Spdy_Id;
	}

	public void setSpdy_Id(String Spdy_Id) {
		this.Spdy_Id = Spdy_Id;
	}

	@Column(name = "ACTINST_ID", length = 400)
	public String getActinst_Id() {
		return Actinst_Id;
	}

	public void setActinst_Id(String Actinst_Id) {
		this.Actinst_Id = Actinst_Id;
	}

	@Column(name = "SPLX", length = 400)
	public String getSplx() {
		return Splx;
	}

	public void setSplx(String Splx) {
		this.Splx = Splx;
	}

	@Column(name = "SPYJ", length = 2000)
	public String getSpyj() {
		return Spyj;
	}

	public void setSpyj(String Spyj) {
		this.Spyj = Spyj;
	}

	@Column(name = "SPR_NAME", length = 200)
	public String getSpr_Name() {
		return Spr_Name;
	}

	public void setSpr_Name(String Spr_Name) {
		this.Spr_Name = Spr_Name;
	}

	@Column(name = "SPR_ID", length = 200)
	public String getSpr_Id() {
		return Spr_Id;
	}

	public void setSpr_Id(String Spr_Id) {
		this.Spr_Id = Spr_Id;
	}

	@Column(name = "SIGN")
	public byte[] getSign() {
		return Sign;
	}

	public void setSign(byte[] Sign) {
		this.Sign = Sign;
	}

	@Column(name = "SPSJ", length = 7)
	public Date getSpsj() {
		return Spsj;
	}

	public void setSpsj(Date Spsj) {
		this.Spsj = Spsj;
	}

	@Column(name = "STATUS")
	public Integer getStatus() {
		return Status;
	}

	public void setStatus(Integer Status) {
		this.Status = Status;
	}

	/**
	 * @return the proinst_Id
	 */
	@Column(name = "PROINST_ID", length = 400)
	public String getProinst_Id() {
		return Proinst_Id;
	}

	/**
	 * @param proinst_Id the proinst_Id to set
	 */
	public void setProinst_Id(String proinst_Id) {
		Proinst_Id = proinst_Id;
	}
	@Column(name = "SIGNFLAG", length = 2000)
	public String getSIGNFLAG() {
		return SIGNFLAG;
	}

	public void setSIGNFLAG(String sIGNFLAG) {
		SIGNFLAG = sIGNFLAG;
	}
	@Column(name = "SIGNID", length = 100)
	public String getSIGNID() {
		return SIGNID;
	}

	public void setSIGNID(String sIGNID) {
		SIGNID = sIGNID;
	}
	@Column(name = "SIGNJG",length = 5000)
	public String getSIGNJG() {
		return SIGNJG;
	}

	public void setSIGNJG(String sIGNJG) {
		SIGNJG = sIGNJG;
	}
	
	@Column(name = "SPID_BATCH", length = 200)
	public String getSpid_Bath() {
		return Spid_Bath;
	}

	public void setSpid_Bath(String spid_Bath) {
		Spid_Bath = spid_Bath;
	}

}
