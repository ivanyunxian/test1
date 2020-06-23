package com.supermap.realestate.registration.web;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.supermap.realestate.registration.ViewClass.ZS;
import com.supermap.realestate.registration.model.BDCS_DJFZ;
import com.supermap.realestate.registration.model.BDCS_DJFZEX;
import com.supermap.realestate.registration.model.BDCS_QL_GZ;
import com.supermap.realestate.registration.model.BDCS_QZGLTJB;
import com.supermap.realestate.registration.model.BDCS_RKQZB;
import com.supermap.realestate.registration.model.BDCS_XMXX;
import com.supermap.realestate.registration.model.BDCS_ZS_GZ;
import com.supermap.realestate.registration.model.BDCS_ZS_LS;
import com.supermap.realestate.registration.model.BDCS_ZS_XZ;
import com.supermap.realestate.registration.service.ZSService;
import com.supermap.realestate.registration.tools.EncodeTools;
import com.supermap.realestate.registration.util.BarcodeUtil;
import com.supermap.realestate.registration.util.ConfigHelper;
import com.supermap.realestate.registration.util.ConstValue;
import com.supermap.realestate.registration.util.ConstValue.DJLX;
import com.supermap.realestate.registration.util.ConstValue.QLLX;
import com.supermap.realestate.registration.util.ConstValue.SF;
import com.supermap.realestate.registration.util.ConstValue.SFDB;
import com.supermap.realestate.registration.util.ObjectHelper;
import com.supermap.realestate.registration.util.PDFHelper.PDFDataContainer;
import com.supermap.realestate.registration.util.Global;
import com.supermap.realestate.registration.util.PDFHelper;
import com.supermap.realestate.registration.util.ProjectHelper;
import com.supermap.realestate.registration.util.QRCodeHelper;
import com.supermap.realestate.registration.util.RequestHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.realestate.registration.util.YwLogUtil;
import com.supermap.wisdombusiness.core.SuperHelper;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import com.supermap.wisdombusiness.web.Message;
import com.supermap.wisdombusiness.web.ResultMessage;
import com.supermap.wisdombusiness.web.ui.Page;
import com.supermap.wisdombusiness.web.ui.tree.Tree;
import com.supermap.wisdombusiness.workflow.service.wfm.SmProMaterService;

/**
 * 
 * @Description:证书信息控制器
 * @author 刘树峰
 * @date 2015年6月12日 上午11:52:18
 * @Copyright SuperMap
 */
@Controller
@RequestMapping("/zs")
public class ZSController {

	/** 证书service */
	@Autowired
	private ZSService zsService;
	@Autowired
	private CommonDao basecommondao;
	@Autowired
	private SmProMaterService smProMaterService;
	@SuppressWarnings("rawtypes")
	private Map<String, Map> listIdcard = new HashMap<String, Map>();

	/**
	 * 打印的时候保存证书信息（URL:"/{xmbh}/zss/{zsid}/savedata",Method：POST）
	 * @Title: saveZSDATA
	 * @author:liushufeng
	 * @date：2016年1月20日 下午10:41:39
	 * @param xmbh
	 * @param zsid
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "/{xmbh}/zss/{zsid}/savedata", method = RequestMethod.POST)
	public @ResponseBody Message saveZSDATA(@PathVariable("xmbh") String xmbh, @PathVariable("zsid") String zsid, HttpServletRequest request, HttpServletResponse response)
			throws UnsupportedEncodingException {
		String str = request.getParameter("zsdata");
		Message msg = new Message();
		msg.setMsg("");
		msg.setSuccess("true");
		try {
			if (!StringHelper.isEmpty(str)) {
				BDCS_ZS_GZ zs = basecommondao.get(BDCS_ZS_GZ.class, zsid);
				if (zs != null) {
					zs.setZSDATA(str);
					String bdcqzh = zs.getBDCQZH();
					if (!StringHelper.isEmpty(bdcqzh)) {
						String sql = " XMBH='" + xmbh + "' AND BDCQZH='" + bdcqzh + "'";
						List<BDCS_ZS_GZ> list = basecommondao.getDataList(BDCS_ZS_GZ.class, sql);
						if (list != null && list.size() > 0) {
							for (BDCS_ZS_GZ zsgz : list) {
								zsgz.setZSDATA(str);
								basecommondao.update(zsgz);
							}
						}
					}
					basecommondao.update(zs);
					basecommondao.flush();
				}

				BDCS_ZS_XZ zsxz = basecommondao.get(BDCS_ZS_XZ.class, zsid);
				if (zsxz != null) {
					zsxz.setZSDATA(str);
					String bdcqzh = zsxz.getBDCQZH();
					if (!StringHelper.isEmpty(bdcqzh)) {
						String sql = " XMBH='" + xmbh + "' AND BDCQZH='" + bdcqzh + "'";
						List<BDCS_ZS_XZ> list = basecommondao.getDataList(BDCS_ZS_XZ.class, sql);
						if (list != null && list.size() > 0) {
							for (BDCS_ZS_XZ zsgz : list) {
								zsgz.setZSDATA(str);
								basecommondao.update(zsgz);
							}
						}
					}
					basecommondao.update(zsxz);
					basecommondao.flush();
				}

				BDCS_ZS_LS zsls = basecommondao.get(BDCS_ZS_LS.class, zsid);
				if (zsls != null) {
					zsls.setZSDATA(str);
					String bdcqzh = zsls.getBDCQZH();
					if (!StringHelper.isEmpty(bdcqzh)) {
						String sql = " XMBH='" + xmbh + "' AND BDCQZH='" + bdcqzh + "'";
						List<BDCS_ZS_LS> list = basecommondao.getDataList(BDCS_ZS_LS.class, sql);
						if (list != null && list.size() > 0) {
							for (BDCS_ZS_LS zsgz : list) {
								zsgz.setZSDATA(str);
								basecommondao.update(zsgz);
							}
						}
					}
					basecommondao.update(zsls);
					basecommondao.flush();
				}
			}
		} catch (Exception ee) {
			msg.setSuccess("false");
			msg.setMsg(ee.getMessage());
		}
		return msg;
	}

	/**
	 * 获取证书列表（URL:"/{xmbh}/zss",Method：GET）
	 * 
	 * @param xmbh
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/{xmbh}/zss", method = RequestMethod.GET)
	public @ResponseBody Message GetZSList(@PathVariable("xmbh") String xmbh, HttpServletRequest request, HttpServletResponse response) {
		List<Tree> list = zsService.getZsTreeEx(xmbh);
		int textMaxLength = 0;
		for (Tree tree : list) {
			String text = tree.getText();
			if (textMaxLength < text.length()) {
				textMaxLength = text.length();
			}			
		}
		Message msg = new Message();
		msg.setTotal(list.size());
		msg.setRows(list);
		msg.setMsg(Integer.toString(textMaxLength));
		return msg;
	}
	
	@RequestMapping(value = "/{xmbh}/zsscombo/{qllx}", method = RequestMethod.GET)
	public @ResponseBody Message GetZSListCombo(@PathVariable("xmbh") String xmbh, @PathVariable("qllx") String qllx,HttpServletRequest request, HttpServletResponse response) {
		List<Tree> list = zsService.getZsTreeCombo(xmbh, qllx);
		int textMaxLength = 0;
		for (Tree tree : list) {
			String text = tree.getText();
			if (textMaxLength < text.length()) {
				textMaxLength = text.length();
			}			
		}
		Message msg = new Message();
		msg.setTotal(list.size());
		msg.setRows(list);
		msg.setMsg(Integer.toString(textMaxLength));
		return msg;
	}
	
	/**
	 * @Title: GetZSInfoList
	 * @Description: 获取证书详情列表
	 * @Author：赵梦帆
	 * @Data：2016年11月16日 上午11:51:56
	 * @param xmbh
	 * @param request
	 * @param response
	 * @return
	 * @return Message
	 */
	@RequestMapping(value = "/{xmbh}/zssInfo", method = RequestMethod.GET)
	public @ResponseBody Message GetZSInfoList(@PathVariable("xmbh") String xmbh, HttpServletRequest request, HttpServletResponse response) {
		List<ZS> list = zsService.getZsInfoList(xmbh);
		Message msg = new Message();
		msg.setTotal(list.size());
		msg.setRows(list);
		return msg;
	}
	
	/**
	 * @Title: ZsInfoExportExcel
	 * @Description: 证书信息导出
	 * @Author：赵梦帆
	 * @Data：2016年11月16日 下午12:02:54
	 * @param xmbh
	 * @param request
	 * @param response
	 * @return
	 * @return Message
	 * @throws Exception 
	 */
	@RequestMapping(value = "/{xmbh}/zssInfo/ExportExcel", method = RequestMethod.POST)
	public @ResponseBody String ZsInfoExportExcel(@PathVariable("xmbh") String xmbh, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String CellIndex = request.getParameter("CellIndex");
		String CellName = request.getParameter("CellName");
		String rows = request.getParameter("rows");
		JSONObject Column = JSONObject.parseObject(CellName);
		JSONObject ColIndex = JSONObject.parseObject(CellIndex);
		JSONArray Rows = JSONArray.fromObject(rows);
		return zsService.ZsInfoExportExcel(ColIndex,Column,Rows,request);
	}

	/**
	 * 商品房首次登记，并且把证书信息加载到树里，以为批量打印使用
	 * @作者 海豹
	 * @创建时间 2015年12月13日下午8:37:19
	 * @param xmbh
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/{xmbh}/buildingzss", method = RequestMethod.POST)
	public @ResponseBody Message GetBuildingZSList(@PathVariable("xmbh") String xmbh, HttpServletRequest request, HttpServletResponse response) {
		List<Tree> list = new ArrayList<Tree>();
		String zsids = request.getParameter("zsids");
		if (!StringHelper.isEmpty(zsids)) {
			String[] ids = zsids.split(",");
			for (String id : ids) {
				Tree tree = new Tree();
				tree.setId(id);
				ZS buildingzsinfo = zsService.getBuildingZSForm(xmbh, id);
				tree.setZsform(buildingzsinfo);
				list.add(tree);
			}
		}
		/***排序啦 by 赵梦帆 2017-05-18 08:40:24***/
		list = ObjectHelper.SortList(list);
		Message msg = new Message();
		msg.setTotal(list.size());
		msg.setRows(list);
		return msg;
	}

	/**
	 * 商品房首次登记中证书树需要添加不动产权证号，因为不保存证书编号
	 * @作者 海豹
	 * @创建时间 2016年1月10日下午5:29:27
	 * @param xmbh
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/{xmbh}/buildingzsstree", method = RequestMethod.GET)
	public @ResponseBody Message GetBuildingZSListtree(@PathVariable("xmbh") String xmbh, HttpServletRequest request, HttpServletResponse response) {
		List<Tree> list = zsService.getZsTreeEx(xmbh);
		
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				if (!StringHelper.isEmpty(list.get(i).getBdcqzh())) {
					List<String> lst = StringHelper.MatchBDCQZH(list.get(i).getBdcqzh());
					if (lst.size() == 4)// 受理页面想查看证书信息时，出错
					{
						list.get(i).setText(list.get(i).getText() + "(产权证号:" + lst.get(3) + ")");
					}
				}
			}
		}
		Message msg = new Message();
		msg.setTotal(list.size());
		msg.setRows(list);
		return msg;
	}

	/**
	 * 获取已发证的记录信息（URL:"/{xmbh}/zss/{zsid}",Method：GET）
	 * 
	 * @作者 李桐
	 * @创建时间 2015年6月7日下午8:12:16
	 * @param xmbh
	 * @param zsid
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/{xmbh}/zss/{zsid}", method = RequestMethod.GET)
	public @ResponseBody BDCS_DJFZ GetZSInfo(@PathVariable("xmbh") String xmbh, @PathVariable("zsid") String zsid, HttpServletRequest request, HttpServletResponse response) {
		BDCS_DJFZ djfzObj = zsService.GetFZList(zsid);
		return djfzObj;
	}

	/**
	 * 读取身份证
	 * 
	 * @param
	 * @return
	 */
	@RequestMapping(value = "/{paras}/readCardPage", method = RequestMethod.GET)
	public String readIdCard(@PathVariable("paras") String paras, Model model, HttpServletRequest request) {
		try {
			paras = new String(paras.getBytes("iso8859-1"), "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		model.addAttribute("paras", paras);
		YwLogUtil.addYwLog("证书读取身份证-成功", ConstValue.SF.YES.Value, ConstValue.LOG.ADD);
		if("lz".equals(paras)){
			return "/realestate/registration/modules/common/reciveZS_LZ";
		}
		return "/realestate/registration/modules/common/reciveZS";
	}

	/**
	 * 获取已发证的记录信息（URL:"/{xmbh}/zss/{zsid}",Method：GET）
	 * 
	 * @作者 hanxu
	 * @创建时间 2015年6月7日下午8:12:16
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/addSqrCard", method = RequestMethod.POST)
	public @ResponseBody ResultMessage AddsqrByCard(HttpServletRequest request, HttpServletResponse response) {
		ResultMessage msg = new ResultMessage();
		String paras = request.getParameter("paras");
		Map map = request.getParameterMap();
		Map mapnew = new HashMap<String, Object>();
		for (Object entry : map.keySet()) {
			mapnew.put(entry.toString().toLowerCase(), map.get(entry));
		}
		if (!StringHelper.isEmpty(paras)) {
			synchronized (listIdcard) {
				if (listIdcard.containsKey(paras)) {
					listIdcard.remove(paras);
				}
				listIdcard.put(paras, mapnew);
			}
		}
		return msg;
	}

	/**
	 * 加载发证数据（读取身份证）
	 * @param paras
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/{paras}/readFZData", method = RequestMethod.GET)
	public @ResponseBody Map readFZData(@PathVariable("paras") String paras, HttpServletRequest request, HttpServletResponse response) {
		Map map = null;
		if (!StringHelper.isEmpty(paras)) {
			synchronized (listIdcard) {
				if (listIdcard.containsKey(paras)) {
					map = listIdcard.get(paras);
					listIdcard.remove(paras);
				}
			}
		}
		return map;
	}

	/**
	 * 向非登记系统提供获取证书证明信息的对外接口,给不动产档案系统调用
	 * @作者 海豹
	 * @创建时间 2016年1月11日下午9:02:43
	 * @param file_numbers
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/getzszmstream/{file_numbers}", method = RequestMethod.GET)
	public @ResponseBody void GetZSZMPDF(@PathVariable("file_numbers") String file_numbers, HttpServletRequest request, HttpServletResponse response) {
		ResultMessage msg = new ResultMessage();
		String sfgzzszm = ConfigHelper.getNameByValue("SFGDZSZM");
		String basePath = request.getRealPath("/") + "resources\\pdf";// 存放PDF文件模板或临时文件夹路径
		String outpath = basePath + "\\tmp\\" + file_numbers + ".pdf";// 导出路径
		String tplzm = basePath + "\\tpl\\zszm.pdf";// 证明模板
		String tplzs = basePath + "\\tpl\\zsxx_nr.pdf";// 证书模板

		ByteArrayOutputStream outstream = new ByteArrayOutputStream();// 用来保存生成最终PDF的流
		String workflowcode = ProjectHelper.getWorkflowCodeByProjectID(file_numbers);// 获取流程编码
		if (SFDB.YES.Value.equals(sfgzzszm))// 是否归档证书证名信息，若为1，归档，若为0，不归档
		{
			String sfgdcodes = ConfigHelper.getNameByValue("SFGDCODE");// 获取不需要归档的流程，这些流程不需要归档证书证名信息
			boolean flag = true;//
			if (!StringHelper.isEmpty(sfgdcodes) && !StringHelper.isEmpty(workflowcode)) {

				if (sfgdcodes.contains(workflowcode)) {
					flag = false;
				}
			}
			if (flag) {
				try {
					BDCS_XMXX bdcs_xmxx = Global.getXMXX(file_numbers);
					if (!StringHelper.isEmpty(bdcs_xmxx)) {
						List<Tree> trees = zsService.getZsTreeEx(bdcs_xmxx.getId());
						ArrayList<ByteArrayOutputStream> baos = new ArrayList<ByteArrayOutputStream>();// PDF流集合
						for (Tree tree : trees) {
							String djlx = tree.getTag1();
							String qllx = tree.getTag2();
							Map zsOrzmXX = new HashMap<String, String>();
							String tplname = "";
							if (DJLX.YGDJ.Value.contains(djlx) || DJLX.YYDJ.Value.contains(djlx) || QLLX.DIYQ.Value.contains(qllx) || QLLX.DYQ.Value.contains(qllx)) {
								tplname = tplzm;
								Map zm = zsService.getBDCDJZM(bdcs_xmxx.getId(), tree.getId());// 证明信息
								zsOrzmXX.put("fm_year", zm.get("year"));
								zsOrzmXX.put("fm_month", zm.get("month"));
								zsOrzmXX.put("fm_day", zm.get("day"));
								zsOrzmXX.put("id_zsbh", zm.get("zsbh"));
								zsOrzmXX.put("qhjc", zm.get("sjc"));
								zsOrzmXX.put("nd", zm.get("nd"));
								zsOrzmXX.put("qhmc", zm.get("qhmc"));
								zsOrzmXX.put("cqzh", zm.get("sxh"));
								zsOrzmXX.put("zmqlhsx", zm.get("zmqlhsx"));
								zsOrzmXX.put("qlr", zm.get("qlr"));
								zsOrzmXX.put("ywr", zm.get("ywr"));
								zsOrzmXX.put("zl", zm.get("zl"));
								zsOrzmXX.put("bdcdyh", zm.get("bdcdyh"));
								zsOrzmXX.put("qt", zm.get("qt"));
								zsOrzmXX.put("fj", zm.get("fj"));

							} else {
								tplname = tplzs;
								ZS zs = zsService.getZSForm(bdcs_xmxx.getId(), tree.getId());// 证书信息
								zsOrzmXX.put("qhjc", zs.getQhjc());
								zsOrzmXX.put("nd", zs.getNd());
								zsOrzmXX.put("qhmc", zs.getQhmc());
								zsOrzmXX.put("cqzh", zs.getCqzh());
								zsOrzmXX.put("qlr", zs.getQlr());
								zsOrzmXX.put("gyqk", zs.getGyqk());
								zsOrzmXX.put("zl", zs.getZl());
								zsOrzmXX.put("bdcdyh", zs.getBdcdyh());
								zsOrzmXX.put("qllx", zs.getQllx());
								zsOrzmXX.put("qlxz", zs.getQlxz());
								zsOrzmXX.put("yt", zs.getYt());
								zsOrzmXX.put("mj", zs.getMj());
								zsOrzmXX.put("syqx", zs.getSyqx());
								zsOrzmXX.put("qlqtzk", zs.getQlqtzk());
								zsOrzmXX.put("fj", zs.getFj());
							}
							ByteArrayOutputStream bao = CreatePdfStream(tplname, zsOrzmXX, request);// 申请表PDF模板和数据形成流
							baos.add(bao);
						}
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
					}
				} catch (Exception e) {
					System.out.print("将该项目的证书或证明信息转成流出异常：" + e.getMessage());
				}
			} else {
				System.out.print("该项目无证书或证明信息需要归档");

			}
		}
	}
	/**
	 * 创建证书信息PDF WUZHU
	 * 
	 * @param xmbh
	 * @param request
	 * @param response
	 * @return
	 * @throws DocumentException
	 * @throws IOException
	 */
	@RequestMapping(value = "/{xmbh}/{tpl}/zsxxCreatePdfAll", method = RequestMethod.POST)
	public @ResponseBody String ZSXXCreatePDFALL(
			@PathVariable("xmbh") String xmbh, @PathVariable("tpl") String tpl,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, DocumentException {
		if (StringUtils.isEmpty(tpl)) {
			return "";
		}
		@SuppressWarnings("deprecation")
		String basePath = request.getRealPath("/") + "\\resources\\PDF";
		try {
			// sunhb-2015-09-08 套打或打印时，添加登记缮证信息，添加try可以保证永远不影响打印的信息
			zsService.updateDJSZ(xmbh);
		} catch (Exception e) {
		}

		String createdPDFFileName=xmbh + "-" + tpl + ".pdf";
	
		String zsids = request.getParameter("zsids");
		String ywlsh=request.getParameter("ywlsh");
		String[] zsidsArray=null;
		List<PDFDataContainer> zss=new ArrayList<PDFDataContainer>();
		 if(!StringUtils.isEmpty(zsids))
			 zsidsArray=zsids.split("\\,");
		 if(zsidsArray==null)
			 return "";
		 for(String zsid:zsidsArray)
		 {
				ZS zsInfo = zsService.getZSForm(xmbh, zsid);
			Map<String,Object> zsmap=ObjectHelper.transBean2Map(zsInfo);
			Map zsmapLowerCase=new HashMap();
			//因为Pdf模板上的控件Id是小写的，转换成小写主键
			for(Map.Entry<String, Object> entry : zsmap.entrySet())   
			{   
				zsmapLowerCase.put(entry.getKey().toLowerCase(), convertClearValue(entry.getValue()));
			}
			zsmapLowerCase.put("djsj", zsmapLowerCase.get("fm_year")+"-"+zsmapLowerCase.get("fm_month")+"-"+zsmapLowerCase.get("fm_day"));
			zsmapLowerCase.put("ywlsh", ywlsh);
			 String xzqhmc = ConfigHelper.getNameByValue("XZQHMC");
			 zsmapLowerCase.put("xzqmc",xzqhmc);
			 if(!StringUtils.isEmpty(zsmapLowerCase.get("fj")))
				zsmapLowerCase.put("fj", "附记：\n"+zsmapLowerCase.get("fj"));
			PDFDataContainer datacontainer=new PDFHelper().new PDFDataContainer();
			datacontainer.setPdfTemplateName(tpl);
			datacontainer.setData(zsmapLowerCase);
			zss.add(datacontainer);
		 }
		 return PDFHelper.CreatePdfs(zss, createdPDFFileName, request);
		
	}
	private String convertClearValue(Object inputvalue)
	{	
		String s = "----起----止";
		if(StringUtils.isEmpty(inputvalue))
			return "";
		String result=inputvalue.toString();
		result=result.replaceAll("&nbsp;", "\t");
		result=result.replaceAll("<br/>", "\n");
		result=result.replaceAll("<br>", "\n");
		if(result.indexOf("----")!= -1 &&result.lastIndexOf("止")!= -1){
			result=result.replaceAll("起"," ");
			result=result.replaceAll("止"," ");
		}
		if((result.indexOf("房屋结构：") != -1 &&result.charAt(result.length()-1) == '：')||(
			result.indexOf("房屋结构：") != -1 &&result.indexOf("----")!= -1)){
			result=result.replaceAll("房屋结构："," ");
			
		}
		result=result.replaceAll("----/", "\n");
		result=result.replaceAll("/----", "\n");
		result=result.replaceAll("----", "\n");
		if(result.charAt(result.length()-1)== '/'||result.indexOf("/----") != -1 ){
			result=result.replaceAll("/"," ");
		}
		if(result.charAt(0)== '/'||result.indexOf("----/") != -1 ){
			result=result.replaceAll("/"," ");
		}
		
		return result;
	}
	/**
	 * 创建证书信息PDF WUZHU
	 * 
	 * @param xmbh
	 * @param request
	 * @param response
	 * @return
	 * @throws DocumentException
	 * @throws IOException
	 */
	@RequestMapping(value = "/{xmbh}/{tpl}/zsxxCreatePDF", method = RequestMethod.POST)
	public @ResponseBody String ZSXXCreatePDF(@PathVariable("xmbh") String xmbh, @PathVariable("tpl") String tpl, HttpServletRequest request, HttpServletResponse response)
			throws IOException, DocumentException {
		if (StringUtils.isEmpty(tpl)) {
			return "";
		}
		@SuppressWarnings("deprecation")
		String basePath = request.getRealPath("/") + "\\resources\\PDF";
		try {
			// sunhb-2015-09-08 套打或打印时，添加登记缮证信息，添加try可以保证永远不影响打印的信息
			zsService.updateDJSZ(xmbh);
		} catch (Exception e) {
		}

		String url = request.getContextPath() + "\\resources\\PDF\\tmp\\" + xmbh + "-" + tpl + ".pdf";
		String outpath = basePath + "\\tmp\\" + xmbh + "-" + tpl + ".pdf";
		String TemplatePDF = basePath + "\\tpl\\" + tpl + ".pdf";
		FileOutputStream outstream = new FileOutputStream(outpath);
		ByteArrayOutputStream fos = new ByteArrayOutputStream();
		String dataString = request.getParameter("info");
		JSONObject object = JSON.parseObject(dataString);

		Map<String, Object> _data = new HashMap<String, Object>();
		for (Entry<String, Object> entry : object.entrySet()) {
			if (!StringHelper.isEmpty(entry.getKey()) && entry.getKey().toUpperCase().equals("FJ")) {
				String fj = entry.getValue().toString();
				_data.put(entry.getKey(), fj.replaceAll("\t", " "));
			} else {
				_data.put(entry.getKey(), entry.getValue());
			}
		}
		fos = CreatePdfStream(TemplatePDF, _data, request);
		if (fos.size() > 0) {
			Document doc = new Document();
			PdfCopy pdfCopy = new PdfCopy(doc, outstream);
			doc.open();
			PdfImportedPage impPage = null;
			PdfReader reader = new PdfReader(fos.toByteArray());
			impPage = pdfCopy.getImportedPage(reader, 1);
			pdfCopy.addPage(impPage);
			doc.close();
		}
		return url;
	}

	// 根据PDF模板创建流WUZHU
	private ByteArrayOutputStream CreatePdfStream(String tplName, Map<String, Object> data, HttpServletRequest request) throws IOException, DocumentException {
		ByteArrayOutputStream bao = new ByteArrayOutputStream();
		PdfReader reader = new PdfReader(tplName);
		PdfStamper stamp = new PdfStamper(reader, bao);
		AcroFields form = stamp.getAcroFields();
		Set<String> fields = form.getFields().keySet();
		if(tplName.contains("zsxx_a4")) {
			//a4打印，加入行政区名称
			data.put("xzqmc", ConfigHelper.getNameByValue("XZQHMC"));
		}

		for (String key : fields) {
			String _key = ConvertToPdfName(key);
			if (data.containsKey(_key)) {
				if (!StringUtils.isEmpty(data.get(_key))) {
					String _value = String.valueOf(data.get(_key));
					if (_value.contains("<br/>")) {
						_value = _value.replace("<br/>", "\n");
					}
					form.setField(_key, _value);
				}
			}
			if (String.valueOf(_key.charAt(0)).equals("_")) { // 有下划线的是CHECK类型
				String _checkKey = ConvertCheckName(_key);
				if (data.containsKey(_checkKey)) {
					if (!StringUtils.isEmpty(data.get(_checkKey))) {
						String _checkValueKey = "_" + _checkKey + "_" + String.valueOf(data.get(_checkKey));
						if (_checkValueKey.equals(_key))
							form.setField(_checkValueKey, "√");// 设置CHECKBOX
					}
				}
			}
			if (form.getFieldType(_key) == AcroFields.DA_SIZE) {// 图片区域
				String resultimgfullpath = "";
				try {
					String imgfullpath = String.valueOf(data.get(_key));
					int start_param = imgfullpath.lastIndexOf("?");
					if(start_param>-1){
						imgfullpath=imgfullpath.substring(0, start_param);
					}
					int start = imgfullpath.lastIndexOf("/");
					String filename = imgfullpath.substring(start + 1, imgfullpath.length());
					// 为了解决服务器IP地址存在多个 gs
					// 案一
					// String servername=request.getServerName();
					// if(Global.ISUsedLocalIP){
					// servername=StringHelper.formatObject(InetAddress.getLocalHost().getHostAddress());
					// }
					// String basePath = request.getScheme() + "://" +
					// servername + ":" + request.getServerPort() +
					// request.getContextPath();
					// resultimgfullpath=String.format("%s/resources/qrcode/%s",
					// basePath,filename);
					// 方案二
					resultimgfullpath = String.format("%s\\resources\\qrcode\\%s", request.getRealPath("/"), filename);// 为了解决服务器IP地址存在多个
					Image image1 = Image.getInstance(resultimgfullpath);
					PdfContentByte overContent = stamp.getOverContent(1);
					List<AcroFields.FieldPosition> imgPosition = form.getFieldPositions(_key);
					float x = imgPosition.get(0).position.getLeft();
					float y = imgPosition.get(0).position.getBottom();
					image1.setAbsolutePosition(x, y);
					// image1.scaleAbsolute(150f, 150f);
					overContent.addImage(image1);
				} catch (Exception e) {
					System.out.println("图片连接为空： " + resultimgfullpath);
				}
			}
		}
		stamp.setFormFlattening(true);
		stamp.close();
		reader.close();
		return bao;
	}

	// 表单1[0].#subform[0].djzqmc[0] 转成djzqmc
	private String ConvertToPdfName(String key) {
		String result = "";
		if (!StringUtils.isEmpty(key)) {
			int start = key.lastIndexOf(".") + 1;
			int end = key.length() - 3;
			result = key.substring(start, end);
		}
		return result;
	}

	// _qllx_9 转成qllx
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
	 * 保存证书编号
	 * 
	 * @param xmbh
	 * @param zsid
	 * @param request
	 * @param response
	 * @return
	 */
	/*
	 * 2016-10-25 11:25:27
	 * 检查证书编号是否已存在
	 */
	@RequestMapping(value = "/{xmbh}/zss/{zsid}", method = RequestMethod.POST)
	public @ResponseBody ResultMessage UpdateZSInfo(@PathVariable("xmbh") String xmbh, @PathVariable("zsid") String zsid, HttpServletRequest request, HttpServletResponse response) {
		String zsbh = request.getParameter("zsbh");
		BDCS_ZS_GZ zs = zsService.getZS(zsid);
		ResultMessage msg = new ResultMessage();
		String bdcqzh1 = null;
		StringBuilder str2 = new StringBuilder();
		str2.append("select bdcqzh from bdck.bdcs_zs_gz where zsid='").append(zsid).append("'");
		List<Map> ma = basecommondao.getDataListByFullSql(str2.toString());
		if(ma!=null&&ma.size()>0){
			bdcqzh1 = ma.get(0).get("BDCQZH")==null?"":ma.get(0).get("BDCQZH").toString();
			//
			bdcqzh1 = bdcqzh1.substring(10, 15);
		}
		StringBuilder str3 = new StringBuilder();
		//根据xmxx表中sfhezs的条件，来判断是否合并证书
		String sfhbzs="select sfhbzs from bdck.bdcs_xmxx a  where  a.xmbh='"+xmbh+"'";
		List<Map> m = basecommondao.getDataListByFullSql(sfhbzs.toString());
		sfhbzs=m.get(0).get("SFHBZS")==null?"":m.get(0).get("SFHBZS").toString();
		str3.append("from (select * from bdck.bdcs_zs_gz where zsid<>'").append(zsid)
		.append("' and zsbh='").append(zsbh).append("' and substr(bdcqzh,11,5)='").append(bdcqzh1).append("'");
		if(sfhbzs.equals("1")){//0：不合并  1：合并
			str3.append(" and xmbh <>'").append(xmbh).append("'");
			
		}
		long total = basecommondao.getCountByFullSql(str3+")".toString());
		if(total>0){
			msg.setMsg("编号已存在，请重新选择");
			msg.setSuccess("false");
			YwLogUtil.addYwLog("保存证书编号，失败，证书编号：" + zsbh, ConstValue.SF.NO.Value, ConstValue.LOG.ADD);
			return msg;
		}
		
		String sql=" FROM (SELECT 	QZBH,QZLX,SFSZ,SFZF  FROM bdck.bdcs_rkqzb )"
				+ " WHERE QZBH='"+ zsbh +"' AND QZLX  IN ('"+(bdcqzh1.contains("权第")?"0":"1")+"') ";
		long countrkzm = basecommondao.getCountByFullSql(sql);
		if(countrkzm==0){
			msg.setMsg("编号请入库，不能保存成功");
			msg.setSuccess("false");
			return msg;
		}
		long countrk = basecommondao.getCountByFullSql(sql+"  AND (SFSZ='1' OR  SFZF='1') ");
		if(countrk>0){
			msg.setMsg("编号已作废已缮证，不能保存成功");
			msg.setSuccess("false");
			return msg;
		}
		
		Boolean flag = zsService.updateZs(zsid, zsbh);
		try {
			// sunhb-2015-09-08 套打或打印时，添加登记缮证信息，添加try可以保证永远不影响打印的信息
			zsService.updateDJSZ(xmbh);
		} catch (Exception e) {
		}
//		if (zs != null) {
//			zs.setZSBH(zsbh);
//			updateZs(String zsid, String zsbh)
//			zsService.updateZS(zs);
//		}
		
		
			if (flag) {
				 zsService.updateRKQZB(bdcqzh1.contains("权第")?"0":"1", zsbh);
				msg.setMsg("保存成功");
				msg.setSuccess("true");
				YwLogUtil.addYwLog("保存证书编号，保存成功，证书编号：" + zsbh, ConstValue.SF.YES.Value, ConstValue.LOG.ADD);
				return msg;
			} else {
					msg.setMsg("保存失败");
					msg.setSuccess("false");
					YwLogUtil.addYwLog("权证管理-更新权证信息", ConstValue.SF.YES.Value,ConstValue.LOG.UPDATE);
					YwLogUtil.addYwLog("权证管理-查询权证列表", ConstValue.SF.YES.Value,ConstValue.LOG.SEARCH);
					return msg;
			}
		

		
	}

	/*
	 * 2016-10-25 11:25:27
	 * 检查证书编号是否已存在
	 */
	@RequestMapping(value = "zss/{zsid}/{qzlx}/{xmbh}/issave", method = RequestMethod.POST)
	public @ResponseBody ResultMessage UpdateZSInfo2_isSave(@PathVariable("zsid") String zsid, @PathVariable("qzlx") String qzlx, @PathVariable("xmbh") String xmbh,
			HttpServletRequest request, HttpServletResponse response) {
		ResultMessage msg = new ResultMessage();
		String zsbh_all = request.getParameter("zsbh");
		String bdcdylx = request.getParameter("bdcdylx");
	    int start = 0;
	    int end = 1;
	    String qzzl ="D";//权证种类
       //获取qzzl
	    if (zsbh_all.substring(start, end).matches("[a-zA-Z]")){
	      qzzl = zsbh_all.substring(start, end).toUpperCase();
	    }
	    /*-验证--*/
	    long countzs = zsService.validateZSBH(zsid, zsbh_all, qzlx);
		if(countzs > 0 ){
			msg.setMsg("编号已存在,是否继续保存编号");
			msg.setSuccess("false");
		}else {
			if (SF.YES.Value.equals(ConfigHelper.getNameByValue("ZsbhManager"))){
				msg = zsService.saveZSBHWithManager(zsid, xmbh, zsbh_all, qzlx, qzzl, bdcdylx);				
			}else {
				msg = zsService.saveZSBH(zsid, xmbh, zsbh_all, qzlx, qzzl, request,bdcdylx);
			}
		}
		return msg;
	}
	/**
	 * 保存证书编号，保存到工作、现在、历史
	 * 
	 * @作者 海豹
	 * @创建时间 2015年8月7日下午3:16:42
	 * @param zsid
	 * @param request
	 * @param response
	 * @return
	 */
	
	@RequestMapping(value = "zss/{zsid}/{qzlx}/{xmbh}", method = RequestMethod.POST)
	public @ResponseBody ResultMessage UpdateZSInfo2(@PathVariable("zsid") String zsid, @PathVariable("qzlx") String qzlx, @PathVariable("xmbh") String xmbh,
			HttpServletRequest request, HttpServletResponse response) {
		ResultMessage msg = new ResultMessage();
		String zsbh_all = request.getParameter("zsbh");
		String bdcdylx = request.getParameter("bdcdylx");
	    int start = 0;
	    int end = 1;
	    String qzzl ="D";//权证种类
       //如果编号第一个为字母
	    if (zsbh_all.substring(start, end).matches("[a-zA-Z]")){
	      qzzl = zsbh_all.substring(start, end).toUpperCase();
	    }
	   
		if (SF.YES.Value.equals(ConfigHelper.getNameByValue("ZsbhManager"))){
			msg = zsService.saveZSBHWithManager(zsid, xmbh, zsbh_all, qzlx, qzzl, bdcdylx);	
		}else {
			msg = zsService.saveZSBH(zsid, xmbh, zsbh_all, qzlx, qzzl, request, bdcdylx);
		}
		return msg;
	}

	/**
	 * 获取发证记录列表（URL:"/{xmbh}/fzs",Method：GET）
	 *
	 * @param xmbh
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/{xmbh}/fzs", method = RequestMethod.GET)
	public @ResponseBody Message GetFZList(@PathVariable("xmbh") String xmbh, HttpServletRequest request, HttpServletResponse response) {
		Integer page = 1;
		if (request.getParameter("page") != null) {
			page = Integer.parseInt(request.getParameter("page"));
		}
		Integer rows = 10;
		if (request.getParameter("rows") != null) {
			rows = Integer.parseInt(request.getParameter("rows"));
		}
		Page fzPaged = zsService.GetPagedFZList(xmbh, page, rows);
		Message m = new Message();
		m.setTotal(fzPaged.getTotalCount());
		m.setRows(fzPaged.getResult());
		return m;
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
	@RequestMapping(value = "/{paras}/{bdcdylx}/fzs", method = RequestMethod.POST)
	public @ResponseBody ResultMessage AddFZInfos(@PathVariable("paras") String paras,@PathVariable("bdcdylx") String bdcdylx, @ModelAttribute("fzxxAttribute") BDCS_DJFZ djfzBO, BindingResult result,
			HttpServletRequest request, HttpServletResponse response) {
		ResultMessage msg = new ResultMessage();
		if (result.hasErrors()) {
			msg.setMsg(result.toString());
		} else {
			// String[] keys = paras.split("\\*");// 多个记录之间键值对用*分隔
			String outcertinfolist = request.getParameter("certinfolist");
			String[] keys = outcertinfolist.split("\\*");// 多个记录之间键值对用*分隔
			BDCS_XMXX xmxx = null;
			for (String key : keys) {
				String xmbh2 = key.split(":")[0];
				String hfzsh = key.split(":")[1];
				if (xmxx == null) {
					xmxx = Global.getXMXXbyXMBH(xmbh2);
				}
				String condition = MessageFormat.format("XMBH=''{0}'' AND HFZSH=''{1}''", xmbh2, hfzsh);
				BDCS_DJFZ djfz = zsService.getDJFZ(condition);				
				if (djfz == null) {
					djfz = new BDCS_DJFZ();
					djfz.setId((String) SuperHelper.GeneratePrimaryKey());
				}
				djfz.setYWH(xmxx == null ? "" : xmxx.getPROJECT_ID());
				djfz.setFZRY(djfzBO.getFZRY());
				djfz.setFZSJ(djfzBO.getFZSJ());
				djfz.setLZRXM(djfzBO.getLZRXM());
				djfz.setLZRZJLB(djfzBO.getLZRZJLB());
				djfz.setLZRZJHM(djfzBO.getLZRZJHM());
				djfz.setLZRDH(djfzBO.getLZRDH());
				djfz.setLZRDZ(djfzBO.getLZRDZ());
				djfz.setLZRYB(djfzBO.getLZRYB());
				djfz.setXMBH(xmbh2);
				djfz.setHFZSH(hfzsh);
				djfz.setYSDM("1");// TODO YSDM为必填项，暂时赋值为1
				zsService.AddFZXX(djfz);
				//添加发证信息到rkqzb中			
				zsService.AddFZXXtoRKQZB(xmbh2,hfzsh, djfzBO.getFZSJ(),djfzBO.getLZRXM(),djfzBO.getLZRZJHM(),bdcdylx);
			}
		}
		YwLogUtil.addYwLog("批量添加发证-成功", ConstValue.SF.YES.Value, ConstValue.LOG.ADD);
		return msg;
	}

	/**
	 * 删除发证记录（URL:"/{xmbh}/fzs/{fzid}",Method：DELETE）
	 * 
	 * @param xmbh
	 * @param fzid
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/{xmbh}/fzs/{fzid}", method = RequestMethod.DELETE)
	public @ResponseBody ResultMessage DeleteFZInfo(@PathVariable("xmbh") String xmbh, @PathVariable("fzid") String fzid, HttpServletRequest request, HttpServletResponse response) {
		ResultMessage msg = new ResultMessage();
		if (StringUtils.hasLength(fzid)) {
			zsService.DeleteFZXX(fzid,xmbh);				
			YwLogUtil.addYwLog("删除发证-成功", ConstValue.SF.YES.Value, ConstValue.LOG.DELETE);
		}
		return msg;
	}

	/**
	 * 根据证书ID和项目编号获取证书信息（URL:"/{xmbh}/{zsid}/zsInfo",Method：GET）
	 * 
	 * @作者 海豹
	 * @创建时间 2015年6月7日下午11:31:05
	 * @param xmbh
	 * @param zsid
	 * @return
	 */
	@RequestMapping(value = "/{xmbh}/{zsid}/zsInfo", method = RequestMethod.GET)
	public @ResponseBody ZS zsInfo(@PathVariable String xmbh, @PathVariable String zsid, HttpServletRequest request) {
		// 获取证书详细信息时候 生成二维码图片WUZ
		// ---------------------------------------------------------------------------------------
		String imgformat = "png";
		System.out.println("xmbh="+xmbh+"/"+zsid);
		String filepath=String.format("%s\\resources\\qrcode\\%s.%s",request.getRealPath("/"),xmbh,imgformat);
		File file=new File(filepath);
		System.out.println("xmbh="+xmbh+"/"+zsid);
		if(!file.exists()){
			BDCS_XMXX xmxx= basecommondao.get(BDCS_XMXX.class, xmbh);
			System.out.println("xmbh="+xmbh+"/"+zsid);
			BufferedImage img = BarcodeUtil.createBarcodeStream(xmxx.getYWLSH(), "Code128", 240, 60, true);
			try {
				ImageIO.write(img,"png",new FileOutputStream(new File(filepath)));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		String xzqdm = ConfigHelper.getNameByValue("XZQHDM").toString();
		String content ="";
		System.out.println("xzqdm="+xzqdm);
		if(xzqdm != null && xzqdm.equals("420900")){
			content = zsService.GetQRCodeInfoXG(xmbh, zsid);
		}else{
			content = zsService.GetQRCodeInfo(xmbh, zsid);
		}
		QRCodeHelper.CreateQRCode(content, zsid, imgformat, 120, 120, request);
		//创建二维. 应用下载宗地图和分户图
		if("0".equals(ConfigHelper.getNameByValue("ISSHOW_QR"))) {
			content= EncodeTools.encoderByDES(zsid);
			QRCodeHelper.CreateQRCode(content,zsid+"_QR", imgformat, 135, 135, request);
			
		}
		// ----------------------------------------------------------------------------------------
		ZS zsInfo = zsService.getZSForm(xmbh, zsid);
		return zsInfo;
	}

	/**
	 * 获取初始登记新建商品房首次登记的证书信息，其中不需要封面信息，不需要二维码信息，附记需要添加预购商品房信息
	 * @作者 海豹
	 * @创建时间 2015年12月5日下午4:02:51
	 * @param xmbh
	 * @param zsid
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/{xmbh}/{zsid}/buildingzsinfo", method = RequestMethod.GET)
	public @ResponseBody ZS buildingzsinfo(@PathVariable String xmbh, @PathVariable String zsid, HttpServletRequest request) {
		ZS buildingzsinfo = zsService.getBuildingZSForm(xmbh, zsid);
		return buildingzsinfo;
	}

	/**
	 * 获取不动产登记证明（URL:"/{xmbh}/bdcdjzm/{zsid}",Method：GET）
	 * 
	 * @作者 刘树峰
	 * @创建时间 2015年6月15日上午4:21:47
	 * @param xmbh
	 * @param zsid
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/{xmbh}/bdcdjzm/{zsid}", method = RequestMethod.GET)
	public @ResponseBody Map getBDCDJZM(@PathVariable String xmbh, @PathVariable String zsid, HttpServletRequest request) {
		// 获取证书详细信息时候 生成二维码图片WUZ
		// ---------------------------------------------------------------------------------------
		String imgformat = "png";
		String filepath=String.format("%s\\resources\\qrcode\\%s.%s",request.getRealPath("/"),xmbh,imgformat);
		File file=new File(filepath);
		if(!file.exists()){
			BDCS_XMXX xmxx= basecommondao.get(BDCS_XMXX.class, xmbh);
			BufferedImage img = BarcodeUtil.createBarcodeStream(xmxx.getYWLSH(), "Code128", 240, 60, true);
			try {
				ImageIO.write(img,"png",new FileOutputStream(new File(filepath)));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		String xzqdm = ConfigHelper.getNameByValue("XZQHDM").toString();
		String content ="";
		if(xzqdm != null && xzqdm.equals("420900")){
			content = zsService.GetQRCodeInfoXG(xmbh, zsid);
			QRCodeHelper.CreateQRCode(content, zsid, imgformat, 135, 135, request);
		}else{
			content = zsService.GetQRCodeInfo(xmbh, zsid);
			QRCodeHelper.CreateQRCode(content, zsid, imgformat, 120, 120, request);
		}
		
		
		// ----------------------------------------------------------------------------------------
		Map map = zsService.getBDCDJZM(xmbh, zsid);
		return map;
	}

	/**
	 * 获取证书二维码信息
	 * 
	 * @作者 俞学斌
	 * @创建时间 2015年11月05日12:08:08
	 * @param xmbh
	 * @param zsid
	 * @return
	 */
	@RequestMapping(value = "getqrcode/{xmbh}/{zsid}", method = RequestMethod.GET)
	public @ResponseBody String GetQRCodeInfo(@PathVariable("xmbh") String xmbh, @PathVariable("xmbh") String zsid, HttpServletRequest request, HttpServletResponse response) {
		YwLogUtil.addYwLog("获取证书二维码信息-成功", ConstValue.SF.YES.Value, ConstValue.LOG.SEARCH);
		return zsService.GetQRCodeInfo(xmbh, zsid);
	}


	/**
	 * 获取登记机构（URL:"/{xmbh}/djjg",Method：GET）
	 * 
	 * @param xmbh
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/{xmbh}/djjg", method = RequestMethod.GET)
	public @ResponseBody String GetDJJG(@PathVariable("xmbh") String xmbh, HttpServletRequest request, HttpServletResponse response) {
		List<BDCS_QL_GZ> qls = basecommondao.getDataList(BDCS_QL_GZ.class, " XMBH='"+ xmbh +"'");
		if(qls!=null&&qls.size()>0){
			return qls.get(0).getDJJG();
		}
		return "";
	}
	
	/**
	 * 获取是否在打印时候生成PDF的配置
	 * 
	 * @作者 WUZ
	 * @创建时间 2015年11月05日12:08:08
	 * @return
	 */
	@RequestMapping(value = "getzsconfig", method = RequestMethod.GET)
	public @ResponseBody String GetZSConfig() {
			String zsdy_sfpdf=	ConfigHelper.getNameByValue("PRINTPARAM_ZSDY_SFPDF");
			String zdtshow=ConfigHelper.getNameByValue("ZDTSHOW");
			String fhtshow=ConfigHelper.getNameByValue("FHTSHOW"); 
			String zrzzdtshow=ConfigHelper.getNameByValue("ZRZZDTSHOW"); 
			String isedit=ConfigHelper.getNameByValue("ZSPARAM_IS_EDIT");
			StringBuilder sb=new  StringBuilder();
			sb.append("{'ZSDY_SFPDF':");
			sb.append(zsdy_sfpdf);
			sb.append(",'ZDTSHOW':");
			sb.append(zdtshow);
			sb.append(",'FHTSHOW':");
			sb.append(fhtshow);
			sb.append(",'ZSPARAM_IS_EDIT':");
			sb.append(isedit);
			sb.append(",'ZRZZDTSHOW' :");
			sb.append(zrzzdtshow==null||"".equals(zrzzdtshow)?0:zrzzdtshow);
			sb.append("}");
			return sb.toString();
	}

	/**
	 * @作者 wuz 对外向非登记系统提供获取二维码图片接口。传入参数，生成二维码图片
	 * @创建时间 2015年12月4日下午2:25:01
	 * @param request
	 * @param response
	 * @param width
	 *            二维码的宽度 可缺省 默认120PX
	 * @param height
	 *            二维码的高度 可缺省 默认120PX
	 * @param content
	 *            二维码的内容
	 * @param imgformat
	 *            二维码的图片格式 可缺省 默认PNG
	 * @throws IOException
	 */
	@RequestMapping(value = "getqrcode", method = RequestMethod.GET)
	public void GetQRCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String width = request.getParameter("width");
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
		BufferedImage img = QRCodeHelper.CreateQRCode(content, imgformat, Integer.parseInt(width), Integer.parseInt(height));
		response.setContentType("image/" + imgformat + "; charset=GBK");
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setIntHeader("Expires", -1);
		ImageIO.write(img, imgformat, response.getOutputStream());
		YwLogUtil.addYwLog("生成二维码-成功", ConstValue.SF.YES.Value, ConstValue.LOG.ADD);
	}

	@RequestMapping(value = "getbarcode", method = RequestMethod.GET)
	public void GetBRCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String width = request.getParameter("width");
		String height = request.getParameter("height");
		String content = RequestHelper.getParam(request, "content");
		String imgformat = request.getParameter("imgformat");
		String code = request.getParameter("code");
		String showText = request.getParameter("showText");
		boolean isShowText = true;//是否显示图片下内容
		if (StringUtils.isEmpty(width))
			width = "240";
		if (StringUtils.isEmpty(height))
			height = "60";
		if (StringUtils.isEmpty(content))
			content = "";
		if (StringUtils.isEmpty(imgformat))
			imgformat = "png";
		if (StringUtils.isEmpty(code))
			code = "Code128";
		if (!StringUtils.isEmpty(showText)){
		    if(!"true".equals(showText)){
			isShowText = false;
		    };
		}
		    
		BufferedImage  img = BarcodeUtil.createBarcodeStream(content, code, Integer.parseInt(width), Integer.parseInt(height), isShowText);
		response.setContentType("image/"+imgformat+"; charset=GBK");
		response.setHeader("Pragma","no-cache");
		response.setHeader("Cache-Control","no-cache");
		response.setIntHeader("Expires",-1);
		ImageIO.write(img,imgformat,response.getOutputStream());
	}

	@RequestMapping(value = "canprintzs/{xmbh}/{zsid}/", method = RequestMethod.GET)
	public @ResponseBody ResultMessage CanPrintZS(@PathVariable("xmbh") String xmbh, @PathVariable("zsid") String zsid, HttpServletRequest request, HttpServletResponse response) {
		ResultMessage ms = zsService.CanPrintZS(xmbh, zsid);
		return ms;
	}

	/**
	 * 根据项目编号和柜号存取柜号信息（URL:"/{xmbh}/{gh}",Method：GET）
	 * 
	 * @作者 卜晓波
	 * @创建时间 2015年12月4日 05:11:30
	 * @param xmbh
	 * @param gh
	 * @return
	 */
	@RequestMapping(value = "/{xmbh}/{gh}", method = RequestMethod.GET)
	public @ResponseBody ResultMessage ghInfo(@PathVariable String xmbh, @PathVariable String gh, HttpServletRequest request) {
		try {

			ResultMessage msg = zsService.saveDAGH(xmbh, gh);
			return msg;
		} catch (Exception e) {

		}
		return null;
	}

	@RequestMapping(value = "/{xmbh}/cfdagh", method = RequestMethod.GET)
	public @ResponseBody String getDAGH(@PathVariable String xmbh, HttpServletRequest request) {
		return zsService.getDAGH(xmbh);
	}

	@RequestMapping(value = "/{qlid}/zsbh", method = RequestMethod.GET)
	public @ResponseBody String getZSBHbyQLID(@PathVariable String qlid, HttpServletRequest request) {
		String zsbh = "";
		// BDCS_QDZR_XZ改成BDCS_QDZR_LS 以及 BDCS_ZS_XZ 改成BDCS_ZS_ls 因为有些流程需要显示上一手证书，这需要去历史层找wuzhu
		String sql = " id in (select ZSID from BDCS_QDZR_LS WHERE QLID='" + qlid + "')";
		List<BDCS_ZS_LS> listzss = basecommondao.getDataList(BDCS_ZS_LS.class, sql);
		if (listzss != null && listzss.size() > 0) {
			for (BDCS_ZS_LS zs : listzss) {
				if(!StringHelper.isEmpty(zs.getZSBH())) {
					zsbh += zs.getZSBH() + ",";
				}
			}
		}
		if (zsbh.endsWith(",")) {
			zsbh = zsbh.substring(0, zsbh.length() - 1);
		}
		return zsbh;
	}
	
	@RequestMapping(value = "{tpl}/{xmbh}/fzinfo", method = RequestMethod.POST)
	public @ResponseBody String SinglePDF(HttpServletRequest request,
			HttpServletResponse response, @PathVariable("tpl") String tpl,
			@PathVariable("xmbh") String xmbh) throws IOException,
			DocumentException {
		if (StringUtils.isEmpty(tpl))
			return "";
		String createdPDFFileName = xmbh;
		String basePath = request.getRealPath("/") + "\\resources\\PDF";// 
		String url = request.getContextPath() + "/resources/PDF/tmp/" + xmbh + "-" + tpl + ".pdf";
		String outpath = basePath + "\\tmp\\" + xmbh + "-" + tpl + ".pdf";
		String TemplatePDF = basePath + "\\tpl\\" + tpl + ".pdf";
		FileOutputStream outstream = new FileOutputStream(outpath);
		ArrayList<ByteArrayOutputStream> baos = new ArrayList<ByteArrayOutputStream>();
		String proinst_id = null;
		String file_number;
		List<BDCS_XMXX> listxmxxs = basecommondao.getDataList(BDCS_XMXX.class, "xmbh='"+xmbh+"'");
		if(listxmxxs.size()>0){
			BDCS_XMXX xmxx=listxmxxs.get(0);
			file_number=xmxx.getPROJECT_ID();
			if(!StringHelper.isEmpty(file_number)){
				String sql = "select proinst_id from bdc_workflow.wfi_proinst where file_number='"
						+ file_number + "'";
				List<Map> proinst_ids = basecommondao.getDataListByFullSql(sql);
				if (proinst_ids != null && proinst_ids.size() > 0) {
					Map proinst = proinst_ids.get(0);
					proinst_id = proinst.get("PROINST_ID").toString();
				}
			}
		}
		Map<String, Map> pageDatas = new TreeMap<String, Map>(new Comparator<String>() {
			public int compare(String obj1, String obj2) {
				// 升序排序return obj1.compareTo(obj2);
				return Integer.parseInt(obj1) - Integer.parseInt(obj2);

			}
		});
		String dataString = request.getParameter("info");
		String keyName = "";
		int keyPageNum = 0;
		pageDatas.put("1", new HashMap<String, Object>());// 初始化第一页
		JSONArray jsonarr = JSONArray.fromObject(dataString); 
		for (int i = 0; i < jsonarr.size(); i++) {
	         String  jsonstr=jsonarr.getString(i);
	         JSONObject object=JSON.parseObject(jsonstr);
	 		for (Entry<String, Object> entry :object.entrySet()) {

				keyPageNum = getPageNum(entry.getKey(),i);
				keyName = getID(entry.getKey(), keyPageNum);
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
					
					//修改领证时间格式
					Pattern ptn_lzsj = Pattern.compile("^lzsj.*");
					Matcher mt_lzsj = ptn_lzsj.matcher(keyName);
					if (mt_lzsj.matches()) {
						Long date = new Long(entryvalue.toString());
						String sj = "";
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
						sj = sdf.format(date);
						entryvalue = sj;
					}

				}
				
				if (pageDatas.containsKey(String.valueOf(keyPageNum)))
				  if(pageDatas.get(String.valueOf(keyPageNum)).containsKey(keyName)){
					  int j=i;
					  if(i>29){
						  j=i%29;
					  }
					  pageDatas.get(String.valueOf(keyPageNum)).put(keyName+j, entryvalue); 
				  }else{
					  pageDatas.get(String.valueOf(keyPageNum)).put(keyName, entryvalue);
				  }
					
				else {
					pageDatas.put(String.valueOf(keyPageNum), new HashMap<String, Object>());
					if(pageDatas.get(String.valueOf(keyPageNum)).containsKey(keyName)){
						pageDatas.get(String.valueOf(keyPageNum)).put(keyName+i, entryvalue);
					}else{
						pageDatas.get(String.valueOf(keyPageNum)).put(keyName, entryvalue);
					}
					
					
				}
			
	 		} 
	 		
	 		
	 		
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
		File file=new File(outpath);
		try {
			smProMaterService.SaveFileToProject(file, "签收簿", proinst_id,".pdf");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return url;
		
	 
	}
	
	/**
	 * @Description: 领证人头像上传
	 * @Title: SaveOrUpdateLZRPIC
	 * @Author：赵梦帆
	 * @Data：2016年12月8日 上午8:36:40
	 * @param request
	 * @param response
	 * @return
	 * @return boolean
	 */
	@RequestMapping(value = "/SaveOrUpdateLZRPIC", method = RequestMethod.POST)
	public @ResponseBody boolean SaveOrUpdateLZRPIC(HttpServletRequest request, HttpServletResponse response) {
		String zsbh_all = request.getParameter("zsbh");
		String bdcqzh = request.getParameter("bdcqzh");
		String pic = request.getParameter("pic");
		String lzrxm = request.getParameter("lzr");
		int start = 0;
		int end = 1;
	    //如果编号第一个为字母
		String zsbh=zsbh_all;
		String qzlx="0";
		String qzzl="D";
	    if (zsbh_all.substring(start, end).matches("[a-zA-Z]")){
	      zsbh = zsbh_all.substring(end, zsbh_all.length());
	      qzzl = zsbh_all.substring(start,end);
	    }
	  
	    if( bdcqzh.contains("不动产证明第")) {
			qzlx="1";
		}
		List<BDCS_RKQZB> qz = basecommondao.getDataList(BDCS_RKQZB.class, "QZBH ='"+ zsbh +"' AND QZLX='" + qzlx + "' AND QZZL='" + qzzl + "'");
		if(qz!=null&&qz.size()>0){
			BDCS_RKQZB rkqzb = qz.get(0);
			rkqzb.setLZRPIC(pic);
			rkqzb.setBDCQZH(bdcqzh);
			basecommondao.update(rkqzb);
			basecommondao.flush();
			String syqk = StringHelper.isEmpty(rkqzb.getSYQK()) ? "" : rkqzb.getSYQK()+"<br/>";
			rkqzb.setSYQK(syqk + StringHelper.FormatDateOnType(new Date(), "yyyy-MM-dd HH:mm:ss")+"："+Global.getCurrentUserInfo().getLoginName()+"保存领证人"+lzrxm+"的头像图片;");

			return true;
		}else{
			BDCS_RKQZB rkqzb = new BDCS_RKQZB();
			rkqzb.setLZRY(lzrxm);
			rkqzb.setLZRPIC(pic);
			rkqzb.setBDCQZH(bdcqzh);			
			//新增基本信息
			rkqzb.setId((String) SuperHelper.GeneratePrimaryKey());					
			rkqzb.setCJSJ(new Date());
			rkqzb.setQZBH(StringHelper.getLong(zsbh));	
			rkqzb.setQZZL(qzzl);	
			rkqzb.setQZLX(qzlx);			
			String syqk = StringHelper.isEmpty(rkqzb.getSYQK()) ? "" : rkqzb.getSYQK()+"<br/>";
			rkqzb.setSYQK(syqk + StringHelper.FormatDateOnType(new Date(), "yyyy-MM-dd HH:mm:ss")+"："+Global.getCurrentUserInfo().getLoginName()+"创建证书编号并保存领证人"+lzrxm+"的头像图片;");

			basecommondao.save(rkqzb);
			basecommondao.flush();
			
			return true;
		}
	}
	
	@RequestMapping(value = "/GetLZRPIC", method = RequestMethod.GET)
	public @ResponseBody String GetLZRPIC(HttpServletRequest request, HttpServletResponse response) {
		String zsbh = request.getParameter("zsbh");
		int start = 0;
		int end = 1;
		if (zsbh.substring(start, end).matches("[a-zA-Z]")){
		      zsbh = zsbh.substring(end, zsbh.length());
		    }
		List<BDCS_RKQZB> qz = basecommondao.getDataList(BDCS_RKQZB.class, "QZBH ='"+zsbh+"'");
		if(qz!=null&&qz.size()>0){
			BDCS_RKQZB rkqz = qz.get(0);
			return rkqz.getLZRPIC();
		}else{
			return null;
		}
	}
	
	/**
	 * 根据PDF模板创建流
	 * @Title: CreatePdfStream
	 * @author:WUZHU
	 * @date：2015年11月14日 下午5:03:01
	 * @param tplName
	 * @param data
	 * @return
	 * @throws IOException
	 * @throws DocumentException
	 */
	private ByteArrayOutputStream CreatePdfStream(String tplName, Map<String, Object> data) throws IOException, DocumentException {
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
							PdfContentByte overContent = stamp.getOverContent(1);
							List<AcroFields.FieldPosition> imgPosition = form.getFieldPositions(_key);
							float x = imgPosition.get(0).position.getLeft();
							float y = imgPosition.get(0).position.getBottom();
							image1.setAbsolutePosition(x, y);
							image1.scaleAbsolute(72, 30);
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
	 * 格式 H_BDCDYH__1 根据__后面的数判断是第几页
	 * @Title: getPageNum
	 * @author:WUZHU
	 * @date：2015年11月14日 下午5:04:13
	 * @param key
	 * @return
	 */
	private int getPageNum(String key,int i) {
		 int pageindex = 0;
		if (!StringHelper.isEmpty(key)) {
			double count = Double.valueOf(i+1);
			pageindex = (int) Math.ceil(count / 30.00);// 29行分一页
		}
		return pageindex;
	}
	
	/**
	 * 格式 H_BDCDYH__1 转成H_BDCDYH1
	 * @Title: getID
	 * @author:WUZHU
	 * @date：2015年11月14日 下午5:04:03
	 * @param key
	 * @param pageindex
	 * @return
	 */
	private String getID(String key, int pageindex) {
		String name = key;
		int end = key.lastIndexOf("__");
		if (end != -1) {
			String[] names = key.split("__");
			int count = Integer.valueOf(names[1]);
			int pagecount = count - (pageindex - 1) * 30;
			name = names[0] + pagecount;
		}
		return name;
	}
	
	/**
	 * @Description: 抵押注销归还证书，抽取抵押权证
	 * @Title: GetDYZSList
	 * @Author：赵梦帆
	 * @Data：2016年12月21日 下午2:04:53
	 * @param xmbh
	 * @param request
	 * @param response
	 * @return
	 * @return Message
	 */
	@RequestMapping(value = "/getdyzs/{xmbh}", method = RequestMethod.GET)
	public @ResponseBody Message GetDYZSList(@PathVariable("xmbh") String xmbh, HttpServletRequest request, HttpServletResponse response) {
		return zsService.GetDYZSList(xmbh);
	}
	
	/**
	 * @Description: 获取抵押注销证书列表
	 * @Title: GetFZListEX
	 * @Author：赵梦帆
	 * @Date：2016年12月21日 下午3:53:01
	 * @param xmbh
	 * @param request
	 * @param response
	 * @return
	 * @return Message
	 */
	@RequestMapping(value = "/{xmbh}/fzsEX", method = RequestMethod.GET)
	public @ResponseBody Message GetFZListEX(@PathVariable("xmbh") String xmbh, HttpServletRequest request, HttpServletResponse response) {
		Integer page = 1;
		if (request.getParameter("page") != null) {
			page = Integer.parseInt(request.getParameter("page"));
		}
		Integer rows = 10;
		if (request.getParameter("rows") != null) {
			rows = Integer.parseInt(request.getParameter("rows"));
		}
		Page fzPaged = zsService.GetPagedFZListEX(xmbh, page, rows);
		Message m = new Message();
		m.setTotal(fzPaged.getTotalCount());
		m.setRows(fzPaged.getResult());
		return m;
	}
	
	
	/**
	 * @Description: 抵押注销发证
	 * @Title: AddFZInfosEX
	 * @Author: zhaomengfan
	 * @Date: 2016年12月22日下午2:41:19
	 * @param paras
	 * @param djfzBO
	 * @param result
	 * @param request
	 * @param response
	 * @return
	 * @return ResultMessage
	 */
	@RequestMapping(value = "/{paras}/fzsEX", method = RequestMethod.POST)
	public @ResponseBody ResultMessage AddFZInfosEX(@PathVariable("paras") String paras, @ModelAttribute("fzxxAttribute") BDCS_DJFZ djfzBO, BindingResult result,
			HttpServletRequest request, HttpServletResponse response) {
		ResultMessage msg = new ResultMessage();
		if (result.hasErrors()) {
			msg.setMsg(result.toString());
		} else {
			// String[] keys = paras.split("\\*");// 多个记录之间键值对用*分隔
			String outcertinfolist = request.getParameter("certinfolist");
			String[] keys = outcertinfolist.split("\\*");// 多个记录之间键值对用*分隔
			BDCS_XMXX xmxx = null;
			for (String key : keys) {
				String xmbh2 = key.split(":")[0];
				String hfzsh = key.split(":")[1];
				if (xmxx == null) {
					xmxx = Global.getXMXXbyXMBH(xmbh2);
				}
				String condition = MessageFormat.format("XMBH=''{0}'' AND HFZSH=''{1}''", xmbh2, hfzsh);
				List<BDCS_DJFZEX> djfzs = basecommondao.getDataList(BDCS_DJFZEX.class, condition);
				BDCS_DJFZEX djfz = null;
				if(djfzs!=null&&djfzs.size()>0)
					djfz = djfzs.get(0);

				if (djfz == null) {
					djfz = new BDCS_DJFZEX();
					djfz.setId((String) SuperHelper.GeneratePrimaryKey());
				}
				djfz.setYWH(xmxx == null ? "" : xmxx.getPROJECT_ID());
				djfz.setFZRY(djfzBO.getFZRY());
				djfz.setFZSJ(djfzBO.getFZSJ());
				djfz.setLZRXM(djfzBO.getLZRXM());
				djfz.setLZRZJLB(djfzBO.getLZRZJLB());
				djfz.setLZRZJHM(djfzBO.getLZRZJHM());
				djfz.setLZRDH(djfzBO.getLZRDH());
				djfz.setLZRYB(djfzBO.getLZRYB());
				djfz.setXMBH(xmbh2);
				djfz.setHFZSH(hfzsh);
				basecommondao.save(djfz);
				basecommondao.flush();
			}
		}
		YwLogUtil.addYwLog("批量添加发证-成功", ConstValue.SF.YES.Value, ConstValue.LOG.ADD);
		return msg;
	}
	
	/**
	 * @Description: 注销发证-撤销发证
	 * @Title: DeleteFZInfoEX
	 * @Author: zhaomengfan
	 * @Date: 2016年12月22日下午4:41:28
	 * @param xmbh
	 * @param fzid
	 * @param request
	 * @param response
	 * @return
	 * @return ResultMessage
	 */
	@RequestMapping(value = "/{xmbh}/fzsEX/{fzid}", method = RequestMethod.DELETE)
	public @ResponseBody ResultMessage DeleteFZInfoEX(@PathVariable("xmbh") String xmbh, @PathVariable("fzid") String fzid, HttpServletRequest request, HttpServletResponse response) {
		ResultMessage msg = new ResultMessage();
		if (StringUtils.hasLength(fzid)) {
			BDCS_DJFZEX zs = basecommondao.get(BDCS_DJFZEX.class, fzid);
			if(zs!=null){
				zs.setFZRY(null);
				zs.setFZSJ(null);
				zs.setLZRXM(null);
				zs.setLZRZJLB(null);
				zs.setLZRZJHM(null);
				zs.setLZRDH(null);
				zs.setLZRYB(null);
				basecommondao.update(zs);
			}
			YwLogUtil.addYwLog("删除发证-成功", ConstValue.SF.YES.Value, ConstValue.LOG.DELETE);
		}
		return msg;
	}
	
	/**
	 * @Description: 领证人头像上传
	 * @Title: SaveOrUpdateLZRPIC
	 * @Author：赵梦帆
	 * @Data：2016年12月8日 上午8:36:40
	 * @param request
	 * @param response
	 * @return
	 * @return boolean
	 */
	@RequestMapping(value = "/SaveOrUpdateLZRPICEX", method = RequestMethod.POST)
	public @ResponseBody boolean SaveOrUpdateLZRPICEX(HttpServletRequest request, HttpServletResponse response) {
		String id = request.getParameter("id");
		String pic = request.getParameter("pic");
		BDCS_DJFZEX qz = basecommondao.get(BDCS_DJFZEX.class, id);
		if(qz!=null){
			qz.setLZRPIC(pic);
			basecommondao.update(qz);
			basecommondao.flush();
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * @Description: 注销发证-领证人头像查看
	 * @Title: GetLZRPICEX
	 * @Author: zhaomengfan
	 * @Date: 2016年12月22日下午4:42:03
	 * @param request
	 * @param response
	 * @return
	 * @return String
	 */
	@RequestMapping(value = "/GetLZRPICEX", method = RequestMethod.GET)
	public @ResponseBody String GetLZRPICEX(HttpServletRequest request, HttpServletResponse response) {
		String id = request.getParameter("id");
		BDCS_DJFZEX qz = basecommondao.get(BDCS_DJFZEX.class, id);
		if(qz!=null){
			return qz.getLZRPIC();
		}else{
			return null;
		}
	}
	//打印证书时将缮证信息存入
	@RequestMapping(value = "zss/{zsid}/{qzlx}/{xmbh}/{zsbh}/{bdcdylx}/printzs", method = RequestMethod.POST)
	public @ResponseBody void UpdateSZXXtoRKQZB(@PathVariable("zsid") String zsid, @PathVariable("qzlx") String qzlx, @PathVariable("xmbh") String xmbh, @PathVariable("zsbh") String zsbh,
			@PathVariable("bdcdylx") String bdcdylx,HttpServletRequest request, HttpServletResponse response) {		
	    int start = 0;
	    int end = 1;
	    String qzzl ="D";//权证种类
		if ("null".equals(zsbh)) {
			zsbh = null;
		}
       //如果编号第一个为字母
	    if (!StringHelper.isEmpty(zsbh) && zsbh.substring(start, end).matches("[a-zA-Z]")){
	      qzzl = zsbh.substring(start, end).toUpperCase();
	    }
	   boolean flag =  zsService.AddSZXXToRKQZB(qzlx, zsbh, qzzl, zsid, xmbh, bdcdylx);
	   
	}
}
