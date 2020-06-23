/**
 * 
 */
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
import org.apache.poi.hssf.record.PageBreakRecord.Break;
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
import com.supermap.realestate.registration.dataExchange.exchangeFactory;
import com.supermap.realestate.registration.dataExchange.cfdj.QLFQLCFDJ;
import com.supermap.realestate.registration.dataExchange.utils.packageXml;
import com.supermap.realestate.registration.handler.DJHandler;
import com.supermap.realestate.registration.model.BDCS_DJDY_GZ;
import com.supermap.realestate.registration.model.BDCS_DJDY_LS;
import com.supermap.realestate.registration.model.BDCS_DJDY_XZ;
import com.supermap.realestate.registration.model.BDCS_FSQL_GZ;
import com.supermap.realestate.registration.model.BDCS_FSQL_LS;
import com.supermap.realestate.registration.model.BDCS_FSQL_XZ;
import com.supermap.realestate.registration.model.BDCS_H_XZ;
import com.supermap.realestate.registration.model.BDCS_QDZR_GZ;
import com.supermap.realestate.registration.model.BDCS_QDZR_LS;
import com.supermap.realestate.registration.model.BDCS_QDZR_XZ;
import com.supermap.realestate.registration.model.BDCS_QLR_LS;
import com.supermap.realestate.registration.model.BDCS_QLR_XZ;
import com.supermap.realestate.registration.model.BDCS_QL_GZ;
import com.supermap.realestate.registration.model.BDCS_QL_LS;
import com.supermap.realestate.registration.model.BDCS_QL_XZ;
import com.supermap.realestate.registration.model.BDCS_SHYQZD_XZ;
import com.supermap.realestate.registration.model.BDCS_SQR;
import com.supermap.realestate.registration.model.BDCS_XMXX;
import com.supermap.realestate.registration.model.BDCS_ZS_LS;
import com.supermap.realestate.registration.model.BDCS_ZS_XZ;
import com.supermap.realestate.registration.model.interfaces.House;
import com.supermap.realestate.registration.model.interfaces.RealUnit;
import com.supermap.realestate.registration.model.interfaces.RegisterUnit;
import com.supermap.realestate.registration.model.interfaces.Rights;
import com.supermap.realestate.registration.model.interfaces.RightsHolder;
import com.supermap.realestate.registration.model.interfaces.SubRights;
import com.supermap.realestate.registration.model.xmlExportmodel.MessageExport;
import com.supermap.realestate.registration.service.impl.ProjectServiceImpl;
import com.supermap.realestate.registration.tools.RightsHolderTools;
import com.supermap.realestate.registration.tools.RightsTools;
import com.supermap.realestate.registration.tools.UnitTools;
import com.supermap.realestate.registration.util.ConfigHelper;
import com.supermap.realestate.registration.util.ConstValue;
import com.supermap.realestate.registration.util.ConstValue.BDCDYLX;
import com.supermap.realestate.registration.util.ConstValue.CFLX;
import com.supermap.realestate.registration.util.ConstValue.DJDYLY;
import com.supermap.realestate.registration.util.Global;
import com.supermap.realestate.registration.util.ProjectHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.realestate.registration.util.YwLogUtil;
import com.supermap.wisdombusiness.core.SuperHelper;
import com.supermap.wisdombusiness.core.SuperSpringContext;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import com.supermap.wisdombusiness.web.ResultMessage;


/**
 * 房地一体（自建房）查封登记处理类
 * @ClassName: CFDJHandler_HouseAndLand
 * @author liangc
 * @date 2017年11月10日 14:55:50
 */
public class CFDJHandler_HouseAndLand extends DJHandlerBase implements DJHandler {

	public CFDJHandler_HouseAndLand(ProjectInfo info) {
		super(info);
	}

	@Override
	public boolean addBDCDY(String bdcdyid) {
		if (!StringHelper.isEmpty(bdcdyid)) {
			String ids[] = bdcdyid.split(",");
			for (String id : ids) {
				if (StringHelper.isEmpty(id)) {
					continue;
				}
				// 循环添加单元
				ResultMessage msg = this.addbdcdy(id);
				if (msg.getSuccess().equals("false")) {
					super.setErrMessage(msg.getMsg());
				}
			}
		}
		this.getCommonDao().flush();
		return true;
	}

	/**
	 * 添加单元
	 * @author diaoliwei
	 * @date 2015-8-4
	 * @param bdcdyid
	 * @return
	 */
	private ResultMessage addbdcdy(String bdcdyid) {
		ResultMessage msg = new ResultMessage();
		CommonDao dao = this.getCommonDao();
		if (ValidateDup(dao, bdcdyid)) {// 重复的插入 忽略掉
			msg.setSuccess("false");
			return msg;
		}
		BDCS_DJDY_GZ djdy = super.createDJDYfromXZ(bdcdyid);
		if (djdy != null) {
			RealUnit unit = null;
			try {
//				UnitTools.loadUnit(this.getBdcdylx(), DJDYLY.initFrom(djdy.getLY()), djdy.getBDCDYID());
				BDCDYLX m_bdcdylx = BDCDYLX.initFrom(djdy.getBDCDYLX());
				UnitTools.loadUnit(m_bdcdylx, DJDYLY.initFrom(djdy.getLY()), djdy.getBDCDYID());
			} catch (Exception e) {
			}
			// 生成权利信息
			BDCS_QL_GZ ql = super.createQL(djdy, unit);
			// 生成附属权利
			BDCS_FSQL_GZ fsql = super.createFSQL(djdy.getDJDYID());
			fsql.setCFWJ(ConfigHelper.getNameByValue("DEFAULTCFWJ"));
			fsql.setCFFW(ConfigHelper.getNameByValue("DEFAULTCFFW"));
			ql.setFSQLID(fsql.getId());
			fsql.setQLID(ql.getId());

			// 计算轮候顺序，判断是查封还是轮候查封
			String djdyid = djdy.getDJDYID();
			// 判断是否存在查封信息
			String sqlSeal = MessageFormat.format(" from BDCK.BDCS_QL_XZ where djdyid=''{0}'' and djlx=''800'' and qllx=''99''", djdyid);
			long sealcount = dao.getCountByFullSql(sqlSeal);
			int lhsx = 0;
			fsql.setCFLX(CFLX.CF.Value);
			BDCS_XMXX xmxx=dao.get(BDCS_XMXX.class, super.getXMBH());
			if(xmxx!=null){
				fsql.setCFSJ(xmxx.getSLSJ());
			}
			// 判断现状中是否存在查封信息，如果存在，取最大的轮候顺序
			if (sealcount > 0) {
				// 先设置为个数加1，放置两个都为空的情况
				int cxz = (int) sealcount;
				fsql.setCFLX(CFLX.LHCF.Value);
				String sqlXZ = MessageFormat.format(
						"SELECT MAX(LHSX) ZDXH FROM BDCK.BDCS_FSQL_XZ A LEFT JOIN BDCK.BDCS_QL_XZ B ON A.QLID=B.QLID WHERE  B.DJDYID=''{0}'' AND B.DJLX='800' AND B.QLLX='99'",
						djdyid);
				@SuppressWarnings("rawtypes")
				List<Map> mpXZlist = dao.getDataListByFullSql(sqlXZ);
				if (mpXZlist != null && mpXZlist.size() > 0) {
					@SuppressWarnings("rawtypes")
					Map mpxz = mpXZlist.get(0);
					if (mpxz != null && mpxz.containsKey("ZDXH")) {
						String cstrxz = StringHelper.formatObject(mpxz.get("ZDXH"));
						try {
							if (!StringHelper.isEmpty(cstrxz)) {
								lhsx = Integer.parseInt(cstrxz) + 1;
								
							}
						} catch (Exception ee) {
						}
					}
				}
				lhsx = Math.max(lhsx, cxz);
			}
			// 判断工作层中是否存在未登簿的关于该登记单元的其他查封信息
			String sqlGZ = MessageFormat
					.format("SELECT MAX(LHSX) ZDXH FROM BDCK.BDCS_FSQL_GZ A LEFT JOIN BDCK.BDCS_QL_GZ B ON A.QLID=B.QLID LEFT JOIN BDCK.BDCS_DJDY_GZ C ON C.DJDYID=B.DJDYID LEFT JOIN BDCK.BDCS_XMXX D ON C.XMBH=D.XMBH WHERE D.SFDB=0 AND   B.DJDYID=''{0}'' AND B.DJLX='800' AND B.QLLX='99'",
							djdyid);
			@SuppressWarnings("rawtypes")
			List<Map> mpGZlist = dao.getDataListByFullSql(sqlGZ);
			if (mpGZlist != null && mpGZlist.size() > 0) {
				int cgz = 0;

				@SuppressWarnings("rawtypes")
				Map mpgz = mpGZlist.get(0);
				if (mpgz != null && mpgz.containsKey("ZDXH")) {
					String cstrgz = StringHelper.formatObject(mpgz.get("ZDXH"));
					try {
						if (!StringHelper.isEmpty(cstrgz)) {
							cgz = Integer.parseInt(cstrgz);
							lhsx = Math.max(cgz + 1, lhsx);
							fsql.setCFLX(CFLX.LHCF.Value);
						}
					} catch (Exception ee) {

					}
				}
				lhsx = Math.max(lhsx, cgz);
			}
			// 设置轮候顺序
			fsql.setLHSX(lhsx);



			// 保存
			dao.save(djdy);
			dao.save(ql);
			dao.save(fsql);
			//添加宗地单元
			/**if(!"02".equals(djdy.getBDCDYLX())) {
				BDCS_H_XZ h_XZ = getCommonDao().get(BDCS_H_XZ.class, bdcdyid);
				if(h_XZ!=null&&!StringHelper.isEmpty(h_XZ.getZDBDCDYID())) {
					addBDCDY(h_XZ.getZDBDCDYID());
				}
				
			}**/
		}
		msg.setSuccess("true");
		return msg;
	}

	// 验证是否重复 重复返回TRUE否则返回FALSE
	private boolean ValidateDup(CommonDao dao, String bdcdyid) {
		String hql = "BDCDYID='" + bdcdyid + "' AND XMBH='" + super.getXMBH() + "'";// 通过不动产单元ID和项目编号判断是否重复

		List<BDCS_DJDY_GZ> list = dao.getDataList(BDCS_DJDY_GZ.class, hql);
		if (list != null && list.size() > 0) {
			return true;
		}
		return false;
	}

	@Override
	public boolean writeDJB() {
		String strSqlXMBH = ProjectHelper.GetXMBHCondition(getXMBH());
		List<BDCS_DJDY_GZ> djdys = getCommonDao().getDataList(BDCS_DJDY_GZ.class, strSqlXMBH);
		if (djdys != null && djdys.size() > 0) {
			for (int idjdy = 0; idjdy < djdys.size(); idjdy++) {
				BDCS_DJDY_GZ bdcs_djdy_gz = djdys.get(idjdy);
				if (bdcs_djdy_gz != null) {
					String djdyid = bdcs_djdy_gz.getDJDYID();
					String bdcdyid = bdcs_djdy_gz.getBDCDYID();
					super.CopyGZQLToXZAndLS(djdyid);
					super.CopyGZQLRToXZAndLS(djdyid);
					super.CopyGZQDZRToXZAndLS(djdyid);
					super.CopyGZZSToXZAndLS(djdyid);
					updateZD(bdcdyid,djdyid);
					// 暂停所有包含查封单元的在办项目
					this.SetXMCFZT(djdyid, "01");
				}
			}
		}
		this.SetSFDB();
		getCommonDao().flush();
		return true;
	}

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
			//删除土地单元
		/**	if(!"02".equals(djdy.getBDCDYLX())) {
				BDCS_H_XZ h_XZ = getCommonDao().get(BDCS_H_XZ.class, bdcdyid);
				if(h_XZ!=null&&!StringHelper.isEmpty(h_XZ.getZDBDCDYID())) {
					removeBDCDY(h_XZ.getZDBDCDYID());
				}
			}**/
		}
		baseCommonDao.flush();
		bsuccess = true;
		return bsuccess;
	}

	@Override
	public List<UnitTree> getUnitTree() {
		return super.getUnitList();
	}

	@Override
	public void addQLRbySQRArray(String qlid, Object[] sqrids) {

	}

	@Override
	public void removeQLR(String qlid, String qlrid) {

	}

	@Override
	public String getError() {
		return null;
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
		List<Rights> list_ql_h_new=RightsTools.loadRightsByCondition(DJDYLY.GZ, "QLLX IN ('99') AND DJLX='800' AND DJDYID='"+djdyid_h+"' AND XMBH='"+super.getXMBH()+"'");
		Rights ql_h_new=null;
		SubRights fsql_h_new = null;
		if(list_ql_h_new!=null&&list_ql_h_new.size()>0){
			ql_h_new=list_ql_h_new.get(0);
			if(!StringHelper.isEmpty(list_ql_h_new.get(0).getFSQLID())){
				fsql_h_new = RightsTools.loadSubRights(DJDYLY.GZ, list_ql_h_new.get(0).getFSQLID());
			}
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
			ql_zd_new.setQLLX(ql_h_new.getQLLX());
			ql_zd_new.setBDCQZH("");
			ql_zd_new.setZSBH("");
			//RightsTools.deleteRightsAll(DJDYLY.XZ, ql_zd_old.getId());
		}
		dao.save(ql_zd_new);
		BDCS_QL_LS ql_zd_new_ls=new BDCS_QL_LS();
		try{
			PropertyUtils.copyProperties(ql_zd_new_ls, ql_zd_new);
		}catch(Exception e){
			
		}
		dao.save(ql_zd_new_ls);
		m_qlid.put(ql_zd_old.getId(), qlid_new);
		if(fsql_h_new!=null){
			BDCS_FSQL_XZ fsql_zd_new=new BDCS_FSQL_XZ();
			try{
				PropertyUtils.copyProperties(fsql_zd_new, fsql_h_new);
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
				List<RightsHolder> bdcqlrs = null;
				MessageExport msg = super.getShareMsgTools().GetMsg(bdcdy, bdcql, bdcfsql, bdcqlrs, bljc, xmxx);
				super.getShareMsgTools().SendMsg(msg, idjdy + 1, djdy.getBDCDYLX(), xmxx);
			}
		}
	}

	@Override
	public Map<String, String> exportXML(String path ,String actinstID) {
		Marshaller mashaller;
		Map<String, String> names = new HashMap<String,String>();
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
				Message msg = exchangeFactory.createMessageByCFDJ();
				String qlid = "";
				for (int i = 0; i < djdys.size(); i++) {
					BDCS_DJDY_GZ djdy = djdys.get(i);
				
					//房屋
					if (djdy != null&&!"02".equals(djdy.getBDCDYLX())) {
						BDCS_QL_GZ ql = super.getQL(djdy.getDJDYID());
						List<BDCS_SQR> sqrs = new ArrayList<BDCS_SQR>();
						ProjectServiceImpl serviceImpl = SuperSpringContext.getContext().getBean(ProjectServiceImpl.class);
						sqrs = serviceImpl.getSQRList(super.getXMBH());
				 
						BDCS_FSQL_GZ fsql = null;
						if (ql != null && !StringUtils.isEmpty(ql.getFSQLID())) {
							fsql = dao.get(BDCS_FSQL_GZ.class, ql.getFSQLID());
						}
						if (ql != null && !StringUtils.isEmpty(ql.getId())) {
							qlid = ql.getId();
						}
						BDCS_SHYQZD_XZ zd = null;
						BDCS_H_XZ h = dao.get(BDCS_H_XZ.class, djdy.getBDCDYID());
						if(h != null){
				//			BDCS_SHYQZD_XZ zd = dao.get(BDCS_SHYQZD_XZ.class, h.getZDBDCDYID());
						 zd = dao.get(BDCS_SHYQZD_XZ.class, h.getZDBDCDYID());
							if(zd != null){
								h.setZDDM(zd.getZDDM());
							}
						}
						super.fillHead(msg, i, ywh,h.getBDCDYH(),h.getQXDM(),ql.getLYQLID());
						msg.getHead().setParcelID(StringHelper.formatObject(h.getZDDM()));
						msg.getHead().setEstateNum(StringHelper.formatObject(h.getBDCDYH()));
						msg.getHead().setRecType("8000101");
//						msg.getHead().setPreEstateNum(StringHelper.formatObject(h.getBDCDYH()));
						if(h != null && !StringUtils.isEmpty(h.getQXDM())){
							msg.getHead().setAreaCode(h.getQXDM());
						}
						try {
							QLFQLCFDJ cfdj = msg.getData().getQLFQLCFDJ();
							cfdj = packageXml.getQLFQLCFDJ(cfdj,null, h, ql, fsql, ywh,null);
							
							DJTDJSLSQ sq = msg.getData().getDJSLSQ();
							sq = packageXml.getDJTDJSLSQ(sq, ywh, null, ql, xmxx, h, xmxx.getSLSJ(), null, null, null);
							
							List<DJFDJSJ> sj = msg.getData().getDJSJ();
							sj = packageXml.getDJFDJSJ(h, ywh,actinstID);
							msg.getData().setDJSJ(sj);

							List<DJFDJSF> sfList = msg.getData().getDJSF();
							sfList = packageXml.getDJSF(h,ywh,this.getXMBH());
							msg.getData().setDJSF(sfList);
							
							List<DJFDJSH> sh = msg.getData().getDJSH();
							sh = packageXml.getDJFDJSH(h, ywh, this.getXMBH(), actinstID);
							msg.getData().setDJSH(sh);
							
							List<DJFDJSZ> sz = packageXml.getDJFDJSZ(h, ywh, this.getXMBH());
							msg.getData().setDJSZ(sz);		
							
							List<DJFDJFZ> fz = packageXml.getDJFDJFZ(h,ywh,this.getXMBH());
							msg.getData().setDJFZ(fz);	
							List<DJFDJGD> gd = packageXml.getDJFDJGD(h,ywh,this.getXMBH());
							msg.getData().setDJGD(gd);	
							List<DJFDJSQR> djsqrs = msg.getData().getDJSQR();
							//查封现在的不填写申请人，为了和接入规范一致，先在这里拼凑出申请人。
							DJFDJSQR djsqr = new DJFDJSQR();
							djsqr.setYsdm("2004020000");
							djsqr.setQlrmc(cfdj == null ? "无" :StringHelper.formatDefaultValue(cfdj.getCfjg()));
							djsqr.setYwh(ywh);
//							djsqr.setQxdm(ConfigHelper.getNameByValue("XZQHDM"));
							djsqrs.add(djsqr);
							djsqrs = packageXml.getDJSQRs(djsqrs, sqrs, h.getYSDM(), ywh, h.getBDCDYH());
							msg.getData().setDJSQR(djsqrs);
							
							FJF100 fj = msg.getData().getFJF100();
							fj = packageXml.getFJF(fj);
						} catch (Exception e) {
							e.printStackTrace();
						}
					//宗地
					}else {
						BDCS_QL_LS ql = super.getLSQL(djdy.getDJDYID());
						List<BDCS_SQR> sqrs = new ArrayList<BDCS_SQR>();
						ProjectServiceImpl serviceImpl = SuperSpringContext.getContext().getBean(ProjectServiceImpl.class);
						sqrs = serviceImpl.getSQRList(super.getXMBH());
				 
						BDCS_FSQL_LS fsql = null;
						if (ql != null && !StringUtils.isEmpty(ql.getFSQLID())) {
							fsql = dao.get(BDCS_FSQL_LS.class, ql.getFSQLID());
						}
						if (ql != null && !StringUtils.isEmpty(ql.getId())) {
							qlid = ql.getId();
						}
						BDCS_SHYQZD_XZ zd = dao.get(BDCS_SHYQZD_XZ.class, djdy.getBDCDYID());
						if(zd==null) {
							continue;
						}
						super.fillHead(msg, i, ywh,zd.getBDCDYH(),zd.getQXDM(),ql.getLYQLID());
						msg.getHead().setParcelID(StringHelper.formatObject(zd.getZDDM()));
						msg.getHead().setEstateNum(StringHelper.formatObject(zd.getBDCDYH()));
						msg.getHead().setRecType("8000101");
//						msg.getHead().setPreEstateNum(StringHelper.formatObject(h.getBDCDYH()));
						try {
							QLFQLCFDJ cfdj = msg.getData().getQLFQLCFDJ();
							cfdj = packageXml.getQLFQLCFDJ(cfdj,zd, null, ql, fsql, ywh,null);
							
							DJTDJSLSQ sq = msg.getData().getDJSLSQ();
							sq = packageXml.getDJTDJSLSQ(sq, ywh, zd, ql, xmxx, null, xmxx.getSLSJ(), null, null, null);
							
							List<DJFDJSJ> sj = msg.getData().getDJSJ();
							sj = packageXml.getDJFDJSJ(zd, ywh,actinstID);
							msg.getData().setDJSJ(sj);

							List<DJFDJSF> sfList = msg.getData().getDJSF();
							sfList = packageXml.getDJSF(zd, ywh,this.getXMBH());
							msg.getData().setDJSF(sfList);
							
							List<DJFDJSH> sh = msg.getData().getDJSH();
							sh = packageXml.getDJFDJSH(zd, ywh, this.getXMBH(), actinstID);
							msg.getData().setDJSH(sh);
							
							List<DJFDJSZ> sz = packageXml.getDJFDJSZ(zd, ywh, this.getXMBH());
							msg.getData().setDJSZ(sz);		
							
							List<DJFDJFZ> fz = packageXml.getDJFDJFZ(zd,ywh,this.getXMBH());
							msg.getData().setDJFZ(fz);	
							List<DJFDJGD> gd = packageXml.getDJFDJGD(zd,ywh,this.getXMBH());
							msg.getData().setDJGD(gd);	
							List<DJFDJSQR> djsqrs = msg.getData().getDJSQR();
							//查封现在的不填写申请人，为了和接入规范一致，先在这里拼凑出申请人。
							DJFDJSQR djsqr = new DJFDJSQR();
							djsqr.setYsdm("2004020000");
							djsqr.setQlrmc(cfdj == null ? "无" :StringHelper.formatDefaultValue(cfdj.getCfjg()));
							djsqr.setYwh(ywh);
//							djsqr.setQxdm(ConfigHelper.getNameByValue("XZQHDM"));
							djsqrs.add(djsqr);
							djsqrs = packageXml.getDJSQRs(djsqrs, sqrs, zd.getYSDM(), ywh, zd.getBDCDYH());
							msg.getData().setDJSQR(djsqrs);
							
							FJF100 fj = msg.getData().getFJF100();
							fj = packageXml.getFJF(fj);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					mashaller.marshal(msg, new File(path + "Biz" + msg.getHead().getBizMsgID() + ".xml"));
					result = uploadFile(path + "Biz" + msg.getHead().getBizMsgID() + ".xml",ConstValue.RECCODE.CF_CFDJ.Value,actinstID,djdy.getDJDYID(),qlid);
					names.put(djdy.getDJDYID(), msg.getHead().getBizMsgID()+ ".xml");
					
					if(result.equals("")|| result==null){
						Map<String, String> xmlError = new HashMap<String, String>();
						xmlError.put("error", "连接SFTP失败,请检查服务器和前置机的连接是否正常");
						YwLogUtil.addSjsbResult("Biz" + msg.getHead().getBizMsgID() + ".xml", "", "连接SFTP失败,请检查服务器和前置机的连接是否正常", ConstValue.SF.NO.Value, ConstValue.RECCODE.CF_CFDJ.Value,ProjectHelper.getpRroinstIDByActinstID(actinstID));
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
