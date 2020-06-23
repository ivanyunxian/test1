package com.supermap.realestate.registration.service.impl;

import com.jcraft.jsch.ChannelSftp;
import com.supermap.realestate.registration.ViewClass.*;
import com.supermap.realestate.registration.ViewClass.JKSInfo.SRXM;
import com.supermap.realestate.registration.factorys.HandlerFactory;
import com.supermap.realestate.registration.handler.DJHandler;
import com.supermap.realestate.registration.mapping.HandlerMapping.RegisterWorkFlow;
import com.supermap.realestate.registration.model.*;
import com.supermap.realestate.registration.model.interfaces.*;
import com.supermap.realestate.registration.service.DBService;
import com.supermap.realestate.registration.service.ProjectService;
import com.supermap.realestate.registration.service.Sender.dataReport;
import com.supermap.realestate.registration.tools.QueryTools;
import com.supermap.realestate.registration.tools.RightsHolderTools;
import com.supermap.realestate.registration.tools.RightsTools;
import com.supermap.realestate.registration.tools.UnitTools;
import com.supermap.realestate.registration.util.*;
import com.supermap.realestate.registration.util.ConstValue.*;
import com.supermap.wisdombusiness.core.SuperHelper;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import com.supermap.wisdombusiness.framework.model.User;
import com.supermap.wisdombusiness.synchroinline.util.FileUtils;
import com.supermap.wisdombusiness.web.Message;
import com.supermap.wisdombusiness.web.ResultMessage;
import com.supermap.wisdombusiness.web.ui.Page;
import com.supermap.wisdombusiness.web.ui.tree.Tree;
import com.supermap.wisdombusiness.workflow.model.Wfd_Prodef;
import com.supermap.wisdombusiness.workflow.model.Wfi_ProInst;
import com.supermap.wisdombusiness.workflow.service.common.Common;
import com.supermap.wisdombusiness.workflow.service.wfd.SmHoliday;
import com.supermap.wisdombusiness.workflow.service.wfi.SmActInst;
import com.supermap.wisdombusiness.workflow.service.wfi.SmProMater;
import com.supermap.wisdombusiness.workflow.service.wfi.SmStaff;
import com.supermap.wisdombusiness.workflow.service.wfm.SmProSPService;
import com.supermap.wisdombusiness.workflow.util.DesHelper;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service("projectService")
public class ProjectServiceImpl implements ProjectService {

	@Autowired
	private SmProMater _SmProMater;

	@Autowired
	private CommonDao baseCommonDao;

	@Autowired
	private SmActInst smActInst;

	@Autowired
	private SmStaff smStaff;

	private static final Log logger = LogFactory
			.getLog(ProjectServiceImpl.class);

	/** 登薄service */
	@Autowired
	private DBService dbService;
	@Autowired
	private SmHoliday smHoliday;

	/**
	 * 获取ProjectInfo
	 * 
	 * @Title: getProjectInfo
	 * @author:liushufeng
	 * @date：2015年7月18日 下午6:52:03
	 * @param project_id
	 * @param request
	 * @return
	 * @throws IOException
	 */
	@Override
	public ProjectInfo getProjectInfo(String project_id,
			HttpServletRequest request) throws IOException {
		/*
		 * 处理逻辑：首先去BDCS_XMXX表里边根据project_id去查找，如果能够查找到，
		 * 就根据XMXX初始化一个ProjectInfo，否则，调用工作流提供的rest接口读取
		 * 项目相关信息，生成BDCS_XMXX记录，保存到数据库，然后初始化ProjectInfo
		 */
		ProjectInfo projectInfo = null;
		BDCS_XMXX xmxx = Global.getXMXX(project_id);
		if (xmxx != null) {
			projectInfo = ProjectHelper.GetPrjInfoByPrjID(project_id);
		} else {
			projectInfo = ProjectHelper.GetProjectFromRest(project_id, request);
		}

		Wfd_Prodef prodef = ProjectHelper.getWorkflowByProjectID(project_id);
		if (prodef != null) {
			projectInfo.setHouse_status(prodef.getHouse_Status());
		}
		// 根据流程ID获取房屋信息是否可编辑
		// 获取流程 编号
		String workflowcode = ProjectHelper
				.getWorkflowCodeByProjectID(project_id);
		RegisterWorkFlow flow = HandlerFactory.getWorkflow(project_id);
		if (!StringHelper.isEmpty(workflowcode) && flow != null) {
			if (flow.getHouseeditmap() != null
					&& flow.getHouseeditmap().containsKey(workflowcode)) {
				String houseedit = flow.getHouseeditmap().get(workflowcode);
				if (SF.YES.Value.equals(houseedit)) {
					projectInfo.setHouseedit(true);
				}
				String landedit = flow.getLandeditmap().get(workflowcode);
				if (SF.YES.Value.equals(landedit)) {
					projectInfo.setLandedit(true);
				}
				String buildingtable = flow.getBuildingTablemap().get(
						workflowcode);
				if (SF.YES.Value.equals(buildingtable)) {
					projectInfo.setShowBuildingTable(true);
				}
				// 本地化配置里的数据上报按钮的控制-总开关
				String isOpen = ConfigHelper.getNameByValue("ISOPEN");
				// 业务流程配置里的开关
				String dataReportBtn = flow.getDataReportBtneditmap().get(
						workflowcode); 
				if ("1".equals(isOpen)) { 
					if (SF.YES.Value.equals(dataReportBtn)) {
						projectInfo.setShowDataReportBtn("1");
					}
				}else if ("2".equals(isOpen)) {
					if (SF.YES.Value.equals(dataReportBtn)) {
						projectInfo.setShowDataReportBtn("2");
					}
				}else {
					projectInfo.setShowDataReportBtn("0");
				}
				String dyfs = flow.getDyfsmap().get(workflowcode);
				if (!StringHelper.isEmpty(dyfs)) {
					projectInfo.setDyfs(dyfs);
				}

				String unitpageid = flow.getUnitpageidmap().get(workflowcode);
				if (!StringHelper.isEmpty(unitpageid)) {
					projectInfo.setUnitpageid(unitpageid);
				}

				String rightpageid = flow.getRightpageidmap().get(workflowcode);
				if (!StringHelper.isEmpty(rightpageid)) {
					projectInfo.setRightpageid(rightpageid);
				}
			}

		}
		projectInfo.setXzqhdm(ConfigHelper.getNameByValue("XZQHDM"));
		String isenable=ConfigHelper.getNameByValue("IS_ENABLE");		
		projectInfo.setIsEnableNewGis(isenable.equals("1"));
		String isenableredflag=ConfigHelper.getNameByValue("IS_ENABLEREDFLAG");		
		projectInfo.setIsEnableRedFlag(isenableredflag.equals("1"));
		return projectInfo;
	}

	/**
	 * 获取ProjectInfo
	 * 
	 * @Title: getProjectInfo
	 * @author:liushufeng
	 * @date：2015年7月18日 下午6:52:03
	 * @param project_id
	 * @param request
	 * @return
	 * @throws IOException
	 */
	@Override
	public BDCS_XMXX getProjectId(String xmbh, HttpServletRequest request)
			throws IOException {
		/*
		 * BDCS_XMXX表里边根据xmbh去查找
		 */
		BDCS_XMXX xmxx = null;
		xmxx = Global.getXMXXbyXMBH(xmbh);
		if (xmxx == null) {
			xmxx = new BDCS_XMXX();
		}
		return xmxx;
	}

	@Autowired
	private SmProSPService smProSPService;

	/**
	 * 获取申请人列表
	 * 
	 * @Title: GetSQRList
	 * @author:liushufeng
	 * @date：2015年7月18日 下午6:32:57
	 * @param xmbh
	 * @return
	 */
	@Override
	public List<BDCS_SQR> getSQRList(String xmbh) {
		String sql = ProjectHelper.GetXMBHCondition(xmbh);
		List<BDCS_SQR> list = baseCommonDao.getDataList(BDCS_SQR.class, sql
				+ " ORDER BY SXH");
		return list;
	}

	  /**
	   * 导入申请人信息（excel）
	   *
	   */
	  @SuppressWarnings({ "rawtypes", "unchecked" })
	  @Override
	  public Message upsqrexcel(MultipartFile file, HttpServletRequest request) {
	    String xmbh = request.getParameter("xmbh");
	    Message msg = new Message();
	    List<Map> sqrlist = new ArrayList<Map>();
	    String re = "";
	    Long total = 0L;
	    if (!file.isEmpty()) {
	      String fileName = file.getOriginalFilename();
	      String index = SuperHelper.GeneratePrimaryKey();
	      re = index;
	      String path = request.getSession().getServletContext()
	          .getRealPath("/")
	          + "resources/upload/" + fileName;
	      File isfile = new File(path);
	      if (!isfile.exists()) {
	        isfile.mkdirs();
	      }
	      try {
	        file.transferTo(new File(path));
	        FileInputStream input = new FileInputStream(path);
	        Workbook workbook = null;
	        if (fileName.toLowerCase().endsWith("xlsx")) {
	          workbook = new XSSFWorkbook(input);
	        } else if (fileName.toLowerCase().endsWith("xls")) {
	          workbook = new HSSFWorkbook(input);
	        }
	        if (workbook == null) {
	          msg.setMsg("文件类型错误");
	        }
	        Sheet sheet = workbook.getSheetAt(0);
	        Row head = sheet.getRow(0);
	        sheet.removeRow(head);
	        boolean check = true;
	        String[] headname = {
	        		"申请人名称",
	        		"证件类型",
	        		"申请人证件号",
	        		"申请人类别",
	        		"申请人类型",
	        		"是否持证人",
	        		"共有方式",
	        		"权利比例",
	        		"地址",
	        		"电话号码",
	        		"法定代表人",
	        		"法人电话号码",
	        		"法定代表人身份证号",
	        		"代理人",
	        		"代理人证件号",
	        		"代理人电话",
	        		"顺序号",
	        		"不动产单元号",
	        		"国家/地区",
	        		"户籍所在省(市)",
	        		"所属行业",
	        		"邮政编码",
	        		"工作单位",
	        		"电子邮件",
	        		"发证机关",
	        		"权利面积",
	        		"代理机构名称",
	        		"备注"
	        };
	        /*********************************/
	        for (int i = 0; i < head.getLastCellNum(); i++) {
	        	String cellname = head.getCell(i).getStringCellValue().trim();
	        	if(!StringHelper.isEmpty(cellname)){
	        		if(check && cellname.equals(headname[i])){
	        			check = true;
	        		}else{
		        		check = false;
	        		}
	        	}else{
	        		check = false;
        		}
			}
	        if (check){
	        	for (Row row : sheet) {
	        		String 申请人名称 = null, 证件类型 = null, 申请人证件号 = null, 申请人类别 = null, 申请人类型 = null,是否持证人 = null,共有方式 = null,权利比例 = null,地址 = null,电话号码 = null,
	        				法定代表人 = null,法人电话号码 = null,法定代表人身份证号 = null,代理人 = null,代理人证件号 = null,代理人电话 = null,顺序号 = null,不动产单元号 = null,
	        				国家 = null,户籍所在省 = null,所属行业 = null,邮政编码 = null,工作单位 = null,电子邮件 = null,发证机关 = null,权利面积 = null,
	        				代理机构名称 = null,备注 = null;
	        		for (int i = 0; i < head.getLastCellNum(); i++) {
	        			/*if(i == 5)
	        				continue;*/
	        			Cell cell = row.getCell(i);
	        			String str = "";
	        			if(cell != null){
	        				switch (cell.getCellType()) {
		        			case HSSFCell.CELL_TYPE_NUMERIC:
		        				DecimalFormat df = new DecimalFormat("0"); // 可避免科学计数法
		        				str = df.format(cell.getNumericCellValue());
		        				break;
		        			case HSSFCell.CELL_TYPE_STRING:
		        				if(i==1 || i==3 || i==4 || i==5 || i==6){
		        					String[] arr = cell.getStringCellValue().trim().split("\\.");
		        					if(arr != null && arr.length > 0)
		        						str = arr[0];
		        				}else{
		        					str = cell.getStringCellValue().trim();
		        				}
		        				break;
		        			default:
		        				break;
		        			}
	        			}
	        			switch (i) {
	        			case 0:
	        				申请人名称 = str;
	        				break;
	        			case 1:
	        				证件类型 = str;
	        				break;
	        			case 2:
	        				申请人证件号 = str;
	        				break;
	        			case 3:
	        				申请人类别 = str;
	        				break;
	        			case 4:
	        				申请人类型 = str;
	        				break;
	        			case 5:	        				
	        				是否持证人 = str;
	        				break;
	        			case 6:
	        				共有方式 = str;
	        				break;
	        			case 7:
	        				权利比例 = str;
	        				break;
	        			case 8:
	        				地址 = str;
	        				break;
	        			case 9:
	        				电话号码 = str;
	        				break;
	        			case 10:
	        				法定代表人 = str;
	        				break;
	        			case 11:
	        				法人电话号码 = str;
	        				break;
	        			case 12:
	        				法定代表人身份证号 = str;
	        				break;
	        			case 13:
	        				代理人 = str;
	        				break;
	        			case 14:
	        				代理人证件号 = str;
	        				break;
	        			case 15:
	        				代理人电话 = str;
	        				break;
	        			case 16:
	        				顺序号 = str;
	        				break;
	        			case 17:
	        				不动产单元号 = str;
	        				break;
	        			case 18:
	        				国家 = str;
	        				break;
	        			case 19:
	        				户籍所在省 = str;
	        				break;
	        			case 20:
	        				所属行业 = str;
	        				break;
	        			case 21:
	        				邮政编码 = str;
	        				break;
	        			case 22:
	        				工作单位 = str;
	        				break;
	        			case 23:
	        				电子邮件 = str;
	        				break;
	        			case 24:
	        				发证机关 = str;
	        				break;
	        			case 25:
	        				权利面积 = str;
	        				break;
	        			case 26:
	        				代理机构名称 = str;
	        				break;
	        			case 27:
	        				备注 = str;
	        				break;
	        			default:
	        				break;
	        			}
	        		}
	        		if (!StringHelper.isEmpty(申请人名称)) {
	        			Map r = new HashMap<String, String>();
	        			r.put("申请人名称", 申请人名称);
	        			r.put("证件类型", 证件类型);
	        			r.put("申请人证件号", 申请人证件号);
	        			r.put("申请人类别", 申请人类别);
	        			r.put("申请人类型", 申请人类型);
	        			if (!StringHelper.isEmpty(是否持证人)) {
	        				r.put("是否持证人", 是否持证人);
						}else {
							r.put("是否持证人", "");
						}	        			
	        			r.put("共有方式", 共有方式);
	        			r.put("权利比例", 权利比例);
	        			r.put("地址", 地址);
	        			r.put("电话号码", 电话号码);
	        			r.put("法定代表人", 法定代表人);
	        			r.put("法人电话号码", 法人电话号码);
	        			r.put("法定代表人身份证号", 法定代表人身份证号);
	        			r.put("代理人", 代理人);
	        			r.put("代理人证件号", 代理人证件号);
	        			r.put("代理人电话", 代理人电话);	   
	        			r.put("顺序号", 顺序号);
	        			r.put("不动产单元号", 不动产单元号);
	        			
	        		/**2017年7月3日20:04:35 添加            huangmingh*/		
	        			r.put("国家/地区", 国家);
	        			r.put("户籍所在省(市)", 户籍所在省);
	        			r.put("所属行业", 所属行业);
	        			r.put("邮政编码", 邮政编码);
	        			r.put("工作单位", 工作单位);
	        			r.put("电子邮件", 电子邮件);
	        			r.put("发证机关", 发证机关);
	        			r.put("权利面积", 权利面积);
	        			r.put("代理机构名称", 代理机构名称);
	        			r.put("备注", 备注);
	        	/******************************************/	
	        			sqrlist.add(r);
	        		}
	        	}
	        } else {
	          msg.setMsg("请检查模板，是否正确？");
	        }
	        input.close();
	      } catch (IllegalStateException e) {
	        e.printStackTrace();
	      } catch (IOException e) {
	        e.printStackTrace();
	      }
	      HashSet<Map> hs = new HashSet<Map>(sqrlist);
	      sqrlist = new ArrayList<Map>(hs);
	      for (Map sqr : sqrlist) {
	        BDCS_SQR ex = new BDCS_SQR();
	        ex.setSQRXM(sqr.get("申请人名称") + "");
	        ex.setZJLX(sqr.get("证件类型") + "");
	        ex.setZJH(sqr.get("申请人证件号") + "");
	        ex.setSQRLB(sqr.get("申请人类别") + "");
	        ex.setSQRLX(sqr.get("申请人类型") + "");
	        ex.setISCZR(sqr.get("是否持证人") + "");
	        ex.setGYFS(sqr.get("共有方式") + "");
	        ex.setQLBL(sqr.get("权利比例") + "");
	        ex.setTXDZ(sqr.get("地址") + "");
	        ex.setLXDH(sqr.get("电话号码") + "");
	        ex.setFDDBR(sqr.get("法定代表人") + "");
	        ex.setFDDBRDH(sqr.get("法人电话号码") + "");
	        ex.setFDDBRZJHM(sqr.get("法定代表人身份证号") + "");
	        ex.setDLRXM(sqr.get("代理人") + "");
	        ex.setDLRLXDH(sqr.get("代理人电话")+"");
	        ex.setDLRZJHM(sqr.get("代理人证件号") + "");
	        ex.setSXH(sqr.get("顺序号") + "");
	        
	        /**于2017年7月3日19:47:34 添加  huangmingh*/
	        
	        ex.setGJDQ(sqr.get("国家/地区") + "");
	        ex.setHJSZSS(sqr.get("户籍所在省(市)")+"");
	        ex.setSSHY(sqr.get("所属行业")+"");
	        ex.setYZBM(sqr.get("邮政编码")+"");
	        ex.setGZDW(sqr.get("工作单位")+"");
	        ex.setDZYJ(sqr.get("电子邮件")+"");
	        ex.setFZJG(sqr.get("发证机关")+"");
	        ex.setQLMJ(sqr.get("权利面积")+"");
	        ex.setDLJGMC(sqr.get("代理机构名称")+"");
	        ex.setYXBZ(sqr.get("备注")+"");
	        
	        /************************************/
	        ex.setXMBH(xmbh);
	        baseCommonDao.save(ex);
	        baseCommonDao.flush();
	        //rq---确定权利关系 ****有一个问题：当多次导入同一file文件时，构建权利人的时候未去重
	        if ("1".equals(sqr.get("申请人类别"))) {	        	
	        	if (!StringHelper.isEmpty(sqr.get("不动产单元号"))) {
	        		String bdcdyh = sqr.get("不动产单元号").toString();
	        		List<BDCS_DJDY_GZ> djdys = baseCommonDao.getDataList(BDCS_DJDY_GZ.class, "XMBH='" + xmbh + "'");
					if (djdys != null && djdys.size() > 0) {
						for (BDCS_DJDY_GZ djdy : djdys) {
							String bdcdylxstr = djdy.getBDCDYLX();
							if (StringHelper.isEmpty(bdcdylxstr))
								continue;
							BDCDYLX lx = BDCDYLX.initFrom(bdcdylxstr);
							String _djdylystr = djdy.getLY();
							if (StringHelper.isEmpty(_djdylystr))
								continue;
							DJDYLY ly = DJDYLY.initFrom(_djdylystr);
							if (StringHelper.isEmpty(djdy.getBDCDYID()))
								continue;							
							RealUnit unit = UnitTools.loadUnit(lx, ly, djdy.getBDCDYID());
							if (unit == null)// 获取单元
								continue;
//							if (!xmbh.equals(unit.getXMBH())) // 项目编号
//								continue;
							if (bdcdyh.equals(unit.getBDCDYH())) {		
								BDCS_XMXX xmxx = baseCommonDao.get(BDCS_XMXX.class, xmbh);
								if (!StringHelper.isEmpty(xmxx.getQLLX())) {
									String condition = "XMBH='"+xmbh+"' AND DJDYID='" + djdy.getDJDYID() + "' AND QLLX='" + xmxx.getQLLX() + "'";
									List<Rights> ql_gZ= RightsTools.loadRightsByCondition(DJDYLY.GZ, condition);
									if (ql_gZ != null && ql_gZ.size()>0) {
										//添加权利人
										String qlid = ql_gZ.get(0).getId();
										BDCS_QLR_GZ qlr_GZ = new BDCS_QLR_GZ();
										//sqr的关联qlid
										ex.setGLQLID(qlid);									
									    //权利人信息
										qlr_GZ.setQLID(qlid);
										qlr_GZ.setSQRID(ex.getId());
										qlr_GZ.setXMBH(xmbh);										
										qlr_GZ.setQLRMC(ex.getSQRXM());// 权利人名称	
										qlr_GZ.setZJZL(ex.getZJLX());// 证件种类
										qlr_GZ.setZJH(ex.getZJH());// 证件号
										qlr_GZ.setQLRLX(ex.getSQRLX());// 权利人类型	
										qlr_GZ.setDH(ex.getLXDH());// 电话
										qlr_GZ.setISCZR(ex.getISCZR());// 持证人
										qlr_GZ.setGYFS(ex.getGYFS());// 共有方式
										qlr_GZ.setDZ(ex.getTXDZ());// 地址
										qlr_GZ.setQLBL(ex.getQLBL());//权利比例
										qlr_GZ.setFDDBR(ex.getFDDBR());// 法定代表人
										qlr_GZ.setFDDBRDH(ex.getFDDBRDH());// 法定代表人电话
										qlr_GZ.setFDDBRZJHM(ex.getFDDBRZJHM());// 法定代表人zjh
										qlr_GZ.setDLRXM(ex.getDLRXM());// 代理人姓名
										qlr_GZ.setDLRLXDH(ex.getDLRLXDH());// 代理人联系电话										
										qlr_GZ.setDLRZJHM(ex.getDLRZJHM());// 代理人证件号码		
										try {
											qlr_GZ.setSXH(Integer.parseInt(sqr.get("顺序号").toString()));// 顺序号	
										} catch (NumberFormatException e) {
											e.printStackTrace();
										}
										baseCommonDao.update(ex);
										baseCommonDao.save(qlr_GZ);
										baseCommonDao.flush();
									}
								}
							}
						}
					}	        	
	        	}
	        }
	    }
	  }
	    msg.setMsg("导入成功，刷新界面");
	    msg.setRows(new ArrayList<String>(Arrays.asList(re)));
	    msg.setTotal(total);
	    return msg;	  
	  }

	/**
	 * 获取申请人信息
	 * 
	 * @Title: GetSQRXX
	 * @author:liushufeng
	 * @date：2015年7月18日 下午6:33:17
	 * @param sqrid
	 * @return
	 */
	@Override
	public BDCS_SQR getSQRXX(String sqrid) {
		BDCS_SQR sqr = baseCommonDao.get(BDCS_SQR.class, sqrid);
		if (sqr != null) {
			if ("1".equals(sqr.getZJLX())) {
				if (!StringHelper.isEmpty(sqr.getZJH())) {
					List<BDCS_IDCARD_PIC> pics = baseCommonDao
							.getDataList(BDCS_IDCARD_PIC.class,
									"ZJH='" + sqr.getZJH() + "'");
					if (pics != null && pics.size() > 0) {
						String pic_code1 = StringHelper.isEmpty(pics.get(0)
								.getPIC1()) ? "" : pics.get(0).getPIC1().trim();
						String pic_code2 = StringHelper.isEmpty(pics.get(0)
								.getPIC2()) ? "" : pics.get(0).getPIC2().trim();
						String pic_code = pic_code1 + pic_code2;
						sqr.setPICTURECODE(pic_code);
					}
				}
			}
			if (!StringHelper.isEmpty(sqr.getDLRXM())) {
				if (!StringHelper.isEmpty(sqr.getDLRZJHM())) {
					List<BDCS_IDCARD_PIC> pics = baseCommonDao.getDataList(
							BDCS_IDCARD_PIC.class, "ZJH='" + sqr.getDLRZJHM()
									+ "'");
					if (pics != null && pics.size() > 0) {
						String pic_code1 = StringHelper.isEmpty(pics.get(0)
								.getPIC1()) ? "" : pics.get(0).getPIC1().trim();
						String pic_code2 = StringHelper.isEmpty(pics.get(0)
								.getPIC2()) ? "" : pics.get(0).getPIC2().trim();
						String pic_code = pic_code1 + pic_code2;
						sqr.setPICTURECODE_DLR(pic_code);
					}
				}
			}
		}
		return sqr;
	}

	/**
	 * 根据给定的申请人名称查找申请人,返回申请人ID（精确查询）
	 * 
	 * @Title: HasSQR
	 * @author:liushufeng
	 * @date：2015年7月18日 下午6:33:34
	 * @param xmbh
	 * @param sqrxm
	 * @return
	 */
	@Override
	public String hasSQR(String xmbh, String sqrxm, String sqrlb, String zjh) {
		StringBuilder builder = new StringBuilder();
		builder.append(" xmbh='").append(xmbh).append("' and sqrxm = '")
				.append(sqrxm).append("'");
		builder.append(" and sqrlb = '").append(sqrlb).append("'");
		if (!StringHelper.isEmpty(zjh)) {
			builder.append(" and zjh = '").append(zjh).append("'");
		}
		String sql = builder.toString();
		List<BDCS_SQR> sqrList = baseCommonDao.getDataList(BDCS_SQR.class, sql);

		if (sqrList.size() > 0)
			return sqrList.get(0).getId();

		return null;
	}

	/**
	 * 增加申请人
	 * 
	 * @Title: AddSQRXX
	 * @author:liushufeng
	 * @date：2015年7月18日 下午6:35:52
	 * @param sqr
	 * @return
	 */
	@Override
	public String addSQRXX(BDCS_SQR sqr) {
		if(sqr.getSQRXM()!=null){
			sqr.setSQRXM(sqr.getSQRXM().trim());
		}
		String sqrid = baseCommonDao.save(sqr);
		if (sqr != null) {
			String Baseworkflow_ID =ProjectHelper.GetPrjInfoByXMBH(sqr.getXMBH()).getBaseworkflowcode();
			if(Baseworkflow_ID.indexOf("YY") == 0){//异议登记， 把申请人拷贝到权利人
				String ywrStrs = "";
				String fsqlYwr = "";
				List<BDCS_SQR> list = baseCommonDao.getDataList(BDCS_SQR.class, "XMBH='" + sqr.getXMBH() + "'");
				BDCS_QL_GZ ql = null;
				BDCS_QDZR_GZ qdzr = null;
				BDCS_FSQL_GZ fsql = null;
				String djdyid = baseCommonDao.getDataList(BDCS_DJDY_GZ.class, "XMBH='"+sqr.getXMBH()+"'").get(0).getDJDYID();
				if (list != null && list.size() > 0) {
					for(BDCS_SQR bscs_sqr :list){
						if(bscs_sqr.getSQRLB().equals("2")){
							ywrStrs +=bscs_sqr.getSQRXM()+",";
						}
					}
					List<BDCS_QL_GZ> listqls = baseCommonDao.getDataList(BDCS_QL_GZ.class, "XMBH='" + sqr.getXMBH() + "' and DJDYID='" + djdyid + "'");
					if (listqls != null && listqls.size() > 0) {
						ql = listqls.get(0);
					}
					//如果添加了义务人，即时更新fsql表中的ywr信息
					List<BDCS_FSQL_GZ> listfsqls = baseCommonDao.getDataList(BDCS_FSQL_GZ.class," QLID='" + ql.getId() + "'");
					if(listfsqls != null && listfsqls.size() > 0){
						fsql = listfsqls.get(0);
						if(!StringHelper.isEmpty(ywrStrs)){
							fsqlYwr = ywrStrs.substring(0, ywrStrs.length()-1);
							fsql.setYWR(fsqlYwr);
						}
						baseCommonDao.update(fsql);
					}
					List<BDCS_QDZR_GZ> listqdzrs = baseCommonDao.getDataList(BDCS_QDZR_GZ.class, "XMBH='" + sqr.getXMBH() + "' AND QLID='" + ql.getId() + "'");
					if (listqdzrs != null && listqdzrs.size() > 0) {
						qdzr = listqdzrs.get(0);
					}
					int i = 0;
					for (BDCS_SQR sqr1 : list) {
						if(!sqr.getSQRLB().equals("2")){
							String sql=MessageFormat.format("XMBH=''{0}'' AND QLRMC=''{1}'' AND ZJH=''{2}''", sqr.getXMBH(),sqr.getSQRXM(),sqr.getZJH());
							List<BDCS_QLR_GZ> listqlr=baseCommonDao.getDataList(BDCS_QLR_GZ.class, sql);
							if(listqlr!=null && listqlr.size()>0)
								continue;
							BDCS_QLR_GZ qlr = ObjectHelper.CopySQRtoQLR(sqr);
							qlr.setQLID(ql.getId());
							qlr.setXMBH(ql.getXMBH());
							if (i == 0) {
								qdzr.setQLRID(qlr.getId());
							} else {
								BDCS_QDZR_GZ qdzrnew = new BDCS_QDZR_GZ();
								ObjectHelper.copyObject(qdzr, qdzrnew);
								qdzrnew.setId((String) SuperHelper.GeneratePrimaryKey());
								qdzrnew.setQLRID(qlr.getId());
								baseCommonDao.save(qdzrnew);
							}
							baseCommonDao.save(qlr);
							i++;
						}
					}
				}
				baseCommonDao.flush();
			}
			if ("1".equals(sqr.getZJLX())) {
				if (!StringHelper.isEmpty(sqr.getZJH())
						&& !StringHelper.isEmpty(sqr.getPICTURECODE())
						&& sqr.getPICTURECODE().length() > 0) {
					List<BDCS_IDCARD_PIC> pics = baseCommonDao
							.getDataList(BDCS_IDCARD_PIC.class,
									"ZJH='" + sqr.getZJH() + "'");
					if (pics != null && pics.size() > 0) {
						BDCS_IDCARD_PIC pic = pics.get(0);
						String pic_code = sqr.getPICTURECODE();
						String pic_code1 = pic_code;
						String pic_code2 = "";
						if (pic_code.length() > 3999) {
							pic_code1 = pic_code.substring(0, 3999);
							pic_code2 = pic_code.substring(3999);
						}
						pic.setZJH(sqr.getZJH());
						pic.setPIC1(pic_code1);
						pic.setPIC2(pic_code2);
						baseCommonDao.update(pic);
					} else {
						BDCS_IDCARD_PIC pic = new BDCS_IDCARD_PIC();
						String _id = SuperHelper.GeneratePrimaryKey();
						pic.setId(_id);
						String pic_code = sqr.getPICTURECODE();
						String pic_code1 = pic_code;
						String pic_code2 = "";
						if (pic_code.length() > 3999) {
							pic_code1 = pic_code.substring(0, 3999);
							pic_code2 = pic_code.substring(3999);
						}
						pic.setZJH(sqr.getZJH());
						pic.setPIC1(pic_code1);
						pic.setPIC2(pic_code2);
						baseCommonDao.save(pic);
					}
				}
			}
			if (!StringHelper.isEmpty(sqr.getDLRXM())) {
				if (!StringHelper.isEmpty(sqr.getDLRZJHM())
						&& !StringHelper.isEmpty(sqr.getPICTURECODE_DLR())
						&& sqr.getPICTURECODE_DLR().length() > 0) {
					List<BDCS_IDCARD_PIC> pics = baseCommonDao.getDataList(
							BDCS_IDCARD_PIC.class, "ZJH='" + sqr.getDLRZJHM()
									+ "'");
					if (pics != null && pics.size() > 0) {
						BDCS_IDCARD_PIC pic = pics.get(0);
						String pic_code = sqr.getPICTURECODE_DLR();
						String pic_code1 = pic_code;
						String pic_code2 = "";
						if (pic_code.length() > 3999) {
							pic_code1 = pic_code.substring(0, 3999);
							pic_code2 = pic_code.substring(3999);
						}
						pic.setZJH(sqr.getDLRZJHM());
						pic.setPIC1(pic_code1);
						pic.setPIC2(pic_code2);
						baseCommonDao.update(pic);
					} else {
						BDCS_IDCARD_PIC pic = new BDCS_IDCARD_PIC();
						String _id = SuperHelper.GeneratePrimaryKey();
						pic.setId(_id);
						String pic_code = sqr.getPICTURECODE_DLR();
						String pic_code1 = pic_code;
						String pic_code2 = "";
						if (pic_code.length() > 3999) {
							pic_code1 = pic_code.substring(0, 3999);
							pic_code2 = pic_code.substring(3999);
						}
						pic.setZJH(sqr.getDLRZJHM());
						pic.setPIC1(pic_code1);
						pic.setPIC2(pic_code2);
						baseCommonDao.save(pic);
					}
				}
			}
		}
		baseCommonDao.flush();
		return sqrid;
	}

	/**
	 * 更新申请人
	 * 
	 * @Title: UpdateSQRXX
	 * @author:liushufeng
	 * @date：2015年7月18日 下午6:35:17
	 * @param sqr
	 */
	@Override
	public void updateSQRXX(BDCS_SQR sqr) {
		if(sqr.getSQRXM()!=null){
			sqr.setSQRXM(sqr.getSQRXM().trim());
		}
		baseCommonDao.update(sqr);
		if ("2".equals(sqr.getSQRLB())) {//更新义务人到附属权利表
			String ywrStr = "";
			String fsqlYwr = "";
			List<BDCS_FSQL_GZ> fsql = baseCommonDao.getDataList(BDCS_FSQL_GZ.class, "XMBH='"+sqr.getXMBH()+"'");
			//TODO 此处查询，主要是考虑到异议登记，fsql表中的ywr需要即时更新
			List<BDCS_SQR> sqrs = baseCommonDao.getDataList(BDCS_SQR.class, "XMBH='"+sqr.getXMBH()+"'");
			if(sqrs.size()>0 && sqrs != null){
				for(BDCS_SQR bdcs_sqr : sqrs){
					if("2".equals(bdcs_sqr.getSQRLB())){
						ywrStr += bdcs_sqr.getSQRXM() + ",";
					}
				}
				if(!StringHelper.isEmpty(ywrStr)){
					fsqlYwr = ywrStr.substring(0, ywrStr.length()-1);
				}
			}
//			fsql.get(0).setYWR(sqr.getSQRXM());
			fsql.get(0).setYWR(fsqlYwr);
			fsql.get(0).setYWRZJZL(sqr.getZJLX());
			fsql.get(0).setYWRZJH(sqr.getZJH());
			if(fsql!=null&&fsql.size()>0) {
				baseCommonDao.update(fsql.get(0));
			}
			
			baseCommonDao.flush();
		}
		
		if (sqr != null) {
			if (!StringHelper.isEmpty(sqr.getDLRXM())) {
				if (!StringHelper.isEmpty(sqr.getDLRZJHM())
						&& !StringHelper.isEmpty(sqr.getPICTURECODE_DLR())
						&& sqr.getPICTURECODE_DLR().length() > 0) {
					List<BDCS_IDCARD_PIC> pics = baseCommonDao.getDataList(
							BDCS_IDCARD_PIC.class, "ZJH='" + sqr.getDLRZJHM()
									+ "'");
					if (pics != null && pics.size() > 0) {
						BDCS_IDCARD_PIC pic = pics.get(0);
						String pic_code = sqr.getPICTURECODE_DLR();
						String pic_code1 = pic_code;
						String pic_code2 = "";
						if (pic_code.length() > 3999) {
							pic_code1 = pic_code.substring(0, 3999);
							pic_code2 = pic_code.substring(3999);
						}
						pic.setZJH(sqr.getDLRZJHM());
						pic.setPIC1(pic_code1);
						pic.setPIC2(pic_code2);
						baseCommonDao.update(pic);
					} else {
						BDCS_IDCARD_PIC pic = new BDCS_IDCARD_PIC();
						String _id = SuperHelper.GeneratePrimaryKey();
						pic.setId(_id);
						String pic_code = sqr.getPICTURECODE_DLR();
						String pic_code1 = pic_code;
						String pic_code2 = "";
						if (pic_code.length() > 3999) {
							pic_code1 = pic_code.substring(0, 3999);
							pic_code2 = pic_code.substring(3999);
						}
						pic.setZJH(sqr.getDLRZJHM());
						pic.setPIC1(pic_code1);
						pic.setPIC2(pic_code2);
						baseCommonDao.save(pic);
					}
				}
			}
			if(!StringHelper.isEmpty(sqr.getZJH())&&!StringHelper.isEmpty(sqr.getPICTURECODE())){
				List<BDCS_IDCARD_PIC> pics = baseCommonDao.getDataList(
						BDCS_IDCARD_PIC.class, "ZJH='" + sqr.getZJH()
								+ "'");
				if (pics != null && pics.size() > 0) {
					BDCS_IDCARD_PIC pic = pics.get(0);
					String pic_code = sqr.getPICTURECODE();
					String pic_code1 = pic_code;
					String pic_code2 = "";
					if (pic_code.length() > 3999) {
						pic_code1 = pic_code.substring(0, 3999);
						pic_code2 = pic_code.substring(3999);
					}
					pic.setZJH(sqr.getZJH());
					pic.setPIC1(pic_code1);
					pic.setPIC2(pic_code2);
					baseCommonDao.update(pic);
				} else {
					BDCS_IDCARD_PIC pic = new BDCS_IDCARD_PIC();
					String _id = SuperHelper.GeneratePrimaryKey();
					pic.setId(_id);
					String pic_code = sqr.getPICTURECODE();
					String pic_code1 = pic_code;
					String pic_code2 = "";
					if (pic_code.length() > 3999) {
						pic_code1 = pic_code.substring(0, 3999);
						pic_code2 = pic_code.substring(3999);
					}
					pic.setZJH(sqr.getZJH());
					pic.setPIC1(pic_code1);
					pic.setPIC2(pic_code2);
					baseCommonDao.save(pic);
				}
			}
		}
		// sunhb-2015-08-21 连带的更新一下BDCS_QLR_GZ信息
		if (sqr != null) {
			String sql = "SQRID ='" + sqr.getId() + "'";
			List<BDCS_QLR_GZ> qlr = baseCommonDao.getDataList(BDCS_QLR_GZ.class, sql);
			for (BDCS_QLR_GZ bdcs_qlr_gz : qlr) {
				if (sqr.getSQRXM() != null) {
					bdcs_qlr_gz.setQLRMC(sqr.getSQRXM());// 权利人名称
				}
				if (sqr.getXB() != null) {
					bdcs_qlr_gz.setXB(sqr.getXB());// 性别
				}
				// 权利人类别
				if (sqr.getSQRLX() != null) {
					bdcs_qlr_gz.setQLRLX(sqr.getSQRLX());// 权利人类型
				}
				if (sqr.getZJLX() != null) {
					bdcs_qlr_gz.setZJZL(sqr.getZJLX());// 证件种类
				}
				if (sqr.getZJH() != null) {
					bdcs_qlr_gz.setZJH(sqr.getZJH());// 证件号
				}
				if (sqr.getLXDH() != null) {
					bdcs_qlr_gz.setDH(sqr.getLXDH());// 电话
				}
				if (sqr.getGYFS() != null) {
					bdcs_qlr_gz.setGYFS(sqr.getGYFS());// 共有方式
				}
				if (sqr.getTXDZ() != null) {
					bdcs_qlr_gz.setDZ(sqr.getTXDZ());// 地址
				}
				if (sqr.getFDDBR() != null) {
					bdcs_qlr_gz.setFDDBR(sqr.getFDDBR());// 法定代表人
				}
				if (sqr.getFDDBRDH() != null) {
					bdcs_qlr_gz.setFDDBRDH(sqr.getFDDBRDH());// 法定代表人电话
				}
				if (sqr.getGJDQ() != null) {
					bdcs_qlr_gz.setGJ(sqr.getGJDQ());// 国家地区
				}
				if (sqr.getHJSZSS() != null) {
					bdcs_qlr_gz.setHJSZSS(sqr.getHJSZSS());// 户籍所在省市
				}
				bdcs_qlr_gz.setSSHY(sqr.getSSHY() == null ? "" : sqr.getSSHY()
						.toString());// 所属行业
				if (sqr.getYZBM() != null) {
					bdcs_qlr_gz.setYB(sqr.getYZBM());// 邮政编码
				}
				if (sqr.getGZDW() != null) {
					bdcs_qlr_gz.setGZDW(sqr.getGZDW());// 工作单位
				}
				if (sqr.getDZYJ() != null) {
					bdcs_qlr_gz.setDZYJ(sqr.getDZYJ());// 电子邮箱
				}
				try {
					double d = Double.valueOf(sqr.getQLMJ()).doubleValue();
					bdcs_qlr_gz.setQLMJ(d);// 权利面积
				} catch (Exception e) {
				}
				if (sqr.getQLBL() != null) {
					bdcs_qlr_gz.setQLBL(sqr.getQLBL());// 权利比例
				}
				if (sqr.getFZJG() != null) {
					bdcs_qlr_gz.setFZJG(sqr.getFZJG());// 发证机关
				}
				if (sqr.getDLJGMC() != null) {
					bdcs_qlr_gz.setDLJGMC(sqr.getDLJGMC());// 代理机构名称
				}
				if (sqr.getDLRXM() != null) {
					bdcs_qlr_gz.setDLRXM(sqr.getDLRXM());// 代理人姓名
				}
				if (sqr.getDLRLXDH() != null) {
					bdcs_qlr_gz.setDLRLXDH(sqr.getDLRLXDH());// 代理人联系电话
				}
				if (sqr.getDLRZJHM() != null) {
					bdcs_qlr_gz.setDLRZJHM(sqr.getDLRZJHM());// 代理人证件号码
				}
				if (sqr.getSXH() != null && !StringHelper.isEmpty(sqr.getSXH())) {
					try {
						Integer sxh = Integer.parseInt(sqr.getSXH());
						bdcs_qlr_gz.setSXH(sxh); // 顺序号
					} catch (Exception ee) {
					}
				}
				if (sqr.getISCZR() != null) {
					bdcs_qlr_gz.setISCZR(sqr.getISCZR()); // 是否持证人
				}
				/*
				 * 目前这些不需要更新 bdcs_qlr_gz.setDLRZJHM(sqr.getDLRZJHM());//代理人证件号码
				 * bdcs_qlr_gz.setDLRZJLX(sqr.getDLRZJLX());//代理人证件类型
				 * bdcs_qlr_gz.setYXBZ(sqr.getYXBZ());//有效标志
				 * bdcs_qlr_gz.setFDDBRZJLX(sqr.getFDDBRZJLX());//法定代表人证件种类
				 * //顺序号 if (!StringUtils.isEmpty(sqr.getSXH())) {
				 * bdcs_qlr_gz.setSXH(Integer.valueOf(sqr.getSXH())); }
				 * bdcs_qlr_gz.setCREATETIME(sqr.getCREATETIME());
				 * bdcs_qlr_gz.setMODIFYTIME(sqr.getMODIFYTIME());
				 */

				baseCommonDao.update(bdcs_qlr_gz);
			}
		}
		baseCommonDao.flush();
	}

	/**
	 * 删除申请人
	 * 
	 * @Title: DeleteSQRXX
	 * @author:liushufeng
	 * @date：2015年7月18日 下午6:36:32
	 * @param sqrid
	 */
	@Override
	public void deleteSQRXX(String sqrid) {
		baseCommonDao.delete(BDCS_SQR.class, sqrid);
		baseCommonDao.flush();
	}

	/**
	 * 删除申请人头像
	 * 
	 * @Title: deleteSQRPIC
	 * @author:赵梦帆
	 * @date：2016-10-17 17:15:39
	 * @param zjh
	 */
	@Override
	public void deleteSQRPIC(String zjh) {
		baseCommonDao.deleteEntitysByHql(BDCS_IDCARD_PIC.class, " ZJH='" + zjh
				+ "'");
		baseCommonDao.flush();
	}

	/**
	 * 加载头像
	 * 
	 * @Title: deleteSQRPIC
	 * @author:赵梦帆
	 * @date：2016-10-17 17:15:39
	 * @param zjh
	 * @return
	 */
	@Override
	public List<BDCS_IDCARD_PIC> LoadSQRPIC(String zjh) {
		List<BDCS_IDCARD_PIC> pic = baseCommonDao.getDataList(
				BDCS_IDCARD_PIC.class, " ZJH='" + zjh + "'");
		return pic;
	}
	
	/**
	 * 实现自动化收费
	 * 
	 * @Title: Jlsfzdh
	 * @author:YX
	 * @date：2017年5月24日 下午6:37:48
	 * @param xmbh
	 * @param page
	 * @param rows
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public void Jlsfzdh(String xmbh,int page,int rows) {
		//吉林个性化需求 收费 单单元自动生成。  YX
		String xzqdm = ConfigHelper.getNameByValue("XZQHDM");
		BDCS_XMXX xmxx = baseCommonDao.get(BDCS_XMXX.class, xmbh);
		List<BDCS_DJDY_GZ> djdygz = baseCommonDao.getDataList(BDCS_DJDY_GZ.class, "XMBH='"+xmbh+"'");
		String djlx = xmxx.getDJLX();
		String djxl = null;
		String djsfid= UUID.randomUUID().toString().replace("-","");
		String sfdyid= UUID.randomUUID().toString().replace("-","");
		//吉林市
		if (xzqdm.contains("2202") && djdygz.size()==1) {  // 吉林多单元要求其手动添加
			List<BDCS_DJSF> djsf = baseCommonDao.getDataList(BDCS_DJSF.class, "XMBH='"+xmbh+"'");
			List<BDCS_H_GZ> H_GZ= baseCommonDao.getDataList(BDCS_H_GZ.class, "BDCDYH='"+djdygz.get(0).getBDCDYH()+"'");//判断各个流程的收费名称 标准
			List<BDCS_H_XZ> H_XZ= baseCommonDao.getDataList(BDCS_H_XZ.class, "BDCDYH='"+djdygz.get(0).getBDCDYH()+"'");//因有首次 转移 且GZ XZ都找一遍
			List<BDCS_H_XZY> H_XZY= baseCommonDao.getDataList(BDCS_H_XZY.class, "BDCDYID='"+djdygz.get(0).getBDCDYID()+"'");//因有首次 转移 且GZ XZ都找一遍
			List<BDCS_QL_GZ> QL_GZ= baseCommonDao.getDataList(BDCS_QL_GZ.class, "XMBH='" + xmbh + "' AND DJDYID='" + djdygz.get(0).getDJDYID() + "'");//看QL中的CZFS 用于工本费
			List<BDCS_QLR_GZ> QLR_GZ= baseCommonDao.getDataList(BDCS_QLR_GZ.class, "XMBH='" + xmbh + "'");  //看有几个权利人
			String CZFSRS = QL_GZ.get(0).getCZFS();
			String sqlpro = "select PROINST_CODE,PRODEF_NAME from bdc_workflow.wfi_proinst where file_number='"   //判斷各種流程
					+ xmxx.getPROJECT_ID() + "'";
			List<Map> proinst_ids = baseCommonDao.getDataListByFullSql(sqlpro);
			Map proinst = proinst_ids.get(0);
			String SFKMMC = null;
			String SFJSALL = null;
			String SFLX = null;
			String SFDLMC = "房屋类";  // 房屋类多设为初始值。
			String proinst_id = "";  // 判断流程具体标识
		//	String prodef_nm = "";
		//	prodef_nm = proinst.get("PRODEF_NAME").toString();
			proinst_id = proinst.get("PROINST_CODE").toString();
			if(djsf.size()==0){
				if(djlx.equals("100") || djlx.equals("200") || djlx.equals("300") && !djlx.equals("400") && !djlx.equals("700") && !djlx.equals("800")){ //首次 转移  变更（查封 注销 预告 更正 依职权更正登记：不收费）
					if(proinst_ids != null && proinst_ids.size() > 0){
						if(proinst_id.equals("200006012") || proinst_id.equals("300003010")|| proinst_id.equals("300003013")){  // 转移中的夫妻更名,不动产登记簿记载的共有人申请分别持证的 工本费 10 元本   房屋所有权-变更-增加共有人
							SFKMMC = "工本费(工本费)";
							SFJSALL = "10";	
							SFLX = "1";
							SFDLMC = "土地类";
						}else if(proinst_id.equals("200003012")){  //转移征收拆迁偿还
						if(H_GZ.size()!=0){
						   if(H_GZ.get(0).getFWLX().equals("1")){
								SFKMMC = "登记费(住宅（全）)";
								SFJSALL = "80";	
								SFLX = "5";
						  }
//						   else if(H_GZ.get(0).getFWLX().equals("6")){    //车库免收费。
//								return;
//							}
						   else{
								SFKMMC = "登记费(非住宅（全）)";
								SFJSALL = "550";
								SFLX = "1";
							}
						}else if(H_XZ.size()!=0){
						if(H_XZ.get(0).getFWLX().equals("1")){
							SFKMMC = "登记费(住宅（全）)";
							SFJSALL = "80";	
							SFLX = "5";
						}
//						else if(H_XZ.get(0).getFWLX().equals("6")){    //车库免收费。
//							return;
//						}
						else{
							SFKMMC = "登记费(非住宅（全）)";
							SFJSALL = "550";
							SFLX = "1";
						}
					}
			}else if(proinst_id.equals("300003003") || proinst_id.equals("300003004") || proinst_id.equals("300003005") || proinst_id.equals("300003007")){  //变更 300003003 面积 300003004用途 300003005 权利期限 300003007房屋性质
//							if(H_GZ.get(0).getFWLX().equals("1") || H_XZ.get(0).getFWLX().equals("1")){
//								SFKMMC = "登记费(住宅（全）)";
//								SFJSALL = "80";	
//								SFLX = "5";
//							}
							if(H_GZ.size()!=0){  //!H_GZ.get(0).getFWLX().equals("")
								if(H_GZ.get(0).getFWLX().equals("1")){
									SFKMMC = "登记费(住宅（全）)";
									SFJSALL = "80";	
									SFLX = "5";
								}
//								else if(H_GZ.get(0).getFWLX().equals("6")){    //车库免收费。
//									return;
//								}
								else{
									SFKMMC = "登记费(非住宅（全）)";
									SFJSALL = "550";
									SFLX = "1";
								}
								}else if(H_XZ.size()!=0){
								if(H_XZ.get(0).getFWLX().equals("1")){
									SFKMMC = "登记费(住宅（全）)";
									SFJSALL = "80";	
									SFLX = "5";
								}
							}
//								else if(H_XZ.get(0).getFWLX().equals("6")){    //车库免收费。
//								return;
//							}
								else{
								SFKMMC = "登记费(非住宅（全）)";
								SFJSALL = "550";
								SFLX = "1";
							}
//							else if(H_GZ.size()!=0){
//								if(H_GZ.get(0).getFWLX().equals("6")){
//									return;
//								}
//								}else if(H_XZ.size()!=0){
//								if(H_XZ.get(0).getFWLX().equals("6")){
//									return;
//								}else{
//								SFKMMC = "登记费(非住宅（全）)";
//								SFJSALL = "550";
//								SFLX = "1";
//							}
//					}
				}else if(proinst_id.equals("300003001") || proinst_id.equals("300003006")){  //变更  收费减半的流程 300003001 权利人身份 300003006 同一权利人因分割 合并
//							if(H_GZ.get(0).getFWLX().equals("1") || H_XZ.get(0).getFWLX().equals("1")){
//								SFKMMC = "登记费(住房（减半）)";
//								SFJSALL = "40";	
//								SFLX = "3";
//							}	
							if(H_GZ.size()!=0){
							   if(H_GZ.get(0).getFWLX().equals("1")){
								   SFKMMC = "登记费(住房（减半）)";
									SFJSALL = "40";	
									SFLX = "3";
							  }
//							   else if(H_GZ.get(0).getFWLX().equals("6")){    //车库免收费。
//									return;
//								}
							   else{
									SFKMMC = "登记费(非住宅（减半）)";
									SFJSALL = "275";
									SFLX = "3";
								}
							}else if(H_XZ.size()!=0){
							if(H_XZ.get(0).getFWLX().equals("1")){
								 SFKMMC = "登记费(住房（减半）)";
								 SFJSALL = "40";	
								 SFLX = "3";
							}
//							else if(H_XZ.get(0).getFWLX().equals("6")){    //车库免收费。
//								return;
//							}
							else{
								SFKMMC = "登记费(非住宅（减半）)";
								SFJSALL = "275";
								SFLX = "3";
							}
						}
				}else if(proinst_id.equals("300003002") || proinst_id.equals("300002002")){  //变更  房屋土地名称 坐落  免收
							return;
							}else if (!djlx.equals("300")){  // 剩余的 首次 转移
								if(H_GZ.size()!=0){  //H_GZ.size()!=0
									if(H_GZ.get(0).getFWLX().equals("1")){
										SFKMMC = "登记费(住宅（全）)";
										SFJSALL = "80";	
										SFLX = "5";
									}
//									else if(H_GZ.get(0).getFWLX().equals("6")){    //车库免收费。
//										return;
//									}
									else{
										SFKMMC = "登记费(非住宅（全）)";
										SFJSALL = "550";
										SFLX = "1";
									}
								}else if(H_XZ.size()!=0){
									if(H_XZ.get(0).getFWLX().equals("1")){
										SFKMMC = "登记费(住宅（全）)";
										SFJSALL = "80";	
										SFLX = "5";
									}
//									else if(H_XZ.get(0).getFWLX().equals("6")){
//										return;
//									}
									else{
										SFKMMC = "登记费(非住宅（全）)";
										SFJSALL = "550";
										SFLX = "1";
									}
								}else{
									System.err.println("无房屋类型");
								}
		       	}
					     }
					}else if(djlx.equals("500") || djlx.equals("600") || proinst_id.equals("700003333")){  //减半流程都在这  更正登记-依申请更正-预购商品房预告更正
//	       		if(prodef_nm.indexOf("依职权更正登记")!=-1){  // 依职权更正不收费
//					return;
//				}
//	       		if(H_GZ.get(0).getFWLX().equals("1") || H_XZ.get(0).getFWLX().equals("1")){
//					SFKMMC = "登记费(住房（减半）)";
//					SFJSALL = "40";	
//					SFLX = "3";
//				}
	       		if(H_GZ.size()!=0){
					   if(H_GZ.get(0).getFWLX().equals("1")){
						   SFKMMC = "登记费(住房（减半）)";
							SFJSALL = "40";	
							SFLX = "3";
					  }
//					   else if(H_GZ.get(0).getFWLX().equals("6")){
//							return;
//						}
					   else{
							SFKMMC = "登记费(非住宅（减半）)";
							SFJSALL = "275";
							SFLX = "3";
						}
					}else if(H_XZ.size()!=0){  //!H_XZ.get(0).getFWLX().equals("")
					if(H_XZ.get(0).getFWLX().equals("1")){
						 SFKMMC = "登记费(住房（减半）)";
						 SFJSALL = "40";	
						 SFLX = "3";
					}
//					else if(H_XZ.get(0).getFWLX().equals("6")){
//						return;
//					}
					else{
						SFKMMC = "登记费(非住宅（减半）)";
						SFJSALL = "275";
						SFLX = "3";
					}
				}else if(proinst_id.equals("500002030") || proinst_id.equals("700003333")){ //商品房抵押预告更正  || 预购商品房预告更正 
					if(H_XZY.size()!=0){
					if(H_XZY.get(0).getFWLX().equals("1")){
						 SFKMMC = "登记费(住房（减半）)";
						 SFJSALL = "40";	
						 SFLX = "3";
					}else{
						SFKMMC = "登记费(非住宅（减半）)";
						SFJSALL = "275";
						SFLX = "3";
					  }
					}
				}else{
					SFKMMC = "登记费(非住宅（减半）)";
					SFJSALL = "275";
					SFLX = "3";
				}
	      }else if(djlx.equals("900")){
	       		if(proinst_id.equals("300003015") || proinst_id.equals("300003017")){  // 补发 换发 工本费 10 元本    300003015 变更其他  300003017 变更其他特殊
	       			if(H_GZ.size()!=0){  //!H_GZ.get(0).getFWLX().equals("")
						if(H_GZ.get(0).getFWLX().equals("1")){
							SFKMMC = "登记费(住宅（全）)";
							SFJSALL = "80";	
							SFLX = "5";
						}
//						else if(H_GZ.get(0).getFWLX().equals("6")){    //车库免收费。
//							return;
//						}
						else{
							SFKMMC = "登记费(非住宅（全）)";
							SFJSALL = "550";
							SFLX = "1";
						}
						}else if(H_XZ.size()!=0){
						if(H_XZ.get(0).getFWLX().equals("1")){
							SFKMMC = "登记费(住宅（全）)";
							SFJSALL = "80";	
							SFLX = "5";
						}else{
							SFKMMC = "登记费(非住宅（全）)";
							SFJSALL = "550";
							SFLX = "1";
						}
					}
//						else if(H_XZ.get(0).getFWLX().equals("6")){    //车库免收费。
//						return;
//					}
						else{
						SFKMMC = "登记费(非住宅（全）)";
						SFJSALL = "550";
						SFLX = "1";
					}
				}else{
					SFKMMC = "工本费(工本费)";
					SFJSALL = "10";	
					SFLX = "1";
					SFDLMC = "土地类";
				}
	       	}
				SaveSfxx(djsfid,xmxx,SFKMMC,SFJSALL,SFLX,sfdyid,SFDLMC,page,rows,xmbh,CZFSRS);
				if(QL_GZ.get(0).getCZFS().equals("02") && QLR_GZ.size()>1){
				    djsfid= UUID.randomUUID().toString().replace("-","");
				    sfdyid= UUID.randomUUID().toString().replace("-","");
					int GBALL = 10 * (QLR_GZ.size()-1);
					SFKMMC = "工本费(工本费)";
					SFJSALL = String.valueOf(GBALL);	
					SFLX = "1";
					SFDLMC = "土地类";
					SaveSfxx(djsfid,xmxx,SFKMMC,SFJSALL,SFLX,sfdyid,SFDLMC,page,rows,xmbh,CZFSRS);
				}
			}else{
				System.err.println("多单元。。。");
			}
	        	}
	}
	
	/**
	 * 保存对应的收费信息 表
	 * 
	 * @Title: SaveSfxx
	 * @author:YX
	 * @date：2017年5月24日 下午6:37:48
	 * @param xmbh
	 * @param page
	 * @param rows
	 * @return
	 * @throws InterruptedException 
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public void SaveSfxx(String djsfid,BDCS_XMXX xmxx,String SFKMMC,String SFJSALL,String SFLX,String sfdyid,String SFDLMC,int page,int rows,String xmbh,String CZFSRS) {
		String sql="";
		String[] vls = new String[18];
		vls[0] = djsfid;
		vls[1] = xmxx.getPROJECT_ID();
		vls[2] = SFKMMC;
		vls[3] = "0";
		vls[4] = SFJSALL;
		vls[5] = SFLX;
		vls[6] = SFJSALL;
		vls[7] = SFJSALL;
		//vls[6] = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(slsj); 吉林市不动产登记中心
		vls[8] = "吉林市不动产登记中心";
		vls[9] = xmxx.getId();
		vls[10] = "1";
		vls[11] = "元";
		vls[12] = sfdyid;
		vls[13] = SFJSALL;
		vls[14] = "EXPRESSION";
		vls[15] = "1";
	    vls[16] = SFJSALL;
		vls[17] = "收费科";
		sql = "insert into BDCK.BDCS_DJSF (ID,YWH,SFKMMC,SFEWSF,SFJS,SFLX,YSJE,SSJE,SFDW,XMBH,SFBL,JFDW,SFDYID,JSGS,CALTYPE,TS,XSGS,SFBMMC) values ('"
				+ vls[0]
				+ "','"
				+ vls[1]
				+ "','"
				+ vls[2]
				+ "','"
				+ vls[3]
				+ "','"
				+ vls[4]
				+ "','"
				+ vls[5]
				+ "','"
				+ vls[6]
				+ "','"
				+ vls[7]
				+ "','"
				+ vls[8]
				+ "','"
				+ vls[9]
				+ "','"
				+ vls[10]
				+ "','"
				+ vls[11]
			    + "','"
				+ vls[12]
				+ "','"
				+ vls[13]
				+ "','"
				+ vls[14]
				+ "','"
				+ vls[15]
				+ "','"
				+ vls[16]
				+ "','"
				+ vls[17]+"')";
		String tablename=BDCS_DJSF.class.getName();
		String[] otherfields = { "ID", "YWH","SFKMMC","SFEWSF","SFJS","SFLX","YSJE","SSJE","SFDW","XMBH","SFBL","JFDW","SFDYID","JSGS","CALTYPE","TS","XSGS","SFBMMC"};
		String[] othervalues = { vls[0],vls[1],vls[2],vls[3],vls[4],vls[5],vls[6],vls[7],vls[8],vls[9],vls[10],vls[11],vls[12],vls[13],vls[14],vls[15],vls[16],vls[17]};
		//一个项目推送要一个事务提交，在工具类里增加通用方法直接返回待保存对象 YX
		try {
			//将对应的收费标准值写入库中
			//BDCS_DJSF bdcsdjsf=Tools.XmxxcreatePushObj(tablename, otherfields, othervalues);
			//baseCommonDao.(bdcsdjsf);
			if(sql.length() != 0){
				baseCommonDao.excuteQueryNoResult(sql);
				baseCommonDao.flush();
				SaveSfdy(xmbh,page,rows,SFDLMC,sfdyid,CZFSRS);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 保存对应的 bdcs_sfdy 表
	 * 
	 * @Title: SaveSfdy
	 * @author:YX
	 * @date：2017年5月24日 下午6:37:48
	 * @param xmbh
	 * @param page
	 * @param rows
	 * @return
	 * @throws InterruptedException 
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public void SaveSfdy(String xmbh,int page,int rows,String SFDLMC,String sfdyid,String CZFSRS) {
		List<BDCS_DJSF> djsf = baseCommonDao.getDataList(BDCS_DJSF.class, "XMBH='"+xmbh+"'");
		BDCS_DJSF djsf1 = djsf.get(0);
		BDCS_SFDY sfdy = new BDCS_SFDY();
		//sfdy.setId(djsf1.getSFDYID());
		sfdy.setId(sfdyid);
		sfdy.setSFDLMC(SFDLMC);
		sfdy.setSFXLMC("登记费");
		sfdy.setSFKMMC(djsf1.getSFKMMC());
		sfdy.setSFLX(djsf1.getSFLX());
		sfdy.setSFJS(djsf1.getSFJS());
		sfdy.setSFBL(djsf1.getSFBL());
		sfdy.setJSGS(djsf1.getJSGS());
		sfdy.setCREAT_TIME(new Date());
		sfdy.setSFDW(djsf1.getJFDW());
		sfdy.setCALTYPE(djsf1.getCALTYPE());
		sfdy.setSFBMMC(djsf1.getSFBMMC());
		baseCommonDao.save(sfdy);
		baseCommonDao.flush();
//		try
//		{
//		  Thread.currentThread().sleep(10000);
//		}catch(Exception e){}
//		List<BDCS_SFDY> sfdy1 = baseCommonDao.getDataList(BDCS_SFDY.class, "ID='"+xmbh+"'");
		if(CZFSRS.equals("02")){     // 分别持证发两份
			if(djsf.size() == 2){
				getPagedSFList(xmbh,page,rows);
			}
		}else{
			getPagedSFList(xmbh,page,rows);
		}
		
	}

	/**
	 * 获取收费列表，分页
	 * 
	 * @Title: GetPagedSFList
	 * @author:liushufeng
	 * @date：2015年7月18日 下午6:37:48
	 * @param xmbh
	 * @param page
	 * @param rows
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Message getPagedSFList(String xmbh, int page, int rows) {
		String xzqdm = ConfigHelper.getNameByValue("XZQHDM");
		if (xzqdm.contains("2202")) {
			try
			{
			  Thread.currentThread().sleep(1500);  //延时0.1秒用于加载
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		Message msg = new Message();
		String condition = "SELECT SFDY.SFXLMC,SFDY.SFKMMC AS DYKMMC,SF.* FROM BDCK.BDCS_DJSF SF LEFT JOIN BDCK.BDCS_SFDY SFDY ON SFDY.ID = SF.SFDYID  WHERE SF.XMBH='"
				+ xmbh + "' ORDER BY SFDY.SFBMMC,SFDY.SFXLMC,SFDY.SFKMMC";
		List<Map> result = baseCommonDao.getPageDataByFullSql(condition, page,
				rows);
		List<Map> list = new ArrayList<Map>();
		BDCS_DJSF djsf = null;
		for (int i = 0; i < result.size(); i++) {
			Map map = result.get(i);
			djsf = new BDCS_DJSF();
			djsf.setId((String) map.get("ID"));
			djsf.setYWH((String) map.get("YWH"));
			djsf.setYSDM((String) map.get("YSDM"));
			djsf.setJFRY((String) map.get("JFRY"));
			djsf.setJFRQ((Date) map.get("JFRQ"));
			djsf.setSFKMMC((String) map.get("SFKMMC"));
			djsf.setSFEWSF((String) map.get("SFEWSF"));
			if (null != map.get("SFJS")) {
				Object o = map.get("SFJS");
				djsf.setSFJS(StringHelper.getDouble(o));
			}
			djsf.setSFLX((String) map.get("SFLX"));
			if (null != map.get("YSJE")) {
				Object o = map.get("YSJE");
				djsf.setYSJE(StringHelper.getDouble(o));
			}
			if (null != map.get("ZKHYSJE")) {
				Object o = map.get("ZKHYSJE");
				djsf.setZKHYSJE(StringHelper.getDouble(o));
			}
			djsf.setSFRY((String) map.get("SFRY"));
			djsf.setSFRQ((Date) map.get("SFRQ"));
			djsf.setFFF((String) map.get("FFF"));
			djsf.setSJFFR((String) map.get("SJFFR"));
			djsf.setSSJE((String) map.get("SSJE"));
			djsf.setSFDW((String) map.get("SFDW"));
			djsf.setXMBH((String) map.get("XMBH"));
			djsf.setSFBMMC((String) map.get("SFBMMC"));
			if (null != map.get("MJJS")) {
				Object o = map.get("MJJS");
				djsf.setMJJS(StringHelper.getDouble(o));
			}
			if (null != map.get("MJZL")) {
				Object o = map.get("MJZL");
				djsf.setMJZL(StringHelper.getDouble(o));
			}
			if (null != map.get("SFZL")) {
				Object o = map.get("SFZL");
				djsf.setSFZL(StringHelper.getDouble(o));
			}
			if (null != map.get("SFSX")) {
				Object o = map.get("SFSX");
				djsf.setSFSX(StringHelper.getDouble(o));
			}
			if (null != map.get("SFBL")) {
				Object o = map.get("SFBL");
				djsf.setSFBL(StringHelper.getDouble(o));
			}
			djsf.setJFDW((String) map.get("JFDW"));
			djsf.setSFDYID((String) map.get("SFDYID"));
			djsf.setJSGS((String) map.get("JSGS"));
			djsf.setBZ((String) map.get("BZ"));
			djsf.setSfxlmc((String) map.get("SFXLMC"));
			if (!StringHelper.isEmpty(map.get("TS"))
					&& !map.get("TS").toString().equals("0")) {
				djsf.setTS(Integer.parseInt(map.get("TS").toString()));
			} else {
				djsf.setTS(0);
			}
			djsf.setXSGS(StringHelper.isEmpty(map.get("XSGS")) ? "" : map.get(
					"XSGS").toString());

			List<String> listqlr1 = new ArrayList<String>();
			List<String> listsqrdh = new ArrayList<String>();
			List<String> listqlr_all = new ArrayList<String>();
			
			// 收费环节收费项前添加权利人名称
			List<BDCS_SQR> listsqr = baseCommonDao.getDataList(BDCS_SQR.class,
			ProjectHelper.GetXMBHCondition(xmbh) + " ORDER BY SQRLB,SXH");
			if (listsqr.size() > 0) {
				for (BDCS_SQR sqr : listsqr) {
					if (ConstValue.SQRLB.JF.Value.equals(sqr.getSQRLB())) { // 权利人
						if (!StringHelper.isEmpty(sqr.getSQRXM())
								&& !listqlr1.contains(sqr.getSQRXM())) {
							listqlr1.add(sqr.getSQRXM());
						}
						// 获取权利人电话
						if (!StringHelper.isEmpty(sqr.getLXDH())
								&& !listsqrdh.contains(sqr.getLXDH())) {
							listsqrdh.add(sqr.getSQRXM() + "(" + sqr.getLXDH()
									+ ")");
						}
					}
					if (!StringHelper.isEmpty(sqr.getSQRXM())
							&& !listqlr_all.contains(sqr.getSQRXM())) {
						listqlr_all.add(sqr.getSQRXM());
					}
				}
				if(StringHelper.isEmpty(map.get("QLRMC"))){
					djsf.setQLRMC(StringHelper.formatList(listqlr1));
				}else{
					List<String> listqlrs = new ArrayList<String>();
					listqlrs.add(map.get("QLRMC").toString());
					djsf.setQLRMC(StringHelper.formatList(listqlrs));
				}
			}

			// djsf.setQLRMC((String) map.get("QLRMC"));

			// 传入权利人电话
			Map tomap = StringHelper.beanToMap(djsf);
			tomap.put("dykmmc",StringHelper.formatObject(map.get("DYKMMC")));
			tomap.put("sfjb",StringHelper.formatObject(map.get("SFJB")));
			if (null != listsqrdh && listsqrdh.size() > 0) {
				tomap.put("qlr_lxdh", StringHelper.formatList(listsqrdh));
			} else {
				tomap.put("qlr_lxdh", "");
			}
			tomap.put("QlrsbySqr", StringHelper.formatList(listqlr1));
			tomap.put("Qlr_All", StringHelper.formatList(listqlr_all));

			list.add(tomap);
		}

		msg.setSuccess("0");
		BDCS_XMXX xmxx = baseCommonDao.get(BDCS_XMXX.class, xmbh);
		if (xmxx != null) {
			msg.setSuccess(xmxx.getSFQR());
		}

		msg.setRows(list);
		msg.setTotal(list.size());
		return msg;
	}

	/**
	 * 增加收费信息
	 * 
	 * @Title: AddSFXX
	 * @author:liushufeng
	 * @date：2015年7月18日 下午6:38:59
	 * @param sf
	 */
	@Transactional
	@Override
	public void addSFXX(BDCS_DJSF sf) {
		baseCommonDao.save(sf);
		baseCommonDao.flush();
	}

	@Override
	public BDCS_DJSF getSFXX(String id) {
		return baseCommonDao.get(BDCS_DJSF.class, id);
	}

	/**
	 * 更新收费信息
	 * 
	 * @Title: UpdateSFXX
	 * @author:liushufeng
	 * @date：2015年7月18日 下午6:39:17
	 * @param sf
	 */
	@Override
	public void updateSFXX(BDCS_DJSF sf) {
		baseCommonDao.update(sf);
		baseCommonDao.flush();
	}

	/**
	 * 删除收费信息
	 * 
	 * @Title: DeleteSFXX
	 * @author:liushufeng
	 * @date：2015年7月18日 下午6:39:29
	 * @param sfid
	 */
	@Override
	public void deleteSFXX(String sfid) {
		BDCS_DJSF sf = baseCommonDao.get(BDCS_DJSF.class, sfid);
		if (sf != null) {
			baseCommonDao.deleteEntity(sf);
			baseCommonDao.flush();
		}
	}

	/**
	 * 通过项目编号跟流程ID获取BDCS_DJGD信息
	 * 
	 * @作者 海豹
	 * @创建时间 2015年6月7日下午9:47:51
	 * @param xmbh
	 * @param projectid
	 * @return
	 */
	@Override
	public BDCS_DJGD getDjgdInfo(String xmbh, String projectid) {
		BDCS_DJGD djgd = null;
		StringBuilder hqlCondition = new StringBuilder();
		hqlCondition.append("  XMBH ='").append(xmbh).append("' ");
		hqlCondition.append(" AND YWH ='").append(projectid).append("' ");
		List<BDCS_DJGD> list = baseCommonDao.getDataList(BDCS_DJGD.class,
				hqlCondition.toString());
		if (list != null && list.size() > 0) {
			djgd = list.get(0);
		} else {
			StringBuilder hql = new StringBuilder();
			StringBuilder qzh = new StringBuilder();
			hql.append(" id in (SELECT QLID FROM BDCS_QDZR_GZ WHERE XMBH = '")
					.append(xmbh).append("') ");
			List<BDCS_QL_GZ> qlList = baseCommonDao.getDataList(
					BDCS_QL_GZ.class, hql.toString());
			// for (int i = 0; i < qlList.size(); i++) {
			// BDCS_QL_XZ ql = qlList.get(0);
			// if(qlList.size() - 1 == i){
			// qzh.append(ql.getBDCQZH());
			// }else{
			// qzh.append(ql.getBDCQZH()).append(",");
			// }
			// }
			if (qlList != null && qlList.size() > 0) {
				BDCS_QL_GZ ql = qlList.get(0);
				if (!StringUtils.isEmpty(ql.getBDCQZH())) {
					String[] bdcqzh = ql.getBDCQZH().split(",");
					if (bdcqzh.length > 0) {
						qzh.append(bdcqzh[0]);
						djgd = new BDCS_DJGD();
						djgd.setQZHM(qzh.toString());
					}
				}
			} else {
				djgd = new BDCS_DJGD();
				djgd.setQZHM("");
			}
		}
		
		List<Wfi_ProInst> listproinst=baseCommonDao.getDataList(Wfi_ProInst.class, "FILE_NUMBER='"+projectid+"'");
		if(listproinst.size()>0){
			Wfi_ProInst proinst=listproinst.get(0);
			String proef_name=proinst.getProdef_Name();
			StringBuffer sb=new StringBuffer();
			if(!StringHelper.isEmpty(proef_name)){
				String[] arrname=proef_name.split(",");
				String lastname=proef_name.replaceAll(",", "-");
				djgd.setDJDL(arrname[0]);
				djgd.setDJXL(lastname.substring(6, lastname.length()));
			}
			
		}
		return djgd;
	}

	/**
	 * 获取登记归档附属信息
	 * 
	 * @作者 刘树峰
	 * @创建时间 2015年6月23日上午6:46:35
	 * @param xmbh
	 * @param project_id
	 * @return
	 */
	@Override
	public List<BDCS_DJGDFS> getDJGDFSList(String xmbh, String project_id) {
		String strxmbh = ProjectHelper.GetXMBHCondition(xmbh);
		List<BDCS_DJGDFS> list = baseCommonDao.getDataList(BDCS_DJGDFS.class,
				strxmbh);
		if (list != null && list.size() > 0) {

		} else {
			BDCS_XMXX xmxx = baseCommonDao.get(BDCS_XMXX.class, xmbh);
			List<Tree> listtree = _SmProMater.GetMaterDataTree(project_id,
					false);
			for (int i = 0; i < listtree.size(); i++) {
				Tree tree = listtree.get(i);
				BDCS_DJGDFS fs = new BDCS_DJGDFS();
				fs.setXMBH(xmbh);
				fs.setSJSJ(xmxx.getSLSJ());
				fs.setSJRY(xmxx.getSLRY());
				fs.setCATALOG(tree.getText());
				fs.setFILENAME(tree.getText());
				baseCommonDao.save(fs);

				// if (tree.children.size() <= 0)
				// continue;
				// for (int j = 0; j < tree.children.size(); j++) {
				// Tree childtree = (Tree) tree.getChildren().get(j);
				// BDCS_DJGDFS fs = new BDCS_DJGDFS();
				// fs.setXMBH(xmbh);
				// fs.setSJSJ(xmxx.getSLSJ());
				// fs.setSJRY(xmxx.getSLRY());
				// fs.setCATALOG(tree.getText());
				// fs.setFILENAME(childtree.getText());
				// baseCommonDao.save(fs);
				// }
			}
		}
		baseCommonDao.flush();

		List<BDCS_DJGDFS> fss = baseCommonDao.getDataList(BDCS_DJGDFS.class,
				strxmbh);
		for (int i = 0; i < fss.size(); i++) {
			fss.get(i).setSJSJ(fss.get(i).getSJSJ());
		}
		return fss;
	}

	/**
	 * 保存或更新BDCS_DJGD信息
	 * 
	 * @作者 海豹
	 * @创建时间 2015年6月7日下午9:48:06
	 * @param bdcs_djgd
	 */
	@Override
	public ResultMessage saveOrUpdateDjgd(BDCS_DJGD bdcs_djgd, String djgdid) {
		ResultMessage ms = new ResultMessage();
		// 先判断卷宗号是否存在
		List<BDCS_DJGD> listDJGD = baseCommonDao.getDataList(BDCS_DJGD.class,
				" JZH='" + bdcs_djgd.getJZH() + "' AND ID NOT IN ('" + djgdid
						+ "')");
		if (listDJGD != null && listDJGD.size() > 0) {
			ms.setMsg("保存失败，卷宗号已存在！");
			ms.setSuccess("false");
			YwLogUtil.addYwLog("登记归档-保存失败，卷宗号已存在！", ConstValue.SF.NO.Value,
					ConstValue.LOG.UPDATE);
			return ms;
		}
		// 目前没有一个好的方法，求建议，djgdid为""值，默认的方法为djSaveOrUpdate（），不传递参数的（为什么不用项目编号？求解释--俞学斌）
		if (!djgdid.equals("error")) {
			bdcs_djgd.setId(djgdid);
		}
		baseCommonDao.saveOrUpdate(bdcs_djgd);
		baseCommonDao.flush();
		ms.setSuccess("true");
		ms.setMsg(bdcs_djgd.getId());
		YwLogUtil.addYwLog("登记归档-保存成功", ConstValue.SF.YES.Value,
				ConstValue.LOG.UPDATE);
		return ms;
	}

	/**
	 * 获取申请审批表
	 * 
	 * @Title: getSQSPB
	 * @author:liushufeng
	 * @date：2015年7月18日 下午6:42:32
	 * @param xmbh
	 * @return
	 */
	@Override
	public SQSPB GetSQSPB(String xmbh, String acinstid,
			HttpServletRequest request) {
		SQSPB sqspb = new SQSPB();
		return sqspb.Create(xmbh, acinstid, baseCommonDao, smProSPService,
				request);
	}

	@Override
	public SQSPBex GetSQSPBex(String xmbh, String acinstid,
			HttpServletRequest request) {
		SQSPBex sqspb = new SQSPBex().build(xmbh, acinstid, smProSPService,
				request);
		return sqspb;// .Create(xmbh,acinstid,
						// baseCommonDao,smProSPService,request);
	}

	/**
	 * 保存或增加登记归档附属
	 * 
	 * @Title: saveOrUpdateDJGDFS
	 * @author:liushufeng
	 * @date：2015年7月18日 下午6:42:04
	 * @param gdfs
	 */
	@Override
	public void saveOrUpdateDJGDFS(BDCS_DJGDFS gdfs) {
		String id = gdfs.getId();
		BDCS_DJGDFS fs = baseCommonDao.get(BDCS_DJGDFS.class, id);
		if (fs != null) {
			fs.setFILENAME(gdfs.getFILENAME());
			fs.setCATALOG(gdfs.getCATALOG());
			fs.setSJSJ(gdfs.getSJSJ());
			fs.setSJRY(gdfs.getSJRY());
			baseCommonDao.update(fs);
		} else {
			baseCommonDao.save(gdfs);
		}
		baseCommonDao.flush();
	}

	/**
	 * 获取登记归档附属信息
	 * 
	 * @Title: getDJGDFSInfo
	 * @author:liushufeng
	 * @date：2015年7月18日 下午6:42:45
	 * @param xmbh
	 * @param id
	 * @return
	 */
	@Override
	public BDCS_DJGDFS getDJGDFSInfo(String xmbh, String id) {
		return baseCommonDao.get(BDCS_DJGDFS.class, id);
	}

	/**
	 * 删除登记归档附属
	 * 
	 * @Title: deleteDJGDFS
	 * @author:liushufeng
	 * @date：2015年7月18日 下午6:42:57
	 * @param xmbh
	 * @param id
	 * @return
	 */
	@Override
	public ResultMessage deleteDJGDFS(String xmbh, String id) {
		baseCommonDao.delete(BDCS_DJGDFS.class, id);
		baseCommonDao.flush();
		ResultMessage msg = new ResultMessage();
		msg.setMsg("删除成功");
		msg.setSuccess("true");
		return msg;
	}

	/**
	 * 获取地图配置信息
	 * 
	 * @Title: GetMapConfig
	 * @author:rongxianfeng
	 * @date：2015年7月18日 下午6:44:00
	 * @return
	 */
	@Override
	public JSONObject GetMapConfig() {
		JSONObject jsonObject = new JSONObject();
		try {
			String zd = ConfigHelper.getNameByValue("MAPURL_ZD");
			String fw = ConfigHelper.getNameByValue("MAPURL_FW");
			String fwy = ConfigHelper.getNameByValue("MAPURL_FWY");
			String house = ConfigHelper.getNameByValue("MAPURL_LPB_HOUSE");
			String floor = ConfigHelper.getNameByValue("MAPURL_FLOOR");
			String zh = ConfigHelper.getNameByValue("MAPURL_ZH");
			String ld = ConfigHelper.getNameByValue("MAPURL_LD");
			String syqzd = ConfigHelper.getNameByValue("MAPURL_SYQZD");
			String zrt = ConfigHelper.getNameByValue("MAPURL_ZRT");
			String nyd = ConfigHelper.getNameByValue("MAPURL_NYD");

			// TenantUser tuser = (TenantUser) Global.getCurrentUserInfo();
			// if (tuser != null) {
			// String sourceid = tuser.getSource() == null ? "" : tuser
			// .getSource().getId();
			// if (!StringHelper.isEmpty(sourceid)) {
			// // dataSource_360700_QJDC
			// int index = sourceid.lastIndexOf("_");
			// if (index > 0) {
			// String workspacename = sourceid.substring(index + 1,
			// sourceid.length());
			// String wkparam = "workspacename=" + workspacename + "&";
			// zd = insertString(zd, "\\?", wkparam);
			// fw = insertString(fw, "\\?", wkparam);
			// zh = insertString(zh, "\\?", wkparam);
			// ld = insertString(ld, "\\?", wkparam);
			// syqzd = insertString(syqzd, "\\?", wkparam);
			// }
			// }
			// }
			jsonObject.put("zd", zd);
			jsonObject.put("fw", fw);
			jsonObject.put("fwy", fwy);
			jsonObject.put("house", house);
			jsonObject.put("floor", floor);
			jsonObject.put("zh", zh);
			jsonObject.put("ld", ld);
			jsonObject.put("syqzd", syqzd);
			jsonObject.put("zrt", zrt);
			jsonObject.put("nyd", nyd);
			// 海口项目和一张图对接，需要的参数
			jsonObject.put("currentPsnId", DesHelper.encrypt_haikou(smStaff
					.getCurrentWorkStaff().getLoginName()));
			jsonObject.put("currentPsnName", DesHelper.encrypt_haikou(smStaff
					.getCurrentWorkStaff().getUserName()));
			jsonObject.put("currentPsnPassword",
					DesHelper.encrypt_haikou(smStaff.getCurrentWorkStaff()
							.getPassword()));
			System.out.print(jsonObject.toString());
			return jsonObject;
		} catch (Exception e) {
		}
		return jsonObject;
	}

	private String insertString(String sourcestr, String positionstr,
			String insertstr) {
		try {
			sourcestr = sourcestr.replaceFirst(positionstr, positionstr
					+ insertstr);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return sourcestr;
	}

	/**
	 * 获取不动产登记受理单/询问笔录/收费明细单信息
	 * 
	 * @Title: GetDJInfo
	 * @author:yuxuebin
	 * @date：2015年7月18日 下午6:44:24
	 * @param project_id
	 * @return
	 */

	@Override
	public DJInfo GetDJInfo(String project_id) {
		return DJInfo.Create(project_id, baseCommonDao, smHoliday);
	}

	/**
	 * 导出XML交换文件，返回路径
	 * 
	 * @Title: exportXML
	 * @author:liushufeng
	 * @date：2015年7月18日 下午6:44:36
	 * @param xmbh
	 * @param path
	 * @return
	 */
	@Override
	public Map<String, String> exportXML(String xmbh, String path,
			String actinstID) {
		DJHandler handler = HandlerFactory.createDJHandler(xmbh);
		if (handler != null) {
			String bizMsgPath = ConfigHelper.getNameByValue("BIZMSGPATH");
			//更改报文存放路径
			path = path + bizMsgPath;
//			path = bizMsgPath; 
			File backfile = new File(path);
			if (!backfile.exists()) {
				FileUtils.createDirectory(path);
			}
			Map<String, String> xmlName = handler.exportXML(path, actinstID);
			if (StringHelper.isEmpty(xmlName)||xmlName.containsKey("error")) {
				BDCS_XMXX xm = Global.getXMXXbyXMBH(xmbh);
				if (xm != null) {
					xm.setSFSB(SF.NO.Value);
					baseCommonDao.update(xm);
				}
			} else {
				BDCS_XMXX xm = Global.getXMXXbyXMBH(xmbh);
				if (xm != null) {
					xm.setSFSB(SF.YES.Value);
					baseCommonDao.update(xm);
				}
			}
			
			List<BDCS_REPORTINFO> list_report=baseCommonDao.getDataList(BDCS_REPORTINFO.class, "XMBH='"+xmbh+"' AND YXBZ='1'");
			String reportstatus="1";
			if(list_report!=null&&list_report.size()>0){
				for(BDCS_REPORTINFO report:list_report){
					if("0".equals(report.getSUCCESSFLAG())){
						reportstatus="0";
						break;
					}else if("2".equals(report.getSUCCESSFLAG())){
						if("1".equals(reportstatus)){
							reportstatus="2";
						}
					}
				}
			}else{
				reportstatus="-1";
			}
			BDCS_XMXX xmxx=Global.getXMXXbyXMBH(xmbh);
			if(xmxx!=null){
				xmxx.setSFSB(reportstatus);
				baseCommonDao.update(xmxx);
				BDCS_XMXX xmxx1=Global.getXMXX(xmxx.getPROJECT_ID());
				if(xmxx1!=null){
					xmxx1.setSFSB(reportstatus);
				}
			}
			return xmlName;
		}
		return null;
	}

	/**
	 * 通过流程编号删除项目信息
	 * 
	 * @Title: DeleteXmxxByPrjID
	 * @author:sunhaibao
	 * @date：2015年7月18日 下午6:45:17
	 * @param project_id
	 * @return
	 */
	@Override
	public Boolean deleteXmxxByPrjID(String project_id) {
		// TODO @孙海豹，判断单元的地方考虑的情况不够全面
		// 获取登记类型、不动产单元类型及项目编号
		StringBuilder builder = new StringBuilder();
		builder.append(" project_id='").append(project_id).append("'");
		String sql = builder.toString();
		List<BDCS_XMXX> lstbdcs_xmxx = baseCommonDao.getDataList(
				BDCS_XMXX.class, sql);
		if (lstbdcs_xmxx == null || lstbdcs_xmxx.size() <= 0) {
			return false;
		} else {
			try {
				dbService.SendMsg(project_id, "0");
			} catch (Exception e) {
			}
			ProjectInfo info = ProjectHelper.GetPrjInfoByXMBH(lstbdcs_xmxx.get(
					0).getId());
			if (info != null) {
				String xmbh = info.getXmbh();// 获取项目编号
				String xmbhsql = ProjectHelper.GetXMBHCondition(xmbh);
				List<BDCS_DJDY_GZ> lstDjdy_gz = baseCommonDao.getDataList(
						BDCS_DJDY_GZ.class, xmbhsql);
				// 循环每个登记单元去删除具体的单元
				for (BDCS_DJDY_GZ djdy : lstDjdy_gz) {
					if (djdy == null)
						continue;
					String bdcdylxstr = djdy.getBDCDYLX();
					if (StringHelper.isEmpty(bdcdylxstr))
						continue;
					BDCDYLX lx = BDCDYLX.initFrom(bdcdylxstr);
					String _djdylystr = djdy.getLY();
					if (StringHelper.isEmpty(_djdylystr))
						continue;
					DJDYLY ly = DJDYLY.initFrom(_djdylystr);
					if (StringHelper.isEmpty(djdy.getBDCDYID()))
						continue;
					RealUnit unit = UnitTools.loadUnit(lx, ly,
							djdy.getBDCDYID());
					if (unit == null)// 获取单元
						continue;
					if (!xmbh.equals(unit.getXMBH())) // 项目编号
						continue;
					// 移除单元,,只能移除工作层里边的。
					if (unit.getLY().equals(DJDYLY.GZ)) {
						baseCommonDao.deleteEntity(unit);
					}
					// 初始登记，并且来源于工作层的，要相应的修改调查库里边的登记状态
					if (ly.equals(DJDYLY.GZ)
							&& info.getDjlx().equals(DJLX.CSDJ.Value)) {
						RealUnit _dcunit = UnitTools.loadUnit(lx, DJDYLY.DC,
								djdy.getBDCDYID());
						if (_dcunit == null)
							continue;
						_dcunit.setDJZT(DJZT.WDJ.Value);
					}
				}

				// /*************************** 具体单元
				// ***************************************/
				// if
				// (info.getBdcdylx().equals(ConstValue.BDCDYLX.SHYQZD.Value))
				// // 使用权宗地
				// {
				// for (BDCS_DJDY_GZ bdcs_djdy_gz : lstDjdy_gz) {
				// // 用项目编号判断一下
				// BDCS_SHYQZD_GZ shyqzd_gz =
				// baseCommonDao.get(BDCS_SHYQZD_GZ.class,
				// bdcs_djdy_gz.getBDCDYID());
				// if (shyqzd_gz != null && xmbh.equals(shyqzd_gz.getXMBH())) {
				// if (ConstValue.DJLX.CSDJ.Value.equals(info.getDjlx()))//
				// 更新对应的调查库中调查状态
				// {
				// DCS_SHYQZD_GZ dcs_shyqzd_gz =
				// baseCommonDao.get(DCS_SHYQZD_GZ.class,
				// bdcs_djdy_gz.getBDCDYID());
				// if (dcs_shyqzd_gz != null) {
				// dcs_shyqzd_gz.setDJZT("01");
				// baseCommonDao.update(dcs_shyqzd_gz);
				// }
				// }
				// baseCommonDao.deleteEntity(shyqzd_gz);
				// }
				// }
				// } else if
				// (info.getBdcdylx().equals(ConstValue.BDCDYLX.SYQZD.Value))//
				// 所有权宗地
				// {
				// for (BDCS_DJDY_GZ bdcs_djdy_gz : lstDjdy_gz) {
				// // 用项目编号判断一下
				// BDCS_SYQZD_GZ syqzd = baseCommonDao.get(BDCS_SYQZD_GZ.class,
				// bdcs_djdy_gz.getBDCDYID());
				// if (null != syqzd && xmbh.equals(syqzd.getXMBH())) {
				// if (ConstValue.DJLX.CSDJ.Value.equals(info.getDjlx())) {
				// DCS_SYQZD_GZ dcs_syqzd_gz =
				// baseCommonDao.get(DCS_SYQZD_GZ.class,
				// bdcs_djdy_gz.getBDCDYID());
				// if (dcs_syqzd_gz != null) {
				// dcs_syqzd_gz.setDJZT("01");
				// baseCommonDao.update(dcs_syqzd_gz);
				// }
				// }
				// baseCommonDao.deleteEntity(syqzd);
				// }
				// }
				// } else if
				// (info.getBdcdylx().equals(ConstValue.BDCDYLX.HY.Value))// 宗海
				// {
				// for (BDCS_DJDY_GZ bdcs_djdy_gz : lstDjdy_gz) {
				// // 用项目编号判断一下
				// BDCS_ZH_GZ zh = baseCommonDao.get(BDCS_ZH_GZ.class,
				// bdcs_djdy_gz.getBDCDYID());
				// if (null != zh && xmbh.equals(zh.getXMBH())) {
				// if (ConstValue.DJLX.CSDJ.Value.equals(info.getDjlx())) {
				// DCS_ZH_GZ dcs_zh_gz = baseCommonDao.get(DCS_ZH_GZ.class,
				// bdcs_djdy_gz.getBDCDYID());
				// if (dcs_zh_gz != null) {
				// dcs_zh_gz.setDJZT("01");
				// baseCommonDao.update(dcs_zh_gz);
				// }
				// }
				// baseCommonDao.deleteEntity(zh);
				// }
				// }
				// // 删除跟宗海关联的信息包括用海用地坐标及用海状况
				// baseCommonDao.deleteEntitysByHql(BDCS_YHYDZB_GZ.class,
				// xmbhsql);
				// baseCommonDao.deleteEntitysByHql(BDCS_YHZK_GZ.class,
				// xmbhsql);
				// } else if
				// (info.getBdcdylx().equals(ConstValue.BDCDYLX.LD.Value))//
				// 森林林地
				// {
				// for (BDCS_DJDY_GZ bdcs_djdy_gz : lstDjdy_gz) {
				// // 用项目编号判断一下
				// BDCS_SLLM_GZ sllm = baseCommonDao.get(BDCS_SLLM_GZ.class,
				// bdcs_djdy_gz.getBDCDYID());
				// if (null != sllm && xmbh.equals(sllm.getXMBH())) {
				// if (ConstValue.DJLX.CSDJ.Value.equals(info.getDjlx())) {
				// DCS_SLLM_GZ dcs_sllm_gz =
				// baseCommonDao.get(DCS_SLLM_GZ.class,
				// bdcs_djdy_gz.getBDCDYID());
				// if (dcs_sllm_gz != null) {
				// dcs_sllm_gz.setDJZT("01");
				// baseCommonDao.update(dcs_sllm_gz);
				// }
				// baseCommonDao.deleteEntity(sllm);
				// }
				// }
				// }
				// } else if
				// (info.getBdcdylx().equals(ConstValue.BDCDYLX.ZRZ.Value))//
				// 自然幢
				// {
				// for (BDCS_DJDY_GZ bdcs_djdy_gz : lstDjdy_gz) {
				// // 用项目编号判断一下
				// BDCS_ZRZ_GZ zrz = baseCommonDao.get(BDCS_ZRZ_GZ.class,
				// bdcs_djdy_gz.getBDCDYID());
				// if (null != zrz && xmbh.equals(zrz.getXMBH())) {
				// if (ConstValue.DJLX.CSDJ.Value.equals(info.getDjlx())) {
				// DCS_ZRZ_GZ dcs_zrz_gz = baseCommonDao.get(DCS_ZRZ_GZ.class,
				// bdcs_djdy_gz.getBDCDYID());
				// if (dcs_zrz_gz != null) {
				// dcs_zrz_gz.setDJZT("01");
				// baseCommonDao.update(dcs_zrz_gz);
				// }
				// }
				// baseCommonDao.deleteEntity(zrz);
				// }
				// }
				// } else if
				// (info.getBdcdylx().equals(ConstValue.BDCDYLX.H.Value))// 户
				// {
				// for (BDCS_DJDY_GZ bdcs_djdy_gz : lstDjdy_gz) {
				// // 用项目编号判断一下
				// BDCS_H_GZ h_gz = baseCommonDao.get(BDCS_H_GZ.class,
				// bdcs_djdy_gz.getBDCDYID());
				// if (null != h_gz && xmbh.equals(h_gz.getXMBH())) {
				// if (ConstValue.DJLX.CSDJ.Value.equals(info.getDjlx())) {
				// DCS_H_GZ dcs_h_gz = baseCommonDao.get(DCS_H_GZ.class,
				// bdcs_djdy_gz.getBDCDYID());
				// if (dcs_h_gz != null) {
				// dcs_h_gz.setDJZT("01");
				// baseCommonDao.update(dcs_h_gz);
				// }
				// }
				// baseCommonDao.deleteEntity(h_gz);
				// }
				// }
				// } else // TODO @刘树峰 当增加新的不动产单元的登记流程的时候，需要修改此处
				// {
				//
				// }
				/*************************** 审批层 ***************************************/
				baseCommonDao.deleteEntitysByHql(BDCS_XMXX.class, xmbhsql);// 删除项目信息
				baseCommonDao.deleteEntitysByHql(BDCS_SQR.class, xmbhsql);// 删除申请人
				baseCommonDao.deleteEntitysByHql(BDCS_DJSF.class, xmbhsql);// 删除登记收费
				baseCommonDao.deleteEntitysByHql(BDCS_DJFZ.class, xmbhsql);// 删除登记发证
				baseCommonDao.deleteEntitysByHql(BDCS_DJSZ.class, xmbhsql);// 删除登记膳证
				baseCommonDao.deleteEntitysByHql(BDCS_DJGD.class, xmbhsql);// 删除登记归档
				baseCommonDao.deleteEntitysByHql(BDCS_DJGDFS.class, xmbhsql);// 删除登记归档附属
				baseCommonDao.deleteEntitysByHql(BDCS_DJDY_GZ.class, xmbhsql);// 删除登记单元
				/*************************** 工作层 ***************************************/
				baseCommonDao.deleteEntitysByHql(BDCS_QDZR_GZ.class, xmbhsql);// 删除登记权利-单元-证书-权利人
				baseCommonDao.deleteEntitysByHql(BDCS_QL_GZ.class, xmbhsql);// 删除权利
				baseCommonDao.deleteEntitysByHql(BDCS_FSQL_GZ.class, xmbhsql);// 删除附属权利
				baseCommonDao.deleteEntitysByHql(BDCS_QLR_GZ.class, xmbhsql);// 删除权利人
				baseCommonDao.deleteEntitysByHql(BDCS_ZS_GZ.class, xmbhsql);// 删除证书
				baseCommonDao.deleteEntitysByHql(BDCS_DJDY_GZ.class, xmbhsql);// 删除证书
				baseCommonDao.flush();
			}
		}
		return true;
	}

	/**
	 * 计费
	 * 
	 * @Title: calculateCharge
	 * @author:liushufeng
	 * @date：2015年7月26日 上午3:49:50
	 * @param xmbh
	 */
	@Override
	public void calculateCharge(String xmbh) {
		DJHandler handler = HandlerFactory.createDJHandler(xmbh);
		if (handler != null) {
		}
	}

	/**
	 * @郭浩龙 获取所有收费定义 不分页 获取所有收费项目，除去在登记收费表DJSF中已有的记录（根据项目编号）
	 * @return
	 */
	@Override
	public Message getSFDYList(String xmbh, Integer page, Integer rows) {
		Message msg = new Message();
		// 获取某项目在BDCS_DJSF表中所有的收费项目定义
		List<BDCS_DJSF> listDJSF = baseCommonDao.getDataList(BDCS_DJSF.class,
				"XMBH='" + xmbh + "'");
		int num = listDJSF.size();
		String fleld = "";
		String sqlStr = "";
		if (num >= 1) {
			for (int i = 0; i < num; i++) {
				String sfdyid = listDJSF.get(i).getSFDYID();
				if (fleld == "") {
					fleld = "'" + sfdyid + "'";
				} else {
					fleld = fleld + "," + "'" + sfdyid + "'";
				}
			}
		} else {
			fleld = "''";
		}

		sqlStr = "ID NOT IN(" + fleld + ")";
		// 添加新的收费项的时候不过滤掉已经添加的，全部显示出来，相同的收费项可以重复添加

		sqlStr = "1>0";
		List<BDCS_SFDY> listSFDJ = baseCommonDao.getDataList(BDCS_SFDY.class,
				sqlStr);
		Page p = baseCommonDao.getPageDataByHql(BDCS_SFDY.class, sqlStr, page,
				rows);
		msg.setTotal(p.getTotalCount());
		msg.setRows(p.getResult());
		msg.setSuccess("true");
		return msg;
	}

	@Override
	public Map<String, String> downXmlFile(String path,
			Map<String, String> xmlNames, String actinstID) {
		String ftpPath = "";
		dataReport mySFTP = new dataReport();
		ChannelSftp sftp = mySFTP.getSftp();
		Map<String, String> bdcqzhs = new HashMap<String, String>();
		if (sftp != null) {
			try {
				ftpPath = ConfigHelper.getNameByValue("SFTPREPMSGPATH");
				Map item = mySFTP.downloadAllFile(ftpPath, path, sftp, null);
				String encoding = "UTF-8";
				System.out.println("响应报文地址：" + path);
				Set keys = xmlNames.keySet();
				String reccode = xmlNames.get("reccode");
				if (!StringUtils.isEmpty(reccode) && reccode.indexOf("#") > -1) {
					reccode = reccode.split("#")[1];
				}
				if (keys != null) {
					Iterator iterator = keys.iterator();
					while (iterator.hasNext()) {
						Object key = iterator.next();
						if ("reccode".equals(key)) {
							continue;
						}
						Object value = xmlNames.get(key);
						File file = new File(path + "Rep" + value);
						String successFlag = "";
						System.out.println("报文文件地址：" + path + "Rep" + value);
						if (file.isFile() && file.exists()) { // 判断文件是否存在
							System.out.println("文件存在");
							InputStreamReader read = new InputStreamReader(
									new FileInputStream(file), encoding);// 考虑到编码格式
							BufferedReader bufferedReader = new BufferedReader(
									read);
							String lineTxt = null;
							while ((lineTxt = bufferedReader.readLine()) != null) {
								System.out.println(lineTxt);
								if (lineTxt.indexOf("ResponseInfo") > -1
										|| lineTxt.indexOf("SuccessFlag") > -1) {
									String responseInfo = lineTxt.replace(
											"<ResponseInfo>", "").replace(
											"</ResponseInfo>", "");
									if (lineTxt.indexOf("SuccessFlag") > -1) {
										successFlag = lineTxt.replace(
												"<SuccessFlag>", "").replace(
												"</SuccessFlag>", "");
										successFlag = successFlag.trim();
									} else {
										responseInfo = responseInfo.trim();
										bdcqzhs.put("ResponseInfo",
												responseInfo);
									}
									if (!StringUtils.isEmpty(successFlag)
											&& lineTxt.indexOf("ResponseInfo") == -1) {
										continue;
									}
									if ("2".equals(successFlag)) { // 失败
										YwLogUtil
												.addSjsbResult(
														"Biz" + value,
														path + "Rep" + value,
														responseInfo,
														ConstValue.SF.NO.Value,
														reccode,
														ProjectHelper
																.getpRroinstIDByActinstID(actinstID));
									} else {
										YwLogUtil
												.addSjsbResult(
														"Biz" + value,
														path + "Rep" + value,
														responseInfo,
														ConstValue.SF.YES.Value,
														reccode,
														ProjectHelper
																.getpRroinstIDByActinstID(actinstID));
									}
									break;
								}
							}
							read.close();
						} else {
							YwLogUtil.addYwLog("数据上报-找不到指定的响应报文!",
									ConstValue.SF.NO.Value, ConstValue.LOG.ADD);
							YwLogUtil
									.addSjsbResult(
											"Biz" + value,
											path + "Rep" + value,
											"未读取到响应报文",
											ConstValue.SF.NO.Value,
											reccode,
											ProjectHelper
													.getpRroinstIDByActinstID(actinstID));
							System.out.println("找不到指定的文件");
						}
					}
				}
				return bdcqzhs;
			} catch (Exception e) {
				YwLogUtil.addYwLog("数据上报-未知错误!", ConstValue.SF.NO.Value,
						ConstValue.LOG.UNKNOWN);
				e.printStackTrace();
			} finally {
				sftp.disconnect();
				try {
					sftp.getSession().disconnect();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return bdcqzhs;
	}

	/**
	 * @param path
	 *            文件路径
	 * @param suffix
	 *            后缀名, 为空则表示所有文件
	 * @param isdepth
	 *            是否遍历子目录
	 * @return list
	 */
	public List<String> getListFiles(String path, String suffix, boolean isdepth) {
		List<String> lstFileNames = new ArrayList<String>();
		File file = new File(path);
		return this.listFile(lstFileNames, file, suffix, isdepth);
	}

	private List<String> listFile(List<String> lstFileNames, File f,
			String suffix, boolean isdepth) {
		// 若是目录, 采用递归的方法遍历子目录
		if (f.isDirectory()) {
			File[] t = f.listFiles();
			for (int i = 0; i < t.length; i++) {
				if (isdepth || t[i].isFile()) {
					listFile(lstFileNames, t[i], suffix, isdepth);
				}
			}
		} else {
			String filePath = f.getAbsolutePath();
			if (!suffix.equals("")) {
				int begIndex = filePath.lastIndexOf(".");
				String tempsuffix = "";
				if (begIndex != -1) {
					tempsuffix = filePath.substring(begIndex + 1,
							filePath.length());
					if (tempsuffix.equals(suffix)) {
						lstFileNames.add(filePath);
					}
				}
			} else {
				lstFileNames.add(filePath);
			}
		}
		return lstFileNames;
	}

	/**
	 * 获取项目编号（xmbh）
	 */

	public String GetXmbhData(String file_number) {

		String xmbhs = null;
		List<BDCS_XMXX> list = baseCommonDao.getDataList(BDCS_XMXX.class,
				"PROJECT_ID='" + file_number + "'");
		if (list.size() > 0) {
			BDCS_XMXX xmxx = list.get(0);
			xmbhs = xmxx.getId();
		}
		return xmbhs;

	}

	/**
	 * 获取申请审核表
	 * 
	 * 
	 * @author:mss
	 * @param xmbh
	 * @return
	 */
	@Override
	public SHB GetSHB(String project_id) {
		SHB shb = new SHB();
		return shb.createSHB(project_id);

	}

	@Override
	public ResultMessage setSFHBZS(String xmbh, String sfhbzs) {

		ResultMessage msg = new ResultMessage();
		BDCS_XMXX xmxx = Global.getXMXXbyXMBH(xmbh);

		// 先判断是否登簿，如果登簿了，就不让改了
		// 接着判断权利个数，如果权利个数<2,不改动，如果>1，清空权利，权利人，证书表里边的权证号
		if (xmxx != null) {
			if (SF.YES.Value.equals(xmxx.getSFDB())) {
				msg.setSuccess("false");
				msg.setMsg("项目已经写入登记簿，不能再修改证书个数！");
				return msg;
			}

			List<BDCS_QL_GZ> qls = baseCommonDao.getDataList(BDCS_QL_GZ.class,
					"XMBH='" + xmbh + "'");
			
			String newqzh = "";
			String workflowcode = ProjectHelper.getWorkflowCodeByProjectID(xmxx.getPROJECT_ID());
			List<WFD_MAPPING> listCode = baseCommonDao.getDataList(
					WFD_MAPPING.class, "WORKFLOWCODE='" + workflowcode + "'");
			if (listCode != null && listCode.size() > 0) {
				newqzh = listCode.get(0).getNEWQZH();
			}
			
			if (qls != null && qls.size() > 1) {
				for (BDCS_QL_GZ ql : qls) {
					if (!SF.NO.Value.equals(newqzh)) {
						ql.setBDCQZH("");
					}
					baseCommonDao.update(ql);
				}
				List<BDCS_ZS_GZ> zss = baseCommonDao.getDataList(
						BDCS_ZS_GZ.class, "XMBH='" + xmbh + "'");
				for (BDCS_ZS_GZ zs : zss) {
					if (!SF.NO.Value.equals(newqzh)) {
						zs.setBDCQZH("");
					}
					baseCommonDao.update(zs);
				}

				List<BDCS_QLR_GZ> qlrs = baseCommonDao.getDataList(
						BDCS_QLR_GZ.class, "XMBH='" + xmbh + "'");
				for (BDCS_QLR_GZ qlr : qlrs) {
					if (!SF.NO.Value.equals(newqzh)) {
						qlr.setBDCQZH("");
						qlr.setBDCQZHXH("");
					}
					baseCommonDao.update(qlr);
				}
			}
			xmxx.setSFHBZS(sfhbzs);
			baseCommonDao.update(xmxx);
			baseCommonDao.flush();
		}
		msg.setSuccess("true");
		return msg;
	}

	/**
	 * 获取登簿信息
	 * 
	 * @author:mss
	 * @param project_id
	 * @return
	 */

	@SuppressWarnings("unchecked")
	public Map<String, Object> GetDBXX(String project_id) {

		Map<String, Object> map = new HashMap<String, Object>();
		// 1,不动产单元号
		// String bdcdyh="" ;
		// 2、坐落
		// String zl="" ;
		// 3、权利类型
		String qllx = "";
		// 4、权证号
		// String bdcqzh = "";
		// 5 权利人
		// String qlrmc = "";
		// 6 项目编号
		String xmbh = "";
		// 7、 单元类型
		// String dylx="" ;
		// 8 登记类型
		String djlx = "";
		// 权利类型名称
		String qllxmc = "";
		// 单元类型名称
		String dylxmc = "";
		// 登记类型名称
		String djlxmc = "";
		// old项目编号
		String oldxmbh = "";

		// 1.通过project_id获取项目信息
		ProjectInfo info = ProjectHelper.GetPrjInfoByPrjID(project_id);
		if (!StringHelper.isEmpty(info)) {
			xmbh = info.getXmbh();
			djlx = info.getDjlx();
			djlxmc = info.getDjlxmc();
			map.put("xmbh", xmbh);
			map.put("djlx", djlx);
			map.put("djlxmc", djlxmc);

			List<Map> lstdjdy = new ArrayList<Map>();
			// 获取登记单元信息
			List<BDCS_DJDY_GZ> djdys = baseCommonDao.getDataList(
					BDCS_DJDY_GZ.class, ProjectHelper.GetXMBHCondition(xmbh));
			String bdcdylxmc = "";
			for (BDCS_DJDY_GZ bdcs_djdy_gz : djdys) {
				Map dy = new HashMap();
				BDCDYLX bdcdylx = BDCDYLX.initFrom(bdcs_djdy_gz.getBDCDYLX());
				DJDYLY dyly = DJDYLY.initFrom(bdcs_djdy_gz.getLY());
				RealUnit unit = UnitTools.loadUnit(bdcdylx, dyly,
						bdcs_djdy_gz.getBDCDYID());
				if (!StringHelper.isEmpty(unit)) {
					dy.put("bdcdyh", unit.getBDCDYH());
					dy.put("zl", unit.getZL());
					dy.put("bdcdylx", unit.getBDCDYLX().Value);
					bdcdylxmc = ConstHelper.getNameByValue("BDCDYLX",
							unit.getBDCDYLX().Value);
					dy.put("bdcdylxmc", bdcdylxmc);
					dy.put("bdcdyid", bdcs_djdy_gz.getBDCDYID());

				}
				Rights rights = RightsTools.loadRightsByDJDYID(DJDYLY.GZ, xmbh,
						bdcs_djdy_gz.getDJDYID());
				// Rights rights =RightsTools.loadRightsByCondition(DJDYLY.XZ,
				// "DJDYID='"+bdcs_djdy_gz.getDJDYID()+"'");

				Map ql = new HashMap();
				if (!StringHelper.isEmpty(rights)) {
					qllxmc = ConstHelper.getNameByValue("QLLX",
							rights.getQLLX());
					ql.put("qllx", rights.getQLLX());
					ql.put("qllxmc", qllxmc);
					ql.put("dbsj", rights.getDJSJ());
					if (djlx.equals("400")
							|| (djlx.equals("800") && ql.get("qllx").equals(
									"98"))
							|| (djlx.equals("500") && ql.get("qllx").equals(
									"23"))) {
						String lyqlid = rights.getLYQLID();
						BDCS_QL_LS dyql = baseCommonDao.get(BDCS_QL_LS.class,
								lyqlid);
						oldxmbh = dyql.getXMBH();
						dy.put("oldxmbh", oldxmbh);
					}
					List<Map> lstqlr = new ArrayList<Map>();
					List<RightsHolder> lstrh = RightsHolderTools
							.loadRightsHolders(DJDYLY.GZ, rights.getId());
					{
						if (lstrh.size() == 0) {
							List<BDCS_SQR> listsqr = baseCommonDao.getDataList(
									BDCS_SQR.class, "XMBH='" + xmbh
											+ "' AND SQRLB='1'");
							if (listsqr.size() > 0) {
								for (BDCS_SQR holder : listsqr) {
									Map qlr = new HashMap();
									qlr.put("qlrmc", holder.getSQRXM());
									qlr.put("zjlx", holder.getZJLX());
									qlr.put("zjh", holder.getZJH());
									qlr.put("bdcqzh", rights.getBDCQZH());
									List<BDCS_QDZR_GZ> qdzrs = baseCommonDao
											.getDataList(BDCS_QDZR_GZ.class,
													"QLID ='" + rights.getId()
															+ "' AND XMBH  ='"
															+ xmbh + "'");
									if (!StringHelper.isEmpty(qdzrs)) {
										if (qdzrs.size() > 0) {
											BDCS_ZS_GZ zs = baseCommonDao.get(
													BDCS_ZS_GZ.class, qdzrs
															.get(0).getZSID());
											if (!StringHelper.isEmpty(zs)) {
												qlr.put("bdcqzh",
														zs.getBDCQZH());
											}
										}
									}

									lstqlr.add(qlr);
								}
							}
						} else {
							for (RightsHolder holder : lstrh) {
								Map qlr = new HashMap();
								qlr.put("qlrmc", holder.getQLRMC());
								qlr.put("zjlx", holder.getZJZL());
								qlr.put("zjh", holder.getZJH());
								qlr.put("bdcqzh", holder.getBDCQZH());
								List<BDCS_QDZR_GZ> qdzrs = baseCommonDao
										.getDataList(BDCS_QDZR_GZ.class,
												"QLID ='" + rights.getId()
														+ "' AND QLRID  ='"
														+ holder.getId() + "'");
								if (!StringHelper.isEmpty(qdzrs)) {
									if (qdzrs.size() > 0) {
										BDCS_ZS_GZ zs = baseCommonDao.get(
												BDCS_ZS_GZ.class, qdzrs.get(0)
														.getZSID());
										if (!StringHelper.isEmpty(zs)) {
											qlr.put("bdcqzh", zs.getBDCQZH());
										}
									}
								}

								lstqlr.add(qlr);
							}
						}

					}
					ql.put("qlr", lstqlr);
				} else {
					List<Map> lstqlr = new ArrayList<Map>();
					List<BDCS_DYXZ> listsqr = baseCommonDao.getDataList(
							BDCS_DYXZ.class, "XMBH='" + xmbh + "'");
					if (listsqr.size() > 0) {
						for (BDCS_DYXZ holder : listsqr) {
							Map qlr = new HashMap();
							qlr.put("qlrmc", holder.getBXZRMC());
							qlr.put("zjlx", holder.getBXZRZJZL());
							qlr.put("zjh", holder.getBXZRZJHM());
							qlr.put("bdcqzh", holder.getBDCQZH());
							lstqlr.add(qlr);
						}

					}
					ql.put("qlr", lstqlr);
				}
				dy.put("ql", ql);
				lstdjdy.add(dy);
			}
			map.put("dys", lstdjdy);
		}

		return map;
	}

	/**
	 * TODO:@liushufeng:请描述这个方法的作用
	 * 
	 * @Title: setDJSFTS
	 * @author:liushufeng
	 * @date：2015年12月3日 上午5:52:43
	 * @param djsfid
	 * @param ts
	 * @return
	 */
	@Override
	public ResultMessage setDJSFTS(String djsfid, int ts, Map<String, Object> param) {
		ResultMessage msg = new ResultMessage();
		BDCS_DJSF sf = baseCommonDao.get(BDCS_DJSF.class, djsfid);
		if (sf != null) {
			sf.setQLRMC(StringHelper.formatObject(param.get("qlrmc")));
			sf.setTS(ts);
			baseCommonDao.update(sf);
			baseCommonDao.flush();
			msg.setSuccess("true");
		} else {
			msg.setSuccess("false");
		}
		return msg;
	}

	/**
	 * @作者 buxiaobo
	 * @创建时间 2015年12月5日 22:13:31 根据project_id返回缴款书相应信息
	 * @param project_id
	 *            进入的原数据
	 * @return JKSInfo缴款书全部信息
	 * @throws SQLException
	 */
	// 执收单位编码

	@Override
	public JKSInfo GetJKSInfo(String project_id, String type) {

		String zsdwbm = StringHelper.isEmpty(ConfigHelper
				.getNameByValue("JF_ZSDWBM")) ? "" : ConfigHelper
				.getNameByValue("JF_ZSDWBM");
		// 执收单位名称
		String zsdwmc = StringHelper.isEmpty(ConfigHelper
				.getNameByValue("JF_ZSDWMC")) ? "" : ConfigHelper
				.getNameByValue("JF_ZSDWMC");

		// 收款人全称
		String skrqc = StringHelper.isEmpty(ConfigHelper
				.getNameByValue("JF_SKRQC")) ? "" : ConfigHelper
				.getNameByValue("JF_SKRQC");
		// 收款人帐号
		String skrzh = StringHelper.isEmpty(ConfigHelper
				.getNameByValue("JF_SKRZH")) ? "" : ConfigHelper
				.getNameByValue("JF_SKRZH");
		// 收款人开户银行
		String skrkhyh = StringHelper.isEmpty(ConfigHelper
				.getNameByValue("JF_SKRKHYH")) ? "" : ConfigHelper
				.getNameByValue("JF_SKRKHYH");

		// 收入项目编码
		String srxmbm = StringHelper.isEmpty(ConfigHelper
				.getNameByValue("JF_SRXMBM")) ? "" : ConfigHelper
				.getNameByValue("JF_SRXMBM");
		// 收入项目名称
		String srxmmc = StringHelper.isEmpty(ConfigHelper
				.getNameByValue("JF_SRXMMC")) ? "" : ConfigHelper
				.getNameByValue("JF_SRXMMC");

		JKSInfo jksinfo = new JKSInfo();
		String dtbm = "";
		String xmbh = "";
		List<BDCS_XMXX> xmxxs = baseCommonDao.getDataList(BDCS_XMXX.class,
				"project_id='" + project_id + "'");
		if (xmxxs != null && xmxxs.size() > 0) {
			dtbm = xmxxs.get(0).getYWLSH();
			xmbh = xmxxs.get(0).getId();
		}
		// 付款人名称BDCS_QLR
		String fkrmc = "";
		List<BDCS_QLR_GZ> fkrmcs = baseCommonDao.getDataList(BDCS_QLR_GZ.class,
				"xmbh='" + xmbh + "'");
		if (fkrmcs != null && fkrmcs.size() > 0) {
			List<String> fkrList = new ArrayList<String>();
			for (int i = 0; i < fkrmcs.size(); i++) {
				if (!fkrList.contains(fkrmcs.get(i).getQLRMC())) {
					fkrList.add(fkrmcs.get(i).getQLRMC());
					fkrmc = StringHelper.isEmpty(fkrmc) ? fkrmcs.get(i)
							.getQLRMC() : fkrmc + ","
							+ fkrmcs.get(i).getQLRMC();
				}
			}

		}
		// 总金额字符串、double、大写
		String zjestr = "";
		double zjedouble = 0;
		String zjedx = "";
		List<SRXM> srxms = new ArrayList<SRXM>();

		// 收入项目集&&总金额计算&&总金额大写【金额前加￥】BDCS_DJSF
		List<BDCS_DJSF> djsfs = baseCommonDao.getDataList(BDCS_DJSF.class,
				"xmbh='" + xmbh + "'");
		if (type != null && type.equals("2")) {// 独立收费
			if (djsfs != null && djsfs.size() > 0) {
				for (BDCS_DJSF djsf : djsfs) {
					SRXM srxm = new SRXM();
					zjedouble = djsf.getYSJE() == null ? 0 : djsf.getYSJE();
					DecimalFormat df = new DecimalFormat("######0.0");
					df.setRoundingMode(RoundingMode.HALF_UP);
					zjestr = df.format(zjedouble);
					srxm.setBM(srxmbm);
					srxm.setJE(zjestr);
					srxm.setDM(djsf.getBZ());
					srxm.setSRXMMC(djsf.getSFKMMC());
					srxm.setSFJS(StringHelper.isEmpty(djsf.getSFJS()) ? "":djsf.getSFJS().toString());
					srxm.setTS(StringHelper.isEmpty(djsf.getTS()) ? "":djsf.getTS().toString());
					srxm.setXSGS(StringHelper.isEmpty(djsf.getXSGS()) ? "" : djsf.getXSGS().toString());
					srxm.setJSGS(djsf.getJSGS());
					srxms.add(srxm);
				}
				jksinfo.setDM(StringHelper.isEmpty(djsfs.get(0).getBZ()) ? "":djsfs.get(0).getBZ());

			}
		} else {
			SRXM srxm = new SRXM();
			if (djsfs != null && djsfs.size() > 0) {
				for (BDCS_DJSF djsf : djsfs) {
					// 计算总额转成“￥1234”格式
					BDCS_SFDY dy = baseCommonDao.get(BDCS_SFDY.class, djsf.getSFDYID());
					if (dy != null) {
						if (!"0".equals(dy.getTJBZ())) {
							zjedouble += djsf.getYSJE() == null ? 0 : djsf.getYSJE();
						}
					}
					DecimalFormat df = new DecimalFormat("######0.0");
					df.setRoundingMode(RoundingMode.HALF_UP);
					zjestr = "￥" + df.format(zjedouble);

				}
				srxm.setBM(srxmbm);
				srxm.setSRXMMC(srxmmc);
				srxm.setJE(zjestr);
				srxm.setDM(djsfs.get(0).getBZ());
				
				srxm.setSFJS(StringHelper.isEmpty(djsfs.get(0).getSFJS()) ? "":djsfs.get(0).getSFJS().toString());
				srxm.setTS(StringHelper.isEmpty(djsfs.get(0).getTS()) ? "":djsfs.get(0).getTS().toString());
				srxm.setXSGS(StringHelper.isEmpty(djsfs.get(0).getXSGS()) ? "" : djsfs.get(0).getXSGS().toString());
				srxm.setJSGS(djsfs.get(0).getJSGS());
				srxms.add(srxm);
				
				jksinfo.setDM(StringHelper.isEmpty(djsfs.get(0).getBZ()) ? "":djsfs.get(0).getBZ());
				// 金额大写
				ConvertMoneyToUppercase jedx = new ConvertMoneyToUppercase();
				zjedx = jedx.convertMoney(zjedouble);
			}
		}

		List<BDCS_DJDY_GZ> djdys = baseCommonDao.getDataList(
				BDCS_DJDY_GZ.class, "xmbh='" + xmbh + "'");
		if (djdys != null && djdys.size() > 0) {
			BDCS_DJDY_GZ djdy = djdys.get(0);
			RealUnit unit = UnitTools.loadUnit(
					BDCDYLX.initFrom(djdy.getBDCDYLX()),
					DJDYLY.initFrom(djdy.getLY()), djdy.getBDCDYID());
			if (unit != null) {
				jksinfo.setZL(unit.getZL());
				jksinfo.setMJ(unit.getMJ());
			}

		}
		// 获取当前年月日
		Calendar a = Calendar.getInstance();
		int year = a.get(Calendar.YEAR);// 得到年
		int month = a.get(Calendar.MONTH) + 1;// 由于月份是从0开始的所以加1
		int day = a.get(Calendar.DATE);// 得到日

		jksinfo.setDTBM(dtbm);
		jksinfo.setDWBM(zsdwbm);
		jksinfo.setDWMC(zsdwmc);
		jksinfo.setCURYEAR(Integer.toString(year));
		jksinfo.setCURMONTH(Integer.toString(month));
		jksinfo.setCURDAY(Integer.toString(day));
		jksinfo.setFKRMC(fkrmc);
		jksinfo.setSKRMC(skrqc);
		jksinfo.setSKRZH(skrzh);
		jksinfo.setSKRYH(skrkhyh);
		jksinfo.setSRXMS(srxms);
		jksinfo.setZJEDX(zjedx);
		jksinfo.setZJE(zjestr);
		return jksinfo;
	}

	/**
	 * @作者 buxiaobo
	 * @创建时间 2015年12月6日 00:48:43 将金额小数转换成中文大写金额
	 * @param money
	 * @return String result
	 */
	public class ConvertMoneyToUppercase {

		private String UNIT[] = { "万", "千", "佰", "拾", "亿", "千", "佰", "拾", "万",
				"千", "佰", "拾", "元", "角", "分" };

		private String NUM[] = { "零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌",
				"玖" };

		private static final double MAX_VALUE = 9999999999999.99D;

		public String convertMoney(double money) {
			if (money < 0 || money > MAX_VALUE)
				return "参数非法!";
			long money1 = Math.round(money * 100); // 四舍五入到分
			if (money1 == 0)
				return "零元整";
			String strMoney = String.valueOf(money1);
			int numIndex = 0; // numIndex用于选择金额数值
			int unitIndex = UNIT.length - strMoney.length(); // unitIndex用于选择金额单位
			boolean isZero = false; // 用于判断当前为是否为零
			String result = "";
			for (; numIndex < strMoney.length(); numIndex++, unitIndex++) {
				char num = strMoney.charAt(numIndex);
				if (num == '0') {
					isZero = true;
					if (UNIT[unitIndex] == "亿" || UNIT[unitIndex] == "万"
							|| UNIT[unitIndex] == "元") { // 如果当前位是亿、万、元，且数值为零
						result = result + UNIT[unitIndex]; // 补单位亿、万、元
						isZero = false;
					}
				} else {
					if (isZero) {
						result = result + "零";
						isZero = false;
					}
					result = result
							+ NUM[Integer.parseInt(String.valueOf(num))]
							+ UNIT[unitIndex];
				}
			}
			// 不是角分结尾就加"整"字
			if (!result.endsWith("角") && !result.endsWith("分")) {
				result = result + "整";
			}
			// 例如没有这行代码，数值"400000001101.2"，输出就是"肆千亿万壹千壹佰零壹元贰角"
			result = result.replaceAll("亿万", "亿");
			return result;
		}
	}

	/**
	 * 获取档案柜号
	 * 
	 * @author:yuxuebin
	 * @param ywlsh
	 * @return
	 */
	@Override
	public ResultMessage GetDAGH(String ywlsh) {
		ResultMessage ms = new ResultMessage();
		if (StringHelper.isEmpty(ywlsh)) {
			ms.setMsg("请输入受理编号！");
			ms.setSuccess("false");
			return ms;
		}
		List<BDCS_XMXX> xmxxs = baseCommonDao.getDataList(BDCS_XMXX.class,
				"YWLSH='" + ywlsh + "'");
		if (xmxxs != null && xmxxs.size() > 0) {
			if (StringHelper.isEmpty(xmxxs.get(0).getDAGH())) {
				ms.setMsg("");
			} else {
				ms.setMsg(StringHelper.formatDouble(xmxxs.get(0).getDAGH()));
			}
			ms.setSuccess("true");
		} else {
			ms.setMsg("未查询到受理项目，请检查受理编号是否正确！");
			ms.setSuccess("false");
		}
		return ms;
	}

	/**
	 * 保存档案柜号
	 * 
	 * @author:yuxuebin
	 * @param ywlsh
	 * @param dagh
	 * @return
	 */
	@Override
	public ResultMessage SaveDAGH(String ywlsh, Integer dagh) {
		ResultMessage ms = new ResultMessage();
		if (StringHelper.isEmpty(ywlsh)) {
			ms.setMsg("请输入受理编号！");
			ms.setSuccess("false");
			return ms;
		}
		if (StringHelper.isEmpty(dagh)) {
			ms.setMsg("请输入档案柜号！");
			ms.setSuccess("false");
			return ms;
		}
		List<BDCS_XMXX> xmxxs = baseCommonDao.getDataList(BDCS_XMXX.class,
				"YWLSH='" + ywlsh + "'");
		if (xmxxs != null && xmxxs.size() > 0) {
			BDCS_XMXX xmxx = xmxxs.get(0);
			xmxx.setDAGH(dagh);

			BDCS_XMXX xmxx2 = Global.getXMXXbyXMBH(xmxx.getId());
			if (xmxx2 != null)
				xmxx2.setDAGH(dagh);
			baseCommonDao.update(xmxx);
			baseCommonDao.flush();
			ms.setSuccess("true");
			ms.setMsg(StringHelper.formatDouble((dagh + 1)));
		} else {
			ms.setMsg("未查询到受理项目，请检查受理编号是否正确！");
			ms.setSuccess("false");
		}
		return ms;
	}

	@Override
	public ResultMessage finishProject(String file_number) {
		ResultMessage msg = new ResultMessage();
		msg.setSuccess("false");

		if (StringHelper.isEmpty(file_number)) {
			msg.setMsg("传入的file_number参数为空！");
			logger.error("工作流调用办结项目接口时出错，错误信息为:" + msg.getMsg());
			return msg;
		}
		if (file_number.equals("null") || file_number.equals("undefined")) {
			msg.setMsg("传入的file_number参数不能为" + file_number);
			logger.error("工作流调用办结项目接口时出错，错误信息为:" + msg.getMsg());
			return msg;
		}
		List<BDCS_XMXX> xmxxs = baseCommonDao.getDataList(BDCS_XMXX.class,
				"PROJECT_ID='" + file_number + "'");
		if (xmxxs != null && xmxxs.size() > 0) {
			BDCS_XMXX xmxx = xmxxs.get(0);
			if (xmxx != null) {
				xmxx.setSFBJ(SF.YES.Value);
				baseCommonDao.update(xmxx);
				baseCommonDao.flush();
				msg.setSuccess("true");
				msg.setMsg("修改项目办结状态成功!");
				BDCS_XMXX xmxx2 = Global.getXMXXbyXMBH(xmxx.getId());
				if (xmxx2 != null)
					xmxx2.setSFBJ(SF.YES.Value);
			}
		} else {
			msg.setMsg("未找到PROJECT_ID为" + file_number + "的项目信息记录！");
			logger.error("工作流调用办结项目接口时出错，错误信息为:" + msg.getMsg());
			return msg;
		}
		return msg;
	}

	@Override
	public HashMap<String, List<HashMap<String, Object>>> GetDBXX2(
			String project_id) {
		// TODO Auto-generated method stub
		return QueryTools.GetXMInfo(project_id);
	}

	/**
	 * 将申请人名称设置为项目名称
	 * 
	 * @param xmbh
	 * @return
	 */
	public void RenameXmmc(String xmbh) {
		String xmbhcond = ProjectHelper.GetXMBHCondition(xmbh);
		List<BDCS_XMXX> xmxxList = baseCommonDao.getDataList(BDCS_XMXX.class,
				xmbhcond);
		if (xmxxList != null && xmxxList.size() > 0) {
			List<BDCS_SQR> sqlList = baseCommonDao.getDataList(BDCS_SQR.class,
					xmbhcond + " and sqrlb = '" + SQRLB.JF.Value + "' ");
			if (sqlList != null && sqlList.size() > 0) {
				List<Wfi_ProInst> instList = baseCommonDao.getDataList(
						Wfi_ProInst.class, " FILE_NUMBER = '"
								+ xmxxList.get(0).getPROJECT_ID() + "' ");
				if (instList != null && instList.size() > 0) {
					xmxxList.get(0).setXMMC(sqlList.get(0).getSQRXM());
					instList.get(0).setProject_Name(sqlList.get(0).getSQRXM());
					baseCommonDao.update(xmxxList.get(0));
					baseCommonDao.update(instList.get(0));
					baseCommonDao.flush();
				}
			}
		}
	}

	/**
	 * 通过业务流水号获取当前的人员信息以及上一手的人员信息
	 */
	public Map getQLRInfo() {
		return null;
	}

	/**
	 * 通过project_id设定项目状态
	 * 
	 * @param project_id
	 * @param YXBZ
	 * @return
	 */
	public ResultMessage setXMXX_STATUS(String project_id, YXBZ yxbz) {
		ResultMessage msg = new ResultMessage();
		msg.setSuccess("true");
		BDCS_XMXX xmxx = Global.getXMXX(project_id);
		if (xmxx != null) {
			if (SFDB.YES.Value.equals(xmxx.getSFDB())) {
				msg.setSuccess("false");
				msg.setMsg("项目已登簿，不能退件！");
				return msg;
			}
			xmxx.setYXBZ(yxbz.Value);
			baseCommonDao.update(xmxx);
			baseCommonDao.flush();
			BDCS_XMXX xmxx_xmbh = Global.getXMXXbyXMBH(xmxx.getId());
			xmxx_xmbh.setYXBZ(yxbz.Value);
		}
		return msg;
	}

	/**
	 * 通过project_id判断项目是否可进行业务流程重载
	 * 
	 * @param project_id
	 * @return
	 */
	public ResultMessage getCanResetWorkflowCode(String project_id) {
		ResultMessage msg = new ResultMessage();
		msg.setSuccess("true");
		BDCS_XMXX xmxx = Global.getXMXX(project_id);
		if (xmxx != null) {
			List<BDCS_DJDY_GZ> list_djdy = baseCommonDao.getDataList(
					BDCS_DJDY_GZ.class, "XMBH='" + xmxx.getId() + "'");
			if (list_djdy != null && list_djdy.size() > 0) {
				msg.setSuccess("false");
				msg.setMsg("已经选择登记数据，不能重置流程！");
			}
		}
		return msg;
	}

	/**
	 * 通过原流程project_id和新流程project_id重置业务流程
	 * 
	 * @param project_id
	 * @return
	 */
	public boolean ResetWorkflowCode(String old_project_id,
			String new_project_id) {
		BDCS_XMXX xmxx = Global.getXMXX(old_project_id);
		if (xmxx == null) {
			return true;
		}
		RegisterWorkFlow flow = HandlerFactory.getWorkflow(new_project_id);
		// 登记类型
		String djlx = flow.getDjlx();
		// 权利类型
		String qllx = flow.getQllx();

		xmxx.setQLLX(qllx);
		xmxx.setDJLX(djlx);
		xmxx.setPROJECT_ID(new_project_id);
		xmxx.setSFHBZS(SF.NO.Value);
		// 刘树峰 2015.12.27 是否配置了默认多个单元一本证书
		// 刘树峰 2016.04.05 还得看单元类型是啥，如果是宗地，就不能多个单元一本证。
		if ((BDCDYLX.H.Value.equals(flow.getUnittype()) || BDCDYLX.YCH.Value
				.equals(flow.getUnittype())) && !DJLX.YYDJ.Value.equals(djlx)) {
			String DEFAULTDGDYYBZ = ConfigHelper
					.getNameByValue("DEFAULTDGDYYBZ");
			if (!StringHelper.isEmpty(DEFAULTDGDYYBZ)
					&& DEFAULTDGDYYBZ.equals("1")) {
				xmxx.setSFHBZS(SF.YES.Value);
			}
		}
		baseCommonDao.update(xmxx);
		baseCommonDao.flush();
		// 删除原收费信息
		List<BDCS_DJSF> list_djsf = baseCommonDao.getDataList(BDCS_DJSF.class,
				"XMBH='" + xmxx.getId() + "'");
		if (list_djsf != null && list_djsf.size() > 0) {
			for (BDCS_DJSF djsf : list_djsf) {
				baseCommonDao.deleteEntity(djsf);
			}
		}
		baseCommonDao.flush();
		// 拷贝收费信息
		ProjectHelper.copyChargeList(xmxx);
		baseCommonDao.flush();

		BDCS_XMXX xmxx_xmbh = Global.getXMXXbyXMBH(xmxx.getId());
		xmxx_xmbh.setQLLX(qllx);
		xmxx_xmbh.setDJLX(djlx);
		xmxx_xmbh.setSFHBZS(xmxx.getSFHBZS());
		xmxx_xmbh.setPROJECT_ID(new_project_id);
		return true;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public String getYwlshByCondtionsEx(String qlr, String ywr, String zjh,
			String qzh, String zl, boolean PreciseQueryQLR) {
		zjh=zjh.replace(" ","").toUpperCase();
		qlr=qlr.replace(" ", "");
		ywr=ywr.replace(" ", "");
		StringBuilder builder = new StringBuilder();
		builder.append("SELECT YWLSH FROM BDCK.BDCS_XMXX XMXX ");
		builder.append("WHERE ");
		if (!StringHelper.isEmpty(qlr)) {
			builder.append("EXISTS (SELECT 1 FROM BDCK.BDCS_SQR ");
			builder.append("WHERE XMBH=XMXX.XMBH ");
			builder.append("AND ");
			if (!StringHelper.isEmpty(qlr)) {
				if (PreciseQueryQLR) {
					if (!StringHelper.isEmpty(zjh)) {
						builder.append("(SQRLB='1' AND SQRXM='" + qlr
								+ "' AND ZJH='" + zjh + "') ");
					} else {
						builder.append("(SQRLB='1' AND SQRXM='" + qlr + "') ");
					}
				} else {
					if (!StringHelper.isEmpty(zjh)) {
						builder.append("(SQRLB='1' AND SQRXM LIKE '%" + qlr
								+ "%' AND ZJH='" + zjh + "') ");
					} else {
						builder.append("(SQRLB='1' AND SQRXM LIKE '%" + qlr
								+ "%') ");
					}
				}
			}
			builder.append(")");
		}
		if (!StringHelper.isEmpty(ywr)) {
			if (!StringHelper.isEmpty(qlr)) {
				builder.append("AND ");
			}
			builder.append("EXISTS (SELECT 1 FROM BDCK.BDCS_SQR ");
			builder.append("WHERE XMBH=XMXX.XMBH ");
			builder.append("AND ");
			if (!StringHelper.isEmpty(ywr)) {
				if (PreciseQueryQLR) {
					if (!StringHelper.isEmpty(zjh)) {
						builder.append("(SQRLB='2' AND SQRXM='" + ywr
								+ "' AND ZJH='" + zjh + "') ");
					} else {
						builder.append("(SQRLB='2' AND SQRXM='" + ywr + "') ");
					}
				} else {
					if (!StringHelper.isEmpty(zjh)) {
						builder.append("(SQRLB='2' AND SQRXM LIKE '%" + ywr
								+ "%' AND ZJH='" + zjh + "') ");
					} else {
						builder.append("(SQRLB='2' AND SQRXM LIKE '%" + ywr
								+ "%') ");
					}
				}
			}
			builder.append(")");
		}
		if (StringHelper.isEmpty(qlr) && StringHelper.isEmpty(ywr)
				&& !StringHelper.isEmpty(zjh.replace(" ", ""))) {
			builder.append("EXISTS (SELECT 1 FROM BDCK.BDCS_SQR ");
			builder.append("WHERE XMBH=XMXX.XMBH ");
			builder.append("AND ");
			builder.append("(ZJH='" + zjh + "') ");
			builder.append(")");
		}
		if (!StringHelper.isEmpty(qzh)) {
			if (!StringHelper.isEmpty(qlr) || !StringHelper.isEmpty(ywr)
					|| !StringHelper.isEmpty(zjh)) {
				builder.append("AND ");
			}
			builder.append("EXISTS (SELECT 1 FROM BDCK.BDCS_QLR_GZ WHERE XMBH=XMXX.XMBH AND BDCQZH LIKE '%"
					+ qzh + "%') ");
		}
		if (!StringHelper.isEmpty(zl)) {
			if (!StringHelper.isEmpty(qlr) || !StringHelper.isEmpty(ywr)
					|| !StringHelper.isEmpty(zjh) || !StringHelper.isEmpty(qzh)) {
				builder.append("AND ");
			}
			builder.append("EXISTS (SELECT 1 FROM ( ");
			builder.append("SELECT DY.ZL ZL,DJDY.XMBH FROM BDCK.BDCS_DJDY_GZ DJDY LEFT JOIN BDCK.BDCS_H_GZ DY ON DY.BDCDYID=DJDY.BDCDYID ");
			builder.append("WHERE DJDY.BDCDYLX='031' AND DJDY.LY='01' ");
			builder.append("UNION ");
			builder.append("SELECT DY.ZL ZL,DJDY.XMBH FROM BDCK.BDCS_DJDY_GZ DJDY LEFT JOIN BDCK.BDCS_H_LS DY ON DY.BDCDYID=DJDY.BDCDYID ");
			builder.append("WHERE DJDY.BDCDYLX='031' AND DJDY.LY IN ('02','03') ");
			builder.append("UNION ");
			builder.append("SELECT DY.ZL ZL,DJDY.XMBH FROM BDCK.BDCS_DJDY_GZ DJDY LEFT JOIN BDCK.BDCS_H_GZY DY ON DY.BDCDYID=DJDY.BDCDYID ");
			builder.append("WHERE DJDY.BDCDYLX='032' AND DJDY.LY='01' ");
			builder.append("UNION ");
			builder.append("SELECT DY.ZL ZL,DJDY.XMBH FROM BDCK.BDCS_DJDY_GZ DJDY LEFT JOIN BDCK.BDCS_H_LSY DY ON DY.BDCDYID=DJDY.BDCDYID ");
			builder.append("WHERE DJDY.BDCDYLX='032' AND DJDY.LY IN ('02','03') ");
			builder.append("UNION ");
			builder.append("SELECT DY.ZL ZL,DJDY.XMBH FROM BDCK.BDCS_DJDY_GZ DJDY LEFT JOIN BDCK.BDCS_SHYQZD_GZ DY ON DY.BDCDYID=DJDY.BDCDYID ");
			builder.append("WHERE DJDY.BDCDYLX='02' AND DJDY.LY='01' ");
			builder.append("UNION ");
			builder.append("SELECT DY.ZL ZL,DJDY.XMBH FROM BDCK.BDCS_DJDY_GZ DJDY LEFT JOIN BDCK.BDCS_SHYQZD_LS DY ON DY.BDCDYID=DJDY.BDCDYID ");
			builder.append("WHERE DJDY.BDCDYLX='02' AND DJDY.LY IN ('02','03') ");
			builder.append("UNION ");
			builder.append("SELECT DY.ZL ZL,DJDY.XMBH FROM BDCK.BDCS_DJDY_GZ DJDY LEFT JOIN BDCK.BDCS_SYQZD_GZ DY ON DY.BDCDYID=DJDY.BDCDYID ");
			builder.append("WHERE DJDY.BDCDYLX='01' AND DJDY.LY='01' ");
			builder.append("UNION ");
			builder.append("SELECT DY.ZL ZL,DJDY.XMBH FROM BDCK.BDCS_DJDY_GZ DJDY LEFT JOIN BDCK.BDCS_SYQZD_LS DY ON DY.BDCDYID=DJDY.BDCDYID ");
			builder.append("WHERE DJDY.BDCDYLX='01' AND DJDY.LY IN ('02','03') ");
			builder.append("UNION ");
			builder.append("SELECT DY.ZL ZL,DJDY.XMBH FROM BDCK.BDCS_DJDY_GZ DJDY LEFT JOIN BDCK.BDCS_ZH_GZ DY ON DY.BDCDYID=DJDY.BDCDYID ");
			builder.append("WHERE DJDY.BDCDYLX='04' AND DJDY.LY='01' ");
			builder.append("UNION ");
			builder.append("SELECT DY.ZL ZL,DJDY.XMBH FROM BDCK.BDCS_DJDY_GZ DJDY LEFT JOIN BDCK.BDCS_ZH_LS DY ON DY.BDCDYID=DJDY.BDCDYID ");
			builder.append("WHERE DJDY.BDCDYLX='04' AND DJDY.LY IN ('02','03') ");
			builder.append("UNION ");
			builder.append("SELECT DY.ZL ZL,DJDY.XMBH FROM BDCK.BDCS_DJDY_GZ DJDY LEFT JOIN BDCK.BDCS_SLLM_GZ DY ON DY.BDCDYID=DJDY.BDCDYID ");
			builder.append("WHERE DJDY.BDCDYLX='05' AND DJDY.LY='01' ");
			builder.append("UNION ");
			builder.append("SELECT DY.ZL ZL,DJDY.XMBH FROM BDCK.BDCS_DJDY_GZ DJDY LEFT JOIN BDCK.BDCS_SLLM_LS DY ON DY.BDCDYID=DJDY.BDCDYID ");
			builder.append("WHERE DJDY.BDCDYLX='05' AND DJDY.LY IN ('02','03') ");
			builder.append("UNION ");
			builder.append("SELECT DY.ZL ZL,DJDY.XMBH FROM BDCK.BDCS_DJDY_GZ DJDY LEFT JOIN BDCK.BDCS_NYD_GZ DY ON DY.BDCDYID=DJDY.BDCDYID ");
			builder.append("WHERE DJDY.BDCDYLX='09' AND DJDY.LY='01' ");
			builder.append("UNION ");
			builder.append("SELECT DY.ZL ZL,DJDY.XMBH FROM BDCK.BDCS_DJDY_GZ DJDY LEFT JOIN BDCK.BDCS_NYD_LS DY ON DY.BDCDYID=DJDY.BDCDYID ");
			builder.append("WHERE DJDY.BDCDYLX='09' AND DJDY.LY IN ('02','03') ");
			builder.append(") DYINFO ");
			builder.append("WHERE DYINFO.XMBH=XMXX.XMBH AND DYINFO.ZL LIKE '%"
					+ zl + "%')");
		}
		List<Map> list = baseCommonDao.getDataListByFullSql(builder.toString());
		List<String> list_ywlsh = new ArrayList<String>();
		if (list != null && list.size() > 0) {
			for (Map m : list) {
				String ywlsh = StringHelper.formatObject(m.get("YWLSH"));
				if (!StringHelper.isEmpty(ywlsh) && !list_ywlsh.contains(ywlsh)) {
					list_ywlsh.add(ywlsh);
				}
			}
		}
		return StringHelper.formatList(list_ywlsh, ",");
	}

	public List<HashMap<String, String>> getCRHTInfo(String project_id) {
		List<HashMap<String, String>> result = new ArrayList<HashMap<String, String>>();
		ProjectInfo info = ProjectHelper.GetPrjInfoByPrjID(project_id);
		if (info != null) {
			List<BDCS_DJDY_GZ> djdys = baseCommonDao.getDataList(
					BDCS_DJDY_GZ.class, "XMBH='" + info.getXmbh() + "'");
			List<BDCS_QL_GZ> qls = baseCommonDao.getDataList(BDCS_QL_GZ.class,
					"XMBH='" + info.getXmbh() + "'");
			if (djdys != null && djdys.size() > 0) {
				for (BDCS_DJDY_GZ djdy : djdys) {
					HashMap<String, String> m_info = new HashMap<String, String>();
					RealUnit unit = UnitTools.loadUnit(
							BDCDYLX.initFrom(djdy.getBDCDYLX()),
							DJDYLY.initFrom(djdy.getLY()), djdy.getBDCDYID());
					if (BDCDYLX.SHYQZD.Value.equals(djdy.getBDCDYLX())) {
						UseLand zd = (UseLand) unit;
						m_info.put("zdzl", zd.getZL());
						m_info.put("qllx", ConstHelper.getNameByValue("QLLX",
								zd.getQLLX()));
						m_info.put("qlxz", ConstHelper.getNameByValue("QLXZ",
								zd.getQLXZ()));
						m_info.put("zdszd", zd.getZDSZD());
						m_info.put("zdszx", zd.getZDSZX());
						m_info.put("zdszn", zd.getZDSZN());
						m_info.put("zdszb", zd.getZDSZB());
						m_info.put("zdmj",
								StringHelper.formatDouble(zd.getZDMJ()));
						m_info.put("djh", zd.getDJH());
						m_info.put("zddm", zd.getZDDM());
						m_info.put("crmj",
								StringHelper.formatDouble(zd.getZDMJ()));
						m_info.put("crtddj","");
						if(zd.getTDYTS()!=null&&zd.getTDYTS().size()>0){
							for(TDYT tdyt:zd.getTDYTS()){
								if(!StringHelper.isEmpty(tdyt.getTDDJName())){
									m_info.put("crtddj",tdyt.getTDDJName());
									break;
								}
							}
							m_info.put("crtddj",zd.getTDYTS().get(0).getTDDJName());
						}
						m_info.put("tdyt","城镇住宅用地");
					} else if (BDCDYLX.H.Value.equals(info.getBdcdylx())) {
						House h = (House) unit;
						m_info.put("crtddj","");
						if (!StringHelper.isEmpty(h.getZDBDCDYID())) {
							BDCS_SHYQZD_LS zd = baseCommonDao.get(
									BDCS_SHYQZD_LS.class, h.getZDBDCDYID());
							if (zd != null) {
								m_info.put("zdzl", zd.getZL());
								m_info.put("qllx", zd.getQLLXName());
								m_info.put("qlxz", zd.getQLXZName());
								m_info.put("zdszd", zd.getZDSZD());
								m_info.put("zdszx", zd.getZDSZX());
								m_info.put("zdszn", zd.getZDSZN());
								m_info.put("zdszb", zd.getZDSZB());
								m_info.put("zdmj",
										StringHelper.formatDouble(zd.getZDMJ()));
								m_info.put("djh", zd.getDJH());
								m_info.put("zddm", zd.getZDDM());
								List<BDCS_TDYT_XZ> tdyts=baseCommonDao.getDataList(BDCS_TDYT_XZ.class, "BDCDYID='"+zd.getId()+"'");
								if(tdyts!=null&&tdyts.size()>0){
									for(TDYT tdyt:tdyts){
										if(!StringHelper.isEmpty(tdyt.getTDDJName())){
											m_info.put("crtddj",tdyt.getTDDJName());
											break;
										}
									}
								}
							}
						}
						m_info.put("dytdmj",
								StringHelper.formatDouble(h.getDYTDMJ()));
						m_info.put("gytdmj",
								StringHelper.formatDouble(h.getGYTDMJ()));
						m_info.put("fttdmj",
								StringHelper.formatDouble(h.getFTTDMJ()));
						m_info.put("jzmj",
								StringHelper.formatDouble(h.getSCJZMJ()));
						m_info.put("qlxz",
								ConstHelper.getNameByValue("QLXZ", h.getQLXZ()));
						m_info.put("zrzh", h.getZRZH());
						m_info.put("dyh", h.getDYH());
						m_info.put("fh", h.getFH());
						m_info.put("crmj",StringHelper.formatDouble(h.getFTTDMJ()));
						m_info.put("tdyt","城镇住宅用地");
					}

					for (BDCS_QL_GZ ql : qls) {
						if (djdy.getDJDYID().equals(ql.getDJDYID())) {
							String lyqlid = ql.getLYQLID();
							String crqx="";
							if(!StringHelper.isEmpty(ql.getQLQSSJ())){
								crqx=crqx+StringHelper.FormatDateOnType(ql.getQLQSSJ(), "yyyy年MM月dd日");
							}else{
								crqx=crqx+"----";
							}
							crqx=crqx+"至";
							if(!StringHelper.isEmpty(ql.getQLJSSJ())){
								crqx=crqx+StringHelper.FormatDateOnType(ql.getQLJSSJ(), "yyyy年MM月dd日");
							}else{
								crqx=crqx+"----";
							}
							m_info.put("qzh", ql.getBDCQZH());
							m_info.put("crqx", crqx);
							List<BDCS_QLR_GZ> list_qlr_1 = baseCommonDao.getDataList(BDCS_QLR_GZ.class, "QLID='"+ql.getId()+"'");
							if (list_qlr_1 != null && list_qlr_1.size() > 0) {
								List<String> list_qlrmc_1 = new ArrayList<String>();
								List<String> list_frdb_1 = new ArrayList<String>();
								List<String> list_qlrdz_1 = new ArrayList<String>();
								for (BDCS_QLR_GZ qlr_1 : list_qlr_1) {
									if (!list_qlrmc_1
											.contains(qlr_1.getQLRMC())) {
										list_qlrmc_1.add(qlr_1.getQLRMC());
									}
									if (!StringHelper.isEmpty(qlr_1.getFDDBR())
											&& !list_frdb_1.contains(qlr_1
													.getFDDBR())) {
										list_frdb_1.add(qlr_1.getFDDBR());
									}
									if (!StringHelper.isEmpty(qlr_1.getDZ())
											&& !list_qlrdz_1.contains(qlr_1
													.getDZ())) {
										list_qlrdz_1.add(qlr_1.getDZ());
									}
								}
								m_info.put("srf", StringHelper.formatList(
										list_qlrmc_1, ","));
								m_info.put("srffrdb", StringHelper.formatList(
										list_frdb_1, ","));
								m_info.put("srfdz", StringHelper.formatList(
										list_qlrdz_1, ","));
							}
							if (!StringHelper.isEmpty(lyqlid)) {
								BDCS_QL_LS ql_ls = baseCommonDao.get(
										BDCS_QL_LS.class, lyqlid);
								if (ql_ls != null) {
									m_info.put("yqzh", ql_ls.getBDCQZH());
									List<BDCS_QLR_LS> list_qlr = baseCommonDao.getDataList(BDCS_QLR_LS.class, "QLID='"+ql_ls.getId()+"'");
									if (list_qlr != null && list_qlr.size() > 0) {
										List<String> list_qlrmc = new ArrayList<String>();
										List<String> list_frdb = new ArrayList<String>();
										List<String> list_qlrdz = new ArrayList<String>();
										for (BDCS_QLR_LS qlr : list_qlr) {
											if (!list_qlrmc.contains(qlr
													.getQLRMC())) {
												list_qlrmc.add(qlr.getQLRMC());
											}
											if (!StringHelper.isEmpty(qlr
													.getFDDBR())
													&& !list_frdb.contains(qlr
															.getFDDBR())) {
												list_frdb.add(qlr.getFDDBR());
											}
											if (!StringHelper.isEmpty(qlr.getDZ())
													&& !list_qlrdz.contains(qlr
															.getDZ())) {
												list_qlrdz.add(qlr.getDZ());
											}
										}
										m_info.put("crf", StringHelper
												.formatList(list_qlrmc, ","));
										m_info.put("crffrdb", StringHelper
												.formatList(list_frdb, ","));
										m_info.put("crfdz", StringHelper.formatList(
												list_qlrdz, ","));
									}
								}
							}
							break;
						}
					}
					m_info.put("crjbz", "");
					m_info.put("rmb", "");
					List<BDCS_DJSF> list_djsf=baseCommonDao.getDataList(BDCS_DJSF.class, " SFKMMC LIKE '%出让金%' AND YWH='"+project_id+"'");
					if(list_djsf!=null&&list_djsf.size()>0){
						String crje=list_djsf.get(0).getSSJE();
						if(!StringHelper.isEmpty(crje)){
							Double d=StringHelper.getDouble(crje);
							m_info.put("rmb", StringHelper.number2CNMontrayUnit(new BigDecimal(d)));
							m_info.put("crjbz", crje);
						}
					}
					m_info.put("slbh", info.getYwlsh());
					result.add(m_info);
				}
			}
		}
		return result;
	}

	public List<HashMap<String, String>> getCRHT(String filenumber) {
		List<HashMap<String, String>> result = new ArrayList<HashMap<String, String>>();
		String sql = "filenumber = '" + filenumber + "'";
		List<BDCS_CRHT> crht = baseCommonDao.getDataList(BDCS_CRHT.class, sql);
		HashMap<String, String> m_info = new HashMap<String, String>();
		if (crht != null && crht.size() > 0) {
			BDCS_CRHT _crht=crht.get(0);
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
			m_info.put("jzmj",
					StringHelper.formatDouble(_crht.getJzmj()));
			m_info.put("crmj",
					StringHelper.formatDouble( _crht.getCrmj()));
			m_info.put("crtddj", _crht.getCrtddj());
			m_info.put("crqx", _crht.getCrqx());
			m_info.put("crjbz", _crht.getCrjbz());
			m_info.put("tdyt", "城镇住宅用地");
			m_info.put("crjze", _crht.getCrjze());
			m_info.put("rmb", _crht.getRmb());
			m_info.put("bz", _crht.getBz());
			m_info.put("slbh", _crht.getSlbh());
			result.add(m_info);
		}

		return result;
	}

	
	public BDCS_CRHT updateCRHT(HashMap<String, String> bdcsCrht,
			String projectId) {
		BDCS_CRHT crht = new BDCS_CRHT();
		if (bdcsCrht != null) {
			crht.setDjh(bdcsCrht.get("djh"));
			crht.setDyh(bdcsCrht.get("dyh"));
			if (!bdcsCrht.get("dytdmj").equals("") && bdcsCrht.get("dytdmj") != null) {
				crht.setDytdmj(StringHelper.getDouble(bdcsCrht.get("dytdmj")));
			}
			crht.setFh(bdcsCrht.get("fh"));
			crht.setFilenumber(bdcsCrht.get("filenumber"));
			
			if (!bdcsCrht.get("gytdmj").equals("") && bdcsCrht.get("gytdmj") != null) {
				crht.setGytdmj(StringHelper.getDouble(bdcsCrht.get("gytdmj")));
			}
			if (!bdcsCrht.get("jzmj").equals("") && bdcsCrht.get("jzmj") != null) {
				crht.setJzmj(StringHelper.getDouble(bdcsCrht.get("jzmj")));
			}
			if (!bdcsCrht.get("fttdmj").equals("") && bdcsCrht.get("fttdmj") != null) {
				crht.setFttdmj(StringHelper.getDouble(bdcsCrht.get("fttdmj")));
			}
			crht.setQllx(bdcsCrht.get("qllx"));

			crht.setQlxz(bdcsCrht.get("qlxz"));
			crht.setZddm(bdcsCrht.get("zddm"));
			if (!bdcsCrht.get("zdmj").equals("") && bdcsCrht.get("zdmj") != null) {
				crht.setZdmj(StringHelper.getDouble(bdcsCrht.get("zdmj")));
			}
			crht.setQzh(bdcsCrht.get("qzh"));
			crht.setZdszd(bdcsCrht.get("zdszd"));
			crht.setZdszb(bdcsCrht.get("zdszb"));
			crht.setZdszn(bdcsCrht.get("zdszn"));
			crht.setZdszx(bdcsCrht.get("zdszx"));
			crht.setZdzl(bdcsCrht.get("zdzl"));
			crht.setZrzh(bdcsCrht.get("zrzh"));
			crht.setFilenumber(projectId);
			crht.setCrf(bdcsCrht.get("crf"));
			crht.setCrffrdb(bdcsCrht.get("crffrdb"));
			crht.setSrf(bdcsCrht.get("srf"));
			crht.setSrffrdb(bdcsCrht.get("srffrdb"));
			
			crht.setCrfdz(bdcsCrht.get("crfdz"));
			crht.setSrfdz(bdcsCrht.get("srfdz"));
			crht.setTh(bdcsCrht.get("th"));
			crht.setYqzh(bdcsCrht.get("yqzh"));
			if (!bdcsCrht.get("crmj").equals("") && bdcsCrht.get("crmj") != null) {
				crht.setCrmj(StringHelper.getDouble(bdcsCrht.get("crmj")));
			}
			crht.setCrtddj(bdcsCrht.get("crtddj"));
			crht.setCrqx(bdcsCrht.get("crqx"));
			crht.setCrjbz(bdcsCrht.get("crjbz"));
			crht.setTdyt(bdcsCrht.get("tdyt"));
			crht.setCrjze(bdcsCrht.get("crjbz"));
			crht.setRmb(bdcsCrht.get("rmb"));
			crht.setBz(bdcsCrht.get("bz"));
			crht.setSlbh(bdcsCrht.get("slbh"));
			
		}
		String hql = " FILENUMBER = '"+projectId+"'";
		List<BDCS_CRHT> oldChrt = baseCommonDao.getDataList(BDCS_CRHT.class, hql);		
		if(oldChrt!=null&&oldChrt.size()>0){
			crht.setID(oldChrt.get(0).getID());
			baseCommonDao.update(crht);
		}else{
			crht.setID(Common.CreatUUID());
			baseCommonDao.save(crht);
		}
		return crht;

	}
	public void delCRHT(String projectId){
		if(projectId!=null&&!projectId.equals("")){
			String sql = "filenumber = '" + projectId + "'";
			List<BDCS_CRHT> crht = baseCommonDao.getDataList(BDCS_CRHT.class, sql);				
			if(crht!=null&&crht.size()>0){
				baseCommonDao.delete(BDCS_CRHT.class, crht.get(0).getID());
			}
		}
	}
	@Override
	public String GetDBJS(String xmbh, String actinst_id) {
		String dbjs = "";
		User user = Global.getCurrentUserInfo();
		List<BDCS_DBJS> list_dbjs = baseCommonDao.getDataList(BDCS_DBJS.class,
				"XMBH='" + xmbh + "' AND USER_ID='" + user.getId() + "'");
		if (list_dbjs != null && list_dbjs.size() > 0) {
			dbjs = StringHelper.formatObject(list_dbjs.get(0).getJS());
		}
		return dbjs;
	}

	@Override
	public ResultMessage UpdateDBJS(String xmbh, String actinst_id, String dbjs) {
		ResultMessage msg = new ResultMessage();
		msg.setMsg("保存失败！");
		msg.setSuccess("false");
		User user = Global.getCurrentUserInfo();
		if (user == null) {
			msg.setMsg("请先登录！");
			return msg;
		}
		List<BDCS_DBJS> list_dbjs = baseCommonDao.getDataList(BDCS_DBJS.class,
				"XMBH='" + xmbh + "' AND USER_ID='" + user.getId() + "'");
		if (list_dbjs != null && list_dbjs.size() > 0) {
			BDCS_DBJS dbjsinfo = list_dbjs.get(0);
			dbjsinfo.setJS(dbjs);
			baseCommonDao.update(dbjsinfo);
		} else {
			BDCS_DBJS dbjsinfo = new BDCS_DBJS();
			dbjsinfo.setJS(dbjs);
			dbjsinfo.setACTINST_ID(actinst_id);
			dbjsinfo.setUSER_ID(user.getId());
			dbjsinfo.setXMBH(xmbh);
			baseCommonDao.save(dbjsinfo);
		}
		msg.setMsg("保存成功！");
		msg.setSuccess("true");
		return msg;
	}

	public List<HashMap<String, String>> getTDZRInfo(String project_id) {
		List<HashMap<String, String>> result = new ArrayList<HashMap<String, String>>();
		ProjectInfo info = ProjectHelper.GetPrjInfoByPrjID(project_id);
		if (info != null) {
			List<BDCS_DJDY_GZ> djdys = baseCommonDao.getDataList(
					BDCS_DJDY_GZ.class, "XMBH='" + info.getXmbh() + "'");
			List<BDCS_QL_GZ> qls = baseCommonDao.getDataList(BDCS_QL_GZ.class,
					"XMBH='" + info.getXmbh() + "'");
			if (djdys != null && djdys.size() > 0) {
				for (BDCS_DJDY_GZ djdy : djdys) {
					HashMap<String, String> m_info = new HashMap<String, String>();
					RealUnit unit = UnitTools.loadUnit(
							BDCDYLX.initFrom(djdy.getBDCDYLX()),
							DJDYLY.initFrom(djdy.getLY()), djdy.getBDCDYID());
					if (BDCDYLX.SHYQZD.Value.equals(djdy.getBDCDYLX())) {
						UseLand zd = (UseLand) unit;
						m_info.put("th", zd.getTFH());
						m_info.put("shyqlx", ConstHelper.getNameByValue("QLXZ",
								zd.getQLXZ()));
						m_info.put("zdszd", zd.getZDSZD());
						m_info.put("zdszx", zd.getZDSZX());
						m_info.put("zdszn", zd.getZDSZN());
						m_info.put("zdszb", zd.getZDSZB());
						m_info.put("zdmj",
								StringHelper.formatDouble(zd.getZDMJ()));
						m_info.put("jzmj",
								StringHelper.formatDouble(zd.getJZMJ()));
						m_info.put("zdh", zd.getDJH());
						List<TDYT> tdyts = zd.getTDYTS();
						List<String> list_tdyt = new ArrayList<String>();
						List<String> list_tddj = new ArrayList<String>();
						if (tdyts != null && tdyts.size() > 0) {
							for (TDYT tdyt : tdyts) {
								if (!StringHelper.isEmpty(tdyt.getTDYTName())
										&& !list_tdyt.contains(tdyt
												.getTDYTName())) {
									list_tdyt.add(tdyt.getTDYTName());
								}
								if (!StringHelper.isEmpty(tdyt.getTDDJ())
										&& !list_tddj.contains(tdyt.getTDDJ())) {
									list_tddj.add(tdyt.getTDDJ());
								}
							}
						}
						m_info.put("tdyt",
								StringHelper.formatList(list_tdyt, ","));
						m_info.put("tddj",
								StringHelper.formatList(list_tddj, ","));
					} else if (BDCDYLX.H.Value.equals(info.getBdcdylx())) {
						House h = (House) unit;
						if (!StringHelper.isEmpty(h.getZDBDCDYID())) {
							BDCS_SHYQZD_LS zd = baseCommonDao.get(
									BDCS_SHYQZD_LS.class, h.getZDBDCDYID());
							if (zd != null) {
								m_info.put("th", zd.getTFH());
								m_info.put(
										"shyqlx",
										ConstHelper.getNameByValue("QLXZ",
												zd.getQLXZ()));
								m_info.put("zdszd", zd.getZDSZD());
								m_info.put("zdszx", zd.getZDSZX());
								m_info.put("zdszn", zd.getZDSZN());
								m_info.put("zdszb", zd.getZDSZB());
								m_info.put("zdmj",
										StringHelper.formatDouble(zd.getZDMJ()));
								m_info.put("zdh", zd.getDJH());
								List<BDCS_TDYT_XZ> tdyts_1 = baseCommonDao
										.getDataList(BDCS_TDYT_XZ.class,
												"BDCDYID='" + zd.getId() + "'");
								List<String> list_tdyt_1 = new ArrayList<String>();
								List<String> list_tddj_1 = new ArrayList<String>();
								if (tdyts_1 != null && tdyts_1.size() > 0) {
									for (TDYT tdyt : tdyts_1) {
										if (!StringHelper.isEmpty(tdyt
												.getTDYTName())
												&& !list_tdyt_1.contains(tdyt
														.getTDYTName())) {
											list_tdyt_1.add(tdyt.getTDYTName());
										}
										if (!StringHelper.isEmpty(tdyt
												.getTDDJ())
												&& !list_tddj_1.contains(tdyt
														.getTDDJ())) {
											list_tddj_1.add(tdyt.getTDDJ());
										}
									}
								}
								m_info.put("tdyt", StringHelper.formatList(
										list_tdyt_1, ","));
								m_info.put("tddj", StringHelper.formatList(
										list_tddj_1, ","));
								RealUnit unit_zd=UnitTools.loadUnit(BDCDYLX.SHYQZD, DJDYLY.XZ, h.getZDBDCDYID());
								if(unit_zd!=null){
									UseLand land=(UseLand)unit_zd;
									m_info.put("tdzh", land.getZDBDCQZH());
								}
							}
						}
						m_info.put("dytdmj",
								StringHelper.formatDouble(h.getDYTDMJ()));
						m_info.put("gytdmj",
								StringHelper.formatDouble(h.getGYTDMJ()));
						m_info.put("fttdmj",
								StringHelper.formatDouble(h.getFTTDMJ()));
						m_info.put("jzmj",
								StringHelper.formatDouble(h.getSCJZMJ()));
						if (!StringHelper.isEmpty(h.getQLXZ())) {
							m_info.put(
									"shyqlx",
									ConstHelper.getNameByValue("QLXZ",
											h.getQLXZ()));
						}
						if (!StringHelper.isEmpty(h.getFWTDYT())) {
							m_info.put(
									"tdyt",
									ConstHelper.getNameByValue("TDYT",
											h.getFWTDYT()));
						}
					}

					for (BDCS_QL_GZ ql : qls) {
						if (djdy.getDJDYID().equals(ql.getDJDYID())) {
							m_info.put(
									"crqssj",
									StringHelper.FormatDateOnType(
											ql.getQLQSSJ(), "yyyy-MM-dd"));
							m_info.put(
									"crzzsj",
									StringHelper.FormatDateOnType(
											ql.getQLJSSJ(), "yyyy-MM-dd"));
							String lyqlid = ql.getLYQLID();
							List<BDCS_QLR_GZ> list_qlr_1 = baseCommonDao.getDataList(BDCS_QLR_GZ.class, "QLID='"+ql.getId()+"'");
							if (list_qlr_1 != null && list_qlr_1.size() > 0) {
								List<String> list_qlrmc_1 = new ArrayList<String>();
								List<String> list_frdb_1 = new ArrayList<String>();
								List<String> list_dz_1 = new ArrayList<String>();
								for (BDCS_QLR_GZ qlr_1 : list_qlr_1) {
									if (!list_qlrmc_1
											.contains(qlr_1.getQLRMC())) {
										list_qlrmc_1.add(qlr_1.getQLRMC());
									}
									if (!StringHelper.isEmpty(qlr_1.getFDDBR())
											&& !list_frdb_1.contains(qlr_1
													.getFDDBR())) {
										list_frdb_1.add(qlr_1.getFDDBR());
									}
									if (!StringHelper.isEmpty(qlr_1.getDZ())
											&& !list_dz_1.contains(qlr_1
													.getDZ())) {
										list_dz_1.add(qlr_1.getDZ());
									}
								}
								m_info.put("srf", StringHelper.formatList(
										list_qlrmc_1, ","));
								m_info.put("srffrdb", StringHelper.formatList(
										list_frdb_1, ","));
								m_info.put("srfdz",
										StringHelper.formatList(list_dz_1, ","));
							}
							if (!StringHelper.isEmpty(lyqlid)) {
								BDCS_QL_LS ql_ls = baseCommonDao.get(
										BDCS_QL_LS.class, lyqlid);
								if (ql_ls != null) {
									m_info.put("qzh", ql.getBDCQZH());
									List<BDCS_QLR_LS> list_qlr = baseCommonDao.getDataList(BDCS_QLR_LS.class, "QLID='"+ql_ls.getId()+"'");
									if (list_qlr != null && list_qlr.size() > 0) {
										List<String> list_qlrmc = new ArrayList<String>();
										List<String> list_frdb = new ArrayList<String>();
										List<String> list_dz = new ArrayList<String>();
										for (BDCS_QLR_LS qlr : list_qlr) {
											if (!list_qlrmc.contains(qlr
													.getQLRMC())) {
												list_qlrmc.add(qlr.getQLRMC());
											}
											if (!StringHelper.isEmpty(qlr
													.getFDDBR())
													&& !list_frdb.contains(qlr
															.getFDDBR())) {
												list_frdb.add(qlr.getFDDBR());
											}
											if (!StringHelper.isEmpty(qlr
													.getDZ())
													&& !list_dz.contains(qlr
															.getDZ())) {
												list_dz.add(qlr.getDZ());
											}
										}
										m_info.put("zrf", StringHelper
												.formatList(list_qlrmc, ","));
										m_info.put("zrffrdb", StringHelper
												.formatList(list_frdb, ","));
										m_info.put("zrfdz", StringHelper
												.formatList(list_dz, ","));
									}
									if ("3".equals(ql_ls.getQLLX())
											|| "5".equals(ql_ls.getQLLX())
											|| "7".equals(ql_ls.getQLLX())) {
										m_info.put("tdzh", ql.getBDCQZH());
										m_info.put("qsxz", ConstHelper
												.getNameByValue("QLLX",
														ql_ls.getQLLX()));
									} else if ("4".equals(ql_ls.getQLLX())
											|| "6".equals(ql_ls.getQLLX())
											|| "8".equals(ql_ls.getQLLX())) {
										
										m_info.put("fwsyqzh", ql.getBDCQZH());
										if ("4".equals(ql_ls.getQLLX())) {
											m_info.put("qsxz",
													ConstHelper.getNameByValue(
															"QLLX", "3"));
										} else if ("6".equals(ql_ls.getQLLX())) {
											m_info.put("qsxz",
													ConstHelper.getNameByValue(
															"QLLX", "5"));
										} else if ("8".equals(ql_ls.getQLLX())) {
											m_info.put("qsxz",
													ConstHelper.getNameByValue(
															"QLLX", "7"));
										}

									}
								}
							}
							break;
						}
					}
					m_info.put("crjdj", "");
					m_info.put("crjzj", "");
					List<BDCS_DJSF> list_djsf=baseCommonDao.getDataList(BDCS_DJSF.class, " SFKMMC LIKE '%出让金%' AND YWH='"+project_id+"'");
					if(list_djsf!=null&&list_djsf.size()>0){
						String crje=list_djsf.get(0).getSSJE();
						if(!StringHelper.isEmpty(crje)){
							m_info.put("crjdj", crje);
							m_info.put("crjzj", crje);
						}
					}
					m_info.put("slbh", info.getYwlsh());
					result.add(m_info);
				}
			}
		}
		return result;
	}

	public BDCS_ZRHT updateZRHT(HashMap<String, String> bdcsZrht,
			String projectId) {
		BDCS_ZRHT zrht = new BDCS_ZRHT();
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");     
		if (bdcsZrht != null) {
			try {
				if(bdcsZrht.get("crqssj")!=null&&!bdcsZrht.get("crqssj").equals("")){
					zrht.setCrqssj(sdf.parse(bdcsZrht.get("crqssj")));
				}
				if(bdcsZrht.get("crzzsj")!=null&&!bdcsZrht.get("crzzsj").equals("")){
					zrht.setCrzzsj(sdf.parse(bdcsZrht.get("crzzsj")));
				}
				if(bdcsZrht.get("jbsj")!=null&&!bdcsZrht.get("jbsj").equals("")){
					zrht.setJbsj(sdf.parse(bdcsZrht.get("jbsj")));
				}
				if(bdcsZrht.get("shsj")!=null&&!bdcsZrht.get("shsj").equals("")){
					zrht.setShsj(sdf.parse(bdcsZrht.get("shsj")));
				}
				if(bdcsZrht.get("spsj")!=null&&!bdcsZrht.get("spsj").equals("")){
					zrht.setSpsj(sdf.parse(bdcsZrht.get("spsj")));
				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			zrht.setFilenumber(projectId);
			zrht.setFwsyqzh(bdcsZrht.get("fwsyqzh"));
			zrht.setQsxz(bdcsZrht.get("qsxz"));
			zrht.setQzh(bdcsZrht.get("qzh"));
			zrht.setShyqlx(bdcsZrht.get("shyqlx"));
			zrht.setSrf(bdcsZrht.get("srf"));
			zrht.setSrfdz(bdcsZrht.get("srfdz"));
			zrht.setSrffrdb(bdcsZrht.get("srffrdb"));
			zrht.setTddj(bdcsZrht.get("tddj"));
			zrht.setTdyt(bdcsZrht.get("tdyt"));
			zrht.setTdzh(bdcsZrht.get("tdzh"));
			zrht.setZdh(bdcsZrht.get("zdh"));
			zrht.setZdszb(bdcsZrht.get("zdszb"));
			zrht.setZdszd(bdcsZrht.get("zdszd"));
			zrht.setZdszn(bdcsZrht.get("zdszn"));
			zrht.setZdszx(bdcsZrht.get("zdszx"));
			zrht.setZrf(bdcsZrht.get("zrf"));
			zrht.setZrfdz(bdcsZrht.get("zrfdz"));
			zrht.setZrffrdb(bdcsZrht.get("zrffrdb"));
			zrht.setTh(bdcsZrht.get("th"));
			if (!bdcsZrht.get("gytdmj").equals("") && bdcsZrht.get("gytdmj") != null) {
				zrht.setGytdmj(StringHelper.getDouble(bdcsZrht.get("gytdmj")));
			}
			if (!bdcsZrht.get("jzmj").equals("") && bdcsZrht.get("jzmj") != null) {
				zrht.setJzmj(StringHelper.getDouble(bdcsZrht.get("jzmj")));
			}
			if (!bdcsZrht.get("dytdmj").equals("") && bdcsZrht.get("dytdmj") != null) {
				zrht.setDytdmj(StringHelper.getDouble(bdcsZrht.get("dytdmj")));
			}
			if (!bdcsZrht.get("zdmj").equals("") && bdcsZrht.get("zdmj") != null) {
				zrht.setZdmj(StringHelper.getDouble(bdcsZrht.get("zdmj")));
			}
			if (!bdcsZrht.get("fttdmj").equals("") && bdcsZrht.get("fttdmj") != null) {
				zrht.setFttdmj(StringHelper.getDouble(bdcsZrht.get("fttdmj")));
			}
			
			if (!bdcsZrht.get("zhdmj").equals("") && bdcsZrht.get("zhdmj") != null) {
				zrht.setZhdmj(StringHelper.getDouble(bdcsZrht.get("zhdmj")));
			}
			zrht.setYtdyt(bdcsZrht.get("ytdyt"));
			zrht.setCrjdj(bdcsZrht.get("crjdj"));
			zrht.setCrjzj(bdcsZrht.get("crjzj"));
			zrht.setLglfbz(bdcsZrht.get("lglfbz"));
			zrht.setYjlgf(bdcsZrht.get("yjlgf"));
			zrht.setDahcqk(bdcsZrht.get("dahcqk"));
			zrht.setLglfbz(bdcsZrht.get("lglfbz"));
			zrht.setJbr(bdcsZrht.get("jbr"));
			zrht.setJbryj(bdcsZrht.get("jbryj"));
			zrht.setShr(bdcsZrht.get("shr"));
			zrht.setShyj(bdcsZrht.get("shyj"));
			zrht.setBz(bdcsZrht.get("bz"));
			zrht.setSpyj(bdcsZrht.get("spyj"));
			zrht.setSpr(bdcsZrht.get("spr"));
			zrht.setSpr(bdcsZrht.get("xmkfztz"));
			zrht.setSpr(bdcsZrht.get("gctz"));
			
		}
		String hql = " FILENUMBER = '"+projectId+"'";
		List<BDCS_ZRHT> oldChrt = baseCommonDao.getDataList(BDCS_ZRHT.class, hql);		
		if(oldChrt!=null&&oldChrt.size()>0){
			zrht.setId(oldChrt.get(0).getId());
			baseCommonDao.update(zrht);
		}else{
			zrht.setId(Common.CreatUUID());
			baseCommonDao.save(zrht);
		}
		
		return zrht;

	}

	@Override
	public List<HashMap<String, String>> getZRHT(String projectId) {
		List<HashMap<String, String>> result = new ArrayList<HashMap<String, String>>();
		String sql = "filenumber = '" + projectId + "'";
		List<BDCS_ZRHT> zrht = baseCommonDao.getDataList(BDCS_ZRHT.class, sql);
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");     

		HashMap<String, String> m_info = new HashMap<String, String>();
		if (zrht != null && zrht.size() > 0) {
			try {
				if(zrht.get(0).getCrqssj()!=null&&!zrht.get(0).getCrqssj().equals("")){
					m_info.put("crqssj", zrht.get(0).getCrqssj().toString());
				}
				if(zrht.get(0).getCrzzsj()!=null&&!zrht.get(0).getCrzzsj().equals("")){
					m_info.put("crzzsj", zrht.get(0).getCrzzsj().toString());
				}
				if(zrht.get(0).getJbsj()!=null&&!zrht.get(0).getJbsj().equals("")){
					m_info.put("jbsj",zrht.get(0).getJbsj().toString());
				}
				if(zrht.get(0).getShsj()!=null&&!zrht.get(0).getShsj().equals("")){
					m_info.put("shsj",zrht.get(0).getShsj().toString());
				}
				if(zrht.get(0).getSpsj()!=null&&!zrht.get(0).getSpsj().equals("")){
					m_info.put("spsj",zrht.get(0).getSpsj().toString());
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			m_info.put("fwsyqzh", zrht.get(0).getFwsyqzh());
			m_info.put("qsxz", zrht.get(0).getQsxz());
			m_info.put("qzh", zrht.get(0).getQzh());
			m_info.put("shyqlx", zrht.get(0).getShyqlx());
			m_info.put("srf", zrht.get(0).getSrf());
			m_info.put("srfdz", zrht.get(0).getSrfdz());
			m_info.put("srffrdb", zrht.get(0).getSrffrdb());
			m_info.put("tddj", zrht.get(0).getTddj());
			m_info.put("tdyt", zrht.get(0).getTdyt());
			m_info.put("tdzh", zrht.get(0).getTdzh());
			
			m_info.put("zdh", zrht.get(0).getZdh());
			m_info.put("fwsyqzh", zrht.get(0).getFwsyqzh());
			m_info.put("zrf", zrht.get(0).getZrf());
			m_info.put("zrfdz", zrht.get(0).getZrfdz());
			m_info.put("zrfdzzrfdz", zrht.get(0).getZrffrdb());
			m_info.put("th", zrht.get(0).getTh());

			m_info.put("zdszx", zrht.get(0).getZdszx());
			m_info.put("zdszn", zrht.get(0).getZdszn());
			m_info.put("zdszb", zrht.get(0).getZdszb());
			m_info.put("zdmj", StringHelper.formatDouble(zrht.get(0).getZdmj()));
			m_info.put("zdszb", zrht.get(0).getZdszb());
			m_info.put("dytdmj",
					StringHelper.formatDouble(zrht.get(0).getDytdmj()));
			m_info.put("gytdmj",
					StringHelper.formatDouble(zrht.get(0).getGytdmj()));
			m_info.put("fttdmj",
					StringHelper.formatDouble(zrht.get(0).getFttdmj()));
			m_info.put("jzmj", StringHelper.formatDouble(zrht.get(0).getJzmj()));
			m_info.put("zdmj",StringHelper.formatDouble(zrht.get(0).getZdmj()));
			
			m_info.put("zhdmj",StringHelper.formatDouble(zrht.get(0).getZhdmj()));
			m_info.put("ytdyt",zrht.get(0).getYtdyt());
			m_info.put("crjdj",zrht.get(0).getCrjdj());
			m_info.put("crjzj",zrht.get(0).getCrjzj());
			m_info.put("lglfbz",zrht.get(0).getLglfbz());
			m_info.put("yjlgf",zrht.get(0).getYjlgf());
			m_info.put("dahcqk",zrht.get(0).getDahcqk());
			m_info.put("jbr",zrht.get(0).getJbr());
			m_info.put("jbryj",zrht.get(0).getJbryj());
			m_info.put("shr",zrht.get(0).getShr());
			m_info.put("shyj",zrht.get(0).getShyj());
			m_info.put("bz",zrht.get(0).getBz());
			m_info.put("spyj",zrht.get(0).getSpyj());
			m_info.put("spr",zrht.get(0).getSpr());
			m_info.put("xmkfztz",zrht.get(0).getXmkfztz());
			m_info.put("gctz",zrht.get(0).getGctz());
			result.add(m_info);
		}
		return result;
	}
	public void delZRHT(String projectId){
		if(projectId!=null&&!projectId.equals("")){
			String sql = "filenumber = '" + projectId + "'";
			List<BDCS_ZRHT> zrht = baseCommonDao.getDataList(BDCS_ZRHT.class, sql);				
			if(zrht!=null&&zrht.size()>0){
				baseCommonDao.delete(BDCS_ZRHT.class, zrht.get(0).getId());
				baseCommonDao.flush();
			}
		}
	}
	
	@Override
	public ResultMessage PlDelSflist(String xmbh, HttpServletRequest request) {
		ResultMessage msg=new ResultMessage();
		msg.setMsg("");
		msg.setSuccess("true");
		List<BDCS_SFDY> sfdylist=baseCommonDao.getDataList(BDCS_SFDY.class, "1>0");
		List<BDCS_DJSF> sflist=baseCommonDao.getDataList(BDCS_DJSF.class, "XMBH='"+xmbh+"'");
		if(sflist==null||sflist.size()<=0){
			return msg;
		}
		String configinfo=request.getParameter("configinfo");
		JSONArray configlist=JSONArray.fromObject(configinfo);
		if(configlist!=null&&configlist.size()>0){
			for(int i=0;i<configlist.size();i++){
				JSONObject config=JSONObject.fromObject(configlist.get(i));
				String dykmmc=config.getString("dykmmc");
				String sfxlmc=config.getString("sfxlmc");
				for(BDCS_DJSF sf:sflist){
					String sfdyid=sf.getSFDYID();
					BDCS_SFDY sfdy=baseCommonDao.get(BDCS_SFDY.class, sfdyid);
					if(sfdy!=null){
						if(sfxlmc.equals(sfdy.getSFXLMC())&&!dykmmc.equals(sfdy.getSFKMMC())){
							baseCommonDao.deleteEntity(sf);
						}
					}
				}
			}
		}
		return msg;
	}

	@Override
	public ResultMessage UpdateSfjbOnDJSF(String djsfid, String sfjb) {
		ResultMessage msg=new ResultMessage();
		msg.setSuccess("true");
		msg.setMsg("成功！");
		BDCS_DJSF djsf=baseCommonDao.get(BDCS_DJSF.class, djsfid);
		if(djsf!=null){
			if("true".equals(sfjb)){
				djsf.setSFJB("1");
			}else{
				djsf.setSFJB("0");
			}
			baseCommonDao.update(djsf);
		}
		return msg;
	}
	
	/*
	 * sqrlist要求申请人属性跟BDCS_SQR属性和类型一致，字典一致，本方法不做专门转换，完全根据相应属性进行转换的
	 */
	@Override
	public ResultMessage createXmxxAndSqr(String proisntId, String fileNumber,
			List<Map<String, String>> sqrlist, HttpServletRequest request) {
		ResultMessage msg=new ResultMessage();
		msg.setSuccess("false");
		ProjectInfo projectInfo=null;
		try {
			projectInfo = getProjectInfo(fileNumber, request);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(projectInfo==null){
			msg.setMsg("创建项目信息失败！");
			return msg;
		}
		if(sqrlist!=null&&sqrlist.size()>0){
			for(Map<String, String> sqr:sqrlist){
				BDCS_SQR sqr_entity=new BDCS_SQR();
				StringHelper.setValue(sqr, sqr_entity);
				sqr_entity.setXMBH(projectInfo.getXmbh());
				baseCommonDao.save(sqr_entity);
			}
		}
		baseCommonDao.flush();
		msg.setSuccess("true");
		msg.setMsg("创建成功！");
		return msg;
	}

	@Override
	public void Updatezdbtn(String project_id,
			HttpServletRequest request, Boolean type) {
			BDCS_XMXX xmxx = Global.getXMXX(project_id);
			if (xmxx != null) {
				xmxx.setZDBTN(type);
				Global.getXMXX(project_id);
				Global.getXMXXbyXMBH(xmxx.getId());
			}
	}
	
	@Override
	public void UpdateXMXX(String lsh,String ajh) {
		
		List<BDCS_XMXX> listxxs=baseCommonDao.getDataList(BDCS_XMXX.class, "YWLSH='"+lsh+"'");
        if(listxxs.size()>0){
      	  BDCS_XMXX onexmxx=listxxs.get(0);
      	  onexmxx.setAJH(ajh);
      	  baseCommonDao.saveOrUpdate(onexmxx);
        }
	}

	@Override
	public List<Map<String, String>> GetSQSPBexNJ(String xmbh, String acinstid,
			HttpServletRequest request) {
		List<Map<String, String>> fbs = new ArrayList<Map<String,String>>();
		String strSqlXMBH = ProjectHelper.GetXMBHCondition(xmbh);
		List<BDCS_DJDY_GZ> djdys = baseCommonDao.getDataList(BDCS_DJDY_GZ.class, strSqlXMBH);
		
		List<RightsHolder> _rightsholder = new ArrayList<RightsHolder>(); 
		List<BDCS_SQR> ywrlist = baseCommonDao.getDataList(BDCS_SQR.class, 
				strSqlXMBH+" AND SQRLB='"+ConstValue.SQRLB.YF.Value+"'");
		ProjectInfo projectinfo = ProjectHelper.GetPrjInfoByXMBH(xmbh);
		String workflowcode = ProjectHelper.getWorkflowCodeByProjectID(projectinfo.getProject_id());
		String sql = " WORKFLOWCODE='" + workflowcode + "'";;
		List<WFD_MAPPING> mappings = baseCommonDao.getDataList(WFD_MAPPING.class, sql);
		String workflowname = mappings.get(0).getWORKFLOWNAME();
		ArrayList<String> list = new ArrayList<String>(Arrays.asList("CS006","ZY007","ZY010","ZY007"));  
		for (BDCS_DJDY_GZ bdcs_DJDY_GZ : djdys) {
			String djdyid = bdcs_DJDY_GZ.getDJDYID();
			//判断是否是组合登记，组合登记不止一条权利，且带抵押权的组合登记抵押义务人是产权权利人
			//CS006 ZY007 ZY010 ZY007
			if(list.contains(workflowname)){
				List<Rights> rights = RightsTools.loadRightsByCondition(DJDYLY.GZ, 
						strSqlXMBH+" AND DJDYID='"+djdyid+"' ORDER BY QLLX ASC");
				if(rights!=null&&rights.size()==2){
					//义务人-抵押
//					_rightsholder = RightsHolderTools.loadRightsHolders(DJDYLY.GZ, rights.get(1).getId());
//					if(_rightsholder!=null&&_rightsholder.size()>0){
//						for (RightsHolder rightsHolder : _rightsholder) {
//							fbs.add(addywrinfobypreql(xmbh,bdcs_DJDY_GZ,rightsHolder));
//						}
//					}
					//权利人-抵押
					_rightsholder = RightsHolderTools.loadRightsHolders(DJDYLY.GZ, rights.get(0).getId());
					if(_rightsholder!=null&&_rightsholder.size()>0){
						for (RightsHolder rightsHolder : _rightsholder) {
							fbs.add(addqlrinfo(xmbh,bdcs_DJDY_GZ,rightsHolder));
						}
					}
					
					//义务人-产权
					String Preqlid = rights.get(1).getLYQLID();
					if(!StringHelper.isEmpty(Preqlid)){
						_rightsholder = RightsHolderTools.loadRightsHolders(DJDYLY.LS, Preqlid);
						if(_rightsholder!=null&&_rightsholder.size()>0){
							for (RightsHolder rightsHolder : _rightsholder) {
								fbs.add(addywrinfobypreql(xmbh,bdcs_DJDY_GZ,rightsHolder));
							}
						}
					}else{
						if(ywrlist!=null&&ywrlist.size()>0){
							for (BDCS_SQR bdcs_SQR : ywrlist) {
								fbs.add(addywrinfobysqr(xmbh,bdcs_DJDY_GZ,bdcs_SQR));
							}
						}
					}
					//权利人-产权
					_rightsholder = RightsHolderTools.loadRightsHolders(DJDYLY.GZ, rights.get(1).getId());
					if(_rightsholder!=null&&_rightsholder.size()>0){
						for (RightsHolder rightsHolder : _rightsholder) {
							fbs.add(addqlrinfo(xmbh,bdcs_DJDY_GZ,rightsHolder));
						}
					}
				}else{
					return null;
				}
			}else{
				Rights _right = RightsTools.loadRightsByDJDYID(DJDYLY.GZ, xmbh, djdyid);
				if(_right==null){
					continue;
				}
				String Preqlid = _right.getLYQLID();
				if(!StringHelper.isEmpty(Preqlid)){
					_rightsholder = RightsHolderTools.loadRightsHolders(DJDYLY.LS, Preqlid);
					if(_rightsholder!=null&&_rightsholder.size()>0){
						for (RightsHolder rightsHolder : _rightsholder) {
							fbs.add(addywrinfobypreql(xmbh,bdcs_DJDY_GZ,rightsHolder));
						}
					}
				}else{
					if(ywrlist!=null&&ywrlist.size()>0){
						for (BDCS_SQR bdcs_SQR : ywrlist) {
							fbs.add(addywrinfobysqr(xmbh,bdcs_DJDY_GZ,bdcs_SQR));
						}
					}
				}
				//权利人
				_rightsholder = RightsHolderTools.loadRightsHolders(DJDYLY.GZ, djdyid, xmbh);
				if(_rightsholder!=null&&_rightsholder.size()>0){
					for (RightsHolder rightsHolder : _rightsholder) {
						fbs.add(addqlrinfo(xmbh,bdcs_DJDY_GZ,rightsHolder));
					}
				}
			}
		}
		//Collections.reverse(fbs);
		
		Collections.sort(fbs, new Comparator<Map<String,String>>() {

			@Override
			public int compare(Map<String, String> o1, Map<String, String> o2) {
		        if(null == o2){  
		            return 1;  
		        }  
		        // 直接相等  
		        if(o1 == o2 || o1.equals(o2)){  
		            return 0;  
		        }  
		        String FH1 = o1.get("FH");  
		        String FH2 = o2.get("FH");  
		        // 特殊情形，ZL有一个为空的情况.  
		        if(null == FH1 || StringHelper.isEmpty(FH1)){  
		            // 都为空，认为相对  
		            if(null == FH2 || StringHelper.isEmpty(FH2)){  
		            	 //原来的排序
						if(o1.get("FB1_ZL").equals(o2.get("FB1_ZL"))){
							if(o1.get("DJDYID").equals(o2.get("DJDYID"))){
								if(o1.get("FB1_QLRLB").equals(o2.get("FB1_QLRLB"))){
									if(StringHelper.isEmpty(o1.get("FB2_DYJE"))){
										return 1;
									}
								}
								return o1.get("FB1_QLRLB").compareTo(o2.get("FB1_QLRLB"));
							}
							return o1.get("DJDYID").compareTo(o2.get("DJDYID"));
						}
						if(!StringHelper.isEmpty(o1.get("FB1_ZL"))){
							return o1.get("FB1_ZL").compareTo(o2.get("FB1_ZL"));
						}
						return -1;  
		            } else {  
		                return -1;  
		            }  
		        } else if(null == FH2 || StringHelper.isEmpty(FH2)){  
		            return 1;  
		        }  
		        // 中间 1-多个数字  
		        Pattern pattern = Pattern.compile("\\D*(\\d+)\\D*");  
		        Matcher matcher1 = pattern.matcher(FH1);  
		        Matcher matcher2 = pattern.matcher(FH2);  
		        //System.out.println(pattern.pattern());  
		        //  
		        int index1_step = 0;  
		        int index2_step = 0;  
		        while(matcher1.find()){  
		            String s1 = matcher1.group(1);  
		            String s2 = null;  
		            if(matcher2.find()){  
		                s2 = matcher2.group(1);  
		            }  
		            int index1 = FH1.indexOf(s1, index1_step);  
		            int index2 = FH2.indexOf(s2, index2_step);  
		            //  
		            index1_step = index1;  
		            index2_step = index2;  
		            // 索引相等的情况下  
		            if(index1 == index2){  
		                String pre1 = FH1.substring(0, index1);  
		                String pre2 = FH2.substring(0, index2);  
		                if(pre1.equals(pre2)){  
		                    //   
		                    long num1 = Long.parseLong(s1);  
		                    long num2 = Long.parseLong(s2);  
		                    //  
		                    if(num1 == num2){  
		                        // 比较下一组  
		                        continue;  
		                    } else {  
		                        return (int)(num1 - num2);  
		                    }  
		                } else {  
		                    break;  
		                }  
		            } else {  
		                break;  
		            }  
		        }  
		        // 最后的情形.  
		        return FH1.compareTo(FH2);   
			}		
		});
		return fbs;
	}

	private Map<String, String> addqlrinfo(String xmbh, BDCS_DJDY_GZ bdcs_DJDY_GZ,
			RightsHolder rightsHolder) {
		Map<String,String> map = new HashMap<String, String>();
		BDCDYLX bdcdylx = BDCDYLX.initFrom(bdcs_DJDY_GZ.getBDCDYLX());
		RealUnit h_unit = UnitTools.loadUnit(bdcdylx, DJDYLY.LS, bdcs_DJDY_GZ.getBDCDYID());
		if(h_unit==null){
			h_unit = UnitTools.loadUnit(bdcdylx, DJDYLY.GZ, bdcs_DJDY_GZ.getBDCDYID());
		}
		House house = (House) h_unit;
		RealUnit zd_unit = UnitTools.loadUnit(BDCDYLX.SHYQZD, DJDYLY.LS, house.getZDBDCDYID());
		if(zd_unit==null){
			zd_unit = UnitTools.loadUnit(BDCDYLX.SHYQZD, DJDYLY.GZ, house.getZDBDCDYID());
		}
		Rights _right = RightsTools.loadRights(DJDYLY.GZ, rightsHolder.getQLID());
		SubRights _subright = RightsTools.loadSubRights(DJDYLY.GZ, _right.getFSQLID());
		
		map.put("DJDYID", bdcs_DJDY_GZ.getDJDYID());
		//面积保留两位小数
		DecimalFormat df = new DecimalFormat("######0.00");  
		// 排序用 FH
		map.put("FH", StringHelper.formatObject(house.getFH()));
		/**
		 * 坐落 FB1_*/
		map.put("FB1_ZL", StringHelper.formatObject(house.getZL()));
		/**
		 * 坐落 FB2_*/
		map.put("FB2_ZL", StringHelper.formatObject(house.getZL()));
		/**
		 * 建筑面积 FB1_*/
		map.put("FB1_JZMJ", df.format(h_unit.getMJ()));
		/**
		 * 套内面积 FB1_*/
		/**
		 * 分摊建筑面积 FB1_*/
		if(bdcdylx.equals(ConstValue.BDCDYLX.H)){
			map.put("FB1_TNMJ", df.format(house.getSCTNJZMJ()));
			map.put("FB1_FTJZMJ", df.format(house.getSCFTJZMJ()));
		}else if(bdcdylx.equals(ConstValue.BDCDYLX.YCH)){
			map.put("FB1_TNMJ", df.format(house.getYCTNJZMJ()));
			map.put("FB1_FTJZMJ", df.format(house.getYCFTJZMJ()));
		}
		/**
		 * 分摊土地面积 FB1_*/
		map.put("FB1_FTTDMJ", df.format(house.getFTTDMJ()));
		/**
		 * 所在层 FB1_*/
		map.put("FB1_SZC", StringHelper.formatObject(house.getSZC()));
		/**
		 * 用途 FB1_*/
		map.put("FB1_YT", StringHelper.formatObject(house.getGHYTName()));
		/**
		 * 权利性质 FB1_*/
		map.put("FB1_QLXZ", StringHelper.formatObject(
				ConstHelper.getNameByValue("QLXZ", house.getQLXZ())));
		/**
		 * 土地使用起止时间 FB1_*/
		map.put("FB1_TDSYQZSJ", StringHelper.FormatByDatetime(house.getTDSYQQSRQ())
				+ "----" + StringHelper.FormatByDatetime(house.getTDSYQZZRQ()));
		/**
		 * 宗地共有面积 FB2_*/
		map.put("FB2_ZDGYMJ", df.format(zd_unit.getMJ()));
		/**
		 * 总层数 FB2_*/
		map.put("FB2_ZCS", StringHelper.formatObject(house.getZCS()));
		/**
		 * 房屋性质 FB2_*/
		map.put("FB2_FWXZ", StringHelper.formatObject(
				ConstHelper.getNameByValue("FWXZ", house.getFWXZ())));
		/**
		 * 房屋结构 FB2_*/
		map.put("FB2_FWJG", StringHelper.formatObject(
				ConstHelper.getNameByValue("FWJG",house.getFWJG())));
		/**
		 * ********
		 * 证件种类 FB2_*/
		map.put("FB2_ZJZL", StringHelper.formatObject(rightsHolder.getZJZLName()));
		/**
		 * 证件号 FB2_*/
		map.put("FB2_ZJH", StringHelper.formatObject(rightsHolder.getZJH()));
		/**
		 * 法定代表人或负责人 FB2_*/
		map.put("FB2_FDDBR", StringHelper.formatObject(rightsHolder.getFDDBR()));
		/**
		 * 代理人名称 FB2_*/
		map.put("FB2_DLR", StringHelper.formatObject(rightsHolder.getDLRXM()));
		/**
		 * 代理人证件号 FB2_*/
		map.put("FB2_DLRZJH", StringHelper.formatObject(rightsHolder.getDLRZJHM()));
		/**
		 * 权利人类型 FB1_*/
		map.put("FB1_QLRLX", ConstHelper.getNameByValue("QLRLX",rightsHolder.getQLRLX()));
		/**
		 * 权利人类别 FB1_*/
		map.put("FB1_QLRLB", "权利人");
		/**
		 * 权利人姓名 FB1_*/
		map.put("FB1_QLRMC", StringHelper.formatObject(rightsHolder.getQLRMC()));
		/**
		 * 持证方式 FB2_*/
		map.put("FB2_CZFS", ConstHelper.getNameByValue("CZFS",_right.getCZFS()));
		/**
		 * 共有方式 FB2_*/
		map.put("FB2_GYFS", StringHelper.formatObject(rightsHolder.getGYFSName()));
		/**
		 * 登记原因 FB1_*/
		map.put("FB1_DJYY", StringHelper.formatObject(_right.getDJYY()));
		/**
		 * 抵押金额 FB2_*/
		if(ConstValue.QLLX.DIYQ.Value.equals(_right.getQLLX())){
			if(ConstValue.DYFS.YBDY.Value.equals(_subright.getDYFS())){
				map.put("FB2_DYJE", StringHelper.formatDouble(_subright.getBDBZZQSE()));
			}else if(ConstValue.DYFS.ZGEDY.Value.equals(_subright.getDYFS())){
				map.put("FB2_DYJE", StringHelper.formatDouble(_subright.getZGZQSE()));
			}
			/**
			 * 抵押起止时间 FB2_*/
			map.put("FB2_DYQZSJ", StringHelper.FormatByDatetime(_right.getQLQSSJ()) 
					+ "----" + StringHelper.FormatByDatetime(_right.getQLJSSJ()));
		}
		/**
		 */
		return map;
	}

	private Map<String, String> addywrinfobypreql(String xmbh, BDCS_DJDY_GZ bdcs_DJDY_GZ,
			RightsHolder rightsHolder) {
		Map<String,String> map = new HashMap<String, String>();
		BDCDYLX bdcdylx = BDCDYLX.initFrom(bdcs_DJDY_GZ.getBDCDYLX());
		RealUnit h_unit = UnitTools.loadUnit(bdcdylx, DJDYLY.LS, bdcs_DJDY_GZ.getBDCDYID());
		if(h_unit==null){
			h_unit = UnitTools.loadUnit(bdcdylx, DJDYLY.GZ, bdcs_DJDY_GZ.getBDCDYID());
		}
		House house = (House) h_unit;
		RealUnit zd_unit = UnitTools.loadUnit(BDCDYLX.SHYQZD, DJDYLY.LS, house.getZDBDCDYID());
		if(zd_unit==null){
			zd_unit = UnitTools.loadUnit(BDCDYLX.SHYQZD, DJDYLY.GZ, house.getZDBDCDYID());
		}
		
		map.put("DJDYID", bdcs_DJDY_GZ.getDJDYID());
		//面积保留两位小数
		DecimalFormat df = new DecimalFormat("######0.00");   
		// 排序用 FH
		map.put("FH", StringHelper.formatObject(house.getFH()));
		/**
		 * 坐落 FB1_*/
		map.put("FB1_ZL", StringHelper.formatObject(house.getZL()));
		/**
		 * 坐落 FB2_*/
		map.put("FB2_ZL", StringHelper.formatObject(house.getZL()));
	
		/**
		 * 建筑面积 FB1_*/
		map.put("FB1_JZMJ", df.format(h_unit.getMJ()));
		/**
		 * 套内面积 FB1_*/
		/**
		 * 分摊建筑面积 FB1_*/
		if(bdcdylx.equals(ConstValue.BDCDYLX.H)){
			map.put("FB1_TNMJ", df.format(house.getSCTNJZMJ()));
			map.put("FB1_FTJZMJ", df.format(house.getSCFTJZMJ()));
		}else if(bdcdylx.equals(ConstValue.BDCDYLX.YCH)){
			map.put("FB1_TNMJ",  df.format(house.getYCTNJZMJ()));
			map.put("FB1_FTJZMJ",  df.format(house.getYCFTJZMJ()));
		}
		/**
		 * 分摊土地面积 FB1_*/
		map.put("FB1_FTTDMJ", df.format(house.getFTTDMJ()));
		/**
		 * 所在层 FB1_*/
		map.put("FB1_SZC", StringHelper.formatObject(house.getSZC()));
		/**
		 * 用途 FB1_*/
		map.put("FB1_YT", StringHelper.formatObject(house.getGHYTName()));
		/**
		 * 权利性质 FB1_*/
		map.put("FB1_QLXZ", StringHelper.formatObject(
				ConstHelper.getNameByValue("QLXZ", house.getQLXZ())));
		/**
		 * 土地使用起止时间 FB1_*/
		map.put("FB1_TDSYQZSJ", StringHelper.FormatByDatetime(house.getTDSYQQSRQ())
				+ "----" + StringHelper.FormatByDatetime(house.getTDSYQZZRQ()));
		/**
		 * 宗地共有面积 FB2_*/
		map.put("FB2_ZDGYMJ", df.format(zd_unit.getMJ()));
		/**
		 * 总层数 FB2_*/
		map.put("FB2_ZCS", StringHelper.formatObject(house.getZCS()));
		/**
		 * 房屋性质 FB2_*/
		map.put("FB2_FWXZ", StringHelper.formatObject(
				ConstHelper.getNameByValue("FWXZ", house.getFWXZ())));
		/**
		 * 房屋结构 FB2_*/
		map.put("FB2_FWJG", StringHelper.formatObject(
				ConstHelper.getNameByValue("FWJG",house.getFWJG())));
		/**
		 * ********
		 * 证件种类 FB2_*/
		map.put("FB2_ZJZL", StringHelper.formatObject(rightsHolder.getZJZLName()));
		/**
		 * 证件号 FB2_*/
		map.put("FB2_ZJH", StringHelper.formatObject(rightsHolder.getZJH()));
		/**
		 * 法定代表人或负责人 FB2_*/
		map.put("FB2_FDDBR", StringHelper.formatObject(rightsHolder.getFDDBR()));
		/**
		 * 代理人名称 FB2_*/
		map.put("FB2_DLR", StringHelper.formatObject(rightsHolder.getDLRXM()));
		/**
		 * 代理人证件号 FB2_*/
		map.put("FB2_DLRZJH", StringHelper.formatObject(rightsHolder.getDLRZJHM()));
		/**
		 * 权利人类型 FB1_*/
		map.put("FB1_QLRLX", ConstHelper.getNameByValue("QLRLX",rightsHolder.getQLRLX()));
		/**
		 * 权利人类别 FB1_*/
		map.put("FB1_QLRLB", "义务人");
		/**
		 * 权利人姓名 FB1_*/
		map.put("FB1_QLRMC", StringHelper.formatObject(rightsHolder.getQLRMC()));
		/**
		 * 持证方式 FB2_*/
		map.put("FB2_CZFS", "");
		/**
		 * 共有方式 FB2_*/
		map.put("FB2_GYFS", ConstHelper.getNameByValue("GYFS",rightsHolder.getGYFS()));
		/**
		 * 登记原因 FB1_*/
		map.put("FB1_DJYY", "");
		/**
		 * 抵押金额 FB2_*/
		map.put("FB2_DYJE", "");
		/**
		 * 抵押起止时间 FB2_*/
		map.put("FB2_DYQZSJ", "");
		
		return map;
	}
	
	private Map<String, String> addywrinfobysqr(String xmbh, BDCS_DJDY_GZ bdcs_DJDY_GZ,
			BDCS_SQR bdcs_SQR) {
		Map<String,String> map = new HashMap<String, String>();
		BDCDYLX bdcdylx = BDCDYLX.initFrom(bdcs_DJDY_GZ.getBDCDYLX());
		RealUnit h_unit = UnitTools.loadUnit(bdcdylx, DJDYLY.LS, bdcs_DJDY_GZ.getBDCDYID());
		if(h_unit==null){
			h_unit = UnitTools.loadUnit(bdcdylx, DJDYLY.GZ, bdcs_DJDY_GZ.getBDCDYID());
		}
		House house = (House) h_unit;
		RealUnit zd_unit = UnitTools.loadUnit(BDCDYLX.SHYQZD, DJDYLY.LS, house.getZDBDCDYID());
		if(zd_unit==null){
			zd_unit = UnitTools.loadUnit(BDCDYLX.SHYQZD, DJDYLY.GZ, house.getZDBDCDYID());
		}
		
		map.put("DJDYID", bdcs_DJDY_GZ.getDJDYID());
		//面积保留两位小数
		DecimalFormat df = new DecimalFormat("######0.00");
		// 排序用 FH
		map.put("FH", StringHelper.formatObject(house.getFH()));
		/**
		 * 坐落 FB1_*/
		map.put("FB1_ZL", StringHelper.formatObject(house.getZL()));
		/**
		 * 坐落 FB2_*/
		map.put("FB2_ZL", StringHelper.formatObject(house.getZL()));
		/**
		 * 建筑面积 FB1_*/
		map.put("FB1_JZMJ", df.format(h_unit.getMJ()));
		/**
		 * 套内面积 FB1_*/
		/**
		 * 分摊建筑面积 FB1_*/
		if(bdcdylx.equals(ConstValue.BDCDYLX.H)){
			map.put("FB1_TNMJ", df.format(house.getSCTNJZMJ()));
			map.put("FB1_FTJZMJ",df.format(house.getSCFTJZMJ()));
		}else if(bdcdylx.equals(ConstValue.BDCDYLX.YCH)){
			map.put("FB1_TNMJ", df.format(house.getYCTNJZMJ()));
			map.put("FB1_FTJZMJ", df.format(house.getYCFTJZMJ()));
		}
		/**
		 * 分摊土地面积 FB1_*/
		map.put("FB1_FTTDMJ", df.format(house.getFTTDMJ()));
		/**
		 * 所在层 FB1_*/
		map.put("FB1_SZC", StringHelper.formatObject(house.getSZC()));
		/**
		 * 用途 FB1_*/
		map.put("FB1_YT", StringHelper.formatObject(house.getGHYTName()));
		/**
		 * 权利性质 FB1_*/
		map.put("FB1_QLXZ", StringHelper.formatObject(
				ConstHelper.getNameByValue("QLXZ", house.getQLXZ())));
		/**
		 * 土地使用起止时间 FB1_*/
		map.put("FB1_TDSYQZSJ", StringHelper.FormatByDatetime(house.getTDSYQQSRQ())
				+ "----" + StringHelper.FormatByDatetime(house.getTDSYQZZRQ()));
		/**
		 * 宗地共有面积 FB2_*/
		map.put("FB2_ZDGYMJ", df.format(zd_unit.getMJ()));
		/**
		 * 总层数 FB2_*/
		map.put("FB2_ZCS", StringHelper.formatObject(house.getZCS()));
		/**
		 * 房屋性质 FB2_*/
		map.put("FB2_FWXZ", StringHelper.formatObject(
				ConstHelper.getNameByValue("FWXZ", house.getFWXZ())));
		/**
		 * 房屋结构 FB2_*/
		map.put("FB2_FWJG", StringHelper.formatObject(
				ConstHelper.getNameByValue("FWJG",house.getFWJG())));
		/**
		 * ********
		 * 证件种类 FB2_*/
		map.put("FB2_ZJZL", ConstHelper.getNameByValue("ZJLX",bdcs_SQR.getZJLX()));
		/**
		 * 证件号 FB2_*/
		map.put("FB2_ZJH", StringHelper.formatObject(bdcs_SQR.getZJH()));
		/**
		 * 法定代表人或负责人 FB2_*/
		map.put("FB2_FDDBR", StringHelper.formatObject(bdcs_SQR.getFDDBR()));
		/**
		 * 代理人名称 FB2_*/
		map.put("FB2_DLR", StringHelper.formatObject(bdcs_SQR.getDLRXM()));
		/**
		 * 代理人证件号 FB2_*/
		map.put("FB2_DLRZJH", StringHelper.formatObject(bdcs_SQR.getDLRZJHM()));
		/**
		 * 权利人类型 FB1_*/
		map.put("FB1_QLRLX", ConstHelper.getNameByValue("QLRLX",bdcs_SQR.getSQRLX()));
		/**
		 * 权利人类别 FB1_*/
		map.put("FB1_QLRLB", "义务人");
		/**
		 * 权利人姓名 FB1_*/
		map.put("FB1_QLRMC", StringHelper.formatObject(bdcs_SQR.getSQRXM()));
		/**
		 * 持证方式 FB2_*/
		map.put("FB2_CZFS", "");
		/**
		 * 共有方式 FB2_*/
		map.put("FB2_GYFS", ConstHelper.getNameByValue("GYFS",bdcs_SQR.getGYFS()));
		/**
		 * 登记原因 FB1_*/
		map.put("FB1_DJYY", "");
		/**
		 * 抵押金额 FB2_*/
		map.put("FB2_DYJE", "");
		/**
		 * 抵押起止时间 FB2_*/
		map.put("FB2_DYQZSJ", "");
		
		return map;
	}
	
	@Override
	public String getYwlshByCondtionsPl(String operaStaffString,int protype,String qlr, String ywr, String zjh,
			String qzh, String zl, String bdcdyh,boolean PreciseQueryQLR) {
		StringBuilder builder = new StringBuilder();
		builder.append("SELECT YWLSH FROM BDCK.BDCS_XMXX XMXX ");
		builder.append(" LEFT JOIN BDC_WORKFLOW.WFI_PROINST P ON XMXX.PROJECT_ID =P.FILE_NUMBER ");
		builder.append(" LEFT JOIN BDC_WORKFLOW.WFI_ACTINST A ON P.PROINST_ID = A.PROINST_ID ");
		if(protype == 3){
			builder.append("WHERE A.STAFF_ID ='"+operaStaffString+"' AND A.ACTINST_NAME IS NOT NULL AND A.ACTINST_NAME='发证' AND A.ACTINST_STATUS IN (3) AND ");
		}else{
			builder.append("WHERE  A.ACTINST_NAME IS NOT NULL AND A.ACTINST_NAME='发证' AND A.ACTINST_STATUS IN (1，2) AND ");
		}
		
		if (!StringHelper.isEmpty(qlr)) {
			builder.append("EXISTS (SELECT 1 FROM BDCK.BDCS_SQR ");
			builder.append("WHERE XMBH=XMXX.XMBH ");
			builder.append("AND ");
			if (!StringHelper.isEmpty(qlr)) {
				if (PreciseQueryQLR) {
					if (!StringHelper.isEmpty(zjh)) {
						builder.append("(SQRLB='1' AND SQRXM='" + qlr
								+ "' AND ZJH='" + zjh + "') ");
					} else {
						builder.append("(SQRLB='1' AND SQRXM='" + qlr + "') ");
					}
				} else {
					if (!StringHelper.isEmpty(zjh)) {
						builder.append("(SQRLB='1' AND SQRXM LIKE '%" + qlr
								+ "%' AND ZJH='" + zjh + "') ");
					} else {
						builder.append("(SQRLB='1' AND SQRXM LIKE '%" + qlr
								+ "%') ");
					}
				}
			}
			builder.append(")");
		}
		if (!StringHelper.isEmpty(ywr)) {
			if (!StringHelper.isEmpty(qlr)) {
				builder.append("AND ");
			}
			builder.append("EXISTS (SELECT 1 FROM BDCK.BDCS_SQR ");
			builder.append("WHERE XMBH=XMXX.XMBH ");
			builder.append("AND ");
			if (!StringHelper.isEmpty(ywr)) {
				if (PreciseQueryQLR) {
					if (!StringHelper.isEmpty(zjh)) {
						builder.append("(SQRLB='2' AND SQRXM='" + ywr
								+ "' AND ZJH='" + zjh + "') ");
					} else {
						builder.append("(SQRLB='2' AND SQRXM='" + ywr + "') ");
					}
				} else {
					if (!StringHelper.isEmpty(zjh)) {
						builder.append("(SQRLB='2' AND SQRXM LIKE '%" + ywr
								+ "%' AND ZJH='" + zjh + "') ");
					} else {
						builder.append("(SQRLB='2' AND SQRXM LIKE '%" + ywr
								+ "%') ");
					}
				}
			}
			builder.append(")");
		}
		if (StringHelper.isEmpty(qlr) && StringHelper.isEmpty(ywr)
				&& !StringHelper.isEmpty(zjh)) {
			builder.append("EXISTS (SELECT 1 FROM BDCK.BDCS_SQR ");
			builder.append("WHERE XMBH=XMXX.XMBH ");
			builder.append("AND ");
			builder.append("(ZJH='" + zjh + "') ");
			builder.append(")");
		}
		if (!StringHelper.isEmpty(qzh)) {
			if (!StringHelper.isEmpty(qlr) || !StringHelper.isEmpty(ywr)
					|| !StringHelper.isEmpty(zjh)) {
				builder.append("AND ");
			}
			builder.append("EXISTS (SELECT 1 FROM BDCK.BDCS_QLR_GZ WHERE XMBH=XMXX.XMBH AND BDCQZH LIKE '%"
					+ qzh + "%') ");
		}
		if (!StringHelper.isEmpty(zl)) {
			if (!StringHelper.isEmpty(qlr) || !StringHelper.isEmpty(ywr)
					|| !StringHelper.isEmpty(zjh) || !StringHelper.isEmpty(qzh)) {
				builder.append("AND ");
			}
			builder.append("EXISTS (SELECT 1 FROM ( ");
			builder.append("SELECT DY.ZL ZL,DJDY.XMBH FROM BDCK.BDCS_DJDY_GZ DJDY LEFT JOIN BDCK.BDCS_H_GZ DY ON DY.BDCDYID=DJDY.BDCDYID ");
			builder.append("WHERE DJDY.BDCDYLX='031' AND DJDY.LY='01' ");
			builder.append("UNION ");
			builder.append("SELECT DY.ZL ZL,DJDY.XMBH FROM BDCK.BDCS_DJDY_GZ DJDY LEFT JOIN BDCK.BDCS_H_LS DY ON DY.BDCDYID=DJDY.BDCDYID ");
			builder.append("WHERE DJDY.BDCDYLX='031' AND DJDY.LY IN ('02','03') ");
			builder.append("UNION ");
			builder.append("SELECT DY.ZL ZL,DJDY.XMBH FROM BDCK.BDCS_DJDY_GZ DJDY LEFT JOIN BDCK.BDCS_H_GZY DY ON DY.BDCDYID=DJDY.BDCDYID ");
			builder.append("WHERE DJDY.BDCDYLX='032' AND DJDY.LY='01' ");
			builder.append("UNION ");
			builder.append("SELECT DY.ZL ZL,DJDY.XMBH FROM BDCK.BDCS_DJDY_GZ DJDY LEFT JOIN BDCK.BDCS_H_LSY DY ON DY.BDCDYID=DJDY.BDCDYID ");
			builder.append("WHERE DJDY.BDCDYLX='032' AND DJDY.LY IN ('02','03') ");
			builder.append("UNION ");
			builder.append("SELECT DY.ZL ZL,DJDY.XMBH FROM BDCK.BDCS_DJDY_GZ DJDY LEFT JOIN BDCK.BDCS_SHYQZD_GZ DY ON DY.BDCDYID=DJDY.BDCDYID ");
			builder.append("WHERE DJDY.BDCDYLX='02' AND DJDY.LY='01' ");
			builder.append("UNION ");
			builder.append("SELECT DY.ZL ZL,DJDY.XMBH FROM BDCK.BDCS_DJDY_GZ DJDY LEFT JOIN BDCK.BDCS_SHYQZD_LS DY ON DY.BDCDYID=DJDY.BDCDYID ");
			builder.append("WHERE DJDY.BDCDYLX='02' AND DJDY.LY IN ('02','03') ");
			builder.append("UNION ");
			builder.append("SELECT DY.ZL ZL,DJDY.XMBH FROM BDCK.BDCS_DJDY_GZ DJDY LEFT JOIN BDCK.BDCS_SYQZD_GZ DY ON DY.BDCDYID=DJDY.BDCDYID ");
			builder.append("WHERE DJDY.BDCDYLX='01' AND DJDY.LY='01' ");
			builder.append("UNION ");
			builder.append("SELECT DY.ZL ZL,DJDY.XMBH FROM BDCK.BDCS_DJDY_GZ DJDY LEFT JOIN BDCK.BDCS_SYQZD_LS DY ON DY.BDCDYID=DJDY.BDCDYID ");
			builder.append("WHERE DJDY.BDCDYLX='01' AND DJDY.LY IN ('02','03') ");
			builder.append("UNION ");
			builder.append("SELECT DY.ZL ZL,DJDY.XMBH FROM BDCK.BDCS_DJDY_GZ DJDY LEFT JOIN BDCK.BDCS_ZH_GZ DY ON DY.BDCDYID=DJDY.BDCDYID ");
			builder.append("WHERE DJDY.BDCDYLX='04' AND DJDY.LY='01' ");
			builder.append("UNION ");
			builder.append("SELECT DY.ZL ZL,DJDY.XMBH FROM BDCK.BDCS_DJDY_GZ DJDY LEFT JOIN BDCK.BDCS_ZH_LS DY ON DY.BDCDYID=DJDY.BDCDYID ");
			builder.append("WHERE DJDY.BDCDYLX='04' AND DJDY.LY IN ('02','03') ");
			builder.append("UNION ");
			builder.append("SELECT DY.ZL ZL,DJDY.XMBH FROM BDCK.BDCS_DJDY_GZ DJDY LEFT JOIN BDCK.BDCS_SLLM_GZ DY ON DY.BDCDYID=DJDY.BDCDYID ");
			builder.append("WHERE DJDY.BDCDYLX='05' AND DJDY.LY='01' ");
			builder.append("UNION ");
			builder.append("SELECT DY.ZL ZL,DJDY.XMBH FROM BDCK.BDCS_DJDY_GZ DJDY LEFT JOIN BDCK.BDCS_SLLM_LS DY ON DY.BDCDYID=DJDY.BDCDYID ");
			builder.append("WHERE DJDY.BDCDYLX='05' AND DJDY.LY IN ('02','03') ");
			builder.append("UNION ");
			builder.append("SELECT DY.ZL ZL,DJDY.XMBH FROM BDCK.BDCS_DJDY_GZ DJDY LEFT JOIN BDCK.BDCS_NYD_GZ DY ON DY.BDCDYID=DJDY.BDCDYID ");
			builder.append("WHERE DJDY.BDCDYLX='09' AND DJDY.LY='01' ");
			builder.append("UNION ");
			builder.append("SELECT DY.ZL ZL,DJDY.XMBH FROM BDCK.BDCS_DJDY_GZ DJDY LEFT JOIN BDCK.BDCS_NYD_LS DY ON DY.BDCDYID=DJDY.BDCDYID ");
			builder.append("WHERE DJDY.BDCDYLX='09' AND DJDY.LY IN ('02','03') ");
			builder.append(") DYINFO ");
			builder.append("WHERE DYINFO.XMBH=XMXX.XMBH AND DYINFO.ZL LIKE '%"
					+ zl + "%')");
		}
		if (!StringHelper.isEmpty(bdcdyh)) {
			if (!StringHelper.isEmpty(qlr) || !StringHelper.isEmpty(ywr)
					|| !StringHelper.isEmpty(zjh) || !StringHelper.isEmpty(qzh)) {
				builder.append("AND ");
			}
			builder.append("EXISTS (SELECT 1 FROM ( ");
			builder.append("SELECT DY.BDCDYH BDCDYH,DJDY.XMBH FROM BDCK.BDCS_DJDY_GZ DJDY LEFT JOIN BDCK.BDCS_H_GZ DY ON DY.BDCDYID=DJDY.BDCDYID ");
			builder.append("WHERE DJDY.BDCDYLX='031' AND DJDY.LY='01' ");
			builder.append("UNION ");
			builder.append("SELECT DY.BDCDYH BDCDYH,DJDY.XMBH FROM BDCK.BDCS_DJDY_GZ DJDY LEFT JOIN BDCK.BDCS_H_LS DY ON DY.BDCDYID=DJDY.BDCDYID ");
			builder.append("WHERE DJDY.BDCDYLX='031' AND DJDY.LY IN ('02','03') ");
			builder.append("UNION ");
			builder.append("SELECT DY.BDCDYH BDCDYH,DJDY.XMBH FROM BDCK.BDCS_DJDY_GZ DJDY LEFT JOIN BDCK.BDCS_H_GZY DY ON DY.BDCDYID=DJDY.BDCDYID ");
			builder.append("WHERE DJDY.BDCDYLX='032' AND DJDY.LY='01' ");
			builder.append("UNION ");
			builder.append("SELECT DY.BDCDYH BDCDYH,DJDY.XMBH FROM BDCK.BDCS_DJDY_GZ DJDY LEFT JOIN BDCK.BDCS_H_LSY DY ON DY.BDCDYID=DJDY.BDCDYID ");
			builder.append("WHERE DJDY.BDCDYLX='032' AND DJDY.LY IN ('02','03') ");
			builder.append("UNION ");
			builder.append("SELECT DY.BDCDYH BDCDYH,DJDY.XMBH FROM BDCK.BDCS_DJDY_GZ DJDY LEFT JOIN BDCK.BDCS_SHYQZD_GZ DY ON DY.BDCDYID=DJDY.BDCDYID ");
			builder.append("WHERE DJDY.BDCDYLX='02' AND DJDY.LY='01' ");
			builder.append("UNION ");
			builder.append("SELECT DY.BDCDYH BDCDYH,DJDY.XMBH FROM BDCK.BDCS_DJDY_GZ DJDY LEFT JOIN BDCK.BDCS_SHYQZD_LS DY ON DY.BDCDYID=DJDY.BDCDYID ");
			builder.append("WHERE DJDY.BDCDYLX='02' AND DJDY.LY IN ('02','03') ");
			builder.append("UNION ");
			builder.append("SELECT DY.BDCDYH BDCDYH,DJDY.XMBH FROM BDCK.BDCS_DJDY_GZ DJDY LEFT JOIN BDCK.BDCS_SYQZD_GZ DY ON DY.BDCDYID=DJDY.BDCDYID ");
			builder.append("WHERE DJDY.BDCDYLX='01' AND DJDY.LY='01' ");
			builder.append("UNION ");
			builder.append("SELECT DY.BDCDYH BDCDYH,DJDY.XMBH FROM BDCK.BDCS_DJDY_GZ DJDY LEFT JOIN BDCK.BDCS_SYQZD_LS DY ON DY.BDCDYID=DJDY.BDCDYID ");
			builder.append("WHERE DJDY.BDCDYLX='01' AND DJDY.LY IN ('02','03') ");
			builder.append("UNION ");
			builder.append("SELECT DY.BDCDYH BDCDYH,DJDY.XMBH FROM BDCK.BDCS_DJDY_GZ DJDY LEFT JOIN BDCK.BDCS_ZH_GZ DY ON DY.BDCDYID=DJDY.BDCDYID ");
			builder.append("WHERE DJDY.BDCDYLX='04' AND DJDY.LY='01' ");
			builder.append("UNION ");
			builder.append("SELECT DY.BDCDYH BDCDYH,DJDY.XMBH FROM BDCK.BDCS_DJDY_GZ DJDY LEFT JOIN BDCK.BDCS_ZH_LS DY ON DY.BDCDYID=DJDY.BDCDYID ");
			builder.append("WHERE DJDY.BDCDYLX='04' AND DJDY.LY IN ('02','03') ");
			builder.append("UNION ");
			builder.append("SELECT DY.BDCDYH BDCDYH,DJDY.XMBH FROM BDCK.BDCS_DJDY_GZ DJDY LEFT JOIN BDCK.BDCS_SLLM_GZ DY ON DY.BDCDYID=DJDY.BDCDYID ");
			builder.append("WHERE DJDY.BDCDYLX='05' AND DJDY.LY='01' ");
			builder.append("UNION ");
			builder.append("SELECT DY.BDCDYH BDCDYH,DJDY.XMBH FROM BDCK.BDCS_DJDY_GZ DJDY LEFT JOIN BDCK.BDCS_SLLM_LS DY ON DY.BDCDYID=DJDY.BDCDYID ");
			builder.append("WHERE DJDY.BDCDYLX='05' AND DJDY.LY IN ('02','03') ");
			builder.append(") DYINFO ");
			builder.append("WHERE DYINFO.XMBH=XMXX.XMBH AND DYINFO.BDCDYH LIKE '%"
					+ bdcdyh + "%')");
		}
		builder.append(" AND YWLSH IS NOT NULL");
		List<Map> list = baseCommonDao.getDataListByFullSql(builder.toString());
		List<String> list_ywlsh = new ArrayList<String>();
		if (list != null && list.size() > 0) {
			for (Map m : list) {
				String ywlsh = StringHelper.formatObject(m.get("YWLSH"));
				if (!StringHelper.isEmpty(ywlsh) && !list_ywlsh.contains(ywlsh)) {
					list_ywlsh.add(ywlsh);
				}
			}
		}
		return StringHelper.formatList(list_ywlsh, ",");
	}
	
	/**
	 * 更正和预告登记的申请人页面，新增、更新或删除义务人时，把所有义务人更新到FSQL_GZ的YWR字段
	 * 
	 * @author:heks
	 * @date 2017-07-11
	 * @param xmbh
	 */
	public void updateFsqlYWR(String xmbh){
				
		if(xmbh == null || xmbh == "")
			return;
		
		BDCS_XMXX xmxx = baseCommonDao.get(BDCS_XMXX.class, xmbh);		
		if(DJLX.YGDJ.Value.equals(xmxx.getDJLX())||DJLX.GZDJ.Value.equals(xmxx.getDJLX())){	
			
			List<BDCS_QL_GZ> qlLists = baseCommonDao.getDataList(BDCS_QL_GZ.class, "XMBH='"+xmbh+"' AND QLLX NOT IN('23','99','98')");
			List<BDCS_SQR> ywrLists = baseCommonDao.getDataList(BDCS_SQR.class, "XMBH='"+xmbh+"' AND SQRLB='"+SQRLB.YF.Value+"'");
			
			if(qlLists != null && qlLists.size()>0){										
				List<String> fsqlid = new ArrayList<String>();
				List<String> ywrmc = new ArrayList<String>();
				StringBuilder ywr = new StringBuilder();				
				if(ywrLists == null || ywrLists.size()<=0)
					ywr.append("");
				else
					for (int i = 0; i < ywrLists.size(); i++) {
						if(!StringHelper.isEmpty(ywrLists.get(i).getSQRXM()) && !ywrmc.contains(ywrLists.get(i).getSQRXM()) ){
							ywrmc.add(ywrLists.get(i).getSQRXM());
							if (i != 0) {
								ywr.append("/");
							}
							ywr.append(ywrLists.get(i).getSQRXM());
						}
					
					}
				for(BDCS_QL_GZ ql_gz : qlLists){
					if(!StringHelper.isEmpty(ql_gz.getFSQLID()) && !fsqlid.contains(ql_gz.getFSQLID())){
						fsqlid.add(ql_gz.getFSQLID());
						BDCS_FSQL_GZ fsql_gz = baseCommonDao.get(BDCS_FSQL_GZ.class, ql_gz.getFSQLID());							
						fsql_gz.setYWR(ywr.toString());
						baseCommonDao.update(fsql_gz);
					}
				}
			}		
		}
			
		baseCommonDao.flush();
	}

	@Override
	public ResultMessage sfqr(HttpServletRequest request, String xmbh) {
		ResultMessage msg = new ResultMessage();
		msg.setSuccess("false");
		msg.setMsg("更新失败！");
		try {
			String sfqr = RequestHelper.getParam(request, "sfqr");
			BDCS_XMXX xmxx = baseCommonDao.get(BDCS_XMXX.class, xmbh);
			if (xmxx != null) {
				xmxx.setSFQR(sfqr);
				baseCommonDao.update(xmxx);
				baseCommonDao.flush();
				msg.setSuccess("true");
				msg.setMsg("更新成功！");
			} else {
				msg.setMsg("更新失败，未获取到对应项目信息！");
			}
		} catch (Exception e) {
			msg.setMsg("更新异常，请联系管理员！");
			e.printStackTrace();
		}
		return msg;
	}

	@Override
	public ResultMessage CheckSfqr(HttpServletRequest request) {
		ResultMessage msg = new ResultMessage();
		msg.setSuccess("false");
		msg.setMsg("检测到该项目未进行收费确认，请先到收费页面进行收费确认！");
		try {
			String xmbh = RequestHelper.getParam(request, "xmbh");
			if (StringHelper.isEmpty(xmbh)) {
				msg.setSuccess("false");
				msg.setMsg("未获取到项目编号！");
				return msg;
			}
			BDCS_XMXX xmxx = baseCommonDao.get(BDCS_XMXX.class, xmbh);
			if (xmxx != null && ("1".equals(xmxx.getSFQR()) || "2".equals(xmxx.getSFQR()))) {
				msg.setSuccess("true");
				msg.setMsg("该项目已进行收费确认！");
			} else {
				msg.setMsg("检测到该项目未进行收费确认，请先到收费页面进行收费确认！");
			}
		} catch (Exception e) {
			msg.setMsg("收费确认检验异常，请联系管理员！");
			e.printStackTrace();
		}
		return msg;
	}

}
