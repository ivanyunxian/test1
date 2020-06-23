package com.supermap.realestate.registration.handlerImpl;

import java.util.List;


import com.supermap.realestate.registration.ViewClass.ProjectInfo;
import com.supermap.realestate.registration.handler.DJHandler;
import com.supermap.realestate.registration.model.BDCS_DJDY_GZ;
import com.supermap.realestate.registration.model.BDCS_QL_GZ;
import com.supermap.realestate.registration.model.BDCS_QL_XZ;
import com.supermap.realestate.registration.model.interfaces.RealUnit;
import com.supermap.realestate.registration.util.ConstValue.BDCDYLX;
import com.supermap.realestate.registration.util.ConstValue.DJDYLY;
import com.supermap.realestate.registration.util.ConstValue.QLLX;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;

/*
 1、集体建设用地使用权转为国有建设用地使用权 
 */
/**
 * 
 * 更正登记处理类
 * 
 * @ClassName: BGDJ_JTTOGYJSYDSHYQHandler
 * @author rq
 * @date 2016年11月12日 下午10:35:03
 */
public class BGDJ_JTTOGYJSYDSHYQHandler extends GZDJHandler implements DJHandler {

	/**
	 * 
	 * @param info
	 */
	public BGDJ_JTTOGYJSYDSHYQHandler(ProjectInfo info) {
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
	    if (this.getBdcdylx().equals(BDCDYLX.SHYQZD)) {
			unit = CopyUseLand(bdcdyid, bdcdyid_new);
		}
		if (unit == null) {
			super.setErrMessage("添加失败！");
			return false;
		}
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
			gzql = super.CopyQLXXFromXZExceptBDCQZH(qls.get(0).getId());
			
			if (gzql != null) {
				//将qllx从集体建设用地使用权"7"转为国有建设用地使用权"3"
				gzql.setQLLX(QLLX.GYJSYDSHYQ.Value);
				dao.update(gzql);
			}
			
		}
		// 把权利人拷贝过来放到申请人里边 add by diaoliwei 2015-7-31
		copyApplicantFromGZ(bdcs_djdy_gz.getDJDYID());
		dao.flush();
		bsuccess = true;
		return bsuccess;
	}
}
