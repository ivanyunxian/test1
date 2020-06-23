package com.supermap.realestate.registration.tools;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.supermap.realestate.registration.ViewClass.HistoryUnitInfo;
import com.supermap.realestate.registration.model.BDCS_DJDY_GZ;
import com.supermap.realestate.registration.model.BDCS_DJDY_LS;
import com.supermap.realestate.registration.model.BDCS_DJDY_XZ;
import com.supermap.realestate.registration.model.BDCS_DYBG;
import com.supermap.realestate.registration.model.BDCS_XMXX;
import com.supermap.realestate.registration.model.T_HANDLER;
import com.supermap.realestate.registration.model.interfaces.House;
import com.supermap.realestate.registration.model.interfaces.RealUnit;
import com.supermap.realestate.registration.model.interfaces.Rights;
import com.supermap.realestate.registration.util.ConstValue.BDCDYLX;
import com.supermap.realestate.registration.util.ConstValue.DJDYLY;
import com.supermap.realestate.registration.util.Global;
import com.supermap.realestate.registration.util.ProjectHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.wisdombusiness.core.SuperSpringContext;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;

/**
 * 历史回溯工具类
 * 
 * @author shb
 *
 */
public class HistoryBackdateTools {

	/**
	 * 
	 * @param qlid
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static HashMap<String, RealUnit> QueryUnitInfoByQlid(String qlid) {
		HashMap<String, RealUnit> map = new HashMap<String, RealUnit>();
		Rights rights = RightsTools.loadRights(DJDYLY.LS, qlid);
		if (rights == null) {
			rights = RightsTools.loadRights(DJDYLY.GZ, qlid);
		}
		if (rights != null) {
			String Baseworkflow_ID ="";
			Map mapdjdyinfo = getDJDYInfo(rights.getDJDYID(),rights.getXMBH());	
			if(!StringHelper.isEmpty(rights.getXMBH())) {
				Baseworkflow_ID=ProjectHelper.GetPrjInfoByXMBH(rights.getXMBH()).getBaseworkflowcode();
			}
			if ( "GZ001".equals(Baseworkflow_ID) ) {
				mapdjdyinfo.put("BDCDYID", rights.getBDCDYID());
			} 
					
			map = CurrentUnitMap(rights, mapdjdyinfo);
		}
		return map;
	}
	
	/**
	 * 通过单元变更初始化所有的单元列表信息
	 * 
	 * @param djdyid
	 *            ：登记单元ID
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static List<HistoryUnitInfo> InitHistoryUnitInfoByDJDYID(
			String djdyid) {
		CommonDao dao = SuperSpringContext.getContext().getBean(CommonDao.class);
		StringBuilder sql = new StringBuilder();
		sql.append(" XDJDYID = LDJDYID ");
		sql.append(" AND  XDJDYID='").append(djdyid).append("'");
		List<BDCS_DYBG> lstdybg = dao.getDataList(BDCS_DYBG.class,sql.toString());
		List<HistoryUnitInfo> lsths = new ArrayList<HistoryUnitInfo>();
		if (lstdybg != null && lstdybg.size() > 0) {
			for (int i = 0; i < lstdybg.size(); i++) {
				BDCS_DYBG dybg = lstdybg.get(i);
				HistoryUnitInfo hinfo = new HistoryUnitInfo();
				hinfo.setBdcdyid(dybg.getLBDCDYID());
				hinfo.setXmbh(dybg.getXMBH());
				Map  map_isgzdj=getIsGZDJ(dybg.getXMBH());
				if(map_isgzdj !=null){
				  	hinfo.setIsgzdj(StringHelper.FormatByDatatype(map_isgzdj.get("ISGZDJ")));
				  	hinfo.setIsgzdjasfdytdj(StringHelper.FormatByDatatype(map_isgzdj.get("ISGZDJASFDYT")));
				}			
				lsths.add(hinfo);
				if (i == lstdybg.size() - 1) {
					HistoryUnitInfo hinfo1 = new HistoryUnitInfo();
					hinfo1.setBdcdyid(dybg.getXBDCDYID());
					hinfo1.setSlsj(new Date());
					lsths.add(hinfo1);
					
				}
			}
		}
		return lsths;
	}

	/**
	 * 通过单元变更初始化所有的单元列表信息
	 * 
	 * @param djdyid
	 *            ：登记单元ID
	 * @return
	 */
	public static List<HistoryUnitInfo> InitHistoryUnitInfoByBDCDYID(
			String bdcdyid) {
		CommonDao dao = SuperSpringContext.getContext().getBean(CommonDao.class);
		StringBuilder sql = new StringBuilder();
		sql.append(" XDJDYID = LDJDYID ");
		sql.append(" AND  XBDCDYID='").append(bdcdyid).append("'");
		List<BDCS_DYBG> lstdybg = dao.getDataList(BDCS_DYBG.class,
				sql.toString());
		List<HistoryUnitInfo> lsths = new ArrayList<HistoryUnitInfo>();
		if (lstdybg != null && lstdybg.size() > 0) {
			for (int i = 0; i < lstdybg.size(); i++) {
				BDCS_DYBG dybg = lstdybg.get(i);
				HistoryUnitInfo hinfo = new HistoryUnitInfo();
				hinfo.setBdcdyid(dybg.getLBDCDYID());
				hinfo.setXmbh(dybg.getXMBH());
				lsths.add(hinfo);
				if (i == lstdybg.size() - 1) {
					HistoryUnitInfo hinfo1 = new HistoryUnitInfo();
					hinfo1.setBdcdyid(dybg.getXBDCDYID());
					lsths.add(hinfo1);
				}
			}
		}
		return lsths;
	}

	/**
	 * 通过字段名称获取字段值
	 * @param fieldName
	 * @param o
	 * @return
	 */
	public static Object getFieldValueByName(String fieldName, Object o) {
		try {
			String firstLetter = fieldName.substring(0, 1).toUpperCase();
			String getter = "get" + firstLetter + fieldName.substring(1);
			Method method = o.getClass().getMethod(getter, new Class[] {});
			Object value = method.invoke(o, new Object[] {});
			Field [] fs= o.getClass().getDeclaredFields();
			if(fs !=null && fs.length>0){
				 for(Field f:fs){
					 String name=f.getName().toLowerCase();
					 if(name.equals(fieldName.toLowerCase())){
						return ""; 
					 }
				 }
			}
			return value;
		} catch (Exception e) {
			return null;
		}
	}
	/**
	 * 获取自然幢不动产单元id
	 * 
	 * @param zrzbdcdyid
	 *            ：自然幢不动产单元id
	 * @param qldjsj
	 *            ：权利对应的起始时间
	 * @param qlxmbh
	 *            ：权利对应的项目编号
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private static RealUnit getBuildingUnit(String zrzbdcdyid, Date qldjsj,String qlxmbh,BDCDYLX lx) {
		RealUnit buildingUnit=null;
		 Map map_isgzdj= getIsGZDJ(qlxmbh);
		 if(map_isgzdj !=null){
			  boolean fdytflag=getIsGzdjAsFDYTFlag(map_isgzdj);
			  boolean gzdjflag=getIsGzdjFlag(map_isgzdj);
			  boolean isdb=getSfdbFlag(map_isgzdj); 
			  if(gzdjflag){
				  if(fdytflag){
					  if(isdb){
						  buildingUnit= getUnit(zrzbdcdyid,qldjsj,qlxmbh,lx,DJDYLY.LS);
					  }else{
						  if(BDCDYLX.YCZRZ.Value.equals(lx.Value)){
							  buildingUnit=UnitTools.loadUnit(lx, DJDYLY.XZ, zrzbdcdyid);  
						  }else{
							  buildingUnit=UnitTools.loadUnit(lx, DJDYLY.GZ, zrzbdcdyid);
						  }
					  }
				  }else{
					  buildingUnit= getUnit(zrzbdcdyid,qldjsj,qlxmbh,lx,DJDYLY.LS);
				  }
			  }else{
				  buildingUnit= getUnit(zrzbdcdyid,qldjsj,qlxmbh,lx,DJDYLY.LS);
			  }
		 }		
       return buildingUnit;
	}

	/**
	 * 获取宗地的不动产单元ID
	 * 
	 * @param zdbdcdyid
	 *            ：宗地的不动产单元ID
	 * @param qldjsj
	 *            ：权利对应的起始时间
	 * @param qlxmbh
	 *            ：权利对应的项目编号
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private static RealUnit getLandUnit(String zdbdcdyid, Date qldjsj,String qlxmbh) {
		RealUnit landUnit=null;
	    Map map_isgzdj=getIsGZDJ(qlxmbh);
	    boolean fdytflag=getIsGzdjAsFDYTFlag(map_isgzdj);
	    boolean gzdjflag=getIsGzdjFlag(map_isgzdj);
	    boolean isdb=getSfdbFlag(map_isgzdj);
	    if(gzdjflag){
	    	if(fdytflag){
	    		if(isdb){	    			
	    			landUnit=getUnit(zdbdcdyid,qldjsj,qlxmbh,BDCDYLX.SHYQZD,DJDYLY.LS);
	    		}else{
	    			landUnit =UnitTools.loadUnit(BDCDYLX.SHYQZD, DJDYLY.GZ, zdbdcdyid);
	    		}
	    	}else{
	    		landUnit=getUnit(zdbdcdyid,qldjsj,qlxmbh,BDCDYLX.SHYQZD,DJDYLY.LS);
	    	}
	    }
	    else{
	    	landUnit=getUnit(zdbdcdyid,qldjsj,qlxmbh,BDCDYLX.SHYQZD,DJDYLY.LS);
	    }
	    return landUnit;
	}
	/**
	 * 获取单元信息
	 * @param bdcdyid ：不动产单元ID
	 * @param qldjsj：权利对应的登记时间
	 * @param qlxmbh：权利对应的项目编号
	 * @param lx：不动产单元类型
	 * @param ly：登记单元来源
	 * @return
	 */
	private  static RealUnit getUnit(String bdcdyid,Date qldjsj,String qlxmbh,BDCDYLX lx,DJDYLY ly){
		RealUnit unit=null;
		String currentid="";
		List<HistoryUnitInfo> lsths = InitHistoryUnitInfoByBDCDYID(bdcdyid);
		lsths = setHistoryUnitsDJSJ(lsths);
		if(bdcdyid!=null&&!StringHelper.isEmpty(bdcdyid)) {
			currentid=bdcdyid;
		}else {
			currentid=getCurrentBDCDYID(lsths, qldjsj, qlxmbh);
		}
		
//		if(StringHelper.isEmpty(currentid)){
//			currentid=bdcdyid;
//		}
		unit =UnitTools.loadUnit(lx, ly, currentid);
		return unit;
	}
	
	@SuppressWarnings({ "unused", "rawtypes" })
	private static String getHandlerName(String xmbh){
		Map map=new HashMap<String, String>();
		String handlername="";
		BDCS_XMXX xmxx=Global.getXMXXbyXMBH(xmbh);
		if(xmxx !=null){
			CommonDao dao = SuperSpringContext.getContext().getBean(CommonDao.class);
			String workflowcode = ProjectHelper.getWorkflowCodeByProjectID(xmxx.getPROJECT_ID());
			String fulSql= " SELECT B.HANDLERID FROM BDCK.T_BASEWORKFLOW B "
					  + " LEFT JOIN BDC_WORKFLOW.WFD_MAPPING M ON M.WORKFLOWNAME=B.ID "
					  + " WHERE M.WORKFLOWCODE= '" + workflowcode + "' ";
		  List<Map> lstmap=	dao.getDataListByFullSql(fulSql);
			if(lstmap !=null && lstmap.size()>0){
				Map m=lstmap.get(0);
				if(m !=null){
					String handlerid=StringHelper.FormatByDatatype(m.get("HANDLERID"));
					if(!StringHelper.isEmpty(handlerid)){
						T_HANDLER handler=dao.get(T_HANDLER.class, handlerid);
						if(handler !=null){
							handlername= handler.getCLASSNAME().toUpperCase();
						}
					}
				}
			}
		}
		return handlername;
	}

	/**
	 * 获取当前项目是否是更正登记
	 * @param xmbh：项目编号
	 * @return 
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static Map getIsGZDJ(String xmbh)
	{
		Map map=new HashMap<String, String>();
		map.put("ISGZDJ", "false");//是否更正登记
		map.put("ISGZDJASFDYT", "false");//是否更正登记对应的房地一体
		map.put("SFDB", "false");//是否登簿
		if(StringHelper.isEmpty(xmbh)){
			return map;
		}
		String sfdb="false";
	   BDCS_XMXX xmxx=	Global.getXMXXbyXMBH(xmbh);
	   if(xmxx !=null ){
		  String tempsfdb= xmxx.getSFDB();
		  if("1".equals(tempsfdb)){
			  sfdb="true";
		  }
	   }
		String handlername=getHandlerName(xmbh);
		if(!StringHelper.isEmpty(handlername))
		{
			String [] lstgzfdyt={""};
			if(lstgzfdyt.equals(handlername))
			{
				map.clear();
				map.put("ISGZDJ", "true");//是否更正登记
				map.put("ISGZDJASFDYT", "true");//是否更正登记对应的房地一体
				map.put("SFDB", sfdb);//是否登簿
				return map;
			}
			String strgzdj="BG_YGandDYQBG_DJHandler,GZDJ_ZDHandler,GZ_HandDYQBG_DJHandler,GZDJHandler,GZDJHandler_LuZhou,LQGZDJHandler";
			strgzdj=strgzdj.toUpperCase();
			String [] lstgzdj=strgzdj.split(",");
			for(int i=0;i<lstgzdj.length;i++){
				if(lstgzdj[i].trim().equals(handlername))
				{
					map.clear();
					map.put("ISGZDJ", "true");//是否更正登记
					map.put("ISGZDJASFDYT", "false");//是否更正登记对应的房地一体
					map.put("SFDB", sfdb);//是否登簿
					return map;
				}
			}
		}
		return map;
	}

	/**
	 * 是否更正登记
	 * @param map
	 * @return true表示是更正登记，false表示非更正登记
	 */
	@SuppressWarnings("rawtypes")
	private static Boolean getIsGzdjFlag(Map map){
		boolean isgzdjflag=false;
		if(map !=null){
			String isgzdj=StringHelper.FormatByDatatype(map.get("ISGZDJ"));
			if("TRUE".equals(isgzdj.toUpperCase())){
				isgzdjflag=true;
			}
		}
		return isgzdjflag;		
	}
	
	/**
	 * 是否更正登记对应的房地一体登记
	 * @param map
	 * @return true表示是更正登记的房地一体登记，false表示非更正登记
	 */
	@SuppressWarnings("rawtypes")
	private static Boolean getIsGzdjAsFDYTFlag(Map map){
		boolean isgzdjasfdytflag=false;
		if(map !=null){
			String isgzdjasfdyt=StringHelper.FormatByDatatype(map.get("ISGZDJASFDYT"));
			if("TRUE".equals(isgzdjasfdyt.toUpperCase())){
				isgzdjasfdytflag=true;
			}
		}
		return isgzdjasfdytflag;
	}
	
	/**
	 * 判断更正登记是否登簿
	 * @param map
	 * @return true表示是更正登记的已登簿，false表示未登簿
	 */
	@SuppressWarnings("rawtypes")
	private static Boolean getSfdbFlag(Map map){
		boolean isdbflag=false;
		if(map !=null){
			String isdb=StringHelper.FormatByDatatype(map.get("SFDB"));
			if("TRUE".equals(isdb.toUpperCase())){
				isdbflag=true;
			}
		}
		return isdbflag;		
	}
	/**
	 * 设置单元历史回溯列表设置每个单元的登记时间,并排序
	 * 
	 * @param lsths
	 *            ：单元历史回溯列表
	 * @return
	 */
	public static List<HistoryUnitInfo> setHistoryUnitsDJSJ(
			List<HistoryUnitInfo> lsths) {
		if (lsths != null && lsths.size() > 0) {
			for (int j = 0; j < lsths.size(); j++) {
				if (j != lsths.size() - 1) {
					String xmbh = lsths.get(j).getXmbh();
					if (!StringHelper.isEmpty(xmbh)) {
						BDCS_XMXX xmxx=	Global.getXMXXbyXMBH(xmbh);
						if (xmxx != null) {
							if(!StringHelper.isEmpty(xmxx.getDJSJ())){
								lsths.get(j).setDjsj(xmxx.getDJSJ());
							}
							else{
								if(!StringHelper.isEmpty(xmxx.getSLSJ())){
									lsths.get(j).setSlsj(xmxx.getSLSJ());
								}
							}
						}
					}
				}else{
					lsths.get(j).setSlsj(new Date());
				}
			}
		}
		lsths = sortDateByAsc(lsths);
		return lsths;
	}

	/**
	 * 通过单元变更获取对应的历史单元信息
	 * 
	 * @param djdyid
	 *            :登记单元ID
	 * @return
	 */
	public static List<HistoryUnitInfo> getHistoryUnits(String djdyid) {
		List<HistoryUnitInfo> lsths = InitHistoryUnitInfoByDJDYID(djdyid);
		lsths = setHistoryUnitsDJSJ(lsths);
		return lsths;
	}

	
	/**
	 * 排序分三部分，第一部分是存量数据（登记时间与受理时间都为空），第二部分是登簿数据（只记录登记时间不记录受理时间）、第三部分是未登簿数据（只有受理时间）（升序排序）
	 * @param lsths
	 * @return
	 */
	public static List<HistoryUnitInfo> sortDateByAsc(
			List<HistoryUnitInfo> lsths) {
		if (lsths != null && lsths.size() > 1) {
			Collections.sort(lsths, new Comparator<HistoryUnitInfo>() {
				@Override
				public int compare(HistoryUnitInfo h1, HistoryUnitInfo h2) {
					Date dtdjsj1 = h1.getDjsj();
					Date dtdjsj2 = h2.getDjsj();
					Date dtslsj1 = h1.getSlsj();
					Date dtslsj2 = h2.getSlsj();
					int flag = 0;
					//为null放在最上面
				    if(StringHelper.isEmpty(dtdjsj1) && StringHelper.isEmpty(dtslsj1)){
				    	return -1;
				    }else if(StringHelper.isEmpty(dtdjsj2) && StringHelper.isEmpty(dtslsj2))
				    {
				    	return 1;
				    }//登记时间都不空，升序排序
				    else if(!StringHelper.isEmpty(dtdjsj1) && !StringHelper.isEmpty(dtdjsj2))
				    {
				    	if(dtdjsj1.after(dtdjsj2))
				    	{
				    		return 1;
				    	}else{
				    		return -1;
				    	}
				    }else if(!StringHelper.isEmpty(dtdjsj1) && StringHelper.isEmpty(dtdjsj2))
				    {
				    	return -1;
				    }else if(StringHelper.isEmpty(dtdjsj1) && !StringHelper.isEmpty(dtdjsj2)){
				    	return 1;
				    }else if(!StringHelper.isEmpty(dtslsj1) && !StringHelper.isEmpty(dtslsj2)){
				    	if(dtslsj1.after(dtslsj2))
				    	{
				    		return 1;
				    	}else{
				    		return -1;
				    	}				    	
				    }				    
					return flag;
				}
			});
		}
		return lsths;
	}

	/**
	 * 获取当前的不动产单元ID
	 * 
	 * @param lsths
	 *            :存放xmbh,djsj,bdcdyid集合
	 * @param currentqlsj
	 *            ：当前权利对应的时间
	 * @param currentqlxmbh
	 *            ：当前权利对应的项目编号
	 * @return
	 */
	public static String getCurrentBDCDYID(List<HistoryUnitInfo> lsths,Date currentqlsj, String currentqlxmbh) {
		String bdcdyid = "";
		if (lsths != null && lsths.size() > 0) {
			//项目编号与权利的登记时间为空时，返回存量数据第一手
			if(StringHelper.isEmpty(currentqlsj) && StringHelper.isEmpty(currentqlxmbh))
			{
				bdcdyid=lsths.get(0).getBdcdyid();
				return bdcdyid;
			}
			//当项目编号跟已有项目编号一样时，返回下一手
			if (!StringHelper.isEmpty(currentqlxmbh)) {
				for (int i = 0; i < lsths.size(); i++) {
					if (currentqlxmbh.equals(lsths.get(i).getXmbh())) {
						if (i<= lsths.size() - 1) {
							bdcdyid = lsths.get(i + 1).getBdcdyid();
							return bdcdyid;
						}
					}
				}
			}
			//当前时间不为可空,返回下一首
			if(!StringHelper.isEmpty(currentqlsj)){
				if(lsths.size()==2){
					bdcdyid = lsths.get(0).getBdcdyid();
					Date djsj1 =lsths.get(0).getDjsj();
					Date slsj1= lsths.get(0).getSlsj();
					if(!StringHelper.isEmpty(djsj1)){
						if(currentqlsj.before(djsj1)){
							return bdcdyid;
						}else{
							return lsths.get(1).getBdcdyid();
						}
					}else if(!StringHelper.isEmpty(slsj1)){
						if(currentqlsj.before(slsj1)){
							return bdcdyid;
						}else{
							return lsths.get(1).getBdcdyid();
						}
					}else{
						return lsths.get(1).getBdcdyid();
					}
				}else{
					for (int k = 0; k < lsths.size(); k++) {
						if(k+2<=lsths.size()){
							Date djsj1 =lsths.get(k).getDjsj();
							Date slsj1= lsths.get(k).getSlsj();
							Date djsj2 =lsths.get(k+1).getDjsj();
							Date slsj2= lsths.get(k+1).getSlsj();
							if(!StringHelper.isEmpty(djsj1)){
								if(!StringHelper.isEmpty(djsj2)){
									if(currentqlsj.after(djsj1) && currentqlsj.before(djsj2)){
										bdcdyid = lsths.get(k+1).getBdcdyid();
										return bdcdyid;
									}else if(currentqlsj.before(djsj1))
									{
										return lsths.get(k).getBdcdyid();
									}
								}else{
									if(!StringHelper.isEmpty(slsj2)){
										if(currentqlsj.after(djsj1) && currentqlsj.before(slsj2)){
											bdcdyid = lsths.get(k+1).getBdcdyid();
											return bdcdyid;
										}else if(currentqlsj.before(slsj2))
										{
											return lsths.get(k).getBdcdyid();
										}
									}
								}
							}else{
								if(!StringHelper.isEmpty(slsj1)){
									if(!StringHelper.isEmpty(djsj2)){
										if(currentqlsj.after(djsj1) && currentqlsj.before(djsj2)){
											bdcdyid = lsths.get(k+1).getBdcdyid();
											return bdcdyid;
										}
									}else{
										if(!StringHelper.isEmpty(slsj2)&&!StringHelper.isEmpty(djsj1)){
											if(currentqlsj.after(djsj1) && currentqlsj.before(slsj2)){
												bdcdyid = lsths.get(k+1).getBdcdyid();
												return bdcdyid;
											}	
										}
									}
								}else{
									if(!StringHelper.isEmpty(djsj2)){
										if(currentqlsj.after(djsj2))
										{
											bdcdyid = lsths.get(k+1).getBdcdyid();
											return bdcdyid;
										}
									}else{
										if(!StringHelper.isEmpty(slsj2)){
											if(currentqlsj.after(slsj2)){
												bdcdyid = lsths.get(k+1).getBdcdyid();
												return bdcdyid;
											}	
										}
									}
								}
							}
						}else{
							return lsths.get(k).getBdcdyid();
						}
					}
				}
			}
		}
//			if (StringHelper.isEmpty(bdcdyid)) {
//				if (StringHelper.isEmpty(currentqlsj)) {
//					bdcdyid = lsths.get(0).getBdcdyid();
//				} else {
//					for (int k = 0; k < lsths.size(); k++) {
//						if (k == lsths.size() - 1) {
//							bdcdyid = lsths.get(k).getBdcdyid();
//							break;
//						} else {
//							if (currentqlsj.equals(lsths.get(k).getDjsj())) {
//								bdcdyid = lsths.get(k + 1).getBdcdyid();
//								break;
//							} else {
//								if (!StringHelper.isEmpty(lsths.get(k).getDjsj()) && !StringHelper.isEmpty(lsths.get(k + 1).getDjsj())) {
//									if (currentqlsj.after(lsths.get(k).getDjsj()) && lsths.get(k + 1).getDjsj().after(currentqlsj)) {
//										bdcdyid = lsths.get(k + 1).getBdcdyid();
//										break;
//									}
//								} else if (!StringHelper.isEmpty(lsths.get(k).getDjsj()) && StringHelper.isEmpty(lsths.get(k + 1).getDjsj())) {
//									if (currentqlsj.after((lsths.get(k).getDjsj()))) {
//										bdcdyid = lsths.get(k + 1).getBdcdyid();
//										break;
//									}
//								} else {
//									if(!StringHelper.isEmpty(lsths.get(k).getSlsj())){
//									 	if(currentqlsj.before(lsths.get(k).getSlsj())){
//									 		bdcdyid = lsths.get(k).getBdcdyid();
//									 		break;
//									 	}
//									}else{
//										bdcdyid = lsths.get(0).getBdcdyid();
//										break;
//									}
//								}
//							}
//						}
//					}
//				}
//			}
//		}
		return bdcdyid;
	}

	/**
	 * 获取登记单元中的不动产单元类型和不动产单元ID,来源，项目编号
	 * 
	 * @param djdyid
	 *            ：登记单元ID
	 * @param qlxmbh
	 *            :项目编号
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static Map getDJDYInfo(String djdyid, String qlxmbh) {
		Map map = new HashMap<String, String>();
		CommonDao dao = SuperSpringContext.getContext().getBean(CommonDao.class);
		String bdcdyid = "";
		String bdcdylx = "";
		String ly="";
		List<BDCS_DJDY_GZ> lstdjdygz = dao.getDataList(BDCS_DJDY_GZ.class,"DJDYID='" + djdyid + "' and XMBH='" + qlxmbh + "'");
		if (lstdjdygz != null && lstdjdygz.size() > 0) {
			bdcdyid = lstdjdygz.get(0).getBDCDYID();
			bdcdylx = lstdjdygz.get(0).getBDCDYLX();
			ly=lstdjdygz.get(0).getLY();
			map.put("BDCDYID", bdcdyid);
			map.put("BDCDYLX", bdcdylx);
			map.put("LY", ly);
			if(qlxmbh!=null){
				map.put("XMBH", qlxmbh);
			}
		}
		 else {
			 List<BDCS_DJDY_XZ> lstdjdy = dao.getDataList(BDCS_DJDY_XZ.class,"DJDYID='" + djdyid + "'");
				if (lstdjdy != null && lstdjdy.size() > 0) {
					bdcdyid = lstdjdy.get(0).getBDCDYID();
					bdcdylx = lstdjdy.get(0).getBDCDYLX();
					ly=lstdjdy.get(0).getLY();
					map.put("LY", ly);
					map.put("BDCDYID", bdcdyid);
					map.put("BDCDYLX", bdcdylx);
					if(lstdjdy.get(0).getXMBH()!=null){
						map.put("XMBH", lstdjdy.get(0).getXMBH());
					}
				}else{
					List<BDCS_DJDY_LS> lsdjdy = dao.getDataList(BDCS_DJDY_LS.class,"DJDYID='" + djdyid + "'");
					if (lsdjdy != null && lsdjdy.size() > 0) {
						bdcdyid = lsdjdy.get(0).getBDCDYID();
						bdcdylx = lsdjdy.get(0).getBDCDYLX();
						ly=lsdjdy.get(0).getLY();
						map.put("LY", ly);
						map.put("BDCDYID", bdcdyid);
						map.put("BDCDYLX", bdcdylx);
						if(lsdjdy.get(0).getXMBH()!=null){
							map.put("XMBH", lsdjdy.get(0).getXMBH());
						}
					}
				}
		}
		return map;
	}

	/**
	 * 当前单元对应的键值对，BDCDYLX-BDCDYID
	 * 
	 * @param rights
	 *            ：当前的权利信息
	 * @param bdcdylx
	 *            ：不动产单元类型
	 * @return BDCDYLX-BDCDYID
	 */
	@SuppressWarnings({ "rawtypes" })
	private static HashMap<String, RealUnit> CurrentUnitMap(Rights rights, Map mapdjdyinfo) {
		HashMap<String, RealUnit> map = new HashMap<String, RealUnit>();
		String bdcdylx="";
		if (!StringHelper.isEmpty(mapdjdyinfo)&&  mapdjdyinfo.size()> 0 ){
			bdcdylx = StringHelper.FormatByDatatype(mapdjdyinfo.get("BDCDYLX"));
		}
		if (BDCDYLX.H.Value.equals(bdcdylx) || BDCDYLX.YCH.Value.equals(bdcdylx)) {
			map = CurrentHouseMap(rights, mapdjdyinfo);
		} else {
			map=CurrentOtherUnitMap(rights,mapdjdyinfo);
		}
		return map;
	}


	@SuppressWarnings("rawtypes")
	private static HashMap<String, RealUnit> CurrentOtherUnitMap(Rights rights,Map mapdjdyinfo) {
		HashMap<String, RealUnit> map = new HashMap<String, RealUnit>();
		String bdcdylx="";
		String ly="";
		CommonDao dao = SuperSpringContext.getContext().getBean(CommonDao.class);
		if (rights != null) {
			Date qldjsj = rights.getDJSJ();
			String qlxmbh = StringHelper.formatObject(rights.getXMBH());
			Map mapisgzdj=getIsGZDJ(qlxmbh);
			Boolean gzdjflag=getIsGzdjFlag(mapisgzdj);
			Boolean sfdbflag=getSfdbFlag(mapisgzdj);
			bdcdylx=StringHelper.FormatByDatatype(mapdjdyinfo.get("BDCDYLX"));
			ly=StringHelper.FormatByDatatype(mapdjdyinfo.get("LY"));
			 String bdcdyid=StringHelper.FormatByDatatype(mapdjdyinfo.get("BDCDYID"));
			if(gzdjflag){
				if(sfdbflag){
					 RealUnit unit=getUnit(bdcdyid,qldjsj,qlxmbh,BDCDYLX.initFrom(bdcdylx),DJDYLY.LS);
					 map.put(bdcdylx, unit);
				}else{
					 RealUnit unit= UnitTools.loadUnit(BDCDYLX.initFrom(bdcdylx), DJDYLY.initFrom(ly), bdcdyid);
					 map.put(bdcdylx, unit);
				}
			}else{
				if ("02".equals(bdcdylx)) {//使用权宗地
					List<Map> bdcdyids = dao.getDataListByFullSql(" select bdcdyid from bdck.bdcs_SHYQZD_ls where xmbh='"+qlxmbh+"'");
					bdcdyid = bdcdyids.get(0).get("BDCDYID").toString();
				} 
				 RealUnit unit=getUnit(bdcdyid,qldjsj,qlxmbh,BDCDYLX.initFrom(bdcdylx),DJDYLY.LS);
				 if (!StringHelper.isEmpty(unit)) {
					 map.put(bdcdylx, unit);
				}
			}
		}
		return map;
	}
	/**
	 * 当前房屋对应的键值对，BDCDYLX-BDCDYID，包括房屋，自然幢及宗地
	 * 
	 * @param rights
	 *            ：当前的权利信息
	 * @param bdcdylx
	 *            ：不动产单元类型
	 * @param lyflag
	 *            ：来源，一种来源于工作层(true)，一种来源于历史层（false）
	 * @return 集合：031/032-HBDCDYID,03/08-ZRZBDCDYID,02-SHYQZDBDCDYID
	 */
	@SuppressWarnings({ "rawtypes" })
	private static HashMap<String, RealUnit> CurrentHouseMap(Rights rights, Map mapdjdyinfo) {
		HashMap<String, RealUnit> map= new HashMap<String, RealUnit>();
		String bdcdylx="";
		String bdcdyid="";
		String ly="";
		if(!StringHelper.isEmpty(mapdjdyinfo)){
			 ly=StringHelper.FormatByDatatype(mapdjdyinfo.get("LY"));
			bdcdylx=StringHelper.FormatByDatatype(mapdjdyinfo.get("BDCDYLX"));
		}		
		if (rights != null) {			
			String djdyid = rights.getDJDYID();
			Date qldjsj = rights.getDJSJ();
			String qlxmbh = StringHelper.formatObject(rights.getXMBH());
			Map map_isgzdj=getIsGZDJ(qlxmbh);
			Boolean gzdjflag=getIsGzdjFlag(map_isgzdj);
			Boolean sfdbflag=getSfdbFlag(map_isgzdj);
			if(gzdjflag){
				if(!sfdbflag){
					 bdcdyid=StringHelper.FormatByDatatype(mapdjdyinfo.get("BDCDYID"));
					RealUnit unit=UnitTools.loadUnit(BDCDYLX.initFrom(bdcdylx), DJDYLY.initFrom(ly), bdcdyid);
					if (!StringHelper.isEmpty(unit)) {
						House h = (House) unit;
					    String landid = h.getZDBDCDYID();
					    String buildingid = h.getZRZBDCDYID();
					    RealUnit land = getLandUnit(landid, qldjsj, qlxmbh);
						map.put(BDCDYLX.SHYQZD.Value, land);
						BDCDYLX lx=BDCDYLX.ZRZ;
						if(BDCDYLX.H.Value.equals(bdcdylx)){
							 lx=BDCDYLX.ZRZ;
						}else{
							 lx=BDCDYLX.YCZRZ;
						}
						RealUnit building = getBuildingUnit(buildingid, qldjsj,qlxmbh,lx);
						map.put(lx.Value, building);
					}
					map.put(bdcdylx, unit);
				}else{
					bdcdyid=StringHelper.FormatByDatatype(mapdjdyinfo.get("BDCDYID"));
					map=CurrentHouseMapEx(bdcdyid,djdyid,bdcdylx,qldjsj,qlxmbh);
				}
			}else{
				bdcdyid=StringHelper.FormatByDatatype(mapdjdyinfo.get("BDCDYID"));
				map=CurrentHouseMapEx(bdcdyid,djdyid,bdcdylx,qldjsj,qlxmbh);
			}			
		}
		return map;
	}
	
	/**
	 * 获取
	 * @param djdyid
	 * @param bdcdylx
	 * @param qldjsj
	 * @param qlxmbh
	 * @return
	 */
	private static HashMap<String, RealUnit> CurrentHouseMapEx(String bdcdyid,String djdyid,String bdcdylx,Date qldjsj ,String qlxmbh){
		HashMap<String, RealUnit> map= new HashMap<String, RealUnit>();
		List<HistoryUnitInfo> lsths = getHistoryUnits(djdyid);
		String housebdcdyid =getCurrentBDCDYID(lsths,qldjsj,qlxmbh);
		if(StringHelper.isEmpty(housebdcdyid)){
			housebdcdyid=bdcdyid;
		}
		RealUnit houseunit =UnitTools.loadUnit(BDCDYLX.initFrom(bdcdylx), DJDYLY.LS, housebdcdyid);
		if(houseunit ==null){
			houseunit =UnitTools.loadUnit(BDCDYLX.initFrom(bdcdylx), DJDYLY.GZ, housebdcdyid);
		}
		map.put(bdcdylx, houseunit);
		if (!StringHelper.isEmpty(houseunit)) {
			House h = (House) houseunit;
			String zdbdcdyid = h.getZDBDCDYID();
			String zrzbdcdyid = h.getZRZBDCDYID();
			if (!StringHelper.isEmpty(zdbdcdyid)) {
				RealUnit land = getLandUnit(zdbdcdyid, qldjsj, qlxmbh);
				map.put(BDCDYLX.SHYQZD.Value, land);
			}			
			BDCDYLX lx=BDCDYLX.H;
			if(lx.Value.equals(bdcdylx)){
				 lx=BDCDYLX.ZRZ;
			}else{
				 lx=BDCDYLX.YCZRZ;
			}
			if (!StringHelper.isEmpty(zrzbdcdyid)) {
				RealUnit building = getBuildingUnit(zrzbdcdyid, qldjsj,qlxmbh,lx);
				map.put(lx.Value, building);
			}
		}
		return map;
	}
	
	/**
	 * 获取上一首的权利ID
	 * @param bdcdyid：不动产单元ID
	 * @param bdcdylx：不动产单元类型
	 * @param currentxmbh：当前的项目编号
	 * @return
	 */
	public static String getPreviousQLID(String bdcdyid,String bdcdylx,String currentxmbh)
	{
		String qlid="";
		String djdyid="";
		CommonDao dao = SuperSpringContext.getContext().getBean(CommonDao.class);
		List<BDCS_DJDY_LS> list_djdy_ls=dao.getDataList(BDCS_DJDY_LS.class, "BDCDYLX='"+bdcdylx+"' AND BDCDYID='"+bdcdyid+"'");
		if(list_djdy_ls==null||list_djdy_ls.size()<=0){
			List<BDCS_DJDY_GZ> list_djdy_gz=dao.getDataList(BDCS_DJDY_GZ.class, "BDCDYLX='"+bdcdylx+"' AND BDCDYID='"+bdcdyid+"'");
			if(list_djdy_gz!=null&&list_djdy_gz.size()>0){
				djdyid=list_djdy_gz.get(0).getDJDYID();
			}
		}else{
			djdyid=list_djdy_ls.get(0).getDJDYID();
		}
		if(StringHelper.isEmpty(djdyid)){
			return qlid;
		}
		List<Rights> list_right_ls=RightsTools.loadRightsByCondition(DJDYLY.LS, "DJDYID='"+djdyid+"'");
		List<Rights> list_right_gz=RightsTools.loadRightsByCondition(DJDYLY.GZ, "DJDYID='"+djdyid+"' AND XMBH IN (SELECT id FROM BDCS_XMXX WHERE SFDB IS NULL OR SFDB<>'1') ORDER BY QLLX DESC");
		List<HistoryUnitInfo> lsths =new ArrayList<HistoryUnitInfo>();
		if(list_right_ls !=null && list_right_ls.size()>0){
			for(int i=0;i<list_right_ls.size();i++){
				Rights r=list_right_ls.get(i);
				if(r !=null){
					HistoryUnitInfo info=new HistoryUnitInfo();
					info.setDjsj(r.getDJSJ());
					info.setBdcdyid(r.getId());
					info.setXmbh(r.getXMBH());
					lsths.add(info);
				}
			}
		}
		if(list_right_gz !=null && list_right_gz.size()>0){
			for(Rights r_gz :list_right_gz){
				if(r_gz !=null){
					HistoryUnitInfo info=new HistoryUnitInfo();
					info.setBdcdyid(r_gz.getId());
					info.setXmbh(r_gz.getXMBH());
					BDCS_XMXX xmxx=Global.getXMXXbyXMBH(r_gz.getXMBH());
					if(xmxx !=null){
						if(!StringHelper.isEmpty(xmxx.getSLSJ())){
							info.setSlsj(xmxx.getSLSJ());
						}
					}
					lsths.add(info);
				}
			}
		}
		lsths=sortDateByAsc(lsths);
		if(lsths !=null && lsths.size()>0){
		  for(int j=0;j<lsths.size();j++){
			  HistoryUnitInfo info=lsths.get(j);
			  String xmbh=info.getXmbh();
			  if(currentxmbh.equals(xmbh)){
				  if(j==0){
					  qlid=info.getBdcdyid();
					  break;
				  }else{
					  qlid=lsths.get(j-1).getBdcdyid();
					  break;
				  }
			  }
		  }
		}
		return qlid;
	}
}
