package com.supermap.realestate.registration.web;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.supermap.realestate.registration.service.SelectorManagerService;
import com.supermap.realestate.registration.util.RequestHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.wisdombusiness.web.Message;
import com.supermap.wisdombusiness.web.ResultMessage;

/**
 * 选择器配置
 * @ClassName: SelectorManagerController
 * @author 俞学斌
 * @date 2016年08月25日 10:42:28
 */
@Controller
@RequestMapping("/selectormanager")
public class SelectorManagerController {

	/**
	 * SelectorManagerService
	 */
	@Autowired
	private SelectorManagerService selectorManagerService;

	private final String prefix = "/realestate/registration/";
	
	/*****************************************选择器配置*****************************************/
	/**
	 * 选择器信息页面(URL:"/selectorconfig/index/",Method：GET)
	 * @Title: getSelectorConfigIndex
	 * @author:俞学斌
	 * @date：2016年08月25日 14:16:28
	 * @return
	 */
	@RequestMapping(value = "/selectorconfig/index/")
	public String getSelectorConfigIndex() {
		return prefix + "handlermapping/selectorconfig";
	}
	
	/**
	 * 获取选择器信息 (URL:"/selectorinfo/{selectorid}/",Method：GET)
	 * @Title: GetSelectorInfo
	 * @author:俞学斌
	 * @date：2016年08月25日 16:36:28
	 * @return
	 */
	@RequestMapping(value = "/selectorinfo/{selectorid}/", method = RequestMethod.GET)
	public @ResponseBody HashMap<String,String> GetSelectorInfo(@PathVariable("selectorid") String selectorid,HttpServletRequest request, HttpServletResponse response) {
		HashMap<String,String> selectorinfo=selectorManagerService.GetSelectorInfo(selectorid);
		if(selectorinfo!=null){
			if(!selectorinfo.containsKey("useconfigsql")||StringHelper.isEmpty(selectorinfo.get("useconfigsql"))){
				selectorinfo.put("useconfigsql", "false");
			}
			if(!selectorinfo.containsKey("singleselect")||StringHelper.isEmpty(selectorinfo.get("singleselect"))){
				selectorinfo.put("singleselect", "true");
			}
			if(!selectorinfo.containsKey("defaultselectfirt")||StringHelper.isEmpty(selectorinfo.get("defaultselectfirt"))){
				selectorinfo.put("defaultselectfirt", "false");
			}
			if(!selectorinfo.containsKey("showdetailaltersleect")||StringHelper.isEmpty(selectorinfo.get("showdetailaltersleect"))){
				selectorinfo.put("showdetailaltersleect", "false");
			}
		}
		return selectorinfo;
	}
	
	/**
	 * 更新选择器信息 (URL:"/selectorinfo/{selectorid}/",Method：POST)
	 * @Title: UpdateSelectorInfo
	 * @author:俞学斌
	 * @date：2016年08月25日 16:36:28
	 * @return
	 */
	@RequestMapping(value = "/selectorinfo/{selectorid}/", method = RequestMethod.POST)
	public @ResponseBody ResultMessage UpdateSelectorInfo(@PathVariable("selectorid") String selectorid,HttpServletRequest request, HttpServletResponse response) {
		ResultMessage ms=selectorManagerService.UpdateSelectorInfo(selectorid,request);
		return ms;
	}
	/*****************************************选择器配置*****************************************/
	
	/*****************************************查询条件配置*****************************************/
	
	/**
	 * 选择器查询条件配置页面(URL:"/queryconfig/index/",Method：GET)
	 * @Title: getQueryConfigIndex
	 * @author:俞学斌
	 * @date：2016年08月25日 14:16:28
	 * @return
	 */
	@RequestMapping(value = "/queryconfig/index/")
	public String getQueryConfigIndex() {
		return prefix + "handlermapping/queryconfig";
	}
	
	/**
	 * 获取选择器查询条件列表(URL:"/queryconfig/",Method：GET)
	 * @Title: GetQueryConfig
	 * @author:俞学斌
	 * @date：2016年08月25日 19:10:28
	 * @return
	 */
	@RequestMapping(value = "/queryconfig/", method = RequestMethod.GET)
	public @ResponseBody Message GetQueryConfig(HttpServletRequest request, HttpServletResponse response) {
		String selectorid="";
		try {
			selectorid = RequestHelper.getParam(request, "selectorid");
		} catch (Exception e) {
		}
		Message m=selectorManagerService.GetQueryConfig(selectorid,request);
		return m;
	}
	
	/**
	 * 新增或保存选择器查询条件 (URL:"/queryconfig/{selectorid}/",Method：POST)
	 * @Title: AddOrUpdateQueryConfig
	 * @author:俞学斌
	 * @date：2016年08月25日 19:14:28
	 * @return
	 */
	@RequestMapping(value = "/queryconfig/{selectorid}/", method = RequestMethod.POST)
	public @ResponseBody ResultMessage AddOrUpdateQueryConfig(@PathVariable("selectorid") String selectorid,HttpServletRequest request, HttpServletResponse response) {
		ResultMessage msg = new ResultMessage();
		msg=selectorManagerService.AddOrUpdateQueryConfig(selectorid,request);
		return msg;
	}
	
	/**
	 * 删除选择器查询条件(URL:"/queryconfig/{id}",Method：DELETE)
	 * @Title: RemoveQueryConfig
	 * @author:俞学斌
	 * @date：2016年08月25日 19:13:28
	 * @return
	 */
	@RequestMapping(value = "/queryconfig/{id}", method = RequestMethod.DELETE)
	public @ResponseBody ResultMessage RemoveQueryConfig(@PathVariable("id") String id,HttpServletRequest request, HttpServletResponse response) {
		ResultMessage msg = new ResultMessage();
		msg=selectorManagerService.RemoveQueryConfig(id,request);
		return msg;
	}
	
	/**
	 * 重置选择器查询条件顺序 (URL:"/queryconfig/{selectorid}/resetsxh、",Method：POST)
	 * @Title: ResetSXHOnQueryConfig
	 * @author:俞学斌
	 * @date：2016年08月25日 19:12:28
	 * @return
	 */
	@RequestMapping(value = "/queryconfig/{selectorid}/resetsxh/", method = RequestMethod.POST)
	public @ResponseBody ResultMessage ResetSXHOnQueryConfig(@PathVariable("selectorid") String selectorid,HttpServletRequest request, HttpServletResponse response) {
		ResultMessage msg = new ResultMessage();
		msg=selectorManagerService.ResetSXHOnQueryConfig(selectorid,request);
		return msg;
	}
	/*****************************************查询条件配置*****************************************/
	
	/*****************************************查询排序配置*****************************************/
	
	/**
	 * 选择器查询排序配置页面(URL:"/sortconfig/index/",Method：GET)
	 * @Title: getSortConfigIndex
	 * @author:俞学斌
	 * @date：2016年12月20日 15:37:28
	 * @return
	 */
	@RequestMapping(value = "/sortconfig/index/")
	public String getSortConfigIndex() {
		return prefix + "handlermapping/sortconfig";
	}
	
	/**
	 * 获取选择器查询排序列表(URL:"/sortconfig/",Method：GET)
	 * @Title: GetSortConfig
	 * @author:俞学斌
	 * @date：2016年12月20日 15:37:28
	 * @return
	 */
	@RequestMapping(value = "/sortconfig/", method = RequestMethod.GET)
	public @ResponseBody Message GetSortConfig(HttpServletRequest request, HttpServletResponse response) {
		String selectorid="";
		try {
			selectorid = RequestHelper.getParam(request, "selectorid");
		} catch (Exception e) {
		}
		Message m=selectorManagerService.GetSortConfig(selectorid,request);
		return m;
	}
	
	/**
	 * 新增或保存选择器查询排序 (URL:"/sortconfig/{selectorid}/",Method：POST)
	 * @Title: AddOrUpdateSortConfig
	 * @author:俞学斌
	 * @date：2016年12月20日 15:37:28
	 * @return
	 */
	@RequestMapping(value = "/sortconfig/{selectorid}/", method = RequestMethod.POST)
	public @ResponseBody ResultMessage AddOrUpdateSortConfig(@PathVariable("selectorid") String selectorid,HttpServletRequest request, HttpServletResponse response) {
		ResultMessage msg = new ResultMessage();
		msg=selectorManagerService.AddOrUpdateSortConfig(selectorid,request);
		return msg;
	}
	
	/**
	 * 删除选择器查询排序(URL:"/sortconfig/{id}",Method：DELETE)
	 * @Title: RemoveSortConfig
	 * @author:俞学斌
	 * @date：2016年12月20日 15:37:28
	 * @return
	 */
	@RequestMapping(value = "/sortconfig/{id}", method = RequestMethod.DELETE)
	public @ResponseBody ResultMessage RemoveSortConfig(@PathVariable("id") String id,HttpServletRequest request, HttpServletResponse response) {
		ResultMessage msg = new ResultMessage();
		msg=selectorManagerService.RemoveSortConfig(id,request);
		return msg;
	}
	
	/**
	 * 重置选择器查询排序顺序 (URL:"/sortconfig/{selectorid}/resetsxh、",Method：POST)
	 * @Title: ResetSXHOnSortConfig
	 * @author:俞学斌
	 * @date：2016年12月20日 15:37:28
	 * @return
	 */
	@RequestMapping(value = "/sortconfig/{selectorid}/resetsxh/", method = RequestMethod.POST)
	public @ResponseBody ResultMessage ResetSXHOnSortConfig(@PathVariable("selectorid") String selectorid,HttpServletRequest request, HttpServletResponse response) {
		ResultMessage msg = new ResultMessage();
		msg=selectorManagerService.ResetSXHOnSortConfig(selectorid,request);
		return msg;
	}
	/*****************************************查询排序配置*****************************************/
	
	/*****************************************查询结果配置*****************************************/

	/**
	 * 选择器查询结果配置页面(URL:"/gridconfig/index/",Method：GET)
	 * @Title: getGridConfigIndex
	 * @author:俞学斌
	 * @date：2016年12月20日 15:37:28
	 * @return
	 */
	@RequestMapping(value = "/gridconfig/index/")
	public String getGridConfigIndex() {
		return prefix + "handlermapping/gridconfig";
	}
	
	/**
	 * 获取选择器查询结果列表(URL:"/gridconfig/",Method：GET)
	 * @Title: GetGridConfig
	 * @author:俞学斌
	 * @date：2016年12月20日 15:37:28
	 * @return
	 */
	@RequestMapping(value = "/gridconfig/", method = RequestMethod.GET)
	public @ResponseBody Message GetGridConfig(HttpServletRequest request, HttpServletResponse response) {
		String selectorid="";
		try {
			selectorid = RequestHelper.getParam(request, "selectorid");
		} catch (Exception e) {
		}
		Message m=selectorManagerService.GetGridConfig(selectorid,request);
		return m;
	}
	
	/**
	 * 新增或保存选择器查询结果 (URL:"/gridconfig/{selectorid}/",Method：POST)
	 * @Title: AddOrUpdateGridConfig
	 * @author:俞学斌
	 * @date：2016年12月20日 15:37:28
	 * @return
	 */
	@RequestMapping(value = "/gridconfig/{selectorid}/", method = RequestMethod.POST)
	public @ResponseBody ResultMessage AddOrUpdateGridConfig(@PathVariable("selectorid") String selectorid,HttpServletRequest request, HttpServletResponse response) {
		ResultMessage msg = new ResultMessage();
		msg=selectorManagerService.AddOrUpdateGridConfig(selectorid,request);
		return msg;
	}
	
	/**
	 * 删除选择器查询结果(URL:"/gridconfig/{id}",Method：DELETE)
	 * @Title: RemoveGridConfig
	 * @author:俞学斌
	 * @date：2016年12月20日 15:37:28
	 * @return
	 */
	@RequestMapping(value = "/gridconfig/{id}", method = RequestMethod.DELETE)
	public @ResponseBody ResultMessage RemoveGridConfig(@PathVariable("id") String id,HttpServletRequest request, HttpServletResponse response) {
		ResultMessage msg = new ResultMessage();
		msg=selectorManagerService.RemoveGridConfig(id,request);
		return msg;
	}
	
	/**
	 * 重置选择器查询结果顺序 (URL:"/gridconfig/{selectorid}/resetsxh、",Method：POST)
	 * @Title: ResetSXHOnGridConfig
	 * @author:俞学斌
	 * @date：2016年12月20日 15:37:28
	 * @return
	 */
	@RequestMapping(value = "/gridconfig/{selectorid}/resetsxh/", method = RequestMethod.POST)
	public @ResponseBody ResultMessage ResetSXHOnGridConfig(@PathVariable("selectorid") String selectorid,HttpServletRequest request, HttpServletResponse response) {
		ResultMessage msg = new ResultMessage();
		msg=selectorManagerService.ResetSXHOnGridConfig(selectorid,request);
		return msg;
	}
	/*****************************************查询结果配置*****************************************/
	
	/*****************************************结果常量配置*****************************************/

	/**
	 * 选择器结果常量配置页面(URL:"/resultconfig/index/",Method：GET)
	 * @Title: getResultConfigIndex
	 * @author:俞学斌
	 * @date：2016年12月20日 15:37:28
	 * @return
	 */
	@RequestMapping(value = "/resultconfig/index/")
	public String getResultConfigIndex() {
		return prefix + "handlermapping/resultconfig";
	}
	
	/**
	 * 获取选择器查询结果常量(URL:"/resultconfig/",Method：GET)
	 * @Title: GetResultConfig
	 * @author:俞学斌
	 * @date：2016年12月20日 15:37:28
	 * @return
	 */
	@RequestMapping(value = "/resultconfig/", method = RequestMethod.GET)
	public @ResponseBody Message GetResultConfig(HttpServletRequest request, HttpServletResponse response) {
		String selectorid="";
		try {
			selectorid = RequestHelper.getParam(request, "selectorid");
		} catch (Exception e) {
		}
		Message m=selectorManagerService.GetResultConfig(selectorid,request);
		return m;
	}
	
	/**
	 * 新增或保存选择器结果常量 (URL:"/resultconfig/{selectorid}/",Method：POST)
	 * @Title: AddOrUpdateResultConfig
	 * @author:俞学斌
	 * @date：2016年12月20日 15:37:28
	 * @return
	 */
	@RequestMapping(value = "/resultconfig/{selectorid}/", method = RequestMethod.POST)
	public @ResponseBody ResultMessage AddOrUpdateResultConfig(@PathVariable("selectorid") String selectorid,HttpServletRequest request, HttpServletResponse response) {
		ResultMessage msg = new ResultMessage();
		msg=selectorManagerService.AddOrUpdateResultConfig(selectorid,request);
		return msg;
	}
	
	/**
	 * 删除选择器结果常量(URL:"/resultconfig/{id}",Method：DELETE)
	 * @Title: RemoveResultConfig
	 * @author:俞学斌
	 * @date：2016年12月20日 15:37:28
	 * @return
	 */
	@RequestMapping(value = "/resultconfig/{id}", method = RequestMethod.DELETE)
	public @ResponseBody ResultMessage RemoveResultConfig(@PathVariable("id") String id,HttpServletRequest request, HttpServletResponse response) {
		ResultMessage msg = new ResultMessage();
		msg=selectorManagerService.RemoveResultConfig(id,request);
		return msg;
	}
	/*****************************************结果常量配置*****************************************/
	
	/*****************************************结果详情配置*****************************************/

	/**
	 * 选择器查询结果详情页面(URL:"/detailconfig/index/",Method：GET)
	 * @Title: getDetailConfigIndex
	 * @author:俞学斌
	 * @date：2016年12月20日 15:37:28
	 * @return
	 */
	@RequestMapping(value = "/detailconfig/index/")
	public String getDetailConfigIndex() {
		return prefix + "handlermapping/detailconfig";
	}
	
	/**
	 * 获取选择器结果详情列表(URL:"/detailconfig/",Method：GET)
	 * @Title: GetDetailConfig
	 * @author:俞学斌
	 * @date：2016年12月20日 15:37:28
	 * @return
	 */
	@RequestMapping(value = "/detailconfig/", method = RequestMethod.GET)
	public @ResponseBody Message GetDetailConfig(HttpServletRequest request, HttpServletResponse response) {
		String selectorid="";
		try {
			selectorid = RequestHelper.getParam(request, "selectorid");
		} catch (Exception e) {
		}
		Message m=selectorManagerService.GetDetailConfig(selectorid,request);
		return m;
	}
	
	/**
	 * 新增或保存选择器结果详情 (URL:"/detailconfig/{selectorid}/",Method：POST)
	 * @Title: AddOrUpdateDetailConfig
	 * @author:俞学斌
	 * @date：2016年12月20日 15:37:28
	 * @return
	 */
	@RequestMapping(value = "/detailconfig/{selectorid}/", method = RequestMethod.POST)
	public @ResponseBody ResultMessage AddOrUpdateDetailConfig(@PathVariable("selectorid") String selectorid,HttpServletRequest request, HttpServletResponse response) {
		ResultMessage msg = new ResultMessage();
		msg=selectorManagerService.AddOrUpdateDetailConfig(selectorid,request);
		return msg;
	}
	
	/**
	 * 删除选择器结果详情(URL:"/detailconfig/{id}",Method：DELETE)
	 * @Title: RemoveDetailConfig
	 * @author:俞学斌
	 * @date：2016年12月20日 15:37:28
	 * @return
	 */
	@RequestMapping(value = "/detailconfig/{id}", method = RequestMethod.DELETE)
	public @ResponseBody ResultMessage RemoveDetailConfig(@PathVariable("id") String id,HttpServletRequest request, HttpServletResponse response) {
		ResultMessage msg = new ResultMessage();
		msg=selectorManagerService.RemoveDetailConfig(id,request);
		return msg;
	}
	
	/**
	 * 重置选择器查询结果详情 (URL:"/detailconfig/{selectorid}/resetsxh、",Method：POST)
	 * @Title: ResetSXHOnDetailConfig
	 * @author:俞学斌
	 * @date：2016年12月20日 15:37:28
	 * @return
	 */
	@RequestMapping(value = "/detailconfig/{selectorid}/resetsxh/", method = RequestMethod.POST)
	public @ResponseBody ResultMessage ResetSXHOnDetailConfig(@PathVariable("selectorid") String selectorid,HttpServletRequest request, HttpServletResponse response) {
		ResultMessage msg = new ResultMessage();
		msg=selectorManagerService.ResetSXHOnDetailConfig(selectorid,request);
		return msg;
	}
	/*****************************************结果详情配置*****************************************/
	
	/*****************************************复制选择器*****************************************/
	/**
	 * 复制选择器 (URL:"/copyselector/{selectorid}/",Method：POST)
	 * @Title: CopySelector
	 * @author:俞学斌
	 * @date：2016年12月20日 15:37:28
	 * @return
	 */
	@RequestMapping(value = "/copyselector/{selectorid}/", method = RequestMethod.POST)
	public @ResponseBody ResultMessage CopySelector(@PathVariable("selectorid") String selectorid,HttpServletRequest request, HttpServletResponse response) {
		ResultMessage msg = new ResultMessage();
		msg=selectorManagerService.CopySelector(selectorid,request);
		return msg;
	}
	/*****************************************复制选择器*****************************************/
}
