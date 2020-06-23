package com.supermap.realestate.registration.web;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.supermap.realestate.registration.model.BDCS_DJYYMB;
import com.supermap.realestate.registration.util.ConstValue;
import com.supermap.realestate.registration.util.Global;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.realestate.registration.util.YwLogUtil;
import com.supermap.wisdombusiness.core.SuperHelper;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import com.supermap.wisdombusiness.framework.model.User;
import com.supermap.wisdombusiness.web.Message;
import com.supermap.wisdombusiness.web.ui.Page;

/**
 * 登记原因
 * @ClassName: DJYYController
 * @author liushufeng
 * @date 2015年9月23日 下午6:55:42
 */
@Controller
@RequestMapping("/djyy")
public class DJYYController {

	@Autowired
	private CommonDao dao;

	/**
	 * 分页获取登记原因模版（URL:"/djyys/",Method：GET）
	 * @Title: QueryDJYYS
	 * @Author:liushufeng
	 * @date：2015年9月23日 下午7:10:39
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "/djyys/", method = RequestMethod.GET)
	public @ResponseBody Message QueryDJYYS(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		User user = Global.getCurrentUserInfo();
		Integer page = 1;
		if (request.getParameter("page") != null) {
			page = Integer.parseInt(request.getParameter("page"));
		}
		Integer rows = 10;
		if (request.getParameter("rows") != null) {
			rows = Integer.parseInt(request.getParameter("rows"));
		}
		Message msg = new Message();
		if (user != null) {
			msg.setSuccess("true");
			String userid = user.getId();
			String _hql = "USERID='" + userid + "'";
			Page p = dao.getPageDataByHql(BDCS_DJYYMB.class, _hql, page, rows);
			if (p != null) {
				msg.setTotal(p.getTotalCount());
				msg.setRows(p.getResult());
			}
		} else {
			msg.setSuccess("false");
			msg.setMsg("登录超时，请重新登录！");
		}
		YwLogUtil.addYwLog("登记原因模版", ConstValue.SF.YES.Value,ConstValue.LOG.SEARCH);
		return msg;
	}

	/**
	 * 添加登记原因模版（URL:"/djyys/",Method：POST）
	 * @Title: AddDJYYS
	 * @author:liushufeng
	 * @date：2015年9月23日 下午7:17:54
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "/djyys/", method = RequestMethod.POST)
	public @ResponseBody Message AddDJYYS(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		Message msg = new Message();
		User user = Global.getCurrentUserInfo();
		if (user == null) {
			msg.setSuccess("false");
			msg.setMsg("登录超时，请重新登录！");
			return msg;
		}
		String djyy =request.getParameter("djyy");
		if (StringHelper.isEmpty(djyy)) {
			msg.setSuccess("false");
			msg.setMsg("登记原因不能为空");
			return msg;
		}
		
		BDCS_DJYYMB djyymb = new BDCS_DJYYMB();
		djyymb.setUSERID(user.getId());
		djyymb.setUSERNAME(user.getUserName());
		djyymb.setREASON(djyy);
		djyymb.setCREATETIME(new Date());
		djyymb.setMODIFYTIME(new Date());
		djyymb.setId((String) SuperHelper.GeneratePrimaryKey());
		djyymb.setBZ("");
		dao.save(djyymb);
		dao.flush();
		
		List<BDCS_DJYYMB> list = new ArrayList<BDCS_DJYYMB>();
		list.add(djyymb);
		msg.setSuccess("true");
		msg.setMsg("保存成功");
		msg.setRows(list);
		msg.setTotal(1);
		YwLogUtil.addYwLog("登记原因模版-添加登记原因模版", ConstValue.SF.YES.Value,ConstValue.LOG.ADD);
		return msg;
	}

	/**
	 * 更新登记原因模版（URL:"/djyys/{mbid}",Method：POST）
	 * @Title: ModifyDJYYS 
	 * @author:liushufeng
	 * @date：2015年9月23日 下午7:23:15
	 * @param mbid
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "/djyys/{mbid}", method = RequestMethod.POST)
	public @ResponseBody Message ModifyDJYYS(@PathVariable("mbid") String mbid, HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		Message msg = new Message();
		User user = Global.getCurrentUserInfo();
		if (user == null) {
			msg.setSuccess("false");
			msg.setMsg("登录超时，请重新登录！");
			return msg;
		}
		
		if (StringHelper.isEmpty(mbid)) {
			msg.setSuccess("false");
			msg.setMsg("待更新记录ID不能为空");
			return msg;
		}
		
		String djyy =request.getParameter("djyy");
		if (StringHelper.isEmpty(djyy)) {
			msg.setSuccess("false");
			msg.setMsg("登记原因不能为空");
			return msg;
		}
		
		
		BDCS_DJYYMB mb=dao.get(BDCS_DJYYMB.class, mbid);
		if(mb==null)
		{
			msg.setSuccess("false");
			msg.setMsg("未找到ID为"+mbid+"的登记原因模版，无法更新！");
			return msg;
		}
		mb.setREASON(djyy);
		dao.update(mb);
		dao.flush();
		
		List<BDCS_DJYYMB> list = new ArrayList<BDCS_DJYYMB>();
		list.add(mb);
		msg.setSuccess("true");
		msg.setMsg("更新成功");
		msg.setTotal(1);
		msg.setRows(list);
		YwLogUtil.addYwLog("登记原因模版-更新登记原因模版", ConstValue.SF.YES.Value,ConstValue.LOG.UPDATE);
		return msg;
	}

	/**
	 * 删除登记原因模版（URL:"/djyys/{mbid}",Method：DELETE）
	 * @Title: DeleteDJYYS 
	 * @author:liushufeng
	 * @date：2015年9月23日 下午7:24:55
	 * @param mbid
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "/djyys/{mbid}", method = RequestMethod.DELETE)
	public @ResponseBody Message DeleteDJYYS(@PathVariable("mbid") String mbid, HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		Message msg = new Message();
		User user = Global.getCurrentUserInfo();
		if (user == null) {
			msg.setSuccess("false");
			msg.setMsg("登录超时，请重新登录！");
			return msg;
		}
		
		if (StringHelper.isEmpty(mbid)) {
			msg.setSuccess("false");
			msg.setMsg("待删除记录ID不能为空");
			return msg;
		}
		
		BDCS_DJYYMB mb=dao.get(BDCS_DJYYMB.class, mbid);
		if(mb==null)
		{
			msg.setSuccess("false");
			msg.setMsg("未找到ID为"+mbid+"的登记原因模版，无法删除！");
			return msg;
		}
		dao.deleteEntity(mb);
		dao.flush();
		
		List<BDCS_DJYYMB> list = new ArrayList<BDCS_DJYYMB>();
		list.add(mb);
		msg.setSuccess("true");
		msg.setMsg("删除成功");
		msg.setTotal(1);
		msg.setRows(list);
		YwLogUtil.addYwLog("登记原因模版-删除登记原因模版", ConstValue.SF.YES.Value,ConstValue.LOG.DELETE);
		return msg;
	}
}
