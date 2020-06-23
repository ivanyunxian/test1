package com.supermap.realestate_gx.registration.web;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.supermap.realestate.registration.util.ConstValue;
import com.supermap.realestate.registration.util.RequestHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.realestate.registration.util.YwLogUtil;
import com.supermap.realestate.registration.util.ConstValue.DJLX;
import com.supermap.realestate.registration.util.ConstValue.QLLX;
import com.supermap.realestate_gx.registration.service.GX_RealestateSearchService;
import com.supermap.realestate_gx.registration.service.QueryService;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import com.supermap.wisdombusiness.framework.model.User;
import com.supermap.wisdombusiness.framework.service.UserService;
import com.supermap.wisdombusiness.web.Message;

@Controller
@RequestMapping("/gx_bdccd")
public class GX_RealestateSearchController {
	//用户service
		@Autowired
		private UserService userService;
		@Autowired
		private CommonDao baseCommonDao;
		@Autowired
		private QueryService queryService_gx;
		@Autowired
		private GX_RealestateSearchService realestateSearchService;
		/** 页面跳转路径 */
		private final String prefix = "/realestate_gx/registration/";

		/**
		 * 不动产查档页面
		 * @author liangc
		 * @创建时间 2018/4/18 14:56
		 * @return
		 */
		@RequestMapping(value = "/bdccd", method = RequestMethod.GET)
		public String matchQlrInfo(){
			return prefix+"djywcx/bdccdInfo";		
		}

		
		/**
		 * 个人查档-不动产查档结果页面
		 */
		@RequestMapping(value = "/bdcprint", method = RequestMethod.GET)
		public String cd_swresultsPrint() {
			
			return prefix + "djywcx/bdcPrint";
		}
		
		/**
		 * 不动产查询批量打印查档结果页面
		 */
		@RequestMapping(value = "/bdcplprint", method = RequestMethod.GET)
		public String zdresultsPrint() {
			return prefix + "djywcx/bdcplPrint";
		}
		
		/**
		 * 不动产查询
		 * @author liangc
		 * @param model
		 * @return
		 */
		@RequestMapping(value = "/bdcdaQueryPage", method = RequestMethod.GET)
		public String showBDCDAquery(Model model) {
			return prefix+"djywcx/bdcdaQuery";
		}
		
		/**
		 * （不动产信息查档）读取Excle模版中的内容并返回页面显示
		 * @author liangc
		 * @time 2018-4-23 14:51:30
		 * @param request
		 * @param response
		 * @return
		 * @throws UnsupportedEncodingException
		 */
		@SuppressWarnings({"unchecked", "rawtypes" })
		@RequestMapping(value = "/queryRealestatelist", method = RequestMethod.GET)
		public @ResponseBody Message getQueryRealestateList(HttpServletRequest request, HttpServletResponse response)throws UnsupportedEncodingException{
			
			Message msg = new Message();
			Map<String,Object> maps = new HashMap<String,Object>();
			List<Map> listMap = new ArrayList<Map>();
			String file_path = (String) request.getSession().getAttribute("FILEPATH");
			maps = realestateSearchService.GetQueryRealestateList(file_path);
			if(maps.containsKey("bdcInfoLists") ){
				request.getSession().setAttribute("bdcInfoLists", (List<Map>)maps.get("bdcInfoLists"));//查档查询条件
				listMap = (List<Map>)maps.get("bdcInfoLists");
			}
			msg.setRows(listMap);
			msg.setTotal(listMap.size());
			msg.setMsg("数据读取成功");
			
			return msg;
		}
		
		/**
		 * 将表格里的信息和根据权利人名称、身份证号从不动产库查出来的结果做数据比较，并填充
		 *@author liangc 
		 * @param sfall 查询全部不动产：0是，1不是。
		 * @param sfhouse 查询现房：0是，1不是。
		 * @param sfqhouse 查询期房：0是，1不是。
		 * @param sfshyqzd 查询使用权宗地：0是，1不是。
		 * @param sfsyqzd 查询所有权宗地：0是，1不是。
		 * @param sfsea 查询海域：0是，1不是。
		 * @param sfld 查询林地：0是，1不是。
		 * @param sfnyd 查询农用地：0是，1不是。
		 * @param queryVauleList
		 * @param request
		 * @param response
		 * @return
		 * @throws UnsupportedEncodingException 
		 */
		@SuppressWarnings({ "unchecked", "rawtypes", "unused" })
		@RequestMapping(value = "/matchdata/{sfall}/{sfhouse}/{sfqhouse}/{sfshyqzd}/{sfsyqzd}/{sfsea}/{sfld}/{sfnyd}", method = RequestMethod.GET)
		public @ResponseBody Message matchData(@PathVariable("sfall") String sfall,@PathVariable("sfhouse") String sfhouse,@PathVariable("sfqhouse") String sfqhouse,
				@PathVariable("sfshyqzd") String sfshyqzd,@PathVariable("sfsyqzd") String sfsyqzd,@PathVariable("sfsea") String sfsea,
				@PathVariable("sfld") String sfld,@PathVariable("sfnyd") String sfnyd,
				HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException{
			
			Message msg = new Message();
			List<Map> queryVauleList = new ArrayList<Map>();
			queryVauleList = (List<Map>) request.getSession().getAttribute("bdcInfoLists");//用于匹配数据的条件集合
			msg = realestateSearchService.getMatchData(sfall, sfhouse, sfqhouse,sfshyqzd,sfsyqzd, sfsea, sfld,sfnyd,queryVauleList);
			if(msg != null){
				List<Map> map = (List<Map>)msg.getRows();
				request.getSession().setAttribute("RESULTS_BDC", map);//匹配结果集
			}
			
			return msg;
		}
		
		
		/**
		 * 返回不动产查档证明内容
		 * @author liangc
		 * @param request
		 * @param response
		 * @return
		 * @throws Exception
		 */
		@SuppressWarnings({ "unchecked", "rawtypes" })
		@RequestMapping(value="/bdcplprintzm",method= RequestMethod.POST)
		public @ResponseBody Message bdcplPrintZM(HttpServletRequest request, HttpServletResponse response) throws Exception{
			
			//获取当前用户为查档经办人
			User user= userService.getCurrentUserInfo();
			String jbr = user.getUserName();
			
			List<Map> results = (List<Map>) request.getSession().getAttribute("RESULTS_BDC");
			Message msg = realestateSearchService.getBDCPLPrintZM(results, jbr);
			
			return msg;
		}
		
		
		/**
		 * 个人查档：匹配数据
		 * 
		 * @author liangc
		 * @param xm 姓名
		 * @param zjh 身份证号
		 * @param sfall 查询全部不动产：0是，1不是。
		 * @param sfhouse 查询现房：0是，1不是。
		 * @param sfqhouse 查询期房：0是，1不是。
		 * @param sfshyqzd 查询使用权宗地：0是，1不是。
		 * @param sfsyqzd 查询所有权宗地：0是，1不是。
		 * @param sfsea 查询海域：0是，1不是。
		 * @param sfld 查询林地：0是，1不是。
		 * @param sfnyd 查询农用地：0是，1不是。
		 * @param request
		 * @param response
		 * @return
		 * @throws UnsupportedEncodingException
		 */
		@SuppressWarnings({ "unchecked", "rawtypes", "unused" })
		@RequestMapping(value = "/whthinmatchdata/{xm}/{zjh}/{cdr}/{sfall}/{sfhouse}/{sfqhouse}/{sfshyqzd}/{sfsyqzd}/{sfsea}/{sfld}/{sfnyd}", method = RequestMethod.GET)
		public @ResponseBody Message WhthinMatchData(@PathVariable("xm") String xm,@PathVariable("zjh") String zjh,@PathVariable("cdr") String cdr,@PathVariable("sfall") String sfall,@PathVariable("sfhouse") String sfhouse,@PathVariable("sfqhouse") String sfqhouse,
				@PathVariable("sfshyqzd") String sfshyqzd,@PathVariable("sfsyqzd") String sfsyqzd,@PathVariable("sfsea") String sfsea,
				@PathVariable("sfld") String sfld,@PathVariable("sfnyd") String sfnyd,HttpServletRequest request, HttpServletResponse response)
				throws UnsupportedEncodingException{
			//获取当前用户为查档经办人
			User user= userService.getCurrentUserInfo();
			String jbr = user.getUserName();
			/*if (!StringHelper.isEmpty(cdr)) {
				cdr = new String(cdr.getBytes("ISO-8859-1"),"utf-8");
			}*/
			Message message = realestateSearchService.getWhthinMatchData(xm, zjh, jbr, cdr,sfall, sfhouse, sfqhouse,sfshyqzd,sfsyqzd, sfsea, sfld,sfnyd);
			
			return message;
		}
		
		/**
		 * 读取身份证
		 * @author taochunda
		 * @date 2017年8月25日 下午09:23:04
		 * @param paras
		 * @param model
		 * @param request
		 * @return
		 */
		@RequestMapping(value = "/{paras}/readCardPage", method = RequestMethod.GET)
		public String readIdCard(@PathVariable("paras") String paras, Model model, HttpServletRequest request) {
			try {
				paras = new String(paras.getBytes("iso8859-1"), "utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			model.addAttribute("paras", paras);
			YwLogUtil.addYwLog("证书读取身份证-成功", ConstValue.SF.YES.Value, ConstValue.LOG.ADD);
			return prefix + "modules/common/reciveZS";
		}
		
		/** 
		 * 不动产信息查档导出
		 * @author liangc
		 * @time 2018-4-28 9:07:30
		 * @return 
		 * @throws Exception 
		 * 
		 */
		@SuppressWarnings({ "deprecation", "rawtypes", "unchecked" })
		@RequestMapping(value = "/export_bdc", method = RequestMethod.GET)
		public @ResponseBody String bdccdExport(HttpServletRequest request, HttpServletResponse response) throws Exception {
			request.setCharacterEncoding("utf-8");
			Message message = new Message();
			List<Map> rows = (List<Map>) request.getSession().getAttribute("RESULTS_BDC");
			
			//导出
			String basePath = request.getRealPath("/") + "\\resources\\PDF";
			String outpath = "";
			String url = "";
			String xlsName = "bdccdinfo.xls";//宗地信息查档导出模板
			FileOutputStream outstream = null;
			outpath = basePath + "\\tmp\\"+xlsName;
			url = request.getContextPath() + "\\resources\\PDF\\tmp\\"+xlsName;
			outstream = new FileOutputStream(outpath); 

			String tplFullName = request.getRealPath("/WEB-INF/jsp/wjmb/"+xlsName);
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
	     * 不动产查询
	     * @author liangc
	     * @param request
	     * @param response
	     * @return
	     * @throws UnsupportedEncodingException
	     */
	    @RequestMapping(value = "/bdcdaQuery", method = RequestMethod.GET)
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
			String querytype = request.getParameter("QUERYTYPE");
			boolean iflike = false;
			if (querytype != null && querytype.toLowerCase().equals("true")) {
				iflike = true;
			}
			Map<String, String> queryvalues = new HashMap<String, String>();

			String qlrmc = request.getParameter("QLRMC").trim();
			String zjh = request.getParameter("ZJH").trim();
			queryvalues.put("QLRMC", qlrmc);
			queryvalues.put("ZJH", zjh);
			
			return realestateSearchService.bdcdaQuery(queryvalues, page, rows,iflike);

		}
	    
	    /**
	     * 不动产查询结果导出
	     * @author liangc
	     * @param request
	     * @param response
	     * @return
	     * @throws Exception
	     */
	    @RequestMapping(value = "/bdcdaQueryDownload", method = RequestMethod.GET)
		public   @ResponseBody String HouseQueryListDownload(HttpServletRequest request, HttpServletResponse response) throws Exception{
			Map<String, String> queryvalues = new HashMap<String, String>();
			String qlrmc = request.getParameter("QLRMC").trim();
			String zjh = request.getParameter("ZJH".trim());
			queryvalues.put("QLRMC", qlrmc);
			queryvalues.put("ZJH", zjh);
			String querytype = request.getParameter("QUERYTYPE");
			boolean iflike = false;
			if (querytype != null && querytype.toLowerCase().equals("true")) {
				iflike = true;
			}
			Message message= realestateSearchService.bdcdaQuery(queryvalues, 1, Integer.MAX_VALUE,iflike);
			List<Map> rows=(List<Map>) message.getRows();
			
			//导出
			String basePath = request.getRealPath("/") + "\\resources\\PDF";
			String outpath = "";
			String url = "";
			FileOutputStream outstream = null;
			outpath = basePath + "\\tmp\\bdcdaqueryresult.xls";
			url = request.getContextPath() + "\\resources\\PDF\\tmp\\bdcdaqueryresult.xls";
		    outstream = new FileOutputStream(outpath); 

		    String tplFullName = request.getRealPath("/WEB-INF/jsp/wjmb/bdcdaquery.xls");
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
