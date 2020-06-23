package com.supermap.realestate.registration.web;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.DocAttributeSet;
import javax.print.attribute.HashDocAttributeSet;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.collections.map.CaseInsensitiveMap;
import org.apache.poi.hssf.usermodel.HSSFHeader;
import org.apache.poi.ss.usermodel.Header;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFComment;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import sun.misc.BASE64Decoder;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.TypeReference;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.supermap.realestate.registration.ViewClass.DJInfo;
import com.supermap.realestate.registration.ViewClass.JKSInfo;
import com.supermap.realestate.registration.ViewClass.ProjectInfo;
import com.supermap.realestate.registration.ViewClass.ResultSHB;
import com.supermap.realestate.registration.ViewClass.SHB;
import com.supermap.realestate.registration.ViewClass.SQSPB;
import com.supermap.realestate.registration.ViewClass.ZS;
import com.supermap.realestate.registration.constraint.ConstraintCheck;
import com.supermap.realestate.registration.model.BDCS_CONST;
import com.supermap.realestate.registration.model.BDCS_CRHT;
import com.supermap.realestate.registration.model.BDCS_DJDY_GZ;
import com.supermap.realestate.registration.model.BDCS_DJDY_LS;
import com.supermap.realestate.registration.model.BDCS_DJGD;
import com.supermap.realestate.registration.model.BDCS_DJGDFS;
import com.supermap.realestate.registration.model.BDCS_DJSF;
import com.supermap.realestate.registration.model.BDCS_FSQL_GZ;
import com.supermap.realestate.registration.model.BDCS_IDCARD_PIC;
import com.supermap.realestate.registration.model.BDCS_PRINTRECORD;
import com.supermap.realestate.registration.model.BDCS_QL_GZ;
import com.supermap.realestate.registration.model.BDCS_SQR;
import com.supermap.realestate.registration.model.BDCS_XMXX;
import com.supermap.realestate.registration.model.BDCS_ZRHT;
import com.supermap.realestate.registration.model.WFD_MAPPING;
import com.supermap.realestate.registration.model.interfaces.House;
import com.supermap.realestate.registration.model.interfaces.RealUnit;
import com.supermap.realestate.registration.model.interfaces.Rights;
import com.supermap.realestate.registration.model.interfaces.TDYT;
import com.supermap.realestate.registration.model.interfaces.UseLand;
import com.supermap.realestate.registration.service.ChargeService;
import com.supermap.realestate.registration.service.ProjectService;
import com.supermap.realestate.registration.service.ZSService;
import com.supermap.realestate.registration.tools.CertInfoTools;
import com.supermap.realestate.registration.tools.RightsTools;
import com.supermap.realestate.registration.tools.UnitTools;
import com.supermap.realestate.registration.util.AccessRequired;
import com.supermap.realestate.registration.util.BarcodeUtil;
import com.supermap.realestate.registration.util.ConfigHelper;
import com.supermap.realestate.registration.util.ConstHelper;
import com.supermap.realestate.registration.util.ConstValue;
import com.supermap.realestate.registration.util.ConstValue.BDCDYLX;
import com.supermap.realestate.registration.util.ConstValue.DJDYLY;
import com.supermap.realestate.registration.util.ConstValue.DJLX;
import com.supermap.realestate.registration.util.ConstValue.QLLX;
import com.supermap.realestate.registration.util.ConstValue.SQRLB;
import com.supermap.realestate.registration.util.Global;
import com.supermap.realestate.registration.util.ProjectHelper;
import com.supermap.realestate.registration.util.RequestHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.realestate.registration.util.YwLogUtil;
import com.supermap.wisdombusiness.web.Message;
import com.supermap.wisdombusiness.web.ResultMessage;
import com.supermap.wisdombusiness.web.ui.tree.Tree;
import com.supermap.wisdombusiness.workflow.dao.CommonDao;
import com.supermap.wisdombusiness.workflow.model.Bank_Trustbook;
import com.supermap.wisdombusiness.workflow.model.Bank_TrustbookPage;
import com.supermap.wisdombusiness.workflow.model.Wfd_Prodef;
import com.supermap.wisdombusiness.workflow.util.FileUpload;

/**
 * 
 * @Description:项目相关信息控制器 项目信息，申请人相关，收费相关，申请审批表等。
 * @author 刘树峰
 * @date 2015年6月12日 上午11:49:27
 * @Copyright SuperMap
 */
@Controller
@RequestMapping("/project")
public class ProjectController {

	@Autowired
	private ChargeService chargeService;

	@Autowired
	private CommonDao commonDao;
	/**
	 * ProjectService
	 */
	@Autowired
	private ProjectService projectService;
	
	@Autowired
	private ConstraintCheck constraintcheck;
	
	@Autowired
	private ZSService ZSService;

	/**
	 * 获取ProjectInfo（URL："/{project_id}"，Method：GET）
	 * 
	 * @Title: GetProjectInfo
	 * @author:liushufeng
	 * @date：2015年7月18日 下午5:50:45
	 * @param project_id
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/{project_id}", method = RequestMethod.GET)
	public @ResponseBody ProjectInfo GetProjectInfo(@PathVariable("project_id") String project_id,
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		System.out.println(new Date());
		ProjectInfo info = projectService.getProjectInfo(project_id, request);
		System.out.println(new Date());
		return info;
	}

	/**
	 * 获取ProjectInfo（URL："/{xmbh}"，Method：GET）
	 * 
	 * @Title: GetProjectInfo
	 * @author:wangluoyi @date：2015年12月1日16:51:44
	 * @param xmbh
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "GetProjectId/{xmbh}", method = RequestMethod.GET)
	public @ResponseBody BDCS_XMXX GetProject_Id(@PathVariable("xmbh") String xmbh, HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		BDCS_XMXX xmxx = projectService.getProjectId(xmbh, request);
		return xmxx;
	}

	/**
	 * 获取申请人列表（URL:"/{xmbh}/sqrs"，Method：GET）
	 * 
	 * @Title: GetSQRList
	 * @author:liushufeng
	 * @date：2015年7月18日 下午5:51:43
	 * @param xmbh
	 * @return
	 */
	@RequestMapping(value = "/{xmbh}/sqrs", method = RequestMethod.GET)
	@AccessRequired
	public @ResponseBody Message GetSQRList(@PathVariable("xmbh") String xmbh) {
		List<BDCS_SQR> sqrList = projectService.getSQRList(xmbh);
		// 申请人Tree对象
		List<Tree> treeList = new ArrayList<Tree>();
		for (BDCS_SQR bdcs_sqr : sqrList) {
			// if (!(bdcs_sqr.getSQRLB().equals(SQRLB.YF.Value))) {
			Tree tree = new Tree();
			tree.setId(bdcs_sqr.getId());
			tree.setText(bdcs_sqr.getSQRXM());

			tree.setTag1(ConstHelper.getNameByValue("SQRLB", bdcs_sqr.getSQRLB()));

			treeList.add(tree);
			// }
		}
		Message message = new Message();
		message.setRows(treeList);
		message.setTotal(treeList.size());
		return message;
	}

	/**
	 * 获取树结构的申请人列表（URL："/{xmbh}/sqrtree"，Method：GET）
	 * 
	 * @Title: getSQRTree
	 * @author:liushufeng
	 * @date：2015年7月18日 下午5:54:00
	 * @param xmbh
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/{xmbh}/sqrtree", method = RequestMethod.GET)
	public @ResponseBody List<Tree> getSQRTree(@PathVariable("xmbh") String xmbh, HttpServletRequest request,
			HttpServletResponse response) {
		List<BDCS_SQR> sqrList = projectService.getSQRList(xmbh);
		// 申请人Tree对象
		List<Tree> eTrees = new ArrayList<Tree>();
		Tree rootTree = new Tree();
		rootTree.setId("sqr");
		rootTree.setText("申请人");
		rootTree.setType("root");
		rootTree.setChecked(false);
		// 把申请人里边的义务人过滤掉
		for (BDCS_SQR sqr : sqrList) {
			if (sqr.getSQRLB() != null && !(sqr.getSQRLB().equals(SQRLB.YF.Value))) {
				Tree uTree = new Tree();
				uTree.setId(sqr.getId());
				uTree.setText(sqr.getSQRXM());
				uTree.setType("sqr");
				uTree.setChecked(false);
				if (rootTree.children == null) {
					rootTree.children = new ArrayList<Tree>();
				}
				rootTree.children.add(uTree);
			}
		}
		eTrees.add(rootTree);
		return eTrees;
	}

	/**
	 * 获取申请人详细信息（URL："/{xmbh}/sqrs/{sqrid}"，Method：GET）
	 * 
	 * @Title: GetSQRInfo
	 * @author:liushufeng
	 * @date：2015年7月18日 下午5:55:24
	 * @param xmbh
	 * @param sqrid
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/{xmbh}/sqrs/{sqrid}", method = RequestMethod.GET)
	public @ResponseBody BDCS_SQR GetSQRInfo(@PathVariable("xmbh") String xmbh, @PathVariable("sqrid") String sqrid,
			Model model) {
		BDCS_SQR sqr = projectService.getSQRXX(sqrid);
		model.addAttribute("sqrAttribute", sqr);
		return sqr;
	}
  //excel导入申请人
    @RequestMapping(value = "/upsqrexcel/", method = RequestMethod.POST)
    public @ResponseBody Message UpLoadBatchZip(@RequestParam("file") MultipartFile file, HttpServletRequest request, HttpServletResponse response) {
          Message msg=projectService.upsqrexcel(file,request);
          return msg;
    }


	/**
	 * 新增申请人（URL："/{xmbh}/sqrs"，Method：POST）
	 * 
	 * @Title: AddSQR
	 * @author:liushufeng
	 * @date：2015年7月18日 下午5:57:07
	 * @param xmbh
	 * @param sqr
	 * @param result
	 * @return
	 */
	@RequestMapping(value = "/{xmbh}/sqrs", method = RequestMethod.POST)
	public @ResponseBody ResultMessage AddSQR(@PathVariable("xmbh") String xmbh, @Validated BDCS_SQR sqr,
			BindingResult result) {
		ResultMessage m = new ResultMessage();
		if (result.hasErrors()) {
			String msgText = result.getFieldErrors().get(0).getDefaultMessage();
			m.setMsg(msgText);
			m.setSuccess("false");
			YwLogUtil.addYwLog("新增申请人-失败", ConstValue.SF.NO.Value, ConstValue.LOG.ADD);
			return m;
		}
		// 证件号中带小写字母转换成大写的，目前处理身份证的
		if (!StringHelper.isEmpty(sqr)) {
			if (!StringHelper.isEmpty(sqr.getZJLX())) {
				if ("1".equals(sqr.getZJLX())) {
					if (!StringHelper.isEmpty(sqr.getZJH())) {
						sqr.setZJH(sqr.getZJH().toUpperCase());
					}
				}
			}
			//一些字段先进行去除空格处理
			if (!StringHelper.isEmpty(sqr.getSQRXM())) {
				sqr.setSQRXM(sqr.getSQRXM().replace(" ", ""));
			}
			if (!StringHelper.isEmpty(sqr.getZJH())) {
				sqr.setZJH(sqr.getZJH().replace(" ", ""));
			}
			if (!StringHelper.isEmpty(sqr.getDLRXM())) {
				sqr.setDLRXM(sqr.getDLRXM().replace(" ", ""));
			}
			if (!StringHelper.isEmpty(sqr.getDLRZJHM())) {
				sqr.setDLRZJHM(sqr.getDLRZJHM().replace(" ", ""));
			}
		}
		String sqrid = projectService.hasSQR(sqr.getXMBH(), sqr.getSQRXM(), sqr.getSQRLB(), sqr.getZJH());
		if (sqrid != null) {
			m.setMsg(sqrid);
			m.setSuccess("fasle");
			YwLogUtil.addYwLog("新增申请人-失败", ConstValue.SF.NO.Value, ConstValue.LOG.ADD);
		} else {
			sqrid = projectService.addSQRXX(sqr);
			m.setMsg(sqrid);
			m.setSuccess("true");
			YwLogUtil.addYwLog("新增申请人-成功", ConstValue.SF.YES.Value, ConstValue.LOG.ADD);
			
			//桂林需求-更正和预告登记的申请人页面，新增、更新或删除义务人时，把所有义务人更新到FSQL_GZ的YWR字段
			if("450300".equals(ConfigHelper.getNameByValue("XZQHDM")) && SQRLB.YF.Value.equals(sqr.getSQRLB()))
				projectService.updateFsqlYWR(xmbh);
			
		}
		return m;
	}

	/**
	 * 身份证读取
	 * 
	 * @author diaoliwei
	 * @param xmbh
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/{xmbh}/addSqrCard", method = RequestMethod.POST)
	public @ResponseBody ResultMessage AddsqrByCard(@PathVariable("xmbh") String xmbh, HttpServletRequest request) {
		ResultMessage m = new ResultMessage();
		BDCS_SQR sqr = new BDCS_SQR();
		String sqrxm = request.getParameter("name");
		String address = request.getParameter("address");
		String sex = request.getParameter("sex");
		String cardno = request.getParameter("cardno");
		String code = request.getParameter("picture");
		sqr.setSQRXM(sqrxm);
		if (!StringUtils.isEmpty(sex)) {
			if ("男".equals(sex)) {
				sqr.setXB("1");
			} else if ("女".equals(sex)) {
				sqr.setXB("2");
			} else {
				sqr.setXB("3");
			}
		}
		sqr.setSQRLX("1");
		sqr.setZJLX("1");
		sqr.setZJH(cardno);
		sqr.setTXDZ(address);
		sqr.setXMBH(xmbh);
		sqr.setPICTURECODE(code);
		String sqrid = projectService.hasSQR(xmbh, sqrxm, SQRLB.JF.Value, cardno);
		sqr.setSQRLB(SQRLB.JF.Value);
		if (sqrid != null) {
			m.setMsg(sqrid);
			m.setSuccess("fasle");
			YwLogUtil.addYwLog("身份证读取-失败", ConstValue.SF.YES.Value, ConstValue.LOG.ADD);
		} else {
			sqrid = projectService.addSQRXX(sqr);
			m.setMsg(sqrid);
			m.setSuccess("true");
			YwLogUtil.addYwLog("身份证读取-成功", ConstValue.SF.YES.Value, ConstValue.LOG.ADD);
		}
		return m;
	}

	/**
	 * 更新申请人（URL："/{xmbh}/sqrs/{sqrid}"，Method：POST）
	 * 
	 * @Title: UpdateSQR
	 * @author:liushufeng
	 * @date：2015年7月18日 下午6:00:22
	 * @param sqrid
	 * @param xmbh
	 * @param sqr
	 * @param result
	 * @return
	 */
	@RequestMapping(value = "/{xmbh}/sqrs/{sqrid}", method = RequestMethod.POST)
	public @ResponseBody ResultMessage UpdateSQR(@PathVariable("sqrid") String sqrid, @PathVariable("xmbh") String xmbh,
			@Validated BDCS_SQR sqr, BindingResult result) {
		ResultMessage m = new ResultMessage();
		if (result.hasErrors()) {
			String msgText = result.getFieldErrors().get(0).getDefaultMessage();
			m.setMsg(msgText);
			m.setSuccess("false");
			YwLogUtil.addYwLog("更新申请人-失败", ConstValue.SF.NO.Value, ConstValue.LOG.UPDATE);
			return m;
		}
		sqr.setId(sqrid);
		// 证件号中带小写字母转换成大写的，目前处理身份证的
		if (!StringHelper.isEmpty(sqr)) {
			if (!StringHelper.isEmpty(sqr.getZJLX())) {
				if ("1".equals(sqr.getZJLX())) {
					if (!StringHelper.isEmpty(sqr.getZJH())) {
						sqr.setZJH(sqr.getZJH().toUpperCase());
					}
				}
			}
			//一些字段先进行去除空格处理
			if (!StringHelper.isEmpty(sqr.getSQRXM())) {
				sqr.setSQRXM(sqr.getSQRXM().replace(" ", ""));
			}
			if (!StringHelper.isEmpty(sqr.getZJH())) {
				sqr.setZJH(sqr.getZJH().replace(" ", ""));
			}
			if (!StringHelper.isEmpty(sqr.getDLRXM())) {
				sqr.setDLRXM(sqr.getDLRXM().replace(" ", ""));
			}
			if (!StringHelper.isEmpty(sqr.getDLRZJHM())) {
				sqr.setDLRZJHM(sqr.getDLRZJHM().replace(" ", ""));
			}
		}
		projectService.updateSQRXX(sqr);
		m.setMsg("更新成功");
		m.setSuccess("true");
		YwLogUtil.addYwLog("更新申请人-成功：申请人姓名：" + sqr.getSQRXM(), ConstValue.SF.YES.Value, ConstValue.LOG.UPDATE);
		
		//桂林需求-更正和预告登记的申请人页面，新增、更新或删除义务人时，把所有义务人更新到FSQL_GZ的YWR字段
		if("450300".equals(ConfigHelper.getNameByValue("XZQHDM")) && SQRLB.YF.Value.equals(sqr.getSQRLB()))
			projectService.updateFsqlYWR(xmbh);
		
		return m;
	}

	/**
	 * 删除申请人（URL："/{xmbh}/sqrs/{sqrid}"，Method：DELETE）
	 * 
	 * @Title: DeleteSQR
	 * @author:liushufeng
	 * @date：2015年7月18日 下午6:00:32
	 * @param xmbh
	 * @param sqrid
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/{xmbh}/sqrs/{sqrid}", method = RequestMethod.DELETE)
	public @ResponseBody ResultMessage DeleteSQR(@PathVariable String xmbh, @PathVariable String sqrid, Model model) {
		
		String sqrlb = null;
		if("450300".equals(ConfigHelper.getNameByValue("XZQHDM"))){
			BDCS_SQR sqr = commonDao.get(BDCS_SQR.class, sqrid);
			if(sqr != null){
				sqrlb = StringHelper.isEmpty(sqr.getSQRLB())? null : sqr.getSQRLB();
			}
			commonDao.flush();
		}
		
		projectService.deleteSQRXX(sqrid);
		String Baseworkflow_ID =ProjectHelper.GetPrjInfoByXMBH(xmbh).getBaseworkflowcode();
		if(Baseworkflow_ID.indexOf("YY") == 0){//如果是异议登记流程，把申请人信息干掉之后，要去更新fsql表中ywr的信息，要不然证书页面会显示多余的数据
			BDCS_FSQL_GZ fsql = null;
			BDCS_QL_GZ ql = null;
			//删除申请人信息，就更新fsql表中的ywr状态
			List<BDCS_SQR> sqrs = commonDao.getDataList(BDCS_SQR.class, "XMBH = '" + xmbh + "'");
			if(sqrs.size()>0 && sqrs != null){
				String ywrStr = "";
				String fsqlYwr = "";
				for(BDCS_SQR bdcs_sqr : sqrs){
					if(bdcs_sqr.getSQRLB().equals("2")){
						ywrStr += bdcs_sqr.getSQRXM() +",";
					}
				}
				fsqlYwr = ywrStr.substring(0, ywrStr.length()-1);
				String djdyid = commonDao.getDataList(BDCS_DJDY_GZ.class, "XMBH='"+xmbh+"'").get(0).getDJDYID();
				List<BDCS_QL_GZ> listqls = commonDao.getDataList(BDCS_QL_GZ.class, "XMBH='" +xmbh + "' and DJDYID='" + djdyid + "'");
				if (listqls != null && listqls.size() > 0) {
					ql = listqls.get(0);
				}
				List<BDCS_FSQL_GZ> listfsqls = commonDao.getDataList(BDCS_FSQL_GZ.class," QLID='" + ql.getId() + "'");
				if(listfsqls != null && listfsqls.size() > 0){
					fsql = listfsqls.get(0);
					if(!StringHelper.isEmpty(ywrStr)){
						fsqlYwr = ywrStr.substring(0, ywrStr.length()-1);
						fsql.setYWR(fsqlYwr);
					}
					commonDao.update(fsql);
				}
			}
		}
		ResultMessage msg = new ResultMessage();
		msg.setSuccess("true");
		msg.setMsg("删除成功");
		YwLogUtil.addYwLog("删除申请人-成功", ConstValue.SF.YES.Value, ConstValue.LOG.DELETE);
		
		//桂林需求-更正和预告登记的申请人页面，新增、更新或删除义务人时，把所有义务人更新到FSQL_GZ的YWR字段
		if(sqrlb != null && SQRLB.YF.Value.equals(sqrlb)){
			projectService.updateFsqlYWR(xmbh);
		}
							
		return msg;
	}
	
	/**
	 * 删除申请人头像（URL："/sqrs/{zjh}"，Method：DELETE）
	 * 
	 * @Title: DeleteSQRPIC
	 * @author:赵梦帆
	 * @date：2016-10-17 17:18:42
	 * @param zjh
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/sqrs/{zjh}", method = RequestMethod.DELETE)
	public @ResponseBody ResultMessage DeleteSQRPIC(@PathVariable String zjh, Model model) {
		projectService.deleteSQRPIC(zjh);
		ResultMessage msg = new ResultMessage();
		msg.setSuccess("true");
		msg.setMsg("删除成功");
		YwLogUtil.addYwLog("删除申请人头像-成功", ConstValue.SF.YES.Value, ConstValue.LOG.DELETE);
		return msg;
	}
	
	/**
	 * 加载申请人头像（URL："/sqrs/{zjh}"，Method：GET）
	 * 
	 * @Title: LoadSQRPIC
	 * @author:赵梦帆
	 * @date：2016-10-17 20:38:01
	 * @param zjh
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/sqrs/{zjh}", method = RequestMethod.GET)
	public @ResponseBody String LoadSQRPIC(@PathVariable String zjh, Model model) {
		List<BDCS_IDCARD_PIC> pic = projectService.LoadSQRPIC(zjh);
		if(pic!=null&&pic.size()==1){
			return pic.get(0).getPIC1();
		}else
			return null;
	}


	/**
	 * 获取收费记录列表（URL："/{xmbh}/sfxxs"，Method：GET）
	 * 
	 * @Title: GetSFList
	 * @author:liushufeng
	 * @date：2015年7月18日 下午6:01:08
	 * @param xmbh
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/{xmbh}/sfxxs", method = RequestMethod.GET)
	public @ResponseBody Message GetSFList(@PathVariable("xmbh") String xmbh, HttpServletRequest request,
			HttpServletResponse response) {
		Message message = new Message();
		Integer page = 1;
		if (request.getParameter("page") != null) {
			page = Integer.parseInt(request.getParameter("page"));
		}
		Integer rows = 20;
		if (request.getParameter("rows") != null) {
			rows = Integer.parseInt(request.getParameter("rows"));
		}
		message = projectService.getPagedSFList(xmbh, page, rows);
		// message.setTotal(paged.getTotalCount());
		// message.setRows(paged.getResult());
		return message;
	}

	/**
	 * 增加收费信息（URL："/{xmbh}/jlsfzdh"，Method：POST）
	 * 
	 * @Title: JLSFZDH
	 * @author:YX
	 * @date：2017年5月24日 下午6:03:56
	 * @param xmbh
	 * @param djsf
	 * @param result
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/{xmbh}/jlsfzdh", method = RequestMethod.POST)
	public void JLSFZDH(@PathVariable("xmbh") String xmbh, @Validated BDCS_DJSF djsf,
			BindingResult result, HttpServletRequest request, HttpServletResponse response) {
		//ResultMessage msg = new ResultMessage();
		Integer page = 1;
		if (request.getParameter("page") != null) {
			page = Integer.parseInt(request.getParameter("page"));
		}
		Integer rows = 20;
		if (request.getParameter("rows") != null) {
			rows = Integer.parseInt(request.getParameter("rows"));
		}
		projectService.Jlsfzdh(xmbh,page,rows);
	}

	/**
	 * 确认收费
	 *
	 * @author:notebao
	 * @date：2019年4月23日16:26:42
	 * @param xmbh
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/{xmbh}/sfqr", method = RequestMethod.POST)
	public @ResponseBody ResultMessage sfqr(@PathVariable("xmbh") String xmbh, HttpServletRequest request) {
		return projectService.sfqr(request, xmbh);
	}

	/**
	 * 校验是否收费
	 *
	 * @author:taochunda
	 * @date：2019年6月14日 11:06:32
	 * @param xmbh
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/checksfqr", method = RequestMethod.POST)
	public @ResponseBody ResultMessage CheckSfqr(HttpServletRequest request) {
		return projectService.CheckSfqr(request);
	}

	
	/**
	 * 增加收费信息（URL："/{xmbh}/sfxxs"，Method：POST）
	 * 
	 * @Title: AddSFXX
	 * @author:liushufeng
	 * @date：2015年7月18日 下午6:03:56
	 * @param xmbh
	 * @param djsf
	 * @param result
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/{xmbh}/sfxxs", method = RequestMethod.POST)
	public @ResponseBody ResultMessage AddSFXX(@PathVariable("xmbh") String xmbh, @Validated BDCS_DJSF djsf,
			BindingResult result, HttpServletRequest request, HttpServletResponse response) {
		ResultMessage msg = new ResultMessage();
		String sfdyids = request.getParameter("dyids");
		chargeService.addSFfromDY(xmbh, sfdyids);
		msg.setSuccess("true");
		msg.setMsg("添加成功!");
		YwLogUtil.addYwLog("增加收费信息-成功", ConstValue.SF.YES.Value, ConstValue.LOG.ADD);
		return msg;
	}

	/**
	 * 更新收费信息（URL："/{xmbh}/sfxxs/{sfid}"，Method：POST）
	 * 
	 * @Title: UpdateSFInfo
	 * @author:liushufeng
	 * @date：2015年7月18日 下午6:04:26
	 * @param xmbh
	 * @param sfid
	 * @param djsf
	 * @param result
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/{xmbh}/sfxxs/{sfid}", method = RequestMethod.POST)
	public @ResponseBody ResultMessage UpdateSFInfo(@PathVariable("xmbh") String xmbh,
			@PathVariable("sfid") String sfid, @ModelAttribute("djsfAttribute") BDCS_DJSF djsf, BindingResult result,
			HttpServletRequest request, HttpServletResponse response) {
		ResultMessage msg = new ResultMessage();
		if (result.hasErrors()) {
			String msgText = result.getFieldErrors().get(0).getDefaultMessage();
			if (msgText.indexOf("Double") != -1) {
				msgText = "收费基数、应收金额、折扣后应收金额！请输入数字格式";
			}
			msg.setMsg(msgText);
			msg.setSuccess("false");
			YwLogUtil.addYwLog("更新收费信息-失败", ConstValue.SF.NO.Value, ConstValue.LOG.UPDATE);
		} else {
			try {
				
				 BDCS_DJSF csf=projectService.getSFXX(sfid);
				 if(csf!=null)
				 {
					 djsf.setQLRMC(csf.getQLRMC());
					 djsf.setJSGS(csf.getJSGS());
					 djsf.setXSGS(csf.getXSGS());
				
				 }
				djsf.setId(sfid);
				djsf.setXMBH(xmbh);
				projectService.updateSFXX(djsf);
				msg.setMsg("更新成功!");
				msg.setSuccess("true");
				YwLogUtil.addYwLog("更新收费信息-成功", ConstValue.SF.YES.Value, ConstValue.LOG.UPDATE);
			} catch (Exception e) {
				msg.setMsg("更新失败!");
				YwLogUtil.addYwLog("更新收费信息-更新失败!", ConstValue.SF.NO.Value, ConstValue.LOG.UPDATE);
			}
		}
		return msg;
	}

	/**
	 * 删除收费记录（URL："/{xmbh}/sfxxs/{sfid}"，Method：DELETE） 增加了XMBH参数修改前台JS中对应的调用的地方
	 * 
	 * @Title: DeleteSFInfo
	 * @author:liushufeng
	 * @date：2015年7月18日 下午6:05:25
	 * @param sfid
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/sfxxs/{sfid}", method = RequestMethod.DELETE)
	public @ResponseBody ResultMessage DeleteSFInfo(@PathVariable("sfid") String sfid, HttpServletRequest request,
			HttpServletResponse response) {
		ResultMessage msg = new ResultMessage();
		if (StringUtils.hasLength(sfid)) {
			try {
				projectService.deleteSFXX(sfid);
				msg.setSuccess("true");
				msg.setMsg("删除成功!");
				YwLogUtil.addYwLog("删除收费信息-删除成功!", ConstValue.SF.YES.Value, ConstValue.LOG.DELETE);
			} catch (Exception e) {
				msg.setSuccess("false");
				msg.setMsg("删除失败!");
				YwLogUtil.addYwLog("删除收费信息-删除失败!", ConstValue.SF.NO.Value, ConstValue.LOG.DELETE);
				e.printStackTrace();
			}
		}
		return msg;
	}

	/**
	 * 获取申请审批表（URL："/{xmbh}/sqspb"，Method：GET）
	 * 
	 * @Title: GetSQSPBInfo
	 * @author:liushufeng
	 * @date：2015年7月18日 下午6:07:28
	 * @param xmbh
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/{xmbh}/{acinstid}/sqspb", method = RequestMethod.GET)
	public @ResponseBody SQSPB GetSQSPBInfo(@PathVariable("xmbh") String xmbh,
			@PathVariable("acinstid") String acinstid, HttpServletRequest request, HttpServletResponse response) {
		SQSPB sqspb = projectService.GetSQSPBex(xmbh, acinstid, request).converToSQSPB();
		return sqspb;
	}
	
	/**
	 * @Description: 四川内江--登记系统新增审批表页面（组件），其中包含“审批表附表1”和“审批表附表2”（便于大件批量审核）
	 * @Title: GetSQSPBInfoNJ
	 * @Author: zhaomengfan
	 * @Date: 2017年2月20日上午11:45:26
	 * @param xmbh
	 * @param acinstid
	 * @param request
	 * @param response
	 * @return
	 * @return SQSPB
	 */
	@RequestMapping(value = "/{xmbh}/{acinstid}/sqspbNJ", method = RequestMethod.GET)
	public @ResponseBody List<Map<String, String>> GetSQSPBInfoNJ(@PathVariable("xmbh") String xmbh,
			@PathVariable("acinstid") String acinstid, HttpServletRequest request, HttpServletResponse response) {
		List<Map<String, String>> sqspb = projectService.GetSQSPBexNJ(xmbh, acinstid, request);
		return sqspb;
	}

	/**
	 * @Description: 预览并打印附章表excel
	 * @Title: SQSPBFZB
	 * @Author：赵梦帆
	 * @Data：2016年12月1日 下午6:31:46
	 * @param xmbh
	 * @param request
	 * @param response
	 * @return
	 * @return String
	 * @throws IOException 
	 */
	@RequestMapping(value = "/{xmbh}/SQSPBFZB", method = RequestMethod.POST)
	public @ResponseBody String SQSPBFZB(@PathVariable("xmbh") String xmbh,
			HttpServletRequest request, HttpServletResponse response) throws IOException{
		String dataString = request.getParameter("info");
		String headerstring = request.getParameter("header");
		if(StringHelper.isEmpty(headerstring)||StringHelper.isEmpty(dataString)){
			return null;
		}
		LinkedHashMap<String, String> object = JSON.parseObject(dataString, new TypeReference<LinkedHashMap<String, String>>() {
        });
		//模板位置
		String tplFullName = request.getSession().getServletContext().getRealPath("/WEB-INF/jsp/wjmb/SQSPBFZB.xlsx");

		String url = "";
		
		String basePath = request.getSession().getServletContext().getRealPath("/") + "\\resources\\PDF";
		
		if(new File(tplFullName).exists()){
			String outpath = basePath + "\\tmp\\SQSPBFZB.xlsx";
			url = request.getContextPath() + "\\resources\\PDF\\tmp\\SQSPBFZB.xlsx";
			FileOutputStream outstream = new FileOutputStream(outpath);
			InputStream input = new FileInputStream(tplFullName);
			XSSFWorkbook wb = null;// 定义一个新的工作簿
			wb = new XSSFWorkbook(input);
			XSSFSheet sheet = wb.getSheetAt(0);
			Header header = sheet.getHeader();
			header.setCenter(HSSFHeader.fontSize((short) 10)+headerstring);
			XSSFRow tplrow = sheet.getRow(0);
			XSSFCellStyle tplcellStyle = tplrow.getCell(0).getCellStyle();
			//插入数据
			int rowindex = 0;
			int cellindex = 0;
			XSSFRow insertRow = null;
			XSSFCell insertcell = null;
			for (Entry<String, String> entry : object.entrySet()) {
				if(rowindex%2==0){
					insertRow = sheet.createRow(rowindex/2);
				}
				rowindex++;
				insertcell = insertRow.createCell(cellindex++%2);
				insertcell.setCellStyle(tplcellStyle);
				Object value = entry.getValue();
				insertcell.setCellValue(value.toString());
			}
			wb.write(outstream);
			outstream.flush();
			outstream.close();
			outstream = null;
		}
		return url;
	}
	
	/**
	 * 创建申请审批表信息PDF WUZHU
	 * 
	 * @param xmbh
	 * @param request
	 * @param response
	 * @return
	 * @throws DocumentException
	 * @throws IOException
	 */
	@SuppressWarnings({ "deprecation", "rawtypes", "unchecked" })
	@RequestMapping(value = "/{xmbh}/{tpl}/sqspbCreatePDF", method = RequestMethod.POST)
	public @ResponseBody String SQSPBCreatePDF(@PathVariable("xmbh") String xmbh, @PathVariable("tpl") String tpl,
			HttpServletRequest request, HttpServletResponse response) throws IOException, DocumentException {
		if (StringUtils.isEmpty(tpl)) {
			return "";
		}
		String basePath = request.getRealPath("/") + "\\resources\\PDF";// bdcdjsqbfb
		String url = request.getContextPath() + "/resources/PDF/tmp/" + xmbh + "-" + tpl + ".pdf";
		String outpath = basePath + "\\tmp\\" + xmbh + "-" + tpl + ".pdf";
		String TemplatePDF = basePath + "\\tpl\\" + tpl + ".pdf";
		FileOutputStream outstream = new FileOutputStream(outpath);
		ArrayList<ByteArrayOutputStream> baos = new ArrayList<ByteArrayOutputStream>();
		Map<String, Map> pageDatas = new TreeMap<String, Map>(new Comparator<String>() {
			public int compare(String obj1, String obj2) {
				// 升序排序return obj1.compareTo(obj2);
				return Integer.parseInt(obj1) - Integer.parseInt(obj2);

			}
		});
		String size = request.getParameter("pagesize");
		int pagesize = StringHelper.getInt(size);
		String dataString = request.getParameter("info");
		com.alibaba.fastjson.JSONObject object = JSON.parseObject(dataString);
		
		String imagepath = basePath + "\\tmp\\" + xmbh + "-" + tpl + "img.png";
		if(!StringHelper.isEmpty(object.get("slbh"))){
			String bh = object.get("slbh").toString();
			BufferedImage img = BarcodeUtil.createBarcodeStream(bh, "Code128", 240, 60, true);
			ImageIO.write(img, "png", new FileOutputStream(new File(imagepath)));
		}
		
		
		String keyName = "";
		int keyPageNum = 0;
		pageDatas.put("1", new HashMap<String, Object>());// 初始化第一页
		pageDatas.get("1").put("page", "1");
		for (Entry<String, Object> entry : object.entrySet()) {
			keyPageNum = getPageNum(entry.getKey(),pagesize);
			keyName = getID(entry.getKey(), keyPageNum, pagesize);
			Object entryvalue = entry.getValue();
			if (entryvalue != null) {
				String imageurl = entryvalue.toString();
				Pattern ptn = Pattern.compile("http://.*/realestate.*");
				Matcher mt = ptn.matcher(imageurl);
				if (mt.matches()) {
					String ip = InetAddress.getLocalHost().getHostAddress();
					String port = request.getLocalPort() + "";
					imageurl = imageurl.replaceAll("http://.*/realestate", "http://" + ip + ":" + port + "/realestate");
					entryvalue = imageurl;
				}

				if (imageurl.indexOf("base64") > -1) {
					String name = UUID.randomUUID().toString();
					String ip = InetAddress.getLocalHost().getHostAddress();
					String port = request.getLocalPort() + "";
					String path = request.getRealPath("/") + "\\resources\\workflow\\signimg\\" + name + ".png";
					String filepath = "http://" + ip + ":" + port + "/realestate/resources/workflow/signimg/" + name
							+ ".png";
					BASE64Decoder decoder = new BASE64Decoder();
					byte[] b = decoder.decodeBuffer(imageurl.substring(imageurl.indexOf(",") + 1));
					for (int i = 0; i < b.length; i++) {
						if (b[i] < 0) {
							b[i] += 256;
						}
					}
					String imgfilepath = "";
					OutputStream out = new FileOutputStream(path);
					out.write(b);
					out.flush();
					out.close();
					entryvalue = filepath;
				}

			}
			if("qlrxm".equals(keyName)||"qlrxm1".equals(keyName)||"sjr".equals(keyName)||"page2_slr".equals(keyName)){
				entryvalue=StringHelper.formatObject(entryvalue).replaceAll("·", "· ");
			}
			if (pageDatas.containsKey(String.valueOf(keyPageNum)))
				pageDatas.get(String.valueOf(keyPageNum)).put(keyName, entryvalue);
			else {
				pageDatas.put(String.valueOf(keyPageNum), new HashMap<String, Object>());
				pageDatas.get(String.valueOf(keyPageNum)).put("page", String.valueOf(keyPageNum));
				pageDatas.get(String.valueOf(keyPageNum)).put(keyName, entryvalue);
			}
		}
		
		if ("bdcdjspb".equals(tpl)||"bdcdjspb3".equals(tpl)) {
			pageDatas.get(String.valueOf(1)).put("tm", imagepath);
		}
		
		for (Entry<String, Map> pageData : pageDatas.entrySet()) {
		
			ByteArrayOutputStream fos = CreatePdfStream(TemplatePDF, pageData.getValue());
			baos.add(fos);
		}

		if (baos.size() > 0) {
			Document doc = new Document();
			PdfCopy pdfCopy = new PdfCopy(doc, outstream);
			doc.open();
			PdfImportedPage impPage = null;
			PdfReader reader = null;
			for (ByteArrayOutputStream bao : baos) {
				reader = new PdfReader(bao.toByteArray());
				int page = reader.getNumberOfPages();
				for (int p = 1; p <= page; p++) {
					impPage = pdfCopy.getImportedPage(reader, p);
					pdfCopy.addPage(impPage);
				}
			}
			doc.close();
		}
		return url;
	}
	
	/**
	 * 格式 H_BDCDYH__1 根据__后面的数判断是第几页
	 * 
	 * @Title: getPageNum
	 * @author:WUZHU
	 * @date：2015年11月14日 下午5:04:13
	 * @param key
	 * @param pagesize 
	 * @return
	 */
	private int getPageNum(String key, int pagesize) {
		int pageindex = 1;
		if(pagesize==0){
			pagesize = 29;
		}
		int start = key.lastIndexOf("__");
		if (start != -1) {
			String[] names = key.split("__");
			double count = Double.valueOf(names[1]);
			pageindex = (int) Math.ceil(count / pagesize);// 默认29行分一页
		}
		return pageindex;
	}

	/**
	 * 格式 H_BDCDYH__1 转成H_BDCDYH1
	 * 
	 * @Title: getID
	 * @author:WUZHU
	 * @date：2015年11月14日 下午5:04:03
	 * @param key
	 * @param pageindex
	 * @param pagesize 
	 * @return
	 */
	private String getID(String key, int pageindex, int pagesize) {
		String name = key;
		if(pagesize==0){
			pagesize = 29;
		}
		int end = key.lastIndexOf("__");
		if (end != -1) {
			String[] names = key.split("__");
			int count = Integer.valueOf(names[1]);
			int pagecount = count - (pageindex - 1) * pagesize;
			name = names[0] + pagecount;
		}
		return name;
	}

	/**
	 * 打印申请审批表信息表单信息 WUZHU
	 * 
	 * @param xmbh
	 * @param request
	 * @param response
	 * @return
	 * @throws PrintException
	 * @throws FileNotFoundException
	 */
	@SuppressWarnings({ "deprecation", "unused" })
	@RequestMapping(value = "/{xmbh}/{tpl}/sqspbPrintPDF", method = RequestMethod.POST)
	public @ResponseBody void SQSPBPrintPDF(@PathVariable("xmbh") String xmbh, @PathVariable("tpl") String tpl,
			HttpServletRequest request, HttpServletResponse response) throws PrintException, FileNotFoundException {
		String basePath = request.getRealPath("/") + "\\resources\\PDF";
		String pdfpath = basePath + "\\tmp\\" + xmbh + "-" + tpl + ".pdf";
		File pdfFile = new File(pdfpath);
		// 构建打印请求属性集
		PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
		// 设置打印格式，因为未确定文件类型，这里选择AUTOSENSE
		DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;
		// 查找所有的可用打印服务
		PrintService printService[] = PrintServiceLookup.lookupPrintServices(flavor, pras);
		// 定位默认的打印服务
		PrintService defaultService = PrintServiceLookup.lookupDefaultPrintService();
		// 显示打印对话框
		// PrintService service = ServiceUI.printDialog(null, 200, 200,
		// printService, defaultService, flavor, pras);
		if (defaultService != null) {
			DocPrintJob job = defaultService.createPrintJob(); // 创建打印作业
			FileInputStream fis = new FileInputStream(pdfFile); // 构造待打印的文件流
			DocAttributeSet das = new HashDocAttributeSet();
			Doc doc = new SimpleDoc(fis, flavor, das); // 建立打印文件格式
			job.print(doc, pras); // 进行文件的打印
		}
		YwLogUtil.addYwLog("打印申请审批表信息表单信息", ConstValue.SF.YES.Value, ConstValue.LOG.PRINT);
	}

	/**
	 * 根据PDF模板创建流
	 * 
	 * @Title: CreatePdfStream
	 * @author:WUZHU
	 * @date：2015年11月14日 下午5:03:01
	 * @param tplName
	 * @param data
	 * @return
	 * @throws IOException
	 * @throws DocumentException
	 */
	private ByteArrayOutputStream CreatePdfStream(String tplName, Map<String, Object> data)
			throws IOException, DocumentException {
		ByteArrayOutputStream bao = new ByteArrayOutputStream();
		PdfReader reader = new PdfReader(tplName);
		PdfStamper stamp = new PdfStamper(reader, bao);
		AcroFields form = stamp.getAcroFields();

		Set<String> fields = form.getFields().keySet();
		for (String key : fields) {
			String _key = ConvertToPdfName(key);
			if (data.containsKey(_key)) {
				if (!StringUtils.isEmpty(data.get(_key)) && !data.get(_key).toString().equals("null")) {
					if (form.getFieldType(_key) == AcroFields.DA_SIZE) {
						try {
							Image image1 = Image.getInstance(String.valueOf(data.get(_key)));
							List<AcroFields.FieldPosition> imgPosition = form.getFieldPositions(_key);
							int page = imgPosition.get(0).page;
							if (page < 1) {
								page = 1;
							}
							PdfContentByte overContent = stamp.getOverContent(page);
							float x = imgPosition.get(0).position.getLeft();
							float y = imgPosition.get(0).position.getBottom();
							image1.setAbsolutePosition(x, y);
							if("tm".equals(_key)){//不知道为什么设定了图像大小，为了使条码完整，排除条码id
								image1.scaleAbsolute(imgPosition.get(0).position.getWidth(), imgPosition.get(0).position.getHeight());
							}else{
								image1.scaleAbsolute(72, 30);
							}
							overContent.addImage(image1);
						} catch (Exception e) {
							System.out.println("图片连接为空： " + String.valueOf(data.get(_key)));
						}
					} else {
						String _value = String.valueOf(data.get(_key));
						form.setField(_key, _value);
					}
				}
			}
			if (String.valueOf(_key.charAt(0)).equals("_")) { // 有下划线的是CHECK类型
				String _checkKey = ConvertCheckName(_key);
				if (data.containsKey(_checkKey)) {
					if (!StringUtils.isEmpty(data.get(_checkKey)) && !data.get(_checkKey).toString().equals("null")) {
						String _checkValueKey = "_" + _checkKey + "_" + String.valueOf(data.get(_checkKey));
						if (_checkValueKey.equals(_key)) {
							form.setField(_checkValueKey, "√");// 设置CHECKBOX
						}
					}
				}
			}
		}
		stamp.setFormFlattening(true);
		stamp.close();
		reader.close();
		return bao;
	}

	/**
	 * 表单1[0].#subform[0].djzqmc[0] 转成djzqmc
	 * 
	 * @Title: ConvertToPdfName
	 * @author:WUZHU
	 * @date：2015年11月14日 下午5:03:38
	 * @param key
	 * @return
	 */
	private String ConvertToPdfName(String key) {
		String result = "";
		if (!StringUtils.isEmpty(key)) {
			int start = key.lastIndexOf(".") + 1;
			int end = key.length() - 3;
			result = key.substring(start, end);
		}
		return result;
	}

	/**
	 * _qllx_9 转成qllx
	 * 
	 * @Title: ConvertCheckName
	 * @author:WUZHU
	 * @date：2015年11月14日 下午5:03:51
	 * @param key
	 * @return
	 */
	private String ConvertCheckName(String key) {
		String result = "";
		if (!StringUtils.isEmpty(key)) {
			int start = 1;
			int end = key.lastIndexOf("_");
			result = key.substring(start, end);
		}
		return result;
	}

	/**
	 * 保存或更新BDCS_DJGD信息,并且返回BDCS_DJGD信息
	 * 
	 * @作者 海豹
	 * @创建时间 2015年6月7日下午9:16:33
	 * @param djgdid
	 * @param djgd
	 * @param re
	 * @return
	 */
	@RequestMapping(value = "/{djgdid}/SaveOrUpdateDJGD", method = RequestMethod.POST)
	public @ResponseBody ResultMessage saveOrUpdateDJGD(@PathVariable String djgdid, @ModelAttribute BDCS_DJGD djgd,
			BindingResult re) {
		ResultMessage ms = new ResultMessage();
		ms = projectService.saveOrUpdateDjgd(djgd, djgdid);
		return ms;
	}

	/**
	 * 获取登记归档信息（URL："/{djgdid}/SaveOrUpdateDJGD"，Method：GET）
	 * 
	 * @Title: getDjgdInfo
	 * @author:sunhaibao
	 * @date：2015年7月18日 下午6:12:36
	 * @param model
	 * @param xmbh
	 * @param projectid
	 * @return
	 */
	@RequestMapping(value = "/{xmbh}/{projectid}/getDjgdInfo", method = RequestMethod.GET)
	public @ResponseBody BDCS_DJGD getDjgdInfo(Model model, @PathVariable String xmbh, @PathVariable String projectid) {
		BDCS_DJGD djgd = projectService.getDjgdInfo(xmbh, projectid);
		model.addAttribute("djgdAttribute", djgd);
		return djgd;
	}

	/**
	 * 获取某个数据字典项（URL："/const/{consttype}"，Method：GET）
	 * 
	 * @Title: getConst
	 * @author:liushufeng
	 * @date：2015年7月18日 下午6:13:09
	 * @param consttype
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/const/{consttype}", method = RequestMethod.GET)
	public @ResponseBody List<BDCS_CONST> getConst(@PathVariable("consttype") String consttype,
			HttpServletRequest request, HttpServletResponse response) {
		String djsj = request.getParameter("djsj");
		List<BDCS_CONST> listconst = getConstByType(consttype);
		List<BDCS_CONST> list = new ArrayList<BDCS_CONST>();
		String gxsj = ConfigHelper.getNameByValue("DJXT_UPDATETIME");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Long xmdjsj = null;
		Long xtgxsj = null;
		try {
			if(!StringHelper.isEmpty(djsj)) {
				if(djsj.contains("-")||djsj.contains(":")){
					xmdjsj = sdf.parse(djsj).getTime();
				}else{
					xmdjsj=new Long(djsj);
				}
			}
			if(!StringHelper.isEmpty(gxsj)) {
				xtgxsj = sdf.parse(gxsj).getTime();
			}
		} catch (ParseException e) {
			System.err.println("时间格式转换错误");
			e.printStackTrace();
		}
		if (listconst != null && listconst.size()>0) {
				if(consttype.equals("TDYT")){
					Map map = ConstHelper.getNewDictionary(consttype);
					Set set = map.entrySet();
					Iterator i = set.iterator();
					while (i.hasNext()) {
						Map.Entry<String, String> entry1 = (Map.Entry<String, String>) i.next();
						BDCS_CONST c = new BDCS_CONST();
						c.setGJVALUE(entry1.getKey());
						c.setGJCONSTTRANS(entry1.getValue()+"_"+entry1.getKey());
						c.setSFSY("1");
						list.add(c);
					}
				}/*else if(consttype.equals("QLLX")&&(xmdjsj!=null&&xtgxsj!=null&&(xmdjsj>xtgxsj))){
					Map map = ConstHelper.getNewDictionary(consttype);
					Set set = map.entrySet();
					Iterator i = set.iterator();
					while (i.hasNext()) {
						Map.Entry<String, String> entry1 = (Map.Entry<String, String>) i.next();
						BDCS_CONST c = new BDCS_CONST();
						c.setGJVALUE(entry1.getKey());
						c.setGJCONSTTRANS(entry1.getValue());
						c.setSFSY("1");
						list.add(c);
					}
				}*/else{
					Map map = ConstHelper.getDictionary(consttype);
					Set set = map.entrySet();
					Iterator i = set.iterator();
					while (i.hasNext()) {
						Map.Entry<String, String> entry1 = (Map.Entry<String, String>) i.next();
						BDCS_CONST c = new BDCS_CONST();
						c.setCONSTVALUE(entry1.getKey());
						c.setCONSTTRANS(entry1.getValue());
						c.setSFSY("0");
						list.add(c);
					}
				}
		}
		list.add(new BDCS_CONST());
		return list;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/consts/constlist", method = RequestMethod.GET)
	public @ResponseBody Map getConsts(
			HttpServletRequest request, HttpServletResponse response) {
		String djsj = request.getParameter("djsj");
		String consttypeList = request.getParameter("constlist");
		String[] consttypes =  consttypeList.split(",");
		Map<String, List<BDCS_CONST>> consttypeMap = new HashMap<String, List<BDCS_CONST>>();
		for (String consttype : consttypes) {
			List<BDCS_CONST> listconst = getConstByType(consttype);
			List<BDCS_CONST> list = new ArrayList<BDCS_CONST>();
			String gxsj = ConfigHelper.getNameByValue("DJXT_UPDATETIME");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Long xmdjsj = null;
			Long xtgxsj = null;
			try {
				if(!StringHelper.isEmpty(djsj)) {
					if(djsj.contains("-")||djsj.contains(":")){
						xmdjsj = sdf.parse(djsj).getTime();
					}else{
						xmdjsj=new Long(djsj);
					}
				}
				if(!StringHelper.isEmpty(gxsj)) {
					xtgxsj = sdf.parse(gxsj).getTime();
				}
			} catch (ParseException e) {
				System.err.println("时间格式转换错误");
				e.printStackTrace();
			}
			if (listconst != null && listconst.size()>0) {
				if(consttype.equals("TDYT")){
					Map map = ConstHelper.getNewDictionary(consttype);
					Set set = map.entrySet();
					Iterator i = set.iterator();
					while (i.hasNext()) {
						Map.Entry<String, String> entry1 = (Map.Entry<String, String>) i.next();
						BDCS_CONST c = new BDCS_CONST();
						c.setGJVALUE(entry1.getKey());
						c.setGJCONSTTRANS(entry1.getValue()+"_"+entry1.getKey());
						c.setSFSY("1");
						list.add(c);
					}
				}/*else if(consttype.equals("QLLX")&&(xmdjsj!=null&&xtgxsj!=null&&(xmdjsj>xtgxsj))){
					Map map = ConstHelper.getNewDictionary(consttype);
					Set set = map.entrySet();
					Iterator i = set.iterator();
					while (i.hasNext()) {
						Map.Entry<String, String> entry1 = (Map.Entry<String, String>) i.next();
						BDCS_CONST c = new BDCS_CONST();
						c.setGJVALUE(entry1.getKey());
						c.setGJCONSTTRANS(entry1.getValue());
						c.setSFSY("1");
						list.add(c);
					}
				}*/else{
					Map map = ConstHelper.getDictionary(consttype);
					Set set = map.entrySet();
					Iterator i = set.iterator();
					while (i.hasNext()) {
						Map.Entry<String, String> entry1 = (Map.Entry<String, String>) i.next();
						BDCS_CONST c = new BDCS_CONST();
						c.setCONSTVALUE(entry1.getKey());
						c.setCONSTTRANS(entry1.getValue());
						c.setSFSY("0");
						list.add(c);
					}
				}
			}
			list.add(new BDCS_CONST());
			consttypeMap.put(consttype, list);
		}
		return consttypeMap;
	}
	
	@SuppressWarnings("unchecked")
	private List<BDCS_CONST> getConstByType(String typeName) {
		List<BDCS_CONST> consts = new ArrayList<BDCS_CONST>();
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("SELECT * FROM bdck.BDCS_CONST T WHERE CONSTSLSID =");
		sqlBuilder.append(" (SELECT cONSTSLSID FROM bdck.BDCS_CONSTCLS WHERE CONSTCLSTYPE = '"+typeName+"') order by SFSY, CONSTORDER");
		consts = commonDao.getDataList(BDCS_CONST.class,sqlBuilder.toString());
		if (consts.size() > 0) {
			return consts;
		} else {
			return new ArrayList<BDCS_CONST>();
		}
	}
	
	/**
	 * 获取某个数据字典项（URL："/const/{consttype}"，Method：GET）
	 * 
	 * @Title: getConst2
	 * @author:yuxuebin
	 * @date：2017年03月16日 15:09:09
	 * @param consttype
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings({ "rawtypes"})
	@RequestMapping(value = "/const2/{consttype}", method = RequestMethod.GET)
	public @ResponseBody Map getConst2(@PathVariable("consttype") String consttype,
			HttpServletRequest request, HttpServletResponse response) {
		Map map = ConstHelper.getDictionary(consttype);
		return map;
	}

	/**
	 * 获取登记归档附属列表（URL："/{xmbh}/{project_id}/djgdfs"，Method：GET）
	 * 
	 * @Title: getdjgdfslist
	 * @author:liushufeng
	 * @date：2015年7月18日 下午6:14:54
	 * @param xmbh
	 * @param project_id
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/{xmbh}/{project_id}/djgdfs", method = RequestMethod.GET)
	public @ResponseBody List<BDCS_DJGDFS> getdjgdfslist(@PathVariable("xmbh") String xmbh,
			@PathVariable("project_id") String project_id, HttpServletRequest request, HttpServletResponse response) {
		List<BDCS_DJGDFS> list = projectService.getDJGDFSList(xmbh, project_id);
		return list;
	}

	/**
	 * 添加或保存登记归档附属（URL："/{xmbh}/{project_id}/djgdfs"，Method：POST）
	 * 
	 * @Title: saveorupdatedjgdfs
	 * @author:liushufeng
	 * @date：2015年7月18日 下午6:16:17
	 * @param xmbh
	 * @param project_id
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "/{xmbh}/djgdfs", method = RequestMethod.POST)
	public @ResponseBody List<BDCS_DJGDFS> saveorupdatedjgdfs(@PathVariable("xmbh") String xmbh,
			HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		// TODO @孙海豹 删除路径中的project_id，没用，修改相应的前台页面（完成）
		String id = request.getParameter("ID");
		String catalog = request.getParameter("CATALOG");
		String filename = request.getParameter("FILENAME");
		String sjry = request.getParameter("SJRY");
		String sjsj = request.getParameter("SJSJ");
		BDCS_DJGDFS gdfs = new BDCS_DJGDFS();
		if (!StringUtils.isEmpty(id)) {
			gdfs.setId(id);
		}
		gdfs.setCATALOG(catalog);
		gdfs.setFILENAME(filename);
		gdfs.setSJRY(sjry);
		gdfs.setXMBH(xmbh);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date qssj = new Date();
		try {
			qssj = sdf.parse(sjsj);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		gdfs.setSJSJ(qssj);
		projectService.saveOrUpdateDJGDFS(gdfs);
		YwLogUtil.addYwLog("保存或增加登记归档附属-保存成功", ConstValue.SF.YES.Value, ConstValue.LOG.UPDATE);
		return null;
	}

	/**
	 * 获取登记归档附属详细信息（URL："/{xmbh}/{project_id}/djgdfs/{id}"，Method：POST）
	 * 
	 * @Title: getdjgdfs
	 * @author:liushufeng
	 * @date：2015年7月18日 下午6:17:53
	 * @param xmbh
	 * @param project_id
	 * @param id
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "/{xmbh}/djgdfs/{id}", method = RequestMethod.POST)
	public @ResponseBody BDCS_DJGDFS getdjgdfs(@PathVariable("xmbh") String xmbh, @PathVariable("id") String id,
			HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		// TODO @孙海豹 删除路径中的project_id，没用，修改相应的前台页面(没有找到相应的js文件)
		return projectService.getDJGDFSInfo(xmbh, id);
	}

	/**
	 * 移除归档附属详细信息（URL："/{xmbh}/{project_id}/djgdfs/{id}"，Method：DELETE）
	 * 
	 * @作者 刘树峰
	 * @创建时间 2015年6月23日上午8:20:33
	 * @param xmbh
	 * @param project_id
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "/{xmbh}/djgdfs/{id}", method = RequestMethod.DELETE)
	public @ResponseBody ResultMessage removedjgdfs(@PathVariable("xmbh") String xmbh, @PathVariable("id") String id,
			HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		// TODO @孙海豹 删除路径中的project_id，没用，修改相应的前台页面（完成）
		return projectService.deleteDJGDFS(xmbh, id);
	}

	/**
	 * 获取地图配置地址（URL："/GetMapConfig"，Method：GET）
	 * 
	 * @Title: GetMapConfig
	 * @author:rongxianfeng
	 * @date：2015年7月18日 下午6:19:09
	 * @return
	 */
	@RequestMapping(value = "/GetMapConfig", method = RequestMethod.GET)
	public @ResponseBody JSONObject GetMapConfig() {
		return projectService.GetMapConfig();
	}

	/**
	 * 获取不动产登记受理单/询问笔录/收费明细单信息（URL："/{project_id}/djinfo"，Method：GET）
	 * 
	 * @Title: GetDJInfo
	 * @author:yuxuebin
	 * @date：2015年7月18日 下午6:20:19
	 * @param project_id
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/{project_id}/djinfo", method = RequestMethod.GET)
	public @ResponseBody DJInfo GetDJInfo(@PathVariable("project_id") String project_id, HttpServletRequest request,
			HttpServletResponse response) {
		DJInfo info = new DJInfo();
		info = projectService.GetDJInfo(project_id);
		return info;
	}

	/**
	 * 下载xml交换文件（URL："/{xmbh}/exchangefile"，Method：GET）
	 * 
	 * @Title: exportXML
	 * @author:liushufeng
	 * @date：2015年7月18日 下午6:21:15
	 * @param xmbh
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("deprecation")
	@RequestMapping(value = "/{xmbh}/{actinstID}/exchangefile", method = RequestMethod.GET)
	public @ResponseBody ResultMessage exportXML(@PathVariable("xmbh") String xmbh,
			@PathVariable("actinstID") String actinstID, HttpServletRequest request, HttpServletResponse response) {
		Map<String, String> filenames = new HashMap<String, String>();
		String bdcqzh = "";
		ResultMessage msg = new ResultMessage();
		try {
			String path = request.getRealPath("/");
			Map<String, String> filename = projectService.exportXML(xmbh, path, actinstID);
			if (filename.get("error") != null) {
				msg.setSuccess("false");
				msg.setMsg(filename.get("error"));
				YwLogUtil.addYwLog("数据上报-校验失败!", ConstValue.SF.NO.Value, ConstValue.LOG.ADD);
				return msg;
			}
			// 上报到我们自己的省级接入系统
			String configvalue = ConfigHelper.getNameByValue("UoloadDestination");
			if ("2".equals(configvalue)) {
				msg.setSuccess("true");
				msg.setMsg("响应成功");
				return msg;
			}
			// 通过wsdl服务调用
			if ("4".equals(configvalue)) {
				msg.setSuccess("true");
				msg.setMsg("响应成功");
				return msg;
			}
			Thread.sleep(2000);

			String returnpath = "resources/changefiles/";
			path = path + returnpath;
			filenames = projectService.downXmlFile(path, filename, actinstID);
			// 保存证号
			// projectService.saveBDCQZH(xmbh, filenames);

			@SuppressWarnings("rawtypes")
			Set keys = filenames.keySet();
			if (keys != null) {
				@SuppressWarnings("rawtypes")
				Iterator iterator = keys.iterator();
				while (iterator.hasNext()) {
					Object key = iterator.next();
					Object value = filenames.get(key);
					bdcqzh = StringHelper.formatObject(value);
					break;
				}
			}

		} catch (Exception e) {
			YwLogUtil.addYwLog("数据上报-失败", ConstValue.SF.NO.Value, ConstValue.LOG.ADD);
			e.printStackTrace();
		}
		msg.setSuccess("true");
		msg.setMsg(bdcqzh);
		YwLogUtil.addYwLog("数据上报", ConstValue.SF.YES.Value, ConstValue.LOG.ADD);
		return msg;
	}

	/**
	 * @Title: calculate
	 * @author:liushufeng
	 * @date：2015年7月26日 下午5:55:37
	 * @param xmbh
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/charge/calculate/{xmbh}", method = RequestMethod.GET)
	public @ResponseBody String calculate(@PathVariable("xmbh") String xmbh, HttpServletRequest request,
			HttpServletResponse response) {
		try {
			chargeService.calculate(xmbh);
		} catch (parsii.tokenizer.ParseException e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 获取所有收费项目，除去在登记收费表DJSF中已有的记录（根据项目编号）
	 * 不分页（URL:"/djsf/all/{xmbh}",Method:GET）
	 * 
	 * @作者：郭浩龙
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/sfdylist/{xmbh}", method = RequestMethod.GET)
	public @ResponseBody Message getSFDYList(@PathVariable String xmbh, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Integer page = 1;
		if (request.getParameter("page") != null) {
			page = Integer.parseInt(request.getParameter("page"));
		}
		Integer rows = 10;
		if (request.getParameter("rows") != null) {
			rows = Integer.parseInt(request.getParameter("rows"));
		}
		Message msg = projectService.getSFDYList(xmbh, page, rows);
		// List<BDCS_SFDY> allSFDY = projectService.getSFDYList(xmbh,page,rows);
		return msg;
	}

	/**
	 * 通过file_number获取xmbh
	 */

	@RequestMapping(value = "/getxmbh/{file_number}", method = RequestMethod.GET)
	@ResponseBody
	public String GetXmbhData(@PathVariable String file_number, HttpServletRequest request,
			HttpServletResponse response) {
		if (file_number != null && !file_number.equals("")) {
			return projectService.GetXmbhData(file_number);
		} else {
			return null;
		}

	}

	/**
	 * 获取申请审核表（URL："/{xmbh}/sqspb"，Method：GET）
	 * 
	 * 
	 * @author:mss
	 * @param xmbh
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/{project_id}/sqshb", method = RequestMethod.GET)
	public @ResponseBody ResultSHB GetSHBInfo(@PathVariable("project_id") String project_id, HttpServletRequest request,
			HttpServletResponse response) {
		ResultSHB rsSHB = new ResultSHB();
		SHB shb = projectService.GetSHB(project_id);
		if (shb != null) {
			rsSHB.setShb(shb);
			rsSHB.setSucesses(true);
		} else {
			rsSHB.setShb(null);
			rsSHB.setSucesses(false);
		}
		return rsSHB;
	}

	/**
	 * 设置项目信息里边是否合并证书的值（URL："/{xmbh}/sfhbzs/{sfhbzs}"，Method：GET）
	 * 
	 * @Title: setSFHBZS
	 * @author:liushufeng
	 * @date：2015年11月14日 下午5:15:31
	 * @param xmbh
	 * @param sfhbzs
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/{xmbh}/sfhbzs/{sfhbzs}")
	@AccessRequired.UnBoardRequired
	public @ResponseBody ResultMessage setSFHBZS(@PathVariable("xmbh") String xmbh,
			@PathVariable("sfhbzs") String sfhbzs, HttpServletRequest request, HttpServletResponse response) {
		ResultMessage msg = new ResultMessage();
		msg = projectService.setSFHBZS(xmbh, sfhbzs);
		return msg;
	}

	/**
	 * 根据file_numbers获取档案移交信息（URL："/transferinfo/{file_numbers}/"，Method：GET）
	 * 
	 * @Title: GetFileTransferInfo
	 * @author:俞学斌
	 * @date：2015年11月22日 下午3:18:45
	 * @param file_number
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/transferinfo/{file_numbers}/", method = RequestMethod.GET)
	public @ResponseBody List<Map> GetFileTransferInfo(@PathVariable("file_numbers") String file_numbers,
			HttpServletRequest request, HttpServletResponse response) {
		List<Map> list = new ArrayList<Map>();
		if (StringHelper.isEmpty(file_numbers)) {
			return list;
		}
		String[] strs_file_number = file_numbers.split(",");
		if (strs_file_number == null || strs_file_number.length < 1) {
			return list;
		}
		for (String file_number : strs_file_number) {
			Map m = ProjectHelper.GetFileTransferInfo(file_number);
			if (m != null) {
				list.add(m);
			}
		}
		YwLogUtil.addYwLog("获取档案移交信息", ConstValue.SF.YES.Value, ConstValue.LOG.SEARCH);
		return list;
	}

	/**
	 * 获取登簿信息（URL："/{project_id}"，Method：GET）
	 * 
	 * @Title: GetDBXX
	 * @author:mss
	 * @date：2015年12月3日 下午
	 * @param project_id
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/{project_id}/dbxx", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> Get(@PathVariable("project_id") String project_id,
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		@SuppressWarnings("unchecked")
		Map<String, Object> info = projectService.GetDBXX(project_id);
		return info;
	}

	@RequestMapping(value = "/{project_id}/v2/dbxx", method = RequestMethod.GET)
	public @ResponseBody Map<String, List<HashMap<String, Object>>> Get2(@PathVariable("project_id") String project_id,
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		@SuppressWarnings("unchecked")
		Map<String, List<HashMap<String, Object>>> info = projectService.GetDBXX2(project_id);
		return info;
	}

	@RequestMapping(value = "/{xmbh}/{sfid}/{ts}", method = RequestMethod.GET)
	public @ResponseBody ResultMessage alterTS(@PathVariable("xmbh") String xmbh, @PathVariable("sfid") String sfid,
			@PathVariable("ts") String ts, HttpServletRequest request, HttpServletResponse response) {
		ResultMessage msg = new ResultMessage();

		try {

			int its = Integer.parseInt(ts);
			Map<String,Object> param = new HashMap<String, Object>();
			param.put("qlrmc", RequestHelper.getParam(request, "qlrmc"));
			msg = projectService.setDJSFTS(sfid, its, param);
		} catch (Exception e) {
			msg.setSuccess("false");
			msg.setMsg(e.getMessage());
		}
		return msg;
	}

	/**
	 * @作者 wuz
	 * @创建时间 2015年12月4日上午12:36:14 向非登记系统提供获取申请审批表信息的对外接口,刚开始是给西安不动产档案系统调用
	 * @param file_numbers
	 * @param acinstid
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/getsqspbstream/{file_numbers}/{acinstid}", method = RequestMethod.GET)
	public @ResponseBody void GetSPBPDF(@PathVariable("file_numbers") String file_numbers,
			@PathVariable("acinstid") String acinstid, HttpServletRequest request, HttpServletResponse response) {
		ResultMessage msg = new ResultMessage();
		String basePath = request.getRealPath("/") + "\\resources\\PDF";// 存放PDF文件模板或临时文件夹路径
		String outpath = basePath + "\\tmp\\" + file_numbers + "-" + acinstid + ".pdf";// 导出路径
		String tplsqb = basePath + "\\tpl\\bdcdjsqb.pdf";// 申请表PDF模板
		String tplspb = basePath + "\\tpl\\bdcdjspb.pdf";// 审批表PDF模板
		String tplsqbfb = basePath + "\\tpl\\bdcdjsqbfb.pdf";// 申请表附表（单元）PDF模板
		String tplsqr = basePath + "\\tpl\\bdcdjsqr.pdf";// 申请表附表（申请人）PDF模板
		try {
			ProjectInfo info = projectService.getProjectInfo(file_numbers, request);
			String xmbh = info.getXmbh();
			SQSPB sqspb = projectService.GetSQSPBex(xmbh, acinstid, request).converToSQSPB();
			Map<String, Object> sqbdata = new HashMap<String, Object>();// 申请表数据
			Map<String, Object> spbdata = new HashMap<String, Object>();// 审批表数据
			Map<String, Object> sqbfbdata = new HashMap<String, Object>();// 申请表附表（单元）数据
			Map<String, Object> sqrdata = new HashMap<String, Object>();// 申请表附表（申请人）数据
			// 组织申请表数据
			sqbdata.put("bh", sqspb.getBh());
			sqbdata.put("sjr", sqspb.getSjr());
			sqbdata.put("dw", sqspb.getDw());
			sqbdata.put("rq", sqspb.getRq());
			sqbdata.put("djlxmc", sqspb.getDjlxmc());
			sqbdata.put("qllxmc", sqspb.getQllxmc());
			sqbdata.put("qlrxm", sqspb.getQlrxm());
			sqbdata.put("dh", sqspb.getDh());
			sqbdata.put("zjzl", sqspb.getZjzl());
			sqbdata.put("zjh", sqspb.getZjh());
			sqbdata.put("dz", sqspb.getDz());
			sqbdata.put("fddbr", sqspb.getFddbr());
			sqbdata.put("fddbrdh", sqspb.getFddbrdh());
			sqbdata.put("dlrxm", sqspb.getDlrxm());
			sqbdata.put("dlrdh", sqspb.getDlrdh());
			sqbdata.put("dljgmc", sqspb.getDljgmc());
			sqbdata.put("yb", sqspb.getYb());
			sqbdata.put("qlrxm1", sqspb.getQlrxm1());
			sqbdata.put("dh1", sqspb.getDh1());
			sqbdata.put("zjzl1", sqspb.getZjzl1());
			sqbdata.put("zjh1", sqspb.getZjh1());
			sqbdata.put("dz1", sqspb.getDz1());
			sqbdata.put("fddbr1", sqspb.getFddbr1());
			sqbdata.put("fddbrdh1", sqspb.getFddbrdh1());
			sqbdata.put("dlrxm1", sqspb.getDlrxm1());
			sqbdata.put("dlrdh1", sqspb.getDlrdh1());
			sqbdata.put("dljgmc1", sqspb.getDljgmc1());
			sqbdata.put("zl", sqspb.getZl());
			sqbdata.put("bdcdyh", sqspb.getBdcdyh());
			sqbdata.put("bdclx", sqspb.getBdclx());
			sqbdata.put("mj", sqspb.getMj());
			sqbdata.put("yt", sqspb.getYt());
			sqbdata.put("ybdcqzsh", sqspb.getYbdcqzsh());
			sqbdata.put("yhlx", sqspb.getYhlx());
			sqbdata.put("gzwlx", sqspb.getGzwlx());
			sqbdata.put("lz", sqspb.getLz());
			sqbdata.put("bdbe", sqspb.getBdbe());
			sqbdata.put("qx", sqspb.getQx());
			sqbdata.put("dyfw", sqspb.getDyfw());
			sqbdata.put("xydzl", sqspb.getXydzl());
			sqbdata.put("xydbdcdyh", sqspb.getXydbdcdyh());
			sqbdata.put("djyy", sqspb.getDjyy());
			sqbdata.put("zsbs", sqspb.getZsbs());
			sqbdata.put("fbcz", sqspb.getFbcz());
			sqbdata.put("bz", sqspb.getBz());
			sqbdata.put("sqrqz", sqspb.getSqrqz());
			sqbdata.put("sqrqz2", sqspb.getSqrqz2());
			sqbdata.put("dlrqz", sqspb.getDlrqz());
			sqbdata.put("dlrqz2", sqspb.getDlrqz2());
			sqbdata.put("qzrq", sqspb.getQzrq());
			sqbdata.put("qzrq2", sqspb.getQzrq2());
			// 组织审批表数据
			spbdata.put("ywlx", sqspb.getEx().get("ywlx"));
			spbdata.put("slbh", sqspb.getBh());// 受理编号和编号为同一
			spbdata.put("bdczl", sqspb.getZl());
			spbdata.put("page2_slr", sqspb.getSjr());
			spbdata.put("page2_slrq", sqspb.getRq());
			spbdata.put("csyj", sqspb.getEx().get("csyj"));
			spbdata.put("csyj_scr", sqspb.getEx().get("csyj_scr"));
			spbdata.put("csyj_scr_src", sqspb.getEx().get("csyj_scr_src"));
			spbdata.put("csyj_scrq", sqspb.getEx().get("csyj_scrq"));
			spbdata.put("fsyj", sqspb.getEx().get("fsyj"));
			spbdata.put("fsyj_scr", sqspb.getEx().get("fsyj_scr"));
			spbdata.put("fsyj_scrq", sqspb.getEx().get("fsyj_scrq"));
			spbdata.put("fsyj_scr_src", sqspb.getEx().get("fsyj_scr_src"));
			spbdata.put("spyj", sqspb.getEx().get("spyj"));
			spbdata.put("spyj_scr", sqspb.getEx().get("spyj_scr"));
			spbdata.put("spyj_scrq", sqspb.getEx().get("spyj_scrq"));
			spbdata.put("spyj_scr_src", sqspb.getEx().get("spyj_scr_src"));
			spbdata.put("kfqfsyj", sqspb.getEx().get("kfqfsyj"));
			spbdata.put("kfqfsyj_scr", sqspb.getEx().get("kfqfsyj_scr"));
			spbdata.put("kfqfsyj_scrq", sqspb.getEx().get("kfqfsyj_scrq"));
			spbdata.put("kfqfsyj_scr_src", sqspb.getEx().get("kfqfsyj_scr_src"));
			spbdata.put("shyj", sqspb.getEx().get("shyj"));
			spbdata.put("shyj_scr", sqspb.getEx().get("shyj_scr"));
			spbdata.put("shyj_scrq", sqspb.getEx().get("shyj_scrq"));
			spbdata.put("shyj_scr_src", sqspb.getEx().get("shyj_scr_src"));
			spbdata.put("EX_DBR", sqspb.getEx().get("EX_DBR"));
			spbdata.put("EX_DJSJ", sqspb.getEx().get("EX_DJSJ"));
			spbdata.put("EX_SZRY", sqspb.getEx().get("EX_SZRY"));
			spbdata.put("EX_SZSJ", sqspb.getEx().get("EX_SZSJ"));
			spbdata.put("EX_FZRY", sqspb.getEx().get("EX_FZRY"));
			spbdata.put("EX_FZSJ", sqspb.getEx().get("EX_FZSJ"));
			spbdata.put("EX_LZRXM", sqspb.getEx().get("EX_LZRXM"));
			spbdata.put("EX_LZSJ", sqspb.getEx().get("EX_LZSJ"));
			spbdata.put("EX_LZRZJHM", sqspb.getEx().get("EX_LZRZJHM"));
			spbdata.put("EX_GDZR", sqspb.getEx().get("EX_GDZR"));
			spbdata.put("EX_GDSJ", sqspb.getEx().get("EX_GDSJ"));
			// 组织单元附表数据
			int i = 1;
			for (Map<String, String> house : sqspb.getHouses()) {
				sqbfbdata.put("XH__" + i, i);
				sqbfbdata.put("H_BDCDYH__" + i, house.get("H_BDCDYH"));
				sqbfbdata.put("H_BDCDYH__" + i, house.get("H_BDCDYH"));
				sqbfbdata.put("H_FH__" + i, house.get("H_FH"));
				sqbfbdata.put("H_SCJZMJ__" + i, house.get("H_SCJZMJ"));
				sqbfbdata.put("H_SCTNJZMJ__" + i, house.get("H_SCTNJZMJ"));
				sqbfbdata.put("H_SCFTJZMJ__" + i, house.get("H_SCFTJZMJ"));
				sqbfbdata.put("H_SJCS__" + i, house.get("H_SJCS"));
				sqbfbdata.put("H_GHYT__" + i, house.get("H_GHYT"));
				sqbfbdata.put("H_ZRZH__" + i, house.get("H_ZRZH"));
				sqbfbdata.put("H_ZL__" + i, house.get("H_ZL"));
				sqbfbdata.put("H_TNMJ__" + i, house.get("H_TNMJ"));
				sqbfbdata.put("H_BDCQZH__" + i, house.get("H_BDCQZH"));
				i++;
			}
			// 组织申请人附表数据
			int j = 1;
			for (Map<String, String> sqr : sqspb.getSqrs()) {
				sqrdata.put("SQR_XH__" + j, j);
				sqrdata.put("SQR_SQRXM__" + j, sqr.get("SQR_SQRXM"));
				sqrdata.put("SQR_ZJLX__" + j, sqr.get("SQR_ZJLX"));
				sqrdata.put("SQR_ZJH__" + j, sqr.get("SQR_ZJH"));
				sqrdata.put("SQR_TXDZ__" + j, sqr.get("SQR_TXDZ"));
				sqrdata.put("SQR_YZBM__" + j, sqr.get("SQR_YZBM"));
				sqrdata.put("SQR_FDDBR__" + j, sqr.get("SQR_FDDBR"));
				sqrdata.put("SQR_LXDH__" + j, sqr.get("SQR_LXDH"));
				sqrdata.put("SQR_DLRXM__" + j, sqr.get("SQR_DLRXM"));
				sqrdata.put("SQR_DLRLXDH__" + j, sqr.get("SQR_DLRLXDH"));
				sqrdata.put("SQR_DLJGMC__" + j, sqr.get("SQR_DLJGMC"));
				sqrdata.put("SQR_SQRLB__" + j, sqr.get("SQR_SQRLB"));
				j++;
			}
			ByteArrayOutputStream outstream = new ByteArrayOutputStream();// 用来保存生成最终PDF的流
			ArrayList<ByteArrayOutputStream> baos = new ArrayList<ByteArrayOutputStream>();
			// 用在单元附表和申请人附表分页显示时候用

			ByteArrayOutputStream sqbfos = CreatePdfStream(tplsqb, sqbdata);// 申请表PDF模板和数据形成流
			baos.add(sqbfos);
			ByteArrayOutputStream spbfos = CreatePdfStream(tplspb, spbdata);// 审批表PDF模板和数据形成流
			baos.add(spbfos);
			Map<String, Map> housePageDatas = ConverDataToPageData(sqbfbdata, request);// 单元附件带分页的数据和PDF模板转成流
			for (Entry<String, Map> pageData : housePageDatas.entrySet()) {
				ByteArrayOutputStream fos = CreatePdfStream(tplsqbfb, pageData.getValue());
				baos.add(fos);
			}
			Map<String, Map> sqrPageDatas = ConverDataToPageData(sqrdata, request);// 申请人附件带分页的数据和PDF模板转成流
			for (Entry<String, Map> pageData : sqrPageDatas.entrySet()) {
				ByteArrayOutputStream fos = CreatePdfStream(tplsqr, pageData.getValue());
				baos.add(fos);
			}
			// FileOutputStream outstream2 = new
			// FileOutputStream(outpath);//可用该流生成PDF文件
			if (baos.size() > 0) {// 将流集合整成一个大的PDF文件
				Document doc = new Document();
				PdfCopy pdfCopy = new PdfCopy(doc, outstream);
				doc.open();
				PdfImportedPage impPage = null;
				PdfReader reader = null;
				for (ByteArrayOutputStream bao : baos) {
					reader = new PdfReader(bao.toByteArray());
					int page = reader.getNumberOfPages();
					for (int p = 1; p <= page; p++) {
						impPage = pdfCopy.getImportedPage(reader, p);
						pdfCopy.addPage(impPage);
					}
				}
				doc.close();
			}
			outstream.writeTo(response.getOutputStream());
			// return outstream.toString();
		} catch (Exception e) {
			System.out.print("将申请审批表信息转成流出异常：" + e.getMessage());
			// return new ByteArrayOutputStream().toString();
		}
	}

	/**
	 * @作者 wuz
	 * @创建时间 2015年12月3日下午11:19:49 将MAP数据转成带分页符号的Map数据 只用在申请表的生成PDF 上
	 * @param inputdata
	 *            进入的原数据 * @param inputdata 单页大小
	 * @return
	 * @throws UnknownHostException
	 */
	private Map<String, Map> ConverDataToPageData(Map<String, Object> inputdata, HttpServletRequest request)
			throws UnknownHostException {
		Map<String, Map> pageDatas = new TreeMap<String, Map>(new Comparator<String>() {
			public int compare(String obj1, String obj2) {
				// 升序排序return obj1.compareTo(obj2);
				return Integer.parseInt(obj1) - Integer.parseInt(obj2);

			}
		});
		String keyName = "";
		int keyPageNum = 0;
		pageDatas.put("1", new HashMap<String, Object>());// 初始化第一页
		for (Entry<String, Object> entry : inputdata.entrySet()) {
			keyPageNum = getPageNum(entry.getKey(),0);
			keyName = getID(entry.getKey(), keyPageNum,0);
			Object entryvalue = entry.getValue();
			if (entryvalue != null) {
				String imageurl = entryvalue.toString();
				Pattern ptn = Pattern.compile("http://.*/realestate.*");
				Matcher mt = ptn.matcher(imageurl);
				if (mt.matches()) {
					String ip = InetAddress.getLocalHost().getHostAddress();
					String port = request.getLocalPort() + "";
					imageurl = imageurl.replaceAll("http://.*/realestate", "http://" + ip + ":" + port + "/realestate");
					entryvalue = imageurl;
				}

			}
			if (pageDatas.containsKey(String.valueOf(keyPageNum)))// 如果有该页记录则将记录插入该页
				pageDatas.get(String.valueOf(keyPageNum)).put(keyName, entryvalue);
			else {
				pageDatas.put(String.valueOf(keyPageNum), new HashMap<String, Object>());// 先初始化没有的页记录
				pageDatas.get(String.valueOf(keyPageNum)).put(keyName, entryvalue);
			}
		}
		return pageDatas;
	}

	/**
	 * @作者 buxiaobo
	 * @创建时间 2015年12月5日 22:13:31 根据project_id返回缴款书相应信息
	 * @param project_id
	 *            进入的原数据
	 * @return JKSInfo缴款书list
	 * @throws SQLException
	 */
	@RequestMapping(value = "/getjksinfo/{project_id}", method = RequestMethod.GET)
	private @ResponseBody JKSInfo GetJKSInfo(@PathVariable("project_id") String project_id,
			HttpServletRequest request) {
		String type = request.getParameter("type");
		JKSInfo jksinfo = projectService.GetJKSInfo(project_id, type);
		return jksinfo;
	}

	/**
	 * 档案柜号管理页面 (URL:"/showdamanager/",Method：GET)
	 * 
	 * @Title: ShowDAGHManager
	 * @author:yuxuebin
	 * @date：2015年12月13日 下午03:49:33
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/showdamanager", method = RequestMethod.GET)
	public String ShowDAGHManager(Model model) {
		return "/realestate/registration/archives/archives";
	}

	/**
	 * 获取档案柜号（URL："/{ywlsh}/dagh/"，Method：GET）
	 * 
	 * @author:yuxuebin
	 * @param ywlsh
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/{ywlsh}/dagh/", method = RequestMethod.GET)
	public @ResponseBody ResultMessage GetDAGH(@PathVariable("ywlsh") String ywlsh, HttpServletRequest request,
			HttpServletResponse response) {
		ResultMessage ms = new ResultMessage();
		ms = projectService.GetDAGH(ywlsh);
		return ms;
	}

	/**
	 * 保存档案柜号（URL："/{ywlsh}/dagh/"，Method：POST）
	 * 
	 * @author:yuxuebin
	 * @param ywlsh
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/{ywlsh}/dagh/", method = RequestMethod.POST)
	public @ResponseBody ResultMessage SaveDAGH(@PathVariable("ywlsh") String ywlsh, HttpServletRequest request,
			HttpServletResponse response) {
		ResultMessage ms = new ResultMessage();
		Integer dagh = 0;
		if (request.getParameter("dagh") != null) {
			dagh = Integer.parseInt(request.getParameter("dagh"));
		}
		if (dagh <= 0) {
			ms.setMsg("请输入有效档案柜号！");
			ms.setSuccess("false");
			return ms;
		}
		ms = projectService.SaveDAGH(ywlsh, dagh);
		return ms;
	}

	/**
	 * 银行委托书预览 根据银行名称返回银行委托书原图二进制字符串
	 * 
	 * @return
	 * 
	 * @作者 buxiaobo
	 * @创建时间 2015年12月30日 02:08:23
	 * @return byte[]
	 */
	@RequestMapping(value = "/previewBank_Trustbookpage/{trustbook_id}", method = RequestMethod.GET)
	public @ResponseBody String PreviewBank_Trustbookpage(@PathVariable String trustbook_id, HttpServletRequest request)
			throws IOException {
		// String trustbook_id = request.getParameter("yhyyzzh");
		// String file_name = request.getParameter("yhmc");
		List<Bank_Trustbook> bank_Trustbooks = commonDao.getDataList(Bank_Trustbook.class,
				"select * from BDC_WORKFLOW.BANK_TRUSTBOOK  WHERE trustbook_id='" + trustbook_id + "'");
		String trustbookpage_id = bank_Trustbooks.get(0).getTrustbookpage_Id();
		List<Bank_TrustbookPage> bank_TrustbookPages = commonDao.getDataList(Bank_TrustbookPage.class,
				"select * from BDC_WORKFLOW.BANK_TRUSTBOOKPAGE  WHERE trustbookpage_id='" + trustbookpage_id + "'");
		Bank_TrustbookPage data = bank_TrustbookPages.get(0);
		String base64png = FileUpload.trustbookpagegetFile(data.getTrustbookpage_Id(), data.getTrustbookpage_Path());
		if (base64png != null) {
			return base64png;
		} else {
			return null;
		}
	}

	/**
	 * 申请表页面自动填充：根据银行名称获取银行营业执照号
	 * 
	 * @作者 buxiaobo
	 * @创建时间2016年1月8日 16:30:57
	 * @param bank_name
	 * @return Bank_Trustbook
	 */
	@RequestMapping(value = "/getbankyyzzh/{bank_name}", method = RequestMethod.GET)
	public @ResponseBody Map<String,String> getbankyyzzh(@PathVariable String bank_name, HttpServletRequest request) {
		Map<String,String> map=new HashMap<String,String>();
		try {
			String bank_name1 = java.net.URLDecoder.decode(bank_name, "UTF-8");
			List<Bank_Trustbook> bank_Trustbooks = commonDao.getDataList(Bank_Trustbook.class,
					"select * from BDC_WORKFLOW.BANK_TRUSTBOOK  WHERE BANK_NAME='" + bank_name1 + "'");
			if (!bank_Trustbooks.isEmpty()) {
				String yyzzh = bank_Trustbooks.get(0).getTrustbook_Id();
				String yhgdbz = bank_Trustbooks.get(0).getTrustbook_Desc();
				map.put("yyzzh", yyzzh);
				map.put("yhgdbz", yhgdbz);
				return map;
			} else {
				return map;
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 将申请人名称设置为项目名称
	 * 
	 * @param xmbh
	 * @return
	 */
	@RequestMapping(value = "/renamexmmc/{xmbh}", method = RequestMethod.POST)
	public @ResponseBody String RenameXmmc(HttpServletRequest request, @PathVariable String xmbh) {
		projectService.RenameXmmc(xmbh);
		return "true";
	}

	/**
	 * 新增一条打印记录（URL："/printrecord/{project_id}/{printtype}"，Method：POST）
	 * 
	 * @Title: addPrintRecord
	 * @author:liushufeng
	 * @date：2016年3月3日 下午4:21:47
	 * @param project_id
	 *            project_id
	 * @param printtype
	 *            打印类型（自定义）
	 * @param request
	 * @return ResultMessage对象，成功或失败
	 */
	@RequestMapping(value = "/printrecord/{project_id}/{printtype}", method = RequestMethod.POST)
	public @ResponseBody ResultMessage addPrintRecord(@PathVariable String project_id, @PathVariable String printtype,
			HttpServletRequest request) {

		BDCS_PRINTRECORD record = new BDCS_PRINTRECORD();
		String username = Global.getCurrentUserName();
		Date date = new Date();
		record.setPRINTSTAFF(username);
		record.setPRINTTIME(date);
		record.setPRINTTYPE(printtype);
		record.setPROJECT_ID(project_id);
		commonDao.save(record);
		commonDao.flush();
		ResultMessage msg = new ResultMessage();
		msg.setSuccess("true");
		return msg;
	}

	/**
	 * 查询打印记录数（URL："/printrecordcount/{project_id}/{printtype}"，Method：GET）
	 * 
	 * @Title: getPrintRecord
	 * @author:liushufeng
	 * @date：2016年3月3日 下午4:22:42
	 * @param project_id
	 *            project_id
	 * @param printtype
	 *            打印类型（自定义）
	 * @param request
	 * @return Message对象，其中total属性即为打印记录总数
	 */
	@RequestMapping(value = "/printrecordcount/{project_id}/{printtype}", method = RequestMethod.GET)
	public @ResponseBody Message getPrintRecord(@PathVariable String project_id, @PathVariable String printtype,
			HttpServletRequest request) {
		Message msg = new Message();
		String fromSql = " FROM BDCK.BDCS_PRINTRECORD WHERE PRINTTYPE='" + printtype + "' AND PROJECT_ID='" + project_id
				+ "'";
		long count = commonDao.getCountByFullSql(fromSql);
		msg.setTotal(count);
		return msg;
	}
	
	/**
	 * 获取归档的ajjbxx
	 * @param project_id
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/{project_id}/ajjbxx/{BMType}", method = RequestMethod.GET)
	public  @ResponseBody Map GetAjjbxx(
			@PathVariable("project_id") String project_id,
			@PathVariable("BMType") String BMType,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		 Map map = new HashMap();
		String currentuser=Global.getCurrentUserName();
		String curuserid=Global.getCurrentUserInfo().getId();
		String fromSql = "SELECT * FROM BDC_DAK.DAS_AJJBXX WHERE project_id='"
				+ project_id + "' ";
		 List<Map> maps = commonDao.getDataListByFullSql(fromSql);
		 if(maps.size()>0){
			 map=maps.get(0);
			 map.put("currentuser", currentuser); 
			 map.put("curuserid", curuserid); 
		 }else{
			 //由于泸州现在是归档时的人只点击归档不负责编码。编码的人才是实际的档案室的人，所以在这里就先不获取AJH,DAGBH,DAHBH
			 /*
			 if(!StringHelper.isEmpty(BMType)){
				 if(BMType.equals("1")){
					 BDCS_XMXX xmxx = Global.getXMXX(project_id);
					 if(xmxx != null&&!StringHelper.isEmpty(xmxx.getId())){
						 String xmbh=xmxx.getId();
						 List<BDCS_DJDY_GZ> djdys=commonDao.getDataList(BDCS_DJDY_GZ.class, "select * from BDCK.BDCS_DJDY_GZ WHERE xmbh='"+xmbh+"'");
						 if(djdys.size()>0){
							 BDCS_DJDY_GZ djdy=djdys.get(0);
								if (!StringHelper.isEmpty(djdy.getBDCDYH())) { 
									String fromSqlbdc = "SELECT * FROM BDC_DAK.DAS_BDC WHERE bdcdyh='"
											+ djdy.getBDCDYH() + "' order by DJSJ asc ";
									List<Map> listbdcs = commonDao.getDataListByFullSql(fromSqlbdc);
									if(listbdcs.size()>0){
										Map firstAJ = new HashMap();
										
										for(int i=0;i<listbdcs.size();i++){
											Map bdci=listbdcs.get(i);
											if(!StringHelper.isEmpty(bdci.get("AJID").toString())){
												String bdciajid=bdci.get("AJID").toString();
												String fromSqlaj = "SELECT * FROM BDC_DAK.DAS_AJJBXX WHERE AJID='"
														+ bdciajid + "'";
												List<Map> listajs = commonDao.getDataListByFullSql(fromSqlaj);
												if(listajs.size()>0){
													Map aj=listajs.get(0);
													if(!StringHelper.isEmpty(aj.get("AJH").toString())){
														firstAJ=aj;
														break;
													}else{
														continue;
													}
												}
											}
										}
									 
										if(firstAJ != null){
											
											 map.put("AJH", firstAJ.get("AJH").toString());  
											 if(!StringHelper.isEmpty(firstAJ.get("DAGBH"))&&firstAJ.get("DAGBH")!=null){
												 map.put("DAGBH", firstAJ.get("DAGBH").toString());
											 }
											 if(!StringHelper.isEmpty(firstAJ.get("DAHBH"))&&firstAJ.get("DAHBH")!=null){
												 map.put("DAHBH", firstAJ.get("DAHBH").toString());
											 }
											 
										}	 
									 
									}else{
										
									}
								}else{
									
								}	 
							 
						 }
					 }
				 }else{
					 
				 }
			 }
			 map.put("currentuser", currentuser);
			 map.put("curuserid", curuserid); 
		 */
			 
			 map.put("currentuser", currentuser);
			 map.put("curuserid", curuserid); 
		 }
		return map;
	}
	/**
	 * 通过业务号获取项目编号
	 * @param ywh
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "Getxmbh/{ywh}", method = RequestMethod.GET)
	public @ResponseBody String Getxmxx(@PathVariable("ywh") String ywh, HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		 String  xmbh = projectService.GetXmbhData(ywh);
		 return xmbh;
	}
	
	
	/**
	 * 通过peoject_id获取ajid
	 * @param _project_id
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	
	@RequestMapping(value = "Getajid/{_project_id}", method = RequestMethod.GET)
	public @ResponseBody Map getAjid(@PathVariable("_project_id") String _project_id, HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		Map map=new HashMap();
		String fromSql = "SELECT * FROM BDC_DAK.DAS_BDC WHERE XMBH='"
				+ _project_id + "' ";
		 List<Map> maps = commonDao.getDataListByFullSql(fromSql);
		 if(maps.size()>0){
			 map=maps.get(0);
			 
		 }
		return map;
	}
	/**
	 * 读取出让合同信息
	 * @param projectId
	 * @return
	 */
	@RequestMapping(value="getCRHTInfo/{projectId}",method = RequestMethod.GET)
	public  @ResponseBody List<HashMap<String,String>> getCRHTInfo(@PathVariable String projectId){
		
		List<HashMap<String,String>> list = projectService.getCRHTInfo(projectId);
		if(list!=null&&list.size()>0){
			return list;
		}
		return null;
	}
	@RequestMapping(value="getCRHT/{projectId}",method = RequestMethod.GET)
	public @ResponseBody List<HashMap<String,String>> getCRHT(@PathVariable String projectId){
		List<HashMap<String,String>> lsit = projectService.getCRHT(projectId);
		HashMap<String,String> hashmap = new HashMap<String,String>();
		if(lsit==null||lsit.size()==0){
			List<HashMap<String,String>> list = getCRHTInfo(projectId);
			if(list!=null&&list.size()>0){
				HashMap<String,String> map =list.get(0);
				projectService.updateCRHT(map,projectId);
				return  list;
			}
		}
		return lsit;
	}
	@RequestMapping(value = "saveCRHT/{projectId}",method = RequestMethod.POST)
	public @ResponseBody BDCS_CRHT saveCRHT(Model model,@ModelAttribute(value="_crht") BDCS_CRHT _crht,@PathVariable String projectId){
		HashMap<String, String> m_info = new HashMap<String, String>();
		m_info.put("zdzl", _crht.getZdzl());
		m_info.put("qllx", _crht.getQllx());
		m_info.put("qlxz", _crht.getQlxz());
		m_info.put("zdszd", _crht.getZdszd());

		m_info.put("zdszx", _crht.getZdszx());
		m_info.put("zdszn", _crht.getZdszn());
		m_info.put("zdszb", _crht.getZdszb());
		m_info.put("zdmj", StringHelper.formatDouble(_crht.getZdmj()));
		m_info.put("djh", _crht.getDjh());
		m_info.put("zddm", _crht.getZddm());
		m_info.put("zdszb", _crht.getZdszb());
		m_info.put("dytdmj",
				StringHelper.formatDouble(_crht.getDytdmj()));

		m_info.put("gytdmj",
				StringHelper.formatDouble(_crht.getGytdmj()));

		m_info.put("fttdmj",
				StringHelper.formatDouble(_crht.getFttdmj()));
		m_info.put("jzmj", StringHelper.formatDouble(_crht.getJzmj()));
		m_info.put("zrzh", _crht.getZrzh());
		m_info.put("dyh", _crht.getDyh());
		m_info.put("fh", _crht.getFh());
		m_info.put("qzh", _crht.getQzh());
		m_info.put("crf", _crht.getCrf());
		m_info.put("crf", _crht.getCrf());
		m_info.put("crffrdb", _crht.getCrffrdb());
		m_info.put("srf", _crht.getSrf());
		m_info.put("srffrdb", _crht.getSrffrdb());
		
		m_info.put("crfdz", _crht.getCrfdz());
		m_info.put("srfdz", _crht.getSrfdz());
		m_info.put("th", _crht.getTh());
		m_info.put("yqzh", _crht.getYqzh());
		m_info.put("sfjzmj",
				StringHelper.formatDouble(_crht.getSfjzmj()));
		m_info.put("crmj",
				StringHelper.formatDouble( _crht.getCrmj()));
		m_info.put("crtddj", _crht.getCrtddj());
		m_info.put("crqx", _crht.getCrqx());
		m_info.put("crjbz", _crht.getCrjbz());
		m_info.put("tdyt", _crht.getTdyt());
		m_info.put("crjze", _crht.getCrjze());
		m_info.put("rmb", _crht.getRmb());
		m_info.put("bz", _crht.getBz());
		BDCS_CRHT result = projectService.updateCRHT(m_info,projectId);
		if(result!=null){
			return result;
		}else{
			return null;
		}
	}
	@RequestMapping(value = "delCRHT/{projectId}",method=RequestMethod.POST)
	@ResponseBody
	public Message delCRHT(@PathVariable String projectId){
		Message m= new Message();
		projectService.delCRHT(projectId);
		m.setSuccess("success");
		return m;
	}
 	/**
	 * 读取转让审批信息
	 * @param projectId
	 * @return
	 */
	@RequestMapping(value="getTDZRInfo/{projectId}",method = RequestMethod.GET)
	public  @ResponseBody List<HashMap<String,String>> getTDZRInfo(@PathVariable String projectId){
		
		List<HashMap<String,String>> list = projectService.getTDZRInfo(projectId);
		if(list!=null&&list.size()>0){
			return list;
		}
		return null;
	}
	@RequestMapping(value="getZRHT/{projectId}",method = RequestMethod.GET)
	public @ResponseBody List<HashMap<String,String>> getZRHT(@PathVariable String projectId){
		List<HashMap<String,String>> zrhtlist = projectService.getZRHT(projectId);
		if(zrhtlist==null||zrhtlist.size()==0){
			List<HashMap<String,String>> list = getTDZRInfo(projectId);
			if(list!=null&&list.size()>0){
				HashMap<String,String> map =list.get(0);
				map.put("zhdmj", "");
				projectService.updateZRHT(map,projectId);
				return  list;
			}
		}
		return zrhtlist;
	}
	@RequestMapping(value = "/delZRHT/{projectId}",method=RequestMethod.POST)
	public @ResponseBody Message delZRHT(@PathVariable String projectId){
		Message m= new Message();
		projectService.delZRHT(projectId);
		m.setSuccess("success");
		return m;
	}
	@RequestMapping(value = "saveZRHT/{projectId}",method = RequestMethod.POST)
	public @ResponseBody BDCS_ZRHT saveZRHT(Model model,@ModelAttribute(value="zrht") BDCS_ZRHT zrht,@PathVariable String projectId){
		HashMap<String, String> m_info = new HashMap<String, String>();
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");     
	    try {
			if(zrht.getCrqssj()!=null&&!zrht.getCrqssj().equals("")){
				m_info.put("crqssj", sdf.format(zrht.getCrqssj()));
			}
			if(zrht.getCrzzsj()!=null&&!zrht.getCrzzsj().equals("")){
				m_info.put("crzzsj", sdf.format(zrht.getCrzzsj()));
			}
			if(zrht.getJbsj()!=null&&!zrht.getJbsj().equals("")){
				m_info.put("jbsj",sdf.format(zrht.getJbsj()));
			}
			if(zrht.getShsj()!=null&&!zrht.getShsj().equals("")){
				m_info.put("shsj",sdf.format(zrht.getShsj()));
			}
			if(zrht.getSpsj()!=null&&!zrht.getSpsj().equals("")){
				m_info.put("spsj",sdf.format(zrht.getSpsj()));
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		m_info.put("fwsyqzh", zrht.getFwsyqzh());
		m_info.put("qsxz", zrht.getQsxz());
		m_info.put("qzh", zrht.getQzh());
		m_info.put("shyqlx", zrht.getShyqlx());
		m_info.put("srf", zrht.getSrf());
		m_info.put("srfdz", zrht.getSrfdz());
		m_info.put("srffrdb", zrht.getSrffrdb());
		m_info.put("tddj", zrht.getTddj());
		m_info.put("tdyt", zrht.getTdyt());
		m_info.put("tdzh", zrht.getTdzh());
		
		m_info.put("zdh", zrht.getZdh());
		m_info.put("fwsyqzh", zrht.getFwsyqzh());
		m_info.put("zrf", zrht.getZrf());
		m_info.put("zrfdz", zrht.getZrfdz());
		m_info.put("zrfdzzrfdz", zrht.getZrffrdb());
		m_info.put("th", zrht.getTh());

		m_info.put("zdszx", zrht.getZdszx());
		m_info.put("zdszn", zrht.getZdszn());
		m_info.put("zdszb", zrht.getZdszb());
		m_info.put("zdmj", StringHelper.formatDouble(zrht.getZdmj()));
		m_info.put("zdszb", zrht.getZdszb());
		m_info.put("dytdmj",
				StringHelper.formatDouble(zrht.getDytdmj()));
		m_info.put("gytdmj",
				StringHelper.formatDouble(zrht.getGytdmj()));
		m_info.put("fttdmj",
				StringHelper.formatDouble(zrht.getFttdmj()));
		m_info.put("jzmj", StringHelper.formatDouble(zrht.getJzmj()));
		m_info.put("zdmj",StringHelper.formatDouble(zrht.getZdmj()));
		
		m_info.put("zhdmj",StringHelper.formatDouble(zrht.getZhdmj()));
		m_info.put("ytdyt",zrht.getYtdyt());
		m_info.put("crjdj",zrht.getCrjdj());
		m_info.put("crjzj",zrht.getCrjzj());
		m_info.put("lglfbz",zrht.getLglfbz());
		m_info.put("yjlgf",zrht.getYjlgf());
		m_info.put("dahcqk",zrht.getDahcqk());
		m_info.put("jbr",zrht.getJbr());
		m_info.put("jbryj",zrht.getJbryj());
		m_info.put("shr",zrht.getShr());
		m_info.put("shyj",zrht.getShyj());
		m_info.put("bz",zrht.getBz());
		m_info.put("spyj",zrht.getSpyj());
		m_info.put("spr",zrht.getSpr());
		m_info.put("xmkfztz",zrht.getXmkfztz());
		m_info.put("gctz",zrht.getGctz());
		
		BDCS_ZRHT result = projectService.updateZRHT(m_info,projectId);
		if(result!=null){
			return result;
		}else{
			return null;
		}
	}
	/**
	 * 保存登簿记事
	 * */
	@RequestMapping(value = "/dbjs/{xmbh}/{actinst_id}", method = RequestMethod.GET)
	public @ResponseBody String GetDBJS(@PathVariable("xmbh") String xmbh,@PathVariable("actinst_id") String actinst_id) {
		return projectService.GetDBJS(xmbh,actinst_id);
	}
	
	/**
	 * 保存登簿记事
	 * */
	@RequestMapping(value = "/dbjs/{xmbh}/{actinst_id}", method = RequestMethod.POST)
	public @ResponseBody ResultMessage UpdateDBJS(@PathVariable("xmbh") String xmbh,@PathVariable("actinst_id") String actinst_id, HttpServletRequest request,
			HttpServletResponse response) {
		String dbjs="";
		try {
			dbjs = RequestHelper.getParam(request, "dbjs");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return projectService.UpdateDBJS(xmbh,actinst_id,dbjs);

	}
	
	/**
	 * 批量删除收费项
	 * */
	@RequestMapping(value = "/{xmbh}/pldelsflist", method = RequestMethod.POST)
	public @ResponseBody ResultMessage PlDelSflist(@PathVariable("xmbh") String xmbh, HttpServletRequest request,
			HttpServletResponse response) {
		return projectService.PlDelSflist(xmbh,request);

	}
	
	/**
	 * 批量删除收费项
	 * */
	@RequestMapping(value = "/sfjb/{djsfid}/{sfjb}", method = RequestMethod.GET)
	public @ResponseBody ResultMessage UpdateSfjbOnDJSF(@PathVariable("djsfid") String djsfid,@PathVariable("sfjb") String sfjb, HttpServletRequest request,
			HttpServletResponse response) {
		return projectService.UpdateSfjbOnDJSF(djsfid,sfjb);

	}
	
	
	@RequestMapping(value = "/{project_id}/zdbtn/{type}",method = RequestMethod.GET)
	public @ResponseBody void Updatazdbtn (@PathVariable("project_id") String project_id , @PathVariable("type") Boolean type ,HttpServletRequest request,HttpServletResponse response){
		projectService.Updatezdbtn(project_id,request,type);
	}
	
	@RequestMapping(value = "/updatexmxx/{lsh}/{ajh}",method = RequestMethod.GET)
	public @ResponseBody void Updatazdbtn (@PathVariable("lsh") String lsh , @PathVariable("ajh") String ajh ,HttpServletRequest request,HttpServletResponse response){
		projectService.UpdateXMXX(lsh,ajh);
	}

	@RequestMapping(value = "/{xmbh}/Exportfb", method = RequestMethod.POST)
	public @ResponseBody String Exportfb(@PathVariable("xmbh") String xmbh,
			HttpServletRequest request, HttpServletResponse response) throws IOException{
		String dataString = request.getParameter("info");
		String headString = request.getParameter("head");
		if(StringHelper.isEmpty(headString)||StringHelper.isEmpty(dataString)){
			return null;
		}
		String TempDirectory = request.getSession().getServletContext().getRealPath("/") + "\\resources\\PDF\\tmp\\approvalfb.xlsx";
		String url = request.getContextPath() + "\\resources\\PDF\\tmp\\approvalfb.xlsx";
		File excel = new File(TempDirectory);
		if(excel.exists()&&excel.isFile()){
			excel.delete();
		}
		if(excel.createNewFile()){
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet();
			workbook.setSheetName(0, "查询结果");
			XSSFCellStyle CellStyle = workbook.createCellStyle();
			CellStyle.setBorderBottom(XSSFCellStyle.BORDER_THIN);   
			CellStyle.setBorderTop(XSSFCellStyle.BORDER_THIN);   
			CellStyle.setBorderLeft(XSSFCellStyle.BORDER_THIN);   
			CellStyle.setBorderRight(XSSFCellStyle.BORDER_THIN); 
			CellStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
			CellStyle.setWrapText(true);
			XSSFRow row=sheet.createRow(0);
			JSONArray head = JSON.parseArray(headString);
			int ColumIndex = 0;
			for (ColumIndex = 0; ColumIndex < head.size(); ColumIndex++) {
				com.alibaba.fastjson.JSONObject array_element = (com.alibaba.fastjson.JSONObject) head.get(ColumIndex);
				XSSFCell cell=row.createCell(ColumIndex);
				cell.setCellStyle(CellStyle);
			    cell.setCellType(XSSFCell.CELL_TYPE_STRING);
			    cell.setCellValue(StringHelper.formatObject(array_element.get("value")));
			}
			LinkedHashMap<String, String> object = JSON.parseObject(dataString, new TypeReference<LinkedHashMap<String, String>>() {
	        });
			int RowIndex = 1;
			int Index = 0;
			XSSFRow InsertRow =null;
			for (Entry<String, String> entry : object.entrySet()) {
				Object value = entry.getValue();
				if(Index%ColumIndex==0){
					InsertRow=sheet.createRow(RowIndex++);
				}
				XSSFCell cell=InsertRow.createCell(Index%ColumIndex);
				sheet.autoSizeColumn(Index%ColumIndex);
				cell.setCellStyle(CellStyle);
			    cell.setCellType(XSSFCell.CELL_TYPE_STRING);
			    cell.setCellValue(StringHelper.formatObject(value));
			    Index++;
			}
			FileOutputStream out = new FileOutputStream(excel);
			workbook.write(out);
			out.close();
		}
		return url;
	}
	/**
	 * @Description: 首次灭迹打印明细说明
	 * @Title: SCMJXQ
	 * @Author: zhaomengfan
	 * @Date: 2017年5月4日下午2:40:51
	 * @param xmbh
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @return String
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/{xmbh}/SCMJXQ", method = RequestMethod.GET)
	public @ResponseBody String SCMJXQ(@PathVariable("xmbh") String xmbh,
			HttpServletRequest request, HttpServletResponse response) throws IOException{
		//模板位置
		String tplFullName = request.getSession().getServletContext().getRealPath("/WEB-INF/jsp/wjmb/SCMJXQ.xlsx");

		String url = "";
		
		String basePath = request.getSession().getServletContext().getRealPath("/") + "\\resources\\PDF";
		
		if(new File(tplFullName).exists()){
			String outpath = basePath + "\\tmp\\"+xmbh+".xlsx";
			url = request.getContextPath() + "\\resources\\PDF\\tmp\\"+xmbh+".xlsx";
			FileOutputStream outstream = new FileOutputStream(outpath);
			InputStream input = new FileInputStream(tplFullName);
			XSSFWorkbook wb = null;// 定义一个新的工作簿
			wb = new XSSFWorkbook(input);
			XSSFSheet sheet = wb.getSheetAt(0);
			//找出表头位置
			int RowIndex = 0;
			int MaxRow = sheet.getPhysicalNumberOfRows()+1;
			boolean flag = true;
			boolean zsdjflag = false;
			String bdcdylx = null;
			ProjectInfo Proj = ProjectHelper.GetPrjInfoByXMBH(xmbh);
			if(Proj==null){
				outstream.flush();
				outstream.close();
				outstream = null;
				return url;
			}
			for (RowIndex = 0; RowIndex < MaxRow; RowIndex++) {
				XSSFRow rooo = sheet.getRow(RowIndex);
				if(!StringHelper.isEmpty(rooo.getCell(0).getCellComment())&&flag){
					flag = false;
					bdcdylx = Proj.getBdcdylx();
					zsdjflag = DJLX.ZXDJ.Value.toString().equals(Proj.getDjlx());
					XSSFCell RepCell = rooo.getCell(0);
					String newValue = RepCell.getStringCellValue();
					List<Map> qlrmcs = commonDao.getDataList("SQRXM", "BDCK.BDCS_SQR", " XMBH='" + xmbh + "' AND SQRLB='1' ");
					boolean firstFlag = true;
					String qlrStr = "";
					for (Map qlrmc : qlrmcs) {
						if(firstFlag){
							firstFlag=false;
							qlrStr+=qlrmc.get("SQRXM");
						}else{
							qlrStr+="、"+qlrmc.get("SQRXM");
						}
					}
					newValue = MessageFormat.format(newValue, qlrStr,Proj.getDjlxmc(),StringHelper.FormatByDatetime(Proj.getDjsj()));
					RepCell.setCellValue(newValue);
				}
				if(!StringHelper.isEmpty(rooo.getCell(1).getCellComment())){
					break;
				}
			}
			if(RowIndex==0){
				outstream.flush();
				outstream.close();
				outstream = null;
				return url;
			} else {
				XSSFRow rooo = sheet.getRow(RowIndex);
				int MaxCell = rooo.getPhysicalNumberOfCells();
				if(!zsdjflag){
					List<ZS> ZsInfoList = ZSService.getZsInfoList(xmbh);
					for (ZS zs : ZsInfoList) {
						Map<String, Object> result = new CaseInsensitiveMap();
						result.putAll(StringHelper.beanToMap(zs));
						sheet.shiftRows(++RowIndex, MaxRow++, 1, true, false);
						XSSFRow InsertRow = sheet.createRow(RowIndex);
						for (int i = 0; i < MaxCell; i++) {
							XSSFCell BaseCell = rooo.getCell(i);
							XSSFComment CellComment = BaseCell.getCellComment();
							XSSFCell InsertCell = InsertRow.createCell(i);
							InsertCell.setCellStyle(BaseCell.getCellStyle());
							InsertCell.setCellType(XSSFCell.CELL_TYPE_STRING);
							if(CellComment==null){
								continue;
							}
							InsertCell.setCellValue(StringHelper.formatObject(result.get(CellComment.getString())));
						}
					}
				}else{
					String fulSql = "SELECT LYQLID FROM BDCK.BDCS_QL_GZ WHERE XMBH='"+xmbh+"'";
					List<Map> qlids = commonDao.getDataListByFullSql(fulSql);
					String lyqlids = "('";
					boolean firstflag = true;
					for (Map map : qlids) {
						if(firstflag){
							firstflag = false;
							lyqlids+=map.get("LYQLID");
						}else{
							lyqlids+="','"+map.get("LYQLID");
						}
					}
					lyqlids+="')";
					List<Rights> lsqls = RightsTools.loadRightsByCondition(DJDYLY.LS, " QLID IN "+lyqlids);
					for (Rights rights : lsqls) {
						Map<String, Object> result = new CaseInsensitiveMap();
						result.put("BDCQZH", rights.getBDCQZH());
						List<BDCS_DJDY_LS> dys = commonDao.findList(BDCS_DJDY_LS.class, " DJDYID='"+rights.getDJDYID()+"'");
						if(dys!=null&&dys.size()>0){
							if("031".equals(bdcdylx)||"032".equals(bdcdylx)){
								House house = (House)UnitTools.loadUnit(BDCDYLX.initFrom(bdcdylx), DJDYLY.LS, dys.get(0).getBDCDYID());
								if(house!=null){
									UseLand land = (UseLand) UnitTools.loadUnit(BDCDYLX.SHYQZD, DJDYLY.LS, house.getZDBDCDYID());
									CertInfoTools certinfo = new CertInfoTools();
									result.put("ZL", house.getZL());
									result.put("BDCDYH", house.getBDCDYH());
									result.put("QLXZ", certinfo.GetQLXZ(house, BDCDYLX.initFrom(bdcdylx), land, Proj, land.getTDYTS(), new WFD_MAPPING()));
									result.put("YT", certinfo.GetTDYT(house, BDCDYLX.initFrom(bdcdylx), land, land.getTDYTS(), Proj,null));
									result.put("MJ", "共有宗地面积："+land.getMJ()+"/房屋建筑面积："+house.getMJ());
								}
							}
						}
						sheet.shiftRows(++RowIndex, MaxRow++, 1, true, false);
						XSSFRow InsertRow = sheet.createRow(RowIndex);
						for (int i = 0; i < MaxCell; i++) {
							XSSFCell BaseCell = rooo.getCell(i);
							XSSFComment CellComment = BaseCell.getCellComment();
							XSSFCell InsertCell = InsertRow.createCell(i);
							InsertCell.setCellStyle(BaseCell.getCellStyle());
							InsertCell.setCellType(XSSFCell.CELL_TYPE_STRING);
							if(CellComment==null){
								continue;
							}
							InsertCell.setCellValue(StringHelper.formatObject(result.get(CellComment.getString())));
						}
					}
				}
			}
			wb.write(outstream);
			outstream.flush();
			outstream.close();
			outstream = null;
		}
		return url;
	}

	@RequestMapping(value = "/{xmbh}/HtmTpl", method = RequestMethod.POST)
	public @ResponseBody Map<String, String> SqspbHtmTpl(@PathVariable("xmbh") String xmbh, HttpServletRequest request,HttpServletResponse response){
		String Project_id = ProjectHelper.GetPrjInfoByXMBH(xmbh).getProject_id();
		String workflowcode = ProjectHelper.getWorkflowCodeByProjectID(Project_id);
		List<WFD_MAPPING> listCode = commonDao.findList(WFD_MAPPING.class, "WORKFLOWCODE='" + workflowcode + "'");
		Map<String,String> tpl = new HashMap<String, String>();
		if ((listCode != null) && (listCode.size() > 0)) {
			String bdcdjsqb = listCode.get(0).getUSESPBDHTM();
			tpl.put("bdcdjsqb", bdcdjsqb);
			String bdcdjspb = listCode.get(0).getUSEAPPROVALHTM();
			tpl.put("bdcdjspb", bdcdjspb);
			String bdcdjsqbfb = listCode.get(0).getUSEFJHTM();
			tpl.put("bdcdjsqbfb", bdcdjsqbfb);
			String bdcdjsqr = listCode.get(0).getUSESQRHTM();
			tpl.put("bdcdjsqr", bdcdjsqr);
		}
		return tpl;
	}
	

	@RequestMapping(value = "/constraintByProdefId/{prodef_id}", method = RequestMethod.GET)
	public @ResponseBody ResultMessage constraintByProdefId(@PathVariable() String prodef_id, HttpServletRequest request, HttpServletResponse response) {
		String bdcdyids = request.getParameter("bdcdyids");
		Wfd_Prodef wfd_prodef = commonDao.get(Wfd_Prodef.class, prodef_id);
		return constraintcheck.acceptCheckByBDCDYIDByProdefId(bdcdyids, wfd_prodef.getProdef_Code());
	}
	
	@RequestMapping(value = "/{xmbh}/sqrs/qlr", method = RequestMethod.GET)
	@AccessRequired
	public @ResponseBody Message GetSQRList_QLR(@PathVariable("xmbh") String xmbh) {
		List<BDCS_SQR> sqrList = projectService.getSQRList(xmbh);
		// 申请人Tree对象
		List<Tree> treeList = new ArrayList<Tree>();
		for (BDCS_SQR bdcs_sqr : sqrList) {
			if (SQRLB.JF.Value.equals(bdcs_sqr.getSQRLB())) {
				Tree tree = new Tree();
				tree.setId(bdcs_sqr.getId());
				tree.setText(bdcs_sqr.getSQRXM());
	
				tree.setTag1(ConstHelper.getNameByValue("SQRLB", bdcs_sqr.getSQRLB()));
	
				treeList.add(tree);
			}
		}
		Message message = new Message();
		message.setRows(treeList);
		message.setTotal(treeList.size());
		return message;
	}
	
	@RequestMapping(value = "/{xmbh}/sqrs/ywr", method = RequestMethod.GET)
	@AccessRequired
	public @ResponseBody Message GetSQRList_YWR(@PathVariable("xmbh") String xmbh) {
		List<BDCS_SQR> sqrList = projectService.getSQRList(xmbh);
		// 申请人Tree对象
		List<Tree> treeList = new ArrayList<Tree>();
		for (BDCS_SQR bdcs_sqr : sqrList) {
			if (!(SQRLB.JF.Value.equals(bdcs_sqr.getSQRLB()))) {
				Tree tree = new Tree();
				tree.setId(bdcs_sqr.getId());
				tree.setText(bdcs_sqr.getSQRXM());
	
				tree.setTag1(ConstHelper.getNameByValue("SQRLB", bdcs_sqr.getSQRLB()));
	
				treeList.add(tree);
			}
		}
		Message message = new Message();
		message.setRows(treeList);
		message.setTotal(treeList.size());
		return message;
	}
	
  @RequestMapping(value={"/{bdcdyid}/{ly}"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
  @ResponseBody
  public Map<String, String> Getbdcdyh(@PathVariable("bdcdyid") String bdcdyid,@PathVariable("ly") String ly, HttpServletRequest request, HttpServletResponse response)
    throws IOException
  {
	String fwbdcdyh = "";
	String zdbdcdyid = "";
	String zdbdcdyh = "";
	String tableh = "BDCK.BDCS_H_XZ";
	String tablezd = "BDCK.BDCS_SHYQZD_XZ";
	/*if(ly.equals("gz")){
		tableh = "BDCK.BDCS_H_GZ";
		tablezd = "BDCK.BDCS_SHYQZD_GZ";
	}else if(ly.equals("dc")){
		tableh = "BDCDCK.BDCS_H_GZY";
		tablezd = "BDCDCK.BDCS_SHYQZD_XZ";
	}else if(ly.equals("ls")){
		tableh = "BDCK.BDCS_H_LS";
		tablezd = "BDCK.BDCS_SHYQZD_LS";
	}*/
    List<Map> bdcdyh_ = commonDao.getDataListByFullSql("SELECT BDCDYH, ZDBDCDYID FROM "+tableh+" WHERE BDCDYID='" + bdcdyid + "'");
    if(bdcdyh_ != null && bdcdyh_.size()>0){
    	fwbdcdyh = StringHelper.formatObject(((Map)bdcdyh_.get(0)).get("BDCDYH"));
    	zdbdcdyid=StringHelper.formatObject(((Map)bdcdyh_.get(0)).get("ZDBDCDYID"));
    }
   
    List<Map> zdbdcdyhs = commonDao.getDataListByFullSql("SELECT BDCDYH FROM "+tablezd+" WHERE BDCDYID='" + zdbdcdyid + "'");
    if(zdbdcdyhs != null && zdbdcdyhs.size()>0){
    	zdbdcdyh = StringHelper.formatObject(((Map)zdbdcdyhs.get(0)).get("BDCDYH"));
    }
    Map<String,String> tpl = new HashMap<String, String>();
    tpl.put("fwbdcdyh",fwbdcdyh);
    tpl.put("zdbdcdyh",zdbdcdyh);
	return tpl;
  }
}
