package com.supermap.wisdombusiness.workflow.web.wfm.serverlayer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.supermap.realestate.registration.ViewClass.ProjectInfo;
import com.supermap.realestate.registration.util.ConfigHelper;
import com.supermap.realestate.registration.util.ProjectHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.services.ogc.filter.encode.ParseException;
import com.supermap.wisdombusiness.framework.model.User;
import com.supermap.wisdombusiness.web.ResultMessage;
import com.supermap.wisdombusiness.workflow.dao.CommonDao;
import com.supermap.wisdombusiness.workflow.model.Wfi_ActInst;
import com.supermap.wisdombusiness.workflow.model.Wfi_ProInst;
import com.supermap.wisdombusiness.workflow.model.Wfi_ProMater;
import com.supermap.wisdombusiness.workflow.service.common.SmObjInfo;
import com.supermap.wisdombusiness.workflow.service.common.SmProInfo;
import com.supermap.wisdombusiness.workflow.service.wfi.SmActInst;
import com.supermap.wisdombusiness.workflow.service.wfi.SmProInst;
import com.supermap.wisdombusiness.workflow.service.wfi.SmProMater;
import com.supermap.wisdombusiness.workflow.service.wfi.SmStaff;
import com.supermap.wisdombusiness.workflow.service.wfm.AdditionalService;
import com.supermap.wisdombusiness.workflow.service.wfm.OperationService;
import com.supermap.wisdombusiness.workflow.service.wfm.SmMaterialService;
import com.supermap.wisdombusiness.workflow.service.wfm.SmProInstService;
import com.supermap.wisdombusiness.workflow.util.Message;

//工作流附加功能
@Controller
@RequestMapping("/additional")
public class additionalController {
	
	@Autowired
	private SmStaff smStaff;
	@Autowired
	private SmActInst _smActInst;
	@Autowired
	private SmProInst _SmProInst;
	@Autowired
	private SmProInstService ProInstService;
	@Autowired
	private OperationService operationService;
	@Autowired
	private SmMaterialService smMaterialService;
	@Autowired
	private AdditionalService additionalService;
	@Autowired
	private CommonDao commonDao;
	
	//批量导入受理页面
	@RequestMapping(value = "/batchaccept", method = RequestMethod.GET)
	public String batchAccept(Model model) {
		return "/workflow/frame/batchaccept";
	}
	
	//批量导入excel受理页面
	@RequestMapping(value = "/acceptbyexcel", method = RequestMethod.GET)
	public String acceptByExcel(Model model) {
		return "/workflow/frame/acceptbyexcel";
	}
	
	//打印批量受理页面
	@RequestMapping(value = "/batchreceipt", method = RequestMethod.GET)
	public String BatchReceipt(Model model) {
		return "/workflow/frame/batchreceipt";
	}
	//批量件修改页面
	@RequestMapping(value = "/modifypro", method = RequestMethod.GET)
	public String relateMaterial(Model model){
		return "/workflow/frame/modifypro";
	}
	//添加批量件页面
	@RequestMapping(value = "/addbatchpro",method = RequestMethod.GET)
	public String addBatchPro(Model model){
		return "/workflow/frame/addbatchpro";
	}
	
	/**
	 * 上传批量受理ywh
	 * 
	 * @param file
	 * @param request
	 * @param response
	 * @return 
	 * @return 
	 * @throws IOException
	 */
	@RequestMapping(value = "/batchaccept/updatetxt", method = RequestMethod.POST)
	public void upload(@RequestParam("file") MultipartFile file,@RequestParam("ProDef_Name") String ProDef_Name,
			@RequestParam("ProDef_ID") String ProDef_ID,HttpServletRequest request, HttpServletResponse response, Object command) throws IOException {
		List<String> list = new ArrayList<String>();
		String errorList = "";
		String successList = "";
		String sywh="";
		String djyy="";
		User user = smStaff.getCurrentWorkStaff();
		BufferedReader bufferedReader =new BufferedReader(new InputStreamReader(file.getInputStream()));
		 for (String line = bufferedReader.readLine(); line != null; line = bufferedReader.readLine()) {
			 sywh=line.trim();
             	if(sywh!=null&&!sywh.isEmpty()){
             		list.add(sywh);
             	}
			}
		 bufferedReader.close();
		 String batch = commonDao.CreatMaxID(user.getAreaCode(), "0", "BATCH");
		 for(String ss : list){
			 SmProInfo smproinf = new SmProInfo();
			 String ywh="",gxzlbm="";
			 djyy = "";
			 smproinf.setProInst_Name("");
				smproinf.setProDef_ID(ProDef_ID);
				smproinf.setProDef_Name(ProDef_Name);
				smproinf.setFile_Urgency("1");
				String[] _info = ss.split(";|；");
				switch(_info.length){
				case 5: gxzlbm=_info[4].trim();
				case 4 : djyy=_info[3].trim();
				case 3:smproinf.setMessage(_info[2].trim());
				case 2:smproinf.setProInst_Name(_info[1].trim());
				case 1:ywh = _info[0].trim(); break;
				}
				smproinf.setYwh(ywh);
				smproinf.setBatch(batch);
				smproinf.setGxzlbm(gxzlbm);
			 try{
				 SmObjInfo info = ProInstService.Accept(smproinf);
				 SmObjInfo smInfo = info.getChildren().get(0);
				 List<Wfi_ActInst> inst=commonDao.getDataList(Wfi_ActInst.class, "select * from BDC_WORKFLOW.WFI_ACTINST WHERE PROINST_ID='"+info.getID()+"'");
				 _smActInst.SetActinstWorkStaff(inst.get(0).getActinst_Id(),user.getId());
				 if(info.getDesc()!=null&&info.getDesc().equals("受理成功")){
					 ProjectInfo project=ProjectHelper.GetProjectFromRest(info.getFile_number(), request);
					 if(project!=null&&project.getXmbh()!=null&&!project.getXmbh().isEmpty()){
						 Map<String , String > map = new HashMap<String , String >();
							map.put("ywh", ywh);
							map.put("prolsh",smInfo.getID());
							map.put("xmbh", project.getXmbh());
							map.put("file_number", info.getFile_number());
							map.put("successinfo", "成功创建项目");
							map.put("projectname", smInfo.getName());
							map.put("djyy", djyy);
						 successList+=map.toString();
					 }else{
						 Map<String , String > map = new HashMap<String , String >(); 
							map.put("ywh", ywh);
							map.put("prolsh",info.getText());
							map.put("errorinfo", "创建项目信息失败");
							map.put("projectname", smInfo.getName());
							errorList+=map.toString();
					 }
				 }else{
					 Map<String,String> map = new HashMap<String,String>(); 
						map.put("ywh", ywh);
						map.put("errorinfo", "创建项目失败");
						errorList+=map.toString();
				 }
				}catch(Exception e){
					Map<String,String> map = new HashMap<String,String>(); 
					map.put("ywh", ywh);
					map.put("errorinfo", "未知错误");
					errorList+=map.toString();
				}
		 }
		 response.getWriter().write(successList+"&&"+errorList);
	}
	
	/**
	 *大宗件受理导出excel
	 *
	 * @author dff
	 * @param url
	 * @param 
	 * @return String
	 * @throws ParseException
	 * @throws IOException
	 */
	@RequestMapping(value = "/batchaccept/downExcel", method = RequestMethod.POST)
	@ResponseBody
	public String acceptExcel(HttpServletRequest request, HttpServletResponse response) throws IOException{
		String list=request.getParameter("list");
		List<Map<String,String>> djdys = new ArrayList<Map<String,String>>();
		if(list!=null&&!list.isEmpty()){
			String[] obj=list.split("&&");
			for(String s : obj){
				String[] info = s.split(",");
				Map<String,String> tt = new HashMap<String,String>();
				tt.put("ywh",info[0]);
				tt.put("projectname", info[1]);
				tt.put("prolsh",info[2]);
				tt.put("creatxmxx",info[3]);
				tt.put("creatdata",info[4]);
				tt.put("errormsg",info[5]);
				djdys.add(tt);
			}
			LinkedHashMap<String,String> map= new LinkedHashMap<String,String>();
			String basePath = request.getRealPath("/") + "\\resources\\PDF";
			String url = "";
			String outpath ="";
			String tmpFullName ="";
				url = request.getContextPath() + "\\resources\\PDF\\tmp\\batchaccept.xls";
				 outpath = basePath + "\\tmp\\batchaccept.xls";
				 tmpFullName = request.getRealPath("/WEB-INF/jsp/wjmb/batchaccept.xls");
				 map.put("房产业务号", "ywh");
				 map.put("项目名称", "projectname");
				 map.put("项目流水号", "prolsh");  
				 map.put("是否成功创建项目", "creatxmxx"); 
				 map.put("是否成功抽取数据", "creatdata");
				 map.put("失败原因", "errormsg");
			return smMaterialService.excelDownload(url, outpath, tmpFullName, map, djdys);
		}
		return null;
	}
	
	//大宗件excel受理
	
/*	@RequestMapping(value = "/batchaccept/uploadexcel", method = RequestMethod.POST)
	public void acceptByExcel(@RequestParam("file") MultipartFile file,@RequestParam("ProDef_Name") String ProDef_Name,
			@RequestParam("ProDef_ID") String ProDef_ID,HttpServletRequest request, HttpServletResponse response, Object command) throws IOException {
		
	}*/
	/**
	 * 海口接口需求
	 * 通过申请人和证件号查办理过的所有不动产登记业务的项目编号及办理业务当前所处的环节名称
	 * @author zhangp
	 * @data 2017年8月9日下午8:04:15
	 * @param sqr
	 * @param cardId
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping(value = "/partproinfo",method = RequestMethod.GET)
	@ResponseBody
	public List<LinkedHashMap<String,String>> getProInfoBySqrAndCard(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException{
		String sqr = request.getParameter("sqr");
		String card = request.getParameter("card");
		sqr = new String(sqr.getBytes("iso8859-1"), "utf-8");
		return additionalService.getPartProinfoBySqrAndCard(sqr, card);
	}
	/**
	 * 档案接收接口
	 * @author zhangp
	 * @data 2017年11月14日上午9:57:21
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/archives",method = RequestMethod.GET)
	@ResponseBody
	public Map<String,Object> archiveList(HttpServletRequest request,HttpServletResponse response){
		String transfercode = request.getParameter("code");
		String pageIndex = request.getParameter("pageindex");
		String pageSize = request.getParameter("pagesize");
		pageIndex = !StringHelper.isEmpty(pageIndex)?pageIndex:"1";
		pageSize = !StringHelper.isEmpty(pageSize)?pageSize:"15";
		Map<String,Object> map = new HashMap<String, Object>();
		if(!StringHelper.isEmpty(transfercode)){
			map = additionalService.archiveList(transfercode, Integer.parseInt(pageIndex),Integer.parseInt(pageSize));
		}
		return map;
	}
	/**
	 * 检查首环节受理时间与项目创建时间是否一致
	 * @author zhangp
	 * @data 2017年11月14日上午9:57:56
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/checkactstart",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> CheckActinstStart(HttpServletRequest request, HttpServletResponse response){
		Map<String,Object> map = new HashMap<String, Object>();
		String actinstid = request.getParameter("actinstid");
		String actstart = request.getParameter("actstart");
		String type = request.getParameter("type");
		if(!StringHelper.isEmpty(actinstid)){
			if(!StringHelper.isEmpty(actstart)){
				map = additionalService.CheckActinstStart(actinstid,actstart,type);
			}
			Wfi_ProInst proinst = additionalService.TimePlusWorkDay(actinstid, "1");
			map.put("proinst",proinst);
		}
		return map;
	}
	/**
	 * 关联项目资料
	 * @author zhangp
	 * @data 2017年11月16日上午10:01:11
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/relatematerial",method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> RelateMaterial(HttpServletRequest request,HttpServletResponse response){
		Map<String,Object> relatemap = new HashMap<String, Object>();
		String relateProlsh = request.getParameter("relateProlsh");
		String currActinstid = request.getParameter("currActinstid");
		if(!StringHelper.isEmpty(relateProlsh)&&!StringHelper.isEmpty(currActinstid)){
			relatemap = additionalService.RelateMaterial(relateProlsh, currActinstid);
		}
		return relatemap;
	}
	/**
	 * 铁岭验证呼号
	 * @author zhangp
	 * @data 2017年11月16日上午10:40:11
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/check/hh",method = RequestMethod.GET)
	@ResponseBody
	public boolean checkHH(HttpServletRequest request,
			HttpServletResponse response) {
		boolean flag = false;
		String HH = request.getParameter("hh");
		if (!StringHelper.isEmpty(HH)) {
			flag = additionalService.checkHH(HH);
		}
		return flag;
	}

	/**
	 * 根据批次号、流水号查找项目（乌鲁木齐）
	 * @author zhangp
	 * @data 2017年11月23日上午11:36:17
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/searchpro",method=RequestMethod.GET)
	@ResponseBody
	public Message searchProjectByPCH(HttpServletRequest request, HttpServletResponse response){
		String batch = request.getParameter("batch");
		String pageIndex = request.getParameter("pageindex");
		String pageSize = request.getParameter("pagesize");
		String prolsh = request.getParameter("prolsh");
		return  additionalService.searchProject(pageIndex, pageSize, batch, prolsh, request);
	}
	//从批量件中移除
	@RequestMapping(value = "/removeproject",method = RequestMethod.POST)
	@ResponseBody
	public boolean removeProject(HttpServletRequest request, HttpServletResponse response){
		boolean success = false;
		String proinstid = request.getParameter("proinstid");
		if(!StringHelper.isEmpty(proinstid)){
			success = additionalService.removeProject(proinstid);
		}
		return success;
	}
	//给批量件中添加
	@RequestMapping(value = "/addproject", method = RequestMethod.POST)
	@ResponseBody
	public boolean addProject(HttpServletRequest request,HttpServletResponse response){
		boolean success = false;
		String proinstid = request.getParameter("proinstid");
		String batch = request.getParameter("batch");
		String[] strArray = null;   
        strArray = proinstid.split(",");
		if(strArray.length>0&&!StringHelper.isEmpty(batch)){
			success = additionalService.addProject(strArray,batch);
		}
		return success;
	}
	/**
	 * 获取指定项目集合中包含驳回环节的项目
	 * @author zhangp
	 * @data 2017年12月11日上午10:04:04
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/colour",method = RequestMethod.GET)
	@ResponseBody
	public List<Map> ColourPassback(HttpServletRequest request, HttpServletResponse response){
		String filenumbers = request.getParameter("filenumbers");
		List<Map> result = new ArrayList<Map>();
		String[] strArray = filenumbers.split(",");
		if(strArray.length>0){
			for(String str : strArray){
				Map map = additionalService.ColourPassback(str);
				result.add(map);
			}
		}
		return result;
	}
	/**
	 * 批量转办（按角色）
	 * @author zhangp
	 * @data 2017年12月12日下午4:06:24
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/trunotherstaff",method=RequestMethod.POST)
	@ResponseBody
	public SmObjInfo BatchTurnTodo(HttpServletRequest request,HttpServletResponse response){
		SmObjInfo smObjInfo = new SmObjInfo();
		String actinsts = request.getParameter("actinsts");
		String msg = request.getParameter("msg");
		String[] actinstArray = actinsts.split(",");
		String staff = request.getParameter("staff");
		if(!StringHelper.isEmpty(staff)){
			smObjInfo = additionalService.batchTurnTodo(actinstArray,msg,staff);
		}else{
			smObjInfo = additionalService.batchTurnTodo(actinstArray,msg);
		}
		return smObjInfo;
	}
	/**
	 * 判断是否是批次件
	 * @author zhangp
	 * @data 2017年12月13日下午9:45:14
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/isbatchproject",method = RequestMethod.GET)
	@ResponseBody
	public boolean isBatchProject(HttpServletRequest request, HttpServletResponse response){
		boolean isbatch = false;
		String actinstid = request.getParameter("actinstid");
		if(!StringHelper.isEmpty(actinstid)){
			isbatch = additionalService.isBatchProject(actinstid);
		}
		return isbatch;
	}
	/**
	 * 改变流程期望时间（以首环节结束时间开始计算）
	 * @author zhangp
	 * @data 2017年12月14日下午5:59:18
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/change/timing",method = RequestMethod.POST)
	@ResponseBody
	public boolean act2Timing(HttpServletRequest request, HttpServletResponse response){
		boolean success = false;
		String actinstid = request.getParameter("actinstid");
		if(!StringHelper.isEmpty(actinstid)){
			success = additionalService.act2Timing(actinstid);
		}
		return success;
	}
	/**
	 * 获取即将超期的质检项目总数
	 * @author zhangp
	 * @data 2017年12月18日下午5:58:11
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/qualitytest/willoverdue",method = RequestMethod.GET)
	@ResponseBody
	public Map QualitytestWilldue(HttpServletRequest request, HttpServletResponse response){
		String remainingtime = request.getParameter("remainingtime");
		remainingtime = !StringHelper.isEmpty(remainingtime)?remainingtime:"2";
		return additionalService.QualitytestWilldue(remainingtime);
	}
	/**
	 * 批量取消质检
	 * @author zhangp
	 * @data 2017年12月18日下午8:17:43
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/qualitytest/cancelall",method = RequestMethod.POST)
	@ResponseBody
	public boolean QualitytestCancelall(HttpServletRequest request, HttpServletResponse response){
		String remainingtime = request.getParameter("remainingtime");
		remainingtime = !StringHelper.isEmpty(remainingtime)?remainingtime:"2";
		return additionalService.QualitytestCancelall(remainingtime);
	} 
	/**
	 * 海口住建、地税接口
	 * @author zhangp
	 * @data 2018年2月2日上午12:02:21
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/result",method = RequestMethod.POST)
	@ResponseBody
	public ResultMessage projectMaterAndInfo(HttpServletRequest request,HttpServletResponse response) throws Exception{
		ResultMessage msg = new ResultMessage();
		msg.setSuccess("false");
		msg.setMsg("推送失败");
		System.out.println("-1");
		String info = request.getParameter("info");
		System.out.println("访问接收数据为："+info);
		System.out.println("0");
		if(!StringHelper.isEmpty(info)){
			Map json = (Map) JSONObject.parse(info); 
			System.out.println("1------------");
			if(null!=json){
				String file_number = (null==json.get("filenumber"))?"":json.get("filenumber").toString();
				System.out.println("2------------");
				if(!StringHelper.isEmpty(file_number)){
					msg = additionalService.projectInfo(file_number, json);
				}
			}
		}
		return msg;
	}
	
	@RequestMapping(value="/test",method = RequestMethod.GET)
	@ResponseBody
	public void test(HttpServletRequest request,HttpServletResponse response){
		additionalService.test();
	}
	
}
