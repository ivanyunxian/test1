package com.supermap.realestate_gx.registration.service.impl;

import com.supermap.realestate.registration.ViewClass.DJFZXX;
import com.supermap.realestate.registration.model.BDCS_DJDY_LS;
import com.supermap.realestate.registration.model.BDCS_DJDY_XZ;
import com.supermap.realestate.registration.model.BDCS_DJFZ;
import com.supermap.realestate.registration.model.BDCS_FSQL_LS;
import com.supermap.realestate.registration.model.BDCS_FSQL_XZ;
import com.supermap.realestate.registration.model.BDCS_H_GZ;
import com.supermap.realestate.registration.model.BDCS_H_LS;
import com.supermap.realestate.registration.model.BDCS_H_XZ;
import com.supermap.realestate.registration.model.BDCS_QDZR_GZ;
import com.supermap.realestate.registration.model.BDCS_QDZR_LS;
import com.supermap.realestate.registration.model.BDCS_QDZR_XZ;
import com.supermap.realestate.registration.model.BDCS_QLR_GZ;
import com.supermap.realestate.registration.model.BDCS_QLR_LS;
import com.supermap.realestate.registration.model.BDCS_QLR_XZ;
import com.supermap.realestate.registration.model.BDCS_QL_GZ;
import com.supermap.realestate.registration.model.BDCS_QL_LS;
import com.supermap.realestate.registration.model.BDCS_QL_XZ;
import com.supermap.realestate.registration.model.BDCS_XMXX;
import com.supermap.realestate.registration.model.BDCS_ZRZ_LS;
import com.supermap.realestate.registration.model.BDCS_ZRZ_XZ;
import com.supermap.realestate.registration.model.BDCS_ZS_GZ;
import com.supermap.realestate.registration.model.BDCS_ZS_LS;
import com.supermap.realestate.registration.model.BDCS_ZS_XZ;
import com.supermap.realestate.registration.service.GZTJService;
import com.supermap.realestate.registration.util.ConstValue;
import com.supermap.realestate.registration.util.ConstValue.CZFS;
import com.supermap.realestate.registration.util.ConstHelper;
import com.supermap.realestate.registration.util.Global;
import com.supermap.realestate.registration.util.ObjectHelper;
import com.supermap.realestate.registration.util.ProjectHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.realestate.registration.util.YwLogUtil;
import com.supermap.realestate_gx.registration.service.ConverterService;
import com.supermap.realestate_gx.registration.service.GX_Service;
import com.supermap.realestate_gx.registration.util.ConverterUtil;
import com.supermap.realestate_gx.registration.util.GX_Util;
import com.supermap.realestate_gx.registration.util.ConverterUtil.FC_TABLE;
import com.supermap.wisdombusiness.core.SuperHelper;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import com.supermap.wisdombusiness.web.Message;

import java.sql.SQLException;

import com.supermap.wisdombusiness.web.ui.tree.State;
import com.supermap.wisdombusiness.web.ui.tree.Tree;
import com.supermap.wisdombusiness.workflow.service.common.DateUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class ConverterServiceImpl implements ConverterService
{
	public static  JSONArray zdmapobj=null;//映射表的对象
  @Autowired
  private CommonDao baseCommonDao;

@Override
public void ImportZRZ(HttpServletRequest request, HttpServletResponse response) {
	//映射配置文件
		@SuppressWarnings("deprecation")
		String mapfilepath = String.format("%s\\resources\\FC2DJXT\\config\\柳江房产数据与登记系统数据映射关系.txt", request.getRealPath("/"));
		String zdmapfilepath = String.format("%s\\resources\\FC2DJXT\\config\\柳江房产数据宗地与登记系统宗地映射关系.txt", request.getRealPath("/"));
		 int zrztime=getAndUpdateTimeConfig(request,"ZRZ");
		 int n=0;
		 String beforetime=StringHelper.FormatYmdhmsByDate(new Date());
		//初始化工具类
		ConverterUtil util=new ConverterUtil(mapfilepath);
		List<Map> validateZrzs =new ArrayList<Map>();
		String zmmapjson = StringHelper.readFile(zdmapfilepath);
		 zdmapobj = JSONArray.fromObject(zmmapjson);
		    for(int i = 0; i < zdmapobj.size(); i++){   
	            JSONObject jsonObject = zdmapobj.getJSONObject(i);
	            //jsonObject.get("ZDBDCDYID")
	            String bdck_zdbdcdyid=StringHelper.FormatByDatatype(jsonObject.get("ZDBDCDYID"));
	            String cardnum=GX_Util.deUnicode(StringHelper.FormatByDatatype(jsonObject.get("CARDNUM")));
	     
	      	  List<Map> builds = this.baseCommonDao.getDataListByFullSql("select \"Build_No\",\"Land_No\" from  EMFTRAN.\"Build\" where \"BuildCode\" in(select distinct \"RoomCode\" from EMFTRAN.\"Land\" where \"CardNum\"='"+cardnum+"')");
	      	killDoubleData(builds,"Build_No,Land_No","");
	      	for(Map build:builds){
				build.put("ZDBDCDYID", bdck_zdbdcdyid);
			}
	      	validateZrzs.addAll(builds);
		    }
		    killDoubleData(validateZrzs,"Build_No,Land_No","");
	      	util.AddPrefix(validateZrzs, FC_TABLE.EMFTRAN$BUILD);
	     
	           for(Map zrz_xzMap:validateZrzs){
	        	   String zdbdcdyid=StringHelper.FormatByDatatype(zrz_xzMap.get((FC_TABLE.EMFTRAN$BUILD.getFullName()+".ZDBDCDYID").toUpperCase()));
	        	BDCS_ZRZ_XZ zrz_xz=util.CreateSingleClass(BDCS_ZRZ_XZ.class, zrz_xzMap);
	        	   zrz_xz.setZDBDCDYID(zdbdcdyid);
	        	  
	        	   zrz_xz.setXMBH("第"+zrztime+"次抽取房产幢过来");
	        	   zrz_xz.setBZ("从房产库中抽取过来");
	        	   BDCS_ZRZ_LS zrz_ls=ObjectHelper.copyZRZ_XZToLS(zrz_xz);
	        	   baseCommonDao.save(zrz_xz);
	        	   baseCommonDao.save(zrz_ls);
	        	   n++;
	        	   System.out.print("成功插入第"+n+"条幢信息入现状库和历史库\n");
	              
	        	  
	           }
		    baseCommonDao.flush();  
		    System.out.print("共插入"+n+"幢信息，抽取时间段："+beforetime+"到"+StringHelper.FormatYmdhmsByDate(new Date())+"\n");
		  
		   
}
/**过滤重复数据
 * @param 数据列表
 * @param 验证码,格式:Build_No,Land_No
 * @param 过滤条件,格式:Build_No>0
 */
private void killDoubleData(List<Map> datas,String validate,String where)
{
	List<Map>  resultlist=new ArrayList<Map>();
	Map validateMap=new HashMap();
	String[] codes=validate.split("\\,");
	
	for(Map data:datas){
		StringBuilder validatekey=new StringBuilder();
		 for(String code:codes){
		   Object codevalue=data.get(code);
		   if(StringUtils.isEmpty(codevalue))
			validatekey.append("");
		   else
			validatekey.append(codevalue.toString().trim()); 
		 }
			if(!validateMap.containsKey(validatekey.toString()))
			{
				validateMap.put(validatekey.toString(), "");
				Boolean insertState=true;
				if(!StringUtils.isEmpty(where))
				{
					   String operate="";
			           if(where.contains(">"))
			        	   operate=">";
			           if(where.contains(">="))
			        	   operate=">=";
			           if(where.contains("="))
			        	   operate="=";
			           if(where.contains("<"))
			        	   operate="<";
			           if(where.contains("<="))
			        	   operate="<=";
			           String[] operates=where.split(operate);
			           if(operates.length==2)
			           {
			        	   String leftvalue=StringHelper.FormatByDatatype(data.get(operates[0].trim()));
			        	   String rightvalue=operates[1];
			        	   Boolean isNum=true;
			        	   if(rightvalue.contains("\\'")){
			        		   isNum=false;
			        		   rightvalue=rightvalue.replace("\\'", "");
			        	
			        	   }
			        	   else
			        	   {
			        		   if(StringUtils.isEmpty(leftvalue)||leftvalue.equals("null"))
			        		    	leftvalue="0";
			        		   if(StringUtils.isEmpty(rightvalue)||rightvalue.equals("null"))
			        		    	leftvalue="0";
			        		   isNum=true;
			        	   }
			        	     if(operate.equals("="))
			        	     {
			        	    	 if(isNum)
			        	    		 insertState=Double.parseDouble(leftvalue)==Double.parseDouble(rightvalue)?true:false;
			        	    	 else
			        	    		 insertState=leftvalue.equals(rightvalue)?true:false;
			        	     }
			        	     else
			        	     {
			        	    	 if(operate.equals(">"))
			        	    		 insertState=Double.parseDouble(leftvalue)>Double.parseDouble(rightvalue)?true:false;
			        	    	 if(operate.equals(">="))
				        	    	 insertState=Double.parseDouble(leftvalue)>=Double.parseDouble(rightvalue)?true:false;
				        	     if(operate.equals("<"))
					        	     insertState=Double.parseDouble(leftvalue)<Double.parseDouble(rightvalue)?true:false;
					        	 if(operate.equals("<="))
						        	 insertState=Double.parseDouble(leftvalue)<=Double.parseDouble(rightvalue)?true:false;
			        	     }
			           }
			        	   
				}
				//符合条件的才插入
				if(insertState)
				   resultlist.add(data);
			}
		}
	
       datas.clear();
	for(Map m:resultlist){
		datas.add(m);
	}
	}

/**wuzhu 合并权利人和共有权利人的不动产权证号
 * @param peoples
 * @param cu_peoples
 * @return
 */
private String getCombieBDCQZH(List<Map> peoples,List<Map> cu_peoples)
{
	StringBuilder resultString=new StringBuilder();
	Map validateMap=new HashMap();
	
	for(Map people:peoples){
		String bdcqzh=StringHelper.FormatByDatatype(people.get((FC_TABLE.EMFTRAN$PERSON.getFullName()+".ocertno").toUpperCase())).trim();
		 if(!validateMap.containsKey(bdcqzh)&&!StringUtils.isEmpty(bdcqzh))
		 {
			 validateMap.put(bdcqzh, "");
				 resultString.append(bdcqzh);
				 resultString.append(","); 
		 }
	}
	for(Map cu_people:cu_peoples){
		String bdcqzh=StringHelper.FormatByDatatype(cu_people.get((FC_TABLE.EMFTRAN$CU_PERSON.getFullName()+".Code").toUpperCase())).trim();
		if(!StringUtils.isEmpty(cu_people.get("Code")))
		   bdcqzh=cu_people.get("Code").toString();
		 if(!validateMap.containsKey(bdcqzh)&&!StringUtils.isEmpty(bdcqzh))
		 {
			 validateMap.put(bdcqzh, "");
			 resultString.append(bdcqzh);
			 resultString.append(","); 
				 
		 }
	}
	if(resultString.toString().contains(","))
       return resultString.toString().substring(0, resultString.toString().length()-1);
	 return "";
			
	}
@Override
public void UpdateZRZ(HttpServletRequest request, HttpServletResponse response) {
	// TODO Auto-generated method stub
	
}

@Override
public void ImportHouse(HttpServletRequest request, HttpServletResponse response) {
	String mapfilepath = String.format("%s\\resources\\FC2DJXT\\config\\柳江房产数据与登记系统数据映射关系.txt", request.getRealPath("/"));
	String beforetime=StringHelper.FormatYmdhmsByDate(new Date());
	int housetime=getAndUpdateTimeConfig(request,"H");
	//初始化工具类
	ConverterUtil util=new ConverterUtil(mapfilepath);
	//获取幢信息
	  List<Map> builds = this.baseCommonDao.getDataListByFullSql("select ZRZH,ZL,ZDBDCDYID,BDCDYID from bdck.bdcs_zrz_xz where bz='从房产库中抽取过来'");
	  killDoubleData(builds,"ZRZH,ZL","");
	  int n=0;
	  //String test="";
	  List<Map> validateHouses =new ArrayList<Map>();
	  for(Map buldmap:builds){
			
			String zrzh=null,zl=null,zdbdcdyid=null,zrzbdcdyid=null,zlvalue=null,zrzhvalue=null;
			
			zrzh=StringHelper.FormatByDatatype(buldmap.get("ZRZH")).trim();
			zl=StringHelper.FormatByDatatype(buldmap.get("ZL")).trim();
			zdbdcdyid=StringHelper.FormatByDatatype(buldmap.get("ZDBDCDYID"));
			zrzbdcdyid=StringHelper.FormatByDatatype(buldmap.get("BDCDYID"));
			//幢号和丘号为空的不抽取
			if(StringUtils.isEmpty(zl)||zl.equals("null")||StringUtils.isEmpty(zrzh)||zrzh.equals("null"))
			   continue;
			//获取房产户信息
			List<Map> houses_map = this.baseCommonDao.getDataListByFullSql("select \"logout_flag\",\"KeyCode\",\"RoomCode\",\"Land_No\",\"Room_No\",\"B_Area\",\"U_Area\",\"Set_Area\",\"CurFloor\",\"H_Use\",\"RType\",\"house_type\",\"AttachWay\",\"reast\",\"rsouth\",\"rwest\",\"rnorth\",\"House_Sit\",\"CurCell\",\"S_Area\",\"hvalue\",\"mark\" from  EMFTRAN.\"Room\" where ltrim(rtrim(\"Land_No\"))='"+
					zl+"' and ltrim(rtrim(\"Build_No\"))='"+zrzh+"'  and \"survey_flag\" <>2 order by \"KeyCode\" desc");
			killDoubleData(houses_map,"RoomCode","logout_flag=0");
			for(Map house:houses_map){
				house.put("ZRZBDCDYID", zrzbdcdyid);
				house.put("ZDBDCDYID", zdbdcdyid);
				
			}
			validateHouses.addAll(houses_map);	
		
	  }
		killDoubleData(validateHouses,"RoomCode","logout_flag=0");
		util.AddPrefix(validateHouses, FC_TABLE.EMFTRAN$ROOM);
			  for(Map house_map:validateHouses){
				  String zdbdcdyid=null,zrzbdcdyid=null;
				  zdbdcdyid=StringHelper.FormatByDatatype(house_map.get((FC_TABLE.EMFTRAN$ROOM.getFullName()+".ZDBDCDYID").toUpperCase()));
					zrzbdcdyid=StringHelper.FormatByDatatype(house_map.get((FC_TABLE.EMFTRAN$ROOM.getFullName()+".ZRZBDCDYID").toUpperCase()));  
					  n++;
					System.out.print("开始第"+n+"户信息插入\n");
				  //房屋号
				String roomcode=StringHelper.FormatByDatatype(house_map.get((FC_TABLE.EMFTRAN$ROOM.getFullName()+".RoomCode").toUpperCase())).trim();
		          //所有权证号
				Map owner=new HashMap(); 	 
				  //获取房屋权利人关联表
					 List<Map> owners = this.baseCommonDao.getDataListByFullSql("select \"KeyCode\",\"ocertno\",\"RoomCode\",\"oshare\",\"oSource\",\"oSort\",\"mark\"   from EMFTRAN.\"Owner\" where \"RoomCode\"='"+roomcode+"' and \"ocertno\" is not null order by \"KeyCode\" desc");
					 killDoubleData(owners,"RoomCode,ocertno","");
					 util.AddPrefix(owners, FC_TABLE.EMFTRAN$OWNER);
					//产权证号为空的不抽取
					 if(owners.size()>0)
						 owner=owners.get(0);
					 else
						 continue;
					 house_map.putAll(owner);
					 //------------房屋单元信息--------------------------------------------
					BDCS_H_XZ house_xz=util.CreateSingleClass(BDCS_H_XZ.class, house_map);
					house_xz.setId(roomcode);
					house_xz.setXMBH("第"+housetime+"次抽取房产户信息过来");
					house_xz.setZDBDCDYID(zdbdcdyid);
					house_xz.setZRZBDCDYID(zrzbdcdyid);
				
					BDCS_H_LS house_ls=ObjectHelper.copyH_XZToLS(house_xz);
					baseCommonDao.save(house_xz);
		        	baseCommonDao.save(house_ls);
		        	System.out.print("成功插入一条房产户信息到登记库户表的现状层和历史层\n");
		         //---------------登记单元信息-------------------------------------------------------
		        	BDCS_DJDY_XZ djdy_xz=new BDCS_DJDY_XZ();
		        	String djdyid=SuperHelper.GeneratePrimaryKey().toString();
		        	djdy_xz.setDJDYID(djdyid);
		        	djdy_xz.setBDCDYID(house_xz.getId());
		        	djdy_xz.setBDCDYLX(ConstValue.BDCDYLX.H.Value);
		        	djdy_xz.setLY(ConstValue.DJDYLY.XZ.Value);
		        	djdy_xz.setXMBH("第"+housetime+"次抽取房产户信息过来");
		        	 BDCS_DJDY_LS djdy_ls=ObjectHelper.copyDJDY_XZToLS(djdy_xz);
		        	   baseCommonDao.save(djdy_xz);
		        	   baseCommonDao.save(djdy_ls);
		        	   System.out.print("成功插入一条登记单元信息到登记库登记单元表的现状层和历史层\n");
				//-------------------------------------------------------------------------------------------
                     String ocertno=StringHelper.FormatByDatatype(owner.get((FC_TABLE.EMFTRAN$OWNER.getFullName()+".ocertno").toUpperCase())).trim();
                     if(StringUtils.isEmpty(ocertno)||ocertno.toString().equals("null"))
                    	 ocertno="";
		          //获取房产权利人信息
					  List<Map> peoples = this.baseCommonDao.getDataListByFullSql("select \"Cert_Type\",\"Cert_No\",\"Name\",\"Sex\",\"Place\",\"Address\",\"tel\",\"u_law_person\",\"ocertno\" from  EMFTRAN.\"person\" where ltrim(rtrim(\"ocertno\"))='"+ocertno+"'");
				 //获取房产共有人信息
					  List<Map> cu_peoples  = this.baseCommonDao.getDataListByFullSql("select \"Code\",\"ocertno\",\"Name\",\"CertName\",\"CertNO\",\"Share\",\"Tel\",\"Address\",\"Sex\" from  EMFTRAN.\"CU_Person\" where ltrim(rtrim(\"ocertno\"))='"+ocertno+"'");
					  killDoubleData(peoples,"Cert_No,Name","");
					  killDoubleData(cu_peoples,"ocertno,Name","");
					  util.AddPrefix(peoples, FC_TABLE.EMFTRAN$PERSON);
					  util.AddPrefix(cu_peoples, FC_TABLE.EMFTRAN$CU_PERSON);
				//------------------------权利附属权利信息-------------------------------------------------------------------
					  String bdcqzh=getCombieBDCQZH(peoples,cu_peoples);
					  BDCS_QL_XZ ql_xz=util.CreateSingleClass(BDCS_QL_XZ.class, house_map);
					  BDCS_FSQL_XZ fsql_xz=new BDCS_FSQL_XZ();
					  ql_xz.setDJDYID(djdyid);
					  ql_xz.setXMBH("第"+housetime+"次抽取房产户信息过来");
					  ql_xz.setFSQLID(fsql_xz.getId());
					  ql_xz.setBDCQZH(bdcqzh);
					  ql_xz.setQLLX(ConstValue.QLLX.GYJSYDSHYQ_FWSYQ.Value);
					  ql_xz.setDJLX(ConstValue.DJLX.CSDJ.Value);
					  ql_xz.setCZFS(ConstValue.CZFS.FBCZ.Value);
					  ql_xz.setZSBS(ConstValue.ZSBS.DYB.Value);
					  fsql_xz.setQLID(ql_xz.getId());
					  fsql_xz.setXMBH("第"+housetime+"次抽取房产户信息过来");
					  BDCS_QL_LS ql_ls=ObjectHelper.copyQL_XZToLS(ql_xz);
					  BDCS_FSQL_LS fsql_ls=ObjectHelper.copyFSQL_XZToLS(fsql_xz);
					  baseCommonDao.save(ql_xz);
		        	   baseCommonDao.save(fsql_xz);
					  baseCommonDao.save(ql_ls);
		        	   baseCommonDao.save(fsql_ls);
		        	   System.out.print("成功插入一条权利附属权利信息到登记库的现状层和历史层\n");
				//-----------------------------权利人信息-------------------------------------------------------------------	 
		        	   for(Map people_map:peoples)
					  {
						  //加入房屋数据用与映射关系
						  people_map.putAll(house_map);
						  //people_map.putAll(owner);   owner已经在 house_map 加有了
						  BDCS_QLR_XZ qlr_xz=util.CreateSingleClass(BDCS_QLR_XZ.class, people_map);
						  qlr_xz.setXMBH("第"+housetime+"次抽取房产户信息过来");
						  qlr_xz.setQLID(ql_xz.getId());
						  //----------证书信息-----------------------------------------------
						  BDCS_ZS_XZ zs_xz=util.CreateSingleClass(BDCS_ZS_XZ.class, people_map);
						  zs_xz.setXMBH("第"+housetime+"次抽取房产户信息过来");
						//----------权地证人信息-----------------------------------------------
						  BDCS_QDZR_XZ qdzr_xz=new BDCS_QDZR_XZ();
						  qdzr_xz.setXMBH("第"+housetime+"次抽取房产户信息过来");
						  qdzr_xz.setDJDYID(djdyid);
						  qdzr_xz.setQLID(ql_xz.getId());
						  qdzr_xz.setQLRID(qlr_xz.getId());
						  qdzr_xz.setZSID(zs_xz.getId());
						  qdzr_xz.setFSQLID(fsql_xz.getId());
						
						  BDCS_QLR_LS qlr_ls=ObjectHelper.copyQLR_XZToLS(qlr_xz);
						  BDCS_ZS_LS zs_ls=ObjectHelper.copyZS_XZToLS(zs_xz);
						  BDCS_QDZR_LS qdzr_ls=ObjectHelper.copyQDZR_XZToLS(qdzr_xz);
			        	   baseCommonDao.save(qlr_xz);
			        	   baseCommonDao.save(qlr_ls);
			        	   baseCommonDao.save(zs_xz);
			        	   baseCommonDao.save(zs_ls);
			        	   baseCommonDao.save(qdzr_xz);
			        	   baseCommonDao.save(qdzr_ls);
			        	 
					  }
					  if(peoples.size()>0)
					  System.out.print("成功插入"+peoples.size()+"条房产权利人到登记库权利人表的现状层和历史层并生成相应的证书和权地证人关系\n");
					  for(Map cu_people_map:cu_peoples)
					  {
						  //加入房屋数据用与映射关系
						  cu_people_map.putAll(house_map);
						 // cu_people_map.putAll(owner); owner已经在 house_map 加有了
						  BDCS_QLR_XZ qlr_xz=util.CreateSingleClass(BDCS_QLR_XZ.class, cu_people_map);
						  qlr_xz.setXMBH("第"+housetime+"次抽取房产户信息过来");
						  qlr_xz.setQLID(ql_xz.getId());
						  //----------证书信息-----------------------------------------------
						  BDCS_ZS_XZ zs_xz=util.CreateSingleClass(BDCS_ZS_XZ.class, cu_people_map);
						  zs_xz.setXMBH("第"+housetime+"次抽取房产户信息过来");
						//----------权地证人信息-----------------------------------------------
						  BDCS_QDZR_XZ qdzr_xz=new BDCS_QDZR_XZ();
						  qdzr_xz.setXMBH("第"+housetime+"次抽取房产户信息过来");
						  qdzr_xz.setDJDYID(djdyid);
						  qdzr_xz.setQLID(ql_xz.getId());
						  qdzr_xz.setQLRID(qlr_xz.getId());
						  qdzr_xz.setZSID(zs_xz.getId());
						  qdzr_xz.setFSQLID(fsql_xz.getId());
						  //qlr_xz.setGYFS(ConstValue.GYFS.GTGY.Value);
						  BDCS_QLR_LS qlr_ls=ObjectHelper.copyQLR_XZToLS(qlr_xz);
						  BDCS_ZS_LS zs_ls=ObjectHelper.copyZS_XZToLS(zs_xz);
						  BDCS_QDZR_LS qdzr_ls=ObjectHelper.copyQDZR_XZToLS(qdzr_xz);
			        	   baseCommonDao.save(qlr_xz);
			        	   baseCommonDao.save(qlr_ls);
			        	   baseCommonDao.save(zs_xz);
			        	   baseCommonDao.save(zs_ls);
			        	   baseCommonDao.save(qdzr_xz);
			        	   baseCommonDao.save(qdzr_ls);
			        	 
					  }
					  if(cu_peoples.size()>0)
						  System.out.print("成功插入"+cu_peoples.size()+"条房产共有权利人到登记库权利人表的现状层和历史层并生成相应的证书和权地证人关系\n");
					  System.out.print("结束第"+n+"户信息插入\n");
					  
			  }
			  baseCommonDao.flush();
			  System.out.print("共插入"+n+"户信息，抽取时间段："+beforetime+"到"+StringHelper.FormatYmdhmsByDate(new Date())+"\n");
			  
}

@Override
public void UpdateHouse(HttpServletRequest request, HttpServletResponse response) {
	// TODO Auto-generated method stub
	
}

@Override
public void ImportQL(HttpServletRequest request, HttpServletResponse response) {
	// TODO Auto-generated method stub
	
}

@Override
public void UpdateQL(HttpServletRequest request, HttpServletResponse response) {
	// TODO Auto-generated method stub
	
}

@Override
public void ImportZS(HttpServletRequest request, HttpServletResponse response) {
	// TODO Auto-generated method stub
	
}

@Override
public void UpdateZS(HttpServletRequest request, HttpServletResponse response) {
	// TODO Auto-generated method stub
	
}

@Override
public void ImportQLR(HttpServletRequest request, HttpServletResponse response) {
	// TODO Auto-generated method stub
	
}

@Override
public void UpdateQLR(HttpServletRequest request, HttpServletResponse response) {
	// TODO Auto-generated method stub
	
}

@Override
public void ImportQDZR(HttpServletRequest request, HttpServletResponse response) {
	// TODO Auto-generated method stub
	
}

@Override
public void UpdateQDZR(HttpServletRequest request, HttpServletResponse response) {
	// TODO Auto-generated method stub
	
}
@Override
public void CreateZDToFcMap(HttpServletRequest request,
		HttpServletResponse response) {
	String zdmapfilepath = String.format("%s\\resources\\FC2DJXT\\config\\柳江房产数据宗地与登记系统宗地映射关系.txt", request.getRealPath("/"));
	 List<Map> result=new ArrayList<Map>(); 
	//获取房产土地的KEYCODE和登记库的DJDYID映射
	  List<Map> keycodeToDjdyids = this.baseCommonDao.getDataListByFullSql(" select distinct land.\"CardNum\" AS CARDNUM,ql.DJDYID from  \"EMFTRAN\".\"Land\" land inner join bdck.bdcs_ql_xz ql on land.\"CardNum\"=ql.bdcqzh ");
	  if(keycodeToDjdyids.size()==0)
	      return;
	  for(Map keycodeToDjdyid:keycodeToDjdyids)
	  {
		  Map newmap=new HashMap();
		  String djdyid=String.valueOf(keycodeToDjdyid.get("DJDYID"));
		  String zdbdcdyid=null;
		  
		 List<BDCS_DJDY_XZ> djdys= baseCommonDao.getDataList(BDCS_DJDY_XZ.class, "DJDYID='"+djdyid+"'");
		  if(djdys.size()==0)
			  continue;
		  BDCS_DJDY_XZ djdy=djdys.get(0);
		  if(djdy.getBDCDYLX().equals("02"))
			  zdbdcdyid=djdy.getBDCDYID();
		  else if(djdy.getBDCDYLX().equals("031"))
		  {
			  List<BDCS_H_XZ> houses= baseCommonDao.getDataList(BDCS_H_XZ.class, "id='"+djdy.getBDCDYID()+"'");
			  if(houses.size()==0)
				  continue;
			  BDCS_H_XZ house=houses.get(0);
			  zdbdcdyid=house.getZDBDCDYID();
		  }
		  else
			  continue;
		  
		  newmap.put("CARDNUM",GX_Util.enUnicode(StringHelper.FormatByDatatype(keycodeToDjdyid.get("CARDNUM"))));//将汉字转16进制
		  newmap.put("ZDBDCDYID", zdbdcdyid);
		  result.add(newmap);
	  }
	  if(result.size()==0)
	      return;
	   JSONArray jsonArray = JSONArray.fromObject(result);
	  String jsonString=jsonArray.toString();
	  FileOutputStream fop = null;
		File file = null;
		try {
			file = new File(zdmapfilepath);
			if (file.exists()) {
				fop = new FileOutputStream(file);
		
				 
				byte[] contentInBytes =  jsonString.getBytes();
				fop.write(contentInBytes);
			}
		  System.out.println("成功生成宗地和房产映射关系"+result.size()+"条");
		} catch (IOException e) {
	       System.out.println(e.getMessage());
		} finally {
			try {
				if (fop != null){
					fop.close();
					
				}
			} catch (IOException e) {
				 System.out.println(e.getMessage());
			}
		}
			  
}
private int getAndUpdateTimeConfig(HttpServletRequest request,String type)
{
	String configfilepath = String.format("%s\\resources\\FC2DJXT\\config\\柳江房产数据抽取次数.txt", request.getRealPath("/"));
	String json = StringHelper.readFile(configfilepath);
	JSONObject timeobj = JSONObject.fromObject(json);
	int zrztime=Integer.parseInt(timeobj.get("ZRZTIME").toString());
	int htime=Integer.parseInt(timeobj.get("HTIME").toString());
	if(type.toUpperCase().equals("ZRZ"))
		zrztime++;
	if(type.toUpperCase().equals("H"))
		htime++;
	Map r=new HashMap();
	r.put("ZRZTIME", zrztime);
	r.put("HTIME", htime);
	JSONObject jsonobj = JSONObject.fromObject(r);
	  String jsonString=jsonobj.toString();
	  FileOutputStream fop = null;
		File file = null;
		try {
			file = new File(configfilepath);
			if (file.exists()) {
				fop = new FileOutputStream(file);
		
				 
				byte[] contentInBytes =  jsonString.getBytes();
				fop.write(contentInBytes);
			}
		  System.out.println("更新次数");
		} catch (IOException e) {
	       System.out.println(e.getMessage());
		} finally {
			try {
				if (fop != null){
					fop.close();
					
				}
			} catch (IOException e) {
				 System.out.println(e.getMessage());
			}
		}
		if(type.toUpperCase().equals("ZRZ"))
			return zrztime;
		if(type.toUpperCase().equals("H"))
			return htime;
		return 0;
}
  

}