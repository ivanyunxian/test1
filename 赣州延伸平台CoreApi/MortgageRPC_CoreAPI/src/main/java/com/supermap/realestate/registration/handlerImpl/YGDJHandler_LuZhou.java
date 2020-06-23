package com.supermap.realestate.registration.handlerImpl;

import java.util.List;
import java.util.Map;

import com.supermap.realestate.registration.ViewClass.ProjectInfo;
import com.supermap.realestate.registration.ViewClass.UnitStatus;
import com.supermap.realestate.registration.handler.DJHandler;
import com.supermap.realestate.registration.model.BDCS_DJDY_GZ;
import com.supermap.realestate.registration.model.BDCS_DJDY_XZ;
import com.supermap.realestate.registration.model.BDCS_DYXZ;
import com.supermap.realestate.registration.model.BDCS_FSQL_GZ;
import com.supermap.realestate.registration.model.BDCS_QL_GZ;
import com.supermap.realestate.registration.model.BDCS_SHYQZD_XZ;
import com.supermap.realestate.registration.model.BDCS_XMXX;
import com.supermap.realestate.registration.model.WFD_MAPPING;
import com.supermap.realestate.registration.model.interfaces.House;
import com.supermap.realestate.registration.model.interfaces.RealUnit;
import com.supermap.realestate.registration.model.interfaces.Rights;
import com.supermap.realestate.registration.tools.RightsTools;
import com.supermap.realestate.registration.tools.UnitTools;
import com.supermap.realestate.registration.util.ConstValue.BDCDYLX;
import com.supermap.realestate.registration.util.ConstValue.DJDYLY;
import com.supermap.realestate.registration.util.ConstValue.DJLX;
import com.supermap.realestate.registration.util.ConstValue.DJZT;
import com.supermap.realestate.registration.util.ConstValue.QLLX;
import com.supermap.realestate.registration.util.ConstValue.YGDJLX;
import com.supermap.realestate.registration.util.ProjectHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.wisdombusiness.core.SuperSpringContext;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import com.supermap.wisdombusiness.web.ResultMessage;

/*
 1、商品房预告登记
 */
/**
 * 
 * 预告登记处理类
 * @ClassName: YGDJHandler
 * @author liushufeng
 * @date 2015年9月8日 下午10:42:38
 */
public class YGDJHandler_LuZhou extends YGDJHandler implements DJHandler {

	/**
	 * 构造函数
	 * 
	 * @param info
	 */
	public YGDJHandler_LuZhou(ProjectInfo info) {
		super(info);
	}

	/**
	 * 添加不动产单元
	 */
	@Override
	public boolean addBDCDY(String bdcdyid) {
		boolean bsuccess = true;
		if (!StringHelper.isEmpty(bdcdyid)) {
			String[] bdcdyids = bdcdyid.split(",");
			if (bdcdyids != null && bdcdyids.length > 0) {
				for (String id : bdcdyids) {
					if (bsuccess) {
						bsuccess = addbcdy(id);
					}
				}
			}
		}
		return bsuccess;
	}

	@Override
	public boolean addbcdy(String bdcdyid) {
		boolean bsuccess = false;
		CommonDao dao = getCommonDao();
		ResultMessage msg = new ResultMessage();
		RealUnit _srcUnit = UnitTools.loadUnit(this.getBdcdylx(), DJDYLY.XZ, bdcdyid);
		if (_srcUnit == null) {
			msg.setSuccess("false");
			msg.setMsg("找不到单元");
			return false;
		}
		if (_srcUnit != null) {

			BDCS_DJDY_GZ djdy = null;
			djdy = super.createDJDYfromXZ(bdcdyid);
			if (djdy == null) {
				djdy = createDJDYfromXZ(bdcdyid, _srcUnit);
			}
			if (djdy != null) {
				// 生成权利信息
				BDCS_QL_GZ ql = super.createQL(djdy, _srcUnit);
				// 生成附属权利
				BDCS_FSQL_GZ fsql = super.createFSQL(djdy.getDJDYID());

				ql.setFSQLID(fsql.getId());
				fsql.setQLID(ql.getId());
				fsql.setYGDJZL(YGDJLX.YSSPFMMYGDJ.Value.toString());

				// 如果是使用权宗地，把使用权面积加上
				if (getBdcdylx().equals(BDCDYLX.SHYQZD)) {
					BDCS_SHYQZD_XZ xzshyqzd = dao.get(BDCS_SHYQZD_XZ.class, bdcdyid);
					if (xzshyqzd != null) {
						fsql.setSYQMJ(xzshyqzd.getZDMJ());
					}
					ql.setQLLX(QLLX.GYJSYDSHYQ.Value);// 预告登记 设置 权利类型时 设置为
														// 国有建设用地使用权
				} else if (getBdcdylx().equals(BDCDYLX.YCH)) {
					ql.setQLLX(QLLX.GYJSYDSHYQ_FWSYQ.Value);// 预告登记 设置 权利类型时 设置为
															// 国有建设用地使用权/房屋（构筑物）所有权
				}

				// 泸州版本：起止时间的处理，把房屋里边的起止时间赋给权利起止时间
				if (_srcUnit instanceof House) {
					House house = (House) _srcUnit;
					if (house != null) {
						ql.setQLQSSJ(house.getTDSYQQSRQ());
						ql.setQLJSSJ(house.getTDSYQZZRQ());
					}
				}

				_srcUnit.setDJZT(DJZT.DJZ.Value); // 设置登记状态

				String fj = "";
				BDCS_XMXX xmxx = getCommonDao().get(BDCS_XMXX.class, getXMBH());
				String workflowcode = ProjectHelper.getWorkflowCodeByProjectID(xmxx.getPROJECT_ID());
				String sql = " WORKFLOWCODE='" + workflowcode + "'";;
				List<WFD_MAPPING> mappings = dao.getDataList(WFD_MAPPING.class, sql);
				if (mappings != null && mappings.size() > 0) {
					WFD_MAPPING maping = mappings.get(0);
					if (("1").equals(maping.getISINITATATUS())){
						fj = ql.getFJ();
						fj = getStatus(fj, ql.getDJDYID(), bdcdyid, this.getBdcdylx().Value);
						ql.setFJ(fj);
					}
				}
				
				// 保存
				dao.update(_srcUnit);
				dao.save(djdy);
				dao.save(ql);
				dao.save(fsql);
			}
			dao.flush();
			bsuccess = true;
		}
		return bsuccess;
	}

	@SuppressWarnings("rawtypes")
	private String getStatus(String fj, String djdyid, String bdcdyid, String bdcdylx) {
		UnitStatus status = new UnitStatus();
		// 在办状态
		StringBuilder builder = new StringBuilder();
		builder.append("SELECT XMXX.YWLSH AS YWLSH,XMXX.DJLX AS XMDJLX,XMXX.QLLX AS XMQLLX, ");
		builder.append("QL.DJLX AS QLDJLX,QL.QLLX AS QLQLLX ");
		builder.append("FROM BDCK.BDCS_QL_GZ QL ");
		builder.append("LEFT JOIN BDCK.BDCS_XMXX XMXX ");
		builder.append("ON QL.XMBH=XMXX.XMBH ");
		builder.append("WHERE XMXX.DJLX<>'400' AND NVL2(XMXX.SFDB,'1','0')<>'1' ");
		builder.append("AND QL.DJDYID='" + djdyid + "' ");
		List<Map> qls_gz = getCommonDao().getDataListByFullSql(builder.toString());
		for (Map ql : qls_gz) {
			String xmdjlx = StringHelper.formatDouble(ql.get("XMDJLX"));
			String xmqllx = StringHelper.formatDouble(ql.get("XMQLLX"));
			if (DJLX.CFDJ.Value.equals(xmdjlx)) {
				if ("98".equals(xmqllx)) {
					status.setSeizureState("正在办理查封");
				}
			}
			if (DJLX.YYDJ.Value.equals(xmdjlx)) {
				status.setObjectionState("正在办理异议");
			} else if (DJLX.YGDJ.Value.equals(xmdjlx)) {
				if (QLLX.QTQL.Value.equals(xmqllx)) {
					status.setTransferNoticeState("正在办理转移预告");
				} else if (QLLX.DIYQ.Value.equals(xmqllx)) {
					if (BDCDYLX.YCH.Value.equals(bdcdylx)) {
						status.setMortgageState("正在办理抵押");
					} else {
						status.setMortgageNoticeState("正在办理抵押预告");
					}
				}
			} else if (QLLX.DIYQ.Value.equals(xmqllx)) {
				status.setMortgageState("正在办理抵押");
			}
		}
		// 已办状态
		List<Rights> qls_xz = RightsTools.loadRightsByCondition(DJDYLY.XZ, "DJDYID='" + djdyid + "'");
		for (Rights ql : qls_xz) {
			String djlx = ql.getDJLX();
			String qllx = ql.getQLLX();
			if (DJLX.CFDJ.Value.equals(djlx)) {
				status.setSeizureState("已查封");
			}
			if (DJLX.YYDJ.Value.equals(djlx)) {
				status.setObjectionState("已异议");
			} else if (DJLX.YGDJ.Value.equals(djlx)) {
				if (QLLX.QTQL.Value.equals(qllx)) {
					status.setTransferNoticeState("已转移预告");
				} else if (QLLX.DIYQ.Value.equals(qllx)) {
					if (BDCDYLX.YCH.Value.equals(bdcdylx)) {
						status.setMortgageState("已抵押");
					} else {
						status.setMortgageNoticeState("已抵押预告");
					}
				}
			} else if (QLLX.DIYQ.Value.equals(qllx)) {
				status.setMortgageState("已抵押");
			}
		}

		List<BDCS_DYXZ> list_limit = getCommonDao().getDataList(BDCS_DYXZ.class,
				"YXBZ<>'2' AND BDCDYID='" + bdcdyid + "' AND BDCDYLX='" + bdcdylx + "' ORDER BY YXBZ ");
		if (list_limit != null && list_limit.size() > 0) {
			for (BDCS_DYXZ limit : list_limit) {
				if ("1".equals(limit.getYXBZ())) {
					status.setLimitState("已限制");
				} else {
					status.setLimitState("正在办理限制");
				}
			}
		}
		String tmp = fj;
		if(StringHelper.isEmpty(tmp)){
			if(status.getMortgageState().contains("已")||status.getMortgageState().contains("正在"))
				tmp = status.getMortgageState() + "," ;
			if(status.getLimitState().contains("已")||status.getLimitState().contains("正在"))
				tmp += status.getLimitState();
			fj = tmp;
		}else{
			if(status.getMortgageState().contains("已")||status.getMortgageState().contains("正在"))
				tmp = status.getMortgageState() + "," ;
			if(status.getLimitState().contains("已")||status.getLimitState().contains("正在"))
				tmp += status.getLimitState();
			fj += tmp;
		}
		return fj;
	}
}
