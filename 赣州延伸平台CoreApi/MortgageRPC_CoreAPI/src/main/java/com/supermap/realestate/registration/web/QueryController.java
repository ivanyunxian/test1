package com.supermap.realestate.registration.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.supermap.realestate.registration.ViewClass.QueryClass.QLInfo;
import com.supermap.realestate.registration.ViewClass.QueryClass.QueryList;
import com.supermap.realestate.registration.model.BDCS_DYXZ;
import com.supermap.realestate.registration.model.BDCS_QLR_XZ;
import com.supermap.realestate.registration.model.BDCS_XZCF;
import com.supermap.realestate.registration.model.BDCS_XZDY;
import com.supermap.realestate.registration.service.QueryService;
import com.supermap.realestate.registration.service.QueryService2;
import com.supermap.realestate.registration.util.ConfigHelper;
import com.supermap.realestate.registration.util.ConstValue;
import com.supermap.realestate.registration.util.DateUtil;
import com.supermap.realestate.registration.util.Global;
import com.supermap.realestate.registration.util.RequestHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.realestate.registration.util.YwLogUtil;
import com.supermap.realestate_gx.registration.model.GX_CONFIG;
import com.supermap.realestate_gx.registration.util.WSDLUtil;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import com.supermap.wisdombusiness.web.Message;
import com.supermap.wisdombusiness.web.ResultMessage;
import com.supermap.realestate.registration.util.HttpRequest;

/**
 * 
 * 登记簿Controller 跟登记簿相关的都放在这里边
 * 
 * @author 俞学斌
 * @date 2015年8月25日 下午10:10:55
 * @Copyright SuperMap
 */
@Controller
@RequestMapping("/query")
@Component("QueryController")
public class QueryController {
	/** 查询service */
	@Autowired
	private QueryService queryService;
	/** 查询service2 */
	@Autowired
	private QueryService2 queryService2;
	@Autowired
	private QueryService queryServiceluzhou;
	
	@Autowired
	private CommonDao baseCommonDao;

	/**
	 * 
	 * @Description: 根据不动产单元ID和不动产单元类型获取不动产单元信息
	 * @date 2015年8月26日 上午1:24:01
	 * @author yuxuebin
	 * @param bdcdyid
	 *            不动产单元ID
	 * @param bdcdylx
	 *            不动产单元类型
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/hinfo/{bdcdyid}/{bdcdylx}", method = RequestMethod.GET)
	public @ResponseBody List<Map> GetHInfo(@PathVariable("bdcdyid") String bdcdyid, @PathVariable("bdcdylx") String bdcdylx, HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException {
		List<Map> list = queryService.GetHInfo(bdcdyid, bdcdylx);
		return list;
	}

	/**
	 * 
	 * @Description: 根据不动产单元ID获取现状所有权
	 * @date 2015年8月26日 上午1:24:01
	 * @author yuxuebin
	 * @param bdcdyid
	 *            不动产单元ID
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "/qlinfo_xz/{bdcdyid}", method = RequestMethod.GET)
	public @ResponseBody QLInfo GetQLInfo_XZ(@PathVariable("bdcdyid") String bdcdyid, HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		QLInfo ql = queryService.GetQLInfo_XZ(bdcdyid);
		return ql;
	}
	
	/**
	 * 通过权利ID获取权利信息
	 * @param qlid
	 * @return
	 */
	@RequestMapping(value="qlinfo/{qlid}",method=RequestMethod.GET)
	public @ResponseBody QLInfo GetQLinfo(@PathVariable String qlid){
		QLInfo ql = queryService.GetQLInfo(qlid);
		return ql;
	}
	@RequestMapping(value = "/qlinfo_xz1/{bdcdyid}", method = RequestMethod.GET)
	public @ResponseBody QLInfo GetQLInfo_XZ1(@PathVariable("bdcdyid") String bdcdyid, HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		QLInfo ql = queryService.GetQLInfo_XZ1(bdcdyid);
		return ql;
	}

	/**
	 * 
	 * @Description: 根据权利ID获取所有权信息
	 * @date 2015年8月26日 上午12:12:12
	 * @author yuxuebin
	 * @param qlid
	 *            权利ID
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "qlinfo/{qlid}/{djzt}", method = RequestMethod.GET)
	public @ResponseBody QLInfo GetSYQInfo(@PathVariable("qlid") String qlid,@PathVariable("djzt") String djzt, HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		QLInfo ql = queryService.GetQLInfo(qlid,djzt);
		return ql;
	}
	
	/**
	 * 
	 * @Description: 根据ID获取单元限制信息
	 * @date 2015年8月26日 上午12:12:12
	 * @author yuxuebin
	 * @param qlid
	 *            权利ID
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "qlinfo/{id}/dyxz", method = RequestMethod.GET)
	public @ResponseBody BDCS_DYXZ GetDYXZInfo(@PathVariable("id") String id, HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		BDCS_DYXZ ql = queryService.GetDYXZInfo(id);
		return ql;
	}

	/**
	 * 
	 * @Description: 根据不动产单元ID获取获取所有权集合、抵押权集合 、查封登记集合、异议登记集合
	 * @date 2015年8月25日 下午11:57:00
	 * @author yuxuebin
	 * @param bdcdyid
	 *            不动产单元ID
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "alllist/{bdcdyid}", method = RequestMethod.GET)
	public @ResponseBody Map GetAllList(@PathVariable("bdcdyid") String bdcdyid, HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		Map map = queryService.GetAllList(bdcdyid);
		return map;
	}

	/**
	 * 
	 * @Description: 根据不动产单元ID获取获取所有权集合、抵押权集合 、查封登记集合、异议登记集合
	 * @date 2015年8月25日 下午11:57:00
	 * @author yuxuebin
	 * @param bdcdyid
	 *            不动产单元ID
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "qllist/{bdcdyid}", method = RequestMethod.GET)
	public @ResponseBody List<List<Map>> GetQLList(@PathVariable("bdcdyid") String bdcdyid, HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		List<List<Map>> list = queryService.GetQLList(bdcdyid);
//		String ycscbdcdyid=queryService.GetScidandYcid(bdcdyid);
//		List<List<Map>> list = queryService.GetQLList(ycscbdcdyid);
		return list;
	}
	/**
	 * 根据宗地的不动产单元Id获取房屋信息
	 * @date2016/3/17 下午14:48:10
	 * @author author HKS
	 * @param bdcdyid
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "/ydyfwlist/{bdcdyid}", method = RequestMethod.GET)
	public @ResponseBody Message GetYdyFwList(@PathVariable("bdcdyid") String bdcdyid, HttpServletRequest request, HttpServletResponse response)throws UnsupportedEncodingException{
		int page = 1;
		if (request.getParameter("page") != null) {
			page = Integer.parseInt(request.getParameter("page"));
		}
		int rows = 10;
		if (request.getParameter("rows") != null) {
			rows = Integer.parseInt(request.getParameter("rows"));
		}

		return queryService.GetYdyFwInfo(bdcdyid, page, rows);
	}
	
	@RequestMapping(value = "/getfwinfobyzrz/{zrzbdcdyid}", method = RequestMethod.GET)
	public @ResponseBody Message GetFwInfoByZrz(@PathVariable("zrzbdcdyid") String zrzbdcdyid, HttpServletRequest request, HttpServletResponse response)throws UnsupportedEncodingException{
		int page = 1;
		if (request.getParameter("page") != null) {
			page = Integer.parseInt(request.getParameter("page"));
		}
		int rows = 10;
		if (request.getParameter("rows") != null) {
			rows = Integer.parseInt(request.getParameter("rows"));
		}

		return queryService.GetFwInfoByZrz(zrzbdcdyid, page, rows);
	}
	/**根据宗地不动产单元Id获取自然幢信息
	 * @param bdcdyid
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "/zrzlist/{zdbdcdyid}", method = RequestMethod.POST)
	public @ResponseBody Message GetZrzList(@PathVariable("zdbdcdyid") String bdcdyid, HttpServletRequest request, HttpServletResponse response)throws UnsupportedEncodingException{
		int page = 1;
		if (request.getParameter("page") != null) {
			page = Integer.parseInt(request.getParameter("page"));
		}
		int rows = 10;
		if (request.getParameter("rows") != null) {
			rows = Integer.parseInt(request.getParameter("rows"));
		}
		Map<String, String> queryvalues = new HashMap<String, String>();
	
		String type = RequestHelper.getParam(request, "TYPE");// 抵押状态
		String zl=RequestHelper.getParam(request,"ZL");
		String bdcdyh=RequestHelper.getParam(request, "BDCDYH");
		if(StringUtils.isEmpty(type))
		 type ="0";
		queryvalues.put("ZL", zl);
		queryvalues.put("BDCDYH", bdcdyh);
		
		return queryService.GetZrzList(bdcdyid,type,queryvalues, page, rows);
	}
	
	/**根据自然幢不动产单元Id获取自然幢信息
	 * @param bdcdyid
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "/zrzXx/{zdbdcdyid}", method = RequestMethod.POST)
	public @ResponseBody Message GetZrzXx(@PathVariable("zdbdcdyid") String bdcdyid, HttpServletRequest request, HttpServletResponse response)throws UnsupportedEncodingException{
		List<Map> ZrzList = new ArrayList<Map>();
		int page = 1;
		if (request.getParameter("page") != null) {
			page = Integer.parseInt(request.getParameter("page"));
		}
		int rows = 10;
		if (request.getParameter("rows") != null) {
			rows = Integer.parseInt(request.getParameter("rows"));
		}
		Map<String, String> queryvalues = new HashMap<String, String>();
	
		String type = RequestHelper.getParam(request, "TYPE");// 抵押状态
		/*String jzmj=RequestHelper.getParam(request,"JZMJ");
		String zzdmj=RequestHelper.getParam(request, "ZZDMJ");*/
		if(StringUtils.isEmpty(type))
		 type ="0";
		/*queryvalues.put("SCJZMJ", jzmj);
		queryvalues.put("ZZDMJ", zzdmj);*/
		
		return queryService.GetZrzXx(bdcdyid,type,queryvalues, page, rows);
	}

	/**
	 * 房屋查询
	 * @Title: GetHouseQueryList
	 * @author:liushufeng
	 * @date：2015年8月26日 下午5:38:52
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "/houseQuery", method = RequestMethod.POST)
	public @ResponseBody Message GetHouseQueryList(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
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
		String dyzt = RequestHelper.getParam(request, "DYZT");// 抵押状态
		String cfzt = RequestHelper.getParam(request, "CFZT");// 查封状态
		String cxzt = RequestHelper.getParam(request, "CXZT");// 查询状态
		String yyzt = RequestHelper.getParam(request, "YYZT");// 异议状态
		String fwzt=RequestHelper.getParam(request,"FWZT");
		String bdcdyh=RequestHelper.getParam(request, "BDCDYH");
		String bdcqzhxh=RequestHelper.getParam(request, "BDCQZHXH");
		String cfwh=RequestHelper.getParam(request, "CFWH");
		String djsj_q=RequestHelper.getParam(request, "DJSJ_Q");
		String djsj_z=RequestHelper.getParam(request, "DJSJ_Z");
		String dh=RequestHelper.getParam(request, "DH");//栋号
		String jgsx=RequestHelper.getParam(request, "JGSX");//结果筛选；"1"：全部显示，"2"：仅显示产权
		String searchstates=RequestHelper.getParam(request, "SEARCHSTATES");//区域筛选
		String res=getfwlogmsg(qlrmc,dyr,qlrzjh,fwzl,bdcqzh,fwbm,fh,ywbh,dyzt,cfzt,cxzt,fwzt,bdcdyh,bdcqzhxh,cfwh,querytype,"","");
		YwLogUtil.addYwLog("房屋查询功能："+res, ConstValue.SF.YES.Value,ConstValue.LOG.SEARCH);
		queryvalues.put("DY.ZL", fwzl);
		queryvalues.put("DY.BDCDYH", bdcdyh);
		queryvalues.put("QLR.QLRMC", qlrmc);
		queryvalues.put("DYR.DYR", dyr);
		queryvalues.put("QLR.ZJH", qlrzjh);
		queryvalues.put("QL.BDCQZH", bdcqzh);
		queryvalues.put("QLR.BDCQZHXH", bdcqzhxh);
		queryvalues.put("FSQL.CFWH", cfwh);
		queryvalues.put("DY.FWBM", fwbm);
		queryvalues.put("DY.FH", fh);
		queryvalues.put("QL.YWH", ywbh);
		queryvalues.put("DYZT", dyzt);
		queryvalues.put("CFZT", cfzt);
		queryvalues.put("CXZT", cxzt);
		queryvalues.put("DJSJ_Q", djsj_q);
		queryvalues.put("DJSJ_Z", djsj_z);
		queryvalues.put("DY.ZRZH", dh);//栋号(自然幢号)
		queryvalues.put("YYZT", yyzt);
		queryvalues.put("JGSX", jgsx);//结果筛选；"1"：全部显示，"2"：仅显示产权
		queryvalues.put("SEARCHSTATES", searchstates);//区域筛选
		queryvalues.put("SWCDBZ", "cd");
		return queryService.queryHouse(queryvalues, page, rows, iflike,fwzt,sort,order);
	}
	
	/**
	 * 房屋查询鹰潭
	 * @Title: GetHouseQueryList
	 * @author:liushufeng
	 * @date：2015年8月26日 下午5:38:52
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "/houseQueryyt", method = RequestMethod.GET)
	public @ResponseBody Message GetHouseQueryListyt(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
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
		String dyzt = RequestHelper.getParam(request, "DYZT");// 抵押状态
		String cfzt = RequestHelper.getParam(request, "CFZT");// 查封状态
		String cxzt = RequestHelper.getParam(request, "CXZT");// 查询状态
		String yyzt = RequestHelper.getParam(request, "YYZT");// 异议状态
		String fwzt=RequestHelper.getParam(request,"FWZT");
		String bdcdyh=RequestHelper.getParam(request, "BDCDYH");
		String bdcqzhxh=RequestHelper.getParam(request, "BDCQZHXH");
		String cfwh=RequestHelper.getParam(request, "CFWH");
		String res=getfwlogmsg(qlrmc,dyr,qlrzjh,fwzl,bdcqzh,fwbm,fh,ywbh,dyzt,cfzt,cxzt,fwzt,bdcdyh,bdcqzhxh,cfwh,querytype,"","");
		YwLogUtil.addYwLog("房屋查询功能："+res, ConstValue.SF.YES.Value,ConstValue.LOG.SEARCH);
		queryvalues.put("DY.ZL", fwzl);
		queryvalues.put("DY.BDCDYH", bdcdyh);
		queryvalues.put("QLR.QLRMC", qlrmc);	
		queryvalues.put("DYR.DYR", dyr);
		queryvalues.put("QLR.ZJH", qlrzjh);
		queryvalues.put("QL.BDCQZH", bdcqzh);
		queryvalues.put("QLR.BDCQZHXH", bdcqzhxh);
		queryvalues.put("FSQL.CFWH", cfwh);
		queryvalues.put("DY.FWBM", fwbm);
		queryvalues.put("DY.FH", fh);
		queryvalues.put("QL.YWH", ywbh);
		queryvalues.put("DYZT", dyzt);
		queryvalues.put("CFZT", cfzt);
		queryvalues.put("CXZT", cxzt);
		queryvalues.put("YYZT", yyzt);
		//YwLogUtil.addYwLog("房屋查询功能：查询", ConstValue.SF.YES.Value,ConstValue.LOG.SEARCH);
		//-------------获取宗地单元房屋统计详情数据
		String fw_xzqh = RequestHelper.getParam(request, "FW_XZQH");
		String fw_qlrlx = RequestHelper.getParam(request, "FW_QLRLX");
		String fw_fwyt = RequestHelper.getParam(request, "FW_FWYT");
		String fw_fwlx = RequestHelper.getParam(request, "FW_FWLX");
		String fw_fwxz = RequestHelper.getParam(request, "FW_FWXZ");
		String fw_hx = RequestHelper.getParam(request, "FW_HX");
		String fw_hxjg = RequestHelper.getParam(request, "FW_HXJG");
		String fw_fwjg = RequestHelper.getParam(request, "FW_FWJG");
		String fw_fwcb = RequestHelper.getParam(request, "FW_FWCB");
		String fw_fzdy = RequestHelper.getParam(request, "FW_FZDY");
		String fzlb = RequestHelper.getParam(request, "FW_FZLB");
	
	
		String zddy = RequestHelper.getParam(request, "ZDDY");

		return queryService.queryHouseyt(queryvalues, page, rows, iflike,fwzt,sort,order,zddy,fw_xzqh,fw_qlrlx,fw_fwyt,fw_fwlx,
				fw_fwxz,fw_hx,fw_hxjg,fw_fwjg,fw_fwcb,fw_fzdy,fzlb);
	}
	
	@SuppressWarnings({ "deprecation", "rawtypes", "unchecked" })
	@RequestMapping(value = "/houseQueryDownload", method = RequestMethod.GET)
	public   @ResponseBody String HouseQueryListDownload(HttpServletRequest request, HttpServletResponse response) throws Exception{
		String querytype = request.getParameter("QUERYTYPE");
		boolean iflike = false;
		if (querytype != null && querytype.toLowerCase().equals("true")) {
			iflike = true;
		}

		Map<String, String> queryvalues = new HashMap<String, String>();
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
		String dyzt = RequestHelper.getParam(request, "DYZT");// 抵押状态
		String cfzt = RequestHelper.getParam(request, "CFZT");// 查封状态
		String cxzt = RequestHelper.getParam(request, "CXZT");// 查询状态
		String fwzt=RequestHelper.getParam(request,"FWZT");
		String bdcdyh=RequestHelper.getParam(request, "BDCDYH");
		String bdcqzhxh=RequestHelper.getParam(request, "BDCQZHXH");
		String cfwh=RequestHelper.getParam(request, "CFWH");
		String djsj_q=RequestHelper.getParam(request, "DJSJ_Q");
		String djsj_z=RequestHelper.getParam(request, "DJSJ_Z");
		String dh=RequestHelper.getParam(request, "DH");//栋号
		String jgsx=RequestHelper.getParam(request, "JGSX");//结果筛选；"1"：全部显示，"2"：仅显示产权
		String searchstates=RequestHelper.getParam(request, "SEARCHSTATES");//区域筛选
		String res=getfwlogmsg(qlrmc,dyr,qlrzjh,fwzl,bdcqzh,fwbm,fh,ywbh,dyzt,cfzt,cxzt,fwzt,bdcdyh,bdcqzhxh,cfwh,querytype,"","");
		YwLogUtil.addYwLog("房屋查询结果导出功能："+res, ConstValue.SF.YES.Value,ConstValue.LOG.PRINT);
		queryvalues.put("DY.ZL", fwzl);
		queryvalues.put("DY.BDCDYH", bdcdyh);
		queryvalues.put("QLR.QLRMC", qlrmc);
		queryvalues.put("DYR.DYR", dyr);
		queryvalues.put("QLR.ZJH", qlrzjh);
		queryvalues.put("QL.BDCQZH", bdcqzh);
		queryvalues.put("QLR.BDCQZHXH", bdcqzhxh);
		queryvalues.put("FSQL.CFWH", cfwh);
		queryvalues.put("DY.FWBM", fwbm);
		queryvalues.put("DY.FH", fh);
		queryvalues.put("QL.YWH", ywbh);
		queryvalues.put("DYZT", dyzt);
		queryvalues.put("CFZT", cfzt);
		queryvalues.put("CXZT", cxzt);
		queryvalues.put("DJSJ_Q", djsj_q);
		queryvalues.put("DJSJ_Z", djsj_z);
		queryvalues.put("DY.ZRZH", dh);//栋号(自然幢号)
		queryvalues.put("JGSX", jgsx);//结果筛选；"1"：全部显示，"2"：仅显示产权
		queryvalues.put("SEARCHSTATES", searchstates);//区域筛选
		//YwLogUtil.addYwLog("房屋查询功能：查询", ConstValue.SF.YES.Value,ConstValue.LOG.SEARCH);
		Message message= queryService.queryHouse(queryvalues, 1, Integer.MAX_VALUE, iflike,fwzt,sort,order);
		List<Map> rows=(List<Map>) message.getRows();
		
		//导出
		String basePath = request.getRealPath("/") + "\\resources\\PDF";
		String outpath = "";
		String url = "";
		FileOutputStream outstream = null;
		outpath = basePath + "\\tmp\\housequeryresult.xls";
		url = request.getContextPath() + "\\resources\\PDF\\tmp\\housequeryresult.xls";
	    outstream = new FileOutputStream(outpath); 

	    String tplFullName = request.getRealPath("/WEB-INF/jsp/wjmb/house1.xls");
	    InputStream input = new FileInputStream(tplFullName);
		HSSFWorkbook  wb = null;// 定义一个新的工作簿
		wb = new HSSFWorkbook(input);
		HSSFSheet sheet = wb.getSheetAt(0);
		Map<String,Integer> MapCol = new HashMap<String,Integer>();
		
		//所选字段
		String selectFields=RequestHelper.getParam(request, "selectFields");
		JSONArray jsonArray = JSONArray.fromObject(selectFields);
		List<Map> mapListJson = (List) jsonArray;
		//获取标题行格式
		int lastrow = sheet.getLastRowNum();
		HSSFRow head = sheet.getRow(lastrow - 1);
		HSSFCellStyle style = sheet.getRow(lastrow).getCell(0).getCellStyle();
		short height = sheet.getRow(lastrow).getHeight();
		//添加标题行
		HSSFRow title = (HSSFRow)sheet.createRow(1);
		title.setHeight(height);
		//序号
		HSSFCell Cell = title.createCell(0);
		 Cell.setCellValue("序号") ;
		 Cell.setCellStyle(style);
		 MapCol.put("序号", 0);
		//content
		for (int i = 0; i < mapListJson.size(); i++) {
			 HSSFCell title_Cell = title.createCell(i+1);
			 title_Cell.setCellValue(mapListJson.get(i).get("name").toString()) ;
			 MapCol.put(mapListJson.get(i).get("name").toString(), i+1);
			 title_Cell.setCellStyle(style);
		}
		//添加内容行
		int rownum = 2;
		for(Map r:rows){
			 HSSFRow row = (HSSFRow)sheet.createRow(rownum);
	         HSSFCell Cell0 = row.createCell(MapCol.get("序号"));
	         Cell0.setCellValue(rownum-1);
	         for (int i = 0; i < mapListJson.size(); i++) {
				 String name = mapListJson.get(i).get("name").toString();
				 String value = mapListJson.get(i).get("value").toString();
	        	 HSSFCell Content_Cell = row.createCell(MapCol.get(name));
	        	 Content_Cell.setCellValue(StringHelper.FormatByDatatype(r.get(value)));
			}
	        rownum += 1;
	  }
	
	    wb.write(outstream); 
	    outstream.flush(); 
	    outstream.close();
	    outstream = null;
	    return url;
	}
	
	
	@SuppressWarnings({ "deprecation", "rawtypes", "unchecked" })
	@RequestMapping(value = "/houseQueryDownloadByPage", method = RequestMethod.GET)
	public   @ResponseBody String houseQueryDownloadByPage(HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		Integer _page = 1;
		if (request.getParameter("page") != null) {
			_page = Integer.parseInt(request.getParameter("page"));
		}
		Integer _rows = 10;
		if (request.getParameter("rows") != null) {
			_rows = Integer.parseInt(request.getParameter("rows"));
		}
		
		String querytype = request.getParameter("QUERYTYPE");
		boolean iflike = false;
		if (querytype != null && querytype.toLowerCase().equals("true")) {
			iflike = true;
		}

		Map<String, String> queryvalues = new HashMap<String, String>();
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
		String dyzt = RequestHelper.getParam(request, "DYZT");// 抵押状态
		String cfzt = RequestHelper.getParam(request, "CFZT");// 查封状态
		String cxzt = RequestHelper.getParam(request, "CXZT");// 查询状态
		String fwzt=RequestHelper.getParam(request,"FWZT");
		String bdcdyh=RequestHelper.getParam(request, "BDCDYH");
		String bdcqzhxh=RequestHelper.getParam(request, "BDCQZHXH");
		String cfwh=RequestHelper.getParam(request, "CFWH");
		String djsj_q=RequestHelper.getParam(request, "DJSJ_Q");
		String djsj_z=RequestHelper.getParam(request, "DJSJ_Z");
		String dh=RequestHelper.getParam(request, "DH");//栋号
		String jgsx=RequestHelper.getParam(request, "JGSX");//结果筛选；"1"：全部显示，"2"：仅显示产权
		String searchstates=RequestHelper.getParam(request, "SEARCHSTATES");//区域筛选
		String res=getfwlogmsg(qlrmc,dyr,qlrzjh,fwzl,bdcqzh,fwbm,fh,ywbh,dyzt,cfzt,cxzt,fwzt,bdcdyh,bdcqzhxh,cfwh,querytype,"","");
		YwLogUtil.addYwLog("房屋查询结果导出功能："+res, ConstValue.SF.YES.Value,ConstValue.LOG.PRINT);
		queryvalues.put("DY.ZL", fwzl);
		queryvalues.put("DY.BDCDYH", bdcdyh);
		queryvalues.put("QLR.QLRMC", qlrmc);
		queryvalues.put("DYR.DYR", dyr);
		queryvalues.put("QLR.ZJH", qlrzjh);
		queryvalues.put("QL.BDCQZH", bdcqzh);
		queryvalues.put("QLR.BDCQZHXH", bdcqzhxh);
		queryvalues.put("FSQL.CFWH", cfwh);
		queryvalues.put("DY.FWBM", fwbm);
		queryvalues.put("DY.FH", fh);
		queryvalues.put("QL.YWH", ywbh);
		queryvalues.put("DYZT", dyzt);
		queryvalues.put("CFZT", cfzt);
		queryvalues.put("CXZT", cxzt);
		queryvalues.put("DJSJ_Q", djsj_q);
		queryvalues.put("DJSJ_Z", djsj_z);
		queryvalues.put("DY.ZRZH", dh);//栋号(自然幢号)
		queryvalues.put("JGSX", jgsx);//结果筛选；"1"：全部显示，"2"：仅显示产权
		queryvalues.put("SEARCHSTATES", searchstates);//区域筛选
		//YwLogUtil.addYwLog("房屋查询功能：查询", ConstValue.SF.YES.Value,ConstValue.LOG.SEARCH);
		Message message= queryService.queryHouse(queryvalues, _page, _rows, iflike,fwzt,sort,order);
		List<Map> rows=(List<Map>) message.getRows();

		//导出
		String basePath = request.getRealPath("/") + "\\resources\\PDF";
		String outpath = "";
		String url = "";
		FileOutputStream outstream = null;
		outpath = basePath + "\\tmp\\housequeryresult.xls";
		url = request.getContextPath() + "\\resources\\PDF\\tmp\\housequeryresult.xls";
	    outstream = new FileOutputStream(outpath); 

	    String tplFullName = request.getRealPath("/WEB-INF/jsp/wjmb/house1.xls");
	    InputStream input = new FileInputStream(tplFullName);
		HSSFWorkbook  wb = null;// 定义一个新的工作簿
		wb = new HSSFWorkbook(input);
		HSSFSheet sheet = wb.getSheetAt(0);
		Map<String,Integer> MapCol = new HashMap<String,Integer>();
		
		//所选字段
		String selectFields=RequestHelper.getParam(request, "selectFields");
		JSONArray jsonArray = JSONArray.fromObject(selectFields);
		List<Map> mapListJson = (List) jsonArray;
		//获取标题行格式
		int lastrow = sheet.getLastRowNum();
		HSSFRow head = sheet.getRow(lastrow - 1);
		HSSFCellStyle style = sheet.getRow(lastrow).getCell(0).getCellStyle();
		short height = sheet.getRow(lastrow).getHeight();
		//添加标题行
		HSSFRow title = (HSSFRow)sheet.createRow(1);
		title.setHeight(height);
		//序号
		HSSFCell Cell = title.createCell(0);
		 Cell.setCellValue("序号") ;
		 Cell.setCellStyle(style);
		 MapCol.put("序号", 0);
		//content
		for (int i = 0; i < mapListJson.size(); i++) {
			 HSSFCell title_Cell = title.createCell(i+1);
			 title_Cell.setCellValue(mapListJson.get(i).get("name").toString()) ;
			 MapCol.put(mapListJson.get(i).get("name").toString(), i+1);
			 title_Cell.setCellStyle(style);
		}
		//添加内容行
		int rownum = 2;
		for(Map r:rows){
			 HSSFRow row = (HSSFRow)sheet.createRow(rownum);
	         HSSFCell Cell0 = row.createCell(MapCol.get("序号"));
	         Cell0.setCellValue(rownum-1);
	         for (int i = 0; i < mapListJson.size(); i++) {
				 String name = mapListJson.get(i).get("name").toString();
				 String value = mapListJson.get(i).get("value").toString();
	        	 HSSFCell Content_Cell = row.createCell(MapCol.get(name));
	        	 Content_Cell.setCellValue(StringHelper.FormatByDatatype(r.get(value)));
								
			}
	        rownum += 1;
	  }
	
	    
	    wb.write(outstream); 
	    outstream.flush(); 
	    outstream.close();
	    outstream = null;
	    return url;
	}
	
	/**
	 * 鹰潭短信查询	 
	 */
	@RequestMapping(value = "/messageQuery", method = RequestMethod.GET)
	public @ResponseBody Message GetmessageList(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
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
		//String sort = RequestHelper.getParam(request, "sort");// 排序字段
		//String order = RequestHelper.getParam(request, "order");// 排序Order
		String SQRMC = RequestHelper.getParam(request, "SQRMC");// 申请人名称
		String DHHM = RequestHelper.getParam(request, "DHHM");// 电话号码
		String XZQH = RequestHelper.getParam(request, "XZQH");// 行政区划
		String FSZT = RequestHelper.getParam(request, "FSZT");// 发送状态
		String FSKSSJ = RequestHelper.getParam(request, "FSKSSJ");// 发送开始时间	
		String FSJSSJ = RequestHelper.getParam(request, "FSJSSJ");//发送结束时间		
		String FSSJ = RequestHelper.getParam(request, "FSSJ");//发送时间	
		//String FSSJ = RequestHelper.getParam(request, "FSSJ");
		if(XZQH==null){
			XZQH = "0";
		}
		queryvalues.put("FSZT", FSZT);
		queryvalues.put("FSKSSJ", FSKSSJ);
		queryvalues.put("FSJSSJ", FSJSSJ);
		queryvalues.put("SQRMC", SQRMC);
		queryvalues.put("DHHM", DHHM);
		queryvalues.put("XZQH", XZQH);
		queryvalues.put("FSSJ", FSSJ);
		
		return queryService.queryMessage(queryvalues, page, rows, iflike);
	}
	
	/**
	 * 土地查询
	 * @Title: GetLandQueryList
	 * @author:mss
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "/landQuery", method = RequestMethod.GET)
	public @ResponseBody Message GetLandQueryList(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
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
		String sort = RequestHelper.getParam(request, "sort");// 排序字段
		String order = RequestHelper.getParam(request, "order");// 排序Order
		String qlrmc = RequestHelper.getParam(request, "QLRMC");// 权利人名称
		String qlrzjh = RequestHelper.getParam(request, "ZJH");// 权利人证件号
		String tdzl = RequestHelper.getParam(request, "ZL");// 土地坐落
		String bdcqzh = RequestHelper.getParam(request, "BDCQZH");// 不动产权证号
		String ywbh = RequestHelper.getParam(request, "YWBH");// 业务编号
		String dyzt = RequestHelper.getParam(request, "DYZT");// 抵押状态
		String cfzt = RequestHelper.getParam(request, "CFZT");// 查封状态
		String cxzt = RequestHelper.getParam(request, "CXZT");// 查询状态
		String fwzt=RequestHelper.getParam(request,"TDZT");
		String bdcdyh=RequestHelper.getParam(request, "BDCDYH");
		String zddm=RequestHelper.getParam(request, "ZDDM");
		String cfwh=RequestHelper.getParam(request, "CFWH");
		String djsj_q=RequestHelper.getParam(request, "DJSJ_Q");
		String djsj_z=RequestHelper.getParam(request, "DJSJ_Z");
		String djh = RequestHelper.getParam(request, "DJH");
		String qlxz = RequestHelper.getParam(request, "QLXZ");
		String tdyt = RequestHelper.getParam(request, "TDYT");
		String fdzk = RequestHelper.getParam(request, "FDZK");
		String jgsx=RequestHelper.getParam(request, "JGSX");//结果筛选；"1"：全部显示，"2"：仅显示产权
		queryvalues.put("DY.ZL", tdzl);
		queryvalues.put("DY.BDCDYH", bdcdyh);
		queryvalues.put("DY.ZDDM", zddm);
		queryvalues.put("QLR.QLRMC", qlrmc);
		queryvalues.put("QLR.ZJH", qlrzjh);
		queryvalues.put("QL.BDCQZH", bdcqzh);
		queryvalues.put("QL.YWH", ywbh);
		queryvalues.put("FSQL.CFWH", cfwh);
		queryvalues.put("DYZT", dyzt);
		queryvalues.put("CFZT", cfzt);
		queryvalues.put("DJSJ_Q", djsj_q);
		queryvalues.put("DJSJ_Z", djsj_z);
		queryvalues.put("DJH", djh);
		queryvalues.put("CXZT", cxzt);
		queryvalues.put("QLXZ", qlxz);
		queryvalues.put("TDYT", tdyt);
		queryvalues.put("FDZK", fdzk);
		queryvalues.put("JGSX", jgsx);//结果筛选；"1"：全部显示，"2"：仅显示产权
		YwLogUtil.addYwLog("土地查询功能，查询", ConstValue.SF.YES.Value,ConstValue.LOG.SEARCH);
		return queryService.queryLand(queryvalues, page, rows, iflike,fwzt,sort,order);
	}
	
	/**
	 * 自然幢查询
	 * @Title: GetLandQueryList
	 * @author:mss
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "/buildingQuery", method = RequestMethod.GET)
	public @ResponseBody Message GetbuildingQueryList(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
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
		String zl = RequestHelper.getParam(request, "ZL");// 坐落
		String bdcdyh=RequestHelper.getParam(request, "BDCDYH"); //不动产单元号
		String lpzt = RequestHelper.getParam(request, "LPZT");// 楼盘状态
		String xmmc=RequestHelper.getParam(request, "XMMC"); //项目名称
		String kfsmc=RequestHelper.getParam(request, "KFSMC"); //开发商名称
		Message msg = queryService.querybuilding(queryvalues, page, zl, bdcdyh, lpzt, xmmc, rows, iflike,kfsmc);
		return msg;
	}
	
	/**
	 * 幢关联房屋
	 * @Title: GetLandQueryList
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "/fwlist/{zrzbdcdyid}", method = RequestMethod.GET)
	public @ResponseBody Message Getfwlist(@PathVariable("zrzbdcdyid") String bdcdyid,HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		Integer page = 1;
		if (request.getParameter("page") != null) {
			page = Integer.parseInt(request.getParameter("page"));
		}
		Integer rows = 10;
		if (request.getParameter("rows") != null) {
			rows = Integer.parseInt(request.getParameter("rows"));
		}
		Map<String, String> queryvalues = new HashMap<String, String>();
		String sort = RequestHelper.getParam(request, "sort");// 排序字段
		queryvalues.put("sort", sort);
		Message msg = queryService.queryfwlist(bdcdyid,queryvalues, page,rows);
		return msg;
	}
	
	
	/** 自然幢查询导出
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@SuppressWarnings({ "rawtypes", "deprecation", "unchecked" })
	@RequestMapping(value = "/buildingQueryDownload", method = RequestMethod.GET)
	public @ResponseBody String GetBuildingQueryListDownload(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Integer page = 1;
		if (request.getParameter("page") != null) {
			page = Integer.parseInt(request.getParameter("page"));
		}
		String querytype = request.getParameter("QUERYTYPE");
		boolean iflike = false;
		if (querytype != null && querytype.toLowerCase().equals("true")) {
			iflike = true;
		}
		Map<String, String> queryvalues = new HashMap<String, String>();
		String zl = RequestHelper.getParam(request, "ZL");// 坐落
		String bdcdyh=RequestHelper.getParam(request, "BDCDYH"); //不动产单元号
		String lpzt = RequestHelper.getParam(request, "LPZT");// 楼盘状态
		String xmmc=RequestHelper.getParam(request, "XMMC"); //项目名称
		String kfsmc = RequestHelper.getParam(request, "KFSMC"); //开发商名称
		Message msg = queryService.querybuilding(queryvalues, page, zl, bdcdyh, lpzt, xmmc, Integer.MAX_VALUE, iflike,kfsmc);
		
		List<Map> rows=(List<Map>) msg.getRows();
		String basePath = request.getRealPath("/") + "\\resources\\PDF";
		String outpath = "";
		String url = "";
		FileOutputStream outstream = null;
		outpath = basePath + "\\tmp\\buildingqueryresult.xls";
		url = request.getContextPath() + "\\resources\\PDF\\tmp\\buildingqueryresult.xls";
	    outstream = new FileOutputStream(outpath); 

	    String tplFullName = request.getRealPath("/WEB-INF/jsp/wjmb/building.xls");
		    InputStream input = new FileInputStream(tplFullName);
			HSSFWorkbook  wb = null;// 定义一个新的工作簿
			wb = new HSSFWorkbook(input);
			Sheet sheet = wb.getSheetAt(0);
			Map<String,Integer> MapCol = new HashMap<String,Integer>();
			
			MapCol.put("序号", 0);
			MapCol.put("自然幢坐落", 1);
			MapCol.put("不动产单元号", 2);
			MapCol.put("自然幢号", 3);
			MapCol.put("幢占地面积", 4);
			MapCol.put("地上层数", 5);
			MapCol.put("地下层数", 6);
			MapCol.put("是否办理首次登记", 7);
			MapCol.put("是否在建工程抵押", 8);
			MapCol.put("是否关联期现房", 9);
			MapCol.put("宗地是否查封", 10);
			MapCol.put("宗地是否抵押", 11);
			MapCol.put("土地证号", 12);
			MapCol.put("土地权利人", 13);
            int rownum = 2;
			for(Map r:rows){
						 HSSFRow row = (HSSFRow)sheet.createRow(rownum);
				         HSSFCell Cell0 = row.createCell(MapCol.get("序号"));
				         Cell0.setCellValue(rownum-1);
				         HSSFCell Cell1 = row.createCell(MapCol.get("自然幢坐落"));
				         Cell1.setCellValue(StringHelper.FormatByDatatype(r.get("ZL")));
				         HSSFCell Cell2 = row.createCell(MapCol.get("不动产单元号"));
				         Cell2.setCellValue(StringHelper.FormatByDatatype(r.get("BDCDYH")));
				         HSSFCell Cell3 = row.createCell(MapCol.get("自然幢号"));
				         Cell3.setCellValue(StringHelper.FormatByDatatype(r.get("ZRZH")));
				         HSSFCell Cell5 = row.createCell(MapCol.get("幢占地面积"));
				         Cell5.setCellValue(StringHelper.FormatByDatatype(r.get("ZZDMJ")));
				         HSSFCell Cell6 = row.createCell(MapCol.get("地上层数"));
				         Cell6.setCellValue(StringHelper.FormatByDatatype(r.get("DSCS")));
				         HSSFCell Cell7 = row.createCell(MapCol.get("地下层数"));
				         Cell7.setCellValue(StringHelper.FormatByDatatype(r.get("DXCS")));
				         HSSFCell Cell12 = row.createCell(MapCol.get("是否办理首次登记"));
				         Cell12.setCellValue(StringHelper.FormatByDatatype(r.get("SFSC")));
				         HSSFCell Cell13 = row.createCell(MapCol.get("是否在建工程抵押"));
				         Cell13.setCellValue(StringHelper.FormatByDatatype(r.get("SFZJDY")));
				         HSSFCell Cell14 = row.createCell(MapCol.get("是否关联期现房"));
				         Cell14.setCellValue(StringHelper.FormatByDatatype(r.get("SFGLQZF")));
				         HSSFCell Cell15 = row.createCell(MapCol.get("宗地是否查封"));
				         Cell15.setCellValue(StringHelper.FormatByDatatype(r.get("SFZDCF")));
				         HSSFCell Cell16 = row.createCell(MapCol.get("宗地是否抵押"));
				         Cell16.setCellValue(StringHelper.FormatByDatatype(r.get("SFZDDY")));
				         HSSFCell Cell17 = row.createCell(MapCol.get("土地证号"));
				         Cell17.setCellValue(StringHelper.FormatByDatatype(r.get("TDZH")));
				         HSSFCell Cell18 = row.createCell(MapCol.get("土地权利人"));
				         Cell18.setCellValue(StringHelper.FormatByDatatype(r.get("TDQLR")));
				         rownum += 1;
				  }
			    wb.write(outstream); 
			    outstream.flush(); 
			    outstream.close();
			    outstream = null;
			    return url;
	}

	
	@SuppressWarnings({ "rawtypes", "deprecation", "unchecked" })
	@RequestMapping(value = "/landQueryDownload", method = RequestMethod.GET)
	public @ResponseBody String GetLandQueryListDownload(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String querytype = request.getParameter("QUERYTYPE");
		boolean iflike = false;
		if (querytype != null && querytype.toLowerCase().equals("true")) {
			iflike = true;
		}

		Map<String, String> queryvalues = new HashMap<String, String>();
		String sort = RequestHelper.getParam(request, "sort");// 排序字段
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
		String djsj_q=RequestHelper.getParam(request, "DJSJ_Q");
		String djsj_z=RequestHelper.getParam(request, "DJSJ_Z");
		String cfwh=RequestHelper.getParam(request, "CFWH");
		String qlxz = RequestHelper.getParam(request, "QLXZ");
		String tdyt = RequestHelper.getParam(request, "TDYT");
		String fdzk = RequestHelper.getParam(request, "FDZK");
		String tdzt=RequestHelper.getParam(request,"TDZT");
		String res=getfwlogmsg(qlrmc,"",qlrzjh,tdzl,bdcqzh,"","",ywbh,dyzt,cfzt,"","",bdcdyh,"",cfwh,querytype,zddm,tdzt);
		YwLogUtil.addYwLog("土地查询功能:"+res, ConstValue.SF.YES.Value,ConstValue.LOG.PRINT);
		queryvalues.put("DY.ZL", tdzl);
		queryvalues.put("DY.BDCDYH", bdcdyh);
		queryvalues.put("DY.ZDDM", zddm);
		queryvalues.put("QLR.QLRMC", qlrmc);
		queryvalues.put("QLR.ZJH", qlrzjh);
		queryvalues.put("QL.BDCQZH", bdcqzh);
		queryvalues.put("QL.YWH", ywbh);
		queryvalues.put("DYZT", dyzt);
		queryvalues.put("CFZT", cfzt);
		queryvalues.put("DJSJ_Q", djsj_q);
		queryvalues.put("DJSJ_Z", djsj_z);
		queryvalues.put("QLXZ", qlxz);
		queryvalues.put("TDYT", tdyt);
		queryvalues.put("FDZK", fdzk);
		Message message =queryService.queryLand(queryvalues, 1, Integer.MAX_VALUE, iflike,fwzt,sort,order);
		List<Map> rows=(List<Map>) message.getRows();

		String basePath = request.getRealPath("/") + "\\resources\\PDF";
		String outpath = "";
		String url = "";
		FileOutputStream outstream = null;
		outpath = basePath + "\\tmp\\landqueryresult.xls";
		url = request.getContextPath() + "\\resources\\PDF\\tmp\\landqueryresult.xls";
	    outstream = new FileOutputStream(outpath); 

	    String tplFullName = request.getRealPath("/WEB-INF/jsp/wjmb/land1.xls");
	    InputStream input = new FileInputStream(tplFullName);
		HSSFWorkbook  wb = null;// 定义一个新的工作簿
		wb = new HSSFWorkbook(input);
		HSSFSheet sheet = wb.getSheetAt(0);
		Map<String,Integer> MapCol = new HashMap<String,Integer>();
		
		//所选字段
		String selectFields=RequestHelper.getParam(request, "selectFields");
		JSONArray jsonArray = JSONArray.fromObject(selectFields);
		List<Map> mapListJson = (List) jsonArray;
		//获取标题行格式
		int lastrow = sheet.getLastRowNum();
		HSSFRow head = sheet.getRow(lastrow - 1);
		HSSFCellStyle style = sheet.getRow(lastrow).getCell(0).getCellStyle();
		short height = sheet.getRow(lastrow).getHeight();
		//添加标题行
		HSSFRow title = (HSSFRow)sheet.createRow(1);
		title.setHeight(height);
		//序号
		HSSFCell Cell = title.createCell(0);
		 Cell.setCellValue("序号") ;
		 Cell.setCellStyle(style);
		 MapCol.put("序号", 0);
		//content
		for (int i = 0; i < mapListJson.size(); i++) {
			 HSSFCell title_Cell = title.createCell(i+1);
			 title_Cell.setCellValue(mapListJson.get(i).get("name").toString()) ;
			 MapCol.put(mapListJson.get(i).get("name").toString(), i+1);
			 title_Cell.setCellStyle(style);
		}
		//添加内容行
		int rownum = 2;
		for(Map r:rows){
			 HSSFRow row = (HSSFRow)sheet.createRow(rownum);
	         HSSFCell Cell0 = row.createCell(MapCol.get("序号"));
	         Cell0.setCellValue(rownum-1);
	         for (int i = 0; i < mapListJson.size(); i++) {
				 String name = mapListJson.get(i).get("name").toString();
				 String value = mapListJson.get(i).get("value").toString();
	        	 HSSFCell Content_Cell = row.createCell(MapCol.get(name));
	        	 Content_Cell.setCellValue(StringHelper.FormatByDatatype(r.get(value)));
			}
	        rownum += 1;
	  }
	
	    wb.write(outstream); 
	    outstream.flush(); 
	    outstream.close();
	    outstream = null;
	    return url;
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/dydjlist_xz/{bdcdyid}", method = RequestMethod.GET)
	public @ResponseBody List<List<Map>> GetDYDJList_XZ(@PathVariable("bdcdyid") String bdcdyid, HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		List<List<Map>> list = queryService.GetDYDJList_XZ(bdcdyid);
		return list;
	}
	
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/dydjlist_xz1/{bdcdyid}", method = RequestMethod.GET)
	public @ResponseBody List<List<Map>> GetDYDJList_XZ1(@PathVariable("bdcdyid") String bdcdyid, HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		List<List<Map>> list = queryService.GetDYDJList_XZ1(bdcdyid);
		return list;
	}
	
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/cfdjlist_xz/{bdcdyid}", method = RequestMethod.GET)
	public @ResponseBody List<List<Map>> GetCFDJList_XZ(@PathVariable("bdcdyid") String bdcdyid, HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		List<List<Map>> list = queryService.GetCFDJList_XZ(bdcdyid);
		return list;
	}
	
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/yydjlist_xz/{bdcdyid}", method = RequestMethod.GET)
	public @ResponseBody List<List<Map>> GetYYDJList_XZ(@PathVariable("bdcdyid") String bdcdyid, HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		List<List<Map>> list = queryService.GetYYDJList_XZ(bdcdyid);
		return list;
	}
	
	@RequestMapping(value = "/dyxzlist_xz/{bdcdyid}", method = RequestMethod.GET)
	public @ResponseBody List<BDCS_DYXZ> GetDYXZList_XZ(@PathVariable("bdcdyid") String bdcdyid, HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		List<BDCS_DYXZ> list = queryService.GetDYXZList_XZ(bdcdyid);
		return list;
	}
	
	
	/*
	 * 按照关联ID查询户状态信息
	 */
	@RequestMapping(value = "/housestatus_relationid/{relationid}/{bdcdylx}/", method = RequestMethod.GET)
	public @ResponseBody List<HashMap<String,String>> HouseStatusQuery_RelationID(@PathVariable("relationid") String relationid,@PathVariable("bdcdylx") String bdcdylx, HttpServletRequest request, HttpServletResponse response) {
			double startTime = System.currentTimeMillis();
			List<HashMap<String, String>> list = queryService.HouseStatusQuery_RelationID(relationid, bdcdylx);
			System.out.println("获取户状态用时:" + (System.currentTimeMillis() - startTime));
			return list;
	}
	
	/*
	 * 按照坐落查询户状态信息
	 */
	@RequestMapping(value = "/housestatus_zl/{zl}/{bdcdylx}/", method = RequestMethod.GET)
	public @ResponseBody List<HashMap<String,String>> HouseStatusQuery_ZL(@PathVariable("zl") String zl,@PathVariable("bdcdylx") String bdcdylx, HttpServletRequest request, HttpServletResponse response) {
		double startTime = System.currentTimeMillis();
		List<HashMap<String, String>> list = queryService.HouseStatusQuery_ZL(zl, bdcdylx);
		System.out.println("获取户状态用时:" + (System.currentTimeMillis() - startTime));
		return list;
	}
	
	/*
	 * 按照权证号或证明号查询户状态信息
	 */
	@RequestMapping(value = "/housestatus_qzh/{qzh}/{bdcdylx}/", method = RequestMethod.GET)
	public @ResponseBody List<HashMap<String,String>> HouseStatusQuery_QZH(@PathVariable("qzh") String qzh,@PathVariable("bdcdylx") String bdcdylx, HttpServletRequest request, HttpServletResponse response) {
			double startTime = System.currentTimeMillis();
			List<HashMap<String, String>> list = queryService.HouseStatusQuery_QZH(qzh, bdcdylx);
			System.out.println("获取户状态用时:" + (System.currentTimeMillis() - startTime));
			return list;
	}
	
	/*
	 * 按照权证号或证明号查询户状态信息
	 */
	@RequestMapping(value = "/housestatus_all/{zl}/{qzh}/{bdcdylx}/{qlrmc}/", method = RequestMethod.GET)
	public @ResponseBody List<HashMap<String,String>> HouseStatusQuery_ALL(@PathVariable("zl") String zl,@PathVariable("qzh") String qzh,@PathVariable("qlrmc") String qlrmc,@PathVariable("bdcdylx") String bdcdylx, HttpServletRequest request, HttpServletResponse response) {
			double startTime = System.currentTimeMillis();
			List<HashMap<String, String>> list = queryService.HouseStatusQuery_ALL(zl, qzh, qlrmc, bdcdylx);
			System.out.println("获取户状态用时:" + (System.currentTimeMillis() - startTime));
			return list;
	}
	
	/*
	 * 按照BDCDYID查询户状态信息
	 */
	@RequestMapping(value = "/housestatus_bdcdyid/{bdcdyid}/{djdyid}/{bdcdylx}/", method = RequestMethod.GET)
	public @ResponseBody HashMap<String,String> HouseStatusQuery_BDCDYID(@PathVariable("bdcdyid") String bdcdyid,@PathVariable("djdyid") String djdyid,@PathVariable("bdcdylx") String bdcdylx, HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		double startTime = System.currentTimeMillis();
		HashMap<String,String> m= queryService.HouseStatusQuery_BDCDYID(bdcdyid,djdyid,bdcdylx);
		System.out.println("获取户状态用时:" + (System.currentTimeMillis() - startTime));
		return m;
	}
	
	/**
	 * 泸州新增查询房屋是否首次登记，在建工程是否抵押
	 * @Title: GetHouseQueryList2
	 * @author:mss
	 * @date：2016年2月26日  
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "/houseQuery2", method = RequestMethod.GET)
	public @ResponseBody Message GetHouseQueryList2(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
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
		String qlrmc = RequestHelper.getParam(request, "QLRMC");// 权利人名称
		String dyr = RequestHelper.getParam(request, "DYR");// 抵押人名称
		String qlrzjh = RequestHelper.getParam(request, "ZJH");// 权利人证件号
		String fwzl = RequestHelper.getParam(request, "ZL");// 房屋坐落
		String bdcqzh = RequestHelper.getParam(request, "BDCQZH");// 不动产权证号
		String fwbm = RequestHelper.getParam(request, "FWBM");// 房屋编码
		String fh = RequestHelper.getParam(request, "FH");// 房号
		String ywbh = RequestHelper.getParam(request, "YWBH");// 业务编号
		String dyzt = RequestHelper.getParam(request, "DYZT");// 抵押状态
		String cfzt = RequestHelper.getParam(request, "CFZT");// 查封状态
		String zjdy = RequestHelper.getParam(request, "ZJDY");// 是否为在建抵押
		String fwzt=RequestHelper.getParam(request,"FWZT");
		String bdcdyh=RequestHelper.getParam(request, "BDCDYH");
		String bdcqzhxh=RequestHelper.getParam(request, "BDCQZHXH");
		String sort=RequestHelper.getParam(request, "sort");
		String order=RequestHelper.getParam(request, "order");
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
		YwLogUtil.addYwLog("房屋查询功能：查询", ConstValue.SF.YES.Value,ConstValue.LOG.SEARCH);
		return queryService2.queryHouse(queryvalues, page, rows, iflike,fwzt,sort,order,zjdy);
	}
	
	/**
	 * 获取分层分户图
	 * @param bdcdyid
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException 
	 * @throws SQLException 
	 */
	@RequestMapping(value = "/fcfht/{bdcdyid}/{fileurl}", method = RequestMethod.GET)
	public void  getFcfhtInfo(@PathVariable("bdcdyid") String bdcdyid,@PathVariable("fileurl") String fileurl, HttpServletRequest request, HttpServletResponse response)  {
		   try {
			queryService.getFcfhtInfo(bdcdyid,response,fileurl );
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	
	/*
	 * 按照BDCDYID查询户状态信息
	 */
	@RequestMapping(value = "/unitraltion/{bdcdyid}/{bdcdylx}/", method = RequestMethod.GET)
	public @ResponseBody HashMap<String,Object> getUnitRelation(@PathVariable("bdcdyid") String bdcdyid,@PathVariable("bdcdylx") String bdcdylx, HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		double startTime = System.currentTimeMillis();
		HashMap<String,Object> m= queryService.getUnitRelation(bdcdyid,bdcdylx);
		System.out.println("获取户状态用时:" + (System.currentTimeMillis() - startTime));
		return m;
	}

	/*
	 * 按照权证号或证明号查询户状态信息(查封，抵押查询)
	 */
	@RequestMapping(value = "/housestatus_qzhs/{input}/{qzlb}", method = RequestMethod.GET)
	public @ResponseBody Map<String,String> HouseStatusQuery_QZHS(@PathVariable("input") String qzh,@PathVariable("qzlb") String qzlb, HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		double startTime = System.currentTimeMillis();
		String nqzlb="";
		if(qzlb!=null&&qzlb.length()>0&&qzlb.equals("0")){//证书
			nqzlb="权";
		}else if(qzlb!=null&&qzlb.length()>0&&qzlb.equals("1")){//证明
			nqzlb="证明";
		}
		Map<String,String> m=new HashMap<String,String>();
		if(qzh!=null&&qzh.toString().length()>6){
			m = queryService.HouseStatusQuery_QZHS(qzh,nqzlb);
		}
		System.out.println("获取户状态用时:" + (System.currentTimeMillis() - startTime));
		return m;
	}
	
	/**
	 * 根据权证号获取档案信息(海口)
	 * @param type 类型
	 * @param bdcqzh 不动产权证号（证明号）
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException 
	 * @throws SQLException 
	 */
	@RequestMapping(value = "/archives/{type}/{bdcqzh}/", method = RequestMethod.GET)
	public @ResponseBody Map<String,Object> GetAchives_HK(@PathVariable("type") String type,@PathVariable("bdcqzh") String bdcqzh, HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		double startTime = System.currentTimeMillis();
		Map<String,Object> result=queryService.GetAchives_HK(type,bdcqzh);
		System.out.println("获取户状态用时:" + (System.currentTimeMillis() - startTime));
		return result;
	}
	
	/**
	 * 根据权证号获取档案信息(海口)
	 * @param bdcqzh 不动产权证号（证明号）
	 * @param archives_classno 档案类号
	 * @param archives_bookno 案卷号
	 * @param override 是否覆盖
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException 
	 * @throws SQLException 
	 */
	@RequestMapping(value = "/archives/{bdcqzh}/{archives_classno}/{archives_bookno}/{override}/", method = RequestMethod.GET)
	public @ResponseBody Map<String,Object> SetAchives_HK(@PathVariable("bdcqzh") String bdcqzh,@PathVariable("archives_classno") String archives_classno,@PathVariable("archives_bookno") String archives_bookno,@PathVariable("override") String override, HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		double startTime = System.currentTimeMillis();
		Map<String,Object> result=queryService.SetAchives_HK(bdcqzh,archives_classno,archives_bookno,override);
		System.out.println("获取户状态用时:" + (System.currentTimeMillis() - startTime));
		return result;
	}
	
	/**
	 * 根据权证号获取坐落信息
	 * @param bdcqzh 不动产权证号（证明号）
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException 
	 * @throws SQLException 
	 */
	@RequestMapping(value = "/getzlfrombdcqzh/{bdcqzh}/", method = RequestMethod.GET)
	public @ResponseBody ResultMessage GetZlFromBDCQZH(@PathVariable("bdcqzh") String bdcqzh,HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		return queryService.GetZlFromBDCQZH(bdcqzh);
	}
	
	/**
	 * 房屋和土地整合后的土地查询鹰潭
	 * @Title: GetLandQueryList
	 * @author:mss
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "/landQueryyt", method = RequestMethod.GET)
	public @ResponseBody Message GetLandQueryList1(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		Integer page = 1;
		if (request.getParameter("page") != null) {
			page = Integer.parseInt(request.getParameter("page"));
		}
		String querytype = request.getParameter("QUERYTYPE-ZD");
		boolean iflike = false;
		if (querytype != null && querytype.toLowerCase().equals("true")) {
			iflike = true;
		}
		Integer rows = 10;
		if (request.getParameter("rows") != null) {
			rows = Integer.parseInt(request.getParameter("rows"));
		}
		Map<String, String> queryvalues = new HashMap<String, String>();
		String sort = RequestHelper.getParam(request, "sort");// 排序字段
		String order = RequestHelper.getParam(request, "order");// 排序Order
		String qlrmc = RequestHelper.getParam(request, "QLRMC-ZD");// 权利人名称
		String qlrzjh = RequestHelper.getParam(request, "ZJH-ZD");// 权利人证件号
		String tdzl = RequestHelper.getParam(request, "ZL-ZD");// 土地坐落
		String bdcqzh = RequestHelper.getParam(request, "BDCQZH-ZD");// 不动产权证号
		String ywbh = RequestHelper.getParam(request, "YWBH-ZD");// 业务编号
		String dyzt = RequestHelper.getParam(request, "DYZT-ZD");// 抵押状态
		String cfzt = RequestHelper.getParam(request, "CFZT-ZD");// 查封状态
		String fwzt=RequestHelper.getParam(request,"TDZT-ZD");
		String bdcdyh=RequestHelper.getParam(request, "BDCDYH-ZD");
		String zddm=RequestHelper.getParam(request, "ZDDM-ZD");
		queryvalues.put("DY.ZL", tdzl);
		queryvalues.put("DY.BDCDYH", bdcdyh);
		queryvalues.put("DY.ZDDM", zddm);
		queryvalues.put("QLR.QLRMC", qlrmc);
		queryvalues.put("QLR.ZJH", qlrzjh);
		queryvalues.put("QL.BDCQZH", bdcqzh);
		queryvalues.put("QL.YWH", ywbh);
		queryvalues.put("DYZT", dyzt);
		queryvalues.put("CFZT", cfzt);
		
		//-------------获取宗地单元土地统计详情数据
		String fzlb = RequestHelper.getParam(request, "TD_FZLB");
		String zddy = RequestHelper.getParam(request, "ZDDY");
		String td_xzqh = RequestHelper.getParam(request, "TD_XZQH");
		String td_qllx = RequestHelper.getParam(request, "TD_QLLX");
		String td_qlsdfs = RequestHelper.getParam(request, "TD_QLSDFS");
		String td_qlxz = RequestHelper.getParam(request, "TD_QLXZ");
		String td_tddj = RequestHelper.getParam(request, "TD_TDDJ");
		String td_qlrlx = RequestHelper.getParam(request, "TD_QLRLX");
		String td_tdyt = RequestHelper.getParam(request, "TD_TDYT");
		String td_fzdy = RequestHelper.getParam(request, "TD_FZDY");
		YwLogUtil.addYwLog("土地查询功能，查询", ConstValue.SF.YES.Value,ConstValue.LOG.SEARCH);
		return queryService.queryLandyt(queryvalues, page, rows, iflike,fwzt,sort,order,td_xzqh,td_qllx,td_qlsdfs,td_qlxz,td_tddj,td_qlrlx,td_tdyt,td_fzdy,fzlb,zddy);
	}
	
	@SuppressWarnings({ "deprecation", "rawtypes", "unchecked" })
	@RequestMapping(value = "/houseQueryresult", method = RequestMethod.POST)
	public @ResponseBody String HouseQueryresultDownload(HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String rows = RequestHelper.getParam(request, "rows");
		com.alibaba.fastjson.JSONArray jsonArr = JSONObject.parseArray(rows);

		// 获取当前时间
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 ");
		String time = format.format(date);
		// String yyyy = time.substring(0, 3);

		String basePath = request.getRealPath("/") + "\\resources\\PDF";
		String outpath = "";
		String url = "";
		FileOutputStream outstream = null;
		outpath = basePath + "\\tmp\\DYXX.xls";
		url = request.getContextPath() + "\\resources\\PDF\\tmp\\DYXX.xls";
		outstream = new FileOutputStream(outpath);
		String tplFullName = request.getRealPath("/WEB-INF/jsp/wjmb/DYXX.xls");
		InputStream input = new FileInputStream(tplFullName);
		HSSFWorkbook wb = null;// 定义一个新的工作簿
		wb = new HSSFWorkbook(input);
		Sheet sheet = wb.getSheetAt(0);
		HSSFCell Cell1 = (HSSFCell) sheet.getRow(10).getCell(0);
		Cell1.setCellValue(time);
		int MaxRow = sheet.getPhysicalNumberOfRows() + 1;
		int RowIndex = 14;
		Row BaseCell = sheet.getRow(14);
		for (Object row : jsonArr) {
			sheet.shiftRows(++RowIndex, MaxRow++, 1, true, false);
			Row InsertRow = sheet.createRow(RowIndex);
			Map map = (Map) row;
			HSSFCell Cell2 = (HSSFCell) InsertRow.createCell(0);
			Cell2.setCellStyle(BaseCell.getCell(0).getCellStyle());
			Cell2.setCellType(HSSFCell.CELL_TYPE_STRING);
			Cell2.setCellValue(StringHelper.formatObject(map.get("QLRMC")));
			HSSFCell Cell3 = (HSSFCell) InsertRow.createCell(1);
			Cell3.setCellStyle(BaseCell.getCell(1).getCellStyle());
			Cell3.setCellType(HSSFCell.CELL_TYPE_STRING);
			Cell3.setCellValue(StringHelper.formatObject(map.get("ZL")));
			HSSFCell Cell4 = (HSSFCell) InsertRow.createCell(2);
			Cell4.setCellStyle(BaseCell.getCell(2).getCellStyle());
			Cell4.setCellType(HSSFCell.CELL_TYPE_STRING);
			Cell4.setCellValue(StringHelper.formatObject(map.get("BDCQZH")));
			HSSFCell Cell5 = (HSSFCell) InsertRow.createCell(3);
			Cell5.setCellStyle(BaseCell.getCell(3).getCellStyle());
			Cell5.setCellType(HSSFCell.CELL_TYPE_STRING);
			Cell5.setCellValue(StringHelper.formatObject(map.get("CFZT")));
			HSSFCell Cell6 = (HSSFCell) InsertRow.createCell(4);
			Cell6.setCellStyle(BaseCell.getCell(4).getCellStyle());
			Cell6.setCellType(HSSFCell.CELL_TYPE_STRING);
			Cell6.setCellValue(StringHelper.formatObject(map.get("DYZT")));
			HSSFCell Cell7 = (HSSFCell) InsertRow.createCell(5);
			Cell7.setCellStyle(BaseCell.getCell(5).getCellStyle());
			Cell7.setCellType(HSSFCell.CELL_TYPE_STRING);
			Cell7.setCellValue("");
		}
		HSSFCell Cell7 = (HSSFCell) sheet.getRow(MaxRow - 2).getCell(3);
		Cell7.setCellValue(time);
		wb.write(outstream);
		outstream.flush();
		outstream.close();
		outstream = null;
		return url;
	}

	/**
	 * 房屋查询全市监管
	 * @Title: GetHouseQueryList
	 * @author:luoyu
	 * @date：2015年8月26日 下午5:38:52
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "/houseQueryJG", method = RequestMethod.GET)
	public @ResponseBody Message GetHouseQueryListJG(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
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
		String dyzt = RequestHelper.getParam(request, "DYZT");// 抵押状态
		String cfzt = RequestHelper.getParam(request, "CFZT");// 查封状态
		String cxzt = RequestHelper.getParam(request, "CXZT");// 查询状态
		String yyzt = RequestHelper.getParam(request, "YYZT");// 异议状态
		String fwzt=RequestHelper.getParam(request,"FWZT");
		String bdcdyh=RequestHelper.getParam(request, "BDCDYH");
		String bdcqzhxh=RequestHelper.getParam(request, "BDCQZHXH");
		String cfwh=RequestHelper.getParam(request, "CFWH");
		String xzqh=RequestHelper.getParam(request, "XZQH");
		queryvalues.put("DY.ZL", fwzl);
		queryvalues.put("DY.BDCDYH", bdcdyh);
		queryvalues.put("QLR.QLRMC", qlrmc);
		queryvalues.put("DYR.DYR", dyr);
		queryvalues.put("QLR.ZJH", qlrzjh);
		queryvalues.put("QL.BDCQZH", bdcqzh);
		queryvalues.put("QLR.BDCQZHXH", bdcqzhxh);
		queryvalues.put("FSQL.CFWH", cfwh);
		queryvalues.put("DY.FWBM", fwbm);
		queryvalues.put("DY.FH", fh);
		queryvalues.put("QL.YWH", ywbh);
		queryvalues.put("DYZT", dyzt);
		queryvalues.put("CFZT", cfzt);
		queryvalues.put("CXZT", cxzt);
		queryvalues.put("YYZT", yyzt);
		//-------------获取宗地单元房屋统计详情数据
		String fw_xzqh = RequestHelper.getParam(request, "FW_XZQH");
		String fw_qlrlx = RequestHelper.getParam(request, "FW_QLRLX");
		String fw_fwyt = RequestHelper.getParam(request, "FW_FWYT");
		String fw_fwlx = RequestHelper.getParam(request, "FW_FWLX");
		String fw_fwxz = RequestHelper.getParam(request, "FW_FWXZ");
		String fw_hx = RequestHelper.getParam(request, "FW_HX");
		String fw_hxjg = RequestHelper.getParam(request, "FW_HXJG");
		String fw_fwjg = RequestHelper.getParam(request, "FW_FWJG");
		String fw_fwcb = RequestHelper.getParam(request, "FW_FWCB");
		String fw_fzdy = RequestHelper.getParam(request, "FW_FZDY");
		if(fw_fzdy!=null && fw_fzdy.equals("4")){
			fw_fzdy = "行政区划";
		}
		String fzlb = RequestHelper.getParam(request, "FW_FZLB");
		if(fzlb!=null && fzlb.equals("1")){
			fzlb="市本级";
		}
		if(fzlb!=null && fzlb.equals("2")){
			fzlb="贵溪";
		}
		if(fzlb!=null && fzlb.equals("3")){
			fzlb="余江";
		}
		String zddy = RequestHelper.getParam(request, "ZDDY");
		String fw_djq = RequestHelper.getParam(request, "FW_DJQ");
		
		YwLogUtil.addYwLog("房屋查询功能：查询", ConstValue.SF.YES.Value,ConstValue.LOG.SEARCH);
		return queryService.queryHouseJG(queryvalues, page, rows, iflike,fwzt,sort,order,xzqh,zddy,fw_xzqh,fw_qlrlx,fw_fwyt,fw_fwlx,
				fw_fwxz,fw_hx,fw_hxjg,fw_fwjg,fw_fwcb,fw_fzdy,fzlb,fw_djq);
	}

    /**
	 * 土地查询全市监管
	 * @Title: GetLandQueryList
	 * @author:luoyu
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "/landQueryJG", method = RequestMethod.GET)
	public @ResponseBody Message GetLandQueryListJG(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		Integer page = 1;
		if (request.getParameter("page") != null) {
			page = Integer.parseInt(request.getParameter("page"));
		}
		String querytype = request.getParameter("QUERYTYPE-ZD");
		boolean iflike = false;
		if (querytype != null && querytype.toLowerCase().equals("true")) {
			iflike = true;
		}
		Integer rows = 10;
		if (request.getParameter("rows") != null) {
			rows = Integer.parseInt(request.getParameter("rows"));
		}
		Map<String, String> queryvalues = new HashMap<String, String>();
		String sort = RequestHelper.getParam(request, "sort");// 排序字段
		String order = RequestHelper.getParam(request, "order");// 排序Order
		String qlrmc = RequestHelper.getParam(request, "QLRMC-ZD");// 权利人名称
		String qlrzjh = RequestHelper.getParam(request, "ZJH-ZD");// 权利人证件号
		String tdzl = RequestHelper.getParam(request, "ZL-ZD");// 土地坐落
		String bdcqzh = RequestHelper.getParam(request, "BDCQZH-ZD");// 不动产权证号
		String ywbh = RequestHelper.getParam(request, "YWBH-ZD");// 业务编号
		String dyzt = RequestHelper.getParam(request, "DYZT-ZD");// 抵押状态
		String cfzt = RequestHelper.getParam(request, "CFZT-ZD");// 查封状态
		String fwzt=RequestHelper.getParam(request,"TDZT-ZD");
		String bdcdyh=RequestHelper.getParam(request, "BDCDYH-ZD");
		String zddm=RequestHelper.getParam(request, "ZDDM-ZD");
		String xzqh=RequestHelper.getParam(request, "XZQH");
		queryvalues.put("DY.ZL", tdzl);
		queryvalues.put("DY.BDCDYH", bdcdyh);
		queryvalues.put("DY.ZDDM", zddm);
		queryvalues.put("QLR.QLRMC", qlrmc);
		queryvalues.put("QLR.ZJH", qlrzjh);
		queryvalues.put("QL.BDCQZH", bdcqzh);
		queryvalues.put("QL.YWH", ywbh);
		queryvalues.put("DYZT", dyzt);
		queryvalues.put("CFZT", cfzt);
		//-------------获取宗地单元土地统计详情数据
		String fzlb = RequestHelper.getParam(request, "TD_FZLB");
		String zddy = RequestHelper.getParam(request, "ZDDY");
		String td_xzqh = RequestHelper.getParam(request, "TD_XZQH");
		String td_qllx = RequestHelper.getParam(request, "TD_QLLX");
		String td_qlsdfs = RequestHelper.getParam(request, "TD_QLSDFS");
		String td_qlxz = RequestHelper.getParam(request, "TD_QLXZ");
		String td_tddj = RequestHelper.getParam(request, "TD_TDDJ");
		String td_qlrlx = RequestHelper.getParam(request, "TD_QLRLX");
		String td_tdyt = RequestHelper.getParam(request, "TD_TDYT");
		String td_fzdy = RequestHelper.getParam(request, "TD_FZDY");
		String td_djqdm = RequestHelper.getParam(request, "TD_DJQ");
		YwLogUtil.addYwLog("土地查询功能，查询", ConstValue.SF.YES.Value,ConstValue.LOG.SEARCH);
		return queryService.queryLandJG(queryvalues, page, rows, iflike,fwzt,sort,order,xzqh,td_xzqh,td_qllx,td_qlsdfs,td_qlxz,td_tddj,td_qlrlx,td_tdyt,td_fzdy,fzlb,zddy,td_djqdm);
	}
	
	/*
	 * 查询
	 * 
	 * */
	@RequestMapping(value = "/GetDyQueryList", method = RequestMethod.POST)
	public @ResponseBody Message GetDyQueryList(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		return QueryList.GetQuery(request);
	}
	@RequestMapping(value = "/GetQueryHead", method = RequestMethod.POST)
	public @ResponseBody List<Map<String, String>> GetQueryHead(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		return QueryList.GetHead(request);
	}
	@RequestMapping(value = "/GetCxTree/{cxlx}/{xmbh}", method = RequestMethod.GET)
	public @ResponseBody Message GetCxTree(@PathVariable("xmbh") String xmbh,@PathVariable("cxlx") String cxlx,HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		return QueryList.GetCxTree(xmbh,cxlx);
	}
	@RequestMapping(value = "/GetPrnTree/{cxlx}/{xmbh}", method = RequestMethod.GET)
	public @ResponseBody Message GetPrnTree(@PathVariable("xmbh") String xmbh,@PathVariable("cxlx") String cxlx,HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		return QueryList.GetPrnTree(xmbh,cxlx);
	}
	@RequestMapping(value = "/GetPrnList", method = RequestMethod.POST)
	public @ResponseBody Message GetPrnList(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		return QueryList.QueryPrint(request);
	}
	@RequestMapping(value = "/{xmbh}/GetNohouseInfo", method = RequestMethod.POST)
	public @ResponseBody Message GetNohouseInfo(@PathVariable("xmbh") String xmbh,HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		return QueryList.NohouseInfo(request,xmbh);
	}
	
	
	/**
	 * @Title: GetHouseQueryBatch
	 * @Description: TODO
	 * @Author：赵梦帆
	 * @Data：2016年11月2日 下午2:49:05
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 * @return Message
	 */
	@RequestMapping(value = "/houseQueryBatch", method = RequestMethod.POST)
	public @ResponseBody Message GetHouseQueryBatch(@RequestParam("file") MultipartFile file,
			HttpServletRequest request, HttpServletResponse response,
			Object command) {
		return queryService.Batch(file,request);
	}
	

	/**
	 * @Title: GetHouseQueryEx
	 * @Description: 批量查询
	 * @Author：赵梦帆
	 * @Data：2016年11月3日 上午11:48:32
	 * @param request
	 * @param response
	 * @param command
	 * @return
	 * @return Message
	 * @throws IOException 
	 */
	@RequestMapping(value = "/houseQueryEX", method = RequestMethod.POST)
	public @ResponseBody String GetHouseQueryEx(HttpServletRequest request, HttpServletResponse response,
			Object command) throws IOException {
		return queryService.BatchQuery(request);
	}
	
	@RequestMapping(value = "/houseQueryGetTemptepl", method = RequestMethod.GET)
	public @ResponseBody String houseQueryGetTemptepl(HttpServletRequest request, HttpServletResponse response,
			Object command) throws IOException {
		return queryService.GetTemptepl(request);
	}
	
	@RequestMapping(value = "/getwlmqfw", method = RequestMethod.GET)
	public @ResponseBody String getWLMQFW(HttpServletRequest request, HttpServletResponse response,
			Object command) throws IOException {
		return queryService.getWLMQFW(request);
	}
	
	
	
	/**
	 * 综合查询-实现在查询同一产权人名下的所有宗地与房产信息时，全部显示
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "/integratedQuery", method = RequestMethod.POST)
	public @ResponseBody Message GetIntegratedQueryList(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
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
		String sort = RequestHelper.getParam(request, "sort");// 排序字段
		String order = RequestHelper.getParam(request, "order");// 排序Order
		String qlrmc = RequestHelper.getParam(request, "QLRMC");// 权利人名称
		String qlrzjh = RequestHelper.getParam(request, "ZJH");// 权利人证件号
		String fwzl = RequestHelper.getParam(request, "ZL");// 房屋坐落
		String bdcdyh=RequestHelper.getParam(request, "BDCDYH");
		queryvalues.put("DY.ZL", fwzl);
		queryvalues.put("DY.BDCDYH", bdcdyh);
		queryvalues.put("QLR.QLRMC", qlrmc);
		queryvalues.put("QLR.ZJH", qlrzjh);
		YwLogUtil.addYwLog("房屋查询功能：查询", ConstValue.SF.YES.Value,ConstValue.LOG.SEARCH);
		return queryService.queryIntegrated(queryvalues, page, rows, iflike,sort,order);
	}
	
	/**
	 * @Description: 小土地证查询
	 * @Title: GetXTDZQueryList
	 * @Author：赵梦帆
	 * @Data：2016年12月14日 上午11:02:41
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 * @return Message
	 */
	@RequestMapping(value = "/XTDZQuery", method = RequestMethod.POST)
	public @ResponseBody Message GetXTDZQueryList(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
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
		
		String xm = RequestHelper.getParam(request, "XM");// 姓名
		String fczh = RequestHelper.getParam(request, "FCZH");// 房产证号
		String tdzh = RequestHelper.getParam(request, "TDZH");// 土地证号
		String zl=RequestHelper.getParam(request, "ZL");// 坐落
		
		String dyr = RequestHelper.getParam(request, "DYR");// 抵押人
		String txzh = RequestHelper.getParam(request, "TXZH");// 他项证号
		String tdz = RequestHelper.getParam(request, "TDZ");// 土地证号
		
		String qlr = RequestHelper.getParam(request, "QLR");// 权利人
		String cfwh = RequestHelper.getParam(request, "CFWH");// 查封文号
		
		String lx=RequestHelper.getParam(request, "LX");
		String fwbdcdyid=RequestHelper.getParam(request, "FWBDCDYID");
		String zdbdcdyid=RequestHelper.getParam(request, "ZDBDCDYID");
		if("XTDZ".equals(lx)){
			queryvalues.put("XTDZ.XM", xm);
			queryvalues.put("XTDZ.FCZH", fczh);
			queryvalues.put("XTDZ.TDZH", tdzh);
			queryvalues.put("XTDZ.ZL", zl);
			if("XTDZ".equals(lx)){
				queryvalues.put("XTDZ.FWBDCDYID", fwbdcdyid);
				queryvalues.put("XTDZ.ZDBDCDYID", zdbdcdyid);
			}
		}
		if ("XZDY".equals(lx)) {
			queryvalues.put("XTDZ.DYR", dyr);
			queryvalues.put("XTDZ.TXZH", txzh);
			queryvalues.put("XTDZ.TDZ", tdz);
			queryvalues.put("XTDZ.ZL", zl);
		}
		if ("XZCF".equals(lx)) {
			queryvalues.put("XTDZ.QLR", qlr);
			queryvalues.put("XTDZ.CFWH", cfwh);
			queryvalues.put("XTDZ.TDZH", tdzh);
			queryvalues.put("XTDZ.ZL", zl);
		}
		
		YwLogUtil.addYwLog("小土地证查询功能：查询", ConstValue.SF.YES.Value,ConstValue.LOG.SEARCH);
		return queryService.queryXTDZ(queryvalues, page, rows, iflike,"","",lx);
	}
	
	/**
	 * @Description: 小土地证信息更新，主要包括产权、抵押、查封
	 * @Title: UpdateXTDZQueryList
	 * @Author：赵梦帆
	 * @Data：2016年12月15日 上午10:16:32
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 * @return Message
	 */
	@RequestMapping(value = "/UpdateXTDZQuery", method = RequestMethod.POST)
	public @ResponseBody Message UpdateXTDZQueryList(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		String id = RequestHelper.getParam(request, "id");
		String lx = RequestHelper.getParam(request, "lx");
		String value = RequestHelper.getParam(request, "value");
		YwLogUtil.addYwLog("小土地证查询功能：更新", ConstValue.SF.YES.Value,ConstValue.LOG.UPDATE);
		return queryService.UpdateXTDZ(id,lx,value);
	}
		
	/**
	 * @Description: 获取小土地证产权
	 * @Title: GetXTDZQLInfo
	 * @Author：赵梦帆
	 * @Data：2016年12月15日 上午10:19:55
	 * @param bdcdyid
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 * @return QLInfo
	 */
	@RequestMapping(value = "/XTDZqlinfo/{bdcdyid}", method = RequestMethod.GET)
	public @ResponseBody Map<String, Map<String, String>> GetXTDZQLInfo(@PathVariable("bdcdyid") String bdcdyid, HttpServletRequest request, HttpServletResponse response) {
		Map<String, Map<String, String>> ql = queryService.GetXTDZQLInfo(bdcdyid);
		return ql;
	}
	
	/**
	 * @Description: 获取小土地证抵押列表
	 * @Title: GetXTDZdydjlist
	 * @Author：赵梦帆
	 * @Data：2016年12月15日 下午12:53:21
	 * @param bdcdyid
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 * @return Map<String,Map<String,String>>
	 */
	@RequestMapping(value = "/XTDZdydjlist/{bdcdyid}/{id}", method = RequestMethod.GET)
	public @ResponseBody Map<String, List<BDCS_XZDY>> GetXTDZdydjlist(@PathVariable("bdcdyid") String bdcdyid, @PathVariable("id") String id, HttpServletRequest request, HttpServletResponse response) {
		List<BDCS_XZDY> list = queryService.GetXTDZdydjlist(bdcdyid,id);
		Map<String, List<BDCS_XZDY>> map = new HashMap<String, List<BDCS_XZDY>>();
		map.put("dylist", list);
		return map;
	}
	
	/**
	 * @Description: 获取小土地证查封列表
	 * @Title: GetXTDZcfdjlist
	 * @Author：赵梦帆
	 * @Data：2016年12月15日 下午1:28:17
	 * @param bdcdyid
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 * @return Map<String,List<BDCS_XZCF>>
	 */
	@RequestMapping(value = "/XTDZcfdjlist/{bdcdyid}/{id}", method = RequestMethod.GET)
	public @ResponseBody Map<String, List<BDCS_XZCF>> GetXTDZcfdjlist(@PathVariable("bdcdyid") String bdcdyid, @PathVariable("id") String id, HttpServletRequest request, HttpServletResponse response) {
		List<BDCS_XZCF> list = queryService.GetXTDZcfdjlist(bdcdyid,id);
		Map<String, List<BDCS_XZCF>> map = new HashMap<String, List<BDCS_XZCF>>();
		map.put("cflist", list);
		return map;
	}
	
	/**
	 * @Description: 小证抵押信息
	 * @Title: GetXTDZdydjinfo
	 * @Author：赵梦帆
	 * @Data：2016年12月15日 下午1:52:19
	 * @param id
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 * @return Map<String,String>
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/XTDZqlinfo/{id}/DYDJ", method = RequestMethod.GET)
	public @ResponseBody Map GetXTDZdydjinfo(@PathVariable("id") String id, HttpServletRequest request, HttpServletResponse response) {
		Map map = queryService.GetXTDZdydjinfo(id);
		return map;
	}
	
	/**
	 * @Description: 小证查封信息
	 * @Title: GetXTDZcfdjinfo
	 * @Author：赵梦帆
	 * @Data：2016年12月15日 下午1:52:46
	 * @param id
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 * @return Map<String,String>
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/XTDZqlinfo/{id}/CFDJ", method = RequestMethod.GET)
	public @ResponseBody Map GetXTDZcfdjinfo(@PathVariable("id") String id, HttpServletRequest request, HttpServletResponse response) {
		Map map = queryService.GetXTDZcfdjinfo(id);
		return map;
	}
	
	/**
	 * @Description: 小土地证详细信息
	 * @Title: GetXTDZDetail
	 * @Author：俞学斌
	 * @Data：2016年12月22日 00:15:46
	 * @param request
	 * @param response
	 * @return Map<String,String>
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/xtdzdetail/", method = RequestMethod.GET)
	public @ResponseBody List<Map> GetXTDZDetail(HttpServletRequest request, HttpServletResponse response) {
		List<Map> list = queryService.GetXTDZDetail(request);
		return list;
	}
	
	/**
	 * @Description: 更改小土地证信息
	 * @Title: UpdateXTDZ
	 * @Author：俞学斌
	 * @Data：2016年12月22日 00:15:46
	 * @param request
	 * @param response
	 * @return ResultMessage
	 */
	@RequestMapping(value = "/xtdzmodify/", method = RequestMethod.POST)
	public @ResponseBody ResultMessage UpdateXTDZ(HttpServletRequest request, HttpServletResponse response) {
		ResultMessage msg = queryService.UpdateXTDZ(request);
		return msg;
	}
	
	
	/**
	 * @Description: 根据djdyid和权利类别获取权利信息
	 * @Title: GetInfoEX
	 * @Author: zhaomengfan
	 * @Date: 2016年12月23日上午11:10:02
	 * @param djdyid
	 * @param ql
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 * @return List<Map<String,String>>
	 */
	@RequestMapping(value = "qlinfoPrint/{djdyid}/{ql}", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> GetInfoEX(@PathVariable("djdyid") String djdyid,@PathVariable("ql") String ql, HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		Map<String, Object> list = queryService.QLInfoEX(djdyid,ql);
		return list;
	}
	
	@RequestMapping(value = "lsqlinfoPrint/{qlid}", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> GetLSInfoEX(@PathVariable("qlid") String qlid,HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		Map<String, Object> list = queryService.LSQLInfoEX(qlid);
		return list;
	}
	
	/**
	 * @Description: 产权获取，获取不到则判断是否有抵押（在建工程抵押）
	 * @Title: GetQLInfo_XZEX
	 * @Author: zhaomengfan
	 * @Date: 2016年12月25日下午10:19:56
	 * @param bdcdyid
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 * @return QLInfo
	 */
	@RequestMapping(value = "/qlinfo_xzEX/{bdcdyid}", method = RequestMethod.GET)
	public @ResponseBody QLInfo GetQLInfo_XZEX(@PathVariable("bdcdyid") String bdcdyid, HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		QLInfo ql = queryService.GetQLInfo_XZEX(bdcdyid);
		return ql;
	}
	
	/**
	 * @Description: 获取房屋信息
	 * @Title: GetFWinfo
	 * @Author: zhaomengfan
	 * @Date: 2017年1月20日下午1:21:32
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 * @return Map<String,String>
	 */
	@RequestMapping(value = "/GetFWinfo", method = RequestMethod.POST)
	public @ResponseBody Map<String,Object> GetFWinfo(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		String bdcdyid = RequestHelper.getParam(request, "bdcdyid");
		String djdyid = RequestHelper.getParam(request, "djdyid");
		String xmbh = RequestHelper.getParam(request, "xmbh");
		String qlid = RequestHelper.getParam(request, "qlid");
		String showlsh = RequestHelper.getParam(request, "lshlx");
		String logid = RequestHelper.getParam(request, "logid");
		Map<String,Object> info = queryService.GetFWinfo(xmbh,djdyid,bdcdyid,qlid,showlsh,logid);
		
		return info;
	}
	
	/** 
	 * 文件上传(导入模版)
	 * @author heks
	 * @param file
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = "/doUpload", method = RequestMethod.POST)
	public void loginIndex(
			HttpServletRequest request, HttpServletResponse response,@RequestParam("file") MultipartFile file)  {
				try {
					request.setCharacterEncoding("utf-8");
				} catch (UnsupportedEncodingException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
		        String basicPath=request.getContextPath()+"/WEB-INF/upload";
		        
		        CommonsMultipartFile cf= (CommonsMultipartFile)file; 
		        DiskFileItem fi = (DiskFileItem)cf.getFileItem(); 

		        File f = fi.getStoreLocation();
		       String sdd= f.getAbsolutePath();
		        //建立目录
		        File dirFile = new File(basicPath);
		        //判断目录是否存在
		        if (!dirFile.exists()) {
		            //不存在则新建
		            dirFile.mkdirs();
		        }        
		        //获取文件名
		        String fileName =file.getOriginalFilename();
		        //文件路径
		        String filepath =basicPath + "/" + fileName;   
		        request.getSession().setAttribute("FILEPATH", filepath);
		        File tempFile = new File(filepath);    
		        // 文件存在，先删除
		        if (tempFile.exists()) {
		            tempFile.delete();
		        }        
		        InputStream inputStream = null;
		        InputStream inStream = null;
		        try{
		            //文件不存在，创建该文件
		            tempFile.createNewFile();
		            inputStream=file.getInputStream();            
		            FileUtils.copyInputStreamToFile(inputStream, tempFile);  		           
		        }catch(Exception e){
		            e.printStackTrace();
		        }finally{
		        	try {
						inputStream.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}		        
		        }      
	}

	
	/**
	 * 读取Excle模版中的权利人名称和身份证号返回页面显示
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@SuppressWarnings("unused")
	@RequestMapping(value = "/queryhousebyqlrmcandzjh/{cdyt}", method = RequestMethod.GET)
	public @ResponseBody Message getLogQueryList(@PathVariable("cdyt") String cdyt,HttpServletRequest request, HttpServletResponse response)throws UnsupportedEncodingException{
		Message msg = new Message();
		//HttpSession session = request.getSession();
		String file_path = (String) request.getSession().getAttribute("FILEPATH");
		File tempFile = new File(file_path); 
		SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
		String cdbh = "";//查档编号
		String cdmd = "";//查档用途
		try{
			List<Map> queryvaluesLists = new ArrayList<Map>();
			//List<Map> queryvaluesLists1 = new ArrayList<Map>();
			List<Map> qlrInfoLists = new ArrayList<Map>();
			FileInputStream is = new FileInputStream(tempFile); //文件流  
	        Workbook wb = WorkbookFactory.create(is); //这种方式 Excel 2003/2007/2010 都是可以处理的 
			Sheet sheet = wb.getSheetAt(0);//获取Excle模版的第一个sheet
			int rowCount = sheet.getLastRowNum(); //获取第一个sheet有效行数 
			//查询当前记录的最大查档编号
			String fullsql = "SELECT Max(cdbh) as CDBH FROM SMWB_SUPPORT.GX_CONFIG";
			List<Map> cdbhs = baseCommonDao.getDataListByFullSql(fullsql); 
			
			if(cdbhs.size() >0){
				String max_cdbh = (String) cdbhs.get(0).get("CDBH");

				int curr_maxcdbh = Integer.parseInt(max_cdbh);

				int new_cdbh = curr_maxcdbh +1;

				if(new_cdbh > 1 && new_cdbh < 10)
					cdbh = "0000" + new_cdbh;
				if(new_cdbh > 9 && new_cdbh < 100)
					cdbh = "000" + new_cdbh;
				if(new_cdbh > 99 && new_cdbh < 1000)
					cdbh = "00" + new_cdbh;
				if(new_cdbh > 999 && new_cdbh < 10000)
					cdbh = "0" + new_cdbh;
				if(new_cdbh > 9999 && new_cdbh < 100000)
					cdbh = new_cdbh + "";
			}
		
			
			for (int r = 1; r < rowCount+1; r++) {   
				String qlrmc = "";
				String zjh = "";
				Map<String,String> m = new HashMap<String, String>();
				Map<String,String> queryvalueMap = new HashMap<String, String>();
				Row rw = sheet.getRow(r);
				Cell cl = rw.getCell(0); //第1列的值 ---编号
				Cell cl0 = rw.getCell(1);//第2列的值 ---姓名
				Cell cl1 = rw.getCell(2);//第3列的值 ---证件号码
				Cell cl_bz = rw.getCell(3);//第4列的值   
				
				if(cl != null){
					cl.setCellType(Cell.CELL_TYPE_STRING);
					String bh = cl.getStringCellValue();
					m.put("BH", bh);
					queryvalueMap.put("BH", bh);
				}else{
					continue;
				}

				if(cl0 == null){
					continue;
				}else{
					qlrmc = cl0.getStringCellValue();//获取权利人名称
					queryvalueMap.put("QLR.QLRMC", qlrmc);
					m.put("QLRMC", qlrmc);
					if(cl1 == null){
						continue;
					}else{
						cl1.setCellType(Cell.CELL_TYPE_STRING);
						zjh = cl1.getStringCellValue();//获取权利人证件号（身份证号）
						queryvalueMap.put("QLR.ZJH", zjh);
						m.put("ZJHM", zjh);

						if(cdyt.equals("2")){
							Cell cl3 = rw.getCell(3);
							String qzh = cl3.getStringCellValue();
							m.put("QZH", qzh);
							queryvalueMap.put("QZH", qzh);
						}
						queryvaluesLists.add(queryvalueMap);//用于查询房屋信息的
					}
				}
				
				String bz = "";
				if(cdyt.equals("0") ||  cdyt.equals("1") ){
					if(cl_bz != null){
						bz = cl_bz.getStringCellValue();
						m.put("BZ", bz);
						queryvalueMap.put("BZ", bz);
					}
					
					cdmd = "有无房产";
					if((String)cdbhs.get(0).get("CDBH") == null) {
						cdbh = "00001";
					}
					/*else if((String)cdbhs.get(0).get("CDBH") != null && cdbhs.size() > 0){
						
					}*/
				}
				if(cdyt.equals("2")){ //权利查档
					
					Cell cl4 = rw.getCell(4);//第4列的值     ---坐落
					String zl = cl4.getStringCellValue();
					m.put("ZL", zl);
					queryvalueMap.put("ZL", zl);

					Cell cl5 = rw.getCell(5);
					String tdqzh = cl5.getStringCellValue();
					m.put("TDQZH", tdqzh);
					queryvalueMap.put("TDQZH", tdqzh);

					Cell cl6 = rw.getCell(6);
					String bz2 = cl6.getStringCellValue();
					m.put("BZ", bz2);
					queryvalueMap.put("BZ", bz2);
					
					cdmd = "不动产权利登记信息查询";
					
					if(cdbhs.size() == 0) 
						cdbh = "00001";
					//else if(cdbhs.size() > 0){}
				}

				qlrInfoLists.add(m);//返回页面显示Excle表的集合

				GX_CONFIG  gx_config = new GX_CONFIG();
				gx_config.setCdbh(cdbh);
				gx_config.setCdmd(cdmd);
				gx_config.setCdrmc(qlrmc);
				gx_config.setCdrzjh(zjh);

				Date currdate = new Date();

				SimpleDateFormat  formatter  = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				try {    
					String cd_datestring  = formatter.format(currdate);  
					gx_config.setCdtime(cd_datestring);
				} catch (Exception e) {    
					e.printStackTrace();    
				} 
				baseCommonDao.save(gx_config);
			}
	         baseCommonDao.flush();
			
	         if(cdyt.equals("0") || cdyt.equals("1") ){ //权利查档
		         request.getSession().setAttribute("QVLL1", queryvaluesLists);//有房无地查档查询条件
	         }
	         else if(cdyt.equals("2")){
	        	 request.getSession().setAttribute("QVLL2", queryvaluesLists);//不动产权利查档查询条件
	         }
	         
	        // resultList = qlrInfoLists;
	         msg.setRows(qlrInfoLists);
 			 msg.setTotal(qlrInfoLists.size()); 				 
 			 msg.setMsg("数据读取成功");
		}catch(Exception e){
			e.printStackTrace();
		}
		return msg;
	}
	/**
	 * 将表格里的信息和根据权利人名称、身份证号从不动产库查出来的结果做数据比较，并填充
	 * @author hks
	 * @Date 2016/6/27 11:02
	 * @param cdyt  0:有无查档 、2：不动产权利（抵押、查封）查档
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	@SuppressWarnings({ "unchecked", "rawtypes", "unused" })
	@RequestMapping(value = "/matchdata/{cdyt}", method = RequestMethod.GET)
	public @ResponseBody Message matchData(@PathVariable("cdyt") String cdyt,HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException{
		Message msg = new Message();
		List<Map> queryVauleList = new ArrayList<Map>();
		
		//queryVauleList1 = (List<Map>) qvlist1.getRows();
		Message m = new Message();
		if(cdyt != null && cdyt.equals("1") || cdyt != null && cdyt.equals("0")){
			List<Map> toExcleLists = new ArrayList<Map>();
			queryVauleList = (List<Map>) request.getSession().getAttribute("QVLL1");//用于匹配数据的条件集合

			 //接着遍历查询权利人的房屋情况
			String querytype = request.getParameter("QUERYTYPE");
			String order = "";
			int total = 0;
			
			
			String statuString = "3";//已办
			String startString = request.getParameter("2016-02-18");//开始登记时间从2016-01-18开始
			String endString = request.getParameter("yyyy-MM-dd");//查到至今
			String actdefname = "";
			String prodefname = request.getParameter("转移");
			String staffanme = "";
			
			try {
				order = RequestHelper.getParam(request, "order");// 排序Order
				for(Map<String, String> qv : queryVauleList){
					List<Map> results = new ArrayList<Map>();
					String qlrmc = "";
					if(qv.get("QLR.ZJH") == null || "".equals(qv.get("QLR.ZJH"))){
						Map<String,String> map = new HashMap<String, String>();
						map.put("BH", qv.get("BH"));
						map.put("QLRMC", qv.get("QLR.QLRMC"));
						map.put("ZJHM", "");
						map.put("BZ", "信息不完整（缺少身份证号）");
						map.put("ZL", "无");
						total += 1;
						toExcleLists.add(map);
						continue;
					}
					
					if(cdyt.equals("0")){
						String zjh = qv.get("QLR.ZJH");
						qlrmc = qv.get("QLR.QLRMC");
						if(zjh != null)
						qv.put("QLR.ZJH", zjh);
						qv.put("CDYT", "0");
						qv.put("DY.ZL", "");
						qv.put("DY.BDCDYH", "");
						qv.put("QLR.QLRMC", "");
						qv.put("DYR.DYR", "");
						qv.put("QL.BDCQZH", "");
						qv.put("QLR.BDCQZHXH", "");
						qv.put("FSQL.CFWH", "");
						qv.put("DY.FWBM", "");
						qv.put("DY.FH", "");
						qv.put("QL.YWH", "");
						qv.put("XM.YWLSH", "");
						qv.put("DYZT", "0");
						qv.put("CFZT","0");
						qv.put("CXZT", "1");
						qv.put("DJSJ_Q", "");
						qv.put("DJSJ_Z", "");
						qv.put("DY.ZRZH", "");//栋号(自然幢号)
						qv.put("YYZT", "");
						qv.put("JGSX", "1");//结果筛选；"1"：全部显示，"2"：仅显示产权
						//results.clear();
						msg = queryService.queryHouse(qv, 1, 20, false, "3", "bdcdyid", "asc");
						total += msg.getTotal();
						results = (List<Map>) msg.getRows();
					}
					Message ms = new Message();
					
					if(results.size() > 0){
						
						/*for(int i = 0; i < results.size();i++){
							Map map = results.get(i);
							String qlrmc = (String) map.get("QLRMC");
							String sfzh = (String) map.get("ZJHM");
							String bdcfwzl = (String) map.get("ZL");
							String bdcdyh = (String) map.get("BDCDYH");
							String fh = (String) map.get("FH");
							if(i == results.size()-1){
								break;
							}
							if(qlrmc != null && sfzh != null && bdcfwzl != null && bdcdyh != null && fh !=null){
								for (int j = i+1; j < results.size()-i;j++) {
									Map map1 = results.get(j);
									String qlrmc1 = (String) map1.get("QLRMC");
									String sfzh1 = (String) map1.get("ZJHM");
									String bdcfwzl1 = (String) map1.get("ZL");
									String bdcdyh1 = (String) map.get("BDCDYH");
									String fh1 = (String) map.get("FH");
									if(qlrmc1 != null && sfzh1 != null && bdcfwzl1 != null && fh1 != null){
										if(qlrmc1.equals(qlrmc) && sfzh1.equals(sfzh) && bdcfwzl1.equals(bdcfwzl) && bdcdyh1.equals(bdcdyh) && fh1.equals(fh)){
											results.remove(j);
											total = total-1;
											System.out.println("删除了以下重复数据"+map1);
										}
										
									}
								}
							} 
						}*/
				 		
						for(int j = 0;j < results.size();j++){
							String qlr = (String) results.get(j).get("QLRMC");
							//String qlrmc = qv.get("QLR.QLRMC");
							String qlrzjh = qv.get("QLR.ZJH");
							List<BDCS_QLR_XZ> qlrinfo_xz = new ArrayList<BDCS_QLR_XZ>();
							if(qlrzjh != null && !qlrzjh.trim().isEmpty())
							qlrinfo_xz = baseCommonDao.getDataList(BDCS_QLR_XZ.class, "ZJH ='"+qlrzjh.trim()+"'");
							
							if(qlrinfo_xz.size() > 0){
								//String xzzl = (String) results.get(j).get("ZL");权利人现状层中户的坐落
								String gyfs = (String) qlrinfo_xz.get(0).getGYFS();
								
								if(gyfs == null || gyfs.trim().isEmpty()){
									results.get(j).put("SFQCQ", "");
								}else{
									if(gyfs.equals("0")){
										results.get(j).put("SFQCQ", "单独所有");
									}
									if(gyfs.equals("1")){
										results.get(j).put("SFQCQ", "共同所有");
									}
									if(gyfs.equals("2")){
										results.get(j).put("SFQCQ", "按份共有 （占有份额："+(String) qlrinfo_xz.get(0).getQLBL()+"）");
									}
									if(gyfs == null||gyfs.equals("")){
										results.get(j).put("SFQCQ", "");
									}

								}
							}
							
							results.get(j).put("BH", qv.get("BH"));
							results.get(j).put("QLRMC", qlrmc);
							results.get(j).put("ZJHM", qv.get("QLR.ZJH"));
							results.get(j).put("BZ", qv.get("BZ"));
							String ghytname = (String) results.get(j).get("GHYTname");
							System.out.println(qlrmc+"房屋用途："+ ghytname);
							if(ghytname != null){
								results.get(j).put("ZL",(String)results.get(j).get("ZL"));
								/**以下做法是针对柳州的，只有房屋规划用途是住宅的才显示坐落（ZL）
								/*if(ghytname.equals("住宅") || ghytname.equals("其它")){
									results.get(j).put("ZL",(String)results.get(j).get("ZL"));
								}else{
									results.get(j).put("ZL","");
								}*/
							}
						}
						for(Map<String,String> mp : results){
							toExcleLists.add(mp);
						}
					}else{
						Map<String,String> map = new HashMap<String, String>();
						map.put("BH", qv.get("BH"));
						map.put("QLRMC", qlrmc);
						map.put("ZJHM", qv.get("QLR.ZJH"));
						map.put("BZ", qv.get("BZ"));
						map.put("ZL", "无");

						if(cdyt.equals("1") || cdyt.equals("0")){
							ms = queryService.getZyProjectList(statuString, startString, endString, actdefname, order, prodefname, 
									staffanme, qlrmc, (String)qv.get("QLR.ZJH"));
							if(ms.getTotal() > 0){
								List<Map> zylists = new ArrayList<Map>();
								zylists = (List<Map>) ms.getRows();
								for (Map promap : zylists) {
									String proname = (String)promap.get("PRODEF_NAME");
									if(proname.indexOf("转移登记") != -1){
										map.put("PRONAME",proname);
									}else{
										map.put("PRONAME","");
									}
									Message msage = queryService.getQlInfo((String)promap.get("FILE_NUMBER"));
									List<Map> houses = new ArrayList<Map>();
									houses = (List<Map>) msage.getRows();
									if(msage.getTotal() > 0){
										for (Map house : houses) {
											String lszl = (String) house.get("ZL");//已转移出去的坐落
											if(lszl != null && !"".equals(lszl)){
												map.put("LSZL", lszl);
											}
											String ybdcqzh = (String) house.get("YBDCQZH");
											if(ybdcqzh != null){
												map.put("YBDCQZH", ybdcqzh);
											}
										}
									}

								}
							}
						}
						total += 1;
						toExcleLists.add(map);
					}
					
				}
				
				request.getSession().setAttribute("MRESULTS", toExcleLists);//匹配结果集
				m.setTotal(total);
				m.setRows(toExcleLists);
				
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				System.out.println("匹配出错！");
			}
			
		}
		List results = new ArrayList();
		if(cdyt != null && cdyt.equals("2")){
			Map<String, String> queryvalues = new HashMap<String, String>();
			queryVauleList = (List<Map>) request.getSession().getAttribute("QVLL2");//用于匹配数据的条件集合

			Integer page = 1;
			if (request.getParameter("page") != null) {
				page = Integer.parseInt(request.getParameter("page"));
			}
			
			Integer rows = 10;
			if (request.getParameter("rows") != null) {
				rows = Integer.parseInt(request.getParameter("rows"));
			}
			for(Map<String, String> qvp : queryVauleList){
				String bh = qvp.get("BH");
				String sort = RequestHelper.getParam(request, "sort");// 排序字段
				String order = RequestHelper.getParam(request, "order");// 排序Order
				String qlrmc = qvp.get("QLR.QLRMC");// 权利人名称
				String dyr = "";// 抵押人名称
				String qlrzjh = qvp.get("QLR.ZJH");// 权利人证件号
				String fwzl = qvp.get("ZL");// 房屋坐落
				//String bdcqzh = "";// 不动产权证号
				//String fwzl = qvp.get("ZL");// 房屋坐落
				String bdcqzh = qvp.get("QZH");// 不动产权证号
				
				//只取字母和数字进行匹配
				//bdcqzh = bdcqzh.replaceAll("[^a-z^A-Z^0-9]", "");
				
				String fwbm = RequestHelper.getParam(request, "FWBM");// 房屋编码
				String fh = RequestHelper.getParam(request, "FH");// 房号
				String ywbh = RequestHelper.getParam(request, "YWBH");// 业务编号
				String dyzt = "0";// 抵押状态
				String cfzt = "0";// 查封状态
				String cxzt = "1";// 查询状态
				String fwzt = "3";
				String bdcdyh=RequestHelper.getParam(request, "BDCDYH");
				String bdcqzhxh=RequestHelper.getParam(request, "BDCQZHXH");
				String cfwh=RequestHelper.getParam(request, "CFWH");
				//queryvalues.put("DY.ZL", fwzl);
				queryvalues.put("DY.BDCDYH", bdcdyh);
				queryvalues.put("QLR.QLRMC", qlrmc);
				queryvalues.put("DYR.DYR", dyr);
				queryvalues.put("QLR.ZJH", qlrzjh);
				queryvalues.put("QL.BDCQZH", bdcqzh);
				queryvalues.put("QLR.BDCQZHXH", bdcqzhxh);
				queryvalues.put("FSQL.CFWH", cfwh);
				queryvalues.put("DY.FWBM", fwbm);
				queryvalues.put("DY.FH", fh);
				queryvalues.put("QL.YWH", ywbh);
				queryvalues.put("DYZT", dyzt);
				queryvalues.put("CFZT", cfzt);
				queryvalues.put("CXZT", cxzt);
				queryvalues.put("SWCDBZ", "cd");
				
				
				Message ms = queryService.queryHouse(queryvalues, page, rows, false,fwzt,sort,order);
				
				List swResultList = new ArrayList();
				swResultList = ms.getRows();
				if(swResultList.size() == 0 ){
					Map map = new HashMap();
					map.put("BH", bh);
					map.put("QLRMC", qlrmc);
					map.put("ZJHM", qlrzjh);
					map.put("QZH", qvp.get("QZH"));
					map.put("ZL", fwzl);
					map.put("TDQZH", qvp.get("TDQZH"));
					map.put("BZ", qvp.get("BZ"));
					results.add(map);
				}
				else if(swResultList.size() > 0 ){
					for (int i = 0; i < swResultList.size(); i++) {
						
						Map map = (Map) swResultList.get(i);
						map.put("QLRMC", qlrmc);
						map.put("ZJHM", qlrzjh);
						map.put("QZH", qvp.get("QZH"));
						map.put("BH", bh);
						map.put("TDQZH", qvp.get("TDQZH"));
						map.put("BZ", qvp.get("BZ"));
						results.add(map);
					}
					
				}
				
			}
			m.setMsg("匹配成功"+results.size()+"条");
			m.setTotal(results.size());
			m.setRows(results);
		}
		return m;
		
	}
	
	/**
	 * 返回购房补贴查证明内容
	 * @author heks
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/plprintzm",method= RequestMethod.POST)    
	public @ResponseBody Message plPrntZM(HttpServletRequest request, HttpServletResponse response) throws Exception{
		Message msg = new Message();
		List<Object> searchResults = new ArrayList<Object>();
		
		List<Map>  results = (List<Map>) request.getSession().getAttribute("MRESULTS");
		
		for(int i=0;i < results.size();i++){
			List<Map> rlist = new ArrayList<Map>();
			
			String bh = (String) results.get(i).get("BH");
			String cdrmc = (String) results.get(i).get("QLRMC");
			String cdrzjh = (String) results.get(i).get("ZJHM");
			List<GX_CONFIG> cdjls = baseCommonDao.getDataList(GX_CONFIG.class, "CDRMC = '"+cdrmc+"' AND CDRZJH = '"+cdrzjh+"' and cdmd = '有无房产' order by cdtime desc");
			for(int j = 0;j < results.size();j++){
				String bh1 = (String) results.get(j).get("BH");
				if(cdjls.size() > 0){
					String cdbh = cdjls.get(0).getCdbh();
					String cdmd = cdjls.get(0).getCdmd();
					if(bh != null && bh1 != null && bh1.equals(bh)){
						Map<Object, Object> cmap = results.get(j);
						cmap.put("CDBH", cdbh);
						cmap.put("CDMD", cdmd);
						
						//加行政区名称
						String xzqhmc = ConfigHelper.getNameByValue("XZQHMC");
						cmap.put("XZQHMC", xzqhmc);
						rlist.add(cmap);
					}
				}
				
			}
			boolean check=false;
			for(int j=0;j<searchResults.size();j++){
				List<Map> children1=(List<Map>)searchResults.get(j);
				for(int k=0;k<children1.size();k++){
					Map cmap=children1.get(k);
					String bh2=String.valueOf(cmap.get("BH"));
					if(null!=bh&&bh2.equals(bh)){
						check=true;
					}
				}
			}
			if(!check){
				searchResults.add(rlist);
				
			}
		}
		Date currdate = new Date();
		String datestring = "";
		SimpleDateFormat  formatter  = new SimpleDateFormat("yyyy-MM-dd");
		try {    
	           datestring = formatter.format(currdate);  
	           
		} catch (Exception e) {    
	           e.printStackTrace();     
		}   
		msg.setMsg(datestring);
		msg.setTotal(searchResults.size());
		msg.setRows(searchResults);
		msg.setSuccess("返回成功！");
		return msg;
		
	}
	
	/**
	 * 返回权利查档证明内容
	 * @author heks
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/swplprintzm",method= RequestMethod.POST)    
	public @ResponseBody Message swplPrntZM(HttpServletRequest request, HttpServletResponse response) throws Exception{
		Message msg = new Message();

		List<Object> searchResults = new ArrayList<Object>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		
		List<Map> intoExclelists = new ArrayList<Map>();
		intoExclelists = (List<Map>) request.getSession().getAttribute("QVLL2");
		if(intoExclelists.size() > 0){
			for(Map map : intoExclelists){
				String qlrmc = (String) map.get("QLR.QLRMC");// 权利人名称
				String qlrzjh = (String) map.get("QLR.ZJH");// 权利人证件号
				String fwzl = (String) map.get("ZL");// 房屋坐落
				String bdcqzh = (String) map.get("QZH");// 不动产权证号
				String bh = (String) map.get("BH");// 编号

				List<GX_CONFIG> cdjls = baseCommonDao.getDataList(GX_CONFIG.class, "CDRMC = '"+qlrmc+"' AND CDRZJH = '"+qlrzjh+"' and cdmd = '不动产权利登记信息查询' order by cdtime desc");

				List<Map> rlist = new ArrayList<Map>();
				Map cmap = new HashMap();

				if(cdjls.size() > 0){
					String cdbh = cdjls.get(0).getCdbh();
					String cdmd = cdjls.get(0).getCdmd();

					cmap.put("CDBH", cdbh);
					cmap.put("CDMD", cdmd);
				}

				cmap.put("QLRMC", qlrmc);
				cmap.put("ZJHM", qlrzjh);
				cmap.put("ZL", fwzl);
				cmap.put("BDCQZH", bdcqzh);
				cmap.put("BH", bh);
				//加行政区名称
				String xzqhmc = ConfigHelper.getNameByValue("XZQHMC");
				cmap.put("XZQHMC", xzqhmc);
				
				List<Map> alists = queryService.getAllQlInfoByQlr(qlrmc, qlrzjh,bdcqzh);
				
				List<Map> list = new ArrayList<Map>();
				
				int dys = 0;//抵押数量
				int cfs = 0;//查封数量
				int ygs = 0;//预告数量
				int yys = 0;//异议数量
				if(alists.size() > 0){
					for(Map ql : alists){
						if(ql.get("QLLX").equals("23")){
							dys += 1;
						}
						if(ql.get("QLLX").equals("99")){
							cfs += 1;
						}
						if(ql.get("DJLX").equals("700")){
							ygs += 1;
						}
						if(ql.get("DJLX").equals("600")){
							yys += 1;
						}
						
						String djsj = "";
						if (ql.get("DJSJ") != null) {
							date.setTime(((Date) ql.get("DJSJ")).getTime());
							djsj = sdf.format(date);
						}
						ql.put("DJSJ", djsj);
						list.add(ql);
					}
				}
				cmap.put("QL", list);
				cmap.put("DY", dys);
				cmap.put("CF", cfs);
				cmap.put("YG", ygs);
				cmap.put("YY", yys);
				rlist.add(cmap);
				searchResults.add(rlist);
			}
		}

		Date currdate = new Date();
		String datestring = "";
		SimpleDateFormat  formatter  = new SimpleDateFormat("yyyy-MM-dd");
		try {    
			datestring = formatter.format(currdate);  

		} catch (Exception e) {    
			e.printStackTrace();    
		}   
		msg.setMsg(datestring);
		msg.setTotal(searchResults.size());
		msg.setRows(searchResults);
		msg.setSuccess("返回成功！");
		return msg;

	}
	
	/**
	 * 导出有房无地查档结果Excle表（每种查档有自己单独的查档结果Excle模版）
	 * @author hks
	 * @param request
	 * @param response
	 * contentType 指定下载文件的文件类型 —— application/octet-stream表示无限制
	 * bufferSize 下载文件的缓冲大小
	 */
	@SuppressWarnings({ "deprecation", "unused", "rawtypes", "unchecked"})
	@RequestMapping(value="/writeToExl",method= RequestMethod.POST)    
	public @ResponseBody String createExcel(HttpServletRequest request, HttpServletResponse response) throws Exception{
		String basePath = request.getRealPath("/") + "\\resources\\PDF";
		String outpath = "";
		String url = "";
		String tmpFullName = "";
		FileOutputStream outstream = null;
		
			outpath = basePath + "\\tmp\\qlrinfo_lz.xls";
			//下载后存放新Excle的路径
			url = request.getContextPath() + "\\resources\\PDF\\tmp\\qlrinfo_lz.xls";
		    outstream = new FileOutputStream(outpath);
		    //模版Excle表的路径
		    tmpFullName = request.getRealPath("/WEB-INF/jsp/wjmb/qlrinfo_lz.xls");
		    InputStream input = new FileInputStream(tmpFullName);
			HSSFWorkbook  wb = null;// 定义一个新的工作簿
	        wb = new HSSFWorkbook(input);
			HSSFSheet sheet=wb.getSheetAt(0);//选取Excle模版的第1个sheet
			
			// 创建单元格，并设置值表头 设置表头居中
			HSSFCellStyle style = wb.createCellStyle();
			style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
			
			Map<String,Integer> MapCol = new HashMap<String,Integer>();
			int rownum = 1;
			List<Map> intoExclelists = new ArrayList<Map>();
			intoExclelists = (List<Map>) request.getSession().getAttribute("MRESULTS");
				
			for(Map map : intoExclelists ){
				//获取每一个对象中的值
			   System.out.println(map);
			    HSSFRow row = sheet.createRow(rownum);//得到工作表的行
			    
			    HSSFCell Cell0 = row.createCell((short) 0);
			    Cell0.setCellValue( (String) map.get("BH"));
	            HSSFCell Cell1 = row.createCell((short) 1);
	            Cell1.setCellValue( (String) map.get("QLRMC")); 
	            
	            HSSFCell Cell2 = row.createCell((short) 2);
	            Cell2.setCellValue( (String) map.get("ZJHM"));
	            
	            HSSFCell Cell3 = row.createCell((short) 3);
	            Cell3.setCellValue( (String) map.get("ZL"));
	            
	            HSSFCell Cell4 = row.createCell((short) 4);
	            Cell4.setCellValue( (String) map.get("BDCQZH"));
	            
	            HSSFCell Cell5 = row.createCell((short) 5);
	            Cell5.setCellValue( (String) map.get("GHYTname"));
	            
	            HSSFCell Cell6 = row.createCell((short) 6);
	            Cell6.setCellValue( (String) map.get("SFQCQ"));
	            
	            HSSFCell Cell7 = row.createCell((short) 7);
	            Cell7.setCellValue( (String) map.get("LSZL"));
	            
	            HSSFCell Cell8 = row.createCell((short) 8);
	            Cell8.setCellValue( (String) map.get("YBDCQZH"));
	            
	            HSSFCell Cell9 = row.createCell((short) 9);
	            Cell9.setCellValue( (String) map.get("PRONAME"));
	             
	            HSSFCell Cell10 = row.createCell((short) 10);
	            Cell10.setCellValue( (String) map.get("BZ"));
	           	            
	            rownum += 1;
	            System.out.println("行数"+row.getRowNum());
			}
			 wb.write(outstream);
			 outstream.flush(); 
			 outstream.close();
		return url;

	}
	
	/**
	 * 导出三无查档的Excle表（每种查档有自己单独的查档结果Excle模版）
	 * @author hks
	 * @param request
	 * @param response
	 * contentType 指定下载文件的文件类型 —— application/octet-stream表示无限制
	 * bufferSize 下载文件的缓冲大小
	 */
	@SuppressWarnings({ "deprecation", "unused", "rawtypes", "unchecked"})
	@RequestMapping(value="/exportswexcel",method= RequestMethod.POST)    
	public @ResponseBody String exportExcel(HttpServletRequest request, HttpServletResponse response) throws Exception{

		String basePath = request.getRealPath("/") + "\\resources\\PDF";
		String outpath = "";
		String url = "";
		String tmpFullName = "";
		FileOutputStream outstream = null;

		outpath = basePath + "\\tmp\\exportQlInfoExl_lz.xls";
		// 下载后存放新Excle的路径
		url = request.getContextPath()
				+ "\\resources\\PDF\\tmp\\exportQlInfoExl_lz.xls";
		outstream = new FileOutputStream(outpath);
		// 模版Excle表的路径
		tmpFullName = request
				.getRealPath("/WEB-INF/jsp/wjmb/exportQlInfoExl_lz.xls");
		InputStream input = new FileInputStream(tmpFullName);
		HSSFWorkbook wb = null;// 定义一个新的工作簿
		wb = new HSSFWorkbook(input);
		HSSFSheet sheet = wb.getSheetAt(0);// 选取Excle模版的第1个sheet

		// 创建单元格，并设置值表头 设置表头居中
		HSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式

		Map<String, Integer> MapCol = new HashMap<String, Integer>();
		int rownum = 1;
		List<Map> intoExclelists = new ArrayList<Map>();
		intoExclelists = (List<Map>) request.getSession().getAttribute("QVLL2");// 用于匹配数据的条件集合

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		Date date = new Date();
		if (intoExclelists.size() > 0) {
			for (Map qvpmap : intoExclelists) {
				String qlrmc = (String) qvpmap.get("QLR.QLRMC");// 权利人名称
				String qlrzjh = (String) qvpmap.get("QLR.ZJH");// 权利人证件号
				String fwzl = (String) qvpmap.get("ZL");// 房屋坐落
				String bdcqzh = (String) qvpmap.get("QZH");// 不动产权证号
				String bh = (String) qvpmap.get("BH");// 序号
				String tdsyqzh = (String) qvpmap.get("TDQZH");// 土地使用权证号
				List<Map> alists = queryService
						.getAllQlInfoByQlr(qlrmc, qlrzjh,bdcqzh);
				if (alists.size() > 0) {
					for (Map map : alists) {
						if(!(map.get("DJLX").equals("100")) && !(map.get("DJLX").equals("600")) && !(map.get("DJLX").equals("700")) && !(map.get("DJLX").equals("800"))){
							continue;
						}
						if(map.get("QLLX").equals("3")){
							continue;
						}
						HSSFRow row = sheet.createRow(rownum + 4);// 得到工作表的行
						// 序号
						HSSFCell Cell0 = row.createCell((short) 0);
						Cell0.setCellValue(bh);
						// 权利人名称
						HSSFCell Cell1 = row.createCell((short) 1);
						Cell1.setCellValue(qlrmc);
						// 权利人证件号
						HSSFCell Cell2 = row.createCell((short) 2);
						Cell2.setCellValue(qlrzjh);
						// 不动产权证号
						HSSFCell Cell3 = row.createCell((short) 3);
						Cell3.setCellValue(bdcqzh);

						// 不动产坐落
						HSSFCell Cell4 = row.createCell((short) 4);
						Cell4.setCellValue(fwzl);

						// 土地使用权证号
						HSSFCell Cell5 = row.createCell((short) 5);
						Cell5.setCellValue(tdsyqzh);
						
						int dys = 0;//抵押数量
						int cfs = 0;//查封数量
						int ygs = 0;//预告数量
						int yys = 0;//异议数量
						if(map.get("QLLX").equals("4")){
							for(Map ql : alists){
								if(ql.get("QLLX").equals("23")){
									dys += 1;
								}
								if(ql.get("QLLX").equals("99")){
									cfs += 1;
								}
								if(ql.get("DJLX").equals("700")){
									ygs += 1;
								}
								if(ql.get("DJLX").equals("600")){
									yys += 1;
								}
							}
							
							// 抵押笔数
							HSSFCell Cell6 = row.createCell((short) 6);
							Cell6.setCellValue(dys == 0?"无": (dys+ "笔"));
							
							// 查封笔数
							HSSFCell Cell11 = row.createCell((short) 11);
							Cell11.setCellValue(cfs == 0?"无": (cfs+ "笔"));
							
							// 预告笔数
							HSSFCell Cell17 = row.createCell((short) 15);
							Cell17.setCellValue(ygs == 0?"无": (ygs+ "笔"));
							
							// 异议笔数
							HSSFCell Cell22 = row.createCell((short) 20);
							Cell22.setCellValue(yys == 0?"无": (yys+ "笔"));
						}
						
						// 抵押
						if (map.get("QLLX").equals("23")) {
							// 抵押权人
							HSSFCell Cell7 = row.createCell((short) 7);
							Cell7.setCellValue((String) map.get("QLRMC"));

							// 抵押面积
							HSSFCell Cell8 = row.createCell((short) 8);
							Cell8.setCellValue((String) map.get("BDCQZH"));

							/*// 抵押面积
							HSSFCell Cell9 = row.createCell((short) 9);
							Cell9.setCellValue(String.valueOf(map.get("DYMJ")));*/

							// 抵押金额
							HSSFCell Cell10 = row.createCell((short) 9);
							String bdbzzqse = String.valueOf(map.get("BDBZZQSE"));
							Cell10.setCellValue(bdbzzqse);

							// 抵押登记时间
							HSSFCell Cell11 = row.createCell((short) 10);
							String djsj = "";
							if (map.get("DJSJ") != null) {
								date.setTime(((Date) map.get("DJSJ")).getTime());
								djsj = sdf.format(date);
							}
							Cell11.setCellValue(djsj);
						}

						// 查封
						if (map.get("QLLX").equals("99")) {
							// 查封机关
							HSSFCell Cell13 = row.createCell((short) 12);
							Cell13.setCellValue((String) map.get("CFJG"));

							// 查封文号
							HSSFCell Cell14 = row.createCell((short) 13);
							Cell14.setCellValue((String) map.get("CFWH"));

							/*// 查封面积
							HSSFCell Cell15 = row.createCell((short) 15);
							Cell15.setCellValue((String) map.get("CFFW"));*/

							// 查封登记时间
							HSSFCell Cell16 = row.createCell((short) 14);
							String djsj = "";
							if (map.get("DJSJ") != null) {
								date.setTime(((Date) map.get("DJSJ")).getTime());
								djsj = sdf.format(date);
							}
							Cell16.setCellValue(djsj);
						}

						// 预告登记
						if (map.get("DJLX").equals("700")) {
							// 权利人
							HSSFCell Cell18 = row.createCell((short) 16);
							Cell18.setCellValue((String) map.get("QLRMC"));

							// 证号
							HSSFCell Cell19 = row.createCell((short) 17);
							Cell19.setCellValue((String) map.get("BDCQZH"));

							// 预告登记时间
							HSSFCell Cell20 = row.createCell((short) 18);
							String djsj = "";
							if (map.get("DJSJ") != null) {
								date.setTime(((Date) map.get("DJSJ")).getTime());
								djsj = sdf.format(date);
							}
							Cell20.setCellValue(djsj);

							// 登记类型
							HSSFCell Cell21 = row.createCell((short) 19);
							Cell21.setCellValue("预告登记");
						}
						// 异议登记
						if (map.get("DJLX").equals("600")) {
							// 申请人
							HSSFCell Cell23 = row.createCell((short) 21);
							Cell23.setCellValue((String) map.get("QLRMC"));
							
							// 异议登记时间
							HSSFCell Cell24 = row.createCell((short) 22);
							String djsj = "";
							if (map.get("DJSJ") != null) {
								date.setTime(((Date) map.get("DJSJ")).getTime());
								djsj = sdf.format(date);
							}
							Cell24.setCellValue(djsj);
						}else{
							// 申请人
							HSSFCell Cell23 = row.createCell((short) 21);
							Cell23.setCellValue("无");
							
							// 登记类型
							HSSFCell Cell24 = row.createCell((short) 22);
							Cell24.setCellValue("无");
						}
						rownum++;
					}
				} else {
					HSSFRow row = sheet.createRow(rownum + 4);// 得到工作表的行
					// 序号
					HSSFCell Cell0 = row.createCell((short) 0);
					Cell0.setCellValue(bh);
					// 权利人名称
					HSSFCell Cell1 = row.createCell((short) 1);
					Cell1.setCellValue(qlrmc);
					// 权利人证件号
					HSSFCell Cell2 = row.createCell((short) 2);
					Cell2.setCellValue(qlrzjh);
					// 不动产权证号
					HSSFCell Cell3 = row.createCell((short) 3);
					Cell3.setCellValue(bdcqzh); 

					// 不动产坐落
					HSSFCell Cell4 = row.createCell((short) 4);
					Cell4.setCellValue(fwzl);

					// 土地使用权证号
					HSSFCell Cell5 = row.createCell((short) 5);
					Cell5.setCellValue(tdsyqzh);
					rownum++;
				}
			}
		}
		
		wb.write(outstream);
		outstream.flush(); 
		outstream.close();
		return url;
	}
	@RequestMapping(value = "/delLog", method = RequestMethod.POST)
	public @ResponseBody void DeleteLog(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException{
		String ids = RequestHelper.getParam(request, "ids");
		String[] id_arr = ids.split(",");
		for (String id : id_arr) {
			queryService.delLog(id);
		}
	}
	
	//鹰潭不动产单元查询全市监管，宗地导出
		@SuppressWarnings({ "unchecked", "rawtypes" })
		@RequestMapping(value = "/landQueryDownloadyt", method = RequestMethod.GET)
		public @ResponseBody String GetLandQueryListDownloadyt(HttpServletRequest request, HttpServletResponse response) throws IOException {
			String querytype = request.getParameter("QUERYTYPE-ZD");
			boolean iflike = false;
			if (querytype != null && querytype.toLowerCase().equals("true")) {
				iflike = true;
			}

			Map<String, String> queryvalues = new HashMap<String, String>();
			String sort = RequestHelper.getParam(request, "sort");// 排序字段
			String order = RequestHelper.getParam(request, "order");// 排序Order
			String qlrmc = RequestHelper.getParam(request, "QLRMC-ZD");// 权利人名称
			String qlrzjh = RequestHelper.getParam(request, "ZJH-ZD");// 权利人证件号
			String tdzl = RequestHelper.getParam(request, "ZL-ZD");// 土地坐落
			String bdcqzh = RequestHelper.getParam(request, "BDCQZH-ZD");// 不动产权证号
			String ywbh = RequestHelper.getParam(request, "YWBH-ZD");// 业务编号
			String dyzt = RequestHelper.getParam(request, "DYZT-ZD");// 抵押状态
			String cfzt = RequestHelper.getParam(request, "CFZT-ZD");// 查封状态
			String fwzt=RequestHelper.getParam(request,"TDZT-ZD");
			String bdcdyh=RequestHelper.getParam(request, "BDCDYH-ZD");
			String zddm=RequestHelper.getParam(request, "ZDDM-ZD");
			String xzqh=RequestHelper.getParam(request, "XZQH");
			//获取当前时间
				Date date=new Date();
				DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm");
				String time=format.format(date);
			queryvalues.put("DY.ZL", tdzl);
			queryvalues.put("DY.BDCDYH", bdcdyh);
			queryvalues.put("DY.ZDDM", zddm);
			queryvalues.put("QLR.QLRMC", qlrmc);
			queryvalues.put("QLR.ZJH", qlrzjh);
			queryvalues.put("QL.BDCQZH", bdcqzh);
			queryvalues.put("QL.YWH", ywbh);
			queryvalues.put("DYZT", dyzt);
			queryvalues.put("CFZT", cfzt);
			YwLogUtil.addYwLog("土地查询功能，查询", ConstValue.SF.YES.Value,ConstValue.LOG.SEARCH);
			Message message =queryService.queryLandJG(queryvalues, 1, Integer.MAX_VALUE, iflike,fwzt,sort,order,xzqh, null, null, null, null, null, null, null, null, null, null,null);
			List<Map> rows=(List<Map>) message.getRows();

			String basePath = request.getRealPath("/") + "\\resources\\PDF";
			String outpath = "";
			String url = "";
			FileOutputStream outstream = null;
			outpath = basePath + "\\tmp\\TDTZ.xls";
			url = request.getContextPath() + "\\resources\\PDF\\tmp\\TDTZ.xls";
		    outstream = new FileOutputStream(outpath); 

		    String tplFullName = request.getRealPath("/WEB-INF/jsp/wjmb/land.xls");
			    InputStream input = new FileInputStream(tplFullName);
				HSSFWorkbook  wb = null;// 定义一个新的工作簿
				wb = new HSSFWorkbook(input);
				Sheet sheet = wb.getSheetAt(0);
				Map<String,Integer> MapCol = new HashMap<String,Integer>();
				MapCol.put("序号", 0);
				MapCol.put("土地座落", 1);
				MapCol.put("不动产单元号", 2);
				MapCol.put("土地类型", 3);
				MapCol.put("权利人", 4);
				MapCol.put("证件号码", 5);
				MapCol.put("权证号", 6);
				MapCol.put("抵押状态", 7);
				MapCol.put("抵押人", 8);
				MapCol.put("抵押期限", 9);
				MapCol.put("查封状态", 10);
				MapCol.put("查封机关", 11);
				MapCol.put("查封文号", 12);
				MapCol.put("查封期限", 13);
				MapCol.put("异议状态", 14);
				MapCol.put("查询日期", 15);
	            int rownum = 2;
				for(Map r:rows){
							 HSSFRow row = (HSSFRow)sheet.createRow(rownum);
					         HSSFCell Cell0 = row.createCell(MapCol.get("序号"));
					         Cell0.setCellValue(rownum-1);
					         HSSFCell Cell1 = row.createCell(MapCol.get("土地座落"));
					         Cell1.setCellValue(StringHelper.FormatByDatatype(r.get("ZL")));
					         HSSFCell Cell2 = row.createCell(MapCol.get("不动产单元号"));
					         Cell2.setCellValue(StringHelper.FormatByDatatype(r.get("BDCDYH")));
					         HSSFCell Cell3 = row.createCell(MapCol.get("土地类型"));
					         Cell3.setCellValue(StringHelper.FormatByDatatype(r.get("BDCDYLXMC")));
					         HSSFCell Cell5 = row.createCell(MapCol.get("权利人"));
					         Cell5.setCellValue(StringHelper.FormatByDatatype(r.get("QLRMC")));
					      
					         HSSFCell Cell6 = row.createCell(MapCol.get("证件号码"));
					         Cell6.setCellValue(StringHelper.FormatByDatatype(r.get("ZJHM")));
					         HSSFCell Cell7 = row.createCell(MapCol.get("权证号"));
					         Cell7.setCellValue(StringHelper.FormatByDatatype(r.get("BDCQZH")));
					        
					         HSSFCell Cell12 = row.createCell(MapCol.get("抵押状态"));
					         Cell12.setCellValue(StringHelper.FormatByDatatype(r.get("DYZT")));
					         HSSFCell Cell13 = row.createCell(MapCol.get("抵押人"));
					         Cell13.setCellValue(StringHelper.FormatByDatatype(r.get("DYR")));
					         HSSFCell Cell14 = row.createCell(MapCol.get("抵押期限"));
					         Cell14.setCellValue(StringHelper.FormatByDatatype(r.get("DYQX")));
					         HSSFCell Cell15 = row.createCell(MapCol.get("查封状态"));
					         Cell15.setCellValue(StringHelper.FormatByDatatype(r.get("CFZT")));
					         HSSFCell Cell16 = row.createCell(MapCol.get("查封机关"));
					         Cell16.setCellValue(StringHelper.FormatByDatatype(r.get("CFJG")));
					         HSSFCell Cell17 = row.createCell(MapCol.get("查封文号"));
					         Cell17.setCellValue(StringHelper.FormatByDatatype(r.get("CFWH")));
					         HSSFCell Cell18 = row.createCell(MapCol.get("查封期限"));
					         Cell18.setCellValue(StringHelper.FormatByDatatype(r.get("CFQX")));
					         HSSFCell Cell19 = row.createCell(MapCol.get("异议状态"));
					         Cell19.setCellValue(StringHelper.FormatByDatatype(r.get("YYZT")));
					         HSSFCell Cell20 = row.createCell(MapCol.get("查询日期"));
					         Cell20.setCellValue(StringHelper.FormatByDatatype(time));

					         rownum += 1;
					  }
	 	    
				    wb.write(outstream); 
				    outstream.flush(); 
				    outstream.close();
				    outstream = null;
				    return url;
		}


	//鹰潭全市监管不动产单元查询，房屋
		@SuppressWarnings({ "deprecation", "rawtypes", "unchecked" })
		@RequestMapping(value = "/houseQueryDownloadyt", method = RequestMethod.GET)
		public   @ResponseBody String HouseQueryListDownloadyt(HttpServletRequest request, HttpServletResponse response) throws Exception{
			String querytype = request.getParameter("QUERYTYPE");
			boolean iflike = false;
			if (querytype != null && querytype.toLowerCase().equals("true")) {
				iflike = true;
			}

			Map<String, String> queryvalues = new HashMap<String, String>();
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
			String dyzt = RequestHelper.getParam(request, "DYZT");// 抵押状态
			String cfzt = RequestHelper.getParam(request, "CFZT");// 查封状态
			String fwzt=RequestHelper.getParam(request,"FWZT");
			String bdcdyh=RequestHelper.getParam(request, "BDCDYH");
			String bdcqzhxh=RequestHelper.getParam(request, "BDCQZHXH");
			String xzqh=RequestHelper.getParam(request, "XZQH");
			//获取当前时间
			Date date=new Date();
			DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm");
			String time=format.format(date);
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
			YwLogUtil.addYwLog("房屋查询功能：查询", ConstValue.SF.YES.Value,ConstValue.LOG.SEARCH);
			Message message= queryService.queryHouseJG(queryvalues, 1, Integer.MAX_VALUE, iflike,fwzt,sort,order,xzqh,null,null,null,null,null,null,null,null,null,null,null,null,null);
			List<Map> rows=(List<Map>) message.getRows();

			String basePath = request.getRealPath("/") + "\\resources\\PDF";
			String outpath = "";
			String url = "";
			FileOutputStream outstream = null;
			outpath = basePath + "\\tmp\\FWTZ.xls";
			url = request.getContextPath() + "\\resources\\PDF\\tmp\\FWTZ.xls";
		    outstream = new FileOutputStream(outpath); 

		    String tplFullName = request.getRealPath("/WEB-INF/jsp/wjmb/house.xls");
			    InputStream input = new FileInputStream(tplFullName);
				HSSFWorkbook  wb = null;// 定义一个新的工作簿
				wb = new HSSFWorkbook(input);
				Sheet sheet = wb.getSheetAt(0);
				Map<String,Integer> MapCol = new HashMap<String,Integer>();
				MapCol.put("序号", 0);
				MapCol.put("房屋座落", 1);
				MapCol.put("不动产单元号", 2);
				MapCol.put("房屋类型", 3);
				MapCol.put("房号", 4);
				MapCol.put("权利人", 5);
				MapCol.put("权利类型", 6);
				MapCol.put("证件号", 6);
				MapCol.put("权证号", 7);
				MapCol.put("规划用途", 8);
				MapCol.put("建筑面积", 9);
				MapCol.put("套内建筑面积", 10);
				MapCol.put("分摊面积", 11);
				MapCol.put("抵押状态", 12);
				MapCol.put("抵押人", 13);
				MapCol.put("抵押期限", 14);
				MapCol.put("查封状态", 15);
				MapCol.put("查封机关", 16);
				MapCol.put("查封文号", 17);
				MapCol.put("查封期限", 18);
				MapCol.put("异议状态", 19);
				MapCol.put("查询日期", 20);
	            int rownum = 2;
				for(Map r:rows){
							 HSSFRow row = (HSSFRow)sheet.createRow(rownum);
					         HSSFCell Cell0 = row.createCell(MapCol.get("序号"));
					         Cell0.setCellValue(rownum-1);
					         HSSFCell Cell1 = row.createCell(MapCol.get("房屋座落"));
					         Cell1.setCellValue(StringHelper.FormatByDatatype(r.get("ZL")));
					         HSSFCell Cell2 = row.createCell(MapCol.get("不动产单元号"));
					         Cell2.setCellValue(StringHelper.FormatByDatatype(r.get("BDCDYH")));
					         HSSFCell Cell3 = row.createCell(MapCol.get("房屋类型"));
					         Cell3.setCellValue(StringHelper.FormatByDatatype(r.get("BDCDYLXMC")));
					         HSSFCell Cell4 = row.createCell(MapCol.get("房号"));
					         Cell4.setCellValue(StringHelper.FormatByDatatype(r.get("FH")));
					         HSSFCell Cell5 = row.createCell(MapCol.get("权利人"));
					         Cell5.setCellValue(StringHelper.FormatByDatatype(r.get("QLRMC")));
					         HSSFCell Cell6 = row.createCell(MapCol.get("证件号"));
					         Cell6.setCellValue(StringHelper.FormatByDatatype(r.get("ZJHM")));
					         HSSFCell Cell7 = row.createCell(MapCol.get("权证号"));
					         Cell7.setCellValue(StringHelper.FormatByDatatype(r.get("BDCQZH")));
					         HSSFCell Cell8 = row.createCell(MapCol.get("规划用途"));
					         Cell8.setCellValue(StringHelper.FormatByDatatype(r.get("GHYTname")));
					         HSSFCell Cell9 = row.createCell(MapCol.get("建筑面积"));
					         Cell9.setCellValue(StringHelper.FormatByDatatype(r.get("SCJZMJ")));
					         HSSFCell Cell10 = row.createCell(MapCol.get("套内建筑面积"));
					         Cell10.setCellValue(StringHelper.FormatByDatatype(r.get("SCTNJZMJ")));
					         HSSFCell Cell11 = row.createCell(MapCol.get("分摊面积"));
					         Cell11.setCellValue(StringHelper.FormatByDatatype(r.get("SCFTJZMJ")));
					         HSSFCell Cell12 = row.createCell(MapCol.get("抵押状态"));
					         Cell12.setCellValue(StringHelper.FormatByDatatype(r.get("DYZT")));
					         HSSFCell Cell13 = row.createCell(MapCol.get("抵押人"));
					         Cell13.setCellValue(StringHelper.FormatByDatatype(r.get("DYR")));
					         HSSFCell Cell14 = row.createCell(MapCol.get("抵押期限"));
					         Cell14.setCellValue(StringHelper.FormatByDatatype(r.get("DYQX")));
					         HSSFCell Cell15 = row.createCell(MapCol.get("查封状态"));
					         Cell15.setCellValue(StringHelper.FormatByDatatype(r.get("CFZT")));
					         HSSFCell Cell16 = row.createCell(MapCol.get("查封机关"));
					         Cell16.setCellValue(StringHelper.FormatByDatatype(r.get("CFJG")));
					         HSSFCell Cell17 = row.createCell(MapCol.get("查封文号"));
					         Cell17.setCellValue(StringHelper.FormatByDatatype(r.get("CFWH")));
					         HSSFCell Cell18 = row.createCell(MapCol.get("查封期限"));
					         Cell18.setCellValue(StringHelper.FormatByDatatype(r.get("CFQX")));
					         HSSFCell Cell19 = row.createCell(MapCol.get("异议状态"));
					         Cell19.setCellValue(StringHelper.FormatByDatatype(r.get("YYZT")));
					         HSSFCell Cell16_1 = row.createCell(MapCol.get("权利类型"));
					         Cell16_1.setCellValue(StringHelper.FormatByDatatype(r.get("QLLX")));
					         HSSFCell Cell20 = row.createCell(MapCol.get("查询日期"));
					         Cell20.setCellValue(StringHelper.FormatByDatatype(time));
					         rownum += 1;
					  }
	 		
			    
				    wb.write(outstream); 
				    outstream.flush(); 
				    outstream.close();
				    outstream = null;
				    return url;
		}
		

	    //qlrmc,dyr,qlrzjh,fwzl,bdcqzh,fwbm,fh,ywbh,dyzt,cfzt,cxzt,fwzt,bdcdyh,bdcqzhxh,cfwh,querytype,""
	public  String getfwlogmsg(String qlrmc,String dyr,String qlrzjh,String fwzl,String bdcqzh,String fwbm,String fh,String ywbh,String dyzt,String cfzt,String cxzt,String fwzt,String bdcdyh,String bdcqzhxh,String cfwh,String querytype,String zddm,String tdzt){
				String sql="";
				//查询内容
				sql=sql+((qlrmc+"").length()==0?"":"权利人名称："+qlrmc+"/");
				//活动类型
				sql=sql+((qlrzjh+"").length()==0?"":"身份证"+qlrzjh+"/");
				sql=sql+((bdcqzh+"").length()==0?"":"不动产权证号"+bdcqzh+"/");
				sql=sql+((fwzl+"").length()>0?"坐落:"+fwzl+"/":"");
				sql=sql+((dyr+"").length()>0?"抵押人:"+dyr+"/":"");
				sql=sql+((ywbh+"").length()>0?"业务编号:"+ywbh+"/":"");
				sql=sql+((fwbm+"").length()>0?"房屋编码:"+fwbm+"/":"");
				sql=sql+((fh+"").length()>0?"房号:"+fh+"/":"");
				sql=sql+((bdcdyh+"").length()>0?"单元号:"+bdcdyh+"/":"");
				sql=sql+((cfwh+"").length()>0?"查封文号:"+cfwh+"/":"");
				if("0".equals(dyzt)){
					dyzt="全部";
				}else if("1".equals(dyzt)){
					dyzt="未抵押";
				}else{
					dyzt="抵押中";
				}
				sql=sql+((dyzt+"").length()>0?"抵押状态:"+dyzt+"/":"");
				if("0".equals(cfzt)){
					cfzt="全部";
				}else if("1".equals(cfzt)){
					cfzt="未被查封";
				}else{
					cfzt="已被查封";
				}
				sql=sql+((cfzt+"").length()>0?"查封状态:"+cfzt+"/":"");
				
				if("0".equals(fwzt)){
					fwzt="全部";
				}else if("1".equals(fwzt)){
					fwzt="现房";
				}else{
					fwzt="期房";
				}
				sql=sql+((fwzt+"").length()>0?"房屋状态:"+fwzt+"/":"");
				
				if("1".equals(cxzt)){
					cxzt="全部";
				}else if("2".equals(cxzt)){
					cxzt="现房";
				}
				sql=sql+((cxzt+"").length()>0?"查询状态:"+cxzt+"/":"");
				if (querytype != null && querytype.toLowerCase().equals("true")) {
					sql=sql+("模糊查询/");
				}
				sql=sql+((zddm+"").length()>0?"宗地代码:"+zddm+"/":"");
				if("3".equals(tdzt)){
					tdzt="全部";
				}else if("1".equals(tdzt)){
					tdzt="使用权宗地";
				}else if("2".equals(tdzt)){
					tdzt="所有权宗地";
				}
				sql=sql+((tdzt+"").length()>0?"宗地状态:"+tdzt+"/":"");
				return sql;
			}
			
	/**
	 * 查询房屋信息及批量上传分户图
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "/housebyzrzbdcdyh", method = RequestMethod.POST)
	public @ResponseBody Message GetHouseList(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
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
		String fwzl = RequestHelper.getParam(request, "ZL");// 房屋坐落
		String fwbm = RequestHelper.getParam(request, "FWBM");// 房屋编码
		String fh = RequestHelper.getParam(request, "FH");// 房号
		String bdcdyh=RequestHelper.getParam(request, "BDCDYH");
		String sort = RequestHelper.getParam(request, "sort");// 排序字段
		String order = RequestHelper.getParam(request, "order");// 排序Order
		queryvalues.put("DY.ZL", fwzl);
		queryvalues.put("DY.BDCDYH", bdcdyh);
		queryvalues.put("DY.FWBM", fwbm);
		queryvalues.put("DY.FH", fh);
		YwLogUtil.addYwLog("房屋查询功能：查询", ConstValue.SF.YES.Value,ConstValue.LOG.SEARCH);
		return queryService.queryHouseByZRZBDCDYH(queryvalues, page, rows, iflike,sort,order);
	}
	
	/**
	 * 查询指认图
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "/getHouseListbyTime", method = RequestMethod.POST)
	public @ResponseBody Message getHouseList(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		Integer page = 1;
		if (request.getParameter("page") != null) {
			page = Integer.parseInt(request.getParameter("page"));
		}
		String querytype = request.getParameter("QUERYTYPE");
		boolean iflike = false;
		if (querytype != null && querytype.toLowerCase().equals("true")) {
			iflike = true;
		}
		Integer rows = 20;
		if (request.getParameter("rows") != null) {
			rows = Integer.parseInt(request.getParameter("rows"));
		}
		Map<String, String> queryvalues = new HashMap<String, String>();
		String sort = RequestHelper.getParam(request, "sort");// 排序字段
		String fwzl = RequestHelper.getParam(request, "ZL");// 房屋坐落
		String fwbm = RequestHelper.getParam(request, "FWBM");// 房屋编码
		String bdcdyh = RequestHelper.getParam(request, "BDCDYH");
		String ddsj_q = RequestHelper.getParam(request, "DDSJ_Q");
		String ddsj_z = RequestHelper.getParam(request, "DDSJ_Z");
		queryvalues.put("DY.ZL", fwzl);
		queryvalues.put("DY.BDCDYH", bdcdyh);
		queryvalues.put("DY.FWBM", fwbm);
		queryvalues.put("DY.DDSJ_Q", ddsj_q);
		queryvalues.put("DY.DDSJ_Z", ddsj_z);
		YwLogUtil.addYwLog("指认图房屋查询功能：查询", ConstValue.SF.YES.Value,ConstValue.LOG.SEARCH);
		return queryService.queryHouseByTime(queryvalues, page, rows, iflike,sort);
	}
	
	/**
	 * 批量限制服务
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "/queryandplxz", method = RequestMethod.POST)
	public @ResponseBody Message GETQuery(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
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
		String fwzl = RequestHelper.getParam(request, "ZL");// 房屋坐落
		String fwbm = RequestHelper.getParam(request, "FWBM");// 房屋编码
		String fh = RequestHelper.getParam(request, "FH");// 房号
		String bdcdyh = RequestHelper.getParam(request, "BDCDYH");
		String zrzh = RequestHelper.getParam(request, "ZRZH");
		String qlrmc = RequestHelper.getParam(request, "QLRMC");
		String qzh = RequestHelper.getParam(request, "BDCQZH");
		String zjh = RequestHelper.getParam(request, "ZJH");
		String sort = RequestHelper.getParam(request, "sort");// 排序字段
		String order = RequestHelper.getParam(request, "order");// 排序Order
		String hzt = RequestHelper.getParam(request, "hzt");
		queryvalues.put("DY.ZL", fwzl);
		queryvalues.put("DY.BDCDYH", bdcdyh);
		queryvalues.put("DY.FWBM", fwbm);
		queryvalues.put("DY.FH", fh);
		queryvalues.put("DY.ZRZH", zrzh);
		queryvalues.put("QL.BDCQZH", qzh);
		queryvalues.put("QLR.QLRMC", qlrmc);
		queryvalues.put("QLR.ZJH", zjh);
		YwLogUtil.addYwLog("房屋查询功能：查询", ConstValue.SF.YES.Value,ConstValue.LOG.SEARCH);
		return queryService.queryAndPLXZ(queryvalues, page, rows, iflike, sort, order, hzt);
	}
	
	@RequestMapping(value = "/blacklist", method = RequestMethod.POST)
	public @ResponseBody Message blacklist(@RequestParam("file") MultipartFile file,
			HttpServletRequest request, HttpServletResponse response,
			Object command) {
		return queryService.blacklist(file,request);
	}
	
	@RequestMapping(value = "/getblacklist", method = RequestMethod.POST)
	public @ResponseBody Message Getblacklist(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		return queryService.Getblacklist(request);
	}
	
	@RequestMapping(value = "/deleteblacklist", method = RequestMethod.POST)
	public @ResponseBody Message Delblacklist(HttpServletRequest request, HttpServletResponse response) {
		return queryService.Delblacklist(request);
	}
	
	@RequestMapping(value = "/addblacklist", method = RequestMethod.POST)
	public @ResponseBody Message Addblacklist(HttpServletRequest request, HttpServletResponse response) {
		return queryService.Addblacklist(request);
	}
	
	@RequestMapping(value = "/updateblacklist", method = RequestMethod.POST)
	public @ResponseBody Message Updateblacklist(HttpServletRequest request, HttpServletResponse response) {
		return queryService.Updateblacklist(request);
	}
	
	@RequestMapping(value = "/duplicate", method = RequestMethod.POST)
	public @ResponseBody Message Duplicate(HttpServletRequest request, HttpServletResponse response) {
		return queryService.Duplicate(request);
	}
	
	/**
	 * 
	 * @Description: 根据qlid和不动产单元类型获取不动产单元信息
	 * @author 孙海豹
	 * @param bdcdyid
	 *            不动产单元ID
	 * @param bdcdylx
	 *            不动产单元类型
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/hinfobyqlid/{qlid}/{bdcdylx}", method = RequestMethod.GET)
	public @ResponseBody List<Map> GetHInfoByQlid(@PathVariable("qlid") String qlid, @PathVariable("bdcdylx") String bdcdylx, HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException {
		    List<Map> list = queryService.queryUnitInfo(qlid, bdcdylx);
		return list;
	}

	/**
	 * @Description: 通过zrzbdcdyid获取该自然幢所有qlrmx，
	 * @Title: GetQlrmcByZrz
	 * @Author: zhaomengfan
	 * @Date: 2017年5月15日下午2:43:40
	 * @param request
	 * @param response
	 * @return
	 * @return Map<String,String>
	 */
	@RequestMapping(value = "/qlrxm/{bdcdylx}", method = RequestMethod.POST)
	public @ResponseBody Map<String,String> GetQlrmcByZrz(@PathVariable("bdcdylx") String bdcdylx,HttpServletRequest request, HttpServletResponse response) {
		String zrzbdcdyid = "";
		 try {
			zrzbdcdyid = RequestHelper.getParam(request, "zrzbdcdyid");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return queryService.GetQlrmcByZrz(zrzbdcdyid,bdcdylx);
	}
	
	/** 根据户的BDCDYID获取所在自然幢的BDCDYID （房屋查询图形定位）
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "/getZRZID", method = RequestMethod.GET)
	public @ResponseBody String getZrzBdcdyid(HttpServletRequest request,HttpServletResponse response) throws UnsupportedEncodingException {
		String hbdcdyid = RequestHelper.getParam(request, "param");
		return queryService.getZrzBdcdyid(hbdcdyid);
	}
	
	/**
	 * 海域查询
	 * @Title: GetSeaQueryList
	 * @author:heks
	 * @date：2017年5月22日 下午5:38:52
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "/seaQuery", method = RequestMethod.POST)
	public @ResponseBody Message GetSeaQueryList(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
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
		String sort = RequestHelper.getParam(request, "sort");// 排序字段
		String order = RequestHelper.getParam(request, "order");// 排序Order
		String qlrmc = RequestHelper.getParam(request, "QLRMC");// 权利人名称
		String dyr = RequestHelper.getParam(request, "DYR");// 抵押人名称
		String qlrzjh = RequestHelper.getParam(request, "ZJH");// 权利人证件号
		String fwzl = RequestHelper.getParam(request, "ZL");// 坐落
		String bdcqzh = RequestHelper.getParam(request, "BDCQZH");// 不动产权证号
		/*String fwbm = RequestHelper.getParam(request, "FWBM");// 房屋编码
		String fh = RequestHelper.getParam(request, "FH");// 房号*/		
		String ywbh = RequestHelper.getParam(request, "YWBH");// 业务编号
		String dyzt = RequestHelper.getParam(request, "DYZT");// 抵押状态
		String cfzt = RequestHelper.getParam(request, "CFZT");// 查封状态
		String cxzt = RequestHelper.getParam(request, "CXZT");// 查询状态
		String yyzt = RequestHelper.getParam(request, "YYZT");// 异议状态
		String fwzt=RequestHelper.getParam(request,"FWZT");
		String bdcdyh=RequestHelper.getParam(request, "BDCDYH");
		String bdcqzhxh=RequestHelper.getParam(request, "BDCQZHXH");
		String cfwh=RequestHelper.getParam(request, "CFWH");
		String djsj_q=RequestHelper.getParam(request, "DJSJ_Q");
		String djsj_z=RequestHelper.getParam(request, "DJSJ_Z");
		String dh=RequestHelper.getParam(request, "DH");//栋号
		String jgsx=RequestHelper.getParam(request, "JGSX");//结果筛选；"1"：全部显示，"2"：仅显示产权
		String searchstates=RequestHelper.getParam(request, "SEARCHSTATES");//区域筛选
		queryvalues.put("DY.ZL", fwzl);
		queryvalues.put("DY.BDCDYH", bdcdyh);
		queryvalues.put("QLR.QLRMC", qlrmc);
		queryvalues.put("DYR.DYR", dyr);
		queryvalues.put("QLR.ZJH", qlrzjh);
		queryvalues.put("QL.BDCQZH", bdcqzh);
		queryvalues.put("QLR.BDCQZHXH", bdcqzhxh);
		queryvalues.put("FSQL.CFWH", cfwh);
		/*queryvalues.put("DY.FWBM", fwbm);
		queryvalues.put("DY.FH", fh);*/
		//queryvalues.put("QL.YWH", ywbh);
		queryvalues.put("XM.YWLSH", ywbh);
		queryvalues.put("DYZT", dyzt);
		queryvalues.put("CFZT", cfzt);
		queryvalues.put("CXZT", cxzt);
		queryvalues.put("DJSJ_Q", djsj_q);
		queryvalues.put("DJSJ_Z", djsj_z);
		queryvalues.put("DY.ZRZH", dh);//栋号(自然幢号)
		queryvalues.put("YYZT", yyzt);
		queryvalues.put("JGSX", jgsx);//结果筛选；"1"：全部显示，"2"：仅显示产权
		queryvalues.put("SEARCHSTATES", searchstates);//区域筛选
		YwLogUtil.addYwLog("海域查询功能：查询", ConstValue.SF.YES.Value,ConstValue.LOG.SEARCH);
		return queryService.querySea(queryvalues, page, rows, iflike,sort,order);
	}

	/*
	 * 泸州状态信息
	 */
	@RequestMapping(value = "/housestatus", method = RequestMethod.POST)
	public @ResponseBody String HouseStatusQuery(HttpServletRequest request, HttpServletResponse response) {
			double startTime = System.currentTimeMillis();
			String srcJson;
			String resultJson =null;
			try {
				srcJson = HttpRequest.getRequestPostStr(request);
//				java.util.Enumeration em = request.getParameterNames();
//				 while (em.hasMoreElements()) {
//				    String name = (String) em.nextElement();
//				    String value = request.getParameter(name);
//				    System.out.println(name);
//				    System.out.println(value);
//				}
			if(srcJson!=null){
				resultJson=queryServiceluzhou.queryhousestate(srcJson,"0").toString();
				System.out.println("获取户状态用时:" + (System.currentTimeMillis() - startTime));

			}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return resultJson;
	}
	
	@RequestMapping(value = "/personal/property", method = RequestMethod.GET)
	public @ResponseBody Message PropertyInfoQuery(HttpServletRequest request, HttpServletResponse response) {
		Message msg = new Message();
		int pageIndex = 1;
		int pageSize = 10;
		try {
			String name = RequestHelper.getParam(request, "name").trim();
			String zjh = RequestHelper.getParam(request, "zjh").trim();
			if(StringHelper.isEmpty(name) && StringHelper.isEmpty(name)) {
				msg.setTotal(0);
				msg.setWhether(false);
				msg.setMsg("姓名和证件号都不能为空！");
				return msg;
			}
			msg = queryService.getPropertyInfoQuery(name,zjh,pageIndex,pageSize);
		} catch (UnsupportedEncodingException e) {
			msg.setSuccess("false");
			msg.setMsg("系统出现未知错误！");
			e.printStackTrace();
		}
		return msg;
		
	}
	
	/**
	 * (房屋或纯宗地)单元信息、权利信息、附属权利信息、权利人信息
	 * @author liangc
	 * @date 2018-11-09 10:02:30
	 * @param djdyid
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "/qlinfoPrint/{djdyid}/fsqlinfo", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> GetInfo_fsqlInfo(@PathVariable("djdyid") String djdyid, HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		Map<String, Object> list = queryService.QLInfoFSQLInfo(djdyid);
		return list;
	}
	
	/**
	 * 交易一体，房屋查询    
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/houseQueryJYYT", method = RequestMethod.POST)
	public @ResponseBody Message GetHouseQueryJYYTList(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		Map params = StringHelper.transToMAP(request.getParameterMap()); 
		return queryService.queryHouseJYYT(params);
	}
	
	@SuppressWarnings({ "deprecation", "rawtypes", "unchecked" })
	@RequestMapping(value = "/houseQueryDownloadJYYT", method = RequestMethod.GET)
	public   @ResponseBody String HouseQueryListDownloadJYYT(HttpServletRequest request, HttpServletResponse response) throws Exception{
		String querytype = request.getParameter("QUERYTYPE");
		boolean iflike = false;
		if (querytype != null && querytype.toLowerCase().equals("true")) {
			iflike = true;
		}

		Map<String, String> queryvalues = new HashMap<String, String>();
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
		String dyzt = RequestHelper.getParam(request, "DYZT");// 抵押状态
		String cfzt = RequestHelper.getParam(request, "CFZT");// 查封状态
		String cxzt = RequestHelper.getParam(request, "CXZT");// 查询状态
		String fwzt=RequestHelper.getParam(request,"FWZT");
		String bdcdyh=RequestHelper.getParam(request, "BDCDYH");
		String bdcqzhxh=RequestHelper.getParam(request, "BDCQZHXH");
		String cfwh=RequestHelper.getParam(request, "CFWH");
		String djsj_q=RequestHelper.getParam(request, "DJSJ_Q");
		String djsj_z=RequestHelper.getParam(request, "DJSJ_Z");
		String dh=RequestHelper.getParam(request, "DH");//栋号
		String jgsx=RequestHelper.getParam(request, "JGSX");//结果筛选；"1"：全部显示，"2"：仅显示产权
		String searchstates=RequestHelper.getParam(request, "SEARCHSTATES");//区域筛选
		String res=getfwlogmsg(qlrmc,dyr,qlrzjh,fwzl,bdcqzh,fwbm,fh,ywbh,dyzt,cfzt,cxzt,fwzt,bdcdyh,bdcqzhxh,cfwh,querytype,"","");
		YwLogUtil.addYwLog("房屋查询结果导出功能："+res, ConstValue.SF.YES.Value,ConstValue.LOG.PRINT);
		Map params = StringHelper.transToMAP(request.getParameterMap()); 
		params.put("page", 1);
		params.put("rows", 1000);//默认只允许导出1000
		Message message = queryService.queryHouseJYYT(params);
		List<Map> rows=(List<Map>) message.getRows();
		
		//导出
		String basePath = request.getRealPath("/") + "\\resources\\PDF";
		String outpath = "";
		String url = "";
		FileOutputStream outstream = null;
		outpath = basePath + "\\tmp\\housequeryresult.xls";
		url = request.getContextPath() + "\\resources\\PDF\\tmp\\housequeryresult.xls";
	    outstream = new FileOutputStream(outpath); 

	    String tplFullName = request.getRealPath("/WEB-INF/jsp/wjmb/house1.xls");
	    InputStream input = new FileInputStream(tplFullName);
		HSSFWorkbook  wb = null;// 定义一个新的工作簿
		wb = new HSSFWorkbook(input);
		HSSFSheet sheet = wb.getSheetAt(0);
		Map<String,Integer> MapCol = new HashMap<String,Integer>();
		
		//所选字段
		String selectFields=RequestHelper.getParam(request, "selectFields");
		JSONArray jsonArray = JSONArray.fromObject(selectFields);
		List<Map> mapListJson = (List) jsonArray;
		//获取标题行格式
		int lastrow = sheet.getLastRowNum();
		HSSFRow head = sheet.getRow(lastrow - 1);
		HSSFCellStyle style = sheet.getRow(lastrow).getCell(0).getCellStyle();
		short height = sheet.getRow(lastrow).getHeight();
		//添加标题行
		HSSFRow title = (HSSFRow)sheet.createRow(1);
		title.setHeight(height);
		//序号
		HSSFCell Cell = title.createCell(0);
		 Cell.setCellValue("序号") ;
		 Cell.setCellStyle(style);
		 MapCol.put("序号", 0);
		//content
		for (int i = 0; i < mapListJson.size(); i++) {
			 HSSFCell title_Cell = title.createCell(i+1);
			 title_Cell.setCellValue(mapListJson.get(i).get("name").toString()) ;
			 MapCol.put(mapListJson.get(i).get("name").toString(), i+1);
			 title_Cell.setCellStyle(style);
		}
		//添加内容行
		int rownum = 2;
		for(Map r:rows){
			 HSSFRow row = (HSSFRow)sheet.createRow(rownum);
	         HSSFCell Cell0 = row.createCell(MapCol.get("序号"));
	         Cell0.setCellValue(rownum-1);
	         for (int i = 0; i < mapListJson.size(); i++) {
				 String name = mapListJson.get(i).get("name").toString();
				 String value = mapListJson.get(i).get("value").toString();
	        	 HSSFCell Content_Cell = row.createCell(MapCol.get(name));
	        	 Content_Cell.setCellValue(StringHelper.FormatByDatatype(r.get(value)));
			}
	        rownum += 1;
	  }
	
	    wb.write(outstream); 
	    outstream.flush(); 
	    outstream.close();
	    outstream = null;
	    return url;
	}
	
	
	@SuppressWarnings({ "deprecation", "rawtypes", "unchecked" })
	@RequestMapping(value = "/houseQueryDownloadByPageJYYT", method = RequestMethod.GET)
	public   @ResponseBody String houseQueryDownloadByPageJYYT(HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		Integer _page = 1;
		if (request.getParameter("page") != null) {
			_page = Integer.parseInt(request.getParameter("page"));
		}
		Integer _rows = 10;
		if (request.getParameter("rows") != null) {
			_rows = Integer.parseInt(request.getParameter("rows"));
		}
		
		String querytype = request.getParameter("QUERYTYPE");
		boolean iflike = false;
		if (querytype != null && querytype.toLowerCase().equals("true")) {
			iflike = true;
		}

		Map<String, String> queryvalues = new HashMap<String, String>();
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
		String dyzt = RequestHelper.getParam(request, "DYZT");// 抵押状态
		String cfzt = RequestHelper.getParam(request, "CFZT");// 查封状态
		String cxzt = RequestHelper.getParam(request, "CXZT");// 查询状态
		String fwzt=RequestHelper.getParam(request,"FWZT");
		String bdcdyh=RequestHelper.getParam(request, "BDCDYH");
		String bdcqzhxh=RequestHelper.getParam(request, "BDCQZHXH");
		String cfwh=RequestHelper.getParam(request, "CFWH");
		String djsj_q=RequestHelper.getParam(request, "DJSJ_Q");
		String djsj_z=RequestHelper.getParam(request, "DJSJ_Z");
		String dh=RequestHelper.getParam(request, "DH");//栋号
		String jgsx=RequestHelper.getParam(request, "JGSX");//结果筛选；"1"：全部显示，"2"：仅显示产权
		String searchstates=RequestHelper.getParam(request, "SEARCHSTATES");//区域筛选
		String res=getfwlogmsg(qlrmc,dyr,qlrzjh,fwzl,bdcqzh,fwbm,fh,ywbh,dyzt,cfzt,cxzt,fwzt,bdcdyh,bdcqzhxh,cfwh,querytype,"","");
		YwLogUtil.addYwLog("房屋查询结果导出功能："+res, ConstValue.SF.YES.Value,ConstValue.LOG.PRINT);
		Map params = StringHelper.transToMAP(request.getParameterMap()); 
		Message message= queryService.queryHouseJYYT(params);
		List<Map> rows=(List<Map>) message.getRows();

		//导出
		String basePath = request.getRealPath("/") + "\\resources\\PDF";
		String outpath = "";
		String url = "";
		FileOutputStream outstream = null;
		outpath = basePath + "\\tmp\\housequeryresult.xls";
		url = request.getContextPath() + "\\resources\\PDF\\tmp\\housequeryresult.xls";
	    outstream = new FileOutputStream(outpath); 

	    String tplFullName = request.getRealPath("/WEB-INF/jsp/wjmb/house1.xls");
	    InputStream input = new FileInputStream(tplFullName);
		HSSFWorkbook  wb = null;// 定义一个新的工作簿
		wb = new HSSFWorkbook(input);
		HSSFSheet sheet = wb.getSheetAt(0);
		Map<String,Integer> MapCol = new HashMap<String,Integer>();
		
		//所选字段
		String selectFields=RequestHelper.getParam(request, "selectFields");
		JSONArray jsonArray = JSONArray.fromObject(selectFields);
		List<Map> mapListJson = (List) jsonArray;
		//获取标题行格式
		int lastrow = sheet.getLastRowNum();
		HSSFRow head = sheet.getRow(lastrow - 1);
		HSSFCellStyle style = sheet.getRow(lastrow).getCell(0).getCellStyle();
		short height = sheet.getRow(lastrow).getHeight();
		//添加标题行
		HSSFRow title = (HSSFRow)sheet.createRow(1);
		title.setHeight(height);
		//序号
		HSSFCell Cell = title.createCell(0);
		 Cell.setCellValue("序号") ;
		 Cell.setCellStyle(style);
		 MapCol.put("序号", 0);
		//content
		for (int i = 0; i < mapListJson.size(); i++) {
			 HSSFCell title_Cell = title.createCell(i+1);
			 title_Cell.setCellValue(mapListJson.get(i).get("name").toString()) ;
			 MapCol.put(mapListJson.get(i).get("name").toString(), i+1);
			 title_Cell.setCellStyle(style);
		}
		//添加内容行
		int rownum = 2;
		for(Map r:rows){
			 HSSFRow row = (HSSFRow)sheet.createRow(rownum);
	         HSSFCell Cell0 = row.createCell(MapCol.get("序号"));
	         Cell0.setCellValue(rownum-1);
	         for (int i = 0; i < mapListJson.size(); i++) {
				 String name = mapListJson.get(i).get("name").toString();
				 String value = mapListJson.get(i).get("value").toString();
	        	 HSSFCell Content_Cell = row.createCell(MapCol.get(name));
	        	 Content_Cell.setCellValue(StringHelper.FormatByDatatype(r.get(value)));
								
			}
	        rownum += 1;
	  }
	
	    
	    wb.write(outstream); 
	    outstream.flush(); 
	    outstream.close();
	    outstream = null;
	    return url;
	}
	
	@SuppressWarnings({ "deprecation", "rawtypes", "unchecked" })
	@RequestMapping(value = "/landQueryDownloadByPage", method = RequestMethod.GET)
	public   @ResponseBody String landQueryDownloadByPage(HttpServletRequest request, HttpServletResponse response) throws Exception{
		
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
		String sort = RequestHelper.getParam(request, "sort");// 排序字段
		String order = RequestHelper.getParam(request, "order");// 排序Order
		String qlrmc = RequestHelper.getParam(request, "QLRMC");// 权利人名称
		String qlrzjh = RequestHelper.getParam(request, "ZJH");// 权利人证件号
		String tdzl = RequestHelper.getParam(request, "ZL");// 土地坐落
		String bdcqzh = RequestHelper.getParam(request, "BDCQZH");// 不动产权证号
		String ywbh = RequestHelper.getParam(request, "YWBH");// 业务编号
		String dyzt = RequestHelper.getParam(request, "DYZT");// 抵押状态
		String cfzt = RequestHelper.getParam(request, "CFZT");// 查封状态
		String cxzt = RequestHelper.getParam(request, "CXZT");// 查询状态
		String fwzt=RequestHelper.getParam(request,"TDZT");
		String bdcdyh=RequestHelper.getParam(request, "BDCDYH");
		String zddm=RequestHelper.getParam(request, "ZDDM");
		String cfwh=RequestHelper.getParam(request, "CFWH");
		String djsj_q=RequestHelper.getParam(request, "DJSJ_Q");
		String djsj_z=RequestHelper.getParam(request, "DJSJ_Z");
		String djh = RequestHelper.getParam(request, "DJH");
		String qlxz = RequestHelper.getParam(request, "QLXZ");
		String tdyt = RequestHelper.getParam(request, "TDYT");
		String fdzk = RequestHelper.getParam(request, "FDZK");
		String jgsx=RequestHelper.getParam(request, "JGSX");//结果筛选；"1"：全部显示，"2"：仅显示产权
		queryvalues.put("DY.ZL", tdzl);
		queryvalues.put("DY.BDCDYH", bdcdyh);
		queryvalues.put("DY.ZDDM", zddm);
		queryvalues.put("QLR.QLRMC", qlrmc);
		queryvalues.put("QLR.ZJH", qlrzjh);
		queryvalues.put("QL.BDCQZH", bdcqzh);
		queryvalues.put("QL.YWH", ywbh);
		queryvalues.put("FSQL.CFWH", cfwh);
		queryvalues.put("DYZT", dyzt);
		queryvalues.put("CFZT", cfzt);
		queryvalues.put("DJSJ_Q", djsj_q);
		queryvalues.put("DJSJ_Z", djsj_z);
		queryvalues.put("DJH", djh);
		queryvalues.put("CXZT", cxzt);
		queryvalues.put("QLXZ", qlxz);
		queryvalues.put("TDYT", tdyt);
		queryvalues.put("FDZK", fdzk);
		queryvalues.put("JGSX", jgsx);//结果筛选；"1"：全部显示，"2"：仅显示产权
		//YwLogUtil.addYwLog("土地查询功能，查询", ConstValue.SF.YES.Value,ConstValue.LOG.SEARCH);
		Message message= queryService.queryLand(queryvalues, page, rows, iflike,fwzt,sort,order);
		List<Map> allrows=(List<Map>) message.getRows();

		//导出
		String basePath = request.getRealPath("/") + "\\resources\\PDF";
		String outpath = "";
		String url = "";
		FileOutputStream outstream = null;
		outpath = basePath + "\\tmp\\landqueryresult.xls";
		url = request.getContextPath() + "\\resources\\PDF\\tmp\\landqueryresult.xls";
	    outstream = new FileOutputStream(outpath); 

	    String tplFullName = request.getRealPath("/WEB-INF/jsp/wjmb/land1.xls");
	    InputStream input = new FileInputStream(tplFullName);
		HSSFWorkbook  wb = null;// 定义一个新的工作簿
		wb = new HSSFWorkbook(input);
		HSSFSheet sheet = wb.getSheetAt(0);
		Map<String,Integer> MapCol = new HashMap<String,Integer>();
		
		//所选字段
		String selectFields=RequestHelper.getParam(request, "selectFields");
		JSONArray jsonArray = JSONArray.fromObject(selectFields);
		List<Map> mapListJson = (List) jsonArray;
		//获取标题行格式
		int lastrow = sheet.getLastRowNum();
		HSSFRow head = sheet.getRow(lastrow - 1);
		HSSFCellStyle style = sheet.getRow(lastrow).getCell(0).getCellStyle();
		short height = sheet.getRow(lastrow).getHeight();
		//添加标题行
		HSSFRow title = (HSSFRow)sheet.createRow(1);
		title.setHeight(height);
		//序号
		HSSFCell Cell = title.createCell(0);
		 Cell.setCellValue("序号") ;
		 Cell.setCellStyle(style);
		 MapCol.put("序号", 0);
		//content
		for (int i = 0; i < mapListJson.size(); i++) {
			 HSSFCell title_Cell = title.createCell(i+1);
			 title_Cell.setCellValue(mapListJson.get(i).get("name").toString()) ;
			 MapCol.put(mapListJson.get(i).get("name").toString(), i+1);
			 title_Cell.setCellStyle(style);
		}
		//添加内容行
		int rownum = 2;
		for(Map r:allrows){
			 HSSFRow row = (HSSFRow)sheet.createRow(rownum);
	         HSSFCell Cell0 = row.createCell(MapCol.get("序号"));
	         Cell0.setCellValue(rownum-1);
	         for (int i = 0; i < mapListJson.size(); i++) {
				 String name = mapListJson.get(i).get("name").toString();
				 String value = mapListJson.get(i).get("value").toString();
	        	 HSSFCell Content_Cell = row.createCell(MapCol.get(name));
	        	 Content_Cell.setCellValue(StringHelper.FormatByDatatype(r.get(value)));
								
			}
	        rownum += 1;
	  }
	
	    
	    wb.write(outstream); 
	    outstream.flush(); 
	    outstream.close();
	    outstream = null;
	    return url;
	}
	
	/**
	 * 通过接口获取家庭婚姻信息
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/loadFamilyData", method = RequestMethod.POST)
	public   @ResponseBody Message loadFamilyData(HttpServletRequest request, HttpServletResponse response) 
			throws Exception{
		String name = RequestHelper.getParam(request, "sqrxm");
		String cardnum = RequestHelper.getParam(request, "zjh");
		String sex = "1";
		//name,cardnum,sex
		if(cardnum.length() == 18){
			//性别 0：女，1：男
			String sextype = cardnum.substring(cardnum.length()-2, cardnum.length()-1);
			if(Integer.parseInt(sextype)%2 == 0){
				//是双数为女性
				sex = "0";
			}else{
				//单数为男性
				sex = "1";
			}
		}
		Message msg = new Message(); 
		String url = ConfigHelper.getNameByValue("FamilyAPIPath");
		String pdatas = "{'datum': [{'name': '"+name+"','cardnum':'"+cardnum+"','sex':'"+sex+"'}],'head': "
				+ "{'datacount': 1,'requestdate': '"+DateUtil.getDateTime("yyyy-MM-dd HH:mm:ss", new Date())+"','requestcode':'ZRZY450000-05',"
				+ "'user':'ZRZYtoMarry','password':'query20190313'}}";
		String datas = WSDLUtil.callService(url,pdatas);
		System.out.println(datas);
		YwLogUtil.addYwLog(Global.getCurrentUserName()+"使用婚姻接口获取婚姻信息，传递参数："+pdatas+";返回结果:"+datas, ConstValue.SF.YES.Value,ConstValue.LOG.SEARCH);
		if(!"".equals(datas) && datas != null){
			JSONArray jsons = JSONArray.fromObject(datas);
			List<Map> redata = (List<Map>) jsons;
			msg.setRows(redata);
			msg.setSuccess("true");
			msg.setMsg("查询成功");
		}else{
			msg.setSuccess("false");
			msg.setMsg("查询失败");
		}
		return msg;
	}
	
}
