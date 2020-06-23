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
import com.supermap.realestate.registration.dataExchange.cfdj.QLFQLCFDJ;
import com.supermap.realestate.registration.dataExchange.utils.packageXml;
import com.supermap.realestate.registration.handler.DJHandler;
import com.supermap.realestate.registration.model.BDCS_DJDY_GZ;
import com.supermap.realestate.registration.model.BDCS_FSQL_GZ;
import com.supermap.realestate.registration.model.BDCS_QL_GZ;
import com.supermap.realestate.registration.model.BDCS_SQR;
import com.supermap.realestate.registration.model.BDCS_XMXX;
import com.supermap.realestate.registration.model.BDCS_ZH_XZ;
import com.supermap.realestate.registration.model.interfaces.AgriculturalLand;
import com.supermap.realestate.registration.model.interfaces.RealUnit;
import com.supermap.realestate.registration.model.interfaces.Rights;
import com.supermap.realestate.registration.model.interfaces.RightsHolder;
import com.supermap.realestate.registration.model.interfaces.SubRights;
import com.supermap.realestate.registration.model.interfaces.UseLand;
import com.supermap.realestate.registration.model.xmlExportmodel.MessageExport;
import com.supermap.realestate.registration.service.impl.ProjectServiceImpl;
import com.supermap.realestate.registration.tools.RightsTools;
import com.supermap.realestate.registration.tools.UnitTools;
import com.supermap.realestate.registration.util.ConfigHelper;
import com.supermap.realestate.registration.util.ConstValue;
import com.supermap.realestate.registration.util.ConstValue.BDCDYLX;
import com.supermap.realestate.registration.util.ConstValue.CFLX;
import com.supermap.realestate.registration.util.ConstValue.DJDYLY;
import com.supermap.realestate.registration.util.ConstValue.QLLX;
import com.supermap.realestate.registration.util.ProjectHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.realestate.registration.util.YwLogUtil;
import com.supermap.wisdombusiness.core.SuperSpringContext;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;

/*
 1、国有建设用地使用权查封登记
 2、集体建设用地使用权查封登记（未配置）
 3、宅基地使用权查封登记（未配置）
 */
/**
 * 宗地查封处理类
 * @ClassName: CFDJ_LandHandler
 * @author wuzhu
 * @date 2015年9月8日 下午10:21:04
 */
public class CFDJ_LandHandler extends DJHandlerBase implements DJHandler {

	public CFDJ_LandHandler(ProjectInfo info) {
		super(info);
	}

	@Override
	public boolean addBDCDY(String bdcdyid) {
		boolean bSuccess = false;
		CommonDao dao = this.getCommonDao();
		if (ValidateDup(dao, bdcdyid))// 重复的插入 忽略掉
			return true;
		BDCS_DJDY_GZ djdy = super.createDJDYfromXZ(bdcdyid);
		if (djdy != null) {
			RealUnit unit = null;
			try {
				UnitTools.loadUnit(this.getBdcdylx(), DJDYLY.initFrom(djdy.getLY()), djdy.getBDCDYID());
			} catch (Exception e) {
			}
			// 生成权利信息
			BDCS_QL_GZ ql = super.createQL(djdy, unit);
			// 生成附属权利
			BDCS_FSQL_GZ fsql = super.createFSQL(djdy.getDJDYID());
			ql.setFSQLID(fsql.getId());
			fsql.setQLID(ql.getId());
			// TODO @刘树峰 查封要选择权利
			fsql.setCFFW(ConfigHelper.getNameByValue("DEFAULTCFFW"));
			fsql.setCFWJ(ConfigHelper.getNameByValue("DEFAULTCFWJ"));
			fsql.setCFLX(CFLX.CF.Value);
			BDCS_XMXX xmxx=dao.get(BDCS_XMXX.class, super.getXMBH());
			if(xmxx!=null){
				fsql.setCFSJ(xmxx.getSLSJ());
			}
			String djdyid = djdy.getDJDYID();
			// 计算轮候顺序，判断是查封还是轮候查封
			String sqlSeal = MessageFormat.format(" from BDCK.BDCS_QL_XZ WHERE djdyid=''{0}'' and djlx=''800'' and qllx=''99''", djdyid);
			long sealcount = dao.getCountByFullSql(sqlSeal);
			int lhsx = 0;
			if (sealcount > 0) {
				// 先设置为个数加1，放置两个都为空的情况
				int cxz = (int) sealcount;
				fsql.setCFLX(CFLX.LHCF.Value);
				String sqlXZ = MessageFormat.format(
						"SELECT MAX(LHSX) ZDXH FROM BDCK.BDCS_FSQL_XZ A LEFT JOIN BDCK.BDCS_QL_XZ B ON A.QLID=B.QLID WHERE B.DJDYID=''{0}'' AND B.DJLX=''800'' AND B.QLLX=''99''",
						djdyid);
				@SuppressWarnings("rawtypes")
				List<Map> mpXZlist = dao.getDataListByFullSql(sqlXZ);
				if (mpXZlist != null && mpXZlist.size() > 0) {
					@SuppressWarnings("rawtypes")
					Map mpxz = mpXZlist.get(0);
					if (mpxz != null && mpxz.containsKey("ZDXH")) {
						String cstrxz = StringHelper.formatObject(mpxz.get("ZDXH"));
						try {
							if (!StringHelper.isEmpty(cstrxz)) {
								cxz = Integer.parseInt(cstrxz) + 1;
								lhsx = cxz;
							}
						} catch (Exception e) {
						}
					}
				}
				lhsx = Math.max(lhsx, cxz);
			}
			// 判断工作层中是否存在未登簿的关于该登记单元的其他查封信息
			String sqlGZ = MessageFormat
					.format("SELECT MAX(LHSX) ZDXH FROM BDCK.BDCS_FSQL_GZ A LEFT JOIN BDCK.BDCS_QL_GZ B ON A.QLID=B.QLID LEFT JOIN BDCK.BDCS_DJDY_GZ C ON C.DJDYID=B.DJDYID LEFT JOIN BDCK.BDCS_XMXX D ON C.XMBH=D.XMBH WHERE D.SFDB=0 AND   B.DJDYID=''{0}'' AND B.DJLX='800' AND B.QLLX='99'",
							djdyid);
			@SuppressWarnings("rawtypes")
			List<Map> mpGZlist = dao.getDataListByFullSql(sqlGZ);
			if (mpGZlist != null && mpGZlist.size() > 0) {
				int cgz = 0;

				@SuppressWarnings("rawtypes")
				Map mpgz = mpGZlist.get(0);
				if (mpgz != null && mpgz.containsKey("ZDXH")) {
					String cstrgz = StringHelper.formatObject(mpgz.get("ZDXH"));
					try {
						if (!StringHelper.isEmpty(cstrgz)) {
							cgz = Integer.parseInt(cstrgz);
							lhsx = Math.max(cgz + 1, lhsx);
							fsql.setCFLX(CFLX.LHCF.Value);
						}
					} catch (Exception ee) {

					}
				}
				lhsx = Math.max(lhsx, cgz);
			}
			// 设置轮候顺序
			fsql.setLHSX(lhsx);
			// 保存
			dao.save(djdy);
			dao.save(ql);
			dao.save(fsql);
			bSuccess = true;
		}
		dao.flush();
		return bSuccess;
	}

	// 验证是否重复 重复返回TRUE否则返回FALSE
	private boolean ValidateDup(CommonDao dao, String bdcdyid) {
		String hql = "BDCDYID='" + bdcdyid + "' AND XMBH='" + super.getXMBH() + "'";// 通过不动产单元ID和项目编号判断是否重复

		List<BDCS_DJDY_GZ> list = dao.getDataList(BDCS_DJDY_GZ.class, hql);
		if (list != null && list.size() > 0) {
			return true;
		}
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
					// super.CopyGZDJDYToXZAndLS(key_djdy);
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

	}

	@Override
	public void removeQLR(String qlid, String qlrid) {

	}

	@Override
	public String getError() {
		return null;
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
				String result = "";
				Message msg = exchangeFactory.createMessageByCFDJ();
				for (int i = 0; i < djdys.size(); i++) {
					BDCS_DJDY_GZ djdy = djdys.get(i);
					BDCS_QL_GZ ql = super.getQL(djdy.getDJDYID());
					BDCS_FSQL_GZ fsql = null;
					List<BDCS_SQR> sqrs = new ArrayList<BDCS_SQR>();
					ProjectServiceImpl serviceImpl = SuperSpringContext.getContext().getBean(ProjectServiceImpl.class);
					sqrs = serviceImpl.getSQRList(super.getXMBH());
					
					if (ql != null && !StringUtils.isEmpty(ql.getFSQLID())) {
						fsql = dao.get(BDCS_FSQL_GZ.class, ql.getFSQLID());
					}
					if (super.getBdcdylx()!=null&&"NYD".equals(super.getBdcdylx().toString())) {
						 //农用地
						RealUnit unit=UnitTools.loadUnit(BDCDYLX.NYD, DJDYLY.GZ, djdy.getBDCDYID());
                    	if(unit==null) {
                    		unit=UnitTools.loadUnit(BDCDYLX.NYD, DJDYLY.XZ, djdy.getBDCDYID());
                    	}
                    	AgriculturalLand nyd=(AgriculturalLand)unit;
                    	super.fillHead(msg, i, ywh,nyd.getBDCDYH(),nyd.getQXDM(),ql.getLYQLID());
                    	msg.getHead().setParcelID(StringHelper.formatObject(nyd.getZDDM()));
						msg.getHead().setEstateNum(StringHelper.formatObject(nyd.getBDCDYH()));
						msg.getHead().setRecType("8000101");
//						msg.getHead().setPreEstateNum(StringHelper.formatObject(nyd.getBDCDYH()));
                    	//查封登记
                    	QLFQLCFDJ cfdj = msg.getData().getQLFQLCFDJ();
						cfdj = packageXml.getQLFQLCFDJ(cfdj, null, null, ql, fsql, ywh,null);
						//维护
						if((cfdj.getBdcdyh()==null||cfdj.getBdcdyh().length()<=0)&&ql!=null&&ql.getBDCDYH()!=null&&ql.getBDCDYH().length()>0) {
							cfdj.setBdcdyh(ql.getBDCDYH());
						}
//						cfdj.setQxdm(StringHelper.formatObject(nyd.getQXDM()) == "" ? ConfigHelper.getNameByValue("XZQHDM")
//								: nyd.getQXDM());
						cfdj.setBdcdyh(StringHelper.formatDefaultValue(nyd.getBDCDYH()));
						msg.getData().setQLFQLCFDJ(cfdj);
						// SLSQ 受理申请
						DJTDJSLSQ sq = msg.getData().getDJSLSQ();
						sq = packageXml.getDJTDJSLSQByNYD(sq, nyd, ql, xmxx);
						msg.getData().setDJSLSQ(sq);
						// SJ 收件
						List<DJFDJSJ> sj = msg.getData().getDJSJ();
						sj = packageXml.getDJFDJSJ(nyd, ywh,actinstID);
						for(DJFDJSJ d:sj) {
							d.setYSDM("6002020400");
						}
						msg.getData().setDJSJ(sj);
						 // SF 收费
						List<DJFDJSF> sfList = msg.getData().getDJSF();
						sfList = packageXml.getDJSF(nyd, ywh, this.getXMBH());
						for(DJFDJSF d :sfList ) {
							d.setYSDM("6002020400");
							//d.setQXDM(StringHelper.formatObject(nyd.getQXDM()));
						}
						msg.getData().setDJSF(sfList);
						 // SH 审核
						List<DJFDJSH> sh = msg.getData().getDJSH();
						sh = packageXml.getDJFDJSH(nyd, ywh, this.getXMBH(), actinstID);
						for(DJFDJSH d:sh) {
							d.setYSDM("6002020400");
						} 
						msg.getData().setDJSH(sh);
				        // SZ 缮证 
						List<DJFDJSZ> sz = packageXml.getDJFDJSZ(nyd, ywh, this.getXMBH());
				        // FZ 发证
						List<DJFDJFZ> fz = packageXml.getDJFDJFZ(nyd, ywh, this.getXMBH());
						// GD 归档
						List<DJFDJGD> gd = packageXml.getDJFDJGD(nyd, ywh,this.getXMBH());
						// SQR 申请人
						List<DJFDJSQR> djsqrs = msg.getData().getDJSQR();
						djsqrs = packageXml.getDJSQRs(djsqrs, sqrs, nyd.getYSDM(), ywh, nyd.getBDCDYH());
						if(nyd!=null) {
							for(DJFDJGD d:gd) {
								d.setYSDM("6002020400");
								//d.setQXDM(StringHelper.formatObject(nyd.getQXDM()));
								d.setZL(StringHelper.formatObject(nyd.getZL()));
							}
							for(DJFDJFZ d:fz) {
								d.setYSDM("6002020400");
								//d.setQXDM(StringHelper.formatObject(nyd.getQXDM()));
							}
							for(DJFDJSQR d:djsqrs) {
								d.setYsdm("6002020400");
							}
							
							for(DJFDJSZ d:sz) {
								d.setYSDM("6002020400");
								//d.setQXDM(StringHelper.formatObject(nyd.getQXDM()));
							}
						}
						msg.getData().setDJSQR(djsqrs);
						msg.getData().setDJSZ(sz);
						msg.getData().setDJFZ(fz);
						msg.getData().setDJGD(gd);
						// FJ 非结    （结构化文档）
						FJF100 fj = msg.getData().getFJF100();
						fj = packageXml.getFJF(fj);
						msg.getData().setFJF100(fj);
						 
					 }else if(super.getBdcdylx()!=null&&"HY".equals(super.getBdcdylx().toString())){
						 //海域
						 BDCS_ZH_XZ zh = dao.get(BDCS_ZH_XZ.class, djdy.getBDCDYID());//宗海基本信息
							//BDCS_ZH_XZ
							super.fillHead(msg, i, ywh,zh.getBDCDYH(),zh.getQXDM(),ql.getLYQLID());
							msg.getHead().setParcelID(StringHelper.formatObject(zh.getZHDM()));
							msg.getHead().setEstateNum(StringHelper.formatObject(zh.getBDCDYH()));
							msg.getHead().setRecType("8000101");
//							msg.getHead().setPreEstateNum(StringHelper.formatObject(zh.getBDCDYH()));
							if(zh != null && !StringUtils.isEmpty(zh.getQXDM())){
								msg.getHead().setAreaCode(zh.getQXDM());
							}
							if (djdy != null) {
								try {
									QLFQLCFDJ cfdj = msg.getData().getQLFQLCFDJ();
									cfdj = packageXml.getQLFQLCFDJ(cfdj, null, null, ql, fsql, ywh,zh);
									//维护不动产单元号
									if((cfdj.getBdcdyh()==null||cfdj.getBdcdyh().length()<=0)&&ql!=null&&ql.getBDCDYH()!=null&&ql.getBDCDYH().length()>0) {
										cfdj.setBdcdyh(ql.getBDCDYH());
									}
									msg.getData().setQLFQLCFDJ(cfdj);
									
									DJTDJSLSQ sq = msg.getData().getDJSLSQ();
									sq = packageXml.getDJTDJSLSQ(sq, ywh, null, ql, xmxx, null, xmxx.getSLSJ(), null, null, zh);
									msg.getData().setDJSLSQ(sq);
									
									List<DJFDJSJ> sj = msg.getData().getDJSJ();
									sj = packageXml.getDJFDJSJ(zh, ywh,actinstID);
									msg.getData().setDJSJ(sj);

									List<DJFDJSF> sfList = msg.getData().getDJSF();
									sfList = packageXml.getDJSF(zh, ywh,this.getXMBH());
									msg.getData().setDJSF(sfList);
									
									List<DJFDJSH> sh = msg.getData().getDJSH();
									sh = packageXml.getDJFDJSH(zh, ywh, this.getXMBH(), actinstID);
									msg.getData().setDJSH(sh);

									List<DJFDJSZ> sz = packageXml.getDJFDJSZ(zh, ywh, this.getXMBH());		
									msg.getData().setDJSZ(sz);
									List<DJFDJFZ> fz = packageXml.getDJFDJFZ(zh,ywh,this.getXMBH());
									msg.getData().setDJFZ(fz);
//									List<DJFDJGD>  gd = packageXml.getDJFDJGD(msg.getData().getDJGD(), zd, null ,ywh, null, null,this.getXMBH());
//									msg.getData().setDJGD(gd);	
									List<DJFDJGD> gd = packageXml.getDJFDJGD(zh,ywh,this.getXMBH());
									msg.getData().setDJGD(gd);
									List<DJFDJSQR> djsqrs = msg.getData().getDJSQR();
									//查封现在的不填写申请人，为了和接入规范一致，先在这里拼凑出申请人。
									DJFDJSQR djsqr = new DJFDJSQR();
									djsqr.setYsdm("2004020000");
									djsqr.setQlrmc(cfdj == null ? "无" :StringHelper.formatDefaultValue(cfdj.getCfjg()));
									djsqr.setYwh(ywh);
									//djsqr.setQxdm(ConfigHelper.getNameByValue("XZQHDM"));
									djsqrs.add(djsqr);
									djsqrs = packageXml.getDJSQRs(djsqrs, sqrs, zh.getYSDM(), ywh, zh.getBDCDYH());
									msg.getData().setDJSQR(djsqrs);
									FJF100 fj = msg.getData().getFJF100();
									fj = packageXml.getFJF(fj);
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						
						
						 
					 }
//					List<BDCS_QLR_GZ> qlrs = super.getQLRs(ql.getId());
					RealUnit unit=UnitTools.loadUnit(BDCDYLX.SHYQZD, DJDYLY.XZ, djdy.getBDCDYID());
					if (unit!=null) {
						//查询宗地信息
						UseLand zd=(UseLand)unit;
						super.fillHead(msg, i, ywh,zd.getBDCDYH(),zd.getQXDM(),ql.getLYQLID());
						msg.getHead().setParcelID(StringHelper.formatObject(zd.getZDDM()));
						msg.getHead().setEstateNum(StringHelper.formatObject(zd.getBDCDYH()));
						msg.getHead().setRecType("8000101");
//						msg.getHead().setPreEstateNum(StringHelper.formatObject(zd.getBDCDYH()));
						if(zd != null && !StringUtils.isEmpty(zd.getQXDM())){
							msg.getHead().setAreaCode(zd.getQXDM());
						}
						
						if (djdy != null) {
							try {
								QLFQLCFDJ cfdj = msg.getData().getQLFQLCFDJ();
								cfdj = packageXml.getQLFQLCFDJ(cfdj, zd, null, ql, fsql, ywh,null);

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

								List<DJFDJSZ> sz = packageXml.getDJFDJSZ(zd, ywh,this.getXMBH());		
								msg.getData().setDJSZ(sz);
								List<DJFDJFZ> fz = packageXml.getDJFDJFZ(zd,ywh,this.getXMBH());
								msg.getData().setDJFZ(fz);
								List<DJFDJGD>  gd = packageXml.getDJFDJGD(zd,ywh,this.getXMBH());
								msg.getData().setDJGD(gd);	
								List<DJFDJSQR> djsqrs = msg.getData().getDJSQR();
								//查封现在的不填写申请人，为了和接入规范一致，先在这里拼凑出申请人。
								DJFDJSQR djsqr = new DJFDJSQR();
								djsqr.setYsdm("2004020000");
								djsqr.setQlrmc(cfdj == null ? "无" :StringHelper.formatDefaultValue(cfdj.getCfjg()));
								djsqr.setYwh(ywh);
								//djsqr.setQxdm(ConfigHelper.getNameByValue("XZQHDM"));
								djsqrs.add(djsqr);
								djsqrs = packageXml.getDJSQRs(djsqrs, sqrs, zd.getYSDM(), ywh, zd.getBDCDYH());
								msg.getData().setDJSQR(djsqrs);
								
								FJF100 fj = msg.getData().getFJF100();
								fj = packageXml.getFJF(fj);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
					
					String msgName = getMessageFileName( msg,  i ,djdys.size(),names,djdy);
					mashaller.marshal(msg, new File(path +msgName));
					result = super.uploadFile(path +msgName, super.getRecType(), actinstID, djdy.getDJDYID(), ql.getId());
					if(result.equals("")|| result==null){
						Map<String, String> xmlError = new HashMap<String, String>();
						xmlError.put("error", "连接SFTP失败,请检查服务器和前置机的连接是否正常");
						YwLogUtil.addSjsbResult("Biz" + msg.getHead().getBizMsgID() + ".xml", "", "连接SFTP失败,请检查服务器和前置机的连接是否正常", ConstValue.SF.NO.Value, ConstValue.RECCODE.CF_CFDJ.Value,ProjectHelper.getpRroinstIDByActinstID(actinstID));
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
				List<RightsHolder> bdcqlrs = null;
				MessageExport msg = super.getShareMsgTools().GetMsg(bdcdy, bdcql, bdcfsql, bdcqlrs, bljc, xmxx);
				super.getShareMsgTools().SendMsg(msg, idjdy + 1, djdy.getBDCDYLX(), xmxx);
			}
		}
	}
}
