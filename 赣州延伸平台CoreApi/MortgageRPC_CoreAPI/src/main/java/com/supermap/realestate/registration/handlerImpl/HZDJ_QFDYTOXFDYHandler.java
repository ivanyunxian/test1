package com.supermap.realestate.registration.handlerImpl;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.StringUtils;

import com.supermap.realestate.registration.ViewClass.ProjectInfo;
import com.supermap.realestate.registration.ViewClass.UnitStatus;
import com.supermap.realestate.registration.ViewClass.UnitTree;
import com.supermap.realestate.registration.handler.DJHandler;
import com.supermap.realestate.registration.model.BDCS_DJDY_GZ;
import com.supermap.realestate.registration.model.BDCS_DYXZ;
import com.supermap.realestate.registration.model.BDCS_FSQL_GZ;
import com.supermap.realestate.registration.model.BDCS_FSQL_LS;
import com.supermap.realestate.registration.model.BDCS_FSQL_XZ;
import com.supermap.realestate.registration.model.BDCS_H_GZ;
import com.supermap.realestate.registration.model.BDCS_H_XZ;
import com.supermap.realestate.registration.model.BDCS_H_XZY;
import com.supermap.realestate.registration.model.BDCS_QDZR_GZ;
import com.supermap.realestate.registration.model.BDCS_QDZR_XZ;
import com.supermap.realestate.registration.model.BDCS_QLR_GZ;
import com.supermap.realestate.registration.model.BDCS_QLR_XZ;
import com.supermap.realestate.registration.model.BDCS_QL_GZ;
import com.supermap.realestate.registration.model.BDCS_QL_LS;
import com.supermap.realestate.registration.model.BDCS_QL_XZ;
import com.supermap.realestate.registration.model.BDCS_SHYQZD_GZ;
import com.supermap.realestate.registration.model.BDCS_SHYQZD_XZ;
import com.supermap.realestate.registration.model.BDCS_SLLM_GZ;
import com.supermap.realestate.registration.model.BDCS_SQR;
import com.supermap.realestate.registration.model.BDCS_SYQZD_GZ;
import com.supermap.realestate.registration.model.BDCS_XMXX;
import com.supermap.realestate.registration.model.BDCS_ZH_GZ;
import com.supermap.realestate.registration.model.BDCS_ZRZ_GZ;
import com.supermap.realestate.registration.model.BDCS_ZRZ_XZ;
import com.supermap.realestate.registration.model.BDCS_ZS_GZ;
import com.supermap.realestate.registration.model.BDCS_ZS_XZ;
import com.supermap.realestate.registration.model.WFD_MAPPING;
import com.supermap.realestate.registration.model.interfaces.House;
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
import com.supermap.realestate.registration.util.ConstValue.DJZT;
import com.supermap.realestate.registration.util.ConstValue.QLLX;
import com.supermap.realestate.registration.util.ConstValue.SF;
import com.supermap.realestate.registration.util.ConstValue.SQRLB;
import com.supermap.realestate.registration.util.ConstValue.YGDJLX;
import com.supermap.realestate.registration.util.Global;
import com.supermap.realestate.registration.util.ObjectHelper;
import com.supermap.realestate.registration.util.ProjectHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.realestate.registration.util.exportXmlUtil;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;

/*
1、换证登记_换证+期房抵押转现房抵押
*/

/**
* 
* 换证登记_换证+期房抵押转现房抵押操作类
* @ClassName: ZY_YDYTODY_DJHandler 
* @author 俞学斌 
* @date 2015年9月12日 下午05:07:16
*/
public class HZDJ_QFDYTOXFDYHandler extends DJHandlerBase implements DJHandler {

	/**
	 * 构造函数
	 * @param info
	 */
	public HZDJ_QFDYTOXFDYHandler(ProjectInfo info) {
		super(info);
	}

	/**
	 * 添加不动产单元
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public boolean addBDCDY(String bdcdyid) {
		boolean bsuccess = false;
		CommonDao dao = getCommonDao();
		// 获取是否获取重新生成权证号配置
		String newqzh = "";
		String workflowcode = ProjectHelper.getWorkflowCodeByProjectID(this.getProject_id());
		List<WFD_MAPPING> listCode = getCommonDao().getDataList(WFD_MAPPING.class, "WORKFLOWCODE='" + workflowcode + "'");
		if (listCode != null && listCode.size() > 0) {
			newqzh = listCode.get(0).getNEWQZH();
		}
		//生成djdy
		BDCS_DJDY_GZ djdy = super.createDJDYfromXZ(bdcdyid);
		if (djdy != null) {
			RealUnit unit = null;
			try {
				unit=UnitTools.loadUnit(this.getBdcdylx(), DJDYLY.initFrom(djdy.getLY()), djdy.getBDCDYID());
			} catch (Exception e) {
				e.printStackTrace();
			}

//			BDCS_QL_GZ ql = super.createQL(djdy, unit);
//			// 生成附属权利
//			BDCS_FSQL_GZ fsql = super.createFSQL(djdy.getDJDYID());
//			fsql.setQLID(ql.getId());
//			ql.setFSQLID(fsql.getId());
//			// 如果是使用权宗地，把使用权面积加上
//			if (getBdcdylx().equals(BDCDYLX.SHYQZD)) {
//				BDCS_SHYQZD_XZ xzshyqzd = dao.get(BDCS_SHYQZD_XZ.class, bdcdyid);
//				if (xzshyqzd != null) {
//					fsql.setSYQMJ(xzshyqzd.getZDMJ());
//					ql.setQDJG(xzshyqzd.getJG());// 取得价格
//				}
//			}

			//拷贝ql和fsql
			BDCS_QL_GZ bdcs_ql_gz = null;
			String qllxarray = "('1','2','3','4','5','6','7','8','9','10','11','12','13','14','15','16','17','18')";
			String hql = " DJDYID='" + djdy.getDJDYID() + "' AND QLLX IN " + qllxarray;
			List<BDCS_QL_XZ> list = dao.getDataList(BDCS_QL_XZ.class, hql);
			if (list != null && list.size() > 0) {
					String qlid = list.get(0).getId();
					Rights ql = RightsTools.loadRights(DJDYLY.XZ, qlid);
				if (ql != null) {
					String fj = "";
				if (SF.NO.Value.equals(newqzh)) {
					// 拷贝权利信息（权证号不为空）
					 bdcs_ql_gz = super.CopyQLXXFromXZ(qlid);
					
					String sql = " WORKFLOWCODE='" + workflowcode + "'";
					List<WFD_MAPPING> mappings = dao.getDataList(WFD_MAPPING.class, sql);
					if (mappings != null && mappings.size() > 0) {
						WFD_MAPPING maping = mappings.get(0);
						if (("1").equals(maping.getISINITATATUS())){
							fj = bdcs_ql_gz.getFJ();
							fj = getStatus(fj, bdcs_ql_gz.getDJDYID(), bdcdyid, this.getBdcdylx().Value);
							bdcs_ql_gz.setFJ(fj);
							dao.update(bdcs_ql_gz);
						}
					}
				} else {
					// 拷贝权利信息（权证号为空）
					 bdcs_ql_gz = super.CopyQLXXFromXZExceptBDCQZH(qlid);
					String sql = " WORKFLOWCODE='" + workflowcode + "'";
					List<WFD_MAPPING> mappings = dao.getDataList(WFD_MAPPING.class, sql);
					if (mappings != null && mappings.size() > 0) {
						WFD_MAPPING maping = mappings.get(0);
						if (("1").equals(maping.getISINITATATUS())){
							fj = bdcs_ql_gz.getFJ();
							fj = getStatus(fj, bdcs_ql_gz.getDJDYID(), bdcdyid, this.getBdcdylx().Value);
							bdcs_ql_gz.setFJ(fj);
							dao.update(bdcs_ql_gz);
						}
					}
				}}
			}
			// 保存djdy
			dao.save(djdy);
			dao.flush();
			// 拷贝权利人到申请人
			CopySQRFromGZQLR(djdy.getDJDYID(), bdcs_ql_gz.getQLLX(), this.getXMBH(), bdcs_ql_gz.getId(),SQRLB.YF.Value);
					
		    //抵押权
			StringBuilder builer=new StringBuilder();
			builer.append("SELECT DYQL.QLID AS QLID FROM BDCK.BDCS_QL_XZ DYQL ");
			builer.append("LEFT JOIN BDCK.BDCS_DJDY_XZ DJDY_YC ON DYQL.DJDYID=DJDY_YC.DJDYID ");
			builer.append("LEFT JOIN BDCK.YC_SC_H_XZ GX ON GX.YCBDCDYID=DJDY_YC.BDCDYID ");
			builer.append("AND GX.SCBDCDYID='");
			builer.append(bdcdyid);
			builer.append("' ");
			builer.append("WHERE DYQL.QLLX='23' AND GX.SCBDCDYID IS NOT NULL");
			List<Map> listDYQ=dao.getDataListByFullSql(builer.toString());
			if(listDYQ!=null&&listDYQ.size()>0){
				CopyQLXXFromYDY(StringHelper.formatObject(listDYQ.get(0).get("QLID")),djdy);
			}else{
				//生成抵押权
				BDCS_QL_GZ dyql = super.createQL(djdy, unit);
				//生成抵押权附属权利
				BDCS_FSQL_GZ dyfsql = super.createFSQL(djdy.getDJDYID());
				
				//关联抵押权权利-附属权利，设置抵押物类型、抵押不动产类型、预告登记种类
				dyql.setFSQLID(dyfsql.getId());
				dyql.setDJLX(DJLX.CSDJ.Value);
				dyql.setQLLX(QLLX.DIYQ.Value);
				dyql.setCZFS(CZFS.GTCZ.Value);//设置抵押权的持证方式为共同持证。
				dyfsql.setQLID(dyql.getId());
				dyfsql.setDYWLX(getDYBDCLXfromBDCDYLX(getBdcdylx()));
				dyfsql.setDYBDCLX(getDYBDCLXfromBDCDYLX(getBdcdylx()));
				dyfsql.setYGDJZL(YGDJLX.YSSPFDYQYGDJ.Value.toString());
				int dysw=RightsTools.getMaxMortgageSWS(djdy.getDJDYID());
				dyfsql.setDYSW(dysw+1);
				dao.save(dyql);
				dao.save(dyfsql);
				dao.flush();
			}
			unit.setDJZT(DJZT.DJZ.Value); // 设置登记状态
			
			
			// 将预抵押的权利人拷贝到申请人
			String fulsql=" SELECT QLID FROM BDCK.BDCS_QL_XZ QL LEFT JOIN BDCK.BDCS_DJDY_XZ DJDY ON QL.DJDYID=DJDY.DJDYID LEFT JOIN BDCK.YC_SC_H_XZ GX ON DJDY.BDCDYID =GX.YCBDCDYID WHERE GX.SCBDCDYID='"+bdcdyid+"' AND QL.QLLX='23' AND QL.DJLX='700'";
			List<Map> listmap=getCommonDao().getDataListByFullSql(fulsql);
			if(listmap!=null && listmap.size()>0)
			{
				for(Map mp:listmap){
					String qlid=StringHelper.isEmpty(mp.get("QLID"))?"":mp.get("QLID").toString();
					if(!StringHelper.isEmpty(qlid)){
						List<RightsHolder> listdyqrs=RightsHolderTools.loadRightsHolders(DJDYLY.XZ, qlid);
						if(listdyqrs!=null && listdyqrs.size()>0)
						{
							for(RightsHolder holder:listdyqrs)
							{
								BDCS_QLR_XZ xzqlr=(BDCS_QLR_XZ)holder;
								BDCS_SQR sqr=super.copyXZQLRtoSQR(xzqlr, SQRLB.JF);
								if(sqr!=null){
									getCommonDao().save(sqr);
								}
								
							}
						}
					}
				}
			}
		}
		dao.flush();
		bsuccess = true;
		return bsuccess;
	}
	
	protected void CopyQLXXFromYDY(String qlid,BDCS_DJDY_GZ djdy) {
		StringBuilder builer = new StringBuilder();
		builer.append(" QLID='").append(qlid).append("'");
		String strQuery = builer.toString();
		BDCS_QL_XZ bdcs_ql_xz = getCommonDao().get(BDCS_QL_XZ.class, qlid);
		if (bdcs_ql_xz != null) {
			//是否自动继承原抵押权信息的查封,0:否，1：是
			String isextendmortgageinfo = "";
			String workflowcode = ProjectHelper.getWorkflowCodeByProjectID(getProject_id());
			List<WFD_MAPPING> listCode = getCommonDao().getDataList(WFD_MAPPING.class, "WORKFLOWCODE='" + workflowcode + "'");
			if ((listCode != null) && (listCode.size() > 0)) {
				isextendmortgageinfo = ((WFD_MAPPING) listCode.get(0)).getISEXTENDMORTGAGEINFO();
			}
			
			String gzqlid = getPrimaryKey();
			String gzfsqlid = getPrimaryKey();
			// 拷贝权利
			BDCS_QL_GZ bdcs_ql_gz = ObjectHelper.copyQL_XZToGZ(bdcs_ql_xz);
			bdcs_ql_gz.setId(gzqlid);//重置权利ID
			bdcs_ql_gz.setDJDYID(djdy.getDJDYID());//重置登记单元id
			bdcs_ql_gz.setBDCDYH(djdy.getBDCDYH());//重置不动产单元号
			bdcs_ql_gz.setYWH(getProject_id());//重置业务号
			bdcs_ql_gz.setDJLX(getDjlx().Value);//重置登记类型
			bdcs_ql_gz.setFSQLID(gzfsqlid);//重置附属权利ID
			bdcs_ql_gz.setXMBH(getXMBH());//重置项目编号
			bdcs_ql_gz.setLYQLID(qlid);//设定来源权利ID
			bdcs_ql_gz.setDJSJ(null);//重置登记时间
			bdcs_ql_gz.setZSBH("");//重置证书编号
			bdcs_ql_gz.setDBR("");//重置登簿人
			bdcs_ql_gz.setBDCQZH("");//重置不动产权证明号
			// bdcs_ql_gz.setDJYY("");//登记原因赞不重置
			// bdcs_ql_gz.setFJ("");//登记附记赞不重置
			if ("0".equals(isextendmortgageinfo)) {			
				bdcs_ql_gz.setQLQSSJ(null);
				bdcs_ql_gz.setQLJSSJ(null);
				bdcs_ql_gz.setDJYY(null);
				bdcs_ql_gz.setFJ(null);
			}
			getCommonDao().save(bdcs_ql_gz);
			if (bdcs_ql_xz.getFSQLID() != null) {
				BDCS_FSQL_XZ bdcs_fsql_xz = getCommonDao().get(BDCS_FSQL_XZ.class, bdcs_ql_xz.getFSQLID());
				if (bdcs_fsql_xz != null) {
					// 拷贝附属权利
					BDCS_FSQL_GZ bdcs_fsql_gz = ObjectHelper.copyFSQL_XZToGZ(bdcs_fsql_xz);
					bdcs_fsql_gz.setQLID(gzqlid);//重置权利ID
					bdcs_fsql_gz.setId(gzfsqlid);//重置附属权利ID
					bdcs_fsql_gz.setXMBH(getXMBH());//重置项目编号
					bdcs_fsql_gz.setDJDYID(djdy.getDJDYID());//重置登记单元id
					bdcs_fsql_gz.setBDCDYH(djdy.getBDCDYH());//重置不动产单元号
					bdcs_fsql_gz.setDYR("");//重置抵押人
					bdcs_fsql_gz.setZXFJ("");//重置抵押人
					bdcs_fsql_gz.setZXSJ(null);//重置注销时间
					bdcs_fsql_gz.setZXDBR("");//重置注销登簿人人
					if ("0".equals(isextendmortgageinfo)) {	
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
			}
			Map<String,String> lyzsid_zsid=new HashMap<String,String>();
			// 获取权利人集合
			List<BDCS_QLR_XZ> qlrs = getCommonDao().getDataList(BDCS_QLR_XZ.class, strQuery);
			if (qlrs != null && qlrs.size() > 0) {
				for (int iqlr = 0; iqlr < qlrs.size(); iqlr++) {
					BDCS_QLR_XZ bdcs_qlr_xz = qlrs.get(iqlr);
					if (bdcs_qlr_xz != null) {
						BDCS_SQR sqr=CopySQRFromDYQR(bdcs_qlr_xz,gzqlid);
						getCommonDao().save(sqr);
						// 拷贝权利人
						String gzqlrid = "";					
						if ("1".equals(isextendmortgageinfo)) {
							gzqlrid = getPrimaryKey();
							BDCS_QLR_GZ bdcs_qlr_gz = ObjectHelper.copyQLR_XZToGZ(bdcs_qlr_xz);
							bdcs_qlr_gz.setId(gzqlrid);//重置权利人ID
							bdcs_qlr_gz.setQLID(gzqlid);//重置权利ID
							bdcs_qlr_gz.setXMBH(getXMBH());//重置项目编号
							bdcs_qlr_gz.setBDCQZH("");//重置不动产权证明号
							bdcs_qlr_gz.setSQRID(sqr.getId());//重置申请人id
							getCommonDao().save(bdcs_qlr_gz);
							
							// 获取证书集合
							StringBuilder builder = new StringBuilder();
							builder.append(" id IN (");
							builder.append("select ZSID FROM BDCS_QDZR_XZ WHERE QLID ='");
							builder.append(qlid).append("'").append(" AND QLRID='");
							builder.append(bdcs_qlr_xz.getId()).append("')");
							String strQueryZS = builder.toString();
							List<BDCS_ZS_XZ> zss = getCommonDao().getDataList(BDCS_ZS_XZ.class, strQueryZS);
							if (zss != null && zss.size() > 0) {
								BDCS_ZS_XZ bdcs_zs_xz = zss.get(0);
								if (!lyzsid_zsid.containsKey(bdcs_zs_xz.getId())) {
									String gzzsid = getPrimaryKey();
									BDCS_ZS_GZ bdcs_zs_gz = ObjectHelper.copyZS_XZToGZ(bdcs_zs_xz);
									bdcs_zs_gz.setId(gzzsid);//重置证书id
									bdcs_zs_gz.setXMBH(getXMBH());//重置项目编号
									bdcs_zs_gz.setBDCQZH("");//重置不动产权证明号
									bdcs_zs_gz.setZSBH("");//重置证书编号
									getCommonDao().save(bdcs_zs_gz);
									lyzsid_zsid.put(bdcs_zs_xz.getId(), gzzsid);
								}
								// 获取权地证人集合
								StringBuilder builderQDZR = new StringBuilder();
								builderQDZR.append(strQuery);
								builderQDZR.append(" AND ZSID='");
								builderQDZR.append(bdcs_zs_xz.getId());
								builderQDZR.append("' AND QLID='");
								builderQDZR.append(qlid);
								builderQDZR.append("' AND QLRID='");
								builderQDZR.append(bdcs_qlr_xz.getId());
								builderQDZR.append("')");
								List<BDCS_QDZR_XZ> qdzrs = getCommonDao().getDataList(BDCS_QDZR_XZ.class, builderQDZR.toString());
								if (qdzrs != null && qdzrs.size() > 0) {
									for (int iqdzr = 0; iqdzr < qdzrs.size(); iqdzr++) {
										BDCS_QDZR_XZ bdcs_qdzr_xz = qdzrs.get(iqdzr);
										if (bdcs_qdzr_xz != null) {
											// 拷贝权地证人
											BDCS_QDZR_GZ bdcs_qdzr_gz = ObjectHelper.copyQDZR_XZToGZ(bdcs_qdzr_xz);
											bdcs_qdzr_gz.setId(getPrimaryKey());
											bdcs_qdzr_gz.setZSID(lyzsid_zsid.get(bdcs_zs_xz.getId()));//重置证书ID
											bdcs_qdzr_gz.setQLID(gzqlid);//重置权利ID
											bdcs_qdzr_gz.setFSQLID(gzfsqlid);//重置附属权利ID
											bdcs_qdzr_gz.setQLRID(gzqlrid);//重置权利人ID
											bdcs_qdzr_gz.setXMBH(getXMBH());//重置项目编号
											bdcs_qdzr_gz.setDJDYID(djdy.getDJDYID());//重置登记单元ID
											bdcs_qdzr_gz.setBDCDYH(djdy.getBDCDYH());//重置不动产权证明号
											getCommonDao().save(bdcs_qdzr_gz);
										}
									}
								}
							}
						}
						
					}
				}
			}
			bdcs_ql_xz.setDJZT("02");
			getCommonDao().update(bdcs_ql_xz);
		}
	}
	
	protected BDCS_SQR CopySQRFromDYQR(BDCS_QLR_XZ qlr,String glqlid) {
		String sqrid=getPrimaryKey();
		BDCS_SQR sqr = new BDCS_SQR();
		sqr.setGYFS(qlr.getGYFS());
		sqr.setFZJG(qlr.getFZJG());
		sqr.setGJDQ(qlr.getGJ());
		sqr.setGZDW(qlr.getGZDW());
		sqr.setXB(qlr.getXB());
		sqr.setHJSZSS(qlr.getHJSZSS());
		sqr.setSSHY(qlr.getSSHY());
		sqr.setYXBZ(qlr.getYXBZ());
		sqr.setQLBL(qlr.getQLBL());
		sqr.setQLMJ(StringHelper.formatObject(qlr.getQLMJ()));
		sqr.setSQRXM(qlr.getQLRMC());
		sqr.setSQRLB(SQRLB.JF.Value);
		sqr.setSQRLX(qlr.getQLRLX());
		sqr.setDZYJ(qlr.getDZYJ());
		sqr.setLXDH(qlr.getDH());
		sqr.setZJH(qlr.getZJH());
		sqr.setZJLX(qlr.getZJZL());
		sqr.setTXDZ(qlr.getDZ());
		sqr.setYZBM(qlr.getYB());
		sqr.setXMBH(getXMBH());
		sqr.setId(sqrid);
		sqr.setGLQLID(glqlid);
		return sqr;
	}
	
	/**
	 * 获得抵押物类型
	 * @作者 diaoliwei
	 * @创建时间 2015年7月25日下午10:18:25
	 * @param bdcdylx
	 * @return
	 */
	protected String getDYBDCLXfromBDCDYLX(BDCDYLX bdcdylx) {
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
	 * 写入登记簿
	 */
	@Override
	public boolean writeDJB() {
		
		if (super.isCForCFING()) {
			return false;
		}
		
		String strSqlXMBH = ProjectHelper.GetXMBHCondition(getXMBH());
		List<BDCS_DJDY_GZ> djdys = getCommonDao().getDataList(BDCS_DJDY_GZ.class, strSqlXMBH);
		if (djdys != null && djdys.size() > 0) {
			for (int idjdy = 0; idjdy < djdys.size(); idjdy++) {
				BDCS_DJDY_GZ bdcs_djdy_gz = djdys.get(idjdy);
				if (bdcs_djdy_gz != null) {
					String djdyid = bdcs_djdy_gz.getDJDYID();
					ZXYDYDJ(djdyid);
					super.removeZSFromXZByQLLX(djdyid);
					super.removeQDZRFromXZByQLLX(djdyid);
					super.removeQLRFromXZByQLLX(djdyid);
					super.removeQLFromXZByQLLX(djdyid);
					
					super.CopyGZQLToXZAndLS(djdyid);
					super.CopyGZQLRToXZAndLSNotOnQLLX(djdyid);
					super.CopyGZQDZRToXZAndLS(djdyid);
					super.CopyGZZSToXZAndLS(djdyid);
					
					//删除期房对应的预告登记以及预抵押登记
					//现房的不动产单元ID
					String bdcdyid = bdcs_djdy_gz.getBDCDYID();
					
					//删除预告登记权利
					super.deletePreHouseRights(bdcdyid,"4","700","因换证登记_换证+期房抵押转现房抵押，自动注销");
					//删除预抵押登记权利
					super.deletePreHouseRights(bdcdyid,"23","700","因换证登记_换证+期房抵押转现房抵押，自动注销");
				}
			}
		}
		this.SetSFDB();
		getCommonDao().flush();
		super.alterCachedXMXX();
		return true;
	}
	
	private void ZXYDYDJ(String djdyid){
		String dbr = Global.getCurrentUserName();
		List<BDCS_QL_GZ> listql=getCommonDao().getDataList(BDCS_QL_GZ.class, "DJDYID='"+djdyid+"' AND QLLX='23' AND LYQLID IS NOT NULL");
		if(listql!=null&&listql.size()>0){
			for(int iql=0;iql<listql.size();iql++){
				BDCS_QL_GZ ql=listql.get(iql);
				if(ql!=null&&!StringHelper.isEmpty(ql.getLYQLID())){
					super.removeQLXXFromXZByQLID(ql.getLYQLID());
					BDCS_QL_LS dyq_ly=getCommonDao().get(BDCS_QL_LS.class, ql.getLYQLID());
					if(dyq_ly!=null){
						if(StringHelper.isEmpty(dyq_ly.getFSQLID())){
							BDCS_FSQL_LS dyqfs_ly=getCommonDao().get(BDCS_FSQL_LS.class, dyq_ly.getFSQLID());
							if(dyqfs_ly!=null){
								dyqfs_ly.setZXDBR(dbr);
								dyqfs_ly.setZXSJ(new Date());
								dyqfs_ly.setZXFJ(ql.getFJ());
								dyqfs_ly.setZXDYYY(ql.getDJYY());
								dyqfs_ly.setZXDYYWH(ql.getYWH());
								getCommonDao().update(dyqfs_ly);
							}
						}
					}
				}
			}
		}
	}

	/**
	 * 移除不动产单元
	 */
	@Override
	public boolean removeBDCDY(String bdcdyid) {
		boolean bsuccess = false;
		// 找到登记单元表，移除记录
		CommonDao baseCommonDao = this.getCommonDao();
		BDCS_DJDY_GZ djdy = super.removeDJDY(bdcdyid);// list.get(0);
		if (djdy != null) {
			String djdyid = djdy.getDJDYID();
			// 删除权利关联申请人
			super.RemoveSQRByQLID(djdyid, getXMBH());


			//删除权利、附属权利、权利人、证书、权地证人关系
			String _hqlCondition=MessageFormat.format("XMBH=''{0}'' AND DJDYID=''{1}''", getXMBH(),djdyid);
			RightsTools.deleteRightsAllByCondition(DJDYLY.GZ, _hqlCondition);
		}
		baseCommonDao.flush();
		bsuccess = true;
		return bsuccess;
	}

	/**
	 * 获取不动产单元列表
	 */
	@Override
	public List<UnitTree> getUnitTree() {
		String xmbhFilter = ProjectHelper.GetXMBHCondition(getXMBH());
		CommonDao dao = this.getCommonDao();
		List<UnitTree> list = new ArrayList<UnitTree>();
		List<BDCS_DJDY_GZ> djdys = dao.getDataList(BDCS_DJDY_GZ.class, xmbhFilter);
		if (djdys != null && djdys.size() > 0) {
			for (int i = 0; i < djdys.size(); i++) {
				BDCS_DJDY_GZ djdy = djdys.get(i);
				UnitTree tree = new UnitTree();
				StringBuilder BuilderQL=new StringBuilder();
				BuilderQL.append(xmbhFilter).append(" AND DJDYID='").append(djdy.getDJDYID()).append("'");
				List<BDCS_QL_GZ> qls=dao.getDataList(BDCS_QL_GZ.class, BuilderQL.toString());
				if(qls!=null){
					for(int iql=0;iql<qls.size();iql++){
						BDCS_QL_GZ ql = qls.get(iql);
						if(ql.getQLLX().equals(QLLX.DIYQ.Value)){
							tree.setDIYQQlid(ql.getId());
							tree.setOlddiyqqlid(ql.getLYQLID());
							
						}else{
							tree.setQlid(ql.getId());
						}
					}
				}
				tree.setId(djdy.getBDCDYID());
				tree.setBdcdyid(djdy.getBDCDYID());
				tree.setBdcdylx(djdy.getBDCDYLX());
				tree.setDjdyid(djdy.getDJDYID());
				String ly = StringUtils.isEmpty(djdy.getLY()) ? "gz" : DJDYLY.initFrom(djdy.getLY()) == null ? "gz" : DJDYLY.initFrom(djdy.getLY()).Name;
				tree.setLy(ly);
				String zl = getZL(tree, djdy.getDJDYID(), djdy.getBDCDYLX(), djdy.getBDCDYID(), ly);
				tree.setText(zl);
				// 如果是户的话，把房号也加上
				if (djdy.getBDCDYLX().equals(BDCDYLX.H.Value)) {
					if (!StringHelper.isEmpty(djdy.getLY())) {
						DJDYLY ely = DJDYLY.initFrom(djdy.getLY());
						House house = (House) (UnitTools.loadUnit(BDCDYLX.H, ely, djdy.getBDCDYID()));
						if (house != null) {
							String fh = house.getFH();
							tree.setFh(fh);
							tree.setText("坐落:" + zl + "|" + "房号:" + (fh == null ? "" : fh));
						}
					}
				}
				tree.setZl(zl);
				list.add(tree);
			}
		}
		return list;
	}
	
	private String getZL(UnitTree tree, String djdyid, String bdcdylx, String bdcdyid, String djdyly) {
		String zl = "";
		CommonDao dao = getCommonDao();
		BDCDYLX dylx = BDCDYLX.initFrom(bdcdylx);
		if (djdyly.equals(DJDYLY.GZ.Name)) {
			if (dylx.equals(BDCDYLX.SHYQZD)) {
				BDCS_SHYQZD_GZ shyqzd = dao.get(BDCS_SHYQZD_GZ.class, bdcdyid);
				zl = shyqzd == null ? "" : shyqzd.getZL();
			} else if (dylx.equals(BDCDYLX.H)) {
				BDCS_H_GZ h = dao.get(BDCS_H_GZ.class, bdcdyid);
				tree.setCid(h.getCID());
				tree.setZdbdcdyid(h.getZDBDCDYID());
				tree.setZrzbdcdyid(h.getZRZBDCDYID());
				tree.setLjzbdcdyid(h.getLJZID());
				zl = h == null ? "" : h.getZL();
			} else if (dylx.equals(BDCDYLX.ZRZ)) {
				BDCS_ZRZ_GZ zrz = dao.get(BDCS_ZRZ_GZ.class, bdcdyid);
				tree.setZdbdcdyid(zrz.getZDBDCDYID());
				zl = zrz == null ? "" : zrz.getZL();
			} else if (dylx.equals(BDCDYLX.SYQZD))// sunhb-2015-06-23添加所有权宗地，获取坐落
			{
				BDCS_SYQZD_GZ syqzd = dao.get(BDCS_SYQZD_GZ.class, bdcdyid);
				zl = syqzd == null ? "" : syqzd.getZL();
			} else if (dylx.equals(BDCDYLX.HY))// sunhb-2015-06-23添加宗海，获取坐落
			{
				BDCS_ZH_GZ zh = dao.get(BDCS_ZH_GZ.class, bdcdyid);
				zl = zh == null ? "" : zh.getZL();
			} else if (dylx.equals(BDCDYLX.LD))// sunhb-2015-06-23添加林地，获取坐落
			{
				BDCS_SLLM_GZ ld = dao.get(BDCS_SLLM_GZ.class, bdcdyid);
				tree.setZdbdcdyid(ld.getZDBDCDYID());
				zl = ld == null ? "" : ld.getZL();
			} else {
				// TODO 刘树峰:获取其他类型不动产单元的坐落
			}
		} else {// 来源于现状，把原来的所有权/使用权的权利ID也加上
			if (dylx.equals(BDCDYLX.SHYQZD)) {
				BDCS_SHYQZD_XZ shyqzd = dao.get(BDCS_SHYQZD_XZ.class, bdcdyid);
				zl = shyqzd == null ? "" : shyqzd.getZL();
			} else if (dylx.equals(BDCDYLX.H)) {
				BDCS_H_XZ shyqzd = dao.get(BDCS_H_XZ.class, bdcdyid);
				if (shyqzd != null) {
					zl = shyqzd == null ? "" : shyqzd.getZL();

					tree.setCid(shyqzd.getCID());
					tree.setZdbdcdyid(shyqzd.getZDBDCDYID());
					tree.setZrzbdcdyid(shyqzd.getZRZBDCDYID());
					tree.setLjzbdcdyid(shyqzd.getLJZID());
				}

			} else if (dylx.equals(BDCDYLX.YCH)) {
				BDCS_H_XZY bdcs_h_xzy = dao.get(BDCS_H_XZY.class, bdcdyid);
				if (bdcs_h_xzy != null) {
					zl = bdcs_h_xzy.getZL();
					tree.setId(bdcs_h_xzy.getCID());
					tree.setZdbdcdyid(bdcs_h_xzy.getZDBDCDYID());
					tree.setZrzbdcdyid(bdcs_h_xzy.getZRZBDCDYID());
					tree.setLjzbdcdyid(bdcs_h_xzy.getLJZID());
				}
			} else if (dylx.equals(BDCDYLX.ZRZ)) {
				BDCS_ZRZ_XZ zrz = dao.get(BDCS_ZRZ_XZ.class, bdcdyid);
				tree.setZdbdcdyid(zrz.getZDBDCDYID());
				zl = zrz == null ? "" : zrz.getZL();
			} else {
				// TODO 刘树峰:获取其他类型不动产单元的坐落
			}
			// 这块的逻辑有点问题，原来的权利ID应该包含两种，一种是所有权/使用权ID，一种是他项权利ID，例如
			// 抵押权的转移，就包含了被抵押单元的所有权权利和转移前的抵押权
			String qllxarray = " ('1','2','3','4','5','6','7','8','9','10','11','12','13','14','15','16','17','18')";
			String hqlCondition = MessageFormat.format(" DJDYID IN (SELECT DJDYID FROM BDCS_DJDY_XZ WHERE BDCDYID=''{0}'') AND QLLX IN {1} ORDER BY DJSJ", bdcdyid, qllxarray);
			List<BDCS_QL_XZ> listxzql = dao.getDataList(BDCS_QL_XZ.class, hqlCondition);
			if (listxzql != null && listxzql.size() > 0) {
				BDCS_QL_XZ ql = listxzql.get(0);
				tree.setOldqlid(ql.getId());
			}
		}
		return zl;
	}

	/**
	 * 根据申请人ID添加权利人
	 */
	@Override
	public void addQLRbySQRArray(String qlid, Object[] sqrids) {
		super.addQLRbySQRs(qlid, sqrids);
		CommonDao dao = getCommonDao();
		BDCS_QL_GZ ql=dao.get(BDCS_QL_GZ.class, qlid);
		if(!ql.getQLLX().equals(QLLX.DIYQ.Value)){
			String xmbhFilter=ProjectHelper.GetXMBHCondition(getXMBH());
			StringBuilder BuilderQLR=new StringBuilder();
			BuilderQLR.append(xmbhFilter).append(" AND QLID='").append(qlid).append("' ORDER BY SXH");
			List<BDCS_QLR_GZ> qlrs=dao.getDataList(BDCS_QLR_GZ.class, BuilderQLR.toString());
			if(qlrs!=null&&qlrs.size()>0){
				StringBuilder builderDYR=new StringBuilder();
				int indexdyr=0;
				for(int iqlr=0;iqlr<qlrs.size();iqlr++){
					if(indexdyr==0){
						builderDYR.append(qlrs.get(iqlr).getQLRMC());
					}else{
						builderDYR.append(",").append(qlrs.get(iqlr).getQLRMC());
					}
					indexdyr++;
				}
				StringBuilder BuilderFSQL=new StringBuilder();
				BuilderFSQL.append(xmbhFilter).append(" AND DJDYID='").append(ql.getDJDYID()).append("'");
				List<BDCS_FSQL_GZ> fsqls=dao.getDataList(BDCS_FSQL_GZ.class, BuilderFSQL.toString());
				if(fsqls!=null&&fsqls.size()>0){
					for(int ifsql=0;ifsql<fsqls.size();ifsql++){
						BDCS_FSQL_GZ fsql=fsqls.get(ifsql);
						if(!fsql.getQLID().equals(qlid)){
							fsql.setDYR(builderDYR.toString());
							dao.update(fsql);
						}
					}
				}
			}
			dao.flush();
		}
	}

	/**
	 * 移除权利人
	 */
	@Override
	public void removeQLR(String qlid, String qlrid) {
		CommonDao dao = getCommonDao();
		BDCS_QL_GZ ql=dao.get(BDCS_QL_GZ.class, qlid);
		super.removeqlr(qlrid, qlid);
		//更新抵押人
		if(!ql.getQLLX().equals(QLLX.DIYQ.Value)){
			List<BDCS_QLR_GZ> qlrs = dao.getDataList(BDCS_QLR_GZ.class, "QLID ='"+ qlid +"' ORDER BY SXH");
			StringBuilder builderDYR=new StringBuilder();
			int indexdyr=0;
			for(int iqlr=0;iqlr<qlrs.size();iqlr++){
				if(qlrid.equals(qlrs.get(iqlr).getId())){
					continue;
				}
				if(indexdyr==0){
					builderDYR.append(qlrs.get(iqlr).getQLRMC());
				}else{
					builderDYR.append(",").append(qlrs.get(iqlr).getQLRMC());
				}
				indexdyr++;
			}
			List<BDCS_FSQL_GZ> fsqlList = dao.getDataList(BDCS_FSQL_GZ.class, " XMBH='" + getXMBH() + "' and DJDYID='"+ql.getDJDYID()+"'");
			if(fsqlList!=null&&fsqlList.size()>0){
				for(int ifsql=0;ifsql<fsqlList.size();ifsql++){
					BDCS_FSQL_GZ fsql=fsqlList.get(ifsql);
					if(!fsql.getQLID().equals(qlid)){
						fsql.setDYR(builderDYR.toString());
						dao.update(fsql);
					}
				}
			}
		}
		dao.flush();
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
		return super.getErrMessage();
	}

	@Override
	public Map<String,String> exportXML(String path, String actinstID) {
		return exportXmlUtil.createXMLAndUp(path,actinstID);
	}

	/**
	 * 共享信息
	 * 
	 * @作者 俞学斌
	 * @创建时间 2015年8月16日下午16:37:43
	 * @return
	 */
	@Override
	public void SendMsg(String bljc){
		BDCS_XMXX xmxx=getCommonDao().get(BDCS_XMXX.class, super.getXMBH());
		String xmbhFilter=ProjectHelper.GetXMBHCondition(super.getXMBH());
		List<BDCS_DJDY_GZ> djdys = getCommonDao().getDataList(
				BDCS_DJDY_GZ.class, xmbhFilter);
		if (djdys != null && djdys.size() > 0) {
			for (int idjdy = 0; idjdy < djdys.size(); idjdy++) {
				BDCS_DJDY_GZ djdy = djdys.get(idjdy);
				ConstValue.BDCDYLX dylx = ConstValue.BDCDYLX.initFrom(djdy.getBDCDYLX());
				ConstValue.DJDYLY dyly = ConstValue.DJDYLY.initFrom(djdy.getLY());
				RealUnit bdcdy = UnitTools.loadUnit(dylx, dyly, djdy.getBDCDYID());
				Rights bdcql=RightsTools.loadRightsByDJDYID(ConstValue.DJDYLY.GZ, getXMBH(), djdy.getDJDYID());
				SubRights bdcfsql=RightsTools.loadSubRightsByRightsID(ConstValue.DJDYLY.GZ, bdcql.getId());
				List<RightsHolder> bdcqlrs=RightsHolderTools.loadRightsHolders(ConstValue.DJDYLY.GZ, djdy.getDJDYID(), getXMBH());
				MessageExport msg= super.getShareMsgTools().GetMsg(bdcdy,bdcql,bdcfsql,bdcqlrs,bljc,xmxx);
				super.getShareMsgTools().SendMsg(msg, idjdy+1,djdy.getBDCDYLX(),xmxx);
			}
		}
	}
	
	@SuppressWarnings("rawtypes")
	private String getStatus(String fj, String djdyid, String bdcdyid, String bdcdylx) {
		UnitStatus status = new UnitStatus();
		// 在办状态
		StringBuilder builder = new StringBuilder();
		builder.append("SELECT XMXX.YWLSH AS YWLSH,XMXX.DJLX AS XMDJLX,XMXX.QLLX AS XMQLLX, ");
		builder.append("QL.DJLX AS QLDJLX,QL.QLLX AS QLQLLX ");
		builder.append("FROM BDCK.BDCS_QL_GZ QL ");
		builder.append("LEFT JOIN BDCK.BDCS_XMXX XMXX ");
		builder.append("ON QL.XMBH=XMXX.XMBH ");
		builder.append("WHERE XMXX.DJLX<>'400' AND NVL2(XMXX.SFDB,'1','0')<>'1' ");
		builder.append("AND QL.DJDYID='" + djdyid + "' ");
		List<Map> qls_gz = getCommonDao().getDataListByFullSql(builder.toString());
		for (Map ql : qls_gz) {
			String xmdjlx = StringHelper.formatDouble(ql.get("XMDJLX"));
			String xmqllx = StringHelper.formatDouble(ql.get("XMQLLX"));
			if (DJLX.CFDJ.Value.equals(xmdjlx)) {
				if ("98".equals(xmqllx)) {
					status.setSeizureState("正在办理查封");
				}
			}
			if (DJLX.YYDJ.Value.equals(xmdjlx)) {
				status.setObjectionState("正在办理异议");
			} else if (DJLX.YGDJ.Value.equals(xmdjlx)) {
				if (QLLX.QTQL.Value.equals(xmqllx)) {
					status.setTransferNoticeState("正在办理转移预告");
				} else if (QLLX.DIYQ.Value.equals(xmqllx)) {
					if (BDCDYLX.YCH.Value.equals(bdcdylx)) {
						status.setMortgageState("正在办理抵押");
					} else {
						status.setMortgageNoticeState("正在办理抵押预告");
					}
				}
			} else if (QLLX.DIYQ.Value.equals(xmqllx)) {
				status.setMortgageState("正在办理抵押");
			}
		}
		// 已办状态
		List<Rights> qls_xz = RightsTools.loadRightsByCondition(DJDYLY.XZ, "DJDYID='" + djdyid + "'");
		for (Rights ql : qls_xz) {
			String djlx = ql.getDJLX();
			String qllx = ql.getQLLX();
			if (DJLX.CFDJ.Value.equals(djlx)) {
				status.setSeizureState("已查封");
			}
			if (DJLX.YYDJ.Value.equals(djlx)) {
				status.setObjectionState("已异议");
			} else if (DJLX.YGDJ.Value.equals(djlx)) {
				if (QLLX.QTQL.Value.equals(qllx)) {
					status.setTransferNoticeState("已转移预告");
				} else if (QLLX.DIYQ.Value.equals(qllx)) {
					if (BDCDYLX.YCH.Value.equals(bdcdylx)) {
						status.setMortgageState("已抵押");
					} else {
						status.setMortgageNoticeState("已抵押预告");
					}
				}
			} else if (QLLX.DIYQ.Value.equals(qllx)) {
				status.setMortgageState("已抵押");
			}
		}

		List<BDCS_DYXZ> list_limit = getCommonDao().getDataList(BDCS_DYXZ.class,
				"YXBZ<>'2' AND BDCDYID='" + bdcdyid + "' AND BDCDYLX='" + bdcdylx + "' ORDER BY YXBZ ");
		if (list_limit != null && list_limit.size() > 0) {
			for (BDCS_DYXZ limit : list_limit) {
				if ("1".equals(limit.getYXBZ())) {
					status.setLimitState("已限制");
				} else {
					status.setLimitState("正在办理限制");
				}
			}
		}
		String tmp = fj;
		if(StringHelper.isEmpty(tmp)){
			if(status.getMortgageState().contains("已")||status.getMortgageState().contains("正在"))
				tmp = status.getMortgageState() + "," ;
			if(status.getLimitState().contains("已")||status.getLimitState().contains("正在"))
				tmp += status.getLimitState();
			fj = tmp;
		}else{
			if(status.getMortgageState().contains("已")||status.getMortgageState().contains("正在"))
				tmp = status.getMortgageState() + "," ;
			if(status.getLimitState().contains("已")||status.getLimitState().contains("正在"))
				tmp += status.getLimitState();
			fj += tmp;
		}
		return fj;
	}
	
	protected void CopySQRFromGZQLR(String djdyid, String qllx, String xmbh, String qlid, String sqrlb) {
		CommonDao dao = getCommonDao();
		StringBuilder builderQL = new StringBuilder();
		builderQL.append("DJDYID ='");
		builderQL.append(djdyid);
		builderQL.append("' AND QLLX='");
		builderQL.append(qllx);
		builderQL.append("'");
		List<BDCS_QL_GZ> qls = dao.getDataList(BDCS_QL_GZ.class, builderQL.toString());
		if (qls == null || qls.size() <= 0) {
			return;
		}
		BDCS_QL_GZ ql = qls.get(0);
		StringBuilder builderQLR = new StringBuilder();
		builderQLR.append("QLID ='");
		builderQLR.append(ql.getId());
		builderQLR.append("'");
		List<BDCS_QLR_GZ> qlrs = dao.getDataList(BDCS_QLR_GZ.class, builderQLR.toString());
		if (qls == null || qls.size() <= 0) {
			return;
		}
		for (int iqlr = 0; iqlr < qlrs.size(); iqlr++) {
			String SQRID = getPrimaryKey();
			BDCS_QLR_GZ qlr = qlrs.get(iqlr);
			List<BDCS_SQR> sqrs = dao.getDataList(BDCS_SQR.class, "XMBH='" + this.getXMBH() + "' AND SQRLB='" + sqrlb + "' AND SQRXM='" + qlr.getQLRMC() + "'");
			if (sqrs != null && sqrs.size() > 0) {
				//权利人关联申请人ID
				qlr.setSQRID(sqrs.get(0).getId());
				dao.update(qlr);
				continue;
			}
			BDCS_SQR sqr = new BDCS_SQR();
			sqr.setGYFS(qlr.getGYFS());
			sqr.setFZJG(qlr.getFZJG());
			sqr.setGJDQ(qlr.getGJ());
			sqr.setGZDW(qlr.getGZDW());
			sqr.setXB(qlr.getXB());
			sqr.setHJSZSS(qlr.getHJSZSS());
			sqr.setSSHY(qlr.getSSHY());
			sqr.setYXBZ(qlr.getYXBZ());
			sqr.setQLBL(qlr.getQLBL());
			sqr.setQLMJ(StringHelper.formatObject(qlr.getQLMJ()));
			sqr.setSQRXM(qlr.getQLRMC());
			sqr.setSQRLB(sqrlb);
			sqr.setSQRLX(qlr.getQLRLX());
			sqr.setDZYJ(qlr.getDZYJ());
			sqr.setLXDH(qlr.getDH());
			sqr.setZJH(qlr.getZJH());
			sqr.setZJLX(qlr.getZJZL());
			sqr.setTXDZ(qlr.getDZ());
			sqr.setYZBM(qlr.getYB());
			sqr.setXMBH(xmbh);
			sqr.setId(SQRID);
			sqr.setGLQLID(qlid);
			dao.save(sqr);
			//权利人关联申请人ID
			qlr.setSQRID(sqr.getId());
			dao.update(qlr);
		}
	}
}
