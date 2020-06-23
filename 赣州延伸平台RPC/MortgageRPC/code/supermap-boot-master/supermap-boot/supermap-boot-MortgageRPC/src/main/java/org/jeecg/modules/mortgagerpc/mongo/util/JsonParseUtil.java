package org.jeecg.modules.mortgagerpc.mongo.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.jeecg.common.system.util.StringHelper;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

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
		Map<String, String>  map=parseJsonToMap(json);
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
	 * @param source
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
	public static Map<String, String>   parseJsonToMap(String json) throws Exception{
		Map<String, String>  map= new HashMap <String, String> ();
		JSONObject object = JSON.parseObject(json);
		for (Entry<String, Object> entry : object.entrySet()) {
    		map.put(entry.getKey(), entry.getValue().toString().replace("<br/>", "\n").replace("&nbsp;", "").replace("<p>", "").replace("</p>", ""));
		}
		if(map.isEmpty()) {
			return  null;
		}
		return map;
	}
	
	

	
	
	
	
	
	
	
	
	
	
	
}
