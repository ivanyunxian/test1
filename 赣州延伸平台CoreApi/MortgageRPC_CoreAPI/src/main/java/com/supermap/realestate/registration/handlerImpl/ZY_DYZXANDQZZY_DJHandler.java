package com.supermap.realestate.registration.handlerImpl;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.util.StringUtils;

import com.supermap.realestate.registration.ViewClass.ProjectInfo;
import com.supermap.realestate.registration.ViewClass.UnitTree;
import com.supermap.realestate.registration.handler.DJHandler;
import com.supermap.realestate.registration.model.BDCS_DJDY_GZ;
import com.supermap.realestate.registration.model.BDCS_DJDY_XZ;
import com.supermap.realestate.registration.model.BDCS_FSQL_GZ;
import com.supermap.realestate.registration.model.BDCS_FSQL_LS;
import com.supermap.realestate.registration.model.BDCS_FSQL_XZ;
import com.supermap.realestate.registration.model.BDCS_H_GZ;
import com.supermap.realestate.registration.model.BDCS_H_XZ;
import com.supermap.realestate.registration.model.BDCS_H_XZY;
import com.supermap.realestate.registration.model.BDCS_PARTIALLIMIT;
import com.supermap.realestate.registration.model.BDCS_QDZR_GZ;
import com.supermap.realestate.registration.model.BDCS_QDZR_XZ;
import com.supermap.realestate.registration.model.BDCS_QLR_GZ;
import com.supermap.realestate.registration.model.BDCS_QLR_XZ;
import com.supermap.realestate.registration.model.BDCS_QL_GZ;
import com.supermap.realestate.registration.model.BDCS_QL_LS;
import com.supermap.realestate.registration.model.BDCS_QL_XZ;
import com.supermap.realestate.registration.model.BDCS_SHYQZD_GZ;
import com.supermap.realestate.registration.model.BDCS_SHYQZD_XZ;
import com.supermap.realestate.registration.model.BDCS_SLLM_GZ;
import com.supermap.realestate.registration.model.BDCS_SQR;
import com.supermap.realestate.registration.model.BDCS_SYQZD_GZ;
import com.supermap.realestate.registration.model.BDCS_XMXX;
import com.supermap.realestate.registration.model.BDCS_ZH_GZ;
import com.supermap.realestate.registration.model.BDCS_ZRZ_GZ;
import com.supermap.realestate.registration.model.BDCS_ZRZ_XZ;
import com.supermap.realestate.registration.model.BDCS_ZS_GZ;
import com.supermap.realestate.registration.model.BDCS_ZS_XZ;
import com.supermap.realestate.registration.model.WFD_MAPPING;
import com.supermap.realestate.registration.model.YC_SC_H_XZ;
import com.supermap.realestate.registration.model.interfaces.House;
import com.supermap.realestate.registration.model.interfaces.RealUnit;
import com.supermap.realestate.registration.model.interfaces.Rights;
import com.supermap.realestate.registration.model.interfaces.RightsHolder;
import com.supermap.realestate.registration.model.interfaces.SubRights;
import com.supermap.realestate.registration.model.xmlExportmodel.MessageExport;
import com.supermap.realestate.registration.tools.RightsHolderTools;
import com.supermap.realestate.registration.tools.RightsTools;
import com.supermap.realestate.registration.tools.UnitTools;
import com.supermap.realestate.registration.util.ConstValue;
import com.supermap.realestate.registration.util.ConstValue.BDCDYLX;
import com.supermap.realestate.registration.util.ConstValue.DJDYLY;
import com.supermap.realestate.registration.util.ConstValue.DJLX;
import com.supermap.realestate.registration.util.ConstValue.QLLX;
import com.supermap.realestate.registration.util.ConstValue.SQRLB;
import com.supermap.realestate.registration.util.ConfigHelper;
import com.supermap.realestate.registration.util.Global;
import com.supermap.realestate.registration.util.ObjectHelper;
import com.supermap.realestate.registration.util.ProjectHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.realestate.registration.util.exportXmlUtil;
import com.supermap.wisdombusiness.core.SuperSpringContext;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;

/**
 * 
* ZY_DYZXANDQZZY_DJHandler-转移登记_抵押注销+强制转移    
* @ClassName: ZY_DYZXANDQZZY_DJHandler 
*/
public class ZY_DYZXANDQZZY_DJHandler extends DJHandlerBase implements DJHandler {

	/**
	 * 构造函数
	 * 
	 * @param info
	 */
	public ZY_DYZXANDQZZY_DJHandler(ProjectInfo info) {
		super(info);
	}

	/**
	 * 添加不动产单元--qlids--qllx=23
	 */
	@Override
	public boolean addBDCDY(String qlids) {
		boolean bsuccess = false;
		CommonDao dao = getCommonDao();
		String qlid[] = qlids.split(",");
		if (qlid != null && qlid.length > 0) {
			for (String id : qlid) {		
				StringBuilder builer = new StringBuilder();
				builer.append(" QLID='").append(id).append("'");
				String strQuery = builer.toString();
				BDCS_QL_XZ bdcs_ql_xz = getCommonDao().get(BDCS_QL_XZ.class, id);
				if (bdcs_ql_xz != null) {
					// 获取登记单元、权利、附属权利---产权
					StringBuilder builderDJDY = new StringBuilder();
					builderDJDY.append(" DJDYID='").append(bdcs_ql_xz.getDJDYID()).append("' ");
					List<BDCS_DJDY_XZ> djdys = getCommonDao().getDataList(BDCS_DJDY_XZ.class, builderDJDY.toString());
					if (djdys != null && djdys.size() > 0) {
						BDCS_DJDY_XZ djdy_xz = djdys.get(0);
						// 拷贝登记单元
						BDCS_DJDY_GZ djdy_gz = ObjectHelper.copyDJDY_XZToGZ(djdy_xz);
						djdy_gz.setId(getPrimaryKey());
						djdy_gz.setLY(DJDYLY.XZ.Value);
						djdy_gz.setXMBH(getXMBH());
						getCommonDao().save(djdy_gz);

						RealUnit unit = null;
						try {
							UnitTools.loadUnit(this.getBdcdylx(), DJDYLY.initFrom(djdy_gz.getLY()), djdy_gz.getBDCDYID());
						} catch (Exception e) {
							e.printStackTrace();
						}

						BDCS_QL_GZ ql = super.createQL(djdy_gz, unit);
						// 生成附属权利
						BDCS_FSQL_GZ fsql = super.createFSQL(djdy_gz.getDJDYID());
						fsql.setQLID(ql.getId());
						ql.setFSQLID(fsql.getId());
						// 如果是使用权宗地，把使用权面积加上
						if (getBdcdylx().equals(BDCDYLX.SHYQZD)) {
							BDCS_SHYQZD_XZ xzshyqzd = dao.get(BDCS_SHYQZD_XZ.class, djdy_gz.getBDCDYID());
							if (xzshyqzd != null) {
								fsql.setSYQMJ(xzshyqzd.getZDMJ());
								ql.setQDJG(xzshyqzd.getJG());// 取得价格
							}
						}

						//做转移的时候加上来源权利ID
						String qllxarray = "('1','2','3','4','5','6','7','8','9','10','11','12','13','14','15','16','17','18')";
						String hql =  builderDJDY.append(" AND QLLX IN ").append(qllxarray).toString();
						String lyqlid = "";
						List<BDCS_QL_XZ> list = dao.getDataList(BDCS_QL_XZ.class, hql);
						if (list != null && list.size() > 0) {
							lyqlid = list.get(0).getId();
							ql.setLYQLID(lyqlid);
						}
						// 保存权利和附属权利
						dao.save(ql);
						dao.save(fsql);
						dao.save(djdy_gz);
						// 拷贝转移前权利人到申请人-义务人
						super.CopySQRFromXZQLR(djdy_gz.getDJDYID(), ql.getQLLX(), this.getXMBH(), ql.getId(),SQRLB.YF.Value);
					}
					
					//抵押权信息
					String gzqlid = getPrimaryKey();
					String gzfsqlid = getPrimaryKey();
					// 拷贝权利
					BDCS_QL_GZ bdcs_ql_gz = ObjectHelper.copyQL_XZToGZ(bdcs_ql_xz);
					bdcs_ql_gz.setId(gzqlid);
					bdcs_ql_gz.setFSQLID(gzfsqlid);
					bdcs_ql_gz.setDJLX(this.getDjlx().Value);
					bdcs_ql_gz.setXMBH(getXMBH());
					bdcs_ql_gz.setYWH(this.getProject_id());
					bdcs_ql_gz.setLYQLID(id);
//					bdcs_ql_gz.setISPARTIAL(bdcs_ql_xz.getISPARTIAL());
					String xzqhdm=ConfigHelper.getNameByValue("XZQHDM");
					if(!StringHelper.isEmpty(xzqhdm) && xzqhdm.startsWith("1301")){
						bdcs_ql_gz.setDJYY("主债权消灭");
					}
					getCommonDao().save(bdcs_ql_gz);
					BDCS_FSQL_XZ bdcs_fsql_xz = getCommonDao().get(BDCS_FSQL_XZ.class, bdcs_ql_xz.getFSQLID());
					if (bdcs_fsql_xz != null) {
						// 拷贝附属权利
						BDCS_FSQL_GZ bdcs_fsql_gz = ObjectHelper.copyFSQL_XZToGZ(bdcs_fsql_xz);
						bdcs_fsql_gz.setQLID(gzqlid);
						bdcs_fsql_gz.setId(gzfsqlid);
						bdcs_fsql_gz.setXMBH(getXMBH());
						bdcs_fsql_gz.setZXDYYWH(super.getProject_id());
						if(!StringHelper.isEmpty(xzqhdm) && xzqhdm.startsWith("13"))
							bdcs_fsql_gz.setZXDYYY("主债权消灭");
//						if (bdcs_fsql_gz.getDYR() == null || StringHelper.isEmpty(bdcs_fsql_gz.getDYR()))
//							bdcs_fsql_gz.setDYR(ywrmc);
						getCommonDao().save(bdcs_fsql_gz);
					}
					
					// 构建权地证人--qlid--抵押权人
					List<BDCS_QLR_XZ> qlrs = getCommonDao().getDataList(BDCS_QLR_XZ.class, strQuery);
					if (qlrs != null && qlrs.size() > 0) {
						for (int iqlr = 0; iqlr < qlrs.size(); iqlr++) {
							BDCS_QLR_XZ bdcs_qlr_xz = qlrs.get(iqlr);
							if (bdcs_qlr_xz != null) {
								// 拷贝权利人
								String gzqlrid = getPrimaryKey();
								BDCS_QLR_GZ bdcs_qlr_gz = ObjectHelper.copyQLR_XZToGZ(bdcs_qlr_xz);
								bdcs_qlr_gz.setId(gzqlrid);
								bdcs_qlr_gz.setQLID(gzqlid);
								bdcs_qlr_gz.setXMBH(getXMBH());
								getCommonDao().save(bdcs_qlr_gz);

								BDCS_SQR sqr = super.copyXZQLRtoSQR(bdcs_qlr_xz, SQRLB.JF);
								if(sqr != null){
									sqr.setGLQLID(bdcs_ql_gz.getId());
									if(StringHelper.isEmpty(sqr.getSQRLX())){
										sqr.setSQRLX("2");
									}
									if(StringHelper.isEmpty(sqr.getZJLX())){
										sqr.setZJLX("7");
									}
									getCommonDao().save(sqr);
								}
								// 获取证书集合
								StringBuilder builder = new StringBuilder();
								builder.append(" id IN (");
								builder.append("select ZSID FROM BDCS_QDZR_XZ WHERE QLID ='");
								builder.append(id).append("'").append(" AND QLRID='");
								builder.append(bdcs_qlr_xz.getId()).append("')");
								String strQueryZS = builder.toString();
								List<BDCS_ZS_XZ> zss = getCommonDao().getDataList(BDCS_ZS_XZ.class, strQueryZS);
								if (zss != null && zss.size() > 0) {
									for (int izs = 0; izs < zss.size(); izs++) {
										BDCS_ZS_XZ bdcs_zs_xz = zss.get(izs);
										if (bdcs_zs_xz != null) {
											String gzzsid = getPrimaryKey();
											BDCS_ZS_GZ bdcs_zs_gz = ObjectHelper.copyZS_XZToGZ(bdcs_zs_xz);
											bdcs_zs_gz.setId(gzzsid);
											bdcs_zs_gz.setXMBH(getXMBH());
											getCommonDao().save(bdcs_zs_gz);
											// 获取权地证人集合
											StringBuilder builderQDZR = new StringBuilder();
											builderQDZR.append(strQuery);
											builderQDZR.append(" AND ZSID='").append(bdcs_zs_xz.getId());
											builderQDZR.append("' AND QLID='").append(id);
											builderQDZR.append("' AND QLRID='").append(bdcs_qlr_xz.getId());
											builderQDZR.append("')");
											List<BDCS_QDZR_XZ> qdzrs = getCommonDao().getDataList(BDCS_QDZR_XZ.class, builderQDZR.toString());
											if (qdzrs != null && qdzrs.size() > 0) {
												for (int iqdzr = 0; iqdzr < qdzrs.size(); iqdzr++) {
													BDCS_QDZR_XZ bdcs_qdzr_xz = qdzrs.get(iqdzr);
													if (bdcs_qdzr_xz != null) {
														// 拷贝权地证人
														BDCS_QDZR_GZ bdcs_qdzr_gz = ObjectHelper.copyQDZR_XZToGZ(bdcs_qdzr_xz);
														bdcs_qdzr_gz.setId(getPrimaryKey());
														bdcs_qdzr_gz.setZSID(gzzsid);
														bdcs_qdzr_gz.setQLID(gzqlid);
														bdcs_qdzr_gz.setFSQLID(gzfsqlid);
														bdcs_qdzr_gz.setQLRID(gzqlrid);
														bdcs_qdzr_gz.setXMBH(getXMBH());
														getCommonDao().save(bdcs_qdzr_gz);
													}
												}
											}
										}
									}
								}
							}
						}
					}
					bdcs_ql_xz.setDJZT("02");
					getCommonDao().update(bdcs_ql_xz);
				}
			}
			dao.flush();
			bsuccess = true;
			return bsuccess;
		}else {
			return false;
		}	
	}

	/**
	 * 写入登记簿
	 */
	@Override
	public boolean writeDJB() {
		String strSqlXMBH = ProjectHelper.GetXMBHCondition(getXMBH());
		List<BDCS_DJDY_GZ> djdys = getCommonDao().getDataList(BDCS_DJDY_GZ.class, strSqlXMBH);		
		//---抵押注销--qllx=23
		String dbr = Global.getCurrentUserName();
		BDCS_XMXX xmxx = getCommonDao().get(BDCS_XMXX.class, getXMBH());
		String workflowcode = ProjectHelper.getWorkflowCodeByProjectID(xmxx.getPROJECT_ID());
		String sql = " WORKFLOWCODE='" + workflowcode + "'";
		CommonDao baseCommonDao = SuperSpringContext.getContext().getBean(CommonDao.class);
		List<WFD_MAPPING> mappings = baseCommonDao.getDataList(WFD_MAPPING.class, sql);
		WFD_MAPPING maping = mappings.get(0);
		List<BDCS_QL_GZ> qls = getCommonDao().getDataList(BDCS_QL_GZ.class, strSqlXMBH);
		for (BDCS_QL_GZ ql: qls) {
			if ("23".equals(ql.getQLLX())) {
				String fsqlid = ql.getFSQLID();
				String lyqlid = ql.getLYQLID();
				String sqlQL = MessageFormat.format(" QLID=''{0}''", lyqlid);
				if (mappings != null && mappings.size() > 0) {
					//解除关联预测户的抵押权
					if (("1").equals(maping.getISUNLOCKYCHDY()))
						updateychdy(sqlQL,fsqlid);
				}
				// 再删除权利人
				getCommonDao().deleteEntitysByHql(BDCS_QLR_XZ.class, sqlQL);
				// 再删除权利
				getCommonDao().deleteEntitysByHql(BDCS_QL_XZ.class, sqlQL);
				// 再删除附属权利
				getCommonDao().deleteEntitysByHql(BDCS_FSQL_XZ.class, sqlQL);
				// 再删除证书
				String sqlZS = MessageFormat.format(" id IN (SELECT B.ZSID FROM BDCS_QDZR_XZ B WHERE B.QLID=''{0}'')", lyqlid);
				getCommonDao().deleteEntitysByHql(BDCS_ZS_XZ.class, sqlZS);
				// 删除权利-权利人-证书-单元关系
				getCommonDao().deleteEntitysByHql(BDCS_QDZR_XZ.class, sqlQL);
				// 更新历史权利
				BDCS_QL_LS bdcs_ql_ls = getCommonDao().get(BDCS_QL_LS.class, lyqlid);
				if (bdcs_ql_ls != null) {
					BDCS_FSQL_GZ bdcs_fsql_gz = getCommonDao().get(BDCS_FSQL_GZ.class, fsqlid);
					if (bdcs_fsql_gz != null) {
						bdcs_fsql_gz.setZXDBR(dbr);
						Date zxsj = new Date();
						bdcs_fsql_gz.setZXSJ(zxsj);
						getCommonDao().update(bdcs_fsql_gz);
						BDCS_FSQL_LS bdcs_fsql_ls = getCommonDao().get(BDCS_FSQL_LS.class, bdcs_ql_ls.getFSQLID());
						if (bdcs_fsql_ls != null) {
							bdcs_fsql_ls.setZXDYYWH(bdcs_fsql_gz.getZXDYYWH());
							bdcs_fsql_ls.setZXDYYY(bdcs_fsql_gz.getZXDYYY());
							bdcs_fsql_ls.setZXFJ(bdcs_fsql_gz.getZXFJ());
							bdcs_fsql_ls.setZXSJ(zxsj);
							bdcs_fsql_ls.setZXDBR(dbr);
							getCommonDao().update(bdcs_fsql_ls);
						}
					}
				}
				if("1".equals(ql.getISPARTIAL())){
					List<BDCS_PARTIALLIMIT> list=getCommonDao().getDataList(BDCS_PARTIALLIMIT.class, "LIMITQLID='" + ql.getLYQLID()+"'");
					if(list!=null&&list.size()>0){
						for(BDCS_PARTIALLIMIT partialseizures:list){
							partialseizures.setYXBZ("2");
							getCommonDao().update(partialseizures);
						}
					}
				}
			}else if (getQllx().Value.equals(ql.getQLLX())) {//---强制转移--qllx=4
				Date djsj = new Date();
				ql.setDBR(dbr);
				ql.setDJSJ(djsj);
				getCommonDao().update(ql);
				// 根据权利ID删除权利，附属权利，证书，权地证人，权利人信息
				RightsTools.deleteRightsAll(DJDYLY.XZ, ql.getLYQLID());
				Rights ql_xz = RightsTools.copyRightsAll(DJDYLY.GZ, DJDYLY.XZ, ql.getId());
				Rights ql_ls = RightsTools.copyRightsAll(DJDYLY.GZ, DJDYLY.LS, ql.getId());
				getCommonDao().save(ql_xz);
				getCommonDao().save(ql_ls);	
				
				//把现状层里边的查封信息删除掉，
				String bdcdyid=ql.getBDCDYID();
				String djdyid=ql.getDJDYID();				
				String sql1=" QLID IN (SELECT DISTINCT id FROM BDCS_QL_XZ WHERE  DJDYID=''{0}'' AND DJLX=''{1}'' AND QLLX=''{2}'')";
				String sql2=" DJDYID=''{0}'' AND DJLX=''{1}'' AND QLLX=''{2}''";
				sql1=MessageFormat.format(sql1, djdyid,DJLX.CFDJ.Value,"99");
				sql2=MessageFormat.format(sql2, djdyid,DJLX.CFDJ.Value,"99");
				getCommonDao().deleteEntitysByHql(BDCS_QL_XZ.class, sql2);
				getCommonDao().deleteEntitysByHql(BDCS_FSQL_XZ.class, sql1);
				//根据本地化配置来确定是否注销期房查封(1为是，2为否)
				String is_cancleqfcf_xfqzgh=ConfigHelper.getNameByValue("IS_CANCLEQFCF_XFQZGH");
				if ("1".equals(is_cancleqfcf_xfqzgh)) {
					List<YC_SC_H_XZ> REA = new ArrayList<YC_SC_H_XZ>();
					REA = getCommonDao().getDataList(YC_SC_H_XZ.class, " SCBDCDYID='"+bdcdyid+"'");
					if(REA.size() > 0 ){
						List<BDCS_DJDY_XZ> YCH = getCommonDao().getDataList(BDCS_DJDY_XZ.class, " BDCDYID ='"+REA.get(0).getYCBDCDYID()+"'");
						if(YCH != null && YCH.size() > 0){
							List<BDCS_QL_XZ> cfqls = getCommonDao().getDataList(BDCS_QL_XZ.class, "QLLX='99' AND DJLX='800' AND DJDYID='"+YCH.get(0).getDJDYID()+"'");
							for (int i = 0; i < cfqls.size(); i++) {
								getCommonDao().deleteEntity(cfqls.get(i));
								if (!StringHelper.isEmpty(cfqls.get(i).getFSQLID())){
									getCommonDao().delete(BDCS_FSQL_XZ.class, cfqls.get(i).getFSQLID());
									//把历史层里边的查封记录填写上注销登簿人和注销原因等
									BDCS_FSQL_LS fsql_LS = getCommonDao().get(BDCS_FSQL_LS.class, cfqls.get(i).getFSQLID());
									fsql_LS.setZXFJ("现房强制过户注销期房查封");
									fsql_LS.setZXDBR(Global.getCurrentUserName());
									fsql_LS.setZXSJ(new Date());
									fsql_LS.setZXDYYWH(super.getProject_id());
									fsql_LS.setZXDYYY("现房强制过户注销期房查封");
									getCommonDao().update(fsql_LS);
								}								
							}
						}
					}
				}
			
				//把历史层里边的查封记录填写上注销登簿人和注销原因等
				String sql3=" QLID IN (SELECT DISTINCT id FROM BDCS_QL_LS WHERE  DJDYID=''{0}'' AND DJLX=''{1}'' AND QLLX=''{2}'')";
				sql3=MessageFormat.format(sql3, djdyid,DJLX.CFDJ.Value,"99");
				List<BDCS_FSQL_LS> lsqls=getCommonDao().getDataList(BDCS_FSQL_LS.class, sql3);
				if(lsqls != null && lsqls.size() > 0){
					for(BDCS_FSQL_LS fsql:lsqls){
						fsql.setZXFJ("强制过户");
						fsql.setZXDBR(Global.getCurrentUserName());
						fsql.setZXSJ(new Date());
						fsql.setZXDYYWH(super.getProject_id());
						fsql.setZXDYYY("强制过户");
						getCommonDao().update(fsql);
					}
				}
				
				BDCDYLX dylx = BDCDYLX.initFrom(this.getBdcdylx().Value);
				DJDYLY dyly = DJDYLY.initFrom(DJDYLY.XZ.Value);
				
				//把房屋的限制状态改为无
				House house = (House)UnitTools.loadUnit(dylx,dyly, bdcdyid);
				if(house != null) {
					house.setXZZT(null);
					getCommonDao().update(house);
				}
			}
		}
		// 更新单元抵押状态
		for (BDCS_DJDY_GZ bdcs_djdy_gz:djdys) {				
			if (bdcs_djdy_gz != null) {
				BDCDYLX dylx = ProjectHelper.GetBDCDYLX(getXMBH());
				String strDYZT = GetDYZT(bdcs_djdy_gz.getDJDYID());
				SetDYDYZT(bdcs_djdy_gz.getBDCDYID(), dylx, strDYZT);
			}
		}
	
		this.SetSFDB();
		getCommonDao().flush();
		super.alterCachedXMXX();
		return true;
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
							tree.setOlddiyqqlid(ql.getLYQLID());

						} else {
							tree.setQlid(ql.getId());
						}
					}
				}
				tree.setId(djdy.getBDCDYID());
				tree.setBdcdyid(djdy.getBDCDYID());
				tree.setBdcdylx(djdy.getBDCDYLX());
				tree.setDjdyid(djdy.getDJDYID());
				String ly = StringUtils.isEmpty(djdy.getLY()) ? "gz"
						: DJDYLY.initFrom(djdy.getLY()) == null ? "gz" : DJDYLY.initFrom(djdy.getLY()).Name;
				tree.setLy(ly);
				String zl = getZL(tree, djdy.getDJDYID(), djdy.getBDCDYLX(), djdy.getBDCDYID(), ly);
				tree.setText(zl);
				// 如果是户（实测、预测）的话，把房号也加上
				if (BDCDYLX.H.Value.equals(djdy.getBDCDYLX()) || BDCDYLX.YCH.Value.equals(djdy.getBDCDYLX())) {
					if (!StringHelper.isEmpty(djdy.getLY())) {
						DJDYLY ely = DJDYLY.initFrom(djdy.getLY());
						House house = (House) (UnitTools.loadUnit(BDCDYLX.initFrom(djdy.getBDCDYLX()), ely, djdy.getBDCDYID()));
						if (house != null) {
							String fh = house.getFH();
							tree.setFh(fh);
							if (StringHelper.isEmpty(house.getMJ())) {
								tree.setMj(0);
							} else {
								tree.setMj(house.getMJ());
							}
							if (StringHelper.isEmpty(house.getFTTDMJ())) {
								tree.setFttdmj(0);
							} else {
								tree.setFttdmj(house.getFTTDMJ());
							}
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
			String hqlCondition = MessageFormat.format(
					" DJDYID IN (SELECT DJDYID FROM BDCS_DJDY_XZ WHERE BDCDYID=''{0}'') AND QLLX IN {1} ORDER BY DJSJ",
					bdcdyid, qllxarray);
			List<BDCS_QL_XZ> listxzql = dao.getDataList(BDCS_QL_XZ.class, hqlCondition);
			if (listxzql != null && listxzql.size() > 0) {
				BDCS_QL_XZ ql = listxzql.get(0);
				tree.setOldqlid(ql.getId());
			}
		}
		return zl;
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

	
	//解除关联预测户的抵押权
	private void updateychdy(String sqlQL,String fsqlid) {
		String dbr = Global.getCurrentUserName();
		List<BDCS_DJDY_XZ> DJDY_XZs = new ArrayList<BDCS_DJDY_XZ>();
		List<BDCS_QL_XZ> dyql = getCommonDao().getDataList(BDCS_QL_XZ.class, sqlQL);
		if(dyql!=null&&dyql.size()>0){
			String scdjdyid = dyql.get(0).getDJDYID();
			List<BDCS_DJDY_XZ> DJDY_XZ = getCommonDao().getDataList(BDCS_DJDY_XZ.class, " DJDYID='" + scdjdyid + "'");
			if(DJDY_XZ!=null&&DJDY_XZ.size()>0){
				String scdbcdyid = DJDY_XZ.get(0).getBDCDYID();
				List<YC_SC_H_XZ> yc_sc = getCommonDao().getDataList(YC_SC_H_XZ.class, " SCBDCDYID='" + scdbcdyid + "'");
				if(yc_sc!=null&&yc_sc.size()>0){
					String ycbdcdyid = yc_sc.get(0).getYCBDCDYID();
					DJDY_XZs = getCommonDao().getDataList(BDCS_DJDY_XZ.class, " BDCDYID='" + ycbdcdyid + "'");
				}
			}
		}
		if(DJDY_XZs!=null&&DJDY_XZs.size()>0){
			String dy = DJDY_XZs.get(0).getDJDYID();
			List<Rights> ycql = RightsTools.loadRightsByCondition(DJDYLY.XZ, " DJDYID='" + dy + "' AND QLLX='23'");
			for (Rights rights : ycql) {
				sqlQL = MessageFormat.format(" QLID=''{0}''", rights.getId());
				// 再删除权利人
				getCommonDao().deleteEntitysByHql(BDCS_QLR_XZ.class, sqlQL);
				// 再删除权利
				getCommonDao().deleteEntitysByHql(BDCS_QL_XZ.class, sqlQL);
				// 再删除附属权利
				getCommonDao().deleteEntitysByHql(BDCS_FSQL_XZ.class, sqlQL);
				// 再删除证书
				String sqlZS = MessageFormat.format(" id IN (SELECT B.ZSID FROM BDCS_QDZR_XZ B WHERE B.QLID=''{0}'')", rights.getId());
				getCommonDao().deleteEntitysByHql(BDCS_ZS_XZ.class, sqlZS);
				// 删除权利-权利人-证书-单元关系
				getCommonDao().deleteEntitysByHql(BDCS_QDZR_XZ.class, sqlQL);
				BDCS_QL_LS bdcs_ql_ls = getCommonDao().get(BDCS_QL_LS.class, rights.getId());
				if (bdcs_ql_ls != null) {
					BDCS_FSQL_GZ bdcs_fsql_gz = getCommonDao().get(BDCS_FSQL_GZ.class, fsqlid);
					if (bdcs_fsql_gz != null) {
						BDCS_FSQL_LS bdcs_fsql_ls = getCommonDao().get(BDCS_FSQL_LS.class, bdcs_ql_ls.getFSQLID());
						if (bdcs_fsql_ls != null) {
							bdcs_fsql_ls.setZXDYYWH(bdcs_fsql_gz.getZXDYYWH());
							bdcs_fsql_ls.setZXDYYY(bdcs_fsql_gz.getZXDYYY());
							bdcs_fsql_ls.setZXFJ(bdcs_fsql_gz.getZXFJ());
							bdcs_fsql_ls.setZXSJ(new Date());
							bdcs_fsql_ls.setZXDBR(dbr);
							getCommonDao().update(bdcs_fsql_ls);
						}
					}
				}
			}
		}
	}
		
	/**
	 * 共享信息
	 * 
	 * @作者 俞学斌
	 * @创建时间 2015年8月16日下午16:37:43
	 * @return
	 */
	@Override
	public void SendMsg(String bljc){
		BDCS_XMXX xmxx=getCommonDao().get(BDCS_XMXX.class, super.getXMBH());
		String xmbhFilter=ProjectHelper.GetXMBHCondition(super.getXMBH());
		List<BDCS_DJDY_GZ> djdys = getCommonDao().getDataList(
				BDCS_DJDY_GZ.class, xmbhFilter);
		if (djdys != null && djdys.size() > 0) {
			for (int idjdy = 0; idjdy < djdys.size(); idjdy++) {
				BDCS_DJDY_GZ djdy = djdys.get(idjdy);
				ConstValue.BDCDYLX dylx = ConstValue.BDCDYLX.initFrom(djdy.getBDCDYLX());
				ConstValue.DJDYLY dyly = ConstValue.DJDYLY.initFrom(djdy.getLY());
				RealUnit bdcdy = UnitTools.loadUnit(dylx, dyly, djdy.getBDCDYID());
				Rights bdcql=RightsTools.loadRightsByDJDYID(ConstValue.DJDYLY.GZ, getXMBH(), djdy.getDJDYID());
				SubRights bdcfsql=RightsTools.loadSubRightsByRightsID(ConstValue.DJDYLY.GZ, bdcql.getId());
				List<RightsHolder> bdcqlrs=RightsHolderTools.loadRightsHolders(ConstValue.DJDYLY.GZ, djdy.getDJDYID(), getXMBH());
				MessageExport msg= super.getShareMsgTools().GetMsg(bdcdy,bdcql,bdcfsql,bdcqlrs,bljc,xmxx);
				super.getShareMsgTools().SendMsg(msg, idjdy+1,djdy.getBDCDYLX(),xmxx);
			}
		}
	}
	
	@Override
	public Map<String, String> exportXML(String path, String actinstID) {
		return exportXmlUtil.createXMLAndUp(path, actinstID);
	}
}
