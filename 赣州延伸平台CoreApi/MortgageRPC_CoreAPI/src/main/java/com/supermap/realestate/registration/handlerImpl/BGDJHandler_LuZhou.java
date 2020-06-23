package com.supermap.realestate.registration.handlerImpl;

import java.text.MessageFormat;
import java.util.List;

import org.springframework.util.StringUtils;

import com.supermap.realestate.registration.ViewClass.ProjectInfo;
import com.supermap.realestate.registration.handler.DJHandler;
import com.supermap.realestate.registration.model.BDCS_DJDY_GZ;
import com.supermap.realestate.registration.model.BDCS_FSQL_GZ;
import com.supermap.realestate.registration.model.BDCS_QL_GZ;
import com.supermap.realestate.registration.model.BDCS_SHYQZD_GZ;
import com.supermap.realestate.registration.model.DCS_QL_GZ;
import com.supermap.realestate.registration.model.interfaces.House;
import com.supermap.realestate.registration.model.interfaces.RealUnit;
import com.supermap.realestate.registration.model.interfaces.UseLand;
import com.supermap.realestate.registration.tools.UnitTools;
import com.supermap.realestate.registration.util.ConstValue.BDCDYLX;
import com.supermap.realestate.registration.util.ConstValue.DJDYLY;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;

/*	
 1、国有建设用地使用权变更登记
 2、集体建设用地使用权变更登记（未配置）
 3、宅基地使用权变更登记（未配置）
 4、国有建设用地使用权/房屋所有权变更登记
 5、集体建设用地使用权/房屋所有权变更登记（未配置）
 6、宅基地使用权/房屋所有权变更登记（未配置）
 */
/**
 * 变更登记处理类
 * @ClassName: BGDJHandler
 * @author liushufeng
 * @date 2015年8月12日 下午3:14:15
 */
public class BGDJHandler_LuZhou extends BGDJHandler implements DJHandler {

	/**
	 * 构造函数
	 * 
	 * @param info
	 */
	public BGDJHandler_LuZhou(ProjectInfo info) {
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
				// 变更前
				BDCS_DJDY_GZ djdy = super.createDJDYfromXZ(id);
				if (djdy != null) {
					dao.save(djdy);
				}
				if (djdy != null && !StringHelper.isEmpty(djdy.getDJDYID())) {
				}
			} else {
				dy = UnitTools.copyUnit(this.getBdcdylx(), DJDYLY.DC, DJDYLY.GZ, id);
				dy.setXMBH(super.getXMBH());
				// 登记单元索引表
				BDCS_DJDY_GZ djdy = super.createDJDYfromGZ(dy);
				if (djdy != null) {
					BDCS_QL_GZ ql = super.createQL(djdy, dy);
					BDCS_FSQL_GZ fsql = super.createFSQL(djdy.getDJDYID());
					ql.setFSQLID(fsql.getId());
					fsql.setQLID(ql.getId());

					// 如果是使用权宗地，要填写使用权面积,或者其他的
					if (djdy.getBDCDYLX().equals(BDCDYLX.SHYQZD.Value)) {
						BDCS_SHYQZD_GZ shyqzd = (BDCS_SHYQZD_GZ) dy;
						if (shyqzd != null) {
							fsql.setSYQMJ(shyqzd.getZDMJ());
						}
					}
					// 泸州版本：变更后的，如果是使用权宗地，从权籍调查表里边取，如果是房屋，就取房屋的起止时间
					if (dy instanceof UseLand) {
						String sql = MessageFormat.format(" DJDYID=''{0}''", bdcdyid);
						List<DCS_QL_GZ> dcqls = dao.getDataList(DCS_QL_GZ.class, sql);
						if (dcqls != null && dcqls.size() > 0) {
							DCS_QL_GZ dcql = dcqls.get(0);
							if (dcql != null) {
								ql.setQLQSSJ(dcql.getQLQSSJ());
								ql.setQLJSSJ(dcql.getQLJSSJ());
							}
						}
					} else if (dy instanceof House) {
						House house = (House) dy;
						if (house != null) {
							ql.setQLQSSJ(house.getTDSYQQSRQ());
							ql.setQLJSSJ(house.getTDSYQZZRQ());
						}
					}
					dao.save(djdy);
					dao.save(ql);
					dao.save(fsql);
				}
			}
			dao.save(dy);
			dao.flush();
		}
		return true;
	}

}
