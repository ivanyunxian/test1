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
import com.supermap.realestate.registration.dataExchange.dyq.QLFQLDYAQ;
import com.supermap.realestate.registration.dataExchange.utils.packageXml;
import com.supermap.realestate.registration.handler.DJHandler;
import com.supermap.realestate.registration.model.BDCS_DJDY_GZ;
import com.supermap.realestate.registration.model.BDCS_DJDY_LS;
import com.supermap.realestate.registration.model.BDCS_DJDY_XZ;
import com.supermap.realestate.registration.model.BDCS_FSQL_GZ;
import com.supermap.realestate.registration.model.BDCS_FSQL_LS;
import com.supermap.realestate.registration.model.BDCS_FSQL_XZ;
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
import com.supermap.realestate.registration.model.BDCS_ZS_LS;
import com.supermap.realestate.registration.model.BDCS_ZS_XZ;
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
import com.supermap.realestate.registration.util.ConstValue.CZFS;
import com.supermap.realestate.registration.util.ConstValue.DJDYLY;
import com.supermap.realestate.registration.util.ConstValue.DJLX;
import com.supermap.realestate.registration.util.ConstValue.QLLX;
import com.supermap.realestate.registration.util.ConstValue.SQRLB;
import com.supermap.realestate.registration.util.Global;
import com.supermap.realestate.registration.util.ObjectHelper;
import com.supermap.realestate.registration.util.ProjectHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.realestate.registration.util.YwLogUtil;
import com.supermap.wisdombusiness.core.SuperHelper;
import com.supermap.wisdombusiness.core.SuperSpringContext;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;

/**
 * 
 * 房地一体抵押权转移登记处理类
 * @ClassName: DYZYDJHandler_HouseAndLand
 * @author liangc
 * @date 2018年05月10日 下午2:24:30
 */
public class DYZYDJHandler_HouseAndLand extends DJHandlerBase implements DJHandler {

	/**
	 * 构造函数
	 * @param info
	 */
	public DYZYDJHandler_HouseAndLand(ProjectInfo info) {
		super(info);
	}

	@Override
	public boolean addBDCDY(String qlid) {
		boolean flag = true;
		if (!StringHelper.isEmpty(qlid)) {
			String[] qlids = qlid.split(",");
			for (String id : qlids) {
				if (!StringHelper.isEmpty(id)) {
					if (flag = true) {
						flag = addbdcdy(id);
					}
				}
			}
		}
		return true;
	}

	/**
	 * 添加不动产单元
	 */

	public boolean addbdcdy(String qlid) {
		StringBuilder builer = new StringBuilder();
		builer.append(" QLID='").append(qlid).append("'");
		CommonDao commonDao = getCommonDao();
		BDCS_QL_XZ bdcs_ql_xz = commonDao.get(BDCS_QL_XZ.class, qlid);

		if (bdcs_ql_xz != null) {
			String gzqlid = getPrimaryKey();
			String gzfsqlid = getPrimaryKey();
			// 拷贝权利
			BDCS_QL_GZ bdcs_ql_gz = ObjectHelper.copyQL_XZToGZ(bdcs_ql_xz);
			if (bdcs_ql_gz.getCZFS() == null) {
				bdcs_ql_gz.setCZFS(CZFS.GTCZ.Value);
			}
			bdcs_ql_gz.setId(gzqlid);
			bdcs_ql_gz.setFSQLID(gzfsqlid);
			bdcs_ql_gz.setXMBH(getXMBH());
			bdcs_ql_gz.setYWH(this.getProject_id());
			bdcs_ql_gz.setDJLX(DJLX.ZYDJ.Value);
			bdcs_ql_gz.setLYQLID(qlid);
			bdcs_ql_gz.setDBR("");
			bdcs_ql_gz.setBDCQZH("");
			bdcs_ql_gz.setDJSJ(null);
			commonDao.save(bdcs_ql_gz);

			BDCS_FSQL_XZ bdcs_fsql_xz = commonDao.get(BDCS_FSQL_XZ.class, bdcs_ql_xz.getFSQLID());
			if (bdcs_fsql_xz != null) {
				// 拷贝附属权利
				BDCS_FSQL_GZ bdcs_fsql_gz = ObjectHelper.copyFSQL_XZToGZ(bdcs_fsql_xz);
				bdcs_fsql_gz.setQLID(gzqlid);
				bdcs_fsql_gz.setId(gzfsqlid);
				bdcs_fsql_gz.setXMBH(getXMBH());
				int dysw=RightsTools.getMaxMortgageSWS(bdcs_ql_gz.getDJDYID());
				bdcs_fsql_gz.setDYSW(dysw+1);
				commonDao.save(bdcs_fsql_gz);
			}
			StringBuilder builderDJDY = new StringBuilder();
			builderDJDY.append(" DJDYID='");
			builderDJDY.append(bdcs_ql_xz.getDJDYID());
			builderDJDY.append("'");
			CopySQRFromXZQLR(qlid, gzqlid);
			// 获取登记单元集合
			List<BDCS_DJDY_XZ> djdys = commonDao.getDataList(BDCS_DJDY_XZ.class, builderDJDY.toString());
			if (djdys != null && djdys.size() > 0) {
				BDCS_DJDY_XZ bdcs_djdy_xz = djdys.get(0);
				// 拷贝登记单元
				BDCS_DJDY_GZ bdcs_djdy_gz = ObjectHelper.copyDJDY_XZToGZ(bdcs_djdy_xz);
				bdcs_djdy_gz.setId(getPrimaryKey());
				bdcs_djdy_gz.setLY(DJDYLY.XZ.Value);
				bdcs_djdy_gz.setXMBH(getXMBH());
				commonDao.save(bdcs_djdy_gz);
				// 把附属权利里边的抵押人和抵押不动产类型写上
				String qllxarray = "('3','4','5','6','7','8','10','11','12','15','36')";
				String hql = "QLID IN(SELECT id FROM BDCS_QL_XZ WHERE  DJDYID='" + bdcs_djdy_gz.getDJDYID() + "' AND QLLX IN " + qllxarray + ") ORDER BY SXH";
				List<BDCS_QLR_XZ> list = commonDao.getDataList(BDCS_QLR_XZ.class, hql);
				if (list != null && list.size() > 0) {
					for (int i = 0; i < list.size(); i++) {
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
							 
							List<BDCS_SQR> sqrlist = commonDao.getDataList(BDCS_SQR.class, Sql);
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
							sqr.setGLQLID(bdcs_ql_gz.getId());
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
							commonDao.save(sqr);
						}
					}
				}
			}
			
			bdcs_ql_xz.setDJZT("02");
			commonDao.update(bdcs_ql_xz);
		}
		commonDao.flush();
		return true;
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
				sqr.setSQRLB(SQRLB.YF.Value);
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
			
			//添加获取义务人 liangq
			for(int i=0;i<qlrs.size();i++){
				boolean _bexists = false;	
				//有义务人就添加
				String sql = "xmbh = '"+qlrs.get(i).getXMBH()+"' and sqrlb= '2'";
				List<BDCS_SQR> sqrlist = getCommonDao().getDataList(BDCS_SQR.class, sql);
				if (sqrlist != null && sqrlist.size() > 0) {
					_bexists = true;
				}
				if (_bexists) {
					String SQRID = getPrimaryKey();
					BDCS_SQR sqr = new BDCS_SQR();
					sqr.setGYFS(sqrlist.get(i).getGYFS());
					sqr.setFZJG(sqrlist.get(i).getFZJG());
					sqr.setGJDQ(sqrlist.get(i).getGJDQ());
					sqr.setGZDW(sqrlist.get(i).getGZDW());
					sqr.setXB(sqrlist.get(i).getXB());
					sqr.setHJSZSS(sqrlist.get(i).getHJSZSS());
					sqr.setSSHY(sqrlist.get(i).getSSHY());
					sqr.setYXBZ(sqrlist.get(i).getYXBZ());
					sqr.setQLBL(sqrlist.get(i).getQLBL());
					sqr.setQLMJ(StringHelper.formatObject(sqrlist.get(i).getQLMJ()));
					sqr.setSQRXM(sqrlist.get(i).getSQRXM());
					sqr.setSQRLB(sqrlist.get(i).getSQRLB());
					sqr.setSQRLX(sqrlist.get(i).getSQRLX());
					sqr.setDZYJ(sqrlist.get(i).getDZYJ());
					sqr.setLXDH(sqrlist.get(i).getLXDH());
					sqr.setZJH(sqrlist.get(i).getZJH());
					sqr.setZJLX(sqrlist.get(i).getZJLX());
					sqr.setTXDZ(sqrlist.get(i).getTXDZ());
					sqr.setYZBM(sqrlist.get(i).getYZBM());
					sqr.setXMBH(getXMBH());
					sqr.setId(SQRID);
					sqr.setGLQLID(gzqlid);
					getCommonDao().save(sqr);
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
				commonDao.update(subright);
			}
		}
		// List<BDCS_QL_GZ> qls = commonDao.getDataList(BDCS_QL_GZ.class,
		// strSqlXMBH);
		// if (qls != null && qls.size() > 0) {
		// for (int iql = 0; iql < qls.size(); iql++) {
		// BDCS_QL_GZ ql_gz = qls.get(iql);
		// String lyqlid = ql_gz.getLYQLID();
		// String sqlQL = MessageFormat.format(" QLID=''{0}''", lyqlid);
		// // 1.删除权利人
		// commonDao.deleteEntitysByHql(BDCS_QLR_XZ.class, sqlQL);
		// // 2.删除权利
		// commonDao.deleteEntitysByHql(BDCS_QL_XZ.class, sqlQL);
		// // 3.删除附属权利
		// commonDao.deleteEntitysByHql(BDCS_FSQL_XZ.class, sqlQL);
		// // 4.删除证书
		// String sqlZS =
		// MessageFormat.format(" id IN (SELECT B.ZSID FROM BDCS_QDZR_XZ B WHERE B.QLID=''{0}'')",
		// lyqlid);
		// commonDao.deleteEntitysByHql(BDCS_ZS_XZ.class, sqlZS);
		// // 5.删除权利-权利人-证书-单元关系
		// commonDao.deleteEntitysByHql(BDCS_QDZR_XZ.class, sqlQL);
		// // 更新历史权利
		// BDCS_QL_LS bdcs_ql_ls = commonDao.get(BDCS_QL_LS.class, lyqlid);
		// if (bdcs_ql_ls != null) {
		// BDCS_FSQL_LS bdcs_fsql_ls =commonDao.get(BDCS_FSQL_LS.class,
		// bdcs_ql_ls.getFSQLID());
		// if(bdcs_fsql_ls !=null)
		// {
		// Date zxsj = new Date();
		// bdcs_fsql_ls.setZXSJ(zxsj);
		// bdcs_fsql_ls.setZXDBR(dbr);
		// commonDao.update(bdcs_fsql_ls);
		// }
		// }
		// }
		// }
		List<BDCS_DJDY_GZ> djdys = commonDao.getDataList(BDCS_DJDY_GZ.class, strSqlXMBH);
		List<String> zdbdcdyids = new ArrayList<String>();
		if (djdys != null && djdys.size() > 0) {
			for (int idjdy = 0; idjdy < djdys.size(); idjdy++) {
				BDCS_DJDY_GZ bdcs_djdy_gz = djdys.get(idjdy);
				if (bdcs_djdy_gz != null) {
					String djdyid = bdcs_djdy_gz.getDJDYID();
					// RightsTools.copyRightsAllByDJDYID(DJDYLY.GZ, DJDYLY.XZ,
					// this.getXMBH(), djdyid);//拷贝到现状
					// RightsTools.copyRightsAllByDJDYID(DJDYLY.GZ, DJDYLY.LS,
					// this.getXMBH(), djdyid);//拷贝到历史
					super.CopyGZQLToXZAndLS(djdyid);
					super.CopyGZQLRToXZAndLS(djdyid);
					super.CopyGZQDZRToXZAndLS(djdyid);
					super.CopyGZZSToXZAndLS(djdyid);
					BDCDYLX dylx = ProjectHelper.GetBDCDYLX(getXMBH());
					// 更新单元抵押状态
					SetDYDYZT(bdcs_djdy_gz.getBDCDYID(), dylx, "0");
					
					RealUnit _srcunit = UnitTools.loadUnit(dylx, DJDYLY.XZ, bdcs_djdy_gz.getBDCDYID());
					if (_srcunit != null) {
						if(_srcunit instanceof House){
							House house = (House) _srcunit;
							if(!zdbdcdyids.contains(house.getZDBDCDYID())){
								zdbdcdyids.add(house.getZDBDCDYID());
								updateZD(bdcs_djdy_gz.getBDCDYID(), djdyid);
							}
						}
					}
				}
			}
		}
		this.SetSFDB();
		super.alterCachedXMXX();
		commonDao.flush();
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
		List<Rights> list_ql_zd_old=RightsTools.loadRightsByCondition(DJDYLY.XZ, "QLLX IN ('23') AND DJDYID='"+djdyid_zd+"'");
		Rights ql_zd_old=null;
		SubRights fsql_zd_old=null;
		if(list_ql_zd_old!=null&&list_ql_zd_old.size()>0){
			ql_zd_old=list_ql_zd_old.get(0);
			fsql_zd_old=RightsTools.loadSubRights(DJDYLY.XZ, ql_zd_old.getFSQLID());
			
		}
		List<Rights> list_ql_h_new=RightsTools.loadRightsByCondition(DJDYLY.GZ, "QLLX IN ('23') AND DJDYID='"+djdyid_h+"' AND XMBH='"+super.getXMBH()+"'");
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
			ql_zd_new.setBDCQZH(ql_h_new.getBDCQZH());
			ql_zd_new.setZSBH(ql_h_new.getZSBH());
			ql_zd_new.setDJYY(ql_h_new.getDJYY());
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
			fsql_zd_old.setZXSJ(ql_h_new.getDJSJ());
			fsql_zd_old.setZXDBR(Global.getCurrentUserName());
			fsql_zd_old.setZXDYYY(ql_h_new.getDJYY());
			dao.save(fsql_zd_old);
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

			// 删除权利、附属权利、权利人、证书、权地证人关系
			String _hqlCondition = MessageFormat.format("XMBH=''{0}'' AND DJDYID=''{1}''", getXMBH(), djdyid);
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
		List<UnitTree> list = super.getRightList();
		for (UnitTree tree : list) {
			String qlid = tree.getQlid();
			Rights right = RightsTools.loadRights(DJDYLY.GZ, qlid);
			if (right != null) {
				tree.setOldqlid(right.getLYQLID());
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
	
	@Override
	public Map<String,String> exportXML(String path, String actinstID) {
		Marshaller mashaller;
		Map<String,String> names = new HashMap<String,String>();
		try {
			CommonDao dao = super.getCommonDao();
			String xmbhHql = ProjectHelper.GetXMBHCondition(super.getXMBH());
			List<BDCS_DJDY_LS> djdys = dao.getDataList(BDCS_DJDY_LS.class, xmbhHql);
			if (djdys != null && djdys.size() > 0) {
				mashaller = JAXBContext.newInstance(Message.class).createMarshaller();
				Calendar calendar = Calendar.getInstance(); // 创建一个日历对象
				calendar.setTime(new Date()); // 用当前时间初始化日历时间
				String cyear = calendar.get(Calendar.YEAR) + "";
				String ywh = packageXml.GetYWLSHByYWH(this.getProject_id());
				BDCS_XMXX xmxx = dao.get(BDCS_XMXX.class, super.getXMBH());
				String result = "";
				for(int i = 0; i < djdys.size(); i ++){
					BDCS_DJDY_LS djdy = djdys.get(i);
					
					String condition = MessageFormat.format("DJDYID=''{0}'' AND QLLX=''23'' ", djdy.getDJDYID());
					List<Rights> _rightss = RightsTools.loadRightsByCondition(DJDYLY.LS, condition);
					BDCS_QL_GZ ql = null;
					List<BDCS_QLR_GZ> qlrs = new ArrayList<BDCS_QLR_GZ>();
					for (Rights rights : _rightss) {
						
						ql = (BDCS_QL_GZ) rights;
						List<BDCS_QLR_LS> qlrsLS = getCommonDao().getDataList(BDCS_QLR_LS.class, "qlid = '"+ql.getId()+"'");
						for (BDCS_QLR_LS qlrls : qlrsLS) {
							RightsHolder qlr = qlrls;
							BDCS_QLR_GZ qlrgz = (BDCS_QLR_GZ) qlr;
							qlrs.add(qlrgz);
						}
						if(qlrs != null){
							break;
						}
					}
					List<BDCS_SQR> sqrs = new ArrayList<BDCS_SQR>();
					ProjectServiceImpl serviceImpl = SuperSpringContext.getContext().getBean(ProjectServiceImpl.class);
					sqrs = serviceImpl.getSQRList(super.getXMBH());
					
					BDCS_FSQL_GZ fsql = new BDCS_FSQL_GZ();
					if(ql != null && !StringUtils.isEmpty(ql.getFSQLID())){
						BDCS_FSQL_LS fsqlls = dao.get(BDCS_FSQL_LS.class, ql.getFSQLID());
						SubRights fsqljk = fsqlls;
						fsql = (BDCS_FSQL_GZ) fsqljk;
					}
					
					Message msg = exchangeFactory.createMessageByDYQ();
					msg.getHead().setRecType("9000101");

					if (BDCDYLX.SHYQZD.Value.equals(djdy.getBDCDYLX()) || QLLX.ZJDSYQ.Value.equals(this.getQllx().Value)) { //使用权宗地、宅基地使用权
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
					BDCS_H_XZ h = null;
					if(BDCDYLX.H.Value.equals(djdy.getBDCDYLX())){ //房屋
						try {
							h = dao.get(BDCS_H_XZ.class, djdy.getBDCDYID());
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
