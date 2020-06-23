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
import com.supermap.realestate.registration.ViewClass.UnitStatus;
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
import com.supermap.realestate.registration.dataExchange.fwsyq.KTTFWC;
import com.supermap.realestate.registration.dataExchange.fwsyq.KTTFWH;
import com.supermap.realestate.registration.dataExchange.fwsyq.KTTFWLJZ;
import com.supermap.realestate.registration.dataExchange.fwsyq.KTTFWZRZ;
import com.supermap.realestate.registration.dataExchange.fwsyq.QLTFWFDCQYZ;
import com.supermap.realestate.registration.dataExchange.shyq.KTFZDBHQK;
import com.supermap.realestate.registration.dataExchange.shyq.KTTGYJZD;
import com.supermap.realestate.registration.dataExchange.shyq.KTTGYJZX;
import com.supermap.realestate.registration.dataExchange.shyq.KTTZDJBXX;
import com.supermap.realestate.registration.dataExchange.shyq.QLFQLJSYDSYQ;
import com.supermap.realestate.registration.dataExchange.shyq.ZDK103;
import com.supermap.realestate.registration.dataExchange.utils.packageXml;
import com.supermap.realestate.registration.dataExchange.zxdj.QLFZXDJ;
import com.supermap.realestate.registration.handler.DJHandler;
import com.supermap.realestate.registration.model.BDCS_C_GZ;
import com.supermap.realestate.registration.model.BDCS_DJDY_GZ;
import com.supermap.realestate.registration.model.BDCS_DJDY_XZ;
import com.supermap.realestate.registration.model.BDCS_DYBG;
import com.supermap.realestate.registration.model.BDCS_DYXZ;
import com.supermap.realestate.registration.model.BDCS_FSQL_GZ;
import com.supermap.realestate.registration.model.BDCS_FSQL_LS;
import com.supermap.realestate.registration.model.BDCS_FSQL_XZ;
import com.supermap.realestate.registration.model.BDCS_H_GZ;
import com.supermap.realestate.registration.model.BDCS_H_LS;
import com.supermap.realestate.registration.model.BDCS_H_XZ;
import com.supermap.realestate.registration.model.BDCS_H_XZY;
import com.supermap.realestate.registration.model.BDCS_LJZ_XZ;
import com.supermap.realestate.registration.model.BDCS_PARTIALLIMIT;
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
import com.supermap.realestate.registration.model.BDCS_YHZK_GZ;
import com.supermap.realestate.registration.model.BDCS_YHZK_XZ;
import com.supermap.realestate.registration.model.BDCS_ZH_GZ;
import com.supermap.realestate.registration.model.BDCS_ZH_XZ;
import com.supermap.realestate.registration.model.BDCS_ZRZ_GZ;
import com.supermap.realestate.registration.model.BDCS_ZRZ_XZ;
import com.supermap.realestate.registration.model.BDCS_ZS_GZ;
import com.supermap.realestate.registration.model.BDCS_ZS_XZ;
import com.supermap.realestate.registration.model.WFD_MAPPING;
import com.supermap.realestate.registration.model.YC_SC_H_XZ;
import com.supermap.realestate.registration.model.interfaces.House;
import com.supermap.realestate.registration.model.interfaces.RealUnit;
import com.supermap.realestate.registration.model.interfaces.Rights;
import com.supermap.realestate.registration.model.interfaces.RightsHolder;
import com.supermap.realestate.registration.model.interfaces.Sea;
import com.supermap.realestate.registration.model.interfaces.SubRights;
import com.supermap.realestate.registration.model.interfaces.YHZK;
import com.supermap.realestate.registration.model.xmlExportmodel.MessageExport;
import com.supermap.realestate.registration.service.impl.ProjectServiceImpl;
import com.supermap.realestate.registration.tools.RightsHolderTools;
import com.supermap.realestate.registration.tools.RightsTools;
import com.supermap.realestate.registration.tools.UnitTools;
import com.supermap.realestate.registration.util.ConstValue;
import com.supermap.realestate.registration.util.ConstValue.BDCDYLX;
import com.supermap.realestate.registration.util.ConstValue.DJDYLY;
import com.supermap.realestate.registration.util.ConstValue.DJLX;
import com.supermap.realestate.registration.util.ConstValue.DJZT;
import com.supermap.realestate.registration.util.ConstValue.QLLX;
import com.supermap.realestate.registration.util.ConstValue.SF;
import com.supermap.realestate.registration.util.ConstValue.SQRLB;
import com.supermap.realestate.registration.util.ConfigHelper;
import com.supermap.realestate.registration.util.Global;
import com.supermap.realestate.registration.util.ObjectHelper;
import com.supermap.realestate.registration.util.ProjectHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.realestate.registration.util.YwLogUtil;
import com.supermap.wisdombusiness.core.SuperSpringContext;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;

/**
 * 
* ZY_DYZXANDZY_DJHandler-转移登记_抵押注销+转移    
* @ClassName: ZY_DYZXANDZY_DJHandler 
*/
public class BZ_DYZXANDBZ_DJHandler extends DJHandlerBase implements DJHandler {

	/**
	 * 构造函数
	 * 
	 * @param info
	 */
	public BZ_DYZXANDBZ_DJHandler(ProjectInfo info) {
		super(info);
	}

	/**
	 * 添加不动产单元--qlids--qllx=23
	 */
	@Override
	public boolean addBDCDY(String qlids) {
		boolean bsuccess = false;
		CommonDao dao = getCommonDao();
		String qlid[] = qlids.split(",");
		if (qlid != null && qlid.length > 0) {
			// 获取是否获取重新生成权证号配置
			String newqzh = "";
			String workflowcode = ProjectHelper.getWorkflowCodeByProjectID(this.getProject_id());
			List<WFD_MAPPING> listCode = getCommonDao().getDataList(WFD_MAPPING.class, "WORKFLOWCODE='" + workflowcode + "'");
			if (listCode != null && listCode.size() > 0) {
				newqzh = listCode.get(0).getNEWQZH();
			}
			
			for (String id : qlid) {		
				StringBuilder builer = new StringBuilder();
				builer.append(" QLID='").append(id).append("'");
				String strQuery = builer.toString();
				BDCS_QL_XZ bdcs_ql_xz = getCommonDao().get(BDCS_QL_XZ.class, id);
				if (bdcs_ql_xz != null) {
					// 获取登记单元、权利、附属权利---产权
					String bdcdyid = "";
					StringBuilder builderDJDY = new StringBuilder();
					builderDJDY.append(" DJDYID='").append(bdcs_ql_xz.getDJDYID()).append("' ");
					List<BDCS_DJDY_XZ> djdys = getCommonDao().getDataList(BDCS_DJDY_XZ.class, builderDJDY.toString());
					if (djdys != null && djdys.size() > 0) {
						bdcdyid = djdys.get(0).getBDCDYID();
						BDCS_DJDY_GZ bdcs_djdy_gz = new BDCS_DJDY_GZ();
						bdcs_djdy_gz.setXMBH(this.getXMBH());
						bdcs_djdy_gz.setDJDYID(djdys.get(0).getDJDYID());
						bdcs_djdy_gz.setBDCDYID(djdys.get(0).getBDCDYID());
						bdcs_djdy_gz.setBDCDYLX(this.getBdcdylx().Value);
						bdcs_djdy_gz.setBDCDYH(djdys.get(0).getBDCDYH());
						bdcs_djdy_gz.setId(getPrimaryKey());
						bdcs_djdy_gz.setLY(DJDYLY.XZ.Value);
						getCommonDao().save(bdcs_djdy_gz);

//						RealUnit unit = null;
//						try {
//							UnitTools.loadUnit(this.getBdcdylx(), DJDYLY.initFrom(djdy_gz.getLY()), djdy_gz.getBDCDYID());
//						} catch (Exception e) {
//							e.printStackTrace();
//						}
						String qllxarray = "('1','2','3','4','5','6','7','8','9','10','11','12','13','14','15','16','17','18')";
						String hql =  builderDJDY.append(" AND QLLX IN ").append(qllxarray).toString();
						List<Rights> qls = RightsTools.loadRightsByCondition(DJDYLY.XZ, hql);
						String gzqlid = "";
						String fj = "";
						if (qls != null && qls.size() > 0) {
							Rights ql_XZ = qls.get(0);
							if (SF.NO.Value.equals(newqzh)) {
								// 拷贝权利信息（权证号不为空）
								BDCS_QL_GZ bdcs_ql_gz = super.CopyQLXXFromXZ(ql_XZ.getId());
								gzqlid = bdcs_ql_gz.getId();
								
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
								BDCS_QL_GZ bdcs_ql_gz = super.CopyQLXXFromXZExceptBDCQZH(ql_XZ.getId());
								gzqlid = bdcs_ql_gz.getId();
								
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
							//CopySQRFromXZQLR(qlid, gzqlid);
							CopySQRFromGZQLR(ql_XZ.getId(), gzqlid);
							ql_XZ.setDJZT(DJZT.DJZ.Value);
							getCommonDao().update(ql_XZ);
						}						
					}
					
					//抵押权信息
					String gzqlid = getPrimaryKey();
					String gzfsqlid = getPrimaryKey();
					// 拷贝权利
					BDCS_QL_GZ bdcs_ql_gz = ObjectHelper.copyQL_XZToGZ(bdcs_ql_xz);
					bdcs_ql_gz.setId(gzqlid);
					bdcs_ql_gz.setFSQLID(gzfsqlid);
					bdcs_ql_gz.setDJLX(this.getDjlx().Value);
					bdcs_ql_gz.setXMBH(getXMBH());
					bdcs_ql_gz.setYWH(this.getProject_id());
					bdcs_ql_gz.setLYQLID(id);
//					bdcs_ql_gz.setISPARTIAL(bdcs_ql_xz.getISPARTIAL());
					String xzqhdm=ConfigHelper.getNameByValue("XZQHDM");
					if(!StringHelper.isEmpty(xzqhdm) && xzqhdm.startsWith("1301")){
						bdcs_ql_gz.setDJYY("主债权消灭");
					}
					getCommonDao().save(bdcs_ql_gz);
					BDCS_FSQL_XZ bdcs_fsql_xz = getCommonDao().get(BDCS_FSQL_XZ.class, bdcs_ql_xz.getFSQLID());
					if (bdcs_fsql_xz != null) {
						// 拷贝附属权利
						BDCS_FSQL_GZ bdcs_fsql_gz = ObjectHelper.copyFSQL_XZToGZ(bdcs_fsql_xz);
						bdcs_fsql_gz.setQLID(gzqlid);
						bdcs_fsql_gz.setId(gzfsqlid);
						bdcs_fsql_gz.setXMBH(getXMBH());
						bdcs_fsql_gz.setZXDYYWH(super.getProject_id());
						if(!StringHelper.isEmpty(xzqhdm) && xzqhdm.startsWith("13"))
							bdcs_fsql_gz.setZXDYYY("主债权消灭");
//						if (bdcs_fsql_gz.getDYR() == null || StringHelper.isEmpty(bdcs_fsql_gz.getDYR()))
//							bdcs_fsql_gz.setDYR(ywrmc);
						getCommonDao().save(bdcs_fsql_gz);
					}
					
					// 构建权地证人--qlid--抵押权人
					List<BDCS_QLR_XZ> qlrs = getCommonDao().getDataList(BDCS_QLR_XZ.class, strQuery);
					if (qlrs != null && qlrs.size() > 0) {
						for (int iqlr = 0; iqlr < qlrs.size(); iqlr++) {
							BDCS_QLR_XZ bdcs_qlr_xz = qlrs.get(iqlr);
							if (bdcs_qlr_xz != null) {
								// 拷贝权利人
								String gzqlrid = getPrimaryKey();
								BDCS_QLR_GZ bdcs_qlr_gz = ObjectHelper.copyQLR_XZToGZ(bdcs_qlr_xz);
								bdcs_qlr_gz.setId(gzqlrid);
								bdcs_qlr_gz.setQLID(gzqlid);
								bdcs_qlr_gz.setXMBH(getXMBH());
								getCommonDao().save(bdcs_qlr_gz);

								BDCS_SQR sqr = super.copyXZQLRtoSQR(bdcs_qlr_xz, SQRLB.JF);
								if(sqr != null){
									sqr.setGLQLID(bdcs_ql_gz.getId());
									if(StringHelper.isEmpty(sqr.getSQRLX())){
										sqr.setSQRLX("2");
									}
									if(StringHelper.isEmpty(sqr.getZJLX())){
										sqr.setZJLX("7");
									}
									getCommonDao().save(sqr);
								}
								// 获取证书集合
								StringBuilder builder = new StringBuilder();
								builder.append(" id IN (");
								builder.append("select ZSID FROM BDCS_QDZR_XZ WHERE QLID ='");
								builder.append(id).append("'").append(" AND QLRID='");
								builder.append(bdcs_qlr_xz.getId()).append("')");
								String strQueryZS = builder.toString();
								List<BDCS_ZS_XZ> zss = getCommonDao().getDataList(BDCS_ZS_XZ.class, strQueryZS);
								if (zss != null && zss.size() > 0) {
									for (int izs = 0; izs < zss.size(); izs++) {
										BDCS_ZS_XZ bdcs_zs_xz = zss.get(izs);
										if (bdcs_zs_xz != null) {
											String gzzsid = getPrimaryKey();
											BDCS_ZS_GZ bdcs_zs_gz = ObjectHelper.copyZS_XZToGZ(bdcs_zs_xz);
											bdcs_zs_gz.setId(gzzsid);
											bdcs_zs_gz.setXMBH(getXMBH());
											getCommonDao().save(bdcs_zs_gz);
											// 获取权地证人集合
											StringBuilder builderQDZR = new StringBuilder();
											builderQDZR.append(strQuery);
											builderQDZR.append(" AND ZSID='").append(bdcs_zs_xz.getId());
											builderQDZR.append("' AND QLID='").append(id);
											builderQDZR.append("' AND QLRID='").append(bdcs_qlr_xz.getId());
											builderQDZR.append("')");
											List<BDCS_QDZR_XZ> qdzrs = getCommonDao().getDataList(BDCS_QDZR_XZ.class, builderQDZR.toString());
											if (qdzrs != null && qdzrs.size() > 0) {
												for (int iqdzr = 0; iqdzr < qdzrs.size(); iqdzr++) {
													BDCS_QDZR_XZ bdcs_qdzr_xz = qdzrs.get(iqdzr);
													if (bdcs_qdzr_xz != null) {
														// 拷贝权地证人
														BDCS_QDZR_GZ bdcs_qdzr_gz = ObjectHelper.copyQDZR_XZToGZ(bdcs_qdzr_xz);
														bdcs_qdzr_gz.setId(getPrimaryKey());
														bdcs_qdzr_gz.setZSID(gzzsid);
														bdcs_qdzr_gz.setQLID(gzqlid);
														bdcs_qdzr_gz.setFSQLID(gzfsqlid);
														bdcs_qdzr_gz.setQLRID(gzqlrid);
														bdcs_qdzr_gz.setXMBH(getXMBH());
														getCommonDao().save(bdcs_qdzr_gz);
													}
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
			dao.flush();
			bsuccess = true;
			return bsuccess;
		}else {
			return false;
		}	
	}

	/**
	 * 写入登记簿
	 */
	@Override
	public boolean writeDJB() {
		boolean cfflag = true;
		String Project_id = ProjectHelper.GetPrjInfoByXMBH(getXMBH()).getProject_id();
		String workflowcode = ProjectHelper.getWorkflowCodeByProjectID(Project_id);
		List<WFD_MAPPING> listCode = getCommonDao().getDataList(WFD_MAPPING.class, "WORKFLOWCODE='" + workflowcode + "'");
		String ISUNLOCKYCHDY ="";
		if ((listCode != null) && (listCode.size() > 0)) {
			String cfconfig = listCode.get(0).getCFCONFIG();
			ISUNLOCKYCHDY = listCode.get(0).getISUNLOCKYCHDY();
			if("1".equals(cfconfig)){
				cfflag = false;
			}
		}
		if(cfflag){
			if (super.isCForCFING()) {
				return false;
			}
		}
		
		String strSqlXMBH = ProjectHelper.GetXMBHCondition(getXMBH());
		List<BDCS_DJDY_GZ> djdys = getCommonDao().getDataList(BDCS_DJDY_GZ.class, strSqlXMBH);		
		String dbr = Global.getCurrentUserName();
		BDCS_XMXX xmxx = getCommonDao().get(BDCS_XMXX.class, getXMBH());
		
		List<BDCS_QL_GZ> qls = getCommonDao().getDataList(BDCS_QL_GZ.class, strSqlXMBH);
		for (BDCS_QL_GZ ql: qls) {
			if ("23".equals(ql.getQLLX())) {//---抵押注销--qllx=23
				String fsqlid = ql.getFSQLID();
				String lyqlid = ql.getLYQLID();
				String sqlQL = MessageFormat.format(" QLID=''{0}''", lyqlid);
				//解除关联预测户的抵押权
				if (("1").equals(ISUNLOCKYCHDY)){
					updateychdy(sqlQL,fsqlid);
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
				if("1".equals(ql.getISPARTIAL())){
					List<BDCS_PARTIALLIMIT> list=getCommonDao().getDataList(BDCS_PARTIALLIMIT.class, "LIMITQLID='"+ql.getLYQLID()+"'");
					if(list!=null&&list.size()>0){
						for(BDCS_PARTIALLIMIT partialseizures:list){
							partialseizures.setYXBZ("2");
							getCommonDao().update(partialseizures);
						}
					}
				}
			}else if (getQllx().Value.equals(ql.getQLLX())) {
				Date djsj = new Date();
				ql.setDBR(dbr);
				ql.setDJSJ(djsj);
				getCommonDao().update(ql);
				// 根据权利ID删除权利，附属权利，证书，权地证人，权利人信息
				RightsTools.deleteRightsAll(DJDYLY.XZ, ql.getLYQLID());
				Rights ql_xz = RightsTools.copyRightsAll(DJDYLY.GZ, DJDYLY.XZ, ql.getId());
				Rights ql_ls = RightsTools.copyRightsAll(DJDYLY.GZ, DJDYLY.LS, ql.getId());
				getCommonDao().save(ql_xz);
				getCommonDao().save(ql_ls);
				// 更新历史附属权利信息
				SubRights subright = RightsTools.loadSubRightsByRightsID(DJDYLY.LS, ql.getLYQLID());
				if (subright != null) {
					subright.setZXSJ(djsj);
					subright.setZXDBR(dbr);
					subright.setZXDYYWH(getProject_id());
					getCommonDao().update(subright);
				}
			}
		}
		// 更新单元抵押状态
		for (BDCS_DJDY_GZ bdcs_djdy_gz:djdys) {				
			if (bdcs_djdy_gz != null) {
				BDCDYLX dylx = ProjectHelper.GetBDCDYLX(getXMBH());
				String strDYZT = GetDYZT(bdcs_djdy_gz.getDJDYID());
				SetDYDYZT(bdcs_djdy_gz.getBDCDYID(), dylx, strDYZT);
			}
		}
	
		this.SetSFDB();
		getCommonDao().flush();
		super.alterCachedXMXX();
		return true;
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
				StringBuilder BuilderQL = new StringBuilder();
				BuilderQL.append(xmbhFilter).append(" AND DJDYID='").append(djdy.getDJDYID()).append("'");
				List<BDCS_QL_GZ> qls = dao.getDataList(BDCS_QL_GZ.class, BuilderQL.toString());
				if (qls != null) {
					for (int iql = 0; iql < qls.size(); iql++) {
						BDCS_QL_GZ ql = qls.get(iql);
						if (ql.getQLLX().equals(QLLX.DIYQ.Value)) {
							tree.setDIYQQlid(ql.getId());
							tree.setOlddiyqqlid(ql.getLYQLID());

						} else {
							tree.setQlid(ql.getId());
						}
					}
				}
				tree.setId(djdy.getBDCDYID());
				tree.setBdcdyid(djdy.getBDCDYID());
				tree.setBdcdylx(djdy.getBDCDYLX());
				tree.setDjdyid(djdy.getDJDYID());
				String ly = StringUtils.isEmpty(djdy.getLY()) ? "gz"
						: DJDYLY.initFrom(djdy.getLY()) == null ? "gz" : DJDYLY.initFrom(djdy.getLY()).Name;
				tree.setLy(ly);
				String zl = getZL(tree, djdy.getDJDYID(), djdy.getBDCDYLX(), djdy.getBDCDYID(), ly);
				tree.setText(zl);
				// 如果是户（实测、预测）的话，把房号也加上
				if (BDCDYLX.H.Value.equals(djdy.getBDCDYLX()) || BDCDYLX.YCH.Value.equals(djdy.getBDCDYLX())) {
					if (!StringHelper.isEmpty(djdy.getLY())) {
						DJDYLY ely = DJDYLY.initFrom(djdy.getLY());
						House house = (House) (UnitTools.loadUnit(BDCDYLX.initFrom(djdy.getBDCDYLX()), ely, djdy.getBDCDYID()));
						if (house != null) {
							String fh = house.getFH();
							tree.setFh(fh);
							if (StringHelper.isEmpty(house.getMJ())) {
								tree.setMj(0);
							} else {
								tree.setMj(house.getMJ());
							}
							if (StringHelper.isEmpty(house.getFTTDMJ())) {
								tree.setFttdmj(0);
							} else {
								tree.setFttdmj(house.getFTTDMJ());
							}
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
			String hqlCondition = MessageFormat.format(
					" DJDYID IN (SELECT DJDYID FROM BDCS_DJDY_XZ WHERE BDCDYID=''{0}'') AND QLLX IN {1} ORDER BY DJSJ",
					bdcdyid, qllxarray);
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
		Message msg=null; 
		ProjectServiceImpl serviceImpl = SuperSpringContext.getContext().getBean(ProjectServiceImpl.class);
		List<BDCS_SQR> sqrs= serviceImpl.getSQRList(super.getXMBH());
		sqrs = serviceImpl.getSQRList(super.getXMBH());
		Marshaller mashaller;
		Map<String, String> names = new HashMap<String, String>();
		
			CommonDao dao = super.getCommonDao();
			String xmbhHql = ProjectHelper.GetXMBHCondition(super.getXMBH());
			List<BDCS_DJDY_GZ> djdys = dao.getDataList(BDCS_DJDY_GZ.class, xmbhHql );
			List<BDCS_DJDY_GZ> bgqdjdys = dao.getDataList(BDCS_DJDY_GZ.class, xmbhHql + " and LY = '" + ConstValue.DJDYLY.XZ.Value + "' ");
			String msgName = "";
			if (djdys != null && djdys.size() > 0) {
				Calendar calendar = Calendar.getInstance(); // 创建一个日历对象
				calendar.setTime(new Date()); // 用当前时间初始化日历时间
				String cyear = calendar.get(Calendar.YEAR) + "";
				String ywh = packageXml.GetYWLSHByYWH(this.getProject_id());
				BDCS_XMXX xmxx = dao.get(BDCS_XMXX.class, super.getXMBH());
				String result = "";
				List<BDCS_QL_GZ> qllist = dao.getDataList(BDCS_QL_GZ.class, xmbhHql );
				if(qllist!=null&&qllist.size()>0) {
					for (int i = 0; i < qllist.size(); i++) {
						try {
							mashaller = JAXBContext.newInstance(Message.class).createMarshaller();
						BDCS_DJDY_GZ djdy = djdys.get(i);
						BDCS_QL_GZ ql = qllist.get(i);
						if("23".equals(ql.getDJLX())) {
								msg = exchangeFactory.createMessageByZXDJ();
								if (QLLX.GYJSYDSHYQ.Value.equals(super.getQllx().Value) || QLLX.ZJDSYQ.Value.equals(super.getQllx().Value) || QLLX.JTJSYDSYQ.Value.equals(super.getQllx().Value)
										||"SHYQZD".equals(super.getBdcdylx().toString())||"02".equals(djdy.getBDCDYLX())) { // 国有建设使用权、宅基地、集体建设用地使用权
									try {
										BDCS_SHYQZD_XZ zd = dao.get(BDCS_SHYQZD_XZ.class, djdy.getBDCDYID());
										
										if(zd != null && !StringUtils.isEmpty(zd.getQXDM())){
											msg.getHead().setAreaCode(zd.getQXDM());
										}
										super.fillHead(msg, i, ywh,zd.getBDCDYH(),zd.getQXDM(),ql.getLYQLID());
										Rights rights = ql;
										QLFZXDJ zxdj = msg.getData().getZXDJ();
										SubRights subrights=RightsTools.loadSubRights(DJDYLY.GZ, ql.getFSQLID());
										zxdj = packageXml.getZXDJ(zxdj, rights, ywh, zd.getQXDM(),subrights);

										DJTDJSLSQ sq = msg.getData().getDJSLSQ();
										sq = packageXml.getDJTDJSLSQ(sq, ywh, zd, ql, xmxx, null, xmxx.getSLSJ(), null, null, null);

										List<DJFDJSJ> sj = msg.getData().getDJSJ();
										sj = packageXml.getDJFDJSJ(zd, ywh,actinstID);
										msg.getData().setDJSJ(sj);

										List<DJFDJSF> sfList = msg.getData().getDJSF();
										sfList = packageXml.getDJSF(zd,ywh, this.getXMBH());
										msg.getData().setDJSF(sfList);

										List<DJFDJSH> sh = msg.getData().getDJSH();
										sh = packageXml.getDJFDJSH(zd, ywh, super.getXMBH(), actinstID);
										msg.getData().setDJSH(sh);

										List<DJFDJSZ> sz = packageXml.getDJFDJSZ(zd, ywh, super.getXMBH());
										msg.getData().setDJSZ(sz);

										List<DJFDJFZ> fz = packageXml.getDJFDJFZ(zd, ywh, super.getXMBH());
										msg.getData().setDJFZ(fz);
										List<DJFDJGD> gd = packageXml.getDJFDJGD(zd, ywh,this.getXMBH());
										msg.getData().setDJGD(gd);
										msg.getHead().setParcelID(StringHelper.formatObject(zd.getZDDM()));
										msg.getHead().setEstateNum(StringHelper.formatObject(zd.getBDCDYH()));
//										msg.getHead().setPreEstateNum(StringHelper.formatObject(zd.getBDCDYH()));
										msg.getHead().setRecType("4000101");
										List<DJFDJSQR> djsqrs = msg.getData().getDJSQR();
										djsqrs = packageXml.getDJSQRs(djsqrs, sqrs, zd.getYSDM(), ywh, zd.getBDCDYH());
										msg.getData().setDJSQR(djsqrs);

										FJF100 fj = msg.getData().getFJF100();
										fj = packageXml.getFJF(fj);
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
								if ((QLLX.GYJSYDSHYQ_FWSYQ.Value.equals(super.getQllx().Value)||"H".equals(super.getBdcdylx().toString()))&&!"02".equals(djdy.getBDCDYLX())) { 
									// 房屋所有权
									try {
										BDCS_H_XZ h = dao.get(BDCS_H_XZ.class, djdy.getBDCDYID());
										if(h != null && h.getZDBDCDYID() != null){
											BDCS_SHYQZD_XZ zd = dao.get(BDCS_SHYQZD_XZ.class, h.getZDBDCDYID());
											h.setZDDM(zd.getZDDM());
										}
										if(h != null && !StringUtils.isEmpty(h.getQXDM())){
											msg.getHead().setAreaCode(h.getQXDM());
										}
										super.fillHead(msg, i, ywh,h.getBDCDYH(),h.getQXDM(),ql.getLYQLID());
										Rights rights = ql;
										QLFZXDJ zxdj = msg.getData().getZXDJ();
										SubRights subrights=RightsTools.loadSubRights(DJDYLY.GZ, ql.getFSQLID());
										zxdj = packageXml.getZXDJ(zxdj, rights, ywh, h.getQXDM(),subrights);
										DJTDJSLSQ sq = msg.getData().getDJSLSQ();
										sq = packageXml.getDJTDJSLSQ(sq, ywh, null, ql, xmxx, h, xmxx.getSLSJ(), null, null, null);

										List<DJFDJSJ> sj = msg.getData().getDJSJ();
										sj = packageXml.getDJFDJSJ(h, ywh,actinstID);
										msg.getData().setDJSJ(sj);

										List<DJFDJSF> sfList = msg.getData().getDJSF();
										sfList = packageXml.getDJSF(h,ywh, this.getXMBH());
										msg.getData().setDJSF(sfList);

										List<DJFDJSH> sh = msg.getData().getDJSH();
										sh = packageXml.getDJFDJSH(h, ywh, super.getXMBH(), actinstID);
										msg.getData().setDJSH(sh);

										List<DJFDJSZ> sz = packageXml.getDJFDJSZ(h, ywh, super.getXMBH());
										msg.getData().setDJSZ(sz);

										List<DJFDJFZ> fz = packageXml.getDJFDJFZ(h, ywh, super.getXMBH());
										msg.getData().setDJFZ(fz);
										List<DJFDJGD> gd = packageXml.getDJFDJGD(h, ywh,this.getXMBH());
										List<DJFDJSQR> djsqrs = msg.getData().getDJSQR();
										djsqrs = packageXml.getDJSQRs(djsqrs, sqrs, h.getYSDM(), ywh, h.getBDCDYH());
										msg.getData().setDJGD(gd);

										msg.getData().setDJSQR(djsqrs);
										FJF100 fj = msg.getData().getFJF100();
										fj = packageXml.getFJF(fj);
										
										
										msg.getHead().setParcelID(StringHelper.formatObject(h.getZDDM()));
										msg.getHead().setEstateNum(StringHelper.formatObject(h.getBDCDYH()));
//										msg.getHead().setPreEstateNum(StringHelper.formatObject(h.getBDCDYH()));
										msg.getHead().setRecType("4000101");
										
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
								if (QLLX.HYSYQ.Value.equals(super.getQllx().Value) || QLLX.WJMHDSYQ.Value.equals(super.getQllx().Value)) { 
									// 海域(含无居民海岛)使用权注销
									 // 海域(含无居民海岛)使用权
									String zhdm=null;
									String hql=null;
									YHZK yhzk_gz = null;
									Sea zh = dao.get(BDCS_ZH_GZ.class, djdy.getBDCDYID());
									if (zh==null) {
										zh=dao.get(BDCS_ZH_XZ.class, djdy.getBDCDYID());
									}
									if (null != zh) {
									 zhdm=zh.getZHDM();
									 hql = "BDCDYID = '" + zh.getId() + "' ";
										List<BDCS_YHZK_GZ> yhzks = dao.getDataList(BDCS_YHZK_GZ.class, hql);
										if (yhzks != null && yhzks.size() > 0) {
											yhzk_gz = yhzks.get(0);
										}else {
											List<BDCS_YHZK_XZ> yhzksxz = dao.getDataList(BDCS_YHZK_XZ.class, hql);
											if (yhzksxz != null && yhzksxz.size() > 0) {
												yhzk_gz = yhzksxz.get(0);
											}
										}
									}
									if(zhdm!=null&&zhdm.length()>0&&(yhzk_gz.getZHDM()==null)||yhzk_gz.getZHDM().length()>0) {
										//维护数据
										yhzk_gz.setZHDM(zhdm);
									}
									// 这些字段先手动赋值 diaoliwei
									if (zh != null) {
										if (StringUtils.isEmpty(zh.getZHT())) {
											zh.setZHT("无");
										}
									}
										//设置报文头
										super.fillHead(msg, i, ywh,zh.getBDCDYH(),zh.getQXDM(),ql.getLYQLID());
										msg.getHead().setParcelID(StringHelper.formatObject(zh.getZHDM()));
										msg.getHead().setEstateNum(StringHelper.formatObject(zh.getBDCDYH()));
//										msg.getHead().setPreEstateNum(StringHelper.formatObject(zh.getBDCDYH()));
										//2.非结构化文档
										FJF100 fj = msg.getData().getFJF100();
										fj = packageXml.getFJF(fj);
										msg.getData().setFJF100(fj);

										//7.登记受理信息表
										DJTDJSLSQ sq = msg.getData().getDJSLSQ();
										sq = packageXml.getDJTDJSLSQ(sq, ywh, null, ql, xmxx, null, xmxx.getSLSJ(), null, null, zh);
										msg.getData().setDJSLSQ(sq);

										//8.登记收件(可选)
										List<DJFDJSJ> sj = msg.getData().getDJSJ();
										sj = packageXml.getDJFDJSJ(zh, ywh,actinstID);
										msg.getData().setDJSJ(sj);

										//9.登记收费(可选)
										List<DJFDJSF> sfList = msg.getData().getDJSF();
										sfList = packageXml.getDJSF(zh,ywh, this.getXMBH());
										msg.getData().setDJSF(sfList);

										//10.审核(可选)
										List<DJFDJSH> sh = msg.getData().getDJSH();
										sh = packageXml.getDJFDJSH(zh ,ywh, super.getXMBH(), actinstID);
										msg.getData().setDJSH(sh);

										//11.缮证(可选)
										List<DJFDJSZ> sz = packageXml.getDJFDJSZ(zh,ywh, super.getXMBH());
										msg.getData().setDJSZ(sz);

										//11.发证(可选)
										List<DJFDJFZ> fz = packageXml.getDJFDJFZ(zh, ywh, super.getXMBH());
										msg.getData().setDJFZ(fz);
										
										//12.归档(可选)
										List<DJFDJGD> gd = packageXml.getDJFDJGD(zh, ywh,this.getXMBH());
										msg.getData().setDJGD(gd);
										
										//13.申请人属性
										List<DJFDJSQR> djsqrs = msg.getData().getDJSQR();
										djsqrs = packageXml.getDJSQRs(djsqrs, sqrs, zh.getYSDM(), ywh, zh.getBDCDYH());
										msg.getData().setDJSQR(djsqrs);
								        //注销登记
										Rights rights = ql;
										QLFZXDJ zxdj = msg.getData().getZXDJ();
										SubRights subrights=RightsTools.loadSubRights(DJDYLY.GZ, ql.getFSQLID());
										zxdj = packageXml.getZXDJ(zxdj, rights, ywh, zh.getQXDM(),subrights);
										msg.getData().setZXDJ(zxdj);
										msg.getHead().setRecType("4000101");
								}
							msgName = getMessageFileName( msg,  i ,djdys.size(),names,djdy);
							mashaller.marshal(msg, new File(path +msgName));
							result = super.uploadFile(path +msgName, ConstValue.RECCODE.YY_ZXDJ.Value, actinstID, djdy.getDJDYID(), ql.getId());
							names.put(djdy.getDJDYID(), path +msgName);
							
						
						}else {
							List<BDCS_QLR_GZ> qlrs = super.getQLRs(ql.getId());
							

							if (QLLX.GYJSYDSHYQ.Value.equals(this.getQllx().Value) || QLLX.ZJDSYQ.Value.equals(this.getQllx().Value) || QLLX.JTJSYDSYQ.Value.equals(this.getQllx().Value)) { // 国有建设使用权、宅基地、集体建设用地使用权
								try {
									BDCS_SHYQZD_GZ zd = dao.get(BDCS_SHYQZD_GZ.class, djdy.getBDCDYID());
									String preEstateNum = "";
									// 目前未维护的字段先手动赋值 diaoliwei 2015-10-15
									if (null != zd) {
										if (StringUtils.isEmpty(zd.getZDT())) {
											zd.setZDT("无");
										}
									}
									for (int j = 0; j < bgqdjdys.size(); j++) {
										BDCS_DJDY_GZ bgqdjdy = bgqdjdys.get(j);
										BDCS_SHYQZD_XZ zd_XZ = dao.get(BDCS_SHYQZD_XZ.class, bgqdjdy.getBDCDYID());
										if (zd_XZ != null && (j == 0 || j == bgqdjdys.size() - 1)) {
											preEstateNum += zd_XZ.getBDCDYH();
										} else {
											preEstateNum += zd_XZ.getBDCDYH() + ",";
										}
									}
									// 标记单元号是否发生变化了，是否取消空间节点的存在
									boolean flag = false;
									if (!(zd.getBDCDYH().equals(preEstateNum)) && !StringUtils.isEmpty(preEstateNum)) {
										flag = true;
									}
									 msg = exchangeFactory.createMessage(flag);

									super.fillHead(msg, i, ywh,zd.getBDCDYH(),zd.getQXDM(),ql.getLYQLID());

									BDCS_DYBG dybg = packageXml.getDYBG(zd.getId());
									msg.getHead().setParcelID(StringHelper.formatObject(zd.getZDDM()));
									msg.getHead().setEstateNum(StringHelper.formatObject(zd.getBDCDYH()));// 当前的不动产单元号
//									msg.getHead().setPreEstateNum(preEstateNum);// 上一首的不动产单元号
									if (zd != null && !StringUtils.isEmpty(zd.getQXDM())) {
										msg.getHead().setAreaCode(zd.getQXDM());
									}

									if (djdy != null) {

										List<ZTTGYQLR> zttqlr = msg.getData().getGYQLR();
										zttqlr = packageXml.getZTTGYQLRs(zttqlr, qlrs, zd, ql, null, null, null, null);
										msg.getData().setGYQLR(zttqlr);

										KTTZDJBXX jbxx = msg.getData().getKTTZDJBXX();
										jbxx = packageXml.getZDJBXX(jbxx, zd, ql, null, null);

										String zdzhdm = "";
										if (zd != null) {
											zdzhdm = zd.getZDDM();
										}

										KTFZDBHQK bhqk = msg.getData().getZDBHQK();
										bhqk = packageXml.getKTFZDBHQK(bhqk, zd, ql, null, null);
										msg.getData().setZDBHQK(bhqk);

										KTTGYJZX jzx = msg.getData().getKTTGYJZX();
										jzx = packageXml.getKTTGYJZX(jzx, zdzhdm);

										KTTGYJZD jzd = msg.getData().getKTTGYJZD();
										jzd = packageXml.getKTTGYJZD(jzd, zdzhdm);

										QLFQLJSYDSYQ syq = msg.getData().getQLJSYDSYQ();
										syq = packageXml.getQLFQLJSYDSYQ(syq, zd, ql, ywh);

										DJTDJSLSQ sq = msg.getData().getDJSLSQ();
										sq = packageXml.getDJTDJSLSQ(sq, ywh, zd, ql, xmxx, null, xmxx.getSLSJ(), null, null, null);

										List<DJFDJSJ> sj = msg.getData().getDJSJ();
										sj = packageXml.getDJFDJSJ(zd, ywh,actinstID);
										msg.getData().setDJSJ(sj);

										List<DJFDJSF> sfList = msg.getData().getDJSF();
										sfList = packageXml.getDJSF(zd,ywh, this.getXMBH());
										msg.getData().setDJSF(sfList);

										List<DJFDJSH> sh = msg.getData().getDJSH();
										sh = packageXml.getDJFDJSH(zd, ywh, this.getXMBH(), actinstID);
										msg.getData().setDJSH(sh);

										List<DJFDJSZ> sz = packageXml.getDJFDJSZ(zd, ywh, this.getXMBH());
										msg.getData().setDJSZ(sz);

										List<DJFDJFZ> fz = packageXml.getDJFDJFZ(zd, ywh, this.getXMBH());
										msg.getData().setDJFZ(fz);
										List<DJFDJGD> gd = packageXml.getDJFDJGD(zd, ywh,this.getXMBH());
										msg.getData().setDJGD(gd);
										
										BDCS_SHYQZD_GZ zd2 = null;
										if (null != dybg) {
											zd2 = new BDCS_SHYQZD_GZ();
											zd2.setId(dybg.getLBDCDYID());
//										zd.setId(dybg.getLBDCDYID());
										}
										// 如果当前不动产单元号和上一个不动产单元号不同的话，把空间节点加进去
										if (!(preEstateNum.equals(zd.getBDCDYH()))) {
											List<ZDK103> zdk = msg.getData().getZDK103();
											zdk = packageXml.getZDK103(zdk, zd2, null, null);
											msg.getData().setZDK103(zdk);
										}
										List<DJFDJSQR> djsqrs = msg.getData().getDJSQR();
										djsqrs = packageXml.getDJSQRs(djsqrs, sqrs, zd.getYSDM(), ywh, zd.getBDCDYH());
										msg.getData().setDJSQR(djsqrs);

										FJF100 fj = msg.getData().getFJF100();
										fj = packageXml.getFJF(fj);
									}
									msgName = "Biz" + msg.getHead().getBizMsgID() + ".xml";
									mashaller.marshal(msg, new File(path + msgName));
									result = uploadFile(path + msgName, ConstValue.RECCODE.JSYDSHYQ_BGDJ.Value,actinstID,djdy.getDJDYID(),ql.getId());
									names.put(djdy.getDJDYID(), msg.getHead().getBizMsgID() + ".xml");
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
							if (QLLX.GYJSYDSHYQ_FWSYQ.Value.equals(this.getQllx().Value)||"H".equals(super.getBdcdylx().toString())) { // 房屋所有权
								try {
									BDCS_H_GZ h = dao.get(BDCS_H_GZ.class, djdy.getBDCDYID());
									if(h==null) {
										BDCS_H_XZ h_xz = dao.get(BDCS_H_XZ.class, djdy.getBDCDYID());
										if(h_xz!=null) {
											h=ObjectHelper.copyHFromXZtoGZ(h_xz);
										}
										
									}
									BDCS_ZRZ_XZ zrz_gz = null;
									if (h != null && !StringUtils.isEmpty(h.getZRZBDCDYID())) {
										zrz_gz = dao.get(BDCS_ZRZ_XZ.class, h.getZRZBDCDYID());
										if(zrz_gz != null){
											zrz_gz.setGHYT(h.getGHYT()); //自然幢的ghyt取户的ghyt
											zrz_gz.setFWJG(zrz_gz.getFWJG() == null || zrz_gz.getFWJG().equals("") ? h.getFWJG() : zrz_gz.getFWJG());
										}
									}
									BDCS_LJZ_XZ ljz_gz = null;
									if (h != null && !StringUtils.isEmpty(h.getLJZID())) {
										ljz_gz = dao.get(BDCS_LJZ_XZ.class, h.getLJZID());
									}
									if (h != null) {
										BDCS_SHYQZD_XZ zd = dao.get(BDCS_SHYQZD_XZ.class, h.getZDBDCDYID());
										h.setZDDM(zd.getZDDM());
										if(zrz_gz != null){
											zrz_gz.setZDDM(zd.getZDDM());
										}
									}
									 msg = exchangeFactory.createMessageByFWSYQ();
									super.fillHead(msg, i, ywh,h.getBDCDYH(),h.getQXDM(),ql.getLYQLID());
									msg.getHead().setParcelID(StringHelper.formatObject(h.getZDDM()));
									msg.getHead().setEstateNum(StringHelper.formatObject(h.getBDCDYH()));
									if (h != null && !StringUtils.isEmpty(h.getQXDM())) {
										msg.getHead().setAreaCode(h.getQXDM());
									}
									String preEstateNum = "";
									for (BDCS_DJDY_GZ bgqdjdy : bgqdjdys) {
										BDCS_H_LS h_LS = dao.get(BDCS_H_LS.class, bgqdjdy.getBDCDYID());
										if (!StringUtils.isEmpty(h.getId()) && h.getId().equals(h_LS.getId())) {
											preEstateNum = h_LS.getBDCDYH();
											break;
										}
									}
//									msg.getHead().setPreEstateNum(preEstateNum);
									BDCS_C_GZ c = null;
									if (h != null && !StringUtils.isEmpty(h.getCID())) {
										c = dao.get(BDCS_C_GZ.class, h.getCID());
									}

									if (djdy != null) {
										List<ZTTGYQLR> zttqlr = msg.getData().getGYQLR();
										zttqlr = packageXml.getZTTGYQLRs(zttqlr, qlrs, null, ql, h, null, null, null);
										msg.getData().setGYQLR(zttqlr);

										KTTFWZRZ zrz = msg.getData().getKTTFWZRZ();
										zrz = packageXml.getKTTFWZRZ(zrz, zrz_gz);

										KTTFWLJZ ljz = msg.getData().getKTTFWLJZ();
										ljz = packageXml.getKTTFWLJZ(ljz, ljz_gz,h);;

										KTTFWC kttc = msg.getData().getKTTFWC();
										kttc = packageXml.getKTTFWC(kttc, c, zrz);
										msg.getData().setKTTFWC(kttc);

										KTTFWH fwh = msg.getData().getKTTFWH();
										fwh = packageXml.getKTTFWH(fwh, h);

										QLTFWFDCQYZ fdcqyz = msg.getData().getQLTFWFDCQYZ();
										fdcqyz = packageXml.getQLTFWFDCQYZ(fdcqyz, h, ql, ywh);

										List<ZDK103> fwk = msg.getData().getZDK103();
										fwk = packageXml.getZDK103H(fwk, h, zrz_gz);

										DJTDJSLSQ sq = msg.getData().getDJSLSQ();
										sq = packageXml.getDJTDJSLSQ(sq, ywh, null, ql, xmxx, h, xmxx.getSLSJ(), null, null, null);

										List<DJFDJSJ> sj = msg.getData().getDJSJ();
										sj = packageXml.getDJFDJSJ(h, ywh,actinstID);
										msg.getData().setDJSJ(sj);

										List<DJFDJSF> sfList = msg.getData().getDJSF();
										sfList = packageXml.getDJSF(h,ywh, this.getXMBH());
										msg.getData().setDJSF(sfList);

										List<DJFDJSH> sh = msg.getData().getDJSH();
										sh = packageXml.getDJFDJSH(h, ywh, this.getXMBH(), actinstID);
										msg.getData().setDJSH(sh);

										List<DJFDJSZ> sz = packageXml.getDJFDJSZ(h, ywh, this.getXMBH());
										msg.getData().setDJSZ(sz);
										List<DJFDJFZ> fz = packageXml.getDJFDJFZ(h, ywh, this.getXMBH());
										msg.getData().setDJFZ(fz);
										List<DJFDJGD> gd = packageXml.getDJFDJGD(h, ywh,this.getXMBH());
										msg.getData().setDJGD(gd);

										List<DJFDJSQR> djsqrs = msg.getData().getDJSQR();
										djsqrs = packageXml.getDJSQRs(djsqrs, sqrs, h.getYSDM(), ywh, h.getBDCDYH());
										msg.getData().setDJSQR(djsqrs);

										FJF100 fj = msg.getData().getFJF100();
										fj = packageXml.getFJF(fj);
									}
									msgName = "Biz" + msg.getHead().getBizMsgID() + ".xml";
									mashaller.marshal(msg, new File(path + msgName));
									result = uploadFile(path + msgName, ConstValue.RECCODE.FDCQDZ_BGDJ.Value,actinstID,djdy.getDJDYID(),ql.getId());
									names.put(djdy.getDJDYID(), msg.getHead().getBizMsgID() + ".xml");
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
							if(null == result){
								Map<String, String> xmlError = new HashMap<String, String>();
								if (QLLX.JTTDSYQ.Value.equals(this.getQllx().Value)) {
									YwLogUtil.addSjsbResult(msgName, "", "连接SFTP失败,请检查服务器和前置机的连接是否正常", ConstValue.SF.NO.Value, ConstValue.RECCODE.TDSYQ_BGDJ.Value,ProjectHelper.getpRroinstIDByActinstID(actinstID));	
								}else if (QLLX.GYJSYDSHYQ_FWSYQ.Value.equals(this.getQllx().Value)){
									YwLogUtil.addSjsbResult(msgName, "", "连接SFTP失败,请检查服务器和前置机的连接是否正常", ConstValue.SF.NO.Value, ConstValue.RECCODE.FDCQDZ_BGDJ.Value,ProjectHelper.getpRroinstIDByActinstID(actinstID));
								}else {
									YwLogUtil.addSjsbResult(msgName, "", "连接SFTP失败,请检查服务器和前置机的连接是否正常", ConstValue.SF.NO.Value, ConstValue.RECCODE.JSYDSHYQ_BGDJ.Value,ProjectHelper.getpRroinstIDByActinstID(actinstID));
								}
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
					} catch (Exception e) {
						e.printStackTrace();
					}
					}
				}
			}
		
		return names;
	
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

	/**
	 * 根据工作权利ID和拷贝现状权利人到申请人，并将sqrid传入qlr_gz
	 * @Author：rq
	 * @param xzqlid
	 * @param gzqlid
	 */
	private void CopySQRFromGZQLR(String xzqlid, String gzqlid) {
		StringBuilder builderQLR = new StringBuilder();
		builderQLR.append("QLID ='");
		builderQLR.append(gzqlid);
		builderQLR.append("'");
		List<BDCS_QLR_GZ> qlrs = getCommonDao().getDataList(BDCS_QLR_GZ.class, builderQLR.toString());
		if (qlrs == null || qlrs.size() <= 0) {
			return;
		}
		for (int iqlr = 0; iqlr < qlrs.size(); iqlr++) {
			BDCS_QLR_GZ qlr = qlrs.get(iqlr);
			// 通过权利人名称和权利人证件号进行过滤，相同的权利人不重复添加到申请人。2015年12月24日晚23点刘树峰
			String zjhm = qlr.getZJH();
			boolean bexists = false;
			if (!StringHelper.isEmpty(qlr.getQLRMC())) {
				String Sql = "";
				if (!StringHelper.isEmpty(zjhm)) {
					Sql = MessageFormat.format(" XMBH=''{0}'' AND SQRXM=''{1}'' AND ZJH=''{2}''", getXMBH(), qlr.getQLRMC(), zjhm);
				} else {
					Sql = MessageFormat.format(" XMBH=''{0}'' AND SQRXM=''{1}'' AND ZJH IS NULL", getXMBH(), qlr.getQLRMC());
				}
				List<BDCS_SQR> sqrlist = getCommonDao().getDataList(BDCS_SQR.class, Sql);
				if (sqrlist != null && sqrlist.size() > 0) {
					bexists = true;
					//权利人关联申请人ID
					qlr.setSQRID(sqrlist.get(0).getId());
					getCommonDao().update(qlr);
				}
			}
			if (!bexists) {
				String SQRID = getPrimaryKey();
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
				sqr.setId(SQRID);
				sqr.setGLQLID(gzqlid);
				getCommonDao().save(sqr);
				//权利人关联申请人ID
				qlr.setSQRID(sqr.getId());
				getCommonDao().update(qlr);
			}
		}
	}
	
}
