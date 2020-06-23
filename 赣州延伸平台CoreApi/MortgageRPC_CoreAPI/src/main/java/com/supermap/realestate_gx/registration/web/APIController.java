package com.supermap.realestate_gx.registration.web;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import parsii.eval.Scope;
import parsii.eval.Variable;









//import com.supermap.realestate.registration.factorys.GX_HandlerFactory;
import com.supermap.realestate.registration.factorys.HandlerFactory;
import com.supermap.realestate.registration.mapping.CheckConfig;
import com.supermap.realestate.registration.mapping.CheckConfig.CheckGroup;
import com.supermap.realestate.registration.mapping.CheckConfig.CheckItem;
import com.supermap.realestate.registration.mapping.HandlerMapping.RegisterWorkFlow;
import com.supermap.realestate.registration.mapping.ICheckItem;
import com.supermap.realestate.registration.model.BDCS_CONSTRAINT;
import com.supermap.realestate.registration.model.BDCS_CONSTRAINTRT;
import com.supermap.realestate.registration.service.QueryService;
import com.supermap.realestate.registration.util.ConstValue.ConstraintsType;
import com.supermap.realestate.registration.util.ConfigHelper;
import com.supermap.realestate.registration.util.ConstHelper;
import com.supermap.realestate.registration.util.ConstValue;
import com.supermap.realestate.registration.util.DateUtil;
import com.supermap.realestate.registration.util.RequestHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.realestate.registration.util.SystemConfig;
import com.supermap.realestate.registration.util.YwLogUtil;
import com.supermap.realestate_gx.registration.factorys.GX_HandlerFactory;
import com.supermap.realestate_gx.registration.service.APIService;
import com.supermap.wisdombusiness.core.SuperHelper;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import com.supermap.wisdombusiness.web.Message;
import com.supermap.wisdombusiness.web.ResultMessage;
import com.supermap.wisdombusiness.web.ui.Page;
import com.supermap.wisdombusiness.workflow.model.Wfd_Pass_Condition;

/**
 * 配置Controller，流程相关的配置放在这里边
 * @ClassName: ConfigController
 * @author liushufeng
 * @date 2015年7月23日 下午11:10:04
 */
@Controller
@RequestMapping("/api")
public class APIController {
	
	/** 查询service */
	@Autowired
	private QueryService queryService;
	@Autowired
	private APIService apiService;
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/land", method = RequestMethod.GET)
	public @ResponseBody Message land(HttpServletRequest request, HttpServletResponse response) {
		Integer page = 1;
		if (request.getParameter("page") != null) {
			page = Integer.parseInt(request.getParameter("page"));
		}
		String querytype = request.getParameter("QUERYTYPE");
		boolean iflike = false;
		if (querytype != null && querytype.toLowerCase().equals("true")) {
			iflike = true;
		}
		Integer rows = 10;
		if (request.getParameter("rows") != null) {
			rows = Integer.parseInt(request.getParameter("rows"));
		}
		Map<String, String> queryvalues = new HashMap<String, String>();
		try {
		String sort = RequestHelper.getParam(request, "sort");
		// 排序字段
		String order = RequestHelper.getParam(request, "order");// 排序Order
		String qlrmc = RequestHelper.getParam(request, "QLRMC");// 权利人名称
		String qlrzjh = RequestHelper.getParam(request, "ZJH");// 权利人证件号
		String tdzl = RequestHelper.getParam(request, "ZL");// 土地坐落
		String bdcqzh = RequestHelper.getParam(request, "BDCQZH");// 不动产权证号
		String ywbh = RequestHelper.getParam(request, "YWBH");// 业务编号
		String dyzt = RequestHelper.getParam(request, "DYZT");// 抵押状态
		String cfzt = RequestHelper.getParam(request, "CFZT");// 查封状态
		String fwzt=RequestHelper.getParam(request,"TDZT");
		String bdcdyh=RequestHelper.getParam(request, "BDCDYH");
		String zddm=RequestHelper.getParam(request, "ZDDM");
		queryvalues.put("DY.ZL", tdzl);
		queryvalues.put("DY.BDCDYH", bdcdyh);
		queryvalues.put("DY.ZDDM", zddm);
		queryvalues.put("QLR.QLRMC", qlrmc);
		queryvalues.put("QLR.ZJH", qlrzjh);
		queryvalues.put("QL.BDCQZH", bdcqzh);
		queryvalues.put("QL.YWH", ywbh);
		queryvalues.put("DYZT", dyzt);
		queryvalues.put("CFZT", cfzt);
		if(StringUtils.isEmpty("sort"))
			sort="BDCDYID";
		if(StringUtils.isEmpty("order"))
			order="ASC";
		//记录调用接口内容
		Map<String, String> logMap = new HashMap<String, String>();
		Boolean isAllEmpty=true;
		StringBuilder logstring=new StringBuilder("宗地查询API接口调用，查询内容：");
		logMap.put("DY.ZL", "宗地坐落");
		logMap.put("DY.BDCDYH", "宗地不动产单元号");
		logMap.put("DY.ZDDM", "宗地代码");
		logMap.put("QLR.QLRMC", "宗地权利人名称");
		logMap.put("QLR.ZJH", "宗地权利人证件号");
		logMap.put("QL.BDCQZH", "宗地权证号");
		logMap.put("QL.YWH", "宗地业务号");
		logMap.put("DYZT", "宗地抵押状态");
		logMap.put("CFZT", "宗地查封状态");
		for(Map.Entry<String, String> entry : queryvalues.entrySet())   
		{   
		if(!StringUtils.isEmpty(entry.getValue())){
			isAllEmpty=false;
			logstring.append(logMap.get(entry.getKey()));
			logstring.append(":[");
			logstring.append(entry.getValue());
			logstring.append("]，"); 
			}
		} 
		logstring.append("当前页码："); 
		logstring.append(page); 
		logstring.append("，每页数："); 
		logstring.append(rows); 
		logstring.append(",查询方式："); 
		logstring.append(iflike==true?"模糊查询。":"精确查询。");
		//如果无查询条件不准返回数据
		if(isAllEmpty)
		{
			Message error=new Message();
			error.setMsg("请最少传入一个查询条件");
			error.setSuccess("false");
			return error;	
		}
		if(StringUtils.isEmpty(dyzt))
			dyzt="0";
		 if(StringUtils.isEmpty(cfzt))
			 cfzt="0";
		 if(StringUtils.isEmpty(fwzt))
			 fwzt="3";
		//记录调用接口内容
		YwLogUtil.addYwLog(logstring.toString(), ConstValue.SF.YES.Value,ConstValue.LOG.SEARCH);
		//Message success= queryService.queryLand(queryvalues, page, rows, iflike,fwzt,sort,order);
		Message success= apiService.queryLand(queryvalues, page, rows, iflike,fwzt,sort,order);
		List<Map> resultList=new ArrayList<Map>();
		//重新封装查询结果。
		if(success.getRows().size()>0)
		{
			for(Object r:success.getRows())
			{
				Map newm=new HashMap();
				Map oldm=(Map)r;
				Map olddy=(Map)oldm.get("DY");
				Map newdy=new HashMap();
				newdy.put("ZL", olddy.get("ZL"));
				newdy.put("BDCDYH", olddy.get("BDCDYH"));
				newdy.put("BDCDYH", olddy.get("BDCDYH"));
				newdy.put("BDCDYLX", olddy.get("BDCDYLX"));
				newdy.put("QLXZ", olddy.get("QLXZ"));
				newdy.put("PZYT", olddy.get("PZYT"));
				newdy.put("DJZT", olddy.get("DJZT"));
				newdy.put("DJQMC", olddy.get("DJQMC"));
				newdy.put("ZDDM", olddy.get("ZDDM"));
				newdy.put("ZDMJ", olddy.get("ZDMJ"));
				//newdy.put("DYZTFLAG", olddy.get("DYZTFLAG"));
				//newdy.put("CFZTFLAG", olddy.get("CFZTFLAG"));
				newdy.put("DYZT", olddy.get("DYZT"));
				newdy.put("CFZT", olddy.get("CFZT"));
				newdy.put("YYZT", olddy.get("YYZT"));
		
				//权利信息
				List<Map> oldrightList=new ArrayList<Map>();
				List<Map> newrightList=new ArrayList<Map>();
			
				oldrightList=(List<Map>)oldm.get("QLXX");
				for(Map oldright:oldrightList)
				{
					Map newrightMap=new HashMap();
					Map newright=new HashMap();
					Map oldrightQL=(Map)oldright.get("QL");
					Map oldrightFSQL=(Map)oldright.get("FSQL");
					List<Map> oldrightHolderList=(List<Map>)oldright.get("QLR");
					List<Map> newrightHolderList=new ArrayList<Map>();
					newright.put("BDCQZH", oldrightQL.get("BDCQZH"));
					newright.put("YWH", oldrightQL.get("YWH"));
					newright.put("QLLX", oldrightQL.get("QLLX"));
					newright.put("QLLXMC", oldrightQL.get("QLLXMC"));
					newright.put("QLJSSJ", oldrightQL.get("QLJSSJ"));
					newright.put("QLQSSJ", oldrightQL.get("QLQSSJ"));
					newright.put("DBR", oldrightQL.get("DBR"));
					newright.put("DJYY", oldrightQL.get("DJYY"));
					newright.put("DJSJ", oldrightQL.get("DJSJ"));
					newright.put("DJLX", oldrightQL.get("DJLX"));
					newright.put("DJLXMC", oldrightQL.get("DJLXMC"));
					//如果是抵押权加入抵押权详细信息
					if(String.valueOf(oldrightQL.get("QLLX")).equals("23"))
					{
						Map dyxx=new HashMap();
						dyxx.put("DYR", oldrightFSQL.get("DYR"));
						dyxx.put("ZJJZWZL",oldrightFSQL.get("ZJJZWZL"));
						dyxx.put("DYFS", oldrightFSQL.get("DYFS"));
						dyxx.put("DYMJ", oldrightFSQL.get("DYMJ"));
						dyxx.put("BDBZZQSE",oldrightFSQL.get("BDBZZQSE"));
						dyxx.put("DYQX",oldrightQL.get("QLQSSJ")+" 至  "+oldrightQL.get("QLJSSJ"));
						newright.put("DYXX", dyxx);
					}
					//如果是查封的加入抵押权详细信息
					if(String.valueOf(oldrightQL.get("QLLX")).equals("99")&&String.valueOf(oldrightQL.get("DJLX")).equals("800"))
					{
						Map cfxx=new HashMap();
						cfxx.put("CFJG", oldrightFSQL.get("CFJG"));
						cfxx.put("CFWJ", oldrightFSQL.get("CFWJ"));
						cfxx.put("CFWH", oldrightFSQL.get("CFWH"));
						cfxx.put("CFFW",oldrightFSQL.get("CFFW"));
						cfxx.put("CFLX",oldrightFSQL.get("CFLX"));
						cfxx.put("LHSX",oldrightFSQL.get("LHSX"));
						cfxx.put("CFQX",oldrightFSQL.get("QLQSSJ")+" 至  "+oldrightFSQL.get("QLJSSJ"));
						newright.put("CFXX", cfxx);
					}
					for(Map oldrightHolder: oldrightHolderList){
						Map newrightHolder=new HashMap();
						newrightHolder.put("GYQK", oldrightHolder.get("GYQK"));
						newrightHolder.put("GYFS", oldrightHolder.get("GYFS"));
						newrightHolder.put("QLRMC", oldrightHolder.get("QLRMC"));
						newrightHolder.put("QLRLX", oldrightHolder.get("QLRLX"));
						newrightHolder.put("ZJH", oldrightHolder.get("ZJH"));
						newrightHolder.put("ZJZL", oldrightHolder.get("ZJZL"));
						newrightHolder.put("SXH", oldrightHolder.get("SXH"));
						newrightHolder.put("XB", oldrightHolder.get("XB"));
						newrightHolder.put("HJSZSS", oldrightHolder.get("HJSZSS"));
						newrightHolder.put("DH", oldrightHolder.get("DH"));
						newrightHolderList.add(newrightHolder);
					}
					newrightMap.put("QL", newright);
					newrightMap.put("QLR", newrightHolderList);
					newrightList.add(newrightMap);
				}
				newm.put("QLXX", newrightList);
				newm.put("DY", newdy);//单元信息
				resultList.add(newm);
			}
		}
		success.setRows(resultList);
		success.setMsg("成功");
		success.setSuccess("true");
		return success;
		} catch (UnsupportedEncodingException e) {
			Message error=new Message();
			error.setMsg(e.getMessage());
			error.setSuccess("false");
			return error;
		}
	}
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/house", method = RequestMethod.GET)
	public @ResponseBody Message house(HttpServletRequest request, HttpServletResponse response) {
		Integer page = 1;
		if (request.getParameter("page") != null) {
			page = Integer.parseInt(request.getParameter("page"));
		}
		String querytype = request.getParameter("QUERYTYPE");
		boolean iflike = false;
		if (querytype != null && querytype.toLowerCase().equals("true")) {
			iflike = true;
		}
		Integer rows = 10;
		if (request.getParameter("rows") != null) {
			rows = Integer.parseInt(request.getParameter("rows"));
		}
		Map<String, String> queryvalues = new HashMap<String, String>();
		try {
			String sort = RequestHelper.getParam(request, "sort");// 排序字段
			String order = RequestHelper.getParam(request, "order");// 排序Order
			String qlrmc = RequestHelper.getParam(request, "QLRMC");// 权利人名称
			String dyr = RequestHelper.getParam(request, "DYR");// 抵押人名称
			String qlrzjh = RequestHelper.getParam(request, "ZJH");// 权利人证件号
			String fwzl = RequestHelper.getParam(request, "ZL");// 房屋坐落
			String bdcqzh = RequestHelper.getParam(request, "BDCQZH");// 不动产权证号
			String fwbm = RequestHelper.getParam(request, "FWBM");// 房屋编码
			String fh = RequestHelper.getParam(request, "FH");// 房号
			String ywbh = RequestHelper.getParam(request, "YWBH");// 业务编号
			String dyzt = RequestHelper.getParam(request, "DYZT");// 抵押状态 0全部， 未抵押1，抵押中2
		
			String cfzt = RequestHelper.getParam(request, "CFZT");// 查封状态 0全部，未查封1，查封中2
			 
			String cxzt = RequestHelper.getParam(request, "CXZT");// 查询状态，现状查询1，历史查询2
		
			String fwzt=RequestHelper.getParam(request,"FWZT");//房屋状态 3 全部，现房1，期房2
			
			String bdcdyh=RequestHelper.getParam(request, "BDCDYH");
			String bdcqzhxh=RequestHelper.getParam(request, "BDCQZHXH");
			queryvalues.put("DY.ZL", fwzl);
			queryvalues.put("DY.BDCDYH", bdcdyh);
			queryvalues.put("QLR.QLRMC", qlrmc);
			queryvalues.put("DYR.DYR", dyr);
			queryvalues.put("QLR.ZJH", qlrzjh);
			queryvalues.put("QL.BDCQZH", bdcqzh);
			queryvalues.put("QLR.BDCQZHXH", bdcqzhxh);
			queryvalues.put("DY.FWBM", fwbm);
			queryvalues.put("DY.FH", fh);
			queryvalues.put("QL.YWH", ywbh);
			queryvalues.put("DYZT", dyzt);
			queryvalues.put("CFZT", cfzt);
			queryvalues.put("CXZT", cxzt);
		if(StringUtils.isEmpty("sort"))
			sort="BDCDYID";
		if(StringUtils.isEmpty("order"))
			order="ASC";
		//记录调用接口内容
		Map<String, String> logMap = new HashMap<String, String>();
		Boolean isAllEmpty=true;
		StringBuilder logstring=new StringBuilder("房产查询API接口调用，查询内容：");
		logMap.put("DY.ZL", "房屋坐落");
		logMap.put("DY.BDCDYH",  "房屋不动产单元号");
		logMap.put("QLR.QLRMC",  "权利人名称");
		logMap.put("DYR.DYR", "抵押人");
		logMap.put("QLR.ZJH", "抵押权人证件号");
		logMap.put("QL.BDCQZH", "不动产权证号");
		logMap.put("QLR.BDCQZHXH", "不动产权证号序号");
		logMap.put("DY.FWBM", "房屋编码");
		logMap.put("DY.FH", "房号");
		logMap.put("QL.YWH", "业务号");
		logMap.put("DYZT", "房屋抵押状态");
		logMap.put("CFZT", "房屋查封状态");
		logMap.put("CXZT", "查询状态");
		
	
		for(Map.Entry<String, String> entry : queryvalues.entrySet())   
		{   
		if(!StringUtils.isEmpty(entry.getValue())){
			isAllEmpty=false;
			logstring.append(logMap.get(entry.getKey()));
			logstring.append(":[");
			logstring.append(entry.getValue());
			logstring.append("]，"); 
			}
		} 
/*		logstring.append("当前页码："); 
		logstring.append(page); 
		logstring.append("，每页数："); 
		logstring.append(rows); */
		logstring.append(",查询方式："); 
		logstring.append(iflike==true?"模糊查询。":"精确查询。");
		//如果无查询条件不准返回数据
		if(isAllEmpty)
		{
			Message error=new Message();
			error.setMsg("请最少传入一个查询条件");
			error.setSuccess("false");
			return error;	
		}
		if(StringUtils.isEmpty(cfzt))
			 cfzt="0";
		 if(StringUtils.isEmpty(dyzt))
			 dyzt="0";
		 if(StringUtils.isEmpty(cxzt))
			 cxzt="1";
		 if(StringUtils.isEmpty(fwzt))
			 fwzt="3";
		//记录调用接口内容
		YwLogUtil.addYwLog(logstring.toString(), ConstValue.SF.YES.Value,ConstValue.LOG.SEARCH);
		//Message success= queryService.queryHouse(queryvalues, page, rows, iflike,fwzt,sort,order);
		Message success= apiService.queryHouse(queryvalues, page, rows, iflike,fwzt,sort,order);
		List<Map> resultList=new ArrayList<Map>();
		//重新封装查询结果。
		if(success.getRows().size()>0)
		{
			for(Object r:success.getRows())
			{
				Map newm=new HashMap();
				Map oldm=(Map)r;
				Map olddy=(Map)oldm.get("DY");
				Map newdy=new HashMap();
				newdy.put("ZL", olddy.get("ZL"));
				newdy.put("BDCDYH", olddy.get("BDCDYH"));
				newdy.put("BDCDYH", olddy.get("BDCDYH"));
				newdy.put("BDCDYLX", olddy.get("BDCDYLX"));
				newdy.put("FWXZ", olddy.get("FWXZ"));
				newdy.put("GHYT", olddy.get("GHYT"));
				newdy.put("DJZT", olddy.get("DJZT"));
				newdy.put("DJQMC", olddy.get("DJQMC"));
				newdy.put("ZDMJ", olddy.get("ZDMJ"));
				//newdy.put("PZYT", olddy.get("PZYT"));
				//newdy.put("DYZTFLAG", olddy.get("DYZTFLAG"));
				//newdy.put("CFZTFLAG", olddy.get("CFZTFLAG"));
				newdy.put("DYZT", olddy.get("DYZT"));
				newdy.put("CFZT", olddy.get("CFZT"));
				newdy.put("YYZT", olddy.get("YYZT"));
				newdy.put("YCFTJZMJ", olddy.get("YCFTJZMJ"));
				newdy.put("YCTNJZMJ", olddy.get("YCTNJZMJ"));
				newdy.put("SCFTJZMJ", olddy.get("SCFTJZMJ"));
				newdy.put("SCTNJZMJ", olddy.get("SCTNJZMJ"));
				newdy.put("SCJZMJ", olddy.get("SCJZMJ"));
			
				
				//权利信息
				List<Map> oldrightList=new ArrayList<Map>();
				List<Map> newrightList=new ArrayList<Map>();
			
				oldrightList=(List<Map>)oldm.get("QLXX");
				for(Map oldright:oldrightList)
				{
					Map newrightMap=new HashMap();
					Map newright=new HashMap();
					Map oldrightQL=(Map)oldright.get("QL");
					Map oldrightFSQL=(Map)oldright.get("FSQL");
					List<Map> oldrightHolderList=(List<Map>)oldright.get("QLR");
					List<Map> newrightHolderList=new ArrayList<Map>();
					newright.put("BDCQZH", oldrightQL.get("BDCQZH"));
					newright.put("YWH", oldrightQL.get("YWH"));
					newright.put("QLLX", oldrightQL.get("QLLX"));
					newright.put("QLLXMC", oldrightQL.get("QLLXMC"));
					newright.put("QLJSSJ", oldrightQL.get("QLJSSJ"));
					newright.put("QLQSSJ", oldrightQL.get("QLQSSJ"));
					newright.put("DBR", oldrightQL.get("DBR"));
					newright.put("DJYY", oldrightQL.get("DJYY"));
					newright.put("DJSJ", oldrightQL.get("DJSJ"));
					newright.put("DJLX", oldrightQL.get("DJLX"));
					newright.put("DJLXMC", oldrightQL.get("DJLXMC"));
					//如果是抵押权加入抵押权详细信息
					if(String.valueOf(oldrightQL.get("QLLX")).equals("23"))
					{
						Map dyxx=new HashMap();
						dyxx.put("DYR", oldrightFSQL.get("DYR"));
						dyxx.put("ZJJZWZL",oldrightFSQL.get("ZJJZWZL"));
						dyxx.put("DYFS", oldrightFSQL.get("DYFS"));
						dyxx.put("DYMJ", oldrightFSQL.get("DYMJ"));
						dyxx.put("BDBZZQSE",oldrightFSQL.get("BDBZZQSE"));
						dyxx.put("DYQX",oldrightQL.get("QLQSSJ")+" 至  "+oldrightQL.get("QLJSSJ"));
						newright.put("DYXX", dyxx);
					}
					//如果是查封的加入抵押权详细信息
					if(String.valueOf(oldrightQL.get("QLLX")).equals("99")&&String.valueOf(oldrightQL.get("DJLX")).equals("800"))
					{
						Map cfxx=new HashMap();
						cfxx.put("CFJG", oldrightFSQL.get("CFJG"));
						cfxx.put("CFWJ", oldrightFSQL.get("CFWJ"));
						cfxx.put("CFWH", oldrightFSQL.get("CFWH"));
						cfxx.put("CFFW",oldrightFSQL.get("CFFW"));
						cfxx.put("CFLX",oldrightFSQL.get("CFLX"));
						cfxx.put("LHSX",oldrightFSQL.get("LHSX"));
						cfxx.put("CFQX",oldrightFSQL.get("QLQSSJ")+" 至  "+oldrightFSQL.get("QLJSSJ"));
						newright.put("CFXX", cfxx);
					}
					for(Map oldrightHolder: oldrightHolderList){
						Map newrightHolder=new HashMap();
						newrightHolder.put("GYQK", oldrightHolder.get("GYQK"));
						newrightHolder.put("GYFS", oldrightHolder.get("GYFS"));
						newrightHolder.put("QLRMC", oldrightHolder.get("QLRMC"));
						newrightHolder.put("QLRLX", oldrightHolder.get("QLRLX"));
						newrightHolder.put("ZJH", oldrightHolder.get("ZJH"));
						newrightHolder.put("ZJZL", oldrightHolder.get("ZJZL"));
						newrightHolder.put("SXH", oldrightHolder.get("SXH"));
						newrightHolder.put("XB", oldrightHolder.get("XB"));
						newrightHolder.put("HJSZSS", oldrightHolder.get("HJSZSS"));
						newrightHolder.put("DH", oldrightHolder.get("DH"));
						newrightHolderList.add(newrightHolder);
					}
					newrightMap.put("QL", newright);
					newrightMap.put("QLR", newrightHolderList);
					newrightList.add(newrightMap);
				}
				newm.put("QLXX", newrightList);//权利信息
				newm.put("DY", newdy);//单元信息
				resultList.add(newm);
			}
		}
		success.setRows(resultList);
		success.setMsg("成功");
		success.setSuccess("true");
		return success;
		} catch (UnsupportedEncodingException e) {
			Message error=new Message();
			error.setMsg(e.getMessage());
			error.setSuccess("false");
			return error;
		}
	}
	
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/updatehth", method = RequestMethod.POST)
	public @ResponseBody Map updatehth(HttpServletRequest request, HttpServletResponse response,String houses) {
		return apiService.updatehth(houses);
	}
}
