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
import com.supermap.realestate.registration.util.ConfigHelper;
import com.supermap.realestate.registration.util.ConstValue;
import com.supermap.realestate.registration.util.ConstValue.BDCDYLX;
import com.supermap.realestate.registration.util.ConstValue.CZFS;
import com.supermap.realestate.registration.util.ConstValue.DJDYLY;
import com.supermap.realestate.registration.util.ConstValue.DYBDCLX;
import com.supermap.realestate.registration.util.ConstValue.QLLX;
import com.supermap.realestate.registration.util.GetProperties;
import com.supermap.realestate.registration.util.ObjectHelper;
import com.supermap.realestate.registration.util.ProjectHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.realestate.registration.util.YwLogUtil;
import com.supermap.wisdombusiness.core.SuperHelper;
import com.supermap.wisdombusiness.core.SuperSpringContext;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;

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
public class DYDJHandler_HouseAndLand extends DJHandlerBase implements DJHandler {

	/**
	 * 构造函数
	 * 
	 * @param info
	 */
	public DYDJHandler_HouseAndLand(ProjectInfo info) {
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
			fsql.setZJJZWZL(_srcUnit.getZL());//将房屋的坐落拷贝到附属权利表的zjjzwzl
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
	 * 添加登记单元信息
	 * 
	 * @作者 海豹
	 * @创建时间 2015年7月10日上午11:47:23
	 * @param bdcdyid
	 * @param bdcs_h_xzy
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
								updateZD(bdcs_djdy_gz.getBDCDYID(), bdcs_djdy_gz.getDJDYID());
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

	public void updateZD(String bdcdyid_h, String djdyid_h) {
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
		BDCS_DJDY_XZ djdyid_zd=null;
		List<BDCS_DJDY_XZ> zd_djdys=getCommonDao().getDataList(BDCS_DJDY_XZ.class, "BDCDYID='"+bdcdyid_zd+"' AND BDCDYLX='02'");
		if(zd_djdys!=null&&zd_djdys.size()>0){
			djdyid_zd=zd_djdys.get(0);
		}else{
			djdyid_zd=new BDCS_DJDY_XZ();
			List<BDCS_DJDY_LS> zd_djdys_ls=getCommonDao().getDataList(BDCS_DJDY_LS.class, "BDCDYID='"+bdcdyid_zd+"' AND BDCDYLX='02'");
			if(zd_djdys_ls!=null&&zd_djdys_ls.size()>0){
				ObjectHelper.copyObject(zd_djdys_ls.get(0), djdyid_zd);
			}else{
				String djdyid=super.getPrimaryKey();
				djdyid_zd.setBDCDYH(unit_zd.getBDCDYH());
				djdyid_zd.setBDCDYID(unit_zd.getId());
				djdyid_zd.setBDCDYLX(BDCDYLX.SHYQZD.Value);
				djdyid_zd.setDJDYID(djdyid);
				djdyid_zd.setLY(DJDYLY.XZ.Value);
				djdyid_zd.setXMBH(super.getXMBH());
				djdyid_zd.setGROUPID(1);
				BDCS_DJDY_LS zd_ls = ObjectHelper.copyDJDY_XZToLS(djdyid_zd);
				getCommonDao().save(zd_ls);
			}
			getCommonDao().save(djdyid_zd);
		}
		Rights right_h = RightsTools.loadRightsByDJDYID(DJDYLY.GZ, super.getXMBH(), djdyid_h);
		if (right_h != null) {
			SubRights subright_h = RightsTools.loadSubRights(DJDYLY.GZ, right_h.getFSQLID());
			String qlid_zd = SuperHelper.GeneratePrimaryKey();
			String fsqlid_zd = SuperHelper.GeneratePrimaryKey();
			//拷贝权利
			BDCS_QL_XZ ql_zd_xz = ObjectHelper.copyQL_GZToXZ((BDCS_QL_GZ) right_h);
			ql_zd_xz.setId(qlid_zd);
			ql_zd_xz.setFSQLID(fsqlid_zd);
//			ql_zd_xz.setBDCQZH("");
//			ql_zd_xz.setBDCQZHXH("");
//			ql_zd_xz.setZSBH("");
			ql_zd_xz.setDJDYID(djdyid_zd.getDJDYID());
			ql_zd_xz.setBDCDYH(djdyid_zd.getBDCDYH());
			BDCS_QL_LS ql_zd_ls = ObjectHelper.copyQL_XZToLS(ql_zd_xz);
			getCommonDao().save(ql_zd_xz);
			getCommonDao().save(ql_zd_ls);
			BDCS_FSQL_XZ fsql_zd_xz = ObjectHelper.copyFSQL_GZToXZ((BDCS_FSQL_GZ) subright_h);
			fsql_zd_xz.setId(fsqlid_zd);
			fsql_zd_xz.setQLID(qlid_zd);
			fsql_zd_xz.setDJDYID(djdyid_zd.getDJDYID());
			fsql_zd_xz.setBDCDYH(djdyid_zd.getBDCDYH());
			fsql_zd_xz.setDYBDCLX(DYBDCLX.TDHFW.Value);
			fsql_zd_xz.setDYWLX(DYBDCLX.TDHFW.Value);
			BDCS_FSQL_LS fsql_zd_LS = ObjectHelper.copyFSQL_XZToLS(fsql_zd_xz);
			getCommonDao().save(fsql_zd_xz);
			getCommonDao().save(fsql_zd_LS);
			List<RightsHolder> qlrs_h=RightsHolderTools.loadRightsHolders(DJDYLY.GZ, right_h.getId());
			List<String> zsids = new ArrayList<String>();
			List<String> zdzsids = new ArrayList<String>();
			for (RightsHolder rightsHolder : qlrs_h) {
				String qlrid=super.getPrimaryKey();
				BDCS_QLR_XZ qlr = ObjectHelper.copyQLR_GZToXZ((BDCS_QLR_GZ) rightsHolder);
				qlr.setId(qlrid);
				qlr.setQLID(qlid_zd);
				getCommonDao().save(qlr);
				BDCS_QLR_LS qlrls = ObjectHelper.copyQLR_XZToLS(qlr);
				getCommonDao().save(qlrls);
				List<BDCS_QDZR_GZ> qdzrs=getCommonDao().getDataList(BDCS_QDZR_GZ.class, "QLRID='"+rightsHolder.getId()+"'");
				String zsid = "";
				for (BDCS_QDZR_GZ bdcs_QDZR_GZ : qdzrs) {
					if(!zsids.contains(bdcs_QDZR_GZ.getZSID())){
						zsids.add(bdcs_QDZR_GZ.getZSID());
						zsid = SuperHelper.GeneratePrimaryKey();
						zdzsids.add(zsid);
					}
					BDCS_QDZR_XZ bdcs_QDZR_XZ = ObjectHelper.copyQDZR_GZToXZ(bdcs_QDZR_GZ);
					bdcs_QDZR_XZ.setId(SuperHelper.GeneratePrimaryKey().toString());
					bdcs_QDZR_XZ.setZSID(zsid);
					bdcs_QDZR_XZ.setXMBH(super.getXMBH());
					bdcs_QDZR_XZ.setDJDYID(djdyid_zd.getDJDYID());
					bdcs_QDZR_XZ.setFSQLID(fsqlid_zd);
					bdcs_QDZR_XZ.setQLID(qlid_zd);
					BDCS_QDZR_LS bdcs_QDZR_LS = ObjectHelper.copyQDZR_XZToLS(bdcs_QDZR_XZ);
					getCommonDao().save(bdcs_QDZR_XZ);
					getCommonDao().save(bdcs_QDZR_LS);
				}
			}
			for (String zsid : zdzsids) {
				BDCS_ZS_XZ zs_zd_xz = new BDCS_ZS_XZ();
				zs_zd_xz.setXMBH(super.getXMBH());
				zs_zd_xz.setId(zsid);
				BDCS_ZS_LS zs_zd_ls = ObjectHelper.copyZS_XZToLS(zs_zd_xz);
				getCommonDao().save(zs_zd_xz);
				getCommonDao().save(zs_zd_ls);
			
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

			//房地一体路程查询历史层
			List<BDCS_DJDY_LS> djdys_ls=dao.getDataList(BDCS_DJDY_LS.class, xmbhHql);
			if(djdys_ls!=null&&djdys_ls.size()>0) {
				BDCS_DJDY_GZ gz=null;
				for(BDCS_DJDY_LS ls:djdys_ls) {
					try {
						gz=null;
						if(ls.getBDCDYLX()!=null&&!"".equals(ls.getBDCDYLX())&&"02".equals(ls.getBDCDYLX())) {
							gz=new BDCS_DJDY_GZ();
							PropertyUtils.copyProperties(gz, ls);
						}
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						e.printStackTrace();
					} catch (NoSuchMethodException e) {
						e.printStackTrace();
					}
					if(gz!=null) {
						djdys.add(gz);
					}
				}
			} 
		
			if (djdys != null && djdys.size() > 0) {
				mashaller = JAXBContext.newInstance(Message.class).createMarshaller();
				Calendar calendar = Calendar.getInstance(); // 创建一个日历对象
				calendar.setTime(new Date()); // 用当前时间初始化日历时间
				String cyear = calendar.get(Calendar.YEAR) + "";
//				String ywh = cyear + ConfigHelper.getNameByValue("XZQHDM") + super.getDjlx().Value
//						+ super.getQllx().Value + super.getPrjNumber().substring(1, 6);
				
				BDCS_XMXX xmxx = dao.get(BDCS_XMXX.class, super.getXMBH());
				String ywh=xmxx.getYWLSH();
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
								sq = packageXml.getDJTDJSLSQ(sq, ywh, zd, ql, xmxx, null, xmxx.getSLSJ(), null,
										null, null);

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
					if (BDCDYLX.H.Value.equals(djdy.getBDCDYLX())) { // 房屋
						try {
							h = dao.get(BDCS_H_XZ.class, djdy.getBDCDYID());
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
								sq = packageXml.getDJTDJSLSQ(sq, ywh, null, ql, xmxx, h, xmxx.getSLSJ(), null,
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
					BDCS_H_XZY xzy = null;
					if (BDCDYLX.YCH.Value.equals(djdy.getBDCDYLX())) { // 在建工程
						try {
							xzy = dao.get(BDCS_H_XZY.class, djdy.getBDCDYID());
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

}
