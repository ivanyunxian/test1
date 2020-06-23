package com.supermap.realestate.registration.service;

import java.util.HashMap;

import com.supermap.wisdombusiness.web.ResultMessage;


/**
 * 
 * @Description:在线服务service 在线服务都放到里边
 * @author 俞学斌
 * @date 2016年10月13日 20:07:22
 */
public interface OnlineService {

	/**
	 * 服务接口：证书（证明校验服务）
	 * @Title: GetZSInfo
	 * @author:yuxuebin
	 * @date：2016年10月13日 20:05:20
	 * @return
	 */
	ResultMessage GetZSInfo(String certtype, String bdcqzh,
			HashMap<String, String> param);


}
