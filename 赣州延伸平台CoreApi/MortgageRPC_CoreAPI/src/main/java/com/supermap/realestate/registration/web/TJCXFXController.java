package com.supermap.realestate.registration.web;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.supermap.realestate.registration.ViewClass.DJDY_LS_EX;
import com.supermap.realestate.registration.ViewClass.QLR_XZ_HOUSE_EX;
import com.supermap.realestate.registration.ViewClass.QL_LS_EX;
import com.supermap.realestate.registration.model.BDCS_QLR_LS;
import com.supermap.realestate.registration.model.BDCS_QLR_XZ;
import com.supermap.realestate.registration.model.T_GRIDCONFIG_USERDEFINEBOOK;
import com.supermap.realestate.registration.model.interfaces.House;
import com.supermap.realestate.registration.model.interfaces.OwnerLand;
import com.supermap.realestate.registration.model.interfaces.UseLand;
import com.supermap.realestate.registration.service.TJCXFXService;
import com.supermap.realestate.registration.util.ConstHelper;
import com.supermap.realestate.registration.util.ConstValue;
import com.supermap.realestate.registration.util.ConstValue.BDCDYLX;
import com.supermap.realestate.registration.util.ConstValue.FWYT;
import com.supermap.realestate.registration.util.DateUtil;
import com.supermap.realestate.registration.util.PDFHelper;
import com.supermap.realestate.registration.util.PDFHelper.PDFDataContainer;
import com.supermap.realestate.registration.util.RequestHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.realestate.registration.util.YwLogUtil;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import com.supermap.wisdombusiness.web.Message;
import com.supermap.wisdombusiness.web.ResultMessage;

import net.sf.json.JSONArray;

/**
 * 不动产统计查询分析
 * @author rongxf
 * http://localhost:8696/realestate/app/tjcxfx/GetQueryxzq
 */
@SuppressWarnings("deprecation")
@Controller
@RequestMapping("/tjcxfx")
public class TJCXFXController {
	
	/** 统计查询分析service */
	@Autowired
	private TJCXFXService tjcxfxService;
	@Autowired
	private CommonDao baseCommonDao;
	/**
	 * 获取行政区信息
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "/GetQueryxzq", method = RequestMethod.POST)
	public @ResponseBody Message GetQueryxzq_info(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException{
		Message msg = tjcxfxService.GetQueryxzq_info();
		return msg;
	}
	
	/**
	 * 房屋交易分析
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "/Getxz_fwdj_info", method = RequestMethod.POST)
	public @ResponseBody Message Getxz_fwdj_info(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException{
		String strxzq = request.getParameter("xzq");
		String fwlx = request.getParameter("fwlx");
		String tjfs = request.getParameter("tjfs");
		String tjdw = request.getParameter("tjdw");
		String kssj = request.getParameter("kssj");
		String jssj = request.getParameter("jssj");
//		String request.getParameter("zl")
		Message msg = tjcxfxService.Getxz_fwdj_info(strxzq,fwlx,tjfs,tjdw,kssj,jssj);
		return msg;
	}
	
	/**
	 * 土地交易分析
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "/Getxz_tdjyfx_info", method = RequestMethod.POST)
	public @ResponseBody Message Getxz_tdjyfx_info(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException{	
		String strxzq = request.getParameter("xzq");
		String fwlx = request.getParameter("fwlx");
		String tjfs = request.getParameter("tjfs");
		String tjdw = request.getParameter("tjdw");
		String kssj = request.getParameter("kssj");
		String jssj = request.getParameter("jssj");
//		String request.getParameter("zl")
		Message msg = tjcxfxService.Getxz_tdfxdj_info(strxzq,fwlx,tjfs,tjdw,kssj,jssj);
		return msg;
	}
	
	/**
	 * 土地交易分析
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "/Getxz_djywfx_info", method = RequestMethod.POST)
	public @ResponseBody Message Getxz_djywfx_info(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException{	
		String strxzq = request.getParameter("xzq");
		String fwlx = request.getParameter("fwlx");
		String tjdw = request.getParameter("tjdw");
		String kssj = request.getParameter("kssj");
		String jssj = request.getParameter("jssj");
//		String request.getParameter("zl")
		Message msg = tjcxfxService.Getxz_djywtj_info(strxzq,fwlx,tjdw,kssj,jssj);
		return msg;
	}
	
	/**
	 * 通过行政区，起始时间，终止时间对房屋交易报表
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "/Getxz_fwjybb_info", method = RequestMethod.POST)
	public @ResponseBody Message Getxz_fwjybb_info(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException{	
		String strxzq = request.getParameter("xzq");
		String kssj = request.getParameter("kssj");
		String jssj = request.getParameter("jssj");
		Message msg = tjcxfxService.Getxz_fwjybb_info(strxzq,kssj,jssj);
		return msg;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/Getxz_fwjybb_download", method = RequestMethod.GET)
	public @ResponseBody String Getxz_fwjybb_FJXX(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException{	
		String strxzq = request.getParameter("xzq");
		String kssj = request.getParameter("kssj");
		String jssj = request.getParameter("jssj");
		Message msg = tjcxfxService.Getxz_fwjybb_info(strxzq,kssj,jssj);
		String path = request.getRealPath("/WEB-INF/jsp/wjmb/房屋交易报表.xls");
		System.out.println(path);

		String fileName = path;
		InputStream input;
		try {
			input = new FileInputStream(fileName);
			HSSFWorkbook wb = null;// 定义一个新的工作簿
			try {
				wb = new HSSFWorkbook(input);
				Sheet sheet = wb.getSheetAt(0);
				//key 对应类型， value 对应列数 从0 开始
				Map MapType = new HashMap(); 
				MapType.put("100", 3);
				MapType.put("200", 2);
				
				Map MapFwlx = new HashMap();
				MapFwlx.put(FWYT.ZZ.Value,1); //住宅
				MapFwlx.put(FWYT.SYYF.Value,5); //商业用房
				MapFwlx.put(FWYT.BGYF.Value,9); //办公用房
				MapFwlx.put(FWYT.GYYF.Value,13); //工业
				MapFwlx.put(FWYT.CCYF.Value,17); //仓储
				MapFwlx.put(FWYT.CK.Value,21); //车库
				MapFwlx.put(FWYT.QT.Value,25); //其他
				List<Map> list = (List<Map>) msg.getRows();
			
				for(int i = 0; i < list.size(); i++) {
					//字段信息 如 h.FWLX,
					//ql.DJLX,
					//sum(h.ZDMJ) as cymj 
					//sum(FDCJYJG) as cjzj 
					//count(1) as cjts
					//sum(SCJZMJ) as cjjj					
					Map mapObj = list.get(i);
					String StrFwLx = (String) mapObj.get("FWLX");
					int RowNum = (Integer) MapFwlx.get(StrFwLx);
					String StrType = (String) mapObj.get("DJLX");
					int CellNum = (Integer) MapType.get(StrType);
					//行信息  成交总价行
			        HSSFRow rowcjzj = (HSSFRow)sheet.getRow(RowNum);
			        //列信息 
			        HSSFCell Cellcjzj = rowcjzj.getCell(CellNum);
			        Cellcjzj.setCellValue(mapObj.get("CJZJ").toString());
			        //行信息  成交面积
			        HSSFRow rowcymj = (HSSFRow)sheet.getRow(RowNum + 1);
			        //列信息 
			        HSSFCell Cellcymj = rowcymj.getCell(CellNum);
			        Cellcymj.setCellValue(mapObj.get("CYMJ").toString());
			        //行信息  成交面积
			        HSSFRow rowcjjj = (HSSFRow)sheet.getRow(RowNum+2);
			        //列信息 
			        HSSFCell Cellcjjj = rowcjjj.getCell(CellNum);
			        Cellcjjj.setCellValue(mapObj.get("CJJJ").toString());
			        //行信息  成交套数
			        HSSFRow rowcjts = (HSSFRow)sheet.getRow(RowNum+3);
			        //列信息 
			        HSSFCell Cellcjts = rowcjts.getCell(CellNum);
			        Cellcjts.setCellValue(mapObj.get("CJTS").toString());	
				}
				// 解决response中文乱码问题
				response.setContentType("text/html;charset=utf-8");	// 设置消息体的编码
				// 通过 http 协议  发送的http响应消息头  不能出现中文  中文必须要经过url编码
				String filename = URLEncoder.encode("房屋交易报表" + kssj + "-" + jssj + ".xls", "utf-8");
				response.setContentType("application/vnd.ms-excel");    
			    response.setHeader("Content-disposition", "attachment;filename=" + filename);    
			    
			    OutputStream ouputStream = response.getOutputStream(); 
			    wb.write(ouputStream); 
			    ouputStream.flush(); 
			    ouputStream.close();
			    ouputStream = null;
				
			} catch (IOException e) {
				e.printStackTrace();
			}
	        
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}  //建立输入流

		return "";
	}
	
	@RequestMapping(value = "/Getxz_QLXXXZ", method = RequestMethod.GET)
	public @ResponseBody Message Getxz_QLXXXZ(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException{	
		String strqlid = request.getParameter("qlid");
		Message msg = tjcxfxService.Getxz_QLXXXZ(strqlid);
		return msg;
	}
	
	/**
	 * 通过不动产单元ID获取权利信息列表，单元信息
	 * @param BDCDYID
	 * @return
	 */
	@RequestMapping(value = "/Getxz_BDCDYIDQLXX", method = RequestMethod.GET)
	public @ResponseBody Message Get_BDCDYID_ALLLC(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException{	
		String strBDCDYID = request.getParameter("bdcdyh");
		String strBDCDtype = request.getParameter("type");
		Message msg = tjcxfxService.Get_BDCDYID_ALLLC(strBDCDYID,strBDCDtype);
		return msg;
	}
	
	/**
	 * 通过不动产单元ID
	 * @param BDCDYID
	 * @return
	 */
	@RequestMapping(value = "/Getxz_BDCDYINFO", method = RequestMethod.GET)
	public @ResponseBody Message Get_BDCDYINFO(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException{	
		String strBDCDYID = request.getParameter("BDCDYH");
		String strBDCDtype = request.getParameter("type");
		Message msg = tjcxfxService.Get_BDCDYID_ALLLC(strBDCDYID,strBDCDtype);
		return msg;
	}
	
	/**
	 * 不动产单元查询   现状
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "/Queryxzzs", method = RequestMethod.POST)
	public @ResponseBody Message Queryxzzs(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException{
		Integer page = 1;
		if (request.getParameter("page") != null) {
			page = Integer.parseInt(request.getParameter("page"));
		}
		Integer rows = 10;
		if (request.getParameter("rows") != null) {
			rows = Integer.parseInt(request.getParameter("rows"));
		}
		String type = RequestHelper.getParam(request, "type"); // 类型：宗地，房屋-户
		// 坐落
		String zl = request.getParameter("zl");//RequestHelper.GetParam(request, "zl");
		// 不动产权证号
		String bdcqzh = request.getParameter("bdcqzh");// RequestHelper.GetParam(request, "bdcqzh");
		// 不动产单元号
		String bdcdyh = request.getParameter("bdcdyh");// RequestHelper.GetParam(request, "bdcdyh");
		String xm = request.getParameter("xm");//姓名
		String zjh = request.getParameter("zjh");//证件号
		Message msg = tjcxfxService.Getxzzs_Info(page,rows,zl, bdcqzh,bdcdyh,type,xm,zjh);
		return msg;
	}		
		
	/**
	 * 历史回溯
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "/Querylszs", method = RequestMethod.POST)
	public @ResponseBody Message Querylszs(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException{
		Integer page = 1;
		if (request.getParameter("page") != null) {
			page = Integer.parseInt(request.getParameter("page"));
		}
		Integer rows = 10;
		if (request.getParameter("rows") != null) {
			rows = Integer.parseInt(request.getParameter("rows"));
		}
		String type = RequestHelper.getParam(request, "type");// 类型：宗地，房屋-户
		// 坐落
		String zl = request.getParameter("zl");//RequestHelper.GetParam(request, "zl");
		// 不动产权证号
		String bdcqzh = request.getParameter("bdcqzh");// RequestHelper.GetParam(request, "bdcqzh");
		// 不动产单元号
		String bdcdyh = request.getParameter("bdcdyh");// RequestHelper.GetParam(request, "bdcdyh");		
		Message msg = tjcxfxService.Getlszs_Info(page,rows,zl, bdcqzh,bdcdyh,type);
		YwLogUtil.addYwLog("历史回溯查询" , ConstValue.SF.YES.Value,ConstValue.LOG.SEARCH);
		return msg;
	}
	
	/**
	 * 通过不同产单元号查询项目信息 历史
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "/QueryProject_History", method = RequestMethod.POST)
	public @ResponseBody Message QueryProject_History(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException{
//		Integer page = 1;
//		if (request.getParameter("page") != null) {
//			page = Integer.parseInt(request.getParameter("page"));
//		}
//		Integer rows = 10;
//		if (request.getParameter("rows") != null) {
//			rows = Integer.parseInt(request.getParameter("rows"));
//		}
		// 不动产单元号
		String bdcdyh = RequestHelper.getParam(request, "bdcdyh");		
		Message msg = tjcxfxService.GetProject_History(bdcdyh);
		return msg;
	}
	
	/**
	 * 房屋查询方法
	 * 备注：现在没使用（刘树峰）
	 * @作者 wuz
	 * 
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/houseQuery", method = RequestMethod.GET)
	public @ResponseBody Message GetHouseQueryList(HttpServletRequest request,
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
		mapCondition.put("QLRMC", request.getParameter("QLRMC"));
		mapCondition.put("ZJH", request.getParameter("ZJH"));
		Message m = tjcxfxService.GetHouseQueryList(mapCondition, page, rows);
		List<Map> newResult = new ArrayList<Map>();
		for(Object o: m.getRows())
		{
			QLR_XZ_HOUSE_EX qlr_xz_house_ex=(QLR_XZ_HOUSE_EX)o;
			  Map<String,String> _tmp=new HashMap<String,String>();
			  _tmp.put("H_BDCDYID",qlr_xz_house_ex.getHouse().getId());
			  _tmp.put("H_ZL",qlr_xz_house_ex.getHouse().getZL());
			  _tmp.put("H_FH",qlr_xz_house_ex.getHouse().getFH());
			  _tmp.put("QLR_QLRMC",qlr_xz_house_ex.getQLRMC());
			  _tmp.put("QLR_GYFS",ConstHelper.getNameByValue("GYFS",qlr_xz_house_ex.getGYFS()));
			  _tmp.put("H_GHYT",qlr_xz_house_ex.getHouse().getGHYT());
			  _tmp.put("H_SCJZMJ",StringHelper.FormatByDatatype(qlr_xz_house_ex.getHouse().getSCJZMJ()));
			  _tmp.put("H_SCTNJZMJ",StringHelper.FormatByDatatype(qlr_xz_house_ex.getHouse().getSCTNJZMJ()));
			  _tmp.put("H_SCFTJZMJ",StringHelper.FormatByDatatype(qlr_xz_house_ex.getHouse().getSCFTJZMJ()));
			  _tmp.put("EX_STATE_23",qlr_xz_house_ex.getEx().get("state_23"));
			  _tmp.put("EX_STATE_800",qlr_xz_house_ex.getEx().get("state_600"));
			  _tmp.put("EX_STATE_600",qlr_xz_house_ex.getEx().get("state_800")); 
			  newResult.add(_tmp);
		}
		Message newM=new Message();
		newM.setRows(newResult);
		newM.setTotal(m.getTotal());
		return newM;
	}
	/**
	 * 房屋查询打印
	 * 
	 * @作者 wuz
	 * 
	 * @return
	 */
	@RequestMapping(value = "/houseQueryPDF", method = RequestMethod.GET)
	public @ResponseBody String  CreateHouseQueryListPDF(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("utf-8");
		Map<String, String> mapCondition = new HashMap<String, String>();
		mapCondition.put("QLRMC", request.getParameter("QLRMC"));
		mapCondition.put("ZJH", request.getParameter("ZJH"));
		Message m = tjcxfxService.GetHouseQueryList(mapCondition, 1, 1000000);
		 Map<String,Object> _data=new HashMap<String,Object>();
		 Map<String,String> _validate=new HashMap<String,String>();//用于验证权利人名是否相同
		 int index_house=1;//查询房屋索引
		 int index_qlr=1;//权利人索引
		 if(m.getRows()==null)
			 return "";
		for(Object o: m.getRows())
		{
			QLR_XZ_HOUSE_EX qlr_xz_house_ex=(QLR_XZ_HOUSE_EX)o;
			StringBuilder qlrmc=new StringBuilder();
			if(!_validate.containsKey(qlr_xz_house_ex.getQLRMC()+qlr_xz_house_ex.getZJH()))//过滤相同权利人名
			{
			_data.put("QLR_QLRMC"+index_qlr,qlr_xz_house_ex.getQLRMC());
			_data.put("QLR_ZJH"+index_qlr,qlr_xz_house_ex.getZJH());
			_data.put("QLR_DZ"+index_qlr,qlr_xz_house_ex.getDZ());
			_data.put("QLR_BZ"+index_qlr,qlr_xz_house_ex.getBZ());
			_data.put("QLR_GYFS"+index_qlr,ConstHelper.getNameByValue("GYFS",qlr_xz_house_ex.getGYFS()));
			  index_qlr+=1;
			  _validate.put(qlr_xz_house_ex.getQLRMC()+qlr_xz_house_ex.getZJH(), "");
			}
			_data.put("H_ZL"+index_house,qlr_xz_house_ex.getHouse().getZL());
			_data.put("H_FH"+index_house,qlr_xz_house_ex.getHouse().getFH());
			int i=0;
			  for(BDCS_QLR_XZ qlr:qlr_xz_house_ex.getDjdy().getQlrs_4())
			 {
				  i+=1;
				  qlrmc.append(qlr.getQLRMC()) ;
				  if(i<qlr_xz_house_ex.getDjdy().getQlrs_4().size())
				  qlrmc.append("、");
			  }
				_data.put("H_QLRMCS"+index_house,qlrmc.toString());
				_data.put("H_STATE"+index_house,qlr_xz_house_ex.getEx().get("state_23")+"、"+qlr_xz_house_ex.getEx().get("state_600")+"、"+qlr_xz_house_ex.getEx().get("state_800"));
				//Global.getCurrentUserName();获取当前用户名
				//GetProperties.getConstValueByKey("XZQHMC");//行政区名称
				 //Date now=new Date();
				 //StringHelper.FormatYmdhmsByDate(now);//当前日期某年某月某日某分某秒
				index_house+=1;
		}
		String createdFileName=String.format("housequery-%s-%s",request.getParameter("QLRMC").hashCode()
				,request.getParameter("ZJH").hashCode());
		PDFDataContainer pdfdata= new PDFHelper().new PDFDataContainer();
		pdfdata.setPdfTemplateName("housequery");
		pdfdata.setData(_data);
	    return	PDFHelper.CreatePdf(pdfdata,createdFileName, request);
	}
	
	/**
	 * 获取不动产（土地/房屋）登记业务台账
	 * 
	 * @作者 wuz
	 * 
	 * @return
	 */
	@RequestMapping(value = "/workBookLand", method = RequestMethod.GET)
	public @ResponseBody Message GetWorkBookLandList(HttpServletRequest request,
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
		mapCondition.put("DJLX", request.getParameter("DJLX"));
		mapCondition.put("BDCDYH", request.getParameter("BDCDYH"));
		mapCondition.put("DJSJ_Q", request.getParameter("DJSJ_Q"));
		mapCondition.put("DJSJ_Z", request.getParameter("DJSJ_Z"));
		String lx = request.getParameter("LX");
		if(StringUtils.isEmpty(lx))
			lx = "land";
		mapCondition.put("LX",lx);
		Message m = tjcxfxService.GetWorkBookLand(mapCondition, page, rows);
		YwLogUtil.addYwLog("获取不动产（土地/房屋）登记业务台账", ConstValue.SF.YES.Value,ConstValue.LOG.SEARCH);
		return m;
	}
	/** 
	 * 抵押信息查询
	 * @作者 胡加红
	 * @创建时间 2016年3月17日上午10:50:40
	 * @param request
	 * @param response
	 * @return 
	 * @throws Exception
	 */
	
	@RequestMapping(value = "/diyinfo", method = RequestMethod.GET)
	public @ResponseBody Message GeDIYInfo(HttpServletRequest request,
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
		mapCondition.put("TJLX", request.getParameter("TJLX"));
		mapCondition.put("BDCDYH", request.getParameter("BDCDYH"));
		mapCondition.put("BDCQZH", request.getParameter("BDCQZH"));
		mapCondition.put("DJSJ_Q", request.getParameter("DJSJ_Q"));
		mapCondition.put("DJSJ_Z", request.getParameter("DJSJ_Z"));
		mapCondition.put("SLSJ_Q", request.getParameter("SLSJ_Q"));
		mapCondition.put("SLSJ_Z", request.getParameter("SLSJ_Z"));
		mapCondition.put("DJLX", request.getParameter("DJLX"));
		mapCondition.put("SQRLX", request.getParameter("SQRLX"));//抵押人类型 
		mapCondition.put("QLRMC", request.getParameter("QLRMC"));//抵押权人
		if(StringHelper.isEmpty(request.getParameter("ZL"))){
			mapCondition.put("ZL", "");
		}else{
			mapCondition.put("ZL", request.getParameter("ZL").trim());
		}
		if(StringHelper.isEmpty(request.getParameter("DYR"))){
			mapCondition.put("DYR", "");
		}else{
			mapCondition.put("DYR", request.getParameter("DYR").trim());
		}
		mapCondition.put("TDYT", request.getParameter("TDYT"));
		Message m = tjcxfxService.GetDiyInfo(mapCondition, page, rows);
		YwLogUtil.addYwLog("获取不动产抵押业务台账", ConstValue.SF.YES.Value,ConstValue.LOG.SEARCH);
		return m;
	}
	
	/** 
	 * 抵押台账面积、抵押金额合计汇总
	 * @作者 taochunda
	 * @创建时间 2017年10月23日下午15:42:19
	 * @param request
	 * @param response
	 * @return 
	 * @throws Exception
	 */
	
	@RequestMapping(value = "/diyinfo/hz", method = RequestMethod.GET)
	public @ResponseBody HashMap<String,String> GeDIYInfo_HZ(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("utf-8");
		Map<String, String> mapCondition = new HashMap<String, String>();
		mapCondition.put("TJLX", request.getParameter("TJLX"));
		mapCondition.put("BDCDYH", request.getParameter("BDCDYH"));
		mapCondition.put("BDCQZH", request.getParameter("BDCQZH"));
		mapCondition.put("DJSJ_Q", request.getParameter("DJSJ_Q"));
		mapCondition.put("DJSJ_Z", request.getParameter("DJSJ_Z"));
		mapCondition.put("SLSJ_Q", request.getParameter("SLSJ_Q"));
		mapCondition.put("SLSJ_Z", request.getParameter("SLSJ_Z"));
		mapCondition.put("DJLX", request.getParameter("DJLX"));
		if(StringHelper.isEmpty(request.getParameter("ZL"))){
			mapCondition.put("ZL", "");
		}else{
			mapCondition.put("ZL", request.getParameter("ZL").trim());
		}
		if(StringHelper.isEmpty(request.getParameter("DYR"))){
			mapCondition.put("DYR", "");
		}else{
			mapCondition.put("DYR", request.getParameter("DYR").trim());
		}
		mapCondition.put("TDYT", request.getParameter("TDYT"));
		HashMap<String,String> m = tjcxfxService.GetDiyInfo_HZ(mapCondition);
		YwLogUtil.addYwLog("获取不动产抵押业务台账汇总", ConstValue.SF.YES.Value,ConstValue.LOG.SEARCH);
		return m;
	}
	
	/** 
	 * 抵押信息查询
	 * @作者 胡加红
	 * @创建时间 2016年3月17日上午10:50:40
	 * @param request
	 * @param response
	 * @return 
	 * @throws Exception
	 */
	
	@RequestMapping(value = "/cfinfo", method = RequestMethod.GET)
	public @ResponseBody Message GeCFInfo(HttpServletRequest request,
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
		mapCondition.put("TJLX", request.getParameter("TJLX"));
		mapCondition.put("BDCDYH", request.getParameter("BDCDYH"));
		mapCondition.put("BDCQZH", request.getParameter("BDCQZH"));
		mapCondition.put("DJSJ_Q", request.getParameter("DJSJ_Q"));
		mapCondition.put("DJSJ_Z", request.getParameter("DJSJ_Z"));
		mapCondition.put("CXZT", request.getParameter("CXZT"));
		mapCondition.put("CFZT", request.getParameter("CFZT"));
		
		Message m = tjcxfxService.GetCFInfo(mapCondition, page, rows);
		YwLogUtil.addYwLog("获取不动产抵押业务台账", ConstValue.SF.YES.Value,ConstValue.LOG.SEARCH);
		return m;
	}
	//下载电子表格WUZHU
	@RequestMapping(value = "/workBookDownload", method = RequestMethod.GET)
	public @ResponseBody String WorkBookDownload(HttpServletRequest request, HttpServletResponse response) throws Exception{
		String lx = request.getParameter("LX");
		String djlx = request.getParameter("DJLX");
		String bdcdyh = request.getParameter("BDCDYH");
		String djsj_q=request.getParameter("DJSJ_Q");
		String djsj_z=request.getParameter("DJSJ_Z");
		Map<String, String> mapCondition = new HashMap<String, String>();
		mapCondition.put("DJSJ_Q", djsj_q);
		mapCondition.put("DJSJ_Z", djsj_z);
		mapCondition.put("DJLX", djlx);
		mapCondition.put("BDCDYH", bdcdyh);
		mapCondition.put("LX",lx);
		if(StringUtils.isEmpty(lx))
			lx = "land";
		List<DJDY_LS_EX> djdys = tjcxfxService.WorkBookDownload(mapCondition, 1, 100000);
		
		String basePath = request.getRealPath("/") + "\\resources\\PDF";
		String outpath = "";
		String url = "";
		String tmpFullName = "";
		FileOutputStream outstream = null;
		if(lx.equals("house")){
			outpath = basePath + "\\tmp\\workBookHouse.xls";
			url = request.getContextPath() + "\\resources\\PDF\\tmp\\workBookHouse.xls";
		    outstream = new FileOutputStream(outpath); 
		    tmpFullName = request.getRealPath("/WEB-INF/jsp/wjmb/workBookHouse.xls");
		    InputStream input = new FileInputStream(tmpFullName);
			HSSFWorkbook  wb = null;// 定义一个新的工作簿
			wb = new HSSFWorkbook(input);
			Sheet sheet = wb.getSheetAt(0);
			Map<String,Integer> MapCol = new HashMap<String,Integer>();
			MapCol.put("业务号", 0);
			MapCol.put("登记时间", 1);
			MapCol.put("登记类型", 2);
			MapCol.put("登记原因", 3);
			MapCol.put("不动产单元号", 4);
			MapCol.put("房屋座落", 5);
			MapCol.put("房号", 6);
			MapCol.put("建筑面积", 7);
			MapCol.put("套内建筑面积", 8);
			MapCol.put("分摊面积", 9);
			MapCol.put("分摊土地面积", 10);
			MapCol.put("房屋用途", 11);
			MapCol.put("权利人", 12);
			MapCol.put("证件类别", 13);
			MapCol.put("权利人证件号", 14);
			MapCol.put("义务人", 15);
			MapCol.put("不动产权证（登记证明）号", 16);
			MapCol.put("抵押状态", 17);
			MapCol.put("查封状态", 18);
			MapCol.put("异议状态", 19);
			
			MapCol.put("权利人类型", 20);
			MapCol.put("权利类型", 21);
			MapCol.put("土地性质", 22);
			MapCol.put("土地使用权起始日期", 23);
			MapCol.put("土地使用权结束日期", 24);
			MapCol.put("房屋性质", 25);
			MapCol.put("房屋土地性质", 26);
			MapCol.put("房屋土地使用权起始日期", 27);
			MapCol.put("房屋土地使用权起始日期", 28);
			
            int rownum = 1;
			for(DJDY_LS_EX djdy:djdys){
				  //HSSFRow row = (HSSFRow)sheet.getRow(rownum);
				  for(QL_LS_EX ql_ls_ex: djdy.getQl_ls_ex()){
				@SuppressWarnings("rawtypes")
				List<Map> sqrxms = baseCommonDao.getDataListByFullSql( "SELECT SQR.SQRXM FROM BDCK.BDCS_SQR SQR WHERE SQR.SQRLB='2' "+ " AND SQR.XMBH='" + ql_ls_ex.getXMBH() + "'");
				
				for(BDCS_QLR_LS bdcs_qlr_ls:ql_ls_ex.getQlr_lss()){
						 if(!StringUtils.isEmpty(djlx)&& !djlx.equals(ql_ls_ex.getDJLX())){
						    continue;
						 }
						 if(!StringUtils.isEmpty(djsj_q)){
							 Date djsj_q_data= DateUtil.convertStringToDate(djsj_q);
							 if(!djsj_q_data.before(ql_ls_ex.getDJSJ()))
							     continue;
							 }
						 if(!StringUtils.isEmpty(djsj_z)){
							 Date djsj_z_data= DateUtil.addDay(DateUtil.convertStringToDate(djsj_z),1);//时间比较的临界值
							 if(!djsj_z_data.after(ql_ls_ex.getDJSJ()))
							     continue;
							 }
						 
						 if(!StringUtils.isEmpty(djlx)&& !djlx.equals(ql_ls_ex.getDJLX())){
							    continue;
							 }
						 
						 HSSFRow row = (HSSFRow)sheet.createRow(rownum);
				         HSSFCell Cell0 = row.createCell(MapCol.get("业务号"));
				         Cell0.setCellValue(ql_ls_ex.getYWH());
				         HSSFCell Cell1 = row.createCell(MapCol.get("登记时间"));
				         Cell1.setCellValue(StringHelper.FormatByDatetime(ql_ls_ex.getDJSJ()));
				         HSSFCell Cell2 = row.createCell(MapCol.get("登记类型"));
				         Cell2.setCellValue(ConstHelper.getNameByValue("DJLX",ql_ls_ex.getDJLX()));
				         HSSFCell Cell3 = row.createCell(MapCol.get("登记原因"));
				         Cell3.setCellValue(ql_ls_ex.getDJYY());
				         HSSFCell Cell4 = row.createCell(MapCol.get("不动产单元号"));
				         Cell4.setCellValue(djdy.getBDCDYH());
				         HSSFCell Cell5 = row.createCell(MapCol.get("房屋座落"));
				         House u = (House)djdy.getRealUnit();
				         if(u != null) {
				             Cell5.setCellValue(u.getZL());
				             HSSFCell Cell7 = row.createCell(MapCol.get("房号"));
				             Cell7.setCellValue(String.valueOf(u.getFH()));
				             HSSFCell Cell8 = row.createCell(MapCol.get("建筑面积"));
				             Cell8.setCellValue(String.valueOf(u.getSCJZMJ()));
				             HSSFCell Cell9 = row.createCell(MapCol.get("套内建筑面积"));
				             Cell9.setCellValue(String.valueOf(u.getSCTNJZMJ()));
				             HSSFCell Cell10 = row.createCell(MapCol.get("分摊面积"));
				             Cell10.setCellValue(String.valueOf(u.getSCFTJZMJ()));
				             HSSFCell Cell11 = row.createCell(MapCol.get("分摊土地面积"));
				             Cell11.setCellValue(String.valueOf(u.getFTTDMJ()));
				             HSSFCell Cell12 = row.createCell(MapCol.get("房屋用途"));
				             Cell12.setCellValue(ConstHelper.getNameByValue("YT", u.getFWYT1()));
				         }
				         HSSFCell Cell13 = row.createCell(MapCol.get("权利人"));
				         Cell13.setCellValue(bdcs_qlr_ls.getQLRMC());
				         HSSFCell Cell14 = row.createCell(MapCol.get("证件类别"));
				         Cell14.setCellValue(ConstHelper.getNameByValue("ZJLX",bdcs_qlr_ls.getZJZL()));
				         HSSFCell Cell15 = row.createCell(MapCol.get("权利人证件号"));
				         Cell15.setCellValue(bdcs_qlr_ls.getZJH());
				         HSSFCell Cell16 = row.createCell(MapCol.get("义务人"));
				         if (sqrxms== null||sqrxms.isEmpty()){ Cell16.setCellValue("");}else{
						         Cell16.setCellValue(sqrxms.get(0).get("SQRXM").toString());
							}
				         HSSFCell Cell17 = row.createCell(MapCol.get("不动产权证（登记证明）号"));
				         Cell17.setCellValue(bdcs_qlr_ls.getBDCQZH());
				         HSSFCell Cell18 = row.createCell(MapCol.get("抵押状态"));
				         Cell18.setCellValue(djdy.getEx().get("state_23"));
				         HSSFCell Cell19 = row.createCell(MapCol.get("查封状态"));
				         Cell19.setCellValue(djdy.getEx().get("state_800"));
				         HSSFCell Cell20 = row.createCell(MapCol.get("异议状态"));
				         Cell20.setCellValue(djdy.getEx().get("state_600"));
				         
				         HSSFCell Cell21 = row.createCell(MapCol.get("权利人类型"));
				         Cell21.setCellValue(bdcs_qlr_ls.getQLRLXName());
				         HSSFCell Cell22 = row.createCell(MapCol.get("权利类型"));
				         Cell22.setCellValue(ql_ls_ex.getQLLXName());
				         if(u != null) {
				        	 //@zhaomengfan 不建议去读宗地的属性和权力
//				        	 RealUnit zd = UnitTools.loadUnit(BDCDYLX.SHYQZD, DJDYLY.LS, u.getZDBDCDYID());
//				        	 if(zd != null) {
				        		 HSSFCell Cell23 = row.createCell(MapCol.get("土地性质"));
						         Cell23.setCellValue("");
						         HSSFCell Cell24 = row.createCell(MapCol.get("土地使用权起始日期"));
						         Cell24.setCellValue("");
						         HSSFCell Cell25 = row.createCell(MapCol.get("土地使用权结束日期"));
						         Cell25.setCellValue("");
//				        	 }
					         HSSFCell Cell26 = row.createCell(MapCol.get("房屋性质"));
					         Cell26.setCellValue(ConstHelper.getNameByValue("FWXZ",u.getFWXZ()));
					         HSSFCell Cell27 = row.createCell(MapCol.get("房屋土地性质"));
					         Cell27.setCellValue(ConstHelper.getNameByValue("TDYT",u.getFWTDYT()));
					         HSSFCell Cell28 = row.createCell(MapCol.get("房屋土地使用权起始日期"));
					         Cell28.setCellValue(StringHelper.FormatByDatetime(u.getTDSYQQSRQ()));
					         HSSFCell Cell29 = row.createCell(MapCol.get("房屋土地使用权起始日期"));
					         Cell29.setCellValue(StringHelper.FormatByDatetime(u.getTDSYQZZRQ()));
				         }
				         
				         rownum += 1;
					  }
					  
				  }
			}
			 wb.write(outstream); 
			 outstream.flush(); 
			 outstream.close();
		 }
		 if(lx.equals("land")){ 
			 outpath = basePath + "\\tmp\\workBookLand.xls";
			 url = request.getContextPath() + "\\resources\\PDF\\tmp\\workBookLand.xls";
			 outstream = new FileOutputStream(outpath); 
			 tmpFullName = request.getRealPath("/WEB-INF/jsp/wjmb/workBookLand.xls");
			 InputStream input = new FileInputStream(tmpFullName);
				HSSFWorkbook wb = null;// 定义一个新的工作簿
				wb = new HSSFWorkbook(input);
				Sheet sheet = wb.getSheetAt(0);
				Map<String,Integer> MapCol = new HashMap<String,Integer>();
				MapCol.put("业务号", 0);
				MapCol.put("登记时间", 1);
				MapCol.put("登记类型", 2);
				MapCol.put("登记原因", 3);
				MapCol.put("不动产单元号", 4);
				MapCol.put("宗地座落", 5);
				MapCol.put("宗地面积", 6);
				MapCol.put("权利人", 7);
				MapCol.put("证件类别", 8);
				MapCol.put("权利人证件号", 9);
				MapCol.put("义务人", 10);
				MapCol.put("不动产权证（登记证明）号", 11);
				MapCol.put("抵押状态", 12);
				MapCol.put("查封状态", 13);
				MapCol.put("异议状态", 14);
				int rownum = 1;
				for(DJDY_LS_EX djdy:djdys){
					 //HSSFRow row = (HSSFRow)sheet.getRow(rownum);
					 for(QL_LS_EX ql_ls_ex: djdy.getQl_ls_ex()){
					@SuppressWarnings("rawtypes")
					List<Map> sqrxms = baseCommonDao.getDataListByFullSql( "SELECT SQR.SQRXM FROM BDCK.BDCS_SQR SQR WHERE SQR.SQRLB='2' "+ " AND SQR.XMBH='" + ql_ls_ex.getXMBH() + "'");
						  for(BDCS_QLR_LS bdcs_qlr_ls:ql_ls_ex.getQlr_lss()){
							 if(djlx != null && djlx != "" && !djlx.equals(ql_ls_ex.getDJLX())){
							    continue;
							 }  
							 HSSFRow row = (HSSFRow)sheet.createRow(rownum);
					         HSSFCell Cell0 = row.createCell(MapCol.get("业务号"));
					         Cell0.setCellValue(ql_ls_ex.getYWH());
					         HSSFCell Cell1 = row.createCell(MapCol.get("登记时间"));
					         Cell1.setCellValue(StringHelper.FormatByDatetime(ql_ls_ex.getDJSJ()));
					         HSSFCell Cell2 = row.createCell(MapCol.get("登记类型"));
					         Cell2.setCellValue(ConstHelper.getNameByValue("DJLX",ql_ls_ex.getDJLX()));
					         HSSFCell Cell3 = row.createCell(MapCol.get("登记原因"));
					         Cell3.setCellValue(ql_ls_ex.getDJYY());
					         HSSFCell Cell4 = row.createCell(MapCol.get("不动产单元号"));
					         Cell4.setCellValue(djdy.getBDCDYH());
					         HSSFCell Cell5 = row.createCell(MapCol.get("宗地座落"));
					         if(BDCDYLX.SYQZD.Value.equals(djdy.getBDCDYLX())){
					             OwnerLand u = (OwnerLand)djdy.getRealUnit();
					             if(u != null){
					                 Cell5.setCellValue(u.getZL());
					                 HSSFCell Cell6 = row.createCell(MapCol.get("宗地面积"));
					                 Cell6.setCellValue(String.valueOf(u.getZDMJ()));
					             }
					         }
					         if(BDCDYLX.SHYQZD.Value.equals(djdy.getBDCDYLX())){
					             UseLand u = (UseLand)djdy.getRealUnit();
					             if(u != null){
					                 Cell5.setCellValue(u.getZL());
					                 HSSFCell Cell6 = row.createCell(MapCol.get("宗地面积"));
					                 Cell6.setCellValue(String.valueOf(u.getZDMJ()));
					             }
					         }
					         HSSFCell Cell7 = row.createCell(MapCol.get("权利人"));
					         Cell7.setCellValue(bdcs_qlr_ls.getQLRMC());
					         HSSFCell Cell8 = row.createCell(MapCol.get("证件类别"));
					         Cell8.setCellValue(ConstHelper.getNameByValue("ZJLX",bdcs_qlr_ls.getZJZL()));
					         HSSFCell Cell9 = row.createCell(MapCol.get("权利人证件号"));
					         Cell9.setCellValue(bdcs_qlr_ls.getZJH());
					         HSSFCell Cell10 = row.createCell(MapCol.get("义务人"));
					         if (sqrxms== null||sqrxms.isEmpty()){ Cell10.setCellValue("");}else{
							         Cell10.setCellValue(sqrxms.get(0).get("SQRXM").toString());
								}
					         HSSFCell Cell11 = row.createCell(MapCol.get("不动产权证（登记证明）号"));
					         Cell11.setCellValue(bdcs_qlr_ls.getBDCQZH());
					         HSSFCell Cell12 = row.createCell(MapCol.get("抵押状态"));
					         Cell12.setCellValue(djdy.getEx().get("state_23"));
					         HSSFCell Cell13 = row.createCell(MapCol.get("查封状态"));
					         Cell13.setCellValue(djdy.getEx().get("state_800"));
					         HSSFCell Cell14 = row.createCell(MapCol.get("异议状态"));
					         Cell14.setCellValue(djdy.getEx().get("state_600"));
					         rownum += 1;
						  }
					  }
				}
				 wb.write(outstream); 
				 outstream.flush(); 
				 outstream.close();
		 }
        return url;
	}
	
	@SuppressWarnings({ "rawtypes" })
	@RequestMapping(value = "/diyinfodownload", method = RequestMethod.GET)
	public @ResponseBody String DiyInfoDownload(HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		String tjlx = request.getParameter("TJLX");
		String bdcdyh = request.getParameter("BDCDYH");
		String bdcqzh = request.getParameter("BDCQZH");
		String djsj_q=request.getParameter("DJSJ_Q");
		String djsj_z=request.getParameter("DJSJ_Z");
		Map<String, String> mapCondition = new HashMap<String, String>();
		mapCondition.put("DJSJ_Q", djsj_q);
		mapCondition.put("DJSJ_Z", djsj_z);
		mapCondition.put("TJLX", tjlx);
		mapCondition.put("BDCDYH", bdcdyh);		
		mapCondition.put("BDCQZH", bdcqzh);
		mapCondition.put("SLSJ_Q", request.getParameter("SLSJ_Q"));
		mapCondition.put("SLSJ_Z", request.getParameter("SLSJ_Z"));
		mapCondition.put("DJLX", request.getParameter("DJLX"));
		if(StringHelper.isEmpty(request.getParameter("ZL"))){
			mapCondition.put("ZL", "");
		}else{
			mapCondition.put("ZL", request.getParameter("ZL").trim());
		}
		if(StringHelper.isEmpty(request.getParameter("DYR"))){
			mapCondition.put("DYR", "");
		}else{
			mapCondition.put("DYR", request.getParameter("DYR").trim());
		}
		mapCondition.put("TDYT", request.getParameter("TDYT"));
		List<Map> djdys = tjcxfxService.DiyInfoDownload(mapCondition);
		
		String basePath = request.getRealPath("/") + "\\resources\\PDF";
		String outpath = "";
		String url = "";
		String tmpFullName = "";
		FileOutputStream outstream = null;
		if(djdys != null && djdys.size() > 0){
			outpath = basePath + "\\tmp\\diyInfo.xls";
			url = request.getContextPath() + "\\resources\\PDF\\tmp\\diyInfo.xls";
		    outstream = new FileOutputStream(outpath); 
		    tmpFullName = request.getRealPath("/WEB-INF/jsp/wjmb/diyInfo.xls");
		    InputStream input = new FileInputStream(tmpFullName);
			HSSFWorkbook  wb = null;// 定义一个新的工作簿
			wb = new HSSFWorkbook(input);
			Sheet sheet = wb.getSheetAt(0);
			Map<String,Integer> MapCol = new HashMap<String,Integer>();
			MapCol.put("序号", 0);
			MapCol.put("不动产坐落", 1);
			MapCol.put("不动产单元号", 2);
			MapCol.put("不动产权证号", 3);
			MapCol.put("抵押权人", 4);
			MapCol.put("抵押人", 5);
			MapCol.put("抵押类型", 6);
			MapCol.put("抵押金额", 7);
			MapCol.put("金额单位", 8);
			MapCol.put("不动产证明号", 9);
			MapCol.put("债务履行起始时间", 10);
			MapCol.put("债务履行结束时间", 11);
			MapCol.put("登记类型", 12);
			MapCol.put("登记时间", 13);
			MapCol.put("登记原因", 14);	
			MapCol.put("受理时间", 15);	
			MapCol.put("宗地面积", 16);	
			MapCol.put("建筑面积", 17);	
			MapCol.put("房屋用途", 18);	
			MapCol.put("土地用途", 19);	
			MapCol.put("评估价值", 20);	
			MapCol.put("受理编号", 21);
			MapCol.put("权利性质", 22);
			MapCol.put("分摊土地面积",23);
			MapCol.put("办结时间",24);
			int rownum = 1;
			for(Map djdy:djdys){
				 
		  		 HSSFRow row = (HSSFRow)sheet.createRow(rownum);
		  		 try{
			         HSSFCell Cell0 = row.createCell(MapCol.get("序号"));
			         Cell0.setCellValue(StringHelper.formatObject(djdy.get("XH")));
			         HSSFCell Cell1 = row.createCell(MapCol.get("不动产坐落"));
			         Cell1.setCellValue(StringHelper.formatObject(djdy.get("ZL")));
			         HSSFCell Cell2 = row.createCell(MapCol.get("不动产单元号"));
			         Cell2.setCellValue(StringHelper.formatObject(djdy.get("BDCDYH")));
			         HSSFCell Cell3 = row.createCell(MapCol.get("不动产权证号"));
			         Cell3.setCellValue(StringHelper.formatObject(djdy.get("SYQZH")));
			         HSSFCell Cell4 = row.createCell(MapCol.get("抵押权人"));
			         Cell4.setCellValue(StringHelper.formatObject(djdy.get("QLRMC")));
			         HSSFCell Cell5 = row.createCell(MapCol.get("抵押人"));
			         Cell5.setCellValue(StringHelper.formatObject(djdy.get("DYR")));
			         HSSFCell Cell6 = row.createCell(MapCol.get("抵押类型"));
			         Cell6.setCellValue(StringHelper.formatObject(djdy.get("DYFS")));
			         HSSFCell Cell7 = row.createCell(MapCol.get("抵押金额"));
			         Cell7.setCellValue(StringHelper.formatObject(djdy.get("DYE")));
			         HSSFCell Cell7e = row.createCell(MapCol.get("金额单位"));
			         Cell7e.setCellValue(ConstHelper.getNameByValue("JEDW", StringHelper.formatObject(djdy.get("ZQDW"))));
			         HSSFCell Cell8 = row.createCell(MapCol.get("不动产证明号"));
			         Cell8.setCellValue(StringHelper.formatObject(djdy.get("BDCQZH")));
			         HSSFCell Cell9 = row.createCell(MapCol.get("债务履行起始时间"));
			         Cell9.setCellValue(StringHelper.formatObject(djdy.get("QLQSSJ")));
			         HSSFCell Cell10 = row.createCell(MapCol.get("债务履行结束时间"));
			         Cell10.setCellValue(StringHelper.formatObject(djdy.get("QLJSSJ")));
			         HSSFCell Cell11 = row.createCell(MapCol.get("登记类型"));
			         Cell11.setCellValue(StringHelper.formatObject(djdy.get("DJLX")));
			         HSSFCell Cell12 = row.createCell(MapCol.get("登记时间"));
			         Cell12.setCellValue(StringHelper.formatObject(djdy.get("DJSJ")));
			         HSSFCell Cell13 = row.createCell(MapCol.get("登记原因"));
			         Cell13.setCellValue(StringHelper.formatObject(djdy.get("DJYY")));
			         HSSFCell Cell14 = row.createCell(MapCol.get("受理时间"));
			         Cell14.setCellValue(StringHelper.formatObject(djdy.get("SLSJ")));
			         HSSFCell Cell15 = row.createCell(MapCol.get("宗地面积"));
			         Cell15.setCellValue(StringHelper.formatObject(djdy.get("SYQMJ")));
			         /*if(djdy.get("SYQMJ")!=null){
			        	 Cell15.setCellValue(StringHelper.formatObject(djdy.get("SYQMJ"))+"㎡");
			         }else{
			        	 Cell15.setCellValue(StringHelper.formatObject(djdy.get("SYQMJ")));
			         }*/
			         HSSFCell Cell16 = row.createCell(MapCol.get("建筑面积"));
			         Cell16.setCellValue(StringHelper.formatObject(djdy.get("JZMJ")));
			         /*if(djdy.get("JZMJ")!=null){
			        	 Cell16.setCellValue(StringHelper.formatObject(djdy.get("JZMJ"))+"㎡");
			         }else{
			        	 Cell16.setCellValue(StringHelper.formatObject(djdy.get("JZMJ")));
			         }*/
			         HSSFCell Cell17 = row.createCell(MapCol.get("房屋用途"));
			         Cell17.setCellValue(StringHelper.formatObject(djdy.get("FWYT")));
			         HSSFCell Cell18 = row.createCell(MapCol.get("土地用途"));
			         Cell18.setCellValue(StringHelper.formatObject(djdy.get("TDYT")));
			         HSSFCell Cell19 = row.createCell(MapCol.get("评估价值"));
			         Cell19.setCellValue(StringHelper.formatObject(djdy.get("PGJZ")));
			         HSSFCell Cell20 = row.createCell(MapCol.get("受理编号"));
			         Cell20.setCellValue(StringHelper.formatObject(djdy.get("YWLSH")));
			         HSSFCell Cell21 = row.createCell(MapCol.get("权利性质"));
			         Cell21.setCellValue(StringHelper.formatObject(djdy.get("QLXZ")));
			         HSSFCell Cell22 = row.createCell(MapCol.get("分摊土地面积"));
			         Cell22.setCellValue(StringHelper.formatObject(djdy.get("FTTDMJ")));
			         HSSFCell Cell23 = row.createCell(MapCol.get("办结时间"));
			         Cell23.setCellValue(StringHelper.formatObject(djdy.get("DJSJ")));
			         rownum ++;
		  		 }
		  		 catch(Exception ex){
		  			 System.out.println("导出数据有误");
		  			ex.printStackTrace();
		  		 }
			  }	  		
			 wb.write(outstream); 
			 outstream.flush(); 
			 outstream.close();
		 }

        return url;
	}
	
	@SuppressWarnings({ "rawtypes" })
	@RequestMapping(value = "/cfinfodownload", method = RequestMethod.GET)
	public @ResponseBody String CFInfoDownload(HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		String tjlx = request.getParameter("TJLX");
		String bdcdyh = request.getParameter("BDCDYH");
		String bdcqzh = request.getParameter("BDCQZH");
		String djsj_q=request.getParameter("DJSJ_Q");
		String djsj_z=request.getParameter("DJSJ_Z");
		Map<String, String> mapCondition = new HashMap<String, String>();
		mapCondition.put("DJSJ_Q", djsj_q);
		mapCondition.put("DJSJ_Z", djsj_z);
		mapCondition.put("TJLX", tjlx);
		mapCondition.put("BDCDYH", bdcdyh);		
		mapCondition.put("BDCQZH", bdcqzh);
		mapCondition.put("CXZT", request.getParameter("CXZT"));
		mapCondition.put("CFZT", request.getParameter("CFZT"));
		
		List<Map> djdys = tjcxfxService.CFInfoDownload(mapCondition);
		
		String basePath = request.getRealPath("/") + "\\resources\\PDF";
		String outpath = "";
		String url = "";
		String tmpFullName = "";
		FileOutputStream outstream = null;
		if(djdys != null && djdys.size() > 0){
			outpath = basePath + "\\tmp\\cfInfo.xls";
			url = request.getContextPath() + "\\resources\\PDF\\tmp\\cfInfo.xls";
		    outstream = new FileOutputStream(outpath); 
		    tmpFullName = request.getRealPath("/WEB-INF/jsp/wjmb/cfInfo.xls");
		    InputStream input = new FileInputStream(tmpFullName);
			HSSFWorkbook  wb = null;// 定义一个新的工作簿
			wb = new HSSFWorkbook(input);
			Sheet sheet = wb.getSheetAt(0);
			Map<String,Integer> MapCol = new HashMap<String,Integer>();
			MapCol.put("序号", 0);
			MapCol.put("不动产坐落", 1);
			MapCol.put("不动产单元号", 2);
			MapCol.put("权利人", 3);
			MapCol.put("不动产权证号", 4);
			MapCol.put("查封机关", 5);
			MapCol.put("查封类型", 6);
			MapCol.put("查封文号", 7);
			MapCol.put("查封文件", 8);
			MapCol.put("查封起始时间", 9);
			MapCol.put("查封结束时间", 10);
			MapCol.put("查封范围", 11);
			MapCol.put("查封登记时间", 12);
			MapCol.put("附记", 13);			
            int rownum = 1;
			for(Map djdy:djdys){
				 
		  		 HSSFRow row = (HSSFRow)sheet.createRow(rownum);
		  		 try{
			         HSSFCell Cell0 = row.createCell(MapCol.get("序号"));
			         Cell0.setCellValue(StringHelper.formatObject(djdy.get("XH")));
			         HSSFCell Cell1 = row.createCell(MapCol.get("不动产坐落"));
			         Cell1.setCellValue(StringHelper.formatObject(djdy.get("ZL")));
			         HSSFCell Cell2 = row.createCell(MapCol.get("不动产单元号"));
			         Cell2.setCellValue(StringHelper.formatObject(djdy.get("BDCDYH")));
			         HSSFCell Cell3 = row.createCell(MapCol.get("权利人"));
			         Cell3.setCellValue(StringHelper.formatObject(djdy.get("QLRMC")));
			         HSSFCell Cell4 = row.createCell(MapCol.get("不动产权证号"));
			         Cell4.setCellValue(StringHelper.formatObject(djdy.get("BDCQZH")));
			         HSSFCell Cell5 = row.createCell(MapCol.get("查封机关"));
			         Cell5.setCellValue(StringHelper.formatObject(djdy.get("CFJG")));
			         HSSFCell Cell6 = row.createCell(MapCol.get("查封类型"));
			         Cell6.setCellValue(StringHelper.formatObject(djdy.get("CFLX")));
			         HSSFCell Cell7 = row.createCell(MapCol.get("查封文号"));
			         Cell7.setCellValue(StringHelper.formatObject(djdy.get("CFWH")));
			         HSSFCell Cell8 = row.createCell(MapCol.get("查封文件"));
			         Cell8.setCellValue(StringHelper.formatObject(djdy.get("CFWJ")));
			         HSSFCell Cell9 = row.createCell(MapCol.get("查封起始时间"));
			         Cell9.setCellValue(StringHelper.formatObject(djdy.get("QLQSSJ")));
			         HSSFCell Cell10 = row.createCell(MapCol.get("查封结束时间"));
			         Cell10.setCellValue(StringHelper.formatObject(djdy.get("QLJSSJ")));
			         HSSFCell Cell11 = row.createCell(MapCol.get("查封范围"));
			         Cell11.setCellValue(StringHelper.formatObject(djdy.get("CFFW")));
			         HSSFCell Cell12 = row.createCell(MapCol.get("查封登记时间"));
			         Cell12.setCellValue(StringHelper.formatObject(djdy.get("DJSJ")));
			         HSSFCell Cell13 = row.createCell(MapCol.get("附记"));
			         Cell13.setCellValue(StringHelper.formatObject(djdy.get("FJ")));
			         rownum ++;
		  		 }
		  		 catch(Exception ex){
		  			System.out.println("导出数据有误");
		  			ex.printStackTrace();
		  		 }
			  }	  		
			 wb.write(outstream); 
			 outstream.flush(); 
			 outstream.close();
		 }

        return url;
	}
	
	
	/** 
	 * 不动产登记台账
	 * @作者 俞学斌
	 * @创建时间 2016年4月11日18:21:40
	 * @param request
	 * @param response
	 * @return 
	 * @throws Exception
	 */
	@RequestMapping(value = "/standingbook", method = RequestMethod.GET)
	public @ResponseBody Message GetStandingBook(HttpServletRequest request,
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
		String sort = RequestHelper.getParam(request, "sort");// 排序字段
		String order = RequestHelper.getParam(request, "order");// 排序Order
		mapCondition.put("TJLX", request.getParameter("TJLX"));
		mapCondition.put("BLHJ", request.getParameter("BLHJ"));
		mapCondition.put("BSHOWDYH", request.getParameter("BSHOWDYH"));
		mapCondition.put("DJSJ_Q", request.getParameter("DJSJ_Q"));                                                                                                                                                           
		mapCondition.put("DJSJ_Z", request.getParameter("DJSJ_Z"));
		mapCondition.put("DJSJ", request.getParameter("DJSJ"));                                                                                                                                                           
		mapCondition.put("SLSJ", request.getParameter("SLSJ"));
		//liangc 增加登记类型和坐落查询条件
		mapCondition.put("DJLX", request.getParameter("DJLX"));
		mapCondition.put("QLLX", request.getParameter("QLLX"));
		if(StringHelper.isEmpty(request.getParameter("ZL"))){
			mapCondition.put("ZL", "");
		}else{
			mapCondition.put("ZL", request.getParameter("ZL").trim());
		}
		mapCondition.put("TDYT", request.getParameter("TDYT").trim());
		mapCondition.put("FWYT", request.getParameter("FWYT").trim());
		mapCondition.put("SFFZ", request.getParameter("SFFZ"));
		//chenbo 添加权利性质和地籍区
		mapCondition.put("QLXZ", request.getParameter("QLXZ").trim());
		mapCondition.put("DJQDM", request.getParameter("DJQDM").trim());
		mapCondition.put("sort", sort);
		mapCondition.put("order", order);
		mapCondition.put("ZSZM", request.getParameter("ZSZM"));
		mapCondition.put("SFSZ", request.getParameter("SFSZ"));
		mapCondition.put("SQRLX", request.getParameter("SQRLX"));
		mapCondition.put("BJSJ_Q", request.getParameter("BJSJ_Q"));
		mapCondition.put("BJSJ_Z", request.getParameter("BJSJ_Z"));
		Message m = tjcxfxService.GetStandingBook(mapCondition, page, rows);
		YwLogUtil.addYwLog("获取不动产登记台账", ConstValue.SF.YES.Value,ConstValue.LOG.SEARCH);
		return m;
	}

	/**
	 * 不动产登记台账(云南玉溪)
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
     */
	@RequestMapping(value = "/standingbookyuxi", method = RequestMethod.GET)
	public @ResponseBody Message GetStandingBookYuxi(HttpServletRequest request,
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
		mapCondition.put("DJSJ_Q", request.getParameter("DJSJ_Q"));
		mapCondition.put("DJSJ_Z", request.getParameter("DJSJ_Z"));

		Message m = tjcxfxService.GetStandingBookYuxi(mapCondition, page, rows);
		YwLogUtil.addYwLog("获取不动产业务台账(云南玉溪)", ConstValue.SF.YES.Value,ConstValue.LOG.SEARCH);
		return m;
	}
	
	/** 
	 * 不动产登记台账
	 * @作者 俞学斌
	 * @创建时间 2016年4月11日18:21:40
	 * @param request
	 * @param response
	 * @return 
	 * @throws Exception
	 */
	@RequestMapping(value = "/standingbook/hz/", method = RequestMethod.GET)   //standingbook的面积合计
	public @ResponseBody HashMap<String,String> GetStandingBook_HZ(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("utf-8");
		String zl =request.getParameter("ZL");
		String fwyt = new String(request.getParameter("FWYT").getBytes("iso8859-1"),"UTF-8");
		Map<String, String> mapCondition = new HashMap<String, String>(); 
		mapCondition.put("BLHJ", request.getParameter("BLHJ"));
		mapCondition.put("BSHOWDYH", request.getParameter("BSHOWDYH"));
		mapCondition.put("TJLX", request.getParameter("TJLX"));    //获取serviceimpl数据
		mapCondition.put("DJSJ_Q", request.getParameter("DJSJ_Q"));
		mapCondition.put("DJSJ_Z", request.getParameter("DJSJ_Z"));
		mapCondition.put("HJZMJ", request.getParameter("HJZMJ"));
		mapCondition.put("YHJZMJ", request.getParameter("YHJZMJ"));
		mapCondition.put("SHYQMJ", request.getParameter("SHYQMJ"));
		mapCondition.put("TDYT",request.getParameter("TDYT"));
		mapCondition.put("FWYT",fwyt);
		mapCondition.put("ZL",zl);
		mapCondition.put("QLXZ",request.getParameter("QLXZ"));
		mapCondition.put("DJQDM",request.getParameter("DJQDM"));
		mapCondition.put("DJLX",request.getParameter("DJLX"));
		mapCondition.put("SFFZ",request.getParameter("SFFZ"));
		mapCondition.put("QLLX",request.getParameter("QLLX"));
		HashMap<String,String> m = tjcxfxService.GetStandingBook_HZ(mapCondition);
		return m;
	}
	
	/** 
	 * 导出不动产登记台账
	 * @作者 俞学斌
	 * @创建时间 2016年4月11日18:21:40
	 * @param request
	 * @param response
	 * @return 
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/standingbookdownload", method = RequestMethod.GET)
	public @ResponseBody String StandingBookDownload(HttpServletRequest request, HttpServletResponse response) throws Exception{
		request.setCharacterEncoding("utf-8");
		Map<String, String> mapCondition = new HashMap<String, String>();
		String sort = RequestHelper.getParam(request, "sort");// 排序字段
		String order = RequestHelper.getParam(request, "order");// 排序Order
		mapCondition.put("TJLX", RequestHelper.getParam(request, "TJLX"));
		mapCondition.put("BLHJ", RequestHelper.getParam(request, "BLHJ"));
		mapCondition.put("BSHOWDYH", RequestHelper.getParam(request, "BSHOWDYH"));
		mapCondition.put("DJSJ_Q", RequestHelper.getParam(request, "DJSJ_Q"));
		mapCondition.put("DJSJ_Z", RequestHelper.getParam(request, "DJSJ_Z"));
		//liangc 增加登记类型和坐落查询条件
		mapCondition.put("DJLX", RequestHelper.getParam(request,"DJLX"));
		mapCondition.put("QLLX", RequestHelper.getParam(request,"QLLX"));
		if(StringHelper.isEmpty(RequestHelper.getParam(request,"ZL"))){
			mapCondition.put("ZL", "");
		}else{
			mapCondition.put("ZL", RequestHelper.getParam(request,"ZL").trim());
		}
		mapCondition.put("TDYT", StringHelper.formatObject(RequestHelper.getParam(request,"TDYT")).trim());
		mapCondition.put("FWYT", StringHelper.formatObject(RequestHelper.getParam(request,"FWYT")).trim());
		mapCondition.put("SFFZ", RequestHelper.getParam(request,"SFFZ"));
		//chenbo 添加权利性质和地籍区
		mapCondition.put("QLXZ", StringHelper.formatObject(RequestHelper.getParam(request,"QLXZ")).trim());
		mapCondition.put("DJQDM", StringHelper.formatObject(RequestHelper.getParam(request,"DJQDM")).trim());
		mapCondition.put("sort", sort);
		mapCondition.put("order", order);
		mapCondition.put("SFSZ", RequestHelper.getParam(request,"SFSZ"));//是否缮证
		mapCondition.put("BJSJ_Q", StringHelper.formatObject(RequestHelper.getParam(request,"BJSJ_Q")).trim());
		mapCondition.put("BJSJ_Z", StringHelper.formatObject(RequestHelper.getParam(request,"BJSJ_Z")).trim());
		mapCondition.put("SQRLX", RequestHelper.getParam(request,"SQRLX"));
		List<Map> djdys = tjcxfxService.GetStandingBook(mapCondition);//获取登记台账数据
		String basePath = request.getRealPath("/") + "\\resources\\PDF";
		String outpath = "";
		String url = "";
		String tmpFullName = "";
		FileOutputStream outstream = null;
		if(djdys != null && djdys.size() > 0){
			outpath = basePath + "\\tmp\\standingbook.xls";
			url = request.getContextPath() + "\\resources\\PDF\\tmp\\standingbook.xls";
		    outstream = new FileOutputStream(outpath); 
		    tmpFullName = request.getRealPath("/WEB-INF/jsp/wjmb/standingbook.xls");
		    InputStream input = new FileInputStream(tmpFullName);
			HSSFWorkbook  wb = null;// 定义一个新的工作簿
			wb = new HSSFWorkbook(input);
			Sheet sheet = wb.getSheetAt(0);
			Map<String,Integer> MapCol = new HashMap<String,Integer>();
			//liangc 添加列“宗地面积”、“受理时间”、“转移前权利人”、“转移前权利人证件号、“转移前不动产权证号”、“抵押人”、房屋结构、共有方式  liangq取得价格，权利人电话，转移前权利人电话
			MapCol.put("序号", 0);
			MapCol.put("房产业务号", 1);
			MapCol.put("业务号", 2);
			MapCol.put("受理人", 3);
			MapCol.put("受理时间", 4);
			MapCol.put("业务类型", 5);
			MapCol.put("登记类型",6);
			MapCol.put("登记时间", 7);
			MapCol.put("注销时间", 8);
			MapCol.put("办结时间", 9);
			MapCol.put("不动产坐落",10);
			MapCol.put("不动产单元号", 11);
			MapCol.put("不动产单元类型", 12);
			MapCol.put("转移前权利人", 13);
			MapCol.put("转移前权利人联系电话", 14);
			MapCol.put("转移前权利人证件号", 15);
			MapCol.put("转移前不动产权证号", 16);
			MapCol.put("原权证号", 17);
			MapCol.put("附记", 18);
			MapCol.put("申请人类型", 19);
			MapCol.put("权利人", 20);
			MapCol.put("权利人联系电话", 21);
			MapCol.put("权利人证件号", 22);
			MapCol.put("权利人类型", 23);
			MapCol.put("权利代理人", 24);
			MapCol.put("权利代理人证件号", 25);
			MapCol.put("权利代理人联系电话", 26);
			MapCol.put("抵押人", 27);	
			MapCol.put("抵押金额", 28);	
			MapCol.put("义务人", 29);
			MapCol.put("义务人证件号", 30);
			MapCol.put("义务人联系电话", 31);
			MapCol.put("义务人代理人", 32);
			MapCol.put("义务人代理人证件号", 33);
			MapCol.put("义务人代理人联系电话", 34);
			MapCol.put("取得价格", 35);
			MapCol.put("不动产权证号（证明号）", 36);
			MapCol.put("权利类型", 37);
			MapCol.put("土地用途", 38);
			MapCol.put("权利性质", 39);
			MapCol.put("房屋用途", 40);
			MapCol.put("房屋性质", 41);
			MapCol.put("房屋结构", 42);
			MapCol.put("共有方式", 43);	
			MapCol.put("分摊（独用）使用权面积", 44);
			MapCol.put("房屋建筑面积", 45);
			MapCol.put("分摊建筑面积", 46);
			MapCol.put("专有建筑面积", 47);
			MapCol.put("宗地面积", 48);
			MapCol.put("登记原因", 49);
			MapCol.put("是否发证", 50);
			MapCol.put("证书编号", 51);
			MapCol.put("所在层", 52);
			MapCol.put("总层数", 53);
			
            int rownum = 1;
			for(Map djdy:djdys){
				 
		  		 HSSFRow row = (HSSFRow)sheet.createRow(rownum);
		  		 try{
			         HSSFCell Cell0 = row.createCell(MapCol.get("序号"));
			         Cell0.setCellValue(StringHelper.formatObject(djdy.get("XH")));
			         HSSFCell Cell1 = row.createCell(MapCol.get("房产业务号"));
			         Cell1.setCellValue(StringHelper.formatObject(djdy.get("FCYWH")));
			         HSSFCell Cell2 = row.createCell(MapCol.get("业务号"));
			         Cell2.setCellValue(StringHelper.formatObject(djdy.get("YWLSH")));
			         HSSFCell Cell3 = row.createCell(MapCol.get("受理人"));
			         Cell3.setCellValue(StringHelper.formatObject(djdy.get("SLRY")));
					 HSSFCell Cell4 = row.createCell(MapCol.get("受理时间"));
			         Cell4.setCellValue(StringHelper.formatObject(djdy.get("SLSJ")));
			         HSSFCell Cell5 = row.createCell(MapCol.get("业务类型"));
			         Cell5.setCellValue(StringHelper.formatObject(djdy.get("YWLX")));
			         HSSFCell Cell6 = row.createCell(MapCol.get("登记类型"));
			         Cell6.setCellValue(StringHelper.formatObject(djdy.get("DJLX")));
			         HSSFCell Cell7 = row.createCell(MapCol.get("登记时间"));
			         Cell7.setCellValue(StringHelper.formatObject(djdy.get("DJSJ")));
			         HSSFCell Cell8 = row.createCell(MapCol.get("注销时间"));
			         Cell8.setCellValue(StringHelper.formatObject(djdy.get("ZXSJ")));
			         HSSFCell Cell9 = row.createCell(MapCol.get("办结时间"));
			         Cell9.setCellValue(StringHelper.formatObject(djdy.get("BJSJ")));
			         HSSFCell Cell10 = row.createCell(MapCol.get("不动产坐落"));
			         Cell10.setCellValue(StringHelper.formatObject(djdy.get("ZL")));
			         HSSFCell Cell11 = row.createCell(MapCol.get("不动产单元号"));
			         Cell11.setCellValue(StringHelper.formatObject(djdy.get("BDCDYH")));
					 HSSFCell Cell12 = row.createCell(MapCol.get("不动产单元类型"));
			         Cell12.setCellValue(StringHelper.formatObject(djdy.get("BDCDYLX")));
					 HSSFCell Cell13 = row.createCell(MapCol.get("转移前权利人"));
			         Cell13.setCellValue(StringHelper.formatObject(djdy.get("ZYQQLR")));
			         HSSFCell Cell14 = row.createCell(MapCol.get("转移前权利人联系电话"));
			         Cell14.setCellValue(StringHelper.formatObject(djdy.get("ZYQQLRLXDH")));
			         HSSFCell Cell15 = row.createCell(MapCol.get("转移前权利人证件号"));
					 Cell15.setCellValue(StringHelper.formatObject(djdy.get("ZYQQLRZJH")));
					 HSSFCell Cell16 = row.createCell(MapCol.get("转移前不动产权证号"));
			         Cell16.setCellValue(StringHelper.formatObject(djdy.get("ZYQBDCQZH")));
			         HSSFCell Cell17 = row.createCell(MapCol.get("原权证号"));
			         Cell17.setCellValue(StringHelper.formatObject(djdy.get("YQZH")));
			         HSSFCell Cell18 = row.createCell(MapCol.get("附记"));
			         Cell18.setCellValue(StringHelper.formatObject(djdy.get("FJ")));
			         HSSFCell Cell19 = row.createCell(MapCol.get("申请人类型"));
			         Cell19.setCellValue(StringHelper.formatObject(djdy.get("SQRLX")));
			         HSSFCell Cell20 = row.createCell(MapCol.get("权利人"));
			         Cell20.setCellValue(StringHelper.formatObject(djdy.get("QLRMC")));
			         HSSFCell Cell21 = row.createCell(MapCol.get("权利人联系电话"));
			         Cell21.setCellValue(StringHelper.formatObject(djdy.get("LXDH")));
			         HSSFCell Cell22 = row.createCell(MapCol.get("权利人证件号"));
			         Cell22.setCellValue(StringHelper.formatObject(djdy.get("ZJH")));
					 HSSFCell Cell23 = row.createCell(MapCol.get("权利人类型"));
					 Cell23.setCellValue(StringHelper.formatObject(djdy.get("QLRLX")));
					 HSSFCell Cell24 = row.createCell(MapCol.get("权利代理人"));
					 Cell24.setCellValue(StringHelper.formatObject(djdy.get("QLDLR")));
			         HSSFCell Cell25 = row.createCell(MapCol.get("权利代理人证件号"));
			         Cell25.setCellValue(StringHelper.formatObject(djdy.get("QLDLRZJH")));
			         HSSFCell Cell26 = row.createCell(MapCol.get("权利代理人联系电话"));
			         Cell26.setCellValue(StringHelper.formatObject(djdy.get("QLDLRLXDH")));
			         HSSFCell Cell27 = row.createCell(MapCol.get("抵押人"));
			         Cell27.setCellValue(StringHelper.formatObject(djdy.get("DYR")));
			         HSSFCell Cell28 = row.createCell(MapCol.get("抵押金额"));
			         Cell28.setCellValue(StringHelper.formatObject(djdy.get("DYJE")));
			         HSSFCell Cell29 = row.createCell(MapCol.get("义务人"));
			         Cell29.setCellValue(StringHelper.formatObject(djdy.get("YWR")));
			         HSSFCell Cell30 = row.createCell(MapCol.get("义务人证件号"));
			         Cell30.setCellValue(StringHelper.formatObject(djdy.get("YWRZJH")));
			         HSSFCell Cell31 = row.createCell(MapCol.get("义务人联系电话"));
			         Cell31.setCellValue(StringHelper.formatObject(djdy.get("YWRDH")));
			         HSSFCell Cell32 = row.createCell(MapCol.get("义务人代理人"));
			         Cell32.setCellValue(StringHelper.formatObject(djdy.get("YWDLRXM")));
			         HSSFCell Cell33 = row.createCell(MapCol.get("义务人代理人证件号"));
			         Cell33.setCellValue(StringHelper.formatObject(djdy.get("YWDLRZJH")));
			         HSSFCell Cell34 = row.createCell(MapCol.get("义务人代理人联系电话"));
			         Cell34.setCellValue(StringHelper.formatObject(djdy.get("YWDLRLXDH")));
			         HSSFCell Cell35 = row.createCell(MapCol.get("取得价格"));
			         Cell35.setCellValue(StringHelper.formatObject(djdy.get("QDJG")));
			         HSSFCell Cell36 = row.createCell(MapCol.get("不动产权证号（证明号）"));
			         Cell36.setCellValue(StringHelper.formatObject(djdy.get("BDCQZH")));
			         HSSFCell Cell37 = row.createCell(MapCol.get("权利类型"));
			         Cell37.setCellValue(StringHelper.formatObject(djdy.get("QLLX")));
			         HSSFCell Cell38 = row.createCell(MapCol.get("土地用途"));
			         Cell38.setCellValue(StringHelper.formatObject(djdy.get("TDYT")));
			         HSSFCell Cell39 = row.createCell(MapCol.get("权利性质"));
			         Cell39.setCellValue(StringHelper.formatObject(djdy.get("QLXZ")));
			         HSSFCell Cell40 = row.createCell(MapCol.get("房屋用途"));				 
			         Cell40.setCellValue(StringHelper.formatObject(djdy.get("FWYT")));
					 HSSFCell Cell41 = row.createCell(MapCol.get("房屋性质"));
					 Cell41.setCellValue(StringHelper.formatObject(djdy.get("FWXZ")));
			         HSSFCell Cell42 = row.createCell(MapCol.get("房屋结构"));
			         Cell42.setCellValue(StringHelper.formatObject(djdy.get("FWJG")));
			         HSSFCell Cell43 = row.createCell(MapCol.get("共有方式"));
			         Cell43.setCellValue(StringHelper.formatObject(djdy.get("GYFS")));
			         HSSFCell Cell44 = row.createCell(MapCol.get("分摊（独用）使用权面积"));
			         Cell44.setCellValue(StringHelper.formatObject(djdy.get("SYQMJ")));
			         HSSFCell Cell45 = row.createCell(MapCol.get("房屋建筑面积"));
			         Cell45.setCellValue(StringHelper.formatObject(djdy.get("JZMJ")));
			         HSSFCell Cell46 = row.createCell(MapCol.get("分摊建筑面积"));
			         Cell46.setCellValue(StringHelper.formatObject(djdy.get("FTJZMJ")));
					 HSSFCell Cell47 = row.createCell(MapCol.get("专有建筑面积"));
					 Cell47.setCellValue(StringHelper.formatObject(djdy.get("ZYJZMJ")));
			         HSSFCell Cell48 = row.createCell(MapCol.get("宗地面积"));
			         Cell48.setCellValue(StringHelper.formatObject(djdy.get("ZDMJ")));
			         HSSFCell Cell49 = row.createCell(MapCol.get("登记原因"));
			         Cell49.setCellValue(StringHelper.formatObject(djdy.get("DJYY")));
			         HSSFCell Cell50 = row.createCell(MapCol.get("是否发证"));
			         Cell50.setCellValue(StringHelper.formatObject(djdy.get("SFFZ")));
			         HSSFCell Cell51 = row.createCell(MapCol.get("证书编号"));
			         Cell51.setCellValue(StringHelper.formatObject(djdy.get("ZSBH")));
			         HSSFCell Cell52 = row.createCell(MapCol.get("所在层"));
			         Cell52.setCellValue(StringHelper.formatObject(djdy.get("SZC")));
			         HSSFCell Cell53 = row.createCell(MapCol.get("总层数"));
			         Cell53.setCellValue(StringHelper.FormatByDatatype(djdy.get("ZCS")));
			         rownum ++;
			         
//			         System.out.println("===================================================================");
//			         System.out.println(rownum);
//			         System.out.println("===================================================================");
		  		 }
		  		 catch(Exception ex){
		  			 System.out.println("导出数据有误");
		  			ex.printStackTrace();
		  		 }
			  }	  		
			 wb.write(outstream); 
			 outstream.flush(); 
			 outstream.close();
		 }

        return url;
	}

	/**
	 * 导出不动产登记台账(云南玉溪)
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
     */
	@SuppressWarnings({ "rawtypes" })
	@RequestMapping(value = "/standingbookdownloadyuxi", method = RequestMethod.GET)
	public @ResponseBody String StandingBookDownloadYuxi(HttpServletRequest request, HttpServletResponse response) throws Exception{
		String djsj_q=request.getParameter("DJSJ_Q");
		String djsj_z=request.getParameter("DJSJ_Z");
		Map<String, String> mapCondition = new HashMap<String, String>();
		mapCondition.put("DJSJ_Q", djsj_q);
		mapCondition.put("DJSJ_Z", djsj_z);

		List<Map> djdys = tjcxfxService.GetStandingBookYuxi(mapCondition);

		String basePath = request.getRealPath("/") + "\\resources\\PDF";
		String outpath = "";
		String url = "";
		String tmpFullName = "";
		FileOutputStream outstream = null;
		if(djdys != null && djdys.size() > 0){
			outpath = basePath + "\\tmp\\standingbookyuxi.xls";
			url = request.getContextPath() + "\\resources\\PDF\\tmp\\standingbookyuxi.xls";
			outstream = new FileOutputStream(outpath);
			tmpFullName = request.getRealPath("/WEB-INF/jsp/wjmb/standingbookyuxi.xls");
			InputStream input = new FileInputStream(tmpFullName);
			HSSFWorkbook  wb = null;// 定义一个新的工作簿
			wb = new HSSFWorkbook(input);
			Sheet sheet = wb.getSheetAt(0);
			Map<String,Integer> MapCol = new HashMap<String,Integer>();
			MapCol.put("序号", 0);
			MapCol.put("登记类型", 1);
			MapCol.put("登记时间", 2);
			MapCol.put("不动产坐落", 3);
			MapCol.put("不动产单元号", 4);
			MapCol.put("权利人", 5);
			MapCol.put("不动产权证号（证明号）", 6);
			MapCol.put("义务人", 7);
			int rownum = 1;
			for(Map djdy:djdys){
				HSSFRow row = (HSSFRow)sheet.createRow(rownum);
				try{
					HSSFCell Cell0 = row.createCell(MapCol.get("序号"));
					Cell0.setCellValue(StringHelper.formatObject(djdy.get("XH")));
					HSSFCell Cell1 = row.createCell(MapCol.get("登记类型"));
					Cell1.setCellValue(StringHelper.formatObject(djdy.get("DJLX")));
					HSSFCell Cell2 = row.createCell(MapCol.get("登记时间"));
					Cell2.setCellValue(StringHelper.formatObject(djdy.get("DJSJ")));
					HSSFCell Cell3 = row.createCell(MapCol.get("不动产坐落"));
					Cell3.setCellValue(StringHelper.formatObject(djdy.get("ZL")));
					HSSFCell Cell4 = row.createCell(MapCol.get("不动产单元号"));
					Cell4.setCellValue(StringHelper.formatObject(djdy.get("BDCDYH")));
					HSSFCell Cell5 = row.createCell(MapCol.get("权利人"));
					Cell5.setCellValue(StringHelper.formatObject(djdy.get("QLRMC")));
					HSSFCell Cell6 = row.createCell(MapCol.get("不动产权证号（证明号）"));
					Cell6.setCellValue(StringHelper.formatObject(djdy.get("BDCQZH")));
					HSSFCell Cell7 = row.createCell(MapCol.get("义务人"));
					Cell7.setCellValue(StringHelper.formatObject(djdy.get("YWR")));
					rownum ++;
				}
				catch(Exception ex){
					System.out.println("导出数据有误");
		  			ex.printStackTrace();
				}
			}
			wb.write(outstream);
			outstream.flush();
			outstream.close();
		}

		return url;
	}
	
	/** 
	 * 获取自定义查询统计页面
	 * @作者 俞学斌
	 * @创建时间 2016年5月8日 16:26:40
	 * @param request
	 * @param response
	 * @return 
	 * @throws Exception
	 */
	@RequestMapping(value = "/userdefinebook/index/{id}/", method = RequestMethod.GET)
	public String showQueryPage(@PathVariable("id") String id, Model model) {
		HashMap<String, Object> config = tjcxfxService.GetUserDefineBookConfig(id);
		String strjson = JSONObject.toJSONString(config);
		System.out.println(strjson);
		model.addAttribute("config", strjson);
		return "/realestate/registration/djywcx/userdefinebook";
	}
	
	/** 
	 * 自定义查询统计获取数据服务
	 * @作者 俞学斌
	 * @创建时间 2016年5月8日 16:26:40
	 * @param request
	 * @param response
	 * @return 
	 * @throws Exception
	 */
	@RequestMapping(value = "/userdefinebook/{id}/", method = RequestMethod.GET)
	public @ResponseBody Message GetUserDefineBook(@PathVariable("id") String id,HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Message m = tjcxfxService.GetUserDefineBook(id,request);
		return m;
	}
	
	/** 
	 * 导出自定义查询统计结果
	 * @作者 俞学斌
	 * @创建时间 2016年5月8日 16:26:40
	 * @param request
	 * @param response
	 * @return 
	 * @throws Exception
	 */
	@SuppressWarnings({"rawtypes", "unchecked" })
	@RequestMapping(value = "/userdefinebookdownload/{id}/", method = RequestMethod.GET)
	public @ResponseBody String UserDefineBookDownload(@PathVariable("id") String id,HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		
		List<Map> list = tjcxfxService.UserDefineBookALL(id,request);
		HashMap<String, Object> config = tjcxfxService.GetUserDefineBookConfig(id);
		
		String tplFullName = request.getSession().getServletContext().getRealPath("/WEB-INF/jsp/wjmb/"
				+ id
				+ ".xls");
		
		String basePath = request.getRealPath("/") + "\\resources\\PDF";
		String outpath = "";
		String url = "";
		String title=StringHelper.formatObject(config.get("title"));
		
		if(new File(tplFullName).exists()){
			List<T_GRIDCONFIG_USERDEFINEBOOK> list_gridconfig=(List<T_GRIDCONFIG_USERDEFINEBOOK>)config.get("resultfields");
			outpath = basePath + "\\tmp\\"+id+".xls";
			url = request.getContextPath() + "\\resources\\PDF\\tmp\\"+id+".xls";
			FileOutputStream outstream = new FileOutputStream(outpath);
			InputStream input = new FileInputStream(tplFullName);
			HSSFWorkbook wb = null;// 定义一个新的工作簿
			wb = new HSSFWorkbook(input);
			HSSFSheet sheet = wb.getSheetAt(0);
			int lastrow = sheet.getLastRowNum();
			HSSFRow head = sheet.getRow(lastrow - 1);
			HSSFCellStyle style = sheet.getRow(lastrow).getCell(0).getCellStyle();
			//short height = sheet.getRow(lastrow).getHeight();
			List<String> sortlist = new ArrayList<String>();
			for (Cell cell : head) {
				for (T_GRIDCONFIG_USERDEFINEBOOK gridconfig : list_gridconfig) {
					if(gridconfig.getCOLUMNTEXT().equals(cell.getStringCellValue()))
						sortlist.add(gridconfig.getFIELDNAME());
				}
			}
			for (Map map:list) {
				HSSFRow row = sheet.createRow(lastrow++);
				//row.setHeight(height);
				int column = 0;
				for (String sort : sortlist) {
					HSSFCell Cell = row.createCell(column++);
					Cell.setCellValue(StringHelper.formatObject(map.get(sort)));
					Cell.setCellStyle(style);
				}
				while(head.getLastCellNum()>row.getLastCellNum()){
					HSSFCell Cell = row.createCell(column++);
					Cell.setCellValue(StringHelper.formatObject(""));
					Cell.setCellStyle(style);
				}
			}
			wb.write(outstream);
			outstream.flush();
			outstream.close();
			outstream = null;
		}else{
			if(list != null && list.size() > 0){
				outpath = basePath + "\\tmp\\"+id+".xls";
				url = request.getContextPath() + "\\resources\\PDF\\tmp\\"+id+".xls";
				HSSFWorkbook  wb = new HSSFWorkbook();//// 定义一个新的工作簿
				Sheet sheet = wb.createSheet(title);
				List<T_GRIDCONFIG_USERDEFINEBOOK> list_gridconfig=(List<T_GRIDCONFIG_USERDEFINEBOOK>)config.get("resultfields");
				int i=0;
				HSSFRow row_title = (HSSFRow)sheet.createRow(0);
				for(T_GRIDCONFIG_USERDEFINEBOOK gridconfig:list_gridconfig){
					HSSFCell Cell0 = row_title.createCell(i);
					Cell0.setCellValue(gridconfig.getCOLUMNTEXT());
					i++;
				}
	            int rownum = 1;
				for(Map map:list){
					HSSFRow row = (HSSFRow)sheet.createRow(rownum);
					try{
						int iii=0;
						for(T_GRIDCONFIG_USERDEFINEBOOK gridconfig:list_gridconfig){
							HSSFCell Cell0 = row.createCell(iii);
							Cell0.setCellValue(StringHelper.formatObject(map.get(gridconfig.getFIELDNAME())));
							iii++;
						}
					}catch(Exception ex){
						System.out.println("导出数据有误");
			  			ex.printStackTrace();
					}	
					rownum++;
				}	  		
				ByteArrayOutputStream os = new ByteArrayOutputStream();  
		        try{  
		            wb.write(os);  
		        }catch(IOException e){  
		            e.printStackTrace();  
		        }  
		          
		        byte[] xls = os.toByteArray();  
		          
		        File file = new File(outpath);  
		        OutputStream out = null;  
		        try {  
		             out = new FileOutputStream(file);  
		             try {  
		                out.write(xls);  
		            } catch (IOException e) {  
		                e.printStackTrace();  
		            }  
		        } catch (FileNotFoundException e1) {  
		            e1.printStackTrace();  
		        }
		        out.flush(); 
		        out.close();
		 	}
		}
		
        return url;
	}

	/** 
	 * 自定义查询统计定义管理页面
	 * @作者 俞学斌
	 * @创建时间 2016年5月8日 16:26:40
	 * @param request
	 * @param response
	 * @return 
	 * @throws Exception
	 */
	@RequestMapping(value = "/bookdefine/index/", method = RequestMethod.GET)
	public String BookDefine() {
		return "/realestate/registration/djywcx/bookdefine";
	}
	
	
	/** 
	 * 自定义查询统计定义列表
	 * @作者 俞学斌
	 * @创建时间 2016年5月8日 16:26:40
	 * @param request
	 * @param response
	 * @return 
	 * @throws Exception
	 */
	@RequestMapping(value = "/bookdefine/", method = RequestMethod.GET)
	public @ResponseBody Message GetBookDefineList(HttpServletRequest request, HttpServletResponse response) {
		Message m = tjcxfxService.GetBookDefineList(request);
		return m;
	}
	
	/** 
	 * 添加或更新自定义查询统计定义
	 * @作者 俞学斌
	 * @创建时间 2016年5月8日 16:26:40
	 * @param request
	 * @param response
	 * @return 
	 * @throws Exception
	 */
	@RequestMapping(value = "/bookdefine/", method = RequestMethod.POST)
	public @ResponseBody ResultMessage AddOrUpdateBookDefine(HttpServletRequest request, HttpServletResponse response) {
		ResultMessage m = tjcxfxService.AddOrUpdateBookDefine(request);
		return m;
	}
	
	/** 
	 * 删除自定义查询统计定义
	 * @作者 俞学斌
	 * @创建时间 2016年5月8日 16:26:40
	 * @param request
	 * @param response
	 * @return 
	 * @throws Exception
	 */
	@RequestMapping(value = "/bookdefine/{id}", method = RequestMethod.DELETE)
	public @ResponseBody ResultMessage RemoveBookDefine(@PathVariable("id") String id,HttpServletRequest request, HttpServletResponse response) {
		ResultMessage m = tjcxfxService.RemoveBookDefine(id);
		return m;
	}
	
	/** 
	 * 自定义查询统计查询条件定义页面
	 * @作者 俞学斌
	 * @创建时间 2016年5月8日 16:26:40
	 * @param request
	 * @param response
	 * @return 
	 * @throws Exception
	 */
	@RequestMapping(value = "/bookmanager_query/index/")
	public String getBookQueryManagerIndex() {
		return "/realestate/registration/djywcx/bookmanager_query";
	}
	
	/** 
	 * 自定义查询统计结果信息定义页面
	 * @作者 俞学斌
	 * @创建时间 2016年5月8日 16:26:40
	 * @param request
	 * @param response
	 * @return 
	 * @throws Exception
	 */
	@RequestMapping(value = "/bookmanager_result/index/")
	public String getBookResultManagerIndex() {
		return "/realestate/registration/djywcx/bookmanager_result";
	}
	
	/** 
	 * 自定义查询统计查询条件列表
	 * @作者 俞学斌
	 * @创建时间 2016年5月8日 16:26:40
	 * @param request
	 * @param response
	 * @return 
	 * @throws Exception
	 */
	@RequestMapping(value = "/querymanager/", method = RequestMethod.GET)
	public @ResponseBody Message GetQueryManager(HttpServletRequest request, HttpServletResponse response) {
		String bookid="";
		try {
			bookid = RequestHelper.getParam(request, "bookid");
		} catch (Exception e) {
		}
		Message m=tjcxfxService.GetQueryManager(bookid);
		return m;
	}
	
	/** 
	 * 添加或更新自定义查询统计查询条件
	 * @作者 俞学斌
	 * @创建时间 2016年5月8日 16:26:40
	 * @param request
	 * @param response
	 * @return 
	 * @throws Exception
	 */
	@RequestMapping(value = "/querymanager/{bookid}/", method = RequestMethod.POST)
	public @ResponseBody ResultMessage AddOrUpdateQueryManager(@PathVariable("bookid") String bookid,HttpServletRequest request, HttpServletResponse response) {
		ResultMessage m=tjcxfxService.AddOrUpdateQueryManager(bookid,request);
		return m;
	}
	
	/** 
	 * 删除自定义查询统计查询条件
	 * @作者 俞学斌
	 * @创建时间 2016年5月8日 16:26:40
	 * @param request
	 * @param response
	 * @return 
	 * @throws Exception
	 */
	@RequestMapping(value = "/querymanager/{id}", method = RequestMethod.DELETE)
	public @ResponseBody ResultMessage RemoveQueryManager(@PathVariable("id") String id,HttpServletRequest request, HttpServletResponse response) {
		ResultMessage m=tjcxfxService.RemoveQueryManager(id);
		return m;
	}
	
	
	
	/** 
	 * 自定义查询统计结果字段列表
	 * @作者 俞学斌
	 * @创建时间 2016年5月8日 16:26:40
	 * @param request
	 * @param response
	 * @return 
	 * @throws Exception
	 */
	@RequestMapping(value = "/resultmanager/", method = RequestMethod.GET)
	public @ResponseBody Message GetResultManager(HttpServletRequest request, HttpServletResponse response) {
		String bookid="";
		try {
			bookid = RequestHelper.getParam(request, "bookid");
		} catch (Exception e) {
		}
		Message m=tjcxfxService.GetResultManager(bookid);
		return m;
	}
	
	/** 
	 * 添加或更新自定义查询统计结果字段
	 * @作者 俞学斌
	 * @创建时间 2016年5月8日 16:26:40
	 * @param request
	 * @param response
	 * @return 
	 * @throws Exception
	 */
	@RequestMapping(value = "/resultmanager/{bookid}/", method = RequestMethod.POST)
	public @ResponseBody ResultMessage AddOrUpdateResultManager(@PathVariable("bookid") String bookid,HttpServletRequest request, HttpServletResponse response) {
		ResultMessage m=tjcxfxService.AddOrUpdateResultManager(bookid,request);
		return m;
	}
	
	/** 
	 * 删除自定义查询统计结果字段
	 * @作者 俞学斌
	 * @创建时间 2016年5月8日 16:26:40
	 * @param request
	 * @param response
	 * @return 
	 * @throws Exception
	 */
	@RequestMapping(value = "/resultmanager/{id}", method = RequestMethod.DELETE)
	public @ResponseBody ResultMessage RemoveResultManager(@PathVariable("id") String id,HttpServletRequest request, HttpServletResponse response) {
		ResultMessage m=tjcxfxService.RemoveResultManager(id);
		return m;
	}
	
	/**
     * 收费统计（URL:"/sftj",Method:GET）
     * 
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/sftj/", method = RequestMethod.GET)
	public @ResponseBody Message getSFTJ(HttpServletRequest request,HttpServletResponse response) throws Exception {
    	String qsj=request.getParameter("qsj");
    	String zsj=request.getParameter("zsj");
    	String sfry=RequestHelper.getParam(request, "sfry");
    	String sfbmmc=RequestHelper.getParam(request, "sfbmmc");
		Message msg = tjcxfxService.getSFTJ(qsj, zsj,sfry,sfbmmc);
		return msg;
	}
    
    /**
     * 收费明细统计（URL:"/sfmxtj",Method:GET）
     * 
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/sfmxtj/", method = RequestMethod.GET)
	public @ResponseBody Message getSFMXTJ(HttpServletRequest request,HttpServletResponse response) throws Exception {
    	String qsj=request.getParameter("qsj");
    	String sfdl=request.getParameter("sfdl");
    	if(sfdl.equals("0")){
    		sfdl="工本费";
    	}else if(sfdl.equals("1")){
    		sfdl="住房";
    	}else if(sfdl.equals("2")){
    		sfdl="非住房";
    	}else if(sfdl.equals("3")){
    		sfdl=null;//默认的“请选择”
    	}
    	String zsj=request.getParameter("zsj");
    	String slry=RequestHelper.getParam(request, "slry");
    	String sfry=RequestHelper.getParam(request, "sfry");
    	String sfbmmc=RequestHelper.getParam(request, "sfbmmc");
    	String sflx=RequestHelper.getParam(request, "sflx");
		Message msg = tjcxfxService.getSFMXTJ(qsj, zsj,slry,sfbmmc,sflx,sfry,sfdl);
		return msg;
	}
    
    /**
     * 收费人员列表（URL:"/sfrylist",Method:GET）
     * 
     * @return
     * @throws Exception
     */
    @SuppressWarnings("rawtypes")
	@RequestMapping(value = "/sfrylist/", method = RequestMethod.GET)
	public @ResponseBody List<Map> getSFRYList(HttpServletRequest request,HttpServletResponse response) throws Exception {
    	List<Map> list = tjcxfxService.getSFRYList();
		return list;
	}
    
    /**
     * 收费定义列表（URL:"/sfdylist",Method:GET）
     * 
     * @return
     * @throws Exception
     */
	@RequestMapping(value = "/sfdylist/{sfbmmc}", method = RequestMethod.GET)
	public @ResponseBody List<HashMap<String,Object>> getSFDYList(@PathVariable("sfbmmc") String sfbmmc,HttpServletRequest request,HttpServletResponse response) throws Exception {
    	List<HashMap<String,Object>> list = tjcxfxService.getSFDYList(sfbmmc);
		return list;
	}
    
    /**
     * 受理人员列表（URL:"/slrylist",Method:GET）
     * 
     * @return
     * @throws Exception
     */
    @SuppressWarnings("rawtypes")
	@RequestMapping(value = "/slrylist/", method = RequestMethod.GET)
	public @ResponseBody List<Map> getSLRYList(HttpServletRequest request,HttpServletResponse response) throws Exception {
    	List<Map> list = tjcxfxService.getSLRYList();
		return list;
	}
    
    /**
     * 收费部门名称列表（URL:"/sfbmmclist",Method:GET）
     * 
     * @return
     * @throws Exception
     */
    @SuppressWarnings("rawtypes")
	@RequestMapping(value = "/sfbmmclist/", method = RequestMethod.GET)
	public @ResponseBody List<Map> getSFBMMCList(HttpServletRequest request,HttpServletResponse response) throws Exception {
    	List<Map> list = tjcxfxService.getSFBMMCList();
		return list;
	}
    
    /** 
	 * 导出收费统计
	 * @作者 俞学斌
	 * @创建时间 2016年4月11日18:21:40
	 * @param request
	 * @param response
	 * @return 
	 * @throws Exception
	 */
	@SuppressWarnings({"rawtypes", "unchecked" })
	@RequestMapping(value = "/sftjdownload", method = RequestMethod.GET)
	public @ResponseBody String SftjBookDownload(HttpServletRequest request, HttpServletResponse response) throws Exception{
		String qsj=request.getParameter("qsj");
    	String zsj=request.getParameter("zsj");
    	String sfry=RequestHelper.getParam(request, "sfry");
    	String sfbmmc=RequestHelper.getParam(request, "sfbmmc");
		Message msg = tjcxfxService.getSFTJ(qsj, zsj,sfry,sfbmmc);
		
		List<Map> sfinfo = (List<Map>)msg.getRows();
		
		String basePath = request.getRealPath("/") + "\\resources\\PDF";
		String outpath = "";
		String url = "";
		String tmpFullName = "";
		FileOutputStream outstream = null;
		if(sfinfo != null && sfinfo.size() > 0){
			outpath = basePath + "\\tmp\\sftj.xls";
			url = request.getContextPath() + "\\resources\\PDF\\tmp\\sftj.xls";
		    outstream = new FileOutputStream(outpath); 
		    tmpFullName = request.getRealPath("/WEB-INF/jsp/wjmb/sftj.xls");
		    InputStream input = new FileInputStream(tmpFullName);
			HSSFWorkbook  wb = null;// 定义一个新的工作簿
			wb = new HSSFWorkbook(input);
			Sheet sheet = wb.getSheetAt(0);
			Map<String,Integer> MapCol = new HashMap<String,Integer>();
			MapCol.put("收费部门", 0);
			MapCol.put("收费类型", 1);
			MapCol.put("金额单位", 2);
			MapCol.put("项目个数", 3);
			MapCol.put("应收金额", 4);
			MapCol.put("实收金额", 5);
			MapCol.put("出让面积", 6);
			HSSFRow row_fliter = (HSSFRow)sheet.getRow(1);
			 HSSFCell Cell_fliter = row_fliter.getCell(0);
			 StringBuilder builder=new StringBuilder();
			 builder.append("统计条件：");
			 builder.append("收费部门：");
			 if("ALL".equals(sfbmmc)){
				 builder.append("全部");
			 }else{
				 builder.append(sfbmmc);
			 }
			 builder.append("；收费人员");
			 if("ALL".equals(sfbmmc)){
				 builder.append("全部");
			 }else{
				 builder.append(sfry);
			 }
			 builder.append("；收费时间");
			 String sfsj="";
			 if(!StringHelper.isEmpty(qsj)){
				 sfsj=sfsj+StringHelper.FormatDateOnType(StringHelper.FormatByDate(qsj), "yyyy年MM月dd日");
			 }else{
				 sfsj=sfsj+"----";
			 }
			 sfsj=sfsj+"至";
			 if(!StringHelper.isEmpty(zsj)){
				 sfsj=sfsj+StringHelper.FormatDateOnType(StringHelper.FormatByDate(zsj), "yyyy年MM月dd日");
			 }else{
				 sfsj=sfsj+"----";
			 }
			 if(!StringHelper.isEmpty(qsj)&&qsj.endsWith(zsj)){
				 sfsj=StringHelper.FormatDateOnType(StringHelper.FormatByDate(zsj), "yyyy年MM月dd日");
			 }
			 builder.append(sfsj).append("。");
			 Cell_fliter.setCellValue(builder.toString());
			 int startrow=3;
			 String t_sfbmmc="";
            int rownum = 3;
			for(Map sf:sfinfo){
				 
		  		 HSSFRow row = (HSSFRow)sheet.createRow(rownum);
		  		 try{
		  			 String tt_sfbmmc=StringHelper.formatObject(sf.get("SFBMMC"));
		  			 if(!"合计".equals(sf.get("SFMC"))){
			  			 HSSFCell Cell0 = row.createCell(MapCol.get("收费部门"));
				         Cell0.setCellValue(StringHelper.formatObject(sf.get("SFBMMC")));
				         HSSFCell Cell1 = row.createCell(MapCol.get("收费类型"));
				         Cell1.setCellValue(StringHelper.formatObject(sf.get("SFMC")));
				         HSSFCell Cell2 = row.createCell(MapCol.get("金额单位"));
				         Cell2.setCellValue("万元");
				         HSSFCell Cell3 = row.createCell(MapCol.get("项目个数"));
				         Cell3.setCellValue(StringHelper.formatObject(sf.get("XMGS")));
				         HSSFCell Cell4 = row.createCell(MapCol.get("应收金额"));
				         Cell4.setCellValue(StringHelper.formatObject(sf.get("YSJE")));
				         HSSFCell Cell5 = row.createCell(MapCol.get("实收金额"));
				         Cell5.setCellValue(StringHelper.formatObject(sf.get("SSJE")));
				         HSSFCell Cell6 = row.createCell(MapCol.get("出让面积"));
				         Cell6.setCellValue(StringHelper.formatObject(sf.get("CRMJ")));
		  			 }else{
			  			 HSSFCell Cell0 = row.createCell(MapCol.get("收费部门"));
				         Cell0.setCellValue(StringHelper.formatObject(sf.get("SFBMMC")));
				         HSSFCell Cell1 = row.createCell(MapCol.get("收费类型"));
				         Cell1.setCellValue(StringHelper.formatObject(sf.get("SFMC")));
				         HSSFCell Cell2 = row.createCell(MapCol.get("金额单位"));
				         Cell2.setCellValue("");
				         HSSFCell Cell3 = row.createCell(MapCol.get("项目个数"));
				         Cell3.setCellValue("");
				         HSSFCell Cell4 = row.createCell(MapCol.get("应收金额"));
				         Cell4.setCellValue(StringHelper.formatObject(sf.get("YSJE")));
				         HSSFCell Cell5 = row.createCell(MapCol.get("实收金额"));
				         Cell5.setCellValue(StringHelper.formatObject(sf.get("SSJE")));
				         HSSFCell Cell6 = row.createCell(MapCol.get("出让面积"));
				         Cell6.setCellValue(StringHelper.formatObject(sf.get("CRMJ")));
				         sheet.addMergedRegion(new CellRangeAddress(rownum,rownum,1,3));// 合并单元格
				         HSSFCell Cellhj = row.getCell(MapCol.get("收费类型"));
				         HSSFCellStyle cellStyle = wb.createCellStyle();
				         cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 水平布局：居中
				         Cellhj.setCellStyle(cellStyle);
		  			 }
		  			 
		        	 if(!tt_sfbmmc.equals(t_sfbmmc)){
			        	 if(rownum>startrow+1){
			        		 sheet.addMergedRegion(new CellRangeAddress(startrow,rownum-1,0,0));// 合并单元格
			        	 }
			        	 startrow=rownum;
			        	 t_sfbmmc=tt_sfbmmc;
			         }
			         
			         rownum ++;
		  		 }
		  		 catch(Exception ex){
		  			System.out.println("导出数据有误");
		  			ex.printStackTrace();
		  		 }
			  }
			HSSFCellStyle style = wb.createCellStyle();
			style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 水平布局：居中
			style.setBorderTop(HSSFCellStyle.BORDER_THIN);
			style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			style.setBorderRight(HSSFCellStyle.BORDER_THIN);
			style.setWrapText(true);
			for(int row =3 ; row<rownum ; row++){
				for(int col = 0 ; col<7 ;col++){
					sheet.getRow(row).getCell(col).setCellStyle(style);
				}
			}
			 wb.write(outstream); 
			 outstream.flush(); 
			 outstream.close();
		 }

        return url;
	}
	
	/** 
	 * 导出收费明细
	 * @作者 俞学斌
	 * @创建时间 2016年4月11日18:21:40
	 * @param request
	 * @param response
	 * @return 
	 * @throws Exception
	 */
	@SuppressWarnings({"rawtypes", "unchecked" })
	@RequestMapping(value = "/sfmxtjdownload", method = RequestMethod.GET)
	public @ResponseBody String SfmxtjBookDownload(HttpServletRequest request, HttpServletResponse response) throws Exception{
		String t_qsj=request.getParameter("qsj");
    	String t_zsj=request.getParameter("zsj");
    	String t_slry=RequestHelper.getParam(request, "slry");
    	String t_sfry=RequestHelper.getParam(request, "sfry");
    	String t_sfbmmc=RequestHelper.getParam(request, "sfbmmc");
    	String t_sflx=RequestHelper.getParam(request, "sflx");
		Message msg = tjcxfxService.getSFMXTJ(t_qsj, t_zsj,t_slry,t_sfbmmc,t_sflx,t_sfry,null);
		
		List<Map> sfinfo = (List<Map>)msg.getRows();
		
		String basePath = request.getRealPath("/") + "\\resources\\PDF";
		String outpath = "";
		String url = "";
		String tmpFullName = "";
		FileOutputStream outstream = null;
		if(sfinfo != null && sfinfo.size() > 0){
			outpath = basePath + "\\tmp\\sfmxtj.xls";
			url = request.getContextPath() + "\\resources\\PDF\\tmp\\sfmxtj.xls";
		    outstream = new FileOutputStream(outpath); 
		    tmpFullName = request.getRealPath("/WEB-INF/jsp/wjmb/sfmxtj.xls");
		    InputStream input = new FileInputStream(tmpFullName);
			HSSFWorkbook  wb = null;// 定义一个新的工作簿
			wb = new HSSFWorkbook(input);
			Sheet sheet = wb.getSheetAt(0);
			Map<String,Integer> MapCol = new HashMap<String,Integer>();
			MapCol.put("业务号", 0);
			MapCol.put("受理人员", 1);
			MapCol.put("收费人员", 2);
			MapCol.put("申请人", 3);
			MapCol.put("收费部门", 4);
			MapCol.put("收费类型", 5);
			MapCol.put("个数", 6);
			MapCol.put("应收金额", 7);
			MapCol.put("实收金额", 8);
			HSSFRow row_fliter = (HSSFRow)sheet.getRow(1);
			 HSSFCell Cell_fliter = row_fliter.getCell(0);
			 StringBuilder builder=new StringBuilder();
			 builder.append("统计明细条件：");
			 builder.append("收费部门：");
			 if(StringHelper.isEmpty(t_sfbmmc)){
				 builder.append("全部");
			 }else{
				 builder.append(t_sfbmmc);
			 }
			 builder.append("；受理人员：");
			 if(StringHelper.isEmpty(t_slry)){
				 builder.append("全部");
			 }else{
				 builder.append(t_slry);
			 }
			 builder.append("；收费人员：");
			 if(StringHelper.isEmpty(t_sfry)){
				 builder.append("全部");
			 }else{
				 builder.append(t_sfry);
			 }
			 builder.append("；收费时间");
			 String sfsj="";
			 if(!StringHelper.isEmpty(t_qsj)){
				 sfsj=sfsj+StringHelper.FormatDateOnType(StringHelper.FormatByDate(t_qsj), "yyyy年MM月dd日");
			 }else{
				 sfsj=sfsj+"----";
			 }
			 sfsj=sfsj+"至";
			 if(!StringHelper.isEmpty(t_zsj)){
				 sfsj=sfsj+StringHelper.FormatDateOnType(StringHelper.FormatByDate(t_zsj), "yyyy年MM月dd日");
			 }else{
				 sfsj=sfsj+"----";
			 }
			 if(!StringHelper.isEmpty(t_qsj)&&t_qsj.endsWith(t_zsj)){
				 sfsj=StringHelper.FormatDateOnType(StringHelper.FormatByDate(t_zsj), "yyyy年MM月dd日");
			 }
			 builder.append(sfsj).append("。");
			 Cell_fliter.setCellValue(builder.toString());
			String prolsh="";
			String sfbmmc="";
			int startrow_prolsh=-1;
			int startrow_sfbmmc=-1;
			int rownum = 3;
			for(Map sf:sfinfo){
				 
		  		 HSSFRow row = (HSSFRow)sheet.createRow(rownum);
		  		 try{
		  			 String m_prolsh=StringHelper.formatObject(sf.get("PROLSH"));
		  			 String m_sfbmmc=StringHelper.formatObject(sf.get("SFBMMC"));
		  			 if(!"合计".equals(sf.get("PROLSH"))){
			  			 HSSFCell Cell0 = row.createCell(MapCol.get("业务号"));
				         Cell0.setCellValue(StringHelper.formatObject(sf.get("PROLSH")));
				         HSSFCell Cell1 = row.createCell(MapCol.get("受理人员"));
				         Cell1.setCellValue(StringHelper.formatObject(sf.get("ACCEPTOR")));
				         HSSFCell Cell2 = row.createCell(MapCol.get("收费人员"));
				         Cell2.setCellValue(StringHelper.formatObject(sf.get("SFRY")));
				         HSSFCell Cell3 = row.createCell(MapCol.get("申请人"));
				         String qlrmc=StringHelper.formatObject(sf.get("QLRMC"));
				         String ywrmc=StringHelper.formatObject(sf.get("YWRMC"));
				         StringBuilder builder_sqr=new StringBuilder();
				         if(!StringHelper.isEmpty(qlrmc)){
				        	 builder_sqr.append("权利人：").append(qlrmc).append("\r\n");
				         }
				         if(!StringHelper.isEmpty(ywrmc)){
				        	 builder_sqr.append("义务人：").append(ywrmc);
				         }
				         Cell3.setCellValue(builder_sqr.toString());
				         HSSFCell Cell4 = row.createCell(MapCol.get("收费部门"));
				         Cell4.setCellValue(StringHelper.formatObject(sf.get("SFBMMC")));
				         HSSFCell Cell5 = row.createCell(MapCol.get("收费类型"));
				         Cell5.setCellValue(StringHelper.formatObject(sf.get("SFLX")));
				         HSSFCell Cell6 = row.createCell(MapCol.get("个数"));
				         Cell6.setCellValue(StringHelper.formatObject(sf.get("GS")));
				         HSSFCell Cell7 = row.createCell(MapCol.get("应收金额"));
				         Cell7.setCellValue(StringHelper.formatObject(sf.get("YSJE")));
				         HSSFCell Cell8 = row.createCell(MapCol.get("实收金额"));
				         Cell8.setCellValue(StringHelper.formatObject(sf.get("SSJE")));
		  			 }else{
			  			 HSSFCell Cell0 = row.createCell(MapCol.get("业务号"));
				         Cell0.setCellValue(StringHelper.formatObject(sf.get("PROLSH")));
				         HSSFCell Cell1 = row.createCell(MapCol.get("受理人员"));
				         Cell1.setCellValue("");
				         HSSFCell Cell2 = row.createCell(MapCol.get("收费人员"));
				         Cell2.setCellValue("");
				         HSSFCell Cell3 = row.createCell(MapCol.get("申请人"));
				         Cell3.setCellValue("");
				         HSSFCell Cell4 = row.createCell(MapCol.get("收费部门"));
				         Cell4.setCellValue("");
				         HSSFCell Cell5 = row.createCell(MapCol.get("收费类型"));
				         Cell5.setCellValue("");
				         HSSFCell Cell6 = row.createCell(MapCol.get("个数"));
				         Cell6.setCellValue("");
				         HSSFCell Cell7 = row.createCell(MapCol.get("应收金额"));
				         Cell7.setCellValue(StringHelper.formatObject(sf.get("YSJE")));
				         HSSFCell Cell8 = row.createCell(MapCol.get("实收金额"));
				         Cell8.setCellValue(StringHelper.formatObject(sf.get("SSJE")));
				         sheet.addMergedRegion(new CellRangeAddress(rownum,rownum,0,7));// 合并单元格
				         HSSFCell Cellhj = row.getCell(MapCol.get("业务号"));
				         HSSFCellStyle cellStyle = wb.createCellStyle();
				         cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 水平布局：居中
				         Cellhj.setCellStyle(cellStyle);
		  			 }
		  			 if(!sfbmmc.equals(m_sfbmmc)||!prolsh.equals(m_prolsh)||rownum==(sfinfo.size()-1)){
		  				 sfbmmc=m_sfbmmc;
		  				 if((startrow_sfbmmc>-1)&&(startrow_sfbmmc<(rownum-1))){
		  					 sheet.addMergedRegion(new CellRangeAddress(startrow_sfbmmc,rownum-1,4,4));// 合并单元格
		  				 }
		  				 startrow_sfbmmc=rownum;
		  			 }
		  			 if(!prolsh.equals(m_prolsh)||rownum==(sfinfo.size()+2)){
		  				 prolsh=m_prolsh;
		  				 if((startrow_prolsh>-1)&&(startrow_prolsh<(rownum-1))){
		  					 sheet.addMergedRegion(new CellRangeAddress(startrow_prolsh,rownum-1,3,3));// 合并单元格
		  					 sheet.addMergedRegion(new CellRangeAddress(startrow_prolsh,rownum-1,2,2));// 合并单元格
		  					 sheet.addMergedRegion(new CellRangeAddress(startrow_prolsh,rownum-1,1,1));// 合并单元格
		  					 sheet.addMergedRegion(new CellRangeAddress(startrow_prolsh,rownum-1,0,0));// 合并单元格
		  				 }
		  				 startrow_prolsh=rownum;
		  			 }
			         rownum ++;
		  		 }
		  		 catch(Exception ex){
		  			System.out.println("导出数据有误");
		  			ex.printStackTrace();
		  		 }
			  }
			HSSFCellStyle cellStyle = wb.createCellStyle();
			cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 水平布局：居中
	        cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
            cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);   //左边框
            cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);    //上边框
	        cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);  //右边框
	        cellStyle.setWrapText(true); 
	        for(int row = 3; row<rownum ;row++){
	        	for(int col =0 ; col<9 ; col++){
	        		sheet.getRow(row).getCell(col).setCellStyle(cellStyle);
	        	}
	        }
            /*for(int i = 0; i < sheet.getNumMergedRegions(); i++) {
                  int startRow = sheet.getMergedRegion(i).getFirstRow();
                  int endRow = sheet.getMergedRegion(i).getLastRow();
                  int startColumn = sheet.getMergedRegion(i).getFirstColumn();
                  int endColumn = sheet.getMergedRegion(i).getLastColumn();
                  for(int row = startRow ; row <=endRow ; row++){//row<=endRow  此处必须是小于等于，如果一共9行 那么endRow = 8 ; 0到8 达到9位数必须是小于等于
                	  for(int col = startColumn ; col <=endColumn ; col++){
                		  sheet.getRow(startRow).getCell(startColumn).setCellStyle(cellStyle);
                	}
                }
            }*/
			 wb.write(outstream); 
			 outstream.flush(); 
			 outstream.close();
		 }

        return url;
	}
	
	/**
     * 获取角色列表（URL:"/departmentlist",Method:GET）
     * 
     * @return
     * @throws Exception
     */
    @SuppressWarnings("rawtypes")
	@RequestMapping(value = "/departmentlist/", method = RequestMethod.GET)
	public @ResponseBody List<Map> getDepartmentlistList(HttpServletRequest request,HttpServletResponse response) throws Exception {
    	List<Map> list = tjcxfxService.getDepartmentlistList();
		return list;
	}
	
	/**
     * 获取角色列表（URL:"/rolelist",Method:GET）
     * 
     * @return
     * @throws Exception
     */
    @SuppressWarnings("rawtypes")
	@RequestMapping(value = "/rolelist/", method = RequestMethod.GET)
	public @ResponseBody List<Map> getRoleList(HttpServletRequest request,HttpServletResponse response) throws Exception {
    	List<Map> list = tjcxfxService.getRoleList();
		return list;
	}
    
    /**
     * 获取人员列表（URL:"/userlist",Method:GET）
     * 
     * @return
     * @throws Exception
     */
    @SuppressWarnings("rawtypes")
	@RequestMapping(value = "/userlist/", method = RequestMethod.GET)
	public @ResponseBody List<Map> getUserList(HttpServletRequest request,HttpServletResponse response) throws Exception {
    	List<Map> list = tjcxfxService.getUserList(request);
		return list;
	}
    
    /**
     * 办件明细统计（URL:"/bjmxtj",Method:GET）
     * 
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/bjmxtj/", method = RequestMethod.GET)
	public @ResponseBody Message getBJMXTJ(HttpServletRequest request,HttpServletResponse response) throws Exception {
    	String qsj=request.getParameter("qsj");
    	String zsj=request.getParameter("zsj");
    	String departmentid=RequestHelper.getParam(request, "departmentid");
    	String userid=RequestHelper.getParam(request, "userid");
    	String isdyinfo=request.getParameter("isdyinfo");
		Message msg = tjcxfxService.getBJMXTJ(qsj, zsj,departmentid,userid,isdyinfo);
		return msg;
	}
    
    /** 
	 * 导出办件明细
	 * @作者 俞学斌
	 * @创建时间 2016年12月08日08:42:40
	 * @param request
	 * @param response
	 * @return 
	 * @throws Exception
	 */
	@SuppressWarnings({"rawtypes", "unchecked" })
	@RequestMapping(value = "/bjmxtjdownload", method = RequestMethod.GET)
	public @ResponseBody String BjmxtjBookDownload(HttpServletRequest request, HttpServletResponse response) throws Exception{
		String qsj=request.getParameter("qsj");
    	String zsj=request.getParameter("zsj");
    	String departmentid=RequestHelper.getParam(request, "departmentid");
    	String userid=RequestHelper.getParam(request, "userid");
    	String isdyinfo=request.getParameter("isdyinfo");
		Message msg = tjcxfxService.getBJMXTJ(qsj, zsj,departmentid,userid,isdyinfo);
		
		List<Map> blqklist = (List<Map>)msg.getRows();
		
		String basePath = request.getRealPath("/") + "\\resources\\PDF";
		String outpath = "";
		String url = "";
		String tmpFullName = "";
		FileOutputStream outstream = null;
		if(blqklist != null && blqklist.size() > 0){
			outpath = basePath + "\\tmp\\bjmxtj.xls";
			url = request.getContextPath() + "\\resources\\PDF\\tmp\\bjmxtj.xls";
		    outstream = new FileOutputStream(outpath); 
		    if("1".equals(isdyinfo)){
		    	tmpFullName = request.getRealPath("/WEB-INF/jsp/wjmb/bjmxtjEx.xls");
		    }else{
		    	tmpFullName = request.getRealPath("/WEB-INF/jsp/wjmb/bjmxtj.xls");
		    }
		    
		    InputStream input = new FileInputStream(tmpFullName);
			HSSFWorkbook  wb = null;// 定义一个新的工作簿
			wb = new HSSFWorkbook(input);
			Sheet sheet = wb.getSheetAt(0);
			Map<String,Integer> MapCol = new HashMap<String,Integer>();
			MapCol.put("序号", 0);
			MapCol.put("业务号", 1);
			MapCol.put("登记类型", 2);
			MapCol.put("办理人员", 3);
			MapCol.put("办理时间", 4);
			MapCol.put("是否转出", 5);
			MapCol.put("单元号", 6);
			MapCol.put("是否关联", 7);
			int rownum = 1;
			for(Map bjqk:blqklist){
		  		 HSSFRow row = (HSSFRow)sheet.createRow(rownum);
		  		 try{
		  			 HSSFCell Cell0 = row.createCell(MapCol.get("序号"));
			         Cell0.setCellValue(StringHelper.formatObject(bjqk.get("xh")));
			         HSSFCell Cell1 = row.createCell(MapCol.get("业务号"));
			         Cell1.setCellValue(StringHelper.formatObject(bjqk.get("ywh")));
			         HSSFCell Cell2 = row.createCell(MapCol.get("登记类型"));
			         Cell2.setCellValue(StringHelper.formatObject(bjqk.get("djlx")));
			         HSSFCell Cell3 = row.createCell(MapCol.get("办理人员"));
			         Cell3.setCellValue(StringHelper.formatObject(bjqk.get("blry")));
			         HSSFCell Cell4 = row.createCell(MapCol.get("办理时间"));
			         Cell4.setCellValue(StringHelper.formatObject(bjqk.get("blsj")));
			         HSSFCell Cell5 = row.createCell(MapCol.get("是否转出"));
			         String sfzc=StringHelper.formatObject(bjqk.get("sfzc"));
			         if("1".equals(sfzc)){
		        	 	 sfzc="是";
					 }else{
						 sfzc="否";
					 }
			         Cell5.setCellValue(sfzc);
			         if("1".equals(isdyinfo)){
			        	 HSSFCell Cell6 = row.createCell(MapCol.get("单元号"));
				         Cell6.setCellValue(StringHelper.formatObject(bjqk.get("dyh")));
				         HSSFCell Cell7 = row.createCell(MapCol.get("是否关联"));
				         String sfgl=StringHelper.formatObject(bjqk.get("sfgl"));
				         if("2".equals(sfgl)){
				        	 sfgl="是";
				         }else if("1".equals(sfgl)){
				        	 sfgl="否";
				         }else if("0".equals(sfgl)){
				        	 sfgl="否";
				         }else{
				        	 sfgl="";
				         }
				         Cell7.setCellValue(sfgl);
			         }
			         rownum ++;
		  		 }
		  		 catch(Exception ex){
		  			System.out.println("导出数据有误");
		  			ex.printStackTrace();
		  		 }
			  }
			
			 wb.write(outstream); 
			 outstream.flush(); 
			 outstream.close();
		 }

        return url;
	}
	
	/** 
	 * 土地用途字典
	 * @作者 luml
	 * @创建时间 2017年6月19日18:21:40
	 * @return 
	 * 
	 */
	@RequestMapping(value = "/tdytzd", method = RequestMethod.GET)
	public @ResponseBody List<Map>  GetStandingBook_tdytzd() {
		List<Map> tdytcls = baseCommonDao.getDataListByFullSql("SELECT CONSTVALUE,CONSTTRANS FROM BDCK.BDCS_CONST WHERE CONSTSLSID='54'");
		return tdytcls;
	}
	
	/** 
	 * 房屋用途字典
	 * @作者 luml
	 * @创建时间 2017年6月19日18:21:40
	 * @return 
	 * 
	 */
	@RequestMapping(value = "/fwytzd", method = RequestMethod.GET)
	public @ResponseBody List<Map>  GetStandingBook_fwytzd() {
		List<Map> fwytcls = baseCommonDao.getDataListByFullSql("SELECT CONSTVALUE,CONSTTRANS FROM BDCK.BDCS_CONST WHERE CONSTSLSID='17'");
		return fwytcls;
	}	
	
	/** 权利性质字典
	 * @return
	 */
	@RequestMapping(value = "/qlxzzd", method = RequestMethod.GET)
	public @ResponseBody List<Map>  GetStandingBook_qlxzzd() {
		List<Map> qlxzcls = baseCommonDao.getDataListByFullSql("SELECT CONSTVALUE,CONSTTRANS FROM BDCK.BDCS_CONST WHERE CONSTSLSID='9'");
		return qlxzcls;
	}	
	
	/** 地籍区
	 * @return
	 */
	@RequestMapping(value = "/djq", method = RequestMethod.GET)
	public @ResponseBody List<Map>  GetStandingBook_djq() {
		List<Map> djq = baseCommonDao.getDataListByFullSql("SELECT XZQDM, XZQMC FROM BDCK.BDCK_DJQ");
		return djq;
	}	
	
	/** 
	 * 登记缮证统计
	 * @作者 rq
	 * @创建时间 2017年6月27日
	 * @return 
	 * @throws UnsupportedEncodingException 
	 * 
	 */
	@RequestMapping(value = "/djsztj", method = RequestMethod.GET)
	public @ResponseBody Message GetCertificateStatistics(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		String SZSJ_Q=request.getParameter("SZSJ_Q");
    	String SZSJ_Z=request.getParameter("SZSJ_Z");
    	String QZLX=RequestHelper.getParam(request, "QZLX");
    	String SZRY=RequestHelper.getParam(request, "SZRY");
    	String SearchStates=RequestHelper.getParam(request, "SEARCHSTATES");
		Message msg = tjcxfxService.getCertificateStatistics(SZSJ_Q, SZSJ_Z,SZRY,SearchStates);
		return msg;
	}
	/** 
	 * 登记缮证统计导出
	 * @作者 rq
	 * @创建时间 2017年6月27日
	 * @return 
	 * @throws IOException 
	 * 
	 */
	@RequestMapping(value = "/djsztj/export", method = RequestMethod.GET)
	public @ResponseBody String ExportCertificateStatistics(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String SZSJ_Q=request.getParameter("SZSJ_Q");
    	String SZSJ_Z=request.getParameter("SZSJ_Z");
    	String SZRY=RequestHelper.getParam(request, "SZRY");
    	String SearchStates=RequestHelper.getParam(request, "SEARCHSTATES");
		Message msg = tjcxfxService.getCertificateStatistics(SZSJ_Q, SZSJ_Z,SZRY,SearchStates);
		List<Map> rows=(List<Map>) msg.getRows();
		
		//导出
		String basePath = request.getRealPath("/") + "\\resources\\PDF";
		String outpath = "";
		String url = "";
		FileOutputStream outstream = null;
		outpath = basePath + "\\tmp\\djsztj.xls";
		url = request.getContextPath() + "\\resources\\PDF\\tmp\\djsztj.xls";
	    outstream = new FileOutputStream(outpath); 

	    String tplFullName = request.getRealPath("/WEB-INF/jsp/wjmb/zstj.xls");
	    InputStream input = new FileInputStream(tplFullName);
		HSSFWorkbook  wb = null;// 定义一个新的工作簿
		wb = new HSSFWorkbook(input);
		HSSFSheet sheet = wb.getSheetAt(0);
		Map<String,Integer> MapCol = new HashMap<String,Integer>();
		
		//所选字段
		String selectFields=RequestHelper.getParam(request, "selectFields");
		JSONArray jsonArray = JSONArray.fromObject(selectFields);
		List<Map> mapListJson = (List) jsonArray;
		//获取标题行
		HSSFRow head = sheet.getRow(0);	
		HSSFCell Cell = head.getCell(0);
		Cell.setCellValue("登记缮证统计") ;
		//添加表头行
		HSSFRow heads = (HSSFRow)sheet.getRow(1);
		
		HSSFCell title_Cell0 = heads.createCell(0);
		title_Cell0.setCellValue("缮证人员") ;
		MapCol.put("缮证人员", 0);
		
		HSSFCell title_Cell1 = heads.createCell(1);
		title_Cell1.setCellValue("证书") ;
		MapCol.put("证书", 1);
		
		HSSFCell title_Cell2 = heads.createCell(2);
		title_Cell2.setCellValue("证明") ;
		MapCol.put("证明", 2);
		
		HSSFCell title_Cell3 = heads.createCell(3);
		title_Cell3.setCellValue("缮证总量") ;
		MapCol.put("缮证总量", 3);
		
		//添加内容行
		int rownum = 2;
		for(Map r:rows){
			if (r.get("HJ") == null) {
			 HSSFRow row = (HSSFRow)sheet.createRow(rownum);
	         HSSFCell Cell00 = row.createCell(MapCol.get("缮证人员"));
	         Cell00.setCellValue((StringHelper.FormatByDatatype(r.get("SZR"))));
	         HSSFCell Cell01 = row.createCell(MapCol.get("证书"));
	         Cell01.setCellValue((StringHelper.FormatByDatatype(r.get("ZSGS"))));
	         HSSFCell Cell02 = row.createCell(MapCol.get("证明"));
	         Cell02.setCellValue((StringHelper.FormatByDatatype(r.get("ZMGS"))));
	         HSSFCell Cell03 = row.createCell(MapCol.get("缮证总量"));
	         Cell03.setCellValue((StringHelper.FormatByDatatype(r.get("ZGS"))));
	         rownum += 1;
			}else {
			 HSSFRow row = (HSSFRow)sheet.createRow(rownum);	        
			 HSSFCell Cell00 = row.createCell(MapCol.get("缮证人员"));
	         Cell00.setCellValue((StringHelper.FormatByDatatype(r.get("HJ"))));
	         HSSFCell Cell01 = row.createCell(MapCol.get("证书"));
	         Cell01.setCellValue((StringHelper.FormatByDatatype(r.get("ZZS"))));
	         HSSFCell Cell02 = row.createCell(MapCol.get("证明"));
	         Cell02.setCellValue((StringHelper.FormatByDatatype(r.get("ZZM"))));
	         HSSFCell Cell03 = row.createCell(MapCol.get("缮证总量"));
	         Cell03.setCellValue((StringHelper.FormatByDatatype(r.get("ALL"))));
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
	 * 登记发证统计
	 * @作者 rq
	 * @创建时间 2017年6月27日
	 * @return 
	 * @throws UnsupportedEncodingException 
	 * 
	 */
	@RequestMapping(value = "/djfztj", method = RequestMethod.GET)
	public @ResponseBody Message GetIssueStatistics(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		String FZSJ_Q=request.getParameter("FZSJ_Q");
    	String FZSJ_Z=request.getParameter("FZSJ_Z");
    	String QZLX=RequestHelper.getParam(request, "QZLX");
    	String SearchStates=RequestHelper.getParam(request, "SEARCHSTATES");
		Message msg = tjcxfxService.getIssueStatistics(FZSJ_Q, FZSJ_Z,SearchStates);
		return msg;
	}
	
	/** 
	 * 登记发证统计导出
	 * @作者 rq
	 * @创建时间 2017年6月27日
	 * @return 
	 * @throws IOException 
	 * 
	 */
	@RequestMapping(value = "/djfztj/export", method = RequestMethod.GET)
	public @ResponseBody String ExportIssueStatistics(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String FZSJ_Q=request.getParameter("FZSJ_Q");
    	String FZSJ_Z=request.getParameter("FZSJ_Z");
    	String FZRY=RequestHelper.getParam(request, "FZRY");
    	String SearchStates=RequestHelper.getParam(request, "SEARCHSTATES");
		Message msg = tjcxfxService.getIssueStatistics(FZSJ_Q, FZSJ_Z,SearchStates);
		List<Map> rows=(List<Map>) msg.getRows();
		
		//导出
		String basePath = request.getRealPath("/") + "\\resources\\PDF";
		String outpath = "";
		String url = "";
		FileOutputStream outstream = null;
		outpath = basePath + "\\tmp\\djfztj.xls";
		url = request.getContextPath() + "\\resources\\PDF\\tmp\\djfztj.xls";
	    outstream = new FileOutputStream(outpath); 

	    String tplFullName = request.getRealPath("/WEB-INF/jsp/wjmb/zstj.xls");
	    InputStream input = new FileInputStream(tplFullName);
		HSSFWorkbook  wb = null;// 定义一个新的工作簿
		wb = new HSSFWorkbook(input);
		HSSFSheet sheet = wb.getSheetAt(0);
		Map<String,Integer> MapCol = new HashMap<String,Integer>();
		
		//所选字段
		String selectFields=RequestHelper.getParam(request, "selectFields");
		JSONArray jsonArray = JSONArray.fromObject(selectFields);
		List<Map> mapListJson = (List) jsonArray;
		//获取标题行
		HSSFRow head = sheet.getRow(0);	
		HSSFCell Cell = head.getCell(0);
		Cell.setCellValue("登记发证统计") ;
		//添加表头行
		HSSFRow heads = (HSSFRow)sheet.getRow(1);
		
		HSSFCell title_Cell0 = heads.createCell(0);
		title_Cell0.setCellValue("发证人员") ;
		MapCol.put("发证人员", 0);
		
		HSSFCell title_Cell1 = heads.createCell(1);
		title_Cell1.setCellValue("证书") ;
		MapCol.put("证书", 1);
		
		HSSFCell title_Cell2 = heads.createCell(2);
		title_Cell2.setCellValue("证明") ;
		MapCol.put("证明", 2);
		
		HSSFCell title_Cell3 = heads.createCell(3);
		title_Cell3.setCellValue("发证总量") ;
		MapCol.put("发证总量", 3);
		
		//添加内容行
		int rownum = 2;
		for(Map r:rows){
			if (r.get("HJ") == null) {
			 HSSFRow row = (HSSFRow)sheet.createRow(rownum);
	         HSSFCell Cell00 = row.createCell(MapCol.get("发证人员"));
	         Cell00.setCellValue((StringHelper.FormatByDatatype(r.get("FZRY"))));
	         HSSFCell Cell01 = row.createCell(MapCol.get("证书"));
	         Cell01.setCellValue((StringHelper.FormatByDatatype(r.get("ZSGS"))));
	         HSSFCell Cell02 = row.createCell(MapCol.get("证明"));
	         Cell02.setCellValue((StringHelper.FormatByDatatype(r.get("ZMGS"))));
	         HSSFCell Cell03 = row.createCell(MapCol.get("发证总量"));
	         Cell03.setCellValue((StringHelper.FormatByDatatype(r.get("ZGS"))));
	         rownum += 1;
			}else {
			 HSSFRow row = (HSSFRow)sheet.createRow(rownum);	        
			 HSSFCell Cell00 = row.createCell(MapCol.get("发证人员"));
	         Cell00.setCellValue((StringHelper.FormatByDatatype(r.get("HJ"))));
	         HSSFCell Cell01 = row.createCell(MapCol.get("证书"));
	         Cell01.setCellValue((StringHelper.FormatByDatatype(r.get("ZZS"))));
	         HSSFCell Cell02 = row.createCell(MapCol.get("证明"));
	         Cell02.setCellValue((StringHelper.FormatByDatatype(r.get("ZZM"))));
	         HSSFCell Cell03 = row.createCell(MapCol.get("发证总量"));
	         Cell03.setCellValue((StringHelper.FormatByDatatype(r.get("ALL"))));
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
	 * 不动产抵押台账导出
	 * @作者 taocd
	 * @创建时间 2017年9月26日
	 * @return 
	 * @throws Exception 
	 * 
	 */
	@SuppressWarnings({ "deprecation", "rawtypes", "unchecked" })
	@RequestMapping(value = "/dytz/export", method = RequestMethod.GET)
	public @ResponseBody String DYTZExport(HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("utf-8");
		Integer page = 1;
		if (request.getParameter("page") != null) {
			page = Integer.parseInt(request.getParameter("page"));
		}
		Integer pagesize = 10;
		if (request.getParameter("rows") != null) {
			pagesize = Integer.parseInt(request.getParameter("rows"));
		}
		Map<String, String> mapCondition = new HashMap<String, String>();
		mapCondition.put("TJLX", request.getParameter("TJLX"));
		mapCondition.put("BDCDYH", request.getParameter("BDCDYH"));
		mapCondition.put("BDCQZH", request.getParameter("BDCQZH"));
		mapCondition.put("DJSJ_Q", request.getParameter("DJSJ_Q"));
		mapCondition.put("DJSJ_Z", request.getParameter("DJSJ_Z"));
		mapCondition.put("SLSJ_Q", request.getParameter("SLSJ_Q"));
		mapCondition.put("SLSJ_Z", request.getParameter("SLSJ_Z"));
		mapCondition.put("DJLX", request.getParameter("DJLX"));
		mapCondition.put("SQRLX", request.getParameter("SQRLX"));//抵押人类型 
		if(StringHelper.isEmpty(request.getParameter("ZL"))){
			mapCondition.put("ZL", "");
		}else{
			mapCondition.put("ZL", request.getParameter("ZL").trim());
		}
		if(StringHelper.isEmpty(request.getParameter("DYR"))){
			mapCondition.put("DYR", "");
		}else{
			mapCondition.put("DYR", request.getParameter("DYR").trim());
		}
		mapCondition.put("TDYT", request.getParameter("TDYT"));
		Message message = tjcxfxService.GetDiyInfo(mapCondition, page, pagesize);
		List<Map> rows=(List<Map>) message.getRows();
		
		//导出
		String basePath = request.getRealPath("/") + "\\resources\\PDF";
		String outpath = "";
		String url = "";
		FileOutputStream outstream = null;
		outpath = basePath + "\\tmp\\bdcdytz.xls";
		url = request.getContextPath() + "\\resources\\PDF\\tmp\\bdcdytz.xls";
	    outstream = new FileOutputStream(outpath); 

	    String tplFullName = request.getRealPath("/WEB-INF/jsp/wjmb/bdcdytz.xls");
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
