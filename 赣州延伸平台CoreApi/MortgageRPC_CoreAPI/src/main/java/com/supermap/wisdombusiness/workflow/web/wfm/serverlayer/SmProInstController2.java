package com.supermap.wisdombusiness.workflow.web.wfm.serverlayer;

import java.io.IOException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.axis.encoding.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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

import com.supermap.realestate.registration.service.ChargeService;
import com.supermap.realestate.registration.service.DBService;
import com.supermap.realestate.registration.service.DJBService;
import com.supermap.realestate.registration.service.ProjectService;
import com.supermap.realestate.registration.service.ZSService;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import com.supermap.wisdombusiness.framework.service.RoleService;
import com.supermap.wisdombusiness.framework.service.UserService;
import com.supermap.wisdombusiness.workflow.model.Wfi_MaterData;
import com.supermap.wisdombusiness.workflow.service.common.SmObjInfo;
import com.supermap.wisdombusiness.workflow.service.common.SmProInfo;
import com.supermap.wisdombusiness.workflow.service.wfi.SmActInst;
import com.supermap.wisdombusiness.workflow.service.wfi.SmProInst;
import com.supermap.wisdombusiness.workflow.service.wfi.SmProMater;
import com.supermap.wisdombusiness.workflow.service.wfi.SmStaff;
import com.supermap.wisdombusiness.workflow.service.wfi.SmSysMod;
import com.supermap.wisdombusiness.workflow.service.wfi.proinstStateModify;
import com.supermap.wisdombusiness.workflow.service.wfm.OperationService;
import com.supermap.wisdombusiness.workflow.service.wfm.SmProDefService;
import com.supermap.wisdombusiness.workflow.service.wfm.SmProInstEXCELService;
import com.supermap.wisdombusiness.workflow.service.wfm.SmProInstService;
import com.supermap.wisdombusiness.workflow.service.wfm.SmProMaterService;
import com.supermap.wisdombusiness.workflow.util.FileUpload;
@Controller
@RequestMapping("/frame")
public class SmProInstController2 {
	private static final Log logger = LogFactory.getLog(SmProInstController2.class);
	@SuppressWarnings("unused")
	private Map<String, SmProInfo> smproinfo = new HashMap<String, SmProInfo>();
	@Autowired
	private SmProInstEXCELService ProInstEXCELService;
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
	
	
	@RequestMapping(value = "/wfipromater/imagedownload3/{materdataid}", method = RequestMethod.GET)
	@ResponseBody
	public JSONObject download3(@PathVariable String materdataid, HttpServletRequest request, HttpServletResponse response) throws IOException {
		// response.setDateHeader("Expires", 0);
		// response.setHeader("Cache-Control", "no-cache");
		// response.setHeader("Prama", "no-cache");
//		OutputStream os = response.getOutputStream();
		request.setCharacterEncoding("UTF-8");
		String fileName = request.getParameter("fileName");
		fileName = "temp";
		Wfi_MaterData data = ProMaterService.getMaterData(materdataid);
		fileName = java.net.URLEncoder.encode(new String(fileName.getBytes("ISO-8859-1"), "UTF-8"), "UTF-8");
		JSONObject jb=new JSONObject();
		
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
			//	os.flush();
			} else {

			}
			jb.put("data",URLEncoder.encode(URLEncoder.encode(Base64.encode(file),"UTF-8"),"UTF-8") );
		} finally {
//			if (os != null) {
//				os.close();
//			}
		}
		return jb;
	}
	@RequestMapping(value = "/acceptprojectEXCEL", method = RequestMethod.GET)
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
		return "/workflow/frame/acceptprojectEXCEL";
	}
	@RequestMapping(value = "/acceptprojectEXCEL", method = RequestMethod.POST)
	@ResponseBody
	public SmObjInfo AcceptProject(SmProInfo info, BindingResult result, RedirectAttributesModelMap modelMap, SessionStatus status, HttpServletRequest request, HttpServletResponse response) {
	//	SmObjInfo returnsmObjInfo=ProInstEXCELService.createproinfo(info,result);
		ProInstEXCELService.createProject(info,result,request);
	//	return returnsmObjInfo;
		return null;
	}
	@RequestMapping(value = "/acceptprojectAddEXCEL", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject addExcel(HttpServletRequest request){
		JSONObject json=ProInstEXCELService.addExcel(request);
		return json;
	}
}
