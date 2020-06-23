package com.supermap.realestate.registration.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.supermap.realestate.registration.model.BDCS_CONST;
import com.supermap.realestate.registration.service.DYJETJService;
import com.supermap.realestate.registration.util.ConstHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import com.supermap.wisdombusiness.web.Message;

@Service("DYJETJSERVICE")
public class DYJETJServiceImpl implements DYJETJService {

	@Autowired
	private CommonDao Dao;

	/**
	 * @Description: 获取查询界面的下拉框内容
	 * @Title: GetCombobox
	 * @Author: zhaomengfan
	 * @Date: 2017年6月27日下午5:33:10
	 * @return
	 * 
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Message GetCombobox() {
		Map dybdclx = ConstHelper.getDictionary("DYBDCLX");
		Set set = dybdclx.entrySet();
		Iterator i = set.iterator();
		List<BDCS_CONST> list = new ArrayList<BDCS_CONST>();
		while (i.hasNext()) {
			Map.Entry<String, String> entry1 = (Map.Entry<String, String>) i.next();
			BDCS_CONST c = new BDCS_CONST();
			c.setCONSTVALUE(entry1.getKey());
			c.setCONSTTRANS(entry1.getValue());
			list.add(c);
		}
		Message msg = new Message();
		msg.setRows(list);
		return msg;
	}

	/**
	 * @Description: 收费统计数据获取
	 * @Title: GetSFTJ
	 * @Author: zhaomengfan
	 * @Date: 2017年6月27日下午12:52:01
	 * @param param
	 * @return
	 * 
	 */
	@Override
	public Message GetDYJETJ(Map<String, String> param) {
		StringBuilder build = new StringBuilder();
		build.append("SELECT COUNT(PROJECT_ID) AS GS,SUM(BDBE) / 10000 AS BDBE, SUM(ZGE) / 10000 AS ZGE, QLRLX ");
		build.append("FROM (SELECT DISTINCT T.PROJECT_ID, CASE WHEN DW.CONSTVALUE > 1 THEN Z.BDBZZQSE * 10000 ELSE Z.BDBZZQSE END BDBE, ");
		build.append("CASE WHEN DW.CONSTVALUE > 1 THEN Z.ZGZQSE * 10000 ELSE Z.ZGZQSE END ZGE, CON.CONSTTRANS AS QLRLX FROM BDCK.BDCS_XMXX T ");
		build.append("LEFT JOIN BDCK.BDCS_DJDY_GZ A ON A.XMBH = T.XMBH ");
		build.append("LEFT JOIN BDCK.BDCS_QL_GZ B ON A.DJDYID = B.DJDYID AND A.XMBH = B.XMBH ");
		build.append("LEFT JOIN BDCK.BDCS_FSQL_GZ Z ON B.QLID = Z.QLID ");
		build.append("LEFT JOIN BDCK.BDCS_SQR SQR ON T.XMBH = SQR.XMBH ");
		build.append("LEFT JOIN (SELECT A.CONSTVALUE, A.CONSTTRANS FROM BDCK.BDCS_CONST A LEFT JOIN BDCK.BDCS_CONSTCLS B ON A.CONSTSLSID = B.CONSTSLSID ");
		build.append("WHERE B.CONSTCLSTYPE = 'QLRLX') CON ON SQR.SQRLX = CON.CONSTVALUE ");
		build.append("LEFT JOIN (SELECT A.CONSTVALUE, A.CONSTTRANS FROM BDCK.BDCS_CONST A LEFT JOIN BDCK.BDCS_CONSTCLS B ON A.CONSTSLSID = B.CONSTSLSID ");
		build.append("WHERE B.CONSTCLSTYPE = 'JEDW') DW ON Z.ZQDW = DW.CONSTVALUE ");
		build.append("WHERE Z.FSQLID IS NOT NULL ");
		if(!StringHelper.isEmpty(param.get("DYWLX"))){
			build.append("AND Z.DYBDCLX =:DYWLX ");
		}
		if(!StringHelper.isEmpty(param.get("QLR"))){
			build.append("AND SQR.SQRXM =:QLR AND SQR.SQRLB='1' ");
		}
		if(!StringHelper.isEmpty(param.get("YWR"))){
			build.append("AND INSTR(Z.DYR,:YWR)>0 AND SQR.SQRLB='2' ");
		}
		build.append("AND T.QLLX = '23' ");
		build.append("AND T.DJLX IN ('100', '700') ");
		build.append("AND SUBSTR(T.PROJECT_ID, 18, 1) <> '6' ");
		build.append("AND T.SFDB = 1 ");
		build.append("AND B.DJSJ BETWEEN TO_DATE(:DBSJ_Q, 'YYYY-MM-DD HH24:MI:SS') AND TO_DATE(:DBSJ_Z, 'YYYY-MM-DD HH24:MI:SS')) AA ");
		build.append("GROUP BY AA.QLRLX ");

		List<Map> maps = Dao.getDataListByFullSql(build.toString(), param);
		Message m = new Message();
		m.setRows(maps);
		m.setTotal(maps.size());
		return m;
	}

}
