/**   
 * "类"类型的受理约束的接口文件
 * @Title: ConstraintInterface.java 
 * @Package com.supermap.realestate.registration.constraint 
 * @author liushufeng 
 * @date 2016年3月28日 下午4:25:13 
 * @version V1.0   
 */

package com.supermap.realestate.registration.constraint;

import java.util.HashMap;

import com.supermap.wisdombusiness.web.ResultMessage;

/**
 * "类"类型的受理约束的接口
 * @ClassName: ConstraintInterface
 * @author liushufeng
 * @date 2016年3月28日 下午4:25:13
 */
public interface ConstraintInterface {
	public ResultMessage check(HashMap<String,String> params);
}
