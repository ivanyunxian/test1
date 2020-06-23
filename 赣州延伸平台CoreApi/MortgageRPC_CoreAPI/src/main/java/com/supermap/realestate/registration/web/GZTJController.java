package com.supermap.realestate.registration.web;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.Region;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.supermap.realestate.registration.dataExchange.JHK.Imp.DataSwapImpEx;
import com.supermap.realestate.registration.service.GZTJService;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import com.supermap.wisdombusiness.web.Message;
import com.supermap.wisdombusiness.web.ResultMessage;
import com.supermap.wisdombusiness.web.ui.tree.Tree;
import com.supermap.wisdombusiness.workflow.service.wfm.SmMaterialService;
import com.supermap.wisdombusiness.workflow.service.wfm.SmProInstService;
import com.supermap.realestate.registration.tools.ShareMsgTools;
import com.supermap.realestate.registration.util.ConfigHelper;
import com.supermap.realestate.registration.util.ConstHelper;
import com.supermap.realestate.registration.util.JH_DBHelper;
import com.supermap.realestate.registration.util.RequestHelper;
import com.supermap.realestate.registration.util.StringHelper;

/**
 * 
 * 赣州统计Controller
 * 
 * @author 胡加红
 *
 * @Copyright SuperMap
 */
@Controller
@RequestMapping("/gztj")
public class GZTJController {
    @Autowired
    private CommonDao dao;
    /** 赣州统计service */
    @Autowired
    private GZTJService gztjService;

    @Autowired
    private ShareMsgTools shareMsgTools;

    @Autowired
    private SmProInstService spService;
    
	@Autowired
	private SmMaterialService smMaterialService;
    /**
     * 收费统计（URL:"/sftj",Method:GET）
     * 
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/sftj/{qsj}/{zsj}", method = RequestMethod.GET)
	public @ResponseBody Message getSFDJ(@PathVariable("qsj") String qsj,
			@PathVariable("zsj") String zsj) throws Exception {
		Message msg = gztjService.GetSFXX(qsj, zsj);
		return msg;
	}

    @RequestMapping(value="depts",method=RequestMethod.GET)
    public @ResponseBody Message getDepts(HttpServletRequest request,
    	    HttpServletResponse response)throws Exception{
    	Message msg = gztjService.GetDepts();
    	return msg;
    }
    /**
     * 房屋统计（URL:"/fwtj",Method:GET）
     * 
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "{fwOrtd}/fwtj/{sfsj}", method = RequestMethod.GET)
    public @ResponseBody Message getFWDJ(@PathVariable("sfsj") String sfsj,
	    @PathVariable("fwOrtd") String fwOrtd, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	Message msg = gztjService.GetFWDJ(sfsj, fwOrtd);
	return msg;
    }

    /**
     * 部门统计
     * @author 崔佳慧
     * @return
     * @throws
     */
    @RequestMapping(value = "{fwOrtd}/dept/{qsj}/{zsj}/{selDept}", method = RequestMethod.GET)
    public @ResponseBody Message GetDEPT(@PathVariable("qsj") String qsj,@PathVariable("zsj") String zsj,@PathVariable("fwOrtd") String fwOrtd,
    		@PathVariable("selDept")String selDept,HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	Message msg = gztjService.GetDEPT(qsj,zsj,fwOrtd,selDept);
	return msg;
    }
    
//    /**
//   	 * 导出部门统计 
//   	 * @author 崔佳慧
//     * @return
//     * @throws
//   	 */
//   	@SuppressWarnings({"rawtypes", "unchecked" })
//   	@RequestMapping(value = "/deptexcel/{qsj}/{zsj}/{deptname}/{fwortd}", method = RequestMethod.GET)
//   	public @ResponseBody String deptexcel(@PathVariable("qsj") String qsj,@PathVariable("zsj") String zsj,@PathVariable("deptname") String deptname,@PathVariable("fwortd") String fwortd,HttpServletRequest request,HttpServletResponse response) throws Exception {
//   		Map<String, String> mapcondition = new HashMap<String, String>();
//   		Message m = gztjService.Getdepttj(qsj,zsj,deptname,fwortd);
//   		List<Map> djfz = null;
//   		if (m != null && m.getRows() != null) {
//   			djfz = (List<Map>) m.getRows();
//   		}
//   		String basePath = request.getRealPath("/") + "\\resources\\PDF";
//   		String outPath = "";
//   		String url = "";
//   		String tmpFullName = "";
//   		FileOutputStream outStream = null;
//   		if (djfz != null && djfz.size() > 0) {
//   			outPath = basePath + "\\tmp\\depttj.xls";
//   			url = request.getContextPath()
//   					+ "\\resources\\PDF\\tmp\\depttj.xls";
//   			outStream = new FileOutputStream(outPath);
//   			tmpFullName = request.getRealPath("/WEB-INF/jsp/wjmb/depttj.xls");
//   			InputStream input = new FileInputStream(tmpFullName);
//   			HSSFWorkbook hw = null;
//   			hw = new HSSFWorkbook(input);
//   			HSSFSheet sheet = hw.getSheetAt(0);
//   			Map<String, Integer> mapCol = new HashMap<String, Integer>();
//   			mapCol.put("登记类型", 0);
//   			mapCol.put("权利类型", 1);			
//   			mapCol.put("总个数", 2);
//   			mapCol.put("部门", 3);
//   			//合计
//   			int DBGS =0,BJGS =0;
//   			int rownum = 3;
//   			for (Map djfzs : djfz) {
//   				HSSFRow row = (HSSFRow) sheet.createRow(rownum);
//   				try {
//   					HSSFCell cell1 = row.createCell(mapCol.get("登记类型"));
//   					cell1.setCellValue(StringHelper.formatObject(djfzs
//   							.get("DJLX")));
//   					HSSFCell cell2 = row.createCell(mapCol.get("权利类型"));
//   					cell2.setCellValue(StringHelper.formatObject(djfzs
//   							.get("QLLX")));					
//   					HSSFCell cell3 = row.createCell(mapCol.get("总个数"));
//   					cell3.setCellValue(StringHelper.formatObject(djfzs
//   							.get("ZGS")));
//   					HSSFCell cell4 = row.createCell(mapCol.get("部门"));
//   					cell4.setCellValue(StringHelper.formatObject(djfzs
//   							.get("BM")));
//   					rownum++;
//   				} catch (Exception ex) {
//   					// TODO Auto-generated catch block
//   					ex.printStackTrace();
//   				}
//
//   			}
//   			//合计	
//   			sheet.addMergedRegion(new Region(rownum, (short)0, rownum, (short)1));
//   			HSSFRow row = (HSSFRow) sheet.createRow(rownum);
//   			HSSFCell cell2 = row.createCell(0);
//   			cell2.setCellValue("合计");					
//   			HSSFCell cell3 = row.createCell(mapCol.get("总个数"));
//   			cell3.setCellValue(Integer.toString(DBGS));
//   			HSSFCell cell4 = row.createCell(mapCol.get("部门"));
//   			cell4.setCellValue(Integer.toString(BJGS));
//   			
//   			hw.write(outStream);
//   			outStream.flush();
//   			outStream.close();
//   		}
//
//   		return url;
//   	}
//       
    
    /**
     * /收费统计（URL:"sftj",Method:GET）
     * 
     */
    @RequestMapping(value = "/sftj", method = RequestMethod.GET)
    public String sftj(Model model) throws Exception {
	return "/gztj/sftj";
    }

    /**
     * 发送存量xmxx的消息
     * 
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "{qsj}/sendallmsg/{zsj}", method = RequestMethod.GET)
    public @ResponseBody ResultMessage sendAllMsg(@PathVariable("qsj") String qsj,
    		@PathVariable("zsj") String zsj, HttpServletRequest request, HttpServletResponse response)
	    throws Exception {
		ResultMessage m = new ResultMessage();
		try {
		    shareMsgTools.SendAllExistMsg(qsj,zsj);
		    m.setMsg("存量发送成功！");// 返回成功数量
		    m.setSuccess("true");
		} catch (Exception ex) {
		    m.setMsg("存量发送失败！");
		    m.setSuccess("false");
		}
	
		return m;
    }

    /**
     * 重新发送出错的消息
     * 
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/resendmsg", method = RequestMethod.GET)
    public @ResponseBody ResultMessage sendMsg(HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	ResultMessage m = new ResultMessage();
	try{
	    shareMsgTools.GetErrMsgAndSend();
	    m.setMsg("重发成功！");
	    m.setSuccess("true");
	}catch(Exception e){
	    m.setMsg("重发失败！");
	    m.setSuccess("false");
	}
	
	return m;
    }
    
    /**
     * 科室统计（URL:"/fwtj",Method:GET）
     * fwortd单元类型，房屋或者土地
     * qsj起始时间
     * zsj截止时间
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "{fwortd}/kstj/{qsj}/{zsj}", method = RequestMethod.GET)
    public @ResponseBody Message getKSTJ(@PathVariable("qsj") String tjsjqs,@PathVariable("zsj") String tjsjjz,
    		@PathVariable("fwortd") String fwortd,HttpServletRequest request,HttpServletResponse response) throws Exception {
    	String ywbh = request.getParameter("ywbm");
    	Integer tjkg = Integer.valueOf(request.getParameter("tjkg"));//统计开关
    	String outsl = request.getParameter("outsl");
    	String outdb = request.getParameter("outdb");
		Message msg = gztjService.GetKSTJ(tjsjqs,tjsjjz, ywbh,tjkg,outsl,outdb,fwortd);
		return msg;
    }
    
   
    /**
     * 抵押金额统计（URL:"/fwtj",Method:GET）
     * fwortd单元类型，房屋或者土地
     * qsj起始时间
     * zsj截止时间
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "{fwortd}/dyjetj/{qsj}/{zsj}", method = RequestMethod.GET)
    public @ResponseBody Message getDYTJ(@PathVariable("qsj") String tjsjqs,@PathVariable("zsj") String tjsjjz,
    		@PathVariable("fwortd") String fwortd,HttpServletRequest request,HttpServletResponse response) throws Exception {

		Message msg = gztjService.GetDYDJ(tjsjqs, tjsjjz, fwortd);
		return msg;
    }
    /**
     * 登记业务统计（URL:"/djywtj",Method:GET）
     * qsj起始时间
     * zsj截止时间
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/djywtj/{qsj}/{zsj}", method = RequestMethod.GET)
    public @ResponseBody Message getDJYWTJ(@PathVariable("qsj") String tjsjqs,@PathVariable("zsj") String tjsjjz,
    		HttpServletResponse response) throws Exception {
		Message msg = gztjService.GetDJYWTJ(tjsjqs, tjsjjz);
		return msg;
    }
    
    /**
     * 不动产档案统计（URL:"/bdcdatj",Method:GET）2016-12-08 luml
     * qsj起始时间
     * zsj截止时间
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/bdcdatj/{qsj}/{zsj}", method = RequestMethod.GET)
    public @ResponseBody Message getBDCDATJ(@PathVariable("qsj") String tjsjqs,@PathVariable("zsj") String tjsjjz,
    		HttpServletResponse response) throws Exception {
		Message msg = gztjService.GetBDCDATJ(tjsjqs, tjsjjz);
		return msg;
    }
    
       
    /**
     * 科室办件量统计（URL:"/ksbjltj",Method:GET）
     * qsj起始时间
     * zsj截止时间
     * ksmc科室名称
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/ksbjltj/{qsj}/{zsj}", method = RequestMethod.GET)
    public @ResponseBody Message getKSYWTJ(@PathVariable("qsj") String tjsjqs,@PathVariable("zsj") String tjsjjz,
    		HttpServletRequest request,HttpServletResponse response) throws Exception {
    	String deptid = request.getParameter("deptid");
		Message msg = gztjService.GetKSYWTJ(tjsjqs, tjsjjz, deptid);
		return msg;
    }
    /**
     * 短信统计
     * @throws Exception
     */
    @RequestMapping(value = "/dxtj/{qsj}/{zsj}/{tjlx}", method = RequestMethod.GET)
    public @ResponseBody Message getDXTJ(@PathVariable("qsj") String tjsjqs,@PathVariable("zsj") String tjsjjz,@PathVariable("tjlx") String tjlx,
    		HttpServletRequest request,HttpServletResponse response) throws Exception {
    	String deptid = request.getParameter("deptid");
    	if(deptid.equals("")){
			deptid="0";
		}
    	if(deptid.equals("0,1,2,3")){
    		deptid="0";
    	}
    	if(deptid==null){
			deptid = "0";
		}
		Message msg = gztjService.GetDXTJ(tjsjqs, tjsjjz, deptid,tjlx);
		return msg;
    }
    /**
     * 数据上报统计
     * @throws Exception
     */
    @RequestMapping(value = "/sjsbtj/{qsj}/{zsj}/{tjlx}", method = RequestMethod.GET)
    public @ResponseBody Message getSJSBTJ(@PathVariable("qsj") String tjsjqs,@PathVariable("zsj") String tjsjjz,@PathVariable("tjlx") String tjlx,
    		HttpServletRequest request,HttpServletResponse response) throws Exception {
    	String deptid = request.getParameter("deptid");
    	String dedjlx = request.getParameter("dedjlx");
    	if(deptid==null){
			deptid = "0";
		}
    	if(deptid.equals("") || deptid.equals("0,1,2,3")){
			deptid = "0";
		}
		Message msg = gztjService.GetSJSBTJ(tjsjqs, tjsjjz, deptid, dedjlx,tjlx);
		return msg;
    }
    /**
     * 
     * 统计全市总数据
     */
    @RequestMapping(value = "/tjall", method = RequestMethod.GET)
    public @ResponseBody Message getTJALL(HttpServletRequest request,HttpServletResponse response) throws Exception {

		Message msg = gztjService.GetTJALL();
		return msg;
    }
    /**
     * 登记中心办件量统计（URL:"/djzxbjltj",Method:GET）
     * qsj起始时间
     * zsj截止时间
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/djzxbjltj/{qsj}/{zsj}", method = RequestMethod.GET)
    public @ResponseBody Message getDJZXBJLTJ(@PathVariable("qsj") String tjsjqs,@PathVariable("zsj") String tjsjjz,
    		HttpServletResponse response) throws Exception {
		Message msg = gztjService.GetDJZXYWTJ(tjsjqs, tjsjjz);
		return msg;
    }
    /**
     * 
     * @作者 胡加红
     * @创建时间 2015年12月5日下午4:02:26
     * @param request
     * @param response
     * @return
     */
    @SuppressWarnings({ "rawtypes" })
	@RequestMapping(value = "/getdept", method = RequestMethod.GET)
	public @ResponseBody List<Map> getDept(HttpServletRequest request, HttpServletResponse response) {
		List<Map> dept = gztjService.GetDeptInfo();
		return dept;
	}
    
    @SuppressWarnings({ "rawtypes" })
	@RequestMapping(value = "/getxzqh1", method = RequestMethod.GET)
	public @ResponseBody List<Tree> getXzqh(HttpServletRequest request, HttpServletResponse response) {
		List<Tree> dept = gztjService.GetXzqhInfo();
		return dept;
	}
    @SuppressWarnings({ "rawtypes" })
   	@RequestMapping(value = "/getdjlx", method = RequestMethod.GET)
   	public @ResponseBody List<Tree> getDjlx(HttpServletRequest request, HttpServletResponse response) {
   		List<Tree> dept = gztjService.GetDjlxInfo();
   		return dept;
   	}
    /**
     * 登记热点统计（URL:"/hot",Method:GET）
     * qsj起始时间
     * zsj截止时间
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/hot/{slsjq}/{slsjz}/{statistics_type}/", method = RequestMethod.GET)
    public @ResponseBody Message getDJZXBJLTJ(@PathVariable("slsjq") String slsjq,@PathVariable("slsjz") String slsjz,
    		@PathVariable("statistics_type") String statistics_type,
    		HttpServletResponse response) throws Exception {
		Message msg = gztjService.GetHotTJ(slsjq, slsjz,statistics_type);
		return msg;
    }
    
    /**
     * 登记发证量统计（URL:"/djfztj",Method:GET）
     * qsj起始时间
     * zsj截止时间
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/djfztj/{slsjq}/{slsjz}/{statistics_type}/", method = RequestMethod.GET)
    public @ResponseBody Message getDJFZTJ(@PathVariable("slsjq") String slsjq,@PathVariable("slsjz") String slsjz,
    		@PathVariable("statistics_type") String statistics_type,
    		HttpServletResponse response) throws Exception {
		Message msg = gztjService.GetDJFZTJ(slsjq, slsjz,statistics_type);
		return msg;
    }
    
    /**
     * 不动产权证移交表（URL:"/fwtj",Method:GET）
     * fwortd单元类型，证书或者证明
     * qsj起始时间
     * zsj截止时间
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/dyqzyjtj", method = RequestMethod.GET)
    public @ResponseBody Message getQzTJ( HttpServletRequest request,HttpServletResponse response) throws Exception {
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
    		mapCondition.put("id_sjq", request.getParameter("id_sjq"));
    		mapCondition.put("id_sjz", request.getParameter("id_sjz"));
    		mapCondition.put("id_xhq", request.getParameter("id_xhq"));
    		mapCondition.put("id_xhz", request.getParameter("id_xhz"));
    		mapCondition.put("id_tjlx", request.getParameter("id_tjlx"));
    		
		Message msg = gztjService.GetQZDJ(mapCondition, page, rows);
		return msg;
    }      
  
	@SuppressWarnings({ "unused", "rawtypes" })
	@RequestMapping(value = "/gzqzyjinfodownload", method = RequestMethod.GET)
	public @ResponseBody String DiygzInfoDownload(HttpServletRequest request, HttpServletResponse response) throws Exception{
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
		mapCondition.put("id_sjq", request.getParameter("id_sjq"));
		mapCondition.put("id_sjz", request.getParameter("id_sjz"));
		mapCondition.put("id_xhq", request.getParameter("id_xhq"));
		mapCondition.put("id_xhz", request.getParameter("id_xhz"));
		mapCondition.put("id_tjlx", request.getParameter("id_tjlx"));
		List<String> alist= new ArrayList<String>();
		try {
			String checklist = new String(request.getParameter("check").getBytes("iso8859-1"), "utf-8");
			String[] list = checklist.trim().split(",");
			alist =  Arrays.asList(list);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}				
		Message msg = gztjService.GetQZDJ(mapCondition, page, rows);;
		
		String basePath = request.getRealPath("/") + "\\resources\\PDF";
		String outpath = "";
		String url = "";
		String tmpFullName = "";
		FileOutputStream outstream = null;
		if(msg.getRows() != null && msg.getRows().size() > 0){
			outpath = basePath + "\\tmp\\qzyjInfo.xls";
			url = request.getContextPath() + "\\resources\\PDF\\tmp\\qzyjInfo.xls";
		    outstream = new FileOutputStream(outpath); 
		    tmpFullName = request.getRealPath("/WEB-INF/jsp/wjmb/qzyjInfo.xls");
		    InputStream input = new FileInputStream(tmpFullName);
			HSSFWorkbook  wb = null;// 定义一个新的工作簿
			wb = new HSSFWorkbook(input);
			Sheet sheet = wb.getSheetAt(0);
			Map<String,Integer> MapCol = new HashMap<String,Integer>();
			MapCol.put("序号", 0);
			MapCol.put("权证类型", 1);
			MapCol.put("业务流水号", 2);
			MapCol.put("证书编号", 3);
			MapCol.put("不动产权证号", 4);
			MapCol.put("权利人名称", 5);
			MapCol.put("义务人名称", 6);
            int rownum = 1;
            List<Map> djdys = (List<Map>)msg.getRows();
			for(Map djdy:djdys){
				 if(!alist.contains(djdy.get("ROWNUM").toString()))//没有选择的就不导出
					 continue;
		  		 HSSFRow row = (HSSFRow)sheet.createRow(rownum);
		  		 try{
			         HSSFCell Cell0 = row.createCell(MapCol.get("序号"));
			         Cell0.setCellValue(rownum);
			         HSSFCell Cell1 = row.createCell(MapCol.get("权证类型"));
			         Cell1.setCellValue(StringHelper.formatObject(djdy.get("QZLX")));
			         HSSFCell Cell7 = row.createCell(MapCol.get("业务流水号"));
			         Cell7.setCellValue(StringHelper.formatObject(djdy.get("YWLSH")));
			         HSSFCell Cell2 = row.createCell(MapCol.get("证书编号"));
			         Cell2.setCellValue(StringHelper.formatObject(djdy.get("ZSBH")));
			         HSSFCell Cell3 = row.createCell(MapCol.get("不动产权证号"));
			         Cell3.setCellValue(StringHelper.formatObject(djdy.get("BDCQZH")));
			         HSSFCell Cell4 = row.createCell(MapCol.get("权利人名称"));
			         Cell4.setCellValue(StringHelper.formatObject(djdy.get("QLRMC")));
			         HSSFCell Cell5 = row.createCell(MapCol.get("义务人名称"));
			         Cell5.setCellValue(StringHelper.formatObject(djdy.get("YWRMC")));
			         rownum ++;
		  		 }
		  		 catch(Exception ex){
		  			 
		  		 }
			  }	  		
			 wb.write(outstream); 
			 outstream.flush(); 
			 outstream.close();
		 }

        return url;
	}
	
	/**
	 * 
	 * @param slsjq
	 * @param slsjz
	 * @author 邓增健
	 * @param response
	 * @return
	 * @throws Exception
	 */
    @RequestMapping(value = "/bdcdjzb/{sjq}/{sjz}/", method = RequestMethod.GET)
    public @ResponseBody Message getBDCDJZB(@PathVariable("sjq") String sjq,@PathVariable("sjz") String sjz,
    		
    		HttpServletResponse response) throws Exception {
		Message msg = gztjService.GetBdcdjZB(sjq, sjz);
		return msg;
    }
    @SuppressWarnings({ "rawtypes", "unchecked", "deprecation" })
	@RequestMapping(value = "/bdcdjzbinfodownload", method = RequestMethod.GET)
	public @ResponseBody String BdcdjzbInfoDownload(HttpServletRequest request, HttpServletResponse response) throws Exception{
				
		Map<String, String> mapCondition = new HashMap<String, String>();
		mapCondition.put("id_sjq", request.getParameter("id_slsjq"));
		mapCondition.put("id_sjz", request.getParameter("id_slsjz"));
		
		
		
		Message m = gztjService.GetBdcdjZB(mapCondition.get("id_sjq"),mapCondition.get("id_sjz"));
		List<Map> djdys = null;
		if (m !=null && m.getRows()!=null){
			djdys=(List<Map>)m.getRows();
		}
		String basePath = request.getRealPath("/") + "\\resources\\PDF";
		String outpath = "";
		String url = "";
		String tmpFullName = "";
		FileOutputStream outstream = null;
		if(djdys != null && djdys.size() > 0){
			outpath = basePath + "\\tmp\\bdcdjzb.xls";
			url = request.getContextPath() + "\\resources\\PDF\\tmp\\bdcdjzb.xls";
		    outstream = new FileOutputStream(outpath); 
		    tmpFullName = request.getRealPath("/WEB-INF/jsp/wjmb/bdcdjzb.xls");
		    InputStream input = new FileInputStream(tmpFullName);
			HSSFWorkbook  wb = null;// 定义一个新的工作簿
			wb = new HSSFWorkbook(input);
			Sheet sheet = wb.getSheetAt(0);
			Map<String,Integer> MapCol = new HashMap<String,Integer>();
			MapCol.put("序号", 0);
			MapCol.put("市、县", 1);
			MapCol.put("受理量", 2);
			MapCol.put("其中抵押登记受理量", 3);
			MapCol.put("办结量", 4);
			MapCol.put("其中抵押登记办结量", 5);
			MapCol.put("累计受理量", 6);
			MapCol.put("累计其中抵押登记受理量", 7);
			MapCol.put("累计办结量", 8);
			MapCol.put("累计其中抵押登记办结量", 9);
			MapCol.put("不动产权证书", 10);
			MapCol.put("不动产权证明", 11);
			MapCol.put("首次登记", 12);
			MapCol.put("转移、变更登记", 13);
			MapCol.put("抵押登记", 14);
			MapCol.put("其他登记", 15);
			MapCol.put("招投标时间", 16);
			MapCol.put("土地", 17);
			MapCol.put("房产", 18);
			MapCol.put("林权", 19);
			MapCol.put("备注", 20);
            int rownum = 4;
			for(Map djdy:djdys){
				 
		  		 HSSFRow row = (HSSFRow)sheet.createRow(rownum);
		  		 try{
			         HSSFCell Cell0 = row.createCell(MapCol.get("序号"));
			         Cell0.setCellValue(rownum-3);
			         HSSFCell Cell1 = row.createCell(MapCol.get("市、县"));
			         Cell1.setCellValue(StringHelper.formatObject(djdy.get("QHMC")));
			         HSSFCell Cell2 = row.createCell(MapCol.get("受理量"));
			         Cell2.setCellValue(StringHelper.formatObject(djdy.get("SLL")));
			         HSSFCell Cell3 = row.createCell(MapCol.get("其中抵押登记受理量"));
			         Cell3.setCellValue(StringHelper.formatObject(djdy.get("DYSLL")));
			         HSSFCell Cell4 = row.createCell(MapCol.get("办结量"));
			         Cell4.setCellValue(StringHelper.formatObject(djdy.get("BJL")));
			         HSSFCell Cell5 = row.createCell(MapCol.get("其中抵押登记办结量"));
			         Cell5.setCellValue(StringHelper.formatObject(djdy.get("DYBJL")));
			         HSSFCell Cell6 = row.createCell(MapCol.get("累计受理量"));
			         Cell6.setCellValue(StringHelper.formatObject(djdy.get("LJSLL")));
			         
			         HSSFCell Cell7 = row.createCell(MapCol.get("累计其中抵押登记受理量"));
			         Cell7.setCellValue(StringHelper.formatObject(djdy.get("LJDYSLL")));
			         HSSFCell Cell8 = row.createCell(MapCol.get("累计办结量"));
			         Cell8.setCellValue(StringHelper.formatObject(djdy.get("LJBJL")));
			         HSSFCell Cell9 = row.createCell(MapCol.get("累计其中抵押登记办结量"));
			         Cell9.setCellValue(StringHelper.formatObject(djdy.get("LJDYBJL")));
			         HSSFCell Cell10 = row.createCell(MapCol.get("不动产权证书"));
			         Cell10.setCellValue(StringHelper.formatObject(djdy.get("LJZSFZL")));
			         HSSFCell Cell11 = row.createCell(MapCol.get("不动产权证明"));
			         Cell11.setCellValue(StringHelper.formatObject(djdy.get("LJZMFZL")));
			         HSSFCell Cell12 = row.createCell(MapCol.get("首次登记"));
			         Cell12.setCellValue(StringHelper.formatObject(djdy.get("SCTS")));
			         HSSFCell Cell13 = row.createCell(MapCol.get("转移、变更登记"));
			         Cell13.setCellValue(StringHelper.formatObject(djdy.get("ZYBGTS")));
			         HSSFCell Cell14 = row.createCell(MapCol.get("抵押登记"));
			         Cell14.setCellValue(StringHelper.formatObject(djdy.get("DYTS")));
			         HSSFCell Cell15 = row.createCell(MapCol.get("其他登记"));
			         Cell15.setCellValue(StringHelper.formatObject(djdy.get("QTTS")));
			         HSSFCell Cell16 = row.createCell(MapCol.get("招标时间"));
			         Cell16.setCellValue(StringHelper.formatObject(djdy.get("ZBSJ")));
			         HSSFCell Cell17 = row.createCell(MapCol.get("土地"));
			         Cell17.setCellValue(StringHelper.formatObject(djdy.get("TD")));
			         HSSFCell Cell18 = row.createCell(MapCol.get("房产"));
			         Cell18.setCellValue(StringHelper.formatObject(djdy.get("FC")));
			         HSSFCell Cell19 = row.createCell(MapCol.get("林权"));
			         Cell19.setCellValue(StringHelper.formatObject(djdy.get("LQ")));
			         HSSFCell Cell20 = row.createCell(MapCol.get("备注"));
			         Cell20.setCellValue(StringHelper.formatObject(djdy.get("BZ")));
			         
			         rownum ++;
		  		 }
		  		 catch(Exception ex){
		  			 
		  		 }
			  }	  		
			 wb.write(outstream); 
			 outstream.flush(); 
			 outstream.close();
		 }

        return url;
	}
    
    @RequestMapping(value = "/efficiency/{sjq}/{sjz}/{user}/", method = RequestMethod.GET)
    public @ResponseBody Message Getefficiency(@PathVariable("sjq") String sjq,@PathVariable("sjz") String sjz,
    		@PathVariable("user") String user,
    		HttpServletResponse response) throws Exception {
		Message msg = gztjService.Getefficiency(sjq, sjz,user);
		return msg;
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
   	@RequestMapping(value = "/efficiencyinfodownload", method = RequestMethod.GET)
   	public @ResponseBody String EfficiencyinfoDownload(HttpServletRequest request, HttpServletResponse response) throws Exception{
   				
   		Map<String, String> mapCondition = new HashMap<String, String>();
   		mapCondition.put("id_sjq", request.getParameter("id_slsjq"));
   		mapCondition.put("id_sjz", request.getParameter("id_slsjz"));
   		mapCondition.put("id_user", request.getParameter("usertree"));
   		   		
   		Message m = gztjService.Getefficiency(mapCondition.get("id_sjq"),mapCondition.get("id_sjz"),mapCondition.get("id_user"));
   		List<Map> djdys = null;
   		if (m !=null && m.getRows()!=null){
   			djdys=(List<Map>)m.getRows();
   		}
   		String basePath = request.getRealPath("/") + "\\resources\\PDF";
   		String outpath = "";
   		String url = "";
   		String tmpFullName = "";
   		FileOutputStream outstream = null;
   		if(djdys != null && djdys.size() > 0){
   			outpath = basePath + "\\tmp\\efficiency.xls";
   			url = request.getContextPath() + "\\resources\\PDF\\tmp\\efficiency.xls";
   		    outstream = new FileOutputStream(outpath); 
   		    tmpFullName = request.getRealPath("/WEB-INF/jsp/wjmb/efficiency.xls");
   		    InputStream input = new FileInputStream(tmpFullName);
   			HSSFWorkbook  wb = null;// 定义一个新的工作簿
   			wb = new HSSFWorkbook(input);
   			Sheet sheet = wb.getSheetAt(0);
   			Map<String,Integer> MapCol = new HashMap<String,Integer>();
   			MapCol.put("姓名", 0);
   			MapCol.put("办件量", 1);
   			MapCol.put("在办件量", 2);
   			MapCol.put("已办件量", 3);
   			MapCol.put("超期件", 4);
   			MapCol.put("在办超期件", 5);
   			MapCol.put("未超期件", 6);
   			
               int rownum = 2;
   			for(Map djdy:djdys){
   				 
   		  		 HSSFRow row = (HSSFRow)sheet.createRow(rownum);
   		  		 try{
   			         HSSFCell Cell0 = row.createCell(MapCol.get("姓名"));
   			         Cell0.setCellValue(StringHelper.formatObject(djdy.get("USERNAME")));
   			         HSSFCell Cell1 = row.createCell(MapCol.get("办件量"));
   			         Cell1.setCellValue(StringHelper.formatObject(djdy.get("BJL")));
   			         HSSFCell Cell2 = row.createCell(MapCol.get("在办件量"));
   			         Cell2.setCellValue(StringHelper.formatObject(djdy.get("ZBJS")));
   			         HSSFCell Cell3 = row.createCell(MapCol.get("已办件量"));
   			         Cell3.setCellValue(StringHelper.formatObject(djdy.get("YBJS")));
   			         HSSFCell Cell4 = row.createCell(MapCol.get("超期件"));
			         Cell4.setCellValue(StringHelper.formatObject(djdy.get("CQJ")));
   			         HSSFCell Cell5 = row.createCell(MapCol.get("在办超期件"));
   			         Cell5.setCellValue(StringHelper.formatObject(djdy.get("ZBCQJ")));
   			         HSSFCell Cell6 = row.createCell(MapCol.get("未超期件"));
   			         Cell6.setCellValue(StringHelper.formatObject(djdy.get("WCQJ")));
   			         
   			         rownum ++;
   		  		 }
   		  		 catch(Exception ex){
   		  			 
   		  		 }
   			  }	  		
   			 wb.write(outstream); 
   			 outstream.flush(); 
   			 outstream.close();
   		 }

           return url;
   	}
    /**
     * 抵押统计导出
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @SuppressWarnings({ "unused", "rawtypes", "unchecked" })
   	@RequestMapping(value = "{fwortd}/dydjinfodownload/{qsj}/{zsj}", method = RequestMethod.GET)
   	public @ResponseBody String DYDJinfoDownload(
   			@PathVariable("qsj") String tjsjqs,@PathVariable("zsj") String tjsjjz,
    		@PathVariable("fwortd") String fwortd,
   			HttpServletRequest request, HttpServletResponse response) throws Exception{
   				
//   		Map<String, String> mapCondition = new HashMap<String, String>();
//   		mapCondition.put("id_sjq", request.getParameter("id_sjq"));
//   		mapCondition.put("id_sjz", request.getParameter("id_sjz"));
//   		try {
//			String id_tjlx = new String(request.getParameter("id_dyjetj").getBytes("iso8859-1"), "utf-8");
//			mapCondition.put("id_tjlx",id_tjlx);
//		} catch (UnsupportedEncodingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}	
   		   		
   		Message m = gztjService.GetDYDJ(tjsjqs, tjsjjz, fwortd);
   		List<Map> djdys = null;
   		if (m !=null && m.getRows()!=null){
   			djdys=(List<Map>)m.getRows();
   		}
   		String basePath = request.getRealPath("/") + "\\resources\\PDF";
   		String outpath = "";
   		String url = "";
   		String tmpFullName = "";
   		FileOutputStream outstream = null;
   		if(djdys != null && djdys.size() > 0){
   			outpath = basePath + "\\tmp\\dydj.xls";
   			url = request.getContextPath() + "\\resources\\PDF\\tmp\\dydj.xls";
   		    outstream = new FileOutputStream(outpath); 
   		    tmpFullName = request.getRealPath("/WEB-INF/jsp/wjmb/dydj.xls");
   		    InputStream input = new FileInputStream(tmpFullName);
   			HSSFWorkbook  wb = null;// 定义一个新的工作簿
   			wb = new HSSFWorkbook(input);
   			Sheet sheet = wb.getSheetAt(0);
   			Map<String,Integer> MapCol = new HashMap<String,Integer>();
   			MapCol.put("权利人类型", 0);
   			MapCol.put("抵押业务数(件)", 1);
   			MapCol.put("被担保债权数额(万元)", 2);
   			MapCol.put("最高债权数额(万元)", 3);
   			
               int rownum = 2;
   			for(Map djdy:djdys){
   				 
   		  		 HSSFRow row = (HSSFRow)sheet.createRow(rownum);
   		  		 try{
   			         HSSFCell Cell0 = row.createCell(MapCol.get("权利人类型"));
   			         Cell0.setCellValue(StringHelper.formatObject(djdy.get("QLRLX")));
   			         HSSFCell Cell1 = row.createCell(MapCol.get("抵押业务数(件)"));
   			         Cell1.setCellValue(StringHelper.formatObject(djdy.get("GS")));
   			         HSSFCell Cell2 = row.createCell(MapCol.get("被担保债权数额(万元)"));
   			         Cell2.setCellValue(StringHelper.formatObject(djdy.get("BDBE")));
   			         HSSFCell Cell3 = row.createCell(MapCol.get("最高债权数额(万元)"));
   			         Cell3.setCellValue(StringHelper.formatObject(djdy.get("ZGE")));
   			         rownum ++;
   		  		 }
   		  		 catch(Exception ex){
   		  			 
   		  		 }
   			  }	  		
   			 wb.write(outstream); 
   			 outstream.flush(); 
   			 outstream.close();
   		 }

           return url;
   	}
    /**
	 * 发证面积统计 李名祥 二〇一六年六月四日 21:19:52
	 * 
	 * @param sjq
	 * @param sjz
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/fzmjtj/{sjq}/{sjz}/{tjmc}", method = RequestMethod.GET)
	public @ResponseBody Message getFZMJTJ(@PathVariable("sjq") String sjq,
			@PathVariable("sjz") String sjz, @PathVariable("tjmc") String tjmc,
			HttpServletResponse response) throws Exception {
		Message msg = gztjService.GetFZMJTJ(sjq, sjz, tjmc);
		return msg;
	}
	
	@SuppressWarnings({ "unused", "rawtypes", "unchecked" })
	@RequestMapping(value = "/fzmjtjinfodownload", method = RequestMethod.GET)
	public @ResponseBody String FzmjtjInfoDownload(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		Map<String, String> mapCondition = new HashMap<String, String>();
		mapCondition.put("id_sjq", request.getParameter("id_sjq"));
		mapCondition.put("id_sjz", request.getParameter("id_sjz"));

		try {
			String tjmc = new String(request.getParameter("id_tjlx").getBytes(
					"ISO-8859-1"), "utf-8");
			mapCondition.put("id_tjmc", tjmc);
			// String tjmj = new
			// String(request.getParameter("id_tjlx").getBytes(
			// "iso8859-1"));
			// mapCondition.put("id_tjmj", tjmj);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Message m = gztjService.GetFZMJTJ(mapCondition.get("id_sjq"),
				mapCondition.get("id_sjz"), mapCondition.get("id_tjmc"));
		List<Map> djdys = null;
		if (m != null && m.getRows() != null) {
			djdys = (List<Map>) m.getRows();
		}
		String basePath = request.getRealPath("/") + "\\resources\\PDF";
		String outpath = "";
		String url = "";
		String tmpFullName = "";
		FileOutputStream outstream = null;
		if (djdys != null && djdys.size() > 0) {
			outpath = basePath + "\\tmp\\fzmjtj.xls";
			url = request.getContextPath()
					+ "\\resources\\PDF\\tmp\\fzmjtj.xls";
			outstream = new FileOutputStream(outpath);
			tmpFullName = request.getRealPath("/WEB-INF/jsp/wjmb/fzmjtj.xls");
			InputStream input = new FileInputStream(tmpFullName);
			HSSFWorkbook wb = null;// 定义一个新的工作簿
			wb = new HSSFWorkbook(input);
			Sheet sheet = wb.getSheetAt(0);
			Map<String, Integer> MapCol = new HashMap<String, Integer>();
			MapCol.put("序号", 0);
			MapCol.put("查询名称", 1);
			MapCol.put("总计发证面积", 2);

			int rownum = 2;
			for (Map djdy : djdys) {

				HSSFRow row = (HSSFRow) sheet.createRow(rownum);
				try {
					HSSFCell Cell0 = row.createCell(MapCol.get("序号"));
					Cell0.setCellValue(rownum - 1);
					HSSFCell Cell1 = row.createCell(MapCol.get("查询名称"));
					Cell1.setCellValue(StringHelper.formatObject(djdy
							.get("TJMC")));
					HSSFCell Cell2 = row.createCell(MapCol.get("总计发证面积"));
					Cell2.setCellValue(StringHelper.formatObject(djdy
							.get("SCJZMJ")));

					rownum++;
				} catch (Exception ex) {

				}
			}
			wb.write(outstream);
			outstream.flush();
			outstream.close();
		}

		return url;
	}
	/**
	 * 导出登记业务统计 李名祥 二〇一六年六月二十一日 20:58:30
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({"rawtypes", "unchecked" })
	@RequestMapping(value = "/djywtjinfodownload", method = RequestMethod.GET)
	public @ResponseBody String DjywtjInfoDownload(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Map<String, String> mapcondition = new HashMap<String, String>();
		mapcondition.put("id_sjq", request.getParameter("id_sjq"));
		mapcondition.put("id_sjz", request.getParameter("id_sjz"));

		Message m = gztjService.GetDJYWTJ(mapcondition.get("id_sjq"),
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
			tmpFullName = request.getRealPath("/WEB-INF/jsp/wjmb/djywtj.xls");
			InputStream input = new FileInputStream(tmpFullName);
			HSSFWorkbook hw = null;
			hw = new HSSFWorkbook(input);
			HSSFSheet sheet = hw.getSheetAt(0);
			Map<String, Integer> mapCol = new HashMap<String, Integer>();
			mapCol.put("登记类型", 0);
			mapCol.put("受理部门", 1);
			mapCol.put("权利类型", 2);			
			mapCol.put("登簿", 3);
			mapCol.put("办结", 4);
			mapCol.put("权证", 5);
			mapCol.put("证明", 6);
			mapCol.put("权证1", 7);
			mapCol.put("证明2", 8);
			//合计
			int DBGS =0,BJGS =0,SZZSGS =0,SZZMGS =0,FZZSGS =0,FZZMGS =0;
			int rownum = 3;
			for (Map djfzs : djfz) {
				HSSFRow row = (HSSFRow) sheet.createRow(rownum);
				
				DBGS += Integer.parseInt(StringHelper.formatObject(djfzs.get("DBGS")));
				BJGS += Integer.parseInt(StringHelper.formatObject(djfzs.get("BJGS")));
				SZZSGS += Integer.parseInt(StringHelper.formatObject(djfzs.get("SZZSGS")));
				SZZMGS += Integer.parseInt(StringHelper.formatObject(djfzs.get("SZZMGS")));
				FZZSGS += Integer.parseInt(StringHelper.formatObject(djfzs.get("FZZSGS")));
				FZZMGS += Integer.parseInt(StringHelper.formatObject(djfzs.get("FZZMGS")));
				//content
				try {
					HSSFCell cell1 = row.createCell(mapCol.get("登记类型"));
					cell1.setCellValue(StringHelper.formatObject(djfzs
							.get("DJLX")));
					HSSFCell cell2 = row.createCell(mapCol.get("受理部门"));
					cell2.setCellValue(StringHelper.formatObject(djfzs
							.get("DEPARTMENTNAME")));					
					HSSFCell cell3 = row.createCell(mapCol.get("权利类型"));
					cell3.setCellValue(StringHelper.formatObject(djfzs
							.get("QLLX")));					
					HSSFCell cell4 = row.createCell(mapCol.get("登簿"));
					cell4.setCellValue(StringHelper.formatObject(djfzs
							.get("DBGS")));
					HSSFCell cell5 = row.createCell(mapCol.get("办结"));
					cell5.setCellValue(StringHelper.formatObject(djfzs
							.get("BJGS")));
					HSSFCell cell6 = row.createCell(mapCol.get("权证"));
					cell6.setCellValue(StringHelper.formatObject(djfzs
							.get("SZZSGS")));
					HSSFCell cell7 = row.createCell(mapCol.get("证明"));
					cell7.setCellValue(StringHelper.formatObject(djfzs
							.get("SZZMGS")));
					HSSFCell cell8 = row.createCell(mapCol.get("权证1"));
					cell8.setCellValue(StringHelper.formatObject(djfzs
							.get("FZZSGS")));
					HSSFCell cell9 = row.createCell(mapCol.get("证明2"));
					cell9.setCellValue(StringHelper.formatObject(djfzs
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
			HSSFCell cell3 = row.createCell(mapCol.get("登簿"));
			cell3.setCellValue(Integer.toString(DBGS));
			HSSFCell cell4 = row.createCell(mapCol.get("办结"));
			cell4.setCellValue(Integer.toString(BJGS));
			HSSFCell cell5 = row.createCell(mapCol.get("权证"));
			cell5.setCellValue(Integer.toString(SZZSGS));
			HSSFCell cell6 = row.createCell(mapCol.get("证明"));
			cell6.setCellValue(Integer.toString(SZZMGS));
			HSSFCell cell7 = row.createCell(mapCol.get("权证1"));
			cell7.setCellValue(Integer.toString(FZZSGS));
			HSSFCell cell8 = row.createCell(mapCol.get("证明2"));
			cell8.setCellValue(Integer.toString(FZZMGS));
			
			hw.write(outStream);
			outStream.flush();
			outStream.close();
		}

		return url;
	}
	
	 @SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/qjcxDownload", method = RequestMethod.GET)
	public @ResponseBody String QJCXDownload( HttpServletRequest request, HttpServletResponse response) throws Exception{
    	String keyString = new String(request.getParameter("value").getBytes("iso8859-1"), "utf-8");
		String statuString = request.getParameter("status");
		String startString = request.getParameter("start");
		String endString = request.getParameter("end");
		int pageindex = Integer.parseInt(request.getParameter("currpage"));
		int pagesize = Integer.parseInt(request.getParameter("pagesize"));
		String actdefname = new String(request.getParameter("actdefname").getBytes("iso8859-1"), "utf-8");
		String prodefname=null;
		try {
			 prodefname = new String(request.getParameter("prodefname").getBytes("iso8859-1"), "utf-8");
		
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	

		String staffanme= new String(request.getParameter("staffanme").getBytes("iso8859-1"), "utf-8");
		String sqr= new String(request.getParameter("sqr").getBytes("iso8859-1"), "utf-8");
		String urgency=request.getParameter("urgency");//紧急程度
		String outtime=request.getParameter("outtime");//超期
		String passback=request.getParameter("passback");//驳回
		
		com.supermap.wisdombusiness.workflow.util.Message m = spService.getAllProjectInfo(keyString,statuString,  startString,  endString, pageindex, pagesize, actdefname, prodefname, staffanme,
				sqr, urgency, outtime, passback);
		List<Map> djdys = null;
		if (m !=null && m.getRows()!=null){
			djdys=(List<Map>)m.getRows();
		}
		String basePath = request.getRealPath("/") + "\\resources\\PDF";
		String outpath = "";
		String url = "";
		String tmpFullName = "";
		FileOutputStream outstream = null;
		if(djdys != null && djdys.size() > 0){
			outpath = basePath + "\\tmp\\qjcx.xls";
			url = request.getContextPath() + "\\resources\\PDF\\tmp\\qjcx.xls";
		    outstream = new FileOutputStream(outpath); 
		    tmpFullName = request.getRealPath("/WEB-INF/jsp/wjmb/qjcx.xls");
		    InputStream input = new FileInputStream(tmpFullName);
			HSSFWorkbook  wb = null;// 定义一个新的工作簿
			wb = new HSSFWorkbook(input);
			Sheet sheet = wb.getSheetAt(0);
			Map<String,Integer> MapCol = new HashMap<String,Integer>();
			MapCol.put("序号", 0);
			MapCol.put("登记类型", 1);
			MapCol.put("流程类型", 2);
			MapCol.put("受理编号",3);
			MapCol.put("登记时间",4);
			MapCol.put("办理人员",5);
			MapCol.put("登薄人员", 6);
			MapCol.put("不动产单元号",7);
			MapCol.put("受理时间",8);
			MapCol.put("备注", 9);
            int rownum = 2;
			for(Map djdy:djdys){
		  		 HSSFRow row = (HSSFRow)sheet.createRow(rownum);
		  	
		  		 try{
		  			
			         HSSFCell Cell0 = row.createCell(MapCol.get("序号"));
			         Cell0.setCellValue(rownum-1);
			         HSSFCell Cell1 = row.createCell(MapCol.get("登记类型"));
			         String prodef_name = StringHelper.formatObject((String) djdy.get("PRODEF_NAME"));
			         String djlx = "",proName="";
			         if(!prodef_name.equals("") &&prodef_name!=null){
			        	 djlx=prodef_name.substring(0,4);
			        	 proName=prodef_name.substring(5);
			         }
			         Cell1.setCellValue(djlx);
			         HSSFCell Cell2 = row.createCell(MapCol.get("流程类型"));
			         Cell2.setCellValue(proName);
			         HSSFCell Cell3 = row.createCell(MapCol.get("受理编号"));
			         Cell3.setCellValue(StringHelper.formatObject(djdy.get("PROLSH")));
			         HSSFCell Cell4 = row.createCell(MapCol.get("登记时间"));
			         String DJSJ = StringHelper.formatObject(djdy.get("DJSJ"));
			         String djsj="";
			         if(!DJSJ.equals("")&&DJSJ != null){
			        	 djsj = DJSJ.substring(0, 10);
			        	 
			         }
			         Cell4.setCellValue(djsj);
			         HSSFCell Cell5 = row.createCell(MapCol.get("办理人员"));
			         Cell5.setCellValue(StringHelper.formatObject(djdy.get("STAFF_NAME")));
			         HSSFCell Cell6= row.createCell(MapCol.get("登薄人员"));
			         Cell6.setCellValue(StringHelper.formatObject(djdy.get("DBR")));
			         HSSFCell Cell7= row.createCell(MapCol.get("不动产单元号"));
			         Cell7.setCellValue(StringHelper.formatObject(djdy.get("BDCDYH")));
			         HSSFCell Cell8= row.createCell(MapCol.get("受理时间"));
			         Cell8.setCellValue(StringHelper.formatObject(djdy.get("SLSJ")));
			         HSSFCell Cell9= row.createCell(MapCol.get("备注"));
			         Cell9.setCellValue(StringHelper.formatObject(djdy.get("BZ")));
			       
			         rownum++ ;
			       
		  		 }
		  		 catch(Exception ex){
		  			 
		  		 }
			  }	  		
			 wb.write(outstream); 
			 outstream.flush(); 
			 outstream.close();
		 }

        return url;
	}
	@RequestMapping(value="/yskcxltj/{tjsjks}/{tjsjjs}",method=RequestMethod.GET)
	public @ResponseBody Message getKSCXTJ(@PathVariable("tjsjks") String tjsjks,@PathVariable("tjsjjs") String tjsjjs){
		Message msg = gztjService.getKSCXTJ(tjsjks, tjsjjs);
		return msg;
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/kscxtjinfodownload", method = RequestMethod.GET)
	public @ResponseBody String KscxtjInfoDownload(HttpServletRequest request, HttpServletResponse response) throws Exception{
				
		Map<String, String> mapCondition = new HashMap<String, String>();
		mapCondition.put("id_sjq", request.getParameter("id_sjq"));
		mapCondition.put("id_sjz", request.getParameter("id_sjz"));
		Message m = gztjService.getKSCXTJ(mapCondition.get("id_sjq"),mapCondition.get("id_sjz"));
		List<Map> djdys = null;
		if (m !=null && m.getRows()!=null){
			djdys=(List<Map>)m.getRows();
		}
		String basePath = request.getRealPath("/") + "\\resources\\PDF";
		String outpath = "";
		String url = "";
		String tmpFullName = "";
		FileOutputStream outstream = null;
		if(djdys != null && djdys.size() > 0){
			outpath = basePath + "\\tmp\\yskcxltj.xls";
			url = request.getContextPath() + "\\resources\\PDF\\tmp\\yskcxltj.xls";
		    outstream = new FileOutputStream(outpath); 
		    tmpFullName = request.getRealPath("/WEB-INF/jsp/wjmb/yskcxltj.xls");
		    InputStream input = new FileInputStream(tmpFullName);
			HSSFWorkbook  wb = null;// 定义一个新的工作簿
			wb = new HSSFWorkbook(input);
			Sheet sheet = wb.getSheetAt(0);
			Map<String,Integer> MapCol = new HashMap<String,Integer>();
			MapCol.put("查询类型", 0);
			MapCol.put("受理量", 1);
            int rownum = 2;
			for(Map djdy:djdys){
				 
		  		 HSSFRow row = (HSSFRow)sheet.createRow(rownum);
		  		 try{
			         HSSFCell Cell0 = row.createCell(MapCol.get("查询类型"));
			         Cell0.setCellValue(StringHelper.formatObject(djdy.get("CX_TYPE")));
			         HSSFCell Cell1 = row.createCell(MapCol.get("受理量"));
			         Cell1.setCellValue(StringHelper.formatObject(djdy.get("GS")));
			         rownum ++;
		  		 }
		  		 catch(Exception ex){
		  			 
		  		 }
			  }	  		
			 wb.write(outstream); 
			 outstream.flush(); 
			 outstream.close();
		 }

        return url;
	}
	
	@SuppressWarnings({ "unused", "rawtypes", "unchecked" })
	@RequestMapping(value = "/djfztjinfodownload", method = RequestMethod.GET)
	public @ResponseBody String DjfztjInfoDownload(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Map<String, String> mapcondition = new HashMap<String, String>();
		mapcondition.put("id_sjq", request.getParameter("id_sjq"));
		mapcondition.put("id_sjz", request.getParameter("id_sjz"));

		try {
			String statistics_type = new String(request.getParameter(
					"id_fzywtj").getBytes("ISO-8859-1"), "utf-8");
			mapcondition.put("id_fzywtj", statistics_type);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Message m = gztjService.GetDJFZTJ(mapcondition.get("id_sjq"),
			mapcondition.get("id_sjz"), mapcondition.get("id_fzywtj"));
		List<Map> djfz = null;
		if (m != null && m.getRows() != null) {
			djfz = (List<Map>) m.getRows();
		}
		String basePath = request.getRealPath("/") + "\\resources\\PDF";
		String outPath = "";
		String url = "";
		String tmpFullName = "";
		FileOutputStream outStream = null;
		int rownum = 4;
		if (djfz != null && djfz.size() > 0) {
			outPath = basePath + "\\tmp\\fzltj.xls";
			url = request.getContextPath() + "\\resources\\PDF\\tmp\\fzltj.xls";
			outStream = new FileOutputStream(outPath);
			tmpFullName = request.getRealPath("/WEB-INF/jsp/wjmb/fzltj.xls");
			InputStream input = new FileInputStream(tmpFullName);
			HSSFWorkbook hw = null;
			hw = new HSSFWorkbook(input);
			Sheet sheet = hw.getSheetAt(0);
			Map<String, Integer> mapCol = new HashMap<String, Integer>();
			mapCol.put("证书登簿合计", 0);
			mapCol.put("证书登簿姓名" , 1);
			mapCol.put("证书登簿当月", 2);
			mapCol.put("证书登簿累计", 3);
			mapCol.put("证书缮证合计", 4);
			mapCol.put("证书缮证姓名", 5);
			mapCol.put("证书缮证当月", 6);
			mapCol.put("证书缮证累计", 7);
			mapCol.put("证明登簿合计", 8);
			mapCol.put("证明登簿姓名", 9);
			mapCol.put("证明登簿当月", 10);
			mapCol.put("证明登簿累计", 11);
			mapCol.put("证明缮证合计", 12);
			mapCol.put("证明缮证姓名", 13);
			mapCol.put("证明缮证当月", 14);
			mapCol.put("证明缮证累计", 15);
			mapCol.put("总计发证", 16);
			List<Map> djf0 = new ArrayList<Map>();
			List<Map> djf1 = new ArrayList<Map>();
			List<Map> djf2 = new ArrayList<Map>();
			List<Map> djf3 = new ArrayList<Map>();
			
			for (Map djfzs : djfz){
				Object ob0=djfzs.get("ZSLJDBS");
				Object ob1=djfzs.get("ZSLJSZL");
				Object ob2=djfzs.get("ZMLJDBS");
				Object ob3=djfzs.get("ZMLJSZL");
				
				if(Integer.parseInt(ob0.toString())>0){
					Map<String, String> maps0 = new HashMap<String, String>();
					maps0.put("ZSDBR",djfzs.get("ZSDBR").toString());
					maps0.put("ZSDQDBS",djfzs.get("ZSDQDBS").toString());
					maps0.put("ZSLJDBS",djfzs.get("ZSLJDBS").toString());
					djf0.add(maps0);
					
					
				}
				if(Integer.parseInt(ob1.toString())>0){
					Map<String, String> maps1 = new HashMap<String, String>();
					maps1.put("ZSSZR",djfzs.get("ZSSZR").toString());
					maps1.put("ZSDQSZL",djfzs.get("ZSDQSZL").toString());
					maps1.put("ZSLJSZL",djfzs.get("ZSLJSZL").toString());
					djf1.add(maps1);
					
				}
				if(Integer.parseInt(ob2.toString())>0){
					Map<String, String> maps2 = new HashMap<String, String>();
					maps2.put("ZMDBR",(String) djfzs.get("ZMDBR"));
					maps2.put("ZMDQDBS",djfzs.get("ZMDQDBS").toString());
					maps2.put("ZMLJDBS", djfzs.get("ZMLJDBS").toString());
					djf2.add(maps2);
				}
				if(Integer.parseInt( ob3.toString())>0){
					Map<String, String> maps3 = new HashMap<String, String>();
					maps3.put("ZMSZR",djfzs.get("ZMSZR").toString());
					maps3.put("ZMDQSZL",djfzs.get("ZMDQSZL").toString());
					maps3.put("ZMLJSZL",djfzs.get("ZMLJSZL").toString());
					djf3.add(maps3);
					
				}
				
			}
			int max=djf0.size();
			if( djf1.size()>max) max= djf1.size();
			if( djf2.size()>max) max= djf2.size();
			if( djf3.size()>max) max= djf3.size();
			
			for(int j = 0;j<max;j++){
				HSSFRow row = (HSSFRow) sheet.createRow(rownum);
				try {
					if(djf0 != null &&j<djf0.size()){
					HSSFCell cell1 = row.createCell(mapCol.get("证书登簿姓名"));
					if(djf0.get(j).get("ZSDBR")==null){
						//cell1.setCellValue(StringHelper.formatObject(djfzs.get("")));
					}else{
					cell1.setCellValue(StringHelper.formatObject(djf0.get(j).get("ZSDBR")));
					}
					HSSFCell cell2 = row.createCell(mapCol.get("证书登簿当月"));
					if(djf0.get(j).get("ZSDQDBS")==null){
						//cell2.setCellValue(StringHelper.formatObject(djfzs.get("")));
					}else{
					cell2.setCellValue(StringHelper.formatObject(djf0.get(j).get("ZSDQDBS")));
					}
					HSSFCell cell3 = row.createCell(mapCol.get("证书登簿累计"));
					if(djf0.get(j).get("ZSLJDBS")==null){
						//cell3.setCellValue(StringHelper.formatObject(djfzs.get("")));
					}else{
					cell3.setCellValue(StringHelper.formatObject(djf0.get(j).get("ZSLJDBS")));
					}
					}
					if(djf1 != null &&j<djf1.size()){
						HSSFCell cell5 = row.createCell(mapCol.get("证书缮证姓名"));
						if(djf1.get(j).get("ZSSZR")==null){
							//cell5.setCellValue(StringHelper.formatObject(djfzs.get("")));
						}else{
						cell5.setCellValue(StringHelper.formatObject(djf1.get(j).get("ZSSZR")));
						}
						HSSFCell cell6 = row.createCell(mapCol.get("证书缮证当月"));
						if(djf1.get(j).get("ZSDQSZL")==null){
							//cell6.setCellValue(StringHelper.formatObject(djfzs.get("")));
						}else{
						cell6.setCellValue(StringHelper.formatObject(djf1.get(j).get("ZSDQSZL")));
						}
						HSSFCell cell7 = row.createCell(mapCol.get("证书缮证累计"));
						if(djf1.get(j).get("ZSLJSZL")==null){
							//cell7.setCellValue(StringHelper.formatObject(djfzs.get("")));
						}else{
						cell7.setCellValue(StringHelper.formatObject(djf1.get(j).get("ZSLJSZL")));
						}
					
					}
					if(djf2 != null &&j<djf2.size()){

						HSSFCell cell9 = row.createCell(mapCol.get("证明登簿姓名"));
						if(djf2.get(j).get("ZMDBR")==null){
							//cell9.setCellValue(StringHelper.formatObject(djfzs.get("")));
						}else{
						cell9.setCellValue(StringHelper.formatObject(djf2.get(j).get("ZMDBR")));
						}
						HSSFCell cell10 = row.createCell(mapCol.get("证明登簿当月"));
						if(djf2.get(j).get("ZMDQDBS")==null){
							//cell10.setCellValue(StringHelper.formatObject(djfzs.get("")));
						}else{
						cell10.setCellValue(StringHelper.formatObject(djf2.get(j).get("ZMDQDBS")));
						}
						HSSFCell cell11 = row.createCell(mapCol.get("证明登簿累计"));
						if(djf2.get(j).get("ZMLJDBS")==null){
							//cell11.setCellValue(StringHelper.formatObject(djfzs.get("")));
						}else{
						cell11.setCellValue(StringHelper.formatObject(djf2.get(j).get("ZMLJDBS")));
						}
					}
					if(djf3 != null && j<djf3.size()){
						
						HSSFCell cell13 = row.createCell(mapCol.get("证明缮证姓名"));
						if(djf3.get(j).get("ZMSZR")==null){
							//cell13.setCellValue(StringHelper.formatObject(djfzs.get("")));
						}else{
						cell13.setCellValue(StringHelper.formatObject(djf3.get(j).get("ZMSZR")));
						}
						HSSFCell cell14 = row.createCell(mapCol.get("证明缮证当月"));
						if(djf3.get(j).get("ZMDQSZL")==null){
							//cell14.setCellValue(StringHelper.formatObject(djfzs.get("")));
						}else{
						cell14.setCellValue(StringHelper.formatObject(djf3.get(j).get("ZMDQSZL")));
						}
						HSSFCell cell15 = row.createCell(mapCol.get("证明缮证累计"));
						if(djf3.get(j).get("ZMLJSZL")==null){
							//cell15.setCellValue(StringHelper.formatObject(djfzs.get("")));
						}else{
						cell15.setCellValue(StringHelper.formatObject(djf3.get(j).get("ZMLJSZL")));
						}
					}
					HSSFCell cell0 = row.createCell(mapCol.get("证书登簿合计"));
					cell0.setCellValue(StringHelper.formatObject(djfz.get(0).get("")));
					
					
					HSSFCell cell4 = row.createCell(mapCol.get("证书缮证合计"));
					cell4.setCellValue(StringHelper.formatObject(djfz.get(0).get("")));
					
					
					HSSFCell cell8 = row.createCell(mapCol.get("证明登簿合计"));
					cell8.setCellValue(StringHelper.formatObject(djfz.get(0).get("")));
					
					HSSFCell cell12 = row.createCell(mapCol.get("证明缮证合计"));
					cell12.setCellValue(StringHelper.formatObject(djfz.get(0).get("")));
					
					HSSFCell cell16 = row.createCell(mapCol.get("总计发证"));
					cell16.setCellValue(StringHelper.formatObject(djfz.get(0).get("")));
					++rownum;
				}catch (Exception ex) {
					// TODO Auto-generated catch block
					ex.printStackTrace();
				}
			}
			hw.write(outStream);
			outStream.flush();
			outStream.close();
		}
		return url;
	}
	
	/**
     * 导出楼盘表统计（URL:"/fwtj",Method:GET）
     * zl坐落
     * zh幢号
     * bdcdyh不动产单元号
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/dylpbtj", method = RequestMethod.GET)
    public @ResponseBody Message getLpbTJ(HttpServletRequest request,HttpServletResponse response) throws Exception {
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
		mapCondition.put("zl", request.getParameter("zl"));
		mapCondition.put("zh", request.getParameter("zh"));
		mapCondition.put("bdcdyh", request.getParameter("bdcdyh"));
    	
    	Message msg = gztjService.GetLpbTJ(mapCondition, page, rows);
		return msg;
    }
    
    /**
     * 导出楼盘表统计下载（URL:"/fwtj",Method:GET）
     * zl坐落
     * zh幢号
     * bdcdyh不动产单元号
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/dylpbtjdownload", method = RequestMethod.GET)
    public @ResponseBody String  downloadLpbTJ(HttpServletRequest request,HttpServletResponse response) throws Exception {
    	//request.setCharacterEncoding("utf-8");
		String zl=request.getParameter("zl");
		Map<String, String> mapCondition = new HashMap<String, String>();
		mapCondition.put("zl", request.getParameter("zl"));
		mapCondition.put("zh", request.getParameter("zh"));
		mapCondition.put("bdcdyh", request.getParameter("bdcdyh"));
    	
		List<Map> lpbtj = gztjService.dowenloadLpbTJ(mapCondition);
		String basePath = request.getRealPath("/") + "\\resources\\PDF";
		String outpath = "";
		String url = "";
		String tmpFullName = "";
		FileOutputStream outstream = null;
		if(lpbtj != null && lpbtj.size() > 0){
			outpath = basePath + "\\tmp\\dylpbtj.xls";
			url = request.getContextPath() + "\\resources\\PDF\\tmp\\dylpbtj.xls";
		    outstream = new FileOutputStream(outpath); 
		    tmpFullName = request.getRealPath("/WEB-INF/jsp/wjmb/dylpbtj.xls");
		    InputStream input = new FileInputStream(tmpFullName);
			HSSFWorkbook  wb = null;// 定义一个新的工作簿
			wb = new HSSFWorkbook(input);
			Sheet sheet = wb.getSheetAt(0);
			Map<String,Integer> MapCol = new HashMap<String,Integer>();
			MapCol.put("坐落", 0);
			MapCol.put("幢号", 1);
			MapCol.put("房号", 2);
			MapCol.put("不动产权证号", 3);
			MapCol.put("不动产单元号", 4);
			MapCol.put("证书编号", 5);
            int rownum = 2;
			for(Map lpb:lpbtj){
				 
		  		 HSSFRow row = (HSSFRow)sheet.createRow(rownum);
		  		 try{
			         HSSFCell Cell0 = row.createCell(MapCol.get("坐落"));
			         Cell0.setCellValue(StringHelper.formatObject(lpb.get("ZL")));
			         HSSFCell Cell1 = row.createCell(MapCol.get("幢号"));
			         Cell1.setCellValue(StringHelper.formatObject(lpb.get("ZH")));
			         HSSFCell Cell2 = row.createCell(MapCol.get("房号"));
			         Cell2.setCellValue(StringHelper.formatObject(lpb.get("FH")));
			         HSSFCell Cell3 = row.createCell(MapCol.get("不动产权证号"));
			         Cell3.setCellValue(StringHelper.formatObject(lpb.get("BDCQZH")));
			         HSSFCell Cell4 = row.createCell(MapCol.get("不动产单元号"));
			         Cell4.setCellValue(StringHelper.formatObject(lpb.get("BDCDYH")));
			         HSSFCell Cell5 = row.createCell(MapCol.get("证书编号"));
			         Cell5.setCellValue(StringHelper.formatObject(lpb.get("ZSBH")));
			         rownum ++;
		  		 }
		  		 catch(Exception ex){
		  			 
		  		 }
			  }	  		
			 wb.write(outstream); 
			 outstream.flush(); 
			 outstream.close();
		 }
		
		return url;
    }
    
	@RequestMapping(value="/meslog/{id_sjq}/{id_sjz}/{type}/{gl}",method=RequestMethod.GET)
	public @ResponseBody Message getMESLOG(@PathVariable("id_sjq") String id_sjq,@PathVariable("id_sjz") String id_sjz, @PathVariable("type") String type,@PathVariable("gl") String gl,HttpServletRequest request, HttpServletResponse response){
		Integer page = 1;
		if (request.getParameter("page") != null) {
			page = Integer.parseInt(request.getParameter("page"));
		}
		Integer rows = 10;
		if (request.getParameter("rows") != null) {
			rows = Integer.parseInt(request.getParameter("rows"));
		}
		Message msg =gztjService.getMesLog(id_sjq, id_sjz, type,gl,page, rows);
		return msg;
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/meslogdownload",method=RequestMethod.GET)
	public @ResponseBody String mesLoginfodownload(HttpServletRequest request,HttpServletResponse response) throws Exception{
		Map<String,String> mapCondition = new HashMap<String,String>();
		mapCondition.put("id_sjq", request.getParameter("id_sjq"));
		mapCondition.put("id_sjz", request.getParameter("id_sjz"));
		try {
			String type = new String(request.getParameter("id_tjlx").getBytes("iso8859-1"), "utf-8");
			mapCondition.put("id_tjlx",type);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}	
		Message msg =gztjService.getMesLog(mapCondition.get("id_sjq"), mapCondition.get("id_sjz"),mapCondition.get("id_tjlx"),"checked",1,100000);
		List<Map> djdys = null;
		if (msg !=null && msg.getRows()!=null){
			djdys=(List<Map>)msg.getRows();
		}
		String basePath = request.getRealPath("/") + "\\resources\\PDF";
		String outpath = "";
		String url = "";
		String tmpFullName = "";
		FileOutputStream outstream = null;
		if(djdys != null && djdys.size() > 0){
			outpath = basePath + "\\tmp\\meslog"+mapCondition.get("id_tjlx")+".xls";
			url = request.getContextPath() + "\\resources\\PDF\\tmp\\meslog"+mapCondition.get("id_tjlx")+".xls";
		    outstream = new FileOutputStream(outpath); 
		    tmpFullName = request.getRealPath("/WEB-INF/jsp/wjmb/meslog.xls");
		    InputStream input = new FileInputStream(tmpFullName);
			HSSFWorkbook  wb = null;// 定义一个新的工作簿
			wb = new HSSFWorkbook(input);
			Sheet sheet = wb.getSheetAt(0);
			Map<String,Integer> MapCol = new HashMap<String,Integer>();
			MapCol.put("接收电话", 0);
			MapCol.put("接收短信人名称", 1);
			MapCol.put("业务流水号",2);
            int rownum = 0;
            List<String> list = new ArrayList<String>();
			for(Map djdy:djdys){
				 if(djdy.get("FLAG")!=null&&djdy.get("FLAG").toString().equals("1")){
					 continue;
				 }
				 if(djdy.get("JSDH")!=null&&djdy.get("JSDH").toString().length()==11){
			  		 HSSFRow row = (HSSFRow)sheet.createRow(rownum);
			  		 try{
			  			 
					         HSSFCell Cell0 = row.createCell(MapCol.get("接收电话"));
					         Cell0.setCellValue(StringHelper.formatObject(djdy.get("JSDH")));
					         HSSFCell Cell1 = row.createCell(MapCol.get("接收短信人名称"));
					         Cell1.setCellValue(StringHelper.formatObject(djdy.get("JSDXRMC")));
					         HSSFCell Cell2 = row.createCell(MapCol.get("业务流水号"));
					         Cell2.setCellValue(StringHelper.formatObject(djdy.get("YWLSH")));
					         rownum ++;
			  			 }
			  		 catch(Exception ex){
			  			 
			  		 }
				 }
			  }	  		
			 wb.write(outstream); 
			 outstream.flush(); 			
			 outstream.close();
			 gztjService.updateFzxx(mapCondition.get("id_sjq"), mapCondition.get("id_sjz"),mapCondition.get("id_tjlx"));
		 }
		
        return url;
		
		
	}
	@RequestMapping(value="/getinsert/{id_sjq}/{id_sjz}",method=RequestMethod.GET)
	public @ResponseBody Message getInsert(@PathVariable("id_sjq") String id_sjq,@PathVariable("id_sjz") String id_sjz){
		Message msg = gztjService.InsertFzxx(id_sjq, id_sjz);
		return msg;
	}
	
	/**
     * 根据合同号查询，获取房屋信息（URL:"/fwtj",Method:GET）
     * hth合同号
     * fwzt房屋状态
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/gzfwjbzt", method = RequestMethod.GET)
    public @ResponseBody Message getFwjbzt(HttpServletRequest request,HttpServletResponse response) throws Exception {
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
		mapCondition.put("hth", request.getParameter("hth"));
		mapCondition.put("fwzt", request.getParameter("fwzt"));
    	Message msg = gztjService.getFwjbzt(mapCondition, page, rows);
		return msg;
    }
    
    /**
     * 微信信息统计（URL:"/fwtj",Method:GET）
     * fwortd单元类型，房屋或者土地
     * qsj起始时间
     * zsj截止时间
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/mesWeiXin/{id_sjq}/{id_sjz}/{type}",method=RequestMethod.GET)
	public @ResponseBody Message getWeiXin(@PathVariable("id_sjq") String id_sjq,@PathVariable("id_sjz") String id_sjz, @PathVariable("type") String type,HttpServletRequest request, HttpServletResponse response){
		Integer page = 1;
		if (request.getParameter("page") != null) {
			page = Integer.parseInt(request.getParameter("page"));
		}
		Integer rows = 10;
		if (request.getParameter("rows") != null) {
			rows = Integer.parseInt(request.getParameter("rows"));
		}
		Message msg =gztjService.getWeiXins(id_sjq, id_sjz, type, page, rows);
		return msg;
	}

    /**
     * 微信信息统计导出（URL:"/fwtj",Method:GET）
     * fwortd单元类型，房屋或者土地
     * qsj起始时间
     * zsj截止时间
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/mesWeiXindownload",method=RequestMethod.GET)
	public @ResponseBody String mesWeiXindownload(HttpServletRequest request,HttpServletResponse response) throws Exception{
		Map<String,String> mapCondition = new HashMap<String,String>();
		mapCondition.put("id_sjq", request.getParameter("id_sjq"));
		mapCondition.put("id_sjz", request.getParameter("id_sjz"));
		try {
			String type = new String(request.getParameter("id_tjlx").getBytes("iso8859-1"), "utf-8");
			mapCondition.put("id_tjlx",type);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}	
		Message msg =gztjService.getWeiXins(mapCondition.get("id_sjq"), mapCondition.get("id_sjz"),"2",1,100000);//选定时间内的所有数据
		List<Map> djdys = null;
		if (msg !=null && msg.getRows()!=null){
			djdys=(List<Map>)msg.getRows();
		}
		String basePath = request.getRealPath("/") + "\\resources\\PDF";
		String outpath = "";
		String url = "";
		String tmpFullName = "";
		FileOutputStream outstream = null;
		if(djdys != null && djdys.size() > 0){
			outpath = basePath + "\\tmp\\mesWeiXin.xls";
			url = request.getContextPath() + "\\resources\\PDF\\tmp\\mesWeiXin.xls";
		    outstream = new FileOutputStream(outpath); 
		    tmpFullName = request.getRealPath("/WEB-INF/jsp/wjmb/mesWeiXin.xls");
		    InputStream input = new FileInputStream(tmpFullName);
			HSSFWorkbook  wb = null;// 定义一个新的工作簿
			wb = new HSSFWorkbook(input);
			Sheet sheet = wb.getSheetAt(0);
			Map<String,Integer> MapCol = new HashMap<String,Integer>();
			MapCol.put("id", 0);
			MapCol.put("编号",1);
			MapCol.put("业务流水号", 2);
			MapCol.put("姓名", 3);
			MapCol.put("身份证号", 4);
			MapCol.put("手机号",5);
			MapCol.put("事件",6);
			MapCol.put("房屋状态",7);
			MapCol.put("状态",8);
			MapCol.put("创建时间",9);
			MapCol.put("修改时间",10);
            int rownum = 1;
			for(Map djdy:djdys){
			  		 HSSFRow row = (HSSFRow)sheet.createRow(rownum);
			  		 try{
			  			 HSSFCell Cell0 = row.createCell(MapCol.get("id"));
				         Cell0.setCellValue(StringHelper.formatObject(djdy.get("ID")));
				         HSSFCell Cell1 = row.createCell(MapCol.get("编号"));
				         Cell1.setCellValue(StringHelper.formatObject(djdy.get("XMBH")));
				         HSSFCell Cell2 = row.createCell(MapCol.get("业务流水号"));
				         Cell2.setCellValue(StringHelper.formatObject(djdy.get("YWLSH")));
				         HSSFCell Cell3 = row.createCell(MapCol.get("姓名"));
				         Cell3.setCellValue(StringHelper.formatObject(djdy.get("QLRMC")));
				         HSSFCell Cell4 = row.createCell(MapCol.get("身份证号"));
				         Cell4.setCellValue(StringHelper.formatObject(djdy.get("ZJH")));
				         HSSFCell Cell5 = row.createCell(MapCol.get("手机号"));
				         Cell5.setCellValue(StringHelper.formatObject(djdy.get("LXDH")));
				         HSSFCell Cell6 = row.createCell(MapCol.get("事件"));
				         Cell6.setCellValue(StringHelper.formatObject(djdy.get("XMMC")));
				         HSSFCell Cell7 = row.createCell(MapCol.get("房屋状态"));
				         Cell7.setCellValue(StringHelper.formatObject(djdy.get("")));
				         HSSFCell Cell8 = row.createCell(MapCol.get("状态"));
				         Cell8.setCellValue(StringHelper.formatObject(djdy.get("BLZT")));
				         HSSFCell Cell9 = row.createCell(MapCol.get("创建时间"));
				         Cell9.setCellValue(StringHelper.formatObject(djdy.get("CJSJ")));
				         HSSFCell Cell10 = row.createCell(MapCol.get("修改时间"));
				         Cell10.setCellValue(StringHelper.formatObject(djdy.get("XGSJ")));
				         rownum ++;
			  		 }
			  		 catch(Exception ex){
				 }
			  }	  		
			 wb.write(outstream); 
			 outstream.flush(); 
			 outstream.close();
		 }
        return url;
	}
    @RequestMapping(value="/getinsertcxjd/{id_sjq}/{id_sjz}/{type}",method=RequestMethod.GET)
	public @ResponseBody Message getInsert(@PathVariable("id_sjq") String id_sjq,@PathVariable("id_sjz") String id_sjz,@PathVariable("type") String type,HttpServletRequest request,HttpServletResponse response){
    	Message msg =new Message();
    	try {
			String types = new String(type.getBytes("iso8859-1"), "utf-8");
			msg = gztjService.InsertCxjd(id_sjq, id_sjz,types);
    	} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}	
		return msg;
	}
    
    @RequestMapping(value = "/qjcxinfo", method = RequestMethod.GET)
	public @ResponseBody Message QJCXInfo( HttpServletRequest request, HttpServletResponse response) throws Exception{
		Message msg = gztjService.GetQjCx();
		return msg;
    	 	 
    }
	/**
     * 业务统计（鹰潭）（URL:"/ywtj",Method:GET）
     * qsj起始时间
     * zsj截止时间
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/ywtj/{qsj}/{zsj}/{qlrlx}/{djlx}/{qhlx}", method = RequestMethod.GET)
    public @ResponseBody Message getYWTJ(@PathVariable("qsj") String tjsjqs,@PathVariable("zsj") String tjsjjz,@PathVariable("qlrlx") String qlrlx,
    		@PathVariable("djlx") String djlx,@PathVariable("qhlx") String qhlx,HttpServletRequest request,HttpServletResponse response) throws Exception {

    	
		String fzlb = new String(request.getParameter(
				"dyselect").getBytes("ISO-8859-1"), "utf-8");
		Message msg = gztjService.GetYWTJ(tjsjqs, tjsjjz,djlx,qlrlx,qhlx,fzlb);
		
		
		return msg;
    }




   /**
     * 效能统计（鹰潭）
     * sjq起始时间
     * sjz截止时间
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/efficiency_yt/{sjq}/{sjz}/{user}/{select}/{select_sj}/", method = RequestMethod.GET)
    public @ResponseBody Message Getefficiency_yt(@PathVariable("sjq") String sjq,@PathVariable("sjz") String sjz,
    		@PathVariable("user") String user,@PathVariable("select") String select,@PathVariable("select_sj") String select_sj,
    		HttpServletResponse response) throws Exception {
		Message msg = gztjService.Getefficiency_yt(sjq, sjz,user,select,select_sj);
		return msg;
    }


 @SuppressWarnings({ "rawtypes", "unchecked" })
   	@RequestMapping(value = "/efficiencyinfodownload_yt", method = RequestMethod.GET)
   	public @ResponseBody String EfficiencyinfoDownload_yt(HttpServletRequest request, HttpServletResponse response) throws Exception{
   				
   		Map<String, String> mapCondition = new HashMap<String, String>();
   		mapCondition.put("id_sjq", request.getParameter("id_slsjq"));
   		mapCondition.put("id_sjz", request.getParameter("id_slsjz"));
   		mapCondition.put("id_user", request.getParameter("usertree"));
   		String select = "";
   		String select1 = "";
   		String ry = request.getParameter(
				"checkbox_ry");  
   		String ks = request.getParameter(
				"checkbox_ks");
   		String nd = request.getParameter(
				"checkbox_nd");
   		String jd = request.getParameter(
				"checkbox_jd");
   		String yd = request.getParameter(
				"checkbox_yd");
   		if(ry != null && !ry.equals("") ){
   			
   			select =  new String(ry.getBytes("ISO-8859-1"), "utf-8");
   		}else if(ks != null && !ks.equals("")){
   			select = new String(ks.getBytes("ISO-8859-1"), "utf-8");
   		}
   		 if(nd != null && !nd.equals("")){
   			select1 =  new String(nd.getBytes("ISO-8859-1"), "utf-8");
   		}else if(jd != null && !jd.equals("")){
   			select1 =  new String(jd.getBytes("ISO-8859-1"), "utf-8");
   		}else if(yd != null && !yd.equals("")) {
   			select1 =  new String(yd.getBytes("ISO-8859-1"), "utf-8");
   		}
   		
   		
   		
   		Message m = gztjService.Getefficiency_yt(mapCondition.get("id_sjq"),mapCondition.get("id_sjz"),mapCondition.get("id_user"),select,select1);
   		List<Map> djdys = null;
   		if (m !=null && m.getRows()!=null){
   			djdys=(List<Map>)m.getRows();
   		}
   		String basePath = request.getRealPath("/") + "\\resources\\PDF";
   		String outpath = "";
   		String url = "";
   		String tmpFullName = "";
   		FileOutputStream outstream = null;
   		if(djdys != null && djdys.size() > 0){
   			
   			if(select.equals("人员")){
   				outpath = basePath + "\\tmp\\efficiency_yt.xls";
   	   			url = request.getContextPath() + "\\resources\\PDF\\tmp\\efficiency_yt.xls";
   	   		    outstream = new FileOutputStream(outpath); 
   	   		    tmpFullName = request.getRealPath("/WEB-INF/jsp/wjmb/efficiency_yt.xls");
   			}else if(select.equals("科室")){
   				outpath = basePath + "\\tmp\\efficiency_yt1.xls";
   	   			url = request.getContextPath() + "\\resources\\PDF\\tmp\\efficiency_yt1.xls";
   	   		    outstream = new FileOutputStream(outpath); 
   	   		    tmpFullName = request.getRealPath("/WEB-INF/jsp/wjmb/efficiency_yt1.xls");
   				
   			}
   			
   		    InputStream input = new FileInputStream(tmpFullName);
   			HSSFWorkbook  wb = null;// 定义一个新的工作簿
   			wb = new HSSFWorkbook(input);
   			Sheet sheet = wb.getSheetAt(0);
   			Map<String,Integer> MapCol = new HashMap<String,Integer>();
   			if(select.equals("人员")){
   				MapCol.put("科室", 0);
   				MapCol.put("姓名", 1);
   				MapCol.put("办件量", 2);
   				MapCol.put("在办件量", 3);
   				MapCol.put("已办件量", 4);
   				MapCol.put("超期件", 5);
   				MapCol.put("在办超期件", 6);
   				MapCol.put("未超期件", 7);
   			
               int rownum = 2;
   			for(Map djdy:djdys){
   				 
   		  		 HSSFRow row = (HSSFRow)sheet.createRow(rownum);
   		  		 try{
   		  			 HSSFCell Cell0 = row.createCell(MapCol.get("科室"));
			         Cell0.setCellValue(StringHelper.formatObject(djdy.get("DEPARTMENTNAME")));
   			         HSSFCell Cell1 = row.createCell(MapCol.get("姓名"));
   			         Cell1.setCellValue(StringHelper.formatObject(djdy.get("USERNAME")));
   			         HSSFCell Cell2 = row.createCell(MapCol.get("办件量"));
   			         Cell2.setCellValue(StringHelper.formatObject(djdy.get("BJL")));
   			         HSSFCell Cell3 = row.createCell(MapCol.get("在办件量"));
   			         Cell3.setCellValue(StringHelper.formatObject(djdy.get("ZBJS")));
   			         HSSFCell Cell4 = row.createCell(MapCol.get("已办件量"));
   			         Cell4.setCellValue(StringHelper.formatObject(djdy.get("YBJS")));
   			         HSSFCell Cell5 = row.createCell(MapCol.get("超期件"));
			         Cell5.setCellValue(StringHelper.formatObject(djdy.get("CQJ")));
   			         HSSFCell Cell6 = row.createCell(MapCol.get("在办超期件"));
   			         Cell6.setCellValue(StringHelper.formatObject(djdy.get("ZBCQJ")));
   			         HSSFCell Cell7 = row.createCell(MapCol.get("未超期件"));
   			         Cell7.setCellValue(StringHelper.formatObject(djdy.get("WCQJ")));
   			         
   			         rownum ++;
   		  		 }
   		  		 catch(Exception ex){
   		  			 
   		  		 }
   			  }	
   			}else if(select.equals("科室")){
   				MapCol.put("科室", 0);
   				MapCol.put("办件量", 1);
   				MapCol.put("在办件量", 2);
   				MapCol.put("已办件量", 3);
   				MapCol.put("超期件", 4);
   				MapCol.put("在办超期件",5);
   				MapCol.put("未超期件", 6);
   			
               int rownum = 2;
   			for(Map djdy:djdys){
   				 
   		  		 HSSFRow row = (HSSFRow)sheet.createRow(rownum);
   		  		 try{
   		  			 HSSFCell Cell0 = row.createCell(MapCol.get("科室"));
			         Cell0.setCellValue(StringHelper.formatObject(djdy.get("DEPARTMENTNAME")));
   			         HSSFCell Cell1 = row.createCell(MapCol.get("办件量"));
   			         Cell1.setCellValue(StringHelper.formatObject(djdy.get("BJL")));
   			         HSSFCell Cell2 = row.createCell(MapCol.get("在办件量"));
   			         Cell2.setCellValue(StringHelper.formatObject(djdy.get("ZBJS")));
   			         HSSFCell Cell3 = row.createCell(MapCol.get("已办件量"));
   			         Cell3.setCellValue(StringHelper.formatObject(djdy.get("YBJS")));
   			         HSSFCell Cell4 = row.createCell(MapCol.get("超期件"));
			         Cell4.setCellValue(StringHelper.formatObject(djdy.get("CQJ")));
   			         HSSFCell Cell5 = row.createCell(MapCol.get("在办超期件"));
   			         Cell5.setCellValue(StringHelper.formatObject(djdy.get("ZBCQJ")));
   			         HSSFCell Cell6 = row.createCell(MapCol.get("未超期件"));
   			         Cell6.setCellValue(StringHelper.formatObject(djdy.get("WCQJ")));
   			         
   			         rownum ++;
   		  		 }
   		  		 catch(Exception ex){
   		  			 
   		  		 }
   			  }	
   			}
   			 wb.write(outstream); 
   			 outstream.flush(); 
   			 outstream.close();
   		 }

           return url;
   	}
    
    /**
     * 行政区划tree
     * @return
     * @throws Exception
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/getxzqh", method = RequestMethod.GET)
	public @ResponseBody List<Tree> getXzqhyt(HttpServletRequest request, HttpServletResponse response) {
    	
    	String consts = request.getParameter("type");
    	StringBuilder build = new StringBuilder();
		String constsID = consts;
		List<Map> maps = null;
		try{
			build.append(" select h.constvalue as id,h.consttrans as text from BDCK.BDCS_const h where h.constslsid='"+constsID+"'");
			maps = dao.getDataListByFullSql(build.toString());
		}
		catch(Exception e){
			maps = null;
		}
    	List<Map> dept = maps;
    	List<Tree> eTrees = new ArrayList<Tree>();
    	Tree dTree = new Tree();
    	
    	String departments = "鹰潭市";
    	dTree.setId("10000");
    	dTree.setText(departments);
    	dTree.setChecked(false);
    	if(dept != null){
			for (Map map : dept) {
					Tree uTree = new Tree();
					Object key = map.get("ID") != null ? map.get("ID"):"";
					Object value = map.get("TEXT") != null ? map.get("TEXT"):"";
					uTree.setId(key.toString());
					uTree.setText(value.toString());
					uTree.setChecked(false); 
					if (dTree.children == null) {
						dTree.children = new ArrayList<Tree>();
					}
					dTree.children.add(uTree);
				}
    	}
			eTrees.add(dTree);
		return eTrees;
    }
    
    /**
     * 数据上报导出
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
   	@RequestMapping(value = "/sjsbtjdownload", method = RequestMethod.GET)
   	public @ResponseBody String sjsbtjDownload(HttpServletRequest request, HttpServletResponse response) throws Exception{
    	String id_sjq = request.getParameter("id_sjq");
   		String id_sjz = request.getParameter("id_sjz");
   		String qhtree = new String(request.getParameter("qhtree").getBytes("ISO-8859-1"), "utf-8");
   		String djlxtree = new String(request.getParameter("djlxtree").getBytes("ISO-8859-1"), "utf-8");
   		String type = request.getParameter("type");
   		if(qhtree.equals("")){
   			qhtree="0";
   		}
   		if(djlxtree.equals("")){
   			djlxtree="0";
   		}
   		Message m = gztjService.GetSJSBTJ(id_sjq, id_sjz, qhtree, djlxtree,type);
   		List<Map> sjsb = null;
   		if (m !=null && m.getRows()!=null){
   			sjsb=(List<Map>)m.getRows();
   		}
   		String basePath = request.getRealPath("/") + "\\resources\\PDF";
   		String outpath = "";
   		String url = "";
   		String tmpFullName = "";
   		FileOutputStream outstream = null;
   		
   		if(sjsb != null && sjsb.size() > 0){
   			if(type.equals("0")){
   				outpath = basePath + "\\tmp\\sjsbtj_qh.xls";
   	   			url = request.getContextPath() + "\\resources\\PDF\\tmp\\sjsbtj_qh.xls";
   	   		    outstream = new FileOutputStream(outpath); 
   	   		    tmpFullName = request.getRealPath("/WEB-INF/jsp/wjmb/sjsbtj_qh.xls");  	
   			}else if(type.equals("1")){
   				outpath = basePath + "\\tmp\\sjsbtj_djlx.xls";
   	   			url = request.getContextPath() + "\\resources\\PDF\\tmp\\sjsbtj_djlx.xls";
   	   		    outstream = new FileOutputStream(outpath); 
   	   		    tmpFullName = request.getRealPath("/WEB-INF/jsp/wjmb/sjsbtj_djlx.xls");    
   			}else if(type.equals("2")){
   				outpath = basePath + "\\tmp\\sjsbtj_year.xls";
   	   			url = request.getContextPath() + "\\resources\\PDF\\tmp\\sjsbtj_year.xls";
   	   		    outstream = new FileOutputStream(outpath); 
   	   		    tmpFullName = request.getRealPath("/WEB-INF/jsp/wjmb/sjsbtj_year.xls");
   				
   			}else if(type.equals("3")){
   				outpath = basePath + "\\tmp\\sjsbtj_jd.xls";
   	   			url = request.getContextPath() + "\\resources\\PDF\\tmp\\sjsbtj_jd.xls";
   	   		    outstream = new FileOutputStream(outpath); 
   	   		    tmpFullName = request.getRealPath("/WEB-INF/jsp/wjmb/sjsbtj_jd.xls");
   				
   			}else {
   				outpath = basePath + "\\tmp\\sjsbtj_yf.xls";
   	   			url = request.getContextPath() + "\\resources\\PDF\\tmp\\sjsbtj_yf.xls";
   	   		    outstream = new FileOutputStream(outpath); 
   	   		    tmpFullName = request.getRealPath("/WEB-INF/jsp/wjmb/sjsbtj_yf.xls");
   				
   			}
   			
   			InputStream input = new FileInputStream(tmpFullName);
   			HSSFWorkbook  wb = null;// 定义一个新的工作簿
   			wb = new HSSFWorkbook(input);
   			Sheet sheet = wb.getSheetAt(0);
   			Map<String,Integer> MapCol = new HashMap<String,Integer>();
   			String name = "";
   			String name1 = "";
   			String name2 ="";
   			String str  = "";
   			String str1  = "";
   			String str2 = "";
   		 int rownum = 3;
   		 if(!type.equals("")){
   			 if(type.equals("0")){
   				name = "行政区划";str="QHMC";
   			
   				MapCol.put(name, 0);
				MapCol.put("上报失败", 1);
				MapCol.put("未上报", 2);
				MapCol.put("已上报", 3);
				for(Map sj:sjsb){
					 HSSFRow row = (HSSFRow)sheet.createRow(rownum);
      		  		 try{
      		  			
      		  			 HSSFCell Cell0 = row.createCell(MapCol.get(name));
   			             Cell0.setCellValue(StringHelper.formatObject(sj.get("CONSTTRANS")));
      			         HSSFCell Cell1 = row.createCell(MapCol.get("上报失败"));
      			         Cell1.setCellValue(StringHelper.formatObject(sj.get("SBSB")));
      			         HSSFCell Cell2 = row.createCell(MapCol.get("未上报"));
      			         Cell2.setCellValue(StringHelper.formatObject(sj.get("WSB")));
      			         HSSFCell Cell3 = row.createCell(MapCol.get("已上报"));
      			         Cell3.setCellValue(StringHelper.formatObject(sj.get("YSB")));  			 
	         
      			         rownum ++;
      		  		 }
      		  		 catch(Exception ex){
      		  			 
      		  		 }
				}
   			 }else if (type.equals("1")){
   				 name = "登记类型"; str="DJLX";
   				 name1 = "权利类型"; str1="QLLX";
   				MapCol.put(name, 0);
   				MapCol.put(name1, 1);
				MapCol.put("上报失败", 2);
				MapCol.put("未上报", 3);
				MapCol.put("已上报", 4);
				for(Map sj:sjsb){
					 HSSFRow row = (HSSFRow)sheet.createRow(rownum);
     		  		 try{
     		  			
     		  			 HSSFCell Cell0 = row.createCell(MapCol.get(name));
  			             Cell0.setCellValue(StringHelper.formatObject(sj.get("DJLX")));
  			             HSSFCell Cell1 = row.createCell(MapCol.get(name1));
			             Cell1.setCellValue(StringHelper.formatObject(sj.get("QLLX")));
     			         HSSFCell Cell2 = row.createCell(MapCol.get("上报失败"));
     			         Cell2.setCellValue(StringHelper.formatObject(sj.get("SBSB")));
     			         HSSFCell Cell3 = row.createCell(MapCol.get("未上报"));
     			         Cell3.setCellValue(StringHelper.formatObject(sj.get("WSB")));
     			         HSSFCell Cell4 = row.createCell(MapCol.get("已上报"));
     			         Cell4.setCellValue(StringHelper.formatObject(sj.get("YSB")));  			 
	         
     			         rownum ++;
     		  		 }
     		  		 catch(Exception ex){
     		  			 
     		  		 }
				}
   		     }else if (type.equals("2")){
  				name = "年度";str="YEAR";
  				
  				MapCol.put(name, 0);
  				MapCol.put("上报失败", 1);
				MapCol.put("未上报", 2);
				MapCol.put("已上报", 3);
				
				for(Map sj:sjsb){
					 HSSFRow row = (HSSFRow)sheet.createRow(rownum);
      		  		 try{
      		  			
      		  			 HSSFCell Cell0 = row.createCell(MapCol.get(name));
   			             Cell0.setCellValue(StringHelper.formatObject(sj.get("YEAR")));
      			         HSSFCell Cell1 = row.createCell(MapCol.get("上报失败"));
      			         Cell1.setCellValue(StringHelper.formatObject(sj.get("SBSB")));
      			         HSSFCell Cell2 = row.createCell(MapCol.get("未上报"));
      			         Cell2.setCellValue(StringHelper.formatObject(sj.get("WSB")));
      			         HSSFCell Cell3 = row.createCell(MapCol.get("已上报"));
      			         Cell3.setCellValue(StringHelper.formatObject(sj.get("YSB")));
          
      			         rownum ++;
      		  		 }
      		  		 catch(Exception ex){
      		  			 
      		  		 }
				}
  			 }
   			 else if (type.equals("3")){
   				name = "年度";str="YEAR";
   				name1 = "季度"; str1 = "TIME";
   				
   				MapCol.put(name, 0);
   				MapCol.put(name1, 1);
   				MapCol.put("上报失败", 2);
				MapCol.put("未上报", 3);
				MapCol.put("已上报", 4);

				for(Map sj:sjsb){
					 HSSFRow row = (HSSFRow)sheet.createRow(rownum);
      		  		 try{
      		  			
      		  			 HSSFCell Cell0 = row.createCell(MapCol.get(name));
   			             Cell0.setCellValue(StringHelper.formatObject(sj.get("YEAR")));
   			             HSSFCell Cell1 = row.createCell(MapCol.get(name1));
			             Cell1.setCellValue(StringHelper.formatObject(sj.get("TIME")));
      			         HSSFCell Cell2 = row.createCell(MapCol.get("上报失败"));
      			         Cell2.setCellValue(StringHelper.formatObject(sj.get("SBSB")));
      			         HSSFCell Cell3 = row.createCell(MapCol.get("未上报"));
      			         Cell3.setCellValue(StringHelper.formatObject(sj.get("WSB")));
      			         HSSFCell Cell4 = row.createCell(MapCol.get("已上报"));
      			         Cell4.setCellValue(StringHelper.formatObject(sj.get("YSB")));
	         
      			         rownum ++;
      		  		 }
      		  		 catch(Exception ex){
      		  			 
      		  		 }
				}
   			 }else{
   				name = "年度";str="YEAR";
   				name1 = "季度"; str1 = "TIME";
   				name2 = "月度";str2="MONTH";
   				
   				MapCol.put(name, 0);
   				MapCol.put(name1, 1);
   				MapCol.put(name2, 2);
				MapCol.put("上报失败", 3);
				MapCol.put("未上报", 4);
				MapCol.put("已上报", 5);

				for(Map sj:sjsb){
					 HSSFRow row = (HSSFRow)sheet.createRow(rownum);
      		  		 try{
      		  			
      		  			 HSSFCell Cell0 = row.createCell(MapCol.get(name));
   			             Cell0.setCellValue(StringHelper.formatObject(sj.get("YEAR")));
   			             HSSFCell Cell1 = row.createCell(MapCol.get(name1));
			             Cell1.setCellValue(StringHelper.formatObject(sj.get("TIME")));
			             HSSFCell Cell2 = row.createCell(MapCol.get(name2));
      			         Cell2.setCellValue(StringHelper.formatObject(sj.get("MONTH")));
      			         HSSFCell Cell3 = row.createCell(MapCol.get("上报失败"));
      			         Cell3.setCellValue(StringHelper.formatObject(sj.get("SBSB")));
      			         HSSFCell Cell4 = row.createCell(MapCol.get("未上报"));
      			         Cell4.setCellValue(StringHelper.formatObject(sj.get("WSB")));
      			         HSSFCell Cell5 = row.createCell(MapCol.get("已上报"));
      			         Cell5.setCellValue(StringHelper.formatObject(sj.get("YSB")));
	         
      			         rownum ++;
      		  		 }
      		  		 catch(Exception ex){
      		  			 
      		  		 }
				}
   			 }
   			    
   		 }
   		    wb.write(outstream); 
			 outstream.flush(); 
			 outstream.close();
   	   }
   		return url;
    }
    
    /**
     * 短信统计导出
     * 
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
   	@RequestMapping(value = "/dxtjdownload", method = RequestMethod.GET)
   	public @ResponseBody String dxtjDownload(HttpServletRequest request, HttpServletResponse response) throws Exception{
    	
    	String id_sjq = request.getParameter("id_sjq");
   		String id_sjz = request.getParameter("id_sjz");
   		String qhtree = new String(request.getParameter("qhtree").getBytes("ISO-8859-1"), "utf-8");
   		String type = request.getParameter("type");
   		if(qhtree.equals("")){
   			qhtree="0";
   		}
   		Message m = gztjService.GetDXTJ(id_sjq, id_sjz, qhtree,type);
   		List<Map> dxxx = null;
   		if (m !=null && m.getRows()!=null){
   			dxxx=(List<Map>)m.getRows();
   		}
   		String basePath = request.getRealPath("/") + "\\resources\\PDF";
   		String outpath = "";
   		String url = "";
   		String tmpFullName = "";
   		FileOutputStream outstream = null;
   		if(dxxx != null && dxxx.size() > 0){
   			if(type.equals("0")){
   				outpath = basePath + "\\tmp\\dxtj_qh.xls";
   	   			url = request.getContextPath() + "\\resources\\PDF\\tmp\\dxtj_qh.xls";
   	   		    outstream = new FileOutputStream(outpath); 
   	   		    tmpFullName = request.getRealPath("/WEB-INF/jsp/wjmb/dxtj_qh.xls");  				
   			}else if(type.equals("1")){
   				outpath = basePath + "\\tmp\\dxtj_year.xls";
   	   			url = request.getContextPath() + "\\resources\\PDF\\tmp\\dxtj_year.xls";
   	   		    outstream = new FileOutputStream(outpath); 
   	   		    tmpFullName = request.getRealPath("/WEB-INF/jsp/wjmb/dxtj_year.xls");
   				
   			}else if(type.equals("2")){
   				outpath = basePath + "\\tmp\\dxtj_jd.xls";
   	   			url = request.getContextPath() + "\\resources\\PDF\\tmp\\dxtj_jd.xls";
   	   		    outstream = new FileOutputStream(outpath); 
   	   		    tmpFullName = request.getRealPath("/WEB-INF/jsp/wjmb/dxtj_jd.xls");
   				
   			}else {
   				outpath = basePath + "\\tmp\\dxtj_yf.xls";
   	   			url = request.getContextPath() + "\\resources\\PDF\\tmp\\dxtj_yf.xls";
   	   		    outstream = new FileOutputStream(outpath); 
   	   		    tmpFullName = request.getRealPath("/WEB-INF/jsp/wjmb/dxtj_yf.xls");
   				
   			}
   			InputStream input = new FileInputStream(tmpFullName);
   			HSSFWorkbook  wb = null;// 定义一个新的工作簿
   			wb = new HSSFWorkbook(input);
   			Sheet sheet = wb.getSheetAt(0);
   			Map<String,Integer> MapCol = new HashMap<String,Integer>();
   			String name = "";
   			String name1 = "";
   			String name2 ="";
   			String str  = "";
   			String str1  = "";
   			String str2 = "";
   		 int rownum = 3;
   		 if(!type.equals("")){
   			 if(type.equals("0")){
   				name = "行政区划";str="QHMC";
   			
   				MapCol.put(name, 0);
				MapCol.put("合计", 1);
				MapCol.put("发送成功", 2);
				MapCol.put("发送失败", 3);
				MapCol.put("合计", 4);
				MapCol.put("发送失败", 5);
				MapCol.put("发送成功", 6);
				for(Map dx:dxxx){
					 HSSFRow row = (HSSFRow)sheet.createRow(rownum);
      		  		 try{
      		  			
      		  			 HSSFCell Cell0 = row.createCell(MapCol.get(name));
   			             Cell0.setCellValue(StringHelper.formatObject(dx.get("CONSTTRANS")));
      			         HSSFCell Cell1 = row.createCell(MapCol.get("合计"));
      			         Cell1.setCellValue(StringHelper.formatObject(dx.get("HJ")));
      			         HSSFCell Cell2 = row.createCell(MapCol.get("发送成功"));
      			         Cell2.setCellValue(StringHelper.formatObject(dx.get("FSCG")));
      			         HSSFCell Cell3 = row.createCell(MapCol.get("发送失败"));
      			         Cell3.setCellValue(StringHelper.formatObject(dx.get("FSSB")));
      			         HSSFCell Cell4 = row.createCell(MapCol.get("合计"));
    			         Cell4.setCellValue(StringHelper.formatObject(dx.get("LHJ")));
    			         HSSFCell Cell5 = row.createCell(MapCol.get("发送成功"));
    			         Cell5.setCellValue(StringHelper.formatObject(dx.get("LFSCG")));
    			         HSSFCell Cell6 = row.createCell(MapCol.get("发送失败"));
    			         Cell6.setCellValue(StringHelper.formatObject(dx.get("LFSSB")));
	         
      			         rownum ++;
      		  		 }
      		  		 catch(Exception ex){
      		  			 
      		  		 }
				}
   			 
   		     }else if (type.equals("1")){
  				name = "年度";str="YEAR";
  				
  				MapCol.put(name, 0);
				MapCol.put("合计", 1);
				MapCol.put("发送成功", 2);
				MapCol.put("发送失败", 3);
				MapCol.put("合计", 4);
				MapCol.put("发送失败", 5);
				MapCol.put("发送成功", 6);
				for(Map dx:dxxx){
					 HSSFRow row = (HSSFRow)sheet.createRow(rownum);
      		  		 try{
      		  			
      		  			 HSSFCell Cell0 = row.createCell(MapCol.get(name));
   			             Cell0.setCellValue(StringHelper.formatObject(dx.get("YEAR")));
      			         HSSFCell Cell1 = row.createCell(MapCol.get("合计"));
      			         Cell1.setCellValue(StringHelper.formatObject(dx.get("HJ")));
      			         HSSFCell Cell2 = row.createCell(MapCol.get("发送成功"));
      			         Cell2.setCellValue(StringHelper.formatObject(dx.get("FSCG")));
      			         HSSFCell Cell3 = row.createCell(MapCol.get("发送失败"));
      			         Cell3.setCellValue(StringHelper.formatObject(dx.get("FSSB")));
      			         HSSFCell Cell4 = row.createCell(MapCol.get("合计"));
    			         Cell4.setCellValue(StringHelper.formatObject(dx.get("LHJ")));
    			         HSSFCell Cell5 = row.createCell(MapCol.get("发送成功"));
    			         Cell5.setCellValue(StringHelper.formatObject(dx.get("LFSCG")));
    			         HSSFCell Cell6 = row.createCell(MapCol.get("发送失败"));
    			         Cell6.setCellValue(StringHelper.formatObject(dx.get("LFSSB")));
	         
      			         rownum ++;
      		  		 }
      		  		 catch(Exception ex){
      		  			 
      		  		 }
				}
  			 }
   			 else if (type.equals("2")){
   				name = "年度";str="YEAR";
   				name1 = "季度"; str1 = "TIME";
   				
   				MapCol.put(name, 0);
   				MapCol.put(name1, 1);
				MapCol.put("合计", 2);
				MapCol.put("发送成功", 3);
				MapCol.put("发送失败", 4);
				MapCol.put("合计", 5);
				MapCol.put("发送失败", 6);
				MapCol.put("发送成功", 7);
				for(Map dx:dxxx){
					 HSSFRow row = (HSSFRow)sheet.createRow(rownum);
      		  		 try{
      		  			
      		  			 HSSFCell Cell0 = row.createCell(MapCol.get(name));
   			             Cell0.setCellValue(StringHelper.formatObject(dx.get("YEAR")));
   			             HSSFCell Cell1 = row.createCell(MapCol.get(name1));
			             Cell1.setCellValue(StringHelper.formatObject(dx.get("TIME")));
      			         HSSFCell Cell2 = row.createCell(MapCol.get("合计"));
      			         Cell2.setCellValue(StringHelper.formatObject(dx.get("HJ")));
      			         HSSFCell Cell3 = row.createCell(MapCol.get("发送成功"));
      			         Cell3.setCellValue(StringHelper.formatObject(dx.get("FSCG")));
      			         HSSFCell Cell4 = row.createCell(MapCol.get("发送失败"));
      			         Cell4.setCellValue(StringHelper.formatObject(dx.get("FSSB")));
      			         HSSFCell Cell5 = row.createCell(MapCol.get("合计"));
    			         Cell5.setCellValue(StringHelper.formatObject(dx.get("LHJ")));
    			         HSSFCell Cell6 = row.createCell(MapCol.get("发送成功"));
    			         Cell6.setCellValue(StringHelper.formatObject(dx.get("LFSCG")));
    			         HSSFCell Cell7 = row.createCell(MapCol.get("发送失败"));
    			         Cell7.setCellValue(StringHelper.formatObject(dx.get("LFSSB")));
	         
      			         rownum ++;
      		  		 }
      		  		 catch(Exception ex){
      		  			 
      		  		 }
				}
   			 }else{
   				name = "年度";str="YEAR";
   				name1 = "季度"; str1 = "TIME";
   				name2 = "月度";str2="MONTH";
   				
   				MapCol.put(name, 0);
   				MapCol.put(name1, 1);
   				MapCol.put(name2, 2);
				MapCol.put("合计", 3);
				MapCol.put("发送成功", 4);
				MapCol.put("发送失败", 5);
				MapCol.put("合计", 6);
				MapCol.put("发送失败", 7);
				MapCol.put("发送成功", 8);
				for(Map dx:dxxx){
					 HSSFRow row = (HSSFRow)sheet.createRow(rownum);
      		  		 try{
      		  			
      		  			 HSSFCell Cell0 = row.createCell(MapCol.get(name));
   			             Cell0.setCellValue(StringHelper.formatObject(dx.get("YEAR")));
   			             HSSFCell Cell1 = row.createCell(MapCol.get(name1));
			             Cell1.setCellValue(StringHelper.formatObject(dx.get("TIME")));
			             HSSFCell Cell2 = row.createCell(MapCol.get(name2));
      			         Cell2.setCellValue(StringHelper.formatObject(dx.get("MONTH")));
      			         HSSFCell Cell3 = row.createCell(MapCol.get("合计"));
      			         Cell3.setCellValue(StringHelper.formatObject(dx.get("HJ")));
      			         HSSFCell Cell4 = row.createCell(MapCol.get("发送成功"));
      			         Cell4.setCellValue(StringHelper.formatObject(dx.get("FSCG")));
      			         HSSFCell Cell5 = row.createCell(MapCol.get("发送失败"));
      			         Cell5.setCellValue(StringHelper.formatObject(dx.get("FSSB")));
      			         HSSFCell Cell6 = row.createCell(MapCol.get("合计"));
    			         Cell6.setCellValue(StringHelper.formatObject(dx.get("LHJ")));
    			         HSSFCell Cell7 = row.createCell(MapCol.get("发送成功"));
    			         Cell7.setCellValue(StringHelper.formatObject(dx.get("LFSCG")));
    			         HSSFCell Cell8 = row.createCell(MapCol.get("发送失败"));
    			         Cell8.setCellValue(StringHelper.formatObject(dx.get("LFSSB")));
	         
      			         rownum ++;
      		  		 }
      		  		 catch(Exception ex){
      		  			 
      		  		 }
				}
   			 }
   			    
   		 }
   		    wb.write(outstream); 
			 outstream.flush(); 
			 outstream.close();
   	   }
   		return url;
    }
    
    /**
     * 业务导出（鹰潭）
   
     * @return
     * @throws Exception
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
   	@RequestMapping(value = "/ywtjdownload_yt", method = RequestMethod.GET)
   	public @ResponseBody String ywtjDownload_yt(HttpServletRequest request, HttpServletResponse response) throws Exception{
   				
   		String id_sjq = request.getParameter("id_sjq");
   		String id_sjz = request.getParameter("id_sjz");
   		String qhtree = new String(request.getParameter("qhtree").getBytes("ISO-8859-1"), "utf-8");
   		String qlrtree = new String(request.getParameter("qlrtree").getBytes("ISO-8859-1"), "utf-8");
   		String djlxtree = new String(request.getParameter("djlxtree").getBytes("ISO-8859-1"), "utf-8");
   		String select = new String(request.getParameter(
				"checkbox").getBytes("ISO-8859-1"), "utf-8");
   		
   		Message m = gztjService.GetYWTJ(id_sjq,id_sjz,djlxtree,qlrtree,qhtree,select);
   		List<Map> djdys = null;
   		if (m !=null && m.getRows()!=null){
   			djdys=(List<Map>)m.getRows();
   		}
   		String basePath = request.getRealPath("/") + "\\resources\\PDF";
   		String outpath = "";
   		String url = "";
   		String tmpFullName = "";
   		FileOutputStream outstream = null;
   		if(djdys != null && djdys.size() > 0){
   			
   			if(select.equals("行政区划")){
   				outpath = basePath + "\\tmp\\ywtj_qh.xls";
   	   			url = request.getContextPath() + "\\resources\\PDF\\tmp\\ywtj_qh.xls";
   	   		    outstream = new FileOutputStream(outpath); 
   	   		    tmpFullName = request.getRealPath("/WEB-INF/jsp/wjmb/ywtj_qh.xls");
   			}else if(select.equals("登记类型")){
   				outpath = basePath + "\\tmp\\ywtj_djlx.xls";
   	   			url = request.getContextPath() + "\\resources\\PDF\\tmp\\ywtj_djlx.xls";
   	   		    outstream = new FileOutputStream(outpath); 
   	   		    tmpFullName = request.getRealPath("/WEB-INF/jsp/wjmb/ywtj_djlx.xls");
   				
   			}else if(select.equals("权利人类型")){
   				outpath = basePath + "\\tmp\\ywtj_qlr.xls";
   	   			url = request.getContextPath() + "\\resources\\PDF\\tmp\\ywtj_qlr.xls";
   	   		    outstream = new FileOutputStream(outpath); 
   	   		    tmpFullName = request.getRealPath("/WEB-INF/jsp/wjmb/ywtj_qlr.xls");
   				
   			}else if(select.equals("年度")){
   				outpath = basePath + "\\tmp\\ywtj_year.xls";
   	   			url = request.getContextPath() + "\\resources\\PDF\\tmp\\ywtj_year.xls";
   	   		    outstream = new FileOutputStream(outpath); 
   	   		    tmpFullName = request.getRealPath("/WEB-INF/jsp/wjmb/ywtj_year.xls");
   				
   			}else if(select.equals("季度")){
   				outpath = basePath + "\\tmp\\ywtj_jd.xls";
   	   			url = request.getContextPath() + "\\resources\\PDF\\tmp\\ywtj_jd.xls";
   	   		    outstream = new FileOutputStream(outpath); 
   	   		    tmpFullName = request.getRealPath("/WEB-INF/jsp/wjmb/ywtj_jd.xls");
   				
   			}else {
   				outpath = basePath + "\\tmp\\ywtj_yf.xls";
   	   			url = request.getContextPath() + "\\resources\\PDF\\tmp\\ywtj_yf.xls";
   	   		    outstream = new FileOutputStream(outpath); 
   	   		    tmpFullName = request.getRealPath("/WEB-INF/jsp/wjmb/ywtj_yf.xls");
   				
   			}
   			
   		    InputStream input = new FileInputStream(tmpFullName);
   			HSSFWorkbook  wb = null;// 定义一个新的工作簿
   			wb = new HSSFWorkbook(input);
   			Sheet sheet = wb.getSheetAt(0);
   			Map<String,Integer> MapCol = new HashMap<String,Integer>();
   			String name = "";
   			String name1 = "";
   			String str  = "";
   			String str1  = "";
   		 int rownum = 3;
   		
   		if(select.equals("行政区划") || select.equals("权利人类型") || select.equals("年度")){	
   			if(select.equals("行政区划")){
   				name = "行政区划";str="QHMC";
   			}else if(select.equals("权利人类型")){
   				name = "权利人类型";str="QLRLX";
   			}else {
   				name = "年度";str="YEAR";
   			}
   				MapCol.put(name, 0);
   				MapCol.put("受理量", 1);
   				MapCol.put("办结量", 2);
   				MapCol.put("办结率", 3);
   				MapCol.put("平均办结时间", 4);
   				MapCol.put("不动产权证", 5);
   				MapCol.put("不动产证明", 6);
   				MapCol.put("被担保债权数额", 7);
   				MapCol.put("最高债权数额", 8);
   				MapCol.put("应收金额", 9);
   				MapCol.put("实收金额", 10);
   				for(Map djdy:djdys){
   					String bjsj = String.valueOf(djdy.get("BJSJ"));
   					if( djdy.get("BJGS").equals("0")){
   						bjsj = "0";
   						
   					}
   					String lx = String.valueOf(djdy.get(str));
   					if(lx .equals("1") ){
   						lx ="个人";
   					}else if(lx .equals("2")){
   						lx ="企业";
   					}else if(lx.equals("3")){
   						lx ="事业单位";	
   					}else if(lx .equals("4")){
   						lx ="国家机关";
   					}else if(lx .equals("99")) {
   						lx ="其它";
   					}
      		  		 HSSFRow row = (HSSFRow)sheet.createRow(rownum);
      		  		 try{
      		  			
      		  			 HSSFCell Cell0 = row.createCell(MapCol.get(name));
   			             Cell0.setCellValue(StringHelper.formatObject(lx));
      			         HSSFCell Cell1 = row.createCell(MapCol.get("受理量"));
      			         Cell1.setCellValue(StringHelper.formatObject(djdy.get("SLGS")));
      			         HSSFCell Cell2 = row.createCell(MapCol.get("办结量"));
      			         Cell2.setCellValue(StringHelper.formatObject(djdy.get("BJGS")));
      			         HSSFCell Cell3 = row.createCell(MapCol.get("办结率"));
      			         Cell3.setCellValue(StringHelper.formatObject(djdy.get("BJL")));
      			         HSSFCell Cell4 = row.createCell(MapCol.get("平均办结时间"));
   			             Cell4.setCellValue(StringHelper.formatObject(bjsj));
      			         HSSFCell Cell5 = row.createCell(MapCol.get("不动产权证"));
      			         Cell5.setCellValue(StringHelper.formatObject(djdy.get("FZZSGS")));
      			         HSSFCell Cell6 = row.createCell(MapCol.get("不动产证明"));
      			         Cell6.setCellValue(StringHelper.formatObject(djdy.get("FZZMGS")));
      			         HSSFCell Cell7 = row.createCell(MapCol.get("被担保债权数额"));
    			         Cell7.setCellValue(StringHelper.formatObject(djdy.get("BDBE")));
    			         HSSFCell Cell8 = row.createCell(MapCol.get("最高债权数额"));
    			         Cell8.setCellValue(StringHelper.formatObject(djdy.get("ZGE")));
    			         HSSFCell Cell9 = row.createCell(MapCol.get("应收金额"));
    			         Cell9.setCellValue(StringHelper.formatObject(djdy.get("YSJE")));
    			         HSSFCell Cell10 = row.createCell(MapCol.get("实收金额"));
 			             Cell10.setCellValue(StringHelper.formatObject(djdy.get("SSJE")));
    			 
      			         
      			         rownum ++;
      		  		 }
      		  		 catch(Exception ex){
      		  			 
      		  		 }
      			  }	
   		}else{
   			if(select.equals("登记类型")){
   				name = "登记类型"; str  = "DJLX";
   				name1 = "权利类型";str1  = "QLLX";
   			}else if(select.equals("季度")){
   				name = "年度";str  = "YEAR";
   				name = "季度";str1  = "JD";
   			}else {
   				name = "年度";str  = "YEAR";
   				name = "月份";str1  = "YD";
   			}
   				MapCol.put(name, 0);
   				MapCol.put(name1,1);
   				MapCol.put("受理量", 2);
   				MapCol.put("办结量", 3);
   				MapCol.put("办结率", 4);
   				MapCol.put("平均办结时间", 5);
   				MapCol.put("不动产权证", 6);
   				MapCol.put("不动产证明", 7);
   				MapCol.put("被担保债权数额", 8);
   				MapCol.put("最高债权数额", 9);
   				MapCol.put("应收金额", 10);
   				MapCol.put("实收金额", 11);
   		
   				for(Map djdy:djdys){
   					String bjsj = String.valueOf(djdy.get("BJSJ"));
   					if( djdy.get("BJGS").equals("0")){
   						bjsj = "0";
   						
   					}
      		  		 HSSFRow row = (HSSFRow)sheet.createRow(rownum);
      		  		 try{
      		  			 HSSFCell Cell0 = row.createCell(MapCol.get(name));
   			             Cell0.setCellValue(StringHelper.formatObject(djdy.get(str)));
      			         HSSFCell Cell1 = row.createCell(MapCol.get(name1));
      			         Cell1.setCellValue(StringHelper.formatObject(djdy.get(str1)));
      			         HSSFCell Cell2 = row.createCell(MapCol.get("受理量"));
    			         Cell2.setCellValue(StringHelper.formatObject(djdy.get("SLGS")));
    			         HSSFCell Cell3 = row.createCell(MapCol.get("办结量"));
    			         Cell3.setCellValue(StringHelper.formatObject(djdy.get("BJGS")));
    			         HSSFCell Cell4 = row.createCell(MapCol.get("办结率"));
    			         Cell4.setCellValue(StringHelper.formatObject(djdy.get("BJL")));
    			         HSSFCell Cell5 = row.createCell(MapCol.get("平均办结时间"));
 			             Cell5.setCellValue(StringHelper.formatObject(bjsj));
    			         HSSFCell Cell6 = row.createCell(MapCol.get("不动产权证"));
    			         Cell6.setCellValue(StringHelper.formatObject(djdy.get("FZZSGS")));
    			         HSSFCell Cell7 = row.createCell(MapCol.get("不动产证明"));
    			         Cell7.setCellValue(StringHelper.formatObject(djdy.get("FZZMGS")));
    			         HSSFCell Cell8 = row.createCell(MapCol.get("被担保债权数额"));
  			             Cell8.setCellValue(StringHelper.formatObject(djdy.get("BDBE")));
  			             HSSFCell Cell9 = row.createCell(MapCol.get("最高债权数额"));
  			             Cell9.setCellValue(StringHelper.formatObject(djdy.get("ZGE")));
  			             HSSFCell Cell10 = row.createCell(MapCol.get("应收金额"));
  			             Cell10.setCellValue(StringHelper.formatObject(djdy.get("YSJE")));
  			             HSSFCell Cell11 = row.createCell(MapCol.get("实收金额"));
			             Cell11.setCellValue(StringHelper.formatObject(djdy.get("SSJE")));
      			         
      			         rownum ++;
      		  		 }
      		  		 catch(Exception ex){
      		  			 
      		  		 }
      			  }	
   			
   		}	
              
   			
   			
   			
   			 wb.write(outstream); 
   			 outstream.flush(); 
   			 outstream.close();
   		 }

           return url;
   	}
    
    
    
    /**
	 * 宗地单元房屋统计（URL:"/zddytj",Method:GET）
	 * 
	 * @作者 邹彦辉
	 * @创建时间 2016年12月21日17:36:41
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/zddytj", method = RequestMethod.GET)
	public @ResponseBody Message getzddytj(HttpServletRequest request, HttpServletResponse response) throws Exception {

		Message msg = gztjService.GetZddyTJ(request);

		return msg;
	}
    
	/**
	 * 宗地单元土地统计（URL:"/zddytjtd",Method:GET）
	 * 
	 * @作者 邹彦辉
	 * @创建时间 2016年12月21日17:36:41
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/zddytjtd/", method = RequestMethod.GET)
	public @ResponseBody Message getzddytjtd(HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		Message msg = gztjService.GetZddyTJTD(request);

		return msg;
	}
    
    
	/**
	 * 交易价格统计(房屋)（URL:"/jyjgtjfw",Method:GET）
	 * 
	 * @作者 邹彦辉
	 * @创建时间 2017年1月18日09:50:14
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/jyjgtjfw", method = RequestMethod.GET)
	public @ResponseBody Message getjyjgtjfw(HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		Message msg = gztjService.Getjyjgtjfw(request);
		return msg;
	}
    
	/**
	 * 交易价格统计(土地)（URL:"/jyjgtjtd",Method:GET）
	 * 
	 * @作者 邹彦辉
	 * @创建时间 2017年1月19日09:50:14
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/jyjgtjtd", method = RequestMethod.GET)
	public @ResponseBody Message Getjyjgtjtd(HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		Message msg = gztjService.Getjyjgttd(request);
		return msg;
	}
	
  	/**
 	 * 
 	 * 宗地单元统计房屋导出
 	 * 
 	 * @作者 邹彦辉
 	 * @param request
 	 * @param response
 	 * @return
 	 * @throws Exception
 	 */
 	@SuppressWarnings({ "deprecation", "rawtypes" })
 	@RequestMapping(value = "/zddytjfwdownload", method = RequestMethod.GET)
 	public @ResponseBody String zddytjfwDownload(HttpServletRequest request, HttpServletResponse response)
 			throws Exception {

 		String FW_FZLB = new String(request.getParameter("push").getBytes("iso8859-1"), "utf-8");

 		Message m = gztjService.GetZddyTJ(request);
 		List<Map> djdys = null;
 		if (m != null && m.getRows() != null) {
 			djdys = (List<Map>) m.getRows();
 		}
 		String basePath = request.getRealPath("/") + "\\resources\\PDF";
 		String outpath = "";
 		String url = "";
 		String tmpFullName = "";
 		FileOutputStream outstream = null;
 		if (djdys != null && djdys.size() > 0) {
 			outpath = basePath + "\\tmp\\zddyfw.xls";
 			url = request.getContextPath() + "\\resources\\PDF\\tmp\\zddyfw.xls";
 			outstream = new FileOutputStream(outpath);
 			tmpFullName = request.getRealPath("/WEB-INF/jsp/wjmb/zddyfw.xls");
 			InputStream input = new FileInputStream(tmpFullName);
 			HSSFWorkbook wb = null;// 定义一个新的工作簿
 			wb = new HSSFWorkbook(input);
 			Sheet sheet = wb.getSheetAt(0);
 			Map<String, Integer> MapCol = new HashMap<String, Integer>();
 			MapCol.put(FW_FZLB, 0);
 			MapCol.put("合计", 1);
 			MapCol.put("其中抵押数", 2);
 			MapCol.put("其中查封数", 3);
 			MapCol.put("其中异议数", 4);
 			MapCol.put("建筑面积(m2)", 5);
 			MapCol.put("套内建筑面积(m2)", 6);
 			MapCol.put("分摊建筑面积(m2)", 7);
 			MapCol.put("房地产交易金额(元)", 8);
 			int rownum = 5;
 			for (Map djdy : djdys) {
 				// 创建单元格样式
 				HSSFCellStyle cellStyleTitle = wb.createCellStyle();
 				// 指定单元格居中对齐
 				cellStyleTitle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
 				// 指定单元格垂直居中对齐
 				cellStyleTitle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
 				HSSFRow row = (HSSFRow) sheet.createRow(rownum);
 				// 定义第一行
 				HSSFRow test = (HSSFRow) sheet.createRow(1);
 				HSSFCell celltest = test.createCell(0);
 				// 第一行第一列
 				celltest.setCellStyle(cellStyleTitle);
 				celltest.setCellValue(FW_FZLB);
 				try {
 					HSSFCell Cell0 = row.createCell(MapCol.get(FW_FZLB));
 					Cell0.setCellValue(StringHelper.formatObject(djdy.get("FZLB")));
 					HSSFCell Cell1 = row.createCell(MapCol.get("合计"));
 					Cell1.setCellValue(StringHelper.formatObject(djdy.get("HJ")));
 					HSSFCell Cell2 = row.createCell(MapCol.get("其中抵押数"));
 					Cell2.setCellValue(StringHelper.formatObject(djdy.get("SFDY")));
 					HSSFCell Cell3 = row.createCell(MapCol.get("其中查封数"));
 					Cell3.setCellValue(StringHelper.formatObject(djdy.get("SFCF")));
 					HSSFCell Cell4 = row.createCell(MapCol.get("其中异议数"));
 					Cell4.setCellValue(StringHelper.formatObject(djdy.get("SFYY")));
 					HSSFCell Cell5 = row.createCell(MapCol.get("建筑面积(m2)"));
 					Cell5.setCellValue(StringHelper.formatObject(djdy.get("SCJZMJ")));
 					HSSFCell Cell6 = row.createCell(MapCol.get("套内建筑面积(m2)"));
 					Cell6.setCellValue(StringHelper.formatObject(djdy.get("SCTNJZMJ")));
 					HSSFCell Cell7 = row.createCell(MapCol.get("分摊建筑面积(m2)"));
 					Cell7.setCellValue(StringHelper.formatObject(djdy.get("SCFTJZMJ")));
 					HSSFCell Cell8 = row.createCell(MapCol.get("房地产交易金额(元)"));
 					Cell8.setCellValue(StringHelper.formatObject(djdy.get("JYJG")));
 					rownum++;
 				} catch (Exception ex) {

 				}
 			}
 			wb.write(outstream);
 			outstream.flush();
 			outstream.close();
 		}

 		return url;
 	}

 	/**
 	 * 
 	 * 宗地单元统计土地导出
 	 * 
 	 * @作者 邹彦辉
 	 * @param request
 	 * @param response
 	 * @return
 	 * @throws Exception
 	 */
 	@SuppressWarnings({ "deprecation", "rawtypes" })
 	@RequestMapping(value = "/zddytjtddownload", method = RequestMethod.GET)
 	public @ResponseBody String zddytjtdDownload(HttpServletRequest request, HttpServletResponse response)
 			throws Exception {

 		String TD_FZLB = new String(request.getParameter("push").getBytes("iso8859-1"), "utf-8");
 		
 		Message m = gztjService.GetZddyTJTD(request);
 		List<Map> djdys = null;
 		if (m != null && m.getRows() != null) {
 			djdys = (List<Map>) m.getRows();
 		}
 		String basePath = request.getRealPath("/") + "\\resources\\PDF";
 		String outpath = "";
 		String url = "";
 		String tmpFullName = "";
 		FileOutputStream outstream = null;
 		if (djdys != null && djdys.size() > 0) {
 			outpath = basePath + "\\tmp\\zddytd.xls";
 			url = request.getContextPath() + "\\resources\\PDF\\tmp\\zddytd.xls";
 			outstream = new FileOutputStream(outpath);
 			tmpFullName = request.getRealPath("/WEB-INF/jsp/wjmb/zddytd.xls");
 			InputStream input = new FileInputStream(tmpFullName);
 			HSSFWorkbook wb = null;// 定义一个新的工作簿
 			wb = new HSSFWorkbook(input);
 			Sheet sheet = wb.getSheetAt(0);
 			Map<String, Integer> MapCol = new HashMap<String, Integer>();
 			MapCol.put(TD_FZLB, 0);
 			MapCol.put("合计", 1);
 			MapCol.put("其中抵押数", 2);
 			MapCol.put("其中查封数", 3);
 			MapCol.put("其中异议数", 4);
 			MapCol.put("宗地面积(m2)", 5);
 			MapCol.put("土地取得价格(元)", 6);
 			int rownum = 5;
 			for (Map djdy : djdys) {
 				// 创建单元格样式
 				HSSFCellStyle cellStyleTitle = wb.createCellStyle();
 				// 指定单元格居中对齐
 				cellStyleTitle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
 				// 指定单元格垂直居中对齐
 				cellStyleTitle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
 				HSSFRow row = (HSSFRow) sheet.createRow(rownum);
 				// 定义第一行
 				HSSFRow test = (HSSFRow) sheet.createRow(1);
 				HSSFCell celltest = test.createCell(0);
 				// 第一行第一列
 				celltest.setCellStyle(cellStyleTitle);
 				celltest.setCellValue(TD_FZLB);
 				try {
 					HSSFCell Cell0 = row.createCell(MapCol.get(TD_FZLB));
 					Cell0.setCellValue(StringHelper.formatObject(djdy.get("FZLB")));
 					HSSFCell Cell1 = row.createCell(MapCol.get("合计"));
 					Cell1.setCellValue(StringHelper.formatObject(djdy.get("HJ")));
 					HSSFCell Cell2 = row.createCell(MapCol.get("其中抵押数"));
 					Cell2.setCellValue(StringHelper.formatObject(djdy.get("SFDY")));
 					HSSFCell Cell3 = row.createCell(MapCol.get("其中查封数"));
 					Cell3.setCellValue(StringHelper.formatObject(djdy.get("SFCF")));
 					HSSFCell Cell4 = row.createCell(MapCol.get("其中异议数"));
 					Cell4.setCellValue(StringHelper.formatObject(djdy.get("SFYY")));
 					HSSFCell Cell5 = row.createCell(MapCol.get("宗地面积(m2)"));
 					Cell5.setCellValue(StringHelper.formatObject(djdy.get("ZDMJ")));
 					HSSFCell Cell8 = row.createCell(MapCol.get("土地取得价格(元)"));
 					Cell8.setCellValue(StringHelper.formatObject(djdy.get("JYJG")));
 					rownum++;
 				} catch (Exception ex) {

 				}
 			}
 			wb.write(outstream);
 			outstream.flush();
 			outstream.close();
 		}

 		return url;
 	}

 	/**
 	 * 
 	 * 宗地单元统计房屋导出
 	 * 
 	 * @作者 邹彦辉
 	 * @param request
 	 * @param response
 	 * @return
 	 * @throws Exception
 	 */
 	@SuppressWarnings({ "deprecation", "rawtypes" })
 	@RequestMapping(value = "/jyjgtjfwdownload", method = RequestMethod.GET)
 	public @ResponseBody String jyjgtjtdDownload(HttpServletRequest request, HttpServletResponse response)
 			throws Exception {

 		String FW_FZLB = new String(request.getParameter("push").getBytes("iso8859-1"), "utf-8");
 	
 		Message m = gztjService.Getjyjgtjfw(request);

 		List<Map> djdys = null;
 		if (m != null && m.getRows() != null) {
 			djdys = (List<Map>) m.getRows();
 		}
 		String basePath = request.getRealPath("/") + "\\resources\\PDF";
 		String outpath = "";
 		String url = "";
 		String tmpFullName = "";
 		FileOutputStream outstream = null;
 		if (djdys != null && djdys.size() > 0) {
 			outpath = basePath + "\\tmp\\jyjgfw.xls";
 			url = request.getContextPath() + "\\resources\\PDF\\tmp\\jyjgfw.xls";
 			outstream = new FileOutputStream(outpath);
 			tmpFullName = request.getRealPath("/WEB-INF/jsp/wjmb/jyjgfw.xls");
 			InputStream input = new FileInputStream(tmpFullName);
 			HSSFWorkbook wb = null;// 定义一个新的工作簿
 			wb = new HSSFWorkbook(input);
 			Sheet sheet = wb.getSheetAt(0);
 			Map<String, Integer> MapCol = new HashMap<String, Integer>();
 			MapCol.put(FW_FZLB, 0);
 			MapCol.put("登记套数", 1);
 			MapCol.put("登记面积", 2);
 			MapCol.put("平均面积", 3);
 			MapCol.put("交易总价", 4);
 			MapCol.put("交易均价", 5);
 			int rownum = 4;
 			for (Map djdy : djdys) {
 				// 创建单元格样式
 				HSSFCellStyle cellStyleTitle = wb.createCellStyle();
 				// 指定单元格居中对齐
 				cellStyleTitle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
 				// 指定单元格垂直居中对齐
 				cellStyleTitle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
 				HSSFRow row = (HSSFRow) sheet.createRow(rownum);
 				// 定义第一行
 				HSSFRow test = (HSSFRow) sheet.createRow(1);
 				HSSFCell celltest = test.createCell(0);
 				// 第一行第一列
 				celltest.setCellStyle(cellStyleTitle);
 				celltest.setCellValue(FW_FZLB);
 				try {
 					HSSFCell Cell0 = row.createCell(MapCol.get(FW_FZLB));
 					Cell0.setCellValue(StringHelper.formatObject(djdy.get("FZLB")));
 					HSSFCell Cell1 = row.createCell(MapCol.get("登记套数"));
 					Cell1.setCellValue(StringHelper.formatObject(djdy.get("HJ")));
 					HSSFCell Cell2 = row.createCell(MapCol.get("登记面积"));
 					Cell2.setCellValue(StringHelper.formatObject(djdy.get("DJMJ")));
 					HSSFCell Cell3 = row.createCell(MapCol.get("平均面积"));
 					Cell3.setCellValue(StringHelper.formatObject(djdy.get("PJMJ")));
 					HSSFCell Cell4 = row.createCell(MapCol.get("交易总价"));
 					Cell4.setCellValue(StringHelper.formatObject(djdy.get("JYZJ")));
 					HSSFCell Cell5 = row.createCell(MapCol.get("交易均价"));
 					Cell5.setCellValue(StringHelper.formatObject(djdy.get("JYJG")));
 					rownum++;
 				} catch (Exception ex) {

 				}
 			}
 			wb.write(outstream);
 			outstream.flush();
 			outstream.close();
 		}
 		return url;
 	}

 	/**
 	 * 
 	 * 宗地单元统计房屋导出
 	 * 
 	 * @作者 邹彦辉
 	 * @param request
 	 * @param response
 	 * @return
 	 * @throws Exception
 	 */
 	@SuppressWarnings({ "deprecation", "rawtypes" })
 	@RequestMapping(value = "/jyjgtjtddownload", method = RequestMethod.GET)
 	public @ResponseBody String jyjgtjtddownload(HttpServletRequest request, HttpServletResponse response)
 			throws Exception {

 		String TD_FZLB = new String(request.getParameter("push").getBytes("iso8859-1"), "utf-8");
 	
 		Message m = gztjService.Getjyjgttd(request);

 		List<Map> djdys = null;
 		if (m != null && m.getRows() != null) {
 			djdys = (List<Map>) m.getRows();
 		}
 		String basePath = request.getRealPath("/") + "\\resources\\PDF";
 		String outpath = "";
 		String url = "";
 		String tmpFullName = "";
 		FileOutputStream outstream = null;
 		if (djdys != null && djdys.size() > 0) {
 			outpath = basePath + "\\tmp\\jyjgtd.xls";
 			url = request.getContextPath() + "\\resources\\PDF\\tmp\\jyjgtd.xls";
 			outstream = new FileOutputStream(outpath);
 			tmpFullName = request.getRealPath("/WEB-INF/jsp/wjmb/jyjgtd.xls");
 			InputStream input = new FileInputStream(tmpFullName);
 			HSSFWorkbook wb = null;// 定义一个新的工作簿
 			wb = new HSSFWorkbook(input);
 			Sheet sheet = wb.getSheetAt(0);
 			Map<String, Integer> MapCol = new HashMap<String, Integer>();
 			MapCol.put(TD_FZLB, 0);
 			MapCol.put("登记套数", 1);
 			MapCol.put("登记面积", 2);
 			MapCol.put("平均面积", 3);
 			MapCol.put("交易总价", 4);
 			MapCol.put("交易均价", 5);
 			int rownum = 4;
 			for (Map djdy : djdys) {
 				// 创建单元格样式
 				HSSFCellStyle cellStyleTitle = wb.createCellStyle();
 				// 指定单元格居中对齐
 				cellStyleTitle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
 				// 指定单元格垂直居中对齐
 				cellStyleTitle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
 				HSSFRow row = (HSSFRow) sheet.createRow(rownum);
 				// 定义第一行
 				HSSFRow test = (HSSFRow) sheet.createRow(1);
 				HSSFCell celltest = test.createCell(0);
 				// 第一行第一列
 				celltest.setCellStyle(cellStyleTitle);
 				celltest.setCellValue(TD_FZLB);
 				try {
 					HSSFCell Cell0 = row.createCell(MapCol.get(TD_FZLB));
 					Cell0.setCellValue(StringHelper.formatObject(djdy.get("FZLB")));
 					HSSFCell Cell1 = row.createCell(MapCol.get("登记套数"));
 					Cell1.setCellValue(StringHelper.formatObject(djdy.get("HJ")));
 					HSSFCell Cell2 = row.createCell(MapCol.get("登记面积"));
 					Cell2.setCellValue(StringHelper.formatObject(djdy.get("DJMJ")));
 					HSSFCell Cell3 = row.createCell(MapCol.get("平均面积"));
 					Cell3.setCellValue(StringHelper.formatObject(djdy.get("PJMJ")));
 					HSSFCell Cell4 = row.createCell(MapCol.get("交易总价"));
 					Cell4.setCellValue(StringHelper.formatObject(djdy.get("JYZJ")));
 					HSSFCell Cell5 = row.createCell(MapCol.get("交易均价"));
 					Cell5.setCellValue(StringHelper.formatObject(djdy.get("JYJG")));
 					rownum++;
 				} catch (Exception ex) {

 				}
 			}
 			wb.write(outstream);
 			outstream.flush();
 			outstream.close();
 		}
 		return url;
 	}
  	 
  	 
 	/**
	 * @获取地籍区地籍子区
	 * @作者 邹彦辉
	 * @创建时间 2016-12-28 14:27:36
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/getdjq", method = RequestMethod.GET)
	public @ResponseBody List<Tree> getdjq(HttpServletRequest request, HttpServletResponse response) {
		// 获取地籍区
		StringBuilder builddjq = new StringBuilder();
		List<Map> djq = null;
		builddjq.append("SELECT djq.xzqdm as djq,djq.xzqmc as djqmc FROM BDCK.BDCK_DJQ djq");
		djq = dao.getDataListByFullSql(builddjq.toString());
		// 一级目录
		List<Tree> eTrees = new ArrayList<Tree>();
		Tree dTree = new Tree();
		dTree.setId("10000");
		dTree.setText("全部");
		dTree.setChecked(false);
		// 遍历获取地籍子区 二级目录
		for (Map map : djq) {
			if(map.size()>0){
				Tree djqTree = new Tree();
				Object djqkey = map.get("DJQ") != null ? map.get("DJQ") : "";
				Object djqvalue = map.get("DJQMC") != null ? map.get("DJQMC") : "";
				StringBuilder builddjzq = new StringBuilder();
				List<Map> djzq = null;
				builddjzq
						.append("SELECT djzq.xzqdm as djzq,djzq.xzqmc as djzqmc  FROM BDCK.BDCK_DJZQ djzq where djzq.xzqdm LIKE '"
								+ djqkey.toString() + "%'");
				djzq = dao.getDataListByFullSql(builddjzq.toString());
				// 三级目录
				for (Map map2 : djzq) {
					if(map2.size()>0){
						Tree zqTree = new Tree();
						Object djzqkey = map2.get("DJZQ");
						Object djzqvalue = map2.get("DJZQMC");
						zqTree.setId(djzqkey.toString());
						zqTree.setText(djzqvalue.toString());
						if (djqTree.children == null) {
							djqTree.children = new ArrayList<Tree>();
						}
						djqTree.setId(djqkey.toString());
						djqTree.setText(djqvalue.toString());
						djqTree.children.add(zqTree);
					}
				}
				if (dTree.children == null) {
					dTree.children = new ArrayList<Tree>();
				}
				dTree.children.add(djqTree);
			}
		}
		eTrees.add(dTree);
		return eTrees;
	}

	/**
	 * 统计已办件量导出
	 * @author zhangp
	 * @data 2017年4月14日上午9:00:45
	 * @param tjsjks
	 * @param tjsjjz
	 * @param request
	 * @param reponse
	 * @return
	 * @throws IOException
	 */
  	@SuppressWarnings({ "unchecked", "rawtypes", "deprecation" })
	@RequestMapping(value="/tjlexport/{qsj}/{zsj}",method=RequestMethod.POST)
  	@ResponseBody
	public String kstjlexport(@PathVariable("qsj") String tjsjks,@PathVariable("zsj") String tjsjjz, HttpServletRequest request,HttpServletResponse reponse) throws IOException{
  		String urlString=null;
  		List<Map> info = null;
  		Message msg = null;
  		boolean isrytj = false;//是否按照人员统计
  		LinkedHashMap<String,String> map = new LinkedHashMap<String, String>();
		List<Map<String,String>> bjltj = new ArrayList<Map<String,String>>();
		String deptid = request.getParameter("deptid");
		if(null!=deptid&&!"".equals(deptid)){
			isrytj = true;
		}
		if(isrytj){
			msg = gztjService.GetKSYWTJ(tjsjks, tjsjjz, deptid);
		}else{
			msg = gztjService.GetDJZXYWTJ(tjsjks, tjsjjz);
		}
		if(msg!=null&&msg.getRows()!=null){
			info = (List<Map>)msg.getRows();
			for(Map m : info){
				Map<String,String> tmp = new HashMap<String, String>();
				if(isrytj){
					tmp.put("STAFF_NAME", m.get("STAFF_NAME")+"");
					tmp.put("YBJS", m.get("YBJS")+"");
				}else{
					tmp.put("DEPARTMENTNAME", m.get("DEPARTMENTNAME")+"");
					tmp.put("YBJS", m.get("YBJS")+"");
				}
				bjltj.add(tmp);
			}
			String basePath = request.getRealPath("/")+"\\resources\\PDF";
			String outPath = "";
			String url = "";
			String tmpFullName = "";
			if(isrytj){
				url = request.getContextPath()+"\\resources\\PDF\\tmp\\rybjltj.xls";
				outPath = basePath + "\\tmp\\rybjltj.xls";
				tmpFullName = request.getRealPath("/WEB-INF/jsp/wjmb/rybjltj.xls");
				map.put("人员", "STAFF_NAME");
				map.put("数量", "YBJS");
			}else{
				url = request.getContextPath()+"\\resources\\PDF\\tmp\\bjltj.xls";
				outPath = basePath + "\\tmp\\bjltj.xls";
				tmpFullName = request.getRealPath("/WEB-INF/jsp/wjmb/bjltj.xls");
				map.put("科室", "DEPARTMENTNAME");
				map.put("数量", "YBJS");
			}

			urlString = smMaterialService.excelDownload(url,outPath,tmpFullName,map,bjltj);
		}	
  		return urlString;
	}
  	
  ///searchcasenum
  	@SuppressWarnings({ "rawtypes", "unchecked" })
  	@RequestMapping(value = "/searchcasenum", method = RequestMethod.GET)
  	public @ResponseBody String getsearchcasenum(HttpServletRequest request, HttpServletResponse response) {
  		String result="";
  		Connection jyConnection = JH_DBHelper.getConnect_gxdjk();
  		try {
  			DataSwapImpEx dataSwapImpEx = new DataSwapImpEx();
  			ResultSet xmxxs=jyConnection.createStatement().executeQuery( "select project_id from gxdjk.gxjhxm where casenum is null ");
  			int count=0;
  			while(xmxxs.next()){
  				String project=xmxxs.getString("PROJECT_ID");
  				count=dataSwapImpEx.updategxxmbh(project);
  			}
  			result="true";
  		} catch (Exception e) {
  			result="false";
  		}
  		return result;
  	}
	
	/**
     * 批量限制房屋信息服务
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
	@RequestMapping(value="/setplxz",method=RequestMethod.GET)
	public @ResponseBody Message getPLXZ(HttpServletRequest request,HttpServletResponse response) throws Exception{
		String bdcdyid =  new String(request.getParameter("bdcdyid").getBytes("iso8859-1"),"utf-8");
		String hzt = new String(request.getParameter("hzt").getBytes("iso8859-1"),"utf-8");
		String sjq = request.getParameter("sjq");
		String sjz = request.getParameter("sjz");
		String xzyy = new String(request.getParameter("xzyy").getBytes("iso8859-1"),"utf-8");
		String xzwh = new String(request.getParameter("xzwh").getBytes("iso8859-1"),"utf-8");
		Message msg = gztjService.setPLXZ(bdcdyid, xzyy, sjq, sjz, hzt, xzwh);
		return msg;
	}
	
    @RequestMapping(value = "/cqHxx", method = RequestMethod.GET)
    public @ResponseBody Message cqHxx(HttpServletRequest request,HttpServletResponse response) throws Exception {
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
		mapCondition.put("relationid", RequestHelper.getParam(request, "relationid"));
		mapCondition.put("fwzt", RequestHelper.getParam(request, "fwzt"));
		mapCondition.put("htbh",RequestHelper.getParam(request, "htbh"));
		mapCondition.put("zl",RequestHelper.getParam(request, "zl"));
		mapCondition.put("xmbh",RequestHelper.getParam(request, "xmbh"));
    	Message msg = gztjService.cqHxx(mapCondition, page, rows);
		return msg;
    }
    
    @RequestMapping(value = "/cqHxx", method = RequestMethod.POST)
    public @ResponseBody Message extractCQHINFO(HttpServletRequest request,HttpServletResponse response) throws Exception {
    	request.setCharacterEncoding("utf-8");
    	Message msgAll = new Message();	
    	Message msg = new Message();
    	List<Map> result = new ArrayList<Map>();
    	long total = 0;
    	String msgs = "";
		//所选字段
		String bdcdyids=RequestHelper.getParam(request, "bdcdyids");
		String fwzt=RequestHelper.getParam(request, "fwzt");
		JSONArray jsonArray = JSONArray.fromObject(bdcdyids);
		List<Map> mapListJson = (List) jsonArray;
		for (int i = 0; i < mapListJson.size(); i++) {
			String bdcdyid = mapListJson.get(i).get("bdcdyid").toString();			
	    	msg = gztjService.extractCQHxx(bdcdyid,fwzt);
	    	Map map =new HashMap();
	    	if (msg.getRows()!=null && msg.getRows().size()>0) {
	    		map = (Map) msg.getRows().get(0);
	    		result.add(map);
	    		total++;    	
	    	}	    
	    	msgs += msg.getMsg();
    	}
		msg.setMsg(msgs);
		msg.setTotal(total);
		msg.setRows(result);
		return msg;
    }
    
    @RequestMapping(value= "/szzftj/{sjq}/{sjz}",method=RequestMethod.GET)
    public @ResponseBody Message getSZZFTJ(@PathVariable("sjq") String sjq,@PathVariable("sjz") String sjz,HttpServletRequest request,HttpServletResponse response){
    	Message m = gztjService.getSZZFTJ(sjq, sjz);
    	return m;
    }
    
    @RequestMapping(value="/szzfdownload",method=RequestMethod.GET)
    public @ResponseBody String getszzfdownload(HttpServletRequest request,HttpServletResponse response)throws Exception{
    	Map<String, String> mapCondition = new HashMap<String, String>();
		mapCondition.put("id_sjq", request.getParameter("id_sjq"));
		mapCondition.put("id_sjz", request.getParameter("id_sjz"));
		Message m = gztjService.getSZZFTJ(mapCondition.get("id_sjq"),mapCondition.get("id_sjz"));
		List<Map> djdys = null;
		if (m !=null && m.getRows()!=null){
			djdys=(List<Map>)m.getRows();
		}
		String basePath = request.getRealPath("/") + "\\resources\\PDF";
		String outpath = "";
		String url = "";
		String tmpFullName = "";
		FileOutputStream outstream = null;
		if(djdys != null && djdys.size() > 0){
			outpath = basePath + "\\tmp\\szzftj.xls";
			url = request.getContextPath() + "\\resources\\PDF\\tmp\\szzftj.xls";
		    outstream = new FileOutputStream(outpath); 
		    tmpFullName = request.getRealPath("/WEB-INF/jsp/wjmb/szzftj.xls");
		    InputStream input = new FileInputStream(tmpFullName);
			HSSFWorkbook  wb = null;// 定义一个新的工作簿
			wb = new HSSFWorkbook(input);
			Sheet sheet = wb.getSheetAt(0);
			Map<String,Integer> MapCol = new HashMap<String,Integer>();
			MapCol.put("姓名", 0);
			MapCol.put("缮证量", 1);
			MapCol.put("证书成功", 2);
			MapCol.put("证书作废", 3);
			MapCol.put("证明成功", 4);
			MapCol.put("证明作废", 5);
            int rownum =2;
			for(Map djdy:djdys){
				 
		  		 HSSFRow row = (HSSFRow)sheet.createRow(rownum);
		  		 try{
			         HSSFCell Cell0 = row.createCell(MapCol.get("姓名"));
			         Cell0.setCellValue(StringHelper.formatObject(djdy.get("RY")));
			         HSSFCell Cell1 = row.createCell(MapCol.get("缮证量"));
			         Cell1.setCellValue(StringHelper.formatObject(djdy.get("ZL")));
			         HSSFCell Cell2 = row.createCell(MapCol.get("证书成功"));
			         Cell2.setCellValue(StringHelper.formatObject(djdy.get("ZSL")));
			         HSSFCell Cell3 = row.createCell(MapCol.get("证书作废"));
			         Cell3.setCellValue(StringHelper.formatObject(djdy.get("ZFZSZL")));
			         HSSFCell Cell4 = row.createCell(MapCol.get("证明成功"));
			         Cell4.setCellValue(StringHelper.formatObject(djdy.get("ZML")));
			         HSSFCell Cell5 = row.createCell(MapCol.get("证明作废"));
			         Cell5.setCellValue(StringHelper.formatObject(djdy.get("ZFZMZL")));
			         rownum ++;
		  		 }
		  		 catch(Exception ex){
		  			 
		  		 }
			  }	  		
			 wb.write(outstream); 
			 outstream.flush(); 
			 outstream.close();
		 }

        return url;
    }
    
    /**
     * @param sjq
     * @param sjz
     * @param request
     * @param response
     * @author rq
     * @date 2017-5-23
     * @return
     */
    @RequestMapping(value= "/zsbhtj/{year}",method=RequestMethod.GET)
    public @ResponseBody Message getZSBHTJ(@PathVariable("year") String year,HttpServletRequest request,HttpServletResponse response){
    	Message m = gztjService.getZSBHTJ(year);
    	return m;
    }
    
    /**     	
     *不动产登记进展情况月报---鹰潭
     */
    @RequestMapping(value= "/djywtj_month/{month}",method=RequestMethod.GET)
    public @ResponseBody Message getDJYWTJ_Month(@PathVariable("month") String month,HttpServletRequest request,HttpServletResponse response){
    	Message m = gztjService.getDJYWTJByMonth(month);
    	return m;
    }
    @SuppressWarnings({ "rawtypes", "unchecked", "deprecation" })
   	@RequestMapping(value = "/bdcdjybinfodownload", method = RequestMethod.GET)
   	public @ResponseBody String BdcdjybInfoDownload(HttpServletRequest request, HttpServletResponse response) throws Exception{
   		String month = request.getParameter("month");
    	Message m = gztjService.getDJYWTJByMonth(month);
   		List<Map> rows = null;
   		if (m !=null && m.getRows()!=null){
   			 rows=(List<Map>) m.getRows();
   		}
   		String basePath = request.getRealPath("/") + "\\resources\\PDF";
   		String outpath = "";
   		String url = "";
   		String tmpFullName = "";
   		FileOutputStream outstream = null;
   		if(rows != null && rows.size() > 0){
   			outpath = basePath + "\\tmp\\bdcdjyb.xls";
   			url = request.getContextPath() + "\\resources\\PDF\\tmp\\bdcdjyb.xls";
   		    outstream = new FileOutputStream(outpath); 
   		    tmpFullName = request.getRealPath("/WEB-INF/jsp/wjmb/bdcdjyb.xls");
   		    InputStream input = new FileInputStream(tmpFullName);
   			HSSFWorkbook  wb = null;// 定义一个新的工作簿
   			wb = new HSSFWorkbook(input);
   			HSSFSheet sheet = wb.getSheetAt(0);
   			Map<String,Integer> MapCol = new HashMap<String,Integer>();
   			//获取标题行格式
   			HSSFCellStyle style = sheet.getRow(0).getCell(0).getCellStyle();
   			//添加标题行
   			HSSFRow title = (HSSFRow)sheet.getRow(0);	
   			String qhmc=ConfigHelper.getNameByValue("XZQHMC");
   			HSSFCell Cell = title.getCell(0);
   			String time = month.replace("-", "年")+"月";
   			Cell.setCellValue(time+qhmc+"不动产登记进展情况月报") ;   	
   			MapCol.put("序号", 0);
   			MapCol.put("地区", 1);
			MapCol.put("市、县", 2);
			MapCol.put("不动产权证书", 3);
			MapCol.put("不动产登记证明", 4);
			MapCol.put("小计1", 5);
			MapCol.put("首次登记1", 6);
			MapCol.put("转移登记1", 7);
			MapCol.put("变更登记1", 8);
			MapCol.put("抵押登记1", 9);
			MapCol.put("其他登记1", 10);
			MapCol.put("小计2", 11);
			MapCol.put("首次登记2", 12);
			MapCol.put("转移登记2", 13);
			MapCol.put("变更登记2", 14);
			MapCol.put("抵押登记2", 15);
			MapCol.put("其他登记2", 16);
			MapCol.put("土地1", 17);
			MapCol.put("房产1", 18);
			MapCol.put("林权1", 19);
			MapCol.put("其他类", 20);
			MapCol.put("土地2", 21);
			MapCol.put("房产2", 22);
			MapCol.put("林权2", 23);
			Row BaseCell = sheet.getRow(5);
            int rownum = 4;
   			for(Map djdy:rows){
   		  		 HSSFRow row = (HSSFRow)sheet.createRow(rownum);
   		  		 try{
   			         HSSFCell Cell0 = row.createCell(MapCol.get("序号"));
   			         Cell0.setCellStyle(BaseCell.getCell(0).getCellStyle());
   			         Cell0.setCellValue(rownum-3);
   			         HSSFCell Cell1 = row.createCell(MapCol.get("地区"));
   			         Cell1.setCellStyle(BaseCell.getCell(0).getCellStyle());
			         Cell1.setCellValue(qhmc);
   			         HSSFCell Cell2 = row.createCell(MapCol.get("市、县"));
   			         Cell2.setCellStyle(BaseCell.getCell(0).getCellStyle());
   			         Cell2.setCellValue(qhmc);
   			         HSSFCell Cell3 = row.createCell(MapCol.get("不动产权证书"));
   			         Cell3.setCellStyle(BaseCell.getCell(0).getCellStyle());
   			         Cell3.setCellValue(StringHelper.formatObject(djdy.get("ZSGS")));
   			         HSSFCell Cell4 = row.createCell(MapCol.get("不动产登记证明"));
   			         Cell4.setCellStyle(BaseCell.getCell(0).getCellStyle());
   			         Cell4.setCellValue(StringHelper.formatObject(djdy.get("ZMGS")));
   			         HSSFCell Cell5 = row.createCell(MapCol.get("小计1"));
   			         Cell5.setCellStyle(BaseCell.getCell(0).getCellStyle());
   			         Cell5.setCellValue(StringHelper.formatObject(djdy.get("SLZGS")));
   			         HSSFCell Cell6 = row.createCell(MapCol.get("首次登记1"));
   			         Cell6.setCellStyle(BaseCell.getCell(0).getCellStyle());
   			         Cell6.setCellValue(StringHelper.formatObject(djdy.get("SC_SLGS")));   			         
   			         HSSFCell Cell7 = row.createCell(MapCol.get("转移登记1"));
   			         Cell7.setCellStyle(BaseCell.getCell(0).getCellStyle());
   			         Cell7.setCellValue(StringHelper.formatObject(djdy.get("ZY_SLGS")));
   			         HSSFCell Cell8 = row.createCell(MapCol.get("变更登记1"));
   			         Cell8.setCellStyle(BaseCell.getCell(0).getCellStyle());
   			         Cell8.setCellValue(StringHelper.formatObject(djdy.get("BG_SLGS")));
   			         HSSFCell Cell9 = row.createCell(MapCol.get("抵押登记1"));
   			         Cell9.setCellStyle(BaseCell.getCell(0).getCellStyle());
   			         Cell9.setCellValue(StringHelper.formatObject(djdy.get("DY_SLGS")));
   			         HSSFCell Cell10 = row.createCell(MapCol.get("其他登记1"));
   			         Cell10.setCellStyle(BaseCell.getCell(0).getCellStyle());
   			         Cell10.setCellValue(StringHelper.formatObject(djdy.get("QT_SLGS")));
   			         HSSFCell Cell11 = row.createCell(MapCol.get("小计2"));
   			         Cell11.setCellStyle(BaseCell.getCell(0).getCellStyle());
   			         Cell11.setCellValue(StringHelper.formatObject(djdy.get("BJZGS")));
   			         HSSFCell Cell12 = row.createCell(MapCol.get("首次登记2"));
   			         Cell12.setCellStyle(BaseCell.getCell(0).getCellStyle());
   			         Cell12.setCellValue(StringHelper.formatObject(djdy.get("SC_BJGS")));
   			         HSSFCell Cell13 = row.createCell(MapCol.get("转移登记2"));
   			         Cell13.setCellStyle(BaseCell.getCell(0).getCellStyle());
   			         Cell13.setCellValue(StringHelper.formatObject(djdy.get("ZY_BJGS")));
   			         HSSFCell Cell14 = row.createCell(MapCol.get("变更登记2"));
   			         Cell14.setCellStyle(BaseCell.getCell(0).getCellStyle());
   			         Cell14.setCellValue(StringHelper.formatObject(djdy.get("BG_BJGS")));
   			         HSSFCell Cell15 = row.createCell(MapCol.get("抵押登记2"));
   			         Cell15.setCellStyle(BaseCell.getCell(0).getCellStyle());
   			         Cell15.setCellValue(StringHelper.formatObject(djdy.get("DY_BJGS")));
   			         HSSFCell Cell16 = row.createCell(MapCol.get("其他登记2"));
   			         Cell16.setCellStyle(BaseCell.getCell(0).getCellStyle());
   			         Cell16.setCellValue(StringHelper.formatObject(djdy.get("QT_BJGS")));
   			         HSSFCell Cell17 = row.createCell(MapCol.get("土地1"));
   			         Cell17.setCellStyle(BaseCell.getCell(0).getCellStyle());
   			         Cell17.setCellValue(StringHelper.formatObject(djdy.get("TD_BJGS")));
   			         HSSFCell Cell18 = row.createCell(MapCol.get("房产1"));
   			         Cell18.setCellStyle(BaseCell.getCell(0).getCellStyle());
   			         Cell18.setCellValue(StringHelper.formatObject(djdy.get("FC_BJGS")));
   			         HSSFCell Cell19 = row.createCell(MapCol.get("林权1"));
   			         Cell19.setCellStyle(BaseCell.getCell(0).getCellStyle());
   			         Cell19.setCellValue(StringHelper.formatObject(djdy.get("LQ_BJGS")));
   			         HSSFCell Cell20 = row.createCell(MapCol.get("其他类"));
   			         Cell20.setCellStyle(BaseCell.getCell(0).getCellStyle());
   			         Cell20.setCellValue(StringHelper.formatObject(djdy.get("QTKT_BJGS")));
   			         HSSFCell Cell21 = row.createCell(MapCol.get("土地2"));
   			         Cell21.setCellStyle(BaseCell.getCell(0).getCellStyle());
   			    	 Cell21.setCellValue("0");
			         HSSFCell Cell22 = row.createCell(MapCol.get("房产2"));
			         Cell22.setCellStyle(BaseCell.getCell(0).getCellStyle());
			         Cell22.setCellValue("0");
			         HSSFCell Cell23 = row.createCell(MapCol.get("林权2"));
			         Cell23.setCellStyle(BaseCell.getCell(0).getCellStyle());
			         Cell23.setCellValue("0");
   			         rownum ++;
   		  		 }
   		  		 catch(Exception ex){
   		  			 
   		  		 }
   			  }	  		
   			 wb.write(outstream); 
   			 outstream.flush(); 
   			 outstream.close();
   		 }

           return url;
   	}
}
