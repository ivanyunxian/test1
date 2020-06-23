package com.supermap.wisdombusiness.archives.web;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.axis.encoding.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
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
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

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
import com.supermap.realestate.registration.util.YwLogUtil;
import com.supermap.wisdombusiness.archives.service.SmProInstService_DA;
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
import com.supermap.wisdombusiness.workflow.service.common.Common;
import com.supermap.wisdombusiness.workflow.service.common.QueryCriteria;
import com.supermap.wisdombusiness.workflow.service.common.SmObjInfo;
import com.supermap.wisdombusiness.workflow.service.common.SmProInfo;
import com.supermap.wisdombusiness.workflow.service.wfd.SmProDef;
import com.supermap.wisdombusiness.workflow.service.wfi.SmActInst;
import com.supermap.wisdombusiness.workflow.service.wfi.SmProInst;
import com.supermap.wisdombusiness.workflow.service.wfi.SmProMater;
import com.supermap.wisdombusiness.workflow.service.wfi.SmStaff;
import com.supermap.wisdombusiness.workflow.service.wfi.SmSysMod;
import com.supermap.wisdombusiness.workflow.service.wfi.proinstStateModify;
import com.supermap.wisdombusiness.workflow.service.wfm.OperationService;
import com.supermap.wisdombusiness.workflow.service.wfm.SmProDefService;
import com.supermap.wisdombusiness.workflow.service.wfm.SmProInstService;
import com.supermap.wisdombusiness.workflow.service.wfm.SmProMaterService;
import com.supermap.wisdombusiness.workflow.util.FileUpload;
import com.supermap.wisdombusiness.workflow.util.Message;
import com.supermap.wisdombusiness.workflow.util.Page;
import com.supermap.wisdombusiness.workflow.web.wfi.ProInst;
import com.supermap.yingtanothers.file.FileDownload;
import com.supermap.yingtanothers.file.ZipCompressor;
import com.supermap.yingtanothers.file.deleteAllFile;

import jcifs.smb.SmbFile;
import net.sf.json.JSONObject;
@Controller
@RequestMapping("/frame_da")
public class SmProInstController_DA {
	private static final Log logger = LogFactory.getLog(SmProInstService.class);
	@SuppressWarnings("unused")
	private Map<String, SmProInfo> smproinfo = new HashMap<String, SmProInfo>();
	@Autowired
	private SmProInstService_DA smProInstService_DA;
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
	
	@RequestMapping(value = "/passwork", method = RequestMethod.GET)
	public String client(Model model) {
		return "/workflow/frame/passwork";
	}

	 
 
 
	/**
	 * mass
	 * 获取项目信息
	 * 
	 * **/
	@RequestMapping(value = "/getprojectinfo/{filenumber}", method = RequestMethod.GET)
	@ResponseBody
	public SmProInfo GetProjectInfoByFileNumber(@PathVariable String filenumber, HttpServletRequest request, HttpServletResponse response) {
		// 判断是否有权限查看此模块
		return smProInstService_DA.GetProjectInfoByFileNumber(filenumber);
	}

	 
	 
	 

	 
	/**
	 * mass
	 * @param materdataid
	 * @param request
	 * @param response
	 * @throws IOException
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
     * mass
     * @param file_number
     * @param request
     * @param response
     * @return
     */
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

	 
    /**
     * mass
     * @return
     */
	@RequestMapping(value = "/all/dossierproject", method = RequestMethod.GET)
	@ResponseBody
	public List<Map> GetAllProjectByActinstName() {
		return smProInstService_DA.GetAllProjectByActinstName();
	}
 
    /**
     * mass
     * @param request
     * @param response
     * @return
     */
	// 获取所有等待归档的项目数量
	@RequestMapping(value = "/wait/dossier/count", method = RequestMethod.GET)
	@ResponseBody
	public long GetWaitDossierCount(HttpServletRequest request, HttpServletResponse response) {
		User user = smStaff.getCurrentWorkStaff();
		return smProInstService_DA.GetWaitDossierCount(user.getId());
	}
    /**
     * mass
     * @param file_number
     * @param request
     * @param response
     * @return
     */
	// 获取归档数据
	@RequestMapping(value = "/workflow/dossier/{file_number}", method = RequestMethod.POST)
	@ResponseBody
	public Map worlflowDossier(@PathVariable String file_number, HttpServletRequest request, HttpServletResponse response) {
		String actinstid = request.getParameter("actinstid");
		return smProInstService_DA.worlflowDossier(actinstid, file_number);
	}

	 
    /**
     * mass
     * @param request
     * @param response
     * @return
     */
	// 按照項目名稱獲取項目信息
	@RequestMapping(value = "/allproject/actinst", method = RequestMethod.POST)
	@ResponseBody
	public Message getAllProjectByActName(HttpServletRequest request, HttpServletResponse response) {
		String actname = request.getParameter("actname");
		String status = request.getParameter("status");
		return smProInstService_DA.getAllProjectByActName(actname, status);
	}

	 
	// 验证一个项目是否处于归档环节
		@RequestMapping(value = "/verify/dossier/{lsh}", method = RequestMethod.POST)
		@ResponseBody
		public Map VerifyDossier(@PathVariable String lsh, HttpServletRequest request, HttpServletResponse response) {
			User user = smStaff.getCurrentWorkStaff();
			return smProInstService_DA.VerifyDossier(lsh, user);
		}
		
		
		@RequestMapping(value = "/proinfo/{lsh}", method = RequestMethod.GET)
		@ResponseBody
		public Wfi_ProInst getInfoByLsh(@PathVariable String lsh) {
			return smProInstService_DA.getInfoByLsh(lsh);
		}

	
	 
}
