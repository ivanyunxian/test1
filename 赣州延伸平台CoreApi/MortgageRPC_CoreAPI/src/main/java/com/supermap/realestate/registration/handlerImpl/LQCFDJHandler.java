package com.supermap.realestate.registration.handlerImpl;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

import com.supermap.realestate.registration.ViewClass.ProjectInfo;
import com.supermap.realestate.registration.ViewClass.UnitTree;
import com.supermap.realestate.registration.handler.DJHandler;
import com.supermap.realestate.registration.model.BDCS_DJDY_GZ;
import com.supermap.realestate.registration.model.BDCS_FSQL_GZ;
import com.supermap.realestate.registration.model.BDCS_QL_GZ;
import com.supermap.realestate.registration.model.BDCS_XMXX;
import com.supermap.realestate.registration.model.interfaces.RealUnit;
import com.supermap.realestate.registration.model.interfaces.Rights;
import com.supermap.realestate.registration.model.interfaces.RightsHolder;
import com.supermap.realestate.registration.model.interfaces.SubRights;
import com.supermap.realestate.registration.model.xmlExportmodel.MessageExport;
import com.supermap.realestate.registration.tools.RightsTools;
import com.supermap.realestate.registration.tools.UnitTools;
import com.supermap.realestate.registration.util.ConfigHelper;
import com.supermap.realestate.registration.util.ConstValue;
import com.supermap.realestate.registration.util.ProjectHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.realestate.registration.util.ConstValue.CFLX;
import com.supermap.realestate.registration.util.ConstValue.DJDYLY;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import com.supermap.wisdombusiness.web.ResultMessage;

public class LQCFDJHandler extends DJHandlerBase implements DJHandler{

	public LQCFDJHandler(ProjectInfo info) {
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

	@Override
	public boolean writeDJB() {
		String strSqlXMBH = ProjectHelper.GetXMBHCondition(getXMBH());
		List<BDCS_DJDY_GZ> djdys = getCommonDao().getDataList(BDCS_DJDY_GZ.class, strSqlXMBH);
		if (djdys != null && djdys.size() > 0) {
			for (int idjdy = 0; idjdy < djdys.size(); idjdy++) {
				BDCS_DJDY_GZ bdcs_djdy_gz = djdys.get(idjdy);
				if (bdcs_djdy_gz != null) {
					String djdyid = bdcs_djdy_gz.getDJDYID();
					// super.CopyGZDJDYToXZAndLS(key_djdy);
					super.CopyGZQLToXZAndLS(djdyid);
					super.CopyGZQLRToXZAndLS(djdyid);
					super.CopyGZQDZRToXZAndLS(djdyid);
					super.CopyGZZSToXZAndLS(djdyid);
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
				UnitTools.loadUnit(this.getBdcdylx(), DJDYLY.initFrom(djdy.getLY()), djdy.getBDCDYID());
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
								lhsx = Integer.parseInt(cstrxz);
								
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
							lhsx = Math.max(cgz, lhsx);
							fsql.setCFLX(CFLX.LHCF.Value);
						}
					} catch (Exception ee) {

					}
				}
				lhsx = Math.max(lhsx, cgz);
			}
			// 设置轮候顺序
			fsql.setLHSX(lhsx+1);



			// 保存
			dao.save(djdy);
			dao.save(ql);
			dao.save(fsql);
		}
		msg.setSuccess("true");
		return msg;
	}
	
	private boolean ValidateDup(CommonDao dao, String bdcdyid) {
		String hql = "BDCDYID='" + bdcdyid + "' AND XMBH='" + super.getXMBH() + "'";// 通过不动产单元ID和项目编号判断是否重复
		List<BDCS_DJDY_GZ> list = dao.getDataList(BDCS_DJDY_GZ.class, hql);
		if (list != null && list.size() > 0) {
			return true;
		}
		return false;
	}

}
