package com.supermap.realestate.registration.web;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sun.org.apache.xerces.internal.impl.xpath.regex.ParseException;
import com.supermap.realestate.registration.model.BDCS_JJSYFSQR;
import com.supermap.realestate.registration.util.ConfigHelper;
import com.supermap.realestate.registration.util.RequestHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import com.supermap.wisdombusiness.web.Message;
import com.supermap.wisdombusiness.web.ui.Page;

/**
 * 经济适用房申请人表
 * @author 海豹
 *
 */
@Controller
@RequestMapping("/jjsyf")
public class JJSYFSQRController {

	@Autowired
	private CommonDao dao;

	/**
	 * 分页获取经济适用房信息
	 * @作者 海豹
	 * @创建时间 2015年9月24日下午3:07:52
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "/sqrs/", method = RequestMethod.GET)
	public @ResponseBody Message QueryJJSYFSQR(HttpServletRequest request,HttpServletResponse response) throws UnsupportedEncodingException {
		Integer page = 1;
		if (request.getParameter("page") != null) {
			page = Integer.parseInt(request.getParameter("page"));
		}
		Integer rows = 10;
		if (request.getParameter("rows") != null) {
			rows = Integer.parseInt(request.getParameter("rows"));
		}
		Message msg = new Message();
		msg.setSuccess("true");
		String _hql = "1='" + 1 + "'";
		String sqrxm=RequestHelper.getParam(request,"sqrxm");
		String zjh=request.getParameter("zjh");
		if(!StringHelper.isEmpty(sqrxm))
		{
			_hql=_hql+" and SQRXM like'%"+sqrxm+"%'";
		}
		if(!StringHelper.isEmpty(zjh))
		{
			_hql=_hql+" and SQRZJH like '%"+zjh+"%'";
		}
		String xzqdm = ConfigHelper.getNameByValue("XZQHDM").toString();
		if("420900".equals(xzqdm)||"420902".equals(xzqdm)){
			String sql="select id,bz1,bz2,a.sqrxm,a.sqrzjh,a.sqrzjlx,to_char(createtime, 'yyyy-mm-dd hh24:mi:ss') createtime from bdck.bdcs_jjsyfsqr  a  where ";
			long a =dao.getCountByFullSql("from ("+(sql+_hql)+")");
			List<Map> result=dao.getPageDataByFullSql(sql+_hql, page, rows);
			List<BDCS_JJSYFSQR> li=new  ArrayList<BDCS_JJSYFSQR>();
			for(Map ma: result){
				BDCS_JJSYFSQR jjs=new BDCS_JJSYFSQR();
				jjs.setBZ1(ma.get("BZ1")==null?"":ma.get("BZ1").toString());
				jjs.setBZ2(ma.get("BZ2")==null?"":ma.get("BZ2").toString());
				jjs.setId(ma.get("ID")==null?"":ma.get("ID").toString());
				Date date;
				try {
					if(ma.get("CREATETIME")!=null){
						try {
							date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(ma.get("CREATETIME").toString());
							jjs.setCREATETIME(date);
						} catch (java.text.ParseException e) {
							e.printStackTrace();
						}
					}
					jjs.setSQRXM(ma.get("SQRXM")==null?"":ma.get("SQRXM").toString());
					jjs.setSQRZJLX(ma.get("SQRZJLX")==null?"":ma.get("SQRZJLX").toString());
					jjs.setSQRZJH(ma.get("SQRZJH")==null?"":ma.get("SQRZJH").toString());
					li.add(jjs);
				} catch (ParseException e) {
					e.printStackTrace();
				} 
				if(a>0){
					msg.setTotal(a);
					msg.setRows(li);
				}
			}	
		}else{
		
				Page p = dao.getPageDataByHql(BDCS_JJSYFSQR.class, _hql, page, rows);
				if (p != null) {
					msg.setTotal(p.getTotalCount());
					msg.setRows(p.getResult());
				}
		}
			
		return msg;
	}

/**
 * 添加经济适用房申请人信息
 * @作者 海豹
 * @创建时间 2015年9月24日下午3:08:40
 * @param request
 * @param response
 * @return
 * @throws UnsupportedEncodingException
 */
	@RequestMapping(value = "/sqrs/", method = RequestMethod.POST)
	public @ResponseBody Message AddDJYYS(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		Message msg = new Message();
		String sqrxm =request.getParameter("sqrxm");
		String sqrzjh=request.getParameter("zjh");
		String sqrzjlx=request.getParameter("zjlx");
		if (StringHelper.isEmpty(sqrxm)) {
			msg.setSuccess("false");
			msg.setMsg("申请人不能为空");
			return msg;
		}
		
		BDCS_JJSYFSQR bdcs_jjsyfsqr = new BDCS_JJSYFSQR();
		bdcs_jjsyfsqr.setSQRXM(sqrxm);
		bdcs_jjsyfsqr.setSQRZJH(sqrzjh);
		bdcs_jjsyfsqr.setSQRZJLX(sqrzjlx);
		bdcs_jjsyfsqr.setCREATETIME(new Date());
		bdcs_jjsyfsqr.setMODIFYTIME(new Date());
		dao.save(bdcs_jjsyfsqr);
		dao.flush();
		
		List<BDCS_JJSYFSQR> list = new ArrayList<BDCS_JJSYFSQR>();
		list.add(bdcs_jjsyfsqr);
		msg.setSuccess("true");
		msg.setMsg("保存成功");
		msg.setRows(list);
		msg.setTotal(1);
		return msg;
	}

/**
 *  更新经济适用房申请人信息
 * @作者 海豹
 * @创建时间 2015年9月24日下午3:15:42
 * @param mbid
 * @param request
 * @param response
 * @return
 * @throws UnsupportedEncodingException
 */
	@RequestMapping(value = "/sqrs/{sqrid}", method = RequestMethod.POST)
	public @ResponseBody Message ModifyDJYYS(@PathVariable("sqrid") String sqrid, HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		Message msg = new Message();	
		if (StringHelper.isEmpty(sqrid)) {
			msg.setSuccess("false");
			msg.setMsg("待更新记录ID不能为空");
			return msg;
		}
		
		String sqrxm =request.getParameter("sqrxm");
		String sqrzjh=request.getParameter("zjh");
		String sqrzjlx=request.getParameter("zjlx");
		String xzyy="";
		String czrxm="";
		String xzqdm = ConfigHelper.getNameByValue("XZQHDM").toString();
		if("420900".equals(xzqdm)||"420902".equals(xzqdm)){
			//添加限制原因和操作人姓名
			xzyy=request.getParameter("xzyy");//"czrxm" : obj.bz2
			czrxm=request.getParameter("czrxm");
		}
		
		if (StringHelper.isEmpty(sqrxm)) {
			msg.setSuccess("false");
			msg.setMsg("申请人姓名不能为空");
			return msg;
		}
		
		
		BDCS_JJSYFSQR bds_jjsyfsqr=dao.get(BDCS_JJSYFSQR.class, sqrid);
		if(sqrid==null)
		{
			msg.setSuccess("false");
			msg.setMsg("未找到ID为"+sqrid+"的经济适用房信息，无法更新！");
			return msg;
		}
		
		bds_jjsyfsqr.setSQRXM(sqrxm);
		bds_jjsyfsqr.setSQRZJH(sqrzjh);
		bds_jjsyfsqr.setSQRZJLX(sqrzjlx);
		if("420900".equals(xzqdm)||"420902".equals(xzqdm)){
			bds_jjsyfsqr.setBZ1(xzyy);
			bds_jjsyfsqr.setBZ2(czrxm);
		}
		
		
		dao.update(bds_jjsyfsqr);
		dao.flush();
		
		List<BDCS_JJSYFSQR> list = new ArrayList<BDCS_JJSYFSQR>();
		list.add(bds_jjsyfsqr);
		msg.setSuccess("true");
		msg.setMsg("更新成功");
		msg.setTotal(1);
		msg.setRows(list);
		
		return msg;
	}

	/**
	 * 删除经济适用房申请人信息
	 * @作者 海豹
	 * @创建时间 2015年9月24日下午3:19:43
	 * @param mbid
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "/sqrs/{sqrid}", method = RequestMethod.DELETE)
	public @ResponseBody Message DeleteDJYYS(@PathVariable("sqrid") String sqrid, HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		Message msg = new Message();		
		if (StringHelper.isEmpty(sqrid)) {
			msg.setSuccess("false");
			msg.setMsg("待删除记录ID不能为空");
			return msg;
		}
		
		BDCS_JJSYFSQR bdcs_jjsyfsqr=dao.get(BDCS_JJSYFSQR.class, sqrid);
		if(bdcs_jjsyfsqr==null)
		{
			msg.setSuccess("false");
			msg.setMsg("未找到ID为"+sqrid+"的登记原因模版，无法删除！");
			return msg;
		}
		dao.deleteEntity(bdcs_jjsyfsqr);
		dao.flush();
		
		List<BDCS_JJSYFSQR> list = new ArrayList<BDCS_JJSYFSQR>();
		list.add(bdcs_jjsyfsqr);
		msg.setSuccess("true");
		msg.setMsg("删除成功");
		msg.setTotal(1);
		msg.setRows(list);
		
		return msg;
	}
    /**
     * 获取经济适用房信息
     * @作者 海豹
     * @创建时间 2015年9月24日下午2:28:12
     * @param model
     * @return
     */
	@RequestMapping(value = "/jjsyfInfo", method = RequestMethod.GET)
	public String jjsyfinfo(Model model) {		
		return "/realestate/registration/modules/common/housingApplication";
	}
}
