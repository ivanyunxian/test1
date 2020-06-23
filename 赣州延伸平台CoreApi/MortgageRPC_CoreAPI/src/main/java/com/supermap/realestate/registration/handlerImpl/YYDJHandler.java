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
import com.supermap.realestate.registration.dataExchange.ZTTGYQLR;
import com.supermap.realestate.registration.dataExchange.exchangeFactory;
import com.supermap.realestate.registration.dataExchange.utils.packageXml;
import com.supermap.realestate.registration.dataExchange.yydj.QLFQLYYDJ;
import com.supermap.realestate.registration.handler.DJHandler;
import com.supermap.realestate.registration.model.BDCS_DJDY_GZ;
import com.supermap.realestate.registration.model.BDCS_DJDY_XZ;
import com.supermap.realestate.registration.model.BDCS_FSQL_GZ;
import com.supermap.realestate.registration.model.BDCS_H_XZ;
import com.supermap.realestate.registration.model.BDCS_QDZR_GZ;
import com.supermap.realestate.registration.model.BDCS_QLR_GZ;
import com.supermap.realestate.registration.model.BDCS_QLR_XZ;
import com.supermap.realestate.registration.model.BDCS_QL_GZ;
import com.supermap.realestate.registration.model.BDCS_QL_XZ;
import com.supermap.realestate.registration.model.BDCS_SHYQZD_XZ;
import com.supermap.realestate.registration.model.BDCS_SQR;
import com.supermap.realestate.registration.model.BDCS_XMXX;
import com.supermap.realestate.registration.model.BDCS_ZS_GZ;
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
import com.supermap.realestate.registration.util.ConfigHelper;
import com.supermap.realestate.registration.util.ConstValue;
import com.supermap.realestate.registration.util.ConstValue.BDCDYLX;
import com.supermap.realestate.registration.util.ConstValue.CZFS;
import com.supermap.realestate.registration.util.ConstValue.DJDYLY;
import com.supermap.realestate.registration.util.ConstValue.QLLX;
import com.supermap.realestate.registration.util.Global;
import com.supermap.realestate.registration.util.ObjectHelper;
import com.supermap.realestate.registration.util.ProjectHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.realestate.registration.util.YwLogUtil;
import com.supermap.wisdombusiness.core.SuperHelper;
import com.supermap.wisdombusiness.core.SuperSpringContext;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;

/*
 1、国有建设用地使用权异议登记
 2、集体建设用地使用权异议登记（未配置）
 3、宅基地使用权异议登记（未配置）
 4、国有建设用地使用权/房屋所有权异议登记
 5、集体建设用地使用权/房屋所有权异议登记（未配置）
 6、宅基地使用权/房屋所有权异议登记（未配置）
 */
/**
 * 
 * 异议登记处理类
 * @ClassName: YYDJHandler
 * @author liushufeng
 * @date 2015年9月8日 下午10:44:02
 */
public class YYDJHandler extends DJHandlerBase implements DJHandler {

	/**
	 * 构造函数
	 * 
	 * @param info
	 */
	public YYDJHandler(ProjectInfo info) {
		super(info);
	}

	/**
	 * 添加不动产单元
	 */
	@Override
	public boolean addBDCDY(String qlid) {
		boolean bsuccess = false;
		CommonDao dao = getCommonDao();
		String ywrStr = "";
		BDCS_QL_XZ bdcs_ql_xz = dao.get(BDCS_QL_XZ.class, qlid);
		if (bdcs_ql_xz != null) {
			List<BDCS_DJDY_XZ> djdys = dao.getDataList(BDCS_DJDY_XZ.class, " DJDYID ='" + bdcs_ql_xz.getDJDYID() + "'");
			if (djdys != null && djdys.size() > 0) {
				BDCS_DJDY_GZ djdy = ObjectHelper.copyDJDY_XZToGZ(djdys.get(0));
				if (djdy != null) {

					RealUnit unit = null;
					try {
						UnitTools.loadUnit(this.getBdcdylx(), DJDYLY.initFrom(djdy.getLY()), djdy.getBDCDYID());
					} catch (Exception e) {
					}
					BDCS_QL_GZ ql = super.createQL(djdy, unit);
					ql.setLYQLID(bdcs_ql_xz.getId());
					// 生成附属权利
					BDCS_FSQL_GZ fsql = super.createFSQL(djdy.getDJDYID());
					fsql.setQLID(ql.getId());
					ql.setFSQLID(fsql.getId());

					BDCS_QDZR_GZ qdzr = new BDCS_QDZR_GZ();
					qdzr.setXMBH(getXMBH());
					qdzr.setQLID(ql.getId());
					List<BDCS_QLR_XZ> qlr_xzs = dao.getDataList(BDCS_QLR_XZ.class, " QLID ='" + qlid + "'");
					if(qlr_xzs.size()>0){
						for(BDCS_QLR_XZ qlr : qlr_xzs){
							BDCS_SQR sqr = new BDCS_SQR();
							try{
								sqr.setSQRXM(qlr.getQLRMC());
								sqr.setZJLX(qlr.getZJZL());
								sqr.setZJH(qlr.getZJH());
								sqr.setLXDH(qlr.getDH());
								sqr.setGYFS(qlr.getGYFS());
								sqr.setFZJG(qlr.getFZJG());
								sqr.setGJDQ(qlr.getGJ());
								sqr.setTXDZ(qlr.getDZ());
								sqr.setGZDW(qlr.getGZDW());
								sqr.setXB(qlr.getXB());
								sqr.setHJSZSS(qlr.getHJSZSS());
								sqr.setSSHY(qlr.getSSHY());
								sqr.setSQRLX(qlr.getQLRLX());
								sqr.setQLBL(qlr.getQLBL());
								sqr.setSQRLB("2");//乙方

								sqr.setYXBZ(qlr.getYXBZ());
								sqr.setFDDBR(qlr.getFDDBR());
								sqr.setFDDBRDH(qlr.getFDDBRDH());
								sqr.setDLRXM(qlr.getDLRXM());
								sqr.setDLRLXDH(qlr.getDLRLXDH());
								sqr.setDLRZJHM(qlr.getDLRZJHM());
								sqr.setDLRZJLX(qlr.getDLRZJLX());
								sqr.setDLJGMC(qlr.getDLJGMC());
								sqr.setXMBH(ql.getXMBH());
								if (qlr.getQLMJ() != null) {
									sqr.setQLMJ(String.valueOf(qlr.getQLMJ()));
								}
								sqr.setFDDBRZJLX(qlr.getFDDBRZJLX());
								 sqr.setId((String) SuperHelper.GeneratePrimaryKey());
								sqr.setDZYJ(qlr.getDZYJ());
								sqr.setYZBM(qlr.getYB());
								if (null != qlr.getSXH()) {
									sqr.setSXH(qlr.getSXH().toString());
								}
								sqr.setCREATETIME(qlr.getCREATETIME());
								sqr.setMODIFYTIME(qlr.getMODIFYTIME());
								
								dao.save(sqr);
								ywrStr +=qlr.getQLRMC()+",";
							}catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
					if(!StringHelper.isEmpty(ywrStr)){
						String ywr = ywrStr.substring(0, ywrStr.length()-1);
						fsql.setYWR(ywr);
						System.out.println(ywr);
					}
//					qdzr.setQLRID(qlr_xz.get(0).getId());
					BDCS_ZS_GZ zs = new BDCS_ZS_GZ();
					zs.setXMBH(getXMBH());
					qdzr.setZSID(zs.getId());

					qdzr.setBDCDYH(ql.getBDCDYH());
					qdzr.setDJDYID(ql.getDJDYID());
					qdzr.setFSQLID(fsql.getId());

					// 设置为共同持证
					ql.setCZFS(CZFS.GTCZ.Value);

					dao.save(zs);
					dao.save(qdzr);

					// 保存权利和附属权利
					djdy.setLY("02");
					djdy.setId(getPrimaryKey());
					djdy.setXMBH(getXMBH());
					dao.save(ql);
					dao.save(fsql);
					dao.save(djdy);
				}
			}
		}
		dao.flush();
		bsuccess = true;
		return bsuccess;
	}

	/**
	 * 写入登记薄
	 */
	@Override
	public boolean writeDJB() {
		String strSqlXMBH = ProjectHelper.GetXMBHCondition(getXMBH());
		List<BDCS_DJDY_GZ> djdys = getCommonDao().getDataList(BDCS_DJDY_GZ.class, strSqlXMBH);

		String dbr = Global.getCurrentUserName();
		Date dbsj=new Date();
		
		CommonDao dao = getCommonDao();
		List<BDCS_SQR> list = dao.getDataList(BDCS_SQR.class, "XMBH='" + getXMBH() + "'");
		if (djdys != null && djdys.size() > 0) {
			for (int idjdy = 0; idjdy < djdys.size(); idjdy++) {
				BDCS_DJDY_GZ bdcs_djdy_gz = djdys.get(idjdy);

				if (bdcs_djdy_gz != null) {
					String djdyid = bdcs_djdy_gz.getDJDYID();

					// 把申请人拷贝到权利人
					BDCS_QL_GZ ql = null;
					BDCS_QDZR_GZ qdzr = null;
					if (list != null && list.size() > 0) {
						List<BDCS_QL_GZ> listqls = dao.getDataList(BDCS_QL_GZ.class, "XMBH='" + getXMBH() + "' and DJDYID='" + djdyid + "'");
						if (listqls != null && listqls.size() > 0) {
							ql = listqls.get(0);
						}
						List<BDCS_QDZR_GZ> listqdzrs = dao.getDataList(BDCS_QDZR_GZ.class, "XMBH='" + getXMBH() + "' AND QLID='" + ql.getId() + "'");
						if (listqdzrs != null && listqdzrs.size() > 0) {
							qdzr = listqdzrs.get(0);
						}
						int i = 0;
						for (BDCS_SQR sqr : list) {
							
							String sql=MessageFormat.format("XMBH=''{0}'' AND QLRMC=''{1}'' AND ZJH=''{2}''", getXMBH(),sqr.getSQRXM(),sqr.getZJH());
							List<BDCS_QLR_GZ> listqlr=dao.getDataList(BDCS_QLR_GZ.class, sql);
							if(listqlr!=null && listqlr.size()>0)
								continue;
							
							BDCS_QLR_GZ qlr = ObjectHelper.CopySQRtoQLR(sqr);
							qlr.setQLID(ql.getId());
							qlr.setXMBH(ql.getXMBH());
							if (i == 0) {
								qdzr.setQLRID(qlr.getId());
							} else {
								BDCS_QDZR_GZ qdzrnew = new BDCS_QDZR_GZ();
								ObjectHelper.copyObject(qdzr, qdzrnew);
								qdzrnew.setId((String) SuperHelper.GeneratePrimaryKey());
								qdzrnew.setQLRID(qlr.getId());
								dao.save(qdzrnew);
							}
							dao.save(qlr);
							i++;
						}
					}
					dao.flush();

//					super.CopyGZQLToXZAndLS(djdyid);
//					super.CopyGZQLRToXZAndLS(djdyid);
//					super.CopyGZQDZRToXZAndLS(djdyid);
//					super.CopyGZZSToXZAndLS(djdyid);
					
					ql.setDBR(dbr);
					ql.setDJSJ(dbsj);
					ql.setDJJG(ConfigHelper.getNameByValue("DJJGMC"));
					
					Rights xzrights = RightsTools.copyRightsAll(DJDYLY.GZ, DJDYLY.XZ, ql);
					Rights lsrights = RightsTools.copyRightsAll(DJDYLY.GZ, DJDYLY.LS, ql);
					
					
					dao.save(xzrights);
					dao.save(lsrights);
					dao.update(ql);
				}
			}
		}
		this.SetSFDB();
		getCommonDao().flush();
		super.alterCachedXMXX();
		return true;
	}

	/**
	 * 移除登记产单元
	 */
	@Override
	public boolean removeBDCDY(String lyqlid) {

		List<BDCS_QL_GZ> qls = getCommonDao().getDataList(BDCS_QL_GZ.class, " LYQLID='" + lyqlid + "' AND XMBH='" + getXMBH() + "'");
		if (qls != null) {
			getCommonDao().delete(BDCS_SQR.class, "XMBH='" + getXMBH() + "'");
			getCommonDao().delete(BDCS_FSQL_GZ.class, qls.get(0).getFSQLID());
			getCommonDao().deleteEntitysByHql(BDCS_DJDY_GZ.class, " DJDYID='" + qls.get(0).getDJDYID() + "' AND XMBH='" + getXMBH() + "'");
			getCommonDao().deleteEntitysByHql(BDCS_SQR.class, "XMBH='" + getXMBH() + "'");
			getCommonDao().deleteEntitysByHql(BDCS_QLR_GZ.class, "XMBH='" + getXMBH() + "'");
			getCommonDao().deleteEntity(qls.get(0));
		}
		getCommonDao().flush();
		return true;
	}

	/**
	 * 获取不动产单元列表
	 */
	@Override
	public List<UnitTree> getUnitTree() {
		return super.getRightList();
	}

	/**
	 * 根据申请人ID数组添加申请人
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

	@Override
	public Map<String,String> exportXML(String path, String actinstID) {
		Marshaller mashaller;
		Map<String, String> names = new HashMap<String, String>();
		try {
			CommonDao dao = this.getCommonDao();
			String xmbhHql = ProjectHelper.GetXMBHCondition(this.getXMBH());
			List<BDCS_DJDY_GZ> djdys = dao.getDataList(BDCS_DJDY_GZ.class, xmbhHql);
			if (djdys != null && djdys.size() > 0) {
				mashaller = JAXBContext.newInstance(Message.class).createMarshaller();
				Calendar calendar = Calendar.getInstance(); // 创建一个日历对象
				calendar.setTime(new Date()); // 用当前时间初始化日历时间
				String cyear = calendar.get(Calendar.YEAR) + "";
				BDCS_XMXX xmxx = dao.get(BDCS_XMXX.class, this.getXMBH());
				String result = "";
				for(int i = 0; i < djdys.size(); i ++){
					BDCS_DJDY_GZ djdy = djdys.get(i);
					String ywh = packageXml.GetYWLSHByYWH(this.getProject_id());
					BDCS_QL_GZ ql = this.getQL(djdy.getDJDYID());
					BDCS_FSQL_GZ fsql = new BDCS_FSQL_GZ();
					if(ql != null && !StringUtils.isEmpty(ql.getFSQLID())){
						fsql = dao.get(BDCS_FSQL_GZ.class, ql.getFSQLID());
					}
					List<BDCS_QLR_GZ> qlrs = this.getQLRs(ql.getId());
					List<BDCS_SQR> sqrs = new ArrayList<BDCS_SQR>();
					ProjectServiceImpl serviceImpl = SuperSpringContext.getContext().getBean(ProjectServiceImpl.class);
					sqrs = serviceImpl.getSQRList(super.getXMBH());
					Message msg = exchangeFactory.createMessageByYYDJ();
					if (QLLX.QTQL.Value.equals(this.getQllx().Value) && BDCDYLX.SHYQZD.Value.equals(this.getBdcdylx().Value)) { // 国有建设使用权
						try {
							BDCS_SHYQZD_XZ zd = dao.get(BDCS_SHYQZD_XZ.class, djdy.getBDCDYID());
							UseLand land = zd;
							super.fillHead(msg, i, ywh,zd.getBDCDYH(),zd.getQXDM(),ql.getLYQLID());
							msg.getHead().setParcelID(StringHelper.formatObject(zd.getZDDM()));
							msg.getHead().setEstateNum(StringHelper.formatObject(zd.getBDCDYH()));
//							msg.getHead().setPreEstateNum(StringHelper.formatObject(zd.getBDCDYH()));
							if(zd != null && !StringUtils.isEmpty(zd.getQXDM())){
								msg.getHead().setAreaCode(zd.getQXDM());
							}
							if (djdy != null) {

								List<ZTTGYQLR> zttqlr = msg.getData().getGYQLR();
								zttqlr = packageXml.getZTTGYQLRs(zttqlr, qlrs, zd, ql,null, null, null, null);
								msg.getData().setGYQLR(zttqlr);
								
								QLFQLYYDJ yydj = msg.getData().getQLFQLYYDJ();
								yydj = packageXml.getQLFQLYYDJ(yydj, ql, fsql, ywh, null, land);

								DJTDJSLSQ sq = msg.getData().getDJSLSQ();
								sq = packageXml.getDJTDJSLSQ(sq, ywh, land, ql, xmxx,null, xmxx.getSLSJ(), null, null, null);
								
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
					if(QLLX.QTQL.Value.equals(this.getQllx().Value) && BDCDYLX.H.Value.equals(this.getBdcdylx().Value)){ //房屋所有权
						try {
							h = dao.get(BDCS_H_XZ.class, djdy.getBDCDYID());
							if(h != null){
								BDCS_SHYQZD_XZ zd = dao.get(BDCS_SHYQZD_XZ.class, h.getZDBDCDYID());
								h.setZDDM(zd.getZDDM());
							}
							super.fillHead(msg, i, ywh,h.getBDCDYH(),h.getQXDM(),ql.getLYQLID());
							msg.getHead().setParcelID(StringHelper.formatObject(h.getZDDM()));
							msg.getHead().setEstateNum(StringHelper.formatObject(h.getBDCDYH()));
//							msg.getHead().setPreEstateNum(StringHelper.formatObject(h.getBDCDYH()));
							if(h != null && !StringUtils.isEmpty(h.getQXDM())){
								msg.getHead().setAreaCode(h.getQXDM());
							}
							if (djdy != null) {
								
								List<ZTTGYQLR> zttqlr = msg.getData().getGYQLR();
								zttqlr = packageXml.getZTTGYQLRs(zttqlr, qlrs, null, ql,h, null, null, null);
								msg.getData().setGYQLR(zttqlr);
									
								QLFQLYYDJ yydj = msg.getData().getQLFQLYYDJ();
								yydj = packageXml.getQLFQLYYDJ(yydj, ql,fsql, ywh, h, null);
								
								DJTDJSLSQ sq = msg.getData().getDJSLSQ();
								sq = packageXml.getDJTDJSLSQ(sq, ywh, null, ql, xmxx, h ,xmxx.getSLSJ(), null, null, null);
								
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
								msg.getData().setDJSQR(djsqrs);
								
								FJF100 fj = msg.getData().getFJF100();
								fj = packageXml.getFJF(fj);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					msg.getHead().setRecType("6000101");
					mashaller.marshal(msg, new File(path + "Biz" + msg.getHead().getBizMsgID()+ ".xml"));
					result = uploadFile(path + "Biz" + msg.getHead().getBizMsgID()+ ".xml", ConstValue.RECCODE.YY_ZXDJ.Value,actinstID,djdy.getDJDYID(),ql.getId());
					names.put(djdy.getDJDYID(),msg.getHead().getBizMsgID()+ ".xml");
					
					if(null == result){
						Map<String, String> xmlError = new HashMap<String, String>();
						xmlError.put("error", "连接SFTP失败,请检查服务器和前置机的连接是否正常");
						YwLogUtil.addSjsbResult("Biz" + msg.getHead().getBizMsgID()+ ".xml", "", "连接SFTP失败,请检查服务器和前置机的连接是否正常", ConstValue.SF.NO.Value, ConstValue.RECCODE.YY_ZXDJ.Value,ProjectHelper.getpRroinstIDByActinstID(actinstID));
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
				Rights bdcql = RightsTools.loadRightsByDJDYID(ConstValue.DJDYLY.GZ, getXMBH(), djdy.getDJDYID());
				SubRights bdcfsql = RightsTools.loadSubRightsByRightsID(ConstValue.DJDYLY.GZ, bdcql.getId());
				List<RightsHolder> bdcqlrs = RightsHolderTools.loadRightsHolders(ConstValue.DJDYLY.GZ, djdy.getDJDYID(), getXMBH());
				MessageExport msg = super.getShareMsgTools().GetMsg(bdcdy, bdcql, bdcfsql, bdcqlrs, bljc, xmxx);
				super.getShareMsgTools().SendMsg(msg, idjdy + 1, djdy.getBDCDYLX(), xmxx);
			}
		}
	}
}
