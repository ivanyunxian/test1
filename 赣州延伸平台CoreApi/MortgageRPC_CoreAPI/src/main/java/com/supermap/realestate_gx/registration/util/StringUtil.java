package com.supermap.realestate_gx.registration.util;

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

import com.supermap.realestate.registration.util.ConstValue.BDCDYLX;

/**
 * 字符串操作工具类
 * 
 * @author Ouzr
 *
 */
public class StringUtil {
    /**
     * 获取值字符串用逗号隔开
     * 
     * @作者 ouzr
     * @创建时间 2016年4月25日上午11:34:01
     * @param list
     * @param field
     * @return
     */
    public static String getListFieldToString(List<Map> list, String field) {
        StringBuilder temp = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            Map colMap = list.get(i);
            String value = StringUtil.valueOf(colMap.get(field));
            temp.append("'");
            temp.append(value);
            temp.append("'");
            if (i != list.size() - 1) {
                temp.append(",");
            }
        }

        return temp.toString();
    }
    
    public static String getSystemTime(){
        Date date=new Date();
        DateFormat format=new SimpleDateFormat("yyyy-MM-dd");
        String time=format.format(date);
        return time;
    }
    /**
     * list通过连接字符串，返回String
     * @作者 Administrator
     * @创建时间 2016年5月9日下午10:41:46
     * @param bdcqzh
     * @return
     */
    public static String valueOfString(List<String> list, String flag)
    {
        StringBuilder result = new StringBuilder();
        for(int i=0; i<list.size(); i++){
            String str = list.get(i);
            result.append(str);
            if(i != list.size()-1){
                result.append(flag);
            }
            
           
        }
        return result.toString();
    }

    /**
     * 判断字符串是否是纯数字
     * @作者 Administrator
     * @创建时间 2016年5月9日下午10:41:46
     * @param bdcqzh
     * @return
     */
    public static boolean isNumericByString(String bdcqzh)
    {
        boolean flag=true;
          Pattern pattern = Pattern.compile("[0-9]*"); 
           Matcher isNum = pattern.matcher(bdcqzh);
           if( !isNum.matches() ){
               flag= false; 
           } 
        return flag;
    }
    

    
    /**
     * Obj转换字符串
     * @作者 ouzr
     * @创建时间 2016年5月9日下午10:41:13
     * @param obj
     * @return
     */
    public static String valueOf(Object obj) {
        if (obj != null) {
            return obj.toString().trim();
        }
        return "";
    }
    
    /**
     * 获取UUID
     * @作者 Ouzr
     * @创建时间 2016年5月10日上午11:00:11
     * @return String 
     */
    public static String getUUID(){
        return UUID.randomUUID().toString();
    }
    
    /**
     * 字符串转java.util.Date
     * @作者 ouzr
     * @创建时间 2016年5月10日下午4:21:42
     * @param obj
     * @return
     */
    public static Date toDate(Object obj){
        String dateStr = valueOf(obj);
        SimpleDateFormat sdf =   new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
        Date date = null;
        try {
            if(dateStr.equals("")){
                return null;
            }
            date = sdf.parse( dateStr);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
       return date;
    }
    
    /**
     * 字符串转换为数字
     * @作者 Ouzr
     * @创建时间 2016年5月10日下午4:34:26
     * @param obj
     * @return
     */
    public static BigDecimal toNumber(Object obj){
        String valueStr = valueOf(obj);
        if(valueStr.equals("")){
            return null;
        }
        return new BigDecimal(valueStr);
    }
    public static Integer toNumberInteger(Object obj){
        String valueStr = valueOf(obj);
        if(valueStr.equals("")){
            return null;
        }
        return new Integer(valueStr);
    }
    
    
    /**
     * 通过bdcdylx和ly获取对应的表String
     */
    public static String getBDCDYLXTable(String bdcdylx,String ly){
    	String tablename = "";
    	String tabletype = "";
    	if("01".equals(ly)){
    		tabletype = "GZ";
    	}else if("02".equals(ly)){
    		tabletype = "XZ";
    	}else if("03".equals(ly)){
    		tabletype = "LS";
    	}
    	BDCDYLX dylx = BDCDYLX.initFrom(bdcdylx);
    	if(BDCDYLX.H.equals(dylx)){
    		tablename = "BDCS_H_"+tabletype;
    	}else if(BDCDYLX.SHYQZD.equals(dylx)){
    		tablename = "BDCS_SHYQZD_"+tabletype;
    	}else if(BDCDYLX.YCH.equals(dylx)){
    		tablename = "BDCS_H_XZY";
    	}else if(BDCDYLX.NYD.equals(dylx)){
    		tablename = "BDCS_NYD_"+tabletype;
    	}else if(BDCDYLX.LD.equals(dylx)){
    		tablename = "BDCS_SLLM_"+tabletype;
    	}else if(BDCDYLX.HY.equals(dylx)){
    		tablename = "BDCS_HY_"+tabletype;
    	}else if(BDCDYLX.SYQZD.equals(dylx)){
    		tablename = "BDCS_SYQZD_"+tabletype;
    	}
    	return tablename;
    }
    
}
