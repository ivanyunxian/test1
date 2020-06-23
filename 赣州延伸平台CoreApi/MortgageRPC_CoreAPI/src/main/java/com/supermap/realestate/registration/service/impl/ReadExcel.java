package com.supermap.realestate.registration.service.impl;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.supermap.realestate.registration.tools.ExcelCommon;
import com.supermap.realestate.registration.tools.FileOperateUtil;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
 
@Component("readExcel")
public class ReadExcel {
	@Autowired
	private CommonDao commonDao;
	public <T> List readExcel(String path, String type) throws IOException,
			ParseException {
		if (path == null || ExcelCommon.EMPTY.equals(path)) {
			return null;
		} else {
			String postfix = FileOperateUtil.getFileSufix(path);
			if (!ExcelCommon.EMPTY.equals(postfix)) {
				if (ExcelCommon.OFFICE_EXCEL_2003_POSTFIX.equals(postfix)) {
					return readXls(path, type);
				} else if (ExcelCommon.OFFICE_EXCEL_2010_POSTFIX
						.equals(postfix)) {
					return readXlsx(path, type);
				}
			} else {
				System.out.println(path + ExcelCommon.NOT_EXCEL_FILE);
			}
		}
		return null;
	}

	/**
	 * Read the Excel 2010
	 * 
	 * @param path
	 *            the path of the excel file
	 * @return
	 * @throws IOException
	 * @throws ParseException
	 */
	public <T> List readXlsx(String path, String type) throws IOException,
			ParseException {
		System.out.println(ExcelCommon.PROCESSING + path);
		InputStream is = new FileInputStream(path);
		XSSFWorkbook xssfWorkbook = new XSSFWorkbook(is);
		Map<String, List<Map<String, String>>> mapone = new HashMap<String, List<Map<String, String>>>();
		//获取所有sheet集合
		List listallsheet = new ArrayList();
		// 1.读取Excel的sheet
		for (int numSheet = 0; numSheet < xssfWorkbook.getNumberOfSheets(); numSheet++) {
			XSSFSheet hssfSheet = xssfWorkbook.getSheetAt(numSheet);
			if (hssfSheet == null) {
				continue;
			}
			//2.读取每个sheet中的值
			for (int rowNum = 0; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
				XSSFRow hssfRow = hssfSheet.getRow(rowNum);
				if (hssfRow != null) {
					mapone =  SetAjjbxx(hssfSheet, rowNum);
					listallsheet.add(mapone);
				}
			}
		}
		//3.对sheetlist集合进行关系构建（还没做）
		
		return listallsheet;
	}

	/**
	 * Read the Excel 2003-2007
	 * 
	 * @param path
	 *            the path of the Excel
	 * @return
	 * @throws IOException
	 * @throws ParseException
	 */
	@SuppressWarnings("unchecked")
	public <T> List readXls(String path, String type) throws IOException,
			ParseException {
		System.out.println(ExcelCommon.PROCESSING + path);
		InputStream is = new FileInputStream(path);
		HSSFWorkbook hssfWorkbook = new HSSFWorkbook(is);
		// T student = null;
		Map<String, List<Map<String, String>>> mapone = new HashMap<String, List<Map<String, String>>>();
		List listallsheet = new ArrayList();
		// 读取Excel的sheet
		for (int numSheet = 0; numSheet < hssfWorkbook.getNumberOfSheets(); numSheet++) {
			HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(numSheet);
			if (hssfSheet == null) {
				continue;
			}
			//读取每个sheet中的值
			for (int rowNum = 0; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
				HSSFRow hssfRow = hssfSheet.getRow(rowNum);
				if (hssfRow != null) {
					mapone =  SetAjjbxx(hssfSheet, rowNum);
					listallsheet.add(mapone);
				}
			}
		}
		return listallsheet;
	}

	 
	private Map<String, List<Map<String, String>>> SetAjjbxx(XSSFSheet xssfSheet,int numSheet)
			throws ParseException {
		List<Map<String,String>> listall=new ArrayList<Map<String,String>>();
		Map<String, List<Map<String,String>>> mapSheet=new HashMap<String, List<Map<String,String>>>();
		Map<Integer, String>  keymap =new HashMap<Integer, String>();;
		//1.首先读取字段名称的map集合（读取excel的第一行数据）
		XSSFRow xssfRowone = xssfSheet.getRow(0);
		if (xssfRowone != null) {
			for(int i=0;i<=xssfRowone.getLastCellNum();i++){
				XSSFCell key = xssfRowone.getCell((short) i);
			    
				keymap.put(i, getValue(key));
			}
		}
		//2.循环遍历除了第一行外的其他行，并且同时对每一行数据进行赋值
		for (int rowNum = 1; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
			XSSFRow xssfRow = xssfSheet.getRow(rowNum);
			Map<String, String> map =new HashMap<String, String>();
			for (Entry<Integer, String> ent : keymap.entrySet()) {
				
	            Integer name = ent.getKey();
	            String value = ent.getValue();
	            map.put(value,  getValue(xssfRow.getCell(Short.parseShort(name.toString()))));
	        }
			listall.add(map);
			 
			 
		}
		
		if(numSheet==0){
			mapSheet.put("InfoH", listall);
		}else if(numSheet==1){
			mapSheet.put("InfoFDCQ", listall);
		}else if(numSheet==2){
			mapSheet.put("InfoQLR", listall);
		}
		 
		return mapSheet;
	
	}
 
	private Map<String, List<Map<String, String>>> SetAjjbxx(HSSFSheet xssfSheet,int numSheet)
			throws ParseException {
		List<Map<String,String>> listall=new ArrayList<Map<String,String>>();
		Map<String, List<Map<String,String>>> mapSheet=new HashMap<String, List<Map<String,String>>>();
		Map<Integer, String>  keymap =new HashMap<Integer, String>();;
		//1.首先读取字段名称的map集合（读取excel的第一行数据）
		HSSFRow xssfRowone = xssfSheet.getRow(0);
		if (xssfRowone != null) {
			for(int i=0;i<=xssfRowone.getLastCellNum();i++){
				HSSFCell key = xssfRowone.getCell((short) i);
			    
				keymap.put(i, getValue(key));
			}
		}
		//2.循环遍历除了第一行外的其他行，并且同时对每一行数据进行赋值
		for (int rowNum = 1; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
			HSSFRow xssfRow = xssfSheet.getRow(rowNum);
			Map<String, String> map =new HashMap<String, String>();
			for (Entry<Integer, String> ent : keymap.entrySet()) {
				
	            Integer name = ent.getKey();
	            String value = ent.getValue();
	            map.put(value,  getValue(xssfRow.getCell(Short.parseShort(name.toString()))));
	        }
			listall.add(map);
			 
			 
		}
		
		if(numSheet==0){
			mapSheet.put("InfoH", listall);
		}else if(numSheet==1){
			mapSheet.put("InfoFDCQ", listall);
		}else if(numSheet==2){
			mapSheet.put("InfoQLR", listall);
		}
		 
		return mapSheet;
	
	}
 
	 
	@SuppressWarnings("static-access")
	private String getValue(XSSFCell xssfRow) {
		if (xssfRow != null) {
			if (xssfRow.getCellType() == xssfRow.CELL_TYPE_BOOLEAN) {
				return String.valueOf(xssfRow.getBooleanCellValue());
			} else if (xssfRow.getCellType() == xssfRow.CELL_TYPE_NUMERIC) {
		         DecimalFormat df = new DecimalFormat("#");//转换成整型
				 df.setRoundingMode(RoundingMode.HALF_UP);
		         return	String.valueOf(df.format(xssfRow.getNumericCellValue()));
			} else {
				return String.valueOf(xssfRow.getStringCellValue()).trim();
			}
		} else {
			return "";
		}
	}

	@SuppressWarnings("static-access")
	private String getValue(HSSFCell hssfCell) {
		if (hssfCell != null) {
			if (hssfCell.getCellType() == hssfCell.CELL_TYPE_BOOLEAN) {
				return String.valueOf(hssfCell.getBooleanCellValue());
			} else if (hssfCell.getCellType() == hssfCell.CELL_TYPE_NUMERIC) {
		         DecimalFormat df = new DecimalFormat("#");//转换成整型
				df.setRoundingMode(RoundingMode.HALF_UP);
		         return	String.valueOf(df.format(hssfCell.getNumericCellValue()));
			} else {
				return String.valueOf(hssfCell.getStringCellValue()).trim();
			}
		} else {
			return "";
		}
	}
}
