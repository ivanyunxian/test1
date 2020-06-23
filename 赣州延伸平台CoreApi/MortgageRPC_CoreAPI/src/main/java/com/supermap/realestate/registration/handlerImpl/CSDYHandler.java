/**
 * 组合业务 初始登记+在建工程抵押转现房登记抵押
 * 
 * @author 海豹
 *
 */
package com.supermap.realestate.registration.handlerImpl;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

import org.springframework.util.StringUtils;

import com.supermap.realestate.registration.ViewClass.ProjectInfo;
import com.supermap.realestate.registration.handler.DJHandler;
import com.supermap.realestate.registration.model.BDCS_DJDY_GZ;
import com.supermap.realestate.registration.model.BDCS_FSQL_GZ;
import com.supermap.realestate.registration.model.BDCS_QL_GZ;
import com.supermap.realestate.registration.model.BDCS_SQR;
import com.supermap.realestate.registration.model.DCS_QLR_GZ;
import com.supermap.realestate.registration.model.interfaces.House;
import com.supermap.realestate.registration.model.interfaces.LandAttach;
import com.supermap.realestate.registration.model.interfaces.OwnerLand;
import com.supermap.realestate.registration.model.interfaces.RealUnit;
import com.supermap.realestate.registration.model.interfaces.UseLand;
import com.supermap.realestate.registration.tools.RightsTools;
import com.supermap.realestate.registration.tools.UnitTools;
import com.supermap.realestate.registration.util.ConstValue.BDCDYLX;
import com.supermap.realestate.registration.util.ConstValue.DJDYLY;
import com.supermap.realestate.registration.util.ConstValue.DJZT;
import com.supermap.realestate.registration.util.ConstValue.QLLX;
import com.supermap.realestate.registration.util.ConstValue.YGDJLX;
import com.supermap.realestate.registration.util.Global;
import com.supermap.realestate.registration.util.ObjectHelper;
import com.supermap.realestate.registration.util.ProjectHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import com.supermap.wisdombusiness.web.ResultMessage;

/*
 1、初始登记+在建工程转现登记（待测）
 */
/**
 * 初始登记+在建工程抵押转现房抵押登记处理类
 * @ClassName: CSDYHandler
 * @author liushufeng
 * @date 2015年9月8日 下午10:31:21
 */
public class CSDYHandler extends ZY_YDYTODY_DJHandler implements DJHandler {
	/**
	 * 构造函数
	 * 
	 * @param info
	 */
	public CSDYHandler(ProjectInfo info) {
		super(info);
	}

	public boolean addBDCDY(String bdcdyid) {
		if (StringHelper.isEmpty(bdcdyid)) {
			return false;
		}
		String[] ids = bdcdyid.split(",");
		ResultMessage msg = new ResultMessage();
		int temp = 0;// 判断
		boolean flag = false;
		for (String id : ids) {
			if (StringHelper.isEmpty(id)) {
				continue;
			}
			// 拷贝不动产单元，生成登记单元记录
			msg = addbdcdy(id, msg);
			if (msg.getSuccess() != null && msg.getSuccess().equals("false")) {
				continue;
			} else {
				if (!flag) {
					flag = true;
				}
				temp++;
			}
			this.getCommonDao().flush();
		}
		if (temp < ids.length && temp == 0)// 一条都没有执行成功
		{
			super.setErrMessage(msg.getMsg());
			return false;
		} else if (temp < ids.length && temp != 0)// 至少有一条执行成功
		{
			String str = msg.getMsg();
			super.setErrMessage("符合登记条件的已登记，不符合登记条件的原因有：" + str);
			return true;
		} else {
			return true;
		}
	}

	/**
	 * 登簿:删除预测户权利信息；更新抵押权信息；更新初始登记信息
	 */
	public boolean writeDJB() {
		// 拷贝单元和登记单元并设定单元抵押状态为抵押中

		String strSqlXMBH = ProjectHelper.GetXMBHCondition(getXMBH());
		CommonDao dao = getCommonDao();
		List<BDCS_DJDY_GZ> djdys = dao.getDataList(BDCS_DJDY_GZ.class, strSqlXMBH);
		for (BDCS_DJDY_GZ bdcs_djdy_gz : djdys) {
			String bdcdyid = bdcs_djdy_gz.getBDCDYID();
			RealUnit _srcUnit = UnitTools.loadUnit(getBdcdylx(), DJDYLY.GZ, bdcdyid);
			RealUnit _desUnit_xz = UnitTools.copyUnit(_srcUnit, getBdcdylx(), DJDYLY.XZ);
			RealUnit _desUnit_ls = UnitTools.copyUnit(_srcUnit, getBdcdylx(), DJDYLY.LS);
			if (BDCDYLX.H.equals(getBdcdylx())) {
				House h_xz = (House) _desUnit_xz;
				h_xz.setDYZT("0");
				dao.save(h_xz);
				House h_ls = (House) _desUnit_ls;
				h_ls.setDYZT("0");
				dao.save(h_ls);
			}
			super.CopyGZDJDYToXZAndLS(bdcs_djdy_gz.getId());
		}
		return super.writeDJB();
	}

	/**
	 * 跟初始登记细微区别是判断权利类型是否为抵押权（23）的，若不是删除具体单元并更新相应的调查单元状态
	 */
	public boolean removeBDCDY(String bdcdyid) {
		// 移除单元
		BDCDYLX bdcdylx = this.getBdcdylx();
		RealUnit _srcunit = UnitTools.loadUnit(bdcdylx, DJDYLY.DC, bdcdyid);
		if (_srcunit != null) {
			if (BDCDYLX.H.equals(bdcdylx)) {
				House h = (House) _srcunit;
				h.setDJZT(DJZT.WDJ.Value);
				getCommonDao().update(h);
			}
		}
		UnitTools.deleteUnit(bdcdylx, DJDYLY.GZ, bdcdyid);
		return super.removeBDCDY(bdcdyid);
	}

	/**
	 * 1、添加不动产单元；2、拷贝在建抵押转移权利
	 * 
	 * @作者 海豹
	 * @创建时间 2015年7月23日下午9:35:29
	 * @param bdcdyid
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private ResultMessage addbdcdy(String bdcdyid, ResultMessage msg) {
		CommonDao dao = getCommonDao();
		BDCS_DJDY_GZ djdy = null;
		// 判断在登记库中是否已经存在，若存在，返回false,不能重复登记
		BDCDYLX bdcdylx = this.getBdcdylx();
		RealUnit dy = UnitTools.loadUnit(bdcdylx, DJDYLY.GZ, bdcdyid);
		if (dy != null) {
			String temp = msg.getMsg();
			if (temp == null || !temp.contains("不能重复登记同一个单元")) {
				msg.setMsg((temp == null ? "" : temp) + "不能重复登记同一个单元,");
				msg.setSuccess("false");
			}
			return msg;
		}
		// 判断调查库中是否存在具体单元信息
		RealUnit _srcunit = UnitTools.loadUnit(bdcdylx, DJDYLY.DC, bdcdyid);
		if (_srcunit == null) {
			String temp = msg.getMsg();
			if (temp == null || !temp.contains("调查库中找不到单元")) {
				msg.setMsg((temp == null ? "" : temp) + "调查库中找不到单元,");
				msg.setSuccess("false");
			}
			return msg;
		}
		// 是否关联宗地
		if (Global.ISRELATIONLAND) {
			// 判断在现状库中是否有关联的宗地，如果没有，不能进行登记
			if (bdcdylx.equals(BDCDYLX.H) || bdcdylx.equals(BDCDYLX.ZRZ) || bdcdylx.equals(BDCDYLX.LD)) {
				LandAttach _landAttach = (LandAttach) _srcunit;
				RealUnit landunit = UnitTools.loadUnit(BDCDYLX.SHYQZD, DJDYLY.XZ, _landAttach.getZDBDCDYID());
				if (landunit == null) {
					String temp = msg.getMsg();
					if (temp == null || !temp.contains("在登记库中找不到关联的宗地")) {
						msg.setMsg((temp == null ? "" : temp) + "在登记库中找不到关联的宗地,");
						msg.setSuccess("false");
					}
					return msg;
				}
			}
		}
		/************************************** 拷贝单元，创建单元、权利及附属权利 ************************************************/
		// 拷贝调查BDCDCK.BDCS_H_GZ数据
		RealUnit _desunit = UnitTools.copyUnit(_srcunit, _srcunit.getBDCDYLX(), DJDYLY.GZ);
		_desunit.setXMBH(this.getXMBH());
		djdy = super.createDJDY(_desunit, DJDYLY.GZ);
		// 生成权利信息和附属权利
		BDCS_QL_GZ ql = super.createQL(djdy, _desunit);
		BDCS_FSQL_GZ fsql = super.createFSQL(djdy.getDJDYID());
		fsql.setQLID(ql.getId());
		ql.setFSQLID(fsql.getId());

		// 如果是使用权宗地，要填写使用权面积,或者其他的
		if (_desunit.getBDCDYLX().equals(BDCDYLX.SHYQZD)) {
			fsql.setSYQMJ(((UseLand) _desunit).getZDMJ());
			ql.setQDJG(((UseLand) _desunit).getJG());
		}
		// 如果是所有权宗地，要填写使用权面积,或者其他的
		if (_desunit.getBDCDYLX().equals(BDCDYLX.SYQZD)) {
			fsql.setSYQMJ(((OwnerLand) _desunit).getZDMJ());
		}
		// 更新调查库里边相应的登记状态
		_srcunit.setDJZT(DJZT.DJZ.Value);
		dao.update(_srcunit);
		// 保存权利和附属权利
		dao.save(_desunit);
		dao.save(djdy);
		dao.save(ql);
		dao.save(fsql);
		// 把权利人拷贝过来放到申请人里面
		copyApplicant(bdcdyid, ql.getId());
		// 判断预测户是否已抵押
		// sql语句的思路是获取在建工程抵押的信息，并且条件是：YGDJZL（预告登记种类）为空，并且预测户抵押状态为0，不动产单元类型为（032）预测户的
		// ，
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
			super.CopyQLXXFromYDY(StringHelper.formatObject(listDYQ.get(0).get("QLID")), djdy);
		} else {
			// 生成抵押权
			BDCS_QL_GZ dyql = super.createQL(djdy, _desunit);
			// 生成抵押权附属权利
			BDCS_FSQL_GZ dyfsql = super.createFSQL(djdy.getDJDYID());

			// 关联抵押权权利-附属权利，设置抵押物类型、抵押不动产类型、预告登记种类
			dyql.setFSQLID(dyfsql.getId());
			dyql.setQLLX(QLLX.DIYQ.Value);
			dyfsql.setQLID(dyql.getId());
			int dysw=RightsTools.getMaxMortgageSWS(djdy.getDJDYID());
			dyfsql.setDYSW(dysw+1);
			dyfsql.setDYWLX(getDYBDCLXfromBDCDYLX(getBdcdylx()));
			dyfsql.setDYBDCLX(getDYBDCLXfromBDCDYLX(getBdcdylx()));
			dyfsql.setYGDJZL(YGDJLX.YSSPFDYQYGDJ.Value.toString());
			dao.save(dyql);
			dao.save(dyfsql);
		}
		return msg;
	}

	/**
	 * 把调查库中的权利人拷贝到申请人
	 * 
	 * @作者 刘树峰
	 * @创建时间 2015年6月30日上午12:07:26
	 * @param bdcdyid
	 */
	private void copyApplicant(String bdcdyid, String glqlid) {
		CommonDao dao = getCommonDao();
		String hql = MessageFormat.format(" QLID IN (SELECT id FROM DCS_QL_GZ WHERE DJDYID=''{0}'')", bdcdyid);
		List<DCS_QLR_GZ> dcqlrs = dao.getDataList(DCS_QLR_GZ.class, hql);
		if (dcqlrs == null || dcqlrs.size() <= 0)
			return;
		for (DCS_QLR_GZ qlr : dcqlrs) {
			//增加相同的权利人过滤，刘树峰2015年12月24日晚23点
			String zjhm = qlr.getZJH();
			boolean bexists = false;
			if (!StringHelper.isEmpty(qlr.getQLRMC())) {
				String Sql = "";
				if (!StringHelper.isEmpty(zjhm)) {
					Sql = MessageFormat.format(" XMBH=''{0}'' AND SQRXM=''{1}'' AND ZJH=''{2}''", getXMBH(), qlr.getQLRMC(), zjhm);
				} else {
					Sql = MessageFormat.format(" XMBH=''{0}'' AND SQRXM=''{1}'' AND ZJH IS NULL", getXMBH(), qlr.getQLRMC());
				}
				List<BDCS_SQR> sqrlist = getCommonDao().getDataList(BDCS_SQR.class, Sql);
				if (sqrlist != null && sqrlist.size() > 0) {
					bexists = true;
				}
			}
			if (!bexists) {
				BDCS_SQR sqr;
				sqr = ObjectHelper.copyDCQLRtoSQR(qlr);
				sqr.setXMBH(super.getXMBH());
				sqr.setGLQLID(glqlid);
				dao.save(sqr);
			}
		}
		dao.flush();
	}

}
