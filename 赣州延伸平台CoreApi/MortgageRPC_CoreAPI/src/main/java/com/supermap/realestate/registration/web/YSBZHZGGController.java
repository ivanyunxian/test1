package com.supermap.realestate.registration.web;
import java.io.UnsupportedEncodingException;
import java.text.MessageFormat;
import java.util.Date;
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

import com.supermap.realestate.registration.model.BDCS_QL_XZ;
import com.supermap.realestate.registration.model.BDCS_YSBZGG;
import com.supermap.realestate.registration.util.ConstValue;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.realestate.registration.util.YwLogUtil;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import com.supermap.wisdombusiness.web.Message;

/**
 * 遗失补证换证公告信息
 * @author 海豹
 *
 */
@Controller
@RequestMapping("/ysbzggxx")
public class YSBZHZGGController {

	@Autowired
	private CommonDao dao;
/**
 * 保存遗失补证换证公告信息
 * @作者 海豹
 * @创建时间 2015年10月29日下午6:21:04
 * @param xmbh
 * @param qlid
 * @param bdcdyid
 * @param request
 * @param response
 * @return
 */
	@RequestMapping(value = "/saveYsbzgg/{xmbh}/{qlid}/{bdcdyid}", method = RequestMethod.POST)
	public @ResponseBody Message AddDJYYS(@PathVariable("xmbh") String xmbh,@PathVariable("qlid") String qlid,@PathVariable("bdcdyid") String bdcdyid,HttpServletRequest request, HttpServletResponse response)  {
		Message msg = new Message();
		Date qssj=	null;
		 Date jssj=null;
		try {
			String ggqssj =request.getParameter("ggqssj");
			String ggjssj=request.getParameter("ggjssj");
			String ggsm=request.getParameter("ggsm");
			 qssj=	StringHelper.FormatByDate(ggqssj);
		     jssj=StringHelper.FormatByDate(ggjssj);
			   String condition= MessageFormat.format("xmbh =''{0}'' and qlid =''{1}'' and bdcdyid=''{2}''", xmbh,qlid,bdcdyid);
			   int flag=dao.deleteEntitysByHql(BDCS_YSBZGG.class, condition);
				if(flag !=0)
				{
					dao.flush();
				}
				BDCS_QL_XZ bdcs_ql_xz=dao.get(BDCS_QL_XZ.class, qlid);
				String bdcqzh="";
				if(bdcs_ql_xz !=null)
				{
					bdcqzh=bdcs_ql_xz.getBDCQZH();
				}
				BDCS_YSBZGG bdcs_ysbzgg=new BDCS_YSBZGG();
				bdcs_ysbzgg.setBDCDYID(bdcdyid);//不动产单元ID
				bdcs_ysbzgg.setQLID(qlid);//权利ID
				bdcs_ysbzgg.setXMBH(xmbh);//项目编号
				bdcs_ysbzgg.setBZZT("0");//补正状态，0表示未补正，1表示已补正
				bdcs_ysbzgg.setGGQSSJ(qssj);//公告起始时间
				bdcs_ysbzgg.setGGJSSJ(jssj);//公告结束时间
				bdcs_ysbzgg.setGGSM(ggsm);//公告说明
				bdcs_ysbzgg.setBDCQZH(bdcqzh);//不动产权证号
				dao.save(bdcs_ysbzgg);
				dao.flush();
				msg.setSuccess("true");
				msg.setMsg("保存成功");
				YwLogUtil.addYwLog("保存遗失补证换证公告信息-保存成功", ConstValue.SF.YES.Value,ConstValue.LOG.ADD);
		} catch (Exception e) {
			msg.setSuccess("false");
			msg.setMsg("保存失败");
			YwLogUtil.addYwLog("保存遗失补证换证公告信息-保存失败", ConstValue.SF.NO.Value,ConstValue.LOG.ADD);
		}
		return msg;
	}

/**
 * 删除遗失补证换证信息
 * @作者 海豹
 * @创建时间 2015年10月29日下午6:21:45
 * @param sqrid
 * @param request
 * @param response
 * @return
 * @throws UnsupportedEncodingException
 */
	@RequestMapping(value = "/deleteYsbzgg/{xmbh}/{qlid}/{bdcdyid}", method = RequestMethod.GET)
	public @ResponseBody Message ModifyDJYYS(@PathVariable("xmbh") String xmbh,@PathVariable("qlid") String qlid,@PathVariable("bdcdyid") String bdcdyid, HttpServletRequest request, HttpServletResponse response) {
		Message msg = new Message();	
		 String condition= MessageFormat.format("xmbh =''{0}'' and qlid =''{1}'' and bdcdyid=''{2}''", xmbh,qlid,bdcdyid);
		   int flag=dao.deleteEntitysByHql(BDCS_YSBZGG.class, condition);
			if(flag !=0)
			{
				dao.flush();
			}
		msg.setSuccess("true");
		msg.setMsg("删除成功");
		YwLogUtil.addYwLog("删除遗失补证换证信息-成功", ConstValue.SF.YES.Value,ConstValue.LOG.DELETE);
		return msg;
	}

	/**
	 * 获取遗失补证换证公告信息
	 * @作者 海豹
	 * @创建时间 2015年10月29日下午8:21:20
	 * @param xmbh
	 * @param qlid
	 * @param bdcdyid
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/getYsbzgg/{xmbh}/{qlid}/{bdcdyid}", method = RequestMethod.GET)
	public @ResponseBody BDCS_YSBZGG GetSQRInfo(@PathVariable("xmbh") String xmbh,@PathVariable("qlid") String qlid,@PathVariable("bdcdyid") String bdcdyid, Model model) {
		 String condition= MessageFormat.format("xmbh =''{0}'' and qlid =''{1}'' and bdcdyid=''{2}''", xmbh,qlid,bdcdyid);
		 BDCS_YSBZGG bdcs_ysbzgg=null;
		List<BDCS_YSBZGG> bdcs_ysbzggs = dao.getDataList(BDCS_YSBZGG.class, condition);
		if(bdcs_ysbzggs !=null && bdcs_ysbzggs.size()>0)
		{
			bdcs_ysbzgg=bdcs_ysbzggs.get(0);
		}
		else
		{
			bdcs_ysbzgg=new BDCS_YSBZGG();
		}
		model.addAttribute("ggxxAttribute", bdcs_ysbzgg);
		return bdcs_ysbzgg;
	}
}
