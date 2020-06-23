package com.supermap.realestate.registration.handlerImpl;

import com.supermap.realestate.registration.ViewClass.ProjectInfo;
import com.supermap.realestate.registration.ViewClass.UnitTree;
import com.supermap.realestate.registration.dataExchange.*;
import com.supermap.realestate.registration.dataExchange.dyq.QLFQLDYAQ;
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
import com.supermap.wisdombusiness.web.ResultMessage;
import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.util.StringUtils;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.File;
import java.text.MessageFormat;
import java.util.*;

/*
 1、抵押初始登记处理类（房地一体）
 */
/**
 * 
 * 抵押初始登记处理类（房地一体）
 * 
 * @ClassName: DYDJHandler_HouseAndLand
 * @author yuxuebin
 * @date 2016年09月01日 14:41:12
 */
public class BGDJ_DYHandler_HouseAndLand extends DJHandlerBase implements DJHandler {

	/**
	 * 构造函数
	 * 
	 * @param info
	 */
	public BGDJ_DYHandler_HouseAndLand(ProjectInfo info) {
		super(info);
	}

	/**
	 * 添加不动产单元
	 */
	@Override
	public boolean addBDCDY(String qlids) {
		boolean bsuccess = false;
		if(!IsCanAccept(qlids)){
			return bsuccess;
		}
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
					}
					getCommonDao().flush();
					//CopySQRFromXZQLR(id, gzqlid);
					CopySQRFromGZQLR(bdcdyh, gzqlid);					
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

	private boolean addbdcdy(String bdcdyid) {
		boolean bSuccess = false;
		CommonDao dao = this.getCommonDao();
		RealUnit _srcUnit = UnitTools.loadUnit(this.getBdcdylx(), DJDYLY.XZ, bdcdyid);
		BDCS_DJDY_GZ djdy = super.createDJDYfromXZ(bdcdyid);
		if (djdy == null) {
			djdy = createDJDYfromXZ(bdcdyid, _srcUnit);
		}
		if (djdy != null) {
			RealUnit unit = null;
			try {
				UnitTools.loadUnit(this.getBdcdylx(), DJDYLY.initFrom(djdy.getLY()), djdy.getBDCDYID());
			} catch (Exception e) {
			}
			// 生成权利信息
			BDCS_QL_GZ ql = super.createQL(djdy, unit);
			ql.setCZFS(CZFS.GTCZ.Value);

			String xzqhdm = ConfigHelper.getNameByValue("XZQHDM");
			if (!StringHelper.isEmpty(xzqhdm) && xzqhdm.startsWith("1301")) {
				ql.setDJYY("借款");
			}

			// 生成附属权利
			BDCS_FSQL_GZ fsql = super.createFSQL(djdy.getDJDYID());
			ql.setFSQLID(fsql.getId());
			fsql.setQLID(ql.getId());

			// 把附属权利里边的抵押人和抵押不动产类型写上
			String qllxarray = "('3','4','5','6','7','8','10','11','12','15','36')";
			String hql = "QLID IN(SELECT id FROM BDCS_QL_XZ WHERE  DJDYID='" + djdy.getDJDYID() + "' AND QLLX IN "
					+ qllxarray + ") ORDER BY SXH";
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

						String Sql = "";
						if (!StringHelper.isEmpty(zjhm)) {
							Sql = MessageFormat.format(" XMBH=''{0}'' AND SQRXM=''{1}'' AND ZJH=''{2}''", getXMBH(),
									qlr.getQLRMC(), zjhm);
						} else {
							Sql = MessageFormat.format(" XMBH=''{0}'' AND SQRXM=''{1}'' AND ZJH IS NULL", getXMBH(),
									qlr.getQLRMC());
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
						// 代理机构名称
						sqr.setDLJGMC(qlr.getDLJGMC());
						sqr.setDLRLXDH(qlr.getDLRLXDH());
						if (StringHelper.isEmpty(sqr.getSQRLX())) {
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
	 * 添加登记单元信息
	 * 
	 * @作者 海豹
	 * @创建时间 2015年7月10日上午11:47:23
	 * @param bdcdyid
	 * @param realUnit
	 * @return
	 */
	protected BDCS_DJDY_GZ createDJDYfromXZ(String bdcdyid, RealUnit realUnit) {
		BDCS_DJDY_GZ gzdjdy = new BDCS_DJDY_GZ();
		String gzdjdyid = getPrimaryKey();
		gzdjdy.setXMBH(this.getXMBH());
		gzdjdy.setDJDYID(gzdjdyid);
		gzdjdy.setBDCDYID(realUnit.getId());
		gzdjdy.setBDCDYLX(this.getBdcdylx().Value);
		gzdjdy.setBDCDYH(realUnit.getBDCDYH());
		gzdjdy.setLY(DJDYLY.XZ.Value);
		// gzdjdy.setDCXMID(bdcs_h_xzy.getDCXMID());

		// 设置预测户的项目编号
		realUnit.setXMBH(this.getXMBH());
		// getCommonDao().update(bdcs_h_xzy);
		return gzdjdy;
	}

	/**
	 * 写入登记薄
	 */
	@Override
	public boolean writeDJB() {
		if (super.isCForCFING()) {
			return false;
		}
		String strSqlXMBH = ProjectHelper.GetXMBHCondition(getXMBH());
		List<BDCS_DJDY_GZ> djdys = getCommonDao().getDataList(BDCS_DJDY_GZ.class, strSqlXMBH);
		List<String> zdbdcdyids = new ArrayList<String>();
		if (djdys != null && djdys.size() > 0) {
			for (int idjdy = 0; idjdy < djdys.size(); idjdy++) {
				BDCS_DJDY_GZ bdcs_djdy_gz = djdys.get(idjdy);
				if (bdcs_djdy_gz != null) {
					String djdyid = bdcs_djdy_gz.getDJDYID();
					StringBuilder builder = new StringBuilder();
					builder.append(" DJDYID='").append(djdyid).append("'");
					builder.append(" AND QLLX='").append(getQllx().Value).append("'");
					builder.append(" AND ").append(strSqlXMBH);
					String strSql = builder.toString();
					List<BDCS_QL_GZ> qls = getCommonDao().getDataList(BDCS_QL_GZ.class, strSql);
					String lyqlid = null;
					if (qls != null && qls.size() > 0) {
						lyqlid = qls.get(0).getLYQLID();
					}
					if (StringHelper.isEmpty(lyqlid)) {
						setErrMessage("未获取到对应来源权利ID，不能登簿！");
						return false;
					}
					String ywh_zd="";
					BDCS_QL_XZ ql_xz = getCommonDao().get(BDCS_QL_XZ.class, lyqlid);
					if (ql_xz != null) {
						ywh_zd = ql_xz.getYWH();
					}
					if(StringHelper.isEmpty(ywh_zd)){
						setErrMessage("未获取到对应来源业务号，不能登簿！");
						return false;
					}
					super.removeQLXXFromXZByQLID(lyqlid);

					super.CopyGZQLToXZAndLS(djdyid);
					super.CopyGZQLRToXZAndLS(djdyid);
					super.CopyGZQDZRToXZAndLS(djdyid);
					super.CopyGZZSToXZAndLS(djdyid);
					BDCDYLX dylx = ProjectHelper.GetBDCDYLX(getXMBH());
					List<BDCS_DJDY_XZ> zd_djdys = getCommonDao().getDataList(BDCS_DJDY_XZ.class,
							"BDCDYID='" + bdcs_djdy_gz.getBDCDYID() + "'");
					if (zd_djdys != null && zd_djdys.size() > 0) {
						// 单元存在不拷贝
					} else {
						super.CopyGZDJDYToXZAndLS(bdcs_djdy_gz.getId());
					}
					// 更新单元抵押状态
					SetDYDYZT(bdcs_djdy_gz.getBDCDYID(), dylx, "0");

					RealUnit _srcunit = UnitTools.loadUnit(dylx, DJDYLY.XZ, bdcs_djdy_gz.getBDCDYID());
					if (_srcunit != null) {
						if(_srcunit instanceof House){
							House house = (House) _srcunit;
							if(!zdbdcdyids.contains(house.getZDBDCDYID())){
								zdbdcdyids.add(house.getZDBDCDYID());
								updateZD(bdcs_djdy_gz.getBDCDYID(), bdcs_djdy_gz.getDJDYID(), ywh_zd);
							}
						}
					}
				}
			}
		}

		this.SetSFDB();
		getCommonDao().flush();
		super.DyLimit();
		super.alterCachedXMXX();
		return true;
	}

	public void updateZD(String bdcdyid_h, String djdyid_h, String ywh) {
		BDCDYLX dylx = ProjectHelper.GetBDCDYLX(getXMBH());
		RealUnit unit_h = UnitTools.loadUnit(dylx, DJDYLY.XZ, bdcdyid_h);
		if (unit_h == null) {
			return;
		}
		House h = (House) unit_h;
		String bdcdyid_zd = h.getZDBDCDYID();
		if (StringHelper.isEmpty(bdcdyid_zd)) {
			return;
		}

		RealUnit unit_zd = UnitTools.loadUnit(BDCDYLX.SHYQZD, DJDYLY.XZ, bdcdyid_zd);
		if (unit_zd == null) {
			return;
		}
		String djdyid_zd="";
		List<BDCS_DJDY_XZ> zd_djdys=getCommonDao().getDataList(BDCS_DJDY_XZ.class, "BDCDYID='"+bdcdyid_zd+"' AND BDCDYLX='02'");
		if(zd_djdys!=null&&zd_djdys.size()>0){
			djdyid_zd=zd_djdys.get(0).getDJDYID();
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
		List<Rights> list_ql_zd_old=RightsTools.loadRightsByCondition(DJDYLY.XZ, "QLLX='23' AND DJDYID='"+djdyid_zd+"' AND YWH='"+ywh+"'");
		Rights ql_zd_old=null;
		SubRights fsql_zd_old=null;
		if(list_ql_zd_old!=null&&list_ql_zd_old.size()>0){
			ql_zd_old=list_ql_zd_old.get(0);
			fsql_zd_old=RightsTools.loadSubRights(DJDYLY.XZ, ql_zd_old.getFSQLID());
			
		}
		List<Rights> list_ql_h_new=RightsTools.loadRightsByCondition(DJDYLY.GZ, "QLLX='23' AND DJDYID='"+djdyid_h+"' AND XMBH='"+super.getXMBH()+"'");
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
		getCommonDao().save(ql_zd_new);
		BDCS_QL_LS ql_zd_new_ls=new BDCS_QL_LS();
		try{
			PropertyUtils.copyProperties(ql_zd_new_ls, ql_zd_new);
		}catch(Exception e){
			
		}
		getCommonDao().save(ql_zd_new_ls);
		m_qlid.put(ql_zd_old.getId(), qlid_new);
		if(fsql_zd_old!=null){
			BDCS_FSQL_XZ fsql_zd_new=new BDCS_FSQL_XZ();
			try{
				PropertyUtils.copyProperties(fsql_zd_new, fsql_zd_old);
			}catch(Exception e){
				
			}
			fsql_zd_new.setId(fsqlid_new);
			fsql_zd_new.setQLID(qlid_new);
			getCommonDao().save(fsql_zd_new);
			BDCS_FSQL_LS fsql_zd_new_ls=new BDCS_FSQL_LS();
			try{
				PropertyUtils.copyProperties(fsql_zd_new_ls, fsql_zd_new);
			}catch(Exception e){
				
			}
			getCommonDao().save(fsql_zd_new_ls);
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
				getCommonDao().save(qlr_zd_new);
				BDCS_QLR_LS qlr_zd_new_ls=new BDCS_QLR_LS();
				try{
					PropertyUtils.copyProperties(qlr_zd_new_ls, qlr_zd_new);
				}catch(Exception e){

				}
				getCommonDao().save(qlr_zd_new_ls);
				m_qlrid.put(qlr_h_new.getId(), qlrid_zd_new);
				List<BDCS_QDZR_GZ> list_qdzr_h_new=getCommonDao().getDataList(BDCS_QDZR_GZ.class, "QLID='"+ql_h_new.getId()+"' AND XMBH='"+super.getXMBH()+"' AND QLRID='"+qlr_h_new.getId()+"'");
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
						getCommonDao().save(qlr_qdzr_zd_new);
						BDCS_QDZR_LS qdzr_zd_new_ls=new BDCS_QDZR_LS();
						try{
							PropertyUtils.copyProperties(qdzr_zd_new_ls, qlr_qdzr_zd_new);
						}catch(Exception e){

						}
						getCommonDao().save(qdzr_zd_new_ls);
					}
				}
			}
		}

		if(list_zsid_h!=null&&list_zsid_h.size()>0){
			for(String zsid_h_new:list_zsid_h){
				BDCS_ZS_XZ zs_zd_new=new BDCS_ZS_XZ();
				zs_zd_new.setXMBH(super.getXMBH());
				zs_zd_new.setId(m_zsid.get(zsid_h_new));
				getCommonDao().save(zs_zd_new);
				BDCS_ZS_LS zs_zd_new_ls=new BDCS_ZS_LS();
				try{
					PropertyUtils.copyProperties(zs_zd_new_ls, zs_zd_new);
				}catch(Exception e){

				}
				getCommonDao().save(zs_zd_new_ls);
			}
		}
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
		List<UnitTree> list = super.getUnitList();

		CommonDao baseCommonDao = this.getCommonDao();
		if (list != null && list.size() > 0) {
			BDCS_QL_GZ QLs = baseCommonDao.get(BDCS_QL_GZ.class, list.get(0).getQlid());
			BDCS_XMXX xmxx = baseCommonDao.get(BDCS_XMXX.class, QLs.getXMBH());
			if (xmxx != null && "1".equals(xmxx.getSFDB())) {
				for (UnitTree unitTree : list) {
					BDCS_QL_GZ QL = baseCommonDao.get(BDCS_QL_GZ.class, unitTree.getQlid());
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
		return super.getErrMessage();
	}

	/**
	 * 移除权利人
	 */
	@Override
	public void removeQLR(String qlid, String qlrid) {
		super.removeqlr(qlrid, qlid);
	}

	/**
	 * 根据工作权利ID和拷贝现状权利人到申请人，并将sqrid传入qlr_gz
	 * @Author：rq
	 * @param bdcdyh
	 * @param gzqlid
	 */
	private void CopySQRFromGZQLR(String bdcdyh, String gzqlid) {
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
				sqr.setFDDBR(qlr.getFDDBR());
				sqr.setFDDBRDH(qlr.getFDDBRDH());
				sqr.setFDDBRZJLX(qlr.getFDDBRZJLX());
				sqr.setDLRXM(qlr.getDLRXM());
				sqr.setDLRZJLX(qlr.getDLRZJLX());
				sqr.setDLRZJHM(qlr.getDLRZJHM());
				// 代理机构名称
				sqr.setDLJGMC(qlr.getDLJGMC());
				sqr.setDLRLXDH(qlr.getDLRLXDH());
				if (StringHelper.isEmpty(sqr.getSQRLX())) {
					sqr.setSQRLX("1");
				}
				getCommonDao().save(sqr);
				//权利人关联申请人ID
				qlr.setSQRID(sqr.getId());
				getCommonDao().update(qlr);
			}
		}
		//添加上一首抵押权的义务人——用于受理回执单的显示
		/*StringBuilder builderQL_XZ = new StringBuilder();
		builderQL_XZ.append("BDCDYH ='").append(bdcdyh).append("' AND QLLX ='23'");
		List<BDCS_QL_XZ> ql_xz = getCommonDao().getDataList(BDCS_QL_XZ.class, builderQL_XZ.toString());
		if (ql_xz != null && ql_xz.size() > 0) {
			StringBuilder builderXMXX = new StringBuilder();
			builderXMXX.append("PROJECT_ID = '").append(ql_xz.get(0).getYWH()).append("'");
			List<BDCS_XMXX> xmxx = getCommonDao().getDataList(BDCS_XMXX.class, builderXMXX.toString());
			if (xmxx != null && xmxx.size() > 0) {
				String xmbh_xz = StringHelper.isEmpty(xmxx.get(0).getId()) ? null : xmxx.get(0).getId();
				if (xmbh_xz != null) {
					String sql = MessageFormat.format(" XMBH=''{0}'' AND SQRLB=''2''", xmbh_xz);
					List<BDCS_SQR> sqrlist = getCommonDao().getDataList(BDCS_SQR.class, sql);
					if (sqrlist == null && sqrlist.size() <= 0) {
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
		}*/
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
			boolean isZipFile = false;
			if (djdys.size() >= 10) {
				isZipFile = true;
				String folderPath = GetProperties.getConstValueByKey("xmlPath") + "\\" + xmxx.getPROJECT_ID() + "_"
						+ bljc;
				super.getShareMsgTools().deleteFolder(folderPath);
			}
			for (int idjdy = 0; idjdy < djdys.size(); idjdy++) {
				BDCS_DJDY_GZ djdy = djdys.get(idjdy);
				ConstValue.BDCDYLX dylx = ConstValue.BDCDYLX.initFrom(djdy.getBDCDYLX());
				ConstValue.DJDYLY dyly = ConstValue.DJDYLY.initFrom(djdy.getLY());
				RealUnit bdcdy = UnitTools.loadUnit(dylx, dyly, djdy.getBDCDYID());
				Rights bdcql = RightsTools.loadRightsByDJDYID(ConstValue.DJDYLY.GZ, getXMBH(), djdy.getDJDYID());
				SubRights bdcfsql = RightsTools.loadSubRightsByRightsID(ConstValue.DJDYLY.GZ, bdcql.getId());
				List<RightsHolder> bdcqlrs = RightsHolderTools.loadRightsHolders(ConstValue.DJDYLY.GZ, djdy.getDJDYID(),
						getXMBH());
				MessageExport msg = super.getShareMsgTools().GetMsg(bdcdy, bdcql, bdcfsql, bdcqlrs, bljc, xmxx);
				if (isZipFile) {
					String folderPath = super.getShareMsgTools().createXMLInFile(xmxx, msg, idjdy + 1, bljc);
					if (idjdy == djdys.size() - 1) {// 文件都生成到文件夹以后再压缩上传
						super.getShareMsgTools().SendMsg(folderPath, xmxx, bljc, djdy.getBDCDYLX());
					}
				} else {
					super.getShareMsgTools().SendMsg(msg, idjdy + 1, djdy.getBDCDYLX(), xmxx);
				}
			}
		}
	}

	/************************ 内部方法 *********************************/

	protected String getDYBDCLXfromBDCDYLX(BDCDYLX bdcdylx) {
		String dybdclx = "";
		if (bdcdylx.equals(BDCDYLX.SHYQZD) || bdcdylx.equals(BDCDYLX.SYQZD)) {
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
				String ywh = cyear + ConfigHelper.getNameByValue("XZQHDM") + super.getDjlx().Value
						+ super.getQllx().Value + super.getPrjNumber().substring(1, 6);
				BDCS_XMXX xmxx = dao.get(BDCS_XMXX.class, super.getXMBH());
				String result = "";
				for (int i = 0; i < djdys.size(); i++) {
					BDCS_DJDY_GZ djdy = djdys.get(i);
					
					String condition = MessageFormat.format("DJDYID=''{0}'' AND QLLX=''23'' ", djdy.getDJDYID());
					List<Rights> _rightss = RightsTools.loadRightsByCondition(DJDYLY.GZ, condition);
					BDCS_QL_GZ ql = null;
					List<BDCS_QLR_GZ> qlrs = new ArrayList<BDCS_QLR_GZ>();
					for (Rights rights : _rightss) {
						ql = (BDCS_QL_GZ) rights;
						qlrs = super.getQLRs(ql.getId());
						if (qlrs != null) {
							break;
						}
					}
					List<BDCS_SQR> sqrs = new ArrayList<BDCS_SQR>();
					ProjectServiceImpl serviceImpl = SuperSpringContext.getContext().getBean(ProjectServiceImpl.class);
					sqrs = serviceImpl.getSQRList(super.getXMBH());
					
					BDCS_FSQL_GZ fsql = new BDCS_FSQL_GZ();
					if (ql != null && !StringUtils.isEmpty(ql.getFSQLID())) {
						fsql = dao.get(BDCS_FSQL_GZ.class, ql.getFSQLID());
					}

					Message msg = exchangeFactory.createMessageByDYQ();
					msg.getHead().setRecType("9000101");

					if (BDCDYLX.SHYQZD.Value.equals(djdy.getBDCDYLX())
							|| QLLX.ZJDSYQ.Value.equals(this.getQllx().Value)) { // 使用权宗地、宅基地使用权
						try {
							BDCS_SHYQZD_XZ zd = dao.get(BDCS_SHYQZD_XZ.class, djdy.getBDCDYID());
							super.fillHead(msg, i, ywh, zd.getBDCDYH(), zd.getQXDM(),ql.getLYQLID());
							msg.getHead().setParcelID(StringHelper.formatObject(zd.getZDDM()));
							msg.getHead().setEstateNum(StringHelper.formatObject(zd.getBDCDYH()));
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
								sq = packageXml.getDJTDJSLSQ(sq, ywh, zd, ql, xmxx, null, super.getCreateTime(), null,
										null, null);

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
					
					if (BDCDYLX.H.Value.equals(djdy.getBDCDYLX())) { // 房屋
						try {
							BDCS_H_XZ h  = dao.get(BDCS_H_XZ.class, djdy.getBDCDYID());
							if (h != null) {
								BDCS_SHYQZD_XZ zd = dao.get(BDCS_SHYQZD_XZ.class, h.getZDBDCDYID());
								h.setZDDM(zd.getZDDM());
							}
							super.fillHead(msg, i, ywh, h.getBDCDYH(), h.getQXDM(),ql.getLYQLID());
							msg.getHead().setParcelID(StringHelper.formatObject(h.getZDDM()));
							msg.getHead().setEstateNum(StringHelper.formatObject(h.getBDCDYH()));
//							msg.getHead().setPreEstateNum(StringHelper.formatObject(h.getBDCDYH()));
							if (h != null && !StringUtils.isEmpty(h.getQXDM())) {
								msg.getHead().setAreaCode(h.getQXDM());
							}
							if (djdy != null) {

								QLFQLDYAQ dyaq = msg.getData().getQLFQLDYAQ();
								dyaq = packageXml.getQLFQLDYAQ(dyaq, null, ql, fsql, ywh, h);

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

								List<DJFDJFZ> fz = packageXml.getDJFDJFZ(h, ywh, this.getXMBH());
								msg.getData().setDJFZ(fz);
								List<DJFDJGD> gd = packageXml.getDJFDJGD(h, ywh,this.getXMBH());
								msg.getData().setDJGD(gd);

								List<ZTTGYQLR> zttqlr = msg.getData().getGYQLR();
								zttqlr = packageXml.getZTTGYQLRs(zttqlr, qlrs, null, ql, h, null, null, null);
								msg.getData().setGYQLR(zttqlr);

								DJTDJSLSQ sq = msg.getData().getDJSLSQ();
								sq = packageXml.getDJTDJSLSQ(sq, ywh, null, ql, xmxx, h, super.getCreateTime(), null,
										null, null);

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
					
					if (BDCDYLX.YCH.Value.equals(djdy.getBDCDYLX())) { // 在建工程
						try {
							BDCS_H_XZY xzy = dao.get(BDCS_H_XZY.class, djdy.getBDCDYID());
							if (xzy != null) {
								BDCS_SHYQZD_XZ zd = dao.get(BDCS_SHYQZD_XZ.class, xzy.getZDBDCDYID());
								xzy.setZDDM(zd.getZDDM());
							}
							super.fillHead(msg, i, ywh, xzy.getBDCDYH(), xzy.getQXDM(),ql.getLYQLID());
							msg.getHead().setParcelID(StringHelper.formatObject(xzy.getZDDM()));
							msg.getHead().setEstateNum(StringHelper.formatObject(xzy.getBDCDYH()));
//							msg.getHead().setPreEstateNum(StringHelper.formatObject(xzy.getBDCDYH()));
							if (xzy != null && !StringUtils.isEmpty(xzy.getQXDM())) {
								msg.getHead().setAreaCode(xzy.getQXDM());
							}
							if (djdy != null) {

								QLFQLDYAQ dyaq = msg.getData().getQLFQLDYAQ();
								dyaq = packageXml.getQLFQLDYAQ(dyaq, null, ql, fsql, ywh, xzy);

								List<DJFDJSJ> sj = msg.getData().getDJSJ();
								sj = packageXml.getDJFDJSJ(xzy, ywh,actinstID);
								msg.getData().setDJSJ(sj);

								List<DJFDJSF> sfList = msg.getData().getDJSF();
								sfList = packageXml.getDJSF(xzy,ywh, this.getXMBH());
								msg.getData().setDJSF(sfList);

								List<DJFDJSH> sh = msg.getData().getDJSH();
								sh = packageXml.getDJFDJSH(xzy, ywh, this.getXMBH(), actinstID);
								msg.getData().setDJSH(sh);

								List<DJFDJSZ> sz = packageXml.getDJFDJSZ(xzy,ywh, this.getXMBH());
								msg.getData().setDJSZ(sz);

								List<DJFDJFZ> fz = packageXml.getDJFDJFZ(xzy,ywh, this.getXMBH());
								msg.getData().setDJFZ(fz);
								List<DJFDJGD> gd = packageXml.getDJFDJGD(xzy, ywh,this.getXMBH());
								msg.getData().setDJGD(gd);

								List<ZTTGYQLR> zttqlr = msg.getData().getGYQLR();
								zttqlr = packageXml.getZTTGYQLRs(zttqlr, qlrs, null, ql, xzy, null, null, null);
								msg.getData().setGYQLR(zttqlr);

								DJTDJSLSQ sq = msg.getData().getDJSLSQ();
								sq = packageXml.getDJTDJSLSQ(sq, ywh, null, ql, xmxx, xzy, super.getCreateTime(), null,
										null, null);

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
					mashaller.marshal(msg, new File(path + "Biz" + msg.getHead().getBizMsgID() + ".xml"));
					result = uploadFile(path + "Biz" + msg.getHead().getBizMsgID() + ".xml",
							ConstValue.RECCODE.DIYQ_ZXDJ.Value, actinstID, djdy.getDJDYID(), ql.getId());
					names.put(djdy.getDJDYID(), msg.getHead().getBizMsgID() + ".xml");

					if (null == result) {
						Map<String, String> xmlError = new HashMap<String, String>();
						xmlError.put("error", "连接SFTP失败,请检查服务器和前置机的连接是否正常");
						YwLogUtil.addSjsbResult("Biz" + msg.getHead().getBizMsgID() + ".xml", "",
								"连接SFTP失败,请检查服务器和前置机的连接是否正常", ConstValue.SF.NO.Value,
								ConstValue.RECCODE.DIYQ_ZXDJ.Value, ProjectHelper.getpRroinstIDByActinstID(actinstID));
						return xmlError;
					}
					if (!"1".equals(result) && result.indexOf("success") == -1) { // xml本地校验不通过时
						Map<String, String> xmlError = new HashMap<String, String>();
						xmlError.put("error", result);
						return xmlError;
					}
					if (!StringUtils.isEmpty(result) && result.indexOf("success") > -1
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

	private boolean IsCanAccept(String qlids){	
		String qlid[] = qlids.split(",");
		for (String id : qlid) {
			Rights h_right = RightsTools.loadRights(DJDYLY.XZ, id);
			String h_bdcqzh = h_right.getBDCQZH();
			if(StringHelper.isEmpty(h_bdcqzh)){
				return false;
			}
			List<BDCS_DJDY_XZ> dy = getCommonDao().getDataList(BDCS_DJDY_XZ.class, "DJDYID='"+h_right.getDJDYID()+"'");
			if(dy!=null&&dy.size()>0){
				BDCS_DJDY_XZ djdy = dy.get(0);
				RealUnit _srcunit = UnitTools.loadUnit(getBdcdylx(), DJDYLY.XZ, djdy.getBDCDYID());
				if (_srcunit != null) {
					LandAttach _landAttach = (LandAttach) _srcunit;
					List<BDCS_DJDY_XZ> zddy = getCommonDao().getDataList(BDCS_DJDY_XZ.class, "BDCDYID='"+_landAttach.getZDBDCDYID()+"'");
					if(zddy!=null&&zddy.size()>0){
						BDCS_DJDY_XZ zd = zddy.get(0);
						List<Rights> zdqls = RightsTools.loadRightsByCondition(DJDYLY.XZ, "DJDYID='"+zd.getDJDYID()+"' AND QLLX='23'");
						if(zdqls!=null&&zdqls.size()>0){
							return true;
						}else{
							super.setErrMessage("未找到关联宗地的抵押权人！");
							return false;							
						}
					}else{
						super.setErrMessage("未找到关联宗地");
						return false;
					}
				}else{
					super.setErrMessage("未找到户单元");
					return false;
				}
			}
		}
		return true;
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
			bdcs_ql_gz.setBDCQZH("");
			bdcs_ql_gz.setDJYY("");
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
