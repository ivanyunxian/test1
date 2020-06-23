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
import com.supermap.realestate.registration.dataExchange.syq.QLFQLTDSYQ;
import com.supermap.realestate.registration.dataExchange.utils.packageXml;
import com.supermap.realestate.registration.handler.DJHandler;
import com.supermap.realestate.registration.model.BDCS_C_GZ;
import com.supermap.realestate.registration.model.BDCS_DJDY_GZ;
import com.supermap.realestate.registration.model.BDCS_DJDY_XZ;
import com.supermap.realestate.registration.model.BDCS_DYBG;
import com.supermap.realestate.registration.model.BDCS_H_GZ;
import com.supermap.realestate.registration.model.BDCS_H_XZ;
import com.supermap.realestate.registration.model.BDCS_LJZ_XZ;
import com.supermap.realestate.registration.model.BDCS_NYD_GZ;
import com.supermap.realestate.registration.model.BDCS_NYD_XZ;
import com.supermap.realestate.registration.model.BDCS_QLR_GZ;
import com.supermap.realestate.registration.model.BDCS_QLR_XZ;
import com.supermap.realestate.registration.model.BDCS_QL_GZ;
import com.supermap.realestate.registration.model.BDCS_QL_XZ;
import com.supermap.realestate.registration.model.BDCS_SHYQZD_GZ;
import com.supermap.realestate.registration.model.BDCS_SHYQZD_XZ;
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
import com.supermap.realestate.registration.model.BDCS_ZRZ_XZ;
import com.supermap.realestate.registration.model.WFD_MAPPING;
import com.supermap.realestate.registration.model.interfaces.RealUnit;
import com.supermap.realestate.registration.model.interfaces.Rights;
import com.supermap.realestate.registration.model.interfaces.RightsHolder;
import com.supermap.realestate.registration.model.interfaces.SubRights;
import com.supermap.realestate.registration.model.interfaces.UseLand;
import com.supermap.realestate.registration.model.xmlExportmodel.MessageExport;
import com.supermap.realestate.registration.service.impl.ProjectServiceImpl;
import com.supermap.realestate.registration.tools.RightsHolderTools;
import com.supermap.realestate.registration.tools.RightsTools;
import com.supermap.realestate.registration.tools.UnitTools;
import com.supermap.realestate.registration.util.ConstValue;
import com.supermap.realestate.registration.util.ConstValue.BDCDYLX;
import com.supermap.realestate.registration.util.ConstValue.DJDYLY;
import com.supermap.realestate.registration.util.ConstValue.DJLX;
import com.supermap.realestate.registration.util.ConstValue.QLLX;
import com.supermap.realestate.registration.util.ConstValue.SF;
import com.supermap.realestate.registration.util.ConstValue.SQRLB;
import com.supermap.realestate.registration.util.ProjectHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.realestate.registration.util.YwLogUtil;
import com.supermap.wisdombusiness.core.SuperHelper;
import com.supermap.wisdombusiness.core.SuperSpringContext;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;

/*
 1、国有建设用地使用权更正登记
 4、国有建设用地使用权/房屋所有权更正登记
 */

public class GZDJ_ZDHandler extends DJHandlerBase implements DJHandler {

	/**
	 * 
	 * @param info
	 */
	public GZDJ_ZDHandler(ProjectInfo info) {
		super(info);
	}

	/**
	 * 添加不动产单元
	 */
	@Override
	public boolean addBDCDY(String qlStrs) {
		boolean bsuccess = false;
		CommonDao dao = getCommonDao();
		String bdcdyid_new = getPrimaryKey();
		Rights Right = RightsTools.loadRights(DJDYLY.XZ, qlStrs);
		String djdyid = Right.getDJDYID();
		List<BDCS_DJDY_XZ> djdys = getCommonDao().getDataList(BDCS_DJDY_XZ.class, "DJDYID='"+ djdyid +"'");
		String bdcdyid = djdys.get(0).getBDCDYID();
		RealUnit unit = null;
		if (this.getBdcdylx().equals(BDCDYLX.SHYQZD)) {
			unit = CopyUseLand(bdcdyid, bdcdyid_new);
		}else if (this.getBdcdylx().equals(BDCDYLX.SYQZD)) {
			unit = CopyOwnershipLand(bdcdyid, bdcdyid_new);
		}else if(this.getBdcdylx().equals(BDCDYLX.LD))
		{
			unit=CopyForest(bdcdyid, bdcdyid_new);
		}else if(this.getBdcdylx().equals(BDCDYLX.NYD)){
			unit=CopyNYDLand(bdcdyid, bdcdyid_new);
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
		super.CopySQRFromXZQLR(djdyid, getQllx().Value, getXMBH(), qlStrs, SQRLB.JF.Value);
		dao.flush();
		bsuccess = true;
		return bsuccess;
	}

	public RealUnit CopyNYDLand(String bdcdyid, String bdcdyid_new) {
		RealUnit unit = null;
		BDCS_NYD_XZ dy_xz = getCommonDao()
				.get(BDCS_NYD_XZ.class, bdcdyid);
		if (dy_xz != null) {
			BDCS_NYD_GZ dy_gz = new BDCS_NYD_GZ();
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
	 * 写入登记簿
	 */
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
				super.CopyGZQLToXZAndLS(djdyid);
				super.CopyGZQLRToXZAndLS(djdyid);
				super.CopyGZQDZRToXZAndLS(djdyid);
				super.CopyGZZSToXZAndLS(djdyid);
				super.CopyGZDYToXZAndLSEx(bdcs_djdy_gz.getBDCDYID(), xz_bdcdyid);
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

	/**
	 * 移除不动产单元
	 */
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

	/**
	 * 获取不动产单元列表
	 */
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

	/**
	 * 根据申请人ID添加权利人
	 */
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

	/**
	 * 移除权利人
	 */
	@Override
	public void removeQLR(String qlid, String qlrid) {

		// 判断一下，是否不生成证书。
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

	@Override
	public Map<String, String> exportXML(String path, String actinstID) {
		Marshaller mashaller;
		Map<String, String> names = new HashMap<String, String>();
		try {
			CommonDao dao = this.getCommonDao();
			String xmbhHql = ProjectHelper.GetXMBHCondition(this.getXMBH());
			List<BDCS_DJDY_GZ> djdys = dao.getDataList(BDCS_DJDY_GZ.class,
					xmbhHql);
			if (djdys != null && djdys.size() > 0) {
				mashaller = JAXBContext.newInstance(Message.class)
						.createMarshaller();
				Calendar calendar = Calendar.getInstance(); // 创建一个日历对象
				calendar.setTime(new Date()); // 用当前时间初始化日历时间
				String cyear = calendar.get(Calendar.YEAR) + "";
				BDCS_XMXX xmxx = dao.get(BDCS_XMXX.class, this.getXMBH());
				String result = "";
				String msgName = "";
				for (int i = 0; i < djdys.size(); i++) {
					BDCS_DJDY_GZ djdy = djdys.get(i);
					String ywh = packageXml.GetYWLSHByYWH(this.getProject_id());
					BDCS_QL_GZ ql = this.getQL(djdy.getDJDYID());
					List<BDCS_QLR_GZ> qlrs = this.getQLRs(ql.getId());
					List<BDCS_SQR> sqrs = new ArrayList<BDCS_SQR>();
					ProjectServiceImpl serviceImpl = SuperSpringContext
							.getContext().getBean(ProjectServiceImpl.class);
					sqrs = serviceImpl.getSQRList(super.getXMBH());

					if (QLLX.GYJSYDSHYQ.Value.equals(this.getQllx().Value)
							|| QLLX.ZJDSYQ.Value.equals(this.getQllx().Value) || QLLX.JTJSYDSYQ.Value.equals(this.getQllx().Value)) { // 国有建设使用权、宅基地、集体建设用地使用权
						try {
//							BDCS_SHYQZD_GZ zd = dao.get(BDCS_SHYQZD_GZ.class,
//									djdy.getBDCDYID());
							UseLand zd=(UseLand)UnitTools.loadUnit(BDCDYLX.SHYQZD,DJDYLY.GZ, djdy.getBDCDYID());
							Message msg = exchangeFactory.createMessageBySHYQ();
							BDCS_DYBG dybg = null;
							if (DJLX.BGDJ.Value.equals(this.getDjlx().Value)) {
								msg = exchangeFactory.createMessage("true");
								dybg = packageXml.getDYBG(zd.getId());
							}
							super.fillHead(msg, i, ywh,zd.getBDCDYH(),zd.getQXDM(),ql.getLYQLID());
							msg.getHead().setParcelID(
									StringHelper.formatObject(zd.getZDDM()));
							msg.getHead().setEstateNum(
									StringHelper.formatObject(zd.getBDCDYH()));
							// msg.getHead().setRecType("RT110");
//							msg.getHead().setPreEstateNum(
//									StringHelper.formatObject(zd.getBDCDYH()));
							if(zd != null && !StringUtils.isEmpty(zd.getQXDM())){
								msg.getHead().setAreaCode(zd.getQXDM());
							}
							// 目前未维护的字段先手动赋值 diaoliwei 2015-10-15
							if (null != zd) {
								if (StringUtils.isEmpty(zd.getZDT())) {
									zd.setZDT("无");
								}
							}
							if (djdy != null) {

								List<ZTTGYQLR> zttqlr = msg.getData().getGYQLR();
								zttqlr = packageXml.getZTTGYQLRs(zttqlr, qlrs, zd,
										ql, null, null, null, null);
								msg.getData().setGYQLR(zttqlr);

								KTTZDJBXX jbxx = msg.getData().getKTTZDJBXX();
								jbxx = packageXml.getZDJBXX(jbxx, zd, ql, null,
										null);

								KTFZDBHQK bhqk = msg.getData().getZDBHQK();
								bhqk = packageXml.getKTFZDBHQK(bhqk, zd, ql, null,
										null);
								msg.getData().setZDBHQK(bhqk);

								if (DJLX.BGDJ.Value.equals(this.getDjlx().Value)) {
									String zdzhdm = "";
									if (zd != null) {
										zdzhdm = zd.getZDDM();
									}
									KTTGYJZX jzx = msg.getData().getKTTGYJZX();
									jzx = packageXml.getKTTGYJZX(jzx, zdzhdm);

									KTTGYJZD jzd = msg.getData().getKTTGYJZD();
									jzd = packageXml.getKTTGYJZD(jzd, zdzhdm);
								}

								QLFQLJSYDSYQ shyq = msg.getData().getQLJSYDSYQ();
								shyq = packageXml
										.getQLFQLJSYDSYQ(shyq, zd, ql, ywh);

								DJTDJSLSQ sq = msg.getData().getDJSLSQ();
								sq = packageXml.getDJTDJSLSQ(sq, ywh, zd, ql, xmxx,
										null, xmxx.getSLSJ(), null, null,
										null);

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
								msg.getData().setDJGD(gd);
								RealUnit unit=UnitTools.loadUnit(super.getBdcdylx(), DJDYLY.XZ, dybg.getXBDCDYID());
								List<ZDK103> zdk = msg.getData().getZDK103();
								zdk = packageXml.getZDK103(zdk, (UseLand)unit, null, null);
								msg.getData().setZDK103(zdk);

								List<DJFDJSQR> djsqrs = msg.getData().getDJSQR();
								djsqrs = packageXml.getDJSQRs(djsqrs, sqrs,
										zd.getYSDM(), ywh, zd.getBDCDYH());
								msg.getData().setDJSQR(djsqrs);

								FJF100 fj = msg.getData().getFJF100();
								fj = packageXml.getFJF(fj);
							}
							msgName = "Biz" + msg.getHead().getBizMsgID() + ".xml";
							mashaller.marshal(msg, new File(path + msgName));
							if (DJLX.BGDJ.Value.equals(this.getDjlx().Value)){
								result = uploadFile(path + msgName, ConstValue.RECCODE.JSYDSHYQ_BGDJ.Value,actinstID,djdy.getDJDYID(),ql.getId());
							}else{
								result = uploadFile(path + msgName, ConstValue.RECCODE.JSYDSHYQ_GZDJ.Value,actinstID,djdy.getDJDYID(),ql.getId());
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
						
					}
					if (QLLX.GYJSYDSHYQ_FWSYQ.Value.equals(this.getQllx().Value)||QLLX.JTJSYDSYQ_FWSYQ.Value.equals(this.getQllx().Value)||QLLX.ZJDSYQ_FWSYQ.Value.equals(this.getQllx().Value)) { // 房屋所有权
						try {
							BDCS_H_GZ h = dao.get(BDCS_H_GZ.class,
									djdy.getBDCDYID());
							BDCS_ZRZ_XZ zrz_gz = null;
							if (h != null
									&& !StringUtils.isEmpty(h.getZRZBDCDYID())) {
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
							Message msg = exchangeFactory.createMessageByFWSYQ2();
							if (DJLX.BGDJ.Value.equals(this.getDjlx().Value)) {
								msg = exchangeFactory.createMessageByFWSYQ();
							}
							super.fillHead(msg, i, ywh,h.getBDCDYH(),h.getQXDM(),ql.getLYQLID());
							msg.getHead().setParcelID(h.getZDDM());
							msg.getHead().setEstateNum(
									StringHelper.formatObject(h.getBDCDYH()));
							// msg.getHead().setRecType("RT160");
//							msg.getHead().setPreEstateNum(
//									StringHelper.formatObject(h.getBDCDYH()));
							if(h != null && !StringUtils.isEmpty(h.getQXDM())){
								msg.getHead().setAreaCode(h.getQXDM());
							}
							BDCS_C_GZ c = null;
							if (h != null && !StringUtils.isEmpty(h.getCID())) {
								c = dao.get(BDCS_C_GZ.class, h.getCID());
							}
							if (djdy != null) {

								if (DJLX.BGDJ.Value.equals(this.getDjlx().Value)) {
									List<ZDK103> fwk = msg.getData().getZDK103();
									fwk = packageXml.getZDK103H(fwk, h, zrz_gz);
								}

								List<ZTTGYQLR> zttqlr = msg.getData().getGYQLR();
								zttqlr = packageXml.getZTTGYQLRs(zttqlr, qlrs,
										null, ql, h, null, null, null);
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
								fdcqyz = packageXml.getQLTFWFDCQYZ(fdcqyz, h, ql,
										ywh);

								DJTDJSLSQ sq = msg.getData().getDJSLSQ();
								sq = packageXml.getDJTDJSLSQ(sq, ywh, null, ql,
										xmxx, h, xmxx.getSLSJ(), null, null,
										null);

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
								djsqrs = packageXml.getDJSQRs(djsqrs, sqrs,
										h.getYSDM(), ywh, h.getBDCDYH());
								msg.getData().setDJSQR(djsqrs);

								FJF100 fj = msg.getData().getFJF100();
								fj = packageXml.getFJF(fj);

							}
							msgName = "Biz" + msg.getHead().getBizMsgID() + ".xml";
							names.put(djdy.getDJDYID(), msg.getHead().getBizMsgID()
									+ ".xml");
							mashaller.marshal(msg, new File(path + msgName));
							if (DJLX.BGDJ.Value.equals(this.getDjlx().Value)){
								result = uploadFile(path + msgName, ConstValue.RECCODE.FDCQDZ_BGDJ.Value,actinstID,djdy.getDJDYID(),ql.getId());
							}else{
								result = uploadFile(path + msgName, ConstValue.RECCODE.FDCQDZ_GZDJ.Value,actinstID,djdy.getDJDYID(),ql.getId());
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
						
					}

					if (QLLX.JTTDSYQ.Value.equals(this.getQllx().Value)) { // 集体土地所有权
						try {
							BDCS_SYQZD_GZ oland = dao.get(BDCS_SYQZD_GZ.class,
									djdy.getBDCDYID());
							Message msg = exchangeFactory.createMessageByTDSYQ();
							super.fillHead(msg, i, ywh,oland.getBDCDYH(),oland.getQXDM(),ql.getLYQLID());
							msg.getHead().setParcelID(
									StringHelper.formatObject(oland.getZDDM()));
							msg.getHead().setEstateNum(
									StringHelper.formatObject(oland.getBDCDYH()));
							// msg.getHead().setRecType("RT100");
//							msg.getHead().setPreEstateNum(
//									StringHelper.formatObject(oland.getBDCDYH()));
							if(oland != null && !StringUtils.isEmpty(oland.getQXDM())){
								msg.getHead().setAreaCode(oland.getQXDM());
							}
							if (djdy != null) {

								KTTZDJBXX jbxx = msg.getData().getKTTZDJBXX();
								jbxx = packageXml.getZDJBXX(jbxx, null, ql, oland,
										null);

								FJF100 fj = msg.getData().getFJF100();
								fj = packageXml.getFJF(fj);

								KTFZDBHQK bhqk = msg.getData().getZDBHQK();
								bhqk = packageXml.getKTFZDBHQK(bhqk, null, ql,
										oland, null);
								msg.getData().setZDBHQK(bhqk);

								QLFQLTDSYQ tdsyq = msg.getData().getQLFQLTDSYQ();
								tdsyq = packageXml.getQLFQLTDSYQ(tdsyq, oland, ql,
										ywh);

								List<ZTTGYQLR> zttqlr = msg.getData().getGYQLR();
								zttqlr = packageXml.getZTTGYQLRs(zttqlr, qlrs,
										null, ql, null, oland, null, null);
								msg.getData().setGYQLR(zttqlr);

								DJTDJSLSQ sq = msg.getData().getDJSLSQ();
								sq = packageXml.getDJTDJSLSQ(sq, ywh, null, ql,
										xmxx, null, xmxx.getSLSJ(), oland,
										null, null);

								List<DJFDJSJ> sj = msg.getData().getDJSJ();
								sj = packageXml.getDJFDJSJ(oland, ywh,actinstID);
								msg.getData().setDJSJ(sj);
								//9.登记收费(可选)
								List<DJFDJSF> sfList = msg.getData().getDJSF();
								sfList = packageXml.getDJSF(oland, ywh,this.getXMBH());
								msg.getData().setDJSF(sfList);

								//10.审核(可选)
								List<DJFDJSH> sh = msg.getData().getDJSH();
								 sh = packageXml.getDJFDJSH(oland, ywh, this.getXMBH(), actinstID);
								msg.getData().setDJSH(sh);

								//11.缮证(可选)
								List<DJFDJSZ>  sz = packageXml.getDJFDJSZ(oland, ywh, this.getXMBH());
								msg.getData().setDJSZ(sz);

								//11.发证(可选)
								List<DJFDJFZ>   fz = packageXml.getDJFDJFZ(oland, ywh, this.getXMBH());
								msg.getData().setDJFZ(fz);
								
								//12.归档(可选)
								List<DJFDJGD> gd = packageXml.getDJFDJGD(oland, ywh, this.getXMBH());
								msg.getData().setDJGD(gd);
								List<ZDK103> zdk = msg.getData().getZDK103();
								zdk = packageXml.getZDK103(zdk, null, oland, null);

								List<DJFDJSQR> djsqrs = msg.getData().getDJSQR();
								djsqrs = packageXml.getDJSQRs(djsqrs, sqrs,
										oland.getYSDM(), ywh, oland.getBDCDYH());
								msg.getData().setDJSQR(djsqrs);
							}
							names.put(djdy.getDJDYID(), msg.getHead().getBizMsgID()
									+ ".xml");
							msgName = "Biz" + msg.getHead().getBizMsgID() + ".xml";
							mashaller.marshal(msg, new File(path + msgName));
							if (DJLX.BGDJ.Value.equals(this.getDjlx().Value)){
								result = uploadFile(path + msgName, ConstValue.RECCODE.TDSYQ_BGDJ.Value,actinstID,djdy.getDJDYID(),ql.getId());
							}else{
								result = uploadFile(path + msgName, ConstValue.RECCODE.TDSYQ_GZDJ.Value,actinstID,djdy.getDJDYID(),ql.getId());
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					if(null == result){
						Map<String, String> xmlError = new HashMap<String, String>();
						xmlError.put("error", "连接SFTP失败,请检查服务器和前置机的连接是否正常");
						if (DJLX.BGDJ.Value.equals(this.getDjlx().Value)){
							if(QLLX.JTTDSYQ.Value.equals(this.getQllx().Value)){
								YwLogUtil.addSjsbResult(msgName, "", "连接SFTP失败,请检查服务器和前置机的连接是否正常", ConstValue.SF.NO.Value, ConstValue.RECCODE.TDSYQ_BGDJ.Value,ProjectHelper.getpRroinstIDByActinstID(actinstID));	
							}else if (QLLX.GYJSYDSHYQ_FWSYQ.Value.equals(this.getQllx().Value)){
								YwLogUtil.addSjsbResult(msgName, "", "连接SFTP失败,请检查服务器和前置机的连接是否正常", ConstValue.SF.NO.Value, ConstValue.RECCODE.FDCQDZ_BGDJ.Value,ProjectHelper.getpRroinstIDByActinstID(actinstID));
							}else {
								YwLogUtil.addSjsbResult(msgName, "", "连接SFTP失败,请检查服务器和前置机的连接是否正常", ConstValue.SF.NO.Value, ConstValue.RECCODE.JSYDSHYQ_BGDJ.Value,ProjectHelper.getpRroinstIDByActinstID(actinstID));
							}
						}else{
							if(QLLX.JTTDSYQ.Value.equals(this.getQllx().Value)){
								YwLogUtil.addSjsbResult(msgName, "", "连接SFTP失败,请检查服务器和前置机的连接是否正常", ConstValue.SF.NO.Value, ConstValue.RECCODE.TDSYQ_GZDJ.Value,ProjectHelper.getpRroinstIDByActinstID(actinstID));	
							}else if (QLLX.GYJSYDSHYQ_FWSYQ.Value.equals(this.getQllx().Value)){
								YwLogUtil.addSjsbResult(msgName, "", "连接SFTP失败,请检查服务器和前置机的连接是否正常", ConstValue.SF.NO.Value, ConstValue.RECCODE.FDCQDZ_GZDJ.Value,ProjectHelper.getpRroinstIDByActinstID(actinstID));
							}else {
								YwLogUtil.addSjsbResult(msgName, "", "连接SFTP失败,请检查服务器和前置机的连接是否正常", ConstValue.SF.NO.Value, ConstValue.RECCODE.JSYDSHYQ_GZDJ.Value,ProjectHelper.getpRroinstIDByActinstID(actinstID));
							}
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
}
