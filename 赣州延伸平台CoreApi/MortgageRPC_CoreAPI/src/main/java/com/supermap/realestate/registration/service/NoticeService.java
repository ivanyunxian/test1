package com.supermap.realestate.registration.service;

import java.util.HashMap;



/**
 * 
 * @Description:公告服务
 * @author yuxuebin
 * @date 2017年03月16日 10:37:22
 * @Copyright SuperMap
 */
public interface NoticeService {

	/**
	 * 服务接口：获取公告信息
	 * @Title: GetNoticeInfo
	 * @author:yuxuebin
	 * @date：2017年03月16日 10:43:20
	 * @return
	 */
	HashMap<String, Object> GetNoticeInfo(String project_id, String noticetype);

}
