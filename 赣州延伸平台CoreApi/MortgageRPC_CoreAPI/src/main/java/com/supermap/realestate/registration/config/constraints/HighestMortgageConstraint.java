/**   
 * 约束：单元处于最高额抵押状态
 * @Title: SingleMortgageConstraint.java 
 * @Package com.supermap.realestate.registration.config.constraints 
 * @author liushufeng 
 * @date 2015年7月25日 下午3:27:53 
 * @version V1.0   
 */

package com.supermap.realestate.registration.config.constraints;

import java.text.MessageFormat;
import java.util.List;

import com.supermap.realestate.registration.config.Constraint;
import com.supermap.realestate.registration.model.BDCS_DJDY_XZ;
import com.supermap.realestate.registration.model.interfaces.Rights;
import com.supermap.realestate.registration.tools.RightsTools;
import com.supermap.realestate.registration.util.ConstValue.BDCDYLX;
import com.supermap.realestate.registration.util.ConstValue.DJDYLY;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;

/**
 * 约束：单元处于最高额抵押状态
 * @ClassName: SingleMortgageConstraint
 * @author liushufeng
 * @date 2015年7月25日 下午3:27:53
 */
public class HighestMortgageConstraint implements Constraint {

	/**
	 * 约束：单元处于最高额抵押状态
	 * @Title: Check
	 * @author:liushufeng
	 * @date：2015年7月25日 下午3:27:53
	 * @param dao
	 * @param xmbh
	 * @param bdcdyid
	 * @param qlid
	 * @return 如果处于抵押状态，返回true，否则返回false
	 */
	@Override
	public boolean check(CommonDao dao, String xmbh, String bdcdyid, String qlid,DJDYLY ly,BDCDYLX lx) {
		boolean b = false;
		String sql = "BDCDYID='" + bdcdyid + "'";
		List<BDCS_DJDY_XZ> lists = dao.getDataList(BDCS_DJDY_XZ.class, sql);
		if (lists != null && lists.size() > 0) {
			BDCS_DJDY_XZ djdy = lists.get(0);
			String djdyid = djdy.getDJDYID();
			String condition = MessageFormat.format("DJDYID=''{0}'' AND QLLX=''23'' AND DYFS=''2''", djdyid);
			List<Rights> _rightss = RightsTools.loadRightsByCondition(DJDYLY.XZ, condition);
			if (_rightss != null && _rightss.size() > 0)
				b = true;
		}
		return b;
	}

}
