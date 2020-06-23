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
import com.supermap.realestate.registration.model.BDCS_ZS_GZ;
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
import com.supermap.realestate.registration.util.ConstValue.DJDYLY;
import com.supermap.realestate.registration.util.ConstValue.DYBDCLX;
import com.supermap.realestate.registration.util.Global;
import com.supermap.realestate.registration.util.ObjectHelper;
import com.supermap.realestate.registration.util.ProjectHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.realestate.registration.util.YwLogUtil;
import com.supermap.wisdombusiness.core.SuperHelper;
import com.supermap.wisdombusiness.core.SuperSpringContext;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;



/**
 * 房地一体查封更正登记处理类
 * @ClassName: GZ_CFDJHouseAndLand
 * @author liangc
 * @date 2018年5月16日 上午11:26:30
 */
public class GZ_CFDJHouseAndLandHandler extends DJHandlerBase implements DJHandler {

	public GZ_CFDJHouseAndLandHandler(ProjectInfo info) {
		super(info);
	}

	@Override
	public boolean addBDCDY(String qlid) {
		boolean bSuccess = true;

		if (!StringHelper.isEmpty(qlid)) {
			String[] qlids = qlid.split(",");
			for (int i = 0; i < qlids.length; i++) {
				String id = qlids[i];
				if (!StringHelper.isEmpty(id))
					if (bSuccess) {
						bSuccess = addbdcdy(id);
					}
			}
		} else {
			bSuccess = false;
			setErrMessage("请选择一条查封信息");
		}
		return bSuccess;
	}

	private boolean addbdcdy(String qlid) {
		boolean bSuccess = false;
		CommonDao dao = this.getCommonDao();
		BDCS_QL_XZ xz_ql = getQLXZbyQLID(dao, qlid);
		BDCS_FSQL_XZ xz_fsql = getFSQLXZbyQLID(dao, qlid);
		BDCS_DJDY_XZ xz_djdy = getDJDYXZbyDJDYID(dao, xz_ql.getDJDYID());

		if (xz_djdy != null) {
			if (ValidateDup(dao, xz_djdy.getBDCDYID()))// 严重是否重复插入
				return true;

			// 现状库拷贝到工作库
			BDCS_QL_GZ gz_ql = new BDCS_QL_GZ();
			BDCS_FSQL_GZ gz_fsql = new BDCS_FSQL_GZ();
			BDCS_DJDY_GZ gz_djdy = new BDCS_DJDY_GZ();

			ObjectHelper.copyObject(xz_ql, gz_ql);
			ObjectHelper.copyObject(xz_fsql, gz_fsql);

			gz_ql.setDJLX(super.getDjlx().Value);
			gz_ql.setQLLX(super.getQllx().Value);
			gz_ql.setXMBH(super.getXMBH());
			gz_ql.setLYQLID(qlid);
			gz_ql.setCASENUM("");

			// 工作库附属权利信息重新赋值，参考super.createFSQL写法
			if (StringHelper.isEmpty(gz_fsql.getCFWJ())) {
				gz_fsql.setCFWJ(ConfigHelper.getNameByValue("DEFAULTCFWJ"));
			}
			if (StringHelper.isEmpty(gz_fsql.getCFFW())) {
				gz_fsql.setCFFW(ConfigHelper.getNameByValue("DEFAULTCFFW"));
			}

			gz_fsql.setXMBH(super.getXMBH());
            //判断现状是否有多条查封数据，多于一条是续封，少于一条是查封wuzhu
		/*	String hql = MessageFormat.format(" DJDYID=''{0}''", xz_djdy.getDJDYID());
			List<BDCS_QL_XZ> list = dao.getDataList(BDCS_QL_XZ.class, hql);
			if (list != null && list.size() > 0) {
				gz_fsql.setCFLX(CFLX.XF.Value);
			}
			else
			{
				if(this.getBdcdylx().equals(BDCDYLX.YCZRZ)||this.getBdcdylx().equals(BDCDYLX.YCH))
				 gz_fsql.setCFLX(CFLX.YCF.Value);
				else
			     gz_fsql.setCFLX(CFLX.CF.Value);
			}*/
			String newqlid = SuperHelper.GeneratePrimaryKey();
			String newfsqlid = SuperHelper.GeneratePrimaryKey();
			gz_ql.setId(newqlid);
			gz_ql.setFSQLID(newfsqlid);
			gz_fsql.setQLID(newqlid);
			gz_fsql.setId(newfsqlid);

			BDCS_XMXX xmxx=dao.get(BDCS_XMXX.class, super.getXMBH());
			if(xmxx!=null&&(!"1".equals(ConfigHelper.getNameByValue("InheritCFSJ")))){
				gz_fsql.setCFSJ(xmxx.getSLSJ());
			}
			// 工作库登记单元信息重新赋值，参考super.createDJDYfromXZ写法
			gz_djdy = new BDCS_DJDY_GZ();
			gz_djdy.setXMBH(super.getXMBH());
			gz_djdy.setDJDYID(xz_djdy.getDJDYID());
			gz_djdy.setBDCDYID(xz_djdy.getBDCDYID());
			gz_djdy.setBDCDYLX(super.getBdcdylx().Value);
			gz_djdy.setBDCDYH(xz_djdy.getBDCDYH());
			gz_djdy.setLY(DJDYLY.XZ.Value);

			// 保存
			dao.save(gz_djdy);
			dao.save(gz_ql);
			dao.save(gz_fsql);
			bSuccess = true;
		}
		dao.flush();
		return bSuccess;
	}

	// 验证是否重复 重复返回TRUE否则返回FALSE
	private boolean ValidateDup(CommonDao dao, String bdcdyid) {
		String hql = "BDCDYID='" + bdcdyid + "' AND XMBH='" + super.getXMBH()
				+ "'";// 通过不动产单元ID和项目编号判断是否重复

		List<BDCS_DJDY_GZ> list = dao.getDataList(BDCS_DJDY_GZ.class, hql);
		if (list != null && list.size() > 0) {
			return true;
		}
		return false;
	}

	// 根据权利ID返回现状库权利
	private BDCS_QL_XZ getQLXZbyQLID(CommonDao dao, String qlid) {
		String hql = MessageFormat.format(" QLID=''{0}''", qlid);
		List<BDCS_QL_XZ> list = dao.getDataList(BDCS_QL_XZ.class, hql);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return new BDCS_QL_XZ();
	}

	// 根据权利ID返回现状库附属权利
	private BDCS_FSQL_XZ getFSQLXZbyQLID(CommonDao dao, String qlid) {
		String hql = MessageFormat.format(" QLID=''{0}''", qlid);
		List<BDCS_FSQL_XZ> list = dao.getDataList(BDCS_FSQL_XZ.class, hql);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	// 根据权利ID返回现状库登记单元
	private BDCS_DJDY_XZ getDJDYXZbyDJDYID(CommonDao dao, String djdyid) {
		String hql = MessageFormat.format(" DJDYID=''{0}''", djdyid);
		List<BDCS_DJDY_XZ> list_djdy = dao.getDataList(BDCS_DJDY_XZ.class, hql);
		if (list_djdy != null && list_djdy.size() > 0) {
			return list_djdy.get(0);// 一个登记ID对应多个不动产单元的先不考虑
		}
		return null;
	}

	/**
	 * 写入登记簿
	 */
	@Override
	public boolean writeDJB() {
		String dbr = Global.getCurrentUserName();
		Date date = new Date();
		String strSqlXMBH = ProjectHelper.GetXMBHCondition(getXMBH());
		List<BDCS_DJDY_GZ> djdys = getCommonDao().getDataList(
				BDCS_DJDY_GZ.class, strSqlXMBH);
		BDCDYLX dylx = ProjectHelper.GetBDCDYLX(getXMBH());
		List<String> zdbdcdyids = new ArrayList<String>();
		if (djdys != null && djdys.size() > 0) {
			for (int idjdy = 0; idjdy < djdys.size(); idjdy++) {
				BDCS_DJDY_GZ bdcs_djdy_gz = djdys.get(idjdy);
				if (bdcs_djdy_gz != null) {
					String djdyid = bdcs_djdy_gz.getDJDYID();
					// 工作层查封权利
					Rights gzrights = RightsTools.loadRightsByDJDYID(DJDYLY.GZ,
							getXMBH(), djdyid);
					if (gzrights != null) {
						// 来源权利ID
						String lyqlid = gzrights.getLYQLID();
						if (!StringHelper.isEmpty(lyqlid)) {

							// 先删除现状层的权利
							Rights xzrights = RightsTools.deleteRights(
									DJDYLY.XZ, lyqlid);
							RightsTools.deleteSubRights(DJDYLY.XZ,
									xzrights.getFSQLID());

							// 再修改历史层的权利
							SubRights lssubrights = RightsTools
									.loadSubRightsByRightsID(DJDYLY.LS, lyqlid);
							if (lssubrights != null) {
								lssubrights.setZXFJ("由于查封更正，本查封自动注销");
								lssubrights.setZXDBR(dbr);
								lssubrights.setZXSJ(date);
								lssubrights.setZXDYYWH(super.getProject_id());
								lssubrights.setZXDYYY("由于查封更正，本查封自动注销");
								getCommonDao().update(lssubrights);
							}

							gzrights.setDBR(dbr);
							gzrights.setDJSJ(date);
							gzrights.setDJJG(ConfigHelper.getNameByValue("DJJGMC"));

							// 再把工作层拷贝到现状层和历史层
							Rights newxzrights = RightsTools.copyRightsOnly(
									DJDYLY.GZ, DJDYLY.XZ, gzrights);
							Rights newlsrights = RightsTools.copyRightsOnly(
									DJDYLY.GZ, DJDYLY.LS, gzrights);
							getCommonDao().save(newxzrights);
							getCommonDao().save(newlsrights);
							getCommonDao().update(gzrights);
							
							RealUnit _srcunit = UnitTools.loadUnit(dylx, DJDYLY.XZ, bdcs_djdy_gz.getBDCDYID());
							if (_srcunit != null) {
								if(_srcunit instanceof House){
									House house = (House) _srcunit;
									if(!zdbdcdyids.contains(house.getZDBDCDYID())){
										zdbdcdyids.add(house.getZDBDCDYID());
										updateZD(bdcs_djdy_gz.getBDCDYID(), bdcs_djdy_gz.getDJDYID(),lyqlid);
									}
								}
							}
						}
					}
					this.SetXMCFZT(djdyid, "01");
				}
			}
		}
		this.SetSFDB();
		getCommonDao().flush();
		return true;
	}
	
	public void updateZD(String bdcdyid_h, String djdyid_h,String lyqlid) {
		BDCDYLX dylx = ProjectHelper.GetBDCDYLX(getXMBH());
		RealUnit unit_h = UnitTools.loadUnit(dylx, DJDYLY.XZ, djdyid_h);
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
		List<BDCS_QL_GZ> lsql=getCommonDao().getDataList(BDCS_QL_GZ.class, "QLID='"+lyqlid+"'");
		if(lsql!=null&&lsql.size()>0){
			String lsxmbh = lsql.get(0).getXMBH();
			List<BDCS_QL_XZ> zd_qls=getCommonDao().getDataList(BDCS_QL_XZ.class, "BDCDYID='"+bdcdyid_zd+"' AND BDCDYLX='02' AND XMBH='"+lsxmbh+"'");
			if(zd_qls!=null&&zd_qls.size()>0){
				for (int iql = 0; iql < zd_qls.size(); iql++) {
					BDCS_QL_XZ bdcs_ql_xz = zd_qls.get(iql);
					RightsTools.deleteRights(
							DJDYLY.XZ, bdcs_ql_xz.getId());
					RightsTools.deleteSubRights(DJDYLY.XZ,
							bdcs_ql_xz.getFSQLID());
				}
			}
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
			BDCS_FSQL_LS fsql_zd_LS = ObjectHelper.copyFSQL_XZToLS(fsql_zd_xz);
			getCommonDao().save(fsql_zd_xz);
			getCommonDao().save(fsql_zd_LS);
			List<RightsHolder> qlrs_h=RightsHolderTools.loadRightsHolders(DJDYLY.GZ, right_h.getId());
			List<String> zsids = new ArrayList<String>();
			List<String> zdzsids = new ArrayList<String>();
			if(qlrs_h != null && qlrs_h.size()>0){
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
	 * 拷贝工作库的权利表，权利附属表到现状库和历史库中。比较特殊是，这里的登薄人，登薄时间是保存在附属权利表的注销人，注销时间
	 * 里作为解封人，解封时间使用。登记机构是放在权利附属表的解封机构里面 参考SUPER的CopyGZQLToXZAndLS写法
	 * 
	 * @作者：WUZ
	 * @param djdyid
	 * @param qlid
	 * @return
	 */
	protected boolean CopyGZQLToXZAndLS(String djdyid) {
		StringBuilder builderDJDYID = new StringBuilder();
		builderDJDYID.append(" DJDYID='").append(djdyid).append("'");
		String strSqlDJDYID = builderDJDYID.toString();
		String xmbhFilter = ProjectHelper.GetXMBHCondition(this.getXMBH());
		List<BDCS_QL_GZ> qls = getCommonDao().getDataList(BDCS_QL_GZ.class,
				xmbhFilter + " and " + strSqlDJDYID);
		if (qls != null && qls.size() > 0) {
			for (int iql = 0; iql < qls.size(); iql++) {
				BDCS_QL_GZ bdcs_ql_gz = qls.get(iql);
				// 拷贝权利
				BDCS_QL_XZ bdcs_ql_xz = ObjectHelper.copyQL_GZToXZ(bdcs_ql_gz);
				getCommonDao().save(bdcs_ql_xz);
			}
		}
		return true;
	}

	/**
	 * 移除现状层中的查封登记权利 参考SUPER的removeQLFromXZByALL写法
	 * 
	 * @作者：WUZ
	 * @param djdyid
	 * @param qlid
	 * @return
	 */
	protected boolean removeQLFromXZByCFDJ(String qlid) {
		StringBuilder builderDJDYID = new StringBuilder();
		builderDJDYID.append(" QLID='").append(qlid).append("'");
		;
		String strSqlDJDYID = builderDJDYID.toString();
		List<BDCS_QL_XZ> qls = getCommonDao().getDataList(BDCS_QL_XZ.class,
				strSqlDJDYID);
		if (qls != null && qls.size() > 0) {
			for (int iql = 0; iql < qls.size(); iql++) {
				BDCS_QL_XZ bdcs_ql_xz = qls.get(iql);
				if (bdcs_ql_xz != null) {
					BDCS_FSQL_XZ bdcs_fsql_xz = getCommonDao().get(
							BDCS_FSQL_XZ.class, bdcs_ql_xz.getFSQLID());
					if (bdcs_fsql_xz != null) {
						getCommonDao().deleteEntity(bdcs_fsql_xz);
					}
					getCommonDao().deleteEntity(bdcs_ql_xz);
				}
			}
		}
		return true;
	}

	/**
	 * 移除历史层中的查封登记权利，在现在层中只保留合并后的解封信息 参考SUPER的removeQLFromXZByALL写法
	 * 
	 * @作者：WUZ
	 * @param djdyid
	 * @param qlid
	 * @return
	 */
	protected boolean removeQLFromLSByCFDJ(String qlid) {
		StringBuilder builderDJDYID = new StringBuilder();
		builderDJDYID.append(" QLID='").append(qlid).append("' ");
		;// 在权利历史层中获取到查封的信息
		String strSqlDJDYID = builderDJDYID.toString();
		List<BDCS_QL_LS> qls = getCommonDao().getDataList(BDCS_QL_LS.class,
				strSqlDJDYID);
		if (qls != null && qls.size() > 0) {
			for (int iql = 0; iql < qls.size(); iql++) {
				BDCS_QL_LS bdcs_ql_ls = qls.get(iql);
				if (bdcs_ql_ls != null) {
					BDCS_FSQL_LS bdcs_fsql_ls = getCommonDao().get(
							BDCS_FSQL_LS.class, bdcs_ql_ls.getFSQLID());
					if (bdcs_fsql_ls != null) {
						getCommonDao().deleteEntity(bdcs_fsql_ls);
					}
					getCommonDao().deleteEntity(bdcs_ql_ls);// 删除掉 历史相关的权利和附属权利
				}
			}
		}
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

			// 删除权利、附属权利、权利人、证书、权地证人关系
			String _hqlCondition = MessageFormat.format(
					"XMBH=''{0}'' AND DJDYID=''{1}''", getXMBH(), djdyid);
			RightsTools.deleteRightsAllByCondition(DJDYLY.GZ, _hqlCondition);

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

	@Override
	public Map<String,String> exportXML(String path, String actinstID) {
		Marshaller mashaller;
		Map<String, String> names = new HashMap<String,String>();
		try {
			CommonDao dao = super.getCommonDao();
			String xmbhHql = ProjectHelper.GetXMBHCondition(super.getXMBH());
			List<BDCS_DJDY_LS> djdys = dao.getDataList(BDCS_DJDY_LS.class, xmbhHql);
			if (djdys != null && djdys.size() > 0) {
				mashaller = JAXBContext.newInstance(Message.class).createMarshaller();
				Calendar calendar = Calendar.getInstance(); // 创建一个日历对象
				calendar.setTime(new Date()); // 用当前时间初始化日历时间
				String ywh = packageXml.GetYWLSHByYWH(this.getProject_id());
				BDCS_XMXX xmxx = dao.get(BDCS_XMXX.class, super.getXMBH());
				Message msg = exchangeFactory.createMessageByCFDJ();
				String result = "";
				String qlid = "";
				for (int i = 0; i < djdys.size(); i++) {
					BDCS_DJDY_LS djdy = djdys.get(i);
				
					//房屋
					if (djdy != null&&!"02".equals(djdy.getBDCDYLX())) {
						BDCS_QL_GZ ql = super.getQL(djdy.getDJDYID());
//						List<BDCS_QLR_GZ> qlrs = super.getQLRs(ql.getId());
						List<BDCS_SQR> sqrs = new ArrayList<BDCS_SQR>();
						ProjectServiceImpl serviceImpl = SuperSpringContext.getContext().getBean(ProjectServiceImpl.class);
						sqrs = serviceImpl.getSQRList(super.getXMBH());
				 
						BDCS_FSQL_GZ fsql = null;
						if (ql != null && !StringUtils.isEmpty(ql.getFSQLID())) {
							fsql = dao.get(BDCS_FSQL_GZ.class, ql.getFSQLID());
						}
						
						House h = null;
	                    if(djdy.getBDCDYLX().equals("032")){
	                    	 h = dao.get(BDCS_H_XZY.class, djdy.getBDCDYID());
	                    }else if(djdy.getBDCDYLX().equals("031")){
	                    	 h = dao.get(BDCS_H_XZ.class, djdy.getBDCDYID());
	                    }
						
						if(h.getZDBDCDYID()!= null){
							BDCS_SHYQZD_XZ zd = dao.get(BDCS_SHYQZD_XZ.class, h.getZDBDCDYID());
							if(zd != null){
								h.setZDDM(zd.getZDDM());
							}
						}else{
							System.out.println("获取不到关联宗地的ID！！！！！！");
						}
						
						super.fillHead(msg, i, ywh,h.getBDCDYH(),h.getQXDM(),ql.getLYQLID());
						msg.getHead().setParcelID(StringHelper.formatObject(h.getZDDM()));
						msg.getHead().setEstateNum(StringHelper.formatObject(h.getBDCDYH()));
//						msg.getHead().setRecType("RT430");
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
					}else {//宗地

						BDCS_QL_LS ql = super.getLSQL(djdy.getDJDYID());
//						List<BDCS_QLR_GZ> qlrs = super.getQLRs(ql.getId());
						List<BDCS_SQR> sqrs = new ArrayList<BDCS_SQR>();
						ProjectServiceImpl serviceImpl = SuperSpringContext.getContext().getBean(ProjectServiceImpl.class);
						sqrs = serviceImpl.getSQRList(super.getXMBH());
				 
						BDCS_FSQL_LS fsql = null;
						if (ql != null && !StringUtils.isEmpty(ql.getFSQLID())) {
							fsql = dao.get(BDCS_FSQL_LS.class, ql.getFSQLID());
						}
						BDCS_SHYQZD_XZ zd = dao.get(BDCS_SHYQZD_XZ.class, djdy.getBDCDYID());
						if(zd==null) {
							continue;
						}
						super.fillHead(msg, i, ywh,zd.getBDCDYH(),zd.getQXDM(),ql.getLYQLID());
						msg.getHead().setParcelID(StringHelper.formatObject(zd.getZDDM()));
						msg.getHead().setEstateNum(StringHelper.formatObject(zd.getBDCDYH()));
//						msg.getHead().setRecType("RT430");
						msg.getHead().setRecType("8000101");
//						msg.getHead().setPreEstateNum(StringHelper.formatObject(h.getBDCDYH()));
						if( !StringUtils.isEmpty(zd.getQXDM())){
							msg.getHead().setAreaCode(zd.getQXDM());
						}
						try {
							QLFQLCFDJ cfdj = msg.getData().getQLFQLCFDJ();
							cfdj = packageXml.getQLFQLCFDJ(cfdj,zd, null, ql, fsql, ywh,null);
							
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
					msg.getHead().setRightType("99");
					mashaller.marshal(msg, new File(path + "Biz" + msg.getHead().getBizMsgID() + ".xml"));
					result = uploadFile(path + "Biz" + msg.getHead().getBizMsgID() + ".xml", ConstValue.RECCODE.CF_CFDJ.Value,actinstID,djdy.getDJDYID(),qlid);
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
//				String lyqlid = bdcql.getLYQLID();//使用事物以后出错，此处注释，在同通用方法里面给导出对象赋值时 qlid=lyqlid
//				bdcql.setId(lyqlid);
				List<RightsHolder> bdcqlrs = null;
				MessageExport msg = super.getShareMsgTools().GetMsg(bdcdy, bdcql, bdcfsql, bdcqlrs, bljc, xmxx);
				super.getShareMsgTools().SendMsg(msg, idjdy + 1, djdy.getBDCDYLX(), xmxx);
			}
		}
	}

	@SuppressWarnings("unused")
	public void updateZD(String djdyid_h,String bdcdyid_h,String dbr,String xmbh)
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
		Rights ql_zd_old=null;
		 
	List<Rights> list_ql_zd_old=RightsTools.loadRightsByCondition(DJDYLY.XZ, "QLLX IN ('99') AND DJDYID='"+djdyid_zd+"' AND XMBH='"+xmbh+"'");
	if(list_ql_zd_old!=null&&list_ql_zd_old.size()>0){
		 ql_zd_old=list_ql_zd_old.get(0);
	}
	boolean succes= addbdcdy(ql_zd_old.getId());
	if(succes){
		String strSqlXMBH = ProjectHelper.GetXMBHCondition(getXMBH());
		BDCS_QL_GZ bdcs_ql_gz = null;
		// 获取工作库权利附属表
		String djlxFilter = String.format("  DJDYID='%s' AND DJLX='800' ORDER BY DJSJ ASC ", djdyid_zd);
		List<BDCS_QL_GZ> bdcs_ql_gzs = getCommonDao().getDataList(BDCS_QL_GZ.class, strSqlXMBH + " and " + djlxFilter);
		if (bdcs_ql_gzs != null && bdcs_ql_gzs.size() > 0) {
			bdcs_ql_gz = bdcs_ql_gzs.get(0);
		}
		BDCS_FSQL_GZ gzfsql = getCommonDao().get(BDCS_FSQL_GZ.class, bdcs_ql_gz.getFSQLID());
		 
			BDCS_QL_GZ	bdcs_ql_gzsh=null;
			String djlxFilters = String.format("  DJDYID='%s' AND DJLX='800' ORDER BY DJSJ ASC ", djdyid_h);
			List<BDCS_QL_GZ> bdcs_ql_gzssh = getCommonDao().getDataList(BDCS_QL_GZ.class, strSqlXMBH + " and " + djlxFilters);
			if (bdcs_ql_gzssh != null && bdcs_ql_gzssh.size() > 0) {
					bdcs_ql_gzsh = bdcs_ql_gzssh.get(0);
			}
			if (bdcs_ql_gzsh != null) {
				BDCS_FSQL_GZ fsqlh = getCommonDao().get(BDCS_FSQL_GZ.class, bdcs_ql_gzsh.getFSQLID());
				if (fsqlh != null) {
					gzfsql.setZXFJ(fsqlh.getZXFJ());
					gzfsql.setJFJG(fsqlh.getJFJG());
					gzfsql.setJFWH(fsqlh.getJFWH());
					gzfsql.setJFWJ(fsqlh.getJFWJ());
					gzfsql.setZXDYYWH(fsqlh.getZXDYYWH());
				}
			}
	  
		gzfsql.setZXDYYWH(super.getProject_id());
		gzfsql.setZXDBR(dbr);
		Date zxsj = new Date();
		gzfsql.setZXSJ(zxsj);
		getCommonDao().update(gzfsql);
		String xzqlid = bdcs_ql_gz.getLYQLID();
		BDCS_QL_XZ xzql = getCommonDao().get(BDCS_QL_XZ.class, xzqlid);
		if (xzql != null) {
			BDCS_FSQL_XZ xzfsql = getCommonDao().get(BDCS_FSQL_XZ.class, xzql.getFSQLID());
			if (xzfsql != null) {
				if("1".equals(ConfigHelper.getNameByValue("IFMODIFYTIME"))){
					StringBuilder builder = new StringBuilder();
					String yxlhsx = "";
					builder.append("select fsql.lhsx from bdck.bdcs_fsql_xz fsql ")
					       .append("left join bdck.bdcs_ql_xz ql on fsql.qlid = ql.qlid ")
					       .append("left join bdck.bdcs_djdy_xz djdy on ql.djdyid = djdy.djdyid ")
					       .append("where djdy.djdyid = '").append(xzql.getDJDYID())
					       .append("' and ql.djlx = '800' order by fsql.lhsx asc");
					List<Map> lhxslist = getCommonDao().getDataListByFullSql(builder.toString());
					if (lhxslist != null && lhxslist.size() > 1) {
						yxlhsx = StringHelper.formatObject(lhxslist.get(0).get("LHSX"));
					}
					if (!StringHelper.isEmpty(yxlhsx) && lhxslist.size() > 1) {
						Integer dqlhsx = xzfsql.getLHSX();  //当前轮候顺序号，判断此顺序号是否为有效查封的解封；是，修改下一手查封的QLQSSJ；否，搁置
						if (dqlhsx.toString().equals(yxlhsx)) {
							StringBuilder getqlid = new StringBuilder();
							getqlid.append("SELECT ql.qlid FROM bdck.bdcs_ql_xz ql ")
							       .append("LEFT JOIN bdck.bdcs_fsql_xz fsql ON fsql.qlid = ql.qlid ")
							       .append("WHERE fsql.lhsx='").append(StringHelper.formatObject(lhxslist.get(1).get("LHSX")))
							       .append("' AND fsql.djdyid='").append(xzql.getDJDYID()).append("'");
							List<Map> qlidlist = getCommonDao().getDataListByFullSql(getqlid.toString());
							if (qlidlist != null && qlidlist.size() > 0) {
								String nextqlid = StringHelper.formatObject(qlidlist.get(0).get("QLID"));
								BDCS_QL_GZ nextgzql = getCommonDao().get(BDCS_QL_GZ.class, nextqlid);
								BDCS_QL_XZ nextxzql = getCommonDao().get(BDCS_QL_XZ.class, nextqlid);
								BDCS_QL_LS nextlsql = getCommonDao().get(BDCS_QL_LS.class, nextqlid);
								nextgzql.setQLQSSJ(zxsj);
								nextxzql.setQLQSSJ(zxsj);
								nextlsql.setQLQSSJ(zxsj);
								getCommonDao().update(nextgzql);
								getCommonDao().update(nextxzql);
								getCommonDao().update(nextlsql);
								getCommonDao().flush();
							}
						}
					}
				}
				getCommonDao().deleteEntity(xzfsql);
			}
			getCommonDao().deleteEntity(xzql);
		}

		BDCS_QL_LS lsql = getCommonDao().get(BDCS_QL_LS.class, xzqlid);
		if (lsql != null) {
			BDCS_FSQL_LS lsfsql = getCommonDao().get(BDCS_FSQL_LS.class, lsql.getFSQLID());
			if (lsfsql != null) {
				lsfsql.setZXDYYWH(gzfsql.getZXDYYWH());
				lsfsql.setJFJG(gzfsql.getJFJG());
				lsfsql.setJFWJ(gzfsql.getJFWJ());
				lsfsql.setJFWH(gzfsql.getJFWH());
				lsfsql.setZXDBR(gzfsql.getZXDBR());
				lsfsql.setZXSJ(gzfsql.getZXSJ());
				lsfsql.setZXFJ(gzfsql.getZXFJ());
				getCommonDao().update(lsfsql);
				getCommonDao().update(lsql);
			}
		}

		// // 只将解封的工作层的权利表、权利附属表同步到现状层和历史层中。
		// this.CopyGZQLToXZAndLS(djdyid);
		// // 删除掉现状层、历史层中的查封信息。确保只保留一条解封信息（该信息保留有查封信息）
		// if (bdcs_ql_gz != null)
		// unLock(bdcs_ql_gz);
		// // 更新项目状态
		this.SetXMCFZT(djdyid_zd, "01");
	
	}
	}

	
}
