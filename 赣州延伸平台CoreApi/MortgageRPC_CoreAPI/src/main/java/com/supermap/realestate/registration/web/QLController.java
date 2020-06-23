package com.supermap.realestate.registration.web;

import java.io.UnsupportedEncodingException;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.supermap.realestate.registration.model.*;
import org.apache.bcel.generic.NEW;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.supermap.realestate.registration.model.interfaces.House;
import com.supermap.realestate.registration.model.interfaces.RealUnit;
import com.supermap.realestate.registration.model.interfaces.Rights;
import com.supermap.realestate.registration.model.interfaces.RightsHolder;
import com.supermap.realestate.registration.service.QLService;
import com.supermap.realestate.registration.service.impl.QLServiceImpl;
import com.supermap.realestate.registration.tools.NewLogTools;
import com.supermap.realestate.registration.tools.RightsHolderTools;
import com.supermap.realestate.registration.tools.RightsTools;
import com.supermap.realestate.registration.tools.UnitTools;
import com.supermap.realestate.registration.util.AccessRequired;
import com.supermap.realestate.registration.util.ConfigHelper;
import com.supermap.realestate.registration.util.ConstHelper;
import com.supermap.realestate.registration.util.ConstValue;
import com.supermap.realestate.registration.util.ConstValue.BDCDYLX;
import com.supermap.realestate.registration.util.ConstValue.DJDYLY;
import com.supermap.realestate.registration.util.ConstValue.SQRLB;
import com.supermap.realestate.registration.util.RequestHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.realestate.registration.util.YwLogUtil;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import com.supermap.wisdombusiness.web.Message;
import com.supermap.wisdombusiness.web.ResultMessage;
import com.supermap.wisdombusiness.web.ui.Page;

/**
 * 
 * @Description:权利相关信息控制器
 * @author 刘树峰
 * @date 2015年6月12日 上午11:51:16
 * @Copyright SuperMap
 */
@Controller
@RequestMapping("/ql")
public class QLController {

	/** 权利service */
	@Autowired
	private QLService qlService;

	@Autowired
	private CommonDao dao;

	@RequestMapping(value = "/qlr/qlrInfo", method = RequestMethod.GET)
	public String getQlrInfo() {
		return "/realestate/registration/modules/common/qlrInfo";
	}

	/**
	 * 获取某个权利详细信息（URL:"/{xmbh}/qls/{qlid}",Method：GET）
	 * 
	 * @param xmbh
	 * @param qlid
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/{xmbh}/qls/{qlid}", method = RequestMethod.GET)
	public @ResponseBody BDCS_QL_GZ GetQLInfo(@PathVariable("xmbh") String xmbh, @PathVariable("qlid") String qlid,
			HttpServletRequest request, HttpServletResponse response) {
		BDCS_QL_GZ bdcs_QL_GZ = new BDCS_QL_GZ();
		bdcs_QL_GZ = qlService.GetQL(qlid);
		if (bdcs_QL_GZ != null) {
			String fj = bdcs_QL_GZ.getFJ();
			if (!StringHelper.isEmpty(fj)) {
				fj = fj.replaceAll(" ", "\u00A0");
			}
			bdcs_QL_GZ.setFJ(fj);
			String djyy = bdcs_QL_GZ.getDJYY();
			if (!StringHelper.isEmpty(djyy)) {
				djyy = djyy.replaceAll(" ", "\u00A0");
			}
			bdcs_QL_GZ.setDJYY(djyy);
			String gyrqk = bdcs_QL_GZ.getGYRQK();
			if (!StringHelper.isEmpty(gyrqk)) {
				gyrqk = gyrqk.replaceAll(" ", "\u00A0");
			}
			bdcs_QL_GZ.setGYRQK(gyrqk == null ? "" :gyrqk);
		}
		
		return bdcs_QL_GZ;
	}
	/**
	 *  自动获取土地使用权人
	 *  @author weilb
	 */
	@RequestMapping(value = "/gettdshyqr/", method = RequestMethod.GET,produces = {"application/text;charset=UTF-8"})
	public @ResponseBody String getTdshyqr(HttpServletRequest request, HttpServletResponse response) {
		String tdshyqr = "";
		String xmbh = request.getParameter("xmbh");
		String qlid = request.getParameter("qlid");
		BDCS_QL_GZ ql  = dao.get(BDCS_QL_GZ.class,qlid);
		if(ql!=null){
		    List<BDCS_QLR_GZ> qlr_gz = dao.getDataList(BDCS_QLR_GZ.class, " QLID='"+qlid+"' AND XMBH='"+xmbh+"'");
		    if(qlr_gz!=null && qlr_gz.size()>0){
		    	for(int i = 0; i < qlr_gz.size(); i++){
		    		BDCS_QLR_GZ qlr = qlr_gz.get(i);
		    		if(qlr_gz.size()>1){
		    			if(i==qlr_gz.size()-1){
		    				tdshyqr += qlr.getQLRMC();
		    			}else{
		    				tdshyqr += qlr.getQLRMC()+",";
		    			}
		    		}else{
		    			tdshyqr += qlr.getQLRMC();
		    		}
		    	}
		    }
	    }  
		return tdshyqr;
	}
		/*String fwqlr = "";
		String xmbh = request.getParameter("xmbh");
		String qlid = request.getParameter("qlid");
		BDCS_QL_GZ ql  = dao.get(BDCS_QL_GZ.class,qlid);
		   if(ql!=null){
			    List<BDCS_QLR_GZ> qlr_gz = dao.getDataList(BDCS_QLR_GZ.class, " QLID='"+qlid+"' AND XMBH='"+xmbh+"'");
			    if(qlr_gz!=null && qlr_gz.size()>0){
				     fwqlr = qlr_gz.get(0).getQLRMC();
			    }
		    }
			List<BDCS_DJDY_GZ> djdy_gz = dao.getDataList(BDCS_DJDY_GZ.class," DJDYID='"+ql.getDJDYID()+"' AND XMBH='"+xmbh+"'");
			if(djdy_gz!=null && djdy_gz.size()>0){
				BDCS_H_XZ h_gz = dao.get(BDCS_H_XZ.class, djdy_gz.get(0).getBDCDYID());
					if(h_gz!=null){
						List<BDCS_DJDY_XZ> djdy_xz = dao.getDataList(BDCS_DJDY_XZ.class,  " BDCDYID='"+h_gz.getZDBDCDYID()+"'");
						if(djdy_xz !=null && djdy_xz.size()>0){
							List<BDCS_QL_XZ> ql_xz = dao.getDataList(BDCS_QL_XZ.class, " DJDYID='"+djdy_xz.get(0).getDJDYID()+"'");
							if(ql_xz!=null && ql_xz.size()>0){
							    List<BDCS_QLR_XZ> qlr_xz = dao.getDataList(BDCS_QLR_XZ.class, " QLID='"+ql_xz.get(0).getId()+"'");
							    if(qlr_xz!=null && qlr_xz.size()>0){
									tdshyqr = qlr_xz.get(0).getQLRMC();
							    }
							}
						}
					 }
				}else{
					BDCS_H_XZY h_xzy = dao.get(BDCS_H_XZY.class,djdy_gz.get(0).getBDCDYID());
					if(h_xzy!=null){
						List<BDCS_DJDY_XZ> djdy_xz = dao.getDataList(BDCS_DJDY_XZ.class,  " BDCDYID='"+h_xzy.getZDBDCDYID()+"'");
						if(djdy_xz !=null && djdy_xz.size()>0){
							List<BDCS_QL_XZ> ql_xz = dao.getDataList(BDCS_QL_XZ.class, " DJDYID='"+djdy_xz.get(0).getDJDYID()+"'");
							if(ql_xz!=null && ql_xz.size()>0){
							    List<BDCS_QLR_XZ> qlr_xz = dao.getDataList(BDCS_QLR_XZ.class, " QLID='"+ql_xz.get(0).getId()+"'");
							    if(qlr_xz!=null && qlr_xz.size()>0){
									tdshyqr = qlr_xz.get(0).getQLRMC();
							    }
							}
						}
					 }
				}
			if(fwqlr.trim().equals(tdshyqr.trim())){
				return tdshyqr;
			}else{
				return null;
			}*/
	
	
	
	/**
	 * 获取某个权利详细信息（URL:"/{xmbh}/qls/{qlid}",Method：GET）
	 * 
	 * @param xmbh
	 * @param qlid
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/{xmbh}/qlsex/{qlid}", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> GetQLInfoEx(@PathVariable("xmbh") String xmbh, @PathVariable("qlid") String qlid,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> m=qlService.GetQLEx(qlid);
		return m;
	}

	/**
	 * 获取房屋性质
	 * 
	 * @param bdcdyid
	 * @param ly
	 * @param dyly
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/getfwxz/{bdcdyid}/{dyly}/{ly}", method = RequestMethod.GET)
	public @ResponseBody String getfwxz(@PathVariable("bdcdyid") String bdcdyid, @PathVariable("ly") String ly,
			@PathVariable("dyly") String dyly, HttpServletRequest request, HttpServletResponse response) {
		String fwxz = "";
		BDCDYLX bdcdylx = BDCDYLX.initFrom(ly);
		DJDYLY djdyly = DJDYLY.initFrom(dyly);
		RealUnit unit = UnitTools.loadUnit(bdcdylx, djdyly, bdcdyid);
		if (!StringHelper.isEmpty(unit)) {
			if (unit instanceof House) {
				House house = (House) unit;
				if (!StringHelper.isEmpty(house)) {
					fwxz = house.getFWXZ();
				}
			}
		}
		return fwxz;
	}

	/**
	 * 保存房屋性质
	 * 
	 * @param bdcdyid
	 * @param ly
	 * @param dyly
	 * @param fwxz
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/savefwxz/{bdcdyid}/{dyly}/{ly}/{fwxz}", method = RequestMethod.GET)
	public void savefwxz(@PathVariable("bdcdyid") String bdcdyid, @PathVariable("ly") String ly,
			@PathVariable("dyly") String dyly, @PathVariable("fwxz") String fwxz, HttpServletRequest request,
			HttpServletResponse response) {
		BDCDYLX bdcdylx = BDCDYLX.initFrom(ly);
		DJDYLY djdyly = DJDYLY.initFrom(dyly);
		RealUnit unit = UnitTools.loadUnit(bdcdylx, djdyly, bdcdyid);
		if (DJDYLY.XZ.Value.equals(dyly)) {
			RealUnit lsunit = UnitTools.loadUnit(bdcdylx, DJDYLY.LS, bdcdyid);
			if (!StringHelper.isEmpty(lsunit)) {
				if (lsunit instanceof House) {
					House lshouse = (House) unit;
					if (!StringHelper.isEmpty(lshouse)) {
						lshouse.setFWXZ(fwxz);
						dao.update(lshouse);
					}
				}
			}
		}
		if (!StringHelper.isEmpty(unit)) {
			if (unit instanceof House) {
				House house = (House) unit;
				if (!StringHelper.isEmpty(house)) {
					house.setFWXZ(fwxz);
					dao.update(house);
				}
			}
		}
		YwLogUtil.addYwLog("保存房屋性质", ConstValue.SF.YES.Value, ConstValue.LOG.ADD);
		dao.flush();
	}
	


	/**
	 * 更新某个权利详细信息（登记原因、附记、持证方式、取得价格、期限、）（URL:"/{xmbh}/qls/{qlid}",Method：POST）
	 * 
	 * @param xmbh
	 * @param qlid
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/{xmbh}/qls/{qlid}", method = RequestMethod.POST)
	@AccessRequired.UnBoardRequired
	public @ResponseBody ResultMessage UpdateQLInfo(@PathVariable("xmbh") String xmbh,
			@PathVariable("qlid") String qlid, HttpServletRequest request, HttpServletResponse response) {
		ResultMessage resultMessage = new ResultMessage();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date qssj = new Date();
		try {
			String strqssj = request.getParameter("qLQSSJ");
			if (!StringHelper.isEmpty(strqssj)) {
				qssj = sdf.parse(request.getParameter("qLQSSJ"));
			} else {
				qssj = null;
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Date jssj = new Date();
		try {
			String strjssj = request.getParameter("qLJSSJ");
			if (!StringHelper.isEmpty(strjssj)) {
				jssj = sdf.parse(request.getParameter("qLJSSJ"));
			} else {
				jssj = null;
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		String djyy = request.getParameter("dJYY").replaceAll("\u00A0", " ");
		String fj = request.getParameter("fJ").replaceAll("\u00A0", " ");
		String gyrqk = request.getParameter("gYRQK");
		if(!StringHelper.isEmpty(gyrqk)){
			gyrqk = gyrqk.replaceAll("\u00A0", " ");
		}
		String czfs = request.getParameter("cZFS");
		String qdjg = request.getParameter("qDJG");
		String tdshyqr = request.getParameter("tDSHYQR");
		String bz = request.getParameter("bZ");
		String tdyt = "";
		String qs = request.getParameter("qS");
		try {
			tdyt = RequestHelper.getParam(request, "tdyt");
		} catch (Exception e) {
		}
		if (StringUtils.isEmpty(qdjg)) {
			qdjg = "0";
		}
		double lqdjg = Double.parseDouble(qdjg);
		boolean brebuild = false;
		BDCS_QL_GZ ql = qlService.GetQL(qlid);
		String tempczfs= ql.getCZFS();
		brebuild = !(!StringUtils.isEmpty(ql.getCZFS()) && ql.getCZFS().equals(czfs));
		ql.setId(qlid);
		ql.setQLQSSJ(qssj);
		ql.setQDJG(lqdjg);
		ql.setQLJSSJ(jssj);
		ql.setDJYY(djyy);
		ql.setFJ(fj);
		ql.setCZFS(czfs);
		ql.setTDSHYQR(tdshyqr);
		ql.setBZ(bz);
		ql.setQS(qs);
		ql.setGYRQK(gyrqk == null ? "" :gyrqk);
	    BDCS_FSQL_GZ fsql=qlService.GetFSQL(ql.getFSQLID());
	    if(fsql !=null){
	    	if (!StringHelper.isEmpty(request.getParameter("dYQNR"))) {
				String dyqnr = request.getParameter("dYQNR").replaceAll("\u00A0", " ");			
		    	dyqnr=StringHelper.FormatByDatatype(dyqnr);
		    	fsql.setDYQNR(dyqnr);
		    	dao.update(fsql);
		    	dao.flush();
			}
	    }
		//鹰潭房产共享，共享项目编号保存至权利CASENUM中  edit by 王帅 2016/8/20
		String fcgxxmbh = request.getParameter("fcgxxmbh");
		if(fcgxxmbh != "" && fcgxxmbh != null){
			ql.setCASENUM(fcgxxmbh);
		}
		String XZQHDM=ConfigHelper.getNameByValue("XZQHDM");
		if("6501".equals(XZQHDM.length()>4?XZQHDM.substring(0,4):XZQHDM)){
			if(!StringHelper.isEmpty(request.getParameter("CASENUM"))){
				ql.setCASENUM(request.getParameter("CASENUM"));
			}
		}
		if (brebuild) {
			JSONObject jsonobj=NewLogTools.getJSONByXMBH(xmbh);
			jsonobj.put("OperateType", "持证方式");
			JSONObject jsonql=new JSONObject();
			jsonql.put("QLID", ql.getId());
			String tempczfsmc=ConstHelper.getNameByValue("CZFS", tempczfs);
			String czfsmc=ConstHelper.getNameByValue("CZFS", czfs);
			jsonql.put("CZFS修改方式", "从"+tempczfsmc+"修改为"+czfsmc);
			jsonobj.put("QLINFOS", jsonql);
			NewLogTools.saveLog(jsonobj.toString(), xmbh, "5", "持证方式");
			qlService.UpdateQLandRebuildRelation(ql);
		} else {
			qlService.UpdateQL(ql);
		}
		if (!StringHelper.isEmpty(tdyt)) {
			qlService.Updatetdyt(ql.getDJDYID(), tdyt, xmbh);
		}
		if(!StringHelper.isEmpty(request.getParameterMap().get("zyqlrmc1"))){
			String[] zyqlrids=(String[])request.getParameterMap().get("zyqlrmc1");
			qlService.UpdateZYQLRList(zyqlrids,ql);
		}
		
		resultMessage.setSuccess("true");
		resultMessage.setMsg("更新成功!");
		YwLogUtil.addYwLog("保存房屋性质-更新成功", ConstValue.SF.YES.Value, ConstValue.LOG.UPDATE);
		return resultMessage;
	}

	/**
	 * 更新使用权宗地权利时，不存在起始时间跟结束时间
	 * 
	 * @作者 海豹
	 * @创建时间 2015年7月19日下午6:16:11
	 * @param xmbh
	 * @param qlid
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/{xmbh}/syqqls/{qlid}", method = RequestMethod.POST)
	public @ResponseBody ResultMessage UpdateSYQQLInfo(@PathVariable("xmbh") String xmbh,
			@PathVariable("qlid") String qlid, HttpServletRequest request, HttpServletResponse response) {
		ResultMessage resultMessage = new ResultMessage();
		String djyy = request.getParameter("dJYY").toString();
		String fj = request.getParameter("fJ");
		String czfs = request.getParameter("cZFS");
		String qdjg = request.getParameter("qDJG");
		if (StringUtils.isEmpty(qdjg)) {
			qdjg = "0";
		}
		boolean brebuild = false;
		double lqdjg = Double.parseDouble(qdjg);
		BDCS_QL_GZ ql = qlService.GetQL(qlid);
		brebuild = !(ql.getCZFS().equals(czfs));
		ql.setQDJG(lqdjg);
		ql.setDJYY(djyy);
		ql.setFJ(fj);
		ql.setCZFS(czfs);
		if (brebuild) {
			qlService.UpdateQLandRebuildRelation(ql);
		} else {
			qlService.UpdateQL(ql);
		}
		resultMessage.setSuccess("true");
		resultMessage.setMsg("更新成功!");
		YwLogUtil.addYwLog("更新使用权宗地权利-更新成功", ConstValue.SF.YES.Value, ConstValue.LOG.UPDATE);
		return resultMessage;
	}

	/**
	 * 更新权利中的登记原因和附记（URL:"/{xmbh}/gzqls/{qlid}",Method：POST）
	 * 
	 * @Title: UpdateGZQLInfo
	 * @author:unknown
	 * @date：2015年7月18日 下午10:09:09
	 * @param xmbh
	 * @param qlid
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/{xmbh}/gzqls/{qlid}", method = RequestMethod.POST)
	public @ResponseBody ResultMessage UpdateGZQLInfo(@PathVariable("xmbh") String xmbh,
			@PathVariable("qlid") String qlid, HttpServletRequest request, HttpServletResponse response) {
		ResultMessage resultMessage = new ResultMessage();
		String djyy = request.getParameter("dJYY").toString();
		String fj = request.getParameter("fJ");
		BDCS_QL_GZ ql = qlService.GetQL(qlid);
		ql.setDJYY(djyy);
		ql.setFJ(fj);
		qlService.UpdateQL(ql);
		resultMessage.setSuccess("true");
		resultMessage.setMsg("更新成功!");
		YwLogUtil.addYwLog("更新权利中的登记原因和附记-更新成功", ConstValue.SF.YES.Value, ConstValue.LOG.UPDATE);
		return resultMessage;
	}

	/**
	 * 更新林地使用权（同时更新了附属权利）（URL:"/{xmbh}/qlAndqlfs/{qlid}",Method：POST）
	 * 
	 * @Title: UpdateQLAndQlfsInfo
	 * @author:liushufeng
	 * @date：2015年7月18日 下午10:10:08
	 * @param xmbh
	 * @param qlid
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/{xmbh}/qlAndqlfs/{qlid}", method = RequestMethod.POST)
	public @ResponseBody ResultMessage UpdateQLAndQlfsInfo(@PathVariable("xmbh") String xmbh,
			@PathVariable("qlid") String qlid, HttpServletRequest request, HttpServletResponse response) {
		ResultMessage resultMessage = new ResultMessage();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date qssj = new Date();
		try {
			String strjssj = request.getParameter("qLQSSJ");
			if (!StringHelper.isEmpty(strjssj)) {
				qssj = sdf.parse(request.getParameter("qLQSSJ"));
			} else {
				qssj = null;
			}
		} catch (ParseException e) {
			YwLogUtil.addYwLog("更新林地使用权-更新失败", ConstValue.SF.NO.Value, ConstValue.LOG.UPDATE);
			e.printStackTrace();
		}
		Date jssj = new Date();
		try {
			String strjssj = request.getParameter("qLJSSJ");
			if (!StringHelper.isEmpty(strjssj)) {
				jssj = sdf.parse(request.getParameter("qLJSSJ"));
			} else {
				jssj = null;
			}
		} catch (ParseException e) {
			YwLogUtil.addYwLog("更新林地使用权-更新失败", ConstValue.SF.NO.Value, ConstValue.LOG.UPDATE);
			e.printStackTrace();
		}
		String djyy = request.getParameter("dJYY").toString();
		String fj = request.getParameter("fJ");
		String czfs = request.getParameter("cZFS");
		String ldsyqr = request.getParameter("sLLMSYQR1");// 森林、林木所有权人
		String ldshyqr = request.getParameter("sLLMSYQR2");// 森林、林木使用权人
		String ldsyqxz = request.getParameter("lDSYQXZ");
		String zsbs = request.getParameter("zSBS");
		String fbf = request.getParameter("fBF");// 发包方
		String gyrqk = request.getParameter("gYRQK");// 共有人情况
		String jGDW = request.getParameter("jGDW");//价格单位	
						
		BDCS_QL_GZ ql = qlService.GetQL(qlid);
		if (ql != null) {
			BDCS_FSQL_GZ fsql = qlService.GetFSQL(ql.getFSQLID());				
			if (fsql != null) {
				fsql.setLDSYQXZ(ldsyqxz);
				fsql.setSLLMSYQR1(ldsyqr);
				fsql.setSLLMSYQR2(ldshyqr);
				fsql.setFBF(fbf);// 发包方
				fsql.setGYRQK(gyrqk);// 共有人情况				
				qlService.UpdateFSQL(fsql);
			}
			ql.setQLQSSJ(qssj);
			ql.setQLJSSJ(jssj);
			ql.setDJYY(djyy);
			ql.setFJ(fj);
			ql.setCZFS(czfs);
			ql.setZSBH(zsbs);
			qlService.UpdateQLandRebuildRelation(ql);
		}
		resultMessage.setSuccess("true");
		resultMessage.setMsg("更新成功!");
		YwLogUtil.addYwLog("更新林地使用权-更新成功", ConstValue.SF.YES.Value, ConstValue.LOG.UPDATE);
		return resultMessage;
	}

	/**
	 * 获取林地权利及附属权利信息（URL:"/getQlAndFsqlInfo/{qlid}",Method：GET）
	 * 
	 * @作者 海豹
	 * @创建时间 2015年6月28日上午12:30:48
	 * @param qlid
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/getQlAndFsqlInfo/{qlid}", method = RequestMethod.GET)
	public @ResponseBody Map getQlAndFsqlInfo(@PathVariable("qlid") String qlid, HttpServletRequest request,
			HttpServletResponse response) {
		Map map = new HashMap();
		map = qlService.GetQlandFsqlInfo(qlid);
		return map;
	}

	/**
	 * 获取现状林地使用权权利信息
	 * 
	 * @Title: getXZForestRightsInfo
	 * @author:liushufeng
	 * @date：2015年10月10日 下午2:51:30
	 * @param qlid
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/xzforestrightsinfo/{qlid}", method = RequestMethod.GET)
	public @ResponseBody Map getXZForestRightsInfo(@PathVariable("qlid") String qlid, HttpServletRequest request,
			HttpServletResponse response) {
		Map map = new HashMap();
		map = qlService.getXZForestRightsInfo(qlid);
		return map;
	}

	/**
	 * 组合业务：初始登记+在建工程抵押转现房登记
	 * 
	 * @作者 海豹
	 * @创建时间 2015年7月24日下午6:29:48
	 * @param bdcdyid
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "{xmbh}/getCombDyqInfo/{djdyid}", method = RequestMethod.GET)
	public @ResponseBody Map getCombDyqInfo(@PathVariable("xmbh") String xmbh, @PathVariable("djdyid") String djdyid,
			HttpServletRequest request, HttpServletResponse response) {
		Map map = new HashMap();
		map = qlService.GetCombDyqInfo(djdyid, xmbh);
		return map;
	}

	/**
	 * 保存权利中的注销信息（登记原因和附记）（URL:"/{xmbh}/qls/zxxx/{qlid}",Method：POST）
	 * 
	 * @作者 刘树峰
	 * @创建时间 2015年6月24日下午2:12:16
	 * @return
	 */
	@RequestMapping(value = "/{xmbh}/qls/zxxx/{qlid}", method = RequestMethod.POST)
	public @ResponseBody ResultMessage UpdateDestroyInfo(@PathVariable("xmbh") String xmbh,
			@PathVariable("qlid") String qlid, HttpServletRequest request, HttpServletResponse response) {
		// TODO @刘树峰 该方法跟前面的方法重复了
		ResultMessage msg = new ResultMessage();
//		String djyy = request.getParameter("DJYY");
//		String fj = request.getParameter("FJ");
		String dataString = request.getParameter("info");
		JSONObject object = JSON.parseObject(dataString);
		String djyy = object.getString("djyy").replaceAll("\u00A0", " ");
		String fj = object.getString("fj").replaceAll("\u00A0", " ");
		String gyrqk = request.getParameter("gYRQK");
		if(!StringHelper.isEmpty(gyrqk)){
			gyrqk = gyrqk.replaceAll("\u00A0", " ");
		}
		
		BDCS_QL_GZ ql = qlService.GetQL(qlid);

		if (ql != null) {
			ql.setDJYY(djyy);
			ql.setFJ(fj);
			ql.setGYRQK(gyrqk == null ? "" :gyrqk);

			qlService.UpdateQL(ql);
			if (!StringHelper.isEmpty(ql.getFSQLID())) {
				BDCS_FSQL_GZ fsql = qlService.GetFSQL(ql.getFSQLID());
				if (fsql != null) {
					fsql.setZXDYYY(djyy);
					qlService.UpdateFSQL(fsql);
				}
			}
			msg.setSuccess("true");
			msg.setMsg("保存成功");
		}
		return msg;
	}
	
	/**
	 * 保存权利中的注销信息（登记原因和附记）（URL:"/{xmbh}/qls/zxxx2/{qlid}",Method：POST）
	 * 
	 * @作者 刘树峰
	 * @创建时间 2015年6月24日下午2:12:16
	 * @return
	 */
	@RequestMapping(value = "/{xmbh}/qls/zxxx2/{qlid}", method = RequestMethod.POST)
	public @ResponseBody ResultMessage UpdateDestroyInfo2(@PathVariable("xmbh") String xmbh,
			@PathVariable("qlid") String qlid, HttpServletRequest request, HttpServletResponse response) {
		// TODO @刘树峰 该方法跟前面的方法重复了
		ResultMessage msg = new ResultMessage();
		String djyy = request.getParameter("djyy");
		String fj = request.getParameter("fj");
		BDCS_QL_GZ ql = qlService.GetQL(qlid);
		if (ql != null) {
			if (!StringHelper.isEmpty(ql.getFSQLID())) {
				BDCS_FSQL_GZ fsql = qlService.GetFSQL(ql.getFSQLID());
				if (fsql != null) {
					fsql.setZXDYYY(djyy);
					fsql.setZXFJ(fj);
					qlService.UpdateFSQL(fsql);
				}
			}
			msg.setSuccess("true");
			msg.setMsg("保存成功");
		}
		return msg;
	}

	/**
	 * 注销信息显示：
	 * 通过权利id从BDCS_FSQL_GZZ中获取数据（URL:"/{xmbh}/qls/zxxx2/{qlid}",Method：GET）
	 * 
	 * @作者 俞学斌
	 * @创建时间 2015年12月05日 18:32:16
	 * @param xmbh
	 * @param qlid
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/{xmbh}/qls/zxxx2/{qlid}", method = RequestMethod.GET)
	public @ResponseBody Map getZxxx(@PathVariable("xmbh") String xmbh, @PathVariable("qlid") String qlid,
			HttpServletRequest request, HttpServletResponse response) {
		Map map = new HashMap();
		map = qlService.getLoadZxxxInfo(qlid, xmbh);
		return map;
	}

	/**************************** 权利人 Start ***********************************/

	/**
	 * 根据权利ID获取权利人列表（URL:"/{xmbh}/qls/{qlid}/qlrs",Method：GET）
	 * 
	 * @param xmbh
	 * @param djdyid
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/{xmbh}/qls/{qlid}/qlrs", method = RequestMethod.GET)
	public @ResponseBody Message GetQLRSBYQLID(@PathVariable("xmbh") String xmbh, @PathVariable("qlid") String qlid,
			HttpServletRequest request, HttpServletResponse response) {
		Integer page = 1;
		if (request.getParameter("page") != null) {
			page = Integer.parseInt(request.getParameter("page"));
		}
		Integer rows = 100;
		if (request.getParameter("rows") != null) {
			rows = Integer.parseInt(request.getParameter("rows"));
		}
		Page qlrPaged = qlService.GetPagedQLR(xmbh, qlid, page, rows);
		Message m = new Message();
		m.setTotal(qlrPaged.getTotalCount());
		m.setRows(qlrPaged.getResult());
		return m;
	}
	
	/**
	 * @Title: GetQLRSBYDJDYID
	 * @Description: 通过登记单元获取抵押权信息（仅适用于返回多个抵押权的情形）
	 * @Author：赵梦帆
	 * @Data：2016年11月12日 上午10:44:13
	 * @param xmbh
	 * @param djdyid
	 * @param request
	 * @param response
	 * @return
	 * @return Message
	 */
	@RequestMapping(value = "/{xmbh}/dyqls/{djdyid}", method = RequestMethod.GET)
	public @ResponseBody Message GetQLRSBYDJDYID(@PathVariable("xmbh") String xmbh, @PathVariable("djdyid") String djdyid, 
			HttpServletRequest request, HttpServletResponse response) {
		
		String fulSql = " SELECT QLR.* FROM BDCK.BDCS_QL_GZ QL INNER JOIN BDCK.BDCS_QLR_GZ QLR ON QLR.QLID=QL.QLID "
				+ " WHERE QL.XMBH=''{0}'' AND QL.DJDYID=''{1}'' AND QL.QLLX='23' ORDER BY SXH";
		fulSql = MessageFormat.format(fulSql, xmbh,djdyid);
		//List<Map> ddd = 
		List<Map> qlrs = dao.getDataListByFullSql(fulSql);
		for (Map map : qlrs) {
			map.put("QLRLXNAME", ConstHelper.getNameByValue("QLRLX", map.get("QLRLX")+""));
			map.put("ZJZLNAME", ConstHelper.getNameByValue("ZJLX", map.get("ZJZL")+""));
			map.put("GYFSNAME", ConstHelper.getNameByValue("GYFS", map.get("GYFS")+""));
		}
		Message m = new Message();
		if(qlrs!=null){
			m.setTotal(qlrs.size());
			m.setRows(qlrs);
		}
		return m;
	}

	/**
	 * 获取某个权利人（工作层）的详细信息（URL:"/{xmbh}/qlrs/{qlrid}",Method：GET）
	 * 
	 * @param xmbh
	 * @param qlrid
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/{xmbh}/qlrs/{qlrid}", method = RequestMethod.GET)
	public @ResponseBody BDCS_QLR_GZ GetQLRInfo(@PathVariable("xmbh") String xmbh, @PathVariable("qlrid") String qlrid,
			HttpServletRequest request, HttpServletResponse response) {
		BDCS_QLR_GZ bdcs_QLR_GZ = new BDCS_QLR_GZ();
		bdcs_QLR_GZ = qlService.GetQLRInfo(qlrid);
		return bdcs_QLR_GZ;
	}
	/**
	 * 获取某个权利人（工作层）的详细信息+QL表的djsj字段（URL:"/{xmbh}/qlrs/{qlrid}",Method：GET）
	 * 
	 * @param xmbh
	 * @param qlrid
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/{xmbh}/qlrs_ex/{qlrid}", method = RequestMethod.GET)
	public @ResponseBody Map<String,Object> GetQLRInfo_EX(@PathVariable("xmbh") String xmbh, @PathVariable("qlrid") String qlrid,
			HttpServletRequest request, HttpServletResponse response) {
		Map<String,Object> map = new HashMap<String,Object>();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");  
		BDCS_QLR_GZ qlr_gz= dao.get(BDCS_QLR_GZ.class, qlrid);
		BDCS_QL_GZ ql_gz = dao.get(BDCS_QL_GZ.class, qlr_gz.getQLID());
		map.put("id", qlrid);map.put("zjzl", qlr_gz.getZJZL());
		map.put("qlid", qlr_gz.getQLID());map.put("xb", qlr_gz.getXB());
		map.put("xmbh", qlr_gz.getXMBH());map.put("zjh", qlr_gz.getZJH());
		map.put("qlrmc", qlr_gz.getQLRMC());map.put("dh", qlr_gz.getDH());
		map.put("bdcqzh", qlr_gz.getBDCQZH());map.put("dz", qlr_gz.getDZ());
		map.put("yb", qlr_gz.getYB());map.put("gyfs", qlr_gz.getGYFS());
		map.put("qlrlx", qlr_gz.getQLRLX());map.put("isczr", qlr_gz.getISCZR());
		if(ql_gz.getDJSJ()==null){
			map.put("djsj","");
		}else{
			map.put("djsj",sdf.format(ql_gz.getDJSJ()) );
		}
		return map;
	}
	/**
	 * 获取某个权利人（现状层）的详细信息（URL:"/qlrxz/{qlrid}",Method：GET）
	 * 
	 * @author diaoliwei
	 * @date 2015-8-3
	 * @param qlrid
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/qlrxz/{qlrid}", method = RequestMethod.GET)
	public @ResponseBody BDCS_QLR_XZ GetXZQLRInfo(@PathVariable("qlrid") String qlrid, HttpServletRequest request,
			HttpServletResponse response) {
		BDCS_QLR_XZ bdcs_QLR_xZ = new BDCS_QLR_XZ();
		bdcs_QLR_xZ = qlService.GetXZQLRInfo(qlrid);
		return bdcs_QLR_xZ;
	}

	/**
	 * 更新某个权利人的信息（URL:"/{xmbh}/qlrs/{qlrid}",Method：POST）
	 * 
	 * @param xmbh
	 * @param qlrid
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/{xmbh}/qlrs/{qlrid}", method = RequestMethod.POST)
	public @ResponseBody ResultMessage UpdateQLRInfo(@PathVariable("xmbh") String xmbh,
			@PathVariable("qlrid") String qlrid, HttpServletRequest request, HttpServletResponse response) {
		ResultMessage m = new ResultMessage();
		m.setSuccess("false");
		m.setMsg("更新权利人失败！请联系管理员！");
		RightsHolder holder = RightsHolderTools.loadRightsHolder(DJDYLY.GZ, qlrid);
		if (holder != null) {
			BDCS_QLR_GZ holdergz = (BDCS_QLR_GZ) holder;
			holder.setQLRMC(request.getParameter("qlrmc"));// 权利人名称
			if (!StringHelper.isEmpty(request.getParameter("sxh")))// 顺序号
			{
				holder.setSXH(Integer.parseInt(request.getParameter("sxh").toString()));
			}
			holder.setZJZL(request.getParameter("zjzl"));
			holder.setXB(request.getParameter("xb"));
			holder.setZJH(request.getParameter("zjh"));
			holder.setFZJG(request.getParameter("fzjg"));
			holder.setDH(request.getParameter("dh"));
			holder.setDZ(request.getParameter("dz"));
			holder.setYB(request.getParameter("yb"));
			holder.setDZYJ(request.getParameter("dzyj"));
			holder.setGJ(request.getParameter("gj"));
			holder.setHJSZSS(request.getParameter("hjszss"));
			holder.setGZDW(request.getParameter("gzdw"));
			holder.setSSHY(request.getParameter("sshy"));
			if (!StringHelper.isEmpty(request.getParameter("qlmj"))) {
				holder.setQLMJ(Double.parseDouble(request.getParameter("qlmj").toString()));
			}
			holder.setQLRLX(request.getParameter("qlrlx"));
			holder.setGYFS(request.getParameter("gyfs"));
			holder.setQLBL(request.getParameter("qlbl"));
			holder.setBZ(request.getParameter("bz"));
			holdergz.setISCZR(request.getParameter("isczr"));
			holdergz.setNATION(request.getParameter("nation"));
			qlService.UpdateQLR(holdergz);
			m.setSuccess("true");
			m.setMsg("更新权利人成功!");
			YwLogUtil.addYwLog("更新权利人信息-更新成功", ConstValue.SF.YES.Value, ConstValue.LOG.UPDATE);
		} else {
			YwLogUtil.addYwLog("更新权利人信息-更新失败", ConstValue.SF.NO.Value, ConstValue.LOG.UPDATE);
		}
		return m;
	}

	@RequestMapping(value = "/{xmbh}/qlrs/batchupdate/{updateoption}/{qlrid}", method = RequestMethod.POST)
	public @ResponseBody ResultMessage batchUpdateQLRInfo(@PathVariable("xmbh") String xmbh,
			@PathVariable("updateoption") String option, @PathVariable("qlrid") String qlrid,
			HttpServletRequest request, HttpServletResponse response) {
		System.out.println("************************批量更新*********************");
		ResultMessage m = new ResultMessage();
		m.setSuccess("false");
		m.setMsg("更新权利人失败！请联系管理员！");
		RightsHolder holder = RightsHolderTools.loadRightsHolder(DJDYLY.GZ, qlrid);
		if (holder != null) {
			BDCS_QLR_GZ holdergz = new BDCS_QLR_GZ();
			holdergz.setQLRMC(request.getParameter("qlrmc"));// 权利人名称
			if (!StringHelper.isEmpty(request.getParameter("sxh")))// 顺序号
			{
				holdergz.setSXH(Integer.parseInt(request.getParameter("sxh").toString()));
			}
			holdergz.setZJZL(request.getParameter("zjzl"));
			holdergz.setXB(request.getParameter("xb"));
			holdergz.setZJH(request.getParameter("zjh"));
			holdergz.setFZJG(request.getParameter("fzjg"));
			holdergz.setDH(request.getParameter("dh"));
			holdergz.setDZ(request.getParameter("dz"));
			holdergz.setYB(request.getParameter("yb"));
			holdergz.setDZYJ(request.getParameter("dzyj"));
			holdergz.setGJ(request.getParameter("gj"));
			holdergz.setHJSZSS(request.getParameter("hjszss"));
			holdergz.setGZDW(request.getParameter("gzdw"));
			holdergz.setSSHY(request.getParameter("sshy"));
			if (!StringHelper.isEmpty(request.getParameter("qlmj"))) {
				holdergz.setQLMJ(Double.parseDouble(request.getParameter("qlmj").toString()));
			}
			holdergz.setQLRLX(request.getParameter("qlrlx"));
			holdergz.setGYFS(request.getParameter("gyfs"));
			holdergz.setQLBL(request.getParameter("qlbl"));
			holdergz.setBZ(request.getParameter("bz"));
			holdergz.setISCZR(request.getParameter("isczr"));
			holdergz.setNATION(request.getParameter("nation"));
			holdergz.setXMBH(xmbh);
			holdergz.setId(qlrid);
			m = qlService.batchUpdateQLR(holdergz, option);
			YwLogUtil.addYwLog("更新权利人信息-更新成功", ConstValue.SF.YES.Value, ConstValue.LOG.UPDATE);
		} else {
			YwLogUtil.addYwLog("更新权利人信息-更新失败", ConstValue.SF.NO.Value, ConstValue.LOG.UPDATE);
		}
		return m;
	}

	/**
	 * 根据申请人ID添加权利人（URL:"/{xmbh}/{qlid}/qlrs",Method：POST）
	 * 
	 * @作者 刘树峰
	 * @创建时间 2015年6月13日上午3:09:16
	 * @param xmbh
	 * @param qlid
	 * @param result
	 * @param status
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/{xmbh}/{qlid}/qlrs", method = RequestMethod.POST)
	@AccessRequired.UnBoardRequired
	public @ResponseBody ResultMessage addQLRfromSQR(@PathVariable("xmbh") String xmbh,
			@PathVariable("qlid") String qlid, HttpServletRequest request, HttpServletResponse response) {
		ResultMessage m = new ResultMessage();
		String dataString = request.getParameter("data");
		JSONArray sqridArray = JSONArray.parseArray(dataString);
		Object[] sqrids = (Object[]) sqridArray.toArray();
		//TODO sun-添加权利人
		m = qlService.addQLRfromSQR(xmbh, qlid, sqrids);
		YwLogUtil.addYwLog("添加权利人信息", ConstValue.SF.YES.Value, ConstValue.LOG.ADD);
		return m;
	}

	/**
	 * 批量更新权利及权利人（URL:"/{xmbh}/addQlAndQlrs/",Method：POST）
	 * 
	 * @作者 海豹
	 * @创建时间 2015年6月30日下午11:35:56
	 * @param xmbh
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/{xmbh}/addQlAndQlrs/", method = RequestMethod.POST)
	public @ResponseBody ResultMessage addQLAndQLRfromSQR(@PathVariable("xmbh") String xmbh, HttpServletRequest request,
			HttpServletResponse response) {

		String SqrString = request.getParameter("sqrdata");
		String QlString = request.getParameter("qldata");
		JSONArray sqridArray = JSONArray.parseArray(SqrString);
		JSONArray qlidArray = JSONArray.parseArray(QlString);
		Object[] objQlids = (Object[]) qlidArray.toArray();// 权利数组
		Object[] sqrids = (Object[]) sqridArray.toArray();// 申请人数组
		Map map = transToMAP(request.getParameterMap());
		System.out.println(map);
		String tdyt = "";
		try {
			if (!StringHelper.isEmpty(map.get("tdyt_pl"))) {
				tdyt = map.get("tdyt_pl").toString();
			}
		} catch (Exception e) {
			YwLogUtil.addYwLog("批量更新权利及权利人-更新失败", ConstValue.SF.NO.Value, ConstValue.LOG.UPDATE);
		}

		// 2016年7月6日11:46:15
		/*
		 * bdck.bdcs_qlr_gz bdck.bdcs_qdzr_gz bdck.bdcs_zs_gz
		 **/
		System.out.println(map.get("sqrdata"));
		if (sqrids!=null&&sqrids.length>0) {
			for (Object qlid : objQlids) {
				if (!StringHelper.isEmpty(qlid)) {
					dao.deleteEntitysByHql(BDCS_QLR_GZ.class, " XMBH='" + xmbh + "'" + " AND QLID='" + qlid + "'");
					List<BDCS_QDZR_GZ> Oldqdzr_list = dao.getDataList(BDCS_QDZR_GZ.class,
							"QLID='" + qlid + "' AND XMBH='" + xmbh + "'");
					if (Oldqdzr_list != null && Oldqdzr_list.size() > 0) {
						for (BDCS_QDZR_GZ qdzr : Oldqdzr_list) {
							BDCS_ZS_GZ zs = dao.get(BDCS_ZS_GZ.class, qdzr.getZSID());
							if (zs != null) {
								dao.deleteEntity(zs);
							}
							dao.deleteEntity(qdzr);
						}
					}
				}
			}
		}

		ResultMessage m = new ResultMessage();
        JSONObject jsonobj=NewLogTools.getJSONByXMBH(xmbh);
        jsonobj.put("OperateType", "批量更新权利人");
        int temp=0;
        JSONObject  jsonqls=new JSONObject();
		for (Object qlid : objQlids) {
			if (!StringHelper.isEmpty(qlid)) {
				temp++;
				JSONObject jsonql=new JSONObject();
				jsonql.put("QLID", qlid);
				BDCS_QL_GZ ql_gz = qlService.GetQL(qlid.toString());
				BDCS_FSQL_GZ fsql_gz = qlService.GetFSQL(ql_gz.getFSQLID().toString());
				
				if (map.containsKey("JGDW_PL")) {
					if (map.get("JGDW_PL") != null)
						fsql_gz.setZQDW(StringHelper.formatObject(map.get("JGDW_PL")));
				}

				if (map.containsKey("QLQSSJ_PL")) {
					if (map.get("QLQSSJ_PL") != null)
						try {
							ql_gz.setQLQSSJ(StringHelper.FormatByDate(map.get("QLQSSJ_PL")));
						} catch (ParseException e) {
						}
				}
				if (map.containsKey("QLJSSJ_PL")) {
					if (map.get("QLJSSJ_PL") != null)
						try {
							ql_gz.setQLJSSJ(StringHelper.FormatByDate(map.get("QLJSSJ_PL")));
						} catch (ParseException e) {
						}
				}

				if (map.containsKey("QDJG_PL")) {
					if (map.get("QDJG_PL") != null) {
						double qdjg = StringHelper.getDouble(map.get("QDJG_PL"));
						ql_gz.setQDJG(qdjg);
					}
				}
				if (map.containsKey("TDSHYQR_PL")) {
					if (map.get("TDSHYQR_PL") != null)
						ql_gz.setTDSHYQR(map.get("TDSHYQR_PL").toString());
				}

				if (map.containsKey("DJYY_PL")) {
					if (map.get("DJYY_PL") != null)
						ql_gz.setDJYY(map.get("DJYY_PL").toString());
				}

				if (map.containsKey("FJ_PL")) {
					if (map.get("FJ_PL") != null)
						ql_gz.setFJ(map.get("FJ_PL").toString());
				}
				if (map.containsKey("FJ_BZ")) {
					if (map.get("FJ_BZ") != null)
						ql_gz.setBZ(map.get("FJ_BZ").toString());
				}
				if (map.containsKey("plZSBS")) {
					if (map.get("plZSBS") != null)
						ql_gz.setZSBH(map.get("plZSBS").toString());
				}
				if (map.containsKey("CZFS_PL")) {
					if (map.get("CZFS_PL") != null) {
						if (!(ql_gz.getCZFS() != null && ql_gz.getCZFS().equals(map.get("CZFS_PL").toString()))) {
							ql_gz.setCZFS(map.get("CZFS_PL").toString());
							List<RightsHolder> list = RightsHolderTools.loadRightsHolders(DJDYLY.GZ, ql_gz.getId());
							if (list != null && list.size() > 0) {
								for (RightsHolder qlr : list) {
									dao.deleteEntity(qlr);
								}
							}

							List<BDCS_QDZR_GZ> qdzr_list = dao.getDataList(BDCS_QDZR_GZ.class,
									"QLID='" + ql_gz.getId() + "' AND XMBH='" + xmbh + "'");
							if (qdzr_list != null && qdzr_list.size() > 0) {
								for (BDCS_QDZR_GZ qdzr : qdzr_list) {
									BDCS_ZS_GZ zs = dao.get(BDCS_ZS_GZ.class, qdzr.getZSID());
									if (zs != null) {
										dao.deleteEntity(zs);
									}
									dao.deleteEntity(qdzr);
								}
							}
						}
					}
				}
				jsonql.put("Ql-Sqrids", sqrids);
				jsonqls.put("序号：（"+temp+")", jsonql);
				qlService.plAddQlRfromSQR(xmbh, ql_gz, sqrids);
				if (!StringHelper.isEmpty(tdyt)) {
					qlService.Updatetdyt(ql_gz.getDJDYID(), tdyt, xmbh);
				}
			}
		}
		m.setMsg("更新成功");
		m.setSuccess("true");
		YwLogUtil.addYwLog("批量更新权利及权利人-更新成功", ConstValue.SF.YES.Value, ConstValue.LOG.UPDATE);
		jsonobj.put("QL-Sqrinfos", jsonqls);
		NewLogTools.saveLog(jsonobj.toString(), xmbh, "3", "添加权利人");
		return m;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Map transToMAP(Map parameterMap) {
		// 返回值Map
		Map returnMap = new HashMap();
		Iterator entries = parameterMap.entrySet().iterator();
		Map.Entry entry;
		String name = "";
		String value = "";
		while (entries.hasNext()) {
			entry = (Map.Entry) entries.next();
			name = (String) entry.getKey();
			Object valueObj = entry.getValue();
			if (null == valueObj) {
				value = "";
			} else if (valueObj instanceof String[]) {
				String[] values = (String[]) valueObj;
				for (int i = 0; i < values.length; i++) {
					value = values[i] + ",";
				}
				value = value.substring(0, value.length() - 1);
			} else {
				value = valueObj.toString();
			}
			returnMap.put(name, value);
		}
		return returnMap;
	}

	/**
	 * 移除权利人（URL:"/{xmbh}/{qlid}/qlrs/{qlrid}",Method：DELETE）
	 * 
	 * @作者 刘树峰
	 * @创建时间 2015年6月13日上午4:18:43
	 * @param xmbh
	 * @param qlid
	 * @param qlrid
	 * @param result
	 * @param status
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/{xmbh}/{qlid}/qlrs/{qlrid}", method = RequestMethod.DELETE)
	@AccessRequired.UnBoardRequired
	public @ResponseBody ResultMessage removeQLR(@PathVariable("xmbh") String xmbh, @PathVariable("qlid") String qlid,
			@PathVariable("qlrid") String qlrid, HttpServletRequest request, HttpServletResponse response) {
		ResultMessage m = new ResultMessage();
		m = qlService.removeQLR(xmbh, qlid, qlrid);

		return m;
	}

	/**
	 * 根据现状权利ID获取现状权利人（URL:"/{qlid}/qlrs",Method：GET）
	 * 
	 * @作者 刘树峰
	 * @创建时间 2015年6月24日下午2:10:51
	 * @param qlid
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/{qlid}/qlrs", method = RequestMethod.GET)
	public @ResponseBody Message GetXZQLRSBYQLID(@PathVariable("qlid") String qlid, HttpServletRequest request,
			HttpServletResponse response) {
		List<RightsHolder> list = RightsHolderTools.loadRightsHolders(DJDYLY.LS, qlid);
		Page articlePaged = new Page();
		articlePaged.setData(list);
		articlePaged.setTotalCount(list.size());
		Message m = new Message();
		m.setTotal(articlePaged.getTotalCount());
		m.setRows(articlePaged.getResult());
		return m;
	}

	/**
	 * 抵押权注销-根据抵押权id查询出所有权的权利人信息
	 * 
	 * @author diaoliwei
	 * @date 2015-8-3
	 * @param dyqQlid
	 *            抵押权权利id
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/{dyqQlid}/syqQlrs", method = RequestMethod.GET)
	public @ResponseBody Message GetXZSYQQLRSBYQLID(@PathVariable("dyqQlid") String dyqQlid, HttpServletRequest request,
			HttpServletResponse response) {
		String qllx = ConstValue.QLLX.GYJSYDSHYQ_FWSYQ.Value; // 所有权
		List<BDCS_QLR_XZ> list = qlService.getSYQXZQLRList(dyqQlid, qllx);
		Page articlePaged = new Page();
		articlePaged.setData(list);
		articlePaged.setTotalCount(list.size());
		Message m = new Message();
		m.setTotal(articlePaged.getTotalCount());
		m.setRows(articlePaged.getResult());
		return m;
	}

	/**************************** 权利人 End ***********************************/

	/**
	 * 根据权利ID获取现状库中的权利信息（URL:"{qlid}/ql_xz_zy",Method：GET）
	 * 
	 * @作者 孙海豹
	 * @创建时间 2015年6月12日下午1:54:05
	 * @param qlid
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "{qlid}/ql_xz_zy", method = RequestMethod.GET)
	public @ResponseBody BDCS_QL_LS getQl_xz(@PathVariable String qlid, HttpServletRequest request,
			HttpServletResponse response) {
		BDCS_QL_LS ql = null;
		Rights rights = RightsTools.loadRights(DJDYLY.LS, qlid);
		if (rights != null) {
			ql = (BDCS_QL_LS) rights;
		} else {
			ql = new BDCS_QL_LS();
		}
		return ql;
	}

	/**
	 * 不知道是干啥的函数（URL:"/{xmbh}/dyq/{qlid}",Method：POST） TODO @孙海豹 需要处理3件事：
	 * 1、通过项目编号跟申请人ID删除BDCS_QLR_GZ中的中的信息并且从新保存 2、通过项目编号更新BDCS_QL_GZ中的时间及附件
	 * 3、通过项目编号更新BDCS_FSQL_GZ中的其余信息
	 * 
	 * @作者 海豹
	 * @创建时间 2015年6月12日下午5:00:53
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/{xmbh}/dyq/{qlid}", method = RequestMethod.POST)
	public @ResponseBody ResultMessage updateDyqxx(@PathVariable("xmbh") String xmbh, @PathVariable("qlid") String qlid,
			HttpServletRequest request, HttpServletResponse response) {
		ResultMessage m = new ResultMessage();
		String dataString = request.getParameter("info");
		JSONObject object = JSON.parseObject(dataString);
		qlService.SaveQlInfo(xmbh, qlid, object);
		m.setMsg("更新成功");
		m.setSuccess("true");
		YwLogUtil.addYwLog("更新抵押权信息-更新成功", ConstValue.SF.YES.Value, ConstValue.LOG.UPDATE);
		return m;
	}

	@RequestMapping(value = "/pldyq/{xmbh}/{qlid}", method = RequestMethod.POST)
	@AccessRequired.UnBoardRequired
	public @ResponseBody ResultMessage plUpdateDyqxx(@PathVariable("xmbh") String xmbh,
			@PathVariable("qlid") String qlid, HttpServletRequest request, HttpServletResponse response) {
		ResultMessage m = new ResultMessage();
		String dataString = request.getParameter("info");
		JSONObject object = JSON.parseObject(dataString);
		object.put("qlid", qlid);
		qlService.plSaveQlInfo(xmbh, object);
		m.setMsg("更新成功");
		m.setSuccess("true");
		YwLogUtil.addYwLog("批量更新抵押权信息-更新成功", ConstValue.SF.YES.Value, ConstValue.LOG.UPDATE);
		return m;
	}

	//批量保存林权的权利信息	
	@RequestMapping(value = "/pllq/{xmbh}/{qlid}", method = RequestMethod.POST)
	@AccessRequired.UnBoardRequired
	public @ResponseBody ResultMessage plUpdatelqxx(@PathVariable("xmbh") String xmbh,
			@PathVariable("qlid") String qlid, HttpServletRequest request, HttpServletResponse response) {
		ResultMessage m = new ResultMessage();
		String dataString = request.getParameter("infos");
		JSONObject object = JSON.parseObject(dataString);
		object.put("qlid", qlid);
		qlService.plSaveQlInfo_lq(xmbh, object);
		m.setMsg("更新成功");
		m.setSuccess("true");
		YwLogUtil.addYwLog("批量更新林权信息-更新成功", ConstValue.SF.YES.Value, ConstValue.LOG.UPDATE);
		return m;
	}
	
	/**
	 * 首次登记-一般抵押权-在建工程抵押权登记转现房抵押登记时，抵押权人不能被修改,只保存权利及附属权利信息（URL:
	 * "/{xmbh}/UpdatedyqQlAndFsql/{qlid}",Method：POST）
	 * 
	 * @作者 海豹
	 * @创建时间 2015年7月16日上午12:05:59
	 * @param xmbh
	 * @param qlid
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/{xmbh}/UpdatedyqQlAndFsql/{qlid}", method = RequestMethod.POST)
	@AccessRequired.UnBoardRequired
	public @ResponseBody ResultMessage UpdatedyqQlAndFsql(@PathVariable("xmbh") String xmbh,
			@PathVariable("qlid") String qlid, HttpServletRequest request, HttpServletResponse response) {
		ResultMessage m = new ResultMessage();
		String dataString = request.getParameter("info");
		JSONObject object = JSON.parseObject(dataString);
		qlService.SaveQlandFsqlInfo(xmbh, qlid, object);
		m.setMsg("更新成功");
		m.setSuccess("true");
		YwLogUtil.addYwLog("更新抵押权和附属权利信息-更新成功", ConstValue.SF.YES.Value, ConstValue.LOG.UPDATE);
		return m;
	}

	@RequestMapping(value = "/plUpdatedyqQlAndFsql/{xmbh}", method = RequestMethod.POST)
	public @ResponseBody ResultMessage plUpdatedyqQlAndFsql(@PathVariable("xmbh") String xmbh,
			HttpServletRequest request, HttpServletResponse response) {
		ResultMessage m = new ResultMessage();
		String dataString = request.getParameter("info");
		JSONObject object = JSON.parseObject(dataString);
		qlService.plSaveQlandFsqlInfo(xmbh, object);
		m.setMsg("更新成功");
		m.setSuccess("true");
		YwLogUtil.addYwLog("批量更新抵押权和附属权利信息-更新成功", ConstValue.SF.YES.Value, ConstValue.LOG.UPDATE);
		return m;
	}

	/**
	 * 不知道是干啥的函数（URL:"{xmbh}/dyq/{qlid}",Method：POST） TODO @孙海豹 需要处理3件事：
	 * 1、通过项目编号从BDCS_QLR_GZ中获取SQRID,再通过通过项目编号及SQRID从BDCS_SQR中获取相应的申请人信息
	 * 2、通过项目编号获取BDCS_QL_GZ中的时间及附件 3、通过项目编号获取BDCS_FSQL_GZ中的其余信息
	 * 
	 * @作者 海豹
	 * @创建时间 2015年6月12日下午5:00:53
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "{xmbh}/dyq/{qlid}", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> getDyqxx(@PathVariable("xmbh") String xmbh,
			@PathVariable("qlid") String qlid, HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();
		map = qlService.getDYQInfo(xmbh, qlid);
		return map;
	}

	/**
	 * 获取现状抵押权信息
	 * 
	 * @Title: getpreDyqxx
	 * @author:liushufeng
	 * @date：2015年9月12日 上午12:35:24
	 * @param qlid
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/predyq/{qlid}", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> getpreDyqxx(@PathVariable("qlid") String qlid, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();
		map = qlService.getzydj_DYQInfo(qlid,"XZ");
		return map;
	}
	
	@RequestMapping(value = "/diyq/{qlid}", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> getDiyqxx(@PathVariable("qlid") String qlid, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();
		map = qlService.getls_DYQInfo(qlid);
		return map;
	}

	/**
	 * 获取转移登记的抵押权信息（URL:"{xmbh}/dyqxx/{qlid}",Method：GET）
	 * 
	 * @作者 diaoliwei
	 * @创建时间 2015年7月14日下午3:58:22
	 * @param xmbh
	 * @param qlid
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "{xmbh}/dyqxx/{qlid}", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> getDyqZyxx(@PathVariable("xmbh") String xmbh,
			@PathVariable("qlid") String qlid, HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();
		map = qlService.getDyqZyInfo(xmbh, qlid);
		return map;
	}

	/**
	 * 查封信息保存： 通过QLID保存信息到QLFS_GZ中（URL:"/updateCfxx/{qlid}",Method：POST）
	 * 
	 * @作者 海豹
	 * @创建时间 2015年6月17日下午11:29:25
	 * @param xmbh
	 * @param qlid
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/updateCfxx/{qlid}", method = RequestMethod.POST)
	public @ResponseBody ResultMessage updateCfxx(@PathVariable("qlid") String qlid, HttpServletRequest request,
			HttpServletResponse response) {
		ResultMessage m = new ResultMessage();
		String dataString = request.getParameter("info");
		JSONObject object = JSON.parseObject(dataString);
		qlService.updateCfxxInfo(qlid, object);
		m.setMsg("更新成功");
		m.setSuccess("true");
		YwLogUtil.addYwLog("查封信息-更新成功", ConstValue.SF.YES.Value, ConstValue.LOG.UPDATE);
		return m;
	}
	
	
	/**
	 * 修改权证号
	 * @param xmbh
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="{xmbh}/qlrxxs/{qlrid}", method = RequestMethod.POST)
	public @ResponseBody ResultMessage updateQzhInfo(@PathVariable("xmbh") String xmbh,@PathVariable("qlrid") String qlrid,
			HttpServletRequest request,HttpServletResponse response){
		ResultMessage m=new ResultMessage();
		String dataString =request.getParameter("bdcqzh");
		String djsj =request.getParameter("djsj");
		System.out.println(dataString);
		BDCS_XMXX xmxx = dao.get(BDCS_XMXX.class, xmbh);
		String isdb =  xmxx.getSFDB();
		if(isdb.equals("0")){
			qlService.updateDbhQzh(qlrid,dataString,djsj);
		}
//		qlService.updateQzh(qlrid,dataString,xmbh);
//		qlService.updataQLqzh(qlrid);
		m.setMsg("保存成功");
		m.setSuccess("true");
		return m;
	}
	/**
	 * 判断是否登簿
	 * @param xmbh
	 * @param request
	 * @param response
	 * @return "true" or  "false" //字符串
	 */
	@RequestMapping(value="/sfdb/{xmbh}", method = RequestMethod.GET)
	public @ResponseBody String sfdb(@PathVariable("xmbh") String xmbh,HttpServletRequest request,HttpServletResponse response){
		String sfdb = "";
		BDCS_XMXX xmxx = dao.get(BDCS_XMXX.class, xmbh);
		if(xmxx!=null){
			sfdb = xmxx.getSFDB();
		}
		return sfdb;
	}

	@RequestMapping(value = "/plUpdateCfxx/{xmbh}", method = RequestMethod.POST)
	public @ResponseBody ResultMessage plUpdateCfxx(@PathVariable("xmbh") String xmbh, HttpServletRequest request,
			HttpServletResponse response) {
		ResultMessage mgs = new ResultMessage();
		String dataString = request.getParameter("info");
		JSONObject object = JSON.parseObject(dataString);
		qlService.plUpdateCfxxInfo(xmbh, object);
		mgs.setMsg("批量更新成功");
		mgs.setSuccess("true");
		YwLogUtil.addYwLog("批量查封信息-更新成功", ConstValue.SF.YES.Value, ConstValue.LOG.UPDATE);
		return mgs;

	}

	/**
	 * 保存解封信息（URL:"/updateJfxx/{qlid}",Method：POST）
	 * 
	 * @Title: updateJfxx
	 * @author:wuzhu
	 * @date：2015年7月18日 下午10:17:18
	 * @param qlid
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/updateJfxx/{qlid}", method = RequestMethod.POST)
	public @ResponseBody ResultMessage updateJfxx(@PathVariable("qlid") String qlid, HttpServletRequest request,
			HttpServletResponse response) {
		ResultMessage m = new ResultMessage();
		String dataString = request.getParameter("info");
		JSONObject object = JSON.parseObject(dataString);
		qlService.updateJfxxInfo(qlid, object);
		m.setMsg("更新成功");
		m.setSuccess("true");
		YwLogUtil.addYwLog("解封信息-更新成功", ConstValue.SF.YES.Value, ConstValue.LOG.UPDATE);
		return m;
	}

	/**
	 * 通过项目编号批量更新解封服务
	 * 
	 * @作者 海豹
	 * @创建时间 2015年8月5日下午11:50:53
	 * @param xmbh
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/plUpdateJfxx/{xmbh}", method = RequestMethod.POST)
	public @ResponseBody ResultMessage plUpdateJfxx(@PathVariable("xmbh") String xmbh, HttpServletRequest request,
			HttpServletResponse response) {
		ResultMessage msg = new ResultMessage();
		String dataString = request.getParameter("info");
		JSONObject object = JSON.parseObject(dataString);
		qlService.plUpdateJfxxInfo(xmbh, object);
		msg.setMsg("批量更新成功");
		msg.setSuccess("true");
		YwLogUtil.addYwLog("批量更新解封信息-更新成功", ConstValue.SF.YES.Value, ConstValue.LOG.UPDATE);
		return msg;
	}

	/**
	 * 查封信息显示：通过权利id从BDCS_FSQL_GZ中获取数据（URL:"/getcfxx/{qlid}",Method：GET）
	 * 
	 * @作者 海豹
	 * @创建时间 2015年6月17日下午11:30:16
	 * @param xmbh
	 * @param qlid
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/getcfxx/{qlid}", method = RequestMethod.GET)
	public @ResponseBody Map getCfxx(@PathVariable("qlid") String qlid, HttpServletRequest request,
			HttpServletResponse response) {
		Map map = new HashMap();
		map = qlService.getCfxxInfo(qlid);
		return map;
	}
	
	/**
	 * @Description: 查封的更正 续封 解封 权利从来源权利获取 ls
	 * @Title: getCfxxEX
	 * @Author: zhaomengfan
	 * @Date: 2017年1月10日下午5:32:59
	 * @param qlid
	 * @param request
	 * @param response
	 * @return
	 * @return Map
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/getcfxxEX/{qlid}", method = RequestMethod.GET)
	public @ResponseBody Map getCfxxEX(@PathVariable("qlid") String qlid, HttpServletRequest request,
			HttpServletResponse response) {
		Map map = new HashMap();
		map = qlService.getCfxxInfoEX(qlid);
		return map;
	}

	/**
	 * 注销信息保存：通过QLID保存信息到QLFS_GZ中（URL:"/updateZxxx/{qlid}",Method：POST）
	 * 
	 * @作者 海豹
	 * @创建时间 2015年6月17日下午11:29:25
	 * @param xmbh
	 * @param qlid
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/updateZxxx/{qlid}", method = RequestMethod.POST)
	public @ResponseBody ResultMessage updateZxxx(@PathVariable("qlid") String qlid, HttpServletRequest request,
			HttpServletResponse response) {
		ResultMessage m = new ResultMessage();
		String dataString = request.getParameter("info");
		JSONObject object = JSON.parseObject(dataString);
		qlService.updateZxxxInfo(qlid, object);
		m.setMsg("更新成功");
		m.setSuccess("true");
		YwLogUtil.addYwLog("注销信息-更新成功", ConstValue.SF.YES.Value, ConstValue.LOG.UPDATE);
		return m;
	}

	/**
	 * 批量更新注销信息
	 */
	@RequestMapping(value = "/plUpdateZxxx/{xmbh}", method = RequestMethod.POST)
	public @ResponseBody ResultMessage plUpdateZxxx(@PathVariable("xmbh") String xmbh, HttpServletRequest request,
			HttpServletResponse response) {
		ResultMessage m = new ResultMessage();
		String dataString = request.getParameter("info");
		JSONObject object = JSON.parseObject(dataString);
		qlService.plUpdateZxxxInfo(xmbh, object);
		m.setMsg("批量更新成功");
		m.setSuccess("true");
		YwLogUtil.addYwLog("批量注销信息-更新成功", ConstValue.SF.YES.Value, ConstValue.LOG.UPDATE);
		return m;
	}

	/**
	 * 注销信息显示： 通过权利id从BDCS_FSQL_GZZ中获取数据（URL:"/getZxxx/{qlid}",Method：GET）
	 * 
	 * @作者 海豹
	 * @创建时间 2015年6月17日下午11:30:16
	 * @param xmbh
	 * @param qlid
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/getZxxx/{qlid}", method = RequestMethod.GET)
	public @ResponseBody Map getZxxx(@PathVariable("qlid") String qlid, HttpServletRequest request,
			HttpServletResponse response) {
		Map map = new HashMap();
		map = qlService.getZxxxInfo(qlid);
		return map;
	}

	/**
	 * 获取异议登记权利详细信息（URL:"/{xmbh}/yydj/{qlid}",Method：GET）
	 * 
	 * @param xmbh
	 * @param qlid
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/{xmbh}/yydj/{qlid}", method = RequestMethod.GET)
	public @ResponseBody Map GetYYDJQLInfo(@PathVariable("xmbh") String xmbh, @PathVariable("qlid") String qlid,
			HttpServletRequest request, HttpServletResponse response) {
		Map m = qlService.GetYYDJQL(qlid);
		return m;
	}

	/**
	 * 更新异议登记权利详细信息（URL:"/{xmbh}/yydj/{qlid}",Method：POST）
	 * 
	 * @param xmbh
	 * @param qlid
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/{xmbh}/yydj/{qlid}", method = RequestMethod.POST)
	public @ResponseBody ResultMessage updateYYDJQLInfo(@PathVariable("xmbh") String xmbh,
			@PathVariable("qlid") String qlid, HttpServletRequest request, HttpServletResponse response) {
		ResultMessage ms = new ResultMessage();
		boolean success = qlService.updateYYDJQL(qlid, request);
		if (success) {
			ms.setMsg("保存成功！");
			ms.setSuccess("true");
			YwLogUtil.addYwLog("异议登记权利信息-保存成功", ConstValue.SF.YES.Value, ConstValue.LOG.ADD);
		} else {
			ms.setMsg("保存失败！");
			ms.setSuccess("false");
			YwLogUtil.addYwLog("异议登记权利信息-保存失败", ConstValue.SF.NO.Value, ConstValue.LOG.ADD);
		}
		return ms;
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/rights/{djdylyname}/{qlid}", method = RequestMethod.GET)
	public @ResponseBody Map getRightsandSubRights(@PathVariable("qlid") String qlid,
			@PathVariable("djdylyname") String djdylyname, HttpServletRequest request) throws ClassNotFoundException {
		DJDYLY ly = DJDYLY.initFromByEnumName(djdylyname.toUpperCase());
		return qlService.getRightsAndSubRights(ly, qlid);
	}

	/**
	 * 获取单元限制详细信息（URL:"/{xzdyid}/xzinfo/",Method：GET）
	 * 
	 * @Title: getXZInfo
	 * @author:yuxuebin
	 * @date：2016年01月07日 下午11:23:18
	 * @param xzdyid
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/{xzdyid}/xzinfo/", method = RequestMethod.GET)
	public @ResponseBody BDCS_DYXZ getXZInfo(@PathVariable("xzdyid") String xzdyid, HttpServletRequest request,
			HttpServletResponse response) {
		BDCS_DYXZ dyxz = qlService.getXZInfo(xzdyid);
		return dyxz;
	}

	/**
	 * 更新单元限制详细信息（URL:"/{xzdyid}/xzinfo/",Method：POST）
	 * 
	 * @Title: updateXZInfo
	 * @author:yuxuebin
	 * @date：2016年01月07日 下午11:23:18
	 * @param xzdyid
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/{xzdyid}/xzinfo/", method = RequestMethod.POST)
	public @ResponseBody ResultMessage updateXZInfo(@PathVariable("xzdyid") String xzdyid, HttpServletRequest request,
			HttpServletResponse response) {
		boolean bUpdate = qlService.updateXZInfo(xzdyid, request);
		ResultMessage ms = new ResultMessage();
		if (bUpdate) {
			ms.setMsg("保存成功！");
			ms.setSuccess("true");
		} else {
			ms.setMsg("保存失败！");
			ms.setSuccess("false");
		}
		return ms;
	}
	
	/**
	 * 批量更新单元限制详细信息（URL:"/{xmbh}/plxzinfo/",Method：POST）
	 * 
	 * @Title: updatePlXZInfo
	 * @author:zhaomf
	 * @date：2016年8月17日15:46:01
	 * @param xzdyid
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/{xmbh}/plxzinfo/", method = RequestMethod.POST)
	public @ResponseBody ResultMessage updatePLXZInfo(@PathVariable("xmbh") String xmbh, HttpServletRequest request,
			HttpServletResponse response) {
		boolean bUpdate = qlService.updatePlXZInfo(xmbh, request);
		ResultMessage ms = new ResultMessage();
		if (bUpdate) {
			ms.setMsg("保存成功！");
			ms.setSuccess("true");
		} else {
			ms.setMsg("保存失败！");
			ms.setSuccess("false");
		}
		return ms;
	}

	/**
	 * 获取单元限制列表（URL:"/{bdcdyid}/{xmbh}/xzinfolist/",Method：GET）
	 * 
	 * @Title: getXZInfoList
	 * @author:yuxuebin
	 * @date：2016年01月08日 下午11:23:18
	 * @param bdcdyid
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings({ "rawtypes" })
	@RequestMapping(value = "/{bdcdyid}/{xmbh}/xzinfolist/", method = RequestMethod.GET)
	public @ResponseBody List<Map> getXZInfoList(@PathVariable("bdcdyid") String bdcdyid,
			@PathVariable("xmbh") String xmbh, HttpServletRequest request, HttpServletResponse response) {
		List<Map> list = qlService.getXZInfoList(bdcdyid, xmbh);
		return list;
	}

	/**
	 * 解除单元限制列表（URL:"/{xzdyid}/{xmbh}/dyxzlifted/",Method：GET）
	 * 
	 * @Title: DYXZLifted
	 * @author:yuxuebin
	 * @date：2016年01月08日 下午11:23:18
	 * @param xzdyid
	 * @param xmbh
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/{xzdyid}/{xmbh}/dyxzlifted/", method = RequestMethod.GET)
	public @ResponseBody ResultMessage DYXZLifted(@PathVariable("xzdyid") String xzdyid,
			@PathVariable("xmbh") String xmbh, HttpServletRequest request, HttpServletResponse response) {
		ResultMessage ms = qlService.DYXZLifted(xzdyid, xmbh);
		return ms;
	}

	/**
	 * 获取解除单元限制详细信息（URL:"/{xzdyid}/{xmbh}/zxxzinfo/",Method：GET）
	 * 
	 * @Title: getZXXZInfo
	 * @author:yuxuebin
	 * @date：2016年01月07日 下午11:23:18
	 * @param xzdyid
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/{xzdyid}/{xmbh}/zxxzinfo/", method = RequestMethod.GET)
	public @ResponseBody BDCS_XM_DYXZ getZXXZInfo(@PathVariable("xzdyid") String xzdyid,
			@PathVariable("xmbh") String xmbh, HttpServletRequest request, HttpServletResponse response) {
		BDCS_XM_DYXZ xmdyxz = qlService.getZXXZInfo(xzdyid, xmbh);
		return xmdyxz;
	}

	/**
	 * 更新解除单元限制详细信息（URL:"/{xzdyid}/{xmbh}/zxxzinfo/",Method：POST）
	 * 
	 * @Title: updateZXXZInfo
	 * @author:yuxuebin
	 * @date：2016年01月07日 下午11:23:18
	 * @param xzdyid
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/{xzdyid}/{xmbh}/zxxzinfo/", method = RequestMethod.POST)
	public @ResponseBody ResultMessage updateZXXZInfo(@PathVariable("xzdyid") String xzdyid,@PathVariable("xmbh") String xmbh, HttpServletRequest request, HttpServletResponse response) {
		boolean bUpdate = qlService.updateZXXZInfo(xzdyid,xmbh, request);
		ResultMessage ms = new ResultMessage();
		if (bUpdate) {
			ms.setMsg("保存成功！");
			ms.setSuccess("true");
		} else {
			ms.setMsg("保存失败！");
			ms.setSuccess("false");
		}
		return ms;
	}
	
	/**
	 * 批量更新解除单元限制详细信息（URL:"/{xmbh}/plzxxzinfo/",Method：POST）
	 * 
	 * @Title: updateZXXZInfo
	 * @author:yuxuebin
	 * @date：2016年01月07日 下午11:23:18
	 * @param xzdyid
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/{xmbh}/plzxxzinfo/", method = RequestMethod.POST)
	public @ResponseBody ResultMessage updatePlZXXZInfo(@PathVariable("xmbh") String xmbh,
			HttpServletRequest request, HttpServletResponse response) {
		boolean bUpdate = qlService.updatePlZXXZInfo(xmbh,request);
		ResultMessage ms = new ResultMessage();
		if (bUpdate) {
			ms.setMsg("保存成功！");
			ms.setSuccess("true");
		} else {
			ms.setMsg("保存失败！");
			ms.setSuccess("false");
		}
		return ms;
	}

	/**
	 * 获取抵押权变更权利信息（URL:"{xmbh}/bgdyq/qlinfo/{type}",Method：POST）
	 * 
	 * @作者 YUXUEBIN
	 * @创建时间 2016年01月10日下午5:54:53
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "{xmbh}/bgdyq/qlinfo/{type}", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> getBGdyqInfo(@PathVariable("xmbh") String xmbh,
			@PathVariable("type") String type, HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();
		map = qlService.getBGdyqInfo(xmbh, type);
		return map;
	}
	
	/**
	 * 单个抵押权变更权利信息（URL:"{bdcdyid}/{xmbh}/qlinfo/{type}",Method：GET）
	 * 
	 * @作者 luml
	 * @创建时间 2017年07月19日下午6:12:53
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "{bdcdyid}/{xmbh}/qlinfo/{type}", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> getBGdyqInfo_dy(@PathVariable("bdcdyid") String bdcdyid,@PathVariable("xmbh") String xmbh,
			@PathVariable("type") String type, HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();
		map = qlService.getBGdyqInfo_dy(bdcdyid,xmbh, type);
		return map;
	}
	

	/**
	 * 获取抵押权变更抵押权人权利信息（URL:"{xmbh}/bgdyq/qlrinfo/{type}",Method：POST）
	 * 
	 * @作者 YUXUEBIN
	 * @创建时间 2016年01月10日下午5:54:53
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "{xmbh}/bgdyq/qlrinfo/{type}", method = RequestMethod.GET)
	public @ResponseBody List<RightsHolder> getBGdyqrInfo(@PathVariable("xmbh") String xmbh,
			@PathVariable("type") String type, HttpServletRequest request, HttpServletResponse response) {
		List<RightsHolder> list = qlService.getBGdyqrInfo(xmbh, type);
		return list;
	}
	
	/**
	 * 获取（当前一条）抵押权变更 抵押权人信息（URL:"/qlrgz/{qlrid}",Method：GET）
	 * @作者 WLB
	 * @创建时间 2017年05月25日下午22:54:53
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value =  "/qlrgz/{qlrid}", method = RequestMethod.GET)
	public @ResponseBody BDCS_QLR_GZ getCurrentDyqrInfo(@PathVariable("qlrid") String qlrid, HttpServletRequest request,
			HttpServletResponse response) {
		BDCS_QLR_GZ bdcs_qlr_gz = new BDCS_QLR_GZ();
		bdcs_qlr_gz = qlService.getCurrentDyqrInfo(qlrid);
		return bdcs_qlr_gz;
	}
    /**
     * 修改变更前抵押权人信息（URL:"/modifybgqdyqlrs/qlrinfo/{qlrid}",Method：POST）
     * 
     * @作者 WEILIBO
	 * @创建时间 2017年05月24日下午20:57:38
	 * @param request
	 * @param response
	 * @return
     */
	@RequestMapping(value = "/modifybgqdyqlrs/qlrinfo/{xmbh}", method = RequestMethod.POST)
	public @ResponseBody ResultMessage modifyBgqDyqlrs(@PathVariable("xmbh") String xmbh, HttpServletRequest request,
			HttpServletResponse response) {
		ResultMessage m = new ResultMessage();
		Map map = transToMAP(request.getParameterMap());
		response.setContentType("text/html;charset=utf-8");
		
		qlService.modifyBgqDyqlrs(map,xmbh);
		
		m.setMsg("更新成功");
		m.setSuccess("true");
		return m;
	}
	
	/**
	 * 保存抵押权变更权利信息（URL:"{xmbh}/bgdyq/qlinfo/",Method：POST）
	 * 
	 * @作者 YUXUEBIN
	 * @创建时间 2016年01月10日下午5:54:53
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/{xmbh}/{bdcdyid}/bgdyq/qlinfo/", method = RequestMethod.POST)
	public @ResponseBody ResultMessage plUpdateDyqxx2(@PathVariable("xmbh") String xmbh,@PathVariable("bdcdyid") String bdcdyid, HttpServletRequest request,
			HttpServletResponse response) {
		ResultMessage m = new ResultMessage();
		qlService.plSaveQlInfo2(xmbh,bdcdyid, request);//更新当前抵押权信息
		m.setMsg("更新成功");
		m.setSuccess("true");
		return m;
	}
	
	/**
	 * 保存抵押权变更权利信息（URL:"{xmbh}/bgdyq/qlinfo/",Method：POST）
	 * 
	 * @作者 YUXUEBIN
	 * @创建时间 2016年01月10日下午5:54:53
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/{xmbh}/bgdyq/qlinfo/", method = RequestMethod.POST)
	public @ResponseBody ResultMessage plUpdateDyqxx2(@PathVariable("xmbh") String xmbh, HttpServletRequest request,
			HttpServletResponse response) {
		ResultMessage m = new ResultMessage();
		qlService.plSaveQlInfo2(xmbh, request);//更新当前抵押权信息
		m.setMsg("更新成功");
		m.setSuccess("true");
		return m;
	}

	/**
	 * 解除单元抵押（URL:"/{qlid}/{xmbh}/dybgcancel/",Method：GET）
	 * 
	 * @Title: dybgCancel
	 * @author:yuxuebin
	 * @date：2016年01月08日 下午11:23:18
	 * @param qlid
	 * @param xmbh
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/{qlid}/{xmbh}/dybgcancel/", method = RequestMethod.GET)
	public @ResponseBody ResultMessage dybgCancel(@PathVariable("qlid") String qlid, @PathVariable("xmbh") String xmbh,
			HttpServletRequest request, HttpServletResponse response) {
		ResultMessage ms = qlService.dybgCancel(qlid, xmbh);
		return ms;
	}

	/**
	 * 添加单元限制详细信息（URL:"/{bdcdyid}/{xmbh}/{bdcdylx}/xzinfo/add/",Method：POST）
	 * 
	 * @Title: AddXZInfo
	 * @author:yuxuebin @date：2016年01月15日 01:10:18
	 * @param bdcdyid
	 * @param xmbh
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/{bdcdyid}/{xmbh}/{bdcdylx}/xzinfo/add/", method = RequestMethod.POST)
	public @ResponseBody ResultMessage AddXZInfo(@PathVariable("bdcdyid") String bdcdyid,
			@PathVariable("xmbh") String xmbh, @PathVariable("bdcdylx") String bdcdylx, HttpServletRequest request,
			HttpServletResponse response) {
		ResultMessage ms = qlService.AddXZInfo(bdcdyid, xmbh, bdcdylx, request);
		return ms;
	}

	/**
	 * 获取单元限制列表（URL:"/{bdcdyid}/{xmbh}/xzinfolist_gz/",Method：GET）
	 * 
	 * @Title: getXZInfoListFromGZ
	 * @author:yuxuebin
	 * @date：2016年01月08日 下午11:23:18
	 * @param bdcdyid
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings({ "rawtypes" })
	@RequestMapping(value = "/{bdcdyid}/{xmbh}/xzinfolist_gz/", method = RequestMethod.GET)
	public @ResponseBody List<Map> getXZInfoListFromGZ(@PathVariable("bdcdyid") String bdcdyid,
			@PathVariable("xmbh") String xmbh, HttpServletRequest request, HttpServletResponse response) {
		List<Map> list = qlService.getXZInfoListFromGZ(bdcdyid, xmbh);
		return list;
	}

	/**
	 * 移除单元限制详细信息（URL:"/{xzdyid}/xzinfo/{xmbh}/",Method：POST）
	 * 
	 * @Title: RemoveXZInfo
	 * @author:yuxuebin
	 * @date：2016年01月07日 下午11:23:18
	 * @param xzdyid
	 * @param xmbh
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/{xzdyid}/xzinfo/{xmbh}/", method = RequestMethod.DELETE)
	public @ResponseBody ResultMessage RemoveXZInfo(@PathVariable("xzdyid") String xzdyid,
			@PathVariable("xmbh") String xmbh, HttpServletRequest request, HttpServletResponse response) {
		boolean bUpdate = qlService.RemoveXZInfo(xzdyid, xmbh);
		ResultMessage ms = new ResultMessage();
		if (bUpdate) {
			ms.setMsg("移除成功！");
			ms.setSuccess("true");
		} else {
			ms.setMsg("移除失败！");
			ms.setSuccess("false");
		}
		return ms;
	}

	/**
	 * 获取当前正在办理抵押的单元的抵押顺位和查封信息（URL:"/mortgageindex/{xmbh}/{bdcdyid}",Method：GET）
	 * 
	 * @Title: getMortgageIndex
	 * @author:liushufeng
	 * @date：2016年1月26日 下午4:29:02
	 * @param bdcdyid
	 * @param xmbh
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/mortgageindex/{xmbh}/{bdcdyid}", method = RequestMethod.GET)
	public @ResponseBody Map getMortgageIndex(@PathVariable("bdcdyid") String bdcdyid,
			@PathVariable("xmbh") String xmbh, HttpServletRequest request, HttpServletResponse response) {
		String sql1 = " FROM BDCK.BDCS_QL_XZ QL WHERE QLLX='23' AND   DJDYID IN(SELECT DJDYID FROM BDCK.BDCS_DJDY_XZ WHERE BDCDYID='"
				+ bdcdyid + "') AND QLID NOT IN(SELECT LYQLID FROM BDCK.BDCS_QL_GZ WHERE XMBH='" + xmbh
				+ "')  AND QLID NOT IN(SELECT QLID FROM BDCK.BDCS_QL_GZ WHERE XMBH='" + xmbh + "')";
		String sql2 = " FROM BDCK.BDCS_QL_XZ QL WHERE QLLX='99' AND DJLX='800' AND   DJDYID IN(SELECT DJDYID FROM BDCK.BDCS_DJDY_XZ WHERE BDCDYID='"
				+ bdcdyid + "')";
		long count1 = dao.getCountByFullSql(sql1);
		long count2 = dao.getCountByFullSql(sql2);
		count1++;
		String cfzt = "未被查封";
		if (count2 > 0)
			cfzt = "已被查封";
		Map<String, String> map = new HashMap<String, String>();
		map.put("DYSW", Long.toString(count1));
		map.put("CFZT", cfzt);
		return map;
	}
	
	@RequestMapping(value = "xzdyq/{xmbh}/{djdyid}/qls", method = RequestMethod.GET)
	public @ResponseBody Message GetXZDYQBYQLID(@PathVariable("xmbh") String xmbh,@PathVariable("djdyid") String djdyid, HttpServletRequest request,
			HttpServletResponse response) {		
		// 获取工作层qlid
/*		String fulsql = " SELECT QLID FROM BDCK.BDCS_QL_GZ QL  WHERE QL.DJDYID='"
				+ djdyid + "' AND QL.XMBH ='"
				+ xmbh + "' AND QL.QLLX='23' ";
		List<Map> listqlid = dao.getDataListByFullSql(fulsql);
		
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map> listmap = new ArrayList<Map>() ;
		if (listqlid != null && listqlid.size() > 0) {
			for (Map mp : listqlid) {
				String qlid = StringHelper.isEmpty(mp.get("QLID")) ? "" : mp.get("QLID").toString();
				if (!StringHelper.isEmpty(qlid)) {
					map = qlService.getzydj_DYQInfo(qlid,"GZ");
					listmap.add(map);
				}
			}
		}else {*/
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map> listmap = new ArrayList<Map>() ;
		String fulsql = " SELECT QLID FROM BDCK.BDCS_QL_XZ QL  WHERE QL.DJDYID='"
					+ djdyid + "' AND QL.QLLX='23' ";
		List<Map> listqlid = dao.getDataListByFullSql(fulsql);
			 if (listqlid != null && listqlid.size() > 0) {
				for (Map mp : listqlid) {
					String qlid = StringHelper.isEmpty(mp.get("QLID")) ? "" : mp.get("QLID").toString();
					if (!StringHelper.isEmpty(qlid)) {
						map = qlService.getzydj_DYQInfo(qlid,"XZ");
						listmap.add(map);
					}
				}
			}else {
				fulsql = " SELECT QLID FROM BDCK.BDCS_QL_LS QL  WHERE QL.DJDYID='"
						+ djdyid + "' AND QL.QLLX='23' ";
				 listqlid = dao.getDataListByFullSql(fulsql);
				 if (listqlid != null && listqlid.size() > 0) {
						for (Map mp : listqlid) {
							String qlid = StringHelper.isEmpty(mp.get("QLID")) ? "" : mp.get("QLID").toString();
							if (!StringHelper.isEmpty(qlid)) {
								map = qlService.getzydj_DYQInfo(qlid,"LS");
								listmap.add(map);
							}
						}
				}
			}
		//}
		
		Page articlePaged = new Page();
		articlePaged.setData(listmap);
		articlePaged.setTotalCount(listmap.size());
		Message m = new Message();
		m.setTotal(articlePaged.getTotalCount());
		m.setRows(articlePaged.getResult());
		return m;
	}
	
	@RequestMapping(value = "gzdyq/{xmbh}/{djdyid}/qls", method = RequestMethod.GET)
	public @ResponseBody Message GetGZDYQBYQLID(@PathVariable("djdyid") String djdyid, @PathVariable("xmbh") String xmbh,HttpServletRequest request,
			HttpServletResponse response) {		
		// 获取工作层qlid getDYQInfo
		String fulsql = " SELECT QLID FROM BDCK.BDCS_QL_GZ QL WHERE QL.DJDYID='"
				+ djdyid + "'AND QL.XMBH='"
				+ xmbh + "' AND QL.QLLX='23' ";
		List<Map> listqlid = dao.getDataListByFullSql(fulsql);
		
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map> listmap = new ArrayList<Map>() ;
		if (listqlid != null && listqlid.size() > 0) {
			for (Map mp : listqlid) {
				String qlid = StringHelper.isEmpty(mp.get("QLID")) ? "" : mp.get("QLID").toString();
				if (!StringHelper.isEmpty(qlid)) {
					map = qlService.getDYQInfo(xmbh,qlid);
					listmap.add(map);
				}
			}
		}
		
		Page articlePaged = new Page();
		articlePaged.setData(listmap);
		articlePaged.setTotalCount(listmap.size());
		Message m = new Message();
		m.setTotal(articlePaged.getTotalCount());
		m.setRows(articlePaged.getResult());
		return m;
	}
	
	@RequestMapping(value = "/getjgdw/{qlid}", method = RequestMethod.GET)
	public @ResponseBody String GetJGDW( @PathVariable("qlid") String qlid,
			HttpServletRequest request, HttpServletResponse response) {
		String sqlforZQDW = "SELECT FSQL.ZQDW FROM BDCK.BDCS_FSQL_GZ FSQL WHERE FSQL.QLID=''{0}''";
		 sqlforZQDW = MessageFormat.format(sqlforZQDW, qlid);				
			List<Map> listZQDW = dao.getDataListByFullSql(sqlforZQDW);
			String jgdw="";
			if (!listZQDW.isEmpty()) {				
				jgdw = StringHelper.formatObject(listZQDW.get(0).get("ZQDW"));	
			}else{
				jgdw = "1";	
			}		
		return jgdw;
	}
	
	@RequestMapping(value = "/savajgdw/{qlid}/{jgdw}", method = RequestMethod.GET)
	public @ResponseBody void UpdateJGDW(@PathVariable("qlid") String qlid, @PathVariable("jgdw") String jgdw, HttpServletRequest request, HttpServletResponse response) {
		BDCS_QL_GZ ql = qlService.GetQL(qlid);
		if (ql != null) {
			BDCS_FSQL_GZ fsql = qlService.GetFSQL(ql.getFSQLID());				
			if (fsql != null) {			
				fsql.setZQDW(jgdw);
				qlService.UpdateFSQL(fsql);
			}
			
		}
		
	}
	
	/**
	 * 更新权利中的备注（URL:"/{xmbh}/lsqls/{qlid}",Method：POST）
	 */
	@RequestMapping(value = "/{xmbh}/lsqls/{qlid}", method = RequestMethod.POST)
	public @ResponseBody ResultMessage UpdateLSQLforBZ(@PathVariable("xmbh") String xmbh,
			@PathVariable("qlid") String qlid, HttpServletRequest request, HttpServletResponse response) {
		ResultMessage resultMessage = new ResultMessage();
		String bz = null;
		try {
			bz = RequestHelper.getParam(request, "bz");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//String bz = request.getParameter("bz");
		if(!StringHelper.isEmpty(qlid)){
			BDCS_QL_LS lsql = dao.get(BDCS_QL_LS.class, qlid);
			BDCS_QL_XZ xzql = dao.get(BDCS_QL_XZ.class, qlid);
			if(lsql!=null && !StringHelper.isEmpty(bz)){
				lsql.setBZ(bz);
				dao.update(lsql);
			}
			if(xzql!=null && !StringHelper.isEmpty(bz)){
				xzql.setBZ(bz);
				dao.update(xzql);
			}
		}
		resultMessage.setSuccess("true");
		resultMessage.setMsg("更新成功!");
		YwLogUtil.addYwLog("更新权利中的备注-更新成功", ConstValue.SF.YES.Value, ConstValue.LOG.UPDATE);
		return resultMessage;
	}
	
	/**
	 * 根据权利ID获取权利人列表（URL:"/{xmbh}/qls/{qlid}/qlrs",Method：GET）
	 * 
	 * @param xmbh
	 * @param djdyid
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/qls/{qlid}/lsqlrs", method = RequestMethod.GET)
	public @ResponseBody Message GetLSQLRSBYQLID( @PathVariable("qlid") String qlid,
			HttpServletRequest request, HttpServletResponse response) {
		ResultMessage resultMessage = new ResultMessage();
		String sqlforQLRS = "SELECT QLRID, QLRMC,ZJH,XMBH,ZJZL FROM BDCK.BDCS_QLR_LS QLR WHERE QLR.QLID=''{0}''";
		sqlforQLRS = MessageFormat.format(sqlforQLRS, qlid);				
		List<Map> qlrs  = dao.getDataListByFullSql(sqlforQLRS);
		Message m = new Message();
		
		if (qlrs!=null && qlrs.size()>0) {
			m.setTotal(qlrs.size());
			m.setRows(qlrs);
			m.setSuccess("true");	
		}else{
			m.setMsg("该权利没有权利人，不可更新证件号码！");
			m.setRows(new ArrayList<Map>());
			m.setSuccess("false");	
		}		
		return m;		
	}
	
	/**
	 * 更新权利人证件号--房屋查询（URL:"/qlrs/{qlrid}",Method：POST）
	 * 
	 * @param xmbh
	 * @param qlrid
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	@SuppressWarnings({ "deprecation", "rawtypes", "unchecked" })
	@RequestMapping(value = "/updateQLRZJH", method = RequestMethod.POST)
	public @ResponseBody ResultMessage UpdateQLRZJH( HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		ResultMessage m = new ResultMessage();
		m.setSuccess("false");
		m.setMsg("更新权利人失败！请联系管理员！");
		String rows=RequestHelper.getParam(request, "rows");
		JSONArray jsonArray = JSONArray.parseArray(rows);
		for (Object row : jsonArray) {
			Map map = (Map) row;
			String zjh = StringHelper.formatObject(map.get("ZJH"));
			String zjzl = StringHelper.formatObject(map.get("ZJZL"));
			String qlrid = StringHelper.formatObject(map.get("QLRID"));
			//gz层
			RightsHolder holder_gz = RightsHolderTools.loadRightsHolder(DJDYLY.GZ, qlrid);
			if (holder_gz != null) {
				BDCS_QLR_GZ holdergz = (BDCS_QLR_GZ) holder_gz;
				holder_gz.setZJH(zjh);
				holder_gz.setZJZL(zjzl);
				dao.update(holder_gz);	
				dao.flush();
				m.setSuccess("true");
				m.setMsg("更新权利人证件成功!");
				YwLogUtil.addYwLog("更新权利人证件信息-更新成功", ConstValue.SF.YES.Value, ConstValue.LOG.UPDATE);
			} else {
				YwLogUtil.addYwLog("更新权利人证件信息-更新失败", ConstValue.SF.NO.Value, ConstValue.LOG.UPDATE);
			}
			//xz
			RightsHolder holder_xz = RightsHolderTools.loadRightsHolder(DJDYLY.XZ, qlrid);
			if (holder_xz != null) {
				BDCS_QLR_XZ holdergz = (BDCS_QLR_XZ) holder_xz;
				holder_xz.setZJH(zjh);
				holder_xz.setZJZL(zjzl);
				dao.update(holder_xz);	
				dao.flush();
				m.setSuccess("true");
				m.setMsg("更新权利人证件成功!");
				YwLogUtil.addYwLog("更新权利人证件信息-更新成功", ConstValue.SF.YES.Value, ConstValue.LOG.UPDATE);
			} else {
				YwLogUtil.addYwLog("更新权利人证件信息-更新失败", ConstValue.SF.NO.Value, ConstValue.LOG.UPDATE);
			}
			//ls
			RightsHolder holder_ls = RightsHolderTools.loadRightsHolder(DJDYLY.LS, qlrid);
			if (holder_ls != null) {
				BDCS_QLR_LS holdergz = (BDCS_QLR_LS) holder_ls;
				holder_ls.setZJH(zjh);
				holder_ls.setZJZL(zjzl);
				dao.update(holder_ls);	
				dao.flush();
				m.setSuccess("true");
				m.setMsg("更新权利人证件成功!");
				YwLogUtil.addYwLog("更新权利人证件信息-更新成功", ConstValue.SF.YES.Value, ConstValue.LOG.UPDATE);
			} else {
				YwLogUtil.addYwLog("更新权利人证件信息-更新失败", ConstValue.SF.NO.Value, ConstValue.LOG.UPDATE);
			}
		}
		return m;
	}
	
	/**
	 * 更新多个权利详细信息（登记原因、附记、持证方式、取得价格、期限、）（URL:"/{xmbh}/ql/{jgdw}",Method：POST）
	 * 
	 * @param xmbh
	 * @param qlid
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/{xmbh}/ql/{jgdw}", method = RequestMethod.POST)
	@AccessRequired.UnBoardRequired
	public @ResponseBody ResultMessage PLUpdateQLInfo(@PathVariable("xmbh") String xmbh,@PathVariable("jgdw") String jgdw,
			HttpServletRequest request, HttpServletResponse response) {
		ResultMessage resultMessage = new ResultMessage();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date qssj = new Date();
		try {
			String strqssj = request.getParameter("qLQSSJ");
			if (!StringHelper.isEmpty(strqssj)) {
				qssj = sdf.parse(request.getParameter("qLQSSJ"));
			} else {
				qssj = null;
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Date jssj = new Date();
		try {
			String strjssj = request.getParameter("qLJSSJ");
			if (!StringHelper.isEmpty(strjssj)) {
				jssj = sdf.parse(request.getParameter("qLJSSJ"));
			} else {
				jssj = null;
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		String djyy = request.getParameter("dJYY").replaceAll("\u00A0", " ");
		String fj = request.getParameter("fJ").replaceAll("\u00A0", " ");
		String czfs = request.getParameter("cZFS");
		String qdjg = request.getParameter("qDJG");
		String tdshyqr = request.getParameter("tDSHYQR");
		String bz = request.getParameter("bZ");
		String tdyt = "";
		String qs = request.getParameter("qS");
		try {
			tdyt = RequestHelper.getParam(request, "tdyt");
		} catch (Exception e) {
		}
		if (StringUtils.isEmpty(qdjg)) {
			qdjg = "0";
		}
		double lqdjg = Double.parseDouble(qdjg);
		boolean brebuild = false;
		List<Map> qlidList =  dao.getDataListByFullSql("select qlid from  BDCK.BDCS_QL_GZ  where xmbh='"+xmbh+"' and qllx !='23'");
		if (qlidList.size() > 0) {
			for (int i = 0; i < qlidList.size(); i++) {
				BDCS_QL_GZ ql =  qlService.GetQL(qlidList.get(i).get("QLID").toString());
				String tempczfs= ql.getCZFS();
				brebuild = !(!StringUtils.isEmpty(ql.getCZFS()) && ql.getCZFS().equals(czfs));
				ql.setId(ql.getId());
				ql.setQLQSSJ(qssj);
				ql.setQDJG(lqdjg);
				ql.setQLJSSJ(jssj);
				ql.setDJYY(djyy);
				ql.setFJ(fj);
				ql.setCZFS(czfs);
				ql.setTDSHYQR(tdshyqr);
				ql.setBZ(bz);
				ql.setQS(qs);
			    BDCS_FSQL_GZ fsql=qlService.GetFSQL(ql.getFSQLID());
			    if(fsql !=null){
			    	if (!StringHelper.isEmpty(request.getParameter("dYQNR"))) {
						String dyqnr = request.getParameter("dYQNR").replaceAll("\u00A0", " ");			
				    	dyqnr=StringHelper.FormatByDatatype(dyqnr);
				    	fsql.setDYQNR(dyqnr);
				    	dao.update(fsql);
				    	dao.flush();
					}
			    	fsql.setZQDW(jgdw);
			    }
				//鹰潭房产共享，共享项目编号保存至权利CASENUM中  edit by 王帅 2016/8/20
				String fcgxxmbh = request.getParameter("fcgxxmbh");
				if(fcgxxmbh != "" && fcgxxmbh != null){
					ql.setCASENUM(fcgxxmbh);
				}
				String XZQHDM=ConfigHelper.getNameByValue("XZQHDM");
				if("6501".equals(XZQHDM.length()>4?XZQHDM.substring(0,4):XZQHDM)){
					if(!StringHelper.isEmpty(request.getParameter("CASENUM"))){
						ql.setCASENUM(request.getParameter("CASENUM"));
					}
				}
				if (brebuild) {
					JSONObject jsonobj=NewLogTools.getJSONByXMBH(xmbh);
					jsonobj.put("OperateType", "持证方式");
					JSONObject jsonql=new JSONObject();
					jsonql.put("QLID", ql.getId());
					String tempczfsmc=ConstHelper.getNameByValue("CZFS", tempczfs);
					String czfsmc=ConstHelper.getNameByValue("CZFS", czfs);
					jsonql.put("CZFS修改方式", "从"+tempczfsmc+"修改为"+czfsmc);
					jsonobj.put("QLINFOS", jsonql);
					NewLogTools.saveLog(jsonobj.toString(), xmbh, "5", "持证方式");
					qlService.UpdateQLandRebuildRelation(ql);
				} else {
					qlService.UpdateQL(ql);
				}
				if (!StringHelper.isEmpty(tdyt)) {
					qlService.Updatetdyt(ql.getDJDYID(), tdyt, xmbh);
				}
				if(!StringHelper.isEmpty(request.getParameterMap().get("zyqlrmc1"))){
					String[] zyqlrids=(String[])request.getParameterMap().get("zyqlrmc1");
					qlService.UpdateZYQLRList(zyqlrids,ql);
				}
				
			}
			resultMessage.setSuccess("true");
			resultMessage.setMsg("更新成功!");
			YwLogUtil.addYwLog("保存房屋性质-更新成功", ConstValue.SF.YES.Value, ConstValue.LOG.UPDATE);
		}

		return resultMessage;
	}
	 @RequestMapping(value={"/{qlid}"}, method={org.springframework.web.bind.annotation.RequestMethod.POST})
	  @ResponseBody
	  public Message getAllMortgage(@PathVariable("qlid") String qlid)
	  {
	    Message msg = this.qlService.getAllMortgage(qlid);
	    return msg;
	  }



	/**
	 * 获取完税信息详情
	 * @param request
	 * @return
	 * @throws ClassNotFoundException
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/wsxxinfo", method = RequestMethod.GET)
	public @ResponseBody List<BDCS_WSXX> getWsxxInfo(HttpServletRequest request) throws UnsupportedEncodingException {
		String xmbh = RequestHelper.getParam(request,"xmbh");
		String qlid = RequestHelper.getParam(request,"qlid");
		return qlService.getWsxxInfo(xmbh, qlid);
	}
}
