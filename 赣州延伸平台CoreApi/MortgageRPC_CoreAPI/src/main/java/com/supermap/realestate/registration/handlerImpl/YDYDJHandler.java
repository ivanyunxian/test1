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
import com.supermap.realestate.registration.dataExchange.ygdj.QLFQLYGDJ;
import com.supermap.realestate.registration.handler.DJHandler;
import com.supermap.realestate.registration.model.BDCS_DJDY_GZ;
import com.supermap.realestate.registration.model.BDCS_FSQL_GZ;
import com.supermap.realestate.registration.model.BDCS_H_XZY;
import com.supermap.realestate.registration.model.BDCS_QLR_GZ;
import com.supermap.realestate.registration.model.BDCS_QLR_LS;
import com.supermap.realestate.registration.model.BDCS_QLR_XZ;
import com.supermap.realestate.registration.model.BDCS_QL_GZ;
import com.supermap.realestate.registration.model.BDCS_SHYQZD_XZ;
import com.supermap.realestate.registration.model.BDCS_SQR;
import com.supermap.realestate.registration.model.BDCS_XMXX;
import com.supermap.realestate.registration.model.YC_SC_H_XZ;
import com.supermap.realestate.registration.model.interfaces.RealUnit;
import com.supermap.realestate.registration.model.interfaces.Rights;
import com.supermap.realestate.registration.model.interfaces.RightsHolder;
import com.supermap.realestate.registration.model.interfaces.SubRights;
import com.supermap.realestate.registration.model.xmlExportmodel.MessageExport;
import com.supermap.realestate.registration.service.impl.ProjectServiceImpl;
import com.supermap.realestate.registration.tools.RightsHolderTools;
import com.supermap.realestate.registration.tools.RightsTools;
import com.supermap.realestate.registration.tools.UnitTools;
import com.supermap.realestate.registration.util.ConstValue;
import com.supermap.realestate.registration.util.ConstValue.BDCDYLX;
import com.supermap.realestate.registration.util.ConstValue.CZFS;
import com.supermap.realestate.registration.util.ConstValue.DJDYLY;
import com.supermap.realestate.registration.util.ConstValue.QLLX;
import com.supermap.realestate.registration.util.ConstValue.YGDJLX;
import com.supermap.realestate.registration.util.ObjectHelper;
import com.supermap.realestate.registration.util.ProjectHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.realestate.registration.util.YwLogUtil;
import com.supermap.wisdombusiness.core.SuperSpringContext;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import com.supermap.wisdombusiness.web.ResultMessage;

/*
 1、商品房预告登记+商品房抵押预告登记（待测）
 */
/**
 * 
* 预告登记+预抵押登记操作类
* @ClassName: YDYDJHandler 
* @author liushufeng 
* @date 2015年9月8日 下午10:41:40
 */
public class YDYDJHandler extends DJHandlerBase implements DJHandler {

	/**
	 * 构造函数
	 * @param inf_o
	 */
	public YDYDJHandler(ProjectInfo info) {
		super(info);
	}

	/**
	 * 添加不动产单元
	 */
	@Override
	public boolean addBDCDY(String bdcdyid) {
		boolean bsuccess = true;
		if (!StringHelper.isEmpty(bdcdyid)) {
			String[] bdcdyids = bdcdyid.split(",");
			if (bdcdyids != null && bdcdyids.length > 0) {
				for (String id : bdcdyids) {
					if (bsuccess) {
						bsuccess = addbdcdy(id);
					}
				}
			}
		}
		return bsuccess;
	}

	private boolean addbdcdy(String bdcdyid) {
		boolean bsuccess = false;
		CommonDao dao = getCommonDao();
		ResultMessage msg = new ResultMessage();
		RealUnit _srcUnit = UnitTools.loadUnit(this.getBdcdylx(), DJDYLY.XZ, bdcdyid);
		if (_srcUnit == null) {
			msg.setSuccess("false");
			msg.setMsg("找不到单元");
			return false;
		}
		if (_srcUnit != null) {
			BDCS_DJDY_GZ djdy = super.createDJDYfromXZ(bdcdyid);
			if (djdy != null) {
				// 抵押权
				BDCS_QL_GZ dyql = super.createQL(djdy, _srcUnit);
				
				dyql.setCZFS(CZFS.GTCZ.Value);
				// 抵押权附属权利
				BDCS_FSQL_GZ dyfsql = super.createFSQL(djdy.getDJDYID());

				dyql.setFSQLID(dyfsql.getId());
				dyql.setCZFS(CZFS.GTCZ.Value);//设置持证方式为共同持证。
				
				dyfsql.setQLID(dyql.getId());
				// 设置抵押物类型
				dyfsql.setDYWLX(getDYBDCLXfromBDCDYLX(getBdcdylx()));
				dyfsql.setDYBDCLX(getDYBDCLXfromBDCDYLX(getBdcdylx()));
				int dysw=RightsTools.getMaxMortgageSWS(djdy.getDJDYID());
				dyfsql.setDYSW(dysw+1);
				if ("23".equals(this.getQllx().Value)) {
					dyfsql.setYGDJZL(YGDJLX.YSSPFDYQYGDJ.Value.toString());
				} else {
				}

				// 把附属权利里边的抵押人和抵押不动产类型写上
				String qllxarray = "('1','2','3','4','5','6','7','8','9','10','11','12','13','14','15','16','17','18')";
				String hql = "QLID IN(SELECT id FROM BDCS_QL_XZ WHERE  DJDYID='" + djdy.getDJDYID() + "' AND QLLX IN " + qllxarray + ")";

				List<BDCS_QLR_XZ> list = dao.getDataList(BDCS_QLR_XZ.class, hql);
				if (list != null && list.size() > 0) {
					String qlrnames = "";
					for (int i = 0; i < list.size(); i++) {
						qlrnames += list.get(i).getQLRMC() + ",";
						//过滤重复，刘树峰2015年12月24日晚23点
						BDCS_QLR_XZ qlr = list.get(i);
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
							}
						}
						
						// 判断申请人是否已经添加过，如果添加过，就不再添加
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
							sqr.setSQRLB("2");
							sqr.setSQRLX(qlr.getQLRLX());
							sqr.setDZYJ(qlr.getDZYJ());
							sqr.setLXDH(qlr.getDH());
							sqr.setZJH(qlr.getZJH());
							sqr.setZJLX(qlr.getZJZL());
							sqr.setTXDZ(qlr.getDZ());
							sqr.setYZBM(qlr.getYB());
							sqr.setXMBH(getXMBH());
							sqr.setId(SQRID);
							sqr.setGLQLID(dyql.getId());
							dao.save(sqr);
						}
					}
					if (!StringUtils.isEmpty(qlrnames)) {
						qlrnames = qlrnames.substring(0, qlrnames.length() - 1);
						dyfsql.setDYR(qlrnames);
						dyfsql.setYWR(qlrnames);
					}
				}
				// 设置抵押物类型
				dyfsql.setDYWLX(getDYBDCLXfromBDCDYLX(getBdcdylx()));
				dyfsql.setZJJZWZL(_srcUnit.getZL());

				// 保存
				dao.update(_srcUnit);
				dao.save(djdy);
				dao.save(dyql);
				dao.save(dyfsql);
			}
			dao.flush();
			bsuccess = true;
		}
		return bsuccess;
	}

	/**
	 * 写入登记薄
	 */
	@Override
	public boolean writeDJB() {
		
		/*if (super.isCForCFING()) {
			return false;
		}*/
		
		String strSqlXMBH = ProjectHelper.GetXMBHCondition(getXMBH());
		CommonDao dao = getCommonDao();
		List<BDCS_DJDY_GZ> djdys = dao.getDataList(BDCS_DJDY_GZ.class, strSqlXMBH);
		for (BDCS_DJDY_GZ bdcs_djdy_gz : djdys) {
			String djdyid = bdcs_djdy_gz.getDJDYID();
			// 拷贝相应的权利信息
			super.CopyGZQLToXZAndLS(djdyid);
			super.CopyGZQDZRToXZAndLS(djdyid);
			super.CopyGZZSToXZAndLS(djdyid);
			super.CopyGZDJDYToXZAndLS(bdcs_djdy_gz.getId());
			// 拷贝权利人
			StringBuilder builder = new StringBuilder();
			builder.append(" QLID IN (");
			builder.append("select id FROM BDCS_QL_GZ WHERE DJDYID ='");
			builder.append(djdyid).append("' and ");
			builder.append(strSqlXMBH);
			// builder.append(" and QLLX='").append(getQllx().Value).append("'");
			builder.append(")");
			String strQueryQLRID = builder.toString();
			List<BDCS_QLR_GZ> qlrs = dao.getDataList(BDCS_QLR_GZ.class, strQueryQLRID);
			if (qlrs != null && qlrs.size() > 0) {
				for (int iqlr = 0; iqlr < qlrs.size(); iqlr++) {
					BDCS_QLR_GZ bdcs_qlr_gz = qlrs.get(iqlr);
					BDCS_QLR_XZ bdcs_qlr_xz = ObjectHelper.copyQLR_GZToXZ(bdcs_qlr_gz);
					dao.save(bdcs_qlr_xz);
					BDCS_QLR_LS bdcs_qlr_ls = ObjectHelper.copyQLR_XZToLS(bdcs_qlr_xz);
					dao.save(bdcs_qlr_ls);
				}
			}

			// 拷贝关系表信息YC_SC_H_XZ
			List<YC_SC_H_XZ> lst = dao.getDataList(YC_SC_H_XZ.class, "YCBDCDYID ='" + bdcs_djdy_gz.getBDCDYID() + "'");
			for (YC_SC_H_XZ yc_sc_h_xz : lst) {
				// 拷贝预测户
				super.CopyYXZHToAndLS(yc_sc_h_xz.getYCBDCDYID());
			}
			RebuildDYBG("", "", bdcs_djdy_gz.getBDCDYID(), bdcs_djdy_gz.getDJDYID(), bdcs_djdy_gz.getCreateTime(), bdcs_djdy_gz.getModifyTime());
			BDCDYLX dylx = ProjectHelper.GetBDCDYLX(getXMBH());
			// 更新单元抵押状态
			SetDYDYZT(bdcs_djdy_gz.getBDCDYID(), dylx, "0");
		}
		this.SetSFDB();
		dao.flush();
		super.alterCachedXMXX();
		return true;
	}

	/**
	 * 移除登记产单元
	 */
	@Override
	public boolean removeBDCDY(String qlid) {
		// 先删除登记单元
		BDCS_QL_GZ bdcs_ql_gz = getCommonDao().get(BDCS_QL_GZ.class, qlid);
		if (bdcs_ql_gz != null) {
			StringBuilder builderDJDY = new StringBuilder();
			builderDJDY.append(" DJDYID='");
			builderDJDY.append(bdcs_ql_gz.getDJDYID());
			builderDJDY.append("' AND XMBH='");
			builderDJDY.append(super.getXMBH());
			builderDJDY.append("'");
			// 获取登记单元集合
			List<BDCS_DJDY_GZ> djdys = getCommonDao().getDataList(BDCS_DJDY_GZ.class, builderDJDY.toString());
			if (djdys != null && djdys.size() > 0) {
				BDCS_DJDY_GZ bdcs_djdy_gz = djdys.get(0);
				// 只有当登记单元关联的权利个数为1的时候才移除单元（不支持批量移除）
				List<BDCS_QL_GZ> qls = getCommonDao().getDataList(BDCS_QL_GZ.class, builderDJDY.toString());
				if (qls != null && qls.size() == 1) {
					getCommonDao().deleteEntity(bdcs_djdy_gz);
				}
			}
		}

		// 删除权利关联申请人
		RemoveSQRByGLQLID(qlid, getXMBH());
		RightsTools.deleteRightsAll(DJDYLY.GZ, qlid);

		getCommonDao().flush();
		return true;
	}
	
	/**
	 * 根据关联权利ID和项目编号删除权利关联申请人
	 * @Author：俞学斌
	 * @param glqlid
	 *            登记单元ID
	 * @param xmbh
	 *            项目编号
	 */
	private void RemoveSQRByGLQLID(String glqlid, String xmbh) {
		StringBuilder builderSQR = new StringBuilder();
		builderSQR.append("GLQLID='");
		builderSQR.append(glqlid);
		builderSQR.append("' AND XMBH='");
		builderSQR.append(xmbh);
		builderSQR.append("'");
		List<BDCS_SQR> sqrs = getCommonDao().getDataList(BDCS_SQR.class, builderSQR.toString());
		if (sqrs == null || sqrs.size() <= 0) {
			return;
		}
		for (int isqr = 0; isqr < sqrs.size(); isqr++) {
			BDCS_SQR sqr = sqrs.get(isqr);
			getCommonDao().deleteEntity(sqr);
		}
	}

	/**
	 * 获取不动产单元列表
	 */
	@Override
	public List<UnitTree> getUnitTree() {
		return super.getUnitList();
	}

	/**
	 * 根据申请人ID数组添加申请人
	 */
	@Override
	public void addQLRbySQRArray(String qlid, Object[] sqrids) {
		super.addQLRbySQRs(qlid, sqrids);
//		CommonDao dao = getCommonDao();
//		String xmbh = getXMBH();
//		DJLX djlx = getDjlx();
//		BDCS_FSQL_GZ fsql_GZ = new BDCS_FSQL_GZ();
//		String dyrStr = ""; // 抵押人
//		if (sqrids != null && sqrids.length > 0) {
//			BDCS_QL_GZ bdcs_ql_gz = dao.get(BDCS_QL_GZ.class, qlid);
//			List<BDCS_QLR_GZ> qlrs = dao.getDataList(BDCS_QLR_GZ.class, " XMBH='" + xmbh + "' AND QLID='" + qlid + "'");
//			List<BDCS_FSQL_GZ> fsqlList = dao.getDataList(BDCS_FSQL_GZ.class, " XMBH='" + xmbh + "'");
//			// 添加权利人
//			for (Object sqrid : sqrids) {
//				String qlrsql = MessageFormat.format(" XMBH=''{0}'' AND QLID=''{1}'' AND SQRID=''{2}''", xmbh, qlid, sqrid.toString());
//				List<BDCS_QLR_GZ> list = dao.getDataList(BDCS_QLR_GZ.class, qlrsql);
//				// 判断该申请人是否已经在权利人中存在
//				if (list == null || list.size() <= 0) {
//					BDCS_SQR bdcs_sqr = dao.get(BDCS_SQR.class, sqrid.toString());
//					if (bdcs_sqr != null) {
//						BDCS_QLR_GZ bdcs_qlr_gz = ObjectHelper.CopySQRtoQLR(bdcs_sqr);
//						bdcs_qlr_gz.setQLID(qlid);
//						bdcs_qlr_gz.setXMBH(xmbh);
//						bdcs_qlr_gz.setSQRID(bdcs_qlr_gz.getId());
//						dao.save(bdcs_qlr_gz);
//						qlrs.add(bdcs_qlr_gz);
//					}
//				}
//			}
//			// 根据持证方式处理证书
//			String zsid = getPrimaryKey();
//			// 删除证书
//			String hqlZS = MessageFormat.format(" XMBH=''{0}'' AND id IN (SELECT B.ZSID FROM BDCS_QDZR_GZ B WHERE B.XMBH=''{0}'' AND B.DJDYID=''{1}'' and B.QLID=''{2}'')", xmbh,
//					bdcs_ql_gz.getDJDYID(), qlid);
//			dao.deleteEntitysByHql(BDCS_ZS_GZ.class, hqlZS);
//			// 删除关系表
//			String sqlQDZR = MessageFormat.format(" XMBH=''{0}'' AND DJDYID=''{1}'' and QLID=''{2}''", xmbh, bdcs_ql_gz.getDJDYID(), qlid);
//			dao.deleteEntitysByHql(BDCS_QDZR_GZ.class, sqlQDZR);
//			// 循环权利人，生成关系表
//			for (int i = 0; i < qlrs.size(); i++) {
//				BDCS_QLR_GZ qlr = qlrs.get(i);
//				BDCS_QDZR_GZ bdcs_qdzr_gz = new BDCS_QDZR_GZ();
//				bdcs_qdzr_gz.setBDCDYH(bdcs_ql_gz.getBDCDYH());
//				bdcs_qdzr_gz.setQLRID(qlr.getId());
//				bdcs_qdzr_gz.setDJDYID(bdcs_ql_gz.getDJDYID());
//				bdcs_qdzr_gz.setXMBH(xmbh);
//				bdcs_qdzr_gz.setQLID(qlid);
//				bdcs_qdzr_gz.setFSQLID(bdcs_ql_gz.getFSQLID());
//				if (bdcs_ql_gz.getCZFS().equals(CZFS.FBCZ.Value)) {
//					BDCS_ZS_GZ bdcs_zs_gz = new BDCS_ZS_GZ();
//					bdcs_zs_gz.setXMBH(xmbh);
//					dao.save(bdcs_zs_gz);
//					bdcs_qdzr_gz.setZSID(bdcs_zs_gz.getId());
//				} else {
//					bdcs_qdzr_gz.setZSID(zsid);
//				}
//				// 设置抵押人
//				if (qlrs.size() - 1 == i) {
//					dyrStr += qlr.getQLRMC();
//				} else {
//					dyrStr += qlr.getQLRMC() + ",";
//				}
//				dao.save(bdcs_qdzr_gz);
//			}
//			if (CZFS.GTCZ.Value.equals(bdcs_ql_gz.getCZFS())) {
//				BDCS_ZS_GZ bdcs_zs_gz = new BDCS_ZS_GZ();
//				bdcs_zs_gz.setId(zsid);
//				bdcs_zs_gz.setXMBH(xmbh);
//				dao.save(bdcs_zs_gz);
//			}
//			// 设置抵押人
//			for (int i = 0; i < fsqlList.size(); i++) {
//				fsql_GZ = fsqlList.get(i);
//				fsql_GZ.setDYR(dyrStr);
//				dao.saveOrUpdate(fsql_GZ);
//			}
//			// 预告登记需要加上义务人
//			if (djlx.Value.equals(DJLX.YGDJ.Value)) {
//				if (fsqlList.size() > 0) {
//					StringBuilder ywr = new StringBuilder();
//					StringBuilder ywrzjlx = new StringBuilder();
//					StringBuilder ywrzjh = new StringBuilder();
//					List<BDCS_SQR> ywrlist = dao.getDataList(BDCS_SQR.class, " XMBH='" + xmbh + "' AND SQRLB='2'");
//					for (int i = 0; i < ywrlist.size(); i++) {
//						if (i != 0) {
//							ywr.append("/");
//							ywrzjlx.append("/");
//							ywrzjh.append("/");
//						}
//						ywr.append(ywrlist.get(i).getSQRXM());
//						ywrzjlx.append(ywrlist.get(i).getZJLX());
//						ywrzjh.append(ywrlist.get(i).getZJH());
//					}
//					for (int i = 0; i < fsqlList.size(); i++) {
//						fsql_GZ = fsqlList.get(i);
//						fsql_GZ.setYWR(ywr.toString());
//						fsql_GZ.setYWRZJZL(ywrzjlx.toString());
//						fsql_GZ.setYWRZJH(ywrzjh.toString());
//						dao.saveOrUpdate(fsql_GZ);
//					}
//				}
//			}
//			dao.flush();
//		}
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
	
	
	/**
	 *  数据上报功能块(预抵押)
	 *  @author weilb 
	 *  @param String path, 
	 *  @param String actinstID,
	 *  @return Map<String, String> names
	 */
	@Override
	public Map<String, String> exportXML(String path, String actinstID) {
		Marshaller mashaller;
		Map<String, String> names = new HashMap<String,String>();
		try {
			CommonDao dao = this.getCommonDao();
			String xmbhHql = ProjectHelper.GetXMBHCondition(this.getXMBH());
			List<BDCS_DJDY_GZ> djdys = dao.getDataList(BDCS_DJDY_GZ.class, xmbhHql);
			if (djdys != null && djdys.size() > 0) {
				mashaller = JAXBContext.newInstance(Message.class).createMarshaller();
				Calendar calendar = Calendar.getInstance(); 
				calendar.setTime(new Date()); 
				String cyear = calendar.get(Calendar.YEAR) + "";
				String result = "";
				for(int i = 0; i < djdys.size(); i ++){
					BDCS_DJDY_GZ djdy = djdys.get(i);
					String ywh = packageXml.GetYWLSHByYWH(this.getProject_id());
					BDCS_QL_GZ ql = this.getQL(djdy.getDJDYID());
					BDCS_FSQL_GZ fsql = new BDCS_FSQL_GZ();
					if(ql != null && !StringUtils.isEmpty(ql.getFSQLID())){
						fsql = dao.get(BDCS_FSQL_GZ.class, ql.getFSQLID());
					}
					List<BDCS_QLR_GZ> qlrs = this.getQLRs(ql.getId());
					List<BDCS_SQR> sqrs = new ArrayList<BDCS_SQR>();
					ProjectServiceImpl serviceImpl = SuperSpringContext.getContext().getBean(ProjectServiceImpl.class);
					sqrs = serviceImpl.getSQRList(super.getXMBH());
					
					BDCS_XMXX xmxx = dao.get(BDCS_XMXX.class, super.getXMBH());
					Message msg = exchangeFactory.createMessageByYGDJ();
					
					BDCS_H_XZY h = null;
					BDCS_SHYQZD_XZ zd =null;
					if(QLLX.DIYQ.Value.equals(this.getQllx().Value)){ 
						try {
							h = dao.get(BDCS_H_XZY.class, djdy.getBDCDYID());
							if(h != null){
								zd = dao.get(BDCS_SHYQZD_XZ.class, h.getZDBDCDYID());
								if(zd != null){
									h.setZDDM(zd.getZDDM());
								}
							}
							super.fillHead(msg, i, ywh,h.getBDCDYH(),h.getQXDM(),ql.getLYQLID());
							msg.getHead().setParcelID(StringHelper.formatObject(h.getZDDM()));
							msg.getHead().setEstateNum(StringHelper.formatObject(h.getBDCDYH()));
//							msg.getHead().setPreEstateNum(StringHelper.formatObject(h.getBDCDYH()));
							if(h != null && !StringUtils.isEmpty(h.getQXDM())){
								msg.getHead().setAreaCode(h.getQXDM());
							}

							if (djdy != null) {
							
								//抵押权常规登记报文
//								QLFQLDYAQ dyaq = msg.getData().getQLFQLDYAQ();
//								dyaq = packageXml.getQLFQLDYAQ(dyaq, zd, ql, fsql, ywh, h);
//								
//								QLFQLDYAQ dyaqs =  msg.getData().getQLFQLDYAQ();
//								dyaqs = packageXml.getQLFQLDYAQEx(dyaqs, ql, fsql, ywh, h);
								
								QLFQLYGDJ ygdj = msg.getData().getQLFQLYGDJ();
								ygdj = packageXml.getQLFQLYGDJ(ygdj, h, ql,fsql, ywh);
								
								DJTDJSLSQ sq = msg.getData().getDJSLSQ();
								sq = packageXml.getDJTDJSLSQ(sq, ywh, null, ql, xmxx, h, xmxx.getSLSJ(), null, null, null);

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
								FJF100 fj = msg.getData().getFJF100();
								fj = packageXml.getFJF(fj);
								
								List<ZTTGYQLR> zttqlr = msg.getData().getGYQLR();
								zttqlr = packageXml.getZTTGYQLRs(zttqlr, qlrs, null, ql, h, null, null, null);
								msg.getData().setGYQLR(zttqlr);
								
								List<DJFDJSQR> djsqrs = msg.getData().getDJSQR();
								djsqrs = packageXml.getDJSQRs(djsqrs, sqrs, h.getYSDM(), ywh, h.getBDCDYH());
								msg.getData().setDJSQR(djsqrs);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					mashaller.marshal(msg, new File(path + "Biz" + msg.getHead().getBizMsgID()+ ".xml"));
					result = uploadFile(path + "Biz" + msg.getHead().getBizMsgID()+ ".xml",ConstValue.RECCODE.YG_ZXDJ.Value,actinstID,djdy.getDJDYID(),ql.getId());
					names.put(djdy.getDJDYID(),msg.getHead().getBizMsgID()+ ".xml");
					if(result.equals("")|| result==null){
						Map<String, String> xmlError = new HashMap<String, String>();
						xmlError.put("error", "连接SFTP失败,请检查服务器和前置机的连接是否正常");
						YwLogUtil.addSjsbResult("Biz" + msg.getHead().getBizMsgID() + ".xml", "", "连接SFTP失败,请检查服务器和前置机的连接是否正常", ConstValue.SF.NO.Value, ConstValue.RECCODE.YG_ZXDJ.Value,ProjectHelper.getpRroinstIDByActinstID(actinstID));
						return xmlError;
					}
					if(!"1".equals(result) && result.indexOf("success") == -1){ //xml本地校验不通过
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
	 * 获得抵押物类型
	 * @作者 diaoliwei
	 * @创建时间 2015年7月25日下午10:18:25
	 * @param bdcdylx
	 * @return
	 */
	private String getDYBDCLXfromBDCDYLX(BDCDYLX bdcdylx) {
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
}
