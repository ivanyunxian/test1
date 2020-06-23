package com.supermap.wisdombusiness.core;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;

import com.supermap.wisdombusiness.utility.StringHelper;

public class SuperHelper {

	@SuppressWarnings("unchecked")
	public static <T extends Serializable> T GeneratePrimaryKey() {
		return (T) UUID.randomUUID().toString().replaceAll("-", "");
	}

	// 获取@Entity注解的Name
	public static <T> String getEntityName(Class<T> clazz) {
		return clazz.getAnnotation(Entity.class).name();
	}
	// 直接执行SQL，慎用！
	public static int excuteBySql(Session session, String sql,
			Map<String, Object> parameterCondition) {
		int result;
		SQLQuery query = session.createSQLQuery(sql);
		for (Map.Entry<String, Object> entry : parameterCondition.entrySet()) {
			query.setParameter(entry.getKey(), entry.getValue());
		}
		result = query.executeUpdate();
		return result;
	}

	public static String GetTableName(
			@SuppressWarnings("rawtypes") Class classtype) {
		Annotation[] anno = classtype.getAnnotations();
		String tableName = "";
		for (int i = 0; i < anno.length; i++) {
			if (anno[i] instanceof Table) {
				Table table = (Table) anno[i];
				tableName = table.name();
				break;
			}
		}
		return tableName;
	}

	public static <T> T getModelClass(Model springModel,
			String modelAttributeName) {
		ModelMap map = (ModelMap) springModel.asMap();
		if (springModel.containsAttribute(modelAttributeName)) {
			@SuppressWarnings("unchecked")
			T t = (T) map.get(modelAttributeName);
			return t;
		}
		return null;
	}

	public static String getBDCQZHXH(String BDCQZH){
		String BDCQZHXH="";
		if(StringHelper.isNotNull(BDCQZH)){
			String[] strs=BDCQZH.split(",");
			if(strs!=null&&strs.length>0){
				for(String str:strs){
					String xh="";
					String regex = "(.*)[(](.*)[)](.*)不动产.*第(.*)号";
					Pattern pattern = Pattern.compile(regex);
					Matcher m = pattern.matcher(str);
					while (m.find()) {
						xh=m.group(2)+m.group(4);
					}
					if(!StringHelper.isNotNull(xh)){
						xh=str;
					}
					if(StringHelper.isNotNull(xh)){
						BDCQZHXH=BDCQZHXH+xh+",";
					}
				}
			}
		}
		if((",").endsWith(BDCQZHXH)){
			BDCQZHXH="";
		}
		if(BDCQZHXH.endsWith(",")){
			BDCQZHXH=BDCQZHXH.substring(0, BDCQZHXH.length()-1);
		}
		return BDCQZHXH;
	}
}
