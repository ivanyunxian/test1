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
import com.supermap.realestate.registration.model.BDCS_DJDY_XZ;
import com.supermap.realestate.registration.model.BDCS_FSQL_GZ;
import com.supermap.realestate.registration.model.BDCS_FSQL_LS;
import com.supermap.realestate.registration.model.BDCS_H_LS;
import com.supermap.realestate.registration.model.BDCS_H_XZ;
import com.supermap.realestate.registration.model.BDCS_H_XZY;
import com.supermap.realestate.registration.model.BDCS_QLR_XZ;
import com.supermap.realestate.registration.model.BDCS_QL_GZ;
import com.supermap.realestate.registration.model.BDCS_QL_LS;
import com.supermap.realestate.registration.model.BDCS_QL_XZ;
import com.supermap.realestate.registration.model.BDCS_SHYQZD_LS;
import com.supermap.realestate.registration.model.BDCS_SHYQZD_XZ;
import com.supermap.realestate.registration.model.BDCS_SQR;
import com.supermap.realestate.registration.model.BDCS_XMXX;
import com.supermap.realestate.registration.model.interfaces.House;
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
import com.supermap.realestate.registration.util.ConstValue.DJDYLY;
import com.supermap.realestate.registration.util.ConstValue.QLLX;
import com.supermap.realestate.registration.util.ConstValue.SQRLB;
import com.supermap.realestate.registration.util.Global;
import com.supermap.realestate.registration.util.ProjectHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.realestate.registration.util.YwLogUtil;
import com.supermap.wisdombusiness.core.SuperSpringContext;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;

/*
 1、国有建设用地灭失登记
 2、集体建设用地灭失登记）
 3、宅基地灭失登记
 4、国有建设用地使用权/房屋所有权灭失登记
 5、集体建设用地使用权/房屋所有权灭失登记
 6、宅基地使用权/房屋所有权灭失登记
 */
/**
 * 
 * 单元灭失登记
 * @ClassName: ZXDJHandler
 * @author liushufeng
 * @date 2015年9月8日 下午10:44:52
 */
public class ZXDJHandler2 extends DJHandlerBase implements DJHandler {

	/**
	 * 构造函数
	 * 
	 * @param info
	 */
	public ZXDJHandler2(ProjectInfo info) {
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
		if (djdy != null) {
			djdy.setLY(DJDYLY.LS.Value);
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

			List<Rights> rightss = (List<Rights>) RightsTools.loadRightsByCondition(DJDYLY.XZ, "DJDYID='" + djdy.getDJDYID() + "' AND QLLX IN ('1','2','3','4','5','6','7','8','15','24')");
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
								sqr.setGLQLID(ql.getId());
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
		 */
		CommonDao dao = getCommonDao();
		String dbr = Global.getCurrentUserName();
		String strSqlXMBH = ProjectHelper.GetXMBHCondition(getXMBH());
		List<BDCS_DJDY_GZ> djdys = dao.getDataList(BDCS_DJDY_GZ.class, strSqlXMBH);
		if (djdys != null && djdys.size() > 0) {
			for (int i = 0; i < djdys.size(); i++) {
				BDCS_DJDY_GZ djdy = djdys.get(i);
				String qllxarray = " ('1','2','3','4','5','6','7','8','9','10','11','12','13','14','15','16','17','18','24')";
				// String sql = MessageFormat.format(
				// " DJDYID={{0}} AND QLLX IN {{1}}", djdy.getDJDYID(),
				// qllxarray);
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
					for(BDCS_DJDY_XZ djdy_xz:djdys_xz){
						String bdcdyid=djdy_xz.getBDCDYID();
						if(!StringHelper.isEmpty(bdcdyid)){
							RealUnit unit_xz=UnitTools.loadUnit(BDCDYLX.initFrom(djdy_xz.getBDCDYLX()), DJDYLY.XZ, bdcdyid);
							if(unit_xz!=null){
								dao.deleteEntity(unit_xz);
								if(unit_xz instanceof House){
									House house = (House) unit_xz;
									String zrzbdcdyid=house.getZRZBDCDYID();
									zrzbdcdyids.add(zrzbdcdyid);
								}
								super.DeleteGeo(bdcdyid, BDCDYLX.initFrom(djdy_xz.getBDCDYLX()), DJDYLY.XZ);
							}
						}
						dao.deleteEntity(djdy_xz);
					}
					dao.flush();
					if(zrzbdcdyids.size()>0&&djdys_xz!=null&&djdys_xz.size()>0){
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
											super.DeleteGeo(zrzbdcdyid, BDCDYLX.ZRZ, DJDYLY.XZ);
											String deleteFulSql_bdck = "delete from bdck.bdck_zrz_xz where bdcdyid = '"+zrzbdcdyid+"'";
											dao.updateBySql(deleteFulSql_bdck);
											String deleteFulSql_bdcdck = "delete from bdcdck.bdck_zrz_xz where bdcdyid = '"+zrzbdcdyid+"'";
											dao.updateBySql(deleteFulSql_bdcdck);
										}
									}else{
										
									}
								}else{
									List<BDCS_H_XZY> hxzy=dao.getDataList(BDCS_H_XZY.class, "ZRZBDCDYID='"+zrzbdcdyid+"'");
									if(hxzy.size()==0){
										RealUnit unit_zrz=UnitTools.loadUnit(BDCDYLX.YCZRZ, DJDYLY.XZ, zrzbdcdyid);
										if(unit_zrz!=null){
											dao.deleteEntity(unit_zrz);
											super.DeleteGeo(zrzbdcdyid, BDCDYLX.YCZRZ, DJDYLY.XZ);
											String deleteFulSql_bdck = "delete from bdck.bdck_zrz_xz where bdcdyid = '"+zrzbdcdyid+"'";
											dao.updateBySql(deleteFulSql_bdck);
											String deleteFulSql_bdcdck = "delete from bdcdck.bdck_zrz_xz where bdcdyid = '"+zrzbdcdyid+"'";
											dao.updateBySql(deleteFulSql_bdcdck);
										}
									}else{
										
									}
								}
							}else{
								
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
	 * 移除登记产单元
	 */
	@Override
	public boolean removeBDCDY(String bdcdyid) {
		BDCS_DJDY_GZ djdy = super.removeDJDY(bdcdyid);
        if(djdy!=null){
        	//删除申请人
			RemoveSQRByQLID(djdy.getDJDYID(), getXMBH()) ;
        	Rights _rights = RightsTools.loadRightsByDJDYID(DJDYLY.GZ, getXMBH(), djdy.getDJDYID());
    		if (_rights != null) {
    			RightsTools.deleteRightsAndSubRights(DJDYLY.GZ, _rights.getId());
    		}
    		getCommonDao().flush();
    		
        }
		return true;
	}

	/**
	 * 获取不动产单元列表
	 */
	@Override
	public List<UnitTree> getUnitTree() {
		List<UnitTree> list_new=new ArrayList<UnitTree>();
		List<UnitTree> list=super.getUnitList();
		for(UnitTree tree:list){
			String qlid=tree.getQlid();
			if(!StringHelper.isEmpty(qlid)){
				Rights ql=RightsTools.loadRights(DJDYLY.GZ, qlid);
				if(ql!=null){
					tree.setOldqlid(ql.getLYQLID());
				}
			}
			list_new.add(tree);
		}
		return list_new;
	}

	/**
	 * 根据申请人ID数组添加申请人
	 */
	@Override
	public void addQLRbySQRArray(String qlid, Object[] sqrids) {
	}

	/**
	 * 移除权利人
	 */
	@Override
	public void removeQLR(String qlid, String qlrid) {
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
	public Map<String, String> exportXML(String path, String actinstID) {
		Marshaller mashaller;
		Map<String, String> names = new HashMap<String, String>();
		try {
			CommonDao dao = super.getCommonDao();
			String xmbhHql = ProjectHelper.GetXMBHCondition(super.getXMBH());
			List<BDCS_DJDY_GZ> djdys = dao.getDataList(BDCS_DJDY_GZ.class, xmbhHql);
			
			if (djdys != null && djdys.size() > 0) {
				mashaller = JAXBContext.newInstance(Message.class).createMarshaller();
				Calendar calendar = Calendar.getInstance(); // 创建一个日历对象
				calendar.setTime(new Date()); // 用当前时间初始化日历时间
				String cyear = calendar.get(Calendar.YEAR) + "";
				String result = "";
				for (int i = 0; i < djdys.size(); i++) {
					BDCS_DJDY_GZ djdy = djdys.get(i);
					String ywh = packageXml.GetYWLSHByYWH(this.getProject_id());
					BDCS_QL_GZ ql = super.getQL(djdy.getDJDYID());
					// List<BDCS_QLR_GZ> qlrs = super.getQLRs(ql.getId());
					List<BDCS_SQR> sqrs = new ArrayList<BDCS_SQR>();
					Message msg = exchangeFactory.createMessageByZXDJ();
					ProjectServiceImpl serviceImpl = SuperSpringContext.getContext().getBean(ProjectServiceImpl.class);
					sqrs = serviceImpl.getSQRList(super.getXMBH());

					BDCS_XMXX xmxx = dao.get(BDCS_XMXX.class, super.getXMBH());
					BDCS_H_LS h = null;
					if (djdy != null) {
						msg.getHead().setRecType("4000101");
						if (QLLX.GYJSYDSHYQ.Value.equals(this.getQllx().Value) || QLLX.ZJDSYQ.Value.equals(this.getQllx().Value) || QLLX.JTJSYDSYQ.Value.equals(this.getQllx().Value)) { // 国有建设使用权、宅基地、集体建设用地使用权
							try {
								UseLand zd = dao.get(BDCS_SHYQZD_XZ.class, djdy.getBDCDYID());
								if(zd==null) {
									zd=dao.get(BDCS_SHYQZD_LS.class, djdy.getBDCDYID());
								}
								
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
								List<DJFDJSQR> djsqrs = msg.getData().getDJSQR();
								djsqrs = packageXml.getDJSQRs(djsqrs, sqrs, zd.getYSDM(), ywh, zd.getBDCDYH());
								msg.getData().setDJSQR(djsqrs);

								FJF100 fj = msg.getData().getFJF100();
								fj = packageXml.getFJF(fj);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
						if (QLLX.GYJSYDSHYQ_FWSYQ.Value.equals(this.getQllx().Value)) { // 房屋所有权
							try {
								h = dao.get(BDCS_H_LS.class, djdy.getBDCDYID());
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
								msg.getHead().setParcelID(StringHelper.formatObject(h.getZDDM()));
								msg.getHead().setEstateNum(StringHelper.formatObject(h.getBDCDYH()));
//								msg.getHead().setPreEstateNum(StringHelper.formatObject(h.getBDCDYH()));
								List<DJFDJSQR> djsqrs = msg.getData().getDJSQR();
								djsqrs = packageXml.getDJSQRs(djsqrs, sqrs, h.getYSDM(), ywh, h.getBDCDYH());
								msg.getData().setDJSQR(djsqrs);
								FJF100 fj = msg.getData().getFJF100();
								fj = packageXml.getFJF(fj);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
					mashaller.marshal(msg, new File(path + "Biz" + msg.getHead().getBizMsgID() + ".xml"));
					result = uploadFile(path + "Biz" + msg.getHead().getBizMsgID() + ".xml",ConstValue.RECCODE.ZX_ZXDJ.Value,actinstID,djdy.getDJDYID(),ql.getId());
					names.put(djdy.getDJDYID(), msg.getHead().getBizMsgID() + ".xml");
					if(null == result){
						Map<String, String> xmlError = new HashMap<String, String>();
						xmlError.put("error", "连接SFTP失败,请检查服务器和前置机的连接是否正常");
						YwLogUtil.addSjsbResult("Biz" + msg.getHead().getBizMsgID() + ".xml", "", "连接SFTP失败,请检查服务器和前置机的连接是否正常", ConstValue.SF.NO.Value, ConstValue.RECCODE.ZX_ZXDJ.Value,ProjectHelper.getpRroinstIDByActinstID(actinstID));
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
