package com.supermap.realestate.registration.handlerImpl;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.supermap.realestate.registration.ViewClass.ProjectInfo;
import com.supermap.realestate.registration.handler.DJHandler;
import com.supermap.realestate.registration.model.BDCS_DJDY_GZ;
import com.supermap.realestate.registration.model.BDCS_FSQL_GZ;
import com.supermap.realestate.registration.model.BDCS_QLR_GZ;
import com.supermap.realestate.registration.model.BDCS_QLR_XZ;
import com.supermap.realestate.registration.model.BDCS_QL_GZ;
import com.supermap.realestate.registration.model.BDCS_QL_XZ;
import com.supermap.realestate.registration.model.BDCS_SHYQZD_XZ;
import com.supermap.realestate.registration.model.BDCS_SQR;
import com.supermap.realestate.registration.model.BDCS_XMXX;
import com.supermap.realestate.registration.model.interfaces.House;
import com.supermap.realestate.registration.model.interfaces.RealUnit;
import com.supermap.realestate.registration.model.interfaces.RightsHolder;
import com.supermap.realestate.registration.tools.RightsHolderTools;
import com.supermap.realestate.registration.tools.UnitTools;
import com.supermap.realestate.registration.util.ConfigHelper;
import com.supermap.realestate.registration.util.ConstValue.BDCDYLX;
import com.supermap.realestate.registration.util.ConstValue.DJDYLY;
import com.supermap.realestate.registration.util.ConstValue.SQRLB;
import com.supermap.realestate.registration.util.Global;
import com.supermap.realestate.registration.util.ProjectHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;

/*
 1、国有建设用地使用权转移登记
 2、集体建设用地使用权转移登记（未配置）
 3、宅基地使用权转移登记（未配置）
 4、国有建设用地使用权/房屋所有权转移登记
 5、集体建设用地使用权/房屋所有权转移登记（未配置）
 6、宅基地使用权/房屋所有权转移登记（未配置）
 */
/**
 * 
 * 转移登记处理类(泸州版本，权利的起止时间不为空)
 * @ClassName: ZYDJHandler
 * @author liushufeng
 * @date 2015年9月8日 下午10:46:29
 */
public class ZYDJHandler_LuZhou extends ZYDJHandler implements DJHandler {
	private static final Log logger = LogFactory.getLog(ZYDJHandler_LuZhou.class);

	/**
	 * 构造函数
	 * 
	 * @param info
	 */
	public ZYDJHandler_LuZhou(ProjectInfo info) {
		super(info);
	}

	/**
	 * 添加不动产单元
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public boolean addBDCDY(String bdcdyid) {
		boolean bsuccess = false;
		CommonDao dao = getCommonDao();
		BDCS_DJDY_GZ djdy = super.createDJDYfromXZ(bdcdyid);
		if (djdy != null) {
			RealUnit unit = null;
			try {
				unit = UnitTools.loadUnit(this.getBdcdylx(), DJDYLY.initFrom(djdy.getLY()), djdy.getBDCDYID());
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

			// 做转移的时候加上来源权利ID
			String qllxarray = "('1','2','3','4','5','6','7','8','9','10','11','12','13','14','15','16','17','18')";
			String hql = " DJDYID='" + djdy.getDJDYID() + "' AND QLLX IN " + qllxarray;
			List<BDCS_QL_XZ> list = dao.getDataList(BDCS_QL_XZ.class, hql);
			if (list != null && list.size() > 0) {
				String lyqlid = list.get(0).getId();
				ql.setLYQLID(lyqlid);

				// 泸州版本：加上起止时间的处理
				ql.setQLQSSJ(list.get(0).getQLQSSJ());
				ql.setQLJSSJ(list.get(0).getQLJSSJ());
				if (unit instanceof House) {
					House house = (House) unit;
					ql.setQLQSSJ(house.getTDSYQQSRQ());
					ql.setQLJSSJ(house.getTDSYQZZRQ());
				}
			}

			// 保存权利和附属权利
			dao.save(ql);
			dao.save(fsql);
			dao.save(djdy);
			
			//拷贝转移预告权利人到申请人中的权利人
			CopyZYYGQLRToSQR(ql.getId(),djdy.getDJDYID());
			//拷贝申请人中关联转移预告权利人到工作层权利人
			CopySQRToGZQLR(ql.getId(),this.getXMBH());
			// 拷贝转移前权利人到申请人
			super.CopySQRFromXZQLR(djdy.getDJDYID(), ql.getQLLX(), this.getXMBH(), ql.getId(),SQRLB.YF.Value);
			
			//商品房转移登记把预告登记中的权利人信息带到申请人中
			String codes_zydj = ConfigHelper.getNameByValue("CODES_ZYDJ");
			if(!StringHelper.isEmpty(codes_zydj))
			{
				BDCS_XMXX xmxx=Global.getXMXXbyXMBH(this.getXMBH());
				if(!StringHelper.isEmpty(xmxx))
				{
					String workflowcode = ProjectHelper.getWorkflowCodeByProjectID(xmxx.getPROJECT_ID());
					if (codes_zydj.contains(workflowcode)) {
						String fulSql="select ql.QLID  from BDCK.BDCS_DJDY_XZ djdy left join BDCK.BDCS_QL_XZ ql  on ql.djdyid = djdy.djdyid  where ql.qllx = '4' and djdy.bdcdylx = '032'   and djdy.bdcdyid in (select ycbdcdyid from BDCK.YC_SC_H_XZ where scbdcdyid = '"+bdcdyid+"' )";
						List<Map> lst=dao.getDataListByFullSql(fulSql);
						 for (Map map :lst)
						 {
							 String qlid=StringHelper.formatObject(map.get("QLID"));
							 if(!StringHelper.isEmpty(qlid))
							 {
								 List<RightsHolder> rhs= RightsHolderTools.loadRightsHolders(DJDYLY.XZ, qlid);
								 for(RightsHolder rh :rhs)
								 {
									 BDCS_QLR_XZ bdcs_qlr_xz=(BDCS_QLR_XZ) rh;
									 String zjh=bdcs_qlr_xz.getZJH();
									 if(!StringHelper.isEmpty(bdcs_qlr_xz.getQLRMC()))
									 {
										 String Sql = "";
											if (!StringHelper.isEmpty(zjh)) {
												Sql = MessageFormat.format(" XMBH=''{0}'' AND SQRXM=''{1}'' AND ZJH=''{2}''", getXMBH(), bdcs_qlr_xz.getQLRMC(), zjh);
											} else {
												Sql = MessageFormat.format(" XMBH=''{0}'' AND SQRXM=''{1}'' AND ZJH IS NULL", getXMBH(), bdcs_qlr_xz.getQLRMC());
											}
											List<BDCS_SQR> lstsqr=dao.getDataList(BDCS_SQR.class, Sql);
											if(!(!StringHelper.isEmpty(lstsqr) && lstsqr.size()>0))
											{
												BDCS_SQR bdcs_sqr= super.copyXZQLRtoSQR(bdcs_qlr_xz, SQRLB.JF);
												if(!StringHelper.isEmpty(bdcs_sqr))
												{
													bdcs_sqr.setGLQLID(ql.getId());
													dao.save(bdcs_sqr);
												}
											}
									 }
									
								 }
							 }
						 }
					}
				}
			}
		}
		dao.flush();
		bsuccess = true;
		return bsuccess;
	}
	
	public void CopyXZQLRtoSQR(String djdyid, String qllx, String xmbh, String qlid, String sqrlb,String lyqlid) {
		CommonDao dao = getCommonDao();
		StringBuilder builderQL = new StringBuilder();
		builderQL.append("QLID ='");
		builderQL.append(lyqlid);
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
			String SQRID = getPrimaryKey();
			BDCS_QLR_XZ qlr = qlrs.get(iqlr);
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

	public void CopySQRToGZQLR(String qlid,String xmbh){
		CommonDao dao = getCommonDao();
		StringBuilder builderQL = new StringBuilder();
		builderQL.append("GLQLID ='");
		builderQL.append(qlid);
		builderQL.append("'");
		List<BDCS_SQR> sqrs = dao.getDataList(BDCS_SQR.class, builderQL.toString());
		if (sqrs == null || sqrs.size() <= 0) {
			return;
		}
		for(int i=0;i<sqrs.size();i++){
			BDCS_SQR sqr=sqrs.get(i);
			String QLRID = getPrimaryKey();
			BDCS_QLR_GZ qlrgz=new BDCS_QLR_GZ();
			qlrgz.setId(QLRID);
			qlrgz.setGYFS(sqr.getGYFS());
			qlrgz.setFZJG(sqr.getFZJG());
			qlrgz.setGJ(sqr.getGJDQ());
			qlrgz.setGZDW(sqr.getGZDW());
			qlrgz.setXB(sqr.getXB()); 
			qlrgz.setHJSZSS(sqr.getHJSZSS()); 
			qlrgz.setSSHY(sqr.getSSHY());
			qlrgz.setYXBZ(sqr.getYXBZ());
			qlrgz.setQLBL(sqr.getQLBL());
			qlrgz.setQLMJ(StringHelper.getDouble(sqr.getQLMJ()));
			qlrgz.setQLRMC(sqr.getSQRXM());
			if(!StringHelper.isEmpty(sqr.getSQRLX())){
				qlrgz.setQLRLX(sqr.getSQRLX());
			}
			qlrgz.setDZYJ(sqr.getDZYJ());
			qlrgz.setDH(sqr.getLXDH());
			qlrgz.setZJH(sqr.getZJH());
			qlrgz.setZJZL(sqr.getZJLX());
			qlrgz.setDZ(sqr.getTXDZ());
			qlrgz.setYB(sqr.getYZBM());
			qlrgz.setXMBH(xmbh);
			dao.save(qlrgz);
		}
		dao.flush();
	}
    

	public Map<String, String> exportXML(String path, String actinstID) {
		return super.exportXML(path, actinstID);
	}
}
