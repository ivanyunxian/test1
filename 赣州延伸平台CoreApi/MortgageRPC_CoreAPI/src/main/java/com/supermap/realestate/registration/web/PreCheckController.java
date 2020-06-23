package com.supermap.realestate.registration.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.supermap.realestate.registration.model.interfaces.House;
import com.supermap.realestate.registration.model.interfaces.RealUnit;
import com.supermap.realestate.registration.model.interfaces.TDYT;
import com.supermap.realestate.registration.model.interfaces.UseLand;
import com.supermap.realestate.registration.tools.UnitTools;
import com.supermap.realestate.registration.util.ConstValue.BDCDYLX;
import com.supermap.realestate.registration.util.ConstValue.DJDYLY;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;

/**
 * 
 * 登记簿Controller 跟登记簿相关的都放在这里边
 * @author 刘树峰
 * @date 2015年6月12日 上午11:46:55
 * @Copyright SuperMap
 */
@Controller
@RequestMapping("/precheck")
@Component("PreCheckController")
public class PreCheckController {

	@Autowired
	CommonDao dao;

	/**
	 * 根据项目编号登簿（URL:"/landcheck",Method:GET）
	 * 
	 * @作者：俞学斌
	 * @param xmbh
	 *            项目编号
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/landcheck/{houseid}", method = RequestMethod.GET)
	public @ResponseBody PreCheckResult precheck(@PathVariable("houseid") String houseid) throws Exception {

		PreCheckResult result = new PreCheckResult();
		int iresult = 0;
		String message = "";

		// 一、传入houseid为空，对应第一位：1
		if (StringHelper.isEmpty(houseid)) {
			iresult = iresult | 1;
			message += "传入的houseid为空;";
			result.setStatus(iresult);
			result.setMessage(message);
			return result;
		}
/*		try {
			String fulSql = "select * from bdck.bdcs_h_xz where relationid='" + houseid + "'";
			List l = dao.getDataListByFullSql(fulSql);
		} catch (Exception ee) {
System.out.println(ee.getMessage());
		}
		dao.flush();*/
		Map<String, String> map = new HashMap<String, String>();
		map.put("houseid", houseid);
		List<RealUnit> houses = UnitTools.loadUnits(BDCDYLX.H, DJDYLY.XZ, "relationid=:houseid", map);
		if (houses == null || houses.size() <= 0)
			houses = UnitTools.loadUnits(BDCDYLX.YCH, DJDYLY.XZ, "relationid=:houseid", map);

		// 二、houseid不为空，但查不到对应的房屋，对应第二位：2
		if (houses == null || houses.size() <= 0 || !(houses.get(0) instanceof House)) {
			iresult = iresult | 2;
			message += "找不到对应的房屋；";
			result.setStatus(iresult);
			result.setMessage(message);
			return result;
		}

		// 三、houseid不为空，对应的宗地不动产单元ID为空，，对应第三位：4
		House house = (House) houses.get(0);
		if (StringHelper.isEmpty(house.getZDBDCDYID())) {
			iresult = iresult | 4;
			message += "房屋对应的宗地不动产单元ID为空；";
			result.setStatus(iresult);
			result.setMessage(message);
			return result;
		}
		String zdbdcdyid = house.getZDBDCDYID();
		RealUnit land = UnitTools.loadUnit(BDCDYLX.SHYQZD, DJDYLY.XZ, zdbdcdyid);

		// 四、宗地不动产单元ID不为空，但找不到对应的使用权宗地，对应第4位：8
		if (land == null) {
			iresult = iresult | 8;
			message += "找不到对应的使用权宗地；";
			result.setStatus(iresult);
			result.setMessage(message);
			return result;
		}

		// 五、宗地权利性质为空，对应第5位：16
		UseLand useland = (UseLand) land;
		if (StringHelper.isEmpty(useland.getQLXZ())) {
			iresult = iresult | 16;
			message += "权利性质为空；";
		}
		List<TDYT> yts = useland.getTDYTS();
		boolean b = false;
		if (yts != null && yts.size() > 0) {
			for (TDYT yt : yts) {
				if (!StringHelper.isEmpty(yt.getTDYT())) {
					b = true;
					break;
				}
			}
		}
		// 六、宗地权利性质为空，对应第5位：32
		if (!b) {
			iresult = iresult | 32;
			message += "土地用途为空；";
		}
		result.setStatus(iresult);
		result.setMessage(message);
		return result;
	}

	public class PreCheckResult {
		int status;

		/**
		 * @return status
		 */
		public int getStatus() {
			return status;
		}

		/**
		 * @param iresult
		 *            要设置的 iresult
		 */
		public void setStatus(int status) {
			this.status = status;
		}

		/**
		 * @return message
		 */
		public String getMessage() {
			return message;
		}

		/**
		 * @param message
		 *            要设置的 message
		 */
		public void setMessage(String message) {
			this.message = message;
		}

		String message;
	}
}
