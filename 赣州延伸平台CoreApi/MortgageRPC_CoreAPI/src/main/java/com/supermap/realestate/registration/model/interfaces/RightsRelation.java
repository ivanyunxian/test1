/**   
* 权地证人接口文件
* @Title: RightsRelation.java 
* @Package com.supermap.realestate.registration.model.interfaces 
* @author liushufeng 
* @date 2016年3月2日 下午5:23:16 
* @version V1.0   
*/

package com.supermap.realestate.registration.model.interfaces;

/** 
 * 权地证人
 * @ClassName: RightsRelation 
 * @author liushufeng 
 * @date 2016年3月2日 下午5:23:16  
 */
public interface RightsRelation {
	public abstract String getId();

	public abstract void setId(String id);

	public abstract String getXMBH();

	public abstract void setXMBH(String xMBH);

	public abstract String getQLID();

	public abstract void setQLID(String qLID);

	public abstract String getQLRID();

	public abstract void setQLRID(String qLRID);

	public abstract String getZSID();

	public abstract void setZSID(String zSID);

	public abstract String getFSQLID();

	public abstract void setFSQLID(String fSQLID);
}
