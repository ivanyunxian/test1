package com.supermap.realestate.registration.handlerImpl;

import java.util.List;
import java.util.Map;

import com.supermap.realestate.registration.ViewClass.ProjectInfo;
import com.supermap.realestate.registration.ViewClass.UnitStatus;
import com.supermap.realestate.registration.handler.DJHandler;
import com.supermap.realestate.registration.model.BDCS_DJDY_GZ;
import com.supermap.realestate.registration.model.BDCS_DYXZ;
import com.supermap.realestate.registration.model.BDCS_FSQL_GZ;
import com.supermap.realestate.registration.model.BDCS_QL_GZ;
import com.supermap.realestate.registration.model.BDCS_XMXX;
import com.supermap.realestate.registration.model.WFD_MAPPING;
import com.supermap.realestate.registration.model.interfaces.House;
import com.supermap.realestate.registration.model.interfaces.RealUnit;
import com.supermap.realestate.registration.model.interfaces.Rights;
import com.supermap.realestate.registration.tools.RightsTools;
import com.supermap.realestate.registration.tools.UnitTools;
import com.supermap.realestate.registration.util.ProjectHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.realestate.registration.util.ConstValue.BDCDYLX;
import com.supermap.realestate.registration.util.ConstValue.CZFS;
import com.supermap.realestate.registration.util.ConstValue.DJDYLY;
import com.supermap.realestate.registration.util.ConstValue.DJLX;
import com.supermap.realestate.registration.util.ConstValue.DJZT;
import com.supermap.realestate.registration.util.ConstValue.QLLX;
import com.supermap.realestate.registration.util.ConstValue.YGDJLX;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import com.supermap.wisdombusiness.web.ResultMessage;

/*
 1、商品房预告登记+商品房抵押预告登记（待测）
 */
/**
 * 
 * 预告登记+预抵押登记操作类(泸州特殊，起止时间要从地的权利里边去读)
 * @ClassName: YGYDYDJHandler
 * @author liushufeng
 * @date 2015年9月8日 下午10:43:16
 */
public class YGYDYDJHandler_LuZhou extends YGYDYDJHandler implements DJHandler {

	/**
	 * 构造函数
	 * @param info
	 */
	public YGYDYDJHandler_LuZhou(ProjectInfo info) {
		super(info);
	}

	/**
	 * 添加不动产单元
	 */
	@Override
	public boolean addBDCDY(String bdcdyid) {
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
			BDCS_DJDY_GZ djdy = super.createDJDYfromXZ(bdcdyid);
			if (djdy == null) {
				String djdyid = getPrimaryKey();
				djdy = new BDCS_DJDY_GZ();
				djdy.setDJDYID(djdyid);
				djdy.setBDCDYID(_srcUnit.getId());
				djdy.setBDCDYH(_srcUnit.getBDCDYH());
				djdy.setLY(DJDYLY.XZ.Value);
				djdy.setBDCDYLX(this.getBdcdylx().Value);
				djdy.setXMBH(this.getXMBH());
			}
			if (djdy != null) {
				// 生成预告权利信息
				BDCS_QL_GZ ql = super.createQL(djdy, _srcUnit);
				
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
				// 生成预告附属权利
				BDCS_FSQL_GZ fsql = super.createFSQL(djdy.getDJDYID());
				// 生成抵押权
				BDCS_QL_GZ dyql = super.createQL(djdy, _srcUnit);
				// 生成抵押权附属权利
				BDCS_FSQL_GZ dyfsql = super.createFSQL(djdy.getDJDYID());

				// 关联预告权利-附属权利，并设定权利类型、预告登记种类
				ql.setFSQLID(fsql.getId());
				ql.setQLLX(getQllx().Value);// 预告登记+预抵押登记 设置 权利类型时 设置为
											// 国有建设用地使用权/房屋（构筑物）所有权
				fsql.setYGDJZL(YGDJLX.YSSPFMMYGDJ.Value.toString());
				fsql.setQLID(ql.getId());

				// 关联抵押权权利-附属权利，设置抵押物类型、抵押不动产类型、预告登记种类
				dyql.setFSQLID(dyfsql.getId());
				dyql.setQLLX(QLLX.DIYQ.Value);
				dyfsql.setQLID(dyql.getId());
				dyql.setCZFS(CZFS.GTCZ.Value);//设置持证方式为共同持证。
				dyfsql.setDYWLX(getDYBDCLXfromBDCDYLX(getBdcdylx()));
				int dysw=RightsTools.getMaxMortgageSWS(djdy.getDJDYID());
				dyfsql.setDYSW(dysw+1);
				dyfsql.setDYBDCLX(getDYBDCLXfromBDCDYLX(getBdcdylx()));
				dyfsql.setYGDJZL(YGDJLX.YSSPFDYQYGDJ.Value.toString());
				_srcUnit.setDJZT(DJZT.DJZ.Value); // 设置登记状态

				// 泸州版本：如果是房屋，权利起止就取房屋的起止时间
				House house = (House) _srcUnit;
				if (house != null) {
					ql.setQLQSSJ(house.getTDSYQQSRQ());
					ql.setQLJSSJ(house.getTDSYQZZRQ());
				}
				// 保存
				dao.update(_srcUnit);
				dao.save(djdy);
				dao.save(ql);
				dao.save(fsql);
				dao.save(dyql);
				dao.save(dyfsql);
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
