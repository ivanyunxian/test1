package com.supermap.wisdombusiness.workflow.web.wfm.serverlayer;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.supermap.wisdombusiness.framework.model.User;
import com.supermap.wisdombusiness.web.ResultMessage;
import com.supermap.wisdombusiness.workflow.service.common.SmObjInfo;
import com.supermap.wisdombusiness.workflow.service.wfm.SmMainTainService;

@Controller
@RequestMapping("/maintain")
public class SmMainTainController {
	@Autowired
	private SmMainTainService smMainTainService;

	/*
	 * 显示默认页面
	 */
	@RequestMapping(value = "/projectmaintain", method = RequestMethod.GET)
	public String ShowIndex(Model model) {
		return "/workflow/maintain/projectmaintain";
	}

	@RequestMapping(value = "/datamatain", method = RequestMethod.GET)
	public String datamatainIndex(Model model) {
		return "/workflow/maintain/datamatain";
	}

	/**
	 * 修改项目紧急程度
	 * 
	 * @param actinst_id
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/project/urgency/{actinst_id}", method = RequestMethod.POST)
	@ResponseBody
	public boolean ModifyUrgency(@PathVariable String actinst_id,
			HttpServletRequest request, HttpServletResponse response) {
		String urgency = request.getParameter("urgency");
		return smMainTainService.ModifyUrgency(actinst_id, urgency);

	}
	/**
	 * 修改项目行政区划代码
	 * @author zhangp
	 * @data 2017年6月16日下午7:04:30
	 * @param actinst_id
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/project/areacode/{actinst_id}", method = RequestMethod.POST)
	@ResponseBody
	public boolean modifyAreaCode(@PathVariable String actinst_id, HttpServletRequest request, HttpServletResponse response){
		String areaCode = request.getParameter("areaCode");
		return smMainTainService.modifyAreaCode(actinst_id,areaCode);
	}
	// 文件资料整理
	@RequestMapping(value = "/project/material/arrangement", method = RequestMethod.POST)
	@ResponseBody
	public SmObjInfo arrangement(HttpServletRequest request,
			HttpServletResponse response) {
		String path = request.getParameter("path");
		return smMainTainService.arrangement(path);

	}

	// 整理剩余文件夹有效性检测

	// 文件资料整理
	@RequestMapping(value = "/project/material/valite", method = RequestMethod.POST)
	@ResponseBody
	public String valite(HttpServletRequest request,
			HttpServletResponse response) {
		String path = request.getParameter("path");
		String del = request.getParameter("del");
		String status = request.getParameter("status");
		return smMainTainService.valite(path, del, status);
	}

	// 计算流程时间
	@RequestMapping(value = "/project/proouttime", method = RequestMethod.POST)
	@ResponseBody
	public String proouttime(HttpServletRequest request,
			HttpServletResponse response) {
		return smMainTainService.proouttime();
	}

	// 计算流程权重

	@RequestMapping(value = "/project/weight", method = RequestMethod.POST)
	@ResponseBody
	public String computeWeight(HttpServletRequest request,
			HttpServletResponse response) {
		return smMainTainService.computeWeight();
	}

	// 抽取调档信息

	@RequestMapping(value = "/ddinfo/extract", method = RequestMethod.POST)
	@ResponseBody
	public String ExtractDDInfo(HttpServletRequest request,
			HttpServletResponse response) {
		String qlr=request.getParameter("QLR");
		return smMainTainService.ExtractDDInfo(qlr);
	}

	// 处理文件中有文件没有勾选的问题
	@RequestMapping(value = "/project/nostatus", method = RequestMethod.GET)
	@ResponseBody
	public String nostatus(HttpServletRequest request,
			HttpServletResponse response) {

		return smMainTainService.nostatus();

	}

	/**
	 * 对前台的数据封装成js文件
	 * 
	 * @throws IOException
	 */
	// 处理文件中有文件没有勾选的问题
	@RequestMapping(value = "/project/configjsss", method = RequestMethod.GET)
	@ResponseBody
	public ResultMessage createJS(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		Map map = request.getParameterMap();
		String realpath = request.getRealPath("/");
		return smMainTainService.createJS(map, realpath);

	}

	// 对比办结未在 档案系统中查询到的项目数量
	@RequestMapping(value = "count/project/nullto/archives", method = RequestMethod.GET)
	@ResponseBody
	public String getNullArvhicesCount(HttpServletRequest request,
			HttpServletResponse response) throws IOException {

		return smMainTainService.getNullArvhicesCount();

	}

	@RequestMapping(value = "list/project/nullto/archives", method = RequestMethod.GET)
	@ResponseBody
	public List<Map> getNullArvhices(HttpServletRequest request,
			HttpServletResponse response) throws IOException {

		return smMainTainService.getNullArvhices();

	}

	// 对比办结未在 档案系统中查询到的项目数量
	@RequestMapping(value = "count/project/nullto/archives/ddd", method = RequestMethod.GET)
	@ResponseBody
	public String getNullArvhicesDDDCount(HttpServletRequest request,
			HttpServletResponse response) throws IOException {

		return smMainTainService.getNullArvhicesDDDCount();

	}

	@RequestMapping(value = "list/project/nullto/archives/ddd", method = RequestMethod.GET)
	@ResponseBody
	public List<Map> getNullArvhicesByDDD(HttpServletRequest request,
			HttpServletResponse response) throws IOException {

		return smMainTainService.getNullArvhicesDDD();

	}
	
	@RequestMapping(value = "maintian/prolsh", method = RequestMethod.GET)
	@ResponseBody
	public ResultMessage MainTainProlshOfNull(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		ResultMessage resmsg = new ResultMessage();
		//TODO:维护WFI_PROINST表中prolsh为空的数据，并且更新不动产登记
		resmsg =smMainTainService.updateProlshOfNull();
		return resmsg;

	}
	
	@RequestMapping(value = "/wait/archives", method = RequestMethod.GET)
	@ResponseBody
	public String getArchives(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		return smMainTainService.getArchives(10);

	}
	
	/**
	 * 修改项目的状态（绿色通道或者取消绿色通道）
	 * @author JHX
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/project/greenchannel/{actinstid}/{insttype}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<Boolean> updateProjectInstanceType(HttpServletRequest request,
			HttpServletResponse response,@PathVariable String actinstid,@PathVariable String insttype) throws IOException {
		boolean flag =  smMainTainService.updateProjectInstanceType(actinstid,insttype);
	    return new ResponseEntity<Boolean>(flag,HttpStatus.OK);

	}
	
	

}
