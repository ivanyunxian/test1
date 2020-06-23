package com.supermap.wisdombusiness.synchroinline.web;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDaoInline;
import com.supermap.wisdombusiness.synchroinline.service.InlineRoleService;

import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.supermap.realestate.registration.util.ConfigHelper;
import com.supermap.realestate.registration.util.ConstHelper;
import com.supermap.wisdombusiness.synchroinline.model.*;
import com.supermap.wisdombusiness.synchroinline.service.AcceptProjectService;
import com.supermap.wisdombusiness.synchroinline.service.InlineProjectService;
import com.supermap.wisdombusiness.workflow.model.Wfi_MaterData;
import com.supermap.wisdombusiness.workflow.service.common.SmObjInfo;
import com.supermap.wisdombusiness.workflow.service.common.SmProInfo;
import com.supermap.wisdombusiness.workflow.service.wfi.SmStaff;


@Controller
@RequestMapping(value = "/inlineproject")
public class InlineProjectController
{
	@Autowired
	InlineProjectService inlineprojectService;
	
	@Autowired
	CommonDaoInline daoInline;
	
	@Autowired
	AcceptProjectService acceptProjectService;
	@Autowired
	private SmStaff smStaff;
	@Autowired
	InlineRoleService inlineRoleService;
	private String root = "/realestate/registration/inline/";

	@RequestMapping(value = "/qjdcsh")
	public String qjdcsh(Model model)
	{
		DicInfo dic_djlx = daoInline.getDicInfo("DJLX");
		DicInfo dic_qllx = daoInline.getDicInfo("QLLX");
		String isAble = inlineRoleService.getIsAble();
		model.addAttribute("djlx", dic_djlx);
		model.addAttribute("qllx", dic_qllx);
		model.addAttribute("spyjs", inlineprojectService.getSpyjs());
		String xzqdm= ConfigHelper.getNameByValue("XZQHDM");
		model.addAttribute("xzqdm",xzqdm);
		model.addAttribute("isAble",isAble);
		return "/realestate/registration/inline/qjdcsh";
	}

	@RequestMapping(value = "/xmslsh")
	public String xmslsh(Model model)
	{
		DicInfo dic_djlx = daoInline.getDicInfo("DJLX");
		DicInfo dic_qllx = daoInline.getDicInfo("QLLX");
		String isAble = inlineRoleService.getIsAble();
		model.addAttribute("djlx", dic_djlx);
		model.addAttribute("qllx", dic_qllx);
		model.addAttribute("spyjs", inlineprojectService.getSpyjs());
		String xzqdm= ConfigHelper.getNameByValue("XZQHDM");
		model.addAttribute("xzqdm",xzqdm);
		model.addAttribute("isAble",isAble);
		return "/realestate/registration/inline/xmslsh";
	}

	@RequestMapping(value = "/ybx")
	public String ybx(Model model)
	{
		DicInfo dic_djlx = daoInline.getDicInfo("DJLX");
		DicInfo dic_qllx = daoInline.getDicInfo("QLLX");
		model.addAttribute("djlx", dic_djlx);
		model.addAttribute("qllx", dic_qllx);
		model.addAttribute("uid", smStaff == null ? "" : smStaff.getCurrentWorkStaffID());
		model.addAttribute("uname", smStaff == null || smStaff.getCurrentWorkStaff() == null ? "" : smStaff.getCurrentWorkStaff().getUserName());
		String xzqdm= ConfigHelper.getNameByValue("XZQHDM");
		model.addAttribute("xzqdm",xzqdm);
		return "/realestate/registration/inline/ybx";
	}

	@RequestMapping(value = "/shgc")
	public String shgc(Model model, String slsqId) throws Exception
	{
		Pro_proinst slsq = daoInline.getProinst(slsqId);
		Pro_slxmsh shxx = daoInline.getSlxmsh(slsqId);
		model.addAttribute("shxx", shxx);
		model.addAttribute("slsq", slsq);
		return "/realestate/registration/inline/shgc";
	}

	@RequestMapping(value = "/selectProdef", method = RequestMethod.GET)
	public String selectProdef(Model model, String slsqId)
	{
		SmProInfo newInfo = new SmProInfo();
		model.addAttribute("SmProInfo", newInfo);
		model.addAttribute("SLR", smStaff.GetStaffName(smStaff.getCurrentWorkStaffID()));
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date dateNow = new Date();
		model.addAttribute("SLSJ", sdf.format(dateNow));
		model.addAttribute("slsqId", slsqId);
		return "/realestate/registration/inline/selectProdef";
	}



	@RequestMapping(value = "/saveProdefInfo", method = RequestMethod.POST)
	public @ResponseBody JsonMessage saveProdefInfo(HttpServletRequest request, String slsqId, String ProDef_ID, SmProInfo info)
	{
		JsonMessage msg = new JsonMessage();
		try
		{
			// acceptProjectService.saveProdefInfo(slsqId,ProDef_ID);
			msg.setState(true);
			msg.setMsg("操作成功。");
		}
		catch (Exception ex)
		{
			msg.setState(false);
			msg.setMsg(ex.getMessage());
		}
		return msg;
	}

	/**
	 * 受理项目查看详情页
	 *
	 * @param request
	 * @param model
	 * @param slsqId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "details", method = RequestMethod.GET)
	public String details(HttpServletRequest request, Model model, String slsqId) throws Exception
	{
		model.addAttribute("slsqId", slsqId);
		return root + "details";
	}

	/**
	 * 申请表
	 *
	 * @param request
	 * @param model
	 * @param slsqId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "sqb", method = RequestMethod.GET)
	public String sqb(HttpServletRequest request, Model model, String slsqId) throws Exception
	{
		if (slsqId == null || slsqId.isEmpty())
			throw new Exception("slsqId不能为空。");
		Pro_proinst slsq = daoInline.getProinst(slsqId);
		if (slsq == null)
			throw new Exception("申请受理项目不存在。");
		List<Pro_proposerinfo> sqrs = daoInline.getSqrsBySlsqId(slsq.getId());
		model.addAttribute("slsq", slsq);
		model.addAttribute("sqrs", daoInline.formatSqr(sqrs));
		model.addAttribute("djlx", ConstHelper.getNameByValue("DJLX", slsq.getDjlx()));
		model.addAttribute("qllx", ConstHelper.getNameByValue("QLLX", slsq.getQllx()));
		model.addAttribute("rows", sqrs.size() * 7);
		model.addAttribute("new_rows", sqrs.size() * 8);
		DicInfo dicInfo = new DicInfo();
		model.addAttribute("lsh", slsq.getLsh());
		model.addAttribute("fwyt", dicInfo.getNameByCode(slsq.getFwsyq_yt()));
		if ("11".equals(slsq.getXmlx()))
		{
			int bdc_rowspan = 5;
			JSONObject jsonExtend = JSON.parseObject(slsq.getExtend_data());
			String bdcdylx = jsonExtend.getString("bdcdylx");
			if ("FW".equals(bdcdylx))
			{
				dicInfo = daoInline.getDicInfo("FWYT");
			}
			else if ("TD".equals(bdcdylx))
			{
				dicInfo = daoInline.getDicInfo("TDYT");
			}
			if (jsonExtend != null && jsonExtend.containsKey("bdcdys"))
			{
				JSONArray bdcArry = jsonExtend.getJSONArray("bdcdys");
				bdc_rowspan = bdcArry.size() * bdc_rowspan;
				for (int i = 0; i < bdcArry.size(); i++)
				{
					JSONObject jsonBdc = (JSONObject) bdcArry.get(i);
					String yt = jsonBdc.getString("fwsyq_yt");
					jsonBdc.put("fwsyq_yt", dicInfo.getNameByCode(yt));
				}
			}
			// 计算不动产跨行数
			model.addAttribute("bdc_rowspan", bdc_rowspan);
			model.addAttribute("extend", jsonExtend);

			//返回鹰潭的预告抵押项目详情视图
			if ("423".equals(slsq.getQllx()))
				return this.root + "sqb_yt_ygdy";
			else
				return this.root + "sqb_yt";
			// 返回鹰潭的受理项目详情视图
			//return this.root + "sqb_yt";

		}
		else if ("12".equals(slsq.getXmlx()))
		{
			int bdc_rowspan = 6;
			JSONObject jsonExtend = JSON.parseObject(slsq.getExtend_data());
			String bdcdylx = jsonExtend.getString("bdcdylx");
			if ("FW".equals(bdcdylx))
			{
				dicInfo = daoInline.getDicInfo("FWYT");
			}
			else if ("TD".equals(bdcdylx))
			{
				dicInfo = daoInline.getDicInfo("TDYT");
			}
			if (jsonExtend != null && jsonExtend.containsKey("bdcdys"))
			{
				JSONArray bdcArry = jsonExtend.getJSONArray("bdcdys");
				bdc_rowspan = bdcArry.size() * bdc_rowspan;
				for (int i = 0; i < bdcArry.size(); i++)
				{
					JSONObject jsonBdc = (JSONObject) bdcArry.get(i);
					String yt = jsonBdc.getString("fwsyq_yt");
					jsonBdc.put("fwsyq_yt", dicInfo.getNameByCode(yt));
				}
			}
			// 计算不动产跨行数
			model.addAttribute("bdc_rowspan", bdc_rowspan);
			model.addAttribute("extend", jsonExtend);
			return this.root + "sqb_tyb";
		}
		return "";
	}

	/**
	 * 申请材料列表页面
	 *
	 * @param req
	 * @param model
	 * @param slsqId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/fujian", method = RequestMethod.GET)
	public String fujian(HttpServletRequest req, Model model, String slsqId) throws Exception
	{
		List<Pro_datuminst> datuminsts = daoInline.getDatuminsts(slsqId);
		List<DatuminstInfo> infos = daoInline.getByDatuminsts(datuminsts);
		model.addAttribute("slsqId", slsqId);
		model.addAttribute("infos", infos);
		return this.root + "fujian";
	}

	@RequestMapping(value = "/fwxx", method = RequestMethod.GET)
    public String fwxx(HttpServletRequest req,  Model model,String slsqId) throws Exception {
        if (slsqId == null || slsqId.isEmpty())
            throw new Exception("slsqId不能为空。");
        Pro_proinst slsq = daoInline.getProinst(slsqId);
		if (slsq == null)
			throw new Exception("申请受理项目不存在。");
		List<Pro_proposerinfo> sqrs = daoInline.getSqrsBySlsqId(slsq.getId());
		model.addAttribute("slsq", slsq);
    	List<Pro_fwxx> fwxxs = daoInline.getFwxxBySlsqId(slsq.getId());
    	if (fwxxs != null && fwxxs.size() > 0) {
    		req.setAttribute("fwxxs", fwxxs);
    		return this.root +"fwxx_ty";
    	}
        return "";
    }
	/**
	 * 审核意见
	 * @param req
	 * @param model
	 * @param slsqId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/shyj", method = RequestMethod.GET)
	public String shxxInfo(HttpServletRequest req,  Model model,String slsqId) throws Exception {
		if (slsqId == null || slsqId.isEmpty())
			throw new Exception("slsqId不能为空。");
		Pro_proinst slsq = daoInline.getProinst(slsqId);
		if (slsq == null)
			throw new Exception("申请受理项目不存在。");
		model.addAttribute("slsq", slsq);
		List<Pro_slxmsh> shxxInfo = daoInline.getShxxBySlsqId(slsq.getId());
		String extend_data = slsq.getExtend_data();
		JSONObject jsonObject = JSONObject.parseObject(extend_data);
		JSONObject usermsg = jsonObject.getJSONObject("usermsg");
		if (!StringHelper.isEmpty(usermsg)) {
			req.setAttribute("usermsg", usermsg);
		}
		req.setAttribute("shxxInfo", shxxInfo);
		return this.root +"shxxInfo";
	}
	/**
	 * 审核意见
	 * @param req
	 * @param model
	 * @param slsqId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/shgcInfo", method = RequestMethod.GET)
	public String shgcInfo(HttpServletRequest req,  Model model,String slsqId) throws Exception {
		if (slsqId == null || slsqId.isEmpty())
			throw new Exception("slsqId不能为空。");
		Pro_proinst slsq = daoInline.getProinst(slsqId);
		if (slsq == null)
			throw new Exception("申请受理项目不存在。");
		model.addAttribute("slsq", slsq);
		List<Pro_slxmsh> shxxInfo = daoInline.getShxxBySlsqId(slsq.getId());
		req.setAttribute("shgcInfo", shxxInfo);
		return this.root +"shgcInfo";
	}
	
	
	/**
	 * 预览界面
	 *
	 * @param req
	 * @param rep
	 * @param fileId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/fujian/viewer", method = RequestMethod.GET)
	public void fujian_view(HttpServletRequest req, HttpServletResponse rep, String fileId) throws Exception
	{
		Pro_attachment file = daoInline.getFileId(fileId);
		if (file == null)
			throw new Exception("文件不存在。");
//		ByteArrayOutputStream outputStream = 
				daoInline.getFileOutputStream(fileId,rep);
//		if (outputStream == null)
//			throw new Exception("文件不存在。");
//		OutputStream stream = rep.getOutputStream();
//		rep.setContentType("image/jpg");
//		stream.write(outputStream.toByteArray());
//		stream.flush();
//		stream.close();
	}

	@RequestMapping(value = "/fujian/viewimg", method = RequestMethod.GET)
	public String viewimg(HttpServletRequest req, HttpServletResponse rep, Model model, String datuminstId) throws Exception
	{
		List<Pro_attachment> files = daoInline.getFilesBydatuminst(datuminstId);
		 Map map = new HashMap();
	        for(Pro_attachment file : files) {
	        	map.put(file.getId(), file.getName()+file.getSuffix());
	        }
	     String jsonString = JSON.toJSONString(map);
		model.addAttribute("defaultId", files.isEmpty() ? "" : files.get(0).getId());
		model.addAttribute("files", files);
		model.addAttribute("pages", files.size());
		model.addAttribute("filename", jsonString);
		return this.root + "viewerimg";
	}

	/**
	 * 下载文件
	 *
	 * @param req
	 * @param rep
	 * @param fileId
	 * @throws Exception
	 */
	@RequestMapping(value = "/fujian/download", method = RequestMethod.GET)
	public void fujian_download(HttpServletRequest req, HttpServletResponse rep, String fileId) throws Exception
	{
		Pro_attachment file = daoInline.getFileId(fileId);
		if (file == null)
			throw new Exception("文件不存在。");
//		ByteArrayOutputStream outputStream = 
				daoInline.getFileOutputStream(fileId,rep);
//		if (outputStream == null)
//			throw new Exception("文件不存在。");
//		Pro_datuminst datuminst = daoInline.getDatuminstByFileId(fileId);
//		OutputStream stream = rep.getOutputStream();
//		String filename = URLEncoder.encode(datuminst.getName() + file.getSuffix(), "utf-8");
//		rep.addHeader("Content-Disposition", "attachment;filename=" + filename);
//		rep.setContentType("application/octet-stream");
//		stream.write(outputStream.toByteArray());
//		stream.flush();
//		stream.close();
	}

	/*
	 * 执行查询sql语句，返回封装了EasyuiDataGrid数据结果的JsonMessage对象。
	 */
	@RequestMapping(value = "/queryEasyuiDatagrid", method = RequestMethod.POST)
	public @ResponseBody JsonMessage queryEasyuiDatagrid(HttpServletRequest request, HttpServletResponse response, String sql)
	{
		JsonMessage result = new JsonMessage();
		try
		{
			result.setData(inlineprojectService.QueryEasyuiDataGrid(sql));
			result.setState(true);
		}
		catch (Exception ex)
		{
			result.setState(false);
			result.setMsg(ex.getMessage());
		}
		return result;
	}

	/*
	 * 执行查询sql语句，返回封装了PageResult<EasyuiDataGrid>数据结果的JsonMessage对象。
	 */
	@RequestMapping(value = "/queryPageEasyuiDatagrid", method = RequestMethod.POST)
	public @ResponseBody JsonMessage queryPageEasyuiDatagrid(HttpServletRequest request, HttpServletResponse response, String sql, int pageIndex, int pageSize)
	{
		JsonMessage result = new JsonMessage();
		try
		{
			result.setData(inlineprojectService.QueryEasyuiDataGrid(sql, pageIndex, pageSize));
			result.setState(true);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			result.setState(false);
			result.setMsg(ex.getMessage());
		}
		return result;
	}


	@RequestMapping(value = "/qjdcAccept", method = RequestMethod.POST)
	public @ResponseBody JsonMessage qjdcAccept(HttpServletRequest request, HttpServletResponse response, String slsqId, String shyj)
	{
		JsonMessage msg = new JsonMessage();
		try
		{
			acceptProjectService.qjdcAccept(slsqId, shyj);
			msg.setState(true);
			msg.setMsg("操作成功。");
		}
		catch (Exception ex)
		{
			msg.setState(false);
			msg.setMsg(ex.getMessage());
			ex.printStackTrace();
		}
		return msg;
	}

	/**
	 * 通过流程编码获取流程信息
	 * 
	 * @param request
	 * @param response
	 * @param code
	 * @return
	 */
	@RequestMapping(value = "/getProdefByCode")
	public @ResponseBody JsonMessage getProdefByCode(HttpServletRequest request, HttpServletResponse response, String code)
	{
		JsonMessage msg = new JsonMessage();
		try
		{
			msg.setData(acceptProjectService.getProdefById(code));
			msg.setState(true);
		}
		catch (Exception ex)
		{
			msg.setState(false);
			msg.setMsg(ex.getMessage());
			ex.printStackTrace();
		}
		return msg;
	}

	/**
	 * 受理项目
	 * 
	 * @param request
	 * @param response
	 * @param slsqId
	 * @param info
	 * @return
	 */
	@RequestMapping(value = "/accectProject", method = RequestMethod.POST)
	public @ResponseBody JsonMessage accectProject(HttpServletRequest request, HttpServletResponse response, String slsqId, SmProInfo info,String isshowbtn,String ispass,String sftg,String shhyj)
	{
		JsonMessage msg = new JsonMessage();
		try {
			msg = acceptProjectService.accectProject(request, slsqId, info, isshowbtn,ispass,sftg,shhyj);
		} catch (Exception ex) {
			msg.setState(false);
			msg.setMsg(ex.getMessage());
			ex.printStackTrace();
		}
		return msg;
	}

	/**
	 * 驳回项目
	 *
	 * @param request
	 * @param response
	 * @param slsqId
	 * @param shyj
	 * @return
	 */
	@RequestMapping(value = "/backProject", method = RequestMethod.POST)
	public @ResponseBody JsonMessage backProject(HttpServletRequest request, HttpServletResponse response, String slsqId, String shyj)
	{
		JsonMessage msg = new JsonMessage();
		try
		{
			acceptProjectService.backProject(slsqId, shyj);
			msg.setState(true);
			msg.setMsg("操作成功。");
		}
		catch (Exception ex)
		{
			msg.setState(false);
			msg.setMsg(ex.getMessage());
			ex.printStackTrace();
		}
		return msg;
	}

	/**
	 * 保存审批意见
	 *
	 * @param request
	 * @param response
	 * @param id
	 * @param spyj
	 * @return
	 */
	@RequestMapping(value = "/saveSpyj", method = RequestMethod.POST)
	public @ResponseBody JsonMessage saveSpyj(HttpServletRequest request, HttpServletResponse response, String id, String spyj)
	{
		JsonMessage msg = new JsonMessage();
		try
		{
			msg.setData(inlineprojectService.saveOrUpdateSpyj(id, spyj).getSpmb_Id());
			msg.setState(true);
			msg.setMsg("操作成功。");
		}
		catch (Exception ex)
		{
			msg.setState(false);
			msg.setMsg(ex.getMessage());
			ex.printStackTrace();
		}
		return msg;
	}

	/**
	 * 删除审批意见
	 *
	 * @param request
	 * @param response
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/delSpyj", method = RequestMethod.POST)
	public @ResponseBody JsonMessage delSpyj(HttpServletRequest request, HttpServletResponse response, String id)
	{
		JsonMessage msg = new JsonMessage();
		try
		{
			inlineprojectService.delSpyj(id);
			msg.setState(true);
			msg.setMsg("操作成功。");
		}
		catch (Exception ex)
		{
			msg.setState(false);
			msg.setMsg(ex.getMessage());
			ex.printStackTrace();
		}
		return msg;
	}

}
