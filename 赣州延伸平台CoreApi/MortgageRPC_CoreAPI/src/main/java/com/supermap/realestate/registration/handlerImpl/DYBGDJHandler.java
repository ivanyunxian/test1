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
import com.supermap.realestate.registration.dataExchange.hy.KTFZHBHQK;
import com.supermap.realestate.registration.dataExchange.hy.KTFZHYHYDZB;
import com.supermap.realestate.registration.dataExchange.hy.KTFZHYHZK;
import com.supermap.realestate.registration.dataExchange.hy.KTTZHJBXX;
import com.supermap.realestate.registration.dataExchange.hy.QLFQLHYSYQ;
import com.supermap.realestate.registration.dataExchange.hy.ZHK105;
import com.supermap.realestate.registration.dataExchange.utils.packageXml;
import com.supermap.realestate.registration.factorys.HandlerFactory;
import com.supermap.realestate.registration.handler.DJHandler;
import com.supermap.realestate.registration.model.BDCS_DJDY_GZ;
import com.supermap.realestate.registration.model.BDCS_DJDY_XZ;
import com.supermap.realestate.registration.model.BDCS_FSQL_GZ;
import com.supermap.realestate.registration.model.BDCS_FSQL_XZ;
import com.supermap.realestate.registration.model.BDCS_H_XZ;
import com.supermap.realestate.registration.model.BDCS_H_XZY;
import com.supermap.realestate.registration.model.BDCS_QDZR_GZ;
import com.supermap.realestate.registration.model.BDCS_QDZR_XZ;
import com.supermap.realestate.registration.model.BDCS_QLR_GZ;
import com.supermap.realestate.registration.model.BDCS_QLR_XZ;
import com.supermap.realestate.registration.model.BDCS_QL_GZ;
import com.supermap.realestate.registration.model.BDCS_QL_XZ;
import com.supermap.realestate.registration.model.BDCS_SHYQZD_GZ;
import com.supermap.realestate.registration.model.BDCS_SHYQZD_XZ;
import com.supermap.realestate.registration.model.BDCS_SQR;
import com.supermap.realestate.registration.model.BDCS_XMXX;
import com.supermap.realestate.registration.model.BDCS_YHZK_GZ;
import com.supermap.realestate.registration.model.BDCS_ZH_XZ;
import com.supermap.realestate.registration.model.BDCS_ZS_GZ;
import com.supermap.realestate.registration.model.BDCS_ZS_XZ;
import com.supermap.realestate.registration.model.WFD_MAPPING;
import com.supermap.realestate.registration.model.interfaces.House;
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
import com.supermap.realestate.registration.util.ConstValue.DJZT;
import com.supermap.realestate.registration.util.ConstValue.SF;
import com.supermap.realestate.registration.util.ConstValue.SQRLB;
import com.supermap.realestate.registration.util.ConstValue.ZSBS;
import com.supermap.realestate.registration.util.Global;
import com.supermap.realestate.registration.util.ObjectHelper;
import com.supermap.realestate.registration.util.ProjectHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.realestate.registration.util.YwLogUtil;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import com.supermap.wisdombusiness.web.ResultMessage;

/*
 1、国有建设用地抵押权变更登记
 2、集体建设用地抵押权变更登记（未配置）
 3、宅基地抵押权变更登记（未配置）
 4、国有建设用地使用权/房屋所有权抵押权变更登记
 5、集体建设用地使用权/房屋所有权抵押权变更登记（未配置）
 6、宅基地使用权/房屋所有权抵押权变更登记（未配置）
 7、在建工程抵押变更（未配置）
 8、商品房抵押预告登记变更（未配置）
 */
/**
 * 
 * 抵押变更登记处理类
 * @ClassName: DYBGDJHandler
 * @author liushufeng
 * @date 2015年9月8日 下午10:33:16
 */
public class DYBGDJHandler extends DJHandlerBase implements DJHandler {

	/**
	 * 构造函数
	 * 
	 * @param info
	 */
	public DYBGDJHandler(ProjectInfo info) {
		super(info);
	}

	/**
	 * 添加不动产单元
	 */
	@Override
	public boolean addBDCDY(String qlids) {
		List<String> listDJDY = new ArrayList<String>();
		String qlid[] = qlids.split(",");
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
						BDCS_QL_GZ bdcs_ql_gz = CopyQLXXFromXZ(id);
						bdcs_ql_gz.setYWH(this.getProject_id());
						bdcs_ql_gz.setDJLX(this.getDjlx().Value);
						gzqlid = bdcs_ql_gz.getId();
					} else {
						// 拷贝权利信息（权证号为空）
						BDCS_QL_GZ bdcs_ql_gz = CopyQLXXFromXZExceptBDCQZH(id);
						bdcs_ql_gz.setYWH(this.getProject_id());
						bdcs_ql_gz.setDJLX(this.getDjlx().Value);
						gzqlid = bdcs_ql_gz.getId();
						//在建建筑物赋值 liangq
						List<BDCS_FSQL_GZ> bdcs_fsql_gz = 
								getCommonDao().getDataList(BDCS_FSQL_GZ.class, " xmbh ='"+this.getXMBH()+"'");
						if(bdcs_fsql_gz.size()>0){
							List<BDCS_DJDY_GZ> list = getCommonDao().getDataList(BDCS_DJDY_GZ.class, "DJDYID='" + ql.getDJDYID() + "'");
							if(list.size()>0){
								if("02".equals(list.get(0).getBDCDYLX())){
									List<BDCS_SHYQZD_XZ> shyqzd_xz = 
											getCommonDao().getDataList(BDCS_SHYQZD_XZ.class, "bdcdyh = '"+ql.getBDCDYH()+"'");
									if(shyqzd_xz.size()>0){
										bdcs_fsql_gz.get(0).setZJJZWZL(shyqzd_xz.get(0).getZL());
										getCommonDao().save(bdcs_fsql_gz.get(0));
									}
								}else if("032".equals(list.get(0).getBDCDYLX())){
									
								}else if("031".equals(list.get(0).getBDCDYLX())){
									List<BDCS_H_XZ> h_xz = 
											getCommonDao().getDataList(BDCS_H_XZ.class, "bdcdyid = '"+ql.getBDCDYID()+"'");
									if (h_xz != null && h_xz.size() > 0) {
										bdcs_fsql_gz.get(0).setZJJZWZL(StringHelper.formatObject(h_xz.get(0).getZL()));
									}
								}
							}
						}
						
					}
					getCommonDao().flush();
					//CopySQRFromXZQLR(id, gzqlid);
					CopySQRFromGZQLR(ql.getXMBH(), gzqlid);
					//GZ201流程，添加义务人
//					if ("GZ201".equals(((WFD_MAPPING)listCode.get(0)).getWORKFLOWNAME())) {
//						String xmbh=  ql.getXMBH();
//						String sql =" XMBH='"+xmbh+"' AND SQRLB='2'";
//						List<BDCS_SQR> sqrlist = getCommonDao().getDataList(BDCS_SQR.class, sql);
//						for (int iywr = 0; iywr < sqrlist.size(); iywr++) {
//							BDCS_SQR ywr = sqrlist.get(iywr);
//							String SQRID = getPrimaryKey();
//							BDCS_SQR sqr_ywr = new BDCS_SQR();
//							ObjectHelper.copyObject(ywr,sqr_ywr);
//							sqr_ywr.setXMBH(getXMBH());
//							sqr_ywr.setGLQLID(gzqlid);
//							sqr_ywr.setId(SQRID);
//							sqr_ywr.setSQRLB(SQRLB.YF.Value);
//							getCommonDao().save(sqr_ywr);
//						}
//					}
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
			String Baseworkflow_ID =HandlerFactory.getWorkflow(this.getProject_id()).getName();
			String sql =null;
			boolean bexists = false;
			if ("BG008".equals(Baseworkflow_ID)||"BG214".equals(Baseworkflow_ID)) {
				sql = MessageFormat.format(" XMBH=''{0}'' AND SQRLX=''1''", xmbh_old);
			} else {
				sql = MessageFormat.format(" XMBH=''{0}'' AND SQRLB=''2''", xmbh_old);
			}
			List<BDCS_SQR> sqrlist = getCommonDao().getDataList(BDCS_SQR.class, sql);
			if (sqrlist == null && sqrlist.size() <= 0) {
				return;
			}
			for (int isqr = 0; isqr < sqrlist.size(); isqr++) {
				BDCS_SQR sqr = sqrlist.get(isqr);
				List<BDCS_SQR> issqr = null;
				if(sqr.getZJH() != null){
					issqr = getCommonDao().getDataList(BDCS_SQR.class, "ZJH='"+sqr.getZJH()+"' AND XMBH='"+getXMBH()+"'");
				}
				else if(sqr.getSQRXM() != null){
					issqr = getCommonDao().getDataList(BDCS_SQR.class, "SQRXM='"+sqr.getSQRXM()+"' AND XMBH='"+getXMBH()+"'");
				}
				if( issqr.size() > 0 && issqr != null ){
					bexists = true;
				}
				if (!bexists) {
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
		if("LD".equals(super.getBdcdylx().toString())||"HY".equals(super.getBdcdylx().toString())){
			return super.exportXMLother(path, actinstID,"NO");
		}
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

//					List<DJFDJSH> sh = msg.getData().getDJSH();
//					sh = packageXml.getDJFDJSH(new DJFDJSH(), ywh, this.getXMBH(), actinstID);
//					msg.getData().setDJSH(sh);
//
//					List<DJFDJSZ> sz = packageXml.getDJFDJSZ(msg.getData().getDJSZ(), ywh, this.getXMBH());
//					msg.getData().setDJSZ(sz);
//
//					List<DJFDJFZ> fz = packageXml.getDJFDJFZ(msg.getData().getDJFZ(), ywh, this.getXMBH());
//					msg.getData().setDJFZ(fz);
					msg.getHead().setRecType("9000101");
					
					if (BDCDYLX.HY.Value.equals(djdy.getBDCDYLX())) { //海域
						// 海域(含无居民海岛)使用权变更
						BDCS_YHZK_GZ yhzk_gz = null;
						msg = exchangeFactory.createMessageByHY();
						//if (djdy != null) {
							BDCS_ZH_XZ zh = dao.get(BDCS_ZH_XZ.class, djdy.getBDCDYID());//宗海基本信息
							if (null != zh) {
								String hql = "BDCDYID = '" + zh.getId() + "' ";
								List<BDCS_YHZK_GZ> yhzks = dao.getDataList(BDCS_YHZK_GZ.class, hql);
								if (yhzks != null && yhzks.size() > 0) {
									yhzk_gz = yhzks.get(0);
								}
							}
							// 这些字段先手动赋值 diaoliwei
							if (zh != null) {
								if (StringUtils.isEmpty(zh.getZHT())) {
									zh.setZHT("无");
								}
							}
							if (zh != null && !StringUtils.isEmpty(zh.getQXDM())) {
								msg.getHead().setAreaCode(zh.getQXDM());
							}
							
							//设置报文头
							this.fillHead(msg, i, ywh,zh.getBDCDYH(),zh.getQXDM(),ql.getLYQLID());
							msg.getHead().setParcelID(StringHelper.formatObject(zh.getZHDM()));
							msg.getHead().setEstateNum(StringHelper.formatObject(zh.getBDCDYH()));
//							msg.getHead().setPreEstateNum(StringHelper.formatObject(zh.getBDCDYH()));
							//设置报文信息
							//1.宗海基本信息系
							KTTZHJBXX jbxx = msg.getData().getKTTZHJBXX();
							jbxx = packageXml.getKTTZHJBXX(jbxx, ql, zh);//组装成xml格式
							msg.getData().setKTTZHJBXX(jbxx);
							
							//2.非结构化文档
							FJF100 fj = msg.getData().getFJF100();
							fj = packageXml.getFJF(fj);
							msg.getData().setFJF100(fj);
							

							//3.宗海变化状况
							KTFZHBHQK bhqk = msg.getData().getKTFZHBHQK();
							bhqk = packageXml.getKTFZHBHQK(bhqk, ql, zh);
							msg.getData().setKTFZHBHQK(bhqk);

							//4.宗海使用权
							QLFQLHYSYQ syq = msg.getData().getQLFQLHYSYQ();
							syq = packageXml.getQLFQLHYSYQ(syq, ql, zh, ywh);
							msg.getData().setQLFQLHYSYQ(syq);

							//5.宗海变化状况表(可选 )
							KTFZHYHZK yhzk = msg.getData().getKTFZHYHZK();
							yhzk = packageXml.getKTFZHYHZK(yhzk, yhzk_gz, zh.getBDCDYH());
							msg.getData().setKTFZHYHZK(yhzk);

							//6.用海,用岛坐标
							KTFZHYHYDZB zb = msg.getData().getKTFZHYHYDZB();
							zb = packageXml.getKTFZHYHYDZB(zb, zh);
							msg.getData().setKTFZHYHYDZB(zb);

							//6.权力人表
							List<ZTTGYQLR> zttqlr = msg.getData().getGYQLR();
							zttqlr = packageXml.getZTTGYQLRs(zttqlr, qlrs, null, ql, null, null, null, zh);
							msg.getData().setGYQLR(zttqlr);

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
							//13.空间属性
							List<ZHK105> zhk = msg.getData().getZHK105();
							zhk = packageXml.getZHK105(zhk, zh);
							msg.getData().setZHK105(zhk);

							List<DJFDJSQR> djsqrs = msg.getData().getDJSQR();
							djsqrs = packageXml.getDJSQRs(djsqrs, sqrs, zh.getYSDM(), ywh, zh.getBDCDYH());
							msg.getData().setDJSQR(djsqrs);
//						}
						
						
					
					}

					if (BDCDYLX.SHYQZD.Value.equals(djdy.getBDCDYLX())) { // 使用权
						try {
							
							BDCS_SHYQZD_XZ zd = dao.get(BDCS_SHYQZD_XZ.class, djdy.getBDCDYID());
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
								
								FJF100 fj = msg.getData().getFJF100();
								fj = packageXml.getFJF(fj);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					House h = null;
					if (BDCDYLX.H.Value.equals(djdy.getBDCDYLX())||BDCDYLX.YCH.Value.equals(djdy.getBDCDYLX())) { // 房屋
						try {
							
							if(BDCDYLX.H.Value.equals(djdy.getBDCDYLX())) {
								h = dao.get(BDCS_H_XZ.class, djdy.getBDCDYID());
							}else {
								h = dao.get(BDCS_H_XZY.class, djdy.getBDCDYID());
							}
							if (h != null) {
								BDCS_SHYQZD_XZ zd = dao.get(BDCS_SHYQZD_XZ.class, h.getZDBDCDYID());
								if (zd != null) {
									h.setZDDM(zd.getZDDM());
								}
							}
							super.fillHead(msg, i, ywh,h.getBDCDYH(),h.getQXDM(),ql.getLYQLID());
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
								
								FJF100 fj = msg.getData().getFJF100();
								fj = packageXml.getFJF(fj);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					mashaller.marshal(msg, new File(path + "Biz" + msg.getHead().getBizMsgID() + ".xml"));
					result = uploadFile(path + "Biz" + msg.getHead().getBizMsgID() + ".xml",ConstValue.RECCODE.DIYQ_ZXDJ.Value,actinstID,djdy.getDJDYID(),ql.getId());
					names.put(djdy.getDJDYID(), msg.getHead().getBizMsgID() + ".xml");
					if(result.equals("")|| result==null){
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
	
	protected BDCS_QL_GZ CopyQLXXFromXZ(String qlid) {
		BDCS_QL_GZ bdcs_ql_gz = null;
		StringBuilder builer = new StringBuilder();
		builer.append(" QLID='").append(qlid).append("'");
		String strQuery = builer.toString();
		BDCS_QL_XZ bdcs_ql_xz = getCommonDao().get(BDCS_QL_XZ.class, qlid);
		if (bdcs_ql_xz != null) {
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
			bdcs_ql_gz.setCASENUM("");
			bdcs_ql_gz.setBDCQZH("");
			bdcs_ql_gz.setDJYY("");
			bdcs_ql_gz.setFJ("");
			
			// bdcs_ql_gz.setDJYY("");
			// bdcs_ql_gz.setFJ("");

			BDCS_FSQL_XZ bdcs_fsql_xz = getCommonDao().get(BDCS_FSQL_XZ.class, bdcs_ql_xz.getFSQLID());
			if (bdcs_fsql_xz != null) {
				// 拷贝附属权利
				BDCS_FSQL_GZ bdcs_fsql_gz = ObjectHelper.copyFSQL_XZToGZ(bdcs_fsql_xz);
				bdcs_fsql_gz.setQLID(gzqlid);
				bdcs_fsql_gz.setId(gzfsqlid);
				bdcs_fsql_gz.setXMBH(getXMBH());
				bdcs_fsql_gz.setBDBZZQSE(null);
				bdcs_fsql_gz.setZGZQSE(null);
				bdcs_fsql_gz.setDYR("");
				bdcs_fsql_gz.setYWR("");
				bdcs_fsql_gz.setZJJZWZL("");
				bdcs_fsql_gz.setBDCZL("");
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
						// 拷贝权利人
						String gzqlrid = getPrimaryKey();
						BDCS_QLR_GZ bdcs_qlr_gz = ObjectHelper.copyQLR_XZToGZ(bdcs_qlr_xz);
						bdcs_qlr_gz.setId(gzqlrid);
						bdcs_qlr_gz.setQLID(gzqlid);
						bdcs_qlr_gz.setXMBH(getXMBH());
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
			getCommonDao().save(bdcs_ql_gz);
			bdcs_ql_xz.setDJZT("02");
			getCommonDao().update(bdcs_ql_xz);
		}
		return bdcs_ql_gz;
	}
	
	/**
	 * 从现状拷贝权利信息，不带权证号
	 * 
	 * @Author：俞学斌
	 * @CreateTime 2015年6月22日 下午5:55:53
	 * @param djdyid
	 *            登记单元ID
	 * @return 状态字符串
	 */
	protected BDCS_QL_GZ CopyQLXXFromXZExceptBDCQZH(String qlid) {
		BDCS_QL_GZ bdcs_ql_gz = null;
		StringBuilder builer = new StringBuilder();
		builer.append(" QLID='").append(qlid).append("'");
		String strQuery = builer.toString();
		BDCS_QL_XZ bdcs_ql_xz = getCommonDao().get(BDCS_QL_XZ.class, qlid);
		if (bdcs_ql_xz != null) {
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
			bdcs_ql_gz.setBDCQZH("");
			bdcs_ql_gz.setZSBS(ZSBS.DYB.Value);// 默认单一版持证方式
			bdcs_ql_gz.setYWH(getProject_id());
			bdcs_ql_gz.setARCHIVES_BOOKNO("");
			bdcs_ql_gz.setARCHIVES_CLASSNO("");
			bdcs_ql_gz.setCASENUM("");
			bdcs_ql_gz.setFJ("");
			// bdcs_ql_gz.setDJYY("");
			// bdcs_ql_gz.setFJ("");
			bdcs_ql_gz.setQLQSSJ(null);
			bdcs_ql_gz.setQLJSSJ(null);
			if (bdcs_ql_xz.getFSQLID() != null) {
				BDCS_FSQL_XZ bdcs_fsql_xz = getCommonDao().get(BDCS_FSQL_XZ.class, bdcs_ql_xz.getFSQLID());
				if (bdcs_fsql_xz != null) {
					// 拷贝附属权利
					BDCS_FSQL_GZ bdcs_fsql_gz = ObjectHelper.copyFSQL_XZToGZ(bdcs_fsql_xz);
					bdcs_fsql_gz.setQLID(gzqlid);
					bdcs_fsql_gz.setId(gzfsqlid);
					bdcs_fsql_gz.setXMBH(getXMBH());
					bdcs_fsql_gz.setQLID(gzqlid);
					bdcs_fsql_gz.setId(gzfsqlid);
					bdcs_fsql_gz.setXMBH(getXMBH());
					bdcs_fsql_gz.setBDBZZQSE(null);
					bdcs_fsql_gz.setZGZQSE(null);
					getCommonDao().save(bdcs_fsql_gz);
				}
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

			StringBuilder builderDJDY = new StringBuilder();
			builderDJDY.append(" DJDYID='");
			builderDJDY.append(bdcs_ql_xz.getDJDYID());
			builderDJDY.append("'");
			// 获取权利人集合
			Map<String, String> lyzsid_zsid = new HashMap<String, String>();
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
						bdcs_qlr_gz.setBDCQZHXH("");
						bdcs_qlr_gz.setBDCQZH("");
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
								bdcs_zs_gz.setBDCQZH("");
								bdcs_zs_gz.setZSBH("");
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
			getCommonDao().save(bdcs_ql_gz);
			bdcs_ql_xz.setDJZT("02");
			getCommonDao().update(bdcs_ql_xz);
		}
		return bdcs_ql_gz;
	}
}
