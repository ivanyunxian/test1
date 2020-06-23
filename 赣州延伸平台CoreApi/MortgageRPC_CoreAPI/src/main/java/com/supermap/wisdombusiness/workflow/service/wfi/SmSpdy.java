package com.supermap.wisdombusiness.workflow.service.wfi;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.supermap.wisdombusiness.workflow.dao.CommonDao;
import com.supermap.wisdombusiness.workflow.model.Wfi_Spdy;
import com.supermap.wisdombusiness.workflow.model.Wfi_Spyj;
import com.supermap.wisdombusiness.workflow.service.common.Common;

@Component("smSpdy")
public class SmSpdy {

	@Autowired
	private CommonDao _CommonDao;
	
	
	/**
	 * 根据审批类型获取审批定义
	 * @param splx
	 * @return
	 */

	public Wfi_Spdy getspdyBySPLX(String splx){
		Wfi_Spdy spdy=null;
		if(splx!=null&&!splx.equals("")){
			List<Wfi_Spdy> spdys=	_CommonDao.getDataList(Wfi_Spdy.class, Common.WORKFLOWDB+"Wfi_Spdy", "splx='"+splx+"'");
			if(spdys!=null&&spdys.size()>0){
				 spdy=spdys.get(0);
			}
		}
		return spdy;
	}
	
	//通过活动id 和审批类型获取  获取当前活动的意见
	public Wfi_Spyj getspyj(String actinstid,String splx ){
		if(actinstid!=null&&!actinstid.equals("")&&splx!=null&&!splx.equals("")){
			List<Wfi_Spyj> list=_CommonDao.getDataList(Wfi_Spyj.class, Common.WORKFLOWDB+"Wfi_Spyj", "actinst_id='"+actinstid+"' and splx='"+splx+"'");
			if(list!=null&&list.size()>0){
				return list.get(0);
			}
		}
		return null;
	}
	
	//
}
