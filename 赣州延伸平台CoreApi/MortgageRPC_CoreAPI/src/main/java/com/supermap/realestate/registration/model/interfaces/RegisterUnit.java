/**   
 * 登记单元接口
 * @Title: RegisterUnit.java 
 * @Package com.supermap.realestate.registration.model.interfaces 
 * @author liushufeng 
 * @date 2015年7月11日 下午10:30:45 
 * @version V1.0   
 */

package com.supermap.realestate.registration.model.interfaces;

import java.util.Date;

/**
 * 登记单元接口
 * @ClassName: RegisterUnit
 * @author liushufeng
 * @date 2015年7月11日 下午10:30:45
 */
public interface RegisterUnit {
	public abstract String getId();

	public abstract void setId(String id);

	public abstract String getDJDYID();

	public abstract void setDJDYID(String dJDYID);

	public abstract String getXMBH();

	public abstract void setXMBH(String xMBH);

	public abstract String getBDCDYH();

	public abstract void setBDCDYH(String bDCDYH);

	public abstract String getBDCDYLX();

	public abstract void setBDCDYLX(String bDCDYLX);

	public abstract String getBDCDYID();

	public abstract void setBDCDYID(String bDCDYID);

	public abstract String getDCXMID();

	public abstract void setDCXMID(String dCXMID);

	public abstract Date getCreateTime();

	public abstract void setCreateTime(Date createTime);

	public abstract Date getModifyTime();

	public abstract void setModifyTime(Date modifyTime);

	public abstract String getLY();

	public abstract void setLY(String lY);
}
