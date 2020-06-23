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
import com.supermap.realestate.registration.dataExchange.syq.QLFQLTDSYQ;
import com.supermap.realestate.registration.dataExchange.utils.packageXml;
import com.supermap.realestate.registration.dataExchange.yydj.QLFQLYYDJ;
import com.supermap.realestate.registration.handler.DJHandler;
import com.supermap.realestate.registration.model.BDCS_C_GZ;
import com.supermap.realestate.registration.model.BDCS_DJDY_GZ;
import com.supermap.realestate.registration.model.BDCS_DJDY_LS;
import com.supermap.realestate.registration.model.BDCS_DJDY_XZ;
import com.supermap.realestate.registration.model.BDCS_DYBG;
import com.supermap.realestate.registration.model.BDCS_FSQL_GZ;
import com.supermap.realestate.registration.model.BDCS_FSQL_LS;
import com.supermap.realestate.registration.model.BDCS_FSQL_XZ;
import com.supermap.realestate.registration.model.BDCS_H_GZ;
import com.supermap.realestate.registration.model.BDCS_H_XZ;
import com.supermap.realestate.registration.model.BDCS_LJZ_XZ;
import com.supermap.realestate.registration.model.BDCS_NYD_GZ;
import com.supermap.realestate.registration.model.BDCS_NYD_XZ;
import com.supermap.realestate.registration.model.BDCS_QDZR_GZ;
import com.supermap.realestate.registration.model.BDCS_QDZR_LS;
import com.supermap.realestate.registration.model.BDCS_QDZR_XZ;
import com.supermap.realestate.registration.model.BDCS_QLR_GZ;
import com.supermap.realestate.registration.model.BDCS_QLR_LS;
import com.supermap.realestate.registration.model.BDCS_QLR_XZ;
import com.supermap.realestate.registration.model.BDCS_QL_GZ;
import com.supermap.realestate.registration.model.BDCS_QL_LS;
import com.supermap.realestate.registration.model.BDCS_QL_XZ;
import com.supermap.realestate.registration.model.BDCS_SHYQZD_GZ;
import com.supermap.realestate.registration.model.BDCS_SHYQZD_XZ;
import com.supermap.realestate.registration.model.BDCS_SQR;
import com.supermap.realestate.registration.model.BDCS_SYQZD_GZ;
import com.supermap.realestate.registration.model.BDCS_SYQZD_XZ;
import com.supermap.realestate.registration.model.BDCS_TDYT_GZ;
import com.supermap.realestate.registration.model.BDCS_TDYT_LS;
import com.supermap.realestate.registration.model.BDCS_TDYT_XZ;
import com.supermap.realestate.registration.model.BDCS_XMXX;
import com.supermap.realestate.registration.model.BDCS_YHYDZB_GZ;
import com.supermap.realestate.registration.model.BDCS_YHYDZB_XZ;
import com.supermap.realestate.registration.model.BDCS_YHZK_GZ;
import com.supermap.realestate.registration.model.BDCS_YHZK_XZ;
import com.supermap.realestate.registration.model.BDCS_ZH_GZ;
import com.supermap.realestate.registration.model.BDCS_ZRZ_XZ;
import com.supermap.realestate.registration.model.BDCS_ZS_GZ;
import com.supermap.realestate.registration.model.BDCS_ZS_LS;
import com.supermap.realestate.registration.model.BDCS_ZS_XZ;
import com.supermap.realestate.registration.model.WFD_MAPPING;
import com.supermap.realestate.registration.model.interfaces.House;
import com.supermap.realestate.registration.model.interfaces.RealUnit;
import com.supermap.realestate.registration.model.interfaces.RegisterUnit;
import com.supermap.realestate.registration.model.interfaces.Rights;
import com.supermap.realestate.registration.model.interfaces.RightsHolder;
import com.supermap.realestate.registration.model.interfaces.SubRights;
import com.supermap.realestate.registration.model.interfaces.UseLand;
import com.supermap.realestate.registration.model.xmlExportmodel.MessageExport;
import com.supermap.realestate.registration.service.impl.ProjectServiceImpl;
import com.supermap.realestate.registration.tools.RightsHolderTools;
import com.supermap.realestate.registration.tools.RightsTools;
import com.supermap.realestate.registration.tools.UnitTools;
import com.supermap.realestate.registration.util.ConstHelper;
import com.supermap.realestate.registration.util.ConstValue;
import com.supermap.realestate.registration.util.ConstValue.BDCDYLX;
import com.supermap.realestate.registration.util.ConstValue.DJDYLY;
import com.supermap.realestate.registration.util.ConstValue.DJLX;
import com.supermap.realestate.registration.util.ConstValue.QLLX;
import com.supermap.realestate.registration.util.ConstValue.SF;
import com.supermap.realestate.registration.util.ConstValue.SQRLB;
import com.supermap.realestate.registration.util.ConstValue.ZSBS;
import com.supermap.realestate.registration.util.ObjectHelper;
import com.supermap.realestate.registration.util.ProjectHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.realestate.registration.util.YwLogUtil;
import com.supermap.wisdombusiness.core.SuperHelper;
import com.supermap.wisdombusiness.core.SuperSpringContext;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import com.supermap.wisdombusiness.web.ResultMessage;


/**
 * 
 * 更正房地一体异议登记处理类
 * 
 *
 */
public class GZ_YYDJHandler_HouseAndLand extends DJHandlerBase implements DJHandler {

	/**
	 * 
	 * @param info
	 */
	public GZ_YYDJHandler_HouseAndLand(ProjectInfo info) {
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
					bdcs_ql_gz.setDJSJ(null);
					bdcs_ql_gz.setDBR("");
					bdcs_ql_gz.setBDCQZH("");
					bdcs_ql_gz.setZSBS(ZSBS.DYB.Value);// 默认单一版持证方式
					bdcs_ql_gz.setYWH(getProject_id());
					bdcs_ql_gz.setARCHIVES_BOOKNO("");
					bdcs_ql_gz.setARCHIVES_CLASSNO("");
					bdcs_ql_gz.setCASENUM("");
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
								String gzsqrid = (String)SuperHelper.GeneratePrimaryKey();
								BDCS_QLR_GZ bdcs_qlr_gz = ObjectHelper.copyQLR_XZToGZ(bdcs_qlr_xz);
								bdcs_qlr_gz.setId(gzqlrid);
								bdcs_qlr_gz.setQLID(gzqlid);
								bdcs_qlr_gz.setXMBH(getXMBH());
								bdcs_qlr_gz.setSQRID(gzsqrid);
								getCommonDao().save(bdcs_qlr_gz);
								
								BDCS_SQR sqr=copyXZQLRtoSQR(bdcs_qlr_xz,SQRLB.LHGXF);
								if(sqr!=null){
									sqr.setXMBH(getXMBH());
									sqr.setGLQLID(bdcs_ql_gz.getId());
									sqr.setId(gzsqrid);
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
											bdcs_zs_gz.setBDCQZH("");
											bdcs_zs_gz.setZSBH("");
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
	 * 写入登记簿
	 */
	@Override
	public boolean writeDJB() {


		List<BDCS_QL_GZ> qls = getCommonDao().getDataList(BDCS_QL_GZ.class,
				" XMBH='" + getXMBH() + "'");
		if (qls != null) {
			BDCDYLX dylx = ProjectHelper.GetBDCDYLX(getXMBH());
			List<String> zdbdcdyids = new ArrayList<String>();
			for (int iql = 0; iql < qls.size(); iql++) {
				super.removeQLXXFromXZByQLID(qls.get(iql).getLYQLID());
				RealUnit _srcunit = UnitTools.loadUnit(dylx, DJDYLY.XZ, qls.get(iql).getBDCDYID());
				if (_srcunit != null) {
					if(_srcunit instanceof House){
						House house = (House) _srcunit;
						if(!zdbdcdyids.contains(house.getZDBDCDYID())){
							zdbdcdyids.add(house.getZDBDCDYID());
							updateZD(qls.get(iql).getBDCDYID(), qls.get(iql).getDJDYID(),qls.get(iql).getLYQLID());
						}
					}
				}
			}
		}
		List<BDCS_DJDY_GZ> djdys = getCommonDao().getDataList(
				BDCS_DJDY_GZ.class, " XMBH='" + getXMBH() + "'");
		if (djdys != null) {
			for (int idjdy = 0; idjdy < djdys.size(); idjdy++) {
				BDCS_DJDY_GZ bdcs_djdy_gz = djdys.get(idjdy);
				String djdyid = bdcs_djdy_gz.getDJDYID();

				String xz_bdcdyid = "";
				List<BDCS_DJDY_XZ> xz_djdys = getCommonDao().getDataList(
						BDCS_DJDY_XZ.class, " DJDYID='" + djdyid + "'");
				if (xz_djdys != null && xz_djdys.size() > 0) {
					BDCS_DJDY_XZ bdcs_djdy_xz = xz_djdys.get(0);
					xz_bdcdyid = bdcs_djdy_xz.getBDCDYID();
					RebuildDYBG(bdcs_djdy_gz.getBDCDYID(),
							bdcs_djdy_gz.getDJDYID(),
							bdcs_djdy_xz.getBDCDYID(),
							bdcs_djdy_xz.getDJDYID(),
							bdcs_djdy_gz.getCreateTime(),
							bdcs_djdy_gz.getModifyTime());
				}
				super.CopyGZQLToXZAndLS(djdyid);
				super.CopyGZQLRToXZAndLS(djdyid);
				super.CopyGZQDZRToXZAndLS(djdyid);
				super.CopyGZZSToXZAndLS(djdyid);
				super.CopyGZDYToXZAndLSEx(bdcs_djdy_gz.getBDCDYID(), xz_bdcdyid);
				
			}
		}
		this.SetSFDB();
		getCommonDao().flush();
		super.alterCachedXMXX();
		return true;
	}
	
	public void updateZD(String bdcdyid_h, String djdyid_h,String lyqlid) {
		BDCDYLX dylx = ProjectHelper.GetBDCDYLX(getXMBH());
		RealUnit unit_h = UnitTools.loadUnit(dylx, DJDYLY.XZ, djdyid_h);
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
		List<BDCS_QL_GZ> lsql=getCommonDao().getDataList(BDCS_QL_GZ.class, "QLID='"+lyqlid+"'");
		if(lsql!=null&&lsql.size()>0){
			String lsxmbh = lsql.get(0).getXMBH();
			List<BDCS_QL_XZ> zd_qls=getCommonDao().getDataList(BDCS_QL_XZ.class, "BDCDYID='"+bdcdyid_zd+"' AND XMBH='"+lsxmbh+"'");
			if(zd_qls!=null&&zd_qls.size()>0){
				for (int iql = 0; iql < zd_qls.size(); iql++) {
					BDCS_QL_XZ bdcs_ql_xz = zd_qls.get(iql);
					super.removeQLXXFromXZByQLID(bdcs_ql_xz.getId());
				}
			}
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
			}else{
				String djdyid=super.getPrimaryKey();
				djdyid_zd.setBDCDYH(unit_zd.getBDCDYH());
				djdyid_zd.setBDCDYID(unit_zd.getId());
				djdyid_zd.setBDCDYLX(BDCDYLX.SHYQZD.Value);
				djdyid_zd.setDJDYID(djdyid);
				djdyid_zd.setLY(DJDYLY.XZ.Value);
				djdyid_zd.setXMBH(super.getXMBH());
				djdyid_zd.setGROUPID(1);
				BDCS_DJDY_LS zd_ls = ObjectHelper.copyDJDY_XZToLS(djdyid_zd);
				getCommonDao().save(zd_ls);
			}
			getCommonDao().save(djdyid_zd);
		}
		Rights right_h = RightsTools.loadRightsByDJDYID(DJDYLY.GZ, super.getXMBH(), djdyid_h);
		if (right_h != null) {
			SubRights subright_h = RightsTools.loadSubRights(DJDYLY.GZ, right_h.getFSQLID());
			String qlid_zd = SuperHelper.GeneratePrimaryKey();
			String fsqlid_zd = SuperHelper.GeneratePrimaryKey();
			//拷贝权利
			BDCS_QL_XZ ql_zd_xz = ObjectHelper.copyQL_GZToXZ((BDCS_QL_GZ) right_h);
			ql_zd_xz.setId(qlid_zd);
			ql_zd_xz.setFSQLID(fsqlid_zd);
			ql_zd_xz.setDJDYID(djdyid_zd.getDJDYID());
			ql_zd_xz.setBDCDYH(djdyid_zd.getBDCDYH());
			BDCS_QL_LS ql_zd_ls = ObjectHelper.copyQL_XZToLS(ql_zd_xz);
			getCommonDao().save(ql_zd_xz);
			getCommonDao().save(ql_zd_ls);
			BDCS_FSQL_XZ fsql_zd_xz = ObjectHelper.copyFSQL_GZToXZ((BDCS_FSQL_GZ) subright_h);
			fsql_zd_xz.setId(fsqlid_zd);
			fsql_zd_xz.setQLID(qlid_zd);
			fsql_zd_xz.setDJDYID(djdyid_zd.getDJDYID());
			fsql_zd_xz.setBDCDYH(djdyid_zd.getBDCDYH());
			BDCS_FSQL_LS fsql_zd_LS = ObjectHelper.copyFSQL_XZToLS(fsql_zd_xz);
			getCommonDao().save(fsql_zd_xz);
			getCommonDao().save(fsql_zd_LS);
			List<RightsHolder> qlrs_h=RightsHolderTools.loadRightsHolders(DJDYLY.GZ, right_h.getId());
			List<String> zsids = new ArrayList<String>();
			List<String> zdzsids = new ArrayList<String>();
			if(qlrs_h != null && qlrs_h.size()>0){
				for (RightsHolder rightsHolder : qlrs_h) {
					String qlrid=super.getPrimaryKey();
					BDCS_QLR_XZ qlr = ObjectHelper.copyQLR_GZToXZ((BDCS_QLR_GZ) rightsHolder);
					qlr.setId(qlrid);
					qlr.setQLID(qlid_zd);
					getCommonDao().save(qlr);
					BDCS_QLR_LS qlrls = ObjectHelper.copyQLR_XZToLS(qlr);
					getCommonDao().save(qlrls);
					List<BDCS_QDZR_GZ> qdzrs=getCommonDao().getDataList(BDCS_QDZR_GZ.class, "QLRID='"+rightsHolder.getId()+"'");
					String zsid = "";
					for (BDCS_QDZR_GZ bdcs_QDZR_GZ : qdzrs) {
						if(!zsids.contains(bdcs_QDZR_GZ.getZSID())){
							zsids.add(bdcs_QDZR_GZ.getZSID());
							zsid = SuperHelper.GeneratePrimaryKey();
							zdzsids.add(zsid);
						}
						BDCS_QDZR_XZ bdcs_QDZR_XZ = ObjectHelper.copyQDZR_GZToXZ(bdcs_QDZR_GZ);
						bdcs_QDZR_XZ.setId(SuperHelper.GeneratePrimaryKey().toString());
						bdcs_QDZR_XZ.setZSID(zsid);
						bdcs_QDZR_XZ.setXMBH(super.getXMBH());
						bdcs_QDZR_XZ.setDJDYID(djdyid_zd.getDJDYID());
						bdcs_QDZR_XZ.setFSQLID(fsqlid_zd);
						bdcs_QDZR_XZ.setQLID(qlid_zd);
						BDCS_QDZR_LS bdcs_QDZR_LS = ObjectHelper.copyQDZR_XZToLS(bdcs_QDZR_XZ);
						getCommonDao().save(bdcs_QDZR_XZ);
						getCommonDao().save(bdcs_QDZR_LS);
					}
				}
			}
			for (String zsid : zdzsids) {
				BDCS_ZS_XZ zs_zd_xz = new BDCS_ZS_XZ();
				zs_zd_xz.setXMBH(super.getXMBH());
				zs_zd_xz.setId(zsid);
				BDCS_ZS_LS zs_zd_ls = ObjectHelper.copyZS_XZToLS(zs_zd_xz);
				getCommonDao().save(zs_zd_xz);
				getCommonDao().save(zs_zd_ls);
			
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
	public Map<String,String> exportXML(String path, String actinstID) {
		Marshaller mashaller;
		Map<String, String> names = new HashMap<String, String>();
		try {
			CommonDao dao = this.getCommonDao();
			String xmbhHql = ProjectHelper.GetXMBHCondition(this.getXMBH());
			List<BDCS_DJDY_LS> djdys = dao.getDataList(BDCS_DJDY_LS.class, xmbhHql);
			if (djdys != null && djdys.size() > 0) {
				mashaller = JAXBContext.newInstance(Message.class).createMarshaller();
				Calendar calendar = Calendar.getInstance(); // 创建一个日历对象
				calendar.setTime(new Date()); // 用当前时间初始化日历时间
				String cyear = calendar.get(Calendar.YEAR) + "";
				BDCS_XMXX xmxx = dao.get(BDCS_XMXX.class, this.getXMBH());
				String result = "";
				for(int i = 0; i < djdys.size(); i ++){
					RegisterUnit djdy = djdys.get(i);
					String ywh = packageXml.GetYWLSHByYWH(this.getProject_id());
					String condition = MessageFormat.format("DJDYID=''{0}'' AND XMBH=''{1}'' ", djdy.getDJDYID(),djdy.getDJDYID());
					List<Rights> _rightss = RightsTools.loadRightsByCondition(DJDYLY.LS, condition);
					BDCS_QL_GZ ql = null;
					List<BDCS_QLR_GZ> qlrs = new ArrayList<BDCS_QLR_GZ>();
					for (Rights rights : _rightss) {
						
						ql = (BDCS_QL_GZ) rights;
						List<BDCS_QLR_LS> qlrsLS = getCommonDao().getDataList(BDCS_QLR_LS.class, "qlid = '"+ql.getId()+"'");
						for (BDCS_QLR_LS qlrls : qlrsLS) {
							RightsHolder qlr = qlrls;
							BDCS_QLR_GZ qlrgz = (BDCS_QLR_GZ) qlr;
							qlrs.add(qlrgz);
						}
						if(qlrs != null){
							break;
						}
					}
					List<BDCS_SQR> sqrs = new ArrayList<BDCS_SQR>();
					ProjectServiceImpl serviceImpl = SuperSpringContext.getContext().getBean(ProjectServiceImpl.class);
					sqrs = serviceImpl.getSQRList(super.getXMBH());
					
					BDCS_FSQL_GZ fsql = new BDCS_FSQL_GZ();
					if(ql != null && !StringUtils.isEmpty(ql.getFSQLID())){
						BDCS_FSQL_LS fsqlls = dao.get(BDCS_FSQL_LS.class, ql.getFSQLID());
						SubRights fsqljk = fsqlls;
						fsql = (BDCS_FSQL_GZ) fsqljk;
					}
					
					Message msg = exchangeFactory.createMessageByYYDJ();
					
					msg.getHead().setRecType("8000101");
					if (BDCDYLX.SHYQZD.Value.equals(djdy.getBDCDYLX())) { 
						try {
							BDCS_SHYQZD_XZ zd = dao.get(BDCS_SHYQZD_XZ.class, djdy.getBDCDYID());
							UseLand land = zd;
							super.fillHead(msg, i, ywh,zd.getBDCDYH(),zd.getQXDM(),ql.getLYQLID());
							msg.getHead().setParcelID(StringHelper.formatObject(zd.getZDDM()));
							msg.getHead().setEstateNum(StringHelper.formatObject(zd.getBDCDYH()));
//							msg.getHead().setPreEstateNum(StringHelper.formatObject(zd.getBDCDYH()));
							if(zd != null && !StringUtils.isEmpty(zd.getQXDM())){
								msg.getHead().setAreaCode(zd.getQXDM());
							}
							if (djdy != null) {

								List<ZTTGYQLR> zttqlr = msg.getData().getGYQLR();
								zttqlr = packageXml.getZTTGYQLRs(zttqlr, qlrs, zd, ql,null, null, null, null);
								msg.getData().setGYQLR(zttqlr);
								
								QLFQLYYDJ yydj = msg.getData().getQLFQLYYDJ();
								yydj = packageXml.getQLFQLYYDJ(yydj, ql, fsql, ywh, null, land);

								DJTDJSLSQ sq = msg.getData().getDJSLSQ();
								sq = packageXml.getDJTDJSLSQ(sq, ywh, land, ql, xmxx,null, xmxx.getSLSJ(), null, null, null);
								
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
								List<DJFDJSQR> djsqrs = msg.getData().getDJSQR();
								djsqrs = packageXml.getDJSQRs(djsqrs, sqrs, zd.getYSDM(), ywh, zd.getBDCDYH());
								msg.getData().setDJSQR(djsqrs);
								
								FJF100 fj = msg.getData().getFJF100();
								fj = packageXml.getFJF(fj);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					BDCS_H_XZ h = null;
					if(BDCDYLX.H.Value.equals(djdy.getBDCDYLX())){ //房屋所有权
						try {
							h = dao.get(BDCS_H_XZ.class, djdy.getBDCDYID());
							if(h != null){
								BDCS_SHYQZD_XZ zd = dao.get(BDCS_SHYQZD_XZ.class, h.getZDBDCDYID());
								h.setZDDM(zd.getZDDM());
							}
							super.fillHead(msg, i, ywh,h.getBDCDYH(),h.getQXDM(),ql.getLYQLID());
							msg.getHead().setParcelID(StringHelper.formatObject(h.getZDDM()));
							msg.getHead().setEstateNum(StringHelper.formatObject(h.getBDCDYH()));
//							msg.getHead().setPreEstateNum(StringHelper.formatObject(h.getBDCDYH()));
							if(h != null && !StringUtils.isEmpty(h.getQXDM())){
								msg.getHead().setAreaCode(h.getQXDM());
							}
							if (djdy != null) {
								
								List<ZTTGYQLR> zttqlr = msg.getData().getGYQLR();
								zttqlr = packageXml.getZTTGYQLRs(zttqlr, qlrs, null, ql,h, null, null, null);
								msg.getData().setGYQLR(zttqlr);
									
								QLFQLYYDJ yydj = msg.getData().getQLFQLYYDJ();
								yydj = packageXml.getQLFQLYYDJ(yydj, ql,fsql, ywh, h, null);
								
								DJTDJSLSQ sq = msg.getData().getDJSLSQ();
								sq = packageXml.getDJTDJSLSQ(sq, ywh, null, ql, xmxx, h ,xmxx.getSLSJ(), null, null, null);
								
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
								msg.getData().setDJSQR(djsqrs);
								
								FJF100 fj = msg.getData().getFJF100();
								fj = packageXml.getFJF(fj);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					mashaller.marshal(msg, new File(path + "Biz" + msg.getHead().getBizMsgID()+ ".xml"));
					result = uploadFile(path + "Biz" + msg.getHead().getBizMsgID()+ ".xml", ConstValue.RECCODE.YY_ZXDJ.Value,actinstID,djdy.getDJDYID(),ql.getId());
					names.put(djdy.getDJDYID(),msg.getHead().getBizMsgID()+ ".xml");
					
					if(null == result){
						Map<String, String> xmlError = new HashMap<String, String>();
						xmlError.put("error", "连接SFTP失败,请检查服务器和前置机的连接是否正常");
						YwLogUtil.addSjsbResult("Biz" + msg.getHead().getBizMsgID()+ ".xml", "", "连接SFTP失败,请检查服务器和前置机的连接是否正常", ConstValue.SF.NO.Value, ConstValue.RECCODE.YY_ZXDJ.Value,ProjectHelper.getpRroinstIDByActinstID(actinstID));
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