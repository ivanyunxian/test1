package com.supermap.realestate.registration.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.supermap.realestate.registration.model.BDCS_SFDY;
import com.supermap.realestate.registration.service.DJSFService;
import com.supermap.realestate.registration.util.ConstValue;
import com.supermap.realestate.registration.util.YwLogUtil;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import com.supermap.wisdombusiness.web.ResultMessage;
import com.supermap.wisdombusiness.web.ui.Page;
import com.supermap.wisdombusiness.workflow.service.wfm.SmProDefService;
import com.supermap.wisdombusiness.workflow.util.Message;

/**
 * 
 * 登记收费Controller 登记收费相关内容
 * 
 * @author 郭浩龙
 * @date 2015年7月22日 下午16:31:55
 * @Copyright SuperMap
 */
@Controller
@RequestMapping("/realestate/registration")
public class DJSFController {
	@Autowired
	private CommonDao dao;
	/** 登记收费service */
	@Autowired
	private DJSFService djsfService;
	@Autowired
	private SmProDefService _SmProDefService;

	/**
	 * 获取所有收费项目 不分页（URL:"/djsf/all",Method:GET）
	 * 
	 * @作者：郭浩龙
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/djsf/all", method = RequestMethod.GET)
	public @ResponseBody List<BDCS_SFDY> getSFDJ() throws Exception {
		List<BDCS_SFDY> allSFDY = djsfService.GetSFDY();
		YwLogUtil.addYwLog("收费项目列表", ConstValue.SF.YES.Value,ConstValue.LOG.SEARCH);
		return allSFDY;
	}

	/**
	 * 根据流程ID获取收费项目（URL:"/djsf/getSFListByPro",Method:GET）
	 * 
	 * @作者：郭浩龙
	 * @param prodefid
	 *            流程id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/djsf/getSFListByProId", method = RequestMethod.GET)
	@ResponseBody
	public List<BDCS_SFDY> getSFListByPro(@RequestParam("ProId") String ProId)
			throws Exception {
		List<BDCS_SFDY> SFListByPro = djsfService.GetSF(ProId);

		return SFListByPro;
	}

	/**
	 * 得到所有收费项目定义 带分页（URL:"djsf/djsflist",Method:GET）
	 * 
	 * @作者：郭浩龙
	 * @param page
	 *            rows
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/djsf/djsflist", method = RequestMethod.GET)
	@ResponseBody
	public Message getSFDY(@RequestParam("page") int page,
			@RequestParam("rows") int rows) throws Exception {
		Message msg = new Message();
		Page pageSFDY = djsfService.GetSFDY(page, rows);
		msg.setRows(pageSFDY.getResult());
		msg.setTotal(pageSFDY.getTotalCount());
		return msg;
	}

	/**
	 * 登记收费配置-更新（URL:"/djsf/djsfadd",Method:GET）
	 * 
	 * @作者：郭浩龙
	 * @param prodefid
	 *            流程id dyid 登记收费定义id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/djsf/djsfadd", method = RequestMethod.GET)
	public @ResponseBody boolean AddSF(
			@RequestParam("prodefId") String prodefId,
			@RequestParam("sfdjId") String sfdjId) throws Exception {
		return djsfService.addSFRela(prodefId, sfdjId);
	}

	/**
	 * 登记收费配置-删除该流程id对应的所有收费记录（URL:"/djsf/djsfdel",Method:GET）
	 * 
	 * @作者：郭浩龙
	 * @param prodefid
	 *            流程id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/djsf/djsfdel", method = RequestMethod.GET)
	public @ResponseBody boolean DeleteSF( @RequestParam("prodefId") String prodefId) throws Exception {
		return djsfService.deleteSFRela(prodefId);
	}

	@RequestMapping(value = "/djsf/djsfpz", method = RequestMethod.GET)
	public String main(Model model) throws Exception {
		return "/realestate/registration/djsf/djsfpz";
	}

	
	@RequestMapping(value = "/djsf/integrationdjsfpz", method = RequestMethod.GET)
	public String integrationdjsfpzmain(Model model) throws Exception {
		return "/realestate/registration/djsf/integrationdjsfpz";
	}
	/**
	 * /登记收费维护页面（URL:"djsf/djsfwh",Method:GET）
	 * 
	 * @作者：郭浩龙
	 */
	@RequestMapping(value = "/djsf/djsfwh", method = RequestMethod.GET)
	public String djsfwh(Model model) throws Exception {
		model.addAttribute("SFAttribute", new BDCS_SFDY());
		return "/realestate/registration/djsf/djsfwh";
	}

	/**
	 * 登记收费项目维护-增加收费项目记录（URL:"/djsf/addsfdy",Method:GET）
	 * 
	 * @作者：郭浩龙
	 * @param prodefid
	 *            流程id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/djsf/addsfdy", method = RequestMethod.POST)
	public @ResponseBody boolean AddSFDY(@Validated BDCS_SFDY sfdy, HttpServletRequest request) throws Exception {
		return djsfService.AddSFDY(sfdy);
	}

	/**
	 * @郭浩龙
	 * @param id
	 *            ……
	 * @return
	 * @throws Exception
	 */

	@RequestMapping(value = "/djsf/editsfdy/{sfid}", method = RequestMethod.POST)
	public @ResponseBody boolean EditSFDY(@PathVariable("sfid") String sfid,@Validated BDCS_SFDY sfdy, HttpServletRequest request)
			throws Exception {
		sfdy.setId(sfid);
		return djsfService.EditSFDY(sfdy);
	}

	/**
	 * 登记收费配置-删除该收费项目（URL:"/djsf/sfdydel",Method:GET）
	 * 
	 * @作者：郭浩龙
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/djsf/sfdydel", method = RequestMethod.GET)
	public @ResponseBody ResultMessage DeleteSFDY(
			@RequestParam("sfdyid") String sfdyid) throws Exception {
		String result = djsfService.deleteSFDY(sfdyid);
		ResultMessage mess = new ResultMessage();
		mess.setSuccess("删除结果");
		mess.setMsg(result);
		return mess;
	}

	/**
	 * /测试页面（URL:"djsf/test",Method:GET）
	 * 
	 * @作者：郭浩龙
	 */
	@RequestMapping(value = "/djsf/test", method = RequestMethod.GET)
	public String test(Model model) throws Exception {
		return "/realestate/registration/djsf/test";
	}

}
