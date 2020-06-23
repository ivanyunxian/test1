package com.supermap.realestate.registration.handlerImpl;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.supermap.realestate.registration.ViewClass.ProjectInfo;
import com.supermap.realestate.registration.model.BDCS_DJDY_GZ;
import com.supermap.realestate.registration.model.BDCS_FSQL_GZ;
import com.supermap.realestate.registration.model.BDCS_FSQL_XZ;
import com.supermap.realestate.registration.model.BDCS_H_XZ;
import com.supermap.realestate.registration.model.BDCS_H_XZY;
import com.supermap.realestate.registration.model.BDCS_QDZR_GZ;
import com.supermap.realestate.registration.model.BDCS_QDZR_XZ;
import com.supermap.realestate.registration.model.BDCS_QLR_GZ;
import com.supermap.realestate.registration.model.BDCS_QLR_XZ;
import com.supermap.realestate.registration.model.BDCS_QL_GZ;
import com.supermap.realestate.registration.model.BDCS_QL_XZ;
import com.supermap.realestate.registration.model.BDCS_SQR;
import com.supermap.realestate.registration.model.BDCS_XMXX;
import com.supermap.realestate.registration.model.BDCS_ZS_GZ;
import com.supermap.realestate.registration.model.BDCS_ZS_XZ;
import com.supermap.realestate.registration.model.WFD_MAPPING;
import com.supermap.realestate.registration.model.YC_SC_H_XZ;
import com.supermap.realestate.registration.model.interfaces.RealUnit;
import com.supermap.realestate.registration.model.interfaces.Rights;
import com.supermap.realestate.registration.model.interfaces.RightsHolder;
import com.supermap.realestate.registration.model.interfaces.SubRights;
import com.supermap.realestate.registration.model.xmlExportmodel.MessageExport;
import com.supermap.realestate.registration.tools.RightsHolderTools;
import com.supermap.realestate.registration.tools.RightsTools;
import com.supermap.realestate.registration.tools.UnitTools;
import com.supermap.realestate.registration.util.ConstValue;
import com.supermap.realestate.registration.util.ConstValue.BDCDYLX;
import com.supermap.realestate.registration.util.ConstValue.CZFS;
import com.supermap.realestate.registration.util.ConstValue.DJDYLY;
import com.supermap.realestate.registration.util.ConstValue.DJLX;
import com.supermap.realestate.registration.util.ConstValue.SQRLB;
import com.supermap.realestate.registration.util.Global;
import com.supermap.realestate.registration.util.ObjectHelper;
import com.supermap.realestate.registration.util.ProjectHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.realestate.registration.util.exportXmlUtil;

/*
 1、预抵押转现房抵押（待测）
 */
/**
 * 
* 预抵押转现房抵押处理类
* @ClassName: YDYZXDYDJHandler 
* @author liushufeng 
* @date 2015年9月8日 下午10:42:08
 */
public class YDYZXDYDJHandler extends DYDJHandler {

	private String StrError = "";

	/**
	 * 构造函数
	 * 
	 * @param info
	 */
	public YDYZXDYDJHandler(ProjectInfo info) {
		super(info);
	}

	/**
	 * 添加现状预测户的不动产权单元
	 * 
	 * @作者 海豹
	 * @创建时间 2015年7月10日下午2:10:44
	 * @param bdcdyid
	 * @param bdcs_h_xzy
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public boolean addBDCDY(String bdcdyid) {
		boolean bSuccess = false;
		List<Map> lst = IsExistYCH(bdcdyid);//抵押ql信息
		if (lst != null && lst.size() > 0) {
			BDCS_H_XZ bdcs_h_xz = getCommonDao().get(BDCS_H_XZ.class, bdcdyid);
			if (bdcs_h_xz == null) {
				return false;
			} else {
				Map map = lst.get(0);
				String ywrs="";
				if (map != null) {
					String gzfsqlid = getPrimaryKey();
					String gzqlid = getPrimaryKey();
					String qlid = map.get("QLID") == null ? "" : map.get("QLID").toString();
					String djdyid = map.get("DJDYID") == null ? "" : map.get("DJDYID").toString();
					if (!StringHelper.isEmpty(djdyid)) {
						// 把房屋所有权人拷贝为义务人
						String sql = MessageFormat.format("QLID=(SELECT id FROM BDCS_QL_XZ WHERE QLLX=''4''  AND  DJDYID=(SELECT DJDYID FROM BDCS_DJDY_XZ WHERE BDCDYID=''{0}''))",
								bdcdyid);
						List<BDCS_QLR_XZ> xzqlrs = getCommonDao().getDataList(BDCS_QLR_XZ.class, sql);
						if (xzqlrs != null && xzqlrs.size() > 0) {
							for (BDCS_QLR_XZ qlr : xzqlrs) {
								if (qlr != null) {
									BDCS_SQR sqr1 = super.copyXZQLRtoSQR(qlr, SQRLB.YF);
									if(sqr1 != null){
										sqr1.setGLQLID(gzqlid);
										ywrs = ywrs + qlr.getQLRMC()+",";
										getCommonDao().save(sqr1);
									}
								}
							}
						}
					}

					if (IsCreateDJDY(qlid))// 若创建，直接跳出
					{
						return bSuccess;
					}
					BDCS_QL_XZ bdcs_ql_xz = getCommonDao().get(BDCS_QL_XZ.class, qlid);
					if (bdcs_ql_xz != null) {
						// 拷贝权利
						BDCS_QL_GZ bdcs_ql_gz = ObjectHelper.copyQL_XZToGZ(bdcs_ql_xz);
						bdcs_ql_gz.setId(gzqlid);
						bdcs_ql_gz.setXMBH(getXMBH());
						bdcs_ql_gz.setBDCDYH(bdcs_h_xz.getBDCDYH());
						bdcs_ql_gz.setLYQLID(qlid);// 来源权利ID					
						bdcs_ql_gz.setBDCQZH(""); // 从新生成不动产权证号
						bdcs_ql_gz.setDJSJ(null);
						bdcs_ql_gz.setDJLX(DJLX.CSDJ.Value);// 应该修改登记类型
						bdcs_ql_gz.setCZFS(CZFS.GTCZ.Value);
						bdcs_ql_gz.setDJJG("");
						bdcs_ql_gz.setDBR("");

						BDCS_FSQL_XZ bdcs_fsql_xz = getCommonDao().get(BDCS_FSQL_XZ.class, bdcs_ql_xz.getFSQLID());
						BDCS_FSQL_GZ bdcs_fsql_gz = new BDCS_FSQL_GZ();
						if (bdcs_fsql_xz != null) {
							// 拷贝附属权利
							bdcs_fsql_gz = ObjectHelper.copyFSQL_XZToGZ(bdcs_fsql_xz);
						}
						bdcs_fsql_gz.setId(gzfsqlid);
						bdcs_fsql_gz.setQLID(gzqlid);
						bdcs_fsql_gz.setXMBH(getXMBH());
						bdcs_fsql_gz.setDYWLX(getDYBDCLXfromBDCDYLX(getBdcdylx()));
						bdcs_ql_gz.setFSQLID(gzfsqlid);
						bdcs_ql_gz.setYWH(super.getProject_id());
						
						//是否自动继承原抵押权信息的查封,0:否，1：是
						String isextendmortgageinfo = "";
						String workflowcode = ProjectHelper.getWorkflowCodeByProjectID(getProject_id());
						List<WFD_MAPPING> listCode = getCommonDao().getDataList(WFD_MAPPING.class, "WORKFLOWCODE='" + workflowcode + "'");
						if ((listCode != null) && (listCode.size() > 0)) {
							isextendmortgageinfo = ((WFD_MAPPING) listCode.get(0)).getISEXTENDMORTGAGEINFO();
						}
						if ("0".equals(isextendmortgageinfo)) {
							// 2015年8月22日，小周同志提出，不能把抵押物价值等信息带过来，所以，要清空 --刘树峰
							bdcs_fsql_gz.setZJJZWZL(null);
							bdcs_fsql_gz.setDYFS(null);
							bdcs_fsql_gz.setBDBZZQSE(null);
							bdcs_fsql_gz.setZJJZWDYFW(null);							
							bdcs_fsql_gz.setZGZQSE(null);
							bdcs_fsql_gz.setZGZQQDSS(null);
							bdcs_fsql_gz.setDYFS(null);							
							bdcs_ql_gz.setQLQSSJ(null);
							bdcs_ql_gz.setQLJSSJ(null);
							bdcs_ql_gz.setDJYY(null);
							bdcs_ql_gz.setFJ(null);
						}
						
						if(!StringHelper.isEmpty(ywrs))
						{
							bdcs_fsql_gz.setDYR(ywrs.substring(0,ywrs.length()-1));
						}
						
						getCommonDao().save(bdcs_fsql_gz);

						BDCS_DJDY_GZ djdy = super.createDJDYfromXZ(bdcdyid);
						getCommonDao().save(djdy);
						bdcs_ql_gz.setDJDYID(djdy.getDJDYID());

						// 把抵押人和抵押权人拷贝到申请人里边

						// String strDJDY =
						// MessageFormat.format("BDCDYID=''{0}''",
						// bdcs_ql_xz.getDJDYID());
						// // 获取登记单元集合
						// List<BDCS_DJDY_XZ> djdys =
						// getCommonDao().getDataList(BDCS_DJDY_XZ.class,
						// strDJDY);
						// for (BDCS_DJDY_XZ bdcs_djdy_xz : djdys) {
						// // 拷贝登记单元
						// String gzdjdyid = getPrimaryKey();
						// BDCS_DJDY_GZ bdcs_djdy_gz =
						// ObjectHelper.copyDJDY_XZToGZ(bdcs_djdy_xz);
						// bdcs_djdy_gz.setId(gzdjdyid);
						// bdcs_djdy_gz.setXMBH(getXMBH());
						// bdcs_djdy_gz.setBDCDYID(bdcs_h_xz.getId());
						// bdcs_djdy_gz.setBDCDYLX(BDCDYLX.H.Value);
						// bdcs_djdy_gz.setLY(DJDYLY.XZ.Value);
						//
						// }
						// 获取权利人集合
						String strQLID = MessageFormat.format("qlid=''{0}''", qlid);
						List<BDCS_QLR_XZ> qlrs = getCommonDao().getDataList(BDCS_QLR_XZ.class, strQLID);
						for (BDCS_QLR_XZ bdcs_qlr_xz : qlrs) {
							String gzqlrid = "";
							// 拷贝权利人
							if ("1".equals(isextendmortgageinfo)) {
								gzqlrid = getPrimaryKey();
								BDCS_QLR_GZ bdcs_qlr_gz = ObjectHelper.copyQLR_XZToGZ(bdcs_qlr_xz);
								bdcs_qlr_gz.setQLID(gzqlid);
								bdcs_qlr_gz.setId(gzqlrid);
								bdcs_qlr_gz.setBDCQZH("");
								bdcs_qlr_gz.setXMBH(getXMBH());
								getCommonDao().save(bdcs_qlr_gz);
								// 获取证书集合
								StringBuilder builder = new StringBuilder();
								builder.append(" id IN (");
								builder.append("select ZSID FROM BDCS_QDZR_XZ WHERE QLID ='");
								builder.append(qlid).append("'").append(" AND QLRID='");
								builder.append(bdcs_qlr_xz.getId()).append("')");
								String strQueryZS = builder.toString();
								List<BDCS_ZS_XZ> zss = getCommonDao().getDataList(BDCS_ZS_XZ.class, strQueryZS);
								for (BDCS_ZS_XZ bdcs_zs_xz : zss) {
									String gzzsid = getPrimaryKey();
									// 拷贝证书
									BDCS_ZS_GZ bdcs_zs_gz = ObjectHelper.copyZS_XZToGZ(bdcs_zs_xz);
									bdcs_zs_gz.setId(gzzsid);
									bdcs_zs_gz.setXMBH(getXMBH());
									bdcs_zs_gz.setBDCQZH("");
									bdcs_zs_gz.setZSBH("");
									getCommonDao().save(bdcs_zs_gz);
									// 获取权地证人集合
									StringBuilder builderQDZR = new StringBuilder();
									builderQDZR.append(" ZSID='");
									builderQDZR.append(bdcs_zs_xz.getId());
									builderQDZR.append("' AND QLID='");
									builderQDZR.append(qlid);
									builderQDZR.append("' AND QLRID='");
									builderQDZR.append(bdcs_qlr_xz.getId());
									builderQDZR.append("')");
									List<BDCS_QDZR_XZ> qdzrs = getCommonDao().getDataList(BDCS_QDZR_XZ.class, builderQDZR.toString());
									for (BDCS_QDZR_XZ bdcs_qdzr_xz : qdzrs) {
										String gzqdzrid = getPrimaryKey();
										// 拷贝权地证人
										BDCS_QDZR_GZ bdcs_qdzr_gz = ObjectHelper.copyQDZR_XZToGZ(bdcs_qdzr_xz);
										bdcs_qdzr_gz.setId(gzqdzrid);
										bdcs_qdzr_gz.setQLID(gzqlid);
										bdcs_qdzr_gz.setZSID(gzzsid);
										bdcs_qdzr_gz.setFSQLID(gzfsqlid);
										bdcs_qdzr_gz.setXMBH(getXMBH());
										bdcs_qdzr_gz.setQLRID(gzqlrid);
										bdcs_qdzr_gz.setDJDYID(djdy.getDJDYID());

										getCommonDao().save(bdcs_qdzr_gz);
									}
								}
							}
							BDCS_SQR sqr = super.copyXZQLRtoSQR(bdcs_qlr_xz, SQRLB.JF);
							if(sqr!=null){
								sqr.setGLQLID(gzqlid);
								getCommonDao().save(sqr);
							}
						}
						getCommonDao().save(bdcs_ql_gz);
						getCommonDao().flush();
					}

				}
			}
			bSuccess = true;
		} else {
			bSuccess = false;
			StrError = "不存在预告抵押权登记信息";
		}
		return bSuccess;
	}

	/**
	 * 写入登记薄:1、拷贝权利信息；2、删除预测户现状权利信息
	 */
	@Override
	public boolean writeDJB() {
		String strSqlXMBH = ProjectHelper.GetXMBHCondition(getXMBH());
		List<BDCS_DJDY_GZ> djdys = getCommonDao().getDataList(BDCS_DJDY_GZ.class, strSqlXMBH);
		for (BDCS_DJDY_GZ bdcs_djdy_gz : djdys) {
			String djdyid = bdcs_djdy_gz.getDJDYID();
			super.CopyGZQLToXZAndLS(djdyid);
			super.CopyGZQLRToXZAndLS(djdyid);
			super.CopyGZQDZRToXZAndLS(djdyid);
			super.CopyGZZSToXZAndLS(djdyid);
			BDCDYLX dylx = ProjectHelper.GetBDCDYLX(getXMBH());
			// 更新单元抵押状态
			SetDYDYZT(bdcs_djdy_gz.getBDCDYID(), dylx, "0");
			//先查询现房关联的期房有没有查封,有的话将预查封转为现查封，删除预查封现状权利
			super.checkisYCF(StringHelper.formatObject(bdcs_djdy_gz.getBDCDYID()));
			DeleteYCHQLInfo(djdyid);
		}
		this.SetSFDB();
		getCommonDao().flush();
		super.alterCachedXMXX();
		return true;
	}

	/**
	 * 获取错误信息
	 * 
	 * @作者 刘树峰
	 * @创建时间 2015年6月23日下午8:11:43
	 * @return
	 */
	@Override
	public String getError() {
		return StrError;
	}

	/**
	 * 删除预测户权利信息
	 * 
	 * @作者 海豹
	 * @创建时间 2015年7月15日上午12:11:45
	 * @param djdyid
	 */
	protected void DeleteYCHQLInfo(String djdyid) {
		Rights right=	RightsTools.loadRightsByDJDYID(DJDYLY.GZ, getXMBH(), djdyid);
		 if(right !=null)
		 {
			 //删除预测户权利信息（权利、权利人、证书、权地证人）
			 RightsTools.deleteRightsAll(DJDYLY.XZ, right.getLYQLID());
			 SubRights subrigths=	RightsTools.loadSubRightsByRightsID(DJDYLY.LS, right.getLYQLID());
				// 更新预测户BDCS_FSQL_GZ中的注销信息
				if(subrigths !=null)
				{
					Date zxsj=new Date();
					String zxdbr=Global.getCurrentUserName();
					subrigths.setZXDBR(zxdbr);
					subrigths.setZXSJ(zxsj);
					getCommonDao().update(subrigths);
				}
		 }
//		String qlSql = MessageFormat.format("DJDYID=''{0}'' and XMBH=''{1}''", djdyid, getXMBH());
//		List<BDCS_QL_GZ> qls = getCommonDao().getDataList(BDCS_QL_GZ.class, qlSql);
//		for (BDCS_QL_GZ bdcs_ql_gz : qls) {
//			String ycqlid = bdcs_ql_gz.getLYQLID() == null ? "" : bdcs_ql_gz.getLYQLID().toString();
//			BDCS_QL_XZ bdcs_ql_xz = getCommonDao().get(BDCS_QL_XZ.class, ycqlid);
//			if (bdcs_ql_xz != null) {
//				BDCS_FSQL_XZ bdcs_fsql_xz = getCommonDao().get(BDCS_FSQL_XZ.class, bdcs_ql_xz.getFSQLID());
//				if (bdcs_fsql_xz != null) {
//					// 删除预测户中的附属权利
//					getCommonDao().deleteEntity(bdcs_fsql_xz);
//				}
//				// 获取登记单元集合
//				String djdySql = MessageFormat.format("DJDYID=''{0}''", bdcs_ql_xz.getDJDYID());
//				List<BDCS_DJDY_XZ> djdys = getCommonDao().getDataList(BDCS_DJDY_XZ.class, djdySql);
//				for (BDCS_DJDY_XZ bdcs_djdy_xz : djdys) {
//					// 删除预测户中的登记单元
//					getCommonDao().deleteEntity(bdcs_djdy_xz);
//				}
//				// 获取权利人集合
//				String qlrSql = MessageFormat.format("QLID=''{0}''", bdcs_ql_xz.getId());
//				List<BDCS_QLR_XZ> qlrs = getCommonDao().getDataList(BDCS_QLR_XZ.class, qlrSql);
//				for (BDCS_QLR_XZ bdcs_qlr_xz : qlrs) {
//					// 获取证书集合
//					String zsSql = MessageFormat.format("id IN(select ZSID FROM BDCS_QDZR_XZ WHERE QLID=''{0}'' AND QLRID=''{1}'')", bdcs_ql_xz.getId(), bdcs_qlr_xz.getId());
//					List<BDCS_ZS_XZ> zss = getCommonDao().getDataList(BDCS_ZS_XZ.class, zsSql);
//					for (BDCS_ZS_XZ bdcs_zs_xz : zss) {
//						// 获取权地证人集合
//						String qdzrSql = MessageFormat.format("ZSID=''{0}'' and QLID=''{1}'' and QLRID=''{2}''", bdcs_zs_xz.getId(), bdcs_ql_xz.getId(), bdcs_qlr_xz.getId());
//						List<BDCS_QDZR_XZ> qdzrs = getCommonDao().getDataList(BDCS_QDZR_XZ.class, qdzrSql);
//						for (BDCS_QDZR_XZ bdcs_qdzr_xz : qdzrs) {
//							// 删除权地证人
//							getCommonDao().deleteEntity(bdcs_qdzr_xz);
//						}
//						// 删除证书信息
//						getCommonDao().deleteEntity(bdcs_zs_xz);
//					}
//					// 删除预测户中权利人
//					getCommonDao().deleteEntity(bdcs_qlr_xz);
//				}
//				// 删除预测户中的权利
//				getCommonDao().deleteEntity(bdcs_ql_xz);
//				// 更新预测户BDCS_FSQL_GZ中的注销信息
//				BDCS_FSQL_LS bdcs_fsql_ls = getCommonDao().get(BDCS_FSQL_LS.class, bdcs_ql_xz.getFSQLID());
//				if (bdcs_fsql_ls != null) {
//					Date zxsj = new Date();
//					String dbr = Global.getCurrentUserName();
//					bdcs_fsql_ls.setZXSJ(zxsj);
//					bdcs_fsql_ls.setZXDBR(dbr);
//					getCommonDao().update(bdcs_fsql_ls);
//				}
//			}
//		}
	}

	/**
	 * 判断是否当前已经创建登记单元，若创建，就不再创建
	 * 
	 * @作者 海豹
	 * @创建时间 2015年7月14日下午4:33:37
	 * @param ycqlid
	 *            预测户权利ID
	 * @return
	 */
	protected Boolean IsCreateDJDY(String ycqlid) {
		Boolean result = false;
		String hqlCondition = MessageFormat.format("LYQLID=''{0}'' and xmbh=''{1}''", ycqlid, this.getXMBH());
		List<BDCS_QL_GZ> bdcs_ql_gz = getCommonDao().getDataList(BDCS_QL_GZ.class, hqlCondition);
		if (bdcs_ql_gz != null && bdcs_ql_gz.size() > 0) {
			result = true;
			StrError = "该抵押权正在办理登记，不能重复选取";
		}
		return result;
	}

	protected String getDYBDCLXfromBDCDYLX(BDCDYLX bdcdylx) {
		String dybdclx = "";
		if (bdcdylx.equals(BDCDYLX.SHYQZD) || bdcdylx.equals(BDCDYLX.SYQZD)) {
			dybdclx = "1";
		} else if (bdcdylx.equals(BDCDYLX.H) || bdcdylx.equals(BDCDYLX.ZRZ)) {
			dybdclx = "2";
		} else if (bdcdylx.equals(BDCDYLX.LD)) {
			dybdclx = "3";
		} else if (bdcdylx.equals(BDCDYLX.GZW) || bdcdylx.equals(BDCDYLX.YCH) || bdcdylx.equals(BDCDYLX.ZRZ)) {
			dybdclx = "4";
		} else if (bdcdylx.equals(BDCDYLX.HY)) {
			dybdclx = "5";
		} else {
			dybdclx = "7";
		}
		return dybdclx;
	}

	/**
	 * 判断是否存在预购商品房抵押权预告登记转现房抵押权登记
	 * 
	 * @作者 海豹
	 * @创建时间 2015年7月14日下午5:08:53
	 * @param scbdcdyid
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	protected List<Map> IsExistYCH(String scbdcdyid) {
		List<Map> lst = new ArrayList<Map>();
		BDCS_H_XZ bdcs_h_xz = getCommonDao().get(BDCS_H_XZ.class, scbdcdyid);
		if (bdcs_h_xz != null) {
			StringBuilder fulSql = new StringBuilder();
			fulSql.append("select ql.* from  BDCK.BDCS_QL_XZ ql left join BDCK.BDCS_DJDY_XZ djdy on djdy.DJDYID=ql.DJDYID ");
			fulSql.append(" left join BDCK.BDCS_H_XZY ych on ych.BDCDYID=djdy.BDCDYID ");
			fulSql.append(" left join BDCK.YC_SC_H_XZ ysh on ysh.YCBDCDYID=djdy.BDCDYID ");
			fulSql.append(" where ql.QLLX='23' and djlx='700' and ysh.SCBDCDYID='" + bdcs_h_xz.getId() + "'");
			lst = getCommonDao().getDataListByFullSql(fulSql.toString());
		}
		return lst;
	}

	/**
	 * 添加登记单元信息
	 * 
	 * @作者 海豹
	 * @创建时间 2015年7月10日上午11:47:23
	 * @param bdcdyid
	 * @param bdcs_h_xzy
	 * @return
	 */
	protected BDCS_DJDY_GZ createDJDYfromXZ(String bdcdyid, BDCS_H_XZY bdcs_h_xzy) {
		BDCS_DJDY_GZ gzdjdy = new BDCS_DJDY_GZ();
		gzdjdy.setXMBH(this.getXMBH());
		gzdjdy.setDJDYID(gzdjdy.getId());
		gzdjdy.setBDCDYID(bdcs_h_xzy.getId());
		gzdjdy.setBDCDYLX(this.getBdcdylx().Value);
		gzdjdy.setBDCDYH(bdcs_h_xzy.getBDCDYH());
		gzdjdy.setLY(DJDYLY.XZ.Value);
		gzdjdy.setDCXMID(bdcs_h_xzy.getDCXMID());

		// 设置预测户的项目编号
		bdcs_h_xzy.setXMBH(this.getXMBH());
		// getCommonDao().update(bdcs_h_xzy);
		return gzdjdy;
	}

	/**
	 * 共享信息
	 * 
	 * @作者 俞学斌
	 * @创建时间 2015年8月16日下午16:37:43
	 * @return
	 */
	@Override
	public void SendMsg(String bljc) {
		BDCS_XMXX xmxx = getCommonDao().get(BDCS_XMXX.class, super.getXMBH());
		String xmbhFilter = ProjectHelper.GetXMBHCondition(super.getXMBH());
		List<BDCS_DJDY_GZ> djdys = getCommonDao().getDataList(BDCS_DJDY_GZ.class, xmbhFilter);
		if (djdys != null && djdys.size() > 0) {
			for (int idjdy = 0; idjdy < djdys.size(); idjdy++) {
				BDCS_DJDY_GZ djdy = djdys.get(idjdy);
				ConstValue.BDCDYLX dylx = ConstValue.BDCDYLX.initFrom(djdy.getBDCDYLX());
				ConstValue.DJDYLY dyly = ConstValue.DJDYLY.initFrom(djdy.getLY());
				RealUnit bdcdy = UnitTools.loadUnit(dylx, dyly, djdy.getBDCDYID());
				Rights bdcql = RightsTools.loadRightsByDJDYID(ConstValue.DJDYLY.GZ, getXMBH(), djdy.getDJDYID());
				SubRights bdcfsql = null;
				List<RightsHolder> bdcqlrs = null;
				if (bdcql != null) {
					bdcfsql = RightsTools.loadSubRightsByRightsID(ConstValue.DJDYLY.GZ, bdcql.getId());
					bdcqlrs = RightsHolderTools.loadRightsHolders(ConstValue.DJDYLY.GZ, djdy.getDJDYID(), getXMBH());
				}
				MessageExport msg = super.getShareMsgTools().GetMsg(bdcdy, bdcql, bdcfsql, bdcqlrs, bljc, xmxx);
				super.getShareMsgTools().SendMsg(msg, idjdy + 1, djdy.getBDCDYLX(), xmxx);
			}
		}
	}
	
	@Override
	public Map<String, String> exportXML(String path, String actinstID) {
		return exportXmlUtil.createXMLAndUp(path, actinstID);
	}
	
}
