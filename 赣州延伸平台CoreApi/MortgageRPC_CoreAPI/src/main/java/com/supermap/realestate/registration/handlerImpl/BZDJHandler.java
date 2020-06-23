package com.supermap.realestate.registration.handlerImpl;

import com.supermap.realestate.registration.ViewClass.ProjectInfo;
import com.supermap.realestate.registration.ViewClass.UnitStatus;
import com.supermap.realestate.registration.ViewClass.UnitTree;
import com.supermap.realestate.registration.dataExchange.*;
import com.supermap.realestate.registration.dataExchange.fwsyq.*;
import com.supermap.realestate.registration.dataExchange.shyq.*;
import com.supermap.realestate.registration.dataExchange.syq.QLFQLTDSYQ;
import com.supermap.realestate.registration.dataExchange.utils.packageXml;
import com.supermap.realestate.registration.handler.DJHandler;
import com.supermap.realestate.registration.model.*;
import com.supermap.realestate.registration.model.interfaces.*;
import com.supermap.realestate.registration.model.xmlExportmodel.MessageExport;
import com.supermap.realestate.registration.service.impl.ProjectServiceImpl;
import com.supermap.realestate.registration.tools.RightsHolderTools;
import com.supermap.realestate.registration.tools.RightsTools;
import com.supermap.realestate.registration.tools.UnitTools;
import com.supermap.realestate.registration.util.*;
import com.supermap.realestate.registration.util.ConstValue.*;
import com.supermap.wisdombusiness.core.SuperSpringContext;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import org.springframework.util.StringUtils;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.File;
import java.text.MessageFormat;
import java.util.*;

/*
 1、国有建设用地使用权换证登记
 2、集体建设用地使用权换证登记（未配置）
 3、宅基地使用权换证登记（未配置）
 4、国有建设用地使用权/房屋所有权换证登记
 5、集体建设用地使用权/房屋所有权换证登记（未配置）
 6、宅基地使用权/房屋所有权换证登记（未配置）
 */
/**
 * 
 * 换证登记处理类
 * @ClassName: BZDJHandler
 * @author yuxuebin
 * @date 2015年9月8日 下午10:36:06
 */
public class BZDJHandler extends DJHandlerBase implements DJHandler {

	/**
	 * 构造函数
	 * 
	 * @param info
	 */
	public BZDJHandler(ProjectInfo info) {
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
		// 获取是否获取重新生成权证号配置
		String newqzh = "";
		String workflowcode = ProjectHelper.getWorkflowCodeByProjectID(this.getProject_id());
		List<WFD_MAPPING> listCode = getCommonDao().getDataList(WFD_MAPPING.class, "WORKFLOWCODE='" + workflowcode + "'");
		if (listCode != null && listCode.size() > 0) {
			newqzh = listCode.get(0).getNEWQZH();
		}
		CommonDao dao = getCommonDao();
		List<String> listDJDY = new ArrayList<String>();
		List<String> xmbhLists = new ArrayList<String>();
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
				String gzqlid = "";
				if (SF.NO.Value.equals(newqzh)) {
					// 拷贝权利信息（权证号不为空）
					BDCS_QL_GZ bdcs_ql_gz = super.CopyQLXXFromXZ(qlid);
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
					BDCS_QL_GZ bdcs_ql_gz = super.CopyQLXXFromXZExceptBDCQZH(qlid);
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
				
				//桂林需求-做预告登记的更正登记业务时，需将预告登记前义务人拷贝到申请人 
				String xzqdm =ConfigHelper.getNameByValue("XZQHDM");
				if("450".equals(xzqdm.substring(0, 3)) && (DJLX.YGDJ.Value.equals(ql.getDJLX())||DJLX.GZDJ.Value.equals(ql.getDJLX())
						||(DJLX.QTDJ.Value.equals(ql.getDJLX())&&QLLX.DIYQ.Value.equals(ql.getQLLX())))){					
					if (!StringHelper.isEmpty(ql.getXMBH()) && !xmbhLists.contains(ql.getXMBH())) {	
							xmbhLists.add(ql.getXMBH());
							List<BDCS_SQR> ywrLists = new ArrayList<BDCS_SQR>();
							ywrLists = getCommonDao().getDataList(BDCS_SQR.class, " XMBH = '" + ql.getXMBH() + "' AND SQRLB='" + SQRLB.YF.Value + "'");
							if(ywrLists != null && ywrLists.size()>0){
								for(BDCS_SQR ywr : ywrLists){									
										BDCS_SQR ywr_new = new BDCS_SQR();
										ObjectHelper.copyObject(ywr, ywr_new);
										String sqrid_new = getPrimaryKey();
										ywr_new.setId(sqrid_new);
										ywr_new.setXMBH(getXMBH());
										dao.save(ywr_new);														
								}
						}
					}else{
						CopySQRFromXZCQQLR(ql.getDJDYID(),gzqlid);
					}
				}
				// 没进行
				dao.flush();
				//CopySQRFromXZQLR(qlid, gzqlid);
				CopySQRFromGZQLR(qlid, gzqlid);
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
	
	/**
	 * 根据djdyid拷贝义务人到申请人
	 * @author liangc
	 * @param djdyid
	 * @param gzqlid
	 */
	@SuppressWarnings("unused")
	private void CopySQRFromXZCQQLR(String djdyid, String gzqlid) {
		StringBuilder builderQLR = new StringBuilder();
		builderQLR.append("DJDYID ='");
		builderQLR.append(djdyid);
		builderQLR.append("'");
		builderQLR.append("AND QLLX IN('1','2','3','4','5','6','7','8','9','10','11','12','13','14','15','16','17','18','24')");
		List<BDCS_QL_XZ> qls = getCommonDao().getDataList(BDCS_QL_XZ.class, builderQLR.toString());
		List<BDCS_SQR> sqrs = getCommonDao().getDataList(BDCS_SQR.class,"XMBH='"+ StringHelper.formatObject( qls.get(0).getXMBH())+ "'");

		if (qls == null || qls.size() <= 0) {
			return;
		}
		for (int isqr = 0; isqr < sqrs.size(); isqr++) {
			BDCS_SQR sqr_now = sqrs.get(isqr);
			// 通过权利人名称和权利人证件号进行过滤，相同的权利人不重复添加到申请人。
			String zjhm = sqr_now.getZJH();
			boolean bexists = false;
			if (!StringHelper.isEmpty(sqr_now.getSQRXM())) {
				String Sql = "";
				if (!StringHelper.isEmpty(zjhm)) {
					Sql = MessageFormat.format(" XMBH=''{0}'' AND SQRXM=''{1}'' AND ZJH=''{2}''", getXMBH(), sqr_now.getSQRXM(), zjhm);
				} else {
					Sql = MessageFormat.format(" XMBH=''{0}'' AND SQRXM=''{1}'' AND ZJH IS NULL", getXMBH(), sqr_now.getSQRXM());
				}
				List<BDCS_SQR> sqrlist = getCommonDao().getDataList(BDCS_SQR.class, Sql);
				if (sqrlist != null && sqrlist.size() > 0) {
					bexists = true;
				}
			}
			if (!bexists) {
				String SQRID = getPrimaryKey();
				BDCS_SQR sqr = new BDCS_SQR();
				sqr.setGYFS(sqr_now.getGYFS());
				sqr.setFZJG(sqr_now.getFZJG());
				sqr.setGJDQ(sqr_now.getGJDQ());
				sqr.setGZDW(sqr_now.getGZDW());
				sqr.setXB(sqr_now.getXB());
				sqr.setHJSZSS(sqr_now.getHJSZSS());
				sqr.setSSHY(sqr_now.getSSHY());
				sqr.setYXBZ(sqr_now.getYXBZ());
				sqr.setQLBL(sqr_now.getQLBL());
				sqr.setQLMJ(StringHelper.formatObject(sqr_now.getQLMJ()));
				sqr.setSQRXM(sqr_now.getSQRXM());
				sqr.setSQRLB(SQRLB.YF.Value);
				sqr.setSQRLX(sqr_now.getSQRLX());
				sqr.setDZYJ(sqr_now.getDZYJ());
				sqr.setLXDH(sqr_now.getLXDH());
				sqr.setZJH(sqr_now.getZJH());
				sqr.setZJLX(sqr_now.getZJLX());
				sqr.setTXDZ(sqr_now.getTXDZ());
				sqr.setYZBM(sqr_now.getYZBM());
				sqr.setXMBH(getXMBH());
				sqr.setId(SQRID);
				sqr.setGLQLID(gzqlid);
				getCommonDao().save(sqr);
			}
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
		if ((listCode != null) && (listCode.size() > 0)) {
			String cfconfig = listCode.get(0).getCFCONFIG();
			if("1".equals(cfconfig)){
				cfflag = false;
			}
		}
		if(cfflag){
			if (super.isCForCFING()) {
				return false;
			}
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
		//删除义务人
		List<BDCS_SQR> ywrLists = getCommonDao().getDataList(BDCS_SQR.class, "XMBH='"+getXMBH()+"' AND SQRLB='"+SQRLB.YF.Value+"'");
		if(ywrLists != null && ywrLists.size() >0){
			for(BDCS_SQR ywr : ywrLists){
				getCommonDao().deleteEntity(ywr);
			}
		}

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
			ProjectInfo info = ProjectHelper.GetPrjInfoByXMBH(StringHelper.formatObject(right.getXMBH()));
			if (right != null) {
				if (QLLX.DIYQ.Value.equals(right.getQLLX()) || DJLX.YYDJ.Value.equals(right.getDJLX())
						) {
					tree.setDIYQQlid(right.getId());
					tree.setOlddiyqqlid(right.getLYQLID());
					tree.setQlid(right.getId());
					List<Rights> rights_syq = RightsTools.loadRightsByCondition(DJDYLY.XZ, "DJDYID='" + right.getDJDYID() + "' AND QLLX IN ('1','2','3','4','5','6','7','8','15','24','36')");
					if (info.getBaseworkflowcode().equals("BZ306")){
						rights_syq = RightsTools.loadRightsByCondition(DJDYLY.XZ, "DJDYID='" + right.getDJDYID() + "' AND QLLX IN ('99')");
					}
					if (rights_syq != null && rights_syq.size() > 0) {
						tree.setOldqlid(rights_syq.get(0).getId());
					}
				}else if (info.getBaseworkflowcode().equals("BZ005")||info.getBaseworkflowcode().equals("BZ006")) {
					tree.setOlddiyqqlid(right.getLYQLID());
					tree.setQlid(right.getId());
					List<Rights> rights_syq = RightsTools.loadRightsByCondition(DJDYLY.XZ, "DJDYID='" + right.getDJDYID() + "' AND QLLX IN ('1','2','3','4','5','6','7','8','24','36')");
					if (rights_syq != null && rights_syq.size() > 0) {
						tree.setOldqlid(rights_syq.get(0).getId());
					}
				}else {
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
		
		//桂林需求-更正登记，把义务人添加到fsql_gz的ywr字段
		if("450300".equals(ConfigHelper.getNameByValue("XZQHDM"))){
			CommonDao baseCommonDao= super.getCommonDao();
			BDCS_QL_GZ ql = baseCommonDao.get(BDCS_QL_GZ.class, qlid);		
			if (ql!=null && DJLX.GZDJ.Value.equals(ql.getDJLX()) && !QLLX.QTQL.Value.equals(ql.getQLLX())
					&& !QLLX.DIYQ.Value.equals(ql.getQLLX())) {						
				List<BDCS_FSQL_GZ> fsqlList = baseCommonDao.getDataList(BDCS_FSQL_GZ.class, "QLID='" + qlid + "'");
				if (fsqlList.size() > 0) {

					StringBuilder ywr = new StringBuilder();
					StringBuilder ywrzjlx = new StringBuilder();
					StringBuilder ywrzjh = new StringBuilder();
					List<BDCS_SQR> ywrlist = baseCommonDao.getDataList(BDCS_SQR.class, " XMBH='" + ql.getXMBH() + "' AND SQRLB='2'");
					for (int i = 0; i < ywrlist.size(); i++) {
						if (i != 0) {
							ywr.append("/");
							ywrzjlx.append("/");
							ywrzjh.append("/");
						}
						ywr.append(ywrlist.get(i).getSQRXM());
						ywrzjlx.append(ywrlist.get(i).getZJLX());
						ywrzjh.append(ywrlist.get(i).getZJH());
					}
						
					for (int i = 0; i < fsqlList.size(); i++) {
						BDCS_FSQL_GZ fsql_GZ = fsqlList.get(i);
						fsql_GZ.setYWR(ywr.toString());
						fsql_GZ.setYWRZJZL(ywrzjlx.toString());
						fsql_GZ.setYWRZJH(ywrzjh.toString());
						baseCommonDao.update(fsql_GZ);
					}					
				}			
			}
			baseCommonDao.flush();
		}	
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

	@SuppressWarnings("unused")
	@Override
	public Map<String, String> exportXML(String path, String actinstID) {
		if("LD".equals(super.getBdcdylx().toString())||"HY".equals(super.getBdcdylx().toString())){
			return super.exportXMLother(path, actinstID,"YES");
		}
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

					System.out.println(super.getBdcdylx().toString());
					if (QLLX.GYJSYDSHYQ.Value.equals(this.getQllx().Value) || QLLX.ZJDSYQ.Value.equals(this.getQllx().Value) || QLLX.JTJSYDSYQ.Value.equals(this.getQllx().Value)
							||"SHYQZD".equals(super.getBdcdylx().toString())) { // 国有建设使用权、宅基地、集体建设用地使用权
						try {
							RealUnit unit=UnitTools.loadUnit(BDCDYLX.SHYQZD, DJDYLY.GZ, djdy.getBDCDYID());
							UseLand zd=(UseLand)unit;
							List<String> preEstateNum=new ArrayList<String>();
							// 目前未维护的字段先手动赋值 diaoliwei 2015-10-15
							if (null != zd) {
								if (StringUtils.isEmpty(zd.getZDT())) {
									zd.setZDT("无");
								}
							}
							for (int j = 0; j < bgqdjdys.size(); j++) {
								BDCS_DJDY_GZ bgqdjdy = bgqdjdys.get(j);
								RealUnit unit_XZ=UnitTools.loadUnit(BDCDYLX.SHYQZD, DJDYLY.LS, bgqdjdy.getBDCDYID());
								if(!StringHelper.isEmpty(unit_XZ)&&!StringHelper.isEmpty(unit_XZ.getBDCDYH())&&!preEstateNum.contains(unit_XZ.getBDCDYH())){
									preEstateNum.add(unit_XZ.getBDCDYH());
								}
							}
							// 标记单元号是否发生变化了，是否取消空间节点的存在
							boolean flag = false;
							if (!(zd.getBDCDYH().equals(preEstateNum)) && !StringUtils.isEmpty(preEstateNum)) {
								flag = true;
							}
							Message msg = exchangeFactory.createMessage(flag);
							msg.getData().setKTTGYJZX(packageXml.getKTTGYJZX(msg.getData().getKTTGYJZX(),zd.getZDDM()));
							msg.getData().setKTTGYJZD(packageXml.getKTTGYJZD(msg.getData().getKTTGYJZD(), zd.getZDDM()));

							super.fillHead(msg, i, ywh,zd.getBDCDYH(),zd.getQXDM(),ql.getLYQLID());

							msg.getHead().setParcelID(StringHelper.formatObject(zd.getZDDM()));
							msg.getHead().setEstateNum(StringHelper.formatObject(zd.getBDCDYH()));// 当前的不动产单元号
//							msg.getHead().setPreEstateNum(StringHelper.formatList(preEstateNum, ","));// 上一首的不动产单元号
							if (zd != null && !StringUtils.isEmpty(zd.getQXDM())) {
								msg.getHead().setAreaCode(zd.getQXDM());
							}
							if (djdy != null) {

								super.fillShyqZdData(msg,ywh,zd,sqrs,qlrs,ql,xmxx,actinstID);
								
								// 如果当前不动产单元号和上一个不动产单元号不同的话，把空间节点加进去
								if (!(preEstateNum.contains(zd.getBDCDYH()))) {
									
								}
								
							}
							List<ZDK103> zdk = msg.getData().getZDK103();
							zdk = packageXml.getZDK103(zdk, zd, null, null);
							msg.getData().setZDK103(zdk);
							msg.getHead().setRecType("3000301");
							msgName = "Biz" + msg.getHead().getBizMsgID() + ".xml";
							mashaller.marshal(msg, new File(path + msgName));
							result = uploadFile(path + msgName, ConstValue.RECCODE.JSYDSHYQ_BGDJ.Value,actinstID,djdy.getDJDYID(),ql.getId());
							names.put(djdy.getDJDYID(), msg.getHead().getBizMsgID() + ".xml");
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					if (QLLX.GYJSYDSHYQ_FWSYQ.Value.equals(this.getQllx().Value)||QLLX.JTJSYDSYQ_FWSYQ.Value.equals(this.getQllx().Value)||QLLX.ZJDSYQ_FWSYQ.Value.equals(this.getQllx().Value)
							||"H".equals(super.getBdcdylx().toString())) { // 房屋所有权
						try {
							House h = dao.get(BDCS_H_XZ.class, djdy.getBDCDYID());
							BDCS_ZRZ_XZ zrz_xz = null;
							if (h != null && !StringUtils.isEmpty(h.getZRZBDCDYID())) {
								zrz_xz = dao.get(BDCS_ZRZ_XZ.class, h.getZRZBDCDYID());
								if(zrz_xz != null){
									zrz_xz.setGHYT(h.getGHYT()); //自然幢的ghyt取户的ghyt
									zrz_xz.setFWJG(zrz_xz.getFWJG() == null || zrz_xz.getFWJG().equals("") ? h.getFWJG() : zrz_xz.getFWJG());
								}
							}
							BDCS_LJZ_XZ ljz_xz = null;
							if (h != null && !StringUtils.isEmpty(h.getLJZID())) {
								ljz_xz = dao.get(BDCS_LJZ_XZ.class, h.getLJZID());
							}
							if (h != null) {
								BDCS_SHYQZD_XZ zd = dao.get(BDCS_SHYQZD_XZ.class, h.getZDBDCDYID());
								h.setZDDM(zd.getZDDM());
								if(zrz_xz != null){
									zrz_xz.setZDDM(zd.getZDDM());
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
								super.fillFwData(msg, ywh, ljz_xz, c, zrz_xz, h, sqrs, qlrs, ql, xmxx, actinstID);
							}
							msg.getHead().setRecType("3000402");
							msgName = "Biz" + msg.getHead().getBizMsgID() + ".xml";
							mashaller.marshal(msg, new File(path + msgName));
							result = uploadFile(path + msgName, ConstValue.RECCODE.FDCQDZ_BGDJ.Value,actinstID,djdy.getDJDYID(),ql.getId());
							names.put(djdy.getDJDYID(), msg.getHead().getBizMsgID() + ".xml");
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					if (QLLX.JTTDSYQ.Value.equals(this.getQllx().Value)) { // 集体土地所有权
						try {
							BDCS_SYQZD_XZ oland = dao.get(BDCS_SYQZD_XZ.class,
									djdy.getBDCDYID());
							Message msg = exchangeFactory
									.createMessageByTDSYQ();
							super.fillHead(msg, i, ywh,oland.getBDCDYH(),oland.getQXDM(),ql.getLYQLID());
							msg.getHead().setParcelID(
									StringHelper.formatObject(oland.getZDDM()));
							msg.getHead()
									.setEstateNum(
											StringHelper.formatObject(oland
													.getBDCDYH()));
//							msg.getHead()
//									.setPreEstateNum(
//											StringHelper.formatObject(oland
//													.getBDCDYH()));
							if (oland != null
									&& !StringUtils.isEmpty(oland.getQXDM())) {
								msg.getHead().setAreaCode(oland.getQXDM());
							}
							if (djdy != null) {

                                super.fillSyqZdData( msg, ywh, oland, sqrs, qlrs, ql, xmxx, actinstID) ;
                                
								List<ZDK103> zdk = msg.getData().getZDK103();
								zdk = packageXml.getZDK103(zdk, null, oland,
										null);
							}
							msgName = "Biz" + msg.getHead().getBizMsgID()
									+ ".xml";
							names.put(djdy.getDJDYID(), msg.getHead()
									.getBizMsgID() + ".xml");
							mashaller.marshal(msg, new File(path + msgName));
							result = uploadFile(path + msgName,
									ConstValue.RECCODE.TDSYQ_CSDJ.Value,
									actinstID, djdy.getId(), ql.getId());
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					if(null == result || result.equals("")){
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
}
