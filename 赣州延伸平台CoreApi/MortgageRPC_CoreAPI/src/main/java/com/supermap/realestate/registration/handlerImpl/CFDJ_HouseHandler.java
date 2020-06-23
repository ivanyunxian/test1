/**
 * 
 */
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
import com.supermap.realestate.registration.model.BDCS_H_XZ;
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
import com.supermap.realestate.registration.tools.RightsTools;
import com.supermap.realestate.registration.tools.UnitTools;
import com.supermap.realestate.registration.util.ConfigHelper;
import com.supermap.realestate.registration.util.ConstValue;
import com.supermap.realestate.registration.util.ConstValue.CFLX;
import com.supermap.realestate.registration.util.ConstValue.DJDYLY;
import com.supermap.realestate.registration.util.ProjectHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.realestate.registration.util.YwLogUtil;
import com.supermap.wisdombusiness.core.SuperSpringContext;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import com.supermap.wisdombusiness.web.ResultMessage;

/*
 1、国有建设用地使用权/房屋所有权查封登记
 2、集体建设用地使用权/房屋所有权查封登记（未配置）
 3、宅基地使用权/房屋所有权查封登记（未配置）
 */
/**
 * 房屋查封登记处理类
 * @ClassName: CFDJ_HouseHandler
 * @author liushufeng
 * @date 2015年9月8日 下午10:20:07
 */
public class CFDJ_HouseHandler extends DJHandlerBase implements DJHandler {

	public CFDJ_HouseHandler(ProjectInfo info) {
		super(info);
	}

	@Override
	public boolean addBDCDY(String bdcdyid) {
		if (!StringHelper.isEmpty(bdcdyid)) {
			String ids[] = bdcdyid.split(",");
			for (String id : ids) {
				if (StringHelper.isEmpty(id)) {
					continue;
				}
				// 循环添加单元
				ResultMessage msg = this.addbdcdy(id);
				if (msg.getSuccess().equals("false")) {
					super.setErrMessage(msg.getMsg());
				}
			}
		}
		this.getCommonDao().flush();
		return true;
	}

	/**
	 * 添加单元
	 * @author diaoliwei
	 * @date 2015-8-4
	 * @param bdcdyid
	 * @return
	 */
	private ResultMessage addbdcdy(String bdcdyid) {
		ResultMessage msg = new ResultMessage();
		CommonDao dao = this.getCommonDao();
		if (ValidateDup(dao, bdcdyid)) {// 重复的插入 忽略掉
			msg.setSuccess("false");
			return msg;
		}
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
			fsql.setCFWJ(ConfigHelper.getNameByValue("DEFAULTCFWJ"));
			fsql.setCFFW(ConfigHelper.getNameByValue("DEFAULTCFFW"));
			ql.setFSQLID(fsql.getId());
			fsql.setQLID(ql.getId());

			// 计算轮候顺序，判断是查封还是轮候查封
			String djdyid = djdy.getDJDYID();
			// 判断是否存在查封信息
			String sqlSeal = MessageFormat.format(" from BDCK.BDCS_QL_XZ where djdyid=''{0}'' and djlx=''800'' and qllx=''99''", djdyid);
			long sealcount = dao.getCountByFullSql(sqlSeal);
			int lhsx = 0;
			fsql.setCFLX(CFLX.CF.Value);
			BDCS_XMXX xmxx=dao.get(BDCS_XMXX.class, super.getXMBH());
			if(xmxx!=null){
				fsql.setCFSJ(xmxx.getSLSJ());
			}
			// 判断现状中是否存在查封信息，如果存在，取最大的轮候顺序
			if (sealcount > 0) {
				// 先设置为个数加1，放置两个都为空的情况
				int cxz = (int) sealcount;
				fsql.setCFLX(CFLX.LHCF.Value);
				String sqlXZ = MessageFormat.format(
						"SELECT MAX(LHSX) ZDXH FROM BDCK.BDCS_FSQL_XZ A LEFT JOIN BDCK.BDCS_QL_XZ B ON A.QLID=B.QLID WHERE  B.DJDYID=''{0}'' AND B.DJLX='800' AND B.QLLX='99'",
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
								lhsx = Integer.parseInt(cstrxz) + 1;
								
							}
						} catch (Exception ee) {
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
		}
		msg.setSuccess("true");
		return msg;
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
	public Map<String, String> exportXML(String path ,String actinstID) {
		Marshaller mashaller;
		Map<String, String> names = new HashMap<String,String>();
		try {
			CommonDao dao = super.getCommonDao();
			String xmbhHql = ProjectHelper.GetXMBHCondition(super.getXMBH());
			List<BDCS_DJDY_GZ> djdys = dao.getDataList(BDCS_DJDY_GZ.class, xmbhHql);
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
					List<BDCS_SQR> sqrs = new ArrayList<BDCS_SQR>();
					ProjectServiceImpl serviceImpl = SuperSpringContext.getContext().getBean(ProjectServiceImpl.class);
					sqrs = serviceImpl.getSQRList(super.getXMBH());
			 
					BDCS_FSQL_GZ fsql = null;
					if (ql != null && !StringUtils.isEmpty(ql.getFSQLID())) {
						fsql = dao.get(BDCS_FSQL_GZ.class, ql.getFSQLID());
					}

					BDCS_H_XZ h = dao.get(BDCS_H_XZ.class, djdy.getBDCDYID());
					if(h != null){
						BDCS_SHYQZD_XZ zd = dao.get(BDCS_SHYQZD_XZ.class, h.getZDBDCDYID());
						if(zd != null){
							h.setZDDM(zd.getZDDM());
						}
					}
					Message msg = exchangeFactory.createMessageByCFDJ();
					super.fillHead(msg, i, ywh,h.getBDCDYH(),h.getQXDM(),ql.getLYQLID());
					msg.getHead().setParcelID(StringHelper.formatObject(h.getZDDM()));
					msg.getHead().setEstateNum(StringHelper.formatObject(h.getBDCDYH()));
					msg.getHead().setRecType("8000101");
//					msg.getHead().setPreEstateNum(StringHelper.formatObject(h.getBDCDYH()));
					if(h != null && !StringUtils.isEmpty(h.getQXDM())){
						msg.getHead().setAreaCode(h.getQXDM());
					}
					if (djdy != null) {
						try {
							QLFQLCFDJ cfdj = msg.getData().getQLFQLCFDJ();
							cfdj = packageXml.getQLFQLCFDJ(cfdj,null, h, ql, fsql, ywh,null);
							
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
							
							List<DJFDJFZ> fz = packageXml.getDJFDJFZ(h,ywh,this.getXMBH());
							msg.getData().setDJFZ(fz);	
							List<DJFDJGD> gd = packageXml.getDJFDJGD(h, ywh,this.getXMBH());
							msg.getData().setDJGD(gd);	
							List<DJFDJSQR> djsqrs = msg.getData().getDJSQR();
							//查封现在的不填写申请人，为了和接入规范一致，先在这里拼凑出申请人。
							DJFDJSQR djsqr = new DJFDJSQR();
							djsqr.setYsdm("2004020000");
							djsqr.setQlrmc(cfdj == null ? "无" :StringHelper.formatDefaultValue(cfdj.getCfjg()));
							djsqr.setYwh(ywh);
							//djsqr.setQxdm(ConfigHelper.getNameByValue("XZQHDM"));
							djsqrs.add(djsqr);
							djsqrs = packageXml.getDJSQRs(djsqrs, sqrs, h.getYSDM(), ywh, h.getBDCDYH());
							msg.getData().setDJSQR(djsqrs);
							
							FJF100 fj = msg.getData().getFJF100();
							fj = packageXml.getFJF(fj);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					mashaller.marshal(msg, new File(path + "Biz" + msg.getHead().getBizMsgID() + ".xml"));
					result = uploadFile(path + "Biz" + msg.getHead().getBizMsgID() + ".xml",ConstValue.RECCODE.CF_CFDJ.Value,actinstID,djdy.getDJDYID(),ql.getId());
					names.put(djdy.getDJDYID(), msg.getHead().getBizMsgID()+ ".xml");
					
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
