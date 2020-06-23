package com.supermap.intelligent.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.supermap.intelligent.util.ManualException;
import com.supermap.realestate.registration.model.*;
import com.supermap.realestate.registration.model.interfaces.Rights;
import com.supermap.realestate.registration.model.interfaces.SubRights;
import com.supermap.realestate.registration.service.QLService;
import com.supermap.realestate.registration.tools.RightsTools;
import com.supermap.realestate.registration.util.ConstValue.DJDYLY;
import com.supermap.realestate.registration.util.ConstValue.QLLX;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDaoInline;
import com.supermap.wisdombusiness.synchroinline.model.Pro_fwxx;
import com.supermap.wisdombusiness.workflow.model.Wfd_Prodef;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 根据不同的流程更新对应的权利
 * @author baojj
 *
 */
@Service
public class InsertQLForData {
	@Autowired
    CommonDao dao;
	@Autowired
	private QLService qlService;
	@Autowired
    CommonDaoInline baseCommonDaoInline;

	private static String czfs = "01";

	public void inserQLByData(String xmbh, JSONObject jsonObject, Wfd_Prodef prodef) throws Exception {
		try{
			// 按具体流程进行具体操作
			List<WFD_MAPPING> mps = dao.getDataList(WFD_MAPPING.class, "WORKFLOWCODE='" + prodef.getProdef_Code() + "'");
			String workflowname = mps.get(0).getWORKFLOWNAME();

			if ("CS010".equals(workflowname) || "CS011".equals(workflowname) || "CS034".equals(workflowname) ) {
				insertQLForCS013(xmbh, jsonObject);
			} else if ("ZY007".equals(workflowname)){
				insertQLForZY007(xmbh, jsonObject);
			} else if ("ZX003".equals(workflowname) || "ZX004".equals(workflowname) || "ZX006".equals(workflowname) || "ZX009".equals(workflowname)) {
				insertQLForZX004(xmbh, jsonObject);
			} else if ("BG005".equals(workflowname) || "BG006".equals(workflowname)||"BG053".equals(workflowname)) {
				// TODO 目前变更仅支持权利信息变更（单元未变），所以可以用抵押权登记插入权利的方法
				insertQLForCS013(xmbh, jsonObject);
			}else if("ZY002".equals(workflowname)) {
				insertQLForZY002(xmbh, jsonObject);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("插入权利信息失败，" + e.getMessage(), e);
		}
	}

	public void insertQLForCS013(String xmbh, JSONObject jsonObject) {
		JSONObject basicInfo = jsonObject.getJSONObject("basicInfo");
		JSONArray dydyList = jsonObject.getJSONArray("dydyList");

		String qxdm = basicInfo.getString("QXDM");
		String dyfs = basicInfo.getString("DYFS");
		czfs = basicInfo.getString("CZFS");
		String sfhbzs = basicInfo.getString("SFHBZS");
		String djyy = basicInfo.getString("DJYY");
		String fj = basicInfo.getString("FJ");
		String zqdw = basicInfo.getString("ZQDW");
		Double zqse = basicInfo.getDouble("ZQSE");
		Double dypgjz = basicInfo.getDouble("DYPGJZ");

		List<String> list_sqrid_qlr=new ArrayList<String>();//存放申请人id
		List<BDCS_SQR> sqrs = dao.getDataList(BDCS_SQR.class, "XMBH='"+xmbh+"'" + " AND SQRLB = '1'");
		for(BDCS_SQR sqr : sqrs) {
			list_sqrid_qlr.add(sqr.getId());
		}

		List<BDCS_XMXX> xmxx = dao.getDataList(BDCS_XMXX.class, "XMBH='" + xmbh + "'");
		if (xmxx != null && xmxx.size() > 0) {
			xmxx.get(0).setSFHBZS(sfhbzs);
			dao.update(xmxx.get(0));
		}
		//构建登记单元id和不动产单元id关系
		HashMap<String,String> djdyid_bdcdyid=new HashMap<String, String>();
		List<BDCS_DJDY_GZ> djdys=dao.getDataList(BDCS_DJDY_GZ.class, "XMBH='"+xmbh+"'");
		for(BDCS_DJDY_GZ djdy:djdys){
			if(!djdyid_bdcdyid.containsKey(djdy.getDJDYID())){
				djdyid_bdcdyid.put(djdy.getDJDYID(), djdy.getBDCDYID());
			}
		}

		List<Map> maps = exchangeDyxxTodjdyid(dydyList);

		//获取权利和附属权利信息
		List<Rights> qls= RightsTools.loadRightsByCondition(DJDYLY.GZ, "XMBH='"+xmbh+"'");
		List<SubRights> fsqls= RightsTools.loadSubRightsByCondition(DJDYLY.GZ, "XMBH='"+xmbh+"'");
		for (Rights ql : qls) {
			String djdyid = StringHelper.formatObject(ql.getDJDYID());
			String bdcdyh = StringHelper.formatObject(ql.getBDCDYH());
			if (dydyList != null && dydyList.size() > 0) {
				for (int i = 0; i < dydyList.size(); i++) {
					String _bdcdyh = dydyList.getJSONObject(i).getString("BDCDYH");
					Date qlqssj = dydyList.getJSONObject(i).getDate("DYQLQSSJ");
					Date qljssj = dydyList.getJSONObject(i).getDate("DYQLJSSJ");
					String dyr = dydyList.getJSONObject(i).getString("DYR");
					String zwr = dydyList.getJSONObject(i).getString("ZWR");
					String zwrzjh = dydyList.getJSONObject(i).getString("ZWRZJH");
					Double dgbdbzzqse = dydyList.getJSONObject(i).getDouble("DGBDBZZQSE");
					String zgzqqdss = dydyList.getJSONObject(i).getString("ZGZQQDSS");
					String zjjzwdyfw = dydyList.getJSONObject(i).getString("ZJJZWDYFW");
					if (!StringHelper.isEmpty(_bdcdyh) && !StringHelper.isEmpty(bdcdyh) && !StringHelper.formatObject(_bdcdyh).contains(bdcdyh)) {
						continue;
					}
					if(!djdyid_bdcdyid.containsKey(djdyid)){
						continue;
					}
					if(QLLX.DIYQ.Value.equals(ql.getQLLX())){
						//添加权利人
						qlService.addQLRfromSQR(xmbh, ql.getId(),list_sqrid_qlr.toArray());
						//更新权利
						for(SubRights fsql:fsqls){
							BDCS_FSQL_GZ fsql_gz = (BDCS_FSQL_GZ)fsql;
							if(ql.getId().equals(fsql_gz.getQLID())||fsql_gz.getId().equals(ql.getFSQLID())){
								fsql_gz.setDYFS(dyfs);
								if ("1".equals(dyfs)) {
									fsql_gz.setBDBZZQSE(zqse);
								}
								if("2".equals(dyfs)){
									fsql_gz.setZGZQSE(zqse);
								}

								fsql_gz.setZQDW(zqdw);
								fsql_gz.setDYPGJZ(dypgjz);
//								fsql_gz.setDYR(dyr);
								fsql_gz.setZWR(zwr);
								fsql_gz.setZWRZJH(zwrzjh);
								fsql_gz.setZJJZWDYFW(zjjzwdyfw);
								fsql_gz.setZGZQQDSS(zgzqqdss);
								fsql_gz.setDGBDBZZQSE(dgbdbzzqse);

								/*for(Map map : maps) {
									Double DGBDBZZQSE = StringHelper.getDouble(map.get("DGBDBZZQSE"));
									String dyxx_djdyid = StringHelper.formatObject(map.get("djdyid"));
									if(djdyid!=null && djdyid.equals(dyxx_djdyid)) {
										fsql_gz.setDGBDBZZQSE(DGBDBZZQSE);
									}
								}*/
								dao.update(fsql_gz);
							}
						}
						dao.flush();
					}
					try{
						ql.setQLQSSJ(qlqssj);
						ql.setQLJSSJ(qljssj);
						ql.setFJ(fj);
						ql.setDJYY(djyy);
						ql.setCZFS(czfs);
					}catch(Exception e){
						System.err.println("时间格式转换错误");
						e.printStackTrace();
					}
				}
				dao.flush();
				qlService.UpdateQLandRebuildRelation((BDCS_QL_GZ) ql);
			}
		}
	}
	
	public void insertQLForZX004(String xmbh, JSONObject jsonObject) {

		JSONObject basicInfo = jsonObject.getJSONObject("basicInfo");
		JSONArray dydyList = jsonObject.getJSONArray("dydyList");

		String zxdjyy = basicInfo.getString("ZXDJYY");
		String zxfj = basicInfo.getString("ZXFJ");

		List<String> list_sqrid_qlr = new ArrayList<String>();//存放申请人id
		List<BDCS_SQR> sqrs = dao.getDataList(BDCS_SQR.class, "XMBH='"+xmbh+"'" + " AND SQRLB = '1'");
		for(BDCS_SQR sqr : sqrs) {
			list_sqrid_qlr.add(sqr.getId());
		}
		
		//构建登记单元id和不动产单元id关系
		HashMap<String, String> djdyid_bdcdyid = new HashMap<String, String>();
		List<BDCS_DJDY_GZ> djdys = dao.getDataList(BDCS_DJDY_GZ.class, "XMBH='" + xmbh + "'");
		for(BDCS_DJDY_GZ djdy:djdys){
			if(!djdyid_bdcdyid.containsKey(djdy.getDJDYID())){
				djdyid_bdcdyid.put(djdy.getDJDYID(), djdy.getBDCDYID());
			}
		}
		//获取权利和附属权利信息
		List<Rights> qls = RightsTools.loadRightsByCondition(DJDYLY.GZ, "XMBH='" + xmbh + "'");
		List<SubRights> fsqls = RightsTools.loadSubRightsByCondition(DJDYLY.GZ, "XMBH='" + xmbh + "'");
		for(Rights ql:qls){
			String djdyid = StringHelper.formatObject(ql.getDJDYID());
			String bdcdyh = StringHelper.formatObject(ql.getBDCDYH());
			if (dydyList != null && dydyList.size() > 0) {
				for (int i = 0; i < dydyList.size(); i++) {
					String _bdcdyh = dydyList.getJSONObject(i).getString("BDCDYH");
					if (!StringHelper.isEmpty(_bdcdyh) && !StringHelper.isEmpty(bdcdyh) && !StringHelper.formatObject(_bdcdyh).contains(bdcdyh)) {
						continue;
					}
					if(!djdyid_bdcdyid.containsKey(djdyid)){
						continue;
					}
					if(QLLX.DIYQ.Value.equals(ql.getQLLX())){
						///更新权利
						for(SubRights fsql:fsqls){
							if(ql.getId().equals(fsql.getQLID())||fsql.getId().equals(ql.getFSQLID())){
								fsql.setZXDYYY(zxdjyy);
								fsql.setZXFJ(zxfj);
								dao.update(fsql);
							}
						}
						dao.flush();
					}
				}
				dao.flush();
			}
		}
	}

	public void insertQLForZY007(String xmbh, JSONObject jsonObject) {
		JSONObject basicInfo = jsonObject.getJSONObject("basicInfo");
		JSONArray dydyList = jsonObject.getJSONArray("dydyList");
		JSONArray dyqrList = jsonObject.getJSONArray("dyqrList");

		String qxdm = basicInfo.getString("QXDM");
		String dyfs = basicInfo.getString("DYFS");
		czfs = basicInfo.getString("CZFS");
		String sfhbzs = basicInfo.getString("SFHBZS");
		String djyy = basicInfo.getString("DJYY");
		String fj = basicInfo.getString("FJ");
		String zqdw = basicInfo.getString("ZQDW");
		Double zqse = basicInfo.getDouble("ZQSE");
		Double dypgjz = basicInfo.getDouble("DYPGJZ");

		List<String> list_sqrid_qlr = new ArrayList<String>();//存放权利人id
		List<String> list_sqrid_dyqr = new ArrayList<String>();//存放抵押权人id
		List<BDCS_SQR> sqrs = dao.getDataList(BDCS_SQR.class, "XMBH='"+xmbh+"' and sqrlb='1' ");
		for(BDCS_SQR sqr : sqrs) {
			if (!dyqrList.isEmpty()) {
			    boolean flag = false;
				for (int i = 0; i < dyqrList.size(); i++) {
					String dyqrzjh = dyqrList.getJSONObject(i).getString("DYQRZJH");
					if (!StringHelper.isEmpty(dyqrzjh) && dyqrzjh.equals(sqr.getZJH())) {
						list_sqrid_dyqr.add(sqr.getId());
                        flag = true;
						break;
					}
				}
				if (flag) {
				    continue;
                }
			}
			list_sqrid_qlr.add(sqr.getId());
		}
		if(list_sqrid_dyqr.isEmpty()) {
			throw new ManualException("系统无法匹配填写的抵押权人信息，请联系办理所在地不动产登记中心工作人员排查数据是否异常");
		}

		List<BDCS_XMXX> xmxx = dao.getDataList(BDCS_XMXX.class, "XMBH='" + xmbh + "'");
		if (xmxx != null && xmxx.size() > 0) {
			xmxx.get(0).setSFHBZS(sfhbzs);
			dao.update(xmxx.get(0));
		}
		//构建登记单元id和不动产单元id关系
        HashMap<String, String> djdyid_bdcdyid = new HashMap<String, String>();
        List<BDCS_DJDY_GZ> djdys = dao.getDataList(BDCS_DJDY_GZ.class, "XMBH='" + xmbh + "'");
        for (BDCS_DJDY_GZ djdy : djdys) {
            if (!djdyid_bdcdyid.containsKey(djdy.getDJDYID())) {
                djdyid_bdcdyid.put(djdy.getDJDYID(), djdy.getBDCDYID());
            }
        }

//        List<Map> maps = exchangeDyxxTodjdyid(dydyList);

		//获取权利和附属权利信息
        List<Rights> qls = RightsTools.loadRightsByCondition(DJDYLY.GZ, "XMBH='" + xmbh + "'");
        List<SubRights> fsqls = RightsTools.loadSubRightsByCondition(DJDYLY.GZ, "XMBH='" + xmbh + "'");
        for (Rights ql : qls) {
            String djdyid = StringHelper.formatObject(ql.getDJDYID());
            String bdcdyh = StringHelper.formatObject(ql.getBDCDYH());
            if (dydyList != null && dydyList.size() > 0) {
                for (int i = 0; i < dydyList.size(); i++) {
                    String _bdcdyh = dydyList.getJSONObject(i).getString("BDCDYH");
                    String _bdcqzh = dydyList.getJSONObject(i).getString("BDCQZH");
                    Date qlqssj = dydyList.getJSONObject(i).getDate("DYQLQSSJ");
                    Date qljssj = dydyList.getJSONObject(i).getDate("DYQLJSSJ");
					String dyr = dydyList.getJSONObject(i).getString("DYR");
					String zwr = dydyList.getJSONObject(i).getString("ZWR");
					String zwrzjh = dydyList.getJSONObject(i).getString("ZWRZJH");
					Double dgbdbzzqse = dydyList.getJSONObject(i).getDouble("DGBDBZZQSE");
					String zgzqqdss = dydyList.getJSONObject(i).getString("ZGZQQDSS");
					String zjjzwdyfw = dydyList.getJSONObject(i).getString("ZJJZWDYFW");
                    if (!StringHelper.isEmpty(_bdcdyh) && !StringHelper.isEmpty(bdcdyh) && !StringHelper.formatObject(_bdcdyh).contains(bdcdyh)) {
                        continue;
                    }
                    if(!djdyid_bdcdyid.containsKey(djdyid)){
                        continue;
                    }
                    if(QLLX.DIYQ.Value.equals(ql.getQLLX())){
                        //添加抵押权人
                        qlService.addQLRfromSQR(xmbh, ql.getId(),list_sqrid_dyqr.toArray());
                        //更新权利
						ql.setQLQSSJ(qlqssj);
						ql.setQLJSSJ(qljssj);
						ql.setFJ(fj);
//						ql.setQDJG(null);
						ql.setDJYY(djyy);
						dao.update(ql);
                        for(SubRights fsql:fsqls){
                            BDCS_FSQL_GZ fsql_gz = (BDCS_FSQL_GZ)fsql;
                            if(ql.getId().equals(fsql_gz.getQLID())||fsql_gz.getId().equals(ql.getFSQLID())){
                                fsql_gz.setDYFS(dyfs);
								if ("1".equals(dyfs)) {
									fsql_gz.setBDBZZQSE(zqse);
								}
								if("2".equals(dyfs)){
									fsql_gz.setZGZQSE(zqse);
								}
								fsql_gz.setZQDW(zqdw);
								fsql_gz.setDYPGJZ(dypgjz);
//								fsql_gz.setDYR(dyr);
								fsql_gz.setZWR(zwr);
								fsql_gz.setZWRZJH(zwrzjh);
								fsql_gz.setZJJZWDYFW(zjjzwdyfw);
								fsql_gz.setZGZQQDSS(zgzqqdss);
								fsql_gz.setDGBDBZZQSE(dgbdbzzqse);

                                /*for(Map map : maps) {
                                    Double DGBDBZZQSE = StringHelper.getDouble(map.get("DGBDBZZQSE"));
                                    String dyxx_djdyid = StringHelper.formatObject(map.get("djdyid"));
                                    if(djdyid!=null && djdyid.equals(dyxx_djdyid)) {
                                        fsql_gz.setDGBDBZZQSE(DGBDBZZQSE);
                                    }
                                }*/
                                dao.update(fsql_gz);
                            }
                        }
                        dao.flush();
                    } else {
						//添加权利人
						qlService.addQLRfromSQR(xmbh, ql.getId(),list_sqrid_qlr.toArray());
						//更新权利
						ql.setFJ(fj);
//						ql.setQDJG(null);
						ql.setDJYY(djyy);
						dao.update(ql);
						//更新附属权利
						for(SubRights fsql:fsqls){
							BDCS_FSQL_GZ fsql_gz = (BDCS_FSQL_GZ)fsql;
							if(ql.getId().equals(fsql_gz.getQLID())||fsql_gz.getId().equals(ql.getFSQLID())){
								fsql_gz.setDYFS(dyfs);
								if ("1".equals(dyfs)) {
									fsql_gz.setBDBZZQSE(zqse);
								}
								if("2".equals(dyfs)){
									fsql_gz.setZGZQSE(zqse);
								}
								fsql_gz.setZQDW(zqdw);
								fsql_gz.setDYPGJZ(dypgjz);
								fsql_gz.setDYPGJZ(dypgjz);
//								fsql_gz.setDYR(dyr);
								fsql_gz.setZWR(zwr);
								fsql_gz.setZWRZJH(zwrzjh);
								fsql_gz.setZJJZWDYFW(zjjzwdyfw);
								fsql_gz.setZGZQQDSS(zgzqqdss);
								fsql_gz.setDGBDBZZQSE(dgbdbzzqse);

								/*for(Map map : maps) {
									Double DGBDBZZQSE = StringHelper.getDouble(map.get("DGBDBZZQSE"));
									String dyxx_djdyid = StringHelper.formatObject(map.get("djdyid"));
									if(djdyid!=null && djdyid.equals(dyxx_djdyid)) {
										fsql_gz.setDGBDBZZQSE(DGBDBZZQSE);
									}
								}*/
								dao.update(fsql_gz);
							}
						}
						dao.flush();
					}
                }
                dao.flush();
                qlService.UpdateQLandRebuildRelation((BDCS_QL_GZ) ql);
            }
        }
	}
	
	
	/**
	 * 产权转移处理
	 * @author tangchunsheng
	 * @param xmbh
	 * @param jsonObject
	 */
	public void insertQLForZY002(String xmbh, JSONObject jsonObject) {
		JSONObject basicInfo = jsonObject.getJSONObject("basicInfo");
		JSONArray dydyList = jsonObject.getJSONArray("dydyList"); 
		JSONArray dyqrList = jsonObject.getJSONArray("dyqrList");

		String qxdm = basicInfo.getString("QXDM");
		String dyfs = basicInfo.getString("DYFS");
		czfs = basicInfo.getString("CZFS");
		String sfhbzs = basicInfo.getString("SFHBZS");
		String djyy = basicInfo.getString("DJYY");
		String fj = basicInfo.getString("FJ");
		String hth = basicInfo.getString("HTH");
		String zsbs = basicInfo.getString("ZSBS");
		String tdshyqr= basicInfo.getString("TDSHYQR");
		Double qdjg = basicInfo.getDouble("QDJG");
		String zqdw =basicInfo.getString("ZQDW");
		String fwxz =basicInfo.getString("FWXZ");
		List<String> list_sqrid_qlr = new ArrayList<String>();//存放权利人id
		List<BDCS_SQR> sqrs = dao.getDataList(BDCS_SQR.class, "XMBH='"+xmbh+"' and sqrlb='1' ");
		for(BDCS_SQR sqr : sqrs) {
			list_sqrid_qlr.add(sqr.getId());
		}
		List<BDCS_XMXX> xmxx = dao.getDataList(BDCS_XMXX.class, "XMBH='" + xmbh + "'");
		if (xmxx != null && xmxx.size() > 0) {
			xmxx.get(0).setSFHBZS(sfhbzs);
			dao.update(xmxx.get(0));
		}
		//构建登记单元id和不动产单元id关系
        HashMap<String, String> djdyid_bdcdyid = new HashMap<String, String>();
        List<BDCS_DJDY_GZ> djdys = dao.getDataList(BDCS_DJDY_GZ.class, "XMBH='" + xmbh + "'");
        for (BDCS_DJDY_GZ djdy : djdys) {
            if (!djdyid_bdcdyid.containsKey(djdy.getDJDYID())) {
                djdyid_bdcdyid.put(djdy.getDJDYID(), djdy.getBDCDYID());
            }
        }

//        List<Map> maps = exchangeDyxxTodjdyid(dydyList);

		//获取权利和附属权利信息
        List<Rights> qls = RightsTools.loadRightsByCondition(DJDYLY.GZ, "XMBH='" + xmbh + "'");
        List<SubRights> fsqls = RightsTools.loadSubRightsByCondition(DJDYLY.GZ, "XMBH='" + xmbh + "'");
        for (Rights ql : qls) {
            String djdyid = StringHelper.formatObject(ql.getDJDYID());
            String bdcdyh = StringHelper.formatObject(ql.getBDCDYH());
            if (dydyList != null && dydyList.size() > 0) {
                for (int i = 0; i < dydyList.size(); i++) {
                    String _bdcdyh = dydyList.getJSONObject(i).getString("BDCDYH");
                    String _bdcqzh = dydyList.getJSONObject(i).getString("BDCQZH");
                    Date qlqssj = dydyList.getJSONObject(i).getDate("DYQLQSSJ");
                    Date qljssj = dydyList.getJSONObject(i).getDate("DYQLJSSJ");
                    if (!StringHelper.isEmpty(_bdcdyh) && !StringHelper.isEmpty(bdcdyh) && !StringHelper.formatObject(_bdcdyh).contains(bdcdyh)) {
                        continue;
                    }
                    if(!djdyid_bdcdyid.containsKey(djdyid)){
                        continue;
                    }
                        //添加抵押权人
                        qlService.addQLRfromSQR(xmbh, ql.getId(),list_sqrid_qlr.toArray());
                        //更新权利
						ql.setQLQSSJ(qlqssj);
						ql.setQLJSSJ(qljssj);
						ql.setFJ(fj);
						ql.setQDJG(qdjg);
						ql.setHTH(hth);
						ql.setCZFS(czfs);
						ql.setZSBS(zsbs);
						ql.setTDSHYQR(tdshyqr);
						ql.setDJYY(djyy);
						dao.update(ql);
                        for(SubRights fsql:fsqls){
                            BDCS_FSQL_GZ fsql_gz = (BDCS_FSQL_GZ)fsql;
                            if(ql.getId().equals(fsql_gz.getQLID())||fsql_gz.getId().equals(ql.getFSQLID())){
                								fsql_gz.setZQDW(zqdw);
                								fsql_gz.setFWXZ(fwxz);
                                /*for(Map map : maps) {
                                    Double DGBDBZZQSE = StringHelper.getDouble(map.get("DGBDBZZQSE"));
                                    String dyxx_djdyid = StringHelper.formatObject(map.get("djdyid"));
                                    if(djdyid!=null && djdyid.equals(dyxx_djdyid)) {
                                        fsql_gz.setDGBDBZZQSE(DGBDBZZQSE);
                                    }
                                }*/
                                dao.update(fsql_gz);
                            }
                        }
                        dao.flush();
                }
                dao.flush();
                qlService.UpdateQLandRebuildRelation((BDCS_QL_GZ) ql);
            }
        }
	}

	/**
	 * 对外网的房屋信息进行查询，转换成登记系统中的djdyid，用于对应登记选择单元后插入对应单元权利信息
	 * @return
	 */
	private List<Map> exchangeFwxxTodjdyid(List<Pro_fwxx> fwxxs) {
		List<Map> result = new ArrayList<Map>();
		for(Pro_fwxx fw : fwxxs) {
			Map<String, Object> map = new HashMap<String, Object>();
			String bdcqzh = fw.getBdcqzmh();
			String bdcdyh = fw.getBdcdyh();
			if(!StringHelper.isEmpty(bdcqzh) || !StringHelper.isEmpty(bdcdyh)) {
				StringBuilder sb = new StringBuilder();
				sb.append(" SELECT QL.DJDYID FROM BDCK.BDCS_QLR_XZ QLR LEFT JOIN BDCK.BDCS_QL_XZ QL ON QLR.QLID = QL.QLID WHERE 1=1 ");
				if(!StringHelper.isEmpty(bdcqzh)) {
					sb.append(" AND QLR.BDCQZH='"+bdcqzh+"' ");
				} else if(!StringHelper.isEmpty(bdcdyh)) {
					sb.append(" AND ql.bdcdyh='"+bdcdyh+"' ");
				} else {
					sb.append("AND 1 > 2 ");
				}
				List<Map> djdyids = dao.getDataListByFullSql(sb.toString());
				if(!djdyids.isEmpty()) {
					map.put("DGBDBZZQSE", fw.getDywjz());//单个被担保数额
					map.put("djdyid", djdyids.get(0).get("DJDYID"));
					result.add(map);
				}
			}
		}
		return result;
	}

	/**
	 * 对申报的单元信息进行查询，转换成登记系统中的djdyid，用于对应登记选择单元后插入对应单元权利信息
	 * @return
	 */
	private List<Map> exchangeDyxxTodjdyid(JSONArray dydyList) {
		List<Map> result = new ArrayList<Map>();
		for (int i = 0; i < dydyList.size(); i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			String bdcdyh = dydyList.getJSONObject(i).getString("BDCDYH");
			String bdcqzh = dydyList.getJSONObject(i).getString("BDCQZH");
			if(!StringHelper.isEmpty(bdcqzh) || !StringHelper.isEmpty(bdcdyh)) {
				StringBuilder sb = new StringBuilder();
				sb.append(" SELECT QL.DJDYID FROM BDCK.BDCS_QLR_XZ QLR LEFT JOIN BDCK.BDCS_QL_XZ QL ON QLR.QLID = QL.QLID WHERE 1=1 ");
				if(!StringHelper.isEmpty(bdcqzh)) {
					sb.append(" AND QLR.BDCQZH='"+bdcqzh+"' ");
				} else if(!StringHelper.isEmpty(bdcdyh)) {
					sb.append(" AND QL.BDCDYH='"+bdcdyh+"' ");
				} else {
					sb.append("AND 1 > 2 ");
				}
				List<Map> djdyids = dao.getDataListByFullSql(sb.toString());
				if(!djdyids.isEmpty()) {
					map.put("DGBDBZZQSE", dydyList.getJSONObject(i).getString("DGBDBZZQSE"));//单个被担保数额
					map.put("djdyid", djdyids.get(0).get("DJDYID"));
					result.add(map);
				}
			}
		}
		return result;
	}

}
