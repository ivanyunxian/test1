package com.supermap.wisdombusiness.utility;

import java.text.SimpleDateFormat;

import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;

public class JsonDateValueProcessor implements JsonValueProcessor {  
	  
    public static final String Default_DATE_PATTERN = "yyyy-MM-dd";    
    private SimpleDateFormat dateFormat;  
      
    public JsonDateValueProcessor(String datePattern){  
        try {    
             dateFormat = new SimpleDateFormat(datePattern);    
         } catch (Exception e) {  
             dateFormat = new SimpleDateFormat(Default_DATE_PATTERN);     
         }  
    }  
       
    public Object processArrayValue(Object value, JsonConfig jsonConfig) {  
        return process(value);  
    }  
  
    public Object processObjectValue(String key, Object value, JsonConfig jsonConfig) {  
        return process(value);  
    }  
      
    private Object process(Object value) {    
        if (value == null) {    
            return "";    
        } else {    
            return dateFormat.format(value);    
        }    
    }   
}  
