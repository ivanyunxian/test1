package com.supermap.realestate.registration.web;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.supermap.luzhouothers.service.SmCompactService;
import com.supermap.realestate.registration.ViewClass.ProjectInfo;
import com.supermap.realestate.registration.ViewClass.UnitTree;
import com.supermap.realestate.registration.model.BDCS_C_GZ;
import com.supermap.realestate.registration.model.BDCS_C_XZ;
import com.supermap.realestate.registration.model.BDCS_DJDY_GZ;
import com.supermap.realestate.registration.model.BDCS_H_GZ;
import com.supermap.realestate.registration.model.BDCS_H_LS;
import com.supermap.realestate.registration.model.BDCS_H_LSY;
import com.supermap.realestate.registration.model.BDCS_H_XZ;
import com.supermap.realestate.registration.model.BDCS_H_XZY;
import com.supermap.realestate.registration.model.BDCS_SLLM_GZ;
import com.supermap.realestate.registration.model.BDCS_SYQZD_GZ;
import com.supermap.realestate.registration.model.BDCS_XMXX;
import com.supermap.realestate.registration.model.BDCS_ZH_GZ;
import com.supermap.realestate.registration.model.BDCS_ZRZ_GZ;
import com.supermap.realestate.registration.model.BDCS_ZRZ_XZ;
import com.supermap.realestate.registration.model.DCS_DCXM;
import com.supermap.realestate.registration.model.DCS_H_GZ;
import com.supermap.realestate.registration.model.DCS_SHYQZD_GZ;
import com.supermap.realestate.registration.model.DCS_SLLM_GZ;
import com.supermap.realestate.registration.model.DCS_SYQZD_GZ;
import com.supermap.realestate.registration.model.DCS_ZH_GZ;
import com.supermap.realestate.registration.model.DCS_ZRZ_GZ;
import com.supermap.realestate.registration.model.interfaces.Forest;
import com.supermap.realestate.registration.model.interfaces.RealUnit;
import com.supermap.realestate.registration.service.DBService;
import com.supermap.realestate.registration.service.DYService;
import com.supermap.realestate.registration.service.ExtractDataService;
import com.supermap.realestate.registration.service.XMLService;
import com.supermap.realestate.registration.service.impl.share.ShareTool;
import com.supermap.realestate.registration.tools.NewLogTools;
import com.supermap.realestate.registration.tools.UnitTools;
import com.supermap.realestate.registration.util.ConfigHelper;
import com.supermap.realestate.registration.util.ConstValue;
import com.supermap.realestate.registration.util.ConstValue.BDCDYLX;
import com.supermap.realestate.registration.util.ConstValue.DJDYLY;
import com.supermap.realestate.registration.util.ConstValue.SF;
import com.supermap.realestate.registration.util.Global;
import com.supermap.realestate.registration.util.ProjectHelper;
import com.supermap.realestate.registration.util.RequestHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.realestate.registration.util.YwLogUtil;
import com.supermap.realestate_gx.registration.service.impl.QueryServiceImpl;
import com.supermap.wisdombusiness.framework.web.UserController;
import com.supermap.wisdombusiness.web.Message;
import com.supermap.wisdombusiness.web.ResultMessage;
import com.supermap.wisdombusiness.web.ui.Page;
import com.supermap.yingtanothers.service.ExtractAttachment;
import com.supermap.yingtanothers.service.QueryShareXxService;

/**
 * 
 * @Description:单元控制器 跟不动产单元操作相关的都放在这里边
 * @author 刘树峰
 * @date 2015年6月12日 上午11:45:12
 * @Copyright SuperMap
 */
@Controller
@RequestMapping("/djdy")
public class DYController {

	/**
	 * DYService
	 */
	@Autowired
	private DYService dyService;

	@Autowired
	private XMLService xmlService;
	private static final Log logger = LogFactory.getLog(UserController.class);

	private static final Object String = null;
	@Autowired
	private ExtractDataService extractDataService;
	@Autowired
	private ExtractDataService extractDataForSJZService;
	@Autowired
	private ExtractDataService extractDataForWLMQService;
	@Autowired
	private com.supermap.wisdombusiness.framework.dao.impl.CommonDao baseCommonDao;
	@Autowired
	private ExtractAttachment e_ExtractAttachment;
	@Autowired
	private QueryShareXxService q_QueryShareXxService;
	@Autowired
	private ExtractDataService extractDataForGXService;
//	@Autowired
//	private ExtractDataService extractDataForJLService;
	@Autowired
	private ShareTool shareTool;

	/**
	 * 读取合同服务 增加by lxk CreateTime 2015年10月31日22:17:00
	 */
	@Autowired
	private SmCompactService smCompactService;
	@Autowired
	private DBService dbService;
	@Autowired
	private QueryServiceImpl queryServiceImpl_gx;

	/**
	 * 分页查询调查项目（URL:"/{xmbh}/dcxmsData",Method：GET）
	 * 
	 * @作者 李桐
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "/{xmbh}/dcxmsData", method = RequestMethod.GET)
	public @ResponseBody Message QueryDCXMS(@PathVariable("xmbh") String xmbh, HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException {
		// TODO @刘树峰 分页查询调查项目，需要优化
		ProjectInfo info = ProjectHelper.GetPrjInfoByXMBH(xmbh);
		Integer page = 1;
		if (request.getParameter("page") != null) {
			page = Integer.parseInt(request.getParameter("page"));
		}
		Integer rows = 10;
		if (request.getParameter("rows") != null) {
			rows = Integer.parseInt(request.getParameter("rows"));
		}
		Map<String, Object> mapCondition = new HashMap<String, Object>();
		if (request.getParameter("id") != null) {
			mapCondition.put("id", request.getParameter("id"));
		}
		if (request.getParameter("dcxmmc") != null) {
			mapCondition.put("dcxmmc", URLDecoder.decode(request.getParameter("dcxmmc"), "UTF-8"));
		}
		if (!StringUtils.isEmpty(info) && !StringUtils.isEmpty(info.getBdcdylx())) {
			if (info.getBdcdylx().equals(BDCDYLX.H.Value)) {
				mapCondition.put("xmlb", BDCDYLX.ZRZ.Value);
			} else {
				mapCondition.put("xmlb", info.getBdcdylx());
			}
		}
		String bdcdylx = info.getBdcdylx();

		List<DCS_DCXM> list = dyService.getPagedDcxm(page, rows, mapCondition, bdcdylx);
		Message m = new Message();
		m.setTotal(list.size());
		m.setRows(list);
		return m;
	}

	/**
	 * 获取调查项目信息（URL:"/{xmbh}/dqdcxm",Method：GET）
	 * 
	 * @作者 李桐
	 * @创建时间 2015年6月9日上午4:48:53
	 * @param xmbh
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/{xmbh}/dqdcxm", method = RequestMethod.GET)
	public @ResponseBody ResultMessage GetDcxmInfo(@PathVariable("xmbh") String xmbh, HttpServletRequest request,
			HttpServletResponse response) {
		ResultMessage message = new ResultMessage();
		int xmxxCount = dyService.getXMXXCountById(xmbh);
		if (xmxxCount == 0) {// 说明调查库调查项目和项目信息库中没有匹配项目
			message.setMsg("0");
		} else {
			message.setMsg("1");
		}
		YwLogUtil.addYwLog("获取调查项目信息", ConstValue.SF.YES.Value, ConstValue.LOG.SEARCH);
		return message;
	}

	/**
	 * 更新调查项目信息（URL:"/{xmbh}/dqdcxm/{dcxmid}",Method：POST）
	 * 
	 * @作者 李桐
	 * @创建时间 2015年6月9日上午4:49:13
	 * @param xmbh
	 * @param dcxmid
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/{xmbh}/dqdcxm/{dcxmid}", method = RequestMethod.POST)
	public @ResponseBody ResultMessage UpdateDcxmInfo(@PathVariable String xmbh, @PathVariable String dcxmid,
			HttpServletRequest request, HttpServletResponse response) {
		ResultMessage message = new ResultMessage();
		message = dyService.updateXmxx(xmbh, dcxmid);
		return message;
	}

	/**
	 * 分页查询调查库使用权宗地（URL:"/{xmbh}/dcshyqzds",Method：GET）
	 * 
	 * @作者 李桐
	 * @param xmbh
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "/{xmbh}/dcshyqzds", method = RequestMethod.GET)
	public @ResponseBody Message Queryshyqzds(@PathVariable("xmbh") String xmbh, HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException {
		// TODO @刘树峰 分页查询调查库使用权宗地可以被选择器替代，此方法暂时不用了，先放着
		request.setCharacterEncoding("utf-8");
		Integer page = 1;
		if (request.getParameter("page") != null) {
			page = Integer.parseInt(request.getParameter("page"));
		}
		Integer rows = 10;
		if (request.getParameter("rows") != null) {
			rows = Integer.parseInt(request.getParameter("rows"));
		}
		String zdzl = request.getParameter("ZDZL");
		String zddm = request.getParameter("ZDDM");
		String bdcdyh = request.getParameter("BDCDYH");
		DCS_SHYQZD_GZ shyqzd = new DCS_SHYQZD_GZ();
		zdzl = new String(zdzl.getBytes("iso8859-1"), "utf-8");
		zddm = new String(zddm.getBytes("iso8859-1"), "utf-8");
		bdcdyh = new String(bdcdyh.getBytes("iso8859-1"), "utf-8");
		shyqzd.setZL(zdzl);
		shyqzd.setZDDM(zddm);
		shyqzd.setBDCDYH(bdcdyh);
		Message m = dyService.QueryDCKSHYQzd(xmbh, page, rows, shyqzd);
		return m;
	}

	/**
	 * 分页查询调查库所有权宗地（URL:"/{xmbh}/dcsyqzds",Method：GET）
	 * 
	 * @作者 刘树峰
	 * @param xmbh
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/{xmbh}/dcsyqzds", method = RequestMethod.GET)
	public @ResponseBody Message Querysyqzds(@PathVariable("xmbh") String xmbh, HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException {
		// TODO @刘树峰 可以被选择器替代，此方法暂时不用了，先放着
		Integer page = 1;
		if (request.getParameter("page") != null) {
			page = Integer.parseInt(request.getParameter("page"));
		}
		Integer rows = 10;
		if (request.getParameter("rows") != null) {
			rows = Integer.parseInt(request.getParameter("rows"));
		}
		String zdzl = request.getParameter("ZDZL");
		zdzl = new String(zdzl.getBytes("iso8859-1"), "utf-8");
		String zddm = request.getParameter("ZDDM");
		zddm = new String(zddm.getBytes("iso8859-1"), "utf-8");
		String bdcdyh = request.getParameter("BDCDYH");
		bdcdyh = new String(bdcdyh.getBytes("iso8859-1"), "utf-8");
		String dcxmbh = request.getParameter("DCXMBH");
		dcxmbh = new String(dcxmbh.getBytes("iso8859-1"), "utf-8");
		DCS_SYQZD_GZ syqzd = new DCS_SYQZD_GZ();
		syqzd.setZL(zdzl);
		syqzd.setZDDM(zddm);
		syqzd.setBDCDYH(bdcdyh);
		syqzd.setDCXMID(dcxmbh);
		Message m = new Message();
		m = dyService.QueryDCKSYQzd(xmbh, page, rows, syqzd);
		return m;
	}

	/**
	 * 分页查询调查库宗海（URL:"/{xmbh}/dczh",Method：GET）
	 * 
	 * @作者 海豹
	 * @创建时间 2015年6月25日下午4:23:18
	 * @param xmbh
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/{xmbh}/dczh", method = RequestMethod.GET)
	public @ResponseBody Message Querydczh(@PathVariable("xmbh") String xmbh, HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException {
		// TODO @刘树峰 可以被选择器替代，此方法暂时不用了，先放着
		Integer page = 1;
		if (request.getParameter("page") != null) {
			page = Integer.parseInt(request.getParameter("page"));
		}
		Integer rows = 10;
		if (request.getParameter("rows") != null) {
			rows = Integer.parseInt(request.getParameter("rows"));
		}
		String zhzl = request.getParameter("ZHZL");
		zhzl = new String(zhzl.getBytes("iso8859-1"), "utf-8");
		String zhdm = request.getParameter("ZHDM");
		zhdm = new String(zhdm.getBytes("iso8859-1"), "utf-8");
		String bdcdyh = request.getParameter("BDCDYH");
		bdcdyh = new String(bdcdyh.getBytes("iso8859-1"), "utf-8");
		String dcxmbh = request.getParameter("DCXMBH");
		dcxmbh = new String(dcxmbh.getBytes("iso8859-1"), "utf-8");
		DCS_ZH_GZ zh = new DCS_ZH_GZ();
		zh.setZL(zhzl);
		zh.setZHDM(zhdm);
		zh.setBDCDYH(bdcdyh);
		zh.setDCXMID(dcxmbh);
		Message m = new Message();
		m = dyService.QueryDCKZH(xmbh, page, rows, zh);
		return m;
	}

	/**
	 * 分页查询调查库房屋（户）（URL:"/{xmbh}/dcfws",Method：GET）
	 * 
	 * @作者 刁立伟
	 * @param xmbh
	 * @param request
	 * @param response
	 * @author diaoliwei
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "/{xmbh}/dcfws", method = RequestMethod.GET)
	public @ResponseBody Message Queryfws(@PathVariable("xmbh") String xmbh, HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException {
		// TODO @刘树峰 可以被选择器替代，此方法暂时不用了，先放着
		Integer page = 1;
		if (request.getParameter("page") != null) {
			page = Integer.parseInt(request.getParameter("page"));
		}
		Integer rows = 10;
		if (request.getParameter("rows") != null) {
			rows = Integer.parseInt(request.getParameter("rows"));
		}
		String fwzl = request.getParameter("FWZL");
		String fwdyh = request.getParameter("FWDYH");
		fwzl = new String(fwzl.getBytes("iso8859-1"), "utf-8");
		fwdyh = new String(fwdyh.getBytes("iso8859-1"), "utf-8");
		DCS_H_GZ h = new DCS_H_GZ();
		h.setZL(fwzl);
		h.setBDCDYH(fwdyh);
		Message m = new Message();
		m = dyService.QueryDCKfws(xmbh, page, rows, h);
		return m;
	}

	/**
	 * 分页查询调查库自然幢（URL:"/{xmbh}/dczrzs",Method：GET）
	 * 
	 * @作者 刁立伟
	 * @param xmbh
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "/{xmbh}/dczrzs", method = RequestMethod.GET)
	public @ResponseBody Message Queryzrzs(@PathVariable("xmbh") String xmbh, HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException {
		Integer page = 1;
		if (request.getParameter("page") != null) {
			page = Integer.parseInt(request.getParameter("page"));
		}
		Integer rows = 10;
		if (request.getParameter("rows") != null) {
			rows = Integer.parseInt(request.getParameter("rows"));
		}
		DCS_ZRZ_GZ zrz = new DCS_ZRZ_GZ();
		zrz.setZL(RequestHelper.getParam(request, "FWZL"));
		zrz.setBDCDYH(request.getParameter("FWDYH"));
		Message m = new Message();
		m = dyService.QueryDCKzrzs(xmbh, page, rows, zrz);
		return m;
	}

	/**
	 * 分页查询调查林地（URL:"/{xmbh}/dcld",Method：GET）
	 * 
	 * @作者 海豹
	 * @创建时间 2015年6月27日下午4:31:40
	 * @param xmbh
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "/{xmbh}/dcld", method = RequestMethod.GET)
	public @ResponseBody Message Queryld(@PathVariable("xmbh") String xmbh, HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException {
		// TODO @刘树峰 可以被选择器替代，此方法暂时不用了，先放着
		Integer page = 1;
		if (request.getParameter("page") != null) {
			page = Integer.parseInt(request.getParameter("page"));
		}
		Integer rows = 10;
		if (request.getParameter("rows") != null) {
			rows = Integer.parseInt(request.getParameter("rows"));
		}
		DCS_SLLM_GZ ld = new DCS_SLLM_GZ();
		String zl = request.getParameter("ZL");
		zl = new String(zl.getBytes("iso8859-1"), "utf-8");
		ld.setZL(zl);
		String dyh = request.getParameter("DYH");
		dyh = new String(dyh.getBytes("iso8859-1"), "utf-8");
		ld.setBDCDYH(dyh);
		Message m = new Message();
		m = dyService.QueryDCKld(xmbh, page, rows, ld);
		return m;
	}

	/**
	 * 获取项目不动产单元列表（URL:"/{xmbh}/djdys",Method：GET）
	 * 
	 * @作者 刁立伟
	 * @param xmbh
	 * @param bdcdyid
	 * @param request
	 * @param response
	 * @author diaoliwei
	 * @return
	 */
	@RequestMapping(value = "/{xmbh}/djdys", method = RequestMethod.GET)
	public @ResponseBody Message GetDJDYS(@PathVariable("xmbh") String xmbh, HttpServletRequest request,
			HttpServletResponse response) {
		double startTime = System.currentTimeMillis();
		Message message = new Message();
		List<UnitTree> listtree;
		// TODO-取得当前站点根目录，没用删除掉
		request.getContextPath();
		listtree = dyService.getDJDYS(xmbh);
		if (null != listtree) {
			message.setTotal(listtree.size());
			message.setRows(listtree);
		} else {
			message.setTotal(0);
		}

		double endTime = System.currentTimeMillis();
		System.out.println("获取单元列表用时:" + (endTime - startTime));
		return message;
	}

	
	/**
	 * 添加一个登记单元（URL:"/{xmbh}/djdys/{bdcdyid}",Method：POST）
	 * 
	 * @作者 刁立伟
	 * @param xmbh
	 * @param bdcdyid
	 * @param request
	 * @param response
	 * @author diaoliwei
	 * @return
	 */
	@RequestMapping(value = "/{xmbh}/djdys/{bdcdyid}", method = RequestMethod.POST)
	public @ResponseBody ResultMessage AddBDCDY(@PathVariable("xmbh") String xmbh,
			@PathVariable("bdcdyid") String bdcdyid, HttpServletRequest request, HttpServletResponse response) {
		ResultMessage resultMessage = new ResultMessage();
		bdcdyid = request.getParameter("bdcdyid");
		bdcdyid = request.getParameter("ids");
		resultMessage = dyService.addBDCDY(xmbh, bdcdyid);
		YwLogUtil.addYwLog("添加单元", ConstValue.SF.YES.Value, ConstValue.LOG.ADD);
		if("true".equals(resultMessage.getSuccess())){
			try {
//				logger.info("选中单元开始推送");
				dbService.SendMsg(xmbh, "100");
			} catch (Exception e) {
				e.printStackTrace();
				logger.info("选中单元推送报错");
			}
		}
		return resultMessage;
	}

	/**
	 * 添加一个登记单元（URL:"/{xmbh}/djdys/{bdcdyid}",Method：POST）
	 * 
	 * @作者 刁立伟
	 * @param xmbh
	 * @param bdcdyid
	 * @param request
	 * @param response
	 * @author diaoliwei
	 * @return
	 */
	@RequestMapping(value = "/{xmbh}/djdys/{bdcdyid}/nocheck", method = RequestMethod.POST)
	public @ResponseBody ResultMessage AddBDCDYNoCheck(@PathVariable("xmbh") String xmbh,
			@PathVariable("bdcdyid") String bdcdyid, HttpServletRequest request, HttpServletResponse response) {
		ResultMessage resultMessage = new ResultMessage();
		bdcdyid = request.getParameter("ids");
		resultMessage = dyService.addBDCDYNoCheck(xmbh, bdcdyid);
		if("true".equals(resultMessage.getSuccess())){
			try {
//				logger.info("选中单元开始推送");
				dbService.SendMsg(xmbh, "100");
			} catch (Exception e) {
				e.printStackTrace();
				logger.info("选中单元推送报错");
			}
		}
		return resultMessage;
	}

	/**
	 * (工作层)根据不动产单元ID获取房屋（户）信息（URL:"/djdys/{bdcdyid}",Method：GET）
	 * 
	 * @作者 刁立伟
	 * @param bdcdyid
	 * @param request
	 * @param response
	 * @author diaoliwei
	 * @return
	 */
	@RequestMapping(value = "/djdys/{bdcdyid}", method = RequestMethod.GET)
	public @ResponseBody Message GetBDCDYInfo(@PathVariable("bdcdyid") String bdcdyid, HttpServletRequest request,
			HttpServletResponse response) {
		Message message = new Message();
		BDCS_H_GZ h = dyService.GetFwInfo(bdcdyid); // 房屋信息
		List<BDCS_H_GZ> list = new ArrayList<BDCS_H_GZ>();
		if (null != h) {
			list.add(h);
		}
		message.setRows(list);
		message.setTotal(list.size());
		return message;
	}

	/**
	 * 移除某个不动产单元（URL:"/{xmbh}/djdys/{bdcdyid}",Method：DELETE）
	 * 
	 * @作者 刁立伟
	 * @param xmbh
	 * @param bdcdyid
	 * @param request
	 * @param response
	 * @author diaoliwei
	 * @return
	 */
	@RequestMapping(value = "/{xmbh}/djdys/{bdcdyid}", method = RequestMethod.DELETE)
	public @ResponseBody ResultMessage DeleteBDCDY(@PathVariable("xmbh") String xmbh,
			@PathVariable("bdcdyid") String bdcdyid, HttpServletRequest request, HttpServletResponse response) {
		ResultMessage resultMessage = new ResultMessage();
		BDCS_XMXX xmxx = Global.getXMXXbyXMBH(xmbh);
		if (SF.YES.Value.equals(xmxx.getSFDB())) {
			resultMessage.setSuccess("false");
			resultMessage.setMsg("项目已经登簿，不能再移除单元！");
			return resultMessage;
		}
		 com.alibaba.fastjson.JSONObject jsondeldys=NewLogTools.getJSONByXMBH(xmbh);
		 jsondeldys.put("OperateType", "删除单元");
		 com.alibaba.fastjson.JSONObject jsondeldy=new com.alibaba.fastjson.JSONObject();		
		 boolean bsuccess = dyService.removeBDCDY(xmbh, bdcdyid);
		 jsondeldy.put("ID", bdcdyid);
		 jsondeldy.put("msg", bsuccess);
		 jsondeldys.put("序号:(1)", jsondeldy);
         NewLogTools.saveLog(jsondeldys.toString(), xmbh, "2", "删除单元");
		if (bsuccess) {
			resultMessage.setSuccess("true");
			resultMessage.setMsg("删除成功!");
			YwLogUtil.addYwLog("删除单元", ConstValue.SF.YES.Value, ConstValue.LOG.DELETE);
		} else {
			YwLogUtil.addYwLog("删除单元", ConstValue.SF.NO.Value, ConstValue.LOG.DELETE);
		}
		return resultMessage;
	}

	/**
	 * 移除某个不动产单元（URL:"/{xmbh}/djdys/{bdcdyid}",Method：DELETE）
	 * 
	 * @作者 刁立伟
	 * @param xmbh
	 * @param bdcdyid
	 * @param request
	 * @param response
	 * @author diaoliwei
	 * @return
	 */
	@RequestMapping(value = "/{xmbh}/djdys/", method = RequestMethod.POST)
	public @ResponseBody ResultMessage DeleteBDCDYS(@PathVariable("xmbh") String xmbh, HttpServletRequest request,
			HttpServletResponse response) {
		ResultMessage resultMessage = new ResultMessage();
		String bdcdyids = request.getParameter("bdcdyids");
		if (!StringHelper.isEmpty(bdcdyids)) {
			if(bdcdyids.contains("gydtype") ||bdcdyids.contains("xydtype")){
				boolean bsuccess = false;
				bsuccess = dyService.removeBDCDY(xmbh, bdcdyids);
				if (bsuccess) {
					resultMessage.setSuccess("true");
					resultMessage.setMsg("删除成功!");
					YwLogUtil.addYwLog("删除单元-成功", ConstValue.SF.YES.Value, ConstValue.LOG.DELETE);
				} else {
					resultMessage.setSuccess("false");
					resultMessage.setMsg("删除失败!");
					YwLogUtil.addYwLog("删除单元-失败", ConstValue.SF.NO.Value, ConstValue.LOG.DELETE);
				}
			}else{
				String[] ids = bdcdyids.split(",");
				if (ids != null && ids.length > 0) {
					boolean bsuccess = false;
					 com.alibaba.fastjson.JSONObject jsondeldys=NewLogTools.getJSONByXMBH(xmbh);
					 jsondeldys.put("OperateType", "删除单元");
					 int temp=0;
					for (String id : ids) {
						if (!StringHelper.isEmpty(id)) {
							bsuccess = dyService.removeBDCDY(xmbh, id);
							temp++;
							 com.alibaba.fastjson.JSONObject jsondeldy=new com.alibaba.fastjson.JSONObject();
							 jsondeldy.put("ID", id);
							 jsondeldy.put("msg", bsuccess);
							 jsondeldys.put("序号:("+temp+")", jsondeldy);
						}
					}
					NewLogTools.saveLog(jsondeldys.toString(), xmbh, "2", "删除单元");
					
			/*******************2017年7月5日15:02:34 调用日志接口，存储被删除掉的日志操作步骤。****************************/
					if (bsuccess) {
						resultMessage.setSuccess("true");
						resultMessage.setMsg("删除成功!");
						YwLogUtil.addYwLog("删除单元-成功", ConstValue.SF.YES.Value, ConstValue.LOG.DELETE);
					} else {
						YwLogUtil.addYwLog("删除单元-失败", ConstValue.SF.NO.Value, ConstValue.LOG.DELETE);
					}
				}
			}
			
		}
		/******************************************************************************/
		return resultMessage;
	}

	/**
	 * (工作层)根据不动产单元ID获取自然幢信息（URL:"/djdys/{bdcdyid}",Method：GET）
	 * 
	 * @作者 刘树峰
	 * @创建时间 2015年6月9日上午4:45:33
	 * @param bdcdyid
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/zrz/{bdcdyid}", method = RequestMethod.GET)
	public @ResponseBody Message GetZRZInfo(@PathVariable("bdcdyid") String bdcdyid, HttpServletRequest request,
			HttpServletResponse response) {
		Message message = new Message();
		BDCS_ZRZ_GZ zrz = dyService.GetZRZInfo(bdcdyid);
		List<BDCS_ZRZ_GZ> list = new ArrayList<BDCS_ZRZ_GZ>();
		list.add(zrz);
		message.setTotal(1);
		message.setRows(list);
		return message;
	}

	/**
	 * (工作层)根据层ID获取层信息（URL:"/c/{cid}",Method：GET）
	 * 
	 * @作者 刘树峰
	 * @创建时间 2015年6月9日上午4:51:28
	 * @param cid
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/c/{cid}", method = RequestMethod.GET)
	public @ResponseBody Message GetCInfo(@PathVariable("cid") String cid, HttpServletRequest request,
			HttpServletResponse response) {
		Message message = new Message();
		BDCS_C_GZ c = dyService.GetCInfo(cid);
		List<BDCS_C_GZ> list = new ArrayList<BDCS_C_GZ>();
		if (null != c) {
			list.add(c);
		}
		message.setTotal(list.size());
		message.setRows(list);
		return message;
	}

	/**
	 * (工作层)根据不动产单元ID获取使用权宗地信息（URL:"/shyqzd/{bdcdyid}",Method：GET）
	 * 
	 * @作者 刘树峰
	 * @创建时间 2015年6月9日上午4:52:55
	 * @param bdcdyid
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/shyqzd/{bdcdyid}", method = RequestMethod.GET)
	public @ResponseBody Message GetSHYQZDInfo(@PathVariable("bdcdyid") String bdcdyid, HttpServletRequest request,
			HttpServletResponse response) {
		Message message = new Message();
		RealUnit unit = UnitTools.loadUnit(BDCDYLX.SHYQZD, DJDYLY.GZ, bdcdyid);
		List<RealUnit> list = new ArrayList<RealUnit>();
		if (null != unit) {
			list.add(unit);
		}
		message.setTotal(list.size());
		message.setRows(list);
		return message;
	}

	/**
	 * (工作层)根据不动产单元ID获取林地信息（URL:"/shyld/{bdcdyid}",Method：GET）
	 * 
	 * @Title: GetSHYldInfo
	 * @author:liushufeng
	 * @date：2015年7月18日 下午9:46:26
	 * @param bdcdyid
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/shyld/{bdcdyid}", method = RequestMethod.GET)
	public @ResponseBody Message GetSHYldInfo(@PathVariable("bdcdyid") String bdcdyid, HttpServletRequest request,
			HttpServletResponse response) {
		Message message = new Message();
		BDCS_SLLM_GZ shyqld = dyService.GetSHYQLDInfo(bdcdyid);
		List<BDCS_SLLM_GZ> list = new ArrayList<BDCS_SLLM_GZ>();
		if (null != shyqld) {
			list.add(shyqld);
		}
		message.setTotal(list.size());
		message.setRows(list);
		return message;
	}

	/**
	 * 获取现状层林地信息
	 * 
	 * @Title: GetXZSHYldInfo
	 * @author:liushufeng
	 * @date：2015年10月10日 下午5:26:52
	 * @param bdcdyid
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/xzshyld/{bdcdyid}", method = RequestMethod.GET)
	public @ResponseBody Message GetXZSHYldInfo(@PathVariable("bdcdyid") String bdcdyid, HttpServletRequest request,
			HttpServletResponse response) {
		Message message = new Message();
		RealUnit unit = UnitTools.loadUnit(BDCDYLX.LD, DJDYLY.XZ, bdcdyid);
		Forest forest = null;
		if (unit instanceof Forest) {
			forest = (Forest) unit;
		}
		List<Forest> list = new ArrayList<Forest>();
		if (null != forest) {
			list.add(forest);
		}
		message.setTotal(list.size());
		message.setRows(list);
		return message;
	}

	/**
	 * （工作层）根据不动产单元ID获取所有权宗地信息（URL:"/syqzd/{bdcdyid}",Method：GET）
	 * 
	 * @作者 海豹
	 * @创建时间 2015年6月25日下午2:42:22
	 * @param bdcdyid
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/syqzd/{bdcdyid}", method = RequestMethod.GET)
	public @ResponseBody Message GetSYQZDInfo(@PathVariable("bdcdyid") String bdcdyid, HttpServletRequest request,
			HttpServletResponse response) {
		Message message = new Message();
		BDCS_SYQZD_GZ syqzd = dyService.GetSYQZDInfo(bdcdyid);
		List<BDCS_SYQZD_GZ> list = new ArrayList<BDCS_SYQZD_GZ>();
		if (list != null) {
			list.add(syqzd);
		}
		message.setTotal(list.size());
		message.setRows(list);
		return message;
	}

	/**
	 * （工作层）根据不动产单元ID获取宗海信息（URL:"zh/{bdcdyid}",Method：GET）
	 * 
	 * @作者 海豹
	 * @创建时间 2015年6月25日下午3:06:40
	 * @param bdcdyid
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/zh/{bdcdyid}", method = RequestMethod.GET)
	public @ResponseBody Message GetZHInfo(@PathVariable("bdcdyid") String bdcdyid, HttpServletRequest request,
			HttpServletResponse response) {
		// TODO @孙海豹 URL写的有问题（修改完成）
		Message message = new Message();
		BDCS_ZH_GZ zh = dyService.GetZHInfo(bdcdyid);
		List<BDCS_ZH_GZ> lst = new ArrayList<BDCS_ZH_GZ>();
		if (lst != null) {
			lst.add(zh);
		}
		message.setTotal(lst.size());
		message.setRows(lst);
		return message;
	}

	/**
	 * （工作层）根据不动产单元号获取宗海的用海状况（URL:"zhzk/{bdcdyid}",Method：GET）
	 * 
	 * @作者 海豹
	 * @创建时间 2015年6月25日下午6:08:49
	 * @param bdcdyid
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "zhzk/{bdcdyid}", method = RequestMethod.GET)
	public @ResponseBody Message GetYHZKBYBDCDYID(@PathVariable("bdcdyid") String bdcdyid, HttpServletRequest request,
			HttpServletResponse response) {
		Integer page = 1;
		if (request.getParameter("page") != null) {
			page = Integer.parseInt(request.getParameter("page"));
		}
		Integer rows = 100;
		if (request.getParameter("rows") != null) {
			rows = Integer.parseInt(request.getParameter("rows"));
		}
		Page yhzkPaged = dyService.GetPagedYHZK(bdcdyid, page, rows);
		Message m = new Message();
		m.setTotal(yhzkPaged.getTotalCount());
		m.setRows(yhzkPaged.getResult());
		return m;
	}

	/**
	 * （工作层） 根据不动产单元号获取宗海的用海坐标（URL:"zhzb/{bdcdyid}",Method：GET）
	 * 
	 * @作者 海豹
	 * @创建时间 2015年6月25日下午6:08:49
	 * @param bdcdyid
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "zhzb/{bdcdyid}", method = RequestMethod.GET)
	public @ResponseBody Message GetYHYDZBBYBDCDYID(@PathVariable("bdcdyid") String bdcdyid, HttpServletRequest request,
			HttpServletResponse response) {
		Integer page = 1;
		if (request.getParameter("page") != null) {
			page = Integer.parseInt(request.getParameter("page"));
		}
		Integer rows = 100;
		if (request.getParameter("rows") != null) {
			rows = Integer.parseInt(request.getParameter("rows"));
		}
		Page yhydzbPaged = dyService.GetPagedYHYDZB(bdcdyid, page, rows);
		Message m = new Message();
		m.setTotal(yhydzbPaged.getTotalCount());
		m.setRows(yhydzbPaged.getResult());
		return m;
	}

	/**
	 * (现状层)根据不动产单元ID获取使用权宗地信息（URL:"/xzshyqzd/{bdcdyid}",Method：GET）
	 * 
	 * @作者 diaoliwei
	 * @创建时间 2015年6月10日下午5:50:12
	 * @param bdcdyid
	 *            不动产单元ID
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/xzshyqzd/{bdcdyid}", method = RequestMethod.GET)
	public @ResponseBody Message GetXZSHYQZDInfo(@PathVariable("bdcdyid") String bdcdyid, HttpServletRequest request,
			HttpServletResponse response) {
		Message message = new Message();
		RealUnit unit = UnitTools.loadUnit(BDCDYLX.SHYQZD, DJDYLY.LS, bdcdyid);
		List<RealUnit> list = new ArrayList<RealUnit>();
		if (null != unit) {
			list.add(unit);
		}
		message.setTotal(list.size());
		message.setRows(list);
		return message;
	}

	/**
	 * （现状层）根据不动产单元ID获取自然幢信息（URL:"/xzzrz/{bdcdyid}",Method：GET）
	 * 
	 * @作者 diaoliwei
	 * @创建时间 2015年6月10日下午5:56:51
	 * @param bdcdyid
	 *            不动产单元ID
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/xzzrz/{bdcdyid}", method = RequestMethod.GET)
	public @ResponseBody Message GetXZZRZInfo(@PathVariable("bdcdyid") String bdcdyid, HttpServletRequest request,
			HttpServletResponse response) {
		Message message = new Message();
		RealUnit unit = UnitTools.loadUnit(BDCDYLX.ZRZ, DJDYLY.LS, bdcdyid);
		List<RealUnit> list = new ArrayList<RealUnit>();
		if (null != unit) {
			list.add(unit);
		}
		// BDCS_ZRZ_XZ xzZrz = dyService.GetXZZRZInfo(bdcdyid);
		// List<BDCS_ZRZ_XZ> xzList = new ArrayList<BDCS_ZRZ_XZ>();
		// if (null != xzZrz) {
		// xzList.add(xzZrz);
		// }
		message.setTotal(list.size());
		message.setRows(list);
		return message;
	}

	/**
	 * （现状层）根据不动产单元ID获取层信息（URL:"/xzc/{cid}",Method：GET）
	 * 
	 * @作者 diaoliwei
	 * @创建时间 2015年6月10日下午6:07:03
	 * @param cid
	 *            层id
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/xzc/{cid}", method = RequestMethod.GET)
	public @ResponseBody Message GetXZCInfo(@PathVariable("cid") String cid, HttpServletRequest request,
			HttpServletResponse response) {
		double startTime = System.currentTimeMillis();
		Message message = new Message();
		BDCS_C_XZ xzC = dyService.GetXZCInfo(cid);
		List<BDCS_C_XZ> xzList = new ArrayList<BDCS_C_XZ>();
		if (null != xzC) {
			xzList.add(xzC);
		}
		message.setRows(xzList);
		message.setTotal(xzList.size());
		System.out.println("获取(现状层)层信息用时:" + (System.currentTimeMillis() - startTime));
		return message;
	}

	/**
	 * （现状层）根据不动产单元ID获取房屋(户)信息（URL:"/xzdjdys/{bdcdyid}",Method：GET）
	 * 
	 * @作者 diaoliwei
	 * @创建时间 2015年6月10日下午6:12:41
	 * @param bdcdyid
	 *            不动产单元id
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/xzdjdys/{bdcdyid}", method = RequestMethod.GET)
	public @ResponseBody Message GetXZBDCDYInfo(@PathVariable("bdcdyid") String bdcdyid, HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException {
		double startTime = System.currentTimeMillis();
		Message message = new Message();
		String xmbh = RequestHelper.getParam(request, "xmbh");
		BDCDYLX bdcdylx = ProjectHelper.GetBDCDYLX(xmbh);
		if (bdcdylx.Value.equals(BDCDYLX.YCH.Value)) {
			BDCS_H_XZY bdcs_h_xzy = dyService.GetXzyFwInfo(bdcdyid);
			List<BDCS_H_XZY> list = new ArrayList<BDCS_H_XZY>();
			if (bdcs_h_xzy != null) {
				list.add(bdcs_h_xzy);
			}
			message.setRows(list);
			message.setTotal(list.size());
		} else {
			BDCS_H_XZ xzh = dyService.GetXZFwInfo(bdcdyid); // 现状房屋信息
			List<BDCS_H_XZ> list = new ArrayList<BDCS_H_XZ>();
			if (null != xzh) {
				list.add(xzh);
			}
			message.setRows(list);
			message.setTotal(list.size());
		}
		System.out.println("获取(现状层)房屋信息用时:" + (System.currentTimeMillis() - startTime));
		return message;
	}

	/**
	 * 维护 户信息（包括预测户、户、工作层、现状层）
	 * 
	 * @author diaoliwei
	 * @date 2015-8-5
	 * @param bdcdyid
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/{xmbh}/updateHouse/{bdcdyid}/{ly}", method = RequestMethod.POST)
	public @ResponseBody ResultMessage SaveXZBDCDYInfo(@PathVariable("xmbh") String xmbh,
			@PathVariable("bdcdyid") String bdcdyid, @PathVariable("ly") String ly, HttpServletRequest request) {
		ResultMessage resultMessage = new ResultMessage();
		BDCDYLX bdcdylx = ProjectHelper.GetBDCDYLX(xmbh);
		String dataString = request.getParameter("info");// 户信息
		if (dataString.indexOf("[") < 0) {
			dataString = "[" + dataString;
		}
		if (dataString.indexOf("]") < 0) {
			dataString = dataString + "]";
		}
		resultMessage = dyService.updateHouse(bdcdylx, ly, dataString, bdcdyid);
		YwLogUtil.addYwLog("修改户信息", ConstValue.SF.YES.Value, ConstValue.LOG.UPDATE);
		return resultMessage;
	}

	/**
	 * 分页查询选择转移信息，包括房屋，宗地（URL:"/xzdy",Method：GET）
	 * 
	 * @param xmbh
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "/xzdy", method = RequestMethod.GET)
	public @ResponseBody Message Queryxzzyfh_zd(HttpServletRequest request, HttpServletResponse response)
			throws UnsupportedEncodingException {
		// TODO @刘树峰 此方法可以被选择器代替，先放着不删除
		Integer page = 1;
		if (request.getParameter("page") != null) {
			page = Integer.parseInt(request.getParameter("page"));
		}
		Integer rows = 10;
		if (request.getParameter("rows") != null) {
			rows = Integer.parseInt(request.getParameter("rows"));
		}
		String type = RequestHelper.getParam(request, "type");// 类型：宗地，房屋-户
		String zl = request.getParameter("zl");// 坐落
		String qlr = request.getParameter("qlr");// 权利人
		String bdcqzh = request.getParameter("bdcqzh");// 不动产权证号
		String bdcfwbh = request.getParameter("bdcfwbh");// 房屋编号
		String bdczddm = request.getParameter("bdczddm");// 不动产宗地代码
		String bdcdyh = request.getParameter("bdcdyh");// 不动产单元号
		Message msg = new Message();

		// 房屋-户
		if (BDCDYLX.H.Value.equals(type)) {
			msg = dyService.GetXZZY_fw_Info(page, rows, zl, qlr, bdcqzh, bdcdyh, bdcfwbh);
		}
		// 使用权宗地
		if (BDCDYLX.SHYQZD.Value.equals(type)) {
			msg = dyService.GetXZZY_zd_Info(page, rows, zl, qlr, bdcqzh, bdcdyh, bdczddm);
		}
		if (BDCDYLX.YCH.Value.equals(type)) {
			msg = dyService.GetXZ_Hy_Info(page, rows, zl, qlr, bdcqzh, bdcdyh, bdcfwbh);
		}
		return msg;
	}

	/**
	 * 分页查询现状自然幢（URL："/xzzrzs",Method：GET） （选择单元时：查询分页的现状自然幢列表）
	 * 
	 * @作者 diaoliwei
	 * @创建时间 2015年6月23日上午11:31:14
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "/xzzrzs", method = RequestMethod.GET)
	public @ResponseBody Message QueryxzZRZs(HttpServletRequest request, HttpServletResponse response)
			throws UnsupportedEncodingException {
		Message msg = new Message();
		Integer page = 1;
		if (request.getParameter("page") != null) {
			page = Integer.parseInt(request.getParameter("page"));
		}
		Integer rows = 10;
		if (request.getParameter("rows") != null) {
			rows = Integer.parseInt(request.getParameter("rows"));
		}
		BDCS_ZRZ_XZ zrz = new BDCS_ZRZ_XZ();
		zrz.setZL(request.getParameter("FWZL"));
		zrz.setBDCDYH(request.getParameter("FWDYH"));
		msg = dyService.QueryZrzs(page, rows, zrz);
		return msg;
	}

	// /**
	// * 通过不动产单元号获取不动产权证号，权利人名称，权属，抵押，查封，异议（URL："/qlrs_cqzs",Method：GET）
	// *
	// * @param request
	// * @param response
	// * @return
	// * @throws UnsupportedEncodingException
	// */
	// @RequestMapping(value = "/qlrs_cqzs", method = RequestMethod.GET)
	// public @ResponseBody Message QueryQLR_CQZInfo(HttpServletRequest request,
	// HttpServletResponse response) throws UnsupportedEncodingException {
	// // @荣险峰 这里的这个方法还有用吗
	// String strbdcdyh ="";// RequestHelper.getParam(request, "bdcdyh");
	// Message msg = dyService.GetQLR_CQZInfo(strbdcdyh);
	// return msg;
	// }

	/**
	 * 分页查询抵押权信息 wuzhu（URL："/dycx/{bdcdylx}",Method：GET）
	 * 
	 * @param bdcdylx
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/dycx/{bdcdylx}", method = RequestMethod.GET)
	public @ResponseBody Message Querydyqxx(@PathVariable("bdcdylx") String bdcdylx, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// @刘树峰 此方法可以用选择器代替，先放着
		request.setCharacterEncoding("utf-8");
		Integer page = 1;
		if (request.getParameter("page") != null) {
			page = Integer.parseInt(request.getParameter("page"));
		}
		Integer rows = 10;
		if (request.getParameter("rows") != null) {
			rows = Integer.parseInt(request.getParameter("rows"));
		}
		Map<String, String> mapCondition = new HashMap<String, String>();
		mapCondition.put("QLRMC", request.getParameter("DYQR"));
		mapCondition.put("BDCDYH", request.getParameter("BDCDYH"));
		mapCondition.put("DYR", request.getParameter("DYR"));
		mapCondition.put("BDCQZH", request.getParameter("BDCQZH"));
		mapCondition.put("DYFS", request.getParameter("DYFS")); // 抵押方式
		Message msg = dyService.GetDYQInfo(bdcdylx, mapCondition, page, rows);
		return msg;
	}

	/**
	 * 分页查询抵押权信息 wuzhu（URL："/zxdjcx/{bdcdylx}",Method：GET）
	 * 
	 * @param bdcdylx
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/zxdjcx/{bdcdylx}", method = RequestMethod.GET)
	public @ResponseBody Message QueryZxdjXX(@PathVariable("bdcdylx") String bdcdylx, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// TODO @刘树峰 此方法是否可以用选择器代替？
		Integer page = 1;
		if (request.getParameter("page") != null) {
			page = Integer.parseInt(request.getParameter("page"));
		}
		Integer rows = 10;
		if (request.getParameter("rows") != null) {
			rows = Integer.parseInt(request.getParameter("rows"));
		}
		Map<String, String> mapCondition = new HashMap<String, String>();
		mapCondition.put("ZL", request.getParameter("ZL"));
		mapCondition.put("QLRMC", request.getParameter("QLRMC"));
		mapCondition.put("BDCDYH", request.getParameter("BDCDYH"));
		mapCondition.put("CFJG", request.getParameter("CFJG"));
		mapCondition.put("CFWH", request.getParameter("CFWH"));
		Message msg = dyService.GetZXDJInfo(bdcdylx, mapCondition, page, rows);
		return msg;
	}

	/**
	 * 分页查询期房单元信息（URL："/PreUnitData",Method：POST）
	 * 
	 * @作者 李想
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/PreUnitData", method = RequestMethod.POST)
	public @ResponseBody Message QueryPreUnitList(HttpServletRequest request, HttpServletResponse response) {
		// TODO @刘树峰 此方法是之前李想做预告登记的时候用的，当时还没有预测户层，所以有问题
		Integer page = 1;
		if (request.getParameter("page") != null) {
			page = Integer.parseInt(request.getParameter("page"));
		}
		Integer rows = 20;
		if (request.getParameter("rows") != null) {
			rows = Integer.parseInt(request.getParameter("rows"));
		}
		Map<String, String> mapCondition = new HashMap<String, String>();
		StringBuilder hqlString = new StringBuilder();
		if (request.getParameter("easteAddress") != null) {
			String easteAddress = request.getParameter("easteAddress");
			if (!easteAddress.equals("")) {
				mapCondition.put("ZLPARA", "%" + easteAddress + "%");
				hqlString.append(" ZL like :ZLPARA");
			}
		}
		if (request.getParameter("easteNumber") != null) {
			String easteNumber = request.getParameter("easteNumber");
			if (!easteNumber.equals("")) {
				mapCondition.put("BDCDYHPARA", "%" + easteNumber + "%");
				if (hqlString.length() > 0) {
					hqlString.append(" and BDCDYH like :BDCDYHPARA");
				} else {
					hqlString.append(" BDCDYH like :BDCDYHPARA");
				}
			}
		}
		if (hqlString.length() == 0) {
			hqlString.append(" 1=1 ");
		}

		Page Paged = dyService.getPagedPreUnit(page, rows, mapCondition, hqlString.toString());
		Message m = new Message();
		m.setTotal(Paged.getTotalCount());
		m.setRows(Paged.getResult());
		return m;
	}

	/**
	 * 批量添加期房单元信息（URL："/{xmbh}/preunits",Method：POST）
	 * 
	 * @作者 李想
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/{xmbh}/preunits", method = RequestMethod.POST)
	public @ResponseBody ResultMessage AddPreUint(@PathVariable("xmbh") String xmbh, HttpServletRequest request,
			HttpServletResponse response) {
		// TODO @刘树峰 此方法是之前李想做预告登记的时候用的，当时还没有预测户层，所以有问题
		String json = request.getParameter("json");
		ResultMessage resultMessage = new ResultMessage();
		JSONArray array = JSONArray.fromObject(json);
		for (int i = 0; i < array.size(); i++) {
			JSONObject object = (JSONObject) array.get(i);
			dyService.addBDCDY(xmbh, object.get("id").toString());
		}
		resultMessage.setMsg("添加成功!");
		return resultMessage;
	}

	/**
	 * 检查期房单元状态期房单元信息（URL："/preunitsstate",Method：POST）
	 * 
	 * @作者 李想
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/preunitsstate", method = RequestMethod.POST)
	public @ResponseBody ResultMessage GetPreUint(HttpServletRequest request, HttpServletResponse response) {
		// TODO @刘树峰 此方法是之前李想做预告登记的时候用的，当时还没有预测户层，所以有问题
		String xmbh = "";
		String bdcdyid = "";
		return dyService.GetPreUnitState(xmbh, bdcdyid);
	}

	/**
	 * 保存单元信息（URL："/{xmbh}/updatedyinfo/{bdcdyid}",Method：POST）
	 * 
	 * @作者：俞学斌
	 * @创建时间 2015年6月26日 上午11:20:45
	 * @param xmbh
	 * @param bdcdyid
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/{xmbh}/updatedyinfo/{bdcdyid}", method = RequestMethod.POST)
	public @ResponseBody ResultMessage UpdateDYInfo(@PathVariable("xmbh") String xmbh,
			@PathVariable("bdcdyid") String bdcdyid, HttpServletRequest request, HttpServletResponse response) {
		ResultMessage resultMessage = new ResultMessage();
		String dataString = request.getParameter("info");
		com.alibaba.fastjson.JSONObject object = JSON.parseObject(dataString);
		resultMessage = dyService.UpdateDYInfo(xmbh, bdcdyid, object);
		resultMessage.setSuccess("true");
		resultMessage.setMsg("保存成功!");
		YwLogUtil.addYwLog("保存单元信息", ConstValue.SF.YES.Value, ConstValue.LOG.UPDATE);
		return resultMessage;
	}

	/**
	 * 读取合同
	 * 
	 * @作者 diaoliwei
	 * @创建时间 2015年7月19日下午11:02:59
	 * @param htbh
	 * @param bmbm
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/readContract/{xmbh}/{htbh}/{bmbm}", method = RequestMethod.POST)
	public @ResponseBody ResultMessage readContract(@PathVariable("xmbh") String xmbh,
			@PathVariable("htbh") String htbh, @PathVariable("bmbm") String bmbm, HttpServletRequest request,
			HttpServletResponse response) {
		ResultMessage msg = new ResultMessage();
		//乌鲁木齐大宗件，需要传过来登记原因
		String djyyString="";
		 djyyString=request.getParameter("djyy");
		String file_Path = request.getSession().getServletContext().getRealPath("/");
		msg=shareTool.extractData(xmbh, htbh, bmbm, file_Path,djyyString,null,null);
		
		return msg;
	}

	@RequestMapping(value = "/addBDCDY/{xmbh}/{htbh}/{bmbm}", method = RequestMethod.POST)
	public @ResponseBody ResultMessage addBDCDY(@PathVariable("xmbh") String xmbh, @PathVariable("htbh") String htbh,
			@PathVariable("bmbm") String bmbm, HttpServletRequest request, HttpServletResponse response) {
		ResultMessage msg = new ResultMessage();
		String reFlag = "";
		try {
			String xzqdm = ConfigHelper.getNameByValue("XZQHDM");
			if(xzqdm.contains("1301")){
				reFlag = extractDataForSJZService.AddDYBDCDY(htbh, xmbh);
			}else if(xzqdm.contains("6501")){
				reFlag = extractDataForWLMQService.AddDYBDCDY(htbh, xmbh);
			}else{
				reFlag = extractDataForGXService.AddDYBDCDY(htbh, xmbh);
			}
			
		} catch (Exception e) {

			e.printStackTrace();
		}
		if (reFlag.equals("true")) {
			msg.setMsg("全部数据读取成功");
			msg.setSuccess("true");
		} else if (reFlag.equals("warning")) {
			msg.setMsg("存在单元未落宗，</br>请先进行单元落宗！");
			msg.setSuccess("false");
		} else if (reFlag.equals("false")) {
			msg.setMsg("案卷号有误!");
			msg.setSuccess("false");
		} else {
			msg.setMsg(reFlag);
			msg.setSuccess("false");
		}
		return msg;
	}

	/**
	 * 房屋单元加上强制过户限制
	 * 
	 * @Title: addQZGHXZ
	 * @author:liushufeng
	 * @date：2015年8月15日 下午5:36:29
	 * @param bdcdyid
	 * @param xmbh
	 * @return
	 */
	@RequestMapping(value = "/addqzghxz/{xmbh}/{bdcdyid}/")
	public @ResponseBody ResultMessage addQZGHXZ(@PathVariable("bdcdyid") String bdcdyid,
			@PathVariable("xmbh") String xmbh) {
		YwLogUtil.addYwLog("房屋单元加上强制过户限制", ConstValue.SF.YES.Value, ConstValue.LOG.ADD);
		return dyService.addQZGHXZ(bdcdyid, xmbh);
	}

	/**
	 * 房屋单元加上强制过户限制
	 * 
	 * @Title: addQZGHXZ
	 * @author:liushufeng
	 * @date：2015年8月15日 下午5:36:29
	 * @param bdcdyid
	 * @param xmbh
	 * @return
	 */
	@RequestMapping(value = "/qxqzghxz/{xmbh}/{bdcdyid}/")
	public @ResponseBody ResultMessage qxQZGHXZ(@PathVariable("bdcdyid") String bdcdyid,
			@PathVariable("xmbh") String xmbh) {
		return dyService.qxQZGHXZ(bdcdyid, xmbh);
	}

	@RequestMapping(value = "/loaddyinfo/{bdcdyid}/{djdyly}/{bdcdylx}", method = RequestMethod.GET)
	public @ResponseBody RealUnit LoadDYInfo(@PathVariable("bdcdyid") String bdcdyid,
			@PathVariable("djdyly") String djdyly, @PathVariable("bdcdylx") String bdcdylx, HttpServletRequest request,
			HttpServletResponse response) {
		RealUnit unit = UnitTools.loadUnit(BDCDYLX.initFrom(bdcdylx), DJDYLY.initFrom(djdyly), bdcdyid);
		String xmbh = request.getParameter("xmbh");
		// 石家庄想在户上显示幢信息
		unit = dyService.getZrzUnit(xmbh, djdyly, bdcdylx, unit);
		return unit;
	}

	/**
	 * 更新不动产单元信息，并记录变更前单元信息
	 * 
	 * @Title: UpdateBDCDYInfoEx
	 * @author:yuxuebin @date：2016年03月24日 10:31:29
	 * @param bdcdyid
	 * @param xmbh
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/updatedyinfo/{xmbh}/", method = RequestMethod.POST)
	public @ResponseBody ResultMessage UpdateBDCDYInfoEx(@PathVariable("xmbh") String xmbh, HttpServletRequest request,
			HttpServletResponse response) {
		ResultMessage msg = new ResultMessage();
		msg.setMsg("保存失败！");
		msg.setSuccess("false");
		try {
			request.setCharacterEncoding("UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		Map map = transToMAP(request.getParameterMap());
		String bdcdyid = StringHelper.formatObject(map.get("id"));
		String djdyly = StringHelper.formatObject(map.get("ly"));
		String bdcdylx = StringHelper.formatObject(map.get("bdcdylx"));
		BDCDYLX dylx = BDCDYLX.initFromByEnumName(bdcdylx);
		DJDYLY dyly = DJDYLY.initFromByEnumName(djdyly);
		if (DJDYLY.LS.equals(dyly)) {
			dyly = DJDYLY.XZ;
		}
		if (dyService.BackBDCDYInfo(xmbh, dylx, dyly, bdcdyid)) {
			msg = dyService.UpdateBDCDYInfo(map, dylx, dyly, bdcdyid);
			dyService.UpdateZrzUnit(map, xmbh, dyly, dylx, bdcdyid);
		}
		return msg;
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/updatedyinfo", method = RequestMethod.POST)
	public @ResponseBody ResultMessage UpdateBDCDYInfo(HttpServletRequest request, HttpServletResponse response) {
		ResultMessage msg = new ResultMessage();
		try {
			request.setCharacterEncoding("UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		Map map = transToMAP(request.getParameterMap());
		String bdcdyid = StringHelper.formatObject(map.get("id"));
		String djdyly = StringHelper.formatObject(map.get("ly"));
		String bdcdylx = StringHelper.formatObject(map.get("bdcdylx"));
		BDCDYLX dylx = BDCDYLX.initFromByEnumName(bdcdylx);
		DJDYLY dyly = DJDYLY.initFromByEnumName(djdyly);
		if (DJDYLY.LS.equals(dyly)) {
			dyly = DJDYLY.XZ;
		}
		msg = dyService.UpdateBDCDYInfo(map, dylx, dyly, bdcdyid);
		return msg;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Map transToMAP(Map parameterMap) {
		// 返回值Map
		Map returnMap = new HashMap();
		Iterator entries = parameterMap.entrySet().iterator();
		Map.Entry entry;
		String name = "";
		String value = "";
		while (entries.hasNext()) {
			entry = (Map.Entry) entries.next();
			name = (String) entry.getKey();
			Object valueObj = entry.getValue();
			if (null == valueObj) {
				value = "";
			} else if (valueObj instanceof String[]) {
				String[] values = (String[]) valueObj;
				for (int i = 0; i < values.length; i++) {
					value = values[i] + ",";
				}
				value = value.substring(0, value.length() - 1);
			} else {
				value = valueObj.toString();
			}
			returnMap.put(name, value);
		}
		return returnMap;
	}

	/**
	 * 根据自然幢不动产单元id、自然幢不动产单元类型获取户不动产单元号
	 * 
	 * @作者 俞学斌
	 * @创建时间 2015年9月18日12:02:53
	 * @param zrzbdcdyid
	 * @param zrzbdcdylx
	 * @return
	 */
	@RequestMapping(value = "/gethousebdcdyh/{zrzbdcdyid}/{zrzbdcdylx}", method = RequestMethod.GET)
	public @ResponseBody ResultMessage getHouseBDCDYH(@PathVariable("zrzbdcdyid") String zrzbdcdyid,
			@PathVariable("zrzbdcdylx") String zrzbdcdylx, HttpServletRequest request, HttpServletResponse response) {
		ResultMessage message = new ResultMessage();
		message = dyService.getHouseBDCDYH(zrzbdcdyid, zrzbdcdylx);
		return message;
	}

	/**
	 * 根据表单信息添加不动产单元、登记单元、权利、附属权利
	 * 
	 * @作者 俞学斌
	 * @创建时间 2015年9月18日12:02:53
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/adddyinfo", method = RequestMethod.POST)
	public @ResponseBody ResultMessage AddBDCDYInfo(HttpServletRequest request, HttpServletResponse response) {
		ResultMessage msg = new ResultMessage();
		try {
			request.setCharacterEncoding("UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		Map map = transToMAP(request.getParameterMap());
		msg = dyService.AddBDCDYInfo(map);
		return msg;
	}

	/**
	 * 批量更新户工作户信息
	 * 
	 * @作者 海豹
	 * @创建时间 2015年9月21日上午1:14:03
	 * @param xmbh
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/plupdatehouseinfo/", method = RequestMethod.POST)
	public @ResponseBody ResultMessage plupdatehouseinfo(HttpServletRequest request, HttpServletResponse response) {
		ResultMessage msg = new ResultMessage();
		try {
			String bdcdy_String = request.getParameter("bdcdyids");
			// String houses_gzString = request.getParameter("infos");
			com.alibaba.fastjson.JSONArray bdcdyidArray = com.alibaba.fastjson.JSONArray.parseArray(bdcdy_String);
			// com.alibaba.fastjson.JSONObject jsonhouseinfo =
			// com.alibaba.fastjson.JSONObject.parseObject(houses_gzString);//
			// 户对象
			Map map = transToMAP(request.getParameterMap());
			Object[] objbdcdyids = (Object[]) bdcdyidArray.toArray();// 不动产单元ID集合
			for (Object obdcdyid : objbdcdyids) {
				if (obdcdyid != null) {
					String bdcdyid = obdcdyid.toString();
					RealUnit unit = UnitTools.loadUnit(BDCDYLX.H, DJDYLY.GZ, bdcdyid);
					if (unit != null) {
						dyService.UpdateBDCDYInfo(map, BDCDYLX.H, DJDYLY.GZ, bdcdyid);
					} else {
						dyService.UpdateBDCDYInfo(map, BDCDYLX.H, DJDYLY.XZ, bdcdyid);
					}
				}
			}
			msg.setSuccess("true");
			msg.setMsg("批量更新成功！");
		} catch (Exception e) {
			logger.error(e.getMessage());
			msg.setSuccess("false");
			msg.setMsg(e.getMessage());
		}
		return msg;
	}

	/**
	 * 批量更新户工作户信息,并备份更改前信息
	 * 
	 * @作者 海豹
	 * @创建时间 2016年03月24日 11:18:03
	 * @param xmbh
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/plupdatehouseinfo/{xmbh}/", method = RequestMethod.POST)
	public @ResponseBody ResultMessage plupdatehouseinfoE(@PathVariable("xmbh") String xmbh, HttpServletRequest request,
			HttpServletResponse response) {
		ResultMessage msg = new ResultMessage();
		try {
			String bdcdy_String = request.getParameter("bdcdyids");
			com.alibaba.fastjson.JSONArray bdcdyidArray = com.alibaba.fastjson.JSONArray.parseArray(bdcdy_String);
			Map map = transToMAP(request.getParameterMap());
			Object[] objbdcdyids = (Object[]) bdcdyidArray.toArray();// 不动产单元ID集合
			for (Object obdcdyid : objbdcdyids) {
				if (obdcdyid != null) {
					String bdcdyid = obdcdyid.toString();
					RealUnit unit = UnitTools.loadUnit(BDCDYLX.H,DJDYLY.GZ, bdcdyid);
					if (unit != null && xmbh.equals(unit.getXMBH())) {
						dyService.UpdateBDCDYInfo(map, BDCDYLX.H, DJDYLY.GZ, bdcdyid);
					} else {
						unit = UnitTools.loadUnit(BDCDYLX.H,DJDYLY.XZ, bdcdyid);
						if(unit!=null){
							if (dyService.BackBDCDYInfo(xmbh, BDCDYLX.H, DJDYLY.XZ, bdcdyid)) {
								dyService.UpdateBDCDYInfo(map, BDCDYLX.H, DJDYLY.XZ, bdcdyid);
							}
						}
					}
					if(unit == null){
						unit = UnitTools.loadUnit(BDCDYLX.YCH,DJDYLY.XZ, bdcdyid);
						if(unit!=null){
							if (dyService.BackBDCDYInfo(xmbh, BDCDYLX.YCH, DJDYLY.XZ, bdcdyid)) {
								dyService.UpdateBDCDYInfo(map, BDCDYLX.YCH, DJDYLY.XZ, bdcdyid);
							}
						}
					}
				}	
			}
			msg.setSuccess("true");
			msg.setMsg("批量更新成功！");
		} catch (Exception e) {
			logger.error(e.getMessage());
			msg.setSuccess("false");
			msg.setMsg(e.getMessage());
		}
		return msg;
	}

	/**
	 * 获取抵押变更项目不动产单元列表（URL:"/{xmbh}/dybgdjdys/{type}",Method：GET）
	 * 
	 * @param xmbh
	 * @param request
	 * @param response
	 * @author yuxuebin
	 * @return
	 */
	@RequestMapping(value = "/{xmbh}/dybgdjdys/{type}", method = RequestMethod.GET)
	public @ResponseBody Message GetDYBGDJDYS(@PathVariable("xmbh") String xmbh, @PathVariable("type") String type,
			HttpServletRequest request, HttpServletResponse response) {
		Integer page = 1;
		if (request.getParameter("page") != null) {
			page = Integer.parseInt(request.getParameter("page"));
		}
		Integer rows = 10;
		if (request.getParameter("rows") != null) {
			rows = Integer.parseInt(request.getParameter("rows"));
		}
		Message message = dyService.GetDYBGDJDYS(xmbh, type, page, rows);
		return message;
	}
	
	/**
	 * BG027权利页面单元过滤查询
	 * @author weilb
	 * @param xmbh
	 * @param request
	 * @param response
	 * @return json
	 */
	@SuppressWarnings({ "static-access", "unused" })
	@RequestMapping(value = "/{xmbh}/dyfilterquery/", method = RequestMethod.GET)
	public @ResponseBody Message dyFilterQuery(@PathVariable("xmbh") String xmbh,HttpServletRequest request, HttpServletResponse response) {
		RequestHelper rh = new RequestHelper();
		String bdcqzh = "";
		String zl = "";
		try {
			bdcqzh = rh.getParam(request, "bdcqzh");
			zl = rh.getParam(request, "zl");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return dyService.dyFilterQuery(xmbh, bdcqzh, zl); 
	}
	

	/**
	 * 抵押变更添加一个登记单元（URL:"/{xmbh}/djdys/{bdcdyid}",Method：POST）
	 * 
	 * @param xmbh
	 * @param bdcdyid
	 * @param request
	 * @param response
	 * @author yuxuebin
	 * @return
	 */
	@RequestMapping(value = "/{xmbh}/djdys/{bdcdyid}/dybg/", method = RequestMethod.POST)
	public @ResponseBody ResultMessage AddBDCDY_DYBG(@PathVariable("xmbh") String xmbh,
			@PathVariable("bdcdyid") String bdcdyid, HttpServletRequest request, HttpServletResponse response) {
		ResultMessage resultMessage = new ResultMessage();
		bdcdyid = request.getParameter("ids");
		resultMessage = dyService.addBDCDY_DYBG(xmbh, bdcdyid);
		YwLogUtil.addYwLog("添加单元", ConstValue.SF.YES.Value, ConstValue.LOG.ADD);
		return resultMessage;
	}

	/**
	 * 获取发证单元列表信息（URL:"/{xmbh}/fzdys/",Method：GET）
	 * 
	 * @param xmbh
	 * @param request
	 * @param response
	 * @author yuxuebin
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping(value = "/{xmbh}/fzdys/", method = RequestMethod.GET)
	public @ResponseBody Message GetFzDys(@PathVariable("xmbh") String xmbh, HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException {
		String qllx=RequestHelper.getParam(request, "qllx");
		Message m = dyService.GetFzDys(xmbh,qllx);
		
		return m;
	}

	/**
	 * 更正发证单元分组标识（URL:"/{xmbh}/fzdys/",Method：POST）
	 * 
	 * @param xmbh
	 * @param request
	 * @param response
	 * @author yuxuebin
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping(value = "/{xmbh}/fzdys/", method = RequestMethod.POST)
	public @ResponseBody ResultMessage UpdateFzDys(@PathVariable("xmbh") String xmbh, HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException {
		String data = request.getParameter("data");
		String qllx=RequestHelper.getParam(request, "qllx");
		HashMap<String, Integer> m = new HashMap<String, Integer>();
		JSONArray array = JSONArray.fromObject(data);
		for (int i = 0; i < array.size(); i++) {
			JSONObject object = (JSONObject) array.get(i);
			if (object.containsKey("DJDYID") && !StringHelper.isEmpty(object.get("DJDYID"))) {
				String djdyid = StringHelper.formatObject(object.get("DJDYID"));
				String qlid="";
				if(object.containsKey("QLID") && !StringHelper.isEmpty(object.get("QLID")))
				{
					qlid = StringHelper.formatObject(object.get("QLID"));
				}
				if (object.containsKey("GROUPID") && !StringHelper.isEmpty(object.get("GROUPID"))) {
					try {
						int intValue = (int) StringHelper.getDouble(object.get("GROUPID"));
						//获得当前单元的抵押金性质
						int  dydyjxz = (int) StringHelper.getDouble(object.get("DYDYJXZ"));
						if (!m.containsKey(djdyid)) {
							m.put(djdyid, intValue);
							m.put("dydyjxz", dydyjxz);
						}
						if(!StringHelper.isEmpty(qlid)){
							if(!m.containsKey(qlid)){
								m.put(qlid, intValue);
							}
						}
						
					} catch (Exception e) {
					}
				}
			}
		}
		ResultMessage ms = dyService.UpdateFzDys(xmbh, m,qllx);
		return ms;
	}

	/**
	 * 根据batchNumber获取project（URL:"/getbatchproject/{prodefid}/{batch}/{number}"
	 * ,Method：POST）
	 * 
	 * @param prodefid
	 * @param batch
	 * @param number
	 * @param request
	 * @param response
	 * @author zhuhe
	 * @return
	 */
	@RequestMapping(value = "/getbatchproject/{prodefid}/{batch}/{number}", method = RequestMethod.POST)
	public @ResponseBody ResultMessage GetBatchProject(@PathVariable("prodefid") String prodefid,
			@PathVariable("batch") String batchNumber, @PathVariable("number") String number,
			HttpServletRequest request, HttpServletResponse response) {
		ResultMessage msg = new ResultMessage();
		String reFlag = "";
		reFlag = extractDataForSJZService.GetBatchProject(prodefid, batchNumber, request, number);
		if (reFlag.equals("true")) {
			msg.setMsg("全部数据读取成功");
			msg.setSuccess("true");
		} else if (reFlag.equals("warning")) {
			msg.setMsg("存在单元未落宗，</br>请先进行落宗！");
			msg.setSuccess("warning");
		} else {
			msg.setMsg(reFlag);
			msg.setSuccess("false");
		}
		return msg;
	}

	/**
	 * 根据casenum获取project（URL:
	 * "/getproject/{prodefid}/{xmbh}/{batchNumber}/{casenum}/{proinstid}/{count}/{num}"
	 * ,Method：POST）
	 * 
	 * @param prodefid
	 * @param xmbh
	 * @param casenum
	 * @param proinstid
	 * @param count
	 * @param num
	 * @param request
	 * @param response
	 * @author zhuhe
	 * @return
	 */
	@RequestMapping(value = "/getproject/{prodefid}/{xmbh}/{batchNumber}/{casenum}/{proinstid}/{count}/{num}", method = RequestMethod.POST)
	public @ResponseBody ResultMessage GetProject(@PathVariable("prodefid") String prodefid,
			@PathVariable("xmbh") String xmbh, @PathVariable("batchNumber") String batchNumber,
			@PathVariable("casenum") String casenum, @PathVariable("proinstid") String proinstid,
			@PathVariable("count") String count, @PathVariable("num") String num, HttpServletRequest request,
			HttpServletResponse response) {
		ResultMessage msg = new ResultMessage();
		String reFlag = "";
		reFlag = extractDataForSJZService.GetProject(prodefid, xmbh, batchNumber, casenum, proinstid, count, num,
				request);
		if (reFlag.equals("true")) {
			msg.setMsg("全部数据读取成功");
			msg.setSuccess("true");
		} else if (reFlag.equals("warning")) {
			msg.setMsg("存在单元未落宗，</br>请先进行落宗！");
			msg.setSuccess("warning");
		} else {
			msg.setMsg(reFlag);
			msg.setSuccess("false");
		}
		return msg;
	}

	@RequestMapping(value = "/checkdata/{xmbh}/{casenum}", method = RequestMethod.POST)
	public @ResponseBody ResultMessage checkData(@PathVariable("xmbh") String xmbh,
			@PathVariable("casenum") String casenum, HttpServletRequest request, HttpServletResponse response) {
		ResultMessage msg = new ResultMessage();
		String reFlag = "";
		//2016年10月9日 19:11:38  石家庄房产交易库数据检查走特殊版本，其他地方默认共享交易库检查方法
		String xzqdm = ConfigHelper.getNameByValue("XZQHDM");
		if (xzqdm.contains("1301")) {
			reFlag = extractDataForSJZService.checkData(casenum, xmbh);
		}else{
			reFlag = extractDataService.checkData(casenum, xmbh);
		}
		if (reFlag.equals("true")) {
			msg.setMsg("数据检查通过");
			msg.setSuccess("true");
		} else {
			msg.setMsg(reFlag);
			msg.setSuccess("false");
		}
		return msg;
	}
	
	@RequestMapping(value = "/savefcywh/{xmbh}/{casenum}/{bdcdyid}/{ly}", method = RequestMethod.POST)
	public @ResponseBody ResultMessage saveFcywh(@PathVariable("xmbh") String xmbh,
			@PathVariable("casenum") String casenum,@PathVariable("bdcdyid") String bdcdyid,@PathVariable("ly") String ly, HttpServletRequest request, HttpServletResponse response) {
		ResultMessage msg = new ResultMessage();
		String reFlag = "";
		reFlag = extractDataService.saveFcywh(casenum, xmbh,bdcdyid,ly);
		if (reFlag.equals("true")) {
			msg.setMsg("房产业务号保存成功！");
			msg.setSuccess("true");
		} else {
			msg.setMsg(reFlag);
			msg.setSuccess("false");
		}
		return msg;
	}
	/**
	 * 读取地籍合同
	 * 
	 * @作者 buxiaobo
	 * @创建时间 2016年11月7日 19:12:29
	 * @param xmbh
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/readDJContract/{xmbh}/{htbh}", method = RequestMethod.POST)
	public @ResponseBody ResultMessage readDJContract(@PathVariable("xmbh") String xmbh,@PathVariable("htbh") String htbh, HttpServletRequest request,
			HttpServletResponse response) {
		ResultMessage msg = new ResultMessage();
		try {
			String xzqdm = ConfigHelper.getNameByValue("XZQHDM");
			//吉林市
			if (xzqdm.contains("2202")) {
				String reFlag = extractDataForGXService.ExtractSXFromDJ(xmbh,htbh);
				if (reFlag.equals("true")) {
					msg.setMsg("全部数据读取成功");
					msg.setSuccess("true");
				} else {
					msg.setMsg(reFlag);
					msg.setSuccess("false");
				}
			}
		}catch (Exception e) {
			msg.setMsg("号码有误!");
			msg.setSuccess("false");
			YwLogUtil.addYwLog("读取地籍合同成功-号码有误!", ConstValue.SF.NO.Value, ConstValue.LOG.ADD);
			e.printStackTrace();
		}
		return msg;
	}
	
	/**
	 * 乌鲁木齐获取房产交易状态
	 * @param xmbh
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/getFCStatus", method = RequestMethod.GET)
	public @ResponseBody String getFcStatus( HttpServletRequest request,
			HttpServletResponse response) {
		String casenum= request.getParameter("lsh");
		String status=extractDataForWLMQService.getFcStatus(casenum);
		return status;
	}

	@RequestMapping(value = "/readdjyy/{xmbh}/{htbh}/{bmbm}", method = RequestMethod.POST)
	public @ResponseBody ResultMessage readDJYY(@PathVariable("xmbh") String xmbh,
			@PathVariable("htbh") String htbh, @PathVariable("bmbm") String bmbm, HttpServletRequest request,
			HttpServletResponse response) {
		ResultMessage msg = new ResultMessage();
		try {
			
		}catch(Exception ex){
		}
		return msg;
	}
	
	@RequestMapping(value = "/createbdcdyh/{relyonvalue}/{bdcdylx}")
    public @ResponseBody
    String createbdcdyh(@PathVariable String relyonvalue, @PathVariable String bdcdylx, HttpServletRequest request) {
        return ProjectHelper.CreatBDCDYH(relyonvalue, bdcdylx);
    }
	
	/**
	 * 抵押变更添加一个登记单元（URL:"/cfcheck Method：POST）
	 * 
	 * @param bdcdyid
	 * @param request
	 * @param response
	 * @author yuxuebin
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/cfcheck", method = RequestMethod.POST)
	public @ResponseBody ResultMessage CheckCfXX(
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		String bdcqzhs = RequestHelper.getParam(request,"bdcqzhs");
		String xmbh = request.getParameter("xmbh");
		String bdcdylx = request.getParameter("bdcdylx");
		String qlids = request.getParameter("qlids");

		ResultMessage resultMessage = new ResultMessage();
		resultMessage = queryServiceImpl_gx.CheckCFXX(bdcqzhs, xmbh, bdcdylx, qlids);
		return resultMessage;
	}
	/**
	 * 检查预关联
	 * 
	 * @param bdcdyid
	 * 
	 * @param request
	 * @param response
	 * @author yuxuebin
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/checkYGL/", method = RequestMethod.POST)
	public  @ResponseBody Message checkYGL(
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		String bdcdyids = RequestHelper.getParam(request,"bdcdyids");
		List<Map<String, String>> list= new ArrayList<Map<String,String>>();//宗地变更未登簿
		boolean  f=false;//是否存在预关联
		if(!StringHelper.isEmpty(bdcdyids)) {
			String [] bdcdidStr=bdcdyids.split(",");
			Map<String, String>  map=null;
			for(String bdcdid :bdcdidStr) {
				List<BDCS_H_LS>  bdcs_h_ls_list= baseCommonDao.getDataList(BDCS_H_LS.class, "BDCDYID='"+bdcdid+"'");
				if (bdcs_h_ls_list!=null&&bdcs_h_ls_list.size()>0) {
				 for(BDCS_H_LS bdcs_h_ls:bdcs_h_ls_list) {
					 if(!StringHelper.isEmpty(bdcs_h_ls.getNZDBDCDYID())) {
						 f=true;
						 map=queryServiceImpl_gx.getInfo(bdcs_h_ls);
					 } 
				 }
				}else {
					List<BDCS_H_LSY>  bdcs_h_lsy_list= baseCommonDao.getDataList(BDCS_H_LSY.class, "BDCDYID='"+bdcdid+"'");
					if(bdcs_h_lsy_list!=null&&bdcs_h_lsy_list.size()>0) {
						for(BDCS_H_LSY bdcs_h_lsy :bdcs_h_lsy_list) {
							if(!StringHelper.isEmpty(bdcs_h_lsy.getNZDBDCDYID())) {
								f=true;
								 map=queryServiceImpl_gx.getInfo(bdcs_h_lsy);
							 } 
							
						}
					}
				}
			}
			if(map!=null&&f){
				list.add(map);
				//宗地做了预关联.宗地有做变更
			}
		} 
		Message resultMessage = new Message();
		if(list!=null&&list.size()>0) {
			resultMessage.setRows(list);
			return resultMessage;
		}else if(f) {
			resultMessage.setMsg("选择单元在权籍已经做好宗地变更调查，是否继续受理");
			return resultMessage;
		}
		return null;
	}
	
}