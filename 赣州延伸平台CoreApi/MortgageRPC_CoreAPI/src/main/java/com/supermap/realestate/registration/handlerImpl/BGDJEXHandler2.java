package com.supermap.realestate.registration.handlerImpl;

import java.io.File;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;

import com.supermap.realestate.registration.ViewClass.ProjectInfo;
import com.supermap.realestate.registration.ViewClass.UnitTree;
import com.supermap.realestate.registration.dataExchange.DJFDJFZ;
import com.supermap.realestate.registration.dataExchange.DJFDJGD;
import com.supermap.realestate.registration.dataExchange.DJFDJSF;
import com.supermap.realestate.registration.dataExchange.DJFDJSH;
import com.supermap.realestate.registration.dataExchange.DJFDJSJ;
import com.supermap.realestate.registration.dataExchange.DJFDJSQR;
import com.supermap.realestate.registration.dataExchange.DJFDJSZ;
import com.supermap.realestate.registration.dataExchange.DJTDJSLSQ;
import com.supermap.realestate.registration.dataExchange.FJF100;
import com.supermap.realestate.registration.dataExchange.Message;
import com.supermap.realestate.registration.dataExchange.ZTTGYQLR;
import com.supermap.realestate.registration.dataExchange.exchangeFactory;
import com.supermap.realestate.registration.dataExchange.fwsyq.KTTFWC;
import com.supermap.realestate.registration.dataExchange.fwsyq.KTTFWH;
import com.supermap.realestate.registration.dataExchange.fwsyq.KTTFWLJZ;
import com.supermap.realestate.registration.dataExchange.fwsyq.KTTFWZRZ;
import com.supermap.realestate.registration.dataExchange.fwsyq.QLTFWFDCQYZ;
import com.supermap.realestate.registration.dataExchange.shyq.KTFZDBHQK;
import com.supermap.realestate.registration.dataExchange.shyq.KTTGYJZD;
import com.supermap.realestate.registration.dataExchange.shyq.KTTGYJZX;
import com.supermap.realestate.registration.dataExchange.shyq.KTTZDJBXX;
import com.supermap.realestate.registration.dataExchange.shyq.QLFQLJSYDSYQ;
import com.supermap.realestate.registration.dataExchange.shyq.ZDK103;
import com.supermap.realestate.registration.dataExchange.utils.packageXml;
import com.supermap.realestate.registration.handler.DJHandler;
import com.supermap.realestate.registration.model.BDCS_C_GZ;
import com.supermap.realestate.registration.model.BDCS_DJDY_GZ;
import com.supermap.realestate.registration.model.BDCS_DJDY_LS;
import com.supermap.realestate.registration.model.BDCS_DJDY_XZ;
import com.supermap.realestate.registration.model.BDCS_DYBG;
import com.supermap.realestate.registration.model.BDCS_FSQL_GZ;
import com.supermap.realestate.registration.model.BDCS_FSQL_LS;
import com.supermap.realestate.registration.model.BDCS_FSQL_XZ;
import com.supermap.realestate.registration.model.BDCS_H_GZ;
import com.supermap.realestate.registration.model.BDCS_H_LS;
import com.supermap.realestate.registration.model.BDCS_LJZ_XZ;
import com.supermap.realestate.registration.model.BDCS_QDZR_GZ;
import com.supermap.realestate.registration.model.BDCS_QDZR_LS;
import com.supermap.realestate.registration.model.BDCS_QDZR_XZ;
import com.supermap.realestate.registration.model.BDCS_QLR_GZ;
import com.supermap.realestate.registration.model.BDCS_QLR_LS;
import com.supermap.realestate.registration.model.BDCS_QLR_XZ;
import com.supermap.realestate.registration.model.BDCS_QL_GZ;
import com.supermap.realestate.registration.model.BDCS_QL_LS;
import com.supermap.realestate.registration.model.BDCS_QL_XZ;
import com.supermap.realestate.registration.model.BDCS_SHYQZD_GZ;
import com.supermap.realestate.registration.model.BDCS_SHYQZD_LS;
import com.supermap.realestate.registration.model.BDCS_SHYQZD_XZ;
import com.supermap.realestate.registration.model.BDCS_SQR;
import com.supermap.realestate.registration.model.BDCS_XMXX;
import com.supermap.realestate.registration.model.BDCS_ZRZ_GZ;
import com.supermap.realestate.registration.model.BDCS_ZRZ_LS;
import com.supermap.realestate.registration.model.BDCS_ZRZ_XZ;
import com.supermap.realestate.registration.model.interfaces.House;
import com.supermap.realestate.registration.model.interfaces.RealUnit;
import com.supermap.realestate.registration.model.interfaces.Rights;
import com.supermap.realestate.registration.model.interfaces.RightsHolder;
import com.supermap.realestate.registration.model.interfaces.SubRights;
import com.supermap.realestate.registration.model.xmlExportmodel.MessageExport;
import com.supermap.realestate.registration.service.impl.ProjectServiceImpl;
import com.supermap.realestate.registration.tools.RightsHolderTools;
import com.supermap.realestate.registration.tools.RightsTools;
import com.supermap.realestate.registration.tools.UnitTools;
import com.supermap.realestate.registration.util.ConstValue;
import com.supermap.realestate.registration.util.ConstValue.BDCDYLX;
import com.supermap.realestate.registration.util.ConstValue.DJDYLY;
import com.supermap.realestate.registration.util.ConstValue.DJZT;
import com.supermap.realestate.registration.util.ConstValue.QLLX;
import com.supermap.realestate.registration.util.ConstValue.SF;
import com.supermap.realestate.registration.util.ConstValue.SQRLB;
import com.supermap.realestate.registration.util.Global;
import com.supermap.realestate.registration.util.ObjectHelper;
import com.supermap.realestate.registration.util.ProjectHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.realestate.registration.util.YwLogUtil;
import com.supermap.wisdombusiness.core.SuperHelper;
import com.supermap.wisdombusiness.core.SuperSpringContext;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;


/** 
 * @ClassName: BGDJEXHandler2 
 * @Description: 房地一体单元变更 ，支持n:n变更
 * @author:zhaomf 
 * @date 2017年12月11日 下午4:16:48 
*/
public class BGDJEXHandler2 extends DJHandlerBase implements DJHandler {
	
	protected Logger logger = Logger.getLogger(BGDJEXHandler2.class);

	/**
	 * 构造函数
	 * 
	 * @param info
	 */
	public BGDJEXHandler2(ProjectInfo info) {
		super(info);
	}

	/**
	 * 添加不动产单元
	 * 注意：图形拷贝可直接拷贝到XZ、LS
	 */
	@Override
	public boolean addBDCDY(String bdcdyid) {
		CommonDao dao = this.getCommonDao();
		String[] ids = bdcdyid.split(",");
		
		List<String> listZRZ = new ArrayList<String>();
		List<String> listZD = new ArrayList<String>();
		
		if (ids == null || ids.length <= 0) {
			return false;
		}
		for (int i = 0; i < ids.length; i++) {
			String id = ids[i];
			if (StringUtils.isEmpty(id)) {
				continue;
			}		
			RealUnit dy = UnitTools.loadUnit(this.getBdcdylx(), DJDYLY.XZ, id);
			if (dy != null) {
				BDCS_DJDY_GZ djdy = super.createDJDYfromXZ(id);
				if (djdy != null) {
					List<Rights> qls = RightsTools.loadRightsByCondition(DJDYLY.XZ, "DJDYID='" + djdy.getDJDYID() + "' AND QLLX='" + getQllx().Value + "'");
					if (qls != null && qls.size() > 0) {
						List<BDCS_QLR_XZ> qlrs = dao.getDataList(BDCS_QLR_XZ.class, "QLID='" + qls.get(0).getId() + "'");
						if (qlrs != null && qlrs.size() > 0) {
							for (int iqlr = 0; iqlr < qlrs.size(); iqlr++) {
								BDCS_QLR_XZ qlr = qlrs.get(iqlr);
								// 通过权利人名称和权利人证件号进行过滤，相同的权利人不重复添加到申请人。2015年12月24日晚23点刘树峰
								String zjhm = qlr.getZJH();
								boolean bexists = false;
								if (!StringHelper.isEmpty(qlr.getQLRMC())) {
									String Sql = "";
									if (!StringHelper.isEmpty(zjhm)) {
										Sql = MessageFormat.format(" XMBH=''{0}'' AND SQRXM=''{1}'' AND ZJH=''{2}''", getXMBH(), qlr.getQLRMC(), zjhm);
									} else {
										Sql = MessageFormat.format(" XMBH=''{0}'' AND SQRXM=''{1}'' AND ZJH IS NULL", getXMBH(), qlr.getQLRMC());
									}
									List<BDCS_SQR> sqrlist = dao.getDataList(BDCS_SQR.class, Sql);
									if (sqrlist != null && sqrlist.size() > 0) {
										bexists = true;
									}
								}
								if (!bexists) {
									BDCS_SQR sqr = super.copyXZQLRtoSQR(qlrs.get(iqlr), SQRLB.JF);
									if (sqr != null) {
										dao.save(sqr);
									}
								}
							}
						}
					}
					dao.save(djdy);
				}
			} else {
			  RealUnit dy_dc =UnitTools.loadUnit(this.getBdcdylx(), DJDYLY.DC, id);
				if(!StringHelper.isEmpty(dy_dc))
				{
					dy_dc.setDJZT(DJZT.DJZ.Value);
					dao.save(dy_dc);
				    dy=UnitTools.copyUnit(dy_dc, this.getBdcdylx(), DJDYLY.GZ);
				   if(!StringHelper.isEmpty(dy))
				   {
					   dy.setXMBH(this.getXMBH());
				   }
					//先判断h对应的宗地跟自然幢在XZ层有没有，没有从BDCDCK拷贝，ZRZ跟ZD
					if(dy instanceof House && BDCDYLX.H.equals(this.getBdcdylx()))
					{
						House h=(House) dy;
						String zrzbdcdyid=h.getZRZBDCDYID();
						RealUnit zrz_xz=UnitTools.loadUnit(BDCDYLX.ZRZ, DJDYLY.GZ, zrzbdcdyid);
						//考虑到分割时，可能添加第一个单元时，没有自然幢信息跟宗地信息，再添加单元时，有自然幢信息跟宗地信息，就不添加了
						if(StringHelper.isEmpty(zrz_xz))
						{
							if(!listZRZ.contains(zrzbdcdyid))
							{
								listZRZ.add(zrzbdcdyid);
								RealUnit zrz_dc=UnitTools.loadUnit(BDCDYLX.ZRZ, DJDYLY.DC, zrzbdcdyid);
								if(!StringHelper.isEmpty(zrz_dc))
								{
									RealUnit zrz_gz=UnitTools.copyUnit(zrz_dc, BDCDYLX.ZRZ, DJDYLY.GZ);
									super.CopyGeo(h.getId(), this.getBdcdylx(), DJDYLY.DC);
									zrz_dc.setDJZT(DJZT.DJZ.Value);
									zrz_gz.setXMBH(this.getXMBH());
									dao.update(zrz_gz);
									dao.update(zrz_dc);
								}
							}
						}
						String zdbdcdyid=h.getZDBDCDYID();
						RealUnit zd_xz=UnitTools.loadUnit(BDCDYLX.SHYQZD, DJDYLY.GZ, zdbdcdyid);
						if(StringHelper.isEmpty(zd_xz))
						{
							if(!listZD.contains(zdbdcdyid))
							{
								listZD.add(zdbdcdyid);
								RealUnit zd_dc=UnitTools.loadUnit(BDCDYLX.SHYQZD, DJDYLY.DC, zdbdcdyid);
								if(!StringHelper.isEmpty(zd_dc))
								{
									zd_dc.setDJZT(DJZT.DJZ.Value);
									RealUnit zd_gz=UnitTools.copyUnit(zd_dc, BDCDYLX.SHYQZD, DJDYLY.GZ);
									super.CopyGeo(zdbdcdyid, BDCDYLX.SHYQZD, DJDYLY.DC);//拷贝图形信息
									zd_gz.setXMBH(this.getXMBH());
									 dao.save(zd_gz);
									 dao.save(zd_dc);
								}
							}
						}
					}
				}
				// 登记单元索引表
				BDCS_DJDY_GZ djdy = super.createDJDYfromGZ(dy);
				if (djdy != null) {
					BDCS_QL_GZ ql = super.createQL(djdy, dy);
					BDCS_FSQL_GZ fsql = super.createFSQL(djdy.getDJDYID());
					ql.setFSQLID(fsql.getId());
					fsql.setQLID(ql.getId());
					// 如果是使用权宗地，要填写使用权面积,或者其他的
					if (djdy.getBDCDYLX().equals(BDCDYLX.SHYQZD.Value)) {
						BDCS_SHYQZD_GZ shyqzd = (BDCS_SHYQZD_GZ) dy;
						if (shyqzd != null) {
							fsql.setSYQMJ(shyqzd.getZDMJ());
						}
					}
					dao.save(djdy);
					dao.save(ql);
					dao.save(fsql);
				}
			}
			dao.save(dy);
			dao.flush();
		}
		return true;
	}

	/**
	 * 写入登记簿
	 */
	@Override
	public boolean writeDJB() {

		if (super.isCForCFING()) {
			return false;
		}
		String strSqlXMBH = ProjectHelper.GetXMBHCondition(getXMBH());
		List<BDCS_DJDY_GZ> djdys = getCommonDao().getDataList(BDCS_DJDY_GZ.class, strSqlXMBH +" ORDER BY LY");
		List<String> listBGQ = new ArrayList<String>();
		List<String> listBGH = new ArrayList<String>();
		if (djdys != null && djdys.size() > 0) {
			for (int idjdy = 0; idjdy < djdys.size(); idjdy++) {
				BDCS_DJDY_GZ bdcs_djdy_gz = djdys.get(idjdy);
				if (bdcs_djdy_gz != null) {
					String djdyid = bdcs_djdy_gz.getDJDYID();
					String bdcdyid = bdcs_djdy_gz.getBDCDYID();
					StringBuilder builder = new StringBuilder();
					builder.append(djdyid).append(";").append(bdcdyid);
					String strDYID = builder.toString();
					String ly = bdcs_djdy_gz.getLY();
					BDCDYLX lx = BDCDYLX.initFrom(bdcs_djdy_gz.getBDCDYLX());
					if (DJDYLY.GZ.Value.equals(ly)) {
						if (!listBGH.contains(strDYID)) {
							listBGH.add(strDYID);
						}
						super.CopyGZQLToXZAndLS(djdyid);
						super.CopyGZQLRToXZAndLS(djdyid);
						super.CopyGZQDZRToXZAndLS(djdyid);
						super.CopyGZZSToXZAndLS(djdyid);
						super.CopyGZDYToXZAndLS(bdcs_djdy_gz.getBDCDYID());
						RealUnit dy = UnitTools.loadUnit(this.getBdcdylx(), DJDYLY.DC, bdcs_djdy_gz.getBDCDYID());
						if(!StringHelper.isEmpty(dy)){
							dy.setDJZT(DJZT.YDJ.Value);
							getCommonDao().save(dy);
						}
						super.CopyGZDJDYToXZAndLS(bdcs_djdy_gz.getId());
						super.CopyGeo_BG(bdcdyid, lx, DJDYLY.initFrom(ly));
						
					} else if (DJDYLY.XZ.Value.equals(ly)) {
						if (!listBGQ.contains(strDYID)) {
							listBGQ.add(strDYID);
						}
						super.removeZSFromXZByALL(djdyid);
						super.removeQLRFromXZByALL(djdyid);
						super.removeQDZRFromXZByALL(djdyid);
						super.removeQLFromXZByALL(djdyid);
						UnitTools.deleteUnit(lx, DJDYLY.XZ, bdcdyid);
						super.removeDJDYFromXZ(djdyid);
						super.DeleteGeo(bdcdyid, lx, DJDYLY.initFrom(ly));
					}
				}
			}
			// 更新房屋属性后，更新zrz、zd
			if(BDCDYLX.H.equals(this.getBdcdylx()))
			{
				updateZRZ();
				updateZD();
			}
		}
		if (listBGQ != null && listBGQ.size() > 0 && listBGH != null && listBGH.size() > 0) {
			for (int ibgq = 0; ibgq < listBGQ.size(); ibgq++) {
				for (int ibgh = 0; ibgh < listBGH.size(); ibgh++) {
					String BGQDYID = listBGQ.get(ibgq);//老单元的ID 
					String BGHDYID = listBGH.get(ibgh);//新增单元的ID-在单元变更中放到lbdcdyid中
					Date time = new Date();
					RebuildDYBG(BGQDYID.split(";")[1], BGQDYID.split(";")[0], BGHDYID.split(";")[1], BGHDYID.split(";")[0], time, null);
				}
			}
		}

		this.SetSFDB();
		getCommonDao().flush();
		return true;
	}
	
	/**   
	 * @Title: updateZD   
	 * @Description: 更新宗地信息，删除变更前的zd一切属性（XZ）   ，登记变更后数据（登记单元、权利）（XZ、LS）（不需要拷贝图形了），构建变更关系
	 * @author:zhaomf
	 * @date:2017年12月11日 下午4:26:44          
	 */ 
	public void updateZD()
	{   
		CommonDao dao=getCommonDao();
		String strSqlXMBH = ProjectHelper.GetXMBHCondition(getXMBH());
		//变更后数据记录
		List<String> listBGH = new ArrayList<String>();
		//记录宗地的bdcdyid
		List<String> listZDBDCDYID = new ArrayList<String>();
		//获取变更后的宗地单元信息
		List<RealUnit> listZD = UnitTools.loadUnits(BDCDYLX.SHYQZD, DJDYLY.GZ, strSqlXMBH);
		//获取变更后宗地上房屋单元信息
		List<RealUnit> listH = UnitTools.loadUnits(BDCDYLX.H, DJDYLY.GZ, strSqlXMBH);
		//获取变更后宗地上房屋的登记单元
		List<BDCS_DJDY_GZ> djdys=dao.getDataList(BDCS_DJDY_GZ.class, strSqlXMBH +" and LY ='"+DJDYLY.GZ.Value+"'");
		if(null!=djdys) {
			for (BDCS_DJDY_GZ bdcs_DJDY_GZ : djdys) {
				if(null!=listH) {
					//确定登记单元与单元一对一的关系（房屋）
					for (RealUnit realUnitH : listH) {
						if(realUnitH.getId().equals(bdcs_DJDY_GZ.getBDCDYID())) {
							if(null!=listZD) {
								//确认房屋所在宗地，要求地上房屋都是多个单元一本证，不允许分开发证
								for (RealUnit realUnitZD : listZD) {
									//记录下宗地的bdcdyid，避免数据重复
									if(!listZDBDCDYID.contains(realUnitZD.getId())) {
										if(realUnitH instanceof BDCS_H_GZ) {
											BDCS_H_GZ h = (BDCS_H_GZ) realUnitH;
											if(realUnitZD.getId().equals(h.getZDBDCDYID())) {
												logger.info("变更后宗地单元号："+realUnitZD.getBDCDYH());
												listBGH = update(bdcs_DJDY_GZ,realUnitZD,listBGH);
												listZDBDCDYID.add(realUnitZD.getId());
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		// 记录变更前单元
		List<String> listBGQ = new ArrayList<String>();
		
		//获取变更前宗地单元
		List<BDCS_DJDY_GZ> listdjdy =dao.getDataList(BDCS_DJDY_GZ.class, strSqlXMBH +" and LY ='"+DJDYLY.XZ.Value+"'");
		for (BDCS_DJDY_GZ bdcs_DJDY_GZ : listdjdy) {
			BDCS_H_LS h = (BDCS_H_LS)UnitTools.loadUnit(BDCDYLX.H, DJDYLY.LS, bdcs_DJDY_GZ.getBDCDYID());
			if(null!=h) {
				List<BDCS_DJDY_XZ> listdjdyzd=dao.getDataList(BDCS_DJDY_XZ.class, "BDCDYID='"+h.getZDBDCDYID()+"'");
				if(listdjdyzd!=null&&listdjdyzd.size()>0){
					String djdyid = listdjdyzd.get(0).getDJDYID();
					String bdcdyid = listdjdyzd.get(0).getBDCDYID();
					super.removeZSFromXZByALL(djdyid);
					super.removeQLRFromXZByALL(djdyid);
					super.removeQDZRFromXZByALL(djdyid);
					super.removeQLFromXZByALL(djdyid);
					UnitTools.deleteUnit(BDCDYLX.SHYQZD, DJDYLY.XZ, bdcdyid);
					super.removeDJDYFromXZ(djdyid);
					logger.info("开始删除宗地图层");
					super.DeleteGeo(bdcdyid, BDCDYLX.SHYQZD, DJDYLY.XZ);
					listBGQ.add(djdyid+":"+bdcdyid);
				}
			}
		}
		for (String q : listBGQ) {
			for (String h : listBGH) {
				RebuildDYBG(q.split(":")[1], q.split(":")[0], h.split(":")[1], h.split(":")[0], new Date(), null);
			}
		}
	}
	
	/**   
	 * @Title: update   
	 * @Description: 通过房屋更新宗地   
	 * @author:zhaomf
	 * @date:2017年12月12日 上午11:48:34    
	 * @param djdy_h 房屋djdy
	 * @param realUnitZD      房屋所在宗地单元
	 * @param listBGH 
	 * @return 
	 */ 
	private List<String> update(BDCS_DJDY_GZ djdy_h,RealUnit realUnitZD, List<String> listBGH) {
		CommonDao dao=getCommonDao();
		String strSqlXMBH = ProjectHelper.GetXMBHCondition(getXMBH());
		//构建djdy
		BDCS_DJDY_GZ djdy_zd = this.createDJDYfromGZ(realUnitZD);
		djdy_zd.setBDCDYLX(BDCDYLX.SHYQZD.Value);
		BDCS_DJDY_XZ bdcs_djdy_xz = ObjectHelper.copyDJDY_GZToXZ(djdy_zd);
		BDCS_DJDY_LS bdcs_djdy_ls = ObjectHelper.copyDJDY_XZToLS(bdcs_djdy_xz);
		dao.save(bdcs_djdy_xz);
		dao.save(bdcs_djdy_ls);
		//记录djdyid
		String djdyid = djdy_zd.getDJDYID();
		//生产qlid、fsqlid、zsid；
		String qlid = SuperHelper.GeneratePrimaryKey();
		String fsqlid = SuperHelper.GeneratePrimaryKey();
		//构建ql,来自于房屋信息
		BDCS_QL_GZ ql_h = (BDCS_QL_GZ)RightsTools.loadRightsByDJDYID(DJDYLY.GZ, this.getXMBH(), djdy_h.getDJDYID());
		BDCS_QL_GZ ql_zd = new BDCS_QL_GZ();
		ObjectHelper.copyObject(ql_h, ql_zd);
		ql_zd.setId(qlid);
		ql_zd.setFSQLID(fsqlid);
		ql_zd.setBDCDYID(realUnitZD.getId());
		ql_zd.setDJDYID(djdyid);
		ql_zd.setBDCDYH(djdy_zd.getBDCDYH());
		ql_zd.setQLLX(QLLX.GYJSYDSHYQ.Value);
		BDCS_QL_XZ bdcs_ql_xz = ObjectHelper.copyQL_GZToXZ(ql_zd);
		BDCS_QL_LS bdcs_ql_ls = ObjectHelper.copyQL_XZToLS(bdcs_ql_xz);
		dao.save(bdcs_ql_xz);
		dao.save(bdcs_ql_ls);
		//构建fsql
		BDCS_FSQL_GZ fsql_h = (BDCS_FSQL_GZ)RightsTools.loadSubRights(DJDYLY.GZ, ql_h.getFSQLID());
		BDCS_FSQL_GZ fsql_zd = new BDCS_FSQL_GZ();
		ObjectHelper.copyObject(fsql_h, fsql_zd);
		fsql_zd.setId(fsqlid);
		fsql_zd.setQLID(qlid);
		fsql_zd.setDJDYID(djdyid);
		fsql_zd.setBDCDYH(djdy_zd.getBDCDYH());
		fsql_zd.setSYQMJ(realUnitZD.getMJ());
		BDCS_FSQL_XZ bdcs_fsql_xz = ObjectHelper.copyFSQL_GZToXZ(fsql_zd);
		BDCS_FSQL_LS bdcs_fsql_ls = ObjectHelper.copyFSQL_XZToLS(bdcs_fsql_xz);
		dao.save(bdcs_fsql_xz);
		dao.save(bdcs_fsql_ls);
		//构建qlr，来自于房屋
		List<RightsHolder> RightsHolders = RightsHolderTools.loadRightsHolders(DJDYLY.GZ, ql_h.getId());
		for (RightsHolder rightsHolder : RightsHolders) {
			BDCS_QLR_GZ qlr_h = (BDCS_QLR_GZ) rightsHolder;
			BDCS_QLR_GZ qlr_zd = new BDCS_QLR_GZ();
			String qlrid = qlr_zd.getId();
			ObjectHelper.copyObject(qlr_h, qlr_zd);
			qlr_zd.setId(qlrid);
			qlr_zd.setQLID(qlid);
			BDCS_QLR_XZ bdcs_qlr_xz = ObjectHelper.copyQLR_GZToXZ(qlr_zd);
			BDCS_QLR_LS bdcs_qlr_ls = ObjectHelper.copyQLR_XZToLS(bdcs_qlr_xz);
			dao.save(bdcs_qlr_xz);
			dao.save(bdcs_qlr_ls);
			//构建qdzr，不构建zs
			List<BDCS_QDZR_GZ> listqdzr = dao.getDataList(BDCS_QDZR_GZ.class, strSqlXMBH + " and qlrid='" + qlr_h.getId() + "' ");
			for (BDCS_QDZR_GZ qdzr_h : listqdzr) {
				BDCS_QDZR_GZ qdzr_zd = new BDCS_QDZR_GZ();
				String qdzrid = qdzr_zd.getId();
				ObjectHelper.copyObject(qdzr_h, qdzr_zd);
				qdzr_zd.setId(qdzrid);
				qdzr_zd.setQLRID(qlrid);
				qdzr_zd.setQLID(qlid);
				qdzr_zd.setFSQLID(fsqlid);
				qdzr_zd.setBDCDYH(djdy_zd.getBDCDYH());
				qdzr_zd.setDJDYID(djdyid);
				BDCS_QDZR_XZ bdcs_qdzr_xz = ObjectHelper.copyQDZR_GZToXZ(qdzr_zd);
				BDCS_QDZR_LS bdcs_qdzr_ls = ObjectHelper.copyQDZR_XZToLS(bdcs_qdzr_xz);
				dao.save(bdcs_qdzr_xz);
				dao.save(bdcs_qdzr_ls);
			}
		}
		//拷贝单元到现状，历史
		RealUnit xzunit = UnitTools.copyUnit(BDCDYLX.SHYQZD, DJDYLY.GZ, DJDYLY.XZ, realUnitZD.getId());
		RealUnit lsunit = UnitTools.copyUnit(BDCDYLX.SHYQZD, DJDYLY.GZ, DJDYLY.LS, realUnitZD.getId());
		dao.save(xzunit);
		dao.save(lsunit);
		listBGH.add(djdy_zd.getDJDYID()+":"+djdy_zd.getBDCDYID());
		return listBGH;
	}
	
	/**   
	 * @Title: updateZRZ   
	 * @Description: 更新zrz信息，删除变更前单元一切属性（XZ），登记变更后单元 （不需要拷贝图形了），不构建变更关系
	 * @author:zhaomf
	 * @date:2017年12月11日 下午4:27:49          
	 */ 
	public void updateZRZ()
	{
		CommonDao dao=getCommonDao();
		String strSqlXMBH = ProjectHelper.GetXMBHCondition(getXMBH());
		//获取变更后的信息，更新单元到LS、XZ
		List<RealUnit> listZRZ = UnitTools.loadUnits(BDCDYLX.ZRZ, DJDYLY.GZ, strSqlXMBH);
		if(null!=listZRZ) {
			for (RealUnit realUnit : listZRZ) {
				if(realUnit instanceof BDCS_ZRZ_GZ) {
					BDCS_ZRZ_GZ bdcs_zrz_gz = (BDCS_ZRZ_GZ) realUnit;
					BDCS_ZRZ_XZ bdcs_zrz_xz = ObjectHelper.copyZRZ_GZToXZ(bdcs_zrz_gz);
					dao.save(bdcs_zrz_xz);
					BDCS_ZRZ_LS bdcs_zrz_ls = ObjectHelper.copyZRZ_XZToLS(bdcs_zrz_xz);
					dao.save(bdcs_zrz_ls);
					RealUnit zrz = UnitTools.loadUnit(BDCDYLX.ZRZ, DJDYLY.DC, realUnit.getId());
					zrz.setDJZT(DJZT.YDJ.Value);
					dao.save(zrz);
				}
			}
		}
		//获取变更前单元，删除XZ图形，删除XZ单元(zrz没有登记单元)
		List<BDCS_DJDY_GZ> listdjdy =dao.getDataList(BDCS_DJDY_GZ.class, strSqlXMBH +" and LY ='"+DJDYLY.XZ.Value+"'");
		for (BDCS_DJDY_GZ bdcs_DJDY_GZ : listdjdy) {
			BDCS_H_LS h = (BDCS_H_LS)UnitTools.loadUnit(BDCDYLX.H, DJDYLY.LS, bdcs_DJDY_GZ.getBDCDYID());
			UnitTools.deleteUnit(BDCDYLX.ZRZ, DJDYLY.XZ, h.getZRZBDCDYID());
			logger.info("开始删除自然幢图层："+h.getZRZBDCDYID());
			super.DeleteGeo(h.getZRZBDCDYID(), BDCDYLX.ZRZ, DJDYLY.XZ);
		}
	}
	/**
	 * 移除不动产单元
	 */
	@Override
	public boolean removeBDCDY(String bdcdyid) {
		CommonDao dao = this.getCommonDao();
		BDCS_DJDY_GZ djdy = super.removeDJDY(bdcdyid);
		if (djdy != null) {
			String bdcdylx = djdy.getBDCDYLX();
			String ly = djdy.getLY();
			String djdyid = djdy.getDJDYID();
			if (ly.equals(DJDYLY.GZ.Value)) {
			// 移除具体单元
			RealUnit unit=	UnitTools.deleteUnit(BDCDYLX.initFrom(djdy.getBDCDYLX()), DJDYLY.GZ, bdcdyid);
			if(!StringHelper.isEmpty(unit))
			{
				if(unit instanceof House)
				{
					//判断是否移除自然幢跟地的信息,先要判断调查户的信息全部移除掉
						String zrzbdcdyid=((House) unit).getZRZBDCDYID();
					    List<RealUnit> zrzs=UnitTools.loadUnits(BDCDYLX.ZRZ, DJDYLY.GZ,ProjectHelper.GetXMBHCondition(this.getXMBH()) +" AND BDCDYID ='"+zrzbdcdyid+"'");
					    if(!StringHelper.isEmpty(zrzs) && zrzs.size()>0)
					    {
					      UnitTools.deleteUnit(BDCDYLX.ZRZ, DJDYLY.GZ,  zrzbdcdyid);
					      updateDCDYStatus(BDCDYLX.ZRZ.Value, zrzbdcdyid);
					  	  super.DeleteGeo(zrzbdcdyid, BDCDYLX.ZRZ, DJDYLY.GZ);
					    }
						String zdbdcdyid=((House) unit).getZDBDCDYID();
						  List<RealUnit> zdzs=UnitTools.loadUnits(BDCDYLX.SHYQZD, DJDYLY.GZ, ProjectHelper.GetXMBHCondition(this.getXMBH()) +" AND BDCDYID ='"+zdbdcdyid+"'");
						    if(!StringHelper.isEmpty(zdzs) && zdzs.size()>0)
						    {
						      UnitTools.deleteUnit(BDCDYLX.SHYQZD, DJDYLY.GZ,  zdbdcdyid);
						  	  super.DeleteGeo(zdbdcdyid, BDCDYLX.SHYQZD, DJDYLY.GZ);
						      updateDCDYStatus(BDCDYLX.SHYQZD.Value, zdbdcdyid);
						    }
				}
			}
				// 删除权利、附属权利、权利人、证书、权地证人关系
				String _hqlCondition = MessageFormat.format("XMBH=''{0}'' AND DJDYID=''{1}''", getXMBH(), djdyid);
				RightsTools.deleteRightsAllByCondition(DJDYLY.initFrom(ly), _hqlCondition);

				// 更新调查库相应单元状态
				updateDCDYStatus(bdcdylx, bdcdyid);
			} else {
				List<Rights> qls = RightsTools.loadRightsByCondition(DJDYLY.XZ, "DJDYID='" + djdy.getDJDYID() + "' AND QLLX='" + getQllx().Value + "'");
				if (qls != null && qls.size() > 0) {
					List<BDCS_SQR> sqrs = dao.getDataList(BDCS_SQR.class, "XMBH='" + getXMBH() + "' AND GLQLID='" + qls.get(0).getId() + "'");
					if (sqrs != null && sqrs.size() > 0) {
						for (int isqr = 0; isqr < sqrs.size(); isqr++) {
							dao.deleteEntity(sqrs.get(isqr));
						}
					}
				}
			}
		}
		dao.flush();
		return false;
	}

	/**
	 * 变更登记在登簿后，通过附属权利中的注销业务号及对应的登记类型获取权利信息
	 */
	@Override
	public List<UnitTree> getUnitTree() {
		List<UnitTree> tree = super.getUnitList();
		BDCS_XMXX xmxx = Global.getXMXXbyXMBH(this.getXMBH());
		if (!StringHelper.isEmpty(xmxx) && SF.YES.Value.equals(xmxx.getSFDB())) {
			if (!StringHelper.isEmpty(tree)) {
				for (int i = 0; i < tree.size(); i++) {
					UnitTree t = tree.get(i);
					if (!StringHelper.isEmpty(t)) {
						//获取变更前的权利信息
						if (DJDYLY.LS.Name.toUpperCase().equals(t.getLy().toUpperCase())||DJDYLY.XZ.Name.toUpperCase().equals(t.getLy().toUpperCase())) {
							String fulSql = "select ql.qlid from BDCK.BDCS_QL_LS ql left join BDCK.BDCS_FSQL_LS fsql on "
									+ "fsql.qlid=ql.qlid where fsql.zxdyywh='"
									+ xmxx.getPROJECT_ID()
									+ "' "
									+ "and ql.djdyid='"
									+ t.getDjdyid()
									+ "' and ql.qllx='" + this.getQllx().Value + "'";
							@SuppressWarnings("rawtypes")
							List<Map> lstql = getCommonDao()
									.getDataListByFullSql(fulSql);
							if (!StringHelper.isEmpty(lstql)
									&& lstql.size() > 0) {
								@SuppressWarnings("rawtypes")
								Map m = lstql.get(0);
								String qlid = StringHelper.formatObject(m
										.get("QLID"));
								tree.get(i).setQlid(qlid);
								tree.get(i).setOldqlid(qlid);
								tree.get(i).setLy("ls");
							}
						}
					}
				}
			}
			// 重写
		}else if(!StringHelper.isEmpty(xmxx)){
			if (!StringHelper.isEmpty(tree)) {
				for (int i = 0; i < tree.size(); i++) {
					UnitTree t = tree.get(i);
					if (!StringHelper.isEmpty(t)) {
						if(!DJDYLY.GZ.Name.equals(t.getLy())){
							String qllxarray = " ('1','2','3','4','5','6','7','8','10','11','12','15','24','36')";
							String hqlCondition = MessageFormat.format(" DJDYID IN (SELECT DJDYID FROM BDCS_DJDY_XZ WHERE BDCDYID=''{0}'') AND QLLX IN {1}", t.getBdcdyid(), qllxarray);
							List<Rights> qls=RightsTools.loadRightsByCondition(DJDYLY.XZ, hqlCondition);
							if(qls!=null&&qls.size()>0){
								t.setOldqlid(qls.get(0).getId());
							}
						}
						else
						{
						 //判断自然幢跟宗地信息来源
						if(BDCDYLX.H.equals(this.getBdcdylx()))
						{
							String zdbdcdyid=t.getZdbdcdyid();
							List<RealUnit> zds=UnitTools.loadUnits(BDCDYLX.SHYQZD, DJDYLY.GZ, "BDCDYID ='" +zdbdcdyid +"' and "+ProjectHelper
									.GetXMBHCondition(this.getXMBH()));
							if(!StringHelper.isEmpty(zds) && zds.size()>0)
							{
								t.setZdly("gz");
							}
							else
							{
								t.setZdly("ls");
							}
							String zrzbdcdyid=t.getZrzbdcdyid();
							List<RealUnit> zrzs=UnitTools.loadUnits(BDCDYLX.ZRZ, DJDYLY.GZ, "BDCDYID ='" +zrzbdcdyid +"' and "+ProjectHelper
									.GetXMBHCondition(this.getXMBH()));
							if(!StringHelper.isEmpty(zrzs) && zrzs.size()>0)
							{
								t.setZrzly("gz");
							}
							else
							{
								t.setZrzly("ls");
							}
						}
						}
					}
				}
			}
		}
		return tree;
	}
	/**
	 * 根据申请人ID数组生成权利人
	 */
	@Override
	public void addQLRbySQRArray(String qlid, Object[] sqrids) {
		super.addQLRbySQRs(qlid, sqrids);
	}

	/**
	 * 移除权利人
	 */
	@Override
	public void removeQLR(String qlid, String qlrid) {
		super.removeqlr(qlrid, qlid);
	}

	/**
	 * 获取错误信息
	 * 
	 * @作者 刘树峰
	 * @创建时间 2015年6月23日下午8:11:43
	 * @return
	 */
	@Override
	public String getError() {
		return super.getErrMessage();
	}

	/**
	 * 更新调查库单元的登记状态
	 * @Title: updateDCDYStatus
	 * @author:liushufeng
	 * @date：2015年10月27日 下午3:24:44
	 * @param bdcdylx
	 * @param bdcdyid
	 */
	private void updateDCDYStatus(String bdcdylx, String bdcdyid) {
		BDCDYLX dylx = BDCDYLX.initFrom(bdcdylx);
		RealUnit unit = UnitTools.loadUnit(dylx, DJDYLY.DC, bdcdyid);
		if (unit != null) {
			unit.setDJZT(DJZT.WDJ.Value);
			getCommonDao().update(unit);
		}
	}
/**
	 * 导出交换文件
	 */
	@Override
	public Map<String, String> exportXML(String path, String actinstID) {
		Marshaller mashaller;
		Map<String, String> names = new HashMap<String, String>();
		try {
			CommonDao dao = super.getCommonDao();
			String xmbhHql = ProjectHelper.GetXMBHCondition(super.getXMBH());
			List<BDCS_DJDY_GZ> djdys = dao.getDataList(BDCS_DJDY_GZ.class, xmbhHql + " and LY = '" + ConstValue.DJDYLY.GZ.Value + "' ");
			List<BDCS_DJDY_GZ> bgqdjdys = dao.getDataList(BDCS_DJDY_GZ.class, xmbhHql + " and LY = '" + ConstValue.DJDYLY.XZ.Value + "' ");
			String msgName = "";
			if (djdys != null && djdys.size() > 0) {
				mashaller = JAXBContext.newInstance(Message.class).createMarshaller();
				Calendar calendar = Calendar.getInstance(); // 创建一个日历对象
				calendar.setTime(new Date()); // 用当前时间初始化日历时间
//				String cyear = calendar.get(Calendar.YEAR) + "";
				String ywh = packageXml.GetYWLSHByYWH(this.getProject_id());
				BDCS_XMXX xmxx = dao.get(BDCS_XMXX.class, super.getXMBH());
				String result = "";
				for (int i = 0; i < djdys.size(); i++) {
					BDCS_DJDY_GZ djdy = djdys.get(i);
					BDCS_QL_GZ ql = super.getQL(djdy.getDJDYID());
					List<BDCS_QLR_GZ> qlrs = super.getQLRs(ql.getId());
					List<BDCS_SQR> sqrs = new ArrayList<BDCS_SQR>();
					ProjectServiceImpl serviceImpl = SuperSpringContext.getContext().getBean(ProjectServiceImpl.class);
					sqrs = serviceImpl.getSQRList(super.getXMBH());

					if (QLLX.GYJSYDSHYQ.Value.equals(this.getQllx().Value) || QLLX.ZJDSYQ.Value.equals(this.getQllx().Value) || QLLX.JTJSYDSYQ.Value.equals(this.getQllx().Value)) { // 国有建设使用权、宅基地、集体建设用地使用权
						try {
							BDCS_SHYQZD_GZ zd = dao.get(BDCS_SHYQZD_GZ.class, djdy.getBDCDYID());
							String preEstateNum = "";
							// 目前未维护的字段先手动赋值 diaoliwei 2015-10-15
							if (null != zd) {
								if (StringUtils.isEmpty(zd.getZDT())) {
									zd.setZDT("无");
								}
							}
							for (int j = 0; j < bgqdjdys.size(); j++) {
								BDCS_DJDY_GZ bgqdjdy = bgqdjdys.get(j);
								BDCS_SHYQZD_XZ zd_XZ = dao.get(BDCS_SHYQZD_XZ.class, bgqdjdy.getBDCDYID());
								if (zd_XZ != null && (j == 0 || j == bgqdjdys.size() - 1)) {
									preEstateNum += zd_XZ.getBDCDYH();
								} else {
									preEstateNum += zd_XZ.getBDCDYH() + ",";
								}
							}
							// 标记单元号是否发生变化了，是否取消空间节点的存在
							boolean flag = false;
							if (!(zd.getBDCDYH().equals(preEstateNum)) && !StringUtils.isEmpty(preEstateNum)) {
								flag = true;
							}
							Message msg = exchangeFactory.createMessage(flag);

							super.fillHead(msg, i, ywh,zd.getBDCDYH(),zd.getQXDM(),ql.getLYQLID());

							BDCS_DYBG dybg = packageXml.getDYBG(zd.getId());
							msg.getHead().setParcelID(StringHelper.formatObject(zd.getZDDM()));
							msg.getHead().setEstateNum(StringHelper.formatObject(zd.getBDCDYH()));// 当前的不动产单元号
//							msg.getHead().setPreEstateNum(preEstateNum);// 上一首的不动产单元号
							if (zd != null && !StringUtils.isEmpty(zd.getQXDM())) {
								msg.getHead().setAreaCode(zd.getQXDM());
							}

							if (djdy != null) {

								List<ZTTGYQLR> zttqlr = msg.getData().getGYQLR();
								zttqlr = packageXml.getZTTGYQLRs(zttqlr, qlrs, zd, ql, null, null, null, null);
								msg.getData().setGYQLR(zttqlr);

								KTTZDJBXX jbxx = msg.getData().getKTTZDJBXX();
								jbxx = packageXml.getZDJBXX(jbxx, zd, ql, null, null);

								String zdzhdm = "";
								if (zd != null) {
									zdzhdm = zd.getZDDM();
								}

								KTFZDBHQK bhqk = msg.getData().getZDBHQK();
								bhqk = packageXml.getKTFZDBHQK(bhqk, zd, ql, null, null);
								msg.getData().setZDBHQK(bhqk);

								KTTGYJZX jzx = msg.getData().getKTTGYJZX();
								jzx = packageXml.getKTTGYJZX(jzx, zdzhdm);

								KTTGYJZD jzd = msg.getData().getKTTGYJZD();
								jzd = packageXml.getKTTGYJZD(jzd, zdzhdm);

								QLFQLJSYDSYQ syq = msg.getData().getQLJSYDSYQ();
								syq = packageXml.getQLFQLJSYDSYQ(syq, zd, ql, ywh);

								DJTDJSLSQ sq = msg.getData().getDJSLSQ();
								sq = packageXml.getDJTDJSLSQ(sq, ywh, zd, ql, xmxx, null, xmxx.getSLSJ(), null, null, null);

								List<DJFDJSJ> sj = msg.getData().getDJSJ();
								sj = packageXml.getDJFDJSJ(zd, ywh,actinstID);
								msg.getData().setDJSJ(sj);

								List<DJFDJSF> sfList = msg.getData().getDJSF();
								sfList = packageXml.getDJSF(zd,ywh, this.getXMBH());
								msg.getData().setDJSF(sfList);

								List<DJFDJSH> sh = msg.getData().getDJSH();
								sh = packageXml.getDJFDJSH(zd, ywh, this.getXMBH(), actinstID);
								msg.getData().setDJSH(sh);

								List<DJFDJSZ> sz = packageXml.getDJFDJSZ(zd, ywh, this.getXMBH());
								msg.getData().setDJSZ(sz);

								List<DJFDJFZ> fz = packageXml.getDJFDJFZ(zd, ywh, this.getXMBH());
								msg.getData().setDJFZ(fz);
								List<DJFDJGD> gd = packageXml.getDJFDJGD(zd, ywh,this.getXMBH());
								msg.getData().setDJGD(gd);
								
								BDCS_SHYQZD_GZ zd2 = null;
								if (null != dybg) {
									zd2 = new BDCS_SHYQZD_GZ();
									zd2.setId(dybg.getLBDCDYID());
//								zd.setId(dybg.getLBDCDYID());
								}
								// 如果当前不动产单元号和上一个不动产单元号不同的话，把空间节点加进去
								if (!(preEstateNum.equals(zd.getBDCDYH()))) {
									List<ZDK103> zdk = msg.getData().getZDK103();
									zdk = packageXml.getZDK103(zdk, zd2, null, null);
									msg.getData().setZDK103(zdk);
								}
								List<DJFDJSQR> djsqrs = msg.getData().getDJSQR();
								djsqrs = packageXml.getDJSQRs(djsqrs, sqrs, zd.getYSDM(), ywh, zd.getBDCDYH());
								msg.getData().setDJSQR(djsqrs);

								FJF100 fj = msg.getData().getFJF100();
								fj = packageXml.getFJF(fj);
							}
							msgName = "Biz" + msg.getHead().getBizMsgID() + ".xml";
							mashaller.marshal(msg, new File(path + msgName));
							result = uploadFile(path + msgName, ConstValue.RECCODE.JSYDSHYQ_BGDJ.Value,actinstID,djdy.getDJDYID(),ql.getId());
							names.put(djdy.getDJDYID(), msg.getHead().getBizMsgID() + ".xml");
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					if (QLLX.GYJSYDSHYQ_FWSYQ.Value.equals(this.getQllx().Value)) { // 房屋所有权
						try {
							BDCS_H_GZ h = dao.get(BDCS_H_GZ.class, djdy.getBDCDYID());
							BDCS_ZRZ_XZ zrz_gz = null;
							if (h != null && !StringUtils.isEmpty(h.getZRZBDCDYID())) {
								zrz_gz = dao.get(BDCS_ZRZ_XZ.class, h.getZRZBDCDYID());
								if(zrz_gz != null){
									zrz_gz.setGHYT(h.getGHYT()); //自然幢的ghyt取户的ghyt
									zrz_gz.setFWJG(zrz_gz.getFWJG() == null || zrz_gz.getFWJG().equals("") ? h.getFWJG() : zrz_gz.getFWJG());
								}
							}
							BDCS_LJZ_XZ ljz_gz = null;
							if (h != null && !StringUtils.isEmpty(h.getLJZID())) {
								ljz_gz = dao.get(BDCS_LJZ_XZ.class, h.getLJZID());
							}
							if (h != null) {
								BDCS_SHYQZD_XZ zd = dao.get(BDCS_SHYQZD_XZ.class, h.getZDBDCDYID());
								h.setZDDM(zd.getZDDM());
								if(zrz_gz != null){
									zrz_gz.setZDDM(zd.getZDDM());
								}
							}
							Message msg = exchangeFactory.createMessageByFWSYQ();
							super.fillHead(msg, i, ywh,h.getBDCDYH(),h.getQXDM(),ql.getLYQLID());
							msg.getHead().setParcelID(StringHelper.formatObject(h.getZDDM()));
							msg.getHead().setEstateNum(StringHelper.formatObject(h.getBDCDYH()));
							if (h != null && !StringUtils.isEmpty(h.getQXDM())) {
								msg.getHead().setAreaCode(h.getQXDM());
							}
							String preEstateNum = "";
							for (BDCS_DJDY_GZ bgqdjdy : bgqdjdys) {
								BDCS_H_LS h_LS = dao.get(BDCS_H_LS.class, bgqdjdy.getBDCDYID());
								if (!StringUtils.isEmpty(h.getId()) && h.getId().equals(h_LS.getId())) {
									preEstateNum = h_LS.getBDCDYH();
									break;
								}
							}
//							msg.getHead().setPreEstateNum(preEstateNum);
							BDCS_C_GZ c = null;
							if (h != null && !StringUtils.isEmpty(h.getCID())) {
								c = dao.get(BDCS_C_GZ.class, h.getCID());
							}

							if (djdy != null) {
								List<ZTTGYQLR> zttqlr = msg.getData().getGYQLR();
								zttqlr = packageXml.getZTTGYQLRs(zttqlr, qlrs, null, ql, h, null, null, null);
								msg.getData().setGYQLR(zttqlr);

								KTTFWZRZ zrz = msg.getData().getKTTFWZRZ();
								zrz = packageXml.getKTTFWZRZ(zrz, zrz_gz);

								KTTFWLJZ ljz = msg.getData().getKTTFWLJZ();
								ljz = packageXml.getKTTFWLJZ(ljz, ljz_gz,h);

								KTTFWC kttc = msg.getData().getKTTFWC();
								kttc = packageXml.getKTTFWC(kttc, c, zrz);
								msg.getData().setKTTFWC(kttc);

								KTTFWH fwh = msg.getData().getKTTFWH();
								fwh = packageXml.getKTTFWH(fwh, h);
								msg.getData().setKTTFWH(fwh);

								QLTFWFDCQYZ fdcqyz = msg.getData().getQLTFWFDCQYZ();
								fdcqyz = packageXml.getQLTFWFDCQYZ(fdcqyz, h, ql, ywh);

								List<ZDK103> fwk = msg.getData().getZDK103();
								fwk = packageXml.getZDK103H(fwk, h, zrz_gz);

								DJTDJSLSQ sq = msg.getData().getDJSLSQ();
								sq = packageXml.getDJTDJSLSQ(sq, ywh, null, ql, xmxx, h, xmxx.getSLSJ(), null, null, null);

								List<DJFDJSJ> sj = msg.getData().getDJSJ();
								sj = packageXml.getDJFDJSJ(h, ywh,actinstID);
								msg.getData().setDJSJ(sj);

								List<DJFDJSF> sfList = msg.getData().getDJSF();
								sfList = packageXml.getDJSF(h,ywh, this.getXMBH());
								msg.getData().setDJSF(sfList);

								List<DJFDJSH> sh = msg.getData().getDJSH();
								sh = packageXml.getDJFDJSH(h, ywh, this.getXMBH(), actinstID);
								msg.getData().setDJSH(sh);

								List<DJFDJSZ> sz = packageXml.getDJFDJSZ(h, ywh, this.getXMBH());
								msg.getData().setDJSZ(sz);
								List<DJFDJFZ> fz = packageXml.getDJFDJFZ(h, ywh, this.getXMBH());
								msg.getData().setDJFZ(fz);
								List<DJFDJGD> gd = packageXml.getDJFDJGD(h, ywh,this.getXMBH());
								msg.getData().setDJGD(gd);

								List<DJFDJSQR> djsqrs = msg.getData().getDJSQR();
								djsqrs = packageXml.getDJSQRs(djsqrs, sqrs, h.getYSDM(), ywh, h.getBDCDYH());
								msg.getData().setDJSQR(djsqrs);

								FJF100 fj = msg.getData().getFJF100();
								fj = packageXml.getFJF(fj);
							}
							msgName = "Biz" + msg.getHead().getBizMsgID() + ".xml";
							mashaller.marshal(msg, new File(path + msgName));
							result = uploadFile(path + msgName, ConstValue.RECCODE.FDCQDZ_BGDJ.Value,actinstID,djdy.getDJDYID(),ql.getId());
							names.put(djdy.getDJDYID(), msg.getHead().getBizMsgID() + ".xml");
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					if(null == result){
						Map<String, String> xmlError = new HashMap<String, String>();
						if (QLLX.JTTDSYQ.Value.equals(this.getQllx().Value)) {
							YwLogUtil.addSjsbResult(msgName, "", "连接SFTP失败,请检查服务器和前置机的连接是否正常", ConstValue.SF.NO.Value, ConstValue.RECCODE.TDSYQ_BGDJ.Value,ProjectHelper.getpRroinstIDByActinstID(actinstID));	
						}else if (QLLX.GYJSYDSHYQ_FWSYQ.Value.equals(this.getQllx().Value)){
							YwLogUtil.addSjsbResult(msgName, "", "连接SFTP失败,请检查服务器和前置机的连接是否正常", ConstValue.SF.NO.Value, ConstValue.RECCODE.FDCQDZ_BGDJ.Value,ProjectHelper.getpRroinstIDByActinstID(actinstID));
						}else {
							YwLogUtil.addSjsbResult(msgName, "", "连接SFTP失败,请检查服务器和前置机的连接是否正常", ConstValue.SF.NO.Value, ConstValue.RECCODE.JSYDSHYQ_BGDJ.Value,ProjectHelper.getpRroinstIDByActinstID(actinstID));
						}
						return xmlError;
					}
					if(!"1".equals(result) && result.indexOf("success") == -1){ //xml本地校验不通过时
						Map<String, String> xmlError = new HashMap<String, String>();
						xmlError.put("error", result);
						return xmlError;
					}
					if(!StringUtils.isEmpty(result) && result.indexOf("success") > -1 && !names.containsKey("reccode")){
						names.put("reccode", result);
					}
				}
			}
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return names;
	}

	/**
	 * 共享信息
	 * 
	 * @作者 俞学斌
	 * @创建时间 2015年8月16日下午16:37:43
	 * @return
	 */
	@Override
	public void SendMsg(String bljc) {
		BDCS_XMXX xmxx = getCommonDao().get(BDCS_XMXX.class, super.getXMBH());
		String xmbhFilter = ProjectHelper.GetXMBHCondition(super.getXMBH());
		List<BDCS_DJDY_GZ> djdys = getCommonDao().getDataList(BDCS_DJDY_GZ.class, xmbhFilter);
		if (djdys != null && djdys.size() > 0) {
			for (int idjdy = 0; idjdy < djdys.size(); idjdy++) {
				BDCS_DJDY_GZ djdy = djdys.get(idjdy);
				ConstValue.BDCDYLX dylx = ConstValue.BDCDYLX.initFrom(djdy.getBDCDYLX());
				ConstValue.DJDYLY dyly = ConstValue.DJDYLY.initFrom(djdy.getLY());
				RealUnit bdcdy = UnitTools.loadUnit(dylx, dyly, djdy.getBDCDYID());
				Rights bdcql = RightsTools.loadRightsByDJDYID(dyly, getXMBH(), djdy.getDJDYID());
				SubRights bdcfsql = RightsTools.loadSubRightsByRightsID(dyly, bdcql.getId());
				List<RightsHolder> bdcqlrs = RightsHolderTools.loadRightsHolders(dyly, djdy.getDJDYID(), getXMBH());
				MessageExport msg = super.getShareMsgTools().GetMsg(bdcdy, bdcql, bdcfsql, bdcqlrs, bljc, xmxx);
				super.getShareMsgTools().SendMsg(msg, idjdy + 1, djdy.getBDCDYLX(), xmxx);
			}
		}
	}
}
