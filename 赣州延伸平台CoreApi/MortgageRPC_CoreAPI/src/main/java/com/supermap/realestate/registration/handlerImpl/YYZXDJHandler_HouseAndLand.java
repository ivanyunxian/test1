package com.supermap.realestate.registration.handlerImpl;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apache.commons.beanutils.PropertyUtils;
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
import com.supermap.realestate.registration.dataExchange.exchangeFactory;
import com.supermap.realestate.registration.dataExchange.utils.packageXml;
import com.supermap.realestate.registration.dataExchange.zxdj.QLFZXDJ;
import com.supermap.realestate.registration.handler.DJHandler;
import com.supermap.realestate.registration.model.BDCS_DJDY_GZ;
import com.supermap.realestate.registration.model.BDCS_DJDY_LS;
import com.supermap.realestate.registration.model.BDCS_DJDY_XZ;
import com.supermap.realestate.registration.model.BDCS_FSQL_GZ;
import com.supermap.realestate.registration.model.BDCS_FSQL_LS;
import com.supermap.realestate.registration.model.BDCS_FSQL_XZ;
import com.supermap.realestate.registration.model.BDCS_H_XZ;
import com.supermap.realestate.registration.model.BDCS_QDZR_GZ;
import com.supermap.realestate.registration.model.BDCS_QDZR_LS;
import com.supermap.realestate.registration.model.BDCS_QDZR_XZ;
import com.supermap.realestate.registration.model.BDCS_QLR_GZ;
import com.supermap.realestate.registration.model.BDCS_QLR_LS;
import com.supermap.realestate.registration.model.BDCS_QLR_XZ;
import com.supermap.realestate.registration.model.BDCS_QL_GZ;
import com.supermap.realestate.registration.model.BDCS_QL_LS;
import com.supermap.realestate.registration.model.BDCS_QL_XZ;
import com.supermap.realestate.registration.model.BDCS_SHYQZD_XZ;
import com.supermap.realestate.registration.model.BDCS_SQR;
import com.supermap.realestate.registration.model.BDCS_XMXX;
import com.supermap.realestate.registration.model.BDCS_YHZK_GZ;
import com.supermap.realestate.registration.model.BDCS_YHZK_XZ;
import com.supermap.realestate.registration.model.BDCS_ZH_GZ;
import com.supermap.realestate.registration.model.BDCS_ZH_XZ;
import com.supermap.realestate.registration.model.BDCS_ZS_GZ;
import com.supermap.realestate.registration.model.BDCS_ZS_LS;
import com.supermap.realestate.registration.model.BDCS_ZS_XZ;
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
import com.supermap.realestate.registration.util.ConstValue.QLLX;
import com.supermap.realestate.registration.util.ConstValue.SFDB;
import com.supermap.realestate.registration.util.ConstValue.SQRLB;
import com.supermap.realestate.registration.util.Global;
import com.supermap.realestate.registration.util.ObjectHelper;
import com.supermap.realestate.registration.util.ProjectHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.realestate.registration.util.YwLogUtil;
import com.supermap.wisdombusiness.core.SuperHelper;
import com.supermap.wisdombusiness.core.SuperSpringContext;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import com.supermap.wisdombusiness.web.ResultMessage;



/**
 * 房地一体异议注销登记操作类
 * @ClassName: YYZXDJHandler_HouseAndLand
 * @author liangc
 * @date 2018年5月17日 上午10:46:30
 */
public class YYZXDJHandler_HouseAndLand extends DJHandlerBase implements DJHandler {

	/**
	 * 构造函数
	 * 
	 * @param info
	 */
	public YYZXDJHandler_HouseAndLand(ProjectInfo info) {
		super(info);
	}

	/**
	 * 添加不动产单元
	 */
	@Override
	public boolean addBDCDY(String qlids) {
		String qlid[] = qlids.split(",");
		if (qlid != null && qlid.length > 0) {
			for (String id : qlid) {
				StringBuilder builer = new StringBuilder();
				builer.append(" QLID='").append(id).append("'");
				String strQuery = builer.toString();
				BDCS_QL_XZ bdcs_ql_xz = getCommonDao().get(BDCS_QL_XZ.class, id);
				if (bdcs_ql_xz != null) {
					String gzqlid = getPrimaryKey();
					String gzfsqlid = getPrimaryKey();
					// 拷贝权利
					BDCS_QL_GZ bdcs_ql_gz = ObjectHelper.copyQL_XZToGZ(bdcs_ql_xz);
					bdcs_ql_gz.setId(gzqlid);
					bdcs_ql_gz.setFSQLID(gzfsqlid);
					bdcs_ql_gz.setXMBH(getXMBH());
					bdcs_ql_gz.setLYQLID(id);
					bdcs_ql_gz.setDJLX(getDjlx().Value);
					bdcs_ql_gz.setDJYY("");
					bdcs_ql_gz.setFJ("");
					getCommonDao().save(bdcs_ql_gz);
					BDCS_FSQL_XZ bdcs_fsql_xz = getCommonDao().get(BDCS_FSQL_XZ.class, bdcs_ql_xz.getFSQLID());
					if (bdcs_fsql_xz != null) {
						// 拷贝附属权利
						BDCS_FSQL_GZ bdcs_fsql_gz = ObjectHelper.copyFSQL_XZToGZ(bdcs_fsql_xz);
						bdcs_fsql_gz.setQLID(gzqlid);
						bdcs_fsql_gz.setId(gzfsqlid);
						bdcs_fsql_gz.setXMBH(getXMBH());
						bdcs_fsql_gz.setZXDYYWH(super.getProject_id());
						getCommonDao().save(bdcs_fsql_gz);
					}
					StringBuilder builderDJDY = new StringBuilder();
					builderDJDY.append(" DJDYID='");
					builderDJDY.append(bdcs_ql_xz.getDJDYID());
					builderDJDY.append("'");
					// 获取登记单元集合
					List<BDCS_DJDY_XZ> djdys = getCommonDao().getDataList(BDCS_DJDY_XZ.class, builderDJDY.toString());
					if (djdys != null && djdys.size() > 0) {
						BDCS_DJDY_XZ bdcs_djdy_xz = djdys.get(0);
						// 拷贝登记单元
						BDCS_DJDY_GZ bdcs_djdy_gz = ObjectHelper.copyDJDY_XZToGZ(bdcs_djdy_xz);
						bdcs_djdy_gz.setId(getPrimaryKey());
						bdcs_djdy_gz.setLY(DJDYLY.XZ.Value);
						bdcs_djdy_gz.setXMBH(getXMBH());
						getCommonDao().save(bdcs_djdy_gz);
					}
					// 获取权利人集合
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
								
								BDCS_SQR sqr=copyXZQLRtoSQR(bdcs_qlr_xz,SQRLB.JF);
								if(sqr!=null){
									sqr.setXMBH(getXMBH());
									sqr.setGLQLID(bdcs_ql_gz.getId());
									sqr.setId((String)SuperHelper.GeneratePrimaryKey());
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
											builderQDZR.append(" AND ZSID='");
											builderQDZR.append(bdcs_zs_xz.getId());
											builderQDZR.append("' AND QLID='");
											builderQDZR.append(id);
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
			getCommonDao().flush();
			return true;
		} else {
			ResultMessage msg = new ResultMessage();
			msg.setMsg("选择失败!");
			msg.setSuccess("false");
			return false;
		}
	}

	/**
	 * 写入登记薄
	 */
	@Override
	public boolean writeDJB() {
		/*
		 * 登簿逻辑：删除现状层的异议登记，更新历史层的异议登记
		 */
		CommonDao dao = getCommonDao();
		String dbr = Global.getCurrentUserName();
		String strSqlXMBH = ProjectHelper.GetXMBHCondition(getXMBH());
		
		List<BDCS_QL_GZ> gzqls = dao.getDataList(BDCS_QL_GZ.class, strSqlXMBH);
		BDCDYLX dylx = ProjectHelper.GetBDCDYLX(getXMBH());
		List<String> zdbdcdyids = new ArrayList<String>();
		if (gzqls != null && gzqls.size() > 0) {
			for (BDCS_QL_GZ ql : gzqls) {
				String lyqlid = ql.getLYQLID();
				RightsTools.deleteRightsAll(DJDYLY.XZ, lyqlid);
				//复制BDCS_QL_GZ到历史和现状
				List<BDCS_DJDY_GZ> h_djdys=getCommonDao().getDataList(BDCS_DJDY_GZ.class, strSqlXMBH+" AND BDCDYID='"+ql.getBDCDYID()+"'");
				if (h_djdys!=null&&h_djdys.size()>0) {
					for(BDCS_DJDY_GZ djdy_gz:h_djdys) {
						BDCS_DJDY_LS ls=ObjectHelper.copyDJDY_XZToLS(ObjectHelper.copyDJDY_GZToXZ(djdy_gz));
						ls.setDJDYID(ql.getBDCDYID());
						ls.setId(UUID.randomUUID().toString());
						dao.save(ls);
					}
				}
				SubRights lssubrights = RightsTools.loadSubRightsByRightsID(DJDYLY.LS, lyqlid);
				if (lssubrights != null) {
					lssubrights.setZXDBR(dbr);
					lssubrights.setZXDYYY(ql.getDJYY());
					lssubrights.setZXSJ(new Date());
					lssubrights.setZXFJ(ql.getFJ());
					lssubrights.setZXDYYWH(getProject_id());
					lssubrights.setXMBH(gzqls.get(0).getXMBH());
					dao.update(lssubrights);
				}
				BDCS_FSQL_GZ fsql=dao.get(BDCS_FSQL_GZ.class, ql.getFSQLID());
				if(fsql!=null)
				{
					fsql.setZXDYYY(ql.getDJYY());
					fsql.setZXDBR(dbr);
					fsql.setZXSJ(new Date());
					dao.update(fsql);
				}
				RealUnit _srcunit = UnitTools.loadUnit(dylx, DJDYLY.XZ, ql.getBDCDYID());
				if (_srcunit != null) {
					if(_srcunit instanceof House){
						House house = (House) _srcunit;
						if(!zdbdcdyids.contains(house.getZDBDCDYID())){
							zdbdcdyids.add(house.getZDBDCDYID());
							updateZD(ql.getBDCDYID(), ql.getDJDYID(),lyqlid);
						}
					}
				}
			}
		}
		super.SetSFDB();
		super.alterCachedXMXX();
		dao.flush();
		return true;
	}
	
	public void updateZD(String bdcdyid_h, String djdyid_h,String lyqlid) {
		BDCDYLX dylx = ProjectHelper.GetBDCDYLX(getXMBH());
		RealUnit unit_h = UnitTools.loadUnit(dylx, DJDYLY.XZ, bdcdyid_h);
		if (unit_h == null) {
			return;
		}
		House h = (House) unit_h;
		String bdcdyid_zd = h.getZDBDCDYID();
		if (StringHelper.isEmpty(bdcdyid_zd)) {
			return;
		}

		RealUnit unit_zd = UnitTools.loadUnit(BDCDYLX.SHYQZD, DJDYLY.XZ, bdcdyid_zd);
		if (unit_zd == null) {
			return;
		}
		
		
		BDCS_DJDY_XZ djdyid_zd=null;
		List<BDCS_DJDY_XZ> zd_djdys=getCommonDao().getDataList(BDCS_DJDY_XZ.class, "BDCDYID='"+bdcdyid_zd+"' AND BDCDYLX='02'");
		if(zd_djdys!=null&&zd_djdys.size()>0){
			djdyid_zd=zd_djdys.get(0);
		}else{
			djdyid_zd=new BDCS_DJDY_XZ();
			List<BDCS_DJDY_LS> zd_djdys_ls=getCommonDao().getDataList(BDCS_DJDY_LS.class, "BDCDYID='"+bdcdyid_zd+"' AND BDCDYLX='02'");
			if(zd_djdys_ls!=null&&zd_djdys_ls.size()>0){
				ObjectHelper.copyObject(zd_djdys_ls.get(0), djdyid_zd);
			}
		}
		if (djdyid_zd!=null) {
			BDCS_DJDY_LS zd_ls = ObjectHelper.copyDJDY_XZToLS(djdyid_zd);
			zd_ls.setId(UUID.randomUUID().toString());
			zd_ls.setBDCDYH(unit_zd.getBDCDYH());
			//zd_ls.setBDCDYID(unit_zd.getId());
			zd_ls.setBDCDYLX(BDCDYLX.SHYQZD.Value);
			zd_ls.setDJDYID(UUID.randomUUID().toString());
			zd_ls.setLY(DJDYLY.LS.Value);
			zd_ls.setXMBH(super.getXMBH());
			zd_ls.setGROUPID(1);
			getCommonDao().save(zd_ls);
			}
		
		List<BDCS_QL_GZ> lsql=getCommonDao().getDataList(BDCS_QL_GZ.class, "  QLID='"+lyqlid+"'");
		if(lsql!=null&&lsql.size()>0){
			String lsxmbh = lsql.get(0).getXMBH();
			List<BDCS_QL_XZ> zd_qls=getCommonDao().getDataList(BDCS_QL_XZ.class, "BDCDYID='"+bdcdyid_zd+"' AND XMBH='"+lsxmbh+"'");
			if(zd_qls!=null&&zd_qls.size()>0){
				for (int iql = 0; iql < zd_qls.size(); iql++) {
					BDCS_QL_XZ bdcs_ql_xz = zd_qls.get(iql);
					super.removeQLXXFromXZByQLID(bdcs_ql_xz.getId());
					Rights right_h = RightsTools.loadRightsByDJDYID(DJDYLY.GZ, super.getXMBH(), djdyid_h);
					SubRights lssubrights = RightsTools.loadSubRightsByRightsID(DJDYLY.LS, bdcs_ql_xz.getId());
					if (lssubrights != null) {
						lssubrights.setZXDBR(Global.getCurrentUserName());
						lssubrights.setZXDYYY(right_h.getDJYY());
						lssubrights.setZXSJ(new Date());
						lssubrights.setZXFJ(right_h.getFJ());
						lssubrights.setZXDYYWH(getProject_id());
						lssubrights.setXMBH(super.getXMBH());
						getCommonDao().update(lssubrights);
					}
				}
			}
		}
		Rights right_h = RightsTools.loadRightsByDJDYID(DJDYLY.GZ, super.getXMBH(), djdyid_h);
		if (right_h != null) {
			SubRights subright_h = RightsTools.loadSubRights(DJDYLY.GZ, right_h.getFSQLID());
			String qlid_zd = SuperHelper.GeneratePrimaryKey();
			String fsqlid_zd = SuperHelper.GeneratePrimaryKey();
			//拷贝权利
			BDCS_QL_GZ ql_zd_gz = new BDCS_QL_GZ();
			ObjectHelper.copyObject((BDCS_QL_GZ) right_h,ql_zd_gz);
			ql_zd_gz.setId(qlid_zd);
			ql_zd_gz.setFSQLID(fsqlid_zd);
			ql_zd_gz.setDJDYID(djdyid_zd.getDJDYID());
			ql_zd_gz.setBDCDYH(djdyid_zd.getBDCDYH());
			getCommonDao().save(ql_zd_gz);
			BDCS_FSQL_GZ fsql_zd_gz = new BDCS_FSQL_GZ();
			ObjectHelper.copyObject((BDCS_FSQL_GZ) subright_h,fsql_zd_gz);
			fsql_zd_gz.setId(fsqlid_zd);
			fsql_zd_gz.setQLID(qlid_zd);
			fsql_zd_gz.setDJDYID(djdyid_zd.getDJDYID());
			fsql_zd_gz.setBDCDYH(djdyid_zd.getBDCDYH());
			getCommonDao().save(fsql_zd_gz);
			List<RightsHolder> qlrs_h=RightsHolderTools.loadRightsHolders(DJDYLY.GZ, right_h.getId());
			List<String> zsids = new ArrayList<String>();
			List<String> zdzsids = new ArrayList<String>();
			if(qlrs_h != null && qlrs_h.size()>0){
				for (RightsHolder rightsHolder : qlrs_h) {
					String qlrid=super.getPrimaryKey();
					BDCS_QLR_GZ qlr = new BDCS_QLR_GZ();
					ObjectHelper.copyObject((BDCS_QLR_GZ) rightsHolder,qlr);
					qlr.setId(qlrid);
					qlr.setQLID(qlid_zd);
					getCommonDao().save(qlr);
					List<BDCS_QDZR_GZ> qdzrs=getCommonDao().getDataList(BDCS_QDZR_GZ.class, "QLRID='"+rightsHolder.getId()+"'");
					String zsid = "";
					for (BDCS_QDZR_GZ bdcs_QDZR_GZ : qdzrs) {
						if(!zsids.contains(bdcs_QDZR_GZ.getZSID())){
							zsids.add(bdcs_QDZR_GZ.getZSID());
							zsid = SuperHelper.GeneratePrimaryKey();
							zdzsids.add(zsid);
						}
					}
				}
			}
			for (String zsid : zdzsids) {
				BDCS_ZS_GZ zs_zd_gz = new BDCS_ZS_GZ();
				zs_zd_gz.setXMBH(super.getXMBH());
				zs_zd_gz.setId(zsid);
				getCommonDao().save(zs_zd_gz);
			
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
			
			//顺便删除申请人
			String sql=" GLQLID='" + qlid + "' AND XMBH='" + xmbh + "'";
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
		for (UnitTree tree : list) {
			String qlid = tree.getQlid();
			Rights rights = RightsTools.loadRights(DJDYLY.GZ, qlid);
			if (rights != null) {
				tree.setOlddiyqqlid(rights.getLYQLID());
			}
		}
		return list;
	}

	/**
	 * 根据申请人ID数组添加申请人
	 */
	@Override
	public void addQLRbySQRArray(String qlid, Object[] sqrids) {
	}

	/**
	 * 移除权利人
	 */
	@Override
	public void removeQLR(String qlid, String qlrid) {
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
		String result = "";
		Marshaller mashaller;
		Map<String, String> names = new HashMap<String, String>();
		CommonDao dao = super.getCommonDao();
		String xmbhHql = ProjectHelper.GetXMBHCondition(super.getXMBH());
		List<BDCS_DJDY_GZ> djdys = dao.getDataList(BDCS_DJDY_GZ.class, xmbhHql);
			//房地一体路程查询历史层
			List<BDCS_DJDY_LS> djdys_ls=dao.getDataList(BDCS_DJDY_LS.class, xmbhHql);
			BDCS_DJDY_GZ gz=null;
			if(djdys_ls!=null&&djdys_ls.size()>0) {
				for(BDCS_DJDY_LS ls:djdys_ls) {
					try {
						
						if(ls.getBDCDYLX()!=null&&!"".equals(ls.getBDCDYLX())&&"02".equals(ls.getBDCDYLX())) {
							gz=new BDCS_DJDY_GZ();
							PropertyUtils.copyProperties(gz, ls);
						}
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						e.printStackTrace();
					} catch (NoSuchMethodException e) {
						e.printStackTrace();
					}
					if(gz!=null) {
						djdys.add(gz);
					}
				}
			} 
	  
		
		try {
			if (djdys != null && djdys.size() > 0) {
				mashaller = JAXBContext.newInstance(Message.class).createMarshaller();
				Calendar calendar = Calendar.getInstance(); // 创建一个日历对象
				calendar.setTime(new Date()); // 用当前时间初始化日历时间
				String cyear = calendar.get(Calendar.YEAR) + "";
				
				for (int i = 0; i < djdys.size(); i++) {
					BDCS_DJDY_GZ djdy = djdys.get(i);
					String ywh = packageXml.GetYWLSHByYWH(super.getProject_id());
					BDCS_QL_GZ ql = super.getQL(djdy.getDJDYID());
					// List<BDCS_QLR_GZ> qlrs = super.getQLRs(ql.getId());
					List<BDCS_SQR> sqrs = new ArrayList<BDCS_SQR>();
					 msg = exchangeFactory.createMessageByZXDJ();
					ProjectServiceImpl serviceImpl = SuperSpringContext.getContext().getBean(ProjectServiceImpl.class);
					sqrs = serviceImpl.getSQRList(super.getXMBH());

					BDCS_XMXX xmxx = dao.get(BDCS_XMXX.class, super.getXMBH());
					BDCS_H_XZ h = null;
//					System.out.println(super.getQllx().Value);
//					System.out.println(super.getBdcdylx().toString());
					if (djdy != null) {
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

								//9.登记收费(可选)
								List<DJFDJSF> sfList = msg.getData().getDJSF();
								sfList = packageXml.getDJSF(zd, ywh,this.getXMBH());
								msg.getData().setDJSF(sfList);

								//10.审核(可选)
								List<DJFDJSH> sh = msg.getData().getDJSH();
								 sh = packageXml.getDJFDJSH(zd, ywh, this.getXMBH(), actinstID);
								msg.getData().setDJSH(sh);

								//11.缮证(可选)
								List<DJFDJSZ>  sz = packageXml.getDJFDJSZ(zd, ywh, this.getXMBH());
								msg.getData().setDJSZ(sz);

								//11.发证(可选)
								List<DJFDJFZ>   fz = packageXml.getDJFDJFZ(zd, ywh, this.getXMBH());
								msg.getData().setDJFZ(fz);
								
								//12.归档(可选)
								List<DJFDJGD> gd = packageXml.getDJFDJGD(zd, ywh, this.getXMBH());
								msg.getData().setDJGD(gd);	
								msg.getHead().setParcelID(StringHelper.formatObject(zd.getZDDM()));
								msg.getHead().setEstateNum(StringHelper.formatObject(zd.getBDCDYH()));
//								msg.getHead().setPreEstateNum(StringHelper.formatObject(zd.getBDCDYH()));
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
								h = dao.get(BDCS_H_XZ.class, djdy.getBDCDYID());
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
								//9.登记收费(可选)
								List<DJFDJSF> sfList = msg.getData().getDJSF();
								sfList = packageXml.getDJSF(h, ywh,this.getXMBH());
								msg.getData().setDJSF(sfList);

								//10.审核(可选)
								List<DJFDJSH> sh = msg.getData().getDJSH();
								 sh = packageXml.getDJFDJSH(h, ywh, this.getXMBH(), actinstID);
								msg.getData().setDJSH(sh);

								//11.缮证(可选)
								List<DJFDJSZ>  sz = packageXml.getDJFDJSZ(h, ywh, this.getXMBH());
								msg.getData().setDJSZ(sz);

								//11.发证(可选)
								List<DJFDJFZ>   fz = packageXml.getDJFDJFZ(h, ywh, this.getXMBH());
								msg.getData().setDJFZ(fz);
								
								//12.归档(可选)
								List<DJFDJGD> gd = packageXml.getDJFDJGD(h, ywh, this.getXMBH());
								msg.getData().setDJGD(gd);
								List<DJFDJSQR> djsqrs = msg.getData().getDJSQR();
								djsqrs = packageXml.getDJSQRs(djsqrs, sqrs, h.getYSDM(), ywh, h.getBDCDYH());
								msg.getData().setDJGD(gd);

								msg.getData().setDJSQR(djsqrs);
								FJF100 fj = msg.getData().getFJF100();
								fj = packageXml.getFJF(fj);
								
								
								msg.getHead().setParcelID(StringHelper.formatObject(h.getZDDM()));
								msg.getHead().setEstateNum(StringHelper.formatObject(h.getBDCDYH()));
//								msg.getHead().setPreEstateNum(StringHelper.formatObject(h.getBDCDYH()));
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
//								msg.getHead().setPreEstateNum(StringHelper.formatObject(zh.getBDCDYH()));
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
								sfList = packageXml.getDJSF(zh, ywh,this.getXMBH());
								msg.getData().setDJSF(sfList);

								//10.审核(可选)
								List<DJFDJSH> sh = msg.getData().getDJSH();
								 sh = packageXml.getDJFDJSH(zh, ywh, this.getXMBH(), actinstID);
								msg.getData().setDJSH(sh);

								//11.缮证(可选)
								List<DJFDJSZ>  sz = packageXml.getDJFDJSZ(zh, ywh, this.getXMBH());
								msg.getData().setDJSZ(sz);

								//11.发证(可选)
								List<DJFDJFZ>   fz = packageXml.getDJFDJFZ(zh, ywh, this.getXMBH());
								msg.getData().setDJFZ(fz);
								
								//12.归档(可选)
								List<DJFDJGD> gd = packageXml.getDJFDJGD(zh, ywh, this.getXMBH());
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
					}
					String msgName = getMessageFileName( msg,  i ,djdys.size(),names,djdy);
					mashaller.marshal(msg, new File(path +msgName));
					result = super.uploadFile(path +msgName, ConstValue.RECCODE.YY_ZXDJ.Value, actinstID, djdy.getDJDYID(), ql.getId());
					names.put(djdy.getDJDYID(), path +msgName);
					
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(null == result){
			Map<String, String> xmlError = new HashMap<String, String>();
			xmlError.put("error", "连接SFTP失败,请检查服务器和前置机的连接是否正常");
			YwLogUtil.addSjsbResult("Biz" + msg.getHead().getBizMsgID() + ".xml", "", "连接SFTP失败,请检查服务器和前置机的连接是否正常", ConstValue.SF.NO.Value, ConstValue.RECCODE.ZX_ZXDJ.Value,ProjectHelper.getpRroinstIDByActinstID(actinstID));
			//return xmlError;
		}
		if(!"1".equals(result) && result.indexOf("success") == -1){ //xml本地校验不通过时
			Map<String, String> xmlError = new HashMap<String, String>();
			xmlError.put("error", result);
			//return xmlError;
		}
		if(!StringUtils.isEmpty(result) && result.indexOf("success") > -1 && !names.containsKey("reccode")){
			names.put("reccode", result);
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
			for (int idjdy = 0; idjdy < djdys.size(); idjdy++) {
				BDCS_DJDY_GZ djdy = djdys.get(idjdy);
				ConstValue.BDCDYLX dylx = ConstValue.BDCDYLX.initFrom(djdy.getBDCDYLX());
				ConstValue.DJDYLY dyly = ConstValue.DJDYLY.initFrom(djdy.getLY());
				RealUnit bdcdy = UnitTools.loadUnit(dylx, dyly, djdy.getBDCDYID());
				Rights bdcql = RightsTools.loadRightsByDJDYID(ConstValue.DJDYLY.GZ, getXMBH(), djdy.getDJDYID());
				SubRights bdcfsql = RightsTools.loadSubRightsByRightsID(ConstValue.DJDYLY.GZ, bdcql.getId());
				List<RightsHolder> bdcqlrs = RightsHolderTools.loadRightsHolders(ConstValue.DJDYLY.GZ, djdy.getDJDYID(), getXMBH());
				MessageExport msg = super.getShareMsgTools().GetMsg(bdcdy, bdcql, bdcfsql, bdcqlrs, bljc, xmxx);
				super.getShareMsgTools().SendMsg(msg, idjdy + 1, djdy.getBDCDYLX(), xmxx);
			}
		}
	}
}
