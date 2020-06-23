package com.supermap.wisdombusiness.synchroinline.service;

import com.alibaba.fastjson.JSONObject;
import com.supermap.internetbusiness.util.ManualException;
import com.supermap.realestate.registration.model.*;
import com.supermap.realestate.registration.model.interfaces.Rights;
import com.supermap.realestate.registration.model.interfaces.SubRights;
import com.supermap.realestate.registration.service.QLService;
import com.supermap.realestate.registration.tools.RightsTools;
import com.supermap.realestate.registration.util.ConfigHelper;
import com.supermap.realestate.registration.util.ConstHelper;
import com.supermap.realestate.registration.util.ConstValue;
import com.supermap.realestate.registration.util.ConstValue.DJDYLY;
import com.supermap.realestate.registration.util.ConstValue.QLLX;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDaoInline;
import com.supermap.wisdombusiness.synchroinline.model.Pro_fwxx;
import com.supermap.wisdombusiness.synchroinline.model.Pro_proinst;
import com.supermap.wisdombusiness.synchroinline.model.Pro_proposerinfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 根据不同的流程更新对应的权利
 * @author baojj
 *
 */
@Service
public class InsertQLForProdef {
	@Autowired
	CommonDao dao;
	@Autowired
	private QLService qlService;
	@Autowired
	private InlineProjectService inlineProjectServicee;
	@Autowired
	CommonDaoInline baseCommonDaoInline;

	private static String czfs = "01";

	public void insertQLForZY002(Pro_proinst proinst, String xmbh) {
		//转移只需要将权利人添加入权利信息中
		//获取填写的部分权利信息
		List<Pro_fwxx> pro_fwxx = baseCommonDaoInline.getFwxxBySlsqId(proinst.getId());

		JSONObject data = JSONObject.parseObject(proinst.getExtend_data());
		if (data != null) {
			if (!StringHelper.isEmpty(data.get("czfs"))) {
				czfs = StringHelper.formatObject(data.get("czfs"));
			}
		}

		List<String> list_sqrid_qlr=new ArrayList<String>();//存放申请人id
		List<BDCS_SQR> sqrs = dao.getDataList(BDCS_SQR.class, "XMBH='"+xmbh+"'" + " AND SQRLB = '1'");
		for(BDCS_SQR sqr : sqrs) {
			list_sqrid_qlr.add(sqr.getId());
		}

		List<BDCS_XMXX> xmxx=dao.getDataList(BDCS_XMXX.class, "XMBH='"+xmbh+"'");
		if(xmxx != null && xmxx.size()>0){
			xmxx.get(0).setSFHBZS(proinst.getSfhbzs());
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
		//获取权利和附属权利信息
		List<Rights> qls=RightsTools.loadRightsByCondition(DJDYLY.GZ, "XMBH='"+xmbh+"'");
		for(Rights ql:qls){
			String djdyid=ql.getDJDYID();
			if(!djdyid_bdcdyid.containsKey(djdyid)){
				continue;
			}
			ql.setQDJG(pro_fwxx.isEmpty()?0:pro_fwxx.get(0).getQdjg());
			ql.setCZFS(czfs);
			ql.setFJ(pro_fwxx.isEmpty()?"":pro_fwxx.get(0).getFj());
			///添加权利人
			qlService.addQLRfromSQR(xmbh, ql.getId(),list_sqrid_qlr.toArray());
			dao.flush();
			qlService.UpdateQLandRebuildRelation((BDCS_QL_GZ) ql);
		}

	}

	public void insertQLForCS013(Pro_proinst proinst, String xmbh) {
		List<Pro_fwxx> pro_fwxx = baseCommonDaoInline.getFwxxBySlsqId(proinst.getId() );
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
		Date qlqssj = proinst.getQlqssj();
		Date qljssj = proinst.getQljssj();

		JSONObject data = JSONObject.parseObject(proinst.getExtend_data());
		if (data != null) {
			if (!StringHelper.isEmpty(data.get("czfs"))) {
				czfs = StringHelper.formatObject(data.get("czfs"));
			}
		}

		List<String> list_sqrid_qlr=new ArrayList<String>();//存放申请人id
		List<BDCS_SQR> sqrs = dao.getDataList(BDCS_SQR.class, "XMBH='"+xmbh+"'" + " AND SQRLB = '1'");
		for(BDCS_SQR sqr : sqrs) {
			list_sqrid_qlr.add(sqr.getId());
		}
		
		List<BDCS_XMXX> xmxx=dao.getDataList(BDCS_XMXX.class, "XMBH='"+xmbh+"'");
		if(xmxx != null && xmxx.size()>0){
			xmxx.get(0).setSFHBZS(proinst.getSfhbzs());
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

		List<Map> fwxx_djdys = exchangeFwxxTodjdyid(pro_fwxx);

		//获取权利和附属权利信息
		List<Rights> qls=RightsTools.loadRightsByCondition(DJDYLY.GZ, "XMBH='"+xmbh+"'");
		List<SubRights> fsqls=RightsTools.loadSubRightsByCondition(DJDYLY.GZ, "XMBH='"+xmbh+"'");
		for(Rights ql:qls){
			String djdyid=StringHelper.formatObject(ql.getDJDYID());
			String bdcdyh = StringHelper.formatObject(ql.getBDCDYH());
			if (pro_fwxx != null && pro_fwxx.size() > 0) {
				for(Pro_fwxx fwxx : pro_fwxx) {
					if(!StringHelper.isEmpty(fwxx.getBdcdyh()) && !StringHelper.isEmpty(bdcdyh)&& !StringHelper.formatObject(fwxx.getBdcdyh()).contains(bdcdyh)){
						continue;
					}

					if(!djdyid_bdcdyid.containsKey(djdyid)){
						continue;
					}
					if(QLLX.DIYQ.Value.equals(ql.getQLLX())){
						///添加权利人
						qlService.addQLRfromSQR(xmbh, ql.getId(),list_sqrid_qlr.toArray());
						///更新权利
						for(SubRights fsql:fsqls){
							BDCS_FSQL_GZ fsql_gz = (BDCS_FSQL_GZ)fsql;
							if(ql.getId().equals(fsql_gz.getQLID())||fsql_gz.getId().equals(ql.getFSQLID())){
								fsql_gz.setDYFS(fwxx.getDyfs());
								fsql_gz.setDYPGJZ(StringHelper.getDouble(fwxx.getDypgjz()));
                                if("1".equals(fwxx.getDyfs())&&!StringHelper.isEmpty(fwxx.getBdbzzqse())&&fwxx.getBdbzzqse()!=0.0){
									fsql_gz.setBDBZZQSE(fwxx.getBdbzzqse());
                                }
                                if("2".equals(fwxx.getDyfs())&&!StringHelper.isEmpty(fwxx.getZgzqse())&&fwxx.getZgzqse()!=0.0){
									fsql_gz.setZGZQSE(fwxx.getZgzqse());
                                }

								for(Map fwxx_djdy: fwxx_djdys) {
									Double DGBDBZZQSE = StringHelper.getDouble(fwxx_djdy.get("DGBDBZZQSE"));
									String fwxx_djdyid = StringHelper.formatObject(fwxx_djdy.get("djdyid"));
									if(djdyid!=null && djdyid.equals(fwxx_djdyid)) {
										fsql_gz.setDGBDBZZQSE(DGBDBZZQSE);
									}
								}

								dao.update(fsql_gz);
							}
						}
						dao.flush();
					}
					try{
						ql.setQLQSSJ(qlqssj);
						ql.setQLJSSJ(qljssj);
						ql.setFJ(fwxx.getFj());
						ql.setDJYY(fwxx.getDjyy());
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
	
	public void insertQLForZX004(Pro_proinst proinst, String xmbh) {
		List<Pro_fwxx> pro_fwxx = baseCommonDaoInline.getFwxxBySlsqId(proinst.getId());
		List<String> list_sqrid_qlr=new ArrayList<String>();//存放申请人id
		List<BDCS_SQR> sqrs = dao.getDataList(BDCS_SQR.class, "XMBH='"+xmbh+"'" + " AND SQRLB = '1'");
		for(BDCS_SQR sqr : sqrs) {
			list_sqrid_qlr.add(sqr.getId());
		}
		
		//构建登记单元id和不动产单元id关系
		HashMap<String,String> djdyid_bdcdyid=new HashMap<String, String>();
		List<BDCS_DJDY_GZ> djdys=dao.getDataList(BDCS_DJDY_GZ.class, "XMBH='"+xmbh+"'");
		for(BDCS_DJDY_GZ djdy:djdys){
			if(!djdyid_bdcdyid.containsKey(djdy.getDJDYID())){
				djdyid_bdcdyid.put(djdy.getDJDYID(), djdy.getBDCDYID());
			}
		}
		//获取权利和附属权利信息
		List<Rights> qls=RightsTools.loadRightsByCondition(DJDYLY.GZ, "XMBH='"+xmbh+"'");
		List<SubRights> fsqls=RightsTools.loadSubRightsByCondition(DJDYLY.GZ, "XMBH='"+xmbh+"'");
		for(Rights ql:qls){
			String djdyid=StringHelper.formatObject(ql.getDJDYID());
			String bdcdyh = StringHelper.formatObject(ql.getBDCDYH());
			if (pro_fwxx != null && pro_fwxx.size() > 0) {
				for(Pro_fwxx fwxx : pro_fwxx) {
					if(!StringHelper.isEmpty(fwxx.getBdcdyh())&& !StringHelper.isEmpty(bdcdyh) && !StringHelper.formatObject(fwxx.getBdcdyh()).contains(bdcdyh)){
						continue;
					}

					if(!djdyid_bdcdyid.containsKey(djdyid)){
						continue;
					}
					if(QLLX.DIYQ.Value.equals(ql.getQLLX())){
						///添加权利人
//						qlService.addQLRfromSQR(xmbh, ql.getId(),list_sqrid_qlr.toArray());
						///更新权利
						for(SubRights fsql:fsqls){
							if(ql.getId().equals(fsql.getQLID())||fsql.getId().equals(ql.getFSQLID())){
								//fsql.setZXFJ(fwxx.getZxfj());
								fsql.setZXDYYY(fwxx.getZxyy());
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
	
	public void insertQLForNormal(Pro_proinst proinst, String xmbh) {
		List<Pro_fwxx> pro_fwxx = baseCommonDaoInline.getFwxxBySlsqId(proinst.getId());
		List<String> list_sqrid_qlr=new ArrayList<String>();//存放申请人id
		List<BDCS_SQR> sqrs = dao.getDataList(BDCS_SQR.class, "XMBH='"+xmbh+"'" + " AND SQRLB = '1'");
		for(BDCS_SQR sqr : sqrs) {
			list_sqrid_qlr.add(sqr.getId());
		}

		JSONObject data = JSONObject.parseObject(proinst.getExtend_data());
		if (data != null) {
			if (!StringHelper.isEmpty(data.get("czfs"))) {
				czfs = StringHelper.formatObject(data.get("czfs"));
			}
		}
		
		List<BDCS_XMXX> xmxx=dao.getDataList(BDCS_XMXX.class, "XMBH='"+xmbh+"'");
		if(xmxx != null && xmxx.size()>0){
			xmxx.get(0).setSFHBZS(proinst.getSfhbzs());
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
		//获取权利和附属权利信息
		List<Rights> qls=RightsTools.loadRightsByCondition(DJDYLY.GZ, "XMBH='"+xmbh+"'");
		List<SubRights> fsqls=RightsTools.loadSubRightsByCondition(DJDYLY.GZ, "XMBH='"+xmbh+"'");
		for(Rights ql:qls){
			ql.setCZFS(czfs);

			String djdyid=StringHelper.formatObject(ql.getDJDYID());
			String bdcdyh = StringHelper.formatObject(ql.getBDCDYH());
			if (pro_fwxx != null && pro_fwxx.size() > 0) {
				for(Pro_fwxx fwxx : pro_fwxx) {
					if(!StringHelper.isEmpty(fwxx.getBdcdyh())&& !StringHelper.isEmpty(bdcdyh) && !StringHelper.formatObject(fwxx.getBdcdyh()).contains(bdcdyh)){
						continue;
					}

					if(!djdyid_bdcdyid.containsKey(djdyid)){
						continue;
					}
					///添加权利人
					qlService.addQLRfromSQR(xmbh, ql.getId(),list_sqrid_qlr.toArray());
				}
				dao.flush();
			}
			qlService.UpdateQLandRebuildRelation((BDCS_QL_GZ) ql);
		}
	}

	public void insertQLForBG(Pro_proinst proinst, String xmbh) {
		List<Pro_fwxx> pro_fwxx = dao.getDataList(Pro_fwxx.class, " PROINST_ID='"+proinst.getId()+"' ");
		List<String> list_sqrid_qlr=new ArrayList<String>();//存放申请人id
		List<BDCS_SQR> sqrs = dao.getDataList(BDCS_SQR.class, "XMBH='"+xmbh+"'" + " AND SQRLB = '1'");
		for(BDCS_SQR sqr : sqrs) {
			list_sqrid_qlr.add(sqr.getId());
		}

		JSONObject data = JSONObject.parseObject(proinst.getExtend_data());
		if (data != null) {
			if (!StringHelper.isEmpty(data.get("czfs"))) {
				czfs = StringHelper.formatObject(data.get("czfs"));
			}
		}
		
		List<BDCS_XMXX> xmxx=dao.getDataList(BDCS_XMXX.class, "XMBH='"+xmbh+"'");
		if(xmxx != null && xmxx.size()>0){
			xmxx.get(0).setSFHBZS(proinst.getSfhbzs());
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
		//获取权利和附属权利信息
		List<Rights> qls=RightsTools.loadRightsByCondition(DJDYLY.GZ, "XMBH='"+xmbh+"'");
		List<SubRights> fsqls=RightsTools.loadSubRightsByCondition(DJDYLY.GZ, "XMBH='"+xmbh+"'");
		for(Rights ql:qls){
			ql.setCZFS(czfs);

			String djdyid=StringHelper.formatObject(ql.getDJDYID());
			String bdcdyh = StringHelper.formatObject(ql.getBDCDYH());
			if (pro_fwxx != null && pro_fwxx.size() > 0) {
				for(Pro_fwxx fwxx : pro_fwxx) {
					if(!StringHelper.isEmpty(fwxx.getBdcdyh()) && !StringHelper.isEmpty(bdcdyh)&& !StringHelper.formatObject(fwxx.getBdcdyh()).contains(bdcdyh)){
						continue;
					}

					if(!djdyid_bdcdyid.containsKey(djdyid)){
						continue;
					}

					List<BDCS_H_GZ> bdcs_h_gz=dao.getDataList(BDCS_H_GZ.class, "BDCDYID='"+djdyid_bdcdyid.get(djdyid)+"'");
					if(bdcs_h_gz!=null&&bdcs_h_gz.size()>0){
						bdcs_h_gz.get(0).setZL(fwxx.getZl_bgh());
						bdcs_h_gz.get(0).setGHYT(fwxx.getYt_bgh());
						dao.update(bdcs_h_gz.get(0));
					}
					List<BDCS_SHYQZD_GZ> bdcs_shyqzd_gz=dao.getDataList(BDCS_SHYQZD_GZ.class, "BDCDYID='"+djdyid_bdcdyid.get(djdyid)+"'");
					if(bdcs_shyqzd_gz!=null&&bdcs_shyqzd_gz.size()>0){
						bdcs_shyqzd_gz.get(0).setZL(fwxx.getZl_bgh());
						bdcs_shyqzd_gz.get(0).setYT(fwxx.getYt_bgh());
						dao.update(bdcs_shyqzd_gz.get(0));
					}
					List<BDCS_TDYT_XZ> bdcs_tdyt_xz=dao.getDataList(BDCS_TDYT_XZ.class, "BDCDYID='"+ql.getBDCDYID()+"'");
					if(bdcs_tdyt_xz!=null&&bdcs_tdyt_xz.size()>0){
						bdcs_tdyt_xz.get(0).setTDYT(fwxx.getYt_bgh());
						bdcs_tdyt_xz.get(0).setTDYTMC(ConstHelper.getNameByValue("TDYT", fwxx.getYt_bgh()));
						dao.update(bdcs_tdyt_xz.get(0));
					}

					///添加权利人
					qlService.addQLRfromSQR(xmbh, ql.getId(),list_sqrid_qlr.toArray());
				}
				dao.flush();
			}
			qlService.UpdateQLandRebuildRelation((BDCS_QL_GZ) ql);
		}
	}

	public void insertQLForYG002(Pro_proinst proinst, String xmbh) {
		String xzqdm = ConfigHelper.getNameByValue("XZQHDM");
		String ed_json = proinst.getExtend_data();
		JSONObject data = JSONObject.parseObject(ed_json);
		if (data != null) {
			if (!StringHelper.isEmpty(data.get("czfs"))) {
				czfs = StringHelper.formatObject(data.get("czfs"));
			}
		}
		List<Pro_fwxx> pro_fwxx = baseCommonDaoInline.getFwxxBySlsqId(proinst.getId());
		List<String> list_sqrid_qlr=new ArrayList<String>();//存放权利人id
		List<String> list_sqrid_dyqr=new ArrayList<String>();//存放抵押权人id
		List<BDCS_SQR> sqrs = dao.getDataList(BDCS_SQR.class, "XMBH='"+xmbh+"'");
		for(BDCS_SQR sqr : sqrs) {
			if ("1".equals(sqr.getSQRLB())) {
				list_sqrid_qlr.add(sqr.getId());
			} else if ("10".equals(sqr.getSQRLB())) {
				list_sqrid_dyqr.add(sqr.getId());
				//外网抽取时设置抵押人申请人类别为10，需修改回来
				sqr.setSQRLB("1");
				dao.saveOrUpdate(sqr);
				dao.flush();
			}
		}

		Date qlqssj = proinst.getQlqssj();
		Date qljssj = proinst.getQljssj();
		Date qt_qlqssj = proinst.getQt_qlqssj();
		Date qt_qljssj = proinst.getQt_qljssj();

		List<BDCS_XMXX> xmxx=dao.getDataList(BDCS_XMXX.class, "XMBH='"+xmbh+"'");
		if(xmxx != null && xmxx.size()>0){
			xmxx.get(0).setSFHBZS(proinst.getSfhbzs());
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

		List<Map> fwxx_djdys = exchangeFwxxTodjdyid(pro_fwxx);

		//获取权利和附属权利信息
		List<Rights> qls=RightsTools.loadRightsByCondition(DJDYLY.GZ, "XMBH='"+xmbh+"'");
		List<SubRights> fsqls=RightsTools.loadSubRightsByCondition(DJDYLY.GZ, "XMBH='"+xmbh+"'");
		for(Rights ql:qls){
			ql.setCZFS(czfs);

			String djdyid=StringHelper.formatObject(ql.getDJDYID());
			String bdcdyh = StringHelper.formatObject(ql.getBDCDYH());
			if (pro_fwxx != null && pro_fwxx.size() > 0) {
				for(Pro_fwxx fwxx : pro_fwxx) {
					if(!StringHelper.isEmpty(fwxx.getBdcdyh()) && !StringHelper.isEmpty(bdcdyh)&& !StringHelper.formatObject(fwxx.getBdcdyh()).contains(bdcdyh)){
						continue;
					}

					if(!djdyid_bdcdyid.containsKey(djdyid)){
						continue;
					}
					if(QLLX.DIYQ.Value.equals(ql.getQLLX())){
						//添加抵押权人
						qlService.addQLRfromSQR(xmbh, ql.getId(),list_sqrid_dyqr.toArray());
						///更新权利
						ql.setQDJG(fwxx.getQdjg());
						ql.setQLQSSJ(qlqssj);
						ql.setQLJSSJ(qljssj);
						//玉林 交易接口调用的话参数比较特殊
						if (xzqdm.contains("4509")) {
							Object object = data.get("bdcdys");
							if(object != null ) {
								List<Map> list = (List<Map>) object;
								if(list.size()>0) {
									for (Map m : list) {
										if (fwxx.getChid().equals(m.get("chid"))) {
											ql.setFJ(StringHelper.formatObject(m.get("dy_fj")));
											ql.setDJYY(StringHelper.formatObject(m.get("dy_djyy")));
											break;
										}
									}
								}
							}
						} else {
							ql.setFJ(fwxx.getFj());
							ql.setDJYY(fwxx.getDjyy());
						}
						dao.update(ql);
						//更新附属权利
						for(SubRights fsql:fsqls){
							BDCS_FSQL_GZ fsql_gz = (BDCS_FSQL_GZ)fsql;
							if(ql.getId().equals(fsql_gz.getQLID())||fsql_gz.getId().equals(ql.getFSQLID())){
								fsql_gz.setDYFS(fwxx.getDyfs());
								fsql_gz.setDYPGJZ(StringHelper.getDouble(fwxx.getDypgjz()));
								if(ConstValue.DYFS.YBDY.Value.equals(fsql_gz.getDYFS())){
									fsql_gz.setBDBZZQSE(fwxx.getBdbzzqse());
									fsql_gz.setZGZQQDSS(null);
								}else if(ConstValue.DYFS.ZGEDY.Value.equals(fsql_gz.getDYFS())){
									fsql_gz.setZGZQSE(fwxx.getZgzqse());
								}
								fsql_gz.setDYFS(fwxx.getDyfs());
								fsql_gz.setDYPGJZ(fwxx.getDypgjz());

								for(Map fwxx_djdy: fwxx_djdys) {
									Double DGBDBZZQSE = StringHelper.getDouble(fwxx_djdy.get("DGBDBZZQSE"));
									String fwxx_djdyid = StringHelper.formatObject(fwxx_djdy.get("djdyid"));
									if(djdyid!=null && djdyid.equals(fwxx_djdyid)) {
										fsql_gz.setDGBDBZZQSE(DGBDBZZQSE);
									}
								}

								dao.update(fsql_gz);
							}
						}
						dao.flush();
					}else{
						//添加权利人
						qlService.addQLRfromSQR(xmbh, ql.getId(),list_sqrid_qlr.toArray());
						///更新权利
						ql.setQDJG(fwxx.getQdjg());
						if (xzqdm.contains("4509")) {
							if (!StringHelper.isEmpty(fwxx.getChid())) {
                                List<BDCS_TDYT_XZ> tdyts = getTDYTS(fwxx.getChid());
                                if (tdyts != null && tdyts.size() > 0) {
                                    ql.setQLQSSJ(tdyts.get(0).getQSRQ());
                                    ql.setQLJSSJ(tdyts.get(0).getZZRQ());
                                }
                            } else {
                                ql.setQLQSSJ(qlqssj);
                                ql.setQLJSSJ(qljssj);
                            }
						} else {
//							ql.setQLQSSJ(qlqssj);
//							ql.setQLJSSJ(qljssj);
						}
						ql.setFJ(fwxx.getFj());
						ql.setDJYY(fwxx.getDjyy());
						dao.update(ql);
						//更新附属权利
						for(SubRights fsql:fsqls){
							if(ql.getId().equals(fsql.getQLID())||fsql.getId().equals(ql.getFSQLID())){
								if(ConstValue.DYFS.YBDY.Value.equals(fsql.getDYFS())){
									fsql.setBDBZZQSE(fwxx.getBdbzzqse());
									fsql.setZGZQQDSS(null);
								}else if(ConstValue.DYFS.ZGEDY.Value.equals(fsql.getDYFS())){
									fsql.setZGZQSE(fwxx.getZgzqse());
								}
								dao.update(fsql);
							}
						}
						dao.flush();
					}
				}
				dao.flush();
			}
			qlService.UpdateQLandRebuildRelation((BDCS_QL_GZ) ql);
		}
	}

	public void insertQLForYG001(Pro_proinst proinst, String xmbh) {
		List<Pro_fwxx> pro_fwxx = baseCommonDaoInline.getFwxxBySlsqId(proinst.getId());
		List<String> list_sqrid_qlr=new ArrayList<String>();//存放申请人id
		List<BDCS_SQR> sqrs = dao.getDataList(BDCS_SQR.class, "XMBH='"+xmbh+"'" + " AND SQRLB = '1'");
		for(BDCS_SQR sqr : sqrs) {
			list_sqrid_qlr.add(sqr.getId());
		}

		JSONObject data = JSONObject.parseObject(proinst.getExtend_data());
		if (data != null) {
			if (!StringHelper.isEmpty(data.get("czfs"))) {
				czfs = StringHelper.formatObject(data.get("czfs"));
			}
		}
		
		List<BDCS_XMXX> xmxx=dao.getDataList(BDCS_XMXX.class, "XMBH='"+xmbh+"'");
		if(xmxx != null && xmxx.size()>0){
			xmxx.get(0).setSFHBZS(proinst.getSfhbzs());
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
		//获取权利和附属权利信息
		List<Rights> qls=RightsTools.loadRightsByCondition(DJDYLY.GZ, "XMBH='"+xmbh+"'");
		List<SubRights> fsqls=RightsTools.loadSubRightsByCondition(DJDYLY.GZ, "XMBH='"+xmbh+"'");
		for(Rights ql:qls){
			String djdyid=StringHelper.formatObject(ql.getDJDYID());
			String bdcdyh = StringHelper.formatObject(ql.getBDCDYH());
			for(Pro_fwxx fwxx : pro_fwxx) {
				if(!StringHelper.isEmpty(fwxx.getBdcdyh()) && !StringHelper.isEmpty(bdcdyh) && !fwxx.getBdcdyh().contains(bdcdyh)){
					continue;
				}
			
				if(!djdyid_bdcdyid.containsKey(djdyid)){
					continue;
				}
				//添加权利人
				qlService.addQLRfromSQR(xmbh, ql.getId(),list_sqrid_qlr.toArray());
				//更新权利
				ql.setQDJG(fwxx.getQdjg());
				ql.setQLQSSJ(fwxx.getQlqssj());
				ql.setQLJSSJ(fwxx.getQljssj());
				ql.setFJ(fwxx.getFj());
				ql.setDJYY(fwxx.getDjyy());
				ql.setCZFS(czfs);
				dao.update(ql);
			}
			dao.flush();
			qlService.UpdateQLandRebuildRelation((BDCS_QL_GZ) ql);
		}
	}

	public void insertQLForYG003(Pro_proinst proinst, String xmbh) {
		List<Pro_fwxx> pro_fwxx = baseCommonDaoInline.getFwxxBySlsqId(proinst.getId());
		List<String> list_sqrid_qlr=new ArrayList<String>();//存放权利人id
		List<String> list_sqrid_dyqr=new ArrayList<String>();//存放抵押权人id
		List<BDCS_SQR> sqrs = dao.getDataList(BDCS_SQR.class, "XMBH='"+xmbh+"'");
		for(BDCS_SQR sqr : sqrs) {
			if ("1".equals(sqr.getSQRLB())) {
				list_sqrid_qlr.add(sqr.getId());
				list_sqrid_dyqr.add(sqr.getId());
			}
		}

		JSONObject data = JSONObject.parseObject(proinst.getExtend_data());
		if (data != null) {
			if (!StringHelper.isEmpty(data.get("czfs"))) {
				czfs = StringHelper.formatObject(data.get("czfs"));
			}
		}

		Date qlqssj = proinst.getQlqssj();
		Date qljssj = proinst.getQljssj();

		List<BDCS_XMXX> xmxx=dao.getDataList(BDCS_XMXX.class, "XMBH='"+xmbh+"'");
		if(xmxx != null && xmxx.size()>0){
			xmxx.get(0).setSFHBZS(proinst.getSfhbzs());
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
		//获取权利和附属权利信息
		List<Rights> qls=RightsTools.loadRightsByCondition(DJDYLY.GZ, "XMBH='"+xmbh+"'");
		List<SubRights> fsqls=RightsTools.loadSubRightsByCondition(DJDYLY.GZ, "XMBH='"+xmbh+"'");
		for(Rights ql:qls){
			ql.setCZFS(czfs);
			String djdyid=StringHelper.formatObject(ql.getDJDYID());
			String bdcdyh = StringHelper.formatObject(ql.getBDCDYH());
			if (pro_fwxx != null && pro_fwxx.size() > 0) {
				for(Pro_fwxx fwxx : pro_fwxx) {
					if(!StringHelper.isEmpty(fwxx.getBdcdyh()) && !StringHelper.isEmpty(bdcdyh)&& !StringHelper.formatObject(fwxx.getBdcdyh()).contains(bdcdyh)){
						continue;
					}

					if(!djdyid_bdcdyid.containsKey(djdyid)){
						continue;
					}
					if(QLLX.DIYQ.Value.equals(ql.getQLLX())){
						//添加抵押权人
						qlService.addQLRfromSQR(xmbh, ql.getId(),list_sqrid_dyqr.toArray());
						///更新权利
						ql.setQDJG(fwxx.getQdjg());
						ql.setQLQSSJ(qlqssj);
						ql.setQLJSSJ(qljssj);
						ql.setFJ(fwxx.getFj());
						ql.setDJYY(fwxx.getDjyy());
						dao.update(ql);
						//更新附属权利
						for(SubRights fsql:fsqls){
							if(ql.getId().equals(fsql.getQLID())||fsql.getId().equals(ql.getFSQLID())){
								fsql.setDYFS(fwxx.getDyfs());
								fsql.setDYPGJZ(StringHelper.getDouble(fwxx.getDypgjz()));
								if(ConstValue.DYFS.YBDY.Value.equals(fsql.getDYFS())){
									fsql.setBDBZZQSE(fwxx.getBdbzzqse());
									fsql.setZGZQQDSS(null);
								}else if(ConstValue.DYFS.ZGEDY.Value.equals(fsql.getDYFS())){
									fsql.setZGZQSE(fwxx.getZgzqse());
								}
								fsql.setDYFS(fwxx.getDyfs());
								fsql.setDYPGJZ(fwxx.getDypgjz());
								dao.update(fsql);
							}
						}
						dao.flush();
					}else{
						//添加权利人
						qlService.addQLRfromSQR(xmbh, ql.getId(),list_sqrid_qlr.toArray());
						///更新权利
						ql.setQDJG(fwxx.getQdjg());
//						ql.setQLQSSJ(qlqssj);
//						ql.setQLJSSJ(qljssj);
						ql.setFJ(fwxx.getFj());
						ql.setDJYY(fwxx.getDjyy());
						dao.update(ql);
						//更新附属权利
						for(SubRights fsql:fsqls){
							if(ql.getId().equals(fsql.getQLID())||fsql.getId().equals(ql.getFSQLID())){
								if(ConstValue.DYFS.YBDY.Value.equals(fsql.getDYFS())){
									fsql.setBDBZZQSE(fwxx.getBdbzzqse());
									fsql.setZGZQQDSS(null);
								}else if(ConstValue.DYFS.ZGEDY.Value.equals(fsql.getDYFS())){
									fsql.setZGZQSE(fwxx.getZgzqse());
								}
								fsql.setDYFS(fwxx.getDyfs());
								fsql.setDYPGJZ(fwxx.getDypgjz());
								dao.update(fsql);
							}
						}
						dao.flush();
					}
				}
				dao.flush();
			}
			qlService.UpdateQLandRebuildRelation((BDCS_QL_GZ) ql);
		}
	}

	private List<BDCS_TDYT_XZ> getTDYTS(String bdcdyid) {
		List<BDCS_TDYT_XZ> tdyts = new ArrayList<BDCS_TDYT_XZ>();
		BDCS_H_XZY bdcs_h_xzy = dao.get(BDCS_H_XZY.class, bdcdyid);
		if (bdcs_h_xzy != null && !StringHelper.isEmpty(bdcs_h_xzy.getZDBDCDYID())) {
			tdyts = dao.getDataList(BDCS_TDYT_XZ.class, "BDCDYID ='"+bdcs_h_xzy.getZDBDCDYID() + "'");
		}
		return tdyts;
	}

	public void insertQLForZY007(Pro_proinst proinst, String xmbh) {
		String xzqdm = ConfigHelper.getNameByValue("XZQHDM");
		String ed_json = proinst.getExtend_data();
		JSONObject data = JSONObject.parseObject(ed_json);
		if (data != null) {
			if (!StringHelper.isEmpty(data.get("czfs"))) {
				czfs = StringHelper.formatObject(data.get("czfs"));
			}
		}
		List<Pro_fwxx> pro_fwxx = baseCommonDaoInline.getFwxxBySlsqId(proinst.getId());
		List<Pro_proposerinfo> wdyqrs = baseCommonDaoInline.getDataList(Pro_proposerinfo.class," proinst_id='"+proinst.getId()+"' and sqr_lx='10'");//外网填写的抵押权人
		List<String> list_sqrid_qlr=new ArrayList<String>();//存放权利人id
		List<String> list_sqrid_dyqr=new ArrayList<String>();//存放抵押权人id
		List<BDCS_SQR> sqrs = dao.getDataList(BDCS_SQR.class, "XMBH='"+xmbh+"' and sqrlb='1' ");
		for(BDCS_SQR sqr : sqrs) {
			if(!wdyqrs.isEmpty()) {
				Pro_proposerinfo dyqr = wdyqrs.get(0);
				if (dyqr.getSqr_zjh().contains(sqr.getZJH())) {
					list_sqrid_dyqr.add(sqr.getId());
				} else {
					list_sqrid_qlr.add(sqr.getId());
				}
			} else {
				list_sqrid_qlr.add(sqr.getId());
			}
		}
		if(list_sqrid_dyqr.isEmpty()) {
			throw new ManualException("系统无法匹配填写的抵押权人信息，请联系办理所在地不动产登记中心工作人员排查数据是否异常");
		}

		Date qlqssj = proinst.getQlqssj();
		Date qljssj = proinst.getQljssj();
		Date qt_qlqssj = proinst.getQt_qlqssj();
		Date qt_qljssj = proinst.getQt_qljssj();

		List<BDCS_XMXX> xmxx=dao.getDataList(BDCS_XMXX.class, "XMBH='"+xmbh+"'");
		if(xmxx != null && xmxx.size()>0){
			xmxx.get(0).setSFHBZS(proinst.getSfhbzs());
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

		List<Map> fwxx_djdys = exchangeFwxxTodjdyid(pro_fwxx);

		//获取权利和附属权利信息
		List<Rights> qls=RightsTools.loadRightsByCondition(DJDYLY.GZ, "XMBH='"+xmbh+"'");
		List<SubRights> fsqls=RightsTools.loadSubRightsByCondition(DJDYLY.GZ, "XMBH='"+xmbh+"'");
		for(Rights ql:qls){
			ql.setCZFS(czfs);

			String djdyid=StringHelper.formatObject(ql.getDJDYID());
			String bdcdyh = StringHelper.formatObject(ql.getBDCDYH());
			if (pro_fwxx != null && pro_fwxx.size() > 0) {
				for(Pro_fwxx fwxx : pro_fwxx) {
					if(!StringHelper.isEmpty(fwxx.getBdcdyh()) && !StringHelper.isEmpty(bdcdyh)&& !StringHelper.formatObject(fwxx.getBdcdyh()).contains(bdcdyh)){
						continue;
					}

					if(!djdyid_bdcdyid.containsKey(djdyid)){
						continue;
					}
					if(QLLX.DIYQ.Value.equals(ql.getQLLX())){
						//添加抵押权人
						qlService.addQLRfromSQR(xmbh, ql.getId(),list_sqrid_dyqr.toArray());
						///更新权利
						ql.setQDJG(fwxx.getQdjg());
						ql.setQLQSSJ(qlqssj);
						ql.setQLJSSJ(qljssj);
						//玉林 交易接口调用的话参数比较特殊
						if (xzqdm.contains("4509")) {
							Object object = data.get("bdcdys");
							if(object != null ) {
								List<Map> list = (List<Map>) object;
								if(list.size()>0) {
									for (Map m : list) {
										if (fwxx.getChid().equals(m.get("chid"))) {
											ql.setFJ(StringHelper.formatObject(m.get("dy_fj")));
											ql.setDJYY(StringHelper.formatObject(m.get("dy_djyy")));
											break;
										}
									}
								}
							}
						} else {
							ql.setFJ(fwxx.getFj());
							ql.setDJYY(fwxx.getDjyy());
						}
						dao.update(ql);
						//更新附属权利
						for(SubRights fsql:fsqls){
							BDCS_FSQL_GZ fsql_gz = (BDCS_FSQL_GZ)fsql;
							if(ql.getId().equals(fsql_gz.getQLID())||fsql_gz.getId().equals(ql.getFSQLID())){
								fsql_gz.setDYFS(fwxx.getDyfs());
								fsql_gz.setDYPGJZ(StringHelper.getDouble(fwxx.getDypgjz()));
								if(ConstValue.DYFS.YBDY.Value.equals(fsql_gz.getDYFS())){
									fsql_gz.setBDBZZQSE(fwxx.getBdbzzqse());
									fsql_gz.setZGZQQDSS(null);
								}else if(ConstValue.DYFS.ZGEDY.Value.equals(fsql_gz.getDYFS())){
									fsql_gz.setZGZQSE(fwxx.getZgzqse());
								}
								fsql_gz.setDYFS(fwxx.getDyfs());
								fsql_gz.setDYPGJZ(fwxx.getDypgjz());
								for(Map fwxx_djdy: fwxx_djdys) {
									Double DGBDBZZQSE = StringHelper.getDouble(fwxx_djdy.get("DGBDBZZQSE"));
									String fwxx_djdyid = StringHelper.formatObject(fwxx_djdy.get("djdyid"));
									if (djdyid != null && djdyid.equals(fwxx_djdyid)) {
										fsql_gz.setDGBDBZZQSE(DGBDBZZQSE);
									}
								}
								dao.update(fsql_gz);
							}
						}
						dao.flush();
					}else{
						//添加权利人
						qlService.addQLRfromSQR(xmbh, ql.getId(),list_sqrid_qlr.toArray());
						///更新权利
						ql.setQDJG(fwxx.getQdjg());
						if (xzqdm.contains("4509")) {
							if (!StringHelper.isEmpty(fwxx.getChid())) {
								List<BDCS_TDYT_XZ> tdyts = getTDYTS(fwxx.getChid());
								if (tdyts != null && tdyts.size() > 0) {
									ql.setQLQSSJ(tdyts.get(0).getQSRQ());
									ql.setQLJSSJ(tdyts.get(0).getZZRQ());
								}
							} else {
								ql.setQLQSSJ(qlqssj);
								ql.setQLJSSJ(qljssj);
							}
						} else {
//							ql.setQLQSSJ(qlqssj);
//							ql.setQLJSSJ(qljssj);
						}
						ql.setFJ(fwxx.getFj());
						ql.setDJYY(fwxx.getDjyy());
						dao.update(ql);
						//更新附属权利
						for(SubRights fsql:fsqls){
							if(ql.getId().equals(fsql.getQLID())||fsql.getId().equals(ql.getFSQLID())){
								if(ConstValue.DYFS.YBDY.Value.equals(fsql.getDYFS())){
									fsql.setBDBZZQSE(fwxx.getBdbzzqse());
									fsql.setZGZQQDSS(null);
								}else if(ConstValue.DYFS.ZGEDY.Value.equals(fsql.getDYFS())){
									fsql.setZGZQSE(fwxx.getZgzqse());
								}
								dao.update(fsql);
							}
						}
						dao.flush();
					}
				}
				dao.flush();
			}
			qlService.UpdateQLandRebuildRelation((BDCS_QL_GZ) ql);
		}
	}

	public void insertQLForZY118(Pro_proinst proinst, String xmbh) {
		String xzqdm = ConfigHelper.getNameByValue("XZQHDM");
		String ed_json = proinst.getExtend_data();
		JSONObject data = JSONObject.parseObject(ed_json);
		if (data != null) {
			if (!StringHelper.isEmpty(data.get("czfs"))) {
				czfs = StringHelper.formatObject(data.get("czfs"));
			}
		}
		List<Pro_fwxx> pro_fwxx = baseCommonDaoInline.getFwxxBySlsqId(proinst.getId());
		List<Pro_proposerinfo> wdyqrs = baseCommonDaoInline.getDataList(Pro_proposerinfo.class," proinst_id='"+proinst.getId()+"' and sqr_lx='10'");//外网填写的抵押权人
		List<String> list_sqrid_qlr=new ArrayList<String>();//存放权利人id
		List<String> list_sqrid_dyqr=new ArrayList<String>();//存放抵押权人id
		List<BDCS_SQR> sqrs = dao.getDataList(BDCS_SQR.class, "XMBH='"+xmbh+"' and sqrlb='1' ");
		for(BDCS_SQR sqr : sqrs) {
			boolean flag = false;
			for(Pro_proposerinfo dyqr : wdyqrs) {
				if (dyqr.getSqr_zjh().contains(sqr.getZJH())) {
					flag = true;
				}
			}
			if(flag) {
				list_sqrid_dyqr.add(sqr.getId());
			} else {
				list_sqrid_qlr.add(sqr.getId());
			}
		}

		List<BDCS_XMXX> xmxx=dao.getDataList(BDCS_XMXX.class, "XMBH='"+xmbh+"'");
		if(xmxx != null && xmxx.size()>0){
			xmxx.get(0).setSFHBZS(proinst.getSfhbzs());
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
		//获取权利和附属权利信息
		List<Rights> qls=RightsTools.loadRightsByCondition(DJDYLY.GZ, "XMBH='"+xmbh+"'");
		List<SubRights> fsqls=RightsTools.loadSubRightsByCondition(DJDYLY.GZ, "XMBH='"+xmbh+"'");
		for(Rights ql:qls){

			String djdyid=StringHelper.formatObject(ql.getDJDYID());
			String bdcdyh = StringHelper.formatObject(ql.getBDCDYH());
			if (pro_fwxx != null && pro_fwxx.size() > 0) {
				for(Pro_fwxx fwxx : pro_fwxx) {
					if(!StringHelper.isEmpty(fwxx.getBdcdyh()) && !StringHelper.isEmpty(bdcdyh)&& !StringHelper.formatObject(fwxx.getBdcdyh()).contains(bdcdyh)){
						continue;
					}

					if(!djdyid_bdcdyid.containsKey(djdyid)){
						continue;
					}
					if(!QLLX.DIYQ.Value.equals(ql.getQLLX())){
						//添加抵押权人
//						qlService.addQLRfromSQR(xmbh, ql.getId(),list_sqrid_dyqr.toArray());
						qlService.addQLRfromSQR(xmbh, ql.getId(),list_sqrid_qlr.toArray());
						ql.setQDJG(fwxx.getQdjg());
						ql.setCZFS(czfs);
						dao.update(ql);

					}
				}
			}
			qlService.UpdateQLandRebuildRelation((BDCS_QL_GZ) ql);
		}

	}


    public void insertQLForZY010(Pro_proinst proinst, String xmbh) {
        String xzqdm = ConfigHelper.getNameByValue("XZQHDM");
        String ed_json = proinst.getExtend_data();
        JSONObject data = JSONObject.parseObject(ed_json);
        if (data != null) {
            if (!StringHelper.isEmpty(data.get("czfs"))) {
                czfs = StringHelper.formatObject(data.get("czfs"));
            }
        }
        List<Pro_fwxx> pro_fwxx = baseCommonDaoInline.getFwxxBySlsqId(proinst.getId());
        List<String> list_sqrid_qlr=new ArrayList<String>();//存放权利人id
        List<String> list_sqrid_dyqr=new ArrayList<String>();//存放抵押权人id
        List<BDCS_SQR> sqrs = dao.getDataList(BDCS_SQR.class, "XMBH='"+xmbh+"' ");

		for(BDCS_SQR sqr : sqrs) {
			if ("1".equals(sqr.getSQRLB())) {
				list_sqrid_qlr.add(sqr.getId());
			} else if ("10".equals(sqr.getSQRLB())) {
				list_sqrid_dyqr.add(sqr.getId());
				//外网抽取时设置抵押人申请人类别为10，需修改回来
				sqr.setSQRLB("1");
				dao.saveOrUpdate(sqr);
				dao.flush();
			}
		}

        Date qlqssj = proinst.getQlqssj();
        Date qljssj = proinst.getQljssj();
        Date qt_qlqssj = proinst.getQt_qlqssj();
        Date qt_qljssj = proinst.getQt_qljssj();

        List<BDCS_XMXX> xmxx=dao.getDataList(BDCS_XMXX.class, "XMBH='"+xmbh+"'");
        if(xmxx != null && xmxx.size()>0){
            xmxx.get(0).setSFHBZS(proinst.getSfhbzs());
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
		List<Map> fwxx_djdys = exchangeFwxxTodjdyid(pro_fwxx);
        //获取权利和附属权利信息
        List<Rights> qls=RightsTools.loadRightsByCondition(DJDYLY.GZ, "XMBH='"+xmbh+"'");
        List<SubRights> fsqls=RightsTools.loadSubRightsByCondition(DJDYLY.GZ, "XMBH='"+xmbh+"'");
        for(Rights ql:qls){
            ql.setCZFS(czfs);

            String djdyid=StringHelper.formatObject(ql.getDJDYID());
            String bdcdyh = StringHelper.formatObject(ql.getBDCDYH());
            if (pro_fwxx != null && pro_fwxx.size() > 0) {
                for(Pro_fwxx fwxx : pro_fwxx) {
                    if(!StringHelper.isEmpty(fwxx.getBdcdyh()) && !StringHelper.isEmpty(bdcdyh)&& !StringHelper.formatObject(fwxx.getBdcdyh()).contains(bdcdyh)){
                        continue;
                    }

                    if(!djdyid_bdcdyid.containsKey(djdyid)){
                        continue;
                    }
                    if(QLLX.DIYQ.Value.equals(ql.getQLLX())){
                        //添加抵押权人
                        qlService.addQLRfromSQR(xmbh, ql.getId(),list_sqrid_dyqr.toArray());
                        ///更新权利
                        ql.setQDJG(fwxx.getQdjg());
                        ql.setQLQSSJ(qlqssj);
                        ql.setQLJSSJ(qljssj);
                        //玉林 交易接口调用的话参数比较特殊
                        if (xzqdm.contains("4509")) {
                            Object object = data.get("bdcdys");
                            if(object != null ) {
                                List<Map> list = (List<Map>) object;
                                if(list.size()>0) {
                                    for (Map m : list) {
                                        if (fwxx.getChid().equals(m.get("chid"))) {
                                            ql.setFJ(StringHelper.formatObject(m.get("dy_fj")));
                                            ql.setDJYY(StringHelper.formatObject(m.get("dy_djyy")));
                                            break;
                                        }
                                    }
                                }
                            }
                        } else {
                            ql.setFJ(fwxx.getFj());
                            ql.setDJYY(fwxx.getDjyy());
                        }
                        dao.update(ql);
                        //更新附属权利
                        for(SubRights fsql:fsqls){
							BDCS_FSQL_GZ fsql_gz = (BDCS_FSQL_GZ)fsql;
                            if(ql.getId().equals(fsql_gz.getQLID())||fsql_gz.getId().equals(ql.getFSQLID())){
								fsql_gz.setDYFS(fwxx.getDyfs());
								fsql_gz.setDYPGJZ(StringHelper.getDouble(fwxx.getDypgjz()));
                                if(ConstValue.DYFS.YBDY.Value.equals(fsql_gz.getDYFS())){
									fsql_gz.setBDBZZQSE(fwxx.getBdbzzqse());
									fsql_gz.setZGZQQDSS(null);
                                }else if(ConstValue.DYFS.ZGEDY.Value.equals(fsql_gz.getDYFS())){
									fsql_gz.setZGZQSE(fwxx.getZgzqse());
                                }
								fsql_gz.setDYFS(fwxx.getDyfs());
								fsql_gz.setDYPGJZ(fwxx.getDypgjz());
								for(Map fwxx_djdy: fwxx_djdys) {
									Double DGBDBZZQSE = StringHelper.getDouble(fwxx_djdy.get("DGBDBZZQSE"));
									String fwxx_djdyid = StringHelper.formatObject(fwxx_djdy.get("djdyid"));
									if(djdyid!=null && djdyid.equals(fwxx_djdyid)) {
										fsql_gz.setDGBDBZZQSE(DGBDBZZQSE);
									}
								}
                                dao.update(fsql_gz);
                            }
                        }
                        dao.flush();
                    }else{
                        //添加权利人
                        qlService.addQLRfromSQR(xmbh, ql.getId(),list_sqrid_qlr.toArray());
                        ///更新权利
                        ql.setQDJG(fwxx.getQdjg());
                        if (xzqdm.contains("4509")) {
                            if (!StringHelper.isEmpty(fwxx.getChid())) {
                                List<BDCS_TDYT_XZ> tdyts = getTDYTS(fwxx.getChid());
                                if (tdyts != null && tdyts.size() > 0) {
                                    ql.setQLQSSJ(tdyts.get(0).getQSRQ());
                                    ql.setQLJSSJ(tdyts.get(0).getZZRQ());
                                }
                            } else {
                                ql.setQLQSSJ(qlqssj);
                                ql.setQLJSSJ(qljssj);
                            }
                        } else {
//                            ql.setQLQSSJ(qlqssj);
//                            ql.setQLJSSJ(qljssj);
                        }
                        ql.setFJ(fwxx.getFj());
                        ql.setDJYY(fwxx.getDjyy());
                        dao.update(ql);
                        //更新附属权利
                        for(SubRights fsql:fsqls){
                            if(ql.getId().equals(fsql.getQLID())||fsql.getId().equals(ql.getFSQLID())){
                                if(ConstValue.DYFS.YBDY.Value.equals(fsql.getDYFS())){
                                    fsql.setBDBZZQSE(fwxx.getBdbzzqse());
                                    fsql.setZGZQQDSS(null);
                                }else if(ConstValue.DYFS.ZGEDY.Value.equals(fsql.getDYFS())){
                                    fsql.setZGZQSE(fwxx.getZgzqse());
                                }
                                dao.update(fsql);
                            }
                        }
                        dao.flush();
                    }
                }
                dao.flush();
            }
            qlService.UpdateQLandRebuildRelation((BDCS_QL_GZ) ql);
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

}
