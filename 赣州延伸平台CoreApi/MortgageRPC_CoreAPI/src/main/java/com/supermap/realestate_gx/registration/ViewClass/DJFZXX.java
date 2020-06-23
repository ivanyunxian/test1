package com.supermap.realestate_gx.registration.ViewClass;

import java.util.Date;

/**
 * 
 * @author LiTong
 *
 */
public class DJFZXX  {
	private String m_zsbh;//证书编号
	
	private String m_bdcqzh;//不懂产权证号
	
	private String m_qlr;//权利人
	
	private String m_bdcdyh;//不动产单元号
	
	private String m_sffz;//是否发证
	
	private String m_lzr;//领证人
	
	private Date m_lzsj;//领证时间
	
	private String m_czlx;//操作类型
	
	private String m_xmbh;
	
	private String m_zsid;//证书ID
	
	private String cfdagh;
	
	private String zl; //不动产坐落
	
	private String csdjtzbh;//首次登记通知编号
	
	
	/** 
	 * @return cfdagh 
	 */
	public String getCfdagh() {
		return cfdagh;
	}

	/** 
	 * @param cfdagh 要设置的 cfdagh 
	 */
	public void setCfdagh(String cfdagh) {
		this.cfdagh = cfdagh;
	}

	public DJFZXX(){		
	}
	
	public String getZSID(){
		return this.m_zsid;
	}
	
	public void setZSID(String _zsid){
		this.m_zsid=_zsid;
	}
	
	public String getZSBH(){
		return this.m_zsbh;
	}
	
	public void setZSBH(String _zsbh){
		this.m_zsbh = _zsbh;
	}
	
	public String getBDCQZH(){
		return this.m_bdcqzh;
	}
	
	public void setBDCQZH(String _bdcqzh){
		this.m_bdcqzh = _bdcqzh;
	}
	
	public String getQLR(){
		return this.m_qlr;
	}
	
	public void setQLR(String _qlr){
		this.m_qlr = _qlr;
	}
	
	public String getBDCDYH(){
		return this.m_bdcdyh;
	}
	
	public void setBDCDYH(String _bdcdyh){
		this.m_bdcdyh = _bdcdyh;
	}
	
	public String getSFFZ(){
		return this.m_sffz;
	}
	
	public void setSFFZ(String _sffz){
		this.m_sffz = _sffz;
	}
	
	public String getLZR(){
		return this.m_lzr;
	}
	
	public void setLZR(String _lzr){
		this.m_lzr = _lzr;
	}
	
	public Date getLZSJ(){
		return this.m_lzsj;
	}
	
	public void setLZSJ(Date _lzsj){
		this.m_lzsj = _lzsj;
	}
	
	public String getCZLX(){
		return this.m_czlx;
	}
	
	public void setCZLX(String _czlx){
		this.m_czlx = _czlx;
	}
	
	public String getXMBH(){
		return this.m_xmbh;
	}
	
	public void setXMBH(String _xmbh){
		this.m_xmbh = _xmbh;
	}

	public String getZl() {
		return zl;
	}

	public void setZl(String zl) {
		this.zl = zl;
	}

	public String getCsdjtzbh() {
		return csdjtzbh;
	}

	public void setCsdjtzbh(String csdjtzbh) {
		this.csdjtzbh = csdjtzbh;
	}
	
	
}
