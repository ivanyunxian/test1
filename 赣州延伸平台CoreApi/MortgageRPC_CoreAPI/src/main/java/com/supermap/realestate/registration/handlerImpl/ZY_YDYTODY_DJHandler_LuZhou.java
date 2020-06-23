package com.supermap.realestate.registration.handlerImpl;

import java.util.List;
import java.util.Map;

import com.supermap.realestate.registration.ViewClass.ProjectInfo;
import com.supermap.realestate.registration.handler.DJHandler;
import com.supermap.realestate.registration.model.BDCS_DJDY_GZ;
import com.supermap.realestate.registration.model.BDCS_FSQL_GZ;
import com.supermap.realestate.registration.model.BDCS_QL_GZ;
import com.supermap.realestate.registration.model.BDCS_QL_XZ;
import com.supermap.realestate.registration.model.BDCS_SHYQZD_XZ;
import com.supermap.realestate.registration.model.interfaces.House;
import com.supermap.realestate.registration.model.interfaces.RealUnit;
import com.supermap.realestate.registration.tools.UnitTools;
import com.supermap.realestate.registration.util.ConstValue.BDCDYLX;
import com.supermap.realestate.registration.util.ConstValue.CZFS;
import com.supermap.realestate.registration.util.ConstValue.DJDYLY;
import com.supermap.realestate.registration.util.ConstValue.DJLX;
import com.supermap.realestate.registration.util.ConstValue.DJZT;
import com.supermap.realestate.registration.util.ConstValue.QLLX;
import com.supermap.realestate.registration.util.ConstValue.SQRLB;
import com.supermap.realestate.registration.util.ConstValue.YGDJLX;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;

/*
 1、国有建设用地使用权转移登记+抵押权初始登记（未配置）
 1、集体建设用地使用权转移登记+抵押权初始登记（未配置）
 1、宅基地使用权转移登记+抵押权初始登记（未配置）
 1、国有建设用地使用权/房屋所有权转移登记+抵押权初始登记（未配置）
 1、集体建设用地使用权/房屋所有权转移登记+抵押权初始登记（未配置）
 1、宅基地使用权/房屋所有权转移登记+抵押权初始登记（未配置）
 */
/**
 * 
 * 转移登记+抵押权初始登记操作类
 * @ClassName: ZY_YDYTODY_DJHandler
 * @author 俞学斌
 * @date 2015年9月12日 下午05:07:16
 */
public class ZY_YDYTODY_DJHandler_LuZhou extends ZY_YDYTODY_DJHandler implements DJHandler {

	/**
	 * 构造函数
	 * @param info
	 */
	public ZY_YDYTODY_DJHandler_LuZhou(ProjectInfo info) {
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

			if (unit instanceof House) {
				House house = (House) unit;
				ql.setQLQSSJ(house.getTDSYQQSRQ());
				ql.setQLJSSJ(house.getTDSYQZZRQ());
			}

			// 做转移的时候加上来源权利ID
			String qllxarray = "('1','2','3','4','5','6','7','8','9','10','11','12','13','14','15','16','17','18')";
			String hql = " DJDYID='" + djdy.getDJDYID() + "' AND QLLX IN " + qllxarray;
			String lyqlid = "";
			List<BDCS_QL_XZ> list = dao.getDataList(BDCS_QL_XZ.class, hql);
			if (list != null && list.size() > 0) {
				lyqlid = list.get(0).getId();
				ql.setLYQLID(lyqlid);
			}
			StringBuilder builer = new StringBuilder();
			builer.append("SELECT DYQL.QLID AS QLID FROM BDCK.BDCS_QL_XZ DYQL ");
			builer.append("LEFT JOIN BDCK.BDCS_DJDY_XZ DJDY_YC ON DYQL.DJDYID=DJDY_YC.DJDYID ");
			builer.append("LEFT JOIN BDCK.YC_SC_H_XZ GX ON GX.YCBDCDYID=DJDY_YC.BDCDYID ");
			builer.append("AND GX.SCBDCDYID='");
			builer.append(bdcdyid);
			builer.append("' ");
			builer.append("WHERE DYQL.QLLX='23' AND GX.SCBDCDYID IS NOT NULL");
			List<Map> listDYQ = dao.getDataListByFullSql(builer.toString());
			if (listDYQ != null && listDYQ.size() > 0) {
				CopyQLXXFromYDY(StringHelper.formatObject(listDYQ.get(0).get("QLID")), djdy);
			} else {
				// 生成抵押权
				BDCS_QL_GZ dyql = super.createQL(djdy, unit);
				// 生成抵押权附属权利
				BDCS_FSQL_GZ dyfsql = super.createFSQL(djdy.getDJDYID());

				// 关联抵押权权利-附属权利，设置抵押物类型、抵押不动产类型、预告登记种类
				dyql.setFSQLID(dyfsql.getId());
				dyql.setDJLX(DJLX.CSDJ.Value);
				dyql.setQLLX(QLLX.DIYQ.Value);
				dyql.setCZFS(CZFS.GTCZ.Value);//设置抵押权的持证方式为共同持证。
				dyfsql.setQLID(dyql.getId());
				dyfsql.setDYWLX(getDYBDCLXfromBDCDYLX(getBdcdylx()));
				dyfsql.setDYBDCLX(getDYBDCLXfromBDCDYLX(getBdcdylx()));
				dyfsql.setYGDJZL(YGDJLX.YSSPFDYQYGDJ.Value.toString());
				dao.save(dyql);
				dao.save(dyfsql);
			}
			unit.setDJZT(DJZT.DJZ.Value); // 设置登记状态

			// 保存权利和附属权利
			dao.save(ql);
			dao.save(fsql);
			dao.save(djdy);
			// 拷贝转移前权利人到申请人
			super.CopySQRFromXZQLR(djdy.getDJDYID(), ql.getQLLX(), this.getXMBH(), ql.getId(),SQRLB.YF.Value);
		}
		dao.flush();
		bsuccess = true;
		return bsuccess;
	}
}
