package com.supermap.wisdombusiness.dossier.web.common;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.supemap.mns.common.HttpMethod;
import com.supemap.mns.model.Message;

@Component("bookMapping")
public class BookMapping {

	private String FILE_NUMBER = "";
	private String DJB="project/";
	private String SQSPB="project/getsqspbstream/";
    private String ZSZM= "zs/getzszmstream/";
	

   public Message GetDJBXX(String File_Number){
	   Map <String ,String> map=new HashMap<String,String>();
	   return Basic.RegistRequest(DJB+File_Number+"/dbxx", map, HttpMethod.GET);
   }
   public Message GetDJBXX(String File_Number,String Regestservice){
	   Map <String ,String> map=new HashMap<String,String>();
	   return Basic.RegistRequest(Regestservice,DJB+File_Number+"/dbxx", map, HttpMethod.GET);
   }
   /**
    * 获取申请审批表
    * @param File_Number
    * @param actinst_id
    * @return
    */
   public Message GetSQSPB(String File_Number,String actinst_id){
	   Map <String ,String> map=new HashMap<String,String>();
	   return Basic.RegistRequest(SQSPB+File_Number+"/"+actinst_id, map, HttpMethod.GET);
   }
   
   /**
    * 获取证书证明
    * @param File_Number
    * @param actinst_id
    * @return
    */
   public Message GetZSZM(String File_Number){
	   Map <String ,String> map=new HashMap<String,String>();
	   return Basic.RegistRequest(ZSZM+File_Number, map, HttpMethod.GET);
   }
   
   
   
}


