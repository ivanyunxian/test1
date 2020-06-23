package com.supermap.realestate.registration.ViewClass;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.supermap.realestate.registration.util.FeatureHelper.POINT2D;


/**
 * 不动产登记受理单/询问笔录/收费明细单信息
 * 
 * @author 俞学斌
 *
 */
public class UnitInfo {
	private int level;
	private List<String> prebdcdyids;
	private List<String> sufbdcdyids;
	private String bdcdyid;
	private String bdcdylx;
	private String bdcdyh;
	private String mj;
	private String zl;
	private int subdygs=1;
	private int predygs=1;
    public int getPredygs() {
		return predygs;
	}
	public void setPredygs(int predygs) {
		if(predygs<1){
			this.predygs=1;
		}else{
			this.predygs = predygs;	
		}	
	}
	public int getSubdygs() {
		return subdygs;
	}
	public void setSubdygs(int subdygs) {
		if(subdygs<1){
			this.subdygs=1;
		}else{
			this.subdygs = subdygs;	
		}		
	}
	public String getZdbdcdyid() {
		return zdbdcdyid;
	}
	public void setZdbdcdyid(String zdbdcdyid) {
		this.zdbdcdyid = zdbdcdyid;
	}
	private String zdbdcdyid;
	List<List<POINT2D>> geopoints = new ArrayList<List<POINT2D>>();
	public List<List<POINT2D>> getGeopoints() {
		return geopoints;
	}
	public void setGeopoints(List<List<POINT2D>> geopoints) {
		this.geopoints = geopoints;
	}
	//单元上权利节点
	private List<List<HashMap<String,String>>> lstrights=new ArrayList<List<HashMap<String,String>>>();
	public List<List<HashMap<String, String>>> getLstrights() {
		return lstrights;
	}
	public void setLstrights(List<List<HashMap<String, String>>> lstrights) {
		this.lstrights = lstrights;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public List<String> getPrebdcdyids() {
		return prebdcdyids;
	}
	public void setPrebdcdyids(List<String> prebdcdyids) {
		this.prebdcdyids = prebdcdyids;
	}
	public List<String> getSufbdcdyids() {
		return sufbdcdyids;
	}
	public void setSufbdcdyids(List<String> sufbdcdyids) {
		this.sufbdcdyids = sufbdcdyids;
	}
	public String getBdcdyid() {
		return bdcdyid;
	}
	public void setBdcdyid(String bdcdyid) {
		this.bdcdyid = bdcdyid;
	}
	public String getBdcdylx() {
		return bdcdylx;
	}
	public void setBdcdylx(String bdcdylx) {
		this.bdcdylx = bdcdylx;
	}
	public String getBdcdyh() {
		return bdcdyh;
	}
	public void setBdcdyh(String bdcdyh) {
		this.bdcdyh = bdcdyh;
	}
	public String getMj() {
		return mj;
	}
	public void setMj(String mj) {
		this.mj = mj;
	}
	public String getZl() {
		return zl;
	}
	public void setZl(String zl) {
		this.zl = zl;
	}
}
