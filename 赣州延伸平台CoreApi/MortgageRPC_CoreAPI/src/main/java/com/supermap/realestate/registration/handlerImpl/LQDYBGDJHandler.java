package com.supermap.realestate.registration.handlerImpl;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.supermap.realestate.registration.ViewClass.ProjectInfo;
import com.supermap.realestate.registration.ViewClass.UnitTree;
import com.supermap.realestate.registration.handler.DJHandler;
import com.supermap.realestate.registration.model.BDCS_DJDY_GZ;
import com.supermap.realestate.registration.model.BDCS_DJDY_XZ;
import com.supermap.realestate.registration.model.BDCS_FSQL_GZ;
import com.supermap.realestate.registration.model.BDCS_QDZR_GZ;
import com.supermap.realestate.registration.model.BDCS_QLR_GZ;
import com.supermap.realestate.registration.model.BDCS_QLR_XZ;
import com.supermap.realestate.registration.model.BDCS_QL_GZ;
import com.supermap.realestate.registration.model.BDCS_QL_XZ;
import com.supermap.realestate.registration.model.BDCS_SQR;
import com.supermap.realestate.registration.model.BDCS_XMXX;
import com.supermap.realestate.registration.model.BDCS_ZS_GZ;
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
import com.supermap.realestate.registration.util.Global;
import com.supermap.realestate.registration.util.ProjectHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.realestate.registration.util.ConstValue.DJDYLY;
import com.supermap.realestate.registration.util.ConstValue.DJZT;
import com.supermap.realestate.registration.util.ConstValue.SF;
import com.supermap.realestate.registration.util.ConstValue.SQRLB;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import com.supermap.wisdombusiness.web.ResultMessage;

public class LQDYBGDJHandler extends DJHandlerBase implements DJHandler{

	public LQDYBGDJHandler(ProjectInfo info) {
		super(info);
	}

	@Override
	public boolean addBDCDY(String qlids) {
		List<String> listDJDY = new ArrayList<String>();
		String qlid[] = qlids.split(",");
		if (qlid != null && qlid.length > 0) {
			for (String id : qlid) {
				BDCS_QL_XZ ql = getCommonDao().get(BDCS_QL_XZ.class, id);
				if (ql != null) {
					// 拷贝登记单元
					if (!StringHelper.isEmpty(ql.getDJDYID()) && !listDJDY.contains(ql.getDJDYID())) {
						listDJDY.add(ql.getDJDYID());
						List<BDCS_DJDY_XZ> list = getCommonDao().getDataList(BDCS_DJDY_XZ.class, "DJDYID='" + ql.getDJDYID() + "'");
						if (list != null && list.size() > 0) {
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
					String gzqlid = "";
					String newqzh = "";
					String workflowcode = ProjectHelper.getWorkflowCodeByProjectID(this
							.getProject_id());
					List<WFD_MAPPING> listCode = getCommonDao().getDataList(
							WFD_MAPPING.class, "WORKFLOWCODE='" + workflowcode + "'");
					if (listCode != null && listCode.size() > 0) {
						newqzh = listCode.get(0).getNEWQZH();
					}
					if (SF.NO.Value.equals(newqzh)) {
						// 拷贝权利信息（权证号不为空）
						BDCS_QL_GZ bdcs_ql_gz = super.CopyQLXXFromXZ(id);
						bdcs_ql_gz.setYWH(this.getProject_id());
						gzqlid = bdcs_ql_gz.getId();
					} else {
						// 拷贝权利信息（权证号为空）
						BDCS_QL_GZ bdcs_ql_gz = super.CopyQLXXFromXZExceptBDCQZH(id);
						bdcs_ql_gz.setYWH(this.getProject_id());
						gzqlid = bdcs_ql_gz.getId();
					}
					CopySQRFromXZQLR(id, gzqlid);

					ql.setDJZT(DJZT.DJZ.Value);
					getCommonDao().update(ql);
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

	@Override
	public boolean writeDJB() {

		if (super.isCForCFING()) {
			return false;
		}

		CommonDao commonDao = getCommonDao();
		String dbr = Global.getCurrentUserName();
		String strSqlXMBH = ProjectHelper.GetXMBHCondition(getXMBH());
		List<Rights> lstrights = RightsTools.loadRightsByCondition(DJDYLY.GZ, strSqlXMBH);
		for (Rights right : lstrights) {
			// 根据权利ID删除权利，附属权利，证书，权地证人，权利人信息
			RightsTools.deleteRightsAll(DJDYLY.XZ, right.getLYQLID());
			// 更新历史附属权利信息
			SubRights subright = RightsTools.loadSubRightsByRightsID(DJDYLY.LS, right.getLYQLID());
			if (subright != null) {
				Date zxsj = new Date();
				subright.setZXDYYWH(this.getProject_id());
				subright.setZXSJ(zxsj);
				subright.setZXDBR(dbr);
				commonDao.update(subright);
			}
		}
		List<BDCS_DJDY_GZ> djdys = commonDao.getDataList(BDCS_DJDY_GZ.class, strSqlXMBH);
		if (djdys != null && djdys.size() > 0) {
			for (int idjdy = 0; idjdy < djdys.size(); idjdy++) {
				BDCS_DJDY_GZ bdcs_djdy_gz = djdys.get(idjdy);
				if (bdcs_djdy_gz != null) {
					String djdyid = bdcs_djdy_gz.getDJDYID();
					super.CopyGZQLToXZAndLS(djdyid);
					super.CopyGZQLRToXZAndLS(djdyid);
					super.CopyGZQDZRToXZAndLS(djdyid);
					super.CopyGZZSToXZAndLS(djdyid);
				}
			}
		}
		this.SetSFDB();
		super.alterCachedXMXX();
		commonDao.flush();
		return true;
	
	}

	@Override
	public boolean removeBDCDY(String qlid) {
		String xmbh = getXMBH();
		// 先删除登记单元
		BDCS_QL_GZ bdcs_ql_gz = getCommonDao().get(BDCS_QL_GZ.class, qlid);
		if (bdcs_ql_gz != null) {
			String lyqlid = bdcs_ql_gz.getLYQLID();
			BDCS_QL_XZ bdcs_ql_xz = getCommonDao().get(BDCS_QL_XZ.class, lyqlid);
			if (bdcs_ql_xz != null) {
				bdcs_ql_xz.setDJZT(DJZT.WDJ.Value);
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
				// 只有当登记单元关联的权利个数为1的时候才移除单元（不支持批量移除）
				List<BDCS_QL_GZ> qls = getCommonDao().getDataList(BDCS_QL_GZ.class, builderDJDY.toString());
				if (qls != null && qls.size() == 1) {
					getCommonDao().deleteEntity(bdcs_djdy_gz);
				}
			}
		}

		// 删除权利关联申请人
		RemoveSQRByGLQLID(qlid, getXMBH());
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

	@Override
	public List<UnitTree> getUnitTree() {
		List<UnitTree> list = super.getRightList();
		for (UnitTree tree : list) {
			String qlid = tree.getQlid();
			Rights right = RightsTools.loadRights(DJDYLY.GZ, qlid);
			if (right != null) {
				tree.setOldqlid(right.getLYQLID());
				tree.setOlddiyqqlid(right.getLYQLID());
				tree.setQlid(right.getId());
				tree.setDIYQQlid(right.getId());
			}
		}
		return list;
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
				String lyqlid = bdcql.getLYQLID();
				SubRights bdcfsql = RightsTools.loadSubRightsByRightsID(ConstValue.DJDYLY.GZ, bdcql.getId());
				List<RightsHolder> bdcqlrs = RightsHolderTools.loadRightsHolders(ConstValue.DJDYLY.LS, lyqlid);
				//bdcql.setId(lyqlid);
				MessageExport msg = super.getShareMsgTools().GetMsg(bdcdy, bdcql, bdcfsql, bdcqlrs, bljc, xmxx);
				super.getShareMsgTools().SendMsg(msg, idjdy + 1, djdy.getBDCDYLX(), xmxx);
			}
		}
	}
	
	private void CopySQRFromXZQLR(String xzqlid, String gzqlid) {
		StringBuilder builderQLR = new StringBuilder();
		builderQLR.append("QLID ='");
		builderQLR.append(xzqlid);
		builderQLR.append("'");
		List<BDCS_QLR_XZ> qlrs = getCommonDao().getDataList(BDCS_QLR_XZ.class, builderQLR.toString());
		if (qlrs == null || qlrs.size() <= 0) {
			return;
		}
		for (int iqlr = 0; iqlr < qlrs.size(); iqlr++) {
			BDCS_QLR_XZ qlr = qlrs.get(iqlr);
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
				if(StringHelper.isEmpty(sqr.getSQRLX())){
					sqr.setSQRLX("1");
				}
				getCommonDao().save(sqr);
			}
		}
	}
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
}
