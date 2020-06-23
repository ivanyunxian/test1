package com.supermap.realestate.registration.dataExchange.utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.springframework.util.StringUtils;

import com.supermap.realestate.registration.ViewClass.ProjectInfo;
import com.supermap.realestate.registration.dataExchange.DJFDJFZ;
import com.supermap.realestate.registration.dataExchange.DJFDJGD;
import com.supermap.realestate.registration.dataExchange.DJFDJSF;
import com.supermap.realestate.registration.dataExchange.DJFDJSH;
import com.supermap.realestate.registration.dataExchange.DJFDJSJ;
import com.supermap.realestate.registration.dataExchange.DJFDJSQR;
import com.supermap.realestate.registration.dataExchange.DJFDJSZ;
import com.supermap.realestate.registration.dataExchange.DJTDJSLSQ;
import com.supermap.realestate.registration.dataExchange.Data;
import com.supermap.realestate.registration.dataExchange.FJF100;
import com.supermap.realestate.registration.dataExchange.ZTTGYQLR;
import com.supermap.realestate.registration.dataExchange.cfdj.QLFQLCFDJ;
import com.supermap.realestate.registration.dataExchange.dyq.QLFQLDYAQ;
import com.supermap.realestate.registration.dataExchange.fwsyq.KTTFWC;
import com.supermap.realestate.registration.dataExchange.fwsyq.KTTFWH;
import com.supermap.realestate.registration.dataExchange.fwsyq.KTTFWLJZ;
import com.supermap.realestate.registration.dataExchange.fwsyq.KTTFWZRZ;
import com.supermap.realestate.registration.dataExchange.fwsyq.QLFFWFDCQDZXM;
import com.supermap.realestate.registration.dataExchange.fwsyq.QLTFWFDCQDZ;
import com.supermap.realestate.registration.dataExchange.fwsyq.QLTFWFDCQYZ;
import com.supermap.realestate.registration.dataExchange.hy.KTFZHBHQK;
import com.supermap.realestate.registration.dataExchange.hy.KTFZHYHYDZB;
import com.supermap.realestate.registration.dataExchange.hy.KTFZHYHZK;
import com.supermap.realestate.registration.dataExchange.hy.KTTZHJBXX;
import com.supermap.realestate.registration.dataExchange.hy.QLFQLHYSYQ;
import com.supermap.realestate.registration.dataExchange.hy.ZHK105;
import com.supermap.realestate.registration.dataExchange.lq.QLTQLLQ;
import com.supermap.realestate.registration.dataExchange.nydsyq.QLNYDSYQ;
import com.supermap.realestate.registration.dataExchange.qfsyq.QLFFWFDCQQFSYQ;
import com.supermap.realestate.registration.dataExchange.shyq.KTFZDBHQK;
import com.supermap.realestate.registration.dataExchange.shyq.KTTGYJZD;
import com.supermap.realestate.registration.dataExchange.shyq.KTTGYJZX;
import com.supermap.realestate.registration.dataExchange.shyq.KTTZDJBXX;
import com.supermap.realestate.registration.dataExchange.shyq.QLFQLJSYDSYQ;
import com.supermap.realestate.registration.dataExchange.shyq.ZDK103;
import com.supermap.realestate.registration.dataExchange.syq.QLFQLTDSYQ;
import com.supermap.realestate.registration.dataExchange.xzdy.QLFXZDY;
import com.supermap.realestate.registration.dataExchange.ygdj.QLFQLYGDJ;
import com.supermap.realestate.registration.dataExchange.yydj.QLFQLYYDJ;
import com.supermap.realestate.registration.dataExchange.zxdj.QLFZXDJ;
import com.supermap.realestate.registration.model.BDCS_DJFZ;
import com.supermap.realestate.registration.model.BDCS_DJGD;
import com.supermap.realestate.registration.model.BDCS_DJSF;
import com.supermap.realestate.registration.model.BDCS_DJSZ;
import com.supermap.realestate.registration.model.BDCS_DYBG;
import com.supermap.realestate.registration.model.BDCS_DYXZ;
import com.supermap.realestate.registration.model.BDCS_FSQL_GZ;
import com.supermap.realestate.registration.model.BDCS_H_GZ;
import com.supermap.realestate.registration.model.BDCS_H_LS;
import com.supermap.realestate.registration.model.BDCS_H_LSY;
import com.supermap.realestate.registration.model.BDCS_H_XZ;
import com.supermap.realestate.registration.model.BDCS_H_XZY;
import com.supermap.realestate.registration.model.BDCS_QLR_GZ;
import com.supermap.realestate.registration.model.BDCS_QL_GZ;
import com.supermap.realestate.registration.model.BDCS_SHYQZD_XZ;
import com.supermap.realestate.registration.model.BDCS_SQR;
import com.supermap.realestate.registration.model.BDCS_XMXX;
import com.supermap.realestate.registration.model.interfaces.AgriculturalLand;
import com.supermap.realestate.registration.model.interfaces.Building;
import com.supermap.realestate.registration.model.interfaces.Floor;
import com.supermap.realestate.registration.model.interfaces.Forest;
import com.supermap.realestate.registration.model.interfaces.House;
import com.supermap.realestate.registration.model.interfaces.LogicBuilding;
import com.supermap.realestate.registration.model.interfaces.OwnerLand;
import com.supermap.realestate.registration.model.interfaces.RealUnit;
import com.supermap.realestate.registration.model.interfaces.Rights;
import com.supermap.realestate.registration.model.interfaces.RightsHolder;
import com.supermap.realestate.registration.model.interfaces.Sea;
import com.supermap.realestate.registration.model.interfaces.SubRights;
import com.supermap.realestate.registration.model.interfaces.TDYT;
import com.supermap.realestate.registration.model.interfaces.UseLand;
import com.supermap.realestate.registration.model.interfaces.YHZK;
import com.supermap.realestate.registration.service.impl.ProjectServiceImpl;
import com.supermap.realestate.registration.tools.OperateFeature;
import com.supermap.realestate.registration.util.ConfigHelper;
import com.supermap.realestate.registration.util.ConstHelper;
import com.supermap.realestate.registration.util.ConstValue;
import com.supermap.realestate.registration.util.ConstValue.BDCDYLX;
import com.supermap.realestate.registration.util.ConstValue.DJDYLY;
import com.supermap.realestate.registration.util.ConstValue.QLLX;
import com.supermap.realestate.registration.util.Global;
import com.supermap.realestate.registration.util.ProjectHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.services.components.commontypes.Feature;
import com.supermap.services.components.commontypes.Geometry;
import com.supermap.wisdombusiness.core.SuperSpringContext;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import com.supermap.wisdombusiness.web.Message;
import com.supermap.wisdombusiness.workflow.model.Wfi_MaterData;
import com.supermap.wisdombusiness.workflow.model.Wfi_ProMater;
import com.supermap.wisdombusiness.workflow.service.common.Approval;
import com.supermap.wisdombusiness.workflow.service.common.Common;
import com.supermap.wisdombusiness.workflow.service.wfm.SmProSPService;

/**
 * @Description:
 * @author diaoliwei
 * @date 2015年9月4日 下午8:46:09
 * @Copyright SuperMap
 */
public class packageXml {

	private static int bsm = 0000000001;
	
	
	
	/**
	 *  根据农用地信填充和维护基本信息
	 * @param nyd 农用地
	 * @param jbxx 宗地基本信息
	 * @param ql 
	 * @return  宗地基本信息
	 */
	public static KTTZDJBXX getZDJBXXByNYD(AgriculturalLand nyd,KTTZDJBXX jbxx, BDCS_QL_GZ ql) {
		if(nyd!=null&&jbxx!=null) {
			//1.标识码
			bsm = bsm++;
			jbxx.setBSM(bsm);
			//2.要素代码
			if(!StringHelper.isEmpty(nyd.getYSDM())) {
				jbxx.setYSDM(StringHelper.formatObject(nyd.getYSDM()));
			} else {
				jbxx.setYSDM("2002020400");
			}
			//3.宗地代码
			jbxx.setZDDM(StringHelper.formatObject(nyd.getZDDM()));
			//4.BDCDYH不动产单元号
			jbxx.setBDCDYH(StringHelper.formatObject(nyd.getBDCDYH()));
			//5.ZDTZM宗地特征码
			jbxx.setZDTZM(StringHelper.formatObject(nyd.getZDTZM()));
			//6.坐落
			jbxx.setZL(StringHelper.formatObject(nyd.getZL()));
			//7.面积
			jbxx.setZDMJ(new BigDecimal(nyd.getCBMJ()));
			//8.面积单位
			jbxx.setMJDW(StringHelper.formatObject("2"));
//			宗地四至-东ZDSZD
			jbxx.setZDSZD(StringHelper.formatObject(nyd.getZDSZD()));
//			宗地四至-南ZDSZN
			jbxx.setZDSZN(StringHelper.formatObject(nyd.getZDSZN()));
//			宗地四至-西
			jbxx.setZDSZX(StringHelper.formatObject(nyd.getZDSZX()));
//			宗地四至-北
			jbxx.setZDSZB(StringHelper.formatObject(nyd.getZDSZB()));
//			宗地图
			if(!StringHelper.isEmpty(nyd.getZDT())) {
				jbxx.setZDT(nyd.getZDT());
			}
//			图幅号
			jbxx.setTFH(StringHelper.formatObject(nyd.getTFH()));
//			备注
			jbxx.setBZ(StringHelper.formatObject(nyd.getBZ()));
//			状态
			if (!StringHelper.isEmpty(nyd.getZT())) {
				jbxx.setZT(StringHelper.formatObject(nyd.getZT()));
			}else {
				jbxx.setZT("1");
			}
			
//			区县代码
			ql.setQXDM(getQXDM(nyd));
//			登记时间
			jbxx.setDJSJ(StringHelper.FormatDateOnType(ql.getDJSJ(),"yyyy-MM-dd'T'HH:mm:ss"));
//			登簿人
			jbxx.setDBR(StringHelper.formatObject(ql.getDBR()));
//			附记
			jbxx.setFJ(StringHelper.formatObject(ql.getFJ()));
//			登记机构编码
			jbxx.setDJJGBM(ConfigHelper.getNameByValue("XZQHDM"));
//			登记机构名称
			jbxx.setDJJGMC(ConfigHelper.getNameByValue("DJJGMC"));// 登记机构名称
//			街道 （乡、镇）
			jbxx.setJDH(StringHelper.formatObject(nyd.getDJZQDM()));
//			街坊（村）
			jbxx.setJFH(StringHelper.formatObject(nyd.getDJQDM()));
//			组
			if(!StringHelper.isEmpty(nyd.getZH())) {
				jbxx.setZH(nyd.getZH());
			}
			jbxx.setZH(StringHelper.formatObject(nyd.getZH()));
			//权利类型
			jbxx.setQLLX(StringHelper.formatObject(ql.getQLLX()));
			
			//数据维护
			if(StringHelper.isEmpty(jbxx.getZDT())) {
				jbxx.setZDT("无");
			}
			if(StringHelper.isEmpty(jbxx.getZH())) {
				jbxx.setZH("无");
			}
			
			jbxx.setBDCDYID(StringHelper.formatObject(ql.getBDCDYID()));
			
			jbxx.replaceEmpty();//移除空记录
			return jbxx;
		}
		return null;
		
	}

	/**
	 * 宗地基本信息
	 * 
	 * @作者 diaoliwei
	 * @创建时间 2015年8月29日下午4:21:00
	 * @param jbxx
	 * @param zd
	 * @param ql
	 * @return
	 */
	
	public static KTTZDJBXX getZDJBXX(KTTZDJBXX jbxx, UseLand zd, Rights ql, OwnerLand oland, Forest sllm) {
		jbxx.setZDDM("无");
		jbxx.setBDCDYH("无");
		jbxx.setQXDM("");
		jbxx.setYSDM("2001010000");
		jbxx.setZL("无");
		jbxx.setMJDW("1");
		jbxx.setYT("无");
		jbxx.setDJ("无");
		jbxx.setQLLX("");
		jbxx.setQLXZ("");
		jbxx.setZDSZD("无");
		jbxx.setZDSZN("无");
		jbxx.setZDSZX("无");
		jbxx.setZDSZB("无");
		jbxx.setZDT("无");
		jbxx.setZT("1");
		jbxx.setDBR("无");
		jbxx.setZDMJ(new BigDecimal(0));
		bsm = bsm++;
		jbxx.setBSM(bsm);// 标识码
		jbxx.setDAH("无");// 档案号
		jbxx.setDJJGBM(ConfigHelper.getNameByValue("XZQHDM"));// 登记机构编码
		jbxx.setDJJGMC(ConfigHelper.getNameByValue("DJJGMC"));// 登记机构名称
		jbxx.setJDH("无");// 街道
		jbxx.setJFH("无");// 街坊（村）
		jbxx.setZH("无");// 组
		jbxx.setMJDW("1");
		jbxx.setDJSJ(StringHelper.FormatDateOnType(ql.getDJSJ(),"yyyy-MM-dd'T'HH:mm:ss"));
		if (null != zd) {
			jbxx.setZDDM(StringHelper.formatDefaultValue(zd.getZDDM()));
			jbxx.setBDCDYH(StringHelper.formatDefaultValue(zd.getBDCDYH()));
			//jbxx.setQXDM(StringHelper.formatObject(zd.getQXDM()) == "" ? ConfigHelper.getNameByValue("XZQHDM") : zd.getQXDM());
			jbxx.setQXDM(getQXDM(zd));
			jbxx.setJFH(StringHelper.formatObject(zd.getDJQDM()) == "" ? "无" : zd.getDJQDM());
			jbxx.setJDH(StringHelper.formatObject(zd.getDJZQDM()) == "" ? "无" : zd.getDJZQDM());
			jbxx.setZDTZM(StringHelper.formatObject(zd.getZDTZM()));
			jbxx.setZL(StringHelper.formatDefaultValue(zd.getZL()));
			Double jg=StringHelper.getDouble(zd.getJG());//10000.0
			jbxx.setJG(StringHelper.cutBigDecimal(StringHelper.getBigDecimal(jg), 4));
			if(!StringHelper.isEmpty(zd.getMJDW())){
				if(oland!=null){
					if(!StringHelper.isEmpty(oland.getMJDW())){
						jbxx.setMJDW(oland.getMJDW());
					}
				}
			}
			if(zd.getTDYTS()!=null&&zd.getTDYTS().size()>0){
				for(TDYT tdyt:zd.getTDYTS()){
					if("1".equals(tdyt.getSFZYT())){
						jbxx.setYT(tdyt.getTDYT());
						jbxx.setDJ(tdyt.getTDDJ());
//						jbxx.setJG(StringHelper.cutBigDecimal(StringHelper.getBigDecimal(tdyt.getTDJG()), 4));
//						if(StringHelper.isEmpty(tdyt.getTDJG())){
//							jbxx.setJG(null);
//						}
						break;
					}
					if(StringHelper.isEmpty(jbxx.getYT())||"无".equals(jbxx.getYT())){
						jbxx.setYT(tdyt.getTDYT());
						jbxx.setDJ(tdyt.getTDDJ());
//						Double jg=StringHelper.getDouble(tdyt.getTDJG())/10000.0;
//						jbxx.setJG(StringHelper.cutBigDecimal(StringHelper.getBigDecimal(jg), 4));
//						jbxx.setJG(StringHelper.cutBigDecimal(StringHelper.getBigDecimal(tdyt.getTDJG()), 4));
//						if(StringHelper.isEmpty(tdyt.getTDJG())){
//							jbxx.setJG(null);
//						}
					}
				}
			}
			jbxx.setQLLX(StringHelper.formatObject(zd.getQLLX()) == "" ? StringHelper.formatObject(ql.getQLLX()) : StringHelper.formatObject(zd.getQLLX()));
			jbxx.setQLXZ(StringHelper.formatObject(zd.getQLXZ()));
			jbxx.setZDSZD(StringHelper.formatDefaultValue(zd.getZDSZD()));
			jbxx.setZDSZN(StringHelper.formatDefaultValue(zd.getZDSZN()));
			jbxx.setZDSZX(StringHelper.formatDefaultValue(zd.getZDSZX()));
			jbxx.setZDSZB(StringHelper.formatDefaultValue(zd.getZDSZB()));
			jbxx.setZDT(StringHelper.formatDefaultValue(zd.getZDT()));
			jbxx.setZT(StringHelper.formatObject(zd.getZT()) == "" ? "1" : zd.getZT());
			if (!StringUtils.isEmpty(zd.getQLSDFS())) {
				jbxx.setQLSDFS(StringHelper.formatObject(zd.getQLSDFS()));
			}
			jbxx.setTFH(StringHelper.formatObject(zd.getTFH()));
			jbxx.setDJH(StringHelper.formatObject(zd.getDJH()));
			jbxx.setZDMJ(StringHelper.cutBigDecimal(StringHelper.getBigDecimal(zd.getZDMJ()), 4));
			
			jbxx.setRJL(StringHelper.cutBigDecimal(StringHelper.getBigDecimal(zd.getRJL()), 2));// 容积率
			if(StringHelper.isEmpty(zd.getRJL())){
				jbxx.setRJL(null);
			}
			jbxx.setJZMD(StringHelper.cutBigDecimal(StringHelper.getBigDecimal(zd.getJZMD()), 2)); // 建筑密度
			if(StringHelper.isEmpty(zd.getJZMD())){
				jbxx.setJZMD(null);
			}
			jbxx.setJZXG(StringHelper.cutBigDecimal(StringHelper.getBigDecimal(zd.getJZXG()), 2)); // 建筑限高
			if(StringHelper.isEmpty(zd.getJZXG())){
				jbxx.setJZXG(null);
			}
			jbxx.setBDCDYID(StringHelper.formatObject(ql.getBDCDYID()));
			
		}
		if (null != oland) {
			jbxx.setZDDM(StringHelper.formatDefaultValue(oland.getZDDM()));
			jbxx.setBDCDYH(StringHelper.formatDefaultValue(oland.getBDCDYH()));
			jbxx.setQXDM(getQXDM(oland));
			jbxx.setZDTZM(StringHelper.formatDefaultValue(oland.getZDTZM()));
			jbxx.setZL(StringHelper.formatDefaultValue(oland.getZL()));
			jbxx.setMJDW(StringHelper.formatObject(oland.getMJDW())  == "" ? "1" : StringHelper.formatObject(oland.getMJDW()));
			jbxx.setYT(StringHelper.formatDefaultValue(oland.getYT()));
			if (!StringUtils.isEmpty(oland.getDJ())) {
				jbxx.setDJ(StringHelper.formatObject(oland.getDJ()));
			}
			jbxx.setQLLX(StringHelper.formatObject(oland.getQLLX()));
			jbxx.setQLXZ(StringHelper.formatObject(oland.getQLXZ()));
			jbxx.setZDSZD(StringHelper.formatDefaultValue(oland.getZDSZD()));
			jbxx.setZDSZN(StringHelper.formatDefaultValue(oland.getZDSZN()));
			jbxx.setZDSZX(StringHelper.formatDefaultValue(oland.getZDSZX()));
			jbxx.setZDSZB(StringHelper.formatDefaultValue(oland.getZDSZB()));
			jbxx.setZDT(StringHelper.formatDefaultValue(oland.getZDT()));
			jbxx.setZT(StringHelper.formatObject(oland.getZT()) == "" ? "1" : oland.getZT());
			if (!StringUtils.isEmpty(oland.getQLSDFS())) {
				jbxx.setQLSDFS(StringHelper.formatObject(oland.getQLSDFS()));
			}
			jbxx.setTFH(StringHelper.formatDefaultValue(oland.getTFH()));
			jbxx.setDJH(StringHelper.formatDefaultValue(oland.getDJH()));
			jbxx.setZDMJ(StringHelper.cutBigDecimal(StringHelper.getBigDecimal(oland.getZDMJ()), 4));
			Double jg=StringHelper.getDouble(oland.getJG())/10000.0;
			jbxx.setJG(StringHelper.cutBigDecimal(StringHelper.getBigDecimal(jg), 4));
//			jbxx.setJG(StringHelper.cutBigDecimal(StringHelper.getBigDecimal(oland.getJG()), 4));
			jbxx.setRJL(StringHelper.cutBigDecimal(
					StringHelper.getBigDecimal(oland.getRJL()), 2));// 容积率
			jbxx.setJZMD(StringHelper.cutBigDecimal(
					StringHelper.getBigDecimal(oland.getJZMD()), 2)); // 建筑密度
			jbxx.setJZXG(StringHelper.cutBigDecimal(
					StringHelper.getBigDecimal(oland.getJZXG()), 2)); // 建筑限高
			jbxx.setBDCDYID(StringHelper.formatObject(ql.getBDCDYID()));
		}
		
		if (null != ql) {
			if (!StringUtils.isEmpty(ql.getDBR())) {
				jbxx.setDBR(ql.getDBR());
			}
			if (!StringUtils.isEmpty(ql.getFJ())) {
				jbxx.setFJ(ql.getFJ());
			}
		}
		jbxx.replaceEmpty();//移除空记录
		return jbxx;
	}

	/**
	 * 房屋层
	 * 
	 * @作者 diaoliwei
	 * @创建时间 2015年9月7日上午10:07:54
	 * @param kttc
	 * @param c
	 * @return
	 */
	public static KTTFWC getKTTFWC(KTTFWC kttc, Floor c, KTTFWZRZ zrz) {
		kttc = new KTTFWC();
		kttc.setCh("无");
		kttc.setZrzh(zrz == null || zrz.getZrzh() == null ? "无" : zrz.getZrzh());
		kttc.setYsdm("2001030130");
		kttc.setQxdm(zrz.getQxdm());
		if (null != c) {
			kttc.setSjc(StringHelper.formatObject(c.getSJC()));
			kttc.setCjzmj(StringHelper.getDouble(c.getCJZMJ()));
			kttc.setCtnjzmj(StringHelper.getDouble(c.getCTNJZMJ()));
			kttc.setCytmj(StringHelper.getDouble(c.getCYTMJ()));
			kttc.setCgyjzmj(StringHelper.getDouble(c.getCGYJZMJ()));
			kttc.setCftjzmj(StringHelper.getDouble(c.getCFTJZMJ()));
			// kttc.setCbqmj(c.getb);
			kttc.setCg(StringHelper.getDouble(c.getCG()));
			kttc.setSptymj(StringHelper.getDouble(c.getSPTYMJ()));
			// kttc.setQxdm();
			kttc.setCh(StringHelper.formatObject(c.getCH()));
		}
		kttc.replaceEmpty();
		return kttc;
	}

	/**
	 * 自然幢
	 * 
	 * @作者 diaoliwei
	 * @创建时间 2015年9月7日上午10:08:56
	 * @param zrz
	 * @param zrz_
	 * @return
	 */
	public static KTTFWZRZ getKTTFWZRZ(KTTFWZRZ zrz, Building zrz_) {
		zrz.setBdcdyh("无");
		zrz.setZddm("无");
		zrz.setZrzh("无");
		zrz.setYsdm("2001030110");
		zrz.setXmmc("无");
		zrz.setZcs("");
		zrz.setZts("");
		zrz.setGhyt("");
		zrz.setFwjg("");
		zrz.setZt("1");
		//zrz.setQxdm("");
		zrz.setDah("无");
		bsm = bsm++;
		zrz.setBsm(bsm);
		if (null != zrz_) {
			zrz.setZcs(StringHelper.FormatByDatatype(zrz_.getZCS()));
			zrz.setZts(StringHelper.formatObject((zrz_.getZTS() == null ? "null" : zrz_.getZTS())));
			zrz.setBdcdyh(StringHelper.formatDefaultValue(zrz_.getBDCDYH()));
			zrz.setBz(StringHelper.formatDefaultValue(zrz_.getBZ()));
			zrz.setDscs(zrz_.getDSCS() == null ? 0 : zrz_.getDSCS());
			zrz.setDxcs(zrz_.getDXCS() == null ? 0 : zrz_.getDXCS());
			zrz.setDxsd(StringHelper.getDouble(zrz_.getDXSD()));
			zrz.setFwjg(StringHelper.formatObject(zrz_.getFWJG()));
			zrz.setGhyt(StringHelper.formatObject(zrz_.getGHYT()));
			zrz.setJgrq(null);
			if(!StringHelper.isEmpty(StringHelper.FormatDateOnType(zrz_.getJGRQ(),"yyyy-MM-dd'T'HH:mm:ss"))){
				zrz.setJgrq(StringHelper.FormatDateOnType(zrz_.getJGRQ(),"yyyy-MM-dd'T'HH:mm:ss"));
			}
			zrz.setJzwgd(StringHelper.cutBigDecimal(StringHelper.getBigDecimal(zrz_.getJZWGD()), 2));
			zrz.setJzwjbyt(StringHelper.formatDefaultValue(zrz_.getJZWJBYT()));
			zrz.setJzwmc(StringHelper.formatDefaultValue(zrz_.getJZWMC()));
			zrz.setQxdm(getQXDM(zrz_));
			zrz.setXmmc(StringHelper.formatDefaultValue(zrz_.getXMMC()));
			zrz.setZddm(StringHelper.formatDefaultValue(zrz_.getZDDM()));
			if (null != zrz_.getZRZH() && zrz_.getZRZH().length() > 20) {
				zrz.setZrzh(zrz_.getZRZH().substring(
						zrz_.getZRZH().length() - 20, zrz_.getZRZH().length()));
			}
			if (null != zrz_.getZRZH() && zrz_.getZRZH().length() <= 20) {
				zrz.setZrzh(zrz_.getZRZH());
			}
			//zrz.setZt(StringHelper.formatObject(zrz_.getZT()) == "" ? "1" : zrz_.getZT());
			zrz.setZydmj(zrz_.getZYDMJ() == null ? 1 : zrz_.getZYDMJ());
			zrz.setZzdmj(zrz_.getZZDMJ() == null ? 1 : zrz_.getZZDMJ());
			zrz.setYcjzmj(zrz_.getYCJZMJ() == null ? 1 : zrz_.getYCJZMJ());
			zrz.setScjzmj(zrz_.getSCJZMJ() == null ? 1 : zrz_.getSCJZMJ());
			zrz.setBdcdyid(StringHelper.formatObject(zrz_.getId()));
			zrz.setZdbdcdyid(StringHelper.formatObject(zrz_.getZDBDCDYID()));
		}
		zrz.replaceEmpty();
		return zrz;
	}

	/**
	 * 逻辑幢
	 * 
	 * @作者 diaoliwei
	 * @创建时间 2015年9月7日上午10:09:44
	 * @param ljz
	 * @param ljz_
	 * @return
	 */
	public static KTTFWLJZ getKTTFWLJZ(KTTFWLJZ ljz, LogicBuilding ljz_,RealUnit unit) {
		ljz.setLjzh("无");
		ljz.setZrzh(ljz_ == null || ljz_.getZRZH() == null || "".equals(ljz_.getZRZH()) ? "无" : ljz_.getZRZH());
		ljz.setYsdm("2001030120");
		ljz.setQxdm(getQXDM(unit));
		if (ljz_ != null) {
			ljz.setBz(StringHelper.formatDefaultValue(ljz_.getBZ()));
			if (ljz_.getDSCS() != null) {
				ljz.setDscs(ljz_.getDSCS());
			}
			if (ljz_.getDXCS() != null) {
				ljz.setDxcs(ljz_.getDXCS());
			}
			if (ljz_.getFWJG1() != null) {
				ljz.setFwjg1(ljz_.getFWJG1());
			}
			if (ljz_.getFWJG2() != null) {
				ljz.setFwjg2(ljz_.getFWJG2());
			}
			if (ljz_.getFWJG3() != null) {
				ljz.setFwjg3(ljz_.getFWJG3());
			}
			if (ljz_.getFWYT1() != null) {
				ljz.setFwyt1(ljz_.getFWYT1());
			}
			if (ljz_.getFWYT2() != null) {
				ljz.setFwyt2(ljz_.getFWYT2());
			}
			if (ljz_.getFWYT3() != null) {
				ljz.setFwyt3(ljz_.getFWYT3());
			}
			// ljz.setJgrq("");
			if (ljz_.getJZWZT() != null) {
				ljz.setJzwzt(ljz_.getJZWZT());
			}
			if (null != ljz_.getLJZH() && ljz_.getLJZH().length() > 20) {
				ljz.setLjzh(ljz_.getLJZH().substring(
						ljz_.getLJZH().length() - 20, ljz_.getLJZH().length()));
			}
			if (null != ljz_.getLJZH() && ljz_.getLJZH().length() <= 20) {
				ljz.setLjzh(ljz_.getLJZH());
			}
			if (ljz_.getMPH() != null) {
				ljz.setMph(ljz_.getMPH());
			}
			if (ljz_.getSCDXMJ() != null) {
				ljz.setScdxmj(ljz_.getSCDXMJ());
			}
			if (ljz_.getSCJZMJ() != null) {
				ljz.setScjzmj(ljz_.getSCJZMJ());
			}
			if (ljz_.getSCQTMJ() != null) {
				ljz.setScqtmj(ljz_.getSCQTMJ());
			}
			if (ljz_.getYCDXMJ() != null) {
				ljz.setYcdxmj(ljz_.getYCDXMJ());
			}
			if (ljz_.getYCJZMJ() != null) {
				ljz.setYcjzmj(ljz_.getYCJZMJ());
			}
			if (ljz_.getYCQTMJ() != null) {
				ljz.setYcqtmj(ljz_.getYCQTMJ());
			}
			if (ljz_.getYSDM() != null) {
				ljz.setYsdm(ljz_.getYSDM());
			}
			ljz.setZcs(StringHelper.FormatByDatatype(ljz_.getZCS()) == "" ? 0 : ljz_.getZCS());
			if (null != ljz_.getZRZH() && ljz_.getZRZH().length() > 20) {
				ljz.setZrzh(ljz_.getZRZH().substring(
						ljz_.getZRZH().length() - 20, ljz_.getZRZH().length()));
			}
			if (null != ljz_.getZRZH() && ljz_.getZRZH().length() <= 20) {
				ljz.setZrzh(ljz_.getZRZH());
			}
			ljz.setBdcdyid(StringHelper.formatObject(ljz_.getId()));
		}
		ljz.replaceEmpty();
		return ljz;
	}

	/**
	 * 界址线
	 * 
	 * @param jzx
	 * @return
	 */
	public static KTTGYJZX getKTTGYJZX(KTTGYJZX jzx, String zdzhdm) {
		KTTGYJZX jzxnew=new KTTGYJZX();
		bsm = bsm++;
		jzxnew.setBsm(bsm);
		if(jzx!=null) {
			if(!StringHelper.isEmpty(jzx.getYsdm())) {
				jzxnew.setYsdm(jzx.getYsdm());
			}else {
				jzxnew.setYsdm("2001060000");
			}
			if(!StringHelper.isEmpty(jzx.getJzxlb())) {
				jzxnew.setJzxlb(jzx.getJzxlb());
			}else {
				jzxnew.setJzxlb("2");
			}
			if(!StringHelper.isEmpty(jzx.getJzxcd())) {
				jzxnew.setJzxcd(jzx.getJzxcd());
			}else {
				jzxnew.setJzxcd(1);
			}
			if(!StringHelper.isEmpty(jzx.getJzxwz())) {
				jzxnew.setJzxwz(jzx.getJzxwz());
			}else {
				jzxnew.setJzxwz("1");
			}
			if(!StringHelper.isEmpty(jzx.getJxxz())) {
				jzxnew.setJxxz(jzx.getJxxz());
			}else {
				jzxnew.setJxxz("600001");
			}
				jzxnew.setQsjxxysbh(StringHelper.formatDefaultValue(jzx.getQsjxxysbh()));
				jzxnew.setQszyyysbh(StringHelper.formatDefaultValue(jzx.getQszyyysbh()));
				jzxnew.setZdzhdm(StringHelper.formatDefaultValue(zdzhdm));
				jzxnew.setQsjxxys(StringHelper.formatDefaultValue(jzx.getQsjxxys()));
				jzxnew.setQszyyys(StringHelper.formatDefaultValue(jzx.getQszyyys()));
			
		}else {
			jzxnew.setYsdm("2001060000");
			jzxnew.setJzxcd(1);
			jzxnew.setJzxlb("2");
			jzxnew.setJzxwz("1");
			jzxnew.setJxxz("600001");
			jzxnew.setQsjxxysbh("无");
			jzxnew.setQszyyysbh("无");
			jzxnew.setZdzhdm(zdzhdm == "" || zdzhdm == null ? "无" : zdzhdm);
			jzxnew.setQsjxxys("无");
			jzxnew.setQszyyys("无");
		}
		
		jzxnew.replaceEmpty();
		return jzxnew;
	}

	/**
	 * 界址点
	 * 
	 * @param jzd
	 * @return
	 */
	public static KTTGYJZD getKTTGYJZD(KTTGYJZD jzd, String zdzhdm) {
		KTTGYJZD kttgyjzd =new KTTGYJZD(); 
		if(jzd!=null) {
			if(!StringHelper.isEmpty(jzd.getYsdm())) {
				kttgyjzd.setYsdm(jzd.getYsdm());
			}else {
				kttgyjzd.setYsdm("2001070000");
			}
			kttgyjzd.setJzdh(StringHelper.formatDefaultValue(jzd.getJzdh()));
			if(!StringHelper.isEmpty(jzd.getJblx())) {
				kttgyjzd.setJblx(jzd.getJblx());
			}else {
				kttgyjzd.setJblx("3");
			}
			if(!StringHelper.isEmpty(jzd.getJzdlx())) {
				kttgyjzd.setJzdlx(jzd.getJzdlx());
			}else {
				kttgyjzd.setJzdlx("2");
			}
			kttgyjzd.setZdzhdm(StringHelper.formatDefaultValue(zdzhdm));
			
		}else {
			kttgyjzd.setYsdm("2001070000");
			kttgyjzd.setJzdh("无");
			kttgyjzd.setJblx("3");
			kttgyjzd.setJzdlx("2");
			kttgyjzd.setZdzhdm(zdzhdm == "" || zdzhdm == null ? "无" : zdzhdm);
			// jzd.setXzbz("");
		}
		
		bsm = bsm++;
		kttgyjzd.setBsm(bsm);
		// jzd.setYzbz("");
		// jzd.setSxh("");
		kttgyjzd.replaceEmpty();
		return kttgyjzd;
	}

	/**
	 * 房屋户
	 * 
	 * @作者 diaoliwei
	 * @创建时间 2015年9月7日上午10:10:49
	 * @param fwh
	 * @param h
	 * @return
	 */
	public static KTTFWH getKTTFWH(KTTFWH fwh, House h) {
		fwh.setBdcdyh("无");
		fwh.setFwbm("无");
		fwh.setYsdm("2001030140");
		fwh.setZl("无");
		fwh.setMjdw("1");
		fwh.setShbw("无");
		fwh.setZt("1");
		//fwh.setQxdm("");
		if (h != null) {
			fwh.setBdcdyh(StringHelper.formatDefaultValue(h.getBDCDYH()));
			fwh.setCh(StringHelper.formatDefaultValue(h.getCH()));
			fwh.setDytdmj(h.getDYTDMJ() == null ? 1 : h.getDYTDMJ());
			fwh.setFcfht(StringHelper.formatDefaultValue(h.getFCFHT()));
			fwh.setFttdmj(h.getFTTDMJ() == null ? 0 : h.getFTTDMJ());
			if (null != h.getFWBM() && h.getFWBM().length() > 26) {
				fwh.setFwbm(h.getFWBM().substring(h.getFWBM().length() - 26, h.getFWBM().length()));
			}
			if (null != h.getFWBM() && h.getFWBM().length() <= 26) {
				fwh.setFwbm(h.getFWBM());
			}
			if (!StringUtils.isEmpty(h.getFWLX())) {
				String fwlx=getFWLX(h.getFWLX());
				fwh.setFwlx(fwlx);
			}
			if (!StringUtils.isEmpty(h.getFWXZ())) {
				String fwxz=getFWXZ(h.getFWXZ());
				fwh.setFwxz(fwxz);
			}
			if (!StringUtils.isEmpty(h.getFWYT1())) {
//				if("85".equals(h.getFWYT1())){ //当时为松原做的处理。现在注释掉
//					fwh.setFwyt1("10");
//				}else{
					fwh.setFwyt1(getFWYT(h.getFWYT1()));
//				}
			}
			if (!StringUtils.isEmpty(h.getFWYT2())) {
				fwh.setFwyt2(getFWYT(h.getFWYT2()));
			}
			if (!StringUtils.isEmpty(h.getFWYT3())) {
				fwh.setFwyt3(getFWYT(h.getFWYT3()));
			}
			fwh.setGytdmj(h.getGYTDMJ() == null ? 0 : h.getGYTDMJ());
			fwh.setHh(StringHelper.FormatByDatatype(h.getHH()));
			fwh.setHx(h.getHX());
			if (!StringUtils.isEmpty(h.getHXJG())) {
				fwh.setHxjg(StringHelper.formatObject(h.getHXJG()));
			}
			if (null != h.getLJZH()) {
				if (h.getLJZH().length() > 20) {
					fwh.setLjzh(h.getLJZH() == null ? "" : h.getLJZH().substring(h.getLJZH().length() - 20, h.getLJZH().length()));
				}
			}
			if (null != h.getLJZH()) {
				if (h.getLJZH().length() <= 20) {
					fwh.setLjzh(h.getLJZH());
				}
			}
			fwh.setMjdw(StringHelper.formatObject(h.getMJDW())  == "" ? "1" : StringHelper.formatObject(h.getMJDW()));
			fwh.setQxdm(getQXDM(h));
			fwh.setScdxbfjzmj(h.getSCDXBFJZMJ() == null ? 0 : h.getSCDXBFJZMJ());
			fwh.setScftjzmj(h.getSCFTJZMJ() == null ? 0 : h.getSCFTJZMJ());
			if (h.getSCFTXS() != null) {
				fwh.setScftxs(h.getSCFTXS());
			}
			if (h.getSCJZMJ() != null) {
				fwh.setScjzmj(h.getSCJZMJ());
			}
			if (h.getSCQTJZMJ() != null) {
				fwh.setScqtjzmj(h.getSCQTJZMJ());
			}
			if (h.getSCTNJZMJ() != null) {
				fwh.setSctnjzmj(h.getSCTNJZMJ());
			}
			if (h.getSHBW() != null) {
				fwh.setShbw(h.getSHBW());
			}
			 if (h.getZZC() != null && h.getQSC()!=null) {
				fwh.setSjcs((double)(h.getZZC()-h.getQSC()+1));
			} 
			if (h.getYCDXBFJZMJ() != null) {
				fwh.setYcdxbfjzmj(h.getYCDXBFJZMJ());
			}
			if (h.getYCFTJZMJ() != null) {
				fwh.setYcftjzmj(h.getYCFTJZMJ());
			}
			if (h.getYCFTXS() != null) {
				fwh.setYcftxs(h.getYCFTXS());
			}
			if (h.getYCJZMJ() != null) {
				fwh.setYcjzmj(h.getYCJZMJ());
			}
			if (h.getYCQTJZMJ() != null) {
				fwh.setYcqtjzmj(h.getYCQTJZMJ());
			}
			if (h.getYCTNJZMJ() != null) {
				fwh.setYctnjzmj(h.getYCTNJZMJ());
			}
			if (h.getZL() != null) {
				fwh.setZl(h.getZL());
			}
			if (null != h.getZRZH() && h.getZRZH().length() > 20) {
				fwh.setZrzh(h.getZRZH().substring(h.getZRZH().length() - 20, h.getZRZH().length()));
			}
			if (null != h.getZRZH() && h.getZRZH().length() <= 20) {
				fwh.setZrzh(h.getZRZH());
			}
			//fwh.setZt(StringHelper.FormatByDatatype(h.getZT()) == "" ? "1" : StringHelper.FormatByDatatype(h.getZT()));
			
			fwh.setBdcdyid(StringHelper.formatObject(h.getId()));
			fwh.setZdbdcdyid(StringHelper.formatObject(h.getZDBDCDYID()));
			fwh.setZrzbdcdyid(StringHelper.formatObject(h.getZRZBDCDYID()));
		}
		fwh.replaceEmpty();
		return fwh;
	}

	/**
	 * 非结构化文档上传
	 * 
	 * @作者 diaoliwei
	 * @创建时间 2015年8月29日下午4:22:35
	 * @param fj
	 * @return
	 */
	public static FJF100 getFJF(FJF100 fj) {
		fj.setFJMC("无");
		fj.setFJLX("无");
		fj.setFJNR("无");
		fj.replaceEmpty();
		return fj;
	}

	/**
	 * 宗地变化情况
	 * 
	 * @作者 diaoliwei
	 * @创建时间 2015年8月29日下午4:23:41
	 * @return
	 */
	public static KTFZDBHQK getKTFZDBHQK(KTFZDBHQK bhqk, UseLand zd, Rights ql,
			OwnerLand oland, Forest sllm) {
		bhqk.setZDDM(zd == null || zd.getZDDM() == null ? "无" : zd.getZDDM());
		// 这里需要修改
		bhqk.setBHYY("无");
		bhqk.setBHNR("无");
		bhqk.setFJ("无");
		bhqk.setDBR(ql == null || ql.getDBR() == null ? "无" : ql.getDBR());
		if (!StringUtils.isEmpty(ql.getFJ())) {
			bhqk.setFJ(ql.getFJ());
		}
		if(zd!=null) {
			bhqk.setQXDM(getQXDM(zd));
		}else if(oland!=null) {
			bhqk.setQXDM(getQXDM(oland));
		}else {
			bhqk.setQXDM(getQXDM(sllm));
		}
		
		bhqk.setDJSJ(StringHelper.FormatDateOnType(ql.getDJSJ(),"yyyy-MM-dd'T'HH:mm:ss"));
		bhqk.replaceEmpty();
		return bhqk;
	}
	
	/**
	 * 宗地变化情况 (农用地)
	 * @创建时间 201年8月23日
	 * @return KTFZDBHQK
	 */
	public static KTFZDBHQK getKTFZDBHQKByNYD(KTFZDBHQK bhqk, AgriculturalLand nyd, Rights ql,
			OwnerLand oland, Forest sllm) {
		bhqk.setZDDM(nyd == null || nyd.getZDDM() == null ? "无" : nyd.getZDDM());
		// 这里需要修改
		bhqk.setBHYY("无");
		bhqk.setBHNR("无");
		bhqk.setFJ("无");
		bhqk.setDBR(ql == null || ql.getDBR() == null ? "无" : ql.getDBR());
		if (!StringUtils.isEmpty(ql.getFJ())) {
			bhqk.setFJ(ql.getFJ());
		}
		//bhqk.setQXDM(nyd == null || nyd.getQXDM() == null ? ConfigHelper.getNameByValue("XZQHDM") : nyd.getQXDM());
		bhqk.setQXDM(getQXDM(nyd));
		bhqk.setDJSJ(StringHelper.FormatDateOnType(ql.getDJSJ(),"yyyy-MM-dd'T'HH:mm:ss"));
		bhqk.replaceEmpty();
		return bhqk;
	}

	/**
	 * 房地产权_独幢、层、套、间房屋信息
	 * 
	 * @作者 diaoliwei
	 * @创建时间 2015年9月14日下午5:35:06
	 * @param fdcqyz
	 * @param h
	 * @param ql
	 * @param ywh
	 * @return
	 */
	public static QLTFWFDCQYZ getQLTFWFDCQYZ(QLTFWFDCQYZ fdcqyz, House h, Rights ql, String ywh) {
		fdcqyz.setBdcdyh("无");
		fdcqyz.setYsdm("6002010210");
		fdcqyz.setQllx("");
		fdcqyz.setDjlx("");
		fdcqyz.setDjyy("无");
		fdcqyz.setFdzl("无");
		fdcqyz.setTdsyqr("无");
		fdcqyz.setBdcqzh("无");
		fdcqyz.setQxdm("");
		fdcqyz.setDjjg("无");
		fdcqyz.setDbr("无");
		fdcqyz.setFj("无");
		fdcqyz.setFtjzmj(0); // 分摊建筑面积
		fdcqyz.setJzmj(0);// 建筑面积
		fdcqyz.setZyjzmj(0);// 专有建筑面积
		if (null != h) {
			fdcqyz.setBdcdyh(StringHelper.formatDefaultValue(h.getBDCDYH()));
			fdcqyz.setFdzl(StringHelper.formatDefaultValue(h.getZL()));
			fdcqyz.setTdsyqr(StringHelper.formatDefaultValue(h.getTDSYQR()));
			fdcqyz.setGhyt(StringHelper.formatObject(h.getGHYT()));
			fdcqyz.setSzc(h.getQSC() == null ? 0 : h.getQSC().intValue());
			fdcqyz.setQxdm(getQXDM(h));
			if(BDCDYLX.H.Value.equals(h.getBDCDYLX().Value)){
				if(DJDYLY.XZ.Value.equals(h.getLY().Value)){
					BDCS_H_XZ h_unit=(BDCS_H_XZ)h;
					if(h_unit.getSCFTJZMJ() !=null){
						fdcqyz.setFtjzmj(h_unit.getSCFTJZMJ()); // 分摊建筑面积
					}else{
						fdcqyz.setFtjzmj(0); // 分摊建筑面积
					}
					if(h_unit.getSCJZMJ() !=null){
						fdcqyz.setJzmj(h_unit.getSCJZMJ());// 建筑面积
					}else{
						fdcqyz.setJzmj(0);// 建筑面积
					}
					if(h_unit.getSCTNJZMJ() !=null){
						fdcqyz.setZyjzmj(h_unit.getSCTNJZMJ());// 专有建筑面积
					}else{
						fdcqyz.setZyjzmj(0);// 专有建筑面积
					}
				}else if(DJDYLY.LS.Value.equals(h.getLY().Value)){
					BDCS_H_LS h_unit=(BDCS_H_LS)h;
					if(h_unit.getSCFTJZMJ() !=null){
						fdcqyz.setFtjzmj(h_unit.getSCFTJZMJ()); // 分摊建筑面积
					}else{
						fdcqyz.setFtjzmj(0); // 分摊建筑面积
					}
					if(h_unit.getSCJZMJ() !=null){
						fdcqyz.setJzmj(h_unit.getSCJZMJ());// 建筑面积
					}else{
						fdcqyz.setJzmj(0);// 建筑面积
					}
					if(h_unit.getSCTNJZMJ() !=null){
						fdcqyz.setZyjzmj(h_unit.getSCTNJZMJ());// 专有建筑面积
					}else{
						fdcqyz.setZyjzmj(0);// 专有建筑面积
					}
				}else if(DJDYLY.GZ.Value.equals(h.getLY().Value)){
					BDCS_H_GZ h_unit=(BDCS_H_GZ)h;
					if(h_unit.getSCFTJZMJ() !=null){
						fdcqyz.setFtjzmj(h_unit.getSCFTJZMJ()); // 分摊建筑面积
					}else{
						fdcqyz.setFtjzmj(0); // 分摊建筑面积
					}
					if(h_unit.getSCJZMJ() !=null){
						fdcqyz.setJzmj(h_unit.getSCJZMJ());// 建筑面积
					}else{
						fdcqyz.setJzmj(0);// 建筑面积
					}
					if(h_unit.getSCTNJZMJ() !=null){
						fdcqyz.setZyjzmj(h_unit.getSCTNJZMJ());// 专有建筑面积
					}else{
						fdcqyz.setZyjzmj(0);// 专有建筑面积
					}
				}
			}else if(BDCDYLX.YCH.Value.equals(h.getBDCDYLX().Value)){
				if(DJDYLY.XZ.equals(h.getLY())){
					BDCS_H_XZY h_unit=(BDCS_H_XZY)h;
					if(h_unit.getSCFTJZMJ() !=null){
						fdcqyz.setFtjzmj(h_unit.getSCFTJZMJ()); // 分摊建筑面积
					}else{
						fdcqyz.setFtjzmj(0); // 分摊建筑面积
					}
					if(h_unit.getSCJZMJ() !=null){
						fdcqyz.setJzmj(h_unit.getSCJZMJ());// 建筑面积
					}else{
						fdcqyz.setJzmj(0);// 建筑面积
					}
					if(h_unit.getSCTNJZMJ() !=null){
						fdcqyz.setZyjzmj(h_unit.getSCTNJZMJ());// 专有建筑面积
					}else{
						fdcqyz.setZyjzmj(0);// 专有建筑面积
					}
				}else if(DJDYLY.LS.Value.equals(h.getLY().Value)){
					BDCS_H_LSY h_unit=(BDCS_H_LSY)h;
					if(h_unit.getSCFTJZMJ() !=null){
						fdcqyz.setFtjzmj(h_unit.getSCFTJZMJ()); // 分摊建筑面积
					}else{
						fdcqyz.setFtjzmj(0); // 分摊建筑面积
					}
					if(h_unit.getSCJZMJ() !=null){
						fdcqyz.setJzmj(h_unit.getSCJZMJ());// 建筑面积
					}else{
						fdcqyz.setJzmj(0);// 建筑面积
					}
					if(h_unit.getSCTNJZMJ() !=null){
						fdcqyz.setZyjzmj(h_unit.getSCTNJZMJ());// 专有建筑面积
					}else{
						fdcqyz.setZyjzmj(0);// 专有建筑面积
					}
				}
			}
		}

		if (null != ql) {
			fdcqyz.setBdcqzh(StringHelper.formatDefaultValue(ql.getBDCQZH()));
			fdcqyz.setDbr(StringHelper.formatDefaultValue(ql.getDBR()));
			fdcqyz.setQllx(StringHelper.formatObject(ql.getQLLX()));
			fdcqyz.setDjlx(StringHelper.formatObject(ql.getDJLX()));
			fdcqyz.setDjyy(StringHelper.formatDefaultValue(ql.getDJYY()));
			fdcqyz.setDjjg(StringHelper.formatDefaultValue(ql.getDJJG()));
			fdcqyz.setFj(StringHelper.formatDefaultValue(ql.getFJ()));
			if(!StringHelper.isEmpty(ql.getQDJG())&&ql.getQDJG()>0){
				//把元修改成万元
				Double jyjg=StringHelper.getDouble(ql.getQDJG())/10000.0;
				fdcqyz.setFdcjyjg(StringHelper.cutBigDecimal(StringHelper.getBigDecimal(jyjg), 4));
//			}else if(!StringHelper.isEmpty(h.getFDCJYJG())){
//				户表存在FDCJYJG字段
//				Double jyjg=StringHelper.getDouble(h.getFDCJYJG())/10000.0;
//				fdcqyz.setFdcjyjg(StringHelper.cutBigDecimal(StringHelper.getBigDecimal(jyjg), 4));
			}else {
				fdcqyz.setFdcjyjg(StringHelper.cutBigDecimal(StringHelper.getBigDecimal(0), 4));
			}
			if(!StringHelper.isEmpty(ql.getTDSHYQR())){
				fdcqyz.setTdsyqr(ql.getTDSHYQR());
			}
		}
		fdcqyz.setDjsj(StringHelper.FormatDateOnType(ql.getDJSJ(),"yyyy-MM-dd'T'HH:mm:ss"));
		fdcqyz.setDytdmj(h == null || h.getDYTDMJ() == null ? 0 : h.getDYTDMJ());
		
		fdcqyz.setFttdmj(h == null || h.getFTTDMJ() == null ? 0 : h.getFTTDMJ());
		if (h != null && !StringUtils.isEmpty(h.getFWJG())) {
			fdcqyz.setFwjg(h.getFWJG());
		}
		if (h != null && !StringUtils.isEmpty(h.getFWXZ())) {
			String fwxz=getFWXZ(h.getFWXZ());
			fdcqyz.setFwxz(fwxz);
		}
		fdcqyz.setJgsj(StringHelper.FormatDateOnType(h.getJGSJ(),"yyyy-MM-dd'T'HH:mm:ss"));
		if("".equals(fdcqyz.getJgsj()))
		{
			fdcqyz.setJgsj(null);
		}
		fdcqyz.setQszt(StringHelper.FormatByDatatype(ql.getQSZT()) == "" ? "1" : StringHelper.FormatByDatatype(ql.getQSZT()));  //qsz默认给值1
		
		Calendar calendar_= new GregorianCalendar(1949, 9, 01,0,0,0); //java 的月份是0到11，所以月份要减1
		Calendar calendar = new GregorianCalendar(2999, 11, 31,0,0,0); 
		Date date_qssj = calendar_.getTime(); 
		Date date_jssj = calendar.getTime(); 
		String xzqhdm = ConfigHelper.getNameByValue("XZQHDM");
		//// 商水，孝感市,孝南区,湖北省孝感市云梦县,湖北省 咸宁市 赤壁市 权利起始时间为空时设置固定起始时间，结束时间不设置。
		if(xzqhdm.equals("411623")|| xzqhdm.equals("420900")|| xzqhdm.equals("420902") || xzqhdm.equals("420923") || xzqhdm.equals("421281") ){
			if(ql.getQLQSSJ()==null){
				fdcqyz.setTdsyqssj(StringHelper.FormatDateOnType(date_qssj,"yyyy-MM-dd'T'HH:mm:ss"));
			}else{
				fdcqyz.setTdsyqssj(StringHelper.FormatDateOnType(ql.getQLQSSJ(),"yyyy-MM-dd'T'HH:mm:ss"));
			}
			// 101划拨权利性质要设定固定的起始和结束时间（国家未对以下性质的起算之日做出规定，设定固定值：（起始）19491001-（结束）29991231）
			if(h.getQLXZ().equals("101")){
				fdcqyz.setTdsyjssj(StringHelper.FormatDateOnType(date_jssj,"yyyy-MM-dd'T'HH:mm:ss"));
			}else{
				fdcqyz.setTdsyjssj(StringHelper.FormatDateOnType(ql.getQLJSSJ(),"yyyy-MM-dd'T'HH:mm:ss"));
			}
		//// 其他省市
		}else {
			if(h.getQLXZ()!=null && h.getQLXZ().equals("101"))
			{
				fdcqyz.setTdsyjssj(StringHelper.FormatDateOnType(date_jssj,"yyyy-MM-dd'T'HH:mm:ss"));
				fdcqyz.setTdsyqssj(StringHelper.FormatDateOnType(date_qssj,"yyyy-MM-dd'T'HH:mm:ss"));
			}else if (h.getQLXZ() == null && !StringHelper.isEmpty(h.getZDBDCDYID())){
				CommonDao dao = SuperSpringContext.getContext()
						.getBean(CommonDao.class);
				BDCS_SHYQZD_XZ zd = dao.get(BDCS_SHYQZD_XZ.class, h.getZDBDCDYID());
				if(zd != null && "101".equals(zd.getQLXZ())) {
					fdcqyz.setTdsyjssj(StringHelper.FormatDateOnType(date_jssj,"yyyy-MM-dd'T'HH:mm:ss"));
					fdcqyz.setTdsyqssj(StringHelper.FormatDateOnType(date_qssj,"yyyy-MM-dd'T'HH:mm:ss"));
				}else{
					fdcqyz.setTdsyjssj(StringHelper.FormatDateOnType(ql.getQLJSSJ(),"yyyy-MM-dd'T'HH:mm:ss"));
					fdcqyz.setTdsyqssj(StringHelper.FormatDateOnType(ql.getQLQSSJ(),"yyyy-MM-dd'T'HH:mm:ss"));
				}
			}else{
				fdcqyz.setTdsyjssj(StringHelper.FormatDateOnType(ql.getQLJSSJ(),"yyyy-MM-dd'T'HH:mm:ss"));
				fdcqyz.setTdsyqssj(StringHelper.FormatDateOnType(ql.getQLQSSJ(),"yyyy-MM-dd'T'HH:mm:ss"));
			}
		}
		
		fdcqyz.setYwh(StringHelper.formatDefaultValue(ywh));
		fdcqyz.setZcs(h == null || h.getZCS() == null ? 0 : h.getZCS());
		fdcqyz.setQlid(StringHelper.formatObject(ql.getId()));
		fdcqyz.setXmbh(StringHelper.formatObject(ql.getXMBH()));
		fdcqyz.setQlqssj(StringHelper.FormatDateOnType(ql.getQLQSSJ(),"yyyy-MM-dd'T'HH:mm:ss"));
		fdcqyz.setQljssj(StringHelper.FormatDateOnType(ql.getQLQSSJ(),"yyyy-MM-dd'T'HH:mm:ss"));
		fdcqyz.setBdcdyid(StringHelper.formatObject(h.getId()));
		fdcqyz.replaceEmpty();
		return fdcqyz;
	}

	/**
	 * 建设用地、宅基地使用权
	 * 
	 * @作者 diaoliwei
	 * @创建时间 2015年8月29日下午4:25:39
	 * @param syq
	 * @return
	 */
	public static QLFQLJSYDSYQ getQLFQLJSYDSYQ(QLFQLJSYDSYQ syq, UseLand zd,
			Rights ql, String ywh) {
		syq.setYSDM("6002020100");
		syq.setZDDM("无");
		syq.setBDCDYH("无");
		syq.setQXDM("");
		if (null != zd) {
			syq.setZDDM(StringHelper.formatDefaultValue(zd.getZDDM()));
			syq.setBDCDYH(StringHelper.formatDefaultValue(zd.getBDCDYH()));
			syq.setQXDM(getQXDM(zd));
		}
		syq.setDJLX("");
		syq.setDJYY("无");
		syq.setQLLX("");
		syq.setBDCQZH("无");
		syq.setDJJG("无");
		syq.setDBR("无");
		syq.setFJ("无");
		syq.setQSZT("1");  //qszt默认给值1
		if (ql != null) {
			if (!StringUtils.isEmpty(ql.getDJLX())) {
				syq.setDJLX(ql.getDJLX());
			}
			if (!StringUtils.isEmpty(ql.getDJYY())) {
				syq.setDJYY(ql.getDJYY());
			}
			if (!StringUtils.isEmpty(ql.getQLLX())) {
				// 国家标准和项目上使用的23和24这两个权利类型是位置对调的
				if(ql.getQLLX().equals("23")){
					syq.setQLLX("24");
				}else if(ql.getQLLX().equals("24")){
					syq.setQLLX("23");
				}else if(ql.getQLLX().equals("37")){
					//国家标准里面没有权利类型为37的字典值
				}else{
					syq.setQLLX(ql.getQLLX());
				}
			}
			if (!StringUtils.isEmpty(ql.getBDCQZH())) {
				syq.setBDCQZH(ql.getBDCQZH());
			}
			if (!StringUtils.isEmpty(ql.getDJJG())) {
				syq.setDJJG(ql.getDJJG());
			}
			if (!StringUtils.isEmpty(ql.getDBR())) {
				syq.setDBR(ql.getDBR());
			}
			if (!StringUtils.isEmpty(ql.getQSZT())) {
				syq.setQSZT(ql.getQSZT().toString());
			}
			syq.setFJ(StringHelper.formatDefaultValue(ql.getFJ()));
		}
		//转换成万元
		 Double qdjg=StringHelper.getDouble(ql.getQDJG())/10000.0;
		 syq.setQDJG(StringHelper.cutBigDecimal(StringHelper.getBigDecimal(qdjg), 4));
//		syq.setQDJG(StringHelper.cutBigDecimal(StringHelper.getBigDecimal(ql.getQDJG()), 4));
		syq.setYWH(GetYWLSHByYWH(ql.getYWH()));
		if(zd!=null ) {
			syq.setSYQMJ(StringHelper.cutBigDecimal(StringHelper.getBigDecimal(zd.getZDMJ()), 2));
		}
		
		/*wlb
		 * 以下权利性质都需要设定固定的起始和结束时间（国家未对以下性质的起算之日做出规定，设定固定值：（起始）19491001-（结束）29991231）
		 * 101：划拨
		 */
//		if(QLLX.JTJSYDSYQ.Value.equals(ql.getQLLX())||QLLX.ZJDSYQ.Value.equals(ql.getQLLX())){
//			if(StringHelper.isEmpty(ql.getQLQSSJ())){
//				syq.setSYQQSSJ(null);
//			}
//			if(StringHelper.isEmpty(ql.getQLJSSJ())){
//				syq.setSYQJSSJ(null);
//			}
//		}
		Calendar calendar = new GregorianCalendar(2999, 11, 31,0,0,0); 
		Calendar calendar_= new GregorianCalendar(1949, 9, 01,0,0,0); //java 的月份是0到11，所以月份要减1
		Date date_jssj = calendar.getTime(); 
		Date date_qssj = calendar_.getTime(); 
		String xzqhdm = ConfigHelper.getNameByValue("XZQHDM");
		//// 商水，孝感市,孝南区,湖北省孝感市云梦县,湖北省 咸宁市 赤壁市 权利起始时间为空时设置固定起始时间，结束时间不设置。
		if(xzqhdm.equals("411623")|| xzqhdm.equals("420900")|| xzqhdm.equals("420902") || xzqhdm.equals("420923") || xzqhdm.equals("421281") ){
			if(ql.getQLQSSJ()==null){
				syq.setSYQQSSJ(StringHelper.FormatDateOnType(date_qssj,"yyyy-MM-dd'T'HH:mm:ss"));
			}else{
				syq.setSYQQSSJ(StringHelper.FormatDateOnType(ql.getQLQSSJ(),"yyyy-MM-dd'T'HH:mm:ss"));
			}
			if(zd!=null&&zd.getQLXZ().equals("101")){
				syq.setSYQJSSJ(StringHelper.FormatDateOnType(date_jssj,"yyyy-MM-dd'T'HH:mm:ss"));
			}else{
				syq.setSYQJSSJ(StringHelper.FormatDateOnType(ql.getQLJSSJ(),"yyyy-MM-dd'T'HH:mm:ss"));
			}
		}else {
			if((zd!=null&&zd.getQLXZ().equals("101"))
					|| (ql!=null&&StringHelper.isEmpty(ql.getQLQSSJ())&&StringHelper.isEmpty(ql.getQLJSSJ())))
			{// 101划拨权利性质要设定固定的起始和结束时间（国家未对以下性质的起算之日做出规定，设定固定值：（起始）19491001-（结束）29991231）
				
				syq.setSYQJSSJ(StringHelper.FormatDateOnType(date_jssj,"yyyy-MM-dd'T'HH:mm:ss"));
				syq.setSYQQSSJ(StringHelper.FormatDateOnType(date_qssj,"yyyy-MM-dd'T'HH:mm:ss"));
			}else{
				syq.setSYQJSSJ(StringHelper.FormatDateOnType(ql.getQLJSSJ(),"yyyy-MM-dd'T'HH:mm:ss"));
				syq.setSYQQSSJ(StringHelper.FormatDateOnType(ql.getQLQSSJ(),"yyyy-MM-dd'T'HH:mm:ss"));
			}
		}
		syq.setDJSJ(StringHelper.FormatDateOnType(ql.getDJSJ(),"yyyy-MM-dd'T'HH:mm:ss"));
		syq.setQlid(StringHelper.formatObject(ql.getId()));
		syq.setXmbh(StringHelper.formatObject(ql.getXMBH()));
		syq.setBdcdyid(StringHelper.formatObject(zd.getId()));
		syq.replaceEmpty();
		//syq.replaceEmpty();
		return syq;
	}

	/**
	 * 土地所有权
	 * 
	 * @作者 diaoliwei
	 * @创建时间 2015年9月21日下午4:56:17
	 * @param tdsyq
	 * @param zd
	 * @param ql
	 * @param ywh
	 * @return
	 */
	public static QLFQLTDSYQ getQLFQLTDSYQ(QLFQLTDSYQ tdsyq, OwnerLand oland,
			Rights ql, String ywh) {
		tdsyq.setYsdm("6002010100");
		tdsyq.setZddm("无");
		tdsyq.setBdcdyh("无");
		tdsyq.setYwh(StringHelper.formatDefaultValue(ywh));
		tdsyq.setQllx("");
		tdsyq.setDjlx("");
		tdsyq.setDjyy("无");
		tdsyq.setMjdw("1");
		tdsyq.setBdcqzh("无");
		tdsyq.setQxdm("");
		tdsyq.setDjjg("无");
		tdsyq.setDbr("无");
		tdsyq.setDjsj(StringHelper.FormatDateOnType(ql.getDJSJ(),"yyyy-MM-dd'T'HH:mm:ss"));
		if (null != oland) {
			tdsyq.setZddm(StringHelper.formatDefaultValue(oland.getZDDM()));
			tdsyq.setBdcdyh(StringHelper.formatDefaultValue(oland.getBDCDYH()));
			tdsyq.setMjdw(StringHelper.formatObject(oland.getMJDW()) == "" ? "1" : StringHelper.formatObject(oland.getMJDW()));
			tdsyq.setNydmj(StringHelper.getDouble(oland.getNYDMJ()));
			tdsyq.setGdmj(StringHelper.getDouble(oland.getGDMJ()));
			tdsyq.setLdmj(StringHelper.getDouble(oland.getLDMJ()));
			tdsyq.setCdmj(StringHelper.getDouble(oland.getCDMJ()));
			tdsyq.setQtnydmj(StringHelper.getDouble(oland.getQTNYDMJ()));
			tdsyq.setJsydmj(StringHelper.getDouble(oland.getJSYDMJ()));
			tdsyq.setWlydmj(StringHelper.getDouble(oland.getWLYDMJ()));
			tdsyq.setQxdm(getQXDM(oland));
			tdsyq.setBdcdyid(StringHelper.formatObject(oland.getId()));
		}
		if (null != ql) {
			tdsyq.setQllx(StringHelper.formatObject(ql.getQLLX()));
			tdsyq.setDjlx(StringHelper.formatObject(ql.getDJLX()));
			tdsyq.setDjyy(StringHelper.formatDefaultValue(ql.getDJYY()));
			tdsyq.setBdcqzh(StringHelper.formatDefaultValue(ql.getBDCQZH()));
			tdsyq.setDjjg(StringHelper.formatDefaultValue(ql.getDJJG()));
			tdsyq.setDbr(StringHelper.formatDefaultValue(ql.getDBR()));
			tdsyq.setFj(StringHelper.formatDefaultValue(ql.getFJ()));
			tdsyq.setQszt(StringHelper.FormatByDatatype(ql.getQSZT()) == "" ? "1" : StringHelper.FormatByDatatype(ql.getQSZT()));  //qszt默认给值1
			tdsyq.setQlid(StringHelper.formatDefaultValue(ql.getId()));
			tdsyq.setXmbh(StringHelper.formatDefaultValue(ql.getXMBH()));
		}
		tdsyq.replaceEmpty();
		return tdsyq;
	}
	
	public static List<QLFXZDY> getQLFXZDYs(List<QLFXZDY> qlfxzdyList,List<BDCS_DYXZ> dyxzList,String ywh) {
		List<QLFXZDY> newqlfxzdyList = new ArrayList<QLFXZDY>();
		if(dyxzList != null && dyxzList.size() >0) {
			for (BDCS_DYXZ dyxz : dyxzList) {
				QLFXZDY qlfxzdy = new QLFXZDY();
				qlfxzdy.setYsdm("6002029900");
				qlfxzdy.setYwh(ywh);
				qlfxzdy.setBdcqzh(dyxz.getBDCQZH());
				qlfxzdy.setBdcdyh(dyxz.getBDCDYH());
				qlfxzdy.setDybdclx(dyxz.getBDCDYLX());
				qlfxzdy.setBxzrmc(dyxz.getBXZRMC());
				qlfxzdy.setBxzrmc(dyxz.getBXZRZJZL());
				qlfxzdy.setBxzrzjhm(dyxz.getBXZRZJHM());
				qlfxzdy.setXzwjhm(dyxz.getXZWJHM());
				qlfxzdy.setXzdw(dyxz.getXZDW());
				qlfxzdy.setSdtzrq(StringHelper.FormatDateOnType(dyxz.getSDTZRQ(),"yyyy-MM-dd'T'HH:mm:ss"));
				qlfxzdy.setXzqsrq(StringHelper.FormatDateOnType(dyxz.getXZQSRQ(),"yyyy-MM-dd'T'HH:mm:ss"));
				qlfxzdy.setXzzzrq(StringHelper.FormatDateOnType(dyxz.getXZZZRQ(),"yyyy-MM-dd'T'HH:mm:ss"));
				qlfxzdy.setSlr(dyxz.getSLR());
				qlfxzdy.setSlryj(dyxz.getSLRYJ());
				qlfxzdy.setXzlx(dyxz.getXZLX());
				qlfxzdy.setLsxz(dyxz.getLSXZ());
				qlfxzdy.setXzfw(dyxz.getXZFW());
				qlfxzdy.setDjsj(StringHelper.FormatDateOnType(dyxz.getDJSJ(),"yyyy-MM-dd'T'HH:mm:ss"));
				qlfxzdy.setDbr(dyxz.getDBR());
				qlfxzdy.setBz(dyxz.getBZ());
				qlfxzdy.setZxdjsj(StringHelper.FormatDateOnType(dyxz.getZXDJSJ(),"yyyy-MM-dd'T'HH:mm:ss"));
				qlfxzdy.setZxdbr(dyxz.getZXDBR());
				qlfxzdy.setZxywh(dyxz.getZXYWH());
				qlfxzdy.setZxbz(dyxz.getZXBZ());
				qlfxzdy.setZxyj(dyxz.getZXYJ());
				qlfxzdy.setXzwjhm(dyxz.getZXXZWJHM());
				qlfxzdy.setZxxzdw(dyxz.getZXXZDW());
				qlfxzdy.setBdcdyid(dyxz.getBDCDYID());
				qlfxzdy.setDyxzid(dyxz.getId());
				qlfxzdy.setYxbz(dyxz.getYXBZ());
				qlfxzdy.setXmbh(dyxz.getXMBH());
				
				newqlfxzdyList.add(qlfxzdy);
			}
		}
		return newqlfxzdyList;
	}

	/**
	 * 权利人
	 * 
	 * @作者 diaoliwei
	 * @创建时间 2015年8月29日下午4:27:46
	 * @return
	 */
	public static List<ZTTGYQLR> getZTTGYQLRs(List<ZTTGYQLR> zttqlr,
			List<BDCS_QLR_GZ> qlrs, UseLand zd, Rights ql, House h,
			OwnerLand oland, Forest sllm, Sea zh) {
		// 顺序号
		int sxh = 0;
		if(qlrs == null){
			return zttqlr;
		}
		String gyfs = null;
		for (BDCS_QLR_GZ qlr : qlrs) {
			ZTTGYQLR q = new ZTTGYQLR();
	//		q.setQLRLX(qlr.getQLRLX());
			q.setQLRMC("无");
			q.setZJH("无");
			q.setGJ("142"); //默认中国
			q.setQXDM("");
			q.setYSDM("2003000000");
			q.setSXH("1");
			if (null != qlr) {
				q.setQLRMC(StringHelper.formatDefaultValue(qlr.getQLRMC()));
				if (!StringUtils.isEmpty(qlr.getZJZL())) {
					q.setZJZL(StringHelper.formatDefaultValue(qlr.getZJZL()));
				}
				q.setZJH(StringHelper.formatDefaultValue(qlr.getZJH()));
				q.setFZJG(StringHelper.formatObject(qlr.getFZJG()));
				if (!StringUtils.isEmpty(qlr.getSSHY())) {
					q.setSSHY(StringHelper.formatObject(qlr.getSSHY()));
				}
				q.setGJ(StringHelper.formatObject(qlr.getGJ()) == "" ? "142" : StringHelper.formatObject(qlr.getGJ()));
				if (!StringUtils.isEmpty(qlr.getHJSZSS())) {
					q.setHJSZSS(StringHelper.formatObject(qlr.getHJSZSS()));
				}
				if (!StringUtils.isEmpty(qlr.getXB())) {
					q.setXB(StringHelper.formatObject(qlr.getXB()));
				}
				q.setDH(StringHelper.formatObject(qlr.getDH()));
				q.setDZ(StringHelper.formatObject(qlr.getDZ()));
				q.setYB(StringHelper.formatObject(qlr.getYB()));
				q.setGZDW(StringHelper.formatObject(qlr.getGZDW()));
				q.setDZYJ(StringHelper.formatObject(qlr.getDZYJ()));
				if(qlr.getQLRLX()!=null && !qlr.getQLRLX().equals("")){
					q.setQLRLX(StringHelper.formatObject(qlr.getQLRLX()));
				}else{
					q.setQLRLX("99");
				}
				
				if (StringUtils.isEmpty(qlr.getSXH())) {
					q.setSXH((sxh + 1) + "");
				} else {
					q.setSXH(StringHelper.formatObject(qlr.getSXH()));
				}
				q.setBZ(StringHelper.formatObject(qlr.getBZ()));
				q.setGYQK(StringHelper.formatObject(qlr.getGYQK()));
				if (StringHelper.isEmpty(qlr.getGYFS())) {
				    if(qlrs.size() >1){
					if (StringHelper.isEmpty(gyfs)) {
					    for (BDCS_QLR_GZ qlr2 : qlrs) {
						if (!StringHelper.isEmpty(qlr.getGYFS())) {
						    gyfs = qlr2.getGYFS();
						    break;
						}
					    } 
					    if(StringHelper.isEmpty(gyfs)){
						gyfs = "3";
					    }
					}
					q.setGYFS(gyfs);
				    }else{
					q.setGYFS("0");
				    }
				}else{
				    gyfs = qlr.getGYFS();
				    q.setGYFS(gyfs);
				}
				if(StringHelper.isEmpty(q.getGYQK())){
					q.setGYQK(ConstHelper.getNameByValue("GYFS", q.getGYFS()));
				}
				q.setQLBL(StringHelper.formatObject(qlr.getQLBL()));
			}
			if (zd != null) {
				q.setQXDM(getQXDM(zd));
				q.setBDCDYH(StringHelper.formatObject(zd.getBDCDYH()));
			}
			if (h != null) {
//				q.setQXDM(StringHelper.formatObject(h.getQXDM()) == "" ? ConfigHelper.getNameByValue("XZQHDM")
//						: h.getQXDM());
				q.setQXDM(getQXDM(h));
				q.setBDCDYH(StringHelper.formatObject(h.getBDCDYH()));
			}
			if (zh != null) {
//				q.setQXDM(StringHelper.formatObject(zh.getQXDM()) == "" ? ConfigHelper.getNameByValue("XZQHDM")
//						: zh.getQXDM());
				q.setQXDM(getQXDM(zh));
				q.setBDCDYH(StringHelper.formatObject(zh.getBDCDYH()));
			}
			q.setBDCQZH(StringHelper.formatDefaultValue(qlr.getBDCQZH()));
			q.setSFCZR(qlr.getISCZR());//是否持证人
			q.setQZYSXLH("");//权证印刷序列号
			q.setQlid(StringHelper.formatObject(qlr.getQLID()));
			q.setSqrid(StringHelper.formatObject(qlr.getSQRID()));
			q.setXmbh(StringHelper.formatObject(qlr.getXMBH()));
			q.setQlrid(StringHelper.formatObject(qlr.getId()));
			q.replaceEmpty();
			zttqlr.add(q);
		}
		return zttqlr;
	}

    /**
     * 权利人信息
     * @param zttqlr
     * @param qlrs 权利人信息
     * @param bdcqzh 不动产权证号
     * @param unit   具体单元
     * @return
     */
	public static List<ZTTGYQLR> getZTTGYQLRsEx(List<ZTTGYQLR> zttqlr,
			List<RightsHolder> qlrs,String bdcqzh,String bdcdyh, RealUnit unit ) {
		// 顺序号
		int sxh = 0;
		if(qlrs == null){
			return zttqlr;
		}
		String gyfs = null;
		for (RightsHolder qlr : qlrs) {
			ZTTGYQLR q = new ZTTGYQLR();
			q.setQLRLX(qlr.getQLRLX());
			q.setQLRMC("无");
			q.setZJH("无");
			q.setGJ("142"); //默认中国
			q.setQXDM("");
			q.setYSDM("2003000000");
			q.setSXH("1");
			q.setBDCQZH("无");
			q.setBDCDYH(bdcdyh);
			if (null != qlr) {
				q.setQLRMC(StringHelper.formatDefaultValue(qlr.getQLRMC()));
				if (!StringUtils.isEmpty(qlr.getZJZL())) {
					q.setZJZL(StringHelper.formatDefaultValue(qlr.getZJZL()));
				}
				q.setZJH(StringHelper.formatDefaultValue(qlr.getZJH()));
				q.setFZJG(StringHelper.formatObject(qlr.getFZJG()));
				if (!StringUtils.isEmpty(qlr.getSSHY())) {
					q.setSSHY(StringHelper.formatObject(qlr.getSSHY()));
				}
				q.setGJ(StringHelper.formatObject(qlr.getGJ()) == "" ? "142" : StringHelper.formatObject(qlr.getGJ()));
				if (!StringUtils.isEmpty(qlr.getHJSZSS())) {
					q.setHJSZSS(StringHelper.formatObject(qlr.getHJSZSS()));
				}
				if (!StringUtils.isEmpty(qlr.getXB())) {
					q.setXB(StringHelper.formatObject(qlr.getXB()));
				}
				q.setDH(StringHelper.formatObject(qlr.getDH()));
				q.setDZ(StringHelper.formatObject(qlr.getDZ()));
				q.setYB(StringHelper.formatObject(qlr.getYB()));
				q.setGZDW(StringHelper.formatObject(qlr.getGZDW()));
				q.setDZYJ(StringHelper.formatObject(qlr.getDZYJ()));
				q.setQLRLX(StringHelper.formatObject(qlr.getQLRLX()));
				if (StringUtils.isEmpty(qlr.getSXH())) {
					q.setSXH((sxh + 1) + "");
				} else {
					q.setSXH(StringHelper.formatObject(qlr.getSXH()));
				}
				q.setBZ(StringHelper.formatObject(qlr.getBZ()));
				q.setGYQK(StringHelper.formatObject(qlr.getGYQK()));
				if (StringHelper.isEmpty(qlr.getGYFS())) {
				    if(qlrs.size() >1){
					if (StringHelper.isEmpty(gyfs)) {
					    for (RightsHolder qlr2 : qlrs) {
						if (!StringHelper.isEmpty(qlr.getGYFS())) {
						    gyfs = qlr2.getGYFS();
						    break;
						}
					    } 
					    if(StringHelper.isEmpty(gyfs)){
						gyfs = "3";
					    }
					}
					q.setGYFS(gyfs);
				    }else{
					q.setGYFS("0");
				    }
				}else{
				    gyfs = qlr.getGYFS();
				    q.setGYFS(gyfs);
				}
				if(StringHelper.isEmpty(q.getGYQK())){
					q.setGYQK(ConstHelper.getNameByValue("GYFS", q.getGYFS()));
				}
				q.setQLBL(StringHelper.formatObject(qlr.getQLBL()));
			}
			if(!StringHelper.isEmpty(unit)){
//				q.setQXDM(StringHelper.formatObject(unit.getQXDM()) == "" ? ConfigHelper.getNameByValue("XZQHDM")
//						: unit.getQXDM());
				q.setQXDM(getQXDM(unit));
				if(!StringHelper.isEmpty(unit.getBDCDYH())){
					q.setBDCDYH(unit.getBDCDYH());
				}
			}
			q.setBDCQZH(qlr.getBDCQZH());
			q.setSFCZR(qlr.getISCZR());//是否持证人
			q.setQZYSXLH("");//权证印刷序列号
			q.setQlid(StringHelper.formatObject(qlr.getQLID()));
			q.setSqrid(StringHelper.formatObject(qlr.getSQRID()));
			q.setXmbh(StringHelper.formatObject(qlr.getXMBH()));
			q.replaceEmpty();
			zttqlr.add(q);
		}
		return zttqlr;
	}
	

	/**
	 * 登记受理申请表
	 * 
	 * @作者 diaoliwei
	 * @创建时间 2015年9月11日上午11:22:39
	 * @param sq
	 * @param ywh
	 * @param zd
	 * @param ql
	 * @param xmxx
	 * @param h
	 * @param createTime
	 * @return
	 */
	public static DJTDJSLSQ getDJTDJSLSQ(DJTDJSLSQ sq, String ywh,UseLand land, Rights ql, BDCS_XMXX xmxx, House h, Date createTime,
			OwnerLand oland, Forest sllm, Sea zh) {
		sq.setYSDM("2004010000");
		sq.setDJDL("");
		sq.setQXDM("");
		sq.setYWH(StringHelper.formatObject(ywh));
		if(land!=null) {
			sq.setQXDM(getQXDM(land));
		}else if (h!=null) {
			sq.setQXDM(getQXDM(h));
		}else if (zh!=null) {
			sq.setQXDM(getQXDM(zh));
		}
		if(xmxx.getDJLX()!=null && !StringUtils.isEmpty(xmxx.getDJLX())){
			sq.setDJDL(xmxx.getDJLX());
		}
		sq.setDJXL(ql == null || ql.getQLLX() == null ? "" : ql.getQLLX());
																			
		sq.setSLRY(xmxx == null || xmxx.getSLRY() == null ? "无" : xmxx.getSLRY());
		sq.setSLSJ(StringHelper.FormatDateOnType(createTime,"yyyy-MM-dd'T'HH:mm:ss"));
		if (h == null && land != null && !StringUtils.isEmpty(land.getZL())) {
			sq.setZL(land.getZL());
		}
		if (land == null && h != null && !StringUtils.isEmpty(h.getZL())) {
			sq.setZL(h.getZL());
		}
		sq.setSQFBCZ(ConstValue.CZFS.FBCZ.Value.equals(ql.getCZFS()) ? 1 : 0); //申请分别持证
		sq.setSQZSBS(ConstValue.ZSBS.DYB.Value.equals(ql.getZSBS()) ? 0 : 1); //证书版式
		sq.setTZRXM("");
		sq.setTZRDH("");
		sq.setTZRYDDH("");
		sq.setTZRDZYJ("");
		sq.setBZ("");
		sq.setJSSJ(null);
		// sq.setSFWTAJ("");
		// sq.setTZFS("");
		// sq.setAJZT("");
		sq.setXmbh(StringHelper.formatObject(xmxx.getId()));
		sq.removeEmpty();
		return sq;
	}
	
	/**
	 * 登记受理申请表扩展
	 * @param sq
	 * @param ywh
	 * @param ql
	 * @param xmxx
	 * @param unit
	 * @return
	 */
	public static DJTDJSLSQ getDJTDJSLSQEx(DJTDJSLSQ sq, String ywh, Rights ql, BDCS_XMXX xmxx,RealUnit unit) {
		sq.setYSDM("2004010000");
		sq.setDJDL("");
		sq.setQXDM("");
		sq.setYWH(StringHelper.formatObject(ywh));
		//instanceof
//		if(!StringHelper.isEmpty(unit)){
//			sq.setQXDM(StringHelper.formatObject(unit.getQXDM()) == "" ? ConfigHelper.getNameByValue("XZQHDM")
//					: unit.getQXDM());
//			sq.setZL(unit.getZL());
//		}
		sq.setQXDM(getQXDM(unit));
		if (ql != null && !StringUtils.isEmpty(ql.getDJLX())) {
			sq.setDJDL(ql.getDJLX());
		}
		if(ql.getQLLX().equals("23") || ql.getQLLX()=="23"){
			sq.setDJXL(ql == null || ql.getQLLX() == null ? "" : "99");
		}else{
			sq.setDJXL(ql == null || ql.getQLLX() == null ? "" : ql.getQLLX());
		}
		sq.setSLRY(xmxx == null || xmxx.getSLRY() == null ? "无" : xmxx.getSLRY());
		sq.setSLSJ(StringHelper.FormatDateOnType(xmxx.getSLSJ(),"yyyy-MM-dd'T'HH:mm:ss"));
		sq.setSQFBCZ(ConstValue.CZFS.FBCZ.Value.equals(ql.getCZFS()) ? 1 : 0); //申请分别持证
		sq.setSQZSBS(ConstValue.ZSBS.DYB.Value.equals(ql.getZSBS()) ? 0 : 1); //证书版式
		sq.setTZRXM("");
		sq.setTZRDH("");
		sq.setTZRYDDH("");
		sq.setTZRDZYJ("");
		sq.setBZ("");
		sq.setJSSJ(null);
		sq.setXmbh(StringHelper.formatObject(xmxx.getId()));
		sq.removeEmpty();
		return sq;
	}

	/**
	 * 登记审核表
	 * 
	 * @作者 diaoliwei
	 * @创建时间 2015年9月7日上午11:42:41
	 * @param sh
	 * @param ywh
	 * @return
	 */
	public static List<DJFDJSH> getDJFDJSH(RealUnit unit, String ywh, String xmbh,String actinstID) {
		List<DJFDJSH> list = new ArrayList<DJFDJSH>();
		SmProSPService smProSPService = SuperSpringContext.getContext()
				.getBean(SmProSPService.class);
		if (!StringUtils.isEmpty(actinstID)) {
			DJFDJSH sh=null;
			List<Approval> _approvals = smProSPService.GetSPYJ(actinstID);
			for (Approval a : _approvals) {
				sh = new DJFDJSH();
				sh.setQXDM(getQXDM(unit));
				sh.setYSDM("2004050000");
				sh.setYWH(StringHelper.formatObject(ywh));
//				sh.setQXDM(ConfigHelper.getNameByValue("XZQHDM"));
				sh.setQXDM(getQXDM(unit));
				sh.setXmbh(StringHelper.formatObject(xmbh));
				if (a.getSplx().equals("CS")) {
					if (a.getSpyjs() != null && a.getSpyjs().size() > 0) {
						int index = a.getSpyjs().size() - 1;
						sh.setSHYJ(StringHelper.formatDefaultValue(a.getSpyjs().get(index).getSpyj()));
						sh.setSHRYXM(StringHelper.formatDefaultValue(a.getSpyjs().get(index).getSpr_Name()));
						sh.setJDMC("初审");
						sh.setSHKSSJ(StringHelper.FormatDateOnType((a
								.getSpyjs().get(index).getSpsj()),"yyyy-MM-dd'T'HH:mm:ss"));
						sh.setSHJSSJ(StringHelper.FormatDateOnType((a
								.getSpyjs().get(index).getSpsj()),"yyyy-MM-dd'T'HH:mm:ss"));
						sh.setCZJG("1");
						sh.setSXH(1);
						sh.replaceEmpty();
						list.add(sh);
					}
					continue;
				}
				if ("KFQFS".equals(a.getSplx())) {
					if (a.getSpyjs() != null && a.getSpyjs().size() > 0) {
						int index = a.getSpyjs().size() - 1;
						sh.setSHYJ(StringHelper.formatDefaultValue(a.getSpyjs().get(index).getSpyj()));
						sh.setSHRYXM(StringHelper.formatDefaultValue(a.getSpyjs().get(index).getSpr_Name()));
						sh.setJDMC("开发区复审");
						sh.setSHKSSJ(StringHelper.FormatDateOnType((a
								.getSpyjs().get(index).getSpsj()),"yyyy-MM-dd'T'HH:mm:ss"));
						sh.setSHJSSJ(StringHelper.FormatDateOnType((a
								.getSpyjs().get(index).getSpsj()),"yyyy-MM-dd'T'HH:mm:ss"));
						sh.setCZJG("1");
						sh.setSXH(2);
						sh.replaceEmpty();
						list.add(sh);
					}
					continue;
				}
				if (a.getSplx().equals("FS")) {
					if (a.getSpyjs() != null && a.getSpyjs().size() > 0) {
						int index = a.getSpyjs().size() - 1;
						sh.setSHYJ(StringHelper.formatDefaultValue(a.getSpyjs().get(index).getSpyj()));
						sh.setSHRYXM(StringHelper.formatDefaultValue(a.getSpyjs().get(index).getSpr_Name()));
						sh.setJDMC("中心复审");
						sh.setSHKSSJ(StringHelper.FormatDateOnType((a
								.getSpyjs().get(index).getSpsj()),"yyyy-MM-dd'T'HH:mm:ss"));
						sh.setSHJSSJ(StringHelper.FormatDateOnType((a
								.getSpyjs().get(index).getSpsj()),"yyyy-MM-dd'T'HH:mm:ss"));
						sh.setCZJG("1");
						sh.setSXH(3);
						sh.replaceEmpty();
						list.add(sh);
					}
					continue;
				}
				if ("SH".equals(a.getSplx())) {
					if (a.getSpyjs() != null && a.getSpyjs().size() > 0) {
						int index = a.getSpyjs().size() - 1;
						sh.setSHYJ(StringHelper.formatDefaultValue(a.getSpyjs().get(index).getSpyj()));
						sh.setSHRYXM(StringHelper.formatDefaultValue(a.getSpyjs().get(index).getSpr_Name()));
						sh.setJDMC("审核");
						sh.setSHKSSJ(StringHelper.FormatDateOnType((a
								.getSpyjs().get(index).getSpsj()),"yyyy-MM-dd'T'HH:mm:ss"));
						sh.setSHJSSJ(StringHelper.FormatDateOnType((a
								.getSpyjs().get(index).getSpsj()),"yyyy-MM-dd'T'HH:mm:ss"));
						sh.setCZJG("1");
						sh.setSXH(4);
						sh.replaceEmpty();
						list.add(sh);
					}
					continue;
				}
				if (a.getSplx().equals("HD")) {
					if (a.getSpyjs() != null && a.getSpyjs().size() > 0) {
						int index = a.getSpyjs().size() - 1;
						sh.setSHYJ(StringHelper.formatDefaultValue(a.getSpyjs().get(index).getSpyj()));
						sh.setSHRYXM(StringHelper.formatDefaultValue(a.getSpyjs().get(index).getSpr_Name()));
						sh.setJDMC("审批");
						sh.setSHKSSJ(StringHelper.FormatDateOnType((a
								.getSpyjs().get(index).getSpsj()),"yyyy-MM-dd'T'HH:mm:ss"));
						sh.setSHJSSJ(StringHelper.FormatDateOnType((a
								.getSpyjs().get(index).getSpsj()),"yyyy-MM-dd'T'HH:mm:ss"));
						sh.setCZJG("1");
						sh.setSXH(5);
						sh.replaceEmpty();
						list.add(sh);
					}
					continue;
				}
			}
		}
		return list;
	}

	/**
	 * 登记缮证
	 * 
	 * @param sz
	 * @param ywh
	 * @return
	 */
	public static List<DJFDJSZ> getDJFDJSZ (RealUnit unit, String ywh, String xmbh) {
		// 查询BDCS_DJSZ
		CommonDao dao = SuperSpringContext.getContext().getBean(CommonDao.class);
		String hql = " XMBH = '" + xmbh + "'";
		List<BDCS_DJSZ> list = dao.getDataList(BDCS_DJSZ.class, hql);
		List<DJFDJSZ> listSz = new ArrayList<DJFDJSZ>();
		DJFDJSZ djsz;
		if (list != null && list.size() > 0) {	
			for (int i = 0; i < list.size(); i++) {
				djsz = new DJFDJSZ();
				djsz.setQXDM(getQXDM(unit));
				djsz.setYWH(StringHelper.formatObject(ywh));
				djsz.setYSDM("6004060000");
				djsz.setSZMC(StringHelper.formatObject(list.get(i).getSZMC()) == "" ? "缮证" : list.get(i).getSZMC());
				djsz.setSZZH(StringHelper.formatObject(list.get(i).getSZZH()) == "" ? "无" : list.get(i).getSZZH());
				djsz.setYSXLH(StringHelper.formatObject(list.get(i).getYSXLH()) == "" ? "无" : list.get(i).getYSXLH());
				djsz.setSZRY(list.get(i).getSZRY());
				djsz.setSZSJ(StringHelper.FormatDateOnType(list.get(i).getSZSJ(),"yyyy-MM-dd'T'HH:mm:ss"));
				djsz.setBZ(list.get(i).getBZ());
				djsz.setXmbh(StringHelper.formatObject(xmbh));
				djsz.replayEmpty();
				listSz.add(djsz);
				djsz = null;
			}
		}
		return listSz;

	}

	/**
	 * 申请人属性信息
	 * 
	 * @作者 diaoliwei
	 * @创建时间 2015年9月14日下午1:25:09
	 * @param djsqrs
	 * @param sqrs
	 * @param ywdm
	 * @param ywh
	 * @return
	 */
	public static List<DJFDJSQR> getDJSQRs(List<DJFDJSQR> djsqrs,
			List<BDCS_SQR> sqrs, String ywdm, String ywh, String bdcdyh) {
		djsqrs=new ArrayList<DJFDJSQR>();
		for (BDCS_SQR sqr : sqrs) {
			DJFDJSQR djsqr = new DJFDJSQR();
			djsqr.setBz("无");
			djsqr.setYwh(StringHelper.formatDefaultValue(ywh));
			djsqr.setYsdm("2004020000");
			djsqr.setQxdm(getQXDM(bdcdyh));
			djsqr.setSqrlb(sqr.getSQRLB());
			djsqr.setXmbh(sqr.getXMBH());
			if (ConstValue.SQRLB.YF.Value.equals(sqr.getSQRLB())) {
				djsqr.setYwrdljg(sqr.getDLJGMC());
				djsqr.setYwrdlrdh(sqr.getDLRLXDH());
				djsqr.setYwrdlrmc(sqr.getDLRXM());
				djsqr.setYwrfrdh(sqr.getFDDBRDH());
				djsqr.setYwrfrmc(sqr.getFDDBR());
				djsqr.setYwrmc(sqr.getSQRXM());
				djsqr.setYwrtxdz(sqr.getTXDZ());
				djsqr.setYwryb(sqr.getYZBM());
				djsqr.setYwrzjh(sqr.getZJH());
				djsqr.setYwrzjzl(sqr.getZJLX());
				djsqr.setQlrmc("无");
				djsqr.setSqrlb(sqr.getSQRLB());
				djsqr.setQlrdh(sqr.getLXDH());
				djsqr.setQlrlx(sqr.getSQRLX());
				djsqr.setSqrid(sqr.getId());
			}
			if (ConstValue.SQRLB.JF.Value.equals(sqr.getSQRLB())) {
				djsqr.setQlryb(StringHelper.formatDefaultValue(sqr.getYZBM()));
				djsqr.setQlrzjh(StringHelper.formatDefaultValue(sqr.getZJH()));
				djsqr.setQlrdljg(StringHelper.formatDefaultValue(sqr.getDLJGMC()));
				djsqr.setQlrdlrdh(StringHelper.formatDefaultValue(sqr.getDLRLXDH()));
				djsqr.setQlrdlrmc(StringHelper.formatDefaultValue(sqr.getDLRXM()));
				djsqr.setQlrfrdh(StringHelper.formatDefaultValue(sqr.getFDDBRDH()));
				djsqr.setQlrfrmc(StringHelper.formatDefaultValue(sqr.getFDDBR()));
				djsqr.setQlrtxdz(StringHelper.formatDefaultValue(sqr.getTXDZ()));
				if (!StringUtils.isEmpty(sqr.getZJLX())) {
					djsqr.setQlrzjzl(StringHelper.formatObject(sqr.getZJLX()));
				}
				djsqr.setQlrmc(StringHelper.formatDefaultValue(sqr.getSQRXM()));
				djsqr.setSqrlb(sqr.getSQRLB());
				djsqr.setQlrdh(sqr.getLXDH());
				djsqr.setQlrlx(sqr.getSQRLX());
				djsqr.setSqrid(sqr.getId());
			}
			djsqr.replaceEmpty();
			djsqrs.add(djsqr);
		}
		if(djsqrs!=null&&djsqrs.size()>0) {
			for(DJFDJSQR s:djsqrs) {
				s.setQxdm(getQXDM(bdcdyh));
			}
		}
		
		return djsqrs;
	}

	public static BDCS_DYBG getDYBG(String xbdcdyid) {
		CommonDao dao = SuperSpringContext.getContext()
				.getBean(CommonDao.class);
		String hql = " LBDCDYID ='" + xbdcdyid + "'";
		List<BDCS_DYBG> list = dao.getDataList(BDCS_DYBG.class, hql);
		BDCS_DYBG dybg = new BDCS_DYBG();
		if (list != null && list.size() > 0) {
			dybg = list.get(0);
		}
		return dybg;
	}

	

	/**
	 * 房地产权_多幢表
	 * 
	 * @作者 diaoliwei
	 * @创建时间 2015年9月4日下午8:55:53
	 * @param fdcqdz
	 * @param h
	 * @param ql
	 * @param ywh
	 * @return
	 */
	public static QLTFWFDCQDZ getQLTFWFDCQDZ(QLTFWFDCQDZ fdcqdz, House h,
			Rights ql, String ywh) {
		fdcqdz.setYsdm("2002010210");
		fdcqdz.setBdcdyh(h == null || h.getBDCDYH() == null ? "" : h
				.getBDCDYH());
		fdcqdz.setYwh(StringHelper.formatObject(ywh));
		fdcqdz.setQllx("");
		fdcqdz.setDjlx("");
		fdcqdz.setDjyy("无");
		fdcqdz.setFdzl(h == null || h.getZL() == null ? "" : h.getZL());
		fdcqdz.setTdsyqr(h == null || h.getTDSYQR() == null ? "无" : h
				.getTDSYQR());
		fdcqdz.setTdsyjssj(StringHelper.FormatDateOnType(ql
				.getQLJSSJ(),"yyyy-MM-dd'T'HH:mm:ss"));
		fdcqdz.setTdsyqssj(StringHelper.FormatDateOnType(ql
				.getQLQSSJ(),"yyyy-MM-dd'T'HH:mm:ss"));
		fdcqdz.setBdcqzh("无");
		fdcqdz.setQxdm(getQXDM(h));
		fdcqdz.setDjjg("无");
		fdcqdz.setDbr("无");
		fdcqdz.setDjsj(StringHelper.FormatDateOnType(ql.getDJSJ(),"yyyy-MM-dd'T'HH:mm:ss"));
		if (null != ql) {
			fdcqdz.setBdcqzh(StringHelper.formatDefaultValue(ql.getBDCQZH()));
			fdcqdz.setDbr(StringHelper.formatDefaultValue(ql.getDBR()));
			fdcqdz.setQllx(StringHelper.formatObject(ql.getQLLX()));
			fdcqdz.setDjlx(StringHelper.formatObject(ql.getDJLX()));
			fdcqdz.setDjyy(StringHelper.formatDefaultValue(ql.getDJYY()));
			fdcqdz.setDjjg(StringHelper.formatDefaultValue(ql.getDJJG()));
			fdcqdz.setQszt(StringHelper.FormatByDatatype(ql.getQSZT()) == "" ? "1" : StringHelper.FormatByDatatype(ql.getQSZT()));  //qszt默认给值1
		}
		fdcqdz.setQlid(StringHelper.formatObject(ql.getId()));
		fdcqdz.setXmbh(StringHelper.formatObject(ql.getXMBH()));
		fdcqdz.setBdcdyid(StringHelper.formatObject(h.getId()));
		fdcqdz.replaceEmpty();
		return fdcqdz;
	}

	/**
	 * 房地产权_多幢表_项目属性表
	 * 
	 * @作者 diaoliwei
	 * @创建时间 2015年9月4日下午9:04:42
	 * @param dzxm
	 * @param h
	 * @param ql
	 * @return
	 */
	public static QLFFWFDCQDZXM getQLFFWFDCQDZXM(QLFFWFDCQDZXM dzxm, House h,
			Rights ql) {
		dzxm.setBdcdyh(h == null || h.getBDCDYH() == null ? "" : h.getBDCDYH());
		dzxm.setZh("无");
		dzxm.setXmmc("无");
		dzxm.setQxdm(getQXDM(h));
		if (null != h) {
			dzxm.setZcs(h.getZCS() == null ? 0 : h.getZCS());
			dzxm.setGhyt(StringHelper.formatObject(h.getGHYT()));
			dzxm.setFwjg(StringHelper.formatObject(h.getFWJG()));
			dzxm.setJgsj(StringHelper.FormatDateOnType(h.getJGSJ(),"yyyy-MM-dd'T'HH:mm:ss"));
			if("".equals(dzxm.getJgsj()))
			{
				dzxm.setJgsj(null);
			}
			// dzxm.setJzmj();
			// dzxm.setZts(1);
		}
		dzxm.setQlid(StringHelper.formatObject(ql.getId()));
		dzxm.setXmbh(StringHelper.formatObject(ql.getXMBH()));
		dzxm.setBdcdyid(StringHelper.formatObject(h.getId()));
		dzxm.replaceEmpty();
		return dzxm;
	}

	/**
	 * 建筑物区分所有权业主共有部分表
	 * 
	 * @作者 diaoliwei
	 * @创建时间 2015年9月4日下午9:13:17
	 * @param fsyq
	 * @param h
	 * @param ql
	 * @param ywh
	 * @return
	 */
	public static QLFFWFDCQQFSYQ getQLFFWFDCQQFSYQ(QLFFWFDCQQFSYQ fsyq,
			House h, Rights ql, String ywh) {
		fsyq.setBdcdyh(h == null || h.getBDCDYH() == null ? "" : h.getBDCDYH());
		fsyq.setYsdm("2001030100");
		fsyq.setDjjg(ql == null || ql.getDJJG() == null ? "" : ql.getDJJG());
		fsyq.setDbr(ql == null || ql.getDBR() == null ? "" : ql.getDBR());
		fsyq.setDjsj(StringHelper.FormatDateOnType(ql.getDJSJ(),"yyyy-MM-dd'T'HH:mm:ss"));
		fsyq.setQszt("1");
		fsyq.setQxdm(getQXDM(h));
		fsyq.setQllx(ql == null || ql.getQLLX() == null ? "" : ql.getQLLX());
		fsyq.setJgzwbh("无");
		fsyq.setJgzwmc("无");
		fsyq.setJgzwsl(0);
		fsyq.setJgzwmj(0);
		fsyq.setYwh(StringHelper.formatDefaultValue(ywh));
		/*
		 * if(null != ql){ fsyq.setDbr(ql.getDBR() == null ? "" : ql.getDBR());
		 * fsyq.setQllx(ql.getQLLX() == null ? "" : ql.getQLLX());
		 * fsyq.setDjjg(ql.getDJJG() == null ? "" : ql.getDJJG()); } if(null !=
		 * h){ fsyq.setBdcdyh(h.getBDCDYH() == null ? "" : h.getBDCDYH());
		 * fsyq.setQxdm(h.getQXDM() == null ? "" : h.getQXDM()); }
		 */
		fsyq.setQlid(StringHelper.formatObject(ql.getId()));
		fsyq.setXmbh(StringHelper.formatObject(ql.getXMBH()));
		fsyq.setBdcdyid(StringHelper.formatObject(h.getId()));
		fsyq.replaceEmpty();
		return fsyq;
	}

	/**
	 * 登记收件
	 * 
	 * @作者 diaoliwei
	 * @创建时间 2015年9月7日下午1:55:25
	 * @param sj
	 * @param ywh
	 * @param qxdm
	 * @return
	 */
	public static List<DJFDJSJ> getDJFDJSJ(RealUnit unit,String ywh,String actinstID) {
		com.supermap.wisdombusiness.workflow.dao.CommonDao commonDao = SuperSpringContext.getContext().getBean(com.supermap.wisdombusiness.workflow.dao.CommonDao.class);

		DJFDJSJ sj=new DJFDJSJ();
		List<DJFDJSJ> sjlist = new ArrayList<DJFDJSJ>();
		List<Wfi_MaterData> materData = commonDao.getDataList(Wfi_MaterData.class, "select a.* from " + Common.WORKFLOWDB
								+ "WFI_MATERDATA a," + Common.WORKFLOWDB + "WFI_PROMATER b," + Common.WORKFLOWDB
								+ "WFI_ACTINST c where a.MATERILINST_ID = b.MATERILINST_ID and b.PROINST_ID = c.PROINST_ID and c.ACTINST_ID = '"
								+ actinstID + "' order by  b.MATERIAL_INDEX,a.file_index");
		
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT A.* FROM BDC_WORKFLOW.WFI_PROMATER A, BDC_WORKFLOW.WFI_ACTINST C WHERE A.PROINST_ID = C.PROINST_ID AND C.ACTINST_ID ='");
		sql.append(actinstID).append("' AND A.MATERILINST_ID IN( SELECT A.MATERILINST_ID");
		sql.append(" FROM BDC_WORKFLOW.WFI_MATERDATA A, BDC_WORKFLOW.WFI_PROMATER  B,BDC_WORKFLOW.WFI_ACTINST   C  WHERE A.MATERILINST_ID = B.MATERILINST_ID AND B.PROINST_ID = C.PROINST_ID");
		sql.append("  AND C.ACTINST_ID ='").append(actinstID).append("') ORDER BY A.MATERIAL_INDEX");
		List<Wfi_ProMater> promaters = commonDao.getDataList(Wfi_ProMater.class, sql.toString());
		int count = 0;
		String xmbh = "";
		List<BDCS_XMXX> xmxx = commonDao.getDataList(BDCS_XMXX.class,"SELECT * FROM BDCK.BDCS_XMXX WHERE YWLSH='"+ywh+"'");
		if(xmxx != null && xmxx.size()>0){
			xmbh = StringHelper.formatObject(xmxx.get(0).getId());
		}
		if(materData == null || materData.size()==0){
			sj.setYWH(StringHelper.formatDefaultValue(ywh));
			sj.setYSDM("6004030000");
			sj.setSJSJ(StringHelper.FormatDateOnType(new Date(),"yyyy-MM-dd'T'HH:mm:ss"));
			sj.setSJLX("1");
			sj.setSJMC("无");
			sj.setSJSL(1);
			sj.setSFSJSY("0");
			//sj.setSFSYBZ("0");
			sj.setSFEWSJ("0");
			sj.setSFBCSJ("0");
			sj.setYS(0);
			sj.setBZ("");
			sj.setXmbh(xmbh);
			if (unit!=null) {
				sj.setQXDM(getQXDM(unit));
			}
			sjlist.add(sj);
		}else{
			for (Wfi_ProMater wfi_ProMater : promaters) {//收件资料的目录
				count = 0;
				String sjsj = null;
				for (Wfi_MaterData  materdate : materData) {//收件资料的图片
					if(wfi_ProMater.getMaterilinst_Id().equals(materdate.getMaterilinst_Id())){
						count ++;
						if (StringHelper.isEmpty(sjsj)) {
							sjsj = StringHelper.FormatDateOnType(
									materdate.getUpload_Date() == null ? new Date() : materdate.getUpload_Date(),
									"yyyy-MM-dd'T'HH:mm:ss");
						}
					}
					
				}
				if (count > 0) {
					sj = new DJFDJSJ();
					sj.setYWH(StringHelper.formatDefaultValue(ywh));
					sj.setYSDM("6004030000");
					sj.setSJMC(wfi_ProMater.getMaterial_Name() == "" ? "无" : wfi_ProMater.getMaterial_Name());
					sj.setSJLX(wfi_ProMater.getMaterial_Type() == null ? "99"
							: wfi_ProMater.getMaterial_Type().toString());
					sj.setYS(wfi_ProMater.getMaterial_Pagecount() == null ? 1 : wfi_ProMater.getMaterial_Pagecount());
					sj.setSJSL(count);
					sj.setSFSJSY("1");//是否收缴收验
					sj.setSFEWSJ("0");//是否额外收件
					sj.setSFBCSJ("0");//是否补充收件
					sj.setBZ("");
					sj.setSJSJ(sjsj);
					sj.setXmbh(xmbh);
					sj.replayEmpty();
					if (unit!=null) {
						sj.setQXDM(getQXDM(unit));
					}
					sjlist.add(sj);
				}
				
			}
			
		}
		return sjlist;
	}
	
	public static List<DJFDJSJ> getDJFDJSJEx(DJFDJSJ sj, String ywh,  String actinstID,RealUnit unit) {
		com.supermap.wisdombusiness.workflow.dao.CommonDao commonDao = SuperSpringContext.getContext().getBean(com.supermap.wisdombusiness.workflow.dao.CommonDao.class);

		List<DJFDJSJ> sjlist = new ArrayList<DJFDJSJ>();
		List<Wfi_MaterData> materData = commonDao.getDataList(Wfi_MaterData.class, "select a.* from " + Common.WORKFLOWDB
								+ "WFI_MATERDATA a," + Common.WORKFLOWDB + "WFI_PROMATER b," + Common.WORKFLOWDB
								+ "WFI_ACTINST c where a.MATERILINST_ID = b.MATERILINST_ID and b.PROINST_ID = c.PROINST_ID and c.ACTINST_ID = '"
								+ actinstID + "' order by  b.MATERIAL_INDEX,a.file_index");
		
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT A.* FROM BDC_WORKFLOW.WFI_PROMATER A, BDC_WORKFLOW.WFI_ACTINST C WHERE A.PROINST_ID = C.PROINST_ID AND C.ACTINST_ID ='");
		sql.append(actinstID).append("' AND A.MATERILINST_ID IN( SELECT A.MATERILINST_ID");
		sql.append(" FROM BDC_WORKFLOW.WFI_MATERDATA A, BDC_WORKFLOW.WFI_PROMATER  B,BDC_WORKFLOW.WFI_ACTINST   C  WHERE A.MATERILINST_ID = B.MATERILINST_ID AND B.PROINST_ID = C.PROINST_ID");
		sql.append("  AND C.ACTINST_ID ='").append(actinstID).append("') ORDER BY A.MATERIAL_INDEX");
		List<Wfi_ProMater> promaters = commonDao.getDataList(Wfi_ProMater.class, sql.toString());
		int count = 0;
		String xmbh = "";
		List<BDCS_XMXX> xmxx = commonDao.getDataList(BDCS_XMXX.class,"SELECT * FROM BDCK.BDCS_XMXX WHERE YWLSH='"+ywh+"'");
		if(xmxx != null && xmxx.size()>0){
			xmbh = StringHelper.formatObject(xmxx.get(0).getId());
		}
		if(materData == null || materData.size()==0){
			sj.setYWH(StringHelper.formatDefaultValue(ywh));
			sj.setYSDM("6004030000");
			sj.setSJSJ(StringHelper.FormatDateOnType(new Date(),"yyyy-MM-dd'T'HH:mm:ss"));
			sj.setSJLX("1");
			sj.setSJMC("无");
			sj.setSJSL(1);
			sj.setSFSJSY("0");
			//sj.setSFSYBZ("0");
			sj.setSFEWSJ("0");
			sj.setSFBCSJ("0");
			sj.setYS(0);
			sj.setBZ("");
//			sj.setQXDM(ConfigHelper.getNameByValue("XZQHDM"));
			sj.setQXDM(getQXDM(unit));
			sj.setXmbh(xmbh);
			sj.replayEmpty();
			sjlist.add(sj);
		}else{
			//注释的循环造成一个目录有多个图片的时候，报文就会有多个收件名称相同的DJFDJSJ节点
//		    for (Wfi_MaterData  materdate : materData) {
//		    	    sj  = new DJFDJSJ();
//					sj.setYWH(StringHelper.formatDefaultValue(ywh));
//					sj.setYSDM("6004030000");
//					count = 1;
//					for (Wfi_ProMater wfi_ProMater : promaters) {
//						if(wfi_ProMater.getMaterilinst_Id().equals(materdate.getMaterilinst_Id())){
//							count ++;
//							sj.setSJMC(wfi_ProMater.getMaterial_Name() == "" ? "无" : wfi_ProMater.getMaterial_Name());
//							sj.setSJLX(wfi_ProMater.getMaterial_Type() == null ? "99" : wfi_ProMater.getMaterial_Type().toString());
//							sj.setYS(wfi_ProMater.getMaterial_Pagecount() == null ? 1 : wfi_ProMater.getMaterial_Pagecount());
//						}
//					}
//					sj.setSJSL(count);
//					sj.setSFSJSY("1");//是否收缴收验
//					sj.setSFEWSJ("0");//是否额外收件
//					sj.setSFBCSJ("0");//是否补充收件
//					sj.setBZ("");
//					sj.setSJSJ(StringHelper.FormatDateOnType(materdate.getUpload_Date() == null ? new Date() : materdate.getUpload_Date(),"yyyy-MM-dd'T'HH:mm:ss"));
//					sj.setQXDM(ConfigHelper.getNameByValue("XZQHDM"));
//					sj.replayEmpty();
//					sjlist.add(sj);
//			}
//		    
		    for (Wfi_ProMater wfi_ProMater : promaters) {//收件资料的目录
				count = 0;
				String sjsj = null;
				for (Wfi_MaterData  materdate : materData) {//收件资料的图片
					if(wfi_ProMater.getMaterilinst_Id().equals(materdate.getMaterilinst_Id())){
						count ++;
						if (StringHelper.isEmpty(sjsj)) {
							sjsj = StringHelper.FormatDateOnType(
									materdate.getUpload_Date() == null ? new Date() : materdate.getUpload_Date(),
									"yyyy-MM-dd'T'HH:mm:ss");
						}
					}
					
				}
				if (count > 0) {
					sj = new DJFDJSJ();
					sj.setYWH(StringHelper.formatDefaultValue(ywh));
					sj.setYSDM("6004030000");
					sj.setSJMC(wfi_ProMater.getMaterial_Name() == "" ? "无" : wfi_ProMater.getMaterial_Name());
					sj.setSJLX(wfi_ProMater.getMaterial_Type() == null ? "99"
							: wfi_ProMater.getMaterial_Type().toString());
					sj.setYS(wfi_ProMater.getMaterial_Pagecount() == null ? 1 : wfi_ProMater.getMaterial_Pagecount());
					sj.setSJSL(count);
					sj.setSFSJSY("1");//是否收缴收验
					sj.setSFEWSJ("0");//是否额外收件
					sj.setSFBCSJ("0");//是否补充收件
					sj.setBZ("");
					sj.setSJSJ(sjsj);
//					sj.setQXDM(ConfigHelper.getNameByValue("XZQHDM"));
					sj.setQXDM(getQXDM(unit));
					sj.setXmbh(xmbh);
					sj.replayEmpty();
					sjlist.add(sj);
				}
				
			}
		}
		return sjlist;
	}

	/**
	 * 登记收费
	 * 
	 * @作者 diaoliwei
	 * @创建时间 2015年9月7日下午1:53:57
	 * @param sf
	 * @param ywh
	 * @param qxdm
	 * @return
	 */
	public static List<DJFDJSF> getDJSF(RealUnit unit, String ywh,String xmbh) {
		DJFDJSF sf=null;
		List<DJFDJSF> sfList = new ArrayList<DJFDJSF>();
		String userName = Global.getCurrentUserName();
		CommonDao dao = SuperSpringContext.getContext()
				.getBean(CommonDao.class);
		List<BDCS_DJSF> list=dao.getDataList(BDCS_DJSF.class, "XMBH='"+xmbh+"'");
		if (null != list && list.size() > 0) {
			int i=0;
			for (BDCS_DJSF djsf : list) {
				i++;
				if(i>10){
					break;
				}
				sf = new DJFDJSF();
				sf.setYSDM("2004040000");
				sf.setQXDM(ConfigHelper.getNameByValue("XZQHDM"));
				sf.setQXDM(getQXDM(unit));
				sf.setFFF(StringHelper.formatObject(djsf.getFFF()) == "" ? "1": djsf.getFFF());
				sf.setYWH(StringHelper.formatDefaultValue(ywh));
				sf.setJFRQ(StringHelper.FormatDateOnType(new Date(),"yyyy-MM-dd'T'HH:mm:ss"));
				sf.setSFRQ(StringHelper.FormatDateOnType(new Date(),"yyyy-MM-dd'T'HH:mm:ss"));
				sf.setJFRY(StringHelper.formatDefaultValue(userName));
				sf.setSFKMMC(StringHelper.formatDefaultValue(djsf.getSFKMMC()));
				sf.setSFEWSF(StringHelper.formatDefaultValue(djsf.getSFEWSF()));
				sf.setSFJS(new Double(StringHelper.cut(StringHelper.getDouble(djsf.getSFJS()), 4)));
				sf.setSFLX(StringHelper.formatDefaultValue(djsf.getSFLX()));
				sf.setYSJE(StringHelper.cutBigDecimal(StringHelper.getBigDecimal(djsf.getYSJE()), 4));
				sf.setZKHYSJE(StringHelper.cutBigDecimal(StringHelper.getBigDecimal(djsf.getZKHYSJE()),4));
				sf.setSFRY(StringHelper.formatDefaultValue(djsf.getSFRY()));
				sf.setSJFFR(StringHelper.formatDefaultValue(djsf.getSJFFR()));
				sf.setSSJE(StringHelper.cutBigDecimal(StringHelper.getBigDecimal(djsf.getSSJE()),4));
				sf.setSFDW(StringHelper.formatDefaultValue(djsf.getSFDW()));
				sf.setXmbh(StringHelper.formatObject(xmbh));
				sf.replayEmpty();
				sfList.add(sf);
			}
		}
		return sfList;
	}
	
	public static List<DJFDJSF> getDJSFEx(DJFDJSF sf, String ywh,String xmbh,RealUnit unit) {
		List<DJFDJSF> sfList = new ArrayList<DJFDJSF>();
		ProjectServiceImpl serviceImpl = SuperSpringContext.getContext()
				.getBean(ProjectServiceImpl.class);
		Integer page = 1;
		Integer rows = 10;
		String userName = Global.getCurrentUserName();
		Message msg = serviceImpl.getPagedSFList(xmbh, page, rows);
		if (null != msg && msg.getTotal() > 0) {
			@SuppressWarnings("unchecked")
			List<BDCS_DJSF> list = (List<BDCS_DJSF>) msg.getRows();
			for (BDCS_DJSF djsf : list) {
				sf = new DJFDJSF();
				sf.setYSDM("2004040000");
//				sf.setQXDM(ConfigHelper.getNameByValue("XZQHDM"));
				
				sf.setQXDM(getQXDM(unit));
//				if(!StringHelper.isEmpty(unit)){
//					sf.setQXDM(StringHelper.formatObject(unit.getQXDM()) == "" ? ConfigHelper.getNameByValue("XZQHDM")
//							: unit.getQXDM());
//				}
				sf.setFFF(StringHelper.formatObject(djsf.getFFF()) == "" ? "1": djsf.getFFF());
				sf.setYWH(StringHelper.formatDefaultValue(ywh));
				sf.setJFRQ(StringHelper.FormatDateOnType(new Date(),"yyyy-MM-dd'T'HH:mm:ss"));
				sf.setSFRQ(StringHelper.FormatDateOnType(new Date(),"yyyy-MM-dd'T'HH:mm:ss"));
				sf.setJFRY(StringHelper.formatDefaultValue(userName));
				sf.setSFKMMC(StringHelper.formatDefaultValue(djsf.getSFKMMC()));
				sf.setSFEWSF(StringHelper.formatDefaultValue(djsf.getSFEWSF()));
				sf.setSFJS(new Double(StringHelper.cut(StringHelper.getDouble(djsf.getSFJS()), 4)));
				sf.setSFLX(StringHelper.formatDefaultValue(djsf.getSFLX()));
				sf.setYSJE(StringHelper.cutBigDecimal(StringHelper.getBigDecimal(djsf.getYSJE()), 4));
				sf.setZKHYSJE(StringHelper.cutBigDecimal(StringHelper.getBigDecimal(djsf.getZKHYSJE()),4));
				sf.setSFRY(StringHelper.formatDefaultValue(djsf.getSFRY()));
				sf.setSJFFR(StringHelper.formatDefaultValue(djsf.getSJFFR()));
				sf.setSSJE(StringHelper.cutBigDecimal(StringHelper.getBigDecimal(djsf.getSSJE()),4));
				sf.setSFDW(StringHelper.formatDefaultValue(djsf.getSFDW()));
				sf.setXmbh(StringHelper.formatObject(xmbh));
				sf.replayEmpty();
				sfList.add(sf);
			}
		}
		return sfList;
	}

	/**
	 * 登记发证
	 * 
	 * @作者 diaoliwei
	 * @创建时间 2015年9月7日下午1:53:41
	 * @param fz
	 * @param ywh
	 * @return
	 */
	public static List<DJFDJFZ> getDJFDJFZ(RealUnit unit,String ywh, String xmbh) {
		List<DJFDJFZ> fz=new ArrayList<DJFDJFZ>();
		CommonDao dao = SuperSpringContext.getContext()
				.getBean(CommonDao.class);
		String hql = " XMBH = '" + xmbh + "'";
		List<BDCS_DJFZ> list = dao.getDataList(BDCS_DJFZ.class, hql);
		DJFDJFZ djfz;
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				djfz=new DJFDJFZ();
				djfz.setYWH(StringHelper.formatDefaultValue(ywh));
				djfz.setYSDM("6004070000");
				djfz.setFZRY(StringHelper.formatDefaultValue(list.get(i).getFZRY()));
				djfz.setFZSJ(StringHelper.FormatDateOnType(list.get(i).getFZSJ(),"yyyy-MM-dd'T'HH:mm:ss"));
				djfz.setFZMC(StringHelper.formatDefaultValue(list.get(i).getFZMC()));
				djfz.setFZSL(list.get(i).getFZSL() == null ? 1 : list.get(i).getFZSL());
				djfz.setHFZSH(StringHelper.formatDefaultValue(list.get(i).getHFZSH()));
				djfz.setLZRXM(StringHelper.formatDefaultValue(list.get(i).getLZRXM()));
				djfz.setLZRZJLB(StringHelper.formatDefaultValue(list.get(i).getLZRZJLB()));
				djfz.setLZRZJHM(StringHelper.formatDefaultValue(list.get(i).getLZRZJHM()));
//				djfz.setQXDM(ConfigHelper.getNameByValue("XZQHDM"));
				djfz.setQXDM(getQXDM(unit));
				djfz.setLZRDH(StringHelper.formatDefaultValue(list.get(i).getLZRDH()));
				djfz.setLZRDZ(StringHelper.formatDefaultValue(list.get(i).getLZRDZ()));
				djfz.setLZRYB(StringHelper.formatDefaultValue(list.get(i).getLZRYB()));
				djfz.setBZ(StringHelper.formatDefaultValue(list.get(i).getLZRYB()));
				djfz.setXmbh(StringHelper.formatObject(xmbh));
				djfz.replaceEmpty();
				fz.add(djfz);
				djfz = null;
			}
		}
		return fz;
	}

	/**
	 * 登记归档
	 * 
	 * @作者 diaoliwei
	 * @创建时间 2015年9月7日下午1:53:20
	 * @param gd
	 * @param zd
	 * @param h
	 * @param ywh
	 * @return
	 */
	public static List<DJFDJGD> getDJFDJGD(RealUnit unit,String ywh, String xmbh) {

		CommonDao dao = SuperSpringContext.getContext().getBean(CommonDao.class);
		List<DJFDJGD> gd=new ArrayList<DJFDJGD>();
		String hql = " XMBH = '" + xmbh + "'";
		List<BDCS_DJGD> list = dao.getDataList(BDCS_DJGD.class, hql);
	
		DJFDJGD djgd;
		if (list != null && list.size() > 0) {
			
			for (int i = 0; i < list.size(); i++) {
				djgd = new DJFDJGD();
				djgd.setYWH(StringHelper.formatObject(ywh));
				djgd.setYSDM("6004080000");
				if("首次登记".equals(list.get(i).getDJDL())||"初始登记".equals(list.get(i).getDJDL())){
					djgd.setDJDL("100");
				}else if("转移登记".equals(list.get(i).getDJDL())){
					djgd.setDJDL("200");
				}else if("变更登记".equals(list.get(i).getDJDL())){
					djgd.setDJDL("300");
				}else if("注销登记".equals(list.get(i).getDJDL())){
					djgd.setDJDL("400");
				}else if("更正登记".equals(list.get(i).getDJDL())){
					djgd.setDJDL("500");
				}else if("异议登记".equals(list.get(i).getDJDL())){
					djgd.setDJDL("600");
				}else if("预告登记".equals(list.get(i).getDJDL())){
					djgd.setDJDL("700");
				}else if("查封登记".equals(list.get(i).getDJDL())){
					djgd.setDJDL("800");
				}else if("其他登记".equals(list.get(i).getDJDL())){
					djgd.setDJDL("900");
				}
				djgd.setQZHM(StringHelper.formatDefaultValue(list.get(i).getQZHM()));
				// gd.setJZH("");
				djgd.setWJJS(list.get(i).getWJJS() == null ? 0 : list.get(i).getWJJS());
				// gd.setZYS(1);
				djgd.setGDRY(StringHelper.formatDefaultValue(list.get(i).getGDZR()));
				djgd.setBZ(StringHelper.formatDefaultValue(list.get(i).getBZ()));
				djgd.setGDSJ(StringHelper.FormatDateOnType(list.get(i).getGDSJ(),"yyyy-MM-dd'T'HH:mm:ss"));
//				djgd.setQXDM(ConfigHelper.getNameByValue("XZQHDM"));
				djgd.setDJXL(list.get(i).getDJXL() == null ? "" : list.get(i).getDJXL().toString());
				/*-----------归档节点一旦在报文中出现，就必须含有DJDL和DJXL-----------*/
				//DJDL必须在100到900 //DJXL 校验规则：必须在0到50之间//都是String
				BDCS_XMXX xmxx  = dao.get(BDCS_XMXX.class, xmbh);
				if(xmxx!=null){
					djgd.setDJDL(xmxx.getDJLX() == null ? "" :xmxx.getDJLX());
					djgd.setDJXL(xmxx.getQLLX()== null ? "" :xmxx.getQLLX());
				}
				/*------------------------------------------------------------------*/
					djgd.setZL(StringHelper.formatDefaultValue(unit.getZL()));
					djgd.setQXDM(getQXDM(unit));
				djgd.setXmbh(StringHelper.formatObject(xmbh));
				djgd.replaceEmpty();
				gd.add(djgd);
				djgd = null;
			}
		}
		return gd;
	}
	
	public static List<DJFDJGD> getDJFDJGDEx(String ywh,String xmbh,RealUnit unit) {

		CommonDao dao = SuperSpringContext.getContext().getBean(CommonDao.class);
		List<DJFDJGD> gd=new  ArrayList<DJFDJGD>();
		String hql = " XMBH = '" + xmbh + "'";
		List<BDCS_DJGD> list = dao.getDataList(BDCS_DJGD.class, hql);
	
		DJFDJGD djgd;
		if (list != null && list.size() > 0) {
			
			for (int i = 0; i < list.size(); i++) {
				djgd = new DJFDJGD();
				djgd.setYWH(StringHelper.formatObject(ywh));
				djgd.setYSDM("6004080000");
				djgd.setDJDL(StringHelper.formatDefaultValue(list.get(i).getDJDL()));
				djgd.setQZHM(StringHelper.formatDefaultValue(list.get(i).getQZHM()));
				// gd.setJZH("");
				djgd.setWJJS(list.get(i).getWJJS() == null ? 0 : list.get(i).getWJJS());
				// gd.setZYS(1);
				djgd.setGDRY(StringHelper.formatDefaultValue(list.get(i).getGDZR()));
				djgd.setBZ(StringHelper.formatDefaultValue(list.get(i).getBZ()));
				djgd.setGDSJ(StringHelper.FormatDateOnType(list.get(i).getGDSJ(),"yyyy-MM-dd'T'HH:mm:ss"));
				djgd.setQXDM(getQXDM(unit));
				djgd.setDJXL(list.get(i).getDJXL() == null ? "" : list.get(i).getDJXL().toString());
				// gd.setDJDL(ql.getDJLX() == null ? "" :ql.getDJLX());
				// gd.setDJXL(ql.getQLLX()== null ? "" :ql.getQLLX());
				// gd.setQZHM(ql.getBDCQZH()== null ? "" :ql.getBDCQZH());
				if(!StringHelper.isEmpty(unit)){
					djgd.setZL(StringHelper.formatDefaultValue(unit.getZL()));
					djgd.setQXDM(StringHelper.formatObject(unit.getQXDM()) == "" ? ConfigHelper.getNameByValue("XZQHDM") : unit.getQXDM());
				}
				djgd.setXmbh(StringHelper.formatObject(xmbh));
				djgd.replaceEmpty();
				gd.add(djgd);
				djgd = null;
			}
		}
		return gd;
	}

	/**
	 * 抵押权常规登记报文
	 * 
	 * @作者 diaoliwei
	 * @创建时间 2015年9月1日下午5:34:43
	 * @param dyaq
	 * @param zd
	 * @param ql
	 * @param fsql
	 * @param ywh
	 * @param h
	 * @return
	 */
	public static QLFQLDYAQ getQLFQLDYAQ(QLFQLDYAQ dyaq, UseLand zd, Rights ql,
			SubRights fsql, String ywh, House h) {
		ql.setQSZT(1);
		dyaq.setBdbzzqse(fsql == null || fsql.getBDBZZQSE() == null ? new BigDecimal(0) : StringHelper.cutBigDecimal(StringHelper.getBigDecimal(fsql
				.getBDBZZQSE()/10000), 4));
		dyaq.setBdcdjzmh("无");
		dyaq.setBdcdyh("无");
		dyaq.setYsdm("2002030100");
		dyaq.setQxdm("");
		dyaq.setScywh("0");
		if (ql != null && !StringHelper.isEmpty(ql.getXMBH())) {
			ProjectInfo projectinfo = ProjectHelper.GetPrjInfoByXMBH(ql.getXMBH());
			if (!StringHelper.isEmpty(projectinfo)) {
				dyaq.setDyjelx(projectinfo.getSfhbzs());
			}
		}
		if (null != zd) {
			dyaq.setBdcdyh(StringHelper.formatDefaultValue(zd.getBDCDYH()));
			dyaq.setQxdm(getQXDM(zd));
			dyaq.setDybdclx(getDYBDCLXfromBDCDYLX(BDCDYLX.SHYQZD));
		}
		if (null != h) {
			dyaq.setBdcdyh(StringHelper.formatDefaultValue(h.getBDCDYH()));
			dyaq.setQxdm(getQXDM(h));
			dyaq.setDybdclx(getDYBDCLXfromBDCDYLX(BDCDYLX.H));
		}
		dyaq.setDbr(ql == null || ql.getDBR() == null ? "无" : ql.getDBR());
		dyaq.setDjjg(ql == null || ql.getDJJG() == null ? "无" : ql.getDJJG());
		dyaq.setDjlx(ql == null || ql.getDJLX() == null ? "" : ql.getDJLX());
		dyaq.setDjsj(StringHelper.FormatDateOnType(ql.getDJSJ(),"yyyy-MM-dd'T'HH:mm:ss"));
		dyaq.setDjyy(ql == null || ql.getDJYY() == null ? "无" : ql.getDJYY());
		dyaq.setDyfs(fsql == null || fsql.getDYFS() == null ? "1" : fsql
				.getDYFS());
		dyaq.setDyr(fsql == null || fsql.getDYR() == null ? "无" : fsql.getDYR());
		dyaq.setFj(ql == null || ql.getFJ() == null ? "" : ql.getFJ());
		dyaq.setQszt(ql == null || ql.getQSZT() == null ? "1" : ql.getQSZT().toString());
		dyaq.setBdcdjzmh(ql == null || ql.getBDCQZH() == null ? "无" : ql
				.getBDCQZH());
		dyaq.setYwh(StringHelper.formatDefaultValue(ywh));
		if (fsql != null) {
			dyaq.setZgzqqdss(StringHelper.formatDefaultValue(fsql.getZGZQQDSS()));
			dyaq.setZgzqse(fsql == null || fsql.getZGZQSE() == null ? new BigDecimal(0) : StringHelper.cutBigDecimal(StringHelper.getBigDecimal(fsql
					.getZGZQSE()/10000), 4));
			
			if(!StringUtils.isEmpty(fsql.getZXDYYWH())){
				dyaq.setZxdyywh(StringHelper.formatDefaultValue(GetYWLSHByYWH(fsql.getZXDYYWH())));
			}
			dyaq.setZjjzwdyfw(StringHelper.formatDefaultValue(fsql.getZJJZWDYFW()));
			dyaq.setZjjzwzl(StringHelper.formatDefaultValue(fsql.getZJJZWZL()));
			if(!StringHelper.isEmpty(fsql.getZXDYYY())){
				dyaq.setZxdyyy(StringHelper.formatDefaultValue(fsql.getZXDYYY()));
			}else{
				dyaq.setZxdyyy("");
			}
			
			dyaq.setZxsj(null);
			if(!StringHelper.isEmpty(StringHelper.FormatDateOnType(fsql.getZXSJ(),"yyyy-MM-dd'T'HH:mm:ss"))){
				dyaq.setZxsj(StringHelper.FormatDateOnType(fsql.getZXSJ(),"yyyy-MM-dd'T'HH:mm:ss"));
			}
		}
		dyaq.setZwlxjssj(StringHelper.FormatDateOnType(ql.getQLJSSJ(),"yyyy-MM-dd'T'HH:mm:ss"));
		dyaq.setZwlxqssj(StringHelper.FormatDateOnType(ql.getQLQSSJ(),"yyyy-MM-dd'T'HH:mm:ss"));
		dyaq.setQlid(StringHelper.formatDefaultValue(ql.getId()));
		dyaq.setXmbh(StringHelper.formatDefaultValue(ql.getXMBH()));
		if(h!=null) {
			dyaq.setBdcdyid(StringHelper.formatObject(h.getId()));
		}else if(zd!=null) {
			dyaq.setBdcdyid(StringHelper.formatObject(zd.getId()));
		}
		
		dyaq.replaceEmpty();
		return dyaq;
	}
	
	public static QLFQLDYAQ getQLFQLDYAQEx(QLFQLDYAQ dyaq,  Rights ql,
			SubRights fsql, String ywh,RealUnit unit) {
		ql.setQSZT(1);
		dyaq.setBdbzzqse(fsql == null || fsql.getBDBZZQSE() == null ? new BigDecimal(0) : StringHelper.cutBigDecimal(StringHelper.getBigDecimal(fsql
				.getBDBZZQSE()/10000), 4));
		dyaq.setBdcdjzmh("无");
		dyaq.setBdcdyh("无");
		dyaq.setYsdm("2002030100");
		dyaq.setQxdm("");
		dyaq.setScywh("0");
		if (ql != null && !StringHelper.isEmpty(ql.getXMBH())) {
			ProjectInfo projectinfo = ProjectHelper.GetPrjInfoByXMBH(ql.getXMBH());
			if (!StringHelper.isEmpty(projectinfo)) {
				dyaq.setDyjelx(projectinfo.getSfhbzs());
			}
		}
		if(!StringHelper.isEmpty(unit)){
			dyaq.setBdcdyh(StringHelper.formatDefaultValue(unit.getBDCDYH()));
			dyaq.setQxdm(getQXDM(unit));
			dyaq.setDybdclx(getDYBDCLXfromBDCDYLX(unit.getBDCDYLX()));
		}
		dyaq.setDbr(ql == null || ql.getDBR() == null ? "无" : ql.getDBR());
		dyaq.setDjjg(ql == null || ql.getDJJG() == null ? "无" : ql.getDJJG());
		dyaq.setDjlx(ql == null || ql.getDJLX() == null ? "" : ql.getDJLX());
		dyaq.setDjsj(StringHelper.FormatDateOnType(ql.getDJSJ(),"yyyy-MM-dd'T'HH:mm:ss"));
		dyaq.setDjyy(ql == null || ql.getDJYY() == null ? "无" : ql.getDJYY());
		dyaq.setDyfs(fsql == null || fsql.getDYFS() == null ? "" : fsql
				.getDYFS());
		dyaq.setDyr(fsql == null || fsql.getDYR() == null ? "无" : fsql.getDYR());
		dyaq.setFj(ql == null || ql.getFJ() == null ? "" : ql.getFJ());
		dyaq.setQszt(ql == null || ql.getQSZT() == null ? "1" : ql.getQSZT().toString());
		dyaq.setBdcdjzmh(ql == null || ql.getBDCQZH() == null ? "无" : ql
				.getBDCQZH());
		dyaq.setYwh(StringHelper.formatDefaultValue(ywh));
		if (fsql != null) {
			dyaq.setZgzqqdss(StringHelper.formatDefaultValue(fsql.getZGZQQDSS()));
			dyaq.setZgzqse(fsql == null || fsql.getZGZQSE() == null ? new BigDecimal(0) : StringHelper.cutBigDecimal(StringHelper.getBigDecimal(fsql
					.getZGZQSE()/10000), 4));
			
			if(!StringUtils.isEmpty(fsql.getZXDYYWH())){
				dyaq.setZxdyywh(StringHelper.formatDefaultValue(GetYWLSHByYWH(fsql.getZXDYYWH())));
			}
			dyaq.setZjjzwdyfw(StringHelper.formatDefaultValue(fsql.getZJJZWDYFW()));
			dyaq.setZjjzwzl(StringHelper.formatDefaultValue(fsql.getZJJZWZL()));
			if(!StringHelper.isEmpty(fsql.getZXDYYY())){
				dyaq.setZxdyyy(StringHelper.formatDefaultValue(fsql.getZXDYYY()));
			}else{
				dyaq.setZxdyyy("");
			}
			
			dyaq.setZxsj(null);
			if(!StringHelper.isEmpty(StringHelper.FormatDateOnType(fsql.getZXSJ(),"yyyy-MM-dd'T'HH:mm:ss"))){
				dyaq.setZxsj(StringHelper.FormatDateOnType(fsql.getZXSJ(),"yyyy-MM-dd'T'HH:mm:ss"));
			}
		}
		dyaq.setZwlxjssj(StringHelper.FormatDateOnType(ql.getQLJSSJ(),"yyyy-MM-dd'T'HH:mm:ss"));
		dyaq.setZwlxqssj(StringHelper.FormatDateOnType(ql.getQLQSSJ(),"yyyy-MM-dd'T'HH:mm:ss"));
		dyaq.setQlid(StringHelper.formatDefaultValue(ql.getId()));
		dyaq.setXmbh(StringHelper.formatDefaultValue(ql.getXMBH()));
		dyaq.setBdcdyid(StringHelper.formatObject(unit.getId()));
		dyaq.replaceEmpty();
		return dyaq;
	}

	/**
	 * 查封登记报文
	 * 
	 * @作者 diaoliwei
	 * @创建时间 2015年8月31日下午10:22:53
	 * @return
	 */
	public static QLFQLCFDJ getQLFQLCFDJ(QLFQLCFDJ cfdj, UseLand zd, House h,
			Rights ql, SubRights fsql, String ywh,Sea zh) {
		ql.setQSZT(1);
		if(fsql != null && fsql.getZXDYYWH() != null ){
			cfdj.setJfywh(StringHelper.formatObject(GetYWLSHByYWH(fsql.getZXDYYWH())));
		}
		cfdj.setQszt("");
		cfdj.setQxdm("");
		cfdj.setYsdm("2002040300");
		cfdj.setBdcdyh("");
		cfdj.setCffw(fsql == null || fsql.getCFFW() == null ? "无" : fsql
				.getCFFW());
		cfdj.setCfjg(fsql == null || fsql.getCFJG() == null ? "无" : fsql
				.getCFJG());
		cfdj.setCfjssj(StringHelper.FormatDateOnType(ql.getQLJSSJ(),"yyyy-MM-dd'T'HH:mm:ss"));
		cfdj.setCflx(fsql == null || fsql.getCFLX() == null ? "" : fsql
				.getCFLX());
		cfdj.setCfqssj(StringHelper.FormatDateOnType(ql.getQLQSSJ(),"yyyy-MM-dd'T'HH:mm:ss"));
		cfdj.setCfwh(fsql == null || fsql.getCFWH() == null ? "无" : fsql
				.getCFWH());
		cfdj.setCfwj(fsql == null || fsql.getCFWJ() == null ? "无" : fsql
				.getCFWJ());
		cfdj.setDbr("无");
		cfdj.setDjjg("无");
		cfdj.setDjsj(StringHelper.FormatDateOnType(new Date(),"yyyy-MM-dd'T'HH:mm:ss"));
		cfdj.setFj("");
		cfdj.setJfdbr(StringHelper.formatObject(fsql.getZXDBR()));
		// cfdj.setJfdjsj();
		cfdj.setJfjg(fsql == null || fsql.getJFJG() == null ? "无" : fsql
				.getJFJG());
		cfdj.setJfwh(fsql == null || fsql.getJFWH() == null ? "无" : fsql
				.getJFWH());
		cfdj.setJfwj(fsql == null || fsql.getJFWJ() == null ? "无" : fsql
				.getJFWJ());
		cfdj.setYwh(StringHelper.formatDefaultValue(ywh));
		if (ql != null) {
			cfdj.setQszt(StringHelper.formatObject(ql.getQSZT()));
			// cfdj.setDbr(StringHelper.formatObject(ql.getDBR()));
			// cfdj.setDjjg(StringHelper.formatObject(ql.getDJJG()));
			cfdj.setFj(StringHelper.formatDefaultValue(ql.getFJ()));
			cfdj.setDjsj(StringHelper.FormatDateOnType(ql.getDJSJ(),"yyyy-MM-dd'T'HH:mm:ss"));
			cfdj.setDbr(StringHelper.formatDefaultValue(ql.getDBR()));
			if(QLLX.QTQL.Value.equals(ql.getQLLX())){
				cfdj.setJfjg(null);
				cfdj.setJfdbr(null);
				cfdj.setJfdjsj(null);
				cfdj.setJfwh(null);
				cfdj.setJfwj(null);
				cfdj.setJfywh(null);
			}else{
				if(fsql!=null){
					cfdj.setJfdbr(fsql.getZXDBR());
					cfdj.setJfdjsj(StringHelper.FormatDateOnType(fsql.getZXSJ(),"yyyy-MM-dd'T'HH:mm:ss"));
					cfdj.setJfjg(fsql.getJFJG());
					cfdj.setJfwh(fsql.getJFWH());
					cfdj.setJfwj(fsql.getJFWJ());
					cfdj.setJfywh(GetYWLSHByYWH(fsql.getZXDYYWH()));
				}
			}
			cfdj.setQlid(StringHelper.formatDefaultValue(ql.getId()));
			cfdj.setXmbh(StringHelper.formatDefaultValue(ql.getXMBH()));
		}
		if (h != null) {
			cfdj.setQxdm(getQXDM(h));
			cfdj.setBdcdyh(StringHelper.formatDefaultValue(h.getBDCDYH()));
			cfdj.setBdcdyid(StringHelper.formatObject(h.getId()));
		}
		if (zd != null) {
//			cfdj.setQxdm(StringHelper.formatObject(zd.getQXDM()) == "" ? ConfigHelper.getNameByValue("XZQHDM")
//					: zd.getQXDM());
			cfdj.setQxdm(getQXDM(zd));
			cfdj.setBdcdyh(StringHelper.formatDefaultValue(zd.getBDCDYH()));
			cfdj.setBdcdyid(StringHelper.formatObject(zd.getId()));
		}
		if(zh!=null) {
//			cfdj.setQxdm(StringHelper.formatObject(zh.getQXDM()) == "" ? ConfigHelper.getNameByValue("XZQHDM")
//					: zh.getQXDM());
			cfdj.setQxdm(getQXDM(zh));
			cfdj.setBdcdyh(StringHelper.formatDefaultValue(zh.getBDCDYH()));
			cfdj.setBdcdyid(StringHelper.formatObject(zh.getId()));
		}
		cfdj.replaceEmpyt();
		return cfdj;
	}

	/**
	 * 预告登记信息
	 * 
	 * @作者 diaoliwei
	 * @创建时间 2015年9月15日下午2:07:52
	 * @param ygdj
	 * @return
	 */
	public static QLFQLYGDJ getQLFQLYGDJ(QLFQLYGDJ ygdj, House house,
			Rights ql, SubRights fsql, String ywh) {
		ql.setQSZT(1);
		ygdj.setYsdm("2002040100");
		ygdj.setBdcdyh("无");
		ygdj.setBdczl("无");
		ygdj.setYwr("无");
		ygdj.setYgdjzl("");
		ygdj.setDjlx("");
		ygdj.setDjyy("无");
		ygdj.setSzc(0);
		ygdj.setZcs(1);
		ygdj.setJzmj(0);
		ygdj.setBdcdjzmh("无");
		//ygdj.setQxdm("");
		ygdj.setDjjg("无");
		ygdj.setDbr("无");
		ygdj.setScywh("0");
		ygdj.setDjsj(StringHelper.FormatDateOnType(new Date(),"yyyy-MM-dd'T'HH:mm:ss"));
		ygdj.setYwh(StringHelper.formatDefaultValue(ywh));
		if (ql != null) {
			ygdj.setQszt(StringHelper.formatObject(ql.getQSZT()));
			ygdj.setFj(StringHelper.formatDefaultValue(ql.getFJ()));
			ygdj.setDbr(StringHelper.formatDefaultValue(ql.getDBR()));
			ygdj.setDjjg(StringHelper.formatDefaultValue(ql.getDJJG()));
			ygdj.setBdcdjzmh(StringHelper.formatDefaultValue(ql.getBDCQZH()));
			//单位以元转换成万元
			Double qdjg=StringHelper.getDouble(ql.getQDJG())/10000.0;
			ygdj.setQdjg(StringHelper.cutBigDecimal(StringHelper.getBigDecimal(qdjg), 4));
//			ygdj.setQdjg(StringHelper.cutBigDecimal(StringHelper.getBigDecimal(ql.getQDJG()), 4));
			ygdj.setDjyy(StringHelper.formatDefaultValue(ql.getDJYY()));
			ygdj.setDjlx(StringHelper.formatObject(ql.getDJLX()));
			ygdj.setDjsj(StringHelper.FormatDateOnType(ql.getDJSJ(),"yyyy-MM-dd'T'HH:mm:ss"));
			ygdj.setQlid(StringHelper.formatDefaultValue(ql.getId()));
			ygdj.setXmbh(StringHelper.formatDefaultValue(ql.getXMBH()));
		}
		if (fsql != null) {
			ygdj.setYwr(StringHelper.formatDefaultValue(fsql.getYWR()));
			if (!StringUtils.isEmpty(fsql.getYWRZJZL())) {
				ygdj.setYwrzjzl(StringHelper.formatObject(fsql.getYWRZJZL()));
			}
			ygdj.setYwrzjh(StringHelper.formatDefaultValue(fsql.getYWRZJH()));
			ygdj.setYgdjzl(StringHelper.formatDefaultValue(fsql.getYGDJZL()));
		}
		if (house != null) {
			ygdj.setBdcdyh(StringHelper.formatDefaultValue(house.getBDCDYH()));
			ygdj.setBdczl(StringHelper.formatDefaultValue(house.getZL()));
			ygdj.setTdsyqr(StringHelper.formatDefaultValue(house.getTDSYQR()));
			if (!StringUtils.isEmpty(house.getGHYT())) {
				ygdj.setGhyt(StringHelper.formatObject(house.getGHYT()));
			}
			if (!StringUtils.isEmpty(house.getFWXZ())) {
				ygdj.setFwxz(StringHelper.formatObject(house.getFWXZ()));
			}
			if (!StringUtils.isEmpty(house.getFWJG())) {
				ygdj.setFwjg(StringHelper.formatObject(house.getFWJG()));
			}
			ygdj.setSzc(house.getQSC() == null ? 0 : house.getQSC().intValue());
			ygdj.setZcs(house.getZCS() == null ? 1 : house.getZCS());
			ygdj.setJzmj(StringHelper.getDouble(house.getYCJZMJ()));
			ygdj.setQxdm(getQXDM(house));
			ygdj.setBdcdyid(StringHelper.formatObject(house.getId()));
		}
		ygdj.replaceEmpty();
		return ygdj;
	}

	/**
	 * 异议登记信息
	 * 
	 * @作者 diaoliwei
	 * @创建时间 2015年9月15日下午5:19:25
	 * @param yydj
	 * @param ql
	 * @param fsql
	 * @param ywh
	 * @param house
	 * @param land
	 * @return
	 */
	public static QLFQLYYDJ getQLFQLYYDJ(QLFQLYYDJ yydj, Rights ql,
			SubRights fsql, String ywh, House house, UseLand land) {
		yydj.setYsdm("2002040200");
		yydj.setBdcdyh("无");
		yydj.setYwh(StringHelper.formatObject(ywh));
		yydj.setYysx("无");
		yydj.setBdcdjzmh("无");
		yydj.setQxdm("");
		yydj.setDjjg("无");
		yydj.setDbr("无");
		yydj.setDjsj(StringHelper.FormatDateOnType(new Date(),"yyyy-MM-dd'T'HH:mm:ss"));
		yydj.setQszt("1");
		if (ql != null) {
			yydj.setBdcdjzmh(StringHelper.formatDefaultValue(ql.getBDCQZH()));
			yydj.setDjjg(StringHelper.formatDefaultValue(ql.getDJJG()));
			yydj.setDbr(StringHelper.formatDefaultValue(ql.getDBR()));
//			yydj.setQszt(StringHelper.formatObject(ql.getQSZT()) == "" ? "1" : StringHelper.formatObject(ql.getQSZT()));
			yydj.setFj(StringHelper.formatDefaultValue(ql.getFJ()));
			yydj.setDjsj(StringHelper.FormatDateOnType(ql.getDJSJ(),"yyyy-MM-dd'T'HH:mm:ss"));
			yydj.setQlid(StringHelper.formatDefaultValue(ql.getId()));
			yydj.setXmbh(StringHelper.formatDefaultValue(ql.getXMBH()));
		}
		if (fsql != null) {
			yydj.setZxyydbr(StringHelper.formatDefaultValue(fsql.getZXDBR()));
			yydj.setZxyyywh(StringHelper.formatDefaultValue(GetYWLSHByYWH(fsql.getZXDYYWH())));
			yydj.setYysx(StringHelper.formatDefaultValue(fsql.getYYSX()));
			yydj.setZxyyyy(StringHelper.formatDefaultValue(fsql.getZXYYYY()));
			yydj.setZxyydjsj(StringHelper.FormatDateOnType(fsql.getZXSJ(),"yyyy-MM-dd'T'HH:mm:ss"));
		}
		if (house != null) {
			yydj.setQxdm(getQXDM(house));
			yydj.setBdcdyh(StringHelper.formatDefaultValue(house.getBDCDYH()));
			yydj.setBdcdyid(StringHelper.formatObject(house.getId()));
		}
		if (land != null) {
			yydj.setQxdm(getQXDM(land));
			yydj.setBdcdyh(StringHelper.formatDefaultValue(land.getBDCDYH()));
			yydj.setBdcdyid(StringHelper.formatObject(land.getId()));
		}
		yydj.replaceEmpty();
		return yydj;
	}

	/**
	 * 注销登记
	 * 
	 * @param zxdj
	 * @return
	 */
	public static QLFZXDJ getZXDJ(QLFZXDJ zxdj, Rights rights, String ywh,
			String qxdm,SubRights subrights) {
		zxdj.setYsdm("2002029900");
		zxdj.setBdcdyh("无");
		zxdj.setYwh(StringHelper.formatDefaultValue(ywh));
		zxdj.setZxsj(StringHelper.FormatDateOnType(new Date(),"yyyy-MM-dd'T'HH:mm:ss"));
		if(rights!=null&&!StringHelper.isEmpty(rights.getBDCDYH())&&rights.getBDCDYH().length()>=6) {
			zxdj.setQxdm(rights.getBDCDYH().substring(0, 6));
		}
		zxdj.setDjjg("无");
		zxdj.setDbr("无");
		zxdj.setZxywh(ywh);
		if (null != rights) {
//			zxdj.setDbr(StringHelper.formatDefaultValue(rights.getDBR()));
			zxdj.setDjjg(StringHelper.formatDefaultValue(rights.getDJJG()));
			zxdj.setDjsj(StringHelper.FormatDateOnType(rights
					.getDJSJ(),"yyyy-MM-dd'T'HH:mm:ss"));
			zxdj.setBdcqzh(StringHelper.formatDefaultValue(rights.getBDCQZH()));
			zxdj.setBdcdyh(StringHelper.formatDefaultValue(rights.getBDCDYH()));
			// zxdj.setZxsj(StringHelper.FormatDateOnType(rights.getzxsj));
			// zxdj.setBz(StringHelper.formatObject(rights.getbz));
			zxdj.setQlid(StringHelper.formatDefaultValue(rights.getId()));
			zxdj.setXmbh(StringHelper.formatDefaultValue(rights.getXMBH()));
		}
		if(!StringHelper.isEmpty(subrights)){
			zxdj.setDbr(StringHelper.formatDefaultValue(subrights.getZXDBR()));
		//	zxdj.setZxsj(StringHelper.FormatDateOnType(subrights.getZXSJ(),"yyyy-MM-dd'T'HH:mm:ss"));
			zxdj.setBz(StringHelper.formatDefaultValue(subrights.getZXDYYY()));
		}
		zxdj.setBdcdyid(StringHelper.formatObject(rights.getId()));
		zxdj.replaceEmpty();
		return zxdj;
	}

	/**
	 * 林权表
	 * 
	 * @作者 diaoliwei
	 * @创建时间 2015年9月21日下午11:28:16
	 * @param lq
	 * @param ql
	 * @param forest
	 * @param ywh
	 * @return
	 */
	public static QLTQLLQ getQLTQLLQ(QLTQLLQ lq, Rights ql, Forest forest,
			String ywh) {
		lq.setYwh(StringHelper.formatDefaultValue(ywh));
		lq.setYsdm("2002020800");
		lq.setQllx("");
		lq.setDjlx("");
		lq.setDjyy("无");
		lq.setFbf("");
		lq.setSyqmj(new BigDecimal(0));
		lq.setLdsyqssj(StringHelper.FormatDateOnType(ql.getQLQSSJ(),"yyyy-MM-dd'T'HH:mm:ss"));
		lq.setLdsyjssj(StringHelper.FormatDateOnType(ql.getQLJSSJ(),"yyyy-MM-dd'T'HH:mm:ss"));
		lq.setZysz("");
		lq.setZs(0);
		lq.setLz("");
		lq.setQy("");
		lq.setZlnd(0);
		lq.setLb("");
		lq.setXb("");
		lq.setBdcqzh("");
		lq.setQxdm("");
		lq.setDbr("");
		lq.setDjsj(StringHelper.FormatDateOnType(ql.getDJSJ(),"yyyy-MM-dd'T'HH:mm:ss"));
		lq.setQszt("1");
		if (null != ql) {
			lq.setQszt(StringHelper.formatObject(ql.getQSZT()) == "" ? "1" : StringHelper.formatObject(ql.getQSZT()));
			lq.setFj(StringHelper.formatObject(ql.getFJ()));
			lq.setDbr(StringHelper.formatObject(ql.getDBR()));
			lq.setDjjg(StringHelper.formatObject(ql.getDJJG()));
			lq.setBdcqzh(StringHelper.formatObject(ql.getBDCQZH()));
			lq.setDjyy(StringHelper.formatDefaultValue(ql.getDJYY()));
			lq.setDjlx(StringHelper.formatObject(ql.getDJLX()));
			lq.setQllx(StringHelper.formatObject(ql.getQLLX()));
			lq.setQlid(StringHelper.formatDefaultValue(ql.getId()));
			lq.setXmbh(StringHelper.formatDefaultValue(ql.getXMBH()));
		}
		if (null != forest) {
			lq.setQxdm(getQXDM(forest));
			lq.setXdm(StringHelper.formatObject(forest.getXDM()));
			lq.setXb(StringHelper.formatObject(forest.getXB()));
			lq.setLb(StringHelper.formatObject(forest.getLB()));
			lq.setZlnd(forest.getZLND() == null ? 0 : forest.getZLND());
			lq.setQy(StringHelper.formatObject(forest.getQY()));
			lq.setLz(StringHelper.formatObject(forest.getLZ()));
			lq.setZs(forest.getZS() == null ? 0 : forest.getZS());
			lq.setZysz(StringHelper.formatObject(forest.getZYSZ()));
			// lq.setSllmsyqr2(StringHelper.formatObject(forest.getSl));
			// lq.setSllmsyqr1("");
			lq.setSyqmj(StringHelper.cutBigDecimal(StringHelper.getBigDecimal(forest.getSYQMJ()), 2));
			lq.setFbf("");
			lq.setBdcdyh(StringHelper.formatObject(forest.getBDCDYH()));
			lq.setBdcdyid(StringHelper.formatObject(forest.getId()));
		}
		lq.replaceEmpty();
		return lq;
	}

	/**
	 * 宗海基本信息
	 * 
	 * @作者 diaoliwei
	 * @创建时间 2015年9月22日下午5:17:09
	 * @param zhjbxx
	 * @param ql
	 * @param sea
	 * @return
	 */
	public static KTTZHJBXX getKTTZHJBXX(KTTZHJBXX zhjbxx, Rights ql, Sea sea) {
		zhjbxx.setZhdm("无");
		zhjbxx.setBdcdyh("无");
		zhjbxx.setYsdm("2002020600");
		zhjbxx.setZhtzm("无");
		zhjbxx.setXmmc("无");
		zhjbxx.setZht("");
		zhjbxx.setZt("1");
		zhjbxx.setQxdm("");
		zhjbxx.setDjsj(StringHelper.FormatDateOnType(ql.getDJSJ(),"yyyy-MM-dd'T'HH:mm:ss"));
		zhjbxx.setDbr("无");
		bsm = bsm++;
		zhjbxx.setBsm(bsm);
		zhjbxx.setDah("无");// 档案号
		zhjbxx.setDjjgbm("无");// 登记机构编码
		zhjbxx.setDjjgmc("无");// 登记机构名称
		zhjbxx.setJdh("无");// 街道
		zhjbxx.setJfh("无");// 街坊（村）
		zhjbxx.setZh("无");// 组
		if (null != ql) {
			zhjbxx.setFj(StringHelper.formatObject(ql.getFJ()));
			zhjbxx.setDbr(StringHelper.formatObject(ql.getDBR()));
			//zhjbxx.setZt(StringHelper.formatObject(ql.getZT()));
		}
		if (null != sea) {
			zhjbxx.setQxdm(getQXDM(sea));
			zhjbxx.setZht(StringHelper.formatObject(sea.getZHT()));
			if (!StringUtils.isEmpty(sea.getHDYT())) {
				zhjbxx.setHdyt(StringHelper.formatObject(sea.getHDYT()));
			}
			zhjbxx.setHdwz(StringHelper.formatObject(sea.getHDWZ()));
			zhjbxx.setYdmj(StringHelper.cutBigDecimal(StringHelper.getBigDecimal(sea.getYDMJ()), 2));
			zhjbxx.setYdfw(StringHelper.formatObject(sea.getYDFW()));
			zhjbxx.setHddm(StringHelper.formatObject(sea.getHDDM()));
			zhjbxx.setHdmc(StringHelper.formatObject(sea.getHDMC()));
			zhjbxx.setYhwzsm(StringHelper.formatObject(sea.getYHWZSM()));
			zhjbxx.setYhlxb(StringHelper.formatObject(sea.getYHLXB()));
			zhjbxx.setYhlxa(StringHelper.formatObject(sea.getYHLXA()));
			zhjbxx.setZhax(StringHelper.cutBigDecimal(StringHelper.getBigDecimal(sea.getZHAX()), 2));
			if (!StringUtils.isEmpty(sea.getDB())) {
				zhjbxx.setDb(StringHelper.formatObject(sea.getDB()));
			}
			zhjbxx.setZhmj(StringHelper.cutBigDecimal(StringHelper.getBigDecimal(sea.getZHMJ()), 4));
			zhjbxx.setYhzmj(StringHelper.cutBigDecimal(StringHelper.getBigDecimal(sea.getYHZMJ()), 4));
			zhjbxx.setXmxz(StringHelper.formatDefaultValue(sea.getXMXX()));
			zhjbxx.setXmmc(StringHelper.formatDefaultValue(sea.getXMMC()));
			zhjbxx.setZhtzm(StringHelper.formatObject(sea.getZHTZM()));
			zhjbxx.setBdcdyh(StringHelper.formatDefaultValue(sea.getBDCDYH()));
			zhjbxx.setZhdm(StringHelper.formatDefaultValue(sea.getZHDM()));
			zhjbxx.setBdcdyid(sea.getId());
		}
		zhjbxx.replaceEmpty();
		return zhjbxx;
	}

	/**
	 * 宗海变化情况
	 * 
	 * @作者 diaoliwei
	 * @创建时间 2015年9月22日下午6:13:22
	 * @param zhbhqk
	 * @param ql
	 * @param sea
	 * @return
	 */
	public static KTFZHBHQK getKTFZHBHQK(KTFZHBHQK zhbhqk, Rights ql, Sea sea) {
//		zhbhqk.setQXDM(sea == null ? ConfigHelper.getNameByValue("XZQHDM") : StringHelper
//				.formatObject(sea.getQXDM()));
		zhbhqk.setQXDM(getQXDM(sea));
		zhbhqk.setZHDM(sea == null ? "" : StringHelper.formatObject(sea
				.getZHDM()));
		//手动赋值
		zhbhqk.setBHYY("无");
		zhbhqk.setBHNR("无");
		zhbhqk.setDBR(ql == null ? "" : StringHelper.formatObject(ql.getDBR()));
		zhbhqk.setDJSJ(StringHelper.FormatDateOnType(ql.getDJSJ(),"yyyy-MM-dd'T'HH:mm:ss"));
		zhbhqk.replaceEmpty();
		return zhbhqk;
	}

	/**
	 * 海域（含无居民海岛）使用权
	 * 
	 * @作者 diaoliwei
	 * @创建时间 2015年9月22日下午6:16:40
	 * @param hysyq
	 * @param ql
	 * @param sea
	 * @param ywh
	 * @return
	 */
	public static QLFQLHYSYQ getQLFQLHYSYQ(QLFQLHYSYQ hysyq, Rights ql,
			Sea sea, String ywh) {
		ql.setQSZT(1);
		hysyq.setYsdm("2002020700");
		hysyq.setZhhddm(sea == null ? "" : StringHelper.formatDefaultValue(sea
				.getZHDM()));
		hysyq.setBdcdyh(sea == null ? "" : StringHelper.formatDefaultValue(sea
				.getBDCDYH()));
		hysyq.setYwh(StringHelper.formatObject(ywh));
		hysyq.setQllx(ql == null ? "" : StringHelper.formatObject(ql.getQLLX()));
		hysyq.setDjlx(ql == null ? "" : StringHelper.formatObject(ql.getDJLX()));
		hysyq.setDjyy(ql == null ? "无" : StringHelper.formatDefaultValue(ql.getDJYY()));
		hysyq.setSyqmj(sea == null ? new BigDecimal(0) : StringHelper.cutBigDecimal(StringHelper.getBigDecimal(sea.getSYQMJ()), 2));
		hysyq.setSyqqssj(StringHelper.FormatDateOnType(ql.getQLQSSJ(),"yyyy-MM-dd'T'HH:mm:ss"));
		hysyq.setSyqjssj(StringHelper.FormatDateOnType(ql.getQLJSSJ(),"yyyy-MM-dd'T'HH:mm:ss"));
		if(sea.getSYJZE()!=null) {
			hysyq.setSyjze(sea == null ? 0 : new Double(StringHelper.cut(sea.getSYJZE(), 4)));
		}
		hysyq.setSyjbzyj(sea == null ? "" : StringHelper.formatDefaultValue(sea
				.getSYJBZYJ()));
		hysyq.setSyjjnqk(sea == null ? "" : StringHelper.formatDefaultValue(sea
				.getSYJJNQK()));
		hysyq.setBdcqzh(ql == null ? "" : StringHelper.formatDefaultValue(ql
				.getBDCQZH()));
		hysyq.setQxdm(getQXDM(sea));
		hysyq.setDjjg(ql == null ? "" : StringHelper.formatDefaultValue(ql.getDJJG()));
		hysyq.setDbr(ql == null ? "" : StringHelper.formatDefaultValue(ql.getDBR()));
		hysyq.setDjsj(StringHelper.FormatDateOnType(ql.getDJSJ(),"yyyy-MM-dd'T'HH:mm:ss"));
		hysyq.setFj(StringHelper.formatDefaultValue(ql.getFJ()));
		hysyq.setQszt(StringHelper.formatObject(ql.getQSZT()));
		hysyq.setQlid(StringHelper.formatDefaultValue(ql.getId()));
		hysyq.setXmbh(StringHelper.formatDefaultValue(ql.getXMBH()));
		hysyq.setBdcdyid(StringHelper.formatObject(sea.getId()));
		hysyq.replaceEmpty();
		return hysyq;
	}

	/**
	 * 用海状况
	 * 
	 * @作者 diaoliwei
	 * @创建时间 2015年9月22日下午7:35:15
	 * @param hysyq
	 * @param sea
	 * @return
	 */
	public static KTFZHYHZK getKTFZHYHZK(KTFZHYHZK hysyq, YHZK yhzk,
			String bdcdyh) {
		//宗海代码
		if (yhzk.getZHDM()!=null &&!StringHelper.isEmpty(yhzk.getZHDM())) {
			hysyq.setZhdm(yhzk.getZHDM().toString());	
		}else {
			hysyq.setZhdm("无");
		}
		
		if (yhzk.getYHFS()!=null &&!StringHelper.isEmpty(yhzk.getYHFS())) {
			hysyq.setYhfs(yhzk.getYHFS());	
		}else {
			hysyq.setYhfs("无");
		}
		hysyq.setYhmj(yhzk == null ? new BigDecimal(1) : StringHelper.cutBigDecimal(StringHelper.getBigDecimal(yhzk.getYHMJ()), 4));
		hysyq.setJtyt(yhzk == null ? "无" : StringHelper.formatObject(yhzk.getJTYT()));
		hysyq.setSyjes(yhzk == null ? new BigDecimal(0) : StringHelper.cutBigDecimal(StringHelper.getBigDecimal(yhzk.getSYJSE()), 4));
		hysyq.setQxdm(getQXDM(bdcdyh));
		//维护具体用途字段
        if(yhzk.getJTYT()!=null&&yhzk.getJTYT().length()>0) {
        	hysyq.setJtyt(yhzk.getJTYT());
		}else {
			hysyq.setJtyt("无");
		}
        hysyq.setBdcdyid(StringHelper.formatObject(yhzk.getBDCDYID()));
        hysyq.replaceEmpty();
		return hysyq;
	}

	/**
	 * 用海、用岛坐标
	 * 
	 * @作者 diaoliwei
	 * @创建时间 2015年9月22日下午7:42:05
	 * @param hydzb
	 * @param sea
	 * @return
	 */
	public static KTFZHYHYDZB getKTFZHYHYDZB(KTFZHYHYDZB hydzb, Sea sea) {
		// 手动赋值
		hydzb.setZhhddm(sea == null ? "无" : StringHelper.formatDefaultValue(sea.getZHDM()));
		hydzb.setXh(1);
		// hydzb.setBw(StringHelper.cut(1, 8));
		// hydzb.setDj(StringHelper.cut(1, 8));
		hydzb.setQxdm(getQXDM(sea));
		hydzb.setBdcdyid(StringHelper.formatObject(sea.getId()));
		hydzb.replaceEmpty();
		return hydzb;
	}

	public static String GetYWLSHByYWH(String project_id){
		if(StringHelper.isEmpty(project_id)){
			return project_id;
		}
		BDCS_XMXX xmxx=Global.getXMXX(project_id);
		if(xmxx!=null){
			if(!StringHelper.isEmpty(xmxx.getYWLSH())){
				return xmxx.getYWLSH();
			}
		}
		return project_id;
	}
	
	/**
	 * 房屋性质字典表
	 * @param fwxz
	 * @return
	 */
	public static String getFWXZ(Object fwxz){
		String xz="";
		//房屋性质字典表
		String [] fwxzs=new String []{"0","1","2","3","4","5","6","7","8","9","99"};
		if(!StringHelper.isEmpty(fwxz)){
			if(Arrays.asList(fwxzs).contains(fwxz)){
				xz=fwxz.toString();
			}
			else{
				xz="99";
			}
		}
		else{
			xz="99";
		}
		return xz;
	}
	
	/**
	 * 房屋用途字典表
	 * @param fwxz
	 * @return
	 */
	public static String getFWYT(Object fwyt){
		String yt="";
		//房屋性质字典表
		String [] fwyts=new String []{"10","11","12","13","20","21","22","23","24","25","26","27","30","31","32","33","34","35","40","41","42","43","50","51","52","53","54","55","60","70","81","82","83","84","111","112"};
		if(!StringHelper.isEmpty(fwyt)){
			if(Arrays.asList(fwyts).contains(fwyt)){
				yt=fwyt.toString();
			}
			else{
				yt="80";
			}
		}
		else{
			yt="80";
		}
		return yt;
	}
	
	/**
	 * 房屋类型字典表
	 * @param fwxz
	 * @return
	 */
	public static String getFWLX(Object fwlx){
		String lx="";
		//房屋性质字典表
		String [] fwlxs=new String []{"0","1","2","3","4","5","6","99"};
		if(!StringHelper.isEmpty(fwlxs)){
			if(Arrays.asList(fwlxs).contains(fwlx)){
				lx=fwlx.toString();
			}
			else{
				lx="99";
			}
		}
		else{
			lx="99";
		}
		return lx;
	}
	
	/**
	 * 获得抵押物类型
	 * @作者 diaoliwei
	 * @创建时间 2015年7月25日下午10:18:25
	 * @param bdcdylx
	 * @return
	 */
	protected static String getDYBDCLXfromBDCDYLX(BDCDYLX bdcdylx) {
		String dybdclx = "";
		if (bdcdylx.equals(BDCDYLX.SHYQZD) || bdcdylx.equals(BDCDYLX.SYQZD)) {
			dybdclx = "1";
		} else if (bdcdylx.equals(BDCDYLX.H) || bdcdylx.equals(BDCDYLX.ZRZ)) {
			dybdclx = "2";
		} else if (bdcdylx.equals(BDCDYLX.LD)) {
			dybdclx = "3";
		} else if (bdcdylx.equals(BDCDYLX.GZW)|| bdcdylx.equals(BDCDYLX.YCH)||bdcdylx.equals(BDCDYLX.ZRZ)) {
			dybdclx = "4";
		} else if (bdcdylx.equals(BDCDYLX.HY)) {
			dybdclx = "5";
		} else {
			dybdclx = "7";
		}
		return dybdclx;
	}
	
	/**
	 * 宗海空间属性
	 * 
	 * @作者 diaoliwei
	 * @创建时间 2015年9月22日下午7:50:47
	 * @param zhk
	 * @param sea
	 * @return
	 */
	public static List<ZHK105> getZHK105(List<ZHK105> zhk, Sea sea) {
		String bdcdyid = "";
		if (sea != null) {
			bdcdyid = sea.getId();
		}
		zhk=getFeatureInfo2(BDCDYLX.HY,bdcdyid,sea.getBDCDYH());
		return zhk;
	}
	
	public static List<ZDK103> getZDK103H(List<ZDK103> fwk, House h,
			Building zrz) {
		if(zrz == null){
			return fwk;
		}
		fwk=getFeatureInfo(BDCDYLX.ZRZ,zrz.getId(),zrz.getBDCDYH());
		return fwk;
	}
	public static List<ZDK103> getZDK103(List<ZDK103> zdk, UseLand zd,
			OwnerLand oland, Forest sllm) {
		zdk = new ArrayList<ZDK103>();
		String bdcdyid = "";
		if (null != zd) {
			bdcdyid = zd.getId();
		}
		if (null != oland) {
			bdcdyid = oland.getId();
		}
		if (null != sllm) {
			bdcdyid = sllm.getId();
		}
		zdk=getFeatureInfo(BDCDYLX.SHYQZD,bdcdyid,zd.getBDCDYH());
		return zdk;
	}
	public static List<ZDK103> getZDK103ByNYD(List<ZDK103> zdk, AgriculturalLand zd,
			OwnerLand oland, Forest sllm) {
		zdk = new ArrayList<ZDK103>();
		String bdcdyid = "";
		if (null != zd) {
			bdcdyid = zd.getId();
		}
		if (null != oland) {
			bdcdyid = oland.getId();
		}
		if (null != sllm) {
			bdcdyid = sllm.getId();
		}
		zdk=getFeatureInfo(BDCDYLX.NYD,bdcdyid,zd.getBDCDYH());
		return zdk;
	}
	
	public static List<ZDK103> getZDK(List<ZDK103> zdk, RealUnit unit) {
		if(unit == null){
			return zdk;
		}
		zdk=getFeatureInfo(unit.getBDCDYLX(),unit.getId(),unit.getBDCDYH());
		return zdk;
	}

	public static List<ZDK103> getFeatureInfo(BDCDYLX bdcdylx,String bdcdyid,String bdcdyh){
		
		String url_iserver_data=ConfigHelper.getNameByValue("URL_ISERVER_DATA");
		List<ZDK103> zdk=new ArrayList<ZDK103>();
		String filterWhere = "BDCDYID='" + bdcdyid + "' ";
		OperateFeature operateFeature=new OperateFeature(url_iserver_data);
		if("1".equals(ConfigHelper.getNameByValue("IS_ENABLE"))){
			com.supermap.realestate.gis.common.Feature[] features = operateFeature.queryFeatures_GIS("BDCDCK", bdcdylx.GZTableName.replaceAll("BDCS_", "BDCK_"), filterWhere);
			if (features == null || features.length < 0) {
				features = operateFeature.queryFeatures_GIS("BDCK", bdcdylx.XZTableName.replaceAll("BDCS_", "BDCK_"), filterWhere);
			}
			if (features != null && features.length > 0) {
				com.supermap.realestate.gis.common.Feature feature = features[0];
				com.supermap.realestate.gis.common.Geometry geometry = feature.geometry;
				int[] parts = geometry.parts;
				int zdx = 1;
				for (int i = 0; i < geometry.points.length; i++) {
					ZDK103 zdk103 = new ZDK103();
					zdk103.setBDCDYH(bdcdyh);
					int length_zdx=0;
					int length_zdx_1=0;
					for(int ipart=0;ipart<zdx;ipart++){
						length_zdx+=parts[ipart];
						if(ipart!=zdx-1){
							length_zdx_1+=parts[ipart];
						}
					}
					if(i>=length_zdx){
						zdx++;
						length_zdx_1=length_zdx;
						
					}
					if(i==length_zdx-1){
						continue;
					}
					
					zdk103.setZDX(zdx);
					zdk103.setXZB(StringHelper.cut(geometry.points[i].x, 4).toString());
					zdk103.setYZB(StringHelper.cut(geometry.points[i].y, 4).toString());
					zdk103.setXH(i-length_zdx_1+1);
					zdk103.replaceEmpty();
					zdk.add(zdk103);
				}
			}
		}else if ("0".equals(ConfigHelper.getNameByValue("IS_ENABLE"))) {
			List<Feature> list_feature=operateFeature.queryFeatures_iServer("BDCDCK", bdcdylx.GZTableName.replaceAll("BDCS_", "BDCK_"), filterWhere);
			if(list_feature==null||list_feature.size()<=0){
				list_feature=operateFeature.queryFeatures_iServer("BDCK", bdcdylx.XZTableName.replaceAll("BDCS_", "BDCK_"), filterWhere);
			}
			if(list_feature==null||list_feature.size()<=0){
				list_feature=operateFeature.queryFeatures_iServer("BDCK", bdcdylx.LSTableName.replaceAll("BDCS_", "BDCK_"), filterWhere);
			}
			if(list_feature!=null&&list_feature.size()>0){
				Feature feature=list_feature.get(0);
				Geometry ObjGeometry=feature.geometry;
				int[] parts=ObjGeometry.parts;
				int zdx=1;
				for(int ni=0;ni<ObjGeometry.points.length;ni++)
				{
					ZDK103 zdk103 = new ZDK103();
					zdk103.setBDCDYH(bdcdyh);
					int length_zdx=0;
					int length_zdx_1=0;
					for(int ipart=0;ipart<zdx;ipart++){
						length_zdx+=parts[ipart];
						if(ipart!=zdx-1){
							length_zdx_1+=parts[ipart];
						}
					}
					if(ni>=length_zdx){
						zdx++;
						length_zdx_1=length_zdx;
						
					}
					if(ni==length_zdx-1){
						continue;
					}
					
					zdk103.setZDX(zdx);
					zdk103.setXZB(StringHelper.cut(ObjGeometry.points[ni].x, 4).toString());
					zdk103.setYZB(StringHelper.cut(ObjGeometry.points[ni].y, 4).toString());
					zdk103.setXH(ni-length_zdx_1+1);
					zdk103.replaceEmpty();
					zdk.add(zdk103);
				}
			}
		}
		return zdk;
	}
	
	public static List<ZHK105> getFeatureInfo2(BDCDYLX bdcdylx,String bdcdyid,String bdcdyh){
		String url_iserver_data=ConfigHelper.getNameByValue("URL_ISERVER_DATA");
		List<ZHK105> zdk=new ArrayList<ZHK105>();
		String filterWhere = "BDCDYID='" + bdcdyid + "' ";
		OperateFeature operateFeature=new OperateFeature(url_iserver_data);
		if("1".equals(ConfigHelper.getNameByValue("IS_ENABLE"))){
			com.supermap.realestate.gis.common.Feature[] features = operateFeature.queryFeatures_GIS("BDCDCK", bdcdylx.GZTableName.replaceAll("BDCS_", "BDCK_"), filterWhere);
			if (features == null || features.length < 0) {
				features = operateFeature.queryFeatures_GIS("BDCK", bdcdylx.XZTableName.replaceAll("BDCS_", "BDCK_"), filterWhere);
			}
			if (features != null && features.length > 0) {
				com.supermap.realestate.gis.common.Feature feature = features[0];
				com.supermap.realestate.gis.common.Geometry geometry = feature.geometry;
				int parts[] = geometry.parts;
				int zdx = 1;
				for(int ni=0;ni<geometry.points.length;ni++){
					ZHK105 zdk103 = new ZHK105();
					zdk103.setBDCDYH(bdcdyh);
					int length_zdx=0;
					int length_zdx_1=0;
					for(int ipart=0;ipart<zdx;ipart++){
						length_zdx+=parts[ipart];
						if(ipart!=zdx-1){
							length_zdx_1+=parts[ipart];
						}
					}
					if(ni>length_zdx){
						zdx++;
						length_zdx_1=length_zdx;
					}
					zdk103.setZDX(zdx);
					zdk103.setXZB(StringHelper.cut(geometry.points[ni].x, 4).toString());
					zdk103.setYZB(StringHelper.cut(geometry.points[ni].y, 4).toString());
					zdk103.setXH(ni-length_zdx_1);
					zdk103.replaceEmpty();
					zdk.add(zdk103);
				}
			}
		}else if ("0".equals(ConfigHelper.getNameByValue("IS_ENABLE"))) {
			List<Feature> list_feature=operateFeature.queryFeatures_iServer("BDCDCK", bdcdylx.GZTableName.replaceAll("BDCS_", "BDCK_"), filterWhere);
			if(list_feature==null||list_feature.size()<=0){
				list_feature=operateFeature.queryFeatures_iServer("BDCK", bdcdylx.XZTableName.replaceAll("BDCS_", "BDCK_"), filterWhere);
			}
			if(list_feature!=null&&list_feature.size()>0){
				Feature feature=list_feature.get(0);
				Geometry ObjGeometry=feature.geometry;
				int[] parts=ObjGeometry.parts;
				int zdx=1;
				for(int ni=0;ni<ObjGeometry.points.length;ni++)
				{
					ZHK105 zdk103 = new ZHK105();
					zdk103.setBDCDYH(bdcdyh);
					int length_zdx=0;
					int length_zdx_1=0;
					for(int ipart=0;ipart<zdx;ipart++){
						length_zdx+=parts[ipart];
						if(ipart!=zdx-1){
							length_zdx_1+=parts[ipart];
						}
					}
					if(ni>length_zdx){
						zdx++;
						length_zdx_1=length_zdx;
					}
					zdk103.setZDX(zdx);
					zdk103.setXZB(StringHelper.cut(ObjGeometry.points[ni].x, 4).toString());
					zdk103.setYZB(StringHelper.cut(ObjGeometry.points[ni].y, 4).toString());
					zdk103.setXH(ni-length_zdx_1);
					zdk103.replaceEmpty();
					zdk.add(zdk103);
				}
			}
		}
		
		return zdk;
	}
/**
 * @param syq 农用地使权表(上报)
 * @param nyd   农用地表
 * @param ql    权力信息表
 * @param ywh   业务号
 * @return   农用地使权表(上报)
 */
	public static QLNYDSYQ getQLNYDSYQ(QLNYDSYQ syq, AgriculturalLand nyd, BDCS_QL_GZ ql, String ywh) {
//		1.数据获取
//		要素代码
		if(!StringHelper.isEmpty(nyd.getYSDM())) {
			syq.setYsdm(nyd.getYSDM());
		}
//		不动产单元号
		syq.setBdcdyh(StringHelper.formatDefaultValue(nyd.getZDDM()));
//		业务号
		syq.setYwh(StringHelper.formatDefaultValue(ywh));
//		权利类型 
		if (!StringUtils.isEmpty(ql.getQLLX())) {
			// 国家标准和项目上使用的23和24这两个权利类型是位置对调的
			if(ql.getQLLX().equals("23")){
				syq.setQllx("24");
			}else if(ql.getQLLX().equals("24")){
				syq.setQllx("23");
			}else if(ql.getQLLX().equals("37")){
				//国家标准里面没有权利类型为37的字典值
			}else{
				syq.setQllx(ql.getQLLX());
			}
		}
//		syq.setQllx(StringHelper.formatDefaultValue(ql.getQLLX()));
//		登记类型
		syq.setDjlx(StringHelper.formatDefaultValue(ql.getDJLX()));
//		登记原因
		syq.setDjyy(StringHelper.formatDefaultValue(ql.getDJYY()));
//		坐落
		syq.setZl(StringHelper.formatDefaultValue(nyd.getZL()));
//		发包方代码
		syq.setFbfdm(StringHelper.formatDefaultValue(nyd.getFBFDM()));
//		发包方名称
		syq.setFbfmc(StringHelper.formatDefaultValue(nyd.getFBFMC()));
//		承包(使用权)面积
		syq.setCbmj(nyd.getCBMJ());
//		承包(使用)起始时间
		if(!StringHelper.isEmpty(nyd.getCBQSSJ())) {
			syq.setCbqssj(StringHelper.FormatDateOnType(nyd.getCBQSSJ(),"yyyy-MM-dd'T'HH:mm:ss"));
		}else {
			syq.setCbqssj(StringHelper.FormatDateOnType(ql.getDJSJ(),"yyyy-MM-dd'T'HH:mm:ss"));
		}
//		承包(使用)结束时间
		if(!StringHelper.isEmpty(nyd.getCBJSSJ())) {
			syq.setCbjssj(StringHelper.FormatDateOnType(nyd.getCBJSSJ(),"yyyy-MM-dd'T'HH:mm:ss"));
		} else {
			syq.setCbjssj(StringHelper.FormatDateOnType(ql.getDJSJ(),"yyyy-MM-dd'T'HH:mm:ss"));
		}
//		土地所有权性质
		syq.setTdsyqxz(StringHelper.formatDefaultValue(nyd.getTDSYQXZ()));
//		水域滩涂类型
		syq.setSyttlx(nyd.getSYTTLX());
//		养殖业方式
		syq.setYzyfs(nyd.getYZYFS());
//		草原质量
		syq.setCyzl(StringHelper.formatDefaultValue(nyd.getCYZL()));
//		适宜载畜量
		syq.setSyzcl(StringHelper.formatDefaultValue(nyd.getSYZCL()));
//		不动产权证号
		syq.setBdcqzh(StringHelper.formatDefaultValue(ql.getBDCQZH()));
//		区县代码
		syq.setQxdm(getQXDM(nyd));
//		登记机构
		syq.setDjjg(ConfigHelper.getNameByValue("XZQHDM"));
//		登簿人
		syq.setDbr(StringHelper.formatDefaultValue(ql.getDBR()));
//		登记时间
		syq.setDjsj(StringHelper.FormatDateOnType(ql.getDJSJ(),"yyyy-MM-dd'T'HH:mm:ss"));
//		附记
		syq.setFj(StringHelper.formatDefaultValue(ql.getFJ()));
//		权属状态
		if(!StringHelper.isEmpty(ql.getQSZT())) {
			syq.setQszt(ql.getQSZT().toString());
		}
		//2.数据维护
		//权属状态.设置默认值
		if(StringHelper.isEmpty(ql.getQSZT())) {
			syq.setQszt("1");
		}
		//要素代码设置默认值
		nyd.getYSDM();
		syq.setYsdm("6002020400");
		syq.setQlid(StringHelper.formatDefaultValue(ql.getId()));
		syq.setXmbh(StringHelper.formatDefaultValue(ql.getXMBH()));
		return syq;
	}

	/**
	 * 受理申请(农用地)
	 * @param sq
	 * @param ywh
	 * @param nyd
	 * @param ql
	 * @param xmxx
	 * @param h
	 * @param slsj
	 * @param oland
	 * @param sllm
	 * @param zh
	 * @return  DJTDJSLSQ  
	 */
public static DJTDJSLSQ getDJTDJSLSQByNYD(DJTDJSLSQ sq, AgriculturalLand nyd, BDCS_QL_GZ ql, BDCS_XMXX xmxx) {
//	要素代码,数控 库中没有此字段数据手动加入
	sq.setYSDM("6002020400");
//	业务号
	sq.setYWH(StringHelper.formatObject(xmxx.getYWLSH()));
//	登记大类
	sq.setDJDL(StringHelper.formatObject(xmxx.getDJLX()));
//	区县代码
//	sq.setQXDM(StringHelper.formatObject(nyd.getQXDM()));
	sq.setQXDM(getQXDM(nyd));
//	申请证书版式
	sq.setSQZSBS(ConstValue.ZSBS.DYB.Value.equals(ql.getZSBS()) ? 0 : 1);
//	申请分别持证
	sq.setSQFBCZ(ConstValue.CZFS.FBCZ.Value.equals(ql.getCZFS()) ? 1 : 0);
//	受理人员
	sq.setSLRY(StringHelper.formatObject(xmxx.getSLRY()));
//	受理时间
	sq.setSLSJ(StringHelper.FormatDateOnType(xmxx.getSLSJ(),"yyyy-MM-dd'T'HH:mm:ss"));
//	坐落
	sq.setZL(StringHelper.formatObject(nyd.getZL()));
	
	//以下内容,由于表BDCS_SQSL为空,不做处理
//登记小类
//	通知人姓名
//	通知方式
//	通知人电话
//	通知人移动电话
//	通知人电子邮件
//	是否问题案件
//	结束时间
//	案件状态
//	备注
	sq.removeEmpty();
	return sq;
}
/**
 * 
 * @param unit
 * @return 行政区区县代码
 */
public static String  getQXDM(RealUnit unit) {
	try {
		if(unit!=null&&!StringHelper.isEmpty(unit.getBDCDYH())&&unit.getBDCDYH().length()>=6) {
			return unit.getBDCDYH().substring(0, 6);
		}
	} catch (Exception e) {
		e.printStackTrace();
	}
	return "";
}

public static String  getQXDM(String bdcdyh) {
	try {
		if(bdcdyh!=null&&!StringHelper.isEmpty(bdcdyh)&&bdcdyh.length()>=6) {
			return bdcdyh.substring(0, 6);
		}
	} catch (Exception e) {
		e.printStackTrace();
	}
	return "";
}

}
