/**   
 * 请描述这个文件
 * @Title: EntityTool.java 
 * @Package com.supermap.realestate.registration.util 
 * @author liushufeng 
 * @date 2015年7月12日 下午5:20:33 
 * @version V1.0   
 */

package com.supermap.realestate.registration.tools;

import java.io.UnsupportedEncodingException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.supermap.realestate.registration.util.*;
import org.apache.commons.lang.StringUtils;

import com.supermap.realestate.registration.ViewClass.ProjectInfo;
import com.supermap.realestate.registration.ViewClass.ZS;
import com.supermap.realestate.registration.model.BDCS_DJDY_GZ;
import com.supermap.realestate.registration.model.BDCS_QLR_GZ;
import com.supermap.realestate.registration.model.BDCS_SHYQZD_XZ;
import com.supermap.realestate.registration.model.BDCS_SQR;
import com.supermap.realestate.registration.model.BDCS_SYQZD_XZ;
import com.supermap.realestate.registration.model.BDCS_TDYT_GZ;
import com.supermap.realestate.registration.model.BDCS_TDYT_XZ;
import com.supermap.realestate.registration.model.BDCS_XMXX;
import com.supermap.realestate.registration.model.BDCS_YHZK_GZ;
import com.supermap.realestate.registration.model.BDCS_YHZK_LS;
import com.supermap.realestate.registration.model.BDCS_ZS_GZ;
import com.supermap.realestate.registration.model.WFD_MAPPING;
import com.supermap.realestate.registration.model.interfaces.AgriculturalLand;
import com.supermap.realestate.registration.model.interfaces.Building;
import com.supermap.realestate.registration.model.interfaces.Forest;
import com.supermap.realestate.registration.model.interfaces.House;
import com.supermap.realestate.registration.model.interfaces.OwnerLand;
import com.supermap.realestate.registration.model.interfaces.RealUnit;
import com.supermap.realestate.registration.model.interfaces.Rights;
import com.supermap.realestate.registration.model.interfaces.RightsHolder;
import com.supermap.realestate.registration.model.interfaces.Sea;
import com.supermap.realestate.registration.model.interfaces.Structure;
import com.supermap.realestate.registration.model.interfaces.SubRights;
import com.supermap.realestate.registration.model.interfaces.TDYT;
import com.supermap.realestate.registration.model.interfaces.UseLand;
import com.supermap.realestate.registration.util.ConstValue.BDCDYLX;
import com.supermap.realestate.registration.util.ConstValue.CZFS;
import com.supermap.realestate.registration.util.ConstValue.DJDYLY;
import com.supermap.realestate.registration.util.ConstValue.GYFS;
import com.supermap.realestate.registration.util.ConstValue.QLLX;
import com.supermap.realestate.registration.util.ConstValue.SF;
import com.supermap.wisdombusiness.core.SuperSpringContext;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import com.supermap.wisdombusiness.workflow.model.Wfi_ProInst;

/**
 * 
 * 证书信息获取操作类，不允许添加重载方法，保证证书方式获取信息
 * 
 * @ClassName: CertInfoTools
 * @author 俞学斌
 * @date 2015年12月24日 00:25:33
 */
public class CertInfoTools {
	private String blank = "----";// 空白符
	private String hhf = "<br/>";// 换行符
	private String space = "&nbsp;&nbsp;&nbsp;";// 空格

	private CommonDao baseCommonDao;

	private String exchange = "";

	public CertInfoTools() {
		this.baseCommonDao = SuperSpringContext.getContext().getBean(
				CommonDao.class);
	}

	/**
	 * 获取证书信息
	 * 
	 * @Title: getZSForm
	 * @author:liushufeng
	 * @date：2015年11月14日 下午8:19:26
	 * @param xmxx
	 * @param zsid
	 * @return
	 */
	public ZS CreatZS(BDCS_XMXX xmxx, String zsid) {
		ZS zsinfo = new ZS();
		
		ProjectInfo info = ProjectHelper.GetPrjInfoByXMBH(xmxx.getId());
		
		String workflowcode = ProjectHelper.getWorkflowCodeByProjectID(xmxx.getPROJECT_ID());
		String sql = " WORKFLOWCODE='" + workflowcode + "'";
		WFD_MAPPING mapping=new WFD_MAPPING();
		List<WFD_MAPPING> list_mapping = baseCommonDao.getDataList(
				WFD_MAPPING.class, sql);
		if(list_mapping!=null&&list_mapping.size()>0){
			mapping=list_mapping.get(0);
		}

		/************************************ 获取证书显示需要信息来源 ************************************/
		// 获取证书信息
		BDCS_ZS_GZ zs = baseCommonDao.get(BDCS_ZS_GZ.class, zsid);
		// 获取权利人信息集合
		List<RightsHolder> qlrlist = RightsHolderTools.loadRightsHoldersByZSID(
				DJDYLY.GZ, xmxx.getId(), zsid);
		// 获取权利关联权利人个数
		List<RightsHolder> qlrAllList = null;
		if (qlrlist != null && qlrlist.size() > 0) {
			qlrAllList = RightsHolderTools.loadRightsHolders(DJDYLY.GZ, qlrlist
					.get(0).getQLID());
		}

		// 获取权利信息
		Rights ql = RightsTools.loadRightsByZSID(DJDYLY.GZ, zsid, xmxx.getId());
		SubRights fsql = null;
		if (ql != null) {
			fsql = RightsTools.loadSubRightsByRightsID(DJDYLY.GZ, ql.getId());
			if(fsql == null){
				fsql = RightsTools.loadSubRights(DJDYLY.GZ, ql.getFSQLID());
			}
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
		if(unit == null && xmxx.getSFDB().equals("1")){
			ly = DJDYLY.initFrom("03");
			unit = UnitTools.loadUnit(lx, ly, bdcdyid);
		}
		RealUnit landunit = null;
		List<TDYT> tdytlist = new ArrayList<TDYT>();
		if (unit != null && BDCDYLX.SHYQZD.equals(lx)) {
			UseLand land = (UseLand) unit;
			if (land != null) {
				tdytlist = land.getTDYTS();
			}
		}else if (unit != null && BDCDYLX.H.equals(lx)) {
			House h = (House) unit;
			if (h != null && !StringHelper.isEmpty(h.getZDBDCDYID())) {
				List<RealUnit> landunits = UnitTools.loadUnits(BDCDYLX.SHYQZD,
						DJDYLY.GZ, "XMBH='" + xmxx.getId() + "' and BDCDYID='"
								+ h.getZDBDCDYID() + "'");
				if (!StringHelper.isEmpty(landunits) && landunits.size() > 0) {
					landunit = landunits.get(0);
				} else {
					landunit = UnitTools.loadUnit(BDCDYLX.SHYQZD, DJDYLY.XZ,
							h.getZDBDCDYID());
				}
				if (landunit == null) {
					landunit = UnitTools.loadUnit(BDCDYLX.SYQZD, DJDYLY.XZ,
							h.getZDBDCDYID());
				}
				if (landunit != null) {
					if (BDCDYLX.SHYQZD.equals(landunit.getBDCDYLX())) {
						UseLand land = (UseLand) landunit;
						if (land != null) {
							tdytlist = land.getTDYTS();
						}
					}
				}
			}
		} else if (unit != null && BDCDYLX.GZW.equals(lx)) {
			Structure gzw = (Structure) unit;
			if (gzw != null && !StringHelper.isEmpty(gzw.getZDBDCDYID())) {
				List<RealUnit> landunits = UnitTools.loadUnits(BDCDYLX.SHYQZD,
						DJDYLY.GZ, "XMBH='" + xmxx.getId() + "' and BDCDYID='"
								+ gzw.getZDBDCDYID() + "'");
				if (!StringHelper.isEmpty(landunits) && landunits.size() > 0) {
					landunit = landunits.get(0);
				} else {
					landunit = UnitTools.loadUnit(BDCDYLX.SHYQZD, DJDYLY.XZ,
							gzw.getZDBDCDYID());
				}
				if (landunit == null) {
					landunit = UnitTools.loadUnit(BDCDYLX.SYQZD, DJDYLY.XZ,
							gzw.getZDBDCDYID());
				}
				if (landunit != null) {
					if (BDCDYLX.SHYQZD.equals(landunit.getBDCDYLX())) {
						UseLand land = (UseLand) landunit;
						if (land != null) {
							tdytlist = land.getTDYTS();
						}
					}
				}
				
			}
		} else if (unit != null && BDCDYLX.LD.equals(lx)) {
			Forest forest = (Forest) unit;
			if (forest != null) {
				tdytlist = forest.getTDYTS();
			}
		}
		/************************************ 获取证书显示需要信息来源 ************************************/

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

		// NR_2,权利人
		String nr_qlrmc = GetQLRMC(qlrlist);
		// NR_3,共有情况
		String nr_gyqk = GetGYQK(qlrlist);
		// NR_4,坐落
		String nr_zl = GetZL(unit);
		// NR_5,不动产单元号
		String nr_bdcdyh = GetBDCDYH(unit);
		// NR_6,权利类型
		String nr_qllx = GetQLLX(ql,StringHelper.formatObject(xmxx.getSLSJ()));
		// NR_7,权利性质
		String nr_qlxz = GetQLXZ(unit, lx, landunit, info,tdytlist,mapping);
		// NR_8,用途
		String nr_yt = GetTDYT(unit, lx, landunit, tdytlist, info,StringHelper.formatObject(xmxx.getSLSJ()));
		// NR_9,面积
//		String nr_mj = "";
//		BDCS_QLR_GZ qlr= new BDCS_QLR_GZ();  
//		if ("1".equals(ConfigHelper.getNameByValue("mjpz"))){
//			nr_mj=StringHelper.formatObject(qlr.getQLMJ());
//			if (qlr.getQLMJ() == null){
//				nr_mj = GetMJ(unit, lx, landunit);
//			}
//		}else{
//			nr_mj = GetMJ(unit, lx, landunit);
//		}
		String nr_mj = GetMJ(unit, lx, landunit, ql, info,mapping);
		// NR_10,使用期限
		String nr_syqx = GetSYQX(unit, ql, landunit, tdytlist, info);
		// NR_11,其他权利状况
		// String nr_qt = GetQT(unit, qlrlist, ql);
		String nr_qt = GetQT(unit, qlrAllList, ql, fsql, xmxx, info,mapping,tdytlist);

		// FJ_1,附记
		String fj = GetFJ(xmxx, ql, lx, qlrAllList, unit);
		/*************************************** 生成证书显示信息 ***************************************/

		/*************************************** 赋值证书显示信息 ***************************************/
		zsinfo.setFm_year(fm_djsj_year);
		zsinfo.setFm_month(fm_djsj_month);
		zsinfo.setFm_day(fm_djsj_day);
		zsinfo.setZsbh(fm_zsbh);

		zsinfo.setQhjc(nr_bdcqzh_qhjc);
		zsinfo.setNd(nr_bdcqzh_nd);
		zsinfo.setQhmc(nr_bdcqzh_qhmc);
		zsinfo.setCqzh(nr_bdcqzh_xh);

		zsinfo.setQlr(nr_qlrmc);
		zsinfo.setGyqk(nr_gyqk);
		zsinfo.setZl(nr_zl);
		zsinfo.setBdcdyh(nr_bdcdyh);
		zsinfo.setQllx(nr_qllx);
		zsinfo.setQlxz(nr_qlxz);
		zsinfo.setYt(nr_yt);
		zsinfo.setMj(nr_mj);
		zsinfo.setSyqx(nr_syqx);
		zsinfo.setQlqtzk(nr_qt);
		if ("抵押权".equals(nr_qllx)){
			zsinfo.setType("ZM");
		}else {
			zsinfo.setType("ZS");
		}
		zsinfo.setFj(fj);

        // 证明权利或事项
        String zmqlhsx = "";
        if (ConstValue.DJLX.YGDJ.Value.equals(xmxx.getDJLX()))// 预告登记
        {
            zmqlhsx = "预告登记";
        } else if (ConstValue.DJLX.YYDJ.Value.equals(xmxx.getDJLX())) {
            zmqlhsx = "异议登记";
        } else if (QLLX.DIYQ.Value.equals(xmxx.getQLLX())||(!StringHelper.isEmpty(ql)&&QLLX.DIYQ.Value.equals(ql.getQLLX()))) {
            zmqlhsx = "抵押权";
        }
		zsinfo.setZmqlhsx(zmqlhsx);
		//添加义务人ywr
		List<BDCS_SQR> listsqr = baseCommonDao.getDataList(BDCS_SQR.class,"xmbh='"+xmxx.getId() + "' ORDER BY SXH");
		if (listsqr != null) {
			String yWR = null;
			for (BDCS_SQR s : listsqr) {
				if (!StringHelper.isEmpty(s.getSQRLB())) {
					if ("2".equals(s.getSQRLB())) {
						yWR= StringHelper.isEmpty(yWR) ? s.getSQRXM() : yWR + "," + s.getSQRXM();
						zsinfo.setYwr(yWR);
					}
				}
			}
		} 
		//添加原不动产权证号
		if(!StringHelper.isEmpty(ql.getLYQLID())){
			Rights ql_ls=RightsTools.loadRights(DJDYLY.LS, ql.getLYQLID());
			if(ql_ls != null){
				List<RightsHolder> list_qlr_ls=RightsHolderTools.loadRightsHolders(DJDYLY.LS, ql.getLYQLID());
				if(list_qlr_ls != null && list_qlr_ls.size() > 0){
					String ybdcqzh = "";
					List<String> ybdcqzhs = new ArrayList<String>();
					for(RightsHolder qlr_ls:list_qlr_ls){
						if(!StringHelper.isEmpty(qlr_ls.getBDCQZH()) && !ybdcqzhs.contains(qlr_ls.getBDCQZH())){
							ybdcqzh= StringHelper.isEmpty(ybdcqzh) ? qlr_ls.getBDCQZH() : ybdcqzh + "," + qlr_ls.getBDCQZH();
							ybdcqzhs.add(qlr_ls.getBDCQZH());
						}
					}
					zsinfo.setYbdcqzh(ybdcqzh);
				}
			}
		}
		/*************************************** 赋值证书显示信息 ***************************************/
		return zsinfo;
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
		hqlBuilder
				.append(" AND DJDYID IN (select distinct DJDYID from BDCS_QDZR_GZ ");
		hqlBuilder.append(" WHERE ZSID = '").append(zsid).append("' ");
		hqlBuilder.append(" AND XMBH = '").append(xmbh).append("' ) ");
		List<BDCS_DJDY_GZ> list = baseCommonDao.getDataList(BDCS_DJDY_GZ.class,hqlBuilder.toString());
		if (null != list && list.size() > 0) {
			return list.get(0);
		}
		return null;
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
				List<String> listqzh = StringHelper.MatchBDCQZH(bdcqzh);
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
			nr_qlrmc = StringHelper.formatList(qlrmclist,",");
			// 权利人名称多于本地化配置设置显示个数时，显示等xx个
			int qlgs = 0;
			if (qlrmclist != null && qlrmclist.size() > 0) {
				int count = StringHelper.getInt(ConfigHelper.getNameByValue("ZSPARAM_QLR_COUNT"));
				qlgs = count == 0 ? 1:count;
				if (qlrmclist.size() > qlgs) {
					for (int i = 0; i < qlgs; i++) {
						if (i == 0) {
							nr_qlrmc = qlrmclist.get(i);
						} else {
							nr_qlrmc = nr_qlrmc + "、" + qlrmclist.get(i);
						}
					}
					nr_qlrmc = nr_qlrmc + "等" + qlrmclist.size() + "人";
				}
			}
		}
		return nr_qlrmc;
	}

	/*
	 * 获取共有方式
	 */
	public String GetGYQK(List<RightsHolder> qlrlist) {
		String nr_gyqk = blank;
		if (qlrlist != null && qlrlist.size() > 0) {
			for (RightsHolder qlr : qlrlist) {
				String gyfs = qlr.getGYFS();
				if (!StringHelper.isEmpty(gyfs)) {
					nr_gyqk = ConstHelper.getNameByValue_new("GYFS", gyfs.trim(),null);
					if (!StringHelper.isEmpty(nr_gyqk)) {
						break;
					}
				}
			}
		}
		return nr_gyqk;
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
	 * 获取权利类型
	 */
	public String GetQLLX(Rights ql,String slsj) {
		String nr_qllx = blank;
		if (ql != null) {
			String qllx = ql.getQLLX();
			if (!StringHelper.isEmpty(qllx)) {
				nr_qllx = ConstHelper.getNameByValue_new("QLLX", qllx.trim(),slsj);
			}
		}
		return nr_qllx;
	}

	/*
	 * 获取权利性质
	 */
	public String GetQLXZ(RealUnit unit, BDCDYLX lx, RealUnit landunit , ProjectInfo info,List<TDYT> tdytlist,WFD_MAPPING mapping) {
		String nr_qlxz = "";
		if (unit != null) {
			String nr_qlxz_zd = "";
			String nr_qlxz_h = "";
			if (BDCDYLX.SHYQZD.equals(lx) || BDCDYLX.SYQZD.equals(lx)) {
				if("1".equals(mapping.getCERTMODE())){
					List<String> list_qlxz=new ArrayList<String>();
					for(TDYT tdyt:tdytlist){
						String qlxz=tdyt.getQLXZName();
						if(!StringHelper.isEmpty(qlxz)&&!list_qlxz.contains(qlxz)){
							list_qlxz.add(0, qlxz);
						}
					}
					nr_qlxz_zd=StringHelper.formatList(list_qlxz, "、");
				}else{
					String zdqlxz = "";
					if (BDCDYLX.SHYQZD.equals(lx)) {
						UseLand land = (UseLand) unit;
						if (land != null) {
							zdqlxz = land.getQLXZ();
						}
					} else if (BDCDYLX.SYQZD.equals(lx)) {
						OwnerLand land = (OwnerLand) unit;
						if (land != null) {
							zdqlxz = land.getQLXZ();
						}
					}
					if (!StringHelper.isEmpty(zdqlxz)) {
						nr_qlxz_zd = ConstHelper.getNameByValue_new("QLXZ",
								zdqlxz.trim(),null);
					}
				}
				
				
				nr_qlxz = nr_qlxz_zd;
			} else if (BDCDYLX.H.equals(lx)) {
				House h = (House) unit;
				if (h != null) {
					String configZDQLXZ = ConfigHelper
							.getNameByValue("GetZDQLXZFrom");
					if ("0".equals(configZDQLXZ)) {// 从户中宗地权利性质获取
						String zdqlxz = h.getQLXZ();
						if (!StringHelper.isEmpty(zdqlxz)) {
							nr_qlxz_zd = ConstHelper.getNameByValue_new("QLXZ",
									zdqlxz.trim(),null);
						}
					} else if ("1".equals(configZDQLXZ)) {// 从关联宗地中获取宗地权利性质
						if (landunit != null) {
							String zdqlxz = "";
							if (BDCDYLX.SHYQZD.equals(landunit.getBDCDYLX())) {
								UseLand land = (UseLand) landunit;
								if (land != null) {
									zdqlxz = land.getQLXZ();
								}
							} else if (BDCDYLX.SYQZD.equals(landunit
									.getBDCDYLX())) {
								OwnerLand land = (OwnerLand) landunit;
								if (land != null) {
									zdqlxz = land.getQLXZ();
								}
							}
							if (!StringHelper.isEmpty(zdqlxz)) {
								nr_qlxz_zd = ConstHelper.getNameByValue_new("QLXZ",
										zdqlxz.trim(),null);
							}
						}
					} else if ("2".equals(configZDQLXZ)) {// 优先从户中获取宗地权利性质，再从关联宗地中获取
						String zdqlxz = h.getQLXZ();
						if (!StringHelper.isEmpty(zdqlxz)) {
							nr_qlxz_zd = ConstHelper.getNameByValue_new("QLXZ",
									zdqlxz.trim(),null);
						}
						if (StringHelper.isEmpty(nr_qlxz_zd)
								&& landunit != null) {
							if (BDCDYLX.SHYQZD.equals(landunit.getBDCDYLX())) {
								UseLand land = (UseLand) landunit;
								if (land != null) {
									zdqlxz = land.getQLXZ();
								}
							} else if (BDCDYLX.SYQZD.equals(landunit
									.getBDCDYLX())) {
								OwnerLand land = (OwnerLand) landunit;
								if (land != null) {
									zdqlxz = land.getQLXZ();
								}
							}
							if (!StringHelper.isEmpty(zdqlxz)) {
								nr_qlxz_zd = ConstHelper.getNameByValue_new("QLXZ",
										zdqlxz.trim(),null);
							}
						}
					}

					String fwxz = h.getFWXZ();
					if (!StringHelper.isEmpty(fwxz)) {
						nr_qlxz_h = ConstHelper.getNameByValue_new("FWXZ",
								fwxz.trim(),null);
					}
				}
				nr_qlxz_zd = ReplaceEmptyByBlankOnString(nr_qlxz_zd);
				nr_qlxz_h = ReplaceEmptyByBlankOnString(nr_qlxz_h);

				nr_qlxz = nr_qlxz_zd + "/" + nr_qlxz_h;
				if(info!=null&&!StringHelper.isEmpty(info.getZdbtn())&&info.getZdbtn()){
					nr_qlxz = blank + "/" + nr_qlxz_h;
				}
			} else if (BDCDYLX.LD.equals(lx)) {
				Forest forest = (Forest) unit;
				String ldqlxz = "";
				String lz = "";
				if (forest != null) {
					ldqlxz = ConstHelper.getNameByValue_new("QLXZ",
							forest.getQLXZ(),null);
					lz = ConstHelper.getNameByValue_new("LZ", forest.getLZ(),null);
				}
				ldqlxz = ReplaceEmptyByBlankOnString(ldqlxz);
				lz = ReplaceEmptyByBlankOnString(lz);
				nr_qlxz = ldqlxz + "/" + lz;
			} else if (BDCDYLX.HY.equals(lx)) {
				Sea sea = (Sea) unit;
				String qdfs = "";
				if (sea != null) {
					qdfs = ConstHelper.getNameByValue_new("HYQDFS", sea.getQDFS(),null);
				}
				qdfs = ReplaceEmptyByBlankOnString(qdfs);
				nr_qlxz = qdfs;
			}else if (BDCDYLX.NYD.equals(lx)) {
				AgriculturalLand nyd = (AgriculturalLand) unit;
				String nydfs = "";
				if (nyd != null) {
					nydfs = ConstHelper.getNameByValue_new("QLXZ", nyd.getQLXZ(),null);
				}
				nydfs = ReplaceEmptyByBlankOnString(nydfs);
				nr_qlxz = nydfs;
			}else if (BDCDYLX.GZW.equals(lx)) {
				Structure gzw = (Structure) unit;
				if (gzw != null) {
					if (landunit != null) {
						String zdqlxz = "";
						if (BDCDYLX.SHYQZD.equals(landunit.getBDCDYLX())) {
							UseLand land = (UseLand) landunit;
							if (land != null) {
								zdqlxz = land.getQLXZ();
							}
						} else if (BDCDYLX.SYQZD.equals(landunit
								.getBDCDYLX())) {
							OwnerLand land = (OwnerLand) landunit;
							if (land != null) {
								zdqlxz = land.getQLXZ();
							}
						}
						if (!StringHelper.isEmpty(zdqlxz)) {
							nr_qlxz_zd = ConstHelper.getNameByValue_new("QLXZ",zdqlxz.trim(),null);
						}
					}
				}
				nr_qlxz_zd = ReplaceEmptyByBlankOnString(nr_qlxz_zd);
				nr_qlxz_h = "构筑物";
				nr_qlxz = nr_qlxz_zd + "/" + nr_qlxz_h;
			}
		}
		if (StringHelper.isEmpty(nr_qlxz)) {
			if(BDCDYLX.SYQZD.equals(lx)||BDCDYLX.SHYQZD.equals(lx)){
				nr_qlxz=blank;
			}else{
				nr_qlxz = blank + "/" + blank;
			}
		}
		return nr_qlxz;
	}

	/*
	 * 获取土地用途
	 */
	public String GetTDYT(RealUnit unit, BDCDYLX lx, RealUnit landunit,
			List<TDYT> tdytlist, ProjectInfo info,String slsj) {
		String nr_yt = "";
		if (unit != null) {
			String nr_yt_zd = "";
			String nr_yt_h ="";
			if(BDCDYLX.SYQZD.equals(lx)) {
				//所有权宗地
				String ytClassName = "BDCS_TDYT" + unit.getLY().TableSuffix;
				String packageName = "com.supermap.realestate.registration.model.";
				Class<?> ytClass = null;
				try {
					ytClass = Class.forName(packageName + ytClassName);
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
				@SuppressWarnings("unchecked")
				List<TDYT> ytList = (List<TDYT>) baseCommonDao.getDataList(ytClass, "BDCDYID='" + unit.getId() + "' ORDER BY SFZYT DESC");
				List<String> tdytmclist = new ArrayList<String>();
				if(ytList!=null&&ytList.size()>0) {
					for (TDYT yt:ytList) {
						String ytname = yt.getTDYT();
						if (!StringHelper.isEmpty(yt)) {
							String tdytmc = ConstHelper.getNameByValue_new("TDYT",
									ytname.trim(),slsj);
							if (!StringHelper.isEmpty(tdytmc)
									&& !tdytmclist.contains(tdytmc)) {
								tdytmclist.add(tdytmc);
							}
						}
					}
					if (tdytmclist != null && tdytmclist.size() > 0) {
						nr_yt_zd = StringHelper.formatList(tdytmclist, "、");
					}
				}
				nr_yt = nr_yt_zd;
			}else if (BDCDYLX.SHYQZD.equals(lx)) {
				if (tdytlist != null && tdytlist.size() > 0) {
					List<String> tdytmclist = new ArrayList<String>();
					for (TDYT tdyt : tdytlist) {
						String yt = tdyt.getTDYT();
						if (!StringHelper.isEmpty(yt)) {
							String tdytmc = ConstHelper.getNameByValue_new("TDYT",
									yt.trim(),slsj);
							if (!StringHelper.isEmpty(tdytmc)
									&& !tdytmclist.contains(tdytmc)) {
								tdytmclist.add(tdytmc);
							}
						}
					}
					if (tdytmclist != null && tdytmclist.size() > 0) {
						nr_yt_zd = StringHelper.formatList(tdytmclist, "、");
					}
				}
				nr_yt = nr_yt_zd;
			} else if (BDCDYLX.H.equals(lx)) {
				House h = (House) unit;
				if (h != null) {
					String configZDTDYT = ConfigHelper
							.getNameByValue("GetZDTDYTFrom");
					if ("0".equals(configZDTDYT)) {// 从房屋土地用途获取
						String yt = h.getFWTDYT();
						if (!StringHelper.isEmpty(yt)) {
							nr_yt_zd = ConstHelper.getNameByValue_new("TDYT",
									yt.trim(),slsj);
						}
					} else if ("1".equals(configZDTDYT)) {// 关联宗地主用途
						if (landunit != null) {
							if (tdytlist != null && tdytlist.size() > 0) {
								String yt = tdytlist.get(0).getTDYT();
								if (!StringHelper.isEmpty(yt)) {
									nr_yt_zd = ConstHelper.getNameByValue_new(
											"TDYT", yt.trim(),slsj);
								}
							}
						}
					} else if ("2".equals(configZDTDYT)) {// 关联宗地所有用途（、分隔）
						if (landunit != null) {
							List<String> tdytmclist = new ArrayList<String>();
							for (TDYT tdyt : tdytlist) {
								String yt = tdyt.getTDYT();
								if (!StringHelper.isEmpty(yt)) {
									String tdytmc = ConstHelper.getNameByValue_new(
											"TDYT", yt.trim(),slsj);
									if (!StringHelper.isEmpty(tdytmc)
											&& !tdytmclist.contains(tdytmc)) {
										tdytmclist.add(tdytmc);
									}
								}
							}
							if (tdytmclist != null && tdytmclist.size() > 0) {
								nr_yt_zd = StringHelper.formatList(tdytmclist,
										"、");
							}
						}
					} else if ("3".equals(configZDTDYT)) {// 优先从房屋土地用途中获取，再从关联宗地主用途获取
						String yt = h.getFWTDYT();
						if (!StringHelper.isEmpty(yt)) {
							nr_yt_zd = ConstHelper.getNameByValue_new("TDYT",
									yt.trim(),slsj);
						}
						if (landunit != null && StringHelper.isEmpty(nr_yt_zd)) {
							if (tdytlist != null && tdytlist.size() > 0) {
								yt = tdytlist.get(0).getTDYT();
								if (!StringHelper.isEmpty(yt)) {
									nr_yt_zd = ConstHelper.getNameByValue_new(
											"TDYT", yt.trim(),slsj);
								}
							}
						}
					} else if ("4".equals(configZDTDYT)) {// 优先从房屋土地用途获取，再从关联宗地所有用途（、分隔）
						String yt = h.getFWTDYT();
						if (!StringHelper.isEmpty(yt)) {
							nr_yt_zd = ConstHelper.getNameByValue_new("TDYT",
									yt.trim(),slsj);
						}
						if (landunit != null && StringHelper.isEmpty(nr_yt_zd)) {
							List<String> tdytmclist = new ArrayList<String>();
							for (TDYT tdyt : tdytlist) {
								yt = tdyt.getTDYT();
								if (!StringHelper.isEmpty(yt)) {
									String tdytmc = ConstHelper.getNameByValue_new(
											"TDYT", yt.trim(),slsj);
									if (!StringHelper.isEmpty(tdytmc)
											&& !tdytmclist.contains(tdytmc)) {
										tdytmclist.add(tdytmc);
									}
								}
							}
							if (tdytmclist != null && tdytmclist.size() > 0) {
								nr_yt_zd = StringHelper.formatList(tdytmclist,
										"、");
							}
						}
					}
					nr_yt_h = h.getGHYTName();
				}
				nr_yt_zd = ReplaceEmptyByBlankOnString(StringUtils.substringBeforeLast(nr_yt_zd, "-"));
				nr_yt_h = ReplaceEmptyByBlankOnString(StringUtils.substringBeforeLast(nr_yt_h, "-"));

				nr_yt = nr_yt_zd + "/" + nr_yt_h;
				
				if(info!=null&&!StringHelper.isEmpty(info.getZdbtn())&&info.getZdbtn()){
					nr_yt = blank + "/" + nr_yt_h;
				}
				
			} else if (BDCDYLX.LD.equals(lx)) {
//				Forest forest = (Forest) unit;
//				String ldyt = "";
//				if (forest != null) {
//					ldyt = ConstHelper.getNameByValue_new("TDYT", forest.getTDYT(),slsj);
//				}
//				if (StringHelper.isEmpty(ldyt)) {
//
//					String djdy_sql = "select * from bdck.bdcs_djdy_gz where bdcdyh='"+unit.getBDCDYH()+"'";
//					if (!StringHelper.isEmpty(unit.getXMBH())) {
//						djdy_sql +=  " and xmbh='"+unit.getXMBH()+"'";
//					}
//					List<Map> djdys = baseCommonDao.getDataListByFullSql(djdy_sql);
//					List<BDCS_TDYT_XZ> lsttdyt = baseCommonDao.getDataList(BDCS_TDYT_XZ.class, "BDCDYID='" + djdys.get(0).get("BDCDYID") + "' ");
//					boolean flag = true;
//					String strtdyt = "";
//					if (!(lsttdyt.size() > 0)) {
//						CommonDao commonDao = SuperSpringContext.getContext().getBean(CommonDao.class);
//						String ytClassName = EntityTools.getEntityName("BDCS_TDYT", DJDYLY.DC);
//						Class<?> ytClass = EntityTools.getEntityClass(ytClassName);
//						List<TDYT> listyts = (List<TDYT>) commonDao.getDataList(ytClass, "BDCDYID='" + djdys.get(0).get("BDCDYID") + "'");
//						for (int i = 0; i < listyts.size(); i++) {
//							strtdyt += listyts.get(i).getTDYTMC();
//							if (i+1 < listyts.size()) {
//								strtdyt +="、";
//							}
//						}
//					}else {
//						for (TDYT tdyt : lsttdyt) {
//							if (flag) {
//								strtdyt = tdyt.getTDYTMC();
//								flag = false;
//							} else {
//								strtdyt = strtdyt + "、" + tdyt.getTDYTMC();
//							}
//						}
//					}
//					ldyt = strtdyt;
//				
//				}
//				ldyt = ReplaceEmptyByBlankOnString(ldyt);
//				nr_yt = ldyt;
				if (tdytlist != null && tdytlist.size() > 0) {
					List<String> tdytmclist = new ArrayList<String>();
					for (TDYT tdyt : tdytlist) {
						String yt = tdyt.getTDYT();
						if (!StringHelper.isEmpty(yt)) {
							String tdytmc = ConstHelper.getNameByValue_new("TDYT",
									yt.trim(),slsj);
							if (!StringHelper.isEmpty(tdytmc)
									&& !tdytmclist.contains(tdytmc)) {
								tdytmclist.add(tdytmc);
							}
						}
					}
					if (tdytmclist != null && tdytmclist.size() > 0) {
						nr_yt = StringHelper.formatList(tdytmclist, "、");
					}
				}
				nr_yt = ReplaceEmptyByBlankOnString(nr_yt);
			} else if (BDCDYLX.HY.equals(lx)) {
				Sea sea = (Sea) unit;
				String hyyt = "";
				String yhlxa = "";
				String yhlxb = "";
				if (sea != null) {
					yhlxa = ConstHelper.getNameByValue_new("HYSYLXA",
							sea.getYHLXA(),slsj);
					yhlxb = ConstHelper.getNameByValue_new("HYSYLXB",
							sea.getYHLXB(),slsj);
				}
				if (!StringHelper.isEmpty(yhlxa)) {
					hyyt = yhlxa;
				}
				if (!StringHelper.isEmpty(yhlxb)) {
					if (StringHelper.isEmpty(hyyt)) {
						hyyt = yhlxb;
					} else {
						hyyt = hyyt + "/" + yhlxb;
					}
				}
				nr_yt = hyyt;
			} else if (BDCDYLX.NYD.equals(lx)) {//农用地
				AgriculturalLand nyd = (AgriculturalLand) unit;
				String nydyt = "";
				if (nyd != null) {
					nydyt =  nyd.getYT();
				}
				if (!StringHelper.isEmpty(nydyt) &&( nydyt.indexOf("0")!=-1|| nydyt.toString().indexOf("1")!=-1)) {
					nydyt = ConstHelper.getNameByValue_new("TDYT",nydyt,slsj);
				}
				if (StringHelper.isEmpty(nydyt)) {
					String djdy_sql = "select * from bdck.bdcs_djdy_gz where bdcdyh='"+unit.getBDCDYH()+"'";
					if (!StringHelper.isEmpty(unit.getXMBH())) {
						djdy_sql +=  " and xmbh='"+unit.getXMBH()+"'";
					}
					List<Map> djdys = baseCommonDao.getDataListByFullSql(djdy_sql);
					List<BDCS_TDYT_XZ> lsttdyt = baseCommonDao.getDataList(BDCS_TDYT_XZ.class, "BDCDYID='" + djdys.get(0).get("BDCDYID") + "' ");
					boolean flag = true;
					String strtdyt = "";
					if (!(lsttdyt.size() > 0)) {
						CommonDao commonDao = SuperSpringContext.getContext().getBean(CommonDao.class);
						String ytClassName = EntityTools.getEntityName("BDCS_TDYT", DJDYLY.DC);
						Class<?> ytClass = EntityTools.getEntityClass(ytClassName);
						List<TDYT> listyts = (List<TDYT>) commonDao.getDataList(ytClass, "BDCDYID='" + djdys.get(0).get("BDCDYID") + "'");
						for (int i = 0; i < listyts.size(); i++) {
							strtdyt += listyts.get(i).getTDYTMC();
							if (i+1 < listyts.size()) {
								strtdyt +="、";
							}
						}
					}else {
						for (TDYT tdyt : lsttdyt) {
							if (flag) {
								strtdyt = tdyt.getTDYTMC();
								flag = false;
							} else {
								strtdyt = strtdyt + "、" + tdyt.getTDYTMC();
							}
						}
					}
					nydyt = strtdyt;
				}
				nydyt = ReplaceEmptyByBlankOnString(nydyt);
				nr_yt = nydyt;
			}else if (BDCDYLX.GZW.equals(lx)) {
				Structure gzw = (Structure) unit; 
				String gzwyt_zd = "";
				String gzwyt_gzw = "";
				if (gzw != null) {
					//gzwyt_zd =  ConstHelper.getNameByValue("TDYT",gzw.getGJZWGHYT());
					gzwyt_gzw ="构筑物";
					if (landunit != null) {
						if (tdytlist != null && tdytlist.size() > 0) {
							String yt = tdytlist.get(0).getTDYT();
							if (!StringHelper.isEmpty(yt)) {
								gzwyt_zd = ConstHelper.getNameByValue_new(
										"TDYT", yt.trim(),slsj);
							}
						}
					}
					if (!StringHelper.isEmpty(gzw.getGJZWGHYT()) && !"null".equals(gzw.getGJZWGHYT())) {
						gzwyt_gzw =  ConstHelper.getNameByValue_new("FWYT",gzw.getGJZWGHYT().trim(),slsj);
					}
				}
				gzwyt_zd = ReplaceEmptyByBlankOnString(gzwyt_zd);
				gzwyt_gzw = ReplaceEmptyByBlankOnString(gzwyt_gzw);
				nr_yt = gzwyt_zd + "/" + gzwyt_gzw;
			}
		}
		if (StringHelper.isEmpty(nr_yt)) {
			if(BDCDYLX.SYQZD.equals(lx)||BDCDYLX.SHYQZD.equals(lx)){
				nr_yt = blank;
			}else{
				nr_yt = blank + "/" + blank;
			}
		}
		return nr_yt;
	}

	/*
	 * 获取面积
	 */
	public String GetMJ(RealUnit unit, BDCDYLX lx, RealUnit landunit, Rights ql, ProjectInfo info,WFD_MAPPING mapping) {
		String pfm = "㎡";
		String qhdm = ConfigHelper.getNameByValue("XZQHDM");
		if(qhdm.indexOf("450721") == 0 ){//广西灵山
			pfm = "平方米";
		}
		String nr_mj = "共有宗地面积:" + blank + pfm + "/房屋建筑面积:" + blank + pfm;		
		String XZQHDM=ConfigHelper.getNameByValue("XZQHDM");
		if (unit != null) {
			if (BDCDYLX.SHYQZD.equals(lx) || BDCDYLX.SYQZD.equals(lx)) {
				nr_mj = ReplaceEmptyByBlankOnDouble(unit.getMJ()) + pfm;
			} else if (BDCDYLX.H.equals(lx)) {
				House h = (House) unit;
				String mj_zd = "";
				String mj_h = "";
				String temp_zdname ="";
				if(XZQHDM.indexOf("45") == 0 && !XZQHDM.contains("45020")&& !XZQHDM.contains("450221")&& !XZQHDM.contains("450226")){//广西需求(柳州除外)，分摊或独有土地面积>0时，显示“宗地面积”4个字
					if (!StringHelper.isEmpty(h.getFTTDMJ())&& h.getFTTDMJ()!=0) {
						if (h.getFTTDMJ()>0 ){ 
							nr_mj = "宗地面积：" ;
						}
					}else if (!StringHelper.isEmpty(h.getDYTDMJ())&& h.getDYTDMJ()!=0) {
						if (h.getDYTDMJ()>0) {
							nr_mj = "宗地面积：" ;
						}	
					}else {
						nr_mj = "共有宗地面积：" ;
					}
				}else{
					nr_mj = "共有宗地面积：" ;
				}
				
				if(XZQHDM.contains("451102")){
					nr_mj = "宗地面积：" ;
				}
				
				exchange = nr_mj; 
				temp_zdname = nr_mj; 
				
				if (h != null) {
					if (landunit != null) {
						mj_zd = ReplaceEmptyByBlankOnDouble(landunit.getMJ())+ pfm;
						if(info!=null&&!StringHelper.isEmpty(info.getZdbtn())&&info.getZdbtn()){
							exchange += blank;   //----
						}else
							exchange += mj_zd;   //(共有)宗地面积：tt㎡
					}
					mj_h = ReplaceEmptyByBlankOnDouble(unit.getMJ()) + pfm;
				}
				
				if ("0".equals(ConfigHelper.getNameByValue("EXCHANGE"))) {
					
					nr_mj =  exchange + "/" + "房屋建筑面积 ：" + mj_h;  //(共有)宗地面积：tt(----)㎡/房屋建筑面积 tt㎡
				
					if ("1".equals(ConfigHelper.getNameByValue("mjpz"))){//证书面积取宗地面积或权利面积控制
						List<BDCS_QLR_GZ> qlrs = null;  
						qlrs = baseCommonDao.getDataList(BDCS_QLR_GZ.class, " QLID='"+ql.getId()+"'");
						BDCS_QLR_GZ qlr = null;
						if(qlrs!=null && qlrs.size()>0){							
							Double sumqlmj = 0.0;
							for (BDCS_QLR_GZ qlrtmp : qlrs) {
								sumqlmj += (qlrtmp.getQLMJ() != null ? qlrtmp.getQLMJ() : 0);
							}	
							nr_mj = temp_zdname + ReplaceEmptyByBlankOnDouble(sumqlmj) + pfm+"/房屋建筑面积 ：" + mj_h;
							if(info!=null && !StringHelper.isEmpty(info.getZdbtn()) && info.getZdbtn()){
								nr_mj = exchange + "/" + "房屋建筑面积 ：" + mj_h;  
							}
						}	
					}	
					//是否将面积中的共用宗地面积改为土地使用权面积，并将共用宗地面积显示在权利其他状况里---赤壁
					if ("1".equals(ConfigHelper.getNameByValue("ISSHOW_TDSYQMJ"))){
						Double fttdmj = 0.0;
						Double dytdmj = 0.0;
						if (h.getFTTDMJ() != null && h.getFTTDMJ() > 0) {
							fttdmj = h.getFTTDMJ();
						}
						if (h.getDYTDMJ() != null && h.getDYTDMJ() > 0) {
							dytdmj = h.getDYTDMJ();
						}	
						String tdsyqmj = "";
						if (fttdmj > 0 && dytdmj == 0) {
							tdsyqmj = "土地使用权面积:" + ReplaceEmptyByBlankOnDouble(fttdmj) + pfm;
							if(info!=null&&!StringHelper.isEmpty(info.getZdbtn())&&info.getZdbtn()){
								tdsyqmj = "土地使用权面积:" + blank +  pfm;
							}
						} else if (fttdmj == 0 && dytdmj > 0) {
							tdsyqmj = "土地使用权面积:" + ReplaceEmptyByBlankOnDouble(dytdmj) + pfm;
							if(info!=null&&!StringHelper.isEmpty(info.getZdbtn())&&info.getZdbtn()){
								tdsyqmj = "土地使用权面积:" + blank + pfm;
							}
						} else if (fttdmj > 0 && dytdmj > 0) {
							tdsyqmj = "土地使用权面积:" + ReplaceEmptyByBlankOnDouble(fttdmj + dytdmj)+ pfm;
							if(info!=null&&!StringHelper.isEmpty(info.getZdbtn())&&info.getZdbtn()){
								tdsyqmj = "土地使用权面积:" + blank + pfm;
							}
						}	
						if (!StringHelper.isEmpty(tdsyqmj)) {
							nr_mj = tdsyqmj + "/房屋建筑面积:" + mj_h ;											
						}
					}
				} else {
					Double fttdmj = 0.0;
					if (h.getFTTDMJ() != null && h.getFTTDMJ() > 0) {
						fttdmj = h.getFTTDMJ();
					}
					if (fttdmj > 0)// 只用分摊土地面积
					{
						String strfttdmj = ReplaceEmptyByBlankOnDouble(fttdmj)
								+ pfm+"/";
						nr_mj = "分摊土地使用权面积：" + strfttdmj + "房屋建筑面积 ：" + mj_h;
						if(info!=null&&!StringHelper.isEmpty(info.getZdbtn())&&info.getZdbtn()){
							nr_mj = "分摊土地使用权面积：" + blank + "/房屋建筑面积 ：" + mj_h;
						}
					} else {
						nr_mj = "房屋建筑面积 ：" + mj_h;
					}
				}
				
				if(info!=null&&!StringHelper.isEmpty(info.getZdbtn())&&info.getZdbtn()){
					exchange = temp_zdname + blank;   //(共有)宗地面积：tt㎡
				}
			} else if (BDCDYLX.LD.equals(lx)) {
				if (!StringHelper.isEmpty(unit.getMJ())) {
					String ldmj = ReplaceEmptyByBlankOnDouble(unit.getMJ());
					nr_mj = ldmj + "亩";
				} else {
					nr_mj = blank + "亩";
				}
			} else if (BDCDYLX.HY.equals(lx)) {				
				nr_mj = ReplaceEmptyByBlankOnDoubleForZH(unit.getMJ()) + "公顷";
			} else if (BDCDYLX.NYD.equals(lx)) {
//				nr_mj = "承包（使用权）面积：" + blank + "亩";
				nr_mj = blank + "亩";
				if (!StringHelper.isEmpty(unit.getMJ()))
//					nr_mj = "承包（使用权）面积：" + unit.getMJ() + "亩";
				nr_mj = unit.getMJ() + "亩";
			} else if (BDCDYLX.GZW.equals(lx)) {
				Structure gzw = (Structure) unit; 
				String mj_zd = "";
				String mj_gwz = "";
				String mj_dw = "";
				if (gzw != null) {
					mj_dw = gzw.getMJDW();					
					mj_zd = ReplaceEmptyByBlankOnDouble(gzw.getTDHYSYMJ());
					mj_gwz = ReplaceEmptyByBlankOnDouble(gzw.getGJZWMJ());
				}
				if (StringHelper.isEmpty(mj_dw)) {
					mj_dw = pfm;
				}
				
				if(XZQHDM.contains("451102")){
					mj_zd = "宗地面积:" + mj_zd;
				}else{
					mj_zd = "共有宗地面积:" + mj_zd;
				}
				mj_gwz = "建筑面积:" + mj_gwz;
				nr_mj = mj_zd + mj_dw + "/" +mj_gwz + mj_dw;
			}
			
			// 依据workflowcode
			if (("1").equals(mapping.getSFADDPZMJ())) {
				String pzmj = GetPZMJ(unit);
				if (!StringHelper.isEmpty(pzmj)) {
					nr_mj += space + space + pzmj;
				}
			}
		}
		return nr_mj;
	}

	/*
	 * 获取使用期限
	 */
	public String GetSYQX(RealUnit unit, Rights ql, RealUnit landunit,
			List<TDYT> tdytlist, ProjectInfo info) {
		String nr_syqx = "";
		if (unit != null && (BDCDYLX.SHYQZD.equals(unit.getBDCDYLX()))) {
			if (tdytlist != null && tdytlist.size() > 0) {
				if (tdytlist.size() == 1) {
					String qssj = ReplaceEmptyByBlankOnDate(tdytlist.get(0)
							.getQSRQ()) + "起";
					String jssj = ReplaceEmptyByBlankOnDate(tdytlist.get(0)
							.getZZRQ()) + "止";

					nr_syqx = qssj + jssj;
				} else {
					int iii = 0;
					for (TDYT tdyt : tdytlist) {
						iii++;
						if (!StringHelper.isEmpty(tdyt.getTDYTName())) {
							String tdytname[] = null;//土地用途新标准去掉数字的key的显示
							if(tdyt.getTDYTName().contains("-")){
								tdytname = tdyt.getTDYTName().split("\\-");
							}
							if (!StringHelper.isEmpty(tdytname)){
								nr_syqx = nr_syqx + tdytname[0] + ":";
							}else{
								nr_syqx = nr_syqx + tdyt.getTDYTName() + ":";
							}
						} else {
							continue;
						}
						String qssj = ReplaceEmptyByBlankOnDate(tdyt.getQSRQ())
								+ "起";
						String jssj = ReplaceEmptyByBlankOnDate(tdyt.getZZRQ())
								+ "止";

						if (iii == tdytlist.size()) {
							nr_syqx = nr_syqx + qssj + jssj;
						} else {
							nr_syqx = nr_syqx + qssj + jssj + hhf;
						}
					}
				}
			}
		} else if (unit != null && (BDCDYLX.H.equals(unit.getBDCDYLX()))) {
			String qssj = ReplaceEmptyByBlankOnDate(ql.getQLQSSJ()) + "起";
			String jssj = ReplaceEmptyByBlankOnDate(ql.getQLJSSJ()) + "止";
			nr_syqx = qssj + jssj;
			if(info!=null&&!StringHelper.isEmpty(info.getZdbtn())&&info.getZdbtn()){
				nr_syqx = blank +"起"+ blank+"止";
			}
		} else if (unit != null && (BDCDYLX.LD.equals(unit.getBDCDYLX()))) {
			String qllxname = ReplaceEmptyByBlankOnString(QLLX.initFrom(ql
					.getQLLX()).Name);
			if (QLLX.TDCBJYQ_SLLMSYQ.Value.equals(ql.getQLLX())) {
				qllxname = "土地承包经营权";
			}
			if (QLLX.LDSYQ_SLLMSYQ.Value.equals(ql.getQLLX())) {
				qllxname = "林地使用权";
			}
			String qssj = ReplaceEmptyByBlankOnDate(ql.getQLQSSJ()) + "起";
			String jssj = ReplaceEmptyByBlankOnDate(ql.getQLJSSJ()) + "止";
			nr_syqx = qllxname + space + qssj + jssj;
		} else if (unit != null && (BDCDYLX.HY.equals(unit.getBDCDYLX()))) {
			String qssj = ReplaceEmptyByBlankOnDate(ql.getQLQSSJ()) + "起";
			String jssj = ReplaceEmptyByBlankOnDate(ql.getQLJSSJ()) + "止";
			nr_syqx = qssj + jssj;
		} else if (unit != null && (BDCDYLX.NYD.equals(unit.getBDCDYLX()))) {
			String qssj = ReplaceEmptyByBlankOnDate(ql.getQLQSSJ()) + "起";
			String jssj = ReplaceEmptyByBlankOnDate(ql.getQLJSSJ()) + "止";
			String XZQHDM=ConfigHelper.getNameByValue("XZQHDM");
			if(XZQHDM.indexOf("450") == 0 ){
				List<Map> cbsjs = baseCommonDao.getDataListByFullSql("SELECT CBQSSJ,CBJSSJ FROM BDCK.BDCS_NYD_GZ WHERE BDCDYH='"+unit.getBDCDYH()+"'");
				String cbqssj ="";
				String cbjssj ="";
				if (cbsjs.size() > 0) {
					cbqssj = ReplaceEmptyByBlankOnDate((Date)cbsjs.get(0).get("CBQSSJ")) + "起";
					cbjssj = ReplaceEmptyByBlankOnDate((Date)cbsjs.get(0).get("CBJSSJ")) + "止";
				}
				nr_syqx = cbqssj + cbjssj;
			}else{
				nr_syqx = qssj + jssj;
			}
		}else if (unit != null && BDCDYLX.GZW.equals(unit.getBDCDYLX())) {
			String qssj = ReplaceEmptyByBlankOnDate(ql.getQLQSSJ()) + "起";
			String jssj = ReplaceEmptyByBlankOnDate(ql.getQLJSSJ()) + "止";
			nr_syqx = qssj + jssj;
//			if(info!=null && !StringHelper.isEmpty(info.getZdbtn())&&info.getZdbtn()){
//				nr_syqx = blank +"起"+ blank+"止";
//			}
		}
		if (StringHelper.isEmpty(nr_syqx)) {
			nr_syqx = blank + "起" + blank + "止";
		}
		if ("1".equals(ConfigHelper.getNameByValue("LXAndSYQX"))) {
			if (BDCDYLX.H.equals(unit.getBDCDYLX())) {
				StringBuffer stringBuffer = new StringBuffer(nr_syqx);
				String nr_qllx = blank;
				if ("4".equals(ql.getQLLX())) {
					nr_qllx = ConstHelper.getNameByValue_new("QLLX", "3",StringHelper.formatObject(info.getSlsj()));
				}
				if ("6".equals(ql.getQLLX())) {
					nr_qllx = ConstHelper.getNameByValue_new("QLLX", "5",StringHelper.formatObject(info.getSlsj()));
				}
				if ("8".equals(ql.getQLLX())) {
					nr_qllx = ConstHelper.getNameByValue_new("QLLX", "7",StringHelper.formatObject(info.getSlsj()));
				}
				nr_syqx = stringBuffer.insert(0, nr_qllx + " ").toString();
			}
		}
		return nr_syqx;
	}

	/*
	 * 获取权利其他状况
	 */
	public String GetQT(RealUnit unit, List<RightsHolder> qlrlist, Rights ql,
			SubRights fsql, BDCS_XMXX xmxx, ProjectInfo info,WFD_MAPPING mapping,List<TDYT> list_tdyt) {
		String pfm = "㎡";
		String qhdm = ConfigHelper.getNameByValue("XZQHDM");
		if(qhdm.indexOf("450721") == 0 ){//广西灵山
			pfm = "平方米";
		}
		String XZQHDM=ConfigHelper.getNameByValue("XZQHDM");
		String nr_qt = "";
		StringBuilder builderQT = new StringBuilder();
		if (unit != null && BDCDYLX.H.equals(unit.getBDCDYLX())) {
			House h = (House) unit;
			if (h != null) {
				if ("1".equals(ConfigHelper.getNameByValue("SFXSFH"))) {
					String nr_qt_fh = h.getFH();
					if (!StringHelper.isEmpty(nr_qt_fh)) {
						builderQT.append("房号：" + nr_qt_fh).append(hhf);
					} else {
						nr_qt_fh = blank;
						builderQT.append("房号：" + nr_qt_fh).append(hhf);
					}
				}
				if(XZQHDM.contains("451102")){
					// 其他状况-共有土地面积 luml
					Double gytdmj=h.getGYTDMJ();
					if(gytdmj==null){
						gytdmj=0.0;
					}
					
					if ("1".equals(ConfigHelper.getNameByValue("ZSPARAM_showGYTDMJ"))) {
						if (gytdmj> 0) {
							builderQT.append("共有土地面积：" +gytdmj+pfm).append(hhf);
						}else {
							if(XZQHDM.contains("451102")){
								builderQT.append("").append(hhf);
							}else{
								builderQT.append("共有土地面积：--- " +pfm).append(hhf);
							}
						}
					}
					
					// NR_11_1,其他状况-土地使用权面积
					String nr_qt_tdsyqmj = GetTDSYQMJ(h,info);
					if (!StringHelper.isEmpty(nr_qt_tdsyqmj)) {
						builderQT.append(nr_qt_tdsyqmj).append(hhf);
					}
				}else{
					// NR_11_1,其他状况-土地使用权面积
					String nr_qt_tdsyqmj = GetTDSYQMJ(h,info);
					if (!StringHelper.isEmpty(nr_qt_tdsyqmj)) {
						builderQT.append(nr_qt_tdsyqmj).append(hhf);
					}
					// 其他状况-共有土地面积 luml
					Double gytdmj=h.getGYTDMJ();
					if(gytdmj==null){
						gytdmj=0.0;
					}
					
					if ("1".equals(ConfigHelper.getNameByValue("ZSPARAM_showGYTDMJ"))) {
						if (gytdmj> 0) {
							builderQT.append("共有土地面积：" +gytdmj+pfm).append(hhf);
						}else {
							if(XZQHDM.contains("451102")){
								builderQT.append("").append(hhf);
							}else{
								builderQT.append("共有土地面积：--- " +pfm).append(hhf);
							}
						}
					}
				}
				
				
				//专有建筑面积和分摊建筑面积为0时，不显示
				if (!StringHelper.isEmpty(h.getSCTNJZMJ())) {
					if (h.getSCTNJZMJ() != 0) {
						// NR_11_2,其他状况-专有建筑面积
						String nr_qt_zyjzmj = GetZYJZMJ(h);
						builderQT.append(nr_qt_zyjzmj + hhf);
					}
				}
				if (!StringHelper.isEmpty(h.getSCFTJZMJ())) {
					if (h.getSCFTJZMJ() != 0) {
						// NR_11_3,其他状况-分摊建筑面积
						String nr_qt_ftjzmj = GetFTJZMJ(h);
						builderQT.append(nr_qt_ftjzmj + hhf);
					}
				}
				
//				// NR_11_2,其他状况-专有建筑面积
//				String nr_qt_zyjzmj = GetZYJZMJ(h);
//				// NR_11_3,其他状况-分摊建筑面积
//				String nr_qt_ftjzmj = GetFTJZMJ(h);
//				builderQT.append(nr_qt_zyjzmj + "," + nr_qt_ftjzmj + hhf);

				// NR_11_4,其他状况-房屋结构
				String nr_qt_fwjg = GetFWJG(h);
				if (!StringHelper.isEmpty(nr_qt_fwjg)) {
					builderQT.append(nr_qt_fwjg);
					String nr_qt_fwjg1 = GetFWJG1(h);
					String nr_qt_fwjg2 = GetFWJG2(h);
					String nr_qt_fwjg3 = GetFWJG3(h);
					if (!StringHelper.isEmpty(nr_qt_fwjg1)) {
						builderQT.append("、").append(nr_qt_fwjg1);
						if (!StringHelper.isEmpty(nr_qt_fwjg2)) {
							builderQT.append("、");
							builderQT.append(nr_qt_fwjg2);
							if (!StringHelper.isEmpty(nr_qt_fwjg3)) {
								builderQT.append("、");
								builderQT.append(nr_qt_fwjg3);
							}
						}
					}
					builderQT.append(hhf);
				}
				
				// NR_11_5,其他状况-房屋总层数
				String nr_qt_fwzcs = GetFWZCS(h);
				// NR_11_6,其他状况-房屋所在层
				String nr_qt_fwszc = GetFWSZC(h);
				builderQT.append(nr_qt_fwzcs + nr_qt_fwszc).append(hhf);

				// NR_11_7,其他状况-房屋竣工时间
				String nr_qt_fwjgsj = GetFWJGSJ(h);
				if (!StringHelper.isEmpty(nr_qt_fwjgsj)) {
					builderQT.append(nr_qt_fwjgsj).append(hhf);
				}
				
				// 其他状况-建筑密度和容积率liangc
				String configShowJZMDandRJL = ConfigHelper
						.getNameByValue("ShowJZMDandRJL");
				if ("1".equals(configShowJZMDandRJL)) {
					if (unit != null && (BDCDYLX.H.equals(unit.getBDCDYLX()))) {
						List<BDCS_SHYQZD_XZ> list_shyqzd_xzs = baseCommonDao
								.getDataList(BDCS_SHYQZD_XZ.class, "BDCDYID='"
										+ h.getZDBDCDYID() + "'");
						String jzmd = "";
						String rjl = "";
						String nr_qt_jzmd_and_rjl = "";
						if (list_shyqzd_xzs != null
								&& list_shyqzd_xzs.size() > 0) {
							for (BDCS_SHYQZD_XZ list_shyqzd_xz : list_shyqzd_xzs) {
								jzmd = FormatByDatatype(list_shyqzd_xz
												.getJZMD());
								rjl = FormatByDatatype(list_shyqzd_xz
												.getRJL());
							}
							nr_qt_jzmd_and_rjl = "建筑密度:" + jzmd
									+ "&nbsp;&nbsp;&nbsp;" + "容积率:" + rjl;
						}
						
						builderQT.append(nr_qt_jzmd_and_rjl).append(hhf);
					}
				}
			}
		} else if (unit != null && (BDCDYLX.LD.equals(unit.getBDCDYLX()))) {
			Forest forest = (Forest) unit;
			if (forest != null) {
				if (QLLX.TDCBJYQ_SLLMSYQ.Value.equals(ql.getQLLX())) {
					// NR_11_10,其他状况-发包方
					String nr_qt_fbf = "";
					// NR_11_11,其他状况-共有人情况
					String nr_qt_gyrqk = "";
					if (fsql != null) {
						nr_qt_fbf = ReplaceEmptyByBlankOnString(fsql.getFBF());
						nr_qt_gyrqk = ReplaceEmptyByBlankOnString(fsql.getGYRQK());
					}
					if (!StringHelper.isEmpty(nr_qt_fbf)) {
						builderQT.append("发包方：").append(nr_qt_fbf).append(hhf);
					}
					if (!StringHelper.isEmpty(nr_qt_gyrqk)) {
						builderQT.append("共有人情况：").append(nr_qt_gyrqk).append(hhf);
					}
				}
				// NR_11_12,其他状况-主要树种
				String strzysz = "";
				String nr_qt_zysz ="";
				if (StringHelper.isEmpty(forest.getZYSZ())) {
					List<BDCS_TDYT_GZ> lsttdyt = baseCommonDao.getDataList(BDCS_TDYT_GZ.class,
							"BDCDYID='" + forest.getId() + "' ");
					boolean flag = true;
					for (TDYT tdyt : lsttdyt) {
						if (!StringHelper.isEmpty(tdyt.getZYSZ())) {
							if (flag ) {
								strzysz = tdyt.getZYSZ();
								flag = false;
							} else {
								strzysz = strzysz + "、" + tdyt.getZYSZ();
							}
						}
					}
					nr_qt_zysz = ReplaceEmptyByBlankOnString(strzysz);
				}else{
					nr_qt_zysz = ReplaceEmptyByBlankOnString(forest.getZYSZ());
				}
				if (!StringHelper.isEmpty(nr_qt_zysz)) {
					builderQT.append("主要树种：").append(nr_qt_zysz).append(hhf);
				}
				// NR_11_13,其他状况-造林年度
				String nr_qt_zlnd = ReplaceEmptyByBlankOnInteger(forest.getZLND());
				if (!StringHelper.isEmpty(nr_qt_zlnd)) {
					builderQT.append("造林年度：").append(nr_qt_zlnd).append(hhf);
				}
				// NR_11_13,其他状况-小地名
				String nr_qt_xdm = ReplaceEmptyByBlankOnString(forest.getXDM());
				if (!StringHelper.isEmpty(nr_qt_xdm)) {
					builderQT.append("小地名：").append(nr_qt_xdm).append(hhf);
				}
				//其他状况-森林、林木使用权人，林班，小班三项				
				if (QLLX.TDCBJYQ_SLLMSYQ.Value.equals(ql.getQLLX()) || QLLX.LDSYQ.Value.equals(ql.getQLLX())
						|| QLLX.LDSYQ_SLLMSYQ.Value.equals(ql.getQLLX()) || QLLX.LDSHYQ_SLLMSYQ.Value.equals(ql.getQLLX()) ) {
					String nr_qt_shyqr ="";
					if (fsql != null) {
						nr_qt_shyqr = ReplaceEmptyByBlankOnString(fsql.getSLLMSYQR2());
					}
					String nr_qt_lb = ReplaceEmptyByBlankOnString(forest.getLB());
					String nr_qt_xb = ReplaceEmptyByBlankOnString(forest.getXB());
					String nr_qt_zs = ReplaceEmptyByBlankOnInteger(forest.getZS());
					builderQT.append("森林、林木使用权人：").append(nr_qt_shyqr).append(hhf);
					builderQT.append("林班：").append(nr_qt_lb).append(hhf);
					builderQT.append("小班：").append(nr_qt_xb).append(hhf);
					builderQT.append("株数：").append(nr_qt_zs).append(hhf);
				}
			}
		} else if (unit != null && (BDCDYLX.HY.equals(unit.getBDCDYLX()))) {
			Sea sea = (Sea) unit;
			if (sea != null) {
				// NR_11_14,其他状况-项目名称
				String nr_qt_xmmc = "";
				// NR_11_15,其他状况-项目性质
				String nr_qt_xmxz = "";
				nr_qt_xmmc = ReplaceEmptyByBlankOnString(sea.getXMMC());
				nr_qt_xmxz = ReplaceEmptyByBlankOnString(ConstHelper.getNameByValue_new("XMXZ", sea.getXMXX(),null));
				builderQT.append("项目名称：").append(nr_qt_xmmc).append(hhf);
				builderQT.append("项目性质：").append(nr_qt_xmxz).append(hhf);
				// NR_11_16,其他状况-用海状况
				StringBuilder builder_yhzk = new StringBuilder();
				if (DJDYLY.GZ.equals(sea.getLY())) {
					List<BDCS_YHZK_GZ> list_yhzk = baseCommonDao
							.getDataList(BDCS_YHZK_GZ.class,"BDCDYID='" + sea.getId() + "'");
					if (list_yhzk != null && list_yhzk.size() > 0) {
						/*   2016-11-10  luml 修改      */
						builder_yhzk.append("用海方式：");
						String sp="";
						for (BDCS_YHZK_GZ yhzk : list_yhzk) {
							builder_yhzk.append(sp).append(ReplaceEmptyByBlankOnString(yhzk.getYHFSName()));
							sp="/";
						}
						builder_yhzk.append(hhf);
						sp="";
						builder_yhzk.append("用海面积：");
						for(BDCS_YHZK_GZ yhzk : list_yhzk){
							builder_yhzk.append(sp).append(ReplaceEmptyByBlankOnDoubleForZH(yhzk.getYHMJ())+"公顷");
							sp="/";
						}
						builder_yhzk.append(hhf);
					}
				} else {
					List<BDCS_YHZK_LS> list_yhzk = baseCommonDao.getDataList(BDCS_YHZK_LS.class,
									"BDCDYID='" + sea.getId() + "'");
					if (list_yhzk != null && list_yhzk.size() > 0) {
						/*   2016-11-10  luml 修改      */
						builder_yhzk.append("用海方式：");
						String sp="";
						for (BDCS_YHZK_LS yhzk : list_yhzk) {
							builder_yhzk.append(sp).append(ReplaceEmptyByBlankOnString(yhzk.getYHFSName()));
							sp="/";
						}
						builder_yhzk.append(hhf);
						sp="";
						builder_yhzk.append("用海面积：");
						for(BDCS_YHZK_LS yhzk : list_yhzk){
							builder_yhzk.append(sp).append(ReplaceEmptyByBlankOnDoubleForZH(yhzk.getYHMJ())+"公顷");
							sp="/";
						}
						builder_yhzk.append(hhf);
					}
				}
				builderQT.append(builder_yhzk);
			}
		}else if(unit != null && (BDCDYLX.SHYQZD.equals(unit.getBDCDYLX()))){
			if("1".equals(mapping.getCERTMODE())){
				String str_mj="";
				HashMap<String,Double> qlxzinfo=new HashMap<String, Double>();
				for(TDYT tdyt:list_tdyt){
					String qlxz=tdyt.getQLXZName();
					if(StringHelper.isEmpty(qlxz)){
						continue;
					}
					Double tdmj=tdyt.getTDMJ();
					if(tdmj==null){
						tdmj=0.0;
					}
					if(qlxzinfo.containsKey(qlxz)){
						Double _tdmj=qlxzinfo.get(qlxz);
						_tdmj=_tdmj+tdmj;
						qlxzinfo.put(qlxz, _tdmj);
					}else{
						qlxzinfo.put(qlxz, tdmj);
					}
				}
				Iterator<Map.Entry<String, Double>> entries = qlxzinfo.entrySet().iterator();
				while (entries.hasNext()) {
					Map.Entry<String, Double> entry=entries.next();
					String qlxz=entry.getKey();
					Double tdmj=entry.getValue();
					if(!StringHelper.isEmpty(str_mj)){
						str_mj=str_mj+"、";
					}
					str_mj=str_mj+qlxz+":"+ReplaceEmptyByBlankOnDouble(tdmj)+pfm;
				}
				if(StringHelper.isEmpty(str_mj)){
					str_mj=blank+ pfm;
				}
				builderQT.append(str_mj);
				builderQT.append(hhf);
			}
			String nr_qt_ql_gyrqk = GetGYRQK(ql);
			if (!StringHelper.isEmpty(nr_qt_ql_gyrqk)) {
				String qxdm_ = ConfigHelper.getNameByValue("XZQHDM");
				if(qxdm_.contains("451102")){
					builderQT.append(nr_qt_ql_gyrqk).append(hhf);
				}
			}
		}
		
		// NR_11_8,其他状况-持证方式
		String nr_qt_czfs = GetCZFS(qlrlist, ql);
		if (!StringHelper.isEmpty(nr_qt_czfs)) {
			builderQT.append(nr_qt_czfs).append(hhf);
		}
		// NR_11_9,其他状况-持证人
		String nr_qt_czr = GetCZR(qlrlist, ql);
		if (!StringHelper.isEmpty(nr_qt_czr)) {
			builderQT.append(nr_qt_czr).append(hhf);
		}
		// 原产权证号
		if ("1".equals(ConfigHelper.getNameByValue("ISSHOW_BDCQZH_INZS"))) {
			if (!StringHelper.isEmpty(ql.getLYQLID())) {
				Rights qlxz = RightsTools.loadRights(DJDYLY.XZ, ql.getLYQLID());
				if (qlxz != null) {
					if (!StringHelper.isEmpty(qlxz.getBDCQZH())) {
						String nr_qt_ycqzh = qlxz.getBDCQZH();
						builderQT.append("原产权证号：").append(nr_qt_ycqzh)
								.append(hhf);
					}
				} else {
					Rights qlls = RightsTools.loadRights(DJDYLY.LS,
							ql.getLYQLID());
					if (qlls != null) {
						if (!StringHelper.isEmpty(qlls.getBDCQZH())) {
							String nr_qt_ycqzh = qlls.getBDCQZH();
							builderQT.append("原产权证号：").append(nr_qt_ycqzh)
									.append(hhf);
						}
					}
				}
			}
		}
		
		nr_qt = builderQT.toString();
		return nr_qt;
	}

	/*
	 * 土地使用权面积
	 */
	private String GetTDSYQMJ(House h, ProjectInfo info) {
		String pfm = "㎡";
		String qhdm = ConfigHelper.getNameByValue("XZQHDM");
		if(qhdm.indexOf("450721") == 0 ){//广西灵山
			pfm = "平方米";
		}
		StringBuilder buildershyqmj = new StringBuilder();
		Double fttdmj = 0.0;
		Double dytdmj = 0.0;
		if (h.getFTTDMJ() != null && h.getFTTDMJ() > 0) {
			fttdmj = h.getFTTDMJ();
		}
		if (h.getDYTDMJ() != null && h.getDYTDMJ() > 0) {
			dytdmj = h.getDYTDMJ();
		}
		if (fttdmj > 0 && dytdmj == 0)// 只用分摊土地面积
		{
			String strfttdmj = ReplaceEmptyByBlankOnDouble(fttdmj);
			if ("0".equals(ConfigHelper.getNameByValue("EXCHANGE"))) {
				if ("1".equals(ConfigHelper.getNameByValue("ISSHOW_TDSYQMJ"))){
					buildershyqmj.append(exchange + hhf);				
				}
				if(info!=null&&!StringHelper.isEmpty(info.getZdbtn())&&info.getZdbtn()){
					buildershyqmj.append("分摊土地使用权面积：").append(blank);
				}else{
					buildershyqmj.append("分摊土地使用权面积：").append(strfttdmj).append(pfm);
				}
			} else {
				if (!StringHelper.isEmpty(exchange)) {
					buildershyqmj.append(exchange);
				}
			}
		} else if (fttdmj == 0 && dytdmj > 0)// 只有独用土地面积
		{
			String strdytdmj = ReplaceEmptyByBlankOnDouble(dytdmj);
			if ("0".equals(ConfigHelper.getNameByValue("EXCHANGE"))) {
				if ("1".equals(ConfigHelper.getNameByValue("ISSHOW_TDSYQMJ"))){
					buildershyqmj.append(exchange + hhf);
				}
				if(info!=null&&!StringHelper.isEmpty(info.getZdbtn())&&info.getZdbtn()){
					buildershyqmj.append("独用土地使用权面积：").append(blank);
				}else{
					buildershyqmj.append("独用土地使用权面积：").append(strdytdmj).append(pfm);
				}
			} else {
				if (!StringHelper.isEmpty(exchange)) {
					buildershyqmj.append(exchange).append(",");
				}
				if(info!=null&&!StringHelper.isEmpty(info.getZdbtn())&&info.getZdbtn()){
					buildershyqmj.append("独用土地使用权面积：").append(blank);
				}else{
					buildershyqmj.append("独用土地使用权面积：").append(strdytdmj) .append(pfm);
				}
			}
		} else if (fttdmj > 0 && dytdmj > 0)// 分摊跟独用土地面积都有
		{
			String strfttdmj = ReplaceEmptyByBlankOnDouble(fttdmj);
			if ("0".equals(ConfigHelper.getNameByValue("EXCHANGE"))) {
				if ("1".equals(ConfigHelper.getNameByValue("ISSHOW_TDSYQMJ"))){
					buildershyqmj.append(exchange + hhf);
				}
				if(info!=null&&!StringHelper.isEmpty(info.getZdbtn())&&info.getZdbtn()){
					buildershyqmj.append("分摊土地使用权面积：").append(blank).append(",");
				}else{
					buildershyqmj.append("分摊土地使用权面积：").append(strfttdmj).append(pfm+",");
				}
			} else {
				if (!StringHelper.isEmpty(exchange)) {
					buildershyqmj.append(exchange).append(",");
				}
			}
			String strdytdmj = ReplaceEmptyByBlankOnDouble(dytdmj);
			if(info!=null&&!StringHelper.isEmpty(info.getZdbtn())&&info.getZdbtn()){
				buildershyqmj.append("独用土地使用权面积：").append(blank);
			}else{
				buildershyqmj.append("独用土地使用权面积：").append(strdytdmj).append(pfm);
			}
		}		
		return buildershyqmj.toString();
	}

	/*
	 * 获取专有建筑面积
	 */
	private String GetZYJZMJ(House h) {
		String pfm = "㎡";
		String qhdm = ConfigHelper.getNameByValue("XZQHDM");
		if(qhdm.indexOf("450721") == 0 ){//广西灵山
			pfm = "平方米";
		}
		StringBuilder builderzyjzmj = new StringBuilder();
		String strzyjzmj = ReplaceEmptyByBlankOnDouble(h.getSCTNJZMJ()) + pfm;
		builderzyjzmj.append("专有建筑面积：").append(strzyjzmj);
		return builderzyjzmj.toString();
	}

	/*
	 * 获取分摊建筑面积
	 */
	private String GetFTJZMJ(House h) {
		String pfm = "㎡";
		String qhdm = ConfigHelper.getNameByValue("XZQHDM");
		if(qhdm.indexOf("450721") == 0 ){//广西灵山
			pfm = "平方米";
		}
		StringBuilder builderftjzmj = new StringBuilder();
		String strftjzmj = ReplaceEmptyByBlankOnDouble(h.getSCFTJZMJ()) + pfm;
		builderftjzmj.append("分摊建筑面积：").append(strftjzmj);
		return builderftjzmj.toString();
	}

	/*
	 * 获取房屋结构
	 */
	private String GetFWJG(House h) {
		StringBuilder builderfwjg = new StringBuilder();
		String fwjgmc = "";
		if (!StringHelper.isEmpty(h.getFWJG())) {
			fwjgmc = ConstHelper.getNameByValue_new("FWJG", h.getFWJG().trim(),null);
		}
		fwjgmc = ReplaceEmptyByBlankOnString(fwjgmc);
		if (!StringHelper.isEmpty(h.getFWJG())) {
			builderfwjg.append("房屋结构：").append(fwjgmc);
		}
		return builderfwjg.toString();
	}
	
	/*
	 * 获取房屋结构1
	 */
	private String GetFWJG1(House h) {
		StringBuilder builderfwjg = new StringBuilder();
		String fwjgmc = "";
		if (!StringHelper.isEmpty(h.getFWJG1())) {
			fwjgmc = ConstHelper.getNameByValue_new("FWJG", h.getFWJG1().trim(),null);
		}
		fwjgmc = ReplaceEmptyByBlankOnString(fwjgmc);
		if (!StringHelper.isEmpty(h.getFWJG1())) {
			builderfwjg.append(fwjgmc);
		}
		return builderfwjg.toString();
	}
	
	/*
	 * 获取房屋结构2
	 */
	private String GetFWJG2(House h) {
		StringBuilder builderfwjg = new StringBuilder();
		String fwjgmc = "";
		if (!StringHelper.isEmpty(h.getFWJG2())) {
			fwjgmc = ConstHelper.getNameByValue_new("FWJG", h.getFWJG2().trim(),null);
		}
		fwjgmc = ReplaceEmptyByBlankOnString(fwjgmc);
		if (!StringHelper.isEmpty(h.getFWJG2())) {
			builderfwjg.append(fwjgmc);
		}
		return builderfwjg.toString();
	}
	
	/*
	 * 获取房屋结构3
	 */
	private String GetFWJG3(House h) {
		StringBuilder builderfwjg = new StringBuilder();
		String fwjgmc = "";
		if (!StringHelper.isEmpty(h.getFWJG3())) {
			fwjgmc = ConstHelper.getNameByValue_new("FWJG", h.getFWJG3().trim(),null);
		}
		fwjgmc = ReplaceEmptyByBlankOnString(fwjgmc);
		if (!StringHelper.isEmpty(h.getFWJG3())) {
			builderfwjg.append(fwjgmc);
		}
		return builderfwjg.toString();
	}

	/*
	 * 获取房屋总层数
	 */
	private String GetFWZCS(House h) {
		StringBuilder builderZCS = new StringBuilder();
		String fwzcs = null;
		if (!StringHelper.isEmpty(h.getZCS())) {
			String zcs = StringHelper.formatDouble(h.getZCS());
			if (!StringHelper.isEmpty(zcs)) {
				fwzcs = zcs;
			}
		}
		// 依据workflowcode	
		//黑龙江齐齐哈尔（230200）、龙沙区（230202）、建华区（230203）、铁锋区（230204）不显示房屋总层数		
		String xzqhdm = ConfigHelper.getNameByValue("XZQHDM");
		boolean flag_zcs = true;
		if ("230200".equals(xzqhdm) || "230202".equals(xzqhdm) || "230203".equals(xzqhdm) || "230203".equals(xzqhdm)) {
			flag_zcs= false;
		}
		if (flag_zcs) {
			if ("1".equals(ConfigHelper.getNameByValue("SHOWWAY_FWZCS"))) {
				if ("0".equals(fwzcs) || fwzcs == null) {
					builderZCS.append("房屋总层数：").append(blank);
					if("DSDXCS".equals(ConfigHelper.getNameByValue("ZCSXSFS"))){
						builderZCS.append("(地上层数：----,地下层数：----)");
					}
				}else {
					builderZCS.append("房屋总层数：").append(fwzcs);
					if("DSDXCS".equals(ConfigHelper.getNameByValue("ZCSXSFS"))){
						String dscs = "----",dxcs = "----";
						if(!StringHelper.isEmpty(h.getZRZBDCDYID())){
							RealUnit unit_zrz=UnitTools.loadUnit(BDCDYLX.ZRZ, DJDYLY.LS, h.getZRZBDCDYID());
							if(unit_zrz==null){
								unit_zrz=UnitTools.loadUnit(BDCDYLX.ZRZ, DJDYLY.GZ, h.getZRZBDCDYID());
							}
							if(unit_zrz!=null){
								Building building=(Building)unit_zrz;
								if (!StringHelper.isEmpty(StringHelper.formatDouble(building.getDSCS())) && !"0".equals(StringHelper.formatDouble(building.getDSCS()))) {
									dscs = StringHelper.formatDouble(building.getDSCS());
								}
								if (!StringHelper.isEmpty(StringHelper.formatDouble(building.getDXCS())) && !"0".equals(StringHelper.formatDouble(building.getDXCS()))) {
									dxcs = StringHelper.formatDouble(building.getDXCS());
								}
							}
						}
						builderZCS.append("(地上层数：").append(dscs) .append(",地下层数：").append(dxcs).append(")");
					}
				}
			}else {
				if ("0".equals(fwzcs) || fwzcs == null) {
					builderZCS.append("房屋总层数：").append("0");
					if("DSDXCS".equals(ConfigHelper.getNameByValue("ZCSXSFS"))){
						builderZCS.append("(地上层数：0,地下层数：0)");
					}
				}else {
					builderZCS.append("房屋总层数：").append(fwzcs);
					if("DSDXCS".equals(ConfigHelper.getNameByValue("ZCSXSFS"))){
						String dscs = "0",dxcs = "0";
						if(!StringHelper.isEmpty(h.getZRZBDCDYID())){
							RealUnit unit_zrz=UnitTools.loadUnit(BDCDYLX.ZRZ, DJDYLY.LS, h.getZRZBDCDYID());
							if(unit_zrz==null){
								unit_zrz=UnitTools.loadUnit(BDCDYLX.ZRZ, DJDYLY.GZ, h.getZRZBDCDYID());
							}
							if(unit_zrz!=null){
								Building building=(Building)unit_zrz;
								if (!StringHelper.isEmpty(StringHelper.formatDouble(building.getDSCS())) && !"0".equals(StringHelper.formatDouble(building.getDSCS()))) {
									dscs = StringHelper.formatDouble(building.getDSCS());
								}
								if (!StringHelper.isEmpty(StringHelper.formatDouble(building.getDXCS())) && !"0".equals(StringHelper.formatDouble(building.getDXCS()))) {
									dxcs = StringHelper.formatDouble(building.getDXCS());
								}
							}
						}
						builderZCS.append("(地上层数：").append(dscs) .append(",地下层数：").append(dxcs).append(")");
					}
				}
			}
			builderZCS.append(",");
		}
		return builderZCS.toString();
	}

	/*
	 * 获取房屋所在层
	 */
	private String GetFWSZC(House h) {
		StringBuilder builderSZC = new StringBuilder();
		String fwszc = ReplaceEmptyByBlankOnString(h.getSZC());
		builderSZC.append("房屋所在层：").append(fwszc);
		return builderSZC.toString();
	}

	/*
	 * 获取房屋竣工时间
	 */
	private String GetFWJGSJ(House h) {
		StringBuilder builderJGSJ = new StringBuilder();
		// 房屋竣工时间为空，不显示
		if (!StringHelper.isEmpty(h.getJGSJ())) {
			String fwjgsj = ReplaceEmptyByBlankOnDate(h.getJGSJ());
			builderJGSJ.append("房屋竣工时间：").append(fwjgsj);
		}
		return builderJGSJ.toString();
	}

	/*
	 * 
	 * 获取批准面积
	 */
	private String GetPZMJ(RealUnit unit) {
		String pfm = "㎡";
		String qhdm = ConfigHelper.getNameByValue("XZQHDM");
		if(qhdm.indexOf("450721") == 0 ){//广西灵山
			pfm = "平方米";
		}
		StringBuilder builderPZMJ = new StringBuilder();
		String pzmj = "";
		double pz = 0.0;
		if (BDCDYLX.SYQZD.equals(unit.getBDCDYLX())) {
			OwnerLand ownerland = (OwnerLand) unit;
			// 批准面积为空，不显示
			if (!StringHelper.isEmpty(ownerland.getPZMJ())) {
				pz = ownerland.getPZMJ();
				pzmj = ReplaceEmptyByBlankOnDouble(pz);
				builderPZMJ.append("批准面积：").append(pzmj).append(pfm);
			}
		} else if (BDCDYLX.SHYQZD.equals(unit.getBDCDYLX())) {
			UseLand useland = (UseLand) unit;
			// 批准面积为空，不显示
			if (!StringHelper.isEmpty(useland.getPZMJ())) {
				pz = useland.getPZMJ();
				pzmj = ReplaceEmptyByBlankOnDouble(pz);
				builderPZMJ.append("批准面积：").append(pzmj).append(pfm);
			}
		} else if (BDCDYLX.H.equals(unit.getBDCDYLX())
				|| BDCDYLX.YCH.equals(unit.getBDCDYLX())) {
			House house = (House) unit;
			String zdbdcdyid = house.getZDBDCDYID();
			if (!StringHelper.isEmpty(zdbdcdyid)) {
				BDCS_SYQZD_XZ SYQZD_XZ = baseCommonDao.get(BDCS_SYQZD_XZ.class,
						zdbdcdyid);
				if (SYQZD_XZ != null
						&& !StringHelper.isEmpty(SYQZD_XZ.getPZMJ())) {
					pz = SYQZD_XZ.getPZMJ();
					pzmj = ReplaceEmptyByBlankOnDouble(pz);
					builderPZMJ.append("批准面积：").append(pzmj).append(pfm);
				} else {
					BDCS_SHYQZD_XZ SHYQZD_XZ = baseCommonDao.get(
							BDCS_SHYQZD_XZ.class, zdbdcdyid);
					if (SHYQZD_XZ != null
							&& !StringHelper.isEmpty(SHYQZD_XZ.getPZMJ())) {
						pz = SHYQZD_XZ.getPZMJ();
						pzmj = ReplaceEmptyByBlankOnDouble(pz);
						builderPZMJ.append("批准面积：").append(pzmj).append(pfm);
					}
				}
			}
		} else {
			return null;
		}
		return builderPZMJ.toString();
	}

	/*
	 * 获取持证方式
	 */
	private String GetCZFS(List<RightsHolder> qlrlist, Rights ql) {
		StringBuilder builderCZFS = new StringBuilder();
		if (qlrlist != null && qlrlist.size() > 1) {
			builderCZFS.append("持证方式：").append(
					ConstHelper.getNameByValue_new("CZFS", ql.getCZFS(),null));
		}
		return builderCZFS.toString();
	}

	/*
	 * 获取持证人
	 */
	private String GetCZR(List<RightsHolder> qlrlist, Rights ql) {
		StringBuilder builderCZR = new StringBuilder();
		if (qlrlist.size() > 1 && CZFS.GTCZ.Value.equals(ql.getCZFS())) {
			String czr = "";
			for (RightsHolder qlr : qlrlist) {
				if (SF.YES.Value.equals(qlr.getISCZR())) {
					czr += qlr.getQLRMC() + ",";
				}
			}
			if (!StringHelper.isEmpty(czr)) {
				czr = czr.substring(0, czr.length() - 1);
				builderCZR.append("持证人：").append(czr);
			}
		}
		return builderCZR.toString();
	}

	/*
	 * 获取附记
	 */
	public String GetFJ(BDCS_XMXX xmxx, Rights ql, BDCDYLX lx,
			List<RightsHolder> qlrAllList, RealUnit unit) {
		String fj = "";
		String fj_lclx = "";
		String fj_ywh = "";
		String fj_qlfj = "";
		String fj_gyrinfo = "";
		String fj_hth = "";
		String fj_msr = "";
		String fj_cqly = ""; //新建乌鲁木齐添加
		String qxdm_ = ConfigHelper.getNameByValue("XZQHDM");

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

		if (ql != null && !StringHelper.isEmpty(ql.getFJ())) {
			fj_qlfj = ql.getFJ().replaceAll("\r\n|\r|\n|\n\r", hhf)
					.replaceAll(" ", "\u00A0")
					+ hhf;
		}

		//附记流程类型显示控制
		String showfjlclx = ConfigHelper.getNameByValue("showlclx");
		List<Wfi_ProInst> proinst=baseCommonDao.getDataList(Wfi_ProInst.class, " FILE_NUMBER='"+xmxx.getPROJECT_ID()+"'");
		if(proinst!=null&&proinst.size()>0){
			Wfi_ProInst profj = proinst.get(0);
			if ("1".equals(showfjlclx)){
				String name = profj.getProdef_Name();
				fj_lclx ="业务类型：" + name +hhf;
			}
		}
		
		// 办理房屋产权首次登记时，添加判断权利中的合同号和买受人存在，则显示出来
		String qllx = ql.getQLLX();
		if (ql != null && "031".equals(lx.Value)
				&& "100".equals(xmxx.getDJLX())
				&& ("4".equals(qllx) || "6".equals(qllx) || "8".equals(qllx))) {
			if (!StringHelper.isEmpty(ql.getHTH())) {
				fj_hth = "合同号：" + ql.getHTH() + hhf;
			}
			if (!StringHelper.isEmpty(ql.getMSR())) {
				fj_msr = "买受人：" + ql.getMSR() + hhf;
			}
		}
		if (qlrAllList != null && qlrAllList.size() > 0) {
			boolean bShowGYQR = false;
			if (CZFS.FBCZ.Value.equals(ql.getCZFS())) {
				for (RightsHolder qlr : qlrAllList) {
					if (!GYFS.DYSY.Value.equals(qlr.getGYFS())) {
						bShowGYQR = true;
						break;
					}
				}
			} else if (CZFS.GTCZ.Value.equals(ql.getCZFS())) {
				for (RightsHolder qlr : qlrAllList) {
					if (GYFS.AFGY.Value.equals(qlr.getGYFS())) {
						bShowGYQR = true;
						break;
					}
				}
			}
			
			if (qlrAllList.size() <= 1) {
				bShowGYQR = false;
			}
			if ("2".equals(ConfigHelper.getNameByValue("ISSHOWALLZJH"))) {
				if(qlrAllList != null && qlrAllList.size() > 0){
					StringBuilder buildergyqr = new StringBuilder();
					buildergyqr.append("<table style='text-align:center;width:auto;border:none;margin:10px 0px;'>"
							+ "<tr>"
							+ "<td style='text-align:left;'>共有权人</td>");
					//证书附记是否显示不动产权证号
					if("1".equals(ConfigHelper.getNameByValue("fjqzhkz"))){
						buildergyqr.append("<td style='padding:0 15px;'>证件号</td>");
					}
					buildergyqr.append("<td style='padding:0 15px;'>不动产权证号</td>"
								+ "<td style='padding:0 15px;'>共有情况</td>"
								+ "</tr>");
					for (RightsHolder qlr : qlrAllList) {
						buildergyqr.append("<tr>");
						buildergyqr.append("<td style='text-align:left;'>").append(qlr.getQLRMC()).append("</td>");
						if("1".equals(ConfigHelper.getNameByValue("fjqzhkz"))){
							buildergyqr.append("<td style='padding:0 15px;'>").append(qlr.getZJH()).append("</td>");
						}
						String bdcqzh = qlr.getBDCQZH();
						if (StringHelper.isEmpty(bdcqzh)) {
							bdcqzh = blank;
						}
						buildergyqr.append("<td style='padding:0 15px;'>").append(bdcqzh).append("</td>");
						if (qlr.getGYFS() != null && GYFS.AFGY.Value.equals(qlr.getGYFS())) {
							String qlbl = qlr.getQLBL();
							if (StringHelper.isEmpty(qlbl)) {
								qlbl = blank;
							} else {
								if (!qlbl.endsWith("%") && !qlbl.contains("/")) {
									qlbl = qlbl + "%";
								}
							}
							buildergyqr.append("<td style='padding:0 15px;'>").append("共有份额 ").append(qlbl).append("</td>");
						} else {
							String gyfs = qlr.getGYFSName();
							if (StringHelper.isEmpty(gyfs)) {
								gyfs = blank;
							}
							buildergyqr.append("<td style='padding:0 15px;'>").append(gyfs).append("</td>");
						}
						buildergyqr.append("</tr>");
					}
					buildergyqr.append("</table>");
					fj_gyrinfo = buildergyqr.toString();
				}
			}else if ("3".equals(ConfigHelper.getNameByValue("ISSHOWALLZJH"))) {
				
			}else if (bShowGYQR) {
				StringBuilder buildergyqr = new StringBuilder();
				buildergyqr.append("<table style='text-align:center;width:auto;border:none;margin:10px 0px;'>"
						+ "<tr>"
						+ "<td style='text-align:left;'>共有权人</td>");
				//证书附记是否显示不动产权证号
				if("1".equals(ConfigHelper.getNameByValue("fjqzhkz"))){
					buildergyqr.append("<td style='padding:0 15px;'>证件号</td>");
				}
				buildergyqr.append("<td style='padding:0 15px;'>不动产权证号</td>"
							+ "<td style='padding:0 15px;'>共有情况</td>"
							+ "</tr>");
				for (RightsHolder qlr : qlrAllList) {
					buildergyqr.append("<tr>");
					buildergyqr.append("<td style='text-align:left;'>").append(qlr.getQLRMC()).append("</td>");
					if("1".equals(ConfigHelper.getNameByValue("fjqzhkz"))){
						buildergyqr.append("<td style='padding:0 15px;'>").append(qlr.getZJH()).append("</td>");
					}
					String bdcqzh = qlr.getBDCQZH();
					if (StringHelper.isEmpty(bdcqzh)) {
						bdcqzh = blank;
					}
					buildergyqr.append("<td style='padding:0 15px;'>").append(bdcqzh).append("</td>");
					if (qlr.getGYFS() != null && GYFS.AFGY.Value.equals(qlr.getGYFS())) {
						String qlbl = qlr.getQLBL();
						if (StringHelper.isEmpty(qlbl)) {
							qlbl = blank;
						} else {
							if (!qlbl.endsWith("%") && !qlbl.contains("/")) {
								qlbl = qlbl + "%";
							}
						}
						buildergyqr.append("<td style='padding:0 15px;'>").append("共有份额 ").append(qlbl).append("</td>");
					} else {
						String gyfs = qlr.getGYFSName();
						if (StringHelper.isEmpty(gyfs)) {
							gyfs = blank;
						}
						buildergyqr.append("<td style='padding:0 15px;'>").append(gyfs).append("</td>");
					}
					buildergyqr.append("</tr>");
				}
				buildergyqr.append("</table>");
				fj_gyrinfo = buildergyqr.toString();
			}
		}
		if(qxdm_.indexOf("45")==0){
			fj = fj_qlfj + fj_ywh + fj_lclx + fj_gyrinfo + fj_hth + fj_msr;
		}else{
			fj = fj_ywh + fj_lclx + fj_gyrinfo + fj_qlfj + fj_hth + fj_msr;
		}	
		if (BDCDYLX.LD.equals(lx)) {
			String fj_sz = "";
			String szd = "";
			String szn = "";
			String szx = "";
			String szb = "";
			Forest forest = (Forest) unit;
			if (forest != null) {
				szd = ReplaceEmptyByBlankOnString(forest.getZDSZD());
				szn = ReplaceEmptyByBlankOnString(forest.getZDSZN());
				szx = ReplaceEmptyByBlankOnString(forest.getZDSZX());
				szb = ReplaceEmptyByBlankOnString(forest.getZDSZB());
			}
			fj_sz = "四至：" + "东：" + szd + hhf + space + space + space + "&nbsp;"
					+ "南：" + szn + hhf + space + space + space + "&nbsp;"
					+ "西：" + szx + hhf + space + space + space + "&nbsp;"
					+ "北：" + szb + hhf;
			fj = fj + fj_sz;
		}
		
		if (("031".equals(lx.Value) ||"032".equals(lx.Value) ) && "1".equals(ConfigHelper.getNameByValue("ISSHOW_FWBM"))) {
			House house = (House)unit;
			if (house != null) {
				if(!StringHelper.isEmpty(house.getFWBM())){
					String fj_fwbm = "房屋编码：" + house.getFWBM() + hhf;
					fj = fj + fj_fwbm;
				}
			}
		}
		
		if(("031".equals(lx.Value) ||"032".equals(lx.Value) ) && "6501".equals(qxdm_.length()>4?qxdm_.substring(0,4):qxdm_)){
			House house = (House)unit;
			if (house != null) {
				if(!StringHelper.isEmpty(house.getCQLY())){				
					fj_cqly = "产权来源:"+ConstHelper.getNameByValue_new("CQLY", house.getCQLY(),null);
					
				}else {
					fj_cqly = "产权来源:----";
				}
				fj = fj + fj_cqly;
			}
		}
		return fj;
	}
	
	/*
	 * 获取共有人情况
	 */
	private String GetGYRQK(Rights ql) {
		StringBuilder builderGYRQK = new StringBuilder();
		if (ql != null) {
			String gyrqk = ql.getGYRQK();
			if (!StringHelper.isEmpty(gyrqk)) {
				builderGYRQK.append("共有人情况：").append(gyrqk);
			}
		}
		return builderGYRQK.toString();
	}

	public String ReplaceEmptyByBlankOnString(String obj) {
		String value = blank;
		if (!StringHelper.isEmpty(obj) && !"null".equals(obj)) {
			value = StringHelper.formatObject(obj);
		}
		return value;
	}

	public String ReplaceEmptyByBlankOnDouble(Double obj) {
		String value = blank;
		String areaFormater = "#######.###";
		if (!StringHelper.isEmpty(ConfigHelper.getNameByValue("AREAFORMATER"))) {
			areaFormater = ConfigHelper.getNameByValue("AREAFORMATER");
		}
		String configAreaJudge = ConfigHelper.getNameByValue("AreaJudge");
		if (!StringHelper.isEmpty(obj) && obj >= 0) {
			if ("1".equals(configAreaJudge) && (obj == 0)) {
				return value;
			}
			DecimalFormat f = new DecimalFormat(areaFormater);
			f.setRoundingMode(RoundingMode.HALF_UP);
			value = f.format(obj);
			if((obj < 1 && value.indexOf("0")==-1)||(obj < 1 && value.indexOf("0")!=0)){	//liangq 当数值小于1时,不显示前面的0问题修正
				value = "0"+value;
			}
			
		}
		return value;
	}

	//针对海域的
	public String ReplaceEmptyByBlankOnDoubleForZH(Double obj) {
		String value = blank;
		String areaFormater = "#######.###";
		if (!StringHelper.isEmpty(ConfigHelper.getNameByValue("AREAFORMATER_ZH"))) {
			areaFormater = ConfigHelper.getNameByValue("AREAFORMATER_ZH");
		}
		String configAreaJudge = ConfigHelper.getNameByValue("AreaJudge");
		if (!StringHelper.isEmpty(obj) && obj >= 0) {
			if ("1".equals(configAreaJudge) && (obj == 0)) {
				return value;
			}
			DecimalFormat f = new DecimalFormat(areaFormater);
			f.setRoundingMode(RoundingMode.HALF_UP);
			value = f.format(obj);
			if((obj < 1 && value.indexOf("0")==-1)||(obj < 1 && value.indexOf("0")!=0)){	//liangq 当数值小于1时,不显示前面的0问题修正
				value = "0"+value;
			}
		}
		return value;
	}
	
	public String ReplaceEmptyByBlankOnInteger(Integer obj) {
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
	 * @Description: 长度补全
	 * @Title: calculatePlaces
	 * @Author：赵梦帆
	 * @Data：2016年12月7日 下午3:05:12
	 * @param Oldstr
	 * @param len
	 * @return
	 * @return StringBuilder
	 */
	public static String calculatePlaces(String Oldstr , int len)  
	{
		if(StringHelper.isEmpty(Oldstr))
			return "";
		StringBuilder str = new StringBuilder(Oldstr);
		byte[] tempByte = null;
		try {
			tempByte = Oldstr.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		if(tempByte!=null)
			str.setLength((len*3-tempByte.length)>0?len*3:tempByte.length);
		return str.toString();
	} 
	
	public static String FormatByDatatype(Object obj) {
		if (obj != null) {
			return obj.toString();
		} else {
			return "----";
		}
	}
}
