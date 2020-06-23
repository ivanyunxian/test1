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
import com.supermap.realestate.registration.model.BDCS_DJDY_XZ;
import com.supermap.realestate.registration.model.BDCS_FSQL_GZ;
import com.supermap.realestate.registration.model.BDCS_FSQL_LS;
import com.supermap.realestate.registration.model.BDCS_FSQL_XZ;
import com.supermap.realestate.registration.model.BDCS_H_XZ;
import com.supermap.realestate.registration.model.BDCS_H_XZY;
import com.supermap.realestate.registration.model.BDCS_QL_GZ;
import com.supermap.realestate.registration.model.BDCS_QL_LS;
import com.supermap.realestate.registration.model.BDCS_QL_XZ;
import com.supermap.realestate.registration.model.BDCS_SHYQZD_XZ;
import com.supermap.realestate.registration.model.BDCS_SQR;
import com.supermap.realestate.registration.model.BDCS_XMXX;
import com.supermap.realestate.registration.model.interfaces.House;
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
import com.supermap.realestate.registration.util.ConstValue.DJDYLY;
import com.supermap.realestate.registration.util.Global;
import com.supermap.realestate.registration.util.ObjectHelper;
import com.supermap.realestate.registration.util.ProjectHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.realestate.registration.util.YwLogUtil;
import com.supermap.wisdombusiness.core.SuperHelper;
import com.supermap.wisdombusiness.core.SuperSpringContext;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;

/*
 1、国有建设用地使用权/房屋所有权解封登记
 2、集体建设用地使用权/房屋所有权解封登记（未配置）
 3、宅基地使用权/房屋所有权解封登记（未配置）
 */

/**
 * 房屋解封登记处理类
 * @ClassName: CFDJ_ZX_HouseHandler
 * @author liushufeng
 * @date 2015年9月8日 下午10:22:41
 */
public class CFDJ_ZX_HouseHandler extends DJHandlerBase implements DJHandler {

	public CFDJ_ZX_HouseHandler(ProjectInfo info) {
		super(info);
	}

	@Override
	public boolean addBDCDY(String qlid) {
		boolean bSuccess = true;

		if (!StringHelper.isEmpty(qlid)) {
			String[] qlids = qlid.split(",");
			for (int i = 0; i < qlids.length; i++) {
				String id = qlids[i];
				if (!StringHelper.isEmpty(id))
					if (bSuccess) {
						bSuccess = addbdcdy(id);
					}
			}
		} else {
			bSuccess = false;
			setErrMessage("请选择一条查封信息");
		}
		return bSuccess;
	}

	private boolean addbdcdy(String qlid) {
		boolean bSuccess = false;
		CommonDao dao = this.getCommonDao();
		BDCS_QL_XZ xz_ql = getQLXZbyQLID(dao, qlid);
		BDCS_FSQL_XZ xz_fsql = getFSQLXZbyQLID(dao, qlid);
		BDCS_DJDY_XZ xz_djdy = getDJDYXZbyDJDYID(dao, xz_ql.getDJDYID());

		if (xz_djdy != null) {
			if (ValidateDup(dao, xz_djdy.getBDCDYID()))// 严重是否重复插入
				return true;

			// 现状库拷贝到工作库
			BDCS_QL_GZ gz_ql = new BDCS_QL_GZ();
			BDCS_FSQL_GZ gz_fsql = new BDCS_FSQL_GZ();
			BDCS_DJDY_GZ gz_djdy = new BDCS_DJDY_GZ();

			ObjectHelper.copyObject(xz_ql, gz_ql);
			ObjectHelper.copyObject(xz_fsql, gz_fsql);

			gz_ql.setDJLX(super.getDjlx().Value);
			gz_ql.setQLLX(super.getQllx().Value);
			gz_ql.setXMBH(super.getXMBH());
			gz_ql.setLYQLID(qlid);
			gz_ql.setCASENUM("");

			// 工作库附属权力信息重新赋值，参考super.createFSQL写法
			gz_fsql.setXMBH(super.getXMBH());
			gz_fsql.setZXDYYWH(super.getProject_id());
			gz_fsql.setJFWJ(ConfigHelper.getNameByValue("DEFAULTCFWJ"));
			String newqlid = SuperHelper.GeneratePrimaryKey();
			String newfsqlid = SuperHelper.GeneratePrimaryKey();
			gz_ql.setId(newqlid);
			gz_ql.setFSQLID(newfsqlid);
			gz_fsql.setQLID(newqlid);
			gz_fsql.setId(newfsqlid);
			if ("1".equals(ConfigHelper.getNameByValue("jcfj"))){
				gz_fsql.setZXFJ(gz_ql.getFJ());
			}

			// 工作库登记单元信息重新赋值，参考super.createDJDYfromXZ写法
			gz_djdy = new BDCS_DJDY_GZ();
			gz_djdy.setXMBH(super.getXMBH());
			gz_djdy.setDJDYID(xz_djdy.getDJDYID());
			gz_djdy.setBDCDYID(xz_djdy.getBDCDYID());
			gz_djdy.setBDCDYLX(super.getBdcdylx().Value);
			gz_djdy.setBDCDYH(xz_djdy.getBDCDYH());
			gz_djdy.setLY(DJDYLY.XZ.Value);
//			if (!"1".equals(ConfigHelper.getNameByValue("jcfj"))){
//				gz_ql.setFJ("");
//			}

			// 保存
			dao.save(gz_djdy);
			dao.save(gz_ql);
			dao.save(gz_fsql);
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

	// 根据权力ID返回现状库权力
	private BDCS_QL_XZ getQLXZbyQLID(CommonDao dao, String qlid) {
		String hql = MessageFormat.format(" QLID=''{0}''", qlid);
		List<BDCS_QL_XZ> list = dao.getDataList(BDCS_QL_XZ.class, hql);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return new BDCS_QL_XZ();
	}

	// 根据权力ID返回现状库附属权力
	private BDCS_FSQL_XZ getFSQLXZbyQLID(CommonDao dao, String qlid) {
		String hql = MessageFormat.format(" QLID=''{0}''", qlid);
		List<BDCS_FSQL_XZ> list = dao.getDataList(BDCS_FSQL_XZ.class, hql);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	// 根据权力ID返回现状库登记单元
	private BDCS_DJDY_XZ getDJDYXZbyDJDYID(CommonDao dao, String djdyid) {
		String hql = MessageFormat.format(" DJDYID=''{0}''", djdyid);
		List<BDCS_DJDY_XZ> list_djdy = dao.getDataList(BDCS_DJDY_XZ.class, hql);
		if (list_djdy != null && list_djdy.size() > 0) {
			return list_djdy.get(0);// 一个登记ID对应多个不动产单元的先不考虑
		}
		return null;
	}

	@Override
	public boolean writeDJB() {
		String dbr = Global.getCurrentUserName();
		String strSqlXMBH = ProjectHelper.GetXMBHCondition(getXMBH());
		List<BDCS_DJDY_GZ> djdys = getCommonDao().getDataList(BDCS_DJDY_GZ.class, strSqlXMBH);
		if (djdys != null && djdys.size() > 0) {
			for (int idjdy = 0; idjdy < djdys.size(); idjdy++) {
				BDCS_DJDY_GZ bdcs_djdy_gz = djdys.get(idjdy);
				if (bdcs_djdy_gz != null) {
					String djdyid = bdcs_djdy_gz.getDJDYID();
					BDCS_QL_GZ bdcs_ql_gz = null;
					// 获取工作库权利附属表
					String djlxFilter = String.format("  DJDYID='%s' AND DJLX='800' ORDER BY DJSJ ASC ", djdyid);
					List<BDCS_QL_GZ> bdcs_ql_gzs = getCommonDao().getDataList(BDCS_QL_GZ.class, strSqlXMBH + " and " + djlxFilter);
					if (bdcs_ql_gzs != null && bdcs_ql_gzs.size() > 0) {
						bdcs_ql_gz = bdcs_ql_gzs.get(0);
					}

					BDCS_FSQL_GZ gzfsql = getCommonDao().get(BDCS_FSQL_GZ.class, bdcs_ql_gz.getFSQLID());
					gzfsql.setZXDYYWH(super.getProject_id());
					gzfsql.setZXDBR(dbr);
					gzfsql.setZXSJ(new Date());
					gzfsql.setJFWJ(ConfigHelper.getNameByValue("DEFAULTCFWJ"));
					getCommonDao().update(gzfsql);

					String xzqlid = bdcs_ql_gz.getLYQLID();
					BDCS_QL_XZ xzql = getCommonDao().get(BDCS_QL_XZ.class, xzqlid);
					if (xzql != null) {
						if (!StringHelper.isEmpty(xzql.getFSQLID())) {
							BDCS_FSQL_XZ xzfsql = getCommonDao().get(BDCS_FSQL_XZ.class, xzql.getFSQLID());
							if (xzfsql != null) {
								getCommonDao().deleteEntity(xzfsql);//删除现状附属权利的查封信息
							}
						}
						getCommonDao().deleteEntity(xzql);//删除现状权利的查封信息
					}

					BDCS_QL_LS lsql = getCommonDao().get(BDCS_QL_LS.class, xzqlid);
					if (lsql != null && !StringHelper.isEmpty(lsql.getFSQLID())) {
						BDCS_FSQL_LS lsfsql = getCommonDao().get(BDCS_FSQL_LS.class, lsql.getFSQLID());
						if (lsfsql != null) {
							lsfsql.setZXDYYWH(gzfsql.getZXDYYWH());
							lsfsql.setJFJG(gzfsql.getJFJG());
							lsfsql.setJFWJ(gzfsql.getJFWJ());
							lsfsql.setJFWH(gzfsql.getJFWH());
							lsfsql.setZXDBR(gzfsql.getZXDBR());
							lsfsql.setZXSJ(gzfsql.getZXSJ());
							lsfsql.setZXFJ(gzfsql.getZXFJ());
							getCommonDao().update(lsfsql);
							getCommonDao().update(lsql);
						}
					}

					// // 只将解封的工作层的权利表、权利附属表同步到现状层和历史层中。
					// this.CopyGZQLToXZAndLS(djdyid);
					// // 删除掉现状层、历史层中的查封信息。确保只保留一条解封信息（该信息保留有查封信息）
					// if (bdcs_ql_gz != null)
					// unLock(bdcs_ql_gz);
					// // 更新项目状态
					this.SetXMCFZT(djdyid, "01");
				}
			}
		}
		this.SetSFDB();
		getCommonDao().flush();
		return true;
	}

	/**
	 * 拷贝工作库的权利表，权利附属表到现状库和历史库中。比较特殊是，这里的登薄人，登薄时间是保存在附属权利表的注销人，注销时间
	 * 里作为解封人，解封时间使用。登记机构是放在权利附属表的解封机构里面 参考SUPER的CopyGZQLToXZAndLS写法
	 * 
	 * @作者：WUZ
	 * @param djdyid
	 * @param qlid
	 * @return
	 */
	protected boolean CopyGZQLToXZAndLS(String djdyid) {
		StringBuilder builderDJDYID = new StringBuilder();
		builderDJDYID.append(" DJDYID='").append(djdyid).append("'");
		String strSqlDJDYID = builderDJDYID.toString();
		String xmbhFilter = ProjectHelper.GetXMBHCondition(this.getXMBH());
		List<BDCS_QL_GZ> qls = getCommonDao().getDataList(BDCS_QL_GZ.class, xmbhFilter + " and " + strSqlDJDYID);
		if (qls != null && qls.size() > 0) {
			for (int iql = 0; iql < qls.size(); iql++) {
				BDCS_QL_GZ bdcs_ql_gz = qls.get(iql);
				// 拷贝权利
				BDCS_QL_XZ bdcs_ql_xz = ObjectHelper.copyQL_GZToXZ(bdcs_ql_gz);
				getCommonDao().save(bdcs_ql_xz);

			}
		}
		return true;
	}

	/**
	 * 移除现状层中的查封登记权利 参考SUPER的removeQLFromXZByALL写法
	 * 
	 * @作者：WUZ
	 * @param djdyid
	 * @param qlid
	 * @return
	 */
	protected boolean removeQLFromXZByCFDJ(String qlid) {
		StringBuilder builderDJDYID = new StringBuilder();
		builderDJDYID.append(" QLID='").append(qlid).append("'");
		;
		String strSqlDJDYID = builderDJDYID.toString();
		List<BDCS_QL_XZ> qls = getCommonDao().getDataList(BDCS_QL_XZ.class, strSqlDJDYID);
		if (qls != null && qls.size() > 0) {
			for (int iql = 0; iql < qls.size(); iql++) {
				BDCS_QL_XZ bdcs_ql_xz = qls.get(iql);
				if (bdcs_ql_xz != null) {
					BDCS_FSQL_XZ bdcs_fsql_xz = getCommonDao().get(BDCS_FSQL_XZ.class, bdcs_ql_xz.getFSQLID());
					if (bdcs_fsql_xz != null) {
						getCommonDao().deleteEntity(bdcs_fsql_xz);
					}
					getCommonDao().deleteEntity(bdcs_ql_xz);
				}
			}
		}
		return true;
	}

	/**
	 * 移除历史层中的查封登记权利，在现在层中只保留合并后的解封信息 参考SUPER的removeQLFromXZByALL写法
	 * 
	 * @作者：WUZ
	 * @param djdyid
	 * @param qlid
	 * @return
	 */
	protected boolean removeQLFromLSByCFDJ(String qlid) {
		StringBuilder builderDJDYID = new StringBuilder();
		builderDJDYID.append(" QLID='").append(qlid).append("' ");
		;// 在权利历史层中获取到查封的信息
		String strSqlDJDYID = builderDJDYID.toString();
		List<BDCS_QL_LS> qls = getCommonDao().getDataList(BDCS_QL_LS.class, strSqlDJDYID);
		if (qls != null && qls.size() > 0) {
			for (int iql = 0; iql < qls.size(); iql++) {
				BDCS_QL_LS bdcs_ql_ls = qls.get(iql);
				if (bdcs_ql_ls != null) {
					BDCS_FSQL_LS bdcs_fsql_ls = getCommonDao().get(BDCS_FSQL_LS.class, bdcs_ql_ls.getFSQLID());
					if (bdcs_fsql_ls != null) {
						getCommonDao().deleteEntity(bdcs_fsql_ls);
					}
					getCommonDao().deleteEntity(bdcs_ql_ls);// 删除掉 历史相关的权利和附属权利
				}
			}
		}
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
//					List<BDCS_QLR_GZ> qlrs = super.getQLRs(ql.getId());
					List<BDCS_SQR> sqrs = new ArrayList<BDCS_SQR>();
					ProjectServiceImpl serviceImpl = SuperSpringContext.getContext().getBean(ProjectServiceImpl.class);
					sqrs = serviceImpl.getSQRList(super.getXMBH());
			 
					BDCS_FSQL_GZ fsql = null;
					if (ql != null && !StringUtils.isEmpty(ql.getFSQLID())) {
						fsql = dao.get(BDCS_FSQL_GZ.class, ql.getFSQLID());
					}
					
					House h = null;
                    if(djdy.getBDCDYLX().equals("032")){
                    	 h = dao.get(BDCS_H_XZY.class, djdy.getBDCDYID());
                    }else if(djdy.getBDCDYLX().equals("031")){
                    	 h = dao.get(BDCS_H_XZ.class, djdy.getBDCDYID());
                    }
					
					if(h.getZDBDCDYID()!= null){
						BDCS_SHYQZD_XZ zd = dao.get(BDCS_SHYQZD_XZ.class, h.getZDBDCDYID());
						if(zd != null){
							h.setZDDM(zd.getZDDM());
						}
					}else{
						System.out.println("获取不到关联宗地的ID！！！！！！");
					}
					Message msg = exchangeFactory.createMessageByCFDJ();
					super.fillHead(msg, i, ywh,h.getBDCDYH(),h.getQXDM(),ql.getLYQLID());
					msg.getHead().setParcelID(StringHelper.formatObject(h.getZDDM()));
					msg.getHead().setEstateNum(StringHelper.formatObject(h.getBDCDYH()));
//					msg.getHead().setRecType("RT430");
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

							List<DJFDJSH> sh = msg.getData().getDJSH();
							sh = packageXml.getDJFDJSH(h, ywh, this.getXMBH(), actinstID);
							msg.getData().setDJSH(sh);

							List<DJFDJSZ> sz = packageXml.getDJFDJSZ(h, ywh, this.getXMBH());
							msg.getData().setDJSZ(sz);
							
							List<DJFDJSF> sfList = msg.getData().getDJSF();
							sfList = packageXml.getDJSF(h,ywh,this.getXMBH());
							msg.getData().setDJSF(sfList);
									
							List<DJFDJFZ> fz = packageXml.getDJFDJFZ(h,ywh,this.getXMBH());
							msg.getData().setDJFZ(fz);
							List<DJFDJGD>	gd = packageXml.getDJFDJGD(h ,ywh,this.getXMBH());
							msg.getData().setDJGD(gd);	
							List<DJFDJSQR> djsqrs = msg.getData().getDJSQR();
							//查封现在的不填写申请人，为了和接入规范一致，先在这里拼凑出申请人。
							DJFDJSQR djsqr = new DJFDJSQR();
							djsqr.setYsdm("2004020000");
							djsqr.setQlrmc(cfdj == null ? "无" :StringHelper.formatDefaultValue(cfdj.getCfjg()));
							djsqr.setYwh(ywh);
//							djsqr.setQxdm(ConfigHelper.getNameByValue("XZQHDM"));
							djsqrs.add(djsqr);
							djsqrs = packageXml.getDJSQRs(djsqrs, sqrs, h.getYSDM(), ywh, h.getBDCDYH());
							msg.getData().setDJSQR(djsqrs);
							
							FJF100 fj = msg.getData().getFJF100();
							fj = packageXml.getFJF(fj);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					msg.getHead().setRightType("99");
					mashaller.marshal(msg, new File(path + "Biz" + msg.getHead().getBizMsgID() + ".xml"));
					result = uploadFile(path + "Biz" + msg.getHead().getBizMsgID() + ".xml", ConstValue.RECCODE.CF_CFDJ.Value,actinstID,djdy.getDJDYID(),ql.getId());
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
//				String lyqlid = bdcql.getLYQLID();//使用事物以后出错，此处注释，在同通用方法里面给导出对象赋值时 qlid=lyqlid
//				bdcql.setId(lyqlid);
				List<RightsHolder> bdcqlrs = null;
				MessageExport msg = super.getShareMsgTools().GetMsg(bdcdy, bdcql, bdcfsql, bdcqlrs, bljc, xmxx);
				super.getShareMsgTools().SendMsg(msg, idjdy + 1, djdy.getBDCDYLX(), xmxx);
			}
		}
	}

	
}
