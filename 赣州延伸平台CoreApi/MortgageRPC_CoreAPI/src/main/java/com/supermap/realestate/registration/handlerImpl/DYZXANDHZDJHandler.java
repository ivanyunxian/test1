package com.supermap.realestate.registration.handlerImpl;

import java.io.File;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.springframework.util.StringUtils;

import com.supermap.realestate.registration.ViewClass.ProjectInfo;
import com.supermap.realestate.registration.ViewClass.UnitTree;
import com.supermap.realestate.registration.dataExchange.DJFDJFZ;
import com.supermap.realestate.registration.dataExchange.DJFDJGD;
import com.supermap.realestate.registration.dataExchange.DJFDJSF;
import com.supermap.realestate.registration.dataExchange.DJFDJSH;
import com.supermap.realestate.registration.dataExchange.DJFDJSJ;
import com.supermap.realestate.registration.dataExchange.DJFDJSQR;
import com.supermap.realestate.registration.dataExchange.DJFDJSZ;
import com.supermap.realestate.registration.dataExchange.DJTDJSLSQ;
import com.supermap.realestate.registration.dataExchange.FJF100;
import com.supermap.realestate.registration.dataExchange.Message;
import com.supermap.realestate.registration.dataExchange.ZTTGYQLR;
import com.supermap.realestate.registration.dataExchange.exchangeFactory;
import com.supermap.realestate.registration.dataExchange.dyq.QLFQLDYAQ;
import com.supermap.realestate.registration.dataExchange.utils.packageXml;
import com.supermap.realestate.registration.handler.DJHandler;
import com.supermap.realestate.registration.model.BDCS_DJDY_GZ;
import com.supermap.realestate.registration.model.BDCS_DJDY_XZ;
import com.supermap.realestate.registration.model.BDCS_FSQL_GZ;
import com.supermap.realestate.registration.model.BDCS_FSQL_LS;
import com.supermap.realestate.registration.model.BDCS_FSQL_XZ;
import com.supermap.realestate.registration.model.BDCS_PARTIALLIMIT;
import com.supermap.realestate.registration.model.BDCS_QDZR_GZ;
import com.supermap.realestate.registration.model.BDCS_QDZR_XZ;
import com.supermap.realestate.registration.model.BDCS_QLR_GZ;
import com.supermap.realestate.registration.model.BDCS_QLR_XZ;
import com.supermap.realestate.registration.model.BDCS_QL_GZ;
import com.supermap.realestate.registration.model.BDCS_QL_LS;
import com.supermap.realestate.registration.model.BDCS_QL_XZ;
import com.supermap.realestate.registration.model.BDCS_SQR;
import com.supermap.realestate.registration.model.BDCS_XMXX;
import com.supermap.realestate.registration.model.BDCS_ZS_GZ;
import com.supermap.realestate.registration.model.BDCS_ZS_XZ;
import com.supermap.realestate.registration.model.WFD_MAPPING;
import com.supermap.realestate.registration.model.YC_SC_H_XZ;
import com.supermap.realestate.registration.model.interfaces.House;
import com.supermap.realestate.registration.model.interfaces.OwnerLand;
import com.supermap.realestate.registration.model.interfaces.RealUnit;
import com.supermap.realestate.registration.model.interfaces.Rights;
import com.supermap.realestate.registration.model.interfaces.RightsHolder;
import com.supermap.realestate.registration.model.interfaces.SubRights;
import com.supermap.realestate.registration.model.interfaces.UseLand;
import com.supermap.realestate.registration.model.xmlExportmodel.MessageExport;
import com.supermap.realestate.registration.service.impl.ProjectServiceImpl;
import com.supermap.realestate.registration.tools.RightsHolderTools;
import com.supermap.realestate.registration.tools.RightsTools;
import com.supermap.realestate.registration.tools.UnitTools;
import com.supermap.realestate.registration.util.ConfigHelper;
import com.supermap.realestate.registration.util.ConstValue;
import com.supermap.realestate.registration.util.ConstValue.BDCDYLX;
import com.supermap.realestate.registration.util.ConstValue.DJDYLY;
import com.supermap.realestate.registration.util.ConstValue.DJZT;
import com.supermap.realestate.registration.util.ConstValue.SF;
import com.supermap.realestate.registration.util.ConstValue.SQRLB;
import com.supermap.realestate.registration.util.GetProperties;
import com.supermap.realestate.registration.util.Global;
import com.supermap.realestate.registration.util.ObjectHelper;
import com.supermap.realestate.registration.util.ProjectHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.realestate.registration.util.YwLogUtil;
import com.supermap.wisdombusiness.core.SuperSpringContext;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import com.supermap.wisdombusiness.web.ResultMessage;

/*
 1、国有建设用地抵押权注销登记
 2、集体建设用地抵押权注销登记（未配置）
 3、宅基地抵押权注销登记（未配置）
 4、国有建设用地使用权/房屋所有权抵押权注销登记
 5、集体建设用地使用权/房屋所有权抵押权注销登记（未配置）
 6、宅基地使用权/房屋所有权抵押权注销登记（未配置）
 */
/**
 * 
 * 抵押注销登记处理类
 * @ClassName: DYZXDJHandler
 * @author liushufeng
 * @date 2015年9月8日 下午10:33:16
 */
public class DYZXANDHZDJHandler extends DJHandlerBase implements DJHandler {

	public DYZXANDHZDJHandler(ProjectInfo info) {
		super(info);
		// TODO Auto-generated constructor stub
	}
	@Override
	public boolean addBDCDY(String qlids) {
		boolean bsuccess = false;
		if (StringHelper.isEmpty(qlids)) {
			return false;
		}
		String[] listqlid = qlids.split(",");
		if (listqlid == null || listqlid.length <= 0)
			return false;
		// 获取是否获取重新生成权证号配置
		String newqzh = "";
		String workflowcode = ProjectHelper.getWorkflowCodeByProjectID(this.getProject_id());
		List<WFD_MAPPING> listCode = getCommonDao().getDataList(WFD_MAPPING.class, "WORKFLOWCODE='" + workflowcode + "'");
		if (listCode != null && listCode.size() > 0) {
			newqzh = listCode.get(0).getNEWQZH();
		}
		CommonDao dao = getCommonDao();
		List<String> listDJDY = new ArrayList<String>();
		for (int iql = 0; iql < listqlid.length; iql++) {
			String qlid = listqlid[iql];
			Rights ql = RightsTools.loadRights(DJDYLY.XZ, qlid);
			if (ql != null) {
				String fj = "";
				String bdcdyid = "";
				// 拷贝登记单元
				if (!StringHelper.isEmpty(ql.getDJDYID()) && !listDJDY.contains(ql.getDJDYID())) {
					listDJDY.add(ql.getDJDYID());
					List<BDCS_DJDY_XZ> list = getCommonDao().getDataList(BDCS_DJDY_XZ.class, "DJDYID='" + ql.getDJDYID() + "'");
					if (list != null && list.size() > 0) {
						 bdcdyid = list.get(0).getBDCDYID();
						BDCS_DJDY_GZ bdcs_djdy_gz = new BDCS_DJDY_GZ();
						bdcs_djdy_gz.setXMBH(this.getXMBH());
						bdcs_djdy_gz.setDJDYID(list.get(0).getDJDYID());
						bdcs_djdy_gz.setBDCDYID(list.get(0).getBDCDYID());
						bdcs_djdy_gz.setBDCDYLX(this.getBdcdylx().Value);
						bdcs_djdy_gz.setBDCDYH(list.get(0).getBDCDYH());
						bdcs_djdy_gz.setId(getPrimaryKey());
						bdcs_djdy_gz.setLY(DJDYLY.XZ.Value);
						getCommonDao().save(bdcs_djdy_gz);
					}
				}
				if (SF.NO.Value.equals(newqzh)) {
					// 拷贝权利信息（权证号不为空）
					BDCS_QL_GZ bdcs_ql_gz = super.CopyQLXXFromXZ(qlid);
					String gzqlid = bdcs_ql_gz.getId();
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
					BDCS_QL_GZ bdcs_ql_gz = super.CopyQLXXFromXZExceptBDCQZH(qlid);
					String gzqlid = bdcs_ql_gz.getId();
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
				}
				// 没进行
				dao.flush();
				super.CopySQRFromXZQLR(ql.getDJDYID(), ql.getQLLX(), this.getXMBH(), ql.getId(),SQRLB.YF.Value);
				ql.setDJZT(DJZT.DJZ.Value);
				getCommonDao().update(ql);
			}
		}
		dao.flush();
		bsuccess = true;
		return bsuccess;
	}
	private String getStatus(String fj, String djdyid, String bdcdyid,
			String value) {
		// TODO Auto-generated method stub
		return null;
	}
	//	/**
//	 * 添加不动产单元
//	 */
//	@Override
//	public boolean addBDCDY(String qlids) {
//		String qlid[] = qlids.split(",");
//		if (qlid != null && qlid.length > 0) {
//			for (String id : qlid) {
//				StringBuilder builer = new StringBuilder();
//				builer.append(" QLID='").append(id).append("'");
//				String strQuery = builer.toString();
//				BDCS_QL_XZ bdcs_ql_xz = getCommonDao().get(BDCS_QL_XZ.class, id);
//				if (bdcs_ql_xz != null) {
//					String gzqlid = getPrimaryKey();
//					String gzfsqlid = getPrimaryKey();
//					// 拷贝权利
//					BDCS_QL_GZ bdcs_ql_gz = ObjectHelper.copyQL_XZToGZ(bdcs_ql_xz);
//					bdcs_ql_gz.setId(gzqlid);
//					bdcs_ql_gz.setFSQLID(gzfsqlid);
//					bdcs_ql_gz.setXMBH(getXMBH());
//					bdcs_ql_gz.setLYQLID(id);
//					bdcs_ql_gz.setISPARTIAL(bdcs_ql_xz.getISPARTIAL());
//					String xzqhdm=ConfigHelper.getNameByValue("XZQHDM");
//					if(!StringHelper.isEmpty(xzqhdm) && xzqhdm.startsWith("1301")){
//						bdcs_ql_gz.setDJYY("主债权消灭");
//					}
//					getCommonDao().save(bdcs_ql_gz);
//					BDCS_FSQL_XZ bdcs_fsql_xz = getCommonDao().get(BDCS_FSQL_XZ.class, bdcs_ql_xz.getFSQLID());
//
//					StringBuilder builderDJDY = new StringBuilder();
//					builderDJDY.append(" DJDYID='");
//					builderDJDY.append(bdcs_ql_xz.getDJDYID());
//					builderDJDY.append("'");
//
//					// 获取所有权人
//					String ywrmc = "";
//					String syqsqlcondition = builderDJDY.toString() + " and QLLX IN ('1','2','3','4','5','6','7','8')";
//					List<Rights> rightss = RightsTools.loadRightsByCondition(DJDYLY.XZ, syqsqlcondition);
//					if (rightss != null && rightss.size() > 0) {
//						List<RightsHolder> rightsholders = RightsHolderTools.loadRightsHolders(DJDYLY.XZ, rightss.get(0).getId());
//						if (rightsholders != null && rightsholders.size() > 0) {
//							for (RightsHolder holder : rightsholders) {
//								BDCS_QLR_XZ qlr = (BDCS_QLR_XZ) holder;
//								if (qlr != null) {
//									BDCS_SQR sqr = super.copyXZQLRtoSQR(qlr, SQRLB.YF);
//									if(sqr!=null){
//										if(StringHelper.isEmpty(sqr.getSQRLX())){
//											sqr.setSQRLX("1");
//										}
//										getCommonDao().save(sqr);
//									}
//								}
//								if (!StringHelper.isEmpty(ywrmc))
//									ywrmc += "、" + holder.getQLRMC();
//								else
//									ywrmc += holder.getQLRMC();
//							}
//						}
//					}
//
//					if (bdcs_fsql_xz != null) {
//						// 拷贝附属权利
//						BDCS_FSQL_GZ bdcs_fsql_gz = ObjectHelper.copyFSQL_XZToGZ(bdcs_fsql_xz);
//						bdcs_fsql_gz.setQLID(gzqlid);
//						bdcs_fsql_gz.setId(gzfsqlid);
//						bdcs_fsql_gz.setXMBH(getXMBH());
//						bdcs_fsql_gz.setZXDYYWH(super.getProject_id());
//						if(!StringHelper.isEmpty(ConfigHelper.getNameByValue("XZQHDM"))&&ConfigHelper.getNameByValue("XZQHDM").startsWith("13")){
//							bdcs_fsql_gz.setZXDYYY("主债权消灭");
//						}
//						if (bdcs_fsql_gz.getDYR()==null || StringHelper.isEmpty(bdcs_fsql_gz.getDYR()))
//							bdcs_fsql_gz.setDYR(ywrmc);
//						getCommonDao().save(bdcs_fsql_gz);
//					}
//
//					// 获取登记单元集合
//					List<BDCS_DJDY_XZ> djdys = getCommonDao().getDataList(BDCS_DJDY_XZ.class, builderDJDY.toString());
//					if (djdys != null && djdys.size() > 0) {
//						BDCS_DJDY_XZ bdcs_djdy_xz = djdys.get(0);
//						// 拷贝登记单元
//						BDCS_DJDY_GZ bdcs_djdy_gz = ObjectHelper.copyDJDY_XZToGZ(bdcs_djdy_xz);
//						bdcs_djdy_gz.setId(getPrimaryKey());
//						bdcs_djdy_gz.setLY(DJDYLY.XZ.Value);
//						bdcs_djdy_gz.setXMBH(getXMBH());
//						getCommonDao().save(bdcs_djdy_gz);
//					}
//					// 获取权利人集合
//					List<BDCS_QLR_XZ> qlrs = getCommonDao().getDataList(BDCS_QLR_XZ.class, strQuery);
//					if (qlrs != null && qlrs.size() > 0) {
//						for (int iqlr = 0; iqlr < qlrs.size(); iqlr++) {
//							BDCS_QLR_XZ bdcs_qlr_xz = qlrs.get(iqlr);
//							if (bdcs_qlr_xz != null) {
//								// 拷贝权利人
//								String gzqlrid = getPrimaryKey();
//								BDCS_QLR_GZ bdcs_qlr_gz = ObjectHelper.copyQLR_XZToGZ(bdcs_qlr_xz);
//								bdcs_qlr_gz.setId(gzqlrid);
//								bdcs_qlr_gz.setQLID(gzqlid);
//								bdcs_qlr_gz.setXMBH(getXMBH());
//								getCommonDao().save(bdcs_qlr_gz);
//
//								BDCS_SQR sqr = super.copyXZQLRtoSQR(bdcs_qlr_xz, SQRLB.JF);
//								if(sqr!=null){
//									if(StringHelper.isEmpty(sqr.getSQRLX())){
//										sqr.setSQRLX("2");
//									}
//									if(StringHelper.isEmpty(sqr.getZJLX())){
//										sqr.setZJLX("7");
//									}
//									getCommonDao().save(sqr);
//								}
//
//								// 获取证书集合
//								StringBuilder builder = new StringBuilder();
//								builder.append(" id IN (");
//								builder.append("select ZSID FROM BDCS_QDZR_XZ WHERE QLID ='");
//								builder.append(id).append("'").append(" AND QLRID='");
//								builder.append(bdcs_qlr_xz.getId()).append("')");
//								String strQueryZS = builder.toString();
//								List<BDCS_ZS_XZ> zss = getCommonDao().getDataList(BDCS_ZS_XZ.class, strQueryZS);
//								if (zss != null && zss.size() > 0) {
//									for (int izs = 0; izs < zss.size(); izs++) {
//										BDCS_ZS_XZ bdcs_zs_xz = zss.get(izs);
//										if (bdcs_zs_xz != null) {
//											String gzzsid = getPrimaryKey();
//											BDCS_ZS_GZ bdcs_zs_gz = ObjectHelper.copyZS_XZToGZ(bdcs_zs_xz);
//											bdcs_zs_gz.setId(gzzsid);
//											bdcs_zs_gz.setXMBH(getXMBH());
//											getCommonDao().save(bdcs_zs_gz);
//											// 获取权地证人集合
//											StringBuilder builderQDZR = new StringBuilder();
//											builderQDZR.append(strQuery);
//											builderQDZR.append(" AND ZSID='");
//											builderQDZR.append(bdcs_zs_xz.getId());
//											builderQDZR.append("' AND QLID='");
//											builderQDZR.append(id);
//											builderQDZR.append("' AND QLRID='");
//											builderQDZR.append(bdcs_qlr_xz.getId());
//											builderQDZR.append("')");
//											List<BDCS_QDZR_XZ> qdzrs = getCommonDao().getDataList(BDCS_QDZR_XZ.class, builderQDZR.toString());
//											if (qdzrs != null && qdzrs.size() > 0) {
//												for (int iqdzr = 0; iqdzr < qdzrs.size(); iqdzr++) {
//													BDCS_QDZR_XZ bdcs_qdzr_xz = qdzrs.get(iqdzr);
//													if (bdcs_qdzr_xz != null) {
//														// 拷贝权地证人
//														BDCS_QDZR_GZ bdcs_qdzr_gz = ObjectHelper.copyQDZR_XZToGZ(bdcs_qdzr_xz);
//														bdcs_qdzr_gz.setId(getPrimaryKey());
//														bdcs_qdzr_gz.setZSID(gzzsid);
//														bdcs_qdzr_gz.setQLID(gzqlid);
//														bdcs_qdzr_gz.setFSQLID(gzfsqlid);
//														bdcs_qdzr_gz.setQLRID(gzqlrid);
//														bdcs_qdzr_gz.setXMBH(getXMBH());
//														getCommonDao().save(bdcs_qdzr_gz);
//													}
//												}
//											}
//										}
//									}
//								}
//							}
//						}
//					}
//					bdcs_ql_xz.setDJZT("02");
//					getCommonDao().update(bdcs_ql_xz);
//				}
//			}
//			getCommonDao().flush();
//			return true;
//		} else {
//			ResultMessage msg = new ResultMessage();
//			msg.setMsg("选择失败!");
//			msg.setSuccess("false");
//			return false;
//		}
//	}
	/**
	 * 写入登记薄
	 */
	@Override
	public boolean writeDJB() {
		super.DyLimitLifted();
		// Subject user = SecurityUtils.getSubject();
		String dbr = Global.getCurrentUserName();
		String strSqlXMBH = ProjectHelper.GetXMBHCondition(getXMBH());
		List<BDCS_QL_GZ> qls = getCommonDao().getDataList(BDCS_QL_GZ.class, strSqlXMBH);
		if (qls != null && qls.size() > 0) {
			for (int iql = 0; iql < qls.size(); iql++) {
				String fsqlid = qls.get(iql).getFSQLID();
				String lyqlid = qls.get(iql).getLYQLID();
				String sqlQL = MessageFormat.format(" QLID=''{0}''", lyqlid);
				
				BDCS_XMXX xmxx = getCommonDao().get(BDCS_XMXX.class, getXMBH());
				String workflowcode = ProjectHelper.getWorkflowCodeByProjectID(xmxx.getPROJECT_ID());
				String sql = " WORKFLOWCODE='" + workflowcode + "'";
				CommonDao baseCommonDao = SuperSpringContext.getContext().getBean(CommonDao.class);
				List<WFD_MAPPING> mappings = baseCommonDao.getDataList(WFD_MAPPING.class, sql);
				if (mappings != null && mappings.size() > 0) {
					WFD_MAPPING maping = mappings.get(0);
					if (("1").equals(maping.getISUNLOCKYCHDY())){
						updateychdy(sqlQL,fsqlid);
					}
				}
				
				// 再删除权利人
				getCommonDao().deleteEntitysByHql(BDCS_QLR_XZ.class, sqlQL);
				// 再删除权利
				getCommonDao().deleteEntitysByHql(BDCS_QL_XZ.class, sqlQL);
				// 再删除附属权利
				getCommonDao().deleteEntitysByHql(BDCS_FSQL_XZ.class, sqlQL);
				// 再删除证书
				String sqlZS = MessageFormat.format(" id IN (SELECT B.ZSID FROM BDCS_QDZR_XZ B WHERE B.QLID=''{0}'')", lyqlid);
				getCommonDao().deleteEntitysByHql(BDCS_ZS_XZ.class, sqlZS);
				// 删除权利-权利人-证书-单元关系
				getCommonDao().deleteEntitysByHql(BDCS_QDZR_XZ.class, sqlQL);
				// 更新历史权利
				BDCS_QL_LS bdcs_ql_ls = getCommonDao().get(BDCS_QL_LS.class, lyqlid);
				if (bdcs_ql_ls != null) {
					BDCS_FSQL_GZ bdcs_fsql_gz = getCommonDao().get(BDCS_FSQL_GZ.class, fsqlid);
					if (bdcs_fsql_gz != null) {
						bdcs_fsql_gz.setZXDBR(dbr);
						Date zxsj = new Date();
						bdcs_fsql_gz.setZXSJ(zxsj);
						getCommonDao().update(bdcs_fsql_gz);
						BDCS_FSQL_LS bdcs_fsql_ls = getCommonDao().get(BDCS_FSQL_LS.class, bdcs_ql_ls.getFSQLID());
						if (bdcs_fsql_ls != null) {
							bdcs_fsql_ls.setZXDYYWH(bdcs_fsql_gz.getZXDYYWH());
							bdcs_fsql_ls.setZXDYYY(bdcs_fsql_gz.getZXDYYY());
							bdcs_fsql_ls.setZXFJ(bdcs_fsql_gz.getZXFJ());
							bdcs_fsql_ls.setZXSJ(zxsj);
							bdcs_fsql_ls.setZXDBR(dbr);
							getCommonDao().update(bdcs_fsql_ls);
						}
					}
				}
				if("1".equals(qls.get(iql).getISPARTIAL())){
					List<BDCS_PARTIALLIMIT> list=getCommonDao().getDataList(BDCS_PARTIALLIMIT.class, "LIMITQLID='"+qls.get(iql).getLYQLID()+"'");
					if(list!=null&&list.size()>0){
						for(BDCS_PARTIALLIMIT partialseizures:list){
							partialseizures.setYXBZ("2");
							getCommonDao().update(partialseizures);
						}
					}
				}
			}
		}
		this.SetSFDB();
		getCommonDao().flush();
		List<BDCS_DJDY_GZ> djdys = getCommonDao().getDataList(BDCS_DJDY_GZ.class, strSqlXMBH);
		if (djdys != null && djdys.size() > 0) {
			for (int idjdy = 0; idjdy < djdys.size(); idjdy++) {
				BDCS_DJDY_GZ bdcs_djdy_gz = djdys.get(idjdy);
				if (bdcs_djdy_gz != null) {
					BDCDYLX dylx = ProjectHelper.GetBDCDYLX(getXMBH());
					String strDYZT = GetDYZT(bdcs_djdy_gz.getDJDYID());
					// 更新单元抵押状态
					SetDYDYZT(bdcs_djdy_gz.getBDCDYID(), dylx, strDYZT);
				}
			}
		}
		getCommonDao().flush();
		return true;
	}

	//解除关联预测户的抵押权
	private void updateychdy(String sqlQL,String fsqlid) {
		String dbr = Global.getCurrentUserName();
		List<BDCS_DJDY_XZ> DJDY_XZs = new ArrayList<BDCS_DJDY_XZ>();
		List<BDCS_QL_XZ> dyql = getCommonDao().getDataList(BDCS_QL_XZ.class, sqlQL);
		if(dyql!=null&&dyql.size()>0){
			String scdjdyid = dyql.get(0).getDJDYID();
			List<BDCS_DJDY_XZ> DJDY_XZ = getCommonDao().getDataList(BDCS_DJDY_XZ.class, " DJDYID='" + scdjdyid + "'");
			if(DJDY_XZ!=null&&DJDY_XZ.size()>0){
				String scdbcdyid = DJDY_XZ.get(0).getBDCDYID();
				List<YC_SC_H_XZ> yc_sc = getCommonDao().getDataList(YC_SC_H_XZ.class, " SCBDCDYID='" + scdbcdyid + "'");
				if(yc_sc!=null&&yc_sc.size()>0){
					String ycbdcdyid = yc_sc.get(0).getYCBDCDYID();
					DJDY_XZs = getCommonDao().getDataList(BDCS_DJDY_XZ.class, " BDCDYID='" + ycbdcdyid + "'");
				}
			}
		}
		if(DJDY_XZs!=null&&DJDY_XZs.size()>0){
			String dy = DJDY_XZs.get(0).getDJDYID();
			List<Rights> ycql = RightsTools.loadRightsByCondition(DJDYLY.XZ, " DJDYID='" + dy + "' AND QLLX='23'");
			for (Rights rights : ycql) {
				sqlQL = MessageFormat.format(" QLID=''{0}''", rights.getId());
				// 再删除权利人
				getCommonDao().deleteEntitysByHql(BDCS_QLR_XZ.class, sqlQL);
				// 再删除权利
				getCommonDao().deleteEntitysByHql(BDCS_QL_XZ.class, sqlQL);
				// 再删除附属权利
				getCommonDao().deleteEntitysByHql(BDCS_FSQL_XZ.class, sqlQL);
				// 再删除证书
				String sqlZS = MessageFormat.format(" id IN (SELECT B.ZSID FROM BDCS_QDZR_XZ B WHERE B.QLID=''{0}'')", rights.getId());
				getCommonDao().deleteEntitysByHql(BDCS_ZS_XZ.class, sqlZS);
				// 删除权利-权利人-证书-单元关系
				getCommonDao().deleteEntitysByHql(BDCS_QDZR_XZ.class, sqlQL);
				BDCS_QL_LS bdcs_ql_ls = getCommonDao().get(BDCS_QL_LS.class, rights.getId());
				if (bdcs_ql_ls != null) {
					BDCS_FSQL_GZ bdcs_fsql_gz = getCommonDao().get(BDCS_FSQL_GZ.class, fsqlid);
					if (bdcs_fsql_gz != null) {
						BDCS_FSQL_LS bdcs_fsql_ls = getCommonDao().get(BDCS_FSQL_LS.class, bdcs_ql_ls.getFSQLID());
						if (bdcs_fsql_ls != null) {
							bdcs_fsql_ls.setZXDYYWH(bdcs_fsql_gz.getZXDYYWH());
							bdcs_fsql_ls.setZXDYYY(bdcs_fsql_gz.getZXDYYY());
							bdcs_fsql_ls.setZXFJ(bdcs_fsql_gz.getZXFJ());
							bdcs_fsql_ls.setZXSJ(new Date());
							bdcs_fsql_ls.setZXDBR(dbr);
							getCommonDao().update(bdcs_fsql_ls);
						}
					}
				}
			}
		}
	}

	/**
	 * 移除登记产单元
	 */
	@Override
	public boolean removeBDCDY(String qlid) {
		String xmbh = getXMBH();
		// 先删除登记单元
		BDCS_QL_GZ bdcs_ql_gz = getCommonDao().get(BDCS_QL_GZ.class, qlid);
		if (bdcs_ql_gz != null) {
			String lyqlid = bdcs_ql_gz.getLYQLID();
			BDCS_QL_XZ bdcs_ql_xz = getCommonDao().get(BDCS_QL_XZ.class, lyqlid);
			if (bdcs_ql_xz != null) {
				bdcs_ql_xz.setDJZT("01");
				getCommonDao().update(bdcs_ql_xz);
			}
			StringBuilder builderDJDY = new StringBuilder();
			builderDJDY.append(" DJDYID='");
			builderDJDY.append(bdcs_ql_gz.getDJDYID());
			builderDJDY.append("' AND XMBH='");
			builderDJDY.append(bdcs_ql_gz.getXMBH());
			builderDJDY.append("'");
			// 获取登记单元集合
			List<BDCS_DJDY_GZ> djdys = getCommonDao().getDataList(BDCS_DJDY_GZ.class, builderDJDY.toString());
			if (djdys != null && djdys.size() > 0) {
				BDCS_DJDY_GZ bdcs_djdy_gz = djdys.get(0);
				getCommonDao().deleteEntity(bdcs_djdy_gz);
			}
			// 删除申请人
			String sql=" GLQLID='" + lyqlid + "' AND XMBH='" + xmbh + "'";
			getCommonDao().deleteEntitysByHql(BDCS_SQR.class, sql);
		}

		// 再删除权利人
		String sqlQLR = MessageFormat.format("  XMBH=''{0}'' AND QLID =''{1}''", xmbh, qlid);
		getCommonDao().deleteEntitysByHql(BDCS_QLR_GZ.class, sqlQLR);
		// 再删除权利
		String sqlQL = MessageFormat.format(" XMBH=''{0}'' AND QLID=''{1}''", xmbh, qlid);
		getCommonDao().deleteEntitysByHql(BDCS_QL_GZ.class, sqlQL);
		// 再删除附属权利
		getCommonDao().deleteEntitysByHql(BDCS_FSQL_GZ.class, sqlQL);
		// 再删除证书
		String sqlZS = MessageFormat.format(" XMBH=''{0}'' AND id IN (SELECT B.ZSID FROM BDCS_QDZR_GZ B WHERE B.XMBH=''{0}'' AND B.QLID=''{1}'')", xmbh, qlid);
		getCommonDao().deleteEntitysByHql(BDCS_ZS_GZ.class, sqlZS);
		// 删除权利-权利人-证书-单元关系
		getCommonDao().deleteEntitysByHql(BDCS_QDZR_GZ.class, sqlQL);

		getCommonDao().flush();
		return true;
	}

	/**
	 * 获取不动产单元列表
	 */
	@Override
	public List<UnitTree> getUnitTree() {
		List<UnitTree> list = super.getRightList();
		List<UnitTree> newlist =new ArrayList();
		for (UnitTree tree : list) {
			String qlid = tree.getQlid();
			Rights right = RightsTools.loadRights(DJDYLY.GZ, qlid);
			if (right != null) {
				tree.setOldqlid(right.getLYQLID());
				String bdcqzh=right.getBDCQZH();
				String newzl=tree.getText()+"-"+bdcqzh;
				tree.setText(newzl);
			}
			newlist.add(tree);
		}
		return newlist;
	}

	/**
	 * 根据申请人ID数组添加申请人
	 */
	@Override
	public void addQLRbySQRArray(String qlid, Object[] sqrids) {
		super.addQLRbySQRs(qlid, sqrids);
	}

	/**
	 * 移除权利人
	 */
	@Override
	public void removeQLR(String qlid, String qlrid) {
		super.removeqlr(qlrid, qlid);
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
	public Map<String, String> exportXML(String path, String actinstID) {
		Marshaller mashaller;
		Map<String,String> names = new HashMap<String,String>();
		try {
			String result = "";
			CommonDao dao = super.getCommonDao();
			String xmbhHql = ProjectHelper.GetXMBHCondition(super.getXMBH());
			String condition =xmbhHql+" and QLLX='23'";
			List<Rights> rights=RightsTools.loadRightsByCondition(DJDYLY.GZ, condition);
			List<BDCS_SQR> sqrs = new ArrayList<BDCS_SQR>();
			ProjectServiceImpl serviceImpl = SuperSpringContext.getContext().getBean(ProjectServiceImpl.class);
			sqrs = serviceImpl.getSQRList(super.getXMBH());
			if(!StringHelper.isEmpty(rights) && rights.size()>0){
				mashaller = JAXBContext.newInstance(Message.class).createMarshaller();
				BDCS_XMXX bdcs_xmxx=Global.getXMXXbyXMBH(super.getXMBH());
				Calendar calendar = Calendar.getInstance(); // 创建一个日历对象
				calendar.setTime(new Date()); // 用当前时间初始化日历时间
				String ywh =packageXml.GetYWLSHByYWH(this.getProject_id());
				Message msg=exchangeFactory.createMessageByDYQ();//注销登记
				RealUnit unit=null;
				BDCS_DJDY_GZ bdcs_djdy_gz=null;
				
				for(Rights right :rights){
					SubRights subrights=RightsTools.loadSubRights(DJDYLY.GZ, right.getFSQLID());
					String djdysql = xmbhHql+ "AND DJDYID='"+ right.getDJDYID()+"'";
					List<BDCS_DJDY_GZ> djdys=dao.getDataList(BDCS_DJDY_GZ.class, djdysql);
					if(!StringHelper.isEmpty(djdys) && djdys.size()>0){
						 bdcs_djdy_gz= djdys.get(0);
						BDCDYLX bdcdylx=BDCDYLX.initFrom(bdcs_djdy_gz.getBDCDYLX());
						DJDYLY djdylx=DJDYLY.initFrom(bdcs_djdy_gz.getLY());
						String bdcdyid=bdcs_djdy_gz.getBDCDYID();
						if(!StringHelper.isEmpty(bdcdyid) &&!StringHelper.isEmpty(bdcdylx) &&!StringHelper.isEmpty(djdylx)){
							 unit=UnitTools.loadUnit(bdcdylx, djdylx, bdcdyid);	
						}
					}					
					//ZTT_GY_QLR 权利人(完成)
					List<ZTTGYQLR> qlrs =msg.getData().getGYQLR();
					List<RightsHolder> holders=RightsHolderTools.loadRightsHolders(DJDYLY.LS , right.getLYQLID());
					qlrs=packageXml.getZTTGYQLRsEx(qlrs, holders, right.getBDCQZH(),right.getBDCDYH(), unit);
					
					//QLF_QL_DYAQ 抵押权
					QLFQLDYAQ dyaq=msg.getData().getQLFQLDYAQ();
					packageXml.getQLFQLDYAQEx(dyaq, right, subrights, ywh, unit);
					msg.getData().setQLFQLDYAQ(dyaq);
					if(!StringHelper.isEmpty(right.getYWH())){
					 String scywh=packageXml.GetYWLSHByYWH(right.getYWH());//获取上次业务号
					 msg.getData().getQLFQLDYAQ().setScywh(scywh);
					}
					
					//DJT_DJ_SLSQ 登记受理申请信息
					DJTDJSLSQ sq = msg.getData().getDJSLSQ();
					sq = packageXml.getDJTDJSLSQEx(sq, ywh, right, bdcs_xmxx,unit);
					msg.getData().setDJSLSQ(sq);
                   
					//DJF_DJ_SJ 登记收件信息
					List<DJFDJSJ> sj = msg.getData().getDJSJ();
					sj = packageXml.getDJFDJSJEx(new DJFDJSJ(), ywh, actinstID, unit);
					msg.getData().setDJSJ(sj);
                    
					//9.登记收费(可选)
					List<DJFDJSF> sfList = msg.getData().getDJSF();
					sfList = packageXml.getDJSF(unit, ywh,this.getXMBH());
					msg.getData().setDJSF(sfList);

					//10.审核(可选)
					List<DJFDJSH> sh = msg.getData().getDJSH();
					 sh = packageXml.getDJFDJSH(unit, ywh, this.getXMBH(), actinstID);
					msg.getData().setDJSH(sh);

					//11.缮证(可选)
					List<DJFDJSZ>  sz = packageXml.getDJFDJSZ(unit, ywh, this.getXMBH());
					msg.getData().setDJSZ(sz);

					//11.发证(可选)
					List<DJFDJFZ>   fz = packageXml.getDJFDJFZ(unit, ywh, this.getXMBH());
					msg.getData().setDJFZ(fz);
					
					//12.归档(可选)
					List<DJFDJGD> gd = packageXml.getDJFDJGD(unit, ywh, this.getXMBH());
					msg.getData().setDJGD(gd);	
					
					//DJF_DJ_SQR	申请人属性信息
					String zddm="";
					String ysdm="";
					if(unit instanceof House){
						House h=(House) unit;
						zddm=h.getZDDM();
						ysdm=h.getYSDM();
					}
					else if( unit instanceof OwnerLand){
						OwnerLand l=(OwnerLand) unit;
						zddm=l.getZDDM();
						ysdm=l.getYSDM();
					}
					else if(unit instanceof UseLand){
						UseLand l=(UseLand) unit;
						zddm=l.getZDDM();
						ysdm=l.getYSDM();
					}					
					List<DJFDJSQR> djsqrs = msg.getData().getDJSQR();
					djsqrs = packageXml.getDJSQRs(djsqrs, sqrs,ysdm, ywh, unit.getBDCDYH());
					msg.getData().setDJSQR(djsqrs);
					
					//FJ_F_100	非结构化文档
					FJF100 fj = msg.getData().getFJF100();
					fj = packageXml.getFJF(fj);
					msg.getData().setFJF100(fj);
					super.fillHead(msg, 1, ywh,unit.getBDCDYH(),unit.getQXDM(),null);
					msg.getHead().setParcelID(StringHelper.formatObject(zddm));
					msg.getHead().setEstateNum(StringHelper.formatObject(unit.getBDCDYH()));
//					msg.getHead().setPreEstateNum(StringHelper.formatObject(unit.getBDCDYH()));
					//msg.getHead().setRightType("23");//抵押权
					//msg.getHead().setRecType("4000101");
					
					mashaller.marshal(msg, new File(path + "Biz" + msg.getHead().getBizMsgID() + ".xml"));
					result = uploadFile(path + "Biz" + msg.getHead().getBizMsgID() + ".xml",ConstValue.RECCODE.DIYQ_ZXDJ.Value,actinstID,bdcs_djdy_gz.getDJDYID(),right.getId());
					names.put(bdcs_djdy_gz.getDJDYID(), msg.getHead().getBizMsgID() + ".xml");
					if(null == result){
						Map<String, String> xmlError = new HashMap<String, String>();
						xmlError.put("error", "连接SFTP失败,请检查服务器和前置机的连接是否正常");
						YwLogUtil.addSjsbResult("Biz" + msg.getHead().getBizMsgID() + ".xml", "", "连接SFTP失败,请检查服务器和前置机的连接是否正常", ConstValue.SF.NO.Value, ConstValue.RECCODE.ZX_ZXDJ.Value,ProjectHelper.getpRroinstIDByActinstID(actinstID));
						return xmlError;
					}
					if(!"1".equals(result) && result.indexOf("success") == -1){ //xml本地校验不通过时
						Map<String, String> xmlError = new HashMap<String, String>();
						xmlError.put("error", result);
						return xmlError;
					}
					if(!StringUtils.isEmpty(result) && result.indexOf("success") > -1 && !names.containsKey("reccode")){
						names.put("reccode", result);
					}
				}
			}
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return names;
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
			boolean isZipFile = false;
			if(djdys.size()>=10){
				isZipFile=true;
				String folderPath=GetProperties.getConstValueByKey("xmlPath") + "\\" + xmxx.getPROJECT_ID()+"_"+bljc;
				super.getShareMsgTools().deleteFolder(folderPath);
			}
			for (int idjdy = 0; idjdy < djdys.size(); idjdy++) {
				BDCS_DJDY_GZ djdy = djdys.get(idjdy);
				ConstValue.BDCDYLX dylx = ConstValue.BDCDYLX.initFrom(djdy.getBDCDYLX());
				ConstValue.DJDYLY dyly = ConstValue.DJDYLY.initFrom(djdy.getLY());

				RealUnit bdcdy = UnitTools.loadUnit(dylx, dyly, djdy.getBDCDYID());
				Rights bdcql = RightsTools.loadRightsByDJDYID(ConstValue.DJDYLY.GZ, getXMBH(), djdy.getDJDYID());
				String lyqlid = bdcql.getLYQLID();
				SubRights bdcfsql = RightsTools.loadSubRightsByRightsID(ConstValue.DJDYLY.GZ, bdcql.getId());
				List<RightsHolder> bdcqlrs = RightsHolderTools.loadRightsHolders(ConstValue.DJDYLY.LS, lyqlid);
				//bdcql.setId(lyqlid);
				MessageExport msg = super.getShareMsgTools().GetMsg(bdcdy, bdcql, bdcfsql, bdcqlrs, bljc, xmxx);
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

	
}
