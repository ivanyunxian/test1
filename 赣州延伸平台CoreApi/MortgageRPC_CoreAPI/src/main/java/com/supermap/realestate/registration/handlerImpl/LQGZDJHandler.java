package com.supermap.realestate.registration.handlerImpl;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;

import com.supermap.realestate.registration.ViewClass.ProjectInfo;
import com.supermap.realestate.registration.ViewClass.UnitTree;
import com.supermap.realestate.registration.handler.DJHandler;
import com.supermap.realestate.registration.model.BDCS_DJDY_GZ;
import com.supermap.realestate.registration.model.BDCS_DJDY_XZ;
import com.supermap.realestate.registration.model.BDCS_H_GZ;
import com.supermap.realestate.registration.model.BDCS_H_XZ;
import com.supermap.realestate.registration.model.BDCS_QLR_XZ;
import com.supermap.realestate.registration.model.BDCS_QL_GZ;
import com.supermap.realestate.registration.model.BDCS_QL_XZ;
import com.supermap.realestate.registration.model.BDCS_SHYQZD_GZ;
import com.supermap.realestate.registration.model.BDCS_SHYQZD_XZ;
import com.supermap.realestate.registration.model.BDCS_SLLM_GZ;
import com.supermap.realestate.registration.model.BDCS_SQR;
import com.supermap.realestate.registration.model.BDCS_SYQZD_GZ;
import com.supermap.realestate.registration.model.BDCS_SYQZD_XZ;
import com.supermap.realestate.registration.model.BDCS_TDYT_GZ;
import com.supermap.realestate.registration.model.BDCS_TDYT_XZ;
import com.supermap.realestate.registration.model.BDCS_XMXX;
import com.supermap.realestate.registration.model.BDCS_YHYDZB_GZ;
import com.supermap.realestate.registration.model.BDCS_YHYDZB_XZ;
import com.supermap.realestate.registration.model.BDCS_YHZK_GZ;
import com.supermap.realestate.registration.model.BDCS_YHZK_XZ;
import com.supermap.realestate.registration.model.BDCS_ZH_GZ;
import com.supermap.realestate.registration.model.WFD_MAPPING;
import com.supermap.realestate.registration.model.interfaces.RealUnit;
import com.supermap.realestate.registration.model.interfaces.Rights;
import com.supermap.realestate.registration.model.interfaces.RightsHolder;
import com.supermap.realestate.registration.model.interfaces.SubRights;
import com.supermap.realestate.registration.model.xmlExportmodel.MessageExport;
import com.supermap.realestate.registration.tools.RightsHolderTools;
import com.supermap.realestate.registration.tools.RightsTools;
import com.supermap.realestate.registration.tools.UnitTools;
import com.supermap.realestate.registration.util.ConstValue;
import com.supermap.realestate.registration.util.ProjectHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.realestate.registration.util.ConstValue.BDCDYLX;
import com.supermap.realestate.registration.util.ConstValue.DJDYLY;
import com.supermap.realestate.registration.util.ConstValue.SF;
import com.supermap.wisdombusiness.core.SuperHelper;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;

public class LQGZDJHandler extends DJHandlerBase implements DJHandler{

	public LQGZDJHandler(ProjectInfo info) {
		super(info);
	}

	@Override
	public boolean addBDCDY(String bdcdyid) {
		boolean bsuccess = false;
		CommonDao dao = getCommonDao();
		String bdcdyid_new = getPrimaryKey();

		RealUnit unit = null;
		if (this.getBdcdylx().equals(BDCDYLX.H)) {
			unit = CopyHouse(bdcdyid, bdcdyid_new);
		} else if (this.getBdcdylx().equals(BDCDYLX.SHYQZD)) {
			unit = CopyUseLand(bdcdyid, bdcdyid_new);
		}else if (this.getBdcdylx().equals(BDCDYLX.SYQZD)) {
			unit = CopyOwnershipLand(bdcdyid, bdcdyid_new);
		}else if(this.getBdcdylx().equals(BDCDYLX.LD))
		{
			unit=CopyForest(bdcdyid, bdcdyid_new);
		}else if(this.getBdcdylx().equals(BDCDYLX.HY))
		{
			unit=CopySea(bdcdyid, bdcdyid_new);
		}
		if (unit == null) {
			super.setErrMessage("添加失败！");
			return false;
		}
		unit.setXMBH(getXMBH());
		dao.save(unit);

		// 拷贝登记单元
		BDCS_DJDY_GZ bdcs_djdy_gz = super.createDJDYfromXZ(bdcdyid);
		bdcs_djdy_gz.setBDCDYID(bdcdyid_new);
		bdcs_djdy_gz.setId(getPrimaryKey());
		bdcs_djdy_gz.setLY(DJDYLY.GZ.Value);
		dao.save(bdcs_djdy_gz);
		// 拷贝权利信息
		StringBuilder builder = new StringBuilder();
		builder.append(" DJDYID='").append(bdcs_djdy_gz.getDJDYID());
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
		// 把权利人拷贝过来放到申请人里边 add by diaoliwei 2015-7-31
		copyApplicant(bdcs_djdy_gz.getDJDYID());
		dao.flush();
		bsuccess = true;
		return bsuccess;
	}

	@Override
	public boolean writeDJB() {

		if (super.isCForCFING()) {
			return false;
		}

		List<BDCS_QL_GZ> qls = getCommonDao().getDataList(BDCS_QL_GZ.class,
				" XMBH='" + getXMBH() + "'");
		if (qls != null) {
			for (int iql = 0; iql < qls.size(); iql++) {
				super.removeQLXXFromXZByQLID(qls.get(iql).getLYQLID());
			}
		}
		List<BDCS_DJDY_GZ> djdys = getCommonDao().getDataList(
				BDCS_DJDY_GZ.class, " XMBH='" + getXMBH() + "'");
		if (djdys != null) {
			for (int idjdy = 0; idjdy < djdys.size(); idjdy++) {
				BDCS_DJDY_GZ bdcs_djdy_gz = djdys.get(idjdy);
				String djdyid = bdcs_djdy_gz.getDJDYID();

				String xz_bdcdyid = "";
				List<BDCS_DJDY_XZ> xz_djdys = getCommonDao().getDataList(
						BDCS_DJDY_XZ.class, " DJDYID='" + djdyid + "'");
				if (xz_djdys != null && xz_djdys.size() > 0) {
					BDCS_DJDY_XZ bdcs_djdy_xz = xz_djdys.get(0);
					xz_bdcdyid = bdcs_djdy_xz.getBDCDYID();
					RebuildDYBG(bdcs_djdy_gz.getBDCDYID(),
							bdcs_djdy_gz.getDJDYID(),
							bdcs_djdy_xz.getBDCDYID(),
							bdcs_djdy_xz.getDJDYID(),
							bdcs_djdy_gz.getCreateTime(),
							bdcs_djdy_gz.getModifyTime());
				}
				// super.removeDYFromXZ(djdyid);
				// getCommonDao().deleteEntitysByHql(BDCS_DJDY_XZ.class,
				// " DJDYID='" + djdyid + "'");
				// getCommonDao().deleteEntitysByHql(BDCS_DJDY_LS.class,
				// " DJDYID='" + djdyid + "'");
				// super.CopyGZDJDYToXZAndLS(djdyid);
				super.CopyGZQLToXZAndLS(djdyid);
				super.CopyGZQLRToXZAndLS(djdyid);
				super.CopyGZQDZRToXZAndLS(djdyid);
				super.CopyGZZSToXZAndLS(djdyid);
				super.CopyGZDYToXZAndLSEx(bdcs_djdy_gz.getBDCDYID(), xz_bdcdyid);
				if(getBdcdylx().equals(BDCDYLX.HY)){
					CopyZHOther(bdcs_djdy_gz.getBDCDYID(), xz_bdcdyid);
				}
				if(getBdcdylx().equals(BDCDYLX.SHYQZD)){
					CopySHYQZDOther(bdcs_djdy_gz.getBDCDYID(), xz_bdcdyid);
				}
				// super.CopyGZDJDYToXZAndLS(bdcs_djdy_gz.getId());
			}
		}
		this.SetSFDB();
		getCommonDao().flush();
		super.alterCachedXMXX();
		return true;
	}

	@Override
	public boolean removeBDCDY(String bdcdyid) {
		CommonDao dao = getCommonDao();
		// 删除单元
		if (getQllx().equals(ConstValue.QLLX.GYJSYDSHYQ)) {
			BDCS_SHYQZD_GZ bdcs_shqzd_gz = dao.get(BDCS_SHYQZD_GZ.class,
					bdcdyid);
			if(bdcs_shqzd_gz!=null){
				dao.deleteEntity(bdcs_shqzd_gz);
			}
		} else if (getQllx().equals(ConstValue.QLLX.GYJSYDSHYQ_FWSYQ)) {
			BDCS_H_GZ bdcs_h_gz = dao.get(BDCS_H_GZ.class, bdcdyid);
			if(bdcs_h_gz!=null){
				dao.deleteEntity(bdcs_h_gz);
			}
		}else if (getBdcdylx().equals(ConstValue.BDCDYLX.HY)) {
			BDCS_ZH_GZ dy_gz = dao.get(BDCS_ZH_GZ.class, bdcdyid);
			if(dy_gz!=null){
				String hqlCondition = "BDCDYID='" + bdcdyid + "'";
				// 先删除
				getCommonDao().deleteEntitysByHql(BDCS_YHZK_GZ.class,
						hqlCondition);
				// 先删除
				getCommonDao().deleteEntitysByHql(BDCS_YHYDZB_GZ.class,
						hqlCondition);
				dao.deleteEntity(dy_gz);
			}
		}else if(getBdcdylx().equals(ConstValue.BDCDYLX.LD)){
			BDCS_SLLM_GZ bdcs_sllm_gz = dao.get(BDCS_SLLM_GZ.class, bdcdyid);
			if(bdcs_sllm_gz!=null){
				dao.deleteEntity(bdcs_sllm_gz);
			}
		}
		// 删除登记单元
		String hql = MessageFormat.format(" BDCDYID=''{0}''", bdcdyid);
		List<BDCS_DJDY_GZ> djdys = getCommonDao().getDataList(
				BDCS_DJDY_GZ.class, hql);
		if (djdys != null && djdys.size() > 0) {
			BDCS_DJDY_GZ djdy = djdys.get(0);
			dao.deleteEntity(djdy);
			// 删除权利信息
			StringBuilder builder = new StringBuilder();
			builder.append(" DJDYID='").append(djdys.get(0).getDJDYID());
			builder.append("' AND QLLX='").append(getQllx().Value);
			builder.append("' AND XMBH='").append(getXMBH()).append("'");
			List<BDCS_QL_GZ> qls = dao.getDataList(BDCS_QL_GZ.class,
					builder.toString());
			if (qls != null && qls.size() > 0) {
				super.RomoveQLXXFromGZ(qls.get(0).getId());
			}
		}
		dao.flush();
		return true;
	}

	@Override
	public List<UnitTree> getUnitTree() {
		List<UnitTree> list_new=new ArrayList<UnitTree>();
		List<UnitTree> list=super.getUnitList();
		for(UnitTree tree:list){
			String djdyid=tree.getDjdyid();
			String bdcdyid=tree.getBdcdyid();
			String oldbdcdyid="";
			List<BDCS_DJDY_XZ> djdys=getCommonDao().getDataList(BDCS_DJDY_XZ.class, "DJDYID='"+djdyid+"' AND BDCDYLX='"+super.getBdcdylx().Value+"'");
			if(djdys!=null&&djdys.size()>0){
				oldbdcdyid=djdys.get(0).getBDCDYID();
			}
			if(!StringHelper.isEmpty(oldbdcdyid)&&!oldbdcdyid.equals(bdcdyid)){
				tree.setOldbdcdyid(oldbdcdyid);
			}
			if(!StringHelper.isEmpty(tree.getQlid())){
				Rights ql=RightsTools.loadRights(DJDYLY.GZ, tree.getQlid());
				if(ql!=null){
					tree.setOldqlid(ql.getLYQLID());
				}
			}
			
			list_new.add(tree);
		}
		return list_new;
	}

	@Override
	public void addQLRbySQRArray(String qlid, Object[] sqrids) {
		// 判断一下，是否不生成证书。
		String newqzh = "";
		String workflowcode = ProjectHelper.getWorkflowCodeByProjectID(this
				.getProject_id());
		List<WFD_MAPPING> listCode = getCommonDao().getDataList(
				WFD_MAPPING.class, "WORKFLOWCODE='" + workflowcode + "'");
		if (listCode != null && listCode.size() > 0) {
			newqzh = listCode.get(0).getNEWQZH();
		}
		if (SF.NO.Value.equals(newqzh)) {
			super.setErrMessage("该登记流程不允许添加权利人");
			return;
		} else {
			super.addQLRbySQRs(qlid, sqrids);
		}
	}

	@Override
	public void removeQLR(String qlid, String qlrid) {
		String newqzh = "";
		String workflowcode = ProjectHelper.getWorkflowCodeByProjectID(this
				.getProject_id());
		List<WFD_MAPPING> listCode = getCommonDao().getDataList(
				WFD_MAPPING.class, "WORKFLOWCODE='" + workflowcode + "'");
		if (listCode != null && listCode.size() > 0) {
			newqzh = listCode.get(0).getNEWQZH();
		}
		if (SF.NO.Value.equals(newqzh))  {
			super.setErrMessage("该登记流程不允许移除权利人");
			return;
		} else {
			super.removeqlr(qlrid, qlid);
		}
	}

	@Override
	public String getError() {
		return super.getErrMessage();
	}

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
	public RealUnit CopyHouse(String bdcdyid, String bdcdyid_new) {
		RealUnit unit = null;
		BDCS_H_XZ dy_xz = getCommonDao().get(BDCS_H_XZ.class, bdcdyid);
		if (dy_xz != null) {
			BDCS_H_GZ dy_gz = new BDCS_H_GZ();
			try {
				PropertyUtils.copyProperties(dy_gz, dy_xz);
				unit = (RealUnit) dy_gz;
				unit.setId(bdcdyid_new);
			} catch (Exception e) {
			}
		}
		return unit;
	}

	public RealUnit CopyUseLand(String bdcdyid, String bdcdyid_new) {
		RealUnit unit = null;
		BDCS_SHYQZD_XZ dy_xz = getCommonDao()
				.get(BDCS_SHYQZD_XZ.class, bdcdyid);
		if (dy_xz != null) {
			BDCS_SHYQZD_GZ dy_gz = new BDCS_SHYQZD_GZ();
			try {
				PropertyUtils.copyProperties(dy_gz, dy_xz);
				unit = (RealUnit) dy_gz;
				unit.setId(bdcdyid_new);

				String hqlCondition = "BDCDYID='" + bdcdyid + "'";
				// 先删除
				getCommonDao().deleteEntitysByHql(BDCS_TDYT_GZ.class,
						hqlCondition);
				// 再拷贝
				List<BDCS_TDYT_XZ> desListTDYT = getCommonDao().getDataList(
						BDCS_TDYT_XZ.class, hqlCondition);
				if (desListTDYT != null && desListTDYT.size() > 0) {
					for (BDCS_TDYT_XZ tdyt : desListTDYT) {
						BDCS_TDYT_GZ yt_gz = new BDCS_TDYT_GZ();
						try {
							PropertyUtils.copyProperties(yt_gz, tdyt);
							yt_gz.setId((String) SuperHelper
									.GeneratePrimaryKey());
							yt_gz.setBDCDYID(bdcdyid_new);
							getCommonDao().save(yt_gz);
						} catch (Exception e) {
						}
					}
				}
			} catch (Exception e) {
			}
		}
		return unit;
	}
	
	public RealUnit CopyOwnershipLand(String bdcdyid, String bdcdyid_new) {
		RealUnit unit = null;
		BDCS_SYQZD_XZ dy_xz = getCommonDao()
				.get(BDCS_SYQZD_XZ.class, bdcdyid);
		if (dy_xz != null) {
			BDCS_SYQZD_GZ dy_gz = new BDCS_SYQZD_GZ();
			try {
				PropertyUtils.copyProperties(dy_gz, dy_xz);
				unit = (RealUnit) dy_gz;
				unit.setId(bdcdyid_new);

				String hqlCondition = "BDCDYID='" + bdcdyid + "'";
				// 先删除
				getCommonDao().deleteEntitysByHql(BDCS_TDYT_GZ.class,
						hqlCondition);
				// 再拷贝
				List<BDCS_TDYT_XZ> desListTDYT = getCommonDao().getDataList(
						BDCS_TDYT_XZ.class, hqlCondition);
				if (desListTDYT != null && desListTDYT.size() > 0) {
					for (BDCS_TDYT_XZ tdyt : desListTDYT) {
						BDCS_TDYT_GZ yt_gz = new BDCS_TDYT_GZ();
						try {
							PropertyUtils.copyProperties(yt_gz, tdyt);
							yt_gz.setId((String) SuperHelper
									.GeneratePrimaryKey());
							yt_gz.setBDCDYID(bdcdyid_new);
							getCommonDao().save(yt_gz);
						} catch (Exception e) {
						}
					}
				}
			} catch (Exception e) {
			}
		}
		return unit;
	}
    /**
     * 从现状层拷贝林地单元信息到工作层
     * @作者 海豹
     * @创建时间 2016年6月2日下午4:17:28
     * @param bdcdyid
     * @param bdcdyid_new
     * @return
     */
	public RealUnit CopyForest(String bdcdyid,String bdcdyid_new)
	{
		RealUnit realunit_gz= null;
			 realunit_gz=UnitTools.copyUnit(BDCDYLX.LD, DJDYLY.XZ, DJDYLY.GZ, bdcdyid);
			realunit_gz.setId(bdcdyid_new);
			getCommonDao().save(realunit_gz);
		return realunit_gz;
	}
	/**
     * 从现状层拷贝林地单元信息到工作层
     * @作者 海豹
     * @创建时间 2016年6月2日下午4:17:28
     * @param bdcdyid
     * @param bdcdyid_new
     * @return
     */
	public RealUnit CopySea(String bdcdyid,String bdcdyid_new)
	{
		RealUnit realunit_gz= null;
		realunit_gz=UnitTools.copyUnit(BDCDYLX.HY, DJDYLY.XZ, DJDYLY.GZ, bdcdyid);
		realunit_gz.setId(bdcdyid_new);
		getCommonDao().save(realunit_gz);
		if (realunit_gz != null) {
			try {
				String hqlCondition = "BDCDYID='" + bdcdyid + "'";
				// 先删除
				getCommonDao().deleteEntitysByHql(BDCS_YHZK_GZ.class,
						hqlCondition);
				// 再拷贝
				List<BDCS_YHZK_XZ> desListYHZK = getCommonDao().getDataList(
						BDCS_YHZK_XZ.class, hqlCondition);
				if (desListYHZK != null && desListYHZK.size() > 0) {
					for (BDCS_YHZK_XZ yhzk : desListYHZK) {
						BDCS_YHZK_GZ yhzk_gz = new BDCS_YHZK_GZ();
						try {
							PropertyUtils.copyProperties(yhzk_gz, yhzk);
							yhzk_gz.setId((String) SuperHelper
									.GeneratePrimaryKey());
							yhzk_gz.setBDCDYID(bdcdyid_new);
							getCommonDao().save(yhzk_gz);
						} catch (Exception e) {
						}
					}
				}
				// 先删除
				getCommonDao().deleteEntitysByHql(BDCS_YHYDZB_GZ.class,
						hqlCondition);
				// 再拷贝
				List<BDCS_YHYDZB_XZ> desListYHYDZB = getCommonDao().getDataList(
						BDCS_YHYDZB_XZ.class, hqlCondition);
				if (desListYHYDZB != null && desListYHYDZB.size() > 0) {
					for (BDCS_YHYDZB_XZ yhydzb : desListYHYDZB) {
						BDCS_YHYDZB_GZ yhydzb_gz = new BDCS_YHYDZB_GZ();
						try {
							PropertyUtils.copyProperties(yhydzb_gz, yhydzb);
							yhydzb_gz.setId((String) SuperHelper
									.GeneratePrimaryKey());
							yhydzb_gz.setBDCDYID(bdcdyid_new);
							getCommonDao().save(yhydzb_gz);
						} catch (Exception e) {
						}
					}
				}
			} catch (Exception e) {
			}
		}
		return realunit_gz;
	}
	
	protected void copyApplicant(String bdcdyid) {
		CommonDao dao = getCommonDao();

		String qlrSql = MessageFormat.format(
				"QLID IN (SELECT id FROM BDCS_QL_XZ WHERE QLLX IN(''10'',''11'',''36'') AND DJDYID=''{0}'')",
				bdcdyid);
		List<BDCS_QLR_XZ> qlrs = dao.getDataList(BDCS_QLR_XZ.class, qlrSql);
		for (BDCS_QLR_XZ bdcs_qlr_xz : qlrs) {
			String zjhm = bdcs_qlr_xz.getZJH();
			String qlrmc = bdcs_qlr_xz.getQLRMC();
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
				}
			}
			// 判断申请人是否已经添加过，如果添加过，就不再添加
			if (!flag) {
				String sqrid = getPrimaryKey();
				BDCS_SQR bdcs_sqr = new BDCS_SQR();
				bdcs_sqr.setGYFS(bdcs_qlr_xz.getGYFS());
				bdcs_sqr.setFZJG(bdcs_qlr_xz.getFZJG());
				bdcs_sqr.setGJDQ(bdcs_qlr_xz.getGJ());
				bdcs_sqr.setGZDW(bdcs_qlr_xz.getGZDW());
				bdcs_sqr.setXB(bdcs_qlr_xz.getXB());
				bdcs_sqr.setHJSZSS(bdcs_qlr_xz.getHJSZSS());
				bdcs_sqr.setSSHY(bdcs_qlr_xz.getSSHY());
				bdcs_sqr.setYXBZ(bdcs_qlr_xz.getYXBZ());
				bdcs_sqr.setQLBL(bdcs_qlr_xz.getQLBL());
				bdcs_sqr.setQLMJ(StringHelper.formatObject(bdcs_qlr_xz.getQLMJ()));
				bdcs_sqr.setSQRXM(bdcs_qlr_xz.getQLRMC());
				bdcs_sqr.setSQRLB("1");
				bdcs_sqr.setSQRLX(bdcs_qlr_xz.getQLRLX());
				bdcs_sqr.setDZYJ(bdcs_qlr_xz.getDZYJ());
				bdcs_sqr.setLXDH(bdcs_qlr_xz.getDH());
				bdcs_sqr.setZJH(bdcs_qlr_xz.getZJH());
				bdcs_sqr.setZJLX(bdcs_qlr_xz.getZJZL());
				bdcs_sqr.setTXDZ(bdcs_qlr_xz.getDZ());
				bdcs_sqr.setYZBM(bdcs_qlr_xz.getYB());
				bdcs_sqr.setXMBH(getXMBH());
				bdcs_sqr.setId(sqrid);
				// bdcs_sqr.setGLQLID(ql.getId());
				dao.save(bdcs_sqr);
			}
		}
	}
	private void CopyZHOther(String bdcdyid_gz,String bdcdyid_xz){
		String hqlCondition_xz = "BDCDYID='" + bdcdyid_xz + "'";
		String hqlCondition_gz = "BDCDYID='" + bdcdyid_gz + "'";
		// 先删除
		getCommonDao().deleteEntitysByHql(BDCS_YHZK_XZ.class,
				hqlCondition_xz);
		// 再拷贝
		List<BDCS_YHZK_GZ> desListYHZK = getCommonDao().getDataList(
				BDCS_YHZK_GZ.class, hqlCondition_gz);
		if (desListYHZK != null && desListYHZK.size() > 0) {
			for (BDCS_YHZK_GZ yhzk : desListYHZK) {
				BDCS_YHZK_XZ yhzk_xz = new BDCS_YHZK_XZ();
				try {
					PropertyUtils.copyProperties(yhzk_xz, yhzk);
					yhzk_xz.setId((String) SuperHelper
							.GeneratePrimaryKey());
					yhzk_xz.setBDCDYID(bdcdyid_xz);
					getCommonDao().save(yhzk_xz);
				} catch (Exception e) {
				}
			}
		}
		// 先删除
		getCommonDao().deleteEntitysByHql(BDCS_YHYDZB_GZ.class,
				hqlCondition_xz);
		// 再拷贝
		List<BDCS_YHYDZB_GZ> desListYHYDZB = getCommonDao().getDataList(
				BDCS_YHYDZB_GZ.class, hqlCondition_gz);
		if (desListYHYDZB != null && desListYHYDZB.size() > 0) {
			for (BDCS_YHYDZB_GZ yhydzb : desListYHYDZB) {
				BDCS_YHYDZB_XZ yhydzb_xz = new BDCS_YHYDZB_XZ();
				try {
					PropertyUtils.copyProperties(yhydzb_xz, yhydzb);
					yhydzb_xz.setId((String) SuperHelper
							.GeneratePrimaryKey());
					yhydzb_xz.setBDCDYID(bdcdyid_xz);
					getCommonDao().save(yhydzb_xz);
				} catch (Exception e) {
				}
			}
		}
	}
	
	private void CopySHYQZDOther(String bdcdyid_gz,String bdcdyid_xz){
		String hqlCondition_xz = "BDCDYID='" + bdcdyid_xz + "'";
		String hqlCondition_gz = "BDCDYID='" + bdcdyid_gz + "'";
		// 先删除
		getCommonDao().deleteEntitysByHql(BDCS_TDYT_XZ.class,
				hqlCondition_xz);
		// 再拷贝
		List<BDCS_TDYT_GZ> desListTDYT = getCommonDao().getDataList(
				BDCS_TDYT_GZ.class, hqlCondition_gz);
		if (desListTDYT != null && desListTDYT.size() > 0) {
			for (BDCS_TDYT_GZ tdyt : desListTDYT) {
				BDCS_TDYT_XZ tdyt_xz = new BDCS_TDYT_XZ();
				try {
					PropertyUtils.copyProperties(tdyt_xz, tdyt);
					tdyt_xz.setId((String) SuperHelper
							.GeneratePrimaryKey());
					tdyt_xz.setBDCDYID(bdcdyid_xz);
					getCommonDao().save(tdyt_xz);
				} catch (Exception e) {
				}
			}
		}
	}


}
