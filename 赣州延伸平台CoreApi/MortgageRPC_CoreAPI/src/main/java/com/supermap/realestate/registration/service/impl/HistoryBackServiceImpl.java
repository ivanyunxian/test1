package com.supermap.realestate.registration.service.impl;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.supermap.realestate.registration.ViewClass.UnitInfo;
import com.supermap.realestate.registration.model.BDCS_DJDY_GZ;
import com.supermap.realestate.registration.model.BDCS_DJDY_LS;
import com.supermap.realestate.registration.model.BDCS_DYBG;
import com.supermap.realestate.registration.model.BDCS_DYXZ;
import com.supermap.realestate.registration.model.BDCS_QL_GZ;
import com.supermap.realestate.registration.model.BDCS_XMXX;
import com.supermap.realestate.registration.model.interfaces.House;
import com.supermap.realestate.registration.model.interfaces.RealUnit;
import com.supermap.realestate.registration.model.interfaces.Rights;
import com.supermap.realestate.registration.model.interfaces.SubRights;
import com.supermap.realestate.registration.service.HistoryBackService;
import com.supermap.realestate.registration.tools.RightsTools;
import com.supermap.realestate.registration.tools.UnitTools;
import com.supermap.realestate.registration.util.ConstHelper;
import com.supermap.realestate.registration.util.ConstValue.BDCDYLX;
import com.supermap.realestate.registration.util.ConstValue.DJDYLY;
import com.supermap.realestate.registration.util.ConstValue.DJLX;
import com.supermap.realestate.registration.util.ConstValue.QLLX;
import com.supermap.realestate.registration.util.FeatureHelper;
import com.supermap.realestate.registration.util.FeatureHelper.POINT2D;
import com.supermap.realestate.registration.util.Global;
import com.supermap.realestate.registration.util.RequestHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import com.supermap.wisdombusiness.web.Message;

@Service("historybackService")
public class HistoryBackServiceImpl implements HistoryBackService {

	@Autowired
	private CommonDao baseCommonDao;

	/**
	 * 获取回溯单元列表
	 * @param request
	 * @param response
	 * @author yuxuebin
	 * @date 2017年02月28日 10:13:10
	 * @return
	 * @throws Exception 
	 */
	@Override
	public List<UnitInfo> getUnitHistoryList(
			HttpServletRequest request) throws Exception {
		String bdcdyid=RequestHelper.getParam(request, "BDCDYID");
		String bdcdylx=RequestHelper.getParam(request, "BDCDYLX");
		List<UnitInfo> list_unitinfo=new ArrayList<UnitInfo>();
		List<String> list_bdcdyid=new ArrayList<String>();
		getUnitList(bdcdyid,bdcdylx,list_unitinfo,list_bdcdyid,0);
		return list_unitinfo;
	}
	/**
	 * 获取回溯单元列表及对应的权利列表
	 * @param request
	 * @param response
	 * @author yuxuebin
	 * @date 2017年02月28日 10:13:10
	 * @return
	 * @throws ParseException 
	 * @throws Exception 
	 */
	@Override
	public List<UnitInfo> getUnitHistoryListEx(HttpServletRequest request) throws UnsupportedEncodingException, ParseException{
		String bdcdyid=RequestHelper.getParam(request, "BDCDYID");
		String bdcdylx=RequestHelper.getParam(request, "BDCDYLX");
		List<UnitInfo> list_unitinfo=new ArrayList<UnitInfo>();
		List<UnitInfo> deslist_unitinfo=new ArrayList<UnitInfo>();
		List<String> list_bdcdyid=new ArrayList<String>();
		Map<String,List<List<POINT2D>>> lstpoints = new HashMap<String, List<List<POINT2D>>>();
		getUnitListEx(bdcdyid,bdcdylx,list_unitinfo,list_bdcdyid,0,lstpoints);
		if(list_unitinfo.size()>0){
			for(UnitInfo info: list_unitinfo){
				if(info !=null){
					String tempbdcdyid=info.getBdcdyid();
					String tempbdcdylx= info.getBdcdylx();
					List<List<HashMap<String,String>>> rights=getRightHistoryListEx(tempbdcdyid,tempbdcdylx);
					info.setLstrights(rights);
					deslist_unitinfo.add(info);
				}
			}
		}
		//排序
		deslist_unitinfo=ascSortUnit(deslist_unitinfo);
		return deslist_unitinfo;
	}
	
	/**
	 * 仅获取回溯单元列表
	 */
	@Override
	public List<UnitInfo> getOnlyUnit(HttpServletRequest request) throws UnsupportedEncodingException{
		String bdcdyid=RequestHelper.getParam(request, "BDCDYID");
		String bdcdylx=RequestHelper.getParam(request, "BDCDYLX");
		List<UnitInfo> list_unitinfo=new ArrayList<UnitInfo>();
		List<UnitInfo> deslist_unitinfo=new ArrayList<UnitInfo>();
		List<String> list_bdcdyid=new ArrayList<String>();
		Map<String,List<List<POINT2D>>> lstpoints = new HashMap<String, List<List<POINT2D>>>();
		getUnitListEx(bdcdyid,bdcdylx,list_unitinfo,list_bdcdyid,0,lstpoints);	
		//排序
		deslist_unitinfo=ascSortUnit(list_unitinfo);
		return deslist_unitinfo;
	}
	
	/**
	 * 获取获取单元里诶啊哦
	 * @param request
	 * @param response
	 * @author yuxuebin
	 * @date 2017年02月28日 10:13:10
	 * @return
	 */
	private void getUnitList(String bdcdyid, String bdcdylx,List<UnitInfo> list_unitinfo,List<String> list_bdcdyid,int level) {
		if(!list_bdcdyid.contains(bdcdyid)){
			list_bdcdyid.add(bdcdyid);
			RealUnit unit=UnitTools.loadUnit(BDCDYLX.initFrom(bdcdylx), DJDYLY.LS, bdcdyid);
			if(unit!=null){
				UnitInfo info=new UnitInfo();
				info.setBdcdyid(bdcdyid);
				info.setBdcdyh(unit.getBDCDYH());
				info.setZl(unit.getZL());
				info.setBdcdylx(unit.getBDCDYLX().Value);
				info.setMj(StringHelper.formatDouble(unit.getMJ()));
				List<String> list_prebdcdyid=new ArrayList<String>();
				List<String> list_sufbdcdyid=new ArrayList<String>();
				List<BDCS_DYBG> list_dybg_from=baseCommonDao.getDataList(BDCS_DYBG.class, "XBDCDYID='"+bdcdyid+"' AND NVL2(LBDCDYID,1,0)=1 AND LBDCDYID<>'"+bdcdyid+"' AND LDJDYID<>XDJDYID");
				if(list_dybg_from!=null&&list_dybg_from.size()>0){
					for(BDCS_DYBG dybg:list_dybg_from){
						list_prebdcdyid.add(dybg.getLBDCDYID());
						getUnitList(dybg.getLBDCDYID(),bdcdylx,list_unitinfo,list_bdcdyid,level-1);
					}
				}
				List<BDCS_DYBG> list_dybg_to=baseCommonDao.getDataList(BDCS_DYBG.class, "LBDCDYID='"+bdcdyid+"' AND NVL2(XBDCDYID,1,0)=1 AND XBDCDYID<>'"+bdcdyid+"' AND LDJDYID<>XDJDYID");
				
				if(list_dybg_to!=null&&list_dybg_to.size()>0){
					for(BDCS_DYBG dybg:list_dybg_to){
						list_sufbdcdyid.add(dybg.getXBDCDYID());
						getUnitList(dybg.getXBDCDYID(),bdcdylx,list_unitinfo,list_bdcdyid,level+1);
						
					}
				}
				info.setLevel(level);
				info.setPrebdcdyids(list_prebdcdyid);
				info.setSufbdcdyids(list_sufbdcdyid);
				list_unitinfo.add(info);
			}
		}
	}
	/**
	 * 获取单元信息（主要是针对单元的分割合并）
	 * @param bdcdyid
	 * @param bdcdylx
	 * @param list_unitinfo
	 * @param list_bdcdyid
	 * @param level
	 * @param lstpoints
	 */
	private void getUnitListEx(String bdcdyid, String bdcdylx,List<UnitInfo> list_unitinfo,List<String> list_bdcdyid,int level,Map<String,List<List<POINT2D>>> lstpoints) {
		if(!list_bdcdyid.contains(bdcdyid)){
			list_bdcdyid.add(bdcdyid);
			RealUnit unit=UnitTools.loadUnit(BDCDYLX.initFrom(bdcdylx), DJDYLY.LS, bdcdyid);
			if(unit!=null){
				UnitInfo info=new UnitInfo();
				info.setBdcdyid(bdcdyid);
				info.setBdcdyh(unit.getBDCDYH());
				info.setZl(unit.getZL());
				info.setBdcdylx(unit.getBDCDYLX().Value);
				info.setMj(StringHelper.formatDouble(unit.getMJ()));
				String tempbdcdyid="";
				if(BDCDYLX.H.Value.equals(bdcdylx) ||BDCDYLX.YCH.Value.equals(bdcdylx)){
					House h=(House)unit;
					tempbdcdyid=h.getZRZBDCDYID();
					info.setZdbdcdyid(h.getZDBDCDYID());
				}else{
					tempbdcdyid=bdcdyid;
				}
				if(!StringHelper.isEmpty(tempbdcdyid)){
					if(!lstpoints.containsKey(tempbdcdyid)){
						BDCDYLX tmpbdcdylx = BDCDYLX.initFrom(bdcdylx);
						if(BDCDYLX.H.Value.equals(bdcdylx)){
							tmpbdcdylx=BDCDYLX.ZRZ;
						}
						if(BDCDYLX.YCH.Value.equals(bdcdylx)){
							tmpbdcdylx=BDCDYLX.YCZRZ;
						}
						List<List<POINT2D>> lstpoint2d=FeatureHelper.getFeatureInfo(tmpbdcdylx,DJDYLY.LS,tempbdcdyid);
						if(StringHelper.isEmpty(lstpoint2d) || lstpoint2d.size()==0){
							lstpoint2d=FeatureHelper.getFeatureInfo(tmpbdcdylx,DJDYLY.GZ,tempbdcdyid);
						}
						if(!StringHelper.isEmpty(lstpoint2d) && lstpoint2d.size()>0){
							lstpoints.put(tempbdcdyid, lstpoint2d);
							info.setGeopoints(lstpoint2d);
						}
					}else{
						info.setGeopoints(lstpoints.get(tempbdcdyid));
					}
				}				
				List<String> list_prebdcdyid=new ArrayList<String>();
				List<String> list_sufbdcdyid=new ArrayList<String>();
				List<BDCS_DYBG> list_dybg_from=baseCommonDao.getDataList(BDCS_DYBG.class, "XBDCDYID='"+bdcdyid+"' AND NVL2(LBDCDYID,1,0)=1 AND LBDCDYID<>'"+bdcdyid+"' AND LDJDYID<>XDJDYID");
				if(list_dybg_from!=null&&list_dybg_from.size()>0){
					for(BDCS_DYBG dybg:list_dybg_from){
						list_prebdcdyid.add(dybg.getLBDCDYID());
						getUnitListEx(dybg.getLBDCDYID(),bdcdylx,list_unitinfo,list_bdcdyid,level-1,lstpoints);
					}
				}
				List<BDCS_DYBG> list_dybg_to=baseCommonDao.getDataList(BDCS_DYBG.class, "LBDCDYID='"+bdcdyid+"' AND NVL2(XBDCDYID,1,0)=1 AND XBDCDYID<>'"+bdcdyid+"' AND LDJDYID<>XDJDYID");
				
				if(list_dybg_to!=null&&list_dybg_to.size()>0){
					for(BDCS_DYBG dybg:list_dybg_to){
						list_sufbdcdyid.add(dybg.getXBDCDYID());
						getUnitListEx(dybg.getXBDCDYID(),bdcdylx,list_unitinfo,list_bdcdyid,level+1,lstpoints);						
					}
				}
				info.setLevel(level);
				info.setPrebdcdyids(list_prebdcdyid);
				info.setSufbdcdyids(list_sufbdcdyid);
				if(!StringHelper.isEmpty(list_sufbdcdyid) && list_sufbdcdyid.size()>0){
					info.setSubdygs(list_sufbdcdyid.size());
				}
				if(!StringHelper.isEmpty(list_prebdcdyid) && list_prebdcdyid.size()>0){
					info.setPredygs(list_prebdcdyid.size());
				}
				list_unitinfo.add(info);
			}
		}
	}
	
	/**
	 * 获取回溯权利列表
	 * @param request
	 * @param response
	 * @author yuxuebin
	 * @date 2017年02月28日 10:13:10
	 * @return
	 * @throws Exception 
	 */
	@Override
	public List<List<HashMap<String,String>>> getRightHistoryList(
			HttpServletRequest request) throws Exception {
		String bdcdyid=RequestHelper.getParam(request, "BDCDYID");
		String bdcdylx=RequestHelper.getParam(request, "BDCDYLX");
		List<List<HashMap<String,String>>> list_rightinfo=new ArrayList<List<HashMap<String,String>>>();
		String djdyid="";
		List<BDCS_DJDY_LS> list_djdy_ls=baseCommonDao.getDataList(BDCS_DJDY_LS.class, "BDCDYLX='"+bdcdylx+"' AND BDCDYID='"+bdcdyid+"'");
		if(list_djdy_ls==null||list_djdy_ls.size()<=0){
			List<BDCS_DJDY_GZ> list_djdy_gz=baseCommonDao.getDataList(BDCS_DJDY_GZ.class, "BDCDYLX='"+bdcdylx+"' AND BDCDYID='"+bdcdyid+"'");
			if(list_djdy_gz!=null&&list_djdy_gz.size()>0){
				djdyid=list_djdy_gz.get(0).getDJDYID();
			}
		}else{
			djdyid=list_djdy_ls.get(0).getDJDYID();
		}
		if(StringHelper.isEmpty(djdyid)){
			return list_rightinfo;
		}
		
		List<Rights> list_right_xz=RightsTools.loadRightsByCondition(DJDYLY.XZ, "DJDYID='"+djdyid+"'");
		List<String> list_rightid_xz=new ArrayList<String>();
		for(Rights right:list_right_xz){
			list_rightid_xz.add(right.getId());
		}
		List<Rights> list_right_ls=RightsTools.loadRightsByCondition(DJDYLY.LS, "DJDYID='"+djdyid+"'");
		List<Rights> list_right_gz=RightsTools.loadRightsByCondition(DJDYLY.GZ, "DJDYID='"+djdyid+"' AND XMBH IN (SELECT id FROM BDCS_XMXX WHERE SFDB IS NULL OR SFDB<>'1') ORDER BY QLLX DESC");
		sortRightsList(list_right_ls);
		
		List<String> list_ids=new ArrayList<String>();
		List<List<Rights>> list_all=new ArrayList<List<Rights>>();
		for(Rights right:list_right_ls){
			List<Rights> list_result=new ArrayList<Rights>();
			getRightsList(list_ids,list_right_ls,right,list_result);
			if(list_result!=null&&list_result.size()>0){
				list_all.add(list_result);
			}
		}
		AppendRightsOnGZ(list_all,list_right_gz);
		ConvertRightInfoFromRightsEx(list_rightinfo,list_all,list_rightid_xz,bdcdylx,bdcdyid);
		return list_rightinfo;
	}
	
	private List<List<HashMap<String,String>>> getRightHistoryListEx(String bdcdyid,String  bdcdylx) throws ParseException
	{
		List<List<HashMap<String,String>>> list_rightinfo=new ArrayList<List<HashMap<String,String>>>();
		String djdyid="";
		List<BDCS_DJDY_LS> list_djdy_ls=baseCommonDao.getDataList(BDCS_DJDY_LS.class, "BDCDYLX='"+bdcdylx+"' AND BDCDYID='"+bdcdyid+"'");
		if(list_djdy_ls==null||list_djdy_ls.size()<=0){
			List<BDCS_DJDY_GZ> list_djdy_gz=baseCommonDao.getDataList(BDCS_DJDY_GZ.class, "BDCDYLX='"+bdcdylx+"' AND BDCDYID='"+bdcdyid+"'");
			if(list_djdy_gz!=null&&list_djdy_gz.size()>0){
				djdyid=list_djdy_gz.get(0).getDJDYID();
			}
		}else{
			djdyid=list_djdy_ls.get(0).getDJDYID();
		}
		if(StringHelper.isEmpty(djdyid)){
			return list_rightinfo;
		}
		
		List<Rights> list_right_xz=RightsTools.loadRightsByCondition(DJDYLY.XZ, "DJDYID='"+djdyid+"'");
		List<String> list_rightid_xz=new ArrayList<String>();
		for(Rights right:list_right_xz){
			list_rightid_xz.add(right.getId());
		}
		List<Rights> list_right_ls=RightsTools.loadRightsByCondition(DJDYLY.LS, "DJDYID='"+djdyid+"'");
		List<Rights> list_right_gz=RightsTools.loadRightsByCondition(DJDYLY.GZ, "DJDYID='"+djdyid+"' AND XMBH IN (SELECT id FROM BDCS_XMXX WHERE SFDB IS NULL OR SFDB<>'1') ORDER BY QLLX DESC");
		sortRightsList(list_right_ls);
		
		List<String> list_ids=new ArrayList<String>();
		List<List<Rights>> list_all=new ArrayList<List<Rights>>();
		for(Rights right:list_right_ls){
			List<Rights> list_result=new ArrayList<Rights>();
			getRightsList(list_ids,list_right_ls,right,list_result);
			if(list_result!=null&&list_result.size()>0){
				list_all.add(list_result);
			}
		}
		AppendRightsOnGZ(list_all,list_right_gz);
		ConvertRightInfoFromRightsEx(list_rightinfo,list_all,list_rightid_xz,bdcdylx,bdcdyid);
		return list_rightinfo;
	}
	
	/**
	 * 权利列表排序
	 * @param request
	 * @param response
	 * @author yuxuebin
	 * @date 2017年02月28日 10:13:10
	 * @return
	 */
	private void sortRightsList(List<Rights> list_rights){
		Collections.sort(list_rights, new Comparator<Rights>() {
			@Override
			public int compare(Rights righti, Rights rightj) {
				Date  djsji = righti.getDJSJ();
				Date  djsjj = rightj.getDJSJ();
				if(djsji==null&&djsjj==null){
					return 0;
				}else if(djsji!=null&&djsjj==null){
					return 1;
				}else if(djsji==null&&djsjj!=null){
					return -1;
				}else if(djsji==null&&djsjj!=null){
					return -1;
				}else if(djsji.after(djsjj)){
					return 1;
				}else if(djsji.before(djsjj)){
					return -1;
				}
				return 0;
			}
		});
	}
	
	/**
	 * 获取横向权利列表
	 * @param request
	 * @param response
	 * @author yuxuebin
	 * @date 2017年02月28日 10:13:10
	 * @return
	 */
	private void getRightsList(List<String> list_ids,List<Rights> list_rights,Rights right,List<Rights> list_result){
		List<String> list_mainqllx=Arrays.asList(("1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,24,28,29,30,36").split(","));
		if(list_ids.contains(right.getId())){
			return;
		}
		list_result.add(right);
		list_ids.add(right.getId());
		for(Rights right_1:list_rights){
			if(list_ids.contains(right_1.getId())){
				continue;
			}
			if(!StringHelper.isEmpty(right_1.getLYQLID())&&right_1.getLYQLID().equals(right.getId())){
				boolean bly=false;
				if("99".equals(right.getQLLX())&&"99".equals(right_1.getQLLX())&&right_1.getDJLX().equals(right.getDJLX())){
					bly=true;
				}else if(!StringHelper.isEmpty(right.getQLLX())&&right.getQLLX().equals(right_1.getQLLX())){
					bly=true;
				}
				if(list_mainqllx.contains(right_1.getQLLX())&&!"700".equals(right_1.getDJLX())){
					bly=false;
				}
				if(bly){
					getRightsList(list_ids,list_rights,right_1,list_result);
				}
			}
		}
	}
	/**
	 * 权利列表转换为权利信息
	 * @param request
	 * @param response
	 * @author yuxuebin
	 * @date 2017年02月28日 10:13:10
	 * @return
	 */
//	private void ConvertRightInfoFromRights(List<List<HashMap<String,String>>> list_rightinfo,List<List<Rights>> list_rights,List<String> list_rightid_xz){
//		List<String> list_mainqllx=Arrays.asList(("1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,24,28,29,30,36").split(","));
//		for(List<Rights> list_rights_1:list_rights){
//			List<HashMap<String,String>> list_rightsinfo_1=new ArrayList<HashMap<String,String>>();
//			int i=0;
//			for(Rights rights:list_rights_1){
//				i++;
//				HashMap<String,String> rightsinfo=new HashMap<String, String>();
//				rightsinfo.put("QLID", rights.getId());
//				rightsinfo.put("DJLX", rights.getDJLX());
//				rightsinfo.put("QLLX", rights.getQLLX());
//				rightsinfo.put("BDCQZH", rights.getBDCQZH());
//				rightsinfo.put("YWH", rights.getYWH());
//				rightsinfo.put("YWLSH", "");
//				if(!StringHelper.isEmpty(rights.getYWH())){
//					BDCS_XMXX xmxx=Global.getXMXX(rights.getYWH());
//					if(xmxx!=null){
//						rightsinfo.put("YWLSH", xmxx.getYWLSH());
//					}
//				}
//				rightsinfo.put("DJSJ", StringHelper.FormatByDatetime(rights.getDJSJ()));
//				
//				String status="1";
//				if(rights instanceof BDCS_QL_GZ){
//					BDCS_XMXX xmxx=Global.getXMXXbyXMBH(rights.getXMBH());
//					if(xmxx!=null){
//						if("400".equals(xmxx.getDJLX())){
//							rightsinfo.put("DJLX", "400");
//						}
//						if("800".equals(xmxx.getDJLX())&&"98".equals(xmxx.getQLLX())){
//							rightsinfo.put("DJLX", "800");
//							rightsinfo.put("QLLX", "98");
//						}
//						rightsinfo.put("YWH", xmxx.getPROJECT_ID());
//						rightsinfo.put("YWLSH", xmxx.getYWLSH());
//						rightsinfo.put("DJSJ", "");
//					}
//					status="0";
//				}else if(!list_rightid_xz.contains(rights.getId())){
//					status="2";
//				}
//				rightsinfo.put("STATUS", status);//0在办，1有效，2注销
//				list_rightsinfo_1.add(rightsinfo);
//				if(i==list_rights_1.size()&&"2".equals(status)){
//					boolean baddzxql=true;
//					if(list_mainqllx.contains(rights.getQLLX())&&!"700".equals(rights.getQLLX())){
//						baddzxql=false;
//					}
//					if(baddzxql){
//						HashMap<String,String> rightsinfo_zx=new HashMap<String, String>();
//						rightsinfo_zx.put("QLID", rights.getId());
//						rightsinfo_zx.put("BDCQZH", rights.getBDCQZH());
//						rightsinfo_zx.put("DJLX", "400");
//						rightsinfo_zx.put("QLLX", rights.getQLLX());
//						if("800".equals(rights.getDJLX())){
//							rightsinfo_zx.put("DJLX", "800");
//							rightsinfo_zx.put("QLLX", "98");
//						}
//						SubRights subright=null;
//						if(!StringHelper.isEmpty(rights.getFSQLID())){
//							subright=RightsTools.loadSubRights(DJDYLY.LS, rights.getFSQLID());
//						}else{
//							subright=RightsTools.loadSubRightsByRightsID(DJDYLY.LS, rights.getId());
//						}
//						if(subright!=null){
//							rightsinfo_zx.put("YWH", subright.getZXDYYWH());
//							rightsinfo_zx.put("YWLSH", "");
//							if(!StringHelper.isEmpty(subright.getZXDYYWH())){
//								BDCS_XMXX xmxx_zx=Global.getXMXX(subright.getZXDYYWH());
//								if(xmxx_zx!=null){
//									rightsinfo_zx.put("YWLSH", xmxx_zx.getYWLSH());
//								}
//							}
//							rightsinfo_zx.put("DJSJ", StringHelper.FormatByDatetime(subright.getZXSJ()));
//							rightsinfo_zx.put("STATUS", "2");//0在办，1有效，2注销
//						}
//						list_rightsinfo_1.add(rightsinfo_zx);
//					}
//				}
//			}
//			list_rightinfo.add(list_rightsinfo_1);
//		}
//	}
    
	/**
	 * 通过不动产单元ID获取单元限制信息
	 * @param bdcdyid：不动产单元id
	 * @return
	 */
	private List<List<HashMap<String,String>>> getDYXZInfos(String bdcdyid){
		List<List<HashMap<String,String>>> list_dyxzinfo=null;
		List<HashMap<String,String>> lst=null;
		CommonDao dao = baseCommonDao;
		List<BDCS_DYXZ> lstdyxz=dao.getDataList(BDCS_DYXZ.class, "BDCDYID='"+bdcdyid+"' order by djsj   nulls  first");
		if(!StringHelper.isEmpty(lstdyxz) && lstdyxz.size()>0){
			list_dyxzinfo=new ArrayList<List<HashMap<String,String>>>();
			
			for(int i=0;i<lstdyxz.size();i++){
				lst=new ArrayList<HashMap<String,String>>();
				BDCS_DYXZ dyxz=lstdyxz.get(i);
				String yxbz=dyxz.getYXBZ();
				HashMap<String,String> rightsinfo=new HashMap<String, String>();
				rightsinfo.put("DJLX", DJLX.QTDJ.Value);
				rightsinfo.put("QLLX","DYXZ");
				rightsinfo.put("YWH", dyxz.getYWH());
				rightsinfo.put("BDCQZH", dyxz.getBDCQZH());
				rightsinfo.put("TITLE", "限");
				rightsinfo.put("TPLNAME", "DYXZ");//前端模版类型
				rightsinfo.put("MAINQLFLAG", "FALSE");//主体权利标记
				rightsinfo.put("DJSJ", StringHelper.FormatByDatetime(dyxz.getDJSJ()));
				rightsinfo.put("SJ", StringHelper.FormatDateOnType(dyxz.getDJSJ(), "yyyy-MM-dd HH:mm:ss"));
				rightsinfo.put("YWLX", "限制登记");
				String xzlx=StringHelper.FormatByDatatype(dyxz.getXZLX());
				String xzlxname="";
				if(!StringHelper.isEmpty(xzlx)){
				  xzlxname=	ConstHelper.getNameByValue("XZLX", xzlx);
				 if(!StringHelper.isEmpty(xzlxname)){
					 rightsinfo.remove("YWLX");
					 rightsinfo.put("YWLX", "限制登记"+"-"+xzlxname);
				 }
				}
				rightsinfo.put("YWLSH", "");
				rightsinfo.put("QLID", dyxz.getId());
				rightsinfo.put("STATUS", yxbz);//0在办，1有效，2注销
				rightsinfo.put("QLRMC", StringHelper.FormatByDatatype(dyxz.getXZDW()));//权利人名称
				lst.add(rightsinfo);
				if(!StringHelper.isEmpty(yxbz) && "2".equals(yxbz)){
					HashMap<String,String> rightsinfo_xz=new HashMap<String, String>();
					rightsinfo_xz.put("DJLX", DJLX.QTDJ.Value);
					rightsinfo_xz.put("QLLX","DYXZ");
					rightsinfo_xz.put("YWH", dyxz.getZXYWH());
					rightsinfo_xz.put("BDCQZH", dyxz.getBDCQZH());
					rightsinfo_xz.put("TITLE", "销");
					rightsinfo_xz.put("TPLNAME", "DYXZ");//前端模版类型
					rightsinfo_xz.put("MAINQLFLAG", "FALSE");//主体权利标记
					rightsinfo_xz.put("DJSJ", StringHelper.FormatByDatetime(dyxz.getZXDJSJ()));
					rightsinfo_xz.put("SJ", StringHelper.FormatDateOnType(dyxz.getZXDJSJ(), "yyyy-MM-dd HH:mm:ss"));
					rightsinfo_xz.put("YWLX", "限制登记-解除限制");
					if(!StringHelper.isEmpty(xzlxname)){
						rightsinfo_xz.remove("YWLX");
						rightsinfo_xz.put("YWLX", "限制登记"+"-解除"+xzlxname);
					 }					
					rightsinfo_xz.put("YWLSH", "");
					rightsinfo_xz.put("QLID", dyxz.getId());
					rightsinfo_xz.put("STATUS", "2");//0在办，1有效，2注销
					rightsinfo_xz.put("QLRMC", StringHelper.FormatByDatatype(dyxz.getZXXZDW()));//权利人名称
					lst.add(rightsinfo_xz);
				}
				list_dyxzinfo.add(lst);
			}
		}
		return list_dyxzinfo;
	}
	/**
	 * 权利列表转换为权利信息
	 * @param request
	 * @param response
	 * @author yuxuebin
	 * @date 2017年02月28日 10:13:10
	 * @return
	 * @throws ParseException 
	 */
	private void ConvertRightInfoFromRightsEx(List<List<HashMap<String,String>>> list_rightinfo,List<List<Rights>> list_rights,List<String> list_rightid_xz,String bdcdylx,String bdcdyid) throws ParseException{
		List<String> list_mainqllx=Arrays.asList(("1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,24,28,29,30,36").split(","));
		HashMap<String,String> qlid_qlrnames=getHolderQlrNames(list_rights);
		//获取权利人信息
		for(List<Rights> list_rights_1:list_rights){
			List<HashMap<String,String>> list_rightsinfo_1=new ArrayList<HashMap<String,String>>();
			int i=0;
			for(Rights rights:list_rights_1){
				i++;
				HashMap<String,String> rightsinfo=new HashMap<String, String>();
				String djlx=StringHelper.FormatByDatatype(rights.getDJLX());
				String qllx=StringHelper.FormatByDatatype(rights.getQLLX());
				String qlid=StringHelper.FormatByDatatype(rights.getId());
				rightsinfo.put("QLID", qlid);
				rightsinfo.put("DJLX", djlx);
				rightsinfo.put("QLLX", qllx);
				rightsinfo.put("BDCQZH", rights.getBDCQZH());
				rightsinfo.put("YWH", rights.getYWH());
				rightsinfo.put("YWLSH", "");	
				rightsinfo.put("TITLE", "");
				rightsinfo.put("TPLNAME", "SYQ");//前端模版类型
				rightsinfo.put("QLRMC", "");//权利人名称
				rightsinfo.put("YWLX","");//业务类型
			    rightsinfo.put("MAINQLFLAG", "FALSE");//主体权利标记
				String type=GetQLType(djlx,qllx);
				if(!StringHelper.isEmpty(type))
				{
					rightsinfo.remove("TPLNAME");
					rightsinfo.put("TPLNAME", type);//前端模版类型
					if("SYQ".equals(type))
					{
						rightsinfo.remove("MAINQLFLAG");
						rightsinfo.put("MAINQLFLAG", "TRUE");//主体权利标记
					}
				}
				String ywlx=GetYWLX(djlx, qllx, BDCDYLX.initFrom(bdcdylx));
				if(!StringHelper.isEmpty(ywlx)){
					rightsinfo.remove("YWLX");
					rightsinfo.put("YWLX", ywlx);//前端模版类型
				}
				if(!StringHelper.isEmpty(qlid_qlrnames) && qlid_qlrnames.size()>0){
					String qlrmc=qlid_qlrnames.get(qlid);
					if(!StringHelper.isEmpty(qlrmc)){
						rightsinfo.remove("QLRMC");
						rightsinfo.put("QLRMC", qlrmc);//权利人名称
					}	
				}
//				String qlrmc=getQlrmc(qlid);//TODO-咋处理一下
//				if(!StringHelper.isEmpty(qlrmc)){
//					rightsinfo.remove("QLRMC");
//					rightsinfo.put("QLRMC", qlrmc);//权利人名称
//				}
				if(QLLX.DIYQ.Value.equals(rights.getQLLX()))
				{
					rightsinfo.remove("TITLE");
					rightsinfo.put("TITLE","抵");
				}else if(DJLX.CFDJ.Value.equals(rights.getDJLX()) && QLLX.QTQL.Value.equals(rights.getQLLX())){
					rightsinfo.remove("TITLE");
					rightsinfo.put("TITLE","查");
					SubRights sub=getSubRights(rights.getFSQLID());//TODO 咋处理一下
					if(sub !=null){
						String cfjg=StringHelper.FormatByDatatype(sub.getCFJG());
						rightsinfo.remove("QLRMC");
						rightsinfo.put("QLRMC", cfjg);//权利人名称
					}
				}else if(DJLX.YYDJ.Value.equals(rights.getDJLX()) && QLLX.QTQL.Value.equals(rights.getQLLX())){
					rightsinfo.remove("TITLE");
					rightsinfo.put("TITLE","异");
				}else if(DJLX.ZXDJ.Value.equals(rights.getDJLX())) {
					rightsinfo.remove("TITLE");
					rightsinfo.put("TITLE", "销");
				}if("800".equals(rights.getDJLX())&&"98".equals(rights.getQLLX())){
					rightsinfo.remove("TITLE");
					rightsinfo.put("TITLE", "解");
					SubRights sub=getSubRights(rights.getFSQLID());//TODO 咋处理一下
					if(sub !=null){
						String jfjg=StringHelper.FormatByDatatype(sub.getJFJG());
						rightsinfo.remove("QLRMC");
						rightsinfo.put("QLRMC", jfjg);//权利人名称
					}
				}
				if(!StringHelper.isEmpty(rights.getYWH())){
					BDCS_XMXX xmxx=Global.getXMXX(rights.getYWH());
					if(xmxx!=null){
						rightsinfo.put("YWLSH", xmxx.getYWLSH());
					}
				}
				rightsinfo.put("DJSJ", StringHelper.FormatByDatetime(rights.getDJSJ()));
				rightsinfo.put("SJ", StringHelper.FormatDateOnType(rights.getDJSJ(), "yyyy-MM-dd HH:mm:ss"));
				
				String status="1";
				if(rights instanceof BDCS_QL_GZ){
					BDCS_XMXX xmxx=Global.getXMXXbyXMBH(rights.getXMBH());
					if(xmxx!=null){
						if("400".equals(xmxx.getDJLX())){
							rightsinfo.put("DJLX", "400");
							rightsinfo.remove("TITLE");
							rightsinfo.put("TITLE", "销");
						}
						if("800".equals(xmxx.getDJLX())&&"98".equals(xmxx.getQLLX())){
							rightsinfo.put("DJLX", "800");
							rightsinfo.put("QLLX", "98");
							rightsinfo.remove("TITLE");
							rightsinfo.put("TITLE", "解");
						}
						rightsinfo.put("YWH", xmxx.getPROJECT_ID());
						rightsinfo.put("YWLSH", xmxx.getYWLSH());
						rightsinfo.put("DJSJ", "");
					}
					status="0";
				}else if(!list_rightid_xz.contains(rights.getId())){
					status="2";
				}
				rightsinfo.put("STATUS", status);//0在办，1有效，2注销
				list_rightsinfo_1.add(rightsinfo);
				if(i==list_rights_1.size()&&"2".equals(status)){
					boolean baddzxql=true;
					if(list_mainqllx.contains(rights.getQLLX())&&!"700".equals(rights.getDJLX())){
						baddzxql=false;
					}
					if(baddzxql){
						HashMap<String,String> rightsinfo_zx=new HashMap<String, String>();
						rightsinfo_zx.put("QLID", rights.getId());
						rightsinfo_zx.put("BDCQZH", rights.getBDCQZH());
						rightsinfo_zx.put("DJLX", "400");
						rightsinfo_zx.put("QLLX", rights.getQLLX());
						rightsinfo_zx.put("TITLE", "销");
						rightsinfo_zx.put("TPLNAME", "DYQ");//前端模版类型
						rightsinfo_zx.put("QLRMC", "");//权利人名称
						rightsinfo_zx.put("YWLX","注销登记");//业务类型
						if("800".equals(rights.getDJLX())){
							rightsinfo_zx.put("DJLX", "800");
							rightsinfo_zx.put("QLLX", "98");
							rightsinfo_zx.remove("TITLE");
							rightsinfo_zx.put("TITLE", "解");
							rightsinfo_zx.remove("TPLNAME");
							rightsinfo_zx.put("TPLNAME", "CFDJ");//前端模版类型
							rightsinfo_zx.remove("YWLX");
							rightsinfo_zx.put("YWLX", "查封登记-解封");
						}
						SubRights subright=null;
						if(!StringHelper.isEmpty(rights.getFSQLID())){
							subright=RightsTools.loadSubRights(DJDYLY.LS, rights.getFSQLID());
						}else{
							subright=RightsTools.loadSubRightsByRightsID(DJDYLY.LS, rights.getId());
						}
						if(subright!=null){		
							if("800".equals(rights.getDJLX())){
								String jfjg=StringHelper.FormatByDatatype(subright.getJFJG());
								rightsinfo_zx.remove("QLRMC");
								rightsinfo_zx.put("QLRMC", jfjg);//权利人名称
							}
							rightsinfo_zx.put("YWH", subright.getZXDYYWH());
							rightsinfo_zx.put("YWLSH", "");
							if(!StringHelper.isEmpty(subright.getZXDYYWH())){
								BDCS_XMXX xmxx_zx=Global.getXMXX(subright.getZXDYYWH());
								if(xmxx_zx!=null){
									rightsinfo_zx.put("YWLSH", xmxx_zx.getYWLSH());
								}
							}
							rightsinfo_zx.put("DJSJ", StringHelper.FormatByDatetime(subright.getZXSJ()));
							rightsinfo_zx.put("SJ", StringHelper.FormatDateOnType(subright.getZXSJ(), "yyyy-MM-dd HH:mm:ss"));
							rightsinfo_zx.put("STATUS", "2");//0在办，1有效，2注销
						}
						list_rightsinfo_1.add(rightsinfo_zx);
					}
				}
			}
			list_rightinfo.add(list_rightsinfo_1);
		}
		list_rightinfo=combinationDYXZAndRights(list_rightinfo,bdcdyid);
		
	}
	
	/**
	 * 组合单元限制与权利之间的排序顺序
	 * @param list_rightinfo ：权利信息
	 * @param bdcdyid：不动产单元ID
	 * @return
	 * @throws ParseException
	 */
	private  List<List<HashMap<String,String>>>  combinationDYXZAndRights(List<List<HashMap<String,String>>> list_rightinfo,String bdcdyid) throws ParseException{
		List<List<HashMap<String,String>>> lstdyxz=getDYXZInfos(bdcdyid);
		//添加单元限制信息
				if(!StringHelper.isEmpty(lstdyxz) && lstdyxz.size()>0){
					for(int k=0;k<lstdyxz.size();k++){
						List<HashMap<String,String>> lstdyxzinfos=lstdyxz.get(k);
						if(!StringHelper.isEmpty(lstdyxzinfos) && lstdyxzinfos.size()>0)
						{
							HashMap<String,String> dyxzinfo=lstdyxzinfos.get(0);//默认取第0了就行了
							if(!StringHelper.isEmpty(dyxzinfo) && !dyxzinfo.isEmpty())
							{
								String currentstate=dyxzinfo.get("STATUS");
								if("0".equals(currentstate)){//正在办理的放在权利最后面
									list_rightinfo.add(list_rightinfo.size(), lstdyxzinfos);
									break;
								}
								String currentsj=dyxzinfo.get("SJ");
								if(!StringHelper.isEmpty(currentsj)){//时间不为空，找在权利中节点的位置，时间为空，直接挂在主体权利上第一个节点
									if(!StringHelper.isEmpty(list_rightinfo) && list_rightinfo.size()>0){
										if(list_rightinfo.size()==1){
											lstdyxzinfos.add(dyxzinfo);
											break;
										}else{
											for(int m=0;m<list_rightinfo.size()-1;m++){
												List<HashMap<String,String>> beforelst=list_rightinfo.get(m);
												List<HashMap<String,String>> afterlst=list_rightinfo.get(m+1);
												HashMap<String,String> beforeinfo=beforelst.get(0);//默认取第0了就行了
												HashMap<String,String> afterinfo=afterlst.get(0);//默认取第0了就行了
												String beforesj=beforeinfo.get("SJ");
												String aftersj=afterinfo.get("SJ");
												String beforestate=beforeinfo.get("STATUS");
												String afterstate=afterinfo.get("STATUS");
												if("0".equals(beforestate)){
													list_rightinfo.add(m, lstdyxzinfos);
													break;
												}else if("0".equals(afterstate)){
													list_rightinfo.add(m+1, lstdyxzinfos);
													break;
												}
												Date currentdt=StringHelper.FormatByDate(currentsj, "yyyy-MM-dd HH:mm:ss");
												if(!StringHelper.isEmpty(beforesj) && !StringHelper.isEmpty(aftersj)){
												Date beforedt=StringHelper.FormatByDate(beforesj, "yyyy-MM-dd HH:mm:ss");
												Date afterdt=StringHelper.FormatByDate(aftersj, "yyyy-MM-dd HH:mm:ss");
									
												/*Date1.after(Date2),当Date1大于Date2时，返回TRUE，当小于等于时，返回false； 
		   		   								  Date1.before(Date2)，当Date1小于Date2时，返回TRUE，当大于等于时，返回false； */
												if(beforedt.after(currentdt)){
													list_rightinfo.add(m, lstdyxzinfos);
													break;
												}
												else if(beforedt.before(currentdt) && afterdt.after(currentdt)){
													list_rightinfo.add(m+1, lstdyxzinfos);
													break;
												} else if(m==list_rightinfo.size()-2)
												{
													list_rightinfo.add(lstdyxzinfos);
													break;
												}
												}else if(StringHelper.isEmpty(beforesj) && !StringHelper.isEmpty(aftersj)){
													Date afterdt=StringHelper.FormatByDate(aftersj, "yyyy-MM-dd HH:mm:ss");
													if(afterdt.after(currentdt)){
														list_rightinfo.add(m+1, lstdyxzinfos);
													}else if(m==list_rightinfo.size()-1){
														list_rightinfo.add(lstdyxzinfos);
													break;
													}
												}else if (StringHelper.isEmpty(beforesj) && StringHelper.isEmpty(aftersj))
												{
													if(m==list_rightinfo.size()-2){
														list_rightinfo.add(lstdyxzinfos);
														break;
														}
												}
											}
										}
									}
								}else{
								
									if(!StringHelper.isEmpty(list_rightinfo) && list_rightinfo.size()>0){
										if(list_rightinfo.size()==1){
											lstdyxzinfos.add(dyxzinfo);
											break;
										}else{
											for(int m=0;m<list_rightinfo.size();m++){
												List<HashMap<String,String>> beforelst=list_rightinfo.get(m);
												HashMap<String,String> beforeinfo=beforelst.get(0);//默认取第0了就行了
												String beforeflag= beforeinfo.get("MAINQLFLAG");//主体权利标记
												//获取到主体权利时，直接放在下面 ，或没有主体权利直接放到最后一个
												if("TRUE".equals(beforeflag) || m==list_rightinfo.size()-1){
													list_rightinfo.add(m+1, lstdyxzinfos);
													break;
												}
											}
										}
									}
								}
							}
						}
					}
				}
		return list_rightinfo;
	}
	/**
	 * 构建所有的权利对应的权利人信息
	 * @param list_rights：权利集合
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private HashMap<String,String> getHolderQlrNames(List<List<Rights>> list_rights){
		HashMap<String,String> qlid_qlrnames=new HashMap<String, String>();
		List<String> lstqlid=new ArrayList<String>();
		for(List<Rights> list_rights_1:list_rights){			
			for(Rights rights:list_rights_1){				
				String qlid=StringHelper.FormatByDatatype(rights.getId());
				if(!lstqlid.contains(qlid) && !StringHelper.isEmpty(qlid))
				{				
					lstqlid.add(qlid);
				}
			}
		}
		String inqlids="";
		for(int i=0;i<lstqlid.size();i++){
		 if(i==0){			 
			 inqlids="'"+lstqlid.get(i)+"'";
		 }	else{
			 inqlids +=",'"+lstqlid.get(i)+"'";			 
		 }
		}
		 if(!StringHelper.isEmpty(inqlids)){
			 String sqlqlr="select  QLID,QLRMC,'01' LY,QLRID  from bdck.bdcs_qlr_gz where qlid in("+inqlids+") ";
			 sqlqlr +=" union all ";
			 sqlqlr +=" select QLID,QLRMC,'02' LY,QLRID  from bdck.bdcs_qlr_ls where qlid in("+inqlids+") ";
			 CommonDao dao = baseCommonDao;
			 List<String> lstqlrids=new ArrayList<String>();
			List<Map> lstmap= dao.getDataListByFullSql(sqlqlr);
			 if(lstmap !=null && lstmap.size()>0){
				 for(int j=0;j<lstmap.size();j++){
					 Map map=lstmap.get(j);
					 String qlid=StringHelper.FormatByDatatype(map.get("QLID"));
					 String qlrid=StringHelper.FormatByDatatype(map.get("QLRID"));
					 String qlrmc=StringHelper.FormatByDatatype(map.get("QLRMC"));
					 if(!StringHelper.isEmpty(qlrid)){
						 if(!lstqlrids.contains(qlrid)){
							 lstqlrids.add(qlrid);
							 if(qlid_qlrnames.containsKey(qlid))
							 {
								 String qlrnames=StringHelper.FormatByDatatype(qlid_qlrnames.get(qlid));
								 qlrnames +=","+qlrmc;
								 qlid_qlrnames.remove(qlid);
								 qlid_qlrnames.put(qlid, qlrnames);
							 }else{
								 qlid_qlrnames.put(qlid, qlrmc);
							 }
						 }						 
					 }
				 }				 
			 }			 
		 }
		return qlid_qlrnames;
	}
   
	/**
	 * 权利信息列表添加在办权利
	 * @param request
	 * @param response
	 * @author yuxuebin
	 * @date 2017年02月28日 10:13:10
	 * @return
	 */
	private void AppendRightsOnGZ(List<List<Rights>> list_all,List<Rights> list_right_gz){
		List<String> list_mainqllx=Arrays.asList(("1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,24,28,29,30,36").split(","));
		for(Rights right:list_right_gz){
			boolean badd_first=false;
			boolean bhavadd=false;
			if(list_mainqllx.contains(right.getQLLX())&&!"700".equals(right.getDJLX())){
				badd_first=true;
			}else if(StringHelper.isEmpty(right.getLYQLID())){
				badd_first=true;
			}else{
				for(List<Rights> list_rights:list_all){
					for(Rights rights_ly:list_rights){
						if(right.getLYQLID().equals(rights_ly.getId())){
							boolean badd=false;
							if("99".equals(right.getQLLX())&&"99".equals(rights_ly.getQLLX())&&rights_ly.getDJLX().equals(right.getDJLX())){
								badd=true;
							}else if(!StringHelper.isEmpty(right.getQLLX())&&right.getQLLX().equals(rights_ly.getQLLX())){
								badd=true;
							}else if("98".equals(right.getQLLX())&&"99".equals(rights_ly.getQLLX())&&"800".equals(right.getDJLX())&&"800".equals(rights_ly.getDJLX())){
								badd=true;
							}
							if(badd){
								list_rights.add(right);
								bhavadd=true;
								break;
							}
						}
					}
					if(bhavadd){
						break;
					}
				}
			}
			if(badd_first||!bhavadd){
				List<Rights> list_rights=new ArrayList<Rights>();
				list_rights.add(right);
				list_all.add(list_rights);
			}
		}
	}
	
	/**
	 * 通过宗地的不动产单元ID查看自然幢信息
	 */
	@Override
	public Message querybuilding(int page, int rows, String zdbdcdyid) {
		Message msg = new Message();
		long count = 0;
		@SuppressWarnings("rawtypes")
		List<Map> listresult = null;
		/* ===============查詢字段=================== */
		String fulsql = "BDCDYID,BDCDYH,ZL,ZRZH,ZZDMJ,DSCS,DXCS,BDCDYLX,LY,XMMC,ZDBDCDYID,BZ  ";
		StringBuilder builder2 = new StringBuilder();
		builder2.append("select ").append(fulsql); 
		String selectstr = builder2.toString(); 
		/* ===============查詢表名=================== */
		
		StringBuilder builder = new StringBuilder();
		StringBuilder builder_qf = new StringBuilder();
		StringBuilder builder_xf = new StringBuilder();
		//08预测自然幢、03自然幢
		builder_qf.append("SELECT BDCDYID,BDCDYH,ZL,ZRZH,ZZDMJ,DSCS,DXCS,'08' BDCDYLX,'02' LY, XMMC,ZDBDCDYID,BZ FROM BDCK.BDCS_ZRZ_XZY");
		builder_qf.append(" UNION ");
		builder_qf.append("SELECT BDCDYID,BDCDYH,ZL,ZRZH,ZZDMJ,DSCS,DXCS,'08' BDCDYLX,'04' LY, XMMC,ZDBDCDYID,BZ FROM BDCDCK.BDCS_ZRZ_GZY DCY WHERE NOT EXISTS (SELECT 1 FROM BDCK.BDCS_ZRZ_XZY XZY WHERE XZY.BDCDYID = DCY.BDCDYID)");
		
		builder_xf.append("SELECT BDCDYID,BDCDYH,ZL,ZRZH,ZZDMJ,DSCS,DXCS,'03' BDCDYLX,'02' LY, XMMC,ZDBDCDYID,BZ FROM BDCK.BDCS_ZRZ_XZ");
		builder_xf.append(" UNION ");
		builder_xf.append("SELECT BDCDYID,BDCDYH,ZL,ZRZH,ZZDMJ,DSCS,DXCS,'03' BDCDYLX,'04' LY, XMMC,ZDBDCDYID,BZ FROM BDCDCK.BDCS_ZRZ_GZ DC WHERE NOT EXISTS (SELECT 1 FROM BDCK.BDCS_ZRZ_XZ XZ WHERE XZ.BDCDYID = DC.BDCDYID)");
		builder.append(" from ( "); 
	    builder.append(builder_qf.toString()).append(" UNION ").append(builder_xf.toString());		
		builder.append(") ZRZ");
		String fromstr = builder.toString();
		/* ===============篩選=================== */
		StringBuilder builder3 = new StringBuilder();
		builder3.append(" where 1=1 ");
		// 坐落
		if (!StringUtils.isEmpty(zdbdcdyid)) {
			builder3.append(" and ZDBDCDYID = '" + zdbdcdyid + "'");
		}		
		String wherestr = builder3.toString();
		String fromSql = fromstr + wherestr;
		builder3.append(" ORDER BY BDCDYLX,ZL");
		String fullSql = selectstr + fromstr + wherestr;
		/* 为了提高效率，先不查询符合条件的记录总数，将来改为异步获取 */
		CommonDao dao = baseCommonDao;
		count = dao.getCountByFullSql(fromSql);
		listresult = dao.getPageDataByFullSql(fullSql, page, rows);			
		msg.setTotal(count);
		msg.setRows(listresult);
		return msg;		
	}
	
	/*
	 * 获取前端配置模版类型
	 */
	private  String GetQLType(String djlx, String qllx) {
		String type = "SYQ";
		if (QLLX.DIYQ.Value.equals(qllx)) {
			type = "DYQ";
		} else if (DJLX.CFDJ.Value.equals(djlx)) {
			type = "CFDJ";
		} else if (DJLX.YYDJ.Value.equals(djlx)) {
			type = "YYDJ";
		}
		return type;
	}
	
	/**
	 * 获取业务类型
	 * @param djlx：登记类型
	 * @param qllx：权利类型
	 * @param dylx:不动产单元类型
	 * @return
	 */
	private String GetYWLX(String djlx, String qllx, BDCDYLX dylx) {
		String ywlx = "";
		if (BDCDYLX.YCH.equals(dylx)) {
			if (DJLX.CFDJ.Value.equals(djlx) || DJLX.YYDJ.Value.equals(djlx)) {
				if(StringHelper.isEmpty(DJLX.initFrom(djlx))){
					ywlx = "----" + "-期房";
				}else{
					ywlx = DJLX.initFrom(djlx).Name + "-期房";
				}
			} else {
				if (QLLX.DIYQ.Value.equals(qllx)) {
					if(DJLX.YGDJ.Value.equals(djlx)){
						ywlx = "期房抵押预告登记";
					}else{
						ywlx = "在建工程抵押登记";
					}
					
				} else {
					ywlx = "期房预告登记";
				}
			}
		} else {
			if (DJLX.CFDJ.Value.equals(djlx) || DJLX.YYDJ.Value.equals(djlx)) {
				ywlx = DJLX.initFrom(djlx).Name;
			} else {
				if (DJLX.YGDJ.Value.equals(djlx) && "99".equals(qllx)) {
					ywlx = "转移预告登记";
				} else if (DJLX.YGDJ.Value.equals(djlx)
						&& QLLX.DIYQ.Value.equals(qllx)) {
					ywlx = "抵押预告登记";
				} else if (QLLX.GYJSYDSHYQ.Value.equals(qllx)
						|| QLLX.ZJDSYQ.Value.equals(qllx)
						|| QLLX.JTJSYDSYQ.Value.equals(qllx)) {
					if(StringHelper.isEmpty(DJLX.initFrom(djlx))){
						ywlx = "----" + "-使用权";
					}else{
						if(djlx != null && !djlx.equals("")){
						   ywlx = DJLX.initFrom(djlx).Name + "-使用权";
						}
					}
				} else if (QLLX.JTTDSYQ.Value.equals(qllx)
						|| QLLX.GJTDSYQ.Value.equals(qllx)
						|| QLLX.GYJSYDSHYQ_FWSYQ.Value.equals(qllx)
						|| QLLX.ZJDSYQ_FWSYQ.Value.equals(qllx)
						|| QLLX.JTJSYDSYQ_FWSYQ.Value.equals(qllx)) {
					if(StringHelper.isEmpty(DJLX.initFrom(djlx))){
						ywlx = "----" + "-所有权";
					}else{
						if(djlx != null && !djlx.equals("")){
							ywlx = DJLX.initFrom(djlx).Name + "-所有权";
						}	
					}
				} else if (QLLX.DIYQ.Value.equals(qllx)) {
					if(StringHelper.isEmpty(DJLX.initFrom(djlx))){
						ywlx = "----" + "-抵押权";
					}else{
						if(djlx != null && !djlx.equals("")){
						  ywlx = DJLX.initFrom(djlx).Name + "-抵押权";
						}
					}
				} else {
					if(StringHelper.isEmpty(DJLX.initFrom(djlx))){
						ywlx = "----" ;
					}else{
						if(djlx != null && !djlx.equals("")){
						  ywlx = DJLX.initFrom(djlx).Name;
						}
					}
					if(StringHelper.isEmpty(QLLX.initFrom(qllx))){
						ywlx = ywlx+"-"+"----" ;
					}else{
						ywlx = ywlx+QLLX.initFrom(qllx).Name;
					}
				}
			}
		}
		return ywlx;
	}
	/**
	 * 升序排序
	 * @param lst
	 */
	private List<UnitInfo> ascSortUnit(List<UnitInfo> lst){
		if(!StringHelper.isEmpty(lst) && lst.size()>0){
			  UnitInfo[] arruint = (UnitInfo[])lst.toArray(new UnitInfo[lst.size()]); 
			for(int i=0;i<arruint.length-1;i++){
				for(int j=0;j<arruint.length-i-1;j++){
					UnitInfo preunit=arruint[j];
					UnitInfo nextunit=arruint[j+1];
					if(preunit.getLevel()>nextunit.getLevel()){
						UnitInfo temp=arruint[j];
				          arruint[j]=arruint[j+1];
				          arruint[j+1]=temp;
					}
				}
			}
			lst.clear();
			lst=Arrays.asList(arruint);
		}
		return lst;
	}
	
	/**
	 * 获取权利人信息
	 * @param qlid
	 * @return
	 */
//	private String getQlrmc(String qlid){
//		String qlrmc="";
//		List<RightsHolder> holder=RightsHolderTools.loadRightsHolders(DJDYLY.LS, qlid);
//		if(holder ==null){
//			holder=RightsHolderTools.loadRightsHolders(DJDYLY.GZ, qlid);
//		}
//		if(holder !=null && holder.size()>0){
//			for(int i=0;i<holder.size();i++){
//				String mc=holder.get(i).getQLRMC();
//				 if(!StringHelper.isEmpty(mc)) {
//					 mc=mc.trim();
//				 }else {
//					 mc="-";
//				 }
//				if(i==0){
//					qlrmc=mc;
//				}else {
//					qlrmc=qlrmc+","+mc;
//				}
//			}
//		}
//		return qlrmc;
//	}
	private SubRights getSubRights(String fsqlid){
		SubRights sub=RightsTools.loadSubRights(DJDYLY.LS,fsqlid);
		if(sub ==null){
			sub=RightsTools.loadSubRights(DJDYLY.GZ, fsqlid);
		}
		return sub;
	}
}
