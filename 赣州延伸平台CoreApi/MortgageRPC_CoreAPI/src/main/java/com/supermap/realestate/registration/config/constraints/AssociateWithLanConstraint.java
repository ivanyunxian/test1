/**   
 * 约束：调查单元已跟宗地进行关联
 * @Title: SingleMortgageConstraint.java 
 * @Package com.supermap.realestate.registration.config.constraints 
 * @author liushufeng 
 * @date 2015年7月25日 下午3:27:53 
 * @version V1.0   
 */

package com.supermap.realestate.registration.config.constraints;

import com.supermap.realestate.registration.config.Constraint;
import com.supermap.realestate.registration.model.interfaces.LandAttach;
import com.supermap.realestate.registration.model.interfaces.RealUnit;
import com.supermap.realestate.registration.tools.UnitTools;
import com.supermap.realestate.registration.util.Global;
import com.supermap.realestate.registration.util.ConstValue.BDCDYLX;
import com.supermap.realestate.registration.util.ConstValue.DJDYLY;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;

/**
 * 约束：调查单元单元已跟宗地进行关联
 * @ClassName: SingleMortgageConstraint
 * @author liushufeng
 * @date 2015年7月25日 下午3:27:53
 */
public class AssociateWithLanConstraint implements Constraint {

	/**
	 * 约束：调查单元单元已跟宗地进行关联
	 * @Title: Check
	 * @author:liushufeng
	 * @date：2015年7月25日 下午3:27:53
	 * @param dao
	 * @param xmbh
	 * @param bdcdyid
	 * @param qlid
	 * @return 如果已经跟宗地关联，返回true，否则返回false
	 */
	@Override
	public boolean check(CommonDao dao, String xmbh, String bdcdyid, String qlid, DJDYLY ly, BDCDYLX lx) {
		boolean b = false;
		RealUnit unit = UnitTools.loadUnit(lx, ly, bdcdyid);
		if(unit==null)
		{
			unit=UnitTools.loadUnit(lx, DJDYLY.GZ, bdcdyid);
		}
		if(unit==null)
		{
			unit=UnitTools.loadUnit(lx, DJDYLY.DC, bdcdyid);
		}
		if (unit instanceof LandAttach) {
			String zdbdcdyid = ((LandAttach) unit).getZDBDCDYID();
			//是否关联宗地
			if (Global.ISRELATIONLAND) {
				RealUnit landunit = UnitTools.loadUnit(BDCDYLX.SHYQZD,
						DJDYLY.XZ, zdbdcdyid);
				if (landunit != null) {
					b = true;
				}
			} else {
				b = true;
			}
		}
		return b;
	}
}
