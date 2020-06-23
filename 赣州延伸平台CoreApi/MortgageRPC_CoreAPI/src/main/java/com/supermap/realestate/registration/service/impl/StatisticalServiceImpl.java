package com.supermap.realestate.registration.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.supermap.realestate.registration.service.StatisticalService;
import com.supermap.realestate.registration.util.ConfigHelper;
import com.supermap.realestate.registration.util.Global;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import com.supermap.wisdombusiness.framework.model.User;
import com.supermap.wisdombusiness.web.StatisticalMessage;
import com.supermap.wisdombusiness.web.ui.tree.Tree;
import com.supermap.wisdombusiness.web.ui.tree.TreeUtil;
@Service("statisticalService")
public class StatisticalServiceImpl implements StatisticalService {
	@Autowired
	CommonDao  dao;
	@SuppressWarnings("rawtypes")
	@Override
	public StatisticalMessage queryFdcjyrbtjData(Map params) {
		//返回结果
		StatisticalMessage statisticalMessage = new StatisticalMessage();
		Map<String, Object> reslutlMap = new HashMap<String, Object>();
		List<Map<String, Object>> resultList =  new ArrayList<Map<String, Object>>();
		String xzqdm = ConfigHelper.getNameByValue("XZQHDM");
		//住宅枚举
		String SPFZZ = ConfigHelper.getNameByValue("SPFZZ");
		//商业枚举
		String SPFSY = ConfigHelper.getNameByValue("SPFSY");
		//办公枚举
		String SPFBG = ConfigHelper.getNameByValue("SPFBG");
		//综合枚举
		String SPFZH = ConfigHelper.getNameByValue("SPFZH");
		//车库枚举
		String SPFCK = ConfigHelper.getNameByValue("SPFCK");


		if (StringUtils.isBlank(SPFZZ)) {
			statisticalMessage.setSuccess("false");
			statisticalMessage.setMsg("请联系管理员配置本地化设置住宅枚举变量，SPFZZ,如配置名称住宅枚举，枚举值：10,11,13,111,112（注意要英文逗号隔开）");
			return statisticalMessage;
		}
		List<String> SPFZZList =Arrays.asList(SPFZZ.split(","));
		//成交套数	成交面积（m2）	成交金额（万元）	均价（元）	套均面积（m2）
		//新建商品房 
		int spf_ts = 0;//成交套数
		double spf_mj = 0.0;//成交面积（m2）
		double spf_jg = 0.0;//成交金额（万元）
		double spf_jj = 0.0;//均价（元）
		double spf_tj = 0.0;//套均面积（m2）
		 
		//新建商品房（住宅）
		int spfzz_ts = 0;
		double spfzz_mj = 0.0;
		double spfzz_jg = 0.0;
		double spfzz_jj = 0.0;
		double spfzz_tj = 0.0;
		 
		//新建商品房（非住宅）
		int spffzz_ts = 0;
		double spffzz_mj = 0.0;
		double spffzz_jg = 0.0;
		double spffzz_jj = 0.0;
		double spffzz_tj = 0.0; 
		 
		//二手房
		int esf_ts = 0;
		double esf_mj = 0.0;
		double esf_jg = 0.0;
		double esf_jj = 0.0;
		double esf_tj = 0.0;
		 
		//二手房（住宅）
		int esfzz_ts = 0;
		double esfzz_mj = 0.0;
		double esfzz_jg = 0.0;
		double esfzz_jj = 0.0;
		double esfzz_tj = 0.0;
		 
		//二手房（非住宅）
		int esffzz_ts = 0;
		double esffzz_mj = 0.0;
		double esffzz_jg = 0.0;
		double esffzz_jj = 0.0;
		double esffzz_tj = 0.0; 
		  
		
		//统计起始时间
		String qssj = StringHelper.formatObject(params.get("qssj"));
		//统计截止时间
		String zzsj = StringHelper.formatObject(params.get("zzsj"));
		//统计城区
		String cq = StringHelper.formatObject(params.get("cq"));
		//统计类别， 
		String radio = StringHelper.formatObject(params.get("radio"));
		User user = Global.getCurrentUserInfo();
		if (user!=null) {
			statisticalMessage.setCzr(user.getUserName());
			statisticalMessage.setDepartmentName(user.getDepartmentName());;
		}
		statisticalMessage.setMsg("查询成功");
		statisticalMessage.setSuccess("true");
		try {
			////////////////期现房预告查询逻辑////////////////////
			StringBuffer querySqlBuffer = new StringBuffer();
			/*已改用视图
			 * querySqlBuffer.append(" SELECT ");
			 * querySqlBuffer.append(" 	djdjdyid, bdcdyid, bdcdyh, zl, fh, "
			 * );
			 * querySqlBuffer.append(" 	ghyt, jzmj, tnjzmj, ftjzmj, fttdmj, "
			 * ); querySqlBuffer.
			 * append(" 	bdcdylx, bdcdylxmc, zrzh, searchstate, bz,fwyt1, "
			 * ); querySqlBuffer.
			 * append(" 	xm.ywlsh, qlid, bdcqzh, qllx, djsj, qdjg  ");
			 * querySqlBuffer.append(" FROM "); querySqlBuffer.
			 * append(" ( SELECT zl, bdcdyh, bdcdyid, fh, ghyt, fwbm, scjzmj AS jzmj, sctnjzmj AS tnjzmj, "
			 * + " scftjzmj AS ftjzmj, fttdmj, '031' AS bdcdylx, '现房' AS bdcdylxmc, zrzh, "
			 * + " searchstate, bz ,QXDM,QXMC,FWYT1 " +
			 * " FROM bdck.bdcs_h_xz  where bdcdyh is not null" +
			 * " UNION ALL SELECT zl, bdcdyh, bdcdyid, fh, ghyt, fwbm, ycjzmj AS jzmj, yctnjzmj AS tnjzmj,"
			 * + " ycftjzmj AS ftjzmj," +
			 * " fttdmj, '032' AS bdcdylx, '期房' AS bdcdylxmc, zrzh, " +
			 * " searchstate, bz ,QXDM,QXMC,FWYT1 " +
			 * " FROM bdck.bdcs_h_xzy where bdcdyh is not null ) dy " +
			 * " LEFT JOIN bdck.bdcs_djdy_xz djdy ON bdcdyid = djbdcdyid LEFT JOIN bdck.bdcs_ql_xz ql"
			 * +
			 * " ON djdyid = djdjdyid LEFT JOIN bdck.bdcs_xmxx xm ON xm.project_id = ywh  "
			 * ); querySqlBuffer.
			 * append(" where djlx='700' and (qllx='4' or qllx='99') and qllx is not null "
			 * );
			 */
			querySqlBuffer.append(" select * from BDCK.V_XJSPFDJYG where 1=1 ");
			if (StringUtils.isNotBlank(qssj)) {
				querySqlBuffer.append(" and djsj>=to_date('").append(qssj).append("','yyyy-mm-dd hh24:mi:ss') ");
			}
			//应为数据精确查询到时分秒，所以这里用<=没问题，如果只计算天的，那么得用<to_date(...)+1
			if (StringUtils.isNotBlank(zzsj)) {
				querySqlBuffer.append(" and djsj<=to_date('").append(zzsj).append("','yyyy-mm-dd hh24:mi:ss') ");
			}
			if (StringUtils.isNotBlank(cq)) {
				String qxdmsString  = StringUtils.join(cq.split(","),"','");
				querySqlBuffer.append(" and   nvl(QXDM,substr(bdcdyh,substr(bdcdyh,1,6))) in('").append(qxdmsString).append("') ");
			} 
			
			List<Map> xjspfList = dao.getDataListByFullSql(querySqlBuffer.toString());
			
			if (xjspfList.size()>0) {
				for (Map hmap : xjspfList) {
					
					spf_ts++; 
					double jzmj = StringHelper.getDouble(hmap.get("JZMJ"));
					spf_mj = StringHelper.sum(jzmj,spf_mj,2); 
					double qdjg = StringHelper.getDouble(hmap.get("QDJG"));
					spf_jg = StringHelper.sum(qdjg,spf_jg,2); 
					if (SPFZZList.contains(StringHelper.formatObject(hmap.get("FWYT1")))) {//住宅
						spfzz_ts++; 
 						spfzz_mj = StringHelper.sum(jzmj,spfzz_mj,2); 
 						spfzz_jg = StringHelper.sum(qdjg,spfzz_jg,2);
					}else {//非住宅
						spffzz_ts++; 
 						spffzz_mj = StringHelper.sum(jzmj,spffzz_mj,2); 
 						spffzz_jg = StringHelper.sum(qdjg,spffzz_jg,2);
					}
				}
			}
			
			////////////////二手房转移登记查询逻辑////////////////////
			StringBuffer queryCLFSqlBuffer = new StringBuffer();
			/*
			 * 改用视图
			 * queryCLFSqlBuffer.append(" SELECT "); queryCLFSqlBuffer.
			 * append(" 	djdjdyid, bdcdyid, bdcdyh, zl, fh, ");
			 * queryCLFSqlBuffer.
			 * append(" 	ghyt, jzmj, tnjzmj, ftjzmj, fttdmj, ");
			 * queryCLFSqlBuffer.
			 * append(" 	bdcdylx, bdcdylxmc, zrzh, searchstate, bz,fwyt1, "
			 * ); queryCLFSqlBuffer.
			 * append(" 	xm.ywlsh, qlid, bdcqzh, qllx, djsj, qdjg  ");
			 * queryCLFSqlBuffer.append(" FROM "); queryCLFSqlBuffer.
			 * append(" ( SELECT zl, bdcdyh, bdcdyid, fh, ghyt, fwbm, scjzmj AS jzmj, sctnjzmj AS tnjzmj, "
			 * + " scftjzmj AS ftjzmj, fttdmj, '031' AS bdcdylx, '现房' AS bdcdylxmc, zrzh, "
			 * + " searchstate, bz ,QXDM,QXMC,FWYT1 " +
			 * " FROM bdck.bdcs_h_xz  where bdcdyh is not null) dy " +
			 * " LEFT JOIN bdck.bdcs_djdy_xz djdy ON bdcdyid = djbdcdyid LEFT JOIN bdck.bdcs_ql_xz ql"
			 * +
			 * " ON djdyid = djdjdyid LEFT JOIN bdck.bdcs_xmxx xm ON xm.project_id = ywh  "
			 * ); queryCLFSqlBuffer.
			 * append(" where djlx='200' and qllx='4' and  exists(select 1 from  bdck.bdcs_ql_ls lsql where lsdjlx='200' and lsqllx='4' and lsqlid= LYQLID) "
			 * );
			 */
			queryCLFSqlBuffer.append("select * from BDCK.V_DJESFZYDJ where 1=1 ");
			if (StringUtils.isNotBlank(qssj)) {
				queryCLFSqlBuffer.append(" and djsj>=to_date('").append(qssj).append("','yyyy-mm-dd hh24:mi:ss') ");
			}
			//应为数据精确查询到时分秒，所以这里用<=没问题，如果只计算天的，那么得用<to_date(...)+1
			if (StringUtils.isNotBlank(zzsj)) {
				queryCLFSqlBuffer.append(" and djsj<=to_date('").append(zzsj).append("','yyyy-mm-dd hh24:mi:ss') ");
			}
			if (StringUtils.isNotBlank(cq)) {
				String qxdmsString  = StringUtils.join(cq.split(","),"','");
				queryCLFSqlBuffer.append(" and  nvl(QXDM,substr(bdcdyh,substr(bdcdyh,1,6))) in('").append(qxdmsString).append("') ");
			} 
			
			List<Map> clfList = dao.getDataListByFullSql(queryCLFSqlBuffer.toString());
			
			if (clfList.size()>0) {
				for (Map hmap : clfList) {
					esf_ts++; 
					double jzmj = StringHelper.getDouble(hmap.get("JZMJ"));
					esf_mj = StringHelper.sum(esf_mj, jzmj,2);
					
					double qdjg = StringHelper.getDouble(hmap.get("QDJG"));
					esf_jg = StringHelper.sum(qdjg,esf_jg,2);
					
					if (SPFZZList.contains(StringHelper.formatObject(hmap.get("FWYT1")))) {//住宅
						esfzz_ts++; 
 						esfzz_mj = StringHelper.sum(jzmj,esfzz_mj,2); 
 						esfzz_jg = StringHelper.sum(qdjg,esfzz_jg,2);
					}else {//非住宅
						esffzz_ts++; 
 						esffzz_mj = StringHelper.sum(jzmj,esffzz_mj,2);
 						esffzz_jg = StringHelper.sum(qdjg,esffzz_jg,2);
					}
				}
			}
			
			 
			//新建商品房 
			reslutlMap.put("spf_ts", spf_ts);
			reslutlMap.put("spf_mj", spf_mj);
			reslutlMap.put("spf_jg", spf_jg);
			if (spf_mj>0 && spf_jg>0) {
				spf_jj = StringHelper.divide(spf_jg, spf_mj, 2);
				reslutlMap.put("spf_jj", spf_jj);
			}else {
				reslutlMap.put("spf_jj", "-");//不符合添加不统计
			}
			
			if (spf_mj>0 && spf_ts>0) {
				spf_tj = StringHelper.divide(spf_mj, spf_ts, 2);
				reslutlMap.put("spf_tj", spf_tj);
			}else {
				reslutlMap.put("spf_tj", "-");//不符合添加不统计
			}
			

			
			//新建商品房（住宅） 
			reslutlMap.put("spfzz_ts", spfzz_ts);
			reslutlMap.put("spfzz_mj", spfzz_mj);
			reslutlMap.put("spfzz_jg", spfzz_jg);
			if (spfzz_mj>0 && spfzz_jg>0) {
				spfzz_jj = StringHelper.divide(spfzz_jg, spfzz_mj, 2);
				reslutlMap.put("spfzz_jj", spfzz_jj);
			}else {
				reslutlMap.put("spfzz_jj", "-");//不符合添加不统计
			}
			
			if (spfzz_mj>0 && spfzz_ts>0) {
				spfzz_tj = StringHelper.divide(spfzz_mj, spfzz_ts, 2);
				reslutlMap.put("spfzz_tj", spfzz_tj);
			}else {
				reslutlMap.put("spfzz_tj", "-");//不符合添加不统计
			}

			//新建商品房（非住宅） 
			reslutlMap.put("spffzz_ts", spffzz_ts);
			reslutlMap.put("spffzz_mj", spffzz_mj);
			reslutlMap.put("spffzz_jg", spffzz_jg);
			if (spffzz_mj>0 && spffzz_jg>0) {
				spffzz_jj = StringHelper.divide(spffzz_jg, spffzz_mj, 2);
				reslutlMap.put("spffzz_jj", spffzz_jj);
			}else {
				reslutlMap.put("spffzz_jj", "-");//不符合添加不统计
			}
			
			if (spffzz_mj>0 && spffzz_ts>0) {
				spffzz_tj = StringHelper.divide(spffzz_mj, spffzz_ts, 2);
				reslutlMap.put("spffzz_tj", spffzz_tj);
			}else {
				reslutlMap.put("spffzz_tj", "-");//不符合添加不统计
			}
 
			//二手房 
			reslutlMap.put("esf_ts", esf_ts);
			reslutlMap.put("esf_mj", esf_mj);
			reslutlMap.put("esf_jg", esf_jg);
			if (esf_mj>0 && esf_jg>0) {
				esf_jj = StringHelper.divide(esf_jg, esf_mj, 2);
				reslutlMap.put("esf_jj", esf_jj);
			}else {
				reslutlMap.put("esf_jj", "-");//不符合添加不统计
			}
			
			if (esf_mj>0 && esf_ts>0) {
				esf_tj = StringHelper.divide(esf_mj, esf_ts, 2);
				reslutlMap.put("esf_tj", esf_tj);
			}else {
				reslutlMap.put("esf_tj", "-");//不符合添加不统计
			}

			//二手房（住宅） 
			reslutlMap.put("esfzz_ts", esfzz_ts);
			reslutlMap.put("esfzz_mj", esfzz_mj);
			reslutlMap.put("esfzz_jg", esfzz_jg);
			if (esfzz_mj>0 && esfzz_jg>0) {
				esfzz_jj = StringHelper.divide(esfzz_jg, esfzz_mj, 2);
				reslutlMap.put("esfzz_jj", esfzz_jj);
			}else {
				reslutlMap.put("esfzz_jj", "-");//不符合添加不统计
			}
			
			if (esfzz_mj>0 && esfzz_ts>0) {
				esfzz_tj = StringHelper.divide(esfzz_mj, esfzz_ts, 2);
				reslutlMap.put("esfzz_tj", esfzz_tj);
			}else {
				reslutlMap.put("esfzz_tj", "-");//不符合添加不统计
			}

			//二手房（非住宅） 
			reslutlMap.put("esffzz_ts", esffzz_ts);
			reslutlMap.put("esffzz_mj", esffzz_mj);
			reslutlMap.put("esffzz_jg", esffzz_jg);
			if (esffzz_mj>0 && esffzz_jg>0) {
				esffzz_jj = StringHelper.divide(esffzz_jg, esffzz_mj, 2);
				reslutlMap.put("esffzz_jj", esffzz_jj);
			}else {
				reslutlMap.put("esffzz_jj", "-");//不符合添加不统计
			}
			
			if (esffzz_mj>0 && esffzz_ts>0) {
				esffzz_tj = StringHelper.divide(esffzz_mj, esffzz_ts, 2);
				reslutlMap.put("esffzz_tj", esffzz_tj);
			}else {
				reslutlMap.put("esffzz_tj", "-");//不符合添加不统计
			}  
			resultList.add(reslutlMap);
			statisticalMessage.setRows(resultList);
		} catch (Exception e) { 
			statisticalMessage.setSuccess("false");
			statisticalMessage.setMsg("查询异常！"+e.getMessage());
			e.printStackTrace();
		} 
		return statisticalMessage;
	} 
	@SuppressWarnings({ "rawtypes" })
	@Override
	public List<Tree> getDivisionCodeTreeList() { 
		List<Tree> treelist = new ArrayList<Tree>();
		//获取城区数据
		List<Map> jdList = dao.getDataListByFullSql("SELECT FIELDENUM_ID,FIELDEXPLAIN_ID,FIELDENUM_VALUE,FIELDENUM_NAME,FIELDENUM_PID FROM SMWB_SUPPORT.T_FIELDENUM WHERE FIELDEXPLAIN_ID IN (SELECT FIELDEXPLAIN_ID FROM SMWB_SUPPORT.T_FIELDEXPLAIN WHERE FIELDEXPLAIN_FIELD = 'DIVISION_CODE')");
 		for (Iterator<Map> jdList2 = jdList.iterator();jdList2.hasNext();) {
			Map map2 = jdList2.next();
			String FIELDENUM_PID = StringHelper.formatObject(map2.get("FIELDENUM_PID"));
			if ("-1".equals(FIELDENUM_PID)) {
				Tree tree2 = new Tree();
				tree2.setId(" ");
				tree2.setText(String.valueOf(map2.get("FIELDENUM_NAME")));
				treelist.add(tree2);
			}else {
				Tree tree2 = new Tree();
				tree2.setId(String.valueOf(map2.get("FIELDENUM_VALUE")));
				tree2.setText(String.valueOf(map2.get("FIELDENUM_NAME")));
				treelist.add(tree2);
			} 
			
		}
		return TreeUtil.build(treelist);
	}
}
