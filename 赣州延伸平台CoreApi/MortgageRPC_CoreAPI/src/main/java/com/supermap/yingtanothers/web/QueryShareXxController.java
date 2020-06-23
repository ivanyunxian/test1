package com.supermap.yingtanothers.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.supermap.yingtanothers.service.QueryShareXxService;

/**
 * 
 * 作者： 苗利涛
 * 时间： 2016年6月20日 上午11:05:16
 * 功能：鹰潭市一键式提取房管局共享信息控制入口
 */
@Controller
@RequestMapping("/queryshareXx")
public class QueryShareXxController {
	
	@Autowired
	private QueryShareXxService q_QueryShareXxService;
	
}
