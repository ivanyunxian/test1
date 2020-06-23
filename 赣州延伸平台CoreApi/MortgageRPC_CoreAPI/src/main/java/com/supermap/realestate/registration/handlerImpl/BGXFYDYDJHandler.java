package com.supermap.realestate.registration.handlerImpl;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.supermap.realestate.registration.ViewClass.ProjectInfo;
import com.supermap.realestate.registration.ViewClass.UnitTree;
import com.supermap.realestate.registration.handler.DJHandler;
import com.supermap.realestate.registration.model.BDCS_DJDY_GZ;
import com.supermap.realestate.registration.model.BDCS_DJDY_XZ;
import com.supermap.realestate.registration.model.BDCS_FSQL_GZ;
import com.supermap.realestate.registration.model.BDCS_FSQL_XZ;
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
import com.supermap.realestate.registration.util.ConstValue.DJDYLY;
import com.supermap.realestate.registration.util.ConstValue.DJZT;
import com.supermap.realestate.registration.util.ConstValue.SF;
import com.supermap.realestate.registration.util.ConstValue.SQRLB;
import com.supermap.realestate.registration.util.Global;
import com.supermap.realestate.registration.util.ObjectHelper;
import com.supermap.realestate.registration.util.ProjectHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import com.supermap.wisdombusiness.web.ResultMessage;

/*
 
 2、实测户转移预告抵押权利预告登记登记
 */
/**
 * 变更现房预抵押
 * @author 10972
 *
 */
  


public class BGXFYDYDJHandler extends DJHandlerBase implements DJHandler {

	/**
	 * 构造函数
	 * @param inf_o
	 */
	public BGXFYDYDJHandler(ProjectInfo info) {
		super(info);
	}

	/**
	 * 添加不动产单元
	 */
	@Override
	public boolean addBDCDY(String bdcdyid) {
		List<String> listDJDY = new ArrayList<String>();
		String qlid[] = bdcdyid.split(",");
		if (qlid != null && qlid.length > 0) {
			for (String id : qlid) {
				BDCS_QL_XZ ql = getCommonDao().get(BDCS_QL_XZ.class, id);
				if (ql != null) {
					String bdcdyh = null;
					// 拷贝登记单元
					if (!StringHelper.isEmpty(ql.getDJDYID()) && !listDJDY.contains(ql.getDJDYID())) {
						listDJDY.add(ql.getDJDYID());
						List<BDCS_DJDY_XZ> list = getCommonDao().getDataList(BDCS_DJDY_XZ.class, "DJDYID='" + ql.getDJDYID() + "'");
						if (list != null && list.size() > 0) {
							bdcdyh = list.get(0).getBDCDYH();
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
					String workflowcode = ProjectHelper.getWorkflowCodeByProjectID(this.getProject_id());
					List<WFD_MAPPING> listCode = getCommonDao().getDataList(
							WFD_MAPPING.class, "WORKFLOWCODE='" + workflowcode + "'");
					if (listCode != null && listCode.size() > 0) {
						newqzh = listCode.get(0).getNEWQZH();
					}
					if (SF.NO.Value.equals(newqzh)) {
						// 拷贝权利信息（权证号不为空）
						BDCS_QL_GZ bdcs_ql_gz = super.CopyQLXXFromXZ(id);
						bdcs_ql_gz.setYWH(this.getProject_id());
						bdcs_ql_gz.setDJLX(this.getDjlx().Value);
						gzqlid = bdcs_ql_gz.getId();
					} else {
						// 拷贝权利信息（权证号为空）
						BDCS_QL_GZ bdcs_ql_gz = super.CopyQLXXFromXZExceptBDCQZH(id);
						bdcs_ql_gz.setYWH(this.getProject_id());
						bdcs_ql_gz.setDJLX(this.getDjlx().Value);
						gzqlid = bdcs_ql_gz.getId();
					}
					getCommonDao().flush();
					//CopySQRFromXZQLR(id, gzqlid);
					CopySQRFromGZQLR(ql.getXMBH(), gzqlid);
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

		/**
		 * 根据工作权利ID和拷贝现状权利人到申请人，并将sqrid传入qlr_gz
		 * @Author：rq
		 * @param xzqlid
		 * @param gzqlid
		 */
		private void CopySQRFromGZQLR(String xmbh_old, String gzqlid) {
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
			//添加上一首抵押权的义务人——用于受理回执单的显示					
			if (xmbh_old != null) {
				String sql = MessageFormat.format(" XMBH=''{0}'' AND SQRLB=''2''", xmbh_old);
				List<BDCS_SQR> sqrlist = getCommonDao().getDataList(BDCS_SQR.class, sql);
				if (sqrlist == null) {
					return;
				}
				for (int isqr = 0; isqr < sqrlist.size(); isqr++) {
					BDCS_SQR sqr = sqrlist.get(isqr);
					String SQRID = getPrimaryKey();
					BDCS_SQR sqr_ywr = new BDCS_SQR();
					ObjectHelper.copyObject(sqr,sqr_ywr);
					sqr_ywr.setXMBH(getXMBH());
					sqr_ywr.setGLQLID(gzqlid);
					sqr_ywr.setId(SQRID);
					sqr_ywr.setSQRLB(SQRLB.YF.Value);
					getCommonDao().save(sqr_ywr);
				}
			}
				
			
		}
	
	/**
	 * 写入登记薄
	 */
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
				subright.setZXDYYY(lstrights.get(0).getDJYY());//登记簿上一手的注销抵押原因是获取本次的登记原因
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
		return null;
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
	protected BDCS_SQR CopySQRFromDYQR(BDCS_QLR_XZ qlr, String glqlid) {
		String sqrid = getPrimaryKey();
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
	protected void CopyQLXXFromYDY(String qlid, BDCS_DJDY_GZ djdy, String sfnewcqr,String newqzh) {
		StringBuilder builer = new StringBuilder();
		builer.append(" QLID='").append(qlid).append("'");
		String strQuery = builer.toString();
		BDCS_QL_XZ bdcs_ql_xz = getCommonDao().get(BDCS_QL_XZ.class, qlid);
		if (bdcs_ql_xz != null) {
			String gzqlid = getPrimaryKey();
			String gzfsqlid = getPrimaryKey();
			// 拷贝权利
			BDCS_QL_GZ bdcs_ql_gz = ObjectHelper.copyQL_XZToGZ(bdcs_ql_xz);
			bdcs_ql_gz.setId(gzqlid);// 重置权利ID
			bdcs_ql_gz.setDJDYID(djdy.getDJDYID());// 重置登记单元id
			bdcs_ql_gz.setBDCDYH(djdy.getBDCDYH());// 重置不动产单元号
			bdcs_ql_gz.setYWH(getProject_id());// 重置业务号
			bdcs_ql_gz.setDJLX(getDjlx().Value);// 重置登记类型
			bdcs_ql_gz.setFSQLID(gzfsqlid);// 重置附属权利ID
			bdcs_ql_gz.setXMBH(getXMBH());// 重置项目编号
			bdcs_ql_gz.setLYQLID(qlid);// 设定来源权利ID
			bdcs_ql_gz.setDJSJ(null);// 重置登记时间
			bdcs_ql_gz.setZSBH("");// 重置证书编号
			bdcs_ql_gz.setDBR("");// 重置登簿人
			if (SF.YES.Value.equals(newqzh)) {
				bdcs_ql_gz.setBDCQZH("");// 重置不动产权证明号	
			}	
			// bdcs_ql_gz.setDJYY("");//登记原因赞不重置
			// bdcs_ql_gz.setFJ("");//登记附记赞不重置
			
			getCommonDao().save(bdcs_ql_gz);
			if (bdcs_ql_xz.getFSQLID() != null) {
				BDCS_FSQL_XZ bdcs_fsql_xz = getCommonDao().get(BDCS_FSQL_XZ.class, bdcs_ql_xz.getFSQLID());
				if (bdcs_fsql_xz != null) {
					// 拷贝附属权利
					BDCS_FSQL_GZ bdcs_fsql_gz = ObjectHelper.copyFSQL_XZToGZ(bdcs_fsql_xz);
					bdcs_fsql_gz.setQLID(gzqlid);// 重置权利ID
					bdcs_fsql_gz.setId(gzfsqlid);// 重置附属权利ID
					bdcs_fsql_gz.setXMBH(getXMBH());// 重置项目编号
					bdcs_fsql_gz.setDJDYID(djdy.getDJDYID());// 重置登记单元id
					bdcs_fsql_gz.setBDCDYH(djdy.getBDCDYH());// 重置不动产单元号
					if (!"1".equals(sfnewcqr)) {
						bdcs_fsql_gz.setDYR("");// 重置抵押人
					}
					bdcs_fsql_gz.setZXFJ("");// 重置抵押人
					bdcs_fsql_gz.setZXSJ(null);// 重置注销时间
					bdcs_fsql_gz.setZXDBR("");// 重置注销登簿人人
					getCommonDao().save(bdcs_fsql_gz);
				}
			}
			Map<String, String> lyzsid_zsid = new HashMap<String, String>();
			// 获取权利人集合
			List<BDCS_QLR_XZ> qlrs = getCommonDao().getDataList(BDCS_QLR_XZ.class, strQuery);
			if (qlrs != null && qlrs.size() > 0) {
				for (int iqlr = 0; iqlr < qlrs.size(); iqlr++) {
					BDCS_QLR_XZ bdcs_qlr_xz = qlrs.get(iqlr);
					if (bdcs_qlr_xz != null) {
						BDCS_SQR sqr = CopySQRFromDYQR(bdcs_qlr_xz, gzqlid);
						getCommonDao().save(sqr);
						// 拷贝权利人
						String gzqlrid = getPrimaryKey();
						BDCS_QLR_GZ bdcs_qlr_gz = ObjectHelper.copyQLR_XZToGZ(bdcs_qlr_xz);
						bdcs_qlr_gz.setId(gzqlrid);// 重置权利人ID
						bdcs_qlr_gz.setQLID(gzqlid);// 重置权利ID
						bdcs_qlr_gz.setXMBH(getXMBH());// 重置项目编号
						if (SF.YES.Value.equals(newqzh)) {
							bdcs_qlr_gz.setBDCQZH("");// 重置不动产权证明号
						}	
						bdcs_qlr_gz.setSQRID(sqr.getId());// 重置申请人id
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
								bdcs_zs_gz.setId(gzzsid);// 重置证书id
								bdcs_zs_gz.setXMBH(getXMBH());// 重置项目编号
								if (SF.YES.Value.equals(newqzh)) {
									bdcs_zs_gz.setBDCQZH("");// 重置不动产权证明号
								}
								bdcs_zs_gz.setZSBH("");// 重置证书编号
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
							List<BDCS_QDZR_XZ> qdzrs = getCommonDao().getDataList(BDCS_QDZR_XZ.class,
									builderQDZR.toString());
							if (qdzrs != null && qdzrs.size() > 0) {
								for (int iqdzr = 0; iqdzr < qdzrs.size(); iqdzr++) {
									BDCS_QDZR_XZ bdcs_qdzr_xz = qdzrs.get(iqdzr);
									if (bdcs_qdzr_xz != null) {
										// 拷贝权地证人
										BDCS_QDZR_GZ bdcs_qdzr_gz = ObjectHelper.copyQDZR_XZToGZ(bdcs_qdzr_xz);
										bdcs_qdzr_gz.setId(getPrimaryKey());
										bdcs_qdzr_gz.setZSID(lyzsid_zsid.get(bdcs_zs_xz.getId()));// 重置证书ID
										bdcs_qdzr_gz.setQLID(gzqlid);// 重置权利ID
										bdcs_qdzr_gz.setFSQLID(gzfsqlid);// 重置附属权利ID
										bdcs_qdzr_gz.setQLRID(gzqlrid);// 重置权利人ID
										bdcs_qdzr_gz.setXMBH(getXMBH());// 重置项目编号
										bdcs_qdzr_gz.setDJDYID(djdy.getDJDYID());// 重置登记单元ID
										bdcs_qdzr_gz.setBDCDYH(djdy.getBDCDYH());// 重置不动产权证明号
										getCommonDao().save(bdcs_qdzr_gz);
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
	/**
	 * @param qlid
	 * @param djdy
	 */
	protected void CopyQLXXFromYCQ(String qlid, BDCS_DJDY_GZ djdy, BDCS_QL_GZ ql, BDCS_FSQL_GZ fsql,String newqzh) {
		StringBuilder builer = new StringBuilder();
		builer.append(" QLID='").append(qlid).append("'");
		String strQuery = builer.toString();
		Map<String, String> lyzsid_zsid = new HashMap<String, String>();

		// 获取权利人集合
		List<BDCS_QLR_XZ> qlrs = getCommonDao().getDataList(BDCS_QLR_XZ.class, strQuery);
		if (qlrs != null && qlrs.size() > 0) {
			for (int iqlr = 0; iqlr < qlrs.size(); iqlr++) {
				BDCS_QLR_XZ bdcs_qlr_xz = qlrs.get(iqlr);
				if (bdcs_qlr_xz != null) {
					// 拷贝权利人
					String gzqlrid = getPrimaryKey();
					BDCS_QLR_GZ bdcs_qlr_gz = ObjectHelper.copyQLR_XZToGZ(bdcs_qlr_xz);
					bdcs_qlr_gz.setId(gzqlrid);// 重置权利人ID
					bdcs_qlr_gz.setQLID(ql.getId());// 重置权利ID
					bdcs_qlr_gz.setXMBH(getXMBH());// 重置项目编号
					if (SF.YES.Value.equals(newqzh)) {
						bdcs_qlr_gz.setBDCQZH("");// 重置不动产权证明号	
					}	
					// bdcs_qlr_gz.setSQRID(sqr.getId());// 重置申请人id
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
							bdcs_zs_gz.setId(gzzsid);// 重置证书id
							bdcs_zs_gz.setXMBH(getXMBH());// 重置项目编号
							if (SF.YES.Value.equals(newqzh)) {
								bdcs_zs_gz.setBDCQZH("");// 重置不动产权证明号
							}
							bdcs_zs_gz.setZSBH("");// 重置证书编号
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
						List<BDCS_QDZR_XZ> qdzrs = getCommonDao().getDataList(BDCS_QDZR_XZ.class,
								builderQDZR.toString());
						if (qdzrs != null && qdzrs.size() > 0) {
							for (int iqdzr = 0; iqdzr < qdzrs.size(); iqdzr++) {
								BDCS_QDZR_XZ bdcs_qdzr_xz = qdzrs.get(iqdzr);
								if (bdcs_qdzr_xz != null) {
									// 拷贝权地证人
									BDCS_QDZR_GZ bdcs_qdzr_gz = ObjectHelper.copyQDZR_XZToGZ(bdcs_qdzr_xz);
									bdcs_qdzr_gz.setId(getPrimaryKey());
									bdcs_qdzr_gz.setZSID(lyzsid_zsid.get(bdcs_zs_xz.getId()));// 重置证书ID
									bdcs_qdzr_gz.setQLID(ql.getId());// 重置权利ID
									bdcs_qdzr_gz.setFSQLID(fsql.getId());// 重置附属权利ID
									bdcs_qdzr_gz.setQLRID(gzqlrid);// 重置权利人ID
									bdcs_qdzr_gz.setXMBH(getXMBH());// 重置项目编号
									bdcs_qdzr_gz.setDJDYID(djdy.getDJDYID());// 重置登记单元ID
									bdcs_qdzr_gz.setBDCDYH(djdy.getBDCDYH());// 重置不动产权证明号
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
