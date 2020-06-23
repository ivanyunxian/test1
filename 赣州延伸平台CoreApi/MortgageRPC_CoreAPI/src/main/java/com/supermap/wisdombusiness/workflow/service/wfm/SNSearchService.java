package com.supermap.wisdombusiness.workflow.service.wfm;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.supermap.realestate.registration.model.BDCS_XMXX;
import com.supermap.realestate.registration.model.xmlmodel.bdcdy.H;
import com.supermap.realestate.registration.util.ConfigHelper;
import com.supermap.realestate.registration.util.JH_DBHelper;
import com.supermap.wisdombusiness.workflow.dao.CommonDao;
import com.supermap.wisdombusiness.workflow.model.Bank_Trustbook;

@Service("sNSearchService")
public class SNSearchService {
	@Autowired
	private CommonDao commonDao;
	//根据权利人名称查询该权利人对应“权利”信息
	public ResultSet GetQlrmcsearch(String qlrmc,String checked, String starttime, String endtime) throws SQLException {
		Connection jyConnection = null;
		String qlrsql = "select * from QLR  WHERE QLRMC = '" + qlrmc + "'";
		jyConnection = JH_DBHelper.getConnect_jy();
		ResultSet qlrset = JH_DBHelper.excuteQuery(jyConnection, qlrsql);
		qlrset.next();
		String YWH=qlrset.getString("YWH");
		String slsqsql = "select * from SLSQ WHERE YWH = '" + YWH + "'";
		//增加时间段统计和问题件统计
		if (starttime != null && !starttime.equals("")) {
			slsqsql = slsqsql + " and SLSJ>to_date('" + starttime
					+ "', 'yyyy-mm-dd hh24:mi:ss') ";
		}
		if (endtime != null && !endtime.equals("")) {
			slsqsql = slsqsql + " and SLSJ<to_date('" + endtime
					+ "', 'yyyy-mm-dd hh24:mi:ss') ";
		}
		if (checked != null && !checked.equals("")) {
			if(checked.equals("1")){
				slsqsql = slsqsql + " and SFWTAJ='1'";
			}
		}
		ResultSet slsqset = JH_DBHelper.excuteQuery(jyConnection, slsqsql);
		try {
			if (slsqset.next()) {
				String DJDL = slsqset.getString("DJDL");
				String DJXL = slsqset.getString("DJXL");
				// 抵押权
				if ((DJDL.equals("100") && DJXL.equals("1"))
						|| (DJDL.equals("100") && DJXL.equals("3"))
						|| (DJDL.equals("100") && DJXL.equals("6"))
						|| (DJDL.equals("100") && DJXL.equals("7"))
						|| (DJDL.equals("100") && DJXL.equals("9"))
						|| (DJDL.equals("200") && DJXL.equals("2"))
						|| (DJDL.equals("200") && DJXL.equals("3"))
						|| (DJDL.equals("200") && DJXL.equals("5"))
						|| (DJDL.equals("200") && DJXL.equals("6"))) {
					String dyaqsql = "select * from DYAQ WHERE YWH = '" + YWH
							+ "'";
					ResultSet dyaqset = JH_DBHelper.excuteQuery(jyConnection,
							dyaqsql);
					return dyaqset;
				}
				// 房地产权
				else if ((DJDL.equals("200") && DJXL.equals("4"))
						|| (DJDL.equals("200") && DJXL.equals("5"))) {
					String fdcqsql = "select * from FDCQ2 WHERE YWH = '" + YWH
							+ "'";
					ResultSet fdcqset = JH_DBHelper.excuteQuery(jyConnection,
							fdcqsql);
					return fdcqset;
				}
				// 预告登记
				else if ((DJDL.equals("200") && DJXL.equals("1"))
						|| (DJDL.equals("700") && DJXL.equals("3"))) {
					String fdcqsql = "select * from YGDJ WHERE YWH = '" + YWH
							+ "'";
					ResultSet fdcqset = JH_DBHelper.excuteQuery(jyConnection,
							fdcqsql);
					return fdcqset;
				}
				// 查封登记
				else if ((DJDL.equals("800") && DJXL.equals("1"))
						|| (DJDL.equals("800") && DJXL.equals("2"))
						|| (DJDL.equals("800") && DJXL.equals("3"))) {
					String fdcqsql = "select * from CFDJ WHERE YWH = '" + YWH
							+ "'";
					ResultSet fdcqset = JH_DBHelper.excuteQuery(jyConnection,
							fdcqsql);
					return fdcqset;
				}
				// 预售许可
				else if ((DJDL.equals("1200") && DJXL.equals("1"))
						|| (DJDL.equals("1200") && DJXL.equals("2"))
						|| (DJDL.equals("1200") && DJXL.equals("3"))
						|| (DJDL.equals("1200") && DJXL.equals("4"))
						|| (DJDL.equals("1200") && DJXL.equals("5"))) {
					String fdcqsql = "select * from YSXK WHERE YWH = '" + YWH
							+ "'";
					ResultSet fdcqset = JH_DBHelper.excuteQuery(jyConnection,
							fdcqsql);
					return fdcqset;
				}
				// 签约备案信息
				else if ((DJDL.equals("1200") && DJXL.equals("9"))
						|| (DJDL.equals("1200") && DJXL.equals("10"))
						|| (DJDL.equals("1200") && DJXL.equals("11"))
						|| (DJDL.equals("1200") && DJXL.equals("14"))
						|| (DJDL.equals("1200") && DJXL.equals("15"))
						|| (DJDL.equals("1200") && DJXL.equals("16"))
						|| (DJDL.equals("1200") && DJXL.equals("19"))
						|| (DJDL.equals("1200") && DJXL.equals("20"))
						|| (DJDL.equals("1200") && DJXL.equals("21"))) {
					String fdcqsql = "select * from QYBAXX WHERE YWH = '" + YWH
							+ "'";
					ResultSet fdcqset = JH_DBHelper.excuteQuery(jyConnection,
							fdcqsql);
					return fdcqset;
				} else {
					return null;
				}
			} else {
				return slsqset;
			}
		} catch (Exception e) {
			return null;
		}
	}
	//根据坐落查询该坐落对应“权利”信息
	public ResultSet GetZlsearch(String zl,String checked, String starttime, String endtime) throws SQLException {
		Connection jyConnection = null;
		String slsqsql = "select * from SLSQ  WHERE ZL like '%" + zl + "%'";
		//增加时间段统计和问题件统计
				if (starttime != null && !starttime.equals("")) {
					slsqsql = slsqsql + " and SLSJ>to_date('" + starttime
							+ "', 'yyyy-mm-dd hh24:mi:ss') ";
				}
				if (endtime != null && !endtime.equals("")) {
					slsqsql = slsqsql + " and SLSJ<to_date('" + endtime
							+ "', 'yyyy-mm-dd hh24:mi:ss') ";
				}
				if (checked != null && !checked.equals("")) {
					if(checked.equals("1")){
						slsqsql = slsqsql + " and SFWTAJ='1'";
					}
				}
		jyConnection = JH_DBHelper.getConnect_jy();
		ResultSet slsqset = JH_DBHelper.excuteQuery(jyConnection,slsqsql);
		try {
			if (slsqset.next()) {
				String YWH=slsqset.getString("YWH");
				String DJDL = slsqset.getString("DJDL");
				String DJXL = slsqset.getString("DJXL");
				// 抵押权
				if ((DJDL.equals("100") && DJXL.equals("1"))
						|| (DJDL.equals("100") && DJXL.equals("3"))
						|| (DJDL.equals("100") && DJXL.equals("6"))
						|| (DJDL.equals("100") && DJXL.equals("7"))
						|| (DJDL.equals("100") && DJXL.equals("9"))
						|| (DJDL.equals("200") && DJXL.equals("2"))
						|| (DJDL.equals("200") && DJXL.equals("3"))
						|| (DJDL.equals("200") && DJXL.equals("5"))
						|| (DJDL.equals("200") && DJXL.equals("6"))) {
					String dyaqsql = "select * from DYAQ WHERE YWH = '" + YWH
							+ "'";
					ResultSet dyaqset = JH_DBHelper.excuteQuery(jyConnection,
							dyaqsql);
					return dyaqset;
				}
				// 房地产权
				else if ((DJDL.equals("200") && DJXL.equals("4"))
						|| (DJDL.equals("200") && DJXL.equals("5"))) {
					String fdcqsql = "select * from FDCQ2 WHERE YWH = '" + YWH
							+ "'";
					ResultSet fdcqset = JH_DBHelper.excuteQuery(jyConnection,
							fdcqsql);
					return fdcqset;
				}
				// 预告登记
				else if ((DJDL.equals("200") && DJXL.equals("1"))
						|| (DJDL.equals("700") && DJXL.equals("3"))) {
					String fdcqsql = "select * from YGDJ WHERE YWH = '" + YWH
							+ "'";
					ResultSet fdcqset = JH_DBHelper.excuteQuery(jyConnection,
							fdcqsql);
					return fdcqset;
				}
				// 查封登记
				else if ((DJDL.equals("800") && DJXL.equals("1"))
						|| (DJDL.equals("800") && DJXL.equals("2"))
						|| (DJDL.equals("800") && DJXL.equals("3"))) {
					String fdcqsql = "select * from CFDJ WHERE YWH = '" + YWH
							+ "'";
					ResultSet fdcqset = JH_DBHelper.excuteQuery(jyConnection,
							fdcqsql);
					return fdcqset;
				}
				// 预售许可
				else if ((DJDL.equals("1200") && DJXL.equals("1"))
						|| (DJDL.equals("1200") && DJXL.equals("2"))
						|| (DJDL.equals("1200") && DJXL.equals("3"))
						|| (DJDL.equals("1200") && DJXL.equals("4"))
						|| (DJDL.equals("1200") && DJXL.equals("5"))) {
					String fdcqsql = "select * from YSXK WHERE YWH = '" + YWH
							+ "'";
					ResultSet fdcqset = JH_DBHelper.excuteQuery(jyConnection,
							fdcqsql);
					return fdcqset;
				}
				// 签约备案信息
				else if ((DJDL.equals("1200") && DJXL.equals("9"))
						|| (DJDL.equals("1200") && DJXL.equals("10"))
						|| (DJDL.equals("1200") && DJXL.equals("11"))
						|| (DJDL.equals("1200") && DJXL.equals("14"))
						|| (DJDL.equals("1200") && DJXL.equals("15"))
						|| (DJDL.equals("1200") && DJXL.equals("16"))
						|| (DJDL.equals("1200") && DJXL.equals("19"))
						|| (DJDL.equals("1200") && DJXL.equals("20"))
						|| (DJDL.equals("1200") && DJXL.equals("21"))) {
					String fdcqsql = "select * from QYBAXX WHERE YWH = '" + YWH
							+ "'";
					ResultSet fdcqset = JH_DBHelper.excuteQuery(jyConnection,
							fdcqsql);
					return fdcqset;
				} else {
					return null;
				}
			} else {
				return null;
			}
		} catch (Exception e) {
			return null;
		}
	}
	//根据不动产权证号查询该不动产权证号对应“权利”信息
	public ResultSet GetBdcqzhsearch(String bdcqzh,String checked, String starttime, String endtime) throws SQLException {
		Connection jyConnection = null;
		String qlrsql = "select * from QLR  WHERE BDCQZH = '" + bdcqzh + "'";
		jyConnection = JH_DBHelper.getConnect_jy();
		ResultSet qlrset = JH_DBHelper.excuteQuery(jyConnection, qlrsql);
		qlrset.next();
		String YWH=qlrset.getString("YWH");
		String sqsqsql = "select * from SLSQ WHERE YWH = '" + YWH + "'";
		ResultSet slsqset = JH_DBHelper.excuteQuery(jyConnection, sqsqsql);
		try {
			if (slsqset.next()) {
				String DJDL = slsqset.getString("DJDL");
				String DJXL = slsqset.getString("DJXL");
				// 抵押权
				if ((DJDL.equals("100") && DJXL.equals("1"))
						|| (DJDL.equals("100") && DJXL.equals("3"))
						|| (DJDL.equals("100") && DJXL.equals("6"))
						|| (DJDL.equals("100") && DJXL.equals("7"))
						|| (DJDL.equals("100") && DJXL.equals("9"))
						|| (DJDL.equals("200") && DJXL.equals("2"))
						|| (DJDL.equals("200") && DJXL.equals("3"))
						|| (DJDL.equals("200") && DJXL.equals("5"))
						|| (DJDL.equals("200") && DJXL.equals("6"))) {
					String dyaqsql = "select * from DYAQ WHERE YWH = '" + YWH
							+ "'";
					ResultSet dyaqset = JH_DBHelper.excuteQuery(jyConnection,
							dyaqsql);
					return dyaqset;
				}
				// 房地产权
				else if ((DJDL.equals("200") && DJXL.equals("4"))
						|| (DJDL.equals("200") && DJXL.equals("5"))) {
					String fdcqsql = "select * from FDCQ2 WHERE YWH = '" + YWH
							+ "'";
					ResultSet fdcqset = JH_DBHelper.excuteQuery(jyConnection,
							fdcqsql);
					return fdcqset;
				}
				// 预告登记
				else if ((DJDL.equals("200") && DJXL.equals("1"))
						|| (DJDL.equals("700") && DJXL.equals("3"))) {
					String fdcqsql = "select * from YGDJ WHERE YWH = '" + YWH
							+ "'";
					ResultSet fdcqset = JH_DBHelper.excuteQuery(jyConnection,
							fdcqsql);
					return fdcqset;
				}
				// 查封登记
				else if ((DJDL.equals("800") && DJXL.equals("1"))
						|| (DJDL.equals("800") && DJXL.equals("2"))
						|| (DJDL.equals("800") && DJXL.equals("3"))) {
					String fdcqsql = "select * from CFDJ WHERE YWH = '" + YWH
							+ "'";
					ResultSet fdcqset = JH_DBHelper.excuteQuery(jyConnection,
							fdcqsql);
					return fdcqset;
				}
				// 预售许可
				else if ((DJDL.equals("1200") && DJXL.equals("1"))
						|| (DJDL.equals("1200") && DJXL.equals("2"))
						|| (DJDL.equals("1200") && DJXL.equals("3"))
						|| (DJDL.equals("1200") && DJXL.equals("4"))
						|| (DJDL.equals("1200") && DJXL.equals("5"))) {
					String fdcqsql = "select * from YSXK WHERE YWH = '" + YWH
							+ "'";
					ResultSet fdcqset = JH_DBHelper.excuteQuery(jyConnection,
							fdcqsql);
					return fdcqset;
				}
				// 签约备案信息
				else if ((DJDL.equals("1200") && DJXL.equals("9"))
						|| (DJDL.equals("1200") && DJXL.equals("10"))
						|| (DJDL.equals("1200") && DJXL.equals("11"))
						|| (DJDL.equals("1200") && DJXL.equals("14"))
						|| (DJDL.equals("1200") && DJXL.equals("15"))
						|| (DJDL.equals("1200") && DJXL.equals("16"))
						|| (DJDL.equals("1200") && DJXL.equals("19"))
						|| (DJDL.equals("1200") && DJXL.equals("20"))
						|| (DJDL.equals("1200") && DJXL.equals("21"))) {
					String fdcqsql = "select * from QYBAXX WHERE YWH = '" + YWH
							+ "'";
					ResultSet fdcqset = JH_DBHelper.excuteQuery(jyConnection,
							fdcqsql);
					return fdcqset;
				} else {
					return null;
				}
			} else {
				return null;
			}
		} catch (Exception e) {
			return null;
		}
	}
	//根据预售许可证号查询该预售许可证号对应“权利”信息
	public ResultSet GetYsxkzhsearch(String ysxkzh,String checked, String starttime, String endtime) throws SQLException {
		Connection jyConnection = null;
		String YSXKsql = "select * from YSXK  WHERE BDCQZH = '" + ysxkzh + "'";
		jyConnection = JH_DBHelper.getConnect_jy();
		ResultSet YSXKset = JH_DBHelper.excuteQuery(jyConnection, YSXKsql);
		YSXKset.next();
		String YWH=YSXKset.getString("YWH");
		String slsqsql = "select * from SLSQ WHERE YWH = '" + YWH + "'";
		//增加时间段统计和问题件统计
		if (starttime != null && !starttime.equals("")) {
			slsqsql = slsqsql + " and SLSJ>to_date('" + starttime
					+ "', 'yyyy-mm-dd hh24:mi:ss') ";
		}
		if (endtime != null && !endtime.equals("")) {
			slsqsql = slsqsql + " and SLSJ<to_date('" + endtime
					+ "', 'yyyy-mm-dd hh24:mi:ss') ";
		}
		if (checked != null && !checked.equals("")) {
			if(checked.equals("1")){
				slsqsql = slsqsql + " and SFWTAJ='1'";
			}
		}
		ResultSet slsqset = JH_DBHelper.excuteQuery(jyConnection, slsqsql);
		try {
			if (slsqset.next()) {
				String DJDL = slsqset.getString("DJDL");
				String DJXL = slsqset.getString("DJXL");
				// 抵押权
				if ((DJDL.equals("100") && DJXL.equals("1"))
						|| (DJDL.equals("100") && DJXL.equals("3"))
						|| (DJDL.equals("100") && DJXL.equals("6"))
						|| (DJDL.equals("100") && DJXL.equals("7"))
						|| (DJDL.equals("100") && DJXL.equals("9"))
						|| (DJDL.equals("200") && DJXL.equals("2"))
						|| (DJDL.equals("200") && DJXL.equals("3"))
						|| (DJDL.equals("200") && DJXL.equals("5"))
						|| (DJDL.equals("200") && DJXL.equals("6"))) {
					String dyaqsql = "select * from DYAQ WHERE YWH = '" + YWH
							+ "'";
					ResultSet dyaqset = JH_DBHelper.excuteQuery(jyConnection,
							dyaqsql);
					return dyaqset;
				}
				// 房地产权
				else if ((DJDL.equals("200") && DJXL.equals("4"))
						|| (DJDL.equals("200") && DJXL.equals("5"))) {
					String fdcqsql = "select * from FDCQ2 WHERE YWH = '" + YWH
							+ "'";
					ResultSet fdcqset = JH_DBHelper.excuteQuery(jyConnection,
							fdcqsql);
					return fdcqset;
				}
				// 预告登记
				else if ((DJDL.equals("200") && DJXL.equals("1"))
						|| (DJDL.equals("700") && DJXL.equals("3"))) {
					String fdcqsql = "select * from YGDJ WHERE YWH = '" + YWH
							+ "'";
					ResultSet fdcqset = JH_DBHelper.excuteQuery(jyConnection,
							fdcqsql);
					return fdcqset;
				}
				// 查封登记
				else if ((DJDL.equals("800") && DJXL.equals("1"))
						|| (DJDL.equals("800") && DJXL.equals("2"))
						|| (DJDL.equals("800") && DJXL.equals("3"))) {
					String fdcqsql = "select * from CFDJ WHERE YWH = '" + YWH
							+ "'";
					ResultSet fdcqset = JH_DBHelper.excuteQuery(jyConnection,
							fdcqsql);
					return fdcqset;
				}
				// 预售许可
				else if ((DJDL.equals("1200") && DJXL.equals("1"))
						|| (DJDL.equals("1200") && DJXL.equals("2"))
						|| (DJDL.equals("1200") && DJXL.equals("3"))
						|| (DJDL.equals("1200") && DJXL.equals("4"))
						|| (DJDL.equals("1200") && DJXL.equals("5"))) {
					String fdcqsql = "select * from YSXK WHERE YWH = '" + YWH
							+ "'";
					ResultSet fdcqset = JH_DBHelper.excuteQuery(jyConnection,
							fdcqsql);
					return fdcqset;
				}
				// 签约备案信息
				else if ((DJDL.equals("1200") && DJXL.equals("9"))
						|| (DJDL.equals("1200") && DJXL.equals("10"))
						|| (DJDL.equals("1200") && DJXL.equals("11"))
						|| (DJDL.equals("1200") && DJXL.equals("14"))
						|| (DJDL.equals("1200") && DJXL.equals("15"))
						|| (DJDL.equals("1200") && DJXL.equals("16"))
						|| (DJDL.equals("1200") && DJXL.equals("19"))
						|| (DJDL.equals("1200") && DJXL.equals("20"))
						|| (DJDL.equals("1200") && DJXL.equals("21"))) {
					String fdcqsql = "select * from QYBAXX WHERE YWH = '" + YWH
							+ "'";
					ResultSet fdcqset = JH_DBHelper.excuteQuery(jyConnection,
							fdcqsql);
					return fdcqset;
				} else {
					return null;
				}
			} else {
				return null;
			}
		} catch (Exception e) {
			return null;
		}
	}
	/*
	 * 根据输入的查询条件和确定的业务号查询"权利人"信息
	 * */
	public ResultSet GetQlrInfo(String ywh) throws SQLException {
		Connection jyConnection = null;
		String QLRsql = "select * from QLR  WHERE YWH = '" + ywh + "'";
		jyConnection = JH_DBHelper.getConnect_jy();
		ResultSet QLRset = JH_DBHelper.excuteQuery(jyConnection, QLRsql);
		return QLRset;
	}
	/*
	 * 根据输入的查询条件和确定的业务号查询"户"信息
	 * */
	public ResultSet GetHuInfo(String ywh) throws SQLException {
		Connection jyConnection = null;
		String Hsql = "select * from H  WHERE YWH = '" + ywh + "'";
		jyConnection = JH_DBHelper.getConnect_jy();
		ResultSet Hset = JH_DBHelper.excuteQuery(jyConnection, Hsql);
		return Hset;
	}
	
	/*
	 * @param project_id
	 * 转出环节增添“预警服务”，返回值说明如下：
	 * 返回值说明：“0”：正常转出无任何操作；“1”：弹框提示‘该件为交易审核不通过件！’；“2”：弹框提示‘该房屋交易系统当前时间未推送到中间库！’;“3”：弹框提示该房屋历史房屋编号未关联到登记库！
	 * */
	public String EarlyWarning(String project_id) throws SQLException {
		String warn = "0";
		String xzqdm = ConfigHelper.getNameByValue("XZQHDM");
		if (xzqdm.contains("5109")) {
			Connection gxConnection = null;
			gxConnection = JH_DBHelper.getConnect_gxdjk();
			//从共享登记库取出当前project_id对应GXXMXX表
			String gxxmxxsql = "select * from GXJHXM  WHERE PROJECT_ID='"
					+ project_id + "'";
			ResultSet XMXXset = JH_DBHelper
					.excuteQuery(gxConnection, gxxmxxsql);
			if (XMXXset.next()) {
				//取得当前待转出项目GXXMBH
				String GXXMBH = XMXXset.getString("GXXMBH");
				String hsql = "select * from H WHERE GXXMBH='" + GXXMBH + "'";
				ResultSet Hset = JH_DBHelper.excuteQuery(gxConnection, hsql);
				if (Hset.next()) {
					//通过GXXMBH关联到H表取得与交易中间库关联字段RELATIONID,即交易的FWBM
					String FWBM = Hset.getString("RELATIONID");
					if(FWBM == null){
						//登记库中不存在当前房屋关联ID，即该房屋历史房屋编号未关联到登记库！
						return "3";
					}
					Connection jyConnection = null;
					jyConnection = JH_DBHelper.getConnect_jy();
					String JYHsql = "select * from H where FWBM='" + FWBM + "'";
					ResultSet JYHset = JH_DBHelper.excuteQuery(jyConnection,
							JYHsql);
					if (JYHset.next()) {
						//从交易中间库的户表通过FWBM取得关联字段YWH
						String ywh = JYHset.getString("YWH");
						String jysfwtjsql = "select * from SLSQ where YWH='"
								+ ywh + "'";
						ResultSet SLSQset = JH_DBHelper.excuteQuery(
								jyConnection, jysfwtjsql);
						if (SLSQset.next()) {
							//根据当前YWH从交易中间库主表SLSQ表取得当前业务是否为问题件字段
							String SFWTAJ = SLSQset.getString("SFWTAJ");
							if (SFWTAJ.equals("1")) {
								//SFWTAJ字段值为1，说明该件为问题件，需要弹框警示该件为“交易审核不通过件”
								return "1";
							} else {
								//SFWTAJ字段值为0，说明该件为正常件，正常转出即可；
								return "0";
							}
						}
					}
					else{
						//交易中间库未推送当前房屋，弹框提示：“交易系统当前时间未推送该房屋信息！”
						return "2";
					}
				}
			}
		}
		return warn;
	}
}
