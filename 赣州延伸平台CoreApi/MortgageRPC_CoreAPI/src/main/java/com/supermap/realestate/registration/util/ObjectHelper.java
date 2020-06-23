package com.supermap.realestate.registration.util;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.util.StringUtils;

import com.supermap.realestate.registration.model.BDCS_C_GZ;
import com.supermap.realestate.registration.model.BDCS_C_XZ;
import com.supermap.realestate.registration.model.BDCS_DJDY_GZ;
import com.supermap.realestate.registration.model.BDCS_DJDY_LS;
import com.supermap.realestate.registration.model.BDCS_DJDY_XZ;
import com.supermap.realestate.registration.model.BDCS_FSQL_GZ;
import com.supermap.realestate.registration.model.BDCS_FSQL_LS;
import com.supermap.realestate.registration.model.BDCS_FSQL_XZ;
import com.supermap.realestate.registration.model.BDCS_H_GZ;
import com.supermap.realestate.registration.model.BDCS_H_LS;
import com.supermap.realestate.registration.model.BDCS_H_LSY;
import com.supermap.realestate.registration.model.BDCS_H_XZ;
import com.supermap.realestate.registration.model.BDCS_H_XZY;
import com.supermap.realestate.registration.model.BDCS_LJZ_GZ;
import com.supermap.realestate.registration.model.BDCS_NYD_GZ;
import com.supermap.realestate.registration.model.BDCS_NYD_XZ;
import com.supermap.realestate.registration.model.BDCS_QDZR_GZ;
import com.supermap.realestate.registration.model.BDCS_QDZR_LS;
import com.supermap.realestate.registration.model.BDCS_QDZR_XZ;
import com.supermap.realestate.registration.model.BDCS_QLR_GZ;
import com.supermap.realestate.registration.model.BDCS_QLR_LS;
import com.supermap.realestate.registration.model.BDCS_QLR_XZ;
import com.supermap.realestate.registration.model.BDCS_QL_GZ;
import com.supermap.realestate.registration.model.BDCS_QL_LS;
import com.supermap.realestate.registration.model.BDCS_QL_XZ;
import com.supermap.realestate.registration.model.BDCS_SHYQZD_GZ;
import com.supermap.realestate.registration.model.BDCS_SHYQZD_LS;
import com.supermap.realestate.registration.model.BDCS_SHYQZD_XZ;
import com.supermap.realestate.registration.model.BDCS_SLLM_GZ;
import com.supermap.realestate.registration.model.BDCS_SLLM_LS;
import com.supermap.realestate.registration.model.BDCS_SLLM_XZ;
import com.supermap.realestate.registration.model.BDCS_SQR;
import com.supermap.realestate.registration.model.BDCS_SYQZD_GZ;
import com.supermap.realestate.registration.model.BDCS_SYQZD_LS;
import com.supermap.realestate.registration.model.BDCS_SYQZD_XZ;
import com.supermap.realestate.registration.model.BDCS_YHYDZB_GZ;
import com.supermap.realestate.registration.model.BDCS_YHYDZB_LS;
import com.supermap.realestate.registration.model.BDCS_YHYDZB_XZ;
import com.supermap.realestate.registration.model.BDCS_YHZK_GZ;
import com.supermap.realestate.registration.model.BDCS_YHZK_LS;
import com.supermap.realestate.registration.model.BDCS_YHZK_XZ;
import com.supermap.realestate.registration.model.BDCS_ZH_GZ;
import com.supermap.realestate.registration.model.BDCS_ZH_LS;
import com.supermap.realestate.registration.model.BDCS_ZH_XZ;
import com.supermap.realestate.registration.model.BDCS_ZRZ_GZ;
import com.supermap.realestate.registration.model.BDCS_ZRZ_LS;
import com.supermap.realestate.registration.model.BDCS_ZRZ_LSY;
import com.supermap.realestate.registration.model.BDCS_ZRZ_XZ;
import com.supermap.realestate.registration.model.BDCS_ZRZ_XZY;
import com.supermap.realestate.registration.model.BDCS_ZS_GZ;
import com.supermap.realestate.registration.model.BDCS_ZS_LS;
import com.supermap.realestate.registration.model.BDCS_ZS_XZ;
import com.supermap.realestate.registration.model.DCS_C_GZ;
import com.supermap.realestate.registration.model.DCS_H_GZ;
import com.supermap.realestate.registration.model.DCS_LJZ_GZ;
import com.supermap.realestate.registration.model.DCS_QLR_GZ;
import com.supermap.realestate.registration.model.DCS_SHYQZD_GZ;
import com.supermap.realestate.registration.model.DCS_SLLM_GZ;
import com.supermap.realestate.registration.model.DCS_SYQZD_GZ;
import com.supermap.realestate.registration.model.DCS_YHYDZB_GZ;
import com.supermap.realestate.registration.model.DCS_YHZK_GZ;
import com.supermap.realestate.registration.model.DCS_ZH_GZ;
import com.supermap.realestate.registration.model.DCS_ZRZ_GZ;


/**
 * 
 * @Description:对象的copy
 * @author diaoliwei
 * @date 2015年6月12日 上午11:55:03
 * @Copyright SuperMap
 */
public class ObjectHelper {

	/**
	 * 从调查库拷贝使用权宗地信息到登记库
	 * 
	 * @作者 diaoliwei
	 * @创建时间 2015年6月8日下午10:59:35
	 * @param dczd
	 * @param bdczd
	 * @return
	 */
	public static BDCS_SHYQZD_GZ copyShyqzd(DCS_SHYQZD_GZ dczd, BDCS_SHYQZD_GZ bdczd) {

		bdczd.setXMBH(dczd.getXMBH());
		bdczd.setBDCDYH(dczd.getBDCDYH());
		bdczd.setZDDM(dczd.getZDDM());
		bdczd.setZDMJ(dczd.getZDMJ());
		bdczd.setMJDW(dczd.getMJDW());
		bdczd.setZL(dczd.getZL());
		bdczd.setQLLX(dczd.getQLLX());
		bdczd.setDJQDM(dczd.getDJQDM());
		bdczd.setDJQMC(dczd.getDJQMC());
		bdczd.setDJZQDM(dczd.getDJZQDM());
		bdczd.setDJZQMC(dczd.getDJZQMC());
		bdczd.setYT(dczd.getYT());
		bdczd.setQLSDFS(dczd.getQLSDFS());
		bdczd.setQLXZ(dczd.getQLXZ());
		bdczd.setJZXG(dczd.getJZXG());
		bdczd.setJZMD(dczd.getJZMD());
		bdczd.setRJL(dczd.getRJL());
		bdczd.setZDTZM(dczd.getZDTZM());
		bdczd.setTFH(dczd.getTFH());
		bdczd.setJG(dczd.getJG());

		bdczd.setBLC(dczd.getBLC());
		bdczd.setBLZT(dczd.getBLZT());
		bdczd.setZT(dczd.getZT());
		bdczd.setYSDM(dczd.getYSDM());
		bdczd.setDJ(dczd.getDJ());
		bdczd.setZDSZD(dczd.getZDSZD());
		bdczd.setZDSZX(dczd.getZDSZX());
		bdczd.setZDSZN(dczd.getZDSZN());
		bdczd.setZDSZB(dczd.getZDSZB());
		bdczd.setZDT(dczd.getZDT());
		bdczd.setDJH(dczd.getDJH());
		bdczd.setDCXMID(dczd.getDCXMID());
		bdczd.setQXDM(dczd.getQXDM());
		bdczd.setZH(dczd.getZH());
		bdczd.setQXMC(dczd.getQXMC());
		bdczd.setZM(dczd.getZM());
		bdczd.setSYQLX(dczd.getSYQLX());
		bdczd.setTDQSLYZMCL(dczd.getTDQSLYZMCL());
		bdczd.setGMJJHYFLDM(dczd.getGMJJHYFLDM());
		bdczd.setYBZDDM(dczd.getYBZDDM());
		bdczd.setBLC(dczd.getBLC());
		bdczd.setPZYT(dczd.getPZYT());
		bdczd.setPZMJ(dczd.getPZMJ());
		bdczd.setJZZDMJ(dczd.getJZZDMJ());
		bdczd.setJZMJ(dczd.getJZMJ());
		bdczd.setCQZT(dczd.getCQZT());
		bdczd.setDYZT(dczd.getDYZT());
		bdczd.setXZZT(dczd.getXZZT());
		bdczd.setJZDWSM(dczd.getJZDWSM());
		bdczd.setYYZT(dczd.getYYZT());
		bdczd.setJZXZXSM(dczd.getJZXZXSM());
		bdczd.setDCJS(dczd.getDCJS());
		bdczd.setDCR(dczd.getDCR());
		bdczd.setDCRQ(dczd.getDCRQ());
		bdczd.setCLJS(dczd.getCLJS());
		bdczd.setCLR(dczd.getCLR());
		bdczd.setCLRQ(dczd.getCLRQ());
		bdczd.setSHYJ(dczd.getSHYJ());
		bdczd.setSHR(dczd.getSHR());
		bdczd.setSHRQ(dczd.getSHRQ());
		bdczd.setYXBZ(dczd.getYXBZ());
		bdczd.setId(dczd.getId());

		return bdczd;
	}

	/**
	 * 从调查库拷贝所用权信息到登记库
	 * 
	 * @作者 海豹
	 * @创建时间 2015年6月23日下午4:31:03
	 * @param dczd
	 * @param bdczd
	 * @return
	 */
	public static BDCS_SYQZD_GZ copySyqzd(DCS_SYQZD_GZ dczd, BDCS_SYQZD_GZ bdczd) {
		try {
			PropertyUtils.copyProperties(bdczd, dczd);
		} catch (Exception e) {
		}
		return bdczd;
	}

	/**
	 * 从预测户现状拷贝到预测户历史中
	 * @作者 海豹
	 * @创建时间 2015年7月10日下午11:10:32
	 * @param bdcs_h_xzy
	 * @param bdcs_h_lsy
	 * @return
	 */
	public static BDCS_H_LSY copyH_LSY(BDCS_H_XZY bdcs_h_xzy, BDCS_H_LSY bdcs_h_lsy) {
		try {
			PropertyUtils.copyProperties(bdcs_h_lsy, bdcs_h_xzy);
		} catch (Exception e) {
		}
		return bdcs_h_lsy;
	}
	/**
	 * 从预测现状自然幢拷贝到预测自然幢历史中
	 * @作者 海豹
	 * @创建时间 2015年7月22日上午2:15:30
	 * @param bdcs_zrz_xzy
	 * @param bdcs_zrz_lsy
	 * @return
	 */
   public static BDCS_ZRZ_LSY copyZRZ_LSY(BDCS_ZRZ_XZY bdcs_zrz_xzy, BDCS_ZRZ_LSY bdcs_zrz_lsy)
   {
	   try {
		PropertyUtils.copyProperties(bdcs_zrz_lsy, bdcs_zrz_xzy);
	} catch (Exception e) {
	}
	   return bdcs_zrz_lsy;
   }
	/**
	 * 从调查库拷贝森林林木信息到登记库
	 * 
	 * @作者 海豹
	 * @创建时间 2015年6月27日下午5:16:17
	 * @param dclm
	 * @param bdclm
	 * @return
	 */
	public static BDCS_SLLM_GZ copySyqzd(DCS_SLLM_GZ dclm, BDCS_SLLM_GZ bdclm) {
		try {
			PropertyUtils.copyProperties(bdclm, dclm);
		} catch (Exception e) {
		}
		return bdclm;
	}

	/**
	 * 从调查库拷贝宗海信息到登记库
	 * 
	 * @作者 海豹
	 * @创建时间 2015年6月25日下午2:11:52
	 * @param dczh
	 * @param bdczh
	 * @return
	 */
	public static BDCS_ZH_GZ copyZh(DCS_ZH_GZ dczh, BDCS_ZH_GZ bdczh) {
		try {
			PropertyUtils.copyProperties(bdczh, dczh);
		} catch (Exception e) {
		}
		return bdczh;
	}

	/**
	 * 从调查库拷贝用海状况信息到登记库
	 * 
	 * @作者 海豹
	 * @创建时间 2015年6月25日下午2:15:15
	 * @param dcyhzk
	 * @param bdcyhzk
	 * @return
	 */
	public static BDCS_YHZK_GZ copyYhzk(DCS_YHZK_GZ dcyhzk, BDCS_YHZK_GZ bdcyhzk) {
		try {
			PropertyUtils.copyProperties(bdcyhzk, dcyhzk);
		} catch (Exception e) {
		}
		return bdcyhzk;
	}

	/**
	 * 从调查库拷贝用海用地坐标信息到登记库
	 * 
	 * @作者 海豹
	 * @创建时间 2015年6月25日下午2:14:36
	 * @param dcyhydzb
	 * @param bdcyhydzb
	 * @return
	 */
	public static BDCS_YHYDZB_GZ copyYhydzb(DCS_YHYDZB_GZ dcyhydzb, BDCS_YHYDZB_GZ bdcyhydzb) {
		try {
			PropertyUtils.copyProperties(bdcyhydzb, dcyhydzb);
		} catch (Exception e) {
		}
		return bdcyhydzb;
	}

	/**
	 * 从调查库拷贝户信息到登记库
	 * 
	 * @作者 刘树峰
	 * @创建时间 2015年6月9日上午3:51:57
	 * @param dcs_h
	 * @param bdcs_h
	 * @return
	 */
	public static BDCS_H_GZ copyH(DCS_H_GZ dcs_h, BDCS_H_GZ bdcs_h) {
		bdcs_h.setYSDM(dcs_h.getYSDM());
		bdcs_h.setBDCDYH(dcs_h.getBDCDYH());
		bdcs_h.setFWBM(dcs_h.getFWBM());
		bdcs_h.setZRZBDCDYID(dcs_h.getZRZBDCDYID());
		bdcs_h.setZDDM(dcs_h.getZDDM());
		bdcs_h.setZDBDCDYID(dcs_h.getZDBDCDYID());
		bdcs_h.setZRZH(dcs_h.getZRZH());
		bdcs_h.setLJZID(dcs_h.getLJZID());
		bdcs_h.setLJZH(dcs_h.getLJZH());

		bdcs_h.setCID(dcs_h.getCID());
		bdcs_h.setCH(dcs_h.getCH());
		bdcs_h.setZL(dcs_h.getZL());
		bdcs_h.setMJDW(dcs_h.getMJDW());
		bdcs_h.setZZC(dcs_h.getZZC());
		bdcs_h.setQSC(dcs_h.getQSC());
		bdcs_h.setSZC(dcs_h.getSZC());
		bdcs_h.setDYH(dcs_h.getDYH());
		bdcs_h.setZCS(dcs_h.getZCS());
		bdcs_h.setHH(dcs_h.getHH());
		bdcs_h.setSHBW(dcs_h.getSHBW());

		bdcs_h.setHX(dcs_h.getHX());
		bdcs_h.setHXJG(dcs_h.getHXJG());
		bdcs_h.setFWYT1(dcs_h.getFWYT1());
		bdcs_h.setFWYT2(dcs_h.getFWYT2());
		bdcs_h.setFWYT3(dcs_h.getFWYT3());
		bdcs_h.setYCJZMJ(dcs_h.getYCJZMJ());
		bdcs_h.setYCTNJZMJ(dcs_h.getYCTNJZMJ());
		bdcs_h.setYCFTJZMJ(dcs_h.getYCFTJZMJ());
		bdcs_h.setYCDXBFJZMJ(dcs_h.getYCDXBFJZMJ());
		bdcs_h.setYCQTJZMJ(dcs_h.getYCQTJZMJ());

		bdcs_h.setYCFTXS(dcs_h.getYCFTXS());
		bdcs_h.setSCJZMJ(dcs_h.getSCJZMJ());
		bdcs_h.setSCTNJZMJ(dcs_h.getSCTNJZMJ());
		bdcs_h.setSCFTJZMJ(dcs_h.getSCFTJZMJ());
		bdcs_h.setSCDXBFJZMJ(dcs_h.getSCDXBFJZMJ());
		bdcs_h.setSCQTJZMJ(dcs_h.getSCQTJZMJ());
		bdcs_h.setSCQTJZMJ(dcs_h.getSCFTXS());
		bdcs_h.setGYTDMJ(dcs_h.getGYTDMJ());
		bdcs_h.setFTTDMJ(dcs_h.getFTTDMJ());
		bdcs_h.setDYTDMJ(dcs_h.getDYTDMJ());

		bdcs_h.setTDSYQR(dcs_h.getTDSYQR());
		bdcs_h.setFDCJYJG(dcs_h.getFDCJYJG());
		bdcs_h.setGHYT(dcs_h.getGHYT());
		bdcs_h.setFWJG(dcs_h.getFWJG());
		bdcs_h.setFWJG1(dcs_h.getFWJG1());
		bdcs_h.setFWJG2(dcs_h.getFWJG2());
		bdcs_h.setFWJG3(dcs_h.getFWJG3());
		bdcs_h.setJGSJ(dcs_h.getJGSJ());
		bdcs_h.setFWLX(dcs_h.getFWLX());
		bdcs_h.setFWXZ(dcs_h.getFWXZ());
		bdcs_h.setZDMJ(dcs_h.getZDMJ());
		bdcs_h.setSYMJ(dcs_h.getSYMJ());
		bdcs_h.setCQLY(dcs_h.getCQLY());

		bdcs_h.setQTGSD(dcs_h.getQTGSD());
		bdcs_h.setQTGSX(dcs_h.getQTGSX());
		bdcs_h.setQTGSN(dcs_h.getQTGSN());
		bdcs_h.setQTGSB(dcs_h.getQTGSB());
		bdcs_h.setFCFHT(dcs_h.getFCFHT());
		bdcs_h.setZT(dcs_h.getZT());
		bdcs_h.setQXDM(dcs_h.getQXDM());
		bdcs_h.setQXMC(dcs_h.getQXMC());
		bdcs_h.setDJQDM(dcs_h.getDJQDM());
		bdcs_h.setDJQMC(dcs_h.getDJQMC());

		bdcs_h.setDJZQMC(dcs_h.getDJZQDM());
		bdcs_h.setDJZQMC(dcs_h.getDJZQMC());
		bdcs_h.setYXBZ(dcs_h.getYXBZ());
		bdcs_h.setCQZT(dcs_h.getCQZT());
		bdcs_h.setDYZT(dcs_h.getDYZT());
		bdcs_h.setXZZT(dcs_h.getXZZT());
		bdcs_h.setBLZT(dcs_h.getBLZT());
		bdcs_h.setYYZT(dcs_h.getYYZT());
		bdcs_h.setDCXMID(dcs_h.getDCXMID());
		bdcs_h.setId(dcs_h.getId());

		return bdcs_h;
	}

	/**
	 * 从调查库拷贝户信息到登记库
	 * 
	 * @作者 刘树峰
	 * @创建时间 2015年6月9日上午3:51:57
	 * @param dcs_h
	 * @param bdcs_h
	 * @return
	 */
	public static BDCS_H_GZ copyH(DCS_H_GZ dcs_h) {
		BDCS_H_GZ bdcs_h = new BDCS_H_GZ();
		bdcs_h.setYSDM(dcs_h.getYSDM());
		bdcs_h.setBDCDYH(dcs_h.getBDCDYH());
		bdcs_h.setFWBM(dcs_h.getFWBM());
		bdcs_h.setZRZBDCDYID(dcs_h.getZRZBDCDYID());
		bdcs_h.setZDDM(dcs_h.getZDDM());
		bdcs_h.setZDBDCDYID(dcs_h.getZDBDCDYID());
		bdcs_h.setZRZH(dcs_h.getZRZH());
		bdcs_h.setLJZID(dcs_h.getLJZID());
		bdcs_h.setLJZH(dcs_h.getLJZH());

		bdcs_h.setCID(dcs_h.getCID());
		bdcs_h.setCH(dcs_h.getCH());
		bdcs_h.setZL(dcs_h.getZL());
		bdcs_h.setMJDW(dcs_h.getMJDW());
		bdcs_h.setQSC(dcs_h.getQSC());
		bdcs_h.setSZC(dcs_h.getSZC());
		bdcs_h.setZCS(dcs_h.getZCS());
		bdcs_h.setHH(dcs_h.getHH());
		bdcs_h.setSHBW(dcs_h.getSHBW());

		bdcs_h.setHX(dcs_h.getHX());
		bdcs_h.setHXJG(dcs_h.getHXJG());
		bdcs_h.setFWYT1(dcs_h.getFWYT1());
		bdcs_h.setFWYT2(dcs_h.getFWYT2());
		bdcs_h.setFWYT3(dcs_h.getFWYT3());
		bdcs_h.setYCJZMJ(dcs_h.getYCJZMJ());
		bdcs_h.setYCTNJZMJ(dcs_h.getYCTNJZMJ());
		bdcs_h.setYCFTJZMJ(dcs_h.getYCFTJZMJ());
		bdcs_h.setYCDXBFJZMJ(dcs_h.getYCDXBFJZMJ());
		bdcs_h.setYCQTJZMJ(dcs_h.getYCQTJZMJ());

		bdcs_h.setYCFTXS(dcs_h.getYCFTXS());
		bdcs_h.setSCJZMJ(dcs_h.getSCJZMJ());
		bdcs_h.setSCTNJZMJ(dcs_h.getSCTNJZMJ());
		bdcs_h.setSCFTJZMJ(dcs_h.getSCFTJZMJ());
		bdcs_h.setSCDXBFJZMJ(dcs_h.getSCDXBFJZMJ());
		bdcs_h.setSCQTJZMJ(dcs_h.getSCQTJZMJ());
		bdcs_h.setSCQTJZMJ(dcs_h.getSCFTXS());
		bdcs_h.setGYTDMJ(dcs_h.getGYTDMJ());
		bdcs_h.setFTTDMJ(dcs_h.getFTTDMJ());
		bdcs_h.setDYTDMJ(dcs_h.getDYTDMJ());

		bdcs_h.setTDSYQR(dcs_h.getTDSYQR());
		bdcs_h.setFDCJYJG(dcs_h.getFDCJYJG());
		bdcs_h.setGHYT(dcs_h.getGHYT());
		bdcs_h.setFWJG(dcs_h.getFWJG());
		bdcs_h.setFWJG1(dcs_h.getFWJG1());
		bdcs_h.setFWJG2(dcs_h.getFWJG2());
		bdcs_h.setFWJG3(dcs_h.getFWJG3());
		bdcs_h.setJGSJ(dcs_h.getJGSJ());
		bdcs_h.setFWLX(dcs_h.getFWLX());
		bdcs_h.setFWXZ(dcs_h.getFWXZ());
		bdcs_h.setZDMJ(dcs_h.getZDMJ());
		bdcs_h.setSYMJ(dcs_h.getSYMJ());
		bdcs_h.setCQLY(dcs_h.getCQLY());

		bdcs_h.setQTGSD(dcs_h.getQTGSD());
		bdcs_h.setQTGSX(dcs_h.getQTGSX());
		bdcs_h.setQTGSN(dcs_h.getQTGSN());
		bdcs_h.setQTGSB(dcs_h.getQTGSB());
		bdcs_h.setFCFHT(dcs_h.getFCFHT());
		bdcs_h.setZT(dcs_h.getZT());
		bdcs_h.setQXDM(dcs_h.getQXDM());
		bdcs_h.setQXMC(dcs_h.getQXMC());
		bdcs_h.setDJQDM(dcs_h.getDJQDM());
		bdcs_h.setDJQMC(dcs_h.getDJQMC());

		bdcs_h.setDJZQMC(dcs_h.getDJZQDM());
		bdcs_h.setDJZQMC(dcs_h.getDJZQMC());
		bdcs_h.setYXBZ(dcs_h.getYXBZ());
		bdcs_h.setCQZT(dcs_h.getCQZT());
		bdcs_h.setDYZT(dcs_h.getDYZT());
		bdcs_h.setXZZT(dcs_h.getXZZT());
		bdcs_h.setBLZT(dcs_h.getBLZT());
		bdcs_h.setYYZT(dcs_h.getYYZT());
		bdcs_h.setDCXMID(dcs_h.getDCXMID());
		bdcs_h.setId(dcs_h.getId());

		return bdcs_h;
	}

	/**
	 * 从调查库拷贝自然幢到登记库
	 * 
	 * @作者 刘树峰
	 * @创建时间 2015年6月9日上午3:54:27
	 * @param dcs_zrz
	 * @param bdcs_zrz
	 * @return
	 */
	public static BDCS_ZRZ_GZ copyZRZ(DCS_ZRZ_GZ dcs_zrz, BDCS_ZRZ_GZ bdcs_zrz) {
		bdcs_zrz.setYSDM(dcs_zrz.getYSDM());
		bdcs_zrz.setXMMC(dcs_zrz.getXMMC());
		bdcs_zrz.setBDCDYH(dcs_zrz.getBDCDYH());
		bdcs_zrz.setZDDM(dcs_zrz.getZDDM());
		bdcs_zrz.setZDBDCDYID(dcs_zrz.getZDBDCDYID());
		bdcs_zrz.setZRZH(dcs_zrz.getZRZH());
		bdcs_zrz.setZL(dcs_zrz.getZL());
		bdcs_zrz.setJZWMC(dcs_zrz.getJZWMC());
		bdcs_zrz.setJGRQ(dcs_zrz.getJGRQ());
		bdcs_zrz.setJZWGD(dcs_zrz.getJZWGD());
		bdcs_zrz.setZZDMJ(dcs_zrz.getZZDMJ());
		bdcs_zrz.setZYDMJ(dcs_zrz.getZYDMJ());
		bdcs_zrz.setYCJZMJ(dcs_zrz.getYCJZMJ());
		bdcs_zrz.setSCJZMJ(dcs_zrz.getSCJZMJ());
		bdcs_zrz.setTDSYQR(dcs_zrz.getTDSYQR());
		bdcs_zrz.setDYTDMJ(dcs_zrz.getDYTDMJ());
		bdcs_zrz.setFTTDMJ(dcs_zrz.getFTTDMJ());
		bdcs_zrz.setFDCJYJG(dcs_zrz.getFDCJYJG());
		bdcs_zrz.setZCS(dcs_zrz.getZCS());
		bdcs_zrz.setDSCS(dcs_zrz.getDSCS());
		bdcs_zrz.setDXCS(dcs_zrz.getDXCS());
		bdcs_zrz.setDXSD(dcs_zrz.getDXSD());
		bdcs_zrz.setGHYT(dcs_zrz.getGHYT());
		bdcs_zrz.setFWJG(dcs_zrz.getFWJG());
		bdcs_zrz.setZTS(dcs_zrz.getZTS());
		bdcs_zrz.setJZWJBYT(dcs_zrz.getJZWJBYT());
		bdcs_zrz.setBZ(dcs_zrz.getBZ());
		bdcs_zrz.setZT(dcs_zrz.getZT());
		bdcs_zrz.setQXDM(dcs_zrz.getQXDM());
		bdcs_zrz.setQXMC(dcs_zrz.getQXMC());
		bdcs_zrz.setDJQDM(dcs_zrz.getDJQDM());
		bdcs_zrz.setDJQMC(dcs_zrz.getDJQMC());
		bdcs_zrz.setDJZQDM(dcs_zrz.getDJZQDM());
		bdcs_zrz.setDJZQMC(dcs_zrz.getDJZQMC());
		bdcs_zrz.setDCXMID(dcs_zrz.getDCXMID());
		bdcs_zrz.setYXBZ(dcs_zrz.getYXBZ());
		//bdcs_zrz.setFCFHT(dcs_zrz.getFCFHT());
		//bdcs_zrz.setFCFHTWJ(dcs_zrz.getFCFHTWJ());
		bdcs_zrz.setId(dcs_zrz.getId());
		return bdcs_zrz;
	}

	/**
	 * 从调查库拷贝自然幢到登记库
	 * 
	 * @作者 刘树峰
	 * @创建时间 2015年6月9日上午3:57:36
	 * @param dcs_zrz
	 * @return
	 */
	public static BDCS_ZRZ_GZ copyZRZ(DCS_ZRZ_GZ dcs_zrz) {
		BDCS_ZRZ_GZ bdcs_zrz = new BDCS_ZRZ_GZ();
		bdcs_zrz.setYSDM(dcs_zrz.getYSDM());
		bdcs_zrz.setXMMC(dcs_zrz.getXMMC());
		bdcs_zrz.setBDCDYH(dcs_zrz.getBDCDYH());
		bdcs_zrz.setZDDM(dcs_zrz.getZDDM());
		bdcs_zrz.setZDBDCDYID(dcs_zrz.getZDBDCDYID());
		bdcs_zrz.setZRZH(dcs_zrz.getZRZH());
		bdcs_zrz.setZL(dcs_zrz.getZL());
		bdcs_zrz.setJZWMC(dcs_zrz.getJZWMC());
		bdcs_zrz.setJGRQ(dcs_zrz.getJGRQ());
		bdcs_zrz.setJZWGD(dcs_zrz.getJZWGD());
		bdcs_zrz.setZZDMJ(dcs_zrz.getZZDMJ());
		bdcs_zrz.setZYDMJ(dcs_zrz.getZYDMJ());
		bdcs_zrz.setYCJZMJ(dcs_zrz.getYCJZMJ());
		bdcs_zrz.setSCJZMJ(dcs_zrz.getSCJZMJ());
		bdcs_zrz.setTDSYQR(dcs_zrz.getTDSYQR());
		bdcs_zrz.setDYTDMJ(dcs_zrz.getDYTDMJ());
		bdcs_zrz.setFTTDMJ(dcs_zrz.getFTTDMJ());
		bdcs_zrz.setFDCJYJG(dcs_zrz.getFDCJYJG());
		bdcs_zrz.setZCS(dcs_zrz.getZCS());
		bdcs_zrz.setDSCS(dcs_zrz.getDSCS());
		bdcs_zrz.setDXCS(dcs_zrz.getDXCS());
		bdcs_zrz.setDXSD(dcs_zrz.getDXSD());
		bdcs_zrz.setGHYT(dcs_zrz.getGHYT());
		bdcs_zrz.setFWJG(dcs_zrz.getFWJG());
		bdcs_zrz.setZTS(dcs_zrz.getZTS());
		bdcs_zrz.setJZWJBYT(dcs_zrz.getJZWJBYT());
		bdcs_zrz.setBZ(dcs_zrz.getBZ());
		bdcs_zrz.setZT(dcs_zrz.getZT());
		bdcs_zrz.setQXDM(dcs_zrz.getQXDM());
		bdcs_zrz.setQXMC(dcs_zrz.getQXMC());
		bdcs_zrz.setDJQDM(dcs_zrz.getDJQDM());
		bdcs_zrz.setDJQMC(dcs_zrz.getDJQMC());
		bdcs_zrz.setDJZQDM(dcs_zrz.getDJZQDM());
		bdcs_zrz.setDJZQMC(dcs_zrz.getDJZQMC());
		bdcs_zrz.setDCXMID(dcs_zrz.getDCXMID());
		bdcs_zrz.setYXBZ(dcs_zrz.getYXBZ());
		//bdcs_zrz.setFCFHT(dcs_zrz.getFCFHT());
		//bdcs_zrz.setFCFHTWJ(dcs_zrz.getFCFHTWJ());
		bdcs_zrz.setId(dcs_zrz.getId());
		return bdcs_zrz;
	}

	/**
	 * 从调查库拷贝 层 到登记库
	 * 
	 * @作者 刘树峰
	 * @创建时间 2015年6月9日上午4:20:53
	 * @param dcc
	 * @return
	 */
	public static BDCS_C_GZ copyC(DCS_C_GZ dcc) {
		BDCS_C_GZ c = new BDCS_C_GZ();

		c.setSJC(dcc.getSJC());
		c.setCGYJZMJ(dcc.getCGYJZMJ());
		c.setCFTJZMJ(dcc.getCFTJZMJ());
		c.setCH(dcc.getCH());
		c.setCTNJZMJ(dcc.getCTNJZMJ());
		c.setCJZMJ(dcc.getCJZMJ());
		c.setCYTMJ(dcc.getCYTMJ());
		c.setCG(dcc.getCG());
		c.setSPTYMJ(dcc.getSPTYMJ());
		c.setZRZBDCDYID(dcc.getZRZBDCDYID());
		c.setZRZH(dcc.getZRZH());
		c.setYSDM(dcc.getYSDM());
		c.setDCXMID(dcc.getDCXMID());
		return c;
	}

	/**
	 * 拷贝DCS_LJZ_GZ属性到BDCS_LJZ_GZ
	 * 
	 * @作者 李桐
	 * @创建时间 2015年6月9日下午3:23:02
	 * @param source
	 * @return
	 */
	public static BDCS_LJZ_GZ copyLJZ(DCS_LJZ_GZ source) {
		BDCS_LJZ_GZ dest = new BDCS_LJZ_GZ();
		dest.setDSCS(source.getDSCS());
		dest.setDXCS(source.getDXCS());
		dest.setBZ(source.getBZ());
		dest.setSCQTMJ(source.getSCQTMJ());
		dest.setSCDXMJ(source.getSCDXMJ());
		dest.setSCJZMJ(source.getSCJZMJ());
		dest.setJZWZT(source.getJZWZT());
		dest.setZCS(source.getZCS());
		dest.setFWYT1(source.getFWYT1());
		dest.setFWYT2(source.getFWYT2());
		dest.setFWYT3(source.getFWYT3());
		dest.setFWJG1(source.getFWYT1());
		dest.setFWJG2(source.getFWJG2());
		dest.setFWJG3(source.getFWJG3());
		dest.setZRZBDCDYID(source.getZRZBDCDYID());
		dest.setZRZH(source.getZRZH());
		dest.setYSDM(source.getYSDM());
		dest.setDCXMID(source.getDCXMID());
		dest.setId(source.getId());
		dest.setLJZH(source.getLJZH());
		dest.setMPH(source.getMPH());
		dest.setXMBH(source.getXMBH());
		dest.setYCQTMJ(source.getYCQTMJ());
		dest.setYCDXMJ(source.getYCDXMJ());
		dest.setYCJZMJ(source.getYCJZMJ());
		return dest;
	}

	/**
	 * 拷贝DCS_QLR信息到BDCS_SQR
	 * 
	 * @作者 李桐
	 * @创建时间 2015年6月9日下午3:47:23
	 * @param source
	 * @return
	 */
	public static BDCS_SQR copySQR(DCS_QLR_GZ source) {
		BDCS_SQR dest = new BDCS_SQR();
		dest.setDLRXM(source.getDLRXM());
		dest.setDLRLXDH(source.getDLRLXDH());
		dest.setDLRZJHM(source.getDLRZJHM());
		dest.setDLRZJLX(source.getDLRZJLX());
		dest.setDLJGMC(source.getDLJGMC());
		dest.setGYFS(source.getGYFS());
		dest.setFZJG(source.getFZJG());
		dest.setGJDQ(source.getGJ());// 国家地区???
		dest.setGZDW(source.getGZDW());
		dest.setXB(source.getXB());
		dest.setHJSZSS(source.getHJSZSS());
		dest.setSSHY(source.getSSHY());
		dest.setYXBZ(source.getYXBZ());
		dest.setQLBL(source.getQLBL());
		if (!StringUtils.isEmpty(source.getQLMJ())) {
			dest.setQLMJ(source.getQLMJ().toString());// 类型不一致
		}
		dest.setFDDBR(source.getFDDBR());
		dest.setFDDBRDH(source.getFDDBRDH());
		dest.setFDDBRZJLX(source.getFDDBRZJLX());
		dest.setId(source.getId());
		dest.setSQRXM(source.getQLRMC());
		dest.setSQRLX(source.getQLRLX());
		dest.setDZYJ(source.getDZYJ());
		dest.setLXDH(source.getDH());
		dest.setZJH(source.getZJH());
		dest.setZJLX(source.getZJZL());
		dest.setTXDZ(source.getDZ());
		dest.setYZBM(source.getYB());
		dest.setXMBH(source.getXMBH());
		if (!StringUtils.isEmpty(source.getSXH())) {
			dest.setSXH(source.getSXH().toString());// 类型不一致
		}
		return dest;
	}

	/**
	 * 从现状层拷贝使用权宗地到工作层
	 * 
	 * @作者 diaoliwei
	 * @创建时间 2015年6月11日下午4:20:22
	 * @param dczd
	 * @param bdczd
	 * @return
	 */
	public static BDCS_SHYQZD_GZ copyShyqzd(BDCS_SHYQZD_XZ xzzd, BDCS_SHYQZD_GZ bdczd) {
		bdczd.setXMBH(xzzd.getXMBH() == null ? "" : xzzd.getXMBH());
		bdczd.setBDCDYH(xzzd.getBDCDYH() == null ? "" : xzzd.getBDCDYH());
		bdczd.setZDDM(xzzd.getZDDM() == null ? "" : xzzd.getZDDM());
		bdczd.setZDMJ(xzzd.getZDMJ());
		bdczd.setMJDW(xzzd.getMJDW() == null ? "" : xzzd.getMJDW());
		bdczd.setZL(xzzd.getZL() == null ? "" : xzzd.getZL());
		bdczd.setQLLX(xzzd.getQLLX() == null ? "" :xzzd.getQLLX());
		bdczd.setDJQDM(xzzd.getDJQDM() == null ? "" :xzzd.getDJQDM());
		bdczd.setDJQMC(xzzd.getDJQMC() == null ? "" :xzzd.getDJQMC());
		bdczd.setDJZQDM(xzzd.getDJZQDM() == null ? "" :xzzd.getDJZQDM());
		bdczd.setDJZQMC(xzzd.getDJZQMC() == null ? "" :xzzd.getDJZQMC());
		bdczd.setYT(xzzd.getYT() == null ? "" :xzzd.getYT());
		bdczd.setQLSDFS(xzzd.getQLSDFS() == null ? "" :xzzd.getQLSDFS());
		bdczd.setQLXZ(xzzd.getQLXZ() == null ? "" :xzzd.getQLXZ());
		bdczd.setJZXG(xzzd.getJZXG());
		bdczd.setJZMD(xzzd.getJZMD() == null ? "" :xzzd.getJZMD());
		bdczd.setRJL(xzzd.getRJL() == null ? "" :xzzd.getRJL());
		bdczd.setZDTZM(xzzd.getZDTZM() == null ? "" :xzzd.getZDTZM());
		bdczd.setTFH(xzzd.getTFH() == null ? "" :xzzd.getTFH());
		bdczd.setJG(xzzd.getJG());

		bdczd.setBLC(xzzd.getBLC() == null ? "" :xzzd.getBLC());
		bdczd.setBLZT(xzzd.getBLZT() == null ? "" :xzzd.getBLZT());
		bdczd.setZT(xzzd.getZT() == null ? "" :xzzd.getZT());
		bdczd.setYSDM(xzzd.getYSDM() == null ? "" :xzzd.getYSDM());
		bdczd.setDJ(xzzd.getDJ() == null ? "" :xzzd.getDJ());
		bdczd.setZDSZD(xzzd.getZDSZD() == null ? "" :xzzd.getZDSZD());
		bdczd.setZDSZX(xzzd.getZDSZX() == null ? "" :xzzd.getZDSZX());
		bdczd.setZDSZN(xzzd.getZDSZN() == null ? "" :xzzd.getZDSZN());
		bdczd.setZDSZB(xzzd.getZDSZB() == null ? "" :xzzd.getZDSZB());
		bdczd.setZDT(xzzd.getZDT() == null ? "" :xzzd.getZDT());
		bdczd.setDJH(xzzd.getDJH() == null ? "" :xzzd.getDJH());
		bdczd.setDCXMID(xzzd.getDCXMID() == null ? "" :xzzd.getDCXMID());
		bdczd.setQXDM(xzzd.getQXDM() == null ? "" :xzzd.getQXDM());
		bdczd.setZH(xzzd.getZH() == null ? "" :xzzd.getZH());
		bdczd.setQXMC(xzzd.getQXMC() == null ? "" :xzzd.getQXMC());
		bdczd.setZM(xzzd.getZM() == null ? "" :xzzd.getZM());
		bdczd.setSYQLX(xzzd.getSYQLX() == null ? "" :xzzd.getSYQLX());
		bdczd.setTDQSLYZMCL(xzzd.getTDQSLYZMCL() == null ? "" :xzzd.getTDQSLYZMCL());
		bdczd.setGMJJHYFLDM(xzzd.getGMJJHYFLDM() == null ? "" :xzzd.getGMJJHYFLDM());
		bdczd.setYBZDDM(xzzd.getYBZDDM() == null ? "" :xzzd.getYBZDDM());
		bdczd.setBLC(xzzd.getBLC() == null ? "" :xzzd.getBLC());
		bdczd.setPZYT(xzzd.getPZYT() == null ? "" :xzzd.getPZYT());
		bdczd.setPZMJ(xzzd.getPZMJ());
		bdczd.setJZZDMJ(xzzd.getJZZDMJ());
		bdczd.setJZMJ(xzzd.getJZMJ());
		bdczd.setCQZT(xzzd.getCQZT() == null ? "" :xzzd.getCQZT());
		bdczd.setDYZT(xzzd.getDYZT() == null ? "" :xzzd.getDYZT());
		bdczd.setXZZT(xzzd.getXZZT() == null ? "" :xzzd.getXZZT());
		bdczd.setJZDWSM(xzzd.getJZDWSM() == null ? "" :xzzd.getJZDWSM());
		bdczd.setYYZT(xzzd.getYYZT() == null ? "" :xzzd.getYYZT());
		bdczd.setJZXZXSM(xzzd.getJZXZXSM() == null ? "" :xzzd.getJZXZXSM());
		bdczd.setDCJS(xzzd.getDCJS() == null ? "" :xzzd.getDCJS());
		bdczd.setDCR(xzzd.getDCR() == null ? "" :xzzd.getDCR());
		bdczd.setDCRQ(xzzd.getDCRQ());
		bdczd.setCLJS(xzzd.getCLJS() == null ? "" :xzzd.getCLJS());
		bdczd.setCLR(xzzd.getCLR() == null ? "" :xzzd.getCLR());
		bdczd.setCLRQ(xzzd.getCLRQ());
		bdczd.setSHYJ(xzzd.getSHYJ() == null ? "" :xzzd.getSHYJ());
		bdczd.setSHR(xzzd.getSHR() == null ? "" :xzzd.getSHR());
		bdczd.setSHRQ(xzzd.getSHRQ());
		bdczd.setYXBZ(xzzd.getYXBZ() == null ? "" :xzzd.getYXBZ());
		bdczd.setId(xzzd.getId() == null ? "" :xzzd.getId());

		return bdczd;
	}

	public static BDCS_H_GZ copyHFromXZtoGZ(BDCS_H_XZ xzh) {
		BDCS_H_GZ gzh = new BDCS_H_GZ();

		gzh.setYSDM(xzh.getYSDM());
		gzh.setBDCDYH(xzh.getBDCDYH());
		gzh.setFWBM(xzh.getFWBM());
		gzh.setZRZBDCDYID(xzh.getZRZBDCDYID());
		gzh.setZDDM(xzh.getZDDM());
		gzh.setZDBDCDYID(xzh.getZDBDCDYID());
		gzh.setZRZH(xzh.getZRZH());
		gzh.setLJZID(xzh.getLJZID());
		gzh.setLJZH(xzh.getLJZH());

		gzh.setCID(xzh.getCID());
		gzh.setCH(xzh.getCH());
		gzh.setZL(xzh.getZL());
		gzh.setMJDW(xzh.getMJDW());
		gzh.setQSC(xzh.getQSC());
		gzh.setSZC(xzh.getSZC());
		gzh.setZCS(xzh.getZCS());
		gzh.setHH(xzh.getHH());
		gzh.setSHBW(xzh.getSHBW());

		gzh.setHX(xzh.getHX());
		gzh.setHXJG(xzh.getHXJG());
		gzh.setFWYT1(xzh.getFWYT1());
		gzh.setFWYT2(xzh.getFWYT2());
		gzh.setFWYT3(xzh.getFWYT3());
		gzh.setYCJZMJ(xzh.getYCJZMJ());
		gzh.setYCTNJZMJ(xzh.getYCTNJZMJ());
		gzh.setYCFTJZMJ(xzh.getYCFTJZMJ());
		gzh.setYCDXBFJZMJ(xzh.getYCDXBFJZMJ());
		gzh.setYCQTJZMJ(xzh.getYCQTJZMJ());

		gzh.setYCFTXS(xzh.getYCFTXS());
		gzh.setSCJZMJ(xzh.getSCJZMJ());
		gzh.setSCTNJZMJ(xzh.getSCTNJZMJ());
		gzh.setSCFTJZMJ(xzh.getSCFTJZMJ());
		gzh.setSCDXBFJZMJ(xzh.getSCDXBFJZMJ());
		gzh.setSCQTJZMJ(xzh.getSCQTJZMJ());
		gzh.setSCQTJZMJ(xzh.getSCFTXS());
		gzh.setGYTDMJ(xzh.getGYTDMJ());
		gzh.setFTTDMJ(xzh.getFTTDMJ());
		gzh.setDYTDMJ(xzh.getDYTDMJ());

		gzh.setTDSYQR(xzh.getTDSYQR());
		gzh.setFDCJYJG(xzh.getFDCJYJG());
		gzh.setGHYT(xzh.getGHYT());
		gzh.setFWJG(xzh.getFWJG());
		gzh.setFWJG1(xzh.getFWJG1());
		gzh.setFWJG2(xzh.getFWJG2());
		gzh.setFWJG3(xzh.getFWJG3());
		gzh.setJGSJ(xzh.getJGSJ());
		gzh.setFWLX(xzh.getFWLX());
		gzh.setFWXZ(xzh.getFWXZ());
		gzh.setZDMJ(xzh.getZDMJ());
		gzh.setSYMJ(xzh.getSYMJ());
		gzh.setCQLY(xzh.getCQLY());

		gzh.setQTGSD(xzh.getQTGSD());
		gzh.setQTGSX(xzh.getQTGSX());
		gzh.setQTGSN(xzh.getQTGSN());
		gzh.setQTGSB(xzh.getQTGSB());
		gzh.setFCFHT(xzh.getFCFHT());
		gzh.setZT(xzh.getZT());
		gzh.setQXDM(xzh.getQXDM());
		gzh.setQXMC(xzh.getQXMC());
		gzh.setDJQDM(xzh.getDJQDM());
		gzh.setDJQMC(xzh.getDJQMC());

		gzh.setDJZQMC(xzh.getDJZQDM());
		gzh.setDJZQMC(xzh.getDJZQMC());
		gzh.setYXBZ(xzh.getYXBZ());
		gzh.setCQZT(xzh.getCQZT());
		gzh.setDYZT(xzh.getDYZT());
		gzh.setXZZT(xzh.getXZZT());
		gzh.setBLZT(xzh.getBLZT());
		gzh.setYYZT(xzh.getYYZT());
		gzh.setDCXMID(xzh.getDCXMID());
		gzh.setId(xzh.getId());
		return gzh;
	}

	/**
	 * 将权利从工作层拷贝到现状层
	 * 
	 * @作者：俞学斌
	 * @param bdcs_ql_gz
	 *            工作层权利
	 * @return
	 */
	public static BDCS_QL_XZ copyQL_GZToXZ(BDCS_QL_GZ bdcs_ql_gz) {
		BDCS_QL_XZ bdcs_ql_xz = new BDCS_QL_XZ();
		try {
			PropertyUtils.copyProperties(bdcs_ql_xz, bdcs_ql_gz);
		} catch (Exception e) {
		}
		return bdcs_ql_xz;
	}

	/**
	 * 将权利人从工作层拷贝到现状层
	 * 
	 * @作者：俞学斌
	 * @param bdcs_qlr_gz
	 *            工作层权利人
	 * @return
	 */
	public static BDCS_QLR_XZ copyQLR_GZToXZ(BDCS_QLR_GZ bdcs_qlr_gz) {
		BDCS_QLR_XZ bdcs_qlr_xz = new BDCS_QLR_XZ();
		try {
			PropertyUtils.copyProperties(bdcs_qlr_xz, bdcs_qlr_gz);
		} catch (Exception e) {
		}
		return bdcs_qlr_xz;
	}

	/**
	 * 将附属权利从工作层拷贝到现状层
	 * 
	 * @作者：俞学斌
	 * @param bdcs_ql_gz
	 *            工作层附属权利
	 * @return
	 */
	public static BDCS_FSQL_XZ copyFSQL_GZToXZ(BDCS_FSQL_GZ bdcs_fsql_gz) {
		BDCS_FSQL_XZ bdcs_fsql_xz = new BDCS_FSQL_XZ();
		try {
			PropertyUtils.copyProperties(bdcs_fsql_xz, bdcs_fsql_gz);
		} catch (Exception e) {
		}
		return bdcs_fsql_xz;
	}

	/**
	 * 将登记单元从工作层拷贝到现状层
	 * 
	 * @作者：俞学斌
	 * @param bdcs_djdy_gz
	 *            工作层登记单元
	 * @return
	 */
	public static BDCS_DJDY_XZ copyDJDY_GZToXZ(BDCS_DJDY_GZ bdcs_djdy_gz) {
		BDCS_DJDY_XZ bdcs_djdy_xz = new BDCS_DJDY_XZ();
		try {
			PropertyUtils.copyProperties(bdcs_djdy_xz, bdcs_djdy_gz);
		} catch (Exception e) {
		}
		return bdcs_djdy_xz;
	}

	/**
	 * 将证书从工作层拷贝到现状层
	 * 
	 * @作者：俞学斌
	 * @param bdcs_zs_gz
	 *            工作层证书
	 * @return
	 */
	public static BDCS_ZS_XZ copyZS_GZToXZ(BDCS_ZS_GZ bdcs_zs_gz) {
		BDCS_ZS_XZ bdcs_zs_xz = new BDCS_ZS_XZ();
		try {
			PropertyUtils.copyProperties(bdcs_zs_xz, bdcs_zs_gz);
		} catch (Exception e) {
		}
		return bdcs_zs_xz;
	}

	/**
	 * 将权利-权利人-证书-单元关系从工作层拷贝到现状层
	 * 
	 * @作者：俞学斌
	 * @param bdcs_qdzr_gz
	 *            工作层权利-权利人-证书-单元关系
	 * @return
	 */
	public static BDCS_QDZR_XZ copyQDZR_GZToXZ(BDCS_QDZR_GZ bdcs_qdzr_gz) {
		BDCS_QDZR_XZ bdcs_qdzr_xz = new BDCS_QDZR_XZ();
		try {
			PropertyUtils.copyProperties(bdcs_qdzr_xz, bdcs_qdzr_gz);
		} catch (Exception e) {
		}
		return bdcs_qdzr_xz;
	}

	/**
	 * 将使用权宗地从工作层拷贝到现状层
	 * 
	 * @作者：俞学斌
	 * @param bdcs_shyqzd_gz
	 *            工作层使用权宗地
	 * @return
	 */
	public static BDCS_SHYQZD_XZ copySHYQZD_GZToXZ(BDCS_SHYQZD_GZ bdcs_shyqzd_gz) {
		BDCS_SHYQZD_XZ bdcs_shyqzd_xz = new BDCS_SHYQZD_XZ();
		try {
			PropertyUtils.copyProperties(bdcs_shyqzd_xz, bdcs_shyqzd_gz);
		} catch (Exception e) {
		}
		return bdcs_shyqzd_xz;
	}

	/**
	 * 将所有权从工作层拷贝到现状层
	 * 
	 * @作者 海豹
	 * @创建时间 2015年6月23日下午3:50:01
	 * @param bdcs_syqzd_gz
	 * @return
	 */
	public static BDCS_SYQZD_XZ copySYQZD_GZToXZ(BDCS_SYQZD_GZ bdcs_syqzd_gz) {
		BDCS_SYQZD_XZ bdcs_syqzd_xz = new BDCS_SYQZD_XZ();
		try {
			PropertyUtils.copyProperties(bdcs_syqzd_xz, bdcs_syqzd_gz);
		} catch (Exception e) {
		}
		return bdcs_syqzd_xz;
	}

	/**
	 * 将森林林木从工作层拷贝到现状层
	 * 
	 * @作者 海豹
	 * @创建时间 2015年6月28日上午1:02:45
	 * @param bdcs_sllm_gz
	 * @return
	 */
	public static BDCS_SLLM_XZ copySLLM_GZToXZ(BDCS_SLLM_GZ bdcs_sllm_gz) {
		BDCS_SLLM_XZ bdcs_sllm_xz = new BDCS_SLLM_XZ();
		try {
			PropertyUtils.copyProperties(bdcs_sllm_xz, bdcs_sllm_gz);
		} catch (Exception e) {
		}
		return bdcs_sllm_xz;
	}
	
	/**
	 * 将农用地从工作层拷贝到现状层
	 * 
	 * @author liangc
	 * @创建时间 2017年10月27日上午16:16:55
	 * @param bdcs_nyd_gz
	 * @return
	 */
	public static BDCS_NYD_XZ copyNYD_GZToXZ(BDCS_NYD_GZ bdcs_nyd_gz) {
		BDCS_NYD_XZ bdcs_nyd_xz = new BDCS_NYD_XZ();
		try {
			PropertyUtils.copyProperties(bdcs_nyd_xz, bdcs_nyd_gz);
		} catch (Exception e) {
		}
		return bdcs_nyd_xz;
	}

	/**
	 * 将宗海从工作层拷贝到现状层
	 * 
	 * @作者 海豹
	 * @创建时间 2015年6月26日下午4:10:23
	 * @param bdcs_zh_gz
	 * @return
	 */
	public static BDCS_ZH_XZ copyZH_GZToXZ(BDCS_ZH_GZ bdcs_zh_gz) {
		BDCS_ZH_XZ bdcs_zh_xz = new BDCS_ZH_XZ();
		try {
			PropertyUtils.copyProperties(bdcs_zh_xz, bdcs_zh_gz);
		} catch (Exception e) {
		}
		return bdcs_zh_xz;
	}

	/**
	 * 将用海状况从工作层拷贝到现状层
	 * 
	 * @作者 海豹
	 * @创建时间 2015年6月26日下午4:10:54
	 * @param bdcs_yhzk_gz
	 * @return
	 */
	public static BDCS_YHZK_XZ copyYHZK_GZToXZ(BDCS_YHZK_GZ bdcs_yhzk_gz) {
		BDCS_YHZK_XZ bdcs_yhzk_xz = new BDCS_YHZK_XZ();
		try {
			PropertyUtils.copyProperties(bdcs_yhzk_xz, bdcs_yhzk_gz);
		} catch (Exception e) {
		}
		return bdcs_yhzk_xz;
	}

	/**
	 * 将用海用地坐标从工作层拷贝到现状层
	 * 
	 * @作者 海豹
	 * @创建时间 2015年6月26日下午4:11:31
	 * @param bdcs_yhydzb_gz
	 * @return
	 */
	public static BDCS_YHYDZB_XZ copyYHYDZB_GZToXZ(BDCS_YHYDZB_GZ bdcs_yhydzb_gz) {
		BDCS_YHYDZB_XZ bdcs_yhydzb_xz = new BDCS_YHYDZB_XZ();
		try {
			PropertyUtils.copyProperties(bdcs_yhydzb_xz, bdcs_yhydzb_gz);
		} catch (Exception e) {
		}
		return bdcs_yhydzb_xz;
	}

	/**
	 * 将自然幢从工作层拷贝到现状层
	 * 
	 * @作者：俞学斌
	 * @param bdcs_zrz_gz
	 *            工作层自然幢
	 * @return
	 */
	public static BDCS_ZRZ_XZ copyZRZ_GZToXZ(BDCS_ZRZ_GZ bdcs_zrz_gz) {
		BDCS_ZRZ_XZ bdcs_zrz_xz = new BDCS_ZRZ_XZ();
		try {
			PropertyUtils.copyProperties(bdcs_zrz_xz, bdcs_zrz_gz);
		} catch (Exception e) {
		}
		return bdcs_zrz_xz;
	}

	/**
	 * 将户从工作层拷贝到现状层
	 * 
	 * @作者：俞学斌
	 * @param bdcs_h_gz
	 *            工作层户
	 * @return
	 */
	public static BDCS_H_XZ copyH_GZToXZ(BDCS_H_GZ bdcs_h_gz) {
		BDCS_H_XZ bdcs_h_xz = new BDCS_H_XZ();
		try {
			PropertyUtils.copyProperties(bdcs_h_xz, bdcs_h_gz);
		} catch (Exception e) {
		}
		return bdcs_h_xz;
	}

	/**
	 * 将层从工作层拷贝到现状层
	 * 
	 * @作者 diaoliwei
	 * @创建时间 2015年6月13日下午6:28:49
	 * @param bdcs_C_GZ
	 * @return
	 */
	public static BDCS_C_XZ copyC_GZToXZ(BDCS_C_GZ bdcs_C_GZ) {
		BDCS_C_XZ bdcs_C_XZ = new BDCS_C_XZ();
		try {
			PropertyUtils.copyProperties(bdcs_C_XZ, bdcs_C_GZ);
		} catch (Exception e) {
		}
		return bdcs_C_XZ;
	}

	/**
	 * 将权利从现状层拷贝到历史层
	 * 
	 * @作者：俞学斌
	 * @param bdcs_ql_xz
	 *            现状层权利
	 * @return
	 */
	public static BDCS_QL_LS copyQL_XZToLS(BDCS_QL_XZ bdcs_ql_xz) {
		BDCS_QL_LS bdcs_ql_ls = new BDCS_QL_LS();
		try {
			PropertyUtils.copyProperties(bdcs_ql_ls, bdcs_ql_xz);
		} catch (Exception e) {
		}
		return bdcs_ql_ls;
	}

	/**
	 * 将权利人从现状层拷贝到历史层
	 * 
	 * @作者：俞学斌
	 * @param bdcs_qlr_xz
	 *            现状层权利人
	 * @return
	 */
	public static BDCS_QLR_LS copyQLR_XZToLS(BDCS_QLR_XZ bdcs_qlr_xz) {
		BDCS_QLR_LS bdcs_qlr_ls = new BDCS_QLR_LS();
		try {
			PropertyUtils.copyProperties(bdcs_qlr_ls, bdcs_qlr_xz);
		} catch (Exception e) {
		}
		return bdcs_qlr_ls;
	}

	/**
	 * 将附属权利从现状层拷贝到历史层
	 * 
	 * @作者：俞学斌
	 * @param bdcs_ql_xz
	 *            现状层附属权利
	 * @return
	 */
	public static BDCS_FSQL_LS copyFSQL_XZToLS(BDCS_FSQL_XZ bdcs_fsql_xz) {
		BDCS_FSQL_LS bdcs_fsql_ls = new BDCS_FSQL_LS();
		try {
			PropertyUtils.copyProperties(bdcs_fsql_ls, bdcs_fsql_xz);
		} catch (Exception e) {
		}
		return bdcs_fsql_ls;
	}

	/**
	 * 将登记单元从现状层拷贝到历史层
	 * 
	 * @作者：俞学斌
	 * @param bdcs_djdy_xz
	 *            现状层登记单元
	 * @return
	 */
	public static BDCS_DJDY_LS copyDJDY_XZToLS(BDCS_DJDY_XZ bdcs_djdy_xz) {
		BDCS_DJDY_LS bdcs_djdy_ls = new BDCS_DJDY_LS();
		try {
			PropertyUtils.copyProperties(bdcs_djdy_ls, bdcs_djdy_xz);
		} catch (Exception e) {
		}
		return bdcs_djdy_ls;
	}

	/**
	 * 将证书从现状层拷贝到历史层
	 * 
	 * @作者：俞学斌
	 * @param bdcs_zs_xz
	 *            现状层证书
	 * @return
	 */
	public static BDCS_ZS_LS copyZS_XZToLS(BDCS_ZS_XZ bdcs_zs_xz) {
		BDCS_ZS_LS bdcs_zs_ls = new BDCS_ZS_LS();
		try {
			PropertyUtils.copyProperties(bdcs_zs_ls, bdcs_zs_xz);
		} catch (Exception e) {
		}
		return bdcs_zs_ls;
	}

	/**
	 * 将权利-权利人-证书-单元关系从现状层拷贝到历史层
	 * 
	 * @作者：俞学斌
	 * @param bdcs_qdzr_xz
	 *            现状层权利-权利人-证书-单元关系
	 * @return
	 */
	public static BDCS_QDZR_LS copyQDZR_XZToLS(BDCS_QDZR_XZ bdcs_qdzr_xz) {
		BDCS_QDZR_LS bdcs_qdzr_ls = new BDCS_QDZR_LS();
		try {
			PropertyUtils.copyProperties(bdcs_qdzr_ls, bdcs_qdzr_xz);
		} catch (Exception e) {
		}
		return bdcs_qdzr_ls;
	}

	/**
	 * 将使用权宗地从现状层拷贝到历史层
	 * 
	 * @作者：俞学斌
	 * @param bdcs_shyqzd_xz
	 *            现状层使用权宗地
	 * @return
	 */
	public static BDCS_SHYQZD_LS copySHYQZD_XZToLS(BDCS_SHYQZD_XZ bdcs_shyqzd_xz) {
		BDCS_SHYQZD_LS bdcs_shyqzd_ls = new BDCS_SHYQZD_LS();
		try {
			PropertyUtils.copyProperties(bdcs_shyqzd_ls, bdcs_shyqzd_xz);
		} catch (Exception e) {
		}
		return bdcs_shyqzd_ls;
	}

	/**
	 * 将所有权宗地从现状层拷贝到历史层
	 * 
	 * @作者 海豹
	 * @创建时间 2015年6月23日下午3:57:30
	 * @param bdcs_syqzd_xz
	 * @return
	 */
	public static BDCS_SYQZD_LS copySYQZD_XZToLS(BDCS_SYQZD_XZ bdcs_syqzd_xz) {
		BDCS_SYQZD_LS bdcs_syqzd_ls = new BDCS_SYQZD_LS();
		try {
			PropertyUtils.copyProperties(bdcs_syqzd_ls, bdcs_syqzd_xz);
		} catch (Exception e) {
		}
		return bdcs_syqzd_ls;
	}

	public static BDCS_SLLM_LS copySLLM_XZToLS(BDCS_SLLM_XZ bdcs_sllm_xz) {
		BDCS_SLLM_LS bdcs_sllm_ls = new BDCS_SLLM_LS();
		try {
			PropertyUtils.copyProperties(bdcs_sllm_ls, bdcs_sllm_xz);
		} catch (Exception e) {
		}
		return bdcs_sllm_ls;
	}

	/**
	 * 将宗海从现状层拷贝到历史层
	 * 
	 * @作者 海豹
	 * @创建时间 2015年6月26日下午4:15:04
	 * @param bdcs_zh_xz
	 * @return
	 */
	public static BDCS_ZH_LS copyZH_XZToLS(BDCS_ZH_XZ bdcs_zh_xz) {
		BDCS_ZH_LS bdcs_zh_ls = new BDCS_ZH_LS();
		try {
			PropertyUtils.copyProperties(bdcs_zh_ls, bdcs_zh_xz);
		} catch (Exception e) {
		}
		return bdcs_zh_ls;
	}

	/**
	 * 将用海用地从现状层拷贝到历史层
	 * 
	 * @作者 海豹
	 * @创建时间 2015年6月26日下午4:15:19
	 * @param bdcs_yhzk_xz
	 * @return
	 */
	public static BDCS_YHZK_LS copyYHZK_XZToLS(BDCS_YHZK_XZ bdcs_yhzk_xz) {
		BDCS_YHZK_LS bdcs_yhzk_ls = new BDCS_YHZK_LS();
		try {
			PropertyUtils.copyProperties(bdcs_yhzk_ls, bdcs_yhzk_xz);
		} catch (Exception e) {
		}
		return bdcs_yhzk_ls;
	}

	/**
	 * 将用海用地坐标从现状层拷贝到历史层
	 * 
	 * @作者 海豹
	 * @创建时间 2015年6月26日下午4:15:35
	 * @param bdcs_yhydzb_xz
	 * @return
	 */
	public static BDCS_YHYDZB_LS copyYHYDZB_XZToLS(BDCS_YHYDZB_XZ bdcs_yhydzb_xz) {
		BDCS_YHYDZB_LS bdcs_yhydzb_ls = new BDCS_YHYDZB_LS();
		try {
			PropertyUtils.copyProperties(bdcs_yhydzb_ls, bdcs_yhydzb_xz);
		} catch (Exception e) {
		}
		return bdcs_yhydzb_ls;
	}

	/**
	 * 将自然幢从现状层拷贝到历史层
	 * 
	 * @作者：俞学斌
	 * @param bdcs_zrz_xz
	 *            现状层自然幢
	 * @return
	 */
	public static BDCS_ZRZ_LS copyZRZ_XZToLS(BDCS_ZRZ_XZ bdcs_zrz_xz) {
		BDCS_ZRZ_LS bdcs_zrz_ls = new BDCS_ZRZ_LS();
		try {
			PropertyUtils.copyProperties(bdcs_zrz_ls, bdcs_zrz_xz);
		} catch (Exception e) {
		}
		return bdcs_zrz_ls;
	}

	/**
	 * 将户从现状层拷贝到历史层
	 * 
	 * @作者：俞学斌
	 * @param bdcs_h_xz
	 *            现状层户
	 * @return
	 */
	public static BDCS_H_LS copyH_XZToLS(BDCS_H_XZ bdcs_h_xz) {
		BDCS_H_LS bdcs_h_ls = new BDCS_H_LS();
		try {
			PropertyUtils.copyProperties(bdcs_h_ls, bdcs_h_xz);
		} catch (Exception e) {
		}
		return bdcs_h_ls;
	}

	/**
	 * 从申请人拷贝到权利人
	 * 
	 * @作者 刘树峰
	 * @创建时间 2015年6月13日上午3:23:20
	 * @param sqr
	 * @return
	 */
	public static BDCS_QLR_GZ CopySQRtoQLR(BDCS_SQR sqr) {
		BDCS_QLR_GZ qlr = new BDCS_QLR_GZ();
		if (sqr != null) {
			qlr.setQLRMC(sqr.getSQRXM());// 权利人名称
			qlr.setZJZL(sqr.getZJLX());// 证件种类
			qlr.setZJH(sqr.getZJH());// 证件号
			qlr.setDH(sqr.getLXDH());// 电话
			// qlr.setGYQK(sqr.getg);//共有情况
			qlr.setGYFS(sqr.getGYFS());// 共有方式
			qlr.setFZJG(sqr.getFZJG());// 发证机关
			qlr.setGJ(sqr.getGJDQ());// 国家地区
			qlr.setDZ(sqr.getTXDZ());// 地址
			qlr.setGZDW(sqr.getGZDW());// 工作单位
			qlr.setXB(sqr.getXB());// 性别
			qlr.setHJSZSS(sqr.getHJSZSS());// 户籍所在省市
			qlr.setSSHY(sqr.getSSHY() == null ? "" : sqr.getSSHY().toString());// 所属行业
			qlr.setQLRLX(sqr.getSQRLX());// 权利人类型
			qlr.setQLBL(sqr.getQLBL());// 权利比例

			qlr.setDLRXM(sqr.getDLRXM());
			qlr.setDLRLXDH(sqr.getDLRLXDH());
			qlr.setDLRZJHM(sqr.getDLRZJHM());
			qlr.setDLRZJLX(sqr.getDLRZJLX());
			qlr.setDLJGMC(sqr.getDLJGMC());
			qlr.setYXBZ(sqr.getYXBZ());
			qlr.setQLID(sqr.getId());
			qlr.setFDDBR(sqr.getFDDBR());
			qlr.setFDDBRDH(sqr.getFDDBRDH());
			qlr.setFDDBRZJLX(sqr.getFDDBRZJLX());
			qlr.setSQRID(sqr.getId());
			qlr.setDZYJ(sqr.getDZYJ());
			qlr.setYB(sqr.getYZBM());
			qlr.setISCZR(sqr.getISCZR());
			if (!StringUtils.isEmpty(sqr.getSXH())) {
				qlr.setSXH(Integer.valueOf(sqr.getSXH()));
			}
			qlr.setCREATETIME(sqr.getCREATETIME());
			qlr.setMODIFYTIME(sqr.getMODIFYTIME());
			qlr.setNATION(sqr.getNATION());
			qlr.setDLRNATION(sqr.getDLRNATION());
			try {
				double d = Double.valueOf(sqr.getQLMJ()).doubleValue();
				qlr.setQLMJ(d);// 权利面积
			} catch (Exception e) {

			}
		}
		return qlr;
	}

	/**
	 * 根据调查库里边的权利人生成申请人信息
	 * 
	 * @作者 刘树峰
	 * @创建时间 2015年6月30日上午1:19:54
	 * @param qlr
	 * @return
	 */
	public static BDCS_SQR copyDCQLRtoSQR(DCS_QLR_GZ qlr) {
		BDCS_SQR sqr = new BDCS_SQR();
		if (qlr != null) {
			sqr.setSQRXM(qlr.getQLRMC());
			sqr.setZJLX(qlr.getZJZL());
			sqr.setZJH(qlr.getZJH());
			sqr.setLXDH(qlr.getDH());
			sqr.setGYFS(qlr.getGYFS());
			sqr.setFZJG(qlr.getFZJG());
			sqr.setGJDQ(qlr.getGJ());
			sqr.setTXDZ(qlr.getDZ());
			sqr.setGZDW(qlr.getGZDW());
			sqr.setXB(qlr.getXB());
			sqr.setHJSZSS(qlr.getHJSZSS());
			sqr.setSSHY(qlr.getSSHY());
			sqr.setSQRLX(qlr.getQLRLX());
			sqr.setQLBL(qlr.getQLBL());
			sqr.setSQRLB("1");// 甲方

			sqr.setYXBZ(qlr.getYXBZ());
			sqr.setFDDBR(qlr.getFDDBR());
			sqr.setFDDBRDH(qlr.getFDDBRDH());
			sqr.setDLRXM(qlr.getDLRXM());
			sqr.setDLRLXDH(qlr.getDLRLXDH());
			sqr.setDLRZJHM(qlr.getDLRZJHM());
			sqr.setDLRZJLX(qlr.getDLRZJLX());
			sqr.setDLJGMC(qlr.getDLJGMC());
			if (qlr.getQLMJ() != null) {
				sqr.setQLMJ(String.valueOf(qlr.getQLMJ()));
			}
			sqr.setFDDBRZJLX(qlr.getFDDBRZJLX());
			// sqr.setId(qlr.getSQRID());
			sqr.setDZYJ(qlr.getDZYJ());
			sqr.setYZBM(qlr.getYB());
			if (null != qlr.getSXH()) {
				sqr.setSXH(qlr.getSXH().toString());
			}
			sqr.setCREATETIME(qlr.getCREATETIME());
			sqr.setMODIFYTIME(qlr.getMODIFYTIME());

			/*
			 * try { double d = Double.valueOf(sqr.getQLMJ()).doubleValue();
			 * qlr.setQLMJ(d);// 权利面积 } catch (Exception e) { }
			 */
		}
		return sqr;
	}

	/**
	 * 将权利从工作层拷贝到现状层
	 * 
	 * @作者：俞学斌
	 * @创建时间 2015年6月19日 下午9:45:54
	 * @param bdcs_ql_xz
	 * @return
	 */
	public static BDCS_QL_GZ copyQL_XZToGZ(BDCS_QL_XZ bdcs_ql_xz) {
		BDCS_QL_GZ bdcs_ql_gz = new BDCS_QL_GZ();
		try {
			// CopyProperties(bdcs_ql_gz,bdcs_ql_xz);
			PropertyUtils.copyProperties(bdcs_ql_gz, bdcs_ql_xz);
			bdcs_ql_gz.setCASENUM("");
		} catch (Exception e) {
		}
		return bdcs_ql_gz;
	}

	/**
	 * 将权利人从现状层拷贝到工作层
	 * 
	 * @作者：俞学斌
	 * @创建时间 2015年6月19日 下午9:46:07
	 * @param bdcs_qlr_xz
	 * @return
	 */
	public static BDCS_QLR_GZ copyQLR_XZToGZ(BDCS_QLR_XZ bdcs_qlr_xz) {
		BDCS_QLR_GZ bdcs_qlr_gz = new BDCS_QLR_GZ();
		try {
			PropertyUtils.copyProperties(bdcs_qlr_gz, bdcs_qlr_xz);
		} catch (Exception e) {
		}
		return bdcs_qlr_gz;
	}

	/**
	 * 将附属权利从现状层拷贝到工作层
	 * 
	 * @作者：俞学斌
	 * @创建时间 2015年6月19日 下午9:48:13
	 * @param bdcs_fsql_xz
	 * @return
	 */
	public static BDCS_FSQL_GZ copyFSQL_XZToGZ(BDCS_FSQL_XZ bdcs_fsql_xz) {
		BDCS_FSQL_GZ bdcs_fsql_gz = new BDCS_FSQL_GZ();
		try {
			PropertyUtils.copyProperties(bdcs_fsql_gz, bdcs_fsql_xz);
		} catch (Exception e) {
		}
		return bdcs_fsql_gz;
	}

	/**
	 * 将登记单元从工作层拷贝到现状层
	 * 
	 * @作者：俞学斌
	 * @创建时间 2015年6月19日 下午9:54:29
	 * @param bdcs_djdy_xz
	 * @return
	 */
	public static BDCS_DJDY_GZ copyDJDY_XZToGZ(BDCS_DJDY_XZ bdcs_djdy_xz) {
		BDCS_DJDY_GZ bdcs_djdy_gz = new BDCS_DJDY_GZ();
		try {
			PropertyUtils.copyProperties(bdcs_djdy_gz, bdcs_djdy_xz);
		} catch (Exception e) {
		}
		return bdcs_djdy_gz;
	}

	/**
	 * 将证书从现状层拷贝到工作层
	 * 
	 * @作者：俞学斌
	 * @创建时间 2015年6月19日 下午10:17:17
	 * @param bdcs_zs_xz
	 * @return
	 */
	public static BDCS_ZS_GZ copyZS_XZToGZ(BDCS_ZS_XZ bdcs_zs_xz) {
		BDCS_ZS_GZ bdcs_zs_gz = new BDCS_ZS_GZ();
		try {
			PropertyUtils.copyProperties(bdcs_zs_gz, bdcs_zs_xz);
		} catch (Exception e) {
		}
		return bdcs_zs_gz;
	}

	/**
	 * 将权利-权利人-证书-单元关系从现状层拷贝到工作层
	 * 
	 * @作者：俞学斌
	 * @创建时间 2015年6月19日 下午10:17:53
	 * @param bdcs_qdzr_xz
	 * @return
	 */
	public static BDCS_QDZR_GZ copyQDZR_XZToGZ(BDCS_QDZR_XZ bdcs_qdzr_xz) {
		BDCS_QDZR_GZ bdcs_qdzr_gz = new BDCS_QDZR_GZ();
		try {
			PropertyUtils.copyProperties(bdcs_qdzr_gz, bdcs_qdzr_xz);
		} catch (Exception e) {
		}
		return bdcs_qdzr_gz;
	}

	/**
	 * 通过get、set方法获取属性并拷贝属性
	 * 
	 * @作者：俞学斌
	 * @创建时间 2015年6月22日 下午4:17:24
	 * @param obj1
	 * @param obj2
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unused")
	private static Object CopyProperties(Object source, Object target) throws Exception {
		Method[] methodSource = source.getClass().getMethods();
		Method[] methodTarget = target.getClass().getMethods();
		String methodNameSource;
		String methodFixSource;
		String methodNameTarget;
		String methodFixTarget;
		for (int i = 0; i < methodSource.length; i++) {
			methodNameSource = methodSource[i].getName();
			methodFixSource = methodNameSource.substring(3, methodNameSource.length());
			if (methodNameSource.startsWith("get")) {
				for (int j = 0; j < methodTarget.length; j++) {
					methodNameTarget = methodTarget[j].getName();
					methodFixTarget = methodNameTarget.substring(3, methodNameSource.length());
					if (methodNameTarget.startsWith("set")) {
						if (methodFixTarget.equals(methodFixSource)) {
							Object[] objsSource = new Object[0];
							Object[] objsTarget = new Object[1];
							objsTarget[0] = methodSource[i].invoke(source, objsSource);// 激活obj1的相应的get的方法，objs1数组存放调用该方法的参数,此例中没有参数，该数组的长度为0
							methodTarget[j].invoke(target, objsTarget);// 激活obj2的相应的set的方法，objs2数组存放调用该方法的参数
							continue;

						}
					}
				}
			}
		}
		return target;
	}

	/**
	 * 通过对象类获取属性名、通过get、set方法拷贝属性
	 * 
	 * @作者：俞学斌
	 * @创建时间 2015年6月22日 下午4:37:41
	 * @param target
	 * @param source
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unused")
	private static Object CopyPropertiesEx(Object target, Object source) throws Exception {
		/*
		 * 分别获得源对象和目标对象的Class类型对象,Class对象是整个反射机制的源头和灵魂！
		 * 
		 * Class对象是在类加载的时候产生,保存着类的相关属性，构造器，方法等信息
		 */
		Class<? extends Object> sourceClz = source.getClass();
		Class<? extends Object> targetClz = target.getClass();
		// 得到Class对象所表征的类的所有属性(包括私有属性)
		Field[] fields = sourceClz.getDeclaredFields();
		if (fields.length == 0) {
			fields = sourceClz.getSuperclass().getDeclaredFields();
		}
		for (int i = 0; i < fields.length; i++) {
			String fieldName = fields[i].getName();
			Field targetField = null;
			// 得到targetClz对象所表征的类的名为fieldName的属性，不存在就进入下次循环
			try {
				targetField = targetClz.getDeclaredField(fieldName);
			} catch (NoSuchFieldException e) {
				targetField = targetClz.getSuperclass().getDeclaredField(fieldName);
			}
			// 判断sourceClz字段类型和targetClz同名字段类型是否相同
			if (fields[i].getType() == targetField.getType()) {
				// 由属性名字得到对应get和set方法的名字
				String getMethodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);

				String setMethodName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
				// 由方法的名字得到get和set方法的Method对象
				Method getMethod;
				Method setMethod;
				try {
					try {
						getMethod = sourceClz.getDeclaredMethod(getMethodName, new Class[] {});

					} catch (NoSuchMethodException e) {
						getMethod = sourceClz.getSuperclass().getDeclaredMethod(getMethodName, new Class[] {});
					}

					try {
						setMethod = targetClz.getDeclaredMethod(setMethodName, fields[i].getType());

					} catch (NoSuchMethodException e) {
						setMethod = targetClz.getSuperclass().getDeclaredMethod(setMethodName, fields[i].getType());
					}
					// 调用source对象的getMethod方法
					Object result = getMethod.invoke(source, new Object[] {});
					// 调用target对象的setMethod方法
					setMethod.invoke(target, result);
				} catch (SecurityException e) {
					e.printStackTrace();

				} catch (NoSuchMethodException e) {
					e.printStackTrace();

				} catch (IllegalArgumentException e) {
					e.printStackTrace();

				} catch (IllegalAccessException e) {
					e.printStackTrace();

				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
			} else {
				throw new Exception("同名属性类型不匹配！");
			}
		}
		return target;
	}

	/**
	 * 拷贝两个对象
	 * @Title: copyObject
	 * @author:liushufeng
	 * @date：2015年7月12日 上午2:40:56
	 * @param srcObj
	 * @param desObj
	 * @return
	 */
	public static boolean copyObject(Object srcObj, Object desObj) {
		boolean bSuccess = false;
		try {
			PropertyUtils.copyProperties(desObj,srcObj);
			bSuccess = true;
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		return bSuccess;
	}
	
	/**
	 * 将对象转换成MAP
	 * @Title: transBean2Map
	 * @author:wuzhu
	 * @date：
	 * @param obj
	 * @return
	 *///将对象转换成MAP
	  public static Map<String, Object> transBean2Map(Object obj) {  
	    if(obj == null){  
	        return null;  
	    }          
	    Map<String, Object> map = new HashMap<String, Object>();  
	    try {  
	        BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());  
	        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();  
	        for (PropertyDescriptor property : propertyDescriptors) {  
	            String key = property.getName();  

	            // 过滤class属性  
	            if (!key.equals("class")) {  
	                // 得到property对应的getter方法  
	                Method getter = property.getReadMethod();  
	                Object value = getter.invoke(obj);  

	                map.put(key, value);  
	            }  

	        }  
	    } catch (Exception e) {  
	        System.out.println("对象转换为MAP失败： " + e);  
	    }  
	    return map;  
	  }
	
	/**
	 * @Description: list排序，使用本地化配置SQSPBDYSORTFIELDS，泛型T：Hashmap,bean实体类
	 * @Title: SortList
	 * @Author: zhaomengfan
	 * @Date: 2017年5月18日下午2:11:11
	 * @param list
	 * @return
	 * @return List<T>
	 */
	public static <T> List<T> SortList(List<T> list) {

		String Config = ConfigHelper.getNameByValue("SQSPBDYSORTFIELDS");
		if (StringHelper.isEmpty(Config)) {
			return list;
		}

		final Class<? extends Object> Class;
		if (list != null && list.size() > 0) {
			T o = list.get(0);
			Class = o.getClass();
		} else {
			return list;
		}
		final boolean mapflag = Class.equals(HashMap.class);
		
		final String[] Configs = Config.split(",");
		final Method[] getmethods = new Method[Configs.length];
		if(!mapflag){
			String getMethodName;
			Method[] methods = Class.getDeclaredMethods();
			for (int i = 0; i < Configs.length; i++) {
				getMethodName = "GET" + Configs[i].trim().split(" ")[0].toUpperCase();
			
				for (Method method : methods) {
					if (method.getName().toUpperCase().equals(getMethodName)) {
						getmethods[i] = method;
					}
				}
			}
		}
		Collections.sort(list, new Comparator<T>() {
			@Override
			public int compare(T o1, T o2) {
				String a = "";
				String b = "";

				if(!mapflag){
					int i = -1;
					for (Method method : getmethods) {
						i++;
						if(method==null)
							continue;
						try {
							// 这里get方法返回的应该都是String才是
							a = StringHelper.formatObject(method.invoke(o1, new Object[] {}));
							b = StringHelper.formatObject(method.invoke(o2, new Object[] {}));
							if((StringHelper.isEmpty(a)&&StringHelper.isEmpty(b))
									||(!StringHelper.isEmpty(a)&&!StringHelper.isEmpty(b)&&a.equals(b))) {
								continue;
							}
							if (StringHelper.isEmpty(a) || !StringHelper.isEmpty(b) && cat(a,b) < 0) {
								return -1;
							}
							if (StringHelper.isEmpty(b) || !StringHelper.isEmpty(a) && cat(b,a) < 0) {
								return 1;
							}
						} catch (IllegalArgumentException e) {
						} catch (IllegalAccessException e) {
						} catch (InvocationTargetException e) {
						}
					}
					return 0;
				}else{
					try {
						Method get = Class.getDeclaredMethod("get", new Object().getClass());
						for (String Config : Configs) {
							// 这里get方法返回的应该都是String才是
							Config = Config.trim().split(" ")[0];
							
							try {
								a = StringHelper.formatObject(get.invoke(o1, Config));
								b = StringHelper.formatObject(get.invoke(o2, Config));
								if((StringHelper.isEmpty(a)&&StringHelper.isEmpty(b))
										||(!StringHelper.isEmpty(a)&&!StringHelper.isEmpty(b)&&a.equals(b))) {
									continue;
								}
								if (StringHelper.isEmpty(a) || !StringHelper.isEmpty(b) && cat(a,b) < 0) {
									return -1;
								}
								if (StringHelper.isEmpty(b) || !StringHelper.isEmpty(a) && cat(b,a) < 0) {
									return 1;
								}
							} catch (IllegalArgumentException e) {
							} catch (IllegalAccessException e) {
							} catch (InvocationTargetException e) {
							}
						}
						return 0;
					} catch (SecurityException e) {
					} catch (NoSuchMethodException e) {
					}
				}
				return 0;
			}
		});
		return list;
	}
	
	public static <T> List<T> SortList(List<T> list,String[] Config) {

		if (StringHelper.isEmpty(Config)) {
			return list;
		}

		final Class<? extends Object> Class;
		if (list != null && list.size() > 0) {
			T o = list.get(0);
			Class = o.getClass();
		} else {
			return list;
		}
		final boolean mapflag = Class.equals(HashMap.class);
		
		final String[] Configs = Config;
		final Method[] getmethods = new Method[Configs.length];
		if(!mapflag){
			String getMethodName;
			Method[] methods = Class.getDeclaredMethods();
			for (int i = 0; i < Configs.length; i++) {
				getMethodName = "GET" + Configs[i].trim().toUpperCase();
				for (Method method : methods) {
					if (method.getName().toUpperCase().equals(getMethodName)) {
						getmethods[i] = method;
					}
				}
			}
		}
		Collections.sort(list, new Comparator<T>() {
			@Override
			public int compare(T o1, T o2) {
				String a = "";
				String b = "";

				if(!mapflag){
					for (Method method : getmethods) {
						if(method==null)
							continue;
						try {
							// 这里get方法返回的应该都是String才是
							a = StringHelper.formatObject(method.invoke(o1, new Object[] {}));
							b = StringHelper.formatObject(method.invoke(o2, new Object[] {}));
							if((StringHelper.isEmpty(a)&&StringHelper.isEmpty(b))
									||(!StringHelper.isEmpty(a)&&!StringHelper.isEmpty(b)&&a.equals(b))) {
								continue;
							}
							if (StringHelper.isEmpty(a) || !StringHelper.isEmpty(b) && cat(a,b) < 0) {
								return -1;
							}
							if (StringHelper.isEmpty(b) || !StringHelper.isEmpty(a) && cat(b,a) < 0) {
								return 1;
							}
						} catch (IllegalArgumentException e) {
						} catch (IllegalAccessException e) {
						} catch (InvocationTargetException e) {
						}
					}
					return 0;
				}else{
					try {
						Method get = Class.getDeclaredMethod("get", new Object().getClass());
						for (String Config : Configs) {
							// 这里get方法返回的应该都是String才是
							Config = Config.trim();
							try {
								a = StringHelper.formatObject(get.invoke(o1, Config));
								b = StringHelper.formatObject(get.invoke(o2, Config));
								if((StringHelper.isEmpty(a)&&StringHelper.isEmpty(b))
										||(!StringHelper.isEmpty(a)&&!StringHelper.isEmpty(b)&&a.equals(b))) {
									continue;
								}
								if (StringHelper.isEmpty(a) || !StringHelper.isEmpty(b) && cat(a,b) < 0) {
									return -1;
								}
								if (StringHelper.isEmpty(b) || !StringHelper.isEmpty(a) && cat(b,a) < 0) {
									return 1;
								}
							} catch (IllegalArgumentException e) {
							} catch (IllegalAccessException e) {
							} catch (InvocationTargetException e) {
							}
						}
						return 0;
					} catch (SecurityException e) {
					} catch (NoSuchMethodException e) {
					}
				}
				return 0;
			}
		});
		return list;
	}
	
	public static int cat(String a, String b){
		int ia = 0, ib = 0;
		int nza = 0, nzb = 0;
		char ca, cb;
		int result;

		while (true) {
			// only count the number of zeroes leading the last number
			// compared
			nza = nzb = 0;

			ca = charAt(a, ia);
			cb = charAt(b, ib);

			// skip over leading zeros
			while (ca == '0') {
				if (ca == '0') {
					nza++;
				} else {
					// only count consecutive zeroes
					nza = 0;
				}

				// if the next character isn't a digit, then we've had a
				// run of only zeros
				// we still need to treat this as a 0 for comparison
				// purposes
				if (!Character.isDigit(charAt(a, ia + 1)))
					break;

				ca = charAt(a, ++ia);
			}

			while (cb == '0') {
				if (cb == '0') {
					nzb++;
				} else {
					// only count consecutive zeroes
					nzb = 0;
				}

				// if the next character isn't a digit, then we've had a
				// run of only zeros
				// we still need to treat this as a 0 for comparison
				// purposes
				if (!Character.isDigit(charAt(b, ib + 1)))
					break;

				cb = charAt(b, ++ib);
			}

			// process run of digits
			if (Character.isDigit(ca) && Character.isDigit(cb)) {
				if ((result = compareRight(a.substring(ia), b.substring(ib))) != 0) {
					return result;
				}
			}

			if (ca == 0 && cb == 0) {
				// The strings compare the same. Perhaps the caller
				// will want to call strcmp to break the tie.
				return nza - nzb;
			}

			if (ca < cb) {
				return -1;
			} else if (ca > cb) {
				return +1;
			}

			++ia;
			++ib;
		}
	}
    private static int compareRight(String a, String b) {
        int bias = 0;
        int ia = 0;
        int ib = 0;
        
        // The longest run of digits wins.  That aside, the greatest
        // value wins, but we can't know that it will until we've scanned
        // both numbers to know that they have the same magnitude, so we
        // remember it in BIAS.
        for (;; ia++, ib++) {
            char ca = charAt(a, ia);
            char cb = charAt(b, ib);
            
            if (!Character.isDigit(ca) && !Character.isDigit(cb)) {
                return bias;
            }
            else if (!Character.isDigit(ca)) {
                return -1;
            }
            else if (!Character.isDigit(cb)) {
                return +1;
            }
            else if (ca < cb) {
                if (bias == 0) {
                    bias = -1;
                }
            }
            else if (ca > cb) {
                if (bias == 0)
                    bias = +1;
            }
            else if (ca == 0 && cb == 0) {
                return bias;
            }
        }
    }
    private static char charAt(String s, int i) {
        if (i >= s.length()) {
            return 0;
        }
        else {
            return Character.toUpperCase(s.charAt(i));
        }
    }
    
    /**
	 * 用utf-8解析
	 * @author wanghongchao 
	 * @param parameterMap
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Map transToMAP(Map parameterMap) {
		// 返回值Map
		Map returnMap = new HashMap();
		Iterator entries = parameterMap.entrySet().iterator();
		Map.Entry entry;
		String name = "";
		String value = "";
		while (entries.hasNext()) {
			entry = (Map.Entry) entries.next();
			name = (String) entry.getKey();
			Object valueObj = entry.getValue();
			if (null == valueObj) {
				value = "";
			} else if (valueObj instanceof String[]) {
				try {
					String[] values = (String[]) valueObj;
					if(values.length>0){
						value = org.apache.commons.lang.StringUtils.join(values,",");
					}else{
						value = "";
					}
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				} 
 			} else { 
				value = valueObj.toString();
			}
			if(!StringUtils.isEmpty(value)){
				try {
					boolean isISO88591 = java.nio.charset.Charset.forName("iso8859-1").newEncoder().canEncode(value);//避免部分页面乱码
					if (isISO88591) {
						value = new String(value.getBytes("iso8859-1"), "utf-8");
					}
				} catch (Exception e) {
					// TODO: handle exception
					
				} 
			}
			returnMap.put(name, value); 
		}
		return returnMap;
	}
}