package com.supermap.realestate.registration.check;

import java.util.Date;

/**
 * 检查规则接口
 * @ClassName: CheckRule
 * @author liushufeng
 * @date 2015年11月7日 下午6:50:59
 */
public interface CheckRule {

	/**
	 * 检查规则ID
	 * @Title: getId
	 * @author:liushufeng
	 * @date：2015年11月7日 下午7:04:43
	 * @return
	 */
	public abstract String getId();

	public abstract void setId(String id);

	/**
	 * 检查规则分类
	 * @Title: getCLASSNAME
	 * @author:liushufeng
	 * @date：2015年11月7日 下午7:04:58
	 * @return
	 */
	public abstract String getCLASSNAME();

	public abstract void setCLASSNAME(String cLASSNAME);

	/**
	 * 检查规则名称
	 * @Title: getNAME
	 * @author:liushufeng
	 * @date：2015年11月7日 下午7:05:16
	 * @return
	 */
	public abstract String getNAME();

	public abstract void setNAME(String nAME);

	/**
	 * 检查规则描述
	 * @Title: getDESCRIPTION
	 * @author:liushufeng
	 * @date：2015年11月7日 下午7:05:45
	 * @return
	 */
	public abstract String getDESCRIPTION();

	public abstract void setDESCRIPTION(String dESCRIPTION);

	/**
	 * 检查规则执行类型（SQL、CLASS）
	 * @Title: getEXECUTETYPE
	 * @author:liushufeng
	 * @date：2015年11月7日 下午7:05:52
	 * @return
	 */
	public abstract String getEXECUTETYPE();

	public abstract void setEXECUTETYPE(String eXECUTETYPE);

	/**
	 * 规则执行类名称
	 * @Title: getEXECUTECLASSNAME
	 * @author:liushufeng
	 * @date：2015年11月7日 下午7:06:10
	 * @return
	 */
	public abstract String getEXECUTECLASSNAME();

	public abstract void setEXECUTECLASSNAME(String eXECUTECLASSNAME);

	/**
	 * 检查规则SQL语句
	 * @Title: getEXECUTESQL
	 * @author:liushufeng
	 * @date：2015年11月7日 下午7:06:29
	 * @return
	 */
	public abstract String getEXECUTESQL();

	public abstract void setEXECUTESQL(String eXECUTESQL);

	/**
	 * 检查通过SQL结果表达式
	 * @Title: getSQLRESULTEXP
	 * @author:liushufeng
	 * @date：2015年11月7日 下午7:06:54
	 * @return
	 */
	public abstract String getSQLRESULTEXP();

	public abstract void setSQLRESULTEXP(String sQLRESULTEXP);

	/**
	 * 检查不通过时结果提示
	 * @Title: getRESULTTIP
	 * @author:liushufeng
	 * @date：2015年11月7日 下午7:07:15
	 * @return
	 */
	public abstract String getRESULTTIP();

	public abstract void setRESULTTIP(String rESULTTIP);

	/**
	 * 是否用户自定义
	 * @Title: getUSERDEFINE
	 * @author:liushufeng
	 * @date：2015年11月7日 下午7:07:28
	 * @return
	 */
	public abstract String getUSERDEFINE();

	public abstract void setUSERDEFINE(String uSERDEFINE);

	/**
	 * 创建时间
	 * @Title: getCREATETIME
	 * @author:liushufeng
	 * @date：2015年11月7日 下午7:07:36
	 * @return
	 */
	public abstract Date getCREATETIME();

	public abstract void setCREATETIME(Date cREATETIME);

	/**
	 * 最后修改时间
	 * @Title: getMODIFYTIME
	 * @author:liushufeng
	 * @date：2015年11月7日 下午7:07:43
	 * @return
	 */
	public abstract Date getMODIFYTIME();

	public abstract void setMODIFYTIME(Date mODIFYTIME);

	/**
	 * 创建人
	 * @Title: getCREATOR
	 * @author:liushufeng
	 * @date：2015年11月7日 下午7:07:50
	 * @return
	 */
	public abstract String getCREATOR();

	public abstract void setCREATOR(String cREATOR);

	/**
	 * 最后修改人
	 * @Title: getLASTMODIFIER
	 * @author:liushufeng
	 * @date：2015年11月7日 下午7:07:56
	 * @return
	 */
	public abstract String getLASTMODIFIER();

	public abstract void setLASTMODIFIER(String lASTMODIFIER);

}
