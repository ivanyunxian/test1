package com.supermap.realestate.registration.service.impl;

import com.supermap.realestate.registration.model.BDCS_SFXX;
import com.supermap.realestate.registration.service.SFMXTJService;
import com.supermap.realestate.registration.util.RequestHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import com.supermap.wisdombusiness.web.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("SFMXTJService")
public class SFMXTJServiceImpl implements SFMXTJService {

	@Autowired
	private CommonDao baseCommonDao;

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
		List<Map> Department = baseCommonDao.getDataListByFullSql(
				"SELECT DISTINCT SFBMMC VALUE,SFBMMC TEXT FROM BDCK.BDCS_SFDY WHERE SFBMMC IS NOT NULL ORDER BY SFBMMC");
		Message dep = new Message();
		dep.setMsg("Department");
		dep.setRows(Department);
		list.add(dep);
		// 收费人员
		List<Map> User = baseCommonDao.getDataListByFullSql(
				"SELECT DISTINCT STAFF_NAME VALUE,STAFF_NAME TEXT FROM BDC_WORKFLOW.WFI_ACTINST AC WHERE  ACTINST_NAME LIKE '%收费%' AND ACTINST_STATUS=3 AND OPERATION_TYPE <> '9' AND STAFF_NAME IS NOT NULL ORDER BY STAFF_NAME");
		Message user = new Message();
		user.setMsg("User");
		user.setRows(User);
		list.add(user);
		// 收费类型
		List<Map> SFLX = baseCommonDao
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
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Message GetSFTJ(Map<String, String> param) {
		StringBuilder Str = new StringBuilder();
		Str.append(" SELECT TO_CHAR(PRO.ACTINST_END,'YYYY-MM-DD HH24:MI:SS') AS SFSJ,T.SFBMMC, XM.SLRY, PRO.STAFF_NAME, Y.SFXLMC AS SFDL, T.SFKMMC AS SFMC, XM.YWLSH, T.SSJE, T.YSJE, (SELECT COUNT(1) FROM BDCK.BDCS_DJDY_GZ DY WHERE DY.XMBH = XM.XMBH) AS GS, ");
		Str.append(" (SELECT WM_CONCAT(TO_CHAR(SQR.SQRLB||'-'||SQR.SQRXM)) FROM BDCK.BDCS_SQR SQR WHERE SQR.XMBH = XM.XMBH) AS SQR ");
		Str.append(" FROM BDCK.BDCS_DJSF T ");
		Str.append(" LEFT JOIN BDCK.BDCS_SFDY Y ON Y.ID = T.SFDYID ");
		Str.append(" LEFT JOIN BDCK.BDCS_XMXX XM ON XM.XMBH=T.XMBH ");
		Str.append(" INNER JOIN (SELECT PRO.FILE_NUMBER AS YWH,AC.ACTINST_END,AC.STAFF_NAME FROM BDC_WORKFLOW.WFI_PROINST PRO ");
		Str.append(
				" LEFT JOIN (SELECT AC.PROINST_ID,AC.ACTINST_END,AC.STAFF_NAME， ROW_NUMBER() OVER(PARTITION BY AC.PROINST_ID ORDER BY AC.ACTINST_END ASC) RANK ");
		Str.append(" FROM BDC_WORKFLOW.WFI_ACTINST AC WHERE 1=1 AND ");
		Str.append(
				" AC.ACTINST_END BETWEEN TO_DATE(:SFSJ_Q, 'YYYY-MM-DD HH24:MI:SS') AND TO_DATE(:SFSJ_Z, 'YYYY-MM-DD HH24:MI:SS')) ");
		Str.append(" AC ON PRO.PROINST_ID = AC.PROINST_ID WHERE AC.RANK = 1) PRO ON PRO.YWH = T.YWH ");
		Str.append(" WHERE T.SSJE IS NOT NULL ");
		if (!StringHelper.isEmpty(param.get("Department"))) {
			Str.append(" AND T.SFBMMC =:Department ");
		}
		if (!StringHelper.isEmpty(param.get("User"))) {
			Str.append(" AND T.SFRY =:User ");
		}
		if (!StringHelper.isEmpty(param.get("SFLX"))) {
			Str.append(" AND T.SFDYID =:SFLX ");
		}
		if (!StringHelper.isEmpty(param.get("YWLSH"))) {
			Str.append(" AND XM.YWLSH =:YWLSH ");
		}
		if (!StringHelper.isEmpty(param.get("SLRY"))) {
			Str.append(" AND XM.SLRY =:SLRY ");
		}
		Str.append( " ORDER BY XM.YWLSH ");
		List<Map> list = baseCommonDao.getDataListByFullSql(Str.toString(), param);
		for (Map map : list) {
			String sqrs = StringHelper.formatObject(map.get("SQR"));
			List<String> qlr = new ArrayList<String>();
			List<String> ywr = new ArrayList<String>();
			if(!StringHelper.isEmpty(sqrs)){
				String[] sqr = sqrs.split(",");
				for (String str : sqr) {
					if(str.startsWith("1-")){
						qlr.add(str.substring(2));
					}else if(str.startsWith("2-")){
						ywr.add(str.substring(2));
					}
				}
				String sqrStr ="权利人：" + StringHelper.formatList(qlr, ",") +"\n义务人："+ StringHelper.formatList(ywr, ",");
				map.put("SQR", sqrStr);
			}
		}
		Message msg = new Message();
		msg.setRows(list);
		return msg;
	}

	@SuppressWarnings({ "rawtypes" })
	@Override
	public Message getZXJFTJ(HttpServletRequest request) {
		StringBuilder builder = new StringBuilder();
		Message msg = new Message();
		try {
			String currentPage = RequestHelper.getParam(request, "currentPageIndex");
			String pageSize = RequestHelper.getParam(request, "pageSize");
			if (StringUtils.isEmpty(currentPage)) {
				currentPage = "1";
			}
			if (StringHelper.isEmpty(pageSize)) {
				pageSize = "10";
			}
			String keyString = RequestHelper.getParam(request, "value");
			String status = RequestHelper.getParam(request, "status");
			//是否分页，导出时Excel用到
			String isPage = RequestHelper.getParam(request, "isPage");
			String startdate = RequestHelper.getParam(request, "startdate");
			String enddate = RequestHelper.getParam(request, "enddate");

			builder.append("FROM ( ");
			builder.append("SELECT XMXX.XMBH, XMXX.YWLSH, PRO.WLSH, XMXX.SFQR, SFXX.PJZT, SFXX.PJBZ, SFXX.SFSJ, SFXX.SFJE, ");
			builder.append("(SELECT TO_CHAR(WM_CONCAT(TO_CHAR(SQRXM))) FROM BDCK.BDCS_SQR WHERE SQRLB='1' AND XMBH = XMXX.XMBH) QLRMC, ");
			builder.append("(SELECT TO_CHAR(WM_CONCAT(TO_CHAR(SQRXM))) FROM BDCK.BDCS_SQR WHERE SQRLB='2' AND XMBH = XMXX.XMBH) YWRMC, ");
			builder.append("(CASE WHEN SFXX.PJZT = '1' THEN '已开票' ELSE '未开票' END) AS PJZTMC, ");
			builder.append("(CASE WHEN SFXX.SFZT = '1' THEN '线下已收费' WHEN SFXX.SFZT = '2' THEN '线上已收费' ");
			builder.append("WHEN SFXX.SFZT = '3' THEN '线上收费失败' ELSE '未收费' END) AS SFZT ");
			builder.append("FROM BDCK.BDCS_XMXX XMXX ");
			builder.append("LEFT JOIN BDCK.BDCS_SFXX SFXX ");
			builder.append("ON XMXX.XMBH = SFXX.XMBH ");
			builder.append("LEFT JOIN BDC_WORKFLOW.WFI_PROINST PRO ");
			builder.append("ON XMXX.PROJECT_ID = PRO.FILE_NUMBER ");
			builder.append("WHERE EXISTS (SELECT 1 FROM BDCK.BDCS_SFXX T WHERE T.XMBH = XMXX.XMBH) ");
			//只查询线上缴费的
			builder.append("AND XMXX.SFQR IN ('2','3') ");
			if (!StringHelper.isEmpty(keyString)) {
				builder.append(" AND (XMXX.YWLSH LIKE '%" + keyString + "%' OR PRO.WLSH LIKE '%" + keyString + "%')");
			}
			if (!StringHelper.isEmpty(startdate)) {
				builder.append(" AND SFXX.SFSJ > TO_DATE('" + startdate + " 00:00:00','yyyy-mm-dd HH24:mi:ss')");
			}
			if (!StringHelper.isEmpty(enddate)) {
				builder.append(" AND SFXX.SFSJ < TO_DATE('" + enddate + " 23:59:59','yyyy-mm-dd HH24:mi:ss')");
			}
			builder.append(" ) T ORDER BY SFSJ");

			long tatalCount = baseCommonDao.getCountByFullSql(builder.toString());
			msg.setTotal(tatalCount);
			List<Map> list = new ArrayList<Map>();
			if (tatalCount > 0) {
				if ("1".equals(isPage)) {
					list = baseCommonDao.getDataListByFullSql("SELECT * " + builder.toString());
				} else {
					list = baseCommonDao.getPageDataByFullSql("SELECT * " + builder.toString(), StringHelper.getInt(currentPage), StringHelper.getInt(pageSize));
				}
			}
			msg.setRows(list);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return msg;
	}

	@Override
	public Message ModifyPJZT(HttpServletRequest request) {
		Message msg = new Message();
		msg.setSuccess("false");
		msg.setMsg("修改票据状态失败！");
		try {
			String xmbh = RequestHelper.getParam(request, "xmbh");
			String pjzt = RequestHelper.getParam(request, "pjzt");
			String pjbz = RequestHelper.getParam(request, "pjbz");
			String[] xmbhs = xmbh.split(",");
			String str = "";
			for (String _xmbh : xmbhs) {
				_xmbh = "'" + _xmbh + "'";
				if (StringHelper.isEmpty(str)) {
					str = _xmbh;
				} else {
					str += "," + _xmbh;
				}
			}
            List<BDCS_SFXX> list = baseCommonDao.getDataList(BDCS_SFXX.class, " XMBH IN(" + str + ")");
			if (list != null && list.size() > 0) {
			    for (BDCS_SFXX sfxx : list) {
                    sfxx.setPJZT(pjzt);
                    sfxx.setPJBZ(pjbz);
                    baseCommonDao.update(sfxx);
                }
                baseCommonDao.flush();
                msg.setSuccess("true");
                msg.setMsg("修改票据状态成功！");
            } else {
                msg.setMsg("未获取到收费信息项！");
            }
        } catch (Exception e) {
			e.printStackTrace();
			msg.setMsg("修改票据发生异常，详情：" + e.getMessage());
		}
		return msg;
	}

	@Override
	public Message RepealPJZT(HttpServletRequest request) {
		return null;
	}

}
