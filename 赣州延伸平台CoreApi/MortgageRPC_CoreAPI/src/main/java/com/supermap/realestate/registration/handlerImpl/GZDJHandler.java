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
import com.supermap.realestate.registration.model.BDCS_H_XZY;
import com.supermap.realestate.registration.model.BDCS_LJZ_XZ;
import com.supermap.realestate.registration.model.BDCS_NYD_GZ;
import com.supermap.realestate.registration.model.BDCS_NYD_XZ;
import com.supermap.realestate.registration.model.BDCS_QLR_GZ;
import com.supermap.realestate.registration.model.BDCS_QLR_XZ;
import com.supermap.realestate.registration.model.BDCS_QL_GZ;
import com.supermap.realestate.registration.model.BDCS_QL_XZ;
import com.supermap.realestate.registration.model.BDCS_SHYQZD_GZ;
import com.supermap.realestate.registration.model.BDCS_SHYQZD_XZ;
import com.supermap.realestate.registration.model.BDCS_SLLM_GZ;
import com.supermap.realestate.registration.model.BDCS_SLLM_XZ;
import com.supermap.realestate.registration.model.BDCS_SQR;
import com.supermap.realestate.registration.model.BDCS_SYQZD_GZ;
import com.supermap.realestate.registration.model.BDCS_SYQZD_XZ;
import com.supermap.realestate.registration.model.BDCS_TDYT_GZ;
import com.supermap.realestate.registration.model.BDCS_TDYT_LS;
import com.supermap.realestate.registration.model.BDCS_TDYT_XZ;
import com.supermap.realestate.registration.model.BDCS_XMXX;
import com.supermap.realestate.registration.model.BDCS_YHYDZB_GZ;
import com.supermap.realestate.registration.model.BDCS_YHYDZB_XZ;
import com.supermap.realestate.registration.model.BDCS_YHZK_GZ;
import com.supermap.realestate.registration.model.BDCS_YHZK_XZ;
import com.supermap.realestate.registration.model.BDCS_ZH_GZ;
import com.supermap.realestate.registration.model.BDCS_ZRZ_XZ;
import com.supermap.realestate.registration.model.BDCS_ZRZ_XZY;
import com.supermap.realestate.registration.model.WFD_MAPPING;
import com.supermap.realestate.registration.model.interfaces.Building;
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
import com.supermap.realestate.registration.util.ConstHelper;
import com.supermap.realestate.registration.util.ConstValue;
import com.supermap.realestate.registration.util.ConstValue.BDCDYLX;
import com.supermap.realestate.registration.util.ConstValue.DJDYLY;
import com.supermap.realestate.registration.util.ConstValue.DJLX;
import com.supermap.realestate.registration.util.ConstValue.QLLX;
import com.supermap.realestate.registration.util.ConstValue.SF;
import com.supermap.realestate.registration.util.ProjectHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.realestate.registration.util.YwLogUtil;
import com.supermap.wisdombusiness.core.SuperHelper;
import com.supermap.wisdombusiness.core.SuperSpringContext;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;

/*
 1、国有建设用地使用权更正登记
 2、集体建设用地使用权更正登记（未配置）
 3、宅基地使用权更正登记（未配置）
 4、国有建设用地使用权/房屋所有权更正登记
 5、集体建设用地使用权/房屋所有权更正登记（未配置）
 6、宅基地使用权/房屋所有权更正登记（未配置）
 */
/**
 * 
 * 更正登记处理类
 * 
 * @ClassName: GZDJHandler
 * @author liushufeng
 * @date 2015年9月8日 下午10:35:03
 */
public class GZDJHandler extends DJHandlerBase implements DJHandler {

	/**
	 * 
	 * @param info
	 */
	public GZDJHandler(ProjectInfo info) {
		super(info);
	}

	/**
	 * 添加不动产单元
	 */
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
			boolean ytok = super.copyTDYT_XZ_TO_GZ(bdcdyid, bdcdyid_new);
			if (!ytok) {
				super.setErrMessage("添加失败！保存单元的土地用途信息错误，请重新选择单元");
				return false;
			}
		}else if (this.getBdcdylx().equals(BDCDYLX.SYQZD)) {
			unit = CopyOwnershipLand(bdcdyid, bdcdyid_new);
			boolean ytok = super.copyTDYT_XZ_TO_GZ(bdcdyid, bdcdyid_new);
			if (!ytok) {
				super.setErrMessage("添加失败！保存单元的土地用途信息错误，请重新选择单元");
				return false;
			}
		}else if(this.getBdcdylx().equals(BDCDYLX.LD))
		{
			unit=CopyForest(bdcdyid, bdcdyid_new);
			boolean ytok = super.copyTDYT_XZ_TO_GZ(bdcdyid, bdcdyid_new);
			if (!ytok) {
				super.setErrMessage("添加失败！保存单元的土地用途信息错误，请重新选择单元");
				return false;
			}
		}else if(this.getBdcdylx().equals(BDCDYLX.HY))
		{
			unit=CopySea(bdcdyid, bdcdyid_new);
		}else if(this.getBdcdylx().equals(BDCDYLX.NYD)){
			unit=CopyNYDLand(bdcdyid, bdcdyid_new);
			boolean ytok = super.copyTDYT_XZ_TO_GZ(bdcdyid, bdcdyid_new);
			if (!ytok) {
				super.setErrMessage("添加失败！保存单元的土地用途信息错误，请重新选择单元");
				return false;
			}
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
		dao.flush();
		// 把权利人拷贝过来放到申请人里边 add by diaoliwei 2015-7-31
		//copyApplicant(bdcs_djdy_gz.getDJDYID());
		copyApplicantFromGZ(bdcs_djdy_gz.getDJDYID());
		dao.flush();
		bsuccess = true;
		return bsuccess;
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
	
	
	
	
	/**
	 * 写入登记簿
	 */
	@Override
	public boolean writeDJB() {

		/*if (super.isCForCFING()) {
			return false;
		}*/

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
				if(getBdcdylx().equals(BDCDYLX.SHYQZD) || getBdcdylx().equals(BDCDYLX.SYQZD)
						||getBdcdylx().equals(BDCDYLX.LD) || getBdcdylx().equals(BDCDYLX.NYD)){
					CopySHYQZDOther(bdcs_djdy_gz.getBDCDYID(), xz_bdcdyid);
				}
				// super.CopyGZDJDYToXZAndLS(bdcs_djdy_gz.getId());
				ModefineSHYQZDTxtype(bdcs_djdy_gz.getBDCDYLX(),xz_bdcdyid);
			}
		}
		this.SetSFDB();
		getCommonDao().flush();
		super.alterCachedXMXX();
		return true;
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
	
	//改变宗地和自然幢的图形维护状态
	private void ModefineSHYQZDTxtype(String bdcdylx,String bdcdyid_xz){
		String bdcdyid_xz_new = "";
		String bdcdyid_xz_zrz = "";
		String bdcdyid_xz_zrzy = "";
		if(bdcdylx.equals("031")){
			List<BDCS_H_XZ> ListH = getCommonDao().getDataList(
					BDCS_H_XZ.class,"BDCDYID='" + bdcdyid_xz + "'");
			bdcdyid_xz_new = ListH.get(0).getZDBDCDYID();
			bdcdyid_xz_zrz = ListH.get(0).getZRZBDCDYID();
			List<BDCS_ZRZ_XZ> ListZRZ = getCommonDao().getDataList(
					BDCS_ZRZ_XZ.class, "BDCDYID='" + bdcdyid_xz_zrz + "'");
			if (ListZRZ != null && ListZRZ.size() > 0) {
				for (BDCS_ZRZ_XZ bdcs_zrz_xz : ListZRZ) {
					if(!StringHelper.isEmpty(bdcs_zrz_xz.getTXWHTYPE())&&"1".equals(bdcs_zrz_xz.getTXWHTYPE())){
						bdcs_zrz_xz.setTXWHTYPE("2");
						getCommonDao().update(bdcs_zrz_xz);
					}
				}
			}
		}else if(bdcdylx.equals("032")){
			List<BDCS_H_XZY> ListYCH = getCommonDao().getDataList(
					BDCS_H_XZY.class,"BDCDYID='" + bdcdyid_xz + "'");
			bdcdyid_xz_new = ListYCH.get(0).getZDBDCDYID();
			bdcdyid_xz_zrzy = ListYCH.get(0).getZRZBDCDYID();
			List<BDCS_ZRZ_XZY> ListZRZY = getCommonDao().getDataList(
					BDCS_ZRZ_XZY.class, "BDCDYID='" + bdcdyid_xz_zrzy + "'");
			if (ListZRZY != null && ListZRZY.size() > 0) {
				for (BDCS_ZRZ_XZY bdcs_zrz_xzy : ListZRZY) {
					if(!StringHelper.isEmpty(bdcs_zrz_xzy.getTXWHTYPE())&&"1".equals(bdcs_zrz_xzy.getTXWHTYPE())){
						bdcs_zrz_xzy.setTXWHTYPE("2");
						getCommonDao().update(bdcs_zrz_xzy);
					}
				}
			}
		}else if(bdcdylx.equals("02")){
			bdcdyid_xz_new = bdcdyid_xz;
			String hqlCondition_xz = "BDCDYID='" + bdcdyid_xz_new + "'";
			List<BDCS_SHYQZD_XZ> ListZD = getCommonDao().getDataList(
					BDCS_SHYQZD_XZ.class, hqlCondition_xz);
			if (ListZD != null && ListZD.size() > 0) {
				for (BDCS_SHYQZD_XZ bdcs_shyqzd_xz : ListZD) {
					if(!StringHelper.isEmpty(bdcs_shyqzd_xz.getTXWHTYPE())&&"1".equals(bdcs_shyqzd_xz.getTXWHTYPE())){
						bdcs_shyqzd_xz.setTXWHTYPE("2");
						getCommonDao().update(bdcs_shyqzd_xz);
					}
				}
			}
		} 
//		else if (bdcdylx.equals("05")) {
//			String hqlCondition_xz = "BDCDYID='" + bdcdyid_xz + "'";
//			List<BDCS_SLLM_XZ> sllm_list = getCommonDao().getDataList(
//					BDCS_SLLM_XZ.class, hqlCondition_xz);
//			if (sllm_list != null && sllm_list.size() > 0) {
//				for (BDCS_SLLM_XZ sllm : sllm_list) {
//					if(!StringHelper.isEmpty(sllm.getTXWHTYPE())&&"1".equals(sllm.getTXWHTYPE())){
//						sllm.setTXWHTYPE("2");
//						getCommonDao().update(sllm);
//					}
//				}
//			}
//		}
		
		
	}
	
	private void CopySHYQZDOther(String bdcdyid_gz,String bdcdyid_xz){
		String hqlCondition_xz = "BDCDYID='" + bdcdyid_xz + "'";
		String hqlCondition_gz = "BDCDYID='" + bdcdyid_gz + "'";
		// 先删除
		getCommonDao().deleteEntitysByHql(BDCS_TDYT_XZ.class,
				hqlCondition_xz);
		//将历史层的数据的bdcdyid缓冲工作层对应的bdcdyid
		List<BDCS_TDYT_LS> desListTDYT_ls = getCommonDao().getDataList(
				BDCS_TDYT_LS.class, hqlCondition_xz);
		if (desListTDYT_ls != null && desListTDYT_ls.size() > 0) {
			for (BDCS_TDYT_LS tdyt_ls : desListTDYT_ls) {
				tdyt_ls.setBDCDYID(bdcdyid_gz);
				getCommonDao().update(tdyt_ls);
			}
		}
		// 再拷贝
		List<BDCS_TDYT_GZ> desListTDYT = getCommonDao().getDataList(
				BDCS_TDYT_GZ.class, hqlCondition_gz);
		if (desListTDYT != null && desListTDYT.size() > 0) {
			for (BDCS_TDYT_GZ tdyt : desListTDYT) {
				BDCS_TDYT_XZ tdyt_xz = new BDCS_TDYT_XZ();
				try {
					PropertyUtils.copyProperties(tdyt_xz, tdyt);
					tdyt_xz.setId((String) SuperHelper.GeneratePrimaryKey());
					tdyt_xz.setBDCDYID(bdcdyid_xz);
					tdyt_xz.setTDYTMC(ConstHelper.getNameByValue("TDYT", tdyt_xz.getTDYT().trim()));
					getCommonDao().save(tdyt_xz);
				} catch (Exception e) {
				}
				BDCS_TDYT_LS tdyt_ls = new BDCS_TDYT_LS();
				try {
					PropertyUtils.copyProperties(tdyt_ls, tdyt_xz);
					getCommonDao().save(tdyt_ls);
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
		} else if (getQllx().equals(ConstValue.QLLX.LDSYQ)||getQllx().equals(ConstValue.QLLX.LDSHYQ_SLLMSYQ)
			|| getQllx().equals(ConstValue.QLLX.LDSYQ_SLLMSYQ)|| getQllx().equals(ConstValue.QLLX.TDCBJYQ_SLLMSYQ)) {
			BDCS_SLLM_GZ bdcs_sllm_gz = dao.get(BDCS_SLLM_GZ.class,
					bdcdyid);
			if(bdcs_sllm_gz!=null){
				dao.deleteEntity(bdcs_sllm_gz);
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
			String djdyid=djdy.getDJDYID();
			String _hqlCondition = MessageFormat.format("XMBH=''{0}'' AND DJDYID=''{1}''", getXMBH(), djdyid);
			RightsTools.deleteRightsAllByCondition(DJDYLY.GZ, _hqlCondition);// 删除权利、附属权利、权利人、证书、权地证人关系
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
		if("LD".equals(super.getBdcdylx().toString())||"HY".equals(this.getBdcdylx().toString())){
			return super.exportXMLother(path, actinstID,"YES");
		}
		if("NYD".equals(super.getBdcdylx().toString())){
			return super.exportXML(path, actinstID);
		}
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

					if (QLLX.HYSYQ.Value.equals(this.getQllx().Value) || QLLX.WJMHDSYQ.Value.equals(this.getQllx().Value)) { 
						// 海域(含无居民海岛)变更
						
						
					}
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
								super.fillShyqZdData(msg,ywh,zd,sqrs,qlrs,ql,xmxx,actinstID);
								if (DJLX.BGDJ.Value.equals(this.getDjlx().Value)) {
									RealUnit unit=UnitTools.loadUnit(super.getBdcdylx(), DJDYLY.XZ, dybg.getXBDCDYID());
									msg.getData().setKTTGYJZD(packageXml.getKTTGYJZD(msg.getData().getKTTGYJZD(), zd.getZDDM()));
									msg.getData().setKTTGYJZX(packageXml.getKTTGYJZX(msg.getData().getKTTGYJZX(), zd.getZDDM()));
									List<ZDK103> zdk = msg.getData().getZDK103();
									zdk = packageXml.getZDK103(zdk, (UseLand)unit, null, null);
									msg.getData().setZDK103(zdk);
								}else if(DJLX.GZDJ.Value.equals(this.getDjlx().Value)){
									if("1".equals(zd.getTXWHTYPE())){
										RealUnit unit=UnitTools.loadUnit(super.getBdcdylx(), DJDYLY.XZ, dybg.getXBDCDYID());
										List<ZDK103> zdk = msg.getData().getZDK103();
										zdk = packageXml.getZDK103(zdk, (UseLand)unit, null, null);
										msg.getData().setZDK103(zdk);
									}
								}
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
					if (QLLX.GYJSYDSHYQ_FWSYQ.Value.equals(this.getQllx().Value)||QLLX.JTJSYDSYQ_FWSYQ.Value.equals(this.getQllx().Value)||QLLX.ZJDSYQ_FWSYQ.Value.equals(this.getQllx().Value)) {
						// 房屋所有权
						try {
							BDCS_H_GZ h = dao.get(BDCS_H_GZ.class,
									djdy.getBDCDYID());
							BDCS_ZRZ_XZ zrz_xz = null;
							if (h != null
									&& !StringUtils.isEmpty(h.getZRZBDCDYID())) {
								zrz_xz = dao.get(BDCS_ZRZ_XZ.class, h.getZRZBDCDYID());
								if(zrz_xz != null){
									zrz_xz.setGHYT(h.getGHYT()); //自然幢的ghyt取户的ghyt
									zrz_xz.setFWJG(zrz_xz.getFWJG() == null || zrz_xz.getFWJG().equals("") ? h.getFWJG() : zrz_xz.getFWJG());
								}
							}
							BDCS_LJZ_XZ ljz_xz = null;
							if (h != null && !StringUtils.isEmpty(h.getLJZID())) {
								ljz_xz = dao.get(BDCS_LJZ_XZ.class, h.getLJZID());
							}
							if (h != null) {
								BDCS_SHYQZD_XZ zd = dao.get(BDCS_SHYQZD_XZ.class, h.getZDBDCDYID());
								h.setZDDM(zd.getZDDM());
								if(zrz_xz != null){
									zrz_xz.setZDDM(zd.getZDDM());
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

								super.fillFwData(msg, ywh ,ljz_xz, c ,zrz_xz, h ,sqrs,qlrs,ql,xmxx,actinstID);

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

								super.fillSyqZdData( msg, ywh, oland, sqrs, qlrs, ql, xmxx, actinstID) ;
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
					if(result.equals("")||result==null){
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
	 * 把权利人拷贝过来放到申请人里边
	 * 
	 * @author diaoliwei
	 * @Data 2015-7-31
	 * @param bdcdyid
	 */
	protected void copyApplicant(String bdcdyid) {
		CommonDao dao = getCommonDao();

		String qlrSql = MessageFormat.format(
				"QLID IN (SELECT id FROM BDCS_QL_XZ WHERE QLLX IN(''3'',''4'',''5'',''6'',''7'',''8'',''9'',''10'',''15'' ,''24'',''36'') AND DJDYID=''{0}'')",
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
				bdcs_sqr.setQLMJ(StringHelper.formatObject(bdcs_qlr_xz
						.getQLMJ()));
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
	/**
	 * 把权利人拷贝过来放到申请人里边
	 * 
	 * @author diaoliwei
	 * @Data 2015-7-31
	 * @param bdcdyid_xz
	 */
	protected void copyApplicantFromGZ(String bdcdyid) {
		CommonDao dao = getCommonDao();

		String qlrSql = MessageFormat.format(
				"QLID IN (SELECT id FROM BDCS_QL_GZ WHERE QLLX IN(''3'',''4'',''5'',''6'',''7'',''8'',''9'',''10'',''11'',''12'',''15'' ,''24'',''36'') AND DJDYID=''{0}'' AND XMBH=''{1}'')",
				bdcdyid, getXMBH());
		List<BDCS_QLR_GZ> qlrs = dao.getDataList(BDCS_QLR_GZ.class, qlrSql);
		for (BDCS_QLR_GZ bdcs_qlr_gz : qlrs) {
			String zjhm = bdcs_qlr_gz.getZJH();
			String qlrmc = bdcs_qlr_gz.getQLRMC();
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
					//权利人关联申请人ID
					bdcs_qlr_gz.setSQRID(sqrs.get(0).getId());
					dao.update(bdcs_qlr_gz);
				}
			}
			// 判断申请人是否已经添加过，如果添加过，就不再添加
			if (!flag) {
				String sqrid = getPrimaryKey();
				BDCS_SQR bdcs_sqr = new BDCS_SQR();
				bdcs_sqr.setGYFS(bdcs_qlr_gz.getGYFS());
				bdcs_sqr.setFZJG(bdcs_qlr_gz.getFZJG());
				bdcs_sqr.setGJDQ(bdcs_qlr_gz.getGJ());
				bdcs_sqr.setGZDW(bdcs_qlr_gz.getGZDW());
				bdcs_sqr.setXB(bdcs_qlr_gz.getXB());
				bdcs_sqr.setHJSZSS(bdcs_qlr_gz.getHJSZSS());
				bdcs_sqr.setSSHY(bdcs_qlr_gz.getSSHY());
				bdcs_sqr.setYXBZ(bdcs_qlr_gz.getYXBZ());
				bdcs_sqr.setQLBL(bdcs_qlr_gz.getQLBL());
				bdcs_sqr.setQLMJ(StringHelper.formatObject(bdcs_qlr_gz.getQLMJ()));
				bdcs_sqr.setSQRXM(bdcs_qlr_gz.getQLRMC());
				bdcs_sqr.setSQRLB("1");
				bdcs_sqr.setSQRLX(bdcs_qlr_gz.getQLRLX());
				bdcs_sqr.setDZYJ(bdcs_qlr_gz.getDZYJ());
				bdcs_sqr.setLXDH(bdcs_qlr_gz.getDH());
				bdcs_sqr.setZJH(bdcs_qlr_gz.getZJH());
				bdcs_sqr.setZJLX(bdcs_qlr_gz.getZJZL());
				bdcs_sqr.setTXDZ(bdcs_qlr_gz.getDZ());
				bdcs_sqr.setYZBM(bdcs_qlr_gz.getYB());
				bdcs_sqr.setXMBH(getXMBH());
				bdcs_sqr.setId(sqrid);
				 bdcs_sqr.setGLQLID(bdcs_qlr_gz.getQLID());
				dao.save(bdcs_sqr);
				//权利人关联申请人ID
				bdcs_qlr_gz.setSQRID(bdcs_sqr.getId());
				dao.update(bdcs_qlr_gz);
			}
		}
	}
	/*
	 * private void removeApplicant(String xmbh){
	 * 
	 * }
	 */

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
