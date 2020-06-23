package com.supermap.realestate.registration.handlerImpl;

import java.io.File;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
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
import com.supermap.realestate.registration.dataExchange.utils.packageXml;
import com.supermap.realestate.registration.handler.DJHandler;
import com.supermap.realestate.registration.model.BDCS_DJDY_GZ;
import com.supermap.realestate.registration.model.BDCS_DJDY_XZ;
import com.supermap.realestate.registration.model.BDCS_FSQL_GZ;
import com.supermap.realestate.registration.model.BDCS_H_XZ;
import com.supermap.realestate.registration.model.BDCS_H_XZY;
import com.supermap.realestate.registration.model.BDCS_QLR_GZ;
import com.supermap.realestate.registration.model.BDCS_QL_GZ;
import com.supermap.realestate.registration.model.BDCS_SHYQZD_XZ;
import com.supermap.realestate.registration.model.BDCS_SQR;
import com.supermap.realestate.registration.model.BDCS_XMXX;
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
import com.supermap.realestate.registration.util.GetProperties;
import com.supermap.realestate.registration.util.ProjectHelper;
import com.supermap.realestate.registration.util.YwLogUtil;
import com.supermap.realestate.registration.util.ConstValue.BDCDYLX;
import com.supermap.realestate.registration.util.ConstValue.CFLX;
import com.supermap.realestate.registration.util.ConstValue.CZFS;
import com.supermap.realestate.registration.util.ConstValue.DJDYLY;
import com.supermap.realestate.registration.util.ConstValue.QLLX;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.wisdombusiness.core.SuperSpringContext;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;


public class CFDJ_ZDHandler extends DJHandlerBase implements DJHandler {

	public CFDJ_ZDHandler(ProjectInfo info) {
		super(info);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean addBDCDY(String qlStrs) {
		boolean isexist = false;
		if (!StringHelper.isEmpty(qlStrs)) {
			String ids[] = qlStrs.split(",");
			HashSet<String> hs = new HashSet<String>();
			for (String string : ids) {
				hs.add(string);
			}
			if(hs.size()<0){
				return false;
			}
			for (String qlStr : hs) {
				if(StringHelper.isEmpty(qlStr)){
					continue;
				}
				isexist = true;
				String qlid = qlStr;
				if(ValidateDup(qlid)){
					return false;
				}
				Rights Right = RightsTools.loadRights(DJDYLY.XZ, qlid);
				if(Right!=null){
					String djdyid = Right.getDJDYID();
					List<BDCS_DJDY_XZ> djdys = getCommonDao().getDataList(BDCS_DJDY_XZ.class, "DJDYID='"+ djdyid +"'");
					if(djdys!=null&&djdys.size()>0){
						String _bdcdyid = djdys.get(0).getBDCDYID();
						/*
						 * 单元
						 */
						BDCS_DJDY_GZ djdy = super.createDJDYfromXZ(_bdcdyid);
						RealUnit unit = null;
						try {
							unit = UnitTools.loadUnit(this.getBdcdylx(), DJDYLY.XZ, _bdcdyid);
						} catch (Exception e) {
						}
						/*
						 * 生成查封权利
						 */
						BDCS_QL_GZ ql = super.createQL(djdy, unit);
						ql.setCZFS(CZFS.GTCZ.Value);
						ql.setMAINQLID(qlid);
						/*
						 * 生成附属权利
						 */
						BDCS_FSQL_GZ fsql = super.createFSQL(djdy.getDJDYID());
						ql.setFSQLID(fsql.getId());
						fsql.setQLID(ql.getId());
						fsql.setCFWJ(ConfigHelper.getNameByValue("DEFAULTCFWJ"));
						fsql.setCFFW(ConfigHelper.getNameByValue("DEFAULTCFFW"));
						
						// 计算轮候顺序，判断是查封还是轮候查封
						// 判断是否存在查封信息
						String sqlSeal = MessageFormat.format(" from BDCK.BDCS_QL_XZ where djdyid=''{0}'' and djlx=''800'' and qllx=''99'' and mainqlid=''{1}''", djdyid,qlid);
						long sealcount = getCommonDao().getCountByFullSql(sqlSeal);
						int lhsx = 0;
						fsql.setCFLX(CFLX.CF.Value);
						
						BDCS_XMXX xmxx=getCommonDao().get(BDCS_XMXX.class, super.getXMBH());
						if(xmxx!=null){
							fsql.setCFSJ(xmxx.getSLSJ());
						}
						// 判断现状中是否存在查封信息，如果存在，取最大的轮候顺序
						if (sealcount > 0) {
							// 先设置为个数加1，放置两个都为空的情况
							int cxz = (int) sealcount;
							fsql.setCFLX(CFLX.LHCF.Value);
							String sqlXZ = MessageFormat.format(
									"SELECT MAX(LHSX) ZDXH FROM BDCK.BDCS_FSQL_XZ A LEFT JOIN BDCK.BDCS_QL_XZ B ON A.QLID=B.QLID WHERE  B.DJDYID=''{0}'' AND B.DJLX='800' AND B.QLLX='99' AND B.MAINQLID=''{1}''",
									djdyid,qlid);
							@SuppressWarnings("rawtypes")
							List<Map> mpXZlist = getCommonDao().getDataListByFullSql(sqlXZ);
							if (mpXZlist != null && mpXZlist.size() > 0) {
								@SuppressWarnings("rawtypes")
								Map mpxz = mpXZlist.get(0);
								if (mpxz != null && mpxz.containsKey("ZDXH")) {
									String cstrxz = StringHelper.formatObject(mpxz.get("ZDXH"));
									try {
										if (!StringHelper.isEmpty(cstrxz)) {
											lhsx = Integer.parseInt(cstrxz);
											
										}
									} catch (Exception ee) {
									}
								}
							}
							lhsx = Math.max(lhsx, cxz);
						}
						// 判断工作层中是否存在未登簿的关于该登记单元的其他查封信息
						String sqlGZ = MessageFormat
								.format("SELECT MAX(LHSX) ZDXH FROM BDCK.BDCS_FSQL_GZ A LEFT JOIN BDCK.BDCS_QL_GZ B ON A.QLID=B.QLID LEFT JOIN BDCK.BDCS_DJDY_GZ C ON C.DJDYID=B.DJDYID LEFT JOIN BDCK.BDCS_XMXX D ON C.XMBH=D.XMBH WHERE D.SFDB=0 AND "
										+ "  B.DJDYID=''{0}'' AND B.DJLX='800' AND B.QLLX='99' AND B.MAINQLID=''{1}''",
										djdyid,qlid);
						@SuppressWarnings("rawtypes")
						List<Map> mpGZlist = getCommonDao().getDataListByFullSql(sqlGZ);
						if (mpGZlist != null && mpGZlist.size() > 0) {
							int cgz = 0;

							@SuppressWarnings("rawtypes")
							Map mpgz = mpGZlist.get(0);
							if (mpgz != null && mpgz.containsKey("ZDXH")) {
								String cstrgz = StringHelper.formatObject(mpgz.get("ZDXH"));
								try {
									if (!StringHelper.isEmpty(cstrgz)) {
										cgz = Integer.parseInt(cstrgz);
										lhsx = Math.max(cgz, lhsx);
										fsql.setCFLX(CFLX.LHCF.Value);
									}
								} catch (Exception ee) {

								}
							}
							lhsx = Math.max(lhsx, cgz);
						}
						// 设置轮候顺序
						fsql.setLHSX(lhsx+1);
						
						// 保存
						getCommonDao().save(djdy);
						getCommonDao().save(ql);
						getCommonDao().save(fsql);
						getCommonDao().flush();
						return true;
					}else{
						return false;
					}
				}else{
					return false;
				}
			}
			if(!isexist){
				super.setErrMessage("请重新选择权利");
				return false;
			}
		}
		this.getCommonDao().flush();
		return true;
	}
	
	private boolean ValidateDup(String qlid) {
		String fromSql = "FROM BDCK.BDCS_QL_GZ QL WHERE QL.LYQLID='"+ qlid +"' AND QL.XMBH='"+ getXMBH() +"'";
	    long count = getCommonDao().getCountByFullSql(fromSql);
	    if(count>0)
	    	return true;
		return false;
	}

	@Override
	public boolean writeDJB() {
		String strSqlXMBH = ProjectHelper.GetXMBHCondition(getXMBH());
		List<BDCS_DJDY_GZ> djdys = getCommonDao().getDataList(BDCS_DJDY_GZ.class, strSqlXMBH);
		if (djdys != null && djdys.size() > 0) {
			for (int idjdy = 0; idjdy < djdys.size(); idjdy++) {
				BDCS_DJDY_GZ bdcs_djdy_gz = djdys.get(idjdy);
				if (bdcs_djdy_gz != null) {
					String djdyid = bdcs_djdy_gz.getDJDYID();
					super.CopyGZQLToXZAndLS(djdyid);
					super.CopyGZQLRToXZAndLS(djdyid);
					super.CopyGZQDZRToXZAndLS(djdyid);
					super.CopyGZZSToXZAndLS(djdyid);
					// 暂停所有包含查封单元的在办项目
					this.SetXMCFZT(djdyid, "01");
				}
			}
		}
		this.SetSFDB();
		getCommonDao().flush();
		return true;
	}

	@Override
	public boolean removeBDCDY(String bdcdyid) {
		boolean bsuccess = false;
		CommonDao baseCommonDao = this.getCommonDao();
		BDCS_DJDY_GZ djdy = super.removeDJDY(bdcdyid);
		if (djdy != null) {
			String djdyid = djdy.getDJDYID();
			// 删除登记单元索引
			baseCommonDao.deleteEntity(djdy);

			//删除权利、附属权利、权利人、证书、权地证人关系
			String _hqlCondition=MessageFormat.format("XMBH=''{0}'' AND DJDYID=''{1}''", getXMBH(),djdyid);
			RightsTools.deleteRightsAllByCondition(DJDYLY.GZ, _hqlCondition);
			
		}
		baseCommonDao.flush();
		bsuccess = true;
		return bsuccess;
	}

	@Override
	public List<UnitTree> getUnitTree() {
		return super.getUnitList();
	}

	@Override
	public void addQLRbySQRArray(String qlid, Object[] sqrids) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeQLR(String qlid, String qlrid) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getError() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void SendMsg(String bljc) {
		BDCS_XMXX xmxx=getCommonDao().get(BDCS_XMXX.class, super.getXMBH());
		String xmbhFilter=ProjectHelper.GetXMBHCondition(super.getXMBH());
		List<BDCS_DJDY_GZ> djdys = getCommonDao().getDataList(
				BDCS_DJDY_GZ.class, xmbhFilter);
		if (djdys != null && djdys.size() > 0) {
			boolean isZipFile = false;
			if(djdys.size()>=10){
				isZipFile=true;
				String folderPath=GetProperties.getConstValueByKey("xmlPath") + "\\" + xmxx.getPROJECT_ID()+"_"+bljc;
				super.getShareMsgTools().deleteFolder(folderPath);
			}
			for (int idjdy = 0; idjdy < djdys.size(); idjdy++) {
				BDCS_DJDY_GZ djdy = djdys.get(idjdy);
				ConstValue.BDCDYLX dylx = ConstValue.BDCDYLX.initFrom(djdy.getBDCDYLX());
				ConstValue.DJDYLY dyly = ConstValue.DJDYLY.initFrom(djdy.getLY());
				RealUnit bdcdy = UnitTools.loadUnit(dylx, dyly, djdy.getBDCDYID());
				Rights bdcql=RightsTools.loadRightsByDJDYID(ConstValue.DJDYLY.GZ, getXMBH(), djdy.getDJDYID());
				SubRights bdcfsql=RightsTools.loadSubRightsByRightsID(ConstValue.DJDYLY.GZ, bdcql.getId());
				List<RightsHolder> bdcqlrs=RightsHolderTools.loadRightsHolders(ConstValue.DJDYLY.GZ, djdy.getDJDYID(), getXMBH());
				MessageExport msg= super.getShareMsgTools().GetMsg(bdcdy,bdcql,bdcfsql,bdcqlrs,bljc,xmxx);
				if(isZipFile){
					String folderPath = super.getShareMsgTools().createXMLInFile(xmxx, msg, idjdy+1,bljc);
					if(idjdy == djdys.size()-1){//文件都生成到文件夹以后再压缩上传
						super.getShareMsgTools().SendMsg(folderPath, xmxx, bljc, djdy.getBDCDYLX());
					}
				}
				else{
					super.getShareMsgTools().SendMsg(msg, idjdy + 1, djdy.getBDCDYLX(), xmxx);				
				}
			}
		}
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
				@SuppressWarnings("unused")
				String cyear = calendar.get(Calendar.YEAR) + "";
				String ywh = packageXml.GetYWLSHByYWH(this.getProject_id());
				BDCS_XMXX xmxx = dao.get(BDCS_XMXX.class, super.getXMBH());
				String result = "";
				for(int i = 0; i < djdys.size(); i ++){
					BDCS_DJDY_GZ djdy = djdys.get(i);
					
					String condition = MessageFormat.format("DJDYID=''{0}'' AND QLLX=''23'' ", djdy.getDJDYID());
					List<Rights> _rightss = RightsTools.loadRightsByCondition(DJDYLY.GZ, condition);
					BDCS_QL_GZ ql = null;
					List<BDCS_QLR_GZ> qlrs = new ArrayList<BDCS_QLR_GZ>();
					for (Rights rights : _rightss) {
						ql = (BDCS_QL_GZ) rights;
						qlrs = super.getQLRs(ql.getId());
						if(qlrs != null){
							break;
						}
					}
					List<BDCS_SQR> sqrs = new ArrayList<BDCS_SQR>();
					ProjectServiceImpl serviceImpl = SuperSpringContext.getContext().getBean(ProjectServiceImpl.class);
					sqrs = serviceImpl.getSQRList(super.getXMBH());
					
					BDCS_FSQL_GZ fsql = new BDCS_FSQL_GZ();
					if(ql != null && !StringUtils.isEmpty(ql.getFSQLID())){
						fsql = dao.get(BDCS_FSQL_GZ.class, ql.getFSQLID());
					}
					

					Message msg = exchangeFactory.createMessageByDYQ();
					msg.getHead().setRecType("9000101");

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
								QLFQLDYAQ dyaq = msg.getData().getQLFQLDYAQ();
								dyaq = packageXml.getQLFQLDYAQ(dyaq, zd, ql, fsql, ywh, null);

								List<ZTTGYQLR> zttqlr = msg.getData().getGYQLR();
								zttqlr = packageXml.getZTTGYQLRs(zttqlr, qlrs, zd, ql,null, null, null, null);
								msg.getData().setGYQLR(zttqlr);

								DJTDJSLSQ sq = msg.getData().getDJSLSQ();
								sq = packageXml.getDJTDJSLSQ(sq, ywh, zd, ql, xmxx, null, xmxx.getSLSJ(), null, null, null);

								List<DJFDJSJ> sj = msg.getData().getDJSJ();
								sj = packageXml.getDJFDJSJ(zd, ywh,actinstID);
								msg.getData().setDJSJ(sj);
								
								List<DJFDJSF> sfList = msg.getData().getDJSF();
								sfList = packageXml.getDJSF(zd,ywh,this.getXMBH());
								msg.getData().setDJSF(sfList);
								
								List<DJFDJSH> sh = msg.getData().getDJSH();
								sh = packageXml.getDJFDJSH(zd, ywh, this.getXMBH(), actinstID);
								msg.getData().setDJSH(sh);

								List<DJFDJSZ> sz = packageXml.getDJFDJSZ(zd, ywh, this.getXMBH());
								msg.getData().setDJSZ(sz);
								
								  List<DJFDJFZ> fz = packageXml.getDJFDJFZ(zd, ywh,this.getXMBH());
								  msg.getData().setDJFZ(fz);		
								 List<DJFDJGD> gd = packageXml.getDJFDJGD(zd, ywh,this.getXMBH());
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
					
					if(BDCDYLX.H.Value.equals(djdy.getBDCDYLX())){ //房屋
						try {
							BDCS_H_XZ h = null;
							h = dao.get(BDCS_H_XZ.class, djdy.getBDCDYID());
							super.fillHead(msg, i, ywh,h.getBDCDYH(),h.getQXDM(),ql.getLYQLID());
							if(h != null){
								BDCS_SHYQZD_XZ zd = dao.get(BDCS_SHYQZD_XZ.class, h.getZDBDCDYID());
								h.setZDDM(zd.getZDDM());
							}
							msg.getHead().setParcelID(StringHelper.formatObject(h.getZDDM()));
							msg.getHead().setEstateNum(StringHelper.formatObject(h.getBDCDYH()));
//							msg.getHead().setPreEstateNum(StringHelper.formatObject(h.getBDCDYH()));
							if(h != null && !StringUtils.isEmpty(h.getQXDM())){
								msg.getHead().setAreaCode(h.getQXDM());
							}
							if (djdy != null) {
								
								QLFQLDYAQ dyaq = msg.getData().getQLFQLDYAQ();
								dyaq = packageXml.getQLFQLDYAQ(dyaq, null, ql, fsql, ywh, h);
								
								List<DJFDJSJ> sj = msg.getData().getDJSJ();
								sj = packageXml.getDJFDJSJ(h, ywh,actinstID);
								msg.getData().setDJSJ(sj);
								
								List<DJFDJSF> sfList = msg.getData().getDJSF();
								sfList = packageXml.getDJSF(h,ywh,this.getXMBH());
								msg.getData().setDJSF(sfList);
								
								List<DJFDJSH> sh = msg.getData().getDJSH();
								sh = packageXml.getDJFDJSH(h, ywh, this.getXMBH(), actinstID);
								msg.getData().setDJSH(sh);

								List<DJFDJSZ> sz = packageXml.getDJFDJSZ(h, ywh, this.getXMBH());
								msg.getData().setDJSZ(sz);
								
								List<DJFDJFZ>	fz = packageXml.getDJFDJFZ(h, ywh, this.getXMBH());
								msg.getData().setDJFZ(fz);	
								 List<DJFDJGD>	gd = packageXml.getDJFDJGD( h, ywh,this.getXMBH());
								 msg.getData().setDJGD(gd);	
								
								List<ZTTGYQLR> zttqlr = msg.getData().getGYQLR();
								zttqlr = packageXml.getZTTGYQLRs(zttqlr, qlrs, null, ql, h, null, null, null);
								msg.getData().setGYQLR(zttqlr);

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
					
					if(BDCDYLX.YCH.Value.equals(djdy.getBDCDYLX())){ //在建工程
						try {
							BDCS_H_XZY xzy = null;
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
								
								QLFQLDYAQ dyaq = msg.getData().getQLFQLDYAQ();
								dyaq = packageXml.getQLFQLDYAQ(dyaq, null, ql, fsql, ywh, xzy);
								
								List<DJFDJSJ> sj = msg.getData().getDJSJ();
								sj = packageXml.getDJFDJSJ(xzy, ywh,actinstID);
								msg.getData().setDJSJ(sj);
								
								List<DJFDJSF> sfList = msg.getData().getDJSF();
								sfList = packageXml.getDJSF( xzy,ywh, this.getXMBH());
								msg.getData().setDJSF(sfList);
								
								List<DJFDJSH> sh = msg.getData().getDJSH();
								sh = packageXml.getDJFDJSH(xzy, ywh, this.getXMBH(), actinstID);
								msg.getData().setDJSH(sh);

								List<DJFDJSZ> sz = packageXml.getDJFDJSZ(xzy, ywh, this.getXMBH());
								msg.getData().setDJSZ(sz);
								
								List<DJFDJFZ>	fz = packageXml.getDJFDJFZ(xzy, ywh, this.getXMBH());
								msg.getData().setDJFZ(fz);	
								 List<DJFDJGD>	gd = packageXml.getDJFDJGD(xzy, ywh,this.getXMBH());
								 msg.getData().setDJGD(gd);	
								
								List<ZTTGYQLR> zttqlr = msg.getData().getGYQLR();
								zttqlr = packageXml.getZTTGYQLRs(zttqlr, qlrs, null, ql, xzy, null, null, null);
								msg.getData().setGYQLR(zttqlr);

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
					mashaller.marshal(msg, new File(path + "Biz" + msg.getHead().getBizMsgID()+ ".xml"));
					result = uploadFile(path + "Biz" + msg.getHead().getBizMsgID()+ ".xml",ConstValue.RECCODE.DIYQ_ZXDJ.Value,actinstID,djdy.getDJDYID(),ql.getId());
					names.put(djdy.getDJDYID(),msg.getHead().getBizMsgID()+ ".xml");
					
					if(null == result){
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
	
}
