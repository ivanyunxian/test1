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
import com.supermap.realestate.registration.dataExchange.utils.packageXml;
import com.supermap.realestate.registration.handler.DJHandler;
import com.supermap.realestate.registration.model.BDCS_C_GZ;
import com.supermap.realestate.registration.model.BDCS_DJDY_GZ;
import com.supermap.realestate.registration.model.BDCS_DYBG;
import com.supermap.realestate.registration.model.BDCS_DYXZ;
import com.supermap.realestate.registration.model.BDCS_FSQL_LS;
import com.supermap.realestate.registration.model.BDCS_FSQL_XZ;
import com.supermap.realestate.registration.model.BDCS_H_GZ;
import com.supermap.realestate.registration.model.BDCS_H_LS;
import com.supermap.realestate.registration.model.BDCS_LJZ_XZ;
import com.supermap.realestate.registration.model.BDCS_QDZR_LS;
import com.supermap.realestate.registration.model.BDCS_QDZR_XZ;
import com.supermap.realestate.registration.model.BDCS_QLR_GZ;
import com.supermap.realestate.registration.model.BDCS_QL_GZ;
import com.supermap.realestate.registration.model.BDCS_QL_LS;
import com.supermap.realestate.registration.model.BDCS_QL_XZ;
import com.supermap.realestate.registration.model.BDCS_SHYQZD_GZ;
import com.supermap.realestate.registration.model.BDCS_SHYQZD_XZ;
import com.supermap.realestate.registration.model.BDCS_SQR;
import com.supermap.realestate.registration.model.BDCS_XMXX;
import com.supermap.realestate.registration.model.BDCS_ZRZ_XZ;
import com.supermap.realestate.registration.model.interfaces.House;
import com.supermap.realestate.registration.model.interfaces.RealUnit;
import com.supermap.realestate.registration.model.interfaces.Rights;
import com.supermap.realestate.registration.model.interfaces.RightsHolder;
import com.supermap.realestate.registration.model.interfaces.SubRights;
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
import com.supermap.realestate.registration.util.Global;
import com.supermap.realestate.registration.util.ObjectHelper;
import com.supermap.realestate.registration.util.ProjectHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.realestate.registration.util.YwLogUtil;
import com.supermap.wisdombusiness.core.SuperSpringContext;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;

/*	
 * 数据来源：从权籍系统中权籍调查中来，
 * 主要针对地与房都发生变化的，也满足基本流程
 * 1:1(单独的房屋信息跟宗地的信息变化的),1：N(分割),N:1(合并)  
 1、国有建设用地使用权/房屋所有权变更登记
 */
/**
 * 变更登记扩展处理类----单元分割/拷贝所以权利值分割后单元
 * @ClassName: BGDJ_DYFGHandler
 * @author ranq
 * @date 2017年5月22日
 */
public class BGDJ_DYFGHandler extends DJHandlerBase implements DJHandler {

	/**
	 * 构造函数
	 * 
	 * @param info
	 */
	public BGDJ_DYFGHandler(ProjectInfo info) {
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
					dao.save(djdy);
				} else {
					BDCS_DJDY_GZ gzdjdy = super.createDJDYfromGZ(dy);
					gzdjdy.setLY(DJDYLY.XZ.Value);
					if (gzdjdy != null) {
						dao.save(gzdjdy);
					}
				}
			} else {
				RealUnit dy_dc = UnitTools.loadUnit(this.getBdcdylx(),
						DJDYLY.DC, bdcdyid);
				if (!StringHelper.isEmpty(dy_dc)) {
					dy_dc.setDJZT(DJZT.DJZ.Value);
					dao.save(dy_dc);
					dy = UnitTools.copyUnit(dy_dc, this.getBdcdylx(), DJDYLY.GZ);
					if (!StringHelper.isEmpty(dy)) {
						dy.setXMBH(this.getXMBH());
					}
				}
				// 登记单元索引表
				BDCS_DJDY_GZ djdy = super.createDJDYfromGZ(dy);
				if (djdy != null) {
					dao.save(djdy);
				}
			}
			dao.save(dy);
			dao.flush();
		}
		return true;
	}

	/**
	 * 写入登记簿
	 */
	@Override
	public boolean writeDJB() {
		
		if (super.isCForCFING()) {
			return false;
		}
		String strSqlXMBH = ProjectHelper.GetXMBHCondition(getXMBH());
		List<BDCS_DJDY_GZ> djdys = getCommonDao().getDataList(BDCS_DJDY_GZ.class, strSqlXMBH +" ORDER BY LY");
		List<String> listBGQ = new ArrayList<String>();
		List<String> listBGH = new ArrayList<String>();
		if (djdys != null && djdys.size() > 0) {
			for (BDCS_DJDY_GZ djdy : djdys) {
				
				String djdyid = djdy.getDJDYID();
				String bdcdyid = djdy.getBDCDYID();
				
				StringBuilder builder = new StringBuilder();
				builder.append(djdyid).append(";").append(bdcdyid);
				String strDYID = builder.toString();
				
				String ly = djdy.getLY();
				BDCDYLX lx = BDCDYLX.initFrom(djdy.getBDCDYLX());
				if(DJDYLY.GZ.Value.equals(ly)){
					if (!listBGH.contains(strDYID)) {
						listBGH.add(strDYID);
					}
					super.CopyGZDYToXZAndLS(bdcdyid);
					RealUnit dy = UnitTools.loadUnit(this.getBdcdylx(), DJDYLY.DC, bdcdyid);
					if(!StringHelper.isEmpty(dy))
					{
						dy.setDJZT(DJZT.YDJ.Value);
						getCommonDao().save(dy);
					}
					super.CopyGZDJDYToXZAndLS(djdy.getId());
					//BG045
					if ("YCH".equals(lx)) {
						updateZRZ();
					}					
				}else if (DJDYLY.XZ.Value.equals(ly)) {
					if (!listBGQ.contains(strDYID)) {
						listBGQ.add(strDYID);
					}
					super.removeDJDYFromXZ(djdyid);
					UnitTools.deleteUnit(lx, DJDYLY.XZ, bdcdyid);
				}
			}
		}
		
		if (listBGQ != null && listBGQ.size() > 0 && listBGH != null && listBGH.size() > 0) {
			for (int ibgq = 0; ibgq < listBGQ.size(); ibgq++) {
				for (int ibgh = 0; ibgh < listBGH.size(); ibgh++) {
					String BGQDYID = listBGQ.get(ibgq);//老单元的ID 
					String BGHDYID = listBGH.get(ibgh);//新增单元的ID-在单元变更中放到lbdcdyid中
					//builder.append(djdyid).append(";").append(bdcdyid);
					String bgqdjdyid = BGQDYID.split(";")[0];
					String bgqbdcdyid = BGQDYID.split(";")[1];
					
					String bghdjdyid = BGHDYID.split(";")[0];
					String bghbdcdyid = BGHDYID.split(";")[1];
					
					String bDCDYH = "";	
					
					/*List<BDCS_H_XZY> bDCDYHs = getCommonDao().getDataList(BDCS_H_XZY.class, " bdcdyid='"+ bgqbdcdyid +"'");
					if(bDCDYHs!=null&&bDCDYHs.size()>0){
						bDCDYH= bDCDYHs.get(0).getBDCDYH();
					}*/
					
					RealUnit unit = UnitTools.loadUnit(this.getBdcdylx(), DJDYLY.XZ, bgqbdcdyid);
					if(unit != null){
						bDCDYH= unit.getBDCDYH();
					}
					
					List<BDCS_QL_XZ> qls = getCommonDao().getDataList(BDCS_QL_XZ.class, " DJDYID='"+ bgqdjdyid +"'");
					if(qls!=null&&qls.size()>0){
						for (BDCS_QL_XZ bdcs_QL_XZ : qls) {
							String qlid =super.getPrimaryKey();
							String fsqlid =super.getPrimaryKey();
							BDCS_QL_XZ qlxz = new BDCS_QL_XZ();
							ObjectHelper.copyObject(bdcs_QL_XZ, qlxz);
							qlxz.setId(qlid);
							qlxz.setFSQLID(fsqlid);
							qlxz.setDJDYID(bghdjdyid);
							qlxz.setDBR(Global.getCurrentUserName());
							qlxz.setDJSJ(new Date());
							getCommonDao().save(qlxz);
							BDCS_QL_LS qlls = new BDCS_QL_LS();
							ObjectHelper.copyObject(qlxz, qlls);
							getCommonDao().save(qlls);
							
							List<BDCS_FSQL_XZ> FSXZ = getCommonDao().getDataList(BDCS_FSQL_XZ.class, " QLID='" + bdcs_QL_XZ.getId() + "'");
							if(FSXZ!=null&&FSXZ.size()>0){
								BDCS_FSQL_XZ fsqlxz = new BDCS_FSQL_XZ();
								ObjectHelper.copyObject(FSXZ.get(0), fsqlxz);
								fsqlxz.setQLID(qlid);
								fsqlxz.setId(fsqlid);
								fsqlxz.setDJDYID(bghdjdyid);
								getCommonDao().save(fsqlxz);
								BDCS_FSQL_LS fsqlls = new BDCS_FSQL_LS();
								ObjectHelper.copyObject(fsqlxz, fsqlls);
								getCommonDao().save(fsqlls);
							}
							
							String qdzrid =super.getPrimaryKey();
							
							List<BDCS_QDZR_XZ> qdzrs = getCommonDao().getDataList(BDCS_QDZR_XZ.class, " DJDYID='"+ bgqdjdyid +"' AND QLID='" + bdcs_QL_XZ.getId() + "'");
							for (BDCS_QDZR_XZ bdcs_QDZR_XZ : qdzrs) {
								BDCS_QDZR_XZ qlzr = new BDCS_QDZR_XZ();
								ObjectHelper.copyObject(bdcs_QDZR_XZ, qlzr);
								qlzr.setId(qdzrid);
								qlzr.setBDCDYH(bDCDYH);
								qlzr.setDJDYID(bghdjdyid);
								qlzr.setQLID(qlid);
								qlzr.setFSQLID(fsqlid);
								getCommonDao().save(qlzr);
								BDCS_QDZR_LS qlzrls = new BDCS_QDZR_LS();
								ObjectHelper.copyObject(qlzr, qlzrls);
								getCommonDao().save(qlzrls);
							}
						}
					}
					List<BDCS_DYXZ> xz = getCommonDao().getDataList(BDCS_DYXZ.class, " bdcdyid='" + bgqbdcdyid + "'");
					if(xz!=null&&xz.size()>0){
						for (BDCS_DYXZ bdcs_DYXZ : xz) {
							BDCS_DYXZ xzxz = new BDCS_DYXZ();
							ObjectHelper.copyObject(bdcs_DYXZ, xzxz);
							xzxz.setBDCDYH(bDCDYH);
							xzxz.setBDCDYID(bghdjdyid);
							getCommonDao().update(xzxz);
						}
					}
					Date time = new Date();
					RebuildDYBG(BGQDYID.split(";")[1], BGQDYID.split(";")[0], BGHDYID.split(";")[1], BGHDYID.split(";")[0], time, null);
				}
			}
			for (int ibgq = 0; ibgq < listBGQ.size(); ibgq++) {
				String djdyid = listBGQ.get(ibgq).split(";")[0];
				getCommonDao().deleteEntitysByHql(BDCS_QL_XZ.class, " djdyid='"+djdyid+"'");
			}
		}
		this.SetSFDB();
		getCommonDao().flush();
		return true;
	}
	
	public void updateZRZ()
	{
		CommonDao dao=getCommonDao();
		String strSqlXMBH = ProjectHelper.GetXMBHCondition(getXMBH());
		//获取变更后的信息
		List<BDCS_DJDY_GZ> djdys=dao.getDataList(BDCS_DJDY_GZ.class, strSqlXMBH +" and LY ='"+DJDYLY.GZ.Value+"'");
		if(!StringHelper.isEmpty(djdys) && djdys.size()>0)
		{
			BDCS_DJDY_GZ bdcs_djdy_gz=djdys.get(0);
			//获取变更后户信息
		    RealUnit unit =UnitTools.loadUnit(BDCDYLX.YCH, DJDYLY.GZ, bdcs_djdy_gz.getBDCDYID());
		    if(!StringHelper.isEmpty(unit))
		    {
		    	House h=(House) unit;
		    	//获取变更后户对应的自然幢信息
		    	List<RealUnit> zrz=UnitTools.loadUnits(BDCDYLX.ZRZ, DJDYLY.GZ, strSqlXMBH+ " and bdcdyid ='"+h.getZRZBDCDYID()+"'");
		    	//若不存在，则自然幢信息修改，则需要修改自然幢信息
		    	if(StringHelper.isEmpty(zrz))
		    	{
		    		RealUnit zrz_xz= UnitTools.copyUnit(zrz.get(0), BDCDYLX.ZRZ, DJDYLY.XZ);
		    		RealUnit zrz_ls= UnitTools.copyUnit(zrz.get(0), BDCDYLX.ZRZ, DJDYLY.LS);
		    		RealUnit zrz_dc=UnitTools.loadUnit(BDCDYLX.ZRZ, DJDYLY.DC, zrz.get(0).getId());
		    		zrz_dc.setDJZT(DJZT.YDJ.Value);
		    		dao.update(zrz_dc);
		    		dao.save(zrz_xz);
		    		dao.save(zrz_ls);
		    		super.CopyGeo(zrz.get(0).getId(), BDCDYLX.ZRZ,DJDYLY.GZ);
		    	}
		    }
		}
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
			// 移除具体单元
			UnitTools.deleteUnit(BDCDYLX.initFrom(djdy.getBDCDYLX()), DJDYLY.GZ, bdcdyid);
			
			// 删除权利、附属权利、权利人、证书、权地证人关系
			String _hqlCondition = MessageFormat.format("XMBH=''{0}'' AND DJDYID=''{1}''", getXMBH(), djdyid);
			RightsTools.deleteRightsAllByCondition(DJDYLY.initFrom(ly), _hqlCondition);
			if (ly.equals(DJDYLY.GZ.Value)) {
				// 更新调查库相应单元状态
				updateDCDYStatus(bdcdylx, bdcdyid);
			} else{
				//根据权利移除拷贝单元带过来的sqr
				List<Rights> qls = RightsTools.loadRightsByCondition(DJDYLY.GZ, _hqlCondition);
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
							BDCS_SHYQZD_GZ zd = dao.get(BDCS_SHYQZD_GZ.class, djdy.getBDCDYID());
//							String preEstateNum = "";
							// 目前未维护的字段先手动赋值 diaoliwei 2015-10-15
							if (null != zd) {
								if (StringUtils.isEmpty(zd.getZDT())) {
									zd.setZDT("无");
								}
							}
//							for (int j = 0; j < bgqdjdys.size(); j++) {
//								BDCS_DJDY_GZ bgqdjdy = bgqdjdys.get(j);
//								BDCS_SHYQZD_XZ zd_XZ = dao.get(BDCS_SHYQZD_XZ.class, bgqdjdy.getBDCDYID());
//								if (zd_XZ != null && (j == 0 || j == bgqdjdys.size() - 1)) {
//									preEstateNum += zd_XZ.getBDCDYH();
//								} else {
//									preEstateNum += zd_XZ.getBDCDYH() + ",";
//								}
//							}
							// 标记单元号是否发生变化了，是否取消空间节点的存在
//							boolean flag = false;
//							if (!(zd.getBDCDYH().equals(preEstateNum)) && !StringUtils.isEmpty(preEstateNum)) {
//								flag = true;
//							}
							Message msg = exchangeFactory.createMessage(true);

							super.fillHead(msg, i, ywh,zd.getBDCDYH(),zd.getQXDM(),ql.getLYQLID());

							BDCS_DYBG dybg = packageXml.getDYBG(zd.getId());
							msg.getHead().setParcelID(StringHelper.formatObject(zd.getZDDM()));
							msg.getHead().setEstateNum(StringHelper.formatObject(zd.getBDCDYH()));// 当前的不动产单元号
//							msg.getHead().setPreEstateNum(preEstateNum);// 上一首的不动产单元号
							if (zd != null && !StringUtils.isEmpty(zd.getQXDM())) {
								msg.getHead().setAreaCode(zd.getQXDM());
							}

							if (djdy != null) {

								List<ZTTGYQLR> zttqlr = msg.getData().getGYQLR();
								zttqlr = packageXml.getZTTGYQLRs(zttqlr, qlrs, zd, ql, null, null, null, null);
								msg.getData().setGYQLR(zttqlr);

								KTTZDJBXX jbxx = msg.getData().getKTTZDJBXX();
								jbxx = packageXml.getZDJBXX(jbxx, zd, ql, null, null);

								String zdzhdm = "";
								if (zd != null) {
									zdzhdm = zd.getZDDM();
								}

								KTFZDBHQK bhqk = msg.getData().getZDBHQK();
								bhqk = packageXml.getKTFZDBHQK(bhqk, zd, ql, null, null);
								msg.getData().setZDBHQK(bhqk);

								KTTGYJZX jzx = msg.getData().getKTTGYJZX();
								jzx = packageXml.getKTTGYJZX(jzx, zdzhdm);

								KTTGYJZD jzd = msg.getData().getKTTGYJZD();
								jzd = packageXml.getKTTGYJZD(jzd, zdzhdm);

								QLFQLJSYDSYQ syq = msg.getData().getQLJSYDSYQ();
								syq = packageXml.getQLFQLJSYDSYQ(syq, zd, ql, ywh);

								DJTDJSLSQ sq = msg.getData().getDJSLSQ();
								sq = packageXml.getDJTDJSLSQ(sq, ywh, zd, ql, xmxx, null, xmxx.getSLSJ(), null, null, null);

								List<DJFDJSJ> sj = msg.getData().getDJSJ();
								sj = packageXml.getDJFDJSJ(zd, ywh,actinstID);
								msg.getData().setDJSJ(sj);

								List<DJFDJSF> sfList = msg.getData().getDJSF();
								sfList = packageXml.getDJSF( zd,ywh, this.getXMBH());
								msg.getData().setDJSF(sfList);

								List<DJFDJSH> sh = msg.getData().getDJSH();
								sh = packageXml.getDJFDJSH(zd, ywh, this.getXMBH(), actinstID);
								msg.getData().setDJSH(sh);

								List<DJFDJSZ> sz = packageXml.getDJFDJSZ(zd, ywh, this.getXMBH());
								msg.getData().setDJSZ(sz);

								List<DJFDJFZ> fz = packageXml.getDJFDJFZ(zd, ywh, this.getXMBH());
								msg.getData().setDJFZ(fz);
								List<DJFDJGD> gd = packageXml.getDJFDJGD( zd, ywh,this.getXMBH());
								msg.getData().setDJGD(gd);
								
								BDCS_SHYQZD_GZ zd2 = null;
								if (null != dybg) {
									zd2 = new BDCS_SHYQZD_GZ();
									zd2.setId(dybg.getLBDCDYID());
//								zd.setId(dybg.getLBDCDYID());
								}
								// 如果当前不动产单元号和上一个不动产单元号不同的话，把空间节点加进去
//								if (!(preEstateNum.equals(zd.getBDCDYH()))) {
									List<ZDK103> zdk = msg.getData().getZDK103();
									zdk = packageXml.getZDK103(zdk, zd2, null, null);
									msg.getData().setZDK103(zdk);
//								}
								List<DJFDJSQR> djsqrs = msg.getData().getDJSQR();
								djsqrs = packageXml.getDJSQRs(djsqrs, sqrs, zd.getYSDM(), ywh, zd.getBDCDYH());
								msg.getData().setDJSQR(djsqrs);

								FJF100 fj = msg.getData().getFJF100();
								fj = packageXml.getFJF(fj);
							}
							msgName = "Biz" + msg.getHead().getBizMsgID() + ".xml";
							mashaller.marshal(msg, new File(path + msgName));
							result = uploadFile(path + msgName, ConstValue.RECCODE.JSYDSHYQ_BGDJ.Value,actinstID,djdy.getDJDYID(),ql.getId());
							names.put(djdy.getDJDYID(), msg.getHead().getBizMsgID() + ".xml");
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					if (QLLX.GYJSYDSHYQ_FWSYQ.Value.equals(this.getQllx().Value)) { // 房屋所有权
						try {
							BDCS_H_GZ h = dao.get(BDCS_H_GZ.class, djdy.getBDCDYID());
							BDCS_ZRZ_XZ zrz_gz = null;
							if (h != null && !StringUtils.isEmpty(h.getZRZBDCDYID())) {
								zrz_gz = dao.get(BDCS_ZRZ_XZ.class, h.getZRZBDCDYID());
								if(zrz_gz != null){
									zrz_gz.setGHYT(h.getGHYT()); //自然幢的ghyt取户的ghyt
									zrz_gz.setFWJG(zrz_gz.getFWJG() == null || zrz_gz.getFWJG().equals("") ? h.getFWJG() : zrz_gz.getFWJG());
								}
							}
							BDCS_LJZ_XZ ljz_gz = null;
							if (h != null && !StringUtils.isEmpty(h.getLJZID())) {
								ljz_gz = dao.get(BDCS_LJZ_XZ.class, h.getLJZID());
							}
							if (h != null) {
								BDCS_SHYQZD_XZ zd = dao.get(BDCS_SHYQZD_XZ.class, h.getZDBDCDYID());
								h.setZDDM(zd.getZDDM());
								if(zrz_gz != null){
									zrz_gz.setZDDM(zd.getZDDM());
								}
							}
							Message msg = exchangeFactory.createMessageByFWSYQ();
							super.fillHead(msg, i, ywh,h.getBDCDYH(),h.getQXDM(),ql.getLYQLID());
							msg.getHead().setParcelID(StringHelper.formatObject(h.getZDDM()));
							msg.getHead().setEstateNum(StringHelper.formatObject(h.getBDCDYH()));
							if (h != null && !StringUtils.isEmpty(h.getQXDM())) {
								msg.getHead().setAreaCode(h.getQXDM());
							}
//							String preEstateNum = "";
//							for (BDCS_DJDY_GZ bgqdjdy : bgqdjdys) {
//								BDCS_H_LS h_LS = dao.get(BDCS_H_LS.class, bgqdjdy.getBDCDYID());
//								if (!StringUtils.isEmpty(h.getId()) && h.getId().equals(h_LS.getId())) {
//									preEstateNum = h_LS.getBDCDYH();
//									break;
//								}
//							}
//							msg.getHead().setPreEstateNum(preEstateNum);
							BDCS_C_GZ c = null;
							if (h != null && !StringUtils.isEmpty(h.getCID())) {
								c = dao.get(BDCS_C_GZ.class, h.getCID());
							}

							if (djdy != null) {
								List<ZTTGYQLR> zttqlr = msg.getData().getGYQLR();
								zttqlr = packageXml.getZTTGYQLRs(zttqlr, qlrs, null, ql, h, null, null, null);
								msg.getData().setGYQLR(zttqlr);

								KTTFWZRZ zrz = msg.getData().getKTTFWZRZ();
								zrz = packageXml.getKTTFWZRZ(zrz, zrz_gz);

								KTTFWLJZ ljz = msg.getData().getKTTFWLJZ();
								ljz = packageXml.getKTTFWLJZ(ljz, ljz_gz,h);

								KTTFWC kttc = msg.getData().getKTTFWC();
								kttc = packageXml.getKTTFWC(kttc, c, zrz);
								msg.getData().setKTTFWC(kttc);

								KTTFWH fwh = msg.getData().getKTTFWH();
								fwh = packageXml.getKTTFWH(fwh, h);

								QLTFWFDCQYZ fdcqyz = msg.getData().getQLTFWFDCQYZ();
								fdcqyz = packageXml.getQLTFWFDCQYZ(fdcqyz, h, ql, ywh);

								List<ZDK103> fwk = msg.getData().getZDK103();
								fwk = packageXml.getZDK103H(fwk, h, zrz_gz);

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

								List<DJFDJSZ> sz = packageXml.getDJFDJSZ(h ,ywh, this.getXMBH());
								msg.getData().setDJSZ(sz);
								List<DJFDJFZ> fz = packageXml.getDJFDJFZ(h, ywh, this.getXMBH());
								msg.getData().setDJFZ(fz);
								List<DJFDJGD> gd = packageXml.getDJFDJGD(h, ywh,this.getXMBH());
								msg.getData().setDJGD(gd);

								List<DJFDJSQR> djsqrs = msg.getData().getDJSQR();
								djsqrs = packageXml.getDJSQRs(djsqrs, sqrs, h.getYSDM(), ywh, h.getBDCDYH());
								msg.getData().setDJSQR(djsqrs);

								FJF100 fj = msg.getData().getFJF100();
								fj = packageXml.getFJF(fj);
							}
							msgName = "Biz" + msg.getHead().getBizMsgID() + ".xml";
							mashaller.marshal(msg, new File(path + msgName));
							result = uploadFile(path + msgName, ConstValue.RECCODE.FDCQDZ_BGDJ.Value,actinstID,djdy.getDJDYID(),ql.getId());
							names.put(djdy.getDJDYID(), msg.getHead().getBizMsgID() + ".xml");
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					if(null == result){
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

