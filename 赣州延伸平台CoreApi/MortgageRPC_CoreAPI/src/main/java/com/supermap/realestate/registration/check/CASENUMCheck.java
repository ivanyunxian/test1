/**   
 * TODO:@buxiaobo:受理转出判断是否抽取过房产业务号，返回true：该项目已经抽取房产业务号
 * @Title: BDCDYHCheck.java 
 * @Package com.supermap.realestate.registration.check 
 * @author buxiaobo 
 * @date 2016年12月27日 19:04:07
 * @version V1.0   
 */

package com.supermap.realestate.registration.check;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import com.supermap.realestate.registration.model.BDCS_DJDY_GZ;
import com.supermap.realestate.registration.model.BDCS_QL_GZ;
import com.supermap.realestate.registration.model.BDCS_XMXX;
import com.supermap.realestate.registration.model.interfaces.RealUnit;
import com.supermap.realestate.registration.tools.UnitTools;
import com.supermap.realestate.registration.util.ConstValue.BDCDYLX;
import com.supermap.realestate.registration.util.ConstValue.DJDYLY;
import com.supermap.realestate.registration.util.ConstValue.DJLX;
import com.supermap.realestate.registration.util.ConstValue.QLLX;
import com.supermap.realestate.registration.util.Global;
import com.supermap.realestate.registration.util.JH_DBHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.wisdombusiness.core.SuperSpringContext;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import com.supermap.wisdombusiness.web.ResultMessage;

/**
 * TODO:@buxiaobo:受理转出判断是否抽取过房产业务号，返回true：该项目已经抽取房产业务号
 * @ClassName: BDCDYHCheck
 * @author buxiaobo
 * @date 2015年11月29日 上午4:46:21
 */
public class CASENUMCheck implements CheckItem {

	/**
	 * TODO:@buxiaobo:请描述这个方法的作用
	 * @Title: check
	 * @author:buxiaobo
	 * @date：2016年12月27日 19:04:18
	 * @param xmbh
	 * @return
	 */
	@Override
	public ResultMessage check(HashMap<String,String> params) {
		String xmbh=params.get("XMBH");
		ResultMessage msg=new ResultMessage();
		msg.setSuccess("false");
		try{
			CommonDao dao = SuperSpringContext.getContext().getBean(CommonDao.class);
			BDCS_XMXX xmxx = Global.getXMXXbyXMBH(xmbh);
			if (dao != null && xmxx!=null) {
				List<BDCS_QL_GZ> qllist = dao.getDataList(BDCS_QL_GZ.class, "XMBH = '"+xmbh+"'");
				if (qllist != null && qllist.size() > 0) {
					String casenum=qllist.get(0).getCASENUM();
					if(casenum!=null &&!casenum.equals("")&&casenum.length()>0){
						msg.setSuccess("true");
					}else{
						msg.setSuccess("false");
					}
				}
			}
		}catch(Exception e){
			msg.setSuccess("false");
		}
		return msg;
	}
}
