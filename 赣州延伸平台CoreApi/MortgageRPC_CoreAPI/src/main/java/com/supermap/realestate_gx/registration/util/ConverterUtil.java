package com.supermap.realestate_gx.registration.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.util.StringUtils;

import net.sf.json.JSONObject;

import com.supermap.realestate.registration.util.StringHelper;


public class ConverterUtil {
	public static  JSONObject mapobj=null;//映射表的对象
	
	public ConverterUtil(String mapfilepath)
	{
		//改映射文件不用重启
		//if(mapobj==null){
			String json = StringHelper.readFile(mapfilepath);
			  mapobj=JSONObject.fromObject(json);
			//}
	}
	 /**多个Map创建多个实体类
	 * @param 创建实体类类型
	 * @param  要填充的数据集合（多个Map创建多个实体类）。
	 * @return 赋值后的实体类集合
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * 
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public  <T> List<T>  CreateClass(Class<T> obj,List<Map> fc_datas)
	 {
		List result=new ArrayList();
		try {
		
		for(Map fc_data:fc_datas){
		Object objnew = obj.newInstance();
			setValue(fc_data,objnew);
			result.add(objnew);
		}
		}catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return  result;
	 }
	
	/**多个Map创建一个实体类。如有相同后面的值覆盖前面的
	 * @param 要创建的实体类型
	 * @param 房产数据Map （多个Map创建一个实体类）
	 * @return 返回单个不动产登记系统实体类。
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public  <T> T  CreateSingleClass(Class<T> obj,List<Map> fc_datas)
	 {
		Object objnew=null;
		try {
		   objnew = obj.newInstance();
			for(Map fc_data:fc_datas){
					setValue(fc_data,objnew);
				}

		}catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return  (T)objnew;
	 }
	/**一个Map创建一个实体类
	 * @param 要创建的实体类型
	 * @param 房产数据Map （一个Map创建一个实体类）
	 * @return 返回单个不动产登记系统实体类。
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public  <T> T  CreateSingleClass(Class<T> obj,Map fc_data)
	 {
		Object objnew=null;
		try {
		   objnew = obj.newInstance();
			setValue(fc_data,objnew);

		}catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return  (T)objnew;
	 }
	/**
	 * @param fcmaplist
	 * @param 库名.表名 如：FDCMAIN.RS_SYQFWJBXX
	 */
	public void AddPrefix(List<Map> fcmaplist,String tablename)
	{

		List<Map>  resultlist=new ArrayList<Map>();
		for(Map fcmap:fcmaplist){
			Set fc_set = fcmap.keySet();
			Map newmap=new HashMap();
			Iterator iterator = fc_set.iterator();
			while (iterator.hasNext()) {
				Object mapfield = iterator.next();
				Object val = fcmap.get(mapfield);
				String key=(tablename+"."+mapfield).toUpperCase();
				newmap.put(key, val);
			}
			resultlist.add(newmap);
		}
		fcmaplist.clear();
		for(Map m:resultlist){
			fcmaplist.add(m);
		}
	}
	/**
	 * @param fcmaplist
	 * @param tablename
	 */
	public void AddPrefix(List<Map> fcmaplist,FC_TABLE tablename)
	{

		List<Map>  resultlist=new ArrayList<Map>();
		for(Map fcmap:fcmaplist){
			Set fc_set = fcmap.keySet();
			Map newmap=new HashMap();
			Iterator iterator = fc_set.iterator();
			while (iterator.hasNext()) {
				Object mapfield = iterator.next();
				Object val = fcmap.get(mapfield);
				String key=(tablename.getFullName()+"."+mapfield).toUpperCase();
				newmap.put(key, val);
			}
			resultlist.add(newmap);
		}
		fcmaplist.clear();
		for(Map m:resultlist){
			fcmaplist.add(m);
		}
	}
	/**
	 * @param fcmap
	 * @param 库名.表名 如：FDCMAIN.RS_SYQFWJBXX
	 */
	public void AddPrefixForSingle(Map fcmap,String tablename)
	{
			Set fc_set = fcmap.keySet();
			Map newmap=new HashMap();
			Iterator iterator = fc_set.iterator();
			while (iterator.hasNext()) {
				Object mapfield = iterator.next();
				Object val = fcmap.get(mapfield);
				String key=(tablename+"."+mapfield).toUpperCase();
				newmap.put(key, val);
			}
			fcmap.clear();
			fcmap.putAll(newmap);
	}
	public void AddPrefixForSingle(Map fcmap,FC_TABLE tablename)
	{
			Set fc_set = fcmap.keySet();
			Map newmap=new HashMap();
			Iterator iterator = fc_set.iterator();
			while (iterator.hasNext()) {
				Object mapfield = iterator.next();
				Object val = fcmap.get(mapfield);
				String key=(tablename.getFullName()+"."+mapfield).toUpperCase();
				newmap.put(key, val);
			}
			fcmap.clear();
			fcmap.putAll(newmap);
	}
	@SuppressWarnings("rawtypes")
    private  void setValue(Map map, Object thisObj) {
		String tablename=thisObj.getClass().getSimpleName();
		String dataname=thisObj.getClass().getAnnotation(javax.persistence.Table.class).schema();
	      if(StringUtils.isEmpty(tablename))
          	return;
          if(StringUtils.isEmpty(dataname))
        	 return;
		String map_param_class=dataname+"."+tablename;
        Set set = map.keySet();
        Iterator iterator = set.iterator();
        while (iterator.hasNext()) {
            Object mapfield = iterator.next();
            String tablefield= getTableFieldByMapping(String.valueOf(mapfield),map_param_class.toUpperCase());
            if(StringUtils.isEmpty(tablefield))
            {
            	continue;
            }
            String[] tablefields=tablefield.split("\\.");
            if(tablefields.length!=3)
            {
            	System.out.print(mapfield+"根据文件得到的对应的不动产："+tablefield.toString()+"映射的字段格式不对。格式为空间名.表名.字段名。如BDCK.BDCS_H_XZ.BDCDYH");
            	continue;
            }
      
            if(tablefields[0].toUpperCase().equals(dataname.toUpperCase())&&tablefields[1].toUpperCase().equals(tablename.toUpperCase())){
              Object val = map.get(mapfield);
              setMethod(tablefields[2], val, thisObj);
            }
        }
    }
    private  String getTableFieldByMapping(String mappingfield,String map_param_class)
    {
	     if(mapobj==null||mapobj.isNullObject()){
	            return null;
	        }
	     JSONObject singlemapobj=mapobj.getJSONObject(map_param_class);
	     if(singlemapobj==null||singlemapobj.isNullObject()){
	            return null;
	        }
	     if(singlemapobj.containsKey(mappingfield))
	       return singlemapobj.getString(mappingfield);
	     else
	       return null;
	     
    }
    @SuppressWarnings({ "rawtypes", "unchecked" })
    private  void setMethod(Object field, Object value, Object thisObj) {
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
	@SuppressWarnings("rawtypes")
	private  Class[] getMethodParamTypes(Object classInstance, String methodName) throws ClassNotFoundException {
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
	 * 房产表
	 *
	 */
	/**
	 * @author Administrator
	 *
	 */
	/**
	 * @author Administrator
	 *
	 */
	public enum FC_TABLE {
		/**
		 * 所有权房屋基本信息
		 */
		FDCMAIN$RS_SYQFWJBXX("FDCMAIN.RS_SYQFWJBXX"),
		/**
		 * 所有权基本信息
		 */
		FDCMAIN$RS_SYQJBXX("FDCMAIN.RS_SYQJBXX"),
		/**
		 * 房地产抵押他项权利信息
		 */
		FDCMAIN$RS_DYTXQLXX("FDCMAIN.RS_DYTXQLXX"),
		/**
		 * 他项权证打印信息
		 */
		FDCMAIN$RS_TXQZDYXX("FDCMAIN.RS_TXQZDYXX"),
		/**
		 * (预)查封关联信息
		 */
		FDCMAIN$RS_YCFGLXX("FDCMAIN.RS_YCFGLXX"),
		/**
		 * 业务预告登记信息
		 */
		FDCMAIN$RS_YGDJXX("FDCMAIN.RS_YGDJXX"),
		/**
		 * 预购商品房他项权利信息
		 */
		FDCMAIN$RS_YGTXQLXX("FDCMAIN.RS_YGTXQLXX"),
		/**
		 * 在建工程他项权利信息
		 */
		FDCMAIN$RS_ZJTXQLXX("FDCMAIN.RS_ZJTXQLXX"),
		
		/**
		 * 登记簿幢基本信息
		 */
		FDCDJK$EP_JZWZJBXX("FDCDJK.EP_JZWZJBXX"),
		/**
		 * 登记簿分户基本信息
		 */
		FDCDJK$EP_JZWFJJBXX("FDCDJK.EP_JZWFJJBXX"),
		/**
		 * 登记簿所有权状况信息
		 */
		FDCDJK$EP_SYQZKXX("FDCDJK.EP_SYQZKXX"),
		/**
		 * 登记簿共有权状况信息
		 */
		FDCDJK$EP_GYQZKXX("FDCDJK.EP_GYQZKXX"),
		/**
		 * 登记簿用益物权信息
		 */
		FDCDJK$EP_YYWQZKXX("FDCDJK.EP_YYWQZKXX"),
		/**
		 * 登记簿担保权状况信息
		 */
		FDCDJK$EP_DBQZKXX("FDCDJK.EP_DBQZKXX"),
		/**
		 * 登记簿债权状况信息
		 */
		FDCDJK$EP_ZQZKXX("FDCDJK.EP_ZQZKXX"),
		/**
		 * 登记簿预告登记信息
		 */
		FDCDJK$EP_QTYGDJXX("FDCDJK.EP_QTYGDJXX"),
		/**
		 * 登记簿异议登记信息
		 */
		FDCDJK$EP_QTYYDJXX("FDCDJK.EP_QTYYDJXX"),
		/**
		 * 登记簿查封登记信息
		 */
		FDCDJK$EP_QTCFDJXX("FDCDJK.EP_QTCFDJXX"),
		/**
		 * 登记簿解封登记信息
		 */
		FDCDJK$EP_QTJFDJXX("FDCDJK.EP_QTJFDJXX"),
		/**
		 * 登记簿注销登记信息
		 */
		FDCDJK$EP_QTZXDJXX("FDCDJK.EP_QTZXDJXX"),
		/**
		 * 登记簿业主共有部分信息
		 */
		FDCDJK$EP_QTYZGYXX("FDCDJK.EP_QTYZGYXX"),
		/**
		 * 登记簿房屋登记业务记录
		 */
		FDCDJK$EP_QTDJYWXX("FDCDJK.EP_QTDJYWXX"),
		/**
		 * 登记簿房屋坐落变更记录
		 */
		FDCDJK$EP_QTZLBGXX("FDCDJK.EP_QTZLBGXX"),
		
		
		/**
		 * 房产所有权基本信息
		 */
		FDC_DAK$EP_FJSYQJBXX("FDC_DAK.EP_FJSYQJBXX"), 
		/**
		 * 林权登记信息
		 */
		FDC_DAK$EP_FJTXQJBXX("FDC_DAK.EP_FJTXQJBXX"), 
		/**
		 * 林权登记信息
		 */
		FDC_DAK$EP_FJSYQGYXX("FDC_DAK.EP_FJSYQGYXX"),
		/**
		 * 林权登记信息
		 */
		FDC_DAK$EP_FJCFQJBXX("FDC_DAK.EP_FJCFQJBXX"),
		/**
		 * 林权登记信息
		 */
		FDC_DAK$DA_SYQDAGLXX("FDC_DAK.DA_SYQDAGLXX"),
		/**
		 * 林权登记信息
		 */
		FDC_DAK$EP_DYQRGYXX("FDC_DAK.EP_DYQRGYXX"),
		/**
		 * 林权登记信息
		 */
		FDC_DAK$EP_QTYGDJXX("FDC_DAK.EP_QTYGDJXX"),
		/**
		 * 林权登记信息
		 */
		FDC_DAK$RS_DAGLXX("FDC_DAK.RS_DAGLXX"),
		/**
		 * 林权登记信息
		 */
		FDC_DAK$DAGLXX1("FDC_DAK.DAGLXX1"),
		/**
		 * 柳江自然幢
		 */
		EMFTRAN$BUILD("EMFTRAN.BUILD"),
		/**
		 * 柳江房屋表
		 */
		EMFTRAN$ROOM("EMFTRAN.ROOM"),
		/**
		 * 柳江土地表
		 */
		EMFTRAN$LAND("EMFTRAN.LAND"),
		/**
		 * 柳江房屋权利关系表
		 */
		EMFTRAN$OWNER("EMFTRAN.OWNER"),
		/**
		 * 柳江权利人表
		 */
		EMFTRAN$PERSON("EMFTRAN.PERSON"),
		/**
		 * 柳江共有人表
		 */
		EMFTRAN$CU_PERSON("EMFTRAN.CU_PERSON");
		
		// 成员变量
				private String fullName;
				// 构造方法
				private FC_TABLE(String fullName) {
					this.setFullName(fullName);
				}
				public String getFullName() {
					return fullName;
				}

				public void setFullName(String fullName) {
					this.fullName = fullName;
				}
	}
}
