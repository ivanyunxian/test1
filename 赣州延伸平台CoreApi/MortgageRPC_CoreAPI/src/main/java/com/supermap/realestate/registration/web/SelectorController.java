package com.supermap.realestate.registration.web;

import com.alibaba.fastjson.JSONObject;
import com.supermap.realestate.registration.factorys.HandlerFactory;
import com.supermap.realestate.registration.mapping.SelectorConfig;
import com.supermap.realestate.registration.mapping.SelectorConfig.GridConfig.GridColumn;
import com.supermap.realestate.registration.mapping.SelectorConfig.ResultConfig.ConstMapping;
import com.supermap.realestate.registration.mapping.SelectorConfig.SortField;
import com.supermap.realestate.registration.model.*;
import com.supermap.realestate.registration.model.interfaces.RealUnit;
import com.supermap.realestate.registration.model.interfaces.RightsHolder;
import com.supermap.realestate.registration.tools.RightsHolderTools;
import com.supermap.realestate.registration.tools.UnitTools;
import com.supermap.realestate.registration.util.*;
import com.supermap.realestate.registration.util.ConstValue.*;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import com.supermap.wisdombusiness.web.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.persistence.Column;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @Description:登记簿Controller 跟登记簿相关的都放在这里边
 * @author 刘树峰
 * @date 2015年6月12日 上午11:46:55
 * @Copyright SuperMap
 */
@Controller
@RequestMapping("/query")
public class SelectorController {

	@Autowired
	private CommonDao dao;
	
	/*
	 * 选择器变量 暂时仅为判断DC库登记状态使用
	 */
	String DCselector;

	/**
	 * 表名和所有字段名的映射Map
	 */
	private static Map<String, String> entityfieldnames = new HashMap<String, String>();

	/**
	 * 服务接口：显示查询结果页面
	 * @Title: showQueryPage
	 * @author:liushufeng
	 * @date：2015年7月16日 下午8:30:20
	 * @param model
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping(value = "/queryindex/{selector}/", method = RequestMethod.GET)
	public String showQueryPage(@PathVariable("selector") String selector, Model model,HttpServletRequest request) throws UnsupportedEncodingException {
		String xmbh = RequestHelper.getParam(request, "xmbh");
		SelectorConfig config = new SelectorConfig();
		config = HandlerFactory.getSelectorByName(selector);
		BDCS_XMXX xmxx = dao.get(BDCS_XMXX.class, xmbh);
		if(xmxx!=null){
			String workflowcode = ProjectHelper.getWorkflowCodeByProjectID(xmxx.getPROJECT_ID());
			String sql = " WORKFLOWCODE='" + workflowcode + "'";
			List<WFD_MAPPING> mappings = dao.getDataList(WFD_MAPPING.class, sql);
			if (mappings != null && mappings.size() > 0) {
				WFD_MAPPING maping = mappings.get(0);
				if(("1").equals(maping.getDATASTYLE())){
					config.setShowDataStyle(true);
				}
			}
		}
		String strjson = JSONObject.toJSONString(config);
		System.out.println(strjson);
		model.addAttribute("config", strjson);
		return "/realestate/registration/common/selector";
	}
	
	/**
	 * 服务接口：显示查询结果页面
	 * @Title: showQueryPage
	 * @author:liushufeng
	 * @date：2015年7月16日 下午8:30:20
	 * @param model
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping(value = "/queryindex2/{selector}/", method = RequestMethod.GET)
	public String showQueryPage2(@PathVariable("selector") String selector, Model model,HttpServletRequest request) throws UnsupportedEncodingException {
		SelectorConfig config = new SelectorConfig();
		config = HandlerFactory.getSelectorByName(selector);
		String strjson = JSONObject.toJSONString(config);
		System.out.println(strjson);
		model.addAttribute("config", strjson);
		return "/realestate/registration/common/selector";
	}

	/**
	 * 服务接口：查询数据
	 * @Title: queryData
	 * @author:liushufeng
	 * @date：2015年7月16日 下午8:30:33
	 * @param request
	 * @return
	 * @throws ClassNotFoundException
	 * @throws UnsupportedEncodingException
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/queryindex/{selector}/querydata/", method = RequestMethod.GET)
	public @ResponseBody Message queryData(@PathVariable("selector") String selector, HttpServletRequest request) throws ClassNotFoundException, UnsupportedEncodingException {
		double starttime = System.currentTimeMillis();
		DCselector = selector;
		if (selector.equals("YCFSelector")) {
			return this.queryYCFData(request);
		}
		if (selector.equals("YCFXZSelector")) {
			return this.queryYCFXZData(request);
		}
		if ("FWCFSelector".equals(selector) || "ZDCFSelector".equals(selector) || "YCFWCFSelector".equals(selector)|| "ZHCFSelector".equals(selector)) { // 解封_房屋的、解封地
			return this.queryCFDJData(request, selector);
		}
		if ("DYBGSelector".equals(selector)) {
			return this.queryDYData(request, selector);
		}
		if ("DYBG_GYJSYDSelector".equals(selector)||"DYBG_JTTDSelector".equals(selector)||"DYBG_ZJDSelector".equals(selector)) {
			return this.queryZDDYData(request, selector);
		}
		String qzhcxms = ConfigHelper.getNameByValue("QZHCXMS");
		if(StringHelper.isEmpty(qzhcxms)){
			qzhcxms="/";
		}
		// 返回的结果对象
		Message msg = new Message();
		// 获取page和rows参数
		Integer page = RequestHelper.getPage(request);//
		Integer rows = RequestHelper.getRows(request);
		// 选择器配置
		SelectorConfig config = HandlerFactory.getSelectorByName(selector);
		// 查询结果列表
		List<Map> listresult = null;
		// 不动产单元类型
		BDCDYLX bdcdylx = BDCDYLX.initFrom(config.getBdcdylx());
		// 来源
		DJDYLY ly = DJDYLY.initFrom(config.getLy());
		// 单元实体名
		String unitentityName = bdcdylx.getTableName(ly);
		// 查询结果记录总数
		long count = 0;
		// 选择单元
		if (config.isSelectbdcdy()) {
			String fullentityname = "com.supermap.realestate.registration.model." + unitentityName;
			Class<?> T = Class.forName(fullentityname);
			javax.persistence.Table tableanno = T.getAnnotation(javax.persistence.Table.class);
			String tablename = tableanno.schema() + "." + tableanno.name();

			String dyfieldsname = getTableFieldsName2(unitentityName);

			String sql = "from " + tablename + " dy where 1>0 ";
			if (config.isUseconfigsql()) {
				sql = " from (" + config.getConfigsql();
			}
//			String cxtj ="";//查询条件
//			String cxzh ="";//输入的查询值
			for (int i = 0; i < config.getQueryconfig().getFields().size(); i++) {
				String name = config.getQueryconfig().getFields().get(i).getFieldname();
				String value = RequestHelper.getParam(request, name);
				if (!StringHelper.isEmpty(value)) {
					String _cond = "";
					if (("ZL").equals(name.toUpperCase()) || ("BDCDYH").equals(name.toUpperCase()) || !SF.YES.Value.equals(ConfigHelper.getNameByValue("PreciseQuery"))) {
						_cond = " and " + config.getQueryconfig().getFields().get(i).getEntityname() + "." + name + " like '%" + value + "%' ";
					} else {
						_cond = " and " + config.getQueryconfig().getFields().get(i).getEntityname() + "." + name + " = '" + value + "' ";
					}
					sql = sql + _cond;
//					cxtj = name.toUpperCase();
//					cxzh = value ;
				}
			}
			if (!StringHelper.isEmpty(config.getCondition())) {
				sql += " " + config.getCondition();
			}
			if (config.isUseconfigsql()) {
				sql += ")";
			}
//			if ("DCKFWSelector".equals(config.getName())) {//房地一体登记，dyh.sfdj_zd <> '1' ，sfdj_zd字段为宗地是否登记 的标识，1表示宗地已登记，0表示宗地未登记
//				sql =" from ( SELECT DY.*, CASE WHEN EXISTS ( SELECT SCBDCDYID FROM BDCK.YC_SC_H_XZ WHERE SCBDCDYID = DY.BDCDYID ) "
//						+ " THEN '是' ELSE '否' END SFGL, CASE WHEN EXISTS ( select * from BDCK.BDCS_shyqzd_XZ zd_xz "
//						+ " 	left join BDCDCK.BDCS_H_GZ H_GZ on zd_xz.bdcdyid = H_GZ.Zdbdcdyid where H_GZ."+ cxtj
//						+ " like '%"+ cxzh +"%' ) then '1' else '0' end sfdj_zd FROM BDCDCK.BDCS_H_GZ DY WHERE 1 > 0 "
//						+ " and DY."+ cxtj+" like '%"+ cxzh +"%' and 1 > 0 ) dyh where dyh.sfdj_zd = '0'";
//			}
			count = dao.getCountByFullSql(sql);
			String orderby = " ";
			if (config.getSortfields() != null && config.getSortfields().size() > 0) {
				orderby = " ORDER BY ";
				for (SortField sortfield : config.getSortfields()) {
					if (!StringHelper.isEmpty(sortfield.getEntityname())) {
						orderby = orderby + sortfield.getEntityname() + "." + sortfield.getFieldname() + " " + sortfield.getSorttype() + ",";
					} else {
						orderby = orderby + sortfield.getFieldname() + " " + sortfield.getSorttype() + ",";
					}
				}
				if (orderby.endsWith(",")) {
					orderby = orderby.substring(0, orderby.length() - 1);
				}
			}
			String fullsql = "select " + dyfieldsname + " " + sql + orderby;
			if (config.isUseconfigsql()) {
				String ssql = "select * " + sql + orderby;
				listresult = dao.getPageDataByFullSql(ssql, page, rows);
			} else {
				listresult = dao.getPageDataByFullSql(fullsql, page, rows);
			}
			addLimitStatus(listresult, bdcdylx);
		}// 选择权利
		else if (config.isSelectql()) {
			String fullSql = "";
			String fromSql = "";
			if (!config.isUseconfigsql()) {
				/* 实体对应的表名(前面加用户名.) */
				unitentityName = "BDCK." + bdcdylx.getTableName(DJDYLY.XZ);
				/* 表名+'_'+字段名 */
				String dyfieldsname = getTableFieldsName(bdcdylx.getTableName(DJDYLY.XZ), "DY");
				String qlfieldsname = getTableFieldsName("BDCS_QL_XZ", "QL");
				String fsqlfieldsname = getTableFieldsName("BDCS_FSQL_XZ", "FSQL");
				String zddyfieldsname = " ZDDY.ZL AS ZDDY_ZDZL,ZDDY.BDCDYH AS ZDDY_ZDBDCDYH ";
				String zrzdyfieldsname = " ZRZDY.ZL AS ZRZDY_ZRZZL,ZRZDY.BDCDYH AS ZRZDY_ZRZBDCDYH,ZRZDY.ZRZH AS ZRZDY_ZRZH ";

				// 先构造查询字段
				// 再构造出from后边，where前边的表
				// 再构造where条件

				/* SELECT 字段部分 */
				StringBuilder builder2 = new StringBuilder();
				builder2.append("select ").append(dyfieldsname).append(",").append(qlfieldsname).append(",").append(fsqlfieldsname);
				String selectstr = builder2.toString();

				/* FROM 后边的表语句 */
				/* 不跟权利人表做连接了 */
				StringBuilder builder = new StringBuilder();
				builder.append(" from {0} DY").append(" left join BDCK.BDCS_DJDY_XZ DJDY on dy.bdcdyid=djdy.bdcdyid")
						.append(" left join BDCK.bdcs_ql_xz ql on ql.djdyid=djdy.djdyid").append(" LEFT JOIN BDCK.BDCS_FSQL_XZ FSQL ON FSQL.QLID=QL.QLID ");
				
				StringBuilder builder4 = new StringBuilder();
				if(config.getName().equals("XZDJ_FWSYQSelector")||config.getName().equals("XZDJ_JTTDFWSYQSelector")||config.getName().equals("XZDJ_ZJDFWSYQSelector")){
					builder.append(" left join bdck.bdcs_shyqzd_xz zddy on dy.zdbdcdyid=zddy.bdcdyid left join bdck.bdcs_zrz_xz zrzdy on dy.zrzbdcdyid=zrzdy.bdcdyid ");
					builder4.append(",").append(zddyfieldsname).append(",").append(zrzdyfieldsname);
					selectstr += builder4.toString();
				}
				/* WHERE 条件语句 */
				StringBuilder builder3 = new StringBuilder();
				builder3.append(" where 1>0");
				if (!StringHelper.isEmpty(config.getCondition())) {
					builder3.append(config.getCondition());
				}
				
				for (int i = 0; i < config.getQueryconfig().getFields().size(); i++) {
					String name = config.getQueryconfig().getFields().get(i).getFieldname();
					String value = RequestHelper.getParam(request, name);
					if (!StringHelper.isEmpty(value)) {
						String entname = config.getQueryconfig().getFields().get(i).getEntityname();
						if (!StringHelper.isEmpty(entname)) {
							// 如果包括权利人相关的查询，再连接权利人表
							if (entname.toUpperCase().equals("QLR")) {
								if(value.indexOf(qzhcxms) == -1){
									if (("ZL").equals(name.toUpperCase()) || ("BDCDYH").equals(name.toUpperCase()) || !SF.YES.Value.equals(ConfigHelper.getNameByValue("PreciseQuery"))) {
										builder3.append(" and QL.QLID IN (SELECT QLID FROM BDCK.BDCS_QLR_XZ  WHERE  INSTR(" + name + ",'" + value + "')>0) ");
									} else {
										builder3.append(" and QL.QLID IN (SELECT QLID FROM BDCK.BDCS_QLR_XZ  WHERE  " + name + " = '" + value + "') ");
									}
								}else if(("BDCQZH").equals(name.toUpperCase())){
									String[] list = value.split(qzhcxms);
									String newvalue="";
									for (String string : list) {
										newvalue+="'" + string + "',";
									}
									newvalue = newvalue.substring(0, newvalue.length()-1);
									builder3.append(" and QL.QLID IN (SELECT QLID FROM BDCK.BDCS_QLR_XZ  WHERE  " + name + " IN (" + newvalue + ")) ");
								}
							}else if(entname.toUpperCase().equals("ZDDY")||entname.toUpperCase().equals("ZRZDY")){
								if(("ZDZL").equals(name.toUpperCase())){
									builder3.append(" and  exists(select 1 from bdck.bdcs_shyqzd_xz where zl like'%"+value+"%' and bdcdyid=dy.zdbdcdyid) ");
								}else if(("ZDBDCDYH").equals(name.toUpperCase())){
									builder3.append(" and  exists(select 1 from bdck.bdcs_shyqzd_xz where bdcdyh like '%"+value+"%' and bdcdyid=dy.zdbdcdyid) ");
								}else if(("ZRZH").equals(name.toUpperCase())){
									builder3.append(" and  exists(select 1 from bdck.bdcs_zrz_xz where zrzh like'%"+value+"%' and bdcdyid=dy.zrzbdcdyid) ");
								}else if(("ZRZZL").equals(name.toUpperCase())){
									builder3.append(" and  exists(select 1 from bdck.bdcs_zrz_xz where zl like'%"+value+"%' and bdcdyid=dy.zrzbdcdyid) ");
								}else if(("ZRZBDCDYH").equals(name.toUpperCase())){
									builder3.append(" and  exists(select 1 from bdck.bdcs_zrz_xz where bdcdyh like'%"+value+"%' and bdcdyid=dy.zrzbdcdyid) ");
								}else{
									builder3.append(" ");
								}
							}else {
								if(value.indexOf(qzhcxms) == -1){
									if (("ZL").equals(name.toUpperCase()) || ("BDCDYH").equals(name.toUpperCase()) || !SF.YES.Value.equals(ConfigHelper.getNameByValue("PreciseQuery"))) {
										builder3.append(" and instr(" + config.getQueryconfig().getFields().get(i).getEntityname() + "." + name + ",'" + value + "')>0 ");
										 } else {
										builder3.append(" and " + config.getQueryconfig().getFields().get(i).getEntityname() + "." + name + " = '" + value + "' ");
									}
								}else if(("BDCQZH").equals(name.toUpperCase())){
									String[] list = value.split(qzhcxms);
									String newvalue="";
									for (String string : list) {
										newvalue+="'" + string + "',";
									}
									newvalue = newvalue.substring(0, newvalue.length()-1);
									builder3.append(" and QL.QLID IN (SELECT QLID FROM BDCK.BDCS_QLR_XZ  WHERE  " + name + " IN (" + newvalue + ")) ");
								}
							}
						}
					}
				}
				
				String fromstr = builder.toString();
				fromstr = MessageFormat.format(fromstr, unitentityName);

				builder3.append(" and ql.qllx='" + config.getSelectqllx() + "'");
				String wherestr = builder3.toString();

				fromSql = " from (select ql.qlid " + fromstr + wherestr + ")";
				fullSql = selectstr + fromstr + wherestr;
			} else {
				String configsql = config.getConfigsql();
				for (int i = 0; i < config.getQueryconfig().getFields().size(); i++) {
					String name = config.getQueryconfig().getFields().get(i).getFieldname();
					String value = RequestHelper.getParam(request, name);
					if (!StringHelper.isEmpty(value)) {
						String entname = config.getQueryconfig().getFields().get(i).getEntityname();
						if (!StringHelper.isEmpty(entname)) {
							// 如果包括权利人相关的查询，再连接权利人表
							if (entname.toUpperCase().equals("QLR")) {
								if(value.indexOf(qzhcxms) == -1){
									if (("ZL").equals(name.toUpperCase()) || ("BDCDYH").equals(name.toUpperCase()) || !SF.YES.Value.equals(ConfigHelper.getNameByValue("PreciseQuery"))) {
										configsql += " and QL.QLID IN (SELECT QLID FROM BDCK.BDCS_QLR_XZ  WHERE  INSTR(" + name + ",'" + value + "')>0) ";
									} else {
										configsql += " and QL.QLID IN (SELECT QLID FROM BDCK.BDCS_QLR_XZ  WHERE  " + name + " = '" + value + "') ";
									}
								}else if(("BDCQZH").equals(name.toUpperCase())){
									String[] list = value.split(qzhcxms);
									String newvalue="";
									for (String string : list) {
										newvalue+="'" + string + "',";
									}
									newvalue = newvalue.substring(0, newvalue.length()-1);
									configsql +=" and QL.QLID IN (SELECT QLID FROM BDCK.BDCS_QLR_XZ  WHERE  " + name + " IN (" + newvalue + ")) ";
								}
							} else {
								if(value.indexOf(qzhcxms) == -1){
									if (("ZL").equals(name.toUpperCase()) || ("BDCDYH").equals(name.toUpperCase()) || !SF.YES.Value.equals(ConfigHelper.getNameByValue("PreciseQuery"))) {
										configsql += " and instr(" + config.getQueryconfig().getFields().get(i).getEntityname() + "." + name + ",'" + value + "')>0 ";
									} else {
										configsql += " and " + config.getQueryconfig().getFields().get(i).getEntityname() + "." + name + " = '" + value + "' ";
									}
								}else if(("BDCQZH").equals(name.toUpperCase())){
									String[] list = value.split(qzhcxms);
									String newvalue="";
									for (String string : list) {
										newvalue+="'" + string + "',";
									}
									newvalue = newvalue.substring(0, newvalue.length()-1);
									configsql +=" and QL.QLID IN (SELECT QLID FROM BDCK.BDCS_QLR_XZ  WHERE  " + name + " IN (" + newvalue + ")) ";
								}
							}
						}
					}
				}
				fromSql = "from (" + configsql + ")";
				fullSql = configsql;
			}
			count = dao.getCountByFullSql(fromSql);
			if (count > 0) {
				String orderby = " ";
				if (config.getSortfields() != null && config.getSortfields().size() > 0) {
					orderby = " ORDER BY ";
					for (SortField sortfield : config.getSortfields()) {
						orderby = orderby + sortfield.getEntityname() + "." + sortfield.getFieldname() + " " + sortfield.getSorttype() + ",";
					}
					if (orderby.endsWith(",")) {
						orderby = orderby.substring(0, orderby.length() - 1);
					}
				}
				fullSql = fullSql + orderby;
				listresult = dao.getPageDataByFullSql(fullSql, page, rows);
				listresult = revmoeprefix(listresult);
				addRightsHolderInfo(listresult);
				addLimitStatus(listresult, bdcdylx);
				if (ConfigHelper.getNameByValue("XZQHDM").startsWith("510")) {
					addYCLimitStatus(listresult, bdcdylx);
				}
			}
		}
		// 格式化结果中的常量值
		if (count > 0) {
			if (config.getResultconfig() != null && config.getResultconfig().getConstmappings() != null && config.getResultconfig().getConstmappings().size() > 0) {
				List<ConstMapping> cmapping = config.getResultconfig().getConstmappings();
				for (ConstMapping _mapping : cmapping) {
					for (Map map : listresult) {
						if (map.containsKey(_mapping.getFieldname())) {
							String value = StringHelper.formatObject(StringHelper.isEmpty(map.get(_mapping.getFieldname())) ? _mapping.getDefaultvalue() : map.get(_mapping
									.getFieldname()));
							if (_mapping.isNewfieldendwithname()) {
								map.put(_mapping.getFieldname() + "name", ConstHelper.getNameByValue(_mapping.getConsttype(), value));
							} else {
								// TODO @刘树峰 ，设置Map中的某一个键的值
							}
						}
					}
				}
			}
		}
		// 追加额外列
		List<GridColumn> Columns = config.getGridconfig().getColumns();
		for (GridColumn gridColumn : Columns) {
			if("SYQBDCQZH".equals(gridColumn.getFieldname())){
				for (Map result : listresult) {
					String qlid = result.get("QLID").toString();
					if(!StringHelper.isEmpty(qlid)){
						BDCS_QL_XZ ql_xz = dao.get(BDCS_QL_XZ.class, qlid);
						String djdyid = ql_xz.getDJDYID();
						 List<BDCS_QL_XZ> lyql_xz = dao.getDataList(BDCS_QL_XZ.class, " DJDYID='"+ djdyid+ "' AND QLLX='4'");
						if(lyql_xz!=null&&lyql_xz.size()>0){
							BDCS_QL_XZ ly_xz = lyql_xz.get(0);
							result.put("SYQBDCQZH", ly_xz.getBDCQZH());
						}
						if("1".equals(ql_xz.getISPARTIAL())){
							List<BDCS_PARTIALLIMIT> partql = dao.getDataList(BDCS_PARTIALLIMIT.class, " LIMITQLID='"+ qlid +"' AND LIMITTYPE='"+ config.getSelectqllx() +"'");
							if(partql!=null&&partql.size()>0){
								String qlrid = partql.get(0).getQLRID();
								BDCS_QLR_XZ qlr = dao.get(BDCS_QLR_XZ.class, qlrid);
								if(qlr!=null){
									result.put("SYQBDCQZH", qlr.getBDCQZH());
								}else{
									result.put("SYQBDCQZH", "");
								}
							}
						}
					}
				}
				break;
			}
		}
		setColorByExistGz(listresult);
		msg.setTotal(count);
		msg.setRows(listresult);
		System.out.println(System.currentTimeMillis() - starttime);
		return msg;
	}

	
	/**
	 * 查询现状户信息
	 * @param request
	 * @return
	 * @throws ClassNotFoundException
	 * @throws UnsupportedEncodingException
	 */
	private Message queryYCFXZData(HttpServletRequest request) throws ClassNotFoundException, UnsupportedEncodingException {
		long count = 0;
		Message msg = new Message();
		// 获取page和rows参数
		Integer page = RequestHelper.getPage(request);//
		Integer rows = RequestHelper.getRows(request);
		String dytablename = getTableFieldsName("BDCS_H_XZ", "DY");
		String selectstr = "select distinct " + dytablename + ",DJDY.DJDYID as DJDY_DJDYID,QL.QLID AS QL_QLID,QL.BDCQZH AS QL_BDCQZH ";
		String strfrom = "FROM BDCK.bdcs_h_xz dy LEFT JOIN BDCK.bdcs_djdy_xz djdy ON djdy.bdcdyid =dy.bdcdyid LEFT JOIN BDCK.bdcs_ql_xz ql ON ql.djdyid=djdy.djdyid AND ql.qllx='99' and ql.djlx='700' LEFT JOIN BDCK.bdcs_qlr_xz qlr ON qlr.qlid=ql.qlid";
		String strsql = selectstr + strfrom;

		SelectorConfig config = HandlerFactory.getSelectorByName("YCFXZSelector");

		/* WHERE 条件语句 */
		StringBuilder builder3 = new StringBuilder();
		builder3.append(" where 1>0");
		for (int i = 0; i < config.getQueryconfig().getFields().size(); i++) {
			String name = config.getQueryconfig().getFields().get(i).getFieldname();
			String value = RequestHelper.getParam(request, name);
			if (!StringHelper.isEmpty(value)) {
				if (("ZL").equals(name.toUpperCase()) || ("BDCDYH").equals(name.toUpperCase()) || !SF.YES.Value.equals(ConfigHelper.getNameByValue("PreciseQuery"))) {
					builder3.append(" and instr(" + config.getQueryconfig().getFields().get(i).getEntityname() + "." + name + ",'" + value + "')>0 ");
				} else {
					builder3.append(" and " + config.getQueryconfig().getFields().get(i).getEntityname() + "." + name + " = '" + value + "' ");
				}
			}
		}
		strsql = strsql + builder3.toString();
		String fromsql = " FROM (" + strsql + ")";
		count = dao.getCountByFullSql(fromsql);
		String orderby = " ";
		if (config.getSortfields() != null && config.getSortfields().size() > 0) {
			orderby = " ORDER BY ";
			for (SortField sortfield : config.getSortfields()) {
				orderby = orderby + sortfield.getEntityname() + "." + sortfield.getFieldname() + " " + sortfield.getSorttype() + ",";
			}
			if (orderby.endsWith(",")) {
				orderby = orderby.substring(0, orderby.length() - 1);
			}
		}
		strsql = strsql + orderby;
		@SuppressWarnings("rawtypes")
		List<Map> listresult = dao.getPageDataByFullSql(strsql, page, rows);
		if (listresult != null && listresult.size() > 0) {
			listresult = revmoeprefix(listresult);
			addRightsHolderInfo(listresult);
			addLimitStatus(listresult, BDCDYLX.H);
			setColorByExistGz(listresult);
		}
		msg.setTotal(count);
		msg.setRows(listresult);
		return msg;
	
		
	}

	private Message queryXZDJData(HttpServletRequest request, String selector) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 抵押变更选择器-变更单元个数的，输入一个证号，汇总这个他项证号下边的单元数，总面积等
	 * @Title: queryDYData
	 * @author:liushufeng
	 * @date：2016年1月10日 下午3:58:57
	 * @param request
	 * @param selector
	 * @return
	 * @throws ClassNotFoundException
	 * @throws UnsupportedEncodingException
	 */
	@SuppressWarnings("rawtypes")
	private Message queryDYData(HttpServletRequest request, String selector) throws ClassNotFoundException, UnsupportedEncodingException {
		Message msg = new Message();
		// 获取page和rows参数
		Integer page = RequestHelper.getPage(request);
		Integer rows = RequestHelper.getRows(request);
		// 选择器配置
		SelectorConfig config = HandlerFactory.getSelectorByName(selector);
		// 查询结果列表
		List<Map> listresult = null;
		String qzhcxms = ConfigHelper.getNameByValue("QZHCXMS");
		if(StringHelper.isEmpty(qzhcxms)){
			qzhcxms="/";
		}

		StringBuilder builder = new StringBuilder();
		builder.append("SELECT QL.BDCQZH TXQZH,");
		builder.append("COUNT(*) DYDYGS,");
		builder.append("SUM(H.MJ) DYMJ ");
		builder.append("FROM BDCK.BDCS_QLR_XZ QLR ");
		builder.append("LEFT JOIN BDCK.BDCS_QL_XZ QL ON QL.QLID=QLR.QLID AND QL.QLLX='23' ");
		builder.append("LEFT JOIN BDCK.BDCS_DJDY_XZ DJDY ON DJDY.DJDYID=QL.DJDYID ");
		builder.append("LEFT JOIN ");
		builder.append("(SELECT BDCDYID,YCJZMJ AS MJ FROM BDCK.BDCS_H_XZY UNION ALL SELECT BDCDYID,SCJZMJ AS MJ FROM BDCK.BDCS_H_XZ)  H ON H.BDCDYID=DJDY.BDCDYID ");
		builder.append("WHERE  QL.QLLX='23' ");

		StringBuilder builder2 = new StringBuilder();
		builder2.append("FROM (SELECT  QL.BDCQZH ");
		builder2.append("FROM BDCK.BDCS_QLR_XZ QLR  ");
		builder2.append("LEFT JOIN BDCK.BDCS_QL_XZ QL ON QL.QLID=QLR.QLID AND QL.QLLX='23' ");
		builder2.append("LEFT JOIN BDCK.BDCS_DJDY_XZ DJDY ON DJDY.DJDYID=QL.DJDYID ");
		builder2.append("LEFT JOIN (SELECT BDCDYID,YCJZMJ AS MJ FROM BDCK.BDCS_H_XZY UNION ALL SELECT BDCDYID,SCJZMJ AS MJ FROM BDCK.BDCS_H_XZ)  H ON H.BDCDYID=DJDY.BDCDYID ");
		builder2.append("WHERE QL.QLLX='23' ");

		String XZQHDM=ConfigHelper.getNameByValue("XZQHDM");
		for (int i = 0; i < config.getQueryconfig().getFields().size(); i++) {
			String name = config.getQueryconfig().getFields().get(i).getFieldname();
			String entityname = config.getQueryconfig().getFields().get(i).getEntityname();
			String value = RequestHelper.getParam(request, name);
			if (!StringHelper.isEmpty(value)) {
				if(value.indexOf(qzhcxms) == -1){
					if(XZQHDM.indexOf("45")==0){//广西需求，他项权证号支持模糊查询
						builder.append(" AND ").append(entityname).append(".BDCQZH like '%").append(value).append("%'");
						builder2.append(" AND ").append(entityname).append(".BDCQZH like '%").append(value).append("%'");
					}else{
						builder.append(" AND ").append(entityname).append(".").append(name).append(" ='").append(value).append("'");
						builder2.append(" AND ").append(entityname).append(".").append(name).append(" ='").append(value).append("'");
					}
					
				}else if(("BDCQZHXH").equals(name.toUpperCase())){
					String[] list = value.split(qzhcxms);
					String newvalue="";
					for (String string : list) {
						newvalue+="'" + string + "',";
					}
					newvalue = newvalue.substring(0, newvalue.length()-1);
					builder.append(" AND ").append(entityname).append(".").append(name).append(" IN (").append(newvalue).append(")");
					builder2.append(" AND ").append(entityname).append(".").append(name).append(" IN (").append(newvalue).append(")");
				}
			}
		}

		builder.append(" GROUP BY QL.BDCQZH ");
		builder2.append(" GROUP BY QL.BDCQZH) ");
		

		listresult = dao.getPageDataByFullSql(builder.toString(), page, rows);
		long count=dao.getCountByFullSql(builder2.toString());
		addDYExternalInfo(listresult);
		setColorByExistGz(listresult);
		msg.setTotal(count);
		msg.setRows(listresult);
		return msg;
	}

	private void addDYExternalInfo(List<Map> result) {

		StringBuilder builder = new StringBuilder();
		builder.append("SELECT H.ZL,H.BDCDYH,QLR.QLRMC,FSQL.DYR FROM BDCK.BDCS_QL_XZ QL ");
		builder.append("LEFT JOIN BDCK.BDCS_FSQL_XZ FSQL ON QL.QLID=FSQL.QLID ");
		builder.append("LEFT JOIN BDCK.BDCS_QLR_XZ QLR ON QL.QLID=QLR.QLID ");
		builder.append("LEFT JOIN BDCK.BDCS_DJDY_XZ DJDY ON DJDY.DJDYID=QL.DJDYID ");
		builder.append("LEFT JOIN  (SELECT ZL,BDCDYH,BDCDYID,YCJZMJ AS MJ FROM BDCK.BDCS_H_XZY UNION ALL SELECT ZL,BDCDYH,BDCDYID,SCJZMJ AS MJ FROM BDCK.BDCS_H_XZ)  H ");
		builder.append("ON H.BDCDYID=DJDY.BDCDYID ");
		builder.append("WHERE  QL.QLLX='23' AND  QL.BDCQZH=''{0}'' AND ROWNUM<2  ");
		String sql = builder.toString();
		if (result != null && result.size() > 0) {
			for (Map map : result) {
				if (map.containsKey("TXQZH")) {
					String txqzh = (String) map.get("TXQZH");
					String tmpsql = MessageFormat.format(sql, txqzh);
					List<Map> tempmap = dao.getDataListByFullSql(tmpsql);
					if (tempmap != null && tempmap.size() > 0) {
						String zl = tempmap.get(0) == null ? "" : tempmap.get(0).get("ZL") == null ? "" : tempmap.get(0).get("ZL").toString() + "(等)";
						String dyqr = tempmap.get(0) == null ? "" : tempmap.get(0).get("QLRMC") == null ? "" : tempmap.get(0).get("QLRMC").toString();
						String dyr = tempmap.get(0) == null ? "" : tempmap.get(0).get("DYR") == null ? "" : tempmap.get(0).get("DYR").toString();
						String bdcdyh = tempmap.get(0) == null ? "" : tempmap.get(0).get("BDCDYH") == null ? "" : tempmap.get(0).get("BDCDYH").toString() + "(等)";
						map.put("ZL", zl);
						map.put("BDCDYH", bdcdyh);
						map.put("DYQR", dyqr);
						map.put("DYR", dyr);
					}
				}
			}
		}
	}

	/**
	 * 查询查封的信息，用于解封登记
	 * @author diaoliwei
	 * @date：2015年8月6日 下午11:20:11
	 * @param request
	 * @param selector
	 *            选择器
	 * @return
	 * @throws ClassNotFoundException
	 * @throws UnsupportedEncodingException
	 */
	@SuppressWarnings("rawtypes")
	private Message queryCFDJData(HttpServletRequest request, String selector) throws ClassNotFoundException, UnsupportedEncodingException {
		Message msg = new Message();
		// 获取page和rows参数
		Integer page = RequestHelper.getPage(request);
		Integer rows = RequestHelper.getRows(request);
		// 选择器配置
		SelectorConfig config = HandlerFactory.getSelectorByName(selector);
		// 查询结果列表
		List<Map> listresult = null;
		// 不动产单元类型
		BDCDYLX bdcdylx = BDCDYLX.initFrom(config.getBdcdylx());
		// 来源
		DJDYLY ly = DJDYLY.initFrom(config.getLy());
		String qzhcxms = ConfigHelper.getNameByValue("QZHCXMS");
		if(StringHelper.isEmpty(qzhcxms)){
			qzhcxms="/";
		}
		// 单元实体名
		String unitentityName = bdcdylx.getTableName(ly);
		unitentityName = "BDCK." + bdcdylx.getTableName(DJDYLY.XZ);
		String dyfieldsname = getTableFieldsName(bdcdylx.getTableName(DJDYLY.XZ), "DY");
		String qlfieldsname = getTableFieldsName("BDCS_QL_XZ", "QL");
		String fsqlfieldsname = getTableFieldsName("BDCS_FSQL_XZ", "FSQL");
		String syqfieldsname = " SYQ.QLID AS SYQ_SYQQLID,SYQ.BDCQZH AS SYQ_BDCQZH";// 不动产权证号

		/* SELECT 字段部分 */
		StringBuilder builder2 = new StringBuilder();
		builder2.append("select ").append(dyfieldsname).append(",").append(qlfieldsname).append(",").append(fsqlfieldsname);
		builder2.append(",").append(syqfieldsname);
		String selectstr = builder2.toString().toUpperCase();
		selectstr = selectstr.replace("QL.BDCQZH AS QL_BDCQZH,", "");
		/* FROM 后边的表语句 */
		StringBuilder builder = new StringBuilder();
		builder.append(" from {0} DY").append(" left join BDCK.BDCS_DJDY_XZ DJDY on dy.bdcdyid=djdy.bdcdyid").append(" left join BDCK.bdcs_ql_xz ql on ql.djdyid=djdy.djdyid")
				.append(" left JOIN BDCK.BDCS_FSQL_XZ FSQL ON FSQL.QLID=QL.QLID");
		builder.append(" left JOIN BDCK.BDCS_QL_XZ SYQ ON SYQ.DJDYID = DJDY.DJDYID AND SYQ.QLLX= ''{1}'' ");

		String qllx = "";
		if (ConstValue.BDCDYLX.SHYQZD.Value.equals(bdcdylx.Value)) { // 地
			qllx = ConstValue.QLLX.GYJSYDSHYQ.Value;
		}
		if (ConstValue.BDCDYLX.H.Value.equals(bdcdylx.Value) || ConstValue.BDCDYLX.YCH.Value.equals(bdcdylx.Value)) { // 户
			qllx = ConstValue.QLLX.GYJSYDSHYQ_FWSYQ.Value;
		}
		if (ConstValue.BDCDYLX.HY.Value.equals(bdcdylx.Value)) { // 户
			qllx = ConstValue.QLLX.HYSYQ.Value;
		}
		String fromstr = builder.toString();
		fromstr = MessageFormat.format(fromstr, unitentityName, qllx);

		/* WHERE 条件语句 */
		StringBuilder builder3 = new StringBuilder();
		String SYQRCondition = "";
		builder3.append(" where 1>0");
		for (int i = 0; i < config.getQueryconfig().getFields().size(); i++) {
			String name = config.getQueryconfig().getFields().get(i).getFieldname();
			String entityname = config.getQueryconfig().getFields().get(i).getEntityname();
			String value = RequestHelper.getParam(request, name);
			if (!StringHelper.isEmpty(value)) {
				if (!StringHelper.isEmpty(entityname) && entityname.toUpperCase().equals("SYQR")) {
					if (ConstValue.BDCDYLX.H.Value.equals(bdcdylx.Value))// 房屋，都用精确查询
					{
						if (SF.YES.Value.equals(ConfigHelper.getNameByValue("PreciseQuery"))) {
							SYQRCondition += " AND SYQR." + name + "='" + value + "'";
						} else {
							SYQRCondition += " AND instr(SYQ." + name + ",'" + value + "')>0 ";
						}
					} else { // 宗地的就使用模糊查询
						SYQRCondition += " AND instr(SYQ." + name + ",'" + value + "')>0 ";
					}

				} else {
					if (("ZL").equals(name.toUpperCase()) || ("BDCDYH").equals(name.toUpperCase()) || !SF.YES.Value.equals(ConfigHelper.getNameByValue("PreciseQuery"))) {
						if(("BDCQZH").equals(name.toUpperCase())){//查封登记以后，不动产权证号没有再权利表中保存，续查封跟解封不动产权证号只能再实体权利表中查找
							if(value.indexOf(qzhcxms) == -1){
								builder3.append(" and exists (select qlid from BDCK.BDCS_QL_XZ WHERE instr(BDCQZH,'"+value +"')>0 and DJDYID=DJDY.DJDYID)");
//								builder3.append(" and instr(SYQ." + name + ",'" + value + "')>0 ");
							}else{
								String[] list = value.split(qzhcxms);
								String newvalue="";
								for (String string : list) {
									newvalue+="'" + string + "',";
								}
								newvalue = newvalue.substring(0, newvalue.length()-1);
								builder3.append(" and exists (select qlid from BDCK.BDCS_QL_XZ WHERE BDCQZH IN ("+newvalue +") and DJDYID=DJDY.DJDYID)");
							}
						}else{
							builder3.append(" and instr(" + config.getQueryconfig().getFields().get(i).getEntityname() + "." + name + ",'" + value + "')>0 ");
						}
					} else {
							if(("BDCQZH").equals(name.toUpperCase())){
								if(value.indexOf(qzhcxms) == -1){
//									builder3.append(" and SYQ." + name + " = '" + value + "' ");
								    builder3.append(" and exists (select qlid from BDCK.BDCS_QL_XZ WHERE BDCQZH='"+value +"' and DJDYID=DJDY.DJDYID)");
								}else{
									String[] list = value.split(qzhcxms);
									String newvalue="";
									for (String string : list) {
										newvalue+="'" + string + "',";
									}
									newvalue = newvalue.substring(0, newvalue.length()-1);
								    builder3.append(" and exists (select qlid from BDCK.BDCS_QL_XZ WHERE BDCQZH IN ("+newvalue +") and DJDYID=DJDY.DJDYID)");
								}
							}else{
								builder3.append(" and " + config.getQueryconfig().getFields().get(i).getEntityname() + "." + name + " = '" + value + "' ");
							}
						}
				}
			}
		}
		if (!StringHelper.isEmpty(SYQRCondition)) {
			String sssss = " and EXISTS(SELECT 1 FROM BDCK.BDCS_QL_XZ SYQ2 LEFT JOIN BDCK.BDCS_QLR_XZ SYQR ON SYQR.QLID=SYQ2.QLID WHERE SYQ2.QLLX='" + qllx
					+ "' AND SYQ2.DJDYID=QL.DJDYID " + SYQRCondition + ")";
			builder3.append(sssss);
		}

		builder3.append(" and ql.qllx='" + config.getSelectqllx() + "'");
		String wherestr = builder3.toString();
		String fromSql = " from (select ql.qlid " + fromstr + wherestr + ")";
		String fullSql = selectstr + fromstr + wherestr;

		/* 为了提高效率，先不查询符合条件的记录总数，将来改为异步获取 */
		Long count = dao.getCountByFullSql(fromSql);
		;// dao.getCountByFullSql(fromSql);
		if (count > 0) {
			String orderby = " ";
			if (config.getSortfields() != null && config.getSortfields().size() > 0) {
				orderby = " ORDER BY ";
				for (SortField sortfield : config.getSortfields()) {
					orderby = orderby + sortfield.getEntityname() + "." + sortfield.getFieldname() + " " + sortfield.getSorttype() + ",";
				}
				if (orderby.endsWith(",")) {
					orderby = orderby.substring(0, orderby.length() - 1);
				}
			}
			fullSql = fullSql + orderby;
			listresult = dao.getPageDataByFullSql(fullSql, page, rows);
			listresult = revmoeprefix(listresult);
			addRightsHolderInfoBySyq(listresult);
			setColorByExistGz(listresult);
			// addLimitStatus(listresult);
		}
		msg.setTotal(count);
		msg.setRows(listresult);
		return msg;
	}

	/**
	 * 查询预测户的信息，包括已经做过预告登记的和未做过预告登记的
	 * @Title: queryYCFData
	 * @author:liushufeng
	 * @date：2015年8月4日 上午1:43:01
	 * @param request
	 * @return
	 * @throws ClassNotFoundException
	 * @throws UnsupportedEncodingException
	 */
	private Message queryYCFData(HttpServletRequest request) throws ClassNotFoundException, UnsupportedEncodingException {
		long count = 0;
		Message msg = new Message();
		// 获取page和rows参数
		Integer page = RequestHelper.getPage(request);//
		Integer rows = RequestHelper.getRows(request);
		String dytablename = getTableFieldsName("BDCS_H_XZY", "DY");
		String selectstr = "select distinct " + dytablename + ",DJDY.DJDYID as DJDY_DJDYID,QL.QLID AS QL_QLID,QL.BDCQZH AS QL_BDCQZH ";
		String strfrom = "FROM BDCK.bdcs_h_xzy dy LEFT JOIN BDCK.bdcs_djdy_xz djdy ON djdy.bdcdyid =dy.bdcdyid LEFT JOIN BDCK.bdcs_ql_xz ql ON ql.djdyid=djdy.djdyid AND ql.qllx='4' LEFT JOIN BDCK.bdcs_qlr_xz qlr ON qlr.qlid=ql.qlid";
		String strsql = selectstr + strfrom;

		SelectorConfig config = HandlerFactory.getSelectorByName("YCFSelector");

		/* WHERE 条件语句 */
		StringBuilder builder3 = new StringBuilder();
		builder3.append(" where 1>0");
		for (int i = 0; i < config.getQueryconfig().getFields().size(); i++) {
			String name = config.getQueryconfig().getFields().get(i).getFieldname();
			String value = RequestHelper.getParam(request, name);
			if (!StringHelper.isEmpty(value)) {
				if (("ZL").equals(name.toUpperCase()) || ("BDCDYH").equals(name.toUpperCase()) || !SF.YES.Value.equals(ConfigHelper.getNameByValue("PreciseQuery"))) {
					builder3.append(" and instr(" + config.getQueryconfig().getFields().get(i).getEntityname() + "." + name + ",'" + value + "')>0 ");
				} else {
					builder3.append(" and " + config.getQueryconfig().getFields().get(i).getEntityname() + "." + name + " = '" + value + "' ");
				}
			}
		}
		strsql = strsql + builder3.toString();
		String fromsql = " FROM (" + strsql + ")";
		count = dao.getCountByFullSql(fromsql);
		String orderby = " ";
		if (config.getSortfields() != null && config.getSortfields().size() > 0) {
			orderby = " ORDER BY ";
			for (SortField sortfield : config.getSortfields()) {
				orderby = orderby + sortfield.getEntityname() + "." + sortfield.getFieldname() + " " + sortfield.getSorttype() + ",";
			}
			if (orderby.endsWith(",")) {
				orderby = orderby.substring(0, orderby.length() - 1);
			}
		}
		strsql = strsql + orderby;
		@SuppressWarnings("rawtypes")
		List<Map> listresult = dao.getPageDataByFullSql(strsql, page, rows);
		if (listresult != null && listresult.size() > 0) {
			listresult = revmoeprefix(listresult);
			addRightsHolderInfo(listresult);
			addLimitStatus(listresult, BDCDYLX.YCH);
			setColorByExistGz(listresult);
		}
		msg.setTotal(count);
		msg.setRows(listresult);
		return msg;
	}

	@RequestMapping(value = "/querylimit/{qlid}/", method = RequestMethod.GET)
	public @ResponseBody Map<String, String> getLimitInfo(@PathVariable("qlid") String qlid) {
		Map<String, String> map = new HashMap<String, String>();
		String mortgageStatus = "无抵押";
		String sealStatus = "无查封";
		String objectionStatus = "无异议";
		BDCS_QL_XZ xzql = dao.get(BDCS_QL_XZ.class, qlid);
		if (xzql != null) {
			String djdyid = xzql.getDJDYID();
			String sqlMortgage = MessageFormat.format(" from BDCK.BDCS_QL_XZ where djdyid=''{0}'' and qllx=''23''", djdyid);
			String sqlSeal = MessageFormat.format(" from BDCK.BDCS_QL_XZ where djdyid=''{0}'' and djlx=''800'' and qllx=''99''", djdyid);
			String sqlObjection = MessageFormat.format("  from BDCK.BDCS_QL_XZ where djdyid=''{0}'' and djlx=''600'' ", djdyid);
			long mortgageCount = dao.getCountByFullSql(sqlMortgage);
			long SealCount = dao.getCountByFullSql(sqlSeal);
			long ObjectionCount = dao.getCountByFullSql(sqlObjection);
			mortgageStatus = mortgageCount > 0 ? "抵押中" : "无抵押";
			sealStatus = SealCount > 0 ? "查封中" : "无查封";
			objectionStatus = ObjectionCount > 0 ? "有异议" : "无异议";

			// 判断完现状层中的查封信息，接着判断办理中的查封信息
			if (!(SealCount > 0)) {
				String sqlSealing = " FROM bdcs_xmxx a LEFT JOIN bdcs_djdy_gz b ON a.xmbh=b.xmbh AND a.sfdb='0' LEFT JOIN bdcs_ql_gz c ON b.djdyid=c.djdyid WHERE c.djlx='800' AND c.qllx='99'";
				long count = dao.getCountByFullSql(sqlSealing);
				sealStatus = count > 0 ? "查封办理中" : "无查封";
			}
		}
		map.put("DYZT", mortgageStatus);
		map.put("CFZT", sealStatus);
		map.put("YYZT", objectionStatus);
		return map;
	}

	/**
	 * 内部方法：移除查询结果中的表名前缀
	 * @Title: revmoeprefix
	 * @author:liushufeng
	 * @date：2015年7月15日 下午11:35:34
	 * @param listresult
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private List<Map> revmoeprefix(List<Map> listresult) {
		List<Map> list = new ArrayList<Map>();
		for (Map map : listresult) {
			Map mp = new HashMap<String, Object>();
			for (Object str : map.keySet()) {
				if (((String) str).toUpperCase().equals("DY_QLLX") || ((String) str).toUpperCase().equals("FSQL_BDCDYH") || ((String) str).toUpperCase().equals("FSQL_ZL"))
					continue;
				String strnew = replaceXHX((String) str);
				try {
					// 权利类型，要用权利表里的
					if (!mp.containsKey(strnew)) {
						mp.put(strnew, map.get(str));
					}
				} catch (Exception e) {
					System.out.println(str);
				}
			}
			list.add(mp);
		}
		return list;
	}

	/**
	 * 内部方法：分离表别名和字段名从类似 DY_DJDYID中提取出DJDYID
	 * @Title: replaceXHX
	 * @author:liushufeng
	 * @date：2015年7月16日 下午8:27:06
	 * @param str
	 * @return
	 */
	private String replaceXHX(String str) {
		String strnew = "";
		try {
			// TODO 暂时这样处理 字段：所在层， 不从附属权利表中取 ，从单元（户）表里取 2015-7-21
			if (str.toUpperCase().indexOf("FSQL_SZC") > -1) {
				return "";
			}
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
	 * 内部方法：给查询出来的权利添加权利人信息
	 * @Title: addRightsHolderInfo
	 * @author:liushufeng
	 * @date：2015年7月16日 下午8:27:51
	 * @param result
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void addRightsHolderInfo(List<Map> result) {
		if (result != null && result.size() > 0) {
			for (Map map : result) {
				if (map.containsKey("QLID")) {
					String qlid = (String) map.get("QLID");
					if (qlid != null) {
						RightsHolder holder = getUnionRightsHolder(qlid);
						if (holder != null) {
							map.put("QLRMC", holder.getQLRMC());
							map.put("DH", holder.getDH());
						}
					}
				}
			}
		}
	}

	/**
	 * 查询所有权的权利人
	 * @author diaoliwei
	 * @date 2015-8-7 2:54
	 * @param result
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void addRightsHolderInfoBySyq(List<Map> result) {
		if (result != null && result.size() > 0) {
			for (Map map : result) {
				if (map.containsKey("SYQQLID")) {
					String qlid = (String) map.get("SYQQLID");
					if (qlid != null) {
						RightsHolder holder = getUnionRightsHolder(qlid);
						if (holder != null) {
							map.put("QLRMC", holder.getQLRMC());
							map.put("DH", holder.getDH());
						}
					}
				}
			}
		}
	}

	/**
	 * 内部方法：给查询出来的权利记录增加限制信息：抵押、查封、异议
	 * @Title: addLimitStatus
	 * @author:liushufeng
	 * @date：2015年7月16日 下午8:28:10
	 * @param result
	 */
	/*
	 * 额外加上 DC库登记状态
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void addLimitStatus(List<Map> result, BDCDYLX bdcdylx) {
		if (result != null && result.size() > 0) {
			for (Map map : result) {
				if (map.containsKey("DJDYID")||map.containsKey("BDCDYID")) {
					String djdyid = (String) map.get("DJDYID");
					if (djdyid != null) {
						if (map.containsKey("DJZTname")) {
							map.remove("DJZTname");
						}
						if (map.containsKey("DJZT")) {
							map.remove("DJZT");
						}
						String fullsql = "";
						String lx = "";
						if(bdcdylx == BDCDYLX.H){
							//直接查封现房
						 fullsql = MessageFormat.format(""
									+ "SELECT SCQL.QLID AS SCH, YCQL.QLID AS YCH, SCQL.QLLX AS SCQLLX, YCQL.QLLX AS YCQLLX, SCQL.DJLX AS SCDJLX, YCQL.DJLX AS YCDJLX, ''XZ'' AS LY "
									+ "FROM BDCK.BDCS_QL_XZ SCQL LEFT JOIN BDCK.YC_SC_H_XZ GX ON GX.SCBDCDYID = SCQL.BDCDYID "
									+ "LEFT JOIN BDCK.BDCS_QL_XZ YCQL ON YCQL.BDCDYID = GX.YCBDCDYID WHERE SCQL.DJDYID=''{0}'' OR YCQL.DJDYID=''{0}'' ",djdyid);
						}else{
							lx = "YCH";
							 fullsql = MessageFormat.format(""
									+ "SELECT SCQL.QLID AS SCH, YCQL.QLID AS YCH, SCQL.QLLX AS SCQLLX, YCQL.QLLX AS YCQLLX, SCQL.DJLX AS SCDJLX, YCQL.DJLX AS YCDJLX, ''XZ'' AS LY "
									+ "FROM BDCK.BDCS_QL_XZ SCQL LEFT JOIN BDCK.YC_SC_H_XZ GX ON GX.SCBDCDYID = SCQL.BDCDYID "
									+ "LEFT JOIN BDCK.BDCS_QL_XZ YCQL ON YCQL.BDCDYID = GX.YCBDCDYID WHERE SCQL.DJDYID=''{0}'' OR YCQL.DJDYID=''{0}'' "
									+ "UNION ALL SELECT SCQL.QLID AS SCH, YCQL.QLID AS YCH, SCQL.QLLX AS SCQLLX, YCQL.QLLX AS YCQLLX, SCQL.DJLX AS SCDJLX, YCQL.DJLX AS YCDJLX, ''GZ'' AS LY "
									+ "FROM BDCK.BDCS_QL_GZ SCQL INNER JOIN BDCK.BDCS_XMXX XM ON XM.XMBH=SCQL.XMBH AND XM.SFDB='0' "
									+ "LEFT JOIN BDCK.YC_SC_H_XZ GX ON GX.SCBDCDYID = SCQL.BDCDYID "
									+ "LEFT JOIN BDCK.BDCS_QL_GZ YCQL ON YCQL.BDCDYID = GX.YCBDCDYID WHERE SCQL.DJDYID=''{0}'' OR YCQL.DJDYID=''{0}''",djdyid);
						}
						if(bdcdylx==BDCDYLX.YCH){
							lx = "SCH";
//							fullsql = fullsql.replaceAll("LEFT", "RIGHT");
						}
						List<Map> resultlist = dao.getDataListByFullSql(fullsql);
						
						map.put("DYZTCODE", false);
						map.put("CFZTCODE", false);
						map.put("YYZTCODE", false);
						DJZT HandleStatus = DJZT.WDJ;
						String qlid,scqllx,ycqllx,scdjlx,ycdjlx;
						String mortgageStatus = "无抵押",
								sealStatus = "无查封",
								objectionStatus = "无异议";
						//首先判断xz
						for (Map result1 : resultlist) {
							String ly = StringHelper.formatObject(result1.get("LY"));
							if("GZ".equals(ly)){
								HandleStatus = DJZT.DJZ;
								continue;
							}
							scqllx = StringHelper.formatObject(result1.get("SCQLLX"));
							scdjlx = StringHelper.formatObject(result1.get("SCDJLX"));
							//cf diy yy
							if(DJLX.CFDJ.Value.equals(scdjlx)&&QLLX.QTQL.Value.equals(scqllx)){
								sealStatus = "查封中";
								map.put("CFZTCODE", true);
							}
							if(QLLX.DIYQ.Value.equals(scqllx)){
								mortgageStatus = "抵押中";
								map.put("DYZTCODE", true);
							}
							if(DJLX.YYDJ.Value.equals(scdjlx)){
								objectionStatus = "有异议";
								map.put("YYZTCODE", true);
							}
						}
						//再判断gz
						for (Map result1 : resultlist) {
							String ly = StringHelper.formatObject(result1.get("LY"));
							if("XZ".equals(ly)){
								continue;
							}
							scqllx = StringHelper.formatObject(result1.get("SCQLLX"));
							scdjlx = StringHelper.formatObject(result1.get("SCDJLX"));
							if(DJLX.CFDJ.Value.equals(scdjlx)&&QLLX.QTQL.Value.equals(scqllx)){
								sealStatus = "查封办理中";
								map.put("CFZTCODE", true);
							}
							if(QLLX.DIYQ.Value.equals(scqllx)){
								mortgageStatus = "抵押办理中";
								map.put("DYZTCODE", true);
							}
							if(DJLX.YYDJ.Value.equals(scdjlx)){
								objectionStatus = "异议办理中";
								map.put("YYZTCODE", true);
							}
						}
						if(bdcdylx==BDCDYLX.H){
							//关联房屋
							for (Map result1 : resultlist) {
								String ly = StringHelper.formatObject(result1.get("LY"));
								qlid = StringHelper.formatObject(result1.get(lx));
								if("GZ".equals(ly)&&StringHelper.isEmpty(qlid)){
									continue;
								}
								ycqllx = StringHelper.formatObject(result1.get("YCQLLX"));
								ycdjlx = StringHelper.formatObject(result1.get("YCDJLX"));
								//cf diy yy
								if(DJLX.CFDJ.Value.equals(ycdjlx)&&QLLX.QTQL.Value.equals(ycqllx)){
									sealStatus += "、关联房屋已查封";
									map.put("CFZTCODE", true);
								}
								if(QLLX.DIYQ.Value.equals(ycqllx)){
									mortgageStatus += "关联房屋已抵押";
									map.put("DYZTCODE", true);
								}
								if(DJLX.YYDJ.Value.equals(ycdjlx)){
									objectionStatus += "关联房屋有异议";
									map.put("YYZTCODE", true);
								}
							}
						}
						
						map.put("DYZT", mortgageStatus);
						map.put("CFZT", sealStatus);
						map.put("YYZT", objectionStatus);
						map.put("DJZTname", HandleStatus.Name);
						map.put("DJZT", HandleStatus.Value);
					}else{
						String bdcdyid = (String) map.get("BDCDYID");
						if (bdcdyid != null) {
							String sqlHandle = MessageFormat
									.format("  from BDCK.BDCS_XMXX XMXX LEFT JOIN BDCK.BDCS_DJDY_GZ DJDY ON DJDY.XMBH=XMXX.XMBH WHERE DJDY.BDCDYID=''{0}'' AND XMXX.SFDB<>''1'' AND (XMXX.XMBH IS NOT NULL OR XMXX.XMBH<>'''')",
											bdcdyid);

							long Handle = dao.getCountByFullSql(sqlHandle);

							if (map.containsKey("DJZTname")) {
								map.remove("DJZTname");
							}
							if (map.containsKey("DJZT")) {
								map.remove("DJZT");
							}
							DJZT HandleStatus = Handle > 0 ? DJZT.DJZ : DJZT.WDJ;

							map.put("DYZT", "无抵押");
							map.put("CFZT", "无查封");
							map.put("YYZT", "无异议");
							map.put("DJZTname", HandleStatus.Name);
							map.put("DJZT", HandleStatus.Value);
						}
					}
				}
				if(map.containsKey("BDCDYID")){
					String bdcdyid = (String) map.get("BDCDYID");
					// 选择器配置
					SelectorConfig config = HandlerFactory.getSelectorByName(DCselector);
					// 来源
					DJDYLY ly = DJDYLY.initFrom(config.getLy());
					if(DJDYLY.DC.equals(ly)){
						// 不动产单元类型
//						BDCDYLX bdcdylx = BDCDYLX.initFrom(config.getBdcdylx());
						RealUnit gz = UnitTools.loadUnit(bdcdylx, DJDYLY.GZ, bdcdyid);
						RealUnit ls = UnitTools.loadUnit(bdcdylx, DJDYLY.LS, bdcdyid);
						
						DJZT DCStatus = (gz != null)? DJZT.DJZ : DJZT.WDJ;
						if(gz != null){
							DCStatus = (ls != null)? DJZT.YDJ : DJZT.DJZ;
						}
						map.put("DJZTname", DCStatus.Name);
						map.put("DJZT", DCStatus.Value);
					}
				}
			}
		}
	}

	/**
	 * 现房查封时，把期房状态带过来（目前先让泸州用着）
	 * @作者 海豹
	 * @创建时间 2015年12月22日下午4:49:40
	 * @param result
	 * @param bdcdylx
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void addYCLimitStatus(List<Map> result, BDCDYLX bdcdylx) {
		if (BDCDYLX.H.equals(bdcdylx)) {
			if (!StringHelper.isEmpty(result) && result.size() > 0) {
				for (Map map : result) {
					if (map.containsKey("BDCDYID")) {
						String bdcdyid = (String) map.get("BDCDYID");
						if (!StringHelper.isEmpty(bdcdyid)) {
							String ycxx = "select count(YCBDCDYID) BDCDYID from BDCK.YC_SC_H_XZ where SCBDCDYID IN ('" + bdcdyid + "')";
							List<Map> ycxxs = dao.getDataListByFullSql(ycxx);
							if (!StringHelper.isEmpty(ycxxs) && ycxxs.size() > 0) {
								Long l = StringHelper.getLong(ycxxs.get(0).get("BDCDYID"));
								if (l > 0) {
									String sqlMortgage = "select count(ql.QLID) QLID FROM  BDCK.BDCS_DJDY_XZ djdy left join BDCK.BDCS_QL_XZ ql on djdy.djdyid=ql.djdyid where djdy.bdcdyid in(select YCBDCDYID from BDCK.YC_SC_H_XZ where SCBDCDYID IN ('"
											+ bdcdyid + "')) and ql.qllx='23'";
									String sqlSeal = "select count(ql.QLID) QLID FROM  BDCK.BDCS_DJDY_XZ djdy left join BDCK.BDCS_QL_XZ ql on djdy.djdyid=ql.djdyid where djdy.bdcdyid in(select YCBDCDYID from BDCK.YC_SC_H_XZ where SCBDCDYID IN ('"
											+ bdcdyid + "')) and ql.qllx='99' and ql.djlx='800'";
									String sqlObjection = "select count(ql.QLID) QLID FROM  BDCK.BDCS_DJDY_XZ djdy left join BDCK.BDCS_QL_XZ ql on djdy.djdyid=ql.djdyid where djdy.bdcdyid in(select YCBDCDYID from BDCK.YC_SC_H_XZ where SCBDCDYID IN ('"
											+ bdcdyid + "'))   and ql.djlx='600'";
									List<Map> lstMortgage = dao.getDataListByFullSql(sqlMortgage);
									List<Map> lstSeal = dao.getDataListByFullSql(sqlSeal);
									List<Map> lstyy = dao.getDataListByFullSql(sqlObjection);
									long mortgageCount = 0;
									long SealCount = 0;
									;
									long ObjectionCount = 0;
									;
									if (!StringHelper.isEmpty(lstMortgage) && lstMortgage.size() > 0) {
										mortgageCount = StringHelper.getLong(lstMortgage.get(0).get("QLID"));
									}
									if (!StringHelper.isEmpty(lstSeal) && lstSeal.size() > 0) {
										SealCount = StringHelper.getLong(lstSeal.get(0).get("QLID"));
									}
									if (!StringHelper.isEmpty(lstyy) && lstyy.size() > 0) {
										ObjectionCount = StringHelper.getLong(lstyy.get(0).get("QLID"));
									}
									String mortgageStatus = mortgageCount > 0 ? "已抵押" : "无抵押";
									String sealStatus = SealCount > 0 ? "已查封" : "无查封";
									String objectionStatus = ObjectionCount > 0 ? "有异议" : "无异议";

									// 判断完现状层中的查封信息，接着判断办理中的查封信息
									if (mortgageCount <= 0) {
										String sqlMortgaging = "select count(ql.QLID) QLID FROM BDCK.BDCS_XMXX xmxx left join  BDCK.BDCS_DJDY_GZ djdy on djdy.xmbh=xmxx.xmbh left join BDCK.BDCS_QL_GZ ql on djdy.djdyid=ql.djdyid where djdy.bdcdyid in(select YCBDCDYID from BDCK.YC_SC_H_XZ where SCBDCDYID IN ('"
												+ bdcdyid + "')) and ql.qllx='23' and xmxx.sfdb='0'";
										List<Map> lstMortgaging = dao.getDataListByFullSql(sqlMortgaging);
										if (!StringHelper.isEmpty(lstMortgaging) && lstMortgaging.size() > 0) {
											mortgageCount = StringHelper.getLong(lstMortgaging.get(0).get("QLID"));
											mortgageStatus = mortgageCount > 0 ? "抵押办理中" : "无抵押";
										}
									}
									if (SealCount <= 0) {
										String sqlSealing = "select count(ql.QLID) QLID FROM  BDCK.BDCS_XMXX xmxx left join  BDCK.BDCS_DJDY_GZ djdy on djdy.xmbh=xmxx.xmbh left join BDCK.BDCS_QL_GZ ql on djdy.djdyid=ql.djdyid where djdy.bdcdyid in(select YCBDCDYID from BDCK.YC_SC_H_XZ where SCBDCDYID IN ('"
												+ bdcdyid + "')) and ql.qllx='99' and ql.djlx='800' and xmxx.sfdb='0'";
										List<Map> lstSealing = dao.getDataListByFullSql(sqlSealing);
										if (!StringHelper.isEmpty(lstSealing) && lstSealing.size() > 0) {
											SealCount = StringHelper.getLong(lstSealing.get(0).get("QLID"));
											sealStatus = SealCount > 0 ? "查封办理中" : "无查封";
										}
									}
									if (ObjectionCount <= 0) {
										String sqlObjecting = "select count(ql.QLID) QLID FROM  BDCK.BDCS_XMXX xmxx left join  BDCK.BDCS_DJDY_GZ djdy on djdy.xmbh=xmxx.xmbh left join BDCK.BDCS_QL_GZ ql on djdy.djdyid=ql.djdyid where djdy.bdcdyid in(select YCBDCDYID from BDCK.YC_SC_H_XZ where SCBDCDYID IN ('"
												+ bdcdyid + "'))   and ql.djlx='600' and xmxx.sfdb='0'";
										List<Map> lstyying = dao.getDataListByFullSql(sqlObjecting);
										if (!StringHelper.isEmpty(lstyying) && lstyying.size() > 0) {
											ObjectionCount = StringHelper.getLong(lstyying.get(0).get("QLID"));
											objectionStatus = ObjectionCount > 0 ? "异议办理中" : "无异议";
										}
									}
									map.put("YCDYZT", mortgageStatus);
									map.put("YCCFZT", sealStatus);
									map.put("YCYYZT", objectionStatus);
								} else {
									map.put("YCDYZT", "");
									map.put("YCCFZT", "");
									map.put("YCYYZT", "");
								}
							}

						}
					}
				}
			}
		}
	}
	

	/**
	 * 内部方法：合并多个权利人为一个权利人对象，权利人名称用逗号分割
	 * @Title: getUnionRightsHolder
	 * @author:liushufeng
	 * @date：2015年7月16日 下午8:28:34
	 * @param qlid
	 * @return
	 */
	private RightsHolder getUnionRightsHolder(String qlid) {
		RightsHolder holder = new BDCS_QLR_XZ();
		List<RightsHolder> holders = RightsHolderTools.loadRightsHolders(DJDYLY.XZ, qlid);
		if (holders != null && holders.size() > 0) {
			for (RightsHolder _holder : holders) {
				holder.setQLRMC(StringHelper.isEmpty(holder.getQLRMC()) ? _holder.getQLRMC() : holder.getQLRMC() + "," + _holder.getQLRMC());
				holder.setDLRXM(StringHelper.isEmpty(holder.getDLRXM()) ? _holder.getDLRXM() : holder.getDLRXM() + "," + _holder.getDLRXM());
				holder.setDH(StringHelper.isEmpty(holder.getDH()) ? _holder.getDH() : holder.getDH() + "," + _holder.getDH());
			}
		}
		return holder;
	}

	/**
	 * 内部方法：获取表的所有字段字符串
	 * @Title: getTableFieldsName
	 * @author:liushufeng
	 * @date：2015年7月16日 下午8:31:26
	 * @param tableName
	 * @param prefix
	 * @return
	 * @throws ClassNotFoundException
	 */
	private String getTableFieldsName(String tableName, String prefix) throws ClassNotFoundException {
		String str = "";
		if (!entityfieldnames.containsKey(tableName.toUpperCase())) {
			StringBuilder builder = new StringBuilder();
			Map<String,String> map = new HashMap<String,String>();
			Class<?> t = Class.forName("com.supermap.realestate.registration.model." + tableName.toUpperCase());
			Method[] mds = t.getDeclaredMethods();
			for (Method md : mds) {
				Column c = md.getAnnotation(Column.class);
				if (c != null&&!"hjson".equalsIgnoreCase(c.name())) {
					//如果包含相同字段，跳出本次循环
					if (map.containsKey(c.name())) {
						continue;
					}
					if (md.getReturnType() != null && md.getReturnType().getName() != null) {
						if (md.getReturnType().getName().toUpperCase().contains("[LJAVA.LANG.BYTE")) {
							continue;
						}
					}
					if (c.name().toUpperCase().equals("ZSEWM") || c.name().toUpperCase().equals("FCFHT") || c.name().toUpperCase().equals("FCFHTWJ")) {
						continue;
					} else if (c.name().toUpperCase().equals("CFSJ")) {
						builder.append("to_char(" + prefix + "." + c.name() + ",'yyyy-MM-dd') as " + prefix + "_" + c.name() + ",");
					} else {
						if("QL".equals(prefix)&&c.name().toUpperCase().equals("BDCDYID")){
							continue;
						}
						builder.append(prefix + "." + c.name() + " as " + prefix + "_" + c.name() + ",");
					}
					map.put(c.name(), c.name());
				}
			}
			str = builder.toString();
			str = str.substring(0, str.length() - 1);
			entityfieldnames.put(tableName.toUpperCase(), str);
		}
		str = entityfieldnames.get(tableName.toUpperCase());
		return str;
	}

	/**
	 * 内部方法：获取表的所有字段字符串，不加前缀的
	 * @Title: getTableFieldsName2
	 * @author:liushufeng
	 * @date：2015年8月28日 下午4:16:43
	 * @param tableName
	 * @return
	 * @throws ClassNotFoundException
	 */
	private String getTableFieldsName2(String tableName) throws ClassNotFoundException {
		String str = "";
		StringBuilder builder = new StringBuilder();
		Map<String,String> map = new HashMap<String,String>();
		Class<?> t = Class.forName("com.supermap.realestate.registration.model." + tableName.toUpperCase());
		Method[] mds = t.getDeclaredMethods();
		for (Method md : mds) {
			Column c = md.getAnnotation(Column.class);
			if (c != null) {
				//如果包含相同字段，跳出本次循环
				if (map.containsKey(c.name())) {
					continue;
				}
				if (c.name().toUpperCase().equals("FCFHTWJ") || c.name().toUpperCase().equals("FCFHT") || c.name().toUpperCase().equals("FCFHTWJ")||c.name().toUpperCase().equals("GLZRZID")) {
					continue;
				} else {
					builder.append(c.name() + ",");
				}
				map.put(c.name(), c.name());
			}
		}
		str = builder.toString();
		str = str.substring(0, str.length() - 1);

		return str;
	}
	

	/**
	 * @Title: setColorByExistGz
	 * @Description: 检查返回列表中单元是否正在办理其他业务
	 * @Author：赵梦帆
	 * @Data：2016年10月24日 下午5:34:36
	 * @param result
	 * @return void
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void setColorByExistGz(List<Map> result) {
			if (result != null && result.size() > 0) {
				for (Map<String, String> map : result) {
					if (map.containsKey("BDCDYID")) {
						String bdcdyid = (String) map.get("BDCDYID");
						if (bdcdyid != null) {
							long count = 0;
							if(bdcdyid!=null)
								count  = dao.getCountByFullSql(" FROM BDCK.BDCS_XMXX XMXX "
										+ "INNER JOIN BDCK.BDCS_DJDY_GZ DY ON DY.XMBH=XMXX.XMBH "
										+ " AND (XMXX.SFDB IS NULL OR XMXX.SFDB<>'1') AND BDCDYID='"+ bdcdyid +"'");
							if(count>0)
								map.put("ISEXIST", "1");
							else
								map.put("ISEXIST", "0");
						}
					}
				}
			}
		}
	
	/**
	 * 使用权宗地抵押变更选择器-变更单元个数的，输入一个证号，汇总这个他项证号下边的单元数，总面积等
	 * @Title: queryDYData
	 * @author:liangcheng
	 * @date：2017年10月12日 上午11:18:30
	 * @param request
	 * @param selector
	 * @return
	 * @throws ClassNotFoundException
	 * @throws UnsupportedEncodingException
	 */
	@SuppressWarnings("rawtypes")
	private Message queryZDDYData(HttpServletRequest request, String selector) throws ClassNotFoundException, UnsupportedEncodingException {
		Message msg = new Message();
		// 获取page和rows参数
		Integer page = RequestHelper.getPage(request);
		Integer rows = RequestHelper.getRows(request);
		// 选择器配置
		SelectorConfig config = HandlerFactory.getSelectorByName(selector);
		// 查询结果列表
		List<Map> listresult = null;
		String qzhcxms = ConfigHelper.getNameByValue("QZHCXMS");
		if(StringHelper.isEmpty(qzhcxms)){
			qzhcxms="/";
		}

		StringBuilder builder = new StringBuilder();
		builder.append("SELECT QL.BDCQZH TXQZH,");
		builder.append("COUNT(*) DYDYGS,");
		builder.append("SUM(ZD.ZDMJ) DYMJ ");
		builder.append("FROM BDCK.BDCS_QLR_XZ QLR ");
		builder.append("LEFT JOIN BDCK.BDCS_QL_XZ QL ON QL.QLID=QLR.QLID AND QL.QLLX='23' ");
		builder.append("LEFT JOIN BDCK.BDCS_DJDY_XZ DJDY ON DJDY.DJDYID=QL.DJDYID ");
		builder.append("LEFT JOIN ");
		builder.append("(SELECT BDCDYID,ZDMJ  FROM BDCK.BDCS_SHYQZD_XZ UNION ALL SELECT BDCDYID,ZDMJ FROM BDCK.BDCS_SYQZD_XZ)  ZD ON ZD.BDCDYID=DJDY.BDCDYID ");
		builder.append("WHERE  QL.QLLX='23' ");

		StringBuilder builder2 = new StringBuilder();
		builder2.append("FROM (SELECT  QL.BDCQZH ");
		builder2.append("FROM BDCK.BDCS_QLR_XZ QLR  ");
		builder2.append("LEFT JOIN BDCK.BDCS_QL_XZ QL ON QL.QLID=QLR.QLID AND QL.QLLX='23' ");
		builder2.append("LEFT JOIN BDCK.BDCS_DJDY_XZ DJDY ON DJDY.DJDYID=QL.DJDYID ");
		builder2.append("LEFT JOIN (SELECT BDCDYID,ZDMJ FROM BDCK.BDCS_SHYQZD_XZ UNION ALL SELECT BDCDYID,ZDMJ FROM BDCK.BDCS_SYQZD_XZ)  ZD ON ZD.BDCDYID=DJDY.BDCDYID ");
		builder2.append("WHERE QL.QLLX='23' ");

		String XZQHDM=ConfigHelper.getNameByValue("XZQHDM");
		for (int i = 0; i < config.getQueryconfig().getFields().size(); i++) {
			String name = config.getQueryconfig().getFields().get(i).getFieldname();
			String entityname = config.getQueryconfig().getFields().get(i).getEntityname();
			String value = RequestHelper.getParam(request, name);
			if (!StringHelper.isEmpty(value)) {
				if(value.indexOf(qzhcxms) == -1){
					if(XZQHDM.indexOf("45")==0){//广西需求，他项权证号支持模糊查询
						builder.append(" AND ").append(entityname).append(".BDCQZH like '%").append(value).append("%'");
						builder2.append(" AND ").append(entityname).append(".BDCQZH like '%").append(value).append("%'");
					}else{
						builder.append(" AND ").append(entityname).append(".").append(name).append(" ='").append(value).append("'");
						builder2.append(" AND ").append(entityname).append(".").append(name).append(" ='").append(value).append("'");
					}
					
				}else if(("BDCQZHXH").equals(name.toUpperCase())){
					String[] list = value.split(qzhcxms);
					String newvalue="";
					for (String string : list) {
						newvalue+="'" + string + "',";
					}
					newvalue = newvalue.substring(0, newvalue.length()-1);
					builder.append(" AND ").append(entityname).append(".").append(name).append(" IN (").append(newvalue).append(")");
					builder2.append(" AND ").append(entityname).append(".").append(name).append(" IN (").append(newvalue).append(")");
				}
			}
		}

		builder.append(" GROUP BY QL.BDCQZH ");
		builder2.append(" GROUP BY QL.BDCQZH) ");
		

		listresult = dao.getPageDataByFullSql(builder.toString(), page, rows);
		long count=dao.getCountByFullSql(builder2.toString());
		addZDDYExternalInfo(listresult);
		setColorByExistGz(listresult);
		msg.setTotal(count);
		msg.setRows(listresult);
		return msg;
	}
	
	private void addZDDYExternalInfo(List<Map> result) {

		StringBuilder builder = new StringBuilder();
		builder.append("SELECT ZD.ZL,ZD.BDCDYH,QLR.QLRMC,FSQL.DYR FROM BDCK.BDCS_QL_XZ QL ");
		builder.append("LEFT JOIN BDCK.BDCS_FSQL_XZ FSQL ON QL.QLID=FSQL.QLID ");
		builder.append("LEFT JOIN BDCK.BDCS_QLR_XZ QLR ON QL.QLID=QLR.QLID ");
		builder.append("LEFT JOIN BDCK.BDCS_DJDY_XZ DJDY ON DJDY.DJDYID=QL.DJDYID ");
		builder.append("LEFT JOIN  (SELECT ZL,BDCDYH,BDCDYID,ZDMJ FROM BDCK.BDCS_SHYQZD_XZ UNION ALL SELECT ZL,BDCDYH,BDCDYID,ZDMJ FROM BDCK.BDCS_SYQZD_XZ) ZD ");
		builder.append("ON ZD.BDCDYID=DJDY.BDCDYID ");
		builder.append("WHERE  QL.QLLX='23' AND  QL.BDCQZH=''{0}'' AND ROWNUM<2  ");
		String sql = builder.toString();
		if (result != null && result.size() > 0) {
			for (Map map : result) {
				if (map.containsKey("TXQZH")) {
					String txqzh = (String) map.get("TXQZH");
					String tmpsql = MessageFormat.format(sql, txqzh);
					List<Map> tempmap = dao.getDataListByFullSql(tmpsql);
					if (tempmap != null && tempmap.size() > 0) {
						String zl = tempmap.get(0) == null ? "" : tempmap.get(0).get("ZL") == null ? "" : tempmap.get(0).get("ZL").toString() + "(等)";
						String dyqr = tempmap.get(0) == null ? "" : tempmap.get(0).get("QLRMC") == null ? "" : tempmap.get(0).get("QLRMC").toString();
						String dyr = tempmap.get(0) == null ? "" : tempmap.get(0).get("DYR") == null ? "" : tempmap.get(0).get("DYR").toString();
						String bdcdyh = tempmap.get(0) == null ? "" : tempmap.get(0).get("BDCDYH") == null ? "" : tempmap.get(0).get("BDCDYH").toString() + "(等)";
						map.put("ZL", zl);
						map.put("BDCDYH", bdcdyh);
						map.put("DYQR", dyqr);
						map.put("DYR", dyr);
					}
				}
			}
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/queryindex/{selector}/queryindexforrunonce/", method = RequestMethod.GET)
	public @ResponseBody Message queryDataForRunOnce(@PathVariable("selector") String selector, HttpServletRequest request) throws ClassNotFoundException, UnsupportedEncodingException {
		double starttime = System.currentTimeMillis();
		DCselector = selector;
		if (selector.equals("YCFSelector")) {
			return this.queryYCFData(request);
		}
		if ("FWCFSelector".equals(selector) || "ZDCFSelector".equals(selector) || "YCFWCFSelector".equals(selector)|| "ZHCFSelector".equals(selector)) { // 解封_房屋的、解封地
			return this.queryCFDJData(request, selector);
		}
		if ("DYBGSelector".equals(selector)) {
			return this.queryDYData(request, selector);
		}
		if ("DYBG_GYJSYDSelector".equals(selector)||"DYBG_JTTDSelector".equals(selector)||"DYBG_ZJDSelector".equals(selector)) {
			return this.queryZDDYData(request, selector);
		}
		String qzhcxms = ConfigHelper.getNameByValue("QZHCXMS");
		if(StringHelper.isEmpty(qzhcxms)){
			qzhcxms="/";
		}
		// 返回的结果对象
		Message msg = new Message();
		// 获取page和rows参数
		Integer page = RequestHelper.getPage(request);//
		Integer rows = RequestHelper.getRows(request);
		// 选择器配置
		SelectorConfig config = HandlerFactory.getSelectorByName(selector);
		// 查询结果列表
		List<Map> listresult = null;
		// 不动产单元类型
		BDCDYLX bdcdylx = BDCDYLX.initFrom(config.getBdcdylx());
		// 来源
		DJDYLY ly = DJDYLY.initFrom(config.getLy());
		// 单元实体名
		String unitentityName = bdcdylx.getTableName(ly);
		// 查询结果记录总数
		long count = 0;
		// 选择单元
		if (config.isSelectbdcdy()) {
			String fullentityname = "com.supermap.realestate.registration.model." + unitentityName;
			Class<?> T = Class.forName(fullentityname);
			javax.persistence.Table tableanno = T.getAnnotation(javax.persistence.Table.class);
			String tablename = tableanno.schema() + "." + tableanno.name();

			String dyfieldsname = getTableFieldsName2(unitentityName);

			String sql = "from " + tablename + " dy where 1>0 ";
			if (config.isUseconfigsql()) {
				sql = " from (" + config.getConfigsql();
			}
			for (int i = 0; i < config.getQueryconfig().getFields().size(); i++) {
				String name = config.getQueryconfig().getFields().get(i).getFieldname();
				String value = RequestHelper.getParam(request, name);
				if (!StringHelper.isEmpty(value)) {
					String _cond = "";
					if(("BDCQZH").equals(name.toUpperCase())){}else{
						_cond = " and " + config.getQueryconfig().getFields().get(i).getEntityname() + "." + name + " = to_single_byte(" + value + ")' ";
					}if (("ZL").equals(name.toUpperCase()) || ("BDCDYH").equals(name.toUpperCase()) || !SF.YES.Value.equals(ConfigHelper.getNameByValue("PreciseQuery"))) {
						_cond = " and " + config.getQueryconfig().getFields().get(i).getEntityname() + "." + name + " like '%" + value + "%' ";
					} else {
						_cond = " and " + config.getQueryconfig().getFields().get(i).getEntityname() + "." + name + " = '" + value + "' ";
					}
					sql = sql + _cond;
				}
			}
			if (!StringHelper.isEmpty(config.getCondition())) {
				sql += " " + config.getCondition();
			}
			if (config.isUseconfigsql()) {
				sql += ")";
			}
			count = dao.getCountByFullSql(sql);
			String orderby = " ";
			if (config.getSortfields() != null && config.getSortfields().size() > 0) {
				orderby = " ORDER BY ";
				for (SortField sortfield : config.getSortfields()) {
					if (!StringHelper.isEmpty(sortfield.getEntityname())) {
						orderby = orderby + sortfield.getEntityname() + "." + sortfield.getFieldname() + " " + sortfield.getSorttype() + ",";
					} else {
						orderby = orderby + sortfield.getFieldname() + " " + sortfield.getSorttype() + ",";
					}
				}
				if (orderby.endsWith(",")) {
					orderby = orderby.substring(0, orderby.length() - 1);
				}
			}
			String fullsql = "select " + dyfieldsname + " " + sql + orderby;
			if (config.isUseconfigsql()) {
				String ssql = "select * " + sql + orderby;
				listresult = dao.getPageDataByFullSql(ssql, page, rows);
			} else {
				listresult = dao.getPageDataByFullSql(fullsql, page, rows);
			}
			addLimitStatus(listresult, bdcdylx);
		}// 选择权利
		else if (config.isSelectql()) {
			String fullSql = "";
			String fromSql = "";
			if (!config.isUseconfigsql()) {
				/* 实体对应的表名(前面加用户名.) */
				unitentityName = "BDCK." + bdcdylx.getTableName(DJDYLY.XZ);
				/* 表名+'_'+字段名 */
				String dyfieldsname = getTableFieldsName(bdcdylx.getTableName(DJDYLY.XZ), "DY");
				String qlfieldsname = getTableFieldsName("BDCS_QL_XZ", "QL");
				String fsqlfieldsname = getTableFieldsName("BDCS_FSQL_XZ", "FSQL");
				String zddyfieldsname = " ZDDY.ZL AS ZDDY_ZDZL,ZDDY.BDCDYH AS ZDDY_ZDBDCDYH ";
				String zrzdyfieldsname = " ZRZDY.ZL AS ZRZDY_ZRZZL,ZRZDY.BDCDYH AS ZRZDY_ZRZBDCDYH,ZRZDY.ZRZH AS ZRZDY_ZRZH ";

				// 先构造查询字段
				// 再构造出from后边，where前边的表
				// 再构造where条件

				/* SELECT 字段部分 */
				StringBuilder builder2 = new StringBuilder();
				builder2.append("select ").append(dyfieldsname).append(",").append(qlfieldsname).append(",").append(fsqlfieldsname);
				String selectstr = builder2.toString();

				/* FROM 后边的表语句 */
				/* 不跟权利人表做连接了 */
				StringBuilder builder = new StringBuilder();
				builder.append(" from {0} DY").append(" left join BDCK.BDCS_DJDY_XZ DJDY on dy.bdcdyid=djdy.bdcdyid")
						.append(" left join BDCK.bdcs_ql_xz ql on ql.djdyid=djdy.djdyid").append(" LEFT JOIN BDCK.BDCS_FSQL_XZ FSQL ON FSQL.QLID=QL.QLID ");
				
				StringBuilder builder4 = new StringBuilder();
				if(config.getName().equals("XZDJ_FWSYQSelector")||config.getName().equals("XZDJ_JTTDFWSYQSelector")||config.getName().equals("XZDJ_ZJDFWSYQSelector")){
					builder.append(" left join bdck.bdcs_shyqzd_xz zddy on dy.zdbdcdyid=zddy.bdcdyid left join bdck.bdcs_zrz_xz zrzdy on dy.zrzbdcdyid=zrzdy.bdcdyid ");
					builder4.append(",").append(zddyfieldsname).append(",").append(zrzdyfieldsname);
					selectstr += builder4.toString();
				}
				/* WHERE 条件语句 */
				StringBuilder builder3 = new StringBuilder();
				builder3.append(" where 1>0");
				if (!StringHelper.isEmpty(config.getCondition())) {
					builder3.append(config.getCondition());
				}
				
				for (int i = 0; i < config.getQueryconfig().getFields().size(); i++) {
					String name = config.getQueryconfig().getFields().get(i).getFieldname();
					String value = RequestHelper.getParam(request, name);
					if (!StringHelper.isEmpty(value)) {
						String entname = config.getQueryconfig().getFields().get(i).getEntityname();
						if (!StringHelper.isEmpty(entname)) {
							// 如果包括权利人相关的查询，再连接权利人表
							if (entname.toUpperCase().equals("QLR")) {
								if(value.indexOf(qzhcxms) == -1){
									if (("ZL").equals(name.toUpperCase()) || ("BDCDYH").equals(name.toUpperCase()) || !SF.YES.Value.equals(ConfigHelper.getNameByValue("PreciseQuery"))) {
										builder3.append(" and QL.QLID IN (SELECT QLID FROM BDCK.BDCS_QLR_XZ  WHERE  INSTR(" + name + ",'" + value + "')>0) ");
									} else {
										builder3.append(" and QL.QLID IN (SELECT QLID FROM BDCK.BDCS_QLR_XZ  WHERE  " + name + " = '" + value + "') ");
									}
								}else if(("BDCQZH").equals(name.toUpperCase())){
									String[] list = value.split(qzhcxms);
									String newvalue="";
									for (String string : list) {
										newvalue+="'" + string + "',";
									}
									newvalue = newvalue.substring(0, newvalue.length()-1);
									builder3.append(" and QL.QLID IN (SELECT QLID FROM BDCK.BDCS_QLR_XZ  WHERE  " + name + " IN (" + newvalue + ")) ");
								}
							}else if(entname.toUpperCase().equals("ZDDY")||entname.toUpperCase().equals("ZRZDY")){
								if(("ZDZL").equals(name.toUpperCase())){
									builder3.append(" and  exists(select 1 from bdck.bdcs_shyqzd_xz where zl like'%"+value+"%' and bdcdyid=dy.zdbdcdyid) ");
								}else if(("ZDBDCDYH").equals(name.toUpperCase())){
									builder3.append(" and  exists(select 1 from bdck.bdcs_shyqzd_xz where bdcdyh like '%"+value+"%' and bdcdyid=dy.zdbdcdyid) ");
								}else if(("ZRZH").equals(name.toUpperCase())){
									builder3.append(" and  exists(select 1 from bdck.bdcs_zrz_xz where zrzh like'%"+value+"%' and bdcdyid=dy.zrzbdcdyid) ");
								}else if(("ZRZZL").equals(name.toUpperCase())){
									builder3.append(" and  exists(select 1 from bdck.bdcs_zrz_xz where zl like'%"+value+"%' and bdcdyid=dy.zrzbdcdyid) ");
								}else if(("ZRZBDCDYH").equals(name.toUpperCase())){
									builder3.append(" and  exists(select 1 from bdck.bdcs_zrz_xz where bdcdyh like'%"+value+"%' and bdcdyid=dy.zrzbdcdyid) ");
								}else{
									builder3.append(" ");
								}
							}else {
								if(value.indexOf(qzhcxms) == -1){
									if (("ZL").equals(name.toUpperCase()) || ("BDCDYH").equals(name.toUpperCase()) || !SF.YES.Value.equals(ConfigHelper.getNameByValue("PreciseQuery"))) {
										builder3.append(" and instr(" + config.getQueryconfig().getFields().get(i).getEntityname() + "." + name + ",'" + value + "')>0 ");
										 } else {
										builder3.append(" and " + config.getQueryconfig().getFields().get(i).getEntityname() + "." + name + " = '" + value + "' ");
									}
								}else if(("BDCQZH").equals(name.toUpperCase())){
									String[] list = value.split(qzhcxms);
									String newvalue="";
									for (String string : list) {
										newvalue+="'" + string + "',";
									}
									newvalue = newvalue.substring(0, newvalue.length()-1);
									builder3.append(" and QL.QLID IN (SELECT QLID FROM BDCK.BDCS_QLR_XZ  WHERE  " + name + " IN (" + newvalue + ")) ");
								}
							}
						}
					}
				}
				
				String fromstr = builder.toString();
				fromstr = MessageFormat.format(fromstr, unitentityName);

				builder3.append(" and ql.qllx='" + config.getSelectqllx() + "'");
				String wherestr = builder3.toString();

				fromSql = " from (select ql.qlid " + fromstr + wherestr + ")";
				fullSql = selectstr + fromstr + wherestr;
			} else {
				String configsql = config.getConfigsql();
				for (int i = 0; i < config.getQueryconfig().getFields().size(); i++) {
					String name = config.getQueryconfig().getFields().get(i).getFieldname();
					String value = RequestHelper.getParam(request, name);
					if (!StringHelper.isEmpty(value)) {
						String entname = config.getQueryconfig().getFields().get(i).getEntityname();
						if (!StringHelper.isEmpty(entname)) {
							// 如果包括权利人相关的查询，再连接权利人表
							if (entname.toUpperCase().equals("QLR")) {
								if(value.indexOf(qzhcxms) == -1){
									if (("ZL").equals(name.toUpperCase()) || ("BDCDYH").equals(name.toUpperCase()) || !SF.YES.Value.equals(ConfigHelper.getNameByValue("PreciseQuery"))) {
										configsql += " and QL.QLID IN (SELECT QLID FROM BDCK.BDCS_QLR_XZ  WHERE  INSTR(" + name + ",'" + value + "')>0) ";
									} else {
										configsql += " and QL.QLID IN (SELECT QLID FROM BDCK.BDCS_QLR_XZ  WHERE  " + name + " = '" + value + "') ";
									}
								}else if(("BDCQZH").equals(name.toUpperCase())){
									String[] list = value.split(qzhcxms);
									String newvalue="";
									for (String string : list) {
										newvalue+="'" + string + "',";
									}
									newvalue = newvalue.substring(0, newvalue.length()-1);
									configsql +=" and QL.QLID IN (SELECT QLID FROM BDCK.BDCS_QLR_XZ  WHERE  " + name + " IN (" + newvalue + ")) ";
								}
							} else {
								if(value.indexOf(qzhcxms) == -1){
									if (("ZL").equals(name.toUpperCase()) || ("BDCDYH").equals(name.toUpperCase()) || !SF.YES.Value.equals(ConfigHelper.getNameByValue("PreciseQuery"))) {
										configsql += " and instr(" + config.getQueryconfig().getFields().get(i).getEntityname() + "." + name + ",'" + value + "')>0 ";
									} else {
										configsql += " and " + config.getQueryconfig().getFields().get(i).getEntityname() + "." + name + " = '" + value + "' ";
									}
								}else if(("BDCQZH").equals(name.toUpperCase())){
									String[] list = value.split(qzhcxms);
									String newvalue="";
									for (String string : list) {
										newvalue+="'" + string + "',";
									}
									newvalue = newvalue.substring(0, newvalue.length()-1);
									configsql +=" and QL.QLID IN (SELECT QLID FROM BDCK.BDCS_QLR_XZ  WHERE  " + name + " IN (" + newvalue + ")) ";
								}
							}
						}
					}
				}
				fromSql = "from (" + configsql + ")";
				fullSql = configsql;
			}
			count = dao.getCountByFullSql(fromSql);
			if (count > 0) {
				String orderby = " ";
				if (config.getSortfields() != null && config.getSortfields().size() > 0) {
					orderby = " ORDER BY ";
					for (SortField sortfield : config.getSortfields()) {
						orderby = orderby + sortfield.getEntityname() + "." + sortfield.getFieldname() + " " + sortfield.getSorttype() + ",";
					}
					if (orderby.endsWith(",")) {
						orderby = orderby.substring(0, orderby.length() - 1);
					}
				}
				fullSql = fullSql + orderby;
				listresult = dao.getPageDataByFullSql(fullSql, page, rows);
				listresult = revmoeprefix(listresult);
				addRightsHolderInfo(listresult);
				addLimitStatus(listresult, bdcdylx);
				if (ConfigHelper.getNameByValue("XZQHDM").startsWith("510")) {
					addYCLimitStatus(listresult, bdcdylx);
				}
			}
		}
		// 格式化结果中的常量值
		if (count > 0) {
			if (config.getResultconfig() != null && config.getResultconfig().getConstmappings() != null && config.getResultconfig().getConstmappings().size() > 0) {
				List<ConstMapping> cmapping = config.getResultconfig().getConstmappings();
				for (ConstMapping _mapping : cmapping) {
					for (Map map : listresult) {
						if (map.containsKey(_mapping.getFieldname())) {
							String value = StringHelper.formatObject(StringHelper.isEmpty(map.get(_mapping.getFieldname())) ? _mapping.getDefaultvalue() : map.get(_mapping
									.getFieldname()));
							if (_mapping.isNewfieldendwithname()) {
								map.put(_mapping.getFieldname() + "name", ConstHelper.getNameByValue(_mapping.getConsttype(), value));
							} else {
								// TODO @刘树峰 ，设置Map中的某一个键的值
							}
						}
					}
				}
			}
		}
		// 追加额外列
		List<GridColumn> Columns = config.getGridconfig().getColumns();
		for (GridColumn gridColumn : Columns) {
			if("SYQBDCQZH".equals(gridColumn.getFieldname())){
				for (Map result : listresult) {
					String qlid = result.get("QLID").toString();
					if(!StringHelper.isEmpty(qlid)){
						BDCS_QL_XZ ql_xz = dao.get(BDCS_QL_XZ.class, qlid);
						String djdyid = ql_xz.getDJDYID();
						 List<BDCS_QL_XZ> lyql_xz = dao.getDataList(BDCS_QL_XZ.class, " DJDYID='"+ djdyid+ "' AND QLLX='4'");
						if(lyql_xz!=null&&lyql_xz.size()>0){
							BDCS_QL_XZ ly_xz = lyql_xz.get(0);
							result.put("SYQBDCQZH", ly_xz.getBDCQZH());
						}
						if("1".equals(ql_xz.getISPARTIAL())){
							List<BDCS_PARTIALLIMIT> partql = dao.getDataList(BDCS_PARTIALLIMIT.class, " LIMITQLID='"+ qlid +"' AND LIMITTYPE='"+ config.getSelectqllx() +"'");
							if(partql!=null&&partql.size()>0){
								String qlrid = partql.get(0).getQLRID();
								BDCS_QLR_XZ qlr = dao.get(BDCS_QLR_XZ.class, qlrid);
								if(qlr!=null){
									result.put("SYQBDCQZH", qlr.getBDCQZH());
								}else{
									result.put("SYQBDCQZH", "");
								}
							}
						}
					}
				}
				break;
			}
		}
		setColorByExistGz(listresult);
		msg.setTotal(count);
		msg.setRows(listresult);
		System.out.println(System.currentTimeMillis() - starttime);
		return msg;
	}
}