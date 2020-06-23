package com.supermap.realestate.registration.model.genrt;

import java.util.ArrayList;
import java.util.List;

import com.supermap.wisdombusiness.core.SuperHelper;
import com.supermap.wisdombusiness.core.SuperModel;
import com.supermap.wisdombusiness.utility.StringHelper;

public class GenerateBDCS_QYDYGX implements SuperModel<String>{

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
	
	private String bDCDYH;
	private boolean modify_bDCDYH = false;
	
	public String getBDCDYH(){
		return bDCDYH;
	}
	
	public void setBDCDYH(String bDCDYH){
		if(this.bDCDYH != bDCDYH){
			this.bDCDYH = bDCDYH;
			modify_bDCDYH = true;
		}
	}
	
	private String wZDID;
	private boolean modify_wZDID = false;
	
	public String getWZDID(){
		return wZDID;
	}
	
	public void setWZDID(String wZDID){
		if(this.wZDID != wZDID){
			this.wZDID = wZDID;
			modify_wZDID = true;
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
		modify_bDCDYH = false;
		modify_wZDID = true;
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
		if(!modify_bDCDYH)
			listStrings.add("bDCDYH");
		if(!modify_wZDID)
			listStrings.add("wZDID");
		if(!modify_pROJECT_ID)
			listStrings.add("pROJECT_ID");
		if(!modify_sHZT)
			listStrings.add("sHZT");
		return StringHelper.ListToStrings(listStrings);
	}
	

}
