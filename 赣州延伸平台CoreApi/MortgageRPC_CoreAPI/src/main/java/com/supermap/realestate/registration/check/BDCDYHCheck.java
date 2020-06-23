/**   
 * TODO:@liushufeng:请描述这个文件
 * @Title: BDCDYHCheck.java 
 * @Package com.supermap.realestate.registration.check 
 * @author liushufeng 
 * @date 2015年11月29日 上午4:46:21 
 * @version V1.0   
 */

package com.supermap.realestate.registration.check;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.supermap.realestate.registration.model.BDCS_DJDY_GZ;
import com.supermap.realestate.registration.model.BDCS_XMXX;
import com.supermap.realestate.registration.model.interfaces.RealUnit;
import com.supermap.realestate.registration.tools.UnitTools;
import com.supermap.realestate.registration.util.ConstValue.BDCDYLX;
import com.supermap.realestate.registration.util.ConstValue.DJDYLY;
import com.supermap.realestate.registration.util.ConstValue.DJLX;
import com.supermap.realestate.registration.util.ConstValue.QLLX;
import com.supermap.realestate.registration.util.Global;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.wisdombusiness.core.SuperSpringContext;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import com.supermap.wisdombusiness.web.ResultMessage;

/**
 * TODO:@liushufeng:请描述这个类或接口
 * @ClassName: BDCDYHCheck
 * @author liushufeng
 * @date 2015年11月29日 上午4:46:21
 */
public class BDCDYHCheck implements CheckItem {

	/**
	 * TODO:@liushufeng:请描述这个方法的作用
	 * @Title: check
	 * @author:liushufeng
	 * @date：2015年11月29日 上午4:46:39
	 * @param xmbh
	 * @return
	 */
	@Override
	public ResultMessage check(HashMap<String,String> params) {
		String xmbh=params.get("XMBH");
		ResultMessage msg=new ResultMessage();
		msg.setSuccess("true");
		CommonDao dao = SuperSpringContext.getContext().getBean(CommonDao.class);
		BDCS_XMXX xmxx = Global.getXMXXbyXMBH(xmbh);
		if (dao != null) {
			List<BDCS_DJDY_GZ> list = new ArrayList<BDCS_DJDY_GZ>();
			if (list != null && list.size() > 0) {
				for (BDCS_DJDY_GZ djdy : list) {
					if (djdy != null) {
						if (!StringHelper.isEmpty(djdy.getBDCDYLX())) {
							BDCDYLX dylx = BDCDYLX.initFrom(djdy.getBDCDYLX());
							if (!StringHelper.isEmpty(djdy.getLY())) {
								DJDYLY ly = DJDYLY.initFrom(djdy.getLY());
								String qllx = xmxx.getQLLX();
								if (!StringHelper.isEmpty(qllx)) {
									String djlx = xmxx.getDJLX();
									if (!StringHelper.isEmpty(djlx)) {
										if (djlx.equals(DJLX.BGDJ.Value)
												&& (qllx.equals(QLLX.GYJSYDSHYQ.Value) || qllx.equals(QLLX.GYJSYDSHYQ_FWSYQ.Value) || qllx.equals(QLLX.GYJSYDSHYQ_FWSYQ.Value)
														|| qllx.equals(QLLX.JTJSYDSYQ.Value) || qllx.equals(QLLX.JTJSYDSYQ_FWSYQ.Value) || qllx.equals(QLLX.ZJDSYQ.Value) || qllx
															.equals(QLLX.ZJDSYQ_FWSYQ.Value)) && ly.equals(DJDYLY.XZ.Value)) {
											continue;
										} else {
											RealUnit unit = UnitTools.loadUnit(dylx, ly, djdy.getBDCDYID());
											if (unit != null) {
												if (StringHelper.isEmpty(unit.getBDCDYH())) {
													msg.setSuccess("false");
													break;
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		return msg;
	}
}
