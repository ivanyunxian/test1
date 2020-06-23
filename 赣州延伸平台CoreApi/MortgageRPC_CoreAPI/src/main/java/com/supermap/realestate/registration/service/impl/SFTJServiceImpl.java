package com.supermap.realestate.registration.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.supermap.realestate.registration.service.SFTJService;
import com.supermap.realestate.registration.util.ObjectHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import com.supermap.wisdombusiness.framework.model.Department;
import com.supermap.wisdombusiness.web.Message;

@Service("SFTJService")
public class SFTJServiceImpl implements SFTJService {

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
	public List<Message> GetCombobox() {
		List<Message> list = new ArrayList<Message>();
		// 部门
		List<Map> Department = Dao.getDataListByFullSql(
				"SELECT DISTINCT SFBMMC VALUE,SFBMMC TEXT FROM BDCK.BDCS_SFDY WHERE SFBMMC IS NOT NULL ORDER BY SFBMMC");
		Message dep = new Message();
		dep.setMsg("Department");
		dep.setRows(Department);
		list.add(dep);
		// 收费人员
		List<Map> User = Dao.getDataListByFullSql(
				"SELECT DISTINCT STAFF_NAME VALUE,STAFF_NAME TEXT FROM BDC_WORKFLOW.WFI_ACTINST AC WHERE  ACTINST_NAME LIKE '%收费%' AND ACTINST_STATUS=3 AND OPERATION_TYPE <> '9' AND STAFF_NAME IS NOT NULL ORDER BY STAFF_NAME");
		Message user = new Message();
		user.setMsg("User");
		user.setRows(User);
		list.add(user);
		// 收费类型
		List<Map> SFLX = Dao
				.getDataListByFullSql("SELECT SFDY.ID,SFDY.SFDLMC,SFDY.SFXLMC,SFDY.SFKMMC FROM BDCK.BDCS_SFDY SFDY");
		Map<String, String> DL = new HashMap<String, String>();
		Map<String, String> XL = new HashMap<String, String>();
		for (Map map : SFLX) {
			DL.put(StringHelper.formatObject(map.get("SFDLMC")), StringHelper.formatObject(map.get("SFDLMC")));
			XL.put(StringHelper.formatObject(map.get("SFXLMC")), StringHelper.formatObject(map.get("SFDLMC")));
		}
		List<Object> tree = new ArrayList<Object>();
		for (Map.Entry<String, String> lv : DL.entrySet()) {
			Map<String, Object> first = new HashMap<String, Object>();
			ArrayList<Map> firstchildrens = new ArrayList<Map>();
			ArrayList<String> firstid = new ArrayList<String>();
			for (Map.Entry<String, String> lv2 : XL.entrySet()) {
				if (lv.getValue().equals(lv2.getValue())) {
					Map<String, Object> second = new HashMap<String, Object>();
					ArrayList<Map> secondchildrens = new ArrayList<Map>();
					ArrayList<String> secondid = new ArrayList<String>();
					for (Map map : SFLX) {
						if (StringHelper.formatObject(map.get("SFDLMC")).equals(StringHelper.formatObject(lv.getKey()))
								&& StringHelper.formatObject(map.get("SFXLMC"))
										.equals(StringHelper.formatObject(lv2.getKey()))) {
							Map<String, Object> third = new HashMap<String, Object>();
							third.put("text", StringHelper.formatObject(lv2.getKey() + "（" + map.get("SFKMMC") + "）"));
							third.put("id", StringHelper.formatObject(map.get("ID")));
							secondid.add(StringHelper.formatObject(map.get("ID")));
							secondchildrens.add(third);
						}
					}
					second.put("children", secondchildrens);
					second.put("text", lv2.getKey());
					second.put("id", StringHelper.formatList(secondid));
					firstid.addAll(secondid);
					firstchildrens.add(second);
				}
			}
			first.put("children", firstchildrens);
			first.put("text", lv.getValue());
			first.put("id", StringHelper.formatList(firstid));
			tree.add(first);
		}
		Message sflx = new Message();
		sflx.setMsg("SFLX");
		sflx.setRows(tree);
		list.add(sflx);

		return list;
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
	public Message GetSFTJ(Map<String, String> param) {
		StringBuilder Str = new StringBuilder();
		Str.append("SELECT T.SFBMMC, PRO.STAFF_NAME, T.SFKMMC AS SFMC ,COUNT(1) AS GS,SUM(T.YSJE) AS YSJE,SUM(T.SSJE) AS SSJE ");
		Str.append(" FROM BDCK.BDCS_DJSF T ");
//		Str.append(" LEFT JOIN BDCK.BDCS_DJDY_GZ DY ON DY.XMBH = T.XMBH ");
		Str.append(" INNER JOIN (SELECT PRO.FILE_NUMBER AS YWH,AC.STAFF_NAME FROM BDC_WORKFLOW.WFI_PROINST PRO ");
		Str.append(
				" LEFT JOIN (SELECT AC.PROINST_ID,AC.STAFF_NAME， ROW_NUMBER() OVER(PARTITION BY AC.PROINST_ID ORDER BY AC.ACTINST_END ASC) RANK ");
		Str.append(" FROM BDC_WORKFLOW.WFI_ACTINST AC WHERE ACTINST_NAME LIKE '%收费%' AND ACTINST_STATUS = 3 AND ");
		Str.append(
				" AC.ACTINST_END BETWEEN TO_DATE(:SFSJ_Q, 'YYYY-MM-DD HH24:MI:SS') AND TO_DATE(:SFSJ_Z, 'YYYY-MM-DD HH24:MI:SS')) ");
		Str.append(" AC ON PRO.PROINST_ID = AC.PROINST_ID WHERE AC.RANK = 1) PRO ON PRO.YWH = T.YWH ");
		Str.append(" WHERE T.SSJE IS NOT NULL AND SUBSTR(T.YWH, 18, 1) <> '6' ");
		if (!StringHelper.isEmpty(param.get("Department"))) {
			Str.append(" AND T.SFBMMC =:Department ");
		}
		if (!StringHelper.isEmpty(param.get("User"))) {
			Str.append(" AND T.SFRY =:User ");
		}
		if (!StringHelper.isEmpty(param.get("SFLX"))) {
			Str.append(" AND T.SFDYID =:SFLX ");
		}
		Str.append( " GROUP BY T.SFBMMC, PRO.STAFF_NAME,T.SFKMMC ORDER BY T.SFBMMC, PRO.STAFF_NAME,T.SFKMMC ");
		List<Map> list = Dao.getDataListByFullSql(Str.toString(), param);
		Message msg = new Message();
		msg.setRows(list);
		return msg;
	}

}
