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
import com.supermap.realestate.registration.model.WFD_MAPPING;
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
import com.supermap.realestate.registration.util.ConstValue;
import com.supermap.realestate.registration.util.ConstValue.BDCDYLX;
import com.supermap.realestate.registration.util.ConstValue.DJDYLY;
import com.supermap.realestate.registration.util.ConstValue.QLLX;
import com.supermap.realestate.registration.util.ConstValue.SF;
import com.supermap.realestate.registration.util.ConstValue.SQRLB;
import com.supermap.realestate.registration.util.Global;
import com.supermap.realestate.registration.util.ProjectHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.realestate.registration.util.YwLogUtil;
import com.supermap.wisdombusiness.core.SuperHelper;
import com.supermap.wisdombusiness.core.SuperSpringContext;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;

/*
 1、国有建设用地使用权/房屋所有权变更登记
 */
/**
 * 
 * 变更登记处理类(房地一起转移)
 * 
 * @ClassName: BGDJHandler_HouseAndLand
 * @author yuxuebin
 * @date 2016年05月10日 14:43:29
 */
public class BGDJHandler_HouseAndLand extends DJHandlerBase implements DJHandler {

	/**
	 * 构造函数
	 * 
	 * @param info
	 */
	public BGDJHandler_HouseAndLand(ProjectInfo info) {
		super(info);
	}

	/**
	 * 添加不动产单元
	 */
	@Override
	public boolean addBDCDY(String bdcdyid) {
		boolean bsuccess = false;
		if(!IsCanAccept(bdcdyid)){
			return bsuccess;
		}
		CommonDao dao = getCommonDao();
		BDCS_DJDY_GZ djdy = super.createDJDYfromXZ(bdcdyid);
		if (djdy != null) {			
			// 拷贝权利信息
			StringBuilder builder = new StringBuilder();
			builder.append(" DJDYID='").append(djdy.getDJDYID());
			builder.append("' AND QLLX='").append(getQllx().Value).append("'");
			List<BDCS_QL_XZ> qls = dao.getDataList(BDCS_QL_XZ.class,
					builder.toString());
			if (qls != null && qls.size() > 0) {
				String newqzh = "";
				String workflowcode = ProjectHelper.getWorkflowCodeByProjectID(this
						.getProject_id());
				List<WFD_MAPPING> listCode = getCommonDao().getDataList(
						WFD_MAPPING.class, "WORKFLOWCODE='" + workflowcode + "'");
				if (listCode != null && listCode.size() > 0) {
					newqzh = listCode.get(0).getNEWQZH();
				}
				if (SF.NO.Value.equals(newqzh)) {
					super.CopyQLXXFromXZ(qls.get(0).getId());
				} else {
					super.CopyQLXXFromXZExceptBDCQZH(qls.get(0).getId());
				}
			}
			dao.flush();
			dao.save(djdy);
			dao.flush();
			// 把权利人拷贝过来放到申请人里边 add by diaoliwei 2015-7-31
			//copyApplicant(bdcs_djdy_gz.getDJDYID());
			copyApplicantFromGZ(djdy.getDJDYID());
			dao.flush();
			bsuccess = true;
		
		}
		return bsuccess;
	}
	
	private boolean IsCanAccept(String bdcdyid){
		RealUnit unit=UnitTools.loadUnit(BDCDYLX.H, DJDYLY.XZ, bdcdyid);
		if(unit==null){
			super.setErrMessage("房屋单元不存在！");
			return false;
		}
		House h=(House)unit;
		String zdbdcdyid=h.getZDBDCDYID();
		if(StringHelper.isEmpty(zdbdcdyid)){
			super.setErrMessage("未关联宗地！");
			return false;
		}
		RealUnit unit_zd=UnitTools.loadUnit(BDCDYLX.SHYQZD, DJDYLY.XZ, zdbdcdyid);
		if(unit_zd==null){
			super.setErrMessage("未找到关联宗地！");
			return false;
		}
		List<Rights> rights=RightsTools.loadRightsByCondition(DJDYLY.XZ, "QLLX IN ('4','6','8') AND DJDYID IN (SELECT DJDYID FROM BDCS_DJDY_XZ WHERE BDCDYID='"+bdcdyid+"')");
		if(rights==null||rights.size()<=0){
			super.setErrMessage("未找到房屋所有权！");
			return false;
		}
		Rights ql=rights.get(0);
		List<RightsHolder> list_qlr_h=RightsHolderTools.loadRightsHolders(DJDYLY.XZ, ql.getId());
		List<String> list_qlrxx_h=new ArrayList<String>();
		if(list_qlr_h!=null&&list_qlr_h.size()>0){
			for(RightsHolder qlr:list_qlr_h){
				String qlrxx=StringHelper.formatObject(qlr.getQLRMC())+"&&"+StringHelper.formatObject(qlr.getZJH());
				if(!list_qlrxx_h.contains(qlrxx)){
					list_qlrxx_h.add(qlrxx);
				}
			}
		}
		List<Rights> rights_zd=RightsTools.loadRightsByCondition(DJDYLY.XZ, "QLLX IN ('3','5','7') AND DJDYID IN (SELECT DJDYID FROM BDCS_DJDY_XZ WHERE BDCDYID='"+zdbdcdyid+"')");
		if(rights_zd==null||rights_zd.size()<=0){
			super.setErrMessage("未找到宗地上的使用权！");
			return false;
		}
		Rights ql_zd=rights_zd.get(0);
		List<RightsHolder> list_qlr_zd=RightsHolderTools.loadRightsHolders(DJDYLY.XZ, ql_zd.getId());
		if(list_qlr_zd!=null&&list_qlr_zd.size()>0){
			for(RightsHolder qlr:list_qlr_zd){
				String qlrxx=StringHelper.formatObject(qlr.getQLRMC())+"&&"+StringHelper.formatObject(qlr.getZJH());
				if(list_qlrxx_h.contains(qlrxx)){
					list_qlrxx_h.remove(qlrxx);
				}
			}
		}
		if(list_qlrxx_h!=null&&list_qlrxx_h.size()>0){
			super.setErrMessage("房屋和宗地中权利人情况不一致！");
			return false;
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
		if (!YCYGCanecl()) {
			return false;
		}
		
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
				}
			}
		}
		this.SetSFDB();
		getCommonDao().flush();
		super.alterCachedXMXX();
		return true;
	}
	
	@SuppressWarnings("unused")
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


	private boolean YCYGCanecl() {
		boolean bCancel = true;
		if (!BDCDYLX.H.equals(super.getBdcdylx())) {
			return true;
		}
		String dbr = Global.getCurrentUserName();
		String strSqlXMBH = ProjectHelper.GetXMBHCondition(getXMBH());
		List<BDCS_DJDY_GZ> djdys = getCommonDao().getDataList(
				BDCS_DJDY_GZ.class, strSqlXMBH);
		if (djdys != null && djdys.size() > 0) {			
			for (BDCS_DJDY_GZ djdy : djdys) {
				String SCBDCDYID = djdy.getBDCDYID();
				List<YC_SC_H_XZ> listGX = getCommonDao().getDataList(
						YC_SC_H_XZ.class, "SCBDCDYID='" + SCBDCDYID + "'");
				if (listGX != null && listGX.size() > 0) {
					for (YC_SC_H_XZ gx : listGX) {
						String YCBDCDYID = gx.getYCBDCDYID();
						if (StringHelper.isEmpty(YCBDCDYID)) {
							continue;
						}
						List<BDCS_DJDY_XZ> djdys_yc = getCommonDao()
								.getDataList(BDCS_DJDY_XZ.class,
										"BDCDYID='" + YCBDCDYID + "'");
						if (djdys_yc == null || djdys_yc.size() <= 0) {
							continue;
						}
						String ycdjdyid = djdys_yc.get(0).getDJDYID();
						List<Rights> ycyg_qls = RightsTools
								.loadRightsByCondition(DJDYLY.XZ, "DJDYID='"
										+ ycdjdyid
										+ "' AND DJLX='700' AND QLLX='4'");
						if (ycyg_qls != null && ycyg_qls.size() > 0) {
							for (Rights ycyg_ql : ycyg_qls) {
								String qlid = ycyg_ql.getId();
								RightsTools.deleteRightsAll(DJDYLY.XZ, qlid);
								// 更新历史附属权利信息
								SubRights subright = RightsTools
										.loadSubRightsByRightsID(DJDYLY.LS,
												qlid);
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

/*	private boolean ZYYGCanecl() {
		boolean bCancel = true;
		String dbr = Global.getCurrentUserName();
		String strSqlXMBH = ProjectHelper.GetXMBHCondition(getXMBH());
		List<BDCS_DJDY_GZ> djdys = getCommonDao().getDataList(
				BDCS_DJDY_GZ.class, strSqlXMBH);
		if (djdys != null && djdys.size() > 0) {
			for (BDCS_DJDY_GZ djdy : djdys) {
				List<String> list_qlrmc_zjh = new ArrayList<String>();
				List<RightsHolder> qlrs = RightsHolderTools.loadRightsHolders(
						DJDYLY.GZ, djdy.getDJDYID(), super.getXMBH());
				if (qlrs != null && qlrs.size() > 0) {
					for (RightsHolder qlr : qlrs) {
						list_qlrmc_zjh.add(qlr.getQLRMC() + "&"
								+ StringHelper.formatObject(qlr.getZJH()));
					}
				}
				List<Rights> zyyg_qls = RightsTools.loadRightsByCondition(
						DJDYLY.XZ, "DJDYID='" + djdy.getDJDYID()
								+ "' AND DJLX='700' AND QLLX='99'");
				if (zyyg_qls != null && zyyg_qls.size() > 0) {
					for (Rights zyyg_ql : zyyg_qls) {
						List<RightsHolder> zyyg_qlrs = RightsHolderTools
								.loadRightsHolders(DJDYLY.XZ, zyyg_ql.getId());
						if (zyyg_qlrs != null && zyyg_qlrs.size() > 0) {
							for (RightsHolder zyyg_qlr : zyyg_qlrs) {
								String str_qlrmc_zjh = zyyg_qlr.getQLRMC()
										+ "&"
										+ StringHelper.formatObject(zyyg_qlr
												.getZJH());
								if (!list_qlrmc_zjh.contains(str_qlrmc_zjh)) {
									super.setErrMessage("单元已办理转移预告登记，且转移预告权利人不在转移后权利人中");
									return false;
								}
							}
						}
					}
				}

			}
			for (BDCS_DJDY_GZ djdy : djdys) {
				List<Rights> zyyg_qls = RightsTools.loadRightsByCondition(
						DJDYLY.XZ, "DJDYID='" + djdy.getDJDYID()
								+ "' AND DJLX='700' AND QLLX='99'");
				if (zyyg_qls != null && zyyg_qls.size() > 0) {
					for (Rights zyyg_ql : zyyg_qls) {
						String qlid = zyyg_ql.getId();
						RightsTools.deleteRightsAll(DJDYLY.XZ, qlid);
						// 更新历史附属权利信息
						SubRights subright = RightsTools
								.loadSubRightsByRightsID(DJDYLY.LS, qlid);
						if (subright != null) {
							subright.setZXSJ(new Date());
							subright.setZXDBR(dbr);
							subright.setZXDYYWH(super.getProject_id());
							getCommonDao().update(subright);
						}
					}
				}
			}
			getCommonDao().flush();
		}
		return bCancel;
	}*/

	/**
	 * 移除不动产单元
	 */
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
			String _hqlCondition = MessageFormat.format(
					"XMBH=''{0}'' AND DJDYID=''{1}''", getXMBH(), djdyid);
			RightsTools.deleteRightsAllByCondition(DJDYLY.GZ, _hqlCondition);
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
		return super.getUnitList();
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
	 * 把权利人拷贝过来放到申请人里边
	 * 
	 * @author diaoliwei
	 * @Data 2015-7-31
	 * @param bdcdyid_xz
	 */
	protected void copyApplicantFromGZ(String bdcdyid) {
		CommonDao dao = getCommonDao();

		String qlrSql = MessageFormat.format(
				"QLID IN (SELECT id FROM BDCS_QL_GZ WHERE QLLX IN(''3'',''4'',''5'',''6'',''7'',''8'',''9'',''10'',''15'' ,''24'',''36'') AND DJDYID=''{0}'' AND XMBH=''{1}'')",
				bdcdyid, getXMBH());
		List<BDCS_QLR_GZ> qlrs = dao.getDataList(BDCS_QLR_GZ.class, qlrSql);
		for (BDCS_QLR_GZ bdcs_qlr_gz : qlrs) {
			String zjhm = bdcs_qlr_gz.getZJH();
			String qlrmc = bdcs_qlr_gz.getQLRMC();
			boolean flag = false;
			if (!StringHelper.isEmpty(qlrmc) ) {
				String sqrSql = "";
				if(!StringHelper.isEmpty(zjhm))
				{
					sqrSql=MessageFormat.format(
						"XMBH=''{0}'' AND SQRXM=''{1}'' AND ZJH=''{2}''",
						getXMBH(), qlrmc, zjhm);
				}
				else
				{
					sqrSql=MessageFormat.format(
							"XMBH=''{0}'' AND SQRXM=''{1}'' AND ZJH IS NULL",
							getXMBH(), qlrmc);
				}
				List<BDCS_SQR> sqrs = dao.getDataList(BDCS_SQR.class, sqrSql);
				if (sqrs != null && sqrs.size() > 0) {
					flag = true;
					//权利人关联申请人ID
					bdcs_qlr_gz.setSQRID(sqrs.get(0).getId());
					dao.update(bdcs_qlr_gz);
				}
			}
			// 判断申请人是否已经添加过，如果添加过，就不再添加
			if (!flag) {
				String sqrid = getPrimaryKey();
				BDCS_SQR bdcs_sqr = new BDCS_SQR();
				bdcs_sqr.setGYFS(bdcs_qlr_gz.getGYFS());
				bdcs_sqr.setFZJG(bdcs_qlr_gz.getFZJG());
				bdcs_sqr.setGJDQ(bdcs_qlr_gz.getGJ());
				bdcs_sqr.setGZDW(bdcs_qlr_gz.getGZDW());
				bdcs_sqr.setXB(bdcs_qlr_gz.getXB());
				bdcs_sqr.setHJSZSS(bdcs_qlr_gz.getHJSZSS());
				bdcs_sqr.setSSHY(bdcs_qlr_gz.getSSHY());
				bdcs_sqr.setYXBZ(bdcs_qlr_gz.getYXBZ());
				bdcs_sqr.setQLBL(bdcs_qlr_gz.getQLBL());
				bdcs_sqr.setQLMJ(StringHelper.formatObject(bdcs_qlr_gz.getQLMJ()));
				bdcs_sqr.setSQRXM(bdcs_qlr_gz.getQLRMC());
				bdcs_sqr.setSQRLB("1");
				bdcs_sqr.setSQRLX(bdcs_qlr_gz.getQLRLX());
				bdcs_sqr.setDZYJ(bdcs_qlr_gz.getDZYJ());
				bdcs_sqr.setLXDH(bdcs_qlr_gz.getDH());
				bdcs_sqr.setZJH(bdcs_qlr_gz.getZJH());
				bdcs_sqr.setZJLX(bdcs_qlr_gz.getZJZL());
				bdcs_sqr.setTXDZ(bdcs_qlr_gz.getDZ());
				bdcs_sqr.setYZBM(bdcs_qlr_gz.getYB());
				bdcs_sqr.setXMBH(getXMBH());
				bdcs_sqr.setId(sqrid);
				 bdcs_sqr.setGLQLID(bdcs_qlr_gz.getQLID());
				dao.save(bdcs_sqr);
				//权利人关联申请人ID
				bdcs_qlr_gz.setSQRID(bdcs_sqr.getId());
				dao.update(bdcs_qlr_gz);
			}
		}
	}
	
	@Override
	public Map<String, String> exportXML(String path, String actinstID) {
		Marshaller mashaller;
		Map<String, String> names = new HashMap<String, String>();
		try {
			CommonDao dao = super.getCommonDao();
			String xmbhHql = ProjectHelper.GetXMBHCondition(super.getXMBH());
			List<BDCS_DJDY_GZ> djdys = dao.getDataList(BDCS_DJDY_GZ.class,
					xmbhHql);
			if (djdys != null && djdys.size() > 0) {
				mashaller = JAXBContext.newInstance(Message.class)
						.createMarshaller();
				// mashaller.setProperty(Marshaller.JAXB_ENCODING, "gbk");
				Calendar calendar = Calendar.getInstance(); // 创建一个日历对象
				calendar.setTime(new Date()); // 用当前时间初始化日历时间
//				String cyear = calendar.get(Calendar.YEAR) + "";
				String ywh = packageXml.GetYWLSHByYWH(this.getProject_id());
				BDCS_XMXX xmxx = dao.get(BDCS_XMXX.class, super.getXMBH());
				String result = "";
				String msgName = "";
				for (int i = 0; i < djdys.size(); i++) {
					BDCS_DJDY_GZ djdy = djdys.get(i);
					BDCS_QL_GZ ql = super.getQL(djdy.getDJDYID());
					List<BDCS_QLR_GZ> qlrs = super.getQLRs(ql.getId());
					List<BDCS_SQR> sqrs = new ArrayList<BDCS_SQR>();
					ProjectServiceImpl serviceImpl = SuperSpringContext
							.getContext().getBean(ProjectServiceImpl.class);
					sqrs = serviceImpl.getSQRList(super.getXMBH());

					if (QLLX.GYJSYDSHYQ.Value.equals(this.getQllx().Value)
							|| QLLX.ZJDSYQ.Value.equals(this.getQllx().Value)
							|| QLLX.JTJSYDSYQ.Value
									.equals(this.getQllx().Value)) { // 国有建设使用权、宅基地、集体建设用地使用权
						try {
							BDCS_SHYQZD_XZ zd = dao.get(BDCS_SHYQZD_XZ.class,
									djdy.getBDCDYID());
							Message msg = exchangeFactory.createMessageBySHYQ();
							super.fillHead(msg, i, ywh,zd.getBDCDYH(),zd.getQXDM(),ql.getLYQLID());

							msg.getHead().setParcelID(
									StringHelper.formatObject(zd.getZDDM()));
							msg.getHead().setEstateNum(
									StringHelper.formatObject(zd.getBDCDYH()));
//							msg.getHead().setPreEstateNum(
//									StringHelper.formatObject(zd.getBDCDYH()));
							if (zd != null
									&& !StringUtils.isEmpty(zd.getQXDM())) {
								msg.getHead().setAreaCode(zd.getQXDM());
							}

							if (djdy != null) {
								List<ZTTGYQLR> zttqlr = msg.getData()
										.getGYQLR();
								zttqlr = packageXml.getZTTGYQLRs(zttqlr, qlrs,
										zd, ql, null, null, null, null);
								msg.getData().setGYQLR(zttqlr);

								KTTZDJBXX jbxx = msg.getData().getKTTZDJBXX();
								jbxx = packageXml.getZDJBXX(jbxx, zd, ql, null,
										null);

								KTFZDBHQK bhqk = msg.getData().getZDBHQK();
								bhqk = packageXml.getKTFZDBHQK(bhqk, zd, ql,
										null, null);
								msg.getData().setZDBHQK(bhqk);

								QLFQLJSYDSYQ syq = msg.getData().getQLJSYDSYQ();
								syq = packageXml.getQLFQLJSYDSYQ(syq, zd, ql,
										ywh);

								DJTDJSLSQ sq = msg.getData().getDJSLSQ();
								sq = packageXml.getDJTDJSLSQ(sq, ywh, zd, ql,
										xmxx, null, xmxx.getSLSJ(),
										null, null, null);

								List<DJFDJSJ> sj = msg.getData().getDJSJ();
								sj = packageXml.getDJFDJSJ(zd, ywh,actinstID);
								msg.getData().setDJSJ(sj);

								List<DJFDJSF> sfList = msg.getData().getDJSF();
								sfList = packageXml.getDJSF(zd,ywh, this.getXMBH());
								msg.getData().setDJSF(sfList);

								List<DJFDJSH> sh = msg.getData().getDJSH();
								sh = packageXml.getDJFDJSH(zd,ywh,this.getXMBH(), actinstID);
								msg.getData().setDJSH(sh);

								List<DJFDJSZ> sz = packageXml.getDJFDJSZ(zd, ywh, this.getXMBH());
								msg.getData().setDJSZ(sz);

								List<DJFDJFZ> fz = packageXml.getDJFDJFZ(zd, ywh, this.getXMBH());
								msg.getData().setDJFZ(fz);
								List<DJFDJGD> gd = packageXml.getDJFDJGD(zd, ywh,this.getXMBH());
								msg.getData().setDJGD(gd);

								List<DJFDJSQR> djsqrs = msg.getData()
										.getDJSQR();
								djsqrs = packageXml.getDJSQRs(djsqrs, sqrs,
										zd.getYSDM(), ywh, zd.getBDCDYH());
								msg.getData().setDJSQR(djsqrs);

								FJF100 fj = msg.getData().getFJF100();
								fj = packageXml.getFJF(fj);

							}
							msgName = "Biz" + msg.getHead().getBizMsgID()
									+ ".xml";
							mashaller.marshal(msg, new File(path + msgName));
							result = uploadFile(path + msgName,
									ConstValue.RECCODE.JSYDSHYQ_ZYDJ.Value,
									actinstID, djdy.getId(), ql.getId());
							names.put(djdy.getDJDYID(), msg.getHead()
									.getBizMsgID() + ".xml");
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					if (QLLX.GYJSYDSHYQ_FWSYQ.Value
							.equals(this.getQllx().Value)) { // 房屋所有权
						try {
							BDCS_H_XZ h = dao.get(BDCS_H_XZ.class,
									djdy.getBDCDYID());
							BDCS_ZRZ_XZ zrz_ = null;
							BDCS_LJZ_XZ ljz_ = null;
							BDCS_SHYQZD_XZ zd = null;
							if (h != null
									&& !StringUtils.isEmpty(h.getZRZBDCDYID())) {
								zrz_ = dao.get(BDCS_ZRZ_XZ.class,
										h.getZRZBDCDYID());
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
							if (h != null
									&& !StringUtils.isEmpty(h.getZDBDCDYID())) {
								zd = dao.get(BDCS_SHYQZD_XZ.class,
										h.getZDBDCDYID());
								if (zd != null) {
									zrz_.setZDDM(zd.getZDDM());
								}
							}
							Message msg = exchangeFactory
									.createMessageByFWSYQ2();
							super.fillHead(msg, i, ywh,h.getBDCDYH(),h.getQXDM(),ql.getLYQLID());
							msg.getHead().setParcelID(zrz_.getZDDM());
							msg.getHead().setEstateNum(h.getBDCDYH());
//							msg.getHead().setPreEstateNum(h.getBDCDYH());
							if (zd != null
									&& !StringUtils.isEmpty(zd.getQXDM())) {
								msg.getHead().setAreaCode(zd.getQXDM());
							}
							if (djdy != null) {

								List<ZTTGYQLR> zttqlr = msg.getData()
										.getGYQLR();
								zttqlr = packageXml.getZTTGYQLRs(zttqlr, qlrs,
										null, ql, h, null, null, null);
								msg.getData().setGYQLR(zttqlr);

								KTTFWZRZ zrz = msg.getData().getKTTFWZRZ();
								zrz = packageXml.getKTTFWZRZ(zrz, zrz_);

								KTTFWLJZ ljz = msg.getData().getKTTFWLJZ();
								ljz = packageXml.getKTTFWLJZ(ljz, ljz_,zd);

								KTTFWC kttc = msg.getData().getKTTFWC();
								kttc = packageXml.getKTTFWC(kttc, c_, zrz);
								msg.getData().setKTTFWC(kttc);

								KTTFWH fwh = msg.getData().getKTTFWH();
								fwh = packageXml.getKTTFWH(fwh, h);

								QLTFWFDCQYZ fdcqyz = msg.getData()
										.getQLTFWFDCQYZ();
								fdcqyz = packageXml.getQLTFWFDCQYZ(fdcqyz, h,
										ql, ywh);

								DJTDJSLSQ sq = msg.getData().getDJSLSQ();
								sq = packageXml.getDJTDJSLSQ(sq, ywh, null, ql,
										xmxx, h, xmxx.getSLSJ(), null,
										null, null);

								List<DJFDJSJ> sj = msg.getData().getDJSJ();
								sj = packageXml.getDJFDJSJ(h, ywh,actinstID);
								msg.getData().setDJSJ(sj);

								List<DJFDJSF> sfList = msg.getData().getDJSF();
								sfList = packageXml.getDJSF(h,ywh, this.getXMBH());
								msg.getData().setDJSF(sfList);

								List<DJFDJSH> sh = msg.getData().getDJSH();
								sh = packageXml.getDJFDJSH(h, ywh,
										this.getXMBH(), actinstID);
								msg.getData().setDJSH(sh);

								List<DJFDJSZ> sz = packageXml.getDJFDJSZ(h, ywh, this
										.getXMBH());
								msg.getData().setDJSZ(sz);

								List<DJFDJFZ> fz = packageXml.getDJFDJFZ(h, ywh, this
										.getXMBH());
								msg.getData().setDJFZ(fz);
								List<DJFDJGD> gd = packageXml.getDJFDJGD(h, ywh,this.getXMBH());
								msg.getData().setDJGD(gd);

								List<DJFDJSQR> djsqrs = msg.getData()
										.getDJSQR();
								djsqrs = packageXml.getDJSQRs(djsqrs, sqrs,
										h.getYSDM(), ywh, h.getBDCDYH());
								msg.getData().setDJSQR(djsqrs);

								FJF100 fj = msg.getData().getFJF100();
								fj = packageXml.getFJF(fj);
							}
							msgName = "Biz" + msg.getHead().getBizMsgID()
									+ ".xml";
							mashaller.marshal(msg, new File(path + msgName));
							result = uploadFile(path + msgName,
									ConstValue.RECCODE.FDCQDZ_ZYDJ.Value,
									actinstID, djdy.getId(), ql.getId());
							names.put(djdy.getDJDYID(), msg.getHead()
									.getBizMsgID() + ".xml");
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

								KTTZDJBXX jbxx = msg.getData().getKTTZDJBXX();
								jbxx = packageXml.getZDJBXX(jbxx, null, ql,
										oland, null);

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
								bhqk = packageXml.getKTFZDBHQK(bhqk, null, ql,
										oland, null);
								msg.getData().setZDBHQK(bhqk);

								QLFQLTDSYQ tdsyq = msg.getData()
										.getQLFQLTDSYQ();
								tdsyq = packageXml.getQLFQLTDSYQ(tdsyq, oland,
										ql, ywh);

								List<ZTTGYQLR> zttqlr = msg.getData()
										.getGYQLR();
								zttqlr = packageXml.getZTTGYQLRs(zttqlr, qlrs,
										null, ql, null, oland, null, null);
								msg.getData().setGYQLR(zttqlr);

								DJTDJSLSQ sq = msg.getData().getDJSLSQ();
								sq = packageXml.getDJTDJSLSQ(sq, ywh, null, ql,
										xmxx, null, xmxx.getSLSJ(),
										oland, null, null);

								List<DJFDJSJ> sj = msg.getData().getDJSJ();
								sj = packageXml.getDJFDJSJ(oland, ywh,actinstID);
								msg.getData().setDJSJ(sj);

								List<DJFDJSF> sfList = msg.getData().getDJSF();
								sfList = packageXml.getDJSF(oland,ywh, this.getXMBH());
								msg.getData().setDJSF(sfList);

								List<DJFDJSH> sh = msg.getData().getDJSH();
								sh = packageXml.getDJFDJSH(oland, ywh,
										this.getXMBH(), actinstID);
								msg.getData().setDJSH(sh);

								List<DJFDJSZ> sz = packageXml.getDJFDJSZ(oland, ywh, this
										.getXMBH());
								msg.getData().setDJSZ(sz);

								List<DJFDJFZ> fz = packageXml.getDJFDJFZ(oland, ywh, this
										.getXMBH());
								msg.getData().setDJFZ(fz);
								List<DJFDJGD> gd = packageXml.getDJFDJGD(oland, ywh,this.getXMBH());
								msg.getData().setDJGD(gd);
								List<ZDK103> zdk = msg.getData().getZDK103();
								zdk = packageXml.getZDK103(zdk, null, oland,
										null);

								List<DJFDJSQR> djsqrs = msg.getData()
										.getDJSQR();
								djsqrs = packageXml.getDJSQRs(djsqrs, sqrs,
										oland.getYSDM(), ywh, oland.getBDCDYH());
								msg.getData().setDJSQR(djsqrs);
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

					if (null == result) {
						Map<String, String> xmlError = new HashMap<String, String>();
						xmlError.put("error", "连接SFTP失败,请检查服务器和前置机的连接是否正常");
						if (QLLX.JTTDSYQ.Value.equals(this.getQllx().Value)) {
							YwLogUtil
									.addSjsbResult(
											msgName,
											"",
											"连接SFTP失败,请检查服务器和前置机的连接是否正常",
											ConstValue.SF.NO.Value,
											ConstValue.RECCODE.TDSYQ_ZYDJ.Value,
											ProjectHelper
													.getpRroinstIDByActinstID(actinstID));
						} else if (QLLX.GYJSYDSHYQ_FWSYQ.Value.equals(this
								.getQllx().Value)) {
							YwLogUtil
									.addSjsbResult(
											msgName,
											"",
											"连接SFTP失败,请检查服务器和前置机的连接是否正常",
											ConstValue.SF.NO.Value,
											ConstValue.RECCODE.FDCQDZ_ZYDJ.Value,
											ProjectHelper
													.getpRroinstIDByActinstID(actinstID));
						} else {
							YwLogUtil
									.addSjsbResult(
											msgName,
											"",
											"连接SFTP失败,请检查服务器和前置机的连接是否正常",
											ConstValue.SF.NO.Value,
											ConstValue.RECCODE.JSYDSHYQ_ZYDJ.Value,
											ProjectHelper
													.getpRroinstIDByActinstID(actinstID));
						}
						return xmlError;
					}
					if (!"1".equals(result) && result.indexOf("success") == -1) { // xml本地校验不通过时
						Map<String, String> xmlError = new HashMap<String, String>();
						xmlError.put("error", result);
						return xmlError;
					}
					if (!StringUtils.isEmpty(result)
							&& result.indexOf("success") > -1
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
		List<BDCS_DJDY_GZ> djdys = getCommonDao().getDataList(
				BDCS_DJDY_GZ.class, xmbhFilter);
		if (djdys != null && djdys.size() > 0) {
			for (int idjdy = 0; idjdy < djdys.size(); idjdy++) {
				BDCS_DJDY_GZ djdy = djdys.get(idjdy);
				ConstValue.BDCDYLX dylx = ConstValue.BDCDYLX.initFrom(djdy
						.getBDCDYLX());
				ConstValue.DJDYLY dyly = ConstValue.DJDYLY.initFrom(djdy
						.getLY());
				RealUnit bdcdy = UnitTools.loadUnit(dylx, dyly,
						djdy.getBDCDYID());
				Rights bdcql = RightsTools.loadRightsByDJDYID(
						ConstValue.DJDYLY.GZ, getXMBH(), djdy.getDJDYID());
				SubRights bdcfsql = RightsTools.loadSubRightsByRightsID(
						ConstValue.DJDYLY.GZ, bdcql.getId());
				List<RightsHolder> bdcqlrs = RightsHolderTools
						.loadRightsHolders(ConstValue.DJDYLY.GZ,
								djdy.getDJDYID(), getXMBH());
				MessageExport msg = super.getShareMsgTools().GetMsg(bdcdy,
						bdcql, bdcfsql, bdcqlrs, bljc, xmxx);
				super.getShareMsgTools().SendMsg(msg, idjdy + 1,
						djdy.getBDCDYLX(), xmxx);
			}
		}
	}


}
