package com.supermap.realestate_gx.registration.util;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.supermap.realestate.registration.util.StringHelper;

/**
 * 反射工具类
 * 
 * @author wuzhu
 *
 */
public class ReflectUtil {
	
	/**
	 * @param 操作对象
	 * @return 表空间名
	 */
	public static String getTablespacesName(Object thisObj)
	{
		return thisObj.getClass().getAnnotation(javax.persistence.Table.class).schema();
	}
	/**
	 * @param 操作对象
	 * @return 表名
	 */
	public static String getTableName(Object thisObj)
	{
		return thisObj.getClass().getSimpleName();
	}
    /**
     * @param 要赋值的对象字段名
     * @param 要赋值的值
     * @param 对象
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static  void setMethod(Object field, Object value, Object thisObj) {
        Class c;
        try {
            c = Class.forName(thisObj.getClass().getName());
            String met = (String) field;
            met = met.trim();
            if (!met.substring(0, 1).equals(met.substring(0, 1).toUpperCase())) {
                met = met.toUpperCase();
            }
            if (!String.valueOf(field).startsWith("set")) {
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
    /**
     * @param 对象的字段名
     * @param 对象
     * @return 返回值
     */
    public static  Object getMethod(Object field, Object thisObj) {
        Class c;
        try {
            c = Class.forName(thisObj.getClass().getName());
            String met = (String) field;
            met = met.trim();
            if (!met.substring(0, 1).equals(met.substring(0, 1).toUpperCase())) {
                met = met.toUpperCase();
            }
            if (!String.valueOf(field).startsWith("get")) {
                met = "get" + met;
            }
            Method m = c.getMethod(met);
            return   m.invoke(thisObj);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
		return null;
    }
	@SuppressWarnings("rawtypes")
	private static Class[] getMethodParamTypes(Object classInstance, String methodName) throws ClassNotFoundException {
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
}
