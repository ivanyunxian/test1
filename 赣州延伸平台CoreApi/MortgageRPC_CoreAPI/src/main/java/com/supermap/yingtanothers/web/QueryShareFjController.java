package com.supermap.yingtanothers.web;

import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.supermap.realestate.registration.model.BDCS_QL_GZ;
import com.supermap.realestate.registration.util.ConstValue;
import com.supermap.realestate.registration.util.RequestHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.realestate.registration.util.YwLogUtil;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import com.supermap.wisdombusiness.web.ResultMessage;
import com.supermap.wisdombusiness.workflow.util.Message;
import com.supermap.yingtanothers.service.ExtractAttachment;
import com.supermap.yingtanothers.service.QueryShareFjService;


/**
 * 
 * 作者： 苗利涛
 * 时间： 2016年4月13日 上午9:23:15
 * 功能：鹰潭市共享附件查询
 */
@Controller
@RequestMapping("/queryshare")
public class QueryShareFjController {
	
	@Autowired
	private QueryShareFjService q_QueryShareFjService;
	@Autowired
	private ExtractAttachment e_ExtractAttachment;
	@Autowired
	private CommonDao dao;
	/**
	 * 服务接口：鹰潭市共享附件查询结果页面
	 * @Title: showSharePage
	 * @date：2016年4月12日 下午21:30:21
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/querysharefj/{proinstid}/{file_number}/", method = RequestMethod.GET)
	public String showSharePage(@PathVariable("proinstid") String proinstid ,@PathVariable("file_number") String file_number ,Model model) {		
		model.addAttribute("config", "{'proinstid':'"+proinstid+"','file_number':'"+file_number +"'}");
		return "/realestate/registration/modules/extractshare/extractShareFj";
	}
	
	
	/**
	 * 服务接口：查询数据
	 * @Title: queryShareData
	 * @date： 2016年4月13日 上午9:23:15
	 * @param request
	 * @return
	 * @throws ClassNotFoundException
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "/querysharefj/queryShareData/", method = RequestMethod.GET)
	public @ResponseBody Message queryShareData(HttpServletRequest request) throws ClassNotFoundException, UnsupportedEncodingException {
		
		// 返回的结果对象
		Message msg = new Message();
		// 获取page和rows参数
		Integer page = RequestHelper.getPage(request);
		Integer rows = RequestHelper.getRows(request);
		String file_number = RequestHelper.getParam(request, "file_number");
		String qlr = RequestHelper.getParam(request, "QLR");
		String ywh = RequestHelper.getParam(request, "YWH");
		msg = q_QueryShareFjService.queryQlxxInfoByGXXMBH(file_number,qlr, ywh, page, rows);					
		
		return msg;
	}
	
	@RequestMapping(value = "/uploadfj/", method = RequestMethod.POST)
	@ResponseBody
	public ResultMessage UploadFj(HttpServletRequest request,
			HttpServletResponse response) {
		ResultMessage msg = null;
		try {
			msg = new ResultMessage();			
//			String qlr = request.getParameter( "qlr");
//			String ywh = request.getParameter( "ywh");
			String gxxmbh = request.getParameter("gxxmbh");
			String proinstid = request.getParameter("proinstid");
			String file_Path = request.getSession().getServletContext().getRealPath("/");
			if (gxxmbh != null && gxxmbh != "" && proinstid != null && proinstid != "") {
				e_ExtractAttachment.abstractFJFromZJK(proinstid, gxxmbh, file_Path);
				msg.setSuccess("true");
				msg.setMsg("上传成功！");
				YwLogUtil.addYwLog("共享房产库上传附件-成功", ConstValue.SF.NO.Value,ConstValue.LOG.ADD);
			}
			
		} catch (Exception e) {
			
			msg.setSuccess("false");
			msg.setMsg("上传成功！");
			YwLogUtil.addYwLog("共享房产库上传附件-失败", ConstValue.SF.NO.Value,ConstValue.LOG.ADD);
		}								
	
		return msg;
	}
	/**
	 * 服务接口：中间库上传附件时候查询是否选择单元
	 * @Title: queryShareData
	 * @date： 2016年4月22日 上午11:23:15
	 * @param request
	 * @return ResultMessage
	 *
	 */
	@RequestMapping(value = "/queryQl/", method = RequestMethod.POST)
	@ResponseBody
	public ResultMessage queryQl(HttpServletRequest request,HttpServletResponse response) {
		ResultMessage msg = null;
		try {
			msg = new ResultMessage();			
			
			String ywh = request.getParameter("ywh");
			StringBuilder conditionBuilder = new StringBuilder("1 = 1");
			if (!StringHelper.isEmpty(ywh)) {
				conditionBuilder.append(" AND YWH = '").append(ywh).append("'");
				String strQuery = conditionBuilder.toString();
				List<BDCS_QL_GZ> qllist = dao.getDataList(BDCS_QL_GZ.class, strQuery);
				if(qllist.size() > 0){					
					msg.setSuccess("true");
					msg.setMsg("已经选择单元信息！");	
				}else {
					msg.setSuccess("false");
					msg.setMsg("未选择单元信息！");
				}				
			}
			
		} catch (Exception e) {
			msg.setSuccess("false");
			msg.setMsg("未选择单元信息！");
		}								
	
		return msg;
	}
	
	/**
	 * 服务接口：中间库上传附件时候查询是否选择单元
	 * @Title: saveCasenum
	 * @date： 2016年4月23日 上午9:23:15
	 * @param request
	 * @return ResultMessage
	 *
	 */
	@RequestMapping(value = "/saveCasenum/", method = RequestMethod.POST)
	@ResponseBody
	public ResultMessage saveCasenum(HttpServletRequest request,HttpServletResponse response) {
		ResultMessage msg = new ResultMessage();	
						
			String ywh = request.getParameter("ywh");
			String gxxmbh = request.getParameter("gxxmbh");
			msg = q_QueryShareFjService.saveCasenumByYwh(gxxmbh, ywh);
											
		return msg;
	}
}
