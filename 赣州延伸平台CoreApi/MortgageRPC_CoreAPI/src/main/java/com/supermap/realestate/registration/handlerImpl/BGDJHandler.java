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

import org.springframework.util.StringUtils;

import com.supermap.realestate.registration.ViewClass.BGBuildingAndHouse;
import com.supermap.realestate.registration.ViewClass.BGBuildingAndHouse.BGHouse;
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
import com.supermap.realestate.registration.dataExchange.hy.KTFZHBHQK;
import com.supermap.realestate.registration.dataExchange.shyq.KTFZDBHQK;
import com.supermap.realestate.registration.dataExchange.shyq.KTTGYJZD;
import com.supermap.realestate.registration.dataExchange.shyq.KTTGYJZX;
import com.supermap.realestate.registration.dataExchange.shyq.KTTZDJBXX;
import com.supermap.realestate.registration.dataExchange.shyq.QLFQLJSYDSYQ;
import com.supermap.realestate.registration.dataExchange.shyq.ZDK103;
import com.supermap.realestate.registration.dataExchange.utils.packageXml;
import com.supermap.realestate.registration.factorys.HandlerFactory;
import com.supermap.realestate.registration.handler.DJHandler;
import com.supermap.realestate.registration.mapping.HandlerMapping;
import com.supermap.realestate.registration.model.BDCS_C_GZ;
import com.supermap.realestate.registration.model.BDCS_DJDY_GZ;
import com.supermap.realestate.registration.model.BDCS_FSQL_GZ;
import com.supermap.realestate.registration.model.BDCS_H_GZ;
import com.supermap.realestate.registration.model.BDCS_H_LS;
import com.supermap.realestate.registration.model.BDCS_LJZ_XZ;
import com.supermap.realestate.registration.model.BDCS_QLR_GZ;
import com.supermap.realestate.registration.model.BDCS_QLR_XZ;
import com.supermap.realestate.registration.model.BDCS_QL_GZ;
import com.supermap.realestate.registration.model.BDCS_SHYQZD_GZ;
import com.supermap.realestate.registration.model.BDCS_SHYQZD_XZ;
import com.supermap.realestate.registration.model.BDCS_SQR;
import com.supermap.realestate.registration.model.BDCS_SYQZD_XZ;
import com.supermap.realestate.registration.model.BDCS_XMXX;
import com.supermap.realestate.registration.model.BDCS_ZRZ_XZ;
import com.supermap.realestate.registration.model.interfaces.Building;
import com.supermap.realestate.registration.model.interfaces.House;
import com.supermap.realestate.registration.model.interfaces.OwnerLand;
import com.supermap.realestate.registration.model.interfaces.RealUnit;
import com.supermap.realestate.registration.model.interfaces.Rights;
import com.supermap.realestate.registration.model.interfaces.RightsHolder;
import com.supermap.realestate.registration.model.interfaces.SubRights;
import com.supermap.realestate.registration.model.interfaces.UseLand;
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
import com.supermap.realestate.registration.util.ConstValue.SQRLB;
import com.supermap.realestate.registration.util.ConfigHelper;
import com.supermap.realestate.registration.util.Global;
import com.supermap.realestate.registration.util.ProjectHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.realestate.registration.util.YwLogUtil;
import com.supermap.wisdombusiness.core.SuperSpringContext;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;

/*	
 1、国有建设用地使用权变更登记
 2、集体建设用地使用权变更登记（未配置）
 3、宅基地使用权变更登记（未配置）
 4、国有建设用地使用权/房屋所有权变更登记
 5、集体建设用地使用权/房屋所有权变更登记（未配置）
 6、宅基地使用权/房屋所有权变更登记（未配置）
 */
/**
 * 变更登记处理类
 * @ClassName: BGDJHandler
 * @author liushufeng
 * @date 2015年8月12日 下午3:14:15
 */
public class BGDJHandler extends DJHandlerBase implements DJHandler {

	/**
	 * 构造函数
	 * 
	 * @param info
	 */
	public BGDJHandler(ProjectInfo info) {
		super(info);
	}

	/**
	 * 添加不动产单元
	 */
	@Override
	public boolean addBDCDY(String bdcdyid) {
		CommonDao dao = this.getCommonDao();
		String[] ids = bdcdyid.split(",");
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
										//权利人关联申请人ID
										qlr.setSQRID(sqrlist.get(0).getId());
										getCommonDao().update(qlr);
									}
								}
								if (!bexists) {
									BDCS_SQR sqr = super.copyXZQLRtoSQR(qlrs.get(iqlr), SQRLB.JF);
									if (sqr != null) {
										dao.save(sqr);
										//权利人关联申请人ID
										qlr.setSQRID(sqr.getId());
										getCommonDao().update(qlr);
									}
								}
							}
						}
					}
					dao.save(djdy);
				}
			} else {
				dy = UnitTools.copyUnit(this.getBdcdylx(), DJDYLY.DC, DJDYLY.GZ, id);
				dy.setXMBH(super.getXMBH());
				// 登记单元索引表
				BDCS_DJDY_GZ djdy = super.createDJDYfromGZ(dy);
				if (djdy != null) {
					BDCS_QL_GZ ql = super.createQL(djdy, dy);
					BDCS_FSQL_GZ fsql = super.createFSQL(djdy.getDJDYID());
					ql.setFSQLID(fsql.getId());
					String xzqdm =ConfigHelper.getNameByValue("XZQHDM");
					if("450".equals(xzqdm.substring(0, 3))){
						//先广西用，ZY034流程需要在变更后单元的来源权利id里加上变更前单元的权利id liangq
						BDCS_XMXX xmxx = dao.get(BDCS_XMXX.class, djdy.getXMBH());
						String workflowcode = ProjectHelper.getWorkflowCodeByProjectID(xmxx.getPROJECT_ID());
						String fulSql= " SELECT m.workflowname from BDC_WORKFLOW.WFD_MAPPING M WHERE M.WORKFLOWCODE= '" + workflowcode + "' ";
						List<Map> lstmap=dao.getDataListByFullSql(fulSql);
						if(lstmap.size()>0){
							if("ZY034".equals(lstmap.get(0).get("WORKFLOWNAME").toString())){
								fulSql = "select qlid from bdck.bdcs_ql_xz where djdyid in  (select djdyid from bdck.bdcs_djdy_gz where xmbh = '"+djdy.getXMBH()+"' and ly = '02')";
								lstmap=dao.getDataListByFullSql(fulSql);
								if(lstmap.size()>0){
									ql.setLYQLID(lstmap.get(0).get("QLID").toString());
								}
							}
						}
					}
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
	 * 更新因土地导致的分割合并的数据
	 */
	public void updataSplitAndMergeData(){
		String sqlxmbh=ProjectHelper.GetXMBHCondition(getXMBH());
		BDCDYLX bdcdylx=getBdcdylx();
		if(BDCDYLX.SHYQZD.Value.equals(bdcdylx.Value) || BDCDYLX.SYQZD.Value.equals(bdcdylx.Value)){
			//过滤出变更后的数据
			List<BDCS_DJDY_GZ> djdys=getCommonDao().getDataList(BDCS_DJDY_GZ.class, sqlxmbh+" and LY='"+DJDYLY.GZ.Value+"' AND BDCDYLX ='"+bdcdylx.Value+"'");
			if(!StringHelper.isEmpty(djdys) && djdys.size()>0){
				List<BGBuildingAndHouse> lstbgbh=new ArrayList<BGBuildingAndHouse>();
				for(BDCS_DJDY_GZ bdcs_djdy_gz : djdys){
					String bdcdyid=bdcs_djdy_gz.getBDCDYID();
					if(!StringHelper.isEmpty(bdcdyid)){
						RealUnit unit=UnitTools.loadUnit(bdcdylx, DJDYLY.GZ, bdcdyid);
						String zrzbdcdyids="";
						String zddm="";
						String zdbdcdyid="";
						if(unit instanceof UseLand){
							UseLand land=(UseLand) unit;
							 zrzbdcdyids=land.getGLZRZID();
							 zddm=land.getZDDM();
							 zdbdcdyid=land.getId();
						}
						else if(unit instanceof OwnerLand){
							OwnerLand land=(OwnerLand) unit;
							 zrzbdcdyids=land.getGLZRZID();
							 zddm=land.getZDDM();
							 zdbdcdyid=land.getId();
						}
						if(!StringHelper.isEmpty(zrzbdcdyids)){
							String [] zrzbdcdyid=zrzbdcdyids.split(";");//ToDo 目前默认以分号分割
							if(!StringHelper.isEmpty(zrzbdcdyid) && zrzbdcdyid.length>0){
								for(String id :zrzbdcdyid){	
									getBuilding(zdbdcdyid, zddm, id, BDCDYLX.ZRZ,lstbgbh);
									getBuilding(zdbdcdyid, zddm, id, BDCDYLX.YCZRZ,lstbgbh);
								}
							}
						}
					}
				}
				updateUnit(lstbgbh);
			}	
			
		}
	}
	
	/**
	 * 更新自然幢信息及房屋信息，并且通过现状层的id更新对应的历史层数据
	 * @param lstbgbh ：宗地下的所有自然幢及房屋信息
	 */
	public void updateUnit(List<BGBuildingAndHouse> lstbgbh){
		if(!StringHelper.isEmpty(lstbgbh) && lstbgbh.size()>0){
			CommonDao dao=	this.getCommonDao();
			for(BGBuildingAndHouse bgbh:lstbgbh){
				Building building =bgbh.getBuilding();
				if(!StringHelper.isEmpty(building)){
					String zrzbdcdyh=bgbh.getZrzbdcdyh();
					String zdbdcdyid=bgbh.getZdbdcdyid();
					String zddm=bgbh.getZddm();
					building.setBDCDYH(zrzbdcdyh);
					building.setZDBDCDYID(zdbdcdyid);
					building.setZDDM(zddm);
					dao.update(building);
					Building b_ls=(Building) UnitTools.loadUnit(bgbh.getBdcdylx(), DJDYLY.LS, bgbh.getZrzbdcdyid());
					if(!StringHelper.isEmpty(b_ls)){
						b_ls.setBDCDYH(zrzbdcdyh);
						b_ls.setZDBDCDYID(zdbdcdyid);
						b_ls.setZDDM(zddm);
						dao.update(b_ls);
					}
					List<BGBuildingAndHouse.BGHouse> lstbgh=bgbh.getBghouses();
					if(!StringHelper.isEmpty(lstbgh) && lstbgh.size()>0){
						for(BGHouse bgh:lstbgh){
							House house=bgh.getHouse();
							String bdcdyh=bgh.getHbdcdyh();
							if(!StringHelper.isEmpty(house)){
								house.setNBDCDYH(bdcdyh);
								house.setZDBDCDYID(zdbdcdyid);
								house.setZDDM(zddm);
								dao.update(house);
								House h_ls= (House) UnitTools.loadUnit(bgh.getHbdcylx(), DJDYLY.LS, house.getId());
								if(!StringHelper.isEmpty(h_ls)){
									h_ls.setNBDCDYH(bdcdyh);
									h_ls.setZDBDCDYID(zdbdcdyid);
									h_ls.setZDDM(zddm);
									dao.update(h_ls);
								}
							}
						}
					}
				}
				
				
			}
		}
	}
	
	/**
	 * 存取宗地下的自然幢信息及房屋信息
	 * @param zdbdcdyid ：宗地不动产单元id，设置自然幢幢下宗地不动产单元ID
	 * @param zddm ：宗地代码，设置自然幢下宗地代码
	 * @param zrzbdcdyid：自然幢不动产单元id，获取对应自然幢信息
	 * @param bdcdylx：不动产单元类型，BDCDYLX.ZRZ或BDCDYLX.YCZRZ,因为宗地下的自然幢不确定是在期房还是现房
	 * @param lstbgbh：存储自然幢下的户信息
	 */
	public void getBuilding(String zdbdcdyid,String zddm,String zrzbdcdyid,BDCDYLX bdcdylx,List<BGBuildingAndHouse> lstbgbh){
		if(!StringHelper.isEmpty(zrzbdcdyid)){
		Building building=(Building)UnitTools.loadUnit(bdcdylx, DJDYLY.XZ, zrzbdcdyid);
		if(!StringHelper.isEmpty(building)){			
			String zrzbdcdyh=UnitTools.CreatBDCDYH(zddm, "03");
			BGBuildingAndHouse bh=new BGBuildingAndHouse();
			bh.setZdbdcdyid(zdbdcdyid);
			bh.setZddm(zddm);
			bh.setZrzbdcdyh(zrzbdcdyh);
			bh.setZrzbdcdyid(zrzbdcdyid);
			bh.setBuilding(building);
			bh.setBdcdylx(bdcdylx);
			List<BGHouse> lstbgh=new ArrayList<BGBuildingAndHouse.BGHouse>();
			if(BDCDYLX.ZRZ.Value.equals(bdcdylx.Value)){//实测自然幢
				getHouse(BDCDYLX.H,building.getId(),zrzbdcdyh,lstbgh,bh);
			}
			else if(BDCDYLX.YCZRZ.Value.equals(bdcdylx.Value)){//预测自然幢
				getHouse(BDCDYLX.YCH,building.getId(),zrzbdcdyh,lstbgh,bh);
			}
			bh.setBghouses(lstbgh);
			lstbgbh.add(bh);
		}
		}
		
	}

	/**
	 * 存放一个自然幢对应的房屋信息
	 * @param bdcdylx :不动产单元类型，BDCDYLX.H或BDCDYLX.YCH,不确定这个宗地上是否现房或期房
	 * @param zrzbdcdyid ：自然幢不动产单元号，获取房屋信息
	 * @param zrzbdcdyh：创建户对应的不动产单元号
	 * @param lstbgh：BGHouse的集合，为把自然幢所对应的房屋信息全部存放进去
	 * @param bh ：BGHouse是BGBuildingAndHouse的子类，所有通过这个调用
	 */
	public void getHouse(BDCDYLX bdcdylx,String zrzbdcdyid,String zrzbdcdyh,List<BGHouse> lstbgh,BGBuildingAndHouse bh){
		List<RealUnit> lsth=UnitTools.loadUnits(bdcdylx, DJDYLY.XZ, "ZRZBDCDYID='"+zrzbdcdyid+"'");
		if(!StringHelper.isEmpty(lsth) && lsth.size()>0){
			for(RealUnit unit :lsth){
				if(!StringHelper.isEmpty(zrzbdcdyh) && zrzbdcdyh.length()==28){
					if(unit instanceof House){						
						House h_xz=(House)unit;
						//创建不动产单元号
						String hbdcdyh=UnitTools.CreatBDCDYH(zrzbdcdyh.substring(0,24), "04");
						BGBuildingAndHouse.BGHouse bgh=bh.new BGHouse();
						bgh.setHbdcdyh(hbdcdyh);
						bgh.setHbdcylx(bdcdylx);
						bgh.setHouse(h_xz);
						lstbgh.add(bgh);
					}
				}
			}
		}
	}	
	
	/**
	 * 写入登记簿
	 */
	@Override
	public boolean writeDJB() {

		/*if (super.isCForCFING()) {
			return false;
		}*/
		updataSplitAndMergeData();//更新分割合并中宗地对应的自然幢及房屋信息
		String strSqlXMBH = ProjectHelper.GetXMBHCondition(getXMBH());
		List<BDCS_DJDY_GZ> djdys = getCommonDao().getDataList(BDCS_DJDY_GZ.class, strSqlXMBH);
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
					if ("01".equals(ly)) {
						if (!listBGH.contains(strDYID)) {
							listBGH.add(strDYID);
						}
						super.CopyGZQLToXZAndLS(djdyid);
						super.CopyGZQLRToXZAndLS(djdyid);
						super.CopyGZQDZRToXZAndLS(djdyid);
						super.CopyGZZSToXZAndLS(djdyid);
						super.CopyGZDYToXZAndLS(bdcs_djdy_gz.getBDCDYID());
						super.CopyGZDJDYToXZAndLS(bdcs_djdy_gz.getId());
						super.CopyGeo(bdcdyid, lx, DJDYLY.initFrom(ly));
					} else if ("02".equals(ly)) {
						if (!listBGQ.contains(strDYID)) {
							listBGQ.add(strDYID);
						}
						super.removeZSFromXZByALL(djdyid);
						super.removeQLRFromXZByALL(djdyid);
						super.removeQDZRFromXZByALL(djdyid);
						super.removeQLFromXZByALL(djdyid);
						UnitTools.deleteUnit(lx, DJDYLY.XZ, bdcdyid);
//						super.removeDYFromXZ(djdyid);sunhb-2016-03-31不需要移除了，上面已经移除了
						super.removeDJDYFromXZ(djdyid);
						super.DeleteGeo(bdcdyid, lx, DJDYLY.initFrom(ly));
					}
				}
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
				UnitTools.deleteUnit(BDCDYLX.initFrom(djdy.getBDCDYLX()), DJDYLY.GZ, bdcdyid);

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
		if (!StringHelper.isEmpty(xmxx) && "1".equals(xmxx.getSFDB())) {
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
						if(!"gz".equals(t.getLy())){
							List<Rights> qls=RightsTools.loadRightsByCondition(DJDYLY.XZ, " DJDYID IN (SELECT DJDYID FROM BDCS_DJDY_XZ WHERE BDCDYID='"+t.getBdcdyid()+"') AND QLLX='"+super.getQllx().Value+"'");
							if(qls!=null&&qls.size()>0){
								t.setOldqlid(qls.get(0).getId());
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
		if("LD".equals(super.getBdcdylx().toString())||"HY".equals(super.getBdcdylx().toString())){
			return super.exportXMLother(path, actinstID,"YES");
		}
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
				String cyear = calendar.get(Calendar.YEAR) + "";
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
							RealUnit unit=UnitTools.loadUnit(BDCDYLX.SHYQZD, DJDYLY.GZ, djdy.getBDCDYID());
							UseLand zd=(UseLand)unit;
							List<String> preEstateNum=new ArrayList<String>();
							// 目前未维护的字段先手动赋值 diaoliwei 2015-10-15
							if (null != zd) {
								if (StringUtils.isEmpty(zd.getZDT())) {
									zd.setZDT("无");
								}
							}
							for (int j = 0; j < bgqdjdys.size(); j++) {
								BDCS_DJDY_GZ bgqdjdy = bgqdjdys.get(j);
								RealUnit unit_XZ=UnitTools.loadUnit(BDCDYLX.SHYQZD, DJDYLY.LS, bgqdjdy.getBDCDYID());
								if(!StringHelper.isEmpty(unit_XZ)&&!StringHelper.isEmpty(unit_XZ.getBDCDYH())&&!preEstateNum.contains(unit_XZ.getBDCDYH())){
									preEstateNum.add(unit_XZ.getBDCDYH());
								}
							}
							// 标记单元号是否发生变化了，是否取消空间节点的存在
							boolean flag = false;
							if (!(zd.getBDCDYH().equals(preEstateNum)) && !StringUtils.isEmpty(preEstateNum)) {
								flag = true;
							}
							Message msg = exchangeFactory.createMessage(flag);
							msg.getData().setKTTGYJZX(packageXml.getKTTGYJZX(msg.getData().getKTTGYJZX(),zd.getZDDM()));
							msg.getData().setKTTGYJZD(packageXml.getKTTGYJZD(msg.getData().getKTTGYJZD(), zd.getZDDM()));

							super.fillHead(msg, i, ywh,zd.getBDCDYH(),zd.getQXDM(),ql.getLYQLID());

							msg.getHead().setParcelID(StringHelper.formatObject(zd.getZDDM()));
							msg.getHead().setEstateNum(StringHelper.formatObject(zd.getBDCDYH()));// 当前的不动产单元号
//							msg.getHead().setPreEstateNum(StringHelper.formatList(preEstateNum, ","));// 上一首的不动产单元号
							if (zd != null && !StringUtils.isEmpty(zd.getQXDM())) {
								msg.getHead().setAreaCode(zd.getQXDM());
							}
							if (djdy != null) {

								super.fillShyqZdData(msg,ywh,zd,sqrs,qlrs,ql,xmxx,actinstID);
								
								// 如果当前不动产单元号和上一个不动产单元号不同的话，把空间节点加进去
								if (!(preEstateNum.contains(zd.getBDCDYH()))) {
									List<ZDK103> zdk = msg.getData().getZDK103();
									zdk = packageXml.getZDK103(zdk, zd, null, null);
									msg.getData().setZDK103(zdk);
								}
								
							}
							KTFZDBHQK bhqk = msg.getData().getZDBHQK();
							bhqk = packageXml.getKTFZDBHQK(bhqk, zd, ql, null, null);
							msg.getData().setZDBHQK(bhqk);
							msgName = "Biz" + msg.getHead().getBizMsgID() + ".xml";
							mashaller.marshal(msg, new File(path + msgName));
							result = uploadFile(path + msgName, ConstValue.RECCODE.JSYDSHYQ_BGDJ.Value,actinstID,djdy.getDJDYID(),ql.getId());
							names.put(djdy.getDJDYID(), msg.getHead().getBizMsgID() + ".xml");
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					if (QLLX.GYJSYDSHYQ_FWSYQ.Value.equals(this.getQllx().Value)||QLLX.JTJSYDSYQ_FWSYQ.Value.equals(this.getQllx().Value)||QLLX.ZJDSYQ_FWSYQ.Value.equals(this.getQllx().Value)) { // 房屋所有权
						try {
							BDCS_H_GZ h = dao.get(BDCS_H_GZ.class, djdy.getBDCDYID());
							BDCS_ZRZ_XZ zrz_xz = null;
							if (h != null && !StringUtils.isEmpty(h.getZRZBDCDYID())) {
								zrz_xz = dao.get(BDCS_ZRZ_XZ.class, h.getZRZBDCDYID());
								if(zrz_xz != null){
									zrz_xz.setGHYT(h.getGHYT()); //自然幢的ghyt取户的ghyt
									zrz_xz.setFWJG(zrz_xz.getFWJG() == null || zrz_xz.getFWJG().equals("") ? h.getFWJG() : zrz_xz.getFWJG());
								}
							}
							BDCS_LJZ_XZ ljz_xz = null;
							if (h != null && !StringUtils.isEmpty(h.getLJZID())) {
								ljz_xz = dao.get(BDCS_LJZ_XZ.class, h.getLJZID());
							}
							if (h != null) {
								BDCS_SHYQZD_XZ zd = dao.get(BDCS_SHYQZD_XZ.class, h.getZDBDCDYID());
								h.setZDDM(zd.getZDDM());
								if(zrz_xz != null){
									zrz_xz.setZDDM(zd.getZDDM());
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
								super.fillFwData(msg, ywh, ljz_xz, c, zrz_xz, h, sqrs, qlrs, ql, xmxx, actinstID);
							}
							msgName = "Biz" + msg.getHead().getBizMsgID() + ".xml";
							mashaller.marshal(msg, new File(path + msgName));
							result = uploadFile(path + msgName, ConstValue.RECCODE.FDCQDZ_BGDJ.Value,actinstID,djdy.getDJDYID(),ql.getId());
							names.put(djdy.getDJDYID(), msg.getHead().getBizMsgID() + ".xml");
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					if (QLLX.JTTDSYQ.Value.equals(this.getQllx().Value)) { // 集体土地所有权
						try {
							BDCS_SYQZD_XZ oland = dao.get(BDCS_SYQZD_XZ.class,
									djdy.getBDCDYID());
							Message msg = exchangeFactory
									.createMessageByTDSYQ();
							super.fillHead(msg, i, ywh,oland.getBDCDYH(),oland.getQXDM(),ql.getLYQLID());
							msg.getHead().setParcelID(
									StringHelper.formatObject(oland.getZDDM()));
							msg.getHead()
									.setEstateNum(
											StringHelper.formatObject(oland
													.getBDCDYH()));
//							msg.getHead()
//									.setPreEstateNum(
//											StringHelper.formatObject(oland
//													.getBDCDYH()));
//							if (oland != null
//									&& !StringUtils.isEmpty(oland.getQXDM())) {
//								msg.getHead().setAreaCode(oland.getQXDM());
//							}
							if (djdy != null) {

                                super.fillSyqZdData( msg, ywh, oland, sqrs, qlrs, ql, xmxx, actinstID) ;
                                
								List<ZDK103> zdk = msg.getData().getZDK103();
								zdk = packageXml.getZDK103(zdk, null, oland,
										null);
							}
							msgName = "Biz" + msg.getHead().getBizMsgID()
									+ ".xml";
							names.put(djdy.getDJDYID(), msg.getHead()
									.getBizMsgID() + ".xml");
							mashaller.marshal(msg, new File(path + msgName));
							result = uploadFile(path + msgName,
									ConstValue.RECCODE.TDSYQ_CSDJ.Value,
									actinstID, djdy.getId(), ql.getId());
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					if(null == result || result.equals("")){
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
