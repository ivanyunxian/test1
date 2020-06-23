package com.supermap.yingtanothers.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.supermap.realestate.registration.util.ConstValue;
import com.supermap.realestate.registration.util.YwLogUtil;
import com.supermap.wisdombusiness.web.ResultMessage;
import com.supermap.yingtanothers.service.InsertGxxmbhToCaseNumService;

/**
 * 
 * 作者： 苗利涛
 * 时间： 2016年4月15日 上午11:03:53
 * 功能：和房管局对接，把共享项目编号插入到casenum字段
 */
@Controller
@RequestMapping("/InsertGxxmbh")
public class InsertGxxmbhToCaseNumController {
	
	@Autowired
	private InsertGxxmbhToCaseNumService i_InsertGxxmbhToCaseNumService;
	
	
	@RequestMapping(value = "/ToCaseNum/", method = RequestMethod.POST)
	@ResponseBody
	public ResultMessage ToCaseNum(HttpServletRequest request,HttpServletResponse response) {
		ResultMessage msg = null;
		try {
			msg = new ResultMessage();			

			String gxxmbh = request.getParameter("gxxmbh");
			String xmbh = request.getParameter("xmbh");
			if (gxxmbh != null && gxxmbh != "" && xmbh != null && xmbh != "") {
				
				i_InsertGxxmbhToCaseNumService.InsertGxxmbhToCaseNum(gxxmbh, xmbh);
				
				YwLogUtil.addYwLog("共享登记库插入casenum-成功", ConstValue.SF.NO.Value,ConstValue.LOG.ADD);
			}
			
		} catch (Exception e) {
						
			YwLogUtil.addYwLog("共享登记库插入casenum-失败", ConstValue.SF.NO.Value,ConstValue.LOG.ADD);
		}								
	
		return msg;
	}
	
}
