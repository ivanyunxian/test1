package com.supermap.realestate.registration.service.impl;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Column;
import javax.servlet.http.HttpServletRequest;

import com.supermap.realestate.registration.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.supermap.realestate.registration.ViewClass.ProjectInfo;
import com.supermap.realestate.registration.factorys.HandlerFactory;
import com.supermap.realestate.registration.handler.DJHandler;
import com.supermap.realestate.registration.mapping.HandlerMapping.RegisterWorkFlow;
import com.supermap.realestate.registration.model.interfaces.House;
import com.supermap.realestate.registration.model.interfaces.RealUnit;
import com.supermap.realestate.registration.model.interfaces.Rights;
import com.supermap.realestate.registration.model.interfaces.RightsHolder;
import com.supermap.realestate.registration.model.interfaces.SubRights;
import com.supermap.realestate.registration.service.QLService;
import com.supermap.realestate.registration.tools.NewLogTools;
import com.supermap.realestate.registration.tools.RightsHolderTools;
import com.supermap.realestate.registration.tools.RightsTools;
import com.supermap.realestate.registration.tools.UnitTools;
import com.supermap.realestate.registration.util.ConfigHelper;
import com.supermap.realestate.registration.util.ConstHelper;
import com.supermap.realestate.registration.util.ConstValue;
import com.supermap.realestate.registration.util.ConstValue.BDCDYLX;
import com.supermap.realestate.registration.util.ConstValue.CZFS;
import com.supermap.realestate.registration.util.ConstValue.DJDYLY;
import com.supermap.realestate.registration.util.ConstValue.SF;
import com.supermap.realestate.registration.util.DateUtil;
import com.supermap.realestate.registration.util.ObjectHelper;
import com.supermap.realestate.registration.util.ProjectHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.realestate.registration.util.YwLogUtil;
import com.supermap.wisdombusiness.core.SuperHelper;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import com.supermap.wisdombusiness.web.ResultMessage;
import com.supermap.wisdombusiness.web.ui.Page;
import com.supermap.wisdombusiness.web.Message;

@Service("qlService")
public class QLServiceImpl implements QLService {

	@Autowired
	private CommonDao baseCommonDao;

	/**
	 * 根据权利ID获取权利(刘树峰 2015.9.12备注，正在使用)
	 */
	@Override
	public BDCS_QL_GZ GetQL(String qlid) {
		BDCS_QL_GZ ql = baseCommonDao.get(BDCS_QL_GZ.class, qlid);
		return ql;
	}

	/**
	 * 根据附属权利ID获取附属权利信息(刘树峰 2015.9.12备注，正在使用)
	 */
	@Override
	public BDCS_FSQL_GZ GetFSQL(String fsqlid) {
		BDCS_FSQL_GZ fsql = baseCommonDao.get(BDCS_FSQL_GZ.class, fsqlid);
		return fsql;
	}

	/**
	 * 更新权利信息，只保存权利信息，不重新构建关系
	 * @Title: UpdateQL
	 * @author:liushufeng
	 * @date：2015年8月7日 下午11:53:16
	 * @param ql
	 */
	@Override
	public void UpdateQL(BDCS_QL_GZ ql) {
		baseCommonDao.update(ql);
		baseCommonDao.flush();
	}

	/**
	 * 更新权利信息，保存权利信息的同时，根据持证方式重新构建证书关系(刘树峰 2015.9.12备注，正在使用）
	 */
	@Override
	public void UpdateQLandRebuildRelation(BDCS_QL_GZ ql) {

		// sunhb-2015-06-30 删除证书，应该删除该权利ID下的证书，而不是通过项目编号删除所有证书
		String str1 = MessageFormat.format("select  distinct zsid  from BDCK.BDCS_QDZR_GZ WHERE XMBH=''{0}'' and qlid=''{1}''", ql.getXMBH(), ql.getId());
		@SuppressWarnings("rawtypes")
		List<Map> lstMap = baseCommonDao.getDataListByFullSql(str1);
		String str = MessageFormat.format("XMBH=''{0}'' and qlid=''{1}''", ql.getXMBH(), ql.getId());
		List<BDCS_QDZR_GZ> list1 = baseCommonDao.getDataList(BDCS_QDZR_GZ.class, str);
		for (@SuppressWarnings("rawtypes")
		Map qdzr : lstMap) {
			if (!StringUtils.isEmpty(qdzr.get("ZSID"))) {
				baseCommonDao.delete(BDCS_ZS_GZ.class, qdzr.get("ZSID").toString());// 删除证书信息
			}
		}
		String zsid1 = (String) SuperHelper.GeneratePrimaryKey();
		if (list1 != null && list1.size() > 0) {
			for (BDCS_QDZR_GZ qdzr : list1)// 重新构建证书信息
			{
				if (ql.getCZFS().equals(CZFS.FBCZ.Value)) {
					BDCS_ZS_GZ zs = new BDCS_ZS_GZ();
					zs.setXMBH(ql.getXMBH());
					qdzr.setZSID(zs.getId());
					baseCommonDao.save(zs);
				} else {
					qdzr.setZSID(zsid1);
				}
				baseCommonDao.saveOrUpdate(qdzr);
			}
			if (ql.getCZFS().equals(CZFS.GTCZ.Value)) {
				BDCS_ZS_GZ zs = new BDCS_ZS_GZ();
				zs.setXMBH(ql.getXMBH());
				zs.setId(zsid1);
				baseCommonDao.save(zs);
			}
		}
		//删除证书之后，还应该把权利表里边的不动产权证号也清空掉
		ql.setBDCQZH("");
		baseCommonDao.update(ql);
		baseCommonDao.flush();
	}

	/**
	 * 更新附属权利信息(刘树峰 2015.9.12备注，只在更新林地使用权的时候使用了)
	 *
	 * @作者 海豹
	 * @创建时间 2015年6月27日下午11:57:10
	 * @param fsql
	 */
	@Override
	public void UpdateFSQL(BDCS_FSQL_GZ fsql) {
		baseCommonDao.update(fsql);
		baseCommonDao.flush();
	}

	/**
	 * 根据权利人ID获取权利人信息(刘树峰 2015.9.12备注，只有一个地方使用，可以用工具类里边的方法代替)
	 * @Title: GetQLRInfo
	 * @author:liushufeng
	 * @date：2015年8月12日 上午3:12:36
	 * @param qlrid
	 * @return
	 */
	@Override
	public BDCS_QLR_GZ GetQLRInfo(String qlrid) {
		return baseCommonDao.get(BDCS_QLR_GZ.class, qlrid);
	}

	/**
	 * 根据权利ID和项目编号获取权利人列表(刘树峰 2015.9.12备注，只有一个地方使用，可以用工具类里边的方法代替)
	 */
	@Override
	public Page GetPagedQLR(String xmbh, String qlid, Integer page, Integer rows) {
		StringBuilder builder = new StringBuilder();
		Page p = new Page();
		if (!"null".equals(qlid) ) {
			builder.append(" QLID='").append(qlid).append("' ORDER BY SXH");
			 p = baseCommonDao.getPageDataByHql(BDCS_QLR_GZ.class, builder.toString(), page, rows);
		}
		
		return p;
	}

	/**
	 * 更新权利人信息(刘树峰 2015.9.12备注，只有一个地方使用，将来可以添加到工具类方法里边)
	 */
	@Override
	public void UpdateQLR(BDCS_QLR_GZ qlr) {
		String sqrid=qlr.getSQRID();
		if(!StringHelper.isEmpty(sqrid)){
			BDCS_SQR sqr=baseCommonDao.get(BDCS_SQR.class, sqrid);
			if(sqr!=null){
				sqr.setSXH(StringHelper.formatObject(qlr.getSXH()));
				sqr.setNATION(StringHelper.formatObject(qlr.getNATION()));
			}
		}
		baseCommonDao.update(qlr);
		baseCommonDao.getCurrentSession().flush();
	}

	/**
	 * 获取林地所对应的权利及权利附属合并后的信息(刘树峰 2015.9.12备注，将来统一换掉）
	 * @作者 海豹
	 * @创建时间 2015年6月28日上午12:27:43
	 * @param qlid
	 * @return
	 */
	@Override
	public Map<String, String> GetQlandFsqlInfo(String qlid) {
		BDCS_QL_GZ ql = baseCommonDao.get(BDCS_QL_GZ.class, qlid);
		Map<String, String> map = new HashMap<String, String>();
		String qssj = "";// 起始时间
		String jssj = "";// 结束时间
		String czfs = "";// 持证方式
		String zsbs = "";// 证书版式
		String djyy = "";// 登记原因
		String fj = "";// 附记
		String ldsyqxz = "";// 林地所有权性质
		String ldsyqr = "";// 森林林木所有权人
		String ldshyqr = "";// 森林林木使用权人
		String fbf = "";// 发包方
		String gyrqk = "";// 共有人情况

		if (ql != null) {
			BDCS_FSQL_GZ fsql = baseCommonDao.get(BDCS_FSQL_GZ.class, ql.getFSQLID());
			if (fsql != null) {
				if (fsql.getLDSYQXZ() != null) {
					ldsyqxz = fsql.getLDSYQXZ().toString();
				}
				if (fsql.getSLLMSYQR1() != null) {
					ldsyqr = fsql.getSLLMSYQR1();
				}
				if (fsql.getSLLMSYQR2() != null) {
					ldshyqr = fsql.getSLLMSYQR2().toString();
				}
				if(fsql.getFBF()!=null){
					fbf=fsql.getFBF();
				}
				if(fsql.getGYRQK()!=null){
					gyrqk=fsql.getGYRQK();
				}
			}
			if (ql.getQLQSSJ() != null) {
				qssj = ql.getQLQSSJ().toString();
			}
			if (ql.getQLJSSJ() != null) {
				jssj = ql.getQLJSSJ().toString();
			}
			if (ql.getCZFS() != null) {
				czfs = ql.getCZFS().toString();
			}
			if (ql.getDJYY() != null) {
				djyy = ql.getDJYY().toString();
			}
			if (ql.getFJ() != null) {
				fj = ql.getFJ().toString();
			}
			if (ql.getZSBS() != null) {
				zsbs = ql.getZSBS().toString();
			}
		}
		map.put("qssj", qssj);
		map.put("jssj", jssj);
		map.put("ldshyqr", ldshyqr);
		map.put("ldsyqxz", ldsyqxz);
		map.put("ldsyqr", ldsyqr);
		map.put("czfs", czfs);
		map.put("djyy", djyy);
		map.put("fj", fj);
		map.put("zsbs", zsbs);
		map.put("fbf", fbf);//发包方
		map.put("gyrqk", gyrqk);//共有人情况
		return map;
	}

	@Override
	public Map<String, String> getXZForestRightsInfo(String qlid) {
		BDCS_QL_LS ql = baseCommonDao.get(BDCS_QL_LS.class, qlid);
		Map<String, String> map = new HashMap<String, String>();
		String qssj = "";// 起始时间
		String jssj = "";// 结束时间
		String czfs = "";// 持证方式
		String zsbs = "";// 证书版式
		String djyy = "";// 登记原因
		String fj = "";// 附记
		String ldsyqxz = "";// 林地所有权性质
		String ldsyqr = "";// 森林林木所有权人
		String ldshyqr = "";// 森林林木使用权人
		String fbf = "";// 发包方
		String gyrqk = "";// 共有人情况
		String bdcqzh=""; //不动产权证号
		if (ql != null) {
			BDCS_FSQL_LS fsql = baseCommonDao.get(BDCS_FSQL_LS.class, ql.getFSQLID());
			if (fsql != null) {
				if (fsql.getLDSYQXZ() != null) {
					ldsyqxz = fsql.getLDSYQXZ().toString();
				}
				if (fsql.getSLLMSYQR1() != null) {
					ldsyqr = fsql.getSLLMSYQR1();
				}
				if (fsql.getSLLMSYQR2() != null) {
					ldshyqr = fsql.getSLLMSYQR2().toString();
				}
				if(fsql.getFBF()!=null){
					fbf=fsql.getFBF();
				}
				if(fsql.getGYRQK()!=null){
					gyrqk=fsql.getGYRQK();
				}
			}
			if (ql.getQLQSSJ() != null) {
				qssj = ql.getQLQSSJ().toString();
			}
			if (ql.getQLJSSJ() != null) {
				jssj = ql.getQLJSSJ().toString();
			}
			if (ql.getCZFS() != null) {
				czfs = ql.getCZFS().toString();
			}
			if (ql.getDJYY() != null) {
				djyy = ql.getDJYY().toString();
			}
			if (ql.getFJ() != null) {
				fj = ql.getFJ().toString();
			}
			if (ql.getZSBS() != null) {
				zsbs = ql.getZSBS().toString();
			}
			if(ql.getBDCQZH()!=null){
				bdcqzh=ql.getBDCQZH();
			}
		}
		map.put("qssj", qssj);
		map.put("jssj", jssj);
		map.put("ldshyqr", ldshyqr);
		map.put("ldsyqxz", ldsyqxz);
		map.put("ldsyqr", ldsyqr);
		map.put("czfs", czfs);
		map.put("djyy", djyy);
		map.put("fj", fj);
		map.put("zsbs", zsbs);
		map.put("fbf", fbf);//发包方
		map.put("gyrqk", gyrqk);//共有人情况
		map.put("bdcqzh", bdcqzh);
		return map;
	}

	/**
	 * 组合业务：初始登记+在建工程抵押转现房抵押(刘树峰 2015.9.12备注，这个流程暂时没有用到）
	 * @作者 海豹
	 * @创建时间 2015年7月24日下午11:34:48
	 * @param djdyid
	 *            ：通过登记单元ID查找出所有的权利，并从中找出抵押权为23
	 * @return
	 */
	@Override
	public Map<String, String> GetCombDyqInfo(String djdyid, String xmbh) {
		ProjectInfo info = ProjectHelper.GetPrjInfoByXMBH(xmbh);// 添加xmbh参数，方便根据不同类型进行不同的操作
																// diaoliwei
																// 2015-8-11
		boolean flag = false; // 大部分流程
		if (ConstValue.DJLX.YGDJ.Value.equals(info.getDjlx()) && ConstValue.QLLX.DIYQ.Value.equals(info.getQllx()) && "02".equals(info.getSllx1())) { // 预告登记-预抵押
			flag = true;
		}
		String sql = MessageFormat.format("DJDYID=''{0}'' and qllx='23'", djdyid);
		Map<String, String> map = new HashMap<String, String>();
		String dyqr = "";// 抵押权人-qlr
		String dyr = "";// 抵押人-fsql
		String dybdclx = "";// 抵押不动产类型-fsql
		String dybdcqzmh = "";// 不动产权证明号-ql
		String djjg = "";// 登记机构-ql
		String djsj = "";// 登记时间-ql
		String dbr = "";// 登簿人-ql
		String djlx = "";// 登记类型-ql
		String zjjzwzl = "";// 在建建筑物坐落-fsql
		String dyfs = "";// 抵押方式-fsql
		String bdbzzqse = "";// 被担保主债权数额-fsql
		String zjjzwdyfw = "";// 在建建筑物抵押范围-fsql
		String zwlxqssj = "";// 债务履行起始时间-ql
		String zwlxjssj = "";// 债务履行结束时间-ql
		String zgezqse = "";// 最高债权数额-fsql
		String zgzqqdss = "";// 最高债权确定事实-fsql
		String djyy = "";// 登记原因-ql
		String fj = "";// 附记-ql
		String sqrId = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy月MM月dd日");
		List<BDCS_QL_GZ> qls = baseCommonDao.getDataList(BDCS_QL_GZ.class, sql);
		if (qls != null && qls.size() > 0) {
			BDCS_QL_GZ bdcs_ql_gz = qls.get(0);
			dbr = bdcs_ql_gz.getDBR();
			djjg = bdcs_ql_gz.getDJJG();
			djlx = bdcs_ql_gz.getDJLX();
			if (!StringUtils.isEmpty(bdcs_ql_gz.getDJSJ()))
				djsj = sdf.format(bdcs_ql_gz.getDJSJ());
			djyy = bdcs_ql_gz.getDJYY();
			fj = bdcs_ql_gz.getFJ();
			dybdcqzmh = bdcs_ql_gz.getBDCQZH();
			if (!StringUtils.isEmpty(bdcs_ql_gz.getQLQSSJ())) {
				if (flag) { // 预抵押
					zwlxqssj = bdcs_ql_gz.getQLQSSJ().toString();
				} else {
					zwlxqssj = sdf.format(bdcs_ql_gz.getQLQSSJ());
				}
			}
			if (!StringUtils.isEmpty(bdcs_ql_gz.getQLJSSJ())) {
				if (flag) { // 预抵押
					zwlxjssj = bdcs_ql_gz.getQLJSSJ().toString();
				} else {
					zwlxjssj = sdf.format(bdcs_ql_gz.getQLJSSJ());
				}
			}
			BDCS_FSQL_GZ bdcs_fsql_gz = baseCommonDao.get(BDCS_FSQL_GZ.class, bdcs_ql_gz.getFSQLID());
			if (bdcs_fsql_gz != null) {
				zjjzwzl = bdcs_fsql_gz.getZJJZWZL();
				zjjzwdyfw = bdcs_fsql_gz.getZJJZWDYFW();
				if (bdcs_fsql_gz.getZGZQSE() != null)
					zgezqse = bdcs_fsql_gz.getZGZQSE().toString();
				zgzqqdss = bdcs_fsql_gz.getZGZQQDSS();
				if (!StringUtils.isEmpty(bdcs_fsql_gz.getDYBDCLX())) {
					String dybdclxName = ConstHelper.getNameByValue("DYBDCLX", bdcs_fsql_gz.getDYBDCLX());
					dybdclx = dybdclxName;
				}
				dyr = bdcs_fsql_gz.getDYR();
				if (flag) { // 预抵押
					dyfs = bdcs_fsql_gz.getDYFS();
				} else {
					dyfs = bdcs_fsql_gz.getDYFSName();
				}
				if (bdcs_fsql_gz.getBDBZZQSE() != null)
					bdbzzqse = bdcs_fsql_gz.getBDBZZQSE().toString();
			}
			String qlSql = MessageFormat.format("qlid=''{0}''", bdcs_ql_gz.getId());
			List<BDCS_QLR_GZ> list = baseCommonDao.getDataList(BDCS_QLR_GZ.class, qlSql);
			if (list != null && list.size() > 0) {
				dyqr = list.get(0).getQLRMC();
				sqrId = list.get(0).getSQRID();
			}
		}
		map.put("dyqr", dyqr);
		map.put("dyr", dyr);
		map.put("dybdclx", dybdclx);
		map.put("dybdcqzmh", dybdcqzmh);
		map.put("djjg", djjg);
		map.put("djsj", djsj);
		map.put("dbr", dbr);
		map.put("djlx", djlx);
		map.put("zjjzwzl", zjjzwzl);
		map.put("dyfs", dyfs);
		map.put("bdbzzqse", bdbzzqse);
		map.put("zjjzwdyfw", zjjzwdyfw);
		map.put("qlqssj", zwlxqssj);
		map.put("qljssj", zwlxjssj);
		map.put("zgzqse", zgezqse);
		map.put("zgzqqdss", zgzqqdss);
		map.put("djyy", djyy);
		map.put("fj", fj);
		map.put("sqrid", sqrId); // 权利人 diaoliwei 2015-7-28
		return map;
	}

	/**
	 * 根据申请人ID数组添加权利人(刘树峰 2015.9.12备注，这个方法暂时可能没用，有待确认）
	 */
	@Override
	public ResultMessage addQLRfromSQR(String xmbh, String qlid, Object[] sqrids) {
		DJHandler handler = HandlerFactory.createDJHandler(xmbh);
		handler.addQLRbySQRArray(qlid, sqrids);
        JSONObject jsonobj=NewLogTools.getJSONByXMBH(xmbh);
        jsonobj.put("OperateType", "添加权利人");
        JSONObject jsonql=new JSONObject();
        jsonql.put("QLID", qlid);
        jsonql.put("Ql-Sqrids", sqrids);
        
        
		ResultMessage msg = new ResultMessage();
		if (handler.getError() != null && !StringHelper.isEmpty(handler.getError())) {
			msg.setMsg(handler.getError());
			msg.setSuccess("false");
		} else {
			msg.setSuccess("true");
		}
		jsonql.put("msg", msg.getSuccess());
		jsonobj.put("QL-Sqrinfos",jsonql);
		NewLogTools.saveLog(jsonobj.toString(), xmbh, "3", "添加权利人");
		return msg;
	}

	/**
	 * 通过权利_GZ及申请人ID数组添加权利人
	 *
	 * @作者 海豹
	 * @创建时间 2015年7月2日下午4:27:03
	 * @param xmbh
	 * @param ql
	 * @param sqrids
	 */
	@Override
	public void plAddQlRfromSQR(String xmbh, BDCS_QL_GZ ql, Object[] sqrids) {
		baseCommonDao.update(ql);
		addQLRbySQRs(ql,xmbh,sqrids);
	}


	/**
	 * 刘树峰新整的，8.9，却别在于添加权利人的时候不重新构建权力关系了，根据持证方式和现有权利人状况判断添加或者不添加证书
	 * @Title: addQLRbySQRs
	 * @author:liushufeng
	 * @date：2015年8月9日 下午10:24:20
	 * @param qlid
	 * @param sqrids
	 */
	protected void addQLRbySQRs(BDCS_QL_GZ ql,String xmbh, Object[] sqrids) {

		boolean existholder = false;
		int count = 0;
		// 获取第一个证书
		String hqlCondition = " id IN (SELECT ZSID FROM BDCS_QDZR_GZ QDZR WHERE QDZR.QLID=''{0}'' AND XMBH=''{1}'' ) AND XMBH=''{1}''";
		hqlCondition = MessageFormat.format(hqlCondition, ql.getId(), xmbh);
		List<BDCS_ZS_GZ> zslist = baseCommonDao.getDataList(BDCS_ZS_GZ.class, hqlCondition);
		// 获取当前权利的所有权利人
		List<BDCS_QLR_GZ> qlrlist = baseCommonDao.getDataList(BDCS_QLR_GZ.class, " XMBH='" + xmbh + "' AND QLID='" + ql.getId() + "'");
		// 根据权利人数量判断是否已经存在权利人
		existholder = (qlrlist == null || qlrlist.size() < 1) ? false : true;
		// 新生成的证书ID
		String newzsid = "";
		// 循环每个申请人ID
		if (sqrids != null && sqrids.length > 0) {
			for (Object sqridobj : sqrids) {

				String sqrid = StringHelper.formatObject(sqridobj);
				if (!StringHelper.isEmpty(sqrid)) {
					BDCS_SQR sqr = baseCommonDao.get(BDCS_SQR.class, sqrid);
					boolean exists = false;
					// 判断该申请人是否已经添加过权利人
					if (qlrlist != null) {
						for (BDCS_QLR_GZ qlr : qlrlist) {
							if (!StringUtils.isEmpty(qlr.getSQRID()) && qlr.getSQRID().equals(sqrid)) {
								exists = true;
								break;
							}
							if(!StringHelper.isEmpty(sqr.getSQRXM())&&sqr.getSQRXM().equals(qlr.getQLRMC())){
								if(StringHelper.isEmpty(sqr.getZJH())&&StringHelper.isEmpty(qlr.getZJH())){
									exists = true;
									break;
								}else if(!StringHelper.isEmpty(sqr.getZJH())&&sqr.getZJH().equals(qlr.getZJH())){
									exists = true;
									break;
								}
							}
						}
					}
					// 如果没有添加过
					if (!exists) {
						// 先添加权利人
						BDCS_QLR_GZ qlr = ObjectHelper.CopySQRtoQLR(sqr);
						qlr.setQLID(ql.getId());
						qlr.setSQRID(sqr.getId());
						qlr.setXMBH(xmbh);
						baseCommonDao.save(qlr);

						// 添加权地证人关系记录
						BDCS_QDZR_GZ qdzr = new BDCS_QDZR_GZ();
						qdzr.setBDCDYH(ql.getBDCDYH());
						qdzr.setQLRID(qlr.getId());
						qdzr.setDJDYID(ql.getDJDYID());
						qdzr.setFSQLID(ql.getFSQLID());
						qdzr.setQLID(ql.getId());
						qdzr.setXMBH(xmbh);
						baseCommonDao.save(qdzr);
						// 判断是否需要添加证书，两种情况
						// 1：分别持证
						// 2:共同持证且当前没有权利人并且这是第一个sqrid
						if (ql.getCZFS()==null || ql.getCZFS().equals(CZFS.FBCZ.Value) || (ql.getCZFS().equals(CZFS.GTCZ.Value) && !existholder && count < 1)) {
							BDCS_ZS_GZ zs = new BDCS_ZS_GZ();
							zs.setId((String) SuperHelper.GeneratePrimaryKey());
							zs.setXMBH(xmbh);
							ql.setBDCQZH("");
							qdzr.setZSID(zs.getId());
							newzsid = zs.getId();
							baseCommonDao.save(zs);
						} else // 这种情况就是共同持证并且已经有证书了，只需要找到一个证书，然后把证书ID写到上面的qdzr里就行了
						{

							if (zslist.size() > 0) {
								qdzr.setZSID(zslist.get(0).getId());
							} else {
								qdzr.setZSID(newzsid);
							}
						}
					}
				}
				count++;
			}
			baseCommonDao.flush();
		}
	}

	/**
	 * 移除申请人
	 */
	@Override
	public ResultMessage removeQLR(String xmbh, String qlid, String qlrid) {
		DJHandler handler = HandlerFactory.createDJHandler(xmbh);
		ResultMessage msg = new ResultMessage();
		String[] qlrids = qlrid.split("-");
		JSONObject jsonobj=NewLogTools.getJSONByXMBH(xmbh);
		jsonobj.put("OperateType", "删除权利人");
		JSONObject jsonql=new JSONObject();
		jsonql.put("QLID", qlid);
		int temp=0;
		for (String id : qlrids) {
			temp++;
			JSONObject jsonqlr=new JSONObject();
			jsonqlr.put("QLRID", id);
			handler.removeQLR(qlid, id);			
			if (handler.getError() != null && !StringHelper.isEmpty(handler.getError())) {
				msg.setMsg(handler.getError());
				msg.setSuccess("false");
				YwLogUtil.addYwLog("移除申请人-失败", ConstValue.SF.NO.Value,ConstValue.LOG.DELETE);
				return msg;
			} else {
				msg.setSuccess("true");
				YwLogUtil.addYwLog("移除申请人-成功", ConstValue.SF.YES.Value,ConstValue.LOG.DELETE);
				jsonqlr.put("msg", "移除申请人-成功");
				jsonql.put("序号：("+temp+")", jsonqlr);
			}
		}
		jsonobj.put("QL-QlrInfos", jsonql);
		NewLogTools.saveLog(jsonobj.toString(), xmbh, "4", "删除权利人");
		return msg;
	}

	/**
	 * 根据权利ID获取现状权利人列表
	 */
	@Override
	public List<BDCS_QLR_XZ> getXZQLRList(String qlid) {
		List<BDCS_QLR_XZ> list = baseCommonDao.getDataList(BDCS_QLR_XZ.class, " QLID='" + qlid + "'");
		return list;
	}

	/**
	 * 保存抵押权信息。刘树峰
	 * @Title: SaveQlInfo
	 * @author:liushufeng
	 * @date：2015年8月12日 上午3:11:37
	 * @param xmbh
	 * @param qlid
	 * @param object
	 */
	@Override
	public void SaveQlInfo(String xmbh, String qlid, JSONObject object) {
		BDCS_QL_GZ ql = baseCommonDao.get(BDCS_QL_GZ.class, qlid);
		BDCS_FSQL_GZ fsql = baseCommonDao.get(BDCS_FSQL_GZ.class, ql.getFSQLID());
		try {
			ql.setQLQSSJ(StringHelper.FormatByDate(object.getString("qlqssj")));
			ql.setQLJSSJ(StringHelper.FormatByDate(object.getString("qljssj")));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		ql.setFJ(object.getString("fj").replaceAll("\u00A0", " "));
		ql.setDJYY(object.getString("djyy"));
		ql.setBZ(object.getString("bz"));
		if(!StringHelper.isEmpty(object.getString("casenum"))){
			ql.setCASENUM(object.getString("casenum"));
		}
		fsql.setDYR(object.getString("dyr"));
		fsql.setDYFS(object.getString("dyfs"));
		fsql.setBDBZZQSE(object.getDouble("bdbzzqse"));
		fsql.setZJJZWDYFW(object.getString("zjjzwdyfw"));
		fsql.setZGZQSE(object.getDouble("zgzqse"));
		fsql.setZGZQQDSS(object.getString("zgzqqdss"));
		fsql.setZJJZWZL(object.getString("zjjzwzl"));
		fsql.setDYPGJZ(object.getDouble("dypgjz"));
		fsql.setTDPGJZ(object.getString("tdpgjz"));
		fsql.setZWR(object.getString("zwr"));
		if(object.containsKey("zqdw")){
			fsql.setZQDW(object.getString("zqdw"));
		}
		if(object.containsKey("dgbdbzzqse")){
			fsql.setDGBDBZZQSE(object.getDouble("dgbdbzzqse"));
		}
		

		if(object.containsKey("dyqlrlist")&&"1".equals(ql.getISPARTIAL())){
			List<String> qlrlist=new ArrayList<String>();
			String qlrids = object.getString("dyqlrlist");
			if(!StringHelper.isEmpty(qlrids)){
				String[] qlrs=qlrids.split(",");
				for(String qlrid:qlrs){
					if(!StringHelper.isEmpty(qlrid)){
						qlrlist.add(qlrid);
					}
				}
			}
			List<String> list_dyr=new ArrayList<String>();
			List<BDCS_PARTIALLIMIT> list=baseCommonDao.getDataList(BDCS_PARTIALLIMIT.class, "LIMITQLID='"+ql.getId()+"'");
			if(list!=null&&list.size()>0){
				for(BDCS_PARTIALLIMIT partialseizures:list){
					if(qlrlist.contains(partialseizures.getQLRID())){
						qlrlist.remove(partialseizures.getQLRID());
						RightsHolder qlr=RightsHolderTools.loadRightsHolder(DJDYLY.XZ, partialseizures.getQLRID());
						if(qlr!=null){
							list_dyr.add(qlr.getQLRMC());
						}
					}else{
						baseCommonDao.deleteEntity(partialseizures);
					}
				}
			}

			if(qlrlist.size()>0){
				for(String qlrid:qlrlist){
					RightsHolder qlr=RightsHolderTools.loadRightsHolder(DJDYLY.XZ, qlrid);

					if(qlr!=null){
						list_dyr.add(qlr.getQLRMC());
						BDCS_PARTIALLIMIT partialseizures=new BDCS_PARTIALLIMIT();
						List<BDCS_DJDY_GZ> djdys=baseCommonDao.getDataList(BDCS_DJDY_GZ.class, "DJDYID='"+ql.getDJDYID()+"' AND XMBH='"+ql.getXMBH()+"'");
						if(djdys!=null&&djdys.size()>0){
							partialseizures.setBDCDYID(djdys.get(0).getBDCDYID());
						}
						partialseizures.setLIMITQLID(ql.getId());
						partialseizures.setQLID(qlr.getQLID());
						partialseizures.setQLRID(qlr.getId());
						partialseizures.setQLRMC(qlr.getQLRMC());
						partialseizures.setXMBH(ql.getXMBH());
						partialseizures.setYXBZ("0");
						partialseizures.setLIMITTYPE("23");
						partialseizures.setZJH(qlr.getZJH());
						baseCommonDao.save(partialseizures);
					}

				}
			}
			fsql.setDYR(StringHelper.formatList(list_dyr, "、"));
		}
		fsql.setYDZMJ(object.getDouble("ydzmj"));
		fsql.setDYYDMJ(object.getDouble("dyydmj"));
		fsql.setZJZMJ(object.getDouble("zjzmj"));
		fsql.setDYJZMJ(object.getDouble("dyjzmj"));
		fsql.setDYTDYT(object.getString("dytdyt"));
		baseCommonDao.update(ql);
		baseCommonDao.update(fsql);
		baseCommonDao.flush();
	}

	/**
	 * 内部方法：保存抵押权信息，同时带上权利人信息
	 * @Title: SaveQlInfo
	 * @author:刘树峰
	 * @date：2015年9月12日下午19：09
	 * @param xmbh
	 * @param qlid
	 * @param object
	 */
	private void SaveQlInfoWithQLR(String xmbh, String qlid, JSONObject object) {
		String xmbhsql = ProjectHelper.GetXMBHCondition(xmbh);
		ProjectInfo projectInfo = ProjectHelper.GetPrjInfoByXMBH(xmbh);
		String sqrid = object.getString("sqrid");
		String[] sqrIds = sqrid.split(","); // 支持添加多个抵押权人
		String sql = xmbhsql + " AND QLID='" + qlid + "'";
		List<BDCS_QLR_GZ> qlrs = baseCommonDao.getDataList(BDCS_QLR_GZ.class, sql);
		BDCS_QL_GZ ql = baseCommonDao.get(BDCS_QL_GZ.class, qlid);
		BDCS_FSQL_GZ fsql = baseCommonDao.get(BDCS_FSQL_GZ.class, ql.getFSQLID());
		String selector = projectInfo.getBaseworkflowcode();
		
		// 传进来的申请人ID列表
		List<String> listId = new ArrayList<String>();
		for (String id : sqrIds) {
			if (!StringHelper.isEmpty(id) && !listId.contains(id)) {
				listId.add(id);
			}
		}
		// 下面这段循环是为了找出要删除的权利人和要新增加的申请人,最后qlrs列表中剩下的是要删除的，listid中剩下的是要添加为权利人的申请人ID
		if (qlrs.size() > 0) {
			for (String id : sqrIds) {
				if (!StringHelper.isEmpty(id)) {
					for (int i = 0; i < qlrs.size(); i++) { // 排除掉已经保存过得
						BDCS_QLR_GZ qlr_gz = qlrs.get(i);
						if (id.equals(qlr_gz.getSQRID())) {
							qlrs.remove(i);
							if (listId.contains(id)) {
								listId.remove(id);
							}
							break;
						}
					}
				}
			}
		}

		DJHandler handler = HandlerFactory.createDJHandler(xmbh);
		for (int i = 0; i < qlrs.size(); i++) {
			handler.removeQLR(qlid, qlrs.get(i).getId());
		}
		if (listId.size() > 0) {
			Object[] ids = listId.toArray();
			handler.addQLRbySQRArray(qlid, ids);
		}

		ql.setQLQSSJ(object.getDate("qlqssj"));
		ql.setQLJSSJ(object.getDate("qljssj"));
		ql.setFJ(object.getString("fj").replaceAll("\u00A0", " "));
		ql.setBZ(object.getString("bz"));
		if(!StringHelper.isEmpty(object.getString("casenum"))){
			ql.setCASENUM(object.getString("casenum"));
		}
		ql.setDJYY(object.getString("djyy"));
/*		if (Arrays.binarySearch(codes, selector)>0){
			fsql.setDYR(object.getString("dyr"));
		}*/
		String[] codes = {"GZ010","CS037","CS013","ZY006","BG007","BG201","BG202","BG028","BG027"};
		if(Arrays.asList(codes).contains(selector))
		{
			fsql.setDYR(object.getString("dyr"));
		}
		fsql.setDYFS(object.getString("dyfs"));
		fsql.setBDBZZQSE(object.getDouble("bdbzzqse"));
		fsql.setZJJZWDYFW(object.getString("zjjzwdyfw"));
		fsql.setZGZQSE(object.getDouble("zgzqse"));
		fsql.setZGZQQDSS(object.getString("zgzqqdss"));
		//fsql.setZJJZWZL(object.getString("zjjzwzl"));
		fsql.setZWR(object.getString("zwr"));
		//fsql.setDYPGJZ(object.getDouble("dypgjz"));
		if(object.containsKey("zqdw")){
			fsql.setZQDW(object.getString("zqdw"));
		}
		if(object.containsKey("dgbdbzzqse")){
			fsql.setDGBDBZZQSE(object.getDouble("dgbdbzzqse"));
		}
		
		ProjectInfo info = ProjectHelper.GetPrjInfoByXMBH(xmbh);
	/*	if(info!=null){
			RegisterWorkFlow flow = HandlerFactory.getWorkflow(info.getProject_id());
			if((("YCDYDJHandler").toUpperCase()).equals(flow.getHandlername().toUpperCase())||flow.getName().equals("BG202")||flow.getQllx().equals("23")){
				fsql.setDYR(object.getString("dyr"));// 保存抵押人
			}
		}*/

		fsql.setDYPGJZ(object.getDouble("dypgjz"));

		baseCommonDao.update(ql);
		baseCommonDao.update(fsql);
		baseCommonDao.flush();
	}

	/**
	 * 批量更新抵押信息
	 * @作者 海豹,刘树峰8.12修改，
	 * @创建时间 2015年8月6日上午12:23:14
	 * @param xmbh
	 * @param object
	 */
	@Override
	public void plSaveQlInfo(String xmbh, JSONObject object) {
		String xmbhsql = ProjectHelper.GetXMBHCondition(xmbh);
		xmbhsql += " AND QLLX='23'";// 加上权利类型的过滤，在组合登记的时候会出现其他类型的权利
		List<BDCS_QL_GZ> qls = baseCommonDao.getDataList(BDCS_QL_GZ.class, xmbhsql);
		String qlid = object.getString("qlid");
		List<RightsHolder> list = RightsHolderTools.loadRightsHolders(DJDYLY.GZ, qlid);
		String sqrids = "";
		if (list != null && list.size() > 0) {
			for (RightsHolder holder : list) {
				sqrids += StringHelper.isEmpty(sqrids) ? holder.getSQRID() : "," + holder.getSQRID();
			}
		}
		object.put("sqrid", sqrids);
		for (BDCS_QL_GZ ql : qls) {
			SaveQlInfoWithQLR(xmbh, ql.getId(), object);
		}
	}
	
	/**
	 * 首次登记-一般抵押权-在建工程抵押权登记转现房抵押登记时，抵押权人不能被修改,只保存权利及附属权利信息
	 * @作者 海豹
	 * @创建时间 2015年7月16日上午12:03:29
	 * @param xmbh
	 * @param qlid
	 * @param object
	 */
	@Override
	public void SaveQlandFsqlInfo(String xmbh, String qlid, JSONObject object) {
		BDCS_QL_GZ ql = baseCommonDao.get(BDCS_QL_GZ.class, qlid);
		BDCS_FSQL_GZ fsql = baseCommonDao.get(BDCS_FSQL_GZ.class, ql.getFSQLID());
		ql.setQLQSSJ(object.getDate("qlqssj"));
		ql.setQLJSSJ(object.getDate("qljssj"));
		ql.setFJ(object.getString("fj"));
		ql.setDJYY(object.getString("djyy"));
		fsql.setDYFS(object.getString("dyfs"));
		fsql.setBDBZZQSE(object.getDouble("bdbzzqse"));
		fsql.setZJJZWDYFW(object.getString("zjjzwdyfw"));
		fsql.setZGZQSE(object.getDouble("zgzqse"));
		fsql.setZGZQQDSS(object.getString("zgzqqdss"));
		fsql.setZJJZWZL(object.getString("zjjzwzl"));
		fsql.setDYR(object.getString("dyr"));

		fsql.setYDZMJ(object.getDouble("ydzmj"));// 用地总面积
		fsql.setDYYDMJ(object.getDouble("dyydmj"));// 抵押用地总面积
		fsql.setZJZMJ(object.getDouble("zjzmj"));// 总建筑面积
		fsql.setDYJZMJ(object.getDouble("dyjzmj"));// 抵押建筑面积
		fsql.setDYTDYT(object.getString("dytdyt"));// 土地用途
		baseCommonDao.update(ql);
		baseCommonDao.update(fsql);
		baseCommonDao.flush();
	}

	/**
	 * 批量更新一般抵押权-在建工程抵押权登记转现房抵押登记时，抵押权人不能被修改,只保存权利及附属权利信息
	 * @作者 海豹
	 * @创建时间 2015年8月6日上午12:48:40
	 * @param xmbh
	 * @param object
	 */
	@Override
	public void plSaveQlandFsqlInfo(String xmbh, JSONObject object) {
		String sql = ProjectHelper.GetXMBHCondition(xmbh);
		List<BDCS_QL_GZ> qls = baseCommonDao.getDataList(BDCS_QL_GZ.class, sql);
		for (BDCS_QL_GZ bdcs_ql_gz : qls) {
			if (bdcs_ql_gz != null) {
				BDCS_FSQL_GZ bdcs_fsql_gz = baseCommonDao.get(BDCS_FSQL_GZ.class, bdcs_ql_gz.getFSQLID());
				bdcs_ql_gz.setQLQSSJ(object.getDate("qlqssj"));
				bdcs_ql_gz.setQLJSSJ(object.getDate("qljssj"));
				bdcs_ql_gz.setFJ(object.getString("fj"));
				bdcs_ql_gz.setDJYY(object.getString("djyy"));
				bdcs_fsql_gz.setDYFS(object.getString("dyfs"));
				bdcs_fsql_gz.setBDBZZQSE(object.getDouble("bdbzzqse"));
				bdcs_fsql_gz.setZJJZWDYFW(object.getString("zjjzwdyfw"));
				bdcs_fsql_gz.setZGZQSE(object.getDouble("zgzqse"));
				bdcs_fsql_gz.setZGZQQDSS(object.getString("zgzqqdss"));
				bdcs_fsql_gz.setZJJZWZL(object.getString("zjjzwzl"));
				bdcs_fsql_gz.setDYR(object.getString("dyr"));

				bdcs_fsql_gz.setYDZMJ(object.getDouble("ydzmj"));// 用地总面积
				bdcs_fsql_gz.setDYYDMJ(object.getDouble("dyydmj"));// 抵押用地总面积
				bdcs_fsql_gz.setZJZMJ(object.getDouble("zjzmj"));// 总建筑面积
				bdcs_fsql_gz.setDYJZMJ(object.getDouble("dyjzmj"));// 抵押建筑面积
				bdcs_fsql_gz.setDYTDYT(object.getString("dytdyt"));// 土地用途
				baseCommonDao.update(bdcs_ql_gz);
				baseCommonDao.update(bdcs_fsql_gz);
			}
		}
		if (qls != null) {
			baseCommonDao.flush();
		}
	}

	/**
	 * 获取抵押权信息
	 */
	@Override
	public Map<String, Object> getDYQInfo(String xmbh, String qlid) {
		BDCS_QL_GZ ql = baseCommonDao.get(BDCS_QL_GZ.class, qlid);
		Map<String, Object> map = new HashMap<String, Object>();
		if (ql != null) {
			BDCS_FSQL_GZ fsql = baseCommonDao.get(BDCS_FSQL_GZ.class, ql.getFSQLID());
			String hql = MessageFormat.format(" XMBH=''{0}'' AND QLID=''{1}''", xmbh, qlid);
			String qlrmc = "";
			String sqrid = "";
			List<BDCS_QLR_GZ> list = baseCommonDao.getDataList(BDCS_QLR_GZ.class, hql);
			for (int i = 0; i < list.size(); i++) {
				BDCS_QLR_GZ qlr = list.get(i);
				if (list.size() - 1 == i) {
					qlrmc += qlr.getQLRMC();
					sqrid += qlr.getSQRID();
				} else {
					qlrmc += qlr.getQLRMC() + ",";
					sqrid += qlr.getSQRID() + ",";
				}
			}
			map.put("casenum", ql.getCASENUM()); 
			String qssj = ql.getQLQSSJ() == null ? "" : ql.getQLQSSJ().toString().substring(0, 11);
			String jssj = ql.getQLJSSJ() == null ? "" : ql.getQLJSSJ().toString().substring(0, 11);
			map.put("qlrmc", qlrmc); // 权利人名称
			map.put("sqrid", sqrid); // 权利人
			map.put("dybdclx", fsql.getDYWLXName()); // 抵押不动产类型
			map.put("zjjzwzl", fsql.getZJJZWZL()); // 在建建筑物坐落
			map.put("dyfs", fsql.getDYFS()); // 抵押方式
			String dyfsmc = "1".equals(fsql.getDYFS()) ? "一般抵押 ":"最高额抵押 ";
			map.put("dyfsmc", dyfsmc); // 抵押方式
			map.put("bdbzzqse", fsql.getBDBZZQSE()); // 被担保主债权数额
			map.put("zjjzwdyfw", fsql.getZJJZWDYFW()); // 在建建筑物抵押范围
			map.put("qljssj", jssj); // 债务旅行结束时间
			map.put("qlqssj", qssj); // 债务履行起始时间
			map.put("zgzqse", fsql.getZGZQSE()); // 最高债权数额
			map.put("zgzqqdss", fsql.getZGZQQDSS()); // 最高债权确定事实
			map.put("zwr", fsql.getZWR()); //债务人
			String fj=ql.getFJ();
			if(!StringHelper.isEmpty(fj)){
				fj=fj.replaceAll(" ", "\u00A0");
			}
			map.put("fj", fj); // 附记
			map.put("djyy", ql.getDJYY()); // 登记原因
			map.put("dyr", fsql.getDYR()); // 登记原因
			map.put("bdcqzh", ql.getBDCQZH()); // 产权证号
			map.put("dypgjz", fsql.getDYPGJZ()); // 抵押评估价值
			map.put("tdpgjz", fsql.getTDPGJZ()); // 土地评估价值
			map.put("bz", ql.getBZ()); // 登记原因
			map.put("zqdw", fsql.getZQDW()); // 债券单位
			map.put("dgbdbzzqse", fsql.getDGBDBZZQSE()); // 单个单元被担保主债权数额

			if("1".equals(ql.getISPARTIAL())){
				List<String> dyqlrlist=new ArrayList<String>();
				List<BDCS_PARTIALLIMIT> list_dylimit=baseCommonDao.getDataList(BDCS_PARTIALLIMIT.class, "LIMITQLID='"+ql.getId()+"'");
				if(list_dylimit!=null&&list_dylimit.size()<1){
					list_dylimit=baseCommonDao.getDataList(BDCS_PARTIALLIMIT.class, "LIMITQLID='"+ql.getLYQLID()+"'");
				}
				if(list_dylimit!=null&&list_dylimit.size()>0){
					for(BDCS_PARTIALLIMIT partialseizures:list_dylimit){
						dyqlrlist.add(partialseizures.getQLRID());
					}
				}
				String qllxarray = "('3','4','5','6','7','8','10','11','12','15','36')";
				String hql_qlr = "QLID IN(SELECT id FROM BDCS_QL_XZ WHERE  DJDYID='" + ql.getDJDYID() + "' AND QLLX IN " + qllxarray + ") ORDER BY SXH";
				List<BDCS_QLR_XZ> mainqlrlist=baseCommonDao.getDataList(BDCS_QLR_XZ.class, hql_qlr);
				List<HashMap<String,String>> mainqlrlist1=new ArrayList<HashMap<String,String>>();

				if(mainqlrlist!=null&&mainqlrlist.size()>0){
					for(BDCS_QLR_XZ qlr:mainqlrlist){
						HashMap<String,String> m=new HashMap<String, String>();
						m.put("id", qlr.getId());
						m.put("qlrmc", qlr.getQLRMC());
						List<BDCS_PARTIALLIMIT> list_limit=baseCommonDao.getDataList(BDCS_PARTIALLIMIT.class, "QLRID='"+qlr.getId()+"' AND LIMITTYPE='800' AND YXBZ IN ('0','1')");
						if(list_limit!=null&&list_limit.size()>0){
							m.put("islimit", "1");
						}else{
							m.put("islimit", "0");
						}
						mainqlrlist1.add(m);
					}
				}

				map.put("dyqlrlist", dyqlrlist);// 被抵押权利人id列表
				map.put("mainqlrlist", mainqlrlist1);// 被抵押单元产权人列表
			}
			map.put("qlid", qlid);
			map.put("ydzmj", fsql.getYDZMJ()); // 用地总面积
			map.put("dyydmj", fsql.getDYYDMJ()); // 抵押用地总面积
			map.put("zjzmj", fsql.getZJZMJ()); // 总建筑面积
			map.put("dyjzmj", fsql.getDYJZMJ()); // 抵押建筑面积
			map.put("dytdyt", fsql.getDYTDYT()); // 土地用途
			String xzqhdm=ConfigHelper.getNameByValue("XZQHDM");
			
			if("220200".equals(xzqhdm)){
				ProjectInfo info = ProjectHelper.GetPrjInfoByXMBH(xmbh);
				if(info!=null){
					if("CS015".equals(info.getBaseworkflowcode())){
						if(!StringHelper.isEmpty(ql.getLYQLID())){
							Rights rights_ly=RightsTools.loadRights(DJDYLY.LS, ql.getLYQLID());
							if(rights_ly!=null){
								map.put("bdcqzh", rights_ly.getBDCQZH()); // 产权证号
							}
						}
					}
				}
			}
		}
		return map;
	}

	/**
	 * 获取抵押权信息
	 */
	@Override
	// 转移登记-抵押前抵押信息
	public Map<String, Object> getzydj_DYQInfo(String qlid,String ly) {
		String id = null;
		if ("GZ".equals(ly)) {
			BDCS_QL_GZ bdcs_ql_gz = baseCommonDao.get(BDCS_QL_GZ.class, qlid);
			if (bdcs_ql_gz != null) {
				id = bdcs_ql_gz.getLYQLID();
			}
		}else {
			id = qlid;
		}

		String qlrmc = "";// 权利人名称
		String sqrid = "";// 权利人
		String dybdclx = "";// 抵押不动产类型
		String zjjzwzl = "";// 在建建筑物坐落
		String dyfs = "";// 抵押方式
		String bdbzzqse = "";// 被担保主债权数额
		String zjjzwdyfw = ""; // 在建建筑物抵押范围
		String zgzqse = "";// 最高债权数额
		String zgzqqdss = "";// 最高债权确定事实
		String fj = "";// 附记
		String djyy = "";// 登记原因
		String dyr = "";// 抵押人
		String bdcqzh = ""; // 产权证号
		Double dypgjz = 0.0;// 抵押评估价值
		String ydzmj=""; // 用地总面积
		String dyydmj="";// 抵押用地总面积
		String zjzmj="";// 总建筑面积
		String dyjzmj=""; // 抵押建筑面积
		String dytdyt="";// 土地用途
		Map<String, Object> map = new HashMap<String, Object>();
		DecimalFormat format = new DecimalFormat("#.00");
		format.setRoundingMode(RoundingMode.HALF_UP);
		if (!StringHelper.isEmpty(id)) {
			BDCS_QL_LS bdcs_ql_ls = baseCommonDao.get(BDCS_QL_LS.class, id);
			if (bdcs_ql_ls != null) {
				BDCS_FSQL_LS bdcs_fsql_ls = baseCommonDao.get(BDCS_FSQL_LS.class, bdcs_ql_ls.getFSQLID());
				if (bdcs_fsql_ls != null) {
					if (!StringHelper.isEmpty(bdcs_fsql_ls.getDYWLXName())) {
						dybdclx = bdcs_fsql_ls.getDYWLXName();
					}
					if (!StringHelper.isEmpty(bdcs_fsql_ls.getZJJZWZL())) {
						zjjzwzl = bdcs_fsql_ls.getZJJZWZL();
					}
					if (!StringHelper.isEmpty(bdcs_fsql_ls.getDYFS())) {
						dyfs = bdcs_fsql_ls.getDYFS();
					}
					if (!StringHelper.isEmpty(bdcs_fsql_ls.getBDBZZQSE())) {
						bdbzzqse = format.format(bdcs_fsql_ls.getBDBZZQSE());
					}
					if (!StringHelper.isEmpty(bdcs_fsql_ls.getZGZQSE())) {
						zgzqse = format.format(bdcs_fsql_ls.getZGZQSE());
					}
					if (!StringHelper.isEmpty(bdcs_fsql_ls.getZJJZWDYFW())) {
						zjjzwdyfw = bdcs_fsql_ls.getZJJZWDYFW();
					}
					if (!StringHelper.isEmpty(bdcs_fsql_ls.getDYR())) {
						dyr = bdcs_fsql_ls.getDYR();
					}
					if (!StringHelper.isEmpty(bdcs_fsql_ls.getDYPGJZ())) {
						dypgjz = bdcs_fsql_ls.getDYPGJZ();
					}
					if (!StringHelper.isEmpty(bdcs_fsql_ls.getZGZQQDSS())) {
						zgzqqdss = bdcs_fsql_ls.getZGZQQDSS();
					}
					if (!StringHelper.isEmpty(bdcs_fsql_ls.getYDZMJ())) {
						ydzmj = format.format(bdcs_fsql_ls.getYDZMJ());
					}
					if (!StringHelper.isEmpty(bdcs_fsql_ls.getDYYDMJ())) {
						dyydmj = format.format(bdcs_fsql_ls.getDYYDMJ());
					}
					if (!StringHelper.isEmpty(bdcs_fsql_ls.getZJZMJ())) {
						zjzmj = format.format(bdcs_fsql_ls.getZJZMJ());
					}
					if (!StringHelper.isEmpty(bdcs_fsql_ls.getDYJZMJ())) {
						dyjzmj = format.format(bdcs_fsql_ls.getDYJZMJ());
					}
					if (!StringHelper.isEmpty(bdcs_fsql_ls.getDYTDYT())) {
						dytdyt = bdcs_fsql_ls.getDYTDYT();
					}
				}
				List<BDCS_QLR_LS> qlrs = baseCommonDao.getDataList(BDCS_QLR_LS.class, "qlid ='" + id + "'");
				boolean flag = true;
				for (BDCS_QLR_LS bdcs_qlr_ls : qlrs) {
					if (flag) {
						qlrmc += bdcs_qlr_ls.getQLRMC();
						sqrid += bdcs_qlr_ls.getSQRID();
						flag = false;
					} else {
						qlrmc += "," + bdcs_qlr_ls.getQLRMC();
						sqrid += "," + bdcs_qlr_ls.getSQRID();
					}
				}
				String qssj = bdcs_ql_ls.getQLQSSJ() == null ? "" : bdcs_ql_ls.getQLQSSJ().toString().substring(0, 11);// 债务履行起始时间
				String jssj = bdcs_ql_ls.getQLJSSJ() == null ? "" : bdcs_ql_ls.getQLJSSJ().toString().substring(0, 11); // 债务旅行结束时间
				fj = bdcs_ql_ls.getFJ();
				djyy = bdcs_ql_ls.getDJYY();
				bdcqzh = bdcs_ql_ls.getBDCQZH();
				map.put("casenum", bdcs_ql_ls.getCASENUM()); 
				map.put("qlrmc", qlrmc); // 权利人名称
				map.put("sqrid", sqrid); // 权利人
				map.put("dybdclx", dybdclx); // 抵押不动产类型
				map.put("zjjzwzl", zjjzwzl); // 在建建筑物坐落
				map.put("dyfs", dyfs); // 抵押方式
				String dyfsmc = "1".equals(dyfs) ? "一般抵押 ":"最高额抵押 ";
				map.put("dyfsmc", dyfsmc); // 抵押方式
				map.put("bdbzzqse", bdbzzqse); // 被担保主债权数额
				map.put("zjjzwdyfw", zjjzwdyfw); // 在建建筑物抵押范围
				map.put("qljssj", jssj); // 债务旅行结束时间
				map.put("qlqssj", qssj); // 债务履行起始时间
				map.put("zgzqse", zgzqse); // 最高债权数额
				map.put("zgzqqdss", zgzqqdss); // 最高债权确定事实
				map.put("fj", fj); // 附记
				map.put("djyy", djyy); // 登记原因
				map.put("dyr", dyr); // 抵押人
				map.put("dypgjz", dypgjz); // 抵押评估价值
				map.put("bdcqzh", bdcqzh); // 产权证号
				map.put("qlid", qlid);
				map.put("ydzmj", ydzmj); // 用地总面积
				map.put("dyydmj", dyydmj); // 抵押用地总面积
				map.put("zjzmj", zjzmj); // 总建筑面积
				map.put("dyjzmj", dyjzmj); // 抵押建筑面积
				map.put("dytdyt", dytdyt); // 土地用途
			}
		}
		return map;
	}

	/************************************ 查封登记 ***********************************************/
	/**
	 * 通过qlid获取查封的基本信息
	 *
	 * @作者 海豹
	 * @创建时间 2015年6月18日上午12:09:28
	 * @param qlid
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Map getCfxxInfo(String qlid) {
		BDCS_FSQL_GZ fsql = new BDCS_FSQL_GZ();
		BDCS_QL_GZ ql = baseCommonDao.get(BDCS_QL_GZ.class, qlid);
		String bdcqzh = "";
		String qlrmc = "";
		List<BDCS_QLR_XZ> mainqlrlist=null;
		if(ql!=null){
			String condition = MessageFormat.format("QLLX IN ('1','2','3','4','5','6','7','8','9','10','11','12','13','14','15','16','17','18','24') AND DJDYID=''{0}''", ql.getDJDYID());
			List<Rights> lyql = RightsTools.loadRightsByCondition(DJDYLY.XZ,condition);
			if(!StringHelper.isEmpty(lyql) && lyql.size()>0){
				bdcqzh = lyql.get(0).getBDCQZH();
				List<BDCS_QLR_XZ> qlrs = baseCommonDao.getDataList(BDCS_QLR_XZ.class, "qlid ='" + lyql.get(0).getId() + "'");
				mainqlrlist=qlrs;
				if(!StringHelper.isEmpty(qlrs) && qlrs.size()>0){
					boolean flag = true;
					for (BDCS_QLR_XZ bdcs_qlr_xz : qlrs) {
						if (flag) {
							qlrmc += bdcs_qlr_xz.getQLRMC();
							flag = false;
						} else {
							qlrmc += "," + bdcs_qlr_xz.getQLRMC();
						}
					}
				}
			}
		}


		Map<String, Object> map = new HashMap<String, Object>();
		String fj = "";
		String cfjg = "";
		String cflx = "";
		String cfwh = "";
		String cffw = "";
		String cfqssj = "";
		String cfjssj = "";
		String cfwj = "";
		String jfjg = "";
		String jfwh = "";
		String jfwj = "";
		String jffj = "";
		String cfsj = "";
		String cfywh = "";
		String fysdsj = "";
		String plaintiff = "";
		String defendant = "";
		Integer lhsx = null;
		List<String> cfqlrlist=new ArrayList<String>();
		if (ql != null) {
			fj = ql.getFJ();
			cfqssj = DateUtil.getDate(ql.getQLQSSJ());
			cfjssj = DateUtil.getDate(ql.getQLJSSJ());
			fsql = baseCommonDao.get(BDCS_FSQL_GZ.class, ql.getFSQLID());
		//	qlrxx = baseCommonDao.get(BDCS_QLR_GZ.class, qlid);
			if (fsql != null) {
				jffj = fsql.getZXFJ();
				cfjg = fsql.getCFJG();
				cflx = fsql.getCFLX();
				cfwh = fsql.getCFWH();
				cfwj = fsql.getCFWJ();
				cffw = fsql.getCFFW();
				cfywh = ql.getYWH();
				fysdsj = !StringHelper.isEmpty(fsql.getFYSDSJ())? fsql.getFYSDSJ().toString(): "";
				cfsj = StringHelper.FormatYmdhmsByDate(fsql.getCFSJ());// DateUtil.getDate(fsql.getCFSJ());

				jfjg = fsql.getJFJG();
				jfwh = fsql.getJFWH();
				jfwj = fsql.getJFWJ();
				lhsx = fsql.getLHSX();
				plaintiff = fsql.getPLAINTIFF();
				defendant = fsql.getDEFENDANT();

			}
			if("1".equals(ql.getISPARTIAL())){
				List<BDCS_PARTIALLIMIT> list=baseCommonDao.getDataList(BDCS_PARTIALLIMIT.class, "LIMITQLID='"+ql.getId()+"'");
				if(list!=null&&list.size()<1){
					list=baseCommonDao.getDataList(BDCS_PARTIALLIMIT.class, "LIMITQLID='"+ql.getLYQLID()+"'");
				}
				if(list!=null&&list.size()>0){
					for(BDCS_PARTIALLIMIT partialseizures:list){
						cfqlrlist.add(partialseizures.getQLRID());
					}
				}
			}
			if("1".equals(ql.getISPARTIAL())){
				map.put("cfqlrlist", cfqlrlist);// 被查封权利人id列表
				map.put("mainqlrlist", mainqlrlist);// 被查封单元产权人列表
			}
		}
		map.put("fj", fj); // 附记
		map.put("cfjg", cfjg); // 查封机关
		map.put("cflx", cflx); // 查封类型
		map.put("cfwh", cfwh); // 查封文号
		map.put("cfwj", cfwj); // 查封文件
		map.put("cffw", cffw); // 查封范围
		map.put("cfqssj", cfqssj); // 查封起始时间
		map.put("cfjssj", cfjssj); // 查封结束时间
		map.put("jfjg", jfjg); // 解封机构
		map.put("jfwh", jfwh); // 解封文号
		map.put("jfwj", jfwj); // 解封文件
		map.put("jffj", jffj); // 解封附记
		map.put("cfsj", cfsj); // 查封时间
		map.put("lhsx", lhsx); // 轮候顺序
		map.put("cfywh", cfywh);// 查封业务号
		map.put("bcfbdcqzh", bdcqzh);// 被查封查封业务号
		map.put("bcfqlrmc", qlrmc);// 被查封权利人名称
		map.put("plaintiff", plaintiff);// 原告
		map.put("defendant", defendant);// 被告
		map.put("fysdsj", fysdsj);// 法院送达时间

		return map;
	}


	/**
	 * @Description: 查封的更正 续封 解封 权利从来源权利获取 ls
	 * @Title: getCfxxInfoEX
	 * @Author: zhaomengfan
	 * @Date: 2017年1月10日下午5:31:45
	 * @param qlid
	 * @return
	 *
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Map getCfxxInfoEX(String qlid) {
		BDCS_FSQL_LS fsql = new BDCS_FSQL_LS();
		BDCS_QL_LS ql = baseCommonDao.get(BDCS_QL_LS.class, qlid);
		String bdcqzh = "";
		String qlrmc = "";
		List<BDCS_QLR_LS> mainqlrlist=null;
		if(ql!=null){
			bdcqzh = ql.getBDCQZH();
			List<BDCS_QLR_LS> qlrs = baseCommonDao.getDataList(BDCS_QLR_LS.class, "qlid ='" + ql.getId() + "'");
			mainqlrlist=qlrs;
			if(!StringHelper.isEmpty(qlrs) && qlrs.size()>0){
				boolean flag = true;
				for (BDCS_QLR_LS bdcs_qlr_xz : qlrs) {
					if (flag) {
						qlrmc += bdcs_qlr_xz.getQLRMC();
						flag = false;
					} else {
						qlrmc += "," + bdcs_qlr_xz.getQLRMC();
					}
				}
			}

		}


		Map<String, Object> map = new HashMap<String, Object>();
		String fj = "";
		String cfjg = "";
		String cflx = "";
		String cfwh = "";
		String cffw = "";
		String cfqssj = "";
		String cfjssj = "";
		String cfwj = "";
		String jfjg = "";
		String jfwh = "";
		String jfwj = "";
		String jffj = "";
		String cfsj = "";
		String cfywh = "";
		String plaintiff = "";
		String defendant = "";
		Integer lhsx = null;
		List<String> cfqlrlist=new ArrayList<String>();
		if (ql != null) {
			fj = ql.getFJ();
			cfqssj = DateUtil.getDate(ql.getQLQSSJ());
			cfjssj = DateUtil.getDate(ql.getQLJSSJ());
			fsql = baseCommonDao.get(BDCS_FSQL_LS.class, ql.getFSQLID());
		//	qlrxx = baseCommonDao.get(BDCS_QLR_GZ.class, qlid);
			if (fsql != null) {
				jffj = fsql.getZXFJ();
				cfjg = fsql.getCFJG();
				cflx = fsql.getCFLX();
				cfwh = fsql.getCFWH();
				cfwj = fsql.getCFWJ();
				cffw = fsql.getCFFW();
				cfywh = ql.getYWH();
				cfsj = StringHelper.FormatByDatetime(fsql.getCFSJ());// DateUtil.getDate(fsql.getCFSJ());

				jfjg = fsql.getJFJG();
				jfwh = fsql.getJFWH();
				jfwj = fsql.getJFWJ();
				lhsx = fsql.getLHSX();
				plaintiff = fsql.getPLAINTIFF();
				defendant = fsql.getDEFENDANT();

			}
			if("1".equals(ql.getISPARTIAL())){
				List<BDCS_PARTIALLIMIT> list=baseCommonDao.getDataList(BDCS_PARTIALLIMIT.class, "LIMITQLID='"+ql.getId()+"'");
				if(list!=null&&list.size()<1){
					list=baseCommonDao.getDataList(BDCS_PARTIALLIMIT.class, "LIMITQLID='"+ql.getLYQLID()+"'");
				}
				if(list!=null&&list.size()>0){
					for(BDCS_PARTIALLIMIT partialseizures:list){
						cfqlrlist.add(partialseizures.getQLRID());
					}
				}
			}
			if("1".equals(ql.getISPARTIAL())){
				map.put("cfqlrlist", cfqlrlist);// 被查封权利人id列表
				map.put("mainqlrlist", mainqlrlist);// 被查封单元产权人列表
			}
		}
		map.put("fj", fj); // 附记
		map.put("cfjg", cfjg); // 查封机关
		map.put("cflx", cflx); // 查封类型
		map.put("cfwh", cfwh); // 查封文号
		map.put("cfwj", cfwj); // 查封文件
		map.put("cffw", cffw); // 查封范围
		map.put("cfqssj", cfqssj); // 查封起始时间
		map.put("cfjssj", cfjssj); // 查封结束时间
		map.put("jfjg", jfjg); // 解封机构
		map.put("jfwh", jfwh); // 解封文号
		map.put("jfwj", jfwj); // 解封文件
		map.put("jffj", jffj); // 解封附记
		map.put("cfsj", cfsj); // 查封时间
		map.put("lhsx", lhsx); // 轮候顺序
		map.put("cfywh", cfywh);// 查封业务号
		map.put("bcfbdcqzh", bdcqzh);// 被查封查封业务号
		map.put("bcfqlrmc", qlrmc);// 被查封权利人名称
		map.put("plaintiff", plaintiff);// 原告
		map.put("defendant", defendant);// 被告

		return map;
	}

	/**
	 * 更新查封中的；通过qlid更新ql_gz中的fj，及更新fsql_gz中的内容
	 *
	 * @作者 海豹
	 * @创建时间 2015年6月18日上午12:08:35
	 * @param qlid
	 * @param object
	 */
	@Override
	public void updateCfxxInfo(String qlid, JSONObject object) {
		BDCS_QL_GZ ql = baseCommonDao.get(BDCS_QL_GZ.class, qlid);
		if (ql != null) {
			BDCS_FSQL_GZ fsql = baseCommonDao.get(BDCS_FSQL_GZ.class, ql.getFSQLID());
			ql.setFJ(object.getString("fj"));
			ql.setYWH(object.getString("ywh"));
			ql.setQLQSSJ(object.getDate("cfqssj"));
			ql.setQLJSSJ(object.getDate("cfjssj"));

			if (fsql != null) {
				// 更新查封时间
//				try {
//					fsql.setCFSJ(object.getDate("cfsj"));
//				} catch (Exception ee) {
//
//				}
				fsql.setCFJG(object.getString("cfjg"));
				fsql.setCFLX(object.getString("cflx"));
				fsql.setCFWH(object.getString("cfwh"));
				fsql.setCFWJ(object.getString("cfwj"));
				fsql.setCFFW(object.getString("cffw"));
				fsql.setLHSX(object.getInteger("lhsx"));
				try {
					fsql.setFYSDSJ(new SimpleDateFormat( "yyyy-MM-dd HH:mm" ).parse(object.getString("fysdsj")));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				String lhsx = object.getString("lhsx");
				if (!StringUtils.isEmpty(lhsx)) {
					Integer lhsxInt = Integer.parseInt(lhsx);
					fsql.setLHSX(lhsxInt);
				}

				if (object.containsKey("plaintiff")) {
					fsql.setPLAINTIFF(object.getString("plaintiff"));
				}
				if (object.containsKey("defendant")) {
					fsql.setDEFENDANT(object.getString("defendant"));
				}
				// String cfsj = object.getString("cfsj");
				// Date cf1 = object.getDate("cfsj");
				// SimpleDateFormat formatter = new
				// SimpleDateFormat("yyyy/MM/dd");
				// Date dateString = formatter.parse(cfsj);
				// fsql.setCFSJ(cf1);
				baseCommonDao.update(fsql);
			}
			if(object.containsKey("cfqlrlist")&&"1".equals(ql.getISPARTIAL())){
				List<String> qlrlist=new ArrayList<String>();
				String qlrids = object.getString("cfqlrlist");
				if(!StringHelper.isEmpty(qlrids)){
					String[] qlrs=qlrids.split(",");
					for(String qlrid:qlrs){
						if(!StringHelper.isEmpty(qlrid)){
							qlrlist.add(qlrid);
						}
					}
				}

				List<BDCS_PARTIALLIMIT> list=baseCommonDao.getDataList(BDCS_PARTIALLIMIT.class, "LIMITQLID='"+ql.getId()+"'");
				if(list!=null&&list.size()>0){
					for(BDCS_PARTIALLIMIT partialseizures:list){
						if(qlrlist.contains(partialseizures.getQLRID())){
							qlrlist.remove(partialseizures.getQLRID());
						}else{
							baseCommonDao.deleteEntity(partialseizures);
						}
					}
				}
				if(qlrlist.size()>0){
					for(String qlrid:qlrlist){
						RightsHolder qlr=RightsHolderTools.loadRightsHolder(DJDYLY.XZ, qlrid);
						if(qlr!=null){
							BDCS_PARTIALLIMIT partialseizures=new BDCS_PARTIALLIMIT();
							List<BDCS_DJDY_GZ> djdys=baseCommonDao.getDataList(BDCS_DJDY_GZ.class, "DJDYID='"+ql.getDJDYID()+"' AND XMBH='"+ql.getXMBH()+"'");
							if(djdys!=null&&djdys.size()>0){
								partialseizures.setBDCDYID(djdys.get(0).getBDCDYID());
							}
							partialseizures.setLIMITQLID(ql.getId());
							partialseizures.setQLID(qlr.getQLID());
							partialseizures.setQLRID(qlr.getId());
							partialseizures.setQLRMC(qlr.getQLRMC());
							partialseizures.setXMBH(ql.getXMBH());
							partialseizures.setYXBZ("0");
							partialseizures.setLIMITTYPE("800");
							partialseizures.setZJH(qlr.getZJH());
							baseCommonDao.save(partialseizures);
						}

					}
				}
			}

			baseCommonDao.update(ql);
			baseCommonDao.flush();
		}

	}

	/**
	 * 通过qlrid更新权证号
	 *
	 */
	public void updateQzh(String qlrid,String dataString,String xmbh){
			String str1 = MessageFormat.format("select  distinct zsid,qlrid,qlid  from BDCK.BDCS_QDZR_GZ WHERE QLRID=''{0}''", qlrid);
			@SuppressWarnings("rawtypes")
			List<Map> lstMap = baseCommonDao.getDataListByFullSql(str1);
			for (@SuppressWarnings("rawtypes") Map qdzr : lstMap) {
				if (!StringUtils.isEmpty(qdzr.get("ZSID"))) {
					String zsids=qdzr.get("ZSID").toString();
					BDCS_ZS_GZ zs = baseCommonDao.get(BDCS_ZS_GZ.class,zsids);
					zs.setBDCQZH(dataString);
					baseCommonDao.update(zs);
					if(zs!=null){
							String str2=MessageFormat.format("select  distinct qlrid,qlid from BDCK.BDCS_QDZR_GZ WHERE ZSID=''{0}''", zsids);
							@SuppressWarnings("rawtypes")
							List<Map> lstMap2 = baseCommonDao.getDataListByFullSql(str2);
							for(@SuppressWarnings("rawtypes") Map qdzr2 : lstMap2){//遍历权利人更改 每个权利人的权证号
								if(!StringUtils.isEmpty(qdzr2.get("QLRID"))){
									String qlrids=qdzr2.get("QLRID").toString();
									BDCS_QLR_GZ qlrs =baseCommonDao.get(BDCS_QLR_GZ.class, qlrids);
									qlrs.setBDCQZH(dataString);
									baseCommonDao.update(qlrs);
								}
							}
					}
				}
				baseCommonDao.flush();
			}
	}

	/**
	 *
	 */
	public void updataQLqzh(String qlrid){
		String str1 = MessageFormat.format("select  distinct zsid,qlrid,qlid  from BDCK.BDCS_QDZR_GZ WHERE QLRID=''{0}''", qlrid);
		@SuppressWarnings("rawtypes")
		List<Map> lstMap = baseCommonDao.getDataListByFullSql(str1);
		for (@SuppressWarnings("rawtypes") Map qdzr2 : lstMap) {
			String qlid= qdzr2.get("QLID").toString();
								if(qlrid!=null){
									BDCS_QL_GZ ql = baseCommonDao.get(BDCS_QL_GZ.class,qlid );
									ql.setBDCQZH(null);
									String str3=MessageFormat.format("select  distinct bdcqzh from BDCK.BDCS_QLR_GZ WHERE QLID=''{0}'' order by bdcqzh",qlid);
									@SuppressWarnings("rawtypes")
									List<Map> lstMap3 = baseCommonDao.getDataListByFullSql(str3);
									StringBuilder sb=new StringBuilder();
									for(@SuppressWarnings("rawtypes") Map qzh3 : lstMap3){
										System.out.println(qzh3);
										if(ql.getBDCQZH()!=null){
										sb.append(","+qzh3.get("BDCQZH").toString());
										}else{
											sb.append(qzh3.get("BDCQZH").toString());
											ql.setBDCQZH(sb.toString());
										}
									}
									ql.setBDCQZH(sb.toString());
									baseCommonDao.update(ql);
									baseCommonDao.flush();
								}
		}
	}
	/**
	 * 登簿后更新权证号和登记时间（组件功能）
	 * @author weilb
	 * @param String qlrid ,djsj  
	 * 进行持久化操作：ql、 qlr、 zs  表， gz、 xz、 ls  层 
	 * 更新维护字段：qlrid ,djsj  
	 */
	@SuppressWarnings("null")
	public void updateDbhQzh(String qlrid,String bdcqzh,String djsj){
		String zsid = "";
		String qlid = "";
		if(qlrid!=null){
			List<BDCS_QDZR_GZ> qdzrs = baseCommonDao.getDataList(BDCS_QDZR_GZ.class, " QLRID='"+qlrid+"'");
			if(qdzrs!=null && qdzrs.size()>0){
				for(BDCS_QDZR_GZ qdzr : qdzrs){
					zsid = qdzr.getZSID() ;
					qlid = qdzr.getQLID() ;
					if(zsid!=null || zsid.equals("")!=true){
						BDCS_ZS_GZ zs_gz = baseCommonDao.get(BDCS_ZS_GZ.class, zsid);
						zs_gz.setBDCQZH(bdcqzh);
						baseCommonDao.update(zs_gz);
					}
					if(qlid!=null || qlid.equals("")!=true){
						BDCS_QL_GZ ql_gz = baseCommonDao.get(BDCS_QL_GZ.class, qlid);
						if(djsj!=null){
							SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
							try {
								java.util.Date date=sdf.parse(djsj);
								ql_gz.setDJSJ(date);
							} catch (ParseException e) {
								e.printStackTrace();
							}
						}
						ql_gz.setBDCQZH(bdcqzh);
						baseCommonDao.update(ql_gz);
					}
					BDCS_QLR_GZ qlr_gz = baseCommonDao.get(BDCS_QLR_GZ.class, qlrid);
					qlr_gz.setBDCQZH(bdcqzh);
					baseCommonDao.update(qlr_gz);
				}
				baseCommonDao.flush();
			}
		}
	}
	
	/**
	 * 通过项目编号批量更新查封信息
	 *
	 * @作者 海豹
	 * @创建时间 2015年8月5日下午10:14:30
	 * @param xmbh
	 * @param object
	 */
	@Override
	public void plUpdateCfxxInfo(String xmbh, JSONObject object) {
		String sql = ProjectHelper.GetXMBHCondition(xmbh);
		List<BDCS_QL_GZ> qls = baseCommonDao.getDataList(BDCS_QL_GZ.class, sql);
		for (BDCS_QL_GZ bdcs_ql_gz : qls) {
			BDCS_FSQL_GZ bdcs_fsql_gz = baseCommonDao.get(BDCS_FSQL_GZ.class, bdcs_ql_gz.getFSQLID());
			bdcs_ql_gz.setFJ(object.getString("fj"));
			bdcs_ql_gz.setYWH(object.getString("ywh"));
			bdcs_ql_gz.setQLQSSJ(object.getDate("cfqssj"));
			bdcs_ql_gz.setQLJSSJ(object.getDate("cfjssj"));
			if (bdcs_fsql_gz != null) {
				bdcs_fsql_gz.setCFJG(object.getString("cfjg"));
				 bdcs_fsql_gz.setCFLX(object.getString("cflx"));
				bdcs_fsql_gz.setCFWH(object.getString("cfwh"));
				bdcs_fsql_gz.setCFWJ(object.getString("cfwj"));
				bdcs_fsql_gz.setCFFW(object.getString("cffw"));
				try {
					bdcs_fsql_gz.setFYSDSJ(new SimpleDateFormat( "yyyy-MM-dd HH:mm" ).parse(object.getString("fysdsj")));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (object.containsKey("plaintiff")) {
					bdcs_fsql_gz.setPLAINTIFF(object.getString("plaintiff"));
				}
				if (object.containsKey("defendant")) {
					bdcs_fsql_gz.setDEFENDANT(object.getString("defendant"));
				}
				 String lhsx = object.getString("lhsx");
				 if (!StringUtils.isEmpty(lhsx)) {
				 Integer lhsxInt = Integer.parseInt(lhsx);
				 bdcs_fsql_gz.setLHSX(lhsxInt);
				 }
				baseCommonDao.update(bdcs_fsql_gz);
			}
			baseCommonDao.update(bdcs_ql_gz);
		}
		if (qls != null && qls.size() > 0) {
			baseCommonDao.flush();
		}
	}

	@Override
	public void updateJfxxInfo(String qlid, JSONObject object) {
		BDCS_QL_GZ ql = baseCommonDao.get(BDCS_QL_GZ.class, qlid);
		if (ql != null) {
			BDCS_FSQL_GZ fsql = baseCommonDao.get(BDCS_FSQL_GZ.class, ql.getFSQLID());
			if (fsql != null) {
				fsql.setZXFJ(object.getString("jffj"));
				fsql.setJFJG(object.getString("jfjg"));
				fsql.setJFWH(object.getString("jfwh"));
				fsql.setJFWJ(object.getString("jfwj"));
				fsql.setZXDYYWH(object.getString("ywh"));
				baseCommonDao.update(fsql);
			}
			baseCommonDao.update(ql);
			baseCommonDao.flush();
		}

	}

	/**
	 * 通过项目编号批量更新解封信息
	 *
	 * @作者 海豹
	 * @创建时间 2015年8月5日下午11:48:02
	 * @param xmbh
	 * @param object
	 */
	@Override
	public void plUpdateJfxxInfo(String xmbh, JSONObject object) {
		String sql = ProjectHelper.GetXMBHCondition(xmbh);
		List<BDCS_QL_GZ> qls = baseCommonDao.getDataList(BDCS_QL_GZ.class, sql);
		for (BDCS_QL_GZ bdcs_ql_gz : qls) {
			if (bdcs_ql_gz != null) {
				BDCS_FSQL_GZ bdcs_fsql_gz = baseCommonDao.get(BDCS_FSQL_GZ.class, bdcs_ql_gz.getFSQLID());
				if (bdcs_fsql_gz != null) {
					bdcs_fsql_gz.setZXFJ(object.getString("jffj"));
					bdcs_fsql_gz.setJFJG(object.getString("jfjg"));
					bdcs_fsql_gz.setJFWH(object.getString("jfwh"));
					bdcs_fsql_gz.setJFWJ(object.getString("jfwj"));
					bdcs_fsql_gz.setZXDYYWH(object.getString("ywh"));
					baseCommonDao.update(bdcs_fsql_gz);
				}
				baseCommonDao.update(bdcs_ql_gz);
			}
		}
		if (qls != null && qls.size() > 0) {
			baseCommonDao.flush();
		}
	}

	/************************************ 注销 ***********************************************/

	/**
	 * 通过qlid获取注销的基本信息
	 *
	 * @作者 海豹
	 * @创建时间 2015年6月18日上午12:09:28
	 * @param qlid
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Map getZxxxInfo(String qlid) {
		BDCS_FSQL_GZ fsql = new BDCS_FSQL_GZ();
		BDCS_QL_GZ ql = baseCommonDao.get(BDCS_QL_GZ.class, qlid);
		Map<String, String> map = new HashMap<String, String>();
		String zxdyyy = ""; // 注销抵押原因
		String zxfj = ""; // 注销附记
		if (ql != null) {
			fsql = baseCommonDao.get(BDCS_FSQL_GZ.class, ql.getFSQLID());
			if (fsql != null) {
				zxdyyy = fsql.getZXDYYY();
				zxfj = fsql.getZXFJ();
			}
		}
		map.put("zxdyyy", zxdyyy); // 注销抵押原因
		map.put("zxfj", zxfj); // 注销附记
		return map;
	}

	/**
	 * 更新注销中的；通过qlid更新ql_gz中的fj，及更新fsql_gz中的内容
	 *
	 * @作者 海豹
	 * @创建时间 2015年6月18日上午12:08:35
	 * @param qlid
	 * @param object
	 */
	@Override
	public void updateZxxxInfo(String qlid, JSONObject object) {
		BDCS_QL_GZ ql = baseCommonDao.get(BDCS_QL_GZ.class, qlid);
		if (ql != null) {
			BDCS_FSQL_GZ fsql = baseCommonDao.get(BDCS_FSQL_GZ.class, ql.getFSQLID());
			if (fsql != null) {
				fsql.setZXDYYY(object.getString("zxdyyy"));
				fsql.setZXFJ(object.getString("zxfj"));
				baseCommonDao.update(fsql);
			}
			baseCommonDao.flush();
		}

	}

	/**
	 * 批量更新注销信息：通过项目编号更新
	 * @作者 海豹
	 * @创建时间 2015年8月4日下午10:29:17
	 * @param xmbh
	 * @param object
	 */
	@Override
	public void plUpdateZxxxInfo(String xmbh, JSONObject object) {
		String sql = ProjectHelper.GetXMBHCondition(xmbh);
		List<BDCS_QL_GZ> qls = baseCommonDao.getDataList(BDCS_QL_GZ.class, sql);
		if (qls != null && qls.size() > 0) {
			for (BDCS_QL_GZ bdcs_ql_gz : qls) {
				BDCS_FSQL_GZ bdcs_fsql_gz = baseCommonDao.get(BDCS_FSQL_GZ.class, bdcs_ql_gz.getFSQLID());
				if (bdcs_fsql_gz != null) {
					bdcs_fsql_gz.setZXDYYY(object.getString("zxdyyy"));
					bdcs_fsql_gz.setZXFJ(object.getString("zxfj"));
					bdcs_fsql_gz.setZXDYYWH(object.getString("zxdyywh"));
					baseCommonDao.update(bdcs_fsql_gz);
				}
			}
			baseCommonDao.flush();
		}
	}

	/********************************** 预告登记 ***************************************/

	/********************************** 预告登记 ***************************************/

	/**
	 * 通过xmbh获取页面初始的预告登记权利信息
	 *
	 * @作者 李想
	 * @创建时间 2015年6月18日下午19:38:28
	 * @param xmbh
	 */
	@Override
	public Map<String, String> getYgxxInfoInit(String project_id) {
		ProjectInfo info = ProjectHelper.GetPrjInfoByPrjID(project_id);
		String hql = MessageFormat.format(" XMBH=''{0}'' and SQRLB='2'", info.getXmbh());// 义务人
		List<BDCS_SQR> list = baseCommonDao.getDataList(BDCS_SQR.class, hql);
		StringBuilder ywrs = new StringBuilder();
		for (int i = 0; i < list.size(); i++) {
			if (ywrs.length() == 0) {
				ywrs.append(list.get(0).getSQRXM());
			} else {
				ywrs.append(",").append(list.get(0).getSQRXM());
			}
		}
		Map<String, String> map = new HashMap<String, String>();
		map.put("yWR", ywrs.toString());
		map.put("dJYY", "");
		map.put("Fj", "");
		map.put("id", "");
		return map;
	}

	/**
	 * 通过xmbh获取预告登记权利信息
	 *
	 * @作者 李想
	 * @创建时间 2015年6月18日下午19:38:28
	 * @param xmbh
	 */
	@Override
	public Map<String, String> getYgxxInfo(String xmbh) {
		return null;
	}

	/**
	 * 获取异议登记权利信息，通过来源权利ID
	 *
	 * @作者：俞学斌
	 * @创建时间 2015年6月30日 上午2:09:01
	 * @param lyqlid
	 * @return
	 */
	@Override
	public Map<String, String> GetYYDJQL(String qlid) {
		Map<String, String> map = new HashMap<String, String>();
		BDCS_QL_GZ ql = baseCommonDao.get(BDCS_QL_GZ.class, qlid);
		if (ql != null) {
			BDCS_FSQL_GZ fsql = baseCommonDao.get(BDCS_FSQL_GZ.class, ql.getFSQLID());
			if (fsql != null) {
				map.put("yysx", fsql.getYYSX());
			} else {
				map.put("yysx", "");
			}
			map.put("fj", ql.getFJ());
		} else {
			map.put("yysx", "");
			map.put("fj", "");
		}
		return map;
	}

	/**
	 * 更新异议登记权利信息，通过来源权利ID
	 *
	 * @作者：俞学斌
	 * @创建时间 2015年6月30日 上午2:09:01
	 * @param lyqlid
	 * @return
	 */
	@Override
	public boolean updateYYDJQL(String qlid, HttpServletRequest request) {
		boolean bResult = false;
		BDCS_QL_GZ ql = baseCommonDao.get(BDCS_QL_GZ.class, qlid);
		if (ql != null) {
			BDCS_FSQL_GZ fsql = baseCommonDao.get(BDCS_FSQL_GZ.class, ql.getFSQLID());
			if (fsql != null) {
				fsql.setYYSX(request.getParameter("dJYY"));
			}
			ql.setFJ(request.getParameter("fJ"));
			baseCommonDao.flush();
			bResult = true;
		}
		return bResult;
	}

	@Override
	public Map<String, Object> getDyqZyInfo(String xmbh, String qlid) {
		// 根据qlid通过工作查询出ylqlid ，再查询现状的权利信息
		String hqlCondition = " id in (SELECT LYQLID FROM BDCS_QL_GZ WHERE id ='" + qlid + "') ";
		List<BDCS_QL_XZ> qls = baseCommonDao.getDataList(BDCS_QL_XZ.class, hqlCondition);
		if (qls.size() == 0) {
			// TODO 这里需要修改
			qls = baseCommonDao.getDataList(BDCS_QL_XZ.class, " id = '" + qlid + "' ");
		}
		BDCS_QL_XZ ql = null;
		Map<String, Object> map = new HashMap<String, Object>();
		if (null != qls && qls.size() > 0) {
			ql = qls.get(0);
		}
		if (null != ql) {
			BDCS_FSQL_XZ fsql = baseCommonDao.get(BDCS_FSQL_XZ.class, ql.getFSQLID());
			// XMBH=''{0}'' AND
			String hql = MessageFormat.format(" QLID=''{0}''", ql.getId());
			List<BDCS_QLR_XZ> list = baseCommonDao.getDataList(BDCS_QLR_XZ.class, hql);

			String qlrmc = "";
			String sqrid = "";
			for (int i = 0; i < list.size(); i++) {
				BDCS_QLR_XZ qlr = list.get(i);
				if (list.size() - 1 == i) {
					qlrmc += qlr.getQLRMC();
					sqrid += qlr.getSQRID();
				} else {
					qlrmc += qlr.getQLRMC() + ",";
					sqrid += qlr.getSQRID() + ",";
				}
			}

			String qssj = ql.getQLQSSJ() == null ? "" : ql.getQLQSSJ().toString();
			String jssj = ql.getQLJSSJ() == null ? "" : ql.getQLJSSJ().toString();

			map.put("qlrmc", qlrmc); // 权利人名称
			map.put("sqrid", sqrid); // 权利人
			map.put("dyr", fsql.getDYR()); // 抵押权人
			map.put("dybdclx", fsql.getDYWLXName()); // 抵押不动产类型
			map.put("zjjzwzl", fsql.getZJJZWZL()); // 在建建筑物坐落
			map.put("dyfs", fsql.getDYFS()); // 抵押方式
			map.put("bdbzzqse", fsql.getBDBZZQSE()); // 被担保主债权数额
			map.put("zjjzwdyfw", fsql.getZJJZWDYFW()); // 在建建筑物抵押范围
			map.put("qljssj", jssj); // 债务旅行结束时间
			map.put("qlqssj", qssj); // 债务履行起始时间
			map.put("zgzqse", fsql.getZGZQSE()); // 最高债权数额
			map.put("zgzqqdss", fsql.getZGZQQDSS()); // 最高债权确定事实
			map.put("fj", ql.getFJ()); // 附记
			map.put("djyy", ql.getDJYY()); // 登记原因
			map.put("ydzmj", fsql.getYDZMJ()); // 用地总面积
			map.put("dyydmj", fsql.getDYYDMJ()); // 抵押用地总面积
			map.put("zjzmj", fsql.getZJZMJ()); // 总建筑面积
			map.put("dyjzmj", fsql.getDYJZMJ()); // 抵押建筑面积
			map.put("dytdyt", fsql.getDYTDYT()); // 土地用途
		}
		return map;
	}

	@Override
	public List<BDCS_QLR_XZ> getSYQXZQLRList(String dyqQlid, String qllx) {
		StringBuilder hql = new StringBuilder();
		hql.append(" QLID IN ( ");
		hql.append(" SELECT id FROM BDCS_QL_XZ WHERE DJDYID IN ");
		hql.append(" (SELECT DJDYID FROM BDCS_QL_GZ WHERE id ='").append(dyqQlid).append("') ");
		hql.append(" AND QLLX = '").append(qllx).append("') ");
		List<BDCS_QLR_XZ> qlrs = new ArrayList<BDCS_QLR_XZ>();
		qlrs = baseCommonDao.getDataList(BDCS_QLR_XZ.class, hql.toString());
		return qlrs;
	}

	@Override
	public BDCS_QLR_XZ GetXZQLRInfo(String qlrid) {
		return baseCommonDao.get(BDCS_QLR_XZ.class, qlrid);
	}

	private static Map<String, String> entityfieldnames = new HashMap<String, String>();

	private String getTableFieldsName(String tableName, String prefix) throws ClassNotFoundException {
		String str = "";
		if (!entityfieldnames.containsKey(tableName.toUpperCase())) {
			StringBuilder builder = new StringBuilder();
			Class<?> t = Class.forName("com.supermap.realestate.registration.model." + tableName.toUpperCase());
			Method[] mds = t.getDeclaredMethods();
			for (Method md : mds) {
				Column c = md.getAnnotation(Column.class);
				if (c != null) {
					if (c.name().toUpperCase().equals("ZSEWM") || c.name().toUpperCase().equals("FCFHT") || c.name().toUpperCase().equals("FCFHTWJ")) {
						continue;
					} else {
						builder.append(prefix + "." + c.name() + " as " + prefix + "_" + c.name() + ",");
					}
				}
			}
			str = builder.toString();
			str = str.substring(0, str.length() - 1);
			entityfieldnames.put(tableName.toUpperCase(), str);
		}
		str = entityfieldnames.get(tableName.toUpperCase());
		return str;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private List<Map> revmoeprefix(List<Map> listresult) {
		List<Map> list = new ArrayList<Map>();
		for (Map map : listresult) {
			Map mp = new HashMap<String, Object>();
			for (Object str : map.keySet()) {
				if (((String) str).toUpperCase().equals("DY_QLLX") || ((String) str).toUpperCase().equals("FSQL_BDCDYH"))
					continue;
				String strnew = replaceXHX((String) str);
				try {
					if (!StringHelper.isEmpty(strnew)) {
						// 权利类型，要用权利表里的
						if (!mp.containsKey(strnew)) {
							mp.put(strnew, map.get(str));
						}
					}
				} catch (Exception e) {
					System.out.println(str);
				}
			}
			list.add(mp);
		}
		return list;
	}

	private String replaceXHX(String str) {
		String strnew = "";
		try {
			int index = str.indexOf("_");
			if (index > 0 && index < str.length() - 1) {
				strnew = str.substring(index + 1, str.length());
			}
		} catch (Exception e) {
			System.out.println("出错了:" + e.getMessage());
		}
		return strnew;
	}

	/**
	 * (非 Javadoc) <p>Title: getRightsAndSubRights</p> <p>Description: </p>
	 * @param ly
	 * @param qlid
	 * @return
	 * @throws ClassNotFoundException
	 * @see com.supermap.realestate.registration.service.QLService#getRightsAndSubRights(com.supermap.realestate.registration.util.ConstValue.DJDYLY,
	 *      java.lang.String)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Map getRightsAndSubRights(DJDYLY ly, String qlid) throws ClassNotFoundException {
		Map map = null;
		String qltablename = "BDCS_QL_GZ";
		String fsqltablename = "BDCS_FSQL_GZ";
		if (ly.equals(DJDYLY.XZ)) {
			qltablename = "BDCS_QL_XZ";
			fsqltablename = "BDCS_FSQL_XZ";
		}
		if (ly.equals(DJDYLY.LS)) {
			qltablename = "BDCS_QL_LS";
			fsqltablename = "BDCS_FSQL_LS";
		}

		String qlfieldsname = getTableFieldsName(qltablename, "QL");
		String fsqlfieldsname = getTableFieldsName(fsqltablename, "FSQL");
		StringBuilder builder2 = new StringBuilder();
		builder2.append("select ").append(qlfieldsname).append(",").append(fsqlfieldsname)
				.append(" FROM BDCK." + qltablename + " QL LEFT JOIN BDCK." + fsqltablename + " FSQL ON QL.QLID=FSQL.QLID WHERE QL.QLID='" + qlid + "'");

		List<Map> list = baseCommonDao.getDataListByFullSql(builder2.toString());
		if (list != null && list.size() > 0) {
			list = revmoeprefix(list);
			map = list.get(0);
		}
		return map;
	}


	/**
	 * 注销信息显示： 通过权利id从BDCS_FSQL_GZZ中获取数据
	 *
	 * @作者 俞学斌
	 * @创建时间 2015年12月05日 18:32:16
	 * @param xmbh
	 * @param qlid
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Map getLoadZxxxInfo(String qlid, String xmbh) {
		BDCS_FSQL_GZ fsql = new BDCS_FSQL_GZ();
		BDCS_QL_GZ ql = baseCommonDao.get(BDCS_QL_GZ.class, qlid);
		Map<String, String> map = new HashMap<String, String>();
		String zxdyyy = ""; // 注销抵押原因
		String zxfj = ""; // 注销附记
		if (ql != null) {
			fsql = baseCommonDao.get(BDCS_FSQL_GZ.class, ql.getFSQLID());
			if (fsql != null) {
				zxdyyy = fsql.getZXDYYY();
				zxfj = fsql.getZXFJ();
			}
		}
		map.put("djyy", zxdyyy); // 注销抵押原因
		map.put("fj", zxfj); // 注销附记
		return map;
	}


	/**
	 * 根据项目编号和土地用途更新户中土地用途
	 *
	 * @作者 俞学斌
	 * @创建时间 2015年12月05日 18:06:16
	 * @param djdyid
	 * @param tdyt
	 * @param xmbh
	 * @return
	 */
	@Override
	public void Updatetdyt(String djdyid, String tdyt, String xmbh) {
		List<BDCS_DJDY_GZ> djdys =baseCommonDao.getDataList(BDCS_DJDY_GZ.class, "XMBH='"+xmbh+"' AND DJDYID='"+djdyid+"'");
		if(djdys!=null&&djdys.size()>0){
			BDCS_DJDY_GZ djdy=djdys.get(0);
			BDCDYLX lx=BDCDYLX.initFrom(djdy.getBDCDYLX());
			DJDYLY ly=DJDYLY.initFrom(djdy.getLY());
			if(BDCDYLX.H.equals(lx)||BDCDYLX.YCH.equals(lx)){
				House h=(House)UnitTools.loadUnit(lx, ly, djdy.getBDCDYID());
				if(h!=null){
					h.setFWTDYT(tdyt);
					baseCommonDao.update(h);
				}
			}
		}
	}

	/**
	 * 获取单元限制详细信息
	 *
	 * @Title: getXZInfo
	 * @author:yuxuebin
	 * @date：2016年01月07日 下午11:23:18
	 * @param xzdyid
	 * @param request
	 * @param response
	 * @return
	 */
	@Override
	public BDCS_DYXZ getXZInfo(String xzdyid) {
		BDCS_DYXZ dyxz =baseCommonDao.get(BDCS_DYXZ.class, xzdyid);
		return dyxz;
	}

	/**
	 * 更新单元限制详细信息（URL:"/{xzdyid}/xzinfo/",Method：POST）
	 *
	 * @Title: updateXZInfo
	 * @author:yuxuebin
	 * @date：2016年01月07日 下午11:23:18
	 * @param xzdyid
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public boolean updateXZInfo(String xzdyid,HttpServletRequest request) {
		Map map = transToMAP(request.getParameterMap());
		BDCS_DYXZ dyxz =baseCommonDao.get(BDCS_DYXZ.class, xzdyid);
		if(dyxz!=null){
			setValue(map, dyxz);
			baseCommonDao.update(dyxz);
			baseCommonDao.flush();
		}else{
			return false;
		}
		return true;
	}
	public boolean updatePlXZInfo(String xmbh, HttpServletRequest request){
		Map map = transToMAP(request.getParameterMap());
		List<BDCS_DYXZ> list=baseCommonDao.getDataList(BDCS_DYXZ.class, " XMBH='"+xmbh+"'");
		for (BDCS_DYXZ dyxz : list) {
			setValue(map, dyxz);
			baseCommonDao.update(dyxz);
			baseCommonDao.flush();
		}
		return true;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Map transToMAP(Map parameterMap) {
		// 返回值Map
		Map returnMap = new HashMap();
		Iterator entries = parameterMap.entrySet().iterator();
		Map.Entry entry;
		String name = "";
		String value = "";
		while (entries.hasNext()) {
			entry = (Map.Entry) entries.next();
			name = (String) entry.getKey();
			Object valueObj = entry.getValue();
			if (null == valueObj) {
				value = "";
			} else if (valueObj instanceof String[]) {
				String[] values = (String[]) valueObj;
				for (int i = 0; i < values.length; i++) {
					value = values[i] + ",";
				}
				value = value.substring(0, value.length() - 1);
			} else {
				value = valueObj.toString();
			}
			returnMap.put(name, value);
		}
		return returnMap;
	}

	@SuppressWarnings("rawtypes")
	public void setValue(Map map, Object thisObj) {
		Set set = map.keySet();
		Iterator iterator = set.iterator();
		while (iterator.hasNext()) {
			Object obj = iterator.next();
			Object val = map.get(obj);
			if(obj.equals("zjjzwzl"))
				continue;
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
	 * 获取单元限制列表
	 *
	 * @Title: getXZInfoList
	 * @author:yuxuebin
	 * @date：2016年01月08日 下午11:23:18
	 * @param bdcdyid
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<Map> getXZInfoList(String bdcdyid,String xmbh) {
		StringBuilder builder=new StringBuilder();
		builder.append("SELECT DYXZ.ID,DYXZ.XZWJHM,DYXZ.XZDW,DYXZ.SLRYJ,DYXZ.XZFW,DYXZ.XZLX,NVL2(XMDYXZ.ID,1,0) AS ISLIFTED,XMDYXZ.ZXXZWJHM,XMDYXZ.ZXXZDW,XMDYXZ.ZXYJ,XMDYXZ.ZXBZ  ");//,XMDYXZ.XMBH
		builder.append("FROM BDCK.BDCS_DYXZ DYXZ LEFT JOIN BDCK.BDCS_XM_DYXZ XMDYXZ ");
		builder.append("ON DYXZ.ID=XMDYXZ.DYXZID AND XMDYXZ.XMBH='");
		builder.append(xmbh).append("' ");
		builder.append("WHERE  DYXZ.BDCDYID='").append(bdcdyid).append("'");
		List<Map> list=baseCommonDao.getDataListByFullSql(builder.toString());
		if(list!=null&&list.size()>0){
			for(Map m:list){
				String xzlx=StringHelper.formatObject(m.get("XZLX"));
				String xzlxname="";
				if (!StringHelper.isEmpty(xzlx)) {
					xzlxname = ConstHelper.getNameByValue("XZLX", xzlx);
				}
				m.put("XZLXNAME", xzlxname);
			}
		}
		return list;
	}

	/**
	 * 解除单元限制列表
	 *
	 * @Title: DYXZLifted
	 * @author:yuxuebin
	 * @date：2016年01月08日 下午11:23:18
	 * @param xzdyid
	 * @param xmbh
	 * @param request
	 * @param response
	 * @return
	 */
	@Override
	public ResultMessage DYXZLifted(String xzdyid, String xmbh) {
		ResultMessage ms=new ResultMessage();
		ms.setSuccess("false");
		List<BDCS_XM_DYXZ> list=baseCommonDao.getDataList(BDCS_XM_DYXZ.class, "DYXZID='"+xzdyid+"' AND XMBH='"+xmbh+"'");
		if(list.size()>0){
			for(BDCS_XM_DYXZ xmdyxz:list){
				baseCommonDao.deleteEntity(xmdyxz);
			}
			baseCommonDao.flush();
			ms.setSuccess("true");
			ms.setMsg(xzdyid);
		}else{
			List<BDCS_XM_DYXZ> lists=baseCommonDao.getDataList(BDCS_XM_DYXZ.class, "XMBH='"+xmbh+"'");
			if(lists.size()>0){
				for(BDCS_XM_DYXZ xmdyxz:lists){
					baseCommonDao.deleteEntity(xmdyxz);
				}
			}
			BDCS_DYXZ dyxzxmbh =baseCommonDao.get(BDCS_DYXZ.class, xzdyid);
			List<BDCS_DYXZ> dyxz=baseCommonDao.getDataList(BDCS_DYXZ.class, "XMBH='"+StringHelper.formatObject(dyxzxmbh.getXMBH())+"'");
			if(dyxz.size()>0){
				for (BDCS_DYXZ xmdyxz : dyxz) {
					BDCS_XM_DYXZ xmdyxzs=new BDCS_XM_DYXZ();
					xmdyxzs.setBDCDYID(xmdyxz.getBDCDYID());
					xmdyxzs.setBDCDYLX(xmdyxz.getBDCDYLX());
					xmdyxzs.setDYXZID(xmdyxz.getId());
					xmdyxzs.setXMBH(xmbh);
					String id = SuperHelper.GeneratePrimaryKey();
					xmdyxzs.setId(id);
					baseCommonDao.save(xmdyxzs);
					baseCommonDao.flush();
				}
				ms.setSuccess("true");
				ms.setMsg(xzdyid);
			}
		}
		return ms;
	}

	/**
	 * 获取解除单元限制详细信息
	 *
	 * @Title: getZXXZInfo
	 * @author:yuxuebin
	 * @date：2016年01月07日 下午11:23:18
	 * @param xzdyid
	 * @param request
	 * @param response
	 * @return
	 */
	@Override
	public BDCS_XM_DYXZ getZXXZInfo(String xzdyid, String xmbh) {
		List<BDCS_XM_DYXZ> list=baseCommonDao.getDataList(BDCS_XM_DYXZ.class, "DYXZID='"+xzdyid+"' AND XMBH='"+xmbh+"'");
		if(list!=null&&list.size()>0){
			return list.get(0);
		}
		return null;
	}

	/**
	 * 更新解除单元限制详细信息
	 *
	 * @Title: updateZXXZInfo
	 * @author:yuxuebin
	 * @date：2016年01月07日 下午11:23:18
	 * @param xzdyid
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public boolean updateZXXZInfo(String xzdyid,String xmbh,HttpServletRequest request) {
		Map map = transToMAP(request.getParameterMap());
		List<BDCS_XM_DYXZ> list=baseCommonDao.getDataList(BDCS_XM_DYXZ.class, "DYXZID='"+xzdyid+"' AND XMBH='"+xmbh+"'");
		if(list!=null&&list.size()>0){
			BDCS_XM_DYXZ xmdyxz=list.get(0);
			if(xmdyxz!=null){
				setValue(map, xmdyxz);
				baseCommonDao.update(xmdyxz);
				baseCommonDao.flush();
				return true;
			}else{
				return false;
			}
		}
		return false;
	}
	//批量更新解除单元限制
	public boolean updatePlZXXZInfo(String xmbh, HttpServletRequest request){
		Map map = transToMAP(request.getParameterMap());
		List<BDCS_XM_DYXZ> list=baseCommonDao.getDataList(BDCS_XM_DYXZ.class, "XMBH='"+xmbh+"'");
		for (BDCS_XM_DYXZ xmdyxz : list) {
			setValue(map, xmdyxz);
			baseCommonDao.update(xmdyxz);
			baseCommonDao.flush();
		}
		return true;
	}


	@Override
	public Map<String, Object> getBGdyqInfo(String xmbh,String type) {
		Map<String, Object> map=new HashMap<String, Object>();
		List<Rights> qls =RightsTools.loadRightsByCondition(DJDYLY.GZ, " XMBH='"+xmbh+"' AND ISCANCEL<>'2' AND  ISCANCEL<>'1' ");//加上ISCANCEL<>'1' 取消的权利不记入内
		if(qls!=null&&qls.size()>0){
			Rights ql=qls.get(0);
			DJDYLY ly=DJDYLY.GZ;
			//String Baseworkflow_ID =ProjectHelper.GetPrjInfoByXMBH(xmbh).getBaseworkflowcode();
			if("bgq".equals(type)){
				ql=RightsTools.loadRights(DJDYLY.LS, ql.getLYQLID());
				ly=DJDYLY.LS;
			}
			if (ql != null) {
				SubRights fsql=RightsTools.loadSubRightsByRightsID(ly, ql.getId());
				List<RightsHolder> list=RightsHolderTools.loadRightsHolders(ly, ql.getId());
				String qlrmc = "";
				String sqrid = "";
				for (int i = 0; i < list.size(); i++) {
					RightsHolder qlr = list.get(i);
					if (list.size() - 1 == i) {
						qlrmc += qlr.getQLRMC();
						sqrid += qlr.getSQRID();
					} else {
						qlrmc += qlr.getQLRMC() + ",";
						sqrid += qlr.getSQRID() + ",";
					}
				}
				String qssj = ql.getQLQSSJ() == null ? "" : ql.getQLQSSJ().toString();
				String jssj = ql.getQLJSSJ() == null ? "" : ql.getQLJSSJ().toString();
				map.put("qlrmc", qlrmc); // 权利人名称
				map.put("sqrid", sqrid); // 权利人
				String dywlxname="";
				if(!StringHelper.isEmpty(fsql.getDYWLX())){
					dywlxname=ConstHelper.getNameByValue("DYBDCLX", fsql.getDYWLX());
				}
				map.put("dybdclx", dywlxname); // 抵押不动产类型
				map.put("zjjzwzl", fsql.getZJJZWZL()); // 在建建筑物坐落
				map.put("dyfs", fsql.getDYFS()); // 抵押方式
				map.put("bdbzzqse", fsql.getBDBZZQSE()); // 被担保主债权数额
				map.put("zjjzwdyfw", fsql.getZJJZWDYFW()); // 在建建筑物抵押范围
				map.put("qljssj", jssj); // 债务旅行结束时间
				map.put("qlqssj", qssj); // 债务履行起始时间
				map.put("zgzqse", fsql.getZGZQSE()); // 最高债权数额
				map.put("zgzqqdss", fsql.getZGZQQDSS()); // 最高债权确定事实
				map.put("fj", ql.getFJ()); // 附记
				map.put("djyy", ql.getDJYY()); // 登记原因
				map.put("dyr", fsql.getDYR()); // 登记原因
				map.put("bdcqzh", ql.getBDCQZH()); // 产权证号
				map.put("dypgjz", fsql.getDYPGJZ()); // 登记原因
				map.put("ydzmj", fsql.getYDZMJ()); // 用地总面积
				map.put("dyydmj", fsql.getDYYDMJ()); // 抵押用地总面积
				map.put("zjzmj", fsql.getZJZMJ()); // 总建筑面积
				map.put("dyjzmj", fsql.getDYJZMJ()); // 抵押建筑面积
				map.put("dytdyt", fsql.getDYTDYT()); // 土地用途
			}
		}
		return map;
	}

	/**
	 * 获取抵押权变更抵押权人权利信息
	 *
	 * @作者 YUXUEBIN
	 * @创建时间 2016年01月10日下午5:54:53
	 * @param request
	 * @param response
	 * @return
	 */
	@Override
	public List<RightsHolder> getBGdyqrInfo(String xmbh,String type) {
		List<RightsHolder> list=new ArrayList<RightsHolder>();
		List<BDCS_QL_GZ> qls = baseCommonDao.getDataList(BDCS_QL_GZ.class, "XMBH='"+xmbh+"'");
		if(qls!=null&&qls.size()>0){
			if("bgq".equals(type)){
				list= RightsHolderTools.loadRightsHolders(DJDYLY.XZ, qls.get(0).getLYQLID());
			}else{
				list= RightsHolderTools.loadRightsHolders(DJDYLY.GZ, qls.get(0).getId());
			}
		}
		return list;
	}
	
	/**
	 * 获取（当前一条）抵押权变更 抵押权人信息
	 * @作者 WLB
	 * @创建时间 2017年05月25日下午22:54:53
	 * @param request
	 * @param response
	 * @return
	 */
	@Override
	public BDCS_QLR_GZ getCurrentDyqrInfo(String qlrid) {
		return baseCommonDao.get(BDCS_QLR_GZ.class, qlrid);
	}
	
   /**
     * 修改变更前抵押权人信息（URL:"{xmbh}/modifybgqdyqlrs/qlrinfo/",Method：POST）
     * 
     * @作者 WEILIBO
	 * @创建时间 2017年05月24日下午20:57:38
	 * @param request
	 * @param response
	 * @return
     */
	@SuppressWarnings("rawtypes")
	@Override
	public void modifyBgqDyqlrs(Map map,String xmbh) {
		List<BDCS_QLR_GZ> qlrs = baseCommonDao.getDataList(BDCS_QLR_GZ.class, " XMBH='"+xmbh+"'");
		if(qlrs!=null && qlrs.size()>0){
			for(BDCS_QLR_GZ qlr : qlrs){
				setValue(map, qlr);
				baseCommonDao.update(qlr);
			}
		}
		baseCommonDao.flush();
	}
	/**
	 * 保存抵押权变更权利信息,只保存当前单元的抵押权信息
	 *
	 * @作者 YUXUEBIN
	 * @创建时间 2016年01月10日下午5:54:53
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public void plSaveQlInfo2(String xmbh,String bdcdyid, HttpServletRequest request) {
		Map map = transToMAP(request.getParameterMap());
		List<BDCS_QL_GZ> qls=baseCommonDao.getDataList(BDCS_QL_GZ.class, "XMBH='"+xmbh+"' and bdcdyid='"+bdcdyid+"'");
		if(qls!=null&&qls.size()>0){
			for(BDCS_QL_GZ ql:qls){
				BDCS_FSQL_GZ fsql = baseCommonDao.get(BDCS_FSQL_GZ.class, ql.getFSQLID());
				setValue(map, ql);
				setValue(map, fsql);
				baseCommonDao.update(ql);
				baseCommonDao.update(fsql);
			}
		}
		baseCommonDao.flush();
	}
	
	/**
	 * 保存抵押权变更权利信息,只保存当前单元的抵押权信息
	 *
	 * @作者 YUXUEBIN
	 * @创建时间 2016年01月10日下午5:54:53
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public void plSaveQlInfo2(String xmbh, HttpServletRequest request) {
		Map map = transToMAP(request.getParameterMap());
		List<BDCS_QL_GZ> qls=baseCommonDao.getDataList(BDCS_QL_GZ.class, "XMBH='"+xmbh+"'");
		if(qls!=null&&qls.size()>0){
			for(BDCS_QL_GZ ql:qls){
				BDCS_FSQL_GZ fsql = baseCommonDao.get(BDCS_FSQL_GZ.class, ql.getFSQLID());
				setValue(map, ql);
				setValue(map, fsql);
				baseCommonDao.update(ql);
				baseCommonDao.update(fsql);
			}
		}
		baseCommonDao.flush();
	}
	
	/**
	 * 解除单元抵押
	 *
	 * @Title: dybgCancel
	 * @author:yuxuebin
	 * @date：2016年01月08日 下午11:23:18
	 * @param qlid
	 * @param xmbh
	 * @param request
	 * @param response
	 * @return
	 */
	@Override
	public ResultMessage dybgCancel(String qlid, String xmbh) {
		ResultMessage ms=new ResultMessage();
		ms.setSuccess("false");
		BDCS_QL_GZ ql=baseCommonDao.get(BDCS_QL_GZ.class, qlid);
		if(ql!=null){
			if("2".equals(ql.getISCANCEL())){
				String djdyid=ql.getDJDYID();
				if(!StringHelper.isEmpty(djdyid)){
					baseCommonDao.deleteEntitysByHql(BDCS_DJDY_GZ.class, "DJDYID='"+djdyid+"'");
					RightsTools.deleteRightsAll(DJDYLY.GZ, ql.getId());
				}
			}else if("1".equals(ql.getISCANCEL())){
		//--------------------------------------如果权利没注销，则根据配置将不动产权证号设为空
				String newqzh = "";
				BDCS_XMXX xmxx=baseCommonDao.get(BDCS_XMXX.class,xmbh);
				if(xmxx!=null){
				String workflowcode = ProjectHelper.getWorkflowCodeByProjectID(xmxx.getPROJECT_ID());
				List<WFD_MAPPING> listCode = baseCommonDao.getDataList(WFD_MAPPING.class, "WORKFLOWCODE='" + workflowcode + "'");
				if (listCode != null && listCode.size() > 0) {
					newqzh = listCode.get(0).getNEWQZH();
				}
				}
				if (SF.YES.Value.equals(newqzh))
				ql.setBDCQZH("");
		//-------------------------------------------------
				ql.setISCANCEL("0");
			}else{
				Rights ql_ls= RightsTools.loadRights(DJDYLY.LS, ql.getLYQLID());
				ql.setBDCQZH(ql_ls.getBDCQZH());//如果是注销权利，把不动产权证号还回来，因为注销的不动产单元号不需要重新生成产权证号-wuzhu20161212
				ql.setISCANCEL("1");
			}

			baseCommonDao.flush();
			ms.setSuccess("true");
			ms.setMsg(qlid);
		}
		return ms;
	}

	@Override
	public boolean RemoveXZInfo(String xzdyid, String xmbh) {
		BDCS_DYXZ dyxz=baseCommonDao.get(BDCS_DYXZ.class, xzdyid);
		if(dyxz!=null){
			baseCommonDao.deleteEntity(dyxz);
		}
		List<BDCS_XM_DYXZ> xmdyxzs=baseCommonDao.getDataList(BDCS_XM_DYXZ.class, " DYXZID='"+xzdyid+"' AND XMBH='"+xmbh+"'");
		if(xmdyxzs!=null&&xmdyxzs.size()>0){
			for(BDCS_XM_DYXZ xmdyxz:xmdyxzs){
				baseCommonDao.deleteEntity(xmdyxz);
			}
		}
		baseCommonDao.flush();
		return true;
	}

	/**
	 * 添加单元限制详细信息
	 *
	 * @Title: AddXZInfo
	 * @author:yuxuebin
	 * @date：2016年01月15日 01:10:18
	 * @param bdcdyid
	 * @param xmbh
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public ResultMessage AddXZInfo(String bdcdyid, String xmbh,String bdcdylx,
			HttpServletRequest request) {
		ResultMessage ms=new ResultMessage();
		ms.setSuccess("false");
		if(StringHelper.isEmpty(bdcdyid)){
			ms.setMsg("单元不存在！");
			return ms;
		}
		Map map = transToMAP(request.getParameterMap());
		BDCS_XMXX xmxx=baseCommonDao.get(BDCS_XMXX.class, xmbh);
		if(StringHelper.isEmpty(xmxx)){
			ms.setMsg("项目不存在！");
			return ms;
		}
		DJDYLY ly=DJDYLY.XZ;
		RealUnit unit = UnitTools.loadUnit(BDCDYLX.initFrom(bdcdylx), ly, bdcdyid);
		if(StringHelper.isEmpty(unit)){
			ms.setMsg("单元不存在！");
			return ms;
		}
		//添加限制
		BDCS_DYXZ dyxz=new BDCS_DYXZ();
		setValue(map, dyxz);
		String dyxzid=SuperHelper.GeneratePrimaryKey();
		dyxz.setXMBH(xmbh);
		dyxz.setId(dyxzid);
		dyxz.setBDCDYID(bdcdyid);
		dyxz.setBDCDYLX(bdcdylx);
		dyxz.setYXBZ("0");
		dyxz.setBDCDYH(unit.getBDCDYH());
		dyxz.setSLR(xmxx.getSLRY());
		dyxz.setYWH(xmxx.getPROJECT_ID());
		//添加項目-限制關係
		BDCS_XM_DYXZ xmdyxz=new BDCS_XM_DYXZ();
		String xmdyxzid=SuperHelper.GeneratePrimaryKey();
		xmdyxz.setId(xmdyxzid);
		xmdyxz.setDYXZID(dyxzid);
		xmdyxz.setBDCDYID(dyxz.getBDCDYID());
		xmdyxz.setBDCDYLX(dyxz.getBDCDYLX());
		xmdyxz.setXMBH(xmbh);

		baseCommonDao.save(dyxz);
		baseCommonDao.save(xmdyxz);
		baseCommonDao.flush();
		ms.setMsg("添加成功！");
		ms.setSuccess("true");
		return ms;
	}

	/**
	 * 获取单元限制列表
	 *
	 * @Title: getXZInfoList
	 * @author:yuxuebin
	 * @date：2016年01月08日 下午11:23:18
	 * @param bdcdyid
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<Map> getXZInfoListFromGZ(String bdcdyid, String xmbh) {
		StringBuilder builder=new StringBuilder();
		builder.append("SELECT DYXZ.ID,DYXZ.XZWJHM,DYXZ.XZDW,DYXZ.SLRYJ,DYXZ.XZFW,DYXZ.BZ,DYXZ.XZLX ");
		builder.append("FROM BDCK.BDCS_DYXZ DYXZ LEFT JOIN BDCK.BDCS_XM_DYXZ XMDYXZ ");
		builder.append("ON DYXZ.ID=XMDYXZ.DYXZID AND XMDYXZ.XMBH='");
		builder.append(xmbh).append("' ");
		builder.append("WHERE DYXZ.YXBZ='0' AND DYXZ.BDCDYID='").append(bdcdyid).append("'");
		List<Map> list=baseCommonDao.getDataListByFullSql(builder.toString());
		if(list!=null&&list.size()>0){
			for(Map m:list){
				String xzlx=StringHelper.formatObject(m.get("XZLX"));
				String xzlxname="";
				if (!StringHelper.isEmpty(xzlx)) {
					xzlxname = ConstHelper.getNameByValue("XZLX", xzlx);
				}
				m.put("XZLXNAME", xzlxname);
			}
		}
		return list;
	}

	/**
	 * 批量更新权利人信息
	 * @Title: batchUpdateQLR
	 * @author:liushufeng
	 * @date：2016年3月16日 上午11:42:43
	 * @param holder
	 *            权利人信息
	 * @param option
	 *            更新选项，1：按照权利人名称批量更新，2：按照权利人证件号批量更新，3：按照名称和证件号更新
	 * @return
	 */
	@Override
	public ResultMessage batchUpdateQLR(RightsHolder holder, String option) {
		ResultMessage msg=new ResultMessage();
		String xmbh=holder.getXMBH();
		RightsHolder oldHolder=RightsHolderTools.loadRightsHolder(DJDYLY.GZ, holder.getId());
		List<BDCS_QLR_GZ> holderlist=null;
		String sql="XMBH='"+xmbh+"'";
		if(option.equals("1")){
			sql+=" AND QLRMC='"+oldHolder.getQLRMC()+"'";
		}
		else if(option.equals("2")){
			sql+=" AND ZJH='"+oldHolder.getZJH()+"'";
		}
		else if(option.equals("3")){
			sql+=" AND QLRMC='"+oldHolder.getQLRMC()+"' AND ZJH='"+oldHolder.getZJH()+"'";
		}
		else{
			msg.setSuccess("false");
			msg.setMsg("更新选项不正确，值只能是1、2或者3");
			return msg;
		}
		holderlist=baseCommonDao.getDataList(BDCS_QLR_GZ.class, sql);
		if(holderlist!=null && holderlist.size()>0){
			for(BDCS_QLR_GZ qlr:holderlist){
				qlr.setQLRMC(holder.getQLRMC());//权利人名称
				qlr.setSXH(holder.getSXH());	//顺序号
				qlr.setZJZL(holder.getZJZL());	//证件种类
				qlr.setXB(holder.getXB());		//性别
				qlr.setZJH(holder.getZJH());	//证件号
				qlr.setFZJG(holder.getFZJG());	//发证机关
				qlr.setDH(holder.getDH());		//电话
				qlr.setDZ(holder.getDZ());		//地址
				qlr.setYB(holder.getYB());		//邮编
				qlr.setDZYJ(holder.getDZYJ());	//电子邮件
				qlr.setGJ(holder.getGJ());		//国家
				qlr.setHJSZSS(holder.getHJSZSS()); //户籍所在省市
				qlr.setGZDW(holder.getGZDW());	//工作单位
				qlr.setSSHY(holder.getSSHY());	//所属行业
				qlr.setQLMJ(holder.getQLMJ());	//权利面积
				qlr.setQLRLX(holder.getQLRLX());//权利人类型
				qlr.setGYFS(holder.getGYFS());	//共有方式
				qlr.setQLBL(holder.getQLBL());	//权利比例
				qlr.setBZ(holder.getBZ());		//备注
				qlr.setISCZR(holder.getISCZR());//是否持证人
				baseCommonDao.update(qlr);
			}
		}
		msg.setSuccess("true");
		msg.setMsg("批量更新成功，共更新了"+(holderlist==null || holderlist.size()<=0?"0":holderlist.size()+"条权利人记录"));
		return msg;
	}

	@Override
	public Map<String, Object> GetQLEx(String qlid) throws Exception {
		BDCS_QL_GZ bdcs_QL_GZ =GetQL(qlid);
		if (bdcs_QL_GZ != null) {
			String fj = bdcs_QL_GZ.getFJ();
			if (!StringHelper.isEmpty(fj)) {
				fj = fj.replaceAll(" ", "\u00A0");
			}
			bdcs_QL_GZ.setFJ(fj);
			String djyy = bdcs_QL_GZ.getDJYY();
			if (!StringHelper.isEmpty(djyy)) {
				djyy = djyy.replaceAll(" ", "\u00A0");
			}
			bdcs_QL_GZ.setDJYY(djyy);
		}
		Map<String, Object> result=objectToMap(bdcs_QL_GZ);
		if("1".equals(bdcs_QL_GZ.getISPARTIAL())){
			List<String> zyqlrlist=new ArrayList<String>();
			List<BDCS_PARTIALLIMIT> list_zylimit=baseCommonDao.getDataList(BDCS_PARTIALLIMIT.class, "LIMITQLID='"+bdcs_QL_GZ.getId()+"'");
			if(list_zylimit!=null&&list_zylimit.size()>0){
				for(BDCS_PARTIALLIMIT partialseizures:list_zylimit){
					zyqlrlist.add(partialseizures.getQLRID());
				}
			}

			List<BDCS_QLR_LS> mainqlrlist=baseCommonDao.getDataList(BDCS_QLR_LS.class, "QLID='"+bdcs_QL_GZ.getLYQLID()+"'");
			List<HashMap<String,String>> mainqlrlist1=new ArrayList<HashMap<String,String>>();

			if(mainqlrlist!=null&&mainqlrlist.size()>0){
				for(BDCS_QLR_LS qlr:mainqlrlist){
					HashMap<String,String> m=new HashMap<String, String>();
					m.put("id", qlr.getId());
					m.put("qlrmc", qlr.getQLRMC());
					List<BDCS_PARTIALLIMIT> list_limit=baseCommonDao.getDataList(BDCS_PARTIALLIMIT.class, "QLRID='"+qlr.getId()+"' AND LIMITTYPE IN ('23','800') AND YXBZ IN ('0','1')");
					if(list_limit!=null&&list_limit.size()>0){
						m.put("islimit", "1");
					}else{
						m.put("islimit", "0");
					}
					mainqlrlist1.add(m);
				}
			}
			result.put("zyqlrlist", zyqlrlist);// 被转移权利人id列表
			result.put("mainqlrlist", mainqlrlist1);// 被转移单元产权人列表
		}

		return result;
	}

	public static Map<String, Object> objectToMap(Object obj) throws Exception {
        if(obj == null)
            return null;

        Map<String, Object> map = new HashMap<String, Object>();

        BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor property : propertyDescriptors) {
            String key = property.getName();
            if (key.compareToIgnoreCase("class") == 0) {
                continue;
            }
            Method getter = property.getReadMethod();
            Object value = getter!=null ? getter.invoke(obj) : null;
            map.put(key, value);
        }

        return map;
    }

	@Override
	public void UpdateZYQLRList(String[] zyqlrids,BDCS_QL_GZ ql) {
		if(zyqlrids!=null&&zyqlrids.length>0){
			List<String> qlrlist=new ArrayList<String>();
			for(String qlrid:zyqlrids){
				if(!StringHelper.isEmpty(qlrid)){
					qlrlist.add(qlrid);
				}
			}
			List<String> list_zyqlr=new ArrayList<String>();
			List<BDCS_PARTIALLIMIT> list=baseCommonDao.getDataList(BDCS_PARTIALLIMIT.class, "LIMITQLID='"+ql.getId()+"'");
			if(list!=null&&list.size()>0){
				for(BDCS_PARTIALLIMIT partialseizures:list){
					if(qlrlist.contains(partialseizures.getQLRID())){
						qlrlist.remove(partialseizures.getQLRID());
						RightsHolder qlr=RightsHolderTools.loadRightsHolder(DJDYLY.XZ, partialseizures.getQLRID());
						if(qlr!=null){
							list_zyqlr.add(qlr.getQLRMC());
						}
					}else{
						baseCommonDao.deleteEntity(partialseizures);
					}
				}
			}

			if(qlrlist.size()>0){
				for(String qlrid:qlrlist){
					RightsHolder qlr=RightsHolderTools.loadRightsHolder(DJDYLY.XZ, qlrid);

					if(qlr!=null){
						BDCS_PARTIALLIMIT partialseizures=new BDCS_PARTIALLIMIT();
						List<BDCS_DJDY_GZ> djdys=baseCommonDao.getDataList(BDCS_DJDY_GZ.class, "DJDYID='"+ql.getDJDYID()+"' AND XMBH='"+ql.getXMBH()+"'");
						if(djdys!=null&&djdys.size()>0){
							partialseizures.setBDCDYID(djdys.get(0).getBDCDYID());
						}
						partialseizures.setLIMITQLID(ql.getId());
						partialseizures.setQLID(qlr.getQLID());
						partialseizures.setQLRID(qlr.getId());
						partialseizures.setQLRMC(qlr.getQLRMC());
						partialseizures.setXMBH(ql.getXMBH());
						partialseizures.setYXBZ("0");
						partialseizures.setLIMITTYPE("200");
						partialseizures.setZJH(qlr.getZJH());
						baseCommonDao.save(partialseizures);
					}
				}
			}
		}
	}

	/**
	 * @Description: 根据权利id获取ls层抵押权
	 * @Title: getls_DYQInfo
	 * @Author: zhaomengfan
	 * @Date: 2017年3月24日下午3:23:22
	 * @param qlid
	 * @return
	 *
	 */
	@Override
	public Map<String, Object> getls_DYQInfo(String qlid) {
		String qlrmc = "";// 权利人名称
		String sqrid = "";// 权利人
		String dybdclx = "";// 抵押不动产类型
		String zjjzwzl = "";// 在建建筑物坐落
		String dyfs = "";// 抵押方式
		String bdbzzqse = "";// 被担保主债权数额
		String zjjzwdyfw = ""; // 在建建筑物抵押范围
		String zgzqse = "";// 最高债权数额
		String zgzqqdss = "";// 最高债权确定事实
		String fj = "";// 附记
		String djyy = "";// 登记原因
		String dyr = "";// 抵押人
		String bdcqzh = ""; // 产权证号
		Double dypgjz = 0.0;// 抵押评估价值
		String ydzmj=""; // 用地总面积
		String dyydmj="";// 抵押用地总面积
		String zjzmj="";// 总建筑面积
		String dyjzmj=""; // 抵押建筑面积
		String dytdyt="";// 土地用途
		String zqdw="";//债权 单位
		String dgbdbzzqse = "";// 单个单元被担保主债权数额
		Map<String, Object> map = new HashMap<String, Object>();
		DecimalFormat format = new DecimalFormat("#.00");
		format.setRoundingMode(RoundingMode.HALF_UP);
		if (!StringHelper.isEmpty(qlid)) {
			BDCS_QL_LS bdcs_ql_ls = baseCommonDao.get(BDCS_QL_LS.class, qlid);
			if (bdcs_ql_ls != null) {
				BDCS_FSQL_LS bdcs_fsql_ls = baseCommonDao.get(BDCS_FSQL_LS.class, bdcs_ql_ls.getFSQLID());
				if (bdcs_fsql_ls != null) {
					if (!StringHelper.isEmpty(bdcs_fsql_ls.getDYWLXName())) {
						dybdclx = bdcs_fsql_ls.getDYWLXName();
					}
					if (!StringHelper.isEmpty(bdcs_fsql_ls.getZJJZWZL())) {
						zjjzwzl = bdcs_fsql_ls.getZJJZWZL();
					}
					if (!StringHelper.isEmpty(bdcs_fsql_ls.getDYFS())) {
						dyfs = bdcs_fsql_ls.getDYFS();
					}
					if (!StringHelper.isEmpty(bdcs_fsql_ls.getBDBZZQSE())) {
						bdbzzqse = format.format(bdcs_fsql_ls.getBDBZZQSE());
					}
					if (!StringHelper.isEmpty(bdcs_fsql_ls.getDGBDBZZQSE())) {
						dgbdbzzqse = format.format(bdcs_fsql_ls.getDGBDBZZQSE());
					}
					if (!StringHelper.isEmpty(bdcs_fsql_ls.getZGZQSE())) {
						zgzqse = format.format(bdcs_fsql_ls.getZGZQSE());
					}
					if (!StringHelper.isEmpty(bdcs_fsql_ls.getZJJZWDYFW())) {
						zjjzwdyfw = bdcs_fsql_ls.getZJJZWDYFW();
					}
					if (!StringHelper.isEmpty(bdcs_fsql_ls.getDYR())) {
						dyr = bdcs_fsql_ls.getDYR();
					}
					if (!StringHelper.isEmpty(bdcs_fsql_ls.getDYPGJZ())) {
						dypgjz = bdcs_fsql_ls.getDYPGJZ();
					}
					if (!StringHelper.isEmpty(bdcs_fsql_ls.getZGZQQDSS())) {
						zgzqqdss = bdcs_fsql_ls.getZGZQQDSS();
					}
					if (!StringHelper.isEmpty(bdcs_fsql_ls.getYDZMJ())) {
						ydzmj = format.format(bdcs_fsql_ls.getYDZMJ());
					}
					if (!StringHelper.isEmpty(bdcs_fsql_ls.getDYYDMJ())) {
						dyydmj = format.format(bdcs_fsql_ls.getDYYDMJ());
					}
					if (!StringHelper.isEmpty(bdcs_fsql_ls.getZJZMJ())) {
						zjzmj = format.format(bdcs_fsql_ls.getZJZMJ());
					}
					if (!StringHelper.isEmpty(bdcs_fsql_ls.getDYJZMJ())) {
						dyjzmj = format.format(bdcs_fsql_ls.getDYJZMJ());
					}
					if (!StringHelper.isEmpty(bdcs_fsql_ls.getDYTDYT())) {
						dytdyt = bdcs_fsql_ls.getDYTDYT();
					}
					zqdw=bdcs_fsql_ls.getZQDW();
				}
				List<BDCS_QLR_LS> qlrs = baseCommonDao.getDataList(BDCS_QLR_LS.class, "qlid ='" + qlid + "'");
				boolean flag = true;
				for (BDCS_QLR_LS bdcs_qlr_ls : qlrs) {
					if (flag) {
						qlrmc += bdcs_qlr_ls.getQLRMC();
						sqrid += bdcs_qlr_ls.getSQRID();
						flag = false;
					} else {
						qlrmc += "," + bdcs_qlr_ls.getQLRMC();
						sqrid += "," + bdcs_qlr_ls.getSQRID();
					}
				}
				String qssj = bdcs_ql_ls.getQLQSSJ() == null ? "" : bdcs_ql_ls.getQLQSSJ().toString().substring(0, 11);// 债务履行起始时间
				String jssj = bdcs_ql_ls.getQLJSSJ() == null ? "" : bdcs_ql_ls.getQLJSSJ().toString().substring(0, 11); // 债务旅行结束时间
				fj = bdcs_ql_ls.getFJ();
				djyy = bdcs_ql_ls.getDJYY();
				bdcqzh = bdcs_ql_ls.getBDCQZH();
				map.put("casenum", bdcs_ql_ls.getCASENUM()); 
				map.put("qlrmc", qlrmc); // 权利人名称
				map.put("sqrid", sqrid); // 权利人
				map.put("dybdclx", dybdclx); // 抵押不动产类型
				map.put("zjjzwzl", zjjzwzl); // 在建建筑物坐落
				map.put("dyfs", dyfs); // 抵押方式
				String dyfsmc = "1".equals(dyfs) ? "一般抵押 ":"最高额抵押 ";
				map.put("dyfsmc", dyfsmc); // 抵押方式
				map.put("bdbzzqse", bdbzzqse); // 被担保主债权数额
				map.put("zjjzwdyfw", zjjzwdyfw); // 在建建筑物抵押范围
				map.put("qljssj", jssj); // 债务旅行结束时间
				map.put("qlqssj", qssj); // 债务履行起始时间
				map.put("zgzqse", zgzqse); // 最高债权数额
				map.put("zgzqqdss", zgzqqdss); // 最高债权确定事实
				map.put("fj", fj); // 附记
				map.put("djyy", djyy); // 登记原因
				map.put("dyr", dyr); // 抵押人
				map.put("dypgjz", dypgjz); // 抵押评估价值
				map.put("bdcqzh", bdcqzh); // 产权证号
				map.put("qlid", qlid);
				map.put("ydzmj", ydzmj); // 用地总面积
				map.put("dyydmj", dyydmj); // 抵押用地总面积
				map.put("zjzmj", zjzmj); // 总建筑面积
				map.put("dyjzmj", dyjzmj); // 抵押建筑面积
				map.put("dytdyt", dytdyt); // 土地用途
				map.put("zqdw", zqdw);//债权单位
				map.put("dgbdbzzqse", dgbdbzzqse); // 单个单元被担保主债权数额
			}
		}
		return map;
	}

	@Override
	public void plSaveQlInfo_lq(String xmbh, JSONObject object) {
		Map map = transToMAP(object);
		List<BDCS_QL_GZ> qls=baseCommonDao.getDataList(BDCS_QL_GZ.class, "XMBH='"+xmbh+"'");
		if(qls!=null&&qls.size()>0){
			for(BDCS_QL_GZ ql:qls){
				BDCS_FSQL_GZ fsql = baseCommonDao.get(BDCS_FSQL_GZ.class, ql.getFSQLID());
				setValue(map, ql);
				setValue(map, fsql);
				baseCommonDao.update(ql);
				baseCommonDao.update(fsql);
			}
		}
		baseCommonDao.flush();
		
	}
	/**
	 * 双击单元时显示该单元信息
	 */
	@Override
	public Map<String, Object> getBGdyqInfo_dy(String bdcdyid,String xmbh,String type) {
		Map<String, Object> map=new HashMap<String, Object>();
		List<Map> qlid =baseCommonDao.getDataListByFullSql("select QLID from bdck.bdcs_ql_gz  where  bdcdyid='"+bdcdyid+"' and xmbh='"+xmbh+"'");//加上ISCANCEL<>'1' 取消的权利不记入内
		String _qlid =null; 
		for (Map<String, String> m : qlid){
			for (String k : m.keySet()){  
				_qlid = m.get(k);  
		      }  
		 }
		List<Rights> qls =RightsTools.loadRightsByCondition(DJDYLY.GZ, " XMBH='"+xmbh+"' AND bdcdyid='"+bdcdyid+"'");
		if(qls!=null&&qls.size()>0){
			Rights ql = qls.get(0);
			if(_qlid!=null){
				DJDYLY ly=DJDYLY.GZ;
				SubRights fsql = RightsTools.loadSubRightsByRightsID(ly, _qlid);
					List<RightsHolder> list=RightsHolderTools.loadRightsHolders(ly, _qlid);
					String qlrmc = "";
					String sqrid = "";
					for (int i = 0; i < list.size(); i++) {
						RightsHolder qlr = list.get(i);
						if (list.size() - 1 == i) {
							qlrmc += qlr.getQLRMC();
							sqrid += qlr.getSQRID();
						} else {
							qlrmc += qlr.getQLRMC() + ",";
							sqrid += qlr.getSQRID() + ",";
						}
					}
					String qssj = ql.getQLQSSJ() == null ? "" : ql.getQLQSSJ().toString();
					String jssj = ql.getQLJSSJ() == null ? "" : ql.getQLJSSJ().toString();
					map.put("qlrmc", qlrmc); // 权利人名称
					map.put("sqrid", sqrid); // 权利人
					String dywlxname="";
					if(!StringHelper.isEmpty(fsql.getDYWLX())){
						dywlxname=ConstHelper.getNameByValue("DYBDCLX", fsql.getDYWLX());
					}
					map.put("dybdclx", dywlxname); // 抵押不动产类型
					map.put("zjjzwzl", fsql.getZJJZWZL()); // 在建建筑物坐落
					map.put("dyfs", fsql.getDYFS()); // 抵押方式
					map.put("bdbzzqse", fsql.getBDBZZQSE()); // 被担保主债权数额
					map.put("zjjzwdyfw", fsql.getZJJZWDYFW()); // 在建建筑物抵押范围
					map.put("qljssj", jssj); // 债务旅行结束时间
					map.put("qlqssj", qssj); // 债务履行起始时间
					map.put("zgzqse", fsql.getZGZQSE()); // 最高债权数额
					map.put("zgzqqdss", fsql.getZGZQQDSS()); // 最高债权确定事实
					map.put("fj", ql.getFJ()); // 附记
					map.put("djyy", ql.getDJYY()); // 登记原因
					map.put("dyr", fsql.getDYR()); // 登记原因
					map.put("bdcqzh", ql.getBDCQZH()); // 产权证号
					map.put("dypgjz", fsql.getDYPGJZ()); // 登记原因
					map.put("ydzmj", fsql.getYDZMJ()); // 用地总面积
					map.put("dyydmj", fsql.getDYYDMJ()); // 抵押用地总面积
					map.put("zjzmj", fsql.getZJZMJ()); // 总建筑面积
					map.put("dyjzmj", fsql.getDYJZMJ()); // 抵押建筑面积
					map.put("dytdyt", fsql.getDYTDYT()); // 土地用途
				}
		}
		return map;
	}
	  public Message getAllMortgage(String qlid)
	  {
	    Message msg = new Message();
	    List list = null;
	    StringBuilder hql = new StringBuilder();
	    if (qlid != null)
	    {
	      BDCS_QL_GZ ql_gz = (BDCS_QL_GZ)this.baseCommonDao.get(BDCS_QL_GZ.class, qlid);
	      if (ql_gz != null)
	      {
	        hql.append("XMBH ='");
	        hql.append(ql_gz.getXMBH() + "'");
	        hql.append("AND QLLX='" + ConstValue.QLLX.DIYQ.Value + "'");
	        
	        String hqlcondition = hql.toString();
	        list = this.baseCommonDao.getDataList(BDCS_QL_GZ.class, hqlcondition);
	        this.baseCommonDao.flush();
	      }
	      if ((list != null) && (list.size() > 0))
	      {
	        msg.setRows(list);
	        msg.setSuccess("true");
	        msg.setMsg("获取成功");
	        msg.setTotal(list.size());
	        return msg;
	      }
	    }
	    return null;
	  }

	/**
	 * 获取完税信息详情
	 * @param ly
	 * @param qlid
	 * @return
	 * @throws ClassNotFoundException
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public List<BDCS_WSXX> getWsxxInfo(String xmbh, String qlid) {
		List<BDCS_WSXX> wsxx = null;
		String wheresql = MessageFormat.format("QLID=''{0}'' AND XMBH=''{1}''" , qlid, xmbh);
		List<BDCS_FSQL_GZ> fsqls = baseCommonDao.getDataList(BDCS_FSQL_GZ.class, wheresql);
		if (fsqls != null && fsqls.size() > 0) {
			BDCS_FSQL_GZ fsql = fsqls.get(0);
			wsxx = baseCommonDao.getDataList(BDCS_WSXX.class, " FSQLID='" + fsql.getId() +"'");
		}

		return wsxx;
	}
}
