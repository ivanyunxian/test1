package com.supermap.wisdombusiness.workflow.web.wfm.serverlayer;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.supermap.wisdombusiness.workflow.service.wfm.SNSearchService;

@Controller
@RequestMapping("/snsearch")
public class SNSearchController {
	@Autowired
	private SNSearchService sNSearchService;
	/**
	 * 中间库查询页面映射
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/snjyzjksearch", method = RequestMethod.GET)
	public String QuestionAndAnswer(Model model) {
		return "/workflow/frame/snjyzjksearch";
	}
	/**
	 *遂宁根据权利人名称、坐落、原不动产权证号、预售许可证号等条件查询交易权利信息
	 * @throws SQLException 
	 * @throws JSONException 
	 */
	@RequestMapping(value = "/search/qlrmcsearch", method = RequestMethod.POST)
	public @ResponseBody Map<String, String> GetQlrmcsearch(HttpServletRequest request, HttpServletResponse response) throws SQLException, JSONException {
		String qlrmc = request.getParameter("qlrmc");
		String checked = request.getParameter("checked");
		String starttime = request.getParameter("starttime");
		String endtime = request.getParameter("endtime");
		ResultSet resultset= sNSearchService.GetQlrmcsearch(qlrmc,checked,starttime,endtime);
		return getResultMap(resultset);
//		if(!resultset.next()){
//			return null;
//		}else{
//			return getResultMap(resultset);
//		}
	}
	@RequestMapping(value = "/search/zlsearch", method = RequestMethod.POST)
	public @ResponseBody Map<String, String> GetZlsearch(HttpServletRequest request, HttpServletResponse response) throws SQLException, JSONException {
		String zl = request.getParameter("zl");
		String checked = request.getParameter("checked");
		String starttime = request.getParameter("starttime");
		String endtime = request.getParameter("endtime");
		ResultSet resultset= sNSearchService.GetZlsearch(zl,checked,starttime,endtime);
		return getResultMap(resultset);
	}
	@RequestMapping(value = "/search/bdcqzhsearch", method = RequestMethod.POST)
	public @ResponseBody Map<String, String> GetBdcqzhsearch(HttpServletRequest request, HttpServletResponse response) throws SQLException, JSONException {
		String bdcqzh = request.getParameter("bdcqzh");
		String checked = request.getParameter("checked");
		String starttime = request.getParameter("starttime");
		String endtime = request.getParameter("endtime");
		ResultSet resultset=  sNSearchService.GetBdcqzhsearch(bdcqzh,checked,starttime,endtime);
		return getResultMap(resultset);
	}
	@RequestMapping(value = "/search/ysxkzhsearch", method = RequestMethod.POST)
	public @ResponseBody Map<String, String> GetYsxkzhsearch(HttpServletRequest request, HttpServletResponse response) throws SQLException, JSONException {
		String ysxkzh = request.getParameter("ysxkzh");
		String checked = request.getParameter("checked");
		String starttime = request.getParameter("starttime");
		String endtime = request.getParameter("endtime");
		ResultSet resultset= sNSearchService.GetYsxkzhsearch(ysxkzh,checked,starttime,endtime);
		return getResultMap(resultset);
	}
	
	//根据业务号自动填充权利人信息
	@RequestMapping(value = "/search/qlrinfosearch/{ywh}", method = RequestMethod.POST)
	public @ResponseBody Map<String, String> GetQlrInfo(@PathVariable String ywh,HttpServletRequest request) throws SQLException, JSONException {
		ResultSet resultset=  sNSearchService.GetQlrInfo(ywh);
		return getResultMap(resultset);
	}
	//根据业务号自动填充户信息
	@RequestMapping(value = "/search/huinfosearch/{ywh}", method = RequestMethod.POST)
	public @ResponseBody Map<String, String> GetHuInfo(@PathVariable String ywh,HttpServletRequest request) throws SQLException, JSONException {
		ResultSet resultset=  sNSearchService.GetHuInfo(ywh);
		return getResultMap(resultset);
	}
	/**
	 * 转出环节“预警”服务
	 * 
	 * @param project_id
	 *            返回值说明：“0”：正常转出无任何操作；“1”：弹框提示‘该件为交易审核不通过件！’；“2”：弹框提示‘
	 *            该房屋交易系统当前时间未推送到中间库！’
	 */
	@RequestMapping(value = "/search/earlywarning/{project_id}", method = RequestMethod.POST)
	public @ResponseBody String EarlyWarning(@PathVariable String project_id,HttpServletRequest request) throws SQLException, JSONException {
		String warn = sNSearchService.EarlyWarning(project_id);
		return warn;
	}
    
    private static Map<String, String> getResultMap(ResultSet rs) throws SQLException {  
        Map<String, String> hm = new HashMap<String, String>();  
        ResultSetMetaData rsmd = rs.getMetaData();  
        int count = rsmd.getColumnCount();  
		if (rs.next()) {
			for (int i = 1; i <= count; i++) {
				String key = rsmd.getColumnLabel(i);
				String value = rs.getString(i);
				hm.put(key, value);
			}
		}
        return hm;  
}  

   
}
