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
import com.supermap.realestate.registration.model.BDCS_C_XZ;
import com.supermap.realestate.registration.model.BDCS_DJDY_GZ;
import com.supermap.realestate.registration.model.BDCS_DJDY_XZ;
import com.supermap.realestate.registration.model.BDCS_FSQL_GZ;
import com.supermap.realestate.registration.model.BDCS_FSQL_LS;
import com.supermap.realestate.registration.model.BDCS_FSQL_XZ;
import com.supermap.realestate.registration.model.BDCS_H_XZ;
import com.supermap.realestate.registration.model.BDCS_LJZ_XZ;
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
import com.supermap.realestate.registration.model.BDCS_SYQZD_XZ;
import com.supermap.realestate.registration.model.BDCS_XMXX;
import com.supermap.realestate.registration.model.BDCS_ZRZ_XZ;
import com.supermap.realestate.registration.model.BDCS_ZS_LS;
import com.supermap.realestate.registration.model.BDCS_ZS_XZ;
import com.supermap.realestate.registration.model.YC_SC_H_XZ;
import com.supermap.realestate.registration.model.interfaces.House;
import com.supermap.realestate.registration.model.interfaces.RealUnit;
import com.supermap.realestate.registration.model.interfaces.Rights;
import com.supermap.realestate.registration.model.interfaces.RightsHolder;
import com.supermap.realestate.registration.model.interfaces.SubRights;
import com.supermap.realestate.registration.model.xmlExportmodel.MessageExport;
import com.supermap.realestate.registration.service.impl.ProjectServiceImpl;
import com.supermap.realestate.registration.tools.RightsHolderTools;
import com.supermap.realestate.registration.tools.RightsTools;
import com.supermap.realestate.registration.tools.UnitTools;
import com.supermap.realestate.registration.util.ConfigHelper;
import com.supermap.realestate.registration.util.ConstValue;
import com.supermap.realestate.registration.util.Global;
import com.supermap.realestate.registration.util.ProjectHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.realestate.registration.util.YwLogUtil;
import com.supermap.realestate.registration.util.ConstValue.BDCDYLX;
import com.supermap.realestate.registration.util.ConstValue.DJDYLY;
import com.supermap.realestate.registration.util.ConstValue.DJLX;
import com.supermap.realestate.registration.util.ConstValue.QLLX;
import com.supermap.realestate.registration.util.ConstValue.SQRLB;
import com.supermap.wisdombusiness.core.SuperHelper;
import com.supermap.wisdombusiness.core.SuperSpringContext;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;

/**
 * 强制过户，房地一体转移
 * 
 * @author zhaomengfan
 * @date 2017-04-10 09:36:38
 */
public class QZZYDJHandler_HouseAndLand extends DJHandlerBase implements DJHandler {

	public QZZYDJHandler_HouseAndLand(ProjectInfo info) {
		super(info);
	}

	@Override
	public boolean addBDCDY(String bdcdyid) {
		boolean bsuccess = false;
		if (!IsCanAccept(bdcdyid)) {
			return bsuccess;
		}
		CommonDao dao = getCommonDao();
		BDCS_DJDY_GZ djdy = super.createDJDYfromXZ(bdcdyid);
		if (djdy != null) {
			RealUnit unit = null;
			try {
				UnitTools.loadUnit(this.getBdcdylx(), DJDYLY.initFrom(djdy.getLY()), djdy.getBDCDYID());
			} catch (Exception e) {
				e.printStackTrace();
			}

			BDCS_QL_GZ ql = super.createQL(djdy, unit);
			// 生成附属权利
			BDCS_FSQL_GZ fsql = super.createFSQL(djdy.getDJDYID());
			fsql.setQLID(ql.getId());
			ql.setFSQLID(fsql.getId());
			// 如果是使用权宗地，把使用权面积加上
			if (getBdcdylx().equals(BDCDYLX.SHYQZD)) {
				BDCS_SHYQZD_XZ xzshyqzd = dao.get(BDCS_SHYQZD_XZ.class, bdcdyid);
				if (xzshyqzd != null) {
					fsql.setSYQMJ(xzshyqzd.getZDMJ());
					ql.setQDJG(xzshyqzd.getJG());// 取得价格
				}
			}

			// 做转移的时候加上来源权利ID，同事权利起始时间和权籍结束时间从上一手权利获取
			String qllxarray = "('1','2','3','4','5','6','7','8','9','10','11','12','13','14','15','16','17','18')";
			String hql = " DJDYID='" + djdy.getDJDYID() + "' AND QLLX IN " + qllxarray;
			String lyqlid = "";
			List<BDCS_QL_XZ> list = dao.getDataList(BDCS_QL_XZ.class, hql);
			if (list != null && list.size() > 0) {
				lyqlid = list.get(0).getId();
				ql.setLYQLID(lyqlid);
				ql.setQLQSSJ(list.get(0).getQLQSSJ());
				ql.setQLJSSJ(list.get(0).getQLJSSJ());
			}
			// 保存权利和附属权利
			dao.save(ql);
			dao.save(fsql);
			dao.save(djdy);
			// 拷贝转移前权利人到申请人
			super.CopySQRFromXZQLR(djdy.getDJDYID(), ql.getQLLX(), this.getXMBH(), ql.getId(), SQRLB.YF.Value);
		}
		dao.flush();
		bsuccess = true;
		return bsuccess;
	}

	private boolean IsCanAccept(String bdcdyid) {
		RealUnit unit = UnitTools.loadUnit(BDCDYLX.H, DJDYLY.XZ, bdcdyid);
		if (unit == null) {
			super.setErrMessage("房屋单元不存在！");
			return false;
		}
		House h = (House) unit;
		String zdbdcdyid = h.getZDBDCDYID();
		if (StringHelper.isEmpty(zdbdcdyid)) {
			super.setErrMessage("未关联宗地！");
			return false;
		}
		RealUnit unit_zd = UnitTools.loadUnit(BDCDYLX.SHYQZD, DJDYLY.XZ, zdbdcdyid);
		if (unit_zd == null) {
			super.setErrMessage("未找到关联宗地！");
			return false;
		}
		List<Rights> rights = RightsTools.loadRightsByCondition(DJDYLY.XZ,
				"QLLX IN ('4','6','8') AND DJDYID IN (SELECT DJDYID FROM BDCS_DJDY_XZ WHERE BDCDYID='" + bdcdyid
						+ "')");
		if (rights == null || rights.size() <= 0) {
			super.setErrMessage("未找到要转移权利！");
			return false;
		}
		Rights ql = rights.get(0);
		List<RightsHolder> list_qlr_h = RightsHolderTools.loadRightsHolders(DJDYLY.XZ, ql.getId());
		List<String> list_qlrxx_h = new ArrayList<String>();
		if (list_qlr_h != null && list_qlr_h.size() > 0) {
			for (RightsHolder qlr : list_qlr_h) {
				String qlrxx = StringHelper.formatObject(qlr.getQLRMC()) + "&&"
						+ StringHelper.formatObject(qlr.getZJH());
				if (!list_qlrxx_h.contains(qlrxx)) {
					list_qlrxx_h.add(qlrxx);
				}
			}
		}
		List<Rights> rights_zd = RightsTools.loadRightsByCondition(DJDYLY.XZ,
				"QLLX IN ('3','5','7') AND DJDYID IN (SELECT DJDYID FROM BDCS_DJDY_XZ WHERE BDCDYID='" + zdbdcdyid
						+ "')");
		if (rights_zd == null || rights_zd.size() <= 0) {
			super.setErrMessage("未找到宗地上的使用权！");
			return false;
		}
		Rights ql_zd = rights_zd.get(0);
		List<RightsHolder> list_qlr_zd = RightsHolderTools.loadRightsHolders(DJDYLY.XZ, ql_zd.getId());
		if (list_qlr_zd != null && list_qlr_zd.size() > 0) {
			for (RightsHolder qlr : list_qlr_zd) {
				String qlrxx = StringHelper.formatObject(qlr.getQLRMC()) + "&&"
						+ StringHelper.formatObject(qlr.getZJH());
				if (list_qlrxx_h.contains(qlrxx)) {
					list_qlrxx_h.remove(qlrxx);
				}
			}
		}
		if (list_qlrxx_h != null && list_qlrxx_h.size() > 0) {
			super.setErrMessage("房屋和宗地中权利人情况不一致！");
			return false;
		}
		return true;
	}

	@Override
	public boolean writeDJB() {
		String strSqlXMBH = ProjectHelper.GetXMBHCondition(getXMBH());
		List<BDCS_DJDY_GZ> djdys = getCommonDao().getDataList(
				BDCS_DJDY_GZ.class, strSqlXMBH);
		if (djdys != null && djdys.size() > 0) {
			for (int idjdy = 0; idjdy < djdys.size(); idjdy++) {
				BDCS_DJDY_GZ bdcs_djdy_gz = djdys.get(idjdy);
				if (bdcs_djdy_gz != null) {
					String djdyid = bdcs_djdy_gz.getDJDYID();
					String bdcdyid = bdcs_djdy_gz.getBDCDYID();
					super.removeQLFromXZByQLLX(djdyid);
					super.removeQLRFromXZByQLLX(djdyid);
					super.removeQDZRFromXZByQLLX(djdyid);
					super.removeZSFromXZByQLLX(djdyid);

					super.CopyGZQLToXZAndLS(djdyid);
					super.CopyGZQLRToXZAndLS(djdyid);
					super.CopyGZQDZRToXZAndLS(djdyid);
					super.CopyGZZSToXZAndLS(djdyid);
					updateZD(bdcdyid,djdyid);
					removeCF(djdyid,"H");
				}
			}
		}
		this.SetSFDB();
		getCommonDao().flush();
		super.alterCachedXMXX();
		return true;
	}

	public void removeCF(String djdyid, String lx){
		String dbr = Global.getCurrentUserName();
		StringBuilder builderDJDYID = new StringBuilder();
		builderDJDYID.append(" DJDYID='").append(djdyid).append("'");
		builderDJDYID.append(" and QLLX='99' and djlx='").append(DJLX.CFDJ.Value).append("'");
		String strSqlDJDYID = builderDJDYID.toString();
		List<BDCS_QL_XZ> qls = getCommonDao().getDataList(BDCS_QL_XZ.class, strSqlDJDYID);
		if (qls != null && qls.size() > 0) {
			for (int iql = 0; iql < qls.size(); iql++) {
				BDCS_QL_XZ bdcs_ql_xz = qls.get(iql);
				if (bdcs_ql_xz != null) {
					BDCS_FSQL_XZ bdcs_fsql_xz = getCommonDao().get(BDCS_FSQL_XZ.class, bdcs_ql_xz.getFSQLID());
					if (bdcs_fsql_xz != null) {
						getCommonDao().deleteEntity(bdcs_fsql_xz);
					}
					BDCS_FSQL_LS bdcs_fsql_ls = getCommonDao().get(BDCS_FSQL_LS.class, bdcs_ql_xz.getFSQLID());
					if (bdcs_fsql_ls != null) {
						// 注销时间，注销登簿人，注销业务号
						bdcs_fsql_ls.setZXSJ(new Date());
						bdcs_fsql_ls.setZXDBR(dbr);
						bdcs_fsql_ls.setZXDYYWH(getProject_id());
						bdcs_fsql_ls.setZXFJ("强制过户");
						bdcs_fsql_ls.setZXDYYY("强制过户");
						getCommonDao().update(bdcs_fsql_ls);
					}
					getCommonDao().deleteEntity(bdcs_ql_xz);
				}
			}
		}
		//根据本地化配置来确定时候注销期房查封(1为是，2为否)
		if ("H".equals(lx)) {
			BDCS_DJDY_GZ djdy = getCommonDao().get(BDCS_DJDY_GZ.class, djdyid);	
			if (djdy != null) {
				String bdcdyid=djdy.getBDCDYID();
				String is_cancleqfcf_xfqzgh=ConfigHelper.getNameByValue("IS_CANCLEQFCF_XFQZGH");
				if ("1".equals(is_cancleqfcf_xfqzgh)) {
					List<YC_SC_H_XZ> REA = new ArrayList<YC_SC_H_XZ>();
					REA = getCommonDao().getDataList(YC_SC_H_XZ.class, " SCBDCDYID='"+bdcdyid+"'");
					if(REA.size() > 0 ){
						List<BDCS_DJDY_XZ> YCH = getCommonDao().getDataList(BDCS_DJDY_XZ.class, " BDCDYID ='"+REA.get(0).getYCBDCDYID()+"'");
						if(YCH != null && YCH.size() > 0){
							List<BDCS_QL_XZ> cfqls = getCommonDao().getDataList(BDCS_QL_XZ.class, "QLLX='99' AND DJLX='800' AND DJDYID='"+YCH.get(0).getDJDYID()+"'");
							for (int i = 0; i < cfqls.size(); i++) {
								getCommonDao().deleteEntity(cfqls.get(i));
								if (!StringHelper.isEmpty(cfqls.get(i).getFSQLID())){
									getCommonDao().delete(BDCS_FSQL_XZ.class, cfqls.get(i).getFSQLID());
									//把历史层里边的查封记录填写上注销登簿人和注销原因等
									BDCS_FSQL_LS fsql_LS = getCommonDao().get(BDCS_FSQL_LS.class, cfqls.get(i).getFSQLID());
									fsql_LS.setZXFJ("现房强制过户注销期房查封");
									fsql_LS.setZXDBR(Global.getCurrentUserName());
									fsql_LS.setZXSJ(new Date());
									fsql_LS.setZXDYYWH(super.getProject_id());
									fsql_LS.setZXDYYY("现房强制过户注销期房查封");
									getCommonDao().update(fsql_LS);
								}								
							}
						}
					}
				}
			}
		}
	}
	
	public void updateZD(String bdcdyid_h, String djdyid_h) {
		CommonDao dao = getCommonDao();
		RealUnit unit_h = UnitTools.loadUnit(BDCDYLX.H, DJDYLY.XZ, bdcdyid_h);
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
		String djdyid_zd = "";
		List<BDCS_DJDY_XZ> djdys = dao.getDataList(BDCS_DJDY_XZ.class, "BDCDYID='" + bdcdyid_zd + "'");
		if (djdys != null && djdys.size() > 0) {
			djdyid_zd = djdys.get(0).getDJDYID();
		}
		if (StringHelper.isEmpty(djdyid_zd)) {
			return;
		}
		removeCF(djdyid_zd,"ZD");
		HashMap<String, String> m_djdyid = new HashMap<String, String>();
		HashMap<String, String> m_qlid = new HashMap<String, String>();
		HashMap<String, String> m_fsqlid = new HashMap<String, String>();
		HashMap<String, String> m_qlrid = new HashMap<String, String>();
		HashMap<String, String> m_zsid = new HashMap<String, String>();
		List<String> list_zsid_h = new ArrayList<String>();
		m_djdyid.put(djdyid_h, djdyid_zd);
		List<Rights> list_ql_zd_old = RightsTools.loadRightsByCondition(DJDYLY.XZ,
				"QLLX IN ('3','5','7') AND DJDYID='" + djdyid_zd + "'");
		Rights ql_zd_old = null;
		SubRights fsql_zd_old = null;
		if (list_ql_zd_old != null && list_ql_zd_old.size() > 0) {
			ql_zd_old = list_ql_zd_old.get(0);
			fsql_zd_old = RightsTools.loadSubRights(DJDYLY.XZ, ql_zd_old.getFSQLID());

		}
		List<Rights> list_ql_h_new = RightsTools.loadRightsByCondition(DJDYLY.GZ,
				"QLLX IN ('4','6','8') AND DJDYID='" + djdyid_h + "' AND XMBH='" + super.getXMBH() + "'");
		Rights ql_h_new = null;
		if (list_ql_h_new != null && list_ql_h_new.size() > 0) {
			ql_h_new = list_ql_h_new.get(0);
		}

		if (ql_zd_old == null) {
			return;
		}
		if (ql_h_new == null) {
			return;
		}
		String qlid_new = SuperHelper.GeneratePrimaryKey();
		String fsqlid_new = SuperHelper.GeneratePrimaryKey();
		BDCS_QL_XZ ql_zd_new = new BDCS_QL_XZ();
		try {
			PropertyUtils.copyProperties(ql_zd_new, ql_zd_old);
		} catch (Exception e) {

		}
		ql_zd_new.setId(qlid_new);
		ql_zd_new.setFSQLID(fsqlid_new);
		if (list_ql_h_new != null) {
			ql_zd_new.setCZFS(ql_h_new.getCZFS());
			ql_zd_new.setZSBS(ql_h_new.getZSBS());
			ql_zd_new.setDBR(Global.getCurrentUserName());
			ql_zd_new.setYWH(super.getProject_id());
			ql_zd_new.setDJLX(super.getDjlx().Value);
			ql_zd_new.setDJSJ(new Date());
			ql_zd_new.setBDCQZH("");
			ql_zd_new.setZSBH("");
			RightsTools.deleteRightsAll(DJDYLY.XZ, ql_zd_old.getId());
		}
		dao.save(ql_zd_new);
		BDCS_QL_LS ql_zd_new_ls = new BDCS_QL_LS();
		try {
			PropertyUtils.copyProperties(ql_zd_new_ls, ql_zd_new);
		} catch (Exception e) {

		}
		dao.save(ql_zd_new_ls);
		m_qlid.put(ql_zd_old.getId(), qlid_new);
		if (fsql_zd_old != null) {
			BDCS_FSQL_XZ fsql_zd_new = new BDCS_FSQL_XZ();
			try {
				PropertyUtils.copyProperties(fsql_zd_new, fsql_zd_old);
			} catch (Exception e) {

			}
			fsql_zd_new.setId(fsqlid_new);
			fsql_zd_new.setQLID(qlid_new);
			dao.save(fsql_zd_new);
			BDCS_FSQL_LS fsql_zd_new_ls = new BDCS_FSQL_LS();
			try {
				PropertyUtils.copyProperties(fsql_zd_new_ls, fsql_zd_new);
			} catch (Exception e) {

			}
			dao.save(fsql_zd_new_ls);
			m_fsqlid.put(fsql_zd_old.getId(), fsqlid_new);
		}

		List<RightsHolder> list_qlr_h_new = RightsHolderTools.loadRightsHolders(DJDYLY.GZ, ql_h_new.getId());
		if (list_qlr_h_new != null && list_qlr_h_new.size() > 0) {
			for (RightsHolder qlr_h_new : list_qlr_h_new) {
				String qlrid_zd_new = SuperHelper.GeneratePrimaryKey();
				BDCS_QLR_XZ qlr_zd_new = new BDCS_QLR_XZ();
				try {
					PropertyUtils.copyProperties(qlr_zd_new, qlr_h_new);
				} catch (Exception e) {

				}
				qlr_zd_new.setId(qlrid_zd_new);
				qlr_zd_new.setQLID(qlid_new);
				dao.save(qlr_zd_new);
				BDCS_QLR_LS qlr_zd_new_ls = new BDCS_QLR_LS();
				try {
					PropertyUtils.copyProperties(qlr_zd_new_ls, qlr_zd_new);
				} catch (Exception e) {

				}
				dao.save(qlr_zd_new_ls);
				m_qlrid.put(qlr_h_new.getId(), qlrid_zd_new);
				List<BDCS_QDZR_GZ> list_qdzr_h_new = dao.getDataList(BDCS_QDZR_GZ.class, "QLID='" + ql_h_new.getId()
						+ "' AND XMBH='" + super.getXMBH() + "' AND QLRID='" + qlr_h_new.getId() + "'");
				if (list_qdzr_h_new != null && list_qdzr_h_new.size() > 0) {
					for (BDCS_QDZR_GZ qdzr_h_new : list_qdzr_h_new) {
						String qdzrid_zd_new = SuperHelper.GeneratePrimaryKey();
						BDCS_QDZR_XZ qlr_qdzr_zd_new = new BDCS_QDZR_XZ();
						qlr_qdzr_zd_new.setId(qdzrid_zd_new);
						qlr_qdzr_zd_new.setXMBH(super.getXMBH());
						qlr_qdzr_zd_new.setDJDYID(djdyid_zd);
						qlr_qdzr_zd_new.setFSQLID(fsqlid_new);
						qlr_qdzr_zd_new.setQLID(qlid_new);
						qlr_qdzr_zd_new.setQLRID(qlrid_zd_new);
						if (m_zsid.containsKey(qdzr_h_new.getZSID())) {
							qlr_qdzr_zd_new.setZSID(m_zsid.get(qdzr_h_new.getZSID()));
						} else {
							String zsid_zd_new = SuperHelper.GeneratePrimaryKey();
							qlr_qdzr_zd_new.setZSID(zsid_zd_new);
							m_zsid.put(qdzr_h_new.getZSID(), zsid_zd_new);
							list_zsid_h.add(qdzr_h_new.getZSID());
						}
						dao.save(qlr_qdzr_zd_new);
						BDCS_QDZR_LS qdzr_zd_new_ls = new BDCS_QDZR_LS();
						try {
							PropertyUtils.copyProperties(qdzr_zd_new_ls, qlr_qdzr_zd_new);
						} catch (Exception e) {

						}
						dao.save(qdzr_zd_new_ls);
					}
				}
			}
		}

		if (list_zsid_h != null && list_zsid_h.size() > 0) {
			for (String zsid_h_new : list_zsid_h) {
				BDCS_ZS_XZ zs_zd_new = new BDCS_ZS_XZ();
				zs_zd_new.setXMBH(super.getXMBH());
				zs_zd_new.setId(m_zsid.get(zsid_h_new));
				dao.save(zs_zd_new);
				BDCS_ZS_LS zs_zd_new_ls = new BDCS_ZS_LS();
				try {
					PropertyUtils.copyProperties(zs_zd_new_ls, zs_zd_new);
				} catch (Exception e) {

				}
				dao.save(zs_zd_new_ls);
			}
		}
	}

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

			// 删除权利、附属权利、权利人、证书、权地证人关系
			String _hqlCondition = MessageFormat.format("XMBH=''{0}'' AND DJDYID=''{1}''", getXMBH(), djdyid);
			RightsTools.deleteRightsAllByCondition(DJDYLY.GZ, _hqlCondition);
		}
		baseCommonDao.flush();
		bsuccess = true;
		return bsuccess;
	}

	@Override
	public List<UnitTree> getUnitTree() {
		return super.getUnitList();
	}

	@Override
	public void addQLRbySQRArray(String qlid, Object[] sqrids) {
		super.addQLRbySQRs(qlid, sqrids);
	}

	@Override
	public void removeQLR(String qlid, String qlrid) {
		super.removeqlr(qlrid, qlid);
	}

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
			List<BDCS_DJDY_GZ> djdys = dao.getDataList(BDCS_DJDY_GZ.class, xmbhHql);
			if (djdys != null && djdys.size() > 0) {
				mashaller = JAXBContext.newInstance(Message.class).createMarshaller();
				// mashaller.setProperty(Marshaller.JAXB_ENCODING, "gbk");
				Calendar calendar = Calendar.getInstance(); // 创建一个日历对象
				calendar.setTime(new Date()); // 用当前时间初始化日历时间
				String ywh = packageXml.GetYWLSHByYWH(this.getProject_id());
				BDCS_XMXX xmxx = dao.get(BDCS_XMXX.class, super.getXMBH());
				String result = "";
				String msgName = "";
				for (int i = 0; i < djdys.size(); i++) {
					BDCS_DJDY_GZ djdy = djdys.get(i);
					BDCS_QL_GZ ql = super.getQL(djdy.getDJDYID());
					List<BDCS_QLR_GZ> qlrs = super.getQLRs(ql.getId());
					List<BDCS_SQR> sqrs = new ArrayList<BDCS_SQR>();
					ProjectServiceImpl serviceImpl = SuperSpringContext.getContext().getBean(ProjectServiceImpl.class);
					sqrs = serviceImpl.getSQRList(super.getXMBH());

					if (QLLX.GYJSYDSHYQ.Value.equals(this.getQllx().Value)
							|| QLLX.ZJDSYQ.Value.equals(this.getQllx().Value)
							|| QLLX.JTJSYDSYQ.Value.equals(this.getQllx().Value)) { // 国有建设使用权、宅基地、集体建设用地使用权
						try {
							BDCS_SHYQZD_XZ zd = dao.get(BDCS_SHYQZD_XZ.class, djdy.getBDCDYID());
							Message msg = exchangeFactory.createMessageBySHYQ();
							super.fillHead(msg, i, ywh, zd.getBDCDYH(), zd.getQXDM(),ql.getLYQLID());

							msg.getHead().setParcelID(StringHelper.formatObject(zd.getZDDM()));
							msg.getHead().setEstateNum(StringHelper.formatObject(zd.getBDCDYH()));
//							msg.getHead().setPreEstateNum(StringHelper.formatObject(zd.getBDCDYH()));
							if (zd != null && !StringUtils.isEmpty(zd.getQXDM())) {
								msg.getHead().setAreaCode(zd.getQXDM());
							}

							if (djdy != null) {
								List<ZTTGYQLR> zttqlr = msg.getData().getGYQLR();
								zttqlr = packageXml.getZTTGYQLRs(zttqlr, qlrs, zd, ql, null, null, null, null);
								msg.getData().setGYQLR(zttqlr);

								KTTZDJBXX jbxx = msg.getData().getKTTZDJBXX();
								jbxx = packageXml.getZDJBXX(jbxx, zd, ql, null, null);

								KTFZDBHQK bhqk = msg.getData().getZDBHQK();
								bhqk = packageXml.getKTFZDBHQK(bhqk, zd, ql, null, null);
								msg.getData().setZDBHQK(bhqk);

								QLFQLJSYDSYQ syq = msg.getData().getQLJSYDSYQ();
								syq = packageXml.getQLFQLJSYDSYQ(syq, zd, ql, ywh);

								DJTDJSLSQ sq = msg.getData().getDJSLSQ();
								sq = packageXml.getDJTDJSLSQ(sq, ywh, zd, ql, xmxx, null, xmxx.getSLSJ(), null, null,
										null);

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
							msgName = "Biz" + msg.getHead().getBizMsgID() + ".xml";
							mashaller.marshal(msg, new File(path + msgName));
							result = uploadFile(path + msgName, ConstValue.RECCODE.JSYDSHYQ_ZYDJ.Value, actinstID,
									djdy.getId(), ql.getId());
							names.put(djdy.getDJDYID(), msg.getHead().getBizMsgID() + ".xml");
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					if (QLLX.GYJSYDSHYQ_FWSYQ.Value.equals(this.getQllx().Value)) { // 房屋所有权
						try {
							BDCS_H_XZ h = dao.get(BDCS_H_XZ.class, djdy.getBDCDYID());
							BDCS_ZRZ_XZ zrz_ = null;
							BDCS_LJZ_XZ ljz_ = null;
							BDCS_SHYQZD_XZ zd = null;
							if (h != null && !StringUtils.isEmpty(h.getZRZBDCDYID())) {
								zrz_ = dao.get(BDCS_ZRZ_XZ.class, h.getZRZBDCDYID());
								if (zrz_ != null) {
									zrz_.setGHYT(h.getGHYT()); // 自然幢的ghyt取户的ghyt
								}
							}
							if (h != null && !StringUtils.isEmpty(h.getLJZID())) {
								ljz_ = dao.get(BDCS_LJZ_XZ.class, h.getLJZID());
							}
							BDCS_C_XZ c_ = null;
							if (h != null && !StringUtils.isEmpty(h.getCID())) {
								c_ = dao.get(BDCS_C_XZ.class, h.getCID());
							}
							if (h != null && !StringUtils.isEmpty(h.getZDBDCDYID())) {
								zd = dao.get(BDCS_SHYQZD_XZ.class, h.getZDBDCDYID());
								if (zd != null) {
									zrz_.setZDDM(zd.getZDDM());
								}
							}
							Message msg = exchangeFactory.createMessageByFWSYQ2();
							super.fillHead(msg, i, ywh, h.getBDCDYH(), h.getQXDM(),ql.getLYQLID());
							msg.getHead().setParcelID(zrz_.getZDDM());
							msg.getHead().setEstateNum(h.getBDCDYH());
//							msg.getHead().setPreEstateNum(h.getBDCDYH());
							if (zd != null && !StringUtils.isEmpty(zd.getQXDM())) {
								msg.getHead().setAreaCode(zd.getQXDM());
							}
							if (djdy != null) {

								List<ZTTGYQLR> zttqlr = msg.getData().getGYQLR();
								zttqlr = packageXml.getZTTGYQLRs(zttqlr, qlrs, null, ql, h, null, null, null);
								msg.getData().setGYQLR(zttqlr);

								KTTFWZRZ zrz = msg.getData().getKTTFWZRZ();
								zrz = packageXml.getKTTFWZRZ(zrz, zrz_);

								KTTFWLJZ ljz = msg.getData().getKTTFWLJZ();
								ljz = packageXml.getKTTFWLJZ(ljz, ljz_,h);

								KTTFWC kttc = msg.getData().getKTTFWC();
								kttc = packageXml.getKTTFWC(kttc, c_, zrz);
								msg.getData().setKTTFWC(kttc);

								KTTFWH fwh = msg.getData().getKTTFWH();
								fwh = packageXml.getKTTFWH(fwh, h);

								QLTFWFDCQYZ fdcqyz = msg.getData().getQLTFWFDCQYZ();
								fdcqyz = packageXml.getQLTFWFDCQYZ(fdcqyz, h, ql, ywh);

								DJTDJSLSQ sq = msg.getData().getDJSLSQ();
								sq = packageXml.getDJTDJSLSQ(sq, ywh, null, ql, xmxx, h, xmxx.getSLSJ(), null, null,
										null);

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
							result = uploadFile(path + msgName, ConstValue.RECCODE.FDCQDZ_ZYDJ.Value, actinstID,
									djdy.getId(), ql.getId());
							names.put(djdy.getDJDYID(), msg.getHead().getBizMsgID() + ".xml");
						} catch (Exception e) {
							e.printStackTrace();
						}
					}

					if (QLLX.JTTDSYQ.Value.equals(this.getQllx().Value)) { // 集体土地所有权
						try {
							BDCS_SYQZD_XZ oland = dao.get(BDCS_SYQZD_XZ.class, djdy.getBDCDYID());
							Message msg = exchangeFactory.createMessageByTDSYQ();
							super.fillHead(msg, i, ywh, oland.getBDCDYH(), oland.getQXDM(),ql.getLYQLID());
							msg.getHead().setParcelID(StringHelper.formatObject(oland.getZDDM()));
							msg.getHead().setEstateNum(StringHelper.formatObject(oland.getBDCDYH()));
//							msg.getHead().setPreEstateNum(StringHelper.formatObject(oland.getBDCDYH()));
							if (oland != null && !StringUtils.isEmpty(oland.getQXDM())) {
								msg.getHead().setAreaCode(oland.getQXDM());
							}
							if (djdy != null) {

								KTTZDJBXX jbxx = msg.getData().getKTTZDJBXX();
								jbxx = packageXml.getZDJBXX(jbxx, null, ql, oland, null);

								String zdzhdm = "";
								if (oland != null) {
									zdzhdm = oland.getZDDM();
								}
								KTTGYJZX jzx = msg.getData().getKTTGYJZX();
								jzx = packageXml.getKTTGYJZX(jzx, zdzhdm);

								KTTGYJZD jzd = msg.getData().getKTTGYJZD();
								jzd = packageXml.getKTTGYJZD(jzd, zdzhdm);

								FJF100 fj = msg.getData().getFJF100();
								fj = packageXml.getFJF(fj);

								KTFZDBHQK bhqk = msg.getData().getZDBHQK();
								bhqk = packageXml.getKTFZDBHQK(bhqk, null, ql, oland, null);
								msg.getData().setZDBHQK(bhqk);

								QLFQLTDSYQ tdsyq = msg.getData().getQLFQLTDSYQ();
								tdsyq = packageXml.getQLFQLTDSYQ(tdsyq, oland, ql, ywh);

								List<ZTTGYQLR> zttqlr = msg.getData().getGYQLR();
								zttqlr = packageXml.getZTTGYQLRs(zttqlr, qlrs, null, ql, null, oland, null, null);
								msg.getData().setGYQLR(zttqlr);

								DJTDJSLSQ sq = msg.getData().getDJSLSQ();
								sq = packageXml.getDJTDJSLSQ(sq, ywh, null, ql, xmxx, null, xmxx.getSLSJ(), oland, null,
										null);

								List<DJFDJSJ> sj = msg.getData().getDJSJ();
								sj = packageXml.getDJFDJSJ(oland, ywh,actinstID);
								msg.getData().setDJSJ(sj);

								//9.登记收费(可选)
								List<DJFDJSF> sfList = msg.getData().getDJSF();
								sfList = packageXml.getDJSF(oland, ywh,this.getXMBH());
								msg.getData().setDJSF(sfList);

								//10.审核(可选)
								List<DJFDJSH> sh = msg.getData().getDJSH();
								 sh = packageXml.getDJFDJSH(oland, ywh, this.getXMBH(), actinstID);
								msg.getData().setDJSH(sh);

								//11.缮证(可选)
								List<DJFDJSZ>  sz = packageXml.getDJFDJSZ(oland, ywh, this.getXMBH());
								msg.getData().setDJSZ(sz);

								//11.发证(可选)
								List<DJFDJFZ>   fz = packageXml.getDJFDJFZ(oland, ywh, this.getXMBH());
								msg.getData().setDJFZ(fz);
								
								//12.归档(可选)
								List<DJFDJGD> gd = packageXml.getDJFDJGD(oland, ywh, this.getXMBH());
								msg.getData().setDJGD(gd);	
								List<ZDK103> zdk = msg.getData().getZDK103();
								zdk = packageXml.getZDK103(zdk, null, oland, null);

								List<DJFDJSQR> djsqrs = msg.getData().getDJSQR();
								djsqrs = packageXml.getDJSQRs(djsqrs, sqrs, oland.getYSDM(), ywh, oland.getBDCDYH());
								msg.getData().setDJSQR(djsqrs);
							}
							msgName = "Biz" + msg.getHead().getBizMsgID() + ".xml";
							names.put(djdy.getDJDYID(), msg.getHead().getBizMsgID() + ".xml");
							mashaller.marshal(msg, new File(path + msgName));
							result = uploadFile(path + msgName, ConstValue.RECCODE.TDSYQ_CSDJ.Value, actinstID,
									djdy.getId(), ql.getId());
						} catch (Exception e) {
							e.printStackTrace();
						}
					}

					if (null == result) {
						Map<String, String> xmlError = new HashMap<String, String>();
						xmlError.put("error", "连接SFTP失败,请检查服务器和前置机的连接是否正常");
						if (QLLX.JTTDSYQ.Value.equals(this.getQllx().Value)) {
							YwLogUtil.addSjsbResult(msgName, "", "连接SFTP失败,请检查服务器和前置机的连接是否正常", ConstValue.SF.NO.Value,
									ConstValue.RECCODE.TDSYQ_ZYDJ.Value,
									ProjectHelper.getpRroinstIDByActinstID(actinstID));
						} else if (QLLX.GYJSYDSHYQ_FWSYQ.Value.equals(this.getQllx().Value)) {
							YwLogUtil.addSjsbResult(msgName, "", "连接SFTP失败,请检查服务器和前置机的连接是否正常", ConstValue.SF.NO.Value,
									ConstValue.RECCODE.FDCQDZ_ZYDJ.Value,
									ProjectHelper.getpRroinstIDByActinstID(actinstID));
						} else {
							YwLogUtil.addSjsbResult(msgName, "", "连接SFTP失败,请检查服务器和前置机的连接是否正常", ConstValue.SF.NO.Value,
									ConstValue.RECCODE.JSYDSHYQ_ZYDJ.Value,
									ProjectHelper.getpRroinstIDByActinstID(actinstID));
						}
						return xmlError;
					}
					if (!"1".equals(result) && result.indexOf("success") == -1) { // xml本地校验不通过时
						Map<String, String> xmlError = new HashMap<String, String>();
						xmlError.put("error", result);
						return xmlError;
					}
					if (!StringUtils.isEmpty(result) && result.indexOf("success") > -1
							&& !names.containsKey("reccode")) {
						names.put("reccode", result);
					}
				}
			}

		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return names;
	}

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
				List<RightsHolder> bdcqlrs = RightsHolderTools.loadRightsHolders(ConstValue.DJDYLY.GZ, djdy.getDJDYID(),
						getXMBH());
				MessageExport msg = super.getShareMsgTools().GetMsg(bdcdy, bdcql, bdcfsql, bdcqlrs, bljc, xmxx);
				super.getShareMsgTools().SendMsg(msg, idjdy + 1, djdy.getBDCDYLX(), xmxx);
			}
		}
	}
}
