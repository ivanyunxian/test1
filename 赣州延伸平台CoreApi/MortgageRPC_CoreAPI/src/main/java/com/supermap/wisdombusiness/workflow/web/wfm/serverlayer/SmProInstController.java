package com.supermap.wisdombusiness.workflow.web.wfm.serverlayer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import com.supermap.realestate.registration.ViewClass.ProjectInfo;
import com.supermap.realestate.registration.model.BDCS_DJFZ;
import com.supermap.realestate.registration.model.BDCS_SQR;
import com.supermap.realestate.registration.model.BDCS_XMXX;
import com.supermap.realestate.registration.service.ChargeService;
import com.supermap.realestate.registration.service.DBService;
import com.supermap.realestate.registration.service.DJBService;
import com.supermap.realestate.registration.service.ProjectService;
import com.supermap.realestate.registration.service.ZSService;
import com.supermap.realestate.registration.tools.HttpRequestTools;
import com.supermap.realestate.registration.util.ConfigHelper;
import com.supermap.realestate.registration.util.ConstMapping;
import com.supermap.realestate.registration.util.ConstValue;
import com.supermap.realestate.registration.util.Global;
import com.supermap.realestate.registration.util.ProjectHelper;
import com.supermap.realestate.registration.util.RequestHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.realestate.registration.util.UploadDiskHelper;
import com.supermap.realestate.registration.util.YwLogUtil;
import com.supermap.wisdombusiness.core.SuperHelper;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import com.supermap.wisdombusiness.framework.model.User;
import com.supermap.wisdombusiness.framework.service.RoleService;
import com.supermap.wisdombusiness.framework.service.UserService;
import com.supermap.wisdombusiness.web.ResultMessage;
import com.supermap.wisdombusiness.web.ui.tree.Tree;
import com.supermap.wisdombusiness.workflow.model.Bank_TrustbookPage;
import com.supermap.wisdombusiness.workflow.model.Wfd_Actdef;
import com.supermap.wisdombusiness.workflow.model.Wfd_Prodef;
import com.supermap.wisdombusiness.workflow.model.Wfi_ActInst;
import com.supermap.wisdombusiness.workflow.model.Wfi_MaterData;
import com.supermap.wisdombusiness.workflow.model.Wfi_PassWork;
import com.supermap.wisdombusiness.workflow.model.Wfi_ProInst;
import com.supermap.wisdombusiness.workflow.model.Wfi_ProMater;
import com.supermap.wisdombusiness.workflow.model.Wfi_ProUserInfo;
import com.supermap.wisdombusiness.workflow.model.Wfi_ProinstDate;
import com.supermap.wisdombusiness.workflow.service.common.Common;
import com.supermap.wisdombusiness.workflow.service.common.QueryCriteria;
import com.supermap.wisdombusiness.workflow.service.common.SmObjInfo;
import com.supermap.wisdombusiness.workflow.service.common.SmProInfo;
import com.supermap.wisdombusiness.workflow.service.wfi.SmActInst;
import com.supermap.wisdombusiness.workflow.service.wfi.SmProInst;
import com.supermap.wisdombusiness.workflow.service.wfi.SmProMater;
import com.supermap.wisdombusiness.workflow.service.wfi.SmStaff;
import com.supermap.wisdombusiness.workflow.service.wfi.SmSysMod;
import com.supermap.wisdombusiness.workflow.service.wfi.proinstStateModify;
import com.supermap.wisdombusiness.workflow.service.wfm.OperationService;
import com.supermap.wisdombusiness.workflow.service.wfm.SmMaterialService;
import com.supermap.wisdombusiness.workflow.service.wfm.SmProDefService;
import com.supermap.wisdombusiness.workflow.service.wfm.SmProInstService;
import com.supermap.wisdombusiness.workflow.service.wfm.SmProMaterService;
import com.supermap.wisdombusiness.workflow.util.FileUpload;
import com.supermap.wisdombusiness.workflow.util.Message;
import com.supermap.wisdombusiness.workflow.util.Page;
import com.supermap.yingtanothers.file.FileDownload;
import com.supermap.yingtanothers.file.ZipCompressor;
import com.supermap.yingtanothers.file.deleteAllFile;

import jcifs.smb.SmbFile;
import net.sf.json.JSONObject;
import sun.misc.BASE64Decoder;
@Controller
@RequestMapping("/frame")
public class SmProInstController {
	private static final Log logger = LogFactory.getLog(SmProInstService.class);
	@SuppressWarnings("unused")
	private Map<String, SmProInfo> smproinfo = new HashMap<String, SmProInfo>();
	@Autowired
	private SmProInstService ProInstService;
	@Autowired
	private SmSysMod SmSysMod;
	@Autowired
	private SmProMater SmProMater;
	@Autowired
	private SmStaff smStaff;
	@Autowired
	private SmProMaterService ProMaterService;
	/** 登薄service */
	@Autowired
	private DBService dbService;
	/** 登记薄service */
	@Autowired
	private DJBService djbService;
	@Autowired
	private ZSService zsService;
	@Autowired
	private ProjectService projectService;
	@Autowired
	private ChargeService chargeService;

	@Autowired
	private OperationService operationService;

	@Autowired
	private SmProDefService smProDefService;

	@Autowired
	private SmProInstService smProInstService;

	@Autowired
	private SmActInst smactinst;
	@Autowired
	private SmProInst smproinst;

	@Autowired
	private UserService userService;
	// 海口
	@Autowired
	private proinstStateModify proinststatemodify;
	
	@Autowired
	private RoleService roleService;
	
	@Autowired
	private UserService userServive;

	@Autowired
	private CommonDao baseCommonDao;
	
	@Autowired
	private SmMaterialService smMaterialService;
	
	
	@RequestMapping(value = "/projectjsbj", method = RequestMethod.GET)
	public String projectjsbj(Model model) {
		return "/workflow/frame/projectjsbj";
	}
	
	@RequestMapping(value = "/passwork", method = RequestMethod.GET)
	public String client(Model model) {
		return "/workflow/frame/passwork";
	}

	/**
	 * 批量操作页面
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/printDecisionBook", method = RequestMethod.GET)
	public String printDecisionBook(Model model) {
		System.out.println("point cut");
		return "/workflow/frame/printDecisionBook";
	}

	/**
	 * 测试页面
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/cs", method = RequestMethod.GET)
	public String cs(Model model) {
		return "/workflow/frame/cs";
	}

	/**
	 * 批量操作页面
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/batchpassover", method = RequestMethod.GET)
	public String batchpassover(Model model) {
		return "/workflow/frame/batchpassover";
	}
	
	/**
	 * 挂起操作页面
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/hangup", method = RequestMethod.GET)
	public String hangup(Model model) {
		return "/workflow/frame/hangup";
	}

	/**
	 * 注销操作页面
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/zxproject", method = RequestMethod.GET)
	public String zxproject(Model model) {
		return "/workflow/frame/zxproject";
	}
	
/*	--------------------------------------------
 * 合同组件模板页面跳转
*/	
	@RequestMapping(value = "/constact", method = RequestMethod.GET)
	public String constact(Model model) {
		return "/workflow/module/contract";
	}
	@RequestMapping(value = "/transferSP", method = RequestMethod.GET)
	public String transferSP(Model model) {
		return "/workflow/module/transferSP";
	}
	/**
	 * congifgjs文件本地化配置说明
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/native/configjs", method = RequestMethod.GET)
	public String nativeConfigjsDesc(Model model) {
		return "/workflow/frame/configdesc";
	}

	/**
	 * @author JHX 挂起操作页面（海口版本）
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/hangupHK", method = RequestMethod.GET)
	public String hangupHK(Model model) {
		List<Map<String, String>> jsonArray = proinststatemodify.getRule();
		model.addAttribute("select", jsonArray);
		return "/workflow/frame/hangupHK";
	}

	/**
	 * 批量登簿页面
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/batchboardbook", method = RequestMethod.GET)
	public String batchboardbook(Model model) {
		return "/workflow/frame/batchboardbook";
	}

	/**
	 * 批量审核页面
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/batchaudit", method = RequestMethod.GET)
	public String batchaudit(Model model) {
		return "/workflow/frame/batchaudit";
	}

	/**
	 * @author JHX 领导办理办件界面
	 * @DATE:2016-08-23 17：03
	 * 
	 * */
	@RequestMapping(value = "/orderprojectlist", method = RequestMethod.GET)
	public String orderProjectlist(Model model) {
		return "/workflow/frame/orderprojectlist";
	}

	/**
	 * 海口用户信息页面
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/getuseinfo", method = RequestMethod.GET)
	public String 用户信息页面(Model model) {
		return "/workflow/frame/getuseinfo";
	}

	/*
	 * 显示默认页面
	 */
	@RequestMapping(value = "/projectlist", method = RequestMethod.GET)
	public String ShowIndex(Model model) {
		// YwLogUtil.addYwLog("访问：在办箱",
		// ConstValue.SF.YES.Value,ConstValue.LOG.SEARCH);
		return "/workflow/frame/projectlist";
	}

	/**
	 * 2015年12月19日 22:50:07 显示银行委托书页面
	 */
	@RequestMapping(value = "/banktrustbook", method = RequestMethod.GET)
	public String ShowBankTrustBook(Model model) {

		return "/workflow/frame/banktrustbook";
	}

	/**
	 * 
	 * 海口
	 * 
	 * 
	 * */
	@RequestMapping(value = "/importproject", method = RequestMethod.GET)
	public String importproject(Model model) {
		return "/workflow/frame/importproject";
	}

	/**
	 * 查看资料链接
	 * */
	@RequestMapping(value = "/dossierscan", method = RequestMethod.GET)
	public String DossierScan(Model model) {
		return "/workflow/frame/dossierscan";
	}

	@RequestMapping(value = "/projectdonelist", method = RequestMethod.GET)
	public String ShowProjectDoneIndex(Model model) {
		// YwLogUtil.addYwLog("访问:已办箱功能",
		// ConstValue.SF.YES.Value,ConstValue.LOG.SEARCH);
		return "/workflow/frame/projectdonelist";
	}
	



	/**
	 * 2015.08.12 获取已办项目 ，简略信息
	 * 
	 * @param model
	 * @return
	 */

	@RequestMapping(value = "/dossiertransf", method = RequestMethod.GET)
	public String ShowProDoneSimpleIndex(Model model) {

		return "/workflow/frame/dossiertransf";
	}

	@RequestMapping(value = "/transferproject", method = RequestMethod.GET)
	public String TransferProject(Model model) {

		return "/workflow/frame/transferproject";
	}

	@RequestMapping(value = "/abnormal", method = RequestMethod.GET)
	public String Abnormal(Model model) {
		// YwLogUtil.addYwLog("访问:异常查询功能",
		// ConstValue.SF.YES.Value,ConstValue.LOG.SEARCH);
		return "/workflow/frame/abnormal";
	}

	/**
	 * 质检模块
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/qualitytesting", method = RequestMethod.GET)
	public String qualitytesting(Model model) {

		return "/workflow/frame/qualitytesting";
	}

	@RequestMapping(value = "/invoice", method = RequestMethod.GET)
	public String invoice(Model model) {

		return "/workflow/frame/invoice";
	}

	/**
	 * 数据维护
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/datamaintain", method = RequestMethod.GET)
	public String DataMaintain(Model model) {
		return "/workflow/frame/datamaintain";
	}

	/**
	 * 流程重载
	 * 
	 * @return
	 */
	@RequestMapping(value = "/prorevice", method = RequestMethod.GET)
	public String revice(Model model) {
		return "/workflow/frame/prorevice";
	}

	/**
	 * 批量发证
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/batchcertificate", method = RequestMethod.GET)
	public String batchcertificate(Model model) {
		String qxdm = ConfigHelper.getNameByValue("XZQHDM");
		if(qxdm.contains("36"))
			return "/workflow/frame/batchcertificate_gz";
		else
			return "/workflow/frame/batchcertificate";
	}
	

	/**
	 * 批量收费
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/batchcharge", method = RequestMethod.GET)
	public String batchcharge(Model model) {
		return "/workflow/frame/batchcharge";
	}
	
	/**
	 * 
	 * 首页超期件弹出页面映射
	 */
	@RequestMapping(value = "/outtimeproject", method = RequestMethod.GET)
	public String outTimeProject(Model model) {
		return "/workflow/frame/outtimeproject";
	}

	/**
	 *
	 * 首页已网签、已完税弹出页面映射
	 */
	@RequestMapping(value = "/tax_netDoneProject", method = RequestMethod.GET)
	public String tax_netDoneProject(Model model) {
		return "/internetbusiness/tax_netDoneProject";
	}
	
	/**
	 * 督办项目列表
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/dbprojectlist", method = RequestMethod.GET)
	public String dbProjectList(Model model) {
		return "/workflow/frame/dbprojectlist";
	}
	
	/**
	 * 海口接收项目
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/acceptmater", method = RequestMethod.GET)
	public String acceptMater(Model model) {
		return "/workflow/frame/acceptmater";
	}
	
	@RequestMapping(value = "/projectlist/{protype}", method = RequestMethod.GET)
	public @ResponseBody Message getProjectListJson(@PathVariable int protype, HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		request.setCharacterEncoding("UTF-8");
		String searchvalue = request.getParameter("value");
		searchvalue = new String(searchvalue.getBytes("iso8859-1"), "utf-8");
		String currpage = request.getParameter("currpage");
		String pagesize = request.getParameter("pagesize");
		String pronames = request.getParameter("pronames");
		if (pronames != null && !pronames.equals("")) {
			pronames = new String(pronames.getBytes("iso8859-1"), "utf-8");
		}
		String sqr = request.getParameter("sqr");
		String lshstart = request.getParameter("lshstart");
		String lshend = request.getParameter("lshend");
		String actinstname = request.getParameter("actinstname");
		if (actinstname != null && !actinstname.equals("")) {
			actinstname = new String(actinstname.getBytes("iso8859-1"), "utf-8");
		}
		if (currpage == null || currpage.equals(""))
			currpage = "1";
		int page = Integer.parseInt(currpage);
		String operaStaffString = smStaff.getCurrentWorkStaffID();// 当前操作人员
		String starttime = request.getParameter("starttime");
		String endtime = request.getParameter("endtime");
		String handlepsersons = request.getParameter("staffids");
		String sfjsbl = request.getParameter("sfjsbl");
		return ProInstService.GetProjectList(operaStaffString, protype, searchvalue, page, Integer.parseInt(pagesize), pronames, sqr, lshstart, lshend, actinstname, starttime, endtime, handlepsersons, null,"",null,null,sfjsbl);
	}

	/**
	 * 根据人员查询项目列表
	 *
	 * @author DreamLi
	 * @param protype
	 *            待办在办
	 * @param staffid
	 *            职员ID
	 * @param searchvalue
	 *            查询框信息
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "/prolistsearch/{protype}", method = RequestMethod.GET)
	@ResponseBody
	public Message getProjectListJsonBySearch(@PathVariable("protype") int protype, HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		request.setCharacterEncoding("UTF-8");
		String searchvalue = RequestHelper.getParam(request,"value");
		if (searchvalue != null && !searchvalue.equals("")) {
			searchvalue.trim();
			String []searchvalueTem=searchvalue.split(" ");
			searchvalue=searchvalueTem[0];
		}
		//searchvalue = new String(searchvalue.getBytes("iso8859-1"), "utf-8");
		String currpage = RequestHelper.getParam(request,"currpage");
		String pagesize = RequestHelper.getParam(request,"pagesize");
		String pronames = RequestHelper.getParam(request,"pronames");
		
		String sqr = RequestHelper.getParam(request,"sqr");
		String lshstart = RequestHelper.getParam(request,"lshstart");
		String lshend = RequestHelper.getParam(request,"lshend");
		String actinstname = RequestHelper.getParam(request,"actinstname");

		String starttime = RequestHelper.getParam(request,"starttime");
		String endtime = RequestHelper.getParam(request,"endtime");
		String handlepsersons = RequestHelper.getParam(request,"staffids");
		String roleid = RequestHelper.getParam(request,"id");
		String descData = RequestHelper.getParam(request,"descData");
		String ywh = RequestHelper.getParam(request,"ywh");
		String sfjsbl = RequestHelper.getParam(request,"sfjsbl");
		if (currpage == null || currpage.equals(""))
			currpage = "1";
		int page = Integer.parseInt(currpage);
		if(descData!=null&&!descData.equals("")){
			descData = descData.split("-")[0]+" "+descData.split("-")[1];
		}
		String operaStaffString = smStaff.getCurrentWorkStaffID();// 当前操作人员

		return ProInstService.GetProjectList(operaStaffString, protype, searchvalue, page, Integer.parseInt(pagesize), pronames, sqr, lshstart, lshend, actinstname, starttime, endtime, handlepsersons, roleid,descData,ywh,request,sfjsbl);
	}

	/**
	 * 根据人员查询已办项目列表 不分页
	 * 
	 * @author
	 * @param staffid
	 *            职员ID
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "/dossertransfer", method = RequestMethod.GET)
	@ResponseBody
	public Message getProjectListByStaff(@PathVariable("protype") int protype) throws UnsupportedEncodingException {
		String operaStaffString = smStaff.getCurrentWorkStaffID();// 当前操作人员
		return ProInstService.GetProjectList(operaStaffString, protype);
	}

	@RequestMapping(value = "/acceptproject", method = RequestMethod.GET)
	public String AcceptProjectShow(Model model) {
		// @ModelAttribute("SmProInfo") SmProInfo smproinfo
		SmProInfo newInfo = new SmProInfo();
		model.addAttribute("SmProInfo", newInfo);
		model.addAttribute("SLR", smStaff.GetStaffName(smStaff.getCurrentWorkStaffID()));
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date dateNow = new Date();
		model.addAttribute("SLSJ", sdf.format(dateNow));
		// YwLogUtil.addYwLog("访问:业务受理功能",
		// ConstValue.SF.YES.Value,ConstValue.LOG.SEARCH);
		return "/workflow/frame/acceptproject";
	}

	@RequestMapping(value = "/delproject/{id}", method = RequestMethod.POST)
	@ResponseBody
	public SmObjInfo DleteProject(@PathVariable String id, HttpServletRequest request, HttpServletResponse response) {
		return ProInstService.deleteProInst(id);
	}

	/**
	 * @author JHX 删除项目(需要输入删除原因)
	 * @param id
	 *            reason
	 * 
	 * */
	@RequestMapping(value = "/delproject/{id}/{reasons}", method = RequestMethod.POST)
	@ResponseBody
	public SmObjInfo DleteProjectOwnerReason(@PathVariable String id, @PathVariable String reasons, HttpServletRequest request, HttpServletResponse response) {
		SmObjInfo smobjinfo = new SmObjInfo();
		smobjinfo = ProInstService.deleteProInst(id, reasons);
		return smobjinfo;
	}

	// @ModelAttribute("SmProInfo") SmProInfo smproinfo,
	@RequestMapping(value = "/acceptproject", method = RequestMethod.POST)
	@ResponseBody
	public SmObjInfo AcceptProject(SmProInfo info, BindingResult result, RedirectAttributesModelMap modelMap, SessionStatus status, HttpServletRequest request, HttpServletResponse response) {
		SmObjInfo returnsmObjInfo = null;
		/*
		 * if (info == null || StringHelper.isEmpty(info.getProInst_Name())) {
		 * return null; }
		 */
		if (result.hasErrors()) {
			// YwLogUtil.addYwLog("在办箱：受理项目出错",
			// ConstValue.SF.NO.Value,ConstValue.LOG.ADD);
			logger.error(result);
		} else {
			if (!ProInstService.HasProdef(info.getProDef_ID())) {
				returnsmObjInfo = new SmObjInfo();
				returnsmObjInfo.setID("0");
				returnsmObjInfo.setDesc("请选择受理流程");

			} else {
				SmObjInfo smObjInfo = new SmObjInfo();
				smObjInfo.setID(smStaff.getCurrentWorkStaffID());
				smObjInfo.setName(smStaff.GetStaffName(smObjInfo.getID()));
				List<SmObjInfo> staffList = new ArrayList<SmObjInfo>();
				staffList.add(smObjInfo);
				info.setAcceptor(smObjInfo.getName());
				returnsmObjInfo = ProInstService.Accept(info, staffList);
				// YwLogUtil.addYwLog("在办箱：受理项目：项目名称：" +info.getProInst_Name()
				// +",流程名称：" + info.getProDef_Name(),
				// ConstValue.SF.YES.Value,ConstValue.LOG.ADD);
				// modelMap.addFlashAttribute("resultMsg", "受理成功");
			}
		}
		return returnsmObjInfo;
	}

	/**
	 * @author Duff 注销项目
	 * @param id
	 *            reason
	 * 
	 * */
	@RequestMapping(value = "/zxproject/{actinst}", method = RequestMethod.POST)
	@ResponseBody
	public boolean ZxProject(@PathVariable String actinst, HttpServletRequest request, HttpServletResponse response) {
		String msg = request.getParameter("msg");
		return ProInstService.ZxProject(actinst, msg);
	}

	// 查看详细页面
	@RequestMapping(value = "/projectdetail/{actinstid}", method = RequestMethod.GET)
	public String ShowDatailForActinst(@PathVariable String actinstid, HttpServletRequest request, HttpServletResponse response) {
		// 判断是否有权限查看此模块
		/*
		 * SFTPEx ex=new SFTPEx(); ex.timerrunner();
		 */
		return "/workflow/frame/projectdetail";
	}
	
	
	

	// 查看详细页面
	@RequestMapping(value = "/projectdetail/show/{actinstid}", method = RequestMethod.GET)
	@ResponseBody
	public SmProInfo ShowDatail(@PathVariable String actinstid, HttpServletRequest request, HttpServletResponse response) {
		// 判断是否有权限查看此模块
		return ProInstService.GetProjectInfo(actinstid);

	}

	/**
	 * 获取项目信息
	 * 
	 * **/
	@RequestMapping(value = "/getprojectinfo/{filenumber}", method = RequestMethod.GET)
	@ResponseBody
	public SmProInfo GetProjectInfoByFileNumber(@PathVariable String filenumber, HttpServletRequest request, HttpServletResponse response) {
		// 判断是否有权限查看此模块
		return ProInstService.GetProjectInfoByFileNumber(filenumber);
	}

	/**
	 * 获取流程办件过程
	 * 
	 * */
	@RequestMapping(value = "/getproinstprocess/{actinstid}", method = RequestMethod.GET)
	@ResponseBody
	public List<Wfi_ActInst> GetProInstProcess(@PathVariable String actinstid, HttpServletRequest request, HttpServletResponse response) {
		List<Wfi_ActInst> actinsts = ProInstService.GetProInstProcess(actinstid);
		// 检查是否有转办，有转办则添进list
		return ProInstService.checkTurnProject(actinsts);
	}

	@RequestMapping(value = "/publicsearch/{filenumber}", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> publicsearch(@PathVariable String filenumber, HttpServletRequest request, HttpServletResponse response) {
		return ProInstService.publicSearch(filenumber);
	}

	@RequestMapping(value = "/publicsearchex/{filenumber}", method = RequestMethod.GET)
	@ResponseBody
	public List<Wfi_ActInst> publicsearchex(@PathVariable String filenumber, HttpServletRequest request, HttpServletResponse response) {
		return ProInstService.publicSearchEx(filenumber);
	}

	@RequestMapping(value = "/selectstaff/{actinst}", method = RequestMethod.GET)
	public String SelectStaff(@PathVariable String actinst, HttpServletRequest request, HttpServletResponse response) {
		Wfd_Actdef currrentActDef = smactinst.GetActDef(actinst);
		JSONObject obj = JSONObject.fromObject(currrentActDef.getCustomeParamsSet());
		if (!obj.isNullObject()) {
			if (obj.containsKey("SENDMESSAGE")) {
				String sendmessage = obj.getString("SENDMESSAGE");
				if (sendmessage != null && !sendmessage.equals("")) {
					request.setAttribute("issend", "true");
				}
			}
		}
		return "/workflow/frame/selectstaff";
	}
	
	/**
	 * 批量转出选择人员列表
	 * @date   2016年11月15日 下午8:46:55
	 * @author JHX
	 * @param actinst
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/selectstaff/batch/passover", method = RequestMethod.GET)
	public String selectBatchPassOverStaff(HttpServletRequest request, HttpServletResponse response) {
		return "/workflow/frame/selectpassoverstaff";
	}

	/**
	 * 转办人员页面地址映射
	 * */
	@RequestMapping(value = "/selectturnstaff/{actinst}", method = RequestMethod.GET)
	public String SelectTurnStaff(@PathVariable String actinst) {
		return "/workflow/frame/selectturnstaff";
	}

	@RequestMapping(value = "/back/selectstaff/{actinst}", method = RequestMethod.GET)
	public String SelectBackStaff(@PathVariable String actinst) {
		return "/workflow/frame/selectbackstaff";
	}

	/*
	 * 完成新版资料收件功能
	 */
	@RequestMapping(value = "/workflowmater", method = RequestMethod.GET)
	public String FileManagerEx(HttpServletRequest request, HttpServletResponse response) {
		return "/workflow/frame/workflowmater";

	}

	/**
	 * 根据活动定义ID获取 流程组件表单数组（标题头） add by wjz 2015.5.20 23:13
	 * 
	 * @param actdef_id
	 *            活动定义ID
	 * @param request
	 * @param response
	 * @return JSON
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/projectdetail/actinstid/{actinst_id}", method = RequestMethod.GET)
	@ResponseBody
	public List<Map> getData(@PathVariable("actinst_id") String actinst_id, HttpServletRequest request, HttpServletResponse response) {
		return SmSysMod.getProjectDetail(actinst_id,request);

	}

	/**
	 * 根据 活动实例ACTINST_ID 获取流程资料清单列表 add by wjz 2015.5.20 23:13
	 * 
	 * @param actinst_id
	 *            活动实例ID
	 * @param request
	 * @param response
	 * @return JSON
	 */
	// @RequestMapping(value = "/wfipromater/actinstid/{actinst_id}", method =
	// RequestMethod.GET)
	// public void getWfdPromater(@PathVariable("actinst_id") String actinst_id,
	// HttpServletRequest request, HttpServletResponse response) {
	// response.setCharacterEncoding("UTF-8");
	// StringBuffer json = SmProMater.getProMate(actinst_id);
	// try {
	// response.getOutputStream().write(json.toString().getBytes("UTF-8"));
	// } catch (UnsupportedEncodingException e) {
	// e.printStackTrace();
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// // return response.json.toString();
	// }
	@RequestMapping(value = "/wfipromater/actinstid/{actinst_id}", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getWfdPromater(@PathVariable("actinst_id") String actinst_id, HttpServletRequest request, HttpServletResponse response) {
		return SmProMater.getProMateEx(actinst_id);

		// return response.json.toString();
	}

	//

	/**
	 * 上传图片 add by wjz
	 * 
	 * @param file
	 * @param request
	 * @param response
	 * @return 
	 * @throws IOException
	 */
	@RequestMapping(value = "/wfipromater/updateimage", method = RequestMethod.POST)
	public  void upload(@RequestParam("file") MultipartFile file, HttpServletRequest request, HttpServletResponse response, Object command) throws IOException {
		if(UploadDiskHelper.isFull()==true) {
			response.getWriter().write("-1");
			return ;
		}
		Wfi_MaterData materData = new Wfi_MaterData();
		response.setContentType("UTF-8");
		String imgName  = file.getOriginalFilename();
		String idString = request.getParameter("_materilinst_id");
		String thumb = request.getParameter("thumb");
		materData.setMaterialdata_Id(Common.CreatUUID());
		//添加PDF文件后缀名大写转换 liangqin
		String checkImgName = "";
		if(imgName.contains("PDF")){
			String filename1 = imgName.substring(0,imgName.lastIndexOf("."));
			String filename2 = imgName.substring(imgName.lastIndexOf("."),imgName.length()).toLowerCase();
			checkImgName = filename1 + filename2;
		}else{
			checkImgName = imgName;
		}
		materData.setFile_Name(checkImgName);
		materData.setMaterilinst_Id(idString);
		//TODO:获取名字中的数字，名字的前或者后的数字如果有数字index使用数字，否则使用time来实现，文件名称规则xx\\d+.*
		String regex = "\\d+$";
		Pattern pat = Pattern.compile(regex); 
	    String index="";
		if(!StringHelper.isEmpty(imgName)){
			 String filename = imgName.substring(0,imgName.lastIndexOf("."));
			 Matcher matcher = pat.matcher(filename); 
			 if (matcher.find()) {
				 index = filename.substring(matcher.start());
			 }
		}
		materData.setFile_Index(new Date().getTime());
		if(!StringHelper.isEmpty(index)){
			materData.setFile_Index(Long.parseLong(index));
		}
		String staff_id="";
		String staff_name="";
		User user=smStaff.getCurrentWorkStaff();
		if(user!=null){
			staff_id=user.getId();
			staff_name=user.getLoginName();
		}else{
			staff_id=request.getParameter("staff_id");
			staff_name=request.getParameter("staff_name");
		}
		materData.setUpload_Id(staff_id);
		materData.setUpload_Name(staff_id);
		materData.setThumb(thumb);
		// materData.setMaterialdata_Id(Common.CreatUUID());
		ProMaterService.ModifyMaterialPath(idString, materData.getMaterialdata_Id());
		// YwLogUtil.addYwLog("上传收件资料",
		// ConstValue.SF.YES.Value,ConstValue.LOG.ADD);
		SmProMater.updateLoadImage(file, materData, request, response, false);
//		}
	}

	@RequestMapping(value = "/wfipromater/windowservice/uploadimage", method = RequestMethod.POST)
	public void windowServiceupload(@RequestParam("file") MultipartFile file, HttpServletRequest request, HttpServletResponse response, Object command) throws IOException {
		Wfi_MaterData materData = new Wfi_MaterData();
		String idString = request.getParameter("_materilinst_id");
		String thumb = request.getParameter("thumb");
		materData.setMaterialdata_Id(Common.CreatUUID());
		materData.setFile_Name(file.getOriginalFilename());
		materData.setMaterilinst_Id(idString);
		materData.setFile_Index(new Date().getTime());
		materData.setUpload_Id(request.getParameter("Staff_Id"));
		materData.setUpload_Name(request.getParameter("Staff_Name"));
		materData.setThumb(thumb);
		// materData.setMaterialdata_Id(Common.CreatUUID());
		ProMaterService.ModifyMaterialPath(idString, materData.getMaterialdata_Id());
		SmProMater.updateLoadImage(file, materData, request, response, false);
	}

	@RequestMapping(value = "/wfipromater/base64", method = RequestMethod.POST)
	public void uploadBase64(@RequestParam(value = "Image", required = false) MultipartFile[] file,@RequestParam("id") String id,@RequestParam("desc") String desc,@RequestParam("imageCount") Integer imageCount,HttpServletRequest request, HttpServletResponse response) throws IOException {

		String image = request.getParameter("data");
		String type = request.getParameter("type");
		//BASE64DecodedMultipartFile file = new BASE64DecodedMultipartFile(null);
		
		uploadGPY(null,request,response,null);
		
	}

	@RequestMapping(value = "/wfipromater/gaopaiyiimage", method = RequestMethod.POST)
	public void uploadGPY(@RequestParam(value = "image", required = false) MultipartFile file, HttpServletRequest request, HttpServletResponse response, Object command) throws IOException {
		Wfi_MaterData materData = new Wfi_MaterData();

		String idString = request.getParameter("id");
		String desc = request.getParameter("desc");
		String coverid = request.getParameter("coverid");
		String Staff_ID = "";
		String Staff_Name = "";
		if (desc != null && !desc.equals("")) {
			String[] descs = desc.split("&");
			Staff_ID = descs[0];
			Staff_Name = descs[1];
		}
		if (coverid != null && !coverid.equals("")) {
			materData = SmProMater.getMaterData(coverid);

		} else {
			// String thumb = request.getParameter("desc");
			materData.setMaterialdata_Id(Common.CreatUUID());
			materData.setMaterilinst_Id(idString);
			materData.setFile_Index(new Date().getTime());
			materData.setUpload_Id(Staff_ID);
			materData.setUpload_Name(Staff_Name);
		}
		// materData.setThumb(thumb);
		// materData.setMaterialdata_Id(Common.CreatUUID());
		ProMaterService.ModifyMaterialPath(idString, materData.getMaterialdata_Id());
		SmProMater.updateLoadImage(file, materData, request, response, true);

	}

	@RequestMapping(value = "/wfipromater/gaopaiyiimage_tl/{folderid}/{staffid}", method = RequestMethod.POST)
	@ResponseBody
	public void upload2(@PathVariable("folderid") String folderid, @PathVariable("staffid") String staffid,
			@RequestParam(value = "FileData", required = false)  MultipartFile file,
			HttpServletRequest request, HttpServletResponse response) throws IOException{		
		response.setContentType("text/html;charset=UTF-8");
		Wfi_MaterData materData = new Wfi_MaterData();
		String[] desc= staffid.split(",");
		String staff="";
		String coverid="";
		if(desc.length>1){
			staff=desc[0];
			coverid=desc[1];
		}else{
			staff=staffid;
		}
		if (coverid != null && !coverid.equals("")) {
			materData = SmProMater.getMaterData(coverid);

		} else {
			// String thumb = request.getParameter("desc");
			materData.setMaterialdata_Id(Common.CreatUUID());
			materData.setMaterilinst_Id(folderid);
			materData.setFile_Index(new Date().getTime());
			materData.setUpload_Id(staffid);
			materData.setUpload_Name(smStaff.GetStaffName(staffid));
		}
		// materData.setThumb(thumb);
		// materData.setMaterialdata_Id(Common.CreatUUID());
		ProMaterService.ModifyMaterialPath(folderid, materData.getMaterialdata_Id());
		SmProMater.updateLoadImage(file, materData, request, response, true);

	}

	@RequestMapping(value = "/wfipromater/scanimage", method = RequestMethod.POST)
	public void scanimage(@RequestParam(value = "Image", required = false) MultipartFile[] files,@RequestParam("id") String id,@RequestParam("desc") String desc,@RequestParam("imageCount") Integer imageCount,HttpServletRequest request, HttpServletResponse response) throws IOException {
		Wfi_MaterData materData = new Wfi_MaterData();
		String cover = request.getParameter("cover");
		cover = cover==null?cover:cover.trim();
		String coverid = request.getParameter("coverid");
		coverid = coverid==null?coverid:coverid.trim();
		id = id==null?id:id.trim();
		String Staff_ID = "";
		String Staff_Name = "";
		if (desc != null && !desc.equals("")) {
			String[] descs = desc.split("&");
			Staff_ID = descs[0];
			Staff_Name = smStaff.GetStaffName(Staff_ID.trim());
		}
		// String thumb = request.getParameter("desc");
		if (coverid != null && !coverid.equals("")) {
			materData = SmProMater.getMaterData(coverid);
			SmProMater.updateLoadImage(files[0], materData, request, response, true);
		} else {
			for (MultipartFile file : files) {
				materData.setMaterialdata_Id(Common.CreatUUID());
				materData.setMaterilinst_Id(id);
				materData.setFile_Index(new Date().getTime());
				materData.setUpload_Id(Staff_ID);
				materData.setUpload_Name(Staff_Name);
				SmProMater.updateLoadImage(file, materData, request, response, true);
			}
			
		}
		ProMaterService.ModifyMaterialPath(id, materData.getMaterialdata_Id());
	}

	// 银行委托书扫描上传
	@RequestMapping(value = "/wfipromater/scanbanktrustbook/{trustbookid}", method = RequestMethod.POST)
	public void scanbanktrustbook(@PathVariable("trustbookid") String trustbookid, @RequestParam("Image") MultipartFile file, HttpServletRequest request, HttpServletResponse response) throws IOException {
		// String yhmcdecode= java.net.URLDecoder.decode(yhmc, "UTF-8");
		Bank_TrustbookPage bank_TrustbookPage = new Bank_TrustbookPage();
		bank_TrustbookPage.setTrustbookdata_id(Common.CreatUUID());
		bank_TrustbookPage.setTrustbookpage_Index(Short.parseShort("1"));
		ProMaterService.ModifyBankTrustbookPath(trustbookid, bank_TrustbookPage.getTrustbookdata_id());
		SmProMater.updateTrustBookImage(trustbookid, file, bank_TrustbookPage, request, response, true);

	}

	@RequestMapping(value = "/wfipromater/foldercount/{folderid}", method = RequestMethod.GET)
	@ResponseBody
	public long GetFolderCount(@PathVariable String folderid, HttpServletRequest request, HttpServletResponse response) {
		if (folderid != null && !"".equals(folderid)) {
			return ProMaterService.GetFileCount(folderid);
		} else {
			return 0;
		}

	}

	@RequestMapping(value = "/wfipromater/photoupload", method = RequestMethod.POST)
	@ResponseBody
	public String photoupload(HttpServletRequest request, HttpServletResponse response) {
		Wfi_MaterData materData = new Wfi_MaterData();
		String idString = request.getParameter("_materilinst_id");
		String webcam = request.getParameter("mydata");
		materData.setMaterialdata_Id(Common.CreatUUID());
		
		//判断文件大小
				BASE64Decoder decoder = new BASE64Decoder();
				byte[] b = null;
				 String webcamb = webcam.substring(23);
					try {
						b = decoder.decodeBuffer(webcamb);
						for (int i = 0; i < b.length; ++i) {
							if (b[i] < 0) {// 调整异常数据
								b[i] += 256;
							}
						}
//						int messagenum=ProInstService.uploadDisk(b.length);
//						response.setContentType("UTF-8");
//						response.getWriter().write(messagenum+"");
//						if(messagenum==3) {//空间不足,且更换盘符失败
//							return materData.getMaterialdata_Id();
//						}
					} catch (Exception e) {
						e.printStackTrace();
						return materData.getMaterialdata_Id();
					}

		materData.setFile_Name("人像采集.jpg");
		materData.setMaterilinst_Id(idString);
		materData.setFile_Index(Short.parseShort("0"));
		materData.setUpload_Id(smStaff.getCurrentWorkStaffID());
		materData.setUpload_Name(smStaff.GetStaffName(materData.getUpload_Id()));
		materData.setThumb(webcam);
		ProMaterService.ModifyMaterialPath(idString, materData.getMaterialdata_Id());
		SmProMater.uploadPhoto(materData);
		return materData.getMaterialdata_Id();

	}

	/**
	 * 批量上传
	 */
	@RequestMapping(value = "/wfipromater/uploadmore", method = RequestMethod.POST)
	@ResponseBody
	public Wfi_MaterData uploadMore(@RequestParam("file") MultipartFile file, HttpServletRequest request, HttpServletResponse response, Object command) {
		String thumb = request.getParameter("thumb");
		String file_number = request.getParameter("file_number");
		Wfi_MaterData materData = new Wfi_MaterData();
		materData.setMaterialdata_Id(Common.CreatUUID());
		materData.setFile_Index(Short.parseShort("0"));
		materData.setUpload_Id(smStaff.getCurrentWorkStaffID());
		materData.setUpload_Name(smStaff.GetStaffName(materData.getUpload_Id()));
		materData.setThumb(thumb);
		materData.setFile_Number(file_number);
		materData.setFile_Name(file.getOriginalFilename());
		return SmProMater.uploadMore(file, materData, request, response);

	}

	/**
	 * 关于获取未分类的文件
	 * */
	@RequestMapping(value = "/wfipromater/nogroup/{filenumber}", method = RequestMethod.GET)
	@ResponseBody
	public List<Wfi_MaterData> getNoGroupMetar(@PathVariable String filenumber, HttpServletRequest request, HttpServletResponse response) {
		if (filenumber != null && !filenumber.equals("")) {
			return ProMaterService.getNoGroupMetar(filenumber);
		} else {
			return null;
		}
	}

	/**
	 * 上传分组
	 * */
	@RequestMapping(value = "/wfipromater/uploadgroup", method = RequestMethod.POST)
	@ResponseBody
	public boolean uploadGroup(HttpServletRequest request, HttpServletResponse response) {
		String metardata_id = request.getParameter("metardataid");
		String metarinst_id = request.getParameter("metarinstid");
		ProMaterService.ModifyMaterialPath(metarinst_id, metardata_id);
		return SmProMater.uploadgroup(metardata_id, metarinst_id);

	}

	@RequestMapping(value = "/wfipromater/deleteimage", method = RequestMethod.GET)
	@ResponseBody
	public boolean deleteImage(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String materilinst_id = request.getParameter("materilinst_id");
		String materialdata_id = request.getParameter("materialdata_id");
		return SmProMater.deleteImage(request, materilinst_id, materialdata_id);
	}

	/**
	 * 删除文件通过文件id
	 * **/

	@RequestMapping(value = "/wfipromater/deletefile", method = RequestMethod.GET)
	@ResponseBody
	public boolean deletefile(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String materilinst_ids = request.getParameter("materilinst_ids");
		return SmProMater.deleteFile(request, materilinst_ids);
	}

	/**
	 * 删除文件通过文件id;如果文件夹中存在他人上传的文件则不能删除；
	 * **/
	@RequestMapping(value = "/wfipromater/deletefilebyowner", method = RequestMethod.GET)
	@ResponseBody
	public SmObjInfo deletefile2(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String materilinst_ids = request.getParameter("materilinst_ids");
		String delfile = request.getParameter("delfile");
		return SmProMater.deleteFile2(request, materilinst_ids,delfile);
	}

	/**
	 * 请空文件夹，只能清空自己上传的内容
	 * 
	 * @author JHX
	 * @date 2016-07-29
	 * */
	@RequestMapping(value = "/wfipromater/emptyfolder", method = RequestMethod.GET)
	@ResponseBody
	public SmObjInfo emptyfolder(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String materilinst_ids = request.getParameter("materilinst_ids");
		String delfile = request.getParameter("delfile");
		return SmProMater.emptyfolder(request, materilinst_ids ,delfile);
	}

	/**
	 * 在浏览器中显现图片 add by wjz
	 * 
	 * @param file
	 * @param request
	 * @param response
	 * @throws IOException
	 * 
	 */
	@RequestMapping(value = "/wfipromater/getimage/materilinstid/{materilinst_id}")
	public void showImage(@PathVariable("materilinst_id") String materilinst_id, HttpServletRequest request, HttpServletResponse response) throws IOException {
		SmProMater.showImage(materilinst_id, request, response);
	}

	/**
	 * 图片文件下载 add by wjz
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ParseException 
	 */
	@RequestMapping(value = "/wfipromater/imagedownload/{materdataid}", method = RequestMethod.GET)
	public void download(@PathVariable String materdataid, HttpServletRequest request, HttpServletResponse response) throws IOException {
		// response.setDateHeader("Expires", 0);
		// response.setHeader("Cache-Control", "no-cache");
		// response.setHeader("Prama", "no-cache");
		OutputStream os = response.getOutputStream();
		request.setCharacterEncoding("UTF-8");
		String fileName = request.getParameter("fileName");
		fileName = "temp";
		Wfi_MaterData data = ProMaterService.getMaterData(materdataid);
		fileName = java.net.URLEncoder.encode(new String(fileName.getBytes("ISO-8859-1"), "UTF-8"), "UTF-8");
		try {
			response.reset();

			if (data.getFile_Name() != null && data.getFile_Name().toLowerCase().indexOf(".pdf") > 0) {
				response.setHeader("Content-Disposition", "inline; filename=" + data.getFile_Name());
				response.setContentType("application/pdf; charset=UTF-8");
			} else {
				response.setHeader("Content-Disposition", "attachment; filename=" + data.getFile_Name());
				response.setContentType("image/jpeg; charset=UTF-8");
			}

			byte[] file = FileUpload.getFile(data.getDisc()+data.getPath(), data.getFile_Path());
			if (file != null) {
				os.write(file);
				os.flush();
			} else {

			}

		} finally {
			if (os != null) {
				os.close();
			}
		}
	}
	/**
	 * 银行委托书分页预览：获取当前页的银行委托书 卜晓波 2016年9月22日 11:45:46
	 * */
	@RequestMapping(value = "/wfipromater/banktrustbookimagedownload/{trustbookpagedata_id}", method = RequestMethod.GET)
	public void banktrustbookdownload(@PathVariable String trustbookpagedata_id, HttpServletRequest request, HttpServletResponse response) throws IOException {
		OutputStream os = response.getOutputStream();
		request.setCharacterEncoding("UTF-8");
		Bank_TrustbookPage data = ProMaterService.getBank_TrustbookPage(trustbookpagedata_id);
		try {
			response.reset();
			response.setHeader("Content-Disposition", "attachment; filename=" + data.getTrustbookpage_Path());
			response.setContentType("image/jpeg; charset=UTF-8");
			byte[] file = FileUpload.getBanktrustbookFile(data.getTrustbookpage_Id(), data.getTrustbookpage_Path());
			if (file != null) {
				os.write(file);
				os.flush();
			} else {

			}
		} finally {
			if (os != null) {
				os.close();
			}
		}
	}

	/**
	 * 获取员工的待办件，在办件，已办件
	 * 
	 * @author yiziyu
	 * @param
	 * */
	@RequestMapping(value = "/getprojecttypeandcount", method = RequestMethod.GET)
	@ResponseBody
	public List<SmObjInfo> GetProjectTypeAndCountByStaffID() {
		String staffid = smStaff.getCurrentWorkStaffID();
		return ProInstService.GetProjectTypeAndCountByStaffID(staffid);
	}

	/**
	 * 流程实例的统计
	 * */
	@RequestMapping(value = "/project/static", method = RequestMethod.GET)
	@ResponseBody
	public List<SmObjInfo> ProjectStatic() {
		String staffid = smStaff.getCurrentWorkStaffID();
		return ProInstService.ProjectStatic(staffid);
	}

	/**
	 * 工作流图形的展示
	 * */
	@RequestMapping(value = "/workflow", method = RequestMethod.GET)
	public String workflowShow(Model model) {
		return "/workflow/frame/workflow";
	}

	/** 资料收取 */
	@RequestMapping(value = "/wfipromater/acceptmateral/{proinst_id}", method = RequestMethod.POST)
	@ResponseBody
	public SmObjInfo AcceptMateral(@PathVariable("proinst_id") String proinst_id, HttpServletRequest request, HttpServletResponse response) {
		SmObjInfo info = null;
		String materalidString = request.getParameter("materalid");
		String count = request.getParameter("count");
		String checked = request.getParameter("status");
		if (checked.equals("true") || checked.equals("false")) {
			info = ProMaterService.ModifyMaterialInfo(proinst_id, materalidString, count, Boolean.parseBoolean(checked));
		}
		return info;
	}

	/** 回执打印 */
	@RequestMapping(value = "/print", method = RequestMethod.GET)
	public String Print() {
		return "/workflow/frame/print";
	}

	/** 附件预览 */
	@RequestMapping(value = "/imgview", method = RequestMethod.GET)
	public String imgView(Model model) {

		return "/workflow/frame/imgview";
	}

	/** 附件预览 */
	@RequestMapping(value = "/banktrustbookimgview", method = RequestMethod.GET)
	public String banktrustbookimgView(Model model) {

		return "/workflow/frame/banktrustbookimgview";
	}

	/**
	 * 项目查询
	 * */
	@RequestMapping(value = "/projectsearch", method = RequestMethod.GET)
	public String projectSearch() {
		// YwLogUtil.addYwLog("访问:全局查询功能",
		// ConstValue.SF.YES.Value,ConstValue.LOG.SEARCH);
		return "/workflow/frame/projectsearch";

	}

	@RequestMapping(value = "/media", method = RequestMethod.GET)
	public String Media() {
		return "/workflow/frame/media";
	}

	@RequestMapping(value = "/mediajl", method = RequestMethod.GET)
	public String MediaJL() {
		return "/workflow/frame/mediaJL";
	}
	
	@RequestMapping(value = "/mediaqzh", method = RequestMethod.GET)
	public String Mediaqzh() {
		return "/workflow/frame/certquerymedia";
	}

	@RequestMapping(value = "/webcam", method = RequestMethod.GET)
	public String webcam() {
		return "/workflow/frame/webcam";
	}

	/**
	 * 为流程实例添加一份收件资料
	 * */
	@RequestMapping(value = "/wfipromater/addpromater/{proinstid}", method = RequestMethod.POST)
	@ResponseBody
	public Wfi_ProMater AddProMater(@PathVariable String proinstid, HttpServletRequest request, HttpServletResponse response) {
		String filename = request.getParameter("filename");
		String filecount = request.getParameter("filecount");
		String filetype = request.getParameter("filetype");
		String index = request.getParameter("index");
		String materials = request.getParameter("materials");
		if (filename != null && filecount != null && filetype != null) {
			// YwLogUtil.addYwLog("流程实例添加一份收件资料",
			// ConstValue.SF.YES.Value,ConstValue.LOG.ADD);
			return ProMaterService.AddProMaterFile(filename, filecount, filetype, index, materials, proinstid);
		} else {
			return null;
		}

	}

	@RequestMapping(value = "/wfipromater/delpromaterdata", method = RequestMethod.POST)
	@ResponseBody
	public Boolean DelProMateril(HttpServletRequest request, HttpServletResponse response) {
		String idString = request.getParameter("id");
		if (idString != null && !idString.equals("")) {
			return ProMaterService.DelProMateril(idString);
		} else {
			return false;
		}
	}

	/**
	 * @author JHX 预览时候删除文件需要判断，是否本人上传
	 * */
	@RequestMapping(value = "/wfipromater/delpromaterdata/byowner", method = RequestMethod.POST)
	@ResponseBody
	public SmObjInfo DelProMaterilByOwner(HttpServletRequest request, HttpServletResponse response) {
		SmObjInfo smobjeinfo = new SmObjInfo();
		String idString = request.getParameter("id");
		String delfile = request.getParameter("delfile");
		String file_number = request.getParameter("file_number");
		if (idString != null && !idString.equals("")) {
			smobjeinfo = ProMaterService.DelProMateril2(idString,delfile,file_number);
		} else {
			smobjeinfo.setDesc("删除失败,资料编号为空");
			smobjeinfo.setConfirm("Error");
		}
		return smobjeinfo;
	}

	@RequestMapping(value = "/wfipromater/delallpromaterdata", method = RequestMethod.POST)
	@ResponseBody
	public Boolean DelAllProMateril(HttpServletRequest request, HttpServletResponse response) {
		String idString = request.getParameter("id");
		if (idString != null && !idString.equals("")) {
			return ProMaterService.DelAllProMateril(idString);
		} else {
			return false;
		}
	}

	// 获取单个文件夹下的文件
	@RequestMapping(value = "/wfipromater/getpromaterdata/{materlinstid}", method = RequestMethod.GET)
	@ResponseBody
	public List<Wfi_MaterData> getMaterDataByMaterlinstid(@PathVariable String materlinstid, HttpServletRequest request, HttpServletResponse response) {
		return ProMaterService.getmaterData(materlinstid);
	}

	// 更新收件数量
	@RequestMapping(value = "/wfipromater/updatepromatercount/{materlinstid}", method = RequestMethod.POST)
	@ResponseBody
	public boolean updatepromatercount(@PathVariable String materlinstid, HttpServletRequest request, HttpServletResponse response) {
		String countString = request.getParameter("count");
		return ProMaterService.UpdatePromaterCount(materlinstid, countString);
	}
	
	//更新收件名称
	@RequestMapping(value = "/wfipromater/updatepromatername/{materlinstid}", method = RequestMethod.POST)
	@ResponseBody
	public boolean updatepromatername(@PathVariable String materlinstid, HttpServletRequest request, HttpServletResponse response) {
		String materlName = request.getParameter("name");
		ProMaterService.UpdatePromaterName(materlinstid, materlName);
		return true;
	}
	
	@RequestMapping(value = "/wfipromater/updatepromatertype/{materlinstid}", method = RequestMethod.POST)
	@ResponseBody
	public boolean updatePromatarType(@PathVariable String materlinstid, HttpServletRequest request, HttpServletResponse response) {
		String typeString = request.getParameter("type");
		return ProMaterService.UpdatePromaterType(materlinstid, typeString);
	}

	/**
	 * 关于档案的移交(按照个人和环节)
	 * */
	@RequestMapping(value = "/dossier/transf/{page}/{pagesize}")
	@ResponseBody
	public Message GetDossier(@PathVariable String page, @PathVariable String pagesize, HttpServletRequest request, HttpServletResponse response) {
		String id = smStaff.getCurrentWorkStaffID();
		String havadone = request.getParameter("havedone");
		String actdefName = request.getParameter("actdefname");
		String type = request.getParameter("type");
		if (type.equals("2")) {// 按照部门移交档案
			id = smStaff.GetStaffDeptID(id);
		}
		Boolean isLoadHavaDoneBoolean = false;
		if (havadone != null && !"".equals(havadone)) {
			isLoadHavaDoneBoolean = Boolean.parseBoolean(havadone);
		}
		if (pagesize != null && !pagesize.equals("") && page != null && !page.equals("")) {
			return ProInstService.getDossierList(Integer.parseInt(type), id, Integer.parseInt(page), Integer.parseInt(pagesize), actdefName, isLoadHavaDoneBoolean);
		} else {
			return null;
		}
	}

	/*
	 * 标记已经移交的档案
	 */
	@RequestMapping(value = "/dossier/sign/transfer", method = RequestMethod.POST)
	@ResponseBody
	public SmObjInfo signDossierTransfer(HttpServletRequest request, HttpServletResponse response) {
		String proinsts = request.getParameter("proinstids");
		String actdefname = request.getParameter("actdefname");
		if (proinsts != null && !"".equals(proinsts)) {
			return ProInstService.SignDossierTransfer(proinsts, actdefname);
		} else {
			return null;
		}

	}
	
	/**
	 * 获取及时办结网签，税务等时间
	 * @param request
	 * @param response
	 * @param query
	 * @return
	 */
	@RequestMapping(value = "/projectjsbjinfo", method = RequestMethod.POST)
	@ResponseBody
	public Message getprojectjsbjList(HttpServletRequest request, HttpServletResponse response,QueryCriteria query){
		String pageindex = request.getParameter("currpage");
		String pagesize = request.getParameter("pagesize");
		String value = request.getParameter("value");
		
		if (!StringHelper.isEmpty(pageindex)) {
			query.setCurrentPageIndex(pageindex);
		}
		if (!StringHelper.isEmpty(pagesize)) {
			query.setPageSize(pagesize);
		}
		
		if (query.getCurrentPageIndex() != null) {
			return ProInstService.getProjectjsbjList(query);
		} else {
			return null;
		}
		
	}
	
	

	@RequestMapping(value = "/allproject", method = RequestMethod.POST)
	@ResponseBody
	public Message getAllProjectList(HttpServletRequest request, HttpServletResponse response,QueryCriteria query) {
		String keyString = request.getParameter("value");
		String statuString = request.getParameter("status");
		String startString = request.getParameter("start");
		String endString = request.getParameter("end");
		String pageindex = request.getParameter("currpage");
		String pagesize = request.getParameter("pagesize");
		String actdefname = request.getParameter("actdefname");
		String prodefname = request.getParameter("prodefname");
		String staffanme = request.getParameter("staffanme");
		String sqr = request.getParameter("sqr");
		String urgency = request.getParameter("urgency");// 紧急程度
		String outtime = request.getParameter("outtime");// 超期
		String passback = request.getParameter("passback");// 驳回
		String proStatus = request.getParameter("prostatus");// 即将超期，已超期
		
		if (!StringHelper.isEmpty(pageindex)) {
			query.setCurrentPageIndex(pageindex);
		}
		if (!StringHelper.isEmpty(pagesize)) {
			query.setPageSize(pagesize);
		}
		if (!StringHelper.isEmpty(statuString)) {
			query.setStatus(statuString);
		}
		if (!StringHelper.isEmpty(endString)) {
			query.setActinstEnd(endString);
		}
		if (!StringHelper.isEmpty(staffanme)) {
			query.setStaff(staffanme);
		}
		if (!StringHelper.isEmpty(startString)) {
			query.setActinstStart(startString);
		}
		
		if (query.getCurrentPageIndex() != null) {
			return ProInstService.getAllProjectList(query);
					/*keyString, statuString, 
					startString, endString, Integer.parseInt(pageindex),
					Integer.parseInt(pagesize), actdefname, prodefname,
					staffanme, sqr, urgency, outtime, passback, proStatus,request);*/
		} else {
			return null;
		}

	}

	@RequestMapping(value = "/hasacceptmater/{file_number}", method = RequestMethod.GET)
	@ResponseBody
	public List<Tree> GetMaterDataTree(@PathVariable String file_number, HttpServletRequest request, HttpServletResponse response) {
		if (file_number != null && !file_number.equals("")) {
			String clear = request.getParameter("clear");
			return ProMaterService.GetMaterDataTree(file_number, clear);
		} else {
			return null;
		}
	}

	@RequestMapping(value = "/allmater/{file_number}", method = RequestMethod.GET)
	@ResponseBody
	public List<Tree> GetAllMaterDataTree(@PathVariable String file_number, HttpServletRequest request, HttpServletResponse response) {
		if (file_number != null && !file_number.equals("")) {
			String type = request.getParameter("type");
			if (type == null || "".equals(type)) {
				return ProMaterService.GetAllMaterFolderTree(file_number);
			} else {
				String id = request.getParameter("id");
				return ProMaterService.GetAllMaterDataTree(id);
			}

		} else {
			return null;
		}
	}

	@RequestMapping(value = "/banktrustbookallmater", method = RequestMethod.GET)
	@ResponseBody
	public List<Tree> GetBanktrustbookAllMaterDataTree(HttpServletRequest request, HttpServletResponse response) {
		String id = request.getParameter("id");
		return ProMaterService.GetBanktrustbookAllMaterDataTree(id);
	}

	// 修改项目名称
	@RequestMapping(value = "/modify/projectname/{proinstid}", method = RequestMethod.POST)
	@ResponseBody
	public boolean ModiftyProjectName(@PathVariable String proinstid, HttpServletRequest request, HttpServletResponse response) {
		String projectNameString = request.getParameter("projectname");
		return ProInstService.ModiftyProjectName(proinstid, projectNameString);

	}

	// 获取项目中某个项目的办理环节（不包含驳回的环节，每个定义节点包含一个实例节点）
	@RequestMapping(value = "/actinst/nopassback/{proinstid}", method = RequestMethod.GET)
	@ResponseBody
	public List<Map> GetActinstNotPassback(@PathVariable String proinstid, HttpServletRequest request, HttpServletResponse response) {

		return ProInstService.GetActinstNotPassback(proinstid);

	}

	// 获取办件过程（在登记系统中使用）
	@RequestMapping(value = "/actinst/{ywh}", method = RequestMethod.GET)
	@ResponseBody
	public List<Map> GetBJGC(@PathVariable String ywh, HttpServletRequest request, HttpServletResponse response) {
		return ProInstService.GetBJGC(ywh);
	}

	// 获取办件过程（在登记系统中使用）--参数为：PROLSH / YWLSH
	@RequestMapping(value = "/actinst/certmanage/{slbh}", method = RequestMethod.GET)
	@ResponseBody
	public List<Map> GetBJGCBySLBH(@PathVariable String slbh, HttpServletRequest request, HttpServletResponse response) {
		return ProInstService.GetBJGCBySLBH(slbh);
	}
	// 获取异常项目信息
	@RequestMapping(value = "/abnormal/{pageindex}/search/{pagesize}", method = RequestMethod.POST)
	@ResponseBody
	public Page GetAbnormalProject(@PathVariable Integer pageindex, @PathVariable Integer pagesize, HttpServletRequest request, HttpServletResponse response) {
		String key = request.getParameter("key");
		String type = request.getParameter("type");
		return ProInstService.GetAbnormalProject(key, type, pageindex, pagesize);
	}

	// 质检项目
	@RequestMapping(value = "/qualitytest/start/{actinst_id}", method = RequestMethod.POST)
	@ResponseBody
	public boolean QualityTest(@PathVariable String actinst_id, HttpServletRequest request, HttpServletResponse response) {
		User user = smStaff.getCurrentWorkStaff();
		return ProInstService.QualityTest(actinst_id, user);
	}

	@RequestMapping(value = "/qualitytest/cancel/{actinst_id}", method = RequestMethod.POST)
	@ResponseBody
	public boolean CancelQualityTest(@PathVariable String actinst_id, HttpServletRequest request, HttpServletResponse response) {
		User user = smStaff.getCurrentWorkStaff();
		return ProInstService.CancelQualityTest(actinst_id, user);

	}

	@RequestMapping(value = "/qualitytest/submit/{actinst_id}", method = RequestMethod.POST)
	@ResponseBody
	public SmObjInfo SubmitQualityApproval(@PathVariable String actinst_id, HttpServletRequest request, HttpServletResponse response) {
		User user = smStaff.getCurrentWorkStaff();
		String yjcongtent = request.getParameter("zjyj");
		return ProInstService.SubmitQualityApproval(actinst_id, user, yjcongtent);
	}

	/**
	 * 获取一个部门可以办理的所有活动
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/dept/actinstname", method = RequestMethod.GET)
	@ResponseBody
	public Map GetActNameByDeptID(HttpServletRequest request, HttpServletResponse response) {
		User user = smStaff.getCurrentWorkStaff();
		return ProInstService.GetActNameByDeptID(user.getId());
	}

	@RequestMapping(value = "/dept/proinstname", method = RequestMethod.GET)
	@ResponseBody
	public Map GetProNameByDeptID(HttpServletRequest request, HttpServletResponse response) {
		User user = smStaff.getCurrentWorkStaff();
		return ProInstService.GetProNameByDeptID(user.getId());
	}

	@RequestMapping(value = "/user/proinstname", method = RequestMethod.GET)
	@ResponseBody
	public Map GetProNameByUserID(HttpServletRequest request, HttpServletResponse response) {
		User user = smStaff.getCurrentWorkStaff();
		String status = request.getParameter("status");
		return ProInstService.GetProNameByUserID(user, status);
	}

	/**
	 * 获取所有的活动名称
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/all/actdefname", method = RequestMethod.GET)
	@ResponseBody
	public Map GetActName(HttpServletRequest request, HttpServletResponse response) {
		return ProInstService.GetActName();
	}

	@RequestMapping(value = "/all/prodefname", method = RequestMethod.GET)
	@ResponseBody
	public List<Map> GetProName(HttpServletRequest request, HttpServletResponse response) {
		return ProInstService.GetProName();
	}

	@RequestMapping(value = "/all/dossierproject", method = RequestMethod.GET)
	@ResponseBody
	public List<Map> GetAllProjectByActinstName() {
		return ProInstService.GetAllProjectByActinstName();
	}

	/**/
	@RequestMapping(value = "/staff/actinstname", method = RequestMethod.GET)
	@ResponseBody
	public List<Map> GetActNameByStaffID(HttpServletRequest request, HttpServletResponse response) {
		User user = smStaff.getCurrentWorkStaff();
		return ProInstService.GetActNameByStaffID(user.getId());
	}

	/**
	 * @author JHX
	 * @DATE:2016-08-23 23:30 获取当前人员所在部门的所有人员
	 * */
	@RequestMapping(value = "/dept/staff", method = RequestMethod.GET)
	@ResponseBody
	public List<User> getStaffByDept(HttpServletRequest request, HttpServletResponse response) {
		List<User> users = new ArrayList<User>();
		User user = smStaff.getCurrentWorkStaff();
		String deptid = user.getDepartment().getId();
		users.add(user);
		if (deptid != null && !deptid.equals("")) {
			users = userService.findUserByDepartmentId(deptid);
		}

		return users;
	}

	// 验证一个项目是否处于归档环节
	@RequestMapping(value = "/verify/dossier/{lsh}", method = RequestMethod.POST)
	@ResponseBody
	public Map VerifyDossier(@PathVariable String lsh, HttpServletRequest request, HttpServletResponse response) {
		User user = smStaff.getCurrentWorkStaff();
		return ProInstService.VerifyDossier(lsh, user);
	}

	// 获取所有等待归档的项目数量
	@RequestMapping(value = "/wait/dossier/count", method = RequestMethod.GET)
	@ResponseBody
	public long GetWaitDossierCount(HttpServletRequest request, HttpServletResponse response) {
		User user = smStaff.getCurrentWorkStaff();
		return ProInstService.GetWaitDossierCount(user.getId());
	}

	// 获取归档数据
	@RequestMapping(value = "/workflow/dossier/{file_number}", method = RequestMethod.POST)
	@ResponseBody
	public Map worlflowDossier(@PathVariable String file_number, HttpServletRequest request, HttpServletResponse response) {
		String actinstid = request.getParameter("actinstid");
		return ProInstService.worlflowDossier(actinstid, file_number);
	}

	// 鹰潭市下载共享中间库图片到本地的接口
	@RequestMapping(value = "/download/downloadfile/{value}", method = RequestMethod.POST)
	@ResponseBody
	public ResultMessage DownloadFile(@PathVariable String value, HttpServletRequest request, HttpServletResponse response) throws Exception {
		ResultMessage sMessage = new ResultMessage();
		try {
			String SHARE_FILE = ConfigHelper.getNameByValue("SHARE_FILE");
			String LOCAL_FILE = ConfigHelper.getNameByValue("LOCAL_FILE");
			String ZIP_FILE = ConfigHelper.getNameByValue("ZIP_FILE");
			String VISUAL_FILE = ConfigHelper.getNameByValue("VISUAL_FILE");
			LOCAL_FILE += "/IMAGE";
			ZIP_FILE += "/ZIP";
			SHARE_FILE = SHARE_FILE + value + "/";
			deleteAllFile.delFolder(LOCAL_FILE);
			deleteAllFile.delFolder(ZIP_FILE);
			// 文件路径不存在时，自动创建目录
			File writeFile = new File(LOCAL_FILE);
			if (!writeFile.exists()) {
				writeFile.mkdirs();
			}
			File zipFile = new File(ZIP_FILE);
			if (!zipFile.exists()) {
				zipFile.mkdirs();
			}

			// 配置中间库的共享文件夹的路径
			SmbFile file = new SmbFile(SHARE_FILE);
			if (file.exists()) {
				FileDownload.readShare(file, writeFile.toString());

				boolean boo = ZipCompressor.fileToZip(LOCAL_FILE, ZIP_FILE, value);
				if (boo == true) {
					sMessage.setSuccess("成功");
					sMessage.setMsg(VISUAL_FILE);
				} else {
					sMessage.setSuccess("失败");
					sMessage.setMsg("提取共享附件失败");
				}
			} else {
				sMessage.setSuccess("信息");
				sMessage.setMsg("你输入的共享项目编号不正确");
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("下载中间库附件发生错误");
			sMessage.setSuccess("失败");
			sMessage.setMsg("提取共享附件失败");
		}
		return sMessage;
	}

	// 按照項目名稱獲取項目信息
	@RequestMapping(value = "/allproject/actinst", method = RequestMethod.POST)
	@ResponseBody
	public Message getAllProjectByActName(HttpServletRequest request, HttpServletResponse response) {
		String actname = request.getParameter("actname");
		String status = request.getParameter("status");
		return ProInstService.getAllProjectByActName(actname, status);
	}

	/**
	 * 获取批量登簿的项目列表
	 * 
	 * @param protype
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "/batchboard/{protype}", method = RequestMethod.GET)
	@ResponseBody
	public Message getBatchBoardSearch(@PathVariable("protype") int protype, HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		request.setCharacterEncoding("UTF-8");
		String searchvalue = request.getParameter("value");
		searchvalue = new String(searchvalue.getBytes("iso8859-1"), "utf-8");
		String currpage = request.getParameter("currpage");
		String pagesize = request.getParameter("pagesize");
		if (currpage == null || currpage.equals(""))
			currpage = "1";
		int page = Integer.parseInt(currpage);
		String actinstname = request.getParameter("actinstname");
		if (actinstname != null && !actinstname.equals("")) {
			actinstname = new String(actinstname.getBytes("iso8859-1"), "utf-8");
		}
		String operaStaffString = smStaff.getCurrentWorkStaffID();// 当前操作人员

		return ProInstService.getBatchBoardSearch(operaStaffString, actinstname, protype, searchvalue, page, Integer.parseInt(pagesize));
	}

	/**
	 * @author JHX 海口项目，电子政务(审批通过后)，调用不动产登记接口 执行挂起操作
	 * */
	@RequestMapping(value = "/hanguphk/{actinst}/{staffid}", method = RequestMethod.POST)
	@ResponseBody
	public boolean SetHangUpHK(@PathVariable String actinst, @PathVariable String staffid, HttpServletRequest request, HttpServletResponse response) {
		String msg = request.getParameter("msg");
		return ProInstService.SetHangUpHK(actinst, msg, staffid);
	}

	/**
	 * @author JHX 海口项目，电子政务(审批不通过后)，调用不动产登记接口 执行挂起操作
	 * */
	@RequestMapping(value = "/hanguphk/appnotpass/{actinst}/{msg}", method = RequestMethod.POST)
	@ResponseBody
	public String hangUpApplyNotPass(@PathVariable String actinst, @PathVariable String msg, HttpServletRequest request, HttpServletResponse response) {
		// 审批不通过：更改审批不通过原因，设置ISAPPLYHANGUP 为 0
		boolean flag = smProInstService.setActinstProperty(actinst, msg);
		return String.valueOf(flag);
	}

	/**
	 * @author DUFF 海口项目，电子政务审批未通过，取消未通过标记
	 * 
	 * */
	@RequestMapping(value = "/delehangupnotpass/{actinst}", method = RequestMethod.GET)
	@ResponseBody
	public boolean DeleNotPass(@PathVariable String actinst, HttpServletRequest request, HttpServletResponse response) {
		return smProInstService.DeleNotPass(actinst);
	}

	/**
	 * 海口项目更具受理编号查询当前的流程的状态
	 * */
	@RequestMapping(value = "/getproject/state/{slbh}", method = RequestMethod.POST)
	@ResponseBody
	public String getCurrentProcessState(@PathVariable String slbh, HttpServletRequest request, HttpServletResponse response) {
		String state = smProInstService.getProcessState(slbh);
		return state;
	}

	/**
	 * @author JHX
	 * @DATE:2016-08-12 20:01 获取所有挂起的项目
	 * 
	 * */
	@RequestMapping(value = "/getprojects/hangup", method = RequestMethod.POST)
	@ResponseBody
	public String getHangupProjects(HttpServletRequest request, HttpServletResponse response) {
		return smProInstService.getAllHangupProject();
	}

	/**
	 * @author JHX 海口项目实施挂起操作
	 * */
	@RequestMapping(value = "/hanguphk/{actinst}", method = RequestMethod.POST)
	@ResponseBody
	public String SetHangUpHK(@PathVariable String actinst, HttpServletRequest request, HttpServletResponse response) {
		boolean flag = true;
		String msg = request.getParameter("msg");
		String type = request.getParameter("type");
		String fgbh = "";
		String fgqx = "";
		String fgyj = "";
		String actinstid = "";
		// 海口调用电子政务中的数据
		if (type != null && type.equals("3")) {
			fgbh = request.getParameter("fgbh");
			fgqx = request.getParameter("fgqx");
			fgyj = request.getParameter("fgyj");
		}
		try {
			boolean insertsuccess = proinststatemodify.hangUp(actinst, type, fgbh, fgyj, fgqx, msg);
			// 更改流程实例的是否申请挂起
			if (insertsuccess)
				smactinst.modifyWfi_ActInst(actinst, 1);
			actinstid = actinst;
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
		}
		return actinst;
	}

	/**
	 * @author JHX 海口解挂,执行解挂，需要向电子政务表中插入数据
	 * */
	@RequestMapping(value = "/hangdownhk/{actinst}", method = RequestMethod.POST)
	@ResponseBody
	public boolean SetHangDownHK(@PathVariable String actinst, HttpServletRequest request, HttpServletResponse response) {
		System.out.println(actinst);
		boolean flag = true;
		try {
			proinststatemodify.hangDown(actinst);
			// 查完数据之后执行登记系统的解挂操作
			// ProInstService.SetHangDown(actinst);
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
		}
		return flag;
	}

	/**
	 * 海口电子政务系统调用接口执行解挂操作 海口解挂
	 * */
	@RequestMapping(value = "/hangdownhk/{actinst}/{staffid}/{downtime}", method = RequestMethod.POST)
	@ResponseBody
	public boolean SetHangDownHK(@PathVariable String actinst, @PathVariable String staffid, @PathVariable String downtime, HttpServletRequest request, HttpServletResponse response) {
		boolean flag = true;
		try {
			boolean downsuccess = ProInstService.SetHangDownHK(actinst, staffid, downtime);
			// 解挂完成之后更改实例状态
			if (downsuccess)
				smactinst.modifyWfi_ActInst(actinst, 0);
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
		}
		return flag;
	}

	@RequestMapping(value = "/hangup/{actinst}", method = RequestMethod.POST)
	@ResponseBody
	public String SetHangUp(@PathVariable String actinst, HttpServletRequest request, HttpServletResponse response) {
		String msg = request.getParameter("msg");
		ProInstService.SetHangUp(actinst, msg);
		return actinst;
	}

	@RequestMapping(value = "/hangdown/{actinst}", method = RequestMethod.POST)
	@ResponseBody
	public boolean SetHangDown(@PathVariable String actinst, HttpServletRequest request, HttpServletResponse response) {
		return ProInstService.SetHangDown(actinst);
	}

	/**
	 * 获取批量发证的项目列表
	 * 
	 * @param protype
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "/batchcert/{protype}", method = RequestMethod.GET)
	@ResponseBody
	public Message getBatchCertSearch(@PathVariable("protype") int protype, HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		request.setCharacterEncoding("UTF-8");
		String searchvalue = request.getParameter("value");
		searchvalue = new String(searchvalue.getBytes("iso8859-1"), "utf-8");
		String currpage = request.getParameter("currpage");
		String pagesize = request.getParameter("pagesize");
		if (currpage == null || currpage.equals(""))
			currpage = "1";
		int page = Integer.parseInt(currpage);
		String actinstname = RequestHelper.getParam(request,"actinstname");
		String operaStaffString = smStaff.getCurrentWorkStaffID();// 当前操作人员

		Message message = ProInstService.getBatchCertSearch(operaStaffString, actinstname, protype, searchvalue, page, Integer.parseInt(pagesize));
 		return  message;
	}
	
	/**
	 *  获取批量发证的项目列表
	 * @param protype
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 * created by liuxingrong 2017-11-8
	 */
	@RequestMapping(value = "/newBatchcert/{protype}", method = RequestMethod.GET)
	@ResponseBody
	public Message getNewBatchCertSearch(@PathVariable("protype") int protype, HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		request.setCharacterEncoding("UTF-8");
		String searchvalue = request.getParameter("value");
		searchvalue = new String(searchvalue.getBytes("iso8859-1"), "utf-8");
		String currpage = request.getParameter("currpage");
		String pagesize = request.getParameter("pagesize");
		String pronames = request.getParameter("pronames");
//		if(pronames!=null){
//			pronames = new String(pronames.getBytes("iso8859-1"), "utf-8");
//		}
		String zl = request.getParameter("zl");
		String lshstart = request.getParameter("lshstart");
		String lshend = request.getParameter("lshend");
		if (currpage == null || currpage.equals(""))
			currpage = "1";
		int page = Integer.parseInt(currpage);
		String actinstname = RequestHelper.getParam(request,"actinstname");
		String operaStaffString = smStaff.getCurrentWorkStaffID();// 当前操作人员
		Message message = ProInstService.getNewBatchCertSearch(operaStaffString, actinstname, protype, searchvalue, page, Integer.parseInt(pagesize),pronames,zl,lshstart,lshend);
		return  message;
	}
	
	@RequestMapping(value = "/gzbatchcert/{protype}", method = RequestMethod.GET)
	@ResponseBody
	public Message getBatchCertSearch_gz(@PathVariable("protype") int protype, HttpServletRequest request, HttpServletResponse response,QueryCriteria query) throws UnsupportedEncodingException {
		request.setCharacterEncoding("UTF-8");
		String searchvalue = request.getParameter("value");
		String bdcdyh = request.getParameter("bdcdyh");
		searchvalue = new String(searchvalue.getBytes("iso8859-1"), "utf-8");
		String currpage = request.getParameter("currpage");
		String pagesize = request.getParameter("pagesize");
		String prolsh=request.getParameter("prolsh");
		String textcode = request.getParameter("textcode");//扫描枪二维码输入
		if (textcode != null || !textcode.equals("") ){
			searchvalue = textcode;
		}
			
		if(currpage.equals("10000")){
			searchvalue="";
			protype =3;
		}
		if (currpage == null || currpage.equals("") )
			currpage = "1";
		int page = Integer.parseInt(currpage);
		String actinstname = request.getParameter("actinstname");
		if (actinstname != null && !actinstname.equals("")) {
			actinstname = new String(actinstname.getBytes("iso8859-1"), "utf-8");
		}
		String operaStaffString = smStaff.getCurrentWorkStaffID();// 当前操作人员
		List<Map> datas = new ArrayList<Map>();
		Message msg = new Message();
		if (query.getCurrentPageIndex() != null) {
			if(currpage.equals("10000")){
				Message m = new Message();
				String[] strarr = prolsh.split(",");
				for(String str :strarr){
					searchvalue =str;
					query.setValue("");
					page=1;
					m= ProInstService.getPLFZProjectList(operaStaffString, actinstname, protype, searchvalue, page, Integer.parseInt(pagesize),bdcdyh,query);
					List<Map> rows=(List<Map>)m.getRows();
					datas.addAll(rows);
				}
				 msg.setRows(datas);
				 return msg;
		  }else{
			    page=Integer.parseInt(currpage);
				return ProInstService.getPLFZProjectList(operaStaffString, actinstname, protype, searchvalue, page, Integer.parseInt(pagesize),bdcdyh,query);

		  }		
		} else {
			return null;
		}
//		return ProInstService.getBatchCertSearch(operaStaffString, actinstname, protype, searchvalue, page, Integer.parseInt(pagesize),query);
	}

	/**
	 * 获取批量收费的项目列表
	 * 
	 * @param protype
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "/batchcharge/{protype}", method = RequestMethod.GET)
	@ResponseBody
	public Message getBatchChargeSearch(@PathVariable("protype") int protype, HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		request.setCharacterEncoding("UTF-8");
		String searchvalue = RequestHelper.getParam(request, "value");
		String currpage = RequestHelper.getParam(request, "currpage");
		String pagesize = RequestHelper.getParam(request, "pagesize");
		String pronames = request.getParameter("pronames");
		if(pronames!=null){
			pronames = new String(pronames.getBytes("iso8859-1"), "utf-8");
		}
		String zl = request.getParameter("zl");
		String lshstart = request.getParameter("lshstart");
		String lshend = request.getParameter("lshend");
		if (currpage == null || currpage.equals(""))
			currpage = "1";
		int page = Integer.parseInt(currpage);
		String actinstname = RequestHelper.getParam(request, "actinstname");
		String operaStaffString = smStaff.getCurrentWorkStaffID();// 当前操作人员
		return ProInstService.getBatchChargeSearch(operaStaffString, actinstname, protype, searchvalue, page, Integer.parseInt(pagesize),pronames,zl,lshstart,lshend);
	}

	/**
	 * 获取批量发证的项目列表
	 * 
	 * @param protype
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getxmxxandbdcqzh/", method = RequestMethod.GET)
	@ResponseBody
	public List getXMBHandBDCQZH(HttpServletRequest request, HttpServletResponse response) throws Exception {
		List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		String ywlshs = RequestHelper.getParam(request, "ywlshs");
		String[] ywlsharr = ywlshs.split(",");
		if (ywlsharr != null && ywlsharr.length > 0) {
			for (String ywlsh : ywlsharr) {
				if (StringHelper.isEmpty(ywlsh)) {
					continue;
				}
				HashMap<String, String> map = new HashMap<String, String>();
				String xmbh = ProInstService.getXMBHByYWLSH(ywlsh);
				map.put("xmbh", xmbh);
				String ms = ProInstService.getQZHORZMHByXMBH(xmbh);
				if (!StringHelper.isEmpty(ms)) {
					map.put("bdcqzh", ms);
				} else {
					map.put("bdcqzh", "");
				}
				list.add(map);
			}
		}
		return list;

	}

	/**
	 * 获取批量收费的收费项目列表
	 * 
	 * @param protype
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/{xmbhs}/charge/", method = RequestMethod.GET)
	public @ResponseBody Message GetSFList(@PathVariable("xmbhs") String xmbhs, HttpServletRequest request, HttpServletResponse response) {
		Message msg = new Message();
		List<Map> listAll = new ArrayList<Map>();
		List<String> listsflx = new ArrayList<String>();
		List<Map> listBysflx = new ArrayList<Map>();
		Map<String,String> MapAll = new HashMap<String,String>();	
		Double zysje = 0.0;
		Double zssje = 0.0;
		if (!StringHelper.isEmpty(xmbhs)) {
			String[] keys = xmbhs.split(",");// 多个记录之间键值对用*分隔
			//获取所有的收费信息
			for (int i = 0; i < keys.length; i++) {
				com.supermap.wisdombusiness.web.Message message = new com.supermap.wisdombusiness.web.Message();
				String condition = " FROM BDCK.BDCS_DJSF SF LEFT JOIN BDCK.BDCS_SFDY SFDY ON SFDY.ID = SF.SFDYID  WHERE SF.XMBH='"
						+ keys[i] + "' ORDER BY SFDY.SFBMMC,SFDY.SFXLMC,SFDY.SFKMMC";
				Long row = baseCommonDao.getCountByFullSql(condition);
				message = projectService.getPagedSFList(keys[i],1,Integer.parseInt(row.toString()));
				List<Map> lists = new ArrayList<Map>();
				lists = (List<Map>)message.getRows();
				for(Map map:lists){
					String sflx = map.get("sfxlmc")+"-"+ map.get("sfkmmc");
					map.put("sflx", sflx);
					if (!listsflx.contains(sflx)){
						listsflx.add(sflx);
					}
					listAll.add(map);
				}
			}
			//相同的sflx进行汇总
			for (String lx : listsflx) {
				Double ysje = 0.0;
				Double ssje = 0.0;
				String sfbmmc = "";
				Map<String,String> MapBylx = new HashMap<String,String>();
				for(Map map : listAll){
					if (lx.equals(map.get("sflx"))){	
						//求应收金额和实收金额的总和
						ysje += Double.parseDouble(map.get("ysje").toString());
						ssje += Double.parseDouble(map.get("ssje").toString());
						sfbmmc = StringHelper.formatObject(map.get("sfbmmc"));
					}
					
				}
				MapBylx.put("sfbmmc", sfbmmc);
				MapBylx.put("sflx", lx);
				MapBylx.put("ysje", ysje.toString());
				MapBylx.put("ssje", ssje.toString());
				listBysflx.add(MapBylx);
				zysje += ysje;
				zssje += ssje;
			}
			Map<String,String> Mapzje = new HashMap<String,String>();
			List<Map> listfooter = new ArrayList<Map>();
			Mapzje.put("sflx", "合计");
			Mapzje.put("ysje", zysje.toString());
			Mapzje.put("ssje", zssje.toString());
			
			listfooter.add(Mapzje);
			
			msg.setRows(listBysflx);;
			msg.setFooter(listfooter);
			msg.setTotal(listBysflx.size());
		}
		return msg;
	}
	/**
	 * 获取批量收费的收费项目列表
	 * 
	 * @param protype
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/{xmbhs}/recharge/", method = RequestMethod.GET)
	public @ResponseBody ResultMessage ReChargeSFList(@PathVariable("xmbhs") String xmbhs, HttpServletRequest request, HttpServletResponse response) {
		ResultMessage msg = new ResultMessage();
		if (!StringHelper.isEmpty(xmbhs)) {
			String[] keys = xmbhs.split(",");// 多个记录之间键值对用*分隔
			//获取所有的收费信息
			for (int i = 0; i < keys.length; i++) {
				try {
					chargeService.calculate(keys[i]);
					msg.setMsg("重新计算成功！");
					msg.setSuccess("true");
				} catch (parsii.tokenizer.ParseException e) {
					e.printStackTrace();
				}
			}
		}	
		return msg;
	}
    /**
     * 导出excel（批量发证页面）
     * @date   2016年11月3日 上午10:53:53
     * @author JHX
     * @param request
     * @param response
     * @return
     * @throws IOException 
     */
	@RequestMapping(value = "/excel/export", method = RequestMethod.POST)
	@ResponseBody
	public String excelExport(HttpServletRequest request,
			HttpServletResponse response) throws IOException  {
		String prolsh=request.getParameter("prolsh");
		String url = "";
		if(prolsh!=null&&!prolsh.equals("")){
			    Message m = new Message();
			    List<Map> datas = null;
				if ( m!=null && m.getRows()!=null){
					datas=(List<Map>)m.getRows();
				}
				String basePath = request.getRealPath("/") + "\\resources\\PDF";
				String outpath = "";
				String tmpFullName = "";
				FileOutputStream outstream = null;
				//if(datas != null && datas.size() > 0){
					outpath = basePath + "\\tmp\\bdcqzyj.xls";
					url = request.getContextPath() + "\\resources\\PDF\\tmp\\bdcqzyj.xls";
				    outstream = new FileOutputStream(outpath); 
				    tmpFullName = request.getRealPath("/WEB-INF/jsp/wjmb/bdcqzyj.xls");
				    InputStream input = new FileInputStream(tmpFullName);
					HSSFWorkbook  wb = new HSSFWorkbook(input);
					Sheet sheet = wb.getSheetAt(0);
					Map<String,Integer> MapCol = new HashMap<String,Integer>();				
					MapCol.put("序号", 0);
					MapCol.put("不动产受理编号", 1);
					MapCol.put("房产受理编号", 2);
					MapCol.put("权利人/抵押人",3);
					MapCol.put("坐落",4);
					MapCol.put("不动产权证号/不动产登记证明号",5);
					MapCol.put("备注", 6);
		            int rownum = 2;
					/*for(Map djdy:datas){
				  		 HSSFRow row = (HSSFRow)sheet.createRow(rownum);
				  		 try{
					         HSSFCell Cell0 = row.createCell(MapCol.get("序号"));
					         Cell0.setCellValue(rownum-1);
					         HSSFCell Cell1 = row.createCell(MapCol.get("不动产受理编号"));
					         Cell1.setCellValue(StringHelper.formatObject(djdy.get("")));
					         HSSFCell Cell2 = row.createCell(MapCol.get("权利人/抵押人"));
					         Cell2.setCellValue(StringHelper.formatObject(djdy.get("")));
					         HSSFCell Cell3 = row.createCell(MapCol.get("坐落"));
					         Cell3.setCellValue(StringHelper.formatObject(djdy.get("")));
					         HSSFCell Cell4 = row.createCell(MapCol.get("不动产权证号/不动产登记证明号"));
					         Cell4.setCellValue(StringHelper.formatObject(djdy.get("")));
					         HSSFCell Cell5 = row.createCell(MapCol.get("备注"));
					         Cell5.setCellValue(StringHelper.formatObject(djdy.get("")));
					         rownum++ ;
				  		 }
				  		 catch(Exception ex){
				  		 }
					  }	*/  		
					 wb.write(outstream); 
					 outstream.flush(); 
					 outstream.close();
				// }
		
	          }
		return url;
	}
	/**
	 * 批量添加发证记录（URL:"/{paras}/fzs",Method：POST）
	 * 
	 * @param paras
	 *            xmbh:hfzsh的键值对，这样就可以和批量发证逻辑的通用
	 * @param djfzBO
	 * @param result
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/fzs/", method = RequestMethod.POST)
	@ResponseBody
	public Message AddFZInfos(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Message msg = new Message();
		String FZRY = request.getParameter("FZRY");
		String FZSJ = request.getParameter("FZSJ");
		String LZRXM = request.getParameter("LZRXM");
		String LZRZJLB = request.getParameter("LZRZJLB");
		String LZRZJHM = request.getParameter("LZRZJHM");
		String LZRDH = request.getParameter("LZRDH");
		String LZRYB = request.getParameter("LZRYB");
		String ISPASSOVER = request.getParameter("ISPASSOVER");
		// String[] keys = paras.split("\\*");// 多个记录之间键值对用*分隔
		String outcertinfolist = request.getParameter("certinfolist");
		List list = new ArrayList();
		if (!StringHelper.isEmpty(outcertinfolist)) {
			String[] keys = outcertinfolist.split("\\*");// 多个记录之间键值对用*分隔
			BDCS_XMXX xmxx = null;
			List<String> ywlshs = new ArrayList<String>();
			for (String key : keys) {
				Map<String,String> map = new HashMap<String, String>();
				String ywlsh = null;
				String xmbh2 = key.split(":")[0];
				String hfzsh = key.split(":")[1];
//				if (xmxx == null) {
				xmxx = Global.getXMXXbyXMBH(xmbh2);
//				}
				if (!StringHelper.isEmpty(xmxx.getYWLSH())) {
					ywlsh = xmxx.getYWLSH();
				}
//				logger.info("ywlsh = xmxx.getYWLSH()");
//				logger.info(ywlsh + "before");
				String condition = MessageFormat.format("XMBH=''{0}'' AND HFZSH=''{1}''", xmbh2, hfzsh);
				BDCS_DJFZ djfz = zsService.getDJFZ(condition);

				if (djfz == null) {
					djfz = new BDCS_DJFZ();
					djfz.setId((String) SuperHelper.GeneratePrimaryKey());
				}
				djfz.setYWH(xmxx == null ? "" : xmxx.getPROJECT_ID());
				djfz.setFZRY(FZRY);
				djfz.setFZSJ(StringHelper.FormatByDate(FZSJ));
				djfz.setLZRXM(LZRXM);
				if (LZRZJLB.equals("身份证")) {
					LZRZJLB = "1";
				} else if (LZRZJLB.equals("军官证")) {
					LZRZJLB = "2";
				}
				djfz.setLZRZJLB(LZRZJLB);
				djfz.setLZRZJHM(LZRZJHM);
				djfz.setLZRDH(LZRDH);
				djfz.setLZRYB(LZRYB);
				djfz.setXMBH(xmbh2);
				djfz.setHFZSH(hfzsh);
				djfz.setYSDM("1");// TODO YSDM为必填项，暂时赋值为1
				zsService.AddFZXX(djfz);
				
				//添加发证信息到rkqzb中
				zsService.AddFZXXtoRKQZB(xmbh2, hfzsh, StringHelper.FormatByDate(FZSJ), LZRXM, LZRZJHM, null);
				
				map.put("ywlsh", ywlsh);
				map.put("hfzsh", hfzsh);
				map.put("success", "true");
				//TODO:是否转出操作
				if(ISPASSOVER!=null&&ISPASSOVER.equals("true")){
					if(ywlshs.contains(ywlsh)){
						continue;
					}else{
						ywlshs.add(ywlsh);
					}
//					logger.info(ywlsh + "after");
					Wfi_ActInst actinst = ProInstService.getFzActinst(ywlsh);
					if(actinst!=null){
						Wfi_ProInst proInst = smproinst.GetProInstByActInstId(actinst.getActinst_Id());
						String area = proInst.getAreaCode();
						if(StringHelper.isEmpty(area)){
							area = userService.findById(proInst.getStaff_Id()).getAreaCode();
						}
						
						Wfd_Actdef actdef = smactinst.GetActDef(actinst.getActinst_Id());
						if(actdef!=null){
							String turnoutrange = actdef.getTurnOutRange();
							if(turnoutrange!=null&&!turnoutrange.trim().equals("")){
								if(turnoutrange.equals("0")){
									area=area.substring(0,2)+"0000";
								}else if(turnoutrange.equals("1")){
									area=area.substring(0,4)+"00";
								}
							}
						}
						
//						logger.info(actinst.getActinst_Name() + "after-actinst");
						//转出操作
						try {
							SmObjInfo info = null;
							List<SmObjInfo> infos = smStaff.GetActStaffByActInst(actinst.getActinst_Id());
							if (infos != null && infos.size() > 0) {
								SmObjInfo route = infos.get(0);
								String routeidString = route.getID();
								SmObjInfo staff = ProInstService.getBatchPassoverStaff(infos);
								List<SmObjInfo> objInfos = new ArrayList<SmObjInfo>();
								if (staff != null) {
									String typeString = staff.getDesc();
								if (typeString.equals("Staff")) {// 按照员工转出
									     objInfos.add(staff);
								} else if (typeString.equals("Dept")) {// 按照部门
									List<User> users = smStaff.getUserByDepidAndAreacode(staff.getID(), area);
									if (users != null && users.size() > 0) {
										for (User user : users) {
											SmObjInfo objInfo = new SmObjInfo();
											objInfo.setID(user.getId());// 设置staffid
											objInfo.setName(user.getUserName());// 设置staffName
											objInfos.add(objInfo);
										}
									}
								} else if (typeString.equals("Role")) {// 按照角色
									List<User> users = roleService.findUsersByRoleIdAndCode(staff.getID(), area);
									if (users != null && users.size() > 0) {
										for (User user : users) {
											SmObjInfo objInfo = new SmObjInfo();
											objInfo.setID(user.getId());// 设置staffid
											objInfo.setName(user.getUserName());// 设置staffName
											objInfos.add(objInfo);
										}
									  }
								    }
									String operaStaffString = smStaff.getCurrentWorkStaffID();
									info = operationService.PassOver(
											routeidString,actinst.getActinst_Id(), 
											objInfos,operaStaffString,"", false);
								}
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
				list.add(map);
			}
		} else {

		}
		msg.setRows(list);
		YwLogUtil.addYwLog("批量添加发证-成功", ConstValue.SF.YES.Value, ConstValue.LOG.ADD);
		return msg;
	}

	@RequestMapping(value = "/proinfo/{lsh}", method = RequestMethod.GET)
	@ResponseBody
	public Wfi_ProInst getInfoByLsh(@PathVariable String lsh) {
		return ProInstService.getInfoByLsh(lsh);
	}

	@RequestMapping(value = "/testconst", method = RequestMethod.GET)
	@ResponseBody
	public Map GetConstMapping() {
		try {
			return ConstMapping.GetConstMap("FWYT", "100");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 海口市获取流程定义的tree
	 * 
	 * @author JHX
	 * @PARAM
	 * @date 2016-07-23
	 * 
	 * */
	@RequestMapping(value = "/getMappingInfos/{ywh}", method = RequestMethod.GET)
	public @ResponseBody HashMap<Object, Object> getProdefTreeByMapping(HttpServletRequest request, HttpServletResponse response, @PathVariable String ywh) {
		HashMap<Object, Object> ressule = new HashMap<Object, Object>();
		List<Map<String, String>> list = null;
		// 获取项目基本信息
		Map<Object, Object> processAndPsnInfoRes = operationService.getSQRXX(ywh);
		if (processAndPsnInfoRes != null && processAndPsnInfoRes.containsKey("desc")) {
			if (processAndPsnInfoRes.get("desc").equals("业务已经受理，不能二次受理")) {
				ressule.put("desc", "业务已经受理，不能二次受理");
			}
		} else {
			List<Map<String, String>> processAndPsnInfo = (List<Map<String, String>>) processAndPsnInfoRes.get("content");
			HashMap<String, String> map = null;
			String peocessmapping = null;
			if (processAndPsnInfoRes != null && processAndPsnInfoRes.containsKey("ProName")) {
				peocessmapping = processAndPsnInfoRes.get("ProName").toString();
				list = ProInstService.GetProdefTreeByMapping(peocessmapping);
			}
			ressule.put("flow", list);
			ressule.put("info", processAndPsnInfo);
		}
		return ressule;
	}

	/**
	 * @author JHX 海口市申请受理项目
	 * */
	@RequestMapping(value = "/acceptProject/from/hk", method = RequestMethod.POST)
	public @ResponseBody SmObjInfo acceptProjectFromHK(HttpServletRequest request, HttpServletResponse response, String username, String tel, String prodefname, String peodefid, String ywh, String starttime, String willfinishtime, String sqr) {
		SmProInfo smproinf = new SmProInfo();
		smproinf.setProDef_ID(peodefid);
		String pronameEnd = prodefname.replaceAll("-->", ",");
		smproinf.setProjectName(sqr);
		smproinf.setProDef_Name(pronameEnd);
		smproinf.setProLSH(ywh);
		smproinf.setProStartTime(starttime);
		smproinf.setProWillFinishTime(willfinishtime);
		Wfi_ProUserInfo info1 = new Wfi_ProUserInfo(null, ywh, username, tel, null, null, null, null, null);
		operationService.SaveInfo(info1);
		SmObjInfo info = smProInstService.Accept(smproinf);
		return info;
	}

	/**
	 * 插入申请人信息
	 * 
	 * 
	 * */
	@RequestMapping(value = "/addappperson/from/hk", method = RequestMethod.POST)
	public @ResponseBody SmObjInfo addApplyPerson(HttpServletRequest request, HttpServletResponse response, String DWDZ, String DWLXDH, String LXR, String LXRDH, String SQDW, String SQR, String XMBH, String ARTIFICIALPERSON) {
		SmObjInfo smobjinfo = new SmObjInfo();
		String[] persons = SQDW.split(" ");
		if (persons.length > 0) {
			for (int i = 0; i < persons.length; i++) {
				BDCS_SQR sqr = new BDCS_SQR();
				sqr.setXMBH(XMBH);
				sqr.setSQRXM(persons[i]);
				sqr.setLXDH(LXRDH);
				sqr.setGZDW(DWDZ);
				sqr.setFDDBR(ARTIFICIALPERSON);
				smProInstService.addSqr(sqr);
				smobjinfo.setDesc("创建申请人成功");
			}
		} else {
			smobjinfo.setDesc("未创建申请人");
		}
		return smobjinfo;
	}

	/**
	 * 鹰潭自动创建查封项目
	 * 
	 * @作者 胡加红
	 * @创建时间 2016年8月23日下午10:57:03
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/cjxm", method = RequestMethod.POST)
	public @ResponseBody String accobj(HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setCharacterEncoding("utf-8");
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		String qzh1 = URLDecoder.decode(request.getParameter("qzh"), "utf-8");
		String ywmc = URLDecoder.decode(request.getParameter("ywmc"), "utf-8");
		String qlrmc1 = URLDecoder.decode(request.getParameter("qlrmc"), "utf-8");
		String cslx1 = URLDecoder.decode(request.getParameter("cslx"), "utf-8");
		String rwlsh1 = URLDecoder.decode(request.getParameter("rwlsh"), "utf-8");
		String username = URLDecoder.decode(request.getParameter("username"), "utf-8");
		String status = smProInstService.getAccobj(qzh1, qlrmc1, cslx1, rwlsh1,ywmc,username, request, response);
		return status;
	}

	/**
	 * 海口获取prouserinfo信息
	 * 
	 * @作者 duff
	 * @创建时间 2016年8月29日
	 * @param request
	 * @param response
	 * @return
	 * @throws lsh
	 */
	@RequestMapping(value = "/getprouserinfo/{lsh}", method = RequestMethod.GET)
	@ResponseBody
	public List<Wfi_ProUserInfo> getInfo(@PathVariable String lsh, HttpServletRequest request, HttpServletResponse response) {
		return smProInstService.GetInfo(lsh);
	}

	/**
	 *
	 * 根据actinstid获取转办人员
	 *
	 */
	@RequestMapping(value = "/getturnname/{actinstid}", method = RequestMethod.GET)
	@ResponseBody
	public String getturnname(@PathVariable String actinstid, HttpServletRequest request, HttpServletResponse response) {
		return operationService.GetTurnName(actinstid);
	}
	
	/**
	 *
	 * 根据actinstid获取督办人员
	 *
	 */
	@RequestMapping(value = "/getdbname/{actinstid}", method = RequestMethod.GET)
	@ResponseBody
	public String getDbName(@PathVariable String actinstid, HttpServletRequest request, HttpServletResponse response) {
		return operationService.getDbName(actinstid);
	}

	/**
	 *
	 * 检测是否可以流程重指
	 *
	 */
	@RequestMapping(value = "/canrevise/{actinstid}", method = RequestMethod.GET)
	@ResponseBody
	public ResultMessage canRevise(@PathVariable String actinstid, HttpServletRequest request, HttpServletResponse response) {
		return smProInstService.canRevice(actinstid);
	}

	/**
	 *
	 * 流程重指
	 *
	 */
	@RequestMapping(value = "/revice", method = RequestMethod.POST)
	@ResponseBody
	public ResultMessage Revise(HttpServletRequest request, HttpServletResponse response) {
		String prodefid = request.getParameter("prodefid");
		String actinstid = request.getParameter("actinstid");
		String prodefname = request.getParameter("prodefname");
		Wfi_ProInst inst = smproinst.GetProInstByActInstId(actinstid);
		ResultMessage message = smProInstService.revice(inst, prodefid, prodefname);
		return message;
	}

	/**
	 *
	 * 获取当前人员所在部门的所有人员角色信息
	 *
	 */
	@RequestMapping(value = "/getuserrole", method = RequestMethod.GET)
	@ResponseBody
	public List<Map> getUserRole(HttpServletRequest request, HttpServletResponse response) {
		User user = smStaff.getCurrentWorkStaff();
		String deptid = user.getDepartment().getId();
		if (deptid != null && !deptid.equals("")) {
			List<Map> list = smProInstService.getUserRole(deptid);
			return list;
		}
		return null;
	}

	// 添加委托
	@RequestMapping(value = "add/passwork", method = RequestMethod.POST)
	@ResponseBody
	public SmObjInfo Addpasswork(HttpServletRequest request, HttpServletResponse response) throws ParseException {
		Wfi_PassWork passwork = new Wfi_PassWork();
		String type = request.getParameter("type");
		String data = request.getParameter("data");
		String start = request.getParameter("start");
		String end = request.getParameter("end");
		String target = request.getParameter("target");
		String targetuser = request.getParameter("targetuser");
		String targetuserid = request.getParameter("targetuserid");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		passwork.setPass_Start(sdf.parse(start));
		passwork.setPass_End(sdf.parse(end));
		passwork.setTostaff_Id(target);
		User user = smStaff.getCurrentWorkStaff();
		passwork.setStaff_Id(user.getId());
		passwork.setStaff_Name(user.getUserName());
		passwork.setTostaff_Name(targetuser);
		passwork.setTostaff_Id(targetuserid);
		if (type != null && !type.equals("")) {
			if (type.equals("1")) {
				passwork.setIsallprower(1);
			} else {
				passwork.setIsallprower(0);
				if (type.equals("2")) {
					passwork.setRole_Ids(data);
				} else if (type.equals("3")) {
					passwork.setProdef_Ids(data);
				}
			}
		}
		return ProInstService.Addpasswork(passwork);
	}

	/**
	 * 获取当前委托列表
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "/curpasswork/{type}", method = RequestMethod.GET)
	@ResponseBody
	public Message getCurPassWork(@PathVariable String type, HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {

		String staff_id = smStaff.getCurrentWorkStaffID();// 当前操作人员
		return ProInstService.getCurPassWork(staff_id, type);
	}

	/**
	 * 获取历史委托列表
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "/historypasswork/{type}", method = RequestMethod.GET)
	@ResponseBody
	public Message getHistoryPassWork(@PathVariable String type, HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {

		String staff_id = smStaff.getCurrentWorkStaffID();// 当前操作人员
		return ProInstService.getHistoryPassWork(staff_id, type);
	}

	@RequestMapping(value = "/cancelpasswork/{id}", method = RequestMethod.GET)
	@ResponseBody
	public Message cancelPasswork(@PathVariable String id) {
		return ProInstService.cancelPasswork(id);
	}
	
	
	@RequestMapping(value = "/online/process/schedule/search/{prolsh}", method = RequestMethod.GET)
	@ResponseBody
	public Map<String ,Object> getProcessScheduleOnline(@PathVariable String prolsh) {
		return ProInstService.getProcessScheduleOnline(prolsh);
	}
	// 修改项目备注
	@RequestMapping(value = "/modify/proinfo/{proinstid}", method = RequestMethod.GET)
	@ResponseBody
	public boolean ModiftyProjectInfo(@PathVariable String proinstid, HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		String projectinfo = request.getParameter("projectinfo");
		//判断中文是否乱码再转换
		if(!Charset.forName("GBK").newEncoder().canEncode(projectinfo)){
			projectinfo = new String(projectinfo.getBytes("iso8859-1"), "utf-8");
		}
		return ProInstService.ModiftyProjectInfo(proinstid, projectinfo);

	}
	
	/**
	 * 根据流水号获取项目信息
	 */
	@RequestMapping(value = "/proinfobylsh", method = RequestMethod.GET)
	@ResponseBody
	public Map<String,String> GetProInfoByLsh ( HttpServletRequest request, HttpServletResponse response){
		Map<String,String> result = new HashMap<String, String>();
		String prolsh = request.getParameter("ywh");
		Wfi_ProInst proinst = ProInstService.GetProInstByLsh(prolsh);
		if(proinst!=null){
			Wfd_Prodef prodef = smProDefService.GetProdefById(proinst.getProdef_Id());
			Wfi_ActInst actinst = ProInstService.GetNewActInst(proinst.getProinst_Id());
			result.put("id", proinst.getProinst_Id());
			String ywh = proinst.getYwh();
			ywh = null==ywh?"":ywh;
			result.put("ywh", ywh);
			result.put("djlb", prodef.getProdef_Name());
			Map plist = ProjectHelper.GetFileTransferInfoEx(proinst.getFile_Number());
			String sqr = plist.get("SQR").toString();
			result.put("sqr", null ==sqr?"":sqr);
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			result.put("xmzt",proinst.getProinst_Status()>0?"办理中":"已办结");
			result.put("slsj", df.format(proinst.getProinst_Start()));
			Date date = proinst.getProinst_End();
			result.put("bjsj", null==date?"":df.format(date));
			result.put("name", actinst.getActinst_Name());
		}
		return result;
	}
	
	/**
	 * 根据filenumber获取业务信息
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/getbusinessinfo", method = RequestMethod.GET)
	@ResponseBody
	public List<Map> getBusinessInfo ( HttpServletRequest request, HttpServletResponse response){
		String filenumbers = request.getParameter("filenumbers");
		List<Map> result = new ArrayList<Map>();
		String[] strArray = filenumbers.split(",");
		for(String str : strArray){
			Map map = ProjectHelper.GetFileTransferInfoEx(str);
			result.add(map);
		}
		return result;
		
	}
	/**
	 * 根据filenumber获取项目锁定状态
	 */
	@RequestMapping(value = "/checkprojectlock/{prolsh}", method = RequestMethod.GET)
	@ResponseBody
	public boolean checkProjectLock (@PathVariable String prolsh,HttpServletRequest request, HttpServletResponse response){
		if(!StringHelper.isEmpty(prolsh)){
			Wfi_ProInst proinst = smproinst.GetProInstByFileNumber(prolsh);
			Wfi_ActInst actinst = ProInstService.GetNewActInst(proinst.getProinst_Id());
			return "47".equals(actinst.getOperation_Type());
		}
		return false;
	}

	/**
	 * 个人或部门办件量统计页面链接
	 * */
	@RequestMapping(value = "/projectsearchbystaffordept", method = RequestMethod.GET)
	public String projectsearchBystaffORdept() {
		return "/workflow/frame/tjprojectbyStaffOrDept";

	}
	
	/**
	 *针对部门的办件统计查询
	 *@author heks
	 *@date 2017-03-09 18:00
	 * @return
	 */
	@RequestMapping(value = "/getdeptprolist", method = RequestMethod.POST)
	@ResponseBody
	public Message getDeptProjectLists(HttpServletRequest request, HttpServletResponse response){
		String pageindex = request.getParameter("currpage");
		String pagesize = request.getParameter("pagesize");
		Map<String, String> mapCondition = new HashMap<String, String>();
		mapCondition.put("type", request.getParameter("type"));
		mapCondition.put("startDate", request.getParameter("startDate"));
		mapCondition.put("endDate", request.getParameter("endDate"));
		mapCondition.put("deptid", request.getParameter("deptid"));
		mapCondition.put("staffid", request.getParameter("staffid"));
		mapCondition.put("cxlx", request.getParameter("cxlx"));
		
		Message msg = smProInstService.getDeptProjects(mapCondition, pageindex, pagesize);
		return msg;
	}
	/**
	 * 批量收费详情页面
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/batchchargeInfo", method = RequestMethod.GET)
	public String batchchargeInfo(Model model) {
		return "/workflow/frame/batchchargeInfo";
	}
	
	/**
	 * 通过prolsh获取流程信息
	 * 
	 */
	@RequestMapping(value = "/getproinsactinst/{prolsh}",method = RequestMethod.GET)
	@ResponseBody
	public Map<String,Object> getProinsActinst(@PathVariable String prolsh, HttpServletRequest request, HttpServletResponse response){
		Wfi_ProInst proinst = smProInstService.GetProInstByLsh(prolsh);
		Map<String ,Object> map = new HashMap<String,Object>();
		if(proinst!=null){
			Map<String,String> proinfo = new HashMap<String,String>();
			Map<String,String> dbinfo = new HashMap<String,String>();
			String prodef_name = proinst.getProdef_Name();
			String djlx = prodef_name.split(",")[0];
			String djlx2 = prodef_name.substring(djlx.length()+1).replaceAll(",", "-");
			proinfo.put("DJLX", djlx);
			proinfo.put("DJLX2",djlx2);
			proinfo.put("PROINST_STATUS", proinst.getProinst_Status().toString());
			proinfo.put("PROJECT_NAME", proinst.getProject_Name());
			String project_id = proinst.getFile_Number();
			if(!StringHelper.isEmpty(project_id)){
				String url = ConfigHelper.getNameByValue("getschduleFC");				
				if(!StringHelper.isEmpty(url)){
					if(!url.substring(url.length()-1).equals("/")){
						url += "/";
					}		
				String dbinfostr = HttpRequestTools.sendGet(url+project_id,"");
				if (!StringHelper.isEmpty(dbinfostr)) {
					dbinfostr = "{" + dbinfostr + "}";

					JSONObject jsonObject = JSONObject.fromObject(dbinfostr);
					String endtime = jsonObject.getString("endtime");
					if (!StringHelper.isEmpty(endtime)) {
						endtime = endtime.substring(0, 10);
					}
					String status = jsonObject.getString("blzt");
					dbinfo.put("endtime", endtime);
					dbinfo.put("status", status);
				}
				}
			}
			map.put("xmxx", proinfo);
			map.put("jd", smProInstService.getProcessSchedule(proinst));
			map.put("dbinfo",dbinfo);
			return map;	
		}
		return null;
	}
	
	@RequestMapping(value = "/getfcbusinessinfo/{project_id}",method = RequestMethod.GET)
	@ResponseBody
	public Map<String,String> getdataTest(@PathVariable String project_id,HttpServletRequest request,HttpServletResponse response){
		Map<String,String> map = new HashMap<String,String>();
		map.put("endtime","2017-01-20 09-31-39");
		map.put("starttime","2017-01-20 09-31-39");
		map.put("blzt","2");
		return map;
	}
	
	@RequestMapping(value = "/getbatchproject/{batchnum}",method = RequestMethod.GET)
	@ResponseBody
	public List<Map> getBatchProject(@PathVariable String batchnum,HttpServletRequest request,HttpServletResponse response){
		return smProInstService.getBatchProject(batchnum);
	}
	
	//重新缮证获取缮证环节项目数据
	@RequestMapping(value = "/datamaintain",method = RequestMethod.POST)
	@ResponseBody
	public Map getProjectList(HttpServletRequest request,HttpServletResponse response){
		return smProInstService.getProjectList( request );
	}
	
	@RequestMapping(value = "/gzfzs/", method = RequestMethod.POST)
	@ResponseBody
	public Message AddFZInfos_gz(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Message msg = new Message();
		String FZRY = request.getParameter("FZRY");
		String FZSJ = request.getParameter("FZSJ");
		String LZRXM = request.getParameter("LZRXM");
		String LZRZJLB = request.getParameter("LZRZJLB");
		String LZRZJHM = request.getParameter("LZRZJHM");
		String LZRDH = request.getParameter("LZRDH");
		String LZRYB = request.getParameter("LZRYB");
		String ISPASSOVER = request.getParameter("ISPASSOVER");
		String actinstid = null;
		// String[] keys = paras.split("\\*");// 多个记录之间键值对用*分隔
		String outcertinfolist = request.getParameter("certinfolist");
		List list = new ArrayList();
		if (!StringHelper.isEmpty(outcertinfolist)) {
			String[] keys = outcertinfolist.split("\\*");// 多个记录之间键值对用*分隔
			
			for (String key : keys) {
				Map map = new HashMap();
				String ywlsh = null;
				String xmbh2 = key.split(":")[0];
				String hfzshs = key.split(":")[1];
				BDCS_XMXX xmxx = null;
				if (xmxx == null) {
					xmxx = Global.getXMXXbyXMBH(xmbh2);
				}
				if (!StringHelper.isEmpty(xmxx.getYWLSH())) {
					ywlsh = xmxx.getYWLSH();
				}
				List<String> listactinst = ProInstService.getActinstid(ywlsh);
				actinstid= listactinst.get(0);
				String operaStaffString = smStaff.getCurrentWorkStaffID();// 当前操作人员
				if(listactinst.get(1)!= null && !listactinst.get(1).equals("") &&listactinst.get(1).equals("1")){
					smactinst.SetActinstWorkStaff(actinstid, operaStaffString);
				}
				
				String[] strarr = hfzshs.split(",");
			for(String hfzsh : strarr){
				String condition = MessageFormat.format("XMBH=''{0}'' AND HFZSH=''{1}''", xmbh2, hfzsh);
				BDCS_DJFZ djfz = zsService.getDJFZ(condition);

				if (djfz == null) {
					djfz = new BDCS_DJFZ();
					djfz.setId((String) SuperHelper.GeneratePrimaryKey());
				}
				djfz.setYWH(xmxx == null ? "" : xmxx.getPROJECT_ID());
				djfz.setFZRY(FZRY);
				djfz.setFZSJ(StringHelper.FormatByDate(FZSJ));
				djfz.setLZRXM(LZRXM);
				if (LZRZJLB.equals("身份证")) {
					LZRZJLB = "1";
				} else if (LZRZJLB.equals("军官证")) {
					LZRZJLB = "2";
				}
				djfz.setLZRZJLB(LZRZJLB);
				djfz.setLZRZJHM(LZRZJHM);
				djfz.setLZRDH(LZRDH);
				djfz.setLZRYB(LZRYB);
				djfz.setXMBH(xmbh2);
				djfz.setHFZSH(hfzsh);
				djfz.setYSDM("1");// TODO YSDM为必填项，暂时赋值为1
				zsService.AddFZXX(djfz);
				map.put("ywlsh", ywlsh);
				map.put("success", "true");
				//TODO:是否转出操作
			
				if(ISPASSOVER.equals("true")){
					Wfi_ActInst actinst = ProInstService.getFzActinst(ywlsh);
					if(actinst!=null){
						//转出操作
						try {
							SmObjInfo info = null;
							List<SmObjInfo> infos = smStaff.GetActStaffByActInst(actinst.getActinst_Id());
							if (infos != null && infos.size() > 0) {
								SmObjInfo route = infos.get(0);
								String routeidString = route.getID();
								SmObjInfo staff = ProInstService.getBatchPassoverStaff(infos);
								List<SmObjInfo> objInfos = new ArrayList<SmObjInfo>();
								if (staff != null) {
									String typeString = staff.getDesc();
								if (typeString.equals("Staff")) {// 按照员工转出
									     objInfos.add(staff);
								} else if (typeString.equals("Dept")) {// 按照部门
									List<User> users = userServive.findUserByDepartmentId(staff.getID());
									if (users != null && users.size() > 0) {
										for (User user : users) {
											SmObjInfo objInfo = new SmObjInfo();
											objInfo.setID(user.getId());// 设置staffid
											objInfo.setName(user.getUserName());// 设置staffName
											objInfos.add(objInfo);
										}
									}
								} else if (typeString.equals("Role")) {// 按照角色
									List<User> users = roleService.findUsersByRoleId(staff.getID());
									if (users != null && users.size() > 0) {
										for (User user : users) {
											SmObjInfo objInfo = new SmObjInfo();
											objInfo.setID(user.getId());// 设置staffid
											objInfo.setName(user.getUserName());// 设置staffName
											objInfos.add(objInfo);
										}
									  }
								    }
//									String operaStaffString = smStaff.getCurrentWorkStaffID();
									info = operationService.PassOver(
											routeidString,actinst.getActinst_Id(), 
											objInfos,operaStaffString,"", false);
								}
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
				list.add(map);
			}
			}
		} else {

		}
		msg.setRows(list);
		YwLogUtil.addYwLog("批量添加发证-成功", ConstValue.SF.YES.Value, ConstValue.LOG.ADD);
		return msg;
	}
	
	
	@RequestMapping(value = "/gzexcel/export", method = RequestMethod.POST)
	@ResponseBody
	public String excelExport_gz(HttpServletRequest request,
			HttpServletResponse response,QueryCriteria query) throws IOException  {
		String prolsh=request.getParameter("prolsh");
		String operaStaffString = smStaff.getCurrentWorkStaffID();// 当前操作人员
		String url = "";
		if(prolsh!=null&&!prolsh.equals("")){
//				Message m = new Message();
				String[] strarr = prolsh.split(",");
				 List<Map> datas = new ArrayList<Map>();
				for(String str :strarr){
					 Message m = new Message();
					 m = ProInstService.getPLFZProjectList(operaStaffString, null, 3, str, 1, 100,null,query);
					 List<Map> rows=(List<Map>)m.getRows();
					 datas.addAll(rows);
	
				}
			   
			   
//				if ( m!=null && m.getRows()!=null){
//					datas=(List<Map>)m.getRows();
//				}
				String basePath = request.getRealPath("/") + "\\resources\\PDF";
				String outpath = "";
				String tmpFullName = "";
				FileOutputStream outstream = null;
				//if(datas != null && datas.size() > 0){
					outpath = basePath + "\\tmp\\bdcqzlq.xls";
					url = request.getContextPath() + "\\resources\\PDF\\tmp\\bdcqzlq.xls";
				    outstream = new FileOutputStream(outpath); 
				    tmpFullName = request.getRealPath("/WEB-INF/jsp/wjmb/bdcqzlq.xls");
				    InputStream input = new FileInputStream(tmpFullName);
					HSSFWorkbook  wb = new HSSFWorkbook(input);
					HSSFSheet sheet = wb.getSheetAt(0);
					Map<String,Integer> MapCol = new HashMap<String,Integer>();				
					MapCol.put("序号", 0);
					MapCol.put("受理编号", 1);
					MapCol.put("权利人",2);
					MapCol.put("义务人",3);
					MapCol.put("坐落",4);
					MapCol.put("权证号",5);
					MapCol.put("备注", 6);
					//获取当前时间
					Date date=new Date();
					DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					String time=format.format(date);
		            int rownum = 2;
		            String strqlr = null;
		            String zllenth = null;
		            String zhlenth = null;
			        HSSFCellStyle bodyStyle = wb.createCellStyle();
			        HSSFFont font = wb.createFont();  
			        font.setFontName("宋体");  
			        font.setFontHeightInPoints((short) 15); 
			        bodyStyle.setFont(font); 
//			        bodyStyle.setBottomBorderColor(HSSFColor.BLACK.index); // 设置单元格的边框颜色． 
			        bodyStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 左右居中  
			        bodyStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 上下居中  
			        bodyStyle.setWrapText(true);  
			        bodyStyle.setLeftBorderColor(HSSFColor.BLACK.index);  
			        bodyStyle.setBorderLeft((short) 1);  
			        bodyStyle.setRightBorderColor(HSSFColor.BLACK.index);  
			        bodyStyle.setBorderRight((short) 1);  
			        bodyStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 设置单元格的边框为粗体  
			        bodyStyle.setBottomBorderColor(HSSFColor.BLACK.index); // 设置单元格的边框颜色．  
			        bodyStyle.setFillForegroundColor(HSSFColor.WHITE.index);// 设置单元格的背景颜色．
			        String username = Global.getCurrentUserName();
					for(Map djdy:datas){
				  		 HSSFRow row = (HSSFRow)sheet.createRow(rownum);
						  	
				  		 try{
					         HSSFCell Cell0 = row.createCell(MapCol.get("序号"));
					         Cell0.setCellValue(rownum-1);
					         Cell0.setCellStyle(bodyStyle);
					         HSSFCell Cell1 = row.createCell(MapCol.get("受理编号"));
					         String slbh =StringHelper.formatObject(djdy.get("ywlsh"));
					         Cell1.setCellValue(new HSSFRichTextString(slbh));
					         Cell1.setCellStyle(bodyStyle);
					         HSSFCell Cell2 = row.createCell(MapCol.get("权利人"));
					         
					         String strr = StringHelper.formatObject(djdy.get("QLR"));
//					         if(strr.length()>12){
//					        	 String str1 = strr.substring(0,12);
//					        	 String str2 = strr.substring(12,strr.length());
//					        	 strqlr = str1+"\r\n"+str2;
//						         Cell2.setCellValue(new HSSFRichTextString(strqlr ));
//
//					         }else{
						     Cell2.setCellValue(new HSSFRichTextString(strr));
//					         }
					         Cell2.setCellStyle(bodyStyle);
					         HSSFCell Cell3 = row.createCell(MapCol.get("义务人"));
					         String st = StringHelper.formatObject(djdy.get("YWR"));
//					         if(strr.length()>12){
//					        	 String str1 = strr.substring(0,12);
//					        	 String str2 = strr.substring(12,strr.length());
//					        	 strqlr = str1+"\r\n"+str2;
//						         Cell2.setCellValue(new HSSFRichTextString(strqlr ));
//
//					         }else{
						     Cell3.setCellValue(new HSSFRichTextString(st));
//					         }
					         Cell3.setCellStyle(bodyStyle);
					         HSSFCell Cell4 = row.createCell(MapCol.get("坐落"));
					         String strzl = StringHelper.formatObject(djdy.get("zl"));
//					         if(strzl.length()>24){
//					        	 String strzl1 = strzl.substring(0,25);
//					        	 String strzl2 = strzl.substring(25,strzl.length());
//					        	 zllenth = strzl1+"\r\n"+strzl2;
//						         Cell3.setCellValue(new HSSFRichTextString(zllenth ));
//
//					         }else{
						      Cell4.setCellValue(new HSSFRichTextString(strzl ));

//					         }
					         Cell4.setCellStyle(bodyStyle);
					     
					         HSSFCell Cell5 = row.createCell(MapCol.get("权证号"));
					         String strzh = StringHelper.formatObject(djdy.get("BDCQZH"));
//					         if(strzh.length()>10){
//					        	 String strzh1 = strzh.substring(0,10);
//					        	 String strzh2 = strzh.substring(10,strzh.length());
//					        	 zhlenth = strzh1+"\r\n"+strzh2;
//						         Cell4.setCellValue(new HSSFRichTextString(zhlenth ));
//
//					         }else{
						       Cell5.setCellValue(new HSSFRichTextString(strzh ));

//					         }
					         Cell5.setCellStyle(bodyStyle);
					         HSSFCell Cell6 = row.createCell(MapCol.get("备注"));
					         Cell6.setCellValue(StringHelper.formatObject(""));
					         Cell6.setCellStyle(bodyStyle);
					         rownum++ ;
				  		 }
				  		
				  		 catch(Exception ex){
				  		 }
				  		
					  }	
					 HSSFRow rows = (HSSFRow)sheet.createRow(rownum+2);
					 HSSFRow row = (HSSFRow)sheet.createRow(rownum+3);
			  		 HSSFCell CellFZ =   rows.createCell(MapCol.get("权利人"));
				     HSSFCellStyle Style = wb.createCellStyle();
				     Style.setFont(font);
			  		 Style.setRightBorderColor(HSSFColor.WHITE.index);// 右边框的颜色  
			  		 CellFZ.setCellValue(new HSSFRichTextString("发证人:"+username+""));
			  		 CellFZ.setCellStyle(Style);
			  		 HSSFCell CellRY =   rows.createCell(MapCol.get("坐落"));
				     Style.setFont(font);
//			  		 Style.setRightBorderColor(HSSFColor.WHITE.index);// 右边框的颜色  
				     CellRY.setCellValue(new HSSFRichTextString("                 领证人:"));
				     CellRY.setCellStyle(Style);
			  		 HSSFCell CellLZ =   rows.createCell(MapCol.get("权证号"));
			  		 CellLZ.setCellValue(new HSSFRichTextString("           发证日期:"+time+""));
			  		 CellLZ.setCellStyle(Style);
			  		HSSFCell CellSFZ =   row.createCell(MapCol.get("坐落"));
				     Style.setFont(font);
//			  		 Style.setRightBorderColor(HSSFColor.WHITE.index);// 右边框的颜色  
				     CellSFZ.setCellValue(new HSSFRichTextString("                 身份证:"));
				     CellSFZ.setCellStyle(Style);
					 wb.write(outstream); 
					 outstream.flush(); 
					 outstream.close();
				// }
		
	          }
		return url;
	}
	
	@RequestMapping(value = "/gzexcel/export1", method = RequestMethod.POST)
	@ResponseBody
	public String excelExport1_gz(HttpServletRequest request,
			HttpServletResponse response,QueryCriteria query) throws IOException  {
		String prolsh=request.getParameter("prolsh");
		String operaStaffString = smStaff.getCurrentWorkStaffID();// 当前操作人员
		String url = "";
		if(prolsh!=null&&!prolsh.equals("")){
//				Message m = new Message();
				String[] strarr = prolsh.split(",");
				 List<Map> datas = new ArrayList<Map>();
				for(String str :strarr){
					 Message m = new Message();
					 m = ProInstService.getPLFZProjectList(operaStaffString, null, 3, str, 1, 100,null,query);
					 List<Map> rows=(List<Map>)m.getRows();
					 datas.addAll(rows);
	
				}
			   
			   
//				if ( m!=null && m.getRows()!=null){
//					datas=(List<Map>)m.getRows();
//				}
				String basePath = request.getRealPath("/") + "\\resources\\PDF";
				String outpath = "";
				String tmpFullName = "";
				FileOutputStream outstream = null;
				//if(datas != null && datas.size() > 0){
					outpath = basePath + "\\tmp\\bdcqzyj.xls";
					url = request.getContextPath() + "\\resources\\PDF\\tmp\\bdcqzyj_gz.xls";
				    outstream = new FileOutputStream(outpath); 
				    tmpFullName = request.getRealPath("/WEB-INF/jsp/wjmb/bdcqzyj_gz.xls");
				    InputStream input = new FileInputStream(tmpFullName);
					HSSFWorkbook  wb = new HSSFWorkbook(input);
					HSSFSheet sheet = wb.getSheetAt(0);
					Map<String,Integer> MapCol = new HashMap<String,Integer>();				
					MapCol.put("序号", 0);
					MapCol.put("受理编号", 1);
					MapCol.put("权利人",2);
					MapCol.put("义务人",3);
					MapCol.put("坐落",4);
					MapCol.put("权证号",5);
					MapCol.put("备注", 6);
					//获取当前时间
					Date date=new Date();
					DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					String time=format.format(date);
		            int rownum = 2;
		            String strqlr = null;
		            String zllenth = null;
		            String zhlenth = null;
			        HSSFCellStyle bodyStyle = wb.createCellStyle();
			        HSSFFont font = wb.createFont();  
			        font.setFontName("宋体");  
			        font.setFontHeightInPoints((short) 15); 
			        bodyStyle.setFont(font); 
//			        bodyStyle.setBottomBorderColor(HSSFColor.BLACK.index); // 设置单元格的边框颜色． 
			        bodyStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 左右居中  
			        bodyStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 上下居中  
			        bodyStyle.setWrapText(true);  
			        bodyStyle.setLeftBorderColor(HSSFColor.BLACK.index);  
			        bodyStyle.setBorderLeft((short) 1);  
			        bodyStyle.setRightBorderColor(HSSFColor.BLACK.index);  
			        bodyStyle.setBorderRight((short) 1);  
			        bodyStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 设置单元格的边框为粗体  
			        bodyStyle.setBottomBorderColor(HSSFColor.BLACK.index); // 设置单元格的边框颜色．  
			        bodyStyle.setFillForegroundColor(HSSFColor.WHITE.index);// 设置单元格的背景颜色．
			        String username = Global.getCurrentUserName();
					for(Map djdy:datas){
				  		 HSSFRow row = (HSSFRow)sheet.createRow(rownum);
				  	
				  		 try{
					         HSSFCell Cell0 = row.createCell(MapCol.get("序号"));
					         Cell0.setCellValue(rownum-1);
					         Cell0.setCellStyle(bodyStyle);
					         HSSFCell Cell1 = row.createCell(MapCol.get("受理编号"));
					         String slbh =StringHelper.formatObject(djdy.get("ywlsh"));
					         Cell1.setCellValue(new HSSFRichTextString(slbh));
					         Cell1.setCellStyle(bodyStyle);
					         HSSFCell Cell2 = row.createCell(MapCol.get("权利人"));
					         
					         String strr = StringHelper.formatObject(djdy.get("QLR"));
//					         if(strr.length()>12){
//					        	 String str1 = strr.substring(0,12);
//					        	 String str2 = strr.substring(12,strr.length());
//					        	 strqlr = str1+"\r\n"+str2;
//						         Cell2.setCellValue(new HSSFRichTextString(strqlr ));
//
//					         }else{
						     Cell2.setCellValue(new HSSFRichTextString(strr));
//					         }
					         Cell2.setCellStyle(bodyStyle);
					         HSSFCell Cell3 = row.createCell(MapCol.get("义务人"));
					         String st = StringHelper.formatObject(djdy.get("YWR"));
//					         if(strr.length()>12){
//					        	 String str1 = strr.substring(0,12);
//					        	 String str2 = strr.substring(12,strr.length());
//					        	 strqlr = str1+"\r\n"+str2;
//						         Cell2.setCellValue(new HSSFRichTextString(strqlr ));
//
//					         }else{
						     Cell3.setCellValue(new HSSFRichTextString(st));
//					         }
					         Cell3.setCellStyle(bodyStyle);
					         HSSFCell Cell4 = row.createCell(MapCol.get("坐落"));
					         String strzl = StringHelper.formatObject(djdy.get("zl"));
//					         if(strzl.length()>24){
//					        	 String strzl1 = strzl.substring(0,25);
//					        	 String strzl2 = strzl.substring(25,strzl.length());
//					        	 zllenth = strzl1+"\r\n"+strzl2;
//						         Cell3.setCellValue(new HSSFRichTextString(zllenth ));
//
//					         }else{
						      Cell4.setCellValue(new HSSFRichTextString(strzl ));

//					         }
					         Cell4.setCellStyle(bodyStyle);
					     
					         HSSFCell Cell5 = row.createCell(MapCol.get("权证号"));
					         String strzh = StringHelper.formatObject(djdy.get("BDCQZH"));
//					         if(strzh.length()>10){
//					        	 String strzh1 = strzh.substring(0,10);
//					        	 String strzh2 = strzh.substring(10,strzh.length());
//					        	 zhlenth = strzh1+"\r\n"+strzh2;
//						         Cell4.setCellValue(new HSSFRichTextString(zhlenth ));
//
//					         }else{
						       Cell5.setCellValue(new HSSFRichTextString(strzh ));

//					         }
					         Cell5.setCellStyle(bodyStyle);
					         HSSFCell Cell6 = row.createCell(MapCol.get("备注"));
					         Cell6.setCellValue(StringHelper.formatObject(""));
					         Cell6.setCellStyle(bodyStyle);
					         rownum++ ;
				  		 }
				  		
				  		 catch(Exception ex){
				  		 }
				  		
					  }	
					 HSSFRow rows = (HSSFRow)sheet.createRow(rownum+2);
			  		 HSSFCell CellFZ =   rows.createCell(MapCol.get("权利人"));
				     HSSFCellStyle Style = wb.createCellStyle();
				     Style.setFont(font);
			  		 Style.setRightBorderColor(HSSFColor.WHITE.index);// 右边框的颜色  
			  		 CellFZ.setCellValue(new HSSFRichTextString("移交人:"+username+""));
			  		 CellFZ.setCellStyle(Style);
			  		 HSSFCell CellRY =   rows.createCell(MapCol.get("坐落"));
				     Style.setFont(font);
//			  		 Style.setRightBorderColor(HSSFColor.WHITE.index);// 右边框的颜色  
				     CellRY.setCellValue(new HSSFRichTextString("                 接收人:"));
				     CellRY.setCellStyle(Style);
			  		 HSSFCell CellLZ =   rows.createCell(MapCol.get("权证号"));
			  		 CellLZ.setCellValue(new HSSFRichTextString("           日期:"+time+""));
			  		 CellLZ.setCellStyle(Style);
					 wb.write(outstream); 
					 outstream.flush(); 
					 outstream.close();
				// }
		
	          }
		return url;
	}
	
	/**
	 * 督办项目列表
	 * @param request
	 * @param response
	 * @param query
	 * @return
	 * @throws UnsupportedEncodingException 
	 * @throws IOException
	 */
	@RequestMapping(value = "/dbproject/search", method = RequestMethod.GET)
	@ResponseBody
	public Message dbprojectList(HttpServletRequest request,HttpServletResponse response) throws UnsupportedEncodingException {
		String searchValue = RequestHelper.getParam(request, "value");
		String pagesize = request.getParameter("pagesize");
		String currentPage = request.getParameter("currpage");
		return smProInstService.getDbprojectList(searchValue, pagesize, currentPage);
	}
	
	/**
	 * 督办项目导出
	 * @throws IOException 
	 */
	@RequestMapping(value = "/dbproject/downexcel", method = RequestMethod.POST)
	@ResponseBody
	public String downExcel(HttpServletRequest request) throws IOException{
		Message data = smProInstService.getDbprojectList( null , "1000", "1");
		if(data!=null && data.getRows()!=null&&data.getRows().size()>0){
		List<Map<String,String>> djdys = (List<Map<String, String>>) data.getRows();
			LinkedHashMap<String,String> map= new LinkedHashMap<String,String>();
			String basePath = request.getRealPath("/") + "\\resources\\PDF";
			String url = request.getContextPath() + "\\resources\\PDF\\tmp\\dbproject.xls";
			String outpath = basePath + "\\tmp\\dbproject.xls";
			String tmpFullName = request.getRealPath("/WEB-INF/jsp/wjmb/dbproject.xls");
				 map.put("受理编号", "PROLSH");
				 map.put("项目名称", "PROJECT_NAME");
				 map.put("流程名称", "PRODEF_NAME");  
				 map.put("办理活动", "ACTINST_NAME"); 
				 map.put("活动结束时间", "PROINST_WILLFINISH");
			return smMaterialService.excelDownload(url, outpath, tmpFullName, map, djdys);
		}
		return null;
	}
	
	/**
	 * 办件量统计（广西）个人统计
	 * @author liangc
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/getpersonprolist", method = RequestMethod.POST)
	@ResponseBody
	public Message getPersonProjectLists(HttpServletRequest request, HttpServletResponse response){
		String keyString = request.getParameter("value");
		String statuString = request.getParameter("status");
		String startString = request.getParameter("start");
		String endString = request.getParameter("end");
		String pageindex = request.getParameter("currpage");
		String pagesize = request.getParameter("pagesize");
		String staffid = request.getParameter("staffid");
		String cxlx = request.getParameter("cxlx");
		
		Message msg = smProInstService.getPersonProjects(cxlx,staffid, pageindex, pagesize, startString, endString);
		return msg;
	}
	
	/**
	 * 获取申请挂起环节权限
	 * @author 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/getQSGJ/{actinstid}", method = RequestMethod.POST)
	@ResponseBody
	public Message getQSGJ(@PathVariable String actinstid,HttpServletRequest request, HttpServletResponse response){
		Message msg = new Message();
		msg.setDesc("ture");
		msg.setTotal(0);
		//后续可以添加开启开关，去掉行政区划划分
		String xzqhdm=ConfigHelper.getNameByValue("XZQHDM");
		if(xzqhdm.contains("4509")){//玉林
			List<Map>m=baseCommonDao.getDataListByFullSql("select t.value from  SMWB_SUPPORT.t_Config t where t.classname='基本参数类' and t.name='SQGJ' ");
			if(!m.isEmpty()){
			msg.setTotal(1);
			String sqgjs=m.get(0).get("VALUE").toString();
			sqgjs.replace("，", ",");
			String[] sqgj=sqgjs.split(",");
			List<Map> actinst_name=	baseCommonDao.getDataListByFullSql("select  t.actinst_name from BDC_WORKFLOW.WFI_ACTINST t  where t.actinst_id='"+actinstid+"' ");
			for (String s : sqgj) {
				if(actinst_name.get(0).get("ACTINST_NAME").toString().equals(s)){
					msg.setDesc("false");
					 return msg;
				}
				
				}
			 }
			}
		
		return msg;
	}
	
	/**
	 * 网签页面 JOE
	 * */
	@RequestMapping(value = "/projecthousenet", method = RequestMethod.GET)
	public String ShowProjectHouseNet(Model model) {
		return "/workflow/frame/projecthousenet";
	}
	/**
	 *税务填写页面
	 * */
	@RequestMapping(value = "/projectlistfortax", method = RequestMethod.GET)
	public String ShowProjectTaxList(Model model) {
		return "/workflow/frame/projectlistfortax";
	}
	// 查看详细页面税务
	@RequestMapping(value = "/projectdetailfortax/{actinstid}", method = RequestMethod.GET)
	public String ShowDatailForTax(@PathVariable String actinstid, HttpServletRequest request, HttpServletResponse response) {
		// 判断是否有权限查看此模块
		return "/workflow/frame/projectdetailfortax";
	}
	// 查看详细页面网签
		@RequestMapping(value = "/projectdetailforhousenet/{actinstid}", method = RequestMethod.GET)
		public String ShowDatailForHouseNet(@PathVariable String actinstid, HttpServletRequest request, HttpServletResponse response) {
			// 判断是否有权限查看此模块
			return "/workflow/frame/projectdetailforhousenet";
		}

	//网签列表
	@RequestMapping(value = "/projecthousenetlist", method = RequestMethod.POST)
	@ResponseBody
	public Message getProjectHouseNetList(HttpServletRequest request, HttpServletResponse response,QueryCriteria query) throws UnsupportedEncodingException {
		String keyString = RequestHelper.getParam(request, "value");
		String statuString = RequestHelper.getParam(request, "status");
		String startString = RequestHelper.getParam(request, "lshstart");
		String endString = RequestHelper.getParam(request, "lshend");
		String pageindex = RequestHelper.getParam(request, "currpage");
		String pagesize = RequestHelper.getParam(request, "pagesize");
		String actdefname = RequestHelper.getParam(request, "actdefname");
		String prodefname = RequestHelper.getParam(request, "prodefname");
		String staffanme = RequestHelper.getParam(request, "staffanme");
		String sqr = RequestHelper.getParam(request, "sqr");
		String urgency = RequestHelper.getParam(request, "urgency");// 紧急程度
		String outtime = RequestHelper.getParam(request, "outtime");// 超期
		String passback = RequestHelper.getParam(request, "passback");// 驳回
		String proStatus = RequestHelper.getParam(request, "prostatus"); // 即将超期，已超期
		String flag = RequestHelper.getParam(request, "flag"); // 待办已办标识
		
		if (!StringHelper.isEmpty(pageindex)) {
			query.setCurrentPageIndex(pageindex);
		}
		if (!StringHelper.isEmpty(sqr)) {
			query.setSqr(sqr);
		}
		if (!StringHelper.isEmpty(pagesize)) {
			query.setPageSize(pagesize);
		}
		if (!StringHelper.isEmpty(statuString)) {
			query.setStatus(statuString);
		}
		if (!StringHelper.isEmpty(endString)) {
			query.setLshEnd(endString);
		}
		if (!StringHelper.isEmpty(staffanme)) {
			query.setStaff(staffanme);
		}
		if (!StringHelper.isEmpty(startString)) {
			query.setLshStart(startString);
		}
		
		if (query.getCurrentPageIndex() != null) {
			List<String> workflowname=new ArrayList<String>();
			String wcodes= ConfigHelper.getNameByValue("HOUSENETSET-WORKFLOWCODE");
			if(wcodes!=null&&wcodes!="")
			{
				for(String w:wcodes.split(","))
				{
					workflowname.add(w);
				}
			}
			return ProInstService.getAllProjectListForHouseOffice(query,workflowname,flag);
		} else {
			return null;
		}

	}
	
//税务列表
	@RequestMapping(value = "/projecttaxlist", method = RequestMethod.POST)
	@ResponseBody
	public Message getProjectTaxList(HttpServletRequest request, HttpServletResponse response,QueryCriteria query) throws UnsupportedEncodingException {
		String keyString = RequestHelper.getParam(request, "value");
		String statuString = RequestHelper.getParam(request, "status");
		String startString = RequestHelper.getParam(request, "lshstart");
		String endString = RequestHelper.getParam(request, "lshend");
		String pageindex = RequestHelper.getParam(request, "currpage");
		String pagesize = RequestHelper.getParam(request, "pagesize");
		String actdefname = RequestHelper.getParam(request, "actdefname");
		String prodefname = RequestHelper.getParam(request, "prodefname");
		String staffanme = RequestHelper.getParam(request, "staffanme");
		String sqr = RequestHelper.getParam(request, "sqr");
		String urgency = RequestHelper.getParam(request, "urgency");// 紧急程度
		String outtime = RequestHelper.getParam(request, "outtime");// 超期
		String passback = RequestHelper.getParam(request, "passback");// 驳回
		String proStatus = RequestHelper.getParam(request, "prostatus"); // 即将超期，已超期
		String flag = RequestHelper.getParam(request, "flag"); // 待办已办标识

		if (!StringHelper.isEmpty(pageindex)) {
			query.setCurrentPageIndex(pageindex);
		}
		if (!StringHelper.isEmpty(sqr)) {
			query.setSqr(sqr);
		}
		if (!StringHelper.isEmpty(pagesize)) {
			query.setPageSize(pagesize);
		}
		if (!StringHelper.isEmpty(statuString)) {
			query.setStatus(statuString);
		}
		if (!StringHelper.isEmpty(endString)) {
			query.setLshEnd(endString);
		}
		if (!StringHelper.isEmpty(staffanme)) {
			query.setStaff(staffanme);
		}
		if (!StringHelper.isEmpty(startString)) {
			query.setLshStart(startString);
		}
		
		if (query.getCurrentPageIndex() != null) {
			List<String> workflowname=new ArrayList<String>();
			String wcodes= ConfigHelper.getNameByValue("TAXSET-WORKFLOWCODE");
			if(wcodes!=null&&wcodes!="")
			{
				for(String w:wcodes.split(","))
				{
					workflowname.add(w);
				}
			}
			return ProInstService.getAllProjectListForHouseOffice(query,workflowname, flag);
		} else {
			return null;
		}

	}
	//填入网签信息
	@RequestMapping(value = "/writehouseinfo", method = RequestMethod.POST)
	@ResponseBody
	public Map writehouseinfo(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		String xmbh = RequestHelper.getParam(request,"xmbh");
		String qlid = RequestHelper.getParam(request,"qlid");
		String htbh = RequestHelper.getParam(request,"htbh");
		String htsj = RequestHelper.getParam(request,"htsj");
		String basj = RequestHelper.getParam(request,"basj");
		String htcjjg = RequestHelper.getParam(request,"htcjjg");
		Map<String,String> param = new HashMap<String,String>();
		param.put("xmbh", xmbh);
		param.put("qlid", qlid);
		param.put("htbh", htbh);
		param.put("htsj", htsj);
		param.put("basj", basj);
		param.put("htcjjg", htcjjg);
			//workflowname
			return ProInstService.writehouseinfo(xmbh,param,qlid);
		

	}
	
	/**
	 * 
	 * @Description: 添加网签审核意见
	 * @author suhaibin
	 * @date 2019年6月6日 下午4:13:02
	 */
	@RequestMapping(value = "/auditOpinions",method = RequestMethod.POST)
	public @ResponseBody Map updateAuditOpinions(HttpServletRequest request,HttpServletResponse response) throws UnsupportedEncodingException{
		
		String xmbh = RequestHelper.getParam(request, "xmbh");
		String AuditOpinions = RequestHelper.getParam(request, "AuditOpinions");
		
		return ProInstService.updateAuditOpinions(xmbh, AuditOpinions);
		
	}
	
	@RequestMapping(value = "/loadProinstDate",method = RequestMethod.POST)
	public @ResponseBody Wfi_ProinstDate loadProinstDate(HttpServletRequest request,HttpServletResponse response) throws UnsupportedEncodingException {
		
		String xmbh = RequestHelper.getParam(request, "xmbh");
		
		return ProInstService.loadProinstDate(xmbh);
		
	}
	
	
	//填入税务信息
	@RequestMapping(value = "/writetaxinfo", method = RequestMethod.POST)
	@ResponseBody
	public Map writetaxinfo(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		String xmbh = RequestHelper.getParam(request,"xmbh");
		String qlid = RequestHelper.getParam(request,"qlid");
		String data = RequestHelper.getParam(request,"data");
		//workflowname
		return ProInstService.writetaxinfo(xmbh, qlid, data);
	}
	//重新推送信息给住建部门
	@RequestMapping(value = "/refreshapi", method = RequestMethod.POST)
	@ResponseBody
	public Map refreshapi(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		String ywh = RequestHelper.getParam(request,"ywh");


		return ProInstService.refreshapi(ywh);
	}
	
	
	@RequestMapping(value = "/netAuditOpinionsIndex",method = RequestMethod.GET)
	public String netAuditOpinionsIndex(Model model){
		
		return "/realestate/registration/modules/common/netAuditOpinions";
		
	}
	
	//获取网签和税务受理的待办件数
	@RequestMapping(value = "/getProjectjs", method = RequestMethod.GET)
	@ResponseBody
	public Message getUserRole() {
		Message m = new Message();
		User user = smStaff.getCurrentWorkStaff();
		boolean flag = false;
	    StringBuilder str = new StringBuilder();
	    str.append("1>0 ");
	    String deptid = user.getId();
		if (deptid != null && !deptid.equals("")) {
			StringBuilder sql = new StringBuilder();
			sql.append("select u.username,u.id as userid,r.rolename as rolename,r.id as roleid ");
			sql.append("from rt_userrole rt ");
			sql.append("inner join t_role r ");
			sql.append("on rt.roleid=r.id ");
			sql.append("inner join t_user u ");
			sql.append("on rt.userid=u.id ");
			sql.append("where u.id='"+deptid+"'");
			List<Map> list=baseCommonDao.getDataListByFullSql(sql.toString());
			if (list.size()>0) {
				for (Map map : list) {
					if ("".equals(map.get("rolename"))) {
						flag = true;
						break;
					}
					if ("一窗受理-网签".equals(map.get("ROLENAME"))) {
						str.append("and EXISTS (SELECT 1 FROM " +
			                    " BDCK.BDCS_XMXX XMXX LEFT JOIN BDCK.BDCS_FSQL_GZ FSQL" +
			                    " ON FSQL.XMBH = XMXX.XMBH" +
			                    " WHERE FSQL.HTBH IS NULL  and XMXX.PROJECT_ID = PO.FILE_NUMBER and  XMXX.SFDB<>1)"
			                    + "and exists  (select 1 from bdc_workflow.WFD_MAPPING maping left join " + 
			                    "BDC_WORKFLOW.WFD_PRODEF PRODEF on maping.WORKFLOWCODE=PRODEF.PRODEF_CODE " + 
			                    "where PRODEF.HOUSE_STATUS='1' and instr(PO.FILE_NUMBER,'-'||maping.workflowcode||'-')>0 and maping.WORKFLOWNAME in ('ZY002','ZY020') )");
						flag = true;
						break;
					}
					if ("一窗受理-税务".equals(map.get("ROLENAME"))) {
						str.append("and EXISTS (SELECT 1" +
			                    " FROM BDCK.BDCS_XMXX XMXX LEFT JOIN BDCK.BDCS_FSQL_GZ FSQL" +
			                    " ON FSQL.XMBH = XMXX.XMBH" +
			                    " WHERE FSQL.WSPZH IS NULL AND XMXX.PROJECT_ID = PO.FILE_NUMBER  and  XMXX.SFDB<>1)"
			                    + "and exists  (select 1 from bdc_workflow.WFD_MAPPING maping left join " + 
			                    "BDC_WORKFLOW.WFD_PRODEF PRODEF on maping.WORKFLOWCODE=PRODEF.PRODEF_CODE " + 
			                    "where PRODEF.HOUSE_STATUS='1' and instr(PO.FILE_NUMBER,'-'||maping.workflowcode||'-')>0 and maping.WORKFLOWNAME in ('ZY002','ZY020') )");
						flag = true;
						break;
					}
				}
			}
		}
		
		if (flag) {
			long tatalCount = baseCommonDao.getCountByFullSql("from "+Common.WORKFLOWDB+"WFI_PROINST po where " + str.toString());
			m.setDesc("true");
			m.setTotal(tatalCount);
		}else {
			m.setDesc("false");
		}
		return m;
	}
}
