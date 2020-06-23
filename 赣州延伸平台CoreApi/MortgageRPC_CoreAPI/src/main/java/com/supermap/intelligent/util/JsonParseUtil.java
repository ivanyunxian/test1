package com.supermap.intelligent.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.supermap.intelligent.model.MaterModel;
import com.supermap.intelligent.model.Mortgage;
import com.supermap.intelligent.model.Mortgage_dydy;
import com.supermap.intelligent.model.Mortgage_dyqr;
import com.supermap.intelligent.model.Mortgage_qlr;
import com.supermap.realestate.registration.util.StringHelper;
/**
 * 解析数据工具类
 * @author lx
 *
 */
public class JsonParseUtil {
	
	/**
	 * 将json字符串解析成实体类形式,异常返回null
	 * @param json
	 * @return
	 * @throws SecurityException 
	 * @throws Exception 
	 */
	@SuppressWarnings({ "unused" })
	public static Object  parseJsonToClss(Object source,String json) throws Exception{
		//判断空值
		Class<? extends Object> t = source.getClass();
		if(StringHelper.isEmpty(json)||t==null||json==null) {
			return null;
		}
		// 得到Class对象所表征的类的所有属性(包括私有属性)
		Field[] fields = t.getDeclaredFields();
		if (fields.length == 0) {
			fields = t.getSuperclass().getDeclaredFields();
		}
		//解析数据成map
		Map<String, Object>  map=parseJsonToMap(json);
		if(map==null||map.isEmpty()) {
			return  null;
		}
		//实体类赋值
		for (int i = 0; i < fields.length; i++) {
			String fieldName = fields[i].getName();
			if(StringHelper.isEmpty(map.get(fieldName.toUpperCase()))) {
				continue;
			}
			Field field_t = null;
			// 得到targetClz对象所表征的类的名为fieldName的属性，不存在就进入下次循环
			try {
				field_t = t.getDeclaredField(fieldName);
			} catch (NoSuchFieldException e) {
				field_t = t.getSuperclass().getDeclaredField(fieldName);
			}
				// 由属性名字得到对应set方法的名字
				String setMethodName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
				// 由方法的名字得到set方法对象
				Method setMethod=null;
				try 
				{
					if("class java.lang.String".equals(fields[i].getType()+"")) {
						setMethod = t.getDeclaredMethod(setMethodName, fields[i].getType());
					}else{
						continue;
					} 
					
				} catch (NoSuchMethodException e) {
					if("class java.lang.String".equals(fields[i].getType()+"")) {
						setMethod = t.getSuperclass().getDeclaredMethod(setMethodName, fields[i].getType());
					}else {
						continue;
					}
				}
				// 调用target对象的setMethod方法
				setMethod.invoke(source, map.get(fieldName.toUpperCase()).toString());
		}
		//返回结果
		return source;
	}
	/**
	 * 将Map解析成实体类形式,异常返回null
	 * @param json
	 * @return
	 * @throws SecurityException 
	 * @throws Exception 
	 */
	@SuppressWarnings({ "unused" })
	public static Object  parseMapToClss(Object source,Map<String, Object> map) throws Exception{
		//判断空值
		Class<? extends Object> t = source.getClass();
		// 得到Class对象所表征的类的所有属性(包括私有属性)
		Field[] fields = t.getDeclaredFields();
		if (fields.length == 0) {
			fields = t.getSuperclass().getDeclaredFields();
		}
		//解析数据成map
		if(map==null||map.isEmpty()) {
			return  null;
		}
		//实体类赋值
		for (int i = 0; i < fields.length; i++) {
			String fieldName = fields[i].getName();
			if(StringHelper.isEmpty(map.get(fieldName.toUpperCase()))) {
				continue;
			}
			Field field_t = null;
			// 得到targetClz对象所表征的类的名为fieldName的属性，不存在就进入下次循环
			try {
				field_t = t.getDeclaredField(fieldName);
			} catch (NoSuchFieldException e) {
				field_t = t.getSuperclass().getDeclaredField(fieldName);
			}
				// 由属性名字得到对应set方法的名字
				String setMethodName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
				// 由方法的名字得到set方法对象
				Method setMethod=null;
				try 
				{
					if("class java.lang.String".equals(fields[i].getType()+"")) {
						setMethod = t.getDeclaredMethod(setMethodName, fields[i].getType());
					}else{
						continue;
					} 
					
				} catch (NoSuchMethodException e) {
					if("class java.lang.String".equals(fields[i].getType()+"")) {
						setMethod = t.getSuperclass().getDeclaredMethod(setMethodName, fields[i].getType());
					}else {
						continue;
					}
				}
				// 调用target对象的setMethod方法
				setMethod.invoke(source, map.get(fieldName.toUpperCase()).toString());
		}
		//返回结果
		return source;
	}
	
	/**
	 * json字符串转换成map
	 * @param json
	 * @return
	 * @throws Exception
	 */
	public static Map<String, Object>   parseJsonToMap(String json) throws Exception{
		Map<String, Object>  map= new HashMap <String, Object> ();
		JSONObject object = JSON.parseObject(json);
		for (Entry<String, Object> entry : object.entrySet()) {
    		map.put(entry.getKey(), entry.getValue().toString().replace("<br/>", "\n").replace("&nbsp;", "").replace("<p>", "").replace("</p>", ""));
		}
		if(map.isEmpty()) {
			return  null;
		}
		return map;
	}
	
	
	
	/**
	 * 抵押平台接口解析
	 * @return
	 * @throws Exception 
	 */
	public  static Mortgage parseToMortgage(String json) throws Exception {
		if(StringHelper.isEmpty(json)) {
			return null;
		}
		Mortgage mortgage =(Mortgage)parseJsonToClss(new Mortgage(),json);
		//一次解析
		Map<String, Object>  map1=parseJsonToMap(json);
		
		if(map1!=null&&!map1.isEmpty()) {
			//二次解析
			String datastr =map1.get("DATA")+"";
			if(!StringHelper.isEmpty(datastr)) {
				mortgage=(Mortgage)parseJsonToClss(mortgage,datastr);
				Map<String, Object>  data=parseJsonToMap(datastr);
				if (data!=null&&!data.isEmpty()) {
					//横向解析多个数据
					//QLRLIST
					String qlrstr =data.get("QLRLIST")+"";
					if(!StringHelper.isEmpty(qlrstr)) {
						List<Mortgage_qlr> list_qlr =new ArrayList<Mortgage_qlr>();
						JSONArray list=JSON.parseArray(qlrstr);
						for(int i=0;i<list.size();i++) {
							list_qlr.add((Mortgage_qlr)parseJsonToClss(new Mortgage_qlr(),list.getJSONObject(i).toJSONString()));
						} 
						mortgage.setQlrlist(list_qlr);
					}
					//DYQRLIST
					String dyqrstr =data.get("DYQRLIST")+"";
					if(!StringHelper.isEmpty(dyqrstr)) {
						//Map<String, Object>  dyqrmap=parseJsonToMap(dyqrstr);
						List<Mortgage_dyqr> mortgage_dyqr =new ArrayList<Mortgage_dyqr>();
						JSONArray list=JSON.parseArray(dyqrstr);
						if (list!=null&&list.size()>0) {
							for(int i=0;i<list.size();i++) {
								mortgage_dyqr.add((Mortgage_dyqr)parseJsonToClss(new Mortgage_dyqr(),list.getJSONObject(i).toJSONString()));
							} 
							mortgage.setDyqrlist(mortgage_dyqr);
						}
						
					}
					//DYDYLIST
					String dydystr =data.get("DYDYLIST")+"";
					if(!StringHelper.isEmpty(dydystr)) {
						List<Mortgage_dydy> Mortgage_dydy =new ArrayList<Mortgage_dydy>();
						JSONArray list=JSON.parseArray(dydystr);
						if (list!=null&&list.size()>0) {
							for(int i=0;i<list.size();i++) {
								Mortgage_dydy.add((Mortgage_dydy)parseJsonToClss(new Mortgage_dydy(),list.getJSONObject(i).toJSONString()));
							} 
							mortgage.setDydylist(Mortgage_dydy);	
						}
						
					}
					
					//MaterModel
					String promaterstr =data.get("PROMATER")+"";
					if(!StringHelper.isEmpty(promaterstr)) {
						List<MaterModel> matermodellist =new ArrayList<MaterModel>();
						JSONArray list=JSON.parseArray(dydystr);
						if (list!=null&&list.size()>0) {
							for(int i=0;i<list.size();i++) {
								matermodellist.add((MaterModel)parseJsonToClss(new MaterModel(),list.getJSONObject(i).toJSONString()));
							} 
							mortgage.setMatermodel(matermodellist);
						}
						
					}
				}
			} 
			
		}
		return mortgage;
		
	}

	
	
	
	
	
	
	
	
	
	
	
}
