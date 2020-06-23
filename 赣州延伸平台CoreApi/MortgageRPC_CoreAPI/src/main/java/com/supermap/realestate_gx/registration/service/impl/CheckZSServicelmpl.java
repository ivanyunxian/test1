package com.supermap.realestate_gx.registration.service.impl;


import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.supermap.realestate.registration.model.BDCS_ZS_XZ;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.realestate_gx.registration.service.CheckZSService;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import com.supermap.wisdombusiness.web.Message;

@Service("CheckZSService")
public class CheckZSServicelmpl implements CheckZSService {
	@Autowired
	private CommonDao baseCommonDao;
	
	
	/**
	 * liangc
	 */
	@Override
	public Message checkzs(String para) {
		Message msg = new Message();
		StringBuilder  msgs = new StringBuilder();
		if (StringHelper.isEmpty(para)) {
			msg.setMsg("此证书不存在！");
			return msg;
		}else{
			List<BDCS_ZS_XZ> lszs = baseCommonDao.getDataList(BDCS_ZS_XZ.class, "ZSBH='"+para+"'");
			if(lszs != null && lszs.size()>0){
				msg.setRows(lszs);
				msg.setMsg("证书存在！");
				msg.setSuccess("true");
			}
		}
		return msg;
	}
}
