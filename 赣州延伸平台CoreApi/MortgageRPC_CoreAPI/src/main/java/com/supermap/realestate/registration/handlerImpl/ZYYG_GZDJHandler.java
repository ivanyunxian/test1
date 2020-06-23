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
import com.supermap.realestate.registration.handler.DJHandler;
import com.supermap.realestate.registration.model.BDCS_C_GZ;
import com.supermap.realestate.registration.model.BDCS_C_XZ;
import com.supermap.realestate.registration.model.BDCS_DJDY_GZ;
import com.supermap.realestate.registration.model.BDCS_DJDY_XZ;
import com.supermap.realestate.registration.model.BDCS_DYBG;
import com.supermap.realestate.registration.model.BDCS_FSQL_GZ;
import com.supermap.realestate.registration.model.BDCS_FSQL_XZ;
import com.supermap.realestate.registration.model.BDCS_H_GZ;
import com.supermap.realestate.registration.model.BDCS_H_LS;
import com.supermap.realestate.registration.model.BDCS_H_XZ;
import com.supermap.realestate.registration.model.BDCS_LJZ_XZ;
import com.supermap.realestate.registration.model.BDCS_NYD_GZ;
import com.supermap.realestate.registration.model.BDCS_NYD_XZ;
import com.supermap.realestate.registration.model.BDCS_QDZR_GZ;
import com.supermap.realestate.registration.model.BDCS_QDZR_XZ;
import com.supermap.realestate.registration.model.BDCS_QLR_GZ;
import com.supermap.realestate.registration.model.BDCS_QLR_XZ;
import com.supermap.realestate.registration.model.BDCS_QL_GZ;
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
import com.supermap.realestate.registration.model.BDCS_ZS_XZ;
import com.supermap.realestate.registration.model.WFD_MAPPING;
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

/*
 1、国有建设用地使用权/房屋所有权转移预告更正登记
 */
/**
 * 
 * 更正转移预告登记处理类
 * @author liangc
 * @time 2018年2月6日 下午5时56分30秒
 */
public class ZYYG_GZDJHandler extends DJHandlerBase implements DJHandler {

	/**
	 * 
	 * @param info
	 */
	public ZYYG_GZDJHandler(ProjectInfo info) {
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
								bdcs_qlr_gz.setBDCQZH("");
								bdcs_qlr_gz.setBDCQZHXH("");
								getCommonDao().save(bdcs_qlr_gz);
								
								BDCS_SQR sqr=copyXZQLRtoSQR(bdcs_qlr_xz,SQRLB.JF);
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
			for (int iql = 0; iql < qls.size(); iql++) {
				super.removeQLXXFromXZByQLID(qls.get(iql).getLYQLID());
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
		super.SetSFDB();
		getCommonDao().flush();
		super.alterCachedXMXX();
		return true;
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
		Marshaller mashaller;
		Map<String, String> names = new HashMap<String, String>();
		try {
			CommonDao dao = super.getCommonDao();
			String xmbhHql = ProjectHelper.GetXMBHCondition(super.getXMBH());
			List<BDCS_DJDY_GZ> djdys = dao.getDataList(BDCS_DJDY_GZ.class, xmbhHql );
			List<BDCS_DJDY_GZ> bgqdjdys = dao.getDataList(BDCS_DJDY_GZ.class, xmbhHql + " and LY = '" + ConstValue.DJDYLY.XZ.Value + "' ");
			String msgName = "";
			if (djdys != null && djdys.size() > 0) {
				mashaller = JAXBContext.newInstance(Message.class).createMarshaller();
				Calendar calendar = Calendar.getInstance(); // 创建一个日历对象
				calendar.setTime(new Date()); // 用当前时间初始化日历时间
				String cyear = calendar.get(Calendar.YEAR) + "";
				String ywh = packageXml.GetYWLSHByYWH(this.getProject_id());
				BDCS_XMXX xmxx = dao.get(BDCS_XMXX.class, super.getXMBH());
				String result = "";
				for (int i = 0; i < djdys.size(); i++) {
					BDCS_DJDY_GZ djdy = djdys.get(i);
					BDCS_QL_GZ ql = super.getQL(djdy.getDJDYID());
					List<BDCS_QLR_GZ> qlrs = super.getQLRs(ql.getId());
					List<BDCS_SQR> sqrs = new ArrayList<BDCS_SQR>();
					ProjectServiceImpl serviceImpl = SuperSpringContext.getContext().getBean(ProjectServiceImpl.class);
					sqrs = serviceImpl.getSQRList(super.getXMBH());

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
							Message msg = exchangeFactory.createMessage(flag);

							super.fillHead(msg, i, ywh,zd.getBDCDYH(),zd.getQXDM(),ql.getLYQLID());

							BDCS_DYBG dybg = packageXml.getDYBG(zd.getId());
							msg.getHead().setParcelID(StringHelper.formatObject(zd.getZDDM()));
							msg.getHead().setEstateNum(StringHelper.formatObject(zd.getBDCDYH()));// 当前的不动产单元号
//							msg.getHead().setPreEstateNum(preEstateNum);// 上一首的不动产单元号
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
								
								BDCS_SHYQZD_GZ zd2 = null;
								if (null != dybg) {
									zd2 = new BDCS_SHYQZD_GZ();
									zd2.setId(dybg.getLBDCDYID());
//								zd.setId(dybg.getLBDCDYID());
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
							Message msg = exchangeFactory.createMessageByFWSYQ();
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
//							msg.getHead().setPreEstateNum(preEstateNum);
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
								ljz = packageXml.getKTTFWLJZ(ljz, ljz_gz,h);

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