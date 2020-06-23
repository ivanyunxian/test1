/**
 * 收费统计
 */
package com.supermap.realestate.registration.web;

import com.supermap.realestate.registration.service.SFMXTJService;
import com.supermap.realestate.registration.util.RequestHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.wisdombusiness.web.Message;
import net.sf.json.JSONArray;
import org.apache.poi.hssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhaomengfan
 * @Description: 收费统计
 */
@Controller
@RequestMapping("/SFMXTJ")
public class SFMXTJController {

	@Autowired
	private SFMXTJService sfmxtjservice;
	
	/**
	 * @Description: 页面跳转
	 * @Title: ShowSFTJIndex
	 * @Author: zhaomengfan
	 * @Date: 2017年6月27日下午12:51:03
	 * @return String
	 */
	@RequestMapping(value = "/Index", method = RequestMethod.GET)
	public String ShowSFTJIndex() {
		return "/realestate/registration/tj/SFMXTJIndex";
	}

	/**
	 * @Author taochunda
	 * @Description 在线缴费模板页面
	 * @Date 2019-07-16 16:34
	 * @Param
	 * @return
	 **/
	@RequestMapping(value = "/zxjftj", method = RequestMethod.GET)
	public String ShowOnlinePayindex() {
		return "/realestate/registration/tj/zxjftj";
	}

	/**
	 * @Description: 获取查询界面的下拉框内容
	 * @Title: GetCombobox
	 * @Author: zhaomengfan
	 * @Date: 2017年6月27日下午5:32:58
	 * @param request
	 * @param response
	 * @return
	 * @return List<Message>
	 */
	@RequestMapping(value = "/GetCombobox", method = RequestMethod.GET)
	public @ResponseBody List<Message> GetCombobox(HttpServletRequest request, HttpServletResponse response) {
		return sfmxtjservice.GetCombobox();
	}

	/**
	 * @Description: 收费统计数据获取
	 * @Title: GetSFTJ
	 * @Author: zhaomengfan
	 * @Date: 2017年6月27日下午12:48:12
	 * @param request
	 * @param response
	 * @return Message
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping(value = "/GetSFTJ", method = RequestMethod.GET)
	public @ResponseBody Message GetSFTJ(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		Map<String,String> Param = new HashMap<String, String>();
		Param.put("Department", RequestHelper.getParam(request, "Department"));
		Param.put("SFLX", RequestHelper.getParam(request, "SFLX"));
		Param.put("SFSJ_Q", RequestHelper.getParam(request, "SFSJ_Q")+" 00:00:00");
		Param.put("SFSJ_Z", RequestHelper.getParam(request, "SFSJ_Z")+" 23:59:59");
		Param.put("User", RequestHelper.getParam(request, "User"));
		Param.put("YWLSH", RequestHelper.getParam(request, "YWLSH"));
		Param.put("SLRY", RequestHelper.getParam(request, "SLRY"));
		return sfmxtjservice.GetSFTJ(Param);
	}

	/**
	 * 在线缴费统计（URL:"/zxjftj",Method:GET）
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/zxjftj/", method = RequestMethod.POST)
	public @ResponseBody Message getZXJFTJ(HttpServletRequest request) throws Exception {
		Message msg = sfmxtjservice.getZXJFTJ(request);
		return msg;
	}

	/**
	 * @Author taochunda
	 * @Description 修改票据
	 * @Date 2019-07-23 15:01
	 * @Param [request]
	 * @return com.supermap.wisdombusiness.web.Message
	 **/
	@RequestMapping(value = "/modifypj", method = RequestMethod.POST)
	public @ResponseBody Message ModifyPJZT(HttpServletRequest request) throws Exception {
		Message msg = sfmxtjservice.ModifyPJZT(request);
		return msg;
	}

	/**
	 * @Author taochunda
	 * @Description 导出在线缴费统计
	 * @Date 2019-07-15 9:50
	 * @Param [request, response]
	 * @return java.lang.String
	 **/
	@SuppressWarnings({ "deprecation", "rawtypes", "unchecked" })
	@RequestMapping(value = "/ZXJFTJDownload", method = RequestMethod.GET)
	public @ResponseBody String ZXJFTJDownload(HttpServletRequest request) {
		try {
			request.setCharacterEncoding("utf-8");
			Message msg = sfmxtjservice.getZXJFTJ(request);
			List<Map> rows = (List<Map>) msg.getRows();
			String qlrmc = "";
			String ywrmc = "";
			String sqrs = "";
			for (Map map : rows) {
				qlrmc = StringHelper.formatObject(map.get("QLRMC"));
				ywrmc = StringHelper.formatObject(map.get("YWRMC"));
				if (!StringHelper.isEmpty(qlrmc)) {
					sqrs = "权利人：" + qlrmc;
					if (!StringHelper.isEmpty(ywrmc)) {
						sqrs += ";\n义务人：" + ywrmc;
					}
				} else {
					sqrs = "义务人：" + ywrmc;
				}
				map.put("SQR", sqrs);
			}

			//导出
			String basePath = request.getRealPath("/") + "\\resources\\PDF";
			String outpath = "";
			String url = "";
			FileOutputStream outstream = null;
			outpath = basePath + "\\tmp\\zxjftj.xls";
			url = request.getContextPath() + "\\resources\\PDF\\tmp\\zxjftj.xls";
			outstream = new FileOutputStream(outpath);

			String tplFullName = request.getRealPath("/WEB-INF/jsp/wjmb/zxjftj.xls");
			InputStream input = new FileInputStream(tplFullName);
			HSSFWorkbook wb = null;// 定义一个新的工作簿
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
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
