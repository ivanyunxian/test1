package com.supermap.realestate.registration.ViewClass;

import com.supermap.realestate.registration.model.*;
import com.supermap.realestate.registration.model.interfaces.House;
import com.supermap.realestate.registration.model.interfaces.RealUnit;
import com.supermap.realestate.registration.model.interfaces.Rights;
import com.supermap.realestate.registration.model.interfaces.RightsHolder;
import com.supermap.realestate.registration.tools.RightsHolderTools;
import com.supermap.realestate.registration.tools.RightsTools;
import com.supermap.realestate.registration.tools.UnitTools;
import com.supermap.realestate.registration.util.*;
import com.supermap.realestate.registration.util.ConstValue.BDCDYLX;
import com.supermap.realestate.registration.util.ConstValue.DJDYLY;
import com.supermap.realestate.registration.util.ConstValue.DJLX;
import com.supermap.realestate.registration.util.ConstValue.QLLX;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import com.supermap.wisdombusiness.framework.model.User;
import com.supermap.wisdombusiness.workflow.model.Wfd_Prodef;
import com.supermap.wisdombusiness.workflow.model.Wfi_ProInst;
import com.supermap.wisdombusiness.workflow.service.wfd.SmHoliday;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 不动产登记受理单/询问笔录/收费明细单信息
 * 
 * @author 俞学斌
 *
 */
public class DJInfo {
	
	/** 受理人员 */
	private String SLRY;
	
	private String SLRYID;

	/** 申请人 */
	private String SQR;

	/** 申请人联系电话 */
	private String LXDH;

	/** 不动产坐落 */
	private String BDCZL;

	/** 不动产类型 */
	private String BDCLX;
	
	/** 债权履行期限*/
	private String ZQLXQX;
	
	/** 债权履行金额*/
	private String DBJE;
	
	private String DLRXM;
	
	/** 是否房屋转移登记 */
	private String B200031;
	
	/** 房屋土地登记 */
	private String FWTDDJ;
	
	/** 房屋分摊土地面积 */
	private String FWFTTDMJ;
	
	/** 房屋土地使用权起始时间 */
	private String FWQLQSSJ;
	
	/** 房屋土地使用权结束时间 */
	private String FWQLJSSJ;
	
	private String CASENUM;
	
	private String BDCQZH;

	private String FWTDYT;

	private List<String> ExceptSF;
	
	public List<String> getExceptSF() {
		return ExceptSF;
	}

	public void setExceptSF(List<String> exceptSF) {
		ExceptSF = exceptSF;
	}

	public String getBDCQZH() {
		return BDCQZH;
	}

	public void setBDCQZH(String bDCQZH) {
		BDCQZH = bDCQZH;
	}
	
	public String getCASENUM() {
		return CASENUM;
	}

	public void setCASENUM(String cASENUM) {
		CASENUM = cASENUM;
	}
	
	public String getFWTDDJ() {
		return FWTDDJ;
	}

	public void setFWTDDJ(String fWTDDJ) {
		FWTDDJ = fWTDDJ;
	}
	
	public String getFWFTTDMJ() {
		return FWFTTDMJ;
	}

	public void setFWFTTDMJ(String fWFTTDMJ) {
		FWFTTDMJ = fWFTTDMJ;
	}

	public String getFWQLQSSJ() {
		return FWQLQSSJ;
	}

	public void setFWQLQSSJ(String fWQLQSSJ) {
		FWQLQSSJ = fWQLQSSJ;
	}

	public String getFWQLJSSJ() {
		return FWQLJSSJ;
	}

	public void setFWQLJSSJ(String fWQLJSSJ) {
		FWQLJSSJ = fWQLJSSJ;
	}

	public String getB200031() {
		return B200031;
	}

	public void setB200031(String b200031) {
		B200031 = b200031;
	}

	public String getFWTDYT() {
		return FWTDYT;
	}

	public void setFWTDYT(String FWTDYT) {
		this.FWTDYT = FWTDYT;
	}

	/*
	 * 收费金额总计
	 */
	private String SSJEZJ;
	
	public String getSSJEZJ() {
		return SSJEZJ;
	}

	public void setSSJEZJ(String sSJEZJ) {
		SSJEZJ = sSJEZJ;
	}

	private String QLR;
	
	public String getQLR() {
		return QLR;
	}

	public void setQLR(String qLR) {
		QLR = qLR;
	}
	//权利人-联系电话
	private String qlr_lxdh;
	
	public String getQlr_lxdh() {
		return qlr_lxdh;
	}

	public void setQlr_lxdh(String qlr_lxdh) {
		this.qlr_lxdh = qlr_lxdh;
	}
	
	//申请人——权利人的代理人信息
	private String qlrWithDlr;
	//申请人——义务人的代理人信息
	private String ywrWithDlr;
	
	public String getQlrWithDlr() {
		return qlrWithDlr;
	}

	public void setQlrWithDlr(String qlrWithDlr) {
		this.qlrWithDlr = qlrWithDlr;
	}

	public String getYwrWithDlr() {
		return ywrWithDlr;
	}

	public void setYwrWithDlr(String ywrWithDlr) {
		this.ywrWithDlr = ywrWithDlr;
	}

	public String getDLRXM() {
		return DLRXM;
	}

	public void setDLRXM(String dLRXM) {
		DLRXM = dLRXM;
	}
	

	public String getZQLXQX() {
		return ZQLXQX;
	}

	public void setZQLXQX(String zQLXQX) {
		ZQLXQX = zQLXQX;
	}

	public String getDBJE() {
		return DBJE;
	}

	public void setDBJE(String dBJE) {
		DBJE = dBJE;
	}

	/** 不动产类型 */
	private List<BDCS_DJSF> SFList;

	/**
	 * 申请人
	 * 
	 * @return
	 */
	public void setSLRY(String slry) {
		SLRY = slry;
	}

	/**
	 * 申请人
	 * 
	 * @return
	 */
	public void setSQR(String sqr) {
		SQR = sqr;
	}

	/**
	 * 申请人联系电话
	 * 
	 * @return
	 */
	public void setLXDH(String lxdh) {
		LXDH = lxdh;
	}

	/**
	 * 不动产坐落
	 * 
	 * @return
	 */
	public void setBDCZL(String bdczl) {
		BDCZL = bdczl;
	}

	/**
	 * 不动产类型
	 * 
	 * @return
	 */
	public void setBDCLX(String bdclx) {
		BDCLX = bdclx;
	}

	/**
	 * 收费列表
	 * 
	 * @return
	 */
	public void setSFList(List<BDCS_DJSF> sflist) {
		SFList = sflist;
	}

	/**
	 * 受理人员
	 * 
	 * @return
	 */
	public String getSLRY() {
		return SLRY;
	}

	/** 
	 * 受理人员ID
	 */
	public String getSLRYID() {
		return SLRYID;
	}

	/** 
	 * 受理人员ID
	 */
	public void setSLRYID(String sLRYID) {
		SLRYID = sLRYID;
	}
	
	/**
	 * 申请人
	 * 
	 * @return
	 */
	public String getSQR() {
		return SQR;
	}

	/**
	 * 申请人联系电话
	 * 
	 * @return
	 */
	public String getLXDH() {
		return LXDH;
	}

	/**
	 * 不动产坐落
	 * 
	 * @return
	 */
	public String getBDCZL() {
		return BDCZL;
	}

	/**
	 * 不动产类型
	 * 
	 * @return
	 */
	public String getBDCLX() {
		return BDCLX;
	}

	/**
	 * 收费列表
	 * 
	 * @return
	 */
	public List<BDCS_DJSF> getSFList() {
		if (SFList == null) {
			SFList = new ArrayList<BDCS_DJSF>();
		}
		return SFList;
	}

	private String BJSJ;
	
	public String getBJSJ() {
		return BJSJ;
	}

	public void setBJSJ(String bJSJ) {
		BJSJ = bJSJ;
	}
	/**
	 * 预告登记审批表
	 * 
	 * @author DreamLi
	 * @time 2015-6-23 20:17
	 * 
	 */
	public static DJInfo Create(String project_id, CommonDao dao,SmHoliday smHoliday) {
		DJInfo b = new DJInfo();
		if (!StringUtils.isEmpty(project_id)) {
			String slry = Global.getCurrentUserName();
			User u = Global.getCurrentUserInfo();
			if (u != null) {
				b.setSLRYID(u.getId());
			}
			b.setSLRY(slry);
			
			// 获取办结时间

			Wfi_ProInst proInst = null;
			if (!project_id.equals("")) {
				List<Wfi_ProInst> proinsts = dao.getDataList(Wfi_ProInst.class,
						"FILE_NUMBER='" + project_id + "' or prolsh='" + project_id + "'");
				if (proinsts != null && proinsts.size() > 0) {
					proInst = proinsts.get(0);
				}
			}
			Wfd_Prodef prodef = dao.get(Wfd_Prodef.class, proInst.getProdef_Id());			
			if (prodef != null) {
				GregorianCalendar theStartTime = new GregorianCalendar();
				theStartTime.setTime(proInst.getProinst_Start());
				if (prodef.getProdef_Time() != null) {
					Date theEndDay = smHoliday.addDateByWorkDay(theStartTime, prodef.getProdef_Time());
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					String bjsj = sdf.format(theEndDay);
					b.setBJSJ(bjsj);
				}
			}
			
			List<BDCS_XMXX> xmxx = dao.getDataList(BDCS_XMXX.class, "PROJECT_ID='" + project_id + "'");
			if (xmxx != null && xmxx.size() > 0) {				
				String xmbh = xmxx.get(0).getId();
				List<BDCS_SQR> list = dao.getDataList(BDCS_SQR.class,
						ProjectHelper.GetXMBHCondition(xmbh) + " ORDER BY SXH");
				StringBuilder sqrXmBuilder = new StringBuilder();// 义务人
				String strsqr_hz = "";
				StringBuilder qlrXmBuilder = new StringBuilder();// 权利人
				StringBuilder sqrDhBuilder = new StringBuilder();
				StringBuilder dlrBuilder = new StringBuilder();
				List<String> listywr = new ArrayList<String>();
				List<String> listqlr = new ArrayList<String>();
				List<String> listqlr_lxdh = new ArrayList<String>();
				List<String> listsqrdh = new ArrayList<String>();
				List<String> listdlrxm = new ArrayList<String>();
				List<String> listdlr_qlr = new ArrayList<String>();//权利人的代理人
				List<String> listdlr_ywr = new ArrayList<String>();//义务人的代理人	
				//sqr显示个数
				String configSQR = ConfigHelper.getNameByValue("SLTZD_DISPLAY_SQR_NUM");
 				Integer qlr_num = 0;
				Integer ywr_num = 0;
				if (configSQR.length() > 0 ) {
					String[] nums = configSQR.split("/");
					if (nums.length > 1) {
						if (nums[0].matches("[0-9]+")) 
							 qlr_num = Integer.parseInt(nums[0]);
						
						if (nums[1].matches("[0-9]+")) 
							ywr_num = Integer.parseInt(nums[1]);
						 
					}else {
						if (nums[0].matches("[0-9]+")) 
							qlr_num = Integer.parseInt(nums[0]);
					}
				}
				Integer qlr_count = qlr_num;
				Integer ywr_count = ywr_num;
				if (list.size() > 0) {
					for (BDCS_SQR sqr : list) {
						if (ConstValue.SQRLB.JF.Value.equals(sqr.getSQRLB()))// 权利人
						{
							if (!StringHelper.isEmpty(sqr.getSQRXM()) && !listqlr.contains(sqr.getSQRXM())) {
								if (qlr_num == 0) {
									listqlr.add(sqr.getSQRXM());
								}else if (qlr_num > 0 ) {
									if (listqlr.size() < qlr_num) {
										listqlr.add(sqr.getSQRXM());
									}else {
										qlr_count ++;
									}
								} 
								
								if (!StringHelper.isEmpty(sqr.getDLRXM()) && !listdlrxm.contains(sqr.getDLRXM())) {
									listdlr_qlr.add(sqr.getDLRXM());
								}
							}
							// 获取权利人电话
							if (!StringHelper.isEmpty(sqr.getLXDH()) && !listqlr_lxdh.contains(sqr.getLXDH())) {
								listqlr_lxdh.add(sqr.getSQRXM() + "(" + sqr.getLXDH() + ")" );
							}
						} else// 义务人
						{
							if (!StringHelper.isEmpty(sqr.getSQRXM()) && !listywr.contains(sqr.getSQRXM())) {
								
								if(ywr_num == 0){
									if (listywr.size() < 3) {
										listywr.add(sqr.getSQRXM());
									} else {
										strsqr_hz = "等";
									}
								}else if (ywr_num > 0 ) { 
									if (listywr.size() < ywr_num) {
										listywr.add(sqr.getSQRXM());
									}else {
										ywr_count ++;
									}
								}
								
								if (!StringHelper.isEmpty(sqr.getDLRXM()) && !listdlrxm.contains(sqr.getDLRXM())) {
									listdlr_ywr.add(sqr.getDLRXM());
								}
							}
						}
						if (!StringHelper.isEmpty(sqr.getLXDH()) && !listsqrdh.contains(sqr.getLXDH())) {
							listsqrdh.add(sqr.getLXDH() + "(" + sqr.getSQRXM() + ")");
						}
						if (!StringHelper.isEmpty(sqr.getDLRXM()) && !listdlrxm.contains(sqr.getDLRXM())) {
							listdlrxm.add(sqr.getDLRXM());
						}
					}
					
					sqrDhBuilder.append(StringHelper.formatList(listsqrdh));
					if(ywr_count == ywr_num){
						sqrXmBuilder.append(StringHelper.formatList(listywr)).append(strsqr_hz);
					}else{
						sqrXmBuilder.append(StringHelper.formatList(listywr)).append("等"+ywr_count+"个");
					}
					if (qlr_num== qlr_count) {
						qlrXmBuilder.append(StringHelper.formatList(listqlr));
					}else {
						qlrXmBuilder.append(StringHelper.formatList(listqlr)).append("等"+qlr_count+"个");
					}
					dlrBuilder.append(StringHelper.formatList(listdlrxm));
					
				}

				String xm = "";
				String xm_qlr_dlr = ""; //权利人及其代理人
				String xm_ywr_dlr = ""; //义务人及其代理人
				if (!StringHelper.isEmpty(qlrXmBuilder.toString())) {
					xm = "权利人：" + qlrXmBuilder.toString() + "; ";
					b.setQLR(qlrXmBuilder.toString());
					if (!StringHelper.isEmpty(StringHelper.formatList(listdlr_qlr))) {
						xm_qlr_dlr = xm + "代理人：" + StringHelper.formatList(listdlr_qlr) + "; ";
					}else {
						xm_qlr_dlr = xm ;
					}
				}
				if (!StringHelper.isEmpty(StringHelper.formatList(listqlr_lxdh))) {
					b.setQlr_lxdh(StringHelper.formatList(listqlr_lxdh));
				}
				if (!StringHelper.isEmpty(sqrXmBuilder.toString())) {
					xm = xm + "义务人：" + sqrXmBuilder.toString() + ";";
					if (!StringHelper.isEmpty(StringHelper.formatList(listdlr_ywr))) {
						xm_ywr_dlr = "义务人：" + sqrXmBuilder.toString() + ";"+ "代理人：" + StringHelper.formatList(listdlr_ywr) + "; ";
					}else {
						xm_ywr_dlr = "义务人：" + sqrXmBuilder.toString() + ";";
					}
				}
				
				b.setSQR(xm);
				b.setQlrWithDlr(xm_qlr_dlr);
				b.setYwrWithDlr(xm_ywr_dlr);
				if (DJLX.CFDJ.Value.equals(xmxx.get(0).getDJLX())) {
					String cfqlr = "";
					List<String> list_cfqlr = new ArrayList<String>();
					List<BDCS_DJDY_GZ> djdys = dao.getDataList(BDCS_DJDY_GZ.class, "XMBH='" + xmbh + "'");
					if (djdys != null && djdys.size() > 0) {
						for (BDCS_DJDY_GZ djdy : djdys) {
							String djdyid = djdy.getDJDYID();
							String bdcdylx = djdy.getBDCDYLX();
							List<Rights> syq_rights = new ArrayList<Rights>();
							if ("032".equals(bdcdylx)) {
								syq_rights = RightsTools.loadRightsByCondition(DJDYLY.XZ,
										"QLLX IN ('4','6','8') AND DJDYID='" + djdyid + "'");
							} else if ("031".equals(bdcdylx)) {
								syq_rights = RightsTools.loadRightsByCondition(DJDYLY.XZ,
										"QLLX IN ('4','6','8') AND (DJLX IS NULL OR DJLX<>'700') AND DJDYID='" + djdyid
												+ "'");
							} else if ("01".equals(bdcdylx) || "02".equals(bdcdylx)) {
								syq_rights = RightsTools.loadRightsByCondition(DJDYLY.XZ,
										"QLLX IN ('1','2','3','5','7') AND (DJLX IS NULL OR DJLX<>'700') AND DJDYID='"
												+ djdyid + "'");
							} else if ("05".equals(bdcdylx)) {
								syq_rights = RightsTools.loadRightsByCondition(DJDYLY.XZ,
										"QLLX IN ('10','11','12') AND (DJLX IS NULL OR DJLX<>'700') AND DJDYID='"
												+ djdyid + "'");
							}
							if (syq_rights != null && syq_rights.size() > 0) {
								boolean b_bdcqzh = false;
								String bdcqzh ="";
								for (Rights syq_right : syq_rights) {
									List<RightsHolder> syq_qlrs = RightsHolderTools.loadRightsHolders(DJDYLY.XZ,
											syq_right.getId());
									if (syq_qlrs != null && syq_qlrs.size() > 0) {
										for (RightsHolder cf_qlr : syq_qlrs) {
											String cf_qlrmc = cf_qlr.getQLRMC();
											String cf_qlrzjh = cf_qlr.getZJH();
											String cf_qlrinfo = StringHelper.formatObject(cf_qlrmc) + "&"
													+ StringHelper.formatObject(cf_qlrzjh);
											if (!list_cfqlr.contains(cf_qlrinfo)) {
												list_cfqlr.add(cf_qlrinfo);
											}
											if (StringHelper.isEmpty(cfqlr)) {
												cfqlr = cf_qlrmc;
											}
										}
									}
									if(!b_bdcqzh){
										b_bdcqzh = true;
										bdcqzh=syq_right.getBDCQZH();
									}else{
										bdcqzh = bdcqzh + "、" + syq_right.getBDCQZH();
									}
								}
								b.setBDCQZH(bdcqzh);
							}
						}
					}

					List<BDCS_FSQL_GZ> fsqls = dao.getDataList(BDCS_FSQL_GZ.class,
							ProjectHelper.GetXMBHCondition(xmbh));
					if (fsqls != null && fsqls.size() > 0) {
						if (!StringHelper.isEmpty(cfqlr) && list_cfqlr != null && list_cfqlr.size() > 1) {
							cfqlr = cfqlr + "等";
						}
						if (QLLX.QTQL.Value.equals(xmxx.get(0).getQLLX())) {
							if (!StringHelper.isEmpty(fsqls.get(0).getCFJG())) {
								String cfjg = "查封机关：" + fsqls.get(0).getCFJG() + "";
								if (!StringHelper.isEmpty(cfqlr)) {
									cfjg = cfjg + "; 权利人：" + cfqlr;
								}
								b.setSQR(cfjg);
							}
						} else {
							if (!StringHelper.isEmpty(fsqls.get(0).getJFJG())) {
								String jfjg = "解封机关：" + fsqls.get(0).getJFJG() + "";
								if (!StringHelper.isEmpty(cfqlr)) {
									jfjg = jfjg + "; 权利人：" + cfqlr;
								}
								b.setSQR(jfjg);
							}
						}
					}
				}
				if (QLLX.DIYQ.Value.equals(xmxx.get(0).getQLLX())) {
					List<BDCS_QL_GZ> qls = dao.getDataList(BDCS_QL_GZ.class, ProjectHelper.GetXMBHCondition(xmbh));
					if (qls != null && qls.size() > 0) {
						BDCS_QL_GZ ql = qls.get(0);
						if(!StringHelper.isEmpty(ql.getCASENUM())){
							b.setCASENUM(ql.getCASENUM());
						}
						String zQLXQX = "";
						if (!StringHelper.isEmpty(ql.getQLQSSJ())) {
							zQLXQX = zQLXQX + StringHelper.FormatByDatetime(ql.getQLQSSJ()) + "起";
						}
						if (!StringHelper.isEmpty(ql.getQLJSSJ())) {
							zQLXQX = zQLXQX + StringHelper.FormatByDatetime(ql.getQLJSSJ()) + "止";
						}
						b.setZQLXQX(zQLXQX);
						if (!StringHelper.isEmpty(ql.getFSQLID())) {
							BDCS_FSQL_GZ fsql = dao.get(BDCS_FSQL_GZ.class, ql.getFSQLID());
							if (fsql != null) {
								String zqdw = ConstHelper.getNameByValue("JEDW", fsql.getZQDW());
								if ("1".equals(fsql.getDYFS())) {
									if (!StringHelper.isEmpty(fsql.getBDBZZQSE())) {
										b.setDBJE(StringHelper.formatDouble(fsql.getBDBZZQSE()) + zqdw);										
									}
								} else {
									if (!StringHelper.isEmpty(fsql.getZGZQSE())) {
										b.setDBJE(StringHelper.formatDouble(fsql.getZGZQSE())+ zqdw);
									}
								}
							}
						}
					}
				}
				b.setDLRXM(dlrBuilder.toString());
				b.setLXDH(sqrDhBuilder.toString());
				// 不动产单元类型、不动产坐落
				BDCDYLX bdcdylx = ProjectHelper.GetBDCDYLX(xmbh);
				if (bdcdylx != null) {
					b.setBDCLX(bdcdylx.Name);
				}
				StringBuilder builder = new StringBuilder();
				builder.append(" XMBH='").append(xmbh).append("'");
				List<BDCS_DJDY_GZ> djdys = dao.getDataList(BDCS_DJDY_GZ.class, builder.toString());
				int dygs = 0;
				int xzdygs = 0;
				int gzdygs = 0;
				if (djdys != null && djdys.size() > 0) {
					for (BDCS_DJDY_GZ djdy : djdys) {
						if (DJDYLY.XZ.Value.equals(djdy.getLY())) {
							xzdygs++;
						} else {
							gzdygs++;
						}
					}
				}
				if (gzdygs > 0) {
					dygs = gzdygs;
				} else {
					dygs = xzdygs;
				}

				List<RealUnit> units = new ArrayList<RealUnit>();
				if (djdys != null) {
					for (BDCS_DJDY_GZ djdy : djdys) {
						RealUnit unit = UnitTools.loadUnit(BDCDYLX.initFrom(djdy.getBDCDYLX()),
								DJDYLY.initFrom(djdy.getLY()), djdy.getBDCDYID());
						if (unit != null) {
							if (gzdygs > 0) {
								if (DJDYLY.GZ.equals(DJDYLY.initFrom(djdy.getLY()))) {
									units.add(unit);
								}
							} else {
								units.add(unit);
							}
						}
					}
				}
				units = SortUnit(units);
				boolean b031=false;
				if (units != null && units.size() > 0) {
					String zl = units.get(0).getZL();
					if(BDCDYLX.H.equals(units.get(0).getBDCDYLX())
							&&("4".equals(xmxx.get(0).getQLLX())||"6".equals(xmxx.get(0).getQLLX())||"8".equals(xmxx.get(0).getQLLX()))){
						House h_unit=(House)units.get(0);
						b.setFWFTTDMJ(StringHelper.formatDouble(h_unit.getFTTDMJ()));
						if(!StringHelper.isEmpty(h_unit.getZDBDCDYID())){
							List<BDCS_TDYT_XZ> tdyts=dao.getDataList(BDCS_TDYT_XZ.class, "BDCDYID='"+h_unit.getZDBDCDYID()+"'");
							if(tdyts!=null&&tdyts.size()>0){
								for(BDCS_TDYT_XZ tdyt:tdyts){
									if(!StringHelper.isEmpty(tdyt.getTDDJName())){
										b.setFWTDDJ(tdyt.getTDDJName());
										break;
									}
								}
								
							}
						}
						b031=true;
						List<BDCS_QL_GZ> qls_gz=dao.getDataList(BDCS_QL_GZ.class, "XMBH='"+xmbh+"' AND QLLX='"+xmxx.get(0).getQLLX()+"'");
						if(qls_gz!=null&&qls_gz.size()>0){
							b.setFWQLQSSJ(StringHelper.FormatDateOnType(qls_gz.get(0).getQLQSSJ(),"yyyy-MM-dd"));
							b.setFWQLJSSJ(StringHelper.FormatDateOnType(qls_gz.get(0).getQLJSSJ(),"yyyy-MM-dd"));
						}
					}
					if (StringHelper.isEmpty(zl)) {
						zl = "";
					}
					if (dygs > 1) {

						b.setBDCZL(zl + "等" + dygs + "处");
					} else {
						b.setBDCZL(zl);
					}
				} else {
					b.setBDCZL("");
				}

				//防城港-打印收费单添加权证号和用途相关
				String xzqdm = "";
				if ("450600".equals(xzqdm)) {
					List<BDCS_DJDY_GZ> djdy_1 = dao.getDataList(BDCS_DJDY_GZ.class, "XMBH='" + xmbh + "'");
					List<String> listH;
					if ((djdy_1 != null) && (djdy_1.size() > 0))
					{
						StringBuilder builderDJDY = new StringBuilder();
						StringBuilder builderQL = new StringBuilder();
						StringBuilder builderH = new StringBuilder();
						Object listQL = new ArrayList();
						listH = new ArrayList();
						List<BDCS_QL_GZ> ql_1 = dao.getDataList(BDCS_QL_GZ.class, "XMBH='" + xmbh + "'");
						String bdcqzh_hz = "";
						String yt_hz = "";
						if ((ql_1 != null) && (ql_1.size() > 0))
						{
							int intBDCQZH = 0;
							for (int i = 0; i < ql_1.size(); i++) {
								if (!StringHelper.isEmpty(((BDCS_QL_GZ)ql_1.get(i)).getBDCQZH()))
								{
									if (intBDCQZH == 2)
									{
										bdcqzh_hz = "等;";
										break;
									}
									if (intBDCQZH < 2) {
										((List)listQL).add(StringHelper.formatObject(((BDCS_QL_GZ)ql_1.get(i)).getBDCQZH()));
									}
									intBDCQZH++;
								}
							}
						}
						int intFWYT_01 = 0;
						int intFWYT_02 = 0;
						int intTDYT_01 = 0;
						int intTDYT_02 = 0;
						for (int i = 0; i < djdy_1.size(); i++)
						{
							String strBDCDYID = ((BDCS_DJDY_GZ)djdy_1.get(i)).getBDCDYID();
							builderDJDY.setLength(0);
							if (("01".equals(((BDCS_DJDY_GZ)djdy_1.get(i)).getLY())) && (("031".equals(((BDCS_DJDY_GZ)djdy_1.get(i)).getBDCDYLX())) || ("032".equals(((BDCS_DJDY_GZ)djdy_1.get(i)).getBDCDYLX()))))
							{
								if (intFWYT_01 == 2)
								{
									yt_hz = "等;";
									break;
								}
								builderDJDY.append("SELECT H.FWYT1 ");
								if ("031".equals(((BDCS_DJDY_GZ)djdy_1.get(i)).getBDCDYLX())) {
									builderDJDY.append("FROM BDCK.BDCS_H_GZ H WHERE H.BDCDYID='");
								} else {
									builderDJDY.append("FROM BDCK.BDCS_H_GZY H WHERE H.BDCDYID='");
								}
								builderDJDY.append(strBDCDYID);
								builderDJDY.append("'");
								String strdjdy = builderDJDY.toString();
								List<Map> listDJDY = dao.getDataListByFullSql(strdjdy);
								if ((listDJDY != null) && (listDJDY.size() > 0))
								{
									String fwyt1 = StringHelper.formatObject(listDJDY.get(0).get("FWYT1"));
									listH.add(ConstHelper.getNameByValue("FWYT", StringHelper.formatObject(fwyt1)));
								}
								intFWYT_01++;
							}
							else if (("02".equals(((BDCS_DJDY_GZ)djdy_1.get(i)).getLY())) && (("031".equals(((BDCS_DJDY_GZ)djdy_1.get(i)).getBDCDYLX())) || ("032".equals(((BDCS_DJDY_GZ)djdy_1.get(i)).getBDCDYLX()))))
							{
								if (intFWYT_02 == 2)
								{
									yt_hz = "等;";
									break;
								}
								builderDJDY.append("SELECT H.FWYT1 ");
								if ("031".equals(((BDCS_DJDY_GZ)djdy_1.get(i)).getBDCDYLX())) {
									builderDJDY.append("FROM BDCK.BDCS_H_XZ H WHERE H.BDCDYID='");
								} else {
									builderDJDY.append("FROM BDCK.BDCS_H_XZY H WHERE H.BDCDYID='");
								}
								builderDJDY.append(strBDCDYID);
								builderDJDY.append("'");
								String strdjdy = builderDJDY.toString();
								List<Map> listDJDY = dao.getDataListByFullSql(strdjdy);
								if ((listDJDY != null) && (listDJDY.size() > 0))
								{
									String fwyt1 = StringHelper.formatObject(listDJDY.get(0).get("FWYT1"));
									listH.add(ConstHelper.getNameByValue("FWYT", StringHelper.formatObject(fwyt1)));
								}
								intFWYT_02++;
							}
							else if (("01".equals(((BDCS_DJDY_GZ)djdy_1.get(i)).getLY())) && ("02".equals(((BDCS_DJDY_GZ)djdy_1.get(i)).getBDCDYLX())))
							{
								if (intTDYT_01 == 2)
								{
									yt_hz = "等;";
									break;
								}
								builderDJDY.append("SELECT TDYT ");
								builderDJDY.append("FROM BDCK.BDCS_TDYT_GZ  WHERE BDCDYID='");
								builderDJDY.append(strBDCDYID);
								builderDJDY.append("'");
								String strdjdy = builderDJDY.toString();
								List<Map> listDJDY = dao.getDataListByFullSql(strdjdy);
								if ((listDJDY != null) && (listDJDY.size() > 0)) {
									for (int a = 0; a < listDJDY.size(); a++)
									{
										if (intTDYT_02 == 2)
										{
											yt_hz = "等;";
											break;
										}
										String tdyt = StringHelper.formatObject(listDJDY.get(a).get("TDYT"));
										listH.add(ConstHelper.getNameByValue("TDYT", StringHelper.formatObject(tdyt)));
										intTDYT_01++;
									}
								}
							}
							else if (("02".equals(((BDCS_DJDY_GZ)djdy_1.get(i)).getLY())) && ("02".equals(((BDCS_DJDY_GZ)djdy_1.get(i)).getBDCDYLX())))
							{
								if (intTDYT_02 == 2)
								{
									yt_hz = "等;";
									break;
								}
								builderDJDY.append("SELECT TDYT ");
								builderDJDY.append("FROM BDCK.BDCS_TDYT_XZ  WHERE BDCDYID='");
								builderDJDY.append(strBDCDYID);
								builderDJDY.append("'");
								String strdjdy = builderDJDY.toString();
								List<Map> listDJDY = dao.getDataListByFullSql(strdjdy);
								if ((listDJDY != null) && (listDJDY.size() > 0)) {
									for (int a = 0; a < listDJDY.size(); a++)
									{
										if (intTDYT_02 == 2)
										{
											yt_hz = "等;";
											break;
										}
										String tdyt = StringHelper.formatObject(listDJDY.get(a).get("TDYT"));
										listH.add(ConstHelper.getNameByValue("TDYT", StringHelper.formatObject(tdyt)));
										intTDYT_02++;
									}
								}
							}
						}
						builderQL.append(StringHelper.formatList((List)listQL));
						builderH.append(StringHelper.formatList(listH));
						b.setBDCQZH(builderQL.toString() + bdcqzh_hz);
						b.setFWTDYT(builderH.toString() + yt_hz);
					}
				}

				// 收费列表
				List<BDCS_DJSF> sflist = dao.getDataList(BDCS_DJSF.class, ProjectHelper.GetXMBHCondition(xmbh)+" ORDER BY SFBMMC");
				b.setSFList(sflist);
				double jezj = 0.0;
				if(sflist!=null){
					List<String> dylist = new ArrayList<String>();
					for (BDCS_DJSF bdcs_DJSF : sflist) {
						if(!StringHelper.isEmpty(bdcs_DJSF.getSFKMMC())&&bdcs_DJSF.getSFKMMC().contains("出让金")&&b031){
							b.setB200031("true");
						}
						BDCS_SFDY dy = dao.get(BDCS_SFDY.class, bdcs_DJSF.getSFDYID());
						if(dy!=null){
							if("0".equals(dy.getTJBZ())){
								dylist.add(dy.getId());
							}else{
								jezj+=StringHelper.getDouble(bdcs_DJSF.getSSJE());
							}
						}
//						jezj+=StringHelper.getDouble(bdcs_DJSF.getSSJE());
					}
					b.setExceptSF(dylist);
				}
				b.setSSJEZJ(StringHelper.number2CNMontrayUnit(new BigDecimal(jezj)));
			}
		}
		return b;
	}

	private static List<RealUnit> SortUnit(List<RealUnit> units_old){
		List<RealUnit> units_new=new ArrayList<RealUnit>();
		if(StringHelper.isEmpty(units_old)||units_old.size()<=0){
			return units_old;
		}
		
		for(RealUnit unit :units_old){
			double mj=StringHelper.getDouble(unit.getMJ());
			int index=0;
			String zl=unit.getZL();
			String fh="";
			if(BDCDYLX.H.equals(unit.getBDCDYLX())||BDCDYLX.YCH.equals(unit.getBDCDYLX())){
				House h=(House)unit;
				if(!StringHelper.isEmpty(h)){
					fh=h.getFH();
				}
			}
			if(StringHelper.isEmpty(zl)||zl.contains("地下室")||zl.contains("车库")||(!StringHelper.isEmpty(fh)&&(fh.contains("地下室")||fh.contains("车库")))){
				if(units_new.size()>0){
					for(index=units_new.size()-1;index>=0;index--){
						RealUnit unit1=units_new.get(index);
						double mj1=StringHelper.getDouble(unit1.getMJ());
						if(mj1>=mj){
							units_new.add(unit);
							break;
						}else{
							String zl1=unit1.getZL();
							String fh1="";
							if(BDCDYLX.H.equals(unit1.getBDCDYLX())||BDCDYLX.YCH.equals(unit1.getBDCDYLX())){
								House h=(House)unit1;
								if(!StringHelper.isEmpty(h)){
									fh1=h.getFH();
								}
							}
							if(StringHelper.isEmpty(zl1)||zl1.contains("地下室")||zl1.contains("车库")||(!StringHelper.isEmpty(fh1)&&(fh1.contains("地下室")||fh1.contains("车库")))){
								if(index==0){
									units_new.add(0,unit);
									break;
								}
								continue;
							}else{
								if(index==units_new.size()-1){
									units_new.add(unit);
								}else{
									units_new.add(index+1,unit);
								}
								break;
							}
						}
					}
				}else{
					units_new.add(unit);
				}
			}else{
				if(units_new.size()==0){
					units_new.add(unit);
				}else{
					for(RealUnit unit1 :units_new){
						double mj1=StringHelper.getDouble(unit1.getMJ());
						if(mj>mj1){
							units_new.add(index, unit);
							break;
						}else{
							String zl1=unit1.getZL();
							String fh1="";
							if(BDCDYLX.H.equals(unit1.getBDCDYLX())||BDCDYLX.YCH.equals(unit1.getBDCDYLX())){
								House h=(House)unit1;
								if(!StringHelper.isEmpty(h)){
									fh1=h.getFH();
								}
							}
							if(StringHelper.isEmpty(zl1)||zl1.contains("地下室")||zl1.contains("车库")||(!StringHelper.isEmpty(fh1)&&(fh1.contains("地下室")||fh1.contains("车库")))){
								units_new.add(index, unit);
								break;
							}
						}
						index++;
						if(index==units_new.size()){
							units_new.add(unit);
							break;
						}
					}
				}
			}
		}
		return units_new;
	}

	
}
