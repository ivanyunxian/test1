/**   
 * 约束：单元处于被查封状态
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
import com.supermap.realestate.registration.util.ConstValue.DJLX;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;

/**
 * 约束：单元处于被查封状态
 * @ClassName: SingleMortgageConstraint
 * @author liushufeng
 * @date 2015年7月25日 下午3:27:53
 */
public class SealConstraint implements Constraint {

	/**
	 * 约束：单元处于被查封状态
	 * @Title: Check
	 * @author:liushufeng
	 * @date：2015年7月25日 下午3:27:53
	 * @param dao
	 * @param xmbh
	 * @param bdcdyid
	 * @param qlid
	 * @return 如果
	 */
	@Override
	public boolean check(CommonDao dao, String xmbh, String bdcdyid, String qlid, DJDYLY ly, BDCDYLX lx) {
		boolean b = false;
		String sql = "BDCDYID='" + bdcdyid + "'";
		List<BDCS_DJDY_XZ> lists = dao.getDataList(BDCS_DJDY_XZ.class, sql);
		if (lists != null && lists.size() > 0) {
			BDCS_DJDY_XZ djdy = lists.get(0);
			String djdyid = djdy.getDJDYID();
			String condition = MessageFormat.format("DJDYID=''{0}'' AND DJLX=''{1}'' and QLLX=''{2}''", djdyid, DJLX.CFDJ.Value,"99");
			List<Rights> _rightss = RightsTools.loadRightsByCondition(DJDYLY.XZ, condition);
			if (_rightss != null && _rightss.size() > 0)
				b = true;
			if (!b) {
				// 判断完现状层中的查封信息，接着判断办理中的查封信息
				String sqlSealing = " FROM bdck.bdcs_xmxx a LEFT JOIN bdck.bdcs_djdy_gz b ON a.xmbh=b.xmbh AND a.sfdb='0' LEFT JOIN bdck.bdcs_ql_gz c ON b.djdyid=c.djdyid WHERE c.djlx='800' AND c.qllx='99' and c.djdyid='"
						+ djdyid + "'";
				long count = dao.getCountByFullSql(sqlSealing);
				b = count > 0 ? true : false;
			}
		}
		return b;
	}

}
