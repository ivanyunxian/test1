package com.supermap.realestate.registration.handlerImpl;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.StringUtils;

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
import com.supermap.realestate.registration.util.ConstValue.BDCDYLX;
import com.supermap.realestate.registration.util.ConstValue.CZFS;
import com.supermap.realestate.registration.util.ConstValue.DJDYLY;
import com.supermap.realestate.registration.util.ConstValue.DJLX;
import com.supermap.realestate.registration.util.ConstValue.DJZT;
import com.supermap.realestate.registration.util.ConstValue.QLLX;
import com.supermap.realestate.registration.util.ConstValue.SF;
import com.supermap.realestate.registration.util.ConstValue.SQRLB;
import com.supermap.realestate.registration.util.ConstValue.ZSBS;
import com.supermap.realestate.registration.util.Global;
import com.supermap.realestate.registration.util.ObjectHelper;
import com.supermap.realestate.registration.util.ProjectHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.realestate.registration.util.exportXmlUtil;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;

/*
 1、现房抵押预告转现房抵押登记
 */

/**
 * 现房抵押预告转现房抵押登记处理类
 * @ClassName: XFYDYZXFDYDJHandler
 * @author liushufeng
 * @date 2016年3月21日 下午3:54:17
 */
public class XFYDYZXFDYDJHandler extends DJHandlerBase implements DJHandler {

	/**
	 * 构造函数
	 * 
	 * @param info
	 */
	public XFYDYZXFDYDJHandler(ProjectInfo info) {
		super(info);
	}

	/**
	 * 添加不动产单元
	 */
	@Override
	public boolean addBDCDY(String qlids) {
		boolean bsuccess = false;
		if (StringHelper.isEmpty(qlids)) {
			return false;
		}
		String[] listqlid = qlids.split(",");
		if (listqlid == null || listqlid.length <= 0)
			return false;
		
		CommonDao dao = getCommonDao();
		List<String> listDJDY = new ArrayList<String>();
		for (int iql = 0; iql < listqlid.length; iql++) {
			String qlid = listqlid[iql];
			Rights ql = RightsTools.loadRights(DJDYLY.XZ, qlid);
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
				
				
				BDCS_QL_GZ bdcs_ql_gz = CopyQLXXFromXZ(qlid);
				gzqlid = bdcs_ql_gz.getId();						
				CopySQRFromXZQLR(qlid, gzqlid); //这种方式没把上一首的义务人拿过来 先注销后面看是否有问题wuzhu
				CopyYWRFromXZQLR(ql.getDJDYID(),gzqlid);
				//if(StringUtils.isEmpty(ql.getXMBH()))//如果项目编号为空按原有方式提前 WUZHU
				//	CopySQRFromXZQLR(qlid, gzqlid);
				//	else
				//		CopyYWRFromXZQLR(ql.getXMBH(),gzqlid);
				ql.setDJZT(DJZT.DJZ.Value);
				getCommonDao().update(ql);
			}
		}
		dao.flush();
		bsuccess = true;
		return bsuccess;
	}

	/**
	 * 根据现状权利ID和工作权利ID拷贝现状权利人到申请人
	 * @Author：俞学斌
	 * @param xzqlid
	 * 
	 * @param gzqlid
	 *            工作权利ID
	 */
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
			}
		}
	}
	/**
	 * 拷贝义务人（产权人）进入
	 * @Author：wuzhu
	 * @param xzqlid
	 * 
	 * @param gzqlid
	 *            工作权利ID
	 */
	private void CopyYWRFromXZQLR(String djdyid, String gzqlid) {
		StringBuilder builderQLR = new StringBuilder();
		builderQLR.append("SELECT * FROM BDCK.BDCS_QLR_XZ WHERE QLID IN(SELECT QLID FROM BDCK.BDCS_QL_XZ WHERE DJDYID ='");
		builderQLR.append(djdyid);
		builderQLR.append("'");
		builderQLR.append(" AND QLLX IN(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18))");
		List<Map> qlrs_xz = getCommonDao().getDataListByFullSql(builderQLR.toString());
		if (qlrs_xz == null || qlrs_xz.size() <= 0) {
			return;
		}
		for (int iqlr = 0; iqlr < qlrs_xz.size(); iqlr++) {
			Map qlr = qlrs_xz.get(iqlr);
			String SQRID = getPrimaryKey();
			BDCS_SQR sqr = new BDCS_SQR();
			sqr.setGYFS(qlr.get("GYFS")==null?"":qlr.get("GYFS").toString());
			sqr.setFZJG(qlr.get("FZJG")==null?"":qlr.get("FZJG").toString());
			sqr.setGJDQ(qlr.get("GJ")==null?"":qlr.get("GJ").toString());
			sqr.setGZDW(qlr.get("GZDW")==null?"":qlr.get("GZDW").toString());
			sqr.setXB(qlr.get("XB")==null?"":qlr.get("XB").toString());
			sqr.setHJSZSS(qlr.get("HJSZSS")==null?"":qlr.get("HJSZSS").toString());
			sqr.setSSHY(qlr.get("SSHY")==null?"":qlr.get("SSHY").toString());
			sqr.setYXBZ(qlr.get("YXBZ")==null?"":qlr.get("YXBZ").toString());
			sqr.setQLBL(qlr.get("QLBL")==null?"":qlr.get("QLBL").toString());
			sqr.setQLMJ(StringHelper.formatObject(qlr.get("QLMJ")));
			sqr.setSQRXM(qlr.get("QLRMC")==null?"":qlr.get("QLRMC").toString());
			sqr.setSQRLB(SQRLB.JF.Value);
			sqr.setSQRLX(qlr.get("QLRLX")==null?"":qlr.get("QLRLX").toString());
			sqr.setDZYJ(qlr.get("DZYJ")==null?"":qlr.get("DZYJ").toString());
			sqr.setLXDH(qlr.get("DH")==null?"":qlr.get("DH").toString());
			sqr.setZJH(qlr.get("ZJH")==null?"":qlr.get("ZJH").toString());
			sqr.setZJLX(qlr.get("ZJZL")==null?"":qlr.get("ZJZL").toString());
			sqr.setTXDZ(qlr.get("DZ")==null?"":qlr.get("DZ").toString());
			sqr.setYZBM(qlr.get("YB")==null?"":qlr.get("YB").toString());
			sqr.setXMBH(getXMBH());
			sqr.setId(SQRID);
			sqr.setGLQLID(gzqlid);
			getCommonDao().save(sqr);
		}
	}
	
	/**
	 * 拷贝上一首申请人（暂时不用该方法）
	 * @Author：wuzhu
	 * @param xzqlid
	 * 
	 * @param gzqlid
	 *            工作权利ID
	 */
	private void CopyYWRFromSQR(String xzxmbh, String gzqlid) {
		StringBuilder builderQLR = new StringBuilder();
		builderQLR.append("XMBH ='");
		builderQLR.append(xzxmbh);
		builderQLR.append("'");
		List<BDCS_SQR> sqrs_xz = getCommonDao().getDataList(BDCS_SQR.class, builderQLR.toString());
		if (sqrs_xz == null || sqrs_xz.size() <= 0) {
			return;
		}
		for (int isqr = 0; isqr < sqrs_xz.size(); isqr++) {
			BDCS_SQR sqr_old =sqrs_xz.get(isqr);
				String SQRID = getPrimaryKey();
				BDCS_SQR sqr_new =new BDCS_SQR();
				sqr_new.setGYFS(sqr_old.getGYFS());
				sqr_new.setFZJG(sqr_old.getFZJG());
				sqr_new.setGJDQ(sqr_old.getGJDQ());
				sqr_new.setGZDW(sqr_old.getGZDW());
				sqr_new.setXB(sqr_old.getXB());
				sqr_new.setHJSZSS(sqr_old.getHJSZSS());
				sqr_new.setSSHY(sqr_old.getSSHY());
				sqr_new.setYXBZ(sqr_old.getYXBZ());
				sqr_new.setQLBL(sqr_old.getQLBL());
				sqr_new.setQLMJ(sqr_old.getQLMJ());
				sqr_new.setSQRXM(sqr_old.getSQRXM());
				sqr_new.setSQRLB(sqr_old.getSQRLB());
				sqr_new.setSQRLX(sqr_old.getSQRLX());
				sqr_new.setDZYJ(sqr_old.getDZYJ());
				sqr_new.setLXDH(sqr_old.getLXDH());
				sqr_new.setZJH(sqr_old.getZJH());
				sqr_new.setZJLX(sqr_old.getZJLX());
				sqr_new.setTXDZ(sqr_old.getTXDZ());
				sqr_new.setYZBM(sqr_old.getYZBM());
				sqr_new.setXMBH(getXMBH());
				sqr_new.setId(SQRID);
				sqr_new.setGLQLID(gzqlid);
				getCommonDao().save(sqr_new);
		}
	}
	/**
	 * 写入登记簿
	 */
	@Override
	public boolean writeDJB() {

		if (super.isCForCFING()) {
			return false;
		}

		String dbr = Global.getCurrentUserName();
		Date djsj = new Date();
		String strSqlXMBH = ProjectHelper.GetXMBHCondition(getXMBH());
		List<Rights> lstrights = RightsTools.loadRightsByCondition(DJDYLY.GZ, strSqlXMBH);
		for (Rights right : lstrights) {
			right.setDBR(dbr);
			right.setDJSJ(djsj);
			getCommonDao().update(right);
			// 根据权利ID删除权利，附属权利，证书，权地证人，权利人信息
			RightsTools.deleteRightsAll(DJDYLY.XZ, right.getLYQLID());
			Rights ql_xz = RightsTools.copyRightsAll(DJDYLY.GZ, DJDYLY.XZ, right.getId());
			Rights ql_ls = RightsTools.copyRightsAll(DJDYLY.GZ, DJDYLY.LS, right.getId());
			getCommonDao().save(ql_xz);
			getCommonDao().save(ql_ls);
			// 更新历史附属权利信息
			SubRights subright = RightsTools.loadSubRightsByRightsID(DJDYLY.LS, right.getLYQLID());
			if (subright != null) {
				subright.setZXSJ(djsj);
				subright.setZXDBR(dbr);
				subright.setZXDYYWH(getProject_id());
				getCommonDao().update(subright);
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
				if (QLLX.DIYQ.Value.equals(right.getQLLX()) || DJLX.YYDJ.Value.equals(right.getDJLX())) {
					tree.setDIYQQlid(right.getId());
					tree.setOlddiyqqlid(right.getLYQLID());
					tree.setQlid(right.getId());
					List<Rights> rights_syq = RightsTools.loadRightsByCondition(DJDYLY.XZ, "DJDYID='" + right.getDJDYID() + "' AND QLLX IN ('1','2','3','4','5','6','7','8')");
					if (rights_syq != null && rights_syq.size() > 0) {
						tree.setOldqlid(rights_syq.get(0).getId());
					}
				} else {
					tree.setQlid(right.getId());
					tree.setOldqlid(right.getLYQLID());
				}
			}
		}
		return list;
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
	
	
	protected BDCS_QL_GZ CopyQLXXFromXZ(String qlid) {
		BDCS_QL_GZ bdcs_ql_gz = null;
		StringBuilder builer = new StringBuilder();
		builer.append(" QLID='").append(qlid).append("'");
		String strQuery = builer.toString();
		BDCS_QL_XZ bdcs_ql_xz = getCommonDao().get(BDCS_QL_XZ.class, qlid);
		if (bdcs_ql_xz != null) {
			//是否自动继承原抵押权信息的查封,0:否，1：是
			String isextendmortgageinfo = "";
			String workflowcode = ProjectHelper.getWorkflowCodeByProjectID(getProject_id());
			List<WFD_MAPPING> listCode_dyxx = getCommonDao().getDataList(WFD_MAPPING.class, "WORKFLOWCODE='" + workflowcode + "'");
			if (listCode_dyxx != null && listCode_dyxx.size() > 0) {
				isextendmortgageinfo = listCode_dyxx.get(0).getISEXTENDMORTGAGEINFO();
			}
			// 获取是否获取重新生成权证号配置
			String newqzh = "";
			List<WFD_MAPPING> listCode_qzh = getCommonDao().getDataList(WFD_MAPPING.class, "WORKFLOWCODE='" + workflowcode + "'");
			if (listCode_qzh != null && listCode_qzh.size() > 0) {
				newqzh = listCode_qzh.get(0).getNEWQZH();
			}
			
			String gzqlid = getPrimaryKey();
			String gzfsqlid = getPrimaryKey();
			// 拷贝权利
			bdcs_ql_gz = ObjectHelper.copyQL_XZToGZ(bdcs_ql_xz);
			bdcs_ql_gz.setId(gzqlid);
			bdcs_ql_gz.setQLLX(getQllx().Value);
			if(!BDCDYLX.YCH.equals(this.getBdcdylx())){
				bdcs_ql_gz.setDJLX(getDjlx().Value);
			}else{
				if(StringHelper.isEmpty(bdcs_ql_gz.getDJLX())){
					bdcs_ql_gz.setDJLX(getDjlx().Value);
				}
			}
			bdcs_ql_gz.setFSQLID(gzfsqlid);
			bdcs_ql_gz.setXMBH(getXMBH());
			bdcs_ql_gz.setLYQLID(qlid);
			bdcs_ql_gz.setDJSJ(null);
			bdcs_ql_gz.setDBR("");
			bdcs_ql_gz.setYWH(getProject_id());
			bdcs_ql_gz.setZSBS(ZSBS.DYB.Value);// 默认单一版持证方式
			bdcs_ql_gz.setARCHIVES_BOOKNO("");
			bdcs_ql_gz.setARCHIVES_CLASSNO("");
			if (SF.YES.Value.equals(newqzh)) {
				bdcs_ql_gz.setBDCQZH("");
			}
			// bdcs_ql_gz.setDJYY("");
			// bdcs_ql_gz.setFJ("");
			if ("0".equals(isextendmortgageinfo)) {			
				bdcs_ql_gz.setQLQSSJ(null);
				bdcs_ql_gz.setQLJSSJ(null);
				bdcs_ql_gz.setDJYY(null);
				bdcs_ql_gz.setFJ(null);
			}
			BDCS_FSQL_XZ bdcs_fsql_xz = getCommonDao().get(BDCS_FSQL_XZ.class, bdcs_ql_xz.getFSQLID());
			if (bdcs_fsql_xz != null) {
				// 拷贝附属权利
				BDCS_FSQL_GZ bdcs_fsql_gz = ObjectHelper.copyFSQL_XZToGZ(bdcs_fsql_xz);
				bdcs_fsql_gz.setQLID(gzqlid);
				bdcs_fsql_gz.setId(gzfsqlid);
				bdcs_fsql_gz.setXMBH(getXMBH());
				//是否自动继承原抵押权信息的查封,0:否，1：是				
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

			// 获取证书集合
			StringBuilder builderZSALL = new StringBuilder();
			builderZSALL.append(" id IN (");
			builderZSALL.append("select ZSID FROM BDCS_QDZR_XZ WHERE QLID ='");
			builderZSALL.append(qlid).append("')");
			String strQueryZSALL = builderZSALL.toString();
			List<BDCS_ZS_XZ> zssALL = getCommonDao().getDataList(BDCS_ZS_XZ.class, strQueryZSALL);
			// 当证书个数大于1时，持证方式为分别持证，否则为共同持证
			if (zssALL.size() > 1) {
				bdcs_ql_gz.setCZFS(CZFS.FBCZ.Value);
			} else {
				if (StringHelper.isEmpty(bdcs_ql_gz.getCZFS()) || (!bdcs_ql_gz.getCZFS().equals(CZFS.FBCZ.Value) && !bdcs_ql_gz.getCZFS().equals(CZFS.GTCZ.Value))) {
					bdcs_ql_gz.setCZFS(CZFS.GTCZ.Value);
				}
			}
			Map<String, String> lyzsid_zsid = new HashMap<String, String>();

			StringBuilder builderDJDY = new StringBuilder();
			builderDJDY.append(" DJDYID='");
			builderDJDY.append(bdcs_ql_xz.getDJDYID());
			builderDJDY.append("'");
			// 获取权利人集合
			List<BDCS_QLR_XZ> qlrs = getCommonDao().getDataList(BDCS_QLR_XZ.class, strQuery);
			if (qlrs != null && qlrs.size() > 0) {
				for (int iqlr = 0; iqlr < qlrs.size(); iqlr++) {
					BDCS_QLR_XZ bdcs_qlr_xz = qlrs.get(iqlr);
					if (bdcs_qlr_xz != null) {
						String gzqlrid = "";					
						if ("1".equals(isextendmortgageinfo)) {
							gzqlrid = getPrimaryKey();
							BDCS_QLR_GZ bdcs_qlr_gz = ObjectHelper.copyQLR_XZToGZ(bdcs_qlr_xz);
							bdcs_qlr_gz.setId(gzqlrid);
							bdcs_qlr_gz.setQLID(gzqlid);
							bdcs_qlr_gz.setXMBH(getXMBH());
							if (SF.YES.Value.equals(newqzh)) {
								bdcs_qlr_gz.setBDCQZHXH("");
								bdcs_qlr_gz.setBDCQZH("");
							}
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
									bdcs_zs_gz.setId(gzzsid);
									bdcs_zs_gz.setXMBH(getXMBH());
									if (SF.YES.Value.equals(newqzh)) {
										bdcs_zs_gz.setBDCQZH("");
										bdcs_zs_gz.setZSBH("");
									}
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
											bdcs_qdzr_gz.setZSID(lyzsid_zsid.get(bdcs_zs_xz.getId()));
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
			getCommonDao().save(bdcs_ql_gz);
			bdcs_ql_xz.setDJZT("02");
			getCommonDao().update(bdcs_ql_xz);
		}
		return bdcs_ql_gz;
	}
	
	@Override
	public Map<String, String> exportXML(String path, String actinstID) {
		return exportXmlUtil.createXMLAndUp(path, actinstID);
	}
}
