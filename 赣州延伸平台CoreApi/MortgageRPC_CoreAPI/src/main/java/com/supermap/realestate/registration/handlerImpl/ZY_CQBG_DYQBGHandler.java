package com.supermap.realestate.registration.handlerImpl;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
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
import com.supermap.realestate.registration.dataExchange.utils.packageXml;
import com.supermap.realestate.registration.handler.DJHandler;
import com.supermap.realestate.registration.model.BDCS_C_GZ;
import com.supermap.realestate.registration.model.BDCS_DJDY_GZ;
import com.supermap.realestate.registration.model.BDCS_DJDY_LS;
import com.supermap.realestate.registration.model.BDCS_DJDY_XZ;
import com.supermap.realestate.registration.model.BDCS_DYBG;
import com.supermap.realestate.registration.model.BDCS_FSQL_GZ;
import com.supermap.realestate.registration.model.BDCS_FSQL_LS;
import com.supermap.realestate.registration.model.BDCS_FSQL_XZ;
import com.supermap.realestate.registration.model.BDCS_H_GZ;
import com.supermap.realestate.registration.model.BDCS_H_LS;
import com.supermap.realestate.registration.model.BDCS_H_LSY;
import com.supermap.realestate.registration.model.BDCS_H_XZ;
import com.supermap.realestate.registration.model.BDCS_H_XZY;
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
import com.supermap.realestate.registration.model.BDCS_SHYQZD_GZ;
import com.supermap.realestate.registration.model.BDCS_SHYQZD_LS;
import com.supermap.realestate.registration.model.BDCS_SHYQZD_XZ;
import com.supermap.realestate.registration.model.BDCS_SLLM_GZ;
import com.supermap.realestate.registration.model.BDCS_SQR;
import com.supermap.realestate.registration.model.BDCS_SYQZD_GZ;
import com.supermap.realestate.registration.model.BDCS_XMXX;
import com.supermap.realestate.registration.model.BDCS_ZH_GZ;
import com.supermap.realestate.registration.model.BDCS_ZRZ_GZ;
import com.supermap.realestate.registration.model.BDCS_ZRZ_LS;
import com.supermap.realestate.registration.model.BDCS_ZRZ_XZ;
import com.supermap.realestate.registration.model.BDCS_ZS_GZ;
import com.supermap.realestate.registration.model.BDCS_ZS_LS;
import com.supermap.realestate.registration.model.BDCS_ZS_XZ;
import com.supermap.realestate.registration.model.WFD_MAPPING;
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
import com.supermap.realestate.registration.util.ConstValue;
import com.supermap.realestate.registration.util.ConstValue.BDCDYLX;
import com.supermap.realestate.registration.util.ConstValue.DJDYLY;
import com.supermap.realestate.registration.util.ConstValue.DJZT;
import com.supermap.realestate.registration.util.ConstValue.QLLX;
import com.supermap.realestate.registration.util.ConstValue.SF;
import com.supermap.realestate.registration.util.ConstValue.SQRLB;
import com.supermap.realestate.registration.util.Global;
import com.supermap.realestate.registration.util.ObjectHelper;
import com.supermap.realestate.registration.util.ProjectHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.realestate.registration.util.YwLogUtil;
import com.supermap.wisdombusiness.core.SuperHelper;
import com.supermap.wisdombusiness.core.SuperSpringContext;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;

/*	
 * 数据来源：从权籍系统中权籍调查中来，
 * 主要针对地与房都发生变化的，也满足基本流程
 * 1:1(单独的房屋信息跟宗地的信息变化的),1：N(分割),N:1(合并)  
 1、国有建设用地使用权/房屋所有权变更登记
 */
/**
 * 变更登记扩展处理类
 * @ClassName: BGDJEXHandler
 * @author sunhb
 * @date 2016年4月24日 下午16:27:20
 */
public class ZY_CQBG_DYQBGHandler extends DJHandlerBase implements DJHandler {

	/**
	 * 构造函数
	 * 
	 * @param info
	 */
	public ZY_CQBG_DYQBGHandler(ProjectInfo info) {
		super(info);
	}

	/**
	 * 添加不动产单元
	 */
	@Override
	public boolean addBDCDY(String bdcdyid) {
		CommonDao dao = this.getCommonDao();
		String[] ids = bdcdyid.split(",");
		boolean zdflag=false;
		boolean zrzflag=false;
		if (ids == null || ids.length <= 0) {
			return false;
		}
		for (int i = 0; i < ids.length; i++) {
			String id = ids[i];
			if (StringUtils.isEmpty(id)) {
				continue;
			}		
			RealUnit dy = UnitTools.loadUnit(this.getBdcdylx(), DJDYLY.XZ, id);
			List<BDCS_DJDY_GZ> djdys_bgh = getCommonDao().getDataList(BDCS_DJDY_GZ.class, "XMBH='"+getXMBH()+"' AND LY='01'");
			if (dy != null) {
				//判断是否先选择了变更后单元
				if (djdys_bgh != null && djdys_bgh.size() > 0  ) {
					super.setErrMessage("已选择变更后单元，请先选择变更前单元再选择变更后单元");
					return false;
				} 
				
				BDCS_DJDY_GZ djdy = super.createDJDYfromXZ(id);
				if (djdy != null) {
					//判断单元是否存在抵押权信息
					StringBuilder builer = new StringBuilder();
					builer.append("SELECT DYQL.QLID AS QLID FROM BDCK.BDCS_QL_XZ DYQL ");
					builer.append("LEFT JOIN BDCK.BDCS_DJDY_XZ DJDY ON DYQL.DJDYID=DJDY.DJDYID ");
					builer.append("AND DJDY.BDCDYID='");
					builer.append(bdcdyid);
					builer.append("' ");
					builer.append("WHERE DYQL.QLLX='23' AND DJDY.BDCDYID IS NOT NULL");
					List<Map> listDYQ = dao.getDataListByFullSql(builer.toString());
					if (listDYQ == null || listDYQ.size() == 0) {
						super.setErrMessage("房屋单元未抵押！");
						return false;
					}
					
					List<Rights> qls = RightsTools.loadRightsByCondition(DJDYLY.XZ, "DJDYID='" + djdy.getDJDYID() + "' AND QLLX='" + getQllx().Value + "'");
					if (qls != null && qls.size() > 0) {
						List<BDCS_QLR_XZ> qlrs = dao.getDataList(BDCS_QLR_XZ.class, "QLID='" + qls.get(0).getId() + "'");
						if (qlrs != null && qlrs.size() > 0) {
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
									List<BDCS_SQR> sqrlist = dao.getDataList(BDCS_SQR.class, Sql);
									if (sqrlist != null && sqrlist.size() > 0) {
										bexists = true;
									}
								}
								if (!bexists) {
									BDCS_SQR sqr = super.copyXZQLRtoSQR(qlrs.get(iqlr), SQRLB.YF);
									if (sqr != null) {
										dao.save(sqr);
									}
								}
							}
						}
					}
					dao.save(djdy);
				}
			} else {
				//判断是否先选择了变更前单元
				List<BDCS_DJDY_GZ> djdys_bgq = getCommonDao().getDataList(BDCS_DJDY_GZ.class, "XMBH='"+getXMBH()+"' AND LY='02'");
				if (djdys_bgq == null || djdys_bgq.size() == 0  ) {				
					super.setErrMessage("未选择变更前单元！");
					return false;
				}
				
				RealUnit dy_dc =UnitTools.loadUnit(this.getBdcdylx(), DJDYLY.DC, bdcdyid);
				if(!StringHelper.isEmpty(dy_dc)){
					dy_dc.setDJZT(DJZT.DJZ.Value);
					dao.save(dy_dc);
				    dy=UnitTools.copyUnit(dy_dc, this.getBdcdylx(), DJDYLY.GZ);
				    if(!StringHelper.isEmpty(dy)){
				    	dy.setXMBH(this.getXMBH());
				    }
					//先判断h对应的宗地跟自然幢在XZ层有没有，没有从BDCDCK拷贝，ZRZ跟ZD
					if(dy instanceof House && BDCDYLX.H.equals(this.getBdcdylx())){
						House h=(House) dy;
						String zrzbdcdyid=h.getZRZBDCDYID();
						RealUnit zrz_xz=UnitTools.loadUnit(BDCDYLX.ZRZ, DJDYLY.XZ, zrzbdcdyid);
						//考虑到分割时，可能添加第一个单元时，没有自然幢信息跟宗地信息，再添加单元时，有自然幢信息跟宗地信息，就不添加了
						if(StringHelper.isEmpty(zrz_xz)){
							if(!zrzflag){
								RealUnit zrz_dc=UnitTools.loadUnit(BDCDYLX.ZRZ, DJDYLY.DC, zrzbdcdyid);
								if(!StringHelper.isEmpty(zrz_dc)){
									RealUnit zrz_gz=UnitTools.copyUnit(zrz_dc, BDCDYLX.ZRZ, DJDYLY.GZ);
									super.CopyGeo(h.getId(), this.getBdcdylx(), DJDYLY.DC);
									zrz_dc.setDJZT(DJZT.DJZ.Value);
									zrz_gz.setXMBH(this.getXMBH());
									dao.update(zrz_gz);
									dao.update(zrz_dc);
								}
								zrzflag=true;
							}
						}
						String zdbdcdyid=h.getZDBDCDYID();
						RealUnit zd_xz=UnitTools.loadUnit(BDCDYLX.SHYQZD, DJDYLY.XZ, zdbdcdyid);
						if(StringHelper.isEmpty(zd_xz)){
							if(!zdflag){
								RealUnit zd_dc=UnitTools.loadUnit(BDCDYLX.SHYQZD, DJDYLY.DC, zdbdcdyid);
								if(!StringHelper.isEmpty(zd_dc)){
									zd_dc.setDJZT(DJZT.DJZ.Value);
									RealUnit zd_gz=UnitTools.copyUnit(zd_dc, BDCDYLX.SHYQZD, DJDYLY.GZ);
									super.CopyGeo(zdbdcdyid, BDCDYLX.SHYQZD, DJDYLY.DC);//拷贝图形信息
									zd_gz.setXMBH(this.getXMBH());
									 dao.save(zd_gz);
									 dao.save(zd_dc);
								}
								zdflag=true;
							}
						}
					}
				}
				// 登记单元索引表
				BDCS_DJDY_GZ djdy = super.createDJDYfromGZ(dy);
				if (djdy != null) {
					BDCS_QL_GZ ql = super.createQL(djdy, dy);
					BDCS_FSQL_GZ fsql = super.createFSQL(djdy.getDJDYID());
					ql.setFSQLID(fsql.getId());
					fsql.setQLID(ql.getId());
					// 如果是使用权宗地，要填写使用权面积,或者其他的
					if (djdy.getBDCDYLX().equals(BDCDYLX.SHYQZD.Value)) {
						BDCS_SHYQZD_GZ shyqzd = (BDCS_SHYQZD_GZ) dy;
						if (shyqzd != null) {
							fsql.setSYQMJ(shyqzd.getZDMJ());
						}
					}
					//继承变更前单元的抵押权信息
					if(djdys_bgq.size() > 1&& ids.length == 1){
						StringBuilder builer = new StringBuilder();
						builer.append("SELECT DYQL.QLID AS QLID FROM BDCK.BDCS_QL_XZ DYQL ");
						builer.append("LEFT JOIN BDCK.BDCS_DJDY_XZ DJDY ON DYQL.DJDYID=DJDY.DJDYID ");
						builer.append("AND DJDY.BDCDYID='");
						builer.append(djdys_bgq.get(0).getBDCDYID());
						builer.append("' ");
						builer.append("WHERE DYQL.QLLX='23' AND DJDY.BDCDYID IS NOT NULL");
						List<Map> listDYQ = dao.getDataListByFullSql(builer.toString());
						if (listDYQ != null && listDYQ.size() > 0) {
							CopyQLXXFromYDY(StringHelper.formatObject(listDYQ.get(0).get("QLID")), djdy, "1");
						}
					}else{
						for(BDCS_DJDY_GZ djdy_bgq : djdys_bgq){
							StringBuilder builer = new StringBuilder();
							builer.append("SELECT DYQL.QLID AS QLID FROM BDCK.BDCS_QL_XZ DYQL ");
							builer.append("LEFT JOIN BDCK.BDCS_DJDY_XZ DJDY ON DYQL.DJDYID=DJDY.DJDYID ");
							builer.append("AND DJDY.BDCDYID='");
							builer.append(djdy_bgq.getBDCDYID());
							builer.append("' ");
							builer.append("WHERE DYQL.QLLX='23' AND DJDY.BDCDYID IS NOT NULL");
							List<Map> listDYQ = dao.getDataListByFullSql(builer.toString());
							if (listDYQ != null && listDYQ.size() > 0) {
								for (int dyqqlid = 0; dyqqlid < listDYQ.size(); dyqqlid++) {
									if(djdys_bgq.size() == 1){
										CopyQLXXFromYDY(StringHelper.formatObject(listDYQ.get(0).get("QLID")), djdy, "1");
									}else {
										CopyQLXXFromYDY(StringHelper.formatObject(listDYQ.get(dyqqlid).get("QLID")), djdy, "1");
									}
								}
							}
//						else {
//							super.setErrMessage("单元未抵押！");
//							return false;
//						}
						}
					} 
					dao.save(djdy);
					dao.save(ql);
					dao.save(fsql);
				}
			}
			dao.save(dy);
			dao.flush();
		}
		return true;
	}

	/**
	 * 写入登记簿
	 */
	@Override
	public boolean writeDJB() {

		if (super.isCForCFING()) {
			return false;
		}
		String strSqlXMBH = ProjectHelper.GetXMBHCondition(getXMBH());
		List<BDCS_DJDY_GZ> djdys = getCommonDao().getDataList(BDCS_DJDY_GZ.class, strSqlXMBH +" ORDER BY LY");
		List<String> listBGQ = new ArrayList<String>();
		List<String> listBGH = new ArrayList<String>();
		if (djdys != null && djdys.size() > 0) {
			for (int idjdy = 0; idjdy < djdys.size(); idjdy++) {
				BDCS_DJDY_GZ bdcs_djdy_gz = djdys.get(idjdy);
				if (bdcs_djdy_gz != null) {
					String djdyid = bdcs_djdy_gz.getDJDYID();
					String bdcdyid = bdcs_djdy_gz.getBDCDYID();
					StringBuilder builder = new StringBuilder();
					builder.append(djdyid).append(";").append(bdcdyid);
					String strDYID = builder.toString();
					String ly = bdcs_djdy_gz.getLY();
					BDCDYLX lx = BDCDYLX.initFrom(bdcs_djdy_gz.getBDCDYLX());
					if(BDCDYLX.H.equals(lx) && DJDYLY.GZ.Value.equals(ly)){
						updateZRZ();
						updateZD();
					}
					if (DJDYLY.GZ.Value.equals(ly)) {
						if (!listBGH.contains(strDYID)) {
							listBGH.add(strDYID);
						}
						super.CopyGZQLToXZAndLS(djdyid);
						super.CopyGZQLRToXZAndLSNotOnQLLX(djdyid);
						super.CopyGZQDZRToXZAndLS(djdyid);
						super.CopyGZZSToXZAndLS(djdyid);
						super.CopyGZDYToXZAndLS(bdcs_djdy_gz.getBDCDYID());
						RealUnit dy = UnitTools.loadUnit(this.getBdcdylx(), DJDYLY.DC, bdcs_djdy_gz.getBDCDYID());
						if(!StringHelper.isEmpty(dy)){
							dy.setDJZT(DJZT.YDJ.Value);
							getCommonDao().save(dy);
						}
						super.CopyGZDJDYToXZAndLS(bdcs_djdy_gz.getId());
						super.CopyGeo(bdcdyid, lx, DJDYLY.initFrom(ly));
						//获取变更后户信息
					    RealUnit unit =UnitTools.loadUnit(BDCDYLX.H, DJDYLY.GZ, bdcs_djdy_gz.getBDCDYID());
					    if(!StringHelper.isEmpty(unit)){
					    	House h=(House) unit;
					    	//获取变更后户对应的宗地信息
					    	List<RealUnit> zd=UnitTools.loadUnits(BDCDYLX.SHYQZD, DJDYLY.GZ, strSqlXMBH+ " and bdcdyid ='"+h.getZDBDCDYID()+"'");
					    	//若存在，则宗地信息修改，则需要修改宗地信息及对应的权利信息
					    	if(!StringHelper.isEmpty(zd) && zd.size()>0){
					    		updateZD(bdcdyid, djdyid);
					    	}
					    }
					} else if (DJDYLY.XZ.Value.equals(ly)) {
						if (!listBGQ.contains(strDYID)) {
							listBGQ.add(strDYID);
						}
						ZXYDYDJ(djdyid);
						super.removeZSFromXZByALL(djdyid);
						super.removeQLRFromXZByALL(djdyid);
						super.removeQDZRFromXZByALL(djdyid);
						super.removeQLFromXZByALL(djdyid);
						UnitTools.deleteUnit(lx, DJDYLY.XZ, bdcdyid);
						super.removeDJDYFromXZ(djdyid);
						super.DeleteGeo(bdcdyid, lx, DJDYLY.initFrom(ly));
					}
				}
			}
		}
		if (listBGQ != null && listBGQ.size() > 0 && listBGH != null && listBGH.size() > 0) {
			for (int ibgq = 0; ibgq < listBGQ.size(); ibgq++) {
				for (int ibgh = 0; ibgh < listBGH.size(); ibgh++) {
					String BGQDYID = listBGQ.get(ibgq);//老单元的ID 
					String BGHDYID = listBGH.get(ibgh);//新增单元的ID-在单元变更中放到lbdcdyid中
					Date time = new Date();
					RebuildDYBG(BGQDYID.split(";")[1], BGQDYID.split(";")[0], BGHDYID.split(";")[1], BGHDYID.split(";")[0], time, null);
				}
			}
		}

		this.SetSFDB();
		getCommonDao().flush();
		return true;
	}
	
	@SuppressWarnings("unused")
	//zd转移
	public void updateZD(String bdcdyid_h,String djdyid_h)
	{   
		CommonDao dao=getCommonDao();
		RealUnit unit_h=UnitTools.loadUnit(BDCDYLX.H, DJDYLY.XZ, bdcdyid_h);
		if(unit_h==null){
			return;
		}
		House h=(House)unit_h;
		if(h==null){
			return;
		}
		String bdcdyid_zd=h.getZDBDCDYID();
		if(StringHelper.isEmpty(bdcdyid_zd)){
			return;
		}
		RealUnit unit_zd=UnitTools.loadUnit(BDCDYLX.SHYQZD, DJDYLY.XZ, bdcdyid_zd);
		if(unit_zd==null){
			return;
		}
		String djdyid_zd="";
		List<BDCS_DJDY_XZ> djdys=dao.getDataList(BDCS_DJDY_XZ.class, "BDCDYID='"+bdcdyid_zd+"'");
		if(djdys!=null&&djdys.size()>0){
			djdyid_zd=djdys.get(0).getDJDYID();
		}
		if(StringHelper.isEmpty(djdyid_zd)){
			return;
		}
		HashMap<String,String> m_djdyid=new HashMap<String, String>();
		HashMap<String,String> m_qlid=new HashMap<String, String>();
		HashMap<String,String> m_fsqlid=new HashMap<String, String>();
		HashMap<String,String> m_qlrid=new HashMap<String, String>();
		HashMap<String,String> m_zsid=new HashMap<String, String>();
		List<String> list_zsid_h=new ArrayList<String>();
		m_djdyid.put(djdyid_h, djdyid_zd);
		List<Rights> list_ql_zd_old=RightsTools.loadRightsByCondition(DJDYLY.XZ, "QLLX IN ('3','5','7') AND DJDYID='"+djdyid_zd+"'");
		Rights ql_zd_old=null;
		SubRights fsql_zd_old=null;
		if(list_ql_zd_old!=null&&list_ql_zd_old.size()>0){
			ql_zd_old=list_ql_zd_old.get(0);
			fsql_zd_old=RightsTools.loadSubRights(DJDYLY.XZ, ql_zd_old.getFSQLID());
			
		}
		List<Rights> list_ql_h_new=RightsTools.loadRightsByCondition(DJDYLY.GZ, "QLLX IN ('4','6','8') AND DJDYID='"+djdyid_h+"' AND XMBH='"+super.getXMBH()+"'");
		Rights ql_h_new=null;
		if(list_ql_h_new!=null&&list_ql_h_new.size()>0){
			ql_h_new=list_ql_h_new.get(0);
		}
		
		if(ql_zd_old==null){
			return;
		}
		if(ql_h_new==null){
			return;
		}
		String qlid_new=SuperHelper.GeneratePrimaryKey();
		String fsqlid_new=SuperHelper.GeneratePrimaryKey();
		BDCS_QL_XZ ql_zd_new=new BDCS_QL_XZ();
		try{
			PropertyUtils.copyProperties(ql_zd_new, ql_zd_old);
		}catch(Exception e){
			
		}
		ql_zd_new.setId(qlid_new);
		ql_zd_new.setFSQLID(fsqlid_new);
		if(list_ql_h_new!=null){
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
		BDCS_QL_LS ql_zd_new_ls=new BDCS_QL_LS();
		try{
			PropertyUtils.copyProperties(ql_zd_new_ls, ql_zd_new);
		}catch(Exception e){
			
		}
		dao.save(ql_zd_new_ls);
		m_qlid.put(ql_zd_old.getId(), qlid_new);
		if(fsql_zd_old!=null){
			BDCS_FSQL_XZ fsql_zd_new=new BDCS_FSQL_XZ();
			try{
				PropertyUtils.copyProperties(fsql_zd_new, fsql_zd_old);
			}catch(Exception e){
				
			}
			fsql_zd_new.setId(fsqlid_new);
			fsql_zd_new.setQLID(qlid_new);
			dao.save(fsql_zd_new);
			BDCS_FSQL_LS fsql_zd_new_ls=new BDCS_FSQL_LS();
			try{
				PropertyUtils.copyProperties(fsql_zd_new_ls, fsql_zd_new);
			}catch(Exception e){
				
			}
			dao.save(fsql_zd_new_ls);
			m_fsqlid.put(fsql_zd_old.getId(), fsqlid_new);
		}
		
		List<RightsHolder> list_qlr_h_new=RightsHolderTools.loadRightsHolders(DJDYLY.GZ, ql_h_new.getId());
		if(list_qlr_h_new!=null&&list_qlr_h_new.size()>0){
			for(RightsHolder qlr_h_new:list_qlr_h_new){
				String qlrid_zd_new=SuperHelper.GeneratePrimaryKey();
				BDCS_QLR_XZ qlr_zd_new=new BDCS_QLR_XZ();
				try{
					PropertyUtils.copyProperties(qlr_zd_new, qlr_h_new);
				}catch(Exception e){
					
				}
				qlr_zd_new.setId(qlrid_zd_new);
				qlr_zd_new.setQLID(qlid_new);
				dao.save(qlr_zd_new);
				BDCS_QLR_LS qlr_zd_new_ls=new BDCS_QLR_LS();
				try{
					PropertyUtils.copyProperties(qlr_zd_new_ls, qlr_zd_new);
				}catch(Exception e){
					
				}
				dao.save(qlr_zd_new_ls);
				m_qlrid.put(qlr_h_new.getId(), qlrid_zd_new);
				List<BDCS_QDZR_GZ> list_qdzr_h_new=dao.getDataList(BDCS_QDZR_GZ.class, "QLID='"+ql_h_new.getId()+"' AND XMBH='"+super.getXMBH()+"' AND QLRID='"+qlr_h_new.getId()+"'");
				if(list_qdzr_h_new!=null&&list_qdzr_h_new.size()>0){
					for(BDCS_QDZR_GZ qdzr_h_new:list_qdzr_h_new){
						String qdzrid_zd_new=SuperHelper.GeneratePrimaryKey();
						BDCS_QDZR_XZ qlr_qdzr_zd_new=new BDCS_QDZR_XZ();
						qlr_qdzr_zd_new.setId(qdzrid_zd_new);
						qlr_qdzr_zd_new.setXMBH(super.getXMBH());
						qlr_qdzr_zd_new.setDJDYID(djdyid_zd);
						qlr_qdzr_zd_new.setFSQLID(fsqlid_new);
						qlr_qdzr_zd_new.setQLID(qlid_new);
						qlr_qdzr_zd_new.setQLRID(qlrid_zd_new);
						if(m_zsid.containsKey(qdzr_h_new.getZSID())){
							qlr_qdzr_zd_new.setZSID(m_zsid.get(qdzr_h_new.getZSID()));
						}else{
							String zsid_zd_new=SuperHelper.GeneratePrimaryKey();
							qlr_qdzr_zd_new.setZSID(zsid_zd_new);
							m_zsid.put(qdzr_h_new.getZSID(), zsid_zd_new);
							list_zsid_h.add(qdzr_h_new.getZSID());
						}
						dao.save(qlr_qdzr_zd_new);
						BDCS_QDZR_LS qdzr_zd_new_ls=new BDCS_QDZR_LS();
						try{
							PropertyUtils.copyProperties(qdzr_zd_new_ls, qlr_qdzr_zd_new);
						}catch(Exception e){
							
						}
						dao.save(qdzr_zd_new_ls);
					}
				}
			}
		}
		
		if(list_zsid_h!=null&&list_zsid_h.size()>0){
			for(String zsid_h_new:list_zsid_h){
				BDCS_ZS_XZ zs_zd_new=new BDCS_ZS_XZ();
				zs_zd_new.setXMBH(super.getXMBH());
				zs_zd_new.setId(m_zsid.get(zsid_h_new));
				dao.save(zs_zd_new);
				BDCS_ZS_LS zs_zd_new_ls=new BDCS_ZS_LS();
				try{
					PropertyUtils.copyProperties(zs_zd_new_ls, zs_zd_new);
				}catch(Exception e){
					
				}
				dao.save(zs_zd_new_ls);
			}
		}
	}

	
	public void updateZD()
	{   
		CommonDao dao=getCommonDao();
		String strSqlXMBH = ProjectHelper.GetXMBHCondition(getXMBH());
		//获取变更后的信息
		List<BDCS_DJDY_GZ> djdys=dao.getDataList(BDCS_DJDY_GZ.class, strSqlXMBH +" and LY ='"+DJDYLY.GZ.Value+"'");
		if(!StringHelper.isEmpty(djdys) && djdys.size()>0)
		{
			BDCS_DJDY_GZ bdcs_djdy_gz=djdys.get(0);
			//获取变更后户信息
		    RealUnit unit =UnitTools.loadUnit(BDCDYLX.H, DJDYLY.GZ, bdcs_djdy_gz.getBDCDYID());
		    if(!StringHelper.isEmpty(unit))
		    {
		    	House h=(House) unit;
		    	//获取变更后户对应的宗地信息
		    	List<RealUnit> zd=UnitTools.loadUnits(BDCDYLX.SHYQZD, DJDYLY.GZ, strSqlXMBH+ " and bdcdyid ='"+h.getZDBDCDYID()+"'");
		    	//若存在，则宗地信息修改，则需要修改宗地信息及对应的权利信息
		    	if(!StringHelper.isEmpty(zd) && zd.size()>0)
		    	{
		    		//获取变更前单元信息
		    		List<BDCS_DJDY_GZ> djdys_h=dao.getDataList(BDCS_DJDY_GZ.class, strSqlXMBH +" and LY ='"+DJDYLY.XZ.Value+"'");
		    		if(!StringHelper.isEmpty(djdys_h) && djdys_h.size()>0)
		    		{
		    			BDCS_DJDY_GZ djdy=djdys_h.get(0);
		    			//获取变更前户信息
		    			RealUnit unit_xz =UnitTools.loadUnit(BDCDYLX.H, DJDYLY.XZ, djdy.getBDCDYID());
		    		    if(!StringHelper.isEmpty(unit_xz))
		    		    {
		    		    	 House h_xz=(House) unit_xz;
		    		    	 //获取变更前户对应的宗地单元信息
		    		    	 List<BDCS_DJDY_XZ> djdys_xz=dao.getDataList(BDCS_DJDY_XZ.class, "bdcdyid ='" +h_xz.getZDBDCDYID() +"'");
		    		    	 if(!StringHelper.isEmpty(djdys_xz) && djdys_xz.size()>0)
		    		    	 {
		    		    		 BDCS_DJDY_XZ bdcs_djdy_xz=djdys_xz.get(0);
		    		    		 //拷贝单元、登记单元、权利，附属权利，权利人，权地证人、证书(XZ及历史，删除原有xz信息)
		    		    		 
		    		    		 //拷贝单元信息到现状跟历史
		    		    		RealUnit zd_xz= UnitTools.copyUnit(zd.get(0), BDCDYLX.SHYQZD, DJDYLY.XZ);
		    		    		RealUnit zd_ls= UnitTools.copyUnit(zd.get(0), BDCDYLX.SHYQZD, DJDYLY.LS);
		    		    		RealUnit zd_dc=UnitTools.loadUnit(BDCDYLX.SHYQZD, DJDYLY.DC, zd.get(0).getId());
		    		    		zd_dc.setDJZT(DJZT.YDJ.Value);
		    		    		dao.update(zd_dc);
		    		    		dao.save(zd_xz);
		    		    		dao.save(zd_ls);
		    		    		//构建变更关系
		    		    		RebuildDYBG(h_xz.getZDBDCDYID(),bdcs_djdy_xz.getDJDYID(),zd.get(0).getId(),bdcs_djdy_xz.getDJDYID(),new Date(),null);
		    		    		// 拷贝宗地对应的图形信息到现状跟历史
								super.CopyGeo(zd.get(0).getId(), BDCDYLX.SHYQZD,DJDYLY.GZ);
								//删除原有的宗地现状单元信息
								dao.delete(BDCS_SHYQZD_XZ.class, h_xz.getZDBDCDYID());
								//删除原有的宗地现状对应的图形信息
								super.DeleteGeo(h_xz.getZDBDCDYID(), BDCDYLX.SHYQZD, DJDYLY.XZ);
								
		    		    		String djdybsm=SuperHelper.GeneratePrimaryKey();
		    		    		//拷贝登记单元信息
		    		    		BDCS_DJDY_LS djdy_ls_zd=ObjectHelper.copyDJDY_XZToLS(bdcs_djdy_xz);
		    		    		djdy_ls_zd.setId(djdybsm);
		    		    		djdy_ls_zd.setDJDYID(zd.get(0).getId());
		    		    		djdy_ls_zd.setBDCDYH(zd.get(0).getBDCDYH());
		    		    		djdy_ls_zd.setBDCDYID(zd.get(0).getId());
		    		    		dao.save(djdy_ls_zd);
		    		    		BDCS_DJDY_XZ djdy_xz_zd=new BDCS_DJDY_XZ();
		    		    		try {
									PropertyUtils.copyProperties(djdy_xz_zd, djdy_ls_zd);
								} catch (IllegalAccessException e) {
									e.printStackTrace();
								} catch (InvocationTargetException e) {
									e.printStackTrace();
								} catch (NoSuchMethodException e) {
									e.printStackTrace();
								}
		    		    		dao.save(djdy_xz_zd);
		    		    		List<BDCS_QL_XZ> lst=dao.getDataList(BDCS_QL_XZ.class, "DJDYID='"+bdcs_djdy_xz.getDJDYID()+"'");
		    		    	  if(!StringHelper.isEmpty(lst) && lst.size()>0)
		    		    	  {
		    		    		  for(BDCS_QL_XZ bdcs_ql_xz:lst)
		    		    		  {
		    		    		  String qlid=SuperHelper.GeneratePrimaryKey();
		    		    		  String fsqlid=SuperHelper.GeneratePrimaryKey();
		    		    		  BDCS_QL_LS bdcs_ql_ls=ObjectHelper.copyQL_XZToLS(bdcs_ql_xz);
		    		    		  bdcs_ql_ls.setBDCDYH(zd.get(0).getBDCDYH());
		    		    		  bdcs_ql_ls.setId(qlid);
		    		    		  bdcs_ql_ls.setDJDYID(zd.get(0).getId());
		    		    		  bdcs_ql_ls.setFSQLID(fsqlid);
		    		    		  dao.save(bdcs_ql_ls);
		    		    		 BDCS_FSQL_XZ bdcs_fsql_xz=dao.get(BDCS_FSQL_XZ.class,bdcs_ql_xz.getFSQLID());
		    		    		 if(!StringHelper.isEmpty(bdcs_fsql_xz))
		    		    		 {
		    		    			 BDCS_FSQL_LS bdcs_fsql_ls=ObjectHelper.copyFSQL_XZToLS(bdcs_fsql_xz);
		    		    			 bdcs_fsql_ls.setId(fsqlid);
		    		    			 bdcs_fsql_ls.setBDCDYH(zd.get(0).getBDCDYH());
		    		    			 bdcs_fsql_ls.setDJDYID(zd.get(0).getId());
		    		    			 bdcs_fsql_ls.setQLID(qlid);
		    		    			 dao.save(bdcs_fsql_ls);
		    		    			 //更新历史注销登簿信息
		    		    			 BDCS_FSQL_LS fsql=dao.get(BDCS_FSQL_LS.class, bdcs_ql_xz.getFSQLID());
		    		    			 fsql.setZXDBR(Global.getCurrentUserName());
		    		    			 fsql.setZXSJ(new Date());
		    		    			 fsql.setZXYYYY(getProject_id());
		    		    			 dao.update(fsql);
		    		    		 }
		    		    		 List<BDCS_QLR_XZ> qlrs=dao.getDataList(BDCS_QLR_XZ.class, "QLID ='"+bdcs_ql_xz.getId()+"'");
		    		    		 if(!StringHelper.isEmpty(qlrs) && qlrs.size()>0)
		    		    		 {
		    		    			 for(BDCS_QLR_XZ bdcs_qlr_xz:qlrs)
		    		    			 {
			    		    			 List<String> lstzsid=new ArrayList<String>();
		    		    				 String qlrid=SuperHelper.GeneratePrimaryKey();
		    		    			     BDCS_QLR_LS bdcs_qlr_ls=ObjectHelper.copyQLR_XZToLS(bdcs_qlr_xz);
		    		    			     bdcs_qlr_ls.setId(qlrid);
	    		    				     bdcs_qlr_ls.setQLID(qlid);
	    		    				     dao.save(bdcs_qlr_ls);
	    		    				     List<BDCS_QDZR_XZ> qdzrs=dao.getDataList(BDCS_QDZR_XZ.class," QLRID ='"+bdcs_qlr_xz.getId()+"'");
	    		    				     if(!StringHelper.isEmpty(qdzrs) && qdzrs.size()>0)
	    		    				     {
	    		    				    	 for(BDCS_QDZR_XZ bdcs_qdzr_xz:qdzrs)
	    		    				    	 {
	    		    				    		 if(!lstzsid.contains(bdcs_qdzr_xz.getZSID()))
	    		    				    		 {
	    		    				    			 lstzsid.add(bdcs_qdzr_xz.getZSID());
	    		    				    			 BDCS_ZS_XZ bdcs_zs_xz =dao.get(BDCS_ZS_XZ.class, bdcs_qdzr_xz.getZSID());
	    		    				    			 String zsid=SuperHelper.GeneratePrimaryKey();
	    		    				    			 if(!StringHelper.isEmpty(bdcs_zs_xz))
	    		    				    			 {
	    		    				    				 BDCS_ZS_LS bdcs_zs_ls= ObjectHelper.copyZS_XZToLS(bdcs_zs_xz);
	    		    				    				 bdcs_zs_ls.setId(zsid);
	    		    				    				 dao.save(bdcs_zs_ls);
	    		    				    			 }
	    		    				    			 BDCS_QDZR_LS bdcs_qdzr_ls=ObjectHelper.copyQDZR_XZToLS(bdcs_qdzr_xz);
	    		    				    			 String qdzrid=SuperHelper.GeneratePrimaryKey();
	    		    				    			 bdcs_qdzr_ls.setBDCDYH(zd.get(0).getBDCDYH());
	    		    				    			 bdcs_qdzr_ls.setCreateTime(new Date());
	    		    				    			 bdcs_qdzr_ls.setZSID(zsid);
	    		    				    			 bdcs_qdzr_ls.setQLID(qlid);
	    		    				    			 bdcs_qdzr_ls.setFSQLID(fsqlid);
	    		    				    			 bdcs_qdzr_ls.setDJDYID(zd.get(0).getId());
	    		    				    			 bdcs_qdzr_ls.setQLRID(qlrid);
	    		    				    			 bdcs_qdzr_ls.setId(qdzrid);
	    		    				    			 dao.save(bdcs_qdzr_ls);
	    		    				    		 }
	    		    				    	 }
	    		    				     }
		    		    			 }
		    		    		 }
		    		    		 //从历史拷贝到现状层
		    		    		Rights ql_xz=  RightsTools.copyRightsAll(DJDYLY.LS, DJDYLY.XZ, qlid);
		    		    		if(!StringHelper.isEmpty(ql_xz)){
		    		    			dao.save(ql_xz);
		    		    		}
		    		    	      //删除原有的宗地的单元信息
		    		    	      dao.deleteEntity(bdcs_djdy_xz);
		    		    		 //根据权利ID删除权利，附属权利，证书，权地证人，权利人信息
		    		    		 RightsTools.deleteRightsAll(DJDYLY.XZ, bdcs_ql_xz.getId());
		    		    		}
		    		    	  }
		    		    	 }
		    		    }
		    		}
		    	}
		    }
		}
	}
	public void updateZRZ()
	{
		CommonDao dao=getCommonDao();
		String strSqlXMBH = ProjectHelper.GetXMBHCondition(getXMBH());
		//获取变更后的信息
		List<BDCS_DJDY_GZ> djdys=dao.getDataList(BDCS_DJDY_GZ.class, strSqlXMBH +" and LY ='"+DJDYLY.GZ.Value+"'");
		if(!StringHelper.isEmpty(djdys) && djdys.size()>0)
		{
			BDCS_DJDY_GZ bdcs_djdy_gz=djdys.get(0);
			//获取变更后户信息
		    RealUnit unit =UnitTools.loadUnit(BDCDYLX.H, DJDYLY.GZ, bdcs_djdy_gz.getBDCDYID());
		    if(!StringHelper.isEmpty(unit))
		    {
		    	House h=(House) unit;
		    	//获取变更后户对应的自然幢信息
		    	List<RealUnit> zrz=UnitTools.loadUnits(BDCDYLX.ZRZ, DJDYLY.GZ, strSqlXMBH+ " and bdcdyid ='"+h.getZRZBDCDYID()+"'");
		    	//若存在，则自然幢信息修改，则需要修改自然幢信息
		    	if(!StringHelper.isEmpty(zrz) && zrz.size()>0)
		    	{
		    		//获取变更前单元信息
		    		List<BDCS_DJDY_GZ> djdys_h=dao.getDataList(BDCS_DJDY_GZ.class, strSqlXMBH +" and LY ='"+DJDYLY.XZ.Value+"'");
		    		if(!StringHelper.isEmpty(djdys_h) && djdys_h.size()>0)
		    		{
		    			BDCS_DJDY_GZ djdy=djdys_h.get(0);
		    			//获取变更前户信息
		    			RealUnit unit_xz =UnitTools.loadUnit(BDCDYLX.H, DJDYLY.XZ, djdy.getBDCDYID());
		    		    if(!StringHelper.isEmpty(unit_xz))
		    		    {
		    		    	 House h_xz=(House) unit_xz;
		    		    	 //获取变更前户对应的宗地单元信息
		    		    	 List<BDCS_DJDY_XZ> djdys_xz=dao.getDataList(BDCS_DJDY_XZ.class, "bdcdyid ='" +h_xz.getZDBDCDYID() +"'");
		    		    	 if(!StringHelper.isEmpty(djdys_xz) && djdys_xz.size()>0)
		    		    	 {
		    		    		 BDCS_DJDY_XZ bdcs_djdy_xz=djdys_xz.get(0);
		    		    		 //拷贝单元、登记单元、权利，附属权利，权利人，权地证人、证书(XZ及历史，删除原有xz信息)
		    		    		 
		    		    		 //拷贝单元信息到现状跟历史
		    		    		RealUnit zrz_xz= UnitTools.copyUnit(zrz.get(0), BDCDYLX.ZRZ, DJDYLY.XZ);
		    		    		RealUnit zrz_ls= UnitTools.copyUnit(zrz.get(0), BDCDYLX.ZRZ, DJDYLY.LS);
		    		    		RealUnit zrz_dc=UnitTools.loadUnit(BDCDYLX.ZRZ, DJDYLY.DC, zrz.get(0).getId());
		    		    		zrz_dc.setDJZT(DJZT.YDJ.Value);
		    		    		dao.update(zrz_dc);
		    		    		dao.save(zrz_xz);
		    		    		dao.save(zrz_ls);
		    		    		//构建变更关系
		    		    		RebuildDYBG(h_xz.getZRZBDCDYID(),bdcs_djdy_xz.getBDCDYID(),zrz.get(0).getId(),bdcs_djdy_xz.getDJDYID(),new Date(),null);
		    		    		// 拷贝自然幢对应的图形信息到现状跟历史
								super.CopyGeo(zrz.get(0).getId(), BDCDYLX.ZRZ,DJDYLY.GZ);
								//删除变更前自然幢单元信息
								dao.delete(BDCS_ZRZ_XZ.class, h_xz.getZRZBDCDYID());
								//删除原有的宗地现状对应的图形信息
								super.DeleteGeo(h_xz.getZRZBDCDYID(), BDCDYLX.ZRZ, DJDYLY.XZ);
		    		    	 }
		    		    	 }
		    		}
		    	}
		    }
		}
	}
	/**
	 * 移除不动产单元
	 */
	@Override
	public boolean removeBDCDY(String bdcdyid) {
		CommonDao dao = this.getCommonDao();
		BDCS_DJDY_GZ djdy = super.removeDJDY(bdcdyid);
		if (djdy != null) {
			String bdcdylx = djdy.getBDCDYLX();
			String ly = djdy.getLY();
			String djdyid = djdy.getDJDYID();
			if (ly.equals(DJDYLY.GZ.Value)) {
				// 移除具体单元
				RealUnit unit=	UnitTools.deleteUnit(BDCDYLX.initFrom(djdy.getBDCDYLX()), DJDYLY.GZ, bdcdyid);
				if(!StringHelper.isEmpty(unit))
				{
					if(unit instanceof House)
					{
						//判断是否移除自然幢跟地的信息,先要判断调查户的信息全部移除掉
							String zrzbdcdyid=((House) unit).getZRZBDCDYID();
						    List<RealUnit> zrzs=UnitTools.loadUnits(BDCDYLX.ZRZ, DJDYLY.GZ,ProjectHelper.GetXMBHCondition(this.getXMBH()) +" AND BDCDYID ='"+zrzbdcdyid+"'");
						    if(!StringHelper.isEmpty(zrzs) && zrzs.size()>0)
						    {
						      UnitTools.deleteUnit(BDCDYLX.ZRZ, DJDYLY.GZ,  zrzbdcdyid);
						      updateDCDYStatus(BDCDYLX.ZRZ.Value, zrzbdcdyid);
						  	  super.DeleteGeo(zrzbdcdyid, BDCDYLX.ZRZ, DJDYLY.GZ);
						    }
							String zdbdcdyid=((House) unit).getZDBDCDYID();
							  List<RealUnit> zdzs=UnitTools.loadUnits(BDCDYLX.SHYQZD, DJDYLY.GZ, ProjectHelper.GetXMBHCondition(this.getXMBH()) +" AND BDCDYID ='"+zdbdcdyid+"'");
							    if(!StringHelper.isEmpty(zdzs) && zdzs.size()>0)
							    {
							      UnitTools.deleteUnit(BDCDYLX.SHYQZD, DJDYLY.GZ,  zdbdcdyid);
							  	  super.DeleteGeo(zdbdcdyid, BDCDYLX.SHYQZD, DJDYLY.GZ);
							      updateDCDYStatus(BDCDYLX.SHYQZD.Value, zdbdcdyid);
							    }
					}
				}
				
				super.RemoveSQRByQLID(djdyid, getXMBH());
				// 删除权利、附属权利、权利人、证书、权地证人关系
				String _hqlCondition = MessageFormat.format("XMBH=''{0}'' AND DJDYID=''{1}''", getXMBH(), djdyid);
				RightsTools.deleteRightsAllByCondition(DJDYLY.initFrom(ly), _hqlCondition);

				// 更新调查库相应单元状态
				updateDCDYStatus(bdcdylx, bdcdyid);
			} else {
				List<Rights> qls = RightsTools.loadRightsByCondition(DJDYLY.XZ, "DJDYID='" + djdy.getDJDYID() + "' AND QLLX='" + getQllx().Value + "'");
				if (qls != null && qls.size() > 0) {
					List<BDCS_SQR> sqrs = dao.getDataList(BDCS_SQR.class, "XMBH='" + getXMBH() + "' AND GLQLID='" + qls.get(0).getId() + "'");
					if (sqrs != null && sqrs.size() > 0) {
						for (int isqr = 0; isqr < sqrs.size(); isqr++) {
							dao.deleteEntity(sqrs.get(isqr));
						}
					}
				}
			}
		}
		dao.flush();
		return false;
	}

	/**
	 * 变更登记在登簿后，通过附属权利中的注销业务号及对应的登记类型获取权利信息
	 */
	@Override
	public List<UnitTree> getUnitTree() {
		String xmbhFilter = ProjectHelper.GetXMBHCondition(getXMBH());
		CommonDao dao = this.getCommonDao();
		List<UnitTree> list = new ArrayList<UnitTree>();
		List<BDCS_DJDY_GZ> djdys = dao.getDataList(BDCS_DJDY_GZ.class, xmbhFilter);
		if (djdys != null && djdys.size() > 0) {
			for (int i = 0; i < djdys.size(); i++) {
				BDCS_DJDY_GZ djdy = djdys.get(i);
				UnitTree tree = new UnitTree();
				StringBuilder BuilderQL = new StringBuilder();
				BuilderQL.append(xmbhFilter).append(" AND DJDYID='").append(djdy.getDJDYID()).append("'");
				List<BDCS_QL_GZ> qls = dao.getDataList(BDCS_QL_GZ.class, BuilderQL.toString());
				if (qls != null) {
					for (int iql = 0; iql < qls.size(); iql++) {
						BDCS_QL_GZ ql = qls.get(iql);
						if (ql.getQLLX().equals(QLLX.DIYQ.Value)) {
							tree.setDIYQQlid(ql.getId());
							tree.setOlddiyqqlid(ql.getLYQLID());

						} else {
							tree.setQlid(ql.getId());
						}
					}
				}
				tree.setId(djdy.getBDCDYID());
				tree.setBdcdyid(djdy.getBDCDYID());
				tree.setBdcdylx(djdy.getBDCDYLX());
				tree.setDjdyid(djdy.getDJDYID());
				String ly = StringUtils.isEmpty(djdy.getLY()) ? "gz"
						: DJDYLY.initFrom(djdy.getLY()) == null ? "gz" : DJDYLY.initFrom(djdy.getLY()).Name;
				tree.setLy(ly);		
				String zl = getZL(tree, djdy.getDJDYID(), djdy.getBDCDYLX(), djdy.getBDCDYID(), ly);
				tree.setText(zl);
				// 如果是户的话，把房号也加上
				if (djdy.getBDCDYLX().equals(BDCDYLX.H.Value)) {
					if (!StringHelper.isEmpty(djdy.getLY())) {
						DJDYLY ely = DJDYLY.initFrom(djdy.getLY());
						House house = (House) (UnitTools.loadUnit(BDCDYLX.H, ely, djdy.getBDCDYID()));
						if (house != null) {
							String fh = house.getFH();
							tree.setFh(fh);
							tree.setText("坐落:" + zl + "|" + "房号:" + (fh == null ? "" : fh));
						}
					}
				}
				tree.setZl(zl);
				 //判断自然幢跟宗地信息来源
				if(BDCDYLX.H.equals(this.getBdcdylx()))
				{
					String zdbdcdyid=tree.getZdbdcdyid();
					List<RealUnit> zds=UnitTools.loadUnits(BDCDYLX.SHYQZD, DJDYLY.GZ, "BDCDYID ='" +zdbdcdyid +"' and "+ProjectHelper
							.GetXMBHCondition(this.getXMBH()));
					if(!StringHelper.isEmpty(zds) && zds.size()>0)
					{
						tree.setZdly("gz");
					}
					else
					{
						tree.setZdly("ls");
					}
					String zrzbdcdyid=tree.getZrzbdcdyid();
					List<RealUnit> zrzs=UnitTools.loadUnits(BDCDYLX.ZRZ, DJDYLY.GZ, "BDCDYID ='" +zrzbdcdyid +"' and "+ProjectHelper
							.GetXMBHCondition(this.getXMBH()));
					if(!StringHelper.isEmpty(zrzs) && zrzs.size()>0)
					{
						tree.setZrzly("gz");
					}
					else
					{
						tree.setZrzly("ls");
					}
				}		
			
				list.add(tree);
			}
		}
		return list;
	}

	/**
	 * 根据申请人ID数组生成权利人
	 */
	@Override
	public void addQLRbySQRArray(String qlid, Object[] sqrids) {
		super.addQLRbySQRs(qlid, sqrids);
		CommonDao dao = getCommonDao();
		BDCS_QL_GZ ql = dao.get(BDCS_QL_GZ.class, qlid);
		if (!ql.getQLLX().equals(QLLX.DIYQ.Value)) {
			String xmbhFilter = ProjectHelper.GetXMBHCondition(getXMBH());
			StringBuilder BuilderQLR = new StringBuilder();
			BuilderQLR.append(xmbhFilter).append(" AND QLID='").append(qlid).append("' ORDER BY SXH");
			List<BDCS_QLR_GZ> qlrs = dao.getDataList(BDCS_QLR_GZ.class, BuilderQLR.toString());
			if (qlrs != null && qlrs.size() > 0) {
				StringBuilder builderDYR = new StringBuilder();
				int indexdyr = 0;
				for (int iqlr = 0; iqlr < qlrs.size(); iqlr++) {
					if (indexdyr == 0) {
						builderDYR.append(qlrs.get(iqlr).getQLRMC());
					} else {
						builderDYR.append(",").append(qlrs.get(iqlr).getQLRMC());
					}
					indexdyr++;
				}
				StringBuilder BuilderFSQL = new StringBuilder();
				BuilderFSQL.append(xmbhFilter).append(" AND DJDYID='").append(ql.getDJDYID()).append("'");
				List<BDCS_FSQL_GZ> fsqls = dao.getDataList(BDCS_FSQL_GZ.class, BuilderFSQL.toString());
				if (fsqls != null && fsqls.size() > 0) {
					for (int ifsql = 0; ifsql < fsqls.size(); ifsql++) {
						BDCS_FSQL_GZ fsql = fsqls.get(ifsql);
						if (!fsql.getQLID().equals(qlid)) {
							fsql.setDYR(builderDYR.toString());
							dao.update(fsql);
						}
					}
				}
			}
			dao.flush();
		}
	}

	/**
	 * 移除权利人
	 */
	@Override
	public void removeQLR(String qlid, String qlrid) {
		CommonDao dao = getCommonDao();
		BDCS_QL_GZ ql = dao.get(BDCS_QL_GZ.class, qlid);
		super.removeqlr(qlrid, qlid);
		// 更新抵押人
		if (!ql.getQLLX().equals(QLLX.DIYQ.Value)) {
			List<BDCS_QLR_GZ> qlrs = dao.getDataList(BDCS_QLR_GZ.class, "QLID ='" + qlid + "' ORDER BY SXH");
			StringBuilder builderDYR = new StringBuilder();
			int indexdyr = 0;
			for (int iqlr = 0; iqlr < qlrs.size(); iqlr++) {
				if (qlrid.equals(qlrs.get(iqlr).getId())) {
					continue;
				}
				if (indexdyr == 0) {
					builderDYR.append(qlrs.get(iqlr).getQLRMC());
				} else {
					builderDYR.append(",").append(qlrs.get(iqlr).getQLRMC());
				}
				indexdyr++;
			}
			List<BDCS_FSQL_GZ> fsqlList = dao.getDataList(BDCS_FSQL_GZ.class,
					" XMBH='" + getXMBH() + "' and DJDYID='" + ql.getDJDYID() + "'");
			if (fsqlList != null && fsqlList.size() > 0) {
				for (int ifsql = 0; ifsql < fsqlList.size(); ifsql++) {
					BDCS_FSQL_GZ fsql = fsqlList.get(ifsql);
					if (!fsql.getQLID().equals(qlid)) {
						fsql.setDYR(builderDYR.toString());
						dao.update(fsql);
					}
				}
			}
		}
		dao.flush();
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
	 * 更新调查库单元的登记状态
	 * @Title: updateDCDYStatus
	 * @author:liushufeng
	 * @date：2015年10月27日 下午3:24:44
	 * @param bdcdylx
	 * @param bdcdyid
	 */
	private void updateDCDYStatus(String bdcdylx, String bdcdyid) {
		BDCDYLX dylx = BDCDYLX.initFrom(bdcdylx);
		RealUnit unit = UnitTools.loadUnit(dylx, DJDYLY.DC, bdcdyid);
		if (unit != null) {
			unit.setDJZT(DJZT.WDJ.Value);
			getCommonDao().update(unit);
		}
	}
/**
	 * 导出交换文件
	 */
	@Override
	public Map<String, String> exportXML(String path, String actinstID) {
		Marshaller mashaller;
		Map<String, String> names = new HashMap<String, String>();
		try {
			CommonDao dao = super.getCommonDao();
			String xmbhHql = ProjectHelper.GetXMBHCondition(super.getXMBH());
			List<BDCS_DJDY_GZ> djdys = dao.getDataList(BDCS_DJDY_GZ.class, xmbhHql + " and LY = '" + ConstValue.DJDYLY.GZ.Value + "' ");
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
					if (QLLX.GYJSYDSHYQ_FWSYQ.Value.equals(this.getQllx().Value)) { // 房屋所有权
						try {
							BDCS_H_GZ h = dao.get(BDCS_H_GZ.class, djdy.getBDCDYID());
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
				Rights bdcql = RightsTools.loadRightsByDJDYID(dyly, getXMBH(), djdy.getDJDYID());
				SubRights bdcfsql = RightsTools.loadSubRightsByRightsID(dyly, bdcql.getId());
				List<RightsHolder> bdcqlrs = RightsHolderTools.loadRightsHolders(dyly, djdy.getDJDYID(), getXMBH());
				MessageExport msg = super.getShareMsgTools().GetMsg(bdcdy, bdcql, bdcfsql, bdcqlrs, bljc, xmxx);
				super.getShareMsgTools().SendMsg(msg, idjdy + 1, djdy.getBDCDYLX(), xmxx);
			}
		}
	}
	
	protected BDCS_SQR CopySQRFromDYQR(BDCS_QLR_XZ qlr, String glqlid) {
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
		BDCS_SQR sqr = new BDCS_SQR();
		if (!bexists) {
			String sqrid = getPrimaryKey();
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
		}
		return sqr;
	}
	
	
	protected void CopyQLXXFromYDY(String qlid, BDCS_DJDY_GZ djdy, String sfnewcqr) {
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
			bdcs_ql_gz.setDBR("");// 重置登簿人
			String newqzh = "";
			String workflowcode = ProjectHelper.getWorkflowCodeByProjectID(this
					.getProject_id());
			List<WFD_MAPPING> listCode = getCommonDao().getDataList(
					WFD_MAPPING.class, "WORKFLOWCODE='" + workflowcode + "'");
			if (listCode != null && listCode.size() > 0) {
				newqzh = listCode.get(0).getNEWQZH();
			}
			if (SF.YES.Value.equals(newqzh)) {
				bdcs_ql_gz.setBDCQZH("");// 重置不动产权证明号
				bdcs_ql_gz.setZSBH("");// 重置证书编号
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
									bdcs_zs_gz.setZSBH("");// 重置证书编号
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
	
	private String getZL(UnitTree tree, String djdyid, String bdcdylx, String bdcdyid, String djdyly) {
		String zl = "";
		CommonDao dao = getCommonDao();
		BDCDYLX dylx = BDCDYLX.initFrom(bdcdylx);
		if (djdyly.equals(DJDYLY.GZ.Name)) {
			if (dylx.equals(BDCDYLX.SHYQZD)) {
				BDCS_SHYQZD_GZ shyqzd = dao.get(BDCS_SHYQZD_GZ.class, bdcdyid);
				zl = shyqzd == null ? "" : shyqzd.getZL();
			} else if (dylx.equals(BDCDYLX.H)) {
				BDCS_H_GZ h = dao.get(BDCS_H_GZ.class, bdcdyid);
				tree.setCid(h.getCID());
				tree.setZdbdcdyid(h.getZDBDCDYID());
				tree.setZrzbdcdyid(h.getZRZBDCDYID());
				tree.setLjzbdcdyid(h.getLJZID());
				zl = h == null ? "" : h.getZL();
			} else if (dylx.equals(BDCDYLX.ZRZ)) {
				BDCS_ZRZ_GZ zrz = dao.get(BDCS_ZRZ_GZ.class, bdcdyid);
				tree.setZdbdcdyid(zrz.getZDBDCDYID());
				zl = zrz == null ? "" : zrz.getZL();
			} else if (dylx.equals(BDCDYLX.SYQZD))// sunhb-2015-06-23添加所有权宗地，获取坐落
			{
				BDCS_SYQZD_GZ syqzd = dao.get(BDCS_SYQZD_GZ.class, bdcdyid);
				zl = syqzd == null ? "" : syqzd.getZL();
			} else if (dylx.equals(BDCDYLX.HY))// sunhb-2015-06-23添加宗海，获取坐落
			{
				BDCS_ZH_GZ zh = dao.get(BDCS_ZH_GZ.class, bdcdyid);
				zl = zh == null ? "" : zh.getZL();
			} else if (dylx.equals(BDCDYLX.LD))// sunhb-2015-06-23添加林地，获取坐落
			{
				BDCS_SLLM_GZ ld = dao.get(BDCS_SLLM_GZ.class, bdcdyid);
				tree.setZdbdcdyid(ld.getZDBDCDYID());
				zl = ld == null ? "" : ld.getZL();
			} else {
				// TODO 刘树峰:获取其他类型不动产单元的坐落
			}
		} else {// 来源于现状，把原来的所有权/使用权的权利ID也加上
			if (dylx.equals(BDCDYLX.SHYQZD)) {
				BDCS_SHYQZD_LS shyqzd = dao.get(BDCS_SHYQZD_LS.class, bdcdyid);
				zl = shyqzd == null ? "" : shyqzd.getZL();
			} else if (dylx.equals(BDCDYLX.H)) {
				BDCS_H_LS shyqzd = dao.get(BDCS_H_LS.class, bdcdyid);
				if (shyqzd != null) {
					zl = shyqzd == null ? "" : shyqzd.getZL();

					tree.setCid(shyqzd.getCID());
					tree.setZdbdcdyid(shyqzd.getZDBDCDYID());
					tree.setZrzbdcdyid(shyqzd.getZRZBDCDYID());
					tree.setLjzbdcdyid(shyqzd.getLJZID());
				}

			} else if (dylx.equals(BDCDYLX.YCH)) {
				BDCS_H_LSY bdcs_h_xzy = dao.get(BDCS_H_LSY.class, bdcdyid);
				if (bdcs_h_xzy != null) {
					zl = bdcs_h_xzy.getZL();
					tree.setId(bdcs_h_xzy.getCID());
					tree.setZdbdcdyid(bdcs_h_xzy.getZDBDCDYID());
					tree.setZrzbdcdyid(bdcs_h_xzy.getZRZBDCDYID());
					tree.setLjzbdcdyid(bdcs_h_xzy.getLJZID());
				}
			} else if (dylx.equals(BDCDYLX.ZRZ)) {
				BDCS_ZRZ_LS zrz = dao.get(BDCS_ZRZ_LS.class, bdcdyid);
				tree.setZdbdcdyid(zrz.getZDBDCDYID());
				zl = zrz == null ? "" : zrz.getZL();
			} else {
				// TODO 刘树峰:获取其他类型不动产单元的坐落
			}
			// 这块的逻辑有点问题，原来的权利ID应该包含两种，一种是所有权/使用权ID，一种是他项权利ID，例如
			// 抵押权的转移，就包含了被抵押单元的所有权权利和转移前的抵押权
			String qllxarray = " ('1','2','3','4','5','6','7','8','9','10','11','12','13','14','15','16','17','18')";
			String hqlCondition = MessageFormat.format(
					" DJDYID IN (SELECT DJDYID FROM BDCS_DJDY_LS WHERE BDCDYID=''{0}'') AND QLLX IN {1} ORDER BY DJSJ",
					bdcdyid, qllxarray);
			List<BDCS_QL_LS> listxzql = dao.getDataList(BDCS_QL_LS.class, hqlCondition);
			if (listxzql != null && listxzql.size() > 0) {
				BDCS_QL_LS ql = listxzql.get(0);
				tree.setOldqlid(ql.getId());
			}
		}
		return zl;
	}
	
	private void ZXYDYDJ(String djdyid) {
		String dbr = Global.getCurrentUserName();
		List<BDCS_QL_GZ> listql = getCommonDao().getDataList(BDCS_QL_GZ.class,
				"DJDYID='" + djdyid + "' AND QLLX='23' AND LYQLID IS NOT NULL");
		if (listql != null && listql.size() > 0) {
			for (int iql = 0; iql < listql.size(); iql++) {
				BDCS_QL_GZ ql = listql.get(iql);
				if (ql != null && !StringHelper.isEmpty(ql.getLYQLID())) {
					super.removeQLXXFromXZByQLID(ql.getLYQLID());
					BDCS_QL_LS dyq_ly = getCommonDao().get(BDCS_QL_LS.class, ql.getLYQLID());
					if (dyq_ly != null) {
						if (StringHelper.isEmpty(dyq_ly.getFSQLID())) {
							BDCS_FSQL_LS dyqfs_ly = getCommonDao().get(BDCS_FSQL_LS.class, dyq_ly.getFSQLID());
							if (dyqfs_ly != null) {
								dyqfs_ly.setZXDBR(dbr);
								dyqfs_ly.setZXSJ(new Date());
								dyqfs_ly.setZXFJ(ql.getFJ());
								dyqfs_ly.setZXDYYY(ql.getDJYY());
								dyqfs_ly.setZXDYYWH(ql.getYWH());
								getCommonDao().update(dyqfs_ly);
							}
						}
					}
				}
			}
		}
	}
	
}
