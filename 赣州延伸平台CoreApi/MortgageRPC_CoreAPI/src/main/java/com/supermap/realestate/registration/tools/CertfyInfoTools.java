/**   
 * 请描述这个文件
 * @Title: EntityTool.java 
 * @Package com.supermap.realestate.registration.util 
 * @author liushufeng 
 * @date 2015年7月12日 下午5:20:33 
 * @version V1.0   
 */

package com.supermap.realestate.registration.tools;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.support.DaoSupport;
import org.springframework.util.StringUtils;

import com.supermap.realestate.registration.ViewClass.ProjectInfo;
import com.supermap.realestate.registration.factorys.HandlerFactory;
import com.supermap.realestate.registration.mapping.HandlerMapping.RegisterWorkFlow;
import com.supermap.realestate.registration.model.BDCS_DJDY_GZ;
import com.supermap.realestate.registration.model.BDCS_FSQL_GZ;
import com.supermap.realestate.registration.model.BDCS_PARTIALLIMIT;
import com.supermap.realestate.registration.model.BDCS_QL_GZ;
import com.supermap.realestate.registration.model.BDCS_QL_XZ;
import com.supermap.realestate.registration.model.BDCS_SQR;
import com.supermap.realestate.registration.model.BDCS_XMXX;
import com.supermap.realestate.registration.model.BDCS_XTDZ;
import com.supermap.realestate.registration.model.BDCS_ZS_GZ;
import com.supermap.realestate.registration.model.interfaces.House;
import com.supermap.realestate.registration.model.interfaces.RealUnit;
import com.supermap.realestate.registration.model.interfaces.Rights;
import com.supermap.realestate.registration.model.interfaces.RightsHolder;
import com.supermap.realestate.registration.model.interfaces.SubRights;
import com.supermap.realestate.registration.util.ConfigHelper;
import com.supermap.realestate.registration.util.ConstHelper;
import com.supermap.realestate.registration.util.ConstValue;
import com.supermap.realestate.registration.util.ConstValue.BDCDYLX;
import com.supermap.realestate.registration.util.ConstValue.CZFS;
import com.supermap.realestate.registration.util.ConstValue.DJDYLY;
import com.supermap.realestate.registration.util.ConstValue.DJLX;
import com.supermap.realestate.registration.util.ConstValue.QLLX;
import com.supermap.realestate.registration.util.ConstValue.SQRLB;
import com.supermap.realestate.registration.util.ConstValue.YGDJLX;
import com.supermap.realestate.registration.util.Global;
import com.supermap.realestate.registration.util.ProjectHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.wisdombusiness.core.SuperSpringContext;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;

/**
 * 
 * 证明信息获取操作类，不允许添加重载方法，保证证书方式获取信息
 * 
 * @ClassName: CertfyInfoTools
 * @author 俞学斌
 * @date 2015年12月24日 00:25:33
 */
public class CertfyInfoTools {
	private String blank = "----";// 空白符
	private String hhf = "<br/>";// 换行符
	@SuppressWarnings("unused")
	private String space = "&nbsp;&nbsp;&nbsp;";// 空格

	private CommonDao baseCommonDao;

	public CertfyInfoTools() {
		this.baseCommonDao = SuperSpringContext.getContext().getBean(CommonDao.class);
	}

	/**
	 * 获取不动产登记证明
	 */
	@SuppressWarnings("rawtypes")
	public Map CreateZM(BDCS_XMXX xmxx, String zsid) {
		Map<String, String> map = new HashMap<String, String>();

		/************************************ 获取证明显示需要信息来源 ************************************/
		// 获取证书信息
		BDCS_ZS_GZ zs = baseCommonDao.get(BDCS_ZS_GZ.class, zsid);
		// 流程信息
		ProjectInfo info = ProjectHelper.GetPrjInfoByXMBH(xmxx.getId());
		// 获取权利人信息集合
		List<RightsHolder> qlrlist=null;
		String Baseworkflow_ID =HandlerFactory.getWorkflow(xmxx.getPROJECT_ID()).getName();
		if (Baseworkflow_ID.indexOf("YY")==0||Baseworkflow_ID=="GZ_YYDJHandler") {//异议登记
			List<Map> qlid = baseCommonDao.getDataListByFullSql("SELECT distinct(QLID) FROM BDCK.BDCS_QLR_GZ where xmbh='"+xmxx.getId()+"'");
			String _qlid =null; 
			for (Map<String, String> m : qlid){
				for (String k : m.keySet()){  
					_qlid = m.get(k);  
			      }  
			 }
			qlrlist = RightsHolderTools.loadRightsHolders(DJDYLY.GZ, _qlid);
		}else {
			qlrlist = RightsHolderTools.loadRightsHoldersByZSID(DJDYLY.GZ, xmxx.getId(), zsid);
		}
		
		// 获取权利信息
		Rights ql = RightsTools.loadRightsByZSID(DJDYLY.GZ, zsid, xmxx.getId());
		// 获取附属权利
		SubRights fsql = null;
		if (ql != null) {
			fsql = RightsTools.loadSubRightsByRightsID(DJDYLY.GZ, ql.getId());
		}
		// 获取单元信息
		BDCS_DJDY_GZ djdy = this.getDjdyGzByZs(xmxx.getId(), zsid);// 登记单元
		BDCDYLX lx = null;
		DJDYLY ly = null;
		String bdcdyid = "";
		if (djdy != null) {
			lx = BDCDYLX.initFrom(djdy.getBDCDYLX());// 不动产单元 类型
			ly = DJDYLY.initFrom(djdy.getLY());// 不动产单元来源
			bdcdyid = djdy.getBDCDYID();// 不动产单元ID
		}
		RealUnit unit = UnitTools.loadUnit(lx, ly, bdcdyid);
		// 主体权利权证号
		String bdcqzh_main = getXZBDCQZH(ql.getDJDYID(), ql.getId(), info);
		BDCS_QL_GZ ql_ex=(BDCS_QL_GZ)ql;
		if(ql_ex!=null){
			if("1".equals(ql_ex.getISPARTIAL())){
				List<String> list_bdcqzh=new ArrayList<String>();
				List<BDCS_PARTIALLIMIT> list_limitqlr=baseCommonDao.getDataList(BDCS_PARTIALLIMIT.class, "LIMITQLID='"+ql_ex.getId()+"'");
				if(list_limitqlr!=null&&list_limitqlr.size()>0){
					for(BDCS_PARTIALLIMIT partiallimit:list_limitqlr){
						RightsHolder limit_qlr=RightsHolderTools.loadRightsHolder(DJDYLY.LS, partiallimit.getQLRID());
						if(limit_qlr!=null&&!list_bdcqzh.contains(limit_qlr.getBDCQZH())){
							list_bdcqzh.add(limit_qlr.getBDCQZH());
						}
					}
				}
				if(list_bdcqzh.size()>0){
					bdcqzh_main=StringHelper.formatList(list_bdcqzh, "、");
				}
			}
		}
		String fj_plqz = "";
		// 以下40行为最高额抵押的时候，把其他的证号也写在附记里
		// 只有权利类型为抵押权，登记类型为初始登记、变更登记、转移登记、预告登记的时候才加
		if (QLLX.DIYQ.Value.equals(ql.getQLLX())
				&& (info.getDjlx().equals(DJLX.CSDJ.Value) || info.getDjlx().equals(DJLX.YGDJ.Value) || info.getDjlx().equals(DJLX.ZYDJ.Value) || info.getDjlx().equals(
						DJLX.BGDJ.Value))) {
			List<BDCS_QL_GZ> lst = baseCommonDao.getDataList(BDCS_QL_GZ.class, "XMBH='" + xmxx.getId() + "' and qllx='" + QLLX.DIYQ.Value
					+ "' and BDCQZH IS NOT NULL order by BDCQZH");
			if (lst != null && lst.size() > 1) {
				String plqz = ConfigHelper.getNameByValue("ZSPARAM_PLZH_QZ");
				String plhz = ConfigHelper.getNameByValue("ZSPARAM_PLZH_HZ");
				String plljf = ConfigHelper.getNameByValue("ZSPARAM_PLZH_LJF");
				List<String> lll = new ArrayList<String>();
				for (BDCS_QL_GZ tzs : lst) {
					String bdczsh = tzs.getBDCQZH();
					if (!StringHelper.isEmpty(bdczsh)) {
						List<String> zhlist = StringHelper.MatchBDCZMH(bdczsh);
						if (zhlist != null && zhlist.size() == 4) {
							String zmxh = zhlist.get(3);
							if (!StringHelper.isEmpty(zmxh) && !lll.contains(zmxh)) {
								lll.add(zmxh);
							}

						}
					}
				}
				fj_plqz = plqz;
				String start = lll.get(0);
				String current = start;
				int kk = 0;
				for (kk = kk + 1; kk < lll.size(); kk++) {
					String zh = lll.get(kk);
					if ((Integer.parseInt(zh)) == (Integer.parseInt(current) + 1)) {
						current = zh;
					} else {
						fj_plqz += (start + plljf + current) + plhz + ",";
						start = current = zh;
					}
				}
				fj_plqz += (start + plljf + current) + plhz;
			}
		}
		/************************************ 获取证明显示需要信息来源 ************************************/

		/*************************************** 生成证书显示信息 ***************************************/
		// FM_1_1,封面年
		String fm_djsj_year = GetDJSJ(ql, "year");
		// FM_1_2,封面月
		String fm_djsj_month = GetDJSJ(ql, "month");
		// FM_1_3,封面日
		String fm_djsj_day = GetDJSJ(ql, "day");
		// FM_2,证书编号
		String fm_zsbh = GetZSBH(zs);

		// NR_1_1,省份
		String nr_bdcqzh_qhjc = GetBDCQZH(zs, "qhjc");
		// NR_1_2,地区名称
		String nr_bdcqzh_qhmc = GetBDCQZH(zs, "qhmc");
		// NR_1_3,年度
		String nr_bdcqzh_nd = GetBDCQZH(zs, "nd");
		// NR_1_4,产权证号
		String nr_bdcqzh_xh = GetBDCQZH(zs, "xh");

		// NR_2、证明权利或事项
		String nr_zmqlhsx = GetZMQLSX(xmxx,ql);
		// NR_3,权利人
		String nr_qlrmc = GetQLRMC(qlrlist);
		// NR_4、义务人
		String nr_ywr = "";
		if("450300".equals(ConfigHelper.getNameByValue("XZQHDM"))//桂林需求，更正登记证书信息的义务人读取申请人的义务人
				&& (DJLX.YGDJ.Value.equals(ql.getDJLX())||DJLX.GZDJ.Value.equals(ql.getDJLX()))){ 
			 nr_ywr = GetYWR_2(xmxx,ql,fsql);
		}else if(QLLX.DYQ.Value.equals(ql.getQLLX())){
			 nr_ywr = GetYWR_2(xmxx,ql,fsql);
		}else if(QLLX.DIYQ.Value.equals(ql.getQLLX())){
			 nr_ywr = GetYWR_2(xmxx,ql,fsql);
		}else{
			 nr_ywr = GetYWR(ql, fsql);
		}
		
		// NR_5,坐落
		String nr_zl = GetZL(unit);
		// NR_5,不动产单元号
		String nr_bdcdyh = GetBDCDYH(unit);
		// NR_6,其他权利状况
		
		String nr_qt ="";
		if(QLLX.DYQ.Value.equals(ql.getQLLX())){
			nr_qt=getQT_DYQ(ql, xmxx);
		}else{
			nr_qt=GetQT(ql, fsql, xmxx, bdcqzh_main,lx,info,unit);
		}
		
		// NR_7,附记
		String nr_fj = GetFJ(xmxx, ql, fj_plqz,qlrlist,ql, unit);

		// NR_8,不动产权证号
		String nr_bdcqzh = zs.getBDCQZH();
		/*************************************** 生成证书显示信息 ***************************************/

		/*************************************** 赋值证书显示信息 ***************************************/
		map.put("year", fm_djsj_year);
		map.put("month", fm_djsj_month);
		map.put("day", fm_djsj_day);
		map.put("zsbh", fm_zsbh);

		map.put("sjc", nr_bdcqzh_qhjc);
		map.put("nd", nr_bdcqzh_nd);
		map.put("qhmc", nr_bdcqzh_qhmc);
		map.put("sxh", nr_bdcqzh_xh);

		map.put("zmqlhsx", nr_zmqlhsx);
		map.put("qlr", nr_qlrmc);
		map.put("ywr", nr_ywr);
		map.put("zl", nr_zl);
		map.put("bdcdyh", nr_bdcdyh);
		map.put("qt", nr_qt);
		map.put("fj", nr_fj);

		map.put("bdcqzh", nr_bdcqzh);
		
		//添加原不动产权证明号
		//添加原不动产权证号
		if(!StringHelper.isEmpty(ql.getLYQLID())){
			Rights ql_ls=RightsTools.loadRights(DJDYLY.LS, ql.getLYQLID());
			if(ql_ls != null){	
				List<RightsHolder> list_qlr_ls=RightsHolderTools.loadRightsHolders(DJDYLY.LS, ql.getLYQLID());
				if(list_qlr_ls != null && list_qlr_ls.size() > 0){
					String ybdcqzmh = "";
					List<String> ybdcqzmhs = new ArrayList<String>();
					for(RightsHolder qlr_ls:list_qlr_ls){
						if(!StringHelper.isEmpty(qlr_ls.getBDCQZH()) && !ybdcqzmhs.contains(qlr_ls.getBDCQZH())){
							ybdcqzmh= StringHelper.isEmpty(ybdcqzmh) ? qlr_ls.getBDCQZH() : ybdcqzmh + "," + qlr_ls.getBDCQZH();
							ybdcqzmhs.add(qlr_ls.getBDCQZH());
						}
					}
					map.put("ybdcqzmhs", ybdcqzmh);
				}
			}
		}
		/*************************************** 赋值证书显示信息 ***************************************/
		return map;
	}

	/**
	 * 根据项目编号和证书id获取BDCS_DJDY_GZ
	 * 
	 * @作者 diaoliwei
	 * @创建时间 2015年7月16日下午8:22:38
	 * @param xmbh
	 * @param zsid
	 * @return
	 */
	private BDCS_DJDY_GZ getDjdyGzByZs(String xmbh, String zsid) {
		StringBuilder hqlBuilder = new StringBuilder();
		hqlBuilder.append(" XMBH ='").append(xmbh).append("' ");
		hqlBuilder.append(" AND DJDYID IN (select distinct DJDYID from BDCS_QDZR_GZ ");
		hqlBuilder.append(" WHERE ZSID = '").append(zsid).append("' ");
		hqlBuilder.append(" AND XMBH = '").append(xmbh).append("' ) ");
		List<BDCS_DJDY_GZ> list = baseCommonDao.getDataList(BDCS_DJDY_GZ.class, hqlBuilder.toString());
		if (null != list && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	/**
	 * 获取原有的不动产单元号
	 * 
	 * @作者 海豹
	 * @创建时间 2015年8月6日下午5:33:42
	 * @param djdyid
	 * @param qlid
	 * @return
	 */
	private String getXZBDCQZH(String djdyid, String qlid, ProjectInfo info) {
		String ybdcqzh = "";// 拼接后不动产证书号
		String bdcqzsh = "";// 查询出的不动产证书号
		QLLX qllx = null;
		String condition_gz = " DJDYID ='"+djdyid+"' AND QLLX IN ('1','2','3','4','5','6','7','8','9','10','11','12','13','14','15','16','17','18','24') AND XMBH='"+info.getXmbh()+"'";
		List<Rights> rights_syq = RightsTools.loadRightsByCondition(DJDYLY.GZ, condition_gz);
		if(rights_syq == null|| rights_syq.size() <=0){
			String condition = " DJDYID ='"+djdyid+"' AND QLLX IN ('1','2','3','4','5','6','7','8','9','10','11','12','13','14','15','16','17','18','24','99') ";
			rights_syq = RightsTools.loadRightsByCondition(DJDYLY.XZ, condition);
		}
		if (rights_syq != null && rights_syq.size() > 0) {
			Rights bdcs_ql_xz =null;
			//基准流程
			String jzlc = "";
			if(info!=null){	
				jzlc = info.getBaseworkflowcode();
			}
			for (Rights right : rights_syq) {
				if ("700".equals(right.getDJLX())||("500".equals(right.getDJLX())&&"GZ202".equals(jzlc))) {//针对GZ202流程，调用上一手预告登记（预告、转移预告或者更正转移预告）得到的不动产证明号
					bdcs_ql_xz = right;
					break;
				}else{
					bdcs_ql_xz =rights_syq.get(0);
				}
			}
			 
			bdcqzsh = bdcs_ql_xz.getBDCQZH();
			qllx = QLLX.initFrom(bdcs_ql_xz.getQLLX());
		}
		// 构建已有不动产权证书号
		if (!StringHelper.isEmpty(bdcqzsh)) {
			if (ConfigHelper.getNameByValue("ZSPARAM_YBDCZSH_CONTAIN") != null) {
				if (bdcqzsh.contains(ConfigHelper.getNameByValue("ZSPARAM_YBDCZSH_CONTAIN")) || QLLX.JTTDSYQ.equals(qllx) || QLLX.JTJSYDSYQ.equals(qllx)
						|| QLLX.GYJSYDSHYQ.equals(qllx) || QLLX.ZJDSYQ.equals(qllx)) {
					ybdcqzh = bdcqzsh;
				} else {
					if (info.getDjlx().equals(DJLX.YGDJ.Value) && info.getQllx().equals(QLLX.DIYQ.Value)) {
						ybdcqzh = ConfigHelper.getNameByValue("ZSPARAM_PREYBDCSZSH_PREFIX") + bdcqzsh + ConfigHelper.getNameByValue("ZSPARAM_PREYBDCSZSH_SUFFIX");
					} else {
						ybdcqzh = ConfigHelper.getNameByValue("ZSPARAM_YBDCZSH_PREFIX") + bdcqzsh + ConfigHelper.getNameByValue("ZSPARAM_YBDCZSH_SUFFIX");// 原不动产权证书号
						// BDCS_QL_XZ
					}
				}
			}
		}
		return ybdcqzh;
	}

	/*
	 * 获取登记时间
	 */
	public String GetDJSJ(Rights ql, String type) {
		String fm_djsj_year = "";
		String fm_djsj_month = "";
		String fm_djsj_day = "";
		if (!StringHelper.isEmpty(ql)) {
			Date djsj = ql.getDJSJ();
			if (djsj != null) {
				Calendar cal = Calendar.getInstance();
				cal.setTime(djsj);
				fm_djsj_year = cal.get(Calendar.YEAR) + "";
				fm_djsj_month = (cal.get(Calendar.MONTH) + 1) + "";
				fm_djsj_day = cal.get(Calendar.DATE) + "";
			}
		}
		if (("year").equals(type)) {
			return fm_djsj_year;
		} else if (("month").equals(type)) {
			return fm_djsj_month;
		} else if (("day").equals(type)) {
			return fm_djsj_day;
		}
		return "";
	}

	/*
	 * 获取证书编号
	 */
	public String GetZSBH(BDCS_ZS_GZ zs) {
		if (zs != null) {
			return StringHelper.formatObject(zs.getZSBH());
		}
		return "";
	}

	/*
	 * 获取不动产权证号
	 */
	public String GetBDCQZH(BDCS_ZS_GZ zs, String type) {
		String nr_bdcqzh_qhjc = "";
		String nr_bdcqzh_nd = "";
		String nr_bdcqzh_qhmc = "";
		String nr_bdcqzh_xh = "";
		if (zs != null) {
			String bdcqzh = zs.getBDCQZH();
			if (!StringUtils.isEmpty(bdcqzh)) {
				List<String> listqzh = StringHelper.MatchBDCZMH(bdcqzh);
				if (listqzh.size() == 4)// 受理页面想查看证书信息时，出错
				{
					nr_bdcqzh_qhjc = listqzh.get(0);
					nr_bdcqzh_nd = listqzh.get(1);
					nr_bdcqzh_qhmc = listqzh.get(2);
					nr_bdcqzh_xh = listqzh.get(3);
				}
			}
		}
		if (("qhjc").equals(type)) {
			return nr_bdcqzh_qhjc;
		} else if (("nd").equals(type)) {
			return nr_bdcqzh_nd;
		} else if (("qhmc").equals(type)) {
			return nr_bdcqzh_qhmc;
		} else if (("xh").equals(type)) {
			return nr_bdcqzh_xh;
		}
		return "";
	}

	/*
	 * 获取权利人名称
	 */
	public String GetZMQLSX(BDCS_XMXX xmxx,Rights ql) {
		String nr_zmqlhsx = "";
		RegisterWorkFlow flow = HandlerFactory.getWorkflow(xmxx.getPROJECT_ID());
		String qxdm_ = ConfigHelper.getNameByValue("XZQHDM");
		if (DJLX.YGDJ.Value.equals(xmxx.getDJLX()))// 预告登记
		{
			nr_zmqlhsx = "预告登记";
		} else if (DJLX.YYDJ.Value.equals(xmxx.getDJLX())||flow.getName().equals("BZ005")||flow.getName().equals("BZ006")) {
			nr_zmqlhsx = "异议登记";
		} else if (QLLX.DIYQ.Value.equals(xmxx.getQLLX())||(!StringHelper.isEmpty(ql)&&QLLX.DIYQ.Value.equals(ql.getQLLX()))) {
			nr_zmqlhsx = "抵押权";
		}else if (flow!=null&&!StringHelper.isEmpty(flow.getHandlername())&&"BG_YGandDYQBG_DJHandler".equals(flow.getHandlername())) {
			//BG043 & GZ017 
			nr_zmqlhsx = "预告登记";
		}
		
		if(flow!=null&&!StringHelper.isEmpty(flow.getHandlername())&&!"XFYDYZXFDYDJHandler".equals(flow.getHandlername())
				&&!"YDYZXDYDJHandler".equals(flow.getHandlername())&&!"ZY_YDYTODY_DJHandler".equals(flow.getHandlername())
				&&!"YDYZXDYDJHandler_LuZhou".equals(flow.getHandlername())&&!"ZY_YDYTODY_DJHandler_LuZhou".equals(flow.getHandlername())&&!"ZY_YGDYTODY_DJHandler".equals(flow.getHandlername())){
			// 如果之前是预告登记并且当前流程是除了注销登记以外的其他登记，还沿用原来的预告登记。
			List<Rights> list = RightsTools.loadRightsByCondition(DJDYLY.GZ, "XMBH='" + xmxx.getId() + "'");
			if (list != null && list.size() > 0) {
				Rights rights = list.get(0);
				String lyqlid = rights.getLYQLID();
				if (!StringHelper.isEmpty(lyqlid)) {
					Rights rightsxz = RightsTools.loadRights(DJDYLY.LS, lyqlid);
					if (rightsxz != null) {
						if (DJLX.YGDJ.Value.equals(rightsxz.getDJLX())) {
							nr_zmqlhsx = "预告登记";
						}
					}
				}
			}
		}
		if (flow!=null&&!StringHelper.isEmpty(flow.getHandlername())&&"ZY_YDYTODY_DJHandler_GuiLin".equals(flow.getHandlername())||"YDYZXDYDJHandler2".equals(flow.getHandlername())||"CS215".equals(flow.getName())) {
			nr_zmqlhsx = "抵押权";
		}
		ProjectInfo Pro = ProjectHelper.GetPrjInfoByXMBH(xmxx.getId());
		if(Pro!=null){
			//广西的预告抵押的证明权利或事项用的是“预告登记”，所以加了行政区代码判断			
			if(!StringHelper.isEmpty(qxdm_) && !qxdm_.substring(0,3).equals("450")){
				if("YG011".equals(Pro.getBaseworkflowcode())){
					if(!StringHelper.isEmpty(ql)&&QLLX.DIYQ.Value.equals(ql.getQLLX())){
						nr_zmqlhsx = "抵押权";
					}
				}
			}
		}
		if(QLLX.DYQ.Value.equals(ql.getQLLX())){
			nr_zmqlhsx="地役权";
		}
		if (flow!=null&&!StringHelper.isEmpty(flow.getHandlername())&&"GZ_YYDJHandler".equals(flow.getHandlername())) {
			nr_zmqlhsx = "异议登记";
		}
		return nr_zmqlhsx;
	}

	/*
	 * 获取权利人名称
	 */
	public String GetQLRMC(List<RightsHolder> qlrlist) {
		String nr_qlrmc = "";
		if (qlrlist != null && qlrlist.size() > 0) {
			List<String> qlrmclist = new ArrayList<String>();
			for (RightsHolder qlr : qlrlist) {
				String qlrmc = qlr.getQLRMC();
				if (!StringHelper.isEmpty(qlrmc)) {
//					if (!qlrmclist.contains(qlrmc)) {
						qlrmclist.add(qlrmc);
//					}
				}
			}
			nr_qlrmc = StringHelper.formatList(qlrmclist, ",");
		}
		return nr_qlrmc;
	}

	/*
	 * 获取义务人名称
	 */
	public String GetYWR(Rights ql, SubRights fsql) {
		String nr_ywr = fsql.getDYR();
		if (!ql.getQLLX().equals(QLLX.DIYQ.Value)) {
			nr_ywr = fsql.getYWR();
		}
		nr_ywr = ReplaceEmptyByBlankOnString(nr_ywr);
		return nr_ywr;
	}
	
	/*
	 * 桂林需求，更正登记证书信息的义务人读取申请人的义务人
	 */
	public String GetYWR_2(BDCS_XMXX xmxx,Rights ql, SubRights fsql) {
		
		String nr_ywr = fsql.getYWR();
		
		if(!ql.getQLLX().equals(QLLX.DIYQ.Value)){
			String xmbh = xmxx.getId();
			List<String> ywrmcList = new ArrayList<String>();
			List<BDCS_SQR> ywrLists = new ArrayList<BDCS_SQR>();
			ywrLists = baseCommonDao.getDataList(BDCS_SQR.class,"xmbh = '" + xmbh + "' AND SQRLB='" + SQRLB.YF.Value + "'");
			if(ywrLists != null && ywrLists.size()>0){
				for(BDCS_SQR ywr : ywrLists){
					String ywrmc = ywr.getSQRXM();
					if (!StringHelper.isEmpty(ywrmc)) {
						if (!ywrmcList.contains(ywrmc)) {
							ywrmcList.add(ywrmc);
						}
					}
				}
				nr_ywr = StringHelper.formatList(ywrmcList, ",");
				return nr_ywr;
			}
		}
		else if(QLLX.DIYQ.Value.equals(ql.getQLLX())){
			if(StringHelper.isEmpty(fsql.getYWR())||!fsql.getYWR().equals(fsql.getDYR())){
				nr_ywr = fsql.getDYR();
			}
		}
		nr_ywr = ReplaceEmptyByBlankOnString(nr_ywr);
		return nr_ywr;
	}
	/*
	 * 获取坐落
	 */
	public String GetZL(RealUnit unit) {
		String nr_zl = blank;
		if (unit != null) {
			nr_zl = StringHelper.formatObject(unit.getZL());
		}
		return nr_zl;
	}

	/*
	 * 获取不动产单元号
	 */
	public String GetBDCDYH(RealUnit unit) {
		String nr_bdcdyh = blank;
		if (unit != null) {
			String bdcdyh = StringHelper.formatObject(unit.getBDCDYH());
			nr_bdcdyh = formatBDCDYH(bdcdyh);
		}
		return nr_bdcdyh;
	}

	/*
	 * 获取权利其他状况
	 */
	public String GetQT(Rights ql, SubRights fsql, BDCS_XMXX xmxx, String ybdcqzsh,BDCDYLX lx,ProjectInfo info,RealUnit unit) {
		String yybdcqzh="";
		if ("1".equals(ConfigHelper.getNameByValue("yybdcqzh"))){
			yybdcqzh ="已有";
		}
		String xzqhdm = ConfigHelper.getNameByValue("XZQHDM");
		String nr_qt = "";
		StringBuilder builderQT = new StringBuilder();
		String se = "";// 一般抵押权：被担保主债权数额；最高额抵押权：最高债权数额
		String configJE2DX = ConfigHelper.getNameByValue("JEisDX");
		RegisterWorkFlow flow = HandlerFactory.getWorkflow(xmxx.getPROJECT_ID());
		if (QLLX.DIYQ.Value.equals(ql.getQLLX()))// 通过权利过滤，因为有组合业务
		{
			
			if (DJLX.YGDJ.Value.equals(xmxx.getDJLX())||"BG_YGandDYQBG_DJHandler".equals(flow.getHandlername())||"BG214".equals(flow.getName())||"GZ007".equals(flow.getName())
					||"BZ306".equals(flow.getName())) {
				YGDJLX ygdjlx = YGDJLX.initFrom(Integer.parseInt((StringHelper.isEmpty(fsql.getYGDJZL()) ? "0" : fsql.getYGDJZL())));
				if(ygdjlx!=null){
					builderQT.append("预告登记的种类：").append(ygdjlx.Name).append(hhf);
				}else{
					builderQT.append("预告登记的种类：").append(!StringHelper.isEmpty(xzqhdm)&&"230200".equals(xzqhdm)?"其它不动产抵押权预告登记":blank).append(hhf);
				}
			}
			String dyfs = fsql.getDYFS();
			String dyfsmc = "";
			if (!StringHelper.isEmpty(dyfs)) {
				dyfsmc = ConstHelper.getNameByValue_new("DYFS", dyfs.trim(),null);
			}
			dyfsmc = ReplaceEmptyByBlankOnString(dyfsmc);
			String strdw="元";
			if(fsql!=null&&!StringHelper.isEmpty(fsql.getZQDW())){
				strdw=ConstHelper.getNameByValue_new("JEDW", fsql.getZQDW(),null);
			}
			Double zse = 0.0;
			if("1".equals(configJE2DX)){
				if (ConstValue.DYFS.YBDY.Value.equals(fsql.getDYFS()))// 一般抵押权
				{
					if (fsql.getBDBZZQSE() != null){
						zse = fsql.getBDBZZQSE();
					}
					if (strdw.equals("万元")) {
						zse = zse * 10000;
					}
					se = "担保债权数额：" + StringHelper.number2CNMontrayUnit( new BigDecimal(zse))+ " ";// b被担保债权数额
					if (se.contains("元整")) {
						if (!strdw.equals("万元")) {
							se = se.replace("元", strdw);
						}
					}else {
						se = se + "(" + strdw + ")";
					}
				} else if (ConstValue.DYFS.ZGEDY.Value.equals(fsql.getDYFS())) {
					if (fsql.getZGZQSE() != null) {
						zse = fsql.getZGZQSE();
					}
					if (strdw.equals("万元")) {
						zse = zse * 10000;
					}
					se = "最高债权数额：" + StringHelper.number2CNMontrayUnit( new BigDecimal(zse))+ " ";// 最高债权数额
					if (se.contains("元整")) {
						if (!strdw.equals("万元")) {
							se = se.replace("元", strdw);
						}
					}else {
						se = se + "(" + strdw + ")";
					}
				}
			}else{
				if (ConstValue.DYFS.YBDY.Value.equals(fsql.getDYFS()))// 一般抵押权
				{
					se = "担保债权数额：" + ReplaceEmptyByBlankOnDouble(fsql.getBDBZZQSE()) + " " +strdw;// b被担保债权数额
				} else if (ConstValue.DYFS.ZGEDY.Value.equals(fsql.getDYFS())) {
					se = "最高债权数额：" + ReplaceEmptyByBlankOnDouble(fsql.getZGZQSE()) + " " +strdw;// 最高债权数额
				}
			}
			String qssj = ReplaceEmptyByBlankOnDate(ql.getQLQSSJ()) + "起";
			String jssj = ReplaceEmptyByBlankOnDate(ql.getQLJSSJ()) + "止";
			//判断是否是转移预告登记的抵押预告登记
			boolean bzyygdy=false;
			if(DJLX.YGDJ.Value.equals(ql.getDJLX()) && QLLX.DIYQ.Value.equals(ql.getQLLX()) && BDCDYLX.H.equals(lx)){
				if(ql!=null){
					if(!StringHelper.isEmpty(ql.getLYQLID())){
						Rights ql_ly=RightsTools.loadRights(DJDYLY.LS, ql.getLYQLID());
						if(ql_ly!=null){
							if(QLLX.QTQL.Value.equals(ql_ly.getQLLX())&&DJLX.YGDJ.Value.equals(ql_ly.getDJLX())){
								bzyygdy=true;
								builderQT.append("预告不动产权证明号：");
								builderQT.append(ql_ly.getBDCQZH()).append(hhf);
							}
						}else{
							ql_ly=RightsTools.loadRights(DJDYLY.GZ, ql.getLYQLID());
							if(ql_ly!=null&&xmxx.getId().equals(ql_ly.getXMBH())){
								if(QLLX.QTQL.Value.equals(ql_ly.getQLLX())&&DJLX.YGDJ.Value.equals(ql_ly.getDJLX())){
									bzyygdy=true;
									builderQT.append("预告不动产权证明号：");
									builderQT.append(StringHelper.formatObject(ql_ly.getBDCQZH())).append(hhf);
								}
							}
						}
					}
				}
			}
			if (!StringHelper.isEmpty(ybdcqzsh))// 过滤在建工程抵押的
			{
				if(!bzyygdy){
					String flowName = HandlerFactory.getWorkflow(xmxx.getPROJECT_ID()).getName();
					if (DJLX.YGDJ.Value.equals(xmxx.getDJLX())||DJLX.YGDJ.Value.equals(ql.getDJLX())) {
						if(BDCDYLX.H.equals(lx)) {
							if ("GZ202".equals(flowName)) 
								builderQT.append(yybdcqzh+"预告不动产权证明号：");
							else
								builderQT.append(yybdcqzh+"产权证书号：");
						}else {
							builderQT.append(yybdcqzh+"产权证明号：");
						}
					}else if(DJLX.BGDJ.Value.equals(xmxx.getDJLX())){
						if(BDCDYLX.H.equals(lx)) {
							if ("BG214".equals(flowName)) 
								builderQT.append(yybdcqzh+"预告不动产权证明号：");
						}
						if(QLLX.DIYQ.Value.equals(xmxx.getQLLX())){
							if ("BG005".equals(flowName)||"BG006".equals(flowName)||"BG042".equals(flowName))
								builderQT.append(yybdcqzh+"产权证书号：");
						}
						if ("BG042".equals(flowName)){
							builderQT.append(yybdcqzh+"产权证书号：");
						}
					}else if(DJLX.ZYDJ.Value.equals(xmxx.getDJLX())&&QLLX.DIYQ.equals(ql.getQLLX())){
						if ("ZY049".equals(flowName)) 
							builderQT.append(yybdcqzh+"产权证明号：");
					} else {
						builderQT.append(yybdcqzh+"产权证书号：");
					}
					///桂林需要房屋宗地一体抵押的情况下，证明号显示宗地权证号 
 					if(ConfigHelper.getNameByValue("XZQHDM").contains("4503")&&("CS034".equals(flowName)||"CS037".equals(flowName))) {
						String zdbdcqzh  = "";
						if(unit!=null) {
							House h = (House)unit;
 							 zdbdcqzh =  getZDBDCZQH(h.getZDBDCDYID());
						}
						if ("".equals(zdbdcqzh)||ybdcqzsh.contains(zdbdcqzh)) {
							builderQT.append(ybdcqzsh).append(hhf);
						}else {
							if(StringUtils.isEmpty(ybdcqzsh)) {
								builderQT.append(zdbdcqzh).append(hhf);
							}else {
								builderQT.append(ybdcqzsh).append("、").append(zdbdcqzh).append(hhf);
							}
						}
					}else {
						builderQT.append(ybdcqzsh).append(hhf);
					}
				}
			}else {
				//如果是在建工程建筑物和宗地一体抵押，桂林要求显示土地产权信息 
				String flowName = HandlerFactory.getWorkflow(xmxx.getPROJECT_ID()).getName();
				if(ConfigHelper.getNameByValue("XZQHDM").contains("4503")&&("CS037".equals(flowName))) {
					builderQT.append(yybdcqzh+"产权证书号：");
					String zdbdcqzh  = "";
					if(unit!=null) {
						House h = (House)unit;
							 zdbdcqzh =  getZDBDCZQH(h.getZDBDCDYID());
					}
					if ("".equals(zdbdcqzh)||ybdcqzsh.contains(zdbdcqzh)) {
						builderQT.append(ybdcqzsh).append(hhf);
					}else {
						if(StringUtils.isEmpty(ybdcqzsh)) {
							builderQT.append(zdbdcqzh).append(hhf);
						}else {
							builderQT.append(ybdcqzsh).append("、").append(zdbdcqzh).append(hhf);
						}
					} 
				}
			}
			if(BDCDYLX.H.equals(unit.getBDCDYLX())&&ConfigHelper.getNameByValue("XZQHDM").contains("6501")){
				List<String> list_xtdz_str=new ArrayList<String>();
				List<BDCS_XTDZ> list_xtdz=baseCommonDao.getDataList(BDCS_XTDZ.class, "FWBDCDYID='"+unit.getId()+"' AND YXBZ=1 AND IFSHOWTDZH=0 ");
				if(list_xtdz!=null&&list_xtdz.size()>0){
					for(BDCS_XTDZ xtdz:list_xtdz){
						if(!StringHelper.isEmpty(xtdz.getTDZ())&&!list_xtdz_str.contains(xtdz.getTDZ())){
							list_xtdz_str.add(xtdz.getTDZ());
						}
					}
				}
				if(list_xtdz_str!=null&&list_xtdz_str.size()>0){
					builderQT.append("土地证号：").append(StringHelper.formatList(list_xtdz_str, "、")).append(hhf);
				}
			}
			builderQT.append("抵押方式：").append(dyfsmc).append(hhf);
			builderQT.append(se).append(hhf);
			if (!StringHelper.isEmpty(dyfs)) {
				if(ConstValue.DYFS.ZGEDY.Value.equals(dyfs.trim())){
					builderQT.append("债权确定期间：").append(qssj).append(jssj);
				}else{
					
					if (xzqhdm.indexOf("441821") == 0) {//广东佛冈
						builderQT.append("债权起止时间：</br>").append(qssj).append(jssj);
					}else {
						builderQT.append("债权起止时间：").append(qssj).append(jssj);
					}
				}
			}else{
				if (xzqhdm.indexOf("441821") == 0) {//广东佛冈
					builderQT.append("债权起止时间：</br>").append(qssj).append(jssj);
				}else {
					builderQT.append("债权起止时间：").append(qssj).append(jssj);
				}
			}
		} else if (DJLX.YGDJ.Value.equals(xmxx.getDJLX())||DJLX.YGDJ.Value.equals(ql.getDJLX())||"BG_YGandDYQBG_DJHandler".equals(flow.getHandlername()))// 预告登记
		{
			YGDJLX ygdjlx = YGDJLX.initFrom(Integer.parseInt((StringHelper.isEmpty(fsql.getYGDJZL()) ? "0" : fsql.getYGDJZL())));
			if(ygdjlx!=null){
				builderQT.append("预告登记的种类：").append(ygdjlx.Name).append(hhf);
			}else{
				builderQT.append("预告登记的种类：").append(blank);
			}

		} else if (DJLX.YYDJ.Value.equals(xmxx.getDJLX())||flow.getName().equals("BZ005")||flow.getName().equals("BZ006"))// 异议登记
		{
			String yysx = ReplaceEmptyByBlankOnString(fsql.getYYSX());
			builderQT.append("异议登记的内容：").append(yysx).append(hhf);
		}
		String qxdm = ConfigHelper.getNameByValue("QLMJXX");
		String pfm = "㎡";
		String qhdm = ConfigHelper.getNameByValue("XZQHDM");
		if(qhdm.indexOf("450721") == 0 ){//广西灵山
			pfm = "平方米";
		}
		if("1".equals(qxdm)){
			if(unit.getMJ()!=0){ //liang q
				if ("LD".equals(lx.name())) {//林地 的面积单位“亩”
					builderQT.append(hhf).append("权利面积：").append(unit.getMJ()+"亩").append(hhf);
				}else {
					builderQT.append(hhf).append("权利面积：").append(unit.getMJ()+pfm).append(hhf);
				}
			}else{
				builderQT.append(hhf).append("权利面积：").append(blank).append(hhf);
			}
		}
		nr_qt = builderQT.toString();
		return nr_qt;
	}

	/**
	 * 获取地役权权利其他状况，包括三部分（1）供役地的不动产权证书号；（2）需役地的坐落；（3）地役权的内容。
	 * @param ql
	 * @param xmxx
	 * @return
	 */
    public String getQT_DYQ(Rights ql,BDCS_XMXX xmxx){
    	StringBuilder builderQT = new StringBuilder();
    	String gydbdczh="";
    	String gydzl="";
    	String xydnr="";
    	String qssj="";
    	String jssj ="";
    	if(!StringHelper.isEmpty(ql)){
    		BDCS_QL_GZ ql_gz =(BDCS_QL_GZ)ql;
    	   BDCS_FSQL_GZ fsql_gz=(BDCS_FSQL_GZ) RightsTools.loadSubRights(DJDYLY.GZ, ql_gz.getFSQLID());
    	   if(fsql_gz !=null){
    		   xydnr=StringHelper.FormatByDatatype(fsql_gz.getDYQNR());
    	   }
    	     qssj = ReplaceEmptyByBlankOnDate(ql.getQLQSSJ()) + "起";
			 jssj = ReplaceEmptyByBlankOnDate(ql.getQLJSSJ()) + "止";
    		String gydbdcdyid=ql_gz.getGYDBDCDYID();
    		String gydbdcdylx=ql_gz.getGYDBDCDYLX();
    		if(!StringHelper.isEmpty(gydbdcdyid)){
    		RealUnit unit= UnitTools.loadUnit(BDCDYLX.initFrom(gydbdcdylx), DJDYLY.LS, gydbdcdyid);
    		if(unit ==null){
    			unit= UnitTools.loadUnit(BDCDYLX.initFrom(gydbdcdylx), DJDYLY.GZ, gydbdcdyid);
    		}
    		if(unit !=null){
    			gydzl=StringHelper.FormatByDatatype(unit.getZL());
    			  List<BDCS_DJDY_GZ> lstdjdy=baseCommonDao.getDataList(BDCS_DJDY_GZ.class, ProjectHelper.GetXMBHCondition(xmxx.getId())+" AND BDCDYID='"+gydbdcdyid+"'");
    		    if(lstdjdy !=null && lstdjdy.size()>0){
    		    	BDCS_DJDY_GZ djdy=lstdjdy.get(0);
    		    	String qllxsql="QLLX IN ('1','2','3','5','7','9','10','11','12','13','14','15','16','17','18','24')";
    		    	String condition_gz=qllxsql +" AND DJDYID='"+djdy.getDJDYID()+"'";
    		    	List<Rights> lstrigths=RightsTools.loadRightsByCondition(DJDYLY.XZ,condition_gz);
    		    	if(lstrigths !=null && lstrigths.size()>0){
    		    		Rights rights=lstrigths.get(0);
    		    		gydbdczh=StringHelper.FormatByDatatype(rights.getBDCQZH());
    		    	}
    		    }
    		}
    		}
    	}
    	builderQT.append("供役地不动产权证书号:").append(gydbdczh).append(hhf);
    	builderQT.append("供役役地坐落:").append(gydzl).append(hhf);
    	builderQT.append("地役权设定合同:").append(qssj).append(jssj).append(hhf);
    	builderQT.append("地役权内容:").append(xydnr).append(hhf);
    	return builderQT.toString();
    }	
	/*
	 * 获取附记
	 */
	public String GetFJ(BDCS_XMXX xmxx, Rights ql, String fj_plqz, List<RightsHolder> qlrlist, Rights qll,RealUnit unit) {
		String fj = "";
		String fj_ywh = "";
		String fj_qlfj = "";
		String fj_qlrzjh = "";
		String fj_mjh = "";
		String pfm = "㎡";
		String qhdm = ConfigHelper.getNameByValue("XZQHDM");
		if(qhdm.indexOf("450721") == 0 ){//广西灵山
			pfm = "平方米";
		}

		String configShowYWH = ConfigHelper.getNameByValue("ShowYWHInFJ");
		if (!StringHelper.isEmpty(xmxx) && "1".equals(configShowYWH)) {
			String project_Id = xmxx.getPROJECT_ID();
			String ywlsh = xmxx.getYWLSH();
			if (!StringHelper.isEmpty(ywlsh)) {
				fj_ywh = "业务编号：" + ywlsh + hhf;
			} else if (!StringHelper.isEmpty(project_Id)) {
				fj_ywh = "业务编号：" + project_Id + hhf;
			}
		}
		String hmj= ConfigHelper.getNameByValue("hymj");
		if (!StringHelper.isEmpty(xmxx)&&"1".equals(hmj)){
			if(CZFS.GTCZ.Value.equals(qll.getCZFS())){
				fj_mjh = "建筑面积：" + ReplaceEmptyByBlankOnDouble(unit.getMJ()) + pfm;
			}else{
				fj_mjh = "建筑面积：" + ReplaceEmptyByBlankOnDouble(unit.getMJ()) + pfm;
			}
		}

		String qlrandzjh = ConfigHelper.getNameByValue("xzqlrandzjh");
		if (!StringHelper.isEmpty(xmxx) && "1".equals(qlrandzjh)) {
			RegisterWorkFlow flow = HandlerFactory.getWorkflow(xmxx.getPROJECT_ID());
			if("YG001".equals(flow.getName())){
				for (RightsHolder qlr : qlrlist) {
					String qlrmc = qlr.getQLRMC() + qlr.getZJH();
					if (!StringHelper.isEmpty(qlrmc)) {
						fj_qlrzjh = qlrmc + hhf;
					}
				}
			}
		}
		if (ql != null && !StringHelper.isEmpty(ql.getFJ())) {
			fj_qlfj = ql.getFJ().replaceAll("\r\n|\r|\n|\n\r", hhf).replaceAll(" ", "\u00A0") + hhf;
		}
		String configShowOhterQZH = ConfigHelper.getNameByValue("ShowOtherQZHInFJ");
		fj = fj_ywh + fj_qlfj;
		if ("1".equals(configShowOhterQZH)) {
			fj += fj_plqz;
		}
		if ("1".equals(qlrandzjh)) {
			fj += fj_qlrzjh;
		}
		if ("1".equals(hmj)) {
			fj += fj_mjh;
		}
		return fj;
	}

	public String ReplaceEmptyByBlankOnString(String obj) {
		String value = blank;
		if (!StringHelper.isEmpty(obj) && !"null".equals(obj)) {
			value = StringHelper.formatObject(obj);
		}
		return value;
	}

	public String ReplaceEmptyByBlankOnDouble(double obj) {
		String value = blank;
		if (!StringHelper.isEmpty(obj) && obj > 0) {
			DecimalFormat df = new DecimalFormat("#########.###");
			df.setRoundingMode(RoundingMode.HALF_UP);
			value = df.format(obj);
		}
		return value;
	}

	public String ReplaceEmptyByBlankOnDate(Date obj) {
		String value = StringHelper.FormatByDatetime(obj);
		if (StringHelper.isEmpty(value)) {
			value = blank;
		}
		return value;
	}

	/**
	 * 格式化不动产单元号，中间加空格
	 * 
	 * @Title: formatBDCDYH
	 * @author:liushufeng
	 * @date：2015年11月13日 下午11:33:11
	 * @param bdcdyh
	 * @return
	 */
	public String formatBDCDYH(String bdcdyh) {
		String _bdcdyh = "";
		// 字符串不为null并且不为""
		if (!StringHelper.isEmpty(bdcdyh)) {
			int length = bdcdyh.length();
			if (length > 0 && length <= 6) { // 长度是0-6
				_bdcdyh = bdcdyh;
			} else if (length > 6 && length <= 12) { // 长度是7-12
				String s1 = bdcdyh.substring(0, 6);
				String s2 = bdcdyh.substring(6);
				_bdcdyh = s1 + "  " + s2;
			} else if (length > 12 && length <= 19) { // 长度是13-19
				String s1 = bdcdyh.substring(0, 6);
				String s2 = bdcdyh.substring(6, 12);
				String s3 = bdcdyh.substring(12);
				_bdcdyh = s1 + " " + s2 + " " + s3;
			} else { // 长度大于19
				String s1 = bdcdyh.substring(0, 6);
				String s2 = bdcdyh.substring(6, 12);
				String s3 = bdcdyh.substring(12, 19);
				String s4 = bdcdyh.substring(19, length);
				_bdcdyh = s1 + " " + s2 + " " + s3 + " " + s4;
			}
		}
		return _bdcdyh;
	}
	/**
	 * 获取宗地产权证号
	 * @param zdbdcdyid 
	 * @return
	 */
	private String getZDBDCZQH(String zdbdcdyid) {
		String zdbdcqzh  = "";
		if(!StringUtils.isEmpty(zdbdcdyid)) {
			//获取了宗地的不动产单元ID，那么就要通过期登记单元ID获取到其权利，然后获取到BDCQZH
			String fulSql = "select wm_concat(to_char(BDCQZH)) ZDBDCQZH  from BDCK.BDCS_QL_XZ where DJDYID=(select DISTINCT DJDYID from  BDCK.bdcs_djdy_XZ where BDCDYID='"+zdbdcdyid+"') AND QLLX IN ('3','5','7') AND (DJLX IS NULL OR DJLX<>'700')";
	 		List<Map> list = baseCommonDao.getDataListByFullSql(fulSql);
	 		if(list!=null&&!list.isEmpty()) {
	 			zdbdcqzh = StringHelper.formatObject(list.get(0).get("ZDBDCQZH"));
	 		}
		} 
		return zdbdcqzh;
	}
}
