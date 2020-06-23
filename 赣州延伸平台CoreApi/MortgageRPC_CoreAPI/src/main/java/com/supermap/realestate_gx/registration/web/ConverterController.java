package com.supermap.realestate_gx.registration.web;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.supermap.realestate.registration.ViewClass.DJInfoExtend;
import com.supermap.realestate.registration.ViewClass.SQSPB;
import com.supermap.realestate.registration.ViewClass.SQSPBex;
import com.supermap.realestate.registration.service.ProjectService;
import com.supermap.realestate.registration.service.ZSService;
import com.supermap.realestate.registration.util.QRCodeHelper;
import com.supermap.realestate.registration.util.RequestHelper;
import com.supermap.realestate_gx.registration.service.ConverterService;
import com.supermap.realestate_gx.registration.service.GX_Service;
import com.supermap.realestate_gx.registration.service.impl.SQSPServiceImpl;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import com.supermap.wisdombusiness.web.Message;
import com.supermap.wisdombusiness.workflow.model.Wfd_Prodef;
import com.supermap.wisdombusiness.workflow.service.common.SmObjInfo;
import com.supermap.wisdombusiness.workflow.service.common.SmProInfo;
import com.supermap.wisdombusiness.workflow.service.wfd.SmProDef;
import com.supermap.wisdombusiness.workflow.service.wfi.SmStaff;
import com.supermap.wisdombusiness.workflow.service.wfm.SmProDefService;
import com.supermap.wisdombusiness.workflow.service.wfm.SmProInstService;

@Controller
@RequestMapping("/converter")
public class ConverterController {

    @Autowired
    private CommonDao baseCommonDao;
	@Autowired
	private ConverterService converterService;

	/**导入自然幢的信息
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = "importzrz", method = RequestMethod.GET)
	public  @ResponseBody String  ImportZRZ(HttpServletRequest request, HttpServletResponse response) throws IOException {
/*		String width = request.getParameter("width");
		String height = request.getParameter("height");
		String content = RequestHelper.getParam(request, "content");
		String imgformat = request.getParameter("imgformat");
		if (StringUtils.isEmpty(width))
			width = "120";
		if (StringUtils.isEmpty(height))
			height = "120";
		if (StringUtils.isEmpty(content))
			content = "";
		if (StringUtils.isEmpty(imgformat))
			imgformat = "png";
		BufferedImage  img = QRCodeHelper.CreateQRCode(content, imgformat, Integer.parseInt(width), Integer.parseInt(height));
		response.setContentType("image/"+imgformat+"; charset=GBK");
		response.setHeader("Pragma","no-cache");
		 response.setHeader("Cache-Control","no-cache");
		 response.setIntHeader("Expires",-1);
		 ImageIO.write(img,imgformat,response.getOutputStream());*/
		converterService.ImportZRZ(request,response);
		return "true";
	}
	
	/**产生宗地和房产的映射关系
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = "create_zd_to_fc_map", method = RequestMethod.GET)
	public @ResponseBody String CreateZDToFcMap(HttpServletRequest request, HttpServletResponse response) throws IOException {
		converterService.CreateZDToFcMap(request,response);
		return "true";
	}
	/**导入户的信息
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = "importhouse", method = RequestMethod.GET)
	public  @ResponseBody String  ImportHouse(HttpServletRequest request, HttpServletResponse response) throws IOException {
		converterService.ImportHouse(request,response);
		return "true";
	}
}