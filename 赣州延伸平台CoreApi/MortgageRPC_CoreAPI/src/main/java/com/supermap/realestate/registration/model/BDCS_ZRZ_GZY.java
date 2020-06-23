package com.supermap.realestate.registration.model;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.supermap.realestate.registration.model.genrt.GenerateBDCS_ZRZ_GZY;
import com.supermap.realestate.registration.model.interfaces.Building;
import com.supermap.realestate.registration.util.ConstHelper;
import com.supermap.realestate.registration.util.ConstValue.BDCDYLX;
import com.supermap.realestate.registration.util.ConstValue.DJDYLY;

@Entity
@Table(name = "bdcs_zrz_gzy", schema = "bdck")
public class BDCS_ZRZ_GZY extends GenerateBDCS_ZRZ_GZY  implements Building{

	@Override
	@Id
	@Column(name = "bdcdyid", length = 50)
	public String getId() {
		return super.getId();
	}

	@Override
	@Column(name = "ysdm")
	public String getYSDM() {
		return super.getYSDM();
	}

	@Override
	@Column(name = "xmmc")
	public String getXMMC() {
		return super.getXMMC();
	}

	@Override
	@Column(name = "bdcdyh")
	public String getBDCDYH() {
		return super.getBDCDYH();
	}

	@Override
	@Column(name = "xmbh")
	public String getXMBH() {
		return super.getXMBH();
	}

	@Override
	@Column(name = "zddm")
	public String getZDDM() {
		return super.getZDDM();
	}

	@Override
	@Column(name = "zdbdcdyid")
	public String getZDBDCDYID() {
		return super.getZDBDCDYID();
	}

	@Override
	@Column(name = "zrzh")
	public String getZRZH() {
		return super.getZRZH();
	}

	@Override
	@Column(name = "zl")
	public String getZL() {
		return super.getZL();
	}

	@Override
	@Column(name = "jzwmc")
	public String getJZWMC() {
		return super.getJZWMC();
	}

	@Override
	@Column(name = "jgrq")
	public Date getJGRQ() {
		return super.getJGRQ();
	}

	@Override
	@Column(name = "jzwgd")
	public Double getJZWGD() {
		return super.getJZWGD();
	}

	@Override
	@Column(name = "zzdmj")
	public Double getZZDMJ() {
		return super.getZZDMJ();
	}

	@Override
	@Column(name = "zydmj")
	public Double getZYDMJ() {
		return super.getZYDMJ();
	}

	@Override
	@Column(name = "ycjzmj")
	public Double getYCJZMJ() {
		return super.getYCJZMJ();
	}

	@Override
	@Column(name = "scjzmj")
	public Double getSCJZMJ() {
		return super.getSCJZMJ();
	}

	@Override
	@Column(name = "tdsyqr")
	public String getTDSYQR() {
		return super.getTDSYQR();
	}

	@Override
	@Column(name = "dytdmj")
	public Double getDYTDMJ() {
		return super.getDYTDMJ();
	}

	@Override
	@Column(name = "fttdmj")
	public Double getFTTDMJ() {
		return super.getFTTDMJ();
	}

	@Override
	@Column(name = "fdcjyjg")
	public Double getFDCJYJG() {
		return super.getFDCJYJG();
	}

	@Override
	@Column(name = "zcs")
	public Integer getZCS() {
		return super.getZCS();
	}

	@Override
	@Column(name = "dscs")
	public Integer getDSCS() {
		return super.getDSCS();
	}

	@Override
	@Column(name = "dxcs")
	public Integer getDXCS() {
		return super.getDXCS();
	}

	@Override
	@Column(name = "dxsd")
	public Double getDXSD() {
		return super.getDXSD();
	}

	@Override
	@Column(name = "ghyt")
	public String getGHYT() {
		return super.getGHYT();
	}

    private String ghytname;
	@Transient
	public String getGHYTName() {
		if (ghytname == null) {
			if (this.getGHYT() != null) {
				ghytname = ConstHelper.getNameByValue("FWYT", this.getGHYT());
			}
		}
		return ghytname;
	}

	@Override
	@Column(name = "fwjg")
	public String getFWJG() {
		return super.getFWJG();
	}

    private String fwjgname;
	@Transient
	public String getFWJGName() {
		if (fwjgname == null) {
			if (this.getFWJG() != null) {
				fwjgname = ConstHelper.getNameByValue("FWJG", this.getFWJG());
			}
		}
		return fwjgname;
	}

	@Override
	@Column(name = "zts")
	public Integer getZTS() {
		return super.getZTS();
	}

	@Override
	@Column(name = "jzwjbyt")
	public String getJZWJBYT() {
		return super.getJZWJBYT();
	}

	@Override
	@Column(name = "bz")
	public String getBZ() {
		return super.getBZ();
	}

	@Override
	@Column(name = "zt")
	public String getZT() {
		return super.getZT();
	}

	@Override
	@Column(name = "qxdm")
	public String getQXDM() {
		return super.getQXDM();
	}

	@Override
	@Column(name = "qxmc")
	public String getQXMC() {
		return super.getQXMC();
	}

	@Override
	@Column(name = "djqdm")
	public String getDJQDM() {
		return super.getDJQDM();
	}

	@Override
	@Column(name = "djqmc")
	public String getDJQMC() {
		return super.getDJQMC();
	}

	@Override
	@Column(name = "djzqdm")
	public String getDJZQDM() {
		return super.getDJZQDM();
	}

	@Override
	@Column(name = "djzqmc")
	public String getDJZQMC() {
		return super.getDJZQMC();
	}

	@Override
	@Column(name = "dcxmid")
	public String getDCXMID() {
		return super.getDCXMID();
	}

	@Override
	@Column(name = "yxbz")
	public String getYXBZ() {
		return super.getYXBZ();
	}


	@Override
	@Column(name = "djzt")
	public String getDJZT() {
		return super.getDJZT();
	}

    private String djztname;
	@Transient
	public String getDJZTName() {
		if (djztname == null) {
			if (this.getDJZT() != null) {
				djztname = ConstHelper.getNameByValue("DJZT", this.getDJZT());
			}
		}
		return djztname;
	}

	@Override
	@Column(name = "createtime")
	public Date getCREATETIME() {
		return super.getCREATETIME();
	}

	@Override
	@Column(name = "modifytime")
	public Date getMODIFYTIME() {
		return super.getMODIFYTIME();
	}

	/*@Override
	@Column(name = "ycbdcdyid")
	public String getYCBDCDYID() {
		return super.getYCBDCDYID();
	}*/

	@Override
	@Column(name = "relationid")
	public String getRELATIONID() {
		return super.getRELATIONID();
	}

	@Override
	@Column(name = "zrzzdt")
	public String getZRZZDT() {
		return super.getZRZZDT();
	}

	@Override
	@Column(name = "zzsynx")
	public Integer getZZSYNX() {
		return super.getZZSYNX();
	}

	@Override
	@Column(name = "zzsyqqsrq")
	public Date getZZSYQQSRQ() {
		return super.getZZSYQQSRQ();
	}

	@Override
	@Column(name = "zzsyqzzrq")
	public Date getZZSYQZZRQ() {
		return super.getZZSYQZZRQ();
	}

	@Override
	@Column(name = "zzhbzcrqsrq")
	public Date getZZHBZCRQSRQ() {
		return super.getZZHBZCRQSRQ();
	}

	@Override
	@Column(name = "zzhbzcrsynx")
	public Integer getZZHBZCRSYNX() {
		return super.getZZHBZCRSYNX();
	}

	@Override
	@Column(name = "zzhbzcrzzrq")
	public Date getZZHBZCRZZRQ() {
		return super.getZZHBZCRZZRQ();
	}

	@Override
	@Column(name = "zzycnxqsrq")
	public Date getZZYCNXQSRQ() {
		return super.getZZYCNXQSRQ();
	}

	@Override
	@Column(name = "zzycnxsynx")
	public Integer getZZYCNXSYNX() {
		return super.getZZYCNXSYNX();
	}

	@Override
	@Column(name = "zzycnxzzrq")
	public Date getZZYCNXZZRQ() {
		return super.getZZYCNXZZRQ();
	}

	@Override
	@Column(name = "zztddj")
	public String getZZTDDJ() {
		return super.getZZTDDJ();
	}

	@Override
	@Column(name = "fzzsyqqsrq")
	public Date getFZZSYQQSRQ() {
		return super.getFZZSYQQSRQ();
	}

	@Override
	@Column(name = "fzzsynx")
	public Integer getFZZSYNX() {
		return super.getFZZSYNX();
	}

	@Override
	@Column(name = "fzzsyqzzrq")
	public Date getFZZSYQZZRQ() {
		return super.getFZZSYQZZRQ();
	}

	@Override
	@Column(name = "fzzhbzcrqsrq")
	public Date getFZZHBZCRQSRQ() {
		return super.getFZZHBZCRQSRQ();
	}

	@Override
	@Column(name = "fzzhbzcrsynx")
	public Integer getFZZHBZCRSYNX() {
		return super.getFZZHBZCRSYNX();
	}

	@Override
	@Column(name = "fzzhbzcrzzrq")
	public Date getFZZHBZCRZZRQ() {
		return super.getFZZHBZCRZZRQ();
	}

	@Override
	@Column(name = "fzzycnxqsrq")
	public Date getFZZYCNXQSRQ() {
		return super.getFZZYCNXQSRQ();
	}

	@Override
	@Column(name = "fzzycnxsynx")
	public Integer getFZZYCNXSYNX() {
		return super.getFZZYCNXSYNX();
	}

	@Override
	@Column(name = "fzzycnxzzrq")
	public Date getFZZYCNXZZRQ() {
		return super.getFZZYCNXZZRQ();
	}

	@Override
	@Column(name = "fzztddj")
	public String getFZZTDDJ() {
		return super.getFZZTDDJ();
	}
	@Lob
	@Override
	@Basic(fetch=FetchType.LAZY) 
	@Column(name = "fcfht",columnDefinition="BLOB", nullable=true)
	public byte[] getFCFHT() {
		return super.getFCFHT();
	}
	
	@Override
	@Column(name = "nbdcdyh")
	public String getNBDCDYH() {
		return super.getNBDCDYH();
	}
	
	@Override
	@Column(name = "nzdbdcdyid")
	public String getNZDBDCDYID() {
		return super.getNZDBDCDYID();
	}
	
	@Override
	@Column(name = "kfsmc")
	public String getKFSMC() {
		return super.getKFSMC();
	}
	
	@Override
	@Column(name = "kfszjh")
	public String getKFSZJH() {
		return super.getKFSZJH();
	}
	/******************* 自定义部分 ****************/
	@Transient
	public BDCDYLX getBDCDYLX() {
		return BDCDYLX.ZRZ;
	}

	@Transient
	public DJDYLY getLY() {
		return DJDYLY.GZ;
	}
	
	@Transient
	public double getMJ() {
		double d = 0;
		if (super.getSCJZMJ() != null)
			d = super.getSCJZMJ();
		return d;
	}

	@Override
	public void setYT(String yt) {
		// TODO Auto-generated method stub
		
	}
	@Override
	@Column(name = "txwhtype")
	public String getTXWHTYPE() {
		return super.getTXWHTYPE();
	}
}

