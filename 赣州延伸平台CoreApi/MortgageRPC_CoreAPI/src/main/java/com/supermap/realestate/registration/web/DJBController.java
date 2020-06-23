package com.supermap.realestate.registration.web;

import java.util.ArrayList;
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

import com.supermap.realestate.registration.ViewClass.DJBDTO;
import com.supermap.realestate.registration.ViewClass.FDCQDB;
import com.supermap.realestate.registration.ViewClass.HYDB;
import com.supermap.realestate.registration.ViewClass.NYDDB;
import com.supermap.realestate.registration.ViewClass.NoticeBook;
import com.supermap.realestate.registration.ViewClass.SLLMDB;
import com.supermap.realestate.registration.ViewClass.SYQZD;
import com.supermap.realestate.registration.service.DJBService;
import com.supermap.realestate.registration.util.ConstValue;
import com.supermap.realestate.registration.util.YwLogUtil;
import com.supermap.wisdombusiness.web.Message;
import com.supermap.wisdombusiness.web.ResultMessage;

/**
 * 
 * @Description:登记簿Controller 跟登记簿相关的都放在这里边
 * @author 刘树峰
 * @date 2015年6月12日 上午11:46:22
 * @Copyright SuperMap
 */
@Controller
@RequestMapping("/djb")
public class DJBController {

	/** 登记薄service */
	@Autowired
	private DJBService djbService;

	/**
	 * 获取建设用地使用权的登记簿预览信息（URL:"/{xmbh}/{qlid}/djb",Method：POST）
	 * 
	 * @param xmbh
	 * @param djdyid
	 * @param request
	 * @param response
	 * @author diaoliwei
	 * @return
	 */
	@RequestMapping(value = "/{xmbh}/{qlid}/djb")
	public @ResponseBody Message GetDJB(@PathVariable("xmbh") String xmbh, @PathVariable("qlid") String qlid,
			HttpServletRequest request, HttpServletResponse response) {
		Message message = new Message();
		DJBDTO djb = new DJBDTO();
		djb = djbService.GetDJB(xmbh, qlid);
		List<Object> list = new ArrayList<Object>();
		list.add(djb);
		Map m=djbService.getZXDJBInfo(xmbh, qlid);
		if(null!=m){
			list.add(1,m);
		}
		message.setRows(list);
		message.setTotal(list.size());
		return message;
	}

	/**
	 * 获取海域登记簿预览信息（URL:"/{xmbh}/{qlid}/hydjb",Method：POST）
	 * 
	 * @作者 海豹
	 * @创建时间 2015年6月26日上午5:32:57
	 * @param xmbh
	 * @param qlid
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/{xmbh}/{qlid}/hydjb", method = RequestMethod.POST)
	public @ResponseBody Message GetHYDJB(@PathVariable("xmbh") String xmbh, @PathVariable("qlid") String qlid,
			HttpServletRequest request, HttpServletResponse response) {
		Message message = new Message();
		HYDB hydb = new HYDB();
		hydb = djbService.GetHYDB(xmbh, qlid);
		List<HYDB> list = new ArrayList<HYDB>();
		list.add(hydb);
		message.setRows(list);
		message.setTotal(list.size());
		return message;
	}

	/**
	 * 获取森林林木登记簿预览信息（URL:"/{xmbh}/{qlid}/lddjb",Method：POST）
	 * 
	 * @作者 海豹
	 * @创建时间 2015年6月27日下午10:07:30
	 * @param xmbh
	 * @param qlid
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/{xmbh}/{qlid}/lddjb", method = RequestMethod.POST)
	public @ResponseBody Message GetLDDJB(@PathVariable("xmbh") String xmbh, @PathVariable("qlid") String qlid,
			HttpServletRequest request, HttpServletResponse response) {
		Message message = new Message();
		SLLMDB lddb = new SLLMDB();
		lddb = djbService.GetSLLMDB(xmbh, qlid);
		List<SLLMDB> list = new ArrayList<SLLMDB>();
		list.add(lddb);
		message.setRows(list);
		message.setTotal(list.size());
		return message;
	}

	/**
	 * 获取集体土地所有权登记簿预览信息（URL:"/{xmbh}/{qlid}/jttdsyqdjb",Method：POST）
	 * 
	 * @作者 海豹
	 * @创建时间 2015年7月15日下午9:29:28
	 * @param xmbh
	 * @param qlid
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/{xmbh}/{qlid}/jttdsyqdjb", method = RequestMethod.POST)
	public @ResponseBody Message GetJTTDSYQDJB(@PathVariable("xmbh") String xmbh, @PathVariable("qlid") String qlid,
			HttpServletRequest request, HttpServletResponse response) {
		Message message = new Message();
		SYQZD syqzd = new SYQZD();
		syqzd = djbService.GetJTTDSYQDB(xmbh, qlid);
		List<SYQZD> list = new ArrayList<SYQZD>();
		list.add(syqzd);
		message.setRows(list);
		message.setTotal(list.size());
		return message;
	}

	/**
	 * 获取国有农用地使用权登记簿预览信息（URL:"/{xmbh}/{qlid}/jttdsyqdjb",Method：POST）
	 * @param xmbh
	 * @param qlid
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/{xmbh}/{qlid}/nyddjb", method = RequestMethod.POST)
	public @ResponseBody Message GetNYDSHYQDJB(@PathVariable("xmbh") String xmbh, @PathVariable("qlid") String qlid,
			HttpServletRequest request, HttpServletResponse response) {
		Message message = new Message();
		NYDDB syqzd = new NYDDB();
		syqzd = djbService.GetNYDSHYQDB(xmbh, qlid);
		List<NYDDB> list = new ArrayList<NYDDB>();
		list.add(syqzd);
		message.setRows(list);
		message.setTotal(list.size());
		return message;
	}

	/**
	 * 
	 * 获取房地产权（独幢、层、套、间房间、项目内多幢）登记簿预览信息（URL:"{xmbh}/{djdyid}/fdcqdbInfo",Method：
	 * GET）
	 * 
	 * @作者 海豹
	 * @创建时间 2015年6月8日上午12:19:55
	 * @param xmbh
	 *            项目编号
	 * @param djdyid
	 *            登记单元ID
	 * @return
	 */
	@RequestMapping(value = "{xmbh}/{djdyid}/fdcqdbInfo", method = RequestMethod.GET)
	public @ResponseBody FDCQDB fdcqdbInfo(@PathVariable String xmbh, @PathVariable String djdyid) {
		FDCQDB fdcqdbInfo = djbService.getFDCQDB(xmbh, djdyid);
		return fdcqdbInfo;
	}
	@RequestMapping(value = "{xmbh}/{djdyid}/zxfdcqdbInfo", method = RequestMethod.GET)
	public @ResponseBody Message zxfdcqdbInfo(@PathVariable String xmbh, @PathVariable String djdyid) {
		FDCQDB fdcqdbInfo = djbService.getFDCQDB(xmbh, djdyid);
		Message message = new Message();
		List<Object> list = new ArrayList<Object>();
		list.add(fdcqdbInfo);
		list.add(1,djbService.getZXDJBInfo(xmbh, djdyid));
		message.setRows(list);
		message.setTotal(list.size());
		return message;
	}

	/**
	 * 获取商品房买卖+预抵押转现登记簿预览信息
	 * 
	 * @作者 diaoliwei
	 * @创建时间 2015年7月24日下午10:23:03
	 * @param xmbh
	 * @param djdyid
	 * @return
	 */
	@RequestMapping(value = "{xmbh}/{djdyid}/salesHouseInfo", method = RequestMethod.GET)
	public @ResponseBody FDCQDB salesHouseAndRightInfo(@PathVariable String xmbh, @PathVariable String djdyid) {
		FDCQDB fdcqdbInfo = djbService.getCombFDCQDB(xmbh, djdyid);
		return fdcqdbInfo;
	}

	/**
	 * 
	 * 获取预告登记登记簿预览信息（URL:"{xmbh}/{djdyid}/noticebook",Method：GET）
	 * 
	 * @作者 李想
	 * @创建时间 2015年9月1日上午1:25:55
	 * @param xmbh
	 *            项目编号
	 * @param qlid
	 *            权利ID
	 * @return
	 */
	@RequestMapping(value = "{xmbh}/{qlid}/noticebookbyqlid", method = RequestMethod.GET)
	public @ResponseBody NoticeBook NoticeBookInfoByQLID(@PathVariable String xmbh, @PathVariable("qlid") String qlid) {
		NoticeBook nBook = djbService.getNoticeBookByQLID(xmbh, qlid);
		return nBook;
	}

	/**
	 * 
	 * 获取预告登记登记簿预览信息（URL:"{xmbh}/{djdyid}/noticebook",Method：GET）
	 * 
	 * @作者 李想
	 * @创建时间 2015年6月24日上午14:44:55
	 * @param xmbh
	 *            项目编号
	 * @param djdyid
	 *            登记单元ID
	 * @return
	 */
	@RequestMapping(value = "{xmbh}/{djdyid}/noticebook", method = RequestMethod.GET)
	public @ResponseBody NoticeBook NoticeBookInfo(@PathVariable String xmbh, @PathVariable String djdyid) {
		NoticeBook nBook = djbService.getNoticeBook(xmbh, djdyid);
		return nBook;
	}

	/**
	 * 获取抵押权登记簿预览信息（URL:"{xmbh}/{qlid}/dyq",Method：GET）
	 * 
	 * @作者 刘树峰
	 * @创建时间 2015年6月15日上午3:14:26
	 * @param xmbh
	 * @param djdyid
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "{xmbh}/{qlid}/dyq", method = RequestMethod.GET)
	public @ResponseBody Map getDYQDJBInfo(@PathVariable String xmbh, @PathVariable("qlid") String qlid) {
		Map map = djbService.getDYQDJBInfo(xmbh, qlid);
		return map;
	}
	
	/**
	 * 获取地役权登簿信息
	 * @param xmhb
	 * @param qlid
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value="{xmbh}/{qlid}/dyqdjb",method=RequestMethod.GET)
	public @ResponseBody Map getDyqDjbInfo(@PathVariable("xmbh") String xmbh,@PathVariable("qlid") String qlid){
		Map map = djbService.getDyqDjbInfos(xmbh, qlid);
		return map;
		
	}

	/**
	 * 获取查封登记簿预览信息（URL:"{xmbh}/{qlid}/cfdj",Method：GET）
	 * 
	 * @作者 WUZ
	 * @param xmbh
	 * @param qlid
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "{xmbh}/{qlid}/cfdj", method = RequestMethod.GET)
	public @ResponseBody Map getCFDJBInfo(@PathVariable String xmbh, @PathVariable String qlid) {
		Map map = djbService.getCFDJBInfo(xmbh, qlid);
		return map;
	}

	/**
	 * 获取解封登记簿预览信息（URL:"{xmbh}/{qlid}/{oldqlid}/jfdj",Method：GET）
	 * 
	 * @作者 WUZ
	 * @param xmbh
	 * @param qlid
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "{xmbh}/{qlid}/{oldqlid}/jfdj", method = RequestMethod.GET)
	public @ResponseBody Map getJFDJBInfo(@PathVariable String xmbh, @PathVariable String qlid,
			@PathVariable String oldqlid) {
		Map map = djbService.getJFDJBInfo(xmbh, qlid, oldqlid);
		return map;
	}

	/**
	 * 获取异议登记的登记簿预览信息（URL:"/{xmbh}/{qlid}/yydjb",Method：POST）
	 * 
	 * @作者：俞学斌
	 * @创建时间 2015年6月30日 下午5:10:01
	 * @param xmbh
	 * @param qlid
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/{xmbh}/{qlid}/yydjb", method = RequestMethod.POST)
	public @ResponseBody Message GetYYDJB(@PathVariable("xmbh") String xmbh, @PathVariable("qlid") String qlid,
			HttpServletRequest request, HttpServletResponse response) {
		Message message = new Message();
		Map map = djbService.getYYDJBInfo(xmbh, qlid);
		List<Map> list = new ArrayList<Map>();
		list.add(map);
		message.setRows(list);
		message.setTotal(list.size());
		return message;
	}

	/**
	 * 生成不动产权证号（URL:"/{xmbh}/{qlid}/bdcqzh",Method：GET）
	 * 
	 * @作者 孙海豹
	 * @创建时间 2015年6月13日下午10:43:48
	 * @param qhdm
	 * @param bdcdyh
	 * @param xmbh
	 * @return
	 */
	@RequestMapping(value = "/{xmbh}/{qlid}/bdcqzh", method = RequestMethod.GET)
	public @ResponseBody String getBdcqzh(@PathVariable("xmbh") String xmbh, @PathVariable("qlid") String qlid) {
		String type = "GETBDCQZH"; // 标示执行存储过程时生成不动产权证号
		String bdcqzh = djbService.createBDCQZH(xmbh, qlid, type);
		YwLogUtil.addYwLog("生成不动产权证号：" + bdcqzh, ConstValue.SF.YES.Value, ConstValue.LOG.ADD);
		return bdcqzh;
	}

	/**
	 * 生成不动产登记证明号（URL:"/{xmbh}/{qlid}/bdczmh",Method：GET）
	 * 
	 * @作者 刘树峰
	 * @创建时间 2015年6月15日上午4:54:06
	 * @param xmbh
	 * @param zsid
	 * @return
	 */
	@RequestMapping(value = "/{xmbh}/{qlid}/bdczmh", method = RequestMethod.GET)
	public @ResponseBody String createBDCDJZMH(@PathVariable String xmbh, @PathVariable("qlid") String qlid) {
		String type = "GETBDCDJZM"; // 标示执行存储过程时生成不动产登记证明号
		String str = djbService.createBDCZMH(xmbh, qlid, type);
		YwLogUtil.addYwLog("生成不动产登记证明号：" + str, ConstValue.SF.YES.Value, ConstValue.LOG.ADD);
		return str;
	}

	/**
	 * 根据权利类型生成不动产权证号（URL:"/{xmbh}/{qllx}/bdcqzh",Method：GET）
	 * 
	 * @作者 俞学斌
	 * @创建时间 2015年9月2日上午1:00:48
	 * @param xmbh
	 * @param qllx
	 * @return
	 */
	@RequestMapping(value = "/{xmbh}/{qllx}/bdcqzhbyqllx", method = RequestMethod.GET)
	public @ResponseBody String getBDCQZHByQLLX(@PathVariable("xmbh") String xmbh, @PathVariable("qllx") String qllx) {
		String type = "GETBDCQZH"; // 标示执行存储过程时生成不动产权证号
		String bdcqzh = djbService.createBDCQZHByQLLX(xmbh, qllx, type);
		return bdcqzh;
	}

	/**
	 * 根据权利类型生成不动产登记证明号（URL:"/{xmbh}/{qllx}/bdczmhbyqllx",Method：GET）
	 * 
	 * @作者 俞学斌
	 * @创建时间 2015年9月2日上午1:00:06
	 * @param xmbh
	 * @param qllx
	 * @return
	 */
	@RequestMapping(value = "/{xmbh}/{qllx}/bdczmhbyqllx", method = RequestMethod.GET)
	public @ResponseBody String createBDCDJZMHByQLLX(@PathVariable String xmbh, @PathVariable("qllx") String qllx) {
		String type = "GETBDCDJZM"; // 标示执行存储过程时生成不动产登记证明号
		String str = djbService.createBDCZMHByQLLX(xmbh, qllx, type);
		YwLogUtil.addYwLog("根据权利类型生成不动产登记证明号：" + str, ConstValue.SF.YES.Value, ConstValue.LOG.ADD);
		return str;
	}
	
	/**
	 *  通过项目编号检查权利表是否有对应的权利人
	 * @param xmbh
	 * @return
	 */
	@RequestMapping(value="/checkqlrbyql/{xmbh}",method=RequestMethod.GET)
	public @ResponseBody ResultMessage checkQlrByQl(@PathVariable String xmbh){
		return djbService.checkQlrByQl(xmbh);
	}
	/**
	 * 获取解除限制登记簿预览信息（URL:"{xmbh}/{bdcdyid}/zxxzdj",Method：GET）
	 * 
	 * @作者 yuxuebin
	 * @param xmbh
	 * @param bdcdyid
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "{xmbh}/{bdcdyid}/zxxzdj", method = RequestMethod.GET)
	public @ResponseBody List<Map> getZXXZDJBInfo(@PathVariable String xmbh, @PathVariable String bdcdyid) {
		List<Map> list = djbService.getZXXZDJBInfo(xmbh, bdcdyid);
		return list;
	}

	/**
	 * 根据项目编号生成不动产权证号或不动产证明号（URL:"/{xmbh}/qzhorzmh",Method：GET）
	 * 
	 * @作者 俞学斌
	 * @创建时间 2016年05月08日 21:53:48
	 * @param xmbh
	 * @return
	 */
	@RequestMapping(value = "/{xmbh}/qzhorzmh", method = RequestMethod.GET)
	public @ResponseBody ResultMessage getQZHORZMHByXMBH(@PathVariable("xmbh") String xmbh) {
		ResultMessage msg = djbService.getQZHORZMHByXMBH(xmbh);
		return msg;
	}
	
	/**
	 * 根据project_id 获取 流程编号进行判断  登簿是否重新生成权证号
	 * @author luml  2018-2-2  10:37
	 * @param project_id
	 * @return  msg
	 */
	@RequestMapping(value = "/{project_id}/isNewQZH", method = RequestMethod.GET)
	public @ResponseBody ResultMessage isNewQZH(@PathVariable("project_id") String project_id) {
		ResultMessage msg = djbService.isNewQZH(project_id);
		return msg;
	}
	
}
