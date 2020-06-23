package com.supermap.realestate.registration.web;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.runners.Parameterized.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.supermap.realestate.registration.model.BDCS_QLR_D;
import com.supermap.realestate.registration.model.BDCS_QLR_GZ;
import com.supermap.realestate.registration.model.BDCS_SQR;
import com.supermap.realestate.registration.model.DCS_DCXM;
import com.supermap.realestate.registration.service.DYService;
import com.supermap.realestate.registration.service.QLRService;
import com.supermap.realestate.registration.util.ConstValue;
import com.supermap.realestate.registration.util.ConstValue.BDCDYLX;
import com.supermap.realestate.registration.util.ConstValue.SQRLB;
import com.supermap.realestate.registration.util.HttpRequest;
import com.supermap.realestate.registration.util.RequestHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.realestate.registration.util.YwLogUtil;
import com.supermap.wisdombusiness.core.SuperHelper;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import com.supermap.wisdombusiness.web.ResultMessage;
import com.supermap.wisdombusiness.web.ui.Page;
import com.supermap.wisdombusiness.workflow.util.Message;

/**
 * 选择申请人操作类
 *
 */
@Controller
@RequestMapping("/qlrxx")
public class QLRController {
	@Autowired
	private CommonDao baseCommonDao;

	@Autowired
	private QLRService qlrService;
	/**
	 * 分页获取选择权利人信息列表
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "/getqlrlist/", method = RequestMethod.GET)
	public @ResponseBody Message GetQlrInfo(HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException {
		// 分页查询
		Integer page = 1;
		if (request.getParameter("page") != null) {
			page = Integer.parseInt(request.getParameter("page"));
		}
		Integer rows = 10;
		if (request.getParameter("rows") != null) {
			rows = Integer.parseInt(request.getParameter("rows"));
		}
		String sqrxm = RequestHelper.getParam(request, "sqrxm");
		String zjlx = RequestHelper.getParam(request, "zjlx");
		StringBuilder conditionBuilder = new StringBuilder("1 = 1");
		if (!StringHelper.isEmpty(sqrxm)) {
			conditionBuilder.append(" AND QLRMC LIKE '%").append(sqrxm)
					.append("%'");
		}
		if(!StringHelper.isEmpty(zjlx)){
			conditionBuilder.append(" AND ZJZL LIKE '%").append(zjlx).append("%'");
		}
		String strQuery = conditionBuilder.toString();
		Page p = baseCommonDao.getPageDataByHql(BDCS_QLR_D.class, strQuery, page, rows);
		Message m = new Message();
		m.setTotal(p.getTotalCount());
		m.setRows(p.getResult());
		return m;
	}
	
       /**
        * 
        * @作者 海豹
        * @创建时间 2015年12月29日下午11:02:14
        * @param request
        * @param response
        * @return
        * @throws UnsupportedEncodingException
        */
		@RequestMapping(value = "/sqrs/", method = RequestMethod.POST)
		public @ResponseBody ResultMessage AddDJYYS(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
			ResultMessage msg = new ResultMessage();
			String qlrmc =request.getParameter("qlrmc");
			String qlrlx=request.getParameter("qlrlx");
			String zjzl=request.getParameter("zjzl");
			String zjh=request.getParameter("zjh");
			String dz=request.getParameter("dz");
			String fddbr=request.getParameter("fddbr");
			String fddbrzjhm=request.getParameter("fddbrzjhm");
			String fddbrdh=request.getParameter("fddbrdh");
			String dlrxm=request.getParameter("dlrxm");
			String dlrzjhm=request.getParameter("dlrzjhm");
			String dlrlxdh=request.getParameter("dlrlxdh");
			if (StringHelper.isEmpty(qlrmc)) {
				msg.setSuccess("false");
				msg.setMsg("申请人不能为空");
				return msg;
			}
			
			BDCS_QLR_D bdcs_qlr_d = new BDCS_QLR_D();
			bdcs_qlr_d.setId((String)SuperHelper.GeneratePrimaryKey());
			bdcs_qlr_d.setQLRMC(qlrmc);
			bdcs_qlr_d.setQLRLX(qlrlx);
			bdcs_qlr_d.setZJH(zjh);
			bdcs_qlr_d.setZJZL(zjzl);
			bdcs_qlr_d.setDZ(dz);
			bdcs_qlr_d.setFDDBR(fddbr);
			bdcs_qlr_d.setFDDBRDH(fddbrdh);
			bdcs_qlr_d.setFDDBRZJHM(fddbrzjhm);
			bdcs_qlr_d.setCREATETIME(new Date());
			bdcs_qlr_d.setDLRXM(dlrxm);
			bdcs_qlr_d.setDLRZJHM(dlrzjhm);
			bdcs_qlr_d.setDLRLXDH(dlrlxdh);
			baseCommonDao.save(bdcs_qlr_d);
			baseCommonDao.flush();
			msg.setSuccess("true");
			msg.setMsg("保存成功");
			return msg;
		}
		/**
		 * 
		 * @作者 海豹
		 * @创建时间 2015年12月29日下午11:02:18
		 * @param sqrid
		 * @param request
		 * @param response
		 * @return
		 * @throws UnsupportedEncodingException
		 */
		@RequestMapping(value = "/sqrs/{sqrid}", method = RequestMethod.POST)
		public @ResponseBody ResultMessage ModifyDJYYS(@PathVariable("sqrid") String sqrid, HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
			ResultMessage msg = new ResultMessage();	
			if (StringHelper.isEmpty(sqrid)) {
				msg.setSuccess("false");
				msg.setMsg("待更新记录ID不能为空");
				return msg;
			}			
			String qlrmc =request.getParameter("qlrmc");
			String qlrlx=request.getParameter("qlrlx");
			String zjzl=request.getParameter("zjzl");
			String zjh=request.getParameter("zjh");
			String dz=request.getParameter("dz");
			String fddbr=request.getParameter("fddbr");
			String fddbrzjhm=request.getParameter("fddbrzjhm");
			String fddbrdh=request.getParameter("fddbrdh");
			String dlrxm=request.getParameter("dlrxm");
			String dlrzjhm=request.getParameter("dlrzjhm");
			String dlrlxdh=request.getParameter("dlrlxdh");
			if (StringHelper.isEmpty(qlrmc)) {
				msg.setSuccess("false");
				msg.setMsg("申请人姓名不能为空");
				return msg;
			}			
			BDCS_QLR_D bdcs_qlr_d=baseCommonDao.get(BDCS_QLR_D.class, sqrid);
			if(sqrid==null)
			{
				msg.setSuccess("false");
				msg.setMsg("未找到ID为"+sqrid+"的经济适用房信息，无法更新！");
				return msg;
			}
			bdcs_qlr_d.setQLRMC(qlrmc);
			bdcs_qlr_d.setQLRLX(qlrlx);
			bdcs_qlr_d.setZJH(zjh);
			bdcs_qlr_d.setZJZL(zjzl);
			bdcs_qlr_d.setDZ(dz);
			bdcs_qlr_d.setFDDBR(fddbr);
			bdcs_qlr_d.setFDDBRZJHM(fddbrzjhm);
			bdcs_qlr_d.setFDDBRDH(fddbrdh);
			bdcs_qlr_d.setMODIFYTIME(new Date());
			bdcs_qlr_d.setDLRXM(dlrxm);
			bdcs_qlr_d.setDLRZJHM(dlrzjhm);
			bdcs_qlr_d.setDLRLXDH(dlrlxdh);
			baseCommonDao.update(bdcs_qlr_d);
			baseCommonDao.flush();
			return msg;
		}
		/**
		 * 
		 * @作者 海豹
		 * @创建时间 2015年12月29日下午11:02:35
		 * @param sqrid
		 * @param request
		 * @param response
		 * @return
		 * @throws UnsupportedEncodingException
		 */
		@RequestMapping(value = "/sqrs/{sqrid}", method = RequestMethod.DELETE)
		public @ResponseBody ResultMessage DeleteDJYYS(@PathVariable("sqrid") String sqrid, HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
			ResultMessage msg = new ResultMessage();		
			if (StringHelper.isEmpty(sqrid)) {
				msg.setSuccess("false");
				msg.setMsg("待删除记录ID不能为空");
				return msg;
			}			
			baseCommonDao.delete(BDCS_QLR_D.class, sqrid);
			baseCommonDao.flush();			
			return msg;
		}
	@RequestMapping(value = "/selectsqr/{qlrid}/{xmbh}/", method = RequestMethod.POST)
	@ResponseBody
	public ResultMessage SelectSqr(@PathVariable("qlrid") String qlrid,@PathVariable("xmbh") String xmbh,HttpServletRequest request,
			HttpServletResponse response) {
		ResultMessage msg = new ResultMessage();
		msg.setSuccess("false");
		msg.setMsg("添加失败！");
		BDCS_QLR_D qlr = baseCommonDao.get(BDCS_QLR_D.class, qlrid);
		if(qlr!=null){
			String qlrmc=qlr.getQLRMC();
			List<BDCS_SQR> list=baseCommonDao.getDataList(BDCS_SQR.class, "XMBH='"+xmbh+"' AND SQRXM='"+qlrmc+"' AND SQRLB='"+SQRLB.JF.Value+"'");
			if(list!=null&&list.size()>0){
				msg.setMsg("该申请人已经存在！");
				YwLogUtil.addYwLog("权利人添加-失败.该申请人已经存在！", ConstValue.SF.NO.Value,ConstValue.LOG.ADD);
			}else{
				String SQRID = SuperHelper.GeneratePrimaryKey();
				BDCS_SQR sqr = new BDCS_SQR();
				sqr.setGYFS(qlr.getGYFS());
				sqr.setFZJG(qlr.getFZJG());
				sqr.setGJDQ(qlr.getGJ());
				sqr.setGZDW(qlr.getGZDW());
				sqr.setXB(qlr.getXB());
				sqr.setHJSZSS(qlr.getHJSZSS());
				sqr.setSSHY(qlr.getSSHY());
				sqr.setYXBZ(qlr.getYXBZ());
				sqr.setQLBL(qlr.getQLBL());
				sqr.setQLMJ(StringHelper.formatObject(qlr.getQLMJ()));
				sqr.setSQRXM(qlr.getQLRMC().replace(" ", ""));
				sqr.setSQRLB(SQRLB.JF.Value);
				sqr.setSQRLX(qlr.getQLRLX());
				sqr.setDZYJ(qlr.getDZYJ());
				sqr.setLXDH(qlr.getDH());
				sqr.setZJH(qlr.getZJH().replace(" ", ""));
				sqr.setZJLX(qlr.getZJZL());
				sqr.setTXDZ(qlr.getDZ());
				sqr.setYZBM(qlr.getYB());
				sqr.setXMBH(xmbh);
				sqr.setFDDBR(qlr.getFDDBR());
				sqr.setFDDBRZJHM(qlr.getFDDBRZJHM());
				sqr.setFDDBRDH(qlr.getFDDBRDH());
				sqr.setDLRXM(qlr.getDLRXM() !=null ? qlr.getDLRXM().replace(" ", "") : StringHelper.formatObject(qlr.getDLRXM()));
				sqr.setDLRZJHM(qlr.getDLRZJHM() !=null ? qlr.getDLRZJHM().replace(" ", "") : StringHelper.formatObject(qlr.getDLRZJHM()));
				sqr.setDLRLXDH(qlr.getDLRLXDH());
				sqr.setId(SQRID);
				baseCommonDao.save(sqr);
				baseCommonDao.flush();
				msg.setSuccess("true");
				msg.setMsg("添加成功！");
				YwLogUtil.addYwLog("权利人添加-保存成功", ConstValue.SF.YES.Value,ConstValue.LOG.ADD);
			}
		}else{
			msg.setMsg("未找到权利人！");
			YwLogUtil.addYwLog("权利人添加-失败", ConstValue.SF.NO.Value,ConstValue.LOG.ADD);
		}
		return msg;
	}
	
	@RequestMapping(value = "/chargeqlrlist/{xmbh}/", method = RequestMethod.GET)
	@ResponseBody
	public List<HashMap<String,String>> GetQLRList(@PathVariable("xmbh") String xmbh,HttpServletRequest request,
			HttpServletResponse response) {
		List<HashMap<String,String>> list=new ArrayList<HashMap<String, String>>();
		List<String> list_qlrmckey=new ArrayList<String>();
		List<BDCS_QLR_GZ> list_qlr=baseCommonDao.getDataList(BDCS_QLR_GZ.class, "XMBH='"+xmbh+"'");
		if(list_qlr!=null){
			for(BDCS_QLR_GZ qlr:list_qlr){
				String qlrmc=qlr.getQLRMC();
				String qlrzjh=qlr.getZJH();
				String qlrmckey=qlrmc;
				if(!StringHelper.isEmpty(qlrzjh)){
					qlrmckey=qlrmc+"-"+qlrzjh;
				}
				if(!list_qlrmckey.contains(qlrmckey)){
					HashMap<String,String> qlrinfo=new HashMap<String, String>();
					qlrinfo.put("text", qlrmckey);
					qlrinfo.put("value", qlrmc);
					list.add(qlrinfo);
					list_qlrmckey.add(qlrmckey);
				}
			}
		}
		return list;
	}
	
	/**
	 * 通过大宗地上的不动产单元号获取权利人信息
	 * @param zdbdcdyid
	 * @param request
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value="/getzdqlrinfo/{zdbdcdyid}",method=RequestMethod.GET)
	public @ResponseBody Message getZDQLRInfo(@PathVariable String zdbdcdyid,HttpServletRequest request) throws UnsupportedEncodingException{
		Integer page = 1;
		if (request.getParameter("page") != null) {
			page = Integer.parseInt(request.getParameter("page"));
		}
		Integer rows = 10;
		if (request.getParameter("rows") != null) {
			rows = Integer.parseInt(request.getParameter("rows"));
		}
		Map<String, Object> mapCondition = new HashMap<String, Object>();
		if (request.getParameter("zjh") != null) {
			mapCondition.put("QLR.ZJH", RequestHelper.getParam(request, "zjh"));
		}
		if (request.getParameter("bdcqzh") != null) {
			mapCondition.put("QLR.BDCQZH", RequestHelper.getParam(request, "bdcqzh"));
		}
		if (request.getParameter("qlrmc") != null) {
			mapCondition.put("QLR.QLRMC", RequestHelper.getParam(request, "qlrmc"));
		}
		Message m = new Message();
        m=qlrService.getZDQLRInfo(zdbdcdyid, page, rows, mapCondition);
		return m;
	}
	
	/**
	 * 注销权利人信息
	 * @param zdbdcdyid
	 * @param request
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value="/writeholderbook/{zdbdcdyid}",method=RequestMethod.GET)
	public @ResponseBody ResultMessage writeHolderBook(@PathVariable String zdbdcdyid,HttpServletRequest request) throws UnsupportedEncodingException{
		 String qlrids=RequestHelper.getParam(request, "qlrids");
		 String ywhs=RequestHelper.getParam(request, "ywhs");
		 return qlrService.writeHolderBook(zdbdcdyid, qlrids,ywhs);
	}
	
	/**
	 * 单元灭失
	 * @param zdbdcdyid
	 * @return
	 */
	@RequestMapping(value="/writeunitbook/{zdbdcdyid}",method=RequestMethod.GET)
	public @ResponseBody  ResultMessage writeUnitBook(@PathVariable String zdbdcdyid){
		return qlrService.writeUnitBook(zdbdcdyid);
	}
}
