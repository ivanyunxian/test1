package com.supermap.realestate.registration.handlerImpl;

import java.text.MessageFormat;
import java.util.List;

import org.springframework.util.StringUtils;

import com.supermap.realestate.registration.ViewClass.ProjectInfo;
import com.supermap.realestate.registration.handler.DJHandler;
import com.supermap.realestate.registration.model.BDCS_DJDY_GZ;
import com.supermap.realestate.registration.model.BDCS_FSQL_GZ;
import com.supermap.realestate.registration.model.BDCS_FSQL_XZ;
import com.supermap.realestate.registration.model.BDCS_QLR_XZ;
import com.supermap.realestate.registration.model.BDCS_QL_GZ;
import com.supermap.realestate.registration.model.BDCS_QL_XZ;
import com.supermap.realestate.registration.model.BDCS_SQR;
import com.supermap.realestate.registration.model.BDCS_XMXX;
import com.supermap.realestate.registration.model.interfaces.RealUnit;
import com.supermap.realestate.registration.model.interfaces.Rights;
import com.supermap.realestate.registration.model.interfaces.RightsHolder;
import com.supermap.realestate.registration.model.interfaces.SubRights;
import com.supermap.realestate.registration.model.xmlExportmodel.BGMessageExport;
import com.supermap.realestate.registration.model.xmlExportmodel.MessageExport;
import com.supermap.realestate.registration.tools.RightsHolderTools;
import com.supermap.realestate.registration.tools.RightsTools;
import com.supermap.realestate.registration.tools.UnitTools;
import com.supermap.realestate.registration.util.ConstValue;
import com.supermap.realestate.registration.util.ObjectHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.realestate.registration.util.ConstValue.BDCDYLX;
import com.supermap.realestate.registration.util.ConstValue.DJDYLY;
import com.supermap.realestate.registration.util.ConstValue.SQRLB;
import com.supermap.realestate.registration.util.ConstValue.ZSBS;
import com.supermap.realestate.registration.util.ProjectHelper;
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
 * 变更登记处理类(添加单元，但不默认初始化权利人)
 * @ClassName: BGDJHandler1
 * @author yuxuebin
 * @date 2015年9月21日 上午9:14:15
 */
public class BGDJHandler1 extends BGDJHandler implements DJHandler {

	/**
	 * 构造函数
	 * 
	 * @param info
	 */
	public BGDJHandler1(ProjectInfo info) {
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
									BDCS_SQR sqr = super.copyXZQLRtoSQR(qlrs.get(iqlr), SQRLB.YF);
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
							String gzqlid = CopyQLXXFromXZExceptBDCQZH(bgqqls.get(0).getId(), djdy.getDJDYID());
							if (!StringHelper.isEmpty(gzqlid)) {
								BDCS_QL_GZ gzql=dao.get(BDCS_QL_GZ.class, gzqlid);
								if(gzql!=null)
								{
									gzql.setBDCDYH(djdy.getBDCDYH());
								}
							}
						}
					}
					dao.save(djdy);
				}
			}
		}
		return true;
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
	protected String CopyQLXXFromXZExceptBDCQZH(String qlid, String djdyid) {
		String gzqlid = "";
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
			bdcs_ql_gz.setDJDYID(djdyid);
			bdcs_ql_gz.setXMBH(getXMBH());
			bdcs_ql_gz.setLYQLID(qlid);
			bdcs_ql_gz.setDJSJ(null);
			bdcs_ql_gz.setDBR("");
			bdcs_ql_gz.setBDCQZH("");
			bdcs_ql_gz.setZSBS(ZSBS.DYB.Value);
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
					bdcs_fsql_gz.setDJDYID(djdyid);
					bdcs_fsql_gz.setXMBH(getXMBH());
					getCommonDao().save(bdcs_fsql_gz);
				}
			}
		}
		return gzqlid;
	}
	@Override
	public void SendMsg(String bljc) {//分割后转移发送消息
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
