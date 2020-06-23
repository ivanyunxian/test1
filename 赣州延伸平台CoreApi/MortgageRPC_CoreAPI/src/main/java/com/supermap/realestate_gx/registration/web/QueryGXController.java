package com.supermap.realestate_gx.registration.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
import org.apache.poi.hssf.util.Region;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import com.supermap.realestate.registration.model.BDCS_BDCCFXX;
import com.supermap.realestate.registration.model.BDCS_UPLOADFILES;
import com.supermap.realestate.registration.util.ConfigHelper;
import com.supermap.realestate.registration.util.ConstValue;
import com.supermap.realestate.registration.util.Global;
import com.supermap.realestate.registration.util.RequestHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.realestate.registration.util.YwLogUtil;
import com.supermap.realestate_gx.registration.service.QueryService;
import com.supermap.wisdombusiness.core.SuperHelper;
import com.supermap.wisdombusiness.framework.model.User;
import com.supermap.wisdombusiness.web.Message;
import com.supermap.wisdombusiness.web.ResultMessage;
import com.supermap.wisdombusiness.web.ui.tree.Tree;
import com.supermap.wisdombusiness.workflow.dao.CommonDao;
import com.supermap.wisdombusiness.workflow.model.Wfi_MaterData;
import com.supermap.wisdombusiness.workflow.service.common.DateUtil;
import com.supermap.wisdombusiness.workflow.util.FileUpload;

@Controller
@RequestMapping("/query_gx")
public class QueryGXController {
	/** 查询service */
	@Autowired
	private QueryService queryService_gx;
	
	@Autowired
	private CommonDao baseCommonDao;
	
	/**
	 * @param request
	 * @param response
	 * @return
	 * @throws 查封查询String dataString = request.getParameter("info");
	 */
	@RequestMapping(value = "/cfquery", method = RequestMethod.GET)
	public @ResponseBody Message GetCFQueryList(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		Integer page = 1;
		if (request.getParameter("page") != null) {
			page = Integer.parseInt(request.getParameter("page"));
		}
		String autounlock = request.getParameter("AUTOUNLOCK");
		boolean ifunlock = false;
		if (autounlock != null && autounlock.toLowerCase().equals("true")) {
			ifunlock = true;
		}
		Integer rows = 10;
		if (request.getParameter("rows") != null) {
			rows = Integer.parseInt(request.getParameter("rows"));
		}
		Map<String, String> queryvalues = new HashMap<String, String>();
		String qlrmc = RequestHelper.getParam(request, "QLRMC");// 权利人名称
		String bdcqzh = RequestHelper.getParam(request, "BDCQZH");// 不动产权证号
		String cfwh = RequestHelper.getParam(request, "CFWH");// 不动产权证号
		String bdcdyh=RequestHelper.getParam(request, "BDCDYH");
		queryvalues.put("FSQL.CFWH", cfwh);
		queryvalues.put("QL.BDCDYH", bdcdyh);
		queryvalues.put("QLR.QLRMC", qlrmc);
	

		return queryService_gx.queryAutoUnlock(queryvalues, page, rows, ifunlock);
	}

	/**解封
	 * @param request
	 * @param response
	 * @return
	 * @throws 根据权利Id解封
	 */
	@RequestMapping(value = "/{qlid}/jf", method = RequestMethod.POST)
	public @ResponseBody Message JF(@PathVariable("qlid") String qlid,HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		String fj = request.getParameter("fj");
		return queryService_gx.Unlock(qlid, fj);
	}
	/**
	 * @param request
	 * @param response
	 * @return
	 * @throws 抵押权列表
	 */
	@RequestMapping(value = "/mortgagelist", method = RequestMethod.GET)
	public @ResponseBody Message MortgageList(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		Integer page = 1;
		if (request.getParameter("page") != null) {
			page = Integer.parseInt(request.getParameter("page"));
		}
		Integer rows = 10;
		if (request.getParameter("rows") != null) {
			rows = Integer.parseInt(request.getParameter("rows"));
		}
		Map<String, String> queryvalues = new HashMap<String, String>();
		String qlrmc = RequestHelper.getParam(request, "QLRMC");// 权利人名称
		String bdcqzh = RequestHelper.getParam(request, "BDCQZH");// 不动产权证号
		String bdcdyh=RequestHelper.getParam(request, "BDCDYH");
		queryvalues.put("QL.BDCQZH", bdcqzh);
		queryvalues.put("QL.BDCDYH", bdcdyh);
		queryvalues.put("QLR.QLRMC", qlrmc);
	

		return queryService_gx.MortgageList(queryvalues, page, rows);
	}

	/**抵押取消
	 * @param request
	 * @param response
	 * @return
	 * @throws 
	 */
	@RequestMapping(value = "/{qlid}/mortgagecancel", method = RequestMethod.POST)
	public @ResponseBody Message MortgageCancel(@PathVariable("qlid") String qlid,HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		String zxdjyy = request.getParameter("zxdjyy");
		String zxfj = request.getParameter("zxfj");
		String zxdyywh = request.getParameter("zxdyywh");
		return queryService_gx.MortgageCancel(qlid, zxdjyy, zxfj, zxdyywh);
	}
	@RequestMapping(value = "/diyazxlist", method = RequestMethod.GET)
	public @ResponseBody Message DiYaZxList(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		Integer page = 1;
		if (request.getParameter("page") != null) {
			page = Integer.parseInt(request.getParameter("page"));
		}
		Integer rows = 10;
		if (request.getParameter("rows") != null) {
			rows = Integer.parseInt(request.getParameter("rows"));
		}
		Map<String, String> queryvalues = new HashMap<String, String>();
		String qlrmc = this.getParam(request, "QLR.QLRMC");//
		String zl = this.getParam(request, "UNIT.ZL");// 
		String bdcqzh = this.getParam(request, "QL.BDCQZH");// 
		String slsj_g = this.getParam(request, "XMXX.SLSJ_G");// 
		String slsj_l = this.getParam(request, "XMXX.SLSJ_L");// 
		queryvalues.put("QLR.QLRMC", qlrmc);
		queryvalues.put("UNIT.ZL", zl);
		queryvalues.put("QL.BDCQZH", bdcqzh);
		queryvalues.put("XMXX.SLSJ_G", slsj_g);
		queryvalues.put("XMXX.SLSJ_L", slsj_l);
		

		return queryService_gx.queryDiyazx(queryvalues, page, rows);
	}
	@RequestMapping(value = "/querysplit", method = RequestMethod.GET)
	public @ResponseBody Message QuerySplitList(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		Integer page = 1;
		if (request.getParameter("page") != null) {
			page = Integer.parseInt(request.getParameter("page"));
		}
		Integer rows = 10;
		if (request.getParameter("rows") != null) {
			rows = Integer.parseInt(request.getParameter("rows"));
		}
		Map<String, String> queryvalues = new HashMap<String, String>();
		String qlrmc = this.getParam(request, "QLR.QLRMC");//
		String zl = this.getParam(request, "UNIT.ZL");// 
		String bdcqzh = this.getParam(request, "QL.BDCQZH");// 
/*		String slsj_g = this.getParam(request, "XMXX.SLSJ_G");// 
		String slsj_l = this.getParam(request, "XMXX.SLSJ_L");// 
*/		queryvalues.put("QLR.QLRMC", qlrmc);
		queryvalues.put("UNIT.ZL", zl);
		queryvalues.put("QL.BDCQZH", bdcqzh);
/*		queryvalues.put("XMXX.SLSJ_G", slsj_g);
		queryvalues.put("XMXX.SLSJ_L", slsj_l);*/
		

		return queryService_gx.querySplit(queryvalues, page, rows);
	}
	/**解封
	 * @param request
	 * @param response
	 * @return
	 * @throws 根据权利Id解封
	 */
	@RequestMapping(value = "/{qlid}/splitql", method = RequestMethod.POST)
	public @ResponseBody Message SplitQL(@PathVariable("qlid") String qlid,HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		return queryService_gx.SplitQL(qlid);
	}
	@SuppressWarnings("deprecation")
	@RequestMapping(value = "/diyazxlistDownload", method = RequestMethod.GET)
	public   @ResponseBody String  DiYaZxListDownload(HttpServletRequest request, HttpServletResponse response) throws Exception{
		Map<String, String> queryvalues = new HashMap<String, String>();
		String qlrmc = this.getParam(request, "QLR.QLRMC");//
		String zl = this.getParam(request, "UNIT.ZL");// 
		String bdcqzh = this.getParam(request, "QL.BDCQZH");// 
		String slsj_g = this.getParam(request, "XMXX.SLSJ_G");// 
		String slsj_l = this.getParam(request, "XMXX.SLSJ_L");// 
		queryvalues.put("QLR.QLRMC", qlrmc);
		queryvalues.put("UNIT.ZL", zl);
		queryvalues.put("QL.BDCQZH", bdcqzh);
		queryvalues.put("XMXX.SLSJ_G", slsj_g);
		queryvalues.put("XMXX.SLSJ_L", slsj_l);
		Message message=queryService_gx.queryDiyazx(queryvalues, 1, 1000000);
		List<Map> rows=(List<Map>) message.getRows();
		String tmpFullName = "";
		String basePath = request.getRealPath("/") + "\\resources\\PDF";
		String outpath = "";
		String url = "";
		FileOutputStream outstream = null;
		outpath = basePath + "\\tmp\\diyazx.xls";
		url = request.getContextPath() + "\\resources\\PDF\\tmp\\diyazx.xls";
	    outstream = new FileOutputStream(outpath); 

		    tmpFullName = request.getRealPath("/WEB-INF/jsp/wjmb/diyazx.xls");
		    InputStream input = new FileInputStream(tmpFullName);
			HSSFWorkbook  wb = null;// 定义一个新的工作簿
			wb = new HSSFWorkbook(input);
			Sheet sheet = wb.getSheetAt(0);
			Map<String,Integer> MapCol = new HashMap<String,Integer>();
			MapCol.put("序号", 0);
			MapCol.put("产权人", 1);
			MapCol.put("房屋坐落", 2);
			MapCol.put("他项权利证号", 3);
			MapCol.put("债权数额", 4);
			MapCol.put("受理时间", 5);
			MapCol.put("登薄时间", 6);
			MapCol.put("导出时间", 7);
            int rownum = 2;
			for(Map r:rows){
						 HSSFRow row = (HSSFRow)sheet.createRow(rownum);
				         HSSFCell Cell0 = row.createCell(MapCol.get("序号"));
				         Cell0.setCellValue(rownum-1);
				         HSSFCell Cell1 = row.createCell(MapCol.get("产权人"));
				         Cell1.setCellValue(String.valueOf(r.get("QLRMC")));
				         HSSFCell Cell2 = row.createCell(MapCol.get("房屋坐落"));
				         Cell2.setCellValue(String.valueOf(r.get("ZL")));
				         HSSFCell Cell3 = row.createCell(MapCol.get("他项权利证号"));
				         Cell3.setCellValue(String.valueOf(r.get("BDCQZH")));
				         HSSFCell Cell4 = row.createCell(MapCol.get("债权数额"));
				         Cell4.setCellValue(String.valueOf(r.get("ZGZQSE")));
				         HSSFCell Cell5 = row.createCell(MapCol.get("受理时间"));
				         Cell5.setCellValue(String.valueOf(r.get("SLSJ")));
				      
				         HSSFCell Cell16 = row.createCell(MapCol.get("登薄时间"));
				         Cell16.setCellValue(String.valueOf(r.get("ZXSJ")));
				         HSSFCell Cell17 = row.createCell(MapCol.get("导出时间"));
				         Cell17.setCellValue(DateUtil.getDateTime());
				         rownum += 1;
				  }
	/*		// 解决response中文乱码问题
			response.setContentType("text/html;charset=utf-8");	// 设置消息体的编码
			// 通过 http 协议  发送的http响应消息头  不能出现中文  中文必须要经过url编码
			String filename = URLEncoder.encode("抵押注销清单.xls", "utf-8");
			response.setContentType("application/vnd.ms-excel");    
		    response.setHeader("Content-disposition", "attachment;filename=" + filename);    
		    
		    OutputStream ouputStream = response.getOutputStream(); */
		    wb.write(outstream); 
		    outstream.flush(); 
		    outstream.close();
		    outstream = null;
		    return url;
	}
	private  String getParam(HttpServletRequest request, String paramName) throws UnsupportedEncodingException {
		String value = request.getParameter(paramName);
		if (!StringUtils.isEmpty(value)) {
			value = new String(value.getBytes("iso8859-1"), "utf-8");
		}
		return value;
	}
	/**
	 * 获取发证记录信息并写入签收薄
	 * @author 何开胜
	 * @param xmbh
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/{xmbh}/qsb", method = RequestMethod.GET)
	public @ResponseBody Message GetQsbList(@PathVariable("xmbh") String xmbh, HttpServletRequest request, HttpServletResponse response) {
	
		Message m = new Message();
		m = queryService_gx.GetFZList(xmbh);
		return m;
	}
	
	/**
	 * //登记业务统计明细 liangc
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "/querydjywtj", method = RequestMethod.GET)
	public @ResponseBody Message getDjywtjMX(HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException {
		Integer page = 1;
		if (request.getParameter("page") != null) {
			page = Integer.parseInt(request.getParameter("page"));
		}
		Integer rows = 10;
		if (request.getParameter("rows") != null) {
			rows = Integer.parseInt(request.getParameter("rows"));
		}
		Map<String, String> queryvalues = new HashMap<String, String>();

		String djlx = request.getParameter("DJLX");
		String qllx = request.getParameter("QLLX");
		String djsj_q = request.getParameter("QSSJ");
		String djsj_z = request.getParameter("QZSJ");
		String tjlx = request.getParameter("TJLX");
		queryvalues.put("DJLX", djlx);
		queryvalues.put("QLLX", qllx);
		queryvalues.put("DJSJ_Q", djsj_q);
		queryvalues.put("DJSJ_Z", djsj_z);
		queryvalues.put("TJLX", tjlx);

		return queryService_gx.djywtj(queryvalues, page, rows);

	}
	
	/**
	 * 登记业务统计（广西）
	 * @param tjsjqs
	 * @param tjsjjz
	 * @param response
	 * @return
	 * @throws Exception
	 */
	
	@RequestMapping(value = "/djywtj_gx/{qsj}/{zsj}", method = RequestMethod.GET)
    public @ResponseBody Message getDJYWTJ_GX(@PathVariable("qsj") String tjsjqs,@PathVariable("zsj") String tjsjjz,
    		HttpServletResponse response) throws Exception {
		Message msg = queryService_gx.GetDJYWTJ_GX(tjsjqs, tjsjjz);
		return msg;
    }
	
	/**
	 * 登记业务统计导出（广西）liangc
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({"rawtypes", "unchecked" })
	@RequestMapping(value = "/djywtjinfodownload_gx", method = RequestMethod.GET)
	public @ResponseBody String DjywtjInfoDownload_gx(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Map<String, String> mapcondition = new HashMap<String, String>();
		mapcondition.put("id_sjq", request.getParameter("id_sjq"));
		mapcondition.put("id_sjz", request.getParameter("id_sjz"));
    	Message m = null;
    	m = queryService_gx.GetDJYWTJ_GX(mapcondition.get("id_sjq"),
    			mapcondition.get("id_sjz"));
		List<Map> djfz = null;
		if (m != null && m.getRows() != null) {
			djfz = (List<Map>) m.getRows();
		}
		String basePath = request.getRealPath("/") + "\\resources\\PDF";
		String outPath = "";
		String url = "";
		String tmpFullName = "";
		FileOutputStream outStream = null;
		if (djfz != null && djfz.size() > 0) {
			outPath = basePath + "\\tmp\\djywtj.xls";
			url = request.getContextPath()
					+ "\\resources\\PDF\\tmp\\djywtj.xls";
			outStream = new FileOutputStream(outPath);
			tmpFullName = request.getRealPath("/WEB-INF/jsp/wjmb/djywtj_gx.xls");
			InputStream input = new FileInputStream(tmpFullName);
			HSSFWorkbook hw = null;
			hw = new HSSFWorkbook(input);
			HSSFSheet sheet = hw.getSheetAt(0);
			Map<String, Integer> mapCol = new HashMap<String, Integer>();
				mapCol.put("登记类型", 0);
				mapCol.put("权利类型", 1);
				mapCol.put("在办", 2);
				mapCol.put("登簿", 3);
				mapCol.put("已归档", 4);
				mapCol.put("未归档", 5);
				mapCol.put("归档", 6);
				mapCol.put("总数", 7);
				mapCol.put("权证", 8);
				mapCol.put("证明", 9);
				mapCol.put("权证1", 10);
				mapCol.put("证明2", 11);
				int rownum = 3;
				//合计
				int ZBGS =0,DBGS =0,YGDGS=0,WGDGS=0,GDGS=0,BJGS=0,SZZSGS =0,SZZMGS =0,FZZSGS =0,FZZMGS =0;
				for (Map djfzs : djfz) {
					HSSFRow row = (HSSFRow) sheet.createRow(rownum);
					ZBGS += Integer.parseInt(StringHelper.formatObject(djfzs.get("ZBGS")));
					DBGS += Integer.parseInt(StringHelper.formatObject(djfzs.get("DBGS")));
					YGDGS += Integer.parseInt(StringHelper.formatObject(djfzs.get("YGDGS")));
					WGDGS += Integer.parseInt(StringHelper.formatObject(djfzs.get("WGDGS")));
					GDGS += Integer.parseInt(StringHelper.formatObject(djfzs.get("GDGS")));
					BJGS += Integer.parseInt(StringHelper.formatObject(djfzs.get("BJGS")));
					SZZSGS += Integer.parseInt(StringHelper.formatObject(djfzs.get("SZZSGS")));
					SZZMGS += Integer.parseInt(StringHelper.formatObject(djfzs.get("SZZMGS")));
					FZZSGS += Integer.parseInt(StringHelper.formatObject(djfzs.get("FZZSGS")));
					FZZMGS += Integer.parseInt(StringHelper.formatObject(djfzs.get("FZZMGS")));
					try {
						HSSFCell cell0 = row.createCell(mapCol.get("登记类型"));
						cell0.setCellValue(StringHelper.formatObject(djfzs
								.get("DJLX")));
						HSSFCell cell1 = row.createCell(mapCol.get("权利类型"));
						cell1.setCellValue(StringHelper.formatObject(djfzs
								.get("QLLX")));
						HSSFCell cell2 = row.createCell(mapCol.get("在办"));
						cell2.setCellValue(StringHelper.formatObject(djfzs
								.get("ZBGS")));
						HSSFCell cell3 = row.createCell(mapCol.get("登簿"));
						cell3.setCellValue(StringHelper.formatObject(djfzs
								.get("DBGS")));
						HSSFCell cell4 = row.createCell(mapCol.get("已归档"));
						cell4.setCellValue(StringHelper.formatObject(djfzs
								.get("YGDGS")));
						HSSFCell cell5 = row.createCell(mapCol.get("未归档"));
						cell5.setCellValue(StringHelper.formatObject(djfzs
								.get("WGDGS")));
						HSSFCell cell6 = row.createCell(mapCol.get("归档"));
						cell6.setCellValue(StringHelper.formatObject(djfzs
								.get("GDGS")));
						HSSFCell cell7 = row.createCell(mapCol.get("总数"));
						cell7.setCellValue(StringHelper.formatObject(djfzs
								.get("BJGS")));
						HSSFCell cell8 = row.createCell(mapCol.get("权证"));
						cell8.setCellValue(StringHelper.formatObject(djfzs
								.get("SZZSGS")));
						HSSFCell cell9 = row.createCell(mapCol.get("证明"));
						cell9.setCellValue(StringHelper.formatObject(djfzs
								.get("SZZMGS")));
						HSSFCell cell10 = row.createCell(mapCol.get("权证1"));
						cell10.setCellValue(StringHelper.formatObject(djfzs
								.get("FZZSGS")));
						HSSFCell cell11 = row.createCell(mapCol.get("证明2"));
						cell11.setCellValue(StringHelper.formatObject(djfzs
								.get("FZZMGS")));
						rownum++;
					} catch (Exception ex) {
						// TODO Auto-generated catch block
						ex.printStackTrace();
					}
			}
				//合计	
				sheet.addMergedRegion(new Region(rownum, (short)0, rownum, (short)1));
				HSSFRow row = (HSSFRow) sheet.createRow(rownum);
				HSSFCell cell2 = row.createCell(0);
				cell2.setCellValue("合计");					
				HSSFCell cell3 = row.createCell(mapCol.get("在办"));
				cell3.setCellValue(Integer.toString(ZBGS));
				HSSFCell cell4 = row.createCell(mapCol.get("登簿"));
				cell4.setCellValue(Integer.toString(DBGS));
				HSSFCell cell5 = row.createCell(mapCol.get("已归档"));
				cell5.setCellValue(Integer.toString(YGDGS));
				HSSFCell cell6 = row.createCell(mapCol.get("未归档"));
				cell6.setCellValue(Integer.toString(WGDGS));
				HSSFCell cell7 = row.createCell(mapCol.get("归档"));
				cell7.setCellValue(Integer.toString(GDGS));
				HSSFCell cell8 = row.createCell(mapCol.get("总数"));
				cell8.setCellValue(Integer.toString(BJGS));
				HSSFCell cell9 = row.createCell(mapCol.get("权证"));
				cell9.setCellValue(Integer.toString(SZZSGS));
				HSSFCell cell10 = row.createCell(mapCol.get("证明"));
				cell10.setCellValue(Integer.toString(SZZMGS));
				HSSFCell cell11 = row.createCell(mapCol.get("权证1"));
				cell11.setCellValue(Integer.toString(FZZSGS));
				HSSFCell cell12 = row.createCell(mapCol.get("证明2"));
				cell12.setCellValue(Integer.toString(FZZMGS));
			
			hw.write(outStream);
			outStream.flush();
			outStream.close();
		}

		return url;
	}
	
	
	
	/**
	 * //登记业务统计明细导出liangc
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/djywtjinfodownload", method = RequestMethod.GET)
	public @ResponseBody String downloadDjywtjMX(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Map<String, String> queryvalues = new HashMap<String, String>();

		String djlx = request.getParameter("DJLX");
		String qllx = request.getParameter("QLLX");
		String djsj_q = request.getParameter("QSSJ");
		String djsj_z = request.getParameter("QZSJ");
		String tjlx = request.getParameter("TJLX");
		queryvalues.put("DJLX", djlx);
		queryvalues.put("QLLX", qllx);
		queryvalues.put("DJSJ_Q", djsj_q);
		queryvalues.put("DJSJ_Z", djsj_z);
		queryvalues.put("TJLX", tjlx);

		Message message = queryService_gx.djywtj(queryvalues, 1,
				Integer.MAX_VALUE);
		List<Map> rows = (List<Map>) message.getRows();

		String basePath = request.getRealPath("/") + "\\resources\\PDF";
		String outpath = "";
		String url = "";
		FileOutputStream outstream = null;
		outpath = basePath + "\\tmp\\djywtjmxqueryresult.xls";
		url = request.getContextPath()
				+ "\\resources\\PDF\\tmp\\djywtjmxqueryresult.xls";
		outstream = new FileOutputStream(outpath);
		String tplFullName = "";
		if (tjlx.equals("zb")) {
			tplFullName = request
					.getRealPath("/WEB-INF/jsp/wjmb/zaiban.xls");
		}
		if (tjlx.equals("db")) {
			tplFullName = request
					.getRealPath("/WEB-INF/jsp/wjmb/dengbu.xls");
		}
		if (tjlx.equals("wgd")) {
			tplFullName = request
					.getRealPath("/WEB-INF/jsp/wjmb/weiguidang.xls");
		}
		if (tjlx.equals("ygd")) {
			tplFullName = request
					.getRealPath("/WEB-INF/jsp/wjmb/yiguidang.xls");
		}
		if (tjlx.equals("szzm")) {
			tplFullName = request
					.getRealPath("/WEB-INF/jsp/wjmb/shanzhengzhengming.xls");
		}
		if (tjlx.equals("szzs")) {
			tplFullName = request
					.getRealPath("/WEB-INF/jsp/wjmb/shanzhengzhengshu.xls");
		}
		if (tjlx.equals("fzzm")) {
			tplFullName = request
					.getRealPath("/WEB-INF/jsp/wjmb/fazhengzhengming.xls");
		}
		if (tjlx.equals("fzzs")) {
			tplFullName = request
					.getRealPath("/WEB-INF/jsp/wjmb/fazhengzhengshu.xls");
		}
		
		InputStream input = new FileInputStream(tplFullName);
		HSSFWorkbook wb = null;// 定义一个新的工作簿
		wb = new HSSFWorkbook(input);
		Sheet sheet = wb.getSheetAt(0);
		Map<String, Integer> MapCol = new HashMap<String, Integer>();
		if (tjlx.equals("zb")) {
			MapCol.put("序号", 0);
			MapCol.put("项目名称", 1);
			MapCol.put("受理编号", 2);
			MapCol.put("受理人", 3);
			MapCol.put("受理时间", 4);
			int rownum = 2;
			for (Map r : rows) {
				HSSFRow row = (HSSFRow) sheet.createRow(rownum);
				HSSFCell Cell0 = row.createCell(MapCol.get("序号"));
				Cell0.setCellValue(rownum - 1);
				HSSFCell Cell1 = row.createCell(MapCol.get("项目名称"));
				Cell1.setCellValue(StringHelper.FormatByDatatype(r.get("XMMC")));
				HSSFCell Cell2 = row.createCell(MapCol.get("受理编号"));
				Cell2.setCellValue(StringHelper.FormatByDatatype(r.get("YWLSH")));
				HSSFCell Cell3 = row.createCell(MapCol.get("受理人"));
				Cell3.setCellValue(StringHelper.FormatByDatatype(r.get("SLRY")));
				HSSFCell Cell4 = row.createCell(MapCol.get("受理时间"));
				Cell4.setCellValue(StringHelper.FormatByDatatype(r.get("SLSJ")));
				rownum += 1;
			}
		}
		if (tjlx.equals("db")) {
			MapCol.put("序号", 0);
			MapCol.put("项目名称", 1);
			MapCol.put("受理编号", 2);
			MapCol.put("登簿人", 3);
			MapCol.put("登簿时间", 4);
			MapCol.put("不动产权证号", 5);
			int rownum = 2;
			for (Map r : rows) {
				HSSFRow row = (HSSFRow) sheet.createRow(rownum);
				HSSFCell Cell0 = row.createCell(MapCol.get("序号"));
				Cell0.setCellValue(rownum - 1);
				HSSFCell Cell1 = row.createCell(MapCol.get("项目名称"));
				Cell1.setCellValue(StringHelper.FormatByDatatype(r.get("XMMC")));
				HSSFCell Cell2 = row.createCell(MapCol.get("受理编号"));
				Cell2.setCellValue(StringHelper.FormatByDatatype(r.get("YWLSH")));
				HSSFCell Cell3 = row.createCell(MapCol.get("登簿人"));
				Cell3.setCellValue(StringHelper.FormatByDatatype(r.get("DBR")));
				HSSFCell Cell4 = row.createCell(MapCol.get("登簿时间"));
				Cell4.setCellValue(StringHelper.FormatByDatatype(r.get("DJSJ")));
				HSSFCell Cell5 = row.createCell(MapCol.get("不动产权证号"));
				Cell5.setCellValue(StringHelper.FormatByDatatype(r
						.get("BDCQZH")));
				rownum += 1;
			}
		}
		if (tjlx.equals("wgd")) {
			MapCol.put("序号", 0);
			MapCol.put("项目名称", 1);
			MapCol.put("受理编号", 2);
			MapCol.put("受理时间", 3);
			int rownum = 2;
			for (Map r : rows) {
				HSSFRow row = (HSSFRow) sheet.createRow(rownum);
				HSSFCell Cell0 = row.createCell(MapCol.get("序号"));
				Cell0.setCellValue(rownum - 1);
				HSSFCell Cell1 = row.createCell(MapCol.get("项目名称"));
				Cell1.setCellValue(StringHelper.FormatByDatatype(r.get("XMMC")));
				HSSFCell Cell2 = row.createCell(MapCol.get("受理编号"));
				Cell2.setCellValue(StringHelper.FormatByDatatype(r.get("YWLSH")));
				HSSFCell Cell3 = row.createCell(MapCol.get("受理时间"));
				Cell3.setCellValue(StringHelper.FormatByDatatype(r.get("SLSJ")));
				rownum += 1;
			}
		}
		if (tjlx.equals("ygd")) {
			MapCol.put("序号", 0);
			MapCol.put("项目名称", 1);
			MapCol.put("受理编号", 2);
			MapCol.put("归档人", 3);
			MapCol.put("归档时间", 4);
			int rownum = 2;
			for (Map r : rows) {
				HSSFRow row = (HSSFRow) sheet.createRow(rownum);
				HSSFCell Cell0 = row.createCell(MapCol.get("序号"));
				Cell0.setCellValue(rownum - 1);
				HSSFCell Cell1 = row.createCell(MapCol.get("项目名称"));
				Cell1.setCellValue(StringHelper.FormatByDatatype(r.get("XMMC")));
				HSSFCell Cell2 = row.createCell(MapCol.get("受理编号"));
				Cell2.setCellValue(StringHelper.FormatByDatatype(r.get("YWLSH")));
				HSSFCell Cell3 = row.createCell(MapCol.get("归档人"));
				Cell3.setCellValue(StringHelper.FormatByDatatype(r.get("LRR")));
				HSSFCell Cell4 = row.createCell(MapCol.get("归档时间"));
				Cell4.setCellValue(StringHelper.FormatByDatatype(r.get("LRSJ")));
				rownum += 1;
			}
		}
		if (tjlx.equals("szzm") || tjlx.equals("szzs")) {
			MapCol.put("序号", 0);
			MapCol.put("项目名称", 1);
			MapCol.put("受理编号", 2);
			MapCol.put("权证号", 3);
			MapCol.put("权利人", 4);
			MapCol.put("权利人电话号码", 5);
			MapCol.put("代理人", 6);
			MapCol.put("代理人电话号码", 7);
			MapCol.put("登簿人", 8);
			MapCol.put("登簿时间", 9);
			int rownum = 2;
			for (Map r : rows) {
				HSSFRow row = (HSSFRow) sheet.createRow(rownum);
				HSSFCell Cell0 = row.createCell(MapCol.get("序号"));
				Cell0.setCellValue(rownum - 1);
				HSSFCell Cell1 = row.createCell(MapCol.get("项目名称"));
				Cell1.setCellValue(StringHelper.FormatByDatatype(r.get("XMMC")));
				HSSFCell Cell2 = row.createCell(MapCol.get("受理编号"));
				Cell2.setCellValue(StringHelper.FormatByDatatype(r.get("YWLSH")));
				HSSFCell Cell3 = row.createCell(MapCol.get("权证号"));
				Cell3.setCellValue(StringHelper.FormatByDatatype(r
						.get("BDCQZH")));
				HSSFCell Cell4 = row.createCell(MapCol.get("权利人"));
				Cell4.setCellValue(StringHelper.FormatByDatatype(r.get("QLRMC")));
				HSSFCell Cell5 = row.createCell(MapCol.get("登簿人"));
				Cell5.setCellValue(StringHelper.FormatByDatatype(r.get("DBR")));
				HSSFCell Cell6 = row.createCell(MapCol.get("登簿时间"));
				Cell6.setCellValue(StringHelper.FormatByDatatype(r.get("DJSJ")));
				HSSFCell Cell7 = row.createCell(MapCol.get("权利人电话号码"));
				Cell7.setCellValue(StringHelper.FormatByDatatype(r.get("QLRDH")));
				HSSFCell Cell8 = row.createCell(MapCol.get("代理人"));
				Cell8.setCellValue(StringHelper.FormatByDatatype(r.get("DLRXM")));
				HSSFCell Cell9 = row.createCell(MapCol.get("代理人电话号码"));
				Cell9.setCellValue(StringHelper.FormatByDatatype(r.get("DLRLXDH")));
				rownum += 1;
			}
		}
		if (tjlx.equals("fzzs") || tjlx.equals("fzzm")) {
			MapCol.put("序号", 0);
			MapCol.put("项目名称", 1);
			MapCol.put("受理编号", 2);
			MapCol.put("权证号", 3);
			MapCol.put("权利人", 4);
			MapCol.put("领证人", 5);
			MapCol.put("领证人证件号", 6);
			MapCol.put("领证时间", 7);
			int rownum = 2;
			for (Map r : rows) {
				HSSFRow row = (HSSFRow) sheet.createRow(rownum);
				HSSFCell Cell0 = row.createCell(MapCol.get("序号"));
				Cell0.setCellValue(rownum - 1);
				HSSFCell Cell1 = row.createCell(MapCol.get("项目名称"));
				Cell1.setCellValue(StringHelper.FormatByDatatype(r.get("XMMC")));
				HSSFCell Cell2 = row.createCell(MapCol.get("受理编号"));
				Cell2.setCellValue(StringHelper.FormatByDatatype(r.get("YWLSH")));
				HSSFCell Cell3 = row.createCell(MapCol.get("权证号"));
				Cell3.setCellValue(StringHelper.FormatByDatatype(r
						.get("BDCQZH")));
				HSSFCell Cell4 = row.createCell(MapCol.get("权利人"));
				Cell4.setCellValue(StringHelper.FormatByDatatype(r.get("QLRMC")));
				HSSFCell Cell5 = row.createCell(MapCol.get("领证人"));
				Cell5.setCellValue(StringHelper.FormatByDatatype(r.get("LZRXM")));
				HSSFCell Cell6 = row.createCell(MapCol.get("领证人证件号"));
				Cell6.setCellValue(StringHelper.FormatByDatatype(r
						.get("LZRZJHM")));
				HSSFCell Cell7 = row.createCell(MapCol.get("领证时间"));
				Cell7.setCellValue(StringHelper.FormatByDatatype(r.get("FZSJ")));
				rownum += 1;
			}
		}

		wb.write(outstream);
		outstream.flush();
		outstream.close();
		outstream = null;
		return url;

	}
		   /**
     * 查询是否多单元一本证
     * liangq
     * 2016年8月9日16:49:44
     */
    @RequestMapping(value="/getSFHBZS/{xmbh}",method = RequestMethod.GET)
    public @ResponseBody Message getSFHBZS(@PathVariable("xmbh") String xmbh,HttpServletRequest request,HttpServletResponse response) 
    		throws UnsupportedEncodingException{
    	return queryService_gx.getSFHBZS(xmbh);
    }
	
    /**
     * 柳州调用时空云接口
     * liangq
     */
    
    @RequestMapping(value="/PushDataToPlatform/{type}/{xmbh}",method = RequestMethod.GET)
    public void getSFHBZS(@PathVariable("xmbh") String xmbh,@PathVariable("type") String type,
    		HttpServletRequest request,HttpServletResponse response){
    	String xzqhdm = ConfigHelper.getNameByValue("XZQHDM");
	//   if("450200".equals(xzqhdm)){	//加上区县判断
	    	if("H".equals(type)){
		    		queryService_gx.AddBuilding(xmbh);
		   }else if("ZD".equals(type)){
		    		queryService_gx.AddLand(xmbh);
		   }
	//   }
	}
    
    /**
     * 办件进度查询
     * @author liangc
     * @param request
     * @param response
     * @return
     * @throws UnsupportedEncodingException
     */
    @RequestMapping(value = "/bjjdQuery", method = RequestMethod.GET)
	public @ResponseBody Message getBJJDresult(HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException {
		Integer page = 1;
		if (request.getParameter("page") != null) {
			page = Integer.parseInt(request.getParameter("page"));
		}
		Integer rows = 10;
		if (request.getParameter("rows") != null) {
			rows = Integer.parseInt(request.getParameter("rows"));
		}
		Map<String, String> queryvalues = new HashMap<String, String>();

		String ywlsh = request.getParameter("YWLSH").trim();
		String djsj_q = request.getParameter("QSSJ");
		String djsj_z = request.getParameter("QZSJ");
		String sqr = request.getParameter("SQR".trim());
		String djrzjhm = request.getParameter("SQRZJHM".replace(" ", "").toUpperCase());
		queryvalues.put("YWLSH", ywlsh);
		queryvalues.put("DJSJ_Q", djsj_q);
		queryvalues.put("DJSJ_Z", djsj_z);
		queryvalues.put("SQR", sqr);
		queryvalues.put("SQRZJHM", djrzjhm);
		return queryService_gx.djjdQuery(queryvalues, page, rows);

	}
    
    /**
     * 办件进度查询结果导出
     * @author liangc
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/bjjdQueryDownload", method = RequestMethod.GET)
	public   @ResponseBody String HouseQueryListDownload(HttpServletRequest request, HttpServletResponse response) throws Exception{
		Map<String, String> queryvalues = new HashMap<String, String>();
		String ywlsh = request.getParameter("YWLSH").trim();
		String djsj_q = request.getParameter("QSSJ");
		String djsj_z = request.getParameter("QZSJ");
		String sqr = request.getParameter("SQR").trim();
		queryvalues.put("YWLSH", ywlsh);
		queryvalues.put("DJSJ_Q", djsj_q);
		queryvalues.put("DJSJ_Z", djsj_z);
		queryvalues.put("SQR", sqr);
		Message message= queryService_gx.djjdQuery(queryvalues, 1, Integer.MAX_VALUE);
		List<Map> rows=(List<Map>) message.getRows();
		
		//导出
		String basePath = request.getRealPath("/") + "\\resources\\PDF";
		String outpath = "";
		String url = "";
		FileOutputStream outstream = null;
		outpath = basePath + "\\tmp\\bjjdqueryresult.xls";
		url = request.getContextPath() + "\\resources\\PDF\\tmp\\bjjdqueryresult.xls";
	    outstream = new FileOutputStream(outpath); 

	    String tplFullName = request.getRealPath("/WEB-INF/jsp/wjmb/bjjd.xls");
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
	 * 文件上传(导入查封信息表)
	 * @author heks
	 * @param file
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = "/doUpload", method = RequestMethod.POST)
	public @ResponseBody Message loginIndex(HttpServletRequest request, HttpServletResponse response,@RequestParam("file") MultipartFile file)  {
		Message msg = new Message();
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
		SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
		FileInputStream is;
		try {
			is = new FileInputStream(tempFile);
			Workbook wb = WorkbookFactory.create(is); //这种方式 Excel 2003/2007/2010 都是可以处理的 
			Sheet sheet = wb.getSheetAt(0);//获取Excle模版的第一个sheet
			int rowCount = sheet.getLastRowNum(); //获取第一个sheet有效行数 
			List<Map> cfxx_list = new ArrayList<Map>();
			if(rowCount > 0){
				for (int r = 1; r < rowCount+1; r++) { 
					BDCS_BDCCFXX cfxx = new BDCS_BDCCFXX();
					Map<String,String> cfmap = new HashMap<String, String>();
					Row rw = sheet.getRow(r);
					//第一列只是手工表的编号，没必要读取，从第二列开始
					for(int c = 1;c <= 18;c++){
						Cell cl_01 = rw.getCell(c);
						String flws = getMergedRegionValue(sheet,r,c);
						flws = flws != null ? flws :(String)getCellValue(cl_01);

						switch (c) {
						case 1 :
							cfxx.setCFWH(flws);
							cfmap.put("CFWH", flws);
							break;
						case 2 :
							cfxx.setSDTIME(flws);
							cfmap.put("SDTIME", flws);
							break;
						case 3 :
							cfxx.setSDRXM(flws);
							cfmap.put("SDRXM", flws);
							break;
						case 4 :
							cfxx.setBCFR(flws);;
							cfmap.put("BCFR", flws);
							break;
						case 5 :
							cfxx.setCFBDW(flws);
							cfmap.put("CFBDW", flws);
							break;
						case 6 :
							cfxx.setTDZH(flws);
							cfmap.put("TDZH", flws);
							break;
						case 7 :
							cfxx.setTDMJ(flws);
							cfmap.put("TDMJ", flws);
							break;
						case 8 :
							cfxx.setFCZH(flws);
							cfmap.put("FCZH", flws);
							break;
						case 9 :
							cfxx.setFWMJ(flws);
							cfmap.put("FWMJ", flws);
							break;
						/*case 10 :
							cfxx.setZL(flws);
							cfmap.put("ZL", flws);
							break;*/
						case 11 :
							cfxx.setCFJG(flws);
							cfmap.put("CFJG", flws);
							break;
						case 12 :
							cfxx.setWTFYCFDW(flws);
							cfmap.put("WTFYCFDW", flws);
							break;
						case 13 :
							cfxx.setBZ(flws);
							cfmap.put("BZ", flws);
							break;
						/*case 14 :
							cfxx.setCFDATE(flws);
							cfmap.put("CFDATE", flws);
							break;*/
						case 15 :
							cfxx.setCFQX(flws);
							cfmap.put("CFQX", flws);
							break;
						/*case 16 :
							cfxx.setJFDATE(flws);
							cfmap.put("JFDATE", flws);
							break;*/
						case 17 :
							cfxx.setCDQK(flws);
							cfmap.put("CDQK", flws);
							break;
						case 18 :
							cfxx.setTSCLQK(flws);
							cfmap.put("TSCLQK", flws);
							break;

						}
					}
					cfxx.setId(cfxx.getId());
					baseCommonDao.save(cfxx);
					
					cfxx_list.add(cfmap);
				}
			}
			
			msg.setTotal(cfxx_list.size());
			msg.setRows(cfxx_list);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} //文件流  
		catch (InvalidFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally{
			baseCommonDao.flush();
		}
		
		return msg;
        
	}
	
	/** 
	* 获取合并单元格的值 
	* @param sheet 
	* @param row 
	* @param column 
	* @return 
	*/ 
	public String getMergedRegionValue(Sheet sheet, int row, int column) { 
		int sheetMergeCount = sheet.getNumMergedRegions(); 
		for (int i = 0; i < sheetMergeCount; i++) { 
			CellRangeAddress ca = sheet.getMergedRegion(i); 
			int firstColumn = ca.getFirstColumn(); 
			int lastColumn = ca.getLastColumn(); 
			int firstRow = ca.getFirstRow(); 
			int lastRow = ca.getLastRow(); 
			if (row >= firstRow && row <= lastRow) { 
				if (column >= firstColumn && column <= lastColumn) { 
					Row fRow = sheet.getRow(firstRow); 
					Cell fCell = fRow.getCell(firstColumn); 
					return fCell.getStringCellValue(); 
				} 
			} 
		} 
		return null; 
	}
	
	/**
	 * 根据单元格的类型获取值
	 * @param cell
	 * @return
	 */
	public Object getCellValue(Cell cell){
		String cellValue = "";
		if(cell == null) return "";
		switch (cell.getCellType()) {
        case HSSFCell.CELL_TYPE_NUMERIC :
        	cellValue = String.valueOf(cell.getNumericCellValue());
            break;
        case HSSFCell.CELL_TYPE_STRING :
        	cellValue = cell.getRichStringCellValue().getString();
            break;
        case HSSFCell.CELL_TYPE_FORMULA :
        	cellValue = cell.getCellFormula();
            break;
           
    }
		return cellValue;
		
	}
	
	/**
	 * @param request
	 * @param response
	 * @return
	 * @throws 查封查询String dataString = request.getParameter("info");
	 */
	@RequestMapping(value = "/showcfxx", method = RequestMethod.GET)
	public @ResponseBody Message GetCFList(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		Integer page = 1;
		if (request.getParameter("page") != null) {
			page = Integer.parseInt(request.getParameter("page"));
		}
		Integer rows = 20;
		if (request.getParameter("rows") != null) {
			rows = Integer.parseInt(request.getParameter("rows"));
		}
		
		String querytype = request.getParameter("QUERYTYPE");
		boolean iflike = false;
		if (querytype != null && querytype.toLowerCase().equals("true")) {
			iflike = true;
		}
		Map<String, String> queryvalues = new HashMap<String, String>();
		String sort = RequestHelper.getParam(request, "sort");// 排序字段
		String order = RequestHelper.getParam(request, "order");// 排序Order
		String qlrmc = RequestHelper.getParam(request, "BCFR");// 被查封人
		String cfwh = RequestHelper.getParam(request, "CFWH");// 查封文号
		String tdzh = RequestHelper.getParam(request, "TDZH");// 土地证号
		String fczh=RequestHelper.getParam(request, "FCZH");//房产证号
		String zl=RequestHelper.getParam(request, "ZL");//查封标的物
		String bh=RequestHelper.getParam(request, "BH");//编号
		queryvalues.put("BCFR", qlrmc);
		queryvalues.put("CFWH", cfwh);
		queryvalues.put("TDZH", tdzh);
		queryvalues.put("FCZH", fczh);
		queryvalues.put("CFBDW", zl);
		queryvalues.put("BH", bh);
		return queryService_gx.queryCFInfoFromExcle(queryvalues,iflike,page, rows,sort,order);
	}
	
	/**
	 * 根据主键删除查封信息---cfbh
	 * @author heks
	 * @date 2017-04-20
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value="/deltctCfxx/{cfbh}",method = RequestMethod.GET)
    public @ResponseBody Message deleteCFxxByCfbh(@PathVariable("cfbh") String cfbh,HttpServletRequest request,HttpServletResponse response){
		
		return queryService_gx.deleteCfByCfbh(cfbh);
	}
	
	/**
	 * 提交并保存查封信息
	 * */
	@RequestMapping(value = "/savecfxx", method = RequestMethod.POST)
	public @ResponseBody ResultMessage saveUser(
			@ModelAttribute("cfxxattribute") BDCS_BDCCFXX cfxx, BindingResult result,
			SessionStatus status, HttpServletRequest request,
			HttpServletResponse response) {
		ResultMessage msg = new ResultMessage();
		if (result.hasErrors()) {
		
			msg.setMsg(result.toString());
		} else {
			try {
				cfxx.setId(cfxx.getId());
				//新增的默认处于“已查封”状态
				String bsm = cfxx.getBSM();
				if(!StringHelper.isEmpty(bsm) && bsm.equals("0")){
					cfxx.setSFJF("已查封");
				}
				
				Subject user = SecurityUtils.getSubject();
				User u =  Global.getCurrentUserInfo();
				String curr_user = u.getLoginName();
				
				cfxx.setCJR(curr_user);
				
				Date currdate = new Date();
				SimpleDateFormat  formatter  = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String cd_datestring  = formatter.format(currdate);  
				ParsePosition pos = new ParsePosition(0);
				Date strtodate = formatter.parse(cd_datestring, pos);
				cfxx.setCREATETIME(strtodate);
				SimpleDateFormat  sdf  = new SimpleDateFormat("yyyy-MM-dd");
				String strDate=sdf.format(currdate);
				cfxx.setLRTIME(strDate);
				baseCommonDao.save(cfxx);
				baseCommonDao.flush();
				msg.setMsg("保存成功！");
				msg.setSuccess("true");
				YwLogUtil.addYwLog("新增查封信息-成功", ConstValue.SF.YES.Value,ConstValue.LOG.ADD);
			} catch (Exception e) {
				
				msg.setMsg(e.getMessage());
				msg.setSuccess("false");
				YwLogUtil.addYwLog("新增查封信息-失败", ConstValue.SF.NO.Value,ConstValue.LOG.ADD);
			}
		}
		return msg;
	}
	
	/**
	 * 提交并保存查封信息(在选择了某条记录的基础上修改信息后生成新的记录)
	 * */
	@RequestMapping(value = "/addcfxxfromother", method = RequestMethod.PUT)
	public @ResponseBody ResultMessage saveCFfromother(
			@ModelAttribute("cfxxattribute") BDCS_BDCCFXX cfxx, BindingResult result,
			SessionStatus status, HttpServletRequest request,
			HttpServletResponse response) {
		ResultMessage msg = new ResultMessage();
		if (result.hasErrors()) {
		
			msg.setMsg(result.toString());
		} else {
			try {
				String new_id = SuperHelper.GeneratePrimaryKey();
				cfxx.setId(new_id);
				//新增的默认处于“已查封”状态
				String bsm = cfxx.getBSM();
				if(!StringHelper.isEmpty(bsm) && bsm.equals("0")){
					cfxx.setSFJF("已查封");
				}
				
				Subject user = SecurityUtils.getSubject();
				User u =  Global.getCurrentUserInfo();
				String curr_user = u.getLoginName();
				
				cfxx.setCJR(curr_user);
				
				Date currdate = new Date();
				SimpleDateFormat  formatter  = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String cd_datestring  = formatter.format(currdate);  
				ParsePosition pos = new ParsePosition(0);
				Date strtodate = formatter.parse(cd_datestring, pos);
				cfxx.setCREATETIME(strtodate);
				
				baseCommonDao.save(cfxx);
				baseCommonDao.flush();
				msg.setMsg("保存成功！");
				msg.setSuccess("true");
				YwLogUtil.addYwLog("新增查封信息-成功", ConstValue.SF.YES.Value,ConstValue.LOG.ADD);
			} catch (Exception e) {
				
				msg.setMsg(e.getMessage());
				msg.setSuccess("false");
				YwLogUtil.addYwLog("新增查封信息-失败", ConstValue.SF.NO.Value,ConstValue.LOG.ADD);
			}
		}
		return msg;
	}
	/**
	 * 更新查封信息
	 * 
	 * */
	@RequestMapping(value = "/editcfxx/{cfbh}", method = RequestMethod.PUT)
	public @ResponseBody ResultMessage updateUser(
			@PathVariable("cfbh") String cfbh,
			@ModelAttribute("cfxxattribute") BDCS_BDCCFXX cfxx, BindingResult result,
			SessionStatus status, HttpServletRequest request,
			HttpServletResponse response) {
		ResultMessage msg = new ResultMessage();
		if (result.hasErrors()) {
			msg.setMsg(result.toString());
		} else {
			try {
				if(!StringHelper.isEmpty(cfbh)){
					//状态判断
					String bsm = cfxx.getBSM();
					BDCS_BDCCFXX cf = baseCommonDao.get(BDCS_BDCCFXX.class, cfbh);
					if(cf != null) {
						String jfwh = cf.getJFWH();
						/*if(!StringHelper.isEmpty(jfwh)){
							msg.setSuccess("false");
							msg.setMsg("已解封,不可修改,解封文号："+jfwh);
							return msg;
						}*/
						if(!StringHelper.isEmpty(jfwh)){
							cf.setSFJF("已解封");
						}else{
							if(bsm.equals("0")){
								cf.setSFJF("已查封");
							}else{
								cf.setSFJF(" ");
							}
						}
//						cf.setCJR(cfxx.getCJR());
//						cf.setCREATETIME(cfxx.getCREATETIME());
						cf.setJFWH(cfxx.getJFWH());
						cf.setJFDATE(cfxx.getJFDATE());
						cf.setBH(cfxx.getBH());
						cf.setSDTIME(cfxx.getSDTIME());
						cf.setSDRXM(cfxx.getSDRXM());
						cf.setCFWH(cfxx.getCFWH());
						cf.setCFJG(cfxx.getCFJG());
						cf.setQSSJ(cfxx.getQSSJ());
						cf.setZZSJ(cfxx.getZZSJ());
						cf.setBCFR(cfxx.getBCFR());
						cf.setBCFRZJHM(cfxx.getBCFRZJHM());
						cf.setCFBDW(cfxx.getCFBDW());
						cf.setCFFW(cfxx.getCFFW());
						cf.setYG(cfxx.getYG());
						cf.setBG(cfxx.getBG());
						cf.setTDZH(cfxx.getTDZH());
						cf.setTDMJ(cfxx.getTDMJ());
						cf.setFCZH(cfxx.getFCZH());
						cf.setFWMJ(cfxx.getFWMJ());
						cf.setMSRMC(cfxx.getMSRMC());
						cf.setMSRZJH(cfxx.getMSRZJH());
						cf.setBZ(cfxx.getBZ());
						cf.setBZXR(cfxx.getBZXR());
						cf.setBZXRZJH(cfxx.getBZXRZJH());
					}

					Subject user = SecurityUtils.getSubject();
					User u =  Global.getCurrentUserInfo(); 
					String curr_user = u.getLoginName();

					cf.setGXR(curr_user);

					Date currdate = new Date();
					SimpleDateFormat  formatter  = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					String cd_datestring  = formatter.format(currdate);  
					ParsePosition pos = new ParsePosition(0);
					Date strtodate = formatter.parse(cd_datestring, pos);
					cf.setUPDATETIME(strtodate);	
					
					baseCommonDao.saveOrUpdate(cf);
					msg.setSuccess("true");
					YwLogUtil.addYwLog("更新查封信息-成功", ConstValue.SF.YES.Value,ConstValue.LOG.ADD);
				}
			} catch (Exception e) {
				msg.setMsg(e.getMessage());
				msg.setSuccess("false");
				YwLogUtil.addYwLog("更新查封信息-失败", ConstValue.SF.NO.Value,ConstValue.LOG.UPDATE);
			}
		}
		return msg;
	}
	
	/**
	 * 解封信息
	 * 
	 * */
	@RequestMapping(value = "/jf/{cfbh}", method = RequestMethod.PUT)
	public @ResponseBody ResultMessage jf(
			@PathVariable("cfbh") String cfbh,
			@ModelAttribute("cfxxattribute") BDCS_BDCCFXX cfxx, BindingResult result,
			SessionStatus status, HttpServletRequest request,
			HttpServletResponse response) {
		ResultMessage msg = new ResultMessage();
		if (result.hasErrors()) {
			msg.setMsg(result.toString());
		} else {
			try {
				if(!StringHelper.isEmpty(cfbh)){
					BDCS_BDCCFXX cf = baseCommonDao.get(BDCS_BDCCFXX.class, cfbh);
					
					cf.setJFWH(cfxx.getJFWH());
					cf.setJFDATE(cfxx.getJFDATE());

					User u =  Global.getCurrentUserInfo();
					String curr_user = u.getLoginName();

					cf.setGXR(curr_user);

					Date currdate = new Date();
					SimpleDateFormat  formatter  = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					String cd_datestring  = formatter.format(currdate);  
					ParsePosition pos = new ParsePosition(0);
					Date strtodate = formatter.parse(cd_datestring, pos);
					cf.setUPDATETIME(strtodate);	
					cf.setSFJF("已解封");
					baseCommonDao.update(cf);
					msg.setSuccess("true");
					msg.setMsg("解封成功！");
					YwLogUtil.addYwLog("更新查封信息-成功", ConstValue.SF.YES.Value,ConstValue.LOG.ADD);
				}
			} catch (Exception e) {
				msg.setMsg(e.getMessage());
				msg.setSuccess("false");
				YwLogUtil.addYwLog("更新查封信息-失败", ConstValue.SF.NO.Value,ConstValue.LOG.UPDATE);
			}
		}
		return msg;
	}
	
	
	/**
	 * 上传司法文书扫描件
	 * */
	@RequestMapping(value = "/addsfws", method = RequestMethod.POST)
	public @ResponseBody Message addSfws(@RequestParam("file") MultipartFile file,HttpServletRequest request, HttpServletResponse response) {
		Message msg = new Message();
		String cfxxbh = request.getParameter("cfxxbh");
		List<Map> fileInfos = new ArrayList<Map>(); 
		fileInfos = queryService_gx.AddSFWS(cfxxbh, file);
		
		queryService_gx.saveFiles(cfxxbh, fileInfos);
		
		msg.setTotal(fileInfos.size());
		msg.setRows(fileInfos);
		msg.setSuccess("true");
		return msg;
	}
	
	@RequestMapping(value = "/allmater/{id}", method = RequestMethod.GET)
	@ResponseBody
	public List<Tree> GetAllMaterDataTree(@PathVariable("id") String cfxxbh, HttpServletRequest request, HttpServletResponse response) {
 		if (cfxxbh != null && !cfxxbh.equals("")) {
			String type = request.getParameter("type");
			if (type != null ) {				
				return queryService_gx.getfilesByfolderID(cfxxbh);
			}
		} 
		return null;
	}
	
	/**
	 * 图片文件下载 add by wjz
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ParseException 
	 */
	@RequestMapping(value = "/filedownload/", method = RequestMethod.GET)
	public void download(@RequestParam("path") String path, HttpServletRequest request, HttpServletResponse response) throws IOException {
		// response.setDateHeader("Expires", 0);
		// response.setHeader("Cache-Control", "no-cache");
		// response.setHeader("Prama", "no-cache");
		OutputStream os = response.getOutputStream();
		request.setCharacterEncoding("UTF-8");
		String fileName="";
		path = java.net.URLDecoder.decode(new String(path.getBytes("ISO-8859-1"), "UTF-8"), "UTF-8");
		File file = new File(path);
		byte[] fileByte = null;
		if (file.exists()) {
			 fileByte = FileUtils.readFileToByteArray(file);
			 int index = path.lastIndexOf("//");
			 fileName = path.substring(index + 1);
			 fileName=fileName.replaceAll("/", "");
		} 
		try {
//			response.reset();
			if (!StringHelper.isEmpty(fileName) && fileName.toLowerCase().indexOf(".pdf") > 0) {
				response.setHeader("Content-Disposition", "inline; filename=" + fileName);
				response.setContentType("application/pdf; charset=UTF-8");
			} else {
				response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
				response.setContentType("image/jpeg; charset=UTF-8");
			}

			if (fileByte != null) {
				os.write(fileByte);
				os.flush();
			} 
		} finally {
			if (os != null) {
				os.close();
			}
		}
	}
	
	/** 附件预览 */
	@RequestMapping(value = "/imgview", method = RequestMethod.GET)
	public String imgView(Model model) {
		return "/workflow/frame/imgview";
	}
	
	/**
	 * 预览时删除图片操作
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/delimage", method = RequestMethod.POST)
	@ResponseBody
	public Map DelImage(HttpServletRequest request, HttpServletResponse response) {
		String id = request.getParameter("id");
		String pid = request.getParameter("pid");
		String filepath = request.getParameter("path");
		Map result = new HashMap();
		if (!StringHelper.isEmpty(id)) {
			baseCommonDao.delete(BDCS_UPLOADFILES.class, id);
			baseCommonDao.flush();
			result.put("confirm", "OK");
			result.put("desc", "删除成功");
		} else if (!StringHelper.isEmpty(pid)) {
			//原存在查封表的图片执行删除操作
			BDCS_BDCCFXX cfxx = baseCommonDao.get(BDCS_BDCCFXX.class, pid);
			if(!StringHelper.isEmpty(cfxx.getFILEPATH())){
				String[] filepaths_old = cfxx.getFILEPATH().split(",");	
				String filepaths_new = "";
				for (String path:filepaths_old) {
					if (!path.equals(filepath)) {
						filepaths_new +=StringHelper.isEmpty(filepaths_new)?path:","+path;
					}  					
				}	
				cfxx.setFILEPATH(filepaths_new);
				baseCommonDao.update(cfxx);
				baseCommonDao.flush();
				result.put("confirm", "OK");
				result.put("desc", "删除成功");
			}
		} else {
			result.put("confirm", "Error");
			result.put("desc", "删除失败");
		}
		return result;
	}
	
	@RequestMapping(value = "/delpromaterdata", method = RequestMethod.POST)
	@ResponseBody
	public Map DelProMateril(HttpServletRequest request, HttpServletResponse response) {
		String id = request.getParameter("id");
		String filepath = request.getParameter("path");
		Map result = new HashMap();
		if (id != null && !id.equals("")) {
			BDCS_BDCCFXX cfxx = baseCommonDao.get(BDCS_BDCCFXX.class, id);
			if(!StringHelper.isEmpty(cfxx.getFILEPATH())){
				String[] filepaths_old = cfxx.getFILEPATH().split(",");	
				String filepaths_new = "";
				for (String path:filepaths_old) {
					if (!path.equals(filepath)) {
						filepaths_new +=StringHelper.isEmpty(filepaths_new)?path:","+path;
					}  					
				}	
				cfxx.setFILEPATH(filepaths_new);
				baseCommonDao.update(cfxx);
				baseCommonDao.flush();
			}
			result.put("confirm", "OK");
			result.put("desc", "删除成功");
			return result;
		} else {
			result.put("confirm", "Error");
			result.put("desc", "删除失败");
			return result;
		}
	}
	
	/**
	 * 登记业务统计（广西）
	 * @param tjsjqs
	 * @param tjsjjz
	 * @param response
	 * @return
	 * @throws Exception
	 */
	
	@RequestMapping(value = "/fzl_sbltj/{qsj}/{zsj}", method = RequestMethod.GET)
    public @ResponseBody Message getFZL_SBLYWTJ(@PathVariable("qsj") String tjsjqs,@PathVariable("zsj") String tjsjjz,
    		HttpServletResponse response) throws Exception {
		Message msg = queryService_gx.GetFZL_SBLYWTJ(tjsjqs, tjsjjz);
		return msg;
    }
	
	/**
	 * 登记业务统计导出（广西）liangc
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({"rawtypes", "unchecked" })
	@RequestMapping(value = "/fzl_sbltjdownload", method = RequestMethod.GET)
	public @ResponseBody String FZL_SBLYWTJDownload(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Map<String, String> mapcondition = new HashMap<String, String>();
		mapcondition.put("id_sjq", request.getParameter("id_sjq"));
		mapcondition.put("id_sjz", request.getParameter("id_sjz"));
    	Message m = null;
    	m = queryService_gx.GetFZL_SBLYWTJ(mapcondition.get("id_sjq"),
    			mapcondition.get("id_sjz"));
		List<Map> djfz = null;
		if (m != null && m.getRows() != null) {
			djfz = (List<Map>) m.getRows();
		}
		String basePath = request.getRealPath("/") + "\\resources\\PDF";
		String outPath = "";
		String url = "";
		String tmpFullName = "";
		FileOutputStream outStream = null;
		if (djfz != null && djfz.size() > 0) {
			outPath = basePath + "\\tmp\\fzl_sbltj.xls";
			url = request.getContextPath()
					+ "\\resources\\PDF\\tmp\\fzl_sbltj.xls";
			outStream = new FileOutputStream(outPath);
			tmpFullName = request.getRealPath("/WEB-INF/jsp/wjmb/fzl_sbltj.xls");
			InputStream input = new FileInputStream(tmpFullName);
			HSSFWorkbook hw = null;
			hw = new HSSFWorkbook(input);
			HSSFSheet sheet = hw.getSheetAt(0);
			Map<String, Integer> mapCol = new HashMap<String, Integer>();
				mapCol.put("登记类型", 0);
				mapCol.put("权利类型", 1);
				mapCol.put("证书数", 2);
				mapCol.put("证明数", 3);
				mapCol.put("上报数", 4);
				int rownum = 3;
				//合计
				int ZSGS =0,ZMGS =0,SBGS=0;
				for (Map djfzs : djfz) {
					HSSFRow row = (HSSFRow) sheet.createRow(rownum);
					ZSGS += Integer.parseInt(StringHelper.formatObject(djfzs.get("ZSGS")));
					ZMGS += Integer.parseInt(StringHelper.formatObject(djfzs.get("ZMGS")));
					SBGS += Integer.parseInt(StringHelper.formatObject(djfzs.get("SBGS")));
					try {
						HSSFCell cell0 = row.createCell(mapCol.get("登记类型"));
						cell0.setCellValue(StringHelper.formatObject(djfzs
								.get("DJLX")));
						HSSFCell cell1 = row.createCell(mapCol.get("权利类型"));
						cell1.setCellValue(StringHelper.formatObject(djfzs
								.get("QLLX")));
						HSSFCell cell2 = row.createCell(mapCol.get("证书数"));
						cell2.setCellValue(StringHelper.formatObject(djfzs
								.get("ZSGS")));
						HSSFCell cell3 = row.createCell(mapCol.get("证明数"));
						cell3.setCellValue(StringHelper.formatObject(djfzs
								.get("ZMGS")));
						HSSFCell cell4 = row.createCell(mapCol.get("上报数"));
						cell4.setCellValue(StringHelper.formatObject(djfzs
								.get("SBGS")));
						rownum++;
					} catch (Exception ex) {
						// TODO Auto-generated catch block
						ex.printStackTrace();
					}
			}
				//合计	
				sheet.addMergedRegion(new Region(rownum, (short)0, rownum, (short)1));
				HSSFRow row = (HSSFRow) sheet.createRow(rownum);
				HSSFCell cell2 = row.createCell(0);
				cell2.setCellValue("合计");					
				HSSFCell cell3 = row.createCell(mapCol.get("证书数"));
				cell3.setCellValue(Integer.toString(ZSGS));
				HSSFCell cell4 = row.createCell(mapCol.get("证明数"));
				cell4.setCellValue(Integer.toString(ZMGS));
				HSSFCell cell5 = row.createCell(mapCol.get("上报数"));
				cell5.setCellValue(Integer.toString(SBGS));
			
			hw.write(outStream);
			outStream.flush();
			outStream.close();
		}

		return url;
	}
	
	/**
	 * 手工录入查封导出
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings({ "deprecation", "rawtypes", "unchecked" })
	@RequestMapping(value = "/downloadcfxx", method = RequestMethod.GET)
	public @ResponseBody String CFListDownload(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//********************************
		String querytype = request.getParameter("QUERYTYPE");
		boolean iflike = false;
		if (querytype != null && querytype.toLowerCase().equals("true")) {
			iflike = true;
		}
		Map<String, String> queryvalues = new HashMap<String, String>();
		String sort = RequestHelper.getParam(request, "sort");// 排序字段
		String order = RequestHelper.getParam(request, "order");// 排序Order
		String qlrmc = RequestHelper.getParam(request, "BCFR");// 被查封人
		String cfwh = RequestHelper.getParam(request, "CFWH");// 查封文号
		String tdzh = RequestHelper.getParam(request, "TDZH");// 土地证号
		String fczh=RequestHelper.getParam(request, "FCZH");//房产证号
		String zl=RequestHelper.getParam(request, "ZL");//查封标的物
		String bh=RequestHelper.getParam(request, "BH");//编号
		queryvalues.put("BCFR", qlrmc);
		queryvalues.put("CFWH", cfwh);
		queryvalues.put("TDZH", tdzh);
		queryvalues.put("FCZH", fczh);
		queryvalues.put("CFBDW", zl);
		queryvalues.put("BH", bh.replace(" ", ""));
		List<Map> listResult = new ArrayList<Map>();
		StringBuilder builderWhere = new StringBuilder();
		for (Entry<String, String> ent : queryvalues.entrySet()) {
			String name = ent.getKey();
			String value = ent.getValue();
			if (!StringHelper.isEmpty(value) && name.equals("BCFR")) {
				if(iflike){
					builderWhere.append(" AND BCFR like '%"+value+"%'");
				}else{
					builderWhere.append(" AND BCFR = '"+value+"'");
				}	
				
			}
			
			if (!StringHelper.isEmpty(value) && name.equals("CFWH")) {
				if(iflike){
					builderWhere.append(" AND CFWH like '%"+value+"%'");
				}else{
					builderWhere.append(" AND CFWH = '"+value+"'");
				}	
			}
			
			if (!StringHelper.isEmpty(value) && name.equals("TDZH")) {
				if(iflike){
					builderWhere.append(" AND TDZH like '%"+value+"%'");
				}else{
					builderWhere.append(" AND TDZH = '"+value+"'");
				}	
			}
			
			if (!StringHelper.isEmpty(value) && name.equals("FCZH")) {
				if(iflike){
					builderWhere.append(" AND FCZH like '%"+value+"%'");
				}else{
					builderWhere.append(" AND FCZH = '"+value+"'");
				}	
			}
			
			if (!StringHelper.isEmpty(value) && name.equals("CFBDW")) {
				if(iflike){
					builderWhere.append(" AND CFBDW like '%"+value+"%'");
				}else{
					builderWhere.append(" AND CFBDW = '"+value+"'");
				}	
			}
			
			if (!StringHelper.isEmpty(value) && name.equals("BH")) {
				if(iflike){
					builderWhere.append(" AND BH like '%"+value+"%'");
				}else{
					builderWhere.append(" AND BH = "+value+"");
				}	
			}
		}

		String fromSql = " FROM BDCK.BDCS_BDCCFXX WHERE 1=1"+ builderWhere.toString() ;
		String fullSql = " SELECT *"+ fromSql;
		
		/* 排序 条件语句 */
		
		if(!StringUtils.isEmpty(sort)&&!StringUtils.isEmpty(order))
		{
		if(sort.toUpperCase().equals("BH"))
			sort="LENGTH(TRIM(BH)), BH";
//			sort="cast(BH as int)";
		if(sort.toUpperCase().equals("SDTIME"))
			sort="SDTIME";
			fullSql=fullSql+" ORDER BY "+sort+" "+order;
		}
		else{
			fullSql=fullSql+" ORDER BY CREATETIME,SDTIME desc";
		}
		listResult = baseCommonDao.getDataListByFullSql(fullSql);
		if(listResult.size() >0){
			for (Map cfmap : listResult) {
				String sfjf = (String) cfmap.get("BSM");
				if(!StringHelper.isEmpty(sfjf)){
					if(sfjf.equals("0")){
						cfmap.put("BSM", "查封");
					}
					if(sfjf.equals("1")){
						cfmap.put("BSM", "协助过户");
					}
					if(sfjf.equals("2")){
						cfmap.put("BSM", "其他类型");
					}
				}
			}
		}
		//********************************
		//导出
		String basePath = request.getRealPath("/") + "\\resources\\PDF";
		String outpath = "";
		String url = "";
		FileOutputStream outstream = null;
		outpath = basePath + "\\tmp\\sglrcffxx.xls";
		url = request.getContextPath() + "\\resources\\PDF\\tmp\\sglrcffxx.xls";
	    outstream = new FileOutputStream(outpath); 

	    String tplFullName = request.getRealPath("/WEB-INF/jsp/wjmb/sglrcffxx.xls");
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
		for(Map r : listResult){
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
     * 注销业务查询
     * @author liangc
     * @param request
     * @param response
     * @return
     * @throws UnsupportedEncodingException
     */
    @RequestMapping(value = "/zxywQuery", method = RequestMethod.GET)
	public @ResponseBody Message getZXYWresult(HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException {
		Integer page = 1;
		if (request.getParameter("page") != null) {
			page = Integer.parseInt(request.getParameter("page"));
		}
		Integer rows = 10;
		if (request.getParameter("rows") != null) {
			rows = Integer.parseInt(request.getParameter("rows"));
		}
		Map<String, String> queryvalues = new HashMap<String, String>();

		String ywlsh = request.getParameter("YWLSH").trim();
		String djsj_q = request.getParameter("QSSJ");
		String djsj_z = request.getParameter("QZSJ");
		String qlrmc = request.getParameter("QLRMC").trim();
		String bdcdyh = request.getParameter("BDCDYH").trim();
		String zl = request.getParameter("ZL").trim();
		queryvalues.put("YWLSH", ywlsh);
		queryvalues.put("DJSJ_Q", djsj_q);
		queryvalues.put("DJSJ_Z", djsj_z);
		queryvalues.put("QLRMC", qlrmc);
		queryvalues.put("BDCDYH", bdcdyh);
		queryvalues.put("ZL", zl);
		
		return queryService_gx.zxywQuery(queryvalues, page, rows);

	}
    
    /**
     * 注销业务查询结果导出
     * @author liangc
     * @param request
     * @param response
     * @time 2018年4月15日 上午11点19分
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/zxywQueryDownload", method = RequestMethod.GET)
	public   @ResponseBody String ZXYWQueryResultDownload(HttpServletRequest request, HttpServletResponse response) throws Exception{
    	Map<String, String> queryvalues = new HashMap<String, String>();
		String ywlsh = request.getParameter("YWLSH").trim();
		String djsj_q = request.getParameter("QSSJ");
		String djsj_z = request.getParameter("QZSJ");
		String qlrmc = request.getParameter("QLRMC").trim();
		String bdcdyh = request.getParameter("BDCDYH").trim();
		String zl = request.getParameter("ZL").trim();
		queryvalues.put("YWLSH", ywlsh);
		queryvalues.put("DJSJ_Q", djsj_q);
		queryvalues.put("DJSJ_Z", djsj_z);
		queryvalues.put("QLRMC", qlrmc);
		queryvalues.put("BDCDYH", bdcdyh);
		queryvalues.put("ZL", zl);
		Message message= queryService_gx.zxywQuery(queryvalues, 1, Integer.MAX_VALUE);
		List<Map> rows=(List<Map>) message.getRows();
		
		//导出
		String basePath = request.getRealPath("/") + "\\resources\\PDF";
		String outpath = "";
		String url = "";
		FileOutputStream outstream = null;
		outpath = basePath + "\\tmp\\zxywqueryresult.xls";
		url = request.getContextPath() + "\\resources\\PDF\\tmp\\zxywqueryresult.xls";
	    outstream = new FileOutputStream(outpath); 

	    String tplFullName = request.getRealPath("/WEB-INF/jsp/wjmb/zxyw.xls");
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
	
}
