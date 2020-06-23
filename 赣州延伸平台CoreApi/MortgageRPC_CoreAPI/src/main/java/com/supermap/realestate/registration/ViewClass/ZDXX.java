package com.supermap.realestate.registration.ViewClass;

import com.supermap.wisdombusiness.core.SuperModel;

/**
 * wuz
 *
 */
public class ZDXX  implements SuperModel<String> {
	
	private String xZL;//坐落
	
	private String xZDMJ;//宗地面积
	
	private String xQLRs;//权利人名称
	
	private String xQLRIDs;//权利人IDS
	
	private String xBDCDYH;//不动产单元号
	
	private String xBDCQZH;//不动产权证号
	
    private String xBDCDYID;//不动产单元ID
	 
	 /**
	  * 坐落
	  * @return
	  */
	 public String getZL() {
	 	return xZL;
	 }
	 public void setZL(String value) {
		 xZL = value;
	 }
	 
	 /**
	  * 宗地面积
	  * @return
	  */
	 public String getZDMJ() {
	 	return xZDMJ;
	 }
	 public void setZDMJ(String value) {
		 xZDMJ = value;
	 }
	 
	 /**
	  * 权利人名称
	  * @return
	  */
	 public String getQLRs() {
	 	return xQLRs;
	 }
	 public void setQLRs(String value) {
		 xQLRs = value;
	 }
	 
	 /**
	  * 权利人IDS
	  * @return
	  */
	 public String getQLRIDs() {
	 	return xQLRIDs;
	 }
	 public void setQLRIDs(String value) {
		 xQLRIDs = value;
	 }
	 
	 /**
	  * 不动产单元号
	  * @return
	  */
	 public String BDCDYH() {
	 	return xBDCDYH;
	 }
	 public void setBDCDYH(String value) {
		 xBDCDYH = value;
	 }
	 
	 /**
	  * 不动产权证号
	  * @return
	  */
	 public String getBDCQZH() {
	 	return xBDCQZH;
	 }
	 public void setBDCQZH(String value) {
		 xBDCQZH = value;
	 }
	 
	 /**
	  * 不动产单元ID
	  * @return
	  */
	 public String getBDCDYID() {
	 	return xBDCDYID;
	 }
	 public void setBDCDYID(String value) {
		 xBDCDYID = value;
	 }
	 
	@Override
	public String getId() {
		return null;
	}
	
	@Override
	public void setId(String id) {
		
	}
	
	@Override
	public void resetModifyState() {
		
	}
	
	@Override
	public String[] getIgnoreProperties() {
		return null;
	}


}
