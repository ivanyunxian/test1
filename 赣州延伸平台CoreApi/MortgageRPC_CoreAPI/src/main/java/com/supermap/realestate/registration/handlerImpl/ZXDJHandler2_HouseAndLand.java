package com.supermap.realestate.registration.handlerImpl;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apache.commons.beanutils.PropertyUtils;
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
import com.supermap.realestate.registration.dataExchange.exchangeFactory;
import com.supermap.realestate.registration.dataExchange.utils.packageXml;
import com.supermap.realestate.registration.dataExchange.zxdj.QLFZXDJ;
import com.supermap.realestate.registration.handler.DJHandler;
import com.supermap.realestate.registration.model.BDCS_DJDY_GZ;
import com.supermap.realestate.registration.model.BDCS_DJDY_LS;
import com.supermap.realestate.registration.model.BDCS_DJDY_XZ;
import com.supermap.realestate.registration.model.BDCS_FSQL_GZ;
import com.supermap.realestate.registration.model.BDCS_FSQL_LS;
import com.supermap.realestate.registration.model.BDCS_H_XZ;
import com.supermap.realestate.registration.model.BDCS_H_XZY;
import com.supermap.realestate.registration.model.BDCS_QLR_XZ;
import com.supermap.realestate.registration.model.BDCS_QL_GZ;
import com.supermap.realestate.registration.model.BDCS_QL_LS;
import com.supermap.realestate.registration.model.BDCS_QL_XZ;
import com.supermap.realestate.registration.model.BDCS_SHYQZD_XZ;
import com.supermap.realestate.registration.model.BDCS_SQR;
import com.supermap.realestate.registration.model.BDCS_XMXX;
import com.supermap.realestate.registration.model.BDCS_YHZK_GZ;
import com.supermap.realestate.registration.model.BDCS_YHZK_XZ;
import com.supermap.realestate.registration.model.BDCS_ZH_GZ;
import com.supermap.realestate.registration.model.BDCS_ZH_XZ;
import com.supermap.realestate.registration.model.interfaces.House;
import com.supermap.realestate.registration.model.interfaces.RealUnit;
import com.supermap.realestate.registration.model.interfaces.Rights;
import com.supermap.realestate.registration.model.interfaces.RightsHolder;
import com.supermap.realestate.registration.model.interfaces.Sea;
import com.supermap.realestate.registration.model.interfaces.SubRights;
import com.supermap.realestate.registration.model.interfaces.YHZK;
import com.supermap.realestate.registration.model.xmlExportmodel.MessageExport;
import com.supermap.realestate.registration.service.impl.ProjectServiceImpl;
import com.supermap.realestate.registration.tools.RightsHolderTools;
import com.supermap.realestate.registration.tools.RightsTools;
import com.supermap.realestate.registration.tools.UnitTools;
import com.supermap.realestate.registration.util.ConfigHelper;
import com.supermap.realestate.registration.util.ConstValue;
import com.supermap.realestate.registration.util.ConstValue.BDCDYLX;
import com.supermap.realestate.registration.util.ConstValue.DJDYLY;
import com.supermap.realestate.registration.util.ConstValue.QLLX;
import com.supermap.realestate.registration.util.ConstValue.SQRLB;
import com.supermap.realestate.registration.util.Global;
import com.supermap.realestate.registration.util.ObjectHelper;
import com.supermap.realestate.registration.util.ProjectHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.realestate.registration.util.YwLogUtil;
import com.supermap.wisdombusiness.core.SuperSpringContext;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;

/*
 1、国有建设用地使用权/房屋所有权-房地一体灭失登记
 2、集体建设用地使用权/房屋所有权-房地一体灭失登记
 3、宅基地使用权/房屋所有权-房地一体灭失登记
 */

/**
 * 房地一体的灭失登记
 * @author shb
 *
 */
public class ZXDJHandler2_HouseAndLand extends ZXDJHandler2 implements DJHandler {

	/**
	 * 构造函数
	 * 
	 * @param info
	 */
	public ZXDJHandler2_HouseAndLand(ProjectInfo info) {
		super(info);
	}

	/**
	 * 添加不动产单元
	 */
	@Override
	public boolean addBDCDY(String bdcdyid) {
		// 注销登记也生成权利记录，只不过权利里边都是空的
		boolean bsuccess = false;
		CommonDao dao = getCommonDao();
		BDCS_DJDY_GZ djdy = super.createDJDYfromXZ(bdcdyid);
		djdy.setLY(DJDYLY.LS.Value);
		if (djdy != null) {
			RealUnit unit = null;
			try {
				UnitTools.loadUnit(this.getBdcdylx(), DJDYLY.initFrom(djdy.getLY()), djdy.getBDCDYID());
			} catch (Exception e) {
			}

			BDCS_QL_GZ ql = super.createQL(djdy, unit);
			// 生成附属权利
			BDCS_FSQL_GZ fsql = super.createFSQL(djdy.getDJDYID());
			fsql.setQLID(ql.getId());
			ql.setFSQLID(fsql.getId());
			List<Rights> rightss = (List<Rights>) RightsTools.loadRightsByCondition(DJDYLY.XZ, "DJDYID='" + djdy.getDJDYID() + "' AND QLLX IN ('4','6','8')");
			if (rightss != null && rightss.size() > 0) {
				Rights rights = rightss.get(0);
				if (rights != null) {
					ql.setLYQLID(rights.getId());
					List<RightsHolder> holders = RightsHolderTools.loadRightsHolders(DJDYLY.XZ, rights.getId());
					if (holders != null && holders.size() > 0) {
						for (RightsHolder holder : holders) {
							BDCS_QLR_XZ qlr = (BDCS_QLR_XZ) holder;
							BDCS_SQR sqr = super.copyXZQLRtoSQR(qlr, SQRLB.JF);
							if(sqr!=null){
								getCommonDao().save(sqr);
							}
						}
					}
				}
			}

			// 保存权利和附属权利
			dao.save(ql);
			dao.save(fsql);
			dao.save(djdy);
		}
		dao.flush();
		bsuccess = true;
		return bsuccess;
	}

	/**
	 * 写入登记薄
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public boolean writeDJB() {
		/*
		 * 登簿逻辑：循环工作层 ，得到登记单元，去删除现状层对应的权利，权利人，证书，权地证人然后把工作层里的权利拷贝到历史层
		 * 登簿逻辑：循环工作层，得到登记单元，去删除现状层对应的权利，权利人，证书及权地证人，并且删除相应自然幢、宗地对应的权利、权利人、证书及权地证人，并修改相应的权利为已注销
		 */
		CommonDao dao = getCommonDao();
		String dbr = Global.getCurrentUserName();
		String strSqlXMBH = ProjectHelper.GetXMBHCondition(getXMBH());
		List<BDCS_DJDY_GZ> djdys = dao.getDataList(BDCS_DJDY_GZ.class, strSqlXMBH);
		if (djdys!=null&&djdys.size()>0) {
			for(BDCS_DJDY_GZ djdy_gz:djdys) {
				BDCS_DJDY_LS ls=ObjectHelper.copyDJDY_XZToLS(ObjectHelper.copyDJDY_GZToXZ(djdy_gz));
				ls.setId(UUID.randomUUID().toString());
				dao.save(ls);
			}
		}
		if (djdys != null && djdys.size() > 0) {
			for (int i = 0; i < djdys.size(); i++) {
				BDCS_DJDY_GZ djdy = djdys.get(i);
				String qllxarray = " ('4','6','8')";
				String djdyid = djdy.getDJDYID();
				String sql = MessageFormat.format(" DJDYID=''{0}'' AND QLLX IN {1}", djdyid, qllxarray);
				// 删除现状层中的权利，权利人，证书等等。
				List<BDCS_QL_XZ> xzqls = dao.getDataList(BDCS_QL_XZ.class, sql);
				if (xzqls != null && xzqls.size() > 0) {
					for (int j = 0; j < xzqls.size(); j++) {
						BDCS_QL_XZ xzql = xzqls.get(j);
						if (xzql != null) {
							super.removeQLXXFromXZByQLID(xzql.getId());
						}
					}
				}
				//删除现状层中单元、登记单元
				List<BDCS_DJDY_XZ> djdys_xz=dao.getDataList(BDCS_DJDY_XZ.class, "DJDYID='"+djdyid+"'");
				if(djdys_xz!=null&&djdys_xz.size()>0){
					List zrzbdcdyids=new ArrayList(); 
					List zdbdcdyids=new ArrayList();
					for(BDCS_DJDY_XZ djdy_xz:djdys_xz){
						String bdcdyid=djdy_xz.getBDCDYID();
						if(!StringHelper.isEmpty(bdcdyid)){
							RealUnit unit_xz=UnitTools.loadUnit(BDCDYLX.initFrom(djdy_xz.getBDCDYLX()), DJDYLY.XZ, bdcdyid);
							if(unit_xz!=null){
								dao.deleteEntity(unit_xz);
								if(unit_xz instanceof House){
									House house = (House) unit_xz;
									String zrzbdcdyid=house.getZRZBDCDYID();
									String zdbdcyid=house.getZDBDCDYID();
									//去重自然幢及宗地信息
									if(!zrzbdcdyids.contains(zrzbdcdyid) && !StringHelper.isEmpty(zrzbdcdyid)){
										zrzbdcdyids.add(zrzbdcdyid);
									}
									if(!zdbdcdyids.contains(zdbdcyid) && !StringHelper.isEmpty(zdbdcyid)){
										zdbdcdyids.add(zdbdcyid);
									}
								}
							}
						}
						dao.deleteEntity(djdy_xz);
					}
					dao.flush();
					if(zrzbdcdyids.size()>0 && djdys_xz!=null && djdys_xz.size()>0){
						for(int k=0;k<zrzbdcdyids.size();k++){
							if(!StringHelper.isEmpty(zrzbdcdyids.get(k))){
								String bdcdylx=djdys_xz.get(0).getBDCDYLX();
								String zrzbdcdyid=zrzbdcdyids.get(k).toString();
								if(bdcdylx.equals("031")){
									List<BDCS_H_XZ> hxz=dao.getDataList(BDCS_H_XZ.class, "ZRZBDCDYID='"+zrzbdcdyid+"'");
									if(hxz.size()==0){
										RealUnit unit_zrz=UnitTools.loadUnit(BDCDYLX.ZRZ, DJDYLY.XZ, zrzbdcdyid);
										if(unit_zrz!=null){
											dao.deleteEntity(unit_zrz);
											super.DeleteGeo(zrzbdcdyid, BDCDYLX.initFrom(bdcdylx), DJDYLY.XZ);
										}
									}
								}else{
									List<BDCS_H_XZY> hxzy=dao.getDataList(BDCS_H_XZY.class, "ZRZBDCDYID='"+zrzbdcdyid+"'");
									if(hxzy.size()==0){
										RealUnit unit_zrz=UnitTools.loadUnit(BDCDYLX.YCZRZ, DJDYLY.XZ, zrzbdcdyid);
										if(unit_zrz!=null){
											dao.deleteEntity(unit_zrz);
											super.DeleteGeo(zrzbdcdyid, BDCDYLX.YCZRZ, DJDYLY.XZ);
										}
									}
								}
							}
						}
					}
					if(zdbdcdyids !=null && zdbdcdyids.size()>0 && djdys_xz!=null && djdys_xz.size()>0){
						String bdcdylx=djdys_xz.get(0).getBDCDYLX();
						for(int j=0;j<zdbdcdyids.size();j++){
							String zdbdcdyid=zdbdcdyids.get(j).toString();
							if(BDCDYLX.H.Value.equals(bdcdylx)){
								List<BDCS_H_XZ> hxz=dao.getDataList(BDCS_H_XZ.class, "zdbdcdyid='"+zdbdcdyid+"'");
								if( hxz.size() ==0){
									UpdatewriteLand(zdbdcdyid);
								}
							}else if(BDCDYLX.YCH.Value.equals(bdcdylx)){
								List<BDCS_H_XZY> hxzy=dao.getDataList(BDCS_H_XZY.class, "zdbdcdyid='"+zdbdcdyid+"'");
								if(hxzy.size() ==0){
									UpdatewriteLand(zdbdcdyid);
								}
							}
						}
					}
				}

				// 登记时间，登簿人，登记机构。
				BDCS_QL_GZ gzql = super.getQL(djdy.getDJDYID());
				if (gzql != null) {
					gzql.setDJSJ(new Date());
					gzql.setDBR(dbr);
					gzql.setDJJG(ConfigHelper.getNameByValue("DJJGMC"));
					dao.update(gzql);
				}

				BDCS_QL_LS lsql = new BDCS_QL_LS();
				// 拷贝到历史层
				try {
					PropertyUtils.copyProperties(lsql, gzql);
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					e.printStackTrace();
				}
				
				dao.save(lsql);
				
				if (gzql != null) {
					// 登记时间，登簿人，登记机构。
					BDCS_FSQL_GZ gzfsql = dao.get(BDCS_FSQL_GZ.class, gzql.getFSQLID());
					if (gzfsql != null) {
						gzfsql.setZXSJ(new Date());
						gzfsql.setZXDBR(dbr);
						dao.update(gzfsql);
					}

					BDCS_FSQL_LS lsfsql = new BDCS_FSQL_LS();
					// 拷贝到历史层
					try {
						PropertyUtils.copyProperties(lsfsql, gzfsql);
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						e.printStackTrace();
					} catch (NoSuchMethodException e) {
						e.printStackTrace();
					}
					dao.save(lsfsql);
				}
			}
		}

		super.SetSFDB();
		super.alterCachedXMXX();
		dao.flush();
		return true;
	}

	/**
	 * 更新宗地的登簿信息
	 * @param zdbdcdyid 宗地的主键
	 */
	private void UpdatewriteLand(String zdbdcdyid){
		CommonDao dao = getCommonDao();
		List<BDCS_DJDY_XZ> djdys=dao.getDataList(BDCS_DJDY_XZ.class, "bdcdyid='"+zdbdcdyid+"'");
		if (djdys!=null&&djdys.size()>0) {
			for(BDCS_DJDY_XZ djdy_gz:djdys) {
				BDCS_DJDY_LS ls=ObjectHelper.copyDJDY_XZToLS(djdy_gz);
				ls.setId(UUID.randomUUID().toString());
				dao.save(ls);
			}
		}
		if(djdys !=null && djdys.size()>0){
			String djdyid=djdys.get(0).getDJDYID();
			String bdcdylx=djdys.get(0).getBDCDYLX();
			String qllxarray = " ('1','2','3','5','7','9','10','11','12','13','14','15','16','17','18')";
			String sql = MessageFormat.format(" DJDYID=''{0}'' AND QLLX IN {1}", djdyid, qllxarray);
			// 删除现状层中的权利，权利人，证书等等。
			List<BDCS_QL_XZ> xzqls = dao.getDataList(BDCS_QL_XZ.class, sql);
			if (xzqls != null && xzqls.size() > 0) {
				for (int j = 0; j < xzqls.size(); j++) {
					BDCS_QL_XZ xzql = xzqls.get(j);
					if (xzql != null) {
						super.removeQLXXFromXZByQLID(xzql.getId());
					}
				}
			}
			RealUnit unit_xz=UnitTools.loadUnit(BDCDYLX.initFrom(bdcdylx), DJDYLY.XZ, zdbdcdyid);
			if(unit_xz!=null){
				dao.deleteEntity(unit_xz);
				super.DeleteGeo(zdbdcdyid, BDCDYLX.initFrom(bdcdylx), DJDYLY.XZ);
			}
			dao.deleteEntity(djdys.get(0));
			dao.flush();
		}
	}
	@Override
	public Map<String, String> exportXML(String path, String actinstID) {



		Message msg=null;
		String result = "";
		Marshaller mashaller;
		Map<String, String> names = new HashMap<String, String>();
		CommonDao dao = super.getCommonDao();
		String xmbhHql = ProjectHelper.GetXMBHCondition(super.getXMBH());
		List<BDCS_DJDY_GZ> djdys = dao.getDataList(BDCS_DJDY_GZ.class, xmbhHql);
			//房地一体路程查询历史层
			List<BDCS_DJDY_LS> djdys_ls=dao.getDataList(BDCS_DJDY_LS.class, xmbhHql);
			BDCS_DJDY_GZ gz=null;
			if(djdys_ls!=null&&djdys_ls.size()>0) {
				for(BDCS_DJDY_LS ls:djdys_ls) {
					try {
						
						if(ls.getBDCDYLX()!=null&&!"".equals(ls.getBDCDYLX())&&"02".equals(ls.getBDCDYLX())) {
							gz=new BDCS_DJDY_GZ();
							PropertyUtils.copyProperties(gz, ls);
						}
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						e.printStackTrace();
					} catch (NoSuchMethodException e) {
						e.printStackTrace();
					}
					if(gz!=null) {
						djdys.add(gz);
					}
				}
			} 
	  
		
		try {
			if (djdys != null && djdys.size() > 0) {
				mashaller = JAXBContext.newInstance(Message.class).createMarshaller();
				Calendar calendar = Calendar.getInstance(); // 创建一个日历对象
				calendar.setTime(new Date()); // 用当前时间初始化日历时间
				String cyear = calendar.get(Calendar.YEAR) + "";
				
				for (int i = 0; i < djdys.size(); i++) {
					BDCS_DJDY_GZ djdy = djdys.get(i);
					String ywh = packageXml.GetYWLSHByYWH(super.getProject_id());
					BDCS_QL_GZ ql = super.getQL(djdy.getDJDYID());
					// List<BDCS_QLR_GZ> qlrs = super.getQLRs(ql.getId());
					List<BDCS_SQR> sqrs = new ArrayList<BDCS_SQR>();
					 msg = exchangeFactory.createMessageByZXDJ();
					ProjectServiceImpl serviceImpl = SuperSpringContext.getContext().getBean(ProjectServiceImpl.class);
					sqrs = serviceImpl.getSQRList(super.getXMBH());

					BDCS_XMXX xmxx = dao.get(BDCS_XMXX.class, super.getXMBH());
					BDCS_H_XZ h = null;
//					System.out.println(super.getQllx().Value);
//					System.out.println(super.getBdcdylx().toString());
					if (djdy != null) {
						if (QLLX.GYJSYDSHYQ.Value.equals(super.getQllx().Value) || QLLX.ZJDSYQ.Value.equals(super.getQllx().Value) || QLLX.JTJSYDSYQ.Value.equals(super.getQllx().Value)
								||"SHYQZD".equals(super.getBdcdylx().toString())||"02".equals(djdy.getBDCDYLX())) { // 国有建设使用权、宅基地、集体建设用地使用权
							try {
								BDCS_SHYQZD_XZ zd = dao.get(BDCS_SHYQZD_XZ.class, djdy.getBDCDYID());
								
								if(zd != null && !StringUtils.isEmpty(zd.getQXDM())){
									msg.getHead().setAreaCode(zd.getQXDM());
								}
								super.fillHead(msg, i, ywh,zd.getBDCDYH(),zd.getQXDM(),ql.getLYQLID());
								Rights rights = ql;
								QLFZXDJ zxdj = msg.getData().getZXDJ();
								SubRights subrights=RightsTools.loadSubRights(DJDYLY.GZ, ql.getFSQLID());
								zxdj = packageXml.getZXDJ(zxdj, rights, ywh, zd.getQXDM(),subrights);

								DJTDJSLSQ sq = msg.getData().getDJSLSQ();
								sq = packageXml.getDJTDJSLSQ(sq, ywh, zd, ql, xmxx, null, xmxx.getSLSJ(), null, null, null);

								List<DJFDJSJ> sj = msg.getData().getDJSJ();
								sj = packageXml.getDJFDJSJ(zd, ywh,actinstID);
								msg.getData().setDJSJ(sj);

								//9.登记收费(可选)
								List<DJFDJSF> sfList = msg.getData().getDJSF();
								sfList = packageXml.getDJSF(zd, ywh,this.getXMBH());
								msg.getData().setDJSF(sfList);

								//10.审核(可选)
								List<DJFDJSH> sh = msg.getData().getDJSH();
								 sh = packageXml.getDJFDJSH(zd, ywh, this.getXMBH(), actinstID);
								msg.getData().setDJSH(sh);

								//11.缮证(可选)
								List<DJFDJSZ>  sz = packageXml.getDJFDJSZ(zd, ywh, this.getXMBH());
								msg.getData().setDJSZ(sz);

								//11.发证(可选)
								List<DJFDJFZ>   fz = packageXml.getDJFDJFZ(zd, ywh, this.getXMBH());
								msg.getData().setDJFZ(fz);
								
								//12.归档(可选)
								List<DJFDJGD> gd = packageXml.getDJFDJGD(zd, ywh, this.getXMBH());
								msg.getData().setDJGD(gd);	
								
								msg.getHead().setParcelID(StringHelper.formatObject(zd.getZDDM()));
								msg.getHead().setEstateNum(StringHelper.formatObject(zd.getBDCDYH()));
//								msg.getHead().setPreEstateNum(StringHelper.formatObject(zd.getBDCDYH()));
								msg.getHead().setRecType("4000101");
								List<DJFDJSQR> djsqrs = msg.getData().getDJSQR();
								djsqrs = packageXml.getDJSQRs(djsqrs, sqrs, zd.getYSDM(), ywh, zd.getBDCDYH());
								msg.getData().setDJSQR(djsqrs);

								FJF100 fj = msg.getData().getFJF100();
								fj = packageXml.getFJF(fj);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
						if ((QLLX.GYJSYDSHYQ_FWSYQ.Value.equals(super.getQllx().Value)||"H".equals(super.getBdcdylx().toString()))&&!"02".equals(djdy.getBDCDYLX())) { 
							// 房屋所有权
							try {
								h = dao.get(BDCS_H_XZ.class, djdy.getBDCDYID());
								if(h != null && h.getZDBDCDYID() != null){
									BDCS_SHYQZD_XZ zd = dao.get(BDCS_SHYQZD_XZ.class, h.getZDBDCDYID());
									h.setZDDM(zd.getZDDM());
								}
								if(h != null && !StringUtils.isEmpty(h.getQXDM())){
									msg.getHead().setAreaCode(h.getQXDM());
								}
								super.fillHead(msg, i, ywh,h.getBDCDYH(),h.getQXDM(),ql.getLYQLID());
								Rights rights = ql;
								QLFZXDJ zxdj = msg.getData().getZXDJ();
								SubRights subrights=RightsTools.loadSubRights(DJDYLY.GZ, ql.getFSQLID());
								zxdj = packageXml.getZXDJ(zxdj, rights, ywh, h.getQXDM(),subrights);
								DJTDJSLSQ sq = msg.getData().getDJSLSQ();
								sq = packageXml.getDJTDJSLSQ(sq, ywh, null, ql, xmxx, h, xmxx.getSLSJ(), null, null, null);

								List<DJFDJSJ> sj = msg.getData().getDJSJ();
								sj = packageXml.getDJFDJSJ(h, ywh,actinstID);
								msg.getData().setDJSJ(sj);

								//9.登记收费(可选)
								List<DJFDJSF> sfList = msg.getData().getDJSF();
								sfList = packageXml.getDJSF(h, ywh,this.getXMBH());
								msg.getData().setDJSF(sfList);

								//10.审核(可选)
								List<DJFDJSH> sh = msg.getData().getDJSH();
								 sh = packageXml.getDJFDJSH(h, ywh, this.getXMBH(), actinstID);
								msg.getData().setDJSH(sh);

								//11.缮证(可选)
								List<DJFDJSZ>  sz = packageXml.getDJFDJSZ(h, ywh, this.getXMBH());
								msg.getData().setDJSZ(sz);

								//11.发证(可选)
								List<DJFDJFZ>   fz = packageXml.getDJFDJFZ(h, ywh, this.getXMBH());
								msg.getData().setDJFZ(fz);
								
								//12.归档(可选)
								List<DJFDJGD> gd = packageXml.getDJFDJGD(h, ywh, this.getXMBH());
								msg.getData().setDJGD(gd);
								
								List<DJFDJSQR> djsqrs = msg.getData().getDJSQR();
								djsqrs = packageXml.getDJSQRs(djsqrs, sqrs, h.getYSDM(), ywh, h.getBDCDYH());
								msg.getData().setDJGD(gd);

								msg.getData().setDJSQR(djsqrs);
								FJF100 fj = msg.getData().getFJF100();
								fj = packageXml.getFJF(fj);
								
								
								msg.getHead().setParcelID(StringHelper.formatObject(h.getZDDM()));
								msg.getHead().setEstateNum(StringHelper.formatObject(h.getBDCDYH()));
//								msg.getHead().setPreEstateNum(StringHelper.formatObject(h.getBDCDYH()));
								msg.getHead().setRecType("4000101");
								
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
						if (QLLX.HYSYQ.Value.equals(super.getQllx().Value) || QLLX.WJMHDSYQ.Value.equals(super.getQllx().Value)) { 
							// 海域(含无居民海岛)使用权注销
							 // 海域(含无居民海岛)使用权
							String zhdm=null;
							String hql=null;
							YHZK yhzk_gz = null;
							Sea zh = dao.get(BDCS_ZH_GZ.class, djdy.getBDCDYID());
							if (zh==null) {
								zh=dao.get(BDCS_ZH_XZ.class, djdy.getBDCDYID());
							}
							if (null != zh) {
							 zhdm=zh.getZHDM();
							 hql = "BDCDYID = '" + zh.getId() + "' ";
								List<BDCS_YHZK_GZ> yhzks = dao.getDataList(BDCS_YHZK_GZ.class, hql);
								if (yhzks != null && yhzks.size() > 0) {
									yhzk_gz = yhzks.get(0);
								}else {
									List<BDCS_YHZK_XZ> yhzksxz = dao.getDataList(BDCS_YHZK_XZ.class, hql);
									if (yhzksxz != null && yhzksxz.size() > 0) {
										yhzk_gz = yhzksxz.get(0);
									}
								}
							}
							if(zhdm!=null&&zhdm.length()>0&&(yhzk_gz.getZHDM()==null)||yhzk_gz.getZHDM().length()>0) {
								//维护数据
								yhzk_gz.setZHDM(zhdm);
							}
							// 这些字段先手动赋值 diaoliwei
							if (zh != null) {
								if (StringUtils.isEmpty(zh.getZHT())) {
									zh.setZHT("无");
								}
							}
								//设置报文头
								super.fillHead(msg, i, ywh,zh.getBDCDYH(),zh.getQXDM(),ql.getLYQLID());
								msg.getHead().setParcelID(StringHelper.formatObject(zh.getZHDM()));
								msg.getHead().setEstateNum(StringHelper.formatObject(zh.getBDCDYH()));
//								msg.getHead().setPreEstateNum(StringHelper.formatObject(zh.getBDCDYH()));
								//2.非结构化文档
								FJF100 fj = msg.getData().getFJF100();
								fj = packageXml.getFJF(fj);
								msg.getData().setFJF100(fj);

								//7.登记受理信息表
								DJTDJSLSQ sq = msg.getData().getDJSLSQ();
								sq = packageXml.getDJTDJSLSQ(sq, ywh, null, ql, xmxx, null, xmxx.getSLSJ(), null, null, zh);
								msg.getData().setDJSLSQ(sq);

								//8.登记收件(可选)
								List<DJFDJSJ> sj = msg.getData().getDJSJ();
								sj = packageXml.getDJFDJSJ(zh, ywh,actinstID);
								msg.getData().setDJSJ(sj);

								//9.登记收费(可选)
								List<DJFDJSF> sfList = msg.getData().getDJSF();
								sfList = packageXml.getDJSF(zh, ywh,this.getXMBH());
								msg.getData().setDJSF(sfList);

								//10.审核(可选)
								List<DJFDJSH> sh = msg.getData().getDJSH();
								 sh = packageXml.getDJFDJSH(zh, ywh, this.getXMBH(), actinstID);
								msg.getData().setDJSH(sh);

								//11.缮证(可选)
								List<DJFDJSZ>  sz = packageXml.getDJFDJSZ(zh, ywh, this.getXMBH());
								msg.getData().setDJSZ(sz);

								//11.发证(可选)
								List<DJFDJFZ>   fz = packageXml.getDJFDJFZ(zh, ywh, this.getXMBH());
								msg.getData().setDJFZ(fz);
								
								//12.归档(可选)
								List<DJFDJGD> gd = packageXml.getDJFDJGD(zh, ywh, this.getXMBH());
								msg.getData().setDJGD(gd);
								
								//13.申请人属性
								List<DJFDJSQR> djsqrs = msg.getData().getDJSQR();
								djsqrs = packageXml.getDJSQRs(djsqrs, sqrs, zh.getYSDM(), ywh, zh.getBDCDYH());
								msg.getData().setDJSQR(djsqrs);
						        //注销登记
								Rights rights = ql;
								QLFZXDJ zxdj = msg.getData().getZXDJ();
								SubRights subrights=RightsTools.loadSubRights(DJDYLY.GZ, ql.getFSQLID());
								zxdj = packageXml.getZXDJ(zxdj, rights, ywh, zh.getQXDM(),subrights);
								msg.getData().setZXDJ(zxdj);
								msg.getHead().setRecType("4000101");
						}
					}
					String msgName = getMessageFileName( msg,  i ,djdys.size(),names,djdy);
					mashaller.marshal(msg, new File(path +msgName));
					result = super.uploadFile(path +msgName, ConstValue.RECCODE.YY_ZXDJ.Value, actinstID, djdy.getDJDYID(), ql.getId());
					names.put(djdy.getDJDYID(), path +msgName);
					
				}
			}
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		if(null == result){
			Map<String, String> xmlError = new HashMap<String, String>();
			xmlError.put("error", "连接SFTP失败,请检查服务器和前置机的连接是否正常");
			YwLogUtil.addSjsbResult("Biz" + msg.getHead().getBizMsgID() + ".xml", "", "连接SFTP失败,请检查服务器和前置机的连接是否正常", ConstValue.SF.NO.Value, ConstValue.RECCODE.ZX_ZXDJ.Value,ProjectHelper.getpRroinstIDByActinstID(actinstID));
			//return xmlError;
		}
		if(!"1".equals(result) && result.indexOf("success") == -1){ //xml本地校验不通过时
			Map<String, String> xmlError = new HashMap<String, String>();
			xmlError.put("error", result);
			//return xmlError;
		}
		if(!StringUtils.isEmpty(result) && result.indexOf("success") > -1 && !names.containsKey("reccode")){
			names.put("reccode", result);
		}
		return names;
	
	
	}
}
