/**   
 * TODO:@buxiaobo:登簿时判断共享交易库中房产的办理进度，返回true：房产已办结 false:房产在办或未查询到项目记录
 * @Title: BDCDYHCheck.java 
 * @Package com.supermap.realestate.registration.check 
 * @author buxiaobo 
 * @date 2016年12月8日 16:25:01
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
 * TODO:@buxiaobo:登簿时判断共享交易库中房产的办理进度，返回true：房产已办结 false:房产在办或未查询到项目记录
 * @ClassName: BDCDYHCheck
 * @author buxiaobo
 * @date 2015年11月29日 上午4:46:21
 */
public class FCBLJDCheck implements CheckItem {

	Connection jyConnection = null;
	/**
	 * TODO:@buxiaobo:请描述这个方法的作用
	 * @Title: check
	 * @author:buxiaobo
	 * @date：2016年12月8日 16:28:26
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
					String sql = "select BLJD from gxjyk.GXJHXM where casenum='"+ casenum + "'";
					if (jyConnection == null || jyConnection.isClosed()) {
						jyConnection = JH_DBHelper.getConnect_jy();
					}
					PreparedStatement pstmt = jyConnection.prepareStatement(sql);
					ResultSet rSet = pstmt.executeQuery();
					if (rSet != null) {
						while (rSet.next()) {
							String FCBLJD = rSet.getString("BLJD");
							if(FCBLJD.equals("确认")){
								msg.setSuccess("true");
							}
						}
					}
					pstmt.close();
					rSet.close();
				}
			}
		}catch(Exception e){
			msg.setSuccess("false");
		}
		return msg;
	}
}
