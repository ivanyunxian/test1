package com.supermap.realestate.registration.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.supermap.realestate.registration.ViewClass.ProjectInfo;
import com.supermap.realestate.registration.ViewClass.UnitTree;
import com.supermap.realestate.registration.constraint.ConstraintCheck;
import com.supermap.realestate.registration.factorys.HandlerFactory;
import com.supermap.realestate.registration.handler.DJHandler;
import com.supermap.realestate.registration.handlerImpl.DYQ_CSDJHandler;
import com.supermap.realestate.registration.mapping.HandlerMapping.RegisterWorkFlow;
import com.supermap.realestate.registration.mapping.SelectorConfig;
import com.supermap.realestate.registration.model.BDCS_C_GZ;
import com.supermap.realestate.registration.model.BDCS_C_XZ;
import com.supermap.realestate.registration.model.BDCS_DJDY_GZ;
import com.supermap.realestate.registration.model.BDCS_DJDY_LS;
import com.supermap.realestate.registration.model.BDCS_DJDY_XZ;
import com.supermap.realestate.registration.model.BDCS_DYBG;
import com.supermap.realestate.registration.model.BDCS_FSQL_GZ;
import com.supermap.realestate.registration.model.BDCS_H_GZ;
import com.supermap.realestate.registration.model.BDCS_H_XZ;
import com.supermap.realestate.registration.model.BDCS_H_XZY;
import com.supermap.realestate.registration.model.BDCS_LJZ_GZ;
import com.supermap.realestate.registration.model.BDCS_QDZR_GZ;
import com.supermap.realestate.registration.model.BDCS_QLR_GZ;
import com.supermap.realestate.registration.model.BDCS_QLR_XZ;
import com.supermap.realestate.registration.model.BDCS_QL_GZ;
import com.supermap.realestate.registration.model.BDCS_QL_LS;
import com.supermap.realestate.registration.model.BDCS_QL_XZ;
import com.supermap.realestate.registration.model.BDCS_SHYQZD_GZ;
import com.supermap.realestate.registration.model.BDCS_SHYQZD_XZ;
import com.supermap.realestate.registration.model.BDCS_SLLM_GZ;
import com.supermap.realestate.registration.model.BDCS_SQR;
import com.supermap.realestate.registration.model.BDCS_SYQZD_GZ;
import com.supermap.realestate.registration.model.BDCS_XMXX;
import com.supermap.realestate.registration.model.BDCS_YHYDZB_GZ;
import com.supermap.realestate.registration.model.BDCS_YHZK_GZ;
import com.supermap.realestate.registration.model.BDCS_ZH_GZ;
import com.supermap.realestate.registration.model.BDCS_ZRZ_GZ;
import com.supermap.realestate.registration.model.BDCS_ZRZ_XZ;
import com.supermap.realestate.registration.model.BDCS_ZS_GZ;
import com.supermap.realestate.registration.model.DCS_DCXM;
import com.supermap.realestate.registration.model.DCS_H_GZ;
import com.supermap.realestate.registration.model.DCS_SHYQZD_GZ;
import com.supermap.realestate.registration.model.DCS_SLLM_GZ;
import com.supermap.realestate.registration.model.DCS_SYQZD_GZ;
import com.supermap.realestate.registration.model.DCS_ZH_GZ;
import com.supermap.realestate.registration.model.DCS_ZRZ_GZ;
import com.supermap.realestate.registration.model.WFD_MAPPING;
import com.supermap.realestate.registration.model.interfaces.Building;
import com.supermap.realestate.registration.model.interfaces.Forest;
import com.supermap.realestate.registration.model.interfaces.House;
import com.supermap.realestate.registration.model.interfaces.LandAttach;
import com.supermap.realestate.registration.model.interfaces.OwnerLand;
import com.supermap.realestate.registration.model.interfaces.RealUnit;
import com.supermap.realestate.registration.model.interfaces.Rights;
import com.supermap.realestate.registration.model.interfaces.SubRights;
import com.supermap.realestate.registration.model.interfaces.UseLand;
import com.supermap.realestate.registration.service.DBService;
import com.supermap.realestate.registration.service.DYService;
import com.supermap.realestate.registration.tools.NewLogTools;
import com.supermap.realestate.registration.tools.RightsTools;
import com.supermap.realestate.registration.tools.UnitTools;
import com.supermap.realestate.registration.util.ConfigHelper;
import com.supermap.realestate.registration.util.ConstHelper;
import com.supermap.realestate.registration.util.ConstValue;
import com.supermap.realestate.registration.util.ConstValue.BDCDYLX;
import com.supermap.realestate.registration.util.ConstValue.CZFS;
import com.supermap.realestate.registration.util.ConstValue.DJDYLY;
import com.supermap.realestate.registration.util.ConstValue.DJLX;
import com.supermap.realestate.registration.util.ConstValue.QLLX;
import com.supermap.realestate.registration.util.ConstValue.SF;
import com.supermap.realestate.registration.util.ConstValue.XZZT;
import com.supermap.realestate.registration.util.ConstValue.ZSBS;
import com.supermap.realestate.registration.util.DateUtil;
import com.supermap.realestate.registration.util.Global;
import com.supermap.realestate.registration.util.ObjectHelper;
import com.supermap.realestate.registration.util.ProjectHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.realestate.registration.util.YwLogUtil;
import com.supermap.wisdombusiness.core.SuperHelper;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import com.supermap.wisdombusiness.web.Message;
import com.supermap.wisdombusiness.web.ResultMessage;
import com.supermap.wisdombusiness.web.ui.Page;

@Service("dyService")
public class DYServiceImpl implements DYService {

	@Autowired
	private CommonDao baseCommonDao;
	
	@Autowired
	private ConstraintCheck constraint_check;
	
	/**
	 * 分页查询调查库使用权宗地
	 */
	@SuppressWarnings("unused")
	@Override
	public Message QueryDCKSHYQzd(String xmbh, int page, int rows, DCS_SHYQZD_GZ shyqzd) {
		StringBuilder hqlBuilder = new StringBuilder();
		hqlBuilder.append(" 1>0 ");
		if (!StringUtils.isEmpty(shyqzd.getZL())) {
			hqlBuilder.append(" and ZL LIKE '%").append(shyqzd.getZL()).append("%'");
		}
		if (!StringUtils.isEmpty(shyqzd.getZDDM())) {
			hqlBuilder.append(" and ZDDM LIKE '%").append(shyqzd.getZDDM()).append("%'");
		}
		if (!StringUtils.isEmpty(shyqzd.getBDCDYH())) {
			hqlBuilder.append(" and BDCDYH LIKE '%").append(shyqzd.getBDCDYH()).append("%'");
		}
		Map<String, Object> conditionParameter = new HashMap<String, Object>();
		Page p = baseCommonDao.getPageDataByHql(DCS_SHYQZD_GZ.class, hqlBuilder.toString(), page, rows);
		Message m = new Message();
		m.setTotal(p.getTotalCount());
		m.setRows(p.getResult());
		return m;
	}

	/**
	 * 分页查询调查库所有权宗地
	 */
	@Override
	public Message QueryDCKSYQzd(String xmbh, int page, int rows, DCS_SYQZD_GZ syqzd) {
		StringBuilder hqlBuilder = new StringBuilder();
		hqlBuilder.append(" 1>0 ");
		if (!StringUtils.isEmpty(syqzd.getZL())) {
			hqlBuilder.append(" and ZL LIKE '%").append(syqzd.getZL()).append("%'");
		}
		if (!StringUtils.isEmpty(syqzd.getZDDM())) {
			hqlBuilder.append(" and ZDDM LIKE '%").append(syqzd.getZDDM()).append("%'");
		}
		if (!StringUtils.isEmpty(syqzd.getBDCDYH())) {
			hqlBuilder.append(" and BDCDYH LIKE '%").append(syqzd.getBDCDYH()).append("%'");
		}
		if (!StringUtils.isEmpty(syqzd.getDCXMID())) {
			hqlBuilder.append(" and DCXMID LIKE '%").append(syqzd.getDCXMID()).append("%'");
		}
		Page p = baseCommonDao.getPageDataByHql(DCS_SYQZD_GZ.class, hqlBuilder.toString(), page, rows);
		Message m = new Message();
		m.setTotal(p.getTotalCount());
		m.setRows(p.getResult());
		return m;
	}

	/**
	 * 分页查询调查库宗海
	 */
	@Override
	public Message QueryDCKZH(String xmbh, int page, int rows, DCS_ZH_GZ zh) {
		StringBuilder hqlBuilder = new StringBuilder();
		hqlBuilder.append(" 1>0 ");
		if (!StringUtils.isEmpty(zh.getZL())) {
			hqlBuilder.append(" and ZL LIKE '%").append(zh.getZL()).append("%'");
		}
		if (!StringUtils.isEmpty(zh.getZHDM())) {
			hqlBuilder.append(" and ZHDM LIKE '%").append(zh.getZHDM()).append("%'");
		}
		if (!StringUtils.isEmpty(zh.getBDCDYH())) {
			hqlBuilder.append(" and BDCDYH LIKE '%").append(zh.getBDCDYH()).append("%'");
		}
		if (!StringUtils.isEmpty(zh.getDCXMID())) {
			hqlBuilder.append(" and DCXMID LIKE '%").append(zh.getDCXMID()).append("%'");
		}
		Page p = baseCommonDao.getPageDataByHql(DCS_ZH_GZ.class, hqlBuilder.toString(), page, rows);
		Message m = new Message();
		m.setTotal(p.getTotalCount());
		m.setRows(p.getResult());
		return m;
	}

	/**
	 * 通过不动产单元ID获取用海状况
	 * 
	 * @作者 海豹
	 * @创建时间 2015年6月25日下午6:19:35
	 * @param bdcdyid
	 * @param page
	 * @param rows
	 * @return
	 */
	@Override
	public Page GetPagedYHZK(String bdcdyid, Integer page, Integer rows) {
		StringBuilder builder = new StringBuilder();
		builder.append(" bdcdyid='").append(bdcdyid).append("'");
		Page p = baseCommonDao.getPageDataByHql(BDCS_YHZK_GZ.class, builder.toString(), page, rows);
		return p;
	}

	/**
	 * 通过不动产单元ID获取用海坐标
	 * 
	 * @作者 海豹
	 * @创建时间 2015年6月25日下午6:20:31
	 * @param bdcdyid
	 * @param page
	 * @param rows
	 * @return
	 */
	@Override
	public Page GetPagedYHYDZB(String bdcdyid, Integer page, Integer rows) {
		StringBuilder builder = new StringBuilder();
		builder.append(" bdcdyid='").append(bdcdyid).append("' order by xh");
		Page p = baseCommonDao.getPageDataByHql(BDCS_YHYDZB_GZ.class, builder.toString(), page, rows);
		return p;
	}

	/**
	 * 分页查询调查库房屋
	 */
	@Override
	public Message QueryDCKfws(String xmbh, int page, int rows, DCS_H_GZ h) {
		StringBuilder hqlBuilder = new StringBuilder();
		hqlBuilder.append("1>0");
		if (!StringUtils.isEmpty(h.getZL())) {
			hqlBuilder.append(" AND ZL LIKE '%").append(h.getZL()).append("%'");
		}
		if (!StringUtils.isEmpty(h.getBDCDYH())) {
			hqlBuilder.append(" AND BDCDYH LIKE '%").append(h.getBDCDYH()).append("%'");
		}
		hqlBuilder.append(" order by rownum");
		Page p = baseCommonDao.getPageDataByHql(DCS_H_GZ.class, hqlBuilder.toString(), page, rows);
		Message m = new Message();
		m.setTotal(p.getTotalCount());
		m.setRows(p.getResult());
		return m;
	}

	/**
	 * 分页查询调查库自然幢
	 */
	@Override
	public Message QueryDCKzrzs(String xmbh, int page, int rows, DCS_ZRZ_GZ zrz) {
		StringBuilder hqlBuilder = new StringBuilder();
		hqlBuilder.append("1>0");

		if (!StringUtils.isEmpty(zrz.getZL())) {
			hqlBuilder.append(" AND ZL LIKE '%").append(zrz.getZL()).append("%'");
		}
		if (!StringUtils.isEmpty(zrz.getBDCDYH())) {
			hqlBuilder.append(" AND BDCDYH LIKE '%").append(zrz.getBDCDYH()).append("%'");
		}
		// 过滤掉已经选择的 diaoliwei add 6-22
		hqlBuilder.append(" AND id NOT IN (SELECT id FROM BDCS_ZRZ_GZ)");
		Page p = baseCommonDao.getPageDataByHql(DCS_ZRZ_GZ.class, hqlBuilder.toString(), page, rows);
		Message m = new Message();
		m.setTotal(p.getTotalCount());
		m.setRows(p.getResult());
		return m;
	}

	/**
	 * 分页查询调查库林地
	 * 
	 * @作者 海豹
	 * @创建时间 2015年6月27日下午4:47:40
	 * @param xmbh
	 * @param page
	 * @param rows
	 * @param ld
	 * @return
	 */
	@Override
	public Message QueryDCKld(String xmbh, int page, int rows, DCS_SLLM_GZ ld) {
		StringBuilder hqlBuilder = new StringBuilder();
		hqlBuilder.append("1>0");

		if (!StringUtils.isEmpty(ld.getZL())) {
			hqlBuilder.append(" AND ZL LIKE '%").append(ld.getZL()).append("%'");
		}
		if (!StringUtils.isEmpty(ld.getBDCDYH())) {
			hqlBuilder.append(" AND BDCDYH LIKE '%").append(ld.getBDCDYH()).append("%'");
		}
		// hqlBuilder.append(" AND id NOT IN (SELECT id FROM BDCS_SLLM_GZ)");
		Page p = baseCommonDao.getPageDataByHql(DCS_SLLM_GZ.class, hqlBuilder.toString(), page, rows);
		Message m = new Message();
		m.setTotal(p.getTotalCount());
		m.setRows(p.getResult());
		return m;
	}

	/**
	 * 添加不动产单元
	 */
	@Transactional
	@Override
	public ResultMessage addBDCDY(String xmbh, String bdcdyid) {
		ResultMessage result = addBDCDYNoCheck(xmbh, bdcdyid);
		return result;
	}

	/**
	 * 更新户信息
	 * @param xmbh
	 */
	@Transactional
	@Override
	public void updateHouse(String xmbh){
		ProjectInfo projectInfo=ProjectHelper.GetPrjInfoByXMBH(xmbh);
		if(!StringHelper.isEmpty(projectInfo)){
		 String bdcdylx=projectInfo.getBdcdylx();
		 String qllx= projectInfo.getQllx();
		 BDCDYLX buildingbdcdylx=BDCDYLX.ZRZ;
		 if(BDCDYLX.H.Value.equals(bdcdylx) || BDCDYLX.YCH.Value.equals(bdcdylx)){
			 if(BDCDYLX.H.Value.equals(bdcdylx)){
				 buildingbdcdylx=BDCDYLX.ZRZ;
			 }else{
				 buildingbdcdylx=BDCDYLX.YCZRZ;
			 }
			 if(QLLX.GYJSYDSHYQ_FWSYQ.Value.equals(qllx) || QLLX.JTJSYDSYQ_FWSYQ.Value.equals(qllx)|| QLLX.ZJDSYQ_FWSYQ.Value.equals(qllx)){
//				String sql="xmbh='"+xmbh+"' and ly='"+DJDYLY.XZ.Value+"'";
				String sql="xmbh='"+xmbh+"'";
				List<BDCS_DJDY_GZ> djdys=baseCommonDao.getDataList(BDCS_DJDY_GZ.class, sql);
				if(!StringHelper.isEmpty(djdys) && djdys.size()>0){
					for(BDCS_DJDY_GZ djdy :djdys){
						if(DJDYLY.GZ.Value.equals(djdy.getLY())){
							String bdcdyid=	djdy.getBDCDYID();
						    RealUnit unit_gz=UnitTools.loadUnit(BDCDYLX.initFrom(bdcdylx), DJDYLY.GZ, bdcdyid);
						    if(!StringHelper.isEmpty(unit_gz)){
						        House h_gz=(House)unit_gz;
						        String nzdbdcdyid= h_gz.getNZDBDCDYID();
						        String nbdcdyh= h_gz.getNBDCDYH();
						        String bdcdyh=h_gz.getBDCDYH();
						        if(!StringHelper.isEmpty(nbdcdyh) && !(nbdcdyh.equals(bdcdyh))){
							    	h_gz.setBDCDYH(nbdcdyh);
							    	h_gz.setNBDCDYH("");
							    	h_gz.setZDBDCDYID(nzdbdcdyid);
							    	h_gz.setNZDBDCDYID("");
							    	baseCommonDao.update(h_gz);
							    	
							    	djdy.setBDCDYH(nbdcdyh);
							    	baseCommonDao.update(djdy);
							    	
							    	List<BDCS_DJDY_XZ> xz_djdys = baseCommonDao.getDataList(
							    			BDCS_DJDY_XZ.class, " DJDYID='" + djdy.getDJDYID() + "'");
							    	if (xz_djdys != null && xz_djdys.size() > 0) {
							    		BDCS_DJDY_XZ bdcs_djdy_xz = xz_djdys.get(0);
							    		bdcs_djdy_xz.setBDCDYH(nbdcdyh);
							    		baseCommonDao.update(bdcs_djdy_xz);
							    	}
							    	
							    	List<BDCS_DJDY_LS> ls_djdys = baseCommonDao.getDataList(
							    			BDCS_DJDY_LS.class, " DJDYID='" + djdy.getDJDYID() + "'");
							    	if (ls_djdys != null && ls_djdys.size() > 0) {
							    		BDCS_DJDY_LS bdcs_djdy_ls = ls_djdys.get(0);
							    		bdcs_djdy_ls.setBDCDYH(nbdcdyh);
							    		baseCommonDao.update(bdcs_djdy_ls);
							    	}
							    	
							    	List<BDCS_QL_GZ> qlList = baseCommonDao.getDataList(
							    			BDCS_QL_GZ.class, " xmbh = '" + xmbh + "' and DJDYID='"+djdy.getDJDYID()+ "'");
							    	if (qlList !=null && qlList.size()>0) {
							    		for (BDCS_QL_GZ ql : qlList) {
							    			ql.setBDCDYH(nbdcdyh);
							    			baseCommonDao.update(ql);
							    			List<BDCS_FSQL_GZ> fsqlList = baseCommonDao.getDataList(BDCS_FSQL_GZ.class, " qlid = '" + ql.getId() + "' ");
							    			if (fsqlList !=null && fsqlList.size()>0) {					
							    				for (BDCS_FSQL_GZ fsql : fsqlList) {
							    					fsql.setBDCDYH(nbdcdyh);
							    					baseCommonDao.update(fsql);
							    				}
							    			}
							    			List<BDCS_QDZR_GZ> qdzrs = baseCommonDao.getDataList(BDCS_QDZR_GZ.class, " qlid = '" + ql.getId() + "' ");
							    			if (qdzrs != null && qdzrs.size() > 0) {
							    				for (int iqdzr = 0; iqdzr < qdzrs.size(); iqdzr++) {
							    					BDCS_QDZR_GZ bdcs_qdzr_gz = qdzrs.get(iqdzr);
							    					if (bdcs_qdzr_gz != null) {
							    						bdcs_qdzr_gz.setBDCDYH(nbdcdyh);
							    						baseCommonDao.update(bdcs_qdzr_gz);
							    					}
							    				}
							    			}
							    		}
							    	}
						        }
									
						        String zrzbdcdyid=h_gz.getZRZBDCDYID();
						        if(!StringHelper.isEmpty(zrzbdcdyid)){
						        	RealUnit unit_zrz_xz=UnitTools.loadUnit(buildingbdcdylx, DJDYLY.XZ, zrzbdcdyid);
						        	if(unit_zrz_xz!=null){
						        		Building building_xz=(Building)unit_zrz_xz;
						        		String nbdcdyh_zrz=building_xz.getNBDCDYH();
						        		String bdcdyh_zrz=building_xz.getBDCDYH();
						        		String nzdbdcdyid_zrz=building_xz.getNZDBDCDYID();
						        		if(!StringHelper.isEmpty(nbdcdyh_zrz)&& !(nbdcdyh_zrz.equals(bdcdyh_zrz))){
						        			building_xz.setBDCDYH(nbdcdyh_zrz);
						        			building_xz.setNBDCDYH("");
						        			building_xz.setZDBDCDYID(nzdbdcdyid_zrz);
						        			building_xz.setNZDBDCDYID("");
						        			baseCommonDao.update(building_xz);
						        			RealUnit unit_zrz_ls=UnitTools.loadUnit(buildingbdcdylx, DJDYLY.LS, zrzbdcdyid);
						        			if(unit_zrz_xz!=null){
						        				Building building_ls=(Building)unit_zrz_ls;
						        				building_ls.setBDCDYH(nbdcdyh_zrz);
						        				building_ls.setNBDCDYH("");
						        				building_ls.setZDBDCDYID(nzdbdcdyid_zrz);
						        				building_ls.setNZDBDCDYID("");
							        			baseCommonDao.update(building_ls);
						        			}
						        		}
						        	}
						        }
						    }
						}else{
							String bdcdyid=	djdy.getBDCDYID();
						    RealUnit unit_xz=UnitTools.loadUnit(BDCDYLX.initFrom(bdcdylx), DJDYLY.XZ, bdcdyid);
						    if(!StringHelper.isEmpty(unit_xz)){
						        House h_xz=(House)unit_xz;
						        String nzdbdcdyid= h_xz.getNZDBDCDYID();
						        String nbdcdyh= h_xz.getNBDCDYH();
						        String bdcdyh=h_xz.getBDCDYH();
						        if(!StringHelper.isEmpty(nbdcdyh) && !(nbdcdyh.equals(bdcdyh))){
						        	RealUnit desUnit=UnitTools.copyUnit(unit_xz, BDCDYLX.initFrom(bdcdylx), DJDYLY.LS);
							    	String id=SuperHelper.GeneratePrimaryKey();
							    	desUnit.setId(id);
							    	baseCommonDao.save(desUnit);
							    	h_xz.setBDCDYH(nbdcdyh);
							    	h_xz.setNBDCDYH("");
							    	h_xz.setZDBDCDYID(nzdbdcdyid);
							    	h_xz.setNZDBDCDYID("");
							    	baseCommonDao.update(h_xz);
							    	RealUnit unit_ls=UnitTools.loadUnit(BDCDYLX.initFrom(bdcdylx), DJDYLY.LS, bdcdyid);
							    	if(!StringHelper.isEmpty(unit_ls)){
							    		House h_ls=(House)unit_ls;
							    		h_ls.setBDCDYH(nbdcdyh);
							    		h_ls.setNBDCDYH("");
							    		h_ls.setZDBDCDYID(nzdbdcdyid);
							    		h_ls.setNZDBDCDYID("");
							    		baseCommonDao.update(h_ls);
							    	}
							    	
							    	djdy.setBDCDYH(nbdcdyh);
							        baseCommonDao.update(djdy);
							        
							        List<BDCS_DJDY_XZ> xz_djdys = baseCommonDao.getDataList(
											BDCS_DJDY_XZ.class, " DJDYID='" + djdy.getDJDYID() + "'");
									if (xz_djdys != null && xz_djdys.size() > 0) {
										BDCS_DJDY_XZ bdcs_djdy_xz = xz_djdys.get(0);
										bdcs_djdy_xz.setBDCDYH(nbdcdyh);
										baseCommonDao.update(bdcs_djdy_xz);
									}
									
									List<BDCS_DJDY_LS> ls_djdys = baseCommonDao.getDataList(
											BDCS_DJDY_LS.class, " DJDYID='" + djdy.getDJDYID() + "'");
									if (ls_djdys != null && ls_djdys.size() > 0) {
										BDCS_DJDY_LS bdcs_djdy_ls = ls_djdys.get(0);
										bdcs_djdy_ls.setBDCDYH(nbdcdyh);
										baseCommonDao.update(bdcs_djdy_ls);
									}
							        
							        List<BDCS_QL_GZ> qlList = baseCommonDao.getDataList(
											BDCS_QL_GZ.class, " xmbh = '" + xmbh + "' and DJDYID='"+djdy.getDJDYID()+ "'");
									if (qlList !=null && qlList.size()>0) {
										for (BDCS_QL_GZ ql : qlList) {
											ql.setBDCDYH(nbdcdyh);
											baseCommonDao.update(ql);
											List<BDCS_FSQL_GZ> fsqlList = baseCommonDao.getDataList(BDCS_FSQL_GZ.class, " qlid = '" + ql.getId() + "' ");
											if (fsqlList !=null && fsqlList.size()>0) {					
												for (BDCS_FSQL_GZ fsql : fsqlList) {
													fsql.setBDCDYH(nbdcdyh);
													baseCommonDao.update(fsql);
												}
											}
											List<BDCS_QDZR_GZ> qdzrs = baseCommonDao.getDataList(BDCS_QDZR_GZ.class, " qlid = '" + ql.getId() + "' ");
											if (qdzrs != null && qdzrs.size() > 0) {
												for (int iqdzr = 0; iqdzr < qdzrs.size(); iqdzr++) {
													BDCS_QDZR_GZ bdcs_qdzr_gz = qdzrs.get(iqdzr);
													if (bdcs_qdzr_gz != null) {
														bdcs_qdzr_gz.setBDCDYH(nbdcdyh);
														baseCommonDao.update(bdcs_qdzr_gz);
													}
												}
											}
										}
									}
							    	
							    	BDCS_DYBG bdcs_dybg = new BDCS_DYBG();
									bdcs_dybg.setCreateTime(new Date());
									bdcs_dybg.setLBDCDYID(id);
									bdcs_dybg.setLDJDYID(djdy.getDJDYID());
									bdcs_dybg.setModifyTime(new Date());
									bdcs_dybg.setXBDCDYID(bdcdyid);
									bdcs_dybg.setXDJDYID(djdy.getDJDYID());
									bdcs_dybg.setXMBH(xmbh);
									baseCommonDao.save(bdcs_dybg);
									
									
									
						        }
						        String zrzbdcdyid=h_xz.getZRZBDCDYID();
						        if(!StringHelper.isEmpty(zrzbdcdyid)){
						        	RealUnit unit_zrz_xz=UnitTools.loadUnit(buildingbdcdylx, DJDYLY.XZ, zrzbdcdyid);
						        	if(unit_zrz_xz!=null){
						        		Building building_xz=(Building)unit_zrz_xz;
						        		String nbdcdyh_zrz=building_xz.getNBDCDYH();
						        		String bdcdyh_zrz=building_xz.getBDCDYH();
						        		String nzdbdcdyid_zrz=building_xz.getNZDBDCDYID();
						        		if(!StringHelper.isEmpty(nbdcdyh_zrz)&& !(nbdcdyh_zrz.equals(bdcdyh_zrz))){
						        			building_xz.setBDCDYH(nbdcdyh_zrz);
						        			building_xz.setNBDCDYH("");
						        			building_xz.setZDBDCDYID(nzdbdcdyid_zrz);
						        			building_xz.setNZDBDCDYID("");
						        			baseCommonDao.update(building_xz);
						        			RealUnit unit_zrz_ls=UnitTools.loadUnit(buildingbdcdylx, DJDYLY.LS, zrzbdcdyid);
						        			if(unit_zrz_ls!=null){
						        				Building building_ls=(Building)unit_zrz_ls;
						        				building_ls.setBDCDYH(nbdcdyh_zrz);
						        				building_ls.setNBDCDYH("");
						        				building_ls.setZDBDCDYID(nzdbdcdyid_zrz);
						        				building_ls.setNZDBDCDYID("");
							        			baseCommonDao.update(building_ls);
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
	/**
	 * 添加不动产单元
	 */
	@Transactional
	@Override
	public ResultMessage addBDCDYNoCheck(String xmbh, String bdcdyid) {
		ResultMessage result = new ResultMessage();
		DJHandler handler = HandlerFactory.createDJHandler(xmbh);
		boolean bSuccess = false;
		if (handler != null) {
			if (!StringHelper.isEmpty(bdcdyid)) {
				//根据选择单元的id，按照不动产单元ID查询重复，如果有重复则提示出来，不进行添加了，
				//此处可以选择FilterRepeatByBDCDYID方法按照所选择的id关联的BDCDYID过滤重复id和已添加的id
				//此处可以选择FilterRepeatByBDCDYIDORQLID方法按照所选择的id(返回的的为bdcdyid则通过bdcdyid过滤；返回的为qlid，则通过qlid)过滤重复id和已添加的id
				if(ValidateRepeat(bdcdyid,xmbh)){
					result.setSuccess("false");
					result.setMsg("不可以添加重复单元！");
					return result;
				}
				if(handler instanceof DYQ_CSDJHandler){
					// 拷贝不动产单元，生成登记单元记录
					bSuccess = handler.addBDCDY(bdcdyid);
				}else{
					String[] ids = bdcdyid.split(",");
					if (ids != null && ids.length > 0) {
						JSONObject jsondys=NewLogTools.getJSONByXMBH(xmbh);
						jsondys.put("OperateType", "添加单元");
					    int temp =0;
						for (String id : ids) {
							if (StringHelper.isEmpty(id))
								continue;
							JSONObject jsondy=new JSONObject();
							temp++;
							// 拷贝不动产单元，生成登记单元记录
							bSuccess = handler.addBDCDY(id);
							jsondy.put("ID", id);
							jsondy.put("msg", bSuccess);
							// 把权利人拷贝过来放到申请人里边
							if (("false").equals(bSuccess)) {
								break;
							}
							jsondys.put("序号:("+temp+")", jsondy);
						}
						if(temp !=0){
							NewLogTools.saveLog(jsondys.toString(), xmbh, "1", "添加单元");
						}
					}
				}
				String czfs = "";
				String sfhbzs = "";
				String djlx = "";
				String xmbhcond = ProjectHelper.GetXMBHCondition(xmbh);
				//根据业务流程配置添加ql.fj/ql.djyy信息
				String defaultFJ = "",defaultDJYY = "";
				List<BDCS_XMXX> xmxx = baseCommonDao.getDataList(
						BDCS_XMXX.class, xmbhcond);
				if (xmxx != null && xmxx.size() > 0) {
					String workflowcode = ProjectHelper.getWorkflowCodeByProjectID(xmxx.get(0).getPROJECT_ID());
					List<WFD_MAPPING> listCode = baseCommonDao.getDataList(
							WFD_MAPPING.class, " WORKFLOWCODE= '"
									+ workflowcode + "' ");
					if (listCode != null && listCode.size() > 0) {
						czfs = listCode.get(0).getCZFS();
						sfhbzs = listCode.get(0).getSFHBZS();					
						defaultFJ = listCode.get(0).getDEFAULTFJ();
						defaultDJYY = listCode.get(0).getDEFAULTDJYY();						
					}
					djlx = xmxx.get(0).getDJLX();
				}
				List<BDCS_QL_GZ> qlList = baseCommonDao.getDataList(
						BDCS_QL_GZ.class, " xmbh = '" + xmbh + "' ");
				if ("1".equals(czfs)) {
					for (BDCS_QL_GZ ql : qlList) {
						ql.setCZFS(CZFS.GTCZ.Value);
						baseCommonDao.update(ql);
					}
				} else if ("2".equals(czfs)) {
					for (BDCS_QL_GZ ql : qlList) {
						ql.setCZFS(CZFS.FBCZ.Value);
						baseCommonDao.update(ql);
					}
				}
				
				//添加默认的附记和登记原因
				List<BDCS_FSQL_GZ> fsqlList = null;
				if ("400".equals(djlx)) {//注销登记将值写入fsql中的zsfj中
					fsqlList = baseCommonDao.getDataList(BDCS_FSQL_GZ.class, " xmbh = '" + xmbh + "' ");
				}
				
				if (!StringHelper.isEmpty(defaultFJ)) {
					if ("400".equals(djlx)) {//注销登记将值写入fsql中的zxfj中						
						for (BDCS_FSQL_GZ fsql : fsqlList) {
							fsql.setZXFJ(defaultFJ);
							baseCommonDao.update(fsql);
						}
					}else{
						for (BDCS_QL_GZ ql : qlList) {
							ql.setFJ(defaultFJ);;
							baseCommonDao.update(ql);
						}
					}
				}
				if (!StringHelper.isEmpty(defaultDJYY)) {
					if ("400".equals(djlx)) {//注销登记将值写入fsql中的zxdyyy中						
						for (BDCS_FSQL_GZ fsql : fsqlList) {
							fsql.setZXDYYY(defaultDJYY);
							baseCommonDao.update(fsql);
						}
					}else{
						for (BDCS_QL_GZ ql : qlList) {					
							ql.setDJYY(defaultDJYY);
							baseCommonDao.update(ql);
						}
					}
				}
				
				//李堃，手工选单元的，原casenum要清空一下，否则共享推送会有影响
//				setCasenumNull(xmbh,bdcdyid);
				
				// 登记类型
				RegisterWorkFlow flow = HandlerFactory.getWorkflow(xmxx.get(0).getPROJECT_ID());
				if ((BDCDYLX.H.Value.equals(flow.getUnittype()) || BDCDYLX.YCH.Value.equals(flow.getUnittype())|| BDCDYLX.SHYQZD.Value.equals(flow.getUnittype()))
						&& !DJLX.YYDJ.Value.equals(flow.getDjlx())) {
					if ("1".equals(sfhbzs)) {
						xmxx.get(0).setSFHBZS(SF.YES.Value);
						baseCommonDao.update(xmxx.get(0));
					} else if ("2".equals(sfhbzs)) {
						xmxx.get(0).setSFHBZS(SF.NO.Value);
						baseCommonDao.update(xmxx.get(0));
					}
				}else {
					xmxx.get(0).setSFHBZS(SF.NO.Value);
					baseCommonDao.update(xmxx.get(0));
				}
				baseCommonDao.flush();
			}
		}
		if (bSuccess) {
			//添加单元之后维护ql中的bdcdyid
			String fulSql = " SELECT QL.QLID,MAX(GZDY.BDCDYID) AS GZBDCDYID,MAX(LSDY.BDCDYID) AS LSBDCDYID "
					+ " FROM BDCK.BDCS_QL_GZ QL "
					+ " LEFT JOIN BDCK.BDCS_DJDY_GZ GZDY ON GZDY.DJDYID=QL.DJDYID "
					+ " LEFT JOIN BDCK.BDCS_DJDY_LS LSDY ON LSDY.DJDYID=QL.DJDYID "
					+ " WHERE QL.XMBH=GZDY.XMBH AND QL.XMBH='"+xmbh+"' GROUP BY QL.QLID";
			List<Map> map = baseCommonDao.getDataListByFullSql(fulSql);
			if(map!=null){
				for (Map m : map) {
					String qlid = StringHelper.formatObject(m.get("QLID"));
					String lsbdcdyid = StringHelper.formatObject(m.get("LSBDCDYID"));
					if(!StringHelper.isEmpty(lsbdcdyid)){
						BDCS_QL_GZ ql = baseCommonDao.get(BDCS_QL_GZ.class, qlid);
						ql.setBDCDYID(lsbdcdyid);
						baseCommonDao.update(ql);
						
					}else{
						String gzbdcdyid = StringHelper.formatObject(m.get("GZBDCDYID"));
						BDCS_QL_GZ ql = baseCommonDao.get(BDCS_QL_GZ.class, qlid);
						ql.setBDCDYID(gzbdcdyid);
						baseCommonDao.update(ql);
					}
				}
			}
			updateHouse(xmbh);
			baseCommonDao.flush();
			result.setSuccess(StringHelper.formatObject(bSuccess));
			if (StringUtils.isEmpty(handler.getError()))
				result.setMsg("成功");
			else
				result.setMsg(handler.getError());
		} else {
			result.setSuccess(StringHelper.formatObject(bSuccess));
			result.setMsg(handler.getError());
		}
		return result;
	}
	
	/**
	 * 验证是否有已添加单元，或选择单元中是否有重复，如果有重复则返回true，否则返回false
	 * @author 俞学斌
	 * @param ids
	 * @param xmbh
	 * @return
	 */
	private boolean ValidateRepeat(String ids,String xmbh){
		boolean bresult=false;
		// 项目信息ProjectInfo
		ProjectInfo info = ProjectHelper.GetPrjInfoByXMBH(xmbh);
		// 选择器
		SelectorConfig config = HandlerFactory.getSelectorByName(info.getSelectorname());
		// 选择器中配置的返回字段名
		String fieldname = config.getIdfieldname().toUpperCase();
		// 操作类名称
		String HandlerName = HandlerFactory.getWorkflow(info.getProject_id()).getHandlername();
	   
		List<String> listBDCDYID=new ArrayList<String>();
		// 如果是多个ID，循环，并且找出BDCDYID
		if (!StringHelper.isEmpty(ids)) {
			String[] dyorqlids = ids.split(",");
			for (int ii = 0; ii < dyorqlids.length; ii++) {
				String id = dyorqlids[ii];
				if (!StringHelper.isEmpty(id)) {
					//为了满足多个单元一块注销进行修改
					 if(HandlerName.equals("DYZXDJHandler")){
						 List<BDCS_QL_GZ> qls = baseCommonDao.getDataList(BDCS_QL_GZ.class, "LYQLID='" + id+ "'");
					    	if(qls.size()>0){
					    		bresult=true;
					    		return bresult;//
					    	}else{
					    		return bresult;
					    	}
					    	
					    }
					/** 查找BDCDYID */
					if (fieldname.equals("QLID")) {
						Rights _rights = RightsTools.loadRights(DJDYLY.XZ, id);
						if (_rights != null) {
							List<BDCS_DJDY_XZ> djdys = baseCommonDao.getDataList(BDCS_DJDY_XZ.class, "DJDYID='" + _rights.getDJDYID() + "'");
							if (djdys != null && djdys.size() > 0) {
								if(listBDCDYID.contains(djdys.get(0).getBDCDYID())){
									bresult=true;
									return true;
								}else{
									listBDCDYID.add(djdys.get(0).getBDCDYID());
								}
							}
						}
					} else {
						if(listBDCDYID.contains(id)){
							bresult=true;
							return true;
						}else{
							listBDCDYID.add(id);
						}
					}
				}
			}
		}
		if(listBDCDYID!=null&&listBDCDYID.size()>0){
			for(String bdcdyid:listBDCDYID){
				if(HandlerName.contains("GZDJHandler")){//如果为更正登记
					List<BDCS_DJDY_XZ> xzdjdys= baseCommonDao.getDataList(BDCS_DJDY_XZ.class, " BDCDYID='"+bdcdyid+"'");
					if(xzdjdys!=null&&xzdjdys.size()>0){//如果单元来源为现状，则用登记单元ID过滤已添加
						List<BDCS_DJDY_GZ> gzdjdys= baseCommonDao.getDataList(BDCS_DJDY_GZ.class, "XMBH='"+xmbh+"' AND DJDYID='"+xzdjdys.get(0).getDJDYID()+"'");
						if(gzdjdys!=null&&gzdjdys.size()>0){
							bresult=true;
							return true;
						}
					}else{//如果单元来源不为现状，则用BDCDYID过滤已添加
						List<BDCS_DJDY_GZ> djdys= baseCommonDao.getDataList(BDCS_DJDY_GZ.class, "XMBH='"+xmbh+"' AND BDCDYID='"+bdcdyid+"'");
						if(djdys!=null&&djdys.size()>0){
							bresult=true;
							return true;
						}
					}
				}else{//如则用BDCDYID过滤已添加
					List<BDCS_DJDY_GZ> djdys= baseCommonDao.getDataList(BDCS_DJDY_GZ.class, "XMBH='"+xmbh+"' AND BDCDYID='"+bdcdyid+"'");
					if(djdys!=null&&djdys.size()>0){
						bresult=true;
						return true;
					}
				}
			}
		}
		return bresult;
	}
	
	/**
	 * 通过选择ids对应的bdcdyid过滤ids重复，同时过滤当前项目中已经添加的单元
	 * @author 俞学斌
	 * @param ids
	 * @param xmbh
	 * @return
	 */
	@SuppressWarnings("unused")
	private String FilterRepeatByBDCDYID(String ids,String xmbh){
		String new_ids="";
		// 项目信息ProjectInfo
		ProjectInfo info = ProjectHelper.GetPrjInfoByXMBH(xmbh);
		// 选择器
		SelectorConfig config = HandlerFactory.getSelectorByName(info.getSelectorname());
		// 选择器中配置的返回字段名
		String fieldname = config.getIdfieldname().toUpperCase();
		// 操作类名称
		String HandlerName = HandlerFactory.getWorkflow(info.getProject_id()).getHandlername();
		//当前选择中过滤重复
		HashMap<String,String> mapID=new HashMap<String, String>();
		// 如果是多个ID，循环，并且找出BDCDYID
		if (!StringHelper.isEmpty(ids)) {
			String[] dyorqlids = ids.split(",");
			for (int ii = 0; ii < dyorqlids.length; ii++) {
				String id = dyorqlids[ii];
				if (!StringHelper.isEmpty(id)) {

					/** 查找BDCDYID */
					if (fieldname.equals("QLID")) {
						Rights _rights = RightsTools.loadRights(DJDYLY.XZ, id);
						if (_rights != null) {
							List<BDCS_DJDY_XZ> djdys = baseCommonDao.getDataList(BDCS_DJDY_XZ.class, "DJDYID='" + _rights.getDJDYID() + "'");
							if (djdys != null && djdys.size() > 0) {
								if(!StringHelper.isEmpty(djdys.get(0).getBDCDYID())&&!mapID.containsKey(djdys.get(0).getBDCDYID())){
									mapID.put(djdys.get(0).getBDCDYID(), id);
								}
							}
						}
					} else {
						if(!StringHelper.isEmpty(id)&&!mapID.containsKey(id)){
							mapID.put(id, id);
						}
					}
				}
			}
		}
		
		List<String> listBDCDYID_new=new ArrayList<String>();
		for (Map.Entry<String, String> entry : mapID.entrySet()) {
			String key = entry.getKey().toString();
	    	String value = entry.getValue().toString();
	    	if(HandlerName.contains("GZDJHandler")){//如果为更正登记
				List<BDCS_DJDY_XZ> xzdjdys= baseCommonDao.getDataList(BDCS_DJDY_XZ.class, " BDCDYID='"+key+"'");
				if(xzdjdys!=null&&xzdjdys.size()>0){//如果单元来源为现状，则使用DJDYID过滤已添加
					List<BDCS_DJDY_GZ> gzdjdys= baseCommonDao.getDataList(BDCS_DJDY_GZ.class, "XMBH='"+xmbh+"' AND DJDYID='"+xzdjdys.get(0).getDJDYID()+"'");
					if(gzdjdys==null||gzdjdys.size()<=0){
						listBDCDYID_new.add(value);
					}
				}else{//如果单元来源不为现状，则使用BDCDYID过滤已添加
					List<BDCS_DJDY_GZ> djdys= baseCommonDao.getDataList(BDCS_DJDY_GZ.class, "XMBH='"+xmbh+"' AND BDCDYID='"+key+"'");
					if(djdys==null||djdys.size()<=0){
						listBDCDYID_new.add(value);
					}
				}
	    	}else{
	    		List<BDCS_DJDY_GZ> djdys= baseCommonDao.getDataList(BDCS_DJDY_GZ.class, "XMBH='"+xmbh+"' AND BDCDYID='"+key+"'");
				if(djdys==null||djdys.size()<=0){
					listBDCDYID_new.add(value);
				}
	    	}
		}
		new_ids=StringHelper.formatList(listBDCDYID_new, ",");
		return new_ids;
	}
	
	/**
	 * 通过选择ids对应的bdcdyid或者qlid过滤ids重复，同时过滤当前项目中已经添加的单元或权利
	 * @author 俞学斌
	 * @param ids
	 * @param xmbh
	 * @return
	 */
	@SuppressWarnings("unused")
	private String FilterRepeatByBDCDYIDORQLID(String ids,String xmbh){
		String new_ids="";
		// 项目信息ProjectInfo
		ProjectInfo info = ProjectHelper.GetPrjInfoByXMBH(xmbh);
		// 选择器
		SelectorConfig config = HandlerFactory.getSelectorByName(info.getSelectorname());
		// 选择器中配置的返回字段名
		String fieldname = config.getIdfieldname().toUpperCase();
		// 操作类名称
		String HandlerName = HandlerFactory.getWorkflow(info.getProject_id()).getHandlername();
		//当前选择中过滤重复
		List<String> listBDCDYID_new=new ArrayList<String>();
		// 如果是多个ID，循环，并且找出BDCDYID
		if (!StringHelper.isEmpty(ids)) {
			String[] dyorqlids = ids.split(",");
			for (int ii = 0; ii < dyorqlids.length; ii++) {
				String id = dyorqlids[ii];
				if (!StringHelper.isEmpty(id)) {

					/** 查找BDCDYID */
					if (fieldname.equals("QLID")) {
						if(!StringHelper.isEmpty(id)&&!listBDCDYID_new.contains(id)){
							List<BDCS_QL_GZ> qls= baseCommonDao.getDataList(BDCS_QL_GZ.class, "XMBH='"+xmbh+"' AND LYQLID='"+id+"'");
							if(qls==null||qls.size()<=0){
								listBDCDYID_new.add(id);
							}
						}
					} else {
						if(!StringHelper.isEmpty(id)&&!listBDCDYID_new.contains(id)){
							if(HandlerName.contains("GZDJHandler")){//如果为更正登记
								List<BDCS_DJDY_XZ> xzdjdys= baseCommonDao.getDataList(BDCS_DJDY_XZ.class, " BDCDYID='"+id+"'");
								if(xzdjdys!=null&&xzdjdys.size()>0){//如果单元来源为现状，则使用DJDYID过滤已添加
									List<BDCS_DJDY_GZ> gzdjdys= baseCommonDao.getDataList(BDCS_DJDY_GZ.class, "XMBH='"+xmbh+"' AND DJDYID='"+xzdjdys.get(0).getDJDYID()+"'");
									if(gzdjdys==null||gzdjdys.size()<=0){
										listBDCDYID_new.add(id);
									}
								}else{//如果单元来源不为现状，则使用BDCDYID过滤已添加
									List<BDCS_DJDY_GZ> djdys= baseCommonDao.getDataList(BDCS_DJDY_GZ.class, "XMBH='"+xmbh+"' AND BDCDYID='"+id+"'");
									if(djdys==null||djdys.size()<=0){
										listBDCDYID_new.add(id);
									}
								}
					    	}else{
					    		List<BDCS_DJDY_GZ> djdys= baseCommonDao.getDataList(BDCS_DJDY_GZ.class, "XMBH='"+xmbh+"' AND BDCDYID='"+id+"'");
								if(djdys==null||djdys.size()<=0){
									listBDCDYID_new.add(id);
								}
					    	}
						}
					}
				}
			}
		}
		new_ids=StringHelper.formatList(listBDCDYID_new, ",");
		return new_ids;
	}

	/**
	 * 移除不动产单元
	 */
	@Override
	public boolean removeBDCDY(String xmbh, String bdcdyid) {
		ResultMessage result = new ResultMessage();
		DJHandler handler = HandlerFactory.createDJHandler(xmbh);
		boolean bSuccess = false;
		if (handler != null) {
			bSuccess = handler.removeBDCDY(bdcdyid);
		}
		if (bSuccess) {
			result.setMsg("成功");
		}
		removeHouse(xmbh);
		return bSuccess;
	}

	/**
	 * 删除户信息
	 * @param xmbh
	 */
	public void removeHouse(String xmbh){
		ProjectInfo projectInfo=ProjectHelper.GetPrjInfoByXMBH(xmbh);
		if(!StringHelper.isEmpty(projectInfo)){
		 String bdcdylx=projectInfo.getBdcdylx();
		 String qllx= projectInfo.getQllx();
		 if(BDCDYLX.H.Value.equals(bdcdylx) || BDCDYLX.YCH.Value.equals(bdcdylx)){
			 if(QLLX.GYJSYDSHYQ_FWSYQ.Value.equals(qllx) || QLLX.JTJSYDSYQ_FWSYQ.Value.equals(qllx)|| QLLX.ZJDSYQ_FWSYQ.Value.equals(qllx)){
				String sql="xmbh='"+xmbh+"' and ly='"+DJDYLY.XZ.Value+"'";
				List<BDCS_DJDY_GZ> lstdjdy=baseCommonDao.getDataList(BDCS_DJDY_GZ.class, sql);
				if(lstdjdy !=null && lstdjdy.size()>0){
					for(BDCS_DJDY_GZ djdy:lstdjdy){
						String bdcdyid=djdy.getBDCDYID();
						List<BDCS_DYBG> lstdybg=baseCommonDao.getDataList(BDCS_DYBG.class, "XBDCDYID='"+bdcdyid+"' and xmbh='"+xmbh+"'");
						if(lstdybg !=null && lstdybg.size()>0){
							BDCS_DYBG dybg=lstdybg.get(0);
								String lbdcdyid=dybg.getLBDCDYID();
								 RealUnit unit_ls=UnitTools.loadUnit(BDCDYLX.initFrom(bdcdylx), DJDYLY.LS, lbdcdyid);
								 unit_ls.setId(bdcdyid);
								 House h_ls=(House) unit_ls;
								 UnitTools.deleteUnit(BDCDYLX.initFrom(bdcdylx), DJDYLY.LS, bdcdyid);
								 RealUnit unit_xz=UnitTools.loadUnit(BDCDYLX.initFrom(bdcdylx), DJDYLY.XZ, bdcdyid);
								 House h_xz=(House) unit_xz;
								 h_xz.setBDCDYH(h_ls.getBDCDYH());
								 h_xz.setNBDCDYH(h_ls.getNBDCDYH());
								 h_xz.setZDBDCDYID(h_ls.getZDBDCDYID());
								 h_xz.setNZDBDCDYID(h_ls.getNZDBDCDYID());
								 baseCommonDao.update(h_xz);
								 baseCommonDao.update(h_ls);
								 baseCommonDao.deleteEntity(dybg);
						}
					}
				}
			 }
		 }
		}
	}
	/**
	 * 获取项目对应的不动产单元列表树
	 */
	@Override
	public List<UnitTree> getDJDYS(String xmbh) {
		List<UnitTree> list = new ArrayList<UnitTree>();
		DJHandler handler = HandlerFactory.createDJHandler(xmbh);
		if (handler != null) {
			list = handler.getUnitTree();
			/***排序啦 by 赵梦帆 2017-05-18 08:40:24***/
			list = ObjectHelper.SortList(list);
		}
		return list;
	}

	/**
	 * 根据不动产单元ID和项目编号获取户信息
	 */
	@Override
	public BDCS_H_GZ GetFwInfo(String bdcdyid) {
		BDCS_H_GZ h = baseCommonDao.get(BDCS_H_GZ.class, bdcdyid);
		return h;
	}

	/**
	 * 根据不动产单元ID获取自然幢信息，不加项目编号
	 */
	@Override
	public BDCS_ZRZ_GZ GetZRZInfo(String bdcdyid) {
		return baseCommonDao.get(BDCS_ZRZ_GZ.class, bdcdyid);
	}

	/**
	 * 根据层ID获取层信息，不加项目编号
	 */
	@Override
	public BDCS_C_GZ GetCInfo(String cid) {
		return baseCommonDao.get(BDCS_C_GZ.class, cid);
	}

	/**
	 * 根据不动产单元ID获取使用权宗地信息，不加项目编号
	 */
	@Override
	public BDCS_SHYQZD_GZ GetSHYQZDInfo(String bdcdyid) {
		return baseCommonDao.get(BDCS_SHYQZD_GZ.class, bdcdyid);
	}

	/**
	 * 根据不动产单元ID获取森林林木信息
	 * 
	 * @作者 海豹
	 * @创建时间 2015年6月27日下午5:00:56
	 * @param bdcdyid
	 * @return
	 */
	@Override
	public BDCS_SLLM_GZ GetSHYQLDInfo(String bdcdyid) {
		return baseCommonDao.get(BDCS_SLLM_GZ.class, bdcdyid);
	}

	/**
	 * 根据不动产单元ID获取所有权宗地信息
	 */
	@Override
	public BDCS_SYQZD_GZ GetSYQZDInfo(String bdcdyid) {
		return baseCommonDao.get(BDCS_SYQZD_GZ.class, bdcdyid);
	}

	/**
	 * 根据不动产单元ID获取宗海信息
	 * 
	 * @作者 海豹
	 * @创建时间 2015年6月25日下午3_:09:38
	 * @param bdcdyid
	 * @return
	 */
	@Override
	public BDCS_ZH_GZ GetZHInfo(String bdcdyid) {
		return baseCommonDao.get(BDCS_ZH_GZ.class, bdcdyid);
	}

	/**
	 * 分页查询调查项目
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<DCS_DCXM> getPagedDcxm(int pageIndex, int pageSize, Map<String, Object> mapCondition, String bdcdylx) {

		StringBuilder conditionBuilder = new StringBuilder("1 = 1");
		for (Iterator<String> iterator = mapCondition.keySet().iterator(); iterator.hasNext();) {
			String key = iterator.next();
			Object value = mapCondition.get(key);
			if (StringUtils.hasLength(value.toString())) {
				conditionBuilder.append(" and " + key + " like '%" + value + "%' and ");
			}
		}
		String resultQuery = "";
		if (conditionBuilder.indexOf("and") > 0) {// 说明记录集合中有参数
			resultQuery += conditionBuilder.substring(0, conditionBuilder.length() - 4);
		}

		Page p = baseCommonDao.getPageDataByHql(DCS_DCXM.class, resultQuery, pageIndex, pageSize);
		List<DCS_DCXM> dcxmList = (List<DCS_DCXM>) p.getResult();
		for (int i = 0; i < dcxmList.size(); i++) {
			DCS_DCXM dcxm = dcxmList.get(i);
			int count = getCountTotal(dcxm, bdcdylx);
			dcxm.setCountTotal(count + "");
		}
		return dcxmList;
	}

	/**
	 * 获取数量
	 * 
	 * @作者 diaoliwei
	 * @创建时间 2015年7月2日下午9:43:37
	 * @param dcxm
	 * @param bdcdylx
	 * @return
	 */
	private int getCountTotal(DCS_DCXM dcxm, String bdcdylx) {
		int count = 0;
		StringBuilder totalSql = new StringBuilder();
		if (!StringUtils.isEmpty(bdcdylx)) {
			if (BDCDYLX.H.Value.equals(bdcdylx)) {
				totalSql.append(" FROM bdck.BDCS_H_GZ ");
			} else if (BDCDYLX.ZRZ.Value.equals(bdcdylx)) {
				totalSql.append(" FROM bdck.BDCS_ZRZ_GZ ");
			} else if (BDCDYLX.SHYQZD.Value.equals(bdcdylx)) {
				totalSql.append(" FROM bdck.BDCS_SHYQZD_GZ ");
			} else if (BDCDYLX.SYQZD.Value.equals(bdcdylx)) {
				totalSql.append(" FROM bdck.BDCS_SYQZD_GZ ");
			} else if (BDCDYLX.LD.Value.equals(bdcdylx)) {
				totalSql.append(" FROM bdck.BDCS_SLLM_GZ ");
			} else if (BDCDYLX.HY.Value.equals(bdcdylx)) {
				totalSql.append(" FROM bdck.BDCS_ZH_GZ ");
			}
		}

		if (totalSql.length() > 0) {
			totalSql.append(" WHERE 1=1 ");
			Map<String, String> paramMap = new HashMap<String, String>();
			if (null != dcxm && !StringUtils.isEmpty(dcxm.getId())) {
				totalSql.append(" and DCXMID =:DCXMID ");
				paramMap.put("DCXMID", dcxm.getId());
			}
			Long total = baseCommonDao.getCountByFullSql(totalSql.toString(), paramMap);
			count = total.intValue();// 查询记录个数
		}
		return count;
	}

	@Override
	public int getXMXXCountById(String dcxmid) {
		int count = 0;
		StringBuilder sqlBuilder = new StringBuilder(" FROM bdck.BDCS_DJDY_GZ WHERE 1=1 ");
		Map<String, String> paramMap = new HashMap<String, String>();
		if (!StringUtils.isEmpty(dcxmid)) {
			sqlBuilder.append(" AND XMBH=:XMBH ");
			paramMap.put("XMBH", dcxmid);
		}
		Long total = baseCommonDao.getCountByFullSql(sqlBuilder.toString(), paramMap);
		count = total.intValue();
		return count;
	}

	@Override
	public ResultMessage updateXmxx(String xmbh, String dcxmid) {
		ProjectInfo info = ProjectHelper.GetPrjInfoByXMBH(xmbh);
		String xmdylx = info.getBdcdylx();
		if (getXMXXCountById(xmbh) > 0) {// 已经有匹配数据，先删除已有信息， 再更新
			this.deleteRelatedInfos(xmbh, xmdylx);
		}

		ResultMessage msg = new ResultMessage();
		String error = null;
		DJHandler handler = HandlerFactory.createDJHandler(xmbh);
		if (handler != null) {
			msg = this.updateBdcdy(dcxmid, xmdylx, handler, error);
		}
		if (error == null || null == msg.getMsg()) {
			msg.setMsg("更新成功");
			msg.setSuccess("false");
			YwLogUtil.addYwLog("更新调查项目信息", ConstValue.SF.YES.Value,ConstValue.LOG.UPDATE);
		}
		return msg;
	}

	/**
	 * 更新相应的不动产单元信息
	 * 
	 * @作者 diaoliwei
	 * @创建时间 2015年7月13日下午2:33:38
	 * @param dcxmid
	 *            调查项目id
	 * @param xmdylx
	 *            项目单元类型
	 * @param handler
	 *            对应的handler
	 * @param error
	 *            错误信息
	 * @return
	 */
	private ResultMessage updateBdcdy(String dcxmid, String xmdylx, DJHandler handler, String error) {
		ResultMessage msg = new ResultMessage();
		StringBuilder hqlCondition = new StringBuilder();
		hqlCondition.append(" dCXMID ='").append(dcxmid).append("' ");
		String bdcdyid = "";
		if (xmdylx.equals(BDCDYLX.SHYQZD.Value)) { // 使用权宗地
			hqlCondition.append(" AND id NOT IN (select id FROM BDCS_SHYQZD_GZ WHERE DCXMID ='").append(dcxmid).append("') ");
			List<DCS_SHYQZD_GZ> list = baseCommonDao.getDataList(DCS_SHYQZD_GZ.class, hqlCondition.toString());
			for (int i = 0; i < list.size(); i++) {
				DCS_SHYQZD_GZ dcs_SHYQZD_GZ = list.get(i);
				if (i + 1 == list.size()) {
					bdcdyid += dcs_SHYQZD_GZ.getId();
				} else {
					bdcdyid += dcs_SHYQZD_GZ.getId() + ",";
				}
			}
			handler.addBDCDY(bdcdyid);
			if (null == list || 0 == list.size()) {
				msg.setMsg("该项目下可被登记数量为0");
				msg.setSuccess("false");
			}
		} else if (xmdylx.equals(BDCDYLX.H.Value)) { // 房屋（户）
			hqlCondition.append(" AND id NOT IN (select id FROM BDCS_H_GZ WHERE DCXMID ='").append(dcxmid).append("') ");
			List<DCS_H_GZ> list = baseCommonDao.getDataList(DCS_H_GZ.class, hqlCondition.toString());
			for (int i = 0; i < list.size(); i++) {
				DCS_H_GZ dcs_h_GZ = list.get(i);
				handler.addBDCDY(dcs_h_GZ.getId());
			}
			error = handler.getError();
			if (error != null) {
				msg.setMsg("找到相关宗地、相关自然幢的已登记,其他" + error);
				msg.setSuccess("false");
			}
			if (null != list || 0 == list.size()) {
				msg.setMsg("该项目下可被登记数量为0");
				msg.setSuccess("false");
			}
		} else if (xmdylx.equals(BDCDYLX.ZRZ.Value)) { // 自然幢
			hqlCondition.append(" AND id NOT IN (select id FROM BDCS_ZRZ_GZ WHERE DCXMID ='").append(dcxmid).append("') ");
			List<DCS_ZRZ_GZ> list = baseCommonDao.getDataList(DCS_ZRZ_GZ.class, hqlCondition.toString());
			for (int i = 0; i < list.size(); i++) {
				DCS_ZRZ_GZ dcs_zrz_GZ = list.get(i);
				handler.addBDCDY(dcs_zrz_GZ.getId());
			}
			error = handler.getError();
			if (error != null) {
				msg.setMsg("找到相关宗地的已登记,其他" + error);
				msg.setSuccess("false");
			}
			if (null != list || 0 == list.size()) {
				msg.setMsg("该项目下可被登记数量为0");
				msg.setSuccess("false");
			}
		} else if (xmdylx.equals(BDCDYLX.SYQZD.Value)) { // 所有权宗地
			hqlCondition.append(" AND id NOT IN (select id FROM BDCS_SYQZD_GZ WHERE DCXMID ='").append(dcxmid).append("') ");
			List<DCS_SYQZD_GZ> list = baseCommonDao.getDataList(DCS_SYQZD_GZ.class, hqlCondition.toString());
			for (int i = 0; i < list.size(); i++) {
				DCS_SYQZD_GZ dcs_syqzd_GZ = list.get(i);
				if (i + 1 == list.size()) {
					bdcdyid += dcs_syqzd_GZ.getId();
				} else {
					bdcdyid += dcs_syqzd_GZ.getId() + ",";
				}
			}
			handler.addBDCDY(bdcdyid);
			if (null != list || 0 == list.size()) {
				msg.setMsg("该项目下可被登记数量为0");
				msg.setSuccess("false");
			}
		} else if (xmdylx.equals(BDCDYLX.HY.Value)) { // 海域
			hqlCondition.append(" AND id NOT IN (select id FROM BDCS_ZH_GZ WHERE DCXMID ='").append(dcxmid).append("') ");
			List<DCS_ZH_GZ> list = baseCommonDao.getDataList(DCS_ZH_GZ.class, hqlCondition.toString());
			for (int i = 0; i < list.size(); i++) {
				DCS_ZH_GZ dcs_zh_GZ = list.get(i);
				if (i + 1 == list.size()) {
					bdcdyid += dcs_zh_GZ.getId();
				} else {
					bdcdyid += dcs_zh_GZ.getId() + ",";
				}
			}
			handler.addBDCDY(bdcdyid);
			if (null != list || 0 == list.size()) {
				msg.setMsg("该项目下可被登记数量为0");
				msg.setSuccess("false");
			}
		} else if (xmdylx.equals(BDCDYLX.LD.Value)) { // 林地
			hqlCondition.append(" AND id NOT IN (select id FROM BDCS_SLLM_GZ WHERE DCXMID ='").append(dcxmid).append("') ");
			List<DCS_SLLM_GZ> list = baseCommonDao.getDataList(DCS_SLLM_GZ.class, hqlCondition.toString());
			for (int i = 0; i < list.size(); i++) {
				DCS_SLLM_GZ dcs_sllm_GZ = list.get(i);
				handler.addBDCDY(dcs_sllm_GZ.getId());
			}
			error = handler.getError();
			if (error != null) {
				msg.setMsg("找到相关宗地的已登记,其他" + error);
				msg.setSuccess("false");
			}
			if (null != list || 0 == list.size()) {
				msg.setMsg("该项目下可被登记数量为0");
				msg.setSuccess("false");
			}
		}
		return msg;
	}

	/**
	 * 删除登记库中已经匹配到的项目信息
	 * 
	 * @作者 李桐
	 * @创建时间 2015年7月13日下午2:42:53
	 * @param xmbh
	 * @param xmdylx
	 */
	private void deleteRelatedInfos(String xmbh, String xmdylx) {
		String hql = ProjectHelper.GetXMBHCondition(xmbh);
		// 1. 清除单元的信息
		if (xmdylx.equals(BDCDYLX.SHYQZD.Value)) { // 使用权宗地
			// 清除BDCS_SHYQZD_GZ表中信息
			baseCommonDao.deleteEntitysByHql(BDCS_SHYQZD_GZ.class, hql);
		} else if (xmdylx.equals(BDCDYLX.H.Value)) { // 房屋（户）
			baseCommonDao.deleteEntitysByHql(BDCS_H_GZ.class, hql); // 删除BDCS_H_GZ表中信息
			baseCommonDao.deleteEntitysByHql(BDCS_ZRZ_GZ.class, hql); // 删除BDCS_ZRZ_GZ表中信息
			baseCommonDao.deleteEntitysByHql(BDCS_SHYQZD_GZ.class, hql); // 清除BDCS_SHYQZD_GZ表中信息
		} else if (xmdylx.equals(BDCDYLX.ZRZ.Value)) { // 自然幢
			baseCommonDao.deleteEntitysByHql(BDCS_ZRZ_GZ.class, hql); // 删除BDCS_ZRZ_GZ表中信息
			baseCommonDao.deleteEntitysByHql(BDCS_SHYQZD_GZ.class, hql); // 清除BDCS_SHYQZD_GZ表中信息
		} else if (xmdylx.equals(BDCDYLX.SYQZD.Value)) { // 所有权宗地
			baseCommonDao.deleteEntitysByHql(BDCS_SYQZD_GZ.class, hql); // 清除BDCS_SYQZD_GZ表中的信息
		} else if (xmdylx.equals(BDCDYLX.HY.Value)) { // 海域
			baseCommonDao.deleteEntitysByHql(BDCS_ZH_GZ.class, hql); // 清除BDCS_ZH_GZ表中的数据
		} else if (xmdylx.equals(BDCDYLX.LD.Value)) { // 林地
			baseCommonDao.deleteEntitysByHql(BDCS_SLLM_GZ.class, hql); // 清除BDCS_SLLM_GZ表中的数据
		}

		// 2. 清除XMXX表中关联的DCXMID
		BDCS_XMXX xmxxObj = baseCommonDao.get(BDCS_XMXX.class, xmbh);
		xmxxObj.setDCXMID(null);
		baseCommonDao.update(xmxxObj);

		// 3.删除BDCS_DJDY_GZ表中信息
		baseCommonDao.deleteEntitysByHql(BDCS_DJDY_GZ.class, hql);
		// 4.删除BDCS_QL_GZ表中信息
		baseCommonDao.deleteEntitysByHql(BDCS_QL_GZ.class, hql);
		// 5.删除BDCS_QLFS_GZ表中信息
		baseCommonDao.deleteEntitysByHql(BDCS_FSQL_GZ.class, hql);

		// 6.删除BDCS_ZS_GZ表中信息
		baseCommonDao.deleteEntitysByHql(BDCS_ZS_GZ.class, hql);
		// 7.删除BDCS_QDZR_GZ表中信息
		baseCommonDao.deleteEntitysByHql(BDCS_QDZR_GZ.class, hql);
		// 删除BDCS_SQR表中信息
		baseCommonDao.deleteEntitysByHql(BDCS_SQR.class, hql);

		// 删除BDCS_QLR_GZ表中信息
		baseCommonDao.deleteEntitysByHql(BDCS_QLR_GZ.class, hql);
		// 删除BDCS_LJZ_GZ表中信息
		baseCommonDao.deleteEntitysByHql(BDCS_LJZ_GZ.class, hql);
		// 删除BDCS_C_GZ表中信息
		baseCommonDao.deleteEntitysByHql(BDCS_C_GZ.class, hql);

		baseCommonDao.flush();
	}

	/**
	 * 获取(现状)使用权宗地信息
	 */
	@Override
	public BDCS_SHYQZD_XZ GetXZSHYQZDInfo(String bdcdyid) {
		return baseCommonDao.get(BDCS_SHYQZD_XZ.class, bdcdyid);
	}

	/**
	 * 获取(现状)自然幢信息
	 */
	@Override
	public BDCS_ZRZ_XZ GetXZZRZInfo(String bdcdyid) {
		return baseCommonDao.get(BDCS_ZRZ_XZ.class, bdcdyid);
	}

	/**
	 * 获取(现状)层信息
	 */
	@Override
	public BDCS_C_XZ GetXZCInfo(String cid) {
		return baseCommonDao.get(BDCS_C_XZ.class, cid);
	}

	/**
	 * 获取(现状)房屋信息
	 */
	@Override
	public BDCS_H_XZ GetXZFwInfo(String bdcdyid) {
		return baseCommonDao.get(BDCS_H_XZ.class, bdcdyid);
	}

	/**
	 * 获取（现状）预测房屋信息
	 * 
	 * @作者 海豹
	 * @创建时间 2015年7月10日下午8:17:28
	 * @param bdcdyid
	 * @return
	 */
	@Override
	public BDCS_H_XZY GetXzyFwInfo(String bdcdyid) {
		return baseCommonDao.get(BDCS_H_XZY.class, bdcdyid);
	}

	/**
	 * 获取转移房屋信息
	 * 
	 * @作者 rongxianfeng
	 * @param page
	 * @param rows
	 * @param StrZL
	 *            坐落
	 * @param StrQLR
	 *            权利人
	 * @param Strbdcqzh
	 *            不动产权证号
	 * @param StrBDCDYH
	 *            房屋编号
	 * @return
	 */
	@Override
	public Message GetXZZY_fw_Info(int page, int rows, String StrZL, String StrQLR, String Strbdcqzh, String Strbdcdyh, String Strbdcfwbh) {
		StringBuilder hqlBuilder = new StringBuilder();
		Map<String, String> MapValue = new HashMap<String, String>();
		hqlBuilder.append(" 1>0 ");
		// 坐落
		if (!StringUtils.isEmpty(StrZL)) {
			// hqlBuilder.append(" and ZL LIKE '%").append(shyqzd.getZL())
			// .append("%'");
			hqlBuilder.append(" and ZL like :ZL");
			MapValue.put("ZL", "%" + StrZL + "%");
		}
		// 房屋编码
		if (!StringUtils.isEmpty(Strbdcfwbh)) {
			// hqlBuilder.append(" and ZL LIKE '%").append(shyqzd.getZL())
			// .append("%'");
			hqlBuilder.append(" and fwbm like :fwbm");
			MapValue.put("fwbm", "%" + Strbdcfwbh + "%");
		}

		// 不动单元号
		if (!StringUtils.isEmpty(Strbdcdyh)) {
			// hqlBuilder.append(" and ZL LIKE '%").append(shyqzd.getZL())
			// .append("%'");
			hqlBuilder.append(" and BDCDYH like :BDCDYH");
			MapValue.put("BDCDYH", "%" + Strbdcdyh + "%");
		}

		// 产权证号
		if (!StringUtils.isEmpty(Strbdcqzh)) {
			// hqlBuilder.append(" and bdcqzh LIKE '%").append(shyqzd.getZDDM())
			// .append("%'");
			String qllxarray = " ('1','2','3','4','5','6','7','8','9','10','11','12','13','14','15','16','17','18')";

			hqlBuilder.append(" and bdcdyh in  (select distinct BDCDYH from BDCS_QL_XZ  " + "where  BDCQZH like :bdcqzh and QLLX IN ").append(qllxarray).append(")");
			MapValue.put("bdcqzh", "%" + Strbdcqzh + "%");
		}
		// 权利人
		if (!StringUtils.isEmpty(StrQLR)) {
			// hqlBuilder.append(" and BDCDYH LIKE '%").append(shyqzd.getBDCDYH())
			// .append("%'");

			hqlBuilder.append(" and BDCDYH in  (select distinct z.BDCDYH  " + "From BDCS_QL_XZ as z,BDCS_QLR_XZ as b "
			// + "From BDCS_QL_XZ as z LEFT JOIN "
			// +
			// "BDCS_QLR_XZ as b   ON z.id=b.QLID AND b.QLRMC LIKE:qlrmc)");
					+ " where z.id = b.QLID and  " + " b.QLRMC like :qlrmc)");
			MapValue.put("qlrmc", "%" + StrQLR + "%");
		}

		// Page p = baseCommonDao.getPageDataByHql(BDCS_H_XZ.class,
		// hqlBuilder.toString(),MapValue, page, rows);
		Page p = baseCommonDao.getPageDataByHql(BDCS_H_XZ.class, hqlBuilder.toString(), MapValue, page, rows);

		Message m = new Message();
		m.setTotal(p.getTotalCount());
		m.setRows(p.getResult());
		return m;

	}

	/**
	 * 初始登记中一般抵押权（最高额抵押权）的在建工程抵押信息的获取按户登记信息
	 * 
	 * @作者 海豹
	 * @创建时间 2015年7月9日下午10:20:33
	 * @param page
	 *            当前页码
	 * @param rows
	 *            当前页的行数
	 * @param StrZL
	 *            坐落
	 * @param StrQLR
	 *            权利人
	 * @param Strbdcqzh
	 *            不动产权证号
	 * @param Strbdcdyh
	 *            不动产权单员号
	 * @param Strbdcfwbh
	 *            不动产房屋编码
	 * @return
	 */
	@Override
	public Message GetXZ_Hy_Info(int page, int rows, String StrZL, String StrQLR, String Strbdcqzh, String Strbdcdyh, String Strbdcfwbh) {
		StringBuilder hqlBuilder = new StringBuilder();
		Map<String, String> MapValue = new HashMap<String, String>();
		hqlBuilder.append(" 1>0 ");
		// 坐落
		if (!StringUtils.isEmpty(StrZL)) {
			hqlBuilder.append(" and ZL like :ZL");
			MapValue.put("ZL", "%" + StrZL + "%");
		}
		// 房屋编码
		if (!StringUtils.isEmpty(Strbdcfwbh)) {
			hqlBuilder.append(" and fwbm like :fwbm");
			MapValue.put("fwbm", "%" + Strbdcfwbh + "%");
		}

		// 不动单元号
		if (!StringUtils.isEmpty(Strbdcdyh)) {
			hqlBuilder.append(" and BDCDYH like :BDCDYH");
			MapValue.put("BDCDYH", "%" + Strbdcdyh + "%");
		}

		// 产权证号
		if (!StringUtils.isEmpty(Strbdcqzh)) {
			String qllxarray = " ('1','2','3','4','5','6','7','8','9','10','11','12','13','14','15','16','17','18')";

			hqlBuilder.append(" and bdcdyh in  (select distinct BDCDYH from BDCS_QL_XZ  " + "where  BDCQZH like :bdcqzh and QLLX IN ").append(qllxarray).append(")");
			MapValue.put("bdcqzh", "%" + Strbdcqzh + "%");
		}
		// 权利人
		if (!StringUtils.isEmpty(StrQLR)) {
			hqlBuilder.append(" and BDCDYH in  (select distinct z.BDCDYH  " + "From BDCS_QL_XZ as z,BDCS_QLR_XZ as b " + " where z.id = b.QLID and  " + " b.QLRMC like :qlrmc)");
			MapValue.put("qlrmc", "%" + StrQLR + "%");
		}
		Page p = baseCommonDao.getPageDataByHql(BDCS_H_XZY.class, hqlBuilder.toString(), MapValue, page, rows);

		Message m = new Message();
		m.setTotal(p.getTotalCount());
		m.setRows(p.getResult());
		return m;

	}

	/**
	 * 获取使用宗地信息列表信息
	 * 
	 * @作者 rongxianfeng
	 * @param page
	 * @param rows
	 * @param StrZL
	 *            坐落
	 * @param StrQLR
	 *            权利人
	 * @param Strbdcqzh
	 *            不动产权证号
	 * @param StrBDCDYH
	 *            不动产单元号
	 * @param Strbdczddm
	 *            不动产宗地代码
	 * @return
	 */
	@Override
	public Message GetXZZY_zd_Info(int page, int rows, String StrZL, String StrQLR, String Strbdcqzh, String StrBDCDYH, String Strbdczddm) {
		StringBuilder hqlBuilder = new StringBuilder();
		Map<String, String> MapValue = new HashMap<String, String>();
		hqlBuilder.append(" 1>0 ");

		// 坐落
		if (!StringUtils.isEmpty(StrZL)) {
			// hqlBuilder.append(" and ZL LIKE '%").append(shyqzd.getZL())
			// .append("%'");
			hqlBuilder.append(" and ZL like :ZL");
			MapValue.put("ZL", "%" + StrZL + "%");
		}
		// 宗地代码
		if (!StringUtils.isEmpty(Strbdczddm)) {
			// hqlBuilder.append(" and ZL LIKE '%").append(shyqzd.getZL())
			// .append("%'");
			hqlBuilder.append(" and zddm like :zddm");
			MapValue.put("zddm", "%" + Strbdczddm + "%");
		}
		// 不动单元号
		if (!StringUtils.isEmpty(StrBDCDYH)) {
			// hqlBuilder.append(" and ZL LIKE '%").append(shyqzd.getZL())
			// .append("%'");
			hqlBuilder.append(" and BDCDYH like :BDCDYH");
			MapValue.put("BDCDYH", "%" + StrBDCDYH + "%");
		}
		// 产权证号
		if (!StringUtils.isEmpty(Strbdcqzh)) {
			// hqlBuilder.append(" and bdcqzh LIKE '%").append(shyqzd.getZDDM())
			// .append("%'");
			String qllxarray = " ('1','2','3','4','5','6','7','8','9','10','11','12','13','14','15','16','17','18')";
			hqlBuilder.append(" and BDCDYH in  (select distinct BDCDYH from BDCS_QL_XZ  " + "where  BDCQZH like :bdcqzh AND QLLX IN ").append(qllxarray).append(")");
			MapValue.put("bdcqzh", "%" + Strbdcqzh + "%");
		}

		// 权利人
		if (!StringUtils.isEmpty(StrQLR)) {
			// hqlBuilder.append(" and BDCDYH LIKE '%").append(shyqzd.getBDCDYH())
			// .append("%'");
			hqlBuilder.append(" and BDCDYH in  (select distinct z.BDCDYH  " + "From BDCS_QL_XZ as z,BDCS_QLR_XZ as b "
			// + "From BDCS_QL_XZ as z LEFT JOIN "
			// +
			// "BDCS_QLR_XZ as b   ON z.id=b.QLID AND b.QLRMC LIKE:qlrmc)");
					+ " where z.id = b.QLID and  " + " b.QLRMC like :qlrmc)");
			MapValue.put("qlrmc", "%" + StrQLR + "%");
		}

		Page p = baseCommonDao.getPageDataByHql(BDCS_SHYQZD_XZ.class, hqlBuilder.toString(), MapValue, page, rows);

		Message m = new Message();
		m.setTotal(p.getTotalCount());
		m.setRows(p.getResult());
		return m;
	}

	private String GetXZTableNameByBDCDYLX(String bdcdylx) {
		for (BDCDYLX b : BDCDYLX.values()) {
			if (b.Value.equals(bdcdylx)) {
				return b.XZTableName;
			}
		}
		return "BDCS_SHYQZD_XZ";// 默认
	}

	static private Map<String, String> BDCDYLX_TABLE = new HashMap<String, String>();// 不动产单元类型对应的表格

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Message GetDYQInfo(String bdcdylx, Map<String, String> conditionParameter, int page, int rows) throws Exception {
		Message msg = new Message();
		if (BDCDYLX_TABLE.isEmpty()) {
			BDCDYLX_TABLE.put("01", "BDCS_SYQZD_XZ");
			BDCDYLX_TABLE.put("02", "BDCS_SHYQZD_XZ");
			BDCDYLX_TABLE.put("03", "BDCS_ZRZ_XZ");
			BDCDYLX_TABLE.put("031", "BDCS_H_XZ");
			BDCDYLX_TABLE.put("032", "BDCS_H_XZY");
			BDCDYLX_TABLE.put("04", "BDCS_ZH_XZ");
			BDCDYLX_TABLE.put("05", "BDCS_SLLM_XZ");
			BDCDYLX_TABLE.put("06", "BDCS_GZW_XZ");
			BDCDYLX_TABLE.put("07", "");
			BDCDYLX_TABLE.put("071", "BDCS_DZDZW_XZ");
			BDCDYLX_TABLE.put("072", "BDCS_XZDZW_XZ");
			BDCDYLX_TABLE.put("073", "BDCS_MZDZW_XZ");
		}
		StringBuilder fieldsql = new StringBuilder();
		fieldsql.append("SELECT * ");
		StringBuilder fromsql = new StringBuilder();
		Map<String, String> newpara = new HashMap<String, String>();
		fromsql.append(" FROM (SELECT QL.DJZT AS DJZT,QL.QLID AS QLID,QLR.QLRMC AS QLRMC,FSQL.DYR AS DYR,FSQL.DYFS AS DYFS,");
		fromsql.append("(SELECT MAX(ZL) FROM BDCK.").append(BDCDYLX_TABLE.get(bdcdylx));
		fromsql.append(" DY WHERE DY.BDCDYID=DJDY.BDCDYID) AS ZL,");
		fromsql.append(" QL.BDCQZH AS BDCQZH,DJDY.BDCDYID AS BDCDYID,DJDY.BDCDYH AS BDCDYH");
		fromsql.append(" FROM BDCK.BDCS_QL_XZ QL");
		fromsql.append(" LEFT JOIN  BDCK.BDCS_DJDY_XZ DJDY ON DJDY.DJDYID=QL.DJDYID");
		fromsql.append(" LEFT JOIN BDCK.BDCS_QLR_XZ QLR ON QLR.QLID=QL.QLID");
		fromsql.append(" LEFT JOIN BDCK.BDCS_FSQL_XZ FSQL ON FSQL.FSQLID=QL.FSQLID");
		fromsql.append(" WHERE QLLX='23' AND DJDY.BDCDYLX='");
		fromsql.append(bdcdylx).append("' ORDER BY QL.DJSJ)");
		fromsql.append(" WHERE 1=1 ");
		String strQLRMC = conditionParameter.get("QLRMC");
		if (!StringUtils.isEmpty(strQLRMC)) {
			strQLRMC = new String(strQLRMC.getBytes("iso8859-1"), "utf-8");
			fromsql.append("  AND QLRMC LIKE:QLRMC ");
			newpara.put("QLRMC", "%" + strQLRMC + "%");
		}
		String strBDCDYH = conditionParameter.get("BDCDYH");
		if (!StringUtils.isEmpty(strBDCDYH)) {
			strBDCDYH = new String(strBDCDYH.getBytes("iso8859-1"), "utf-8");
			fromsql.append("  AND BDCDYH LIKE:BDCDYH ");
			newpara.put("BDCDYH", "%" + strBDCDYH + "%");
		}
		String strYWR = conditionParameter.get("YWR");
		if (!StringUtils.isEmpty(strYWR)) {
			strYWR = new String(strYWR.getBytes("iso8859-1"), "utf-8");
			fromsql.append("  AND YWR LIKE:YWR ");
			newpara.put("YWR", "%" + strYWR + "%");
		}
		String strBDCQZH = conditionParameter.get("BDCQZH");
		if (!StringUtils.isEmpty(strBDCQZH)) {
			strBDCQZH = new String(strBDCQZH.getBytes("iso8859-1"), "utf-8");
			fromsql.append("  AND BDCQZH LIKE:BDCQZH ");
			newpara.put("BDCQZH", "%" + strBDCQZH + "%");
		}
		String strDYFS = conditionParameter.get("DYFS"); // 抵押方式
		if (!StringUtils.isEmpty(strDYFS)) {
			fromsql.append("  AND DYFS =:DYFS ");
			newpara.put("DYFS", strDYFS);
		}
		Long total = baseCommonDao.getCountByFullSql(fromsql.toString(), newpara);
		if (total == 0) {
			List<Map> result1 = new ArrayList<Map>();
			msg.setRows(result1);
			msg.setTotal(total);
			return msg;
		}
		List<Map> result = baseCommonDao.getPageDataByFullSql(fieldsql.append(fromsql).toString(), newpara, page, rows);
		for (int i = 0; i < result.size(); i++) {
			Map map = result.get(i);
			String DJZT = StringHelper.formatObject(map.get("DJZT"));
			String djztname = "";
			if (DJZT != null && DJZT != "") {
				djztname = ConstHelper.getNameByValue("DJZT", DJZT);
			} else {
				djztname = "未登记";
			}
			map.remove("DJZT");
			map.put("DJZT", djztname);
		}
		msg.setRows(result);
		msg.setTotal(total);
		return msg;
	}

	// TODO 草，写代码也不加个注释，
	@SuppressWarnings("rawtypes")
	@Override
	public Message GetZXDJInfo(String bdcdylx, Map<String, String> conditionParameter, int page, int rows) throws Exception {
		StringBuilder fieldsql = new StringBuilder();
		StringBuilder fromsql = new StringBuilder();
		Map<String, String> newpara = new HashMap<String, String>();
		String zlsql = String.format("(SELECT MAX(ZL) FROM   BDCK.%s BDCDY WHERE BDCDY.BDCDYID=DJDY.BDCDYID) AS ZL ", GetXZTableNameByBDCDYLX(bdcdylx));
		fieldsql.append(" SELECT *  ");
		fromsql.append(" FROM (SELECT FSQL.CFJG,QL.BDCDYH,FSQL.CFWH,QL.QLID,");
		fromsql.append(" FSQL.YWR AS QLRMC,");
		fromsql.append(zlsql);
		fromsql.append(" FROM BDCK.BDCS_DJDY_XZ DJDY LEFT JOIN BDCK.BDCS_QL_XZ QL ON DJDY.DJDYID=QL.DJDYID  ");
		fromsql.append(" LEFT JOIN BDCK.BDCS_FSQL_XZ FSQL ON QL.QLID=FSQL.QLID ");
		fromsql.append(String.format(" WHERE QL.DJLX=800 AND QL.QLLX='99' AND DJDY.BDCDYLX='%s' ORDER BY QL.DJYY DESC ", bdcdylx));
		fromsql.append(" )");
		fromsql.append(" WHERE 1=1 ");
		if (!StringUtils.isEmpty(conditionParameter.get("ZL"))) {
			fromsql.append("  AND ZL LIKE:ZL ");
			newpara.put("ZL", "%" + new String(conditionParameter.get("ZL").getBytes("iso8859-1"), "utf-8") + "%");
		}
		if (!StringUtils.isEmpty(conditionParameter.get("QLRMC"))) {
			fromsql.append("  AND QLRMC LIKE:QLRMC ");
			newpara.put("QLRMC", "%" + new String(conditionParameter.get("QLRMC").getBytes("iso8859-1"), "utf-8") + "%");
		}

		if (!StringUtils.isEmpty(conditionParameter.get("BDCDYH"))) {
			fromsql.append("  AND BDCDYH LIKE:BDCDYH ");
			newpara.put("BDCDYH", "%" + new String(conditionParameter.get("BDCDYH").getBytes("iso8859-1"), "utf-8") + "%");
		}
		if (!StringUtils.isEmpty(conditionParameter.get("CFJG"))) {
			fromsql.append("  AND CFJG LIKE:CFJG ");
			newpara.put("CFJG", "%" + new String(conditionParameter.get("CFJG").getBytes("iso8859-1"), "utf-8") + "%");
		}
		if (!StringUtils.isEmpty(conditionParameter.get("CFWH"))) {
			fromsql.append("  AND CFWH LIKE:CFWH ");
			newpara.put("CFWH", "%" + new String(conditionParameter.get("CFWH").getBytes("iso8859-1"), "utf-8") + "%");
		}
		Long total = baseCommonDao.getCountByFullSql(fromsql.toString(), newpara);
		if (total == 0)
			return new Message();
		List<Map> result = baseCommonDao.getPageDataByFullSql(fieldsql.append(fromsql).toString(), newpara, page, rows);
		Message msg = new Message();
		msg.setRows(result);
		msg.setTotal(total);
		return msg;
	}

	/**
	 * 分页查询期房单元
	 */
	@Override
	public Page getPagedPreUnit(int pageIndex, int pageSize, Map<String, String> mapCondition, String hqlCondition) {
		return baseCommonDao.getPageDataByHql(BDCS_H_XZ.class, hqlCondition, mapCondition, pageIndex, pageSize);
	}

	/**
	 * 分页查询现状自然幢信息
	 */
	@Override
	public Message QueryZrzs(int page, int rows, BDCS_ZRZ_XZ zrz) {
		StringBuilder hqlBuilder = new StringBuilder();
		hqlBuilder.append("1>0");

		if (!StringUtils.isEmpty(zrz.getZL())) {
			hqlBuilder.append(" AND ZL LIKE '%").append(zrz.getZL()).append("%'");
		}
		if (!StringUtils.isEmpty(zrz.getBDCDYH())) {
			hqlBuilder.append(" AND BDCDYH LIKE '%").append(zrz.getBDCDYH()).append("%'");
		}
		hqlBuilder.append(" AND id IN (SELECT BDCDYID FROM BDCS_DJDY_XZ)");
		Page p = baseCommonDao.getPageDataByHql(BDCS_ZRZ_XZ.class, hqlBuilder.toString(), page, rows);
		Message m = new Message();
		m.setTotal(p.getTotalCount());
		m.setRows(p.getResult());
		return m;
	}

	/**
	 * 检测期房单元状态
	 * 
	 * @作者 李想
	 * @创建时间 2015年6月27日上午19:35:59
	 * @param page
	 * @param rows
	 * @param zrz
	 * @return
	 */
	@Override
	public ResultMessage GetPreUnitState(String xmbh, String bdcdyid) {
		// TODO 暂时先判断房屋的吧，后面有需求在根据流程增加
		ResultMessage msg = new ResultMessage();
		// 判断现状房屋
		BDCS_H_XZ h_XZ = baseCommonDao.get(BDCS_H_XZ.class, bdcdyid);
		StringBuffer stateBuffer = new StringBuffer();
		if (h_XZ != null) {
			if (h_XZ.getDYZT() == null) {
				stateBuffer.append("[未知]");
			} else if (h_XZ.getDYZT().equals("0"))
				stateBuffer.append("[无抵押]");
			else
				stateBuffer.append("[已抵押]");

			if (h_XZ.getXZZT() == null) {
				stateBuffer.append("[未知]");
			} else if (h_XZ.getXZZT().equals("0"))
				stateBuffer.append("[无限制]");
			else
				stateBuffer.append("[已限制]");
		}

		msg.setMsg(stateBuffer.toString());
		return msg;
	}

	/**
	 * 根据项目编号，不动产单元ID更新不动产单元信息
	 * 
	 * @作者：俞学斌
	 * @创建时间 2015年6月26日 上午11:34:24
	 * @param xmbh
	 * @param bdcdyid
	 * @param object
	 * @return
	 * @throws ParseException
	 */
	@Override
	public ResultMessage UpdateDYInfo(String xmbh, String bdcdyid, JSONObject object) {
		ResultMessage result = new ResultMessage();
		BDCS_XMXX xmxx = baseCommonDao.get(BDCS_XMXX.class, xmbh);
		if (xmxx.getQLLX().equals(ConstValue.QLLX.GYJSYDSHYQ.Value)||xmxx.getQLLX().equals(ConstValue.QLLX.JTJSYDSYQ.Value)||xmxx.getQLLX().equals(ConstValue.QLLX.ZJDSYQ.Value)) {
			BDCS_SHYQZD_GZ dy = baseCommonDao.get(BDCS_SHYQZD_GZ.class, bdcdyid);
			if (dy != null) {
				dy.setZL(object.getString("zl"));
				// dy.setZDDM(object.getString("zddm"));
				// dy.setBDCDYH(object.getString("bdcdyh"));
				dy.setZDMJ(object.getDouble("zdmj"));
				// dy.setQLLX(object.getString("qllx"));
				// dy.setQXDM(object.getString("qx"));
				// dy.setDJQDM(object.getString("djq"));
				// dy.setDJZQDM(object.getString("djzq"));
				dy.setYT(object.getString("yt"));
				dy.setQLSDFS(object.getString("qlsdfs"));
				dy.setQLXZ(object.getString("qlxz"));
				dy.setDJ(object.getString("dj"));
				dy.setJZXG(object.getDouble("jzxg"));
				dy.setJZMD(object.getString("jzmd"));
				dy.setRJL(object.getString("rjl"));
				dy.setZDSZD(object.getString("zddz"));
				dy.setZDSZX(object.getString("zdxz"));
				dy.setZDSZN(object.getString("zdnz"));
				dy.setZDSZB(object.getString("zdbz"));
				dy.setTFH(object.getString("tfh"));
				dy.setZDMJ(object.getDouble("syqmj"));
				String jg = object.getString("jg");
				if (jg.contains("万元")) {
					jg = jg.substring(0, jg.length() - 2);
				}
				double jg1 = 0;
				if (jg.length() > 0) {
					jg1 = Double.parseDouble(jg);
				}
				dy.setJG(jg1);
				baseCommonDao.update(dy);
				baseCommonDao.flush();
			}
		} else if (xmxx.getQLLX().equals(ConstValue.QLLX.GYJSYDSHYQ_FWSYQ.Value)||xmxx.getQLLX().equals(ConstValue.QLLX.JTJSYDSYQ_FWSYQ.Value)||xmxx.getQLLX().equals(ConstValue.QLLX.ZJDSYQ_FWSYQ.Value)) {
			BDCS_H_GZ dy = baseCommonDao.get(BDCS_H_GZ.class, bdcdyid);
			if (dy != null) {
				// dy.setBDCDYH(object.getString("bdcdyh"));
				// dy.setFWBM(object.getString("fwbm"));
				dy.setZL(object.getString("zl"));
				dy.setSZC(object.getString("szc"));
				dy.setHH(object.getInteger("hh"));
				dy.setZZC(object.getDouble("zzc"));
				dy.setHX(object.getString("hx"));
				dy.setHXJG(object.getString("hxjg"));
				dy.setSHBW(object.getString("shbw"));
				dy.setFWYT1(object.getString("fwyt1"));
				dy.setFWLX(object.getString("fwlx"));
				dy.setFWYT2(object.getString("fwyt2"));
				dy.setFWXZ(object.getString("fwxz"));
				dy.setFWYT3(object.getString("fwyt3"));
				dy.setSCJZMJ(object.getDouble("scjzmj"));
				dy.setYCJZMJ(object.getDouble("ycjzmj"));
				dy.setSCTNJZMJ(object.getDouble("sctnjzmj"));
				dy.setYCTNJZMJ(object.getDouble("yctnjzmj"));
				dy.setSCFTJZMJ(object.getDouble("scftjzmj"));
				dy.setYCFTJZMJ(object.getDouble("ycftjzmj"));
				// dy.setSCDXJZMJ(object.getDouble("scdxjzmj"));
				dy.setYCDXBFJZMJ(object.getDouble("ycdxbfjzmj"));
				dy.setSCQTJZMJ(object.getDouble("scqtjzmj"));
				dy.setYCQTJZMJ(object.getDouble("ycqtjzmj"));
				dy.setSCFTXS(object.getDouble("scftxs"));
				dy.setYCFTXS(object.getDouble("ycftxs"));
				dy.setGYTDMJ(object.getDouble("gytdmj"));
				dy.setFTTDMJ(object.getDouble("fttdmj"));
				dy.setDYTDMJ(object.getDouble("dytdmj"));
				dy.setMJDW(object.getString("mjdw"));
				dy.setTDSYQR(object.getString("tdsyqr"));
				// dy.setFDCJYJG(object.getString("fdcjyjg"));
				dy.setGHYT(object.getString("ghyt"));
				dy.setFWJG(object.getString("fwjg"));
				String strJGSJ = object.getString("jgsj");
				if (!StringUtils.isEmpty(strJGSJ)) {
					try {
						Date date = DateUtil.convertStringToDate(strJGSJ);
						dy.setJGSJ(date);
					} catch (Exception e) {

					}
				}
				dy.setSCDXBFJZMJ(object.getDouble("scdxbfjzmj"));
				// dy.setCJZMJ(object.getDouble("cjzmj"));
				// dy.setCTNJZMJ(object.getDouble("ctnjzmj"));
				// dy.setCYTMJ(object.getDouble("cytmj"));
				// dy.setCGYJZMJ(object.getDouble("cgyjzmj"));
				// dy.setCFTJZMJ(object.getDouble("cftjzmj"));
				// dy.setCBQMJ(object.getDouble("cbqmj"));
				// dy.setCG(object.getString("cg"));
				// dy.setSPTYMJ(object.getDouble("sptymj"));
				dy.setZCS(object.getInteger("zcs"));
				baseCommonDao.update(dy);
				baseCommonDao.flush();
			}
		}
		result.setMsg("保存成功！");
		result.setSuccess("true");
		return result;
	}

	@Override
	public ResultMessage readContract(String htbh, String bmbm, String xmbh) throws IOException, ParseException {
		ResultMessage msg = new ResultMessage();
		// 读取合同的url
		String contractUrl =ConfigHelper.getNameByValue("JYDJ_CONTRACTURL");//改为从配置表读取 刘树峰2015年12月26日2点
		if (StringUtils.isEmpty(htbh) || StringUtils.isEmpty(bmbm)) {
			msg.setMsg("合同编号为空");
			msg.setSuccess("false");
		}
		// 实例一个URL资源
		URL url = new URL(contractUrl + htbh);
		// 实例一个HTTP CONNECT
		HttpURLConnection connet = (HttpURLConnection) url.openConnection();
		connet.setRequestProperty("Accept-Charset", "utf-8");
		connet.setRequestProperty("contentType", "utf-8");
		if (connet.getResponseCode() != 200) {
			throw new IOException(connet.getResponseMessage());
		}
		// 将返回的值存入到String中
		BufferedReader brd = new BufferedReader(new InputStreamReader(connet.getInputStream(), "utf-8"));
		StringBuilder sb = new StringBuilder();
		String line;
		while ((line = brd.readLine()) != null) {
			sb.append(line);
		}
		brd.close();
		connet.disconnect();
		if (StringHelper.isEmpty(sb.toString())) {
			msg.setSuccess("false");
			msg.setMsg("未能根据合同号查找到相应的合同");
			return msg;
		}
		JSONObject object = JSON.parseObject(sb.toString());
		JSONObject data = object.getJSONObject("data");
		JSONObject BDCDY = data.getJSONObject("bdcdy");
		JSONObject BDCQL = data.getJSONObject("bdcql");
		JSONArray qlrArray = data.getJSONArray("bdcqlrs");
		JSONObject H = BDCDY.getJSONObject("h");

		JSONObject qlArray = BDCQL.getJSONObject("fdcq2");
		@SuppressWarnings("rawtypes")
		Map qlMap = (Map) qlArray; // 权利
		@SuppressWarnings("rawtypes")
		Map bdcdyMap = (Map) H;// 单元

		if (null != bdcdyMap) {
			String bdcdyh = StringHelper.formatObject(bdcdyMap.get("bdcdyh"));
			String hql = " BDCDYH = '" + bdcdyh + "' ";
			List<BDCS_H_XZ> h_xzs = baseCommonDao.getDataList(BDCS_H_XZ.class, hql);
			if (h_xzs.size() > 0) {
				String bdcdyid = h_xzs.get(0).getId();
				DJHandler handler = HandlerFactory.createDJHandler(xmbh);
				handler.addBDCDY(bdcdyid);
				String str = MessageFormat.format("XMBH=''{0}'' AND BDCDYID=''{1}''", xmbh, bdcdyid);
				List<BDCS_DJDY_GZ> listdjdy = baseCommonDao.getDataList(BDCS_DJDY_GZ.class, str);
				if (listdjdy != null && listdjdy.size() > 0) {
					String djdyid = listdjdy.get(0).getDJDYID();
					listdjdy.get(0).setDCXMID(htbh);
					baseCommonDao.update(listdjdy.get(0));
					baseCommonDao.flush();
					Rights _rights = RightsTools.loadRightsByDJDYID(DJDYLY.GZ, xmbh, listdjdy.get(0).getDJDYID());
					if (_rights != null) {
						_rights = this.packageQL(qlMap, _rights);
						this.packageQlrs(_rights.getFSQLID(), _rights.getId(), bdcdyh, djdyid, xmbh, qlrArray);
					}
				}
				baseCommonDao.flush();
				msg.setMsg("操作成功!");
				msg.setSuccess("true");
			} else {
				msg.setMsg("未找到相关信息");
				msg.setSuccess("false");
			}
		}
		return msg;
	}

	/**
	 * 组装权利信息
	 * 
	 * @作者 diaoliwei
	 * @创建时间 2015年7月20日上午1:21:40
	 * @param map
	 * @return
	 * @throws ParseException
	 */
	private Rights packageQL(@SuppressWarnings("rawtypes") Map map, Rights rights) throws ParseException {
		if (null != map) {
			rights.setQLQSSJ(StringHelper.FormatByDate(map.get("qlqssj")));
			rights.setQLJSSJ(StringHelper.FormatByDate(map.get("qljssj")));
			rights.setFJ(StringHelper.formatObject(map.get("fj")));
			rights.setDJSJ(StringHelper.FormatByDate(map.get("djsj")));
		}
		return rights;
	}

	/**
	 * 组装权利人
	 * 
	 * @作者 diaoliwei
	 * @创建时间 2015年7月20日上午1:45:06
	 * @param qlrArray
	 * @return
	 * @throws ParseException
	 */
	private List<BDCS_QLR_GZ> packageQlrs(String fsqlid, String qlid, String bdcdyh, String djdyid, String xmbh, JSONArray qlrArray) throws ParseException {
		List<BDCS_QLR_GZ> qlrs = new ArrayList<BDCS_QLR_GZ>();
		for (int i = 0; i < qlrArray.size(); i++) {
			@SuppressWarnings("rawtypes")
			Map qlrMap = (Map) qlrArray.get(i);
			if (null != qlrMap) {
				BDCS_QLR_GZ qlr = new BDCS_QLR_GZ();
				qlr.setId(StringHelper.formatObject(qlrMap.get("qlid")));
				qlr.setQLID(qlid);
				qlr.setBZ(StringHelper.formatObject(qlrMap.get("bz")));
				qlr.setQLRMC(StringHelper.formatObject(qlrMap.get("qlrmc")));
				qlr.setZJZL(StringHelper.formatObject(qlrMap.get("zjzl")));
				qlr.setZJH(StringHelper.formatObject(qlrMap.get("zjh")));
				qlr.setFZJG(StringHelper.formatObject(qlrMap.get("fzjg")));
				qlr.setSSHY(StringHelper.formatObject(qlrMap.get("sshy")));
				qlr.setGJ(StringHelper.formatObject(qlrMap.get("gj")));
				qlr.setHJSZSS(StringHelper.formatObject(qlrMap.get("hjszss")));
				qlr.setDH(StringHelper.formatObject(qlrMap.get("dh")));
				qlr.setDZ(StringHelper.formatObject(qlrMap.get("dz")));
				qlr.setYB(StringHelper.formatObject(qlrMap.get("yb")));
				qlr.setGZDW(StringHelper.formatObject(qlrMap.get("gzdw")));
				qlr.setDZYJ(StringHelper.formatObject(qlrMap.get("dzyj")));
				qlr.setQLRLX(StringHelper.formatObject(qlrMap.get("qlrlx")));
				qlr.setQLBL(StringHelper.formatObject(qlrMap.get("qlbl")));
				qlr.setGYQK(StringHelper.formatObject(qlrMap.get("gyqk")));
				qlr.setFDDBR(StringHelper.formatObject(qlrMap.get("fddbr")));
				qlr.setFDDBRZJLX(StringHelper.formatObject(qlrMap.get("fddbrzjlx")));
				qlr.setFDDBRZJHM(StringHelper.formatObject(qlrMap.get("fddbrzjhm")));
				qlr.setFDDBRDH(StringHelper.formatObject(qlrMap.get("fddbrdh")));
				qlr.setDLRXM(StringHelper.formatObject(qlrMap.get("dlrxm")));
				qlr.setDLJGMC(StringHelper.formatObject(qlrMap.get("dljgmc")));
				qlr.setDLRZJLX(StringHelper.formatObject(qlrMap.get("dlrzjlx")));
				qlr.setDLRZJHM(StringHelper.formatObject(qlrMap.get("dlrzjhm")));
				qlr.setDLRLXDH(StringHelper.formatObject(qlrMap.get("dlrlxdh")));
				qlr.setGYFS(StringHelper.formatObject(qlrMap.get("gyfs")));
				qlr.setXMBH(xmbh);
				qlrs.add(qlr);

				BDCS_ZS_GZ zs = new BDCS_ZS_GZ();
				zs.setXMBH(xmbh);

				BDCS_QDZR_GZ qdzr = new BDCS_QDZR_GZ();
				qdzr.setBDCDYH(bdcdyh);
				qdzr.setXMBH(xmbh);
				qdzr.setDJDYID(djdyid);
				qdzr.setFSQLID(fsqlid);
				qdzr.setQLID(qlid);
				qdzr.setQLRID(qlr.getId());
				qdzr.setZSID(zs.getId());

				baseCommonDao.save(qlr);
				baseCommonDao.flush();
				baseCommonDao.save(qdzr);
				baseCommonDao.flush();
				baseCommonDao.save(zs);
				baseCommonDao.flush();
			}
		}
		return qlrs;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public ResultMessage updateHouse(BDCDYLX bdcdylx, String ly, String dataString, String bdcdyid) {
		ResultMessage resultMessage = new ResultMessage();
		resultMessage.setSuccess("false");
		resultMessage.setMsg("保存失败!");
		net.sf.json.JSONArray array = net.sf.json.JSONArray.fromObject(dataString);
		Collection collection = net.sf.json.JSONArray.toCollection(array);
		net.sf.json.JSONObject jsonObj = null;
		if (null != collection && !collection.isEmpty()) {
			Iterator it = collection.iterator();
			while (it.hasNext()) {
				jsonObj = net.sf.json.JSONObject.fromObject(it.next());
			}
		}
		// 1.查询出原先的单元信息
		RealUnit unit = null;
		if ("xz".equals(ly)) {
			unit = UnitTools.loadUnit(bdcdylx, DJDYLY.XZ, bdcdyid);
		}
		if ("gz".equals(ly)) {
			unit = UnitTools.loadUnit(bdcdylx, DJDYLY.GZ, bdcdyid);
		}
		// 查询出的转换成House
		House house = null;
		if (null != unit && (bdcdylx.Value.equals(BDCDYLX.YCH.Value) || bdcdylx.Value.equals(BDCDYLX.H.Value))) {
			house = (House) unit;
		}
		// 2.需要更新的单元信息 newhouse中的更新的字段值需要赋值到house中。
		House newhouse = null;
		if (bdcdylx.Value.equals(BDCDYLX.YCH.Value) && null != jsonObj && "xz".equals(ly)) {
			BDCS_H_XZY h_xzy = (BDCS_H_XZY) net.sf.json.JSONObject.toBean(jsonObj, BDCS_H_XZY.class);
			newhouse = h_xzy;
			// newhouse中的值赋值到house中
			house = packageHouseInfo(house, newhouse);
			if (null != house) {
				h_xzy = (BDCS_H_XZY) house;
				baseCommonDao.update(h_xzy);
				resultMessage.setSuccess("true");
				resultMessage.setMsg("保存成功!");
			}
		}
		if (bdcdylx.Value.equals(BDCDYLX.H.Value) && null != jsonObj) {
			if ("xz".equals(ly)) {
				BDCS_H_XZ h = (BDCS_H_XZ) net.sf.json.JSONObject.toBean(jsonObj, BDCS_H_XZ.class);
				newhouse = h;
				// newhouse中的值赋值到house中
				house = packageHouseInfo(house, newhouse);
				if (null != house) {
					h = (BDCS_H_XZ) house;
					baseCommonDao.update(h);
					resultMessage.setSuccess("true");
					resultMessage.setMsg("保存成功!");
				}
			}
			if ("gz".equals(ly)) {
				BDCS_H_GZ h = (BDCS_H_GZ) net.sf.json.JSONObject.toBean(jsonObj, BDCS_H_GZ.class);
				newhouse = h;
				// newhouse中的值赋值到house中
				house = packageHouseInfo(house, newhouse);
				if (null != house) {
					h = (BDCS_H_GZ) house;
					baseCommonDao.update(h);
					resultMessage.setSuccess("true");
					resultMessage.setMsg("保存成功!");
				}
			}
		}
		baseCommonDao.flush();
		return resultMessage;
	}

	/**
	 * 组装需要更新的house信息
	 * 
	 * @author diaoliwei
	 * @date 2015-8-6
	 * @param house
	 * @param newhouse
	 * @return
	 */
	private House packageHouseInfo(House house, House newhouse) {
		if (null != house && null != newhouse) {
			house.setBDCDYH(newhouse.getBDCDYH());
			house.setFWBM(newhouse.getFWBM());
			house.setZRZH(newhouse.getZRZH());
			house.setFH(newhouse.getFH());
			house.setZL(newhouse.getZL());
			house.setSZC(newhouse.getSZC());
			house.setHH(newhouse.getHH());
			house.setHX(newhouse.getHX());
			house.setHXJG(newhouse.getHXJG());
			house.setSHBW(newhouse.getSHBW());
			house.setFWYT1(newhouse.getFWYT1());
			house.setFWLX(newhouse.getFWLX());
			house.setFWYT2(newhouse.getFWYT2());
			house.setFWXZ(newhouse.getFWXZ());
			house.setFWYT3(newhouse.getFWYT3());
			house.setSCJZMJ(newhouse.getSCJZMJ());
			house.setYCJZMJ(newhouse.getYCJZMJ());
			house.setSCTNJZMJ(newhouse.getSCTNJZMJ());
			house.setYCTNJZMJ(newhouse.getYCTNJZMJ());
			house.setSCFTJZMJ(newhouse.getSCFTJZMJ());
			house.setYCFTJZMJ(newhouse.getYCFTJZMJ());
			// house.setSCDXJZMJ(newhouse.getSCDXJZMJ());
			house.setYCDXBFJZMJ(newhouse.getYCDXBFJZMJ());
			house.setSCQTJZMJ(newhouse.getSCQTJZMJ());
			house.setYCQTJZMJ(newhouse.getYCQTJZMJ());
			house.setSCFTXS(newhouse.getSCFTXS());
			house.setYCFTXS(newhouse.getYCFTXS());
			house.setGYTDMJ(newhouse.getGYTDMJ());
			house.setFTTDMJ(newhouse.getFTTDMJ());
			house.setDYTDMJ(newhouse.getDYTDMJ());
			house.setMJDW(newhouse.getMJDW());
			house.setTDSYQR(newhouse.getTDSYQR());
			house.setFDCJYJG(newhouse.getFDCJYJG());
			house.setGHYT(newhouse.getGHYT());
			house.setJGSJ(newhouse.getJGSJ());
			house.setSCDXBFJZMJ(newhouse.getSCDXBFJZMJ());
		}
		return house;
	}

	/**
	 * 给房屋加上强制过户限制状态
	 * 
	 * @Title: addQZGHXZ
	 * @author:liushufeng
	 * @date：2015年8月15日 下午5:49:41
	 * @param bdcdyid
	 * @param xmbh
	 * @return
	 */
	@Override
	public ResultMessage addQZGHXZ(String bdcdyid, String xmbh) {
		ResultMessage msg = new ResultMessage();
		ProjectInfo info = ProjectHelper.GetPrjInfoByXMBH(xmbh);
		BDCDYLX dylx = BDCDYLX.H;
		if (info != null && !StringHelper.isEmpty(info.getBdcdylx())) {
			try {
				dylx = BDCDYLX.initFrom(info.getBdcdylx());
			} catch (Exception e) {

			}
		}
		msg.setSuccess("false");
		House house = (House) UnitTools.loadUnit(dylx, DJDYLY.XZ, bdcdyid);
		if (house != null) {
			house.setXZZT(XZZT.QZGH.Value);
			baseCommonDao.update(house);
			baseCommonDao.flush();
			msg.setSuccess("true");
		}
		House house1 = (House) UnitTools.loadUnit(dylx, DJDYLY.LS, bdcdyid);
		if (house1 != null) {
			house1.setXZZT(XZZT.QZGH.Value);
			baseCommonDao.update(house1);
			baseCommonDao.flush();
			msg.setSuccess("true");
		}
		return msg;
	}

	/**
	 * 给房屋解除强制过户限制状态
	 * 
	 * @Title: qxQZGHXZ
	 * @author:liushufeng
	 * @date：2015年8月15日 下午5:49:41
	 * @param bdcdyid
	 * @param xmbh
	 * @return
	 */
	@Override
	public ResultMessage qxQZGHXZ(String bdcdyid, String xmbh) {
		ResultMessage msg = new ResultMessage();

		ProjectInfo info = ProjectHelper.GetPrjInfoByXMBH(xmbh);
		BDCDYLX dylx = BDCDYLX.H;
		if (info != null && !StringHelper.isEmpty(info.getBdcdylx())) {
			try {
				dylx = BDCDYLX.initFrom(info.getBdcdylx());
			} catch (Exception e) {

			}
		}
		msg.setSuccess("false");
		House house = (House) UnitTools.loadUnit(dylx, DJDYLY.XZ, bdcdyid);
		if (house != null) {
			house.setXZZT(null);
			baseCommonDao.update(house);
			baseCommonDao.flush();
			msg.setSuccess("true");
		}
		House house1 = (House) UnitTools.loadUnit(dylx, DJDYLY.LS, bdcdyid);
		if (house1 != null) {
			house1.setXZZT(null);
			baseCommonDao.update(house1);
			baseCommonDao.flush();
			msg.setSuccess("true");
		}
		return msg;
	}
	
	@Override
	public boolean BackBDCDYInfo(String xmbh,BDCDYLX dylx,DJDYLY dyly,String bdcdyid) {
		if(DJDYLY.XZ.equals(dyly)||DJDYLY.LS.equals(dyly)){
			RealUnit unit = UnitTools.loadUnit(dylx, DJDYLY.LS, bdcdyid);
			if(unit!=null){
				List<BDCS_DYBG> dybgs=baseCommonDao.getDataList(BDCS_DYBG.class, "XMBH='"+xmbh+"' AND XBDCDYID='"+bdcdyid+"'");
				if(dybgs!=null&&dybgs.size()>0){
					return true;
				}
				String old_bdcdyid=SuperHelper.GeneratePrimaryKey();
				String dybgid=SuperHelper.GeneratePrimaryKey();
				String djdyid="";
				List<BDCS_DJDY_LS> djdys=baseCommonDao.getDataList(BDCS_DJDY_LS.class, "BDCDYID='"+bdcdyid+"'");
				if(djdys!=null&&djdys.size()>0){
					djdyid=djdys.get(0).getDJDYID();
				}
				BDCS_DYBG dybg=new BDCS_DYBG();
				dybg.setId(dybgid);
				dybg.setLBDCDYID(old_bdcdyid);
				dybg.setXBDCDYID(bdcdyid);
				dybg.setLDJDYID(djdyid);
				dybg.setXDJDYID(djdyid);
				dybg.setXMBH(xmbh);
				baseCommonDao.save(dybg);
				
				RealUnit unit_back=UnitTools.newRealUnit(dylx, DJDYLY.LS);
				ObjectHelper.copyObject(unit, unit_back);
				unit_back.setId(old_bdcdyid);
				baseCommonDao.save(unit_back);
			}
		}
		return true;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public ResultMessage UpdateBDCDYInfo(Map map,BDCDYLX dylx,DJDYLY dyly,String bdcdyid) {
		ResultMessage msg = new ResultMessage();
		RealUnit unit_update = UnitTools.loadUnit(dylx, dyly, bdcdyid);
		if (unit_update != null) {
			if (dylx.equals(BDCDYLX.H) || dylx.equals(BDCDYLX.YCH)) {
				House h = (House) unit_update;
				setValue(map, h);
				baseCommonDao.update(h);
				YwLogUtil.addYwLog("更新单元信息-户", ConstValue.SF.YES.Value,ConstValue.LOG.UPDATE);
			} else if (dylx.equals(BDCDYLX.ZRZ) || dylx.equals(BDCDYLX.YCZRZ)) {
				Building building = (Building) unit_update;
				setValue(map, building);
				baseCommonDao.update(building);
				YwLogUtil.addYwLog("更新单元信息-自然幢", ConstValue.SF.YES.Value,ConstValue.LOG.UPDATE);
			} else if (dylx.equals(BDCDYLX.SHYQZD)) {
				UseLand useland = (UseLand) unit_update;
				setValue(map, useland);
				baseCommonDao.update(useland);
				YwLogUtil.addYwLog("更新单元信息-使用权宗地", ConstValue.SF.YES.Value,ConstValue.LOG.UPDATE);
			} else if (dylx.equals(BDCDYLX.SYQZD)) {
				OwnerLand ownerland = (OwnerLand) unit_update;
				setValue(map, ownerland);
				baseCommonDao.update(ownerland);
				YwLogUtil.addYwLog("更新单元信息-所有权宗地", ConstValue.SF.YES.Value,ConstValue.LOG.UPDATE);
			} else if (dylx.equals(BDCDYLX.LD)) {
				Forest forest = (Forest) unit_update;
				setValue(map, forest);
				baseCommonDao.update(forest);
				YwLogUtil.addYwLog("更新单元信息-林地", ConstValue.SF.YES.Value,ConstValue.LOG.UPDATE);
			}
			if(DJDYLY.XZ.equals(dyly)){
				this.UpdateBDCDYInfo(map, dylx, DJDYLY.LS, bdcdyid);
			}
			baseCommonDao.flush();
		} else {
			msg.setSuccess("false");
			msg.setMsg("更新失败!未找到不动产单元");
			YwLogUtil.addYwLog("更新单元信息-更新失败!未找到不动产单元", ConstValue.SF.NO.Value,ConstValue.LOG.UPDATE);
			return msg;
		}
		msg.setSuccess("true");
		msg.setMsg("更新成功!");
		return msg;
	}

	@SuppressWarnings("rawtypes")
	public void setValue(Map map, Object thisObj) {
		Set set = map.keySet();
		Iterator iterator = set.iterator();
		while (iterator.hasNext()) {
			Object obj = iterator.next();
			Object val = map.get(obj);
			setMethod(obj, val, thisObj);
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void setMethod(Object method, Object value, Object thisObj) {
		Class c;
		try {
			c = Class.forName(thisObj.getClass().getName());
			String met = (String) method;
			met = met.trim();
			if (!met.substring(0, 1).equals(met.substring(0, 1).toUpperCase())) {
				met = met.toUpperCase();
			}
			if (!String.valueOf(method).startsWith("set")) {
				met = "set" + met;
			}
			Class types[] = getMethodParamTypes(thisObj, met);
			if (types != null && types.length > 0) {
				Method m = c.getMethod(met, types);
				if (types[0].getName().contains("String")) {
					String strValue = StringHelper.formatObject(value);
					m.invoke(thisObj, strValue);
				} else if (types[0].getName().contains("Double")) {

					Double doubleValue = 0.0;
					if (!StringHelper.isEmpty(value)) {
						doubleValue = StringHelper.getDouble(value);
					}
					m.invoke(thisObj, doubleValue);
				} else if (types[0].getName().contains("Date")) {
					Object obj = null;
					if (!StringHelper.isEmpty(value)) {
						m.invoke(thisObj, StringHelper.FormatByDate(value));
					} else {
						m.invoke(thisObj, obj);
					}
				} else if (types[0].getName().contains("Integer")) {
					int intValue = 0;
					if (!StringHelper.isEmpty(value)) {
						intValue = (int) StringHelper.getDouble(value);
					}
					m.invoke(thisObj, intValue);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("rawtypes")
	public Class[] getMethodParamTypes(Object classInstance, String methodName) throws ClassNotFoundException {
		Class[] paramTypes = null;
		Method[] methods = classInstance.getClass().getMethods();// 全部方法
		for (int i = 0; i < methods.length; i++) {
			if (methodName.equals(methods[i].getName())) {// 和传入方法名匹配
				Class[] params = methods[i].getParameterTypes();
				paramTypes = new Class[params.length];
				for (int j = 0; j < params.length; j++) {
					paramTypes[j] = Class.forName(params[j].getName());
				}
				break;
			}
		}
		return paramTypes;
	}

	/**
	 * 根据自然幢不动产单元id、自然幢不动产单元类型获取户不动产单元号
	 * 
	 * @作者 俞学斌
	 * @创建时间 2015年9月18日12:02:53
	 * @param zrzbdcdyid
	 * @param zrzbdcdylx
	 * @return
	 */
	@Override
	public ResultMessage getHouseBDCDYH(String zrzbdcdyid, String zrzbdcdylx) {
		String m_bdcdylx = zrzbdcdylx;
		if (StringHelper.isEmpty(m_bdcdylx)) {
			m_bdcdylx = "03";
		} else if (!BDCDYLX.ZRZ.Value.equals(m_bdcdylx) && !!BDCDYLX.YCZRZ.Value.equals(m_bdcdylx)) {
			m_bdcdylx = "03";
		}
		ResultMessage msg = new ResultMessage();
		msg.setSuccess("false");
		if (StringHelper.isEmpty(zrzbdcdyid)) {
			msg.setMsg("获取失败！请先关联自然幢！");
		} else {
			RealUnit unit = UnitTools.loadUnit(BDCDYLX.initFrom(m_bdcdylx), DJDYLY.XZ, zrzbdcdyid);
			if (unit == null) {
				msg.setMsg("获取失败！关联自然幢不存在，请检查是否关联正确！");
			} else {
				String zrzbdcdyh = unit.getBDCDYH();
				if (StringHelper.isEmpty(zrzbdcdyh)) {
					msg.setMsg("获取失败！关联自然幢未生成不动产单元号！");
				} else {
					if (zrzbdcdyh.length() != 28 || zrzbdcdyh.charAt(19) != 'F') {
						msg.setMsg("获取失败！关联自然幢不动产单元号格式有误！");
					} else {
						String bdcdyh = UnitTools.CreatBDCDYH(zrzbdcdyh.substring(0, 24), "04");
						if (StringHelper.isEmpty(bdcdyh)) {
							msg.setMsg("获取失败！请检查数据库版本！");
						} else {
							msg.setSuccess("true");
							msg.setMsg(bdcdyh);
						}
					}
				}
			}
		}
		return msg;
	}

//	/**
//	 * 根据单元号生成依赖值，单元类型生成不动产单元号 单元类型：宗地：01;宗海：02;自然幢：03;户04；森林、林木：05
//	 * @作者 俞学斌
//	 * @创建时间 2015年9月18日上午11:04:53
//	 * @param RelyOnValue
//	 *            单元号生成依赖值
//	 * @param DYLX
//	 *            单元类型
//	 * @return
//	 */
//	private String CreatBDCDYH(String RelyOnValue, String DYLX) {
//		final String m_producename = "GETDYHEX";
//		final String m_relyonvalue = RelyOnValue;
//		final String m_dylx = DYLX;
//		Session session = this.baseCommonDao.getCurrentSession();
//		String filenumber = session.doReturningWork(new ReturningWork<String>() {
//			@Override
//			public String execute(Connection connection) throws SQLException {
//				StringBuilder str = new StringBuilder();
//				str.append("{ Call ");
//				str.append("BDCDCK.");
//				str.append(m_producename);
//				str.append("(");
//				str.append("?,?,?");
//				str.append(") }");
//				String filrnumberString = "";
//				CallableStatement statement;
//				statement = connection.prepareCall(str.toString());
//				statement.setString(1, m_relyonvalue);
//				statement.setString(2, m_dylx);
//				statement.registerOutParameter(3, Types.NVARCHAR);
//				statement.execute();
//				filrnumberString = statement.getString(3);
//				statement.close();
//				return filrnumberString;
//			}
//		});
//		if (StringHelper.isEmpty(filenumber)) {
//			return "";
//		}
//		String BDCDYH = "";
//		if ("01".equals(m_dylx)) {// 宗地
//			BDCDYH = RelyOnValue + StringHelper.PadLeft(filenumber, 5, '0') + "W00000000";
//		}
//		if ("011".equals(m_dylx)) {// 宗地
//			BDCDYH = RelyOnValue + StringHelper.PadLeft(filenumber, 5, '0');
//		}
//		if ("02".equals(m_dylx)) {// 宗海
//			BDCDYH = RelyOnValue + StringHelper.PadLeft(filenumber, 5, '0') + "H00000000";
//		}
//		if ("03".equals(m_dylx)) {// 自然幢
//			BDCDYH = RelyOnValue + "F" + StringHelper.PadLeft(filenumber, 4, '0') + "0000";
//		}
//		if ("04".equals(m_dylx)) {// 户
//			BDCDYH = RelyOnValue + StringHelper.PadLeft(filenumber, 4, '0');
//		}
//		if ("05".equals(m_dylx)) {// 森林林木
//			BDCDYH = RelyOnValue + "L" + StringHelper.PadLeft(filenumber, 8, '0');
//		}
//		return BDCDYH;
//	}

	/**
	 * 根据表单信息添加不动产单元、登记单元、权利、附属权利
	 * @作者 俞学斌
	 * @创建时间 2015年9月18日16:42:53
	 * @param map
	 *            表单信息
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public ResultMessage AddBDCDYInfo(Map map) {
		ResultMessage msg = new ResultMessage();
		String bdcdyid = SuperHelper.GeneratePrimaryKey();
		String xmbh = StringHelper.formatObject(map.get("xmbh"));
		if (StringHelper.isEmpty(xmbh)) {
			msg.setSuccess("false");
			msg.setMsg("添加失败!");
			YwLogUtil.addYwLog("根据表单信息添加不动产单元、登记单元、权利、附属权利", ConstValue.SF.NO.Value,ConstValue.LOG.ADD);
			return msg;
		}
		ProjectInfo info = ProjectHelper.GetPrjInfoByXMBH(xmbh);
		if (info == null) {
			msg.setSuccess("false");
			msg.setMsg("添加失败!");
			YwLogUtil.addYwLog("根据表单信息添加不动产单元、登记单元、权利、附属权利", ConstValue.SF.NO.Value,ConstValue.LOG.ADD);
			return msg;
		}
		BDCDYLX dylx = BDCDYLX.initFrom(info.getBdcdylx());
		boolean bAdd = AddBDCDYInfo(dylx, bdcdyid, map);
		if (bAdd) {
			DJHandler handler = HandlerFactory.createDJHandler(xmbh);
			if (handler != null) {
				handler.addBDCDY(bdcdyid);
			}
		} else {
			msg.setSuccess("false");
			msg.setMsg("添加失败!");
			YwLogUtil.addYwLog("根据表单信息添加不动产单元、登记单元、权利、附属权利", ConstValue.SF.NO.Value,ConstValue.LOG.ADD);
			return msg;
		}
		baseCommonDao.flush();
		msg.setSuccess("true");
		msg.setMsg("添加成功!");
		YwLogUtil.addYwLog("根据表单信息添加不动产单元、登记单元、权利、附属权利", ConstValue.SF.YES.Value,ConstValue.LOG.ADD);
		return msg;
	}

	/**
	 * 批量更新户信息
	 */
	@Override
	public ResultMessage plupdatehouseinfo(Object[] bdcdyids, JSONObject houseinfo) {
		ResultMessage msg = new ResultMessage();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date jgsj = new Date();
		try {
			if (!StringHelper.isEmpty(houseinfo.get("jgsj"))) {
				jgsj = sdf.parse(houseinfo.get("jgsj").toString());
			} else {
				jgsj = null;
			}
		} catch (ParseException e) {
		}
		String fwlx = "";// 房屋类型
		String fwxz = "";// 房屋性质
		String ghyt = "";// 规划用途
		int zcs = -1000;// 总层数
		String fwjg = "";// 房屋结构
		if (houseinfo.get("fwlx") != null)
			fwlx = houseinfo.get("fwlx").toString();
		if (houseinfo.get("fwxz") != null)
			fwxz = houseinfo.get("fwxz").toString();
		if (houseinfo.get("ghyt") != null)
			ghyt = houseinfo.get("ghyt").toString();
		if (houseinfo.get("fwjg") != null)
			fwjg = houseinfo.get("fwjg").toString();
		if (houseinfo.get("zcs") != null)
			try {
				zcs = Integer.parseInt(houseinfo.get("zcs").toString());
			} catch (Exception e) {
				zcs = -1000;
			}

		for (Object bdcdyid : bdcdyids) {
			if (bdcdyid != null) {
				BDCS_H_GZ bdcs_h_gz = (BDCS_H_GZ) UnitTools.loadUnit(BDCDYLX.H, DJDYLY.GZ, bdcdyid.toString());
				if (bdcs_h_gz != null) {
					bdcs_h_gz.setFWLX(fwlx);
					if (zcs != -1000)// 判断是否需要更新总层数
						bdcs_h_gz.setZCS(zcs);
					bdcs_h_gz.setGHYT(ghyt);
					bdcs_h_gz.setFWXZ(fwxz);
					bdcs_h_gz.setFWJG(fwjg);
					bdcs_h_gz.setJGSJ(jgsj);
					baseCommonDao.update(bdcs_h_gz);
				}
			}
			baseCommonDao.flush();
		}
		msg.setSuccess("true");
		msg.setMsg("更新成功!");
		YwLogUtil.addYwLog("批量更新户信息-更新成功", ConstValue.SF.YES.Value,ConstValue.LOG.UPDATE);
		return msg;
	}

	/**
	 * 根据单元类型、不动产单元ID、表单信息添加不动产单元
	 * @作者 俞学斌
	 * @创建时间 2015年9月18日16:42:53
	 * @param dylx
	 *            单元类型
	 * @param bdcdyid
	 *            不动产单元ID
	 * @param map
	 *            表单信息
	 * @return
	 */
	@SuppressWarnings({ "rawtypes" })
	private boolean AddBDCDYInfo(BDCDYLX dylx, String bdcdyid, Map map) {
		if (dylx.equals(BDCDYLX.H)) {
			BDCS_H_GZ h = new BDCS_H_GZ();
			setValue(map, h);
			h.setId(bdcdyid);
			String xzqhdm=ConfigHelper.getNameByValue("XZQHDM");
			if(!StringHelper.isEmpty(xzqhdm) && ("360700").equals(xzqhdm)){
				String relationid = map.get("fwbm")==null?"":map.get("fwbm").toString();
				h.setRELATIONID(relationid);
			}
			baseCommonDao.save(h);
		} else if (dylx.equals(BDCDYLX.ZRZ)) {
			BDCS_ZRZ_GZ building = new BDCS_ZRZ_GZ();
			setValue(map, building);
			building.setId(bdcdyid);
			baseCommonDao.save(building);
		} else if (dylx.equals(BDCDYLX.SHYQZD)) {
			BDCS_SHYQZD_GZ useland = new BDCS_SHYQZD_GZ();
			setValue(map, useland);
			useland.setId(bdcdyid);
			baseCommonDao.save(useland);
		} else if (dylx.equals(BDCDYLX.SYQZD)) {
			BDCS_SYQZD_GZ ownerland = new BDCS_SYQZD_GZ();
			setValue(map, ownerland);
			ownerland.setId(bdcdyid);
			baseCommonDao.save(ownerland);
		} else {
			return false;
		}
		return true;
	}
	
	@Override
	public ResultMessage checkAcceptable(String xmbh, String bdcdyid)
	{
		ResultMessage msg=new ResultMessage();
		msg.setSuccess("true");
		msg.setMsg("");
		return msg;
	}

	/**
	 * 获取抵押变更项目不动产单元列表
	 * 
	 * @param xmbh
	 * @param request
	 * @param response
	 * @author yuxuebin
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Message GetDYBGDJDYS(String xmbh,String type,Integer page,Integer rows) {
		Message ms=new Message();
		ms.setSuccess("false");
		ms.setTotal(0);
		ProjectInfo info=ProjectHelper.GetPrjInfoByXMBH(xmbh);
		if(info!=null){
			String bdcdylx="031";
			List<Map> list=new ArrayList<Map>();
			long count = 0;
			List<Map> listmap = new ArrayList<Map>();
			if("bgq".equals(type)){
				count=baseCommonDao.getCountByFullSql(" FROM BDCK.BDCS_QL_GZ WHERE XMBH='"+xmbh+"' AND ISCANCEL<>'2'");
				listmap = baseCommonDao.getPageDataByFullSql("SELECT QL.*,ROW_NUMBER() OVER(PARTITION BY QL.ISCANCEL ORDER BY QL.DJSJ DESC) ROW_NUMBER FROM BDCK.BDCS_QL_GZ QL WHERE QL.XMBH='"+xmbh+"' AND QL.ISCANCEL<>'2'", page, rows);
			}else{
				count=baseCommonDao.getCountByFullSql(" FROM BDCK.BDCS_QL_GZ WHERE XMBH='"+xmbh+"'");
				listmap = baseCommonDao.getPageDataByFullSql("SELECT QL.*,ROW_NUMBER() OVER(PARTITION BY QL.ISCANCEL ORDER BY QL.DJSJ DESC) ROW_NUMBER FROM BDCK.BDCS_QL_GZ QL WHERE QL.XMBH='"+xmbh+"'", page, rows);
			}
			
			if(listmap!=null&&listmap.size()>0){
				for(Map ql:listmap){
					String bdcqzh = "";
					String djdyid=StringHelper.formatObject(ql.get("DJDYID"));
					String qlid=StringHelper.formatObject(ql.get("QLID"));
					List<BDCS_DJDY_GZ> djdys=baseCommonDao.getDataList(BDCS_DJDY_GZ.class, "DJDYID='"+djdyid+"' AND XMBH='"+ xmbh +"'");
					if(djdys!=null&&djdys.size()>0){
						String bdcdyid=djdys.get(0).getBDCDYID();
						bdcdylx=djdys.get(0).getBDCDYLX();
						RealUnit unit =UnitTools.loadUnit(BDCDYLX.initFrom(bdcdylx), DJDYLY.XZ, bdcdyid);
						List<BDCS_QL_XZ> ql_xz = baseCommonDao.getDataList(BDCS_QL_XZ.class, " DJDYID='"+djdyid+"'");
						if(info.getBaseworkflowcode().equals("BG211")||info.getBaseworkflowcode().equals("BG212")||info.getBaseworkflowcode().equals("BG213")){
							ql_xz = baseCommonDao.getDataList(BDCS_QL_XZ.class, " DJDYID='"+djdyid+"' AND QLLX='23' ");
						}
						if(ql_xz.size() >0){
							bdcqzh = StringHelper.formatObject(ql_xz.get(0).getBDCQZH());
						}
						if("BG027".equals(info.getBaseworkflowcode())){
							List<BDCS_QL_LS> ql_ls = baseCommonDao.getDataList(BDCS_QL_LS.class, " DJDYID='"+djdyid+"'");
							ql_ls = baseCommonDao.getDataList(BDCS_QL_LS.class, " DJDYID='"+djdyid+"' AND QLLX !='23' AND QLLX !='99' ORDER BY DJSJ DESC ");
							if (ql_ls.size() >0) {
								bdcqzh = StringHelper.formatObject(ql_ls.get(0).getBDCQZH());
							}
						}
						if(unit!=null){
							HashMap<String,String> m=new HashMap<String, String>();
							m.put("ZL", StringHelper.formatObject(unit.getZL()));
							m.put("BDCDYH", StringHelper.formatObject(unit.getBDCDYH()));
							m.put("BDCDYLX", StringHelper.formatObject(unit.getBDCDYLX().Value));
							m.put("BDCDYID", StringHelper.formatObject(unit.getId()));
							m.put("BDCQZH",bdcqzh);
							m.put("MJ", StringHelper.formatDouble(unit.getMJ()));
							if(BDCDYLX.H.Value.equals(bdcdylx)||BDCDYLX.YCH.Value.equals(bdcdylx)){
								House h=(House)unit;
								m.put("FH", h.getFH());
							}
							String iscancel="0";
							if(!StringHelper.isEmpty(StringHelper.formatObject(ql.get("ISCANCEL")))){
								iscancel=StringHelper.formatObject(ql.get("ISCANCEL"));
							}
							m.put("ISCANCEL", iscancel);
							m.put("QLID", qlid);
							list.add(m);
						}
					}
				}
			}
			//状态、再房号
			Collections.sort(list, new Comparator<Map>() {
				@Override
				public int compare(Map o1, Map o2) {
					String ISCANCEL_o1 = StringHelper.formatObject(o1.get("ISCANCEL"));
					String ISCANCEL_o2 = StringHelper.formatObject(o2.get("ISCANCEL"));
					if (ISCANCEL_o1 == null || ISCANCEL_o2 != null
							&& ISCANCEL_o1.compareTo(ISCANCEL_o2) > 0) {
						return -1;
					} else if (ISCANCEL_o1.equals(ISCANCEL_o2)) {
						String FH_o1 = StringHelper.formatObject(o1.get("FH"));
						String FH_o2 = StringHelper.formatObject(o2.get("FH"));
						if (FH_o1 == null || FH_o2 != null
								&& FH_o1.compareTo(FH_o2) > 0) {
							return 1;
						} else if (FH_o1.equals(FH_o2)) {
							return 0;
						}
						return -1;
					}
					return 1;
				}
			});
			StringBuilder builder=new StringBuilder();
			if("bgq".equals(type)){
				if(BDCDYLX.H.Value.equals(bdcdylx)){
					builder.append("SELECT COUNT(*) AS GS,SUM(SCJZMJ) AS MJ FROM BDCK.BDCS_H_XZ DY ");
					builder.append("WHERE EXISTS (SELECT 1 FROM BDCK.BDCS_QL_GZ QL ");
					builder.append("LEFT JOIN BDCK.BDCS_DJDY_GZ DJDY ON DJDY.DJDYID=QL.DJDYID ");
					builder.append("WHERE DJDY.BDCDYID=DY.BDCDYID ");
					builder.append("AND QL.XMBH='"+xmbh+"' AND QL.ISCANCEL<>'2')");
				}else if(BDCDYLX.YCH.Value.equals(bdcdylx)){
					builder.append("SELECT COUNT(*) AS GS,SUM(YCJZMJ) AS MJ FROM BDCK.BDCS_H_XZY DY ");
					builder.append("WHERE EXISTS (SELECT 1 FROM BDCK.BDCS_QL_GZ QL ");
					builder.append("LEFT JOIN BDCK.BDCS_DJDY_GZ DJDY ON DJDY.DJDYID=QL.DJDYID ");
					builder.append("WHERE DJDY.BDCDYID=DY.BDCDYID ");
					builder.append("AND QL.XMBH='"+xmbh+"' AND QL.ISCANCEL<>'2')");
				}else if(BDCDYLX.SHYQZD.Value.equals(bdcdylx)){
					builder.append("SELECT COUNT(*) AS GS,SUM(ZDMJ) AS MJ FROM BDCK.BDCS_SHYQZD_XZ DY ");
					builder.append("WHERE EXISTS (SELECT 1 FROM BDCK.BDCS_QL_GZ QL ");
					builder.append("LEFT JOIN BDCK.BDCS_DJDY_GZ DJDY ON DJDY.DJDYID=QL.DJDYID ");
					builder.append("WHERE DJDY.BDCDYID=DY.BDCDYID ");
					builder.append("AND QL.XMBH='"+xmbh+"' AND QL.ISCANCEL<>'2')");
				}else if(BDCDYLX.SYQZD.Value.equals(bdcdylx)){
					builder.append("SELECT COUNT(*) AS GS,SUM(ZDMJ) AS MJ FROM BDCK.BDCS_SYQZD_XZ DY ");
					builder.append("WHERE EXISTS (SELECT 1 FROM BDCK.BDCS_QL_GZ QL ");
					builder.append("LEFT JOIN BDCK.BDCS_DJDY_GZ DJDY ON DJDY.DJDYID=QL.DJDYID ");
					builder.append("WHERE DJDY.BDCDYID=DY.BDCDYID ");
					builder.append("AND QL.XMBH='"+xmbh+"' AND QL.ISCANCEL<>'2')");
				}
			}else{
				if(BDCDYLX.H.Value.equals(bdcdylx)){
					builder.append("SELECT COUNT(*) AS GS,SUM(SCJZMJ) AS MJ FROM BDCK.BDCS_H_XZ DY ");
					builder.append("WHERE EXISTS (SELECT 1 FROM BDCK.BDCS_QL_GZ QL ");
					builder.append("LEFT JOIN BDCK.BDCS_DJDY_GZ DJDY ON DJDY.DJDYID=QL.DJDYID ");
					builder.append("WHERE DJDY.BDCDYID=DY.BDCDYID AND (QL.ISCANCEL='0' OR QL.ISCANCEL='2') ");
					builder.append("AND QL.XMBH='"+xmbh+"')");
				}else if(BDCDYLX.YCH.Value.equals(bdcdylx)){
					builder.append("SELECT COUNT(*) AS GS,SUM(YCJZMJ) AS MJ FROM BDCK.BDCS_H_XZY DY ");
					builder.append("WHERE EXISTS (SELECT 1 FROM BDCK.BDCS_QL_GZ QL ");
					builder.append("LEFT JOIN BDCK.BDCS_DJDY_GZ DJDY ON DJDY.DJDYID=QL.DJDYID ");
					builder.append("WHERE DJDY.BDCDYID=DY.BDCDYID AND (QL.ISCANCEL='0' OR QL.ISCANCEL='2') ");
					builder.append("AND QL.XMBH='"+xmbh+"')");
				}else if(BDCDYLX.SHYQZD.Value.equals(bdcdylx)){
					builder.append("SELECT COUNT(*) AS GS,SUM(ZDMJ) AS MJ FROM BDCK.BDCS_SHYQZD_XZ DY ");
					builder.append("WHERE EXISTS (SELECT 1 FROM BDCK.BDCS_QL_GZ QL ");
					builder.append("LEFT JOIN BDCK.BDCS_DJDY_GZ DJDY ON DJDY.DJDYID=QL.DJDYID ");
					builder.append("WHERE DJDY.BDCDYID=DY.BDCDYID AND (QL.ISCANCEL='0' OR QL.ISCANCEL='2') ");
					builder.append("AND QL.XMBH='"+xmbh+"')");
				}else if(BDCDYLX.SYQZD.Value.equals(bdcdylx)){
					builder.append("SELECT COUNT(*) AS GS,SUM(ZDMJ) AS MJ FROM BDCK.BDCS_SYQZD_XZ DY ");
					builder.append("WHERE EXISTS (SELECT 1 FROM BDCK.BDCS_QL_GZ QL ");
					builder.append("LEFT JOIN BDCK.BDCS_DJDY_GZ DJDY ON DJDY.DJDYID=QL.DJDYID ");
					builder.append("WHERE DJDY.BDCDYID=DY.BDCDYID AND (QL.ISCANCEL='0' OR QL.ISCANCEL='2') ");
					builder.append("AND QL.XMBH='"+xmbh+"')");
				}
			}
			
			String strGS="";
			String strMJ="";
			List<Map> lll=baseCommonDao.getDataListByFullSql(builder.toString());
			if(lll!=null&&lll.size()>0){
				strGS=StringHelper.formatObject(lll.get(0).get("GS"));
				double mj=StringHelper.getDouble(lll.get(0).get("MJ"));
				DecimalFormat df = new DecimalFormat("#########.##");
				df.setRoundingMode(RoundingMode.HALF_UP);
				strMJ = df.format(mj);
			}
			ms.setMsg("抵押单元个数为："+strGS+"；抵押单元总面积为："+strMJ);
			ms.setSuccess("true");
			ms.setTotal(count);
			ms.setRows(list);
		}
		return ms;
	}

	
	/**
	 * BG027权利页面单元过滤查询
	 * @author weilb
	 * @param xmbh
	 * @param request
	 * @param response
	 * @return json
	 */
	@SuppressWarnings({ "rawtypes", "rawtypes" })
	public Message dyFilterQuery(String xmbh, String bdcqzh,String  zl){
		Message msg = new Message();
		List<Map> list=new ArrayList<Map>();
		msg.setSuccess("false");
		msg.setTotal(0);
		ProjectInfo info=ProjectHelper.GetPrjInfoByXMBH(xmbh);
		if(info!=null){
			String bdcdylx="031";
			
			List<Map> listmap = new ArrayList<Map>();
			StringBuilder fullsql = new StringBuilder();
			fullsql.append(" SELECT QL.*,ROW_NUMBER() OVER(PARTITION BY QL.ISCANCEL ORDER BY QL.DJSJ DESC) ROW_NUMBER ")
			       .append(" FROM BDCK.BDCS_QL_GZ QL")
			       .append(" WHERE QL.XMBH='"+xmbh+"'");
			if(bdcqzh!=null && !bdcqzh.equals("")){
				/*权证号转换成djdyid进行过滤  
				/*因为传过来的权证号为现状层的上一手的权证号，所以利用QL表工作和现状的djdyid相同进行转换过滤*/
				List<BDCS_QL_XZ> ql_old = baseCommonDao.getDataList(BDCS_QL_XZ.class, " BDCQZH='"+bdcqzh+"'");
				if(ql_old!=null){
					fullsql.append(" AND QL.DJDYID='"+ql_old.get(0).getDJDYID()+"'");
				}
			}
			listmap = baseCommonDao.getDataListByFullSql(fullsql.toString());
			
			if(listmap!=null&&listmap.size()>0){
				for(Map ql:listmap){
					String djdyid=StringHelper.formatObject(ql.get("DJDYID"));
					String qlid=StringHelper.formatObject(ql.get("QLID"));
					List<BDCS_DJDY_GZ> djdys=baseCommonDao.getDataList(BDCS_DJDY_GZ.class, "DJDYID='"+djdyid+"' AND XMBH='"+ xmbh +"'");
					if(djdys!=null&&djdys.size()>0){
						String bdcdyid=djdys.get(0).getBDCDYID();
						bdcdylx=djdys.get(0).getBDCDYLX();
						RealUnit unit =UnitTools.loadUnit(BDCDYLX.initFrom(bdcdylx), DJDYLY.XZ, bdcdyid);
						
						if(unit!=null){
							HashMap<String,String> m=new HashMap<String, String>();
							String qzh = "";
							List<BDCS_QL_XZ> ql_xz = baseCommonDao.getDataList(BDCS_QL_XZ.class, " DJDYID='"+djdyid+"'");
							if(ql_xz!=null){
								qzh = StringHelper.formatObject(ql_xz.get(0).getBDCQZH());
							}
							if(zl!=null && !zl.equals("")){
								// 坐落过滤点
								if(zl.equals(StringHelper.formatObject(unit.getZL()))){
									m.put("ZL", StringHelper.formatObject(unit.getZL()));
									m.put("BDCDYH", StringHelper.formatObject(unit.getBDCDYH()));
									m.put("BDCDYLX", StringHelper.formatObject(unit.getBDCDYLX().Value));
									m.put("BDCDYID", StringHelper.formatObject(unit.getId()));
									m.put("BDCQZH",qzh);
									m.put("MJ", StringHelper.formatDouble(unit.getMJ()));
									if(BDCDYLX.H.Value.equals(bdcdylx)||BDCDYLX.YCH.Value.equals(bdcdylx)){
										House h=(House)unit;
										m.put("FH", h.getFH());
									}
									String iscancel="0";
									if(!StringHelper.isEmpty(StringHelper.formatObject(ql.get("ISCANCEL")))){
										iscancel=StringHelper.formatObject(ql.get("ISCANCEL"));
									}
									m.put("ISCANCEL", iscancel);
									m.put("QLID", qlid);
									list.add(m);
								}else{
									// 被过滤出去的情况不对 m 进行操作
								}
							}else{
								// 过滤条件为空，不进行过滤
								m.put("ZL", StringHelper.formatObject(unit.getZL()));
								m.put("BDCDYH", StringHelper.formatObject(unit.getBDCDYH()));
								m.put("BDCDYLX", StringHelper.formatObject(unit.getBDCDYLX().Value));
								m.put("BDCDYID", StringHelper.formatObject(unit.getId()));
								m.put("BDCQZH",qzh);
								m.put("MJ", StringHelper.formatDouble(unit.getMJ()));
								if(BDCDYLX.H.Value.equals(bdcdylx)||BDCDYLX.YCH.Value.equals(bdcdylx)){
									House h=(House)unit;
									m.put("FH", h.getFH());
								}
								String iscancel="0";
								if(!StringHelper.isEmpty(StringHelper.formatObject(ql.get("ISCANCEL")))){
									iscancel=StringHelper.formatObject(ql.get("ISCANCEL"));
								}
								m.put("ISCANCEL", iscancel);
								m.put("QLID", qlid);
								list.add(m);
							}
						}
					}
				}
			}
		}
		msg.setSuccess("true");
		msg.setRows(list);
		msg.setMsg("过滤成功");
		return msg;
	}
	
	/**
	 * 抵押变更添加一个登记单元（URL:"/{xmbh}/djdys/{bdcdyid}",Method：POST）
	 * 
	 * @param xmbh
	 * @param bdcdyid
	 * @param request
	 * @param response
	 * @author yuxuebin
	 * @return
	 */
	@Override
	public ResultMessage addBDCDY_DYBG(String xmbh, String bdcdyids) {
		ResultMessage msg=constraint_check.acceptCheckByBDCDYID(bdcdyids, xmbh);
		if("true".equals(msg.getSuccess())){
			msg.setMsg("成功");
			ProjectInfo info=ProjectHelper.GetPrjInfoByXMBH(xmbh);
			if (!StringHelper.isEmpty(bdcdyids)) {
				String[] ids = bdcdyids.split(",");
				for (int ibdcdyid = 0; ibdcdyid < ids.length; ibdcdyid++) {
					String id = ids[ibdcdyid];
					if (!StringHelper.isEmpty(id)) {
						BDCS_DJDY_GZ djdy = null;
						String hql_djdy = MessageFormat.format(" BDCDYID=''{0}'' ", id);
						List<BDCS_DJDY_XZ> list_djdy = baseCommonDao.getDataList(BDCS_DJDY_XZ.class, hql_djdy);
						if (list_djdy != null && list_djdy.size() > 0) {
							BDCS_DJDY_XZ xzdjdy = list_djdy.get(0);
							djdy = new BDCS_DJDY_GZ();
							djdy.setXMBH(xmbh);
							djdy.setDJDYID(xzdjdy.getDJDYID());
							djdy.setBDCDYID(id);
							djdy.setBDCDYLX(info.getBdcdylx());
							djdy.setBDCDYH(xzdjdy.getBDCDYH());
							djdy.setLY(DJDYLY.XZ.Value);
						}else{
							List<BDCS_DJDY_LS> list_djdy_ls = baseCommonDao.getDataList(BDCS_DJDY_LS.class, hql_djdy);
							if (list_djdy_ls != null && list_djdy_ls.size() > 0) {
								BDCS_DJDY_LS lsdjdy = list_djdy_ls.get(0);
								djdy = new BDCS_DJDY_GZ();
								djdy.setXMBH(xmbh);
								djdy.setDJDYID(lsdjdy.getDJDYID());
								djdy.setBDCDYID(id);
								djdy.setBDCDYLX(info.getBdcdylx());
								djdy.setBDCDYH(lsdjdy.getBDCDYH());
								djdy.setLY(DJDYLY.XZ.Value);
							}else{
								djdy = new BDCS_DJDY_GZ();
								String id_1=SuperHelper.GeneratePrimaryKey();
								String id_2=SuperHelper.GeneratePrimaryKey();
								djdy.setId(id_1);
								djdy.setXMBH(xmbh);
								djdy.setDJDYID(id_2);
								djdy.setBDCDYID(id);
								djdy.setBDCDYLX(info.getBdcdylx());
								djdy.setLY(DJDYLY.XZ.Value);
							}
							
						}
						if (djdy != null) {
							RealUnit unit = null;
							try {
								unit=UnitTools.loadUnit(BDCDYLX.initFrom(info.getBdcdylx()), DJDYLY.XZ, djdy.getBDCDYID());
							} catch (Exception e) {
							}
							// 生成权利信息
							BDCS_QL_GZ ql = new BDCS_QL_GZ();
							ql.setBDCDYH(djdy.getBDCDYH());
							ql.setCZFS(CZFS.FBCZ.Value);
							ql.setZSBS(ZSBS.DYB.Value);
							ql.setDJDYID(djdy.getDJDYID());
							ql.setDJLX(info.getDjlx());
							ql.setQLLX(info.getQllx());
							ql.setXMBH(xmbh);
							ql.setYWH(info.getProject_id());
							ql.setISCANCEL("2");

							try {
								if (unit != null) {
									djdy.setBDCDYH(unit.getBDCDYH());
									//如果现状预的户有值，则权利表里的不动产单元号改成从单元获取
									ql.setBDCDYH(unit.getBDCDYH());
									if (unit instanceof LandAttach) {
										LandAttach attach = (LandAttach) unit;
										unit = UnitTools.loadUnit(BDCDYLX.SHYQZD, DJDYLY.XZ, attach.getZDBDCDYID());
									}
								}
								if (unit != null) {
									ql.setQXDM(unit.getQXDM());
								}
							} catch (Exception ee) {
							}
							ql.setCZFS(CZFS.GTCZ.Value);
							
							String xzqhdm=ConfigHelper.getNameByValue("XZQHDM");
							if(!StringHelper.isEmpty(xzqhdm) && xzqhdm.startsWith("1301")){
								ql.setDJYY("借款");
							}
							
							// 生成附属权利
							BDCS_FSQL_GZ fsql = new BDCS_FSQL_GZ();
							fsql.setDJDYID(djdy.getDJDYID());
							fsql.setXMBH(xmbh);
							ql.setFSQLID(fsql.getId());
							fsql.setQLID(ql.getId());

							// 把附属权利里边的抵押人和抵押不动产类型写上
							String qllxarray = "('3','4','5','6','7','8')";
							String hql = "QLID IN(SELECT id FROM BDCS_QL_XZ WHERE  DJDYID='" + djdy.getDJDYID() + "' AND QLLX IN " + qllxarray + ") ORDER BY SXH";
							String lyqlid = "";
							List<BDCS_QLR_XZ> list = baseCommonDao.getDataList(BDCS_QLR_XZ.class, hql);
							if (list != null && list.size() > 0) {
								String qlrnames = "";
								lyqlid = list.get(0).getQLID();
								ql.setLYQLID(lyqlid);
								for (int i = 0; i < list.size(); i++) {
									qlrnames += list.get(i).getQLRMC() + ",";

									BDCS_QLR_XZ qlr = list.get(i);
									String zjhm = qlr.getZJH();
									boolean bexists = false;
									if (!StringHelper.isEmpty(qlr.getQLRMC())) {
										
										String Sql ="";
										if(!StringHelper.isEmpty(zjhm))
										{
										Sql=MessageFormat.format(" XMBH=''{0}'' AND SQRXM=''{1}'' AND ZJH=''{2}''", xmbh, qlr.getQLRMC(), zjhm);
										}
										else
										{
											Sql=MessageFormat.format(" XMBH=''{0}'' AND SQRXM=''{1}'' AND ZJH IS NULL", xmbh, qlr.getQLRMC());	
										}
										 
										List<BDCS_SQR> sqrlist = baseCommonDao.getDataList(BDCS_SQR.class, Sql);
										if (sqrlist != null && sqrlist.size() > 0) {
											bexists = true;
										}
									}
									// 判断申请人是否已经添加过，如果添加过，就不再添加
									if (!bexists) {
										String SQRID = SuperHelper.GeneratePrimaryKey();
										BDCS_SQR sqr = new BDCS_SQR();
										sqr.setGYFS(qlr.getGYFS());
										sqr.setFZJG(qlr.getFZJG());
										sqr.setGJDQ(qlr.getGJ());
										sqr.setGZDW(qlr.getGZDW());
										sqr.setXB(qlr.getXB());
										sqr.setHJSZSS(qlr.getHJSZSS());
										sqr.setSSHY(qlr.getSSHY());
										sqr.setYXBZ(qlr.getYXBZ());
										sqr.setQLBL(qlr.getQLBL());
										sqr.setQLMJ(StringHelper.formatObject(qlr.getQLMJ()));
										sqr.setSQRXM(qlr.getQLRMC());
										sqr.setSQRLB("2");
										sqr.setSQRLX(qlr.getQLRLX());
										sqr.setDZYJ(qlr.getDZYJ());
										sqr.setLXDH(qlr.getDH());
										sqr.setZJH(qlr.getZJH());
										sqr.setZJLX(qlr.getZJZL());
										sqr.setTXDZ(qlr.getDZ());
										sqr.setYZBM(qlr.getYB());
										sqr.setXMBH(xmbh);
										sqr.setId(SQRID);
										sqr.setGLQLID(ql.getId());
										sqr.setFDDBR(qlr.getFDDBR());
										sqr.setFDDBRDH(qlr.getFDDBRDH());
										sqr.setFDDBRZJLX(qlr.getFDDBRZJLX());
										sqr.setDLRXM(qlr.getDLRXM());
										sqr.setDLRZJLX(qlr.getDLRZJLX());
										sqr.setDLRZJHM(qlr.getDLRZJHM());
										//代理机构名称
										sqr.setDLJGMC(qlr.getDLJGMC());
										sqr.setDLRLXDH(qlr.getDLRLXDH());
										
										baseCommonDao.save(sqr);
									}
								}
								if (!StringUtils.isEmpty(qlrnames)) {
									qlrnames = qlrnames.substring(0, qlrnames.length() - 1);
									fsql.setDYR(qlrnames);
								}
							}
							//赋值变更中其他的权利信息
							List<BDCS_QL_GZ> qls=baseCommonDao.getDataList(BDCS_QL_GZ.class, "XMBH='"+xmbh+"'");
							if(qls!=null&&qls.size()>0){
								ql.setBZ(qls.get(0).getBZ());
								ql.setCZFS(qls.get(0).getCZFS());
								ql.setDJYY(qls.get(0).getDJYY());
								ql.setFJ(qls.get(0).getFJ());
								ql.setQLJSSJ(qls.get(0).getQLJSSJ());
								ql.setQLQSSJ(qls.get(0).getQLQSSJ());
								//是否设置bdcqzh
								String newqzh = "";
								String workflowcode = ProjectHelper.getWorkflowCodeByProjectID(info.getProject_id());
								List<WFD_MAPPING> listCode = baseCommonDao.getDataList(
										WFD_MAPPING.class, "WORKFLOWCODE='" + workflowcode + "'");
								if (listCode != null && listCode.size() > 0) {
									newqzh = listCode.get(0).getNEWQZH();
								}								
								if (SF.NO.Value.equals(newqzh)) {
									ql.setBDCQZH(qls.get(0).getBDCQZH());
									ql.setBDCQZHXH(qls.get(0).getBDCQZHXH());
								}
								String fsqlid_oo=qls.get(0).getFSQLID();
								if(!StringHelper.isEmpty(fsqlid_oo)){
									BDCS_FSQL_GZ fsql_oo=baseCommonDao.get(BDCS_FSQL_GZ.class, fsqlid_oo);
									if(fsql_oo!=null){
										fsql.setZJJZWZL(fsql_oo.getZJJZWZL());
										fsql.setDYFS(fsql_oo.getDYFS());
										fsql.setBDBZZQSE(fsql_oo.getBDBZZQSE());
										fsql.setZGZQSE(fsql_oo.getZGZQSE());
										fsql.setZJJZWDYFW(fsql_oo.getZJJZWDYFW());
										fsql.setDYPGJZ(fsql_oo.getDYPGJZ());
										fsql.setZGZQQDSS(fsql_oo.getZGZQQDSS());
										fsql.setDYWLX(fsql_oo.getDYWLX());
									}
								}
								// 获取证书集合
								StringBuilder builderZSALL = new StringBuilder();
								builderZSALL.append(" id IN (");
								builderZSALL.append("select ZSID FROM BDCS_QDZR_GZ WHERE QLID ='");
								builderZSALL.append(qls.get(0).getId()).append("')");
								String strQueryZSALL = builderZSALL.toString();
								List<BDCS_ZS_GZ> zssALL = baseCommonDao.getDataList(BDCS_ZS_GZ.class, strQueryZSALL);
								// 当证书个数大于1时，持证方式为分别持证，否则为共同持证
								if (zssALL.size() > 1) {
									ql.setCZFS(CZFS.FBCZ.Value);
								} else {
									if (StringHelper.isEmpty(ql.getCZFS()) || (!ql.getCZFS().equals(CZFS.FBCZ.Value) && !ql.getCZFS().equals(CZFS.GTCZ.Value))) {
										ql.setCZFS(CZFS.GTCZ.Value);
									}
								}
								Map<String, String> lyzsid_zsid = new HashMap<String, String>();

								// 获取权利人集合
								List<BDCS_QLR_GZ> qlrs = baseCommonDao.getDataList(BDCS_QLR_GZ.class, "QLID='"+qls.get(0).getId()+"'");
								if (qlrs != null && qlrs.size() > 0) {
									for (int iqlr = 0; iqlr < qlrs.size(); iqlr++) {
										BDCS_QLR_GZ bdcs_qlr_xz = qlrs.get(iqlr);
										if (bdcs_qlr_xz != null) {
											// 拷贝权利人
											String gzqlrid = SuperHelper.GeneratePrimaryKey();
											BDCS_QLR_GZ bdcs_qlr_gz = new BDCS_QLR_GZ();
											try {
												PropertyUtils.copyProperties(bdcs_qlr_gz, bdcs_qlr_xz);
											} catch (Exception e) {
											}
											bdcs_qlr_gz.setId(gzqlrid);
											bdcs_qlr_gz.setQLID(ql.getId());
											baseCommonDao.save(bdcs_qlr_gz);
											// 获取证书集合
											StringBuilder builder = new StringBuilder();
											builder.append(" id IN (");
											builder.append("select ZSID FROM BDCS_QDZR_GZ WHERE QLID ='");
											builder.append(qls.get(0).getId()).append("'").append(" AND QLRID='");
											builder.append(bdcs_qlr_xz.getId()).append("')");
											String strQueryZS = builder.toString();
											List<BDCS_ZS_GZ> zss = baseCommonDao.getDataList(BDCS_ZS_GZ.class, strQueryZS);
											if (zss != null && zss.size() > 0) {
												BDCS_ZS_GZ bdcs_zs_xz = zss.get(0);
												if (!lyzsid_zsid.containsKey(bdcs_zs_xz.getId())) {
													String gzzsid = SuperHelper.GeneratePrimaryKey();
													BDCS_ZS_GZ bdcs_zs_gz = new BDCS_ZS_GZ();
													try {
														PropertyUtils.copyProperties(bdcs_zs_gz, bdcs_zs_xz);
													} catch (Exception e) {
													}
													bdcs_zs_gz.setId(gzzsid);
													baseCommonDao.save(bdcs_zs_gz);
													lyzsid_zsid.put(bdcs_zs_xz.getId(), gzzsid);
												}
												// 获取权地证人集合
												StringBuilder builderQDZR = new StringBuilder();
												builderQDZR.append(" DJDYID='"+qls.get(0).getDJDYID()+"'");
												builderQDZR.append(" AND ZSID='");
												builderQDZR.append(bdcs_zs_xz.getId());
												builderQDZR.append("' AND QLID='");
												builderQDZR.append(qls.get(0).getId());
												builderQDZR.append("' AND QLRID='");
												builderQDZR.append(bdcs_qlr_xz.getId());
												builderQDZR.append("')");
												List<BDCS_QDZR_GZ> qdzrs = baseCommonDao.getDataList(BDCS_QDZR_GZ.class, builderQDZR.toString());
												if (qdzrs != null && qdzrs.size() > 0) {
													for (int iqdzr = 0; iqdzr < qdzrs.size(); iqdzr++) {
														BDCS_QDZR_GZ bdcs_qdzr_xz = qdzrs.get(iqdzr);
														if (bdcs_qdzr_xz != null) {
															// 拷贝权地证人
															BDCS_QDZR_GZ bdcs_qdzr_gz = new BDCS_QDZR_GZ();
															try {
																PropertyUtils.copyProperties(bdcs_qdzr_gz, bdcs_qdzr_xz);
															} catch (Exception e) {
															}
															String qdzrid=SuperHelper.GeneratePrimaryKey();
															bdcs_qdzr_gz.setId(qdzrid);
															bdcs_qdzr_gz.setDJDYID(djdy.getDJDYID());
															bdcs_qdzr_gz.setZSID(lyzsid_zsid.get(bdcs_zs_xz.getId()));
															bdcs_qdzr_gz.setQLID(ql.getId());
															bdcs_qdzr_gz.setFSQLID(fsql.getId());
															bdcs_qdzr_gz.setBDCDYH(unit.getBDCDYH());
															bdcs_qdzr_gz.setQLRID(gzqlrid);
															baseCommonDao.save(bdcs_qdzr_gz);
														}
													}
												}
											}
										}
									}
								}
							}
							// 保存
							baseCommonDao.save(djdy);
							baseCommonDao.save(ql);
							baseCommonDao.save(fsql);
						}
						baseCommonDao.flush();
					}
				}
			}
		}
		
		return msg;
	}

	/**
	 * 获取发证单元列表信息
	 * 
	 * @param xmbh
	 * @author yuxuebin
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Message GetFzDys(String xmbh,String qllx) {
		Message m=new Message();
		ProjectInfo info=ProjectHelper.GetPrjInfoByXMBH(xmbh);
		if(info!=null){
			String dyName_GZ=BDCDYLX.initFrom(info.getBdcdylx()).GZTableName;
			String dyName_XZ=BDCDYLX.initFrom(info.getBdcdylx()).XZTableName;
			if(!StringHelper.isEmpty(qllx) && !qllx.toLowerCase().equals("undefined")){
				StringBuilder builder_gz=new StringBuilder();
				String groupid="DJDY.GROUPID";
				if(QLLX.DIYQ.Value.equals(qllx)){
					groupid="QL.GROUPID";
				}
				builder_gz.append("SELECT QL.QLID,"+groupid +",DJDY.XMBH,DJDY.BDCDYID,DJDY.DJDYID,DY.ZL,DY.BDCDYH,FSQL.DYDYJXZ ");
				builder_gz.append("FROM BDCK.BDCS_DJDY_GZ DJDY LEFT JOIN BDCK.");
				builder_gz.append(dyName_GZ);
				builder_gz.append(" DY ON DY.BDCDYID=DJDY.BDCDYID ");
				builder_gz.append(" LEFT JOIN BDCK.BDCS_QL_GZ QL  ON DJDY.DJDYID =QL.DJDYID  ");
				builder_gz.append(" LEFT JOIN BDCK.BDCS_FSQL_GZ FSQL ON FSQL.QLID=QL.QLID ");
				builder_gz.append("WHERE DJDY.XMBH='");
				builder_gz.append(xmbh);
				builder_gz.append("' AND DY.XMBH='");
				builder_gz.append(xmbh);
				builder_gz.append("' AND QL.QLLX='");
				builder_gz.append(qllx);
				builder_gz.append("' AND QL.XMBH='");
				builder_gz.append(xmbh);
				builder_gz.append("' AND EXISTS(SELECT 1 FROM BDCK.BDCS_QL_GZ WHERE DJDYID=DJDY.DJDYID AND XMBH='");
				builder_gz.append(xmbh);
				builder_gz.append("')");
				List<Map> list=baseCommonDao.getDataListByFullSql(builder_gz.toString());
				if(list==null||list.size()<=0){
					StringBuilder builder_xz=new StringBuilder();
					builder_xz.append("SELECT QL.QLID,"+groupid +",DJDY.XMBH,DJDY.BDCDYID,DJDY.DJDYID,DY.ZL,DY.BDCDYH,FSQL.DYDYJXZ ");
					builder_xz.append("FROM BDCK.BDCS_DJDY_GZ DJDY LEFT JOIN BDCK.");
					builder_xz.append(dyName_XZ);
					builder_xz.append(" DY ON DY.BDCDYID=DJDY.BDCDYID ");
					builder_xz.append(" LEFT JOIN BDCK.BDCS_QL_GZ QL ON QL.DJDYID = DJDY.DJDYID ");
					builder_xz.append(" LEFT JOIN BDCK.BDCS_FSQL_GZ FSQL  ON QL.QLID =FSQL.QLID AND FSQL.XMBH='");
					builder_xz.append(xmbh);
					builder_xz.append("'  WHERE DJDY.XMBH='");
					builder_xz.append(xmbh);
					builder_xz.append("' AND QL.QLLX='");
					builder_xz.append(qllx);
					builder_xz.append("' AND QL.XMBH='");
					builder_xz.append(xmbh);
					builder_xz.append("' AND EXISTS(SELECT 1 FROM BDCK.BDCS_QL_GZ WHERE DJDYID=DJDY.DJDYID AND XMBH='");
					builder_xz.append(xmbh);
					builder_xz.append("')");
					list=baseCommonDao.getDataListByFullSql(builder_xz.toString());
				}
				m.setMsg("获取成功！");
				m.setSuccess("true");
				m.setRows(list);
				m.setTotal(list.size());
			}else{
				StringBuilder builder_gz=new StringBuilder();
				builder_gz.append("SELECT DJDY.GROUPID,DJDY.XMBH,DJDY.BDCDYID,DJDY.DJDYID,DY.ZL,DY.BDCDYH,FSQL.DYDYJXZ ");
				builder_gz.append("FROM BDCK.BDCS_DJDY_GZ DJDY LEFT JOIN BDCK.");
				builder_gz.append(dyName_GZ);
				builder_gz.append(" DY ON DY.BDCDYID=DJDY.BDCDYID ");
				builder_gz.append(" LEFT JOIN BDCK.BDCS_FSQL_GZ FSQL  ON DJDY.DJDYID =FSQL.DJDYID  ");
				builder_gz.append("WHERE DJDY.XMBH='");
				builder_gz.append(xmbh);
				builder_gz.append("' AND DY.XMBH='");
				builder_gz.append(xmbh);
				builder_gz.append("' AND EXISTS(SELECT 1 FROM BDCK.BDCS_QL_GZ WHERE DJDYID=DJDY.DJDYID AND XMBH='");
				builder_gz.append(xmbh);
				builder_gz.append("')");
				List<Map> list=baseCommonDao.getDataListByFullSql(builder_gz.toString());
				if(list==null||list.size()<=0){
					StringBuilder builder_xz=new StringBuilder();
					builder_xz.append("SELECT DJDY.GROUPID,DJDY.XMBH,DJDY.BDCDYID,DJDY.DJDYID,DY.ZL,DY.BDCDYH,FSQL.DYDYJXZ ");
					builder_xz.append("FROM BDCK.BDCS_DJDY_GZ DJDY LEFT JOIN BDCK.");
					builder_xz.append(dyName_XZ);
					builder_xz.append(" DY ON DY.BDCDYID=DJDY.BDCDYID ");
					builder_xz.append(" LEFT JOIN BDCK.BDCS_FSQL_GZ FSQL  ON DJDY.DJDYID =FSQL.DJDYID AND FSQL.XMBH='");
					builder_xz.append(xmbh);
					builder_xz.append("'  WHERE DJDY.XMBH='");
					builder_xz.append(xmbh);
					builder_xz.append("' AND EXISTS(SELECT 1 FROM BDCK.BDCS_QL_GZ WHERE DJDYID=DJDY.DJDYID AND XMBH='");
					builder_xz.append(xmbh);
					builder_xz.append("')");
					list=baseCommonDao.getDataListByFullSql(builder_xz.toString());
				}
				m.setMsg("获取成功！");
				m.setSuccess("true");
				m.setRows(list);
				m.setTotal(list.size());
			}
			
		}
		return m;
	}

	/**
	 * 更正发证单元分组标识
	 * 
	 * @param xmbh
	 * @param m
	 * @author yuxuebin
	 * @return
	 */
	@Override
	public ResultMessage UpdateFzDys(String xmbh, HashMap<String, Integer> m,String qllx) {
		ResultMessage ms=new ResultMessage();
		if(!StringHelper.isEmpty(qllx) && !qllx.toLowerCase().equals("undefined") && QLLX.DIYQ.Value.equals(qllx)){
		 List<BDCS_QL_GZ> lstqls=baseCommonDao.getDataList(BDCS_QL_GZ.class, "XMBH='"+xmbh+"' AND QLLX='"+23+"'");
		 if(lstqls !=null && lstqls.size()>0){
			 for(BDCS_QL_GZ ql :lstqls){
				 String qlid=ql.getId();
				 BDCS_FSQL_GZ subrights= (BDCS_FSQL_GZ)RightsTools.loadSubRights(DJDYLY.GZ, ql.getFSQLID()) ;
				 if(m.containsKey(qlid)){
					 Integer groupid=m.get(qlid);
					 Integer dydyjxz=m.get("dydyjxz");
					 if(!StringHelper.isEmpty(groupid) && ! groupid.equals(ql.getGROUPID())){
						 ql.setGROUPID(groupid);
							baseCommonDao.update(ql);
					 }
					 if(!StringHelper.isEmpty(dydyjxz)&&!dydyjxz.equals(subrights.getDYDYJXZ())){
						 subrights.setDYDYJXZ(dydyjxz);
							baseCommonDao.update(subrights);
						}
				 }
			 }
		 }
		 List<BDCS_DJDY_GZ> djdys=baseCommonDao.getDataList(BDCS_DJDY_GZ.class, "XMBH='"+xmbh+"'");
			if(djdys!=null&&djdys.size()>0){
				for(BDCS_DJDY_GZ djdy:djdys){
					String djdyid=djdy.getDJDYID();
					if(m.containsKey(djdyid)){
						Integer groupid=m.get(djdyid);
						Integer dydyjxz=m.get("dydyjxz");
						if(!StringHelper.isEmpty(groupid)&&!groupid.equals(djdy.getGROUPID())){
							djdy.setGROUPID(groupid);
							baseCommonDao.update(djdy);
						}
					}
				}
			}
		 ms.setMsg("保存成功！");
			ms.setSuccess("true");
		}else{
			List<BDCS_DJDY_GZ> djdys=baseCommonDao.getDataList(BDCS_DJDY_GZ.class, "XMBH='"+xmbh+"'");
			if(djdys!=null&&djdys.size()>0){
				for(BDCS_DJDY_GZ djdy:djdys){
					//BDCS_FSQL_GZ fsql = baseCommonDao.getDataList(BDCS_FSQL_GZ.class, "XMBH='"+xmbh+"'and DJDYID='"+djdy.getDJDYID()+"'").get(0);
					//2018年5月24日12:13:09 huangmingh 
					BDCS_FSQL_GZ fsql = baseCommonDao.getDataList(BDCS_FSQL_GZ.class, "XMBH='"+xmbh+"'").get(0);
					String djdyid=djdy.getDJDYID();
					if(m.containsKey(djdyid)){
						Integer groupid=m.get(djdyid);
						Integer dydyjxz=m.get("dydyjxz");
						if(!StringHelper.isEmpty(groupid)&&!groupid.equals(djdy.getGROUPID())){
							djdy.setGROUPID(groupid);
							baseCommonDao.update(djdy);
						}
						if(!StringHelper.isEmpty(dydyjxz)&&!dydyjxz.equals(fsql.getDYDYJXZ())){
							fsql.setDYDYJXZ(dydyjxz);
							baseCommonDao.update(fsql);
						}
					}
				}
			}
			ms.setMsg("保存成功！");
			ms.setSuccess("true");
		}
		return ms;
	}
	/**
	 * 获取对应自然幢（实测\预测）中的地上层数及地下层数（石家庄需要）
	 * @作者 海豹
	 * @创建时间 2016年3月28日下午3:18:10
	 * @param xmbh，为了获取想对应的流程
	 * @param djdyly,为了确定单元来源（GZ,XZ）
	 * @param bdcdylx，为了确定单元对应的自然幢信息
	 * @param unit，设置地下层数及地上层数
	 * @return
	 */
	@Override
	public RealUnit  getZrzUnit(String xmbh,String djdyly, String  bdcdylx, RealUnit unit)
	{
		String xzqhdm=ConfigHelper.getNameByValue("XZQHDM");
		if(xzqhdm.contains("130"))
		{
			BDCDYLX lx=BDCDYLX.initFrom(bdcdylx);
			 if(BDCDYLX.H.equals(lx)||BDCDYLX.YCH.equals(lx))
			 {							
					DJDYLY zrzly=DJDYLY.initFrom(djdyly);
					if(!StringHelper.isEmpty(xmbh)){
						ProjectInfo info=ProjectHelper.GetPrjInfoByXMBH(xmbh);
						if(!StringHelper.isEmpty(info))
						{
							String djlx=info.getDjlx();
							if(DJLX.BGDJ.Value.equals(djlx) || DJLX.GZDJ.Value.equals(djlx))
							{
								zrzly=DJDYLY.XZ;
							}
						}
					}
				House h=(House) unit;
				if(!StringHelper.isEmpty(h.getZRZBDCDYID())){
					BDCDYLX zrzlx=BDCDYLX.ZRZ;
					if(BDCDYLX.YCH.equals(lx))
					{
						zrzlx=BDCDYLX.YCZRZ;
					}
				 Building zrz=(Building)UnitTools.loadUnit(zrzlx, zrzly, h.getZRZBDCDYID());	
				 if(!StringHelper.isEmpty(zrz))
				 {
				 h.setDscs_zrz(zrz.getDSCS());
				 h.setDxcs_zrz(zrz.getDXCS());
				 }
				 unit =h;
				}
			 }			
		}
		return unit;
	}
	/**
	 * 更新自然幢（实测\预测）中的地上层数及地下层数（石家庄需要）
	 * @作者 海豹
	 * @创建时间 2016年3月28日下午3:18:10
	 * @param xmbh，为了获取相对应的流程
	 * @param ly,为了确定单元来源（GZ,XZ）
	 * @param lx，为了确定单元对应的自然幢信息
	 * @param bdcdyid，获取户的信息
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void UpdateZrzUnit(Map map,String xmbh,DJDYLY ly, BDCDYLX  lx,String bdcdyid)
	{
		String xzqhdm=ConfigHelper.getNameByValue("XZQHDM");
		if(xzqhdm.contains("130"))
		{
			 if(BDCDYLX.H.equals(lx)||BDCDYLX.YCH.equals(lx))
			 {							
					BDCDYLX zrzlx=BDCDYLX.ZRZ;
					ProjectInfo info=ProjectHelper.GetPrjInfoByXMBH(xmbh);
					String zrzbdcdyid="";
					int dxcs = StringHelper.getInt(map.get("dxcs_zrz"));
					int dscs = StringHelper.getInt(map.get("dscs_zrz"));
					Map m=new HashMap();
					m.put("dscs", dscs);
					m.put("dxcs", dxcs);
					if(!StringHelper.isEmpty(info))
					{
						String djlx=info.getDjlx();
						House h=(House) UnitTools.loadUnit(lx, ly, bdcdyid);
						if(!StringHelper.isEmpty(h) && !StringHelper.isEmpty(h.getZRZBDCDYID())){
							if(BDCDYLX.YCH.equals(lx))
							{
								zrzlx=BDCDYLX.YCZRZ;
							}
							zrzbdcdyid=h.getZRZBDCDYID();
						}
						else 
						{
							return;
						}
						if(DJLX.CSDJ.Value.equals(djlx))//如果是初始登记只更新工作层，其它的现在及历史层
						{
							 UpdateBDCDYInfo(m,zrzlx,DJDYLY.GZ,zrzbdcdyid);
						}
						else 
						{
							 UpdateBDCDYInfo(m,zrzlx,DJDYLY.XZ,zrzbdcdyid);
							 UpdateBDCDYInfo(m,zrzlx,DJDYLY.LS,zrzbdcdyid);
						}
					}
				
			 }			
		}
	}

	//选择单元后，先将casenum设为null，否则会继承上一手的casenum
	//一个项目可能多次抽取，每次只置空指定bdcdyid的那一条
	//单元选择器中bdcdyid返回的可能是 不动产单元ID 或 权利ID 或 不动产权证号！
	private void setCasenumNull(String xmbh, String bdcdyid) {
		try{
		String[] ids = bdcdyid.split(",");
		if (ids != null && ids.length > 0) {
			for (String id : ids) {
				String sql="bdcdyid='"+id+"'";
				List<BDCS_DJDY_GZ> djdy_GZs=baseCommonDao.getDataList(BDCS_DJDY_GZ.class, sql);
				if(djdy_GZs!=null&&djdy_GZs.size()>0){
							List<BDCS_QL_GZ> qlList = baseCommonDao.getDataList(
							   BDCS_QL_GZ.class, " xmbh = '" + xmbh + "' and djdyid='"+djdy_GZs.get(0).getDJDYID()+"'");
							if(qlList!=null &&qlList.size()>0){
								BDCS_QL_GZ ql_GZ=qlList.get(0);
								ql_GZ.setCASENUM("");
								baseCommonDao.update(ql_GZ);
							}
							
				}else{
					sql="qlid='"+id+"' or bdcqzh ='"+id+"'";
					List<BDCS_QL_XZ> ql_XZs=baseCommonDao.getDataList(BDCS_QL_XZ.class, sql);
					if(ql_XZs!=null && ql_XZs.size()>0){
						List<BDCS_QL_GZ> qlList = baseCommonDao.getDataList(
								   BDCS_QL_GZ.class, " xmbh = '" + xmbh + "' and djdyid='"+ql_XZs.get(0).getDJDYID()+"'");
								if(qlList!=null &&qlList.size()>0){
									BDCS_QL_GZ ql_GZ=qlList.get(0);
									ql_GZ.setCASENUM("");
									baseCommonDao.update(ql_GZ);
								}
					}
				}
		}
		}
		}catch(Exception ex){
		}
	}
}
