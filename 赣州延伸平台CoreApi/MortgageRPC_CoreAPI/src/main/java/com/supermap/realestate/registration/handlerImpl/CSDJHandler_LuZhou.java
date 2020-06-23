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
import com.supermap.realestate.registration.model.DCS_QL_GZ;
import com.supermap.realestate.registration.model.interfaces.House;
import com.supermap.realestate.registration.model.interfaces.LandAttach;
import com.supermap.realestate.registration.model.interfaces.OwnerLand;
import com.supermap.realestate.registration.model.interfaces.RealUnit;
import com.supermap.realestate.registration.model.interfaces.UseLand;
import com.supermap.realestate.registration.tools.UnitTools;
import com.supermap.realestate.registration.util.ConstValue.BDCDYLX;
import com.supermap.realestate.registration.util.ConstValue.DJDYLY;
import com.supermap.realestate.registration.util.ConstValue.DJZT;
import com.supermap.realestate.registration.util.Global;
import com.supermap.realestate.registration.util.ObjectHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import com.supermap.wisdombusiness.web.ResultMessage;
import com.thoughtworks.xstream.mapper.Mapper.Null;

/*
 1、集体土地所有权初始登记（待测）
 2、集体土地使用权初始登记（待测）
 3、宅基地使用权初始登记（待测）
 4、国有建设用地使用权初始登记
 5、国有建设用地使用权/房屋（按户，按幢）所有权初始登记
 6、集体建设用地使用权/房屋所有权初始登记（待测）
 7、宅基地使用权/房屋所有权初始登记（待测）
 8、林地使用权初始登记（待测）
 9、森林林木所有权初始登记（待测）
 10、宗海使用权初始登记（待测）
 */
/**
 * 
 * 初始登记处理类（泸州）
 * 
 * @ClassName: CSDJHandler_LuZhou
 * @author liushufeng
 * @date 2015年9月8日 下午10:29:35
 */
public class CSDJHandler_LuZhou extends CSDJHandler implements DJHandler {

	/**
	 * 构造函数
	 * 
	 * @param info
	 */
	public CSDJHandler_LuZhou(ProjectInfo info) {
		super(info);
	}

	/**
	 * 添加不动产单元
	 */
	@Override
	public boolean addBDCDY(String bdcdyid) {
		if (StringHelper.isEmpty(bdcdyid))
			return false;
		String[] ids = bdcdyid.split(",");
		if (ids == null || ids.length <= 0)
			return false;
		for (String id : ids) {
			if (StringHelper.isEmpty(id))
				continue;
			// 拷贝不动产单元，生成登记单元记录,同时把权利人拷贝过来放到申请人里边
			ResultMessage msg = addbdcdy(id);

			if (msg.getSuccess().equals("false")) {
				super.setErrMessage(msg.getMsg());
				return false;
			}
		}
		this.getCommonDao().flush();
		return true;
	}

	/**
	 * 把调查库中的权利人拷贝到申请人
	 * 
	 * @作者 刘树峰
	 * @创建时间 2015年6月30日上午12:07:26
	 * @param bdcdyid
	 */
	private void copyApplicant(String bdcdyid, String qlid) {
		String hql = MessageFormat.format(" QLID IN (SELECT id FROM DCS_QL_GZ WHERE DJDYID=''{0}'')", bdcdyid);
		List<DCS_QLR_GZ> dcqlrs = getCommonDao().getDataList(DCS_QLR_GZ.class, hql);
		if (dcqlrs == null || dcqlrs.size() <= 0)
			return;
		for (DCS_QLR_GZ qlr : dcqlrs) {
			String zjhm = qlr.getZJH();
			if (!StringUtils.isEmpty(zjhm)) {
				String hql2 = MessageFormat.format(" ZJH =''{0}'' AND XMBH=''{1}''", zjhm, super.getXMBH());
				List<BDCS_SQR> sqrs = getCommonDao().getDataList(BDCS_SQR.class, hql2);
				if (sqrs != null && sqrs.size() > 0)
					return;
			}
			BDCS_SQR sqr;
			sqr = ObjectHelper.copyDCQLRtoSQR(qlr);
			sqr.setXMBH(super.getXMBH());
			sqr.setGLQLID(qlid);
			getCommonDao().save(sqr);
		}
	}

	/**
	 * 内部方法：添加不动产单元
	 * 
	 * @Title: addbdcdy
	 * @author:liushufeng
	 * @date：2015年7月19日 上午3:08:02
	 * @param bdcdyid
	 * @return
	 */
	private ResultMessage addbdcdy(String bdcdyid) {
		ResultMessage msg = new ResultMessage();
		BDCS_DJDY_GZ djdy = null;
		CommonDao dao = this.getCommonDao();

		// 判断在登记库中是否已经存在了，如果存在，返回false，不能重复登记
		RealUnit dy = UnitTools.loadUnit(this.getBdcdylx(), DJDYLY.GZ, bdcdyid);
		if (dy != null) {
			msg.setSuccess("false");
			msg.setMsg("不能重复登记同一个单元！");
			return msg;
		}
		BDCDYLX bdcdylx = this.getBdcdylx();
		RealUnit _srcunit = UnitTools.loadUnit(bdcdylx, DJDYLY.DC, bdcdyid);
		if (_srcunit == null) {
			msg.setSuccess("false");
			msg.setMsg("找不到单元！");
			return msg;
		}
		// String zdbdcdyid = null;
		// 如果是户或者自然幢，要判断在现状库中是否有关联的宗地，如果没有，不能进行登记
		if (Global.ISRELATIONLAND) // 是否关联宗地
		{
			if (bdcdylx.equals(BDCDYLX.H) || bdcdylx.equals(BDCDYLX.ZRZ) || bdcdylx.equals(BDCDYLX.LD)) {
				LandAttach _landAttach = (LandAttach) _srcunit;
				RealUnit zdunit = UnitTools.loadUnit(BDCDYLX.SHYQZD, DJDYLY.XZ, _landAttach.getZDBDCDYID());
				if (zdunit == null) {
					msg.setSuccess("false");
					msg.setMsg("在登记库中找不到关联的宗地！");
					return msg;
				}
				// zdbdcdyid = zdunit.getId();
			}
		}
		RealUnit _desunit = null;
		_desunit = UnitTools.copyUnit(_srcunit, _srcunit.getBDCDYLX(), DJDYLY.GZ);
		_desunit.setXMBH(this.getXMBH());
		djdy = super.createDJDY(_desunit, DJDYLY.GZ);

		// 生成权利信息和附属权利
		BDCS_QL_GZ ql = super.createQL(djdy, _desunit);
		BDCS_FSQL_GZ fsql = super.createFSQL(djdy.getDJDYID());
		fsql.setQLID(ql.getId());
		ql.setFSQLID(fsql.getId());

		
		// 添加网签合同号及买受人，bdcdylx=031 实测户 && qllx=4/6/8 && 首次登记:DJLX=100
		String qllx = this.getQllx().Value;	
		if (("4".equals(qllx) || "6".equals(qllx)
				|| "8".equals(qllx)) && "031".equals(this.getBdcdylx().Value) && "100".equals(this.getDjlx().Value)) {
			String sql = "SELECT HTH,MSR FROM BDCDCK.BDCS_NETSIGN WHERE BDCDYID=''{0}''";
			sql = MessageFormat.format(sql, djdy.getBDCDYID());
			List<Map> netSign = dao.getDataListByFullSql(sql);
			if (netSign != null && netSign.size() > 0) {
				ql.setHTH(
						StringHelper.isEmpty(netSign.get(0).get("HTH")) ? null : netSign.get(0).get("HTH").toString());
				ql.setMSR(
						StringHelper.isEmpty(netSign.get(0).get("MSR")) ? null : netSign.get(0).get("MSR").toString());
			}
		}

		// 泸州版本：如果是使用权宗地，从权籍调查表里边取，如果是房屋，就取房屋的起止时间
		if (_srcunit instanceof UseLand) {
			String sql = MessageFormat.format(" DJDYID=''{0}''", bdcdyid);
			List<DCS_QL_GZ> dcqls = dao.getDataList(DCS_QL_GZ.class, sql);
			if (dcqls != null && dcqls.size() > 0) {
				DCS_QL_GZ dcql = dcqls.get(0);
				if (dcql != null) {
					ql.setQLQSSJ(dcql.getQLQSSJ());
					ql.setQLJSSJ(dcql.getQLJSSJ());
				}
			}
		} else if (_srcunit instanceof House) {
			House house = (House) _srcunit;
			if (house != null) {
				ql.setQLQSSJ(house.getTDSYQQSRQ());
				ql.setQLJSSJ(house.getTDSYQZZRQ());
			}
		}

		// 如果是使用权宗地，要填写使用权面积,或者其他的
		if (_desunit.getBDCDYLX().equals(BDCDYLX.SHYQZD)) {
			fsql.setSYQMJ(((UseLand) _desunit).getZDMJ());
			ql.setQDJG(((UseLand) _desunit).getJG());

			// 泸州特有，要把权籍调查里边的使用权起始时间和结束时间拷贝过来
			String hql = MessageFormat.format(" DJDYID=''{0}''", bdcdyid);
			List<DCS_QL_GZ> dcqls = getCommonDao().getDataList(DCS_QL_GZ.class, hql);
			if (dcqls != null && dcqls.size() > 0) {
				DCS_QL_GZ dcql = dcqls.get(0);
				if (dcql != null) {
					ql.setQLQSSJ(dcql.getQLQSSJ());
					ql.setQLJSSJ(dcql.getQLJSSJ());
				}
			}
		}
		// 如果是所有权宗地，要填写使用权面积,或者其他的
		if (_desunit.getBDCDYLX().equals(BDCDYLX.SYQZD)) {
			fsql.setSYQMJ(((OwnerLand) _desunit).getZDMJ());
		}
		// 更新调查库里边相应的登记状态
		_srcunit.setDJZT(DJZT.DJZ.Value);

		copyApplicant(bdcdyid, ql.getId());

		dao.update(_srcunit);

		// 保存权利和附属权利
		dao.save(_desunit);
		dao.save(djdy);
		dao.save(ql);
		dao.save(fsql);
		msg.setSuccess("true");
		return msg;
	}
}
