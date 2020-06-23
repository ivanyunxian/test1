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
import com.supermap.realestate.registration.model.BDCS_CONST;
import com.supermap.realestate.registration.model.BDCS_CRHT;
import com.supermap.realestate.registration.model.BDCS_DJDY_LS;
import com.supermap.realestate.registration.model.BDCS_DJGD;
import com.supermap.realestate.registration.model.BDCS_DJGDFS;
import com.supermap.realestate.registration.model.BDCS_DJSF;
import com.supermap.realestate.registration.model.BDCS_IDCARD_PIC;
import com.supermap.realestate.registration.model.BDCS_PRINTRECORD;
import com.supermap.realestate.registration.model.BDCS_SQR;
import com.supermap.realestate.registration.model.BDCS_XMXX;
import com.supermap.realestate.registration.model.BDCS_ZRHT;
import com.supermap.realestate.registration.model.WFD_MAPPING;
import com.supermap.realestate.registration.model.interfaces.House;
import com.supermap.realestate.registration.model.interfaces.RealUnit;
import com.supermap.realestate.registration.model.interfaces.Rights;
import com.supermap.realestate.registration.model.interfaces.UseLand;
import com.supermap.realestate.registration.service.ChargeService;
import com.supermap.realestate.registration.service.ProjectService;
import com.supermap.realestate.registration.service.ProjectService_DA;
import com.supermap.realestate.registration.service.ZSService;
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
import com.supermap.wisdombusiness.workflow.util.FileUpload;

 /**
  * 把档案需要的方法单独抽取出来 
  * @author mass
  *
  */
@Controller
@RequestMapping("/project_da")
public class ProjectController_DA {

	@Autowired
	private ChargeService chargeService;

	@Autowired
	private CommonDao commonDao;
	/**
	 * ProjectService
	 */
	@Autowired
	private ProjectService_DA projectService_da;
	
	@Autowired
	private ZSService ZSService;

 
	/**
	 * mass
	 * @param project_id
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/{project_id}", method = RequestMethod.GET)
	public @ResponseBody ProjectInfo GetProjectInfo(@PathVariable("project_id") String project_id,
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		ProjectInfo info = projectService_da.getProjectInfo(project_id, request);
		return info;
	}

	 
	/**
	 * mass
	 * @param model
	 * @param xmbh
	 * @param projectid
	 * @return
	 */
	@RequestMapping(value = "/{xmbh}/{projectid}/getDjgdInfo", method = RequestMethod.GET)
	public @ResponseBody BDCS_DJGD getDjgdInfo(Model model, @PathVariable String xmbh, @PathVariable String projectid) {
		BDCS_DJGD djgd = projectService_da.getDjgdInfo(xmbh, projectid);
		model.addAttribute("djgdAttribute", djgd);
		return djgd;
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
		SHB shb = projectService_da.GetSHB(project_id);
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
		Map<String, Object> info = projectService_da.GetDBXX(project_id);
		return info;
	}

	/**
	 * mass
	 * @param project_id
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/{project_id}/v2/dbxx", method = RequestMethod.GET)
	public @ResponseBody Map<String, List<HashMap<String, Object>>> Get2(@PathVariable("project_id") String project_id,
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		@SuppressWarnings("unchecked")
		Map<String, List<HashMap<String, Object>>> info = projectService_da.GetDBXX2(project_id);
		return info;
	}
	
	/**
	 * yuxb
	 * @param ywlsh
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/{ywlsh}/v3/dbxx", method = RequestMethod.GET)
	public @ResponseBody Map<String, List<HashMap<String, Object>>> Get3(@PathVariable("ywlsh") String ywlsh,
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		Map<String, List<HashMap<String, Object>>> info = projectService_da.GetDBXX3(ywlsh);
		return info;
	}

	 

	/**
	 * mass
	 * @param file_numbers
	 * @param acinstid
	 * @param request
	 * @param response
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
			ProjectInfo info = projectService_da.getProjectInfo(file_numbers, request);
			String xmbh = info.getXmbh();
			SQSPB sqspb = projectService_da.GetSQSPBex(xmbh, acinstid, request).converToSQSPB();
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
	 * mass
	 * @param project_id
	 * @param BMType
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
		String SFQYXDA =ConfigHelper.getNameByValue("SFQYXDA");//新旧判断依据,本地化配置中设置
		 Map map = new HashMap();
		 String currentuser=Global.getCurrentUserName();
		 if(SFQYXDA!=null
				 &&SFQYXDA.equals("1")){
			 String fromSql = "SELECT * FROM SMWB_DAK.DAS_AJJBXX WHERE FILENUMBER='"
						+ project_id + "' ";
				 List<Map> maps = commonDao.getDataListByFullSql(fromSql);
				 if(maps!=null&&maps.size()>0) {
					 map=maps.get(0);
				 }
				 map.put("currentuser", currentuser); 
				 return map;
		 }
		
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
	 * mass
	 * @param lsh
	 * @param ajh
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/updatexmxx/{lsh}/{ajh}",method = RequestMethod.GET)
	public @ResponseBody void Updatazdbtn (@PathVariable("lsh") String lsh , @PathVariable("ajh") String ajh ,HttpServletRequest request,HttpServletResponse response){
		projectService_da.UpdateXMXX(lsh,ajh);
	}

	
	/**
	 * 根据PDF模板创建流
	   mass
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
							if(!"tm".equals(_key)){//不知道为什么设定了图像大小，为了使条码完整，排除条码id
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
	 * mass
	 * 表单1[0].#subform[0].djzqmc[0] 转成djzqmc
	 * mass
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
	 * mass
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
	 * mass
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
	 * mass
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
	 * 
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
}
