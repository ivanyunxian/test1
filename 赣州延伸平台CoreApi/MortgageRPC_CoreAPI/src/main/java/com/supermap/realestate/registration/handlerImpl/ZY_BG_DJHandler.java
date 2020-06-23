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
import com.supermap.realestate.registration.model.BDCS_SHYQZD_GZ;
import com.supermap.realestate.registration.model.BDCS_SHYQZD_LS;
import com.supermap.realestate.registration.model.BDCS_SHYQZD_XZ;
import com.supermap.realestate.registration.model.BDCS_SQR;
import com.supermap.realestate.registration.model.BDCS_SYQZD_GZ;
import com.supermap.realestate.registration.model.BDCS_SYQZD_XZ;
import com.supermap.realestate.registration.model.BDCS_TDYT_GZ;
import com.supermap.realestate.registration.model.BDCS_TDYT_XZ;
import com.supermap.realestate.registration.model.BDCS_XMXX;
import com.supermap.realestate.registration.model.BDCS_ZRZ_GZ;
import com.supermap.realestate.registration.model.BDCS_ZRZ_LS;
import com.supermap.realestate.registration.model.BDCS_ZRZ_XZ;
import com.supermap.realestate.registration.model.BDCS_ZS_GZ;
import com.supermap.realestate.registration.model.BDCS_ZS_XZ;
import com.supermap.realestate.registration.model.WFD_MAPPING;
import com.supermap.realestate.registration.model.YC_SC_H_XZ;
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
import com.supermap.realestate.registration.util.Global;
import com.supermap.realestate.registration.util.ObjectHelper;
import com.supermap.realestate.registration.util.ProjectHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.realestate.registration.util.YwLogUtil;
import com.supermap.realestate.registration.util.ConstValue.QLLX;
import com.supermap.wisdombusiness.core.SuperHelper;
import com.supermap.wisdombusiness.core.SuperSpringContext;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;

public class ZY_BG_DJHandler extends DJHandlerBase implements DJHandler {
	public ZY_BG_DJHandler(ProjectInfo info) {
		super(info);
	}

	public boolean addBDCDY(String bdcdyid) {
		boolean bsuccess = false;
		CommonDao dao = getCommonDao();

		String bdcdyid_house = getPrimaryKey();
		String bdcdyid_zrz = getPrimaryKey();
		String bdcdyid_shyqzd = getPrimaryKey();
		StringBuilder error = new StringBuilder();
		BDCS_H_GZ house = null;
		BDCS_ZRZ_GZ zrz = null;
		BDCS_SHYQZD_GZ shyqzd = null;
		house = (BDCS_H_GZ) CopyHouse(bdcdyid, bdcdyid_house);

		if (house != null) {
			house.setXMBH(getXMBH());
			zrz = (BDCS_ZRZ_GZ) CopyZRZ(house.getZRZBDCDYID(), bdcdyid_zrz);
			shyqzd = (BDCS_SHYQZD_GZ) CopyUseLand(house.getZDBDCDYID(),
					bdcdyid_shyqzd);
			if (zrz != null) {
				zrz.setXMBH(getXMBH());
				dao.save(zrz);
			} else {
				error.append("该房屋未关联自然幢。");
			}
			if (shyqzd != null) {
				shyqzd.setXMBH(getXMBH());
				dao.save(shyqzd);
			} else {
				error.append("该房屋未关联宗地。");
			}
			house.setZRZBDCDYID(bdcdyid_zrz);
			house.setZDBDCDYID(bdcdyid_shyqzd);
			dao.save(house);
		} else {
			super.setErrMessage("添加失败！");
			return false;
		}
		super.setErrMessage(error.toString());

		BDCS_DJDY_GZ djdy = super.createDJDYfromXZ(bdcdyid);
		if (djdy != null) {
			RealUnit unit = null;
			BDCS_QL_GZ ql_gz = super.createQL(djdy, unit);

			BDCS_FSQL_GZ fsql = super.createFSQL(djdy.getDJDYID());
			fsql.setQLID(ql_gz.getId());
			ql_gz.setFSQLID(fsql.getId());

			if (shyqzd != null) {
				fsql.setSYQMJ(shyqzd.getZDMJ());
				ql_gz.setQDJG(shyqzd.getJG());
			}

			String qllxarray = "('1','2','3','4','5','6','7','8','9','10','11','12','13','14','15','16','17','18')";
			String hql = " DJDYID='" + djdy.getDJDYID() + "' AND QLLX IN "
					+ qllxarray;
			String lyqlid = "";
			List<BDCS_QL_XZ> list = dao.getDataList(BDCS_QL_XZ.class, hql);
			if ((list != null) && (list.size() > 0)) {
				lyqlid = ((BDCS_QL_XZ) list.get(0)).getId();
				ql_gz.setLYQLID(lyqlid);
				ql_gz.setQLQSSJ(((BDCS_QL_XZ) list.get(0)).getQLQSSJ());
				ql_gz.setQLJSSJ(((BDCS_QL_XZ) list.get(0)).getQLJSSJ());

				StringBuilder builderZSALL = new StringBuilder();
				builderZSALL.append(" id IN (");
				builderZSALL
						.append("select ZSID FROM BDCS_QDZR_XZ WHERE QLID ='");
				builderZSALL.append(lyqlid).append("')");
				String strQueryZSALL = builderZSALL.toString();
				List<BDCS_ZS_XZ> zssALL = getCommonDao().getDataList(BDCS_ZS_XZ.class,
						strQueryZSALL);

				if (zssALL.size() > 1) {
					ql_gz.setCZFS(ConstValue.CZFS.FBCZ.Value);
				} else if ((StringHelper.isEmpty(ql_gz.getCZFS()))
						|| ((!ql_gz.getCZFS()
								.equals(ConstValue.CZFS.FBCZ.Value)) && (!ql_gz
								.getCZFS().equals(ConstValue.CZFS.GTCZ.Value)))) {
					ql_gz.setCZFS(ConstValue.CZFS.GTCZ.Value);
				}

				String newqzh = "";
				String workflowcode = ProjectHelper
						.getWorkflowCodeByProjectID(getProject_id());
				List<WFD_MAPPING> listCode = getCommonDao().getDataList(WFD_MAPPING.class,
						"WORKFLOWCODE='" + workflowcode + "'");
				if ((listCode != null) && (listCode.size() > 0)) {
					newqzh = ((WFD_MAPPING) listCode.get(0)).getNEWQZH();
				}

				HashMap<String,String> lyzsid_zsid = new HashMap<String,String>();

				List<BDCS_QLR_XZ> qlrs = getCommonDao().getDataList(BDCS_QLR_XZ.class,
						" QLID='" + lyqlid + "'");
				if ((qlrs != null) && (qlrs.size() > 0)) {
					for (int iqlr = 0; iqlr < qlrs.size(); iqlr++) {
						BDCS_QLR_XZ bdcs_qlr_xz = (BDCS_QLR_XZ) qlrs.get(iqlr);
						if (bdcs_qlr_xz == null)
							continue;
						String gzqlrid = getPrimaryKey();
						BDCS_QLR_GZ bdcs_qlr_gz = ObjectHelper
								.copyQLR_XZToGZ(bdcs_qlr_xz);
						bdcs_qlr_gz.setId(gzqlrid);
						bdcs_qlr_gz.setQLID(ql_gz.getId());
						bdcs_qlr_gz.setXMBH(getXMBH());
						if (ConstValue.SF.YES.Value.equals(newqzh)) {
							bdcs_qlr_gz.setBDCQZHXH("");
							bdcs_qlr_gz.setBDCQZH("");
						}
						getCommonDao().save(bdcs_qlr_gz);

						StringBuilder builder = new StringBuilder();
						builder.append(" id IN (");
						builder.append("select ZSID FROM BDCS_QDZR_XZ WHERE QLID ='");
						builder.append(lyqlid).append("'")
								.append(" AND QLRID='");
						builder.append(bdcs_qlr_xz.getId()).append("')");
						String strQueryZS = builder.toString();
						List<BDCS_ZS_XZ> zss = getCommonDao().getDataList(BDCS_ZS_XZ.class,
								strQueryZS);
						if ((zss != null) && (zss.size() > 0)) {
							BDCS_ZS_XZ bdcs_zs_xz = (BDCS_ZS_XZ) zss.get(0);
							if (!lyzsid_zsid.containsKey(bdcs_zs_xz.getId())) {
								String gzzsid = getPrimaryKey();
								BDCS_ZS_GZ bdcs_zs_gz = ObjectHelper
										.copyZS_XZToGZ(bdcs_zs_xz);
								bdcs_zs_gz.setId(gzzsid);
								bdcs_zs_gz.setXMBH(getXMBH());
								if (ConstValue.SF.YES.Value.equals(newqzh)) {
									bdcs_zs_gz.setBDCQZH("");
									bdcs_zs_gz.setZSBH("");
								}
								getCommonDao().save(bdcs_zs_gz);
								lyzsid_zsid.put(bdcs_zs_xz.getId(), gzzsid);
							}

							StringBuilder builderQDZR = new StringBuilder();
							builderQDZR.append(" QLID='").append(lyqlid)
									.append("'");
							builderQDZR.append(" AND ZSID='");
							builderQDZR.append(bdcs_zs_xz.getId());
							builderQDZR.append("' AND QLID='");
							builderQDZR.append(lyqlid);
							builderQDZR.append("' AND QLRID='");
							builderQDZR.append(bdcs_qlr_xz.getId());
							builderQDZR.append("')");
							List<BDCS_QDZR_XZ> qdzrs = getCommonDao().getDataList(
									BDCS_QDZR_XZ.class, builderQDZR.toString());
							if ((qdzrs != null) && (qdzrs.size() > 0)) {
								for (int iqdzr = 0; iqdzr < qdzrs.size(); iqdzr++) {
									BDCS_QDZR_XZ bdcs_qdzr_xz = (BDCS_QDZR_XZ) qdzrs
											.get(iqdzr);
									if (bdcs_qdzr_xz == null)
										continue;
									BDCS_QDZR_GZ bdcs_qdzr_gz = ObjectHelper
											.copyQDZR_XZToGZ(bdcs_qdzr_xz);
									bdcs_qdzr_gz.setId(getPrimaryKey());
									bdcs_qdzr_gz.setZSID((String) lyzsid_zsid
											.get(bdcs_zs_xz.getId()));
									bdcs_qdzr_gz.setQLID(ql_gz.getId());
									bdcs_qdzr_gz.setFSQLID(fsql.getId());
									bdcs_qdzr_gz.setQLRID(gzqlrid);
									bdcs_qdzr_gz.setXMBH(getXMBH());
									getCommonDao().save(bdcs_qdzr_gz);
								}

							}

						}

					}

				}

			}

			dao.save(ql_gz);
			dao.save(fsql);

			djdy.setBDCDYID(bdcdyid_house);
			djdy.setId(getPrimaryKey());
			djdy.setLY(ConstValue.DJDYLY.GZ.Value);
			dao.save(djdy);

			super.CopySQRFromXZQLR(djdy.getDJDYID(), ql_gz.getQLLX(),
					getXMBH(), ql_gz.getId(), ConstValue.SQRLB.YF.Value);

			CopyZYYGQLRToSQR(ql_gz.getId(), djdy.getDJDYID());
		}
		dao.flush();
		bsuccess = true;
		return bsuccess;
	}

	public RealUnit CopyZRZ(String bdcdyid, String bdcdyid_new) {
		RealUnit unit = null;
		BDCS_ZRZ_XZ dy_xz = (BDCS_ZRZ_XZ) getCommonDao().get(BDCS_ZRZ_XZ.class,
				bdcdyid);
		if (dy_xz != null) {
			BDCS_ZRZ_GZ dy_gz = new BDCS_ZRZ_GZ();
			try {
				PropertyUtils.copyProperties(dy_gz, dy_xz);
				unit = dy_gz;
				unit.setId(bdcdyid_new);
			} catch (Exception localException) {
			}
		}
		return unit;
	}

	public RealUnit CopyHouse(String bdcdyid, String bdcdyid_new) {
		RealUnit unit = null;
		BDCS_H_XZ dy_xz = (BDCS_H_XZ) getCommonDao().get(BDCS_H_XZ.class,
				bdcdyid);
		if (dy_xz != null) {
			BDCS_H_GZ dy_gz = new BDCS_H_GZ();
			try {
				PropertyUtils.copyProperties(dy_gz, dy_xz);
				unit = dy_gz;
				unit.setId(bdcdyid_new);
			} catch (Exception localException) {
			}
		}
		return unit;
	}

	public RealUnit CopyUseLand(String bdcdyid, String bdcdyid_new) {
		RealUnit unit = null;
		BDCS_SHYQZD_XZ dy_xz = (BDCS_SHYQZD_XZ) getCommonDao().get(
				BDCS_SHYQZD_XZ.class, bdcdyid);
		if (dy_xz != null) {
			BDCS_SHYQZD_GZ dy_gz = new BDCS_SHYQZD_GZ();
			try {
				PropertyUtils.copyProperties(dy_gz, dy_xz);
				unit = dy_gz;
				unit.setId(bdcdyid_new);

				String hqlCondition = "BDCDYID='" + bdcdyid + "'";

				getCommonDao().deleteEntitysByHql(BDCS_TDYT_GZ.class,
						hqlCondition);

				List<BDCS_TDYT_XZ> desListTDYT = getCommonDao().getDataList(
						BDCS_TDYT_XZ.class, hqlCondition);
				if ((desListTDYT != null) && (desListTDYT.size() > 0))
					for (BDCS_TDYT_XZ tdyt : desListTDYT) {
						BDCS_TDYT_GZ yt_gz = new BDCS_TDYT_GZ();
						try {
							PropertyUtils.copyProperties(yt_gz, tdyt);
							yt_gz.setId((String) SuperHelper
									.GeneratePrimaryKey());
							yt_gz.setBDCDYID(bdcdyid_new);
							getCommonDao().save(yt_gz);
						} catch (Exception localException) {
						}
					}
			} catch (Exception localException1) {
			}
		}
		return unit;
	}

	public RealUnit CopyOwnershipLand(String bdcdyid, String bdcdyid_new) {
		RealUnit unit = null;
		BDCS_SYQZD_XZ dy_xz = (BDCS_SYQZD_XZ) getCommonDao().get(
				BDCS_SYQZD_XZ.class, bdcdyid);
		if (dy_xz != null) {
			BDCS_SYQZD_GZ dy_gz = new BDCS_SYQZD_GZ();
			try {
				PropertyUtils.copyProperties(dy_gz, dy_xz);
				unit = dy_gz;
				unit.setId(bdcdyid_new);

				String hqlCondition = "BDCDYID='" + bdcdyid + "'";

				getCommonDao().deleteEntitysByHql(BDCS_TDYT_GZ.class,
						hqlCondition);

				List<BDCS_TDYT_XZ> desListTDYT = getCommonDao().getDataList(
						BDCS_TDYT_XZ.class, hqlCondition);
				if ((desListTDYT != null) && (desListTDYT.size() > 0))
					for (BDCS_TDYT_XZ tdyt : desListTDYT) {
						BDCS_TDYT_GZ yt_gz = new BDCS_TDYT_GZ();
						try {
							PropertyUtils.copyProperties(yt_gz, tdyt);
							yt_gz.setId((String) SuperHelper
									.GeneratePrimaryKey());
							yt_gz.setBDCDYID(bdcdyid_new);
							getCommonDao().save(yt_gz);
						} catch (Exception localException) {
						}
					}
			} catch (Exception localException1) {
			}
		}
		return unit;
	}

	public boolean writeDJB() {
		if (super.isCForCFING()) {
			return false;
		}
		if (!YCYGCanecl()) {
			return false;
		}
		if (!ZYYGCanecl()) {
			return false;
		}
		String strSqlXMBH = ProjectHelper.GetXMBHCondition(getXMBH());
		List<BDCS_DJDY_GZ> djdys = getCommonDao().getDataList(BDCS_DJDY_GZ.class, strSqlXMBH);
		if ((djdys != null) && (djdys.size() > 0)) {
			for (int idjdy = 0; idjdy < djdys.size(); idjdy++) {
				BDCS_DJDY_GZ bdcs_djdy_gz = (BDCS_DJDY_GZ) djdys.get(idjdy);

				if (bdcs_djdy_gz != null) {
					List<BDCS_DJDY_XZ> xz_djdys = getCommonDao().getDataList(
							BDCS_DJDY_XZ.class,
							" DJDYID='" + bdcs_djdy_gz.getDJDYID() + "'");
					if ((xz_djdys != null) && (xz_djdys.size() > 0)) {
						BDCS_DJDY_XZ bdcs_djdy_xz = (BDCS_DJDY_XZ) xz_djdys
								.get(0);
						BDCS_H_GZ house_gz = (BDCS_H_GZ) getCommonDao().get(
								BDCS_H_GZ.class, bdcs_djdy_gz.getBDCDYID());
						BDCS_H_XZ house_xz = (BDCS_H_XZ) getCommonDao().get(
								BDCS_H_XZ.class, bdcs_djdy_xz.getBDCDYID());

						CopySHYQZDToXZAndLS(bdcs_djdy_gz, bdcs_djdy_xz,
								house_gz, house_xz);

						CopyZRZToXZAndLS(bdcs_djdy_gz, bdcs_djdy_xz, house_gz,
								house_xz);

						CopyHouseToXZAndLS(bdcs_djdy_gz, bdcs_djdy_xz,
								house_gz, house_xz);
					}

					String djdyid = bdcs_djdy_gz.getDJDYID();
					super.removeQLFromXZByQLLX(djdyid);
					super.removeQLRFromXZByQLLX(djdyid);
					super.removeQDZRFromXZByQLLX(djdyid);
					super.removeZSFromXZByQLLX(djdyid);
					super.removeDJDYFromXZ(djdyid);

					super.CopyGZQLToXZAndLS(djdyid);
					super.CopyGZQLRToXZAndLS(djdyid);
					super.CopyGZQDZRToXZAndLS(djdyid);
					super.CopyGZZSToXZAndLS(djdyid);
					super.CopyGZDJDYToXZAndLS(djdyid);
				}
			}
		}
		SetSFDB();
		getCommonDao().flush();
		super.alterCachedXMXX();
		return true;
	}

	private void CopyHouseToXZAndLS(BDCS_DJDY_GZ house_djdy_gz,
			BDCS_DJDY_XZ house_djdy_xz, BDCS_H_GZ house_gz, BDCS_H_XZ house_xz) {
		BDCS_H_LS house_ls_new = ObjectHelper.copyH_XZToLS(house_xz);
		house_ls_new.setId(house_gz.getId());
		BDCS_H_XZ house_xz_update = ObjectHelper.copyH_GZToXZ(house_gz);
		house_xz_update.setId(house_xz.getId());
		BDCS_H_LS house_ls_update = ObjectHelper.copyH_XZToLS(house_xz_update);
		getCommonDao().update(house_xz_update);
		getCommonDao().update(house_ls_update);
		getCommonDao().save(house_ls_new);

		RebuildDYBG(house_djdy_gz.getBDCDYID(), house_djdy_gz.getDJDYID(),
				house_djdy_xz.getBDCDYID(), house_djdy_xz.getDJDYID(),
				house_djdy_gz.getCreateTime(), house_djdy_gz.getModifyTime());
	}

	private void CopyZRZToXZAndLS(BDCS_DJDY_GZ house_djdy_gz,
			BDCS_DJDY_XZ house_djdy_xz, BDCS_H_GZ house_gz, BDCS_H_XZ house_xz) {
		BDCS_ZRZ_GZ zrz_gz = (BDCS_ZRZ_GZ) getCommonDao().get(
				BDCS_ZRZ_GZ.class, house_gz.getZRZBDCDYID());
		if (zrz_gz == null)
			return;
		BDCS_ZRZ_XZ zrz_ls_xz = (BDCS_ZRZ_XZ) getCommonDao().get(
				BDCS_ZRZ_XZ.class, house_xz.getZRZBDCDYID());
		BDCS_ZRZ_LS zrz_ls_new = ObjectHelper.copyZRZ_XZToLS(zrz_ls_xz);
		zrz_ls_new.setId(house_gz.getZRZBDCDYID());
		BDCS_ZRZ_XZ zrz_xz_update = ObjectHelper.copyZRZ_GZToXZ(zrz_gz);
		zrz_xz_update.setId(house_xz.getZRZBDCDYID());

		BDCS_ZRZ_LS zrz_ls_update = ObjectHelper.copyZRZ_XZToLS(zrz_xz_update);

		getCommonDao().update(zrz_xz_update);
		getCommonDao().update(zrz_ls_update);
		getCommonDao().save(zrz_ls_new);

		RebuildDYBG(zrz_gz.getId(), "", zrz_xz_update.getId(), "",
				house_djdy_gz.getCreateTime(), house_djdy_gz.getModifyTime());
	}

	private void CopySHYQZDToXZAndLS(BDCS_DJDY_GZ house_djdy_gz,
			BDCS_DJDY_XZ house_djdy_xz, BDCS_H_GZ house_gz, BDCS_H_XZ house_xz) {
		BDCS_SHYQZD_GZ shyqzd_gz = (BDCS_SHYQZD_GZ) getCommonDao().get(
				BDCS_SHYQZD_GZ.class, house_gz.getZDBDCDYID());
		if (shyqzd_gz == null)
			return;
		BDCS_SHYQZD_XZ shyqzd_ls_xz = (BDCS_SHYQZD_XZ) getCommonDao().get(
				BDCS_SHYQZD_XZ.class, house_xz.getZDBDCDYID());
		BDCS_SHYQZD_LS shyqzd_ls_new = ObjectHelper
				.copySHYQZD_XZToLS(shyqzd_ls_xz);
		shyqzd_ls_new.setId(house_gz.getZDBDCDYID());
		BDCS_SHYQZD_XZ shyqzd_xz_update = ObjectHelper
				.copySHYQZD_GZToXZ(shyqzd_gz);
		shyqzd_xz_update.setId(house_xz.getZDBDCDYID());
		BDCS_SHYQZD_LS shyqzd_ls_update = ObjectHelper
				.copySHYQZD_XZToLS(shyqzd_xz_update);
		BDCS_DJDY_XZ shyqzd_djdy_xz = null;
		List<BDCS_DJDY_XZ> shyqzd_djdys = getCommonDao().getDataList(BDCS_DJDY_XZ.class,
				"BDCDYID='" + house_xz.getZDBDCDYID() + "'");
		if ((shyqzd_djdys != null) && (shyqzd_djdys.size() > 0)) {
			shyqzd_djdy_xz = (BDCS_DJDY_XZ) shyqzd_djdys.get(0);
		}
		if (shyqzd_djdy_xz == null) {
			shyqzd_djdy_xz = new BDCS_DJDY_XZ();
		}
		getCommonDao().update(shyqzd_xz_update);
		getCommonDao().update(shyqzd_ls_update);
		getCommonDao().save(shyqzd_ls_new);

		RebuildDYBG(shyqzd_gz.getId(), shyqzd_djdy_xz.getDJDYID(),
				shyqzd_xz_update.getId(), shyqzd_djdy_xz.getDJDYID(),
				house_djdy_gz.getCreateTime(), house_djdy_gz.getModifyTime());

		BDCS_DJDY_LS shyqzd_djdy_ls_new = ObjectHelper
				.copyDJDY_XZToLS(shyqzd_djdy_xz);
		String shyqzd_djdy_ls_new_id = getPrimaryKey();

		shyqzd_djdy_ls_new.setId(shyqzd_djdy_ls_new_id);
		shyqzd_djdy_ls_new.setBDCDYID(shyqzd_gz.getId());

		List<BDCS_QL_XZ> zd_qls = getCommonDao().getDataList(
				BDCS_QL_XZ.class,
				"DJDYID='" + shyqzd_djdy_xz.getDJDYID()
						+ "' AND QLLX IN ('3','5','7') ");
		if ((zd_qls != null) && (zd_qls.size() > 0))
			for (int iql = 0; iql < zd_qls.size(); iql++) {
				String zd_ql_ls_new_qlid = getPrimaryKey();

				String zd_fsql_ls_new_fsqlid = getPrimaryKey();
				BDCS_QL_XZ zd_ql_xz = (BDCS_QL_XZ) zd_qls.get(iql);

				BDCS_QL_LS zd_ql_ls_new = ObjectHelper.copyQL_XZToLS(zd_ql_xz);

				zd_ql_ls_new.setId(zd_ql_ls_new_qlid);
				zd_ql_ls_new.setFSQLID(zd_fsql_ls_new_fsqlid);
				zd_ql_xz.setDBR(Global.getCurrentUserName());
				zd_ql_xz.setDJSJ(new Date());

				getCommonDao().update(zd_ql_xz);
				getCommonDao().save(zd_ql_ls_new);

				if (zd_ql_xz.getFSQLID() != null) {
					BDCS_FSQL_XZ zd_fsql_xz = (BDCS_FSQL_XZ) getCommonDao()
							.get(BDCS_FSQL_XZ.class, zd_ql_xz.getFSQLID());
					if (zd_fsql_xz != null) {
						BDCS_FSQL_LS zd_fsql_ls_new = ObjectHelper
								.copyFSQL_XZToLS(zd_fsql_xz);

						zd_fsql_ls_new.setId(zd_fsql_ls_new_fsqlid);
						zd_fsql_ls_new.setQLID(zd_ql_ls_new_qlid);

						zd_fsql_ls_new.setZXDBR(Global.getCurrentUserName());
						zd_fsql_ls_new.setZXSJ(new Date());
						zd_fsql_ls_new.setZXFJ("走转移变更流程注销");
						getCommonDao().save(zd_fsql_ls_new);
					}

				}

				List<BDCS_QLR_XZ> zd_qlrs = getCommonDao().getDataList(BDCS_QLR_XZ.class,
						"QLID='" + zd_ql_xz.getId() + "'");
				if ((zd_qlrs != null) && (zd_qlrs.size() > 0))
					for (int iqlr = 0; iqlr < zd_qlrs.size(); iqlr++) {
						BDCS_QLR_XZ zd_qlr_xz = (BDCS_QLR_XZ) zd_qlrs.get(iqlr);
						BDCS_QLR_LS zd_qlr_ls_new = ObjectHelper
								.copyQLR_XZToLS(zd_qlr_xz);

						String zd_qlr_ls_new_id = getPrimaryKey();
						zd_qlr_ls_new.setQLID(zd_ql_ls_new_qlid);
						zd_qlr_ls_new.setId(zd_qlr_ls_new_id);
						getCommonDao().save(zd_qlr_ls_new);

						List<BDCS_QDZR_XZ> zd_xz_qdzrs = getCommonDao().getDataList(
								BDCS_QDZR_XZ.class,
								"QLID='" + zd_ql_xz.getId() + "'  AND QLRID='"
										+ zd_qlr_xz.getId() + "'");
						if ((zd_xz_qdzrs != null) && (zd_xz_qdzrs.size() > 0))
							for (int iqdzr = 0; iqdzr < zd_xz_qdzrs.size(); iqdzr++) {
								BDCS_QDZR_XZ zd_qdzr_xz = (BDCS_QDZR_XZ) zd_xz_qdzrs
										.get(iqdzr);

								BDCS_QDZR_LS zd_qdzr_ls_new = ObjectHelper
										.copyQDZR_XZToLS(zd_qdzr_xz);
								zd_qdzr_ls_new.setId(getPrimaryKey());
								zd_qdzr_ls_new.setQLID(zd_ql_ls_new_qlid);
								zd_qdzr_ls_new.setFSQLID(zd_fsql_ls_new_fsqlid);
								zd_qdzr_ls_new.setQLRID(zd_qlr_ls_new_id);

								getCommonDao().save(zd_qdzr_ls_new);
							}
					}
			}
	}

	protected boolean RebuildDYBG(String lbdcdyid, String ldjdyid,
			String xbdcdyid, String xdjdyid, Date createtime, Date modifytime) {
		BDCS_DYBG bdcs_dybg = new BDCS_DYBG();
		bdcs_dybg.setCreateTime(createtime);
		bdcs_dybg.setLBDCDYID(lbdcdyid);
		bdcs_dybg.setLDJDYID(ldjdyid);
		bdcs_dybg.setModifyTime(modifytime);
		bdcs_dybg.setXBDCDYID(xbdcdyid);
		bdcs_dybg.setXDJDYID(xdjdyid);
		bdcs_dybg.setXMBH(getXMBH());
		getCommonDao().save(bdcs_dybg);
		return true;
	}

	protected boolean CopyYCYGQLRToSQR(String qlid) {
		if (!ConstValue.BDCDYLX.H.equals(super.getBdcdylx())) {
			return true;
		}
		String strSqlXMBH = ProjectHelper.GetXMBHCondition(getXMBH());
		List<BDCS_DJDY_GZ> djdys = getCommonDao().getDataList(
				BDCS_DJDY_GZ.class, strSqlXMBH);
		if ((djdys != null) && (djdys.size() > 0)) {
			for (BDCS_DJDY_GZ djdy : djdys) {
				String SCBDCDYID = djdy.getBDCDYID();
				List<YC_SC_H_XZ> listGX = getCommonDao().getDataList(
						YC_SC_H_XZ.class, "SCBDCDYID='" + SCBDCDYID + "'");
				if ((listGX != null) && (listGX.size() > 0)) {
					for (YC_SC_H_XZ gx : listGX) {
						String YCBDCDYID = gx.getYCBDCDYID();
						if (StringHelper.isEmpty(YCBDCDYID)) {
							continue;
						}
						List<BDCS_DJDY_XZ> djdys_yc = getCommonDao().getDataList(
								BDCS_DJDY_XZ.class,
								"BDCDYID='" + YCBDCDYID + "'");
						if ((djdys_yc == null) || (djdys_yc.size() <= 0)) {
							continue;
						}
						String ycdjdyid = ((BDCS_DJDY_XZ) djdys_yc.get(0))
								.getDJDYID();
						List<Rights> ycyg_qls = RightsTools
								.loadRightsByCondition(
										ConstValue.DJDYLY.XZ,
										"DJDYID='"
												+ ycdjdyid
												+ "' AND DJLX='700' AND QLLX='4'");
						if ((ycyg_qls != null) && (ycyg_qls.size() > 0)) {
							for (Rights ycyg_ql : ycyg_qls) {
								List<RightsHolder> zyyg_qlrs = RightsHolderTools
										.loadRightsHolders(
												ConstValue.DJDYLY.XZ,
												ycyg_ql.getId());
								if ((zyyg_qlrs != null)
										&& (zyyg_qlrs.size() > 0)) {
									for (RightsHolder zyyg_qlr : zyyg_qlrs) {
										BDCS_QLR_XZ qlr = (BDCS_QLR_XZ) zyyg_qlr;
										BDCS_SQR sqr = super.copyXZQLRtoSQR(
												qlr, ConstValue.SQRLB.JF);
										if (sqr != null) {
											sqr.setGLQLID(qlid);
											getCommonDao().save(sqr);
										}
									}
								}
							}
						}
					}
				}
			}
		}

		return true;
	}

	protected boolean CopyZYYGQLRToSQR(String qlid, String djdyid) {
		List<Rights> zyyg_qls = RightsTools.loadRightsByCondition(
				ConstValue.DJDYLY.XZ, "DJDYID='" + djdyid
						+ "' AND DJLX='700' AND QLLX='99'");
		if ((zyyg_qls != null) && (zyyg_qls.size() > 0)) {
			for (Rights zyyg_ql : zyyg_qls) {
				List<RightsHolder> zyyg_qlrs = RightsHolderTools
						.loadRightsHolders(ConstValue.DJDYLY.XZ,
								zyyg_ql.getId());
				if ((zyyg_qlrs != null) && (zyyg_qlrs.size() > 0)) {
					for (RightsHolder zyyg_qlr : zyyg_qlrs) {
						BDCS_QLR_XZ qlr = (BDCS_QLR_XZ) zyyg_qlr;
						BDCS_SQR sqr = super.copyXZQLRtoSQR(qlr,
								ConstValue.SQRLB.JF);
						if (sqr != null) {
							sqr.setGLQLID(qlid);
							getCommonDao().save(sqr);
						}
					}
				}
			}
		}
		return true;
	}

	private boolean YCYGCanecl() {
		boolean bCancel = true;
		if (!ConstValue.BDCDYLX.H.equals(super.getBdcdylx())) {
			return true;
		}
		String dbr = Global.getCurrentUserName();
		String strSqlXMBH = ProjectHelper.GetXMBHCondition(getXMBH());
		List<BDCS_DJDY_GZ> djdys = getCommonDao().getDataList(
				BDCS_DJDY_GZ.class, strSqlXMBH);
		if ((djdys != null) && (djdys.size() > 0)) {
			for (BDCS_DJDY_GZ djdy : djdys) {
				String SCBDCDYID = djdy.getBDCDYID();
				List<YC_SC_H_XZ> listGX = getCommonDao().getDataList(
						YC_SC_H_XZ.class, "SCBDCDYID='" + SCBDCDYID + "'");
				if ((listGX != null) && (listGX.size() > 0)) {
					for (YC_SC_H_XZ gx : listGX) {
						String YCBDCDYID = gx.getYCBDCDYID();
						if (StringHelper.isEmpty(YCBDCDYID)) {
							continue;
						}
						List<BDCS_DJDY_XZ> djdys_yc = getCommonDao().getDataList(
								BDCS_DJDY_XZ.class,
								"BDCDYID='" + YCBDCDYID + "'");
						if ((djdys_yc == null) || (djdys_yc.size() <= 0)) {
							continue;
						}
						String ycdjdyid = ((BDCS_DJDY_XZ) djdys_yc.get(0))
								.getDJDYID();
						List<Rights> ycyg_qls = RightsTools
								.loadRightsByCondition(
										ConstValue.DJDYLY.XZ,
										"DJDYID='"
												+ ycdjdyid
												+ "' AND DJLX='700' AND QLLX='4'");
						if ((ycyg_qls != null) && (ycyg_qls.size() > 0)) {
							for (Rights ycyg_ql : ycyg_qls) {
								String qlid = ycyg_ql.getId();
								RightsTools.deleteRightsAll(
										ConstValue.DJDYLY.XZ, qlid);

								SubRights subright = RightsTools
										.loadSubRightsByRightsID(
												ConstValue.DJDYLY.LS, qlid);
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

	private boolean ZYYGCanecl() {
		boolean bCancel = true;
		String dbr = Global.getCurrentUserName();
		String strSqlXMBH = ProjectHelper.GetXMBHCondition(getXMBH());
		List<BDCS_DJDY_GZ> djdys = getCommonDao().getDataList(
				BDCS_DJDY_GZ.class, strSqlXMBH);
		if ((djdys != null) && (djdys.size() > 0)) {
			List<Rights> zyyg_qls;
			for (BDCS_DJDY_GZ djdy : djdys) {
				List<String> list_qlrmc_zjh = new ArrayList<String>();
				List<RightsHolder> qlrs = RightsHolderTools
						.loadRightsHolders(ConstValue.DJDYLY.GZ,
								djdy.getDJDYID(), super.getXMBH());
				if ((qlrs != null) && (qlrs.size() > 0)) {
					for (RightsHolder qlr : qlrs) {
						list_qlrmc_zjh.add(qlr.getQLRMC() + "&"
								+ StringHelper.formatObject(qlr.getZJH()));
					}
				}
				zyyg_qls = RightsTools.loadRightsByCondition(
						ConstValue.DJDYLY.XZ, "DJDYID='" + djdy.getDJDYID()
								+ "' AND DJLX='700' AND QLLX='99'");
				if ((zyyg_qls != null) && (zyyg_qls.size() > 0)) {
					for (Rights zyyg_ql : zyyg_qls) {
						List<RightsHolder> zyyg_qlrs = RightsHolderTools
								.loadRightsHolders(ConstValue.DJDYLY.XZ,
										zyyg_ql.getId());
						if ((zyyg_qlrs != null) && (zyyg_qlrs.size() > 0)) {
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
				List<Rights> zyyg_qls2 = RightsTools.loadRightsByCondition(
						ConstValue.DJDYLY.XZ, "DJDYID='" + djdy.getDJDYID()
								+ "' AND DJLX='700' AND QLLX='99'");
				if ((zyyg_qls2 != null) && (zyyg_qls2.size() > 0)) {
					for (Rights zyyg_ql : zyyg_qls2) {
						String qlid = zyyg_ql.getId();
						RightsTools.deleteRightsAll(ConstValue.DJDYLY.XZ, qlid);

						SubRights subright = RightsTools
								.loadSubRightsByRightsID(ConstValue.DJDYLY.LS,
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
			getCommonDao().flush();
		}
		return bCancel;
	}

	public boolean removeBDCDY(String bdcdyid) {
		boolean bsuccess = false;

		CommonDao baseCommonDao = getCommonDao();

		BDCS_H_GZ house_gz = (BDCS_H_GZ) baseCommonDao.get(BDCS_H_GZ.class,
				bdcdyid);
		if (house_gz != null) {
			BDCS_ZRZ_GZ zrz_gz = (BDCS_ZRZ_GZ) baseCommonDao.get(
					BDCS_ZRZ_GZ.class, house_gz.getZRZBDCDYID());
			BDCS_SHYQZD_GZ shyqzd_gz = (BDCS_SHYQZD_GZ) baseCommonDao.get(
					BDCS_SHYQZD_GZ.class, house_gz.getZDBDCDYID());
			baseCommonDao.deleteEntity(house_gz);
			if (zrz_gz != null)
				baseCommonDao.deleteEntity(zrz_gz);
			if (shyqzd_gz != null)
				baseCommonDao.deleteEntity(shyqzd_gz);
		}
		BDCS_DJDY_GZ djdy = super.removeDJDY(bdcdyid);
		if (djdy != null) {
			String djdyid = djdy.getDJDYID();

			super.RemoveSQRByQLID(djdyid, getXMBH());

			String _hqlCondition = MessageFormat.format(
					"XMBH=''{0}'' AND DJDYID=''{1}''", new Object[] {
							getXMBH(), djdyid });
			RightsTools.deleteRightsAllByCondition(ConstValue.DJDYLY.GZ,
					_hqlCondition);
		}
		baseCommonDao.flush();
		bsuccess = true;
		return bsuccess;
	}

	public List<UnitTree> getUnitTree() {
		return super.getUnitList();
	}

	public void addQLRbySQRArray(String qlid, Object[] sqrids) {
		super.addQLRbySQRs(qlid, sqrids);
	}

	public void removeQLR(String qlid, String qlrid) {
		super.removeqlr(qlrid, qlid);
	}

	public String getError() {
		return super.getErrMessage();
	}

	public Map<String, String> exportXML(String path, String actinstID) {

		Marshaller mashaller;
		Map<String, String> names = new HashMap<String, String>();
		try {
			CommonDao dao = super.getCommonDao();
			String xmbhHql = ProjectHelper.GetXMBHCondition(super.getXMBH());
			List<BDCS_DJDY_GZ> djdys = dao.getDataList(BDCS_DJDY_GZ.class, xmbhHql );
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
					if (QLLX.GYJSYDSHYQ_FWSYQ.Value.equals(this.getQllx().Value)||"H".equals(super.getBdcdylx().toString())) { // 房屋所有权
						try {
							BDCS_H_GZ h = dao.get(BDCS_H_GZ.class, djdy.getBDCDYID());
							if(h==null) {
								BDCS_H_XZ h_xz = dao.get(BDCS_H_XZ.class, djdy.getBDCDYID());
								if(h_xz!=null) {
									h=ObjectHelper.copyHFromXZtoGZ(h_xz);
								}
								
							}
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

	public void SendMsg(String bljc) {
		BDCS_XMXX xmxx = (BDCS_XMXX) getCommonDao().get(BDCS_XMXX.class,
				super.getXMBH());
		String xmbhFilter = ProjectHelper.GetXMBHCondition(super.getXMBH());
		List<BDCS_DJDY_GZ> djdys = getCommonDao().getDataList(BDCS_DJDY_GZ.class, xmbhFilter);
		if ((djdys != null) && (djdys.size() > 0))
			for (int idjdy = 0; idjdy < djdys.size(); idjdy++) {
				BDCS_DJDY_GZ djdy = (BDCS_DJDY_GZ) djdys.get(idjdy);
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
				List<RightsHolder> bdcqlrs = RightsHolderTools.loadRightsHolders(
						ConstValue.DJDYLY.GZ, djdy.getDJDYID(), getXMBH());
				MessageExport msg = super.getShareMsgTools().GetMsg(bdcdy,
						bdcql, bdcfsql, bdcqlrs, bljc, xmxx);
				super.getShareMsgTools().SendMsg(msg, idjdy + 1,
						djdy.getBDCDYLX(), xmxx);
			}
	}
}