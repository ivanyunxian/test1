package com.supermap.realestate.registration.service;

import com.supermap.wisdombusiness.web.ResultMessage;

public interface XIANExtractDataService {
	/**
	 * 西安房产数据抽取
	 * @作者 胡加红
	 * @创建时间 2016年6月18日下午3:29:51
	 * @param _xmbh
	 * @param _compactNo
	 * @return
	 */
	public ResultMessage GetDatabyCompactNo(String _xmbh,String _compactNo);
}
