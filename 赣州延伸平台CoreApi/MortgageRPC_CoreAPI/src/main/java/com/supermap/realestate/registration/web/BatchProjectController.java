package com.supermap.realestate.registration.web;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.supermap.realestate.registration.service.BatchProjectService;
import com.supermap.wisdombusiness.web.Message;

/**
 * 远程报件Controller，远程报件相关服务放在这里边
 * @ClassName: ConfigController
 * @author 俞学斌
 * @date 2016年04月24日 21:22:28
 */
@Controller
@RequestMapping("/batchproject")
public class BatchProjectController {

	/** 权利service */
	@Autowired
	private BatchProjectService batchProjectService;
	
	private final String prefix = "/realestate/registration/";

	/*****************************************远程报件模块*****************************************/

	/**
	 * 远程报件受理页面(URL:"/accept/index/",Method：GET)
	 * @Title: getAcceptIndex
	 * @author:俞学斌
	 * @date：2016年04月25日  12:11:28
	 * @return
	 */
	@RequestMapping(value = "/accept/index/")
	public String getAcceptIndex() {
		return prefix + "bigproject/acceptbatchproject";
	}
	
	/**
	 * 上传远程报件压缩包（URL:"/uploadbatchzip/",Method：POST）
	 * 
	 * @param file
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/uploadbatchzip/", method = RequestMethod.POST)
	public @ResponseBody Message UpLoadBatchZip(@RequestParam("file") MultipartFile file, HttpServletRequest request, HttpServletResponse response) {
		Message msg=batchProjectService.UpLoadBatchZip(file,request);
		return msg;
	}
	
	/**
	 * 解析远程报件压缩包（URL:"/analysis/{prodef_id}/",Method：POST）
	 * 
	 * @param file
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/analysis/{prodef_id}/", method = RequestMethod.POST)
	public @ResponseBody Message AnalysisBatchProject(@PathVariable("prodef_id") String prodef_id,HttpServletRequest request, HttpServletResponse response) {
		Message msg=batchProjectService.AnalysisBatchProject(request,prodef_id);
		return msg;
	}
	
	/**
	 * 受理远程报件项目（URL:"/accept/{prodef_id}/",Method：POST）
	 * 
	 * @param file
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/accept/{prodef_id}/", method = RequestMethod.POST)
	public @ResponseBody Message AcceptBatchProject(@PathVariable("prodef_id") String prodef_id,HttpServletRequest request, HttpServletResponse response) {
		Message msg=batchProjectService.AcceptBatchProject(request,prodef_id,response);
		return msg;
	}
	
	/*****************************************远程报件模块*****************************************/
	
	/*****************************************批量受理*****************************************/
	/**
	 * 上传批量受理Excel（URL:"/uploadbatchzip/",Method：POST）
	 * 
	 * @param file
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/uploadbatchexcel/{prodef_id}", method = RequestMethod.POST)
	public @ResponseBody HashMap<String,Object> UpLoadBatchExcel(@RequestParam("file") MultipartFile file,@PathVariable("prodef_id") String prodef_id, HttpServletRequest request, HttpServletResponse response) {
		HashMap<String,Object> msg=batchProjectService.UpLoadBatchExcel(file,prodef_id,request);
		return msg;
	}
	
	/**
	 * AcceptBathcProjectExcel（URL:"/uploadbatchzip/",Method：POST）
	 * 
	 * @param file
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/acceptexcel/{excelid}/{prodef_id}/", method = RequestMethod.POST)
	public @ResponseBody HashMap<String,Object> AcceptBathcProjectExcel(@PathVariable("excelid") String excelid,@PathVariable("prodef_id") String prodef_id, HttpServletRequest request, HttpServletResponse response) {
		System.out.println("进入受理：");
		HashMap<String,Object> msg=batchProjectService.AcceptBathcProjectExcel(request,excelid,prodef_id, response);
		System.out.println(msg);
		return msg;
	}
	/*****************************************批量受理*****************************************/
	
}
