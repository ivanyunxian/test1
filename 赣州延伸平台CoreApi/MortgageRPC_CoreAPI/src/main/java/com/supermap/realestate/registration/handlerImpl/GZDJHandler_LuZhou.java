package com.supermap.realestate.registration.handlerImpl;

import java.util.List;

import com.supermap.realestate.registration.ViewClass.ProjectInfo;
import com.supermap.realestate.registration.handler.DJHandler;
import com.supermap.realestate.registration.model.BDCS_DJDY_GZ;
import com.supermap.realestate.registration.model.BDCS_QL_GZ;
import com.supermap.realestate.registration.model.BDCS_QL_XZ;
import com.supermap.realestate.registration.model.WFD_MAPPING;
import com.supermap.realestate.registration.model.interfaces.House;
import com.supermap.realestate.registration.model.interfaces.RealUnit;
import com.supermap.realestate.registration.util.ProjectHelper;
import com.supermap.realestate.registration.util.ConstValue.BDCDYLX;
import com.supermap.realestate.registration.util.ConstValue.DJDYLY;
import com.supermap.realestate.registration.util.ConstValue.SF;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;

/*
 1、国有建设用地使用权更正登记
 2、集体建设用地使用权更正登记（未配置）
 3、宅基地使用权更正登记（未配置）
 4、国有建设用地使用权/房屋所有权更正登记
 5、集体建设用地使用权/房屋所有权更正登记（未配置）
 6、宅基地使用权/房屋所有权更正登记（未配置）
 */
/**
 * 
 * 更正登记处理类
 * @ClassName: GZDJHandler
 * @author liushufeng
 * @date 2015年9月8日 下午10:35:03
 */
public class GZDJHandler_LuZhou extends GZDJHandler implements DJHandler {

	/**
	 * 构造函数
	 * 
	 * @param info
	 */
	public GZDJHandler_LuZhou(ProjectInfo info) {
		super(info);
	}

	/**
	 * 添加不动产单元
	 */
	@Override
	public boolean addBDCDY(String bdcdyid) {
		boolean bsuccess = false;
		CommonDao dao = getCommonDao();
		String bdcdyid_new = getPrimaryKey();

		RealUnit unit = null;
		if (this.getBdcdylx().equals(BDCDYLX.H) || this.getBdcdylx().equals(BDCDYLX.YCH)) {
			unit=super.CopyHouse(bdcdyid,bdcdyid_new);
		} else {
			unit=super.CopyUseLand(bdcdyid,bdcdyid_new);
		}
		unit.setId(bdcdyid_new);
		unit.setXMBH(getXMBH());
		dao.save(unit);

		// 拷贝登记单元
		BDCS_DJDY_GZ bdcs_djdy_gz = super.createDJDYfromXZ(bdcdyid);
		bdcs_djdy_gz.setBDCDYID(bdcdyid_new);
		bdcs_djdy_gz.setId(getPrimaryKey());
		bdcs_djdy_gz.setLY(DJDYLY.GZ.Value);
		dao.save(bdcs_djdy_gz);
		// 拷贝权利信息
		StringBuilder builder = new StringBuilder();
		builder.append(" DJDYID='").append(bdcs_djdy_gz.getDJDYID());
		builder.append("' AND QLLX='").append(getQllx().Value).append("'");
		List<BDCS_QL_XZ> qls = dao.getDataList(BDCS_QL_XZ.class, builder.toString());
		if (qls != null && qls.size() > 0) {
			
			BDCS_QL_GZ gzql =null;
			String newqzh="";
			String workflowcode = ProjectHelper.getWorkflowCodeByProjectID(this.getProject_id());
			List<WFD_MAPPING> listCode=getCommonDao().getDataList(WFD_MAPPING.class, "WORKFLOWCODE='"+workflowcode+"'");
			if(listCode!=null&&listCode.size()>0){
				newqzh=listCode.get(0).getNEWQZH();
			}
			if (SF.NO.Value.equals(newqzh)){
				gzql=super.CopyQLXXFromXZ(qls.get(0).getId());
			}else{
				gzql=super.CopyQLXXFromXZExceptBDCQZH(qls.get(0).getId());
			}
			if (gzql != null) {

				// 泸州特殊的地方，要把权利的起始时间和结束时间赋值
				/*
				 * if (unit instanceof LandAttach) { LandAttach attach =
				 * (LandAttach) unit; String zdbdcdyid = attach.getZDBDCDYID();
				 * if (zdbdcdyid != null) { String hqlzdql =
				 * MessageFormat.format(
				 * " DJDYID IN (SELECT DJDYID FROM BDCS_DJDY_XZ WHERE BDCDYID=''{0}'') AND QLLX IN ('1','2','3','5','11')"
				 * , zdbdcdyid); List<BDCS_QL_XZ> xzzdqls =
				 * getCommonDao().getDataList(BDCS_QL_XZ.class, hqlzdql); if
				 * (xzzdqls != null && xzzdqls.size() > 0) { BDCS_QL_XZ zdql =
				 * xzzdqls.get(0); if (zdql != null) {
				 * gzql.setQLQSSJ(zdql.getQLQSSJ());
				 * gzql.setQLJSSJ(zdql.getQLJSSJ()); } } } }
				 */
				// 泸州版本：更正登记，起止时间处理，如果是宗地，就不管了，如果是房屋，就把房屋里边的起止时间放到权利起止时间
				if (unit instanceof House) {
					House house = (House) unit;
					if (house != null) {
						gzql.setQLQSSJ(house.getTDSYQQSRQ());
						gzql.setQLJSSJ(house.getTDSYQZZRQ());
					}
				}
			}
		}
		// 把权利人拷贝过来放到申请人里边 add by diaoliwei 2015-7-31
		super.copyApplicant(bdcs_djdy_gz.getDJDYID());
		dao.flush();
		bsuccess = true;
		return bsuccess;
	}
}
