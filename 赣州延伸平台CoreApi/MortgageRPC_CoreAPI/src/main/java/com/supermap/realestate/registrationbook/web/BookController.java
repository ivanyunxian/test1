package com.supermap.realestate.registrationbook.web;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.itextpdf.text.DocumentException;
import com.supermap.realestate.registration.util.ConstValue;
import com.supermap.realestate.registration.util.Global;
import com.supermap.realestate.registration.util.PDFHelper;
import com.supermap.realestate.registration.util.RequestHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.realestate.registration.util.PDFHelper.PDFDataContainer;
import com.supermap.realestate.registration.util.YwLogUtil;
import com.supermap.realestate.registrationbook.model.BDCS_DJDY_LS_EX.DJBMenu;
import com.supermap.realestate.registrationbook.model.BookMenu;
import com.supermap.realestate.registrationbook.service.BookService;
import com.supermap.wisdombusiness.web.Message;

/**
 * 登记簿管理模块对应的控制器
 * 
 * @author 刘树峰
 *
 */
@Controller
@RequestMapping("/book")
public class BookController {

	@Autowired
	private BookService bookService;

	private final String prefix = "/realestate/registrationbook/";
	/**
	 * 登簿-封面
	 * 
	 * @作者 杨鹏
	 * @创建时间 2015年6月17日下午9:57:00
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/test2", method = RequestMethod.GET)
	public String test2(Model model) {
		return prefix + "test2";
	}
	/**
	 * 登簿-封面
	 * 
	 * @作者 杨鹏
	 * @创建时间 2015年6月17日下午9:57:00
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/test", method = RequestMethod.GET)
	public String test(Model model) {
		//QRCodeHelper.CreateQRCode("测试测试", 300, 300, "png");
		return prefix + "test";
	}
	/**
	 * 登簿-封面
	 * 
	 * @作者 杨鹏
	 * @创建时间 2015年6月17日下午9:57:00
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/bdcdjbfm", method = RequestMethod.GET)
	public String ShowDBCDJBFMIndex(Model model) {
		return prefix + "bdcdjbfm";
	}

	/**
	 * 登簿宗地基本信息表页
	 * 
	 * @作者 杨鹏
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/dbzdjbxx", method = RequestMethod.GET)
	public String ShowDBZDJBXXIndex(Model model) {
		return prefix + "dbzdjbxx";
	}

	/**
	 * 登簿宗海基本信息表页
	 * 
	 * @作者 杨鹏
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/dbzhjbxx", method = RequestMethod.GET)
	public String ShowDBZHJBXXIndex(Model model) {
		return prefix + "dbzhjbxx";
	}

	/**
	 * 不动产权利登记目录
	 * 
	 * @作者 杨鹏
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/dbbdcqldjml", method = RequestMethod.GET)
	public String ShowDBBDCQLDJMLIndex(Model model) {
		return prefix + "dbbdcqldjml";
	}

	/**
	 * 不动产权利及其他事项登记信息
	 * 
	 * @作者 杨鹏
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/dbbdcqjqtsx", method = RequestMethod.GET)
	public String ShowDBBDCQJQTSXIndex(Model model) {
		return prefix + "dbbdcqjqtsx";
	}

	/**
	 * 土地所有权登记信息
	 * 
	 * @作者 杨鹏
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/dbtdsyqdjxx", method = RequestMethod.GET)
	public String ShowDBTDSYQDJXXIndex(Model model) {
		return prefix + "dbtdsyqdjxx";
	}

	/**
	 * 房地产权登记信息(独幢、层、套、间房屋)
	 * 
	 * @作者 杨鹏
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/dbfdcqdjxxdz", method = RequestMethod.GET)
	public String ShowDBFDCQDJXXDZIndex(Model model) {
		return prefix + "dbfdcqdjxxdz";
	}

	/**
	 * 建设用地使用权、宅基地使用权登记信息
	 * 
	 * @作者 杨鹏
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/dbjsydsyqxx", method = RequestMethod.GET)
	public String ShowDBJSYDSYQXXIndex(Model model) {
		return prefix + "dbjsydsyqxx";
	}

	/**
	 * 建筑物区分所有权业主共有部分登记信息
	 * 
	 * @作者 杨鹏
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/dbjzwqfdjxx", method = RequestMethod.GET)
	public String ShowDBJZWQFDJXXIndex(Model model) {
		return prefix + "dbjzwqfdjxx";
	}

	/**
	 * 房地产权登记信息(项目内多幢房屋)
	 * 
	 * @作者 杨鹏
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/dbfdcqdjxxduoz", method = RequestMethod.GET)
	public String ShowDBFDCQDJXXDUOZIndex(Model model) {
		return prefix + "dbfdcqdjxxduoz";
	}

	/**
	 * 抵押权登记信息
	 * 
	 * @作者 杨鹏
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/dbdyqdjxx", method = RequestMethod.GET)
	public String ShowDBDYQDJXXIndex(Model model) {
		return prefix + "dbdyqdjxx";
	}

	/**
	 * 海域(含无居民海岛)使用权登记信息
	 * 
	 * @作者 杨鹏
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/dbhydjxx", method = RequestMethod.GET)
	public String ShowDBHYDJXXIndex(Model model) {
		return prefix + "dbhydjxx";
	}

	/**
	 * 构(建)筑物所有权登记信息
	 * 
	 * @作者 杨鹏
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/dbgzwsyqdjxx", method = RequestMethod.GET)
	public String ShowDBGZWSYQDJXXIndex(Model model) {
		return prefix + "dbgzwsyqdjxx";
	}

	/**
	 * 土地承包经营权、农用地的其他使用权登记信息(非林地)
	 * 
	 * @作者 杨鹏
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/dbtdcbdjxx", method = RequestMethod.GET)
	public String ShowDBTDCBDJXXIndex(Model model) {
		return prefix + "dbtdcbdjxx";
	}

	/**
	 * 其他相关权利登记信息(取水权、探矿权、采矿权等)
	 * 
	 * @作者 杨鹏
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/dbqtxgqldjxx", method = RequestMethod.GET)
	public String ShowDBQTXGQLDJXXIndex(Model model) {
		return prefix + "dbqtxgqldjxx";
	}

	/**
	 * 林权登记信息
	 * 
	 * @作者 杨鹏
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/dblqdjxx", method = RequestMethod.GET)
	public String ShowDBLQDJXXIndex(Model model) {
		return prefix + "dblqdjxx";
	}
	
	/** 国有农用地使用权登记信息
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/dbnyddjxx", method = RequestMethod.GET)
	public String ShowDBNYDDJXXIndex(Model model) {
		return prefix + "dbnyddjxx";
	}
	
	/**
	 * 森林林木使用权登记信息
	 * @author diaoliwei
	 * @date 2015-12-28
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/dbsllmdjxx", method = RequestMethod.GET)
	public String ShowDBSLLMDJXXIndex(Model model) {
		return prefix + "dbsllmdjxx";
	}

	/**
	 * 地役权登记信息
	 * 
	 * @作者 杨鹏
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/dbdiyiqdjxx", method = RequestMethod.GET)
	public String ShowDBDIYIQDJXXIndex(Model model) {
		return prefix + "dbdiyiqdjxx";
	}

	/**
	 * 预告登记信息
	 * 
	 * @作者 杨鹏
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/dbygdjxx", method = RequestMethod.GET)
	public String ShowDBYGDJXXIndex(Model model) {
		return prefix + "dbygdjxx";
	}

	/**
	 * 异议登记信息
	 * 
	 * @作者 杨鹏
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/dbyydjxx", method = RequestMethod.GET)
	public String ShowDBYYDJXXIndex(Model model) {
		return prefix + "dbyydjxx";
	}

	/**
	 * 查封登记信息
	 * 
	 * @作者 杨鹏
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/dbcfdjxx", method = RequestMethod.GET)
	public String ShowDBCFDJXXIndex(Model model) {
		return prefix + "dbcfdjxx";
	}

	/**
	 * 不动产登记证明
	 * 
	 * @作者 杨鹏
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/bdcdjzm", method = RequestMethod.GET)
	public String ShowBDCDJZMIndex(Model model) {
		return prefix + "bdcdjzm";
	}

	/**
	 * 登记簿管理模块入口
	 * 
	 * @作者 刘树峰
	 * @创建时间 2015年6月9日上午11:53:45
	 * @return
	 */
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String BookIndex() {
		YwLogUtil.addYwLog("访问：登记薄管理", ConstValue.SF.YES.Value,ConstValue.LOG.SEARCH);
		return prefix + "index";
	}

	/**
	 * 登记簿管理模块入口
	 * 
	 * @作者 WUZHU
	 * @创建时间
	 * @return
	 */
	@RequestMapping(value = "/xq", method = RequestMethod.GET)
	public String XQXX() {
		return prefix + "xqxx";
	}
	/**
	 * 批量推送数据到中间库
	 * 
	 * @作者 likun
	 * @创建时间
	 * @return
	 */
	@RequestMapping(value = "/pushdata", method = RequestMethod.GET)
	public String PUSHDATA() {
		return prefix + "pushdata";
	}

	/**
	 * 获取宗地宗海分页数据集
	 * 
	 * @作者 wuz
	 * 
	 * @return
	 */
	@RequestMapping(value = "/zdzh", method = RequestMethod.GET)
	public @ResponseBody Message GetZDORZHList(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("utf-8");
		Integer page = 1;
		if (request.getParameter("page") != null) {
			page = Integer.parseInt(request.getParameter("page"));
		}
		Integer rows = 10;
		if (request.getParameter("rows") != null) {
			rows = Integer.parseInt(request.getParameter("rows"));
		}
		Map<String, String> mapCondition = new HashMap<String, String>();
		mapCondition.put("ZL", request.getParameter("ZL"));
		mapCondition.put("BDCDYH", request.getParameter("BDCDYH"));
		mapCondition.put("ZDORZH", request.getParameter("ZDORZH"));
		mapCondition.put("ZDDM", request.getParameter("ZDDM"));
		mapCondition.put("BDCDYH_FW", request.getParameter("BDCDYH_FW"));
		mapCondition.put("ZL_FW", request.getParameter("ZL_FW"));
		mapCondition.put("QLR_FW", request.getParameter("QLR_FW"));
		Message msg = bookService.GetZDORZHList(mapCondition, page, rows);
		return msg;
	}

	/**
	 * 获取登记薄索引树形数据
	 * 
	 * @作者 wuz
	 * 
	 * @return
	 */
	@RequestMapping(value = "/{zddm}/{bdcdyid}/{bdcdyh}/{bdcdylx}/tree/{page}/{rows}", method = RequestMethod.GET)
	public @ResponseBody Message GetBookTree(@PathVariable String zddm,
			@PathVariable String bdcdyid, 
			@PathVariable String bdcdyh,
			@PathVariable String bdcdylx,
			@PathVariable long page,
			@PathVariable long rows,
			HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String bdcdyhSearch = RequestHelper.getParam(request, "BDCDYH");// 搜索的不动产单元号
		String zlSearch = RequestHelper.getParam(request, "ZL");// 搜索的坐落
		Map<String, String> mapCondition = new HashMap<String, String>();
		mapCondition.put("ZL",zlSearch);
		mapCondition.put("BDCDYH", bdcdyhSearch);
		
		return bookService.GetBookTree(bdcdyid, bdcdylx,mapCondition,page,rows);
		
	}
	/**
	 * 异步加载获取不动产单元的权利类型菜单（优化登记簿详情树形）
	 * 
	 * @作者 wuz
	 * 
	 * @return
	 */
	@RequestMapping(value = "/{parentid}/{bdcdyid}/{bdcdylx}/treechildren", method = RequestMethod.POST)
	public @ResponseBody List<BookMenu> GetBookChildrenByAsync(@PathVariable String parentid,
			@PathVariable String bdcdyid,
			@PathVariable String bdcdylx) throws Exception {
		List<BookMenu> trs = bookService.GetBookChildrenByAsync(parentid,bdcdyid, bdcdylx);
		return trs;
	}
	/**
	 * 获取登记薄索引树形数据
	 * 
	 * @作者 wuz
	 * 
	 * @return
	 */
	@RequestMapping(value = "/{bdcdyid}/{bdcdylx}/unittree", method = RequestMethod.GET)
	public @ResponseBody Message GetUnitTree(
			@PathVariable String bdcdyid,
			@PathVariable String bdcdylx) throws Exception {
		List<BookMenu> trs = bookService.GetUnitTree(bdcdyid, bdcdylx);
		Message msg = new Message();
		msg.setRows(trs);
		msg.setTotal(trs.size());
		return msg;
	}
	/**
	 * 根据不动产单元ID和不动产单元类型获取首页信息
	 * 
	 * @作者：WUZHU
	 * @param bdcdyid
	 *            不动产单元ID
	 * @param bdcydylx
	 *            不动产单元类型
	 * @return
	 */
	@RequestMapping(value = "/{bdcdyid}/{bdcdylx}/fminfo", method = RequestMethod.GET)
	public @ResponseBody Map GetFMInfo(@PathVariable("bdcdyid") String bdcdyid,
			@PathVariable("bdcdylx") String bdcdylx) {
		Map info = bookService.GetFMInfo(bdcdyid, bdcdylx);
		YwLogUtil.addYwLog("登记薄管理查看（宗地/宗海/农用地）详细信息，宗地/宗海/农用地代码：" + String.valueOf(info.get("ZDZHDM")), ConstValue.SF.YES.Value,ConstValue.LOG.SEARCH);
		return info;
	}
	
	/**
	 * 登记簿获取宗地图\房地产平面图
	 * @param bdcdyid
	 * @param response
	 * @author diaoliwei
	 * @date 2016-1-9 11:58:20
	 */
	@RequestMapping(value = "/{bdcdyid}/{bdcdylx}/zdtImg", method = RequestMethod.GET)
	@ResponseBody
	public void getZDTImg(@PathVariable("bdcdyid") String bdcdyid,@PathVariable("bdcdylx") String bdcdylx,HttpServletResponse response){
		try {
			Blob img = null;
			if(ConstValue.BDCDYLX.H.Value.equals(bdcdylx)){
				img = bookService.getFDCImg(bdcdyid);//实际是bdcdyh
			}else{
				img = bookService.getZDTImg(bdcdyid);
			}
			if(img != null){
				byte [] buf = new byte[1024];
			    InputStream in = img.getBinaryStream();
				OutputStream out = response.getOutputStream();
				int len;
				while((len = in.read(buf)) != -1){
				    out.write(buf, 0, len);
			    }
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取宗地基本信息
	 * 
	 * @作者 wuz
	 * 
	 * @return
	 */
	@RequestMapping(value = "/{bdcdyid}/zdxx", method = RequestMethod.GET)
	public @ResponseBody Map GetZDXX(@PathVariable("bdcdyid") String bdcdyid)
			throws Exception {
		Map zdjbxx = bookService.GetZDXX(bdcdyid);
		return zdjbxx;
	}

	/**
	 * 根据不动产单元ID获取宗海基本信息
	 * 
	 * @作者：WUZHU
	 * @param bdcdyid
	 *            不动产单元ID
	 * @return
	 */
	@RequestMapping(value = "/{bdcdyid}/zhxx", method = RequestMethod.GET)
	public @ResponseBody Map GetZHXX(@PathVariable("bdcdyid") String bdcdyid) {
		Map zhxx = bookService.GetZHXX(bdcdyid);
		return zhxx;
	}

	/**
	 * 不动产权利登记目录
	 * 
	 * @作者：俞学斌
	 * @param bdcdyid
	 *            不动产单元ID
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/{bdcdyid}/ml/{page}/{pagesize}", method = RequestMethod.GET)
	public @ResponseBody Map GetML(@PathVariable("bdcdyid") String bdcdyid,
			@PathVariable("page") String page,
			@PathVariable("pagesize") String pagesize) {
		long _page = 0;
		long _pagesize = 23;
		if (!StringUtils.isEmpty(page) && !page.equals("null"))
			_page = Long.parseLong(page);
		if (!StringUtils.isEmpty(pagesize) && !pagesize.equals("null"))
			_pagesize = Long.parseLong(pagesize);
		Map DYList = bookService.GetML(bdcdyid, _page, _pagesize);
		return DYList;
	}

	/**
	 * 根据不动产单元ID获取权利信息集合
	 * 
	 * @作者：WUZHU
	 * @param bdcdyid
	 *            不动产单元ID
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/{bdcdyid}/{bdcdylx}/{qllxOrDjlx}/djinfo/{page}/{pagesize}/{yh}", method = RequestMethod.GET)
	public @ResponseBody Map GetQLInfoList(
			@PathVariable("bdcdyid") String bdcdyid,
			@PathVariable("bdcdylx") String bdcdylx,
			@PathVariable("qllxOrDjlx") String qllxOrDjlx,
			@PathVariable("page") String page,
			@PathVariable("pagesize") String pagesize,
			@PathVariable("yh") String yh) throws UnsupportedEncodingException {
		long _page = 0;
		long _pagesize = 4;
        if(bdcdyid.equals(new String(bdcdyid.getBytes("iso8859-1"), "iso8859-1"))){
    		bdcdyid = new String(bdcdyid.getBytes("iso-8859-1"),"gb2312"); 
         }
		if (!StringUtils.isEmpty(page) && !page.equals("null"))
			_page = Long.parseLong(page);
		if (!StringUtils.isEmpty(pagesize) && !pagesize.equals("null"))
			_pagesize = Long.parseLong(pagesize);
		Map listQL = bookService.GetQLInfoList(bdcdyid, bdcdylx, qllxOrDjlx,
				_page, _pagesize);
		listQL.put("YH", yh);
		return listQL;
	}

	@RequestMapping(value = "{tpl}/{bdcdyid}/singlepdf", method = RequestMethod.POST)
	public @ResponseBody String SinglePDF(HttpServletRequest request,
			HttpServletResponse response, @PathVariable("tpl") String tpl,
			@PathVariable("bdcdyid") String bdcdyid) throws IOException,
			DocumentException {
		if (StringUtils.isEmpty(tpl))
			return "";
		String createdPDFFileName = bdcdyid + "-" + tpl;
		String dataString = request.getParameter("info");
		JSONObject object = JSON.parseObject(dataString);
		Map<String, Object> _data = new HashMap<String, Object>();
		for (Entry<String, Object> entry : object.entrySet()) {
			_data.put(entry.getKey(), entry.getValue());
		}
		PDFDataContainer pdfdata= new PDFHelper().new PDFDataContainer();
		Date Ptime = new Date();
		String Puser = Global.getCurrentUserName();
		_data.put("Ptime", StringHelper.FormatYmdhmsByDate(Ptime));
		_data.put("Puser", Puser);
		pdfdata.setData(_data);
		pdfdata.setPdfTemplateName(tpl);
		return PDFHelper.CreatePdf(pdfdata,createdPDFFileName, request);
	}
	@RequestMapping(value = "{bdcdyid}/allpdf", method = RequestMethod.POST)
	public @ResponseBody String AllPDF(HttpServletRequest request,
			HttpServletResponse response, @PathVariable("bdcdyid") String bdcdyid)
			throws IOException, DocumentException {
		String dataString = request.getParameter("info");//获取前台收到数据
		JSONArray urlJsonArray = JSONArray.parseArray(dataString);//还原成数组
		List<PDFDataContainer> pdfDatas=new  ArrayList<PDFDataContainer>(); //模板和填充数据的映射集合
		String createdPDFFileName =bdcdyid;//生产PDF文件名
		Date Ptime = new Date();
		String Puser = Global.getCurrentUserName();
		for (int j = 0; j < urlJsonArray.size(); j++) {
			JSONObject urlobj = (JSONObject) urlJsonArray.get(j);
			String tpl = "";
			String pageurl = "";
			for (Entry<String, Object> urlentry : urlobj.entrySet()) { // 这里只会有一个KEY
				tpl = urlentry.getKey();
				pageurl = String.valueOf(urlentry.getValue());
			}
			if (StringUtils.isEmpty(tpl))
				continue;
			if (StringUtils.isEmpty(pageurl))
				continue;
			String jsonString ="";
			if (DJBMenu.DJSY.getPrintTemplate().equals(tpl))//索引页的数据来自链接
				jsonString=pageurl;
			else
				jsonString=loadJson(pageurl);
			JSONObject jsonobjects = JSON.parseObject(jsonString);
			Map<String, Object> _data = new HashMap<String, Object>();
			for (Entry<String, Object> jsonobject : jsonobjects.entrySet()) {
				_data.put(jsonobject.getKey(), jsonobject.getValue());
			}
			_data.put("Ptime", StringHelper.FormatYmdhmsByDate(Ptime));
			_data.put("Puser", Puser);
			PDFDataContainer pdfdata= new PDFHelper().new PDFDataContainer();
			pdfdata.setPdfTemplateName(tpl);
			pdfdata.setData(_data);
			pdfDatas.add(pdfdata);
		}
		return PDFHelper.CreatePdfs(pdfDatas, createdPDFFileName, request); 
	}

	public static String loadJson(String url) {
		StringBuilder json = new StringBuilder();
		try {
			URL urlObject = new URL(url);
			URLConnection uc = urlObject.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(
					uc.getInputStream(), "utf-8"));
			String inputLine = null;
			while ((inputLine = in.readLine()) != null) {
				json.append(inputLine);
			}
			in.close();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return json.toString();
	}
	
	
	/**
	 * 批量解析房产存量cad图
	 * @return
	 */
	@RequestMapping(value = "/pljy", method = RequestMethod.GET)
	public String PLJY() {
		return prefix + "pljy";
	}
	
	@RequestMapping(value = "/UpdateTextToExcel", method = RequestMethod.POST)
	public @ResponseBody String UpdateTextToExcel(HttpServletRequest request,
			HttpServletResponse response)  throws Exception {	
		 response.setContentType("application/vnd.ms-excel");
		//所选字段
		String yh="第    本 第 "+RequestHelper.getParam(request, "yh") + " 页";
		String rowcount=RequestHelper.getParam(request, "rowcount");
		String fields=RequestHelper.getParam(request, "fields");
		JSONArray jsonArray = JSONArray.parseArray(fields);
		List<List<Map>> mapListJson = (List) jsonArray;
		
		//导出
		String basePath = request.getRealPath("/") + "\\resources\\PDF";
		String outpath = "";
		String url = "";
		FileOutputStream outstream = null;
		outpath = basePath + "\\tmp\\djbExcel.xls";
		url = request.getContextPath() + "\\resources\\PDF\\tmp\\djbExcel.xls";
	    outstream = new FileOutputStream(outpath); 

	    String tplFullName = request.getRealPath("/WEB-INF/jsp/wjmb/djb.xls");
	    InputStream input = new FileInputStream(tplFullName);
		HSSFWorkbook  wb = null;// 定义一个新的工作簿
		wb = new HSSFWorkbook(input);
		HSSFSheet sheet = wb.getSheetAt(0);
		Map<String,Integer> MapCol = new HashMap<String,Integer>();
		//获取标题行格式
		HSSFCellStyle style = sheet.getRow(2).getCell(0).getCellStyle();
		//添加标题行
		HSSFRow title = (HSSFRow)sheet.getRow(0);	
		HSSFCell Cell = title.getCell(0);
		Cell.setCellValue(yh) ;
		int rownum = 1;
		List<Integer> list_rowindex=new ArrayList<Integer>();
		for (List<Map> listM : mapListJson) {//行
			if(!StringHelper.isEmpty(listM.get(0).get("row"))){
				Integer rownidex=StringHelper.getInt(listM.get(0).get("row"));
				if(rownidex>=0&&!list_rowindex.contains(rownidex)){
					list_rowindex.add(rownidex);
				}
			}
		}
		for(int i=0;i<list_rowindex.size();i++){
			Integer rowindex=list_rowindex.get(i);
			for (List<Map> listM : mapListJson) {//行
				if(!StringHelper.isEmpty(listM.get(0).get("row"))){
					Integer rownidex1=StringHelper.getInt(listM.get(0).get("row"));
					if(rowindex==rownidex1){
						HSSFRow row = (HSSFRow)sheet.getRow(rowindex+1);	
						for(Map m:listM){
							Integer colidex=StringHelper.getInt(m.get("column"));
							if ((rowindex==2 && colidex==0) || (rowindex==2 && colidex==1)) {//内容/业务号
							}else if (rowindex==2) {//业务号
								HSSFCell Content_Cell = row.getCell(colidex-1);
								if (!StringHelper.isEmpty(m.get("value"))) {
									Content_Cell.setCellValue(StringHelper.formatObject(m.get("value")));
								}
							}else if (rowindex==1 && colidex== 1) {//坐落								
								HSSFCell Content_Cell = row.getCell(2);
								if (!StringHelper.isEmpty(m.get("value"))) {
									Content_Cell.setCellValue(StringHelper.formatObject(m.get("value")));
								}
							}else{
								HSSFCell Content_Cell = row.getCell(colidex);
								if (!StringHelper.isEmpty(m.get("value"))) {
									Content_Cell.setCellValue(StringHelper.formatObject(m.get("value")));
								}
							}
						}
						break;
					}
				}
			}
		}
		
		

	    wb.write(outstream); 
	    outstream.flush(); 
	    outstream.close();
	    //outstream = null;
	    return url;
	}
}
