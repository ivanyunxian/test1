/**   
 * 计费服务实现类
 * @Title: ChargeServiceImpl.java 
 * @Package com.supermap.realestate.registration.service.impl 
 * @author liushufeng 
 * @date 2015年7月26日 上午3:54:14 
 * @version V1.0   
 */

package com.supermap.realestate.registration.service.impl;

import java.util.HashMap;

import org.springframework.stereotype.Service;

import com.supermap.realestate.registration.service.NoticeService;
import com.supermap.realestate.registration.tools.NoticeTools;

/**
 * 
 * @Description:公告服务
 * @author yuxuebin
 * @date 2017年03月16日 10:37:22
 * @Copyright SuperMap
 */
@Service
public class NoticeServiceImpl implements NoticeService {

	@Override
	public HashMap<String, Object> GetNoticeInfo(String project_id,
			String noticetype) {
		return NoticeTools.GetNoticeInfo(project_id, noticetype);
	}

}
