package com.supermap.wisdombusiness.workflow.web.wfm.serverlayer;


import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.wisdombusiness.framework.model.User;
import com.supermap.wisdombusiness.workflow.model.Wfd_Prodef;
import com.supermap.wisdombusiness.workflow.model.Wfi_ProInst;
import com.supermap.wisdombusiness.workflow.service.common.SmObjInfo;
import com.supermap.wisdombusiness.workflow.service.wfi.SmProInst;
import com.supermap.wisdombusiness.workflow.service.wfm.SmProDefService;


@Controller

@RequestMapping("/frame")
public class SmProDefController {

	@Autowired
	SmProDefService _SmProDefService;
	@Autowired
	SmProInst smProInst;
	
	//
	@RequestMapping(value = "/{staffid}/prodefinfos",method = RequestMethod.GET)
	public @ResponseBody void GetProDef(
			@PathVariable("staffid") String staffid,
			HttpServletRequest request, HttpServletResponse response) throws IOException {
			
			response.setHeader("Content-type","text/html;charset=UTF-8"); //指定消息头以UTF-8码表读数据 
			String data=_SmProDefService.GetProDefInfos(staffid);
			response.getOutputStream().write(data.getBytes("utf-8"));
		    //return "";
			
	}
	
	//流程删除
	@RequestMapping(value = "/prodef/del/{prodefid}",method = RequestMethod.POST)
	@ResponseBody
	public  SmObjInfo delProdef(@PathVariable String prodefid,HttpServletRequest request, HttpServletResponse response){
		SmObjInfo smObjInfo=null;
		if(prodefid!=null&&!prodefid.equals("")){
			smObjInfo=_SmProDefService.delProdef(prodefid);
		}
		return smObjInfo;
		
	}


	/**
	 * 获取流程定义的图形数据
	 * 
	 * */
	@RequestMapping(value = "/workflow/define/{actinstid}", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object>  GetWorkFlow( @PathVariable String actinstid){
		return _SmProDefService.GetWorkFlow(actinstid);
	}
	@RequestMapping(value = "/actdef/role/{actinstid}", method = RequestMethod.GET)
	@ResponseBody
	public List<User>  GetACTSaffByactdefID( @PathVariable String actinstid){
		return _SmProDefService.GetACTSaffByactdefID(actinstid);
	}
	
	//获取当前工作流流中所有定义的活动
	@RequestMapping(value = "/actdef/name", method = RequestMethod.GET)
	@ResponseBody
	public List<Map> GetActDefName(HttpServletRequest request, HttpServletResponse response){
		return _SmProDefService.GetActDefName();
	}
	/**
	 * 验证流程编码是否重复
	 */
	@RequestMapping(value = "/prodef/cordcheck", method = RequestMethod.GET)
	@ResponseBody
	public boolean cordCheck ( HttpServletRequest request, HttpServletResponse response){
		String proDefId = request.getParameter("prodefid");
		String proDefCord = request.getParameter("prodefcord");
		return _SmProDefService.cordCheck(proDefId,proDefCord);
	}
	
	/**
	 * 根据actinstid获取prodef
	 */
	@RequestMapping(value = "/getprodefbyactinst/{actinstid}", method = RequestMethod.GET)
	@ResponseBody
	public Wfd_Prodef getProDef ( @PathVariable String actinstid,HttpServletRequest request, HttpServletResponse response){
		if(!StringHelper.isEmpty(actinstid)){
			Wfi_ProInst proinst = smProInst.GetProInstByActInstId(actinstid);
			if(null!=proinst){
				return _SmProDefService.GetProdefById(proinst.getProdef_Id());
			}
		}
		return null;
	}
	
}
