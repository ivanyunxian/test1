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
import com.supermap.realestate.registration.dataExchange.hy.KTFZHYHZK;
import com.supermap.realestate.registration.dataExchange.utils.packageXml;
import com.supermap.realestate.registration.dataExchange.xzdy.QLFXZDY;
import com.supermap.realestate.registration.handler.DJHandler;
import com.supermap.realestate.registration.model.BDCS_DJDY_GZ;
import com.supermap.realestate.registration.model.BDCS_DYXZ;
import com.supermap.realestate.registration.model.BDCS_H_XZ;
import com.supermap.realestate.registration.model.BDCS_H_XZY;
import com.supermap.realestate.registration.model.BDCS_QL_GZ;
import com.supermap.realestate.registration.model.BDCS_SHYQZD_XZ;
import com.supermap.realestate.registration.model.BDCS_SQR;
import com.supermap.realestate.registration.model.BDCS_XMXX;
import com.supermap.realestate.registration.model.BDCS_XM_DYXZ;
import com.supermap.realestate.registration.model.BDCS_YHZK_GZ;
import com.supermap.realestate.registration.model.BDCS_YHZK_XZ;
import com.supermap.realestate.registration.model.BDCS_ZH_GZ;
import com.supermap.realestate.registration.model.BDCS_ZH_XZ;
import com.supermap.realestate.registration.model.interfaces.RealUnit;
import com.supermap.realestate.registration.model.interfaces.Rights;
import com.supermap.realestate.registration.model.interfaces.RightsHolder;
import com.supermap.realestate.registration.model.interfaces.Sea;
import com.supermap.realestate.registration.model.interfaces.YHZK;
import com.supermap.realestate.registration.service.impl.ProjectServiceImpl;
import com.supermap.realestate.registration.tools.RightsHolderTools;
import com.supermap.realestate.registration.tools.RightsTools;
import com.supermap.realestate.registration.tools.UnitTools;
import com.supermap.realestate.registration.util.ConstValue.BDCDYLX;
import com.supermap.realestate.registration.util.ConstValue.DJDYLY;
import com.supermap.realestate.registration.util.ConstValue.QLLX;
import com.supermap.realestate.registration.util.ConstValue.SF;
import com.supermap.realestate.registration.util.ConstValue.SFDB;
import com.supermap.realestate.registration.util.ConstValue;
import com.supermap.realestate.registration.util.Global;
import com.supermap.realestate.registration.util.ProjectHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.realestate.registration.util.YwLogUtil;
import com.supermap.wisdombusiness.core.SuperSpringContext;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;

/*
 1、单元限制登记
 */
/**
 * 
 * 单元限制登记
 * @ClassName: DYXZDJHandler
 * @author yuxuebin
 * @date 2016年01月07日 下午03:30:35
 */
public class DYLimitedDJHandler extends DJHandlerBase implements DJHandler {

	/**
	 * 构造函数
	 * 
	 * @param info
	 */
	public DYLimitedDJHandler(ProjectInfo info) {
		super(info);
	}

	/**
	 * 添加不动产单元
	 */
	@Override
	public boolean addBDCDY(String bdcdyid) {
		boolean bsuccess = false;
		CommonDao dao = getCommonDao();
		RealUnit unit = UnitTools.loadUnit(this.getBdcdylx(), DJDYLY.XZ, bdcdyid);
		BDCS_DJDY_GZ djdy = null;
		djdy = super.createDJDYfromXZ(bdcdyid);
		if (djdy == null) {
			djdy = createDJDYfromXZ(bdcdyid, unit);
		}
		if (djdy != null) {
			BDCS_XMXX xmxx=dao.get(BDCS_XMXX.class, this.getXMBH());
			List<Rights> qls=RightsTools.loadRightsByCondition(DJDYLY.XZ, "DJDYID='"+djdy.getDJDYID()+"' AND QLLX IN ('1','2','3','4','5','6','7','8','24')");
			List<RightsHolder> qlrs=null;
			if(qls!=null&&qls.size()>0){
				String qlid=qls.get(0).getId();
				qlrs=RightsHolderTools.loadRightsHolders(DJDYLY.XZ, qlid);
			}
			//添加限制
			BDCS_DYXZ dyxz=new BDCS_DYXZ();
			String dyxzid=super.getPrimaryKey();
			dyxz.setXMBH(super.getXMBH());
			dyxz.setId(dyxzid);
			dyxz.setBDCDYID(djdy.getBDCDYID());
			dyxz.setBDCDYLX(djdy.getBDCDYLX());
			dyxz.setYXBZ("0");
			dyxz.setYWH(super.getProject_id());
			if(unit!=null){
				dyxz.setBDCDYH(unit.getBDCDYH());
			}else{
				dyxz.setBDCDYH(djdy.getBDCDYH());
			}
			if(qls!=null&&qls.size()>0){
				dyxz.setBDCQZH(qls.get(0).getBDCQZH());
			}
			if(qlrs!=null&&qlrs.size()>0){
				String bxzrmc="";
				String bxzrzjzl="";
				String bxzrzjhm="";
				List<String> qlrmclist=new ArrayList<String>();
				List<String> zjhlist=new ArrayList<String>();
				for(RightsHolder qlr:qlrs){
					if(!StringHelper.isEmpty(qlr.getQLRMC())){
						qlrmclist.add(qlr.getQLRMC());
						zjhlist.add(StringHelper.formatObject(qlr.getZJH()));
					}
					if(StringHelper.isEmpty(bxzrzjzl)&&!StringHelper.isEmpty(qlr.getZJZL())){
						bxzrzjzl=qlr.getZJZL();
					}
				}
				bxzrmc=StringHelper.formatList(qlrmclist, "、");
				bxzrzjhm=StringHelper.formatList(zjhlist, "、");
				dyxz.setBXZRMC(bxzrmc);
				dyxz.setBXZRZJHM(bxzrzjhm);
				dyxz.setBXZRZJZL(bxzrzjzl);
			}
			if(xmxx!=null){
				dyxz.setSLR(xmxx.getSLRY());
			}
			//添加項目-限制關係
			BDCS_XM_DYXZ xmdyxz=new BDCS_XM_DYXZ();
			String xmdyxzid=super.getPrimaryKey();
			xmdyxz.setId(xmdyxzid);
			xmdyxz.setDYXZID(dyxzid);
			xmdyxz.setBDCDYID(dyxz.getBDCDYID());
			xmdyxz.setBDCDYLX(dyxz.getBDCDYLX());
			xmdyxz.setXMBH(super.getXMBH());
			
			dao.save(dyxz);
			dao.save(xmdyxz);
			dao.save(djdy);
		}
		dao.flush();
		bsuccess = true;
		return bsuccess;
	}
	
	/**
	 * 添加登记单元信息
	 * @作者 diaoliwe
	 * @创建时间 2015年7月25日下午10:13:56
	 * @param bdcdyid
	 * @param realUnit
	 * @return
	 */
	protected BDCS_DJDY_GZ createDJDYfromXZ(String bdcdyid, RealUnit realUnit) {
		BDCS_DJDY_GZ gzdjdy = new BDCS_DJDY_GZ();
		String gzdjdyid = getPrimaryKey();
		gzdjdy.setXMBH(this.getXMBH());
		gzdjdy.setDJDYID(gzdjdyid);
		gzdjdy.setBDCDYID(realUnit.getId());
		gzdjdy.setBDCDYLX(this.getBdcdylx().Value);
		gzdjdy.setBDCDYH(realUnit.getBDCDYH());
		gzdjdy.setLY(DJDYLY.XZ.Value);
		// 设置预测户的项目编号
		realUnit.setXMBH(this.getXMBH());
		return gzdjdy;
	}

	/**
	 * 写入登记薄
	 */
	@Override
	public boolean writeDJB() {
		CommonDao dao = getCommonDao();
		String dbr = Global.getCurrentUserName();
		Date djsj=new Date();
		List<BDCS_DYXZ> dyxzs=dao.getDataList(BDCS_DYXZ.class, ProjectHelper.GetXMBHCondition(super.getXMBH()));
		if(dyxzs!=null&&dyxzs.size()>0){
			for(BDCS_DYXZ dyxz:dyxzs){
				dyxz.setYXBZ("1");
				dyxz.setDBR(dbr);
				dyxz.setDJSJ(djsj);
				dao.update(dyxz);
			}
		}
		this.SetSFDB();
		this.alterCachedXMXX();
		dao.flush();
		return true;
	}

	/**
	 * 移除登记产单元
	 */
	@Override
	public boolean removeBDCDY(String bdcdyid) {
		BDCS_XMXX xmxx=Global.getXMXXbyXMBH(super.getXMBH());
		if(SF.YES.Value.equals(xmxx.getSFDB()))
		{
			super.setErrMessage("项目已经登簿，不能再删除，请使用限制解除流程！");
			return false;
		}
		CommonDao dao = getCommonDao();
		List<BDCS_XM_DYXZ> xmdyxzs=dao.getDataList(BDCS_XM_DYXZ.class, ProjectHelper.GetXMBHCondition(super.getXMBH())+" AND BDCDYID='"+bdcdyid+"'");
		if(xmdyxzs!=null&&xmdyxzs.size()>0){
			for(BDCS_XM_DYXZ xmdyxz:xmdyxzs){
				String dyxzid=xmdyxz.getDYXZID();
				BDCS_DYXZ dyxz=dao.get(BDCS_DYXZ.class, dyxzid);
				if(dyxz!=null){
					dao.deleteEntity(dyxz);
				}
				dao.deleteEntity(xmdyxz);
			}
		}
		List<BDCS_DJDY_GZ> djdys=dao.getDataList(BDCS_DJDY_GZ.class, ProjectHelper.GetXMBHCondition(super.getXMBH())+" AND BDCDYID='"+bdcdyid+"'");
		if(djdys!=null&&djdys.size()>0){
			for(BDCS_DJDY_GZ djdy:djdys){
				dao.deleteEntity(djdy);
			}
		}
		dao.flush();
		return true;
	}

	/**
	 * 获取不动产单元列表
	 */
	@Override
	public List<UnitTree> getUnitTree() {
		
		List<UnitTree> tree=super.getUnitList();
		if(tree!=null&&tree.size()>0){
			for(UnitTree unitnode:tree){
				String bdcdyid=unitnode.getBdcdyid();
				List<BDCS_DYXZ> list=getCommonDao().getDataList(BDCS_DYXZ.class, ProjectHelper.GetXMBHCondition(super.getXMBH())+" AND BDCDYID='"+bdcdyid+"'");
				if(list!=null&&list.size()>0){
					String dyxzid=list.get(0).getId();
					unitnode.setQlid(dyxzid);
				}
			}
		}
		return tree;
	}

	/**
	 * 获取错误信息
	 */
	@Override
	public String getError() {
		return super.getErrMessage();
	}

	@Override
	public void addQLRbySQRArray(String qlid, Object[] sqrids) {
	}

	@Override
	public void removeQLR(String qlid, String qlrid) {
	}

	@Override
	public void SendMsg(String bljc) {
	}
	
	@Override
	public Map<String,String> exportXML(String path, String actinstID) {
		Marshaller mashaller;
		Map<String,String> names = new HashMap<String,String>();
		try {
			CommonDao dao = super.getCommonDao();
			String xmbhHql = ProjectHelper.GetXMBHCondition(super.getXMBH());
			List<BDCS_DJDY_GZ> djdys = dao.getDataList(BDCS_DJDY_GZ.class, xmbhHql);
			if (djdys != null && djdys.size() > 0) {
				mashaller = JAXBContext.newInstance(Message.class).createMarshaller();
				Calendar calendar = Calendar.getInstance(); // 创建一个日历对象
				calendar.setTime(new Date()); // 用当前时间初始化日历时间
				String cyear = calendar.get(Calendar.YEAR) + "";
				String ywh = packageXml.GetYWLSHByYWH(this.getProject_id());
				BDCS_XMXX xmxx = dao.get(BDCS_XMXX.class, super.getXMBH());
				
				BDCS_QL_GZ ql = new BDCS_QL_GZ();
				ql.setDJLX(xmxx.getDJLX());
				ql.setQLLX(xmxx.getQLLX());
				String result = "";
				for(int i = 0; i < djdys.size(); i ++){
					BDCS_DJDY_GZ djdy = djdys.get(i);
					
					String condition = MessageFormat.format("DJDYID=''{0}'' AND XMBH=''{1}'' ", djdy.getDJDYID(),djdy.getXMBH());
					List<BDCS_DYXZ> xzdyList = dao.getDataList(BDCS_DYXZ.class, condition);
					
					List<BDCS_SQR> sqrs = new ArrayList<BDCS_SQR>();
					ProjectServiceImpl serviceImpl = SuperSpringContext.getContext().getBean(ProjectServiceImpl.class);
					sqrs = serviceImpl.getSQRList(super.getXMBH());
					
					Message msg = exchangeFactory.createMessageByXZDY();
					msg.getHead().setRecType("9000103");

					if (BDCDYLX.SHYQZD.Value.equals(djdy.getBDCDYLX()) || QLLX.ZJDSYQ.Value.equals(this.getQllx().Value)) { //使用权宗地、宅基地使用权
						try {
							BDCS_SHYQZD_XZ zd = dao.get(BDCS_SHYQZD_XZ.class, djdy.getBDCDYID());
							super.fillHead(msg, i, ywh,zd.getBDCDYH(),zd.getQXDM(),ql.getLYQLID());
							msg.getHead().setParcelID(StringHelper.formatObject(zd.getZDDM()));
							msg.getHead().setEstateNum(StringHelper.formatObject(zd.getBDCDYH()));
//							msg.getHead().setPreEstateNum(StringHelper.formatObject(zd.getBDCDYH()));
							if(zd != null && !StringUtils.isEmpty(zd.getQXDM())){
								msg.getHead().setAreaCode(zd.getQXDM());
							}
							
							if (djdy != null) {
								List<QLFXZDY> xzdy = msg.getData().getQLFXZDY();
								xzdy = packageXml.getQLFXZDYs(xzdy,xzdyList,ywh);
								
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
								
								List<DJFDJSQR> djsqrs = msg.getData().getDJSQR();
								djsqrs = packageXml.getDJSQRs(djsqrs, sqrs, zd.getYSDM(), ywh, zd.getBDCDYH());
								msg.getData().setDJSQR(djsqrs);
								
								FJF100 fj = msg.getData().getFJF100();
								fj = packageXml.getFJF(fj);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					BDCS_H_XZ h = null;
					if(BDCDYLX.H.Value.equals(djdy.getBDCDYLX())){ //房屋
						try {
							h = dao.get(BDCS_H_XZ.class, djdy.getBDCDYID());
							if(h != null){
								BDCS_SHYQZD_XZ zd = dao.get(BDCS_SHYQZD_XZ.class, h.getZDBDCDYID());
								h.setZDDM(zd.getZDDM());
							}
							super.fillHead(msg, i, ywh,h.getBDCDYH(),h.getQXDM(),ql.getLYQLID());
							msg.getHead().setParcelID(StringHelper.formatObject(h.getZDDM()));
							msg.getHead().setEstateNum(StringHelper.formatObject(h.getBDCDYH()));
							
							
							if(h != null && !StringUtils.isEmpty(h.getQXDM())){
								msg.getHead().setAreaCode(h.getQXDM());
							}
							if (djdy != null) {
								
								List<QLFXZDY> xzdy = msg.getData().getQLFXZDY();
								xzdy = packageXml.getQLFXZDYs(xzdy,xzdyList,ywh);
								msg.getData().setQLFXZDY(xzdy);
								
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
								

								DJTDJSLSQ sq = msg.getData().getDJSLSQ();
								sq = packageXml.getDJTDJSLSQ(sq, ywh, null, ql, xmxx, h, xmxx.getSLSJ(), null, null, null);
								
								List<DJFDJSQR> djsqrs = msg.getData().getDJSQR();
								djsqrs = packageXml.getDJSQRs(djsqrs, sqrs, h.getYSDM(), ywh, h.getBDCDYH());
								msg.getData().setDJSQR(djsqrs);
								
								FJF100 fj = msg.getData().getFJF100();
								fj = packageXml.getFJF(fj);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					BDCS_H_XZY xzy = null;
					if(BDCDYLX.YCH.Value.equals(djdy.getBDCDYLX())){ //在建工程
						try {
							xzy = dao.get(BDCS_H_XZY.class, djdy.getBDCDYID());
							if(xzy != null){
								BDCS_SHYQZD_XZ zd = dao.get(BDCS_SHYQZD_XZ.class, xzy.getZDBDCDYID());
								xzy.setZDDM(zd.getZDDM());
							}
							super.fillHead(msg, i, ywh,xzy.getBDCDYH(),xzy.getQXDM(),ql.getLYQLID());
							msg.getHead().setParcelID(StringHelper.formatObject(xzy.getZDDM()));
							msg.getHead().setEstateNum(StringHelper.formatObject(xzy.getBDCDYH()));
//							msg.getHead().setPreEstateNum(StringHelper.formatObject(xzy.getBDCDYH()));
							if(xzy != null && !StringUtils.isEmpty(xzy.getQXDM())){
								msg.getHead().setAreaCode(xzy.getQXDM());
							}
							if (djdy != null) {
								
								List<QLFXZDY> xzdy = msg.getData().getQLFXZDY();
								xzdy = packageXml.getQLFXZDYs(xzdy,xzdyList,ywh);
								msg.getData().setQLFXZDY(xzdy);

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
								

								DJTDJSLSQ sq = msg.getData().getDJSLSQ();
								sq = packageXml.getDJTDJSLSQ(sq, ywh, null, ql, xmxx, xzy, xmxx.getSLSJ(), null, null, null);
								
								List<DJFDJSQR> djsqrs = msg.getData().getDJSQR();
								djsqrs = packageXml.getDJSQRs(djsqrs, sqrs, xzy.getYSDM(), ywh, xzy.getBDCDYH());
								msg.getData().setDJSQR(djsqrs);
								
								FJF100 fj = msg.getData().getFJF100();
								fj = packageXml.getFJF(fj);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					
					if("HY".equals(this.getBdcdylx().toString())){ //海域
						try {
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
								this.fillHead(msg, i, ywh,zh.getBDCDYH(),zh.getQXDM(),ql.getLYQLID());
								msg.getHead().setParcelID(StringHelper.formatObject(zh.getZHDM()));
								msg.getHead().setEstateNum(StringHelper.formatObject(zh.getBDCDYH()));
//								msg.getHead().setPreEstateNum(StringHelper.formatObject(zh.getBDCDYH()));
							if (djdy != null) {
								
								List<QLFXZDY> xzdy = msg.getData().getQLFXZDY();
								xzdy = packageXml.getQLFXZDYs(xzdy,xzdyList,ywh);
								msg.getData().setQLFXZDY(xzdy);
								
								//3.非结构化文档
								FJF100 fj = msg.getData().getFJF100();
								fj = packageXml.getFJF(fj);
								msg.getData().setFJF100(fj);
								
								//5.宗海变化状况表(可选 )
								KTFZHYHZK yhzk = msg.getData().getKTFZHYHZK();
								if(yhzk!=null) {
									yhzk = packageXml.getKTFZHYHZK(yhzk, yhzk_gz, zh.getBDCDYH());
									msg.getData().setKTFZHYHZK(yhzk);
								}

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
								msg.getData().setDJGD(gd);	;
								
								List<DJFDJSQR> djsqrs = msg.getData().getDJSQR();
								djsqrs = packageXml.getDJSQRs(djsqrs, sqrs, zh.getYSDM(), ywh, zh.getBDCDYH());
								msg.getData().setDJSQR(djsqrs);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					
					
					
					
					
					
					mashaller.marshal(msg, new File(path + "Biz" + msg.getHead().getBizMsgID()+ ".xml"));
					result = uploadFile(path + "Biz" + msg.getHead().getBizMsgID()+ ".xml",ConstValue.RECCODE.DIYQ_ZXDJ.Value,actinstID,djdy.getDJDYID(),ql.getId());
					names.put(djdy.getDJDYID(),msg.getHead().getBizMsgID()+ ".xml");
					
					if(result.equals("")|| result==null){
						Map<String, String> xmlError = new HashMap<String, String>();
						xmlError.put("error", "连接SFTP失败,请检查服务器和前置机的连接是否正常");
						YwLogUtil.addSjsbResult("Biz" + msg.getHead().getBizMsgID() + ".xml", "", "连接SFTP失败,请检查服务器和前置机的连接是否正常", ConstValue.SF.NO.Value, ConstValue.RECCODE.DIYQ_ZXDJ.Value,ProjectHelper.getpRroinstIDByActinstID(actinstID));
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

	/************************ 内部方法 *********************************/
}
