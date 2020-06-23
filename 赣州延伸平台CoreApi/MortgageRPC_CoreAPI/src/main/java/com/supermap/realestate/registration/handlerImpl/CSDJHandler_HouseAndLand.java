package com.supermap.realestate.registration.handlerImpl;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.PropertyUtils;

import com.supermap.realestate.registration.ViewClass.ProjectInfo;
import com.supermap.realestate.registration.ViewClass.UnitTree;
import com.supermap.realestate.registration.handler.DJHandler;
import com.supermap.realestate.registration.model.BDCS_DJDY_GZ;
import com.supermap.realestate.registration.model.BDCS_DJDY_LS;
import com.supermap.realestate.registration.model.BDCS_DJDY_XZ;
import com.supermap.realestate.registration.model.BDCS_FSQL_GZ;
import com.supermap.realestate.registration.model.BDCS_FSQL_LS;
import com.supermap.realestate.registration.model.BDCS_FSQL_XZ;
import com.supermap.realestate.registration.model.BDCS_QDZR_GZ;
import com.supermap.realestate.registration.model.BDCS_QDZR_LS;
import com.supermap.realestate.registration.model.BDCS_QDZR_XZ;
import com.supermap.realestate.registration.model.BDCS_QLR_LS;
import com.supermap.realestate.registration.model.BDCS_QLR_XZ;
import com.supermap.realestate.registration.model.BDCS_QL_GZ;
import com.supermap.realestate.registration.model.BDCS_QL_LS;
import com.supermap.realestate.registration.model.BDCS_QL_XZ;
import com.supermap.realestate.registration.model.BDCS_SQR;
import com.supermap.realestate.registration.model.BDCS_XMXX;
import com.supermap.realestate.registration.model.BDCS_ZS_GZ;
import com.supermap.realestate.registration.model.BDCS_ZS_LS;
import com.supermap.realestate.registration.model.BDCS_ZS_XZ;
import com.supermap.realestate.registration.model.DCS_QLR_GZ;
import com.supermap.realestate.registration.model.WFD_MAPPING;
import com.supermap.realestate.registration.model.YC_SC_H_XZ;
import com.supermap.realestate.registration.model.interfaces.LandAttach;
import com.supermap.realestate.registration.model.interfaces.RealUnit;
import com.supermap.realestate.registration.model.interfaces.Rights;
import com.supermap.realestate.registration.model.interfaces.RightsHolder;
import com.supermap.realestate.registration.model.interfaces.SubRights;
import com.supermap.realestate.registration.model.xmlExportmodel.MessageExport;
import com.supermap.realestate.registration.tools.RightsHolderTools;
import com.supermap.realestate.registration.tools.RightsTools;
import com.supermap.realestate.registration.tools.UnitTools;
import com.supermap.realestate.registration.util.ConfigHelper;
import com.supermap.realestate.registration.util.ConstValue;
import com.supermap.realestate.registration.util.ConstValue.BDCDYLX;
import com.supermap.realestate.registration.util.ConstValue.CFLX;
import com.supermap.realestate.registration.util.ConstValue.DJDYLY;
import com.supermap.realestate.registration.util.ConstValue.DJZT;
import com.supermap.realestate.registration.util.ConstValue.QLLX;
import com.supermap.realestate.registration.util.Global;
import com.supermap.realestate.registration.util.ObjectHelper;
import com.supermap.realestate.registration.util.ProjectHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.wisdombusiness.core.SuperHelper;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import com.supermap.wisdombusiness.web.ResultMessage;

/*
 1、国有建设用地使用权/房屋（按户，按幢）所有权初始登记（房地一体）
 2、集体建设用地使用权/房屋所有权初始登记（房地一体）
 3、宅基地使用权/房屋所有权初始登记（房地一体）
 */
/**
 * 
 * 初始登记处理类(房地一体)
 * @ClassName: CSDJHandler_HouseAndLand
 * @author yuxuebin
 * @date 2016年06月15日  21:15:35
 */
public class CSDJHandler_HouseAndLand extends DJHandlerBase implements DJHandler {

	/**
	 * 构造函数
	 * 
	 * @param info
	 */
	public CSDJHandler_HouseAndLand(ProjectInfo info) {
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
	 * @作者 yuxuebin
	 * @创建时间 2016年06月15日 21:17:26
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
	 * 内部方法：添加不动产单元
	 * @Title: addbdcdy
	 * @author:yuxuebin
	 * @date：2016年06月15日  21:18:02
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
		LandAttach _landAttach = (LandAttach) _srcunit;
		RealUnit zdunit = UnitTools.loadUnit(BDCDYLX.SHYQZD, DJDYLY.DC, _landAttach.getZDBDCDYID());
		if (zdunit == null) {
			msg.setSuccess("false");
			msg.setMsg("在调查库中找不到关联的宗地！");
			return msg;
		}else{
			zdunit.setDJZT(DJZT.DJZ.Value);
			RealUnit zdunit_gz = UnitTools.loadUnit(BDCDYLX.SHYQZD, DJDYLY.GZ, _landAttach.getZDBDCDYID());
			if(zdunit_gz==null){
				zdunit_gz=UnitTools.copyUnit(zdunit, BDCDYLX.SHYQZD, DJDYLY.GZ);
				zdunit_gz.setXMBH(this.getXMBH());
				dao.save(zdunit);
				dao.save(zdunit_gz);
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
	@SuppressWarnings("rawtypes")
	@Override
	public boolean writeDJB() {
		String strSqlXMBH = ProjectHelper.GetXMBHCondition(getXMBH());
		List<BDCS_DJDY_GZ> djdys = getCommonDao().getDataList(BDCS_DJDY_GZ.class, strSqlXMBH);
		if (djdys == null || djdys.size() <= 0)
			return false;
		List<String> list_zdbdcdyid=new ArrayList<String>();
		for (int idjdy = 0; idjdy < djdys.size(); idjdy++) {
			BDCS_DJDY_GZ bdcs_djdy_gz = djdys.get(idjdy);
			String djdyid = bdcs_djdy_gz.getDJDYID();
			String bdcdyid = bdcs_djdy_gz.getBDCDYID();
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
				LandAttach _landAttach = (LandAttach) _srcunit;
				RealUnit zdunit = UnitTools.loadUnit(BDCDYLX.SHYQZD, DJDYLY.DC, _landAttach.getZDBDCDYID());
				if(zdunit!=null){
					if(!list_zdbdcdyid.contains(zdunit.getId())){
						list_zdbdcdyid.add(zdunit.getId());
					}
					zdunit.setDJZT(DJZT.YDJ.Value);
					getCommonDao().save(zdunit);
					super.CopyGeo(zdunit.getId(), zdunit.getBDCDYLX(), zdunit.getLY());
				}
			}
			
			String iscopyycf2cf = "";
			String workflowcode = ProjectHelper.getWorkflowCodeByProjectID(getProject_id());
			List<WFD_MAPPING> listCode = getCommonDao().getDataList(WFD_MAPPING.class, "WORKFLOWCODE='" + workflowcode + "'");
			if ((listCode != null) && (listCode.size() > 0)) {
				iscopyycf2cf = ((WFD_MAPPING) listCode.get(0)).getISCOPYYCF2CF();
			}
			// 如果实测绘户关联预测绘户上有查封，则拷贝查封信息到实测户上
			if (BDCDYLX.H.equals(dylx) && 
					(("1").equals(ConfigHelper.getNameByValue("CopyLimitFromYCH")) 
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
		int groupid=1;
		if(list_zdbdcdyid!=null&&list_zdbdcdyid.size()>0){
			for(String zdbdcdyid:list_zdbdcdyid){
				RealUnit unit_xz=UnitTools.copyUnit(BDCDYLX.SHYQZD, DJDYLY.GZ, DJDYLY.XZ, zdbdcdyid);
				getCommonDao().save(unit_xz);
				RealUnit unit_ls=UnitTools.copyUnit(BDCDYLX.SHYQZD, DJDYLY.GZ, DJDYLY.LS, zdbdcdyid);
				getCommonDao().save(unit_ls);
				String djdyid=super.getPrimaryKey();
				BDCS_DJDY_XZ djdy_xz=new BDCS_DJDY_XZ();
				djdy_xz.setBDCDYH(unit_xz.getBDCDYH());
				djdy_xz.setBDCDYID(zdbdcdyid);
				djdy_xz.setBDCDYLX(unit_xz.getBDCDYLX().Value);
				djdy_xz.setDJDYID(djdyid);
				djdy_xz.setGROUPID(groupid);
				groupid++;
				djdy_xz.setId(super.getPrimaryKey());
				djdy_xz.setLY(unit_xz.getLY().Value);
				djdy_xz.setXMBH(super.getXMBH());
				getCommonDao().save(djdy_xz);
				BDCS_DJDY_LS djdy_ls=new BDCS_DJDY_LS();
				ObjectHelper.copyObject(djdy_xz, djdy_ls);
				getCommonDao().save(djdy_ls);
				
				List<Rights> qls_h=RightsTools.loadRightsByCondition(DJDYLY.GZ, "XMBH='"+super.getXMBH()+"' AND DJDYID IN (SELECT DJDYID FROM BDCS_DJDY_GZ WHERE XMBH='"+super.getXMBH()+"' AND BDCDYID IN (SELECT id FROM BDCS_H_GZ WHERE ZDBDCDYID='"+zdbdcdyid+"' AND XMBH='"+super.getXMBH()+"'))");
				if(qls_h!=null&&qls_h.size()>0){
					String qlid=super.getPrimaryKey();
					String fsqlid=super.getPrimaryKey();
					Rights ql_h=qls_h.get(0);
					BDCS_QL_XZ ql_xz=new BDCS_QL_XZ();
					ql_xz.setBDCDYH(unit_xz.getBDCDYH());
					ObjectHelper.copyObject(ql_h, ql_xz);
					ql_xz.setId(qlid);
					ql_xz.setFSQLID(fsqlid);
					ql_xz.setBDCDYH(unit_xz.getBDCDYH());
					ql_xz.setBDCQZH(ql_h.getBDCQZH());
					ql_xz.setBDCQZHXH(null);
					ql_xz.setDJDYID(djdyid);
					QLLX qllx=QLLX.GYJSYDSHYQ_FWSYQ;
					if(super.getQllx().equals(QLLX.GYJSYDSHYQ_FWSYQ)){
						qllx=QLLX.GYJSYDSHYQ;
					}else if(super.getQllx().equals(QLLX.ZJDSYQ_FWSYQ)){
						qllx=QLLX.ZJDSYQ;
					}else if(super.getQllx().equals(QLLX.JTJSYDSYQ_FWSYQ)){
						qllx=QLLX.JTJSYDSYQ;
					}
					ql_xz.setQLLX(qllx.Value);
					BDCS_FSQL_XZ fsql_xz=new BDCS_FSQL_XZ();
					fsql_xz.setId(fsqlid);
					fsql_xz.setQLID(qlid);
					fsql_xz.setDJDYID(djdyid);
					fsql_xz.setBDCDYH(unit_xz.getBDCDYH());
					getCommonDao().save(ql_xz);
					getCommonDao().save(fsql_xz);
					BDCS_QL_LS ql_ls=new BDCS_QL_LS();
					ObjectHelper.copyObject(ql_xz, ql_ls);
					getCommonDao().save(ql_ls);
					BDCS_FSQL_LS fsql_ls=new BDCS_FSQL_LS();
					ObjectHelper.copyObject(fsql_xz, fsql_ls);
					getCommonDao().save(fsql_ls);
					List<RightsHolder> qlrs_h=RightsHolderTools.loadRightsHolders(DJDYLY.GZ, ql_h.getId());
					HashMap<String,String> map_zsid=new HashMap<String, String>();
					if(qlrs_h!=null&&qlrs_h.size()>0){
						for(RightsHolder qlr_h:qlrs_h){
							String qlrid=super.getPrimaryKey();
							BDCS_QLR_XZ qlr_xz=new BDCS_QLR_XZ();
							ObjectHelper.copyObject(qlr_h, qlr_xz);
							qlr_xz.setQLID(qlid);
							qlr_xz.setId(qlrid);
							qlr_xz.setBDCQZH(null);
							qlr_xz.setBDCQZHXH(null);
							getCommonDao().save(qlr_xz);
							BDCS_QLR_LS qlr_ls=new BDCS_QLR_LS();
							ObjectHelper.copyObject(qlr_xz, qlr_ls);
							getCommonDao().save(qlr_ls);
							List<BDCS_QDZR_GZ> qdzrs=getCommonDao().getDataList(BDCS_QDZR_GZ.class, "QLRID='"+qlr_h.getId()+"'");
							if(qdzrs!=null){
								BDCS_QDZR_GZ qdzr_h=qdzrs.get(0);
								String zsid_h=qdzr_h.getZSID();
								String zsid=super.getPrimaryKey();
								if(map_zsid.containsKey(zsid_h)){
									zsid=map_zsid.get(zsid_h);
								}else{
									map_zsid.put(zsid_h, zsid);
								}
								BDCS_QDZR_XZ qdzr_xz=new BDCS_QDZR_XZ();
								ObjectHelper.copyObject(qdzr_h, qdzr_xz);
								qdzr_xz.setBDCDYH(unit_xz.getBDCDYH());
								qdzr_xz.setDJDYID(djdyid);
								qdzr_xz.setFSQLID(fsqlid);
								qdzr_xz.setQLID(qlid);
								qdzr_xz.setQLRID(qlrid);
								qdzr_xz.setZSID(zsid);
								String qdzrid=super.getPrimaryKey();
								qdzr_xz.setId(qdzrid);
								getCommonDao().save(qdzr_xz);
								BDCS_QDZR_LS qdzr_ls=new BDCS_QDZR_LS();
								ObjectHelper.copyObject(qdzr_xz, qdzr_ls);
								getCommonDao().save(qdzr_ls);
							}
						}
					}
					Set set = map_zsid.keySet();
					Iterator iterator = set.iterator();
					while (iterator.hasNext()) {
						String zsid_h = StringHelper.formatObject(iterator.next());
						String zsid = map_zsid.get(zsid_h);
						BDCS_ZS_GZ zs_h=getCommonDao().get(BDCS_ZS_GZ.class, zsid_h);
						if(zs_h!=null){
							BDCS_ZS_XZ zs_xz=new BDCS_ZS_XZ();
							ObjectHelper.copyObject(zs_h, zs_xz);
							zs_xz.setBDCQZH(null);
							zs_xz.setCFDAGH(null);
							zs_xz.setId(zsid);
							zs_xz.setZSBH(null);
							zs_xz.setZSDATA(null);
							getCommonDao().save(zs_xz);
							BDCS_ZS_LS zs_ls=new BDCS_ZS_LS();
							ObjectHelper.copyObject(zs_xz, zs_ls);
							getCommonDao().save(zs_ls);
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
		return super.getUnitList();
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
		return super.exportXML(path, actinstID);
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
				List<RightsHolder> bdcqlrs = RightsHolderTools.loadRightsHolders(ConstValue.DJDYLY.GZ, djdy.getDJDYID(), getXMBH());
				MessageExport msg = super.getShareMsgTools().GetMsg(bdcdy, bdcql, bdcfsql, bdcqlrs, bljc, xmxx);
				super.getShareMsgTools().SendMsg(msg, idjdy + 1, djdy.getBDCDYLX(), xmxx);
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
		RealUnit unit=UnitTools.loadUnit(dylx, DJDYLY.GZ, bdcdyid);
		if(unit!=null){
			LandAttach _landAttach = (LandAttach) unit;
			UnitTools.deleteUnit(BDCDYLX.SHYQZD, DJDYLY.GZ, _landAttach.getZDBDCDYID());
			updateDCDYStatus(BDCDYLX.SHYQZD.Value,_landAttach.getZDBDCDYID());
		}
		UnitTools.deleteUnit(dylx, DJDYLY.GZ, bdcdyid);
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

}
