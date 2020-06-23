package com.supermap.realestate.registration.web;

import java.io.UnsupportedEncodingException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.supermap.realestate.registration.model.BDCS_FJMB;
import com.supermap.realestate.registration.model.BDCS_FJMB_EX;
import com.supermap.realestate.registration.util.ConstHelper;
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
 * 附记
 * @ClassName: 
 * @author  
 * @date  
 */
@Controller
@RequestMapping("/fj")
public class FJController {

	@Autowired
	private CommonDao dao;

	/**
	 * 分页获取附记模版（URL:"/fjs/",Method：GET）
	 * @Title: QueryFJS
	 * @Author: 
	 * @date： 
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "/fjs/", method = RequestMethod.GET)
	public @ResponseBody Message QueryFJS(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
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
			Page p = dao.getPageDataByHql(BDCS_FJMB.class, _hql, page, rows);
			if (p != null) {
				msg.setTotal(p.getTotalCount());
				msg.setRows(p.getResult());
			}
		} else {
			msg.setSuccess("false");
			msg.setMsg("登录超时，请重新登录！");
		}
		 
		return msg;
	}

	/**
	 * 添加附记模版（URL:"/fjs/",Method：POST）
	 * @Title: AddFJS
	 * @author: 
	 * @date： 
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "/fjs/", method = RequestMethod.POST)
	public @ResponseBody Message AddFJS(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		Message msg = new Message();
		User user = Global.getCurrentUserInfo();
		if (user == null) {
			msg.setSuccess("false");
			msg.setMsg("登录超时，请重新登录！");
			return msg;
		}
		String fj =request.getParameter("fj");
		if (StringHelper.isEmpty(fj)) {
			msg.setSuccess("false");
			msg.setMsg("登记原因不能为空");
			return msg;
		}
		
		BDCS_FJMB fjmb = new BDCS_FJMB();
		fjmb.setUSERID(user.getId());
		fjmb.setUSERNAME(user.getUserName());
		fjmb.setFJ(fj);
		fjmb.setCREATETIME(new Date());
		fjmb.setMODIFYTIME(new Date());
		fjmb.setId((String) SuperHelper.GeneratePrimaryKey());
		fjmb.setBZ("");
		dao.save(fjmb);
		dao.flush();
		
		List<BDCS_FJMB> list = new ArrayList<BDCS_FJMB>();
		list.add(fjmb);
		msg.setSuccess("true");
		msg.setMsg("保存成功");
		msg.setRows(list);
		msg.setTotal(1);
		YwLogUtil.addYwLog("附记模版-添加附记模版", ConstValue.SF.YES.Value,ConstValue.LOG.ADD);
		return msg;
	}

	/**
	 * 更新附记模版（URL:"/fjs/{mbid}",Method：POST）
	 * @Title: ModifyFJS 
	 * @author: 
	 * @date： 
	 * @param mbid
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "/fjs/{mbid}", method = RequestMethod.POST)
	public @ResponseBody Message ModifyFJS(@PathVariable("mbid") String mbid, HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
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
		
		String fj =request.getParameter("fj");
		if (StringHelper.isEmpty(fj)) {
			msg.setSuccess("false");
			msg.setMsg("附记不能为空");
			return msg;
		}
		
		
		BDCS_FJMB mb=dao.get(BDCS_FJMB.class, mbid);
		if(mb==null)
		{
			msg.setSuccess("false");
			msg.setMsg("未找到ID为"+mbid+"的附记模版，无法更新！");
			return msg;
		}
		mb.setFJ(fj);
		dao.update(mb);
		dao.flush();
		
		List<BDCS_FJMB> list = new ArrayList<BDCS_FJMB>();
		list.add(mb);
		msg.setSuccess("true");
		msg.setMsg("更新成功");
		msg.setTotal(1);
		msg.setRows(list);
		YwLogUtil.addYwLog("附记模版-更新附记模版", ConstValue.SF.YES.Value,ConstValue.LOG.UPDATE);
		return msg;
	}

	/**
	 * 删除附记模版（URL:"/fjs/{mbid}",Method：DELETE）
	 * @Title:   
	 * @author: 
	 * @date： 
	 * @param mbid
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "/fjs/{mbid}", method = RequestMethod.DELETE)
	public @ResponseBody Message DeleteFJS(@PathVariable("mbid") String mbid, HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
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
		
		BDCS_FJMB mb=dao.get(BDCS_FJMB.class, mbid);
		if(mb==null)
		{
			msg.setSuccess("false");
			msg.setMsg("未找到ID为"+mbid+"的附记模版，无法删除！");
			return msg;
		}
		dao.deleteEntity(mb);
		dao.flush();
		
		List<BDCS_FJMB> list = new ArrayList<BDCS_FJMB>();
		list.add(mb);
		msg.setSuccess("true");
		msg.setMsg("删除成功");
		msg.setTotal(1);
		msg.setRows(list);
		YwLogUtil.addYwLog("附记模版-删除附记模版", ConstValue.SF.YES.Value,ConstValue.LOG.DELETE);
		return msg;
	}
	
	/**
	 * @Title: MBRENDER
	 * @Description: TODO
	 * @Author：赵梦帆
	 * @Data：2016年11月5日 下午3:43:30
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 * @return String
	 */
	@RequestMapping(value = "/render/", method = RequestMethod.POST)
	public @ResponseBody String MBRENDER(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		
		String fj = request.getParameter("fj");
		String xmbh = request.getParameter("xmbh");
		String qlid = request.getParameter("qlid");
		String project_id = request.getParameter("project_id");
		
		Matcher mat = Pattern.compile("(\\{(.| )+?\\})").matcher(fj);
		List<String> vars = new ArrayList<String>();
		int index = 0;
		while (mat.find()) {
			String var = mat.group().toUpperCase().trim();
			var = var.substring(1, var.length()-1);
			fj = fj.replaceFirst("\\{" + var + "\\}", "{"+ index++ +"}");
			vars.add(var);
		}
		
		HashSet<String> has = new HashSet<String>(vars);
		List<BDCS_FJMB_EX> detil = dao.getDataList(BDCS_FJMB_EX.class, " 1 > 0");
		Map<String, String> param = new HashMap<String, String>();
		for (String var : has) {
			for (BDCS_FJMB_EX bdcs_FJMB_EX : detil) {
				if(var.equals(bdcs_FJMB_EX.getName().toUpperCase().trim())){
					String sql = bdcs_FJMB_EX.getSql().toUpperCase();
					Map<String, String> paramMap = new HashMap<String, String>();

					if(sql.contains("=:XMBH")&&!StringHelper.isEmpty(xmbh))
						paramMap.put("XMBH", xmbh);
					if(sql.contains("=:QLID")&&!StringHelper.isEmpty(qlid))
						paramMap.put("QLID", qlid);
					if(sql.contains("=:PROJECT_ID")&&!StringHelper.isEmpty(project_id))
						paramMap.put("PROJECT_ID", project_id);
					if(paramMap.size()<1){
						param .put(var, "----");
						continue;
					}
					String con = bdcs_FJMB_EX.getCondition().toUpperCase().trim();
					List<Map> results = dao.getDataListByFullSql(sql, paramMap);
					if(results!=null&&results.size()>0){
						String re = "";
						for (Map map : results) {
							if(!StringHelper.isEmpty(map.get(con))){
								String constvalue = ConstHelper.getNameByValue(con, map.get(con)+"");
								if(StringHelper.isEmpty(constvalue)){
									re += map.get(con) + ",";
								}else{
									re += constvalue + ",";
								}								
							}else{
								re = "----" ;
							}
						}
						if(re.endsWith(",")){
							re = re.substring(0, re.length()-1);
						}
						param .put(var, re);
					}else{
						param .put(var, "----");
					}
				}
			}
		}
		Object[] anse = vars.toArray();
		for (int i = 0; i < anse.length; i++) {
			if(param.get(anse[i])!=null)
				anse[i] = param.get(anse[i]);
			else
				anse[i]="----";
		}
		
		fj = MessageFormat.format(fj, anse);
		return fj;
	}

	/**
	 * @Title: GetFJMB
	 * @Description: 获取模板配置
	 * @Author：赵梦帆
	 * @Data：2016年11月5日 下午8:20:23
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 * @return Message
	 */
	@RequestMapping(value = "/fjmb", method = RequestMethod.GET)
	public @ResponseBody Message GetFJMB(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		Integer page = 1;
		if (request.getParameter("page") != null) {
			page = Integer.parseInt(request.getParameter("page"));
		}
		Integer rows = 10;
		if (request.getParameter("rows") != null) {
			rows = Integer.parseInt(request.getParameter("rows"));
		}
		Page P = dao.getPageDataByHql(BDCS_FJMB_EX.class, " 1>0 ORDER BY ID DESC" , page , rows);
		Message msg = new Message();
		msg.setSuccess("true");
		if(P!=null){
			msg.setRows(P.getResult());
			msg.setTotal(P.getTotalCount());
		}
		return msg;
	}

	/**
	 * @Title: AddFJMB
	 * @Description: 添加模板配置
	 * @Author：赵梦帆
	 * @Data：2016年11月5日 下午8:35:46
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 * @return Message
	 */
	@RequestMapping(value = "/fjmb", method = RequestMethod.POST)
	public @ResponseBody Message AddFJMB(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		String id = request.getParameter("id");
		String sql = request.getParameter("sql");
		String condition = request.getParameter("condition");
		BDCS_FJMB_EX mb = new BDCS_FJMB_EX();
		mb.setName(id);
		mb.setSql(sql);
		mb.setCondition(condition);
		dao.save(mb);
		dao.flush();
		List<BDCS_FJMB_EX> list = new ArrayList<BDCS_FJMB_EX>();
		list.add(mb);
		Message msg = new Message();
		msg .setSuccess("true");
		msg.setMsg("保存成功");
		msg.setRows(list);
		msg.setTotal(1);
		YwLogUtil.addYwLog("附记模版配置-添加附记模版配置", ConstValue.SF.YES.Value,ConstValue.LOG.ADD);
		return msg;
	}
	
	/**
	 * @Title: ModifyFJMB
	 * @Description: 更新模板配置
	 * @Author：赵梦帆
	 * @Data：2016年11月5日 下午8:23:17
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 * @return Message
	 */
	@RequestMapping(value = "/fjmb/{mbid}", method = RequestMethod.POST)
	public @ResponseBody Message ModifyFJMB(@PathVariable("mbid") String mbid, HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		String id = request.getParameter("id");
		String sql = request.getParameter("sql");
		String condition = request.getParameter("condition");
		BDCS_FJMB_EX mb=dao.get(BDCS_FJMB_EX.class, mbid);
		Message msg = new Message();
		if(mb==null)
		{
			msg.setSuccess("false");
			msg.setMsg("未找到ID为"+mbid+"的附记模版配置，无法更新！");
			return msg;
		}
		mb.setName(id);
		mb.setSql(sql);
		mb.setCondition(condition);
		dao.update(mb);
		dao.flush();
		
		List<BDCS_FJMB_EX> list = new ArrayList<BDCS_FJMB_EX>();
		list.add(mb);
		msg.setSuccess("true");
		msg.setMsg("更新成功");
		msg.setTotal(1);
		msg.setRows(list);
		YwLogUtil.addYwLog("附记模版配置-更新附记模版配置", ConstValue.SF.YES.Value,ConstValue.LOG.UPDATE);
		return msg;
		
	}
	
	/**
	 * @Title: DeleteFJMB
	 * @Description: 删除模板配置
	 * @Author：赵梦帆
	 * @Data：2016年11月5日 下午8:24:19
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 * @return Message
	 */
	@RequestMapping(value = "/fjmb/{mbid}", method = RequestMethod.DELETE)
	public @ResponseBody Message DeleteFJMB(@PathVariable("mbid") String mbid, HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		BDCS_FJMB_EX mb=dao.get(BDCS_FJMB_EX.class, mbid);
		Message msg = new Message();
		if(mb==null)
		{
			msg .setSuccess("false");
			msg.setMsg("未找到ID为"+mbid+"的附记模版配置，无法删除！");
			return msg;
		}
		dao.deleteEntity(mb);
		dao.flush();
		
		List<BDCS_FJMB_EX> list = new ArrayList<BDCS_FJMB_EX>();
		list.add(mb);
		msg.setSuccess("true");
		msg.setMsg("删除成功");
		msg.setTotal(1);
		msg.setRows(list);
		YwLogUtil.addYwLog("附记模版配置-删除附记模版配置", ConstValue.SF.YES.Value,ConstValue.LOG.DELETE);
		return msg;
	}
	
	/**
	 * @Title: EditFJMB
	 * @Description: TODO
	 * @Author：赵梦帆
	 * @Data：2016年11月5日 下午9:00:29
	 * @param request
	 * @param response
	 * @return
	 * @return String
	 */
	@RequestMapping(value = "/editfjmb", method = RequestMethod.GET)
	public String EditFJMB(){
		return "/realestate/registration/editFjmb/editfjmb";
	}
	
}
