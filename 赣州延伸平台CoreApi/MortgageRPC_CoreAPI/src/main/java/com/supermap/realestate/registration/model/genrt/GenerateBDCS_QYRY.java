package com.supermap.realestate.registration.model.genrt;

import java.util.ArrayList;
import java.util.List;

import com.sun.xml.internal.ws.message.StringHeader;
import com.supermap.wisdombusiness.core.SuperHelper;
import com.supermap.wisdombusiness.core.SuperModel;
import com.supermap.wisdombusiness.utility.StringHelper;

public class GenerateBDCS_QYRY implements SuperModel<String>{

	private String id;
	private boolean modify_id = false;
	
	@Override
	public String getId() {
		if (!modify_id && id == null)
		{
			id = SuperHelper.GeneratePrimaryKey();
			modify_id = true;
		}
		return id;
	}

	@Override
	public void setId(String id) {
		if (this.id != id) {
			this.id = id;
			modify_id = true;
		}
		
	}
	
	private String qYID;
	private boolean modify_qYID = false;
	
	public String getQYID(){
		return qYID;
	}
	
	public void setQYID(String qYID){
		if(this.qYID != qYID){
			this.qYID = qYID;
			modify_qYID = true;
		}
	}
	
	private String rYMC;
	private boolean modify_rYMC = false;
	
	public String getRYMC(){
		return rYMC;
	}
	
	public void setRYMC(String rYMC){
		if(this.rYMC != rYMC){
			this.rYMC = rYMC;
			modify_rYMC = true;
		}
	}
	
	private String lXDH;
	private boolean modify_lXDH = false;
	
	public String getLXDH(){
		return lXDH;
	}
	
	public void setLXDH(String lXDH){
		if(this.lXDH != lXDH){
			this.lXDH = lXDH;
			modify_lXDH = true;
		}
	}
	
	private String tXDZ;
	private boolean modify_tXDZ = false;
	
	public String getTXDZ(){
		return tXDZ;
	}
	
	public void setTXDZ(String tXDZ){
		if(this.tXDZ != tXDZ){
			this.tXDZ = tXDZ;
			modify_tXDZ = true;
		}
	}
	
	private String xB;
	private boolean modify_xB = false;
	
	public String getXB(){
		return xB;
	}
	
	public void setXB(String xB){
		if(this.xB != xB){
			this.xB = xB;
			modify_xB = true;
		}
	}
	
	private String zJH;
	private boolean modify_zJH = false;
	
	public String getZJH(){
		return zJH;
	}
	
	public void setZJH(String zJH){
		if(this.zJH != zJH){
			this.zJH = zJH;
			modify_zJH = true;
		}
	}
	
	private String zJLX;
	private boolean modify_zJLX = false;
	
	public String getZJLX(){
		return zJLX;
	}
	
	public void setZJLX(String zJLX){
		if(this.zJLX != zJLX){
			this.zJLX = zJLX;
			modify_zJLX = true;
		}
	}
	
	private String lOGINNAME;
	private boolean modify_lOGINNAME = false;
	
	public String getLOGINNAME(){
		return lOGINNAME;
	}
	
	public void setLOGINNAME(String lOGINNAME){
		if(this.lOGINNAME!=lOGINNAME){
			this.lOGINNAME = lOGINNAME;
			modify_lOGINNAME = true;
		}
	}
	
	private String pROJECT_ID;
	private boolean modify_pROJECT_ID = false;
	
	public String getPROJECT_ID(){
		return pROJECT_ID;
	}
	
	public void setPROJECT_ID(String pROJECT_ID){
		if(this.pROJECT_ID != pROJECT_ID){
			this.pROJECT_ID = pROJECT_ID;
			modify_pROJECT_ID = true;
		}
	}
	
	private String sHZT;
	private boolean modify_sHZT = false;
	
	public String getSHZT(){
		return sHZT;
	}
	
	public void setSHZT(String sHZT){
		if(this.sHZT!=sHZT){
			this.sHZT = sHZT;
		}
	}

	@Override
	public void resetModifyState() {
		modify_id = false;
		modify_qYID = false;
		modify_rYMC = false;
		modify_lXDH = false;
		modify_tXDZ = false;
		modify_xB = false;
		modify_zJH = false;
		modify_zJLX = false;
		modify_lOGINNAME = false;
		modify_pROJECT_ID = false;
		modify_sHZT = false;
	}

	@Override
	public String[] getIgnoreProperties() {
		List<String> listStrings = new ArrayList<String>();
		if(!modify_id)
			listStrings.add("id");
		if(!modify_qYID)
			listStrings.add("qYID");
		if(!modify_rYMC)
			listStrings.add("rYMC");
		if(!modify_lXDH)
			listStrings.add("lXDH");
		if(!modify_tXDZ)
			listStrings.add("tXDZ");
		if(!modify_xB)
			listStrings.add("xB");
		if(!modify_zJH)
			listStrings.add("zJH");
		if(!modify_zJLX)
			listStrings.add("zJLX");
		if(!modify_lOGINNAME)
			listStrings.add("lOGINNAME");
		if(!modify_pROJECT_ID)
			listStrings.add("pROJECT_ID");
		if(!modify_sHZT)
			listStrings.add("sHZT");
		return StringHelper.ListToStrings(listStrings);
	}

	
}
