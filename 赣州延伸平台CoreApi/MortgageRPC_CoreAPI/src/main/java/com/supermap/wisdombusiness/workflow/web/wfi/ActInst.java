package com.supermap.wisdombusiness.workflow.web.wfi;

import org.springframework.stereotype.Controller;

import com.supermap.wisdombusiness.workflow.service.common.SmActInfo;


@Controller
public class ActInst {

	
	public ActInst() {
		
	}
	
	//通过actinst 获取SmActInfo
	public SmActInfo GetActInfo(String actinstid) {
		SmActInfo info=new SmActInfo();
		
		
		return info;
	}
}
