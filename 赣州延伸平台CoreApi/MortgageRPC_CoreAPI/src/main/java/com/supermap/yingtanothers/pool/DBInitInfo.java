package com.supermap.yingtanothers.pool;

import java.util.ArrayList;  
import java.util.List;  

import com.supermap.realestate.registration.util.ConfigHelper;
/**
 * 
 * 作者： 苗利涛
 * 时间： 2016年1月26日 上午9:13:31
 * 功能：初始化，加载所有的配置文件 
 */
public class DBInitInfo {

	public  static List<DBbean>  beans = null;  
    static{  
        beans = new ArrayList<DBbean>();  
        DBbean beanOracle = new DBbean(); 
        
        String url_jy =ConfigHelper.getNameByValue("URL_JY");
		String username_jy = ConfigHelper.getNameByValue("USERNAME_JY");
		String password_jy = ConfigHelper.getNameByValue("PASSWORD_JY");
       
        beanOracle.setDriverName("oracle.jdbc.driver.OracleDriver");  
        beanOracle.setUrl(url_jy);  
        beanOracle.setUserName(username_jy);  
        beanOracle.setPassword(password_jy);  
          
        beanOracle.setMinConnections(5);  
        beanOracle.setMaxConnections(100);  
          
        beanOracle.setPoolName("yingtanPool");  
        beans.add(beanOracle);  
    }  
}
