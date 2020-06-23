package com.supermap.realestate.registration.handlerImpl;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.StringUtils;

import com.supermap.realestate.registration.ViewClass.ProjectInfo;
import com.supermap.realestate.registration.handler.DJHandler;
import com.supermap.realestate.registration.model.BDCS_DJDY_GZ;
import com.supermap.realestate.registration.model.BDCS_FSQL_GZ;
import com.supermap.realestate.registration.model.BDCS_FSQL_XZ;
import com.supermap.realestate.registration.model.BDCS_QDZR_GZ;
import com.supermap.realestate.registration.model.BDCS_QDZR_XZ;
import com.supermap.realestate.registration.model.BDCS_QLR_GZ;
import com.supermap.realestate.registration.model.BDCS_QLR_XZ;
import com.supermap.realestate.registration.model.BDCS_QL_GZ;
import com.supermap.realestate.registration.model.BDCS_QL_XZ;
import com.supermap.realestate.registration.model.BDCS_SQR;
import com.supermap.realestate.registration.model.BDCS_XMXX;
import com.supermap.realestate.registration.model.BDCS_ZS_GZ;
import com.supermap.realestate.registration.model.BDCS_ZS_XZ;
import com.supermap.realestate.registration.model.interfaces.RealUnit;
import com.supermap.realestate.registration.model.interfaces.Rights;
import com.supermap.realestate.registration.model.xmlExportmodel.BGMessageExport;
import com.supermap.realestate.registration.tools.RightsTools;
import com.supermap.realestate.registration.tools.UnitTools;
import com.supermap.realestate.registration.util.ConstValue.BDCDYLX;
import com.supermap.realestate.registration.util.ConstValue.CZFS;
import com.supermap.realestate.registration.util.ConstValue.DJDYLY;
import com.supermap.realestate.registration.util.ConstValue.SQRLB;
import com.supermap.realestate.registration.util.ObjectHelper;
import com.supermap.realestate.registration.util.ProjectHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;

/*	
 1、国有建设用地使用权变更登记
 2、集体建设用地使用权变更登记（未配置）
 3、宅基地使用权变更登记（未配置）
 4、国有建设用地使用权/房屋所有权变更登记
 5、集体建设用地使用权/房屋所有权变更登记（未配置）
 6、宅基地使用权/房屋所有权变更登记（未配置）
 */
/**
 * 变更登记处理类(添加单元，并默认初始化权利人)
 * @ClassName: BGDJHandler2
 * @author yuxuebin
 * @date 2015年9月21日 上午9:14:15
 */
public class BGDJHandler2 extends BGDJHandler implements DJHandler {

	/**
	 * 构造函数
	 * 
	 * @param info
	 */
	public BGDJHandler2(ProjectInfo info) {
		super(info);
	}

	/**
	 * 添加不动产单元
	 */
	@Override
	public boolean addBDCDY(String bdcdyid) {
		CommonDao dao = this.getCommonDao();
		String[] ids = bdcdyid.split(",");
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
										//权利人关联申请人ID
										qlr.setSQRID(sqrlist.get(0).getId());
										getCommonDao().update(qlr);
									}
								}
								if (!bexists) {
									BDCS_SQR sqr = super.copyXZQLRtoSQR(qlrs.get(iqlr), SQRLB.JF);
									if (sqr != null) {
										dao.save(sqr);
										qlr.setSQRID(sqr.getId());
										getCommonDao().update(qlr);
									}
								}
							}
						}
					}
					dao.save(djdy);
					dao.flush();
				}
			} else {
				String xmbhFilter = ProjectHelper.GetXMBHCondition(getXMBH());
				dy = UnitTools.loadUnit(getBdcdylx(), DJDYLY.GZ, bdcdyid);
				// 登记单元索引表
				BDCS_DJDY_GZ djdy = super.createDJDYfromGZ(dy);
				if (djdy != null) {
					List<BDCS_DJDY_GZ> bgqdjdys = dao.getDataList(BDCS_DJDY_GZ.class, xmbhFilter + " AND LY='02'");
					if (bgqdjdys != null && bgqdjdys.size() > 0) {
						String bgqdjdyid = bgqdjdys.get(0).getDJDYID();
						String qllx = getQllx().Value;
						List<Rights> bgqqls = RightsTools.loadRightsByCondition(DJDYLY.XZ, "DJDYID='" + bgqdjdyid + "' AND QLLX='" + qllx + "'");
						if (bgqqls != null && bgqqls.size() > 0) {
							CopyQLXXFromXZExceptBDCQZH(bgqqls.get(0).getId(), djdy);
						}
					}
					dao.save(djdy);
				}
			}
		}
		return true;
	}

	/**
	 * 从现状拷贝权利信息，不带权证号
	 * 
	 * @Author：俞学斌
	 * @CreateTime 2015年6月22日 下午5:55:53
	 * @param qlid
	 *            权利id
	 * @param djdyid
	 *            新登记单元ID
	 * @return 状态字符串
	 */
	private String CopyQLXXFromXZExceptBDCQZH(String qlid, BDCS_DJDY_GZ djdy) {
		String gzqlid = "";
		StringBuilder builer = new StringBuilder();
		builer.append(" QLID='").append(qlid).append("'");
		String strQuery = builer.toString();
		BDCS_QL_XZ bdcs_ql_xz = getCommonDao().get(BDCS_QL_XZ.class, qlid);
		if (bdcs_ql_xz != null) {
			gzqlid = getPrimaryKey();
			String gzfsqlid = getPrimaryKey();
			// 拷贝权利
			BDCS_QL_GZ bdcs_ql_gz = ObjectHelper.copyQL_XZToGZ(bdcs_ql_xz);
			bdcs_ql_gz.setId(gzqlid);
			bdcs_ql_gz.setQLLX(getQllx().Value);
			bdcs_ql_gz.setDJLX(getDjlx().Value);
			bdcs_ql_gz.setFSQLID(gzfsqlid);
			bdcs_ql_gz.setXMBH(getXMBH());
			bdcs_ql_gz.setDJDYID(djdy.getDJDYID());
			bdcs_ql_gz.setBDCDYH(djdy.getBDCDYH());
			bdcs_ql_gz.setLYQLID(qlid);
			bdcs_ql_gz.setDJSJ(null);
			bdcs_ql_gz.setDBR("");
			bdcs_ql_gz.setBDCQZH("");
			bdcs_ql_gz.setYWH(getProject_id());
			bdcs_ql_gz.setARCHIVES_BOOKNO("");
			bdcs_ql_gz.setARCHIVES_CLASSNO("");
			// bdcs_ql_gz.setDJYY("");
			// bdcs_ql_gz.setFJ("");
			getCommonDao().save(bdcs_ql_gz);
			if (bdcs_ql_xz.getFSQLID() != null) {
				BDCS_FSQL_XZ bdcs_fsql_xz = getCommonDao().get(BDCS_FSQL_XZ.class, bdcs_ql_xz.getFSQLID());
				if (bdcs_fsql_xz != null) {
					// 拷贝附属权利
					BDCS_FSQL_GZ bdcs_fsql_gz = ObjectHelper.copyFSQL_XZToGZ(bdcs_fsql_xz);
					bdcs_fsql_gz.setQLID(gzqlid);
					bdcs_fsql_gz.setId(gzfsqlid);
					bdcs_fsql_gz.setDJDYID(djdy.getDJDYID());
					bdcs_fsql_gz.setBDCDYH(djdy.getBDCDYH());
					bdcs_fsql_gz.setXMBH(getXMBH());
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
										bdcs_qdzr_gz.setDJDYID(djdy.getDJDYID());
										bdcs_qdzr_gz.setBDCDYH(djdy.getBDCDYH());
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
			bdcs_ql_xz.setDJZT("02");
			getCommonDao().update(bdcs_ql_xz);
		}
		return gzqlid;
	}

	/**
	 * 移除不动产单元
	 */
	@Override
	public boolean removeBDCDY(String bdcdyid) {
		CommonDao dao = this.getCommonDao();
		BDCS_DJDY_GZ djdy = super.removeDJDY(bdcdyid);
		if (djdy != null) {
			String ly = djdy.getLY();
			String djdyid = djdy.getDJDYID();
			if (ly.equals(DJDYLY.GZ.Value)) {
				// 移除具体单元
				UnitTools.deleteUnit(BDCDYLX.initFrom(djdy.getBDCDYLX()), DJDYLY.GZ, bdcdyid);

				// 删除权利、附属权利、权利人、证书、权地证人关系
				String _hqlCondition = MessageFormat.format("XMBH=''{0}'' AND DJDYID=''{1}''", getXMBH(), djdyid);
				RightsTools.deleteRightsAllByCondition(DJDYLY.initFrom(ly), _hqlCondition);

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
	
	@Override	
	public void SendMsg(String bljc) {//变更登记分割合并流程发送消息
		BDCS_XMXX xmxx = getCommonDao().get(BDCS_XMXX.class, super.getXMBH());
	    String xmbhFilter = ProjectHelper.GetXMBHCondition(xmxx.getId());
	    //发存量数据时只发送房产业务数据;
	    xmbhFilter += " and(bdcdylx='031' OR bdcdylx='032') ";
	    List<BDCS_DJDY_GZ> djdys = getCommonDao().getDataList(BDCS_DJDY_GZ.class, xmbhFilter);
		BGMessageExport bgmsg = super.getShareMsgTools().GetMsg(xmxx,djdys, bljc);
		if(djdys != null && djdys.size()>0){
			super.getShareMsgTools().BGSendMsg(bgmsg, 1, djdys.get(0).getBDCDYLX(), xmxx);
		}
	}
}
