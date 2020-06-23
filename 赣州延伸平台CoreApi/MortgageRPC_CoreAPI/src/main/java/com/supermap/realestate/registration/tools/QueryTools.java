package com.supermap.realestate.registration.tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import com.supermap.realestate.registration.factorys.HandlerFactory;
import com.supermap.realestate.registration.mapping.HandlerMapping.RegisterWorkFlow;
import com.supermap.realestate.registration.model.BDCS_DJDY_GZ;
import com.supermap.realestate.registration.model.BDCS_DJDY_XZ;
import com.supermap.realestate.registration.model.BDCS_QL_GZ;
import com.supermap.realestate.registration.model.BDCS_QL_LS;
import com.supermap.realestate.registration.model.BDCS_SHYQZD_XZ;
import com.supermap.realestate.registration.model.BDCS_SQR;
import com.supermap.realestate.registration.model.BDCS_XMXX;
import com.supermap.realestate.registration.model.BDCS_ZS_GZ;
import com.supermap.realestate.registration.model.BDCS_ZS_XZ;
import com.supermap.realestate.registration.model.interfaces.House;
import com.supermap.realestate.registration.model.interfaces.RealUnit;
import com.supermap.realestate.registration.model.interfaces.Rights;
import com.supermap.realestate.registration.model.interfaces.RightsHolder;
import com.supermap.realestate.registration.model.interfaces.SubRights;
import com.supermap.realestate.registration.model.interfaces.TDYT;
import com.supermap.realestate.registration.model.interfaces.UseLand;
import com.supermap.realestate.registration.util.ConstHelper;
import com.supermap.realestate.registration.util.ConstValue.BDCDYLX;
import com.supermap.realestate.registration.util.ConstValue.DJDYLY;
import com.supermap.realestate.registration.util.ConstValue.DJLX;
import com.supermap.realestate.registration.util.ConstValue.QLLX;
import com.supermap.realestate.registration.util.Global;
import com.supermap.realestate.registration.util.ProjectHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.wisdombusiness.archives.web.common.RightsInfo;
import com.supermap.wisdombusiness.core.SuperSpringContext;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import com.supermap.wisdombusiness.workflow.model.Wfi_ProInst;

public class QueryTools {

	 
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static HashMap<String, List<HashMap<String, Object>>> GetXMInfo(String project_id) {
		HashMap<String, List<HashMap<String, Object>>> info = new HashMap<String, List<HashMap<String, Object>>>();
		List<HashMap<String, Object>> XM_List = new ArrayList<HashMap<String, Object>>();
		List<HashMap<String, Object>> Common_List = new ArrayList<HashMap<String, Object>>();
		List<HashMap<String, Object>> Owner_List = new ArrayList<HashMap<String, Object>>();
		List<HashMap<String, Object>> Other_List = new ArrayList<HashMap<String, Object>>();
		List<HashMap<String, Object>> Limit_List = new ArrayList<HashMap<String, Object>>();
		List<HashMap<String, Object>> Notice_List = new ArrayList<HashMap<String, Object>>();
		
		
		CommonDao dao = SuperSpringContext.getContext().getBean(CommonDao.class);
		List<Wfi_ProInst> instList = dao.getDataList(
				Wfi_ProInst.class, " FILE_NUMBER = '"
						+project_id + "' ");
		BDCS_XMXX xmxx = Global.getXMXX(project_id);
		if (xmxx == null) {
			return info;
		}
		HashMap<String, Object> xm_info = new HashMap<String, Object>();
		xm_info.put("DJLXMC", DJLX.initFrom(xmxx.getDJLX()).Name);
		xm_info.put("DJLX", DJLX.initFrom(xmxx.getDJLX()).Value);
		xm_info.put("QLLXMC", QLLX.initFrom(xmxx.getQLLX()).Name);
		xm_info.put("QLLX", QLLX.initFrom(xmxx.getQLLX()).Value);
		xm_info.put("XMMC", xmxx.getXMMC());
		xm_info.put("XMBH", xmxx.getId());
		xm_info.put("PROJECT_ID", xmxx.getPROJECT_ID());
		xm_info.put("SLRY", xmxx.getSLRY());
		xm_info.put("SLSJ", xmxx.getSLSJ());
		xm_info.put("AJH", xmxx.getAJH());
		//获取流程编号
		if(!StringHelper.isEmpty(project_id)) {
			xm_info.put("LCBH", ProjectHelper.getWorkflowCodeByProjectID(project_id));
		}
		if(instList.size()>0){
			Wfi_ProInst oneproinst=instList.get(0);
			if(!StringHelper.isEmpty(oneproinst.getProdef_Name())){
				xm_info.put("DJXL",oneproinst.getProdef_Name());
				xm_info.put("DJDL", DJLX.initFrom(xmxx.getDJLX()).Name);
			}
		}
		// xm_info.put("YWLX", "");// 不知道咋取值
		XM_List.add(xm_info);
		String xmbhFilter = ProjectHelper.GetXMBHCondition(xmxx.getId());
		List<BDCS_DJDY_GZ> djdys = dao.getDataList(BDCS_DJDY_GZ.class, xmbhFilter);
		boolean flag = false;
		List<BDCS_SQR> sqrlist = new ArrayList<BDCS_SQR>();
		List<String> ywrlist = new ArrayList<String>();
		List<String> ywrzjhlist = new ArrayList<String>();
		List<String> ywrzjzllist = new ArrayList<String>();
		
	    sqrlist = dao.getDataList(BDCS_SQR.class, "XMBH='" + xmxx.getId() + "'");
	
		if (djdys != null && djdys.size() > 0) {
			for (BDCS_DJDY_GZ djdy : djdys) {
				String bdcdyid = djdy.getBDCDYID();
				String djdyid = djdy.getDJDYID();
				BDCDYLX bdcdylx = BDCDYLX.initFrom(djdy.getBDCDYLX());
				DJDYLY djdyly = DJDYLY.initFrom(djdy.getLY());
				if(DJLX.initFrom(xmxx.getDJLX()).Value=="400"&&QLLX.initFrom(xmxx.getQLLX()).Value!="23"){
					djdyly=DJDYLY.LS;
				}
				RealUnit unit = UnitTools.loadUnit(bdcdylx, djdyly, bdcdyid);
				if (unit == null) {
					continue;
				}
				List RightsInfo_List = new ArrayList();
				HashMap<String, Object> dy_info = new HashMap<String, Object>();
				dy_info.put("XMBH", xmxx.getId());
				dy_info.put("ZL", unit.getZL());
				dy_info.put("BDCDYLXMC", BDCDYLX.initFrom(djdy.getBDCDYLX()).Bdclx);
				dy_info.put("BDCDYLX", BDCDYLX.initFrom(djdy.getBDCDYLX()).Value);
				dy_info.put("BDCDYH", unit.getBDCDYH());
				String str_bdcdyid = bdcdyid;
				RegisterWorkFlow flow = HandlerFactory.getWorkflow(xmxx.getPROJECT_ID());
				if (flow.getHandlername().toUpperCase().contains("GZDJ")) {
					List<BDCS_DJDY_XZ> djdys_xz = dao.getDataList(BDCS_DJDY_XZ.class, "DJDYID='" + djdyid + "'");
					if (djdys_xz != null && djdys_xz.size() > 0) {
						str_bdcdyid = djdys_xz.get(0).getBDCDYID();
					}
				}
				dy_info.put("BDCDYID", str_bdcdyid);
				House h = null;
				UseLand land = null;
				if (BDCDYLX.SHYQZD.equals(bdcdylx)) {
					dy_info.put("ZDMJ", unit.getMJ());
					 land = (UseLand) unit;
					List<TDYT> tdyts = land.getTDYTS();
					if (tdyts != null && tdyts.size() > 0) {
						List<String> list_tdyt = new ArrayList<String>();
						for (TDYT tdyt : tdyts) {
							if (!StringHelper.isEmpty(tdyt.getTDYT())) {
								String tdytmc = ConstHelper.getNameByValue("TDYT", tdyt.getTDYT());
								if (!StringHelper.isEmpty(tdytmc) && !list_tdyt.contains(tdytmc)) list_tdyt.add(tdytmc);
							}
						}
						String str_tdyt = StringHelper.formatList(list_tdyt, "、");
						dy_info.put("TDYT", str_tdyt);
						dy_info.put("DJH",land.getDJH());

					}
				} else if (BDCDYLX.H.equals(bdcdylx) || BDCDYLX.YCH.equals(bdcdylx)) {
					 h = (House) unit;
					dy_info.put("JZMJ", unit.getMJ());
					dy_info.put("GHYT", h.getGHYTName());
					dy_info.put("FWJG", ConstHelper.getNameByValue("FWJG", h.getFWJG()));
					dy_info.put("JGSJ", h.getJGSJ());
					dy_info.put("FWXZ", ConstHelper.getNameByValue("FWXZ", h.getFWXZ()));
					dy_info.put("FH", h.getFH());
					dy_info.put("SZC", h.getSZC());
					dy_info.put("FWCB", ConstHelper.getNameByValue("FWCB", h.getFWCB()));
					String zdid=h.getZDBDCDYID();
					List<BDCS_SHYQZD_XZ> listzds=dao.getDataList(BDCS_SHYQZD_XZ.class, "bdcdyid='"+zdid+"'");
					if(listzds.size()>0){
						BDCS_SHYQZD_XZ onezd=listzds.get(0);
						dy_info.put("ZDMJ", onezd.getZDMJ());
					}
					
					
				}

				List<Rights> qls = RightsTools.loadRightsByCondition(DJDYLY.GZ, "DJDYID='" + djdyid + "' AND " + xmbhFilter);

				if (qls != null && qls.size() > 0) {
					for (Rights ql : qls) {
						String qllx = ql.getQLLX();
						String djlx = ql.getDJLX();
						String lyqlid=ql.getLYQLID();
						String lyxmbh = null;
						if(!StringHelper.isEmpty(lyqlid)){
							BDCS_QL_GZ lyql=dao.get(BDCS_QL_GZ.class, lyqlid);
							if(lyql != null){
								lyxmbh=lyql.getXMBH();
							}
						}
						
						SubRights fsql = RightsTools.loadSubRights(DJDYLY.GZ, ql.getFSQLID());
						if ((QLLX.GJTDSYQ.Value.equals(qllx) || QLLX.JTTDSYQ.Value.equals(qllx) || QLLX.LDSHYQ_SLLMSYQ.Value.equals(qllx) || QLLX.LDSYQ_SLLMSYQ.Value.equals(qllx) || QLLX.LDSYQ.Value.equals(qllx) || QLLX.GYJSYDSHYQ.Value.equals(qllx) || QLLX.ZJDSYQ.Value.equals(qllx) || QLLX.JTJSYDSYQ.Value.equals(qllx) || QLLX.GYJSYDSHYQ_FWSYQ.Value.equals(qllx) || QLLX.ZJDSYQ_FWSYQ.Value.equals(qllx) || QLLX.JTJSYDSYQ_FWSYQ.Value.equals(qllx)|| QLLX.GYNYDSHYQ.Value.equals(qllx)|| QLLX.DYQ.Value.equals(qllx)|| QLLX.LDSYQ_SLLMSYQ.Value.equals(qllx)|| QLLX.CYSQY.Value.equals(qllx)|| QLLX.TDCBJYQ.Value.equals(qllx)|| QLLX.TDCBJYQ_SLLMSYQ.Value.equals(qllx)) && !DJLX.YGDJ.Value.equals(djlx)) {
							RightsInfo rightsinfo=new RightsInfo();
							rightsinfo.setDJLX(djlx);
							rightsinfo.setQLLX(qllx);
							if(sqrlist.size()>0){
								for(BDCS_SQR sqr:sqrlist){
									if(sqr.getSQRLB().equals("1")){
										rightsinfo.setQLR(sqr.getSQRXM());
										rightsinfo.setQLRZJH(sqr.getZJH());
										rightsinfo.setQLRZJLX(sqr.getZJLX());
									}else{
										rightsinfo.setYWR(sqr.getSQRXM());
										rightsinfo.setYWRZJH(sqr.getZJH());
										rightsinfo.setYWRZJLX(sqr.getZJLX());
									}
								}
							}
							HashMap<String, Object> ql_info = new HashMap<String, Object>();
							List<RightsHolder> qlrs = RightsHolderTools.loadRightsHolders(DJDYLY.GZ, ql.getId());
							List<String> list_qlr = new ArrayList<String>();
							List<String> list_zjh = new ArrayList<String>();
							List<String> list_zjzl = new ArrayList<String>();
							List<String> list_qzh = new ArrayList<String>();
							List<String> list_zsbh = new ArrayList<String>();
							String onezsinfo = null;
							if (qlrs != null && qlrs.size() > 0) {
								for (RightsHolder qlr : qlrs) {
									if (!StringHelper.isEmpty(qlr.getQLRMC())) {
										list_qlr.add(qlr.getQLRMC());
										if (!StringHelper.isEmpty(qlr.getZJH()) && !list_zjh.contains(qlr.getZJH())) {
											list_zjh.add(qlr.getZJH());
										}
										if (!StringHelper.isEmpty(qlr.getZJZLName()) && !list_zjzl.contains(qlr.getZJZLName())) {
											list_zjzl.add(qlr.getZJZLName());
										}
										if (!StringHelper.isEmpty(qlr.getBDCQZH()) && !list_qzh.contains(qlr.getBDCQZH())) {
											list_qzh.add(qlr.getBDCQZH());
										}
										 
										List<BDCS_ZS_GZ> zss = dao.getDataList(BDCS_ZS_GZ.class, " id in (SELECT ZSID FROM BDCS_QDZR_GZ WHERE QLRID='" + qlr.getId() + "')");
										if (zss != null && zss.size() > 0) {
											if (!StringHelper.isEmpty(zss.get(0).getZSBH()) && !list_zsbh.contains(zss.get(0).getZSBH())) {
												list_zsbh.add(zss.get(0).getZSBH());
											}
											
											if (!StringHelper.isEmpty(zss.get(0).getZSDATA())) {
												onezsinfo= zss.get(0).getZSDATA();
											}
										}
									}
								}
							} else {

								sqrlist = dao.getDataList(BDCS_SQR.class, "XMBH='" + xmxx.getId() + "' AND SQRLB='1' ");
								if (sqrlist.size() > 0) {
									for (BDCS_SQR holder : sqrlist) {

										if (!StringHelper.isEmpty(holder.getSQRXM())) {
											list_qlr.add(holder.getSQRXM());
										}
										if (!StringHelper.isEmpty(holder.getZJH()) && !list_zjh.contains(holder.getZJH())) {
											list_zjh.add(holder.getZJH());
										}
										 
									}
								}
								
								if (!StringHelper.isEmpty(ql.getBDCQZH()) && !list_qzh.contains(ql.getBDCQZH())) {
									list_qzh.add(ql.getBDCQZH());
								}

							}
							String str_qlr = StringHelper.formatList(list_qlr, "、");
							String str_zjh = StringHelper.formatList(list_zjh, "、");
							String str_zjzl = StringHelper.formatList(list_zjzl, "、");
							String str_qzh = StringHelper.formatList(list_qzh, "、");
							String str_zsbh = StringHelper.formatList(list_zsbh, "、");
							if (!dy_info.containsKey("SYQR")) {
								dy_info.put("SYQR", str_qlr);
								dy_info.put("ZJH", str_zjh);
								dy_info.put("ZJZL", str_zjzl);
								dy_info.put("QZH", str_qzh);
								dy_info.put("BDCQZH", str_qzh);
								dy_info.put("QZHZSBH", str_zsbh);
					
								if(!StringHelper.isEmpty(onezsinfo)){
									HashMap<String, String> zsmap=toHashMap(onezsinfo);
									dy_info.put("GYQK", zsmap.get("GYQK"));
									dy_info.put("QLXZ", zsmap.get("QLXZ"));
									dy_info.put("SYQX", zsmap.get("SYQX"));
									dy_info.put("YT", zsmap.get("YT"));
									dy_info.put("MJ", zsmap.get("MJ"));
									dy_info.put("QLQTZK", zsmap.get("QLQTZK"));
									dy_info.put("FJ", zsmap.get("FJ"));
								}
									 
							}else{
								dy_info.put("SYQR", str_qlr);
							}
							 
							ql_info.put("SYQR", str_qlr);
							ql_info.put("ZJH", str_zjh);
							ql_info.put("ZJZL", str_zjzl);
							List<String> listzmh=new ArrayList<String>();
							for(String qzh:list_qzh){
								if(!listzmh.contains(qzh)){
									listzmh.add(qzh);
								}else{
									continue;
								}
							}
							String str_zmh = StringHelper.formatList(listzmh, "、");
							
							ql_info.put("QZH", str_zmh);
							ql_info.put("QZHZSBH", str_zsbh);
							ql_info.put("FJ", ql.getFJ());
							ql_info.put("DBR", ql.getDBR());
							ql_info.put("DJSJ", ql.getDJSJ());
							rightsinfo.setDJSJ(StringHelper.FormatByDatatype(ql.getDJSJ()));
							ql_info.put("BDCDYID", str_bdcdyid);
							ql_info.put("BDCDYH", unit.getBDCDYH());
							ql_info.put("QLQSSJ", ql.getQLQSSJ());
							ql_info.put("QLJSSJ", ql.getQLJSSJ());
							rightsinfo.setQLQSSJ(StringHelper.FormatByDatatype(ql.getQLQSSJ()));
							rightsinfo.setQLJSSJ(StringHelper.FormatByDatatype(ql.getQLJSSJ()));
							ql_info.put("ZL", unit.getZL());
							ql_info.put("BDCDYLXMC", BDCDYLX.initFrom(djdy.getBDCDYLX()).Bdclx);
							if (fsql != null) {
								ql_info.put("ZXDBR", fsql.getZXDBR());
								ql_info.put("ZXSJ", fsql.getZXSJ());
								rightsinfo.setZXSJ(StringHelper.FormatByDatatype(fsql.getZXSJ()));
							}
							ql_info.put("LYXMBH", lyxmbh);
							rightsinfo.setLYXMBH(lyxmbh);
							Owner_List.add(ql_info);
							RightsInfo_List.add(rightsinfo);
						} else if (QLLX.DIYQ.Value.equals(qllx)) {
							RightsInfo rightsinfo=new RightsInfo();
							rightsinfo.setDJLX(djlx);
							rightsinfo.setQLLX(qllx);
							if(sqrlist.size()>0){
								for(BDCS_SQR sqr:sqrlist){
									if(sqr.getSQRLB().equals("1")){
										rightsinfo.setQLR(sqr.getSQRXM());
										rightsinfo.setQLRZJH(sqr.getZJH());
										rightsinfo.setQLRZJLX(sqr.getZJLX());
									}else{
										rightsinfo.setYWR(sqr.getSQRXM());
										rightsinfo.setYWRZJH(sqr.getZJH());
										rightsinfo.setYWRZJLX(sqr.getZJLX());
									}
								}
							}
							HashMap<String, Object> ql_info = new HashMap<String, Object>();
							List<RightsHolder> qlrs = RightsHolderTools.loadRightsHolders(DJDYLY.GZ, ql.getId());
							List<String> list_qlr = new ArrayList<String>();
							List<String> list_dyqlr = new ArrayList<String>();
							List<String> list_dyqzjh = new ArrayList<String>();
							List<String> list_zjh = new ArrayList<String>();
							List<String> list_zjzl = new ArrayList<String>();
							List<String> list_dyqzjzl = new ArrayList<String>();
							List<String> list_qzh = new ArrayList<String>();
							List<String> list_gyqk = new ArrayList<String>();
							List<String> list_zsbh = new ArrayList<String>();
							String onezsinfo = null;
							if (qlrs != null && qlrs.size() > 0) {

								for (RightsHolder qlr : qlrs) {
									if (!StringHelper.isEmpty(qlr.getQLRMC())) {
										if (sqrlist.size() > 0) {
											for (BDCS_SQR sqr : sqrlist) {
												if (sqr.getSQRLB().equals("2") && !ywrlist.contains(sqr.getSQRXM())) {
													ywrlist.add(sqr.getSQRXM());
													ywrzjhlist.add(sqr.getZJH());
													ywrzjzllist.add(sqr.getZJLX());
												}
											}
										}

										list_qlr.add(qlr.getQLRMC());
										if (!StringHelper.isEmpty(qlr.getZJH()) && !list_zjh.contains(qlr.getZJH())) {
											list_zjh.add(qlr.getZJH());
										}
										if (!StringHelper.isEmpty(qlr.getZJZLName()) && !list_zjzl.contains(qlr.getZJZLName())) {
											list_zjzl.add(qlr.getZJZLName());
										}
										if (!StringHelper.isEmpty(qlr.getBDCQZH()) && !list_qzh.contains(qlr.getBDCQZH())) {
											list_qzh.add(qlr.getBDCQZH());
										}else{
											list_qzh.add(ql.getBDCQZH());
										}
										if (!StringHelper.isEmpty(qlr.getGYFSName()) && !list_gyqk.contains(qlr.getGYFSName())) {
											list_gyqk.add(qlr.getGYFSName());
										}
										List<BDCS_ZS_GZ> zss = dao.getDataList(BDCS_ZS_GZ.class, " id in (SELECT ZSID FROM BDCS_QDZR_GZ WHERE QLRID='" + qlr.getId() + "')");
										if (zss != null && zss.size() > 0) {
											if (!StringHelper.isEmpty(zss.get(0).getZSBH()) && !list_zsbh.contains(zss.get(0).getZSBH())) {
												list_zsbh.add(zss.get(0).getZSBH());
											}
											
											if (!StringHelper.isEmpty(zss.get(0).getZSDATA())) {
												onezsinfo= zss.get(0).getZSDATA();
											}
										}
									}
								}
								
							}else{
								if (sqrlist.size() > 0) {
									for (BDCS_SQR sqr : sqrlist) {
										if (sqr.getSQRLB().equals("2") && !ywrlist.contains(sqr.getSQRXM())) {
											ywrlist.add(sqr.getSQRXM());
											ywrzjhlist.add(sqr.getZJH());
											ywrzjzllist.add(sqr.getZJLX());
										}
									}
								}
								if (!StringHelper.isEmpty(ql.getBDCQZH()) && !list_qzh.contains(ql.getBDCQZH())) {
									list_qzh.add(ql.getBDCQZH());
								}
								
							}
							 
							//判断义务人为空就维护下
							if(ywrlist.size()>0){
								
							}else{

								if (sqrlist.size() > 0) {
									for (BDCS_SQR sqr : sqrlist) {
										if (sqr.getSQRLB().equals("2") && !ywrlist.contains(sqr.getSQRXM())) {
											ywrlist.add(sqr.getSQRXM());
											ywrzjhlist.add(sqr.getZJH());
											ywrzjzllist.add(sqr.getZJLX());
										}
									}
								}
								if (!StringHelper.isEmpty(ql.getBDCQZH()) && !list_qzh.contains(ql.getBDCQZH())) {
									list_qzh.add(ql.getBDCQZH());
								}
								
							}
							
							
							 
							String str_qlr = StringHelper.formatList(list_qlr, "、");
							String str_zjh = StringHelper.formatList(list_zjh, "、");
							String str_zjzl = StringHelper.formatList(list_zjzl, "、");
							String str_qzh = StringHelper.formatList(list_qzh, "、");
							String str_zsbh = StringHelper.formatList(list_zsbh, "、");
							List<Rights> qls_syq = null;
							if (BDCDYLX.YCH.equals(bdcdylx)) {
								qls_syq = RightsTools.loadRightsByCondition(DJDYLY.XZ, " QLLX IN ('1','2','3','4','5','6','7','8','23') AND DJDYID='" + djdyid + "'");
							} else {
								qls_syq = RightsTools.loadRightsByCondition(DJDYLY.XZ, " QLLX IN ('1','2','3','4','5','6','7','8','23') AND DJDYID='" + djdyid + "' AND (DJLX IS NULL OR DJLX<>'700')");
							}
							
							sqrlist = dao.getDataList(BDCS_SQR.class, "XMBH='" + xmxx.getId() + "' AND SQRLB='1' ");
							if (sqrlist.size() > 0) {
								for (BDCS_SQR holder : sqrlist) {

									if (!StringHelper.isEmpty(holder.getSQRXM())) {
										list_dyqlr.add(holder.getSQRXM());
									}
									if (!StringHelper.isEmpty(holder.getZJH()) && !list_zjh.contains(holder.getZJH())) {
										list_dyqzjh.add(holder.getZJH());
									}
								}
							}
							
							HashSet<String> hset  =   new  HashSet<String>(list_qlr); 
							list_qlr.clear(); 
							list_qlr.addAll(hset);
							String str_dyqr_syq = StringHelper.formatList(list_dyqlr, "、");
							List<RightsHolder> qlrs_syq=new ArrayList<RightsHolder>();
							if (qls_syq != null && qls_syq.size() > 0) {
								Rights ql_syq = qls_syq.get(0);
								 qlrs_syq = RightsHolderTools.loadRightsHolders(DJDYLY.XZ, ql_syq.getId());
								List<String> list_qlr_syq = new ArrayList<String>();
								List<String> list_zjh_syq = new ArrayList<String>();
								List<String> list_zjzl_syq = new ArrayList<String>();
								List<String> list_qzh_syq = new ArrayList<String>();
								List<String> list_qzh_syq_zsbh = new ArrayList<String>();
								if (qlrs_syq != null && qlrs_syq.size() > 0) {
									for (RightsHolder qlr : qlrs_syq) {
										if (!StringHelper.isEmpty(qlr.getQLRMC())) {
											list_qlr_syq.add(qlr.getQLRMC());
											if (ywrlist.contains(qlr.getQLRMC())) {
												flag = true;
											}
											if (!StringHelper.isEmpty(qlr.getZJH()) && !list_zjh_syq.contains(qlr.getZJH())) {
												list_zjh_syq.add(qlr.getZJH());
											}
											if (!StringHelper.isEmpty(qlr.getZJZLName()) && !list_zjzl_syq.contains(qlr.getZJZLName())) {
												list_zjzl_syq.add(qlr.getZJZLName());
											}
											if (!StringHelper.isEmpty(qlr.getBDCQZH()) && !list_qzh_syq.contains(qlr.getBDCQZH())) {
												list_qzh_syq.add(qlr.getBDCQZH());
											}else{
												list_qzh_syq.add(ql.getBDCQZH());
											}
											List<BDCS_ZS_XZ> zss = dao.getDataList(BDCS_ZS_XZ.class, " id in (SELECT ZSID FROM BDCS_QDZR_XZ WHERE QLRID='" + qlr.getId() + "')");
											if (zss != null && zss.size() > 0) {
												if (!StringHelper.isEmpty(zss.get(0).getZSBH()) && !list_qzh_syq_zsbh.contains(zss.get(0).getZSBH())) {
													list_qzh_syq_zsbh.add(zss.get(0).getZSBH());
												}
											}
										}
									}
								}
								if (!flag) {
									list_qlr_syq = ywrlist;
									list_zjh_syq = ywrzjhlist;
									list_zjzl_syq = ywrzjzllist;
								}
								if(DJLX.ZXDJ.Value.equals(djlx)||DJLX.CSDJ.Value.equals(djlx)){
									list_qlr_syq=ywrlist;
									list_zjh_syq = ywrzjhlist;
									list_zjzl_syq = ywrzjzllist;
								}else{
									 
									List<String> lastqlr = new ArrayList<String>();
									List<String> lastzjh = new ArrayList<String>();
									List<String> lastzjzl = new ArrayList<String>();
									for(RightsHolder qlr:qlrs_syq){
										if(qlr.equals(str_qlr)){
											
										}else{
											String oneqlr = null;
											String onezjh = null;
											String onezjzl = null;
											if(!StringHelper.isEmpty(qlr.getQLRMC())){
											   oneqlr=qlr.getQLRMC();
											}
											if(!StringHelper.isEmpty(qlr.getZJH())){
												 onezjh=qlr.getZJH();
											}
											if(!StringHelper.isEmpty(qlr.getZJZLName())){
												 onezjzl=qlr.getZJZLName();
											}
											lastqlr.add(oneqlr);
											lastzjh.add(onezjh);
											lastzjzl.add(onezjh);
										}
									}
									list_qlr_syq=lastqlr;
									list_zjh_syq=lastzjh;
									list_zjzl_syq=lastzjh;
								}
								String str_qlr_syq = StringHelper.formatList(list_qlr_syq, "、");
								String str_zjh_syq = StringHelper.formatList(list_zjh_syq, "、");
								String str_zjzl_syq = StringHelper.formatList(list_zjzl_syq, "、");
								String str_qzh_syq = StringHelper.formatList(list_qzh_syq, "、");
								String str_qzh_syq_zsbh = StringHelper.formatList(list_qzh_syq_zsbh, "、");
							
								ql_info.put("DYR", str_qlr_syq);
								ql_info.put("SYQR", str_qlr_syq);
								ql_info.put("DYRZH", str_zjh_syq);
								ql_info.put("DYRZJLX", str_zjzl_syq);
								ql_info.put("QZH", str_qzh_syq);
								ql_info.put("QZHZSBH", str_qzh_syq_zsbh);
								if (!dy_info.containsKey("SYQR")) {
									ql_info.put("SYQR", str_qlr_syq);
									dy_info.put("SYQR", str_qlr_syq);
									dy_info.put("ZJH", str_zjh_syq);
									dy_info.put("ZJZL", str_zjzl_syq);
									dy_info.put("QZH", str_qzh_syq);
									dy_info.put("QZHZSBH", str_qzh_syq_zsbh);
									
									
									if(!StringHelper.isEmpty(onezsinfo)){
										HashMap<String, String> zsmap=toHashMap(onezsinfo);
										dy_info.put("GYQK", zsmap.get("gyqk"));
										dy_info.put("QLXZ", zsmap.get("qlxz"));
										dy_info.put("SYQX", zsmap.get("syqx"));
										dy_info.put("YT", zsmap.get("yt"));
										dy_info.put("MJ", zsmap.get("mj"));
										dy_info.put("QLQTZK", zsmap.get("qt"));
										dy_info.put("FJ", zsmap.get("fj"));
									}
									 
								}
							}else{
								
								String ywr = StringHelper.formatList(ywrlist, "、");
								String ywrzjh = StringHelper.formatList(ywrzjhlist, "、");
								String ywrzjzl = StringHelper.formatList(ywrzjzllist, "、");
								if(DJLX.ZXDJ.Value.equals(djlx)){
									ql_info.put("SYQR", ywr);
									ql_info.put("DYR", ywr);
								}else{
									ql_info.put("SYQR", ywr);
									ql_info.put("DYR", ywr);
								}
								ql_info.put("ZJH", ywrzjh);
								ql_info.put("ZJZL", ywrzjzl);
								ql_info.put("QZH", str_qzh);
								ql_info.put("QZHZSBH", str_zsbh);
								if (!dy_info.containsKey("SYQR")) {
									if(DJLX.ZXDJ.Value.equals(djlx)){
										ql_info.put("SYQR", ywr);
										dy_info.put("SYQR", ywr);
										 
									}else{
										ql_info.put("SYQR", ywr);
										dy_info.put("SYQR", ywr);
									}
									dy_info.put("ZJH", ywrzjh);
									dy_info.put("ZJZL", ywrzjzl);
									dy_info.put("ZJZL", ywrzjzl);
									dy_info.put("QZH", str_qzh);
									dy_info.put("QZHZSBH", str_zsbh);
									 
									if(!StringHelper.isEmpty(onezsinfo)){
										HashMap<String, String> zsmap=toHashMap(onezsinfo);
										dy_info.put("GYQK", zsmap.get("gyqk"));
										dy_info.put("QLXZ", zsmap.get("qlxz"));
										dy_info.put("SYQX", zsmap.get("syqx"));
										dy_info.put("YT", zsmap.get("yt"));
										dy_info.put("MJ", zsmap.get("mj"));
										dy_info.put("QLQTZK", zsmap.get("qt"));
										dy_info.put("FJ", zsmap.get("fj"));
									}
									
								}
							}
							if (StringHelper.isEmpty(str_qlr)) {
								str_qlr = fsql.getDYR();
							}
							 
							if(DJLX.ZXDJ.Value.equals(djlx)||DJLX.CSDJ.Value.equals(djlx)){
								ql_info.put("DYQR", str_dyqr_syq);
							}else{
								ql_info.put("DYQR", str_qlr);
							}
							
							ql_info.put("DYQRZH", str_zjh);
							List<String> listzmh=new ArrayList<String>();
							for(String qzh:list_qzh){
								if(!listzmh.contains(qzh)){
									listzmh.add(qzh);
								}else{
									continue;
								}
							}
							String str_zmh = StringHelper.formatList(listzmh, "、");
							ql_info.put("ZMH", str_zmh);
							ql_info.put("ZMHZSBH", str_zsbh);
							rightsinfo.setZMH(str_zmh);
							rightsinfo.setZMHZSBH(str_zsbh);
							ql_info.put("FJ", ql.getFJ());
							ql_info.put("DBR", ql.getDBR());
							ql_info.put("DJSJ", ql.getDJSJ());
							rightsinfo.setDJSJ(StringHelper.FormatByDatatype(ql.getDJSJ()));
							ql_info.put("BDCDYID", str_bdcdyid);
							ql_info.put("BDCDYH", unit.getBDCDYH());
							ql_info.put("QLQSSJ", ql.getQLQSSJ());
							ql_info.put("QLJSSJ", ql.getQLJSSJ());
							rightsinfo.setQLQSSJ(StringHelper.FormatByDatatype(ql.getQLQSSJ()));
							rightsinfo.setQLJSSJ(StringHelper.FormatByDatatype(ql.getQLJSSJ()));
							ql_info.put("ZL", unit.getZL());
							ql_info.put("BDCDYLXMC", BDCDYLX.initFrom(djdy.getBDCDYLX()).Bdclx);
							if (fsql != null) {
								ql_info.put("ZXDBR", fsql.getZXDBR());
								ql_info.put("ZXSJ", fsql.getZXSJ());
								ql_info.put("DYFS", fsql.getDYFS());
								ql_info.put("BDBZQSE", fsql.getBDBZZQSE());
								ql_info.put("ZGZQSE", fsql.getZGZQSE());
								rightsinfo.setZXSJ(StringHelper.FormatByDatatype(fsql.getZXSJ()));
								if(!StringHelper.isEmpty(fsql.getBDBZZQSE())){
									rightsinfo.setBDBZQSE(fsql.getBDBZZQSE().toString());
								}
								if(!StringHelper.isEmpty(fsql.getZGZQSE())){
									rightsinfo.setZGZQSE(fsql.getZGZQSE().toString());
								}
								
							}
							ql_info.put("LYXMBH", lyxmbh);
							rightsinfo.setLYXMBH(lyxmbh);
							Other_List.add(ql_info);
                            RightsInfo_List.add(rightsinfo);
						} else if (DJLX.CFDJ.Value.equals(djlx)||DJLX.YYDJ.Value.equals(djlx)) {
							RightsInfo rightsinfo=new RightsInfo();
							rightsinfo.setDJLX(djlx);
							rightsinfo.setQLLX(qllx);
							if(sqrlist.size()>0){
								for(BDCS_SQR sqr:sqrlist){
									if(sqr.getSQRLB().equals("1")){
										rightsinfo.setQLR(sqr.getSQRXM());
										rightsinfo.setQLRZJH(sqr.getZJH());
										rightsinfo.setQLRZJLX(sqr.getZJLX());
									}else{
										rightsinfo.setYWR(sqr.getSQRXM());
										rightsinfo.setYWRZJH(sqr.getZJH());
										rightsinfo.setYWRZJLX(sqr.getZJLX());
									}
								}
							}
							HashMap<String, Object> ql_info = new HashMap<String, Object>();
							List<Rights> qls_syq = null;
							if (BDCDYLX.YCH.equals(bdcdylx)) {
								qls_syq = RightsTools.loadRightsByCondition(DJDYLY.XZ, " QLLX IN ('1','2','3','4','5','6','7','8') AND DJDYID='" + djdyid + "'");
							} else {
								qls_syq = RightsTools.loadRightsByCondition(DJDYLY.XZ, " QLLX IN ('1','2','3','4','5','6','7','8') AND DJDYID='" + djdyid + "' AND (DJLX IS NULL OR DJLX<>'700')");
							}
							if (qls_syq != null && qls_syq.size() > 0) {
								Rights ql_syq = qls_syq.get(0);
								List<RightsHolder> qlrs_syq = RightsHolderTools.loadRightsHolders(DJDYLY.XZ, ql_syq.getId());
								List<String> list_qlr_syq = new ArrayList<String>();
								List<String> list_zjh_syq = new ArrayList<String>();
								List<String> list_zjzl_syq = new ArrayList<String>();
								List<String> list_qzh_syq = new ArrayList<String>();
								List<String> list_gyqk = new ArrayList<String>();
								List<String> list_qzh_syq_zsbh = new ArrayList<String>();
								String onezsinfo = null;
								if (qlrs_syq != null && qlrs_syq.size() > 0) {
									for (RightsHolder qlr : qlrs_syq) {
										if (!StringHelper.isEmpty(qlr.getQLRMC())) {
											list_qlr_syq.add(qlr.getQLRMC());
											if (!StringHelper.isEmpty(qlr.getZJH()) && !list_zjh_syq.contains(qlr.getZJH())) {
												list_zjh_syq.add(qlr.getZJH());
											}
											if (!StringHelper.isEmpty(qlr.getZJZLName()) && !list_zjzl_syq.contains(qlr.getZJZLName())) {
												list_zjzl_syq.add(qlr.getZJZLName());
											}
											if (!StringHelper.isEmpty(qlr.getBDCQZH()) && !list_qzh_syq.contains(qlr.getBDCQZH())) {
												list_qzh_syq.add(qlr.getBDCQZH());
											}else{
												list_qzh_syq.add(ql.getBDCQZH());
											}
											if (!StringHelper.isEmpty(qlr.getGYFSName()) && !list_gyqk.contains(qlr.getGYFSName())) {
												list_gyqk.add(qlr.getGYFSName());
											}
											List<BDCS_ZS_XZ> zss = dao.getDataList(BDCS_ZS_XZ.class, " id in (SELECT ZSID FROM BDCS_QDZR_XZ WHERE QLRID='" + qlr.getId() + "')");
											if (zss != null && zss.size() > 0) {
												if (!StringHelper.isEmpty(zss.get(0).getZSBH()) && !list_qzh_syq_zsbh.contains(zss.get(0).getZSBH())) {
													list_qzh_syq_zsbh.add(zss.get(0).getZSBH());
												}
												
												if (!StringHelper.isEmpty(zss.get(0).getZSDATA())) {
													onezsinfo= zss.get(0).getZSDATA();
												}

											}
										}
									}
								}
								String str_qlr_syq = StringHelper.formatList(list_qlr_syq, "、");
								String str_zjh_syq = StringHelper.formatList(list_zjh_syq, "、");
								String str_zjzl_syq = StringHelper.formatList(list_zjzl_syq, "、");
								String str_qzh_syq = StringHelper.formatList(list_qzh_syq, "、");
								String str_qzh_syq_zsbh = StringHelper.formatList(list_qzh_syq_zsbh, "、");
								ql_info.put("SYQR", str_qlr_syq);
								ql_info.put("ZJH", str_zjh_syq);
								ql_info.put("ZJZL", str_zjzl_syq);
								List<String> listzmh=new ArrayList<String>();
								for(String qzh:list_qzh_syq){
									if(!listzmh.contains(qzh)){
										listzmh.add(qzh);
									}else{
										continue;
									}
								}
								String str_zmh = StringHelper.formatList(listzmh, "、");
								
								ql_info.put("QZH", str_zmh);
								ql_info.put("QZHZSBH", str_qzh_syq_zsbh);
								if (!dy_info.containsKey("SYQR")) {
									dy_info.put("SYQR", str_qlr_syq);
									dy_info.put("ZJH", str_zjh_syq);
									dy_info.put("ZJZL", str_zjzl_syq);
									dy_info.put("QZH", str_zmh);
									dy_info.put("QZHZSBH", str_qzh_syq_zsbh);
									if(!StringHelper.isEmpty(onezsinfo)){
										HashMap<String, String> zsmap=toHashMap(onezsinfo);
										dy_info.put("GYQK", zsmap.get("GYQK"));
										dy_info.put("QLXZ", zsmap.get("QLXZ"));
										dy_info.put("SYQX", zsmap.get("SYQX"));
										dy_info.put("YT", zsmap.get("YT"));
										dy_info.put("MJ", zsmap.get("MJ"));
										dy_info.put("QLQTZK", zsmap.get("QLQTZK"));
										dy_info.put("FJ", zsmap.get("FJ"));
									}
									
								 
								}
							}else{
								List<String> list_qzh_syq = new ArrayList<String>();
								if (sqrlist.size() > 0) {
									for (BDCS_SQR sqr : sqrlist) {
										if (sqr.getSQRLB().equals("2") && !ywrlist.contains(sqr.getSQRXM())) {
											ywrlist.add(sqr.getSQRXM());
											ywrzjhlist.add(sqr.getZJH());
											ywrzjzllist.add(sqr.getZJLX());
										}
									}
								}
								if (!StringHelper.isEmpty(ql.getBDCQZH()) && !list_qzh_syq.contains(ql.getBDCQZH())) {
									list_qzh_syq.add(ql.getBDCQZH());
								}
								
								String ywr = StringHelper.formatList(ywrlist, "、");
								String ywrzjh = StringHelper.formatList(ywrzjhlist, "、");
								String ywrzjzl = StringHelper.formatList(ywrzjzllist, "、");
								ql_info.put("SYQR", ywr);
								ql_info.put("ZJH", ywrzjh);
								ql_info.put("ZJZL", ywrzjzl);
								dy_info.put("SYQR", ywr);
								dy_info.put("ZJH", ywrzjh);
							}
						 
							
 
							ql_info.put("FJ", ql.getFJ());
							ql_info.put("CFDBR", ql.getDBR());
							ql_info.put("DJSJ", ql.getDJSJ());
							rightsinfo.setDJSJ(StringHelper.FormatByDatatype(ql.getDJSJ()));
							ql_info.put("BDCDYID", str_bdcdyid);
							ql_info.put("BDCDYH", unit.getBDCDYH());
							ql_info.put("QLQSSJ", ql.getQLQSSJ());
							ql_info.put("QLJSSJ", ql.getQLJSSJ());
							rightsinfo.setQLQSSJ(StringHelper.FormatByDatatype(ql.getQLQSSJ()));
							rightsinfo.setQLJSSJ(StringHelper.FormatByDatatype(ql.getQLJSSJ()));
							ql_info.put("ZL", unit.getZL());
							ql_info.put("BDCDYLXMC", BDCDYLX.initFrom(djdy.getBDCDYLX()).Bdclx);
							if (fsql != null) {
								ql_info.put("JFDBR", fsql.getZXDBR());
								ql_info.put("JFSJ", fsql.getZXSJ());
								rightsinfo.setZXSJ(StringHelper.FormatByDatatype(fsql.getZXSJ()));
								ql_info.put("DYFS", fsql.getDYFS());
								ql_info.put("CFJG", fsql.getCFJG());
								ql_info.put("CFLX", ConstHelper.getNameByValue("CFLX", fsql.getCFLX()));
								ql_info.put("CFWH", fsql.getCFWH());
								ql_info.put("CFWJ", fsql.getCFWJ());
								ql_info.put("LHSX", fsql.getLHSX());
								ql_info.put("CFSJ", fsql.getCFSJ());
								ql_info.put("CFFW", fsql.getCFFW());
								ql_info.put("JFWH", fsql.getJFWH());
								ql_info.put("JFWJ", fsql.getJFWJ());
								rightsinfo.setJFDBR(fsql.getZXDBR());
								rightsinfo.setJFSJ(StringHelper.FormatByDatatype(fsql.getZXSJ()));
								rightsinfo.setCFJG(fsql.getCFJG());
								rightsinfo.setCFLX(ConstHelper.getNameByValue("CFLX", fsql.getCFLX()));
								rightsinfo.setCFWH(fsql.getCFWH());
								rightsinfo.setCFWJ(fsql.getCFWJ());
								if(!StringHelper.isEmpty(fsql.getLHSX())){
									rightsinfo.setLHSX(fsql.getLHSX().toString());
								}
								
								rightsinfo.setCFSJ(StringHelper.FormatByDatatype(fsql.getCFSJ()));
								rightsinfo.setCFFW(fsql.getCFFW());
								rightsinfo.setJFWH(fsql.getJFWH());
								rightsinfo.setJFWJ(fsql.getJFWJ());
							}
							ql_info.put("LYXMBH", lyxmbh);
							rightsinfo.setLYXMBH(lyxmbh);
							Limit_List.add(ql_info);
							RightsInfo_List.add(rightsinfo);
						} else if (DJLX.YGDJ.Value.equals(djlx)) {
							RightsInfo rightsinfo=new RightsInfo();
							rightsinfo.setDJLX(djlx);
							rightsinfo.setQLLX(qllx);
							if(sqrlist.size()>0){
								for(BDCS_SQR sqr:sqrlist){
									if(sqr.getSQRLB().equals("1")){
										rightsinfo.setQLR(sqr.getSQRXM());
										rightsinfo.setQLRZJH(sqr.getZJH());
										rightsinfo.setQLRZJLX(sqr.getZJLX()); 
									}else{
										rightsinfo.setYWR(sqr.getSQRXM());
										rightsinfo.setYWRZJH(sqr.getZJH());
										rightsinfo.setYWRZJLX(sqr.getZJLX());
									}
								}
							}
							HashMap<String, Object> ql_info = new HashMap<String, Object>();
							List<RightsHolder> qlrs = RightsHolderTools.loadRightsHolders(DJDYLY.GZ, ql.getId());
							List<String> list_qlr = new ArrayList<String>();
							List<String> list_zjh = new ArrayList<String>();
							List<String> list_zjzl = new ArrayList<String>();
							List<String> list_qzh = new ArrayList<String>();
							List<String> list_gyqk = new ArrayList<String>();
							List<String> list_zsbh = new ArrayList<String>();
							String onezsinfo = null;
							if (qlrs != null && qlrs.size() > 0) {
								for (RightsHolder qlr : qlrs) {
									if (!StringHelper.isEmpty(qlr.getQLRMC())) {
										list_qlr.add(qlr.getQLRMC());
										if (!StringHelper.isEmpty(qlr.getZJH()) && !list_zjh.contains(qlr.getZJH())) {
											list_zjh.add(qlr.getZJH());
										}
										if (!StringHelper.isEmpty(qlr.getZJZLName()) && !list_zjzl.contains(qlr.getZJZLName())) {
											list_zjzl.add(qlr.getZJZLName());
										}
										if (!StringHelper.isEmpty(qlr.getBDCQZH()) && !list_qzh.contains(qlr.getBDCQZH())) {
											list_qzh.add(qlr.getBDCQZH());
										}else{
											list_qzh.add(ql.getBDCQZH());
										}
										if (!StringHelper.isEmpty(qlr.getGYFSName()) && !list_gyqk.contains(qlr.getGYFSName())) {
											list_gyqk.add(qlr.getGYFSName());
										}
										List<BDCS_ZS_GZ> zss = dao.getDataList(BDCS_ZS_GZ.class, " id in (SELECT ZSID FROM BDCS_QDZR_GZ WHERE QLRID='" + qlr.getId() + "')");
										if (zss != null && zss.size() > 0) {
											if (!StringHelper.isEmpty(zss.get(0).getZSBH()) && !list_zsbh.contains(zss.get(0).getZSBH())) {
												list_zsbh.add(zss.get(0).getZSBH());
											}
											
											if (!StringHelper.isEmpty(zss.get(0).getZSDATA())) {
												onezsinfo= zss.get(0).getZSDATA();
											}

										}
									}
								}
							}
							if (sqrlist.size() > 0) {
								for (BDCS_SQR sqr : sqrlist) {
									if (sqr.getSQRLB().equals("2") && !ywrlist.contains(sqr.getSQRXM())) {
										ywrlist.add(sqr.getSQRXM());
										ywrzjhlist.add(sqr.getZJH());
										ywrzjzllist.add(sqr.getZJLX());
									}
								}
							}
							String str_qlr = StringHelper.formatList(list_qlr, "、");
							String str_zjh = StringHelper.formatList(list_zjh, "、");
							String str_zjzl = StringHelper.formatList(list_zjzl, "、");
							String str_qzh = StringHelper.formatList(list_qzh, "、");
							String str_zsbh = StringHelper.formatList(list_zsbh, "、");
							String str_ywrlist = StringHelper.formatList(ywrlist, "、");
							String str_ywrzjhlist= StringHelper.formatList(ywrzjhlist, "、");
							String str_ywrzjzllist= StringHelper.formatList(ywrzjzllist, "、");
							List<String> listzmh=new ArrayList<String>();
							for(String qzh:list_qzh){
								if(!listzmh.contains(qzh)){
									listzmh.add(qzh);
								}else{
									continue;
								}
							}
							String str_zmh = StringHelper.formatList(listzmh, "、");
							if (!dy_info.containsKey("SYQR")) {
								dy_info.put("SYQR", str_qlr);
								dy_info.put("ZJH", str_ywrzjhlist);
								dy_info.put("ZJZL", str_ywrzjzllist);
								dy_info.put("QZH", str_zmh);
								dy_info.put("QZHZSBH", str_zsbh);
								if(!StringHelper.isEmpty(onezsinfo)){
									HashMap<String, String> zsmap=toHashMap(onezsinfo);
									dy_info.put("GYQK", zsmap.get("gyqk"));
									dy_info.put("QLXZ", zsmap.get("qlxz"));
									dy_info.put("SYQX", zsmap.get("syqx"));
									dy_info.put("YT", zsmap.get("yt"));
									dy_info.put("MJ", zsmap.get("mj"));
									dy_info.put("QLQTZK", zsmap.get("qt"));
									dy_info.put("FJ", zsmap.get("fj"));
								}
						
							}
							
							ql_info.put("YGQLR", str_qlr);
							ql_info.put("QLRZJH", str_zjh);
							ql_info.put("ZJZL", str_zjzl);
							ql_info.put("ZMH", str_zmh);
							ql_info.put("ZMHZSBH", str_zsbh);
							rightsinfo.setZMH(str_zmh);
							rightsinfo.setZMHZSBH(str_zsbh);
							ql_info.put("FJ", ql.getFJ());
							ql_info.put("DBR", ql.getDBR());
							ql_info.put("DJSJ", ql.getDJSJ());
							rightsinfo.setDJSJ(StringHelper.FormatByDatatype(ql.getDJSJ()));
							ql_info.put("BDCDYID", str_bdcdyid);
							ql_info.put("BDCDYH", unit.getBDCDYH());
							ql_info.put("QLQSSJ", ql.getQLQSSJ());
							ql_info.put("QLJSSJ", ql.getQLJSSJ());
							rightsinfo.setQLQSSJ(StringHelper.FormatByDatatype(ql.getQLQSSJ()));
							rightsinfo.setQLJSSJ(StringHelper.FormatByDatatype(ql.getQLJSSJ()));
							ql_info.put("ZL", unit.getZL());
							ql_info.put("BDCDYLXMC", BDCDYLX.initFrom(djdy.getBDCDYLX()).Bdclx);
							if (fsql != null) {
								ql_info.put("ZXDBR", fsql.getZXDBR());
								ql_info.put("ZXSJ", fsql.getZXSJ());
								rightsinfo.setZXSJ(StringHelper.FormatByDatatype(fsql.getZXSJ()));
								ql_info.put("YGDJZL", ConstHelper.getNameByValue("YGDJZL", fsql.getYGDJZL()));
								ql_info.put("YGYWR", fsql.getYWR());
								ql_info.put("YWRZJH", fsql.getYWRZJH());
							}
							ql_info.put("LYXMBH", lyxmbh);
							rightsinfo.setLYXMBH(lyxmbh);
							Notice_List.add(ql_info);
							RightsInfo_List.add(rightsinfo);
						}
					}
				}
				if(RightsInfo_List.size()>0){
					dy_info.put("RightsInfo", RightsInfo_List);
					 
				}else{
					dy_info.put("RightsInfo", "");
				}
				
				Common_List.add(dy_info);
			}
		}
		//整合数据
		info.put("XMInfo", XM_List);
		info.put("CommonInfo", Common_List);
		info.put("OwnerInfo", Owner_List);
		info.put("MortgageInfo", Other_List);
		info.put("LimitInfo", Limit_List);
		info.put("NoticeInfo", Notice_List);
		
		
		return info;
	}
	
	 private static HashMap<String, String> toHashMap(Object object)  
	   {  
	       HashMap<String, String> data = new HashMap<String, String>();  
	       // 将json字符串转换成jsonObject  
	       JSONObject jsonObject = JSONObject.fromObject(object);  
	       Iterator it = jsonObject.keys();  
	       // 遍历jsonObject数据，添加到Map对象  
	       while (it.hasNext())  
	       {  
	           String key = String.valueOf(it.next());  
	           if(!StringHelper.isEmpty(jsonObject.get(key))&& !jsonObject.get(key).toString().equals("null")){
	        	   String value = (String) jsonObject.get(key);  
		           data.put(key, value);  
	           }else{
	        	   data.put(key, ""); 
	           }
	           
	       }  
	       return data;  
	   }
	
}
