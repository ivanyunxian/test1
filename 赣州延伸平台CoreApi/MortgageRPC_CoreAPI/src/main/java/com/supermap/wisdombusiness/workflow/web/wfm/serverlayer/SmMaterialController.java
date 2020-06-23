/**
 * */

package com.supermap.wisdombusiness.workflow.web.wfm.serverlayer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.itextpdf.text.pdf.PdfStructTreeController.returnType;
import com.supermap.realestate.registration.util.ConfigHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.wisdombusiness.framework.model.User;
import com.supermap.wisdombusiness.web.ResultMessage;
import com.supermap.wisdombusiness.workflow.model.WFD_BOXDEF;
import com.supermap.wisdombusiness.workflow.model.WFI_BOXINST;
import com.supermap.wisdombusiness.workflow.model.WFI_DOSSIERTRANSFER;
import com.supermap.wisdombusiness.workflow.model.WFI_TRANSFERLIST;
import com.supermap.wisdombusiness.workflow.model.Wfd_Actdef;
import com.supermap.wisdombusiness.workflow.model.Wfi_MaterClass;
import com.supermap.wisdombusiness.workflow.model.Wfi_ProInst;
import com.supermap.wisdombusiness.workflow.service.common.Http;
import com.supermap.wisdombusiness.workflow.service.common.SmObjInfo;
import com.supermap.wisdombusiness.workflow.service.common.WFConst;
import com.supermap.wisdombusiness.workflow.service.wfi.SmProInst;
import com.supermap.wisdombusiness.workflow.service.wfi.SmStaff;
import com.supermap.wisdombusiness.workflow.service.wfm.SmMaterialService;
import com.supermap.wisdombusiness.workflow.service.wfm.SmProMaterService;
import com.supermap.wisdombusiness.workflow.util.Message;
import com.supermap.wisdombusiness.workflow.util.Page;

@Controller
@RequestMapping("/material")
public class SmMaterialController {
	@Autowired
	private SmProMaterService smProMaterService;
	@Autowired
	private SmMaterialService smMaterialService;
	@Autowired
	private SmStaff smStaff;

	@Autowired
	private SmProInst smProInst;
	/*
	 * 档案移交
	 */
	@RequestMapping(value = "/transfer", method = RequestMethod.GET)
	public String DossierTransfer(Model model) {
		String qhdm=ConfigHelper.getNameByValue("XZQHDM");
		if (!StringHelper.isEmpty(qhdm)&&qhdm.equals("450500")) {
			//北海档案移交
			return "/workflow/material/transfer_BeHai";
		}
		return "/workflow/material/transfer";
	}
	/*
	 * 档案移交
	 */
	@RequestMapping(value = "/transfer_hk", method = RequestMethod.GET)
	public String DossierTransferHk(Model model) {
		return "/workflow/material/transfer_hk";
	}
//	/*
//	 * 档案接收
//	 */
	@RequestMapping(value = "/receive", method = RequestMethod.GET)
	public String DossierReceive(Model model) {
		return "/workflow/material/receive";
	}

	/**
	 * 条码归档
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/codedossier", method = RequestMethod.GET)
	public String codedossier(Model model) {
		return "/workflow/material/codedossier";
	}

	@RequestMapping(value = "/print", method = RequestMethod.GET)
	public String print(Model model) {
		return "/workflow/material/print";
	}
	/*
	 * 档案移交
	 */
	@RequestMapping(value = "/accept", method = RequestMethod.GET)
	public String Accept(Model model) {
		return "/workflow/material/accept";
	}
	/**
	 * 批量输出
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/batchoutput", method = RequestMethod.GET)
	public String batchoutput(Model model) {
		return "/workflow/material/batchoutput";
	}
	

	
	/**
	 * 获取个人或是部门的档案移交记录
	 * 
	 * @param type
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/transfer/list/{type}", method = RequestMethod.POST)
	@ResponseBody
	public Message GetDossierTransfer(@PathVariable int type,
			HttpServletRequest request, HttpServletResponse response) {
		String status = request.getParameter("status");
		String pagesize = request.getParameter("pagesize");
		String pageindex = request.getParameter("pageindex");
		String key = request.getParameter("key");
		String actinstname = request.getParameter("actinstname");
		String staffs = request.getParameter("staffs");
		String proname = request.getParameter("proname");
		String starttime = request.getParameter("starttime");
		String endtime = request.getParameter("endtime");
		String staffid = smStaff.getCurrentWorkStaffID();
		String deptname=request.getParameter("deptname");
		String areas=request.getParameter("areas");
		String hk=request.getParameter("hk");
		String lshstart=request.getParameter("lshstart");
		String lshend=request.getParameter("lshend");
		String sorttype =request.getParameter("sorttype");
		int page = 1;
		int size = 10;
		if (pagesize != null && !pagesize.equals("")) {
			page = Integer.parseInt(pageindex);
			size = Integer.parseInt(pagesize);
		}
		
		return smMaterialService.GetFileProject(status, staffid, page, size,
				key, actinstname, staffs, proname, starttime, endtime,deptname,areas,hk,lshstart,lshend,null,sorttype);
	}

	@RequestMapping(value = "/transfer/record/{type}", method = RequestMethod.POST)
	@ResponseBody
	public Page GetTransferRecord(@PathVariable int type,
			HttpServletRequest request, HttpServletResponse response) {
		String staffid = smStaff.getCurrentWorkStaffID();
		String pageindex = request.getParameter("pageindex");
		String pagesize = request.getParameter("pagesize");
//		String dept = new String(request.getParameter("deptn").getBytes("iso-8859-1"),"utf-8");
		String dept = request.getParameter("deptn");
		String searchKey = request.getParameter("key");
		String start = request.getParameter("start");
		String end = request.getParameter("end");
		return smMaterialService.GetTransferRecord(staffid,
				Integer.parseInt(pageindex), Integer.parseInt(pagesize), type,dept,start,end,searchKey);
	}

	/**
	 * 获取接收档案列表
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/receive/list/{type}", method = RequestMethod.GET)
	@ResponseBody
	public Message GetReceiveDossier(@PathVariable int type,
			HttpServletRequest request, HttpServletResponse response) {
		String staffid = smStaff.getCurrentWorkStaffID();
		String status = request.getParameter("status");
		// WFConst.MateralStatus.NoReceive.value
		String pagesize = request.getParameter("pagesize");
		String pageindex = request.getParameter("pageindex");
		String prolsh= request.getParameter("prolsh");
		String start= request.getParameter("start");
		String end= request.getParameter("end");
		return smMaterialService.GetReceiveDossier(staffid, type, status,prolsh,start,end,
				Integer.parseInt(pagesize), Integer.parseInt(pageindex));
	}

	/**
	 * 移交档案
	 * 
	 * @param type
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/transfer/project/{type}", method = RequestMethod.POST)
	@ResponseBody
	public String TransferMaterial(@PathVariable int type,
			HttpServletRequest request, HttpServletResponse response) {
		User staff = smStaff.getCurrentWorkStaff();
		if (staff != null) {
			String fileNumber = request.getParameter("filenumber");
			String projectname = request.getParameter("projectname");
			String actinstname = request.getParameter("actinstname");
			String actinstid = request.getParameter("actinstid");

			return smMaterialService.TransferMaterial(staff, fileNumber,
					projectname, actinstid, actinstname, type);
		}
		return null;

	}
	/**
	 * 移交接收
	 * 
	 * @param type
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/accept/project/{type}", method = RequestMethod.POST)
	@ResponseBody
	public boolean Accept(@PathVariable int type,
			HttpServletRequest request, HttpServletResponse response) {
		User staff = smStaff.getCurrentWorkStaff();
		if (staff != null) {
			String fileNumber = request.getParameter("filenumber");
			String projectname = request.getParameter("projectname");
			String actinstname = request.getParameter("actinstname");
			String actinstid = request.getParameter("actinstid");
			return smMaterialService.AcceptMaterial(staff, fileNumber,
					projectname, actinstid, actinstname, type);
		}
		return false;

	}
    /**
     * 批量归档
     * @param request
     * @param response
     * @return
     */
	@RequestMapping(value = "/transfer/dossier", method = RequestMethod.POST)
	@ResponseBody
	public SmObjInfo TransferDossier(HttpServletRequest request,
			HttpServletResponse response) {
		User staff = smStaff.getCurrentWorkStaff();
		if (staff != null) {
			String lshs = request.getParameter("lshs");
			String actinstids = request.getParameter("actinstid");
			String cb=request.getParameter("cb");

			return smMaterialService
					.TransferDossier(staff, actinstids, lshs,cb, 2);
		}
		return null;

	}
	
	  /**
         *旧归档数据归档到2.0档案库
     * @param request
     * @param response
     * @return
     */
	@RequestMapping(value = "/transfer/dossier/djgd", method = RequestMethod.POST)
	@ResponseBody
	public SmObjInfo TransferDossierDjgd(HttpServletRequest request,
			HttpServletResponse response) {
		User staff = smStaff.getCurrentWorkStaff();
		if (staff != null) {
			return smMaterialService.dossierForDjgd(staff,2);
		}
		return null;

	}
	@RequestMapping(value = "/transfer/extract", method = RequestMethod.POST)
	@ResponseBody
	public SmObjInfo extract(HttpServletRequest request,
			HttpServletResponse response){
		String ddh=request.getParameter("ddh");
		
		
		return null;
	}
	
	
	
	/**
	 * 获取某次移交的所有记录
	 * 
	 * @param id
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/transfer/record/list/{id}", method = RequestMethod.GET)
	@ResponseBody
	public List<Map> GetTransferList(@PathVariable String id,
			HttpServletRequest request, HttpServletResponse response) {
		return smMaterialService.GetTransferList(id);

	}
	
	/**
	 * 撤销一次接收记录
	 * 
	 * @param id
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/transfer/revoke/{id}", method = RequestMethod.GET)
	@ResponseBody
	public SmObjInfo revokeTransferList(@PathVariable String id,
			HttpServletRequest request, HttpServletResponse response) {
		return smMaterialService.revokeTransferList(id);

	}
	/**
	 * 海口  获取某次移交的所有记录
	 * 
	 * @param id
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/transfer/record/list_hk/{id}", method = RequestMethod.GET)
	@ResponseBody
	public List<Map> GetTransList(@PathVariable String id,
			HttpServletRequest request, HttpServletResponse response) {
		return smMaterialService.GetTransList(id);

	}

	/**
	 * 接收档案
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/receive/{id}", method = RequestMethod.POST)
	@ResponseBody
	public SmObjInfo ReceiveMaterial(@PathVariable String id,
			HttpServletRequest request, HttpServletResponse response) {
		User staff = smStaff.getCurrentWorkStaff();
		return smMaterialService.ReceiveMaterial(staff, id);

	}

	/**
	 * 验证是否有资料
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/validate", method = RequestMethod.GET)
	@ResponseBody
	public List<String> ValidateMaterial(HttpServletRequest request,
			HttpServletResponse response) {
		String file_numbers = request.getParameter("filenumbers");
		String staffid = smStaff.getCurrentWorkStaffID();
		return smMaterialService.ValidateMaterial(file_numbers, staffid);

	}

	// 验证流程是否有收件资料
	@RequestMapping(value = "/process/{file_number}", method = RequestMethod.GET)
	@ResponseBody
	public Map GetMaterialProcess(@PathVariable String file_number,
			HttpServletRequest request, HttpServletResponse response) {
		return smMaterialService.GetMaterialProcess(file_number);

	}

	// 该表资料的目录
	@RequestMapping(value = "/move/{srcfile}/to/{targetfolder}", method = RequestMethod.POST)
	@ResponseBody
	public boolean MoveMaterial(@PathVariable String srcfile,
			@PathVariable String targetfolder, HttpServletRequest request,
			HttpServletResponse response) {
		return smMaterialService.MoveMaterial(srcfile, targetfolder);

	}

	// 该表资料的目录
	@RequestMapping(value = "getdata/dossierbydhh", method = RequestMethod.GET)
	@ResponseBody
	public List<Message> GetProjectByDHH(
			HttpServletRequest request, HttpServletResponse response) {
		String ddh=request.getParameter("ddh");
		return smMaterialService.GetProjectByDHH(ddh);
	}
	//维护档案号
	@RequestMapping(value = "transferlist/adddah/{id}", method = RequestMethod.POST)
	@ResponseBody
	public boolean setTransferListDAH(@PathVariable String id,
			HttpServletRequest request, HttpServletResponse response) {
		String dah=request.getParameter("dah");
		String ajid=request.getParameter("ajid");
		String actinstid=request.getParameter("actinstid");
		String olddah=request.getParameter("olddah");
		String qlr=request.getParameter("qlr");
		return smMaterialService.isGDSuccess(id,dah,ajid,actinstid,olddah,qlr);
	}
	//记录归档是否提取了资料完成
	@RequestMapping(value = "transferlist/fj/{file_number}", method = RequestMethod.POST)
	@ResponseBody
	public boolean setTransferFJ(@PathVariable String file_number,
			HttpServletRequest request, HttpServletResponse response) {
		return smMaterialService.setTransferFJ(file_number);
	}
	
	//按照顺序获取
	@RequestMapping(value = "list/ddh/order", method = RequestMethod.GET)
	@ResponseBody
	public Message getOrderList(
			HttpServletRequest request, HttpServletResponse response) {
		String ddh=request.getParameter("ddh");
		return smMaterialService.getOrderList(ddh);
	}
	
	//获取已经归档的项目
	@RequestMapping(value = "havedossier/project", method = RequestMethod.GET)
	@ResponseBody
	public List<Map> getHaveDossier(
			HttpServletRequest request, HttpServletResponse response) {
		return smMaterialService.getHaveDossier();
	}
	
	@RequestMapping(value = "/virtualboxdefine", method = RequestMethod.GET)
	public String VirtualBoxDef(Model model){
		return "/workflow/material/virtualboxdefine";
	}
	
	/**
	 * 查看柜子中文件的信息
	 * @param pageSize
	 * @param pageIndex
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="boxdef/files/{pageSize}/{pageIndex}",method=RequestMethod.GET)
	@ResponseBody
	public Page getBoxFiles(@PathVariable String pageSize,
			@PathVariable String pageIndex,HttpServletRequest request,HttpServletResponse response){
		String boxDef_Id=request.getParameter("boxDef_Id");
		return smMaterialService.getBoxFiles(boxDef_Id,pageSize,pageIndex);
	}
	
	/**
	 * 取出盒子中的某个文件
	 * @param boxDefId
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="boxinst/pickupfile",method=RequestMethod.GET)
	@ResponseBody
	public SmObjInfo pickUpFiles(HttpServletRequest request,HttpServletResponse response){
		String fileId = request.getParameter("fileId");
		return smMaterialService.pickUpFiles(fileId);
	}
	/**
	 * 
	 * @Title: getBoxList   
	 * @Description: 获取虚拟柜列表-分页
	 * @author: 郭浩龙  
	 * @date:   2016年2月26日 下午3:36:55    
	 * @param: @param pageSize
	 * @param: @param pageIndex
	 * @param: @param request
	 * @param: @param response
	 * @param: @return      
	 * @return: List<WFD_BOXDEF>      
	 * @throws
	 */
	@RequestMapping(value="boxdef/list/{pageSize}/{pageIndex}",method=RequestMethod.GET)
	@ResponseBody
	public Page getBoxDefList(@PathVariable String pageSize,
			@PathVariable String pageIndex,HttpServletRequest request,HttpServletResponse response){
		return smMaterialService.getBoxDefList(pageSize,pageIndex);
	}
	
	/**
	 * 
	 * @Title: getBoxDefByDefId   
	 * @Description: 根据DefId获取虚拟柜内所有子元素-分页
	 * @author: 郭浩龙  
	 * @date:   2016年2月26日 下午11:01:57    
	 * @param: @param pageSize
	 * @param: @param pageIndex
	 * @param: @param request
	 * @param: @param response
	 * @param: @return      
	 * @return: Page      
	 * @throws
	 */
	@RequestMapping(value="boxdef/content/{pageSize}/{pageIndex}",method=RequestMethod.GET)
	@ResponseBody
	public Page getBoxDefByDefId(@PathVariable String pageSize,
			@PathVariable String pageIndex,HttpServletRequest request,HttpServletResponse response){
		String boxDef_Id=request.getParameter("boxDef_Id");
		return smMaterialService.getBoxDefByDefId(boxDef_Id,pageSize,pageIndex);
	}
	
	/**
	 * 
	 * @Title: addOrUpdateBoxDef   
	 * @Description: 虚拟柜信息维护-单条信息添加或更新
	 * @author: 郭浩龙  
	 * @date:   2016年2月26日 下午9:04:56    
	 * @param: @param request
	 * @param: @param response
	 * @param: @return      
	 * @return: SmObjInfo      
	 * @throws
	 */
	@RequestMapping(value="boxdef/addorupdate",method=RequestMethod.POST)
	@ResponseBody
	public SmObjInfo addOrUpdateBoxDef(HttpServletRequest request,HttpServletResponse response){
		String boxDef_Id=request.getParameter("boxDef_Id");
		String boxDef_Pid=request.getParameter("boxDef_Pid");
		String boxName=request.getParameter("boxName");
		String sort=request.getParameter("sort");
		System.out.println(sort);
		String lx=request.getParameter("lx");
		String boxBm=request.getParameter("boxBm");
		String bz=request.getParameter("bz");
		WFD_BOXDEF boxDef = new WFD_BOXDEF();
		if(boxDef_Id!=null&&!boxDef_Id.equals("")){
			boxDef = smMaterialService.getBoxDefById(boxDef_Id);
			boxDef.setBoxdef_Pid(boxDef_Pid);
			boxDef.setBox_Bm(boxBm);
			boxDef.setBox_Name(boxName);
			if(sort!=null&&!sort.equals("")){
				try {
					boxDef.setSort(Integer.parseInt(sort));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			boxDef.setBz(bz);
			boxDef.setLx(lx);
			return smMaterialService.updateBoxDef(boxDef);
		}else{
			boxDef.setBoxdef_Pid(boxDef_Pid);
			boxDef.setBox_Bm(boxBm);
			boxDef.setBox_Name(boxName);
			if(sort!=null&&!sort.equals("")){
				try {
					boxDef.setSort(Integer.parseInt(sort));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			boxDef.setBz(bz);
			boxDef.setLx(lx);
			return smMaterialService.addBoxDef(boxDef);
		}
	}
	
	/**
	 * 
	 * @Title: multipleAddBoxDef   
	 * @Description: 虚拟柜信息维护-批量添加
	 * @author: 郭浩龙  
	 * @date:   2016年2月26日 下午9:05:35    
	 * @param: @param request
	 * @param: @param response
	 * @param: @return      
	 * @return: SmObjInfo      
	 * @throws
	 */
	@RequestMapping(value="boxdef/add/multiple",method=RequestMethod.POST)
	@ResponseBody
	public SmObjInfo multipleAddBoxDef(HttpServletRequest request,HttpServletResponse response){
		String boxName=request.getParameter("box_Name");
		String boxBM=request.getParameter("box_Bm");
		String sort=request.getParameter("sort");
		String rowNum=request.getParameter("rowNum");
		String columnNum=request.getParameter("columnNum");
		return smMaterialService.multipleAddBoxDef(boxName,boxBM,sort,rowNum,columnNum);
	}
	
	/**
	 * 
	 * @Title: deleteBoxDef   
	 * @Description: 虚拟柜信息维护-删除
	 * @author: 郭浩龙  
	 * @date:   2016年2月26日 下午5:13:07    
	 * @param: @param boxDefId
	 * @param: @param request
	 * @param: @param response
	 * @param: @return      
	 * @return: SmObjInfo      
	 * @throws
	 */
	@RequestMapping(value="boxdef/delete/{boxDefId}",method=RequestMethod.GET)
	@ResponseBody
	public SmObjInfo deleteBoxDef(@PathVariable String boxDefId,
			HttpServletRequest request,HttpServletResponse response){
		return smMaterialService.deleteBoxDef(boxDefId);
	}
	
	/**                                                     
	 *                                                      
	 * @Title: addBoxInst                                   
	 * @Description: 入柜操作-增加虚拟柜实例                
	 * @author: 郭浩龙                                      
	 * @date:   2016年2月26日 下午4:53:19                   
	 * @param: @param boxDefId                              
	 * @param: @param lsh                                   
	 * @param: @param request                               
	 * @param: @param response                              
	 * @param: @return                                      
	 * @return: SmObjInfo                                   
	 * @throws                                              
	 */
	@RequestMapping(value="boxinst/add/{boxDefId}/{lsh}",method=RequestMethod.GET)
	@ResponseBody
	public SmObjInfo addBoxInst(@PathVariable String boxDefId,
			@PathVariable String lsh,HttpServletRequest request,HttpServletResponse response){
		User staff = smStaff.getCurrentWorkStaff();
		return smMaterialService.addBoxInst(staff,boxDefId,lsh);
	}
	
	/**
	 * 
	 * @Title: searchBoxInst   
	 * @Description: 根据流水号查询虚拟柜信息
	 * @author: 郭浩龙  
	 * @date:   2016年2月26日 下午6:09:43    
	 * @param: @param lsh
	 * @param: @param request
	 * @param: @param response
	 * @param: @return      
	 * @return: List<WFI_BOXINST>      
	 * @throws
	 */
	@RequestMapping(value="boxinst/search/{lsh}",method=RequestMethod.GET)
	@ResponseBody
	public List<WFI_BOXINST> searchBoxInst(@PathVariable String lsh,HttpServletRequest request,
			HttpServletResponse response){
		return smMaterialService.searchBoxInst(lsh);
	}
	
	//通过多个调档单获取案卷信息
	@RequestMapping(value="archives/ddd",method=RequestMethod.GET)
	@ResponseBody
	public List<Map> getArchivesByDDDs(HttpServletRequest request,
			HttpServletResponse response){
		String ddds=request.getParameter("DDDs");
		String orderField=request.getParameter("orderfield");
		String order=request.getParameter("order");
		return smMaterialService.getArchivesByDDDs(ddds,orderField,order);
		
	}
	/**
	 * 获取所有的部门
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/all/deptname",method=RequestMethod.GET)
	@ResponseBody
	public List<Map> getallDepts(HttpServletRequest request,HttpServletResponse response){
		return smMaterialService.getallDepts();
	}
	//通过AJID 和流水号更新调档信息的档案号
	@RequestMapping(value="/matain/from/archives/ajh",method=RequestMethod.POST)
	@ResponseBody
	public boolean MatainAJH(HttpServletRequest request,HttpServletResponse response){
		String ajid=request.getParameter("AJID");
		String lsh=request.getParameter("LSH");
		String dah=request.getParameter("AJH");
		return smMaterialService.MatainAJH(ajid,lsh,dah);
	}
	
	
	/**
	 * 档案移交导出excel
	 * @param request
	 * @param response
	 * @return
	 */
	
		@RequestMapping(value = "/transferDownload", method = RequestMethod.POST)
		public @ResponseBody String QJCXDownload( HttpServletRequest request, HttpServletResponse response) throws Exception{
		 String type = request.getParameter("type");
		 String qhdm=ConfigHelper.getNameByValue("XZQHDM");
		 String sorttype=request.getParameter("lshend");
		 Message m=null;
		 String hk=request.getParameter("hk");
		 if(type!=null&&type!=""){
			 String actinstids = request.getParameter("actinstid"); 
			 if(hk!=null){
				 m=smMaterialService.GetTransferInfo(actinstids);   
			 }else{
				 m=smMaterialService.GetFileProject(null, smStaff.getCurrentWorkStaffID(), 1, 999,
							"", "", null, null, null, null,null,null,null,null,null,actinstids,sorttype);
			 }            
		 }else{                                                             
			 String status = request.getParameter("status");
				String pagesize = request.getParameter("pagesize");
				String pageindex = request.getParameter("pageindex");
				String key = request.getParameter("key");
				String actinstname = request.getParameter("actinstname");
				String staffs = request.getParameter("staffs");
				String proname = request.getParameter("proname");
				String starttime = request.getParameter("starttime");
				String endtime = request.getParameter("endtime");
				String staffid = smStaff.getCurrentWorkStaffID();
				String deptname=request.getParameter("deptname");
				String areas=request.getParameter("areas");
				String lshstart=request.getParameter("lshstart");
				String lshend=request.getParameter("lshend");
			    int page = 1;                                                         
			    int size = 10;                                                        
			 m =  smMaterialService.GetFileProjectNotPage(status, staffid, page, size,
						key, actinstname, staffs, proname, starttime, endtime,deptname,areas,hk,lshstart,lshend,null,sorttype);
		 }
			List<Map<String,String>> djdys = null;
			if (m !=null && m.getRows()!=null){
				djdys=(List<Map<String,String>>)m.getRows();
			}
			if(djdys != null && djdys.size() > 0){
				LinkedHashMap<String,String> map= new LinkedHashMap<String,String>();

				ArrayList<String> colname = new ArrayList<String>();
				//读取EXCEL文件字段
				HSSFWorkbook wb = null;
				FileInputStream fileInputStream = new FileInputStream(request.getRealPath("/WEB-INF/jsp/wjmb/transfer.xls"));
				if (!StringHelper.isEmpty(qhdm)&&qhdm.equals("450500")) {
					fileInputStream = new FileInputStream(request.getRealPath("/WEB-INF/jsp/wjmb/transferBeiHai.xls"));
				}
				wb = new HSSFWorkbook(fileInputStream);
				HSSFSheet hssfsheet = wb.getSheetAt(0);
				HSSFRow hssfRow = hssfsheet.getRow(2);
				if(null!=hssfRow){
				for(int i=0;i<hssfRow.getLastCellNum();i++){
					HSSFCell hSSFCell = hssfRow.getCell(i);
					if(hSSFCell==null){
						continue;
					}
					System.out.println(hSSFCell.toString());
					if(null!=hssfRow&&!"".equals(hSSFCell.toString())){
						colname.add(hSSFCell.toString());
					}
					}
				}
				String basePath = request.getRealPath("/") + "\\resources\\PDF";
				String url = "";
				String outpath ="";
				String tmpFullName ="";
				if(hk!=null){
					 url = request.getContextPath() + "\\resources\\PDF\\tmp\\transferhk.xls";
					 outpath = basePath + "\\tmp\\transferhk.xls";
					 tmpFullName = request.getRealPath("/WEB-INF/jsp/wjmb/transferhk.xls");
					 map.put("受理编号", "PROLSH");
					 map.put("不动产单元坐落", "BDCDYZL");
					 map.put("权证号", "BDCQZH");
					 map.put("申请人", "USERNAME");
					 map.put("申请人联系方式", "TEL");
					 map.put("行政区", "AREA");
					 map.put("小区名称", "ADDRESS");
					 map.put("业务类别", "PROTYPE");
					 map.put("受理人", "STAFF_NAME");
				}else if (!StringHelper.isEmpty(qhdm)&&qhdm.equals("450500")) {
					url = request.getContextPath() + "\\resources\\PDF\\tmp\\transferBeiHai.xls";
					 outpath = basePath + "\\tmp\\transferBeiHai.xls";
					 tmpFullName = request.getRealPath("/WEB-INF/jsp/wjmb/transferBeiHai.xls");
					 map.put("受理编号", "PROLSH");
					 map.put("登记原因", "DJYY");
					 map.put("业务类别", "PRODEF_NAME");  
					 map.put("项目名称", "PROJECT_NAME"); 
					 map.put("活动类型", "ACTINST_NAME");
					 map.put("坐落", "ZL");
					 map.put("权利人", "QLR");
					 map.put("义务人", "YWR");
//					 map.put("申请人", "SQR");
					 map.put("权证号", "BDCQZH");
					 map.put("登簿时间","DJSJ");
					 map.put("活动结束时间","ACTINST_END");
					 map.put("受理人员", "ACCEPTOR");
					 if(null!=colname&&colname.size()>0){ //判断EXCEL模板是否修改为数据库字段
						 Set keySet = map.keySet();	
						 LinkedHashMap<String,String> transfermap= new LinkedHashMap<String,String>();
						 for(String name:colname){
							 name = name.trim();
							 transfermap.put(name, name);
						 }
						 map = transfermap;
					 }
				}else{
					url = request.getContextPath() + "\\resources\\PDF\\tmp\\transfer.xls";
					 outpath = basePath + "\\tmp\\transfer.xls";
					 tmpFullName = request.getRealPath("/WEB-INF/jsp/wjmb/transfer.xls");
					 map.put("受理编号", "PROLSH");
					 map.put("业务类别", "PRODEF_NAME");  
					 map.put("项目名称", "PROJECT_NAME"); 
					 map.put("活动类型", "ACTINST_NAME");
					 map.put("坐落", "ZL");
					 map.put("权利人", "QLR");
					 map.put("义务人", "YWR");
//					 map.put("申请人", "SQR");
					 map.put("权证号", "BDCQZH");
					 map.put("活动结束时间","ACTINST_END");
					 map.put("受理人员", "ACCEPTOR");
					 if(null!=colname&&colname.size()>0){ //判断EXCEL模板是否修改为数据库字段
						 Set keySet = map.keySet();	
						 LinkedHashMap<String,String> transfermap= new LinkedHashMap<String,String>();
						 for(String name:colname){
							 name = name.trim();
							 transfermap.put(name, name);
						 }
						 map = transfermap;
					 }

				}
				return smMaterialService.excelDownload(url, outpath, tmpFullName, map, djdys);
			}
			return null;
		}
	 /**
	  * 
	  * 档案移交记录导出Excel
	  */
		@RequestMapping(value = "/downrecode", method = RequestMethod.POST)
		@ResponseBody
		public  String downRecode( HttpServletRequest request, HttpServletResponse response) throws Exception{
			String transferids = request.getParameter("transferids");
			List<Map<String,String>> list = new ArrayList<Map<String,String>>();
			if(!StringHelper.isEmpty(transferids)){
				String[] transArray = transferids.split(",");
				for(int i=0,j=transArray.length;i<j;i++){
					List<Map> transList = smMaterialService.GetTransferList(transArray[i]);
					if(null!=transList&&transList.size()>0){
						for(Map transfer : transList){
							list.add(transfer);
						}
					}
				}
				if(null!=list&&list.size()>0){
					//List<Map<String,String>> djdys = (List<Map<String,String>>)list.getRows();
					String url = request.getContextPath() + "\\resources\\PDF\\tmp\\transfer.xls";
					String outpath = request.getRealPath("/") + "\\resources\\PDF\\tmp\\transfer.xls";
					String tmpFullName = request.getRealPath("/WEB-INF/jsp/wjmb/transfer.xls");
					LinkedHashMap<String,String> map= new LinkedHashMap<String,String>();
					//读取excel字段
					HSSFWorkbook wb = null;
					FileInputStream fileInputStream = new FileInputStream(request.getRealPath("/WEB-INF/jsp/wjmb/transfer.xls"));
					wb = new HSSFWorkbook(fileInputStream);
					HSSFSheet hssfsheet = wb.getSheetAt(0);
					HSSFRow hssfRow = hssfsheet.getRow(2);
					if (null != hssfRow) {
						for (int i = 0; i < hssfRow.getLastCellNum(); i++) {
							HSSFCell hSSFCell = hssfRow.getCell(i);
							if (hSSFCell == null) {
								continue;
							}
							if (null != hssfRow && !"".equals(hSSFCell.toString())) {
								String key = hSSFCell.toString();
								map.put(key, key);
							}
						}
					}
					if(null==map||map.size()<1){
						map.put("受理编号", "PROLSH");
						 map.put("业务类别", "PRODEF_NAME");  
						 map.put("项目名称", "PROJECT_NAME"); 
						 map.put("活动类型", "ACTINST_NAME");
						 map.put("坐落", "ZL");
						 map.put("申请人", "SQR");
						 map.put("权证号", "BDCQZH");
						 map.put("活动结束时间","ACTINST_END");
						 map.put("受理人员", "SLRY");
					}
					return smMaterialService.excelDownload(url, outpath, tmpFullName, map, list);
				}
			}
			return null;
			
			
		}
		
	 /**
	  * 批量上传资料：
	  * @date   2016年11月9日 下午5:30:08
	  * @author JHX
	  * @param file
	  * @param request
	  * @param response
	  * @return
	  */
	 @RequestMapping(value="/upload/master/zip" , method = RequestMethod.POST)
	 @ResponseBody
	 public ResultMessage uploadMasterZip(@RequestParam(value = "file", required = false) MultipartFile file,
			 HttpServletRequest request,HttpServletResponse response,Object command){
		 ResultMessage res = new ResultMessage();
		 String proisntid = request.getParameter("proinstid");
			if(proisntid!=null&&!proisntid.equals("")&&file!=null){
				List<Map> uploadResponse = smMaterialService.uploadMasterZipFiles(request, file);
				if(uploadResponse!=null&&uploadResponse.size()>0){
					smMaterialService.afterUploadMasterZip(uploadResponse,proisntid);
					res.setSuccess("SUCCESS");
					res.setMsg("上传成功");
			    }
			}else{
				res.setSuccess("ERROR");
				res.setMsg("上传失败!上传文件为空");
			}
		    return res;
	 }
	 /**
	  * 海口获取当前捆号
	  * @author dff
	  * @date 2017-2-20
	  * @param filenumber
	  * @return String 当前捆号
	  */
	 @RequestMapping(value="/getcurrentkh/{file_number}" , method = RequestMethod.GET)
	 @ResponseBody
	 public String getCurrentKh( @PathVariable String file_number,HttpServletRequest request,HttpServletResponse response){
		return smMaterialService.getCurrentKhByFilenumber(file_number);
		 
	 }
	 
	 /**
	  *期房转现房抽取预告登记资料
	  */
	 
	 @RequestMapping(value="/extract/{file_number}" , method = RequestMethod.POST)
	 @ResponseBody
	 public SmObjInfo extractMater (@PathVariable String file_number,HttpServletRequest request,HttpServletResponse response){
		 return smMaterialService.extractMater(file_number);
	 }
	 
	 /**
	  *添加收件资料目录
	  */
	 @RequestMapping(value="/floderclass/add" , method = RequestMethod.POST)
	 @ResponseBody
	 public String addFloderClass (Wfi_MaterClass materClass,HttpServletRequest request,HttpServletResponse response){
		 return smMaterialService.addMaterClass(materClass);
	 }
	 
	 /**
	  * 移除收件目录
	  */
		 @RequestMapping(value="/floderclass/remove/{materClassId}" , method = RequestMethod.POST)
		 @ResponseBody
		 public String removeFloderClass (@PathVariable String materClassId,HttpServletRequest request,HttpServletResponse response){
			 return smMaterialService.removeFloderClass(materClassId);
		 }
	
	/**
	 * 海口导出移交记录
	 * @param materClass
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/downrecodehk" , method = RequestMethod.POST)
	@ResponseBody
	public String downRecode (Wfi_MaterClass materClass,HttpServletRequest request,HttpServletResponse response) throws Exception{
		String transferids = request.getParameter("transferids");
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		String url = request.getContextPath() + "\\resources\\PDF\\tmp\\transrecode.xls";
		String outpath = request.getRealPath("/") + "\\resources\\PDF\\tmp\\transrecode.xls";
		String tmpFullName = request.getRealPath("/WEB-INF/jsp/wjmb/transrecode.xls");
		FileOutputStream outstream = new FileOutputStream(outpath); 
	    InputStream input = new FileInputStream(tmpFullName);
		HSSFWorkbook  wb = null;// 定义一个新的工作簿
		wb = new HSSFWorkbook(input);
		Sheet sheet = wb.getSheetAt(0);
		//创建表头样式
		HSSFCellStyle cellStyle = wb.createCellStyle();
		cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		HSSFFont font = wb.createFont();    
		font.setFontName("黑体");    
		font.setFontHeightInPoints((short) 18);//设置字体大小 
		cellStyle.setFont(font);
		
		if(!StringHelper.isEmpty(transferids)){
			String[] transArray = transferids.split(",");
			int row = 0;
			for(int i=0,j=transArray.length;i<j;i++){
				List<Map> transList = smMaterialService.GetTransferList(transArray[i]);
				if(null!=transList&&transList.size()>0){
					String transfercode = transList.get(0).get("TRANSFERCODE").toString();
					//合并单元格
					 CellRangeAddress cra=new CellRangeAddress(row, row+1, 0, 4); 
					 sheet.addMergedRegion(cra); 
					 Row crow = sheet.createRow(row);
					 Cell cell_1 = crow.createCell(0);
					 cell_1.setCellValue("档案移交电子表         捆号："+transfercode);
					 cell_1.setCellStyle(cellStyle);
					 row+=2;
					 //添加表头
					 Row crow1 = sheet.createRow(row);
					 Cell cell_2 = crow1.createCell(0);
					 cell_2.setCellValue("序号");
					 Cell cell_3 = crow1.createCell(1);
					 cell_3.setCellValue("业务流水号");
					 Cell cell_4 = crow1.createCell(2);
					 cell_4.setCellValue("业务类型");
					 Cell cell_5 = crow1.createCell(3);
					 cell_5.setCellValue("申请人名称");
					 Cell cell_6 = crow1.createCell(4);
					 cell_6.setCellValue("备注");
					 //添加每次移交数据
					 int index = 0;
					for(Map transfer : transList){
						Row crow2 = sheet.createRow(++row);
						Cell cell_7 = crow2.createCell(0);
						cell_7.setCellValue(++index);
						Cell cell_8 = crow2.createCell(1);
						cell_8.setCellValue(transfer.get("PROLSH").toString());
						Cell cell_9 = crow2.createCell(2);
						cell_9.setCellValue(transfer.get("PRODEF_NAME").toString());
						Cell cell_10 = crow2.createCell(3);
						cell_10.setCellValue(transfer.get("SQR").toString());
					}
				}
			}
		}
		
		 /*CellRangeAddress cra=new CellRangeAddress(0, 3, 3, 9);        
         
	        //在sheet里增加合并单元格  
	        sheet.addMergedRegion(cra);  
	         Row row = sheet.createRow(0);  
	        Cell cell_1 = row.createCell(3);
	        cell_1.setCellValue("When you're right , no one remembers, when you're wrong ,no one forgets .");  
	          
	        //cell 位置3-9被合并成一个单元格，不管你怎样创建第4个cell还是第5个cell…然后在写数据。都是无法写入的。  
	        Cell cell_2 = row.createCell(10);  
	          
	        cell_2.setCellValue("what's up ! ");  */
	        
	        wb.write(outstream); 
			 outstream.flush(); 
			 outstream.close();
	        
		return url;
	}
	
	/**
	 * 海口按捆接收资料
	 */
	@RequestMapping(value="/receivetransfer/{transferid}" , method = RequestMethod.POST)
	@ResponseBody
	public String receiveByPromater (@PathVariable String transferid,HttpServletRequest request,HttpServletResponse response) {
		User user = smStaff.getCurrentWorkStaff();
		List<WFI_TRANSFERLIST> transferList = smMaterialService.getTransferList(transferid);
		for(WFI_TRANSFERLIST transfer : transferList){
			smMaterialService.ReceiveMaterial(user, transfer.getTransferlistid()); 
		}
		return transferid;
	}

}
