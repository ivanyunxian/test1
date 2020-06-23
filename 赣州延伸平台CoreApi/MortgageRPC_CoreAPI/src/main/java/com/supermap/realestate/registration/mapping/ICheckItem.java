/**   
 * @Title: ICheckItem.java 
 * @Package com.supermap.realestate.registration.mapping 
 * @author liushufeng 
 * @date 2015年8月5日 下午1:28:25 
 * @version V1.0   
 */

package com.supermap.realestate.registration.mapping;

/**
 * 系统检查项接口
 * @ClassName: ICheckItem
 * @author liushufeng
 * @date 2015年8月5日 下午1:28:25
 */
public interface ICheckItem {
	abstract String getId();

	abstract void setId(String id);

	abstract String getITEMNAME();

	abstract void setITEMNAME(String itemName);

	abstract String getSQLEXPR();

	abstract void setSQLEXPR(String sQL);
	
	abstract String getCHECKTYPE();

	abstract void setCHECKTYPE(String tYPE);

	abstract String getRESULTEXPR();

	abstract void setRESULTEXPR(String resultExpr);

	abstract String getCHECKLEVEL();

	abstract void setCHECKLEVEL(String errorLevel);

	abstract String getERRORINFO();

	abstract void setERRORINFO(String errorInfo);

	abstract String getQUESTIONINFO();

	abstract void setQUESTIONINFO(String questionInfo);

	abstract String getCREATOR();

	abstract void setCREATOR(String creator);

	abstract String getDESCRIPTION();

	abstract void setDESCRIPTION(String creator);

}
