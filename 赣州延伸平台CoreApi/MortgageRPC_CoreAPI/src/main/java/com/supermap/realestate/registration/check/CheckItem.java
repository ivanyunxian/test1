package com.supermap.realestate.registration.check;

import java.util.HashMap;

import com.supermap.wisdombusiness.web.ResultMessage;

/**
 * 检查项接口
 * @ClassName: CheckItem
 * @author liushufeng
 * @date 2015年11月7日 下午7:17:15
 */
public interface CheckItem {
	public ResultMessage check(HashMap<String,String> params);
}
