package com.supermap.realestate.registration.model.genrt;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.supermap.wisdombusiness.core.SuperModel;
import com.supermap.wisdombusiness.core.SuperSpringContext;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import com.supermap.wisdombusiness.utility.StringHelper;

public class GenerateLOG_ACCESS implements SuperModel<Integer>{

	private  CommonDao dao;
	private Integer id;
	private boolean modify_id = false;

	@SuppressWarnings("rawtypes")
	@Override
	public Integer getId() {
		if (dao == null) {
			dao = SuperSpringContext.getContext().getBean(CommonDao.class);
		}
		if (!modify_id && id == null){
			String fulSql = "SELECT LOG.LOG_ACCESS_SEQUENCE.NEXTVAL AS ID FROM DUAL";
			List<Map> list = dao.getDataListByFullSql(fulSql);
			id = StringHelper.ObjtoInt(list.get(0).get("ID"));
			modify_id = true;
		}
		return id;
	}
	
	@Override
	public void setId(Integer id) {
		if (this.id != id) {
			this.id = id;
			modify_id = true;
		}
	}
	
	private String aCCESSNAME;
	private boolean modify_aCCESSNAME = false;

	public String getACCESSNAME() {
		return aCCESSNAME;
	}

	public void setACCESSNAME(String aCCESSNAME) {
		if (this.aCCESSNAME != aCCESSNAME) {
			this.aCCESSNAME = aCCESSNAME;
			modify_aCCESSNAME = true;
		}
	}

	private String pASSWORD;
	private boolean modify_pASSWORD = false;

	public String getPASSWORD() {
		return pASSWORD;
	}

	public void setPASSWORD(String pASSWORD) {
		if (this.pASSWORD != pASSWORD) {
			this.pASSWORD = pASSWORD;
			modify_pASSWORD = true;
		}
	}

	private String aCCESSPARAM;
	private boolean modify_aCCESSPARAM = false;

	public String getACCESSPARAM() {
		return aCCESSPARAM;
	}

	public void setACCESSPARAM(String aCCESSPARAM) {
		if (this.aCCESSPARAM != aCCESSPARAM) {
			this.aCCESSPARAM = aCCESSPARAM;
			modify_aCCESSPARAM = true;
		}
	}

	private Integer uSELESSCOUNT;
	private boolean modify_uSELESSCOUNT = false;

	public Integer getUSELESSCOUNT() {
		return uSELESSCOUNT;
	}

	public void setUSELESSCOUNT(Integer uSELESSCOUNT) {
		if (this.uSELESSCOUNT != uSELESSCOUNT) {
			this.uSELESSCOUNT = uSELESSCOUNT;
			modify_uSELESSCOUNT = true;
		}
	}
	
	private String lGMACHINE;
	private boolean modify_lGMACHINE = false;

	public String getLGMACHINE() {
		return lGMACHINE;
	}

	public void setLGMACHINE(String lGMACHINE) {
		if (this.lGMACHINE != lGMACHINE) {
			this.lGMACHINE = lGMACHINE;
			modify_lGMACHINE = true;
		}
	}
	
	private String lGMAC;
	private boolean modify_lGMAC = false;

	public String getLGMAC() {
		return lGMAC;
	}
	public void setLGMAC(String lGMAC) {
		if (this.lGMAC != lGMAC) {
			this.lGMAC = lGMAC;
			modify_lGMAC = true;
		}
	}

	private Date aCCESSTIME;
	private boolean modify_aCCESSTIME = false;

	public Date getACCESSTIME() {
		return aCCESSTIME;
	}

	public void setACCESSTIME(Date aCCESSTIME) {
		if (this.aCCESSTIME != aCCESSTIME) {
			this.aCCESSTIME = aCCESSTIME;
			modify_aCCESSTIME = true;
		}
	}

	private String uSELESSCAUSE;
	private boolean modify_uSELESSCAUSE = false;

	public String getUSELESSCAUSE() {
		return uSELESSCAUSE;
	}

	public void setUSELESSCAUSE(String uSELESSCAUSE) {
		if (this.uSELESSCAUSE != uSELESSCAUSE) {
			this.uSELESSCAUSE = uSELESSCAUSE;
			modify_uSELESSCAUSE = true;
		}
	}

	private String sFCG;
	private boolean modify_sFCG = false;

	public String getSFCG() {
		return sFCG;
	}

	public void setSFCG(String sFCG) {
		if (this.sFCG != sFCG) {
			this.sFCG = sFCG;
			modify_sFCG = true;
		}
	}
	
	private Date aLLOWACCESSTIME;
	private boolean modify_aLLOWACCESSTIME = false;

	public Date getALLOWACCESSTIME() {
		return aLLOWACCESSTIME;
	}

	public void setALLOWACCESSTIME(Date aLLOWACCESSTIME) {
		if (this.aLLOWACCESSTIME != aLLOWACCESSTIME) {
			this.aLLOWACCESSTIME = aLLOWACCESSTIME;
			modify_aLLOWACCESSTIME = true;
		}
	}
	
	private String rETURNRESULT;
	private boolean modify_rETURNRESULT = false;

	public String getRETURNRESULT() {
		return rETURNRESULT;
	}

	public void setRETURNRESULT(String rETURNRESULT) {
		if (this.rETURNRESULT != rETURNRESULT) {
			this.rETURNRESULT = rETURNRESULT;
			modify_rETURNRESULT = true;
		}
	}
	
	private String yXBZ;
	private boolean modify_yXBZ = false;

	public String getYXBZ() {
		return yXBZ;
	}

	public void setYXBZ(String yXBZ) {
		if (this.yXBZ != yXBZ) {
			this.yXBZ = yXBZ;
			modify_yXBZ = true;
		}
	}

	@Override
	public void resetModifyState() {
		modify_id = false;
		modify_aCCESSNAME = false;
		modify_pASSWORD = false;
		modify_aCCESSPARAM = false;
		modify_uSELESSCOUNT = false;
		modify_lGMACHINE = false;
		modify_lGMAC = false;
		modify_aCCESSTIME = false;
		modify_uSELESSCAUSE = false;
		modify_sFCG = false;
		modify_aLLOWACCESSTIME = false;
		modify_rETURNRESULT = false;
		modify_yXBZ = false;
	}

	@Override
	@JsonIgnore
	public String[] getIgnoreProperties() {
		List<String> listStrings = new ArrayList<String>();
		if (!modify_id)
			listStrings.add("intId");
		if (!modify_aCCESSNAME)
			listStrings.add("aCCESSNAME");
		if (!modify_pASSWORD)
			listStrings.add("pASSWORD");
		if (!modify_aCCESSPARAM)
			listStrings.add("aCCESSPARAM");
		if (!modify_uSELESSCOUNT)
			listStrings.add("uSELESSCOUNT");
		if (!modify_lGMACHINE)
			listStrings.add("lGMACHINE");
		if (!modify_lGMAC)
			listStrings.add("lGMAC");
		if (!modify_aCCESSTIME)
			listStrings.add("aCCESSTIME");
		if (!modify_uSELESSCAUSE)
			listStrings.add("uSELESSCAUSE");
		if (!modify_sFCG)
			listStrings.add("sFCG");
		if (!modify_aLLOWACCESSTIME)
			listStrings.add("aLLOWACCESSTIME");
		if (!modify_rETURNRESULT)
			listStrings.add("rETURNRESULT");
		if (!modify_yXBZ)
			listStrings.add("yXBZ");

		return StringHelper.ListToStrings(listStrings);
	}
}
