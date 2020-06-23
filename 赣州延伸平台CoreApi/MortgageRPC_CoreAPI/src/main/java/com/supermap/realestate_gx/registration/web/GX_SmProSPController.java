package com.supermap.realestate_gx.registration.web;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.text.SimpleDateFormat;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.supermap.realestate.registration.ViewClass.DJInfoExtend;
import com.supermap.realestate.registration.ViewClass.ProjectInfo;
import com.supermap.realestate.registration.ViewClass.SQSPB;
import com.supermap.realestate.registration.ViewClass.SQSPBex;
import com.supermap.realestate.registration.model.BDCS_DJDY_GZ;
import com.supermap.realestate.registration.model.BDCS_H_GZ;
import com.supermap.realestate.registration.model.BDCS_H_XZ;
import com.supermap.realestate.registration.model.BDCS_XMXX;
import com.supermap.realestate.registration.service.ProjectService;
import com.supermap.realestate.registration.util.ConfigHelper;
import com.supermap.realestate.registration.util.ConstHelper;
import com.supermap.realestate.registration.util.QRCodeHelper;
import com.supermap.realestate.registration.util.RequestHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.realestate.registration.util.ZipCompressorByAnt;
import com.supermap.realestate_gx.registration.service.impl.SQSPServiceImpl;
import com.supermap.wisdombusiness.core.SuperHelper;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import com.supermap.wisdombusiness.web.Message;
import com.supermap.wisdombusiness.workflow.model.Wfd_Prodef;
import com.supermap.wisdombusiness.workflow.service.common.SmObjInfo;
import com.supermap.wisdombusiness.workflow.service.common.SmProInfo;
import com.supermap.wisdombusiness.workflow.service.wfd.SmProDef;
import com.supermap.wisdombusiness.workflow.service.wfi.SmStaff;
import com.supermap.wisdombusiness.workflow.service.wfm.SmProDefService;
import com.supermap.wisdombusiness.workflow.service.wfm.SmProInstService;
import com.supermap.realestate_gx.registration.service.GX_Service;
import com.supermap.realestate_gx.registration.service.GX_SmProSPService;

/**
 * 
 * @Description:审批页面处理相关
 * @author taochunda
 * @date 2017年09月08日 下午14:11:12
 * @Copyright SuperMap
 */
@Controller
@RequestMapping("/gxframe")
public class GX_SmProSPController {

    @Autowired
    private CommonDao baseCommonDao;
    @Autowired
    private SmProDef smProdef;
    @Autowired
    private SmStaff smStaff;
    @Autowired
    private SmProInstService smProInstService;
    @Autowired
    SmProDefService _SmProDefService;
    @Autowired
    private SQSPServiceImpl sqspbService;
    @Autowired
    private GX_SmProSPService gxSmProSPService;
    /**
     * ProjectService
     */
    @Autowired
    private ProjectService projectService;
    
    /**
	 * 动态意见模板处理相关
     * @throws IOException 
	 * */
	@RequestMapping(value = "/approval/getdttpl", method = RequestMethod.POST)
	@ResponseBody
	public String GetDTTpl(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String yjnr = request.getParameter("content");
		String project_id = request.getParameter("project_id");
		
		return gxSmProSPService.getInfo(yjnr, project_id);
	}
    
}