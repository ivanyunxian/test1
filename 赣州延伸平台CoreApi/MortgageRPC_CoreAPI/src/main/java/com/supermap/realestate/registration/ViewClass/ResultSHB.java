package com.supermap.realestate.registration.ViewClass;


/**
 * 
 * @Description：审核表
 * @author mss
 * @date
 * @Copyright SuperMap
 */

public class ResultSHB {
	private boolean sucesses = false;

	public boolean isSucesses() {
		return sucesses;
	}

	public void setSucesses(boolean sucesses) {
		this.sucesses = sucesses;
	}

	public SHB getShb() {
		return shb;
	}

	public void setShb(SHB shb) {
		this.shb = shb;
	}

	private SHB shb = null;
}