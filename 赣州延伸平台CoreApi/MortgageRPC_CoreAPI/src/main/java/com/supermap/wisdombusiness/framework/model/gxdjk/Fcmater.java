package com.supermap.wisdombusiness.framework.model.gxdjk;

public class Fcmater {
	private String IMGNO;
	private String MATERIAL_NAME;
	private String ywbh;
	public String getIMGNO() {
		return IMGNO;
	}
	public void setIMGNO(String iMGNO) {
		IMGNO = iMGNO;
	}
	public String getMATERIAL_NAME() {
		return MATERIAL_NAME;
	}
	public void setMATERIAL_NAME(String mATERIAL_NAME) {
		MATERIAL_NAME = mATERIAL_NAME;
	}
	public String getYwbh() {
		return ywbh;
	}
	public void setYwbh(String ywbh) {
		this.ywbh = ywbh;
	}
	@Override
	public String toString() {
		return "Fcmater [IMGNO=" + IMGNO + ", MATERIAL_NAME=" + MATERIAL_NAME
				+ ", ywbh=" + ywbh + "]";
	}
	
}
