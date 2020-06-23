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
import com.supermap.realestate.registration.dataExchange.syq.QLFQLTDSYQ;
import com.supermap.realestate.registration.dataExchange.utils.packageXml;
import com.supermap.realestate.registration.handler.DJHandler;
import com.supermap.realestate.registration.model.BDCS_C_XZ;
import com.supermap.realestate.registration.model.BDCS_DJDY_GZ;
import com.supermap.realestate.registration.model.BDCS_DJDY_XZ;
import com.supermap.realestate.registration.model.BDCS_FSQL_GZ;
import com.supermap.realestate.registration.model.BDCS_H_XZ;
import com.supermap.realestate.registration.model.BDCS_LJZ_XZ;
import com.supermap.realestate.registration.model.BDCS_PARTIALLIMIT;
import com.supermap.realestate.registration.model.BDCS_QDZR_LS;
import com.supermap.realestate.registration.model.BDCS_QDZR_XZ;
import com.supermap.realestate.registration.model.BDCS_QLR_GZ;
import com.supermap.realestate.registration.model.BDCS_QLR_LS;
import com.supermap.realestate.registration.model.BDCS_QLR_XZ;
import com.supermap.realestate.registration.model.BDCS_QL_GZ;
import com.supermap.realestate.registration.model.BDCS_QL_XZ;
import com.supermap.realestate.registration.model.BDCS_SHYQZD_XZ;
import com.supermap.realestate.registration.model.BDCS_SQR;
import com.supermap.realestate.registration.model.BDCS_SYQZD_XZ;
import com.supermap.realestate.registration.model.BDCS_XMXX;
import com.supermap.realestate.registration.model.BDCS_ZRZ_XZ;
import com.supermap.realestate.registration.model.BDCS_ZS_LS;
import com.supermap.realestate.registration.model.BDCS_ZS_XZ;
import com.supermap.realestate.registration.model.YC_SC_H_XZ;
import com.supermap.realestate.registration.model.interfaces.RealUnit;
import com.supermap.realestate.registration.model.interfaces.Rights;
import com.supermap.realestate.registration.model.interfaces.RightsHolder;
import com.supermap.realestate.registration.model.interfaces.SubRights;
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
import com.supermap.realestate.registration.util.ConstValue.SQRLB;
import com.supermap.realestate.registration.util.ConstValue.ZSBS;
import com.supermap.realestate.registration.util.Global;
import com.supermap.realestate.registration.util.ObjectHelper;
import com.supermap.realestate.registration.util.ProjectHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.realestate.registration.util.YwLogUtil;
import com.supermap.wisdombusiness.core.SuperHelper;
import com.supermap.wisdombusiness.core.SuperSpringContext;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;

/*
 1、部分转移登记
 */
/**
 * 
 * 部分转移登记处理类
 * @ClassName: ZYDJ_PartHandler
 * @author yuxuebin
 * @date 2016年08月19日 11:23:29
 */
public class ZYDJ_PartHandler extends DJHandlerBase implements DJHandler {

	/**
	 * 构造函数
	 * 
	 * @param info
	 */
	public ZYDJ_PartHandler(ProjectInfo info) {
		super(info);
	}

	/**
	 * 添加不动产单元
	 */
	@Override
	public boolean addBDCDY(String bdcdyid) {
		boolean bsuccess = false;
		CommonDao dao = getCommonDao();
		BDCS_DJDY_GZ djdy = super.createDJDYfromXZ(bdcdyid);
		if (djdy != null) {
			RealUnit unit = null;
			try {
				UnitTools.loadUnit(this.getBdcdylx(), DJDYLY.initFrom(djdy.getLY()), djdy.getBDCDYID());
			} catch (Exception e) {
				e.printStackTrace();
			}

			BDCS_QL_GZ ql = super.createQL(djdy, unit);
			// 生成附属权利
			BDCS_FSQL_GZ fsql = super.createFSQL(djdy.getDJDYID());
			fsql.setQLID(ql.getId());
			ql.setFSQLID(fsql.getId());
			// 如果是使用权宗地，把使用权面积加上
			if (getBdcdylx().equals(BDCDYLX.SHYQZD)) {
				BDCS_SHYQZD_XZ xzshyqzd = dao.get(BDCS_SHYQZD_XZ.class, bdcdyid);
				if (xzshyqzd != null) {
					fsql.setSYQMJ(xzshyqzd.getZDMJ());
					ql.setQDJG(xzshyqzd.getJG());// 取得价格
				}
			}

			// 做转移的时候加上来源权利ID，同事权利起始时间和权籍结束时间从上一手权利获取
			String qllxarray = "('1','2','3','4','5','6','7','8','9','10','11','12','13','14','15','16','17','18')";
			String hql = " DJDYID='" + djdy.getDJDYID() + "' AND QLLX IN " + qllxarray;
			String lyqlid = "";
			List<BDCS_QL_XZ> list = dao.getDataList(BDCS_QL_XZ.class, hql);
			if (list != null && list.size() > 0) {
				lyqlid = list.get(0).getId();
				ql.setLYQLID(lyqlid);
				ql.setQLQSSJ(list.get(0).getQLQSSJ());
				ql.setQLJSSJ(list.get(0).getQLJSSJ());
			}
			ql.setISPARTIAL("1");
			ql.setCZFS(CZFS.FBCZ.Value);
			ql.setZSBS(ZSBS.DYB.Value);
			// 保存权利和附属权利
			dao.save(ql);
			dao.save(fsql);
			dao.save(djdy);
			// 拷贝转移前权利人到申请人
			CopySQRFromXZQLR(djdy.getDJDYID(), ql.getQLLX(), this.getXMBH(), ql.getId(),SQRLB.YF.Value,djdy.getBDCDYID());
			
//			//拷贝商品上预告权利人到申请人中的权利人
//			CopyYCYGQLRToSQR(ql.getId());
			//拷贝转移预告权利人到申请人中的权利人
			CopyZYYGQLRToSQR(ql.getId(),djdy.getDJDYID());
		}
		dao.flush();
		bsuccess = true;
		return bsuccess;
	}
	
	/**
	 * 根据登记单元ID和权利类型拷贝权利人到申请人
	 * @Author：俞学斌
	 * @param djdyid
	 *            登记单元ID
	 * @param qllx
	 *            权利类型
	 * @param xmbh
	 *            项目编号
	 * @param qlid
	 *            关联权利ID
	 */
	protected void CopySQRFromXZQLR(String djdyid, String qllx, String xmbh, String qlid, String sqrlb,String bdcdyid) {
		CommonDao dao = getCommonDao();
		StringBuilder builderQL = new StringBuilder();
		builderQL.append("DJDYID ='");
		builderQL.append(djdyid);
		builderQL.append("' AND QLLX='");
		builderQL.append(qllx);
		builderQL.append("'");
		List<BDCS_QL_XZ> qls = dao.getDataList(BDCS_QL_XZ.class, builderQL.toString());
		if (qls == null || qls.size() <= 0) {
			return;
		}
		BDCS_QL_XZ ql = qls.get(0);

		StringBuilder builderQLR = new StringBuilder();
		builderQLR.append("QLID ='");
		builderQLR.append(ql.getId());
		builderQLR.append("'");
		List<BDCS_QLR_XZ> qlrs = dao.getDataList(BDCS_QLR_XZ.class, builderQLR.toString());
		if (qls == null || qls.size() <= 0) {
			return;
		}
		for (int iqlr = 0; iqlr < qlrs.size(); iqlr++) {
			
			BDCS_QLR_XZ qlr = qlrs.get(iqlr);
			
			List<BDCS_PARTIALLIMIT> list_limit=dao.getDataList(BDCS_PARTIALLIMIT.class, "QLRID='"+qlr.getId()+"' AND LIMITTYPE IN ('23','800') AND YXBZ IN ('0','1')");
			if(list_limit!=null&&list_limit.size()>0){
				continue;
			}
			BDCS_PARTIALLIMIT partialseizures=new BDCS_PARTIALLIMIT();
			partialseizures.setBDCDYID(bdcdyid);
			partialseizures.setLIMITQLID(qlid);
			partialseizures.setQLID(qlr.getQLID());
			partialseizures.setQLRID(qlr.getId());
			partialseizures.setQLRMC(qlr.getQLRMC());
			partialseizures.setXMBH(super.getXMBH());
			partialseizures.setYXBZ("0");
			partialseizures.setLIMITTYPE("200");
			partialseizures.setZJH(qlr.getZJH());
			dao.save(partialseizures);
			
			String SQRID = getPrimaryKey();
			List<BDCS_SQR> sqrs = dao.getDataList(BDCS_SQR.class, "XMBH='" + this.getXMBH() + "' AND SQRLB='" + sqrlb + "' AND SQRXM='" + qlr.getQLRMC() + "'");
			if (sqrs != null && sqrs.size() > 0) {
				continue;
			}
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
			sqr.setSQRLB(sqrlb);
			sqr.setSQRLX(qlr.getQLRLX());
			sqr.setDZYJ(qlr.getDZYJ());
			sqr.setLXDH(qlr.getDH());
			sqr.setZJH(qlr.getZJH());
			sqr.setZJLX(qlr.getZJZL());
			sqr.setTXDZ(qlr.getDZ());
			sqr.setYZBM(qlr.getYB());
			sqr.setXMBH(xmbh);
			sqr.setId(SQRID);
			sqr.setGLQLID(qlid);
			dao.save(sqr);
		}
	}
	

	/**
	 * 写入登记簿
	 */
	@Override
	public boolean writeDJB() {
		if(!YCYGCanecl()){
			return false;
		}
		if(!ZYYGCanecl()){
			return false;
		}
		String strSqlXMBH = ProjectHelper.GetXMBHCondition(getXMBH());
		List<BDCS_DJDY_GZ> djdys = getCommonDao().getDataList(BDCS_DJDY_GZ.class, strSqlXMBH);
		if (djdys != null && djdys.size() > 0) {
			for (int idjdy = 0; idjdy < djdys.size(); idjdy++) {
				BDCS_DJDY_GZ bdcs_djdy_gz = djdys.get(idjdy);
				if (bdcs_djdy_gz != null) {
					String djdyid = bdcs_djdy_gz.getDJDYID();
					super.removeQLFromXZByQLLX(djdyid);
					super.removeQLRFromXZByQLLX(djdyid);
					super.removeQDZRFromXZByQLLX(djdyid);
					super.removeZSFromXZByQLLX(djdyid);

					super.CopyGZQLToXZAndLS(djdyid);
					super.CopyGZQLRToXZAndLS(djdyid);
					super.CopyGZQDZRToXZAndLS(djdyid);
					super.CopyGZZSToXZAndLS(djdyid);
					addqlr(djdyid);
				}
			}
		}
		this.SetSFDB();
		getCommonDao().flush();
		super.alterCachedXMXX();
		return true;
	}
	
	public void addqlr(String djdyid){
		Rights ql=RightsTools.loadRightsByDJDYID(DJDYLY.GZ, super.getXMBH(), djdyid);
		if(ql==null){
			return;
		}
		String lyqlid=ql.getLYQLID();
		if(StringHelper.isEmpty(lyqlid)){
			return;
		}
		List<String> list_zyqlrid=new ArrayList<String>();
		List<BDCS_PARTIALLIMIT> list_limit=getCommonDao().getDataList(BDCS_PARTIALLIMIT.class, "LIMITQLID='"+ql.getId()+"'");
		if(list_limit!=null&&list_limit.size()>0){
			for(BDCS_PARTIALLIMIT partialseizures:list_limit){
				list_zyqlrid.add(partialseizures.getQLRID());
			}
		}
		List<String> addqzh=new ArrayList<String>();
		List<BDCS_QLR_LS> list_qlr_zyq=getCommonDao().getDataList(BDCS_QLR_LS.class, "QLID='"+lyqlid+"'");
		if(list_qlr_zyq!=null&&list_qlr_zyq.size()>0){
			for(RightsHolder qlr_zyq:list_qlr_zyq){
				if(!list_zyqlrid.contains(qlr_zyq.getId())){
					if(!StringHelper.isEmpty(qlr_zyq.getBDCQZH())&&!addqzh.contains(qlr_zyq.getBDCQZH())){
						addqzh.add(qlr_zyq.getBDCQZH());
					}
					String qlrid=SuperHelper.GeneratePrimaryKey();
					String zsid=SuperHelper.GeneratePrimaryKey();
					String qdzrid=SuperHelper.GeneratePrimaryKey();
					BDCS_QLR_XZ qlr_zyh_xz=new BDCS_QLR_XZ();
					ObjectHelper.copyObject(qlr_zyq, qlr_zyh_xz);
					qlr_zyh_xz.setId(qlrid);
					qlr_zyh_xz.setQLID(ql.getId());
					getCommonDao().save(qlr_zyh_xz);
					
					BDCS_QLR_LS qlr_zyh_ls=new BDCS_QLR_LS();
					ObjectHelper.copyObject(qlr_zyq, qlr_zyh_ls);
					qlr_zyh_ls.setId(qlrid);
					qlr_zyh_ls.setQLID(ql.getId());
					getCommonDao().save(qlr_zyh_ls);
					List<BDCS_QDZR_LS> list_qdzr=getCommonDao().getDataList(BDCS_QDZR_LS.class, "QLRID='"+qlr_zyq.getId()+"'"); 
					if(list_qdzr==null||list_qdzr.size()<=0){
						continue;
					}
					BDCS_QDZR_LS qdzr_zyq=list_qdzr.get(0);
					
					BDCS_QDZR_XZ qdzr_zyh_xz=new BDCS_QDZR_XZ();
					ObjectHelper.copyObject(qdzr_zyq, qdzr_zyh_xz);
					qdzr_zyh_xz.setBDCDYH(ql.getBDCDYH());
					qdzr_zyh_xz.setDJDYID(ql.getDJDYID());
					qdzr_zyh_xz.setFSQLID(ql.getFSQLID());
					qdzr_zyh_xz.setQLID(ql.getId());
					qdzr_zyh_xz.setQLRID(qlrid);
					qdzr_zyh_xz.setZSID(zsid);
					qdzr_zyh_xz.setId(qdzrid);
					getCommonDao().save(qdzr_zyh_xz);
					
					BDCS_QDZR_LS qdzr_zyh_ls=new BDCS_QDZR_LS();
					ObjectHelper.copyObject(qdzr_zyq, qdzr_zyh_ls);
					qdzr_zyh_ls.setBDCDYH(ql.getBDCDYH());
					qdzr_zyh_ls.setDJDYID(ql.getDJDYID());
					qdzr_zyh_ls.setFSQLID(ql.getFSQLID());
					qdzr_zyh_ls.setQLID(ql.getId());
					qdzr_zyh_ls.setQLRID(qlrid);
					qdzr_zyh_ls.setZSID(zsid);
					qdzr_zyh_ls.setId(qdzrid);
					getCommonDao().save(qdzr_zyh_ls);
					
					BDCS_ZS_LS zs_zyq=getCommonDao().get(BDCS_ZS_LS.class, qdzr_zyq.getZSID());
					if(zs_zyq==null){
						continue;
					}
					BDCS_ZS_XZ zs_zyh_xz=new BDCS_ZS_XZ();
					ObjectHelper.copyObject(zs_zyq, zs_zyh_xz);
					zs_zyh_xz.setId(zsid);
					getCommonDao().save(zs_zyh_xz);
					
					BDCS_ZS_LS zs_zyh_ls=new BDCS_ZS_LS();
					ObjectHelper.copyObject(zs_zyq, zs_zyh_ls);
					zs_zyh_ls.setId(zsid);
					getCommonDao().save(zs_zyh_ls);
					
					
					List<BDCS_PARTIALLIMIT> list_limit_qlr=getCommonDao().getDataList(BDCS_PARTIALLIMIT.class, "QLRID='"+qlr_zyq.getId()+"' AND LIMITTYPE IN ('23','800')");
					if(list_limit_qlr!=null&&list_limit_qlr.size()>0){
						for(BDCS_PARTIALLIMIT partialseizures:list_limit_qlr){
							if("2".equals(partialseizures.getYXBZ())){
								continue;
							}
							String limitid=SuperHelper.GeneratePrimaryKey();
							BDCS_PARTIALLIMIT partialseizures_new=new BDCS_PARTIALLIMIT();
							ObjectHelper.copyObject(partialseizures, partialseizures_new);
							partialseizures_new.setId(limitid);
							partialseizures_new.setQLRID(qlrid);
							getCommonDao().save(partialseizures_new);
							partialseizures.setYXBZ("2");
							getCommonDao().update(partialseizures);
						}
					}
				}
			}
		}
		getCommonDao().flush();
		if(addqzh.size()>0){
			String strqzh=StringHelper.formatList(addqzh, ",");
			Rights ql_xz=RightsTools.loadRights(DJDYLY.XZ, ql.getId());
			String qzh_1=ql_xz.getBDCQZH();
			String qzh=strqzh+","+qzh_1;
			if(ql_xz!=null){
				ql_xz.setBDCQZH(qzh);
				getCommonDao().update(ql_xz);
			}
			Rights ql_ls=RightsTools.loadRights(DJDYLY.XZ, ql.getId());
			if(ql_ls!=null){
				ql_ls.setBDCQZH(qzh);
				getCommonDao().update(ql_ls);
			}
		}
		getCommonDao().flush();
	}
	
	protected boolean CopyYCYGQLRToSQR(String qlid){
		if(!BDCDYLX.H.equals(super.getBdcdylx())){
			return true;
		}
		String strSqlXMBH = ProjectHelper.GetXMBHCondition(getXMBH());
		List<BDCS_DJDY_GZ> djdys = getCommonDao().getDataList(BDCS_DJDY_GZ.class, strSqlXMBH);
		if(djdys!=null&&djdys.size()>0){
			for(BDCS_DJDY_GZ djdy:djdys){
				String SCBDCDYID=djdy.getBDCDYID();
				List<YC_SC_H_XZ> listGX=getCommonDao().getDataList(YC_SC_H_XZ.class, "SCBDCDYID='"+SCBDCDYID+"'");
				if(listGX!=null&&listGX.size()>0){
					for(YC_SC_H_XZ gx:listGX){
						String YCBDCDYID=gx.getYCBDCDYID();
						if(StringHelper.isEmpty(YCBDCDYID)){
							continue;
						}
						List<BDCS_DJDY_XZ> djdys_yc=getCommonDao().getDataList(BDCS_DJDY_XZ.class, "BDCDYID='"+YCBDCDYID+"'");
						if(djdys_yc==null||djdys_yc.size()<=0){
							continue;
						}
						String ycdjdyid=djdys_yc.get(0).getDJDYID();
						List<Rights> ycyg_qls=RightsTools.loadRightsByCondition(DJDYLY.XZ, "DJDYID='"+ycdjdyid+"' AND DJLX='700' AND QLLX='4'");
						if(ycyg_qls!=null&&ycyg_qls.size()>0){
							for(Rights ycyg_ql:ycyg_qls){
								List<RightsHolder> zyyg_qlrs=RightsHolderTools.loadRightsHolders(DJDYLY.XZ, ycyg_ql.getId());
								if(zyyg_qlrs!=null&&zyyg_qlrs.size()>0){
									for(RightsHolder zyyg_qlr:zyyg_qlrs){
										BDCS_QLR_XZ qlr=(BDCS_QLR_XZ)zyyg_qlr;
										BDCS_SQR sqr=super.copyXZQLRtoSQR(qlr, SQRLB.JF);
										if(sqr!=null){
											sqr.setGLQLID(qlid);
											getCommonDao().save(sqr);
										}
									}
								}
							}
						}
					}
					
				}
			}
		}
		return true;
	}
	
	protected boolean CopyZYYGQLRToSQR(String qlid,String djdyid){
		List<Rights> zyyg_qls=RightsTools.loadRightsByCondition(DJDYLY.XZ, "DJDYID='"+djdyid+"' AND DJLX='700' AND QLLX='99'");
		if(zyyg_qls!=null&&zyyg_qls.size()>0){
			for(Rights zyyg_ql:zyyg_qls){
				List<RightsHolder> zyyg_qlrs=RightsHolderTools.loadRightsHolders(DJDYLY.XZ, zyyg_ql.getId());
				if(zyyg_qlrs!=null&&zyyg_qlrs.size()>0){
					for(RightsHolder zyyg_qlr:zyyg_qlrs){
						BDCS_QLR_XZ qlr=(BDCS_QLR_XZ)zyyg_qlr;
						BDCS_SQR sqr=super.copyXZQLRtoSQR(qlr, SQRLB.JF);
						if(sqr!=null){
							sqr.setGLQLID(qlid);
							getCommonDao().save(sqr);
						}
					}
				}
			}
		}
		return true;
	}
	
	
	private boolean YCYGCanecl(){
		boolean bCancel=true;
		if(!BDCDYLX.H.equals(super.getBdcdylx())){
			return true;
		}
		String dbr = Global.getCurrentUserName();
		String strSqlXMBH = ProjectHelper.GetXMBHCondition(getXMBH());
		List<BDCS_DJDY_GZ> djdys = getCommonDao().getDataList(BDCS_DJDY_GZ.class, strSqlXMBH);
		if(djdys!=null&&djdys.size()>0){
			//直接注销预测户上的预告登记，不加限制
//			for(BDCS_DJDY_GZ djdy:djdys){
//				List<String> list_qlrmc_zjh=new ArrayList<String>();
//				List<RightsHolder> qlrs=RightsHolderTools.loadRightsHolders(DJDYLY.GZ, djdy.getDJDYID(), super.getXMBH());
//				if(qlrs!=null&&qlrs.size()>0){
//					for(RightsHolder qlr:qlrs){
//						list_qlrmc_zjh.add(qlr.getQLRMC()+"&"+StringHelper.formatObject(qlr.getZJH()));
//					}
//				}
//				String SCBDCDYID=djdy.getBDCDYID();
//				List<YC_SC_H_XZ> listGX=getCommonDao().getDataList(YC_SC_H_XZ.class, "SCBDCDYID='"+SCBDCDYID+"'");
//				if(listGX!=null&&listGX.size()>0){
//					for(YC_SC_H_XZ gx:listGX){
//						String YCBDCDYID=gx.getYCBDCDYID();
//						if(StringHelper.isEmpty(YCBDCDYID)){
//							continue;
//						}
//						List<BDCS_DJDY_XZ> djdys_yc=getCommonDao().getDataList(BDCS_DJDY_XZ.class, "BDCDYID='"+YCBDCDYID+"'");
//						if(djdys_yc==null||djdys_yc.size()<=0){
//							continue;
//						}
//						String ycdjdyid=djdys_yc.get(0).getDJDYID();
//						List<Rights> ycyg_qls=RightsTools.loadRightsByCondition(DJDYLY.XZ, "DJDYID='"+ycdjdyid+"' AND DJLX='700' AND QLLX='4'");
//						if(ycyg_qls!=null&&ycyg_qls.size()>0){
//							for(Rights ycyg_ql:ycyg_qls){
//								List<RightsHolder> zyyg_qlrs=RightsHolderTools.loadRightsHolders(DJDYLY.XZ, ycyg_ql.getId());
//								if(zyyg_qlrs!=null&&zyyg_qlrs.size()>0){
//									for(RightsHolder zyyg_qlr:zyyg_qlrs){
//										String str_qlrmc_zjh=zyyg_qlr.getQLRMC()+"&"+StringHelper.formatObject(zyyg_qlr.getZJH());
//										if(!list_qlrmc_zjh.contains(str_qlrmc_zjh)){
//											super.setErrMessage("单元已办理商品房预告登记，且商品房预告权利人不在转移后权利人中");
//											return false;
//										}
//									}
//								}
//							}
//						}
//					}
//					
//				}
//			}
			for(BDCS_DJDY_GZ djdy:djdys){
				String SCBDCDYID=djdy.getBDCDYID();
				List<YC_SC_H_XZ> listGX=getCommonDao().getDataList(YC_SC_H_XZ.class, "SCBDCDYID='"+SCBDCDYID+"'");
				if(listGX!=null&&listGX.size()>0){
					for(YC_SC_H_XZ gx:listGX){
						String YCBDCDYID=gx.getYCBDCDYID();
						if(StringHelper.isEmpty(YCBDCDYID)){
							continue;
						}
						List<BDCS_DJDY_XZ> djdys_yc=getCommonDao().getDataList(BDCS_DJDY_XZ.class, "BDCDYID='"+YCBDCDYID+"'");
						if(djdys_yc==null||djdys_yc.size()<=0){
							continue;
						}
						String ycdjdyid=djdys_yc.get(0).getDJDYID();
						List<Rights> ycyg_qls=RightsTools.loadRightsByCondition(DJDYLY.XZ, "DJDYID='"+ycdjdyid+"' AND DJLX='700' AND QLLX='4'");
						if(ycyg_qls!=null&&ycyg_qls.size()>0){
							for(Rights ycyg_ql:ycyg_qls){
								String qlid=ycyg_ql.getId();
								RightsTools.deleteRightsAll(DJDYLY.XZ, qlid);
								// 更新历史附属权利信息
								SubRights subright = RightsTools.loadSubRightsByRightsID(DJDYLY.LS, qlid);
								if (subright != null) {
									subright.setZXSJ(new Date());
									subright.setZXDBR(dbr);
									subright.setZXDYYWH(super.getProject_id());
									getCommonDao().update(subright);
								}
							}
						}
					}
					
				}
			}
			getCommonDao().flush();
		}
		return bCancel;
	}
	
	private boolean ZYYGCanecl(){
		boolean bCancel=true;
		String dbr = Global.getCurrentUserName();
		String strSqlXMBH = ProjectHelper.GetXMBHCondition(getXMBH());
		List<BDCS_DJDY_GZ> djdys = getCommonDao().getDataList(BDCS_DJDY_GZ.class, strSqlXMBH);
		if(djdys!=null&&djdys.size()>0){
			for(BDCS_DJDY_GZ djdy:djdys){
				List<String> list_qlrmc_zjh=new ArrayList<String>();
				List<RightsHolder> qlrs=RightsHolderTools.loadRightsHolders(DJDYLY.GZ, djdy.getDJDYID(), super.getXMBH());
				if(qlrs!=null&&qlrs.size()>0){
					for(RightsHolder qlr:qlrs){
						list_qlrmc_zjh.add(qlr.getQLRMC()+"&"+StringHelper.formatObject(qlr.getZJH()));
					}
				}
				List<Rights> zyyg_qls=RightsTools.loadRightsByCondition(DJDYLY.XZ, "DJDYID='"+djdy.getDJDYID()+"' AND DJLX='700' AND QLLX='99'");
				if(zyyg_qls!=null&&zyyg_qls.size()>0){
					for(Rights zyyg_ql:zyyg_qls){
						List<RightsHolder> zyyg_qlrs=RightsHolderTools.loadRightsHolders(DJDYLY.XZ, zyyg_ql.getId());
						if(zyyg_qlrs!=null&&zyyg_qlrs.size()>0){
							for(RightsHolder zyyg_qlr:zyyg_qlrs){
								String str_qlrmc_zjh=zyyg_qlr.getQLRMC()+"&"+StringHelper.formatObject(zyyg_qlr.getZJH());
								if(!list_qlrmc_zjh.contains(str_qlrmc_zjh)){
									super.setErrMessage("单元已办理转移预告登记，且转移预告权利人不在转移后权利人中");
									return false;
								}
							}
						}
					}
				}
				
			}
			for(BDCS_DJDY_GZ djdy:djdys){
				List<Rights> zyyg_qls=RightsTools.loadRightsByCondition(DJDYLY.XZ, "DJDYID='"+djdy.getDJDYID()+"' AND DJLX='700' AND QLLX='99'");
				if(zyyg_qls!=null&&zyyg_qls.size()>0){
					for(Rights zyyg_ql:zyyg_qls){
						String qlid=zyyg_ql.getId();
						RightsTools.deleteRightsAll(DJDYLY.XZ, qlid);
						// 更新历史附属权利信息
						SubRights subright = RightsTools.loadSubRightsByRightsID(DJDYLY.LS, qlid);
						if (subright != null) {
							subright.setZXSJ(new Date());
							subright.setZXDBR(dbr);
							subright.setZXDYYWH(super.getProject_id());
							getCommonDao().update(subright);
						}
					}
				}
			}
			getCommonDao().flush();
		}
		return bCancel;
	}

	/**
	 * 移除不动产单元
	 */
	@Override
	public boolean removeBDCDY(String bdcdyid) {
		boolean bsuccess = false;
		// 找到登记单元表，移除记录
		CommonDao baseCommonDao = this.getCommonDao();
		BDCS_DJDY_GZ djdy = super.removeDJDY(bdcdyid);// list.get(0);
		if (djdy != null) {
			String djdyid = djdy.getDJDYID();
			// 删除权利关联申请人
			super.RemoveSQRByQLID(djdyid, getXMBH());
			
			//移除部分限制
			String _hqlCondition=MessageFormat.format("XMBH=''{0}'' AND DJDYID=''{1}''", getXMBH(),djdyid);
			List<Rights> _rights = RightsTools.loadRightsByCondition(DJDYLY.GZ, _hqlCondition);
			if(_rights!=null&&_rights.size()>0){
				for(Rights right:_rights){
					List<BDCS_PARTIALLIMIT> list=baseCommonDao.getDataList(BDCS_PARTIALLIMIT.class, "LIMITQLID='"+right.getId()+"'");
					if(list!=null&&list.size()>0){
						for(BDCS_PARTIALLIMIT partialseizures:list){
							baseCommonDao.deleteEntity(partialseizures);
						}
					}
				}
			}

			// 删除权利、附属权利、权利人、证书、权地证人关系
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
		return super.getUnitList();
	}

	/**
	 * 根据申请人ID添加权利人
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
	public Map<String, String> exportXML(String path, String actinstID) {
		Marshaller mashaller;
		Map<String, String> names = new HashMap<String, String>();
		try {
			CommonDao dao = super.getCommonDao();
			String xmbhHql = ProjectHelper.GetXMBHCondition(super.getXMBH());
			List<BDCS_DJDY_GZ> djdys = dao.getDataList(BDCS_DJDY_GZ.class, xmbhHql);
			if (djdys != null && djdys.size() > 0) {
				mashaller = JAXBContext.newInstance(Message.class).createMarshaller();
				//mashaller.setProperty(Marshaller.JAXB_ENCODING, "gbk");
				Calendar calendar = Calendar.getInstance(); // 创建一个日历对象
				calendar.setTime(new Date()); // 用当前时间初始化日历时间
				String cyear = calendar.get(Calendar.YEAR) + "";
				String ywh = cyear + ConfigHelper.getNameByValue("XZQHDM") + super.getDjlx().Value + super.getQllx().Value + super.getPrjNumber().substring(1, 6);
				BDCS_XMXX xmxx = dao.get(BDCS_XMXX.class, super.getXMBH());
				String result = "";
				String msgName = "";
				for (int i = 0; i < djdys.size(); i++) {
					BDCS_DJDY_GZ djdy = djdys.get(i);
					BDCS_QL_GZ ql = super.getQL(djdy.getDJDYID());
					List<BDCS_QLR_GZ> qlrs = super.getQLRs(ql.getId());
					List<BDCS_SQR> sqrs = new ArrayList<BDCS_SQR>();
					ProjectServiceImpl serviceImpl = SuperSpringContext.getContext().getBean(ProjectServiceImpl.class);
					sqrs = serviceImpl.getSQRList(super.getXMBH());

					if (QLLX.GYJSYDSHYQ.Value.equals(this.getQllx().Value) || QLLX.ZJDSYQ.Value.equals(this.getQllx().Value) || QLLX.JTJSYDSYQ.Value.equals(this.getQllx().Value)) { // 国有建设使用权、宅基地、集体建设用地使用权
						try {
							BDCS_SHYQZD_XZ zd = dao.get(BDCS_SHYQZD_XZ.class, djdy.getBDCDYID());
							Message msg = exchangeFactory.createMessageBySHYQ();
							super.fillHead(msg, i, ywh,zd.getBDCDYH(),zd.getQXDM(),ql.getLYQLID());

							msg.getHead().setParcelID(StringHelper.formatObject(zd.getZDDM()));
							msg.getHead().setEstateNum(StringHelper.formatObject(zd.getBDCDYH()));
//							msg.getHead().setPreEstateNum(StringHelper.formatObject(zd.getBDCDYH()));
							if(zd != null && !StringUtils.isEmpty(zd.getQXDM())){
								msg.getHead().setAreaCode(zd.getQXDM());
							}

							if (djdy != null) {
								List<ZTTGYQLR> zttqlr = msg.getData().getGYQLR();
								zttqlr = packageXml.getZTTGYQLRs(zttqlr, qlrs, zd, ql, null, null, null, null);
								msg.getData().setGYQLR(zttqlr);

								KTTZDJBXX jbxx = msg.getData().getKTTZDJBXX();
								jbxx = packageXml.getZDJBXX(jbxx, zd, ql, null, null);

								KTFZDBHQK bhqk = msg.getData().getZDBHQK();
								bhqk = packageXml.getKTFZDBHQK(bhqk, zd, ql, null, null);
								msg.getData().setZDBHQK(bhqk);

								QLFQLJSYDSYQ syq = msg.getData().getQLJSYDSYQ();
								syq = packageXml.getQLFQLJSYDSYQ(syq, zd, ql, ywh);

								DJTDJSLSQ sq = msg.getData().getDJSLSQ();
								sq = packageXml.getDJTDJSLSQ(sq, ywh, zd, ql, xmxx, null, super.getCreateTime(), null, null, null);

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
							msgName = "Biz" + msg.getHead().getBizMsgID() + ".xml";
							mashaller.marshal(msg, new File(path + msgName));
							result = uploadFile(path + msgName,ConstValue.RECCODE.JSYDSHYQ_ZYDJ.Value,actinstID,djdy.getDJDYID(),ql.getId());
							names.put(djdy.getDJDYID(), msg.getHead().getBizMsgID() + ".xml");
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					if (QLLX.GYJSYDSHYQ_FWSYQ.Value.equals(this.getQllx().Value)) { // 房屋所有权
						try {
							BDCS_H_XZ h = dao.get(BDCS_H_XZ.class, djdy.getBDCDYID());
							BDCS_ZRZ_XZ zrz_ = null;
							BDCS_LJZ_XZ ljz_ = null;
							BDCS_SHYQZD_XZ zd = null;
							if (h != null && !StringUtils.isEmpty(h.getZRZBDCDYID())) {
								zrz_ = dao.get(BDCS_ZRZ_XZ.class, h.getZRZBDCDYID());
								if(zrz_ != null){
									zrz_.setGHYT(h.getGHYT()); //自然幢的ghyt取户的ghyt
								}
							}
							if (h != null && !StringUtils.isEmpty(h.getLJZID())) {
								ljz_ = dao.get(BDCS_LJZ_XZ.class, h.getLJZID());
							}
							BDCS_C_XZ c_ = null;
							if (h != null && !StringUtils.isEmpty(h.getCID())) {
								c_ = dao.get(BDCS_C_XZ.class, h.getCID());
							}
							if(h != null && !StringUtils.isEmpty(h.getZDBDCDYID())){
								zd = dao.get(BDCS_SHYQZD_XZ.class, h.getZDBDCDYID());
								if(zd != null){
									zrz_.setZDDM(zd.getZDDM());
								}
							}
							Message msg = exchangeFactory.createMessageByFWSYQ2();
							super.fillHead(msg, i, ywh,h.getBDCDYH(),h.getQXDM(),ql.getLYQLID());
							msg.getHead().setParcelID(zrz_.getZDDM());
							msg.getHead().setEstateNum(h.getBDCDYH());
//							msg.getHead().setPreEstateNum(h.getBDCDYH());
							if(zd != null && !StringUtils.isEmpty(zd.getQXDM())){
								msg.getHead().setAreaCode(zd.getQXDM());
							}
							if (djdy != null) {

								List<ZTTGYQLR> zttqlr = msg.getData().getGYQLR();
								zttqlr = packageXml.getZTTGYQLRs(zttqlr, qlrs, null, ql, h, null, null, null);
								msg.getData().setGYQLR(zttqlr);

								KTTFWZRZ zrz = msg.getData().getKTTFWZRZ();
								zrz = packageXml.getKTTFWZRZ(zrz, zrz_);

								KTTFWLJZ ljz = msg.getData().getKTTFWLJZ();
								ljz = packageXml.getKTTFWLJZ(ljz, ljz_,h);

								KTTFWC kttc = msg.getData().getKTTFWC();
								kttc = packageXml.getKTTFWC(kttc, c_, zrz);
								msg.getData().setKTTFWC(kttc);

								KTTFWH fwh = msg.getData().getKTTFWH();
								fwh = packageXml.getKTTFWH(fwh, h);

								QLTFWFDCQYZ fdcqyz = msg.getData().getQLTFWFDCQYZ();
								fdcqyz = packageXml.getQLTFWFDCQYZ(fdcqyz, h, ql, ywh);

								DJTDJSLSQ sq = msg.getData().getDJSLSQ();
								sq = packageXml.getDJTDJSLSQ(sq, ywh, null, ql, xmxx, h, super.getCreateTime(), null, null, null);

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
							msgName = "Biz" + msg.getHead().getBizMsgID() + ".xml";
							mashaller.marshal(msg, new File(path + msgName));
							result = uploadFile(path + msgName,ConstValue.RECCODE.FDCQDZ_ZYDJ.Value,actinstID,djdy.getDJDYID(),ql.getId());
							names.put(djdy.getDJDYID(), msg.getHead().getBizMsgID() + ".xml");
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					
					if (QLLX.JTTDSYQ.Value.equals(this.getQllx().Value)) { // 集体土地所有权
						try {
							BDCS_SYQZD_XZ oland = dao.get(BDCS_SYQZD_XZ.class, djdy.getBDCDYID());
							Message msg = exchangeFactory.createMessageByTDSYQ();
							super.fillHead(msg, i, ywh,oland.getBDCDYH(),oland.getQXDM(),ql.getLYQLID());
							msg.getHead().setParcelID(StringHelper.formatObject(oland.getZDDM()));
							msg.getHead().setEstateNum(StringHelper.formatObject(oland.getBDCDYH()));
//							msg.getHead().setPreEstateNum(StringHelper.formatObject(oland.getBDCDYH()));
							if(oland != null && !StringUtils.isEmpty(oland.getQXDM())){
								msg.getHead().setAreaCode(oland.getQXDM());
							}
							if (djdy != null) {

								KTTZDJBXX jbxx = msg.getData().getKTTZDJBXX();
								jbxx = packageXml.getZDJBXX(jbxx, null, ql, oland, null);

								String zdzhdm = "";
								if (oland != null) {
									zdzhdm = oland.getZDDM();
								}
								KTTGYJZX jzx = msg.getData().getKTTGYJZX();
								jzx = packageXml.getKTTGYJZX(jzx, zdzhdm);

								KTTGYJZD jzd = msg.getData().getKTTGYJZD();
								jzd = packageXml.getKTTGYJZD(jzd, zdzhdm);

								FJF100 fj = msg.getData().getFJF100();
								fj = packageXml.getFJF(fj);

								KTFZDBHQK bhqk = msg.getData().getZDBHQK();
								bhqk = packageXml.getKTFZDBHQK(bhqk, null, ql, oland, null);
								msg.getData().setZDBHQK(bhqk);

								QLFQLTDSYQ tdsyq = msg.getData().getQLFQLTDSYQ();
								tdsyq = packageXml.getQLFQLTDSYQ(tdsyq, oland, ql, ywh);

								List<ZTTGYQLR> zttqlr = msg.getData().getGYQLR();
								zttqlr = packageXml.getZTTGYQLRs(zttqlr, qlrs, null, ql, null, oland, null, null);
								msg.getData().setGYQLR(zttqlr);

								DJTDJSLSQ sq = msg.getData().getDJSLSQ();
								sq = packageXml.getDJTDJSLSQ(sq, ywh, null, ql, xmxx, null, this.getCreateTime(), oland, null, null);

								List<DJFDJSJ> sj = msg.getData().getDJSJ();
								sj = packageXml.getDJFDJSJ(oland, ywh,actinstID);
								msg.getData().setDJSJ(sj);


								  //9.登记收费(可选)
									List<DJFDJSF> sfList = msg.getData().getDJSF();
									sfList = packageXml.getDJSF(oland, ywh,this.getXMBH());
									msg.getData().setDJSF(sfList);

									//10.审核(可选)
									List<DJFDJSH> sh = msg.getData().getDJSH();
									 sh = packageXml.getDJFDJSH(oland, ywh, this.getXMBH(), actinstID);
									msg.getData().setDJSH(sh);

									//11.缮证(可选)
									List<DJFDJSZ>  sz = packageXml.getDJFDJSZ(oland, ywh, this.getXMBH());
									msg.getData().setDJSZ(sz);

									//11.发证(可选)
									List<DJFDJFZ>   fz = packageXml.getDJFDJFZ(oland, ywh, this.getXMBH());
									msg.getData().setDJFZ(fz);
									
									//12.归档(可选)
									List<DJFDJGD> gd = packageXml.getDJFDJGD(oland, ywh, this.getXMBH());
									msg.getData().setDJGD(gd);	
									
								List<ZDK103> zdk = msg.getData().getZDK103();
								zdk = packageXml.getZDK103(zdk, null, oland, null);

								List<DJFDJSQR> djsqrs = msg.getData().getDJSQR();
								djsqrs = packageXml.getDJSQRs(djsqrs, sqrs, oland.getYSDM(), ywh, oland.getBDCDYH());
								msg.getData().setDJSQR(djsqrs);
							}
							msgName = "Biz" + msg.getHead().getBizMsgID() + ".xml";
							names.put(djdy.getDJDYID(), msg.getHead().getBizMsgID() + ".xml");
							mashaller.marshal(msg, new File(path + msgName));
							result = uploadFile(path + msgName, ConstValue.RECCODE.TDSYQ_CSDJ.Value,actinstID,djdy.getDJDYID(),ql.getId());
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					
					if(null == result){
						Map<String, String> xmlError = new HashMap<String, String>();
						xmlError.put("error", "连接SFTP失败,请检查服务器和前置机的连接是否正常");
						if(QLLX.JTTDSYQ.Value.equals(this.getQllx().Value)){
							YwLogUtil.addSjsbResult(msgName, "", "连接SFTP失败,请检查服务器和前置机的连接是否正常", ConstValue.SF.NO.Value, ConstValue.RECCODE.TDSYQ_ZYDJ.Value,ProjectHelper.getpRroinstIDByActinstID(actinstID));	
						}else if (QLLX.GYJSYDSHYQ_FWSYQ.Value.equals(this.getQllx().Value)){
							YwLogUtil.addSjsbResult(msgName, "", "连接SFTP失败,请检查服务器和前置机的连接是否正常", ConstValue.SF.NO.Value, ConstValue.RECCODE.FDCQDZ_ZYDJ.Value,ProjectHelper.getpRroinstIDByActinstID(actinstID));
						}else {
							YwLogUtil.addSjsbResult(msgName, "", "连接SFTP失败,请检查服务器和前置机的连接是否正常", ConstValue.SF.NO.Value, ConstValue.RECCODE.JSYDSHYQ_ZYDJ.Value,ProjectHelper.getpRroinstIDByActinstID(actinstID));
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
				Rights bdcql = RightsTools.loadRightsByDJDYID(ConstValue.DJDYLY.GZ, getXMBH(), djdy.getDJDYID());
				SubRights bdcfsql = RightsTools.loadSubRightsByRightsID(ConstValue.DJDYLY.GZ, bdcql.getId());
				List<RightsHolder> bdcqlrs = RightsHolderTools.loadRightsHolders(ConstValue.DJDYLY.GZ, djdy.getDJDYID(), getXMBH());
				MessageExport msg = super.getShareMsgTools().GetMsg(bdcdy, bdcql, bdcfsql, bdcqlrs, bljc, xmxx);
				super.getShareMsgTools().SendMsg(msg, idjdy + 1, djdy.getBDCDYLX(), xmxx);
			}
		}
	}

	
}
