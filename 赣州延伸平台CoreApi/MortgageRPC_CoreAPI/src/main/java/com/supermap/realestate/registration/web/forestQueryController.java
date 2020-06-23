package com.supermap.realestate.registration.web;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.supermap.realestate.registration.ViewClass.QueryClass.QLInfo;
import com.supermap.realestate.registration.model.BDCS_DYXZ;
import com.supermap.realestate.registration.service.forestQueryService;
import com.supermap.realestate.registration.util.ConstValue;
import com.supermap.realestate.registration.util.RequestHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.realestate.registration.util.YwLogUtil;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import com.supermap.wisdombusiness.web.Message;

@Controller
@RequestMapping("/forestQuery")
@Component("forestQueryController")
public class forestQueryController {
	
	@Autowired
	private forestQueryService queryService;
	
	@Autowired
	private CommonDao baseCommonDao;

	/** 林地信息查询
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "/forestQuery", method = RequestMethod.GET)
	public @ResponseBody Message GetForestQueryList(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
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
		String tdzl = RequestHelper.getParam(request, "ZL");// 坐落
		String xdm = RequestHelper.getParam(request, "XDM");// 小地名
		String bdcqzh = RequestHelper.getParam(request, "BDCQZH");// 不动产权证号
		String ywbh = RequestHelper.getParam(request, "YWBH");// 业务编号
		String dyzt = RequestHelper.getParam(request, "DYZT");// 抵押状态
		String cfzt = RequestHelper.getParam(request, "CFZT");// 查封状态
		String cxzt = RequestHelper.getParam(request, "CXZT");// 查询状态
		String bdcdyh = RequestHelper.getParam(request, "BDCDYH");// 不动产单元号
		String cfwh = RequestHelper.getParam(request, "CFWH");// 查封文号
		String djsj_q = RequestHelper.getParam(request, "DJSJ_Q");// 登记时间起始
		String djsj_z = RequestHelper.getParam(request, "DJSJ_Z");// 登记时间终止
		queryvalues.put("DY.ZL", tdzl);
		queryvalues.put("DY.XDM", xdm);
		queryvalues.put("DY.BDCDYH", bdcdyh);
		queryvalues.put("QLR.QLRMC", qlrmc);
		queryvalues.put("QLR.ZJH", qlrzjh);
		queryvalues.put("QL.BDCQZH", bdcqzh);
		queryvalues.put("QL.YWH", ywbh);
		queryvalues.put("FSQL.CFWH", cfwh);
		queryvalues.put("DYZT", dyzt);
		queryvalues.put("CFZT", cfzt);
		queryvalues.put("CXZT", cxzt);
		queryvalues.put("DJSJ_Q", djsj_q);
		queryvalues.put("DJSJ_Z", djsj_z);
		YwLogUtil.addYwLog("林地查询功能，查询", ConstValue.SF.YES.Value,ConstValue.LOG.SEARCH);
		return queryService.queryForest(queryvalues, page, rows, iflike, sort, order);
	}
	
	/** 林地查询导出功能
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@SuppressWarnings({ "rawtypes", "deprecation", "unchecked" })
	@RequestMapping(value = "/forestQueryDownload", method = RequestMethod.GET)
	public @ResponseBody String GetForestQueryListDownload(HttpServletRequest request, HttpServletResponse response) throws IOException {
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
		String zl = RequestHelper.getParam(request, "ZL");// 坐落
		String xdm = RequestHelper.getParam(request, "XDM");// 坐落
		String bdcqzh = RequestHelper.getParam(request, "BDCQZH");// 不动产权证号
		String ywbh = RequestHelper.getParam(request, "YWBH");// 业务编号
		String dyzt = RequestHelper.getParam(request, "DYZT");// 抵押状态
		String cfzt = RequestHelper.getParam(request, "CFZT");// 查封状态
		String cxzt = RequestHelper.getParam(request, "CXZT");// 查询状态
		String bdcdyh = RequestHelper.getParam(request, "BDCDYH");
		String djsj_q = RequestHelper.getParam(request, "DJSJ_Q");
		String djsj_z = RequestHelper.getParam(request, "DJSJ_Z");
		String cfwh = RequestHelper.getParam(request, "CFWH");
		queryvalues.put("DY.ZL", zl);
		queryvalues.put("DY.XDM", xdm);
		queryvalues.put("DY.BDCDYH", bdcdyh);
		queryvalues.put("QLR.QLRMC", qlrmc);
		queryvalues.put("QLR.ZJH", qlrzjh);
		queryvalues.put("QL.BDCQZH", bdcqzh);
		queryvalues.put("QL.YWH", ywbh);
		queryvalues.put("FSQL.CFWH", cfwh);
		queryvalues.put("DYZT", dyzt);
		queryvalues.put("CFZT", cfzt);
		queryvalues.put("CXZT", cxzt);
		queryvalues.put("DJSJ_Q", djsj_q);
		queryvalues.put("DJSJ_Z", djsj_z);
		Message message = queryService.queryForest(queryvalues, 1, Integer.MAX_VALUE, iflike,sort,order);
		List<Map> rows=(List<Map>) message.getRows();

		String basePath = request.getRealPath("/") + "\\resources\\PDF";
		String outpath = "";
		String url = "";
		FileOutputStream outstream = null;
		outpath = basePath + "\\tmp\\forestqueryresult.xls";
		url = request.getContextPath() + "\\resources\\PDF\\tmp\\forestqueryresult.xls";
	    outstream = new FileOutputStream(outpath); 

	    String tplFullName = request.getRealPath("/WEB-INF/jsp/wjmb/forest.xls");
		    InputStream input = new FileInputStream(tplFullName);
			HSSFWorkbook  wb = null;// 定义一个新的工作簿
			wb = new HSSFWorkbook(input);
			Sheet sheet = wb.getSheetAt(0);
			Map<String,Integer> MapCol = new HashMap<String,Integer>();
			MapCol.put("序号", 0);
			MapCol.put("坐落", 1);
			MapCol.put("不动产单元号", 2);
			MapCol.put("权利人", 3);
			MapCol.put("证件号码", 4);
			MapCol.put("权证号", 5);
			MapCol.put("抵押状态", 6);
			MapCol.put("抵押人", 7);
			MapCol.put("抵押期限", 8);
			MapCol.put("查封状态", 9);
			MapCol.put("查封机关", 10);
			MapCol.put("查封文号", 11);
			MapCol.put("查封期限", 12);
			MapCol.put("异议状态", 13);
			MapCol.put("登簿时间", 14);
            int rownum = 2;
			for(Map r:rows){
				 HSSFRow row = (HSSFRow)sheet.createRow(rownum);
		         HSSFCell Cell0 = row.createCell(MapCol.get("序号"));
		         Cell0.setCellValue(rownum-1);
		         HSSFCell Cell1 = row.createCell(MapCol.get("坐落"));
		         Cell1.setCellValue(StringHelper.FormatByDatatype(r.get("ZL")));
		         HSSFCell Cell2 = row.createCell(MapCol.get("不动产单元号"));
		         Cell2.setCellValue(StringHelper.FormatByDatatype(r.get("BDCDYH")));
		         HSSFCell Cell3 = row.createCell(MapCol.get("权利人"));
		         Cell3.setCellValue(StringHelper.FormatByDatatype(r.get("QLRMC")));
		         HSSFCell Cell4 = row.createCell(MapCol.get("证件号码"));
		         Cell4.setCellValue(StringHelper.FormatByDatatype(r.get("ZJHM")));
		         HSSFCell Cell5 = row.createCell(MapCol.get("权证号"));
		         Cell5.setCellValue(StringHelper.FormatByDatatype(r.get("BDCQZH")));
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
		         HSSFCell Cell20 = row.createCell(MapCol.get("登簿时间"));
		         Cell20.setCellValue(StringHelper.FormatByDatatype(r.get("DJSJ")));
		         rownum += 1;
		   }
			    wb.write(outstream); 
			    outstream.flush(); 
			    outstream.close();
			    outstream = null;
			    return url;
	}
	
	/** 根据不动产单元ID和不动产单元类型获取不动产单元信息
	 * @param bdcdyid : 不动产单元ID
	 * @param bdcdylx : 不动产单元类型
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/forestinfo/{bdcdyid}/{bdcdylx}", method = RequestMethod.GET)
	public @ResponseBody List<Map> GetForestInfo(@PathVariable("bdcdyid") String bdcdyid, @PathVariable("bdcdylx") String bdcdylx, HttpServletRequest request,HttpServletResponse response) throws UnsupportedEncodingException {
		List<Map> list = queryService.getForestInfo(bdcdyid, bdcdylx);
		return list;
	}
	
	/** 根据不动产单元ID获取权利信息
	 * @param bdcdyid : 不动产单元ID
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
	
	/** 根据不动产单元ID获取抵押信息
	 * @param bdcdyid : 不动产单元ID
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/dydjlist_xz/{bdcdyid}", method = RequestMethod.GET)
	public @ResponseBody List<List<Map>> GetDYDJList_XZ(@PathVariable("bdcdyid") String bdcdyid, HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		List<List<Map>> list = queryService.GetDYDJList_XZ(bdcdyid);
		return list;
	}
	
	/** 根据不动产单元ID获取查封信息
	 * @param bdcdyid : 不动产单元ID
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/cfdjlist_xz/{bdcdyid}", method = RequestMethod.GET)
	public @ResponseBody List<List<Map>> GetCFDJList_XZ(@PathVariable("bdcdyid") String bdcdyid, HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		List<List<Map>> list = queryService.GetCFDJList_XZ(bdcdyid);
		return list;
	}
	
	/** 根据不动产单元ID获取异议登记信息
	 * @param bdcdyid : 不动产单元ID
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/yydjlist_xz/{bdcdyid}", method = RequestMethod.GET)
	public @ResponseBody List<List<Map>> GetYYDJList_XZ(@PathVariable("bdcdyid") String bdcdyid, HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		List<List<Map>> list = queryService.GetYYDJList_XZ(bdcdyid);
		return list;
	}
	
	/** 根据不动产单元ID获取其他限制信息
	 * @param bdcdyid : 不动产单元ID
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "/dyxzlist_xz/{bdcdyid}", method = RequestMethod.GET)
	public @ResponseBody List<BDCS_DYXZ> GetDYXZList_XZ(@PathVariable("bdcdyid") String bdcdyid, HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		List<BDCS_DYXZ> list = queryService.GetDYXZList_XZ(bdcdyid);
		return list;
	}
	
	/** 根据不动产单元ID获取获取所有权集合、抵押权集合 、查封登记集合、异议登记集合
	 * @param bdcdyid : 不动产单元ID
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "qllist/{bdcdyid}", method = RequestMethod.GET)
	public @ResponseBody List<List<Map>> GetQLList(@PathVariable("bdcdyid") String bdcdyid, HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		List<List<Map>> list = queryService.GetQLList(bdcdyid);
		return list;
	}
	
	/** 根据权利ID获取所有权信息
	 * @param qlid
	 * @param djzt
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "qlinfo/{qlid}/{djzt}", method = RequestMethod.GET)
	public @ResponseBody QLInfo GetSYQInfo(@PathVariable("qlid") String qlid,@PathVariable("djzt") String djzt, HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		QLInfo ql = queryService.GetQLInfo(qlid,djzt);
		return ql;
	}
	
	/** 根据ID获取单元限制信息
	 * @param id
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "qlinfo/{id}/dyxz", method = RequestMethod.GET)
	public @ResponseBody BDCS_DYXZ GetDYXZInfo(@PathVariable("id") String id, HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		BDCS_DYXZ ql = queryService.GetDYXZInfo(id);
		return ql;
	}
}
