package com.supermap.realestate.registration.model.genrt;

import java.util.ArrayList;
import java.util.List;

import com.supermap.wisdombusiness.core.SuperHelper;
import com.supermap.wisdombusiness.core.SuperModel;
import com.supermap.wisdombusiness.utility.StringHelper;

public class GenerateBDCS_QYXX implements SuperModel<String>{

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
	
	private String qYMC;
	private boolean modify_qYMC = false;
	
	public String getQYMC(){
		return qYMC;
	}
	
	public void setQYMC(String qYMC){
		if(this.qYMC != qYMC){
			this.qYMC = qYMC;
			modify_qYMC = true;
		}
	}
	
	private String tYSHXYDM;
	private boolean modify_tYSHXYDM = false;
	
	public String getTYSHXYDM(){
		return tYSHXYDM;
	}
	
	public void setTYSHXYDM(String tYSHXYDM){
		if(this.tYSHXYDM != tYSHXYDM){
			this.tYSHXYDM = tYSHXYDM;
			modify_tYSHXYDM = true;
		}
	}
	
	private String qYDZ;
	private boolean modify_qYDZ = false;
	
	public String getQYDZ(){
		return qYDZ;
	}
	
	public void setQYDZ(String qYDZ){
		if(this.qYDZ!= qYDZ){
			this.qYDZ = qYDZ;
			modify_qYDZ = true;
		}
	}
	
	private String fDDBR;
	private boolean modify_fDDBR = false;
	
	public String getFDDBR(){
		return fDDBR;
	}
	
	public void setFDDBR(String fDDBR){
		if(this.fDDBR != fDDBR){
			this.fDDBR = fDDBR;
			modify_fDDBR = true;
		}
	}
	
	private String fDDBRZJH;
	private boolean modify_fDDBRZJH = false;
	
	public String getFDDBRZJH(){
		return fDDBRZJH;
	}
	
	public void setFDDBRZJH(String fDDBRZJH) {
		if(this.fDDBRZJH != fDDBRZJH){
			this.fDDBRZJH = fDDBRZJH;
			modify_fDDBRZJH = true;
		}
		
	}
	
	private String zCZXM;
	private boolean modify_zCZXM = false;
	
	public String getZCZXM(){
		return zCZXM;
	}
	
	public void setZCZXM(String zCZXM) {
		if(this.zCZXM != zCZXM){
			this.zCZXM = zCZXM;
			modify_zCZXM = true;
		}
	}
	
	private String zCZZJH;
	private boolean modify_zCZZJH = false;
	
	public String getZCZZJH() {
		return zCZZJH;
	}
	
	public void setZCZZJH(String zCZZJH) {
		if(this.zCZZJH != zCZZJH){
			this.zCZZJH = zCZZJH;
			modify_zCZZJH = true;
		}
	}
	
	private String zCZSJH;
	private boolean modify_zCZSJH = false;
	
	public String getZCZSJH(){
		return zCZSJH;
	}
	
	public void setZCZSJH(String zCZSJH){
		if(this.zCZSJH != zCZSJH){
			this.zCZSJH = zCZSJH;
			modify_zCZSJH = true;
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
		modify_pROJECT_ID = false;
		modify_qYMC = false;
		modify_tYSHXYDM = false;
		modify_qYDZ = false;
		modify_fDDBR = false;
		modify_fDDBRZJH = false;
		modify_zCZXM = false;
		modify_zCZZJH = false;
		modify_zCZSJH = false;
		modify_sHZT = false;
	}

	@Override
	public String[] getIgnoreProperties() {
		List<String> listStrings = new ArrayList<String>();
		if(!modify_id)
			listStrings.add("id");
		if(!modify_pROJECT_ID)
			listStrings.add("pROJECT_ID");
		if(!modify_qYMC)
			listStrings.add("qYMC");
		if(!modify_tYSHXYDM)
			listStrings.add("tYSHXYDM");
		if(!modify_qYDZ)
			listStrings.add("qYDZ");
		if(!modify_fDDBR)
			listStrings.add("fDDBR");
		if(!modify_fDDBRZJH)
			listStrings.add("fDDBRZJH");
		if(!modify_zCZXM)
			listStrings.add("zCZXM");
		if(!modify_zCZZJH)
			listStrings.add("zCZZJH");
		if(!modify_zCZSJH)
			listStrings.add("zCZSJH");
		if(!modify_sHZT)
			listStrings.add("sHZT");
		
		return StringHelper.ListToStrings(listStrings);
	}

}
