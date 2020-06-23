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
import com.supermap.realestate.registration.dataExchange.hy.KTFZHYHZK;
import com.supermap.realestate.registration.dataExchange.utils.packageXml;
import com.supermap.realestate.registration.handler.DJHandler;
import com.supermap.realestate.registration.model.BDCS_DJDY_GZ;
import com.supermap.realestate.registration.model.BDCS_DJDY_LS;
import com.supermap.realestate.registration.model.BDCS_DJDY_XZ;
import com.supermap.realestate.registration.model.BDCS_FSQL_GZ;
import com.supermap.realestate.registration.model.BDCS_FSQL_LS;
import com.supermap.realestate.registration.model.BDCS_FSQL_XZ;
import com.supermap.realestate.registration.model.BDCS_H_LSY;
import com.supermap.realestate.registration.model.BDCS_H_XZ;
import com.supermap.realestate.registration.model.BDCS_H_XZY;
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
import com.supermap.realestate.registration.model.BDCS_XMXX;
import com.supermap.realestate.registration.model.BDCS_YHZK_GZ;
import com.supermap.realestate.registration.model.BDCS_YHZK_XZ;
import com.supermap.realestate.registration.model.BDCS_ZH_GZ;
import com.supermap.realestate.registration.model.BDCS_ZH_XZ;
import com.supermap.realestate.registration.model.BDCS_ZS_GZ;
import com.supermap.realestate.registration.model.BDCS_ZS_LS;
import com.supermap.realestate.registration.model.BDCS_ZS_XZ;
import com.supermap.realestate.registration.model.WFD_MAPPING;
import com.supermap.realestate.registration.model.YC_SC_H_XZ;
import com.supermap.realestate.registration.model.interfaces.RealUnit;
import com.supermap.realestate.registration.model.interfaces.Rights;
import com.supermap.realestate.registration.model.interfaces.RightsHolder;
import com.supermap.realestate.registration.model.interfaces.Sea;
import com.supermap.realestate.registration.model.interfaces.SubRights;
import com.supermap.realestate.registration.model.interfaces.YHZK;
import com.supermap.realestate.registration.model.xmlExportmodel.MessageExport;
import com.supermap.realestate.registration.service.impl.ProjectServiceImpl;
import com.supermap.realestate.registration.tools.RightsHolderTools;
import com.supermap.realestate.registration.tools.RightsTools;
import com.supermap.realestate.registration.tools.UnitTools;
import com.supermap.realestate.registration.util.ConfigHelper;
import com.supermap.realestate.registration.util.ConstValue;
import com.supermap.realestate.registration.util.ConstValue.BDCDYLX;
import com.supermap.realestate.registration.util.ConstValue.CZFS;
import com.supermap.realestate.registration.util.ConstValue.DJDYLY;
import com.supermap.realestate.registration.util.ConstValue.QLLX;
import com.supermap.realestate.registration.util.GetProperties;
import com.supermap.realestate.registration.util.Global;
import com.supermap.realestate.registration.util.ObjectHelper;
import com.supermap.realestate.registration.util.ProjectHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.realestate.registration.util.YwLogUtil;
import com.supermap.wisdombusiness.core.SuperSpringContext;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;

/*
 1、国有建设用地抵押权初始登记
 2、集体建设用地抵押权初始登记（未配置）
 3、宅基地抵押权初始登记（未配置）
 4、国有建设用地使用权/房屋所有权抵押权初始登记
 5、集体建设用地使用权/房屋所有权抵押权初始登记（未配置）
 6、宅基地使用权/房屋所有权抵押权初始登记（未配置）
 */
/**
 * 
* 抵押初始登记处理类
* @ClassName: DYDJHandler 
* @author liushufeng 
* @date 2015年9月8日 下午10:32:12
 */
public class DYDJHandler extends DJHandlerBase implements DJHandler {

	/**
	 * 构造函数
	 * 
	 * @param info
	 */
	public DYDJHandler(ProjectInfo info) {
		super(info);
	}

	/**
	 * 添加不动产单元
	 */
	@Override
	public boolean addBDCDY(String bdcdyid) {
		boolean b = true;
		if (!StringHelper.isEmpty(bdcdyid)) {
			String[] bdcdyids = bdcdyid.split(",");
			for (int i = 0; i < bdcdyids.length; i++) {
				String id = bdcdyids[i];
				if (!StringHelper.isEmpty(id)) {
					if (b = true) {
						b = addbdcdy(id);
					}
				}
			}
		}
		return b;
	}

	private boolean addbdcdy(String bdcdyid) {
		boolean bSuccess = false;
		CommonDao dao = this.getCommonDao();
		BDCS_DJDY_GZ djdy = super.createDJDYfromXZ(bdcdyid);
		if (djdy != null) {
			RealUnit unit = null;
			try {
				unit=UnitTools.loadUnit(this.getBdcdylx(), DJDYLY.initFrom(djdy.getLY()), djdy.getBDCDYID());
			} catch (Exception e) {
			}
			// 生成权利信息
			BDCS_QL_GZ ql = super.createQL(djdy, unit);
			ql.setCZFS(CZFS.GTCZ.Value);
			
			String xzqhdm=ConfigHelper.getNameByValue("XZQHDM");
			if(!StringHelper.isEmpty(xzqhdm) && xzqhdm.startsWith("1301")){
				ql.setDJYY("借款");
			}
			
			// 生成附属权利
			BDCS_FSQL_GZ fsql = super.createFSQL(djdy.getDJDYID());
			ql.setFSQLID(fsql.getId());
			fsql.setQLID(ql.getId());
			// 把附属权利里边的抵押人和抵押不动产类型写上
			String qllxarray = "('3','4','5','6','7','8','10','11','12','15','24','36')";
			String hql = "QLID IN(SELECT id FROM BDCS_QL_XZ WHERE  DJDYID='" + djdy.getDJDYID() + "' AND QLLX IN " + qllxarray + ") ORDER BY SXH";
			String lyqlid = "";
			List<BDCS_QLR_XZ> list = dao.getDataList(BDCS_QLR_XZ.class, hql);
			if (list != null && list.size() > 0) {
				String qlrnames = "";
				lyqlid = list.get(0).getQLID();
				ql.setLYQLID(lyqlid);
				for (int i = 0; i < list.size(); i++) {
					qlrnames += list.get(i).getQLRMC() + ",";

					BDCS_QLR_XZ qlr = list.get(i);
					String zjhm = qlr.getZJH();
					boolean bexists = false;
					if (!StringHelper.isEmpty(qlr.getQLRMC())) {
						
						String Sql ="";
						if(!StringHelper.isEmpty(zjhm))
						{
						Sql=MessageFormat.format(" XMBH=''{0}'' AND SQRXM=''{1}'' AND ZJH=''{2}''", getXMBH(), qlr.getQLRMC(), zjhm);
						}
						else
						{
							Sql=MessageFormat.format(" XMBH=''{0}'' AND SQRXM=''{1}'' AND ZJH IS NULL", getXMBH(), qlr.getQLRMC());	
						}
						 
						List<BDCS_SQR> sqrlist = dao.getDataList(BDCS_SQR.class, Sql);
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
						sqr.setGLQLID(ql.getId());
						sqr.setFDDBR(qlr.getFDDBR());
						sqr.setFDDBRDH(qlr.getFDDBRDH());
						sqr.setFDDBRZJLX(qlr.getFDDBRZJLX());
						sqr.setDLRXM(qlr.getDLRXM());
						sqr.setDLRZJLX(qlr.getDLRZJLX());
						sqr.setDLRZJHM(qlr.getDLRZJHM());
						//代理机构名称
						sqr.setDLJGMC(qlr.getDLJGMC());
						sqr.setDLRLXDH(qlr.getDLRLXDH());
						if(StringHelper.isEmpty(sqr.getSQRLX())){
							sqr.setSQRLX("1");
						}
						dao.save(sqr);
					}
				}
				if (!StringUtils.isEmpty(qlrnames)) {
					qlrnames = qlrnames.substring(0, qlrnames.length() - 1);
					fsql.setDYR(qlrnames);
				}
			}
			// 设置抵押物类型
			fsql.setDYWLX(getDYBDCLXfromBDCDYLX(getBdcdylx()));
			fsql.setZJJZWZL(unit.getZL());
			int dysw=RightsTools.getMaxMortgageSWS(djdy.getDJDYID());
			fsql.setDYSW(dysw+1);
			// 保存
			dao.save(djdy);
			dao.save(ql);
			dao.save(fsql);
			bSuccess = true;
		}
		dao.flush();
		return bSuccess;
	}

	/**
	 * 写入登记薄
	 */
	@Override
	public boolean writeDJB() {
		String strSqlXMBH = ProjectHelper.GetXMBHCondition(getXMBH());
		List<BDCS_DJDY_GZ> djdys = getCommonDao().getDataList(BDCS_DJDY_GZ.class, strSqlXMBH);
		if(!YCDYCanecl()){
			return false;
		}
		if(djdys!=null&&djdys.size()>20){
			return writeDJBEx();
		}
		if (super.isCForCFING()) {
			return false;
		}
		if (djdys != null && djdys.size() > 0) {
			for (int idjdy = 0; idjdy < djdys.size(); idjdy++) {
				BDCS_DJDY_GZ bdcs_djdy_gz = djdys.get(idjdy);
				if (bdcs_djdy_gz != null) {
					String djdyid = bdcs_djdy_gz.getDJDYID();
					super.CopyGZQLToXZAndLS(djdyid);
					super.CopyGZQLRToXZAndLS(djdyid);
					super.CopyGZQDZRToXZAndLS(djdyid);
					super.CopyGZZSToXZAndLS(djdyid);
					BDCDYLX dylx = ProjectHelper.GetBDCDYLX(getXMBH());
					// 更新单元抵押状态
					SetDYDYZT(bdcs_djdy_gz.getBDCDYID(), dylx, "0");
				}
			}
		}
		this.SetSFDB();
		getCommonDao().flush();
		super.DyLimit();
		super.alterCachedXMXX();
		return true;
	}
	
	public boolean writeDJBEx() {
		if (isCForCFINGEx()) {
			return false;
		}
		CopyQL();// 拷贝权利
		CopyFSQL();// 拷贝权利
		CopyQLR();// 拷贝权利人
		CopyQDZR();// 拷贝权地证人
		CopyZS();//拷贝证书
		CopyDJDY();// 拷贝登记单元
		SetDYZT();//设定抵押状态
		this.SetSFDB();//设定登簿
		getCommonDao().flush();
		super.alterCachedXMXX();
		return true;
	}
	
	private boolean YCDYCanecl(){
		boolean bCancel=true;
		if(!BDCDYLX.H.equals(super.getBdcdylx())){
			return true;
		}
		
		ProjectInfo info=ProjectHelper.GetPrjInfoByXMBH(getXMBH());
		WFD_MAPPING mapping=new WFD_MAPPING();
		List<WFD_MAPPING> list_mapping=super.getCommonDao().getDataList(WFD_MAPPING.class, "WORKFLOWCODE='"+info.getBaseworkflowcode()+"'");
		if(list_mapping!=null&&list_mapping.size()>0){
			mapping=list_mapping.get(0);
		}
		
		if(!"1".equals(mapping.getDELYCDY())){
			return true;
		}
		String dbr = Global.getCurrentUserName();
		String strSqlXMBH = ProjectHelper.GetXMBHCondition(getXMBH());
		List<BDCS_DJDY_GZ> djdys = getCommonDao().getDataList(BDCS_DJDY_GZ.class, strSqlXMBH);
		if(djdys!=null&&djdys.size()>0){
			for(BDCS_DJDY_GZ djdy:djdys){
				String SCBDCDYID=djdy.getBDCDYID();
				List<YC_SC_H_XZ> listGX=getCommonDao().getDataList(YC_SC_H_XZ.class, "SCBDCDYID='"+SCBDCDYID+"'");
				if(listGX!=null&&listGX.size()>0){
					for(YC_SC_H_XZ gx:listGX){
						String YCBDCDYID=gx.getYCBDCDYID();
						if(StringHelper.isEmpty(YCBDCDYID)){
							continue;
						}
						List<BDCS_DJDY_XZ> djdys_yc=getCommonDao().getDataList(BDCS_DJDY_XZ.class, "BDCDYID='"+YCBDCDYID+"'");
						if(djdys_yc==null||djdys_yc.size()<=0){
							continue;
						}
						String ycdjdyid=djdys_yc.get(0).getDJDYID();
						List<Rights> ycyg_qls=RightsTools.loadRightsByCondition(DJDYLY.XZ, "DJDYID='"+ycdjdyid+"' AND QLLX='23'");
						if(ycyg_qls!=null&&ycyg_qls.size()>0){
							for(Rights ycyg_ql:ycyg_qls){
								String qlid=ycyg_ql.getId();
								RightsTools.deleteRightsAll(DJDYLY.XZ, qlid);
								// 更新历史附属权利信息
								SubRights subright = RightsTools.loadSubRightsByRightsID(DJDYLY.LS, qlid);
								if (subright != null) {
									subright.setZXSJ(new Date());
									subright.setZXDBR(dbr);
									subright.setZXDYYWH(super.getProject_id());
									getCommonDao().update(subright);
								}
							}
						}
					}
					
				}
			}
			getCommonDao().flush();
		}
		return bCancel;
	}
	

	@SuppressWarnings("rawtypes")
	public boolean isCForCFINGEx() {
		boolean b = false;
		StringBuilder builder_indeal=new StringBuilder();
		builder_indeal.append("SELECT DJDY.BDCDYLX,DJDY.BDCDYID,DJDY.LY ");
		builder_indeal.append("FROM BDCK.BDCS_DJDY_GZ DJDY LEFT JOIN BDCK.BDCS_QL_XZ QL ON QL.DJDYID=DJDY.DJDYID ");
		builder_indeal.append("WHERE QL.QLID IS NOT NULL AND QL.DJLX='800' AND DJDY.XMBH='");
		builder_indeal.append(super.getXMBH());
		builder_indeal.append("'");
		
		StringBuilder builder_deal=new StringBuilder();
		builder_deal.append("SELECT DJDY.BDCDYLX,DJDY.BDCDYID,DJDY.LY ");
		builder_deal.append("FROM BDCK.BDCS_DJDY_GZ DJDY LEFT JOIN BDCK.BDCS_QL_GZ QL ON QL.DJDYID=DJDY.DJDYID ");
		builder_deal.append("WHERE QL.DJSJ IS NULL AND QL.QLID IS NOT NULL AND  QL.DJLX='800' AND QL.QLLX='99' AND  DJDY.XMBH='");
		builder_deal.append(super.getXMBH());
		builder_deal.append("'");
		
		List<Map> list_deal =getCommonDao().getDataListByFullSql(builder_deal.toString());
		if(list_deal!=null&&list_deal.size()>0){
			for(Map djdy:list_deal){
				String bdcdylx=StringHelper.formatObject(djdy.get("BDCDYLX"));
				String bdcdyid=StringHelper.formatObject(djdy.get("BDCDYID"));
				String djdyly=StringHelper.formatObject(djdy.get("ly"));
				RealUnit unit=UnitTools.loadUnit(BDCDYLX.initFrom(bdcdylx), DJDYLY.initFrom(djdyly), bdcdyid);
				if(unit!=null){
					b = true;
					super.setErrMessage("不动产单元号为" + unit.getBDCDYH() + "的单元已经被查封或正在办理查封，不能登簿！");
					break;
				}
			}
		}else{
			List<Map> list_indeal =getCommonDao().getDataListByFullSql(builder_indeal.toString());
			if(list_indeal!=null&&list_indeal.size()>0){
				for(Map djdy:list_indeal){
					String bdcdylx=StringHelper.formatObject(djdy.get("BDCDYLX"));
					String bdcdyid=StringHelper.formatObject(djdy.get("BDCDYID"));
					String djdyly=StringHelper.formatObject(djdy.get("ly"));
					RealUnit unit=UnitTools.loadUnit(BDCDYLX.initFrom(bdcdylx), DJDYLY.initFrom(djdyly), bdcdyid);
					if(unit!=null){
						b = true;
						super.setErrMessage("不动产单元号为" + unit.getBDCDYH() + "的单元已经被查封或正在办理查封，不能登簿！");
						break;
					}
				}
			}else{
				if(BDCDYLX.H.equals(super.getBdcdylx())){
					StringBuilder builder_deal_yc=new StringBuilder();
					builder_deal_yc.append("SELECT GLDJDY.BDCDYLX,GLDJDY.BDCDYID,GLDJDY.LY ");
					builder_deal_yc.append("FROM BDCK.BDCS_DJDY_GZ GZDJDY ");
					builder_deal_yc.append("LEFT JOIN BDCK.YC_SC_H_XZ GX ON GX.YCBDCDYID=GZDJDY.BDCDYID ");
					builder_deal_yc.append("LEFT JOIN BDCK.BDCS_DJDY_XZ GLDJDY ON GX.SCBDCDYID=GLDJDY.BDCDYID ");
					builder_deal_yc.append("LEFT JOIN BDCK.BDCS_QL_XZ QL ON QL.DJDYID=GLDJDY.DJDYID ");
					builder_deal_yc.append("WHERE QL.QLID IS NOT NULL AND  QL.DJLX='800' AND GZDJDY.XMBH='");
					builder_deal_yc.append(super.getXMBH());
					builder_deal_yc.append("'");
					
					StringBuilder builder_indeal_yc=new StringBuilder();
					builder_indeal_yc.append("SELECT GLDJDY.BDCDYLX,GLDJDY.BDCDYID,GLDJDY.LY ");
					builder_indeal_yc.append("FROM BDCK.BDCS_DJDY_GZ GZDJDY ");
					builder_indeal_yc.append("LEFT JOIN BDCK.YC_SC_H_XZ GX ON GX.SCBDCDYID=GZDJDY.BDCDYID ");
					builder_indeal_yc.append("LEFT JOIN BDCK.BDCS_DJDY_GZ GLDJDY ON GX.YCBDCDYID=GLDJDY.BDCDYID ");
					builder_indeal_yc.append("LEFT JOIN BDCK.BDCS_QL_XZ QL ON QL.DJDYID=GLDJDY.DJDYID ");
					builder_indeal_yc.append("WHERE QL.QLID IS NOT NULL AND QL.DJSJ IS NOT NULL AND  QL.DJLX='800' AND QL.QLLX='99' AND GZDJDY.XMBH='");
					builder_indeal_yc.append(super.getXMBH());
					builder_indeal_yc.append("'");
					
					List<Map> list_deal_yc =getCommonDao().getDataListByFullSql(builder_deal_yc.toString());
					if(list_deal_yc!=null&&list_deal_yc.size()>0){
						for(Map djdy:list_deal_yc){
							String bdcdylx=StringHelper.formatObject(djdy.get("BDCDYLX"));
							String bdcdyid=StringHelper.formatObject(djdy.get("BDCDYID"));
							String djdyly=StringHelper.formatObject(djdy.get("ly"));
							RealUnit unit=UnitTools.loadUnit(BDCDYLX.initFrom(bdcdylx), DJDYLY.initFrom(djdyly), bdcdyid);
							if(unit!=null){
								b = true;
								super.setErrMessage("该房屋对应预测户的不动产单元号为" + unit.getBDCDYH() + "的单元已经被查封或正在办理查封，不能登簿！");
								break;
							}
						}
					}else{
						List<Map> list_indeal_yc =getCommonDao().getDataListByFullSql(builder_indeal_yc.toString());
						if(list_indeal_yc!=null&&list_indeal_yc.size()>0){
							for(Map djdy:list_indeal){
								String bdcdylx=StringHelper.formatObject(djdy.get("BDCDYLX"));
								String bdcdyid=StringHelper.formatObject(djdy.get("BDCDYID"));
								String djdyly=StringHelper.formatObject(djdy.get("ly"));
								RealUnit unit=UnitTools.loadUnit(BDCDYLX.initFrom(bdcdylx), DJDYLY.initFrom(djdyly), bdcdyid);
								if(unit!=null){
									b = true;
									super.setErrMessage("该房屋对应预测户的不动产单元号为" + unit.getBDCDYH() + "的单元已经被查封或正在办理查封，不能登簿！");
									break;
								}
							}
						}
					}
				}else if(BDCDYLX.YCH.equals(super.getBdcdylx())){
					StringBuilder builder_deal_sc=new StringBuilder();
					builder_deal_sc.append("SELECT GLDJDY.BDCDYLX,GLDJDY.BDCDYID,GLDJDY.LY ");
					builder_deal_sc.append("FROM BDCK.BDCS_DJDY_GZ GZDJDY ");
					builder_deal_sc.append("LEFT JOIN BDCK.YC_SC_H_XZ GX ON GX.YCBDCDYID=GZDJDY.BDCDYID ");
					builder_deal_sc.append("LEFT JOIN BDCK.BDCS_DJDY_XZ GLDJDY ON GX.SCBDCDYID=GLDJDY.BDCDYID ");
					builder_deal_sc.append("LEFT JOIN BDCK.BDCS_QL_XZ QL ON QL.DJDYID=GLDJDY.DJDYID ");
					builder_deal_sc.append("WHERE QL.QLID IS NOT NULL AND  QL.DJLX='800' AND GZDJDY.XMBH='");
					builder_deal_sc.append(super.getXMBH());
					builder_deal_sc.append("'");
					
					StringBuilder builder_indeal_sc=new StringBuilder();
					builder_indeal_sc.append("SELECT GLDJDY.BDCDYLX,GLDJDY.BDCDYID,GLDJDY.LY ");
					builder_indeal_sc.append("FROM BDCK.BDCS_DJDY_GZ GZDJDY ");
					builder_indeal_sc.append("LEFT JOIN BDCK.YC_SC_H_XZ GX ON GX.YCBDCDYID=GZDJDY.BDCDYID ");
					builder_indeal_sc.append("LEFT JOIN BDCK.BDCS_DJDY_GZ GLDJDY ON GX.SCBDCDYID=GLDJDY.BDCDYID ");
					builder_indeal_sc.append("LEFT JOIN BDCK.BDCS_QL_XZ QL ON QL.DJDYID=GLDJDY.DJDYID ");
					builder_indeal_sc.append("WHERE QL.QLID IS NOT NULL AND QL.DJSJ IS NOT NULL AND  QL.DJLX='800' AND QL.QLLX='99' AND GZDJDY.XMBH='");
					builder_indeal_sc.append(super.getXMBH());
					builder_indeal_sc.append("'");
					
					List<Map> list_deal_yc =getCommonDao().getDataListByFullSql(builder_deal_sc.toString());
					if(list_deal_yc!=null&&list_deal_yc.size()>0){
						for(Map djdy:list_deal_yc){
							String bdcdylx=StringHelper.formatObject(djdy.get("BDCDYLX"));
							String bdcdyid=StringHelper.formatObject(djdy.get("BDCDYID"));
							String djdyly=StringHelper.formatObject(djdy.get("ly"));
							RealUnit unit=UnitTools.loadUnit(BDCDYLX.initFrom(bdcdylx), DJDYLY.initFrom(djdyly), bdcdyid);
							if(unit!=null){
								b = true;
								super.setErrMessage("该房屋对应实测户的不动产单元号为" + unit.getBDCDYH() + "的单元已经被查封或正在办理查封，不能登簿！");
								break;
							}
						}
					}else{
						List<Map> list_indeal_yc =getCommonDao().getDataListByFullSql(builder_indeal_sc.toString());
						if(list_indeal_yc!=null&&list_indeal_yc.size()>0){
							for(Map djdy:list_indeal){
								String bdcdylx=StringHelper.formatObject(djdy.get("BDCDYLX"));
								String bdcdyid=StringHelper.formatObject(djdy.get("BDCDYID"));
								String djdyly=StringHelper.formatObject(djdy.get("ly"));
								RealUnit unit=UnitTools.loadUnit(BDCDYLX.initFrom(bdcdylx), DJDYLY.initFrom(djdyly), bdcdyid);
								if(unit!=null){
									b = true;
									super.setErrMessage("该房屋对应实测户的不动产单元号为" + unit.getBDCDYH() + "的单元已经被查封或正在办理查封，不能登簿！");
									break;
								}
							}
						}
					}
				}
			}
		}
		return b;
	}
	
	/**
	 * 批量拷贝权利
	 * 
	 * @Author 俞学斌
	 * @CreateTime 2016年09月05日 15:45:34
	 */
	protected boolean CopyQL() {
		String dbr = Global.getCurrentUserName();
		String xmbhFilter = ProjectHelper.GetXMBHCondition(super.getXMBH());
		List<BDCS_QL_GZ> qls = getCommonDao().getDataList(BDCS_QL_GZ.class, xmbhFilter);
		if (qls != null && qls.size() > 0) {
			for (int iql = 0; iql < qls.size(); iql++) {
				BDCS_QL_GZ bdcs_ql_gz = qls.get(iql);
				// 登记时间，登簿人，登记机构
				bdcs_ql_gz.setDBR(dbr);
				bdcs_ql_gz.setDJSJ(new Date());
				bdcs_ql_gz.setDJJG(ConfigHelper.getNameByValue("DJJGMC"));
				getCommonDao().getCurrentSession().update(bdcs_ql_gz);
				// 拷贝权利
				BDCS_QL_XZ bdcs_ql_xz = ObjectHelper.copyQL_GZToXZ(bdcs_ql_gz);
				getCommonDao().save(bdcs_ql_xz);
				BDCS_QL_LS bdcs_ql_ls = ObjectHelper.copyQL_XZToLS(bdcs_ql_xz);
				getCommonDao().save(bdcs_ql_ls);
			}
		}
		return true;
	}
	/**
	 * 批量拷贝附属权利
	 * 
	 * @Author 俞学斌
	 * @CreateTime 2016年09月05日 15:45:34
	 */
	protected boolean CopyFSQL() {
		String xmbhFilter = ProjectHelper.GetXMBHCondition(super.getXMBH());
		List<BDCS_FSQL_GZ> fsqls = getCommonDao().getDataList(BDCS_FSQL_GZ.class, xmbhFilter);
		if (fsqls != null && fsqls.size() > 0) {
			for (int ifsql = 0; ifsql < fsqls.size(); ifsql++) {
				BDCS_FSQL_GZ bdcs_fsql_gz = fsqls.get(ifsql);
				BDCS_FSQL_XZ bdcs_fsql_xz = ObjectHelper.copyFSQL_GZToXZ(bdcs_fsql_gz);
				getCommonDao().save(bdcs_fsql_xz);
				BDCS_FSQL_LS bdcs_fsql_ls = ObjectHelper.copyFSQL_XZToLS(bdcs_fsql_xz);
				getCommonDao().save(bdcs_fsql_ls);
			}
		}
		return true;
	}
	
	/**
	 * 批量拷贝权利人
	 * 
	 * @Author 俞学斌
	 * @CreateTime 2016年09月05日 15:45:34
	 */
	protected boolean CopyQLR() {
		String xmbhFilter = ProjectHelper.GetXMBHCondition(super.getXMBH());
		List<BDCS_QLR_GZ> qlrs = getCommonDao().getDataList(BDCS_QLR_GZ.class, xmbhFilter);
		if (qlrs != null && qlrs.size() > 0) {
			for (int iqlr = 0; iqlr < qlrs.size(); iqlr++) {
				BDCS_QLR_GZ bdcs_qlr_gz = qlrs.get(iqlr);
				BDCS_QLR_XZ bdcs_qlr_xz = ObjectHelper.copyQLR_GZToXZ(bdcs_qlr_gz);
				getCommonDao().save(bdcs_qlr_xz);
				BDCS_QLR_LS bdcs_qlr_ls = ObjectHelper.copyQLR_XZToLS(bdcs_qlr_xz);
				getCommonDao().save(bdcs_qlr_ls);
			}
		}
		return true;
	}
	
	/**
	 * 批量拷贝权地证人
	 * 
	 * @Author 俞学斌
	 * @CreateTime 2016年09月05日 15:45:34
	 */
	protected boolean CopyQDZR() {
		String xmbhFilter = ProjectHelper.GetXMBHCondition(super.getXMBH());
		List<BDCS_QDZR_GZ> qdzrs = getCommonDao().getDataList(BDCS_QDZR_GZ.class, xmbhFilter);
		if (qdzrs != null && qdzrs.size() > 0) {
			for (int iqdzr = 0; iqdzr < qdzrs.size(); iqdzr++) {
				BDCS_QDZR_GZ bdcs_qdzr_gz = qdzrs.get(iqdzr);
				BDCS_QDZR_XZ bdcs_qdzr_xz = ObjectHelper.copyQDZR_GZToXZ(bdcs_qdzr_gz);
				getCommonDao().save(bdcs_qdzr_xz);
				BDCS_QDZR_LS bdcs_qdzr_ls = ObjectHelper.copyQDZR_XZToLS(bdcs_qdzr_xz);
				getCommonDao().save(bdcs_qdzr_ls);
			}
		}
		return true;
	}
	
	/**
	 * 批量拷贝证书
	 * 
	 * @Author 俞学斌
	 * @CreateTime 2016年09月05日 15:45:34
	 */
	protected boolean CopyZS() {
		String xmbhFilter = ProjectHelper.GetXMBHCondition(super.getXMBH());
		List<BDCS_ZS_GZ> zss = getCommonDao().getDataList(BDCS_ZS_GZ.class, xmbhFilter);
		if (zss != null && zss.size() > 0) {
			for (int izs = 0; izs < zss.size(); izs++) {
				BDCS_ZS_GZ bdcs_zs_gz = zss.get(izs);
				BDCS_ZS_XZ bdcs_zs_xz = ObjectHelper.copyZS_GZToXZ(bdcs_zs_gz);
				getCommonDao().save(bdcs_zs_xz);
				BDCS_ZS_LS bdcs_zs_ls = ObjectHelper.copyZS_XZToLS(bdcs_zs_xz);
				getCommonDao().save(bdcs_zs_ls);
			}
		}
		return true;
	}
	
	/**
	 * 批量拷贝登记单元
	 * 
	 * @Author 俞学斌
	 * @CreateTime 2016年09月05日 15:45:34
	 */
	@SuppressWarnings("unchecked")
	protected boolean CopyDJDY() {
		String xmbhFilter = ProjectHelper.GetXMBHCondition(super.getXMBH());
		String strsql="SELECT * FROM BDCK.BDCS_DJDY_GZ WHERE NOT EXISTS (SELECT 1 FROM BDCK.BDCS_DJDY_XZ WHERE BDCS_DJDY_GZ.DJDYID=BDCS_DJDY_XZ.DJDYID AND BDCDYLX='"+super.getBdcdylx().Value+"') AND "+xmbhFilter;
		List<BDCS_DJDY_GZ> djdys=getCommonDao().getCurrentSession().createSQLQuery(strsql).addEntity(BDCS_DJDY_GZ.class).list();
		if (djdys != null && djdys.size() > 0) {
			for (int idjdy = 0; idjdy < djdys.size(); idjdy++) {
				BDCS_DJDY_GZ bdcs_djdy_gz = djdys.get(idjdy);
				BDCS_DJDY_XZ bdcs_djdy_xz = ObjectHelper.copyDJDY_GZToXZ(bdcs_djdy_gz);
				getCommonDao().save(bdcs_djdy_xz);
				BDCS_DJDY_LS bdcs_djdy_ls = ObjectHelper.copyDJDY_XZToLS(bdcs_djdy_xz);
				getCommonDao().save(bdcs_djdy_ls);
			}
		}
		return true;
	}
	
	/**
	 * 批量设定抵押状态
	 * 
	 * @Author 俞学斌
	 * @CreateTime 2016年09月05日 15:45:34
	 */
	protected boolean SetDYZT() {
		List<BDCS_H_XZY> dys_xz = getCommonDao().getDataList(BDCS_H_XZY.class, "id IN (SELECT BDCDYID FROM BDCS_DJDY_GZ WHERE XMBH='"+super.getXMBH()+"')");
		if (dys_xz != null && dys_xz.size() > 0) {
			for (int idy = 0; idy < dys_xz.size(); idy++) {
				BDCS_H_XZY bdcs_dy_xz = dys_xz.get(idy);
				bdcs_dy_xz.setDYZT("0");
				getCommonDao().update(bdcs_dy_xz);
			}
		}
		List<BDCS_H_LSY> dys_ls = getCommonDao().getDataList(BDCS_H_LSY.class, "id IN (SELECT BDCDYID FROM BDCS_DJDY_GZ WHERE XMBH='"+super.getXMBH()+"')");
		if (dys_ls != null && dys_ls.size() > 0) {
			for (int idy = 0; idy < dys_ls.size(); idy++) {
				BDCS_H_LSY bdcs_dy_ls = dys_ls.get(idy);
				bdcs_dy_ls.setDYZT("0");
				getCommonDao().update(bdcs_dy_ls);
			}
		}
		return true;
	}
	

	/**
	 * 移除登记产单元
	 */
	@Override
	public boolean removeBDCDY(String bdcdyid) {
		boolean bsuccess = false;
		CommonDao baseCommonDao = this.getCommonDao();
		BDCS_DJDY_GZ djdy = super.removeDJDY(bdcdyid);
		if (djdy != null) {
			String djdyid = djdy.getDJDYID();
			// 删除登记单元索引
			baseCommonDao.deleteEntity(djdy);

			//删除权利、附属权利、权利人、证书、权地证人关系
			String _hqlCondition=MessageFormat.format("XMBH=''{0}'' AND DJDYID=''{1}''", getXMBH(),djdyid);
			RightsTools.deleteRightsAllByCondition(DJDYLY.GZ, _hqlCondition);
			
			// 删除权利关联申请人
			super.RemoveSQRByQLID(djdyid, getXMBH());
		}
		baseCommonDao.flush();
		bsuccess = true;
		return bsuccess;
	}

	/**
	 * 获取不动产单元列表
	 */
	@Override
	public List<UnitTree> getUnitTree() {
		List<UnitTree> list=super.getUnitList();
		
		CommonDao baseCommonDao = this.getCommonDao();
		if(list!=null&&list.size()>0){
			BDCS_QL_GZ QLs=baseCommonDao.get(BDCS_QL_GZ.class, list.get(0).getQlid());
			BDCS_XMXX xmxx = baseCommonDao.get(BDCS_XMXX.class, QLs.getXMBH());
			if(xmxx!=null&&"1".equals(xmxx.getSFDB())){
				for (UnitTree unitTree : list) {
					BDCS_QL_GZ QL=baseCommonDao.get(BDCS_QL_GZ.class, unitTree.getQlid());
					unitTree.setOldqlid(QL.getLYQLID());
				}
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
	 * 获取错误信息
	 * 
	 * @作者 刘树峰
	 * @创建时间 2015年6月23日下午8:11:43
	 * @return
	 */
	@Override
	public String getError() {
		return null;
	}

	/**
	 * 移除权利人
	 */
	@Override
	public void removeQLR(String qlid, String qlrid) {
		super.removeqlr(qlrid, qlid);
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
			boolean isZipFile = false;
			if(djdys.size()>=10){
				isZipFile=true;
				String folderPath=GetProperties.getConstValueByKey("xmlPath") + "\\" + xmxx.getPROJECT_ID()+"_"+bljc;
				super.getShareMsgTools().deleteFolder(folderPath);
			}
			for (int idjdy = 0; idjdy < djdys.size(); idjdy++) {
				BDCS_DJDY_GZ djdy = djdys.get(idjdy);
				ConstValue.BDCDYLX dylx = ConstValue.BDCDYLX.initFrom(djdy.getBDCDYLX());
				ConstValue.DJDYLY dyly = ConstValue.DJDYLY.initFrom(djdy.getLY());
				RealUnit bdcdy = UnitTools.loadUnit(dylx, dyly, djdy.getBDCDYID());
				Rights bdcql=RightsTools.loadRightsByDJDYID(ConstValue.DJDYLY.GZ, getXMBH(), djdy.getDJDYID());
				SubRights bdcfsql=RightsTools.loadSubRightsByRightsID(ConstValue.DJDYLY.GZ, bdcql.getId());
				List<RightsHolder> bdcqlrs=RightsHolderTools.loadRightsHolders(ConstValue.DJDYLY.GZ, djdy.getDJDYID(), getXMBH());
				MessageExport msg= super.getShareMsgTools().GetMsg(bdcdy,bdcql,bdcfsql,bdcqlrs,bljc,xmxx);
				if(isZipFile){
					String folderPath = super.getShareMsgTools().createXMLInFile(xmxx, msg, idjdy+1,bljc);
					if(idjdy == djdys.size()-1){//文件都生成到文件夹以后再压缩上传
						super.getShareMsgTools().SendMsg(folderPath, xmxx, bljc, djdy.getBDCDYLX());
					}
				}
				else{
					super.getShareMsgTools().SendMsg(msg, idjdy + 1, djdy.getBDCDYLX(), xmxx);				
				}
			}
		}
	}

	/************************ 内部方法 *********************************/

	protected String getDYBDCLXfromBDCDYLX(BDCDYLX bdcdylx) {
		String dybdclx = "";
		if (bdcdylx.equals(BDCDYLX.SHYQZD) || bdcdylx.equals(BDCDYLX.SYQZD)|| bdcdylx.equals(BDCDYLX.NYD)) {
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

	@Override
	public Map<String,String> exportXML(String path, String actinstID) {
		Marshaller mashaller;
		Map<String,String> names = new HashMap<String,String>();
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
				for(int i = 0; i < djdys.size(); i ++){
					BDCS_DJDY_GZ djdy = djdys.get(i);
					
					String condition = MessageFormat.format("DJDYID=''{0}'' AND QLLX=''23'' ", djdy.getDJDYID());
					List<Rights> _rightss = RightsTools.loadRightsByCondition(DJDYLY.GZ, condition);
					BDCS_QL_GZ ql = null;
					List<BDCS_QLR_GZ> qlrs = new ArrayList<BDCS_QLR_GZ>();
					for (Rights rights : _rightss) {
						ql = (BDCS_QL_GZ) rights;
						qlrs = super.getQLRs(ql.getId());
						if(qlrs != null){
							break;
						}
					}
					List<BDCS_SQR> sqrs = new ArrayList<BDCS_SQR>();
					ProjectServiceImpl serviceImpl = SuperSpringContext.getContext().getBean(ProjectServiceImpl.class);
					sqrs = serviceImpl.getSQRList(super.getXMBH());
					
					BDCS_FSQL_GZ fsql = new BDCS_FSQL_GZ();
					if(ql != null && !StringUtils.isEmpty(ql.getFSQLID())){
						fsql = dao.get(BDCS_FSQL_GZ.class, ql.getFSQLID());
					}
					Message msg = exchangeFactory.createMessageByDYQ();
					if (BDCDYLX.SHYQZD.Value.equals(djdy.getBDCDYLX()) || QLLX.ZJDSYQ.Value.equals(this.getQllx().Value)) { //使用权宗地、宅基地使用权
						msg.getHead().setRecType("9000101");
						try {
							BDCS_SHYQZD_XZ zd = dao.get(BDCS_SHYQZD_XZ.class, djdy.getBDCDYID());
							super.fillHead(msg, i, ywh,zd.getBDCDYH(),zd.getQXDM(),ql.getLYQLID());
							msg.getHead().setParcelID(StringHelper.formatObject(zd.getZDDM()));
							msg.getHead().setEstateNum(StringHelper.formatObject(zd.getBDCDYH()));
//							msg.getHead().setPreEstateNum(StringHelper.formatObject(zd.getBDCDYH()));
							if(zd != null && !StringUtils.isEmpty(zd.getQXDM())){
								msg.getHead().setAreaCode(zd.getQXDM());
							}
							
							if (djdy != null) {
								QLFQLDYAQ dyaq = msg.getData().getQLFQLDYAQ();
								dyaq = packageXml.getQLFQLDYAQ(dyaq, zd, ql, fsql, ywh, null);

								List<ZTTGYQLR> zttqlr = msg.getData().getGYQLR();
								zttqlr = packageXml.getZTTGYQLRs(zttqlr, qlrs, zd, ql,null, null, null, null);
								msg.getData().setGYQLR(zttqlr);

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
					
					if(BDCDYLX.H.Value.equals(djdy.getBDCDYLX())){ //房屋
						try {
							BDCS_H_XZ h  = dao.get(BDCS_H_XZ.class, djdy.getBDCDYID());
							if(h != null){
								BDCS_SHYQZD_XZ zd = dao.get(BDCS_SHYQZD_XZ.class, h.getZDBDCDYID());
								h.setZDDM(zd.getZDDM());
							}
							super.fillHead(msg, i, ywh,h.getBDCDYH(),h.getQXDM(),ql.getLYQLID());
							msg.getHead().setParcelID(StringHelper.formatObject(h.getZDDM()));
							msg.getHead().setEstateNum(StringHelper.formatObject(h.getBDCDYH()));
//							msg.getHead().setPreEstateNum(StringHelper.formatObject(h.getBDCDYH()));
							
							msg.getHead().setRecType("9000101");
							if(h != null && !StringUtils.isEmpty(h.getQXDM())){
								msg.getHead().setAreaCode(h.getQXDM());
							}
							if (djdy != null) {
								
								QLFQLDYAQ dyaq = msg.getData().getQLFQLDYAQ();
								dyaq = packageXml.getQLFQLDYAQ(dyaq, null, ql, fsql, ywh, h);
								
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
								
								List<ZTTGYQLR> zttqlr = msg.getData().getGYQLR();
								zttqlr = packageXml.getZTTGYQLRs(zttqlr, qlrs, null, ql, h, null, null, null);
								msg.getData().setGYQLR(zttqlr);

								DJTDJSLSQ sq = msg.getData().getDJSLSQ();
								sq = packageXml.getDJTDJSLSQ(sq, ywh, null, ql, xmxx, h, xmxx.getSLSJ(), null, null, null);
								
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
					BDCS_H_XZY xzy = null;
					if(BDCDYLX.YCH.Value.equals(djdy.getBDCDYLX())){ //在建工程
						try {
							xzy = dao.get(BDCS_H_XZY.class, djdy.getBDCDYID());
							if(xzy != null){
								BDCS_SHYQZD_XZ zd = dao.get(BDCS_SHYQZD_XZ.class, xzy.getZDBDCDYID());
								xzy.setZDDM(zd.getZDDM());
							}
							super.fillHead(msg, i, ywh,xzy.getBDCDYH(),xzy.getQXDM(),ql.getLYQLID());
							msg.getHead().setParcelID(StringHelper.formatObject(xzy.getZDDM()));
							msg.getHead().setEstateNum(StringHelper.formatObject(xzy.getBDCDYH()));
//							msg.getHead().setPreEstateNum(StringHelper.formatObject(xzy.getBDCDYH()));
							if(xzy != null && !StringUtils.isEmpty(xzy.getQXDM())){
								msg.getHead().setAreaCode(xzy.getQXDM());
							}
							if (djdy != null) {
								
								QLFQLDYAQ dyaq = msg.getData().getQLFQLDYAQ();
								dyaq = packageXml.getQLFQLDYAQ(dyaq, null, ql, fsql, ywh, xzy);
								
								List<DJFDJSJ> sj = msg.getData().getDJSJ();
								sj = packageXml.getDJFDJSJ(xzy, ywh,actinstID);
								msg.getData().setDJSJ(sj);
								
								//9.登记收费(可选)
								List<DJFDJSF> sfList = msg.getData().getDJSF();
								sfList = packageXml.getDJSF(xzy, ywh,this.getXMBH());
								msg.getData().setDJSF(sfList);

								//10.审核(可选)
								List<DJFDJSH> sh = msg.getData().getDJSH();
								 sh = packageXml.getDJFDJSH(xzy, ywh, this.getXMBH(), actinstID);
								msg.getData().setDJSH(sh);

								//11.缮证(可选)
								List<DJFDJSZ>  sz = packageXml.getDJFDJSZ(xzy, ywh, this.getXMBH());
								msg.getData().setDJSZ(sz);

								//11.发证(可选)
								List<DJFDJFZ>   fz = packageXml.getDJFDJFZ(xzy, ywh, this.getXMBH());
								msg.getData().setDJFZ(fz);
								
								//12.归档(可选)
								List<DJFDJGD> gd = packageXml.getDJFDJGD(xzy, ywh, this.getXMBH());
								msg.getData().setDJGD(gd);	
								
								List<ZTTGYQLR> zttqlr = msg.getData().getGYQLR();
								zttqlr = packageXml.getZTTGYQLRs(zttqlr, qlrs, null, ql, xzy, null, null, null);
								msg.getData().setGYQLR(zttqlr);

								DJTDJSLSQ sq = msg.getData().getDJSLSQ();
								sq = packageXml.getDJTDJSLSQ(sq, ywh, null, ql, xmxx, xzy, xmxx.getSLSJ(), null, null, null);
								
								List<DJFDJSQR> djsqrs = msg.getData().getDJSQR();
								djsqrs = packageXml.getDJSQRs(djsqrs, sqrs, xzy.getYSDM(), ywh, xzy.getBDCDYH());
								msg.getData().setDJSQR(djsqrs);
								
								FJF100 fj = msg.getData().getFJF100();
								fj = packageXml.getFJF(fj);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					
					if("HY".equals(this.getBdcdylx().toString())){ //海域
						try {
							String zhdm=null;
							String hql=null;
							YHZK yhzk_gz = null;
							Sea zh = dao.get(BDCS_ZH_GZ.class, djdy.getBDCDYID());
							if (zh==null) {
								zh=dao.get(BDCS_ZH_XZ.class, djdy.getBDCDYID());
							}
							if (null != zh) {
							 zhdm=zh.getZHDM();
							 hql = "BDCDYID = '" + zh.getId() + "' ";
								List<BDCS_YHZK_GZ> yhzks = dao.getDataList(BDCS_YHZK_GZ.class, hql);
								if (yhzks != null && yhzks.size() > 0) {
									yhzk_gz = yhzks.get(0);
								}else {
									List<BDCS_YHZK_XZ> yhzksxz = dao.getDataList(BDCS_YHZK_XZ.class, hql);
									if (yhzksxz != null && yhzksxz.size() > 0) {
										yhzk_gz = yhzksxz.get(0);
									}
								}
							}
							if(zhdm!=null&&zhdm.length()>0&&(yhzk_gz.getZHDM()==null)||yhzk_gz.getZHDM().length()>0) {
								//维护数据
								yhzk_gz.setZHDM(zhdm);
							}
							// 这些字段先手动赋值 diaoliwei
							if (zh != null) {
								if (StringUtils.isEmpty(zh.getZHT())) {
									zh.setZHT("无");
								}
							}
								//设置报文头
								this.fillHead(msg, i, ywh,zh.getBDCDYH(),zh.getQXDM(),ql.getLYQLID());
								msg.getHead().setParcelID(StringHelper.formatObject(zh.getZHDM()));
								msg.getHead().setEstateNum(StringHelper.formatObject(zh.getBDCDYH()));
//								msg.getHead().setPreEstateNum(StringHelper.formatObject(zh.getBDCDYH()));
							if (djdy != null) {
								
								QLFQLDYAQ dyaq = msg.getData().getQLFQLDYAQ();
								dyaq = packageXml.getQLFQLDYAQ(dyaq, null, ql, fsql, ywh, xzy);
								//获取不动产抵押单元类型
								if((dyaq.getDybdclx()==null||dyaq.getDybdclx().length()<=0)&&fsql!=null&&fsql.getDYWLX()!=null&&fsql.getDYWLX().length()>0) {
									dyaq.setDybdclx(fsql.getDYWLX());
								}
								//维护区县代码
								if((dyaq.getQxdm()==null||dyaq.getQxdm().length()<=0)&&ql!=null&&ql.getQXDM()!=null&&ql.getQXDM().length()>0) {
									dyaq.setQxdm(ql.getQXDM());
								}
								msg.getData().setQLFQLDYAQ(dyaq);
								
								//3.非结构化文档
								FJF100 fj = msg.getData().getFJF100();
								fj = packageXml.getFJF(fj);
								msg.getData().setFJF100(fj);
								
								//5.宗海变化状况表(可选 )
								KTFZHYHZK yhzk = msg.getData().getKTFZHYHZK();
								if(yhzk!=null) {
									yhzk = packageXml.getKTFZHYHZK(yhzk, yhzk_gz, zh.getBDCDYH());
									msg.getData().setKTFZHYHZK(yhzk);
								}
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
								
								List<DJFDJSQR> djsqrs = msg.getData().getDJSQR();
								djsqrs = packageXml.getDJSQRs(djsqrs, sqrs, zh.getYSDM(), ywh, zh.getBDCDYH());
								msg.getData().setDJSQR(djsqrs);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					
					
					
					
					
					
					mashaller.marshal(msg, new File(path + "Biz" + msg.getHead().getBizMsgID()+ ".xml"));
					result = uploadFile(path + "Biz" + msg.getHead().getBizMsgID()+ ".xml",ConstValue.RECCODE.DIYQ_ZXDJ.Value,actinstID,djdy.getDJDYID(),ql.getId());
					names.put(djdy.getDJDYID(),msg.getHead().getBizMsgID()+ ".xml");
					
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

}
