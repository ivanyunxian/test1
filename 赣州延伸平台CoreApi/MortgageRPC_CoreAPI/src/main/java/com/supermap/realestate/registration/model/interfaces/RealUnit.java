/**   
 * 不动产单元通用接口文件
 * @Title: RealUnit.java 
 * @Package com.supermap.realestate.registration.model.interfaces 
 * @author liushufeng 
 * @date 2015年7月11日 下午4:27:46 
 * @version V1.0   
 */
package com.supermap.realestate.registration.model.interfaces;

import com.supermap.realestate.registration.util.ConstValue.BDCDYLX;
import com.supermap.realestate.registration.util.ConstValue.DJDYLY;

/**
 * 不动产单元通用接口
 * @ClassName: RealUnit
 * @author liushufeng
 * @date 2015年7月11日 下午4:27:46
 */
public interface RealUnit {

	/**
	 * 获取不动产单元ID
	 * @Title: getId
	 * @author:liushufeng
	 * @date：2015年7月11日 下午4:29:15
	 * @return
	 */
	public String getId();

	/**
	 * 设置不动产单元ID
	 * @Title: setId
	 * @author:liushufeng
	 * @date：2015年7月12日 上午12:08:09
	 */
	public void setId(String id);

	/**
	 * 获取坐落
	 * @Title: getZL
	 * @author:liushufeng
	 * @date：2015年7月11日 下午4:30:10
	 * @return
	 */
	public String getZL();

	/**
	 * 获取不动产单元号
	 * @Title: getBDCDYH
	 * @author:liushufeng
	 * @date：2015年7月11日 下午4:30:31
	 * @return
	 */
	public String getBDCDYH();

	/**
	 * 获取登记状态
	 * @Title: getDJZT
	 * @author:liushufeng
	 * @date：2015年7月11日 下午4:30:47
	 * @return
	 */
	public String getDJZT();

	/**
	 * 设置项目编号
	 * @Title: setXMBH
	 * @author:liushufeng
	 * @date：2015年7月11日 下午4:34:20
	 * @param xmbh
	 */
	public void setXMBH(String xmbh);

	/**
	 * 获取项目编号
	 * @Title: getXMBH
	 * @author:liushufeng
	 * @date：2015年7月11日 下午4:31:42
	 * @return
	 */
	public String getXMBH();

	public BDCDYLX getBDCDYLX();

	public DJDYLY getLY();

	public void setDJZT(String djzt);
	
	public String getQXDM();
	
	public void setQXDM(String qxdm);
	
	public double getMJ();
	
	public String getRELATIONID();
	
	public void setYT(String yt);
}