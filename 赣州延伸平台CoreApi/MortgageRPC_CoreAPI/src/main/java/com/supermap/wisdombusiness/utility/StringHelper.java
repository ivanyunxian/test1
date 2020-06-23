package com.supermap.wisdombusiness.utility;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.shiro.crypto.hash.Md5Hash;



public class StringHelper {
	public static final String encryptMD5(String source) {
		if (source == null) {
			source = "";
		}
		Md5Hash md5 = new Md5Hash(source);
		return md5.toString();
	}

	public static String[] ListToStrings(List<String> listStrings) {
		
		String[] resultStrings = new String[listStrings.size()];
		for (int i = 0, j = listStrings.size(); i < j; i++) {
			resultStrings[i] = listStrings.get(i);
		}
		return resultStrings;
	}
	
	/**
	 * 验证单个参数不为空
	 * @param str
	 * @return  (true为不空)
	 */
	public static boolean isNotNull(String str){
		if(str!=null && str.length()>0){
			return true;
		}
		return false;
	}
	/**
	 * 验证多个参数不为空
	 * @param strings
	 * @return （true为不空）
	 */
	public static boolean isNotNull(String... strings){
		boolean boo = true;
		for (String arg : strings) {  
            if(arg==null || arg.length()<=0){
            	boo=false;
            	break;
            }
        } 
		return boo;
	}
	
//	/**
//    * 日期转换成字符串 "{0:yyyy-MM-dd}"
//    * @param 
//    * returns 
//    */
//    public static String DateTimeToStr(Date dt)
//    {
//        String result = "";
//        if (dt != null)
//        {
//            result = String.format("{0:yyyy-MM-dd}", dt);
//        }
//        return result;
//    }
    /** 
     * 将Date类型时间转换为字符串 
     * @param date 
     * @return 
     */  
    public static String DateTimeToStr(Date date) {  
  
        String time;  
        SimpleDateFormat formater = new SimpleDateFormat();  
        formater.applyPattern("yyyy-MM-dd");  
        time = formater.format(date);  
        return time;  
    }  
    /**
    * object类型转换成int类型 
    *@param 
    * return int ,转换失败-1.
    */
    public static int ObjtoInt(Object obj)
    {
        int result = -1;
        try
        {
            if (obj != null)
            {
            	if(isNotNull(obj.toString())){
            		result = Integer.parseInt(obj.toString());
            	}
            }
        }
        catch(Exception ee)
        { }
        return result;
    }
    /**
     * obj转换为String
     * @param obj
     * @return String
     */
    public static String ObjToString(Object obj)
    {
        String result = "";
        try
        {
            if (obj != null)
            {
                result = obj.toString();
            }
        }
        catch(Exception ee)
        { }
        return result;
    }
    /**
     * object类型转换成double类型
     * @param obj
     * @return
     */
    public static Double ObjToDouble(Object obj)
    {
    	Double result = -1.00;
        try
        {
            if (obj != null)
            {
            	result = Double.parseDouble(obj.toString());
            }
        }
        catch(Exception ee)
        { }
        return result;
    }
    /**
     * object类型转换成Float类型
     * @param obj
     * @return
     */
    public static Float ObjToFloat(Object obj)
    {
    	Float result = -1f;
        try
        {
        	if(obj != null){
	            if (isNotNull(obj.toString() ))
	            {
	            	result = Float.parseFloat(obj.toString());
	            }
        	}
        }
        catch(Exception ee)
        { }
        return result;
    }
    /***
     * 字符串转换为整数型
     * @param str
     * @return
     */
    public static int StringToInt(String str)
    {
        int result = -1;
        try
        {
            if ( isNotNull(str))
            {
            	result = Integer.parseInt(str);
            }
        }
        catch(Exception ee)
        { }
        return result;
    }
    /***
     * 字符串转换为双精度浮点型
     * @param str
     * @return
     */
    public static Double StringToDouble(String str)
    {
        double result = -1;
        try
        {      	
            if (isNotNull( str))
            {
            	result = Double.parseDouble(str);
            }
        }
        catch(Exception ee)
        { }
        return result;
    }

    public static Date objToDateTime(Object obj) throws ParseException
    {
		
		Date dt = null ;
		try{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");   
			if(obj != null){
				String str = obj.toString();	
		        if(isNotNull( str) ){
		            dt = sdf.parse(str);
		        }
			}
		}
		catch(Exception ee){
			ee.getMessage();
		}
        return dt;
    }

    /** 
     * 将timestamp转换成date 
     * @author hellostoy 
     * @param tt 
     * @return 
     */  
    public static Date timestampToDate(Timestamp tt){  
        return new Date(tt.getTime());  
    } 
}
