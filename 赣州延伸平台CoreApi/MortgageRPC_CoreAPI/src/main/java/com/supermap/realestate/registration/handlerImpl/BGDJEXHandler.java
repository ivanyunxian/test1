package com.supermap.realestate.registration.handlerImpl;

import com.supermap.realestate.registration.ViewClass.ProjectInfo;
import com.supermap.realestate.registration.ViewClass.UnitTree;
import com.supermap.realestate.registration.dataExchange.*;
import com.supermap.realestate.registration.dataExchange.fwsyq.*;
import com.supermap.realestate.registration.dataExchange.shyq.*;
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
import com.supermap.wisdombusiness.core.SuperHelper;
import com.supermap.wisdombusiness.core.SuperSpringContext;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.util.StringUtils;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.text.MessageFormat;
import java.util.*;

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
public class BGDJEXHandler extends DJHandlerBase implements DJHandler {

	/**
	 * 构造函数
	 * 
	 * @param info
	 */
	public BGDJEXHandler(ProjectInfo info) {
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
			if (dy != null) {
				BDCS_DJDY_GZ djdy = super.createDJDYfromXZ(id);
				if (djdy != null) {
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
									BDCS_SQR sqr = super.copyXZQLRtoSQR(qlrs.get(iqlr), SQRLB.JF);
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
			  RealUnit dy_dc =UnitTools.loadUnit(this.getBdcdylx(), DJDYLY.DC, id);
				if(!StringHelper.isEmpty(dy_dc))
				{
					dy_dc.setDJZT(DJZT.DJZ.Value);
					dao.save(dy_dc);
				    dy=UnitTools.copyUnit(dy_dc, this.getBdcdylx(), DJDYLY.GZ);
				   if(!StringHelper.isEmpty(dy))
				   {
					   dy.setXMBH(this.getXMBH());
				   }
					//先判断h对应的宗地跟自然幢在XZ层有没有，没有从BDCDCK拷贝，ZRZ跟ZD
					if(dy instanceof House && BDCDYLX.H.equals(this.getBdcdylx()))
					{
						House h=(House) dy;
						String zrzbdcdyid=h.getZRZBDCDYID();
						RealUnit zrz_xz=UnitTools.loadUnit(BDCDYLX.ZRZ, DJDYLY.XZ, zrzbdcdyid);
						//考虑到分割时，可能添加第一个单元时，没有自然幢信息跟宗地信息，再添加单元时，有自然幢信息跟宗地信息，就不添加了
						if(StringHelper.isEmpty(zrz_xz))
						{
							if(!zrzflag)
							{
								RealUnit zrz_dc=UnitTools.loadUnit(BDCDYLX.ZRZ, DJDYLY.DC, zrzbdcdyid);
								if(!StringHelper.isEmpty(zrz_dc))
								{
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
						if(StringHelper.isEmpty(zd_xz))
						{
							if(!zdflag)
							{
								RealUnit zd_dc=UnitTools.loadUnit(BDCDYLX.SHYQZD, DJDYLY.DC, zdbdcdyid);
								if(!StringHelper.isEmpty(zd_dc))
								{
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
					if(BDCDYLX.H.equals(lx) && DJDYLY.GZ.Value.equals(ly))
					{
						updateZRZ();
						updateZD();
					}
					if (DJDYLY.GZ.Value.equals(ly)) {
						if (!listBGH.contains(strDYID)) {
							listBGH.add(strDYID);
						}
						super.CopyGZQLToXZAndLS(djdyid);
						super.CopyGZQLRToXZAndLS(djdyid);
						super.CopyGZQDZRToXZAndLS(djdyid);
						super.CopyGZZSToXZAndLS(djdyid);
						super.CopyGZDYToXZAndLS(bdcs_djdy_gz.getBDCDYID());
						RealUnit dy = UnitTools.loadUnit(this.getBdcdylx(), DJDYLY.DC, bdcs_djdy_gz.getBDCDYID());
						if(!StringHelper.isEmpty(dy))
						{
							dy.setDJZT(DJZT.YDJ.Value);
							getCommonDao().save(dy);
						}
						super.CopyGZDJDYToXZAndLS(bdcs_djdy_gz.getId());
						super.CopyGeo_BG(bdcdyid, lx, DJDYLY.initFrom(ly));
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
					    		updateZD(bdcdyid, djdyid);
					    	}
					    }
					} else if (DJDYLY.XZ.Value.equals(ly)) {
						if (!listBGQ.contains(strDYID)) {
							listBGQ.add(strDYID);
						}
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
	public void updateZD(String bdcdyid_h,String djdyid_h){   
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
		if(!StringHelper.isEmpty(djdys) && djdys.size()>0){
			for (int i = 0; i < djdys.size(); i++) {
				BDCS_DJDY_GZ bdcs_djdy_gz = djdys.get(i);
				// 获取变更后户信息
				RealUnit unit = UnitTools.loadUnit(BDCDYLX.H, DJDYLY.GZ, bdcs_djdy_gz.getBDCDYID());
				if (!StringHelper.isEmpty(unit)) {
					House h = (House) unit;
					// 获取变更后户对应的宗地信息
					List<RealUnit> zd = UnitTools.loadUnits(BDCDYLX.SHYQZD, DJDYLY.GZ,
							strSqlXMBH + " and bdcdyid ='" + h.getZDBDCDYID() + "'");
					// 若存在，则宗地信息修改，则需要修改宗地信息及对应的权利信息
					if (!StringHelper.isEmpty(zd) && zd.size() > 0) {
						// 获取变更前单元信息
						List<BDCS_DJDY_GZ> djdys_h = dao.getDataList(BDCS_DJDY_GZ.class,
								strSqlXMBH + " and LY ='" + DJDYLY.XZ.Value + "'");
						if (!StringHelper.isEmpty(djdys_h) && djdys_h.size() > 0) {
							BDCS_DJDY_GZ djdy = djdys_h.get(0);
							// 获取变更前户信息
							RealUnit unit_xz = UnitTools.loadUnit(BDCDYLX.H, DJDYLY.XZ, djdy.getBDCDYID());
							if (!StringHelper.isEmpty(unit_xz)) {
								House h_xz = (House) unit_xz;
								// 获取变更前户对应的宗地单元信息
								List<BDCS_DJDY_XZ> djdys_xz = dao.getDataList(BDCS_DJDY_XZ.class,
										"bdcdyid ='" + h_xz.getZDBDCDYID() + "'");
								if (!StringHelper.isEmpty(djdys_xz) && djdys_xz.size() > 0) {
									BDCS_DJDY_XZ bdcs_djdy_xz = djdys_xz.get(0);
									// 拷贝单元、登记单元、权利，附属权利，权利人，权地证人、证书(XZ及历史，删除原有xz信息)

									// 拷贝单元信息到现状跟历史
									RealUnit zd_xz = UnitTools.copyUnit(zd.get(0), BDCDYLX.SHYQZD, DJDYLY.XZ);
									RealUnit zd_ls = UnitTools.copyUnit(zd.get(0), BDCDYLX.SHYQZD, DJDYLY.LS);
									RealUnit zd_dc = UnitTools.loadUnit(BDCDYLX.SHYQZD, DJDYLY.DC, zd.get(0).getId());
									zd_dc.setDJZT(DJZT.YDJ.Value);
									dao.update(zd_dc);
									dao.save(zd_xz);
									dao.save(zd_ls);
										// 构建变更关系
									RebuildDYBG(h_xz.getZDBDCDYID(), bdcs_djdy_xz.getDJDYID(), zd.get(0).getId(),
												bdcs_djdy_xz.getDJDYID(), new Date(), null);
									// 拷贝宗地对应的图形信息到现状跟历史
									super.CopyGeo(zd.get(0).getId(), BDCDYLX.SHYQZD, DJDYLY.GZ);
									if (i==djdys.size()-1) {
										// 删除原有的宗地现状单元信息
										dao.delete(BDCS_SHYQZD_XZ.class, h_xz.getZDBDCDYID());
										// 删除原有的宗地现状对应的图形信息
										super.DeleteGeo(h_xz.getZDBDCDYID(), BDCDYLX.SHYQZD, DJDYLY.XZ);
									}

									String djdybsm = SuperHelper.GeneratePrimaryKey();
									// 拷贝登记单元信息
									BDCS_DJDY_LS djdy_ls_zd = ObjectHelper.copyDJDY_XZToLS(bdcs_djdy_xz);
									djdy_ls_zd.setId(djdybsm);
									djdy_ls_zd.setDJDYID(zd.get(0).getId());
									djdy_ls_zd.setBDCDYH(zd.get(0).getBDCDYH());
									djdy_ls_zd.setBDCDYID(zd.get(0).getId());
									dao.save(djdy_ls_zd);
									BDCS_DJDY_XZ djdy_xz_zd = new BDCS_DJDY_XZ();
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
									List<BDCS_QL_XZ> lst = dao.getDataList(BDCS_QL_XZ.class,
											"DJDYID='" + bdcs_djdy_xz.getDJDYID() + "'");
									if (!StringHelper.isEmpty(lst) && lst.size() > 0) {
										for (BDCS_QL_XZ bdcs_ql_xz : lst) {
											String qlid = SuperHelper.GeneratePrimaryKey();
											String fsqlid = SuperHelper.GeneratePrimaryKey();
											BDCS_QL_LS bdcs_ql_ls = ObjectHelper.copyQL_XZToLS(bdcs_ql_xz);
											bdcs_ql_ls.setBDCDYH(zd.get(0).getBDCDYH());
											bdcs_ql_ls.setId(qlid);
											bdcs_ql_ls.setDJDYID(zd.get(0).getId());
											bdcs_ql_ls.setFSQLID(fsqlid);
											dao.save(bdcs_ql_ls);
											BDCS_FSQL_XZ bdcs_fsql_xz = dao.get(BDCS_FSQL_XZ.class,
													bdcs_ql_xz.getFSQLID());
											if (!StringHelper.isEmpty(bdcs_fsql_xz)) {
												BDCS_FSQL_LS bdcs_fsql_ls = ObjectHelper.copyFSQL_XZToLS(bdcs_fsql_xz);
												bdcs_fsql_ls.setId(fsqlid);
												bdcs_fsql_ls.setBDCDYH(zd.get(0).getBDCDYH());
												bdcs_fsql_ls.setDJDYID(zd.get(0).getId());
												bdcs_fsql_ls.setQLID(qlid);
												dao.save(bdcs_fsql_ls);
												// 更新历史注销登簿信息
												BDCS_FSQL_LS fsql = dao.get(BDCS_FSQL_LS.class, bdcs_ql_xz.getFSQLID());
												if (fsql != null) {
													fsql.setZXDBR(Global.getCurrentUserName());
													fsql.setZXSJ(new Date());
													fsql.setZXYYYY(getProject_id());
													dao.update(fsql);
												}
											}
											List<BDCS_QLR_XZ> qlrs = dao.getDataList(BDCS_QLR_XZ.class,
													"QLID ='" + bdcs_ql_xz.getId() + "'");
											if (!StringHelper.isEmpty(qlrs) && qlrs.size() > 0) {
												for (BDCS_QLR_XZ bdcs_qlr_xz : qlrs) {
													List<String> lstzsid = new ArrayList<String>();
													String qlrid = SuperHelper.GeneratePrimaryKey();
													BDCS_QLR_LS bdcs_qlr_ls = ObjectHelper.copyQLR_XZToLS(bdcs_qlr_xz);
													bdcs_qlr_ls.setId(qlrid);
													bdcs_qlr_ls.setQLID(qlid);
													dao.save(bdcs_qlr_ls);
													List<BDCS_QDZR_XZ> qdzrs = dao.getDataList(BDCS_QDZR_XZ.class,
															" QLRID ='" + bdcs_qlr_xz.getId() + "'");
													if (!StringHelper.isEmpty(qdzrs) && qdzrs.size() > 0) {
														for (BDCS_QDZR_XZ bdcs_qdzr_xz : qdzrs) {
															if (!lstzsid.contains(bdcs_qdzr_xz.getZSID())) {
																lstzsid.add(bdcs_qdzr_xz.getZSID());
																BDCS_ZS_XZ bdcs_zs_xz = dao.get(BDCS_ZS_XZ.class,
																		bdcs_qdzr_xz.getZSID());
																String zsid = SuperHelper.GeneratePrimaryKey();
																if (!StringHelper.isEmpty(bdcs_zs_xz)) {
																	BDCS_ZS_LS bdcs_zs_ls = ObjectHelper
																			.copyZS_XZToLS(bdcs_zs_xz);
																	bdcs_zs_ls.setId(zsid);
																	dao.save(bdcs_zs_ls);
																}
																BDCS_QDZR_LS bdcs_qdzr_ls = ObjectHelper
																		.copyQDZR_XZToLS(bdcs_qdzr_xz);
																String qdzrid = SuperHelper.GeneratePrimaryKey();
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
											// 从历史拷贝到现状层
											Rights ql_xz = RightsTools.copyRightsAll(DJDYLY.LS, DJDYLY.XZ, qlid);
											if (!StringHelper.isEmpty(ql_xz)) {
												dao.save(ql_xz);
											}
											if (i==djdys.size()-1) {
												// 删除原有的宗地的单元信息
												dao.deleteEntity(bdcs_djdy_xz);
												// 根据权利ID删除权利，附属权利，证书，权地证人，权利人信息
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
		}
	}
	public void updateZRZ()
	{
		CommonDao dao=getCommonDao();
		String strSqlXMBH = ProjectHelper.GetXMBHCondition(getXMBH());
		//获取变更后的信息
		List<BDCS_DJDY_GZ> djdys=dao.getDataList(BDCS_DJDY_GZ.class, strSqlXMBH +" and LY ='"+DJDYLY.GZ.Value+"'");
		if(!StringHelper.isEmpty(djdys) && djdys.size()>0){	
			for (int i = 0; i < djdys.size(); i++) {
				BDCS_DJDY_GZ bdcs_djdy_gz = djdys.get(i);
				// 获取变更后户信息
				RealUnit unit = UnitTools.loadUnit(BDCDYLX.H, DJDYLY.GZ, bdcs_djdy_gz.getBDCDYID());
				if (!StringHelper.isEmpty(unit)) {
					House h = (House) unit;
					// 获取变更后户对应的自然幢信息
					List<RealUnit> zrz = UnitTools.loadUnits(BDCDYLX.ZRZ, DJDYLY.GZ,
							strSqlXMBH + " and bdcdyid ='" + h.getZRZBDCDYID() + "'");
					// 若存在，则自然幢信息修改，则需要修改自然幢信息
					if (!StringHelper.isEmpty(zrz) && zrz.size() > 0) {
						// 获取变更前单元信息
						List<BDCS_DJDY_GZ> djdys_h = dao.getDataList(BDCS_DJDY_GZ.class,
								strSqlXMBH + " and LY ='" + DJDYLY.XZ.Value + "'");
						if (!StringHelper.isEmpty(djdys_h) && djdys_h.size() > 0) {
							BDCS_DJDY_GZ djdy = djdys_h.get(0);
							// 获取变更前户信息
							RealUnit unit_xz = UnitTools.loadUnit(BDCDYLX.H, DJDYLY.XZ, djdy.getBDCDYID());
							if (!StringHelper.isEmpty(unit_xz)) {
								House h_xz = (House) unit_xz;
								// 获取变更前户对应的宗地单元信息
								List<BDCS_DJDY_XZ> djdys_xz = dao.getDataList(BDCS_DJDY_XZ.class,
										"bdcdyid ='" + h_xz.getZDBDCDYID() + "'");
								if (!StringHelper.isEmpty(djdys_xz) && djdys_xz.size() > 0) {
									BDCS_DJDY_XZ bdcs_djdy_xz = djdys_xz.get(0);
									// 拷贝单元、登记单元、权利，附属权利，权利人，权地证人、证书(XZ及历史，删除原有xz信息)

									// 拷贝单元信息到现状跟历史
									RealUnit zrz_xz = UnitTools.copyUnit(zrz.get(0), BDCDYLX.ZRZ, DJDYLY.XZ);
									RealUnit zrz_ls = UnitTools.copyUnit(zrz.get(0), BDCDYLX.ZRZ, DJDYLY.LS);
									RealUnit zrz_dc = UnitTools.loadUnit(BDCDYLX.ZRZ, DJDYLY.DC, zrz.get(0).getId());
									zrz_dc.setDJZT(DJZT.YDJ.Value);
									dao.update(zrz_dc);
									dao.save(zrz_xz);
									dao.save(zrz_ls);
									// 构建变更关系
									RebuildDYBG(h_xz.getZRZBDCDYID(), bdcs_djdy_xz.getBDCDYID(), zrz.get(0).getId(),
												bdcs_djdy_xz.getDJDYID(), new Date(), null);
									// 拷贝自然幢对应的图形信息到现状跟历史
									super.CopyGeo(h.getId(), BDCDYLX.H, DJDYLY.GZ);
									if (i==djdys.size()-1) {
										// 删除变更前自然幢单元信息
										dao.delete(BDCS_ZRZ_XZ.class, h_xz.getZRZBDCDYID());
										// 删除原有的宗地现状对应的图形信息
										super.DeleteGeo(h_xz.getZRZBDCDYID(), BDCDYLX.ZRZ, DJDYLY.XZ);
									}
								}
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
		List<UnitTree> tree = super.getUnitList();
		BDCS_XMXX xmxx = Global.getXMXXbyXMBH(this.getXMBH());
		if (!StringHelper.isEmpty(xmxx) && SF.YES.Value.equals(xmxx.getSFDB())) {
			if (!StringHelper.isEmpty(tree)) {
				for (int i = 0; i < tree.size(); i++) {
					UnitTree t = tree.get(i);
					if (!StringHelper.isEmpty(t)) {
						//获取变更前的权利信息
						if (DJDYLY.LS.Name.toUpperCase().equals(t.getLy().toUpperCase())||DJDYLY.XZ.Name.toUpperCase().equals(t.getLy().toUpperCase())) {
							String fulSql = "select ql.qlid from BDCK.BDCS_QL_LS ql left join BDCK.BDCS_FSQL_LS fsql on "
									+ "fsql.qlid=ql.qlid where fsql.zxdyywh='"
									+ xmxx.getPROJECT_ID()
									+ "' "
									+ "and ql.djdyid='"
									+ t.getDjdyid()
									+ "' and ql.qllx='" + this.getQllx().Value + "'";
							@SuppressWarnings("rawtypes")
							List<Map> lstql = getCommonDao()
									.getDataListByFullSql(fulSql);
							if (!StringHelper.isEmpty(lstql)
									&& lstql.size() > 0) {
								@SuppressWarnings("rawtypes")
								Map m = lstql.get(0);
								String qlid = StringHelper.formatObject(m
										.get("QLID"));
								tree.get(i).setQlid(qlid);
								tree.get(i).setOldqlid(qlid);
								tree.get(i).setLy("ls");
							}
						}
					}
				}
			}
			// 重写
		}else if(!StringHelper.isEmpty(xmxx)){
			if (!StringHelper.isEmpty(tree)) {
				for (int i = 0; i < tree.size(); i++) {
					UnitTree t = tree.get(i);
					if (!StringHelper.isEmpty(t)) {
						if(!DJDYLY.GZ.Name.equals(t.getLy())){
							String qllxarray = " ('1','2','3','4','5','6','7','8','10','11','12','15','24','36')";
							String hqlCondition = MessageFormat.format(" DJDYID IN (SELECT DJDYID FROM BDCS_DJDY_XZ WHERE BDCDYID=''{0}'') AND QLLX IN {1}", t.getBdcdyid(), qllxarray);
							List<Rights> qls=RightsTools.loadRightsByCondition(DJDYLY.XZ, hqlCondition);
							if(qls!=null&&qls.size()>0){
								t.setOldqlid(qls.get(0).getId());
							}
						}
						else
						{
						 //判断自然幢跟宗地信息来源
						if(BDCDYLX.H.equals(this.getBdcdylx()))
						{
							String zdbdcdyid=t.getZdbdcdyid();
							List<RealUnit> zds=UnitTools.loadUnits(BDCDYLX.SHYQZD, DJDYLY.GZ, "BDCDYID ='" +zdbdcdyid +"' and "+ProjectHelper
									.GetXMBHCondition(this.getXMBH()));
							if(!StringHelper.isEmpty(zds) && zds.size()>0)
							{
								t.setZdly("gz");
							}
							else
							{
								t.setZdly("ls");
							}
							String zrzbdcdyid=t.getZrzbdcdyid();
							List<RealUnit> zrzs=UnitTools.loadUnits(BDCDYLX.ZRZ, DJDYLY.GZ, "BDCDYID ='" +zrzbdcdyid +"' and "+ProjectHelper
									.GetXMBHCondition(this.getXMBH()));
							if(!StringHelper.isEmpty(zrzs) && zrzs.size()>0)
							{
								t.setZrzly("gz");
							}
							else
							{
								t.setZrzly("ls");
							}
						}
						}
					}
				}
			}
		}
		return tree;
	}
	/**
	 * 根据申请人ID数组生成权利人
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
//			List<BDCS_DJDY_GZ> bgqdjdys = dao.getDataList(BDCS_DJDY_GZ.class, xmbhHql + " and LY = '" + ConstValue.DJDYLY.XZ.Value + "' ");
			String msgName = "";
			if (djdys != null && djdys.size() > 0) {
				mashaller = JAXBContext.newInstance(Message.class).createMarshaller();
				Calendar calendar = Calendar.getInstance(); // 创建一个日历对象
				calendar.setTime(new Date()); // 用当前时间初始化日历时间
//				String cyear = calendar.get(Calendar.YEAR) + "";
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
//							String preEstateNum = "";
							// 目前未维护的字段先手动赋值 diaoliwei 2015-10-15
							if (null != zd) {
								if (StringUtils.isEmpty(zd.getZDT())) {
									zd.setZDT("无");
								}
							}
//							for (int j = 0; j < bgqdjdys.size(); j++) {
//								BDCS_DJDY_GZ bgqdjdy = bgqdjdys.get(j);
//								BDCS_SHYQZD_XZ zd_XZ = dao.get(BDCS_SHYQZD_XZ.class, bgqdjdy.getBDCDYID());
//								if (zd_XZ != null && (j == 0 || j == bgqdjdys.size() - 1)) {
//									preEstateNum += zd_XZ.getBDCDYH();
//								} else {
//									preEstateNum += zd_XZ.getBDCDYH() + ",";
//								}
//							}
							// 标记单元号是否发生变化了，是否取消空间节点的存在
//							boolean flag = false;
//							if (!(zd.getBDCDYH().equals(preEstateNum)) && !StringUtils.isEmpty(preEstateNum)) {
//								flag = true;
//							}
							Message msg = exchangeFactory.createMessage(true);

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

								List<DJFDJSF> sfList = msg.getData().getDJSF();
								sfList = packageXml.getDJSF(zd,ywh, this.getXMBH());
								msg.getData().setDJSF(sfList);

								List<DJFDJSH> sh = msg.getData().getDJSH();
								sh = packageXml.getDJFDJSH(zd, ywh, this.getXMBH(), actinstID);
								msg.getData().setDJSH(sh);

								List<DJFDJSZ> sz = packageXml.getDJFDJSZ(zd, ywh, this.getXMBH());
								msg.getData().setDJSZ(sz);

								List<DJFDJFZ> fz = packageXml.getDJFDJFZ(zd, ywh, this.getXMBH());
								msg.getData().setDJFZ(fz);
								List<DJFDJGD> gd = packageXml.getDJFDJGD(zd, ywh,this.getXMBH());
								msg.getData().setDJGD(gd);
								
								BDCS_SHYQZD_GZ zd2 = null;
								if (null != dybg) {
									zd2 = new BDCS_SHYQZD_GZ();
									zd2.setId(dybg.getLBDCDYID());
//								zd.setId(dybg.getLBDCDYID());
								}
								// 如果当前不动产单元号和上一个不动产单元号不同的话，把空间节点加进去
//								if (!(preEstateNum.equals(zd.getBDCDYH()))) {
									List<ZDK103> zdk = msg.getData().getZDK103();
									zdk = packageXml.getZDK103(zdk, zd2, null, null);
									msg.getData().setZDK103(zdk);
//								}
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
					if (QLLX.GYJSYDSHYQ_FWSYQ.Value.equals(this.getQllx().Value)||"H".equals(super.getBdcdylx().toString())&&!"02".equals(djdy.getBDCDYLX())) { // 房屋所有权
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
//							String preEstateNum = "";
//							for (BDCS_DJDY_GZ bgqdjdy : bgqdjdys) {
//								BDCS_H_LS h_LS = dao.get(BDCS_H_LS.class, bgqdjdy.getBDCDYID());
//								if (!StringUtils.isEmpty(h.getId()) && h.getId().equals(h_LS.getId())) {
//									preEstateNum = h_LS.getBDCDYH();
//									break;
//								}
//							}
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

								List<DJFDJSF> sfList = msg.getData().getDJSF();
								sfList = packageXml.getDJSF(h,ywh, this.getXMBH());
								msg.getData().setDJSF(sfList);

								List<DJFDJSH> sh = msg.getData().getDJSH();
								sh = packageXml.getDJFDJSH(h, ywh, this.getXMBH(), actinstID);
								msg.getData().setDJSH(sh);

								List<DJFDJSZ> sz = packageXml.getDJFDJSZ(h, ywh, this.getXMBH());
								msg.getData().setDJSZ(sz);
								List<DJFDJFZ> fz = packageXml.getDJFDJFZ(h ,ywh, this.getXMBH());
								msg.getData().setDJFZ(fz);
								List<DJFDJGD> gd = packageXml.getDJFDJGD(h, ywh,this.getXMBH());
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
}
