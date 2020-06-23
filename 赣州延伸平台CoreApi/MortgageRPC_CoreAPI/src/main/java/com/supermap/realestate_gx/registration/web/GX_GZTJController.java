package com.supermap.realestate_gx.registration.web;


import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.supermap.wisdombusiness.web.Message;

import net.sf.json.JSONArray;

import com.supermap.realestate.registration.util.RequestHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.realestate_gx.registration.service.GX_GZTJService;

/**
 * 
 * 广西统计Controller
 * 
 * @author 何开胜
 *
 * @Copyright SuperMap
 */
@Controller
@RequestMapping("/gx_gztj")
public class GX_GZTJController {
    /** 广西统计service */
    @Autowired
    private GX_GZTJService gx_gztjService;

    /**
     * 科室人员办件量统计（URL:"/ksbjltj",Method:GET）
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
    	String username = request.getParameter("blry");
    	
    	if (!StringUtils.isEmpty(username)) {
    		username = new String(username.getBytes("ISO-8859-1"),"utf-8");
    	}
    	
		Message msg = gx_gztjService.GetKSYWTJ(tjsjqs, tjsjjz, deptid, username);
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
		Message msg = gx_gztjService.GetDJZXYWTJ(tjsjqs, tjsjjz);
		return msg;
    }
    
    /**
     * liangc 获取部门下的员工
     * @param request
     * @param response
     * @return
     */
    @SuppressWarnings({ "rawtypes" })
   	@RequestMapping(value = "/getstaffnamefromdept", method = RequestMethod.GET)
   	public @ResponseBody List<Map> getDept(HttpServletRequest request, HttpServletResponse response) {
    	String deptid = request.getParameter("deptid");
   		List<Map> staffnames = gx_gztjService.GetDeptStaffname(deptid);
   		return staffnames;
   	}
    
    /**
     * 办件量统计(科室OR科室人员)（URL:"/ksbjltj",Method:GET）
     * @author:taochunda   重构办件量统计（广西）模块
     * @throws Exception
     */
    @RequestMapping(value = "/bjltj/", method = RequestMethod.GET)
    public @ResponseBody Message getBJLTJ(HttpServletRequest request,HttpServletResponse response) throws Exception {
    	request.setCharacterEncoding("utf-8");
		Map<String, String> mapCondition = new HashMap<String, String>();
		mapCondition.put("type", request.getParameter("type"));
		mapCondition.put("startDate", request.getParameter("startDate"));
		mapCondition.put("endDate", request.getParameter("endDate"));
		mapCondition.put("deptid", request.getParameter("deptid"));
		mapCondition.put("staffid", request.getParameter("staffid"));
		Message msg = gx_gztjService.GetBJLTJ(mapCondition);
    	
		return msg;
    }
    
    /** 
	 * 办件量统计（广西）导出
	 * @作者 taochunda
	 * @创建时间 2018年01月31日
	 * @return 
	 * @throws Exception 
	 * 
	 */
	@SuppressWarnings({ "deprecation", "rawtypes", "unchecked" })
	@RequestMapping(value = "/bjltj/export", method = RequestMethod.GET)
	public @ResponseBody String DYTZExport(HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("utf-8");
		Map<String, String> mapCondition = new HashMap<String, String>();
		mapCondition.put("type", request.getParameter("type"));
		mapCondition.put("startDate", request.getParameter("startDate"));
		mapCondition.put("endDate", request.getParameter("endDate"));
		mapCondition.put("deptid", request.getParameter("deptid"));
		mapCondition.put("staffid", request.getParameter("staffid"));
		Message message = gx_gztjService.GetBJLTJ(mapCondition);
		List<Map> rows=(List<Map>) message.getRows();
		
		//导出
		String basePath = request.getRealPath("/") + "\\resources\\PDF";
		String outpath = "";
		String url = "";
		String xlsName = "bjltj.xls";//科室办件量导出模板
		if ("RY".equals(request.getParameter("type"))) {
			xlsName = "rybjltj.xls";//人员办件量导出模板
		}
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
     * 柳州月业务办理量统计
     * 
     * @return
     * @author hpf
     * @date 20180328
     */
    @RequestMapping(value = "/yywblltj", method = RequestMethod.GET)
    public @ResponseBody Message getYYWBLLTJ(HttpServletRequest request, HttpServletResponse response) throws Exception {
    	
         Map<String, Object> mapCondition = new HashMap<String, Object>();
         mapCondition.put("QSSJ", request.getParameter("QSSJ"));
         mapCondition.put("JSSJ", request.getParameter("JSSJ"));
         Message msg = gx_gztjService.getDataYYWBLLTJ(mapCondition);
         request.getSession().setAttribute("YYWBLLTJExcel", msg.getRows());//匹配结果集
		
         return msg;
    }
    
    /**
     * 柳州月业务办理量统计填充Excel
     * 
     * @return
     * @author hpf
     * @date 20180328
     */
    @SuppressWarnings({ "deprecation", "unused", "rawtypes", "unchecked"})
    @RequestMapping(value = "/yywblltjExl", method = RequestMethod.POST)
    public @ResponseBody String getYYWBLLTJExl(HttpServletRequest request, HttpServletResponse response) throws Exception {
    	String basePath = request.getRealPath("/") + "\\resources\\PDF";
   		String outpath = "";
   		String url = "";
   		String tmpFullName = "";
   		FileOutputStream outstream = null;
   		
   		outpath = basePath + "\\tmp\\yywblltj_lz.xls";
   		//下载后存放新Excle的路径
   		url = request.getContextPath() + "\\resources\\PDF\\tmp\\yywblltj_lz.xls";
   		outstream = new FileOutputStream(outpath);
   		//模版Excle表的路径
   		 tmpFullName = request.getRealPath("/WEB-INF/jsp/wjmb/yywblltj_lz.xls");
   		 InputStream input = new FileInputStream(tmpFullName);
   		HSSFWorkbook  wb = null;// 定义一个新的工作簿
   	      wb = new HSSFWorkbook(input);
   		HSSFSheet sheet=wb.getSheetAt(0);//选取Excle模版的第1个sheet
   			
   		// 创建单元格，并设置值表头 设置表头居中
   			HSSFCellStyle style = wb.createCellStyle();
   			style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
   			style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER); // 创建一个居中格式
   			
   			Map<String,Integer> MapCol = new HashMap<String,Integer>();
   			List<Map> newExclelists = new ArrayList<Map>();	
   			int rownum = 0;
   			List<Map> intoExclelists =  (List<Map>) request.getSession().getAttribute("YYWBLLTJExcel");			
   			newExclelists.addAll(intoExclelists);
   			for(Map mp : newExclelists){
   				if(mp.containsKey("QSSJ") && mp.containsKey("JSSJ")){
   					Map date = new HashMap();
   					date.put("QSSJ", mp.get("QSSJ"));
   					date.put("JSSJ", mp.get("JSSJ"));
   					newExclelists.add(0, date);
   					break;
   				}
   			}
   			newExclelists.add(1, new HashMap());
   			newExclelists.add(2, new HashMap());
   			newExclelists.add(3, new HashMap());
   			if(newExclelists.size()>0){
   				for(Map map : newExclelists ){   				
    				//获取每一个对象中的值
    			   System.out.println(map);
    			    HSSFRow row = sheet.getRow(rownum);//得到工作表的行
    			    if(rownum==0 && map.containsKey("QSSJ") && map.containsKey("JSSJ")){
    			          HSSFFont font = wb.createFont();
    			          font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);//粗体显示    
    			          font.setFontHeightInPoints((short) 15);  
    			          style.setFont(font);
    			          String headline = "柳州市不动产登记中心业务办理统计表";
    			          String qssj = StringHelper.formatObject(map.get("QSSJ")).replace("-", "");
    			          String jssj = StringHelper.formatObject(map.get("JSSJ")).replace("-", "");
    			          HSSFCell Cell1_1 = row.createCell((short) 1);
    			          Cell1_1.setCellValue(headline+qssj+"-"+jssj); 
    			          Cell1_1.setCellStyle(style);
    			    }		    
    			    if(rownum>3){
    			           HSSFCell Cell4 = row.createCell((short) 4);
    			           Cell4.setCellValue(StringHelper.getInt(map.get("SLL")));
    			           
    			           HSSFCell Cell5 = row.createCell((short) 5);
    			           Cell5.setCellValue(StringHelper.getInt(map.get("DBL"))); 
 	            
    			           HSSFCell Cell6 = row.createCell((short) 6);
    			           Cell6.setCellValue(StringHelper.getInt(map.get("ZS_SZL")));
 	            
    			           HSSFCell Cell7 = row.createCell((short) 7);
    			           Cell7.setCellValue( StringHelper.getInt(map.get("ZM_SZL")));
 	            
    			           HSSFCell Cell8 = row.createCell((short) 8);
    			           Cell8.setCellValue(StringHelper.getInt(map.get("ZS_FZL")));
 	   
    			           HSSFCell Cell9 = row.createCell((short) 9);
    			           Cell9.setCellValue( StringHelper.getInt(map.get("ZM_FZL")));
    			    }
    			     	            
    	            rownum += 1;
    	            System.out.println("行数"+row.getRowNum());
    			}
   			}	
   			
   			 wb.write(outstream);
   			 outstream.flush(); 
   			 outstream.close();
   			 
   		return url;
   	}
		
     /**
     * 柳州半个月办件量统计
     * 
     * @return
     * @author hpf
     * @date 20180330
     */
    @RequestMapping(value = "/bybjltj", method = RequestMethod.GET)
    public @ResponseBody Message getBYBJLTJ(HttpServletRequest request, HttpServletResponse response) throws Exception {
    	
    		Map<String, Object> mapCondition = new HashMap<String, Object>();
    		mapCondition.put("QSSJ", request.getParameter("QSSJ"));
    		mapCondition.put("JSSJ", request.getParameter("JSSJ"));
    
    		Message msg = gx_gztjService.getDataBYBJLTJ(mapCondition);
    		request.getSession().setAttribute("BYBJLTJExcel", msg.getRows());//匹配结果集
    	
    		return msg;
    }
    /**
     * 柳州半个月办件量统计填充Excel
     * 
     * @return
     * @author hpf
     * @date 20180331
     */
    @SuppressWarnings({ "deprecation", "unused", "rawtypes", "unchecked"})
    @RequestMapping(value = "/bybjltjExl", method = RequestMethod.POST)
    public @ResponseBody String getBYBJLTJExl(HttpServletRequest request, HttpServletResponse response) 
    		throws Exception {
    		
    	String basePath = request.getRealPath("/") + "\\resources\\PDF";
   		String outpath = "";
   		String url = "";
   		String tmpFullName = "";
   		FileOutputStream outstream = null;
   		
   		outpath = basePath + "\\tmp\\bybjltj_lz.xls";
   		//下载后存放新Excle的路径
   		url = request.getContextPath() + "\\resources\\PDF\\tmp\\bybjltj_lz.xls";
   		outstream = new FileOutputStream(outpath);
   		//模版Excle表的路径
   		 tmpFullName = request.getRealPath("/WEB-INF/jsp/wjmb/bybjltj_lz.xls");
   		 InputStream input = new FileInputStream(tmpFullName);
   		HSSFWorkbook  wb = null;// 定义一个新的工作簿
   	 wb = new HSSFWorkbook(input);
   		HSSFSheet sheet=wb.getSheetAt(0);//选取Excle模版的第1个sheet
   			
   		// 创建单元格，并设置值表头 设置表头居中
   			HSSFCellStyle style = wb.createCellStyle();
   			style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
   			style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER); // 创建一个居中格式
   			
   			List<Map> newExclelists = new ArrayList<Map>();	
   			int rownum = 0;
   			List<Map> intoExclelists =  (List<Map>) request.getSession().getAttribute("BYBJLTJExcel");			
   			newExclelists.addAll(intoExclelists);
   			for(Map mp : newExclelists){
   				if(mp.containsKey("QSSJ") && mp.containsKey("JSSJ")){
   					Map date = new HashMap();
   					date.put("QSSJ", mp.get("QSSJ"));
   					date.put("JSSJ", mp.get("JSSJ"));
   					newExclelists.add(0, date);
   					break;
   				}
   			}
   			newExclelists.add(1, new HashMap());
   			newExclelists.add(2, new HashMap());
   			newExclelists.add(3, new HashMap());
   			newExclelists.add(4, new HashMap());
   			for(Map map : newExclelists ){   				
   				//获取每一个对象中的值
   			   System.out.println(map);
   			    HSSFRow row = sheet.getRow(rownum);//得到工作表的行
   			    if(rownum==0 && map.containsKey("JSSJ")){
   			          HSSFFont font = wb.createFont();
   			          font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);//粗体显示    
   			          font.setFontHeightInPoints((short) 11);  
   			          style.setFont(font);
   			          String headline = "不动产登记工作进展统计系统--不动产登记完善规范情况基础表（截止****日）";
   			          String jssj = "";
   			          if(!StringHelper.isEmpty(map.get("JSSJ"))){
   			          	jssj = map.get("JSSJ").toString().replaceFirst("-", "年").replaceFirst("-", "月");
   			          }
   			          HSSFCell Cell1_0 = row.createCell((short) 0);
   			          Cell1_0.setCellValue(headline.replace("****", jssj)); 
   			          Cell1_0.setCellStyle(style);
   			    }		    
   			    if(rownum>4){
   			           HSSFCell Cell2 = row.createCell((short) 2);
   			           Cell2.setCellValue(StringHelper.getInt(map.get("DBL")));
   			           
   			           HSSFCell Cell3 = row.createCell((short) 3);
   			           Cell3.setCellValue(StringHelper.getInt(map.get("ZSZL"))); 
	            
   			           HSSFCell Cell4 = row.createCell((short) 4);
   			           Cell4.setCellValue(StringHelper.getInt(map.get("ZMZL")));
	            
   			    }
   			     	            
   	            rownum += 1;
   	            System.out.println("行数"+row.getRowNum());
   			}
   			
   			 wb.write(outstream);
   			 outstream.flush(); 
   			 outstream.close();
    	
    	return url;
    }
    /**
     * 柳州中心业务登簿量统计
     * 
     * @return
     * @author hpf
     * @date 20180402
     */
    @RequestMapping(value = "/zxywdbltj", method = RequestMethod.GET)
    public @ResponseBody Message getZXYWDBLTJ(HttpServletRequest request, HttpServletResponse response) throws Exception {
    	
    		Map<String, Object> mapCondition = new HashMap<String, Object>();
    		mapCondition.put("QSSJ", request.getParameter("QSSJ"));
    		mapCondition.put("JSSJ", request.getParameter("JSSJ"));
    		
    		Message msg = gx_gztjService.getDataZXYWDBLTJ(mapCondition);
    		request.getSession().setAttribute("ZXYWDBLTJExcel", msg.getRows());//匹配结果集
    	
    		return msg;
    }
    /**
     * 柳州中心业务登簿量统计填充Excel
     * 
     * @return
     * @author hpf
     * @date 20180402
     */
    @SuppressWarnings({ "deprecation", "unused", "rawtypes", "unchecked"})
    @RequestMapping(value = "/zxywdbltjExl", method = RequestMethod.POST)
    public @ResponseBody String getZXYWDBLExl(HttpServletRequest request, HttpServletResponse response) 
    		throws Exception {
    		
    	String basePath = request.getRealPath("/") + "\\resources\\PDF";
   		String outpath = "";
   		String url = "";
   		String tmpFullName = "";
   		FileOutputStream outstream = null;
   		
   		outpath = basePath + "\\tmp\\zxywdbltj_lz.xls";
   		//下载后存放新Excle的路径
   		url = request.getContextPath() + "\\resources\\PDF\\tmp\\zxywdbltj_lz.xls";
   		outstream = new FileOutputStream(outpath);
   		//模版Excle表的路径
   		 tmpFullName = request.getRealPath("/WEB-INF/jsp/wjmb/zxywdbltj_lz.xls");
   		 InputStream input = new FileInputStream(tmpFullName);
   		HSSFWorkbook  wb = null;// 定义一个新的工作簿
   	 wb = new HSSFWorkbook(input);
   		HSSFSheet sheet=wb.getSheetAt(0);//选取Excle模版的第1个sheet
   			
   			List<Map> newExclelists = new ArrayList<Map>();	
   			int rownum = 0;
   			List<Map> intoExclelists =  (List<Map>) request.getSession().getAttribute("ZXYWDBLTJExcel");			
   			newExclelists.addAll(intoExclelists);
   			for(Map mp : newExclelists){
   				if(mp.containsKey("QSSJ") && mp.containsKey("JSSJ")){
   					Map date = new HashMap();
   					date.put("QSSJ", mp.get("QSSJ"));
   					date.put("JSSJ", mp.get("JSSJ"));
   					newExclelists.add(0, date);
   					break;
   				}
   			}
   			newExclelists.add(1, new HashMap());
   			for(Map map : newExclelists ){   				
   				//获取每一个对象中的值
   			  System.out.println(map);
   			  HSSFRow row = sheet.getRow(rownum);//得到工作表的行
   			  if(rownum==0 && map.containsKey("JSSJ")){
   			   	String qssj = StringHelper.formatObject(map.get("QSSJ")).replace("-", "");
   			    String jssj = StringHelper.formatObject(map.get("JSSJ")).replace("-", "");
   			    HSSFCell Cell1_1 = row.createCell((short) 1);
   			    Cell1_1.setCellValue(qssj+"-"+jssj); 
   			  }		    
   			  if(rownum>1){
   			    HSSFCell Cell1 = row.createCell((short) 1);
   			    Cell1.setCellValue(StringHelper.getInt(map.get("DBL")));
   			  }
   			     	            
   	    rownum += 1;
   	    System.out.println("行数"+row.getRowNum());
   			}
   			
   			 wb.write(outstream);
   			 outstream.flush(); 
   			 outstream.close();
    	
    	return url;
    }
    /**
     * 柳州推送税局、住建统计
     * 
     * @return
     * @author hpf
     * @date 20180402
     */
    @RequestMapping(value = "/tssjzjtj", method = RequestMethod.GET)
    public @ResponseBody Message getTSSJZJTJ(HttpServletRequest request, HttpServletResponse response) throws Exception {
    	
    		Map<String, Object> mapCondition = new HashMap<String, Object>();
    		mapCondition.put("QSSJ", request.getParameter("QSSJ"));
    		mapCondition.put("JSSJ", request.getParameter("JSSJ"));
    		
    		Message msg = gx_gztjService.getDataTSSJZJTJ(mapCondition);
    		request.getSession().setAttribute("TSSJZJTJExcel", msg.getRows());//匹配结果集
    	
    		return msg;
    }
    /**
     * 柳州推送税局、住建统计填充Excel
     * 
     * @return
     * @author hpf
     * @date 20180402
     */
    @SuppressWarnings({ "deprecation", "unused", "rawtypes", "unchecked"})
    @RequestMapping(value = "/tssjzjtjExl", method = RequestMethod.POST)
    public @ResponseBody String getTSSJZJTJExl(HttpServletRequest request, HttpServletResponse response) 
    		throws Exception {
    		
    	String basePath = request.getRealPath("/") + "\\resources\\PDF";
   		String outpath = "";
   		String url = "";
   		String tmpFullName = "";
   		FileOutputStream outstream = null;
   		
   		outpath = basePath + "\\tmp\\tssjzjtj_lz.xls";
   		//下载后存放新Excle的路径
   		url = request.getContextPath() + "\\resources\\PDF\\tmp\\tssjzjtj_lz.xls";
   		outstream = new FileOutputStream(outpath);
   		//模版Excle表的路径
   		 tmpFullName = request.getRealPath("/WEB-INF/jsp/wjmb/tssjzjtj_lz.xls");
   		 InputStream input = new FileInputStream(tmpFullName);
   		HSSFWorkbook  wb = null;// 定义一个新的工作簿
   	 wb = new HSSFWorkbook(input);
   		HSSFSheet sheet=wb.getSheetAt(0);//选取Excle模版的第1个sheet

   			int rownum = 1;
   			List<Map> intoExclelists =  (List<Map>) request.getSession().getAttribute("TSSJZJTJExcel");
   			if(intoExclelists != null){
   				for(Map map : intoExclelists){   				
    				//获取每一个对象中的值
    			  System.out.println(map);
    			  HSSFRow row = sheet.getRow(rownum);//得到工作表的行
    			  
    			  HSSFCell Cell0 = row.createCell((short) 0);
    			  Cell0.setCellValue(StringHelper.formatObject(map.get("SJ")));
    			  
    			  HSSFCell Cell1 = row.createCell((short) 1);
    			  Cell1.setCellValue(StringHelper.formatObject(map.get("TJXM")));
    			  
    			  HSSFCell Cell2 = row.createCell((short) 2);
    			  Cell2.setCellValue(StringHelper.getInt(map.get("SL")));
    		            
    	    rownum += 1;
    	    System.out.println("行数"+row.getRowNum());
    			}
   			}
   			 wb.write(outstream);
   			 outstream.flush(); 
   			 outstream.close();
    	
    	return url;
    }
    /**
     * 柳州报件、自助、批量统计
     * 
     * @return
     * @author hpf
     * @date 20180402
     */
    @RequestMapping(value = "/bjzzpltj", method = RequestMethod.GET)
    public @ResponseBody Message getBJZZPLTJ(HttpServletRequest request, HttpServletResponse response) throws Exception {
    	
    		Map<String, Object> mapCondition = new HashMap<String, Object>();
    		mapCondition.put("QSSJ", request.getParameter("QSSJ"));
    		mapCondition.put("JSSJ", request.getParameter("JSSJ"));
    		
    		Message msg = gx_gztjService.getDataBJZZPLTJ(mapCondition);
    		request.getSession().setAttribute("BJZZPLTJExcel", msg.getRows());//匹配结果集
    	
    		return msg;
    }
    /**
     * 柳州报件、自助、批量统计填充Excel
     * 
     * @return
     * @author hpf
     * @date 20180402
     */
    @SuppressWarnings({ "deprecation", "unused", "rawtypes", "unchecked"})
    @RequestMapping(value = "/bjzzpltjExl", method = RequestMethod.POST)
    public @ResponseBody String getBJZZPLTJExl(HttpServletRequest request, HttpServletResponse response) 
    		throws Exception {
    		
    	String basePath = request.getRealPath("/") + "\\resources\\PDF";
   		String outpath = "";
   		String url = "";
   		String tmpFullName = "";
   		FileOutputStream outstream = null;
   		
   		outpath = basePath + "\\tmp\\bjzzpltj_lz.xls";
   		//下载后存放新Excle的路径
   		url = request.getContextPath() + "\\resources\\PDF\\tmp\\bjzzpltj_lz.xls";
   		outstream = new FileOutputStream(outpath);
   		//模版Excle表的路径
   		 tmpFullName = request.getRealPath("/WEB-INF/jsp/wjmb/bjzzpltj_lz.xls");
   		 InputStream input = new FileInputStream(tmpFullName);
   		HSSFWorkbook  wb = null;// 定义一个新的工作簿
   	 wb = new HSSFWorkbook(input);
   		HSSFSheet sheet=wb.getSheetAt(0);//选取Excle模版的第1个sheet

   			int rownum = 1;
   			List<Map> intoExclelists = (List<Map>) request.getSession().getAttribute("BJZZPLTJExcel");
   			if(intoExclelists != null){
   				for(Map map : intoExclelists){   				
    				//获取每一个对象中的值
    			  System.out.println(map);
    			  HSSFRow row = sheet.getRow(rownum);//得到工作表的行
    			  
    			  HSSFCell Cell0 = row.createCell((short) 0);
    			  Cell0.setCellValue(StringHelper.formatObject(map.get("SJ")));
    			  
    			  HSSFCell Cell1 = row.createCell((short) 1);
    			  Cell1.setCellValue(StringHelper.formatObject(map.get("TJXM")));
    			  
    			  HSSFCell Cell2 = row.createCell((short) 2);
    			  Cell2.setCellValue(StringHelper.getInt(map.get("SL")));
    		            
    	    rownum += 1;
    	    System.out.println("行数"+row.getRowNum());
    			}
   			}
   			 wb.write(outstream);
   			 outstream.flush(); 
   			 outstream.close();
    	
    	return url;
    }
    /**
     * 柳州雒容、柳地、南铁房换证登记统计
     * 
     * @return
     * @author hpf
     * @date 20180402
     */
    @RequestMapping(value = "/lrldnttj", method = RequestMethod.GET)
    public @ResponseBody Message getLRLDNTTJ(HttpServletRequest request, HttpServletResponse response) throws Exception {
    	
    		Map<String, Object> mapCondition = new HashMap<String, Object>();
    		mapCondition.put("QSSJ", request.getParameter("QSSJ"));
    		mapCondition.put("JSSJ", request.getParameter("JSSJ"));
    		
    		Message msg = gx_gztjService.getDataLRLDNTTJ(mapCondition);
    		request.getSession().setAttribute("LRLDNTTJExcel", msg.getRows());//匹配结果集
    	
    		return msg;
    }
    /**
     * 柳州雒容、柳地、南铁房换证登记统计填充Excel
     * 
     * @return
     * @author hpf
     * @date 20180402
     */
    @SuppressWarnings({ "deprecation", "unused", "rawtypes", "unchecked"})
    @RequestMapping(value = "/lrldnttjExl", method = RequestMethod.POST)
    public @ResponseBody String getLRLDNTTJExl(HttpServletRequest request, HttpServletResponse response) 
    		throws Exception {
    		
    	String basePath = request.getRealPath("/") + "\\resources\\PDF";
   		String outpath = "";
   		String url = "";
   		String tmpFullName = "";
   		FileOutputStream outstream = null;
   		
   		outpath = basePath + "\\tmp\\lrldnttj_lz.xls";
   		//下载后存放新Excle的路径
   		url = request.getContextPath() + "\\resources\\PDF\\tmp\\lrldnttj_lz.xls";
   		outstream = new FileOutputStream(outpath);
   		//模版Excle表的路径
   		 tmpFullName = request.getRealPath("/WEB-INF/jsp/wjmb/lrldnttj_lz.xls");
   		 InputStream input = new FileInputStream(tmpFullName);
   		HSSFWorkbook  wb = null;// 定义一个新的工作簿
   	 wb = new HSSFWorkbook(input);
   		HSSFSheet sheet=wb.getSheetAt(0);//选取Excle模版的第1个sheet

   			int rownum = 2;
   			List<Map> intoExclelists =  (List<Map>) request.getSession().getAttribute("LRLDNTTJExcel");			
   			if(intoExclelists != null){
   				for(Map map : intoExclelists){   				
    				//获取每一个对象中的值
    			  System.out.println(map);
    			  HSSFRow row = sheet.getRow(rownum);//得到工作表的行
    			  
    			  HSSFCell Cell1 = row.createCell((short) 1);
    			  Cell1.setCellValue(StringHelper.formatObject(map.get("SLL")));
    			  
    			  HSSFCell Cell2 = row.createCell((short) 2);
    			  Cell2.setCellValue(StringHelper.getInt(map.get("SZL")));
    			  
    			  HSSFCell Cell3 = row.createCell((short) 3);
    			  Cell3.setCellValue(StringHelper.formatObject(map.get("SJ")));
    		            
    	    rownum += 1;
    	    System.out.println("行数"+row.getRowNum());
    			}
   			}
   			 wb.write(outstream);
   			 outstream.flush(); 
   			 outstream.close();
    	
    	return url;
    }
    
    /**
     * 办结统计(按月)（Method:GET）
     * @author:luml   
     * @throws Exception
     */
    @RequestMapping(value = "/banjie_tj/{startTime}/{endTime}", method = RequestMethod.GET)
    public @ResponseBody Message getBanJie_TJ(@PathVariable("startTime") String startTime,@PathVariable("endTime") String endTime,HttpServletRequest request,HttpServletResponse response) throws Exception {
    	request.setCharacterEncoding("utf-8");
		Message msg = gx_gztjService.getBanJie_TJ( startTime,  endTime);
		return msg;
    }
    
    /** 
	 * 办结统计(按月)导出
	 * @作者 luml
	 * @return 
	 * @throws Exception 
	 * 
	 */
	@SuppressWarnings({ "deprecation", "rawtypes", "unchecked" })
	@RequestMapping(value = "/banjie_tj/export/{startTime}/{endTime}", method = RequestMethod.GET)
	public @ResponseBody String BANJIEExport(@PathVariable("startTime") String startTime,@PathVariable("endTime") String endTime,HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("utf-8");
		Message message = gx_gztjService.getBanJie_TJ(startTime , endTime);
		List<Map> rows=(List<Map>) message.getRows();
		String basePath = request.getRealPath("/") + "\\resources\\PDF";
		String outpath = "";
		String url = "";
		FileOutputStream outstream = null;
		outpath = basePath + "\\tmp\\banjie_tj.xls";
		url = request.getContextPath() + "\\resources\\PDF\\tmp\\banjie_tj.xls";
	    outstream = new FileOutputStream(outpath); 

	    String tplFullName = request.getRealPath("/WEB-INF/jsp/wjmb/banjie_tj.xls");
		    InputStream input = new FileInputStream(tplFullName);
			HSSFWorkbook  wb = null;// 定义一个新的工作簿
			wb = new HSSFWorkbook(input);
			Sheet sheet = wb.getSheetAt(0);
			Map<String,Integer> MapCol = new HashMap<String,Integer>();
			MapCol.put("序号", 0);
			MapCol.put("年月", 1);
			MapCol.put("业务类型", 2);
			MapCol.put("本月办结总量", 3);
			MapCol.put("本月平均办结时间（天）", 4);
			MapCol.put("超期件数",5);
            int rownum = 4;
		for (int i = 0; i < rownum; i++) {
			HSSFRow row = (HSSFRow) sheet.createRow(i + 2);
			HSSFCell Cell0 = row.createCell(MapCol.get("序号"));
			Cell0.setCellValue(i + 1);
			HSSFCell Cell1 = row.createCell(MapCol.get("年月"));
			//Cell1.setCellValue(request.getParameter("date"));
			Cell1.setCellValue(StringHelper.FormatByDatatype(rows.get(0).get("YF")));
			HSSFCell Cell2 = row.createCell(MapCol.get("业务类型"));
			HSSFCell Cell3 = row.createCell(MapCol.get("本月办结总量"));
			HSSFCell Cell4 = row.createCell(MapCol.get("本月平均办结时间（天）"));
			HSSFCell Cell5 = row.createCell(MapCol.get("超期件数"));
			if (1 == i + 1) {
				Cell2.setCellValue("首次登记-抵押权");
				Cell3.setCellValue(StringHelper.FormatByDatatype(rows.get(0).get("SC_DIYA_SL")));
				Cell4.setCellValue(StringHelper.FormatByDatatype(rows.get(0).get("SC_DIYA_PJSJ")));
				Cell5.setCellValue(StringHelper.FormatByDatatype(rows.get(0).get("CQ_SC_DIYA")));
			}
			if (2 == i + 1) {
				Cell2.setCellValue("首次登记-国有建设用地使用权");
				Cell3.setCellValue(StringHelper.FormatByDatatype(rows.get(0).get("SC_JSYD_SL")));
				Cell4.setCellValue(StringHelper.FormatByDatatype(rows.get(0).get("SC_JSYD_PJSJ")));
				Cell5.setCellValue(StringHelper.FormatByDatatype(rows.get(0).get("CQ_SC_JSYD")));
			}
			if (3 == i + 1) {
				Cell2.setCellValue("首次登记-国有建设用地使用权/房屋所有权");
				Cell3.setCellValue(StringHelper.FormatByDatatype(rows.get(0).get("SC_DHF_SL")));
				Cell4.setCellValue(StringHelper.FormatByDatatype(rows.get(0).get("SC_DHF_PJSJ")));
				Cell5.setCellValue(StringHelper.FormatByDatatype(rows.get(0).get("CQ_SC_DHF")));
			}
			if (4 == i + 1) {
				Cell2.setCellValue("转移登记-国有建设用地使用权/房屋所有权");
				Cell3.setCellValue(StringHelper.FormatByDatatype(rows.get(0).get("ZY_DHF_SL")));
				Cell4.setCellValue(StringHelper.FormatByDatatype(rows.get(0).get("ZY_DHF_PJSJ")));
				Cell5.setCellValue(StringHelper.FormatByDatatype(rows.get(0).get("CQ_ZY_DHF")));
			}

		}
			    wb.write(outstream); 
			    outstream.flush(); 
			    outstream.close();
			    outstream = null;
			    return url;
	}
	/**
	 * 超期详情   luml
	 * @param startTime
	 * @param endTime
	 * @param djlx
	 * @param qllx
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	 @RequestMapping(value = "/banjie_cqxq/{startTime}/{endTime}/{djlx}/{qllx}", method = RequestMethod.GET)
	 public @ResponseBody Message getBanJie_CQXQ(@PathVariable("startTime") String startTime,@PathVariable("endTime") String endTime,
			 @PathVariable("djlx") String djlx,@PathVariable("qllx") String qllx,HttpServletRequest request,HttpServletResponse response) throws Exception {
	    	request.setCharacterEncoding("utf-8");
			Message msg = gx_gztjService.getBanJie_CQXQ( startTime,  endTime, djlx, qllx);
			return msg;
	}
}
