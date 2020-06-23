package com.supermap.wisdombusiness.workflow.web.wfm.serverlayer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.supermap.realestate.registration.util.RequestHelper;
import com.supermap.wisdombusiness.web.Message;
import com.supermap.wisdombusiness.workflow.service.wfi.SmStaff;
import com.supermap.wisdombusiness.workflow.service.wfm.SmMaterialService;
import com.supermap.wisdombusiness.workflow.service.wfm.StatisticService;

@Controller
@RequestMapping("/statistic")
public class StatisticsController {
	private final String prefix="/workflow/statistic/";
	@Autowired
	private StatisticService statisticService;
	@Autowired
	private SmStaff smStaff;
	
	@Autowired
	private SmMaterialService smMaterialService;
	/*
	 * 效能统计
	 */
	@RequestMapping(value = "/efficiency", method = RequestMethod.GET)
	public String ShowIndex(Model model) {

		return prefix+"efficiency";
	}
	@RequestMapping(value = "/month/project/count/{year}", method = RequestMethod.GET)
	@ResponseBody
	public List<Map> GetStaffMonthProject(@PathVariable int year,HttpServletRequest request, HttpServletResponse response){
		String staff="'"+smStaff.getCurrentWorkStaffID()+"'";
		return statisticService.GetYearProject(year,staff);
	}
	@RequestMapping(value = "/month/project/overdue/{year}", method = RequestMethod.GET)
	@ResponseBody
	public Map GetStaffOverDueProject(@PathVariable int year,HttpServletRequest request, HttpServletResponse response){
		String staff="'"+smStaff.getCurrentWorkStaffID()+"'";
		return statisticService.GetOverDueProject(year,staff);
	}
	
    @RequestMapping(value = "/djzxbjltj/{qsj}/{zsj}", method = RequestMethod.GET)
    public @ResponseBody Message getDJZXBJLTJ(@PathVariable("qsj") String tjsjqs,@PathVariable("zsj") String tjsjjz,
    		HttpServletResponse response) throws Exception {
		Message msg = statisticService.GetDJZXYWTJ(tjsjqs, tjsjjz);
		return msg;
    }
	
    @RequestMapping(value = "/ksbjltj/{qsj}/{zsj}", method = RequestMethod.GET)
    public @ResponseBody Message getKSYWTJ(@PathVariable("qsj") String tjsjqs,@PathVariable("zsj") String tjsjjz,
    		HttpServletRequest request,HttpServletResponse response) throws Exception {
    	String deptid = request.getParameter("deptid");
		Message msg = statisticService.GetKSYWTJ(tjsjqs, tjsjjz, deptid);
		return msg;
    }
    
    /**
     * 登记中心受理量
     */
    @RequestMapping(value = "/acceptcount", method = RequestMethod.GET)
    public @ResponseBody List<Map> getAcceptCount(HttpServletRequest request,HttpServletResponse response) throws Exception {
    	String qssj = request.getParameter("qssj");
    	String zzsj = request.getParameter("zzsj");
    	String pronames = RequestHelper.getParam(request, "pronames");
    	return statisticService.getAcceptCount(qssj, zzsj, pronames);
    }
    
    /**
     * 登记中心办结量
     */
    @RequestMapping(value = "/endcount", method = RequestMethod.GET)
    public @ResponseBody List<Map> getEndCount(HttpServletRequest request,HttpServletResponse response) throws Exception {
    	String qssj = request.getParameter("qssj");
    	String zzsj = request.getParameter("zzsj");
    	String pronames = RequestHelper.getParam(request, "pronames");
		return statisticService.getEndCount(qssj, zzsj, pronames);
    }
    
    /**
     * 登记中心退件量
     */
    @RequestMapping(value = "/canclecount", method = RequestMethod.GET)
    public @ResponseBody List<Map> getCancleCount(HttpServletRequest request,HttpServletResponse response) throws Exception {
    	String qssj = request.getParameter("qssj");
    	String zzsj = request.getParameter("zzsj");
    	String pronames = RequestHelper.getParam(request, "pronames");
		return statisticService.getCancleCount(qssj, zzsj, pronames);
    }
    
    /**
     * 办结件数量统计
     */
    @RequestMapping(value = "/actend", method = RequestMethod.GET)
    public @ResponseBody List<Map> getActendCount(HttpServletRequest request,HttpServletResponse response) throws Exception {
    	String tjlx = request.getParameter("tjlx");
    	String depid = request.getParameter("depid");
    	String qssj = request.getParameter("qssj");
    	String zzsj = request.getParameter("zzsj");
    	String pronames = RequestHelper.getParam(request, "pronames");
    	return statisticService.getActendCount(tjlx,depid,qssj, zzsj, pronames);
    }
    
    /**
     * 超时件数量统计
     */
    @RequestMapping(value = "/actouttime", method = RequestMethod.GET)
    public @ResponseBody List<Map> getActouttimeCount(HttpServletRequest request,HttpServletResponse response) throws Exception {
    	String tjlx = request.getParameter("tjlx");
    	String depid = request.getParameter("depid");
    	String qssj = request.getParameter("qssj");
    	String zzsj = request.getParameter("zzsj");
    	String pronames = RequestHelper.getParam(request, "pronames");
    	return statisticService.getActouttimeCount(tjlx,depid,qssj, zzsj, pronames);
    }
    
    /**
     * 驳回件数量统计
     */
    @RequestMapping(value = "/actpassback", method = RequestMethod.GET)
    public @ResponseBody List<Map> getActpassbackCount(HttpServletRequest request,HttpServletResponse response) throws Exception {
    	String tjlx = request.getParameter("tjlx");
    	String depid = request.getParameter("depid");
    	String qssj = request.getParameter("qssj");
    	String zzsj = request.getParameter("zzsj");
    	String pronames = RequestHelper.getParam(request, "pronames");
    	return statisticService.getActpassbackCount(tjlx,depid,qssj, zzsj, pronames);
    }
    
    /**
     * 活动平均办理时间统计
     */
    @RequestMapping(value = "/actavgtime", method = RequestMethod.GET)
    public @ResponseBody List<Map> getActavgtimeCount(HttpServletRequest request,HttpServletResponse response) throws Exception {
    	String tjlx = request.getParameter("tjlx");
    	String depid = request.getParameter("depid");
    	String qssj = request.getParameter("qssj");
    	String zzsj = request.getParameter("zzsj");
    	String pronames = RequestHelper.getParam(request, "pronames");
    	return statisticService.getActavgtimeCount(tjlx,depid,qssj, zzsj, pronames);
    }
    
    /**
     * 办件量统计导出
     * @param tjsjks
     * @param tjsjjz
     * @param request
     * @param reponse
     * @return
     * @throws IOException
     */
    @SuppressWarnings({ "unchecked", "rawtypes", "deprecation" })
	@RequestMapping(value="/ywtj/excel",method=RequestMethod.POST)
  	@ResponseBody
	public String kstjlexport( HttpServletRequest request,HttpServletResponse reponse) throws IOException{
    	LinkedHashMap map = new LinkedHashMap<String, String>();
    	String data = request.getParameter("tjdata");
    	JSONArray tjsj  = JSONArray.fromObject(data);
    	/*List<Map> bjltj = JSONArray.parseArray(data, Map.class);*/
    	
    	List<Map<String, String>> bjltj = (List<Map<String, String>>) JSONArray.toCollection(tjsj, Map.class);
		String basePath = request.getRealPath("/")+"\\resources\\PDF";
		String url = request.getContextPath()+"\\resources\\PDF\\tmp\\rybjltj.xls";
		String outPath = basePath + "\\tmp\\rybjltj.xls";
		String tmpFullName = request.getRealPath("/WEB-INF/jsp/wjmb/rybjltj.xls");

		for(int i=0,j=bjltj.get(0).entrySet().size();i<j;i++){
			map.put(i+"", i+"");
		}
		return smMaterialService.excelDownload1(url,outPath,tmpFullName,map,bjltj,0,false);

	}
}
