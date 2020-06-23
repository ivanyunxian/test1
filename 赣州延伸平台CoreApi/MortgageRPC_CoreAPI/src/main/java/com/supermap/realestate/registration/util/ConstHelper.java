package com.supermap.realestate.registration.util;

import com.supermap.realestate.registration.model.BDCS_CONST;
import com.supermap.wisdombusiness.core.SuperSpringContext;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 
 * @Description:获取字典表信息
 * @author 刘树峰
 * @date 2015年6月12日 上午11:53:14
 * @Copyright SuperMap
 */
public class ConstHelper {

	private static CommonDao dao;

	static {
		if (dao == null) {
			dao = SuperSpringContext.getContext().getBean(CommonDao.class);
		}
	}

	/**
	 * 字典缓存
	 */
	@SuppressWarnings("rawtypes")
	private static final Map<String, Map> typeMap = new HashMap<String, Map>();
	
	@SuppressWarnings("rawtypes")
	private static final Map<String, Map> typeMap_new = new HashMap<String, Map>();
	
	/**
	 * 字典缓存（上报）
	 */
	@SuppressWarnings("rawtypes")
	private static final Map<String, Map> reportMap = new HashMap<String, Map>();

	/**
	 * 根据常量分类名称获取常量列表
	 * @Title: getConstByType
	 * @author:liushufeng
	 * @date：2015年7月12日 下午10:11:56
	 * @param typeName
	 *            常量分类名称
	 * @return 常量列表
	 */
	@SuppressWarnings("unchecked")
	private static List<BDCS_CONST> getConstByType(String typeName) {
		
		List<BDCS_CONST> consts = new ArrayList<BDCS_CONST>();
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("SELECT * FROM bdck.BDCS_CONST T WHERE CONSTSLSID =");
		sqlBuilder.append(" (SELECT cONSTSLSID FROM bdck.BDCS_CONSTCLS WHERE CONSTCLSTYPE = '"+typeName+"') order by CONSTORDER");
		List<Map> mapList = dao.getDataListByFullSql(sqlBuilder.toString());
		for(Map map : mapList){
			BDCS_CONST _const = new BDCS_CONST();
			_const.setCONSTVALUE(StringHelper.formatObject(map.get("CONSTVALUE")));
			_const.setCONSTTRANS(StringHelper.formatObject(map.get("CONSTTRANS")));
			String parentNode = StringHelper.formatObject(map.get("PARENTNODE"));
			if(!parentNode.equals("")){
				_const.setPARENTNODE(new Integer(StringHelper.formatObject(map.get("PARENTNODE"))));
			}
			String constorder = StringHelper.formatObject(map.get("CONSTORDER"));
			if(!constorder.equals("")){
				_const.setCONSTORDER(new Integer(StringHelper.formatObject(map.get("CONSTORDER"))));
			}
			_const.setREPORTVALUE(StringHelper.formatObject(map.get("REPORTVALUE")));
			consts.add(_const);
		}
		if (consts.size() > 0) {
			return consts;
		} else {
			return new ArrayList<BDCS_CONST>();
		}
	}
	
	@SuppressWarnings("unchecked")
	private static List<BDCS_CONST> getConstByType_new() {
		List<BDCS_CONST> consts = new ArrayList<BDCS_CONST>();
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("SELECT * FROM bdck.BDCS_CONST T WHERE CONSTSLSID in('8','54')");
		sqlBuilder.append(" order by SFSY desc, CONSTORDER asc");
		List<Map> mapList = dao.getDataListByFullSql(sqlBuilder.toString());
		for(Map map : mapList){
			BDCS_CONST _const = new BDCS_CONST();
			_const.setCONSTVALUE(StringHelper.formatObject(map.get("CONSTVALUE")));
			_const.setCONSTTRANS(StringHelper.formatObject(map.get("CONSTTRANS")));
			String parentNode = StringHelper.formatObject(map.get("PARENTNODE"));
			if(!parentNode.equals("")){
				_const.setPARENTNODE(new Integer(StringHelper.formatObject(map.get("PARENTNODE"))));
			}
			String constorder = StringHelper.formatObject(map.get("CONSTORDER"));
			if(!constorder.equals("")){
				_const.setCONSTORDER(new Integer(StringHelper.formatObject(map.get("CONSTORDER"))));
			}
			_const.setREPORTVALUE(StringHelper.formatObject(map.get("REPORTVALUE")));
			consts.add(_const);
		}
		if (consts.size() > 0) {
			return consts;
		} else {
			return new ArrayList<BDCS_CONST>();
		}
	}
	
	/**
	 * 根据类型值和value获取字典的name值
	 * 
	 * @作者 刘树峰
	 * @创建时间 2015年6月10日下午3:22:23
	 * @param consttype
	 * @param constvalue
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static String getNameByValue(String consttype, String constvalue) {
		String name = "";
		if (typeMap.containsKey(consttype)) {
			Map<String, String> innermap = typeMap.get(consttype);
			if (innermap.containsKey(constvalue)) {
				name = innermap.get(constvalue);
			}
		} else {
			LinkedHashMap<String, String> innermap = new LinkedHashMap<String, String>();
			LinkedHashMap<String, String> innermap_report = new LinkedHashMap<String, String>();
			List<BDCS_CONST> list = getConstByType(consttype);
			if (list != null) {
				for (int i = 0; i < list.size(); i++) {
					innermap.put(list.get(i).getCONSTVALUE(), list.get(i).getCONSTTRANS());
					String reportvalue=list.get(i).getREPORTVALUE();
					if(StringHelper.isEmpty(reportvalue)||StringHelper.isEmpty(reportvalue.trim())){
						reportvalue=list.get(i).getCONSTVALUE();
					}
					innermap_report.put(list.get(i).getCONSTVALUE(), reportvalue);
				}
			}
			typeMap.put(consttype, innermap);
			reportMap.put(consttype, innermap_report);
			if (innermap.containsKey(constvalue))
				name = innermap.get(constvalue);
		}
		return name;
	}
	
	//用于“2018权利类型国标调整支持新旧枚举显示”
	@SuppressWarnings("unchecked")
	public static String getNameByValue_new(String consttype, String constvalue,String slsj) {
		String gxsj = ConfigHelper.getNameByValue("DJXT_UPDATETIME");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Long xmdjsj = null;
		Long xtgxsj = null;
		try {
			if(!StringHelper.isEmpty(slsj)) {
				if(slsj.contains("-")||slsj.contains(":")){
					xmdjsj = sdf.parse(slsj).getTime();
				}else{
					xmdjsj=new Long(slsj);
				}
			}
			if(!StringHelper.isEmpty(gxsj)) {
				xtgxsj = sdf.parse(gxsj).getTime();
			}
		} catch (ParseException e) {
			System.err.println("时间格式转换错误");
			e.printStackTrace();
		} catch(Exception e) {
			System.err.println("发生错误");
			e.printStackTrace();
		}
		String name = "";
		if("TDYT".equals(consttype)||("QLLX".equals(consttype)&&!StringHelper.isEmpty(slsj)&&!StringHelper.isEmpty(gxsj)&&xtgxsj<xmdjsj)){//用于“2018权利类型国标调整支持新旧枚举显示”
			LinkedHashMap<String, String> innermap_report = new LinkedHashMap<String, String>();
			LinkedHashMap<String, String> innermap_new = new LinkedHashMap<String, String>();
			List<BDCS_CONST> list = getConstByType_new();
			if (list != null) {
				for (int i = 0; i < list.size(); i++) {
					if("TDYT".equals(consttype)&&"1".equals(list.get(i).getSFSY())){
						innermap_new.put(list.get(i).getGJVALUE(), list.get(i).getGJCONSTTRANS());
					}else if("QLLX".equals(consttype)&&"1".equals(list.get(i).getSFSY())){
						innermap_new.put(list.get(i).getGJVALUE(), list.get(i).getGJCONSTTRANS());
					}else if(!"QLLX".equals(consttype)||("QLLX".equals(consttype)&&!"4".equals(constvalue)&&!"6".equals(constvalue)&&!"8".equals(constvalue))){
						innermap_new.put(list.get(i).getCONSTVALUE(), list.get(i).getCONSTTRANS());
					}
				}
			}
			if (innermap_new.containsKey(constvalue))
				name = innermap_new.get(constvalue);
		}else{
			if (typeMap.containsKey(consttype)) {
				Map<String, String> innermap = typeMap.get(consttype);
				if (innermap.containsKey(constvalue)) {
					name = innermap.get(constvalue);
				}
			}else {
				LinkedHashMap<String, String> innermap = new LinkedHashMap<String, String>();
				LinkedHashMap<String, String> innermap_report = new LinkedHashMap<String, String>();
				List<BDCS_CONST> list = getConstByType(consttype);
				if (list != null) {
					for (int i = 0; i < list.size(); i++) {
						if(!StringHelper.isEmpty(list.get(i).getSFSY())&&"0".equals(list.get(i).getSFSY())){
							innermap.put(list.get(i).getCONSTVALUE(), list.get(i).getCONSTTRANS());
						}else if(!StringHelper.isEmpty(list.get(i).getSFSY())&&"1".equals(list.get(i).getSFSY())&&"TDYT".equals(consttype)){
							innermap.put(list.get(i).getGJVALUE(), list.get(i).getGJCONSTTRANS()+"-"+list.get(i).getGJVALUE());
						}
						String reportvalue=list.get(i).getREPORTVALUE();
						if(StringHelper.isEmpty(reportvalue)||StringHelper.isEmpty(reportvalue.trim())){
							reportvalue=list.get(i).getCONSTVALUE();
						}
						innermap_report.put(list.get(i).getCONSTVALUE(), reportvalue);
					}
				}
				typeMap.put(consttype, innermap);
				reportMap.put(consttype, innermap_report);
				if (innermap.containsKey(constvalue))
					name = innermap.get(constvalue);
			}
		}
		
		return name;
	}
	
	/**
	 * 根据类型值和value获取字典的name值
	 * 
	 * @作者 刘树峰
	 * @创建时间 2015年6月10日下午3:22:23
	 * @param consttype
	 * @param constvalue
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static String getReportValueByValue(String consttype, String constvalue) {
		String name = "";
		if (reportMap.containsKey(consttype)) {
			Map<String, String> innermap_report = reportMap.get(consttype);
			if (innermap_report.containsKey(constvalue)) {
				name = innermap_report.get(constvalue);
			}
		} else {
			LinkedHashMap<String, String> innermap = new LinkedHashMap<String, String>();
			LinkedHashMap<String, String> innermap_report = new LinkedHashMap<String, String>();
			List<BDCS_CONST> list = getConstByType(consttype);
			if (list != null) {
				for (int i = 0; i < list.size(); i++) {
					innermap.put(list.get(i).getCONSTVALUE(), list.get(i).getCONSTTRANS());
					String reportvalue=list.get(i).getREPORTVALUE();
					if(StringHelper.isEmpty(reportvalue)||StringHelper.isEmpty(reportvalue.trim())){
						reportvalue=list.get(i).getCONSTVALUE();
					}
					innermap_report.put(list.get(i).getCONSTVALUE(), reportvalue);
				}
			}
			typeMap.put(consttype, innermap);
			reportMap.put(consttype, innermap_report);
			if (innermap_report.containsKey(constvalue))
				name = innermap_report.get(constvalue);
		}
		return name;
	}

	/**
	 * 获取字典表信息
	 * 
	 * @作者 刘树峰
	 * @创建时间 2015年6月22日下午3:02:45
	 * @param consttype
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static Map getDictionary(String consttype) {
		Map map = null;
		if (typeMap.containsKey(consttype)) {
			Map innermap = typeMap.get(consttype);
			map = innermap;
		} else {
			LinkedHashMap<String, String> innermap = new LinkedHashMap<String, String>();
			LinkedHashMap<String, String> innermap_report = new LinkedHashMap<String, String>();
			List<BDCS_CONST> list = getConstByType(consttype);
			if (list != null) {
				for (int i = 0; i < list.size(); i++) {
					if(!StringHelper.isEmpty(list.get(i).getSFSY()) && list.get(i).getSFSY().equals("1") && "TDYT".equals(consttype)){
						innermap.put(list.get(i).getGJVALUE(), list.get(i).getGJCONSTTRANS()+"-"+list.get(i).getGJVALUE());
					}else{
						innermap.put(list.get(i).getCONSTVALUE(), list.get(i).getCONSTTRANS());
					}
					String reportvalue=list.get(i).getREPORTVALUE();
					if(StringHelper.isEmpty(reportvalue)||StringHelper.isEmpty(reportvalue.trim())){
						reportvalue=list.get(i).getCONSTVALUE();
					}
					innermap_report.put(list.get(i).getCONSTVALUE(), reportvalue);
				}
			}
			typeMap.put(consttype, innermap);
			reportMap.put(consttype, innermap_report);
			map = innermap;
		}
		return map;
	}
	
	//用于“2018权利类型国标调整支持新旧枚举显示”
	@SuppressWarnings("rawtypes")
	public static Map getNewDictionary(String consttype) {
		Map map = null;
		if (typeMap_new.containsKey(consttype)) {
			Map innermap = typeMap_new.get(consttype);
			map = innermap;
		} else {
			LinkedHashMap<String, String> innermap = new LinkedHashMap<String, String>();
			LinkedHashMap<String, String> innermap_report = new LinkedHashMap<String, String>();
			List<BDCS_CONST> list = getConstByType(consttype);
			if (list != null) {
				for (int i = 0; i < list.size(); i++) {
					if("TDYT".equals(consttype)&&"1".equals(list.get(i).getSFSY())){
						innermap.put(list.get(i).getGJVALUE(), list.get(i).getGJCONSTTRANS()+"-"+list.get(i).getGJVALUE());
					}else if("QLLX".equals(consttype)&&"1".equals(list.get(i).getSFSY())){
						innermap.put(list.get(i).getGJVALUE(), list.get(i).getGJCONSTTRANS());
					}
					String reportvalue=list.get(i).getREPORTVALUE();
					if(StringHelper.isEmpty(reportvalue)||StringHelper.isEmpty(reportvalue.trim())){
						reportvalue=list.get(i).getGJVALUE();
					}
					innermap_report.put(list.get(i).getGJVALUE(), reportvalue);
				}
			}
			typeMap_new.put(consttype, innermap);
			reportMap.put(consttype, innermap_report);
			map = innermap;
		}
		return map;
	}

	public static void reload()
	{
		typeMap.clear();
	}
	/**
	 * 根据常量consttrans字段获取常量值（constvalue）
	 * @Title: getvalueByConst
	 * @author:heks
	 * @date：2017年6月6日 下午10:11:56
	 * @param consttrans
	 *            名称
	 * @return 常量值
	 */
	public static String getvalueByConst(String consttrans) {
		if(!StringHelper.isEmpty(consttrans)){
			List<BDCS_CONST> con = dao.getDataList(BDCS_CONST.class, "CONSTTRANS = '"+consttrans+"'");
			String constvalue = "";
			if(con.size() > 0){
				constvalue = con.get(0).getCONSTVALUE();
			}
			
			return constvalue;
		}else{
			return "";
		}
	}
}
