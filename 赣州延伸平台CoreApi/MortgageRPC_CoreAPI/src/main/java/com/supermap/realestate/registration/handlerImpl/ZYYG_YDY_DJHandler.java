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
import com.supermap.realestate.registration.dataExchange.dyq.QLFQLDYAQ;
import com.supermap.realestate.registration.dataExchange.hy.KTFZHYHZK;
import com.supermap.realestate.registration.dataExchange.utils.packageXml;
import com.supermap.realestate.registration.dataExchange.ygdj.QLFQLYGDJ;
import com.supermap.realestate.registration.factorys.HandlerFactory;
import com.supermap.realestate.registration.handler.DJHandler;
import com.supermap.realestate.registration.model.BDCS_DJDY_GZ;
import com.supermap.realestate.registration.model.BDCS_DJDY_XZ;
import com.supermap.realestate.registration.model.BDCS_FSQL_GZ;
import com.supermap.realestate.registration.model.BDCS_FSQL_XZ;
import com.supermap.realestate.registration.model.BDCS_H_GZ;
import com.supermap.realestate.registration.model.BDCS_H_XZ;
import com.supermap.realestate.registration.model.BDCS_H_XZY;
import com.supermap.realestate.registration.model.BDCS_QLR_GZ;
import com.supermap.realestate.registration.model.BDCS_QLR_XZ;
import com.supermap.realestate.registration.model.BDCS_QL_GZ;
import com.supermap.realestate.registration.model.BDCS_QL_XZ;
import com.supermap.realestate.registration.model.BDCS_SHYQZD_GZ;
import com.supermap.realestate.registration.model.BDCS_SHYQZD_XZ;
import com.supermap.realestate.registration.model.BDCS_SLLM_GZ;
import com.supermap.realestate.registration.model.BDCS_SQR;
import com.supermap.realestate.registration.model.BDCS_SYQZD_GZ;
import com.supermap.realestate.registration.model.BDCS_XMXX;
import com.supermap.realestate.registration.model.BDCS_YHZK_GZ;
import com.supermap.realestate.registration.model.BDCS_YHZK_XZ;
import com.supermap.realestate.registration.model.BDCS_ZH_GZ;
import com.supermap.realestate.registration.model.BDCS_ZH_XZ;
import com.supermap.realestate.registration.model.BDCS_ZRZ_GZ;
import com.supermap.realestate.registration.model.BDCS_ZRZ_XZ;
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
import com.supermap.realestate.registration.util.ConstValue;
import com.supermap.realestate.registration.util.ConstValue.BDCDYLX;
import com.supermap.realestate.registration.util.ConstValue.CZFS;
import com.supermap.realestate.registration.util.ConstValue.DJDYLY;
import com.supermap.realestate.registration.util.ConstValue.DJZT;
import com.supermap.realestate.registration.util.ConstValue.QLLX;
import com.supermap.realestate.registration.util.ConstValue.SQRLB;
import com.supermap.realestate.registration.util.ConstValue.YGDJLX;
import com.supermap.realestate.registration.util.ConstValue.ZSBS;
import com.supermap.realestate.registration.util.ObjectHelper;
import com.supermap.realestate.registration.util.ProjectHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.realestate.registration.util.YwLogUtil;
import com.supermap.wisdombusiness.core.SuperSpringContext;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;

/*
 1、转移预告登记+抵押预告登记
 */
/**
 * 
 * 转移预告登记+预抵押登记操作类
 * @ClassName: ZYYG_YDY_DJHandler
 * @author liushufeng
 * @date 2015年9月8日 下午10:43:16
 */
public class ZYYG_YDY_DJHandler extends DJHandlerBase implements DJHandler {

	/**
	 * 构造函数
	 * @param info
	 */
	public ZYYG_YDY_DJHandler(ProjectInfo info) {
		super(info);
	}

	/**
	 * 添加不动产单元
	 */
	@Override
	public boolean addBDCDY(String qlids) {
		boolean bsuccess = false;
		if(StringHelper.isEmpty(qlids)){
			return false;
		}
		String[] listqlid = qlids.split(",");
		if (listqlid == null || listqlid.length <= 0)
			return false;
		CommonDao dao = getCommonDao();
		List<String> listDJDY=new ArrayList<String>();
		for(int iql=0;iql<listqlid.length;iql++){
			String qlid=listqlid[iql];
			Rights ql=RightsTools.loadRights(DJDYLY.XZ, qlid);
			if(ql!=null){
				BDCS_DJDY_GZ bdcs_djdy_gz=null;
				// 拷贝登记单元
				if(!StringHelper.isEmpty(ql.getDJDYID())&&!listDJDY.contains(ql.getDJDYID()))
				{
					listDJDY.add(ql.getDJDYID());
					List<BDCS_DJDY_XZ> list=getCommonDao().getDataList(BDCS_DJDY_XZ.class, "DJDYID='"+ql.getDJDYID()+"'");
					if(list!=null&&list.size()>0){
						bdcs_djdy_gz = new BDCS_DJDY_GZ();
						bdcs_djdy_gz.setXMBH(this.getXMBH());
						bdcs_djdy_gz.setDJDYID(list.get(0).getDJDYID());
						bdcs_djdy_gz.setBDCDYID(list.get(0).getBDCDYID());
						bdcs_djdy_gz.setBDCDYLX(this.getBdcdylx().Value);
						bdcs_djdy_gz.setBDCDYH(list.get(0).getBDCDYH());
						bdcs_djdy_gz.setId(getPrimaryKey());
						bdcs_djdy_gz.setLY(DJDYLY.XZ.Value);
						getCommonDao().save(bdcs_djdy_gz);
					}
				}
				String gzqlid="";
				// 拷贝权利信息（权证号为空）
				
				BDCS_QL_GZ bdcs_ql_gz=CopyQLXXFromXZ(qlid,ql.getDJDYID());
				bdcs_ql_gz.setQLLX(super.getQllx().Value);
				bdcs_ql_gz.setDJLX(super.getDjlx().Value);
				gzqlid=bdcs_ql_gz.getId();
				CopySQRFromXZQLR(qlid,gzqlid);
				ql.setDJZT(DJZT.DJZ.Value);
				
				
				RealUnit unit=UnitTools.loadUnit(BDCDYLX.initFrom(bdcs_djdy_gz.getBDCDYLX()), DJDYLY.initFrom(bdcs_djdy_gz.getLY()), bdcs_djdy_gz.getBDCDYID());
				
				// 生成抵押权
				BDCS_QL_GZ dyql = super.createQL(bdcs_djdy_gz, unit);
				// 生成抵押权附属权利
				BDCS_FSQL_GZ dyfsql = super.createFSQL(bdcs_djdy_gz.getDJDYID());

				// 关联抵押权权利-附属权利，设置抵押物类型、抵押不动产类型、预告登记种类
				dyql.setFSQLID(dyfsql.getId());
				dyql.setQLLX(QLLX.DIYQ.Value);
				dyql.setCZFS(CZFS.GTCZ.Value);//设置持证方式为共同持证。
				dyfsql.setQLID(dyql.getId());
				dyfsql.setDYWLX(getDYBDCLXfromBDCDYLX(getBdcdylx()));
				int dysw=RightsTools.getMaxMortgageSWS(bdcs_djdy_gz.getDJDYID());
				dyfsql.setDYSW(dysw+1);
				dyfsql.setDYBDCLX(getDYBDCLXfromBDCDYLX(getBdcdylx()));
				dyfsql.setYGDJZL(YGDJLX.ZQTBDCDYQYGDJ.Value.toString());
				dyfsql.setZJJZWZL(unit.getZL());
				dyql.setLYQLID(bdcs_ql_gz.getId());
				dao.save(dyql);
				dao.save(dyfsql);
			}
		}
		dao.flush();
		bsuccess = true;
		return bsuccess;
	}
	
	
	/**
	 * 从现状拷贝权利信息，不带权利人
	 * 
	 * @Author：俞学斌
	 * @CreateTime 2015年6月22日 下午5:55:53
	 * @param qlid 权利id
	 * @param djdyid
	 *            新登记单元ID
	 * @return 状态字符串
	 */
	protected BDCS_QL_GZ CopyQLXXFromXZ(String qlid,String djdyid) {
		BDCS_QL_GZ bdcs_ql_gz=null;
		BDCS_QL_XZ bdcs_ql_xz = getCommonDao().get(BDCS_QL_XZ.class, qlid);
		if (bdcs_ql_xz != null) {
			String gzqlid = getPrimaryKey();
			String gzfsqlid = getPrimaryKey();
			// 拷贝权利
			bdcs_ql_gz = ObjectHelper.copyQL_XZToGZ(bdcs_ql_xz);
			bdcs_ql_gz.setId(gzqlid);
			bdcs_ql_gz.setQLLX(getQllx().Value);
			bdcs_ql_gz.setDJLX(getDjlx().Value);
			bdcs_ql_gz.setFSQLID(gzfsqlid);
			bdcs_ql_gz.setDJDYID(djdyid);
			bdcs_ql_gz.setXMBH(getXMBH());
			bdcs_ql_gz.setLYQLID(qlid);
			bdcs_ql_gz.setDJSJ(null);
			bdcs_ql_gz.setDBR("");
			bdcs_ql_gz.setBDCQZH("");
			bdcs_ql_gz.setDJYY("");
			bdcs_ql_gz.setFJ("");
			bdcs_ql_gz.setZSBS(ZSBS.DYB.Value);
			bdcs_ql_gz.setYWH(getProject_id());
			bdcs_ql_gz.setARCHIVES_BOOKNO("");
			bdcs_ql_gz.setARCHIVES_CLASSNO("");
			getCommonDao().save(bdcs_ql_gz);
			if (bdcs_ql_xz.getFSQLID() != null) {
				BDCS_FSQL_XZ bdcs_fsql_xz = getCommonDao().get(BDCS_FSQL_XZ.class, bdcs_ql_xz.getFSQLID());
				if (bdcs_fsql_xz != null) {
					// 拷贝附属权利
					BDCS_FSQL_GZ bdcs_fsql_gz = ObjectHelper.copyFSQL_XZToGZ(bdcs_fsql_xz);
					bdcs_fsql_gz.setQLID(gzqlid);
					bdcs_fsql_gz.setId(gzfsqlid);
					bdcs_fsql_gz.setYGDJZL(StringHelper.formatObject(YGDJLX.QTBDCMMYGDJ.Value));
					bdcs_fsql_gz.setDJDYID(djdyid);
					bdcs_fsql_gz.setXMBH(getXMBH());
					
					// 附属权利中赋值转移预告义务人信息
					StringBuilder ywr = new StringBuilder();
					StringBuilder ywrzjlx = new StringBuilder();
					StringBuilder ywrzjh = new StringBuilder();
					List<RightsHolder> ywrlist=RightsHolderTools.loadRightsHolders(DJDYLY.XZ, qlid);
					for (int i = 0; i < ywrlist.size(); i++) {
						if (i != 0) {
							ywr.append("/");
							ywrzjlx.append("/");
							ywrzjh.append("/");
						}
						ywr.append(ywrlist.get(i).getQLRMC());
						ywrzjlx.append(ywrlist.get(i).getZJZL());
						ywrzjh.append(ywrlist.get(i).getZJH());
					}
					bdcs_fsql_gz.setYWR(ywr.toString());
					bdcs_fsql_gz.setYWRZJZL(ywrzjlx.toString());
					bdcs_fsql_gz.setYWRZJH(ywrzjh.toString());
					getCommonDao().save(bdcs_fsql_gz);
				}
			}
		}
		return bdcs_ql_gz;
	}

	/**
	 * 根据现状权利ID和工作权利ID拷贝现状权利人到申请人
	 * @Author：俞学斌
	 * @param xzqlid
	 *            
	 * @param gzqlid
	 *            工作权利ID
	 */
	private void CopySQRFromXZQLR(String xzqlid, String gzqlid) {
		StringBuilder builderQLR = new StringBuilder();
		builderQLR.append("QLID ='");
		builderQLR.append(xzqlid);
		builderQLR.append("'");
		List<BDCS_QLR_XZ> qlrs = getCommonDao().getDataList(BDCS_QLR_XZ.class, builderQLR.toString());
		if (qlrs == null || qlrs.size() <= 0) {
			return;
		}
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
				List<BDCS_SQR> sqrlist = getCommonDao().getDataList(BDCS_SQR.class, Sql);
				if (sqrlist != null && sqrlist.size() > 0) {
					bexists = true;
				}
			}
			if (!bexists) {
				String SQRID = getPrimaryKey();
				BDCS_SQR sqr = new BDCS_SQR();
				sqr.setGYFS(qlr.getGYFS());
				sqr.setFZJG(qlr.getFZJG());
				sqr.setGJDQ(qlr.getGJ());
				sqr.setGZDW(qlr.getGZDW());
				sqr.setXB(qlr.getXB());
				sqr.setHJSZSS(qlr.getHJSZSS());
				sqr.setSSHY(qlr.getSSHY());
				sqr.setYXBZ(qlr.getYXBZ());
				sqr.setQLBL(qlr.getQLBL());
				sqr.setQLMJ(StringHelper.formatObject(qlr.getQLMJ()));
				sqr.setSQRXM(qlr.getQLRMC());
				sqr.setSQRLB(SQRLB.YF.Value);
				sqr.setSQRLX(qlr.getQLRLX());
				sqr.setDZYJ(qlr.getDZYJ());
				sqr.setLXDH(qlr.getDH());
				sqr.setZJH(qlr.getZJH());
				sqr.setZJLX(qlr.getZJZL());
				sqr.setTXDZ(qlr.getDZ());
				sqr.setYZBM(qlr.getYB());
				sqr.setXMBH(getXMBH());
				sqr.setId(SQRID);
				sqr.setGLQLID(gzqlid);
				getCommonDao().save(sqr);
			}
		}
	}


	/**
	 * 写入登记薄
	 */
	@Override
	public boolean writeDJB() {
		
		if (super.isCForCFING()) {
			return false;
		}
		
		String strSqlXMBH = ProjectHelper.GetXMBHCondition(getXMBH());
		CommonDao dao = getCommonDao();
		List<BDCS_DJDY_GZ> djdys = dao.getDataList(BDCS_DJDY_GZ.class, strSqlXMBH);
		for (BDCS_DJDY_GZ bdcs_djdy_gz : djdys) {
			String djdyid = bdcs_djdy_gz.getDJDYID();
			// 拷贝相应的权利信息
			super.CopyGZQLToXZAndLS(djdyid);
			super.CopyGZQLRToXZAndLSNotOnQLLX(djdyid);
			super.CopyGZQDZRToXZAndLS(djdyid);
			super.CopyGZZSToXZAndLS(djdyid);
			super.CopyGZDJDYToXZAndLS(bdcs_djdy_gz.getId());

			BDCDYLX dylx = ProjectHelper.GetBDCDYLX(getXMBH());
			// 更新单元抵押状态
			SetDYDYZT(bdcs_djdy_gz.getBDCDYID(), dylx, "0");
		}
		this.SetSFDB();
		dao.flush();
		super.alterCachedXMXX();
		return true;
	}

	/**
	 * 移除登记产单元
	 */
	@Override
	public boolean removeBDCDY(String bdcdyid) {
		boolean bsuccess = false;
		// 找到登记单元表，移除记录
		CommonDao baseCommonDao = this.getCommonDao();
		BDCS_DJDY_GZ djdy = super.removeDJDY(bdcdyid);// list.get(0);
		if (djdy != null) {
			String djdyid = djdy.getDJDYID();

			//删除权利、附属权利、权利人、证书、权地证人关系
			String _hqlCondition=MessageFormat.format("XMBH=''{0}'' AND DJDYID=''{1}''", getXMBH(),djdyid);
			RightsTools.deleteRightsAllByCondition(DJDYLY.GZ, _hqlCondition);
		}
		baseCommonDao.flush();
		bsuccess = true;
		return bsuccess;
	}

	/**
	 * 获取不动产单元列表
	 */
	@Override
	public List<UnitTree> getUnitTree() {
		String xmbhFilter = ProjectHelper.GetXMBHCondition(getXMBH());
		CommonDao dao = this.getCommonDao();
		List<UnitTree> list = new ArrayList<UnitTree>();
		List<BDCS_DJDY_GZ> djdys = dao.getDataList(BDCS_DJDY_GZ.class, xmbhFilter);
		if (djdys != null && djdys.size() > 0) {
			for (int i = 0; i < djdys.size(); i++) {
				BDCS_DJDY_GZ djdy = djdys.get(i);
				UnitTree tree = new UnitTree();
				StringBuilder BuilderQL = new StringBuilder();
				BuilderQL.append(xmbhFilter).append(" AND DJDYID='").append(djdy.getDJDYID()).append("'");
				List<BDCS_QL_GZ> qls = dao.getDataList(BDCS_QL_GZ.class, BuilderQL.toString());
				if (qls != null) {
					for (int iql = 0; iql < qls.size(); iql++) {
						BDCS_QL_GZ ql = qls.get(iql);
						if (ql.getQLLX().equals(QLLX.DIYQ.Value)) {
							tree.setDIYQQlid(ql.getId());
						} else {
							tree.setQlid(ql.getId());
						}
					}
				}
				tree.setId(djdy.getBDCDYID());
				tree.setBdcdyid(djdy.getBDCDYID());
				tree.setBdcdylx(djdy.getBDCDYLX());
				tree.setDjdyid(djdy.getDJDYID());
				String ly = StringUtils.isEmpty(djdy.getLY()) ? "gz" : DJDYLY.initFrom(djdy.getLY()) == null ? "gz" : DJDYLY.initFrom(djdy.getLY()).Name;
				tree.setLy(ly);
				String zl = getZL(tree, djdy.getDJDYID(), djdy.getBDCDYLX(), djdy.getBDCDYID(), ly);
				tree.setText(zl);
				// 如果是户的话，把房号也加上
				if (djdy.getBDCDYLX().equals(BDCDYLX.H.Value)) {
					if (!StringHelper.isEmpty(djdy.getLY())) {
						DJDYLY ely = DJDYLY.initFrom(djdy.getLY());
						House house = (House) (UnitTools.loadUnit(BDCDYLX.H, ely, djdy.getBDCDYID()));
						if (house != null) {
							String fh = house.getFH();
							tree.setFh(fh);
							tree.setMj(house.getMJ());
							tree.setText("坐落:" + zl + "|" + "房号:" + (fh == null ? "" : fh));
						}
					}
				}
				tree.setZl(zl);
				list.add(tree);
			}
		}
		return list;
	}

	private String getZL(UnitTree tree, String djdyid, String bdcdylx, String bdcdyid, String djdyly) {
		String zl = "";
		CommonDao dao = getCommonDao();
		BDCDYLX dylx = BDCDYLX.initFrom(bdcdylx);
		if (djdyly.equals(DJDYLY.GZ.Name)) {
			if (dylx.equals(BDCDYLX.SHYQZD)) {
				BDCS_SHYQZD_GZ shyqzd = dao.get(BDCS_SHYQZD_GZ.class, bdcdyid);
				zl = shyqzd == null ? "" : shyqzd.getZL();
			} else if (dylx.equals(BDCDYLX.H)) {
				BDCS_H_GZ h = dao.get(BDCS_H_GZ.class, bdcdyid);
				tree.setCid(h.getCID());
				tree.setZdbdcdyid(h.getZDBDCDYID());
				tree.setZrzbdcdyid(h.getZRZBDCDYID());
				tree.setLjzbdcdyid(h.getLJZID());
				zl = h == null ? "" : h.getZL();
			} else if (dylx.equals(BDCDYLX.ZRZ)) {
				BDCS_ZRZ_GZ zrz = dao.get(BDCS_ZRZ_GZ.class, bdcdyid);
				tree.setZdbdcdyid(zrz.getZDBDCDYID());
				zl = zrz == null ? "" : zrz.getZL();
			} else if (dylx.equals(BDCDYLX.SYQZD))// sunhb-2015-06-23添加所有权宗地，获取坐落
			{
				BDCS_SYQZD_GZ syqzd = dao.get(BDCS_SYQZD_GZ.class, bdcdyid);
				zl = syqzd == null ? "" : syqzd.getZL();
			} else if (dylx.equals(BDCDYLX.HY))// sunhb-2015-06-23添加宗海，获取坐落
			{
				BDCS_ZH_GZ zh = dao.get(BDCS_ZH_GZ.class, bdcdyid);
				zl = zh == null ? "" : zh.getZL();
			} else if (dylx.equals(BDCDYLX.LD))// sunhb-2015-06-23添加林地，获取坐落
			{
				BDCS_SLLM_GZ ld = dao.get(BDCS_SLLM_GZ.class, bdcdyid);
				tree.setZdbdcdyid(ld.getZDBDCDYID());
				zl = ld == null ? "" : ld.getZL();
			} else {
				// TODO 刘树峰:获取其他类型不动产单元的坐落
			}
		} else {// 来源于现状，把原来的所有权/使用权的权利ID也加上
			if (dylx.equals(BDCDYLX.SHYQZD)) {
				BDCS_SHYQZD_XZ shyqzd = dao.get(BDCS_SHYQZD_XZ.class, bdcdyid);
				zl = shyqzd == null ? "" : shyqzd.getZL();
			} else if (dylx.equals(BDCDYLX.H)) {
				BDCS_H_XZ shyqzd = dao.get(BDCS_H_XZ.class, bdcdyid);
				if (shyqzd != null) {
					zl = shyqzd == null ? "" : shyqzd.getZL();

					tree.setCid(shyqzd.getCID());
					tree.setZdbdcdyid(shyqzd.getZDBDCDYID());
					tree.setZrzbdcdyid(shyqzd.getZRZBDCDYID());
					tree.setLjzbdcdyid(shyqzd.getLJZID());
				}

			} else if (dylx.equals(BDCDYLX.YCH)) {
				BDCS_H_XZY bdcs_h_xzy = dao.get(BDCS_H_XZY.class, bdcdyid);
				if (bdcs_h_xzy != null) {
					zl = bdcs_h_xzy.getZL();
					tree.setId(bdcs_h_xzy.getCID());
					tree.setZdbdcdyid(bdcs_h_xzy.getZDBDCDYID());
					tree.setZrzbdcdyid(bdcs_h_xzy.getZRZBDCDYID());
					tree.setLjzbdcdyid(bdcs_h_xzy.getLJZID());
				}
			} else if (dylx.equals(BDCDYLX.ZRZ)) {
				BDCS_ZRZ_XZ zrz = dao.get(BDCS_ZRZ_XZ.class, bdcdyid);
				tree.setZdbdcdyid(zrz.getZDBDCDYID());
				zl = zrz == null ? "" : zrz.getZL();
			} else {
				// TODO 刘树峰:获取其他类型不动产单元的坐落
			}
			// 这块的逻辑有点问题，原来的权利ID应该包含两种，一种是所有权/使用权ID，一种是他项权利ID，例如
			// 抵押权的转移，就包含了被抵押单元的所有权权利和转移前的抵押权
			String qllxarray = " ('1','2','3','4','5','6','7','8','9','10','11','12','13','14','15','16','17','18')";
			String hqlCondition = MessageFormat.format(" DJDYID IN (SELECT DJDYID FROM BDCS_DJDY_XZ WHERE BDCDYID=''{0}'') AND QLLX IN {1} ORDER BY DJSJ", bdcdyid, qllxarray);
			List<BDCS_QL_XZ> listxzql = dao.getDataList(BDCS_QL_XZ.class, hqlCondition);
			if (listxzql != null && listxzql.size() > 0) {
				BDCS_QL_XZ ql = listxzql.get(0);
				tree.setOldqlid(ql.getId());
			}
		}
		return zl;
	}

	/**
	 * 根据申请人ID数组添加申请人
	 */
	@Override
	public void addQLRbySQRArray(String qlid, Object[] sqrids) {
		super.addQLRbySQRs(qlid, sqrids);
		CommonDao dao = getCommonDao();
		BDCS_QL_GZ ql = dao.get(BDCS_QL_GZ.class, qlid);
		if (!ql.getQLLX().equals(QLLX.DIYQ.Value)) {
			String xmbhFilter = ProjectHelper.GetXMBHCondition(getXMBH());
			StringBuilder BuilderQLR = new StringBuilder();
			BuilderQLR.append(xmbhFilter).append(" AND QLID='").append(qlid).append("' ORDER BY SXH");
			List<BDCS_QLR_GZ> qlrs = dao.getDataList(BDCS_QLR_GZ.class, BuilderQLR.toString());
			if (qlrs != null && qlrs.size() > 0) {
				StringBuilder builderDYR = new StringBuilder();
				int indexdyr = 0;
				for (int iqlr = 0; iqlr < qlrs.size(); iqlr++) {
					if (indexdyr == 0) {
						builderDYR.append(qlrs.get(iqlr).getQLRMC());
					} else {
						builderDYR.append(",").append(qlrs.get(iqlr).getQLRMC());
					}
					indexdyr++;
				}
				StringBuilder BuilderFSQL = new StringBuilder();
				BuilderFSQL.append(xmbhFilter).append(" AND DJDYID='").append(ql.getDJDYID()).append("'");
				List<BDCS_FSQL_GZ> fsqls = dao.getDataList(BDCS_FSQL_GZ.class, BuilderFSQL.toString());
				if (fsqls != null && fsqls.size() > 0) {
					for (int ifsql = 0; ifsql < fsqls.size(); ifsql++) {
						BDCS_FSQL_GZ fsql = fsqls.get(ifsql);
						if (!fsql.getQLID().equals(qlid)) {
							fsql.setDYR(builderDYR.toString());
							dao.update(fsql);
						}
					}
				}
			}
			dao.flush();
		}
	}

	/**
	 * 移除权利人
	 */
	@Override
	public void removeQLR(String qlid, String qlrid) {
		CommonDao dao = getCommonDao();
		BDCS_QL_GZ ql = dao.get(BDCS_QL_GZ.class, qlid);
		super.removeqlr(qlrid, qlid);
		// 更新抵押人
		if (!ql.getQLLX().equals(QLLX.DIYQ.Value)) {
			List<BDCS_QLR_GZ> qlrs = dao.getDataList(BDCS_QLR_GZ.class, "QLID ='" + qlid + "' ORDER BY SXH");
			StringBuilder builderDYR = new StringBuilder();
			int indexdyr = 0;
			for (int iqlr = 0; iqlr < qlrs.size(); iqlr++) {
				if (qlrid.equals(qlrs.get(iqlr).getId())) {
					continue;
				}
				if (indexdyr == 0) {
					builderDYR.append(qlrs.get(iqlr).getQLRMC());
				} else {
					builderDYR.append(",").append(qlrs.get(iqlr).getQLRMC());
				}
				indexdyr++;
			}
			List<BDCS_FSQL_GZ> fsqlList = dao.getDataList(BDCS_FSQL_GZ.class, " XMBH='" + getXMBH() + "' and DJDYID='" + ql.getDJDYID() + "'");
			if (fsqlList != null && fsqlList.size() > 0) {
				for (int ifsql = 0; ifsql < fsqlList.size(); ifsql++) {
					BDCS_FSQL_GZ fsql = fsqlList.get(ifsql);
					if (!fsql.getQLID().equals(qlid)) {
						fsql.setDYR(builderDYR.toString());
						dao.update(fsql);
					}
				}
			}
		}
		dao.flush();
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
		ProjectInfo info  =ProjectHelper.GetPrjInfoByActinstID(actinstID);
		DJHandler handler = new YGYDYDJHandler(info);
		return handler.exportXML(path, actinstID);
		
	}

	/**
	 * 获得抵押物类型
	 * @作者 diaoliwei
	 * @创建时间 2015年7月25日下午10:18:25
	 * @param bdcdylx
	 * @return
	 */
	protected String getDYBDCLXfromBDCDYLX(BDCDYLX bdcdylx) {
		String dybdclx = "";
		if (bdcdylx.equals(BDCDYLX.SHYQZD) || bdcdylx.equals(BDCDYLX.SYQZD)) {
			dybdclx = "1";
		} else if (bdcdylx.equals(BDCDYLX.H) || bdcdylx.equals(BDCDYLX.ZRZ)) {
			dybdclx = "2";
		} else if (bdcdylx.equals(BDCDYLX.LD)) {
			dybdclx = "3";
		} else if (bdcdylx.equals(BDCDYLX.GZW) || bdcdylx.equals(BDCDYLX.YCH) || bdcdylx.equals(BDCDYLX.ZRZ)) {
			dybdclx = "4";
		} else if (bdcdylx.equals(BDCDYLX.HY)) {
			dybdclx = "5";
		} else {
			dybdclx = "7";
		}
		return dybdclx;
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
