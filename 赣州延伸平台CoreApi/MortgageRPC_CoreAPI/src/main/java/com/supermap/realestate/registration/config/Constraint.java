/**   
 * 约束接口
 * @Title: Constraint.java 
 * @Package com.supermap.realestate.registration.config 
 * @author liushufeng 
 * @date 2015年7月25日 下午3:01:36 
 * @version V1.0   
 */

package com.supermap.realestate.registration.config;

import com.supermap.realestate.registration.util.ConstValue.BDCDYLX;
import com.supermap.realestate.registration.util.ConstValue.DJDYLY;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;

/**
 * 约束接口
 * @ClassName: Constraint
 * @author liushufeng
 * @date 2015年7月25日 下午3:01:36
 */
public interface Constraint {

	public boolean check(CommonDao dao,String xmbh, String bdcdyid, String qlid,DJDYLY ly,BDCDYLX lx);

}
