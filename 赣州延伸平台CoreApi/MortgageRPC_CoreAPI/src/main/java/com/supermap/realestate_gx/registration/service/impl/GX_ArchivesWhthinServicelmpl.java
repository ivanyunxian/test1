package com.supermap.realestate_gx.registration.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.supermap.realestate.registration.model.interfaces.House;
import com.supermap.realestate.registration.model.interfaces.RightsHolder;
import com.supermap.realestate.registration.tools.ConverIdCard;
import com.supermap.realestate.registration.tools.RightsHolderTools;
import com.supermap.realestate.registration.tools.UnitTools;
import com.supermap.realestate.registration.util.ConfigHelper;
import com.supermap.realestate.registration.util.ConstHelper;
import com.supermap.realestate.registration.util.DateUtil;
import com.supermap.realestate.registration.util.RequestHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.realestate.registration.util.ConstValue.BDCDYLX;
import com.supermap.realestate.registration.util.ConstValue.DJDYLY;
import com.supermap.realestate.registration.util.ConstValue.DJLX;
import com.supermap.realestate.registration.util.ConstValue.QLLX;
import com.supermap.realestate_gx.registration.model.GX_CONFIG;
import com.supermap.realestate_gx.registration.service.GX_ArchivesWhthinService;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import com.supermap.wisdombusiness.web.Message;

@Service("gx_dacdService")
public class GX_ArchivesWhthinServicelmpl implements GX_ArchivesWhthinService{

	@Autowired
	private CommonDao baseCommonDao;
	
	/**
	 * 读取Excle模版中的权利人名称和身份证号返回页面显示	 
	 * @return msg
	 */
	public Map<String,Object>  GetLogQueryList(String cdyt,String sffk,String file_path){
		Message msg = new Message();	
		Map<String,Object> maps = new HashMap<String,Object>();
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
		/*	String fullsql = "SELECT Max(cdbh) as CDBH FROM SMWB_SUPPORT.GX_CONFIG";
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
			}*/
		
			for (int r = 1; r < rowCount+1; r++) { 			
				String qlrmc = "";
				String zjh = "";
				Map<String,String> m = new HashMap<String, String>();
				Map<String,String> queryvalueMap = new HashMap<String, String>();
				Row rw = sheet.getRow(r);
				Cell cl = rw.getCell(0); //第1列的值 ---编号
				Cell cdr = rw.getCell(1);//第2列查档人
				Cell cl0 = rw.getCell(2);//第3列的值 ---权利人姓名
				Cell cl1 = rw.getCell(3);//第4列的值 ---证件号码
				//Cell cl_bz = rw.getCell(3);//第4列的值   
				
				if(cl != null){
					cl.setCellType(Cell.CELL_TYPE_STRING);
					String bh = cl.getStringCellValue();
					//去掉序号的小数点
					if (bh.indexOf(".") != -1) {
						bh = bh.substring(0, bh.indexOf("."));
					}
					m.put("BH", bh);
					queryvalueMap.put("BH", bh);
				}else{
					continue;
				}

				if(cl0 == null){
					continue;
				}else{
					qlrmc = cl0.getStringCellValue();//获取权利人名称
					if(qlrmc == null || qlrmc.length()<=0)
						continue;
					queryvalueMap.put("QLRMC", qlrmc);
					m.put("QLRMC", qlrmc);
					if("0".equals(sffk) && cl1 == null){
						continue;
					}else{
						if(sffk.equals("0")){
							cl1.setCellType(Cell.CELL_TYPE_STRING);
							zjh = cl1.getStringCellValue();//获取权利人证件号（身份证号）		
							queryvalueMap.put("ZJH", zjh);
							}
						m.put("ZJH", zjh);

					/*	if(cdyt.equals("2")){
							Cell cl3 = rw.getCell(3);
							String qzh = cl3.getStringCellValue();
							m.put("QZH", qzh);
							queryvalueMap.put("QZH", qzh);
						}*/
						
					}
					if(cdr != null){
						String strCdr = cdr.getStringCellValue();
						queryvalueMap.put("CDR", strCdr);
					}	
					Cell cl_zl = rw.getCell(4);	//坐落
					if(cl_zl != null){
						String zl = cl_zl.getStringCellValue();
						m.put("ZL", zl);
						queryvalueMap.put("ZL", zl);
					}
				}
							
				if(cdyt.equals("0") ||  cdyt.equals("1") ){
					/*cdmd = "有无房产";
					if((String)cdbhs.get(0).get("CDBH") == null) {
						cdbh = "00001";
					}*/
					/*else if((String)cdbhs.get(0).get("CDBH") != null && cdbhs.size() > 0){
						
					}*/
				}
				if(cdyt.equals("2")){ //权利查档
					
					/*Cell cl4 = rw.getCell(4);//第4列的值     ---坐落
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
					*/						
					
					Cell cl_bdcqzhH = rw.getCell(5);//户不动产权证号
					if(cl_bdcqzhH != null){
						String bdcqzhH = cl_bdcqzhH.getStringCellValue();
						m.put("BDCQZH", bdcqzhH);
						queryvalueMap.put("BDCQZH", bdcqzhH);
					}else{
						cl_bdcqzhH = rw.getCell(6);
						if(cl_bdcqzhH != null){
							String bdcqzhH = cl_bdcqzhH.getStringCellValue();
							m.put("BDCQZH", bdcqzhH);
							queryvalueMap.put("BDCQZH", bdcqzhH);				
						}						
					}
					
					Cell cl_mj = rw.getCell(7);//面积
					if(cl_mj != null){
						String mj = cl_mj.getStringCellValue();
						m.put("MJ", mj);
						queryvalueMap.put("MJ", mj);
					}
										
					Cell cl_fwyt = rw.getCell(8);//房屋用途
					if(cl_fwyt != null){
						String fwyt = cl_fwyt.getStringCellValue();
						m.put("FWYT", fwyt);
						queryvalueMap.put("FWYT",fwyt);
					}
														
					cdmd = "不动产权利登记信息查询";
					
					/*if(cdbhs.size() == 0) 
						cdbh = "00001";
					//else if(cdbhs.size() > 0){}
*/				}

				qlrInfoLists.add(m);//返回页面显示Excle表的集合
				queryvaluesLists.add(queryvalueMap);//用于查询房屋信息的
		/*		GX_CONFIG  gx_config = new GX_CONFIG();
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
				baseCommonDao.save(gx_config);*/
			}
	         baseCommonDao.flush();
			
	         if("0".equals(cdyt) || "1".equals(cdyt) ){ //权利查档
		     //    request.getSession().setAttribute("QVLL1", queryvaluesLists);//有房无地查档查询条件
	        	 maps.put("QVLL1", queryvaluesLists);
	         }
	         else if("2".equals(cdyt)){
	        //	 request.getSession().setAttribute("QVLL2", queryvaluesLists);//不动产权利查档查询条件
	        	 maps.put("QVLL2", queryvaluesLists);
	         }
	         maps.put("qlrInfoLists", qlrInfoLists);
	        // resultList = qlrInfoLists;
	         /*msg.setRows(qlrInfoLists);
 			 msg.setTotal(qlrInfoLists.size()); 				 
 			 msg.setMsg("数据读取成功");*/
		}catch(Exception e){
			e.printStackTrace();
		}				
		return maps;
	}
	/**
	 * （宗地信息查档）读取Excle模版中的内容并返回页面显示
	 * @return msg
	 */
	public Map<String,Object>  GetQueryLandList(String file_path){
		Message msg = new Message();	
		Map<String,Object> maps = new HashMap<String,Object>();
		File tempFile = new File(file_path);
		SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
		String cdbh = "";//查档编号
		String cdmd = "";//查档用途
		try{
			List<Map> landInfoLists = new ArrayList<Map>();
			FileInputStream is = new FileInputStream(tempFile); //文件流  
			Workbook wb = WorkbookFactory.create(is); //这种方式 Excel 2003/2007/2010 都是可以处理的 
			Sheet sheet = wb.getSheetAt(0);//获取Excle模版的第一个sheet
			int num = wb.getNumberOfSheets();
			int rowCount = sheet.getLastRowNum(); //获取第一个sheet有效行数 
			//查询当前记录的最大查档编号
			for (int r = 1; r < rowCount+1; r++) {
				Map<String,String> m = new HashMap<String, String>();
				Map<String,String> queryvalueMap = new HashMap<String, String>();
				Row rw = sheet.getRow(r);
				Cell cl0 = rw.getCell(0); //第1列的值 ---编号
				Cell cl1 = rw.getCell(1);//第2列 ---查档人
				Cell cl2 = rw.getCell(2);//第3列的值 ---不动产单元号
				Cell cl3 = rw.getCell(3);//第4列的值 ---权利人
				Cell cl4 = rw.getCell(4);//第5列的值 ---证件号码
				Cell cl5 = rw.getCell(5);//第6列的值 ---土地座落
				Cell cl6 = rw.getCell(6);//第7列的值 ---权证号
				Cell cl7 = rw.getCell(7);//第8列的值 ---土地类型
				Cell cl8 = rw.getCell(8);//第9列的值 ---抵押状态
				Cell cl9 = rw.getCell(9);//第10列的值 ---查封状态
				Cell cl10 = rw.getCell(10);//第11列的值 ---查询状态
				Cell cl11 = rw.getCell(11);//第12列的值 ---查封文号
				Cell cl12 = rw.getCell(12);//第13列的值 ---房地状况
				
				if(!StringHelper.isEmpty(cl0)){
					cl0.setCellType(Cell.CELL_TYPE_STRING);
					String bh = cl0.getStringCellValue();
					//去掉序号的小数点
					if (bh.indexOf(".") != -1) {
						bh = bh.substring(0, bh.indexOf("."));
					}
					m.put("BH", bh);
				}
				if(!StringHelper.isEmpty(cl1)){
					String cdr = cl1.getStringCellValue();
					m.put("CDR", cdr);
				}
				if(!StringHelper.isEmpty(cl2)){
					String bdcdyh = cl2.getStringCellValue();
					m.put("BDCDYH", bdcdyh);
				}
				if(!StringHelper.isEmpty(cl3)){
					String qlrmc = cl3.getStringCellValue();
					m.put("QLRMC", qlrmc);
				}
				if(!StringHelper.isEmpty(cl4)){
					String zjhm = cl4.getStringCellValue();
					m.put("ZJHM", zjhm);
				}
				if(!StringHelper.isEmpty(cl5)){
					String zl = cl5.getStringCellValue();
					m.put("ZL", zl);
				}
				if(!StringHelper.isEmpty(cl6)){
					String bdcqzh = cl6.getStringCellValue();
					m.put("BDCQZH", bdcqzh);
				}
				if(!StringHelper.isEmpty(cl7)){
					String bdcdylxmc = cl7.getStringCellValue();
					m.put("BDCDYLXMC", bdcdylxmc);
				}
				if(!StringHelper.isEmpty(cl8)){
					String dyzt = cl8.getStringCellValue();
					m.put("DYZT", dyzt);
				}
				if(!StringHelper.isEmpty(cl9)){
					String cfzt = cl9.getStringCellValue();
					m.put("CFZT", cfzt);
				}
				if(!StringHelper.isEmpty(cl10)){
					String cxzt = cl10.getStringCellValue();
					m.put("CXZT", cxzt);
				}
				if(!StringHelper.isEmpty(cl11)){
					String cfwh = cl11.getStringCellValue();
					m.put("CFWH", cfwh);
				}
				if(!StringHelper.isEmpty(cl12)){
					String fdzk = cl12.getStringCellValue();
					m.put("FDZK", fdzk);
				}
				
				
				landInfoLists.add(m);//返回页面显示Excle表的集合
			}
			maps.put("landInfoLists", landInfoLists);
		}catch(Exception e){
			e.printStackTrace();
		}
		return maps;
	}
	/**
	 * 
	 * 获取查档编号
	 * huangpeifeng
	 * 20170926	
	 * @param qlrmc 权利人名称
	 * @param zjh 证号号
	 * @param cdmd 查档目的
	 * @return
	 */
	public String getCdbh(String qlrmc,String zjh,String cdmd){
		String cdbh = "";
		String fullsql = "SELECT Max(cdbh) as CDBH FROM SMWB_SUPPORT.GX_CONFIG";
		List<Map> cdbhs = baseCommonDao.getDataListByFullSql(fullsql); 	
		if(cdbhs != null && cdbhs.size() >0){
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
			
			GX_CONFIG  gx_config = new GX_CONFIG();
			Date currdate = new Date();
			SimpleDateFormat  formatter  = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			gx_config.setCdbh(cdbh);
			gx_config.setCdmd(cdmd);
			gx_config.setCdrmc(qlrmc);
			gx_config.setCdrzjh(zjh);		
			String cd_datestring  = formatter.format(currdate);  
			gx_config.setCdtime(cd_datestring);
			
			baseCommonDao.save(gx_config);
		}
		baseCommonDao.flush();
		return cdbh;
	}
	/**
	 * 将表格里的信息和根据权利人名称、身份证号从不动产库查出来的结果做数据比较，并填充
	 */
	public Message  getMatchData(String cdyt, String sffk, String sfyg, String sfzl, List<Map> queryVauleList){
					
		Message message = new Message();
		
/*		
		if(cdyt != null && (cdyt.equals("1") || cdyt.equals("0"))){					
			 //接着遍历查询权利人的房屋情况
		//	String querytype = request.getParameter("QUERYTYPE");
				String order = "";
			int total = 0;
			Integer page = 1;
			if (request.getParameter("page") != null) {
				page = Integer.parseInt(request.getParameter("page"));
			}
			
			Integer rows = 20;
			if (request.getParameter("rows") != null) {
				rows = Integer.parseInt(request.getParameter("rows"));
			}			
			String statuString = "3";//全部
			String startString = request.getParameter("2016-02-18");//开始登记时间从2016-01-18开始
			String endString = request.getParameter("yyyy-MM-dd");//查到至今
			String actdefname = "";
			String prodefname = request.getParameter("转移");
			String staffanme = "";
			
		//	try {
			//	order = RequestHelper.getParam(request, "order");// 排序Order
				List<Map> toExcleLists = new ArrayList<Map>();	
				StringBuilder derDY_XZ = new StringBuilder();
				StringBuilder derDY_LS = new StringBuilder();
				int total = 0;
				for(Map<String, String> qv : queryVauleList){
					List<Map> results = new ArrayList<Map>();
					String qlrmc = "";
					if(sffk.equals("0")){
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
					}
					
					if(cdyt.equals("0") || cdyt.equals("1")){
						String zjh = qv.get("QLR.ZJH");
						qlrmc = qv.get("QLR.QLRMC");		
											
						total += results.size();
						
						for(Map<String,String> mp : results){
							toExcleLists.add(mp);
						}
							if(sffk.equals("0")){
							if(zjh != null)
								qv.put("QLR.ZJH", zjh);
								qv.put("QLR.QLRMC", "");
							
						}
						else if(sffk.equals("1")){
							qv.put("QLR.QLRMC", qlrmc);
						}
						qv.put("CDYT", "0");
						qv.put("FDCCX", sffk);
						qv.put("DY.ZL", "");
						qv.put("DY.BDCDYH", "");
						qv.put("MJ","");
						
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
						qv.put("JGSX", "2");//结果筛选；"1"：全部显示，"2"：仅显示产权
						//results.clear();
						msg = queryService.queryHouse(qv, 1, rows, false, "1", "bdcdyid", "asc");
						total += msg.getTotal();										
					}					
					if(cdyt.equals("1")){
						String zjh = qv.get("QLR.ZJH");
						qlrmc = qv.get("QLR.QLRMC");
						if(sffk.equals("0")){
							if(zjh != null)
								qv.put("QLR.ZJH", zjh);
								qv.put("QLR.QLRMC", "");
							
						}
						else if(sffk.equals("1")){
							qv.put("QLR.QLRMC", qlrmc);
						}
						qv.put("CDYT", "1");

						qv.put("DY.ZL", "");
						qv.put("DY.BDCDYH", "");
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
						msg = queryService.queryHouseByQlrmcAndZjhm(qv, false, "3", cdyt, "BDCDYID");//查询有误房屋
					}
					results = (List<Map>) msg.getRows();
					
					Message ms = new Message();
					
					if(cdyt.equals("1")){
						int number = results.size();
						for(int k = 0; k < number;k++){
							String bdcqzh = (String) results.get(k).get("BDCQZH");
							if(!StringHelper.isEmpty(bdcqzh) && bdcqzh.indexOf("不动产") == -1){
								results.remove(k);
								k--;
								number--;
							}
						}
					}
					if(results.size() > 0){
						for(int j = 0;j < results.size();j++){
							//String qlr = (String) results.get(j).get("QLRMC");
							String qlr = qv.get("QLR.QLRMC");
							
							List<BDCS_QLR_XZ> qlrinfo_xz = new ArrayList<BDCS_QLR_XZ>();
							
								if(sffk.equals("1")){
									String bdcqzh = (String) results.get(j).get("BDCQZH");
									
									qlrinfo_xz = baseCommonDao.getDataList(BDCS_QLR_XZ.class, "BDCQZH ='"+bdcqzh.trim()+"'");
								}else{
									String qlrzjh = qv.get("QLR.ZJH");
									if(qlrzjh != null && !qlrzjh.trim().isEmpty()){
										qlrinfo_xz = baseCommonDao.getDataList(BDCS_QLR_XZ.class, "ZJH ='"+qlrzjh.trim()+"'");
									}
								}
							
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
							results.get(j).put("MJ", results.get(j).get("SCJZMJ"));
							results.get(j).put("BZ", qv.get("BZ"));
							String ghytname = (String) results.get(j).get("GHYTname");
							System.out.println(qlrmc+"房屋用途："+ ghytname);
							if(ghytname != null){
								results.get(j).put("ZL",(String)results.get(j).get("ZL"));
								*//**以下做法是针对柳州的，只有房屋规划用途是住宅的才显示坐落（ZL）
								/*if(ghytname.equals("住宅") || ghytname.equals("其它")){
									results.get(j).put("ZL",(String)results.get(j).get("ZL"));
								}else{
									results.get(j).put("ZL","");
								}*//*
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
				
				baseCommonDao.flush();
			//	request.getSession().setAttribute("MRESULTS", toExcleLists);//匹配结果集
				message.setTotal(total);
				message.setRows(toExcleLists);
				
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				System.out.println("匹配出错！");
			}
			
		}
	
		if(cdyt != null && cdyt.equals("2")){
			long count = 0;
			Map<String, String> queryvalues = new HashMap<String, String>();	
			List<Map> toExcleLists = new ArrayList<Map>();
			StringBuilder derQL = new StringBuilder(); 			
			for(Map<String, String> qvp : queryVauleList){
				
				List<Map> results = new ArrayList<Map>();
			}
			baseCommonDao.flush();
		//	request.getSession().setAttribute("MRESULTS_2", toExcleLists);//匹配结果集
			message.setMsg("匹配成功"+toExcleLists.size()+"条");
			message.setTotal(toExcleLists.size());
			message.setRows(toExcleLists);
		}
		*/
		
		/**
		 * 重写三无查档的查全部和查不动产的数据匹配，处理数据读取不正确和查询效率低的问题
		 * huangpeifeng
		 * 20170801
		 */
		if(cdyt != null && ("1".equals(cdyt) || "0".equals(cdyt))){
			message = getHouseData(cdyt,sffk,sfyg,sfzl,queryVauleList);
		}
		if(cdyt != null && "2".equals(cdyt) ){
			message = getFSQLData(cdyt,sffk,sfyg,queryVauleList);
		}
		
		return message;
		
	}
	/**
	 * （宗地信息查档）将表格里的信息和根据权利人名称、身份证号从不动产库查出来的结果做数据比较，并填充
	 */
	public Message  getLandMatchData(HttpServletRequest request, List<Map> queryVauleList){
		
		Message message = new Message();
		List results = new ArrayList();
		

		Map<String, String> queryvalues = new HashMap<String, String>();

		Integer page = 1;
		if (request.getParameter("page") != null) {
			page = Integer.parseInt(request.getParameter("page"));
		}
		
		Integer rows = 10;
		if (request.getParameter("rows") != null) {
			rows = Integer.parseInt(request.getParameter("rows"));
		}
		for(Map<String, String> q : queryVauleList){
			//只取字母和数字进行匹配
			//bdcqzh = bdcqzh.replaceAll("[^a-z^A-Z^0-9]", "");
			
			try {
				String bdcdyh = q.get("BDCDYH");// 不动产单元号
				String qlrmc = q.get("QLRMC"); // 权利人名称
				String qlrzjh = q.get("ZJHM");// 证件号码
				String zl = q.get("ZL");// 坐落
				String bdcqzh = q.get("BDCQZH");// 不动产权证号
				String tdzt = q.get("BDCDYLXMC");// 土地状态
				String dyzt = q.get("DYZT");// 抵押状态
				String cfzt = q.get("CFZT");// 查封状态
				String cxzt = q.get("CXZT");// 查询状态
				String cfwh = q.get("CFWH");// 查封文号
				String fdzk = q.get("FDZK");// 房地状况
				String cdbh = getCdbh(qlrmc, qlrzjh, "宗地信息查询");
				//********************************************************
				queryvalues.put("DY.ZL", zl);
				queryvalues.put("DY.BDCDYH", bdcdyh);
				queryvalues.put("QLR.QLRMC", qlrmc);
				queryvalues.put("QLR.ZJH", qlrzjh);
				queryvalues.put("QL.BDCQZH", bdcqzh);
				queryvalues.put("FSQL.CFWH", cfwh);
				queryvalues.put("DYZT", dyzt);
				queryvalues.put("CFZT", cfzt);
				queryvalues.put("CXZT", cxzt);
				queryvalues.put("TDZT", tdzt);
				if (StringHelper.isEmpty(fdzk)) {
					queryvalues.put("FDZK", "纯土地");
				} else {
					queryvalues.put("FDZK", fdzk);
				}
//				queryvalues.put("SWCDBZ", "cd");
				Message ms = queryLand(queryvalues, page, rows);
				
				List landResult = new ArrayList();
				landResult = ms.getRows();
				if(landResult.size() == 0 ){
					Map map = new HashMap();
					map.put("CDBH", cdbh);// 查档编号
					map.put("BH", q.get("BH"));
					map.put("CDR", q.get("CDR"));
					map.put("BDCDYH", bdcdyh);
					map.put("BDCDYLXMC", tdzt);
					map.put("QLRMC", qlrmc);
					map.put("ZJHM", qlrzjh);
					map.put("BDCQZH", bdcqzh);
					map.put("DYZT", dyzt);
					map.put("CFZT", cfzt);
					map.put("ZL", zl);
					map.put("BZ", "查无记录！");
					map.put("isOK", "NO");//有无信息
					results.add(map);
				} else if(landResult.size() > 0 ){
					for (int i = 0; i < landResult.size(); i++) {
						
						Map map = (Map) landResult.get(i);
						map.put("CDBH", cdbh);// 查档编号
						map.put("BH", q.get("BH"));
						map.put("CDR", q.get("CDR"));
						map.put("BZ", "数据匹配成功！");
						map.put("isOK", "YES");//有无信息
						results.add(map);
					}
					
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			
		}
		message.setMsg("匹配成功"+results.size()+"条");
		message.setTotal(results.size());
		message.setRows(results);
	
		
		return message;
		
	}
	
	/**
	 *  * 
	 *  匹配有无房产
	 *  huangpeifeng
	 *  20170928
	 * @param cdyt 查档用途：0是查全部户，1是只查不动产户，2是查抵押权、查封等附属权利。
	 * @param sffk 是否房开 ：房开查询不填身份证号，0否，1是。
	 * @param sfyg 是否预告 ：是否查询期房（目前设计是查不动产专用），0否，1是。
	 * @param sfzl 是否坐落：是否加坐落查询条件，0否，1是。
	 * @param queryVauleList
	 * @return
	 */
	public Message getHouseData(String cdyt, String sffk, String sfyg, String sfzl, List<Map> queryVauleList){		
		Message message = new Message();
		List<Map> toExcleLists = new ArrayList<Map>();	
		StringBuilder derDY_XZ = new StringBuilder();
		StringBuilder derDY_LS = new StringBuilder();
		String qlrzjh_18 = "";
		String qlrzjh_15 = "";
		int total = 0;
		for(Map<String, String> qv : queryVauleList){
			List<Map> results = new ArrayList<Map>();
			String qlrmc = "";
			if("0".equals(sffk)){
				if(qv.get("ZJH") == null || "".equals(qv.get("ZJH"))){
					Map<String,String> map = new HashMap<String, String>();
					map.put("BH", qv.get("BH"));
					map.put("QLRMC", qv.get("QLRMC"));
					map.put("ZJH", "");
					map.put("BZ", "信息不完整（缺少身份证号）");
					map.put("ZL", "无");
					total += 1;
					toExcleLists.add(map);
					continue;
				}
			}
			if(("0".equals(cdyt) || "1".equals(cdyt)) && "1".equals(sfzl) && sfzl.length()<=0){
				Map<String,String> map = new HashMap<String, String>();
				map.put("BH", qv.get("BH"));
				map.put("QLRMC", qv.get("QLRMC"));
				map.put("ZJH", "");
				map.put("BZ", "信息不完整（缺少坐落信息）");
				map.put("ZL", "信息不完整（缺少坐落信息）");
				total += 1;
				toExcleLists.add(map);
				continue;
			}
			String cdr = "";//查档人
			if("".equals(StringHelper.formatObject(qv.get("CDR")))){
				cdr = qv.get("QLRMC");
			}else{
				cdr = qv.get("CDR");
			}
			String zjh = qv.get("ZJH");
			qlrmc = StringHelper.formatObject(qv.get("QLRMC"));
			String zl = StringHelper.formatObject(qv.get("ZL"));
			boolean SFSFZH = false;
			if(!StringHelper.isEmpty(zjh)){
				String[] zjhs = zjh.split("，");
				if(zjhs.length>0){
					zjh = zjhs[0];
				}
				zjh = zjh.trim().toUpperCase(); 
				if(ConverIdCard.checkIDCard(zjh)){
					if(zjh.length()==18){
						qlrzjh_18 = zjh;
						qlrzjh_15 = ConverIdCard.getOldIDCard(zjh);			
					}
					if(zjh.length()==15){
						qlrzjh_15 = zjh;
						qlrzjh_18 = ConverIdCard.getNewIDCard(zjh);		
					}
				}else{
					qlrzjh_18 = qlrzjh_15 = zjh;
				}
			}
			/*if(!StringHelper.isEmpty(zjh)){
				SFSFZH = ConverIdCard.checkIDCard(zjh);
				zjh = ConverIdCard.getOldIDCard(zjh);//把18位身份证换成旧的15位身份证
			}*/
			derDY_XZ.setLength(0);	
			derDY_XZ.append("SELECT DJDY.BDCDYID,DJDY.BDCDYLX,QLR.QLRMC,QLR.BDCQZH,QLR.ZJH,QLR.GYFS,QL.DJLX,QL.QLLX,QL.DJSJ,QL.QLID,QLR.QLRID,QL.FJ FROM");
			derDY_XZ.append(" (SELECT TRIM(QLRMC) AS QLRMC,TRIM(ZJH) AS ZJH,BDCQZH,GYFS,QLID,QLRID FROM BDCK.BDCS_QLR_XZ) QLR");
			derDY_XZ.append(" LEFT JOIN BDCK.BDCS_QL_XZ QL ON QLR.QLID=QL.QLID LEFT JOIN BDCK.BDCS_DJDY_XZ DJDY ON DJDY.DJDYID=QL.DJDYID");
			derDY_XZ.append(" WHERE QLR.QLRMC='");
			derDY_XZ.append(qlrmc);
			derDY_XZ.append("' AND DJDY.BDCDYLX IN('031','032')");//现只查户的数据
			if("0".equals(sffk)){//判断是否是房开查询，房开查询只用名字为查询条件，SFFK为1是房开查询
				if (!StringHelper.isEmpty(zjh)) {
					if (zjh.length() == 18) {
						String oldCard = ConverIdCard.getOldIDCard(zjh);
						derDY_XZ.append(" AND (" + "QLR.ZJH ='" + zjh + "' OR QLR.ZJH = '" + oldCard + "')");
					} else {
						derDY_XZ.append(" AND" + " QLR.ZJH = '" + zjh + "' ");
					}
				}
			}
			if("1".equals(sfzl)){//判断是否按坐落查询，1是，0否
				derDY_XZ.append(" AND DJDY.BDCDYID IN (SELECT BDCDYID FROM");
				derDY_XZ.append(" (SELECT BDCDYID,TRIM(ZL) AS ZL FROM BDCK.BDCS_H_XZ UNION SELECT BDCDYID,TRIM(ZL) AS ZL FROM BDCK.BDCS_H_XZY) H WHERE H.ZL='");
				derDY_XZ.append(zl);
				derDY_XZ.append("')");
			}
			
			derDY_XZ.append(" and ql.qllx not in('23','98','99')");						
			derDY_XZ.append(" ORDER BY DJDY.BDCDYLX");						
			List<Map> listDJDY_XZ =  baseCommonDao.getDataListByFullSql(derDY_XZ.toString());
			if(listDJDY_XZ != null && listDJDY_XZ.size()>0){
				List<String> listZL_h = new ArrayList<String>();
				for(int i=0; i<listDJDY_XZ.size(); i++){
					String strBdcdylx = StringHelper.formatObject(listDJDY_XZ.get(i).get("BDCDYLX"));
					if(strBdcdylx.length()<=0){
						continue; 
					}								
					if(!BDCDYLX.H.Value.equals(strBdcdylx) && !BDCDYLX.YCH.Value.equals(strBdcdylx)){//现只要户的数据
							continue;
					}																
					Map<String,String> map = new HashMap<String, String>();
					BDCDYLX bdcdylx = BDCDYLX.initFrom(strBdcdylx);
					House house = (House)UnitTools.loadUnit(bdcdylx, DJDYLY.XZ, StringHelper.formatObject(listDJDY_XZ.get(i).get("BDCDYID")));
					if (house != null) {
						String cdbh = getCdbh(qlrmc, zjh, "有无房产");// 获取查档编号
						String mj = "";
						String djsj = StringHelper.FormatByDatetime(listDJDY_XZ.get(i).get("DJSJ"));// 登记时间
						String gyfs = StringHelper.formatObject(listDJDY_XZ.get(i).get("GYFS"));//共有方式
						String djlx = StringHelper.formatObject(listDJDY_XZ.get(i).get("DJLX"));
						String gyrmc = "";
						String djlxmc = "";
						if (!StringHelper.isEmpty(djlx)) {
							djlxmc = ConstHelper.getNameByValue("DJLX", djlx);
						}
						if (!"0".equals(gyfs)) {//除单独所有外
							String qlid = StringHelper.formatObject(listDJDY_XZ.get(i).get("QLID"));
							String qlrid = StringHelper.formatObject(listDJDY_XZ.get(i).get("QLRID"));
							gyrmc = getGYRMC(qlid, qlrid);
						}
						if (BDCDYLX.YCH.equals(house.getBDCDYLX()) && listZL_h.contains(house.getZL()))// 当该权利人已有现房时，就不读取期房数据。期现房坐落一致，所以用zl做判断。
							continue;
						if (!"0".equals(String.valueOf(house.getMJ())))
							mj = String.valueOf(house.getMJ());
						if ("1".equals(cdyt)) {// cdyt为1是不动产查档，只查不动产单元,0是查全部
							if (!StringHelper.isEmpty((String) listDJDY_XZ.get(i).get("BDCQZH"))
									&& listDJDY_XZ.get(i).get("BDCQZH").toString().indexOf("不动产权") != -1) {// 只查产权
								map.put("CDBH", cdbh);// 查档编号
								map.put("BH", qv.get("BH"));// 编号
								map.put("BDCQZH", StringHelper.formatObject(listDJDY_XZ.get(i).get("BDCQZH")));
								map.put("ZL", house.getZL());
								map.put("MJ", mj);
								map.put("QLRMC", StringHelper.formatObject(listDJDY_XZ.get(i).get("QLRMC")));
								map.put("DJSJ", djsj);
								map.put("ZJH", StringHelper.formatObject(listDJDY_XZ.get(i).get("ZJH")));
								map.put("SFQCQ", ConstHelper.getNameByValue("GYFS", gyfs)); // 共有方式
								map.put("GHYTname", ConstHelper.getNameByValue("FWYT", house.getFWYT1()));// 房屋用途
								map.put("DJLX", djlx);
								map.put("DJLXMC", djlxmc);
								map.put("FWXZ", ConstHelper.getNameByValue("FWXZ", house.getFWXZ()));//房屋性质
								map.put("FJ", StringHelper.formatObject(listDJDY_XZ.get(i).get("FJ")));
								map.put("GYRMC", gyrmc);
								results.add(map);
								if (BDCDYLX.H.equals(house.getBDCDYLX()))
									listZL_h.add(house.getZL());
							}
							if ("1".equals(sfyg) && DJLX.YGDJ.Value.equals(listDJDY_XZ.get(i).get("DJLX").toString())) {// sfyg为1是查期房产权
								String qllx = StringHelper.formatObject(listDJDY_XZ.get(i).get("QLLX"));
								if (QLLX.GYJSYDSHYQ_FWSYQ.Value.equals(qllx) || QLLX.JTJSYDSYQ_FWSYQ.Value.equals(qllx)
										|| QLLX.ZJDSYQ_FWSYQ.Value.equals(qllx)) {
									map.put("CDBH", cdbh);
									map.put("BH", qv.get("BH"));
									map.put("YGBDCQZH", StringHelper.formatObject(listDJDY_XZ.get(i).get("BDCQZH")));
									map.put("ZL", house.getZL());
									map.put("MJ", mj);
									map.put("QLRMC", StringHelper.formatObject(listDJDY_XZ.get(i).get("QLRMC")));
									map.put("DJSJ", djsj);
									map.put("ZJH", StringHelper.formatObject(listDJDY_XZ.get(i).get("ZJH")));
									map.put("SFQCQ", ConstHelper.getNameByValue("GYFS", gyfs));
									map.put("GHYTname", ConstHelper.getNameByValue("FWYT", house.getFWYT1()));
									map.put("DJLX", djlx);
									map.put("DJLXMC", djlxmc);
									map.put("FWXZ", ConstHelper.getNameByValue("FWXZ", house.getFWXZ()));//房屋性质
									map.put("FJ", StringHelper.formatObject(listDJDY_XZ.get(i).get("FJ")));
									map.put("GYRMC", gyrmc);
									results.add(map);
								}
							}
						} else {
							if (BDCDYLX.H.equals(house.getBDCDYLX())) {
								listZL_h.add(house.getZL());
								map.put("BDCQZH", StringHelper.formatObject(listDJDY_XZ.get(i).get("BDCQZH")));
							} else if (BDCDYLX.YCH.equals(house.getBDCDYLX())) {
								map.put("YGBDCQZH", StringHelper.formatObject(listDJDY_XZ.get(i).get("BDCQZH")));
							}
							map.put("CDBH", cdbh);
							map.put("BH", qv.get("BH"));
							map.put("ZL", house.getZL());
							map.put("MJ", mj);
							map.put("DJSJ", djsj);
							map.put("QLRMC", StringHelper.formatObject(listDJDY_XZ.get(i).get("QLRMC")));
							map.put("ZJH", StringHelper.formatObject(listDJDY_XZ.get(i).get("ZJH")));
							map.put("SFQCQ", ConstHelper.getNameByValue("GYFS", gyfs));
							map.put("GHYTname", ConstHelper.getNameByValue("FWYT", house.getFWYT1()));
							map.put("DJLX", djlx);
							map.put("DJLXMC", djlxmc);
							map.put("FWXZ", ConstHelper.getNameByValue("FWXZ", house.getFWXZ()));//房屋性质
							map.put("FJ", StringHelper.formatObject(listDJDY_XZ.get(i).get("FJ")));
							map.put("GYRMC", gyrmc);
							results.add(map);
						}
					}
					}
				}
				if(results == null || results.size()<=0){//查不到现状单元，就显示原需查档的人员信息
					Map<String,String> map2 = new HashMap<String, String>();
					String cdbh = getCdbh(qlrmc,zjh,"有无房产");
					map2.put("CDBH", cdbh);
					map2.put("BH", qv.get("BH"));
					map2.put("QLRMC",qlrmc);
					map2.put("ZJH",qv.get("ZJH"));
					map2.put("ZL","无");							
					results.add(map2);
				}												
				/*
				 * 获取该权利人历史权利的单元
				 */						
				derDY_LS.setLength(0);
				derDY_LS.append("SELECT distinct DY.ZL,QLR.BDCQZH,QLR.QLID FROM (SELECT TRIM(SQRXM) AS SQRXM,XMBH,SQRLB FROM BDCK.BDCS_SQR) SQR");
				derDY_LS.append(" LEFT JOIN BDCK.BDCS_DJDY_GZ DJDY ON DJDY.XMBH=SQR.XMBH");
				derDY_LS.append(" LEFT JOIN BDCK.BDCS_XMXX XMXX ON XMXX.XMBH=SQR.XMBH");
				derDY_LS.append(" LEFT JOIN BDCK.BDCS_QL_LS QL ON QL.DJDYID=DJDY.DJDYID LEFT JOIN (SELECT TRIM(QLRMC) AS QLRMC,TRIM(ZJH) AS ZJH,QLID,BDCQZH FROM BDCK.BDCS_QLR_LS) QLR");
				derDY_LS.append(" ON QLR.QLID=QL.QLID LEFT JOIN BDCK.BDCS_H_XZ DY ON DY.BDCDYH=QL.BDCDYH");
				derDY_LS.append(" WHERE XMXX.DJLX='200' AND XMXX.SFDB='1' AND QL.QLLX IN('4','6','8') AND QLR.BDCQZH NOT LIKE '%不动产%' AND SQR.SQRLB='2' AND SQR.SQRXM='");
				derDY_LS.append(qlrmc);
				derDY_LS.append("' AND QLR.QLRMC='");
				derDY_LS.append(qlrmc);
				derDY_LS.append("'");
				if("0".equals(sffk)){//判断是否是房开查询，房开查询只用名字为查询条件，SFFK为1是房开查询
					if (!StringHelper.isEmpty(zjh)) {
						if (zjh.length() == 18) {
							String oldCard = ConverIdCard.getOldIDCard(zjh);
							derDY_LS.append(" AND (" + "QLR.ZJH ='" + zjh + "' OR QLR.ZJH = '" + oldCard + "')");
						} else {
							derDY_LS.append(" AND" + " QLR.ZJH = '" + zjh + "' ");
						}
					}
				}
				if("1".equals(sfzl)){//判断是否按坐落查询，1是，0否
					derDY_LS.append(" AND DY.ZL='");
					derDY_LS.append(zl);
					derDY_LS.append("'");
				}
				List<Map> listDY_LS =  baseCommonDao.getDataListByFullSql(derDY_LS.toString()); //获取历史权利的单元信息
				if(listDY_LS != null && listDY_LS.size()>0){
					int dyxz_size=results.size();//获取现状单元长度，用于合并历史权利的单元
					boolean True  = true;
					Map<String,String> map2 = new HashMap<String, String>();//记录现状单元,用于现状单元只有一个，历史单元有多个的合并情况。
					List<String> listBDCQZH = new ArrayList<String>();
					for(int dy_ls=0; dy_ls<listDY_LS.size(); dy_ls++){
						Map<String,String> map = new HashMap<String, String>();
						String bdcqzh = StringHelper.formatObject(listDY_LS.get(dy_ls).get("BDCQZH"));										
						if(bdcqzh != null && bdcqzh.indexOf("/") != -1 || bdcqzh.indexOf("、") != -1){//去除不是房的产权证号
							 String[] strsBDCQZH = bdcqzh.split("/|、");											 											
							 for(int a=0;a<strsBDCQZH.length;a++){
								 if(strsBDCQZH[a].indexOf("房") != -1){
									 bdcqzh = strsBDCQZH[a];
								 }
							 }
						}
						if(bdcqzh != null && !"".equals(bdcqzh)	&& bdcqzh.indexOf("不动产") == -1 && !listBDCQZH.contains(bdcqzh)){//只要原房产数据
							listBDCQZH.add(bdcqzh);
							map.put("LSZL", StringHelper.formatObject(listDY_LS.get(dy_ls).get("ZL")));
							map.put("YBDCQZH",bdcqzh);
									
							//获取该单元办理的业务流程名称
							derDY_LS.setLength(0);
							derDY_LS.append("SELECT distinct PT.PRODEF_NAME FROM BDC_WORKFLOW.WFI_PROINST PT LEFT JOIN BDCK.BDCS_XMXX XMXX ON XMXX.PROJECT_ID=PT.FILE_NUMBER");
							derDY_LS.append(" LEFT JOIN (SELECT TRIM(SQRXM) AS SQRXM,XMBH FROM BDCK.BDCS_SQR) SQR");
							derDY_LS.append(" ON SQR.XMBH=XMXX.XMBH LEFT JOIN BDCK.BDCS_DJDY_GZ DJDY ON DJDY.XMBH=SQR.XMBH");
							derDY_LS.append(" LEFT JOIN  BDCK.BDCS_QL_LS QL ON QL.DJDYID=DJDY.DJDYID WHERE XMXX.SFDB='1' AND XMXX.DJLX='200' AND SQR.SQRXM='");
							derDY_LS.append(qlrmc);
							derDY_LS.append("' AND QL.QLID='");
							derDY_LS.append(StringHelper.formatObject(listDY_LS.get(dy_ls).get("QLID")));
							derDY_LS.append("'");	
							List<Map> listPRODEF_NAME =  baseCommonDao.getDataListByFullSql(derDY_LS.toString()); 
							if(listPRODEF_NAME != null && listPRODEF_NAME.size()>0){
								String strPRODEF_NAME = StringHelper.formatObject(listPRODEF_NAME.get(0).get("PRODEF_NAME"));							
									map.put("PRONAME",strPRODEF_NAME);								
							}
							
							//把现状单元与历史单元合并在一行显示																		
							if(((results.size()==listDY_LS.size() || results.size()>listDY_LS.size()) && listDY_LS.size()>0)
									&& results.size()>0 && listDY_LS.size()>0){
								Map maplistDY_LS = results.get(dy_ls);										
								maplistDY_LS.putAll(map);
								results.remove(dy_ls);
								results.add(dy_ls,maplistDY_LS);
							}
							if(results.size()<listDY_LS.size() && results.size()>0 && listDY_LS.size()>0){										
								if(dyxz_size==1)
									map2 = results.get(0);
								dyxz_size--;
								if(dyxz_size<0){
									if(True){
										map.putAll(map2);	
										True = false;
									}else{
										map.put("BH", map2.get("BH"));
										map.put("CDBH", map2.get("CDBH"));
										map.put("CDR", map2.get("CDR"));
										map.put("QLRMC", map2.get("QLRMC"));
										map.put("ZJH", map2.get("ZJH"));
									}											
									results.add(map);
								}else{
									Map maplistDY_XZ = results.get(dyxz_size);											
									map.putAll(maplistDY_XZ);											
									results.remove(dyxz_size);
									results.add(dyxz_size,map);																					
								}										
							}
						}															
					}
				}						
			total += results.size();					
			for(Map<String,String> mp : results){
				if(SFSFZH)//判断证件号是否是身份证号，1是，0否。
					mp.put("SFSFZH", "1");
				else
					mp.put("SFSFZH", "0");
				mp.put("CDR", cdr);// 查档人
				toExcleLists.add(mp);
			}
	}		
		baseCommonDao.flush();
		message.setTotal(total);
		message.setRows(toExcleLists);

		return message;
	}
	
	/**
	 *  匹配附属权利数据（抵押、查封）
	 *   huangpeifeng
	 * @date 20170815
	 * @param cdyt 查档用途
	 * @param sffk 是否房开
	 * @param sfyg 是否预告
	 * @param queryVauleList
	 * @return
	 */
	public Message  getFSQLData(String cdyt, String sffk, String sfyg,List<Map> queryVauleList){
		
		Message message = new Message();		
		long count = 0;
		Map<String, String> queryvalues = new HashMap<String, String>();	
		List<Map> toExcleLists = new ArrayList<Map>();
		StringBuilder derQL = new StringBuilder(); 			
		String xzq =ConfigHelper.getNameByValue("XZQHDM");
		
		for(Map<String, String> qvp : queryVauleList){					
			
			List<Map> results = new ArrayList<Map>();			
			String bh = StringHelper.formatObject(qvp.get("BH")) ;//编号
			String cdbh = "";//查档编号
			String cdr =StringHelper.formatObject(qvp.get("CDR")) ;//查档人
			String qlrmc = StringHelper.formatObject(qvp.get("QLRMC"));//权利人名称
			String zjh = StringHelper.formatObject(qvp.get("ZJH"));//身份证号
			String zl = StringHelper.formatObject(qvp.get("ZL")) ;//坐落
			String lszl = StringHelper.formatObject(qvp.get("LSZL"));//历史坐落
			String qvp_mj = StringHelper.formatObject(qvp.get("MJ")) ;//面积
			String qvp_fwyt = StringHelper.formatObject( qvp.get("FWYT"));//房屋用途
			String sfqcq = StringHelper.formatObject(qvp.get("SFQCQ"));
			
			Map<String,Object> mapQLR = new HashMap<String,Object>();//存传进来的权利人信息
			if("无".equals(zl) || zl.length()<=0){//判断是否缺少坐落信息，并返回情况
				mapQLR.put("SFBS","1");
				mapQLR.put("BH", bh);
				mapQLR.put("QLRMC", qlrmc);
				mapQLR.put("ZJH", zjh);
				mapQLR.put("ZL", "缺少坐落信息，无法查询");
				toExcleLists.add(mapQLR);
			//	count++;
				continue;
			}
			if("无".equals(qlrmc) || qlrmc.length()<=0){//判断是否缺少权利人信息，并返回情况
				mapQLR.put("SFBS","1");
				mapQLR.put("BH", bh);
				mapQLR.put("QLRMC", "缺少权利人信息，无法查询");
				mapQLR.put("ZJH", zjh);
				mapQLR.put("ZL", zl);
				toExcleLists.add(mapQLR);
			//	count++;
				continue;
			}
			if("0".equals(sffk) && ("无".equals(qlrmc) || qlrmc.length()<=0)){//判断是否缺少身份证信息（排除房开查询），并返回情况
				mapQLR.put("SFBS","1");
				mapQLR.put("BH", bh);
				mapQLR.put("QLRMC", qlrmc);
				mapQLR.put("ZJH", "缺少身份证信息，无法查询");
				mapQLR.put("ZL", zl);
				toExcleLists.add(mapQLR);
			//	count++;
				continue;
			}
			if("".equals(StringHelper.formatObject(qvp.get("CDBH")))){//获取查档编号
				cdbh = getCdbh(qlrmc,zjh,"不动产权利登记信息查询");
			}else{
				cdbh = qvp.get("CDBH");
			}
			boolean SFSFZH = false;
			if(!StringHelper.isEmpty(zjh)){
				SFSFZH = ConverIdCard.checkIDCard(zjh);
			}
			derQL.setLength(0);
			derQL.append("SELECT QLR.QLRMC,QLR.ZJH,QL.DJDYID,QL.DJLX,QL.QLLX,QLR.BDCQZH,QL.DJSJ,H.FWYT1,H.ZL,H.SCJZMJ,H.YCJZMJ,QL.QLID,QL.FJ,QLR.QLRID,QLR.GYFS,H.FWXZ FROM BDCK.BDCS_QL_XZ QL LEFT JOIN");	
			derQL.append(" (SELECT TRIM(QLRMC) AS QLRMC,QLID,QLRID,GYFS,ZJH,BDCQZH FROM BDCK.BDCS_QLR_XZ) QLR ON QLR.QLID=QL.QLID LEFT JOIN BDCK.BDCS_DJDY_XZ DJDY");
			derQL.append(" ON DJDY.DJDYID=QL.DJDYID LEFT JOIN");
			derQL.append(" (SELECT BDCDYID,FWYT1,ZL,SCJZMJ,YCJZMJ,FWXZ FROM BDCK.BDCS_H_XZ UNION SELECT BDCDYID,FWYT1,ZL,SCJZMJ,YCJZMJ,FWXZ FROM BDCK.BDCS_H_XZY) H");
			derQL.append(" ON H.BDCDYID=DJDY.BDCDYID WHERE QLR.QLRMC='");				
			derQL.append(qlrmc);
			derQL.append("'");
			if("0".equals(sffk)){//判断是否是房开查询，房开查询只用名字为查询条件，SFFK为1是房开查询
				if (!StringHelper.isEmpty(zjh)) {
					if (zjh.length() == 18) {
						String oldCard = ConverIdCard.getOldIDCard(zjh);
						derQL.append(" AND (" + "QLR.ZJH ='" + zjh + "' OR QLR.ZJH = '" + oldCard + "')");
					} else {
						derQL.append(" AND" + " QLR.ZJH = '" + zjh + "' ");
					}
				}
			}
			derQL.append(" AND H.ZL='");
			derQL.append(zl);
			derQL.append("'");
			List<Map> listQL = baseCommonDao.getDataListByFullSql(derQL.toString());//查询权利和户的信息
			if(listQL != null && listQL.size()>0){				
			//	Map map2 = new HashMap();//存储房屋信息，用于一个房屋多个附属权利合并在一行显示。
				Map<String,Object> map3 = new HashMap<String,Object>();//存储各个登记数量
				Map<String,Object> map4 = new HashMap<String,Object>();//存储没有附属权利的单元信息	
				List<Map> listPutall = new ArrayList<Map>();//用于存储合并的数据
				for(int i=0; i<listQL.size(); i++){											
					List<Map> listDYQ = new ArrayList<Map>();//记录抵押权登记数据
					List<Map> listCFQ = new ArrayList<Map>();//记录查封登记数据
					List<Map> listYGQ = new ArrayList<Map>();//记录预告登记数据
					List<Map> listYYQ = new ArrayList<Map>();//记录异议登记数据
					List<Integer> nums = new ArrayList<Integer>();//记录各个登记的list数据数量
					boolean booleanDYZT = true;//抵押状态
					boolean booleanCFZT = true;//查封状态
					String djdyid = StringHelper.formatObject(listQL.get(i).get("DJDYID"));//登记单元id
					String mj = StringHelper.formatDouble(listQL.get(i).get("SCJZMJ"));//户实测建筑面积
					if(mj.length()<=0)
						mj = StringHelper.formatDouble(listQL.get(i).get("YCJZMJ"));//户预测建筑面积												
					StringBuilder derFSQL = new StringBuilder();						
					derFSQL.append("SELECT QLR.QLRMC,QLR.BDCQZH,QL.DJLX,QL.QLLX,QL.DJSJ,FSQL.DYFS,FSQL.BDBZZQSE,FSQL.ZGZQSE,FSQL.CFJG,FSQL.CFWH,QL.QLQSSJ,QL.QLJSSJ FROM ");	
					derFSQL.append(" BDCK.BDCS_FSQL_XZ FSQL LEFT JOIN BDCK.BDCS_QL_XZ QL ON QL.QLID=FSQL.QLID");
					derFSQL.append(" LEFT JOIN BDCK.BDCS_QLR_XZ QLR ON QLR.QLID=QL.QLID WHERE QL.DJDYID='");
					derFSQL.append(djdyid);
					derFSQL.append("' ORDER BY QL.QLLX ASC,QL.DJSJ");				
					List<Map> listFSQL = baseCommonDao.getDataListByFullSql(derFSQL.toString());//查询附属权利和权利人信息
					if(listFSQL != null && listFSQL.size()>0){							
						int dys = 0;//抵押数量
						int cfs = 0;//查封数量
						int ygs = 0;//预告数量
						int yys = 0;//异议数量					
						for(Map maps : listFSQL){//记录抵押、查封、预告、异议等登记数量
							if("23".equals(maps.get("QLLX"))){
								if("450300".equals(xzq)){//桂林需求只要不动产登记的抵押
									if(StringHelper.formatObject(maps.get("BDCQZH")).indexOf("证明") != -1)
										dys += 1;						
								}else{
									dys += 1;
								}					
							}
							if("99".equals(maps.get("QLLX"))){
								cfs += 1;
							}
							if("700".equals(maps.get("DJLX")) && (QLLX.GYJSYDSHYQ_FWSYQ.Value.equals(maps.get("QLLX")) 
									|| QLLX.JTJSYDSYQ_FWSYQ.Value.equals(maps.get("QLLX")) || QLLX.ZJDSYQ_FWSYQ.Value.equals(maps.get("QLLX")))){
								ygs += 1;
							}
							if("600".equals(maps.get("DJLX"))){
								yys += 1;
							}					
						}
						map3.put("SFBS","1");//是否笔数，用于区分map数据是数量还是单元信息，1是，0否。
						if("450300".equals(xzq)){//桂林需求只要不动产登记的抵押
							map3.put("DY", dys);
						}else{
							map3.put("DY", dys);
							map3.put("CF", cfs);
							map3.put("YG", ygs);
							map3.put("YY", yys);
						}									
						int countDY = 0;//记录没有附属权利的单元数量
						for(Map maps : listQL){	
							if(dys==0 && cfs==0 && ygs==0 && yys==0){//记录抵押没有附属权利的单元信息							
								if(SFSFZH)//判断证件号是否是身份证，1是，0否。
									map4.put("SFSFZH", "1");
								else
									map4.put("SFSFZH", "0");
								map4.put("CDBH", cdbh);//查档编号
								map4.put("BH", bh);//编号
								map4.put("QLRMC", maps.get("QLRMC"));
								map4.put("CDR", cdr);//查档人
								map4.put("ZJH", maps.get("ZJH"));
								map4.put("BDCQZH", maps.get("BDCQZH"));//不动产权证号
								map4.put("DJSJ", StringHelper.FormatByDatetime(maps.get("DJSJ")));//登记时间
								map4.put("ZL", maps.get("ZL"));//坐落（现状）
								map4.put("LSZL", lszl);//历史坐落
								map4.put("MJ", qvp_mj);//面积
								map4.put("FWYT", qvp_fwyt);//房屋用途
								map4.put("DJLX", maps.get("DJLX"));
								map4.put("DJLXMC", ConstHelper.getNameByValue("DJLX", StringHelper.formatObject(maps.get("DJLX"))));
								map4.put("QLLX", "");
								map4.put("SFQCQ", ConstHelper.getNameByValue("GYFS", StringHelper.formatObject(maps.get("GYFS"))));
								map4.put("FWXZ", ConstHelper.getNameByValue("FWXZ", StringHelper.formatObject(maps.get("FWXZ"))));//房屋性质
								map4.put("FJ", StringHelper.formatObject(maps.get("FJ")));
								if (!"0".equals(maps.get("GYFS"))) {
									String qlid = StringHelper.formatObject(maps.get("QLID"));
									String qlrid = StringHelper.formatObject(maps.get("QLRID"));
									map4.put("GYRMC", getGYRMC(qlid, qlrid));
								}
								map4.putAll(map3);
								toExcleLists.add(map4);
								countDY++;
							//	count++;
							}								
						}
						if(countDY==listQL.size())//没有附属权利就跳到下次循环，提高效率、
							continue;				
						for(int intfsql=0; intfsql<listFSQL.size(); intfsql++){			
							Map<String,Object> map = new HashMap<String,Object>();
							String qllx = StringHelper.formatObject(listFSQL.get(intfsql).get("QLLX"));//权利类型
							String djlx = StringHelper.formatObject(listFSQL.get(intfsql).get("DJLX"));//登记类型
							String djsj = StringHelper.FormatByDatetime(listFSQL.get(intfsql).get("DJSJ"));//登记时间	
							String strQLRMC = StringHelper.formatObject(listFSQL.get(intfsql).get("QLRMC"));//权利人名称
							map.put("BH", bh);
							map.put("DJLX", djlx);
							if(QLLX.DIYQ.Value.equals(qllx)){	//抵押权												
							//	count++;
								if("450300".equals(xzq)){//桂林需求只要不动产登记的抵押
									if(intfsql==0 && StringHelper.formatObject(listFSQL.get(intfsql).get("BDCQZH")).indexOf("证明") == -1){
										if(!StringHelper.isEmpty(listQL.get(i).get("FWYT1")))//判断房屋用途是否为空
											map.put("FWYT", ConstHelper.getNameByValue("FWYT",StringHelper.formatObject(listQL.get(i).get("FWYT1"))));
										else
											map.put("FWYT","");	
										map.put("ZJH",  StringHelper.formatObject(listQL.get(i).get("ZJH")));
										map.put("QLRMC",  StringHelper.formatObject(listQL.get(i).get("QLRMC")));
										map.put("BDCQZH",  StringHelper.formatObject(listQL.get(i).get("BDCQZH")));
										map.put("ZL",   StringHelper.formatObject(listQL.get(i).get("ZL")));
										map.put("LSZL", lszl);
										map.put("MJ",mj);
										
										map.putAll(map3);
										listDYQ.add(map);
										continue;
									}
									if(intfsql !=0 && StringHelper.formatObject(listFSQL.get(intfsql).get("BDCQZH")).indexOf("证明") == -1){
										map.put("SFBS","0");
										continue;
									}
								}
								String dyje = "";
								String dyfs = StringHelper.formatObject(listFSQL.get(intfsql).get("DYFS"));//抵押方式
								if("1".equals(dyfs)){
									dyje= StringHelper.formatDouble(listFSQL.get(intfsql).get("BDBZZQSE"));//被担保债权金额
								}else if("2".equals(dyfs)){
									dyje = StringHelper.formatDouble(listFSQL.get(intfsql).get("ZGZQSE"));//最高债权金额
								}
								map.put("DYQR", strQLRMC);//抵押权人
								map.put("DYFS", dyfs);//抵押方式
								map.put("DYJE", dyje);//抵押金额
								map.put("DYDJSJ", djsj);
								map.put("DYZH", StringHelper.formatObject(listFSQL.get(intfsql).get("BDCQZH")));
								map.put("DYDJLX",  djlx);
								map.put("DYQLLX", qllx);									
								if(booleanDYZT){
									map.put("DYZT", "该单元已抵押");
									booleanDYZT = false;
								}										
								if(intfsql != 0)
									listDYQ.add(map);
							}
							if(!"450300".equals(xzq) && QLLX.QTQL.Value.equals(qllx)){	//查封			
								///	count++;									
								map.put("CFJG", StringHelper.formatObject(listFSQL.get(intfsql).get("CFJG")));//查封机构
								map.put("CFWH", StringHelper.formatObject(listFSQL.get(intfsql).get("CFWH")));//查封文号
								map.put("CFDJSJ", djsj);
								map.put("CFDJLX",  djlx);
								map.put("CFQLLX", qllx);
								map.put("CFQSSJ", StringHelper.FormatByDatetime(listFSQL.get(intfsql).get("QSSJ")));
								map.put("CFJSSJ", StringHelper.FormatByDatetime(listFSQL.get(intfsql).get("JSSJ")));
								if(booleanCFZT){
									map.put("CFZT", "该单元已查封");
									booleanCFZT = false;
								}										
								if(intfsql != 0)
									listCFQ.add(map);
							}
							if(!"450300".equals(xzq) && DJLX.YGDJ.Value.equals(djlx) && (QLLX.GYJSYDSHYQ_FWSYQ.Value.equals(qllx) 
									|| QLLX.JTJSYDSYQ_FWSYQ.Value.equals(qllx) || QLLX.ZJDSYQ_FWSYQ.Value.equals(qllx))){//预告										
								//count++;									
								map.put("YGQLR", strQLRMC);//预告权利人
								map.put("YGZH", StringHelper.formatObject(listFSQL.get(intfsql).get("BDCQZH")));
								map.put("YGDJXX", ConstHelper.getNameByValue("DJLX", djlx));			
								map.put("YGDJSJ", djsj);
								map.put("YGDJLX",  djlx);
								map.put("YGQLLX", qllx);
								map.put("SFQCQ", sfqcq);
								if(intfsql != 0)
									listYGQ.add(map);
							}				
							if(!"450300".equals(xzq) && DJLX.YYDJ.Value.equals(djlx)){	//异议														
							//	count++;									
								map.put("YYQLR", strQLRMC);								
								map.put("YYDJSJ", djsj);
								map.put("YDJLX",  djlx);
								map.put("YQLLX", qllx);
								if(intfsql != 0)
									listYYQ.add(map);
							}
							if(!QLLX.DIYQ .equals(qllx) && !QLLX.CFZX.equals(qllx) && !QLLX.QTQL.equals(qllx)){ //产权登记时间
								map.put("DJSJ", djsj);
							}
							//只在每个单元的第一行显示权利人和户的基本信息	
							if(intfsql==0){																
								
								if(!StringHelper.isEmpty(listQL.get(i).get("FWYT1")))//判断房屋用途是否为空
									map.put("FWYT", ConstHelper.getNameByValue("FWYT",StringHelper.formatObject(listQL.get(i).get("FWYT1"))));
								else
									map.put("FWYT","");	
								map.put("ZJH",  StringHelper.formatObject(listQL.get(i).get("ZJH")));
								map.put("QLRMC",  StringHelper.formatObject(listQL.get(i).get("QLRMC")));
								map.put("BDCQZH",  StringHelper.formatObject(listQL.get(i).get("BDCQZH")));
								map.put("ZL",   StringHelper.formatObject(listQL.get(i).get("ZL")));
								map.put("LSZL", lszl);
								map.put("MJ",mj);
								map.put("SFQCQ", sfqcq);
								map.putAll(map3);
								listDYQ.add(map);
								
							}else{
								map.put("SFBS","0");//是否笔数，用于区分map数据是数量还是单元信息，1是，0否。
							}
						}
					}
					
					/*
					 * 把各个登记权利合并在一行
					 */
					nums.add(listDYQ.size());
					nums.add(listCFQ.size());
					nums.add(listYGQ.size());
					nums.add(listYYQ.size());
					int max = Collections.max(nums);						
					for(;max>=0;max--){						
						Map mapDYQ = new HashMap();//抵押权
						Map mapCFQ = new HashMap();//查封权
						Map mapYGQ = new HashMap();//预告权
						Map mapYYQ = new HashMap();//异议权							
						if(listDYQ.size()>max)
							mapDYQ = listDYQ.get(max);							
						if(listCFQ.size()>max)
							mapCFQ = listCFQ.get(max);						
						if(listYGQ.size()>max)
							mapYGQ = listYGQ.get(max);						
						if(listYYQ.size()>max)
							mapYYQ = listYYQ.get(max);							
						mapDYQ.putAll(mapCFQ);
						mapDYQ.putAll(mapYGQ);
						mapDYQ.putAll(mapYYQ);
						if(max==0)
							mapDYQ.put("SFBS", "1");
						if(!mapDYQ.isEmpty()){
							if(SFSFZH)//判断证件号是否是身份证，1是，0否。
								mapDYQ.put("SFSFZH", "1");
							else
								mapDYQ.put("SFSFZH", "0");
							mapDYQ.put("CDBH", cdbh);//查档编号
							mapDYQ.put("CDR", cdr);//查档人
							listPutall.add(mapDYQ);	
						}	
					}
					for(int a=listPutall.size(); a>0 ; a--){
					//	count++;							
						toExcleLists.add(listPutall.get(a-1));
					}
				}
			}else{
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("SFBS", "1");
				map.put("QLRMC", qlrmc);
				map.put("BDCQZH", qvp.get("BDCQZH"));
				map.put("ZL", zl);
				map.put("FWYT", "没有找到该单元信息，请仔细核查权利人姓名或坐落是否正确");
				toExcleLists.add(map);
			}			
		}
		baseCommonDao.flush();
	//	request.getSession().setAttribute("MRESULTS_2", toExcleLists);//匹配结果集
		message.setMsg("匹配成功"+toExcleLists.size()+"条");
		message.setTotal(toExcleLists.size());
		message.setRows(toExcleLists);

	return message;
	}
	/**
	 * 打印证明：有无房产数据匹配
	 */
	public Message getPlPrntZM(List<Map>  results, String jbr){
		Message message = new Message();
		List<Object> searchResults = new ArrayList<Object>();	
	//	List<Map>  results = (List<Map>) request.getSession().getAttribute("MRESULTS");		
		/*for(int i=0;i < results.size();i++){
			List<Map> rlist = new ArrayList<Map>();		
			String bh = (String) results.get(i).get("BH");
			String cdrmc = (String) results.get(i).get("QLRMC");
			String cdrzjh = (String) results.get(i).get("ZJH");
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
		
		}*/		
		
		/*
		 * hpf改
		 * 20170926
		 */
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		List<String> listZL = new ArrayList<String>();
		List<String> listBDCQZH = new ArrayList<String>();
		int a = 0;
		int c = 0;
		for(int i=0 ; i<results.size(); i++){
			Map<String,Object> map = results.get(i);		
			String zl = StringHelper.formatObject(map.get("ZL"));
			String bdcqzh = StringHelper.formatObject(map.get("BDCQZH"));
	/*		if(Collections.frequency(listZL, zl) < 1){//过滤重复的现状单元,如果重复就把坐落和不动产权证号设为空。
				listZL.add(zl);
				listBDCQZH.add(bdcqzh);
			}else{
				if(Collections.frequency(listBDCQZH, bdcqzh) >0){
					map.put("ZL", "");		
					map.put("BDCQZH", "");
				}
			}	*/		
			String qlrmc = "";
			String xzqhmc = ConfigHelper.getNameByValue("XZQHMC");//获取行政区号
			map.put("CDMD", "有无房产");
			map.put("XZQHMC", xzqhmc);
			map.put("JBR", jbr);
			if(i==0){
				list.add(map);
			}else{
				Map<String,Object> map2 = list.get(a);//获取上一个map
				a++;
				String qlrmc2 = "";
				if(map.containsKey("QLRMC")){
					qlrmc = (String)map.get("QLRMC");
				}
				if(map2.containsKey("QLRMC")){
					qlrmc2 = (String)map2.get("QLRMC");
				}
				if(qlrmc.indexOf(qlrmc2) != -1){//把同一个权利人的数据放一个list。						
					list.add(map);
					c++;
					if(c!=0 && i==results.size()-1){
						List<Map<String,Object>> listmap = new  ArrayList<Map<String,Object>>();
						for(int b=0 ; b<list.size(); b++){	//处理List<map>类型覆盖数据问题						
							listmap.add(list.get(b));
							if(b==list.size()-1)
								searchResults.add(listmap);
						}
					}
				}else{
					
					List<Map<String,Object>> listmap = new  ArrayList<Map<String,Object>>();
					for(int b=0 ; b<list.size(); b++){	//处理List<map>类型覆盖数据问题						
						listmap.add(list.get(b));
						if(b==list.size()-1)
							searchResults.add(listmap);
					}
					if(i==results.size()-1){
						List<Map<String,Object>> list2 = new  ArrayList<Map<String,Object>>();
						list2.add(map);
						searchResults.add(list2);
					}					
					list.clear();						
					list.add(map);
					a = 0;
					c = 0;
				}							
			}		
		}
		if(searchResults.size()<=0){
			searchResults.add(list);
		}
		Date currdate = new Date();
		String datestring = "";
		SimpleDateFormat  formatter  = new SimpleDateFormat("yyyy-MM-dd");
		try {    
	           datestring = formatter.format(currdate);  
		} catch (Exception e) {    
	           e.printStackTrace();     
		}   
		baseCommonDao.flush();
		message.setMsg(datestring);
		message.setTotal(searchResults.size());
		message.setRows(searchResults);
		message.setSuccess("返回成功！");
		return message;
	}
	
	/**
	 * 返回权利查档证明内容
	 * @author heks
	 */
	public Message getSwplPrntZM(List<Map> results, String jbr){
		Message msg = new Message();
		List<Object> searchResults = new ArrayList<Object>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();	
		if(results != null &&results.size() > 0){
			/*	for(Map map : results){
				String qlrmc = (String) map.get("QLRMC");// 权利人名称
				String qlrzjh = (String) map.get("ZJH");// 权利人证件号
				String fwzl = (String) map.get("ZL");// 房屋坐落
				String bdcqzh = (String) map.get("QZH");// 不动产权证号
				String bh = (String) map.get("BH");// 编号

			//	List<GX_CONFIG> cdjls = baseCommonDao.getDataList(GX_CONFIG.class, "CDRMC = '"+qlrmc+"' AND CDRZJH = '"+qlrzjh+"' and cdmd = '不动产权利登记信息查询' order by cdtime desc");

				List<Map> rlist = new ArrayList<Map>();
				Map cmap = new HashMap();

			//	if(cdjls.size() > 0){
				//	String cdbh = cdjls.get(0).getCdbh();
				//	String cdmd = cdjls.get(0).getCdmd();					
				cmap.put("CDBH", StringHelper.formatObject(map.get("CDBH")));
				cmap.put("CDMD", "不动产权利登记信息查询");
			//	}
				cmap.put("QLRMC", qlrmc);
				cmap.put("ZJH", qlrzjh);
				cmap.put("ZL", fwzl);
				cmap.put("BDCQZH", bdcqzh);
				cmap.put("BH", bh);
				//加行政区名称
				String xzqhmc = ConfigHelper.getNameByValue("XZQHMC");
				cmap.put("XZQHMC", xzqhmc);
				
				List<Map> alists = this.getAllQlInfoByQlr(qlrmc, qlrzjh,bdcqzh);
				
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
			}*/
				
			/*
			 * hpf改
			 * 20170926
			 */
			List<Map> list = new ArrayList<Map>();
			int a = 0;
			int c = 0;
			for(int i=0 ; i<results.size() ; i++){
				Map map = results.get(i);
				String cdbh = "";				
				String xzqhmc = ConfigHelper.getNameByValue("XZQHMC");//获取行政区号
				map.put("XZQHMC", xzqhmc);
				map.put("CDMD", "不动产权利登记信息查询");
				map.put("JBR", jbr);
				if(i==0){					
					list.add(map);
				}else{					
					Map map2 = list.get(a);//获取上一个map
					a++;
					String cdbh2 = "";
					if(map.containsKey("CDBH")){
						cdbh = (String)map.get("CDBH");
					}
					if(map2.containsKey("CDBH")){
						cdbh2 = (String)map2.get("CDBH");
					}
					if(cdbh.indexOf(cdbh2) != -1){//根据相同的查档编号判断是同一个单元，把属于同一个单元的权利、附属权利放一个list。						
						list.add(map);	
						c++;
						if(c!=0 && i==results.size()-1){
							List<Map<String,Object>> listmap = new  ArrayList<Map<String,Object>>();
							for(int b=0 ; b<list.size(); b++){	//处理List<map>类型覆盖数据问题						
								listmap.add(list.get(b));
								if(b==list.size()-1)
									searchResults.add(listmap);
							}
						}
					}else{
						
						List listmap = new  ArrayList();
						for(int b=0 ; b<list.size(); b++){	//处理List<map>类型覆盖数据问题						
							listmap.add(list.get(b));
							if(b==list.size()-1)
								searchResults.add(listmap);
						}	
						if(i==results.size()-1){
							List<Map<String,Object>> list2 = new  ArrayList<Map<String,Object>>();
							list2.add(map);
							searchResults.add(list2);
						}			
						list.clear();						
						list.add(map);
						a = 0;
						c = 0;
					}							
				}
									
			}
			if(searchResults.size()<=0){
				searchResults.add(list);
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
		baseCommonDao.flush();
		msg.setMsg(datestring);
		msg.setTotal(searchResults.size());
		msg.setRows(searchResults);
		msg.setSuccess("返回成功！");
		return msg;
	}
	
	/**
	 * 返回宗地查档证明内容
	 * 
	 */
	public Message getZDPLPrintZM(List<Map> results, String jbr){
		Message msg = new Message();
		List<Object> searchResults = new ArrayList<Object>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		if(results != null &&results.size() > 0){
			for(Map map : results){
				
				String qlrmc = StringHelper.formatObject(map.get("QLRMC"));// 权利人名称
				String qlrzjh = StringHelper.formatObject(map.get("ZJHM"));// 权利人证件号
				String bdcdylxmc = StringHelper.formatObject(map.get("BDCDYLXMC"));// 不动产单元类型名称
				String djlx = StringHelper.formatObject(map.get("DJLX"));// 登记类型
				String zl = StringHelper.formatObject(map.get("ZL"));// 坐落
				String bdcqzh = StringHelper.formatObject(map.get("BDCQZH"));// 不动产权证号
				String djsj = StringHelper.formatObject(map.get("DJSJ"));//登记时间
				String bh = StringHelper.formatObject(map.get("BH"));// 编号
				String mj = StringHelper.formatObject(map.get("ZDMJ"));// 宗地面积
				String cfzt = StringHelper.formatObject(map.get("CFZT"));// 查封状态
				String dyzt = StringHelper.formatObject(map.get("ZDMJ"));// 抵押状态
				String xzzt = StringHelper.formatObject(map.get("XZZT"));// 限制状态
				String yyzt = StringHelper.formatObject(map.get("YYZT"));// 异议状态

				List<GX_CONFIG> cdjls = baseCommonDao.getDataList(GX_CONFIG.class, "CDRMC = '"+qlrmc+"' AND CDRZJH = '"+qlrzjh+"' AND CDMD = '宗地信息查询' ORDER BY CDTIME DESC");

				List<Map> rlist = new ArrayList<Map>();
				Map cmap = new HashMap();

				if(cdjls.size() > 0){
					String cdbh = cdjls.get(0).getCdbh();
					String cdmd = cdjls.get(0).getCdmd();

					cmap.put("CDBH", cdbh);
					cmap.put("CDMD", cdmd);
				}

				//TODO
				String cdr = "";
				if (!StringHelper.isEmpty(map.get("CDR"))) {
					cmap.put("CDR", map.get("CDR"));
				} else {
					cmap.put("CDR", qlrmc);
				}
				if (!StringHelper.isEmpty(djlx)) {
					cmap.put("DJLXMC", ConstHelper.getNameByValue("DJLX", djlx));
				}
				cmap.put("JBR", jbr);
				cmap.put("QLRMC", qlrmc);
				cmap.put("ZJHM", qlrzjh);
				cmap.put("BDCDYLXMC", bdcdylxmc);
				cmap.put("ZL", zl);
				cmap.put("BDCQZH", bdcqzh);
				cmap.put("DJSJ", djsj);
				cmap.put("BH", bh);
				cmap.put("MJ", mj);
				cmap.put("CFZT", cfzt);
				cmap.put("DYZT", dyzt);
				cmap.put("XZZT", xzzt);
				cmap.put("YYZT", yyzt);
				cmap.put("isOK", map.get("isOK"));
				//加行政区名称
				String xzqhmc = ConfigHelper.getNameByValue("XZQHMC");
				cmap.put("XZQHMC", xzqhmc);
				
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
	 * 根据权利人名称和证件号码获取对应的坐落和不动产权证号的房屋下的产权和其他权利
	 * @author heks
	 * @date 2016/12/08 15:05:49
	 * @param qlrmc
	 * @param qlrzjh
	 * @return
	 */
	public List<Map> getAllQlInfoByQlr(String qlrmc,String qlrzjh,String bdcqzh){
		Message msg = new Message();
		StringBuffer qlr_zjhbuilder = new StringBuffer();
		// 权利人，权利人的条件比较特殊，where里边又套了where
		if (qlrmc != null) {

			qlr_zjhbuilder.append(" and d.qlrmc = '" +qlrmc+"'");

			if (qlrzjh != null && qlrzjh.length() == 18) {
				String oldCard = ConverIdCard.getOldIDCard(qlrzjh);
				if (oldCard.length() != 15) {
					qlr_zjhbuilder.append(" and" + " d.zjh = '" + qlrzjh
							+ "' ");
				} else {
					qlr_zjhbuilder.append(" and (" + "d.zjh ='" + qlrzjh
							+ "' or ");
					qlr_zjhbuilder.append(" " + " d.zjh = '" + oldCard
							+ "')");
				}
			} /*else {
				qlr_zjhbuilder.append(" and" + " d.zjh = '" + qlrzjh + "' ");
			}*/

		}else{
			return null;
		}
		if(!StringHelper.isEmpty(bdcqzh)){
			qlr_zjhbuilder.append(" and a.bdcqzh like '%" +bdcqzh+"%'");
		}
		String sqlString = "select distinct a.bdcdyh from bdck.bdcs_ql_xz a join bdck.bdcs_qdzr_xz c on "
				+ "a.bdcdyh = c.bdcdyh join bdck.bdcs_qlr_xz d on c.qlrid = d.qlrid where 1=1"+qlr_zjhbuilder.toString();
		
		List<Map> qlList = baseCommonDao.getDataListByFullSql(sqlString);

		List<Map> list = new ArrayList<Map>();
		if(qlList.size() > 0){
			for(Map q : qlList){
				String fullSql = "select a.bdcqzh,a.djlx,a.qlid,b.dymj,b.bdbzzqse,a.djsj,b.cfjg,b.cfwh,b.cffw,a.qllx,b.dyfs,b.zgzqse "
						+ " from bdck.bdcs_ql_xz a join bdck.bdcs_fsql_xz b on a.fsqlid = b.fsqlid  where  a.bdcdyh = '"+q.get("BDCDYH")+"'";

				List<Map> allQqlInfos = baseCommonDao.getDataListByFullSql(fullSql);
				if(allQqlInfos.size() > 0){
					for(Map map : allQqlInfos){
						Map<String, String> ql = new HashMap();
						ql.putAll(map);
						String sql = "select qlrmc from bdck.bdcs_qlr_xz where qlid = '"+map.get("QLID")+"'";
						List<Map> qlrList = baseCommonDao.getDataListByFullSql(sql);
						String _qlrmc = "";
						if(qlrList.size() > 0){
							for(Map qlr : qlrList){
								_qlrmc += qlr.get("QLRMC") + ",";
							}
						}
						if(_qlrmc != null && !_qlrmc.equals("")){
							_qlrmc = _qlrmc.substring(0, _qlrmc.length()-1);
							ql.put("QLRMC", _qlrmc);
							
						}
						list.add(ql);
					}
				}
			}
		}
	
		baseCommonDao.flush();
		return list;		
	}
	
	/**
	  个人查档：根据名称和证件号码获取对应的房屋产权和其他权利
	 * @author huangpeifeng
	 * @date 20170927
	 * @param xm 姓名
	 * @param zjh 证件号
	 */
	public Message getWhthinMatchData(String xm, String zjh, String jbr, String cdr){
		
		Message message = new Message();
		List<Map> list = new ArrayList<Map>();//存传进来的个人信息，用于匹配数据
		Map<String,String> map = new HashMap<String,String> ();
		map.put("BH", "1");
		map.put("QLRMC", xm);
		map.put("CDR", cdr);
		map.put("ZJH", zjh);
		map.put("JBR", jbr);
		list.add(map);

		Message msgHouseData = this.getHouseData("0", "0", "1", "0",list);
		if(msgHouseData != null){
			List<Map> listHouse = (List<Map>)msgHouseData.getRows();
			List<String> listLszl = new ArrayList<String>();//存历史坐落
			List<Map> listmap = new ArrayList<Map>();//存没有查到现状单元的数据
			List<Map> listDY = new ArrayList<Map>();//存现状单元数据
			for(Map maps : listHouse){//筛选没有查到房产的map，并记录历史坐落
				String zl = "";
				if(maps.containsKey("ZL")){
					zl = StringHelper.formatObject(maps.get("ZL"));
				}
				if(zl.length()<=0 || "无".equals(zl)){
					if(maps.containsKey("LSZL")){
						String lszl = StringHelper.formatObject(maps.get("LSZL"));
						if(lszl.length()>0){
							listLszl.add(lszl);
						}						
					}
					listmap.add(maps);
				}else{
					listDY.add(maps);
				}
			}
			if(listDY.size()>0){//拿查到的房产去查三无，并把历史坐落合并到查到的三无数据中。
				Message msgFsqlData = this.getFSQLData("1", "0", "1", listDY);
				List<Map> listFsql = (List<Map>)msgFsqlData.getRows();
				List<String> reslut = new ArrayList<String>();
				List<Map> newFsql = new ArrayList<Map>();
				StringBuilder derLszl = new StringBuilder();
				for(Map mp :  listFsql){
					if(mp.containsKey("LSZL")){
						String lszl = StringHelper.formatObject(mp.get("LSZL"));
						if(lszl.length()>0){
							listLszl.add(lszl);
						}	
					}
				}
				for(String s: listLszl){//过滤重复的历史坐落
	              if(Collections.frequency(reslut, s) < 1) 
	   	 reslut.add(s);
				}
				for(int i=0 ; i<reslut.size(); i++){//把历史坐落数据拼接在一起
					if(i==15){//只显示15个坐落。
						derLszl.append(reslut.get(i));
						break;
					}						
					if(i==0 && i == reslut.size()-1)
						derLszl.append(reslut.get(i));
					if(i==0 &&  i != reslut.size()-1)
						derLszl.append(reslut.get(i) + "，");
					if( i!=0 && i != reslut.size()-1)
						derLszl.append(reslut.get(i) + "，");
					if( i!=0 && i == reslut.size()-1)
						derLszl.append(reslut.get(i));
				}
				String lszl = StringHelper.formatObject(derLszl);				
				for(Map mp :  listFsql){//把历史坐落合并到三无数据里
					if(!"".equals(lszl))
						mp.put("LSZL", lszl);
					if(reslut.size()>15)
						mp.put("LSZLSL", reslut.size());//历史坐落数量
					newFsql.add(mp);
				}
	
				message = this.getSwplPrntZM(newFsql, jbr);
				
			}else{
				message = this.getSwplPrntZM(listmap, jbr);
			}
					
		}else{
			message.setMsg("找不到单元，查询失败");
			return message;
		}
		
		return message;
	}
	
	/**
	 * 土地信息查询
	 */
	
	@SuppressWarnings({ "rawtypes" })
	@Override
	public Message queryLand(Map<String, String> queryvalues, int page, int rows) {
		Message msg = new Message();
		String fsqlcfwh = queryvalues.get("FSQL.CFWH");
		String cxzt = queryvalues.get("CXZT");
		queryvalues.remove("CXZT");
		String tdzt = queryvalues.get("TDZT");
		queryvalues.remove("TDZT");
		long count = 0;
		List<Map> listresult = null;
		/* ===============1、先获取实体对应的表名=================== */
//		String unitentityName = "BDCK.BDCS_SHYQZD_XZ";

		/* ===============2、再获取表名+'_'+字段名=================== */
//		String dyfieldsname = "DY.DJH,DJDY.DJDYID,DY.BDCDYID,DY.BDCDYH,DY.ZL,DY.ZDDM,DY.ZDMJ,'02' AS BDCDYLX ,'使用权宗地' AS BDCDYLXMC,DY.BZ,DY.QLXZ,TDYT.TDYT  ";
		// 集合使用权宗地与所有权宗地
		String unitentityName = "(SELECT DJH,ZL,ZDDM,ZDMJ,BDCDYH,BDCDYID,'01' AS BDCDYLX ,'所有权宗地' AS BDCDYLXMC,BZ,QLXZ  FROM BDCK.BDCS_SYQZD_XZ UNION ALL SELECT DJH,ZL,ZDDM,ZDMJ,BDCDYH,BDCDYID,'02' AS BDCDYLX ,'使用权宗地' AS BDCDYLXMC,BZ,QLXZ  FROM BDCK.BDCS_SHYQZD_XZ)";
		String dyfieldsname = "DJDY.DJDYID,DY.BDCDYID,DY.BDCDYH,DY.DJH,DY.ZL,DY.ZDDM,DY.ZDMJ,DY.BDCDYLX AS BDCDYLX,DY.BDCDYLXMC AS BDCDYLXMC,DY.BZ,DY.QLXZ,TDYT.TDYT ";
		
		String qlfieldsname = "QL.QLID,QL.BDCQZH,QL.DJSJ,QL.QLLX,QL.DJLX";
		String fsqlfieldsname = "FSQL.FSQLID,FSQL.CFWH";

		if (tdzt != null && tdzt.equals("所有权宗地")) {
			unitentityName = "BDCK.BDCS_SYQZD_XZ";
			dyfieldsname = "DY.DJH,DJDY.DJDYID,DY.BDCDYID,DY.BDCDYH,DY.ZL,DY.ZDDM,DY.ZDMJ,'01' AS BDCDYLX,'所有权宗地' AS BDCDYLXMC,DY.BZ,DY.QLXZ,TDYT.TDYT  ";
		}
		if (tdzt != null && tdzt.equals("使用权宗地")) {
			unitentityName = "BDCK.BDCS_SHYQZD_XZ";
			dyfieldsname = "DY.DJH,DJDY.DJDYID,DY.BDCDYID,DY.BDCDYH,DY.ZL,DY.ZDDM,DY.ZDMJ,'02' AS BDCDYLX ,'使用权宗地' AS BDCDYLXMC,DY.BZ,DY.QLXZ,TDYT.TDYT  ";
		}

		/* ===============3、构造查询语句=================== */
		/* SELECT 字段部分 */
		StringBuilder builder2 = new StringBuilder();
		builder2.append("select ").append(dyfieldsname).append(",")
				.append(qlfieldsname);
		if(!StringHelper.isEmpty(fsqlcfwh)){
			builder2.append(",").append(fsqlfieldsname);
		}
		String selectstr = builder2.toString();

		/* FROM 后边的表语句 */
		StringBuilder builder = new StringBuilder();
		builder.append(" from {0} DY")
				.append(" left join BDCK.BDCS_DJDY_XZ DJDY on dy.bdcdyid=djdy.bdcdyid")
				.append(" left join BDCK.bdcs_ql_xz ql on ql.djdyid=djdy.djdyid  ")
				.append(" left join bdck.bdcs_tdyt_xz tdyt on dy.bdcdyid=tdyt.bdcdyid ");
		if(!StringHelper.isEmpty(fsqlcfwh)){
			builder.append(" left join BDCK.bdcs_fsql_xz fsql on ql.fsqlid=fsql.fsqlid  ");
		}
		String fromstr = builder.toString();
		fromstr = MessageFormat.format(fromstr, unitentityName);

		/* WHERE 条件语句 */
		StringBuilder builder3 = new StringBuilder();
		builder3.append(" where  ");
		StringBuilder qlrbuilder = new StringBuilder();
		boolean havecondition = false;
		boolean haveqlr = false;
		for (Entry<String, String> ent : queryvalues.entrySet()) {
			String name = ent.getKey();
			String value = ent.getValue();
			if (!StringHelper.isEmpty(value)) {
				// 抵押状态和查封状态，为空的时候表示不过滤，查询全部
				if ((name.equals("CFZT") && StringHelper.isEmpty(value))
						|| (name.equals("DYZT") && StringHelper.isEmpty(value))) {
					continue;
				}

				// 权利人，权利人的条件比较特殊，where里边又套了where
				if (name.startsWith("QLR.")) {
					if (haveqlr) {
						qlrbuilder.append(" and ");
					}
					if (name.equals("QLR.ZJH") && value.length() == 18) {
						String oldCard = ConverIdCard.getOldIDCard(value);
						if (oldCard.length() != 15) {
							qlrbuilder.append(" " + name + "='" + value
									+ "' ");
						} else {
							qlrbuilder.append(" (" + name + "='" + value
									+ "' or ");
							qlrbuilder.append(" " + name + "='" + oldCard
									+ "') ");
						}
					} else {
						qlrbuilder.append(" " + name + "='" + value + "' ");
					}
					haveqlr = true;
					continue;
				}

				if (havecondition) {
					builder3.append(" and ");
				}

				// 抵押状态
				if (name.equals("DYZT")) {
					if (value.equals("未抵押")) {
						builder3.append(" djdy.djdyid NOT IN (SELECT DYQ.DJDYID FROM BDCK.BDCS_QL_XZ DYQ WHERE DYQ.DJDYID=DJDYID AND DYQ.QLLX='23') ");
					} else {
						builder3.append(" djdy.djdyid IN (SELECT DYQ.DJDYID FROM BDCK.BDCS_QL_XZ DYQ WHERE DYQ.DJDYID=DJDYID AND DYQ.QLLX='23') ");
					}
					havecondition = true;
					continue;
				}
				// 查封状态
				if (name.equals("CFZT")) {
					if (value.equals("未被查封")) {
						builder3.append("  djdy.DJDYID NOT IN (SELECT CF.DJDYID FROM BDCK.BDCS_QL_XZ CF WHERE CF.DJDYID=DJDYID AND CF.QLLX='99') ");
					} else {
						builder3.append("  djdy.DJDYID IN (SELECT CF.DJDYID FROM BDCK.BDCS_QL_XZ CF WHERE CF.DJDYID=DJDYID AND CF.QLLX='99') ");
					}
					havecondition = true;
					continue;
				}
				
				// 房地状况
				if (name.equals("FDZK")) {
					if(value.equals("房地一体")){
						builder3.append(" EXISTS(SELECT 1 FROM BDCK.BDCS_H_XZ H LEFT JOIN BDCK.BDCS_ZRZ_XZ ZRZ ON H.ZRZBDCDYID=ZRZ.BDCDYID  WHERE H.ZDBDCDYID=DY.BDCDYID) ");
					}else{//默认查询纯土地
						builder3.append(" NOT EXISTS(SELECT 1 FROM BDCK.BDCS_H_XZ H LEFT JOIN BDCK.BDCS_ZRZ_XZ ZRZ ON H.ZRZBDCDYID=ZRZ.BDCDYID  WHERE H.ZDBDCDYID=DY.BDCDYID) ");
					}
					havecondition = true;
					continue;
				}
				
				// 权利性质-----》宗地查档模板未添加此条件，可忽略
				if (name.equals("QLXZ")) {
					if (value.equals("1")) {
						builder3.append(" DY.QLXZ='100' ");
					}else if(value.equals("2")){
						builder3.append(" DY.QLXZ='200' ");
					}else if(value.equals("3")){
						builder3.append(" DY.QLXZ='101' ");
					}else if(value.equals("4")){
						builder3.append(" DY.QLXZ='102' ");
					}else if(value.equals("5")){
						builder3.append(" DY.QLXZ='203' ");
					}else{
						builder3.append(" 1=1 ");
					}
					havecondition = true;
					continue;
				}
				
				//土地用途-----》宗地查档模板未添加此条件，可忽略
				if (name.equals("TDYT")) {
					if (value.equals("1")) {
						builder3.append(" TDYT.TDYT='071' ");
					}else if(value.equals("2")){
						builder3.append(" TDYT.TDYT='072' ");
					}else if(value.equals("3")){
						builder3.append(" TDYT.TDYT='051' ");
					}else if(value.equals("4")){
						builder3.append(" TDYT.TDYT='053' ");
					}else if(value.equals("5")){
						builder3.append(" TDYT.TDYT='052' ");
					}else if(value.equals("6")){
						builder3.append(" TDYT.TDYT='054' ");
					}else if(value.equals("7")){
						builder3.append(" TDYT.TDYT='085' ");
					}else if(value.equals("8")){
						builder3.append(" TDYT.TDYT='084' ");
					}else if(value.equals("9")){
						builder3.append(" TDYT.TDYT='081' ");
					}else if(value.equals("10")){
						builder3.append(" TDYT.TDYT='083' ");
					}else if(value.equals("11")){
						builder3.append(" TDYT.TDYT='061' ");
					}else if(value.equals("12")){
						builder3.append(" TDYT.TDYT='062' ");
					}else if(value.equals("13")){
						builder3.append(" TDYT.TDYT='063' ");
					}else{
						builder3.append(" 1=1 ");
					}
					havecondition = true;
					continue;
				}

				//宗地查档模板未添加时间条件，可忽略
				if (name.equals("DJSJ_Q")) {
					builder3.append("  QL.DJSJ >=to_date('"+queryvalues.get("DJSJ_Q")+"','yyyy-mm-dd') ");
				}else if (name.equals("DJSJ_Z")) {
					builder3.append("   QL.DJSJ <=(to_date('"+queryvalues.get("DJSJ_Z")+"','yyyy-mm-dd')+1) ");
				}else {
					builder3.append(" " + name + "='" + value + "' ");
				}
				havecondition = true;
			}
		}
//		String wherein="";
		// 有权利人查询条件
		if (!StringHelper.isEmpty(qlrbuilder.toString())) {
			if (havecondition) {
				builder3.append(" and ");
			}
//			 wherein=builder3.toString()+" QL.QLID IN (SELECT QLID FROM BDCK.BDCS_QLR_XZ QLR WHERE "+ qlrbuilder.toString() + ")";
//			builder3.append(" QL.QLID IN (SELECT QLID FROM BDCK.BDCS_QLR_XZ QLR WHERE "
//					+ qlrbuilder.toString() + ")");
			builder3.append(" exists(select 1 from BDCK.BDCS_QLR_XZ QLR  where "+ qlrbuilder.toString()+"   AND QLR.QLID=QL.QLID)  ");
			havecondition = true;
		}

		if (tdzt == null || !tdzt.equals("所有权宗地")) {
			if (havecondition) {
				// builder3.append(" and ");
			}
			// builder3.append(" ql.qllx='4'");
		}
		String wherestr = builder3.toString();
		if (wherestr.trim().toUpperCase().endsWith("WHERE")) {
			wherestr = "";
		}
//		String fromsql1="select ql.qlid " + fromstr + wherestr ;
		String fromSql = " from (select ql.qlid " + fromstr + wherestr + ")";
		String fullSql = selectstr + fromstr + wherestr;
		/* 为了提高效率，先不查询符合条件的记录总数，将来改为异步获取 */
		CommonDao dao = baseCommonDao;
		Long test1=System.currentTimeMillis(); 
		if("历史查询".equals(cxzt)){
			fromSql = fromSql.replaceAll("_xz", "_ls").replaceAll("_XZ", "_LS");
			fullSql = fullSql.replaceAll("_xz", "_ls").replaceAll("_XZ", "_LS");
		}
		
		count = dao.getCountByFullSql(fromSql);
		Long test2=System.currentTimeMillis();
		listresult = dao.getPageDataByFullSql(fullSql, page, rows);
		Long test3=System.currentTimeMillis();
		addRightsHolderInfo(listresult);
		Long test4=System.currentTimeMillis();
		addLimitZDStatus(listresult);
		Long test5=System.currentTimeMillis();
		//国有土地使用权时，显示出土地上房屋状态。
		addLimitZDStausByFw(listresult);
		Long test6=System.currentTimeMillis();
		addDyCfDetails(listresult);
		Long test7=System.currentTimeMillis();
		addZRZCount(listresult);
		Long test8=System.currentTimeMillis();
//		List<Map> m=dao.getDataListByFullSql(fromsql1);
//		Long test9 =System.currentTimeMillis();
//		List<Map> d=dao.getDataListByFullSql(wherein);
//		Long test10=System.currentTimeMillis();
		System.out.println("addZRZCount方法消耗时间为："+(test8-test7));
		System.out.println("addDyCfDetails方法消耗时间为："+(test7-test6));
		System.out.println("addLimitZDStausByFw方法消耗时间为："+(test6-test5));
		System.out.println("addLimitZDStatus方法消耗时间为："+(test5-test4));
		System.out.println("addRightsHolderInfo方法消耗时间为："+(test4-test3));
		System.out.println("datalist修改成exist方法消耗时间为："+(test3-test2));
//		System.out.println("datalist中in方法消耗时间为："+(test10-test9));
		System.out.println("Count方法消耗时间为："+(test2-test1));
//		System.out.println("弃用Count方法消耗的时间为："+(test9-test8)+",弃用Count方法总和为"+m.size()+",共Count方法总和为"+count);
		System.out.println("共消耗时间为："+(test8-test1));
		// 格式化结果中的登簿时间
		for (Map map : listresult) {
			if (map.containsKey("DJSJ")) {
				map.put("DJSJ",StringHelper.FormatYmdhmsByDate(map.get("DJSJ")));
			}
			if (map.containsKey("QLXZ")) {
				map.put("QLXZ",ConstHelper.getNameByValue("QLXZ",(String)map.get("QLXZ")));
			}
			if (map.containsKey("TDYT")) {
				map.put("TDYT",ConstHelper.getNameByValue("TDYT",(String)map.get("TDYT")));
			}
		}
		msg.setTotal(count);
		msg.setRows(listresult);
		return msg;
	}
	
	@SuppressWarnings("unchecked")
	protected void addRightsHolderInfo(@SuppressWarnings("rawtypes") List<Map> result) {
		if (result != null && result.size() > 0) {
			for (@SuppressWarnings("rawtypes")
			Map map : result) {
				if (map.containsKey("QLID")) {
					String qlid = (String) map.get("QLID");
					if (qlid != null) {
						RightsHolder holder = RightsHolderTools
								.getUnionRightsHolder(DJDYLY.XZ, qlid);
						if (holder != null) {
							map.put("QLRMC", holder.getQLRMC());
							map.put("DH", holder.getDH());
							map.put("ZJHM", holder.getZJH());
						}
					}
				}
			}
		}
	}
	
	/**
	 * 土地的状态赋值
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected void addLimitZDStatus(List<Map> result) {
		if (result != null && result.size() > 0) {
			for (Map map : result) {
				if (map.containsKey("DJDYID")) {
					String djdyid = (String) map.get("DJDYID");
					if (djdyid != null) {
						String sqlMortgage = MessageFormat
								.format(" from BDCK.BDCS_QL_XZ where djdyid=''{0}'' and qllx=''23''",
										djdyid);
						String sqlSeal = MessageFormat
								.format(" from BDCK.BDCS_QL_XZ where djdyid=''{0}'' and djlx=''800'' and qllx=''99''",
										djdyid);
						String sqlObjection = MessageFormat
								.format("  from BDCK.BDCS_QL_XZ where djdyid=''{0}'' and djlx=''600'' ",
										djdyid);
						long mortgageCount = baseCommonDao
								.getCountByFullSql(sqlMortgage);
						long SealCount = baseCommonDao
								.getCountByFullSql(sqlSeal);
						long ObjectionCount = baseCommonDao
								.getCountByFullSql(sqlObjection);

						String mortgageStatus = mortgageCount > 0 ? "已抵押"
								: "无抵押";
						String sealStatus = SealCount > 0 ? "已查封" : "无查封";

						// 判断完现状层中的查封信息，接着判断办理中的查封信息
						if (!(SealCount > 0)) {
							String sqlSealing = " FROM bdck.bdcs_xmxx a LEFT JOIN bdck.bdcs_djdy_gz b ON a.xmbh=b.xmbh AND a.sfdb='0' LEFT JOIN bdck.bdcs_ql_gz c ON b.xmbh=c.xmbh and  b.djdyid=c.djdyid WHERE c.djlx='800' AND c.qllx='99' and c.djdyid='"
									+ djdyid + "' and a.sfdb='0' ";
							long count = baseCommonDao
									.getCountByFullSql(sqlSealing);
							sealStatus = count > 0 ? "查封办理中" : "无查封";
						}

						// 判断完现状层中的查封信息，接着判断办理中的查封信息
						if (!(mortgageCount > 0)) {
							String sqlmortgageing = " FROM bdck.bdcs_xmxx a LEFT JOIN bdck.bdcs_djdy_gz b ON a.xmbh=b.xmbh AND a.sfdb='0' LEFT JOIN bdck.bdcs_ql_gz c ON b.xmbh=c.xmbh and b.djdyid=c.djdyid WHERE c.djlx='100' AND c.qllx='23' and c.djdyid='"
									+ djdyid + "' and a.sfdb='0' ";
							long count = baseCommonDao
									.getCountByFullSql(sqlmortgageing);
							mortgageStatus = count > 0 ? "抵押办理中" : "无抵押";
						}

						String objectionStatus = ObjectionCount > 0 ? "有异议"
								: "无异议";
						map.put("DYZT", mortgageStatus);
						map.put("CFZT", sealStatus);
						map.put("YYZT", objectionStatus);
						
						//增加限制状态
						String sqlLimit = MessageFormat.format("  from BDCK.BDCS_DYXZ where bdcdyid=''{0}'' and yxbz=''1'' ",
										map.get("BDCDYID"));

						long LimitCount = baseCommonDao.getCountByFullSql(sqlLimit);
						String LimitStatus = LimitCount > 0 ? map.get("BDCDYLXMC") + "已限制" : map.get("BDCDYLXMC") + "无限制";
						// 改为判断完查封 人后判断限制
						if (!(LimitCount > 0)) {
							String sqlLimiting = " FROM bdck.bdcs_xmxx a LEFT JOIN bdck.bdcs_djdy_gz b ON a.xmbh=b.xmbh AND a.sfdb='0' LEFT JOIN bdck.bdcs_dyxz c ON b.xmbh=c.xmbh and b.bdcdyid=c.bdcdyid WHERE c.yxbz='0' and c.bdcdyid='"
									+ map.get("BDCDYID") + "' and a.sfdb='0' ";
							long countxz = baseCommonDao.getCountByFullSql(sqlLimiting);
							LimitStatus = countxz > 0 ? "限制办理中" : "无限制";
						}
						map.put("XZZT", LimitStatus);
					}
				}
			}
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected void addLimitZDStausByFw(List<Map> result){

		if(result != null && result.size() > 0){
			for(Map map : result){
				long mortgageCount = 0,mortgagingCount=0;
				List<String> lstmortgage=new ArrayList<String>();
				List<String> lstmortgageing=new ArrayList<String>();
				long SealCount = 0,SealingCount=0;
				List<String> lstseal=new ArrayList<String>();
				List<String> lstsealing=new ArrayList<String>();
				long ObjectionCount = 0,ObjectioningCount=0;
				List<String> lstObjection=new ArrayList<String>();
				List<String> lstObjectioning=new ArrayList<String>();
				long LimitCount = 0,LimitingCount=0;
				long housecount = 0;
				//只有使用权宗地才有房屋
				if(map.containsKey("BDCDYID")&&map.get("BDCDYLX").equals("02")){
					String zdbdcdyid = (String) map.get("BDCDYID");
					if(zdbdcdyid != null ){
						//已办理的业务
						StringBuilder strdealed=new StringBuilder();
						strdealed.append("SELECT QL.QLLX,QL.DJLX,DY.BDCDYID FROM  ( ");
						strdealed.append("select BDCDYID,'031' BDCDYLX,ZDBDCDYID from BDCK.BDCS_H_XZ union  all  ");
						strdealed.append("select BDCDYID,'032' BDCDYLX,ZDBDCDYID from BDCK.BDCS_H_XZY ) DY   ");
						strdealed.append("LEFT JOIN BDCK.BDCS_DJDY_XZ DJDY ON DJDY.BDCDYID=DY.BDCDYID AND DJDY.BDCDYLX=DY.BDCDYLX ");
						strdealed.append("LEFT JOIN BDCK.BDCS_QL_XZ QL ON QL.DJDYID=DJDY.DJDYID ");
						strdealed.append("WHERE QL.QLID IS NOT NULL AND DY.ZDBDCDYID='");
						strdealed.append(zdbdcdyid).append("'");
						String dealedSql=strdealed.toString();
						 List<Map> dealedmap=baseCommonDao.getDataListByFullSql(dealedSql);
						 if(!StringHelper.isEmpty(dealedmap) && dealedmap.size()>0){
							 for(Map m :dealedmap){
								String qllx= StringHelper.FormatByDatatype(m.get("QLLX"));
								String djlx=StringHelper.FormatByDatatype(m.get("DJLX"));
								String bdcdyid= StringHelper.FormatByDatatype(m.get("BDCDYID"));
								if(DJLX.CFDJ.Value.equals(djlx) && QLLX.QTQL.Value.equals(qllx)){
									if(!lstseal.contains(bdcdyid)){
										lstseal.add(bdcdyid);
										SealCount++;
									}
								}else if(DJLX.YYDJ.Value.equals(djlx)){
									if(!lstObjection.contains(bdcdyid)){
										lstObjection.add(bdcdyid);
										ObjectionCount++;
									}
									
								}else if(QLLX.DIYQ.Value.equals(qllx)){
									if(!lstmortgage.contains(bdcdyid)){
										lstmortgage.add(bdcdyid);
										mortgageCount++;
									}
									
								}
							 }
						 }
						//正在办理的业务
						StringBuilder strdealing=new StringBuilder();
						strdealing.append("SELECT QL.QLLX,QL.DJLX,DY.BDCDYID,XMXX.QLLX XMQLLX,XMXX.DJLX XMDJLX  FROM  ( ");
						strdealing.append("select BDCDYID,'031' BDCDYLX,ZDBDCDYID from BDCK.BDCS_H_XZ union  all  ");
						strdealing.append("select BDCDYID,'032' BDCDYLX,ZDBDCDYID from BDCK.BDCS_H_XZY ) DY   ");
						strdealing.append("LEFT JOIN BDCK.BDCS_DJDY_GZ DJDY ON DJDY.BDCDYID=DY.BDCDYID AND DJDY.BDCDYLX=DY.BDCDYLX  ");
						strdealing.append("LEFT JOIN BDCK.BDCS_QL_GZ QL ON QL.DJDYID=DJDY.DJDYID ");
						strdealing.append("LEFT JOIN BDCK.BDCS_XMXX XMXX ON XMXX.XMBH=QL.XMBH  ");
						strdealing.append("WHERE (XMXX.SFDB IS NULL OR XMXX.SFDB<>'1') AND QL.QLID IS NOT NULL  ");
						strdealing.append("AND DY.ZDBDCDYID= '");
						strdealing.append(zdbdcdyid).append("'");
						String dealingsql=strdealing.toString();
						List<Map> dealingmap=baseCommonDao.getDataListByFullSql(dealingsql);
						 if(!StringHelper.isEmpty(dealingmap) && dealingmap.size()>0){
							 for(Map m :dealingmap){
								String qllx= StringHelper.FormatByDatatype(m.get("XMQLLX"));
								String djlx=StringHelper.FormatByDatatype(m.get("XMDJLX"));
								String bdcdyid= StringHelper.FormatByDatatype(m.get("BDCDYID"));
								if(DJLX.CFDJ.Value.equals(djlx) && QLLX.QTQL.Value.equals(qllx)){
									if(!lstseal.contains(bdcdyid) && !lstsealing.contains(bdcdyid)){
										SealingCount++;
										lstsealing.add(bdcdyid);
									}
								}else if(DJLX.YYDJ.Value.equals(djlx)){
									if(!lstObjection.contains(bdcdyid) && !lstObjectioning.contains(bdcdyid)){
										ObjectioningCount++;
										lstObjectioning.add(bdcdyid);
									}
								}else if(QLLX.DIYQ.Value.equals(qllx) && !DJLX.ZXDJ.Value.equals(djlx)){
									if(!lstmortgage.contains(bdcdyid) && !lstmortgageing.contains(bdcdyid)){
										mortgagingCount++;
										lstmortgageing.add(bdcdyid);
									}
								}
							 }
						 }
						//限制的业务
						StringBuilder strlimit=new StringBuilder();
						strlimit.append("SELECT DYXZ.YXBZ FROM (SELECT BDCDYID,ZDBDCDYID FROM BDCK.BDCS_H_XZ UNION ALL  ");
						strlimit.append("SELECT BDCDYID,ZDBDCDYID FROM BDCK.BDCS_H_XZY ) DY ");
						strlimit.append("LEFT JOIN BDCK.BDCS_DYXZ DYXZ ON DYXZ.BDCDYID=DY.BDCDYID ");
						strlimit.append("WHERE DYXZ.ID IS NOT NULL  ");
						strlimit.append(" AND DY.ZDBDCDYID='").append(zdbdcdyid).append("'");
						String limitsql=strlimit.toString();
						List<Map> limitmap = baseCommonDao.getDataListByFullSql(limitsql);
						//商品房的土地抵消状态赋值
						if(limitmap != null && limitmap.size() > 0){
							for(Map m :limitmap){
								String yxbz= StringHelper.FormatByDatatype(m.get("YXBZ"));
								if("1".equals(yxbz)){
									LimitCount++;
								}else{
									LimitingCount++;
								}
							}
							
						}
						String mortgageStatus =  MessageFormat.format("土地{0};地上房屋已抵押{1}起,正在抵押{2}起",map.get("DYZT"),mortgageCount,mortgagingCount);
						String sealStatus = MessageFormat.format("土地{0};地上房屋已查封{1}起,正在查封{2}起",map.get("CFZT"),SealCount,SealingCount);
						String objectionStatus = MessageFormat.format("土地{0};地上房屋有异议{1}起",map.get("YYZT"),ObjectionCount);
						String LimitStatus = MessageFormat.format("土地{0};地上房屋有限制{1}起,正在限制{2}起",map.get("XZZT"),LimitCount,LimitingCount);
						map.put("DYZT", mortgageStatus);
						map.put("CFZT", sealStatus);
						map.put("YYZT", objectionStatus);
						map.put("XZZT", LimitStatus);
//						System.out.println(mortgageStatus);
//						System.out.println(sealStatus);
//						System.out.println(objectionStatus);
//						System.out.println(LimitStatus);
					}
				}
				map.put("HOUSECOUNT",housecount);
			}
		}		
	}
	
	//登记查封状态的详情信息
    @SuppressWarnings({ "rawtypes", "unchecked" })
	protected void addDyCfDetails(List<Map> result){
    	if (result != null && result.size() > 0) {
			for (Map map : result) {
				if(!StringUtils.isEmpty(map.get("BDCDYH"))&&!StringUtils.isEmpty(map.get("DJDYID")))
				{
					String dycfdetailssql = MessageFormat.format("select fsql.cfjg,fsql.cfwh,fsql.dyr,ql.qlqssj,ql.qljssj,ql.qllx,ql.djlx,ql.qlid "
							+ ",(CASE WHEN FSQL.DYFS='1' THEN TO_NUMBER(FSQL.BDBZZQSE)  WHEN FSQL.DYFS='2' THEN TO_NUMBER(FSQL.ZGZQSE) ELSE 0 END) AS DYJE       "
							+ " from bdck.bdcs_ql_xz ql left join bdck.bdcs_fsql_xz fsql on ql.qlid=fsql.qlid where ql.bdcdyh=''{0}'' and ql.djdyid=''{1}'' and ql.qllx in(''99'',''23'') ",
							map.get("BDCDYH"),map.get("DJDYID"));
					
					List<Map> dycfs=baseCommonDao.getDataListByFullSql(dycfdetailssql);
					String dyqr ="",dyje = "";
					for (Map dycf : dycfs) {
						if("800".equals(dycf.get("DJLX"))&&"99".equals(dycf.get("QLLX"))){
							map.put("CFJG", dycf.get("CFJG"));
							map.put("CFWH", dycf.get("CFWH"));
							map.put("CFQX", DateUtil.FormatByDatetime(dycf.get("QLQSSJ"))+" 至  "+DateUtil.FormatByDatetime(dycf.get("QLJSSJ")));
						}
						if("23".equals(dycf.get("QLLX"))){
							map.put("DYR", dycf.get("DYR"));
							map.put("DYQX", DateUtil.FormatByDatetime(dycf.get("QLQSSJ"))+" 至  "+DateUtil.FormatByDatetime(dycf.get("QLJSSJ")));
							String dyjeex=StringHelper.formatObject(dycf.get("DYJE"));
							dyje = StringHelper.isEmpty(dyje)?dyjeex:dyje + "/" + dyjeex;
							String qlid =  StringHelper.formatObject(dycf.get("QLID"));
							if (qlid != null) {
								RightsHolder holder = RightsHolderTools
										.getUnionRightsHolder(DJDYLY.XZ, qlid);
								dyqr = StringHelper.isEmpty(dyqr)?holder.getQLRMC():dyqr+"/" + holder.getQLRMC();
							}
						}
					}
					map.put("DYJE",dyje);
					map.put("DYQR",dyqr);
				}
				
			}
    	}
    }
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected void addZRZCount(List<Map> result) {
		String zrzcount = "0";
		if (result != null && result.size() > 0) {
			for (Map map : result) {
				if (map.containsKey("BDCDYID")) {
					String zdbdcdyid = (String) map.get("BDCDYID");
					if (zdbdcdyid != null) {
						String countsql = MessageFormat.format(
								" SELECT SUM(C) AS C FROM (SELECT COUNT(0) AS C FROM BDCK.BDCS_ZRZ_XZ WHERE ZDBDCDYID=''{0}'' UNION ALL SELECT COUNT(0) AS C FROM BDCK.BDCS_ZRZ_XZY WHERE ZDBDCDYID=''{0}'')",
								zdbdcdyid);
						List<Map> counts = baseCommonDao.getDataListByFullSql(countsql);
						zrzcount = String.valueOf(counts.get(0).get("C"));
					}
				}
				map.put("ZRZCOUNT", zrzcount);
			}
		}
	}
	
	protected String getGYRMC(String qlid, String qlrid) {
		if (!StringHelper.isEmpty(qlid) && !StringHelper.isEmpty(qlrid)) {
			//除当前权利人外其他共有权利人
			String gyrsql = "SELECT QLRMC FROM BDCK.BDCS_QLR_XZ WHERE QLID='"+qlid+"' AND QLRID <> '"+qlrid+"'";
			List<Map> gyr =  baseCommonDao.getDataListByFullSql(gyrsql);
			String gyrmc = "";
			if (gyr != null && gyr.size() > 0) {
				for (int k=0; k < gyr.size() ; k++) {
					if (k > 2) {
						gyrmc += "等" + gyr.size() + "人";
						break;
					}
					if (StringHelper.isEmpty(gyrmc)) {
						gyrmc = StringHelper.formatObject(gyr.get(k).get("QLRMC"));
					} else {
						gyrmc += "、" + StringHelper.formatObject(gyr.get(k).get("QLRMC"));
					}
				}
			}
			return gyrmc;
		} else {
			return "";
		}
	}
}
