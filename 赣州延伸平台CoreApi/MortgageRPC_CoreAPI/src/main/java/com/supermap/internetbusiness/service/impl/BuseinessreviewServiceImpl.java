package com.supermap.internetbusiness.service.impl;

import java.util.*;

import com.supermap.wisdombusiness.synchroinline.model.Pro_fwxx;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.supermap.internetbusiness.dao.CommonDao;
import com.supermap.internetbusiness.service.BusinessreviewService;
import com.supermap.realestate.registration.util.ConfigHelper;
import com.supermap.realestate.registration.util.ConstValue;
import com.supermap.realestate.registration.util.Global;
import com.supermap.realestate.registration.util.ConstValue.BDCDYLX;
import com.supermap.realestate.registration.util.ConstValue.DJLX;
import com.supermap.realestate.registration.util.ConstValue.QLLX;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDaoInline;
import com.supermap.wisdombusiness.synchroinline.model.JsonMessage;
import com.supermap.wisdombusiness.synchroinline.model.Pro_proinst;

import net.sf.json.JSONArray;

@Service("businessreviewService")
public class BuseinessreviewServiceImpl implements BusinessreviewService {
	
	@Autowired
	private CommonDao commonDao;

//	@Autowired
//	private baseCommonDaoInline baseCommonDaoInline;
	
	@Autowired
	CommonDaoInline baseCommonDaoInline;
	
	@Override
	public JsonMessage getAllData(int pageIndex, int pageSize, String sqr,  String bdcqzh,
			String zjh, String zjlx, String shzt, String deadline_s, String deadline_e, String isvaguequery,
			String prolsh,String ywlx,String zl ,String phone,String sfjsbl,String flag,String prodefid) {
		JsonMessage jsonMessage  = new JsonMessage();
		Map<String, String> paraMap = new HashMap<String, String>();
		String AreaCode = Global.getCurrentUserInfo().getAreaCode().trim();
		String strWhere = "";
		if("1".equals(sfjsbl)){//及时办结在办箱
			strWhere = " where a.ID = b.PROINST_ID and a.id = c.PROINST_ID and a.sfjsbl in ('1','2')";
		}else if("1".equals(flag) && "".equals(sfjsbl)){//在办箱
			strWhere = " where a.ID = b.PROINST_ID and a.id = c.PROINST_ID and a.shzt in ('0','10') and (a.sfjsbl not in ('1','2') or a.sfjsbl is null )";
		}else if("2".equals(flag) && "".equals(sfjsbl)){//已办箱
			strWhere = " where a.ID = b.PROINST_ID and a.id = c.PROINST_ID and a.shzt not in ('0','10')  and  (a.sfjsbl not in ('1','2') or a.sfjsbl is null )";
		}else{
			strWhere = " where a.ID = b.PROINST_ID and a.id = c.PROINST_ID  ";
		}
		StringBuffer sql = new StringBuffer(strWhere);
		String xzqdm = ConfigHelper.getNameByValue("XZQHDM");
		//桂林、玉林暂时不需要分区
		String[] xzqdms = {"450300","450902","450923"};
		if(!Arrays.asList(xzqdms).contains(xzqdm)|| !xzqdm.contains("4509")) {
			sql.append(" and a.qxdm = '"+AreaCode+"'");
			//paraMap.put("qxdm", AreaCode);
		}
		//项目的外网受理流水号 或是 内网流水号
		if (!StringHelper.isEmpty(prolsh)) {
			sql.append(" and (instr(a.prolsh,:prolsh)>0 or instr(a.lsh,:prolsh)>0) ");
			paraMap.put("prolsh", prolsh);
		}
		//项目的业务类型
		if (!StringHelper.isEmpty(ywlx)) {
			sql.append(" and a.ywlx = :ywlx ");
			paraMap.put("ywlx", ywlx);
		}
		
		//申请受理用户姓名
		if (!StringHelper.isEmpty(sqr)) {
			if ("true".equals(isvaguequery)) {
				sql.append(" AND instr(b.SQR_NAME,:SQR_NAME)>0 ");
			} else {
				sql.append(" AND b.SQR_NAME = :SQR_NAME ");
			}
			paraMap.put("SQR_NAME", sqr);
		}
	
		//申请受理用户证件类型
		if (!StringHelper.isEmpty(zjlx)) {
			sql.append(" AND b.SQR_ZJLX = :SQR_ZJLX ");
			paraMap.put("SQR_ZJLX", zjlx);
		}
		
		//申请受理用户证件号
		if (!StringHelper.isEmpty(zjh)) {
			if ("true".equals(isvaguequery)) {
				sql.append(" AND instr(b.SQR_ZJH,:SQR_ZJH)>0 ");
			} else {
				sql.append(" AND b.SQR_ZJH = :SQR_ZJH ");
			}
			paraMap.put("SQR_ZJH", zjh);
		}

		//申请人手机号
		if (!StringHelper.isEmpty(phone)) {
			if ("true".equals(isvaguequery)) {
				sql.append(" AND instr(a.USERPHONE,:SQR_TEL)>0 ");
			} else {
				sql.append(" AND a.USERPHONE = :SQR_TEL ");
			}
			paraMap.put("SQR_TEL", phone);
		}
		
		//不动产权证/证明
		if (!StringHelper.isEmpty(bdcqzh)) {
			if ("true".equals(isvaguequery)) {
				sql.append(" AND instr(a.BDCQZMH,:BDCQZMH)>0");
			} else {
				sql.append(" AND a.BDCQZMH = :BDCQZMH ");
			}
			paraMap.put("BDCQZMH", bdcqzh);
		}
		
		
		//不动产单元号
//		if (!StringHelper.isEmpty(bdcdyh)) {
//			if ("true".equals(isvaguequery)) {
//				sql.append(" AND instr(BDCDYH,:BDCDYH)>0 ");
//			} else {
//				sql.append(" AND BDCDYH = :BDCDYH ");
//			}
//			paraMap.put("BDCDYH", bdcdyh);
//		}
		
		
		//坐落
		if (!StringHelper.isEmpty(zl)) {
			if ("true".equals(isvaguequery)) {
				sql.append(" AND (instr(A.BDCZL,:BDCZL)>0 OR instr(C.ZL,:ZL)>0)");
			} else {
				sql.append(" AND (A.BDCZL = :BDCZL OR instr(C.ZL,:ZL)>0)");
			}
			paraMap.put("BDCZL", zl);
			paraMap.put("ZL", zl);
		}
		
		
		//审核状态
		if (!StringHelper.isEmpty(shzt)) {
			sql.append(" AND A.SHZT in ("+shzt+") ");
		}
		
		//加入行政区划代码
		String xzqhdm = ConfigHelper.getNameByValue("XZQHDM");
		if(!StringHelper.isEmpty(xzqhdm)) {
			sql.append(" AND A.AREACODE = '"+xzqhdm+"' ");
		}
		
		//申请时间
		if (!StringHelper.isEmpty(deadline_s)) {
			sql.append(" AND A.PROSTART >= to_date(:DEADLINE_S,'yyyy-mm-dd hh24:mi:ss') ");
			paraMap.put("DEADLINE_S", deadline_s+" 00:00:00");
		}
		//申请时间
		if (!StringHelper.isEmpty(deadline_e)) {
			sql.append(" AND A.PROSTART <= to_date(:DEADLINE_E,'yyyy-mm-dd hh24:mi:ss') ");
			paraMap.put("DEADLINE_E", deadline_e+" 23:59:59");
		}
		//流程id，
		if (!StringHelper.isEmpty(prodefid)) {
			long ccount = commonDao.getCountByFullSql
					(" from bdc_workflow.Wfd_Prodef where PRODEF_ID = '"+prodefid+"' "); //最后一级
			if(ccount > 0){
				sql.append(" AND A.PRODEFCODE = :PRODEFCODE ");
				paraMap.put("PRODEFCODE", prodefid);
			}else{
//				sql.append(" AND exists ( "
//						+ " 	select 1 from bdc_workflow.Wfd_Prodef b where prodefclass_id in( "  
//						+ "		select prodefclass_id from  BDC_WORKFLOW.WFD_PROCLASS t"
//						+ "		start with t.prodefclass_id = '"+prodefid+"' "
//						+ "		connect by t.prodefclass_pid = prior t.prodefclass_id"
//						+ "   ) AND A.PRODEFCODE = b.prodef_id "
//						+ " ) "); //因为dao不能连上bdc库用不了
				List<Map> prodefidlist = commonDao.getDataListByFullSql(
						" 	select b.PRODEF_ID from bdc_workflow.Wfd_Prodef b where prodefclass_id in( "  
						+ "		select prodefclass_id from  BDC_WORKFLOW.WFD_PROCLASS t"
						+ "		start with t.prodefclass_id = '"+prodefid+"' "
						+ "		connect by t.prodefclass_pid = prior t.prodefclass_id"
						+ "   ) "
						);
				if(prodefidlist.size() > 0){
					String prodefids = "";
					for(Map prodefidmap : prodefidlist){
						prodefids += "'" +prodefidmap.get("PRODEF_ID")+ "',";
					}
					prodefids = prodefids.substring(0, prodefids.length()-1);
					sql.append(" AND A.PRODEFCODE in ("+prodefids+") ");
				}else{
					sql.append(" AND A.PRODEFCODE = :PRODEFCODE ");
					paraMap.put("PRODEFCODE", prodefid);
				}
			}
		}
		
		
		
		long total = baseCommonDaoInline.getCountByFullSql(" from (select distinct a.id from INLINE_INNER.PRO_PROINST a ,"
				+ "INLINE_INNER.PRO_PROPOSERINFO b ,INLINE_INNER.PRO_FWXX C" + sql+")",paraMap);
		jsonMessage.setMsg(String.valueOf(total));
		jsonMessage.setState(false);
		if (total > 0) {
			List<Map> proinst_list = baseCommonDaoInline.getDataListByFullSql("SELECT SQR_NAME_LIST, ID, PRODEFCODE, PROSTART,"
					+ "  SHZT, PROLSH, LSH, BDCZL, BDCQZMH, YWLX,DJLX,QLLX,NAME,TJSJ FROM  "

					+ "(select rownum rn,SQR_NAME_LIST, ID, PRODEFCODE, PROSTART, SHZT, PROLSH, LSH, BDCZL, BDCQZMH, YWLX,djlx,qllx,name,TJSJ "
					+ " from  (SELECT   ListAgg(TO_CHAR(SQR_NAME),',') within group (order by a.id)  AS SQR_NAME_LIST ,  "
					+ "A.ID, A.PRODEFCODE, A.PROSTART, A.SHZT,  A.BDCZL, A.BDCQZMH, A.YWLX, A.PROLSH,A.LSH,A.NAME,A.TJSJ, "
					+ " (SELECT CONSTTRANS  FROM INLINE_INNER.PRO_CONST WHERE CONSTSLSID = 21 AND CONSTVALUE = a.DJLX) as DJLX, "
					+ " (SELECT CONSTTRANS  FROM INLINE_INNER.PRO_CONST WHERE CONSTSLSID = 8 AND CONSTVALUE = a.QLLX) as QLLX "
					+ "   FROM INLINE_INNER.PRO_PROINST a,INLINE_INNER.PRO_PROPOSERINFO b ,INLINE_INNER.PRO_FWXX C " +sql
					+ "  GROUP BY  A.ID, A.PRODEFCODE, A.PROSTART, A.SHZT, A.PROLSH, A.LSH, A.BDCZL, A.BDCQZMH, A.YWLX,A.QLLX,A.DJLX,A.NAME,A.TJSJ "
					+ " order by A.PROSTART  ) "
					
						+ " where ROWNUM < " + (pageIndex * pageSize + 1)+ ") WHERE RN >" + (pageIndex - 1) * pageSize,paraMap);
			//如果是根据申请人的一些信息查询，则会出现申请人里只有被查询的申请人名字的情况
			if(!StringHelper.isEmpty(sqr) || !StringHelper.isEmpty(zjh) || !StringHelper.isEmpty(zjlx) ) {
				for(Map map : proinst_list) {
					List<Map> sqrnamelist = baseCommonDaoInline.getDataListByFullSql("SELECT ListAgg(TO_CHAR(B.SQR_NAME ), ',') within group(order by a.id) AS SQR_NAME_LIST \n" +
							"FROM PRO_PROINST A ,PRO_PROPOSERINFO B WHERE A.ID=B.PROINST_ID AND A.ID = '"+map.get("ID")+"'");
					if(sqrnamelist!=null && sqrnamelist.size()>0) {
						map.put("SQR_NAME_LIST", sqrnamelist.get(0).get("SQR_NAME_LIST"));
					}
				}
			}
			jsonMessage.setData(proinst_list);
			jsonMessage.setState(true);
		}
		return jsonMessage;
	}

	@Override
	public JsonMessage getEnterpriseAuditInfoData(int pageIndex, int pageSize, String id, String djlx, String qllx) {
		JsonMessage msg = new JsonMessage();
		List<Map> dyInfoList = new ArrayList<Map>();
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("dyInfo", "");
		data.put("bdcdyids", "");
		data.put("auditOpinion", "");
		data.put("toal", "0");
		if (!StringHelper.isEmpty(id)) {
			Map<String, String> paraMap = new HashMap<String, String>();
			Map<String, Object> createSQL = new HashMap<String, Object>();
			paraMap.put("ID", id);
			String k_user = " bdck.";
			//只能写不动产登记系统中的单元表
			String k_table_dy = "bdcs_h_xz   ";
			String row = " ID,BDCDYH,ZL,DYH,FWSYQMJ ";
			boolean bdcdyhSQL_b = false;
			boolean bdcqzmhSQL_b = false;
			//判断业务类型，确定去不动产登记系统哪个库中找数据。
			Map map = getRtconstraint(id);
			if(map != null) {
				String bdcdylx = map.get("BDCDYLX").toString();
				String ly = map.get("LY").toString();
				data.put("bdcdylx", ConstValue.BDCDYLX.initFrom(bdcdylx).toString());
				
				//确定初审的对比表
				if(ConstValue.DJDYLY.DC.Value.equals(ly)) {
					k_user = " bdcdck.";
					k_table_dy = "B"+ConstValue.BDCDYLX.initFrom(bdcdylx).XZTableName;
				} else if(ConstValue.DJDYLY.XZ.Value.equals(ly)) {
					k_table_dy = ConstValue.BDCDYLX.initFrom(bdcdylx).XZTableName;
				} else if(ConstValue.DJDYLY.GZ.Value.equals(ly)) {
					k_table_dy = ConstValue.BDCDYLX.initFrom(bdcdylx).GZTableName;
				} else if(ConstValue.DJDYLY.LS.Value.equals(ly)) {
					k_table_dy = ConstValue.BDCDYLX.initFrom(bdcdylx).LSTableName;
				}
				

				//PRO_FWXX表保存的是不动产权证/证明号的时候，用来确定返回的字段
				if (ConstValue.BDCDYLX.initFrom(bdcdylx).equals(ConstValue.BDCDYLX.H)) {
					row = " h.BDCDYID AS ID,h.BDCDYH,h.ZL,h.DYH,h.SCJZMJ AS FWSYQMJ ";
				} else if (ConstValue.BDCDYLX.initFrom(bdcdylx).equals(ConstValue.BDCDYLX.YCH)) {
					row = " h.BDCDYID AS ID,h.BDCDYH,h.ZL,h.DYH,h.YCJZMJ AS FWSYQMJ ";
				} else if (ConstValue.BDCDYLX.initFrom(bdcdylx).equals(ConstValue.BDCDYLX.SHYQZD)) {
					row = " h.BDCDYID as ID,h.BDCDYH,h.ZL,h.ZDMJ,h.ZDSZD,h.ZDSZX,h.ZDSZN,h.ZDSZB ";
				} else if (ConstValue.BDCDYLX.initFrom(bdcdylx).equals(ConstValue.BDCDYLX.SYQZD)) {
					row = " h.BDCDYID as ID,h.BDCDYH,h.ZL,h.ZDMJ,h.ZDSZD,h.ZDSZX,h.ZDSZN,h.ZDSZB ";
				} else if (ConstValue.BDCDYLX.initFrom(bdcdylx).equals(ConstValue.BDCDYLX.LD)) {
					row = " h.BDCDYID as ID,h.BDCDYH,h.ZL,h.ZDMJ,h.ZDSZD,h.ZDSZX,h.ZDSZN,h.ZDSZB ";
				} 
			}else {
				data.put("auditOpinion", "没有找到对应的流程！");
				msg.setState(false);
				msg.setData(data);
				return msg;
			}
			//确定外网流程录入的单元数据表
			createSQL = this.createSQL(id, " BDCDYH,BDCQZMH,CHID ", "PRO_FWXX", bdcdyhSQL_b, bdcqzmhSQL_b);
			
			
			if (createSQL != null) {
				bdcdyhSQL_b = (Boolean) createSQL.get("bdcdyh_b");
				bdcqzmhSQL_b = (Boolean) createSQL.get("bdcqzmh_b");
				//不动产登记系统存在的单元个数
				long toal = this.getDYtoGS(bdcdyhSQL_b, bdcqzmhSQL_b, createSQL, k_user, k_table_dy);
				if (bdcdyhSQL_b) {
					//外网录入的单元个数
					long dytoal = Long.parseLong(createSQL.get("dytoal").toString());
					//有bdcdyh为查询条件的，单元数据就是取外网的数据列表，总数就是dytoal
					if (toal == 0) {
						data = this.getDataBybdcdyh(data, pageIndex, pageSize, paraMap, k_user, k_table_dy,
								createSQL.get("bdcdyhSQL").toString());
						data.put("toal", dytoal);
						data.put("bdcdyids", "");
						data.put("auditOpinion", "初审不通过<br>企业的单元在不动产登记系统中没有找到的单元<br>");
						msg.setState(false);
					} else if (dytoal > toal) {
						data = this.getDataBybdcdyh(data, pageIndex, pageSize, paraMap, k_user, k_table_dy,
								createSQL.get("bdcdyhSQL").toString());
						msg.setState(false);
						data.put("toal", dytoal);
						long no_tg_dy = dytoal - toal;
						data.put("auditOpinion", "初审不通过<br>企业的单元在不动产登记系统中没有找到的单元:" + no_tg_dy + "个");
					} else {
						data = this.getDataBybdcdyh(data, pageIndex, pageSize, paraMap, k_user, k_table_dy,
								createSQL.get("bdcdyhSQL").toString());
						data.put("toal", dytoal);
						data.put("auditOpinion", "初审通过<br>企业的单元在不动产登记系统中都已存在<br>");
						msg.setState(true);
					}
				} else {
					//不动产权证/证明号为查询条件，那么取的单元数据就是在不动产登记系统，总数就是toal
					if (toal > 0) {
						data = this.getDataBybdcqzmh(data, pageIndex, pageSize, paraMap, k_user, k_table_dy,
								createSQL.get("bdcqzmhSQL").toString(), row);
						data.put("toal", toal);
						data.put("auditOpinion", "初审通过<br>不动产权证/证明号在不动产登记系统中的单元都存在<br>");
						msg.setState(true);
					} else {
						msg.setState(false);
						data.put("toal", toal);
						data.put("auditOpinion", "初审不通过<br>企业的不动产权证/证明号在不动产登记系统中的单元不存在<br>");
					}
				} 
			}
		}
		msg.setData(data);
		return msg;
	}

	@Override
	public JsonMessage getPersonalAuditInfoData(int pageIndex, int pageSize, String id,String djlx,String qllx,String bdcqzmh) {
		JsonMessage msg = new JsonMessage();
		Map<String,Object> data = new HashMap<String, Object>();
		data.put("dyInfo", "");
		data.put("toal", 0);
		data.put("auditOpinion", "审核不通过！");
		msg.setState(false);
		String sqr = null;
		String zjlx = null;
		String zjh = null;
		String dlr = null;
		String dlrzjzl = null;
		String dlrzjh = null;
		if (!StringHelper.isEmpty(id)) {
			if((DJLX.CSDJ.Name.equals(djlx) && !QLLX.DIYQ.Name.equals(qllx)) 
					|| (DJLX.YGDJ.Name.equals(djlx) && QLLX.GYJSYDSHYQ_FWSYQ.Name.equals(qllx))) {
				msg.setMsg("个人暂时不能办理所有权/使用权业务!");
				//后期添加个人办理首次登记的审核方法
				data.put("dyInfo", "");
				data.put("toal", 0);
				data.put("auditOpinion", "审核不通过！个人暂时不能办理所有权/使用权业务!");
				msg.setData(data);
				msg.setState(false);
				msg = getCSDJtoNO_DIYA(id, djlx, qllx, msg,pageIndex, pageSize);
				return msg;
			}
			Map<String, String> xm_map = new HashMap<String, String>();
			xm_map.put("PROINST_ID", id);			
			List<Map> sqrList = baseCommonDaoInline.getDataListByFullSql("select * from INLINE_INNER.PRO_PROPOSERINFO "
					+ " where SQR_LX = '1' and  PROINST_ID = :PROINST_ID", xm_map);
//			List<Map> sqrList = baseCommonDaoInline.getDataListByFullSql("select * from INLINE_INNER.PRO_PROPOSERINFO "
//					+ " where SQR_LX = '1' and sqr_name is not null and sqr_zjh is not null "
//					+ " and sqr_zjlx is not null and PROINST_ID = :PROINST_ID", xm_map);
			StringBuffer sqrSQL = new StringBuffer();
			StringBuffer sqrSQL_list = new StringBuffer(" and (");
			Map<String, String> sqrMapnew = new HashMap<String, String>();
			int gs = 0;
			boolean sqr_not_ok = false;
			for (Map ww_sqr : sqrList) {
				gs++;
				if (ww_sqr.get("SQR_NAME") !=null && ww_sqr.get("SQR_NAME").toString() != "") {
					sqrSQL.append("  replace(c.qlrmc,' ','') = :sqr"+gs);
					sqrMapnew.put("sqr"+gs, ww_sqr.get("SQR_NAME").toString().replaceAll(" ", ""));
				}else {
					sqr_not_ok = true;
					break;
				}
				if (ww_sqr.get("SQR_ZJH") !=null && ww_sqr.get("SQR_ZJH").toString() != "") {
					sqrSQL.append(" and  replace(c.zjh,' ','') = :zjh"+gs);
					sqrMapnew.put("zjh"+gs, ww_sqr.get("SQR_ZJH").toString().replaceAll(" ", ""));
				}else {
					sqr_not_ok = true;
					break;
				}
//				if (ww_sqr.get("SQR_ZJLX") !=null && ww_sqr.get("SQR_ZJLX").toString() != "") {
//					sqrSQL.append("  and c.zjzl = :zjzl"+gs);
//					sqrMapnew.put("zjzl"+gs, ww_sqr.get("SQR_ZJLX").toString());
//				}else {
//					sqr_not_ok = true;
//					break;
//				}
//				if (ww_sqr.get("SQR_BDCQZMH") !=null && ww_sqr.get("SQR_BDCQZMH").toString() != "") {
//					sqrSQL.append("  and c.bdcqzh = :bdcqzh"+gs);
//					sqrMapnew.put("bdcqzh"+gs, ww_sqr.get("SQR_BDCQZMH").toString());
//				}else {
//					sqr_not_ok = true;
//					break;
//				}
				if(!StringHelper.isEmpty(bdcqzmh)) {
					sqrSQL.append("  and replace(c.bdcqzh,' ','') = :bdcqzh"+gs);
					sqrMapnew.put("bdcqzh"+gs, bdcqzmh.replaceAll(" ", ""));
				}
				if (gs>1) {
					sqrSQL_list.append(" or "+sqrSQL);
				} else {
					sqrSQL_list.append(sqrSQL);
				}
				sqrSQL.delete(0, sqrSQL.length());
			}
			sqrSQL_list.append(") ");
			
			if(sqr_not_ok) {
				msg.setState(false);
				data.put("auditOpinion", "审核不通过！申请人的义务人信息不完整。");
				data.put("dyInfo", "");
				data.put("toal", 0);
				data.put("bdcdylx", "YCH");
				msg.setData(data);
				return msg;
			}
//			if (sqrList != null && sqrList.size()>0) {
//				sqr = sqrList.get(0).get("SQR_NAME").toString();
//				zjlx = sqrList.get(0).get("SQR_ZJLX").toString();
//				zjh = sqrList.get(0).get("SQR_ZJH").toString();
//			}
			//首先判断bdcqzmh不是空值
//			if(StringHelper.isEmpty(bdcqzmh) || "null".equals(bdcqzmh) || StringHelper.isEmpty(zjh) || "null".equals(zjh)
//					|| StringHelper.isEmpty(zjlx) || "null".equals(zjlx) || StringHelper.isEmpty(sqr) || "null".equals(sqr)) {
//				msg.setState(false);
//				data.put("auditOpinion", "审核不通过！申请人的义务人信息不完整。");
//				data.put("dyInfo", "");
//				data.put("toal", 0);
//				msg.setData(data);
//				return msg;
//			}
//			Map<String, String> sqrMap = new HashMap<String, String>();
//			
//			sqrMap.put("bdcqzmh", bdcqzmh);
//			sqrMap.put("zjh", zjh);
//			sqrMap.put("zjlx", zjlx);
//			sqrMap.put("sqr", sqr);
			
//			long toal = commonDao.getCountByFullSql(" from  bdck.bdcs_qlr_xz where QLRMC = :sqr and zjzl = :zjlx "
//					+ "and zjh = :zjh and bdcqzh = :bdcqzmh", sqrMap);
			long toal = commonDao.getCountByFullSql(" from  bdck.bdcs_qlr_xz c where 1>0 "+sqrSQL_list, sqrMapnew);
			data.put("toal", toal);
			if (toal>0) {
//				List<Map> bdcdylxList = commonDao.getDataListByFullSql("select  a.bdcdylx, ListAgg(TO_CHAR(a.bdcdyid),',') "
//						+ "within group (order by a.bdcdylx) AS bdcdyids from bdck.bdcs_djdy_xz a, bdck.bdcs_ql_xz b,"
//						+ "bdck.bdcs_qlr_xz c  where a.djdyid = b.djdyid and b.qlid = c.qlid and a.bdcdylx is not null "
//						+ " and c.QLRMC = :sqr and c.zjzl = :zjlx and c.zjh = :zjh and c.bdcqzh = :bdcqzmh group by a.BDCDYLX", sqrMap);
				List<Map> bdcdylxList = commonDao.getDataListByFullSql("select distinct a.bdcdylx from bdck.bdcs_djdy_xz a, bdck.bdcs_ql_xz b,"
						+ "bdck.bdcs_qlr_xz c  where a.djdyid = b.djdyid and b.qlid = c.qlid "+sqrSQL_list+" group by a.BDCDYLX", sqrMapnew);
				if (bdcdylxList != null && bdcdylxList.size()>0) {
					String tableSQL =  null;
					String bdcdylx = bdcdylxList.get(0).get("BDCDYLX").toString();
					String k_user= " bdck.";
					String k_table_dy = "bdcs_h_xz ";
					if (BDCDYLX.SHYQZD.Value.equals(bdcdylx)) {
						tableSQL =  "select 'SHYQZD' as BDCDYLX, BDCDYID,BDCDYH,ZL,ZDMJ,ZDSZD,ZDSZX,ZDSZN,ZDSZB,"
								+ "'是' as SFTG , '#BBFFBB' as SFTGCOLOR FROM bdck.bdcs_shyqzd_xz ";
						data.put("bdcdylx", "SHYQZD");
						k_table_dy = "bdcs_shyqzd_xz ";
					} else if (BDCDYLX.SYQZD.Value.equals(bdcdylx)) {
						tableSQL =  "select 'SYQZD' as BDCDYLX,BDCDYID,BDCDYH,ZL,ZDMJ,ZDSZD,ZDSZX,ZDSZN,ZDSZB,"
								+ " '是' as SFTG , '#BBFFBB' as SFTGCOLOR FROM bdck.BDCS_SYQZD_XZ ";
						data.put("bdcdylx", "SYQZD");
						k_table_dy = "BDCS_SYQZD_XZ ";
					} else if (BDCDYLX.YCH.Value.equals(bdcdylx)) {
						tableSQL =  "select 'YCH' as BDCDYLX,BDCDYID,BDCDYH,ZL,DYH,ZCS,QSC,ZZC,SZC,YCJZMJ AS JZMJ ，"
								+ "'是' as SFTG , '#BBFFBB' as SFTGCOLOR from bdck.bdcs_h_xzy ";
						data.put("bdcdylx", "YCH");
						k_table_dy = "bdcs_h_xzy ";
					} else if (BDCDYLX.H.Value.equals(bdcdylx)) {
						tableSQL =  "select 'H' as BDCDYLX,BDCDYID,BDCDYH,ZL,DYH,ZCS,QSC,ZZC,SZC,SCJZMJ AS JZMJ， "
								+ "'是' as SFTG , '#BBFFBB' as SFTGCOLOR  from bdck.bdcs_h_xz ";
						data.put("bdcdylx", "H");
					} else if (BDCDYLX.LD.Value.equals(bdcdylx)) {
						tableSQL =  "select 'LD' as BDCDYLX,BDCDYID,BDCDYH,ZL ，'是' as SFTG , '#BBFFBB' as SFTGCOLOR "
								+ " from bdck.BDCS_SLLM_XZ ";
						k_table_dy = "BDCS_SLLM_XZ ";
						data.put("bdcdylx", "SHYQZD");
					}
//					String sql = tableSQL+" where bdcdyid in "
//							+ "(select distinct a.bdcdyid from bdck.bdcs_djdy_xz a, bdck.bdcs_ql_xz b,"
//							+ "bdck.bdcs_qlr_xz c  where a.djdyid = b.djdyid and b.qlid = c.qlid and a.bdcdylx is not null"
//							+ " and c.QLRMC = :sqr and c.zjzl = :zjlx and c.zjh = :zjh and c.bdcqzh = :bdcqzmh)";
					String sql = tableSQL+" where bdcdyid in "
							+ "(select distinct a.bdcdyid from bdck.bdcs_djdy_xz a, bdck.bdcs_ql_xz b,"
							+ "bdck.bdcs_qlr_xz c  where a.djdyid = b.djdyid and b.qlid = c.qlid "+sqrSQL_list+")";
					
					
					
//					List<Map> dyInfo = commonDao.getDataListByFullSql(sql, sqrMap,pageIndex,pageSize);
					List<Map> dyInfo = commonDao.getDataListByFullSql(sql, sqrMapnew,pageIndex,pageSize);
					
					if (dyInfo != null && dyInfo.size()>0) {
						msg.setState(true);
						getDYZT(dyInfo, k_user, k_table_dy, null, false);
						data.put("dyInfo", dyInfo);
//						List<Map> qlList = commonDao.getDataListByFullSql("select ql,count(1) as qlgs from (select (case "
//								+ " when djlx = 800 and qllx = '99' then '存在查封'"
//								+ " when djlx = 600 and qllx = '99' then '存在异议' "
//								+ " when  qllx = '23' then '存在抵押'else "
//								+ " '存在：'||(SELECT to_char(CONSTTRANS)  FROM bdck.BDCS_CONST "
//								+ " WHERE CONSTSLSID = 8 AND CONSTVALUE = a.QLLX ) end) as ql  "
//								+ " from bdck.bdcs_ql_xz a where djdyid in "
//								+ " (select distinct b.djdyid from bdck.bdcs_ql_xz b,bdck.bdcs_qlr_xz c "
//								+ " where  b.qlid = c.qlid and c.QLRMC = :sqr and c.zjzl = :zjlx "
//								+ " and c.zjh = :zjh and c.bdcqzh = :bdcqzmh)) group by ql" , sqrMap);
						List<Map> qlList = commonDao.getDataListByFullSql("select ql,count(1) as qlgs from (select (case "
								+ " when djlx = 800 and qllx = '99' then '存在查封'"
								+ " when djlx = 600 and qllx = '99' then '存在异议' "
								+ " when  qllx = '23' then '存在抵押'else "
								+ " '存在：'||(SELECT to_char(CONSTTRANS)  FROM bdck.BDCS_CONST "
								+ " WHERE CONSTSLSID = 8 AND CONSTVALUE = a.QLLX ) end) as ql  "
								+ " from bdck.bdcs_ql_xz a where djdyid in "
								+ " (select distinct b.djdyid from bdck.bdcs_ql_xz b,bdck.bdcs_qlr_xz c "
								+ " where  b.qlid = c.qlid "+sqrSQL_list+")) group by ql" , sqrMapnew);
						if (qlList != null && qlList.size()>0) {
							StringBuffer auditOpinion = new StringBuffer("初审通过<br>该申请信息查询到的权利信息：<br>");
							for (Map map : qlList) {
								String czql = map.get("QL").toString();
								String czqlgs = map.get("QLGS").toString();
								auditOpinion.append(czql).append("有").append(czqlgs).append("个;<br>");
							}
							data.put("auditOpinion", auditOpinion);
						}
						
						//this.getRTCONSTRAINT(id);
					}
				}
			}else {
				msg.setState(false);
				data.put("dyInfo", "");
				data.put("toal", 0);
				data.put("auditOpinion", "审核不通过！不动产登记系统没有找到单元。");
			}
			msg.setData(data);
		}
		
		
		return msg;
	}
	
	public JsonMessage getPersonalAuditInfoData(int pageIndex, int pageSize, String id,String bdcqzmh){
		JsonMessage msg = new JsonMessage();
		List<Map> dyInfoList = new ArrayList<Map>();
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("dyInfo", "");
		data.put("bdcdyids", "");
		data.put("auditOpinion", "");
		data.put("toal", "0");
		if (!StringHelper.isEmpty(id)) {
			Map<String, String> paraMap = new HashMap<String, String>();
			Map<String, Object> createSQL = new HashMap<String, Object>();
			paraMap.put("ID", id);
			String k_user = " bdck.";
			//只能写不动产登记系统中的单元表
			String k_table_dy = "bdcs_h_xz   ";
			String row = " ID,BDCDYH,ZL,DYH,FWSYQMJ ";
			boolean bdcdyhSQL_b = false;
			boolean bdcqzmhSQL_b = false;
			//判断业务类型，确定去不动产登记系统哪个库中找数据。
			Map map = getRtconstraint(id);
			if(map != null) {
				String bdcdylx = map.get("BDCDYLX").toString();
				String ly = map.get("LY").toString();
				data.put("bdcdylx", ConstValue.BDCDYLX.initFrom(bdcdylx).toString());
				
				//确定初审的对比表
				if(ConstValue.DJDYLY.DC.Value.equals(ly)) {
					k_user = " bdcdck.";
					k_table_dy = "B"+ConstValue.BDCDYLX.initFrom(bdcdylx).XZTableName;
				} else if(ConstValue.DJDYLY.XZ.Value.equals(ly)) {
					k_table_dy = ConstValue.BDCDYLX.initFrom(bdcdylx).XZTableName;
				} else if(ConstValue.DJDYLY.GZ.Value.equals(ly)) {
					k_table_dy = ConstValue.BDCDYLX.initFrom(bdcdylx).GZTableName;
				} else if(ConstValue.DJDYLY.LS.Value.equals(ly)) {
					k_table_dy = ConstValue.BDCDYLX.initFrom(bdcdylx).LSTableName;
				}
				

				//PRO_FWXX表保存的是不动产权证/证明号的时候，用来确定返回的字段
				if (ConstValue.BDCDYLX.initFrom(bdcdylx).equals(ConstValue.BDCDYLX.H)) {
					row = " 'H' as BDCDYLX,BDCDYID,BDCDYH,ZL,DYH,ZCS,QSC,ZZC,SZC,SCJZMJ AS JZMJ ";
				} else if (ConstValue.BDCDYLX.initFrom(bdcdylx).equals(ConstValue.BDCDYLX.YCH)) {
					row = " 'YCH' as BDCDYLX,BDCDYID,BDCDYH,ZL,DYH,ZCS,QSC,ZZC,SZC,YCJZMJ AS JZMJ ";
				} else if (ConstValue.BDCDYLX.initFrom(bdcdylx).equals(ConstValue.BDCDYLX.SHYQZD)) {
					row = " 'SHYQZD' as BDCDYLX, BDCDYID,BDCDYH,ZL,ZDMJ,ZDSZD,ZDSZX,ZDSZN,ZDSZB ";
				} else if (ConstValue.BDCDYLX.initFrom(bdcdylx).equals(ConstValue.BDCDYLX.SYQZD)) {
					row = " 'SHYQZD' as BDCDYLX, BDCDYID,BDCDYH,ZL,ZDMJ,ZDSZD,ZDSZX,ZDSZN,ZDSZB ";
				} else if (ConstValue.BDCDYLX.initFrom(bdcdylx).equals(ConstValue.BDCDYLX.LD)) {
					row = " 'SHYQZD' as BDCDYLX, BDCDYID,BDCDYH,ZL,ZDMJ,ZDSZD,ZDSZX,ZDSZN,ZDSZB ";
				} 
			}else {
				data.put("auditOpinion", "没有找到对应的流程！");
				msg.setState(false);
				msg.setData(data);
				return msg;
			}
			//确定外网流程录入的单元数据表
			createSQL = this.createSQL(id, " BDCDYH,BDCQZMH ", "PRO_FWXX", bdcdyhSQL_b, bdcqzmhSQL_b);
			
			
			if (createSQL != null) {
				bdcdyhSQL_b = (Boolean) createSQL.get("bdcdyh_b");
				bdcqzmhSQL_b = (Boolean) createSQL.get("bdcqzmh_b");
			}
			long toal_dy = 0;
			if(bdcdyhSQL_b && bdcqzmhSQL_b) {
				toal_dy = commonDao.getCountByFullSqlCustom(" from "+k_user+k_table_dy
						+ " where ("+createSQL.get("bdcdyhSQL")+") "
								+ "and bdcdyid in (select a.bdcdyid from bdck.bdcs_djdy_xz a,bdck.bdcs_ql_xz b"
								+ " where a.djdyid = b.djdyid and instr(b.bdcqzh,'"+createSQL.get("bdcqzmhSQL")+"')>0)");
			} else if(bdcdyhSQL_b) {
				toal_dy = commonDao.getCountByFullSqlCustom(" from "+k_user+k_table_dy
						+" where "+createSQL.get("bdcdyhSQL"));
			} else if(bdcqzmhSQL_b) {
				toal_dy = commonDao.getCountByFullSqlCustom(" from "+k_user+k_table_dy
						+ " where bdcdyid in (select a.bdcdyid from bdck.bdcs_djdy_xz a,bdck.bdcs_ql_xz b"
						+ " where a.djdyid = b.djdyid and instr(b.bdcqzh,'"+createSQL.get("bdcqzmhSQL")+"')>0)");
				
			}
			
			
			if(toal_dy > 0) {
				
			}
		}
		
		return msg;
		
	}

	@Override
	public JsonMessage getQlInfo(String bdcdyid) {
		JsonMessage msg = new JsonMessage();
		msg.setState(false);
		msg.setMsg("该单元没有找到对应的有效权利信息。");
		List<Map> qlInfo = commonDao.getDataListByFullSql("select ListAgg(TO_CHAR(c.qlrmc),',') within group (order by c.qlid) AS  qlrmc,"
				+ " ListAgg(TO_CHAR(c.zjh),',') within group (order by c.qlid) AS zjh,"
				+ "(SELECT CONSTTRANS  FROM bdck.BDCS_CONST WHERE CONSTSLSID = 21 AND CONSTVALUE = b.DJLX) as DJLX,"
				+ "(SELECT CONSTTRANS  FROM bdck.BDCS_CONST WHERE CONSTSLSID = 8 AND CONSTVALUE = b.QLLX) as qllx,"
				+ "b.BDCQZH,to_char(b.qlqssj,'yyyy-mm-dd') as qlqssj,to_char(b.qljssj,'yyyy-mm-dd') as qljssj,"
				+ "to_char(b.djsj,'yyyy-mm-dd') as djsj,(case when b.czfs = '01' then '共同持证' "
				+ "when b.czfs= '02' then '分别持证' else null end) as CZFS ,b.fj  "
				+ "from bdck.BDCS_DJDY_XZ a, bdck.BDCS_QL_XZ b, bdck.BDCS_QLR_XZ c "
				+ "where a.DJDYID = b.DJDYID and b.QLID = c.QLID and a.BDCDYID = '"+bdcdyid+"' "
						+ "group by b.djlx,b.qllx,b.qlqssj,b.qljssj,b.djsj,b.BDCQZH,b.fj,b.czfs ");
		if (qlInfo != null && qlInfo.size()>0) {
			msg.setData(qlInfo);
			msg.setState(true);
		}
		return msg;
	}
	
	/**
	 * 获取不动产登记系统中的流程名称
	 * @param id
	 * @return
	 */
	private Map getRtconstraint(String id) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("id", id);
		List<Map> prodef_idMap = baseCommonDaoInline.getDataListByFullSql("select PRODEFCODE as PRODEFID "
				+ "from  INLINE_INNER.PRO_PROINST where id = :id and PRODEFCODE is not null", map);
		if (prodef_idMap != null && prodef_idMap.size() >0) {
			List<Map> maplist = commonDao.getDataListByFullSql("SELECT A.BDCDYLX, A.LY, C.WORKFLOWCODE, C.WORKFLOWNAME  "
					+ " FROM BDCK.T_SELECTOR A, BDCK.T_BASEWORKFLOW B, BDC_WORKFLOW.WFD_MAPPING C, "
					+ " BDC_WORKFLOW.WFD_PRODEF D WHERE A.ID = B.SELECTORID AND B.ID = C.WORKFLOWNAME "
					+ " AND C.WORKFLOWCODE = D.PRODEF_CODE AND D.PRODEF_ID  ='" + prodef_idMap.get(0).get("PRODEFID") + "'");
			if (maplist != null && maplist.size()>0) {
				return maplist.get(0);
			}
		}
		return null;
	}
	
	
	/**
	 * 获取对比单元个数的初审查询条件
	 * @param id
	 * @param tj
	 * @param bdcdyh_b
	 * @param bdcqzmh_b
	 * @return
	 */
	private Map<String, Object> createSQL (String id,String tj,String table,boolean bdcdyh_b,boolean bdcqzmh_b) {
		Map<String, Object> sql_map = new HashMap<String, Object>();
		if (!StringHelper.isEmpty(id)) {
			Map<String, String> paraMap = new HashMap<String, String>();
			paraMap.put("ID", id);
			List<Map> bdcdyhMap;
			bdcdyhMap = baseCommonDaoInline.getDataListByFullSql(
					"select " + tj + " FROM INLINE_INNER." + table + " WHERE PROINST_ID = :ID ", paraMap);
			
			if (bdcdyhMap != null && bdcdyhMap.size()>0) {
				//判断都存在什么数据
				StringBuffer bdcdyhSQL = new StringBuffer();
				StringBuffer bdcdyhor  = new StringBuffer();
				int bdcdyhSQL_lenght = 0;
				sql_map.put("dytoal", bdcdyhMap.size());
				boolean bdcqzmh_two = true;
				boolean bdcdyh_two = true;
				for (int i = 0; i < bdcdyhMap.size(); i++) {
					//判断填写条件是否存在不动产权证/证明号
					if (bdcqzmh_two) {
						if (!StringHelper.isEmpty(bdcdyhMap.get(i).get("BDCQZMH"))) {
							bdcqzmh_b = true;
							bdcqzmh_two = false;
							String bdcqzmh = bdcdyhMap.get(i).get("BDCQZMH").toString().replaceAll(" ", "");
							bdcqzmh = bdcqzmh.replaceAll("（", "(");
							bdcqzmh =bdcqzmh.replaceAll("）", ")");
							sql_map.put("bdcqzmhSQL", bdcqzmh);
						} 
					}
//					if(bdcqzmh_b && bdcqzmh_two) {
//						if(bdcdyhMap.get(i).get("BDCQZMH") != null && bdcdyhMap.get(i).get("BDCQZMH") != "") {
//							String bdcqzmh = bdcdyhMap.get(i).get("BDCQZMH").toString().replaceAll(" ", "");
//							bdcqzmh.replaceAll("（", "(");
//							bdcqzmh.replaceAll("）", ")");
//							sql_map.put("bdcqzmhSQL", bdcqzmh);
//							bdcqzmh_two =  false;
//						}
//					}
					
					
					
					if (!StringHelper.isEmpty(bdcdyhMap.get(i).get("BDCDYH"))) {
						bdcdyh_b = true;
						String bdcdyh = bdcdyhMap.get(i).get("BDCDYH").toString().replaceAll(" ", "");
						if (bdcdyhSQL.toString().contains(bdcdyh)) {
							continue;
						}
						if (bdcdyhor.length() == 0) {
							if (bdcdyhSQL.length() == 0) {
								bdcdyhSQL.append(" bdcdyh = '" + bdcdyh + "' ");
							} else {
								bdcdyhor.append("'" + bdcdyh + "'");
								
							}
						} else {
							bdcdyhor.append(",'" + bdcdyh + "'");
						}
						bdcdyhSQL_lenght++;
						if (bdcdyhSQL_lenght > 900) {
							bdcdyhSQL.append(" or bdcdyh in (" + bdcdyhor + ")");
							bdcdyhor.delete(0, bdcdyhor.length());
							bdcdyhSQL_lenght = 0;
						}
					}


					//交易系统调用接口创建的项目，目前都是预告类业务，传入的是chid，也就是bdcdyid,转换成bdcdyh
					if(!StringHelper.isEmpty(bdcdyhMap.get(i).get("CHID"))) {
						String bdcdyid = bdcdyhMap.get(i).get("CHID").toString().replaceAll(" ", "");
						List<Map> bdcdyList = commonDao.getDataListByFullSql("select bdcdyh from bdck.bdcs_h_xzy t where t.bdcdyid='"+bdcdyid+"'" );
						if(bdcdyList.size()>0) {
							bdcdyh_b = true;
							String bdcdyh = StringHelper.formatObject(bdcdyList.get(0).get("BDCDYH"));
							if (bdcdyhSQL.toString().contains(bdcdyh)) {
								continue;
							}
							if (bdcdyhor.length() == 0) {
								if (bdcdyhSQL.length() == 0) {
									bdcdyhSQL.append(" bdcdyh = '" + bdcdyh + "' ");
								} else {
									bdcdyhor.append("'" + bdcdyh + "'");

								}
							} else {
								bdcdyhor.append(",'" + bdcdyh + "'");
							}
							bdcdyhSQL_lenght++;
							if (bdcdyhSQL_lenght > 900) {
								bdcdyhSQL.append(" or bdcdyh in (" + bdcdyhor + ")");
								bdcdyhor.delete(0, bdcdyhor.length());
								bdcdyhSQL_lenght = 0;
							}
						}
					}
				}
				
				if (bdcdyh_b) {
					if (!StringHelper.isEmpty(bdcdyhor.toString())) {
						//防止最后只有一个只有一个bdcdyh用in 会出现问题
						if (bdcdyhor.toString().contains(",")) {
							bdcdyhSQL.append(" or bdcdyh in (" + bdcdyhor + ")");
						} else {
							bdcdyhSQL.append(" or bdcdyh = " + bdcdyhor);
						} 
					}
					sql_map.put("bdcdyhSQL", bdcdyhSQL);
				}
				
				sql_map.put("bdcqzmh_b", bdcqzmh_b);
				sql_map.put("bdcdyh_b", bdcdyh_b);
				return sql_map;
			}else {
				return null;
			}
		}
		return null;
		
	}
	
	/**
	 * 查询不动产登记系统存在多少条，外网条件的对应数据。
	 * @param bdcdyhSQL_b 是否用bdcdyh 当查询条件
	 * @param bdcqzmhSQL_b 是否用bdcqzmh 当查询条件
	 * @param paraeMap 查询条件
	 * @param k_user 取数据的数据库用户
	 * @param k_table 取数据的数据库表
	 * @return
	 */
	private long getDYtoGS (boolean bdcdyhSQL_b,boolean bdcqzmhSQL_b, Map<String, Object> paraeMap,String k_user,String k_table){
		if (bdcqzmhSQL_b && bdcdyhSQL_b) {
			return commonDao.getCountByFullSql(" from (select distinct h.BDCDYID "
					+ " from "+k_user+k_table+" h , bdck.BDCS_DJDY_XZ djdy, bdck.BDCS_QL_XZ ql "
					+ " where h.BDCDYID = djdy.BDCDYID and djdy.DJDYID = ql.DJDYID "
					+ " and h.bdcdyid in (select bdcdyid from "+k_user+k_table
					+ " where "+paraeMap.get("bdcdyhSQL").toString()+") "
					+ " and instr(ql.BDCQZH,'"+paraeMap.get("bdcqzmhSQL").toString()+"')>0)");
		} else if(bdcqzmhSQL_b) {
			return commonDao.getCountByFullSql(" from (select distinct h.BDCDYID "
					+ " from "+k_user+k_table+" h , bdck.BDCS_DJDY_XZ djdy, bdck.BDCS_QL_XZ ql "
					+ " where h.BDCDYID = djdy.BDCDYID and djdy.DJDYID = ql.DJDYID "
					+ " and instr(ql.BDCQZH,'"+paraeMap.get("bdcqzmhSQL").toString()+"')>0)");
		} else if(bdcdyhSQL_b) {
			return commonDao.getCountByFullSql(" from "+k_user+k_table+" where "+paraeMap.get("bdcdyhSQL"));
		}else {
			return 0;
		}
		
	}
	
	
	/**
	 * 批量受理业务，需要展示的外网单元数据
	 * @param data
	 * @param pageIndex
	 * @param pageSize
	 * @param paraMap
	 * @param k_user
	 * @param k_table_dy
	 * @param bdcdyhSQL
	 * @return
	 */
	private Map<String,Object> getDataBybdcdyh(Map<String,Object> data,int pageIndex, int pageSize,
			Map <String,String> paraMap,String k_user,String k_table_dy,String bdcdyhSQL) {
		List<Map> dyInfo = baseCommonDaoInline.getDataListByFullSql("select id,BDCDYH,FWBM,zl,FWSYQMJ, SFTG,SFTGCOLOR  FROM "
				+ "(select ROWNUM rn ,id,BDCDYH,FWBM,zl,FWSYQMJ,'是' as SFTG , '#BBFFBB' as SFTGCOLOR "
				+ "  FROM INLINE_INNER.PRO_FWXX  WHERE PROINST_ID = :ID " 
				+ " and ROWNUM < "+ (pageIndex * pageSize + 1) + ") WHERE RN >" + (pageIndex - 1) * pageSize, paraMap);
		String bdcdyids = "";
		List<Map> bdcdyList = commonDao.getDataListByFullSql("select bdcdyh from "+k_user+k_table_dy+" where "+bdcdyhSQL);
		String bdcdyhList = "";
		for (Map map : bdcdyList) {
			bdcdyhList += map.get("BDCDYH")+",";
		}
		dyInfo = getDYZT(dyInfo,k_user,k_table_dy,bdcdyhList,true);
		data.put("dyInfo", dyInfo);
		return data;
	}
	
	
	/**
	 * 根据条件查询到的单元是否存在抵押、查封、异议状态，条件的单元在不动产登记系统是否存在单元数据。
	 * @param data
	 * @param pageIndex
	 * @param pageSize
	 * @param paraMap
	 * @param k_user
	 * @param k_table_dy
	 * @param bdcqzmhSQL
	 * @param row
	 * @return
	 */
	private Map<String,Object> getDataBybdcqzmh(Map<String,Object> data,int pageIndex, int pageSize,
			Map <String,String> paraMap,String k_user,String k_table_dy,String bdcqzmhSQL,String row) {
		
		List<Map> dyInfo = commonDao.getDataListByFullSql("select "+row
				+ ", '是' as SFZG ,'#BBFFBB' as SFTGCOLOR from "+k_user+k_table_dy+" h , bdck.BDCS_DJDY_XZ djdy, bdck.BDCS_QL_XZ ql "
				+ " where h.BDCDYID = djdy.BDCDYID and djdy.DJDYID = ql.DJDYID "
				+ " and instr(ql.BDCQZH,'"+bdcqzmhSQL+"')>0", pageIndex,pageSize);
		dyInfo = getDYZT(dyInfo,k_user,k_table_dy,null,false);
		data.put("dyInfo", dyInfo);
		return data;
	}
	
	private List<Map> getDYZT(List<Map> dyInfo,String k_user,String k_table_dy,String bdcdyhList,boolean sfpd) {
		String bdcdyh = null;
		for (Map dymap : dyInfo) {
			bdcdyh = StringHelper.formatObject(dymap.get("BDCDYH"));
			if (!StringHelper.isEmpty(bdcdyh)) {
				List<Map> dyzt = commonDao.getDataListByFullSql("SELECT "
						+ " (CASE WHEN C.djlx = 800 and C.qllx = '99' then '是' else '否' end) as CFZT,"
						+ " (CASE WHEN C.djlx = 600 and C.qllx = '99' then '是' else '否' end) as YYZT,"
						+ " (CASE WHEN  C.qllx = '23' then '是' else '否' end) as DYZT,"
						+ " A.BDCDYH FROM "+k_user+k_table_dy+" A, BDCK.BDCS_DJDY_XZ B, BDCK.BDCS_QL_XZ C "
						+ " WHERE A.BDCDYID = B.BDCDYID AND B.DJDYID = C.DJDYID  "
						+ " AND A.BDCDYH = '"+bdcdyh+"'"
						+ " GROUP BY A.BDCDYH,C.DJLX,C.QLLX");
				if (dyzt != null && dyzt.size()>0) {
					dymap.putAll(dyzt.get(0));
				} else {
					dymap.put("DYZT", "否");
					dymap.put("CFZT", "否");
					dymap.put("YYZT", "否");
				}
				if (sfpd) {
					if (!bdcdyhList.contains(bdcdyh)) {
						dymap.put("SFTGCOLOR", "#FFD2D2");
						dymap.put("SFTG", "否");
					}
				}
			}
		}
		return dyInfo;
	}

	@Override
	public JsonMessage getsqrInfo(String proinst_id) {
		JsonMessage msg = new JsonMessage();
		Map<String,Object> data = new HashMap<String, Object>();
		Map<String,String> paramMap = new HashMap<String, String>();
		paramMap.put("proinst_id", proinst_id);
//		List<Map> sqrList = baseCommonDaoInline.getDataListByFullSql("select "
//				+ " (SQR_NAME || SQR_QT_FR_NAME || SQR_QT_DLR_NAME) as sqrname,"
//				+ " (SQR_ZJH || SQR_QT_ZJH) as sqrzjh,(SQR_TEL || SQR_QT_TEL) as sqrtel,"
//				+ " SQR_LX from INLINE_INNER.PRO_PROPOSERINFO "
//				+ " where proinst_id = :proinst_id", paramMap);
		List<Map> sqrList = baseCommonDaoInline.getDataListByFullSql("select "
				+ " SQR_NAME  as sqrname,"
				+ " SQR_ZJH  as sqrzjh, SQR_TEL  as sqrtel,"
				+ " SQR_LX from INLINE_INNER.PRO_PROPOSERINFO "
				+ " where proinst_id = :proinst_id and (sfcqr is null or sfcqr='1')", paramMap);
		if(sqrList != null && sqrList.size()>0) {
			Map<String,String> ywr = new HashMap<String,String>();
			Map<String,String> qlr = new HashMap<String,String>();
			List<Map> bdcqzmhMap = baseCommonDaoInline.getDataListByFullSql("select bdcqzmh "
					+ "from INLINE_INNER.PRO_FWXX where proinst_id= :proinst_id and bdcqzmh is not null ",paramMap);
			String bdcqzmh = "";
			if(bdcqzmhMap != null && bdcqzmhMap.size() > 0 && !StringHelper.isEmpty(bdcqzmhMap.get(0).get("BDCQZMH"))) {
				for (Map map : bdcqzmhMap) {
					bdcqzmh += map.get("BDCQZMH").toString() +"、";
				}
				bdcqzmh = bdcqzmh.substring(0, bdcqzmh.length()-1);
			}
			for (Map sqr : sqrList) {
				if ("1".equals(sqr.get("SQR_LX").toString())) {
					//义务人名称
					if(ywr.get("sqr_ywr") != null) {
						ywr.put("sqr_ywr", sqr.get("SQRNAME") != null?
								ywr.get("sqr_ywr").toString() + "," + sqr.get("SQRNAME").toString():ywr.get("sqr_ywr"));
					}else {
						ywr.put("sqr_ywr", sqr.get("SQRNAME") != null?sqr.get("SQRNAME").toString():null);
					}
					//义务人证件号
					if(ywr.get("sqr_ywr_zjh") != null) {
						ywr.put("sqr_ywr_zjh", sqr.get("SQRZJH") != null?
								ywr.get("sqr_ywr_zjh").toString() + "," + sqr.get("SQRZJH").toString():ywr.get("sqr_ywr_zjh"));
					}else {
						ywr.put("sqr_ywr_zjh", sqr.get("SQRZJH") != null?sqr.get("SQRZJH").toString():null);
					}
				} else {
					//权利人名称
					if(qlr.get("sqr_qlr") !=null) {
						qlr.put("sqr_qlr", sqr.get("SQRNAME") != null?
								qlr.get("sqr_qlr").toString()  + "," + sqr.get("SQRNAME").toString():qlr.get("sqr_qlr"));
					}else {
						qlr.put("sqr_qlr", sqr.get("SQRNAME") != null?sqr.get("SQRNAME").toString():null);
					}
					//权利人证件号
					if(qlr.get("sqr_qlr_zjh") !=null) {
						qlr.put("sqr_qlr_zjh", sqr.get("SQRZJH") != null?
								qlr.get("sqr_qlr_zjh").toString()  + "," + sqr.get("SQRZJH").toString():qlr.get("sqr_qlr_zjh"));
					}else {
						qlr.put("sqr_qlr_zjh", sqr.get("SQRZJH") != null?sqr.get("SQRZJH").toString():null);
					}
					//权利人联系电话
					if(qlr.get("sqr_qlr_lxdh") !=null) {
						qlr.put("sqr_qlr_lxdh", sqr.get("SQRTEL") != null?
								qlr.get("sqr_qlr_lxdh").toString()  + "," + sqr.get("SQRTEL").toString():qlr.get("sqr_qlr_lxdh"));
					}else {
						qlr.put("sqr_qlr_lxdh", sqr.get("SQRTEL") != null?sqr.get("SQRTEL").toString():null);
					}
				}
			}
			if(ywr.size() >0 ) {
				if (!StringHelper.isEmpty(bdcqzmh)) {
					ywr.put("sqr_ywr_bdcqzmh", bdcqzmh);
				}
				data.put("ywr", ywr);
			}else {
				if (!StringHelper.isEmpty(bdcqzmh)) {
					qlr.put("sqr_qlr_bdcqzmh", bdcqzmh);
				}
				
			}
			if(qlr.size() >0 ) {
				data.put("qlr", qlr);
			}
			msg.setData(data);
			msg.setState(true);
			
		}else {
			msg.setState(false);
		}
		return msg;
	}

	
	
	@Override
	public JsonMessage getdyInfo(String bdcdyid, String bdcdylx,String djlx,String qllx) {
		JsonMessage msg = new JsonMessage();
		String dy_table = "bdcs_h_xz ";
		String dy_user = "bdck.";
		if (DJLX.CSDJ.Name.equals(djlx) && !QLLX.DIYQ.Name.equals(qllx)) {
			if ("H".equals(bdcdylx)) {
				dy_table = "bdcs_h_xz ";
			} else if ("YCH".equals(bdcdylx)) {
				dy_table = "bdcs_h_xzy ";
			} else if ("SHYQZD".equals(bdcdylx)) {
				dy_table = "bdcs_SHYQZD_xz ";

			} else if ("SYQZD".equals(bdcdylx)) {
				dy_table = "bdcs_SYQZD_xz ";
			} else if ("LD".equals(bdcdylx)) {
				dy_table = "bdcs_sllm_xz ";
			} 
		}else {
			if ("H".equals(bdcdylx)) {
				dy_user = "  bdcdck.";
				dy_table = "bdcs_h_gz ";
			} else if ("YCH".equals(bdcdylx)) {
				dy_table = "bdcs_h_xzy ";
			} else if ("SHYQZD".equals(bdcdylx)) {
				dy_user = "  bdcdck.";
				dy_table = "bdcs_SHYQZD_GZ ";
			} else if ("SYQZD".equals(bdcdylx)) {
				dy_user = "  bdcdck.";
				dy_table = "bdcs_SYQZD_GZ ";
			} else if ("LD".equals(bdcdylx)) {
				dy_user = "  bdcdck.";
				dy_table = "bdcs_sllm_GZ ";
			}
			
			List<Map> dyMap = commonDao.getDataListByFullSql("select * from "
			+dy_user+dy_table+" where bdcdyid = '"+bdcdyid+"'");
			if (dyMap != null && dyMap.size() >0) {
				msg.setState(true);
				msg.setData(dyMap);
			}else {
				msg.setState(false);
				msg.setMsg("未找到单元信息！");
			}
		}
		return msg;
	}
	
	/**
	 * 获取首次登记，非抵押的业务。
	 * 注意：单元记录是存在bdcdck中的
	 * @param id
	 * @return
	 */
	private JsonMessage getCSDJtoNO_DIYA(String id,String djlxname, String qllxName,
			JsonMessage msg,int pageIndex,int pageSize) {
		Map<String, String> paramMap = new HashMap<String, String>();
		Map<String, Object> data = new HashMap<String, Object>();
		paramMap.put("id", id);
		StringBuffer bdcdyhSQL = new StringBuffer();
		StringBuffer bdcdyhor  = new StringBuffer();
		int bdcdyhSQL_lenght = 0;
		List<Map> dyinfo = baseCommonDaoInline.getDataListByFullSql("select EXTEND_DATA "
				+ "from INLINE_INNER.PRO_PROINST where EXTEND_DATA is not null and  id = :id", paramMap);
		if(dyinfo != null && dyinfo.size() >0 ) {
			List<Map> ww_dyList = new ArrayList();
			String extend_data = dyinfo.get(0).get("EXTEND_DATA").toString();
			JSONArray dyArray;
			try {
				JSONObject jsonExtend = JSON.parseObject(extend_data);
				dyArray = JSONArray.fromObject(jsonExtend.getString("bdcdys"));
				if (dyArray != null && dyArray.size() > 0) {
					for (int i = 0; i < dyArray.size(); i++) {
						Map<String,Object> dyMap = new HashMap<String, Object>();
						String bdcdyh = dyArray.getJSONObject(0).getString("bdcdyh");
						String bdczl = dyArray.getJSONObject(0).getString("bdczl");
						dyMap.put("BDCDYH", bdcdyh);
						dyMap.put("ZL", bdczl);
						ww_dyList.add(dyMap);
						if(bdcdyhSQL.toString().contains(bdcdyh)) {
							continue;
						}
						if (bdcdyhor.length() == 0) {
							if (bdcdyhSQL.length() == 0) {
								bdcdyhSQL.append(" bdcdyh = '" + bdcdyh + "' ");
							} else {
								bdcdyhor.append("'" + bdcdyh + "'");

							}
						} else {
							bdcdyhor.append(",'" + bdcdyh + "'");
						}
						bdcdyhSQL_lenght++;
						if (bdcdyhSQL_lenght > 900) {
							bdcdyhSQL.append(" or bdcdyh in (" + bdcdyhor + ")");
							bdcdyhor.delete(0, bdcdyhor.length());
							bdcdyhSQL_lenght = 0;
						}
					}
					if (!StringHelper.isEmpty(bdcdyhor.toString())) {
						if (bdcdyhor.toString().contains(",")) {
							bdcdyhSQL.append(" or bdcdyh in (" + bdcdyhor + ")");
						} else {
							bdcdyhSQL.append(" or bdcdyh = " + bdcdyhor);
						}
					}
				} else {
					data.put("toal", 0);
					data.put("auditOpinion", "审核不通过！外网录入的单元信息不完整！");
					msg.setData(data);
					msg.setState(false);
					return msg;
				} 
			} catch (Exception e) {
				e.printStackTrace();
				data.put("toal", 0);
				data.put("auditOpinion", "审核不通过！外网录入的单元信息不完整！");
				msg.setData(data);
				msg.setState(false);
				return msg;
				// TODO: handle exception
			}
			String selectSQL = "";
			String formSQL = " bdcdck.bdcs_shyqzd_gz ";
			
			if (QLLX.JTTDSYQ.Name.equals(qllxName) 
					|| QLLX.GYJSYDSHYQ.Name.equals(qllxName) || QLLX.ZJDSYQ.Name.equals(qllxName)
					|| QLLX.JTJSYDSYQ.Name.equals(qllxName) || QLLX.TDCBJYQ.Name.equals(qllxName)) {
				formSQL = " bdcdck.bdcs_shyqzd_gz ";
				selectSQL =  "select 'SHYQZD' as BDCDYLX, BDCDYID,BDCDYH,ZL,ZDMJ,ZDSZD,ZDSZX,ZDSZN,ZDSZB, "
						+ " '是' as SFTG , '#BBFFBB' as SFTGCOLOR FROM ";
				data.put("bdcdylx", "SHYQZD");
			} else if (QLLX.GYJSYDSHYQ_FWSYQ.Name.equals(qllxName) || QLLX.JTJSYDSYQ_FWSYQ.Name.equals(qllxName) 
					|| QLLX.ZJDSYQ_FWSYQ.Name.equals(qllxName)) {
				selectSQL =  "select 'H' as BDCDYLX,BDCDYID,BDCDYH,ZL,DYH,ZCS,QSC,ZZC,SZC,SCJZMJ AS JZMJ,"
						+ " '是' as SFTG , '#BBFFBB' as SFTGCOLOR from  ";
				formSQL = " bdcdck.bdcs_h_gz ";
				data.put("bdcdylx", "H");
			} else if (QLLX.TDCBJYQ_SLLMSYQ.Name.equals(qllxName) || QLLX.LDSYQ.Name.equals(qllxName) 
					|| QLLX.LDSYQ_SLLMSYQ.Name.equals(qllxName)){
				selectSQL =  "select 'LD' as BDCDYLX,BDCDYID,BDCDYH,ZL ,'是' as SFTG , '#BBFFBB' as SFTGCOLOR from ";
				formSQL = " bdcdck.BDCS_SLLM_GZ ";
				data.put("bdcdylx", "SHYQZD");
			}
			if (DJLX.YGDJ.Name.equals(djlxname) && QLLX.GYJSYDSHYQ_FWSYQ.Name.equals(qllxName)) {
				selectSQL =  "select 'YCH' as BDCDYLX,BDCDYID,BDCDYH,ZL,DYH,ZCS,QSC,ZZC,SZC,YCJZMJ AS JZMJ,"
						+ " '是' as SFTG , '#BBFFBB' as SFTGCOLOR FROM ";
				formSQL = " bdck.bdcs_h_xzy ";
				data.put("bdcdylx", "YCH");
			}
			
			long toal = commonDao.getCountByFullSql(" from "+formSQL+" where "+bdcdyhSQL);
			int toal2 = Integer.parseInt(Long.toString(toal));
			data.put("toal", dyArray.size());
			if(toal2 >= dyArray.size()) {
				//不动产登记系统单元数据
				List<Map> dyInfo = commonDao.getDataListByFullSql(selectSQL+formSQL+ " where "+bdcdyhSQL, pageIndex, pageSize);
				dyInfo = getDYZT(dyInfo,formSQL," ",null,false);
				data.put("dyInfo", dyInfo);
				data.put("auditOpinion", "审核通过！在不动产登记系统的权籍调查库中存在单元信息。");
				msg.setState(true);
			}else if(dyArray.size() > toal2) {
				data = getWW_bdcdy_fy(pageIndex, pageSize, ww_dyList, data, selectSQL, formSQL, Integer.parseInt(Long.toString(toal)));
				msg.setState(false);
			}else {
				msg.setState(false);
				data.put("auditOpinion", "审核不通过！在不动产登记系统的权籍调查库中不存在单元信息。");
			}
			msg.setData(data);
		}
		return msg;
		
	}
	
	/**
	 * 外网项目单元存放在pro_proinst表上的EXTEND_DATA字段单元，
	 * 调用该方法分页查询及审核在不动产系统中是否存在数据
	 * @param pageIndex 页数
	 * @param pageSize 每页显示个数
	 * @param ww_dyList  外网单元List<Map>
	 * @param data 返回值
	 * @param toal 查询表中总共存在多少条
	 * @return
	 */
	private Map<String, Object> getWW_bdcdy_fy(int pageIndex,int pageSize,List<Map> ww_dyList,
			Map<String, Object> data,String selectSQL,String formSQL,int toal) {
		List<Map> dyInfo = new ArrayList<Map>();
		int gs = pageSize;
		String bdcdyhList = "";
		for (int i = 0; i < ww_dyList.size(); i++) {
			if (pageIndex*pageSize-gs == i) {
				bdcdyhList += ww_dyList.get(i).get("BDCDYH") +",";
				Map showdy = null;
				List<Map> bdcdy = commonDao.getDataListByFullSql(selectSQL+formSQL 
						+ " where bdcdyh = '"+ww_dyList.get(i).get("BDCDYH")+"'");
				if(bdcdy != null && bdcdy.size()>0) {
					showdy = bdcdy.get(0);
				} else {
					showdy = ww_dyList.get(i);
					showdy.put("SFTGCOLOR", "#FFD2D2");
					showdy.put("SFTG", "否");
				}
				dyInfo.add(showdy);
				gs--;
			}
		}
		getDYZT(dyInfo, formSQL, "", bdcdyhList, false);
		data.put("dyInfo", dyInfo);
		data.put("toal", ww_dyList.size());
		int notgs = ww_dyList.size()-toal;
		data.put("auditOpinion", "审核不通过！在不动产登记系统的权籍调查库中有"+notgs+"个不存在单元信息。");
		return data;
	}
	
	
	@Override
	public JsonMessage getAuditInfoData(int pageIndex, int pageSize, String id,String isshowbtn) throws Exception {
		JsonMessage msg = new JsonMessage();
		List<Map> dyInfoList = new ArrayList<Map>();
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("dyInfo", "");
		data.put("auditOpinion", "");
		data.put("toal", "0");
		if (!StringHelper.isEmpty(id)) {
			Map<String, String> paraMap = new HashMap<String, String>();
			Map<String, Object> createSQL = new HashMap<String, Object>();
			paraMap.put("ID", id);
			String k_user = " bdck.";
			//只能写不动产登记系统中的单元表
			String k_table_dy = "bdcs_h_xz   ";
			boolean bdcdyhSQL_b = false;
			boolean bdcqzmhSQL_b = false;
			//判断业务类型，确定去不动产登记系统哪个库中找数据。
			Map map = getRtconstraint(id);
			if(map != null) {
				String bdcdylx = map.get("BDCDYLX").toString();
				String ly = map.get("LY").toString();
				data.put("bdcdylx", ConstValue.BDCDYLX.initFrom(bdcdylx).toString());
				
				//确定初审的对比表
				if(ConstValue.DJDYLY.DC.Value.equals(ly)) {
					k_user = " bdcdck.";
					k_table_dy = ConstValue.BDCDYLX.initFrom(bdcdylx).GZTableName;
				} else if(ConstValue.DJDYLY.XZ.Value.equals(ly)) {
					k_table_dy = ConstValue.BDCDYLX.initFrom(bdcdylx).XZTableName;
				} else if(ConstValue.DJDYLY.GZ.Value.equals(ly)) {
					k_table_dy = ConstValue.BDCDYLX.initFrom(bdcdylx).GZTableName;
				} else if(ConstValue.DJDYLY.LS.Value.equals(ly)) {
					k_table_dy = ConstValue.BDCDYLX.initFrom(bdcdylx).LSTableName;
				}
				

				String sqrlx = "1";
				String workflowname = map.get("WORKFLOWNAME").toString();
				Pro_proinst pro_proinst  = baseCommonDaoInline.get(Pro_proinst.class, id);
				if ("BZ001".equals(workflowname) || "BZ002".equals(workflowname) || "HZ008".equals(workflowname)) {
					sqrlx= "0";
				}

				createSQL = createSQL2(id, sqrlx, bdcdyhSQL_b, bdcqzmhSQL_b,workflowname);
				if (createSQL != null) {
					bdcdyhSQL_b = (Boolean) createSQL.get("bdcdyh_b");
					bdcqzmhSQL_b = (Boolean) createSQL.get("bdcqzmh_b");
					String fromSQL = " from "+k_user+k_table_dy+" where ";
					//不动产登记系统存在的单元个数
					if(bdcdyhSQL_b && bdcqzmhSQL_b) {
						fromSQL += createSQL.get("bdcdyhSQL").toString() + " and "+createSQL.get("bdcqzmhSQL").toString() ;
					} else if(bdcdyhSQL_b) {
						fromSQL += createSQL.get("bdcdyhSQL").toString() ;
					} else if (bdcqzmhSQL_b) {
						fromSQL += createSQL.get("bdcqzmhSQL").toString() ;
						
					}
					long toal = commonDao.getCountByFullSql(fromSQL);
					
					//有单元号的，显示外网内容
					if(bdcdyhSQL_b) {
						//是否查询单元的权利
						boolean sfcxql = true;
						//有单元号录入的，显示的总数一定是外网pro_fwxx表上的
						long wwtoal = Long.parseLong(createSQL.get("dytoal").toString());
						data.put("toal", wwtoal);
						if(toal == 0 && StringHelper.getDouble(isshowbtn)  == 1) {
							data.put("auditOpinion", "初审通过<br>请创建项目后，手动选择单元<br>");
							sfcxql = true;
							msg.setState(true);
						}else if(toal == 0){
							sfcxql = false;
							data.put("auditOpinion", "初审不通过<br>办理业务的单元在不动产登记系统中没有找到的单元<br>");
							msg.setState(false);
						}/*else if (wwtoal > toal) {
							data.put("auditOpinion", "初审不通过<br>办理业务的单元在不动产登记系统中没有找到的单元："
									+(wwtoal - toal)+"个<br>");
							sfcxql = true;
							msg.setState(false);
						}*/ else {
							data.put("auditOpinion", "初审通过<br>办理业务的单元在不动产登记系统中都已存在<br>");
							sfcxql = true;
							msg.setState(true);
						}
						data = getDataBybdcdyh2(data, pageIndex, pageSize, paraMap, k_user, k_table_dy, fromSQL,sfcxql,bdcdylx);
					} else {
						//只有权证号的，显示不动产登记系统内容，没有提示为空。
						if(toal == 0 && isshowbtn == "1") {
							String row = getData(bdcdylx);
							data.put("auditOpinion", "初审通过<br>请创建项目后，手动选择单元<br>");
							msg.setState(true);
							data = getDataBybdcqzmh2(data, pageIndex, pageSize, k_user, k_table_dy, fromSQL, row);
							data.put("toal", toal);
						} else if(toal == 0 ){
							data.put("auditOpinion", "初审不通过<br>办理业务的单元在不动产登记系统中无法找到，请检查数据填写是否正确<br>");
							msg.setState(false);
						}else{
							String row = getData(bdcdylx);
							data.put("auditOpinion", "初审不通过<br>办理业务的单元在不动产登记系统中没有找到的单元<br>");
							msg.setState(true);
							data = getDataBybdcqzmh2(data, pageIndex, pageSize, k_user, k_table_dy, fromSQL, row);
							data.put("toal", toal);
						}
					}
				}
				msg.setData(data);
			}else {
				data.put("auditOpinion", "没有找到对应的流程！");
				msg.setState(false);
				msg.setData(data);
			}
		}
		return msg;
	}
	
	/**
	 * PRO_FWXX表保存的是不动产权证/证明号的时候，用来确定返回的字段
	 * @param bdcdylx
	 * @return
	 */
	public String getData(String bdcdylx){
		String row = " ID,BDCDYH,ZL,DYH,FWSYQMJ ";
		if (ConstValue.BDCDYLX.initFrom(bdcdylx).equals(ConstValue.BDCDYLX.H)) {
			row = " BDCDYID AS ID,BDCDYH,ZL,DYH,SCJZMJ AS FWSYQMJ,szc,qsc,zzc,zcs ";
		} else if (ConstValue.BDCDYLX.initFrom(bdcdylx).equals(ConstValue.BDCDYLX.YCH)) {
			row = " BDCDYID AS ID,BDCDYH,ZL,DYH,YCJZMJ AS FWSYQMJ,szc,qsc,zzc,zcs  ";
		} else if (ConstValue.BDCDYLX.initFrom(bdcdylx).equals(ConstValue.BDCDYLX.SHYQZD)) {
			row = " BDCDYID as ID,BDCDYH,ZL,ZDMJ,ZDSZD,ZDSZX,ZDSZN,ZDSZB ";
		} else if (ConstValue.BDCDYLX.initFrom(bdcdylx).equals(ConstValue.BDCDYLX.SYQZD)) {
			row = " BDCDYID as ID,BDCDYH,ZL,ZDMJ,ZDSZD,ZDSZX,ZDSZN,ZDSZB ";
		} else if (ConstValue.BDCDYLX.initFrom(bdcdylx).equals(ConstValue.BDCDYLX.LD)) {
			row = " BDCDYID as ID,BDCDYH,ZL,ZDMJ,ZDSZD,ZDSZX,ZDSZN,ZDSZB ";
		} 
		row += " , '"+bdcdylx+"' as bdcdylx ";
		return row;
		
	}
	
	private Map<String, Object> createSQL2 (String id,String sqrlx,boolean bdcdyh_b,boolean bdcqzmh_b, String workflowname) {
		Map<String, Object> sql_map = new HashMap<String, Object>();
		if (!StringHelper.isEmpty(id)) {
			Map<String, String> paraMap = new HashMap<String, String>();
			paraMap.put("ID", id);
			List<Map> bdcdyhMap;
			bdcdyhMap = baseCommonDaoInline.getDataListByFullSql(
					"select id,bdcdyh,bdcqzmh,chid FROM INLINE_INNER.pro_fwxx WHERE PROINST_ID = :ID ", paraMap);

			List<Map> proinstmap = baseCommonDaoInline.getDataListByFullSql(
					"select id,djlx,qllx FROM INLINE_INNER.pro_proinst WHERE id = :ID ", paraMap);
			if (bdcdyhMap != null && bdcdyhMap.size()>0 && !proinstmap.isEmpty()) {
				String djlx = StringHelper.formatObject(proinstmap.get(0).get("DJLX"));
				String qllx = StringHelper.formatObject(proinstmap.get(0).get("QLLX"));
				//判断都存在什么数据
				StringBuffer bdcdyhSQL = new StringBuffer();
				StringBuffer bdcdyhor  = new StringBuffer();
				int bdcdyhSQL_lenght = 0;
				sql_map.put("dytoal", bdcdyhMap.size());
				List<String>  bdcqzmhList = new ArrayList<String>();
				for (int i = 0; i < bdcdyhMap.size(); i++) {
					//判断填写条件是否存在不动产权证/证明号
					if (!StringHelper.isEmpty(bdcdyhMap.get(i).get("BDCQZMH"))) {
						bdcqzmh_b = true;
						String bdcqzmh = toSemiangle(bdcdyhMap.get(i).get("BDCQZMH").toString()).replaceAll(" ", "");
//							String bdcqzmh = bdcdyhMap.get(i).get("BDCQZMH").toString().replaceAll(" ", "");
//							bdcqzmh = bdcqzmh.replaceAll("（", "(");
//							bdcqzmh =bdcqzmh.replaceAll("）", ")");
						if(!bdcqzmhList.contains(bdcqzmh)) {
							bdcqzmhList.add(bdcqzmh);
						}
					} 
					
					if (!StringHelper.isEmpty(bdcdyhMap.get(i).get("BDCDYH"))) {
						bdcdyh_b = true;
						String bdcdyh = bdcdyhMap.get(i).get("BDCDYH").toString().replaceAll(" ", "");
						if (bdcdyhSQL.toString().contains(bdcdyh)) {
							continue;
						}
						if (bdcdyhor.length() == 0) {
							if (bdcdyhSQL.length() == 0) {
								bdcdyhSQL.append(" bdcdyh = '" + bdcdyh + "' ");
							} else {
								bdcdyhor.append("'" + bdcdyh + "'");
								
							}
						} else {
							bdcdyhor.append(",'" + bdcdyh + "'");
						}
						bdcdyhSQL_lenght++;
						if (bdcdyhSQL_lenght > 900) {
							bdcdyhSQL.append(" or bdcdyh in (" + bdcdyhor + ")");
							bdcdyhor.delete(0, bdcdyhor.length());
							bdcdyhSQL_lenght = 0;
						}
					}

					//交易系统调用接口创建的项目，目前都是预告类业务，传入的是chid，也就是bdcdyid,转换成bdcdyh

					if(!StringHelper.isEmpty(bdcdyhMap.get(i).get("CHID"))) {
						String bdcdyid = bdcdyhMap.get(i).get("CHID").toString().replaceAll(" ", "");
						List<Map> bdcdyList = commonDao.getDataListByFullSql("select bdcdyh from bdck.bdcs_h_xzy t where t.bdcdyid='"+bdcdyid+"'" );
						if(bdcdyList.size()>0) {
							bdcdyh_b = true;
							String bdcdyh = StringHelper.formatObject(bdcdyList.get(0).get("BDCDYH"));
							if (bdcdyhSQL.toString().contains(bdcdyh)) {
								continue;
							}

							//查出单元号直接赋值保存到profwxx表里，省去后面选择单元还要转换的步骤
							Pro_fwxx fwxx = baseCommonDaoInline.get(Pro_fwxx.class, StringHelper.formatObject(bdcdyhMap.get(i).get("ID")));
							fwxx.setBdcdyh(bdcdyh);
							baseCommonDaoInline.update(fwxx);

							if (bdcdyhor.length() == 0) {
								if (bdcdyhSQL.length() == 0) {
									bdcdyhSQL.append(" bdcdyh = '" + bdcdyh + "' ");
								} else {
									bdcdyhor.append("'" + bdcdyh + "'");

								}
							} else {
								bdcdyhor.append(",'" + bdcdyh + "'");
							}
							bdcdyhSQL_lenght++;
							if (bdcdyhSQL_lenght > 900) {
								bdcdyhSQL.append(" or bdcdyh in (" + bdcdyhor + ")");
								bdcdyhor.delete(0, bdcdyhor.length());
								bdcdyhSQL_lenght = 0;
							}
						}
					}

				}
				
				if(bdcqzmh_b) {
					String bdcsqzmh = "";
					if(bdcqzmhList.size() >1) {
						for (String bdcqzhString : bdcqzmhList) {
							if(bdcsqzmh == "") {
//								bdcsqzmh = " and (instr(TO_SINGLE_BYTE(to_char(bdcqzh)),'"+bdcqzhString+"')>0 ";
								bdcsqzmh = " and bdcqzh='"+bdcqzhString+"'";
							}else {
//								bdcsqzmh += " or instr(TO_SINGLE_BYTE(to_char(bdcqzh)),'"+bdcqzhString+"')>0 ";
								bdcsqzmh = " or bdcqzh='"+bdcqzhString+"' ";
							}
						}
						bdcsqzmh +=") ";
					} else {
//						bdcsqzmh =  " and instr(TO_SINGLE_BYTE(to_char(bdcqzh)),'"+bdcqzmhList.get(0)+"')>0  ";
						bdcsqzmh = " and bdcqzh='"+bdcqzmhList.get(0)+"'";
					}
					String sqr_sql = "";
					if("400".equals(djlx) && qllx.contains("23")) {
						//抵押注销，外网填的是义务人和抵押证明号，先从抵押证明号查询到房子，再关联房子和义务人
						sqr_sql = createSQR_SQL_ZX(id, sqrlx);
						sql_map.put("bdcqzmhSQL", " bdcdyid in (select distinct djdy.bdcdyid "
								+ " from bdck.bdcs_djdy_xz djdy,bdck.bdcs_ql_xz ql "
								+ " where djdy.djdyid = ql.djdyid "+bdcsqzmh+" and ql.djdyid in ("+sqr_sql+"))");
					} else {
						sqr_sql = createSQR_SQL(id, sqrlx);
						sql_map.put("bdcqzmhSQL", " bdcdyid in (select distinct djdy.bdcdyid "
								+ " from bdck.bdcs_djdy_xz djdy,bdck.bdcs_ql_xz ql "
								+ " where djdy.djdyid = ql.djdyid "+bdcsqzmh+" and ql.qlid in ("+sqr_sql+"))");
					}

				}
				
				if (bdcdyh_b) {
					if (!StringHelper.isEmpty(bdcdyhor.toString())) {
						//防止最后只有一个只有一个bdcdyh用in 会出现问题
						if (bdcdyhor.toString().contains(",")) {
							bdcdyhSQL.append(" or bdcdyh in (" + bdcdyhor + ")");
						} else {
							bdcdyhSQL.append(" or bdcdyh = " + bdcdyhor);
						} 
					}
					sql_map.put("bdcdyhSQL", "("+bdcdyhSQL+")");
				}
				
				sql_map.put("bdcqzmh_b", bdcqzmh_b);
				sql_map.put("bdcdyh_b", bdcdyh_b);
				return sql_map;
			}else {
				return null;
			}
		}
		return null;
		
	}
	
	/**
	 * 创建申请人匹配SQL，只有录入的是不动产权证/证明号才添加申请人进行匹配
	 * @param proinst_id
	 * @param sqrlx 不同的业务，匹配的可能是义务人或权利人
	 * @return
	 */
	private String createSQR_SQL(String proinst_id,String sqrlx) {
		String sqrsql = "";
		Map<String,String> paramMap = new HashMap<String, String>();
		paramMap.put("proinst_id", proinst_id);
		paramMap.put("sqrlx", sqrlx);
//		List<Map> sqrList = baseCommonDaoInline.getDataListByFullSql("select "
//				+ " (SQR_NAME || SQR_QT_FR_NAME || SQR_QT_DLR_NAME) as sqrname,"
//				+ " (SQR_ZJH || SQR_QT_ZJH) as sqrzjh "
//				+ "  from INLINE_INNER.PRO_PROPOSERINFO "
//				+ " where proinst_id = :proinst_id and sqr_lx = :sqrlx", paramMap);
		List<Map> sqrList = baseCommonDaoInline.getDataListByFullSql("select "
				+ " SQR_NAME  as sqrname,"
				+ " SQR_ZJH  as sqrzjh, " +
				" SQRJZH "
				+ "  from INLINE_INNER.PRO_PROPOSERINFO "
				+ " where proinst_id = :proinst_id and sqr_lx = :sqrlx", paramMap);
		if(sqrList != null && sqrList.size() >0) {
			String sqrListSQL = "";
			for (Map sqr : sqrList) {
				String sqrname = sqr.get("SQRNAME").toString();
				String sqrzjh = sqr.get("SQRZJH").toString();
				String sqrSQL = " QLRMC = '"+sqrname+"' and zjh = '"+sqrzjh+"' ";
				String jzh = StringHelper.formatObject(sqr.get("SQRJZH"));
				if(!StringHelper.isEmpty(jzh)){
					sqrSQL = " QLRMC = '"+sqrname+"' and （zjh = '"+sqrzjh+"' or zjh='"+jzh+"')";
				}
				if(sqrList.size() > 1) {
					if (sqrListSQL == "") {
						sqrListSQL = "(" + sqrSQL + ")";
					}else {
						sqrListSQL += " or (" + sqrSQL + ")";
					}
				}else {
					sqrListSQL =sqrSQL;
				}
			}
			sqrsql =  "select distinct qlid from bdck.bdcs_qlr_xz where "+sqrListSQL;
		}
		
		return sqrsql;
		
	}


	/**
	 * 创建申请人匹配SQL，只有录入的是不动产权证/证明号才添加申请人进行匹配
	 * @param proinst_id
	 * @param sqrlx 不同的业务，匹配的可能是义务人或权利人
	 * @return
	 */
	private String createSQR_SQL_ZX(String proinst_id,String sqrlx) {
		String sqrsql = "";
		Map<String,String> paramMap = new HashMap<String, String>();
		paramMap.put("proinst_id", proinst_id);
		paramMap.put("sqrlx", sqrlx);
		List<Map> sqrList = baseCommonDaoInline.getDataListByFullSql("select "
				+ " SQR_NAME  as sqrname,"
				+ " SQR_ZJH  as sqrzjh "
				+ "  from INLINE_INNER.PRO_PROPOSERINFO "
				+ " where proinst_id = :proinst_id and sqr_lx = :sqrlx", paramMap);
		if(sqrList != null && sqrList.size() >0) {
			String sqrListSQL = "";
			for (Map sqr : sqrList) {
				String sqrname = sqr.get("SQRNAME").toString();
				String sqrzjh = sqr.get("SQRZJH").toString();
				String sqrSQL = " QLRMC = '"+sqrname+"' and zjh = '"+sqrzjh+"' ";
				if(sqrList.size() > 1) {
					if (sqrListSQL == "") {
						sqrListSQL = "(" + sqrSQL + ")";
					}else {
						sqrListSQL += " or (" + sqrSQL + ")";
					}
				}else {
					sqrListSQL =sqrSQL;
				}
			}
			sqrsql =  " select ql.djdyid from bdck.bdcs_ql_xz ql where ql.qlid in ( select distinct qlid from bdck.bdcs_qlr_xz where "+sqrListSQL +")";
		}

		return sqrsql;

	}
	
	private Map<String,Object> getDataBybdcdyh2(Map<String,Object> data,int pageIndex, int pageSize,
			Map <String,String> paraMap,String k_user,String k_table_dy,String fromSQL,boolean sfjc,String bdcdylx) {
		List<Map> dyInfo = baseCommonDaoInline.getDataListByFullSql("select id,'"+bdcdylx+"' as bdcdylx,BDCDYH,zl,FWSYQMJ, SFTG,SFTGCOLOR, "
				+ " null as ZDMJ,null as ZDSZD,null as ZDSZX,null as ZDSZN,null as ZDSZB,null as SZC,null as QSC,null as ZZC  FROM "
				+ "(select ROWNUM rn ,id,BDCDYH,zl,FWSYQMJ,'是' as SFTG , '#BBFFBB' as SFTGCOLOR "
				+ "  FROM INLINE_INNER.PRO_FWXX  WHERE PROINST_ID = :ID " 
				+ " and ROWNUM < "+ (pageIndex * pageSize + 1) + ") WHERE RN >" + (pageIndex - 1) * pageSize, paraMap);
		if (sfjc) {
			List<Map> bdcdyList = commonDao
					.getDataListByFullSql("select bdcdyh " + fromSQL);
			String bdcdyhList = "";
			for (Map map : bdcdyList) {
				bdcdyhList += map.get("BDCDYH") + ",";
			}
			dyInfo = getDYZT(dyInfo, k_user, k_table_dy, bdcdyhList, true);
		}else {
			for (Map dymap : dyInfo) {
				dymap.put("DYZT", "否");
				dymap.put("CFZT", "否");
				dymap.put("YYZT", "否");
				dymap.put("SFTGCOLOR", "#FFD2D2");
				dymap.put("SFTG", "否");
			}
		}
		data.put("dyInfo", dyInfo);
		return data;
	}
	
	private Map<String,Object> getDataBybdcqzmh2(Map<String,Object> data,int pageIndex, int pageSize,
			String k_user,String k_table_dy,String fromSQL,String row) {
		
		List<Map> dyInfo = commonDao.getDataListByFullSql("select "+row
				+ ", '是' as SFTG ,'#BBFFBB' as SFTGCOLOR  "+fromSQL, pageIndex,pageSize);
		dyInfo = getDYZT(dyInfo,k_user,k_table_dy,null,false);
		data.put("dyInfo", dyInfo);
		return data;
	}
	
	
	/**
     * 全角空格为12288，半角空格为32
     * 其他字符半角(33-126)与全角(65281-65374)的对应关系是：均相差65248
     * 
     * 将字符串中的全角字符转为半角
     * @param src 要转换的包含全角的任意字符串
     * @return  转换之后的字符串
     */
	private static String toSemiangle(String src) {
        char[] c = src.toCharArray();
        for (int index = 0; index < c.length; index++) {
            if (c[index] == 12288) {// 全角空格
                c[index] = (char) 32;
            } else if (c[index] > 65280 && c[index] < 65375) {// 其他全角字符
                c[index] = (char) (c[index] - 65248);
            }
        }
        return String.valueOf(c);
    }

}
