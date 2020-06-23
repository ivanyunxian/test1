package com.supermap.realestate.registration.handlerImpl;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.supermap.realestate.registration.ViewClass.ProjectInfo;
import com.supermap.realestate.registration.model.BDCS_DJDY_GZ;
import com.supermap.realestate.registration.model.BDCS_DJDY_XZ;
import com.supermap.realestate.registration.model.BDCS_FSQL_GZ;
import com.supermap.realestate.registration.model.BDCS_FSQL_LS;
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
import com.supermap.realestate.registration.util.ConstValue.DJDYLY;
import com.supermap.realestate.registration.util.ConstValue.GYFS;
import com.supermap.realestate.registration.util.ConstValue.SFDB;
import com.supermap.realestate.registration.util.ConstValue.SQRLB;
import com.supermap.realestate.registration.util.GetProperties;
import com.supermap.realestate.registration.util.Global;
import com.supermap.realestate.registration.util.ObjectHelper;
import com.supermap.realestate.registration.util.ProjectHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.realestate.registration.util.exportXmlUtil;

/*
 1、在建工程抵押转现房抵押登记（待测）
 */
/**
 * 
* 在建工程抵押转现房抵押处理类
* @ClassName: YCSCDYDJHandler 
* @author liushufeng 
* @date 2015年9月8日 下午10:41:06
 */
public class YCSCDYDJHandler extends DYDJHandler {

	private String StrError = "";

	/**
	 * 构造函数
	 * 
	 * @param info
	 */
	public YCSCDYDJHandler(ProjectInfo info) {
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
		List<Map> lst = IsExistYCH(bdcdyid);
		if (lst != null && lst.size() > 0) {
			BDCS_H_XZ bdcs_h_xz = getCommonDao().get(BDCS_H_XZ.class, bdcdyid);
			if (bdcs_h_xz == null) {
				return false;
			} else {
				Map map = lst.get(0);
				List<String> listdyr= new ArrayList<String>();
				if (map != null) {
					String gzfsqlid = getPrimaryKey();
					String gzqlid = getPrimaryKey();
					String qlid = map.get("QLID") == null ? "" : map
							.get("QLID").toString();
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
									if(sqr1!=null){
										sqr1.setGLQLID(gzqlid);
										if(!StringHelper.isEmpty(qlr.getQLRMC())&&!listdyr.contains(qlr.getQLRMC())){
											listdyr.add(qlr.getQLRMC());
										}
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
					BDCS_QL_XZ bdcs_ql_xz = getCommonDao().get(
							BDCS_QL_XZ.class, qlid);
					if (bdcs_ql_xz != null) {
						// 拷贝权利
						BDCS_QL_GZ bdcs_ql_gz = ObjectHelper.copyQL_XZToGZ(bdcs_ql_xz);
						bdcs_ql_gz.setId(gzqlid);
						bdcs_ql_gz.setXMBH(getXMBH());
						bdcs_ql_gz.setLYQLID(qlid);// 来源权利ID
						bdcs_ql_gz.setDBR("");
						bdcs_ql_gz.setBDCQZH("");// 不动产权证号清空
						bdcs_ql_gz.setDJSJ(null);
						//是否自动继承原抵押权信息的查封,0:否，1：是
						String isextendmortgageinfo = "";
						String workflowcode = ProjectHelper.getWorkflowCodeByProjectID(getProject_id());
						List<WFD_MAPPING> listCode = getCommonDao().getDataList(WFD_MAPPING.class, "WORKFLOWCODE='" + workflowcode + "'");
						if ((listCode != null) && (listCode.size() > 0)) {
							isextendmortgageinfo = ((WFD_MAPPING) listCode.get(0)).getISEXTENDMORTGAGEINFO();
						}
						if ("0".equals(isextendmortgageinfo)) {
							// 2015年8月22日，小周同志提出，不能把抵押物价值等信息带过来，所以，要清空 --刘树峰
							bdcs_ql_gz.setQLQSSJ(null);
							bdcs_ql_gz.setQLJSSJ(null);
							bdcs_ql_gz.setDJYY(null);
							bdcs_ql_gz.setFJ(null);
						}
						BDCS_FSQL_XZ bdcs_fsql_xz = getCommonDao().get(BDCS_FSQL_XZ.class, bdcs_ql_xz.getFSQLID());
						BDCS_FSQL_GZ bdcs_fsql_gz = new BDCS_FSQL_GZ();
						if (bdcs_fsql_xz != null) {
							// 拷贝附属权利
							bdcs_fsql_gz = ObjectHelper.copyFSQL_XZToGZ(bdcs_fsql_xz);
							bdcs_fsql_gz.setId(gzfsqlid);
							bdcs_fsql_gz.setQLID(gzqlid);
							bdcs_fsql_gz.setXMBH(getXMBH());
							bdcs_fsql_gz.setDYR(StringHelper.formatList(listdyr, "、"));
							bdcs_ql_gz.setFSQLID(gzfsqlid);
							bdcs_ql_gz.setYWH(super.getProject_id());
							bdcs_fsql_gz.setDYBDCLX("2");
							bdcs_fsql_gz.setDYWLX("2");
							
							if ("0".equals(isextendmortgageinfo)) {
								// 2015年8月22日，小周同志提出，不能把抵押物价值等信息带过来，所以，要清空 --刘树峰
								bdcs_fsql_gz.setZJJZWZL(null);
								bdcs_fsql_gz.setDYFS(null);
								bdcs_fsql_gz.setBDBZZQSE(null);
								bdcs_fsql_gz.setZJJZWDYFW(null);							
								bdcs_fsql_gz.setZGZQSE(null);
								bdcs_fsql_gz.setZGZQQDSS(null);
								bdcs_fsql_gz.setDYFS(null);							
							}
							
							getCommonDao().save(bdcs_fsql_gz);
						}
						BDCS_DJDY_GZ djdy = super.createDJDYfromXZ(bdcdyid);
						getCommonDao().save(djdy);
						bdcs_ql_gz.setDJDYID(djdy.getDJDYID());

						// 获取权利人集合
						String strQLID = MessageFormat.format("qlid=''{0}''",qlid);
						List<BDCS_QLR_XZ> qlrs = getCommonDao().getDataList(BDCS_QLR_XZ.class, strQLID);
						for (BDCS_QLR_XZ bdcs_qlr_xz : qlrs) {
							String gzqlrid = "";
							// 拷贝权利人
							if ("1".equals(isextendmortgageinfo)) {
								gzqlrid = getPrimaryKey();
								BDCS_QLR_GZ bdcs_qlr_gz = ObjectHelper
										.copyQLR_XZToGZ(bdcs_qlr_xz);
								if(qlrs.size()==1){
									bdcs_qlr_gz.setGYFS(GYFS.DYSY.Value);
								}
								bdcs_qlr_gz.setQLID(gzqlid);
								bdcs_qlr_gz.setId(gzqlrid);
								bdcs_qlr_gz.setBDCQZH("");
								bdcs_qlr_gz.setXMBH(getXMBH());
								getCommonDao().save(bdcs_qlr_gz);
								
								// 获取证书集合
								StringBuilder builder = new StringBuilder();
								builder.append(" id IN (");
								builder.append("select ZSID FROM BDCS_QDZR_XZ WHERE QLID ='");
								builder.append(qlid).append("'")
										.append(" AND QLRID='");
								builder.append(bdcs_qlr_xz.getId()).append("')");
								String strQueryZS = builder.toString();
								List<BDCS_ZS_XZ> zss = getCommonDao().getDataList(
										BDCS_ZS_XZ.class, strQueryZS);
								for (BDCS_ZS_XZ bdcs_zs_xz : zss) {
									String gzzsid = getPrimaryKey();
									// 拷贝证书
									BDCS_ZS_GZ bdcs_zs_gz = ObjectHelper
											.copyZS_XZToGZ(bdcs_zs_xz);
									bdcs_zs_gz.setId(gzzsid);
									bdcs_zs_gz.setBDCQZH("");
									bdcs_zs_gz.setXMBH(getXMBH());
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
									List<BDCS_QDZR_XZ> qdzrs = getCommonDao()
											.getDataList(BDCS_QDZR_XZ.class,
													builderQDZR.toString());
									for (BDCS_QDZR_XZ bdcs_qdzr_xz : qdzrs) {
										String gzqdzrid = getPrimaryKey();
										// 拷贝权地证人
										BDCS_QDZR_GZ bdcs_qdzr_gz = ObjectHelper
												.copyQDZR_XZToGZ(bdcs_qdzr_xz);
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
								if(qlrs.size()==1){
									sqr.setGYFS(GYFS.DYSY.Value);
								}
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
			StrError = "不存在在建工程抵押权登记信息";
		}
		return bSuccess;
	}

	/**
	 * 写入登记薄:1、拷贝权利信息；2、删除预测户现状权利信息
	 */
	@Override
	public boolean writeDJB() {
		String strSqlXMBH = ProjectHelper.GetXMBHCondition(getXMBH());
		List<BDCS_DJDY_GZ> djdys = getCommonDao().getDataList(
				BDCS_DJDY_GZ.class, strSqlXMBH);
		for (BDCS_DJDY_GZ bdcs_djdy_gz : djdys) {
			String djdyid = bdcs_djdy_gz.getDJDYID();
			DeleteYCHQLInfo(djdyid);// 删除预测户权利信息
			super.CopyGZQLToXZAndLS(djdyid);
			super.CopyGZQLRToXZAndLS(djdyid);
			super.CopyGZQDZRToXZAndLS(djdyid);
			super.CopyGZZSToXZAndLS(djdyid);
			// super.CopyGZDJDYToXZAndLS(bdcs_djdy_gz.getId());// TODO 拷贝登记单元
			BDCDYLX dylx = ProjectHelper.GetBDCDYLX(getXMBH());
			// 拷贝关系表信息SC_YC_H_XZ
			// List<YC_SC_H_XZ> lst=getCommonDao().getDataList(YC_SC_H_XZ.class,
			// "SCBDCDYID ='"+bdcs_djdy_gz.getBDCDYID()+"'");
			// for(YC_SC_H_XZ yc_sc_h_xz :lst)
			// {
			// //拷贝预测户
			// super.CopyYXZHToAndLS(bdcs_djdy_gz.getBDCDYID());
			// //拷贝预测实测关系表
			// super.CopyYC_SC_H_XZToAndLS(yc_sc_h_xz.getId());
			// }
			// 更新单元抵押状态
			SetDYDYZT(bdcs_djdy_gz.getBDCDYID(), dylx, "0");
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
		String hqlCondition = MessageFormat.format(
				"LYQLID=''{0}'' and xmbh=''{1}''", ycqlid, this.getXMBH());
		List<BDCS_QL_GZ> bdcs_ql_gz = getCommonDao().getDataList(
				BDCS_QL_GZ.class, hqlCondition);
		if (bdcs_ql_gz != null && bdcs_ql_gz.size() > 0) {
			result = true;
			StrError = "该抵押权正在办理登记，不能重复选取";
		}
		return result;
	}

	/**
	 * 判断是否存在在建工程抵押权登记信息
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
			// 在建工程抵押转现房抵押权登记
			fulSql.append("select ql.* from  BDCK.BDCS_QL_XZ ql left join BDCK.BDCS_DJDY_XZ djdy on djdy.DJDYID=ql.DJDYID ");
			fulSql.append(" left join BDCK.BDCS_H_XZY ych on ych.BDCDYID=djdy.BDCDYID ");
			fulSql.append(" left join BDCK.YC_SC_H_XZ ysh on ysh.YCBDCDYID=djdy.BDCDYID ");
//			fulSql.append(" where ql.QLLX='23' and djdy.BDCDYLX='032' and DJLX='100' and ysh.SCBDCDYID='"
			fulSql.append(" where ql.QLLX='23' and djdy.BDCDYLX='032' and DJLX in('100','300') and ysh.SCBDCDYID='"
					+ bdcs_h_xz.getId() + "'");
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
	protected BDCS_DJDY_GZ createDJDYfromXZ(String bdcdyid,
			BDCS_H_XZY bdcs_h_xzy) {
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
		List<BDCS_DJDY_GZ> djdys = getCommonDao().getDataList(
				BDCS_DJDY_GZ.class, xmbhFilter);
		if (djdys != null && djdys.size() > 0) {
			boolean isZipFile = false;
			if(djdys.size()>=10){
				isZipFile=true;
				String folderPath=GetProperties.getConstValueByKey("xmlPath") + "\\" + xmxx.getPROJECT_ID()+"_"+bljc;
				super.getShareMsgTools().deleteFolder(folderPath);
			}
			for (int idjdy = 0; idjdy < djdys.size(); idjdy++) {
				BDCS_DJDY_GZ djdy = djdys.get(idjdy);
				ConstValue.BDCDYLX dylx = ConstValue.BDCDYLX.initFrom(djdy
						.getBDCDYLX());
				ConstValue.DJDYLY dyly = ConstValue.DJDYLY.initFrom(djdy
						.getLY());
				RealUnit bdcdy = UnitTools.loadUnit(dylx, dyly,
						djdy.getBDCDYID());
				Rights bdcql = RightsTools.loadRightsByDJDYID(dyly, getXMBH(),
						djdy.getDJDYID());
				SubRights bdcfsql = RightsTools.loadSubRightsByRightsID(dyly,
						bdcql.getId());
				List<RightsHolder> bdcqlrs = RightsHolderTools
						.loadRightsHolders(dyly, djdy.getDJDYID(), getXMBH());
				MessageExport msg = super.getShareMsgTools().GetMsg(bdcdy,
						bdcql, bdcfsql, bdcqlrs, bljc, xmxx);
				if(isZipFile){
					String folderPath = super.getShareMsgTools().createXMLInFile(xmxx, msg, idjdy+1,bljc);
					if(idjdy == djdys.size()-1){//文件都生成到文件夹以后再压缩上传
						super.getShareMsgTools().SendMsg(folderPath, xmxx, bljc, djdy.getBDCDYLX());
					}
				}
				else{
					super.getShareMsgTools().SendMsg(msg, idjdy + 1, djdy.getBDCDYLX(), xmxx);				
				}
			}
		}
	}
	
	
	@Override
	public Map<String, String> exportXML(String path, String actinstID) {
		return exportXmlUtil.createXMLAndUp(path, actinstID);
	}
}
