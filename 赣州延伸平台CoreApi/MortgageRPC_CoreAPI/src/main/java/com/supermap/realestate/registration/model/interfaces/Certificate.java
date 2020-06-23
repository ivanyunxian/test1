/**   
 * 数据库证书表接口
 * @Title: Certificate.java 
 * @Package com.supermap.realestate.registration.model.interfaces 
 * @author liushufeng 
 * @date 2015年7月11日 下午9:52:08 
 * @version V1.0   
 */

package com.supermap.realestate.registration.model.interfaces;

import java.util.Date;

/**
 * 数据库证书表接口
 * @ClassName: Certificate
 * @author liushufeng
 * @date 2015年7月11日 下午9:52:08
 */
public interface Certificate {

	public abstract String getId();

	public abstract void setId(String id);

	public abstract String getXMBH();

	public abstract void setXMBH(String xMBH);

	public abstract String getZSBH();

	public abstract void setZSBH(String zSBH);

	public abstract String getBDCQZH();

	public abstract void setBDCQZH(String bDCQZH);

	public abstract Date getSZSJ();

	public abstract void setSZSJ(Date sZSJ);

	public abstract Date getCreateTime();

	public abstract void setCreateTime(Date createTime);

	public abstract Date getModifyTime();

	public abstract void setModifyTime(Date modifyTime);

}
