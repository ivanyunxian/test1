package com.supermap.realestate_gx.registration.web;

import com.supermap.realestate.registration.util.ConstValue;
import com.supermap.realestate.registration.util.ConstValue.DJLX;
import com.supermap.realestate.registration.util.ConstValue.QLLX;
import com.supermap.realestate.registration.util.RequestHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.realestate.registration.util.YwLogUtil;
import com.supermap.realestate_gx.registration.service.GX_ArchivesWhthinService;
import com.supermap.realestate_gx.registration.service.QueryService;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import com.supermap.wisdombusiness.framework.model.User;
import com.supermap.wisdombusiness.framework.service.UserService;
import com.supermap.wisdombusiness.web.Message;
import net.sf.json.JSONArray;
import org.apache.poi.hssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * 
 * 广西档案查档证明Controller
 * 
 * @author 何开胜
 *
 * @Copyright SuperMap
 */
@Controller
@RequestMapping("/gx_dacd")
public class GX_ArchivesWhthinController {

	//用户service
	@Autowired
	private UserService userService;
	@Autowired
	private CommonDao baseCommonDao;
	@Autowired
	private QueryService queryService_gx;
	@Autowired
	private GX_ArchivesWhthinService gx_dacdService;
	/** 页面跳转路径 */
	private final String prefix = "/realestate_gx/registration/";

	/**
	 * 跳转到匹配权利人信息的页面
	 * @author hks
	 * @创建时间 2016/6/21 10:24
	 * @return
	 */
	@RequestMapping(value = "/qlrmatchdata", method = RequestMethod.GET)
	public String matchQlrInfo(){
		return "/realestate_gx/registration/djywcx/importQlrInfo";		
	}
	
	/**
	 * 购房补贴批量打印查档结果页面
	 */
	@RequestMapping(value = "/plprint", method = RequestMethod.GET)
	public String resultsPrint() {
		return prefix + "djywcx/plPrint";
	}
	
	/**
	 * 权利查询批量打印查档结果页面
	 */
	@RequestMapping(value = "/swplprint", method = RequestMethod.GET)
	public String swresultsPrint() {
		return prefix + "djywcx/swplPrint";
	}
	
	/**
	 * 个人查档-权利查询批量打印查档结果页面
	 */
	@RequestMapping(value = "/cd_swplPrint", method = RequestMethod.GET)
	public String cd_swresultsPrint() {
		
		return prefix + "djywcx/cd_swplPrint";
	}
	
	/**
	 * 宗地查询批量打印查档结果页面
	 */
	@RequestMapping(value = "/zdplprint", method = RequestMethod.GET)
	public String zdresultsPrint() {
		return prefix + "djywcx/zdplPrint";
	}
	
	/**
	 * 读取Excle模版中的权利人名称和身份证号返回页面显示
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@SuppressWarnings("unused")
	@RequestMapping(value = "/queryhousebyqlrmcandzjh/{cdyt}/{sffk}", method = RequestMethod.GET)
	public @ResponseBody Message getLogQueryList(@PathVariable("cdyt") String cdyt,@PathVariable("sffk") String sffk,HttpServletRequest request, HttpServletResponse response)throws UnsupportedEncodingException{
		
		Message msg = new Message();
		Map<String,Object> maps = new HashMap<String,Object>();
		List<Map> listMap = new ArrayList<Map>();
		String file_path = (String) request.getSession().getAttribute("FILEPATH");
		maps = gx_dacdService.GetLogQueryList(cdyt, sffk, file_path);
		if(maps.containsKey("QVLL1") ){ //权利查档
		     request.getSession().setAttribute("QVLL1", (List<Map>)maps.get("QVLL1"));//有房无地查档查询条件
	      }else if(maps.containsKey("QVLL2")){
	         request.getSession().setAttribute("QVLL2",  (List<Map>)maps.get("QVLL2"));//不动产权利查档查询条件
	       }
		if(maps.containsKey("qlrInfoLists") ){
			listMap = (List<Map>)maps.get("qlrInfoLists");
		}
		msg.setRows(listMap);
		msg.setTotal(listMap.size()); 				 
		msg.setMsg("数据读取成功");
	
		return msg;
	}
	
	/**
	 * （宗地信息查档）读取Excle模版中的内容并返回页面显示
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@SuppressWarnings("unused")
	@RequestMapping(value = "/querylandlist", method = RequestMethod.GET)
	public @ResponseBody Message getQueryLandList(HttpServletRequest request, HttpServletResponse response)throws UnsupportedEncodingException{
		
		Message msg = new Message();
		Map<String,Object> maps = new HashMap<String,Object>();
		List<Map> listMap = new ArrayList<Map>();
		String file_path = (String) request.getSession().getAttribute("FILEPATH");
		maps = gx_dacdService.GetQueryLandList(file_path);
		if(maps.containsKey("landInfoLists") ){
			request.getSession().setAttribute("landInfoLists", (List<Map>)maps.get("landInfoLists"));//查档查询条件
			listMap = (List<Map>)maps.get("landInfoLists");
		}
		msg.setRows(listMap);
		msg.setTotal(listMap.size());
		msg.setMsg("数据读取成功");
		
		return msg;
	}
	
	/**
	 * 将表格里的信息和根据权利人名称、身份证号从不动产库查出来的结果做数据比较，并填充
	 * @author hks
	 * @Date 2016/6/27 11:02
	 * @param cdyt  0:有无查档 、2：不动产权利（抵押、查封）查档
	 * @param sfyg 是否查询期房，0否，1是。
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	@SuppressWarnings({ "unchecked", "rawtypes", "unused" })
	@RequestMapping(value = "/matchdata/{cdyt}/{sffk}/{sfyg}/{sfzl}", method = RequestMethod.GET)
	public @ResponseBody Message matchData(@PathVariable("cdyt") String cdyt,@PathVariable("sffk") String sffk,@PathVariable("sfyg") String sfyg,
			@PathVariable("sfzl") String sfzl,HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException{
		
		Message msg = new Message();
		List<Map> queryVauleList = new ArrayList<Map>();
		if(cdyt != null && (cdyt.equals("1") || cdyt.equals("0"))){
			queryVauleList = (List<Map>)request.getSession().getAttribute("QVLL1");//用于匹配数据的条件集合
		}
		if(cdyt != null && cdyt.equals("2")){
			queryVauleList = (List<Map>) request.getSession().getAttribute("QVLL2");//用于匹配数据的条件集合
		}	
		msg = gx_dacdService.getMatchData(cdyt, sffk, sfyg,sfzl, queryVauleList);
		if(msg != null){
			List<Map> map = (List<Map>)msg.getRows();
			if(cdyt != null && (cdyt.equals("1") || cdyt.equals("0"))){
				request.getSession().setAttribute("MRESULTS", map);//匹配结果集
			}else if(cdyt != null && cdyt.equals("2")){
				request.getSession().setAttribute("MRESULTS_2", map);//匹配结果集
			}
		}
		
		return msg;
	}
	
	/**
	 * （宗地信息查档）将表格里的信息和根据权利人名称、身份证号从不动产库查出来的结果做数据比较，并填充
	 * @author taochunda
	 * @Date 2017/11/28 13:00
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	@SuppressWarnings({ "unchecked", "rawtypes", "unused" })
	@RequestMapping(value = "/landMatchdata", method = RequestMethod.GET)
	public @ResponseBody Message landMatchData(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException{
		
		Message msg = new Message();
		List<Map> queryVauleList = new ArrayList<Map>();
		queryVauleList = (List<Map>) request.getSession().getAttribute("landInfoLists");//用于匹配数据的条件集合
		msg = gx_dacdService.getLandMatchData(request,queryVauleList);
		if(msg != null){
			List<Map> map = (List<Map>)msg.getRows();
			request.getSession().setAttribute("RESULTS_ZD", map);//匹配结果集
		}
		
		return msg;
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

		//获取当前用户为查档经办人
		User user= userService.getCurrentUserInfo();
		String jbr = user.getUserName();
		List<Map>  results = (List<Map>) request.getSession().getAttribute("MRESULTS");
		Message msg = gx_dacdService.getPlPrntZM(results, jbr);
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
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value="/swplprintzm",method= RequestMethod.POST)    
	public @ResponseBody Message swplPrntZM(HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		//获取当前用户为查档经办人
		User user= userService.getCurrentUserInfo();
		String jbr = user.getUserName();
		List<Map>	results = (List<Map>) request.getSession().getAttribute("MRESULTS_2");
		Message msg = gx_dacdService.getSwplPrntZM(results, jbr);
		
		return msg;

	}
	
	/**
	 * 返回宗地查档证明内容
	 * @author taochunda
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value="/zdplprintzm",method= RequestMethod.POST)
	public @ResponseBody Message zdplPrntZM(HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		//获取当前用户为查档经办人
		User user= userService.getCurrentUserInfo();
		String jbr = user.getUserName();
		
		List<Map> results = (List<Map>) request.getSession().getAttribute("RESULTS_ZD");
		Message msg = gx_dacdService.getZDPLPrintZM(results, jbr);
		
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
//			   System.out.println(map);
			    HSSFRow row = sheet.createRow(rownum);//得到工作表的行
		
			    HSSFCell Cell0 = row.createCell((short) 0);
			    Cell0.setCellValue( (String) map.get("BH"));
	          
			    HSSFCell Cell1 = row.createCell((short) 1);
	            Cell1.setCellValue( (String) map.get("CDR")); 
	           
	            HSSFCell Cell2 = row.createCell((short) 2);
	            Cell2.setCellValue( (String) map.get("QLRMC")); 
	          
	            HSSFCell Cell3 = row.createCell((short) 3);
	            Cell3.setCellValue( (String) map.get("ZJH"));
	
	            HSSFCell Cell4 = row.createCell((short) 4);
	            Cell4.setCellValue( (String) map.get("ZL"));
	
	            HSSFCell Cell5 = row.createCell((short) 5);
	            Cell5.setCellValue( (String) map.get("BDCQZH"));

	            HSSFCell Cell6 = row.createCell((short) 6);
	            Cell6.setCellValue( (String) map.get("YGBDCQZH"));
	
	            HSSFCell Cell7 = row.createCell((short) 7);
	            Cell7.setCellValue( (String) map.get("MJ"));
	
	            HSSFCell Cell8 = row.createCell((short) 8);
	            Cell8.setCellValue( (String) map.get("GHYTname"));
	
	            HSSFCell Cell9 = row.createCell((short) 9);
	            Cell9.setCellValue( (String) map.get("SFQCQ"));
	
	            HSSFCell Cell10 = row.createCell((short) 10);
	            Cell10.setCellValue( (String) map.get("FWXZ"));
	            
	            HSSFCell Cell11 = row.createCell((short) 11);
	            Cell11.setCellValue( (String) map.get("GYRMC"));
	            
	            HSSFCell Cell12 = row.createCell((short) 12);
	            Cell12.setCellValue( (String) map.get("FJ"));

	            HSSFCell Cell13 = row.createCell((short) 13);
	            Cell13.setCellValue( (String) map.get("LSZL"));
	
	            HSSFCell Cell14 = row.createCell((short) 14);
	            Cell14.setCellValue( (String) map.get("YBDCQZH"));

	            HSSFCell Cell15 = row.createCell((short) 15);
	            Cell15.setCellValue( (String) map.get("PRONAME"));
	
	            HSSFCell Cell16 = row.createCell((short) 16);
	            Cell16.setCellValue( (String) map.get("BZ"));
	
	            rownum += 1;
//	            System.out.println("行数"+row.getRowNum());
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
		//intoExclelists = (List<Map>) request.getSession().getAttribute("QVLL2");// 用于匹配数据的条件集合
		intoExclelists = (List<Map>) request.getSession().getAttribute("MRESULTS_2");// 用于匹配数据的条件集合
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		Date date = new Date();
		if (intoExclelists.size() > 0) {
			for (Map qvpmap : intoExclelists) {
				
		/*		String qlrmc = (String) qvpmap.get("QLRMC");// 权利人名称
				String qlrzjh = (String) qvpmap.get("ZJH");// 权利人证件号
				String fwzl = (String) qvpmap.get("ZL");// 房屋坐落
				String bdcqzh = (String) qvpmap.get("QZH");// 不动产权证号
				String bh = (String) qvpmap.get("BH");// 序号
				String mj = (String) qvpmap.get("MJ");//面积
				String fwyt = (String) qvpmap.get("FWYT");//房屋用途
				int dys = StringHelper.getInt(qvpmap.get("DY"));//抵押数量
				int cfs = StringHelper.getInt(qvpmap.get("CF"));//查封数量
				int ygs = StringHelper.getInt(qvpmap.get("YG"));//预告数量
				int yys = StringHelper.getInt(qvpmap.get("YY"));//异议数量
				
				//String tdsyqzh = (String) qvpmap.get("TDQZH");// 土地使用权证号
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
						
						//面积
						HSSFCell Cell4 = row.createCell((short) 4);
						Cell4.setCellValue(mj);
						
						// 不动产坐落
						HSSFCell Cell5 = row.createCell((short) 5);
						Cell5.setCellValue(fwzl);
						
						//房屋用途
						HSSFCell Cell6 = row.createCell((short) 6);
						Cell6.setCellValue(fwyt);
						// 土地使用权证号
						//HSSFCell Cell5 = row.createCell((short) 5);
						String sql = "select bdcqzh from bdck.bdcs_ql_xz where bdcdyh in(select bdcdyh from bdck.bdcs_shyqzd_xz d left join bdck.bdcs_h_xz h on d.bdcdyid = h.zdbdcdyid where h.bdcdyh = '"+bdcqzh+"')";
						List<Map> list = this.baseCommonDao.getDataListByFullSql(sql);
						if(list.size() > 0 && list != null){
							Cell5.setCellValue((String)list.get(0).get("BDCQZH"));
						}else{
							Cell5.setCellValue("");
						}
						
						//Cell5.setCellValue("");
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
							HSSFCell Cell7 = row.createCell((short) 7);
							Cell7.setCellValue(dys == 0?"无": (dys+ "笔"));
							
							// 查封笔数
							HSSFCell Cell12 = row.createCell((short) 12);
							Cell12.setCellValue(cfs == 0?"无": (cfs+ "笔"));
							
							// 预告笔数
							HSSFCell Cell18 = row.createCell((short) 16);
							Cell18.setCellValue(ygs == 0?"无": (ygs+ "笔"));
							
							// 异议笔数
							HSSFCell Cell23 = row.createCell((short) 21);
							Cell23.setCellValue(yys == 0?"无": (yys+ "笔"));
						}
						
						// 抵押
						if (map.get("QLLX").equals("23")) {
							// 抵押权人
							HSSFCell Cell8 = row.createCell((short) 8);
							Cell8.setCellValue((String) map.get("QLRMC"));

							// 抵押面积
							HSSFCell Cell9 = row.createCell((short) 9);
							Cell9.setCellValue((String) map.get("BDCQZH"));

							// 抵押面积
							HSSFCell Cell9 = row.createCell((short) 9);
							Cell9.setCellValue(String.valueOf(map.get("DYMJ")));

							// 抵押金额
							HSSFCell Cell11 = row.createCell((short) 10);
							String bdbzzqse = String.valueOf(map.get("BDBZZQSE"));
							Cell11.setCellValue(bdbzzqse);

							// 抵押登记时间
							HSSFCell Cell12 = row.createCell((short) 11);
							String djsj = "";
							if (map.get("DJSJ") != null) {
								date.setTime(((Date) map.get("DJSJ")).getTime());
								djsj = sdf.format(date);
							}
							Cell12.setCellValue(djsj);
						}

						// 查封
						if (map.get("QLLX").equals("99")) {
							// 查封机关
							HSSFCell Cell14 = row.createCell((short) 13);
							Cell14.setCellValue((String) map.get("CFJG"));

							// 查封文号
							HSSFCell Cell15 = row.createCell((short) 14);
							Cell15.setCellValue((String) map.get("CFWH"));

							// 查封面积
							HSSFCell Cell15 = row.createCell((short) 15);
							Cell15.setCellValue((String) map.get("CFFW"));

							// 查封登记时间
							HSSFCell Cell17 = row.createCell((short) 15);
							String djsj = "";
							if (map.get("DJSJ") != null) {
								date.setTime(((Date) map.get("DJSJ")).getTime());
								djsj = sdf.format(date);
							}
							Cell17.setCellValue(djsj);
						}

						// 预告登记
						if (map.get("DJLX").equals("700")) {
							// 权利人
							HSSFCell Cell19 = row.createCell((short) 17);
							Cell19.setCellValue((String) map.get("QLRMC"));

							// 证号
							HSSFCell Cell20 = row.createCell((short) 18);
							Cell20.setCellValue((String) map.get("BDCQZH"));

							// 预告登记时间
							HSSFCell Cell21 = row.createCell((short) 19);
							String djsj = "";
							if (map.get("DJSJ") != null) {
								date.setTime(((Date) map.get("DJSJ")).getTime());
								djsj = sdf.format(date);
							}
							Cell21.setCellValue(djsj);

							// 登记类型
							HSSFCell Cell22 = row.createCell((short) 20);
							Cell22.setCellValue("预告登记");
						}
						// 异议登记
						if (map.get("DJLX").equals("600")) {
							// 申请人
							HSSFCell Cell24 = row.createCell((short) 22);
							Cell24.setCellValue((String) map.get("QLRMC"));
							
							// 异议登记时间
							HSSFCell Cell25 = row.createCell((short) 23);
							String djsj = "";
							if (map.get("DJSJ") != null) {
								date.setTime(((Date) map.get("DJSJ")).getTime());
								djsj = sdf.format(date);
							}
							Cell25.setCellValue(djsj);
						}else{
							// 申请人
							HSSFCell Cell24 = row.createCell((short) 22);
							Cell24.setCellValue("无");
							
							// 登记类型
							HSSFCell Cell25 = row.createCell((short) 23);
							Cell25.setCellValue("无");
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
					Cell5.setCellValue("");
					rownum++;
				}*/
				
				/**
				 * 重写三无查档的导出excel的方法
				 * huangpeifeng
				 * 20170815
				 */
				String qlrmc = (String) qvpmap.get("QLRMC");// 权利人名称
				String qlrzjh = (String) qvpmap.get("ZJH");// 权利人证件号
				String fwzl = (String) qvpmap.get("ZL");// 房屋坐落
				String bdcqzh = (String) qvpmap.get("BDCQZH");// 不动产权证号
				String bh = (String) qvpmap.get("BH");// 序号
				String mj = (String) qvpmap.get("MJ");//面积
				String fwyt = (String) qvpmap.get("FWYT");//房屋用途
				String sfbs = (String) qvpmap.get("SFBS");//是否是笔数
				String djlx = (String) qvpmap.get("DJLX");//登记类型
				String qllx = (String) qvpmap.get("QLLX");//权利类型
				String sfqcq = (String) qvpmap.get("SFQCQ");//房屋性质
				String fwxz = (String) qvpmap.get("FWXZ");//房屋性质
				String gyrmc = (String) qvpmap.get("GYRMC");//共有人名称
				String fj = (String) qvpmap.get("FJ");//附记
				int dys = StringHelper.getInt(qvpmap.get("DY"));//抵押数量
				int cfs = StringHelper.getInt(qvpmap.get("CF"));//查封数量
				int ygs = StringHelper.getInt(qvpmap.get("YG"));//预告数量
				int yys = StringHelper.getInt(qvpmap.get("YY"));//异议数量
				
				HSSFRow row = sheet.createRow(rownum + 3);// 得到工作表的行
				
				if("1".equals(sfbs)){//是否是笔数，1是，0否					
					// 抵押笔数
					HSSFCell Cell11 = row.createCell((short) 11);
					Cell11.setCellValue(dys == 0?"无": (dys+ "笔"));
					
					// 查封笔数
					HSSFCell Cell16 = row.createCell((short) 16);
					Cell16.setCellValue(cfs == 0?"无": (cfs+ "笔"));
					
					// 预告笔数
					HSSFCell Cell20 = row.createCell((short) 20);
					Cell20.setCellValue(ygs == 0?"无": (ygs+ "笔"));
					
					// 异议笔数
					HSSFCell Cell25 = row.createCell((short) 25);
					Cell25.setCellValue(yys == 0?"无": (yys+ "笔"));
					
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
					
					//面积
					HSSFCell Cell4 = row.createCell((short) 4);
					Cell4.setCellValue(mj);
					
					// 不动产坐落
					HSSFCell Cell5 = row.createCell((short) 5);
					Cell5.setCellValue(fwzl);
					
					//房屋用途
					HSSFCell Cell6 = row.createCell((short) 6);
					Cell6.setCellValue(fwyt);
					
					//是否全产权
					HSSFCell Cell7 = row.createCell((short) 7);
					Cell7.setCellValue(sfqcq);
					
					//房屋性质
					HSSFCell Cell8 = row.createCell((short) 8);
					Cell8.setCellValue(fwxz);
					
					//共有人姓名
					HSSFCell Cell9 = row.createCell((short) 9);
					Cell9.setCellValue(gyrmc);
					
					//附记
					HSSFCell Cell10 = row.createCell((short) 10);
					Cell10.setCellValue(fj);
					
					if(QLLX.DIYQ.Value.equals((String) qvpmap.get("DYQLLX"))){
						// 抵押权人
						HSSFCell Cell12 = row.createCell((short) 12);
						Cell12.setCellValue((String) qvpmap.get("DYQR"));

						// 证号
						HSSFCell Cell13 = row.createCell((short) 13);
						Cell13.setCellValue(String.valueOf(qvpmap.get("DYZH")));

						// 抵押金额
						HSSFCell Cell14 = row.createCell((short) 14);
						String bdbzzqse = String.valueOf(qvpmap.get("DYJE"));
						Cell14.setCellValue(bdbzzqse);

						// 抵押登记时间
						HSSFCell Cell15 = row.createCell((short) 15);
						String dydjsj = String.valueOf(qvpmap.get("DYDJSJ"));
						Cell15.setCellValue(dydjsj);
					}
					if(QLLX.QTQL.Value.equals((String) qvpmap.get("CFQLLX"))){
						// 查封机关
						HSSFCell Cell17 = row.createCell((short) 17);
						Cell17.setCellValue((String) qvpmap.get("CFJG"));

						// 查封文号
						HSSFCell Cell18 = row.createCell((short) 18);
						Cell18.setCellValue((String) qvpmap.get("CFWH"));

						// 查封登记时间
						HSSFCell Cell19 = row.createCell((short) 19);
						String cfdjsj = String.valueOf(qvpmap.get("CFDJSJ"));			
						Cell19.setCellValue(cfdjsj);
					}
					String ygqllx = (String) qvpmap.get("YGQLLX");//预告权利类型
					if(DJLX.YGDJ.Value.equals((String) qvpmap.get("YGDJLX")) && (QLLX.GYJSYDSHYQ_FWSYQ.Value.equals(ygqllx) 
							|| QLLX.JTJSYDSYQ_FWSYQ.Value.equals(ygqllx) || QLLX.ZJDSYQ_FWSYQ.Value.equals(ygqllx))){
						// 权利人
						HSSFCell Cell21 = row.createCell((short) 21);
						Cell21.setCellValue((String) qvpmap.get("YGQLR"));

						// 证号
						HSSFCell Cell_22 = row.createCell((short) 22);
						Cell_22.setCellValue((String) qvpmap.get("YGZH"));

						// 预告登记时间
						HSSFCell Cell23 = row.createCell((short) 23);
						String ygdjsj = String.valueOf(qvpmap.get("YGDJSJ"));
						Cell23.setCellValue(ygdjsj);

						// 登记类型
						HSSFCell Cell24 = row.createCell((short) 24);
						Cell24.setCellValue((String) qvpmap.get("YGDJXX"));
					}
					if(DJLX.YYDJ.Value.equals((String) qvpmap.get("YYDJLX"))){
						// 申请人
						HSSFCell Cell26 = row.createCell((short) 26);
						Cell26.setCellValue((String) qvpmap.get("YYQLR"));
						
						// 异议登记时间
						HSSFCell Cell27 = row.createCell((short) 27);
						String yydjsj = String.valueOf(qvpmap.get("YYDJSJ"));
						Cell27.setCellValue(yydjsj);
					}
				}else if("0".equals(sfbs)){
					// 序号
					HSSFCell Cell0 = row.createCell((short) 0);
					Cell0.setCellValue(bh);			
					if(QLLX.DIYQ.Value.equals((String) qvpmap.get("DYQLLX"))){
						// 抵押权人
						HSSFCell Cell12 = row.createCell((short) 12);
						Cell12.setCellValue((String) qvpmap.get("DYQR"));

						// 证号
						HSSFCell Cell13 = row.createCell((short) 13);
						Cell13.setCellValue(String.valueOf(qvpmap.get("DYZH")));

						// 抵押金额
						HSSFCell Cell14 = row.createCell((short) 14);
						String bdbzzqse = String.valueOf(qvpmap.get("DYJE"));
						Cell14.setCellValue(bdbzzqse);

						// 抵押登记时间
						HSSFCell Cell15 = row.createCell((short) 15);
						String dydjsj = String.valueOf(qvpmap.get("DYDJSJ"));
						Cell15.setCellValue(dydjsj);
					}
					if(QLLX.QTQL.Value.equals((String) qvpmap.get("CFQLLX"))){
						// 查封机关
						HSSFCell Cell17 = row.createCell((short) 17);
						Cell17.setCellValue((String) qvpmap.get("CFJG"));

						// 查封文号
						HSSFCell Cell18 = row.createCell((short) 18);
						Cell18.setCellValue((String) qvpmap.get("CFWH"));

						// 查封登记时间
						HSSFCell Cell19 = row.createCell((short) 19);
						String cfdjsj = String.valueOf(qvpmap.get("CFDJSJ"));			
						Cell19.setCellValue(cfdjsj);
					}
					String ygqllx = (String) qvpmap.get("YGQLLX");
					if(DJLX.YGDJ.Value.equals((String) qvpmap.get("YGDJLX")) && (QLLX.GYJSYDSHYQ_FWSYQ.Value.equals(ygqllx) 
							|| QLLX.JTJSYDSYQ_FWSYQ.Value.equals(ygqllx) || QLLX.ZJDSYQ_FWSYQ.Value.equals(ygqllx))){
						// 权利人
						HSSFCell Cell21 = row.createCell((short) 21);
						Cell21.setCellValue((String) qvpmap.get("YGQLR"));

						// 证号
						HSSFCell Cell_22 = row.createCell((short) 22);
						Cell_22.setCellValue((String) qvpmap.get("YGZH"));

						// 预告登记时间
						HSSFCell Cell23 = row.createCell((short) 23);
						String ygdjsj = String.valueOf(qvpmap.get("YGDJSJ"));
						Cell23.setCellValue(ygdjsj);

						// 登记类型
						HSSFCell Cell24 = row.createCell((short) 24);
						Cell24.setCellValue((String) qvpmap.get("YGDJXX"));
					}
					if(DJLX.YYDJ.Value.equals((String) qvpmap.get("YYDJLX"))){
						// 申请人
						HSSFCell Cell26 = row.createCell((short) 26);
						Cell26.setCellValue((String) qvpmap.get("YYQLR"));
						
						// 异议登记时间
						HSSFCell Cell27 = row.createCell((short) 27);
						String yydjsj = String.valueOf(qvpmap.get("YYDJSJ"));
						Cell27.setCellValue(yydjsj);
					}
					
				}
				rownum++;
				
			}
		}
		
		wb.write(outstream);
		outstream.flush(); 
		outstream.close();
		return url;
	}
	
	/**
	 * 个人查档：匹配数据
	 * 
	 * huangpeifeng
	 * 20170918
	 * @param xm 姓名
	 * @param zjh 身份证号
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@SuppressWarnings({ "unchecked", "rawtypes", "unused" })
	@RequestMapping(value = "/whthinmatchdata/{xm}/{zjh}", method = RequestMethod.GET)
	public @ResponseBody Message WhthinMatchData(@PathVariable("xm") String xm,@PathVariable("zjh") String zjh,HttpServletRequest request, HttpServletResponse response)
			throws UnsupportedEncodingException{
		//获取当前用户为查档经办人
		User user= userService.getCurrentUserInfo();
		String jbr = user.getUserName();
		String cdr = RequestHelper.getParam(request, "cdr");
		Message message = gx_dacdService.getWhthinMatchData(xm, zjh, jbr, cdr);
		
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
	 * 宗地信息查档（广西）导出
	 * @作者 taochunda
	 * @创建时间 2018年01月31日
	 * @return 
	 * @throws Exception 
	 * 
	 */
	@SuppressWarnings({ "deprecation", "rawtypes", "unchecked" })
	@RequestMapping(value = "/export_zd", method = RequestMethod.GET)
	public @ResponseBody String landExport(HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("utf-8");
		Message message = new Message();
		List<Map> rows = (List<Map>) request.getSession().getAttribute("RESULTS_ZD");
		
		//导出
		String basePath = request.getRealPath("/") + "\\resources\\PDF";
		String outpath = "";
		String url = "";
		String xlsName = "zdinfo.xls";//宗地信息查档导出模板
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
}
