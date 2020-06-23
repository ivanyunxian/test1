package com.supermap.wisdombusiness.workflow.web.wfm.serverlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.supermap.realestate.registration.util.ConstValue;
import com.supermap.realestate.registration.util.GetProperties;
import com.supermap.wisdombusiness.archives.web.common.BookMapping;
import com.supermap.wisdombusiness.web.Message;
import com.supermap.wisdombusiness.workflow.model.WFD_SPMB;
import com.supermap.wisdombusiness.workflow.model.Wfi_ProInst;
import com.supermap.wisdombusiness.workflow.model.Wfi_Spdy;
import com.supermap.wisdombusiness.workflow.model.Wfi_Spyj;
import com.supermap.wisdombusiness.workflow.service.common.Approval;
import com.supermap.wisdombusiness.workflow.service.common.SmApproval;
import com.supermap.wisdombusiness.workflow.service.common.SmObjInfo;
import com.supermap.wisdombusiness.workflow.service.common.TreeInfo;
import com.supermap.wisdombusiness.workflow.service.wfi.SmProInst;
import com.supermap.wisdombusiness.workflow.service.wfi.SmSpdy;
import com.supermap.wisdombusiness.workflow.service.wfi.SmStaff;
import com.supermap.wisdombusiness.workflow.service.wfm.OperationService;
import com.supermap.wisdombusiness.workflow.service.wfm.SmProSPService;
import com.supermap.wisdombusiness.workflow.util.Page;

@Controller
@RequestMapping("/frame")
public class SmProSPController {
	@Autowired
	private SmProSPService smProSPService;
	@Autowired
	private SmStaff smStaff;
	@Autowired
	private SmSpdy smSpdy;
	@Autowired
	private BookMapping bookMapping;
	@Autowired
	private SmProInst smProinst;
	@Autowired
	private OperationService operationService;
	/**
	 * 审批页面映射
	 * */
	@RequestMapping(value = "/approval/list", method = RequestMethod.GET)
	public String ShowIndex(Model model) {
				//增加是否是海口判断
				String  area = operationService.getNativeAreaCodeConfig();
				if(area.equals("460100")){
					return "/workflow/frame/approval_HK";
				}
				return "/workflow/frame/approval";
	}
	/**
	 * 审批页面映射
	 * */
	@RequestMapping(value = "/approval/signature", method = RequestMethod.GET)
	public String showApproval(Model model) {
				return "/workflow/frame/approval_sign";
	}
	/**
	 * 审批页面映射
	 * */
	@RequestMapping(value = "/approval/unit", method = RequestMethod.GET)
	public String ApprovalByUnit(Model model) {
		return "/workflow/frame/approval_unit";
	}
	/**
	 * @author JHX
	 * 审批意见编辑
	 * 海口项目，要求环节意见只有一个，跟环节定义走。
	 * */
	@RequestMapping(value = "/approval/yj/edite/{spyjid}", method = RequestMethod.GET)
	public String spyjEdite(@PathVariable String spyjid, HttpServletRequest request, HttpServletResponse response,Model model){
		Wfi_Spyj spyj = smProSPService.GetSPYJById(spyjid);
		String password = request.getParameter("psw");
		if(spyj!=null){
			model.addAttribute("spyj", spyj);
			model.addAttribute("psw", password==null?"":password);
		}
		return "/workflow/frame/approvalyjedite";
	}
	/**
	 * @author JHX
	 * 审批意见编辑之后保存
	 * 只是更新审批意见
	 * */
	@RequestMapping(value = "/approval/yj/edite/save", method = RequestMethod.POST)
	@ResponseBody
	public SmObjInfo spyjEditeSave(HttpServletRequest request, HttpServletResponse response,Model model){
		boolean flag=true;
		SmObjInfo objectinfo = new SmObjInfo();
		String password = request.getParameter("password");
		String spyj = request.getParameter("spyj");
		String spyjid = request.getParameter("spyjid");
	    //调用接口验证密码的正确性
		flag=smProSPService.SignCheckPassword(password);
		if(!flag){
			objectinfo.setConfirm("NO");
			objectinfo.setDesc("密码密码不正确！");
			return objectinfo;
		}
		boolean isupdate = smProSPService.updateSpyjById(spyjid, spyj);
		if(isupdate){
			//签名
			smProSPService.Sign(password,spyj,spyjid);
			objectinfo.setConfirm("OK");
			objectinfo.setDesc("保存成功!");
		}else{
			objectinfo.setConfirm("NO");
			objectinfo.setDesc("保存失败!");
		}
		return objectinfo;

	}
	/**
	 * 获取活动审批信息
	 * */
	@RequestMapping(value = "/approval/{acinstid}", method = RequestMethod.GET)
	@ResponseBody
	public List<Approval> GetApprovalList(@PathVariable String acinstid, HttpServletRequest request, HttpServletResponse response) {
		String readonly = request.getParameter("readonly");
		return smProSPService.GetSPYJ(acinstid, readonly);
	}
	/**
	 * 获取单元审批信息
	 **/
	@RequestMapping(value = "/approval/unit/{actinstid}")
	@ResponseBody
	public List<Approval> GetUnitApprovalList(@PathVariable String actinstid, HttpServletRequest request, HttpServletResponse response){
		String readonly = request.getParameter("readonly");
		String bdcdyh = request.getParameter("bdcdyh");
		return smProSPService.GetSPYJ(actinstid, readonly, bdcdyh);
	}
	/**
	 * 保存审批意见
	 * */
	@RequestMapping(value = "/approval/save", method = RequestMethod.POST)
	@ResponseBody
	public SmObjInfo saveApproval(HttpServletRequest request, HttpServletResponse response) {
		String splxString = request.getParameter("splx");
		String spdyidString = request.getParameter("spdyid");
		String actinstidString = request.getParameter("actinst_id");
		String spyj = request.getParameter("spyj");
		String spyjid = request.getParameter("spyjid");
		String signid=request.getParameter("signid");
		String signflag=request.getParameter("signflag");
		String signjg=request.getParameter("signjg");
		String bdcdyh=request.getParameter("bdcdyh");
		// YwLogUtil.addYwLog("保存审批意见：内容：" + spyj,
		// ConstValue.SF.YES.Value,ConstValue.LOG.ADD);
		return smProSPService.SaveApproval(spyjid, spdyidString, splxString, actinstidString, spyj,signid,signflag,signjg,null,bdcdyh);

	}
	/**
	 * @author JHX
	 * 删除审批意见:海口项目要求意见是可以删除的
	 * */
	@RequestMapping(value = "/approval/delete", method = RequestMethod.GET)
	@ResponseBody
	public SmObjInfo delteApproval(HttpServletRequest request, HttpServletResponse response) {
		boolean flag=true;
		SmObjInfo objectinfo = new SmObjInfo();
		String spyjid = request.getParameter("spyjid");
		String password = request.getParameter("password");
		//调用接口验证密码的正确性
		flag=smProSPService.SignCheckPassword(password);
		if(flag){
			objectinfo = smProSPService.deleteApproval(spyjid);
		}else{
			objectinfo.setConfirm("NO");
			objectinfo.setDesc("删除失败,密码错误");
		}
        return objectinfo;

	}

	/**
	 * 批量审批
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "batch/approval/save", method = RequestMethod.POST)
	@ResponseBody
	public List<SmObjInfo> batchSaveApproval(HttpServletRequest request, HttpServletResponse response) {
		List<SmObjInfo> infos = new ArrayList<SmObjInfo>();
		String param = request.getParameter("param");
		if (param != null) {
			JSONArray array = JSONArray.fromObject(param);
			if (array != null && array.size() > 0) {
				for (int i = 0; i < array.size(); i++) {
					Object obj = array.get(i);
					if (obj != null && !obj.equals("")) {
						JSONObject o = JSONObject.fromObject(obj);
						if (o != null) {
							String spyjid = o.getString("spyjid");
							String splx = o.getString("splx");
							String actinstid = o.getString("actinstid");
							String spyj = o.getString("spyj");
							String spid_batch = o.getString("spid_batch");
							String signid = o.getString("signid");
							String signflag = o.getString("signflag");
							String signjg = o.getString("spid_batch");
							SmObjInfo info = smProSPService.SaveApproval(spyjid, "", splx, actinstid, spyj, signid, signflag, signjg, spid_batch,null);
							info.setName(actinstid);
							infos.add(info);
						}
					}
				}
			}
		}
		return infos;

	}

	/**
	 * 审批定义树
	 */
	@RequestMapping(value = "/approval/spdytree", method = RequestMethod.GET)
	@ResponseBody
	public List<TreeInfo> GetTreeSpdy(HttpServletRequest request, HttpServletResponse response) {
		return smProSPService.GetSpdyTree();
	}

	/**
	 * 插入一个审批定义关系
	 */
	@RequestMapping(value = "/approval/add/newacttospdy", method = RequestMethod.POST)
	@ResponseBody
	public SmObjInfo CreateActToSpdy(HttpServletRequest request, HttpServletResponse response) {
		String spdyid = request.getParameter("spdyid");
		String actdefid = request.getParameter("actdefid");
		int index = Integer.parseInt(request.getParameter("index"));
		return smProSPService.CreateActToSpdy(spdyid, actdefid, index);
	}

	/**
	 * 获取某个活动已经配置的审批
	 * */
	@RequestMapping(value = "/approval/define/{actdefid}", method = RequestMethod.GET)
	@ResponseBody
	public List<SmApproval> GetSPDYByActinstID(@PathVariable String actdefid, HttpServletRequest request, HttpServletResponse response) {
		return smProSPService.GetActSPDY(actdefid);
	}

	// 删除活动定义审批

	@RequestMapping(value = "/approval/delactspdy/{actdefspdyid}", method = RequestMethod.POST)
	@ResponseBody
	public SmObjInfo DelSPDYByActSPDYID(@PathVariable String actdefspdyid, HttpServletRequest request, HttpServletResponse response) {
		return smProSPService.DelActSPDY(actdefspdyid);
	}

	// 更新索引审批定义和活动关系索引
	@RequestMapping(value = "/approval/newacttospdyindex", method = RequestMethod.POST)
	public @ResponseBody SmObjInfo UpdateActdefToSpdy(HttpServletRequest request, HttpServletResponse response) {
		String json = request.getParameter("spdyindex");
		JSONArray array = JSONArray.fromObject(json);
		smProSPService.UpdateActToSpdy(array);
		SmObjInfo obinfo = new SmObjInfo();
		obinfo.setDesc("更新成功！");
		return obinfo;
	}

	// 更新流程的配置的意见是否只读
	@RequestMapping(value = "/approval/update/readonly/{actdefspdyid}", method = RequestMethod.POST)
	public @ResponseBody SmObjInfo UpdateReadonly(@PathVariable String actdefspdyid, HttpServletRequest request, HttpServletResponse response) {
		String readonlyString = request.getParameter("readonly");
		smProSPService.updateReadonly(actdefspdyid, Integer.parseInt(readonlyString));
		SmObjInfo obinfo = new SmObjInfo();
		obinfo.setDesc("更新成功！");
		return obinfo;
	}

	// 新增审批定义
	@RequestMapping(value = "/approval/add/spdy", method = RequestMethod.POST)
	public @ResponseBody SmObjInfo AddSpdy(@ModelAttribute Wfi_Spdy spdy, HttpServletRequest request, HttpServletResponse response) {
		return smProSPService.SaveSpdy(spdy);
	}

	// 获取一个审批定义
	@RequestMapping(value = "/approval/edit/{spdyid}", method = RequestMethod.GET)
	@ResponseBody
	public Wfi_Spdy GetSpdyBySpdyid(@PathVariable String spdyid, HttpServletRequest request, HttpServletResponse response) {
		return smProSPService.GetSPDYBySpdyid(spdyid);

	}

	// 删除审批定义
	@RequestMapping(value = "/approval/del/{spdyid}", method = RequestMethod.POST)
	@ResponseBody
	public SmObjInfo DelSpdy(@PathVariable String spdyid, HttpServletRequest request, HttpServletResponse response) {
		return smProSPService.DelSpInfo(spdyid);
	}

	/**
	 * 获取某人可以调用的所有的意见
	 * */
	@RequestMapping(value = "/approval/getmbdefine", method = RequestMethod.GET)
	@ResponseBody
	public Page GetMBDefine(HttpServletRequest request, HttpServletResponse response) {
		String staffid = smStaff.getCurrentWorkStaffID();
		return smProSPService.GetSPMBDefine(staffid);

	}

	/**
	 * 为某人新增一个模板
	 * */
	@RequestMapping(value = "/approval/addmbdefine", method = RequestMethod.POST)
	@ResponseBody
	public SmObjInfo AddMBDefine(HttpServletRequest request, HttpServletResponse response) {
		String staffid = smStaff.getCurrentWorkStaffID();
		String spContentString = request.getParameter("spcontent");
		String idString = request.getParameter("id");
		String xhString = request.getParameter("xh");
		// YwLogUtil.addYwLog("新增意见模版",
		// ConstValue.SF.YES.Value,ConstValue.LOG.ADD);
		return smProSPService.SaveTpl(spContentString, staffid,idString, xhString);
	}

	/**
	 * 为某人删除一个模板
	 * */
	@RequestMapping(value = "/approval/delmbdefine/{spmbid}", method = RequestMethod.POST)
	@ResponseBody
	public SmObjInfo DelMBDefine(@PathVariable String spmbid, HttpServletRequest request, HttpServletResponse response) {
		if (spmbid != null && !spmbid.equals("")) {
			// YwLogUtil.addYwLog("删除意见模版",
			// ConstValue.SF.YES.Value,ConstValue.LOG.DELETE);
			return smProSPService.DelSp(spmbid);
		}
		return null;
	}

	/**
	 * 为某人更新一个模板
	 * */
	@RequestMapping(value = "/approval/updatembdefine/{staffid}", method = RequestMethod.POST)
	@ResponseBody
	public List<WFD_SPMB> UpdateMBDefine(@PathVariable String staffid, HttpServletRequest request, HttpServletResponse response) {

		return null;
	}

	// 批量获取活动的当前意见

	@RequestMapping(value = "batch/some/approval", method = RequestMethod.GET)
	@ResponseBody
	public List<Wfi_Spyj> batchGetApproval(HttpServletRequest request, HttpServletResponse response) {
		String splx = request.getParameter("splx");
		String actinsts = request.getParameter("actinsts");
		return smProSPService.batchGetApproval(splx, actinsts);
	}

	// 关于第三方审批使用电子签名
	@RequestMapping(value = "jinge/sign", method = RequestMethod.POST)
	@ResponseBody
	public Object Sign(HttpServletRequest request, HttpServletResponse response) {
		String password = request.getParameter("password");
		String spyj = request.getParameter("spyj");
		String yjid = request.getParameter("spyjid");
		//为了时间统一需要在签名的时候需要将意见保存：
		return smProSPService.Sign(password,spyj,yjid);

	}
	@RequestMapping(value = "/unit/info",method = RequestMethod.POST)
	@ResponseBody
	public com.supemap.mns.model.Message getUnitInfo(HttpServletRequest request, HttpServletResponse response){
		String actinst_id = request.getParameter("actinst_id");
		Wfi_ProInst proinst = smProinst.GetProInstByActInstId(actinst_id);
		String filenumber = proinst.getFile_Number();
		com.supemap.mns.model.Message msg = new com.supemap.mns.model.Message();
		msg = bookMapping.GetDJBXX(filenumber);
		return msg;
	}
	// 海口批量审核
	@RequestMapping(value = "batch/approvalhk/{password}", method = RequestMethod.POST)
	@ResponseBody
	public Object batchApproval(@PathVariable String password , HttpServletRequest request, HttpServletResponse response) {
		boolean check = smProSPService.SignCheckPassword(password);
		if(!check){
			return "密码错误!";
		}
		List<SmObjInfo> infos = new ArrayList<SmObjInfo>();
		String param = request.getParameter("param");
		if (param != null) {
			JSONArray array = JSONArray.fromObject(param);
			if (array != null && array.size() > 0) {
				for (int i = 0; i < array.size(); i++) {
					Object obj = array.get(i);
					if (obj != null && !obj.equals("")) {
						JSONObject o = JSONObject.fromObject(obj);
						if (o != null) {
							String spyjid = o.getString("spyjid");
							String splx = o.getString("splx");
							String actinstid = o.getString("actinstid");
							String spyj = o.getString("spyj");
							Map<String, String> map = smProSPService.beforeApproval(spyjid , actinstid , spyj , splx , password);
							SmObjInfo info = smProSPService.SaveApproval(map.get("spyjid"), "", splx, map.get("actinstid"), spyj, null, null, null, null,null);
							info.setName(actinstid);
							infos.add(info);
						}
					}
				}
			}
		}
		return infos;
		
	}
	
}
