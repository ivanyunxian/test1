/**   
 * 房屋(户)不动产单元接口
 * @Title: House.java 
 * @Package com.supermap.realestate.registration.model.interfaces.newer 
 * @author liushufeng 
 * @date 2015年7月11日 下午4:43:44 
 * @version V1.0   
 */

package com.supermap.realestate.registration.model.interfaces;


/**
 * 房屋(户)不动产单元接口
 * @ClassName: House
 * @author liushufeng
 * @date 2015年7月11日 下午4:43:44
 */
public interface LandAttach extends RealUnit {

	public abstract String getId();

	public abstract void setId(String id);

	public abstract String getYSDM();

	public abstract void setYSDM(String ySDM);

	public abstract String getBDCDYH();

	public abstract void setBDCDYH(String bDCDYH);

	public abstract String getXMBH();

	public abstract void setXMBH(String xMBH);

	public abstract String getZDBDCDYID();
	
	public abstract void setZDBDCDYID(String zdbdcdyid);

}
