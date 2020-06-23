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
import com.supermap.realestate.registration.dataExchange.DJFDJSH;
import com.supermap.realestate.registration.dataExchange.DJFDJSJ;
import com.supermap.realestate.registration.dataExchange.DJFDJSQR;
import com.supermap.realestate.registration.dataExchange.DJFDJSZ;
import com.supermap.realestate.registration.dataExchange.DJTDJSLSQ;
import com.supermap.realestate.registration.dataExchange.Message;
import com.supermap.realestate.registration.dataExchange.ZTTGYQLR;
import com.supermap.realestate.registration.dataExchange.exchangeFactory;
import com.supermap.realestate.registration.dataExchange.dyq.QLFQLDYAQ;
import com.supermap.realestate.registration.dataExchange.utils.packageXml;
import com.supermap.realestate.registration.handler.DJHandler;
import com.supermap.realestate.registration.model.BDCS_DJDY_GZ;
import com.supermap.realestate.registration.model.BDCS_DJDY_LS;
import com.supermap.realestate.registration.model.BDCS_DJDY_XZ;
import com.supermap.realestate.registration.model.BDCS_FSQL_GZ;
import com.supermap.realestate.registration.model.BDCS_H_GZ;
import com.supermap.realestate.registration.model.BDCS_QDZR_GZ;
import com.supermap.realestate.registration.model.BDCS_QLR_GZ;
import com.supermap.realestate.registration.model.BDCS_QLR_XZ;
import com.supermap.realestate.registration.model.BDCS_QL_GZ;
import com.supermap.realestate.registration.model.BDCS_SHYQZD_GZ;
import com.supermap.realestate.registration.model.BDCS_SHYQZD_XZ;
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
import com.supermap.realestate.registration.util.ConfigHelper;
import com.supermap.realestate.registration.util.ConstValue;
import com.supermap.realestate.registration.util.ConstValue.BDCDYLX;
import com.supermap.realestate.registration.util.ConstValue.DJDYLY;
import com.supermap.realestate.registration.util.ConstValue.DJZT;
import com.supermap.realestate.registration.util.ConstValue.SF;
import com.supermap.realestate.registration.util.ConstValue.SQRLB;
import com.supermap.realestate.registration.util.Global;
import com.supermap.realestate.registration.util.ObjectHelper;
import com.supermap.realestate.registration.util.ProjectHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.realestate.registration.util.YwLogUtil;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;

/*
 	抵押权变更登记，可同时处理抵押权变更和抵押权注销
 */
/**
 * 
 * 抵押变更登记处理类
 * @ClassName: DYBGDJHandler2
 * @author yuxuebin
 * @date 2016年01月10日 下午02:25:16
 */
public class DYBGDJHandler2 extends DJHandlerBase implements DJHandler {

	/**
	 * 构造函数
	 * 
	 * @param info
	 */
	public DYBGDJHandler2(ProjectInfo info) {
		super(info);
	}

	/**
	 * 添加不动产单元
	 */
	@Override
	public boolean addBDCDY(String bdcqzh) {
		boolean badd=false;
		this.removeBDCDY("");
		BDCS_XMXX xmxx=Global.getXMXXbyXMBH(super.getXMBH());
		if(xmxx!=null){
			xmxx.setSFHBZS("1");
			getCommonDao().update(xmxx);
		}
		List<Rights> qls=RightsTools.loadRightsByCondition(DJDYLY.XZ, "QLLX='23' AND BDCQZH='"+bdcqzh+"'");
		if(qls!=null&&qls.size()>0){
			List<String> listDJDY = new ArrayList<String>();
			for(Rights ql:qls){
				// 拷贝登记单元
				if (!StringHelper.isEmpty(ql.getDJDYID()) && !listDJDY.contains(ql.getDJDYID())) {
					ql.setDJLX("300");
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
					getCommonDao().save(ql);
				}
				String newqzh = "";
				String workflowcode = ProjectHelper.getWorkflowCodeByProjectID(this
						.getProject_id());
				List<WFD_MAPPING> listCode = getCommonDao().getDataList(
						WFD_MAPPING.class, "WORKFLOWCODE='" + workflowcode + "'");
				if (listCode != null && listCode.size() > 0) {
					newqzh = listCode.get(0).getNEWQZH();
				}
				String gzqlid = "";
				if (SF.YES.Value.equals(newqzh)) {
					// 拷贝权利信息（权证号为空）
					BDCS_QL_GZ bdcs_ql_gz = super.CopyQLXXFromXZExceptBDCQZH(ql.getId());
					bdcs_ql_gz.setYWH(this.getProject_id());
					bdcs_ql_gz.setISCANCEL("0");
					gzqlid = bdcs_ql_gz.getId();
					this.getCommonDao().save(bdcs_ql_gz);
				} else {
					// 拷贝权利信息（权证号不为空）
					BDCS_QL_GZ bdcs_ql_gz = super.CopyQLXXFromXZ(ql.getId());
					bdcs_ql_gz.setYWH(this.getProject_id());
					bdcs_ql_gz.setISCANCEL("0");
					gzqlid = bdcs_ql_gz.getId();
					this.getCommonDao().save(bdcs_ql_gz);
				}
				CopySQRFromXZQLR(ql.getId(), gzqlid);
				List<Rights> rights=RightsTools.loadRightsByCondition(DJDYLY.XZ, "DJDYID='"+ql.getDJDYID()+"' AND QLLX IN ('3','4','5','6','7','8')");
				if(rights!=null&&rights.size()>0){
					List<RightsHolder> qlrs =RightsHolderTools.loadRightsHolders(DJDYLY.XZ, rights.get(0).getId());
					if(qlrs!=null&&qlrs.size()>0){
						for(RightsHolder qlr:qlrs){
							BDCS_QLR_XZ qlr_xz=(BDCS_QLR_XZ)qlr;
							BDCS_SQR sqr=super.copyXZQLRtoSQR(qlr_xz, SQRLB.YF);
							if(sqr!=null){
								sqr.setGLQLID(gzqlid);
								getCommonDao().save(sqr);
							}
						}
					}
				}
				ql.setDJZT(DJZT.DJZ.Value);
				getCommonDao().update(ql);
			}
			getCommonDao().flush();
			badd=true;
		}
		return badd;
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

	/**
	 * 写入登记薄
	 */
	@Override
	public boolean writeDJB() {
//		if (super.isCForCFING()) {
//			return false;
//		}
		CommonDao commonDao = getCommonDao();
		String dbr = Global.getCurrentUserName();
		String strSqlXMBH = ProjectHelper.GetXMBHCondition(getXMBH());
		List<Rights> lstrights = RightsTools.loadRightsByCondition(DJDYLY.GZ, strSqlXMBH);
		for (Rights right : lstrights) {
			if(!"2".equals(right.getISCANCEL())){
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
		}
		List<BDCS_DJDY_GZ> djdys = commonDao.getDataList(BDCS_DJDY_GZ.class, strSqlXMBH);
		if (djdys != null && djdys.size() > 0) {
			for (int idjdy = 0; idjdy < djdys.size(); idjdy++) {
				BDCS_DJDY_GZ bdcs_djdy_gz = djdys.get(idjdy);
				if (bdcs_djdy_gz != null) {
					String djdyid = bdcs_djdy_gz.getDJDYID();
					String bdcdyid=bdcs_djdy_gz.getBDCDYID();
					long count = commonDao.getCountByFullSql(" FROM BDCK.BDCS_DJDY_XZ WHERE BDCDYID='"+bdcdyid+"'");
					if(count<1){
						BDCS_DJDY_XZ xz = new BDCS_DJDY_XZ();
						xz=ObjectHelper.copyDJDY_GZToXZ(bdcs_djdy_gz);
						BDCS_DJDY_LS ls = new BDCS_DJDY_LS();
						ls=ObjectHelper.copyDJDY_XZToLS(xz);
						commonDao.update(xz);
						commonDao.update(ls);
					}
					Rights right = RightsTools.loadRightsByDJDYID(DJDYLY.GZ, super.getXMBH(), djdyid);
					if(right!=null){
						if("1".equals(right.getISCANCEL())){
							SubRights subright = RightsTools.loadSubRightsByRightsID(DJDYLY.GZ, right.getId());
							Rights right_old = RightsTools.loadRights(DJDYLY.GZ, right.getLYQLID());
							if (subright != null) {
								Date zxsj = new Date();
								subright.setZXDYYWH(this.getProject_id());
								subright.setZXSJ(zxsj);
								subright.setZXDBR(dbr);
								commonDao.update(subright);
							}
							if (right_old != null) {
								right.setBDCQZH(right_old.getBDCQZH());//是注销的不动产单元，恢复原来的产权证号
								commonDao.update(right);
							}
							continue;
						}
						// 登记时间，登簿人，登记机构
						right.setDBR(dbr);
						right.setDJSJ(new Date());
						right.setDJJG(ConfigHelper.getNameByValue("DJJGMC"));
						getCommonDao().getCurrentSession().update(right);
						Rights xzql= RightsTools.copyRightsAll(DJDYLY.GZ, DJDYLY.XZ, right.getId());
						getCommonDao().save(xzql);
						Rights lsql=RightsTools.copyRightsAll(DJDYLY.GZ, DJDYLY.LS, right.getId());
						getCommonDao().save(lsql);
						
					}
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
		String sql = MessageFormat.format("  XMBH=''{0}''", xmbh);
		// 先删除登记单元
		getCommonDao().deleteEntitysByHql(BDCS_DJDY_GZ.class, sql);
		// 删除权利关联申请人
		String sqlSQR = MessageFormat.format("  XMBH=''{0}'' AND GLQLID IN (SELECT id FROM BDCS_QL_GZ WHERE XMBH=''{0}'')", xmbh);
		getCommonDao().deleteEntitysByHql(BDCS_SQR.class, sqlSQR);
		// 再删除权利人
		getCommonDao().deleteEntitysByHql(BDCS_QLR_GZ.class, sql);
		// 再删除权利
		getCommonDao().deleteEntitysByHql(BDCS_QL_GZ.class, sql);
		// 再删除附属权利
		getCommonDao().deleteEntitysByHql(BDCS_FSQL_GZ.class, sql);
		// 再删除证书
		getCommonDao().deleteEntitysByHql(BDCS_ZS_GZ.class, sql);
		// 删除权利-权利人-证书-单元关系
		getCommonDao().deleteEntitysByHql(BDCS_QDZR_GZ.class, sql);
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
		List<Rights> qls=RightsTools.loadRightsByCondition(DJDYLY.GZ, " XMBH='"+super.getXMBH()+"'");
		if(qls!=null&&qls.size()>0){
			for(Rights ql:qls){
				super.addQLRbySQRs(ql.getId(), sqrids);
			}
		}
	}

	/**
	 * 移除权利人
	 */
	@Override
	public void removeQLR(String qlid, String qlrid) {
		RightsHolder qlr_m=RightsHolderTools.loadRightsHolder(DJDYLY.GZ, qlrid);
		if(qlr_m!=null){
			List<Rights> qls=RightsTools.loadRightsByCondition(DJDYLY.GZ, " XMBH='"+super.getXMBH()+"'");
			if(qls!=null&&qls.size()>0){
				for(Rights ql:qls){
					List<RightsHolder> qlrs=RightsHolderTools.loadRightsHolders(DJDYLY.GZ, ql.getId());
					if(qlrs!=null&&qlrs.size()>0){
						for(RightsHolder qlr:qlrs){
							if(qlr.getQLRMC().equals(qlr_m.getQLRMC())){
								super.removeqlr(qlr.getId(), qlr.getQLID());
							}
						}
					}
				}
			}
		}
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
			List<BDCS_DJDY_GZ> djdys = dao.getDataList(BDCS_DJDY_GZ.class, xmbhHql);
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
					
					String condition = MessageFormat.format("DJDYID=''{0}'' AND QLLX=''23'' ", djdy.getDJDYID());
					List<Rights> _rightss = RightsTools.loadRightsByCondition(DJDYLY.GZ, condition);
					BDCS_QL_GZ ql = null;
					if (_rightss != null && _rightss.size() > 0) {
						ql = (BDCS_QL_GZ) _rightss.get(0);
					}
					List<BDCS_QLR_GZ> qlrs = super.getQLRs(ql.getId());
					List<BDCS_SQR> sqrs = new ArrayList<BDCS_SQR>();
					if (null != qlrs) {
						sqrs = super.getSQRs(qlrs);
					}
					BDCS_FSQL_GZ fsql = new BDCS_FSQL_GZ();
					if (ql != null && !StringUtils.isEmpty(ql.getFSQLID())) {
						fsql = dao.get(BDCS_FSQL_GZ.class, ql.getFSQLID());
					}

					Message msg = exchangeFactory.createMessageByDYQ();
					List<DJFDJSH> sh = msg.getData().getDJSH();
//					sh = packageXml.getDJFDJSH(new DJFDJSH(), ywh, this.getXMBH(), actinstID);
//					msg.getData().setDJSH(sh);
//
//					List<DJFDJSZ> sz = packageXml.getDJFDJSZ(msg.getData().getDJSZ(), ywh, this.getXMBH());
//					msg.getData().setDJSZ(sz);
//
//					List<DJFDJFZ> fz = packageXml.getDJFDJFZ(msg.getData().getDJFZ(), ywh, this.getXMBH());
//					msg.getData().setDJFZ(fz);
					msg.getHead().setRecType("9000101");

					if (BDCDYLX.SHYQZD.Value.equals(djdy.getBDCDYLX())) { // 使用权
						try {
							BDCS_SHYQZD_GZ zd = dao.get(BDCS_SHYQZD_GZ.class, djdy.getBDCDYID());
							super.fillHead(msg, i, ywh,zd.getBDCDYH(),zd.getQXDM(),ql.getLYQLID());
							msg.getHead().setParcelID(StringHelper.formatObject(zd.getZDDM()));
							msg.getHead().setEstateNum(StringHelper.formatObject(zd.getBDCDYH()));
//							msg.getHead().setPreEstateNum(StringHelper.formatObject(zd.getBDCDYH()));
							if (zd != null && !StringUtils.isEmpty(zd.getQXDM())) {
								msg.getHead().setAreaCode(zd.getQXDM());
							}

							if (djdy != null) {
								QLFQLDYAQ dyaq = msg.getData().getQLFQLDYAQ();
								dyaq = packageXml.getQLFQLDYAQ(dyaq, zd, ql, fsql, ywh, null);

								List<ZTTGYQLR> zttqlr = msg.getData().getGYQLR();
								zttqlr = packageXml.getZTTGYQLRs(zttqlr, qlrs, zd, ql, null, null, null, null);
								msg.getData().setGYQLR(zttqlr);

								DJTDJSLSQ sq = msg.getData().getDJSLSQ();
								sq = packageXml.getDJTDJSLSQ(sq, ywh, zd, ql, xmxx, null, xmxx.getSLSJ(), null, null, null);

								List<DJFDJSJ> sj = msg.getData().getDJSJ();
								sj = packageXml.getDJFDJSJ(zd, ywh,actinstID);
								msg.getData().setDJSJ(sj);
								
								List<DJFDJSQR> djsqrs = msg.getData().getDJSQR();
								djsqrs = packageXml.getDJSQRs(djsqrs, sqrs, zd.getYSDM(), ywh, zd.getBDCDYH());
								msg.getData().setDJSQR(djsqrs);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					BDCS_H_GZ h = null;
					if (BDCDYLX.H.Value.equals(djdy.getBDCDYLX())) { // 房屋
						try {
							h = dao.get(BDCS_H_GZ.class, djdy.getBDCDYID());
							if (h != null) {
								super.fillHead(msg, i, ywh,h.getBDCDYH(),h.getQXDM(),ql.getLYQLID());
								BDCS_SHYQZD_GZ zd = dao.get(BDCS_SHYQZD_GZ.class, h.getZDBDCDYID());
								if (zd != null) {
									h.setZDDM(zd.getZDDM());
								} else{
									BDCS_SHYQZD_XZ zd2 = dao.get(BDCS_SHYQZD_XZ.class, h.getZDBDCDYID());
									h.setZDDM(zd2.getZDDM());
								}
							}
							msg.getHead().setParcelID(StringHelper.formatObject(h.getZDDM()));
							msg.getHead().setEstateNum(StringHelper.formatObject(h.getBDCDYH()));
//							msg.getHead().setPreEstateNum(StringHelper.formatObject(h.getBDCDYH()));
							if (h != null && !StringUtils.isEmpty(h.getQXDM())) {
								msg.getHead().setAreaCode(h.getQXDM());
							}
							if (djdy != null) {
								QLFQLDYAQ dyaq = msg.getData().getQLFQLDYAQ();
								dyaq = packageXml.getQLFQLDYAQ(dyaq, null, ql, fsql, ywh, h);

								List<ZTTGYQLR> zttqlr = msg.getData().getGYQLR();
								zttqlr = packageXml.getZTTGYQLRs(zttqlr, qlrs, null, ql, h, null, null, null);
								msg.getData().setGYQLR(zttqlr);

								DJTDJSLSQ sq = msg.getData().getDJSLSQ();
								sq = packageXml.getDJTDJSLSQ(sq, ywh, null, ql, xmxx, h, xmxx.getSLSJ(), null, null, null);

								List<DJFDJSJ> sj = msg.getData().getDJSJ();
								sj = packageXml.getDJFDJSJ(h, ywh,actinstID);
								msg.getData().setDJSJ(sj);

								List<DJFDJSQR> djsqrs = msg.getData().getDJSQR();
								djsqrs = packageXml.getDJSQRs(djsqrs, sqrs, h.getYSDM(), ywh, h.getBDCDYH());
								msg.getData().setDJSQR(djsqrs);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					mashaller.marshal(msg, new File(path + "Biz" + msg.getHead().getBizMsgID() + ".xml"));
					result = uploadFile(path + "Biz" + msg.getHead().getBizMsgID() + ".xml",ConstValue.RECCODE.DIYQ_ZXDJ.Value,actinstID,djdy.getDJDYID(),ql.getId());
					names.put(djdy.getDJDYID(), msg.getHead().getBizMsgID() + ".xml");
					if(null == result){
						Map<String, String> xmlError = new HashMap<String, String>();
						xmlError.put("error", "连接SFTP失败,请检查服务器和前置机的连接是否正常");
						YwLogUtil.addSjsbResult("Biz" + msg.getHead().getBizMsgID() + ".xml", "", "连接SFTP失败,请检查服务器和前置机的连接是否正常", ConstValue.SF.NO.Value, ConstValue.RECCODE.DIYQ_ZXDJ.Value,ProjectHelper.getpRroinstIDByActinstID(actinstID));
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
				String lyqlid = bdcql.getLYQLID();
				SubRights bdcfsql = RightsTools.loadSubRightsByRightsID(ConstValue.DJDYLY.GZ, bdcql.getId());
				List<RightsHolder> bdcqlrs = RightsHolderTools.loadRightsHolders(ConstValue.DJDYLY.LS, lyqlid);
				//bdcql.setId(lyqlid);
				MessageExport msg = super.getShareMsgTools().GetMsg(bdcdy, bdcql, bdcfsql, bdcqlrs, bljc, xmxx);
				super.getShareMsgTools().SendMsg(msg, idjdy + 1, djdy.getBDCDYLX(), xmxx);
			}
		}
	}

}
