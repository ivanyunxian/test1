package com.supermap.realestate.registration.handlerImpl;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
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
import com.supermap.realestate.registration.dataExchange.nydsyq.QLNYDSYQ;
import com.supermap.realestate.registration.dataExchange.shyq.KTFZDBHQK;
import com.supermap.realestate.registration.dataExchange.shyq.KTTZDJBXX;
import com.supermap.realestate.registration.dataExchange.shyq.ZDK103;
import com.supermap.realestate.registration.dataExchange.utils.packageXml;
import com.supermap.realestate.registration.handler.DJHandler;
import com.supermap.realestate.registration.model.BDCS_C_GZ;
import com.supermap.realestate.registration.model.BDCS_DJDY_GZ;
import com.supermap.realestate.registration.model.BDCS_DJDY_LS;
import com.supermap.realestate.registration.model.BDCS_DJDY_XZ;
import com.supermap.realestate.registration.model.BDCS_FSQL_GZ;
import com.supermap.realestate.registration.model.BDCS_FSQL_LS;
import com.supermap.realestate.registration.model.BDCS_FSQL_XZ;
import com.supermap.realestate.registration.model.BDCS_H_GZ;
import com.supermap.realestate.registration.model.BDCS_LJZ_GZ;
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
import com.supermap.realestate.registration.model.BDCS_SHYQZD_XZ;
import com.supermap.realestate.registration.model.BDCS_SQR;
import com.supermap.realestate.registration.model.BDCS_SYQZD_GZ;
import com.supermap.realestate.registration.model.BDCS_TDYT_GZ;
import com.supermap.realestate.registration.model.BDCS_TDYT_LS;
import com.supermap.realestate.registration.model.BDCS_TDYT_XZ;
import com.supermap.realestate.registration.model.BDCS_XMXX;
import com.supermap.realestate.registration.model.BDCS_ZRZ_GZ;
import com.supermap.realestate.registration.model.BDCS_ZS_GZ;
import com.supermap.realestate.registration.model.BDCS_ZS_LS;
import com.supermap.realestate.registration.model.BDCS_ZS_XZ;
import com.supermap.realestate.registration.model.DCS_FSQL_GZ;
import com.supermap.realestate.registration.model.DCS_QLR_GZ;
import com.supermap.realestate.registration.model.DCS_QL_GZ;
import com.supermap.realestate.registration.model.DCS_TDYT_GZ;
import com.supermap.realestate.registration.model.WFD_MAPPING;
import com.supermap.realestate.registration.model.YC_SC_H_XZ;
import com.supermap.realestate.registration.model.interfaces.AgriculturalLand;
import com.supermap.realestate.registration.model.interfaces.House;
import com.supermap.realestate.registration.model.interfaces.LandAttach;
import com.supermap.realestate.registration.model.interfaces.OwnerLand;
import com.supermap.realestate.registration.model.interfaces.RealUnit;
import com.supermap.realestate.registration.model.interfaces.Rights;
import com.supermap.realestate.registration.model.interfaces.RightsHolder;
import com.supermap.realestate.registration.model.interfaces.SubRights;
import com.supermap.realestate.registration.model.interfaces.TDYT;
import com.supermap.realestate.registration.model.interfaces.UseLand;
import com.supermap.realestate.registration.model.xmlExportmodel.MessageExport;
import com.supermap.realestate.registration.service.impl.ProjectServiceImpl;
import com.supermap.realestate.registration.tools.EntityTools;
import com.supermap.realestate.registration.tools.RightsHolderTools;
import com.supermap.realestate.registration.tools.RightsTools;
import com.supermap.realestate.registration.tools.UnitTools;
import com.supermap.realestate.registration.util.ConfigHelper;
import com.supermap.realestate.registration.util.ConstValue;
import com.supermap.realestate.registration.util.ConstValue.BDCDYLX;
import com.supermap.realestate.registration.util.ConstValue.CFLX;
import com.supermap.realestate.registration.util.ConstValue.DJDYLY;
import com.supermap.realestate.registration.util.ConstValue.DJLX;
import com.supermap.realestate.registration.util.ConstValue.DJZT;
import com.supermap.realestate.registration.util.ConstValue.QLLX;
import com.supermap.realestate.registration.util.GetProperties;
import com.supermap.realestate.registration.util.Global;
import com.supermap.realestate.registration.util.ObjectHelper;
import com.supermap.realestate.registration.util.ProjectHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.realestate.registration.util.YwLogUtil;
import com.supermap.wisdombusiness.core.SuperHelper;
import com.supermap.wisdombusiness.core.SuperSpringContext;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import com.supermap.wisdombusiness.web.ResultMessage;

/*
 1、集体土地所有权初始登记（待测）
 2、集体土地使用权初始登记（待测）
 3、宅基地使用权初始登记（待测）
 4、国有建设用地使用权初始登记
 5、国有建设用地使用权/房屋（按户，按幢）所有权初始登记
 6、集体建设用地使用权/房屋所有权初始登记（待测）
 7、宅基地使用权/房屋所有权初始登记（待测）
 8、林地使用权初始登记（待测）
 9、森林林木所有权初始登记（待测）
 10、宗海使用权初始登记（待测）
 11、国有建设用地使用权/构筑物所有权（待测）
 12、宅基地使用权/构筑物所有权（待测）
 13、集体建设用地使用权/构筑物所有权（待测）
 */
/**
 * 
 * 初始登记处理类
 * @ClassName: CSDJHandler_LuZhou
 * @author liushufeng
 * @date 2015年9月8日 下午10:29:35
 */
public class CSDJHandler extends DJHandlerBase implements DJHandler {

	/**
	 * 构造函数
	 * 
	 * @param info
	 */
	public CSDJHandler(ProjectInfo info) {
		super(info);
	}

	/**
	 * 添加不动产单元
	 */
	@Override
	public boolean addBDCDY(String bdcdyid) {
		if (StringHelper.isEmpty(bdcdyid))
			return false;
		String[] ids = bdcdyid.split(",");
		if (ids == null || ids.length <= 0)
			return false;
		for (String id : ids) {
			if (StringHelper.isEmpty(id))
				continue;
			// 拷贝不动产单元，生成登记单元记录
			ResultMessage msg = addbdcdy(id);
			// 把权利人拷贝过来放到申请人里边
			if (msg.getSuccess().equals("false")) {
				super.setErrMessage(msg.getMsg());
				return false;
			}
		}
		this.getCommonDao().flush();
		return true;
	}

	/**
	 * 把调查库中的权利人拷贝到申请人
	 * 
	 * @作者 刘树峰
	 * @创建时间 2015年6月30日上午12:07:26
	 * @param bdcdyid
	 */
	private void copyApplicant(String bdcdyid, String qlid) {
		String hql = MessageFormat.format(" QLID IN (SELECT id FROM DCS_QL_GZ WHERE DJDYID=''{0}'')", bdcdyid);
		List<DCS_QLR_GZ> dcqlrs = getCommonDao().getDataList(DCS_QLR_GZ.class, hql);
		if (dcqlrs == null || dcqlrs.size() <= 0)
			return;
		for (DCS_QLR_GZ qlr : dcqlrs) {
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
				List<BDCS_SQR> sqrlist = getCommonDao().getDataList(BDCS_SQR.class, Sql);
				if (sqrlist != null && sqrlist.size() > 0) {
					bexists = true;
				}
			}
			if (!bexists) {
				BDCS_SQR sqr;
				sqr = ObjectHelper.copyDCQLRtoSQR(qlr);
				sqr.setXMBH(super.getXMBH());
				sqr.setGLQLID(qlid);
				getCommonDao().save(sqr);
			}
		}
	}
	/**
	 * 更新森林林木的权利及附属权利信息
	 * @作者 海豹
	 * @创建时间 2016年3月17日下午5:20:28
	 * @param ql
	 * @param fsql
	 * @param bdcdyid
	 */
	private void UpdateSllmQLinfo(BDCS_QL_GZ ql,BDCS_FSQL_GZ fsql,String bdcdyid)
	{
		if(!StringHelper.isEmpty(ql))
		{
			List<DCS_QL_GZ> dcqls=getCommonDao().getDataList(DCS_QL_GZ.class, "DJDYID ='"+bdcdyid+"'");
			if(!StringHelper.isEmpty(dcqls) && dcqls.size()>0)
			{
				DCS_QL_GZ dcql=dcqls.get(0);
				if(!StringHelper.isEmpty(dcql.getQLQSSJ()))
				{
				ql.setQLQSSJ(dcql.getQLQSSJ());
				}
				if(!StringHelper.isEmpty(dcql.getQLJSSJ()))
				{
				ql.setQLJSSJ(dcql.getQLJSSJ());
				}
				List<DCS_FSQL_GZ> dcfsqls=getCommonDao().getDataList(DCS_FSQL_GZ.class, "QLID ='"+dcql.getId()+"'");
				if(!StringHelper.isEmpty(dcfsqls) && dcfsqls.size()>0)
				{
				DCS_FSQL_GZ dcfsql=dcfsqls.get(0);
				if(!StringHelper.isEmpty(dcfsql))
				{
				   fsql.setSLLMSYQR1(dcfsql.getSLLMSYQR1());
				   fsql.setSLLMSYQR2(dcfsql.getSLLMSYQR2());
				}
				}
			}
		}
	}

	/**
	 * 内部方法：添加不动产单元
	 * @Title: addbdcdy
	 * @author:liushufeng
	 * @date：2015年7月19日 上午3:08:02
	 * @param bdcdyid
	 * @return
	 */
	private ResultMessage addbdcdy(String bdcdyid) {
		ResultMessage msg = new ResultMessage();
		BDCS_DJDY_GZ djdy = null;
		CommonDao dao = this.getCommonDao();

		// 判断在登记库中是否已经存在了，如果存在，返回false，不能重复登记
		RealUnit dy = UnitTools.loadUnit(this.getBdcdylx(), DJDYLY.GZ, bdcdyid);
		if (dy != null) {
			msg.setSuccess("false");
			msg.setMsg("不能重复登记同一个单元！");
			return msg;
		}
		BDCDYLX bdcdylx = this.getBdcdylx();
		RealUnit _srcunit = UnitTools.loadUnit(bdcdylx, DJDYLY.DC, bdcdyid);
		if (_srcunit == null) {
			msg.setSuccess("false");
			msg.setMsg("找不到单元！");
			return msg;
		}
		// 如果是户或者自然幢，要判断在现状库中是否有关联的宗地，如果没有，不能进行登记
		if (Global.ISRELATIONLAND) // 是否关联宗地
		{
			if (bdcdylx.equals(BDCDYLX.H) || bdcdylx.equals(BDCDYLX.ZRZ) || bdcdylx.equals(BDCDYLX.LD)) {
				LandAttach _landAttach = (LandAttach) _srcunit;
				RealUnit zdunit = UnitTools.loadUnit(BDCDYLX.SHYQZD, DJDYLY.XZ, _landAttach.getZDBDCDYID());
				if (zdunit == null) {
					msg.setSuccess("false");
					msg.setMsg("在登记库中找不到关联的宗地！");
					return msg;
				}

			}
		}
		RealUnit _desunit = null;
		_desunit = UnitTools.copyUnit(_srcunit, _srcunit.getBDCDYLX(), DJDYLY.GZ);
		_desunit.setXMBH(this.getXMBH());
		djdy = super.createDJDY(_desunit, DJDYLY.GZ);

		// 生成权利信息和附属权利
		BDCS_QL_GZ ql = super.createQL(djdy, _desunit);
		BDCS_FSQL_GZ fsql = super.createFSQL(djdy.getDJDYID());
		fsql.setQLID(ql.getId());
		ql.setFSQLID(fsql.getId());

		// 如果是使用权宗地，要填写使用权面积,或者其他的
		if (_desunit.getBDCDYLX().equals(BDCDYLX.SHYQZD)) {
			fsql.setSYQMJ(((UseLand) _desunit).getZDMJ());
			ql.setQDJG(((UseLand) _desunit).getJG());
		}
		// 如果是所有权宗地，要填写使用权面积,或者其他的
		if (_desunit.getBDCDYLX().equals(BDCDYLX.SYQZD)) {
			fsql.setSYQMJ(((OwnerLand) _desunit).getZDMJ());
		}
		//拷贝权利中权利起始时间及结束时间，和附属权利中的森林林木使用权人及森林林木所有权人
		if(_desunit.getBDCDYLX().equals(BDCDYLX.LD))
		{
			UpdateSllmQLinfo(ql,fsql,bdcdyid);
		}
		// 如果是农用地,所有权 土地用途可以显示多个
		if (_desunit!=null&&(_desunit.getBDCDYLX().equals(BDCDYLX.NYD)||_desunit.getBDCDYLX().equals(BDCDYLX.SYQZD))) {
//			List<Map> tdyts = baseCommonDao.getDataListByFullSql("select tdyd from bdcdck.bdcs_tdyt_gz where bdcdyid='"+ bdcdyid +"'");
			CommonDao commonDao = SuperSpringContext.getContext().getBean(CommonDao.class);
			String ytClassName = EntityTools.getEntityName("BDCS_TDYT", DJDYLY.DC);
			Class<?> ytClass = EntityTools.getEntityClass(ytClassName);
			@SuppressWarnings("unchecked")
			List<TDYT> listyts = (List<TDYT>) commonDao.getDataList(ytClass, "BDCDYID='" + bdcdyid + "'");
			@SuppressWarnings("unchecked")
			List<DCS_TDYT_GZ> listyts_gz = (List<DCS_TDYT_GZ>) commonDao.getDataList(ytClass, "BDCDYID='" + bdcdyid + "'");
			if (listyts != null && listyts_gz.size() > 0) {//  将DC库的 TDYT 拷贝到 BDCK TDYT表
				for (DCS_TDYT_GZ tdyt : listyts_gz) {
					BDCS_TDYT_GZ yt_gz = new BDCS_TDYT_GZ();
					try {
						PropertyUtils.copyProperties(yt_gz, tdyt);
						yt_gz.setId((String) SuperHelper
								.GeneratePrimaryKey());
						yt_gz.setBDCDYID(bdcdyid);
						yt_gz.setXMBH(getXMBH());
						getCommonDao().save(yt_gz);
					} catch (Exception e) {
					}
				}
			}
			String tdyt ="";
			for (int i = 0; i < listyts.size(); i++) {
				tdyt += listyts.get(i).getTDYTMC();
				if (i+1 < listyts.size()) {
					tdyt +="、";
				}
			}
			_desunit.setYT(tdyt);
		}
		
		// 更新调查库里边相应的登记状态
		_srcunit.setDJZT(DJZT.DJZ.Value);

		copyApplicant(bdcdyid, ql.getId());
	
		dao.update(_srcunit);
		// 保存权利和附属权利
		dao.save(_desunit);
		dao.save(djdy);
		dao.save(ql);
		dao.save(fsql);
		msg.setSuccess("true");
		return msg;
	}

	/**
	 * 写入登记薄
	 */
	@Override
	public boolean writeDJB() {
		String strSqlXMBH = ProjectHelper.GetXMBHCondition(getXMBH());
		List<BDCS_DJDY_GZ> djdys = getCommonDao().getDataList(BDCS_DJDY_GZ.class, strSqlXMBH);
		if(!zxtdql()){
			return false;
		}
		if (djdys == null || djdys.size() <= 0)
			return false;
		copyCFformDC(djdys);
		if(djdys.size()>20){
			return writeDJBEx();
		}
		for (int idjdy = 0; idjdy < djdys.size(); idjdy++) {
			BDCS_DJDY_GZ bdcs_djdy_gz = djdys.get(idjdy);
			String djdyid = bdcs_djdy_gz.getDJDYID();
			String bdcdyid = bdcs_djdy_gz.getBDCDYID();
			
//			BDCS_XMXX xmxx = getCommonDao().get(BDCS_XMXX.class, getXMBH());
//			String workflowcode = ProjectHelper.getWorkflowCodeByProjectID(xmxx.getPROJECT_ID());
//			String sql = " WORKFLOWCODE='" + workflowcode + "'";
//			CommonDao baseCommonDao = SuperSpringContext.getContext().getBean(CommonDao.class);
//			List<WFD_MAPPING> mappings = baseCommonDao.getDataList(WFD_MAPPING.class, sql);
//			if (mappings != null && mappings.size() > 0) {
//				WFD_MAPPING maping = mappings.get(0);
//				if (("1").equals(maping.getDELTDQL())){
//					zxtdql();
//				}
//			}
//			
			
			super.CopyGZQLToXZAndLS(djdyid);
			super.CopyGZQLRToXZAndLS(djdyid);
			super.CopyGZQDZRToXZAndLS(djdyid);
			super.CopyGZZSToXZAndLS(djdyid);
			super.CopyGZDYToXZAndLS(bdcs_djdy_gz.getBDCDYID());
			super.CopyGZDJDYToXZAndLS(bdcs_djdy_gz.getId());
			super.CopyGeo(bdcdyid, BDCDYLX.initFrom(bdcs_djdy_gz.getBDCDYLX()), DJDYLY.initFrom(bdcs_djdy_gz.getLY()));
			RebuildDYBG("", "", bdcs_djdy_gz.getBDCDYID(), bdcs_djdy_gz.getDJDYID(), bdcs_djdy_gz.getCreateTime(), bdcs_djdy_gz.getModifyTime());
			// 设定调查库中单元登记状态为已登记
			BDCDYLX dylx = super.getBdcdylx();
			RealUnit _srcunit = UnitTools.loadUnit(dylx, DJDYLY.DC, bdcs_djdy_gz.getBDCDYID());
			if (_srcunit != null) {
				_srcunit.setDJZT(DJZT.YDJ.Value);
				getCommonDao().save(_srcunit);
			}
			String iscopyycf2cf = "";
			String workflowcode = ProjectHelper.getWorkflowCodeByProjectID(getProject_id());
			List<WFD_MAPPING> listCode = getCommonDao().getDataList(WFD_MAPPING.class, "WORKFLOWCODE='" + workflowcode + "'");
			if ((listCode != null) && (listCode.size() > 0)) {
				iscopyycf2cf = ((WFD_MAPPING) listCode.get(0)).getISCOPYYCF2CF();
			}
			// 如果实测绘户关联预测绘户上有查封，则拷贝查封信息到实测户上
			if (BDCDYLX.H.equals(dylx)&&(("1").equals(ConfigHelper.getNameByValue("CopyLimitFromYCH")) 
					|| ("3").equals(ConfigHelper.getNameByValue("CopyLimitFromYCH")))||"1".equals(iscopyycf2cf)||"3".equals(iscopyycf2cf)) {
				List<YC_SC_H_XZ> gxs = getCommonDao().getDataList(YC_SC_H_XZ.class, "SCBDCDYID='" + bdcs_djdy_gz.getBDCDYID() + "'");
				if (gxs != null && gxs.size() > 0) {
					String ycbdcyid = gxs.get(0).getYCBDCDYID();
					if (!StringHelper.isEmpty(ycbdcyid)) {
						List<BDCS_DJDY_XZ> djdys_yc = getCommonDao().getDataList(BDCS_DJDY_XZ.class, "BDCDYID='" + ycbdcyid + "' AND BDCDYLX='032'");
						if (djdys_yc != null && djdys_yc.size() > 0) {
							String djdyid_yc = djdys_yc.get(0).getDJDYID();
							if (!StringHelper.isEmpty(djdyid_yc)) {
								List<BDCS_QL_XZ> qls_yc = getCommonDao().getDataList(BDCS_QL_XZ.class, "DJDYID='" + djdyid_yc + "' AND QLLX='99' AND DJLX='800'");
								if (qls_yc != null && qls_yc.size() > 0) {
									for (BDCS_QL_XZ ql_yc : qls_yc) {
										String cfqlid = SuperHelper.GeneratePrimaryKey();
										String cffsqlid = SuperHelper.GeneratePrimaryKey();
										BDCS_QL_XZ cfql_sc = new BDCS_QL_XZ();
										try {
											PropertyUtils.copyProperties(cfql_sc, ql_yc);
										} catch (Exception e) {
										}
										cfql_sc.setId(cfqlid);
										cfql_sc.setYWH(super.getProject_id());
										cfql_sc.setXMBH(getXMBH());
										cfql_sc.setDJDYID(bdcs_djdy_gz.getDJDYID());
										cfql_sc.setFSQLID(cffsqlid);
										cfql_sc.setDBR(Global.getCurrentUserName());
										cfql_sc.setDJSJ(new Date());
										cfql_sc.setDJJG(ConfigHelper.getNameByValue("DJJGMC"));
										cfql_sc.setLYQLID(ql_yc.getId());
										cfql_sc.setBDCDYH(_srcunit.getBDCDYH());
										getCommonDao().save(cfql_sc);
										BDCS_QL_LS cfql_sc_ls = new BDCS_QL_LS();
										try {
											PropertyUtils.copyProperties(cfql_sc_ls, cfql_sc);
										} catch (Exception e) {
										}
										getCommonDao().save(cfql_sc_ls);
										if (!StringHelper.isEmpty(ql_yc.getFSQLID())) {
											BDCS_FSQL_XZ fsql_yc = getCommonDao().get(BDCS_FSQL_XZ.class, ql_yc.getFSQLID());
											if (fsql_yc != null) {
												BDCS_FSQL_XZ cffsql_sc = new BDCS_FSQL_XZ();
												try {
													PropertyUtils.copyProperties(cffsql_sc, fsql_yc);
												} catch (Exception e) {
												}
												cffsql_sc.setQLID(cfqlid);
												cffsql_sc.setId(cffsqlid);
												cffsql_sc.setXMBH(getXMBH());
												cffsql_sc.setDJDYID(bdcs_djdy_gz.getDJDYID());
												if(fsql_yc.getCFLX().equals("3")){//预查封
													cffsql_sc.setCFLX(CFLX.CF.Value);
												}else if(fsql_yc.getCFLX().equals("4")){//轮候预查封
													cffsql_sc.setCFLX(CFLX.LHCF.Value);
												}
												getCommonDao().save(cffsql_sc);
												BDCS_FSQL_LS cffsql_sc_ls = new BDCS_FSQL_LS();
												try {
													PropertyUtils.copyProperties(cffsql_sc_ls, cffsql_sc);
												} catch (Exception e) {
												}
												getCommonDao().save(cffsql_sc_ls);
												//注销期房查封
												if(("3").equals(ConfigHelper.getNameByValue("CopyLimitFromYCH"))||"3".equals(iscopyycf2cf)){
													
													if (fsql_yc != null) {
														//标记注销状态
														BDCS_FSQL_LS fsql_ycls = getCommonDao().get(BDCS_FSQL_LS.class, ql_yc.getFSQLID());
														fsql_ycls.setZXDBR(Global.getCurrentUserName());														
														fsql_ycls.setZXSJ(new Date());
														fsql_ycls.setZXFJ("单元办理现房首次登记，期房查封转移到现房查封，并对期房查封进行了注销");
														getCommonDao().save(fsql_ycls);
														//删除期房的查封信息
														getCommonDao().deleteEntity(ql_yc);
														getCommonDao().deleteEntity(fsql_yc);
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}

		this.SetSFDB();
		getCommonDao().flush();
		super.alterCachedXMXX();
		return true;
	}
	
	public boolean writeDJBEx() {
		try {
			String code = ConfigHelper.getNameByValue("XZQHDM");
			if(code =="450200"){
				CopyQLEX();// 拷贝权利
				CopyFSQLEX();// 拷贝权利
				CopyQLREX();// 拷贝权利人
				CopyQDZREX();// 拷贝权地证人
				CopyZSEX();//拷贝证书
				CopyDJDYEX();// 拷贝登记单元
			}else{
				CopyQL();// 拷贝权利
				CopyFSQL();// 拷贝权利
				CopyQLR();// 拷贝权利人
				CopyQDZR();// 拷贝权地证人
				CopyZS();//拷贝证书
				CopyDJDY();// 拷贝登记单元
			}
			
			
		} catch (Exception e) {
			throw new RuntimeException(); 
		}

		this.SetSFDB();
		getCommonDao().flush();
		super.alterCachedXMXX();
		return true;
	}
	
	private boolean zxtdql(){
		boolean bCancel=true;
//		if(!BDCDYLX.h.equals()){
//			return true;
//		}
		//ProjectInfo info=ProjectHelper.GetPrjInfoByXMBH(getXMBH());
		String workflowcode = ProjectHelper.getWorkflowCodeByProjectID(getProject_id());
		WFD_MAPPING mapping=new WFD_MAPPING();
		List<WFD_MAPPING> list_mapping=super.getCommonDao().getDataList(WFD_MAPPING.class, "WORKFLOWCODE='"+workflowcode+"'");
		if(list_mapping!=null&&list_mapping.size()>0){
			mapping=list_mapping.get(0);
		}
		if("0".equals(StringHelper.formatObject(mapping.getDELTDQL()))){
			return true;
		}
		String dbr = Global.getCurrentUserName();
		String strSqlXMBH = ProjectHelper.GetXMBHCondition(getXMBH());
		List<BDCS_DJDY_GZ> djdys = getCommonDao().getDataList(BDCS_DJDY_GZ.class, strSqlXMBH);
		if(djdys!=null&&djdys.size()>0){
			for (int i = 0; i < djdys.size(); i++){
				BDCS_DJDY_GZ djdy = djdys.get(i);
				String qllxarray = " ('1','2','3','4','5','6','7','8','9','10','11','12','13','14','15','16','17','18')";
				String djdyid = djdy.getDJDYID();
				String sql = MessageFormat.format(" DJDYID=''{0}'' AND QLLX IN {1}", djdyid, qllxarray);
				 RealUnit H = UnitTools.loadUnit(BDCDYLX.H, DJDYLY.GZ, djdy.getBDCDYID());
				String zdbdcdyid=((House)H).getZDBDCDYID();
				List<BDCS_QL_XZ> zdql=getCommonDao().getDataList(BDCS_QL_XZ.class, "DJDYID IN (SELECT DJDYID FROM BDCS_DJDY_XZ WHERE BDCDYID ='"+zdbdcdyid+"')");
				if (zdql != null && zdql.size() > 0) {
					for (int j = 0; j < zdql.size(); j++) {
						BDCS_QL_XZ xzql = zdql.get(j);
						if (xzql != null) {
							super.removeQLXXFromXZByQLID(xzql.getId());
						}
					}
				}
						if(zdql!=null&&zdql.size()>0){
							for(Rights qls:zdql){
								String qlid=qls.getId();
								RightsTools.deleteRightsAll(DJDYLY.XZ, qlid);
								// 更新历史附属权利信息
								SubRights subright = RightsTools.loadSubRightsByRightsID(DJDYLY.LS, qlid);
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
	
	
	
	/**
	 * 批量拷贝权利
	 * 
	 * @Author 俞学斌
	 * @CreateTime 2016年09月05日 15:45:34
	 */
	protected boolean CopyQL() {
		String dbr = Global.getCurrentUserName();
		String xmbhFilter = ProjectHelper.GetXMBHCondition(super.getXMBH());
		List<BDCS_QL_GZ> qls = getCommonDao().getDataList(BDCS_QL_GZ.class, xmbhFilter);
		if (qls != null && qls.size() > 0) {
			for (int iql = 0; iql < qls.size(); iql++) {
				BDCS_QL_GZ bdcs_ql_gz = qls.get(iql);
				// 登记时间，登簿人，登记机构
				bdcs_ql_gz.setDBR(dbr);
				bdcs_ql_gz.setDJSJ(new Date());
				bdcs_ql_gz.setDJJG(ConfigHelper.getNameByValue("DJJGMC"));
				getCommonDao().getCurrentSession().update(bdcs_ql_gz);
				// 拷贝权利
				BDCS_QL_XZ bdcs_ql_xz = ObjectHelper.copyQL_GZToXZ(bdcs_ql_gz);
				getCommonDao().save(bdcs_ql_xz);
				BDCS_QL_LS bdcs_ql_ls = ObjectHelper.copyQL_XZToLS(bdcs_ql_xz);
				getCommonDao().save(bdcs_ql_ls);
			}
		}
		return true;
	}
	/**
	 * 批量拷贝附属权利
	 * 
	 * @Author 俞学斌
	 * @CreateTime 2016年09月05日 15:45:34
	 */
	protected boolean CopyFSQL() {
		String xmbhFilter = ProjectHelper.GetXMBHCondition(super.getXMBH());
		List<BDCS_FSQL_GZ> fsqls = getCommonDao().getDataList(BDCS_FSQL_GZ.class, xmbhFilter);
		if (fsqls != null && fsqls.size() > 0) {
			for (int ifsql = 0; ifsql < fsqls.size(); ifsql++) {
				BDCS_FSQL_GZ bdcs_fsql_gz = fsqls.get(ifsql);
				BDCS_FSQL_XZ bdcs_fsql_xz = ObjectHelper.copyFSQL_GZToXZ(bdcs_fsql_gz);
				getCommonDao().save(bdcs_fsql_xz);
				BDCS_FSQL_LS bdcs_fsql_ls = ObjectHelper.copyFSQL_XZToLS(bdcs_fsql_xz);
				getCommonDao().save(bdcs_fsql_ls);
			}
		}
		return true;
	}
	
	/**
	 * 批量拷贝权利人
	 * 
	 * @Author 俞学斌
	 * @CreateTime 2016年09月05日 15:45:34
	 */
	protected boolean CopyQLR() {
		String xmbhFilter = ProjectHelper.GetXMBHCondition(super.getXMBH());
		List<BDCS_QLR_GZ> qlrs = getCommonDao().getDataList(BDCS_QLR_GZ.class, xmbhFilter);
		if (qlrs != null && qlrs.size() > 0) {
			for (int iqlr = 0; iqlr < qlrs.size(); iqlr++) {
				BDCS_QLR_GZ bdcs_qlr_gz = qlrs.get(iqlr);
				BDCS_QLR_XZ bdcs_qlr_xz = ObjectHelper.copyQLR_GZToXZ(bdcs_qlr_gz);
				getCommonDao().save(bdcs_qlr_xz);
				BDCS_QLR_LS bdcs_qlr_ls = ObjectHelper.copyQLR_XZToLS(bdcs_qlr_xz);
				getCommonDao().save(bdcs_qlr_ls);
			}
		}
		return true;
	}
	
	/**
	 * 批量拷贝权地证人
	 * 
	 * @Author 俞学斌
	 * @CreateTime 2016年09月05日 15:45:34
	 */
	protected boolean CopyQDZR() {
		String xmbhFilter = ProjectHelper.GetXMBHCondition(super.getXMBH());
		List<BDCS_QDZR_GZ> qdzrs = getCommonDao().getDataList(BDCS_QDZR_GZ.class, xmbhFilter);
		if (qdzrs != null && qdzrs.size() > 0) {
			for (int iqdzr = 0; iqdzr < qdzrs.size(); iqdzr++) {
				BDCS_QDZR_GZ bdcs_qdzr_gz = qdzrs.get(iqdzr);
				BDCS_QDZR_XZ bdcs_qdzr_xz = ObjectHelper.copyQDZR_GZToXZ(bdcs_qdzr_gz);
				getCommonDao().save(bdcs_qdzr_xz);
				BDCS_QDZR_LS bdcs_qdzr_ls = ObjectHelper.copyQDZR_XZToLS(bdcs_qdzr_xz);
				getCommonDao().save(bdcs_qdzr_ls);
			}
		}
		return true;
	}
	
	/**
	 * 批量拷贝证书
	 * 
	 * @Author 俞学斌
	 * @CreateTime 2016年09月05日 15:45:34
	 */
	protected boolean CopyZS() {
		String xmbhFilter = ProjectHelper.GetXMBHCondition(super.getXMBH());
		List<BDCS_ZS_GZ> zss = getCommonDao().getDataList(BDCS_ZS_GZ.class, xmbhFilter);
		if (zss != null && zss.size() > 0) {
			for (int izs = 0; izs < zss.size(); izs++) {
				BDCS_ZS_GZ bdcs_zs_gz = zss.get(izs);
				BDCS_ZS_XZ bdcs_zs_xz = ObjectHelper.copyZS_GZToXZ(bdcs_zs_gz);
				getCommonDao().save(bdcs_zs_xz);
				BDCS_ZS_LS bdcs_zs_ls = ObjectHelper.copyZS_XZToLS(bdcs_zs_xz);
				getCommonDao().save(bdcs_zs_ls);
			}
		}
		return true;
	}
	
	/**
	 * 批量拷贝登记单元/单元/构建单元变更关系/拷贝图形/期房查封带入现房
	 * 
	 * @Author 俞学斌
	 * @CreateTime 2016年09月05日 15:45:34
	 */
	protected boolean CopyDJDY() {
		String xmbhFilter = ProjectHelper.GetXMBHCondition(super.getXMBH());
		List<BDCS_DJDY_GZ> djdys=getCommonDao().getDataList(BDCS_DJDY_GZ.class, xmbhFilter);
		if (djdys != null && djdys.size() > 0) {
			for (int idjdy = 0; idjdy < djdys.size(); idjdy++) {
				BDCS_DJDY_GZ bdcs_djdy_gz = djdys.get(idjdy);
				BDCS_DJDY_XZ bdcs_djdy_xz = ObjectHelper.copyDJDY_GZToXZ(bdcs_djdy_gz);
				getCommonDao().save(bdcs_djdy_xz);
				BDCS_DJDY_LS bdcs_djdy_ls = ObjectHelper.copyDJDY_XZToLS(bdcs_djdy_xz);
				getCommonDao().save(bdcs_djdy_ls);
				
				super.CopyGZDYToXZAndLS(bdcs_djdy_gz.getBDCDYID());
				
				BDCDYLX dylx = super.getBdcdylx();
				RealUnit _srcunit = UnitTools.loadUnit(dylx, DJDYLY.DC, bdcs_djdy_gz.getBDCDYID());
				if (_srcunit != null) {
					_srcunit.setDJZT(DJZT.YDJ.Value);
					getCommonDao().save(_srcunit);
				}
				RebuildDYBG("", "", bdcs_djdy_gz.getBDCDYID(), bdcs_djdy_gz.getDJDYID(), bdcs_djdy_gz.getCreateTime(), bdcs_djdy_gz.getModifyTime());
				super.CopyGeo(bdcs_djdy_gz.getBDCDYID(), BDCDYLX.initFrom(bdcs_djdy_gz.getBDCDYLX()), DJDYLY.initFrom(bdcs_djdy_gz.getLY()));
				
				// 如果实测绘户关联预测绘户上有查封，则拷贝查封信息到实测户上
				if (BDCDYLX.H.equals(dylx)&&(("1").equals(ConfigHelper.getNameByValue("CopyLimitFromYCH")) 
						|| ("3").equals(ConfigHelper.getNameByValue("CopyLimitFromYCH")))) {
					List<YC_SC_H_XZ> gxs = getCommonDao().getDataList(YC_SC_H_XZ.class, "SCBDCDYID='" + bdcs_djdy_gz.getBDCDYID() + "'");
					if (gxs != null && gxs.size() > 0) {
						String ycbdcyid = gxs.get(0).getYCBDCDYID();
						if (!StringHelper.isEmpty(ycbdcyid)) {
							List<BDCS_DJDY_XZ> djdys_yc = getCommonDao().getDataList(BDCS_DJDY_XZ.class, "BDCDYID='" + ycbdcyid + "' AND BDCDYLX='032'");
							if (djdys_yc != null && djdys_yc.size() > 0) {
								String djdyid_yc = djdys_yc.get(0).getDJDYID();
								if (!StringHelper.isEmpty(djdyid_yc)) {
									List<BDCS_QL_XZ> qls_yc = getCommonDao().getDataList(BDCS_QL_XZ.class, "DJDYID='" + djdyid_yc + "' AND QLLX='99' AND DJLX='800'");
									if (qls_yc != null && qls_yc.size() > 0) {
										for (BDCS_QL_XZ ql_yc : qls_yc) {
											String cfqlid = SuperHelper.GeneratePrimaryKey();
											String cffsqlid = SuperHelper.GeneratePrimaryKey();
											BDCS_QL_XZ cfql_sc = new BDCS_QL_XZ();
											try {
												PropertyUtils.copyProperties(cfql_sc, ql_yc);
											} catch (Exception e) {
											}
											cfql_sc.setId(cfqlid);
											cfql_sc.setYWH(super.getProject_id());
											cfql_sc.setXMBH(getXMBH());
											cfql_sc.setDJDYID(bdcs_djdy_gz.getDJDYID());
											cfql_sc.setFSQLID(cffsqlid);
											cfql_sc.setDBR(Global.getCurrentUserName());
											cfql_sc.setDJSJ(new Date());
											cfql_sc.setDJJG(ConfigHelper.getNameByValue("DJJGMC"));
											cfql_sc.setLYQLID(ql_yc.getId());
											getCommonDao().save(cfql_sc);
											BDCS_QL_LS cfql_sc_ls = new BDCS_QL_LS();
											try {
												PropertyUtils.copyProperties(cfql_sc_ls, cfql_sc);
											} catch (Exception e) {
											}
											getCommonDao().save(cfql_sc_ls);
											if (!StringHelper.isEmpty(ql_yc.getFSQLID())) {
												BDCS_FSQL_XZ fsql_yc = getCommonDao().get(BDCS_FSQL_XZ.class, ql_yc.getFSQLID());
												if (fsql_yc != null) {
													BDCS_FSQL_XZ cffsql_sc = new BDCS_FSQL_XZ();
													try {
														PropertyUtils.copyProperties(cffsql_sc, fsql_yc);
													} catch (Exception e) {
													}
													cffsql_sc.setQLID(cfqlid);
													cffsql_sc.setId(cffsqlid);
													cffsql_sc.setXMBH(getXMBH());
													cffsql_sc.setDJDYID(bdcs_djdy_gz.getDJDYID());
													getCommonDao().save(cffsql_sc);
													BDCS_FSQL_LS cffsql_sc_ls = new BDCS_FSQL_LS();
													try {
														PropertyUtils.copyProperties(cffsql_sc_ls, cffsql_sc);
													} catch (Exception e) {
													}
													getCommonDao().save(cffsql_sc_ls);
													//注销期房查封
													if(("3").equals(ConfigHelper.getNameByValue("CopyLimitFromYCH"))){
														
														if (fsql_yc != null) {
															//标记注销状态
															BDCS_FSQL_LS fsql_ycls = getCommonDao().get(BDCS_FSQL_LS.class, ql_yc.getFSQLID());
															fsql_ycls.setZXDBR(Global.getCurrentUserName());
															fsql_ycls.setZXSJ(new Date());
															fsql_ycls.setZXFJ("单元办理现房首次登记，期房查封转移到现房查封，并对期房查封进行了注销");
															getCommonDao().save(fsql_ycls);
															//删除期房的查封信息
															getCommonDao().deleteEntity(ql_yc);
															getCommonDao().deleteEntity(fsql_yc);
														}
													}
												}
											}
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
	

	/**
	 * 移除登记产单元
	 */
	@Override
	public boolean removeBDCDY(String bdcdyid) {
		// 找到登记单元表，移除记录
		CommonDao baseCommonDao = this.getCommonDao();
		BDCS_DJDY_GZ djdy = new BDCS_DJDY_GZ();

		djdy = super.removeDJDY(bdcdyid);
		if (djdy != null) {
			String djdyid = djdy.getDJDYID();
			String bdcdylx = djdy.getBDCDYLX();
			super.RemoveSQRByQLID(djdyid, getXMBH());
			// 删除具体单元
			this.removedy(bdcdylx, bdcdyid);

			// 删除权利、附属权利、权利人、证书、权地证人关系
			String _hqlCondition = MessageFormat.format("XMBH=''{0}'' AND DJDYID=''{1}''", getXMBH(), djdyid);
			RightsTools.deleteRightsAllByCondition(DJDYLY.GZ, _hqlCondition);

			//删除GZ层土地用途tdyt  luml  2018-5-15
			baseCommonDao.deleteEntitysByHql(BDCS_TDYT_GZ.class," BDCDYID='"+ bdcdyid +"'");
			
			// 更新调查库相应单元状态
			updateDCDYStatus(bdcdylx, bdcdyid);
			baseCommonDao.flush();
		}
		return true;
	}

	/**
	 * 获取不动产单元列表
	 */
	@Override
	public List<UnitTree> getUnitTree() {
		String sllx1 = super.getSllx1();
		QLLX qllx = super.getQllx();

		// 项目内多幢
		if ("03".equals(sllx1) && qllx.Value.equals(QLLX.GYJSYDSHYQ_FWSYQ.Value)) {
			String xmbh = super.getXMBH();
			String sql = ProjectHelper.GetXMBHCondition(xmbh);
			CommonDao dao = this.getCommonDao();
			List<UnitTree> list = new ArrayList<UnitTree>();
			List<BDCS_DJDY_GZ> djdys = dao.getDataList(BDCS_DJDY_GZ.class, sql);
			if (djdys != null && djdys.size() > 0) {
				UnitTree tree = null;
				List<UnitTree> children = new ArrayList<UnitTree>();
				for (int i = 0; i < djdys.size(); i++) {
					BDCS_DJDY_GZ djdy = djdys.get(i);
					if (null == tree || !djdy.getDJDYID().equals(tree.getId())) {
						tree = new UnitTree();
						children = new ArrayList<UnitTree>();
						tree.setId(djdy.getDJDYID());
						tree.setDjdyid(djdy.getDJDYID());
						tree.setText("登记单元" + (i + 1));
					}
					UnitTree childTree = new UnitTree();
					BDCS_QL_GZ ql = null;
					List<BDCS_QL_GZ> qllist = getCommonDao().getDataList(BDCS_QL_GZ.class, " DJDYID='" + djdy.getDJDYID() + "' AND " + ProjectHelper.GetXMBHCondition(xmbh));
					if (qllist.size() > 0) {
						ql = qllist.get(0);
					}
					if (ql != null) {
						childTree.setQlid(ql.getId());
						tree.setQlid(ql.getId());
					}
					childTree.setId(djdy.getBDCDYID());
					childTree.setBdcdyid(djdy.getBDCDYID());
					childTree.setBdcdylx(djdy.getBDCDYLX());
					childTree.setDjdyid(djdy.getDJDYID());
					childTree.setBdcqzh(ql.getBDCQZH());
					String ly = StringUtils.isEmpty(djdy.getLY()) ? "gz" : DJDYLY.initFrom(djdy.getLY()) == null ? "gz" : DJDYLY.initFrom(djdy.getLY()).Name;
					childTree.setLy(ly);
					String zl = "";
					BDCDYLX dylx = BDCDYLX.initFrom(djdy.getBDCDYLX());
					if (ly.equals(DJDYLY.GZ.Name)) {
						if (dylx.equals(BDCDYLX.ZRZ)) {
							BDCS_ZRZ_GZ zrz = dao.get(BDCS_ZRZ_GZ.class, djdy.getBDCDYID());
							childTree.setZdbdcdyid(zrz.getZDBDCDYID());
							zl = zrz == null ? "" : zrz.getZL();
						}
					}
					childTree.setText(zl);
					childTree.setZl(zl);
					children.add(childTree);
					tree.setChildren(children);
				}
				list.add(tree);
			}

			return list;
		} else {

			return super.getUnitList();
		}
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

	@Override
	public Map<String, String> exportXML(String path, String actinstID) {

		Marshaller mashaller;
		if("LD".equals(super.getBdcdylx().toString().toString())
				||"HY".equals(this.getBdcdylx().toString().toString())){
			//海域,林地
			return exportXMLother(path, actinstID,"YES");
		}
		Map<String, String> names = new HashMap<String, String>();
		try {
			CommonDao dao = this.getCommonDao();
			String xmbhHql = ProjectHelper.GetXMBHCondition(this.getXMBH());
			List<BDCS_DJDY_GZ> djdys = dao.getDataList(BDCS_DJDY_GZ.class, xmbhHql);
			
			if (djdys != null && djdys.size() > 0) {
				mashaller = JAXBContext.newInstance(Message.class).createMarshaller();
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
					ProjectServiceImpl serviceImpl = SuperSpringContext.getContext().getBean(ProjectServiceImpl.class);
					sqrs = serviceImpl.getSQRList(this.getXMBH());
					if (QLLX.GYJSYDSHYQ.Value.equals(this.getQllx().Value) || QLLX.ZJDSYQ.Value.equals(this.getQllx().Value) || QLLX.JTJSYDSYQ.Value.equals(this.getQllx().Value)) { // 国有建设使用权、宅基地使用权、集体建设用地使用权
						RealUnit unit=UnitTools.loadUnit(BDCDYLX.SHYQZD, DJDYLY.GZ, djdy.getBDCDYID());
						UseLand zd=(UseLand)unit;
						Message msg = exchangeFactory.createMessage(null);
						this.fillHead(msg, i, ywh,zd.getBDCDYH(),zd.getQXDM(),ql.getLYQLID());
						msg.getHead().setParcelID(StringHelper.formatObject(zd.getZDDM()));
						msg.getHead().setEstateNum(StringHelper.formatObject(zd.getBDCDYH()));
//						msg.getHead().setPreEstateNum(StringHelper.formatObject(zd.getBDCDYH()));
						if (zd != null && !StringUtils.isEmpty(zd.getQXDM())) {
							msg.getHead().setAreaCode(zd.getQXDM());
						}
						if (djdy != null) {
							this.fillShyqZdData(msg,ywh,zd,sqrs,qlrs,ql,xmxx,actinstID);//单独提出来个方法
						}
						
						msgName = getMessageFileName( msg,  i ,djdys.size(),null,null);
						mashaller.marshal(msg, new File(path + msgName));
						result = uploadFile(path + msgName, ConstValue.RECCODE.JSYDSHYQ_CSDJ.Value, actinstID, djdy.getDJDYID(), ql.getId());
						
					}
                    if ("NYD".equals(super.getBdcdylx().toString())) {
                    	try {
                        	//农用地
                        	RealUnit unit=UnitTools.loadUnit(BDCDYLX.NYD, DJDYLY.GZ, djdy.getBDCDYID());
                        	if(unit==null) {
                        		unit=UnitTools.loadUnit(BDCDYLX.NYD, DJDYLY.XZ, djdy.getBDCDYID());
                        	}
                        	AgriculturalLand nyd=(AgriculturalLand)unit;
    						Message msg = exchangeFactory.createMessageByNYD();//创建信息承载器
    						//设置表头
    						this.fillHead(msg, i, ywh,nyd.getBDCDYH(),nyd.getQXDM(),ql.getLYQLID());
    						msg.getHead().setParcelID(StringHelper.formatObject(nyd.getZDDM()));
    						msg.getHead().setEstateNum(StringHelper.formatObject(nyd.getBDCDYH()));
//    						msg.getHead().setPreEstateNum(StringHelper.formatObject(nyd.getBDCDYH()));
    						//权证号
    						String  qzhzmh="";
    						for(BDCS_QLR_GZ qlr:qlrs) {
    							if(StringHelper.isEmpty(qlr.getBDCQZH().toString())) {
    								qzhzmh=qzhzmh+qlr.getBDCQZH().toString()+",";
    							}
    						}
//    						msg.getHead().setPreEstateNum(StringHelper.formatObject(qzhzmh));
    						if (nyd != null && !StringUtils.isEmpty(nyd.getQXDM())) {
    							msg.getHead().setAreaCode(nyd.getQXDM());
    						}
    						//填充内容
//    						1.权力人表
							List<ZTTGYQLR> zttqlr = msg.getData().getGYQLR();
							zttqlr = packageXml.getZTTGYQLRs(zttqlr, qlrs, null, ql, null, null, null, null);
							if (nyd != null) {
								//维护区县代码和不动产单元号
								for(ZTTGYQLR qlr:zttqlr) {
									qlr.setQXDM(nyd.getQXDM());
									qlr.setBDCDYH(nyd.getBDCDYH());
								}
							}
							msg.getData().setGYQLR(zttqlr);
//							KTT_ZDJBXX宗地基本信息
							KTTZDJBXX jbxx = msg.getData().getKTTZDJBXX();
							jbxx = packageXml.getZDJBXXByNYD(nyd, jbxx,ql);
							msg.getData().setKTTZDJBXX(jbxx);
//							宗地变化情况
							KTFZDBHQK bhqk = msg.getData().getZDBHQK();
				    		bhqk = packageXml.getKTFZDBHQKByNYD(bhqk, nyd, ql, null, null);
				    		msg.getData().setZDBHQK(bhqk);
				    		if (DJLX.CSDJ.Value.equals(this.getDjlx().Value)||DJLX.BGDJ.Value.equals(this.getDjlx().Value)) { 
				    			// 首次登记.变更登记取空间属性
					    		List<ZDK103> zdk = msg.getData().getZDK103();//宗地空间属性
					    		zdk = packageXml.getZDK103ByNYD(zdk, nyd, null, null);
					    		msg.getData().setZDK103(zdk);
							}
//							农用地使用权（非林地）
    						QLNYDSYQ nydql=msg.getData().getQlnydsyq();
    						nydql= packageXml.getQLNYDSYQ(nydql,nyd,ql,ywh);
							msg.getData().setQlnydsyq(nydql);
							// SLSQ 受理申请
							DJTDJSLSQ sq = msg.getData().getDJSLSQ();
							sq = packageXml.getDJTDJSLSQByNYD(sq, nyd, ql, xmxx);
							msg.getData().setDJSLSQ(sq);
							// SJ 收件
							List<DJFDJSJ> sj = msg.getData().getDJSJ();
							sj = packageXml.getDJFDJSJ(nyd, ywh,actinstID);
							for(DJFDJSJ d:sj) {
								d.setYSDM("6002020400");
							}
							msg.getData().setDJSJ(sj);
							 // SF 收费
							List<DJFDJSF> sfList = msg.getData().getDJSF();
							sfList = packageXml.getDJSF(nyd,ywh,this.getXMBH());
							for(DJFDJSF d :sfList ) {
								d.setYSDM("6002020400");
								d.setQXDM(StringHelper.formatObject(nyd.getQXDM()));
							}
							msg.getData().setDJSF(sfList);
							 // SH 审核
							List<DJFDJSH> sh = msg.getData().getDJSH();
							sh = packageXml.getDJFDJSH(nyd, ywh, this.getXMBH(), actinstID);
							for(DJFDJSH d:sh) {
								d.setYSDM("6002020400");
							} 
							msg.getData().setDJSH(sh);
					        // SZ 缮证 
							List<DJFDJSZ> sz = packageXml.getDJFDJSZ(nyd, ywh, this.getXMBH());
					        // FZ 发证
							List<DJFDJFZ> fz = packageXml.getDJFDJFZ(nyd, ywh, this.getXMBH());
							// GD 归档
							List<DJFDJGD> gd = packageXml.getDJFDJGD( nyd ,ywh,this.getXMBH());
							// SQR 申请人
							List<DJFDJSQR> djsqrs = msg.getData().getDJSQR();
							djsqrs = packageXml.getDJSQRs(djsqrs, sqrs, nyd.getYSDM(), ywh, nyd.getBDCDYH());
							if(nyd!=null) {
								for(DJFDJGD d:gd) {
									d.setYSDM("6002020400");
//									d.setQXDM(StringHelper.formatObject(nyd.getQXDM()));
									d.setZL(StringHelper.formatObject(nyd.getZL()));
								}
								for(DJFDJFZ d:fz) {
									d.setYSDM("6002020400");
//									d.setQXDM(StringHelper.formatObject(nyd.getQXDM()));
								}
								for(DJFDJSQR d:djsqrs) {
									d.setYsdm("6002020400");
								}
								
								for(DJFDJSZ d:sz) {
									d.setYSDM("6002020400");
//									d.setQXDM(StringHelper.formatObject(nyd.getQXDM()));
								}
							}
							msg.getData().setDJSQR(djsqrs);
							msg.getData().setDJSZ(sz);
							msg.getData().setDJFZ(fz);
							msg.getData().setDJGD(gd);
							// FJ 非结    （结构化文档）
							FJF100 fj = msg.getData().getFJF100();
							fj = packageXml.getFJF(fj);
							msg.getData().setFJF100(fj);
    						

    						
							
							msgName = getMessageFileName( msg,  i ,djdys.size(),names,djdy);
							mashaller.marshal(msg, new File(path +msgName));
							result = uploadFile(path +msgName, getRecType(), actinstID, djdy.getDJDYID(), ql.getId());
//    						msgName = getMessageFileName( msg,  i ,djdys.size(),null,null);
//    						mashaller.marshal(msg, new File(path + msgName));
//    						result = uploadFile(path + msgName, getRecType(), actinstID, djdy.getDJDYID(), ql.getId());
    						
    					
						} catch (Exception e) {
							e.printStackTrace();
						}
                    }
                    
                    
					if (QLLX.GYJSYDSHYQ_FWSYQ.Value.equals(this.getQllx().Value) ||
							QLLX.JTJSYDSYQ_FWSYQ.Value.equals(this.getQllx().Value)||
							QLLX.ZJDSYQ_FWSYQ.Value.equals(this.getQllx().Value)) { // 房屋所有权
						try {
							BDCS_H_GZ h = dao.get(BDCS_H_GZ.class, djdy.getBDCDYID());
							BDCS_ZRZ_GZ zrz_gz = null;
							if (h != null && !StringUtils.isEmpty(h.getZRZBDCDYID())) {
								zrz_gz = dao.get(BDCS_ZRZ_GZ.class, h.getZRZBDCDYID());
								if (zrz_gz != null) {
									zrz_gz.setGHYT(h.getGHYT()); // 自然幢的ghyt取户的ghyt
									zrz_gz.setFWJG(zrz_gz.getFWJG() == null || zrz_gz.getFWJG().equals("") ? h.getFWJG() : zrz_gz.getFWJG());
								}
							}
							BDCS_LJZ_GZ ljz_gz = null;
							if (h != null && !StringUtils.isEmpty(h.getLJZID())) {
								ljz_gz = dao.get(BDCS_LJZ_GZ.class, h.getLJZID());
							}
							if (h != null) {
								BDCS_SHYQZD_GZ zd = dao.get(BDCS_SHYQZD_GZ.class, h.getZDBDCDYID());
								if (zd != null) {
									h.setZDDM(zd.getZDDM());
								} else {
									BDCS_SHYQZD_XZ zd2 = dao.get(BDCS_SHYQZD_XZ.class, h.getZDBDCDYID());
									h.setZDDM(zd2.getZDDM());
								}
							}
							Message msg = exchangeFactory.createMessageByFWSYQ();
							this.fillHead(msg, i, ywh,h.getBDCDYH(),h.getQXDM(),ql.getLYQLID());
							msg.getHead().setParcelID(h.getZDDM());
							msg.getHead().setEstateNum(StringHelper.formatObject(h.getBDCDYH()));
//							msg.getHead().setPreEstateNum(StringHelper.formatObject(h.getBDCDYH()));
							if (h != null && !StringUtils.isEmpty(h.getQXDM())) {
								msg.getHead().setAreaCode(h.getQXDM());
							}
							BDCS_C_GZ c = null;
							if (h != null && !StringUtils.isEmpty(h.getCID())) {
								c = dao.get(BDCS_C_GZ.class, h.getCID());
							}
							if (djdy != null) {
								this.fillFwData(msg, ywh ,ljz_gz, c ,zrz_gz, h ,sqrs,qlrs,ql,xmxx,actinstID);
							}
						
							msgName = getMessageFileName( msg,  i ,djdys.size(),names,djdy);							
							mashaller.marshal(msg, new File(path + msgName));
							result = uploadFile(path + msgName, ConstValue.RECCODE.FDCQDZ_CSDJ.Value, actinstID, djdy.getDJDYID(), ql.getId());
							
						} catch (Exception e) {
							e.printStackTrace();
						}
					}

					if (QLLX.JTTDSYQ.Value.equals(this.getQllx().Value)) { // 集体土地所有权
						try {
							BDCS_SYQZD_GZ oland = dao.get(BDCS_SYQZD_GZ.class, djdy.getBDCDYID());
							Message msg = exchangeFactory.createMessageByTDSYQ();
							this.fillHead(msg, i, ywh,oland.getBDCDYH(),oland.getQXDM(),ql.getLYQLID());
							msg.getHead().setParcelID(StringHelper.formatObject(oland.getZDDM()));
							msg.getHead().setEstateNum(StringHelper.formatObject(oland.getBDCDYH()));
//							msg.getHead().setPreEstateNum(StringHelper.formatObject(oland.getBDCDYH()));
							if (oland != null && !StringUtils.isEmpty(oland.getQXDM())) {
								msg.getHead().setAreaCode(oland.getQXDM());
							}
							if (djdy != null) {
								 this.fillSyqZdData( msg, ywh, oland, sqrs, qlrs, ql, xmxx, actinstID) ;
							}
							
							msgName = getMessageFileName( msg,  i ,djdys.size(),names,djdy);
							mashaller.marshal(msg, new File(path + msgName));
							result = uploadFile(path + msgName, ConstValue.RECCODE.TDSYQ_CSDJ.Value, actinstID, djdy.getDJDYID(), ql.getId());
							
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					if (result.equals("")|| result==null) {
						Map<String, String> xmlError = new HashMap<String, String>();
						xmlError.put("error", "连接SFTP失败,请检查服务器和前置机的连接是否正常");
						if (QLLX.JTTDSYQ.Value.equals(this.getQllx().Value)) {
							YwLogUtil.addSjsbResult(msgName, "", "连接SFTP失败,请检查服务器和前置机的连接是否正常", ConstValue.SF.NO.Value, ConstValue.RECCODE.TDSYQ_CSDJ.Value,
									ProjectHelper.getpRroinstIDByActinstID(actinstID));
						} else if (QLLX.GYJSYDSHYQ_FWSYQ.Value.equals(this.getQllx().Value)) {
							YwLogUtil.addSjsbResult(msgName, "", "连接SFTP失败,请检查服务器和前置机的连接是否正常", ConstValue.SF.NO.Value, ConstValue.RECCODE.FDCQDZ_CSDJ.Value,
									ProjectHelper.getpRroinstIDByActinstID(actinstID));
						} else {
							YwLogUtil.addSjsbResult(msgName, "", "连接SFTP失败,请检查服务器和前置机的连接是否正常", ConstValue.SF.NO.Value, ConstValue.RECCODE.JSYDSHYQ_CSDJ.Value,
									ProjectHelper.getpRroinstIDByActinstID(actinstID));
						}
						return xmlError;
					}
					if (!"1".equals(result) && result.indexOf("success") == -1) { // xml本地校验不通过时
						Map<String, String> xmlError = new HashMap<String, String>();
						xmlError.put("error", result);
						return xmlError;
					}
					if (!StringUtils.isEmpty(result) && result.indexOf("success") > -1 && !names.containsKey("reccode")) {
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
			boolean isZipFile = false;
			if(djdys.size()>=10){
				isZipFile=true;
				String folderPath=GetProperties.getConstValueByKey("xmlPath") + "\\" + xmxx.getPROJECT_ID()+"_"+bljc;
				super.getShareMsgTools().deleteFolder(folderPath);
			}
			for (int idjdy = 0; idjdy < djdys.size(); idjdy++) {
				BDCS_DJDY_GZ djdy = djdys.get(idjdy);
				ConstValue.BDCDYLX dylx = ConstValue.BDCDYLX.initFrom(djdy.getBDCDYLX());
				ConstValue.DJDYLY dyly = ConstValue.DJDYLY.initFrom(djdy.getLY());
				RealUnit bdcdy = UnitTools.loadUnit(dylx, dyly, djdy.getBDCDYID());
				Rights bdcql = RightsTools.loadRightsByDJDYID(ConstValue.DJDYLY.GZ, getXMBH(), djdy.getDJDYID());
				SubRights bdcfsql = RightsTools.loadSubRightsByRightsID(ConstValue.DJDYLY.GZ, bdcql.getId());
				List<RightsHolder> bdcqlrs = RightsHolderTools.loadRightsHolders(ConstValue.DJDYLY.GZ, djdy.getDJDYID(), getXMBH());
				MessageExport msg = super.getShareMsgTools().GetMsg(bdcdy, bdcql, bdcfsql, bdcqlrs, bljc, xmxx);
				if(isZipFile){
					String folderPath = super.getShareMsgTools().createXMLInFile(xmxx, msg, idjdy+1,bljc);
					if(idjdy == djdys.size()-1){//文件都生成到文件夹以后再压缩上传
						super.getShareMsgTools().SendMsg(folderPath, xmxx, bljc, djdy.getBDCDYLX());
					}
				}
				else{
					super.getShareMsgTools().SendMsg(msg, idjdy + 1, djdy.getBDCDYLX(), xmxx);				
				}
			}
		}
	}

	/************************ 内部方法 *********************************/

	/**
	 * 移除具体单元
	 * 
	 * @作者 刘树峰
	 * @创建时间 2015年6月12日下午9:39:49
	 * @param bdcdylx
	 * @param bdcdyid
	 */
	private void removedy(String bdcdylx, String bdcdyid) {
		BDCDYLX dylx = BDCDYLX.initFrom(bdcdylx);
		RealUnit unit=UnitTools.deleteUnit(dylx, DJDYLY.GZ, bdcdyid);
		if(BDCDYLX.H.equals(dylx)&&unit!=null){
			House h_unit=(House)unit;
			if(h_unit!=null){
				String zrzbdcdyid=h_unit.getZRZBDCDYID();
				if(!StringHelper.isEmpty(zrzbdcdyid)){
					RealUnit unit_zrz=UnitTools.loadUnit(BDCDYLX.ZRZ, DJDYLY.GZ, zrzbdcdyid);
					if(unit_zrz!=null){
						List<RealUnit> list_unit=UnitTools.loadUnits(dylx, DJDYLY.GZ, "ZRZBDCDYID='"+zrzbdcdyid+"'");
						if(list_unit==null||list_unit.size()<=0){
							super.getCommonDao().deleteEntity(unit_zrz);
						}
					}
				}
			}
		}
	}

	/**
	 * 更新调查库单元的登记状态
	 * 
	 * @作者 刘树峰
	 * @创建时间 2015年6月13日上午4:13:34
	 * @param bdcdylx
	 * @param bdcdyid
	 */
	private void updateDCDYStatus(String bdcdylx, String bdcdyid) {
		BDCDYLX dylx = BDCDYLX.initFrom(bdcdylx);
		RealUnit unit = UnitTools.loadUnit(dylx, DJDYLY.DC, bdcdyid);
		if (unit != null) {
			unit.setDJZT(DJZT.WDJ.Value);
			getCommonDao().update(unit);
		}
	}

	private boolean copyCFformDC(List<BDCS_DJDY_GZ> djdys) {
		//宗地预查封处理
		String iszd = "01,02,05,09";
		if(iszd.indexOf(super.getBdcdylx().Value) != -1){
			//单元类型为宗地
			for (BDCS_DJDY_GZ bdcs_DJDY_GZ : djdys) {
				//单元与权利的关系为：单元层中BDCDYID与权利层中的DJDYID一样
				//调查库数据统一再工作层
				String djdyid = bdcs_DJDY_GZ.getBDCDYID();
				List<Rights> dcql = RightsTools.loadRightsByCondition(DJDYLY.DC, "DJDYID ='" + djdyid + "' AND DJLX = '800' AND QLLX = '99'");
				//如果调查库的宗地上存在查封权利，则将查封权利继承到登簿后现状、历史权利中
				if(dcql!=null&&dcql.size()>0){
					//继承查封权利时，要将权利、附属权利、权地证人中的DJDYID重置，
					for (Rights dcs_QL_GZ : dcql) {
						Rights srcRights = new BDCS_QL_GZ();
						boolean bSuccess = ObjectHelper.copyObject(dcs_QL_GZ, srcRights);
						SubRights sub = RightsTools.loadSubRights(DJDYLY.DC, srcRights.getFSQLID());
						SubRights srcSubRights = new BDCS_FSQL_GZ();
						boolean bSuccess_ = ObjectHelper.copyObject(sub, srcSubRights);
						if(bSuccess&&bSuccess_){
							//重置为初始登记受理时生成的DJDYID
							srcRights.setDJDYID(bdcs_DJDY_GZ.getDJDYID());
							BDCS_QL_XZ zxql = ObjectHelper.copyQL_GZToXZ((BDCS_QL_GZ)srcRights);
							getCommonDao().save(zxql);
							BDCS_QL_LS lsql = ObjectHelper.copyQL_XZToLS(zxql);
							getCommonDao().save(lsql);
							BDCS_FSQL_XZ xzsub = ObjectHelper.copyFSQL_GZToXZ((BDCS_FSQL_GZ) srcSubRights);
							getCommonDao().save(xzsub);
							BDCS_FSQL_LS lssub = ObjectHelper.copyFSQL_XZToLS(xzsub);
							getCommonDao().save(lssub);
							
							this.SetXMCFZT(djdyid, "01");
						}
					}
				}
			}
			return true;
		}
		return false;
	}
	
	protected boolean CopyQLEX() throws Exception {
		String dbr = Global.getCurrentUserName();
		String xmbh2 = super.getXMBH();
		String djjdmc = ConfigHelper.getNameByValue("DJJGMC");
		SimpleDateFormat format = new SimpleDateFormat("yyyy:MM:dd:HH:mm:ss");
		String date = format.format(new Date());
		//获取权利工作层数据
		StringBuilder ql_gz_sql = new StringBuilder("UPDATE BDCK.BDCS_QL_GZ QL SET QL.DBR='"+dbr+"' ,QL.DJSJ=TO_DATE('" + date + "','YYYY-MM-DD HH24:MI:SS'),QL.DJJG='"+djjdmc+"' WHERE QL.XMBH='"+xmbh2+"'");
		 getCommonDao().excuteQueryNoResult(ql_gz_sql.toString());
		//创建现状层权利数据
		StringBuilder ql_xz_sql = new StringBuilder("INSERT INTO BDCK.BDCS_QL_XZ  (QLID, DJDYID, BDCDYH, XMBH, FSQLID, YWH, QLLX,");
				ql_xz_sql.append(" DJLX, DJYY, QLQSSJ, QLJSSJ, BDCQZH, QXDM, DJJG, DBR, DJSJ, FJ, QSZT, QLLXMC, ZSEWM, ZSBH, ZT, CZFS, ZSBS, YXBZ, ");
				ql_xz_sql.append(" DCXMID, CreateTime,ModifyTime, QDJG, LYQLID, DJZT, PACTNO, TDSHYQR, TDZH, TDZHXH, CASENUM, ");
				ql_xz_sql.append(" ISCANCEL, BDCQZHXH, BZ, ISPARTIAL, ARCHIVES_CLASSNO, ARCHIVES_BOOKNO, XMDJLX, HTH, MSR, TDZSRELATIONID, TDCASENUM, ");
				ql_xz_sql.append(" MAINQLID, QS, BDCDYID, GROUPID, GYDBDCDYID, GYDBDCDYLX, SHOWDYZDMJORGYZDMJ,  ZSJG, YJSE, SPHM, ZXARCHIVES_CLASSNO, ");
				ql_xz_sql.append(" ZXARCHIVES_BOOKNO, GYRQK, DYQX, LUZHOU_HTH, JGH, ISYCTOSC  )");
				ql_xz_sql.append(" SELECT QLID,DJDYID, BDCDYH, XMBH, FSQLID, YWH, QLLX, DJLX, DJYY, QLQSSJ, QLJSSJ, BDCQZH, QXDM, DJJG, DBR, DJSJ, FJ, QSZT, ");
				ql_xz_sql.append(" QLLXMC, ZSEWM, ZSBH, ZT, CZFS, ZSBS, YXBZ, DCXMID, CreateTime,ModifyTime, QDJG, LYQLID, DJZT, PACTNO, TDSHYQR, ");
				ql_xz_sql.append(" TDZH, TDZHXH, CASENUM, ISCANCEL, BDCQZHXH, BZ, ISPARTIAL, ARCHIVES_CLASSNO, ARCHIVES_BOOKNO, XMDJLX, HTH, MSR, TDZSRELATIONID, TDCASENUM, ");
				ql_xz_sql.append(" MAINQLID, QS, BDCDYID, GROUPID, GYDBDCDYID, GYDBDCDYLX, SHOWDYZDMJORGYZDMJ,   ZSJG, YJSE, SPHM, ZXARCHIVES_CLASSNO, ZXARCHIVES_BOOKNO,");
				ql_xz_sql.append(" GYRQK, DYQX, LUZHOU_HTH, JGH, ISYCTOSC   FROM BDCK.BDCS_QL_GZ WHERE XMBH='"+xmbh2+"'");
		getCommonDao().excuteQueryNoResult(ql_xz_sql.toString());
		//历史层
		StringBuilder ql_ls_sql = new StringBuilder("INSERT INTO BDCK.BDCS_QL_LS  (QLID, DJDYID, BDCDYH, XMBH, FSQLID, YWH, QLLX,");
				ql_ls_sql.append(" DJLX, DJYY, QLQSSJ, QLJSSJ, BDCQZH, QXDM, DJJG, DBR, DJSJ, FJ, QSZT, QLLXMC, ZSEWM, ZSBH, ZT, CZFS, ZSBS, YXBZ, ");
				ql_ls_sql.append(" DCXMID, CreateTime, ModifyTime, QDJG, LYQLID, DJZT, PACTNO, TDSHYQR, TDZH, TDZHXH, CASENUM, ");
				ql_ls_sql.append(" ISCANCEL, BDCQZHXH, BZ, ISPARTIAL, ARCHIVES_CLASSNO, ARCHIVES_BOOKNO, XMDJLX, HTH, MSR, TDZSRELATIONID, TDCASENUM, ");
				ql_ls_sql.append(" MAINQLID, QS, BDCDYID, GROUPID, GYDBDCDYID, GYDBDCDYLX, SHOWDYZDMJORGYZDMJ,  ZSJG, YJSE, SPHM, ZXARCHIVES_CLASSNO, ");
				ql_ls_sql.append(" ZXARCHIVES_BOOKNO, GYRQK, DYQX, LUZHOU_HTH, JGH, ISYCTOSC  ) ");
				ql_ls_sql.append(" SELECT QLID,DJDYID, BDCDYH, XMBH, FSQLID, YWH, QLLX, DJLX, DJYY, QLQSSJ, QLJSSJ, BDCQZH, QXDM, DJJG, DBR, DJSJ, FJ, QSZT, ");
				ql_ls_sql.append(" QLLXMC, ZSEWM, ZSBH, ZT, CZFS, ZSBS, YXBZ, DCXMID, CreateTime, ModifyTime,  QDJG, LYQLID, DJZT, PACTNO, TDSHYQR, ");
				ql_ls_sql.append(" TDZH, TDZHXH, CASENUM, ISCANCEL, BDCQZHXH, BZ, ISPARTIAL, ARCHIVES_CLASSNO, ARCHIVES_BOOKNO, XMDJLX, HTH, MSR, TDZSRELATIONID, TDCASENUM, ");
				ql_ls_sql.append(" MAINQLID, QS, BDCDYID, GROUPID, GYDBDCDYID, GYDBDCDYLX, SHOWDYZDMJORGYZDMJ,   ZSJG, YJSE, SPHM, ZXARCHIVES_CLASSNO, ZXARCHIVES_BOOKNO,");
				ql_ls_sql.append(" GYRQK, DYQX, LUZHOU_HTH, JGH, ISYCTOSC   FROM BDCK.BDCS_QL_GZ WHERE XMBH='"+xmbh2+"'");
		getCommonDao().excuteQueryNoResult(ql_ls_sql.toString());
		return true;
	}
	protected boolean CopyQLREX() throws Exception {
		String xmbh2 = super.getXMBH();
		//拷贝权利人现状层
		StringBuilder qlr_xz_sql = new StringBuilder("INSERT INTO BDCK.BDCS_QLR_XZ (QLRID, XMBH, QLID, SQRID, SXH, QLRMC, BDCQZH, ZJZL, ZJH, FZJG, SSHY, GJ, HJSZSS, XB, DH, DZ, YB, GZDW,");
		  qlr_xz_sql.append(" DZYJ, QLRLX, QLMJ, QLBL, GYFS, GYQK, FDDBR, FDDBRZJLX, FDDBRZJHM, FDDBRDH, DLRXM, DLJGMC, DLRZJLX, DLRZJHM, DLRLXDH, YXBZ, BZ, DCXMID, ");
		  qlr_xz_sql.append(" CREATETIME, MODIFYTIME, ISCZR, BDCQZHXH, ZSBH, SFZJXM, NATION, DLRNATION,   BZ1)");
		  qlr_xz_sql.append(" SELECT QLRID, XMBH, QLID, SQRID, SXH, QLRMC, BDCQZH, ZJZL, ZJH, FZJG, SSHY, GJ, HJSZSS, XB, DH, DZ, YB, GZDW,");
		  qlr_xz_sql.append(" DZYJ, QLRLX, QLMJ, QLBL, GYFS, GYQK, FDDBR, FDDBRZJLX, FDDBRZJHM, FDDBRDH, DLRXM, DLJGMC, DLRZJLX, DLRZJHM, DLRLXDH, YXBZ, BZ, DCXMID, ");
		  qlr_xz_sql.append(" CREATETIME, MODIFYTIME, ISCZR, BDCQZHXH, ZSBH, SFZJXM, NATION, DLRNATION,   BZ1 FROM BDCK.BDCS_QLR_GZ WHERE XMBH='"+xmbh2+"'");
		getCommonDao().excuteQueryNoResult(qlr_xz_sql.toString());
		//拷贝权利人历史层
		StringBuilder qlr_ls_sql = new StringBuilder("INSERT INTO BDCK.BDCS_QLR_LS (QLRID, XMBH, QLID, SQRID, SXH, QLRMC, BDCQZH, ZJZL, ZJH, FZJG, SSHY, GJ, HJSZSS, XB, DH, DZ, YB, GZDW,");
		  qlr_ls_sql.append(" DZYJ, QLRLX, QLMJ, QLBL, GYFS, GYQK, FDDBR, FDDBRZJLX, FDDBRZJHM, FDDBRDH, DLRXM, DLJGMC, DLRZJLX, DLRZJHM, DLRLXDH, YXBZ, BZ, DCXMID, ");
		  qlr_ls_sql.append(" CREATETIME, MODIFYTIME, ISCZR, BDCQZHXH, ZSBH, SFZJXM, NATION, DLRNATION,   BZ1)");
		  qlr_ls_sql.append(" SELECT QLRID, XMBH, QLID, SQRID, SXH, QLRMC, BDCQZH, ZJZL, ZJH, FZJG, SSHY, GJ, HJSZSS, XB, DH, DZ, YB, GZDW,");
		  qlr_ls_sql.append(" DZYJ, QLRLX, QLMJ, QLBL, GYFS, GYQK, FDDBR, FDDBRZJLX, FDDBRZJHM, FDDBRDH, DLRXM, DLJGMC, DLRZJLX, DLRZJHM, DLRLXDH, YXBZ, BZ, DCXMID, ");
		  qlr_ls_sql.append(" CREATETIME, MODIFYTIME, ISCZR, BDCQZHXH, ZSBH, SFZJXM, NATION, DLRNATION,   BZ1 FROM BDCK.BDCS_QLR_GZ WHERE XMBH='"+xmbh2+"'");
		getCommonDao().excuteQueryNoResult(qlr_ls_sql.toString());
		return true;
	}
	/**
	 * 批量拷贝登记单元/单元/构建单元变更关系/拷贝图形/期房查封带入现房
	 * @throws SQLException 
	 * 
	 */
	protected boolean CopyDJDYEX() throws SQLException {

		String xmbh2 = super.getXMBH();
		//拷贝数据到现状层
		StringBuilder djdy_xz_sql = new StringBuilder("INSERT INTO BDCK.BDCS_DJDY_XZ (ID, DJDYID, XMBH, BDCDYH, BDCDYLX, BDCDYID, DCXMID, LY, CREATETIME, MODIFYTIME, GROUPID)");
			  djdy_xz_sql.append(" SELECT ID,DJDYID, XMBH, BDCDYH, BDCDYLX, BDCDYID, DCXMID, LY, CREATETIME, MODIFYTIME, GROUPID");
			  djdy_xz_sql.append(" FROM BDCK.BDCS_DJDY_GZ WHERE XMBH='"+xmbh2+"'");
		getCommonDao().excuteQueryNoResult(djdy_xz_sql.toString());
		//拷贝数据到历史层
		StringBuilder djdy_ls_sql = new StringBuilder("INSERT INTO BDCK.BDCS_DJDY_LS (ID, DJDYID, XMBH, BDCDYH, BDCDYLX, BDCDYID, DCXMID, LY, CREATETIME, MODIFYTIME, GROUPID)");
			  djdy_ls_sql.append(" SELECT ID,DJDYID, XMBH, BDCDYH, BDCDYLX, BDCDYID, DCXMID, LY, CREATETIME, MODIFYTIME, GROUPID");
			  djdy_ls_sql.append(" FROM BDCK.BDCS_DJDY_GZ WHERE XMBH='"+xmbh2+"'");
		getCommonDao().excuteQueryNoResult(djdy_ls_sql.toString());
		
		
		BDCDYLX dylx = super.getBdcdylx();
		boolean dyCopy_flag=false;
		//拷贝不动产单元
		if(dylx.equals(BDCDYLX.H)) {
			CopyGZDYToXZAndLSEX(xmbh2);
			dyCopy_flag=true;
		}
		
		
		@SuppressWarnings("rawtypes")
		List<Map>  djdy_gz_list= getCommonDao().getDataListByFullSql("SELECT BDCDYID,BDCDYLX,DJDYID,CREATETIME,MODIFYTIME,LY FROM BDCK.BDCS_DJDY_GZ WHERE XMBH='"+xmbh2+"'");
		for ( Map map : djdy_gz_list) {
			String bdcdyid = (String) map.get("BDCDYID");
			String bdcdylx = (String) map.get("BDCDYLX");
			String djdyid = (String) map.get("DJDYID");
			String ly = (String) map.get("LY");
			Date createtime =  (Date) map.get("CREATETIME");
			Date modiefytime =  (Date) map.get("MODIFYTIME");
			
			if(!dyCopy_flag) {
				super.CopyGZDYToXZAndLS(bdcdyid);
			}
			
			//2.构建变更单元
			StringBuilder dybg_sql = new StringBuilder("insert into bdck.bdcs_dybg (ID, LDJDYID, LBDCDYID, XDJDYID, XBDCDYID, XMBH, CREATETIME, MODIFYTIME ) values ( SYS_GUID(), NULL, NULL, '"+djdyid+"', '"+bdcdyid+"', '"+xmbh2+"', "+createtime+", "+modiefytime+")");
			getCommonDao().excuteQueryNoResult(dybg_sql.toString());
			//3.拷贝图形信息
			super.CopyGeo(bdcdyid, BDCDYLX.initFrom(bdcdylx), DJDYLY.initFrom(ly));
		
			//4. 如果实测绘户关联预测绘户上有查封，则拷贝查封信息到实测户上
			if (BDCDYLX.H.equals(dylx)&&(("1").equals(ConfigHelper.getNameByValue("CopyLimitFromYCH")) 
				|| ("3").equals(ConfigHelper.getNameByValue("CopyLimitFromYCH")))) {
				CopyLimitFromYCH(bdcdyid,djdyid);
			}
		}
		//5.更新调查库中地址状态
		StringBuilder bdcdck_h_gz_sql = new StringBuilder("update  bdcdck.bdcs_h_gz h set djzt='03' where exists  (SELECT 1 FROM BDCK.BDCS_DJDY_GZ DJDY WHERE H.BDCDYID=DJDY.BDCDYID AND DJDY.XMBH='"+xmbh2+"')");
		getCommonDao().excuteQueryNoResult(bdcdck_h_gz_sql.toString());
		
	
		return true;
	
	}
	/**
	 * 拷贝不动产单元信息
	 * @param bdcdyid
	 * @param dylx 
	 * @throws SQLException 
	 */
	private void CopyGZDYToXZAndLSEX(String xmbh2) throws SQLException {
		StringBuilder h_xz_sql = new StringBuilder(" insert into BDCK.BDCS_H_XZ (BDCDYID, YSDM, BDCDYH, XMBH, FWBM, ZRZBDCDYID, ZDDM, ZDBDCDYID, ZRZH, LJZID, LJZH, CID, CH, ZL, MJDW, ZCS, HH, ");
		       h_xz_sql.append(" SHBW, HX, HXJG, FWYT1, FWYT2, FWYT3, YCJZMJ, YCTNJZMJ, YCFTJZMJ, YCDXBFJZMJ, YCQTJZMJ, YCFTXS, SCJZMJ, SCTNJZMJ, SCFTJZMJ, SCDXBFJZMJ, ");
		       h_xz_sql.append(" SCQTJZMJ, SCFTXS, GYTDMJ, FTTDMJ, DYTDMJ, TDSYQR, FDCJYJG, GHYT, FWJG, JGSJ, FWLX, FWXZ, ZDMJ, SYMJ, CQLY, QTGSD, QTGSX, QTGSN, QTGSB, ");
		       h_xz_sql.append(" FCFHT, ZT, QXDM, QXMC, DJQDM, DJQMC, DJZQDM, DJZQMC, YXBZ, CQZT, DYZT, XZZT, BLZT, YYZT, DCXMID, FH, DJZT, BGZT, CREATETIME, MODIFYTIME, ");
		       h_xz_sql.append(" FCFHTWJ, YFWXZ, YFWYT, YGHYT, YFWJG, YZL, XMZL, PACTNO, XMMC, FWCB, GZWLX, YFWCB, RELATIONID, SFLJQPJYC, QLXZ, FWXZMC, FWYTMC, MJDWMC, ");
		       h_xz_sql.append(" TDSYQQSRQ, TDSYQZZRQ, TDSYNX, FCFHTSLTX, FWTDYT, CRJJE, QSC, ZZC, SZC, DYH, BZ, NBDCDYH, FCFHTWJLX, YZRZBDCDYID, NZDBDCDYID, XZB, YZB,");
		       h_xz_sql.append(" MARKERZT, MARKERZTMC, MARKERSM, MARKERTIME, SEARCHSTATE, SFSZZ, HJSON, HTH, MSR, CREATOR, MODIFYER, ISJG,   SFCT, CTXMBH, FWJG1,");
		       h_xz_sql.append(" FWJG2, FWJG3, GJZLX, ISPACKAGED, PACKAGESTAFF, PACKAGETIME, GLQDZR) ");
			   h_xz_sql.append(" select BDCDYID, YSDM, BDCDYH, XMBH, FWBM, ZRZBDCDYID, ZDDM, ZDBDCDYID, ZRZH, LJZID, LJZH, CID, CH, ZL, MJDW, ZCS, HH, ");
			   h_xz_sql.append(" SHBW, HX, HXJG, FWYT1, FWYT2, FWYT3, YCJZMJ, YCTNJZMJ, YCFTJZMJ, YCDXBFJZMJ, YCQTJZMJ, YCFTXS, SCJZMJ, SCTNJZMJ, SCFTJZMJ, SCDXBFJZMJ, ");
			   h_xz_sql.append(" SCQTJZMJ, SCFTXS, GYTDMJ, FTTDMJ, DYTDMJ, TDSYQR, FDCJYJG, GHYT, FWJG, JGSJ, FWLX, FWXZ, ZDMJ, SYMJ, CQLY, QTGSD, QTGSX, QTGSN, QTGSB, ");
			   h_xz_sql.append(" FCFHT, ZT, QXDM, QXMC, DJQDM, DJQMC, DJZQDM, DJZQMC, YXBZ, CQZT, DYZT, XZZT, BLZT, YYZT, DCXMID, FH, DJZT, BGZT, CREATETIME, MODIFYTIME, ");
			   h_xz_sql.append(" FCFHTWJ, YFWXZ, YFWYT, YGHYT, YFWJG, YZL, XMZL, PACTNO, XMMC, FWCB, GZWLX, YFWCB, RELATIONID, SFLJQPJYC, QLXZ, FWXZMC, FWYTMC, MJDWMC, ");
			   h_xz_sql.append(" TDSYQQSRQ, TDSYQZZRQ, TDSYNX, FCFHTSLTX, FWTDYT, CRJJE, QSC, ZZC, SZC, DYH, BZ, NBDCDYH, FCFHTWJLX, YZRZBDCDYID, NZDBDCDYID, XZB, YZB,");
			   h_xz_sql.append(" MARKERZT, MARKERZTMC, MARKERSM, MARKERTIME, SEARCHSTATE, SFSZZ, HJSON, HTH, MSR, CREATOR, MODIFYER, ISJG,   SFCT, CTXMBH, FWJG1,");
			   h_xz_sql.append(" FWJG2, FWJG3, GJZLX, ISPACKAGED, PACKAGESTAFF, PACKAGETIME, GLQDZR from bdck.bdcs_h_gz h where exists ( ");
			   h_xz_sql.append(" SELECT BDCDYID FROM BDCK.BDCS_DJDY_GZ DJDY WHERE DJDY.BDCDYID = H.BDCDYID AND DJDY.XMBH='"+xmbh2+"')");
	    getCommonDao().excuteQueryNoResult(h_xz_sql.toString());
		//拷贝历史层
	    StringBuilder h_ls_sql = new StringBuilder(" insert into BDCK.BDCS_H_LS (BDCDYID, YSDM, BDCDYH, XMBH, FWBM, ZRZBDCDYID, ZDDM, ZDBDCDYID, ZRZH, LJZID, LJZH, CID, CH, ZL, MJDW, ZCS, HH, ");
	       h_ls_sql.append(" SHBW, HX, HXJG, FWYT1, FWYT2, FWYT3, YCJZMJ, YCTNJZMJ, YCFTJZMJ, YCDXBFJZMJ, YCQTJZMJ, YCFTXS, SCJZMJ, SCTNJZMJ, SCFTJZMJ, SCDXBFJZMJ, ");
	       h_ls_sql.append(" SCQTJZMJ, SCFTXS, GYTDMJ, FTTDMJ, DYTDMJ, TDSYQR, FDCJYJG, GHYT, FWJG, JGSJ, FWLX, FWXZ, ZDMJ, SYMJ, CQLY, QTGSD, QTGSX, QTGSN, QTGSB, ");
	       h_ls_sql.append(" FCFHT, ZT, QXDM, QXMC, DJQDM, DJQMC, DJZQDM, DJZQMC, YXBZ, CQZT, DYZT, XZZT, BLZT, YYZT, DCXMID, FH, DJZT, BGZT, CREATETIME, MODIFYTIME, ");
	       h_ls_sql.append(" FCFHTWJ, YFWXZ, YFWYT, YGHYT, YFWJG, YZL, XMZL, PACTNO, XMMC, FWCB, GZWLX, YFWCB, RELATIONID, SFLJQPJYC, QLXZ, FWXZMC, FWYTMC, MJDWMC, ");
	       h_ls_sql.append(" TDSYQQSRQ, TDSYQZZRQ, TDSYNX, FCFHTSLTX, FWTDYT, CRJJE, QSC, ZZC, SZC, DYH, BZ, NBDCDYH, FCFHTWJLX, YZRZBDCDYID, NZDBDCDYID, XZB, YZB,");
	       h_ls_sql.append(" MARKERZT, MARKERZTMC, MARKERSM, MARKERTIME, SEARCHSTATE, SFSZZ, HJSON, HTH, MSR, CREATOR, MODIFYER, ISJG,   SFCT, CTXMBH, FWJG1,");
	       h_ls_sql.append(" FWJG2, FWJG3, GJZLX, ISPACKAGED, PACKAGESTAFF, PACKAGETIME, GLQDZR) ");
		   h_ls_sql.append(" select BDCDYID, YSDM, BDCDYH, XMBH, FWBM, ZRZBDCDYID, ZDDM, ZDBDCDYID, ZRZH, LJZID, LJZH, CID, CH, ZL, MJDW, ZCS, HH, ");
		   h_ls_sql.append(" SHBW, HX, HXJG, FWYT1, FWYT2, FWYT3, YCJZMJ, YCTNJZMJ, YCFTJZMJ, YCDXBFJZMJ, YCQTJZMJ, YCFTXS, SCJZMJ, SCTNJZMJ, SCFTJZMJ, SCDXBFJZMJ, ");
		   h_ls_sql.append(" SCQTJZMJ, SCFTXS, GYTDMJ, FTTDMJ, DYTDMJ, TDSYQR, FDCJYJG, GHYT, FWJG, JGSJ, FWLX, FWXZ, ZDMJ, SYMJ, CQLY, QTGSD, QTGSX, QTGSN, QTGSB, ");
		   h_ls_sql.append(" FCFHT, ZT, QXDM, QXMC, DJQDM, DJQMC, DJZQDM, DJZQMC, YXBZ, CQZT, DYZT, XZZT, BLZT, YYZT, DCXMID, FH, DJZT, BGZT, CREATETIME, MODIFYTIME, ");
		   h_ls_sql.append(" FCFHTWJ, YFWXZ, YFWYT, YGHYT, YFWJG, YZL, XMZL, PACTNO, XMMC, FWCB, GZWLX, YFWCB, RELATIONID, SFLJQPJYC, QLXZ, FWXZMC, FWYTMC, MJDWMC, ");
		   h_ls_sql.append(" TDSYQQSRQ, TDSYQZZRQ, TDSYNX, FCFHTSLTX, FWTDYT, CRJJE, QSC, ZZC, SZC, DYH, BZ, NBDCDYH, FCFHTWJLX, YZRZBDCDYID, NZDBDCDYID, XZB, YZB,");
		   h_ls_sql.append(" MARKERZT, MARKERZTMC, MARKERSM, MARKERTIME, SEARCHSTATE, SFSZZ, HJSON, HTH, MSR, CREATOR, MODIFYER, ISJG,   SFCT, CTXMBH, FWJG1,");
		   h_ls_sql.append(" FWJG2, FWJG3, GJZLX, ISPACKAGED, PACKAGESTAFF, PACKAGETIME, GLQDZR from bdck.bdcs_h_gz h  where exists ( ");
		   h_ls_sql.append(" SELECT BDCDYID FROM BDCK.BDCS_DJDY_GZ DJDY WHERE DJDY.BDCDYID = H.BDCDYID AND DJDY.XMBH='"+xmbh2+"')");
		getCommonDao().excuteQueryNoResult(h_ls_sql.toString());
			   
		// 自然幢 现状层
		
		StringBuilder cont_xz_sql = new StringBuilder(" from bdck.bdcs_zrz_xz where bdcdyid in( select bdcdyid from bdck.bdcs_zrz_gz zrz  where exists  (SELECT 1  FROM BDCK.BDCS_DJDY_GZ DJDY   LEFT JOIN BDCK.BDCS_H_GZ H   ON H.BDCDYID = DJDY.BDCDYID   WHERE ZRZ.BDCDYID = H.ZRZBDCDYID   AND DJDY.XMBH = '"+xmbh2+"'))") ;
		long count = getCommonDao().getCountByFullSql(cont_xz_sql.toString());
		if(count == 0) {
			StringBuilder zrz_xz_sql = new StringBuilder("insert into BDCK.BDCS_ZRZ_XZ (BDCDYID, YSDM, XMMC, BDCDYH, XMBH, ZDDM, ZDBDCDYID, ZRZH, ZL, JZWMC, JGRQ, JZWGD, ZZDMJ, ZYDMJ, YCJZMJ, SCJZMJ,"); 
			zrz_xz_sql.append(" TDSYQR, DYTDMJ, FTTDMJ, FDCJYJG, ZCS, DSCS, DXCS, DXSD, GHYT, FWJG, ZTS, JZWJBYT, BZ, ZT, QXDM, QXMC, DJQDM, DJQMC, DJZQDM, DJZQMC, DCXMID, ");
			zrz_xz_sql.append(" YXBZ, FCFHT, DJZT, FCFHTWJ, CREATETIME, MODIFYTIME, YCBDCDYID, RELATIONID, ZDTWJ, ZZSYQQSRQ, ZZSYNX, ZZSYQZZRQ, ZZHBZCRQSRQ, ZZHBZCRSYNX, ");
			zrz_xz_sql.append(" ZZHBZCRZZRQ, ZZYCNXQSRQ, ZZYCNXSYNX, ZZYCNXZZRQ, ZZTDDJ, FZZSYQQSRQ, FZZSYNX, FZZSYQZZRQ, FZZHBZCRQSRQ, FZZHBZCRSYNX, FZZHBZCRZZRQ, FZZYCNXQSRQ, "); 
			zrz_xz_sql.append(" FZZYCNXSYNX, FZZYCNXZZRQ, FZZTDDJ, ZRZZDT, FTXS, SFFTXS, BDCDYHZH, NZDBDCDYID, NBDCDYH, GC, QLSDFS, KFSMC, KFSZJH, CREATOR, MODIFYER, ");
			zrz_xz_sql.append(" FTZT,   ISPACKAGED, PACKAGESTAFF, PACKAGETIME  ) ");
			zrz_xz_sql.append(" select BDCDYID, YSDM, XMMC, BDCDYH, XMBH, ZDDM, ZDBDCDYID, ZRZH, ZL, JZWMC, JGRQ, JZWGD, ZZDMJ, ZYDMJ, YCJZMJ, SCJZMJ,"); 
			zrz_xz_sql.append(" TDSYQR, DYTDMJ, FTTDMJ, FDCJYJG, ZCS, DSCS, DXCS, DXSD, GHYT, FWJG, ZTS, JZWJBYT, BZ, ZT, QXDM, QXMC, DJQDM, DJQMC, DJZQDM, DJZQMC, DCXMID, "); 
			zrz_xz_sql.append(" YXBZ, FCFHT, DJZT, FCFHTWJ, CREATETIME, MODIFYTIME, YCBDCDYID, RELATIONID, ZDTWJ, ZZSYQQSRQ, ZZSYNX, ZZSYQZZRQ, ZZHBZCRQSRQ, ZZHBZCRSYNX, ");
			zrz_xz_sql.append(" ZZHBZCRZZRQ, ZZYCNXQSRQ, ZZYCNXSYNX, ZZYCNXZZRQ, ZZTDDJ, FZZSYQQSRQ, FZZSYNX, FZZSYQZZRQ, FZZHBZCRQSRQ, FZZHBZCRSYNX, FZZHBZCRZZRQ, FZZYCNXQSRQ,"); 
			zrz_xz_sql.append(" FZZYCNXSYNX, FZZYCNXZZRQ, FZZTDDJ, ZRZZDT, FTXS, SFFTXS, BDCDYHZH, NZDBDCDYID, NBDCDYH, GC, QLSDFS, KFSMC, KFSZJH, CREATOR, MODIFYER,");
			zrz_xz_sql.append(" FTZT,   ISPACKAGED, PACKAGESTAFF, PACKAGETIME   ");
			zrz_xz_sql.append(" from bdck.bdcs_zrz_gz zrz where exists( ");
			zrz_xz_sql.append(" SELECT 1 FROM BDCK.BDCS_DJDY_GZ DJDY LEFT JOIN BDCK.BDCS_H_GZ H ON H.BDCDYID= DJDY.BDCDYID WHERE ZRZ.BDCDYID=H.ZRZBDCDYID AND DJDY.XMBH='"+xmbh2+"')");
			getCommonDao().excuteQueryNoResult(zrz_xz_sql.toString());
		}
		//历史层
		StringBuilder cont_ls_sql = new StringBuilder(" from bdck.bdcs_zrz_ls where bdcdyid in( select bdcdyid from bdck.bdcs_zrz_gz zrz  where exists  (SELECT 1  FROM BDCK.BDCS_DJDY_GZ DJDY   LEFT JOIN BDCK.BDCS_H_GZ H   ON H.BDCDYID = DJDY.BDCDYID   WHERE ZRZ.BDCDYID = H.ZRZBDCDYID   AND DJDY.XMBH = '"+xmbh2+"'))") ;
		long count_ls = getCommonDao().getCountByFullSql(cont_ls_sql.toString());
		if(count_ls == 0) {
			StringBuilder zrz_ls_sql = new StringBuilder("insert into BDCK.BDCS_ZRZ_XZ (BDCDYID, YSDM, XMMC, BDCDYH, XMBH, ZDDM, ZDBDCDYID, ZRZH, ZL, JZWMC, JGRQ, JZWGD, ZZDMJ, ZYDMJ, YCJZMJ, SCJZMJ,"); 
			zrz_ls_sql.append(" TDSYQR, DYTDMJ, FTTDMJ, FDCJYJG, ZCS, DSCS, DXCS, DXSD, GHYT, FWJG, ZTS, JZWJBYT, BZ, ZT, QXDM, QXMC, DJQDM, DJQMC, DJZQDM, DJZQMC, DCXMID, ");
			zrz_ls_sql.append(" YXBZ, FCFHT, DJZT, FCFHTWJ, CREATETIME, MODIFYTIME, YCBDCDYID, RELATIONID, ZDTWJ, ZZSYQQSRQ, ZZSYNX, ZZSYQZZRQ, ZZHBZCRQSRQ, ZZHBZCRSYNX, ");
			zrz_ls_sql.append(" ZZHBZCRZZRQ, ZZYCNXQSRQ, ZZYCNXSYNX, ZZYCNXZZRQ, ZZTDDJ, FZZSYQQSRQ, FZZSYNX, FZZSYQZZRQ, FZZHBZCRQSRQ, FZZHBZCRSYNX, FZZHBZCRZZRQ, FZZYCNXQSRQ, "); 
			zrz_ls_sql.append(" FZZYCNXSYNX, FZZYCNXZZRQ, FZZTDDJ, ZRZZDT, FTXS, SFFTXS, BDCDYHZH, NZDBDCDYID, NBDCDYH,  GC, QLSDFS, KFSMC, KFSZJH, CREATOR, MODIFYER, ");
			zrz_ls_sql.append(" FTZT,   ISPACKAGED, PACKAGESTAFF, PACKAGETIME  ) ");
			zrz_ls_sql.append(" select BDCDYID, YSDM, XMMC, BDCDYH, XMBH, ZDDM, ZDBDCDYID, ZRZH, ZL, JZWMC, JGRQ, JZWGD, ZZDMJ, ZYDMJ, YCJZMJ, SCJZMJ,"); 
			zrz_ls_sql.append(" TDSYQR, DYTDMJ, FTTDMJ, FDCJYJG, ZCS, DSCS, DXCS, DXSD, GHYT, FWJG, ZTS, JZWJBYT, BZ, ZT, QXDM, QXMC, DJQDM, DJQMC, DJZQDM, DJZQMC, DCXMID, "); 
			zrz_ls_sql.append(" YXBZ, FCFHT, DJZT, FCFHTWJ, CREATETIME, MODIFYTIME, YCBDCDYID, RELATIONID, ZDTWJ, ZZSYQQSRQ, ZZSYNX, ZZSYQZZRQ, ZZHBZCRQSRQ, ZZHBZCRSYNX, ");
			zrz_ls_sql.append(" ZZHBZCRZZRQ, ZZYCNXQSRQ, ZZYCNXSYNX, ZZYCNXZZRQ, ZZTDDJ, FZZSYQQSRQ, FZZSYNX, FZZSYQZZRQ, FZZHBZCRQSRQ, FZZHBZCRSYNX, FZZHBZCRZZRQ, FZZYCNXQSRQ,"); 
			zrz_ls_sql.append(" FZZYCNXSYNX, FZZYCNXZZRQ, FZZTDDJ, ZRZZDT, FTXS, SFFTXS, BDCDYHZH, NZDBDCDYID, NBDCDYH, GC, QLSDFS, KFSMC, KFSZJH, CREATOR, MODIFYER,");
			zrz_ls_sql.append(" FTZT,   ISPACKAGED, PACKAGESTAFF, PACKAGETIME   ");
			zrz_ls_sql.append(" from bdck.bdcs_zrz_gz zrz where exists( ");
			zrz_ls_sql.append(" SELECT 1 FROM BDCK.BDCS_DJDY_GZ DJDY LEFT JOIN BDCK.BDCS_H_GZ H ON H.BDCDYID= DJDY.BDCDYID WHERE ZRZ.BDCDYID=H.ZRZBDCDYID AND DJDY.XMBH='"+xmbh2+"')");
			getCommonDao().excuteQueryNoResult(zrz_ls_sql.toString());
			
		}
		
	}
	protected boolean CopyFSQLEX() throws Exception {
		String xmbh2 = super.getXMBH();
		//拷贝现状层附属权利
		StringBuilder fsql_xz_sql = new StringBuilder("INSERT INTO BDCK.BDCS_FSQL_XZ (FSQLID, DJDYID, BDCDYH, XMBH, QLID, ZDDM, NYDMJ, GDMJ, LDMJ, CDMJ, QTNYDMJ, JSYDMJ, WLYDMJ, ");
			    fsql_xz_sql.append("SYQMJ, SYJZE, SYJBZYJ, SYJJNQK, HYMJDW, HYMJ, JZMJ, FDZL, TDSYQR, DYTDMJ, FTTDMJ, FDCJYJG, JGSJ, ZH, ZCS, GHYT, FWJG, FWXZ, SZC, ZYJZMJ, FTJZMJ,");
			    fsql_xz_sql.append(" LMMJ, FBF, ZS, LZ, ZLND, XDM, GYDR, DYQNR, DYWLX, DYSW, GRDR1, DYQNR1, SCYQMJ, DYFS, ZJGCJD, BDBZZQSE, DBFW, ZGZQQDSS, ZGZQSE, YGDJZL, YWR, YWRZJZL,");
			    fsql_xz_sql.append(" YYSX, ZXYYYY, CFLX, LHSX, CFSJ, CFJG, CFWH, CFWJ, CFFW, JFJG, JFWH, JFWJ, ZYSZ, ZWRZJH, DBRZJH, HTBH, DYMJ, YXBZ, ZXDYYWH, ZXDYYY, ZXSJ, JGZWBH, JGZWMC,");
			    fsql_xz_sql.append(" JGZWSL, JGZWMJ, ZL, TDHYSYQR, TDHYSYMJ, GJZWLX, GJZWGHYT, GJZWMJ, CBSYQMJ, TDSYQXZ, SYTTLX, YZYFS, CYZL, SYZCL, LDSYQXZ, SLLMSYQR1, SLLMSYQR2, QY, LB, XB,");
			    fsql_xz_sql.append(" QSFS, SYLX, QSL, QSYT, KCMJ, KCFS, KCKZ, SCGM, XYDZL, GYDQLR, GYDQLRZJH, DYBDCLX, ZJJZWZL, ZJJZWDYFW, BDCZL, YWRZJH, QDJG, GYDQLRZJZL, DCXMID, CREATETIME, ");
			    fsql_xz_sql.append(" MODIFYTIME, ZXDBR, ZXFJ, DYR, DYPGJZ, CASENUM, GYRQK, ZQDW, ZWR, PLAINTIFF, DEFENDANT, TDPGJZ, YDZMJ, DYYDMJ, ZJZMJ, DYJZMJ, DYTDYT, DYDYJXZ,   SFTGHL,");
			    fsql_xz_sql.append(" LMXZ, QLYJ, LDSYQR, LDSHYQR, QDFS, CBHTBM, CBFCYSL, CBFDCRQ, CBFDCY, CBFDCJS, GSJS, GSJSR, GSR, GSRQ, MJ, DKS, HTRQ, YT, HTH)");
			    fsql_xz_sql.append(" SELECT FSQLID, DJDYID, BDCDYH, XMBH, QLID, ZDDM, NYDMJ, GDMJ, LDMJ, CDMJ, QTNYDMJ, JSYDMJ, WLYDMJ,");
			    fsql_xz_sql.append(" SYQMJ, SYJZE, SYJBZYJ, SYJJNQK, HYMJDW, HYMJ, JZMJ, FDZL, TDSYQR, DYTDMJ, FTTDMJ, FDCJYJG, JGSJ, ZH, ZCS, GHYT, FWJG, FWXZ, SZC, ZYJZMJ, FTJZMJ,");
			    fsql_xz_sql.append(" LMMJ, FBF, ZS, LZ, ZLND, XDM, GYDR, DYQNR, DYWLX, DYSW, GRDR1, DYQNR1, SCYQMJ, DYFS, ZJGCJD, BDBZZQSE, DBFW, ZGZQQDSS, ZGZQSE, YGDJZL, YWR, YWRZJZL,");
			    fsql_xz_sql.append(" YYSX, ZXYYYY, CFLX, LHSX, CFSJ, CFJG, CFWH, CFWJ, CFFW, JFJG, JFWH, JFWJ, ZYSZ, ZWRZJH, DBRZJH, HTBH, DYMJ, YXBZ, ZXDYYWH, ZXDYYY, ZXSJ, JGZWBH, JGZWMC,");
			    fsql_xz_sql.append(" JGZWSL, JGZWMJ, ZL, TDHYSYQR, TDHYSYMJ, GJZWLX, GJZWGHYT, GJZWMJ, CBSYQMJ, TDSYQXZ, SYTTLX, YZYFS, CYZL, SYZCL, LDSYQXZ, SLLMSYQR1, SLLMSYQR2, QY, LB, XB,");
			    fsql_xz_sql.append(" QSFS, SYLX, QSL, QSYT, KCMJ, KCFS, KCKZ, SCGM, XYDZL, GYDQLR, GYDQLRZJH, DYBDCLX, ZJJZWZL, ZJJZWDYFW, BDCZL, YWRZJH, QDJG, GYDQLRZJZL, DCXMID, CREATETIME, ");
			    fsql_xz_sql.append(" MODIFYTIME, ZXDBR, ZXFJ, DYR, DYPGJZ, CASENUM, GYRQK, ZQDW, ZWR, PLAINTIFF, DEFENDANT, TDPGJZ, YDZMJ, DYYDMJ, ZJZMJ, DYJZMJ, DYTDYT, DYDYJXZ,   SFTGHL,");
			    fsql_xz_sql.append(" LMXZ, QLYJ, LDSYQR, LDSHYQR, QDFS, CBHTBM, CBFCYSL, CBFDCRQ, CBFDCY, CBFDCJS, GSJS, GSJSR, GSR, GSRQ, MJ, DKS, HTRQ, YT, HTH FROM BDCK.BDCS_FSQL_GZ WHERE XMBH='"+xmbh2+"'");
		getCommonDao().excuteQueryNoResult(fsql_xz_sql.toString());
	   //拷贝历史层附属权利
	   StringBuilder fsql_ls_sql = new StringBuilder("INSERT INTO BDCK.BDCS_FSQL_LS (FSQLID, DJDYID, BDCDYH, XMBH, QLID, ZDDM, NYDMJ, GDMJ, LDMJ, CDMJ, QTNYDMJ, JSYDMJ, WLYDMJ, ");
			    fsql_ls_sql.append("SYQMJ, SYJZE, SYJBZYJ, SYJJNQK, HYMJDW, HYMJ, JZMJ, FDZL, TDSYQR, DYTDMJ, FTTDMJ, FDCJYJG, JGSJ, ZH, ZCS, GHYT, FWJG, FWXZ, SZC, ZYJZMJ, FTJZMJ,");
			    fsql_ls_sql.append(" LMMJ, FBF, ZS, LZ, ZLND, XDM, GYDR, DYQNR, DYWLX, DYSW, GRDR1, DYQNR1, SCYQMJ, DYFS, ZJGCJD, BDBZZQSE, DBFW, ZGZQQDSS, ZGZQSE, YGDJZL, YWR, YWRZJZL,");
			    fsql_ls_sql.append(" YYSX, ZXYYYY, CFLX, LHSX, CFSJ, CFJG, CFWH, CFWJ, CFFW, JFJG, JFWH, JFWJ, ZYSZ, ZWRZJH, DBRZJH, HTBH, DYMJ, YXBZ, ZXDYYWH, ZXDYYY, ZXSJ, JGZWBH, JGZWMC,");
			    fsql_ls_sql.append(" JGZWSL, JGZWMJ, ZL, TDHYSYQR, TDHYSYMJ, GJZWLX, GJZWGHYT, GJZWMJ, CBSYQMJ, TDSYQXZ, SYTTLX, YZYFS, CYZL, SYZCL, LDSYQXZ, SLLMSYQR1, SLLMSYQR2, QY, LB, XB,");
			    fsql_ls_sql.append(" QSFS, SYLX, QSL, QSYT, KCMJ, KCFS, KCKZ, SCGM, XYDZL, GYDQLR, GYDQLRZJH, DYBDCLX, ZJJZWZL, ZJJZWDYFW, BDCZL, YWRZJH, QDJG, GYDQLRZJZL, DCXMID, CREATETIME, ");
			    fsql_ls_sql.append(" MODIFYTIME, ZXDBR, ZXFJ, DYR, DYPGJZ, CASENUM, GYRQK, ZQDW, ZWR, PLAINTIFF, DEFENDANT, TDPGJZ, YDZMJ, DYYDMJ, ZJZMJ, DYJZMJ, DYTDYT, DYDYJXZ,   SFTGHL,");
			    fsql_ls_sql.append(" LMXZ, QLYJ, LDSYQR, LDSHYQR, QDFS, CBHTBM, CBFCYSL, CBFDCRQ, CBFDCY, CBFDCJS, GSJS, GSJSR, GSR, GSRQ, MJ, DKS, HTRQ, YT, HTH)");
			    fsql_ls_sql.append(" SELECT FSQLID, DJDYID, BDCDYH, XMBH, QLID, ZDDM, NYDMJ, GDMJ, LDMJ, CDMJ, QTNYDMJ, JSYDMJ, WLYDMJ,");
			    fsql_ls_sql.append(" SYQMJ, SYJZE, SYJBZYJ, SYJJNQK, HYMJDW, HYMJ, JZMJ, FDZL, TDSYQR, DYTDMJ, FTTDMJ, FDCJYJG, JGSJ, ZH, ZCS, GHYT, FWJG, FWXZ, SZC, ZYJZMJ, FTJZMJ,");
			    fsql_ls_sql.append(" LMMJ, FBF, ZS, LZ, ZLND, XDM, GYDR, DYQNR, DYWLX, DYSW, GRDR1, DYQNR1, SCYQMJ, DYFS, ZJGCJD, BDBZZQSE, DBFW, ZGZQQDSS, ZGZQSE, YGDJZL, YWR, YWRZJZL,");
			    fsql_ls_sql.append(" YYSX, ZXYYYY, CFLX, LHSX, CFSJ, CFJG, CFWH, CFWJ, CFFW, JFJG, JFWH, JFWJ, ZYSZ, ZWRZJH, DBRZJH, HTBH, DYMJ, YXBZ, ZXDYYWH, ZXDYYY, ZXSJ, JGZWBH, JGZWMC,");
			    fsql_ls_sql.append(" JGZWSL, JGZWMJ, ZL, TDHYSYQR, TDHYSYMJ, GJZWLX, GJZWGHYT, GJZWMJ, CBSYQMJ, TDSYQXZ, SYTTLX, YZYFS, CYZL, SYZCL, LDSYQXZ, SLLMSYQR1, SLLMSYQR2, QY, LB, XB,");
			    fsql_ls_sql.append(" QSFS, SYLX, QSL, QSYT, KCMJ, KCFS, KCKZ, SCGM, XYDZL, GYDQLR, GYDQLRZJH, DYBDCLX, ZJJZWZL, ZJJZWDYFW, BDCZL, YWRZJH, QDJG, GYDQLRZJZL, DCXMID, CREATETIME, ");
			    fsql_ls_sql.append(" MODIFYTIME, ZXDBR, ZXFJ, DYR, DYPGJZ, CASENUM, GYRQK, ZQDW, ZWR, PLAINTIFF, DEFENDANT, TDPGJZ, YDZMJ, DYYDMJ, ZJZMJ, DYJZMJ, DYTDYT, DYDYJXZ,  SFTGHL,");
			    fsql_ls_sql.append(" LMXZ, QLYJ, LDSYQR, LDSHYQR, QDFS, CBHTBM, CBFCYSL, CBFDCRQ, CBFDCY, CBFDCJS, GSJS, GSJSR, GSR, GSRQ, MJ, DKS, HTRQ, YT, HTH FROM BDCK.BDCS_FSQL_GZ WHERE XMBH='"+xmbh2+"'");    
		getCommonDao().excuteQueryNoResult(fsql_ls_sql.toString());
			 return true;
	}
	protected boolean CopyQDZREX() throws Exception {
		String xmbh2 = super.getXMBH();
		//拷贝现状层层
		StringBuilder qdzr_xz_sql = new StringBuilder("INSERT INTO BDCK.BDCS_QDZR_XZ (ID, XMBH, DJDYID, QLID, QLRID, ZSID, FSQLID, DCXMID, BDCDYH, CREATETIME, MODIFYTIME)");
			  qdzr_xz_sql.append(" SELECT ID,XMBH, DJDYID, QLID, QLRID, ZSID, FSQLID, DCXMID, BDCDYH, CREATETIME, MODIFYTIME");
			  qdzr_xz_sql.append(" FROM BDCK.BDCS_QDZR_GZ WHERE XMBH='"+xmbh2+"'");
		getCommonDao().excuteQueryNoResult(qdzr_xz_sql.toString());	 
		//拷贝历史层
		StringBuilder qdzr_ls_sql = new StringBuilder("INSERT INTO BDCK.BDCS_QDZR_LS (ID, XMBH, DJDYID, QLID, QLRID, ZSID, FSQLID, DCXMID, BDCDYH, CREATETIME, MODIFYTIME)");
		  qdzr_ls_sql.append(" SELECT ID,XMBH, DJDYID, QLID, QLRID, ZSID, FSQLID, DCXMID, BDCDYH, CREATETIME, MODIFYTIME");
		  qdzr_ls_sql.append(" FROM BDCK.BDCS_QDZR_GZ WHERE XMBH='"+xmbh2+"'");
		getCommonDao().excuteQueryNoResult(qdzr_ls_sql.toString());	 
		return true;
	}
	protected boolean CopyZSEX() throws Exception {
		String xmbh2 = super.getXMBH();
		//拷贝现状层数据
		StringBuilder zs_xz_sql = new StringBuilder("INSERT INTO BDCK.BDCS_ZS_XZ (XMBH, ZSID, ZSBH, BDCQZH, SZSJ, CREATETIME, MODIFYTIME, CFDAGH, ZSDATA)");
			  zs_xz_sql.append(" SELECT XMBH, ZSID, ZSBH, BDCQZH, SZSJ,  CREATETIME, MODIFYTIME, CFDAGH, ZSDATA");
			  zs_xz_sql.append(" FROM BDCK.BDCS_ZS_GZ WHERE XMBH='"+xmbh2+"'");
		getCommonDao().excuteQueryNoResult(zs_xz_sql.toString());
		//拷贝历史层数据
		StringBuilder zs_ls_sql = new StringBuilder("INSERT INTO BDCK.BDCS_ZS_LS (XMBH, ZSID, ZSBH, BDCQZH, SZSJ, CREATETIME, MODIFYTIME, CFDAGH, ZSDATA)");
		  zs_ls_sql.append(" SELECT XMBH, ZSID, ZSBH, BDCQZH, SZSJ, CREATETIME, MODIFYTIME, CFDAGH, ZSDATA");
		  zs_ls_sql.append(" FROM BDCK.BDCS_ZS_GZ WHERE XMBH='"+xmbh2+"'");
		getCommonDao().excuteQueryNoResult(zs_ls_sql.toString());
		return true;
	}	
	/**
	 * 如果实测绘户关联预测绘户上有查封，则拷贝查封信息到实测户上
	 * @param bdcdyid
	 * @param djdyid
	 */
	private void CopyLimitFromYCH(String bdcdyid,String djdyid) {
		List<YC_SC_H_XZ> gxs = getCommonDao().getDataList(YC_SC_H_XZ.class, "SCBDCDYID='" + bdcdyid + "'");
		if (gxs != null && gxs.size() > 0) {
			String ycbdcyid = gxs.get(0).getYCBDCDYID();
			if (!StringHelper.isEmpty(ycbdcyid)) {
				List<BDCS_DJDY_XZ> djdys_yc = getCommonDao().getDataList(BDCS_DJDY_XZ.class, "BDCDYID='" + ycbdcyid + "' AND BDCDYLX='032'");
				if (djdys_yc != null && djdys_yc.size() > 0) {
					String djdyid_yc = djdys_yc.get(0).getDJDYID();
					if (!StringHelper.isEmpty(djdyid_yc)) {
						List<BDCS_QL_XZ> qls_yc = getCommonDao().getDataList(BDCS_QL_XZ.class, "DJDYID='" + djdyid_yc + "' AND QLLX='99' AND DJLX='800'");
						if (qls_yc != null && qls_yc.size() > 0) {
							for (BDCS_QL_XZ ql_yc : qls_yc) {
								String cfqlid = SuperHelper.GeneratePrimaryKey();
								String cffsqlid = SuperHelper.GeneratePrimaryKey();
								BDCS_QL_XZ cfql_sc = new BDCS_QL_XZ();
								try {
									PropertyUtils.copyProperties(cfql_sc, ql_yc);
								} catch (Exception e) {
								}
								cfql_sc.setId(cfqlid);
								cfql_sc.setYWH(super.getProject_id());
								cfql_sc.setXMBH(getXMBH());
								cfql_sc.setDJDYID(djdyid);
								cfql_sc.setFSQLID(cffsqlid);
								cfql_sc.setDBR(Global.getCurrentUserName());
								cfql_sc.setDJSJ(new Date());
								cfql_sc.setDJJG(ConfigHelper.getNameByValue("DJJGMC"));
								cfql_sc.setLYQLID(ql_yc.getId());
								getCommonDao().save(cfql_sc);
								BDCS_QL_LS cfql_sc_ls = new BDCS_QL_LS();
								try {
									PropertyUtils.copyProperties(cfql_sc_ls, cfql_sc);
								} catch (Exception e) {
								}
								getCommonDao().save(cfql_sc_ls);
								if (!StringHelper.isEmpty(ql_yc.getFSQLID())) {
									BDCS_FSQL_XZ fsql_yc = getCommonDao().get(BDCS_FSQL_XZ.class, ql_yc.getFSQLID());
									if (fsql_yc != null) {
										BDCS_FSQL_XZ cffsql_sc = new BDCS_FSQL_XZ();
										try {
											PropertyUtils.copyProperties(cffsql_sc, fsql_yc);
										} catch (Exception e) {
										}
										cffsql_sc.setQLID(cfqlid);
										cffsql_sc.setId(cffsqlid);
										cffsql_sc.setXMBH(getXMBH());
										cffsql_sc.setDJDYID(djdyid);
										getCommonDao().save(cffsql_sc);
										BDCS_FSQL_LS cffsql_sc_ls = new BDCS_FSQL_LS();
										try {
											PropertyUtils.copyProperties(cffsql_sc_ls, cffsql_sc);
										} catch (Exception e) {
										}
										getCommonDao().save(cffsql_sc_ls);
										//注销期房查封
										if(("3").equals(ConfigHelper.getNameByValue("CopyLimitFromYCH"))){
											
											if (fsql_yc != null) {
												//标记注销状态
												BDCS_FSQL_LS fsql_ycls = getCommonDao().get(BDCS_FSQL_LS.class, ql_yc.getFSQLID());
												fsql_ycls.setZXDBR(Global.getCurrentUserName());
												fsql_ycls.setZXSJ(new Date());
												fsql_ycls.setZXFJ("单元办理现房首次登记，期房查封转移到现房查封，并对期房查封进行了注销");
												getCommonDao().save(fsql_ycls);
												//删除期房的查封信息
												getCommonDao().deleteEntity(ql_yc);
												getCommonDao().deleteEntity(fsql_yc);
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}
}
