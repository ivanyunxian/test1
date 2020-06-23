package com.supermap.realestate.registration.service.impl;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import com.supermap.realestate.registration.ViewClass.ProjectInfo;
import com.supermap.realestate.registration.constraint.ConstraintCheck;
import com.supermap.realestate.registration.factorys.HandlerFactory;
import com.supermap.realestate.registration.mapping.HandlerMapping.RegisterWorkFlow;
import com.supermap.realestate.registration.model.BDCS_DJDY_GZ;
import com.supermap.realestate.registration.model.BDCS_SQR;
import com.supermap.realestate.registration.model.BDCS_XMXX;
import com.supermap.realestate.registration.model.WFD_MAPPING;
import com.supermap.realestate.registration.model.interfaces.RealUnit;
import com.supermap.realestate.registration.model.interfaces.Rights;
import com.supermap.realestate.registration.model.interfaces.SubRights;
import com.supermap.realestate.registration.service.BatchProjectService;
import com.supermap.realestate.registration.service.DYService;
import com.supermap.realestate.registration.service.QLService;
import com.supermap.realestate.registration.tools.RightsTools;
import com.supermap.realestate.registration.tools.UnitTools;
import com.supermap.realestate.registration.util.ConfigHelper;
import com.supermap.realestate.registration.util.ConstValue.BDCDYLX;
import com.supermap.realestate.registration.util.ConstValue.DJDYLY;
import com.supermap.realestate.registration.util.ConstValue.DYFS;
import com.supermap.realestate.registration.util.ConstValue.QLLX;
import com.supermap.realestate.registration.util.ConstValue.SQRLB;
import com.supermap.realestate.registration.util.Global;
import com.supermap.realestate.registration.util.ProjectHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.realestate.registration.util.data.DataColumn;
import com.supermap.realestate.registration.util.data.DataColumnCollection;
import com.supermap.realestate.registration.util.data.DataException;
import com.supermap.realestate.registration.util.data.DataRow;
import com.supermap.realestate.registration.util.data.DataTable;
import com.supermap.wisdombusiness.core.SuperHelper;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import com.supermap.wisdombusiness.framework.model.User;
import com.supermap.wisdombusiness.web.Message;
import com.supermap.wisdombusiness.web.ResultMessage;
import com.supermap.wisdombusiness.workflow.model.Wfd_Prodef;
import com.supermap.wisdombusiness.workflow.model.Wfi_ProInst;
import com.supermap.wisdombusiness.workflow.service.common.SmObjInfo;
import com.supermap.wisdombusiness.workflow.service.wfm.OperationService;
import com.supermap.wisdombusiness.workflow.service.wfm.SmProMaterService;

/**
 * 
 * @Description:远程报件ServicImpl
 * @author 俞学斌
 * @date 2016年4月24日 21:20:12
 */

@Service("batchprojectservice")
public class BatchProjectServicImpl implements BatchProjectService {

	@Autowired
	private CommonDao baseCommonDao;
	
	@Autowired
	private OperationService operationService;

	@Autowired
	private ConstraintCheck constraintcheck;

	@Autowired
	private DYService dyService;

	@Autowired
	private QLService qlService;
	
	@Autowired
	private SmProMaterService promaterService;

	/*****************************************远程报件模块*****************************************/
	/*
	 * XML批量受理信息列表
	 */
	private Map<String, List<HashMap<String, Object>>> listBatchProjectInfo = new HashMap<String, List<HashMap<String, Object>>>();
	
	/*
	 * Excel批量受理信息列表
	 */
	private static HashMap<String,HashMap<String,Object>> listBatchProjectExcelInfo=new HashMap<String, HashMap<String,Object>>();

	/*
	 * 上传xml批量受理压缩包
	 */
	@SuppressWarnings("deprecation")
	@Override
	public Message UpLoadBatchZip(MultipartFile file, HttpServletRequest request) {
		Message msg = new Message();
		msg.setSuccess("false");

		User user = Global.getCurrentUserInfo();
		if (user == null) {
			return msg;
		}
		String strFileName = user.getId();
		String strDir = request.getRealPath("/") + "\\resources\\PDF\\tmp\\";
		deleteFile(strDir + strFileName + ".zip");
		boolean bload = UploadFile(file, request, strDir, strFileName + ".zip");
		boolean bupzip = true;
		if (bload) {
			deleteDirectory(strDir + strFileName);
			bupzip = unZipFiles(strDir + strFileName + ".zip");
		}
		msg.setSuccess(StringHelper.formatObject(bupzip));
		return msg;
	}
	
	/*
	 * 解析xml批量受理信息
	 */
	@SuppressWarnings("deprecation")
	@Override
	public Message AnalysisBatchProject(HttpServletRequest request,String prodef_id) {
		Message msg = new Message();
		msg.setSuccess("false");

		User user = Global.getCurrentUserInfo();
		if (user == null) {
			return msg;
		}
		String strFileName = user.getId();
		String strDir = request.getRealPath("/") + "\\resources\\PDF\\tmp\\";
		List<HashMap<String, Object>> list = ReadDirector(strDir + "\\"
				+ strFileName,prodef_id);
		synchronized (listBatchProjectInfo) {
			if (listBatchProjectInfo.containsKey(strFileName)) {
				listBatchProjectInfo.remove(strFileName);
			}
			listBatchProjectInfo.put(strFileName, list);
		}
		msg.setRows(list);
		msg.setTotal(list.size());
		msg.setSuccess("true");
		return msg;
	}

	/*
	 * 上传文件
	 */
	public boolean UploadFile(MultipartFile file, HttpServletRequest request,
			String strDir, String strFileName) {
		// 创建文件
		File dirPath = new File(strDir);
		if (!dirPath.exists()) {
			dirPath.mkdirs();
		}
		File uploadFile = new File(strDir + strFileName);
		try {
			FileCopyUtils.copy(file.getBytes(), uploadFile);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * 解压文件到指定目录
	 * 
	 * @param zipFile
	 * @param descDir
	 * @author isea533
	 */
	public boolean unZipFiles(String sZipPathFile) {
		int count = -1;
		String savepath = "";
		int buffer = 2048;
		File file = null;
		InputStream is = null;
		FileOutputStream fos = null;
		BufferedOutputStream bos = null;

		savepath = sZipPathFile.substring(0, sZipPathFile.lastIndexOf("."))
				+ File.separator; // 保存解压文件目录
		new File(savepath).mkdir(); // 创建保存目录
		ZipFile zipFile = null;
		try {
			zipFile = new ZipFile(sZipPathFile, "gbk"); // 解决中文乱码问题
			Enumeration<?> entries = zipFile.getEntries();

			while (entries.hasMoreElements()) {
				byte buf[] = new byte[buffer];

				ZipEntry entry = (ZipEntry) entries.nextElement();

				String filename = entry.getName();
				boolean ismkdir = false;
				if (filename.lastIndexOf("/") != -1) { // 检查此文件是否带有文件夹
					ismkdir = true;
				}
				filename = savepath + filename;

				if (entry.isDirectory()) { // 如果是文件夹先创建
					file = new File(filename);
					file.mkdirs();
					continue;
				}
				file = new File(filename);
				if (!file.exists()) { // 如果是目录先创建
					if (ismkdir) {
						new File(filename.substring(0,
								filename.lastIndexOf("/"))).mkdirs(); // 目录先创建
					}
				}
				file.createNewFile(); // 创建文件

				is = zipFile.getInputStream(entry);
				fos = new FileOutputStream(file);
				bos = new BufferedOutputStream(fos, buffer);

				while ((count = is.read(buf)) > -1) {
					bos.write(buf, 0, count);
				}
				bos.flush();
				bos.close();
				fos.close();

				is.close();
			}

			zipFile.close();

		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			try {
				if (bos != null) {
					bos.close();
				}
				if (fos != null) {
					fos.close();
				}
				if (is != null) {
					is.close();
				}
				if (zipFile != null) {
					zipFile.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return true;
	}

	/**
	 * 删除单个文件
	 * 
	 * @param sPath
	 *            被删除文件的文件名
	 * @return 单个文件删除成功返回true，否则返回false
	 */
	public boolean deleteFile(String sPath) {
		boolean flag = false;
		File file = new File(sPath);
		// 路径为文件且不为空则进行删除
		if (file.isFile() && file.exists()) {
			file.delete();
			flag = true;
		}
		return flag;
	}

	/**
	 * 删除目录（文件夹）以及目录下的文件
	 * 
	 * @param sPath
	 *            被删除目录的文件路径
	 * @return 目录删除成功返回true，否则返回false
	 */
	public boolean deleteDirectory(String sPath) {
		// 如果sPath不以文件分隔符结尾，自动添加文件分隔符
		if (!sPath.endsWith(File.separator)) {
			sPath = sPath + File.separator;
		}
		File dirFile = new File(sPath);
		// 如果dir对应的文件不存在，或者不是一个目录，则退出
		if (!dirFile.exists() || !dirFile.isDirectory()) {
			return false;
		}
		boolean flag = true;
		// 删除文件夹下的所有文件(包括子目录)
		File[] files = dirFile.listFiles();
		for (int i = 0; i < files.length; i++) {
			// 删除子文件
			if (files[i].isFile()) {
				flag = deleteFile(files[i].getAbsolutePath());
				if (!flag)
					break;
			} // 删除子目录
			else {
				flag = deleteDirectory(files[i].getAbsolutePath());
				if (!flag)
					break;
			}
		}
		if (!flag)
			return false;
		// 删除当前目录
		if (dirFile.delete()) {
			return true;
		} else {
			return false;
		}
	}
	
	/*
	 * 解析文件夹中批量受理信息
	 */
	private List<HashMap<String, Object>> ReadDirector(String strDir, String prodef_id) {
		Wfd_Prodef prodef=baseCommonDao.get(Wfd_Prodef.class, prodef_id);
		String workflowcode="";
		if(prodef!=null){
			workflowcode=prodef.getProdef_Code();
		}
		List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
		File file = new File(strDir);
		File[] tempList = file.listFiles();
		for (int i = 0; i < tempList.length; i++) {
			File fiel_xm = tempList[i];
			if (fiel_xm.isDirectory()) {
				String strFile = fiel_xm.getPath() + "//登记信息.xml";
				File file_info = new File(strFile);
				if (file_info != null && file_info.exists()) {
					HashMap<String, Object> projectinfo = AnalysisXML(strFile);

					@SuppressWarnings("unchecked")
					List<HashMap<String, String>> list_dy = (List<HashMap<String, String>>) projectinfo
							.get("DYInfo");
					String result = "可受理";
					if (list_dy.size() <= 0) {
						result = "错误：未选择单元!";
					} else {
						List<String> list_bdcdyid = new ArrayList<String>();
						for (HashMap<String, String> dyinfo : list_dy) {
							String bdcdyh = dyinfo.get("BDCDYH");
							if (StringHelper.isEmpty(bdcdyh)) {
								result = "错误：不动产单元号为空!";
								break;
							}
							List<RealUnit> units = UnitTools.loadUnits(
									BDCDYLX.H, DJDYLY.XZ, "BDCDYH='" + bdcdyh
											+ "'");
							if (units == null || units.size() <= 0) {
								result = "错误：单元不存在!";
								break;
							}
							list_bdcdyid.add(units.get(0).getId());
						}
						if (!result.contains("错误")) {
							ResultMessage ms = constraintcheck
									.acceptCheckByBDCDYIDByWorkflowCode(
											StringHelper.formatList(
													list_bdcdyid, ","),
													workflowcode);
							if (("false").equals(ms.getSuccess())) {
								result = "错误：" + ms.getMsg();
							} else if (("warning").equals(ms.getSuccess())) {
								result = "警告：" + ms.getMsg();
							}
						}
					}

					projectinfo.put("DIRECTORY", fiel_xm.getPath());
					projectinfo.put("Result", result);
					list.add(projectinfo);
				}
			}
		}
		return list;
	}
	
	/*
	 * 解析xml项目信息
	 */
	@SuppressWarnings("unchecked")
	private HashMap<String, Object> AnalysisXML(String strXmlPath) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		SAXReader saxReader = new SAXReader();
		Document document = null;
		try {
			document = saxReader.read(new File(strXmlPath));
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		if (document == null) {
			return null;
		}
		// 获取根元素
		Element root = document.getRootElement();
		List<Element> root_elements = root.elements();

		List<Element> unit_elements = new ArrayList<Element>();
		List<Element> ql_elements = root.elements();
		List<Element> ywr_elements = root.elements();

		// 获取项目信息
		HashMap<String, String> xmxx_map = new HashMap<String, String>();
		if (root_elements != null && root_elements.size() > 0) {
			for (Element root_element : root_elements) {
				String property_name = root_element.getName();
				if ("UNITS".endsWith(property_name)) {
					unit_elements = root_element.elements();
				} else if ("QLS".endsWith(property_name)) {
					ql_elements = root_element.elements();
				} else if ("YWRS".endsWith(property_name)) {
					ywr_elements = root_element.elements();
				} else {
					String property_value = root_element.getText();
					if (!xmxx_map.containsKey(property_name)) {
						xmxx_map.put(property_name, property_value);
					}
				}
			}
		}

		// 获取单元信息集合
		List<HashMap<String, String>> list_unit = new ArrayList<HashMap<String, String>>();
		if (unit_elements != null && unit_elements.size() > 0) {
			for (Element unit_element : unit_elements) {
				HashMap<String, String> unit_map = new HashMap<String, String>();
				List<Element> unit_property_elements = unit_element.elements();
				for (Element unit_property_element : unit_property_elements) {
					String property_name = unit_property_element.getName();
					String property_value = unit_property_element.getText();
					if (!unit_map.containsKey(property_name)) {
						unit_map.put(property_name, property_value);
					}
				}
				list_unit.add(unit_map);
			}
		}
		// 获取义务人信息集合
		List<HashMap<String, String>> list_ywr = new ArrayList<HashMap<String, String>>();
		if (ywr_elements != null && ywr_elements.size() > 0) {
			for (Element ywr_element : ywr_elements) {
				HashMap<String, String> ywr_map = new HashMap<String, String>();
				List<Element> ywr_property_elements = ywr_element.elements();
				for (Element ywr_property_element : ywr_property_elements) {
					String property_name = ywr_property_element.getName();
					String property_value = ywr_property_element.getText();
					if (!ywr_map.containsKey(property_name)) {
						ywr_map.put(property_name, property_value);
					}
				}
				list_ywr.add(ywr_map);
			}
		}
		// 获取权利信息集合
		List<HashMap<String, String>> list_ql = new ArrayList<HashMap<String, String>>();
		List<HashMap<String, String>> list_qlr = new ArrayList<HashMap<String, String>>();
		if (ql_elements != null && ql_elements.size() > 0) {
			int iql = 1;
			for (Element ql_element : ql_elements) {
				HashMap<String, String> ql_map = new HashMap<String, String>();
				String QLBSM = StringHelper.formatDouble(iql);
				ql_map.put("BSM", QLBSM);
				List<Element> ql_property_elements = ql_element.elements();
				for (Element ql_property_element : ql_property_elements) {
					String property_name = ql_property_element.getName();
					if ("QLRS".endsWith(property_name)) {
						List<Element> qlr_elements = ql_property_element
								.elements();
						if (qlr_elements != null && qlr_elements.size() > 0) {
							for (Element qlr_element : qlr_elements) {
								HashMap<String, String> qlr_map = new HashMap<String, String>();
								qlr_map.put("QLBSM", QLBSM);
								List<Element> qlr_property_elements = qlr_element
										.elements();
								for (Element qlr_property_element : qlr_property_elements) {
									String qlr_property_name = qlr_property_element
											.getName();
									String qlr_property_value = qlr_property_element
											.getText();
									if (!qlr_map.containsKey(qlr_property_name)) {
										qlr_map.put(qlr_property_name,
												qlr_property_value);
									}
								}
								qlr_map.put("SQRLB", "1");
								list_qlr.add(qlr_map);
							}
						}
					} else if (!ql_map.containsKey(property_name)) {
						String property_value = ql_property_element.getText();
						ql_map.put(property_name, property_value);
					}
				}
				list_ql.add(ql_map);
				iql++;
			}
		}
		map.put("XMInfo", xmxx_map);
		map.put("DYInfo", list_unit);
		map.put("QLInfo", list_ql);
		map.put("QLRInfo", list_qlr);
		map.put("YWRInfo", list_ywr);
		return map;
	}

	/*
	 * 批量受理xml格式项目
	 */
	@SuppressWarnings("deprecation")
	@Override
	public Message AcceptBatchProject(HttpServletRequest request,String prodef_id, HttpServletResponse response) {
		Message msg = new Message();
		msg.setSuccess("false");

		User user = Global.getCurrentUserInfo();
		if (user == null) {
			return msg;
		}
		String strDir = request.getRealPath("/") + "\\resources\\PDF\\tmp\\";
		List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
		synchronized (listBatchProjectInfo) {
			if (listBatchProjectInfo.containsKey(user.getId())) {
				list = listBatchProjectInfo.get(user.getId());
				listBatchProjectInfo.remove(user.getId());

			}
		}
		int i=0;
		if (list != null && list.size() > 0) {
			for (HashMap<String, Object> projectinfo : list) {
				String result = StringHelper.formatObject(projectinfo
						.get("Result"));
				if (!result.contains("错误")) {
					try {
						boolean baccept=AcceptProject(projectinfo, request,prodef_id,response);
						if(baccept){
							i++;
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		deleteFile(strDir + user.getId() + ".zip");
		deleteDirectory(strDir + user.getId());
		msg.setSuccess("true");
		msg.setMsg("受理成功"+i+"个项目！");
		return msg;
	}

	/*
	 * 受理XML格式项目
	 */
	@SuppressWarnings({ "unchecked", "unused"})
	private boolean AcceptProject(HashMap<String, Object> projectinfo,
			HttpServletRequest request,String prodef_id, HttpServletResponse response) throws Exception {
		String staffid = Global.getCurrentUserInfo().getId();
		String batchNumber="PC"+baseCommonDao.CreatMaxID(ConfigHelper.getNameByValue("XZQHDM"), "123456", "BATCHNUM");
		// id :产生流程实例iD
		String id = "";
		///创建项目
		BDCS_XMXX xmxx= CreateProject(request,batchNumber,prodef_id, response);
		HashMap<String, String> xmxx_map = (HashMap<String, String>) projectinfo
				.get("XMInfo");
		if(xmxx==null){
			return false;
		}
		
		List<Wfi_ProInst> list_proinst = baseCommonDao.getDataList(Wfi_ProInst.class, "File_Number='"+xmxx.getPROJECT_ID()+"'");
		if(list_proinst!=null&&list_proinst.size()>0){
			id=list_proinst.get(0).getProinst_Id();
		}
		if(StringHelper.isEmpty(id)){
			return false;
		}
		String XMBH = xmxx.getId();
		if (xmxx_map != null) {
			String XMMC = xmxx_map.get("XMMC");
			String CASENUM = xmxx_map.get("CASENUM");
			String SFHBZS = xmxx_map.get("SFHBZS");

			xmxx.setXMMC(XMMC);
			xmxx.setSFHBZS(SFHBZS);
			baseCommonDao.update(xmxx);
			baseCommonDao.flush();
			List<HashMap<String, String>> list_dy = (List<HashMap<String, String>>) projectinfo
					.get("DYInfo");
			if (list_dy != null && list_dy.size() > 0) {
				for (HashMap<String, String> dy : list_dy) {
					String bdcdyh = dy.get("BDCDYH");
					String bdcdyid = "";
					List<RealUnit> units = UnitTools.loadUnits(BDCDYLX.H,
							DJDYLY.XZ, "BDCDYH='" + bdcdyh + "'");
					if (units == null || units.size() <= 0) {
						continue;
					}
					bdcdyid = units.get(0).getId();
					if (!StringHelper.isEmpty(bdcdyid)) {
						ResultMessage ms = constraintcheck
								.acceptCheckByBDCDYID(bdcdyid, XMBH);
						if (!("false").equals(ms.getSuccess())) {
							dyService.addBDCDYNoCheck(XMBH, bdcdyid);
						}
					}
				}
			}

			List<HashMap<String, String>> list_ywr = (List<HashMap<String, String>>) projectinfo
					.get("YWRInfo");
			List<BDCS_SQR> list_sqr = baseCommonDao.getDataList(BDCS_SQR.class,
					"XMBH='" + XMBH + "' AND SQRLB='2'");
			if (list_sqr != null && list_sqr.size() > 0) {
				for (BDCS_SQR sqr : list_sqr) {
					String sqrxm = sqr.getSQRXM();
					if (list_ywr != null && list_ywr.size() > 0) {
						for (HashMap<String, String> ywr : list_ywr) {
							String ywrmc = ywr.get("SQRXM");
							if (sqrxm.equals(ywrmc)) {
								StringHelper.setValue(ywr, sqr);
								baseCommonDao.update(sqr);
								break;
							}
						}
					}
				}
			}
			baseCommonDao.flush();
			List<HashMap<String, String>> list_qlr = (List<HashMap<String, String>>) projectinfo
					.get("QLRInfo");
			List<String> list_sqrid = new ArrayList<String>();
			if (list_qlr != null && list_qlr.size() > 0) {
				for (HashMap<String, String> qlr : list_qlr) {
					BDCS_SQR sqr = new BDCS_SQR();
					StringHelper.setValue(qlr, sqr);
					String sqrid = SuperHelper.GeneratePrimaryKey();
					list_sqrid.add(sqrid);
					sqr.setId(sqrid);
					sqr.setXMBH(XMBH);
					sqr.setSQRLB("1");
					baseCommonDao.save(sqr);
				}
			}
			baseCommonDao.flush();
			List<HashMap<String, String>> list_ql = (List<HashMap<String, String>>) projectinfo
					.get("QLInfo");
			if (list_ql != null && list_ql.size() > 0) {
				HashMap<String, String> ql = list_ql.get(0);
				List<Rights> rights = RightsTools.loadRightsByCondition(
						DJDYLY.GZ, "XMBH='" + XMBH + "'");
				if (rights != null && rights.size() > 0) {
					for (Rights right : rights) {
						StringHelper.setValue(ql, right);
						right.setCASENUM(CASENUM);
						baseCommonDao.update(right);
						qlService.addQLRfromSQR(XMBH, right.getId(),
								list_sqrid.toArray());
					}
				}
			}
			baseCommonDao.flush();
		}
		String strDir = StringHelper.formatObject(projectinfo.get("DIRECTORY"));
		File file = new File(strDir);
		File[] tempList = file.listFiles();
		String file_Path = request.getSession().getServletContext().getRealPath("/");
		if(tempList!=null&&tempList.length>0){
			for(File attachment:tempList){
				if(attachment.isDirectory()){
					File[] file_images = attachment.listFiles();
					if(file_images==null||file_images.length<=0){
						continue;
					}
					String dirName=attachment.getName();
					for(File file_image:file_images){
						if(file_image.isFile()){
							promaterService.SaveFileToProject(file_image,dirName,id);//功能没调好，先不加，等处理好再加
						}
					}
				}
			}
		}
		return true;
	}

	/*****************************************远程报件模块*****************************************/
	
	/*****************************************Excel批量受理*****************************************/
	/*
	 * 上传批量受理Excel
	 */
	@Override
	public HashMap<String,Object> UpLoadBatchExcel(MultipartFile file,String prodef_id,HttpServletRequest request) {
		HashMap<String,Object> msg = new HashMap<String,Object>();
		msg.put("success", "false");
		User user = Global.getCurrentUserInfo();
		if (user == null) {
			msg.put("msg", "请先登录！");
			return msg;
		}
		Calendar now = Calendar.getInstance();  
		int year=now.get(Calendar.YEAR);
		int month=now.get(Calendar.MONTH) + 1;
		int day=now.get(Calendar.DAY_OF_MONTH);
		String excelDir=ConfigHelper.getNameByValue("BatchExcelPath");
		excelDir="D:\\ttt";
		String excelPath=excelDir+"\\"+year+"\\"+month+"\\"+day+"\\";
		String fileName=file.getOriginalFilename();
    	boolean bUpload=UploadFile(file, request,excelPath,fileName);
    	if(!bUpload){
    		msg.put("msg", "上传文件失败！");
			return msg;
    	}
    	return CheckExcelByProdefID(prodef_id,excelPath+fileName);
	}
	
	/**
	 * 解析Excel
	 * 
	 * @param excelPath excel文件路径
	 * @return excel解析信息
	 */
	private HashMap<String,Object> AnalysisExcel(String excelPath){
		HashMap<String,Object> excelInfo=new HashMap<String, Object>();
		//Sheet页号、页名
		HashMap<Integer,String> sheetInfoList=new HashMap<Integer, String>();
		//Sheet页名、列号、列名
		HashMap<Integer,HashMap<Integer,String>> colInfoList=new HashMap<Integer, HashMap<Integer,String>>();
		//Sheet页名、行号、列号、单元格值
		HashMap<Integer,HashMap<Integer,HashMap<Integer,String>>> cellInfoList=new HashMap<Integer, HashMap<Integer,HashMap<Integer,String>>>();
		InputStream inputStream=null;
		try {
			inputStream=new FileInputStream(excelPath);
			String prefix=excelPath.substring(excelPath.lastIndexOf(".")+1);
			if("XLSX".equals(prefix.toUpperCase())){
				XSSFWorkbook xssfworkbook= new XSSFWorkbook(inputStream);//整个Excel
	        	for(int i=0;i<xssfworkbook.getNumberOfSheets();i++){
	        		XSSFSheet hssfsheet=xssfworkbook.getSheetAt(i);//第I页
	        		String sheetName=hssfsheet.getSheetName();
	        		if(StringHelper.isEmpty(sheetName)){
	        			continue;
	        		}
	        		///列号、列名
	        		HashMap<Integer,String> colInfo=new HashMap<Integer, String>();
	        		//行号、列号、值
	        		HashMap<Integer,HashMap<Integer,String>> cellInfo=new HashMap<Integer,HashMap<Integer,String>>();
	        		for(int j=0;j<=hssfsheet.getLastRowNum();j++){
	        			XSSFRow hssfrow=hssfsheet.getRow(j);//第I页第J行
	        			int mincoll=hssfrow.getFirstCellNum();
	        			int maxcoll=hssfrow.getLastCellNum();
	        			HashMap<Integer,String> rowInfo=new HashMap<Integer,String>();
	        			for(int cellnum=mincoll;cellnum<maxcoll;cellnum++){
	        				XSSFCell cell=hssfrow.getCell(cellnum);//单元格
	        				if(cell==null){
	        					continue;
	        				}
	        				String val="";
	        				if(Cell.CELL_TYPE_STRING==cell.getCellType()){
	        					val=cell.getStringCellValue();
	        				}else if(Cell.CELL_TYPE_NUMERIC==cell.getCellType()){
	        					if (HSSFDateUtil.isCellDateFormatted(cell)) {
	            			        //  如果是date类型则 ，获取该cell的date值   
	        						try{
	        							SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");  
	        							Date dt = HSSFDateUtil.getJavaDate(cell.getNumericCellValue());//获取成DATE类型     
	        							val = dateformat.format(dt);   
	        						}catch(Exception ex){
	        							
	        						}
	        						  
	            			    }else{
	            			    	val = StringHelper.formatDouble(cell.getNumericCellValue());
	            			    }
	        				}else{
	        					cell.setCellType(Cell.CELL_TYPE_STRING);
	        					val=cell.getStringCellValue();
	        				}
	        				
	        				if(j==0){
	        					colInfo.put(cellnum, val);
	        				}else{
	        					rowInfo.put(cellnum, val);
	        				}
	        			}
	        			if(j>0){
	        				cellInfo.put(j, rowInfo);
	        			}
	        		}
	    			cellInfoList.put(i, cellInfo);
	    			colInfoList.put(i, colInfo);
	    			sheetInfoList.put(i, sheetName);
	        	}
			}else if("XLS".equals(prefix.toUpperCase())){
				HSSFWorkbook hssfWorkbook = new HSSFWorkbook(inputStream);
	        	for(int i=0;i<hssfWorkbook.getNumberOfSheets();i++){
	        		HSSFSheet hssfsheet=hssfWorkbook.getSheetAt(i);//第I页
	        		String sheetName=hssfsheet.getSheetName();
	        		if(StringHelper.isEmpty(sheetName)){
	        			continue;
	        		}
	        		///列号、列名
	        		HashMap<Integer,String> colInfo=new HashMap<Integer, String>();
	        		//行号、列号、值
	        		HashMap<Integer,HashMap<Integer,String>> cellInfo=new HashMap<Integer,HashMap<Integer,String>>();
	        		for(int j=0;j<=hssfsheet.getLastRowNum();j++){
	        			HSSFRow hssfrow=hssfsheet.getRow(j);//第I页第J行
	        			int mincoll=hssfrow.getFirstCellNum();
	        			int maxcoll=hssfrow.getLastCellNum();
	        			HashMap<Integer,String> rowInfo=new HashMap<Integer,String>();
	        			for(int cellnum=mincoll;cellnum<maxcoll;cellnum++){
	        				HSSFCell cell=hssfrow.getCell(cellnum);//单元格
	        				if(cell==null){
	        					continue;
	        				}
	        				String val="";
	        				if(Cell.CELL_TYPE_STRING==cell.getCellType()){
	        					val=cell.getStringCellValue();
	        				}else if(Cell.CELL_TYPE_NUMERIC==cell.getCellType()){
	        					if (HSSFDateUtil.isCellDateFormatted(cell)) {
	            			        //  如果是date类型则 ，获取该cell的date值   
	        						try{
	        							SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");  
	        							Date dt = HSSFDateUtil.getJavaDate(cell.getNumericCellValue());//获取成DATE类型     
	        							val = dateformat.format(dt);   
	        						}catch(Exception ex){
	        							
	        						}
	        						  
	            			    }else{
	            			    	val = StringHelper.formatDouble(cell.getNumericCellValue());
	            			    }
	        				}else{
	        					cell.setCellType(Cell.CELL_TYPE_STRING);
	        					val=cell.getStringCellValue();
	        				}
	        				
	        				if(j==0){
	        					colInfo.put(cellnum, val);
	        				}else{
	        					rowInfo.put(cellnum, val);
	        				}
	        			}
	        			if(j>0){
	        				cellInfo.put(j, rowInfo);
	        			}
	        		}
	    			cellInfoList.put(i, cellInfo);
	    			colInfoList.put(i, colInfo);
	    			sheetInfoList.put(i, sheetName);
	        	}
			}
			
        	inputStream.close(); 
		} catch (Exception e) {
			if(inputStream!=null){
				try {
					inputStream.close();
				} catch (IOException e1) {
				} 
			}
			e.printStackTrace();
		}
		excelInfo.put("cellinfolist", cellInfoList);
		excelInfo.put("colinfolist", colInfoList);
		excelInfo.put("sheetInfoList", sheetInfoList);
		return excelInfo;
	}
	
	/**
	 * 检查Excel信息
	 * 
	 * @param excelPath excel文件路径
	 * @return excel解析信息
	 */
	@SuppressWarnings("unchecked")
	private HashMap<String,Object> CheckExcelByProdefID(String prodef_id,String excelPath){
		HashMap<String,Object> result=new HashMap<String, Object>();
		Wfd_Prodef prodef=baseCommonDao.get(Wfd_Prodef.class, prodef_id);
		if(prodef==null){
			result.put("msg", "流程解析错误！");
			result.put("success", "false");
			return result;
		}
		String prodefCode=prodef.getProdef_Code();
		if(StringHelper.isEmpty(prodefCode)){
			result.put("msg", "流程解析错误！");
			result.put("success", "false");
			return result;
		}
		List<WFD_MAPPING> mps=baseCommonDao.getDataList(WFD_MAPPING.class, "workflowcode='"+prodefCode+"'");
		if(mps==null||mps.size()!=1){
			result.put("msg", "流程配置错误！");
			result.put("success", "false");
			return result;
		}
		String baseWorkflowName=mps.get(0).getWORKFLOWNAME();
		if("ZY002".equals(baseWorkflowName)||"ZY102".equals(baseWorkflowName)
				||"YG002".equals(baseWorkflowName)||"YG102".equals(baseWorkflowName)
				||"CS011".equals(baseWorkflowName)||"CS013".equals(baseWorkflowName)
				||"ZY007".equals(baseWorkflowName)||"YG001".equals(baseWorkflowName)
				||"YG003".equals(baseWorkflowName)){
			HashMap<String,Object> excelInfo=AnalysisExcelEx(excelPath);
			result=CheckExcelByBaseWorkflowNameEx(excelInfo,baseWorkflowName,prodefCode);
			if("true".equals(result.get("success"))){
				List<HashMap<String,String>> checkAccept=(List<HashMap<String,String>>) result.get("msg");
				excelInfo.put("checkInfo", checkAccept);
				String excelid=SuperHelper.GeneratePrimaryKey();
				//HashMap<String,Object> parsingExcelinfo=ParsingExcelInfoEx(checkAccept,excelInfo);
				listBatchProjectExcelInfo.put(excelid, excelInfo);
				result.put("excelid", excelid);
			}
		}else{
			HashMap<String,Object> excelInfo=AnalysisExcel(excelPath);
			result=CheckExcelByBaseWorkflowName(excelInfo,baseWorkflowName,prodefCode);
			if("true".equals(result.get("success"))){
				List<HashMap<String,String>> checkAccept=(List<HashMap<String,String>>) result.get("msg");
				excelInfo.put("checkInfo", checkAccept);
				String excelid=SuperHelper.GeneratePrimaryKey();
				HashMap<String,Object> parsingExcelinfo=ParsingExcelInfo(checkAccept,excelInfo);
				listBatchProjectExcelInfo.put(excelid, parsingExcelinfo);
				result.put("excelid", excelid);
			}
		}
		
		return result;
	}
	
	/*
	 * 格式化批量受理项目信息
	 */
	@SuppressWarnings("unchecked")
	private HashMap<String,Object> ParsingExcelInfo(List<HashMap<String,String>> checkAccept,HashMap<String,Object> excelInfo){
		HashMap<String,Object> parsingExcelinfo=new HashMap<String, Object>();
		//项目标识列表
		List<String> xmidlist=new ArrayList<String>();
		HashMap<String,HashMap<String,String>> xmid_xminfo=new HashMap<String,HashMap<String,String>>();
		//项目标识和不动产单元id列表关系
		HashMap<String,List<String>> xmid_dyidlist=new HashMap<String, List<String>>();
		//项目标识下不动产单元id和不动产单元号关系
		HashMap<String,HashMap<String,String>> xmid_dyid_dyh=new HashMap<String,HashMap<String,String>>();
		//项目标识下权利id和抵押权证明号关系
		HashMap<String,HashMap<String,String>> xmid_qlid_zmh=new HashMap<String,HashMap<String,String>>();
		//Sheet页号、页名
		HashMap<Integer,String> sheetInfoList=(HashMap<Integer,String>)excelInfo.get("sheetInfoList");
		for(HashMap<String,String> acceptinfo:checkAccept){
			HashMap<String,String> xminfo=new HashMap<String, String>();
			String status=acceptinfo.get("状态");
			String xmid=acceptinfo.get("项目标识");
			String bdcdyid=acceptinfo.get("BDCDYID");
			String xmmc=acceptinfo.get("项目名称");
			String bdcdyh="";
			String dyqzmh = "";
			if (sheetInfoList.containsValue("项目信息")) {
				dyqzmh = acceptinfo.get("抵押权证明号");
				String zxyi = acceptinfo.get("注销原因");
				String zxfj = acceptinfo.get("注销附记");
				xminfo.put("dyqzmh", dyqzmh);
				xminfo.put("zxyi", zxyi);
				xminfo.put("zxfj", zxfj);
			}else {
				bdcdyh=acceptinfo.get("不动产单元号");
				String sfhbfz=acceptinfo.get("是否多个单元一本证");
				if("是".equals(sfhbfz)){
					sfhbfz="1";
				}else if("否".equals(sfhbfz)){
					sfhbfz="0";
				}
				xminfo.put("sfhbfz", sfhbfz);
			}
			xminfo.put("xmid", xmid);
			xminfo.put("xmmc", xmmc);
			if("严重".equals(status)){
				continue;
			}
			if(StringHelper.isEmpty(xmid)){
				continue;
			}
			if(StringHelper.isEmpty(bdcdyid)){
				continue;
			}
			if(xmid_dyid_dyh.containsKey(xmid)){
				HashMap<String,String> dyid_dyh=xmid_dyid_dyh.get(xmid);
				if(!dyid_dyh.containsKey(bdcdyid)){
					dyid_dyh.put(bdcdyid,bdcdyh);
					xmid_dyid_dyh.put(xmid, dyid_dyh);
				}
			}else{
				HashMap<String,String> dyid_dyh=new HashMap<String,String>();
				dyid_dyh.put(bdcdyid,bdcdyh);
				xmid_dyid_dyh.put(xmid, dyid_dyh);
			}
			
			if(xmid_qlid_zmh.containsKey(xmid)){
				HashMap<String,String> qlid_zmh=xmid_qlid_zmh.get(xmid);
				if(!qlid_zmh.containsKey(bdcdyid)){
					qlid_zmh.put(bdcdyid,dyqzmh);
					xmid_qlid_zmh.put(xmid, qlid_zmh);
				}
			}else{
				HashMap<String,String> qlid_zmh=new HashMap<String,String>();
				qlid_zmh.put(bdcdyid,dyqzmh);
				xmid_qlid_zmh.put(xmid, qlid_zmh);
			}
			
			if(xmid_dyidlist.containsKey(xmid)){
				List<String> dyidlist=xmid_dyidlist.get(xmid);
				if(!dyidlist.contains(bdcdyid)){
					dyidlist.add(bdcdyid);
					xmid_dyidlist.put(xmid, dyidlist);
				}
			}else{
				List<String> dyidlist=new ArrayList<String>();
				dyidlist.add(bdcdyid);
				xmid_dyidlist.put(xmid, dyidlist);
			}
			
			if(xmidlist.contains(xmid)){
				continue;
			}
			xmidlist.add(xmid);
			if(!xmid_xminfo.containsKey(xmid)){
				xmid_xminfo.put(xmid, xminfo);
			}
		}
		parsingExcelinfo.put("xmidlist", xmidlist);
		
		//Sheet页名、列号、列名
		HashMap<Integer,HashMap<Integer,String>> colInfoList=(HashMap<Integer,HashMap<Integer,String>>)excelInfo.get("colinfolist");
		//Sheet页名、行号、列号、单元格值
		HashMap<Integer,HashMap<Integer,HashMap<Integer,String>>> cellInfoList=(HashMap<Integer,HashMap<Integer,HashMap<Integer,String>>>)excelInfo.get("cellinfolist");
		
		
		for(String xmid:xmidlist){
			///项目信息
			HashMap<String,Object> xminfo=new HashMap<String, Object>();
			xminfo.put("dyidlist", xmid_dyidlist.get(xmid));
			xminfo.put("dyid_dyh", xmid_dyid_dyh.get(xmid));
			xminfo.put("qlid_zmh", xmid_qlid_zmh.get(xmid));
			//单元号和权利信息关系
			HashMap<String,HashMap<String,Object>> dyh_qlinfo=new HashMap<String, HashMap<String,Object>>();
			//单元号和抵押权信息关系
			HashMap<String,HashMap<String,Object>> dyh_dyqinfo=new HashMap<String, HashMap<String,Object>>();
			//申请人id列表
			List<String> sqridlist=new ArrayList<String>();
			//申请人id和申请人信息关系
			HashMap<String,HashMap<String,Object>> sqrid_sqrxx=new HashMap<String, HashMap<String,Object>>();
			//单元号和权利人申请人id关系
			HashMap<String,List<String>> dyh_qlrids=new HashMap<String, List<String>>();
			//单元号和抵押权人申请人id关系
			HashMap<String,List<String>> dyh_dyqrids=new HashMap<String, List<String>>();
			
			List<String> xm_dyhlist=Arrays.asList(xmid_dyid_dyh.get(xmid).values().toArray(new String[xmid_dyid_dyh.get(xmid).values().size()]));
			
			HashMap<String,String> ywrmc_sqrid=new HashMap<String, String>();
			HashMap<String,String> qlrmc_sqrid=new HashMap<String, String>();
			for(Entry<Integer,String> sheetInfoEntry : sheetInfoList.entrySet()){
				Integer sheetNum=sheetInfoEntry.getKey();
				String sheetName=sheetInfoEntry.getValue();
				HashMap<Integer,String> colInfo=colInfoList.get(sheetNum);
				Integer colNum_bdcdyh=-1;
				for(Entry<Integer,String> colInfoEntry : colInfo.entrySet()){
					Integer colNum=colInfoEntry.getKey();
					String colName=colInfoEntry.getValue();
					if("不动产单元号".equals(colName)){
						colNum_bdcdyh=colNum;
						break;
					}
				}
				HashMap<Integer,HashMap<Integer,String>> cellInfo=cellInfoList.get(sheetNum);
				if("商品房预告登记信息".equals(sheetName)||"国有建设用地使用权-房屋所有权登记信息".equals(sheetName)){
					for(Entry<Integer,HashMap<Integer,String>> rowInfoEntry : cellInfo.entrySet()){
						HashMap<Integer,String> rowInfo=rowInfoEntry.getValue();
						List<String> bdcdyhlist=Arrays.asList(rowInfo.get(colNum_bdcdyh).split(";"));
						for(String dyh:xm_dyhlist){
							if(dyh_qlinfo.containsKey(dyh)){
								continue;
							}
							if(bdcdyhlist.contains(dyh)){
								HashMap<String,Object> qlinfo=new HashMap<String, Object>();
								for(Entry<Integer,String> colInfoEntry : colInfo.entrySet()){
									Integer colNum=colInfoEntry.getKey();
									String colName=colInfoEntry.getValue();
									String fieldName=GetFieldName(sheetName,colName);
									Object fieldValue=GetConstValue(colName,rowInfo.get(colNum));
									qlinfo.put(fieldName, fieldValue);
								}
								dyh_qlinfo.put(dyh, qlinfo);
							}
						}
					}
				}else if("抵押权预告登记信息".equals(sheetName)||"抵押权登记信息".equals(sheetName)||"在建工程抵押登记信息".equals(sheetName)){
					for(Entry<Integer,HashMap<Integer,String>> rowInfoEntry : cellInfo.entrySet()){
						HashMap<Integer,String> rowInfo=rowInfoEntry.getValue();
						List<String> bdcdyhlist=Arrays.asList(rowInfo.get(colNum_bdcdyh).split(";"));
						for(String dyh:xm_dyhlist){
							if(dyh_dyqinfo.containsKey(dyh)){
								continue;
							}
							if(bdcdyhlist.contains(dyh)){
								HashMap<String,Object> dyqinfo=new HashMap<String, Object>();
								for(Entry<Integer,String> colInfoEntry : colInfo.entrySet()){
									Integer colNum=colInfoEntry.getKey();
									String colName=colInfoEntry.getValue();
									String fieldName=GetFieldName(sheetName,colName);
									Object fieldValue=GetConstValue(colName,rowInfo.get(colNum));
									dyqinfo.put(fieldName, fieldValue);
								}
								dyh_dyqinfo.put(dyh, dyqinfo);
							}
						}
					}
				}else if("商品房预告登记义务人".equals(sheetName)||"抵押权登记义务人".equals(sheetName)||"国有建设用地使用权-房屋所有权义务人".equals(sheetName)
						||"在建工程抵押登记义务人".equals(sheetName)){
					for(Entry<Integer,HashMap<Integer,String>> rowInfoEntry : cellInfo.entrySet()){
						HashMap<Integer,String> rowInfo=rowInfoEntry.getValue();
						List<String> bdcdyhlist=Arrays.asList(rowInfo.get(colNum_bdcdyh).split(";"));
						for(String dyh:xm_dyhlist){
							if(bdcdyhlist.contains(dyh)){
								HashMap<String,Object> sqrinfo=new HashMap<String, Object>();
								for(Entry<Integer,String> colInfoEntry : colInfo.entrySet()){
									Integer colNum=colInfoEntry.getKey();
									String colName=colInfoEntry.getValue();
									String fieldName=GetFieldName(sheetName,colName);
									Object fieldValue=GetConstValue(colName,rowInfo.get(colNum));
									sqrinfo.put(fieldName, fieldValue);
								}
								sqrinfo.put("SQRLB", "2");
								String sqrid=SuperHelper.GeneratePrimaryKey();
								if(!ywrmc_sqrid.containsKey(sqrinfo.get("SQRXM")+"----"+sqrinfo.get("ZJH"))){
									ywrmc_sqrid.put(sqrinfo.get("SQRXM")+"----"+sqrinfo.get("ZJH"), sqrid);
									sqridlist.add(sqrid);
									sqrid_sqrxx.put(sqrid, sqrinfo);
								}
							}
						}
					}
				}else if("商品房预告登记权利人".equals(sheetName)||"国有建设用地使用权-房屋所有权权利人".equals(sheetName)){
					for(Entry<Integer,HashMap<Integer,String>> rowInfoEntry : cellInfo.entrySet()){
						HashMap<Integer,String> rowInfo=rowInfoEntry.getValue();
						List<String> bdcdyhlist=Arrays.asList(rowInfo.get(colNum_bdcdyh).split(";"));
						for(String dyh:xm_dyhlist){
							if(bdcdyhlist.contains(dyh)){
								HashMap<String,Object> sqrinfo=new HashMap<String, Object>();
								for(Entry<Integer,String> colInfoEntry : colInfo.entrySet()){
									Integer colNum=colInfoEntry.getKey();
									String colName=colInfoEntry.getValue();
									String fieldName=GetFieldName(sheetName,colName);
									Object fieldValue=GetConstValue(colName,rowInfo.get(colNum));
									sqrinfo.put(fieldName, fieldValue);
								}
								sqrinfo.put("SQRLB", "1");
								String sqrid=SuperHelper.GeneratePrimaryKey();
								if(!qlrmc_sqrid.containsKey(sqrinfo.get("SQRXM")+"----"+sqrinfo.get("ZJH"))){
									qlrmc_sqrid.put(sqrinfo.get("SQRXM")+"----"+sqrinfo.get("ZJH"), sqrid);
									sqridlist.add(sqrid);
									sqrid_sqrxx.put(sqrid, sqrinfo);
								}else{
									sqrid=qlrmc_sqrid.get(sqrinfo.get("SQRXM")+"----"+sqrinfo.get("ZJH"));
								}
								if(dyh_qlrids.containsKey(dyh)){
									List<String> qlrlist=dyh_qlrids.get(dyh);
									if(!qlrlist.contains(sqrid)){
										qlrlist.add(sqrid);
										dyh_qlrids.put(dyh, qlrlist);
									}
								}else{
									List<String> qlrlist=new ArrayList<String>();
									qlrlist.add(sqrid);
									dyh_qlrids.put(dyh, qlrlist);
								}
							}
						}
					}
				}else if("抵押权预告登记权利人".equals(sheetName)||"抵押权登记权利人".equals(sheetName)||"在建工程抵押登记权利人".equals(sheetName)){
					for(Entry<Integer,HashMap<Integer,String>> rowInfoEntry : cellInfo.entrySet()){
						HashMap<Integer,String> rowInfo=rowInfoEntry.getValue();
						List<String> bdcdyhlist=Arrays.asList(rowInfo.get(colNum_bdcdyh).split(";"));
						for(String dyh:xm_dyhlist){
							if(bdcdyhlist.contains(dyh)){
								HashMap<String,Object> sqrinfo=new HashMap<String, Object>();
								for(Entry<Integer,String> colInfoEntry : colInfo.entrySet()){
									Integer colNum=colInfoEntry.getKey();
									String colName=colInfoEntry.getValue();
									String fieldName=GetFieldName(sheetName,colName);
									Object fieldValue=GetConstValue(colName,rowInfo.get(colNum));
									sqrinfo.put(fieldName, fieldValue);
								}
								sqrinfo.put("SQRLB", "1");
								String sqrid=SuperHelper.GeneratePrimaryKey();
								if(!qlrmc_sqrid.containsKey(sqrinfo.get("SQRXM")+"----"+sqrinfo.get("ZJH"))){
									qlrmc_sqrid.put(sqrinfo.get("SQRXM")+"----"+sqrinfo.get("ZJH"), sqrid);
									sqridlist.add(sqrid);
									sqrid_sqrxx.put(sqrid, sqrinfo);
								}else{
									sqrid=qlrmc_sqrid.get(sqrinfo.get("SQRXM")+"----"+sqrinfo.get("ZJH"));
								}
								if(dyh_dyqrids.containsKey(dyh)){
									List<String> qlrlist=dyh_dyqrids.get(dyh);
									if(!qlrlist.contains(sqrid)){
										qlrlist.add(sqrid);
										dyh_dyqrids.put(dyh, qlrlist);
									}
								}else{
									List<String> qlrlist=new ArrayList<String>();
									qlrlist.add(sqrid);
									dyh_dyqrids.put(dyh, qlrlist);
								}
							}
						}
					}
				}
			}
			xminfo.put("info", xmid_xminfo.get(xmid));
			xminfo.put("dyh_qlinfo", dyh_qlinfo);
			xminfo.put("dyh_dyqinfo", dyh_dyqinfo);
			xminfo.put("sqridlist", sqridlist);
			xminfo.put("sqrid_sqrxx", sqrid_sqrxx);
			xminfo.put("dyh_qlrids", dyh_qlrids);
			xminfo.put("dyh_dyqrids", dyh_dyqrids);
			parsingExcelinfo.put(xmid, xminfo);
		}
		return parsingExcelinfo;
	}
	
	/*
	 * 批量受理Excel属性映射
	 */
	private String GetFieldName(String sheetName,String colName){
		String fieldName=colName;
		if("商品房预告登记信息".equals(sheetName)||"国有建设用地使用权-房屋所有权登记信息".equals(sheetName)){
			if("权利起始时间".equals(colName)){
				fieldName="QLQSSJ";
			}else if("权利结束时间".equals(colName)){
				fieldName="QLJSSJ";
			}else if("取得价格".equals(colName)){
				fieldName="QDJG";
			}else if("登记原因".equals(colName)){
				fieldName="DJYY";
			}else if("附记".equals(colName)){
				fieldName="FJ";
			}else if("持证方式".equals(colName)){
				fieldName="CZFS";
			}
		}else if("抵押权预告登记信息".equals(sheetName)||"抵押权登记信息".equals(sheetName)||"在建工程抵押登记信息".equals(sheetName)){
			if("债权起始时间".equals(colName)){
				fieldName="QLQSSJ";
			}else if("债权结束时间".equals(colName)){
				fieldName="QLJSSJ";
			}else if("抵押方式".equals(colName)){
				fieldName="DYFS";
			}else if("被担保债权数额(最高债权数额)".equals(colName)){
				fieldName="ZQSE";
			}else if("最高债权确定事实".equals(colName)){
				fieldName="ZGZQQDSS";
			}else if("债权单位".equals(colName)){
				fieldName="ZQDW";
			}else if("抵押评估价值".equals(colName)){
				fieldName="DYPGJZ";
			}else if("在建建筑物坐落".equals(colName)){
				fieldName="ZJJZWZL";
			}else if("在建建筑物抵押范围".equals(colName)){
				fieldName="ZJJZWDYFW";
			}else if("登记原因".equals(colName)){
				fieldName="DJYY";
			}else if("附记".equals(colName)){
				fieldName="FJ";
			}
			if("在建工程抵押登记信息".equals(sheetName)){
				if("抵押人".equals(colName)){
					fieldName="DYR";
				}
			}
		}else if("商品房预告登记义务人".equals(sheetName)||"商品房预告登记权利人".equals(sheetName)||"抵押权预告登记权利人".equals(sheetName)
				||"国有建设用地使用权-房屋所有权义务人".equals(sheetName)||"国有建设用地使用权-房屋所有权权利人".equals(sheetName)
				||"抵押权登记义务人".equals(sheetName)||"抵押权登记权利人".equals(sheetName)
				||"在建工程抵押登记义务人".equals(sheetName)||"在建工程抵押登记权利人".equals(sheetName)){
			if("权利人名称".equals(colName)){
				fieldName="SQRXM";
			}else if("证件类型".equals(colName)){
				fieldName="ZJLX";
			}else if("证件号".equals(colName)){
				fieldName="ZJH";
			}else if("性别".equals(colName)){
				fieldName="XB";
			}else if("权利人类型".equals(colName)){
				fieldName="SQRLX";
			}else if("共有方式".equals(colName)){
				fieldName="GYFS";
			}else if("权利比例".equals(colName)){
				fieldName="QLBL";
			}else if("通讯地址".equals(colName)){
				fieldName="TXDZ";
			}else if("联系电话".equals(colName)){
				fieldName="LXDH";
			}else if("顺序号".equals(colName)){
				fieldName="SXH";
			}else if("法定代表人".equals(colName)){
				fieldName="FDDBR";
			}else if("法定代表人证件类型".equals(colName)){
				fieldName="FDDBRZJLX";
			}else if("法定代表人证件号".equals(colName)){
				fieldName="FDDBRZJH";
			}else if("法定代表人电话".equals(colName)){
				fieldName="FDDBRDH";
			}else if("代理人姓名".equals(colName)){
				fieldName="DLRXM";
			}else if("代理人证件类型".equals(colName)){
				fieldName="DLRZJLX";
			}else if("代理人证件号码".equals(colName)){
				fieldName="DLRZJHM";
			}else if("代理人联系电话".equals(colName)){
				fieldName="DLRLXDH";
			}else if("是否持证人".equals(colName)){
				fieldName="SFCZR";
			}
		}
		return fieldName;
	}
	
	/*
	 * 批量受理Excel字典映射
	 */
	private Object GetConstValue(String colName,String value){
		Object contValue=value;
		if("权利人类型".equals(colName)||"QLRLX".equals(colName)||"SQRLX".equals(colName)){
			if("个人".equals(value)){
				contValue="1";
			}else if("企业".equals(value)){
				contValue="2";
			}else if("事业单位".equals(value)){
				contValue="3";
			}else if("国家机关".equals(value)){
				contValue="4";
			}else if("其它".equals(value)){
				contValue="99";
			}
		}else if("性别".equals(colName)||"XB".equals(colName)){
			if("男性".equals(value)){
				contValue="1";
			}else if("女性".equals(value)){
				contValue="2";
			}else if("不详".equals(value)){
				contValue="3";
			}else if("男".equals(value)){
				contValue="1";
			}else if("女".equals(value)){
				contValue="2";
			}else if("不详".equals(value)){
				contValue="3";
			}
		}else if("证件类型".equals(colName)||"法定代表人证件类型".equals(colName)||"代理人证件类型".equals(colName)||"ZJLX".equals(colName)||"DLRZJLX".equals(colName)){
			if("身份证".equals(value)){
				contValue="1";
			}else if("港澳台身份证".equals(value)){
				contValue="2";
			}else if("护照".equals(value)){
				contValue="3";
			}else if("户口簿".equals(value)){
				contValue="4";
			}else if("军官证（士兵证）".equals(value)){
				contValue="5";
			}else if("组织机构代码".equals(value)){
				contValue="6";
			}else if("营业执照".equals(value)){
				contValue="7";
			}else if("其它".equals(value)){
				contValue="99";
			}
		}else if("共有方式".equals(colName)||"GYFS".equals(colName)){
			if("单独所有".equals(value)){
				contValue="0";
			}else if("共同共有".equals(value)){
				contValue="1";
			}else if("按份共有".equals(value)){
				contValue="2";
			}else if("其它共有".equals(value)){
				contValue="3";
			}
		}else if("是否持证人".equals(colName)||"SFCZR".equals(colName)){
			if("是".equals(value)){
				contValue="1";
			}else if("否".equals(value)){
				contValue="0";
			}
		}else if("债权单位".equals(colName)||"ZQDW".equals(colName)){
			if("元".equals(value)){
				contValue="1";
			}else if("万元".equals(value)){
				contValue="2";
			}
		}else if("持证方式".equals(colName)||"CZFS".equals(colName)){
			if("共同持证".equals(value)){
				contValue="01";
			}else if("分别持证".equals(value)){
				contValue="02";
			}
		}else if("抵押方式".equals(colName)||"DYFS".equals(colName)){
			if("一般抵押权".equals(value)){
				contValue="1";
			}else if("最高额抵押".equals(value)){
				contValue="2";
			}
		}
		return contValue;
	}
	
	/**
	 * 检查Excel信息
	 * 
	 * @param excelPath excel文件路径
	 * @return excel解析信息
	 */
	private HashMap<String,Object> CheckExcelByBaseWorkflowName(HashMap<String,Object> excelInfo,String baseWorkflowName,String workflowCode){
		HashMap<String,Object> result=new HashMap<String, Object>();
		result=CheckExcelIntegrity(excelInfo,workflowCode,baseWorkflowName);
		if(!"true".equals(result.get("success"))){
			return result;
		}
		return result;
	}
	
	/*
	 * 业务流程校验
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private HashMap<String,Object> CheckExcelIntegrity(HashMap<String,Object> excelInfo,String workflowCode,String baseWorkflowName){
		HashMap<String,Object> result=new HashMap<String, Object>();
		//Sheet页号、页名
		HashMap<Integer,String> sheetInfoList=(HashMap<Integer, String>)excelInfo.get("sheetInfoList");
		//Sheet页名、列号、列名
		HashMap<Integer,HashMap<Integer,String>> colInfoList=(HashMap<Integer, HashMap<Integer,String>>)excelInfo.get("colinfolist");
		//Sheet页名、行号、列号、单元格值
		HashMap<Integer,HashMap<Integer,HashMap<Integer,String>>> cellInfoList=(HashMap<Integer, HashMap<Integer,HashMap<Integer,String>>>)excelInfo.get("cellinfolist");
		//检查缺失Sheet页
		List<String> sheetRequired=GetRequiredInfo(baseWorkflowName,false);
		List<String> checkSheet=CheckReuired(sheetRequired,sheetInfoList);
		if(checkSheet.size()>0){
			result.put("success", "false");
			result.put("msg", "缺失"+StringHelper.formatList(checkSheet, "、")+"信息");
			return result;
		}
		
		List<String> checkCols=new ArrayList<String>();
		Set<Integer> setSheet = sheetInfoList.keySet();
		Iterator<Integer> iteratorSheet = setSheet.iterator();
		while (iteratorSheet.hasNext()) {
			Integer sheetNum = iteratorSheet.next();
			String sheetName = sheetInfoList.get(sheetNum);
			HashMap<Integer,String> colsInfo=colInfoList.get(sheetNum);
			if(sheetRequired.contains(sheetName)){
				List<String> colsRequired=GetRequiredInfo(sheetName,false);
				List<String> loseCols=CheckReuired(colsRequired,colsInfo);
				if(loseCols.size()>0){
					checkCols.add(sheetName+"中缺失信息："+StringHelper.formatList(loseCols, "、")+"");
				}
			}
		}
		if(checkCols.size()>0){
			result.put("success", "false");
			result.put("msg", StringHelper.formatList(checkCols, ";")+"");
			return result;
		}
		List<String> checkValues=new ArrayList<String>();
		Set<Integer> setSheet2 = sheetInfoList.keySet();
		Iterator<Integer> iteratorSheet2 = setSheet2.iterator();
		while (iteratorSheet2.hasNext()) {
			Integer sheetNum = iteratorSheet2.next();
			String sheetName = sheetInfoList.get(sheetNum);
			List<String> colsRequired=GetRequiredInfo(sheetName,true);
			List<String> loseValues=CheckValueReuired(colsRequired,colInfoList.get(sheetNum),cellInfoList.get(sheetNum));
			if(loseValues.size()>0){
				checkValues.add(sheetName+"中："+StringHelper.formatList(loseValues, ";"));
			}
		}
		if(checkValues.size()>0){
			result.put("success", "false");
			result.put("msg", "必填值校验："+StringHelper.formatList(checkValues, ";")+"");
			return result;
		}
		List<String> sheetRelationRequired=GetRequiredInfo(baseWorkflowName,true);
		HashMap<Integer,HashMap<Integer,String>> mainCellInfo=new HashMap<Integer, HashMap<Integer,String>>();
		HashMap<Integer,String> mainColInfo=new HashMap<Integer, String>();
		List<String> checkRelation = new ArrayList<String>();
		if (!"ZX004".equals(baseWorkflowName) && !"ZX006".equals(baseWorkflowName) && !"ZX009".equals(baseWorkflowName)) {
			checkRelation=CheckRelationReuired(mainCellInfo,mainColInfo,sheetRelationRequired,"项目单元信息",sheetRequired,sheetInfoList,colInfoList,cellInfoList);
		}else {
			Set<Integer> setSheet3 = sheetInfoList.keySet();
			Iterator<Integer> iteratorSheet3 = setSheet3.iterator();
			while (iteratorSheet3.hasNext()) {
				Integer sheetNum = iteratorSheet3.next();
				mainCellInfo.putAll(cellInfoList.get(sheetNum));
				mainColInfo.putAll(colInfoList.get(sheetNum));
			}
		}
		if(checkRelation.size()>0){
			result.put("success", "false");
			result.put("msg", "关联性校验："+StringHelper.formatList(checkRelation, ";")+"");
			return result;
		}
		List<HashMap<String,String>> checkAccept=new ArrayList<HashMap<String,String>>();
		for(HashMap<Integer,String> rowInfo:mainCellInfo.values()){
			HashMap<String,String> detailInfo=new HashMap<String, String>();
			Set<Integer> setCol = mainColInfo.keySet();
			Iterator<Integer> iteratorCol = setCol.iterator();
			while (iteratorCol.hasNext()) {
				Integer colNum = iteratorCol.next();
				String colName = mainColInfo.get(colNum);
				detailInfo.put(colName, rowInfo.get(colNum));
				if("不动产单元号".equals(colName)){
					RegisterWorkFlow baseWorkflow=HandlerFactory.getMapping().getWorkflowByName(baseWorkflowName);
					List<RealUnit> listUnit=UnitTools.loadUnits(BDCDYLX.initFrom(baseWorkflow.getUnittype()), DJDYLY.XZ, "BDCDYH='"+rowInfo.get(colNum)+"'");
					if(listUnit==null||listUnit.size()<=0){
						detailInfo.put("BDCDYID", "");
						detailInfo.put("状态", "严重");
						detailInfo.put("说明", "单元不存在！");
					}else if(listUnit.size()>1){
						detailInfo.put("BDCDYID", "");
						detailInfo.put("状态", "严重");
						detailInfo.put("说明", "存在多个单元！");
					}else{
						ResultMessage ms =constraintcheck.acceptCheckByBDCDYIDByWorkflowCode(listUnit.get(0).getId(),workflowCode);
						if (("false").equals(ms.getSuccess())) {
							detailInfo.put("状态", "严重"); 
							detailInfo.put("说明", ms.getMsg());
						} else if (("warning").equals(ms.getSuccess())) {
							detailInfo.put("状态", "警告");
							detailInfo.put("说明", ms.getMsg());
						}else{
							detailInfo.put("状态", "校验通过");
							detailInfo.put("说明", "可受理");
						}
						detailInfo.put("BDCDYID", listUnit.get(0).getId());
					}
				}else if("抵押权证明号".equals(colName)){
					RegisterWorkFlow baseWorkflow=HandlerFactory.getMapping().getWorkflowByName(baseWorkflowName);
					StringBuilder builderQuery=new StringBuilder();
					builderQuery.append("SELECT QL.QLID ");
					builderQuery.append("FROM BDCK.BDCS_DJDY_XZ DJDY ");
					builderQuery.append("LEFT JOIN BDCK.BDCS_QL_XZ QL ON QL.DJDYID=DJDY.DJDYID ");
					builderQuery.append("LEFT JOIN BDCK.BDCS_QLR_XZ QLR ON QL.QLID=QLR.QLID ");
					builderQuery.append("WHERE DJDY.BDCDYLX='").append(baseWorkflow.getUnittype()).append("' ");
					builderQuery.append("AND QLR.BDCQZH='").append(rowInfo.get(colNum)).append("' ");
					List<Map> listQlr=baseCommonDao.getDataListByFullSql(builderQuery.toString());
					List<String> listQlid=new ArrayList<String>();
					if(listQlr!=null&&listQlr.size()>0){
						for(Map qlinfo : listQlr){
							String qlid=StringHelper.formatObject(qlinfo.get("QLID"));
							if(!StringHelper.isEmpty(qlid)&&!listQlid.contains(qlid)){
								listQlid.add(qlid);
							}
						}
					}
					if(listQlid==null||listQlid.size()<=0){
						detailInfo.put("BDCDYID", "");
						detailInfo.put("状态", "严重");
						detailInfo.put("说明", "抵押权不存在！");
					}else{
						ResultMessage ms =constraintcheck.acceptCheckByBDCDYIDByWorkflowCode(StringHelper.formatList(listQlid, ","),workflowCode);
						if (("false").equals(ms.getSuccess())) {
							detailInfo.put("状态", "严重"); 
							detailInfo.put("说明", ms.getMsg());
						} else if (("warning").equals(ms.getSuccess())) {
							detailInfo.put("状态", "警告");
							detailInfo.put("说明", ms.getMsg());
						}else{
							detailInfo.put("状态", "校验通过");
							detailInfo.put("说明", "可受理");
						}
						detailInfo.put("BDCDYID", StringHelper.formatList(listQlid, ","));
					}
				}
			}
			if(!detailInfo.containsKey("状态")){
				detailInfo.put("BDCDYID", "");
				detailInfo.put("状态", "严重");
				detailInfo.put("说明", "校验失败");
			}
			checkAccept.add(detailInfo);
		}
		result.put("success", "true");
		result.put("msg", checkAccept);
		return result;
	}
	
	/*
	 * 必须登记信息及属性及其相应必填性控制
	 */
	private List<String> GetRequiredInfo(String requeiredInfo,boolean bVaule){
		List<String> listRequired=new ArrayList<String>();
		if("YG002".equals(requeiredInfo)||"YG102".equals(requeiredInfo)||"YG003".equals(requeiredInfo)){
			if(bVaule){
				listRequired.add("项目标识");
				listRequired.add("项目名称");
				listRequired.add("不动产单元号");
			}else{
				listRequired.add("项目单元信息");
				listRequired.add("商品房预告登记信息");
				listRequired.add("抵押权预告登记信息");
				listRequired.add("商品房预告登记义务人");
				listRequired.add("商品房预告登记权利人");
				listRequired.add("抵押权预告登记权利人");
			}
		}else if("YG001".equals(requeiredInfo)){
			if(bVaule){
				listRequired.add("项目标识");
				listRequired.add("项目名称");
				listRequired.add("不动产单元号");
			}else{
				listRequired.add("项目单元信息");
			}
		}else if("CS013".equals(requeiredInfo)){
			if(bVaule){
				listRequired.add("项目标识");
				listRequired.add("项目名称");
				listRequired.add("不动产单元号");
			}else{
				listRequired.add("项目单元信息");
				listRequired.add("在建工程抵押登记信息");
				listRequired.add("在建工程抵押登记义务人");
				listRequired.add("在建工程抵押登记权利人");
			}
		}else if("ZY002".equals(requeiredInfo)||"ZY102".equals(requeiredInfo)){
			if(bVaule){
				listRequired.add("项目标识");
				listRequired.add("项目名称");
				listRequired.add("不动产单元号");
			}else{
				listRequired.add("项目单元信息");
				listRequired.add("国有建设用地使用权-房屋所有权登记信息");
				listRequired.add("国有建设用地使用权-房屋所有权义务人");
				listRequired.add("国有建设用地使用权-房屋所有权权利人");
			}
		}else if("CS011".equals(requeiredInfo)){
			if(bVaule){
				listRequired.add("项目标识");
				listRequired.add("项目名称");
				listRequired.add("不动产单元号");
			}else{
				listRequired.add("项目单元信息");
				listRequired.add("抵押权登记信息");
				listRequired.add("抵押权登记义务人");
				listRequired.add("抵押权登记权利人");
			}
		}else if("ZX004".equals(requeiredInfo)||"ZX006".equals(requeiredInfo)||"ZX009".equals(requeiredInfo)){
			if(bVaule){
				listRequired.add("项目标识");
				listRequired.add("项目名称");
				listRequired.add("抵押权证明号");
			}else{
				listRequired.add("项目信息");
			}
		}else if("项目单元信息".equals(requeiredInfo)){
			listRequired.add("项目标识");
			listRequired.add("项目名称");
			listRequired.add("不动产单元号");
			if(!bVaule){
				listRequired.add("是否多个单元一本证");
			}
		}else if("项目信息".equals(requeiredInfo)){
			listRequired.add("项目标识");
			listRequired.add("项目名称");
			listRequired.add("抵押权证明号");
		}else if("商品房预告登记信息".equals(requeiredInfo)||"国有建设用地使用权-房屋所有权登记信息".equals(requeiredInfo)){
			listRequired.add("项目标识");
			listRequired.add("不动产单元号");
			listRequired.add("持证方式");
			if(!bVaule){
				listRequired.add("权利起始时间");
				listRequired.add("权利结束时间");
				listRequired.add("登记原因");
				listRequired.add("附记");
				listRequired.add("取得价格");
			}
		}else if("抵押权预告登记信息".equals(requeiredInfo)||"抵押权登记信息".equals(requeiredInfo)||"在建工程抵押登记信息".equals(requeiredInfo)){
			listRequired.add("项目标识");
			listRequired.add("不动产单元号");
			listRequired.add("债权起始时间");
			listRequired.add("债权结束时间");
			listRequired.add("抵押方式");
			listRequired.add("被担保债权数额(最高债权数额)");
			if("在建工程抵押登记信息".equals(requeiredInfo)){
				listRequired.add("抵押人");
			}
			if(!bVaule){
				listRequired.add("最高债权确定事实");
				listRequired.add("债权单位");
				listRequired.add("抵押评估价值");
				listRequired.add("在建建筑物坐落");
				listRequired.add("在建建筑物抵押范围");
				listRequired.add("登记原因");
				listRequired.add("附记");
			}
		}else if("商品房预告登记义务人".equals(requeiredInfo)||"抵押权登记义务人".equals(requeiredInfo)||"国有建设用地使用权-房屋所有权义务人".equals(requeiredInfo)
				||"在建工程抵押登记义务人".equals(requeiredInfo)){
			listRequired.add("项目标识");
			listRequired.add("不动产单元号");
			listRequired.add("权利人名称");
			listRequired.add("证件类型");
			listRequired.add("证件号");
			if(!bVaule){
				listRequired.add("性别");
				listRequired.add("权利人类型");
				listRequired.add("通讯地址");
				listRequired.add("联系电话");
				listRequired.add("法定代表人");
				listRequired.add("法定代表人证件类型");
				listRequired.add("法定代表人证件号");
				listRequired.add("法定代表人电话");
				listRequired.add("代理人姓名");
				listRequired.add("代理人证件类型");
				listRequired.add("代理人证件号码");
				listRequired.add("代理人联系电话");
			}
		}else if("商品房预告登记权利人".equals(requeiredInfo)||"国有建设用地使用权-房屋所有权权利人".equals(requeiredInfo)){
			listRequired.add("项目标识");
			listRequired.add("不动产单元号");
			listRequired.add("权利人名称");
			listRequired.add("证件类型");
			listRequired.add("证件号");
			if(!bVaule){
				listRequired.add("性别");
				listRequired.add("权利人类型");
				listRequired.add("共有方式");
				listRequired.add("权利比例");
				listRequired.add("通讯地址");
				listRequired.add("联系电话");
				listRequired.add("顺序号");
				listRequired.add("法定代表人");
				listRequired.add("法定代表人证件类型");
				listRequired.add("法定代表人证件号");
				listRequired.add("法定代表人电话");
				listRequired.add("代理人姓名");
				listRequired.add("代理人证件类型");
				listRequired.add("代理人证件号码");
				listRequired.add("代理人联系电话");
				listRequired.add("是否持证人");
			}
		}else if("抵押权预告登记权利人".equals(requeiredInfo)||"抵押权登记权利人".equals(requeiredInfo)||"在建工程抵押登记权利人".equals(requeiredInfo)){
			listRequired.add("项目标识");
			listRequired.add("不动产单元号");
			listRequired.add("权利人名称");
			listRequired.add("证件类型");
			listRequired.add("证件号");
			if(!bVaule){
				listRequired.add("性别");
				listRequired.add("权利人类型");
				listRequired.add("共有方式");
				listRequired.add("权利比例");
				listRequired.add("通讯地址");
				listRequired.add("联系电话");
				listRequired.add("顺序号");
				listRequired.add("法定代表人");
				listRequired.add("法定代表人证件类型");
				listRequired.add("法定代表人证件号");
				listRequired.add("法定代表人电话");
				listRequired.add("代理人姓名");
				listRequired.add("代理人证件类型");
				listRequired.add("代理人证件号码");
				listRequired.add("代理人联系电话");
				listRequired.add("是否持证人");
			}
		}
		return listRequired;
	}
	
	/*
	 * 必须登记信息及属性是否存在检查
	 */
	private List<String> CheckReuired(List<String> listRequired,HashMap<Integer,String> info){
		List<String> loseInfo=new ArrayList<String>();
		for(int i=0;i<listRequired.size();i++){
			if(!info.containsValue(listRequired.get(i))){
				loseInfo.add(listRequired.get(i));
			}
		}
		return loseInfo;
	}
	
	/*
	 * 必须登记信息及属性值必填检查
	 */
	private List<String> CheckValueReuired(List<String> listRequired,HashMap<Integer,String> colInfos,HashMap<Integer,HashMap<Integer,String>> cellInfo){
		List<String> loseSheetInfo=new ArrayList<String>();
		Set<Integer> setCol = colInfos.keySet();
		Iterator<Integer> iteratorCol = setCol.iterator();
		while (iteratorCol.hasNext()) {
			Integer colNum = iteratorCol.next();
			String colName = colInfos.get(colNum);
			if(StringHelper.isEmpty(colName)){
				continue;
			}
			if(!listRequired.contains(colName)){
				continue;
			}
			List<String> loseRowInfo=new ArrayList<String>();
			Set<Integer> setRow = cellInfo.keySet();
			Iterator<Integer> iteratorRow = setRow.iterator();
			while (iteratorRow.hasNext()) {
				Integer rowNum = iteratorRow.next();
				HashMap<Integer,String> rowInfo = cellInfo.get(rowNum);
				if(!rowInfo.containsKey(colNum)||StringHelper.isEmpty(rowInfo.get(colNum))){
					loseRowInfo.add(StringHelper.formatObject(rowNum));
				}
			}
			if(loseRowInfo.size()>0){
				loseSheetInfo.add(colName+"为必填项，第"+StringHelper.formatList(loseRowInfo, "、")+"行值为空！");
			}
		}
		return loseSheetInfo;
	}
	
	/*
	 * 各个登记信息中 关联字段信息检查
	 */
	private List<String> CheckRelationReuired(HashMap<Integer,HashMap<Integer,String>> mainCellInfo,HashMap<Integer,String> mainColInfo,
			List<String> listRequired,String mainSheetName,List<String> listSheet,HashMap<Integer,String> sheetInfoList,
			HashMap<Integer,HashMap<Integer,String>> colInfoList,HashMap<Integer,HashMap<Integer,HashMap<Integer,String>>> cellInfoList){
		List<String> loseRelationInfo=new ArrayList<String>();
		HashMap<String,List<String>> mainCellValue=new HashMap<String, List<String>>();
		Set<Integer> setSheet = sheetInfoList.keySet();
		Iterator<Integer> iteratorSheet = setSheet.iterator();
		while (iteratorSheet.hasNext()) {
			Integer sheetNum = iteratorSheet.next();
			String sheetName = sheetInfoList.get(sheetNum);
			if(StringHelper.isEmpty(sheetName)){
				continue;
			}
			if(!sheetName.equals(mainSheetName)){
				continue;
			}
			mainCellInfo.putAll(cellInfoList.get(sheetNum));
			mainColInfo.putAll(colInfoList.get(sheetNum));
			HashMap<Integer,String> colInfo=colInfoList.get(sheetNum);
			HashMap<Integer,HashMap<Integer,String>> cellInfo=cellInfoList.get(sheetNum);
			Set<Integer> setCol = colInfo.keySet();
			Iterator<Integer> iteratorCol = setCol.iterator();
			while (iteratorCol.hasNext()) {
				Integer colNum = iteratorCol.next();
				String colName = colInfo.get(colNum);
				if(listRequired.contains(colName)){
					List<String> listInfoRequired=new ArrayList<String>();
					Set<Integer> setRow = cellInfo.keySet();
					Iterator<Integer> iteratorRow = setRow.iterator();
					while (iteratorRow.hasNext()) {
						Integer rowNum = iteratorRow.next();
						HashMap<Integer,String> rowInfo = cellInfo.get(rowNum);
						if(rowInfo.containsKey(colNum)){
							if("不动产单元号".equals(colNum)||"抵押权证明号".equals(colNum)){
								List<String> listdyh=Arrays.asList(rowInfo.get(colNum).split(";"));
								if(listdyh!=null&&listdyh.size()>0){
									for(String dyh:listdyh){
										if(!StringHelper.isEmpty(dyh)&&!listInfoRequired.contains(dyh)){
											listInfoRequired.add(dyh);
										}
									}
								}
							}else{
								if(!StringHelper.isEmpty(rowInfo.get(colNum))&&!listInfoRequired.contains(rowInfo.get(colNum))){
									listInfoRequired.add(rowInfo.get(colNum));
								}
							}
						}
					}
					mainCellValue.put(colName, listInfoRequired);
				}
			}
			break;
		}
		
		Set<Integer> setSheet2 = sheetInfoList.keySet();
		Iterator<Integer> iteratorSheet2 = setSheet2.iterator();
		while (iteratorSheet2.hasNext()) {
			Integer sheetNum = iteratorSheet2.next();
			String sheetName = sheetInfoList.get(sheetNum);
			if(StringHelper.isEmpty(sheetName)){
				continue;
			}
			if(sheetName.equals(mainSheetName)){
				continue;
			}
			if(!listSheet.contains(sheetName)){
				continue;
			}
			HashMap<Integer,String> colInfo=colInfoList.get(sheetNum);
			HashMap<Integer,HashMap<Integer,String>> cellInfo=cellInfoList.get(sheetNum);
			Set<Integer> setCol = colInfo.keySet();
			Iterator<Integer> iteratorCol = setCol.iterator();
			while (iteratorCol.hasNext()) {
				Integer colNum = iteratorCol.next();
				String colName = colInfo.get(colNum);
				if(listRequired.contains(colName)){
					List<String> listInfoRequired=new ArrayList<String>();
					Set<Integer> setRow = cellInfo.keySet();
					Iterator<Integer> iteratorRow = setRow.iterator();
					while (iteratorRow.hasNext()) {
						Integer rowNum = iteratorRow.next();
						HashMap<Integer,String> rowInfo = cellInfo.get(rowNum);
						if(rowInfo.containsKey(colNum)){
							if("不动产单元号".equals(colNum)||"抵押权证明号".equals(colNum)){
								List<String> listdyh=Arrays.asList(rowInfo.get(colNum).split(";"));
								if(listdyh!=null&&listdyh.size()>0){
									for(String dyh:listdyh){
										if(!StringHelper.isEmpty(dyh)&&!listInfoRequired.contains(dyh)){
											listInfoRequired.add(dyh);
										}
									}
								}
							}else{
								if(!StringHelper.isEmpty(rowInfo.get(colNum))&&!listInfoRequired.contains(rowInfo.get(colNum))){
									listInfoRequired.add(rowInfo.get(colNum));
								}
							}
						}
					}
					List<String> listMiss=new ArrayList<String>();
					List<String> listRedundant =new ArrayList<String>();
					listRedundant.addAll(listInfoRequired);
					
					for(String cellvalue : mainCellValue.get(colName)){
						if(listRedundant.contains(cellvalue)){
							listRedundant.remove(cellvalue);
						}else{
							listMiss.add(cellvalue);
						}
					}
					if(listMiss.size()>0){
						loseRelationInfo.add(sheetName+"中"+colName+"缺失值："+StringHelper.formatList(listMiss, "、"));
					}
					if(listRedundant.size()>0){
						loseRelationInfo.add(sheetName+"中"+colName+"冗余值："+StringHelper.formatList(listRedundant, "、"));
					}
				}
			}
		}
		return loseRelationInfo;
	}
	
	/*
	 * 根据解析excel标识和根据流程定义id受理项目
	 */
	@Override
	public HashMap<String, Object> AcceptBathcProjectExcel(
			HttpServletRequest request, String excelid, String prodef_id,HttpServletResponse response) {
		HashMap<String,Object> result=new HashMap<String, Object>();
		Wfd_Prodef prodef=baseCommonDao.get(Wfd_Prodef.class, prodef_id);
		if(prodef==null){
			result.put("msg", "流程解析错误！");
			result.put("success", "false");
			return result;
		}
		String prodefCode=prodef.getProdef_Code();
		if(StringHelper.isEmpty(prodefCode)){
			result.put("msg", "流程解析错误！");
			result.put("success", "false");
			return result;
		}
		List<WFD_MAPPING> mps=baseCommonDao.getDataList(WFD_MAPPING.class, "workflowcode='"+prodefCode+"'");
		if(mps==null||mps.size()!=1){
			result.put("msg", "流程配置错误！");
			result.put("success", "false");
			return result;
		}
		String baseWorkflowName=mps.get(0).getWORKFLOWNAME();
		result=AcceptProjectExcelByBaseWorkflowName(request,excelid,prodef_id,baseWorkflowName, response);
		return result;
	}
	
	/*
	 * 根据解析excel标识和根据流程定义id和基准流程标识受理项目
	 */
	public HashMap<String, Object> AcceptProjectExcelByBaseWorkflowName(HttpServletRequest request,String excelid,
			String prodef_id,String baseWorkflowName,HttpServletResponse response){
		HashMap<String,Object> result=new HashMap<String, Object>();
		String batchNumber="PC"+baseCommonDao.CreatMaxID(ConfigHelper.getNameByValue("XZQHDM"), "123456", "BATCHNUM");
		HashMap<String,Object> excelInfo=listBatchProjectExcelInfo.get(excelid);
		if("YG002".equals(baseWorkflowName)||"YG102".equals(baseWorkflowName)||"YG003".equals(baseWorkflowName)){
			result=AcceptProjectExcelYG002Ex(request,prodef_id,batchNumber,excelInfo, response);
		}else if("YG001".equals(baseWorkflowName)){
			result=AcceptProjectExcelYG001Ex(request,prodef_id,batchNumber,excelInfo, response);
		}else if("CS011".equals(baseWorkflowName)){
			result=AcceptProjectExcelCS011Ex2(request,prodef_id,batchNumber,excelInfo, response);
		}else if("ZY002".equals(baseWorkflowName)||"ZY102".equals(baseWorkflowName)||"ZY007".equals(baseWorkflowName)){
			result=AcceptProjectExcelZY002Ex(request,prodef_id,batchNumber,excelInfo, response);
		}else if("CS013".equals(baseWorkflowName)){
			result=AcceptProjectExcelCS013Ex2(request,prodef_id,batchNumber,excelInfo, response);
		}else if("CS014".equals(baseWorkflowName)){
			result=AcceptProjectExcelCS014(request,prodef_id,batchNumber,excelInfo, response);
		}else if("CS015".equals(baseWorkflowName)||"CS115".equals(baseWorkflowName)){
			result=AcceptProjectExcelCS015(request,prodef_id,batchNumber,excelInfo, response);
		}else if("ZX004".equals(baseWorkflowName)||"ZX006".equals(baseWorkflowName)||"ZX009".equals(baseWorkflowName)) {
			result=AcceptProjectExcelZX004(request,prodef_id,batchNumber,excelInfo, response);
		}
		listBatchProjectExcelInfo.remove(excelid);
		result.put("asd", excelInfo);
		return result;
	}
	
	
	/*
	 * 根据流程定义id和批次号及excel解析信息受理项目（预告+预抵押项目:YG002/YG102）
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	private HashMap<String,Object> AcceptProjectExcelYG002(HttpServletRequest request,String prodef_id,String batchNumber,HashMap<String,Object> excelInfo,HttpServletResponse response){
		HashMap<String,Object> result=new HashMap<String, Object>();
		List<String> xmidlist=(List<String>)excelInfo.get("xmidlist");
		List<HashMap<String,String>> acceptinfolist=new ArrayList<HashMap<String,String>>();
		result.put("BATCHNUM", batchNumber);
		int iXh=1;
		for(String xmid:xmidlist){
			///创建项目
			BDCS_XMXX xmxx= CreateProject(request,batchNumber,prodef_id, response);
			if(xmxx==null){
				break;
			}
			///项目信息
			HashMap<String,Object> xminfo=(HashMap<String,Object>)excelInfo.get(xmid);
			
			///项目信息
			HashMap<String,String> info=(HashMap<String,String>)xminfo.get("info");
			//更新项目信息
			StringHelper.setValue(info, xmxx);
			
			List<Wfi_ProInst> list_proinst=baseCommonDao.getDataList(Wfi_ProInst.class, "FILE_NUMBER='"+xmxx.getPROJECT_ID()+"'");
			if(list_proinst!=null&&list_proinst.size()>0){
				Wfi_ProInst proinst=list_proinst.get(0);
				proinst.setProject_Name(xmxx.getXMMC());
				baseCommonDao.save(proinst);
			}
			
			baseCommonDao.save(xmxx);
			baseCommonDao.flush();
			Global.removeXMXX(xmxx.getPROJECT_ID());
			
			HashMap<String,String> acceptinfo=new HashMap<String, String>();
			acceptinfo.put("XMMC", xmxx.getXMMC());
			acceptinfo.put("YWLSH", xmxx.getYWLSH());
			//不动产单元id列表
			List<String> dyidlist=(List<String>)xminfo.get("dyidlist");
			//不动产单元id和不动产单元号关系
			HashMap<String,String> dyid_dyh=(HashMap<String,String>)xminfo.get("dyid_dyh");
			//单元号和权利信息关系
			HashMap<String,HashMap<String,String>> dyh_qlinfo=(HashMap<String,HashMap<String,String>>)xminfo.get("dyh_qlinfo");
			//单元号和抵押权信息关系
			HashMap<String,HashMap<String,String>> dyh_dyqinfo=(HashMap<String,HashMap<String,String>>)xminfo.get("dyh_dyqinfo");
			//申请人id列表
			List<String> sqridlist=(List<String>)xminfo.get("sqridlist");
			//申请人id和申请人信息关系
			HashMap<String,HashMap<String,String>> sqrid_sqrxx=(HashMap<String,HashMap<String,String>>)xminfo.get("sqrid_sqrxx");
			//单元号和权利人申请人id关系
			HashMap<String,List<String>> dyh_qlrids=(HashMap<String,List<String>>)xminfo.get("dyh_qlrids");
			//单元号和抵押权人申请人id关系
			HashMap<String,List<String>> dyh_dyqrids=(HashMap<String,List<String>>)xminfo.get("dyh_dyqrids");
			if(dyid_dyh!=null&&dyid_dyh.size()>0){
				String dyhlist=StringHelper.formatList(Arrays.asList(dyid_dyh.values().toArray(new String[dyid_dyh.size()])),"、");
				acceptinfo.put("DYHLIST", dyhlist);
			}else{
				acceptinfo.put("DYHLIST", "");
			}
			acceptinfo.put("SLRY", xmxx.getSLRY());
			acceptinfo.put("SLSJ", StringHelper.FormatDateOnType(xmxx.getSLSJ(), "yyyy-MM-dd"));
			acceptinfo.put("XH", StringHelper.formatObject(iXh));
			acceptinfo.put("XMID", StringHelper.formatObject(info.get("xmid")));
			
			//添加单元
			dyService.addBDCDYNoCheck(xmxx.getId(), StringHelper.formatList(dyidlist, ","));
			baseCommonDao.flush();
			//构建登记单元id和不动产单元id关系
			HashMap<String,String> djdyid_bdcdyid=new HashMap<String, String>();
			List<BDCS_DJDY_GZ> djdys=baseCommonDao.getDataList(BDCS_DJDY_GZ.class, "XMBH='"+xmxx.getId()+"'");
			for(BDCS_DJDY_GZ djdy:djdys){
				if(!djdyid_bdcdyid.containsKey(djdy.getDJDYID())){
					djdyid_bdcdyid.put(djdy.getDJDYID(), djdy.getBDCDYID());
				}
			}
			//移除原申请人
			List<BDCS_SQR> sqrlist_old=baseCommonDao.getDataList(BDCS_SQR.class, "XMBH='"+xmxx.getId()+"'");
			if(sqrlist_old!=null&&sqrlist_old.size()>0){
				for(BDCS_SQR sqr_old:sqrlist_old){
					baseCommonDao.deleteEntity(sqr_old);
				}
			}
			baseCommonDao.flush();
			///添加申请人
			for(String sqrid:sqridlist){
				if(!sqrid_sqrxx.containsKey(sqrid)){
					continue;
				}
				HashMap<String,String> sqrinfo=sqrid_sqrxx.get(sqrid);
				BDCS_SQR sqr = new BDCS_SQR();
				StringHelper.setValue(sqrinfo, sqr);
				sqr.setId(sqrid);
				sqr.setXMBH(xmxx.getId());
				baseCommonDao.save(sqr);
			}
			baseCommonDao.flush();
			//获取权利和附属权利信息
			List<Rights> qls=RightsTools.loadRightsByCondition(DJDYLY.GZ, "XMBH='"+xmxx.getId()+"'");
			List<SubRights> fsqls=RightsTools.loadSubRightsByCondition(DJDYLY.GZ, "XMBH='"+xmxx.getId()+"'");
			for(Rights ql:qls){
				String djdyid=ql.getDJDYID();
				if(!djdyid_bdcdyid.containsKey(djdyid)){
					continue;
				}
				String bdcdyid=djdyid_bdcdyid.get(djdyid);
				if(!dyid_dyh.containsKey(bdcdyid)){
					continue;
				}
				String bdcdyh=dyid_dyh.get(bdcdyid);
				///更新权利和抵押权信息
				if(QLLX.DIYQ.Value.equals(ql.getQLLX())){
					HashMap<String,String> dyqinfo=dyh_dyqinfo.get(bdcdyh);
					StringHelper.setValue(dyqinfo, ql);
					baseCommonDao.update(ql);
					for(SubRights fsql:fsqls){
						if(ql.getId().equals(fsql.getQLID())||fsql.getId().equals(ql.getFSQLID())){
							StringHelper.setValue(dyqinfo, fsql);
							Double dyje=StringHelper.getDouble(dyqinfo.get("ZQSE"));
							if(DYFS.YBDY.Value.equals(fsql.getDYFS())){
								fsql.setBDBZZQSE(dyje);
								fsql.setZGZQQDSS(null);
							}else if(DYFS.ZGEDY.Value.equals(fsql.getDYFS())){
								fsql.setZGZQSE(dyje);
							}
							baseCommonDao.update(fsql);
						}
					}
				}else{
					HashMap<String,String> qlinfo=dyh_qlinfo.get(bdcdyh);
					StringHelper.setValue(qlinfo, ql);
					baseCommonDao.update(ql);
					for(SubRights fsql:fsqls){
						if(ql.getId().equals(fsql.getQLID())||fsql.getId().equals(ql.getFSQLID())){
							StringHelper.setValue(qlinfo, fsql);
							baseCommonDao.update(fsql);
						}
					}
				}
				baseCommonDao.flush();
				///添加权利人和抵押权人
				if(QLLX.DIYQ.Value.equals(ql.getQLLX())){
					if(dyh_dyqrids.containsKey(bdcdyh)){
						List<String> dyqridlist=dyh_dyqrids.get(bdcdyh);
						qlService.addQLRfromSQR(xmxx.getId(), ql.getId(),
								dyqridlist.toArray());
					}
					
					
				}else{
					if(dyh_qlrids.containsKey(bdcdyh)){
						List<String> qlridlist=dyh_qlrids.get(bdcdyh);
						qlService.addQLRfromSQR(xmxx.getId(), ql.getId(),
								qlridlist.toArray());
					}
				}
				baseCommonDao.flush();
			}
			acceptinfo.put("DYGS", StringHelper.formatObject(djdys.size()));
			acceptinfo.put("BATCHNUM", batchNumber);
			acceptinfolist.add(acceptinfo);
			iXh++;
		}
		result.put("acceptinfolist", acceptinfolist);
		result.put("batchnum", batchNumber);
		result.put("success", "true");
		return result;
	}
	
	/*
	 * 根据流程定义id和批次号及excel解析信息受理项目（现房抵押项目:CS011）
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	private HashMap<String,Object> AcceptProjectExcelCS011(HttpServletRequest request,String prodef_id,String batchNumber,HashMap<String,Object> excelInfo,HttpServletResponse response){
		HashMap<String,Object> result=new HashMap<String, Object>();
		List<String> xmidlist=(List<String>)excelInfo.get("xmidlist");
		List<HashMap<String,String>> acceptinfolist=new ArrayList<HashMap<String,String>>();
		result.put("BATCHNUM", batchNumber);
		int iXh=1;
		for(String xmid:xmidlist){
			///创建项目
			BDCS_XMXX xmxx= CreateProject(request,batchNumber,prodef_id, response);
			if(xmxx==null){
				break;
			}
			///项目信息
			HashMap<String,Object> xminfo=(HashMap<String,Object>)excelInfo.get(xmid);
			
			///项目信息
			HashMap<String,String> info=(HashMap<String,String>)xminfo.get("info");
			//更新项目信息
			StringHelper.setValue(info, xmxx);
			
			List<Wfi_ProInst> list_proinst=baseCommonDao.getDataList(Wfi_ProInst.class, "FILE_NUMBER='"+xmxx.getPROJECT_ID()+"'");
			if(list_proinst!=null&&list_proinst.size()>0){
				Wfi_ProInst proinst=list_proinst.get(0);
				proinst.setProject_Name(xmxx.getXMMC());
				baseCommonDao.save(proinst);
			}
			
			baseCommonDao.save(xmxx);
			baseCommonDao.flush();
			Global.removeXMXX(xmxx.getPROJECT_ID());
			
			HashMap<String,String> acceptinfo=new HashMap<String, String>();
			acceptinfo.put("XMMC", xmxx.getXMMC());
			acceptinfo.put("YWLSH", xmxx.getYWLSH());
			//不动产单元id列表
			List<String> dyidlist=(List<String>)xminfo.get("dyidlist");
			//不动产单元id和不动产单元号关系
			HashMap<String,String> dyid_dyh=(HashMap<String,String>)xminfo.get("dyid_dyh");
			//单元号和抵押权信息关系
			HashMap<String,HashMap<String,String>> dyh_dyqinfo=(HashMap<String,HashMap<String,String>>)xminfo.get("dyh_dyqinfo");
			//申请人id列表
			List<String> sqridlist=(List<String>)xminfo.get("sqridlist");
			//申请人id和申请人信息关系
			HashMap<String,HashMap<String,String>> sqrid_sqrxx=(HashMap<String,HashMap<String,String>>)xminfo.get("sqrid_sqrxx");
			//单元号和抵押权人申请人id关系
			HashMap<String,List<String>> dyh_dyqrids=(HashMap<String,List<String>>)xminfo.get("dyh_dyqrids");
			
			if(dyid_dyh!=null&&dyid_dyh.size()>0){
				String dyhlist=StringHelper.formatList(Arrays.asList(dyid_dyh.values().toArray(new String[dyid_dyh.size()])),"、");
				acceptinfo.put("DYHLIST", dyhlist);
			}else{
				acceptinfo.put("DYHLIST", "");
			}
			acceptinfo.put("SLRY", xmxx.getSLRY());
			acceptinfo.put("SLSJ", StringHelper.FormatDateOnType(xmxx.getSLSJ(), "yyyy-MM-dd"));
			acceptinfo.put("XH", StringHelper.formatObject(iXh));
			acceptinfo.put("XMID", StringHelper.formatObject(info.get("xmid")));
			
			//添加单元
			dyService.addBDCDYNoCheck(xmxx.getId(), StringHelper.formatList(dyidlist, ","));
			baseCommonDao.flush();
			//构建登记单元id和不动产单元id关系
			HashMap<String,String> djdyid_bdcdyid=new HashMap<String, String>();
			List<BDCS_DJDY_GZ> djdys=baseCommonDao.getDataList(BDCS_DJDY_GZ.class, "XMBH='"+xmxx.getId()+"'");
			for(BDCS_DJDY_GZ djdy:djdys){
				if(!djdyid_bdcdyid.containsKey(djdy.getDJDYID())){
					djdyid_bdcdyid.put(djdy.getDJDYID(), djdy.getBDCDYID());
				}
			}
			//移除原申请人
			List<BDCS_SQR> sqrlist_old=baseCommonDao.getDataList(BDCS_SQR.class, "XMBH='"+xmxx.getId()+"'");
			if(sqrlist_old!=null&&sqrlist_old.size()>0){
				for(BDCS_SQR sqr_old:sqrlist_old){
					baseCommonDao.deleteEntity(sqr_old);
				}
			}
			baseCommonDao.flush();
			///添加申请人
			for(String sqrid:sqridlist){
				if(!sqrid_sqrxx.containsKey(sqrid)){
					continue;
				}
				HashMap<String,String> sqrinfo=sqrid_sqrxx.get(sqrid);
				BDCS_SQR sqr = new BDCS_SQR();
				StringHelper.setValue(sqrinfo, sqr);
				sqr.setId(sqrid);
				sqr.setXMBH(xmxx.getId());
				baseCommonDao.save(sqr);
			}
			baseCommonDao.flush();
			//获取权利和附属权利信息
			List<Rights> qls=RightsTools.loadRightsByCondition(DJDYLY.GZ, "XMBH='"+xmxx.getId()+"'");
			List<SubRights> fsqls=RightsTools.loadSubRightsByCondition(DJDYLY.GZ, "XMBH='"+xmxx.getId()+"'");
			for(Rights ql:qls){
				String djdyid=ql.getDJDYID();
				if(!djdyid_bdcdyid.containsKey(djdyid)){
					continue;
				}
				String bdcdyid=djdyid_bdcdyid.get(djdyid);
				if(!dyid_dyh.containsKey(bdcdyid)){
					continue;
				}
				String bdcdyh=dyid_dyh.get(bdcdyid);
				///更新抵押权信息
				HashMap<String,String> dyqinfo=dyh_dyqinfo.get(bdcdyh);
				StringHelper.setValue(dyqinfo, ql);
				baseCommonDao.update(ql);
				for(SubRights fsql:fsqls){
					if(ql.getId().equals(fsql.getQLID())||fsql.getId().equals(ql.getFSQLID())){
						StringHelper.setValue(dyqinfo, fsql);
						Double dyje=StringHelper.getDouble(dyqinfo.get("ZQSE"));
						if(DYFS.YBDY.Value.equals(fsql.getDYFS())){
							fsql.setBDBZZQSE(dyje);
							fsql.setZGZQQDSS(null);
						}else if(DYFS.ZGEDY.Value.equals(fsql.getDYFS())){
							fsql.setZGZQSE(dyje);
						}
						baseCommonDao.update(fsql);
					}
				}
				baseCommonDao.flush();
				///添加抵押权人
				if(dyh_dyqrids.containsKey(bdcdyh)){
					List<String> dyqridlist=dyh_dyqrids.get(bdcdyh);
					qlService.addQLRfromSQR(xmxx.getId(), ql.getId(),
							dyqridlist.toArray());
				}
				baseCommonDao.flush();
			}
			acceptinfo.put("DYGS", StringHelper.formatObject(djdys.size()));
			acceptinfo.put("BATCHNUM", batchNumber);
			acceptinfolist.add(acceptinfo);
			iXh++;
		}
		result.put("acceptinfolist", acceptinfolist);
		result.put("batchnum", batchNumber);
		result.put("success", "true");
		return result;
	}
	
	/*
	 * 根据流程定义id和批次号及excel解析信息受理项目（在建工程抵押项目:CS013）
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	private HashMap<String,Object> AcceptProjectExcelCS013(HttpServletRequest request,String prodef_id,String batchNumber,HashMap<String,Object> excelInfo,HttpServletResponse response){
		HashMap<String,Object> result=new HashMap<String, Object>();
		List<String> xmidlist=(List<String>)excelInfo.get("xmidlist");
		List<HashMap<String,String>> acceptinfolist=new ArrayList<HashMap<String,String>>();
		result.put("BATCHNUM", batchNumber);
		int iXh=1;
		for(String xmid:xmidlist){
			///创建项目
			BDCS_XMXX xmxx= CreateProject(request,batchNumber,prodef_id, response);
			if(xmxx==null){
				break;
			}
			///项目信息
			HashMap<String,Object> xminfo=(HashMap<String,Object>)excelInfo.get(xmid);
			
			///项目信息
			HashMap<String,String> info=(HashMap<String,String>)xminfo.get("info");
			//更新项目信息
			StringHelper.setValue(info, xmxx);
			
			List<Wfi_ProInst> list_proinst=baseCommonDao.getDataList(Wfi_ProInst.class, "FILE_NUMBER='"+xmxx.getPROJECT_ID()+"'");
			if(list_proinst!=null&&list_proinst.size()>0){
				Wfi_ProInst proinst=list_proinst.get(0);
				proinst.setProject_Name(xmxx.getXMMC());
				baseCommonDao.save(proinst);
			}
			
			baseCommonDao.save(xmxx);
			baseCommonDao.flush();
			Global.removeXMXX(xmxx.getPROJECT_ID());
			
			HashMap<String,String> acceptinfo=new HashMap<String, String>();
			acceptinfo.put("XMMC", xmxx.getXMMC());
			acceptinfo.put("YWLSH", xmxx.getYWLSH());
			//不动产单元id列表
			List<String> dyidlist=(List<String>)xminfo.get("dyidlist");
			//不动产单元id和不动产单元号关系
			HashMap<String,String> dyid_dyh=(HashMap<String,String>)xminfo.get("dyid_dyh");
			//单元号和抵押权信息关系
			HashMap<String,HashMap<String,String>> dyh_dyqinfo=(HashMap<String,HashMap<String,String>>)xminfo.get("dyh_dyqinfo");
			//申请人id列表
			List<String> sqridlist=(List<String>)xminfo.get("sqridlist");
			//申请人id和申请人信息关系
			HashMap<String,HashMap<String,String>> sqrid_sqrxx=(HashMap<String,HashMap<String,String>>)xminfo.get("sqrid_sqrxx");
			//单元号和抵押权人申请人id关系
			HashMap<String,List<String>> dyh_dyqrids=(HashMap<String,List<String>>)xminfo.get("dyh_dyqrids");
			
			if(dyid_dyh!=null&&dyid_dyh.size()>0){
				String dyhlist=StringHelper.formatList(Arrays.asList(dyid_dyh.values().toArray(new String[dyid_dyh.size()])),"、");
				acceptinfo.put("DYHLIST", dyhlist);
			}else{
				acceptinfo.put("DYHLIST", "");
			}
			acceptinfo.put("SLRY", xmxx.getSLRY());
			acceptinfo.put("SLSJ", StringHelper.FormatDateOnType(xmxx.getSLSJ(), "yyyy-MM-dd"));
			acceptinfo.put("XH", StringHelper.formatObject(iXh));
			acceptinfo.put("XMID", StringHelper.formatObject(info.get("xmid")));
			//添加单元
			dyService.addBDCDYNoCheck(xmxx.getId(), StringHelper.formatList(dyidlist, ","));
			baseCommonDao.flush();
			//构建登记单元id和不动产单元id关系
			HashMap<String,String> djdyid_bdcdyid=new HashMap<String, String>();
			List<BDCS_DJDY_GZ> djdys=baseCommonDao.getDataList(BDCS_DJDY_GZ.class, "XMBH='"+xmxx.getId()+"'");
			for(BDCS_DJDY_GZ djdy:djdys){
				if(!djdyid_bdcdyid.containsKey(djdy.getDJDYID())){
					djdyid_bdcdyid.put(djdy.getDJDYID(), djdy.getBDCDYID());
				}
			}
			//移除原申请人
			List<BDCS_SQR> sqrlist_old=baseCommonDao.getDataList(BDCS_SQR.class, "XMBH='"+xmxx.getId()+"'");
			if(sqrlist_old!=null&&sqrlist_old.size()>0){
				for(BDCS_SQR sqr_old:sqrlist_old){
					baseCommonDao.deleteEntity(sqr_old);
				}
			}
			baseCommonDao.flush();
			///添加申请人
			for(String sqrid:sqridlist){
				if(!sqrid_sqrxx.containsKey(sqrid)){
					continue;
				}
				HashMap<String,String> sqrinfo=sqrid_sqrxx.get(sqrid);
				BDCS_SQR sqr = new BDCS_SQR();
				StringHelper.setValue(sqrinfo, sqr);
				sqr.setId(sqrid);
				sqr.setXMBH(xmxx.getId());
				baseCommonDao.save(sqr);
			}
			baseCommonDao.flush();
			//获取权利和附属权利信息
			List<Rights> qls=RightsTools.loadRightsByCondition(DJDYLY.GZ, "XMBH='"+xmxx.getId()+"'");
			List<SubRights> fsqls=RightsTools.loadSubRightsByCondition(DJDYLY.GZ, "XMBH='"+xmxx.getId()+"'");
			for(Rights ql:qls){
				String djdyid=ql.getDJDYID();
				if(!djdyid_bdcdyid.containsKey(djdyid)){
					continue;
				}
				String bdcdyid=djdyid_bdcdyid.get(djdyid);
				if(!dyid_dyh.containsKey(bdcdyid)){
					continue;
				}
				String bdcdyh=dyid_dyh.get(bdcdyid);
				///更新抵押权信息
				HashMap<String,String> dyqinfo=dyh_dyqinfo.get(bdcdyh);
				StringHelper.setValue(dyqinfo, ql);
				baseCommonDao.update(ql);
				for(SubRights fsql:fsqls){
					if(ql.getId().equals(fsql.getQLID())||fsql.getId().equals(ql.getFSQLID())){
						StringHelper.setValue(dyqinfo, fsql);
						Double dyje=StringHelper.getDouble(dyqinfo.get("ZQSE"));
						if(DYFS.YBDY.Value.equals(fsql.getDYFS())){
							fsql.setBDBZZQSE(dyje);
							fsql.setZGZQQDSS(null);
						}else if(DYFS.ZGEDY.Value.equals(fsql.getDYFS())){
							fsql.setZGZQSE(dyje);
						}
						baseCommonDao.update(fsql);
					}
				}
				baseCommonDao.flush();
				///添加抵押权人
				if(dyh_dyqrids.containsKey(bdcdyh)){
					List<String> dyqridlist=dyh_dyqrids.get(bdcdyh);
					qlService.addQLRfromSQR(xmxx.getId(), ql.getId(),
							dyqridlist.toArray());
				}
				baseCommonDao.flush();
			}
			acceptinfo.put("DYGS", StringHelper.formatObject(djdys.size()));
			acceptinfo.put("BATCHNUM", batchNumber);
			acceptinfolist.add(acceptinfo);
			iXh++;
		}
		result.put("acceptinfolist", acceptinfolist);
		result.put("batchnum", batchNumber);
		result.put("success", "true");
		return result;
	}
	
	/*
	 * 根据流程定义id和批次号及excel解析信息受理项目（在建工程抵押登记转现房抵押项目:CS014）
	 */
	@SuppressWarnings("unchecked")
	private HashMap<String,Object> AcceptProjectExcelCS014(HttpServletRequest request,String prodef_id,String batchNumber,HashMap<String,Object> excelInfo,HttpServletResponse response){
		HashMap<String,Object> result=new HashMap<String, Object>();
		List<String> xmidlist=(List<String>)excelInfo.get("xmidlist");
		List<HashMap<String,String>> acceptinfolist=new ArrayList<HashMap<String,String>>();
		result.put("BATCHNUM", batchNumber);
		int iXh=1;
		for(String xmid:xmidlist){
			///创建项目
			BDCS_XMXX xmxx= CreateProject(request,batchNumber,prodef_id, response);
			if(xmxx==null){
				break;
			}
			///项目信息
			HashMap<String,Object> xminfo=(HashMap<String,Object>)excelInfo.get(xmid);
			
			///项目信息
			HashMap<String,String> info=(HashMap<String,String>)xminfo.get("info");
			//更新项目信息
			StringHelper.setValue(info, xmxx);
			
			List<Wfi_ProInst> list_proinst=baseCommonDao.getDataList(Wfi_ProInst.class, "FILE_NUMBER='"+xmxx.getPROJECT_ID()+"'");
			if(list_proinst!=null&&list_proinst.size()>0){
				Wfi_ProInst proinst=list_proinst.get(0);
				proinst.setProject_Name(xmxx.getXMMC());
				baseCommonDao.save(proinst);
			}
			
			baseCommonDao.save(xmxx);
			baseCommonDao.flush();
			Global.removeXMXX(xmxx.getPROJECT_ID());
			
			HashMap<String,String> acceptinfo=new HashMap<String, String>();
			acceptinfo.put("XMMC", xmxx.getXMMC());
			acceptinfo.put("YWLSH", xmxx.getYWLSH());
			//不动产单元id列表
			List<String> dyidlist=(List<String>)xminfo.get("dyidlist");
			//不动产单元id和不动产单元号关系
			HashMap<String,String> dyid_dyh=(HashMap<String,String>)xminfo.get("dyid_dyh");
//			//单元号和抵押权信息关系
//			HashMap<String,HashMap<String,String>> dyh_dyqinfo=(HashMap<String,HashMap<String,String>>)xminfo.get("dyh_dyqinfo");
//			//申请人id列表
//			List<String> sqridlist=(List<String>)xminfo.get("sqridlist");
//			//申请人id和申请人信息关系
//			HashMap<String,HashMap<String,String>> sqrid_sqrxx=(HashMap<String,HashMap<String,String>>)xminfo.get("sqrid_sqrxx");
//			//单元号和抵押权人申请人id关系
//			HashMap<String,List<String>> dyh_dyqrids=(HashMap<String,List<String>>)xminfo.get("dyh_dyqrids");
			
			if(dyid_dyh!=null&&dyid_dyh.size()>0){
				String dyhlist=StringHelper.formatList(Arrays.asList(dyid_dyh.values().toArray(new String[dyid_dyh.size()])),"、");
				acceptinfo.put("DYHLIST", dyhlist);
			}else{
				acceptinfo.put("DYHLIST", "");
			}
			acceptinfo.put("SLRY", xmxx.getSLRY());
			acceptinfo.put("SLSJ", StringHelper.FormatDateOnType(xmxx.getSLSJ(), "yyyy-MM-dd"));
			acceptinfo.put("XH", StringHelper.formatObject(iXh));
			acceptinfo.put("XMID", StringHelper.formatObject(info.get("xmid")));
			
			//添加单元
			dyService.addBDCDYNoCheck(xmxx.getId(), StringHelper.formatList(dyidlist, ","));
			baseCommonDao.flush();
			//构建登记单元id和不动产单元id关系
			HashMap<String,String> djdyid_bdcdyid=new HashMap<String, String>();
			List<BDCS_DJDY_GZ> djdys=baseCommonDao.getDataList(BDCS_DJDY_GZ.class, "XMBH='"+xmxx.getId()+"'");
			for(BDCS_DJDY_GZ djdy:djdys){
				if(!djdyid_bdcdyid.containsKey(djdy.getDJDYID())){
					djdyid_bdcdyid.put(djdy.getDJDYID(), djdy.getBDCDYID());
				}
			}
//			//移除原申请人
//			List<BDCS_SQR> sqrlist_old=baseCommonDao.getDataList(BDCS_SQR.class, "XMBH='"+xmxx.getId()+"'");
//			if(sqrlist_old!=null&&sqrlist_old.size()>0){
//				for(BDCS_SQR sqr_old:sqrlist_old){
//					baseCommonDao.deleteEntity(sqr_old);
//				}
//			}
//			baseCommonDao.flush();
//			//移除原权利人
//			List<BDCS_QLR_GZ> qlrlist_old=baseCommonDao.getDataList(BDCS_QLR_GZ.class, "XMBH='"+xmxx.getId()+"'");
//			if(qlrlist_old!=null&&qlrlist_old.size()>0){
//				for(BDCS_QLR_GZ qlr_old:qlrlist_old){
//					baseCommonDao.deleteEntity(qlr_old);
//				}
//			}
//			baseCommonDao.flush();
//			//移除原权地证人
//			List<BDCS_QDZR_GZ> qdzrlist_old=baseCommonDao.getDataList(BDCS_QDZR_GZ.class, "XMBH='"+xmxx.getId()+"'");
//			if(qdzrlist_old!=null&&qdzrlist_old.size()>0){
//				for(BDCS_QDZR_GZ qdzr_old:qdzrlist_old){
//					baseCommonDao.deleteEntity(qdzr_old);
//				}
//			}
//			baseCommonDao.flush();
//			//移除原证书
//			List<BDCS_ZS_GZ> zslist_old=baseCommonDao.getDataList(BDCS_ZS_GZ.class, "XMBH='"+xmxx.getId()+"'");
//			if(zslist_old!=null&&zslist_old.size()>0){
//				for(BDCS_ZS_GZ zs_old:zslist_old){
//					baseCommonDao.deleteEntity(zs_old);
//				}
//			}
//			baseCommonDao.flush();
//			///添加申请人
//			for(String sqrid:sqridlist){
//				if(!sqrid_sqrxx.containsKey(sqrid)){
//					continue;
//				}
//				HashMap<String,String> sqrinfo=sqrid_sqrxx.get(sqrid);
//				BDCS_SQR sqr = new BDCS_SQR();
//				StringHelper.setValue(sqrinfo, sqr);
//				sqr.setId(sqrid);
//				sqr.setXMBH(xmxx.getId());
//				baseCommonDao.save(sqr);
//			}
//			baseCommonDao.flush();
//			//获取权利和附属权利信息
//			List<Rights> qls=RightsTools.loadRightsByCondition(DJDYLY.GZ, "XMBH='"+xmxx.getId()+"'");
//			List<SubRights> fsqls=RightsTools.loadSubRightsByCondition(DJDYLY.GZ, "XMBH='"+xmxx.getId()+"'");
//			for(Rights ql:qls){
//				String djdyid=ql.getDJDYID();
//				if(!djdyid_bdcdyid.containsKey(djdyid)){
//					continue;
//				}
//				String bdcdyid=djdyid_bdcdyid.get(djdyid);
//				if(!dyid_dyh.containsKey(bdcdyid)){
//					continue;
//				}
//				String bdcdyh=dyid_dyh.get(bdcdyid);
//				///更新抵押权信息
//				HashMap<String,String> dyqinfo=dyh_dyqinfo.get(bdcdyh);
//				StringHelper.setValue(dyqinfo, ql);
//				baseCommonDao.update(ql);
//				for(SubRights fsql:fsqls){
//					if(ql.getId().equals(fsql.getQLID())||fsql.getId().equals(ql.getFSQLID())){
//						StringHelper.setValue(dyqinfo, fsql);
//						Double dyje=StringHelper.getDouble(dyqinfo.get("ZQSE"));
//						if(DYFS.YBDY.Value.equals(fsql.getDYFS())){
//							fsql.setBDBZZQSE(dyje);
//							fsql.setZGZQQDSS(null);
//						}else if(DYFS.ZGEDY.Value.equals(fsql.getDYFS())){
//							fsql.setZGZQSE(dyje);
//						}
//						baseCommonDao.update(fsql);
//					}
//				}
//				baseCommonDao.flush();
//				///添加抵押权人
//				if(dyh_dyqrids.containsKey(bdcdyh)){
//					List<String> dyqridlist=dyh_dyqrids.get(bdcdyh);
//					qlService.addQLRfromSQR(xmxx.getId(), ql.getId(),
//							dyqridlist.toArray());
//				}
//				baseCommonDao.flush();
//			}
			acceptinfo.put("DYGS", StringHelper.formatObject(djdys.size()));
			acceptinfo.put("BATCHNUM", batchNumber);
			acceptinfolist.add(acceptinfo);
			iXh++;
		}
		result.put("acceptinfolist", acceptinfolist);
		result.put("batchnum", batchNumber);
		result.put("success", "true");
		return result;
	}
	
	/*
	 * 根据流程定义id和批次号及excel解析信息受理项目（预购商品房抵押权预告登记转现房抵押权项目:CS015/CS115）
	 */
	@SuppressWarnings("unchecked")
	private HashMap<String,Object> AcceptProjectExcelCS015(HttpServletRequest request,String prodef_id,String batchNumber,HashMap<String,Object> excelInfo,HttpServletResponse response){
		HashMap<String,Object> result=new HashMap<String, Object>();
		List<String> xmidlist=(List<String>)excelInfo.get("xmidlist");
		List<HashMap<String,String>> acceptinfolist=new ArrayList<HashMap<String,String>>();
		result.put("BATCHNUM", batchNumber);
		int iXh=1;
		for(String xmid:xmidlist){
			///创建项目
			BDCS_XMXX xmxx= CreateProject(request,batchNumber,prodef_id, response);
			if(xmxx==null){
				break;
			}
			///项目信息
			HashMap<String,Object> xminfo=(HashMap<String,Object>)excelInfo.get(xmid);
			
			///项目信息
			HashMap<String,String> info=(HashMap<String,String>)xminfo.get("info");
			//更新项目信息
			StringHelper.setValue(info, xmxx);
			
			List<Wfi_ProInst> list_proinst=baseCommonDao.getDataList(Wfi_ProInst.class, "FILE_NUMBER='"+xmxx.getPROJECT_ID()+"'");
			if(list_proinst!=null&&list_proinst.size()>0){
				Wfi_ProInst proinst=list_proinst.get(0);
				proinst.setProject_Name(xmxx.getXMMC());
				baseCommonDao.save(proinst);
			}
			
			baseCommonDao.save(xmxx);
			baseCommonDao.flush();
			Global.removeXMXX(xmxx.getPROJECT_ID());
			
			HashMap<String,String> acceptinfo=new HashMap<String, String>();
			acceptinfo.put("XMMC", xmxx.getXMMC());
			acceptinfo.put("YWLSH", xmxx.getYWLSH());
			//不动产单元id列表
			List<String> dyidlist=(List<String>)xminfo.get("dyidlist");
//			//不动产单元id和不动产单元号关系
			HashMap<String,String> dyid_dyh=(HashMap<String,String>)xminfo.get("dyid_dyh");
//			//单元号和抵押权信息关系
//			HashMap<String,HashMap<String,String>> dyh_dyqinfo=(HashMap<String,HashMap<String,String>>)xminfo.get("dyh_dyqinfo");
//			//申请人id列表
//			List<String> sqridlist=(List<String>)xminfo.get("sqridlist");
//			//申请人id和申请人信息关系
//			HashMap<String,HashMap<String,String>> sqrid_sqrxx=(HashMap<String,HashMap<String,String>>)xminfo.get("sqrid_sqrxx");
//			//单元号和抵押权人申请人id关系
//			HashMap<String,List<String>> dyh_dyqrids=(HashMap<String,List<String>>)xminfo.get("dyh_dyqrids");
			
			if(dyid_dyh!=null&&dyid_dyh.size()>0){
				String dyhlist=StringHelper.formatList(Arrays.asList(dyid_dyh.values().toArray(new String[dyid_dyh.size()])),"、");
				acceptinfo.put("DYHLIST", dyhlist);
			}else{
				acceptinfo.put("DYHLIST", "");
			}
			acceptinfo.put("SLRY", xmxx.getSLRY());
			acceptinfo.put("SLSJ", StringHelper.FormatDateOnType(xmxx.getSLSJ(), "yyyy-MM-dd"));
			acceptinfo.put("XH", StringHelper.formatObject(iXh));
			acceptinfo.put("XMID", StringHelper.formatObject(info.get("xmid")));
			
			//添加单元
			dyService.addBDCDYNoCheck(xmxx.getId(), StringHelper.formatList(dyidlist, ","));
			baseCommonDao.flush();
			//构建登记单元id和不动产单元id关系
			HashMap<String,String> djdyid_bdcdyid=new HashMap<String, String>();
			List<BDCS_DJDY_GZ> djdys=baseCommonDao.getDataList(BDCS_DJDY_GZ.class, "XMBH='"+xmxx.getId()+"'");
			for(BDCS_DJDY_GZ djdy:djdys){
				if(!djdyid_bdcdyid.containsKey(djdy.getDJDYID())){
					djdyid_bdcdyid.put(djdy.getDJDYID(), djdy.getBDCDYID());
				}
			}
//			//移除原申请人
//			List<BDCS_SQR> sqrlist_old=baseCommonDao.getDataList(BDCS_SQR.class, "XMBH='"+xmxx.getId()+"'");
//			if(sqrlist_old!=null&&sqrlist_old.size()>0){
//				for(BDCS_SQR sqr_old:sqrlist_old){
//					baseCommonDao.deleteEntity(sqr_old);
//				}
//			}
//			baseCommonDao.flush();
//			//移除原权利人
//			List<BDCS_QLR_GZ> qlrlist_old=baseCommonDao.getDataList(BDCS_QLR_GZ.class, "XMBH='"+xmxx.getId()+"'");
//			if(qlrlist_old!=null&&qlrlist_old.size()>0){
//				for(BDCS_QLR_GZ qlr_old:qlrlist_old){
//					baseCommonDao.deleteEntity(qlr_old);
//				}
//			}
//			baseCommonDao.flush();
//			//移除原权地证人
//			List<BDCS_QDZR_GZ> qdzrlist_old=baseCommonDao.getDataList(BDCS_QDZR_GZ.class, "XMBH='"+xmxx.getId()+"'");
//			if(qdzrlist_old!=null&&qdzrlist_old.size()>0){
//				for(BDCS_QDZR_GZ qdzr_old:qdzrlist_old){
//					baseCommonDao.deleteEntity(qdzr_old);
//				}
//			}
//			baseCommonDao.flush();
//			//移除原证书
//			List<BDCS_ZS_GZ> zslist_old=baseCommonDao.getDataList(BDCS_ZS_GZ.class, "XMBH='"+xmxx.getId()+"'");
//			if(zslist_old!=null&&zslist_old.size()>0){
//				for(BDCS_ZS_GZ zs_old:zslist_old){
//					baseCommonDao.deleteEntity(zs_old);
//				}
//			}
//			baseCommonDao.flush();
//			///添加申请人
//			for(String sqrid:sqridlist){
//				if(!sqrid_sqrxx.containsKey(sqrid)){
//					continue;
//				}
//				HashMap<String,String> sqrinfo=sqrid_sqrxx.get(sqrid);
//				BDCS_SQR sqr = new BDCS_SQR();
//				StringHelper.setValue(sqrinfo, sqr);
//				sqr.setId(sqrid);
//				sqr.setXMBH(xmxx.getId());
//				baseCommonDao.save(sqr);
//			}
//			baseCommonDao.flush();
//			//获取权利和附属权利信息
//			List<Rights> qls=RightsTools.loadRightsByCondition(DJDYLY.GZ, "XMBH='"+xmxx.getId()+"'");
//			List<SubRights> fsqls=RightsTools.loadSubRightsByCondition(DJDYLY.GZ, "XMBH='"+xmxx.getId()+"'");
//			for(Rights ql:qls){
//				String djdyid=ql.getDJDYID();
//				if(!djdyid_bdcdyid.containsKey(djdyid)){
//					continue;
//				}
//				String bdcdyid=djdyid_bdcdyid.get(djdyid);
//				if(!dyid_dyh.containsKey(bdcdyid)){
//					continue;
//				}
//				String bdcdyh=dyid_dyh.get(bdcdyid);
//				///更新抵押权信息
//				HashMap<String,String> dyqinfo=dyh_dyqinfo.get(bdcdyh);
//				StringHelper.setValue(dyqinfo, ql);
//				baseCommonDao.update(ql);
//				for(SubRights fsql:fsqls){
//					if(ql.getId().equals(fsql.getQLID())||fsql.getId().equals(ql.getFSQLID())){
//						StringHelper.setValue(dyqinfo, fsql);
//						Double dyje=StringHelper.getDouble(dyqinfo.get("ZQSE"));
//						if(DYFS.YBDY.Value.equals(fsql.getDYFS())){
//							fsql.setBDBZZQSE(dyje);
//							fsql.setZGZQQDSS(null);
//						}else if(DYFS.ZGEDY.Value.equals(fsql.getDYFS())){
//							fsql.setZGZQSE(dyje);
//						}
//						baseCommonDao.update(fsql);
//					}
//				}
//				baseCommonDao.flush();
//				///添加抵押权人
//				if(dyh_dyqrids.containsKey(bdcdyh)){
//					List<String> dyqridlist=dyh_dyqrids.get(bdcdyh);
//					qlService.addQLRfromSQR(xmxx.getId(), ql.getId(),
//							dyqridlist.toArray());
//				}
//				baseCommonDao.flush();
//			}
			acceptinfo.put("DYGS", StringHelper.formatObject(djdys.size()));
			acceptinfo.put("BATCHNUM", batchNumber);
			acceptinfolist.add(acceptinfo);
			iXh++;
		}
		result.put("acceptinfolist", acceptinfolist);
		result.put("batchnum", batchNumber);
		result.put("success", "true");
		return result;
	}
	
	/*
	 * 根据流程定义id和批次号及excel解析信息受理项目（现房转移项目:ZY002/ZY102）
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	private HashMap<String,Object> AcceptProjectExcelZY002(HttpServletRequest request,String prodef_id,String batchNumber,HashMap<String,Object> excelInfo,HttpServletResponse response){
		HashMap<String,Object> result=new HashMap<String, Object>();
		List<String> xmidlist=(List<String>)excelInfo.get("xmidlist");
		List<HashMap<String,String>> acceptinfolist=new ArrayList<HashMap<String,String>>();
		result.put("BATCHNUM", batchNumber);
		int iXh=1;
		for(String xmid:xmidlist){
			///创建项目
			BDCS_XMXX xmxx= CreateProject(request,batchNumber,prodef_id, response);
			if(xmxx==null){
				break;
			}
			///项目信息
			HashMap<String,Object> xminfo=(HashMap<String,Object>)excelInfo.get(xmid);
			
			///项目信息
			HashMap<String,String> info=(HashMap<String,String>)xminfo.get("info");
			//更新项目信息
			StringHelper.setValue(info, xmxx);
			
			List<Wfi_ProInst> list_proinst=baseCommonDao.getDataList(Wfi_ProInst.class, "FILE_NUMBER='"+xmxx.getPROJECT_ID()+"'");
			if(list_proinst!=null&&list_proinst.size()>0){
				Wfi_ProInst proinst=list_proinst.get(0);
				proinst.setProject_Name(xmxx.getXMMC());
				baseCommonDao.save(proinst);
			}
			
			baseCommonDao.save(xmxx);
			baseCommonDao.flush();
			Global.removeXMXX(xmxx.getPROJECT_ID());
			
			HashMap<String,String> acceptinfo=new HashMap<String, String>();
			acceptinfo.put("XMMC", xmxx.getXMMC());
			acceptinfo.put("YWLSH", xmxx.getYWLSH());
			//不动产单元id列表
			List<String> dyidlist=(List<String>)xminfo.get("dyidlist");
			//不动产单元id和不动产单元号关系
			HashMap<String,String> dyid_dyh=(HashMap<String,String>)xminfo.get("dyid_dyh");
			//单元号和权利信息关系
			HashMap<String,HashMap<String,String>> dyh_qlinfo=(HashMap<String,HashMap<String,String>>)xminfo.get("dyh_qlinfo");
			//申请人id列表
			List<String> sqridlist=(List<String>)xminfo.get("sqridlist");
			//申请人id和申请人信息关系
			HashMap<String,HashMap<String,String>> sqrid_sqrxx=(HashMap<String,HashMap<String,String>>)xminfo.get("sqrid_sqrxx");
			//单元号和权利人申请人id关系
			HashMap<String,List<String>> dyh_qlrids=(HashMap<String,List<String>>)xminfo.get("dyh_qlrids");
			
			if(dyid_dyh!=null&&dyid_dyh.size()>0){
				String dyhlist=StringHelper.formatList(Arrays.asList(dyid_dyh.values().toArray(new String[dyid_dyh.size()])),"、");
				acceptinfo.put("DYHLIST", dyhlist);
			}else{
				acceptinfo.put("DYHLIST", "");
			}
			acceptinfo.put("SLRY", xmxx.getSLRY());
			acceptinfo.put("SLSJ", StringHelper.FormatDateOnType(xmxx.getSLSJ(), "yyyy-MM-dd"));
			acceptinfo.put("XH", StringHelper.formatObject(iXh));
			acceptinfo.put("XMID", StringHelper.formatObject(info.get("xmid")));
			//添加单元
			dyService.addBDCDYNoCheck(xmxx.getId(), StringHelper.formatList(dyidlist, ","));
			baseCommonDao.flush();
			//构建登记单元id和不动产单元id关系
			HashMap<String,String> djdyid_bdcdyid=new HashMap<String, String>();
			List<BDCS_DJDY_GZ> djdys=baseCommonDao.getDataList(BDCS_DJDY_GZ.class, "XMBH='"+xmxx.getId()+"'");
			for(BDCS_DJDY_GZ djdy:djdys){
				if(!djdyid_bdcdyid.containsKey(djdy.getDJDYID())){
					djdyid_bdcdyid.put(djdy.getDJDYID(), djdy.getBDCDYID());
				}
			}
			//移除原申请人
			List<BDCS_SQR> sqrlist_old=baseCommonDao.getDataList(BDCS_SQR.class, "XMBH='"+xmxx.getId()+"'");
			if(sqrlist_old!=null&&sqrlist_old.size()>0){
				for(BDCS_SQR sqr_old:sqrlist_old){
					baseCommonDao.deleteEntity(sqr_old);
				}
			}
			baseCommonDao.flush();
			///添加申请人
			for(String sqrid:sqridlist){
				if(!sqrid_sqrxx.containsKey(sqrid)){
					continue;
				}
				HashMap<String,String> sqrinfo=sqrid_sqrxx.get(sqrid);
				BDCS_SQR sqr = new BDCS_SQR();
				StringHelper.setValue(sqrinfo, sqr);
				sqr.setId(sqrid);
				sqr.setXMBH(xmxx.getId());
				baseCommonDao.save(sqr);
			}
			baseCommonDao.flush();
			//获取权利和附属权利信息
			List<Rights> qls=RightsTools.loadRightsByCondition(DJDYLY.GZ, "XMBH='"+xmxx.getId()+"'");
			List<SubRights> fsqls=RightsTools.loadSubRightsByCondition(DJDYLY.GZ, "XMBH='"+xmxx.getId()+"'");
			for(Rights ql:qls){
				String djdyid=ql.getDJDYID();
				if(!djdyid_bdcdyid.containsKey(djdyid)){
					continue;
				}
				String bdcdyid=djdyid_bdcdyid.get(djdyid);
				if(!dyid_dyh.containsKey(bdcdyid)){
					continue;
				}
				String bdcdyh=dyid_dyh.get(bdcdyid);
				///更新权利
				HashMap<String,String> qlinfo=dyh_qlinfo.get(bdcdyh);
				StringHelper.setValue(qlinfo, ql);
				baseCommonDao.update(ql);
				for(SubRights fsql:fsqls){
					if(ql.getId().equals(fsql.getQLID())||fsql.getId().equals(ql.getFSQLID())){
						StringHelper.setValue(qlinfo, fsql);
						baseCommonDao.update(fsql);
					}
				}
				baseCommonDao.flush();
				///添加权利人
				if(dyh_qlrids.containsKey(bdcdyh)){
					List<String> qlridlist=dyh_qlrids.get(bdcdyh);
					qlService.addQLRfromSQR(xmxx.getId(), ql.getId(),
							qlridlist.toArray());
				}
				baseCommonDao.flush();
			}
			acceptinfo.put("DYGS", StringHelper.formatObject(djdys.size()));
			acceptinfo.put("BATCHNUM", batchNumber);
			acceptinfolist.add(acceptinfo);
			iXh++;
		}
		result.put("acceptinfolist", acceptinfolist);
		result.put("batchnum", batchNumber);
		result.put("success", "true");
		return result;
	}
	
	/*
	 * 根据流程定义id和批次号及excel解析信息受理项目（抵押注销：ZX004、ZX006、ZX009）
	 */
	@SuppressWarnings("unchecked")
	private HashMap<String,Object> AcceptProjectExcelZX004(HttpServletRequest request,String prodef_id,String batchNumber,HashMap<String,Object> excelInfo,HttpServletResponse response){
		HashMap<String,Object> result=new HashMap<String, Object>();
		List<String> xmidlist=(List<String>)excelInfo.get("xmidlist");
		List<HashMap<String,String>> acceptinfolist=new ArrayList<HashMap<String,String>>();
		result.put("BATCHNUM", batchNumber);
		int iXh=1;
		for(String xmid:xmidlist){
			
			///创建项目
			BDCS_XMXX xmxx= CreateProject(request,batchNumber,prodef_id, response);
			if(xmxx==null){
				break;
			}
			
			///项目信息
			HashMap<String,Object> xminfo=(HashMap<String,Object>)excelInfo.get(xmid);
			HashMap<String,String> info=(HashMap<String,String>)xminfo.get("info");
			
			//更新项目信息
			StringHelper.setValue(info, xmxx);
			List<Wfi_ProInst> list_proinst=baseCommonDao.getDataList(Wfi_ProInst.class, "FILE_NUMBER='"+xmxx.getPROJECT_ID()+"'");
			if(list_proinst!=null&&list_proinst.size()>0){
				Wfi_ProInst proinst=list_proinst.get(0);
				proinst.setProject_Name(xmxx.getXMMC());
				baseCommonDao.save(proinst);
			}
			baseCommonDao.save(xmxx);
			baseCommonDao.flush();
			Global.removeXMXX(xmxx.getPROJECT_ID());
			
			HashMap<String,String> acceptinfo=new HashMap<String, String>();
			acceptinfo.put("XMMC", xmxx.getXMMC());
			acceptinfo.put("YWLSH", xmxx.getYWLSH());
			acceptinfo.put("SLRY", xmxx.getSLRY());
			acceptinfo.put("SLSJ", StringHelper.FormatDateOnType(xmxx.getSLSJ(), "yyyy-MM-dd"));
			acceptinfo.put("XH", StringHelper.formatObject(iXh));
			acceptinfo.put("XMID", StringHelper.formatObject(info.get("xmid")));
			
			//权利id和抵押权证号关系
			HashMap<String,String> qlid_zmh = (HashMap<String,String>)xminfo.get("qlid_zmh");
			if(qlid_zmh != null && qlid_zmh.size() > 0){
				String zmhlist = StringHelper.formatList(Arrays.asList(qlid_zmh.values().toArray(new String[qlid_zmh.size()])),"、");
				acceptinfo.put("ZMHLIST", zmhlist);
			}else{
				acceptinfo.put("ZMHLIST", "");
			}
			
			//权利id列表
			List<String> dyidlist=(List<String>)xminfo.get("dyidlist");
			
			//添加单元
			dyService.addBDCDYNoCheck(xmxx.getId(), StringHelper.formatList(dyidlist, ","));
			baseCommonDao.flush();
			
			List<BDCS_DJDY_GZ> djdys=baseCommonDao.getDataList(BDCS_DJDY_GZ.class, "XMBH='"+xmxx.getId()+"'");
			acceptinfo.put("DYGS", StringHelper.formatObject(djdys.size()));
			acceptinfo.put("BATCHNUM", batchNumber);
			acceptinfolist.add(acceptinfo);
			
			//更新附属权利
			List<SubRights> fsqls = RightsTools.loadSubRightsByCondition(DJDYLY.GZ, "XMBH='"+xmxx.getId()+"'");
			for(SubRights fsql:fsqls){
				fsql.setZXDYYY(info.get("zxyi"));
				fsql.setZXFJ(info.get("zxfj"));
			}
			baseCommonDao.flush();

			iXh++;
		}
		result.put("acceptinfolist", acceptinfolist);
		result.put("batchnum", batchNumber);
		result.put("success", "true");
		return result;
	}
	
	/*
	 * 根据批次号和流程定义id创建项目信息
	 */
	@SuppressWarnings("unused")
	private BDCS_XMXX CreateProject(HttpServletRequest request,String batchNumber,String prodef_id,HttpServletResponse response){
		try
		{
			String staffid = Global.getCurrentUserInfo().getId();
			SmObjInfo object=operationService.BatchAcceptProject(prodef_id, batchNumber, staffid, request, response);
			String id = object.getID();
			String desc = object.getDesc();
			if (!"受理成功".equals(desc)) {
				return null;
			}
			// 刘树峰 2016.3.17 创建项目 获取xmbh
			String project_id = null;
			Wfi_ProInst proinst = baseCommonDao.get(Wfi_ProInst.class, id);
			if (proinst != null) {
				project_id = proinst.getFile_Number();
			}
			//创建项目
			if (!StringHelper.isEmpty(project_id)) {
				ProjectInfo info = ProjectHelper.GetProjectFromRest(
						project_id, request);
			}
			BDCS_XMXX xmxx = Global.getXMXX(project_id);
			return xmxx;
		}catch(Exception ex){
			return null;
		}
	}


	/*****************************************Excel批量受理*****************************************/
	
	/*****************************************Excel批量受理Ex*****************************************/
	/**
	 * 解析Excel
	 * 
	 * @param excelPath excel文件路径
	 * @return excel解析信息
	 */
	private HashMap<String,Object> AnalysisExcelEx(String excelPath){
		HashMap<String,Object> excelInfo=new HashMap<String, Object>();
		//Sheet页号、页名
		HashMap<Integer,String> sheetInfoList=new HashMap<Integer, String>();
		//Sheet页号、数据
		HashMap<Integer,DataTable> cellinfolist=new HashMap<Integer, DataTable>();
		InputStream inputStream=null;
		try {
			inputStream=new FileInputStream(excelPath);
			String prefix=excelPath.substring(excelPath.lastIndexOf(".")+1);
			if("XLSX".equals(prefix.toUpperCase())){
				XSSFWorkbook xssfworkbook= new XSSFWorkbook(inputStream);//整个Excel
				for(int i=0;i<xssfworkbook.getNumberOfSheets();i++){
	        		XSSFSheet xssfsheet=xssfworkbook.getSheetAt(i);//第I页
	        		String sheetName=xssfsheet.getSheetName();
	        		if(StringHelper.isEmpty(sheetName)){
	        			continue;
	        		}
	        		DataTable dataTable=new DataTable();
	        		DataColumnCollection dtCols=dataTable.getColumns();
	        		///添加标题
        			XSSFRow xssfrow_col=xssfsheet.getRow(xssfsheet.getFirstRowNum());//第I页第J行
        			int mincoll_col=xssfrow_col.getFirstCellNum();
        			int maxcoll_col=xssfrow_col.getLastCellNum();
        			List<Integer> list_excelindex=new ArrayList<Integer>();
        			HashMap<Integer,Integer> excel_col_index=new HashMap<Integer,Integer>();
        			for(int cellnum=mincoll_col;cellnum<maxcoll_col;cellnum++){
    					XSSFCell cell=xssfrow_col.getCell(cellnum);//单元格
        				if(cell==null){
        					continue;
        				}
        				DataColumn dataColumn=new DataColumn();
        				String val="";
        				if(Cell.CELL_TYPE_STRING==cell.getCellType()){
        					val=cell.getStringCellValue();
        				}else if(Cell.CELL_TYPE_NUMERIC==cell.getCellType()){
        					if (HSSFDateUtil.isCellDateFormatted(cell)) {
            			        //  如果是date类型则 ，获取该cell的date值   
        						try{
        							SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");  
        							Date dt = HSSFDateUtil.getJavaDate(cell.getNumericCellValue());//获取成DATE类型     
        							val = dateformat.format(dt);   
        						}catch(Exception ex){
        							
        						}
        						  
            			    }else{
            			    	val = StringHelper.formatDouble(cell.getNumericCellValue());
            			    }
        				}else{
        					cell.setCellType(Cell.CELL_TYPE_STRING);
        					val=cell.getStringCellValue();
        				}
        				if(StringHelper.isEmpty(val)){
        					val=StringHelper.formatObject(cellnum);
        				}
        				dataColumn.setColumnName(GetFieldNameEx(sheetName,val,"Field"));
        				dataColumn.setLabel(val);
        				dataColumn.setDataType(0);
        				boolean bAdd=dtCols.add(dataColumn);
        				if(bAdd){
        					list_excelindex.add(cellnum);
            				excel_col_index.put(cellnum, dtCols.size()-1);
        				}
        			}
	        		
	        		for(int j=xssfsheet.getFirstRowNum()+1;j<=xssfsheet.getLastRowNum();j++){
	        			XSSFRow xssfrow=xssfsheet.getRow(j);//第I页第J行
        				DataRow dataRow=dataTable.newRow();
        				for(Integer cellnum:list_excelindex){
        					XSSFCell cell=xssfrow.getCell(cellnum);//单元格
	        				if(cell==null){
	        					continue;
	        				}
	        				String val="";
	        				if(Cell.CELL_TYPE_STRING==cell.getCellType()){
	        					val=cell.getStringCellValue();
	        				}else if(Cell.CELL_TYPE_NUMERIC==cell.getCellType()){
	        					if (HSSFDateUtil.isCellDateFormatted(cell)) {
	            			        //  如果是date类型则 ，获取该cell的date值   
	        						try{
	        							SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");  
	        							Date dt = HSSFDateUtil.getJavaDate(cell.getNumericCellValue());//获取成DATE类型     
	        							val = dateformat.format(dt);   
	        						}catch(Exception ex){
	        							
	        						}
	        						  
	            			    }else{
	            			    	val = StringHelper.formatDouble(cell.getNumericCellValue());
	            			    }
	        				}else{
	        					cell.setCellType(Cell.CELL_TYPE_STRING);
	        					val=cell.getStringCellValue();
	        				}
	        				dataRow.setObject(excel_col_index.get(cellnum), val);
	        			}
        				dataTable.getRows().add(dataRow);
//        				if(dataTable.getRows().size()>1){
//        					DataRow preDataRow=dataTable.getRows().get(dataTable.getRows().size()-2);
//            				if(StringHelper.isEmpty(dataRow.getString("BDCDYH"))){
//            					dataRow.setObject("BDCDYH", preDataRow.getString("BDCDYH"));
//            					dataRow.setObject("XMID", preDataRow.getString("XMID"));
//            					dataRow.setObject("XMMC", preDataRow.getString("XMMC"));
//            					dataRow.setObject("SFHBFZ", preDataRow.getString("SFHBFZ"));
//            					dataRow.setObject("ZL", preDataRow.getString("ZL"));
//            					dataRow.setObject("DYQLQSSJ", preDataRow.getString("DYQLQSSJ"));
//            					dataRow.setObject("DYQLJSSJ", preDataRow.getString("DYQLJSSJ"));
//            					dataRow.setObject("DYQLJSSJ", preDataRow.getString("DYQLJSSJ"));
//            				}
//    					}
	        		}
	        		cellinfolist.put(i, dataTable);
	    			sheetInfoList.put(i, sheetName);
	        	}
			}else if("XLS".equals(prefix.toUpperCase())){
				HSSFWorkbook hssfWorkbook = new HSSFWorkbook(inputStream);
	        	for(int i=0;i<hssfWorkbook.getNumberOfSheets();i++){
	        		HSSFSheet hssfsheet=hssfWorkbook.getSheetAt(i);//第I页
	        		String sheetName=hssfsheet.getSheetName();
	        		if(StringHelper.isEmpty(sheetName)){
	        			continue;
	        		}
	        		DataTable dataTable=new DataTable();
	        		DataColumnCollection dtCols=dataTable.getColumns();
	        		///添加标题
        			HSSFRow hssfrow_col=hssfsheet.getRow(hssfsheet.getFirstRowNum());//第I页第J行
        			int mincoll_col=hssfrow_col.getFirstCellNum();
        			int maxcoll_col=hssfrow_col.getLastCellNum();
        			List<Integer> list_excelindex=new ArrayList<Integer>();
        			HashMap<Integer,Integer> excel_col_index=new HashMap<Integer,Integer>();
        			for(int cellnum=mincoll_col;cellnum<maxcoll_col;cellnum++){
    					HSSFCell cell=hssfrow_col.getCell(cellnum);//单元格
        				if(cell==null){
        					continue;
        				}
        				DataColumn dataColumn=new DataColumn();
        				String val="";
        				if(Cell.CELL_TYPE_STRING==cell.getCellType()){
        					val=cell.getStringCellValue();
        				}else if(Cell.CELL_TYPE_NUMERIC==cell.getCellType()){
        					if (HSSFDateUtil.isCellDateFormatted(cell)) {
            			        //  如果是date类型则 ，获取该cell的date值   
        						try{
        							SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");  
        							Date dt = HSSFDateUtil.getJavaDate(cell.getNumericCellValue());//获取成DATE类型     
        							val = dateformat.format(dt);   
        						}catch(Exception ex){
        							
        						}
        						  
            			    }else{
            			    	val = StringHelper.formatDouble(cell.getNumericCellValue());
            			    }
        				}else{
        					cell.setCellType(Cell.CELL_TYPE_STRING);
        					val=cell.getStringCellValue();
        				}
        				if(StringHelper.isEmpty(val)){
        					val=StringHelper.formatObject(cellnum);
        				}
        				dataColumn.setColumnName(GetFieldNameEx(sheetName,val,"Field"));
        				dataColumn.setLabel(val);
        				dataColumn.setDataType(0);
        				boolean bAdd=dtCols.add(dataColumn);
        				if(bAdd){
        					list_excelindex.add(cellnum);
            				excel_col_index.put(cellnum, dtCols.size()-1);
        				}
        			}
	        		
	        		for(int j=hssfsheet.getFirstRowNum()+1;j<=hssfsheet.getLastRowNum();j++){
	        			HSSFRow hssfrow=hssfsheet.getRow(j);//第I页第J行
        				DataRow dataRow=dataTable.newRow();
        				for(Integer cellnum:list_excelindex){
        					HSSFCell cell=hssfrow.getCell(cellnum);//单元格
	        				if(cell==null){
	        					continue;
	        				}
	        				String val="";
	        				if(Cell.CELL_TYPE_STRING==cell.getCellType()){
	        					val=cell.getStringCellValue();
	        				}else if(Cell.CELL_TYPE_NUMERIC==cell.getCellType()){
	        					if (HSSFDateUtil.isCellDateFormatted(cell)) {
	            			        //  如果是date类型则 ，获取该cell的date值   
	        						try{
	        							SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");  
	        							Date dt = HSSFDateUtil.getJavaDate(cell.getNumericCellValue());//获取成DATE类型     
	        							val = dateformat.format(dt);   
	        						}catch(Exception ex){
	        							
	        						}
	        						  
	            			    }else{
	            			    	val = StringHelper.formatDouble(cell.getNumericCellValue());
	            			    }
	        				}else{
	        					cell.setCellType(Cell.CELL_TYPE_STRING);
	        					val=cell.getStringCellValue();
	        				}
	        				dataRow.setObject(excel_col_index.get(cellnum), val);
	        			}
        				dataTable.getRows().add(dataRow);
	        		}
	        		cellinfolist.put(i, dataTable);
	    			sheetInfoList.put(i, sheetName);
	        	}
			}
        	inputStream.close(); 
		} catch (Exception e) {
			if(inputStream!=null){
				try {
					inputStream.close();
				} catch (IOException e1) {
				} 
			}
			e.printStackTrace();
		}
		excelInfo.put("sheetInfoList", sheetInfoList);
		excelInfo.put("cellinfolist", cellinfolist);
		return excelInfo;
	}
	
	/*
	 * 批量受理Excel属性映射
	 */
	private String GetFieldNameEx(String sheetName,String colName,String returnType){
		String fieldName=colName;
		if("商品房转移登记".equals(sheetName)){
			if("项目标识".equals(colName)){
				fieldName="XMID";
				if("Entity".equals(returnType)){
					fieldName="XM";
				}
			}else if("项目".equals(colName)){
				fieldName="XMMC";
				if("Entity".equals(returnType)){
					fieldName="XM";
				}
			}else if("不动产单元号".equals(colName)){
				fieldName="BDCDYH";
				if("Entity".equals(returnType)){
					fieldName="DY";
				}
			}else if("坐落".equals(colName)){
				fieldName="ZL";
				if("Entity".equals(returnType)){
					fieldName="DY";
				}
			}else if("房屋取得价格".equals(colName)){
				fieldName="QDJG";
				if("Entity".equals(returnType)){
					fieldName="QL";
				}
			}else if("申请人名称".equals(colName)){
				fieldName="SQRXM";
				if("Entity".equals(returnType)){
					fieldName="QLR";
				}
			}else if("申请人类别".equals(colName)){
				fieldName="SQRLB";
				if("Entity".equals(returnType)){
					fieldName="QLR";
				}
			}else if("证件类型".equals(colName)){
				fieldName="ZJLX";
				if("Entity".equals(returnType)){
					fieldName="QLR";
				}
			}else if("证件号".equals(colName)){
				fieldName="ZJH";
				if("Entity".equals(returnType)){
					fieldName="QLR";
				}
			}else if("性别".equals(colName)){
				fieldName="XB";
				if("Entity".equals(returnType)){
					fieldName="QLR";
				}
			}else if("权利人类型".equals(colName)){
				fieldName="SQRLX";
				if("Entity".equals(returnType)){
					fieldName="QLR";
				}
			}else if("权利比例".equals(colName)){
				fieldName="QLBL";
				if("Entity".equals(returnType)){
					fieldName="QLR";
				}
			}else if("联系电话".equals(colName)){
				fieldName="LXDH";
				if("Entity".equals(returnType)){
					fieldName="QLR";
				}
			}else if("代理人姓名".equals(colName)){
				fieldName="DLRXM";
				if("Entity".equals(returnType)){
					fieldName="QLR";
				}
			}else if("代理人证件类型".equals(colName)){
				fieldName="DLRZJLX";
				if("Entity".equals(returnType)){
					fieldName="QLR";
				}
			}else if("代理人证件号码".equals(colName)){
				fieldName="DLRZJHM";
				if("Entity".equals(returnType)){
					fieldName="QLR";
				}
			}else if("代理人联系电话".equals(colName)){
				fieldName="DLRLXDH";
				if("Entity".equals(returnType)){
					fieldName="QLR";
				}
			}else if("共有方式".equals(colName)){
				fieldName="GYFS";
				if("Entity".equals(returnType)){
					fieldName="QLR";
				}
			}else if("持证方式".equals(colName)){
				fieldName="CZFS";
				if("Entity".equals(returnType)){
					fieldName="QL";
				}
			}else if("是否持证人".equals(colName)){
				fieldName="SFCZR";
				if("Entity".equals(returnType)){
					fieldName="QLR";
				}
			}
		}else if("预购商品房预告与抵押权预告合并登记".equals(sheetName)||"预购商品房预告登记".equals(sheetName)
				||"预购商品房抵押权预告登记".equals(sheetName)){
			if("项目标识".equals(colName)){
				fieldName="XMID";
				if("Entity".equals(returnType)){
					fieldName="XM";
				}
			}else if("项目名称".equals(colName)){
				fieldName="XMMC";
				if("Entity".equals(returnType)){
					fieldName="XM";
				}
			}else if("不动产单元号".equals(colName)){
				fieldName="BDCDYH";
				if("Entity".equals(returnType)){
					fieldName="DY";
				}
			}else if("坐落".equals(colName)){
				fieldName="ZL";
				if("Entity".equals(returnType)){
					fieldName="DY";
				}
			}else if("房屋取得价格".equals(colName)){
				fieldName="QDJG";
				if("Entity".equals(returnType)){
					fieldName="QL";
				}
			}else if("抵押债权起始时间".equals(colName)){
				fieldName="DYQLQSSJ";
				if("Entity".equals(returnType)){
					fieldName="DYQ";
				}
			}else if("抵押债权结束时间".equals(colName)){
				fieldName="DYQLJSSJ";
				if("Entity".equals(returnType)){
					fieldName="DYQ";
				}
			}else if("抵押方式".equals(colName)){
				fieldName="DYFS";
				if("Entity".equals(returnType)){
					fieldName="DYQ";
				}
			}else if("被担保债权数额(最高债权数额)".equals(colName)){
				fieldName="ZQSE";
				if("Entity".equals(returnType)){
					fieldName="DYQ";
				}
			}else if("申请人名称".equals(colName)){
				fieldName="SQRXM";
				if("Entity".equals(returnType)){
					fieldName="QLR";
				}
			}else if("申请人类别".equals(colName)){
				fieldName="SQRLB";
				if("Entity".equals(returnType)){
					fieldName="QLR";
				}
			}else if("证件类型".equals(colName)){
				fieldName="ZJLX";
				if("Entity".equals(returnType)){
					fieldName="QLR";
				}
			}else if("证件号".equals(colName)){
				fieldName="ZJH";
				if("Entity".equals(returnType)){
					fieldName="QLR";
				}
			}else if("性别".equals(colName)){
				fieldName="XB";
				if("Entity".equals(returnType)){
					fieldName="QLR";
				}
			}else if("权利人类型".equals(colName)){
				fieldName="SQRLX";
				if("Entity".equals(returnType)){
					fieldName="QLR";
				}
			}else if("共有方式".equals(colName)){
				fieldName="GYFS";
				if("Entity".equals(returnType)){
					fieldName="QLR";
				}
			}else if("权利比例".equals(colName)){
				fieldName="QLBL";
				if("Entity".equals(returnType)){
					fieldName="QLR";
				}
			}else if("联系电话".equals(colName)){
				fieldName="LXDH";
				if("Entity".equals(returnType)){
					fieldName="QLR";
				}
			}else if("代理人姓名".equals(colName)){
				fieldName="DLRXM";
				if("Entity".equals(returnType)){
					fieldName="QLR";
				}
			}else if("代理人证件类型".equals(colName)){
				fieldName="DLRZJLX";
				if("Entity".equals(returnType)){
					fieldName="QLR";
				}
			}else if("代理人证件号码".equals(colName)){
				fieldName="DLRZJHM";
				if("Entity".equals(returnType)){
					fieldName="QLR";
				}
			}else if("代理人联系电话".equals(colName)){
				fieldName="DLRLXDH";
				if("Entity".equals(returnType)){
					fieldName="QLR";
				}
			}
		}else if("现房抵押登记".equals(sheetName)||"在建工程抵押登记".equals(sheetName)){
			if("项目标识".equals(colName)){
				fieldName="XMID";
				if("Entity".equals(returnType)){
					fieldName="XM";
				}
			}else if("项目名称".equals(colName)){
				fieldName="XMMC";
				if("Entity".equals(returnType)){
					fieldName="XM";
				}
			}else if("是否多个单元一本证".equals(colName)){
				fieldName="SFHBZS";
				if("Entity".equals(returnType)){
					fieldName="XM";
				}
			}else if("不动产单元号".equals(colName)){
				fieldName="BDCDYH";
				if("Entity".equals(returnType)){
					fieldName="DY";
				}
			}else if("坐落".equals(colName)){
				fieldName="ZL";
				if("Entity".equals(returnType)){
					fieldName="DY";
				}
			}else if("抵押债权起始时间".equals(colName)){
				fieldName="DYQLQSSJ";
				if("Entity".equals(returnType)){
					fieldName="DYQ";
				}
			}else if("抵押债权结束时间".equals(colName)){
				fieldName="DYQLJSSJ";
				if("Entity".equals(returnType)){
					fieldName="DYQ";
				}
			}else if("抵押方式".equals(colName)){
				fieldName="DYFS";
				if("Entity".equals(returnType)){
					fieldName="DYQ";
				}
			}else if("被担保债权数额(最高债权数额)".equals(colName)){
				fieldName="ZQSE";
				if("Entity".equals(returnType)){
					fieldName="DYQ";
				}
			}else if("申请人名称".equals(colName)){
				fieldName="SQRXM";
				if("Entity".equals(returnType)){
					fieldName="QLR";
				}
			}else if("申请人类别".equals(colName)){
				fieldName="SQRLB";
				if("Entity".equals(returnType)){
					fieldName="QLR";
				}
			}else if("证件类型".equals(colName)){
				fieldName="ZJLX";
				if("Entity".equals(returnType)){
					fieldName="QLR";
				}
			}else if("证件号".equals(colName)){
				fieldName="ZJH";
				if("Entity".equals(returnType)){
					fieldName="QLR";
				}
			}else if("性别".equals(colName)){
				fieldName="XB";
				if("Entity".equals(returnType)){
					fieldName="QLR";
				}
			}else if("权利人类型".equals(colName)){
				fieldName="SQRLX";
				if("Entity".equals(returnType)){
					fieldName="QLR";
				}
			}else if("共有方式".equals(colName)){
				fieldName="GYFS";
				if("Entity".equals(returnType)){
					fieldName="QLR";
				}
			}else if("权利比例".equals(colName)){
				fieldName="QLBL";
				if("Entity".equals(returnType)){
					fieldName="QLR";
				}
			}else if("联系电话".equals(colName)){
				fieldName="LXDH";
				if("Entity".equals(returnType)){
					fieldName="QLR";
				}
			}else if("代理人姓名".equals(colName)){
				fieldName="DLRXM";
				if("Entity".equals(returnType)){
					fieldName="QLR";
				}
			}else if("代理人证件类型".equals(colName)){
				fieldName="DLRZJLX";
				if("Entity".equals(returnType)){
					fieldName="QLR";
				}
			}else if("代理人证件号码".equals(colName)){
				fieldName="DLRZJHM";
				if("Entity".equals(returnType)){
					fieldName="QLR";
				}
			}else if("代理人联系电话".equals(colName)){
				fieldName="DLRLXDH";
				if("Entity".equals(returnType)){
					fieldName="QLR";
				}
			}
		}
		return fieldName;
	}

	/**
	 * 检查Excel信息
	 * 
	 * @param excelPath excel文件路径
	 * @return excel解析信息
	 */
	private HashMap<String,Object> CheckExcelByBaseWorkflowNameEx(HashMap<String,Object> excelInfo,String baseWorkflowName,String workflowCode){
		HashMap<String,Object> result=new HashMap<String, Object>();
		result=CheckExcelIntegrityEx(excelInfo,workflowCode,baseWorkflowName);
		if(!"true".equals(result.get("success"))){
			return result;
		}
		return result;
	}
	
	/*
	 * 业务流程校验
	 */
	@SuppressWarnings({ "unchecked" })
	private HashMap<String,Object> CheckExcelIntegrityEx(HashMap<String,Object> excelInfo,String workflowCode,String baseWorkflowName){
		HashMap<String,Object> result=new HashMap<String, Object>();
		
		//Sheet页号、页名
		HashMap<Integer,String> sheetInfoList=(HashMap<Integer, String>)excelInfo.get("sheetInfoList");
		//Sheet页号、数据
		HashMap<Integer,DataTable> cellinfolist=(HashMap<Integer,DataTable>)excelInfo.get("cellinfolist");
		//检查缺失Sheet页
		List<String> sheetRequired=GetRequiredInfoEx(baseWorkflowName,false);
		List<String> checkSheet=CheckReuired(sheetRequired,sheetInfoList);
		if(checkSheet.size()>0){
			result.put("success", "false");
			result.put("msg", "缺失"+StringHelper.formatList(checkSheet, "、")+"信息");
			return result;
		}
		//必填列缺失
		List<String> checkCols=new ArrayList<String>();
		Set<Integer> setSheet = sheetInfoList.keySet();
		Iterator<Integer> iteratorSheet = setSheet.iterator();
		while (iteratorSheet.hasNext()) {
			Integer sheetNum = iteratorSheet.next();
			String sheetName = sheetInfoList.get(sheetNum);
			DataTable dataTable=cellinfolist.get(sheetNum);
			if(sheetRequired.contains(sheetName)){
				List<String> colsRequired=GetRequiredInfoEx(sheetName,false);
				List<String> loseCols=CheckReuiredEx(colsRequired,dataTable);
				if(loseCols.size()>0){
					checkCols.add(sheetName+"中缺失信息："+StringHelper.formatList(loseCols, "、")+"");
				}
			}
		}
		if(checkCols.size()>0){
			result.put("success", "false");
			result.put("msg", StringHelper.formatList(checkCols, ";")+"");
			return result;
		}
		
		DataTable dataTableXM=null;
		DataTable dataTableQL=null;
		DataTable dataTableDYQ=null;
		DataTable dataTableQLR=null;
		
		//必填值缺失
		List<String> checkValues=new ArrayList<String>();
		Set<Integer> setSheet2 = sheetInfoList.keySet();
		Iterator<Integer> iteratorSheet2 = setSheet2.iterator();
		while (iteratorSheet2.hasNext()) {
			Integer sheetNum = iteratorSheet2.next();
			String sheetName = sheetInfoList.get(sheetNum);
			List<String> colsRequired=GetRequiredInfoEx(sheetName,true);
			if(colsRequired.size()>0){
				DataTable dataTable=cellinfolist.get(sheetNum);
				HashMap<String,Object> loseInfo=CheckValueReuiredEx(colsRequired,dataTable,sheetName);
				List<String> loseValues=(List<String>)loseInfo.get("loseValues");
				dataTableXM=(DataTable)loseInfo.get("dataTableXM");
				dataTableQL=(DataTable)loseInfo.get("dataTableQL");
				dataTableDYQ=(DataTable)loseInfo.get("dataTableDYQ");
				dataTableQLR=(DataTable)loseInfo.get("dataTableQLR");
				if(loseValues.size()>0){
					checkValues.add(sheetName+"中："+StringHelper.formatList(loseValues, ";"));
				}
			}
		}
		if(checkValues.size()>0){
			result.put("success", "false");
			result.put("msg", "必填值校验："+StringHelper.formatList(checkValues, ";")+"");
			return result;
		}
		
		List<HashMap<String,String>> checkAccept=new ArrayList<HashMap<String,String>>();
		if(dataTableXM!=null&&dataTableXM.getRows().size()>0){
			DataColumn dataColumnDyh=new DataColumn();
			dataColumnDyh.setColumnName("BDCDYID");
			dataColumnDyh.setDataType(0);
			dataColumnDyh.setLabel("不动产单元号");
			dataTableXM.getColumns().add(dataColumnDyh);
			
			DataColumn dataColumnZT=new DataColumn();
			dataColumnZT.setColumnName("ZT");
			dataColumnZT.setDataType(0);
			dataColumnZT.setLabel("状态");
			dataTableXM.getColumns().add(dataColumnZT);
			for(int i=0;i<dataTableXM.getRows().size();i++){
				HashMap<String,String> detailInfo=new HashMap<String, String>();
				String bdcdyh="";
				try {
					bdcdyh = StringHelper.formatObject(dataTableXM.getRows().get(i).getObject("BDCDYH"));
				} catch (DataException e) {
				}
				try {
					detailInfo.put("项目标识", StringHelper.formatObject(dataTableXM.getRows().get(i).getObject("XMID")));
				} catch (DataException e) {
				}
				try {
					detailInfo.put("项目名称", StringHelper.formatObject(dataTableXM.getRows().get(i).getObject("XMMC")));
				} catch (DataException e) {
				}
				detailInfo.put("不动产单元号", bdcdyh);
				RegisterWorkFlow baseWorkflow=HandlerFactory.getMapping().getWorkflowByName(baseWorkflowName);
				List<RealUnit> listUnit=UnitTools.loadUnits(BDCDYLX.initFrom(baseWorkflow.getUnittype()), DJDYLY.XZ, "BDCDYH='"+bdcdyh+"'");
				if(listUnit==null||listUnit.size()<=0){
					try {
						dataTableXM.getRows().get(i).setString("ZT", "-1");
					} catch (Exception e) {
					}
					detailInfo.put("BDCDYID", "");
					detailInfo.put("状态", "严重");
					detailInfo.put("说明", "单元不存在！");
				}else if(listUnit.size()>1){
					try {
						dataTableXM.getRows().get(i).setString("ZT", "-1");
					} catch (Exception e) {
					}
					detailInfo.put("BDCDYID", "");
					detailInfo.put("状态", "严重");
					detailInfo.put("说明", "存在多个单元！");
				}else{
					try {
						dataTableXM.getRows().get(i).setString("BDCDYID", listUnit.get(0).getId());
					} catch (Exception e) {
					}
					ResultMessage ms =constraintcheck.acceptCheckByBDCDYIDByWorkflowCode(listUnit.get(0).getId(),workflowCode);
					if (("false").equals(ms.getSuccess())) {
						try {
							dataTableXM.getRows().get(i).setString("ZT", "-1");
						} catch (Exception e) {
						}
						detailInfo.put("状态", "严重"); 
						detailInfo.put("说明", ms.getMsg());
					} else if (("warning").equals(ms.getSuccess())) {
						try {
							dataTableXM.getRows().get(i).setString("ZT", "0");
						} catch (Exception e) {
						}
						detailInfo.put("状态", "警告");
						detailInfo.put("说明", ms.getMsg());
					}else{
						try {
							dataTableXM.getRows().get(i).setString("ZT", "1");
						} catch (Exception e) {
						}
						detailInfo.put("状态", "校验通过");
						detailInfo.put("说明", "可受理");
					}
					detailInfo.put("BDCDYID", listUnit.get(0).getId());
				}
				if(!detailInfo.containsKey("状态")){
					try {
						dataTableXM.getRows().get(i).setString("ZT", "-1");
					} catch (Exception e) {
					}
					detailInfo.put("BDCDYID", "");
					detailInfo.put("状态", "严重");
					detailInfo.put("说明", "校验失败");
				}
				checkAccept.add(detailInfo);
			}
		}
		excelInfo.put("dataTableXM", dataTableXM);
		excelInfo.put("dataTableDYQ", dataTableDYQ);
		excelInfo.put("dataTableQL", dataTableQL);
		excelInfo.put("dataTableQLR", dataTableQLR);
		result.put("success", "true");
		result.put("msg", checkAccept);
		return result;
	}
	
	/*
	 * 必须登记信息及属性及其相应必填性控制
	 */
	private List<String> GetRequiredInfoEx(String requeiredInfo,boolean bVaule){
		List<String> listRequired=new ArrayList<String>();
		if("YG002".equals(requeiredInfo)||"YG102".equals(requeiredInfo)){
			if(bVaule){
				listRequired.add("项目标识");
				listRequired.add("项目名称");
				listRequired.add("不动产单元号");
			}else{
				listRequired.add("预购商品房预告与抵押权预告合并登记");
			}
		}else if("YG001".equals(requeiredInfo)){
			if(bVaule){
				listRequired.add("项目标识");
				listRequired.add("项目名称");
				listRequired.add("不动产单元号");
			}else{
				listRequired.add("预购商品房预告登记");
			}
		}else if("YG003".equals(requeiredInfo)){
			if(bVaule){
				listRequired.add("项目标识");
				listRequired.add("项目名称");
				listRequired.add("不动产单元号");
			}else{
				listRequired.add("预购商品房抵押权预告登记");
			}
		}else if("CS013".equals(requeiredInfo)){
			if(bVaule){
				listRequired.add("项目标识");
				listRequired.add("项目名称");
				listRequired.add("不动产单元号");
			}else{
				listRequired.add("在建工程抵押登记");
			}
		}else if("ZY002".equals(requeiredInfo)||"ZY102".equals(requeiredInfo)||"ZY007".equals(requeiredInfo)){
			if(bVaule){
				listRequired.add("项目标识");
				listRequired.add("项目名称");
				listRequired.add("不动产单元号");
			}else{
				listRequired.add("商品房转移登记");
			}
		}else if("CS011".equals(requeiredInfo)){
			if(bVaule){
				listRequired.add("项目标识");
				listRequired.add("项目名称");
				listRequired.add("不动产单元号");
			}else{
				listRequired.add("现房抵押登记");
			}
		}else if("商品房转移登记".equals(requeiredInfo)){
			listRequired.add("项目标识");
			listRequired.add("不动产单元号");
			listRequired.add("持证方式");
			listRequired.add("申请人名称");
			listRequired.add("申请人类别");
			listRequired.add("证件类型");
			listRequired.add("证件号");
		}else if("现房抵押登记".equals(requeiredInfo)||"在建工程抵押登记".equals(requeiredInfo)){
			listRequired.add("项目标识");
			listRequired.add("不动产单元号");
			listRequired.add("抵押债权起始时间");
			listRequired.add("抵押债权结束时间");
			listRequired.add("抵押方式");
			listRequired.add("被担保债权数额(最高债权数额)");
			listRequired.add("申请人名称");
			listRequired.add("申请人类别");
			listRequired.add("证件类型");
			listRequired.add("证件号");
		}else if("预购商品房预告与抵押权预告合并登记".equals(requeiredInfo)||"预购商品房抵押权预告登记".equals(requeiredInfo)){
			listRequired.add("项目标识");
			listRequired.add("不动产单元号");
			listRequired.add("抵押债权起始时间");
			listRequired.add("抵押债权结束时间");
			listRequired.add("抵押方式");
			listRequired.add("被担保债权数额(最高债权数额)");
			listRequired.add("申请人名称");
			listRequired.add("申请人类别");
			listRequired.add("证件类型");
			listRequired.add("证件号");
		}else if("预购商品房预告登记".equals(requeiredInfo)){
			listRequired.add("项目标识");
			listRequired.add("不动产单元号");
			listRequired.add("申请人名称");
			listRequired.add("申请人类别");
			listRequired.add("证件类型");
			listRequired.add("证件号");
		}
		return listRequired;
	}
	
	/*
	 * 必须登记信息及属性是否存在检查
	 */
	private List<String> CheckReuiredEx(List<String> listRequired,DataTable dataTable){
		List<String> loseInfo=new ArrayList<String>();
		for(int i=0;i<listRequired.size();i++){
			loseInfo.add(listRequired.get(i));
		}
		if(dataTable!=null&&dataTable.getColumns().size()>0){
			for(int i=0;i<dataTable.getColumns().size();i++){
				String title=dataTable.getColumns().get(i).getLabel();
				if(loseInfo.contains(title)){
					loseInfo.remove(title);
				}
			}
		}
		return loseInfo;
	}
	
	/*
	 * 必须登记信息及属性值必填检查
	 */
	private HashMap<String,Object> CheckValueReuiredEx(List<String> listRequired,DataTable dataTable,String sheetName){
		HashMap<String,Object> loseInfo=new HashMap<String, Object>();
		DataTable dataTableXM=null;
		DataTable dataTableQL=null;
		DataTable dataTableDYQ=null;
		DataTable dataTableQLR=null;
		if(dataTable!=null&&dataTable.getColumns().size()>0){
			for(int i=0;i<dataTable.getColumns().size();i++){
				String label=dataTable.getColumns().get(i).getLabel();
				String entityName=GetFieldNameEx(sheetName,label,"Entity");
				if("XM".equals(entityName)){
					if(dataTableXM==null){
						dataTableXM=new DataTable();
					}
					DataColumn dataColumn=new DataColumn();
					dataColumn.setColumnName(dataTable.getColumns().get(i).getColumnName());
					dataColumn.setDataType(dataTable.getColumns().get(i).getDataType());
					dataColumn.setLabel(dataTable.getColumns().get(i).getLabel());
					dataTableXM.getColumns().add(dataColumn);
				}
				if("QL".equals(entityName)){
					if(dataTableQL==null){
						dataTableQL=new DataTable();
					}
					DataColumn dataColumn=new DataColumn();
					dataColumn.setColumnName(dataTable.getColumns().get(i).getColumnName());
					dataColumn.setDataType(dataTable.getColumns().get(i).getDataType());
					dataColumn.setLabel(dataTable.getColumns().get(i).getLabel());
					dataTableQL.getColumns().add(dataColumn);
				}
				if("DYQ".equals(entityName)){
					if(dataTableDYQ==null){
						dataTableDYQ=new DataTable();
					}
					DataColumn dataColumn=new DataColumn();
					dataColumn.setColumnName(dataTable.getColumns().get(i).getColumnName());
					dataColumn.setDataType(dataTable.getColumns().get(i).getDataType());
					dataColumn.setLabel(dataTable.getColumns().get(i).getLabel());
					dataTableDYQ.getColumns().add(dataColumn);
				}
				if("QLR".equals(entityName)){
					if(dataTableQLR==null){
						dataTableQLR=new DataTable();
					}
					DataColumn dataColumn=new DataColumn();
					dataColumn.setColumnName(dataTable.getColumns().get(i).getColumnName());
					dataColumn.setDataType(dataTable.getColumns().get(i).getDataType());
					dataColumn.setLabel(dataTable.getColumns().get(i).getLabel());
					dataTableQLR.getColumns().add(dataColumn);
				}
			}
			if(dataTableXM!=null){
				DataColumn dataColumn=new DataColumn();
				dataColumn.setColumnName("BDCDYH");
				dataColumn.setDataType(0);
				dataColumn.setLabel("不动产单元号");
				dataTableXM.getColumns().add(dataColumn);
			}
			if(dataTableQL!=null){
				DataColumn dataColumn=new DataColumn();
				dataColumn.setColumnName("BDCDYH");
				dataColumn.setDataType(0);
				dataColumn.setLabel("不动产单元号");
				dataTableQL.getColumns().add(dataColumn);
			}
			if(dataTableDYQ!=null){
				DataColumn dataColumn=new DataColumn();
				dataColumn.setColumnName("BDCDYH");
				dataColumn.setDataType(0);
				dataColumn.setLabel("不动产单元号");
				dataTableDYQ.getColumns().add(dataColumn);
			}
			if(dataTableQLR!=null){
				DataColumn dataColumn=new DataColumn();
				dataColumn.setColumnName("BDCDYH");
				dataColumn.setDataType(0);
				dataColumn.setLabel("不动产单元号");
				dataTableQLR.getColumns().add(dataColumn);
			}
		}
		
		if(dataTable!=null&&dataTable.getRows().size()>0){
			String preBdcdyh="";
			List<String> listBdcdyh=new ArrayList<String>();
			for(int i=0;i<dataTable.getRows().size();i++){
				DataRow dataRow=dataTable.getRows().get(i);
				String bdcdyh="";
				try {
					bdcdyh = dataRow.getString("BDCDYH");
				} catch (DataException e3) {
				}
				if(StringHelper.isEmpty(bdcdyh)){
					bdcdyh=preBdcdyh;
				}
				preBdcdyh=bdcdyh;
				if(!listBdcdyh.contains(bdcdyh)){
					if(dataTableXM!=null){
						DataRow dataRowXM=dataTableXM.newRow();
						for(int j=0;j<dataTableXM.getColumns().size();j++){
							String colName=dataTableXM.getColumns().get(j).getColumnName();
							Object val="";
							try {
								val = dataTable.getRows().get(i).getObject(colName);
							} catch (DataException e1) {
							}
							val=GetConstValue(colName,StringHelper.formatObject(val));
							try {
								dataRowXM.setObject(colName, val);
							} catch (DataException e) {
							}
						}
						try {
							dataTableXM.getRows().add(dataRowXM);
						} catch (Exception e) {
						}
					}
					if(dataTableDYQ!=null){
						DataRow dataRowDYQ=dataTableDYQ.newRow();
						for(int j=0;j<dataTableDYQ.getColumns().size();j++){
							String colName=dataTableDYQ.getColumns().get(j).getColumnName();
							Object val=null;
							try {
								val = dataTable.getRows().get(i).getObject(colName);
							} catch (DataException e1) {
							}
							val=GetConstValue(colName,StringHelper.formatObject(val));
							try {
								dataRowDYQ.setObject(colName, val);
							} catch (DataException e) {
							}
						}
						try {
							dataTableDYQ.getRows().add(dataRowDYQ);
						} catch (Exception e) {
						}
					}
					if(dataTableQL!=null){
						DataRow dataRowQL=dataTableQL.newRow();
						for(int j=0;j<dataTableQL.getColumns().size();j++){
							String colName=dataTableQL.getColumns().get(j).getColumnName();
							Object val=null;
							try {
								val = dataTable.getRows().get(i).getObject(colName);
							} catch (DataException e1) {
							}
							val=GetConstValue(colName,StringHelper.formatObject(val));
							try {
								dataRowQL.setObject(colName, val);
							} catch (DataException e) {
							}
						}
						try {
							dataTableQL.getRows().add(dataRowQL);
						} catch (Exception e) {
						}
					}
					listBdcdyh.add(bdcdyh);
				}
				if(dataTableQLR!=null){
					DataRow dataRowQLR=dataTableQLR.newRow();
					for(int j=0;j<dataTableQLR.getColumns().size();j++){
						String colName=dataTableQLR.getColumns().get(j).getColumnName();
						Object val=null;
						try {
							val = dataTable.getRows().get(i).getObject(colName);
						} catch (DataException e1) {
						}
						val=GetConstValue(colName,StringHelper.formatObject(val));
						if("BDCDYH".equals(colName)){
							val=bdcdyh;
						}
						try {
							dataRowQLR.setObject(colName, val);
						} catch (DataException e) {
						}
					}
					try {
						String sqrxm=StringHelper.formatObject(dataRowQLR.getObject("SQRXM"));
						if(!StringHelper.isEmpty(sqrxm)){
							dataTableQLR.getRows().add(dataRowQLR);
						}
						
					} catch (Exception e) {
					}
				}
			}
		}
		List<String> loseCol=new ArrayList<String>();
		for(String requiredCol:listRequired){
			String entityName=GetFieldNameEx(sheetName,requiredCol,"Entity");
			String fieldName=GetFieldNameEx(sheetName,requiredCol,"Field");
			if("XM".equals(entityName)){
				if(dataTableXM!=null&&dataTableXM.getRows().size()>0){
					for(int i=0;i<dataTableXM.getRows().size();i++){
						String val="";
						try{
							val=StringHelper.formatObject(dataTableXM.getRows().get(i).getObject(fieldName));
						}catch(Exception ex){
							
						}
						if(StringHelper.isEmpty(val)){
							if(!loseCol.contains(requiredCol)){
								loseCol.add(requiredCol);
							}
						}
					}
				}
			}else if("QL".equals(entityName)){
				if(dataTableQL!=null&&dataTableQL.getRows().size()>0){
					for(int i=0;i<dataTableQL.getRows().size();i++){
						String val="";
						try{
							val=StringHelper.formatObject(dataTableQL.getRows().get(i).getObject(fieldName));
						}catch(Exception ex){
							
						}
						if(StringHelper.isEmpty(val)){
							if(!loseCol.contains(requiredCol)){
								loseCol.add(requiredCol);
							}
						}
					}
				}
			}else if("DYQ".equals(entityName)){
				if(dataTableDYQ!=null&&dataTableDYQ.getRows().size()>0){
					for(int i=0;i<dataTableDYQ.getRows().size();i++){
						String val="";
						try{
							val=StringHelper.formatObject(dataTableDYQ.getRows().get(i).getObject(fieldName));
						}catch(Exception ex){
							
						}
						
						if(StringHelper.isEmpty(val)){
							if(!loseCol.contains(requiredCol)){
								loseCol.add(requiredCol);
							}
						}
					}
				}
			}else if("QLR".equals(entityName)){
				if(dataTableQLR!=null&&dataTableQLR.getRows().size()>0){
					for(int i=0;i<dataTableQLR.getRows().size();i++){
						String val="";
						try{
							val=StringHelper.formatObject(dataTableQLR.getRows().get(i).getObject(fieldName));
						}catch(Exception ex){
							
						}
						
						if(StringHelper.isEmpty(val)){
							if(!loseCol.contains(requiredCol)){
								loseCol.add(requiredCol);
							}
						}
					}
				}
			}
			
		}
		
		List<String> loseSheetInfo=new ArrayList<String>();
		if(loseCol.size()>0){
			loseSheetInfo.add(StringHelper.formatList(loseCol, "、")+"为必填项！");
		}
		loseInfo.put("loseValues", loseSheetInfo);
		loseInfo.put("dataTableXM", dataTableXM);
		loseInfo.put("dataTableDYQ", dataTableDYQ);
		loseInfo.put("dataTableQL", dataTableQL);
		loseInfo.put("dataTableQLR", dataTableQLR);
		return loseInfo;
	}
	
	/*****************************************Excel批量受理Ex*****************************************/
	
	
	/*
	 * 根据流程定义id和批次号及excel解析信息受理项目（现房转移项目:ZY002/ZY102）
	 */
	private HashMap<String,Object> AcceptProjectExcelZY002Ex(HttpServletRequest request,String prodef_id,
			String batchNumber,HashMap<String,Object> excelInfo,HttpServletResponse response){
		HashMap<String,String> dyh_xmid=new HashMap<String,String>();
		HashMap<String,String> dyid_dyh=new HashMap<String,String>();
		HashMap<String,HashMap<String,String>> dyh_qlinfo=new HashMap<String,HashMap<String,String>>();
		
		HashMap<String,List<String>> dyh_qlridlist=new HashMap<String,List<String>>();
		
		HashMap<String,List<String>> xmid_qlridlist=new HashMap<String,List<String>>();
		
		HashMap<String,HashMap<String,String>> sqrid_qlrxx=new HashMap<String,HashMap<String,String>>();
		List<HashMap<String,String>> list_ywrxx=new ArrayList<HashMap<String,String>>();
		
		HashMap<String,Object> result=new HashMap<String, Object>();
		
		DataTable dataTableXM=(DataTable)excelInfo.get("dataTableXM");
		DataTable dataTableQL=(DataTable)excelInfo.get("dataTableQL");
		DataTable dataTableQLR=(DataTable)excelInfo.get("dataTableQLR");
		
		List<String> xmidlist=new ArrayList<String>();
		HashMap<String,HashMap<String,String>> xmid_xminfo=new HashMap<String, HashMap<String,String>>();
		HashMap<String,List<String>> xmid_dyidlist=new HashMap<String,List<String>>();
 		for(int i=0;i<dataTableXM.getRows().size();i++){
 			String zt="";
			try {
				zt = dataTableXM.getRows().get(i).getString("ZT");
			} catch (Exception e2) {
			}
			if(!"0".equals(zt)&&!"1".equals(zt)){
				continue;
			}
			String xmid="";
			try {
				xmid = dataTableXM.getRows().get(i).getString("XMID");
			} catch (Exception e2) {
			}
			String dyh="";
			try {
				dyh = dataTableXM.getRows().get(i).getString("BDCDYH");
			} catch (Exception e1) {
			}
			String dyid="";
			try {
				dyid = dataTableXM.getRows().get(i).getString("BDCDYID");
			} catch (Exception e1) {
			}
			dyh_xmid.put(dyh, xmid);
			dyid_dyh.put(dyid, dyh);
			if(xmid_dyidlist.containsKey(xmid)){
				List<String> dyidlist=xmid_dyidlist.get(xmid);
				if(!dyidlist.contains(dyid)){
					dyidlist.add(dyid);
				}
				xmid_dyidlist.put(xmid, dyidlist);
			}else{
				List<String> dyidlist=new ArrayList<String>();
				if(!dyidlist.contains(dyid)){
					dyidlist.add(dyid);
				}
				xmid_dyidlist.put(xmid, dyidlist);
			}
			if(!xmidlist.contains(xmid)){
				xmidlist.add(xmid);
				HashMap<String,String> xminfo=new HashMap<String, String>();
				String xmmc="";
				try {
					xmmc = dataTableXM.getRows().get(i).getString("XMMC");
				} catch (Exception e) {
				}
				String sfhbfz="0";
				xminfo.put("XMMC", xmmc);
				xminfo.put("SFHBZS", sfhbfz);
				xmid_xminfo.put(xmid, xminfo);
			}
		}
 		
 		for(int i=0;i<dataTableQL.getRows().size();i++){
			String dyh="";
			try {
				dyh = dataTableQL.getRows().get(i).getString("BDCDYH");
			} catch (Exception e1) {
			}
			if(!dyid_dyh.containsValue(dyh)){
				continue;
			}
			HashMap<String,String> qlinfo=new HashMap<String, String>();
			for(int iCol=0;iCol<dataTableQL.getColumns().size();iCol++){
				String fieldName=dataTableQL.getColumns().get(iCol).getColumnName();
				String fieldValue="";
				try {
					fieldValue = dataTableQL.getRows().get(i).getString(iCol);
				} catch (Exception e) {
				}
				qlinfo.put(fieldName, fieldValue);
			}
			dyh_qlinfo.put(dyh, qlinfo);
		}
 		HashMap<String,HashMap<String,String>> xmid_qlrmczjh_sqrid=new HashMap<String, HashMap<String,String>>();
 		for(int i=0;i<dataTableQLR.getRows().size();i++){
 			String sqrlb="";
			try {
				sqrlb = dataTableQLR.getRows().get(i).getString("SQRLB");
			} catch (Exception e1) {
			}
 			if("义务人".equals(sqrlb)){
 				HashMap<String,String> ywrinfo=new HashMap<String, String>();
 				for(int iCol=0;iCol<dataTableQLR.getColumns().size();iCol++){
 					String fieldName=dataTableQLR.getColumns().get(iCol).getColumnName();
 					String fieldValue="";
 					try {
 						fieldValue = dataTableQLR.getRows().get(i).getString(iCol);
 					} catch (Exception e) {
 					}
 					ywrinfo.put(fieldName, fieldValue);
 				}
 				list_ywrxx.add(ywrinfo);
 				continue;
 			}
 			
			String dyh="";
			try {
				dyh = dataTableQLR.getRows().get(i).getString("BDCDYH");
			} catch (Exception e1) {
			}
			if(!dyid_dyh.containsValue(dyh)){
				continue;
			}
			String xmid=dyh_xmid.get(dyh);
			
			HashMap<String,String> qlrinfo=new HashMap<String, String>();
			String sqrxm="";
			String zjh="";
			for(int iCol=0;iCol<dataTableQLR.getColumns().size();iCol++){
				String fieldName=dataTableQLR.getColumns().get(iCol).getColumnName();
				String fieldValue="";
				try {
					fieldValue = dataTableQLR.getRows().get(i).getString(iCol);
				} catch (Exception e) {
				}
				if("SQRXM".equals(fieldName)){
					sqrxm=fieldValue;
				}
				if("zjh".equals(fieldName)){
					zjh=fieldValue;
				}
				qlrinfo.put(fieldName, fieldValue);
			}
			String sqrid=SuperHelper.GeneratePrimaryKey();
			if(xmid_qlrmczjh_sqrid.containsKey(xmid)&&xmid_qlrmczjh_sqrid.get(xmid).containsKey(sqrxm+"-"+zjh)){
				sqrid=xmid_qlrmczjh_sqrid.get(xmid).get(sqrxm+"-"+zjh);
			}else{
				if(xmid_qlrmczjh_sqrid.containsKey(xmid)){
					HashMap<String,String> qlrmczjh_sqrid=xmid_qlrmczjh_sqrid.get(xmid);
					qlrmczjh_sqrid.put(sqrxm+"-"+zjh, sqrid);
					xmid_qlrmczjh_sqrid.put(xmid, qlrmczjh_sqrid);
				}else{
					HashMap<String,String> qlrmczjh_sqrid=new HashMap<String, String>();
					qlrmczjh_sqrid.put(sqrxm+"-"+zjh, sqrid);
					xmid_qlrmczjh_sqrid.put(xmid, qlrmczjh_sqrid);
				}
				sqrid_qlrxx.put(sqrid, qlrinfo);
			}
			if(dyh_qlridlist.containsKey(dyh)){
				List<String> qlridlist=dyh_qlridlist.get(dyh);
				if(!qlridlist.contains(sqrid)){
					qlridlist.add(sqrid);
				}
				dyh_qlridlist.put(dyh, qlridlist);
			}else{
				List<String> qlridlist=new ArrayList<String>();
				if(!qlridlist.contains(sqrid)){
					qlridlist.add(sqrid);
				}
				dyh_qlridlist.put(dyh, qlridlist);
			}
			
			if(xmid_qlridlist.containsKey(xmid)){
				List<String> qlridlist=xmid_qlridlist.get(xmid);
				if(!qlridlist.contains(sqrid)){
					qlridlist.add(sqrid);
				}
				xmid_qlridlist.put(xmid, qlridlist);
			}else{
				List<String> qlridlist=new ArrayList<String>();
				if(!qlridlist.contains(sqrid)){
					qlridlist.add(sqrid);
				}
				xmid_qlridlist.put(xmid, qlridlist);
			}
			
		}
 		
		List<HashMap<String,String>> acceptinfolist=new ArrayList<HashMap<String,String>>();
		result.put("BATCHNUM", batchNumber);
		int iXh=1;
		for(String xmid:xmidlist){
			///创建项目
			BDCS_XMXX xmxx= CreateProject(request,batchNumber,prodef_id, response);
			if(xmxx==null){
				break;
			}
			///项目信息
			HashMap<String,String> xminfo=xmid_xminfo.get(xmid);
			
			//更新项目信息
			StringHelper.setValue(xminfo, xmxx);
			
			List<Wfi_ProInst> list_proinst=baseCommonDao.getDataList(Wfi_ProInst.class, "FILE_NUMBER='"+xmxx.getPROJECT_ID()+"'");
			if(list_proinst!=null&&list_proinst.size()>0){
				Wfi_ProInst proinst=list_proinst.get(0);
				proinst.setProject_Name(xmxx.getXMMC());
				baseCommonDao.save(proinst);
			}
			
			baseCommonDao.save(xmxx);
			baseCommonDao.flush();
			Global.removeXMXX(xmxx.getPROJECT_ID());
			
			HashMap<String,String> acceptinfo=new HashMap<String, String>();
			acceptinfo.put("XMMC", xmxx.getXMMC());
			acceptinfo.put("YWLSH", xmxx.getYWLSH());
			//不动产单元id列表
			List<String> dyidlist=xmid_dyidlist.get(xmid);
			
			if(dyid_dyh!=null&&dyid_dyh.size()>0){
				String dyhlist=StringHelper.formatList(Arrays.asList(dyid_dyh.values().toArray(new String[dyid_dyh.size()])),"、");
				acceptinfo.put("DYHLIST", dyhlist);
			}else{
				acceptinfo.put("DYHLIST", "");
			}
			acceptinfo.put("SLRY", xmxx.getSLRY());
			acceptinfo.put("SLSJ", StringHelper.FormatDateOnType(xmxx.getSLSJ(), "yyyy-MM-dd"));
			acceptinfo.put("XH", StringHelper.formatObject(iXh));
			acceptinfo.put("XMID", xmid);
			//添加单元
			dyService.addBDCDYNoCheck(xmxx.getId(), StringHelper.formatList(dyidlist, ","));
			baseCommonDao.flush();
			//构建登记单元id和不动产单元id关系
			HashMap<String,String> djdyid_bdcdyid=new HashMap<String, String>();
			List<BDCS_DJDY_GZ> djdys=baseCommonDao.getDataList(BDCS_DJDY_GZ.class, "XMBH='"+xmxx.getId()+"'");
			for(BDCS_DJDY_GZ djdy:djdys){
				if(!djdyid_bdcdyid.containsKey(djdy.getDJDYID())){
					djdyid_bdcdyid.put(djdy.getDJDYID(), djdy.getBDCDYID());
				}
			}
			//移除原申请人(未关联权利)
			List<BDCS_SQR> sqrlist_old=baseCommonDao.getDataList(BDCS_SQR.class, " id not in (SELECT  SQRID FROM BDCS_QLR_GZ WHERE XMBH='"+xmxx.getId()+"') AND XMBH='"+xmxx.getId()+"'");
			if(sqrlist_old!=null&&sqrlist_old.size()>0){
				for(BDCS_SQR sqr_old:sqrlist_old){
					baseCommonDao.deleteEntity(sqr_old);
				}
			}
			baseCommonDao.flush();
			List<String> sqridlist=xmid_qlridlist.get(xmid);
			///添加申请人
			for(String sqrid:sqridlist){
				if(!sqrid_qlrxx.containsKey(sqrid)){
					continue;
				}
				HashMap<String,String> sqrinfo=sqrid_qlrxx.get(sqrid);
				BDCS_SQR sqr = new BDCS_SQR();
				StringHelper.setValue(sqrinfo, sqr);
				sqr.setId(sqrid);
				sqr.setSQRLB(SQRLB.JF.Value);
				sqr.setXMBH(xmxx.getId());
				baseCommonDao.save(sqr);
			}
			///添加义务人
			if(list_ywrxx!=null&&list_ywrxx.size()>0){
				for(HashMap<String,String> ywrinfo:list_ywrxx){
					String sqrid=SuperHelper.GeneratePrimaryKey();
					BDCS_SQR sqr = new BDCS_SQR();
					StringHelper.setValue(ywrinfo, sqr);
					sqr.setId(sqrid);
					sqr.setSQRLB(SQRLB.YF.Value);
					sqr.setXMBH(xmxx.getId());
					baseCommonDao.save(sqr);
				}
			}
			
			baseCommonDao.flush();
			//获取权利和附属权利信息
			List<Rights> qls=RightsTools.loadRightsByCondition(DJDYLY.GZ, "XMBH='"+xmxx.getId()+"'");
			List<SubRights> fsqls=RightsTools.loadSubRightsByCondition(DJDYLY.GZ, "XMBH='"+xmxx.getId()+"'");
			for(Rights ql:qls){
				String djdyid=ql.getDJDYID();
				if(!djdyid_bdcdyid.containsKey(djdyid)){
					continue;
				}
				String bdcdyid=djdyid_bdcdyid.get(djdyid);
				if(!dyid_dyh.containsKey(bdcdyid)){
					continue;
				}
				String bdcdyh=dyid_dyh.get(bdcdyid);
				if(!QLLX.DIYQ.Value.equals(ql.getQLLX())){
					///更新权利
					HashMap<String,String> qlinfo=dyh_qlinfo.get(bdcdyh);
					StringHelper.setValue(qlinfo, ql);
					baseCommonDao.update(ql);
					for(SubRights fsql:fsqls){
						if(ql.getId().equals(fsql.getQLID())||fsql.getId().equals(ql.getFSQLID())){
							StringHelper.setValue(qlinfo, fsql);
							baseCommonDao.update(fsql);
						}
					}
					baseCommonDao.flush();
					///添加权利人
					if(dyh_qlridlist.containsKey(bdcdyh)){
						List<String> qlridlist=dyh_qlridlist.get(bdcdyh);
						qlService.addQLRfromSQR(xmxx.getId(), ql.getId(),
								qlridlist.toArray());
					}
				}
				baseCommonDao.flush();
			}
			acceptinfo.put("DYGS", StringHelper.formatObject(djdys.size()));
			acceptinfo.put("BATCHNUM", batchNumber);
			acceptinfolist.add(acceptinfo);
			iXh++;
		}
		result.put("acceptinfolist", acceptinfolist);
		result.put("batchnum", batchNumber);
		result.put("success", "true");
		excelInfo.remove("dataTableXM");
		excelInfo.remove("dataTableQL");
		excelInfo.remove("dataTableQLR");
		excelInfo.remove("cellinfolist");
		return result;
	}
	
	/*
	 * 根据流程定义id和批次号及excel解析信息受理项目（现房转移项目:CS011）
	 */
	@SuppressWarnings("unused")
	private HashMap<String,Object> AcceptProjectExcelCS011Ex(HttpServletRequest request,String prodef_id,
			String batchNumber,HashMap<String,Object> excelInfo,HttpServletResponse response){
		HashMap<String,String> dyh_xmid=new HashMap<String,String>();
		HashMap<String,String> dyid_dyh=new HashMap<String,String>();
		HashMap<String,HashMap<String,String>> dyh_qlinfo=new HashMap<String,HashMap<String,String>>();
		
		HashMap<String,List<String>> dyh_qlridlist=new HashMap<String,List<String>>();
		
		HashMap<String,List<String>> xmid_qlridlist=new HashMap<String,List<String>>();
		
		HashMap<String,HashMap<String,String>> sqrid_qlrxx=new HashMap<String,HashMap<String,String>>();
		List<HashMap<String,String>> list_ywrxx=new ArrayList<HashMap<String,String>>();
		
		HashMap<String,Object> result=new HashMap<String, Object>();
		
		DataTable dataTableXM=(DataTable)excelInfo.get("dataTableXM");
		DataTable dataTableDYQ=(DataTable)excelInfo.get("dataTableDYQ");
		DataTable dataTableQLR=(DataTable)excelInfo.get("dataTableQLR");
		
		List<String> xmidlist=new ArrayList<String>();
		HashMap<String,HashMap<String,String>> xmid_xminfo=new HashMap<String, HashMap<String,String>>();
		HashMap<String,List<String>> xmid_dyidlist=new HashMap<String,List<String>>();
 		for(int i=0;i<dataTableXM.getRows().size();i++){
 			String zt="";
			try {
				zt = dataTableXM.getRows().get(i).getString("ZT");
			} catch (Exception e2) {
			}
			if(!"0".equals(zt)&&!"1".equals(zt)){
				continue;
			}
			String xmid="";
			try {
				xmid = dataTableXM.getRows().get(i).getString("XMID");
			} catch (Exception e2) {
			}
			String dyh="";
			try {
				dyh = dataTableXM.getRows().get(i).getString("BDCDYH");
			} catch (Exception e1) {
			}
			String dyid="";
			try {
				dyid = dataTableXM.getRows().get(i).getString("BDCDYID");
			} catch (Exception e1) {
			}
			dyh_xmid.put(dyh, xmid);
			dyid_dyh.put(dyid, dyh);
			if(xmid_dyidlist.containsKey(xmid)){
				List<String> dyidlist=xmid_dyidlist.get(xmid);
				if(!dyidlist.contains(dyid)){
					dyidlist.add(dyid);
				}
				xmid_dyidlist.put(xmid, dyidlist);
			}else{
				List<String> dyidlist=new ArrayList<String>();
				if(!dyidlist.contains(dyid)){
					dyidlist.add(dyid);
				}
				xmid_dyidlist.put(xmid, dyidlist);
			}
			if(!xmidlist.contains(xmid)){
				xmidlist.add(xmid);
				HashMap<String,String> xminfo=new HashMap<String, String>();
				String xmmc="";
				try {
					xmmc = dataTableXM.getRows().get(i).getString("XMMC");
				} catch (Exception e) {
				}
				String sfhbfz="0";
				xminfo.put("XMMC", xmmc);
				xminfo.put("SFHBZS", sfhbfz);
				xmid_xminfo.put(xmid, xminfo);
			}
		}
 		
 		for(int i=0;i<dataTableDYQ.getRows().size();i++){
			String dyh="";
			try {
				dyh = dataTableDYQ.getRows().get(i).getString("BDCDYH");
			} catch (Exception e1) {
			}
			if(!dyid_dyh.containsValue(dyh)){
				continue;
			}
			HashMap<String,String> qlinfo=new HashMap<String, String>();
			for(int iCol=0;iCol<dataTableDYQ.getColumns().size();iCol++){
				String fieldName=dataTableDYQ.getColumns().get(iCol).getColumnName();
				String fieldValue="";
				try {
					fieldValue = dataTableDYQ.getRows().get(i).getString(iCol);
				} catch (Exception e) {
				}
				if("DYQLQSSJ".equals(fieldName)){
					fieldName="QLQSSJ";
				}
				if("DYQLJSSJ".equals(fieldName)){
					fieldName="QLJSSJ";
				}
				qlinfo.put(fieldName, fieldValue);
			}
			qlinfo.put("ZQDW", "1");
			dyh_qlinfo.put(dyh, qlinfo);
		}
 		HashMap<String,HashMap<String,String>> xmid_qlrmczjh_sqrid=new HashMap<String, HashMap<String,String>>();
 		for(int i=0;i<dataTableQLR.getRows().size();i++){
 			String sqrlb="";
			try {
				sqrlb = dataTableQLR.getRows().get(i).getString("SQRLB");
			} catch (Exception e1) {
			}
 			if("义务人".equals(sqrlb)){
 				HashMap<String,String> ywrinfo=new HashMap<String, String>();
 				for(int iCol=0;iCol<dataTableQLR.getColumns().size();iCol++){
 					String fieldName=dataTableQLR.getColumns().get(iCol).getColumnName();
 					String fieldValue="";
 					try {
 						fieldValue = dataTableQLR.getRows().get(i).getString(iCol);
 					} catch (Exception e) {
 					}
 					ywrinfo.put(fieldName, fieldValue);
 				}
 				list_ywrxx.add(ywrinfo);
 				continue;
 			}
 			
			String dyh="";
			try {
				dyh = dataTableQLR.getRows().get(i).getString("BDCDYH");
			} catch (Exception e1) {
			}
			if(!dyid_dyh.containsValue(dyh)){
				continue;
			}
			String xmid=dyh_xmid.get(dyh);
			
			HashMap<String,String> qlrinfo=new HashMap<String, String>();
			String sqrxm="";
			String zjh="";
			for(int iCol=0;iCol<dataTableQLR.getColumns().size();iCol++){
				String fieldName=dataTableQLR.getColumns().get(iCol).getColumnName();
				String fieldValue="";
				try {
					fieldValue = dataTableQLR.getRows().get(i).getString(iCol);
				} catch (Exception e) {
				}
				if("SQRXM".equals(fieldName)){
					sqrxm=fieldValue;
				}
				if("zjh".equals(fieldName)){
					zjh=fieldValue;
				}
				qlrinfo.put(fieldName, fieldValue);
			}
			String sqrid=SuperHelper.GeneratePrimaryKey();
			if(xmid_qlrmczjh_sqrid.containsKey(xmid)&&xmid_qlrmczjh_sqrid.get(xmid).containsKey(sqrxm+"-"+zjh)){
				sqrid=xmid_qlrmczjh_sqrid.get(xmid).get(sqrxm+"-"+zjh);
			}else{
				if(xmid_qlrmczjh_sqrid.containsKey(xmid)){
					HashMap<String,String> qlrmczjh_sqrid=xmid_qlrmczjh_sqrid.get(xmid);
					qlrmczjh_sqrid.put(sqrxm+"-"+zjh, sqrid);
					xmid_qlrmczjh_sqrid.put(xmid, qlrmczjh_sqrid);
				}else{
					HashMap<String,String> qlrmczjh_sqrid=new HashMap<String, String>();
					qlrmczjh_sqrid.put(sqrxm+"-"+zjh, sqrid);
					xmid_qlrmczjh_sqrid.put(xmid, qlrmczjh_sqrid);
				}
				sqrid_qlrxx.put(sqrid, qlrinfo);
			}
			if(dyh_qlridlist.containsKey(dyh)){
				List<String> qlridlist=dyh_qlridlist.get(dyh);
				if(!qlridlist.contains(sqrid)){
					qlridlist.add(sqrid);
				}
				dyh_qlridlist.put(dyh, qlridlist);
			}else{
				List<String> qlridlist=new ArrayList<String>();
				if(!qlridlist.contains(sqrid)){
					qlridlist.add(sqrid);
				}
				dyh_qlridlist.put(dyh, qlridlist);
			}
			
			if(xmid_qlridlist.containsKey(xmid)){
				List<String> qlridlist=xmid_qlridlist.get(xmid);
				if(!qlridlist.contains(sqrid)){
					qlridlist.add(sqrid);
				}
				xmid_qlridlist.put(xmid, qlridlist);
			}else{
				List<String> qlridlist=new ArrayList<String>();
				if(!qlridlist.contains(sqrid)){
					qlridlist.add(sqrid);
				}
				xmid_qlridlist.put(xmid, qlridlist);
			}
			
		}
 		
		List<HashMap<String,String>> acceptinfolist=new ArrayList<HashMap<String,String>>();
		result.put("BATCHNUM", batchNumber);
		int iXh=1;
		for(String xmid:xmidlist){
			///创建项目
			BDCS_XMXX xmxx= CreateProject(request,batchNumber,prodef_id, response);
			if(xmxx==null){
				break;
			}
			///项目信息
			HashMap<String,String> xminfo=xmid_xminfo.get(xmid);
			
			//更新项目信息
			StringHelper.setValue(xminfo, xmxx);
			
			List<Wfi_ProInst> list_proinst=baseCommonDao.getDataList(Wfi_ProInst.class, "FILE_NUMBER='"+xmxx.getPROJECT_ID()+"'");
			if(list_proinst!=null&&list_proinst.size()>0){
				Wfi_ProInst proinst=list_proinst.get(0);
				proinst.setProject_Name(xmxx.getXMMC());
				baseCommonDao.save(proinst);
			}
			
			baseCommonDao.save(xmxx);
			baseCommonDao.flush();
			Global.removeXMXX(xmxx.getPROJECT_ID());
			
			HashMap<String,String> acceptinfo=new HashMap<String, String>();
			acceptinfo.put("XMMC", xmxx.getXMMC());
			acceptinfo.put("YWLSH", xmxx.getYWLSH());
			//不动产单元id列表
			List<String> dyidlist=xmid_dyidlist.get(xmid);
			
			if(dyid_dyh!=null&&dyid_dyh.size()>0){
				String dyhlist=StringHelper.formatList(Arrays.asList(dyid_dyh.values().toArray(new String[dyid_dyh.size()])),"、");
				acceptinfo.put("DYHLIST", dyhlist);
			}else{
				acceptinfo.put("DYHLIST", "");
			}
			acceptinfo.put("SLRY", xmxx.getSLRY());
			acceptinfo.put("SLSJ", StringHelper.FormatDateOnType(xmxx.getSLSJ(), "yyyy-MM-dd"));
			acceptinfo.put("XH", StringHelper.formatObject(iXh));
			acceptinfo.put("XMID", xmid);
			//添加单元
			dyService.addBDCDYNoCheck(xmxx.getId(), StringHelper.formatList(dyidlist, ","));
			baseCommonDao.flush();
			//构建登记单元id和不动产单元id关系
			HashMap<String,String> djdyid_bdcdyid=new HashMap<String, String>();
			List<BDCS_DJDY_GZ> djdys=baseCommonDao.getDataList(BDCS_DJDY_GZ.class, "XMBH='"+xmxx.getId()+"'");
			for(BDCS_DJDY_GZ djdy:djdys){
				if(!djdyid_bdcdyid.containsKey(djdy.getDJDYID())){
					djdyid_bdcdyid.put(djdy.getDJDYID(), djdy.getBDCDYID());
				}
			}
			//移除原申请人
			List<BDCS_SQR> sqrlist_old=baseCommonDao.getDataList(BDCS_SQR.class, "XMBH='"+xmxx.getId()+"'");
			if(sqrlist_old!=null&&sqrlist_old.size()>0){
				for(BDCS_SQR sqr_old:sqrlist_old){
					baseCommonDao.deleteEntity(sqr_old);
				}
			}
			baseCommonDao.flush();
			List<String> sqridlist=xmid_qlridlist.get(xmid);
			///添加申请人
			for(String sqrid:sqridlist){
				if(!sqrid_qlrxx.containsKey(sqrid)){
					continue;
				}
				HashMap<String,String> sqrinfo=sqrid_qlrxx.get(sqrid);
				BDCS_SQR sqr = new BDCS_SQR();
				StringHelper.setValue(sqrinfo, sqr);
				sqr.setId(sqrid);
				sqr.setSQRLB(SQRLB.JF.Value);
				sqr.setXMBH(xmxx.getId());
				baseCommonDao.save(sqr);
			}
			///添加义务人
			if(list_ywrxx!=null&&list_ywrxx.size()>0){
				for(HashMap<String,String> ywrinfo:list_ywrxx){
					String sqrid=SuperHelper.GeneratePrimaryKey();
					BDCS_SQR sqr = new BDCS_SQR();
					StringHelper.setValue(ywrinfo, sqr);
					sqr.setId(sqrid);
					sqr.setSQRLB(SQRLB.YF.Value);
					sqr.setXMBH(xmxx.getId());
					baseCommonDao.save(sqr);
				}
			}
			
			baseCommonDao.flush();
			//获取权利和附属权利信息
			List<Rights> qls=RightsTools.loadRightsByCondition(DJDYLY.GZ, "XMBH='"+xmxx.getId()+"'");
			List<SubRights> fsqls=RightsTools.loadSubRightsByCondition(DJDYLY.GZ, "XMBH='"+xmxx.getId()+"'");
			for(Rights ql:qls){
				String djdyid=ql.getDJDYID();
				if(!djdyid_bdcdyid.containsKey(djdyid)){
					continue;
				}
				String bdcdyid=djdyid_bdcdyid.get(djdyid);
				if(!dyid_dyh.containsKey(bdcdyid)){
					continue;
				}
				String bdcdyh=dyid_dyh.get(bdcdyid);
				///更新权利
				HashMap<String,String> qlinfo=dyh_qlinfo.get(bdcdyh);
				StringHelper.setValue(qlinfo, ql);
				baseCommonDao.update(ql);
				for(SubRights fsql:fsqls){
					if(ql.getId().equals(fsql.getQLID())||fsql.getId().equals(ql.getFSQLID())){
						StringHelper.setValue(qlinfo, fsql);
						Double dyje=StringHelper.getDouble(qlinfo.get("ZQSE"));
						if(DYFS.YBDY.Value.equals(fsql.getDYFS())){
							fsql.setBDBZZQSE(dyje);
							fsql.setZGZQQDSS(null);
						}else if(DYFS.ZGEDY.Value.equals(fsql.getDYFS())){
							fsql.setZGZQSE(dyje);
						}
						baseCommonDao.update(fsql);
					}
				}
				baseCommonDao.flush();
				///添加权利人
				if(dyh_qlridlist.containsKey(bdcdyh)){
					List<String> qlridlist=dyh_qlridlist.get(bdcdyh);
					qlService.addQLRfromSQR(xmxx.getId(), ql.getId(),
							qlridlist.toArray());
				}
				baseCommonDao.flush();
			}
			acceptinfo.put("DYGS", StringHelper.formatObject(djdys.size()));
			acceptinfo.put("BATCHNUM", batchNumber);
			acceptinfolist.add(acceptinfo);
			iXh++;
		}
		result.put("acceptinfolist", acceptinfolist);
		result.put("batchnum", batchNumber);
		result.put("success", "true");
		excelInfo.remove("dataTableXM");
		excelInfo.remove("dataTableDYQ");
		excelInfo.remove("dataTableQLR");
		excelInfo.remove("cellinfolist");
		return result;
	}
	
	/*
	 * 根据流程定义id和批次号及excel解析信息受理项目（现房转移项目:CS011）
	 */
	private HashMap<String,Object> AcceptProjectExcelCS011Ex2(HttpServletRequest request,String prodef_id,
			String batchNumber,HashMap<String,Object> excelInfo,HttpServletResponse response){
		HashMap<String,String> dyh_xmid=new HashMap<String,String>();
		HashMap<String,String> dyid_dyh=new HashMap<String,String>();
		HashMap<String,HashMap<String,String>> dyh_qlinfo=new HashMap<String,HashMap<String,String>>();
		
		HashMap<String,HashMap<String,String>> list_ywrinfo=new HashMap<String,HashMap<String,String>>();
		
		HashMap<String,List<String>> xmid_sqridlist=new HashMap<String, List<String>>();
		
		HashMap<String,List<String>> dyh_sqridlist=new HashMap<String, List<String>>();
		
		List<HashMap<String,String>> list_qlrinfo=new ArrayList<HashMap<String,String>>();
		
		HashMap<String,Object> result=new HashMap<String, Object>();
		
		DataTable dataTableXM=(DataTable)excelInfo.get("dataTableXM");
		DataTable dataTableDYQ=(DataTable)excelInfo.get("dataTableDYQ");
		DataTable dataTableQLR=(DataTable)excelInfo.get("dataTableQLR");
		
		List<String> xmidlist=new ArrayList<String>();
		HashMap<String,HashMap<String,String>> xmid_xminfo=new HashMap<String, HashMap<String,String>>();
		HashMap<String,List<String>> xmid_dyidlist=new HashMap<String,List<String>>();
 		for(int i=0;i<dataTableXM.getRows().size();i++){
 			String zt="";
			try {
				zt = dataTableXM.getRows().get(i).getString("ZT");
			} catch (Exception e2) {
			}
			if(!"0".equals(zt)&&!"1".equals(zt)){
				continue;
			}
			String xmid="";
			try {
				xmid = dataTableXM.getRows().get(i).getString("XMID");
			} catch (Exception e2) {
			}
			String dyh="";
			try {
				dyh = dataTableXM.getRows().get(i).getString("BDCDYH");
			} catch (Exception e1) {
			}
			String dyid="";
			try {
				dyid = dataTableXM.getRows().get(i).getString("BDCDYID");
			} catch (Exception e1) {
			}
			dyh_xmid.put(dyh, xmid);
			dyid_dyh.put(dyid, dyh);
			if(xmid_dyidlist.containsKey(xmid)){
				List<String> dyidlist=xmid_dyidlist.get(xmid);
				if(!dyidlist.contains(dyid)){
					dyidlist.add(dyid);
				}
				xmid_dyidlist.put(xmid, dyidlist);
			}else{
				List<String> dyidlist=new ArrayList<String>();
				if(!dyidlist.contains(dyid)){
					dyidlist.add(dyid);
				}
				xmid_dyidlist.put(xmid, dyidlist);
			}
			if(!xmidlist.contains(xmid)){
				xmidlist.add(xmid);
				HashMap<String,String> xminfo=new HashMap<String, String>();
				String xmmc="";
				try {
					xmmc = dataTableXM.getRows().get(i).getString("XMMC");
				} catch (Exception e) {
				}
				String sfhbfz="0";
				xminfo.put("XMMC", xmmc);
				xminfo.put("SFHBZS", sfhbfz);
				xmid_xminfo.put(xmid, xminfo);
			}
		}
 		
 		for(int i=0;i<dataTableDYQ.getRows().size();i++){
			String dyh="";
			try {
				dyh = dataTableDYQ.getRows().get(i).getString("BDCDYH");
			} catch (Exception e1) {
			}
			if(!dyid_dyh.containsValue(dyh)){
				continue;
			}
			HashMap<String,String> qlinfo=new HashMap<String, String>();
			for(int iCol=0;iCol<dataTableDYQ.getColumns().size();iCol++){
				String fieldName=dataTableDYQ.getColumns().get(iCol).getColumnName();
				String fieldValue="";
				try {
					fieldValue = dataTableDYQ.getRows().get(i).getString(iCol);
				} catch (Exception e) {
				}
				if("DYQLQSSJ".equals(fieldName)){
					fieldName="QLQSSJ";
				}
				if("DYQLJSSJ".equals(fieldName)){
					fieldName="QLJSSJ";
				}
				qlinfo.put(fieldName, fieldValue);
			}
			qlinfo.put("ZQDW", "1");
			dyh_qlinfo.put(dyh, qlinfo);
		}
 		HashMap<String,HashMap<String,String>> xmid_qlrmczjh_sqrid=new HashMap<String, HashMap<String,String>>();
 		for(int i=0;i<dataTableQLR.getRows().size();i++){
 			String sqrlb="";
			try {
				sqrlb = dataTableQLR.getRows().get(i).getString("SQRLB");
			} catch (Exception e1) {
			}
 			if("抵押权人".equals(sqrlb)){
 				HashMap<String,String> qlrinfo=new HashMap<String, String>();
 				for(int iCol=0;iCol<dataTableQLR.getColumns().size();iCol++){
 					String fieldName=dataTableQLR.getColumns().get(iCol).getColumnName();
 					String fieldValue="";
 					try {
 						fieldValue = dataTableQLR.getRows().get(i).getString(iCol);
 					} catch (Exception e) {
 					}
 					qlrinfo.put(fieldName, fieldValue);
 				}
 				list_qlrinfo.add(qlrinfo);
 				continue;
 			}
 			
			String dyh="";
			try {
				dyh = dataTableQLR.getRows().get(i).getString("BDCDYH");
			} catch (Exception e1) {
			}
			if(!dyid_dyh.containsValue(dyh)){
				continue;
			}
			String xmid=dyh_xmid.get(dyh);
			
			HashMap<String,String> ywrinfo=new HashMap<String, String>();
			String sqrxm="";
			String zjh="";
			for(int iCol=0;iCol<dataTableQLR.getColumns().size();iCol++){
				String fieldName=dataTableQLR.getColumns().get(iCol).getColumnName();
				String fieldValue="";
				try {
					fieldValue = dataTableQLR.getRows().get(i).getString(iCol);
				} catch (Exception e) {
				}
				if("SQRXM".equals(fieldName)){
					sqrxm=fieldValue;
				}
				if("zjh".equals(fieldName)){
					zjh=fieldValue;
				}
				ywrinfo.put(fieldName, fieldValue);
			}
			String sqrid=SuperHelper.GeneratePrimaryKey();
			if(xmid_qlrmczjh_sqrid.containsKey(xmid)&&xmid_qlrmczjh_sqrid.get(xmid).containsKey(sqrxm+"-"+zjh)){
				sqrid=xmid_qlrmczjh_sqrid.get(xmid).get(sqrxm+"-"+zjh);
			}else{
				if(xmid_qlrmczjh_sqrid.containsKey(xmid)){
					HashMap<String,String> qlrmczjh_sqrid=xmid_qlrmczjh_sqrid.get(xmid);
					qlrmczjh_sqrid.put(sqrxm+"-"+zjh, sqrid);
					xmid_qlrmczjh_sqrid.put(xmid, qlrmczjh_sqrid);
				}else{
					HashMap<String,String> qlrmczjh_sqrid=new HashMap<String, String>();
					qlrmczjh_sqrid.put(sqrxm+"-"+zjh, sqrid);
					xmid_qlrmczjh_sqrid.put(xmid, qlrmczjh_sqrid);
				}
				list_ywrinfo.put(sqrid, ywrinfo);
			}
			if(dyh_sqridlist.containsKey(dyh)){
				List<String> qlridlist=dyh_sqridlist.get(dyh);
				if(!qlridlist.contains(sqrid)){
					qlridlist.add(sqrid);
				}
				dyh_sqridlist.put(dyh, qlridlist);
			}else{
				List<String> qlridlist=new ArrayList<String>();
				if(!qlridlist.contains(sqrid)){
					qlridlist.add(sqrid);
				}
				dyh_sqridlist.put(dyh, qlridlist);
			}
			
			if(xmid_sqridlist.containsKey(xmid)){
				List<String> qlridlist=xmid_sqridlist.get(xmid);
				if(!qlridlist.contains(sqrid)){
					qlridlist.add(sqrid);
				}
				xmid_sqridlist.put(xmid, qlridlist);
			}else{
				List<String> qlridlist=new ArrayList<String>();
				if(!qlridlist.contains(sqrid)){
					qlridlist.add(sqrid);
				}
				xmid_sqridlist.put(xmid, qlridlist);
			}
			
		}
 		
		List<HashMap<String,String>> acceptinfolist=new ArrayList<HashMap<String,String>>();
		result.put("BATCHNUM", batchNumber);
		int iXh=1;
		for(String xmid:xmidlist){
			///创建项目
			BDCS_XMXX xmxx= CreateProject(request,batchNumber,prodef_id, response);
			if(xmxx==null){
				break;
			}
			///项目信息
			HashMap<String,String> xminfo=xmid_xminfo.get(xmid);
			
			//更新项目信息
			StringHelper.setValue(xminfo, xmxx);
			
			List<Wfi_ProInst> list_proinst=baseCommonDao.getDataList(Wfi_ProInst.class, "FILE_NUMBER='"+xmxx.getPROJECT_ID()+"'");
			if(list_proinst!=null&&list_proinst.size()>0){
				Wfi_ProInst proinst=list_proinst.get(0);
				proinst.setProject_Name(xmxx.getXMMC());
				baseCommonDao.save(proinst);
			}
			
			baseCommonDao.save(xmxx);
			baseCommonDao.flush();
			Global.removeXMXX(xmxx.getPROJECT_ID());
			
			HashMap<String,String> acceptinfo=new HashMap<String, String>();
			acceptinfo.put("XMMC", xmxx.getXMMC());
			acceptinfo.put("YWLSH", xmxx.getYWLSH());
			//不动产单元id列表
			List<String> dyidlist=xmid_dyidlist.get(xmid);
			
			if(dyid_dyh!=null&&dyid_dyh.size()>0){
				String dyhlist=StringHelper.formatList(Arrays.asList(dyid_dyh.values().toArray(new String[dyid_dyh.size()])),"、");
				acceptinfo.put("DYHLIST", dyhlist);
			}else{
				acceptinfo.put("DYHLIST", "");
			}
			acceptinfo.put("SLRY", xmxx.getSLRY());
			acceptinfo.put("SLSJ", StringHelper.FormatDateOnType(xmxx.getSLSJ(), "yyyy-MM-dd"));
			acceptinfo.put("XH", StringHelper.formatObject(iXh));
			acceptinfo.put("XMID", xmid);
			//添加单元
			dyService.addBDCDYNoCheck(xmxx.getId(), StringHelper.formatList(dyidlist, ","));
			baseCommonDao.flush();
			//构建登记单元id和不动产单元id关系
			HashMap<String,String> djdyid_bdcdyid=new HashMap<String, String>();
			List<BDCS_DJDY_GZ> djdys=baseCommonDao.getDataList(BDCS_DJDY_GZ.class, "XMBH='"+xmxx.getId()+"'");
			for(BDCS_DJDY_GZ djdy:djdys){
				if(!djdyid_bdcdyid.containsKey(djdy.getDJDYID())){
					djdyid_bdcdyid.put(djdy.getDJDYID(), djdy.getBDCDYID());
				}
			}
			//移除原申请人
			List<BDCS_SQR> sqrlist_old=baseCommonDao.getDataList(BDCS_SQR.class, "XMBH='"+xmxx.getId()+"'");
			if(sqrlist_old!=null&&sqrlist_old.size()>0){
				for(BDCS_SQR sqr_old:sqrlist_old){
					baseCommonDao.deleteEntity(sqr_old);
				}
			}
			baseCommonDao.flush();
			List<String> sqridlist=xmid_sqridlist.get(xmid);
			///添加义务人
			for(String sqrid:sqridlist){
				if(!list_ywrinfo.containsKey(sqrid)){
					continue;
				}
				HashMap<String,String> sqrinfo=list_ywrinfo.get(sqrid);
				BDCS_SQR sqr = new BDCS_SQR();
				StringHelper.setValue(sqrinfo, sqr);
				sqr.setId(sqrid);
				sqr.setSQRLB(SQRLB.YF.Value);
				sqr.setXMBH(xmxx.getId());
				baseCommonDao.save(sqr);
			}
			List<String> list_sqrid_qlr=new ArrayList<String>();
			///添加权利人
			if(list_qlrinfo!=null&&list_qlrinfo.size()>0){
				for(HashMap<String,String> qlrinfo:list_qlrinfo){
					String sqrid=SuperHelper.GeneratePrimaryKey();
					BDCS_SQR sqr = new BDCS_SQR();
					StringHelper.setValue(qlrinfo, sqr);
					sqr.setId(sqrid);
					sqr.setSQRLB(SQRLB.JF.Value);
					sqr.setXMBH(xmxx.getId());
					baseCommonDao.save(sqr);
					list_sqrid_qlr.add(sqrid);
				}
			}
			
			baseCommonDao.flush();
			//获取权利和附属权利信息
			List<Rights> qls=RightsTools.loadRightsByCondition(DJDYLY.GZ, "XMBH='"+xmxx.getId()+"'");
			List<SubRights> fsqls=RightsTools.loadSubRightsByCondition(DJDYLY.GZ, "XMBH='"+xmxx.getId()+"'");
			for(Rights ql:qls){
				String djdyid=ql.getDJDYID();
				if(!djdyid_bdcdyid.containsKey(djdyid)){
					continue;
				}
				String bdcdyid=djdyid_bdcdyid.get(djdyid);
				if(!dyid_dyh.containsKey(bdcdyid)){
					continue;
				}
				String bdcdyh=dyid_dyh.get(bdcdyid);
				///更新权利
				HashMap<String,String> qlinfo=dyh_qlinfo.get(bdcdyh);
				StringHelper.setValue(qlinfo, ql);
				baseCommonDao.update(ql);
				for(SubRights fsql:fsqls){
					if(ql.getId().equals(fsql.getQLID())||fsql.getId().equals(ql.getFSQLID())){
						StringHelper.setValue(qlinfo, fsql);
						Double dyje=StringHelper.getDouble(qlinfo.get("ZQSE"));
						if(DYFS.YBDY.Value.equals(fsql.getDYFS())){
							fsql.setBDBZZQSE(dyje);
							fsql.setZGZQQDSS(null);
						}else if(DYFS.ZGEDY.Value.equals(fsql.getDYFS())){
							fsql.setZGZQSE(dyje);
						}
						baseCommonDao.update(fsql);
					}
				}
				baseCommonDao.flush();
				///添加权利人
				qlService.addQLRfromSQR(xmxx.getId(), ql.getId(),
						list_sqrid_qlr.toArray());
				baseCommonDao.flush();
			}
			acceptinfo.put("DYGS", StringHelper.formatObject(djdys.size()));
			acceptinfo.put("BATCHNUM", batchNumber);
			acceptinfolist.add(acceptinfo);
			iXh++;
		}
		result.put("acceptinfolist", acceptinfolist);
		result.put("batchnum", batchNumber);
		result.put("success", "true");
		excelInfo.remove("dataTableXM");
		excelInfo.remove("dataTableDYQ");
		excelInfo.remove("dataTableQLR");
		excelInfo.remove("cellinfolist");
		return result;
	}
	
	/*
	 * 根据流程定义id和批次号及excel解析信息受理项目（现房转移项目:CS013）
	 */
	@SuppressWarnings("unused")
	private HashMap<String,Object> AcceptProjectExcelCS013Ex(HttpServletRequest request,String prodef_id,
			String batchNumber,HashMap<String,Object> excelInfo,HttpServletResponse response){
		HashMap<String,String> dyh_xmid=new HashMap<String,String>();
		HashMap<String,String> dyid_dyh=new HashMap<String,String>();
		HashMap<String,HashMap<String,String>> dyh_qlinfo=new HashMap<String,HashMap<String,String>>();
		
		HashMap<String,List<String>> dyh_qlridlist=new HashMap<String,List<String>>();
		
		HashMap<String,List<String>> xmid_qlridlist=new HashMap<String,List<String>>();
		
		HashMap<String,HashMap<String,String>> sqrid_qlrxx=new HashMap<String,HashMap<String,String>>();
		List<HashMap<String,String>> list_ywrxx=new ArrayList<HashMap<String,String>>();
		
		HashMap<String,Object> result=new HashMap<String, Object>();
		
		DataTable dataTableXM=(DataTable)excelInfo.get("dataTableXM");
		DataTable dataTableDYQ=(DataTable)excelInfo.get("dataTableDYQ");
		DataTable dataTableQLR=(DataTable)excelInfo.get("dataTableQLR");
		
		List<String> xmidlist=new ArrayList<String>();
		HashMap<String,HashMap<String,String>> xmid_xminfo=new HashMap<String, HashMap<String,String>>();
		HashMap<String,List<String>> xmid_dyidlist=new HashMap<String,List<String>>();
 		for(int i=0;i<dataTableXM.getRows().size();i++){
 			String zt="";
			try {
				zt = dataTableXM.getRows().get(i).getString("ZT");
			} catch (Exception e2) {
			}
			if(!"0".equals(zt)&&!"1".equals(zt)){
				continue;
			}
			String xmid="";
			try {
				xmid = dataTableXM.getRows().get(i).getString("XMID");
			} catch (Exception e2) {
			}
			String dyh="";
			try {
				dyh = dataTableXM.getRows().get(i).getString("BDCDYH");
			} catch (Exception e1) {
			}
			String dyid="";
			try {
				dyid = dataTableXM.getRows().get(i).getString("BDCDYID");
			} catch (Exception e1) {
			}
			dyh_xmid.put(dyh, xmid);
			dyid_dyh.put(dyid, dyh);
			if(xmid_dyidlist.containsKey(xmid)){
				List<String> dyidlist=xmid_dyidlist.get(xmid);
				if(!dyidlist.contains(dyid)){
					dyidlist.add(dyid);
				}
				xmid_dyidlist.put(xmid, dyidlist);
			}else{
				List<String> dyidlist=new ArrayList<String>();
				if(!dyidlist.contains(dyid)){
					dyidlist.add(dyid);
				}
				xmid_dyidlist.put(xmid, dyidlist);
			}
			if(!xmidlist.contains(xmid)){
				xmidlist.add(xmid);
				HashMap<String,String> xminfo=new HashMap<String, String>();
				String xmmc="";
				try {
					xmmc = dataTableXM.getRows().get(i).getString("XMMC");
				} catch (Exception e) {
				}
				String sfhbfz="0";
				xminfo.put("XMMC", xmmc);
				xminfo.put("SFHBZS", sfhbfz);
				xmid_xminfo.put(xmid, xminfo);
			}
		}
 		
 		for(int i=0;i<dataTableDYQ.getRows().size();i++){
			String dyh="";
			try {
				dyh = dataTableDYQ.getRows().get(i).getString("BDCDYH");
			} catch (Exception e1) {
			}
			if(!dyid_dyh.containsValue(dyh)){
				continue;
			}
			HashMap<String,String> qlinfo=new HashMap<String, String>();
			for(int iCol=0;iCol<dataTableDYQ.getColumns().size();iCol++){
				String fieldName=dataTableDYQ.getColumns().get(iCol).getColumnName();
				String fieldValue="";
				try {
					fieldValue = dataTableDYQ.getRows().get(i).getString(iCol);
				} catch (Exception e) {
				}
				if("DYQLQSSJ".equals(fieldName)){
					fieldName="QLQSSJ";
				}
				if("DYQLJSSJ".equals(fieldName)){
					fieldName="QLJSSJ";
				}
				qlinfo.put(fieldName, fieldValue);
			}
			qlinfo.put("ZQDW", "1");
			dyh_qlinfo.put(dyh, qlinfo);
		}
 		HashMap<String,HashMap<String,String>> xmid_qlrmczjh_sqrid=new HashMap<String, HashMap<String,String>>();
 		for(int i=0;i<dataTableQLR.getRows().size();i++){
 			String sqrlb="";
			try {
				sqrlb = dataTableQLR.getRows().get(i).getString("SQRLB");
			} catch (Exception e1) {
			}
 			if("义务人".equals(sqrlb)){
 				HashMap<String,String> ywrinfo=new HashMap<String, String>();
 				for(int iCol=0;iCol<dataTableQLR.getColumns().size();iCol++){
 					String fieldName=dataTableQLR.getColumns().get(iCol).getColumnName();
 					String fieldValue="";
 					try {
 						fieldValue = dataTableQLR.getRows().get(i).getString(iCol);
 					} catch (Exception e) {
 					}
 					ywrinfo.put(fieldName, fieldValue);
 				}
 				list_ywrxx.add(ywrinfo);
 				continue;
 			}
 			
			String dyh="";
			try {
				dyh = dataTableQLR.getRows().get(i).getString("BDCDYH");
			} catch (Exception e1) {
			}
			if(!dyid_dyh.containsValue(dyh)){
				continue;
			}
			String xmid=dyh_xmid.get(dyh);
			
			HashMap<String,String> qlrinfo=new HashMap<String, String>();
			String sqrxm="";
			String zjh="";
			for(int iCol=0;iCol<dataTableQLR.getColumns().size();iCol++){
				String fieldName=dataTableQLR.getColumns().get(iCol).getColumnName();
				String fieldValue="";
				try {
					fieldValue = dataTableQLR.getRows().get(i).getString(iCol);
				} catch (Exception e) {
				}
				if("SQRXM".equals(fieldName)){
					sqrxm=fieldValue;
				}
				if("zjh".equals(fieldName)){
					zjh=fieldValue;
				}
				qlrinfo.put(fieldName, fieldValue);
			}
			String sqrid=SuperHelper.GeneratePrimaryKey();
			if(xmid_qlrmczjh_sqrid.containsKey(xmid)&&xmid_qlrmczjh_sqrid.get(xmid).containsKey(sqrxm+"-"+zjh)){
				sqrid=xmid_qlrmczjh_sqrid.get(xmid).get(sqrxm+"-"+zjh);
			}else{
				if(xmid_qlrmczjh_sqrid.containsKey(xmid)){
					HashMap<String,String> qlrmczjh_sqrid=xmid_qlrmczjh_sqrid.get(xmid);
					qlrmczjh_sqrid.put(sqrxm+"-"+zjh, sqrid);
					xmid_qlrmczjh_sqrid.put(xmid, qlrmczjh_sqrid);
				}else{
					HashMap<String,String> qlrmczjh_sqrid=new HashMap<String, String>();
					qlrmczjh_sqrid.put(sqrxm+"-"+zjh, sqrid);
					xmid_qlrmczjh_sqrid.put(xmid, qlrmczjh_sqrid);
				}
				sqrid_qlrxx.put(sqrid, qlrinfo);
			}
			if(dyh_qlridlist.containsKey(dyh)){
				List<String> qlridlist=dyh_qlridlist.get(dyh);
				if(!qlridlist.contains(sqrid)){
					qlridlist.add(sqrid);
				}
				dyh_qlridlist.put(dyh, qlridlist);
			}else{
				List<String> qlridlist=new ArrayList<String>();
				if(!qlridlist.contains(sqrid)){
					qlridlist.add(sqrid);
				}
				dyh_qlridlist.put(dyh, qlridlist);
			}
			
			if(xmid_qlridlist.containsKey(xmid)){
				List<String> qlridlist=xmid_qlridlist.get(xmid);
				if(!qlridlist.contains(sqrid)){
					qlridlist.add(sqrid);
				}
				xmid_qlridlist.put(xmid, qlridlist);
			}else{
				List<String> qlridlist=new ArrayList<String>();
				if(!qlridlist.contains(sqrid)){
					qlridlist.add(sqrid);
				}
				xmid_qlridlist.put(xmid, qlridlist);
			}
			
		}
 		
		List<HashMap<String,String>> acceptinfolist=new ArrayList<HashMap<String,String>>();
		result.put("BATCHNUM", batchNumber);
		int iXh=1;
		for(String xmid:xmidlist){
			///创建项目
			BDCS_XMXX xmxx= CreateProject(request,batchNumber,prodef_id, response);
			if(xmxx==null){
				break;
			}
			///项目信息
			HashMap<String,String> xminfo=xmid_xminfo.get(xmid);
			
			//更新项目信息
			StringHelper.setValue(xminfo, xmxx);
			
			List<Wfi_ProInst> list_proinst=baseCommonDao.getDataList(Wfi_ProInst.class, "FILE_NUMBER='"+xmxx.getPROJECT_ID()+"'");
			if(list_proinst!=null&&list_proinst.size()>0){
				Wfi_ProInst proinst=list_proinst.get(0);
				proinst.setProject_Name(xmxx.getXMMC());
				baseCommonDao.save(proinst);
			}
			
			baseCommonDao.save(xmxx);
			baseCommonDao.flush();
			Global.removeXMXX(xmxx.getPROJECT_ID());
			
			HashMap<String,String> acceptinfo=new HashMap<String, String>();
			acceptinfo.put("XMMC", xmxx.getXMMC());
			acceptinfo.put("YWLSH", xmxx.getYWLSH());
			//不动产单元id列表
			List<String> dyidlist=xmid_dyidlist.get(xmid);
			
			if(dyid_dyh!=null&&dyid_dyh.size()>0){
				String dyhlist=StringHelper.formatList(Arrays.asList(dyid_dyh.values().toArray(new String[dyid_dyh.size()])),"、");
				acceptinfo.put("DYHLIST", dyhlist);
			}else{
				acceptinfo.put("DYHLIST", "");
			}
			acceptinfo.put("SLRY", xmxx.getSLRY());
			acceptinfo.put("SLSJ", StringHelper.FormatDateOnType(xmxx.getSLSJ(), "yyyy-MM-dd"));
			acceptinfo.put("XH", StringHelper.formatObject(iXh));
			acceptinfo.put("XMID", xmid);
			//添加单元
			dyService.addBDCDYNoCheck(xmxx.getId(), StringHelper.formatList(dyidlist, ","));
			baseCommonDao.flush();
			//构建登记单元id和不动产单元id关系
			HashMap<String,String> djdyid_bdcdyid=new HashMap<String, String>();
			List<BDCS_DJDY_GZ> djdys=baseCommonDao.getDataList(BDCS_DJDY_GZ.class, "XMBH='"+xmxx.getId()+"'");
			for(BDCS_DJDY_GZ djdy:djdys){
				if(!djdyid_bdcdyid.containsKey(djdy.getDJDYID())){
					djdyid_bdcdyid.put(djdy.getDJDYID(), djdy.getBDCDYID());
				}
			}
			//移除原申请人
			List<BDCS_SQR> sqrlist_old=baseCommonDao.getDataList(BDCS_SQR.class, "XMBH='"+xmxx.getId()+"'");
			if(sqrlist_old!=null&&sqrlist_old.size()>0){
				for(BDCS_SQR sqr_old:sqrlist_old){
					baseCommonDao.deleteEntity(sqr_old);
				}
			}
			baseCommonDao.flush();
			List<String> sqridlist=xmid_qlridlist.get(xmid);
			///添加申请人
			for(String sqrid:sqridlist){
				if(!sqrid_qlrxx.containsKey(sqrid)){
					continue;
				}
				HashMap<String,String> sqrinfo=sqrid_qlrxx.get(sqrid);
				BDCS_SQR sqr = new BDCS_SQR();
				StringHelper.setValue(sqrinfo, sqr);
				sqr.setId(sqrid);
				sqr.setSQRLB(SQRLB.JF.Value);
				sqr.setXMBH(xmxx.getId());
				baseCommonDao.save(sqr);
			}
			///添加义务人
			if(list_ywrxx!=null&&list_ywrxx.size()>0){
				for(HashMap<String,String> ywrinfo:list_ywrxx){
					String sqrid=SuperHelper.GeneratePrimaryKey();
					BDCS_SQR sqr = new BDCS_SQR();
					StringHelper.setValue(ywrinfo, sqr);
					sqr.setId(sqrid);
					sqr.setSQRLB(SQRLB.YF.Value);
					sqr.setXMBH(xmxx.getId());
					baseCommonDao.save(sqr);
				}
			}
			
			baseCommonDao.flush();
			//获取权利和附属权利信息
			List<Rights> qls=RightsTools.loadRightsByCondition(DJDYLY.GZ, "XMBH='"+xmxx.getId()+"'");
			List<SubRights> fsqls=RightsTools.loadSubRightsByCondition(DJDYLY.GZ, "XMBH='"+xmxx.getId()+"'");
			for(Rights ql:qls){
				String djdyid=ql.getDJDYID();
				if(!djdyid_bdcdyid.containsKey(djdyid)){
					continue;
				}
				String bdcdyid=djdyid_bdcdyid.get(djdyid);
				if(!dyid_dyh.containsKey(bdcdyid)){
					continue;
				}
				String bdcdyh=dyid_dyh.get(bdcdyid);
				///更新权利
				HashMap<String,String> qlinfo=dyh_qlinfo.get(bdcdyh);
				StringHelper.setValue(qlinfo, ql);
				baseCommonDao.update(ql);
				for(SubRights fsql:fsqls){
					if(ql.getId().equals(fsql.getQLID())||fsql.getId().equals(ql.getFSQLID())){
						StringHelper.setValue(qlinfo, fsql);
						Double dyje=StringHelper.getDouble(qlinfo.get("ZQSE"));
						if(DYFS.YBDY.Value.equals(fsql.getDYFS())){
							fsql.setBDBZZQSE(dyje);
							fsql.setZGZQQDSS(null);
						}else if(DYFS.ZGEDY.Value.equals(fsql.getDYFS())){
							fsql.setZGZQSE(dyje);
						}
						List<String> list_dyr=new ArrayList<String>();
						List<String> list_dyrzjlx=new ArrayList<String>();
						List<String> list_dyrzjh=new ArrayList<String>();
						if(list_ywrxx!=null&&list_ywrxx.size()>0){
							for(HashMap<String,String> ywrinfo:list_ywrxx){
								String dyr=ywrinfo.get("SQRXM");
								String dyrzjlx=ywrinfo.get("ZJLX");
								String dyrzjh=ywrinfo.get("ZJH");
								list_dyr.add(dyr);
								list_dyrzjh.add(dyrzjh);
								list_dyrzjlx.add(dyrzjlx);
							}
							fsql.setDYR(StringHelper.formatList(list_dyr, ","));
							fsql.setYWR(StringHelper.formatList(list_dyr, ","));
							fsql.setYWRZJZL(StringHelper.formatList(list_dyrzjlx, ","));
							fsql.setYWRZJH(StringHelper.formatList(list_dyrzjh, ","));
						}
						baseCommonDao.update(fsql);
					}
				}
				baseCommonDao.flush();
				///添加权利人
				if(dyh_qlridlist.containsKey(bdcdyh)){
					List<String> qlridlist=dyh_qlridlist.get(bdcdyh);
					qlService.addQLRfromSQR(xmxx.getId(), ql.getId(),
							qlridlist.toArray());
				}
				baseCommonDao.flush();
			}
			acceptinfo.put("DYGS", StringHelper.formatObject(djdys.size()));
			acceptinfo.put("BATCHNUM", batchNumber);
			acceptinfolist.add(acceptinfo);
			iXh++;
		}
		result.put("acceptinfolist", acceptinfolist);
		result.put("batchnum", batchNumber);
		result.put("success", "true");
		excelInfo.remove("dataTableXM");
		excelInfo.remove("dataTableDYQ");
		excelInfo.remove("dataTableQLR");
		excelInfo.remove("cellinfolist");
		return result;
	}
	
	/*
	 * 根据流程定义id和批次号及excel解析信息受理项目（现房转移项目:CS013）
	 */
	private HashMap<String,Object> AcceptProjectExcelCS013Ex2(HttpServletRequest request,String prodef_id,
			String batchNumber,HashMap<String,Object> excelInfo,HttpServletResponse response){
		HashMap<String,String> dyh_xmid=new HashMap<String,String>();
		HashMap<String,String> dyid_dyh=new HashMap<String,String>();
		HashMap<String,HashMap<String,String>> dyh_qlinfo=new HashMap<String,HashMap<String,String>>();
		
		HashMap<String,HashMap<String,String>> list_ywrinfo=new HashMap<String,HashMap<String,String>>();
		
		HashMap<String,List<String>> xmid_sqridlist=new HashMap<String, List<String>>();
		
		HashMap<String,List<String>> dyh_sqridlist=new HashMap<String, List<String>>();
		
		List<HashMap<String,String>> list_qlrinfo=new ArrayList<HashMap<String,String>>();
		
		HashMap<String,Object> result=new HashMap<String, Object>();
		
		DataTable dataTableXM=(DataTable)excelInfo.get("dataTableXM");
		DataTable dataTableDYQ=(DataTable)excelInfo.get("dataTableDYQ");
		DataTable dataTableQLR=(DataTable)excelInfo.get("dataTableQLR");
		
		List<String> xmidlist=new ArrayList<String>();
		HashMap<String,HashMap<String,String>> xmid_xminfo=new HashMap<String, HashMap<String,String>>();
		HashMap<String,List<String>> xmid_dyidlist=new HashMap<String,List<String>>();
 		for(int i=0;i<dataTableXM.getRows().size();i++){
 			String zt="";
			try {
				zt = dataTableXM.getRows().get(i).getString("ZT");
			} catch (Exception e2) {
			}
			if(!"0".equals(zt)&&!"1".equals(zt)){
				continue;
			}
			String xmid="";
			try {
				xmid = dataTableXM.getRows().get(i).getString("XMID");
			} catch (Exception e2) {
			}
			String dyh="";
			try {
				dyh = dataTableXM.getRows().get(i).getString("BDCDYH");
			} catch (Exception e1) {
			}
			String dyid="";
			try {
				dyid = dataTableXM.getRows().get(i).getString("BDCDYID");
			} catch (Exception e1) {
			}
			dyh_xmid.put(dyh, xmid);
			dyid_dyh.put(dyid, dyh);
			if(xmid_dyidlist.containsKey(xmid)){
				List<String> dyidlist=xmid_dyidlist.get(xmid);
				if(!dyidlist.contains(dyid)){
					dyidlist.add(dyid);
				}
				xmid_dyidlist.put(xmid, dyidlist);
			}else{
				List<String> dyidlist=new ArrayList<String>();
				if(!dyidlist.contains(dyid)){
					dyidlist.add(dyid);
				}
				xmid_dyidlist.put(xmid, dyidlist);
			}
			if(!xmidlist.contains(xmid)){
				xmidlist.add(xmid);
				HashMap<String,String> xminfo=new HashMap<String, String>();
				String xmmc="";
				try {
					xmmc = dataTableXM.getRows().get(i).getString("XMMC");
				} catch (Exception e) {
				}
				String sfhbfz="0";
				xminfo.put("XMMC", xmmc);
				xminfo.put("SFHBZS", sfhbfz);
				xmid_xminfo.put(xmid, xminfo);
			}
		}
 		
 		for(int i=0;i<dataTableDYQ.getRows().size();i++){
			String dyh="";
			try {
				dyh = dataTableDYQ.getRows().get(i).getString("BDCDYH");
			} catch (Exception e1) {
			}
			if(!dyid_dyh.containsValue(dyh)){
				continue;
			}
			HashMap<String,String> qlinfo=new HashMap<String, String>();
			for(int iCol=0;iCol<dataTableDYQ.getColumns().size();iCol++){
				String fieldName=dataTableDYQ.getColumns().get(iCol).getColumnName();
				String fieldValue="";
				try {
					fieldValue = dataTableDYQ.getRows().get(i).getString(iCol);
				} catch (Exception e) {
				}
				if("DYQLQSSJ".equals(fieldName)){
					fieldName="QLQSSJ";
				}
				if("DYQLJSSJ".equals(fieldName)){
					fieldName="QLJSSJ";
				}
				qlinfo.put(fieldName, fieldValue);
			}
			qlinfo.put("ZQDW", "1");
			dyh_qlinfo.put(dyh, qlinfo);
		}
 		HashMap<String,HashMap<String,String>> xmid_qlrmczjh_sqrid=new HashMap<String, HashMap<String,String>>();
 		for(int i=0;i<dataTableQLR.getRows().size();i++){
 			String sqrlb="";
			try {
				sqrlb = dataTableQLR.getRows().get(i).getString("SQRLB");
			} catch (Exception e1) {
			}
 			if("抵押权人".equals(sqrlb)){
 				HashMap<String,String> qlrinfo=new HashMap<String, String>();
 				for(int iCol=0;iCol<dataTableQLR.getColumns().size();iCol++){
 					String fieldName=dataTableQLR.getColumns().get(iCol).getColumnName();
 					String fieldValue="";
 					try {
 						fieldValue = dataTableQLR.getRows().get(i).getString(iCol);
 					} catch (Exception e) {
 					}
 					qlrinfo.put(fieldName, fieldValue);
 				}
 				list_qlrinfo.add(qlrinfo);
 				continue;
 			}
 			
			String dyh="";
			try {
				dyh = dataTableQLR.getRows().get(i).getString("BDCDYH");
			} catch (Exception e1) {
			}
			if(!dyid_dyh.containsValue(dyh)){
				continue;
			}
			String xmid=dyh_xmid.get(dyh);
			
			HashMap<String,String> ywrinfo=new HashMap<String, String>();
			String sqrxm="";
			String zjh="";
			for(int iCol=0;iCol<dataTableQLR.getColumns().size();iCol++){
				String fieldName=dataTableQLR.getColumns().get(iCol).getColumnName();
				String fieldValue="";
				try {
					fieldValue = dataTableQLR.getRows().get(i).getString(iCol);
				} catch (Exception e) {
				}
				if("SQRXM".equals(fieldName)){
					sqrxm=fieldValue;
				}
				if("zjh".equals(fieldName)){
					zjh=fieldValue;
				}
				ywrinfo.put(fieldName, fieldValue);
			}
			String sqrid=SuperHelper.GeneratePrimaryKey();
			if(xmid_qlrmczjh_sqrid.containsKey(xmid)&&xmid_qlrmczjh_sqrid.get(xmid).containsKey(sqrxm+"-"+zjh)){
				sqrid=xmid_qlrmczjh_sqrid.get(xmid).get(sqrxm+"-"+zjh);
			}else{
				if(xmid_qlrmczjh_sqrid.containsKey(xmid)){
					HashMap<String,String> qlrmczjh_sqrid=xmid_qlrmczjh_sqrid.get(xmid);
					qlrmczjh_sqrid.put(sqrxm+"-"+zjh, sqrid);
					xmid_qlrmczjh_sqrid.put(xmid, qlrmczjh_sqrid);
				}else{
					HashMap<String,String> qlrmczjh_sqrid=new HashMap<String, String>();
					qlrmczjh_sqrid.put(sqrxm+"-"+zjh, sqrid);
					xmid_qlrmczjh_sqrid.put(xmid, qlrmczjh_sqrid);
				}
				list_ywrinfo.put(sqrid, ywrinfo);
			}
			if(dyh_sqridlist.containsKey(dyh)){
				List<String> qlridlist=dyh_sqridlist.get(dyh);
				if(!qlridlist.contains(sqrid)){
					qlridlist.add(sqrid);
				}
				dyh_sqridlist.put(dyh, qlridlist);
			}else{
				List<String> qlridlist=new ArrayList<String>();
				if(!qlridlist.contains(sqrid)){
					qlridlist.add(sqrid);
				}
				dyh_sqridlist.put(dyh, qlridlist);
			}
			
			if(xmid_sqridlist.containsKey(xmid)){
				List<String> qlridlist=xmid_sqridlist.get(xmid);
				if(!qlridlist.contains(sqrid)){
					qlridlist.add(sqrid);
				}
				xmid_sqridlist.put(xmid, qlridlist);
			}else{
				List<String> qlridlist=new ArrayList<String>();
				if(!qlridlist.contains(sqrid)){
					qlridlist.add(sqrid);
				}
				xmid_sqridlist.put(xmid, qlridlist);
			}
		}
 		
		List<HashMap<String,String>> acceptinfolist=new ArrayList<HashMap<String,String>>();
		result.put("BATCHNUM", batchNumber);
		int iXh=1;
		for(String xmid:xmidlist){
			///创建项目
			BDCS_XMXX xmxx= CreateProject(request,batchNumber,prodef_id, response);
			if(xmxx==null){
				break;
			}
			///项目信息
			HashMap<String,String> xminfo=xmid_xminfo.get(xmid);
			
			//更新项目信息
			StringHelper.setValue(xminfo, xmxx);
			
			List<Wfi_ProInst> list_proinst=baseCommonDao.getDataList(Wfi_ProInst.class, "FILE_NUMBER='"+xmxx.getPROJECT_ID()+"'");
			if(list_proinst!=null&&list_proinst.size()>0){
				Wfi_ProInst proinst=list_proinst.get(0);
				proinst.setProject_Name(xmxx.getXMMC());
				baseCommonDao.save(proinst);
			}
			
			baseCommonDao.save(xmxx);
			baseCommonDao.flush();
			Global.removeXMXX(xmxx.getPROJECT_ID());
			
			HashMap<String,String> acceptinfo=new HashMap<String, String>();
			acceptinfo.put("XMMC", xmxx.getXMMC());
			acceptinfo.put("YWLSH", xmxx.getYWLSH());
			//不动产单元id列表
			List<String> dyidlist=xmid_dyidlist.get(xmid);
			
			if(dyid_dyh!=null&&dyid_dyh.size()>0){
				String dyhlist=StringHelper.formatList(Arrays.asList(dyid_dyh.values().toArray(new String[dyid_dyh.size()])),"、");
				acceptinfo.put("DYHLIST", dyhlist);
			}else{
				acceptinfo.put("DYHLIST", "");
			}
			acceptinfo.put("SLRY", xmxx.getSLRY());
			acceptinfo.put("SLSJ", StringHelper.FormatDateOnType(xmxx.getSLSJ(), "yyyy-MM-dd"));
			acceptinfo.put("XH", StringHelper.formatObject(iXh));
			acceptinfo.put("XMID", xmid);
			//添加单元
			dyService.addBDCDYNoCheck(xmxx.getId(), StringHelper.formatList(dyidlist, ","));
			baseCommonDao.flush();
			//构建登记单元id和不动产单元id关系
			HashMap<String,String> djdyid_bdcdyid=new HashMap<String, String>();
			List<BDCS_DJDY_GZ> djdys=baseCommonDao.getDataList(BDCS_DJDY_GZ.class, "XMBH='"+xmxx.getId()+"'");
			for(BDCS_DJDY_GZ djdy:djdys){
				if(!djdyid_bdcdyid.containsKey(djdy.getDJDYID())){
					djdyid_bdcdyid.put(djdy.getDJDYID(), djdy.getBDCDYID());
				}
			}
			//移除原申请人
			List<BDCS_SQR> sqrlist_old=baseCommonDao.getDataList(BDCS_SQR.class, "XMBH='"+xmxx.getId()+"'");
			if(sqrlist_old!=null&&sqrlist_old.size()>0){
				for(BDCS_SQR sqr_old:sqrlist_old){
					baseCommonDao.deleteEntity(sqr_old);
				}
			}
			baseCommonDao.flush();
			List<String> sqridlist=xmid_sqridlist.get(xmid);
			///添加义务人
			for(String sqrid:sqridlist){
				if(!list_ywrinfo.containsKey(sqrid)){
					continue;
				}
				HashMap<String,String> sqrinfo=list_ywrinfo.get(sqrid);
				BDCS_SQR sqr = new BDCS_SQR();
				StringHelper.setValue(sqrinfo, sqr);
				sqr.setId(sqrid);
				sqr.setSQRLB(SQRLB.YF.Value);
				sqr.setXMBH(xmxx.getId());
				baseCommonDao.save(sqr);
			}
			List<String> list_sqrid_qlr=new ArrayList<String>();
			///添加权利人
			if(list_qlrinfo!=null&&list_qlrinfo.size()>0){
				for(HashMap<String,String> qlrinfo:list_qlrinfo){
					String sqrid=SuperHelper.GeneratePrimaryKey();
					BDCS_SQR sqr = new BDCS_SQR();
					StringHelper.setValue(qlrinfo, sqr);
					sqr.setId(sqrid);
					sqr.setSQRLB(SQRLB.JF.Value);
					sqr.setXMBH(xmxx.getId());
					baseCommonDao.save(sqr);
					list_sqrid_qlr.add(sqrid);
				}
			}
			
			baseCommonDao.flush();
			//获取权利和附属权利信息
			List<Rights> qls=RightsTools.loadRightsByCondition(DJDYLY.GZ, "XMBH='"+xmxx.getId()+"'");
			List<SubRights> fsqls=RightsTools.loadSubRightsByCondition(DJDYLY.GZ, "XMBH='"+xmxx.getId()+"'");
			for(Rights ql:qls){
				String djdyid=ql.getDJDYID();
				if(!djdyid_bdcdyid.containsKey(djdyid)){
					continue;
				}
				String bdcdyid=djdyid_bdcdyid.get(djdyid);
				if(!dyid_dyh.containsKey(bdcdyid)){
					continue;
				}
				String bdcdyh=dyid_dyh.get(bdcdyid);
				///更新权利
				HashMap<String,String> qlinfo=dyh_qlinfo.get(bdcdyh);
				StringHelper.setValue(qlinfo, ql);
				baseCommonDao.update(ql);
				for(SubRights fsql:fsqls){
					if(ql.getId().equals(fsql.getQLID())||fsql.getId().equals(ql.getFSQLID())){
						StringHelper.setValue(qlinfo, fsql);
						Double dyje=StringHelper.getDouble(qlinfo.get("ZQSE"));
						if(DYFS.YBDY.Value.equals(fsql.getDYFS())){
							fsql.setBDBZZQSE(dyje);
							fsql.setZGZQQDSS(null);
						}else if(DYFS.ZGEDY.Value.equals(fsql.getDYFS())){
							fsql.setZGZQSE(dyje);
						}
						List<String> list_dyr=new ArrayList<String>();
						List<String> list_dyrzjlx=new ArrayList<String>();
						List<String> list_dyrzjh=new ArrayList<String>();
						List<String> list_ywrsqrid=dyh_sqridlist.get(bdcdyh);
						if(list_ywrsqrid!=null&&list_ywrsqrid.size()>0){
							for(String ywrsqrid:list_ywrsqrid){
								HashMap<String,String> ywrinfo=list_ywrinfo.get(ywrsqrid);
								String dyr=ywrinfo.get("SQRXM");
								String dyrzjlx=ywrinfo.get("ZJLX");
								String dyrzjh=ywrinfo.get("ZJH");
								list_dyr.add(dyr);
								list_dyrzjh.add(dyrzjh);
								list_dyrzjlx.add(dyrzjlx);
							}
							fsql.setDYR(StringHelper.formatList(list_dyr, ","));
							fsql.setYWR(StringHelper.formatList(list_dyr, ","));
							fsql.setYWRZJZL(StringHelper.formatList(list_dyrzjlx, ","));
							fsql.setYWRZJH(StringHelper.formatList(list_dyrzjh, ","));
						}
						baseCommonDao.update(fsql);
					}
				}
				baseCommonDao.flush();
				///添加权利人
				qlService.addQLRfromSQR(xmxx.getId(), ql.getId(),
						list_sqrid_qlr.toArray());
				baseCommonDao.flush();
			}
			acceptinfo.put("DYGS", StringHelper.formatObject(djdys.size()));
			acceptinfo.put("BATCHNUM", batchNumber);
			acceptinfolist.add(acceptinfo);
			iXh++;
		}
		result.put("acceptinfolist", acceptinfolist);
		result.put("batchnum", batchNumber);
		result.put("success", "true");
		excelInfo.remove("dataTableXM");
		excelInfo.remove("dataTableDYQ");
		excelInfo.remove("dataTableQLR");
		excelInfo.remove("cellinfolist");
		return result;
	}

	/*
	 * 根据流程定义id和批次号及excel解析信息受理项目（现房转移项目:ZY002/ZY102）
	 */
	private HashMap<String,Object> AcceptProjectExcelYG002Ex(HttpServletRequest request,String prodef_id,
			String batchNumber,HashMap<String,Object> excelInfo,HttpServletResponse response){
		HashMap<String,String> dyh_xmid=new HashMap<String,String>();
		HashMap<String,String> dyid_dyh=new HashMap<String,String>();
		HashMap<String,HashMap<String,String>> dyh_qlinfo=new HashMap<String,HashMap<String,String>>();
		
		HashMap<String,HashMap<String,String>> dyh_dyqinfo=new HashMap<String,HashMap<String,String>>();
		
		HashMap<String,List<String>> dyh_qlridlist=new HashMap<String,List<String>>();
		
		HashMap<String,List<String>> xmid_qlridlist=new HashMap<String,List<String>>();
		
		HashMap<String,HashMap<String,String>> sqrid_qlrxx=new HashMap<String,HashMap<String,String>>();
		List<HashMap<String,String>> list_ywrxx=new ArrayList<HashMap<String,String>>();
		List<HashMap<String,String>> list_dyqrxx=new ArrayList<HashMap<String,String>>();
		
		HashMap<String,Object> result=new HashMap<String, Object>();
		
		DataTable dataTableXM=(DataTable)excelInfo.get("dataTableXM");
		DataTable dataTableQL=(DataTable)excelInfo.get("dataTableQL");
		DataTable dataTableDYQ=(DataTable)excelInfo.get("dataTableDYQ");
		DataTable dataTableQLR=(DataTable)excelInfo.get("dataTableQLR");
		
		List<String> xmidlist=new ArrayList<String>();
		HashMap<String,HashMap<String,String>> xmid_xminfo=new HashMap<String, HashMap<String,String>>();
		HashMap<String,List<String>> xmid_dyidlist=new HashMap<String,List<String>>();
 		for(int i=0;i<dataTableXM.getRows().size();i++){
 			String zt="";
			try {
				zt = dataTableXM.getRows().get(i).getString("ZT");
			} catch (Exception e2) {
			}
			if(!"0".equals(zt)&&!"1".equals(zt)){
				continue;
			}
			String xmid="";
			try {
				xmid = dataTableXM.getRows().get(i).getString("XMID");
			} catch (Exception e2) {
			}
			String dyh="";
			try {
				dyh = dataTableXM.getRows().get(i).getString("BDCDYH");
			} catch (Exception e1) {
			}
			String dyid="";
			try {
				dyid = dataTableXM.getRows().get(i).getString("BDCDYID");
			} catch (Exception e1) {
			}
			dyh_xmid.put(dyh, xmid);
			dyid_dyh.put(dyid, dyh);
			if(xmid_dyidlist.containsKey(xmid)){
				List<String> dyidlist=xmid_dyidlist.get(xmid);
				if(!dyidlist.contains(dyid)){
					dyidlist.add(dyid);
				}
				xmid_dyidlist.put(xmid, dyidlist);
			}else{
				List<String> dyidlist=new ArrayList<String>();
				if(!dyidlist.contains(dyid)){
					dyidlist.add(dyid);
				}
				xmid_dyidlist.put(xmid, dyidlist);
			}
			if(!xmidlist.contains(xmid)){
				xmidlist.add(xmid);
				HashMap<String,String> xminfo=new HashMap<String, String>();
				String xmmc="";
				try {
					xmmc = dataTableXM.getRows().get(i).getString("XMMC");
				} catch (Exception e) {
				}
				String sfhbfz="0";
				xminfo.put("XMMC", xmmc);
				xminfo.put("SFHBZS", sfhbfz);
				xmid_xminfo.put(xmid, xminfo);
			}
		}
 		
 		for(int i=0;i<dataTableQL.getRows().size();i++){
			String dyh="";
			try {
				dyh = dataTableQL.getRows().get(i).getString("BDCDYH");
			} catch (Exception e1) {
			}
			if(!dyid_dyh.containsValue(dyh)){
				continue;
			}
			HashMap<String,String> qlinfo=new HashMap<String, String>();
			for(int iCol=0;iCol<dataTableQL.getColumns().size();iCol++){
				String fieldName=dataTableQL.getColumns().get(iCol).getColumnName();
				String fieldValue="";
				try {
					fieldValue = dataTableQL.getRows().get(i).getString(iCol);
				} catch (Exception e) {
				}
				qlinfo.put(fieldName, fieldValue);
			}
			dyh_qlinfo.put(dyh, qlinfo);
		}
 		
 		for(int i=0;i<dataTableDYQ.getRows().size();i++){
			String dyh="";
			try {
				dyh = dataTableDYQ.getRows().get(i).getString("BDCDYH");
			} catch (Exception e1) {
			}
			if(!dyid_dyh.containsValue(dyh)){
				continue;
			}
			HashMap<String,String> dyqinfo=new HashMap<String, String>();
			for(int iCol=0;iCol<dataTableDYQ.getColumns().size();iCol++){
				String fieldName=dataTableDYQ.getColumns().get(iCol).getColumnName();
				String fieldValue="";
				try {
					fieldValue = dataTableDYQ.getRows().get(i).getString(iCol);
				} catch (Exception e) {
				}
				if("DYQLQSSJ".equals(fieldName)){
					fieldName="QLQSSJ";
				}
				if("DYQLJSSJ".equals(fieldName)){
					fieldName="QLJSSJ";
				}
				dyqinfo.put(fieldName, fieldValue);
			}
			dyh_dyqinfo.put(dyh, dyqinfo);
		}
 		
 		HashMap<String,HashMap<String,String>> xmid_qlrmczjh_sqrid=new HashMap<String, HashMap<String,String>>();
 		for(int i=0;i<dataTableQLR.getRows().size();i++){
 			String sqrlb="";
			try {
				sqrlb = dataTableQLR.getRows().get(i).getString("SQRLB");
			} catch (Exception e1) {
			}
 			if("义务人".equals(sqrlb)){
 				HashMap<String,String> ywrinfo=new HashMap<String, String>();
 				for(int iCol=0;iCol<dataTableQLR.getColumns().size();iCol++){
 					String fieldName=dataTableQLR.getColumns().get(iCol).getColumnName();
 					String fieldValue="";
 					try {
 						fieldValue = dataTableQLR.getRows().get(i).getString(iCol);
 					} catch (Exception e) {
 					}
 					ywrinfo.put(fieldName, fieldValue);
 				}
 				list_ywrxx.add(ywrinfo);
 				continue;
 			}else if("抵押权人".equals(sqrlb)){
 				HashMap<String,String> dyqrinfo=new HashMap<String, String>();
 				for(int iCol=0;iCol<dataTableQLR.getColumns().size();iCol++){
 					String fieldName=dataTableQLR.getColumns().get(iCol).getColumnName();
 					String fieldValue="";
 					try {
 						fieldValue = dataTableQLR.getRows().get(i).getString(iCol);
 					} catch (Exception e) {
 					}
 					dyqrinfo.put(fieldName, fieldValue);
 				}
 				list_dyqrxx.add(dyqrinfo);
 				continue;
 			}
 			
			String dyh="";
			try {
				dyh = dataTableQLR.getRows().get(i).getString("BDCDYH");
			} catch (Exception e1) {
			}
			if(!dyid_dyh.containsValue(dyh)){
				continue;
			}
			String xmid=dyh_xmid.get(dyh);
			
			HashMap<String,String> qlrinfo=new HashMap<String, String>();
			String sqrxm="";
			String zjh="";
			for(int iCol=0;iCol<dataTableQLR.getColumns().size();iCol++){
				String fieldName=dataTableQLR.getColumns().get(iCol).getColumnName();
				String fieldValue="";
				try {
					fieldValue = dataTableQLR.getRows().get(i).getString(iCol);
				} catch (Exception e) {
				}
				if("SQRXM".equals(fieldName)){
					sqrxm=fieldValue;
				}
				if("zjh".equals(fieldName)){
					zjh=fieldValue;
				}
				qlrinfo.put(fieldName, fieldValue);
			}
			String sqrid=SuperHelper.GeneratePrimaryKey();
			if(xmid_qlrmczjh_sqrid.containsKey(xmid)&&xmid_qlrmczjh_sqrid.get(xmid).containsKey(sqrxm+"-"+zjh)){
				sqrid=xmid_qlrmczjh_sqrid.get(xmid).get(sqrxm+"-"+zjh);
			}else{
				if(xmid_qlrmczjh_sqrid.containsKey(xmid)){
					HashMap<String,String> qlrmczjh_sqrid=xmid_qlrmczjh_sqrid.get(xmid);
					qlrmczjh_sqrid.put(sqrxm+"-"+zjh, sqrid);
					xmid_qlrmczjh_sqrid.put(xmid, qlrmczjh_sqrid);
				}else{
					HashMap<String,String> qlrmczjh_sqrid=new HashMap<String, String>();
					qlrmczjh_sqrid.put(sqrxm+"-"+zjh, sqrid);
					xmid_qlrmczjh_sqrid.put(xmid, qlrmczjh_sqrid);
				}
				sqrid_qlrxx.put(sqrid, qlrinfo);
			}
			if(dyh_qlridlist.containsKey(dyh)){
				List<String> qlridlist=dyh_qlridlist.get(dyh);
				if(!qlridlist.contains(sqrid)){
					qlridlist.add(sqrid);
				}
				dyh_qlridlist.put(dyh, qlridlist);
			}else{
				List<String> qlridlist=new ArrayList<String>();
				if(!qlridlist.contains(sqrid)){
					qlridlist.add(sqrid);
				}
				dyh_qlridlist.put(dyh, qlridlist);
			}
			
			if(xmid_qlridlist.containsKey(xmid)){
				List<String> qlridlist=xmid_qlridlist.get(xmid);
				if(!qlridlist.contains(sqrid)){
					qlridlist.add(sqrid);
				}
				xmid_qlridlist.put(xmid, qlridlist);
			}else{
				List<String> qlridlist=new ArrayList<String>();
				if(!qlridlist.contains(sqrid)){
					qlridlist.add(sqrid);
				}
				xmid_qlridlist.put(xmid, qlridlist);
			}
			
		}
 		
		List<HashMap<String,String>> acceptinfolist=new ArrayList<HashMap<String,String>>();
		result.put("BATCHNUM", batchNumber);
		int iXh=1;
		for(String xmid:xmidlist){
			///创建项目
			BDCS_XMXX xmxx= CreateProject(request,batchNumber,prodef_id, response);
			if(xmxx==null){
				break;
			}
			///项目信息
			HashMap<String,String> xminfo=xmid_xminfo.get(xmid);
			
			//更新项目信息
			StringHelper.setValue(xminfo, xmxx);
			
			List<Wfi_ProInst> list_proinst=baseCommonDao.getDataList(Wfi_ProInst.class, "FILE_NUMBER='"+xmxx.getPROJECT_ID()+"'");
			if(list_proinst!=null&&list_proinst.size()>0){
				Wfi_ProInst proinst=list_proinst.get(0);
				proinst.setProject_Name(xmxx.getXMMC());
				baseCommonDao.save(proinst);
			}
			
			baseCommonDao.save(xmxx);
			baseCommonDao.flush();
			Global.removeXMXX(xmxx.getPROJECT_ID());
			
			HashMap<String,String> acceptinfo=new HashMap<String, String>();
			acceptinfo.put("XMMC", xmxx.getXMMC());
			acceptinfo.put("YWLSH", xmxx.getYWLSH());
			//不动产单元id列表
			List<String> dyidlist=xmid_dyidlist.get(xmid);
			
			if(dyid_dyh!=null&&dyid_dyh.size()>0){
				String dyhlist=StringHelper.formatList(Arrays.asList(dyid_dyh.values().toArray(new String[dyid_dyh.size()])),"、");
				acceptinfo.put("DYHLIST", dyhlist);
			}else{
				acceptinfo.put("DYHLIST", "");
			}
			acceptinfo.put("SLRY", xmxx.getSLRY());
			acceptinfo.put("SLSJ", StringHelper.FormatDateOnType(xmxx.getSLSJ(), "yyyy-MM-dd"));
			acceptinfo.put("XH", StringHelper.formatObject(iXh));
			acceptinfo.put("XMID", xmid);
			//添加单元
			dyService.addBDCDYNoCheck(xmxx.getId(), StringHelper.formatList(dyidlist, ","));
			baseCommonDao.flush();
			//构建登记单元id和不动产单元id关系
			HashMap<String,String> djdyid_bdcdyid=new HashMap<String, String>();
			List<BDCS_DJDY_GZ> djdys=baseCommonDao.getDataList(BDCS_DJDY_GZ.class, "XMBH='"+xmxx.getId()+"'");
			for(BDCS_DJDY_GZ djdy:djdys){
				if(!djdyid_bdcdyid.containsKey(djdy.getDJDYID())){
					djdyid_bdcdyid.put(djdy.getDJDYID(), djdy.getBDCDYID());
				}
			}
			//移除原申请人
			List<BDCS_SQR> sqrlist_old=baseCommonDao.getDataList(BDCS_SQR.class, "XMBH='"+xmxx.getId()+"'");
			if(sqrlist_old!=null&&sqrlist_old.size()>0){
				for(BDCS_SQR sqr_old:sqrlist_old){
					baseCommonDao.deleteEntity(sqr_old);
				}
			}
			baseCommonDao.flush();
			List<String> sqridlist=xmid_qlridlist.get(xmid);
			//添加权利人
			if (sqridlist != null && sqridlist.size() > 0) {
				for(String sqrid:sqridlist){
					if(!sqrid_qlrxx.containsKey(sqrid)){
						continue;
					}
					HashMap<String,String> sqrinfo=sqrid_qlrxx.get(sqrid);
					BDCS_SQR sqr = new BDCS_SQR();
					StringHelper.setValue(sqrinfo, sqr);
					sqr.setId(sqrid);
					sqr.setSQRLB(SQRLB.JF.Value);
					sqr.setXMBH(xmxx.getId());
					baseCommonDao.save(sqr);
				}
			}
			///添加义务人
			if(list_ywrxx!=null&&list_ywrxx.size()>0){
				for(HashMap<String,String> ywrinfo:list_ywrxx){
					String sqrid=SuperHelper.GeneratePrimaryKey();
					BDCS_SQR sqr = new BDCS_SQR();
					StringHelper.setValue(ywrinfo, sqr);
					sqr.setId(sqrid);
					sqr.setSQRLB(SQRLB.YF.Value);
					sqr.setXMBH(xmxx.getId());
					baseCommonDao.save(sqr);
				}
			}
			List<String> list_sqrid_dyqr=new ArrayList<String>();
			///添加抵押权人
			if(list_dyqrxx!=null&&list_dyqrxx.size()>0){
				for(HashMap<String,String> dyqrinfo:list_dyqrxx){
					String sqrid=SuperHelper.GeneratePrimaryKey();
					BDCS_SQR sqr = new BDCS_SQR();
					StringHelper.setValue(dyqrinfo, sqr);
					sqr.setId(sqrid);
					sqr.setSQRLB(SQRLB.JF.Value);
					sqr.setXMBH(xmxx.getId());
					baseCommonDao.save(sqr);
					list_sqrid_dyqr.add(sqrid);
				}
			}
			
			baseCommonDao.flush();
			//获取权利和附属权利信息
			List<Rights> qls=RightsTools.loadRightsByCondition(DJDYLY.GZ, "XMBH='"+xmxx.getId()+"'");
			List<SubRights> fsqls=RightsTools.loadSubRightsByCondition(DJDYLY.GZ, "XMBH='"+xmxx.getId()+"'");
			for(Rights ql:qls){
				String djdyid=ql.getDJDYID();
				if(!djdyid_bdcdyid.containsKey(djdyid)){
					continue;
				}
				String bdcdyid=djdyid_bdcdyid.get(djdyid);
				if(!dyid_dyh.containsKey(bdcdyid)){
					continue;
				}
				String bdcdyh=dyid_dyh.get(bdcdyid);
				
				if(QLLX.DIYQ.Value.equals(ql.getQLLX())){
					///更新权利
					HashMap<String,String> qlinfo=dyh_dyqinfo.get(bdcdyh);
					StringHelper.setValue(qlinfo, ql);
					baseCommonDao.update(ql);
					for(SubRights fsql:fsqls){
						if(ql.getId().equals(fsql.getQLID())||fsql.getId().equals(ql.getFSQLID())){
							StringHelper.setValue(qlinfo, fsql);
							Double dyje=StringHelper.getDouble(qlinfo.get("ZQSE"));
							if(DYFS.YBDY.Value.equals(fsql.getDYFS())){
								fsql.setBDBZZQSE(dyje);
								fsql.setZGZQQDSS(null);
							}else if(DYFS.ZGEDY.Value.equals(fsql.getDYFS())){
								fsql.setZGZQSE(dyje);
							}
							baseCommonDao.update(fsql);
						}
					}
					baseCommonDao.flush();
					///添加权利人
					qlService.addQLRfromSQR(xmxx.getId(), ql.getId(),
							list_sqrid_dyqr.toArray());
				}else{
					///更新权利
					HashMap<String,String> qlinfo=dyh_qlinfo.get(bdcdyh);
					StringHelper.setValue(qlinfo, ql);
					baseCommonDao.update(ql);
					for(SubRights fsql:fsqls){
						if(ql.getId().equals(fsql.getQLID())||fsql.getId().equals(ql.getFSQLID())){
							StringHelper.setValue(qlinfo, fsql);
							baseCommonDao.update(fsql);
						}
					}
					baseCommonDao.flush();
					///添加权利人
					if(dyh_qlridlist.containsKey(bdcdyh)){
						List<String> qlridlist=dyh_qlridlist.get(bdcdyh);
						qlService.addQLRfromSQR(xmxx.getId(), ql.getId(),
								qlridlist.toArray());
					}
				}
				
				baseCommonDao.flush();
			}
			acceptinfo.put("DYGS", StringHelper.formatObject(djdys.size()));
			acceptinfo.put("BATCHNUM", batchNumber);
			acceptinfolist.add(acceptinfo);
			iXh++;
		}
		result.put("acceptinfolist", acceptinfolist);
		result.put("batchnum", batchNumber);
		result.put("success", "true");
		excelInfo.remove("dataTableXM");
		excelInfo.remove("dataTableQL");
		excelInfo.remove("dataTableDYQ");
		excelInfo.remove("dataTableQLR");
		excelInfo.remove("cellinfolist");
		return result;
	}
	
	/*
	 * 根据流程定义id和批次号及excel解析信息受理项目（现房转移项目:ZY002/ZY102）
	 */
	private HashMap<String,Object> AcceptProjectExcelYG001Ex(HttpServletRequest request,String prodef_id,
			String batchNumber,HashMap<String,Object> excelInfo,HttpServletResponse response){
		HashMap<String,String> dyh_xmid=new HashMap<String,String>();
		HashMap<String,String> dyid_dyh=new HashMap<String,String>();
		HashMap<String,HashMap<String,String>> dyh_qlinfo=new HashMap<String,HashMap<String,String>>();
		
		HashMap<String,List<String>> dyh_qlridlist=new HashMap<String,List<String>>();
		
		HashMap<String,List<String>> xmid_qlridlist=new HashMap<String,List<String>>();
		
		HashMap<String,HashMap<String,String>> sqrid_qlrxx=new HashMap<String,HashMap<String,String>>();
		List<HashMap<String,String>> list_ywrxx=new ArrayList<HashMap<String,String>>();
		
		HashMap<String,Object> result=new HashMap<String, Object>();
		
		DataTable dataTableXM=(DataTable)excelInfo.get("dataTableXM");
		DataTable dataTableQL=(DataTable)excelInfo.get("dataTableQL");
		DataTable dataTableQLR=(DataTable)excelInfo.get("dataTableQLR");
		
		List<String> xmidlist=new ArrayList<String>();
		HashMap<String,HashMap<String,String>> xmid_xminfo=new HashMap<String, HashMap<String,String>>();
		HashMap<String,List<String>> xmid_dyidlist=new HashMap<String,List<String>>();
 		for(int i=0;i<dataTableXM.getRows().size();i++){
 			String zt="";
			try {
				zt = dataTableXM.getRows().get(i).getString("ZT");
			} catch (Exception e2) {
			}
			if(!"0".equals(zt)&&!"1".equals(zt)){
				continue;
			}
			String xmid="";
			try {
				xmid = dataTableXM.getRows().get(i).getString("XMID");
			} catch (Exception e2) {
			}
			String dyh="";
			try {
				dyh = dataTableXM.getRows().get(i).getString("BDCDYH");
			} catch (Exception e1) {
			}
			String dyid="";
			try {
				dyid = dataTableXM.getRows().get(i).getString("BDCDYID");
			} catch (Exception e1) {
			}
			dyh_xmid.put(dyh, xmid);
			dyid_dyh.put(dyid, dyh);
			if(xmid_dyidlist.containsKey(xmid)){
				List<String> dyidlist=xmid_dyidlist.get(xmid);
				if(!dyidlist.contains(dyid)){
					dyidlist.add(dyid);
				}
				xmid_dyidlist.put(xmid, dyidlist);
			}else{
				List<String> dyidlist=new ArrayList<String>();
				if(!dyidlist.contains(dyid)){
					dyidlist.add(dyid);
				}
				xmid_dyidlist.put(xmid, dyidlist);
			}
			if(!xmidlist.contains(xmid)){
				xmidlist.add(xmid);
				HashMap<String,String> xminfo=new HashMap<String, String>();
				String xmmc="";
				try {
					xmmc = dataTableXM.getRows().get(i).getString("XMMC");
				} catch (Exception e) {
				}
				String sfhbfz="0";
				xminfo.put("XMMC", xmmc);
				xminfo.put("SFHBZS", sfhbfz);
				xmid_xminfo.put(xmid, xminfo);
			}
		}
 		
 		for(int i=0;i<dataTableQL.getRows().size();i++){
			String dyh="";
			try {
				dyh = dataTableQL.getRows().get(i).getString("BDCDYH");
			} catch (Exception e1) {
			}
			if(!dyid_dyh.containsValue(dyh)){
				continue;
			}
			HashMap<String,String> qlinfo=new HashMap<String, String>();
			for(int iCol=0;iCol<dataTableQL.getColumns().size();iCol++){
				String fieldName=dataTableQL.getColumns().get(iCol).getColumnName();
				String fieldValue="";
				try {
					fieldValue = dataTableQL.getRows().get(i).getString(iCol);
				} catch (Exception e) {
				}
				qlinfo.put(fieldName, fieldValue);
			}
			dyh_qlinfo.put(dyh, qlinfo);
		}
 		
 		
 		HashMap<String,HashMap<String,String>> xmid_qlrmczjh_sqrid=new HashMap<String, HashMap<String,String>>();
 		for(int i=0;i<dataTableQLR.getRows().size();i++){
 			String sqrlb="";
			try {
				sqrlb = dataTableQLR.getRows().get(i).getString("SQRLB");
			} catch (Exception e1) {
			}
 			if("义务人".equals(sqrlb)){
 				HashMap<String,String> ywrinfo=new HashMap<String, String>();
 				for(int iCol=0;iCol<dataTableQLR.getColumns().size();iCol++){
 					String fieldName=dataTableQLR.getColumns().get(iCol).getColumnName();
 					String fieldValue="";
 					try {
 						fieldValue = dataTableQLR.getRows().get(i).getString(iCol);
 					} catch (Exception e) {
 					}
 					ywrinfo.put(fieldName, fieldValue);
 				}
 				list_ywrxx.add(ywrinfo);
 				continue;
 			}
 			
			String dyh="";
			try {
				dyh = dataTableQLR.getRows().get(i).getString("BDCDYH");
			} catch (Exception e1) {
			}
			if(!dyid_dyh.containsValue(dyh)){
				continue;
			}
			String xmid=dyh_xmid.get(dyh);
			
			HashMap<String,String> qlrinfo=new HashMap<String, String>();
			String sqrxm="";
			String zjh="";
			for(int iCol=0;iCol<dataTableQLR.getColumns().size();iCol++){
				String fieldName=dataTableQLR.getColumns().get(iCol).getColumnName();
				String fieldValue="";
				try {
					fieldValue = dataTableQLR.getRows().get(i).getString(iCol);
				} catch (Exception e) {
				}
				if("SQRXM".equals(fieldName)){
					sqrxm=fieldValue;
				}
				if("zjh".equals(fieldName)){
					zjh=fieldValue;
				}
				qlrinfo.put(fieldName, fieldValue);
			}
			String sqrid=SuperHelper.GeneratePrimaryKey();
			if(xmid_qlrmczjh_sqrid.containsKey(xmid)&&xmid_qlrmczjh_sqrid.get(xmid).containsKey(sqrxm+"-"+zjh)){
				sqrid=xmid_qlrmczjh_sqrid.get(xmid).get(sqrxm+"-"+zjh);
			}else{
				if(xmid_qlrmczjh_sqrid.containsKey(xmid)){
					HashMap<String,String> qlrmczjh_sqrid=xmid_qlrmczjh_sqrid.get(xmid);
					qlrmczjh_sqrid.put(sqrxm+"-"+zjh, sqrid);
					xmid_qlrmczjh_sqrid.put(xmid, qlrmczjh_sqrid);
				}else{
					HashMap<String,String> qlrmczjh_sqrid=new HashMap<String, String>();
					qlrmczjh_sqrid.put(sqrxm+"-"+zjh, sqrid);
					xmid_qlrmczjh_sqrid.put(xmid, qlrmczjh_sqrid);
				}
				sqrid_qlrxx.put(sqrid, qlrinfo);
			}
			if(dyh_qlridlist.containsKey(dyh)){
				List<String> qlridlist=dyh_qlridlist.get(dyh);
				if(!qlridlist.contains(sqrid)){
					qlridlist.add(sqrid);
				}
				dyh_qlridlist.put(dyh, qlridlist);
			}else{
				List<String> qlridlist=new ArrayList<String>();
				if(!qlridlist.contains(sqrid)){
					qlridlist.add(sqrid);
				}
				dyh_qlridlist.put(dyh, qlridlist);
			}
			
			if(xmid_qlridlist.containsKey(xmid)){
				List<String> qlridlist=xmid_qlridlist.get(xmid);
				if(!qlridlist.contains(sqrid)){
					qlridlist.add(sqrid);
				}
				xmid_qlridlist.put(xmid, qlridlist);
			}else{
				List<String> qlridlist=new ArrayList<String>();
				if(!qlridlist.contains(sqrid)){
					qlridlist.add(sqrid);
				}
				xmid_qlridlist.put(xmid, qlridlist);
			}
			
		}
 		
		List<HashMap<String,String>> acceptinfolist=new ArrayList<HashMap<String,String>>();
		result.put("BATCHNUM", batchNumber);
		int iXh=1;
		for(String xmid:xmidlist){
			///创建项目
			BDCS_XMXX xmxx= CreateProject(request,batchNumber,prodef_id, response);
			if(xmxx==null){
				break;
			}
			///项目信息
			HashMap<String,String> xminfo=xmid_xminfo.get(xmid);
			
			//更新项目信息
			StringHelper.setValue(xminfo, xmxx);
			
			List<Wfi_ProInst> list_proinst=baseCommonDao.getDataList(Wfi_ProInst.class, "FILE_NUMBER='"+xmxx.getPROJECT_ID()+"'");
			if(list_proinst!=null&&list_proinst.size()>0){
				Wfi_ProInst proinst=list_proinst.get(0);
				proinst.setProject_Name(xmxx.getXMMC());
				baseCommonDao.save(proinst);
			}
			
			baseCommonDao.save(xmxx);
			baseCommonDao.flush();
			Global.removeXMXX(xmxx.getPROJECT_ID());
			
			HashMap<String,String> acceptinfo=new HashMap<String, String>();
			acceptinfo.put("XMMC", xmxx.getXMMC());
			acceptinfo.put("YWLSH", xmxx.getYWLSH());
			//不动产单元id列表
			List<String> dyidlist=xmid_dyidlist.get(xmid);
			
			if(dyid_dyh!=null&&dyid_dyh.size()>0){
				String dyhlist=StringHelper.formatList(Arrays.asList(dyid_dyh.values().toArray(new String[dyid_dyh.size()])),"、");
				acceptinfo.put("DYHLIST", dyhlist);
			}else{
				acceptinfo.put("DYHLIST", "");
			}
			acceptinfo.put("SLRY", xmxx.getSLRY());
			acceptinfo.put("SLSJ", StringHelper.FormatDateOnType(xmxx.getSLSJ(), "yyyy-MM-dd"));
			acceptinfo.put("XH", StringHelper.formatObject(iXh));
			acceptinfo.put("XMID", xmid);
			//添加单元
			dyService.addBDCDYNoCheck(xmxx.getId(), StringHelper.formatList(dyidlist, ","));
			baseCommonDao.flush();
			//构建登记单元id和不动产单元id关系
			HashMap<String,String> djdyid_bdcdyid=new HashMap<String, String>();
			List<BDCS_DJDY_GZ> djdys=baseCommonDao.getDataList(BDCS_DJDY_GZ.class, "XMBH='"+xmxx.getId()+"'");
			for(BDCS_DJDY_GZ djdy:djdys){
				if(!djdyid_bdcdyid.containsKey(djdy.getDJDYID())){
					djdyid_bdcdyid.put(djdy.getDJDYID(), djdy.getBDCDYID());
				}
			}
			//移除原申请人
			List<BDCS_SQR> sqrlist_old=baseCommonDao.getDataList(BDCS_SQR.class, "XMBH='"+xmxx.getId()+"'");
			if(sqrlist_old!=null&&sqrlist_old.size()>0){
				for(BDCS_SQR sqr_old:sqrlist_old){
					baseCommonDao.deleteEntity(sqr_old);
				}
			}
			baseCommonDao.flush();
			List<String> sqridlist=xmid_qlridlist.get(xmid);
			//添加权利人
			if (sqridlist != null && sqridlist.size() > 0) {
				for(String sqrid:sqridlist){
					if(!sqrid_qlrxx.containsKey(sqrid)){
						continue;
					}
					HashMap<String,String> sqrinfo=sqrid_qlrxx.get(sqrid);
					BDCS_SQR sqr = new BDCS_SQR();
					StringHelper.setValue(sqrinfo, sqr);
					sqr.setId(sqrid);
					sqr.setSQRLB(SQRLB.JF.Value);
					sqr.setXMBH(xmxx.getId());
					baseCommonDao.save(sqr);
				}
			}
			///添加义务人
			if(list_ywrxx!=null&&list_ywrxx.size()>0){
				for(HashMap<String,String> ywrinfo:list_ywrxx){
					String sqrid=SuperHelper.GeneratePrimaryKey();
					BDCS_SQR sqr = new BDCS_SQR();
					StringHelper.setValue(ywrinfo, sqr);
					sqr.setId(sqrid);
					sqr.setSQRLB(SQRLB.YF.Value);
					sqr.setXMBH(xmxx.getId());
					baseCommonDao.save(sqr);
				}
			}
			baseCommonDao.flush();
			//获取权利和附属权利信息
			List<Rights> qls=RightsTools.loadRightsByCondition(DJDYLY.GZ, "XMBH='"+xmxx.getId()+"'");
			List<SubRights> fsqls=RightsTools.loadSubRightsByCondition(DJDYLY.GZ, "XMBH='"+xmxx.getId()+"'");
			for(Rights ql:qls){
				String djdyid=ql.getDJDYID();
				if(!djdyid_bdcdyid.containsKey(djdyid)){
					continue;
				}
				String bdcdyid=djdyid_bdcdyid.get(djdyid);
				if(!dyid_dyh.containsKey(bdcdyid)){
					continue;
				}
				String bdcdyh=dyid_dyh.get(bdcdyid);
				
				///更新权利
				HashMap<String,String> qlinfo=dyh_qlinfo.get(bdcdyh);
				StringHelper.setValue(qlinfo, ql);
				baseCommonDao.update(ql);
				for(SubRights fsql:fsqls){
					if(ql.getId().equals(fsql.getQLID())||fsql.getId().equals(ql.getFSQLID())){
						StringHelper.setValue(qlinfo, fsql);
						baseCommonDao.update(fsql);
					}
				}
				baseCommonDao.flush();
				///添加权利人
				if(dyh_qlridlist.containsKey(bdcdyh)){
					List<String> qlridlist=dyh_qlridlist.get(bdcdyh);
					qlService.addQLRfromSQR(xmxx.getId(), ql.getId(),
							qlridlist.toArray());
				}
				
				baseCommonDao.flush();
			}
			acceptinfo.put("DYGS", StringHelper.formatObject(djdys.size()));
			acceptinfo.put("BATCHNUM", batchNumber);
			acceptinfolist.add(acceptinfo);
			iXh++;
		}
		result.put("acceptinfolist", acceptinfolist);
		result.put("batchnum", batchNumber);
		result.put("success", "true");
		excelInfo.remove("dataTableXM");
		excelInfo.remove("dataTableQL");
		excelInfo.remove("dataTableQLR");
		excelInfo.remove("cellinfolist");
		return result;
	}
}
