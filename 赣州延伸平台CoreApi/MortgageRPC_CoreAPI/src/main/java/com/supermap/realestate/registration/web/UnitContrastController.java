package com.supermap.realestate.registration.web;

import com.supermap.realestate.registration.model.BDCS_DJDY_GZ;
import com.supermap.realestate.registration.model.BDCS_DJDY_LS;
import com.supermap.realestate.registration.model.BDCS_XMXX;
import com.supermap.realestate.registration.model.interfaces.RealUnit;
import com.supermap.realestate.registration.model.interfaces.Rights;
import com.supermap.realestate.registration.model.interfaces.RightsHolder;
import com.supermap.realestate.registration.model.interfaces.SubRights;
import com.supermap.realestate.registration.tools.HistoryBackdateTools;
import com.supermap.realestate.registration.tools.RightsHolderTools;
import com.supermap.realestate.registration.tools.RightsTools;
import com.supermap.realestate.registration.util.ConstHelper;
import com.supermap.realestate.registration.util.ConstValue;
import com.supermap.realestate.registration.util.RequestHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import net.sf.json.JSONObject;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.beans.PropertyDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.*;

/**
 * 单元信息对比类
 * @author shb
 *
 */
@Controller
@RequestMapping("/unitcontrast")
public class UnitContrastController {

	@Autowired
	private CommonDao basecommondao;

	/***************************************** 单元模块信息对比 *****************************************/
	
	/**
	 * 单元信息对比服务
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings({ "rawtypes" })
	@RequestMapping(value = "/unitcontrast/", method = RequestMethod.GET)	
	public @ResponseBody HashMap unitContrastInfo(HttpServletRequest request,
			HttpServletResponse response){
		HashMap unitMap=new HashMap<String, ArrayList>();
		String bdcdyid = "";
		try {
			bdcdyid = RequestHelper.getParam(request, "bdcdyid");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String bdcdylx = "";
		try {
			bdcdylx = RequestHelper.getParam(request, "bdcdylx");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String djdyly = "";
		try {
			djdyly = RequestHelper.getParam(request, "djdyly");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		if ("ls".equals(djdyly)) {
			djdyly = "03";
		} else if ("xz".equals(djdyly)) {
			djdyly = "02";
		} else if ("gz".equals(djdyly)) {
			djdyly = "01";
		}

		String pageid = "";
		try {
			pageid = RequestHelper.getParam(request, "pageid");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		String xmbh = "";
		try {
			xmbh = RequestHelper.getParam(request, "xmbh");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		if (StringHelper.isEmpty(bdcdyid)) {
			return unitMap;
		}
		if (StringHelper.isEmpty(djdyly)) {
			return unitMap;
		}
		if (StringHelper.isEmpty(bdcdylx)) {
			return unitMap;
		}
		unitMap=unitContrast(xmbh,bdcdyid,djdyly,bdcdylx,pageid);		
		return unitMap;
	}
	/**
	 * 对比单元信息记录
	 * @param xmbh:项目编号
	 * @param bdcdyid:不动产单元ID
	 * @param djdyly:登记单元来源
	 * @param bdcdylx:不动产单元类型
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public HashMap unitContrast(String xmbh,String bdcdyid,String djdyly,String bdcdylx,String pageid){
		HashMap unitMap=new HashMap<String, ArrayList>();	
		//当前项目的单元信息
		HashMap<String, RealUnit> currentmodifyunit=getCurrentModifyUnit(xmbh,bdcdyid);
		//上一首单元信息
		HashMap<String,RealUnit> previousmodifyunit=getPreviousModifyUnit(xmbh,bdcdyid,bdcdylx);
		//单元信息对比
		unitMap=uintInfoContrastResult(previousmodifyunit,currentmodifyunit,pageid);
		//单元信息排序
		unitMap=sortUnit(unitMap,pageid);
		return unitMap;
	}
	
	/**
	 * 获取当前的单元信息
	 * @param bdcdyid:不动产单元ID
	 * @param xmbh:项目编号
	 * @return
	 */
	@SuppressWarnings({ "rawtypes" })
	private HashMap<String, RealUnit> getCurrentModifyUnit(String xmbh,String bdcdyid){
		HashMap<String, RealUnit> modifyunitafter=new HashMap<String, RealUnit>();
		String qlid="";
		String fulSql= " SELECT QLID,DJDY.LY FROM BDCK.BDCS_QL_GZ QL "
				     + " LEFT JOIN BDCK.BDCS_DJDY_GZ DJDY ON DJDY.DJDYID = QL.DJDYID "
				     + " WHERE DJDY.BDCDYID='" + bdcdyid + "' "
				     + " AND QL.XMBH='" + xmbh + "' AND DJDY.XMBH='" + xmbh + "'";
		List<Map>  lstmap= basecommondao.getDataListByFullSql(fulSql);
		if(lstmap !=null && lstmap.size()>0){
			qlid=StringHelper.FormatByDatatype(lstmap.get(0).get("QLID"));
		}
		if(StringHelper.isEmpty(qlid)){
			return modifyunitafter;
		}
		 modifyunitafter=HistoryBackdateTools.QueryUnitInfoByQlid(qlid);		
		return modifyunitafter;
	}
	
	/**
	 * 上一首单元信息
	 * @param currentxmbh：当前项目编号
	 * @param bdcdyid：不动产单元ID
	 * @param bdcdylx：不动产单元类型
	 * @return
	 */
	private HashMap<String,RealUnit> getPreviousModifyUnit(String currentxmbh,String bdcdyid,String bdcdylx){
		HashMap<String,RealUnit> previousmodifyunit= new HashMap<String, RealUnit>();//上一首的单元信息
		String qlid=HistoryBackdateTools.getPreviousQLID(bdcdyid, bdcdylx, currentxmbh);
		previousmodifyunit=HistoryBackdateTools.QueryUnitInfoByQlid(qlid);
		return previousmodifyunit;
	}
	
	/**
	 * 获取修改前后单元信息对比结果
	 * @param previousmodifyunit：上一首单元信息
	 * @param currentmodifyunit：当前单元信息
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private HashMap uintInfoContrastResult(HashMap<String,RealUnit> previousmodifyunit,HashMap<String, RealUnit> currentmodifyunit,String pageid){
		HashMap unitMap=new HashMap<String, ArrayList>();
		if(previousmodifyunit !=null && previousmodifyunit.size()>0){			
			for(Map.Entry<String, RealUnit> entry :previousmodifyunit.entrySet()){
				String bdcdylx=entry.getKey();
				if(!StringHelper.isEmpty(bdcdylx) && currentmodifyunit.containsKey(bdcdylx)){
					RealUnit new_unit=currentmodifyunit.get(bdcdylx);
					RealUnit old_unit=entry.getValue();
					ArrayList list=new ArrayList<JSONObject>();
//					List<T_UNIT_FIELD> lstFields=basecommondao.getDataList(T_UNIT_FIELD.class, "BDCDYLX='"+bdcdylx+"'");
					String fulSql=" SELECT M.FIELDNAME,M.FIELDDESCRIPTION,M.FIELDTYPE,M.NUMBER_L,M.NUMBER_R,M.FIELDOPTION FROM BDCK.RT_UNIT_MODULEMANAGER M LEFT JOIN "+
							" BDCK.RT_UNIT_PAGEMANAGER  P ON P.MODULEID=M.MODULEID "+
							" WHERE M.VISIBLE ='1' AND P.PAGEID='"+pageid+"'  AND M.BDCDYLX='"+bdcdylx+"' "+
							" ORDER BY P.SXH";	
					List<Map> lstFields=basecommondao.getDataListByFullSql(fulSql);
					List<String> lstf=new ArrayList<String>();
					if(!lstFields.isEmpty()  && lstFields.size()>0){
						List<String> lst= getFieldValueByName(new_unit);
						for(Map field :lstFields){
							 String fieldName=StringHelper.FormatByDatatype(field.get("FIELDNAME"));//field.getFIELDNAME();//字段名称
							 if(lstf.contains(fieldName)){
								 continue;
							 }
							 lstf.add(fieldName);
							 String fieldChinaName=StringHelper.FormatByDatatype(field.get("FIELDDESCRIPTION"));//field.getFIELDDESCRIPTION();//字段中文名称
							 String fieldType=StringHelper.FormatByDatatype(field.get("FIELDTYPE"));//field.getFIELDTYPE();//字段类型
							 String fieldOption=StringHelper.FormatByDatatype(field.get("FIELDOPTION"));//常量类型
							 int fieldNumberL=StringHelper.getInt(field.get("NUMBER_L"));//
							 int fieldNumberR=StringHelper.getInt(field.get("NUMBER_R"));//
							 Object obj_before_value=null;//修改前的值-对象
							 Object obj_after_value=null;//修改后的值-对象
							 String before_value="";//修改前的值-字符串
							 String after_value="";//修改后的值-字符串	
							 if(lst.equals(fieldName.toLowerCase())){
								 continue;
							 }
							 obj_after_value=getFieldValueByName(fieldName,new_unit);
							 obj_before_value=getFieldValueByName(fieldName,old_unit);
								if ("text".equals(fieldType) || "combox".equals(fieldType)) {
									before_value=StringHelper.FormatByDatatype(obj_before_value);
									after_value=StringHelper.FormatByDatatype(obj_after_value);
									if("combox".equals(fieldType))
									{
										if(!StringHelper.isEmpty(before_value))
										{
											before_value=ConstHelper.getNameByValue(fieldOption, before_value);
										}
										if(!StringHelper.isEmpty(after_value))
										{
											after_value=ConstHelper.getNameByValue(fieldOption, after_value);
										}
									}
								}else if ("number".equals(fieldType)) {
									after_value=FormatDouble(obj_after_value,fieldNumberL,fieldNumberR);
									before_value=FormatDouble(obj_before_value,fieldNumberL,fieldNumberR);
								} else if ("date".equals(fieldType) || fieldType.contains("-")) {
									try {
										before_value=StringHelper.FormatDateOnType(
												StringHelper.FormatByDate(obj_before_value, "yyyy-MM-dd"),
												"yyyy-MM-dd");
										after_value=StringHelper.FormatDateOnType(
												StringHelper.FormatByDate(obj_after_value, "yyyy-MM-dd"),
												"yyyy-MM-dd");
									} catch (ParseException e) {
										e.printStackTrace();
									}									
								}
							 if(!after_value.equals(before_value)){
								 JSONObject jsonField_Value=new JSONObject();
								 jsonField_Value.put("原"+fieldChinaName, before_value);
								 jsonField_Value.put("新"+fieldChinaName, after_value);
								 list.add(jsonField_Value);
							 }							 
						}
					}
					if(!list.isEmpty())
					{
						unitMap.put(bdcdylx, list);
					}
				}				
			}
		}
		return unitMap;
	}
	
	/**
	 * 按页面模块定义的方式给对比结果单元排序
	 * @param resultUnit :对比结果单元
	 * @param pageid：页面模块ID
	 * @return
	 */
	@SuppressWarnings({ "rawtypes" })
	private HashMap<String, ArrayList> sortUnit(HashMap<String, ArrayList>  resultUnit,String pageid){
		HashMap<String, ArrayList> unitMap=new HashMap<String, ArrayList>();
		String sql="SELECT P.SXH,P.TITLE,M.BDCDYLX,M.NAME FROM BDCK.T_UNIT_MODULE M "
				+" LEFT JOIN BDCK.RT_UNIT_PAGEMANAGER P ON P.MODULEID=M.ID "
				+" WHERE P.PAGEID='"+pageid+"' ORDER BY P.SXH ";
		List<String> lst=new ArrayList<String>();
		if(resultUnit !=null && resultUnit.size()>0){
			List<Map> lstmap=basecommondao.getDataListByFullSql(sql);
			if(lstmap!=null && lstmap.size()>0){
				for(Map m: lstmap){
					String title=StringHelper.FormatByDatatype(m.get("TITLE"));
					String bdcdylx=StringHelper.FormatByDatatype(m.get("BDCDYLX"));
					for(Map.Entry<String,ArrayList> key_value:resultUnit.entrySet()){
						if(bdcdylx.equals(key_value.getKey())){
							if(!lst.contains(key_value.getKey())){
								unitMap.put(title+"对比结果", key_value.getValue());
								lst.add(key_value.getKey());
							}
							
						}						
					}
				}
			}	
		}			
		return unitMap;
	}
	
	/**
	 * 根据属性名获取属性值
	 */
	private Object getFieldValueByName(String fieldName, Object o) {
		return HistoryBackdateTools.getFieldValueByName(fieldName, o);
	}
	
	/**
	 * 获取通过注解声明的字段集合
	 * @param 
	 * @return
	 */
	private List<String> getFieldValueByName(Object o) {
		List<String> lst=new ArrayList<String>();
		try {
			Field [] fs= o.getClass().getDeclaredFields();
			if(fs !=null && fs.length>0){
				 for(Field f:fs){
					 String name=f.getName().toLowerCase();
					 lst.add(name);
				 }
			}
		} catch (Exception e) {
		}
		return lst;
	}
	
	 private String FormatDouble(Object obj, Integer number_L, Integer number_R) {
		String value = "";
		if (!StringHelper.isEmpty(obj)) {
			Double d = StringHelper.getDouble(obj);
			if (d == 0) {
				return "";
			}
			String formattype = "";
			if (!StringHelper.isEmpty(number_L) && number_L > 0) {
				for (int i = 0; i < number_L; i++) {
					if (i == number_L - 1) {
						formattype = formattype + "0";
					} else {
						formattype = formattype + "#";
					}
				}
			}
			if (!StringHelper.isEmpty(number_R) && number_R > 0) {
				formattype = formattype + ".";
				for (int i = 0; i < number_R; i++) {
					formattype = formattype + "0";
				}
			}
			if (StringHelper.isEmpty(formattype)) {
				formattype = "##################.###";
			}
			DecimalFormat df = new DecimalFormat(formattype);
			df.setRoundingMode(RoundingMode.HALF_UP);
			value = df.format(d);
		}
		return value;
	}
	 
	/***************************************** 单元模块信息对比 *****************************************/

	/*****************************************start: 使用日志查询的单元相关信息对比定义的部分功能定制柳州的单元信息对比模块 *****************************************/
	/**
	 * 单元信息对比服务
	 *
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/getUnitContrastInfo", method = RequestMethod.GET)
	public @ResponseBody Map getunitContrastInfo(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException{
		String djdyid = RequestHelper.getParam(request, "djdyid");
		String bdcdyid = RequestHelper.getParam(request, "bdcdyid");
		String bdcdylx = RequestHelper.getParam(request, "bdcdylx");
		String ly = RequestHelper.getParam(request, "ly");
		//String xmbh = RequestHelper.getParam(request, "xmbh");
		String qlid = RequestHelper.getParam(request, "qlid");
		String lyqlid= RequestHelper.getParam(request, "lyqlid");
		Rights nowright = RightsTools.loadRights(ConstValue.DJDYLY.GZ, qlid);
		String xmbh = "";
		if(!StringHelper.isEmpty(nowright.getXMBH())){
			xmbh = nowright.getXMBH();
		}
		List<BDCS_DJDY_GZ> DJDY = basecommondao.getDataList(BDCS_DJDY_GZ.class, "XMBH='"+xmbh+"'");
		boolean lyqlidz=false;
		String lyid = null;
		if(StringHelper.isEmpty(lyqlid)){
			if(nowright.getLYQLID()==null||nowright.getLYQLID().equals("")){//若是没有获取到lyqlid
				lyqlidz=true;//判断是否要把lyqlid存入ql表
				lyqlid=HistoryBackdateTools.getPreviousQLID(bdcdyid, bdcdylx, xmbh);//获取上一手qlid

				boolean lxcontranst = true;
				while(lxcontranst){//获取qllx相同的上一手数据进行对比
					Rights oldrights = RightsTools.loadRights(ConstValue.DJDYLY.LS, lyqlid);
					if(oldrights==null){
						System.out.println("lyqlid "+lyqlid+"在权利表历史层无数据");
					}
					if(nowright.getQLLX().equals(oldrights.getQLLX())||nowright.getQLLX()!="23"||nowright.getDJLX().equals(oldrights.getDJLX())){
						lxcontranst=false;
					}else{
						if(oldrights.getLYQLID()!=null||!oldrights.getLYQLID().equals("")){
							lyqlid=oldrights.getLYQLID();

						}else{
							lyid = lyqlid;
							List<BDCS_DJDY_LS> djdy_ls=basecommondao.getDataList(BDCS_DJDY_LS.class, "DJDYID'"+oldrights.getDJDYID()+"'");
							lyqlid=HistoryBackdateTools.getPreviousQLID(djdy_ls.get(0).getBDCDYID(),djdy_ls.get(0).getBDCDYLX() , djdy_ls.get(0).getXMBH());
						}
					}
					if(lyqlid==null||lyqlid.equals("")){
						lyqlid = lyid ;
						lxcontranst=false;

					}
				}
			}else{
				lyqlid = nowright.getLYQLID();
			}
		}else{
			lyqlid = RequestHelper.getParam(request, "lyqlid");
		}

		Map result = new HashMap();

		String djdyly = "";
		if ("ls".equals(ly)) {
			djdyly = "03";
		} else if ("xz".equals(ly)) {
			djdyly = "02";
		} else {
			djdyly = "01";
		}

		//获取单元信息
		Map baseinfo = new HashMap();

		//now
		HashMap<String, RealUnit> dy_now = HistoryBackdateTools.QueryUnitInfoByQlid(qlid);
		baseinfo.put("newvalue", beanToMap(dy_now.get(bdcdylx)));

		//before

		HashMap<String, RealUnit> dy_pre = HistoryBackdateTools.QueryUnitInfoByQlid(lyqlid);
		if (dy_pre == null) {

		}
		baseinfo.put("oldvalue", beanToMap(dy_pre.get(bdcdylx)));

		result.put("baseinfo",baseinfo);


		//获取权利信息
		Map right = new HashMap();
		List<Map> rights = new ArrayList<Map>();

		Map mortgage = new HashMap();
		List<Map> mortgages = new ArrayList<Map>();

		Map seal = new HashMap();
		List<Map> seals = new ArrayList<Map>();

		//now
		List<String> cqlx = new ArrayList<String>(Arrays.asList("1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","24") );
		Rights nowrights = RightsTools.loadRights(ConstValue.DJDYLY.GZ, qlid);
		if(lyqlidz){
			nowrights.setLYQLID(lyqlid);
			basecommondao.update(nowrights);}
		BDCS_XMXX xmxx = basecommondao.get(BDCS_XMXX.class, nowrights.getXMBH());
		List<Rights> rights_now = RightsTools.loadRightsByCondition(ConstValue.DJDYLY.GZ, "xmbh='"+xmxx.getId()+"' and djdyid='" + djdyid + "'");
		if (rights_now != null && rights_now.size()>0) {
			for (Rights right_now : rights_now) {
				boolean hasCq = false;
				Map qlMap_now =new HashMap();
				SubRights nowfsql = RightsTools.loadSubRightsByRightsID(ConstValue.DJDYLY.GZ, right_now.getId());
				List<RightsHolder> holders_now = RightsHolderTools.loadRightsHolders(ConstValue.DJDYLY.GZ, right_now.getId());
				String qllx_now = right_now.getQLLX();
				qlMap_now = beanToMap(right_now);
				qlMap_now.put("fsql", beanToMap(nowfsql));
				List<Map> holdersTrans_now_temp = new ArrayList<Map>();
				for (RightsHolder holder : holders_now) {
					Map holderMap = new HashMap();
					holderMap = beanToMap(holder);
					holdersTrans_now_temp.add(holderMap);
				}

				//before
				Map qlMap_pre =new HashMap();
				List<Map> holdersTrans_pre = new ArrayList<Map>();
				List<Map> holdersTrans_now = new ArrayList<Map>();

				String qlid_pre = right_now.getLYQLID();
				if (!StringHelper.isEmpty(qlid_pre)) {
					Rights  right_pre = RightsTools.loadRights(ConstValue.DJDYLY.LS, qlid_pre);
					if (!StringHelper.isEmpty(right_pre.getId())) {
						String xmbh_pre = "";
						if (!StringHelper.isEmpty(right_pre.getXMBH())) {
							BDCS_XMXX xmxx_pre = basecommondao.get(BDCS_XMXX.class, right_pre.getXMBH());
							xmbh_pre = xmxx_pre.getDJLX();
						}
						SubRights fsql_pre = RightsTools.loadSubRightsByRightsID(ConstValue.DJDYLY.LS, right_pre.getId());
						List<RightsHolder> holders_pre = RightsHolderTools.loadRightsHolders(ConstValue.DJDYLY.LS, right_pre.getId());

						String qllx_pre = right_pre.getQLLX();
						qlMap_pre = beanToMap(right_pre);

						qlMap_pre.put("fsql", beanToMap(fsql_pre));

						for (RightsHolder holder : holders_pre) {
							Map holderMap = new HashMap();
							holderMap = beanToMap(holder);
							holdersTrans_pre.add(holderMap);

							//将新增加的hodles的顺序改成和pre一致——顺序与权利人顺序一致
							boolean sortflag = false;
							int i = 0;
							for (i = 0; i < holdersTrans_now_temp.size(); i++) {
								Map holder_now_temp = holdersTrans_now_temp.get(i);
								if (holder_now_temp.get("qlrmc").equals(holderMap.get("qlrmc"))) {
									holdersTrans_now.add(holder_now_temp);
									sortflag = true;
									break;
								}
							}
							if (sortflag) {
								holdersTrans_now_temp.remove(i);
							}

						}
						qlMap_pre.put("holders",holdersTrans_pre);

						if ("23".equals(qllx_pre)) {
							mortgage.put("oldvalue",qlMap_pre);
						}if ("99".equals(qllx_pre) && "900".equals(xmbh_pre)) {
							seal.put("oldvalue",qlMap_pre);
						}else if (cqlx.contains(qllx_pre)) {
							right.put("oldvalue", qlMap_pre);

							if (!cqlx.contains(qllx_now)) {//if(!hasCq)
								right.put("newvalue", qlMap_pre);
							}
						}
					}
				}

				holdersTrans_now.addAll(holdersTrans_now_temp);
				qlMap_now.put("holders",holdersTrans_now);

				if ("23".equals(qllx_now)) {
					mortgage.put("newvalue",qlMap_now);
				}if ("99".equals(qllx_now) && "900".equals(xmxx.getDJLX())) {
					seal.put("newvalue",qlMap_now);
				}else if (cqlx.contains(qllx_now)) {
					right.put("newvalue", qlMap_now);
//					hasCq = true;
				}

				if (right != null && right.size() > 0) {
					rights.add(right);

				}
				if (mortgage != null && mortgage.size() > 0) {
					mortgages.add(mortgage);
				}
				if (seal != null && seal.size() > 0) {
					seals.add(seal);
				}
			}
		}

		//before
//		if (!StringHelper.isEmpty(lyqlid)) {
//			Rights prerights = RightsTools.loadRights(DJDYLY.LS, lyqlid);
//			if (prerights != null) {
//				if (!StringHelper.isEmpty(prerights.getXMBH())) {
//					BDCS_XMXX xmxx_pre = basecommondao.get(BDCS_XMXX.class, prerights.getXMBH());
//					List<Rights> rights_pre = RightsTools.loadRightsByCondition(DJDYLY.LS, "xmbh='"+xmxx_pre.getId()+"' and djdyid='" + djdyid + "'");
//					if (rights_pre != null && rights_pre.size()>0) {
//						for (Rights right_pre : rights_pre) {
//							Map qlMap =new HashMap();
//							SubRights nowfsql = RightsTools.loadSubRightsByRightsID(DJDYLY.LS,right_pre.getId());
//							List<RightsHolder> holders = RightsHolderTools.loadRightsHolders(DJDYLY.LS, right_pre.getId());
//							String qllx = right_pre.getQLLX();
//
//							qlMap = beanToMap(right_pre);
//
//							qlMap.put("fsql", beanToMap(nowfsql));
//
//							List<Map> holdersTrans = new ArrayList<Map>();
//							for (RightsHolder holder : holders) {
//								Map holderMap = new HashMap();
//								holderMap = beanToMap(holder);
//								holdersTrans.add(holderMap);
//							}
//							qlMap.put("holders",holdersTrans);
//
//							if ("23".equals(qllx)) {
//								mortgage.put("oldvalue",qlMap);
//							}if ("99".equals(qllx) && "900".equals(xmxx.getDJLX())) {
//								seal.put("oldvalue",qlMap);
//							}else if (cqlx.contains(qllx)) {
//								right.put("oldvalue", qlMap);
//							}
//						}
//					}
//				}else{
//					Map qlMap =new HashMap();
//					SubRights nowfsql = RightsTools.loadSubRightsByRightsID(DJDYLY.LS,prerights.getId());
//					List<RightsHolder> holders = RightsHolderTools.loadRightsHolders(DJDYLY.LS, prerights.getId());
//					String qllx = prerights.getQLLX();
//
//					qlMap = beanToMap(prerights);
//
//					qlMap.put("fsql", beanToMap(nowfsql));
//
//					List<Map> holdersTrans = new ArrayList<Map>();
//					for (RightsHolder holder : holders) {
//						Map holderMap = new HashMap();
//						holderMap = beanToMap(holder);
//						holdersTrans.add(holderMap);
//					}
//					qlMap.put("holders",holdersTrans);
//
//					if ("23".equals(qllx)) {
//						mortgage.put("oldvalue",qlMap);
//					}if ("99".equals(qllx) && "900".equals(xmxx.getDJLX())) {
//						seal.put("oldvalue",qlMap);
//					}else if (cqlx.contains(qllx)) {
//						right.put("oldvalue", qlMap);
//					}
//				}
//			}
//
//		}
		if (rights != null && rights.size() > 0) {
			result.put("rights",rights);
		}
		if (mortgages != null && mortgages.size() > 0) {
			result.put("mortgages",mortgages);
		}
		if (seals != null && seals.size() > 0) {
			result.put("seals",seals);
		}
		basecommondao.flush();
		return result;
	}
	/**
	 * 单元信息对比服务
	 *
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/getUnitContrastInfoNVN/{xmbh}", method = RequestMethod.GET)
	public @ResponseBody Map getunitContrastInfoNVN(@PathVariable("xmbh") String xmbh, HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException{
		List<String> bgqbdcdyid =  new ArrayList<String>();
		List<String> bghbdcdyid =  new ArrayList<String>();
		List<String> bgqqlid =  new ArrayList<String>();
		List<String> bghqlid =  new ArrayList<String>();
		List<String> bgqdjdyid =  new ArrayList<String>();
		List<String> bghdjdyid =  new ArrayList<String>();
		String bgqdjdyly = "";
		String bghdjdyly = "";
		String bgqbdcdylx = "";
		String bghbdcdylx = "";
		List<BDCS_DJDY_GZ> DJDY = basecommondao.getDataList(BDCS_DJDY_GZ.class, "XMBH='"+xmbh+"'");
		for(BDCS_DJDY_GZ djdygz :DJDY){
			if(djdygz.getLY()=="02"||"02".equals(djdygz.getLY())||djdygz.getLY()=="03"||"03".equals(djdygz.getLY())){//xz
				bgqbdcdyid.add(djdygz.getBDCDYID());
				bgqdjdyid.add(djdygz.getDJDYID());
				bgqdjdyly = "02";
				//	List<Rights> list_right_ls=RightsTools.loadRightsByCondition(DJDYLY.LS, "DJDYID='"+djdygz.getDJDYID()+"' ");
				bgqbdcdylx = djdygz.getBDCDYLX();
				List<Rights> list_right_ls=RightsTools.loadRightsByCondition(ConstValue.DJDYLY.LS, "DJDYID='"+djdygz.getDJDYID()+"' AND BDCDYID='"+djdygz.getBDCDYID()+"'");
				if(list_right_ls!=null&&list_right_ls.size()>0){
					bgqqlid.add(list_right_ls.get(0).getId());
				}else{
					list_right_ls=RightsTools.loadRightsByCondition(ConstValue.DJDYLY.XZ, "DJDYID='"+djdygz.getDJDYID()+"'");
					if(list_right_ls!=null&&list_right_ls.size()>0){
						bgqqlid.add(list_right_ls.get(0).getId());
					}
				}
			}else if(djdygz.getLY()=="01"||"01".equals(djdygz.getLY())){//gz
				bghbdcdyid.add(djdygz.getBDCDYID());
				bghdjdyid.add(djdygz.getDJDYID());
				bghdjdyly = "01";
				List<Rights> list_right_gz=RightsTools.loadRightsByCondition(ConstValue.DJDYLY.GZ, "DJDYID='"+djdygz.getDJDYID()+"' AND XMBH='"+xmbh+"'");
				bghbdcdylx = djdygz.getBDCDYLX();
				bghqlid.add(list_right_gz.get(0).getId());

			}
		}
		Map result = new HashMap();
		//获取单元信息
		Map baseinfo = new HashMap();
		List<Map> newvalue = new ArrayList<Map>();
		List<Map> oldvalue = new ArrayList<Map>();
		int i=0;
		//now
		for(i = 0;i < bghqlid.size();i++){
			HashMap<String, RealUnit> dy_now = HistoryBackdateTools.QueryUnitInfoByQlid(bghqlid.get(i));
			newvalue.add(beanToMap(dy_now.get(bghbdcdylx)));
		}
		baseinfo.put("newvalue", newvalue);
		baseinfo.put("newvaluesize", i);
		//before
		for(i = 0;i < bgqqlid.size();i++){
			HashMap<String, RealUnit> dy_pre = HistoryBackdateTools.QueryUnitInfoByQlid(bgqqlid.get(i));
			oldvalue.add(beanToMap(dy_pre.get(bghbdcdylx)));
		}
		baseinfo.put("oldvalue",oldvalue);
		baseinfo.put("oldvaluesize", i);

		result.put("baseinfo",baseinfo);


		//获取权利信息

		Map rights = new HashMap();

		Map mortgages = new HashMap();

		Map seals = new HashMap();

		List<Map> right = new ArrayList<Map>();
		List<Map> mortgage = new ArrayList<Map>();
		List<Map> seal = new ArrayList<Map>();
		List<Map> right_pr = new ArrayList<Map>();
		List<Map> mortgage_pre = new ArrayList<Map>();
		List<Map> seal_pre = new ArrayList<Map>();
		int size = bghqlid.size()<bgqqlid.size()?bghqlid.size():bgqqlid.size();
		BDCS_XMXX xmxx = basecommondao.get(BDCS_XMXX.class,xmbh);
		//now
		List<String> cqlx = new ArrayList<String>(Arrays.asList("1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","24") );
		boolean hasCq = false;
		//List<Map> qlMap_now =new ArrayList<Map>();

		String qllx_now = "";
		List<RightsHolder> holders_now = new ArrayList<RightsHolder>();
		//before
		List<Map> qlMap_pre =new ArrayList<Map>();

		String qllx_pre = "";
		String xmbh_pre = "";
		List<Map> holdersTrans_now_temp = new ArrayList<Map>();
		List<Map> holdersTrans_pre_temp = new ArrayList<Map>();


		for(i = 0;i <bghqlid.size();i++){
			if (!StringHelper.isEmpty(bghqlid.get(i))) {
				Rights  right_now = RightsTools.loadRights(ConstValue.DJDYLY.GZ, bghqlid.get(i));
				if (right_now != null) {
					List<Map> holdersTrans_now = new ArrayList<Map>();
					SubRights nowfsql = RightsTools.loadSubRightsByRightsID(ConstValue.DJDYLY.GZ, right_now.getId());
					holders_now = RightsHolderTools.loadRightsHolders(ConstValue.DJDYLY.GZ, right_now.getId());
					qllx_now = right_now.getQLLX();
					Map qlMap_now1= beanToMap(right_now);
	    			/*qlMap_now1.put("djsj",StringHelper.isEmpty(qlMap_now1.get("djsj"))?"":qlMap_now1.get("djsj").toString().substring(0, 10));
	    			qlMap_now1.put("qlqssj",StringHelper.isEmpty(qlMap_now1.get("qlqssj"))?"":qlMap_now1.get("qlqssj").toString().substring(0, 10));
	    			qlMap_now1.put("qljssj",StringHelper.isEmpty(qlMap_now1.get("qljssj"))?"":qlMap_now1.get("qljssj").toString().substring(0, 10));*/

					qlMap_now1.put("fsql", beanToMap(nowfsql));
					for (RightsHolder holder : holders_now) {
						if(holder!=null||!holder.equals("")){
							Map holderMap = new HashMap();
							holderMap = beanToMap(holder);
							holdersTrans_now.add(holderMap);
						}
					}
					holdersTrans_now_temp.addAll(holdersTrans_now);
					qlMap_now1.put("holders",holdersTrans_now);


					if ("23".equals(qllx_now)) {
						mortgage.add(qlMap_now1);
					}if ("99".equals(qllx_now) && "900".equals(xmxx.getDJLX())) {
						seal.add(qlMap_now1);
					}else if (cqlx.contains(qllx_now)) {
						right.add( qlMap_now1);

					}

				}
			}
		}
		if (right != null && right.size() > 0) {
			rights.put("newvalue",right);
			rights.put("oldvalue",null);
			rights.put("length",  right.size());

		}
		if (mortgage != null && mortgage.size() > 0) {
			mortgages.put("newvalue",mortgage);
			mortgages.put("oldvalue",null);
			mortgages.put("length",  mortgage.size());
		}
		if (seal != null && seal.size() > 0) {
			seals.put("newvalue",seal);
			seals.put("oldvalue",null);
			seals.put("length", seal.size());
		}
		for(i = 0;i <bgqqlid.size();i++){
			if (!StringHelper.isEmpty(bgqqlid.get(i))) {
				Rights  right_pre = RightsTools.loadRights(ConstValue.DJDYLY.LS, bgqqlid.get(i));
				if (!StringHelper.isEmpty(right_pre.getId())) {
					if (!StringHelper.isEmpty(right_pre.getXMBH())) {							BDCS_XMXX xmxx_pre = basecommondao.get(BDCS_XMXX.class, right_pre.getXMBH());
						xmbh_pre = xmxx_pre.getDJLX();
					}
					SubRights fsql_pre = RightsTools.loadSubRightsByRightsID(ConstValue.DJDYLY.LS, right_pre.getId());
					List<RightsHolder> holders_pre = RightsHolderTools.loadRightsHolders(ConstValue.DJDYLY.LS, right_pre.getId());
					qllx_pre =right_pre.getQLLX();
					Map qlMap_pre1 = beanToMap(right_pre);
					/*qlMap_pre1.put("djsj",StringHelper.isEmpty(qlMap_pre1.get("djsj"))?"":qlMap_pre1.get("djsj").toString().substring(0, 10));
					qlMap_pre1.put("qlqssj",StringHelper.isEmpty(qlMap_pre1.get("qlqssj"))?"":qlMap_pre1.get("qlqssj").toString().substring(0, 10));
					qlMap_pre1.put("qljssj",StringHelper.isEmpty(qlMap_pre1.get("qljssj"))?"":qlMap_pre1.get("qljssj").toString().substring(0, 10));*/

					qlMap_pre1.put("fsql", beanToMap(fsql_pre));
					List<Map> holdersTrans_pre = new ArrayList<Map>();
					for (RightsHolder holder : holders_pre) {
						if(holder!=null||!holder.equals("")){
							Map holderMap = new HashMap();
							holderMap = beanToMap(holder);
							holdersTrans_pre.add(holderMap);
						}
					}
					holdersTrans_pre_temp.addAll(holdersTrans_pre);
					qlMap_pre1.put("holders",holdersTrans_pre);

					if ("23".equals(qllx_pre)) {
						mortgage_pre.add(qlMap_pre1);
					}if ("99".equals(qllx_pre) && "900".equals(xmbh_pre)) {
						seal_pre.add(qlMap_pre1);
					}else if (cqlx.contains(qllx_pre)) {
						right_pr.add(qlMap_pre1);

					}

				}
			}


		}
		if (right_pr != null && right_pr.size() > 0) {
			rights.put("oldvalue",right_pr);
			if(rights.get("newvalue")!=null){
				int length = right_pr.size()>=Integer.valueOf(rights.get("length").toString())?right_pr.size():Integer.valueOf(rights.get("length").toString());
				rights.put("length", length);
			}else{
				rights.put("newvalue",null);
				rights.put("length", right_pr.size());
			}

		}
		if (mortgage_pre != null && mortgage_pre.size() > 0) {
			mortgages.put("oldvalue",mortgage_pre);
			if(mortgages.get("newvalue")!=null){
				int length = mortgage_pre.size()>=Integer.valueOf(mortgages.get("length").toString())?mortgage_pre.size():Integer.valueOf(mortgages.get("length").toString());
				mortgages.put("length", length);
			}else{
				mortgages.put("newvalue",null);
				mortgages.put("length", mortgage_pre.size());
			}
		}
		if (seal_pre != null && seal_pre.size() > 0) {
			seals.put("oldvalue",seal_pre);
			if(seals.get("newvalue")!=null){
				int length = seal_pre.size()>=Integer.valueOf(seals.get("length").toString())?seal_pre.size():Integer.valueOf(seals.get("length").toString());
				seals.put("length", length);
			}else{
				seals.put("newvalue",null);
				seals.put("length", seal_pre.size());
			}
		}


		if (rights != null && rights.size() > 0) {

			rights.put("length", Integer.valueOf(rights.get("length").toString()));
			result.put("rights",rights);

		}
		if (mortgages != null && mortgages.size() > 0) {
			mortgages.put("length",Integer.valueOf(mortgages.get("length").toString()));
			result.put("mortgages",mortgages);

		}
		if (seals != null && seals.size() > 0) {
			seals.put("length",Integer.valueOf(seals.get("length").toString()));
			result.put("seals",seals);

		}

		return result;
	}

	/*****************************************end: 使用日志查询的单元相关信息对比定义的部分功能定制柳州的单元信息对比模块 *****************************************/
	//将实体bean转换为Map类型
	public static Map<String, Object> beanToMap(Object obj) {
		Map<String, Object> params = new HashMap<String, Object>(0);
		if(obj !=null){
			try {
				PropertyUtilsBean propertyUtilsBean = new PropertyUtilsBean();
				PropertyDescriptor[] descriptors = propertyUtilsBean.getPropertyDescriptors(obj);
				for (int i = 0; i < descriptors.length; i++) {
					String name = descriptors[i].getName();
					if (!"class".equals(name)) {
						params.put(name.toLowerCase(), propertyUtilsBean.getNestedProperty(obj, name));
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return params;
	}

	/**
	 * 通过数据字段常量类型及数据字段值获取对应的常量名称
	 * @param consttype 常量类型
	 * @param constvalue 常量值
	 * @param request
	 * @param response
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	@RequestMapping(value="{consttype}/{constvalue}/getnamebyvalue",method=RequestMethod.GET)
	public @ ResponseBody String getNameByValue(@PathVariable String consttype, @PathVariable String constvalue,HttpServletRequest request,HttpServletResponse response){
		return ConstHelper.getNameByValue(consttype, constvalue);
	}

}
