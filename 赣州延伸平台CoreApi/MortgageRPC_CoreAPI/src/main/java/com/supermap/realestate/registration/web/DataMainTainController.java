package com.supermap.realestate.registration.web;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import com.supermap.wisdombusiness.web.ResultMessage;

/**
 * 配置Controller，登记系统进行数据维护的服务
 * @ClassName: DataMainTainController
 * @author 俞学斌
 * @date 2016年11月03日 16:50:04
 */
@Controller
@RequestMapping("/datamaintain")
public class DataMainTainController {

	@Autowired
	private CommonDao dao;
	
	/**
	 * 项目信息表中登簿时间维护
	 * @Title: 
	 * @author:yuxuebin
	 * @date：2016年11月03日 16:52:28
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/djsjinxmxx/", method = RequestMethod.GET)
	public @ResponseBody ResultMessage DjsjInXmxx(HttpServletRequest request, HttpServletResponse response) {
		ResultMessage msg=new ResultMessage();
		msg.setSuccess("false");
		msg.setMsg("维护失败！");
		//添加登记时间字段
		try {
			dao.excuteQueryNoResult("ALTER TABLE BDCK.BDCS_XMXX ADD DJSJ DATE");
		} catch (SQLException e) {
		}
		//删除项目编号-登簿时间关系表
		try {
			dao.excuteQueryNoResult("DROP TABLE BDCK.XMBH_DJSJ");
		} catch (SQLException e) {
		}
		//生成项目编号-登簿时间关系表
		try {
			StringBuilder builder=new StringBuilder();
			builder.append("CREATE TABLE BDCK.XMBH_DJSJ AS ");
			builder.append("SELECT QL.XMBH,");
			builder.append("MAX(CASE WHEN QL.DJSJ IS NULL THEN FSQL.ZXSJ WHEN FSQL.ZXSJ IS NULL THEN QL.DJSJ ");
			builder.append("WHEN QL.DJSJ>FSQL.ZXSJ THEN QL.DJSJ ELSE FSQL.ZXSJ END) DJSJ ");
			builder.append("FROM BDCK.BDCS_QL_GZ QL LEFT JOIN BDCK.BDCS_FSQL_GZ FSQL ON QL.QLID=FSQL.QLID ");
			builder.append("WHERE QL.XMBH IN (SELECT XMBH FROM BDCK.BDCS_XMXX WHERE SFDB='1') ");
			builder.append("GROUP BY QL.XMBH");
			dao.excuteQueryNoResult(builder.toString());
		} catch (SQLException e) {
			msg.setMsg("生成项目编号-登簿时间关系表失败！");
			return msg;
		}
		
		//删除项目信息临时表（附带登簿时间）
		try {
			dao.excuteQueryNoResult("DROP TABLE BDCK.TEMP_XMXX");
		} catch (SQLException e) {
		}
		
		//生成项目信息临时表（附带登簿时间）
		try {
			StringBuilder builder=new StringBuilder();
			builder.append("CREATE TABLE BDCK.TEMP_XMXX AS ");
			builder.append("SELECT XMXX.*,GX.DJSJ DJSJ2 ");
			builder.append("FROM BDCK.BDCS_XMXX XMXX ");
			builder.append("LEFT JOIN BDCK.XMBH_DJSJ GX ");
			builder.append("ON XMXX.XMBH=GX.XMBH");
			dao.excuteQueryNoResult(builder.toString());
		} catch (SQLException e) {
			msg.setMsg("生成项目信息临时表（附带登簿时间）失败！");
			return msg;
		}
		
		//更新项目信息临时表登簿时间
		try {
			dao.excuteQueryNoResult("UPDATE BDCK.TEMP_XMXX SET DJSJ=DJSJ2 WHERE SFDB='1'");
		} catch (SQLException e) {
			msg.setMsg("更新项目信息临时表登簿时间失败！");
			return msg;
		}
		
		//清空项目信息表
		try {
			dao.excuteQueryNoResult("TRUNCATE TABLE BDCK.BDCS_XMXX");
		} catch (SQLException e) {
			msg.setMsg("清空项目信息表失败！");
			return msg;
		}
		
		List<Map> xmxx_cols=dao.getDataListByFullSql("SELECT COLUMN_NAME FROM DBA_TAB_COLS WHERE TABLE_NAME='BDCS_XMXX' AND OWNER='BDCK'");
		List<String> list_cols=new ArrayList<String>();
		for(Map col:xmxx_cols){
			list_cols.add(StringHelper.formatObject(col.get("COLUMN_NAME")));
		}
		String str_cols=StringHelper.formatList(list_cols, ",");
		//追加数据到项目信息表
		try {
			StringBuilder builder=new StringBuilder();
			builder.append("INSERT INTO BDCK.BDCS_XMXX(");
			builder.append(str_cols);
			builder.append(")");
			builder.append("SELECT ");
			builder.append(str_cols);
			builder.append(" FROM BDCK.TEMP_XMXX");
			dao.excuteQueryNoResult(builder.toString());
		} catch (SQLException e) {
			msg.setMsg("追加数据到项目信息表失败！");
			return msg;
		}
		
		//根据单元限制项目维护登簿时间
		try {
			StringBuilder builder=new StringBuilder();
			builder.append("UPDATE BDCK.BDCS_XMXX XMXX ");
			builder.append("SET DJSJ=");
			builder.append("(SELECT MAX(DJSJ) FROM BDCK.BDCS_DYXZ WHERE YWH=XMXX.PROJECT_ID) ");
			builder.append("WHERE SFDB='1' AND DJSJ IS NULL");
			dao.excuteQueryNoResult(builder.toString());
		} catch (SQLException e) {
		}
		
		//根据单元限制项目维护登簿时间
		try {
			StringBuilder builder=new StringBuilder();
			builder.append("UPDATE BDCK.BDCS_XMXX XMXX ");
			builder.append("SET DJSJ=");
			builder.append("(SELECT MAX(ZXDJSJ) FROM BDCK.BDCS_DYXZ WHERE ZXYWH=XMXX.PROJECT_ID) ");
			builder.append("WHERE SFDB='1' AND DJSJ IS NULL");
			dao.excuteQueryNoResult(builder.toString());
		} catch (SQLException e) {
		}
		
		//删除项目编号-登簿时间关系表
		try {
			dao.excuteQueryNoResult("DROP TABLE BDCK.XMBH_DJSJ");
		} catch (SQLException e) {
		}
		
		//删除项目信息临时表（附带登簿时间）
		try {
			dao.excuteQueryNoResult("DROP TABLE BDCK.TEMP_XMXX");
		} catch (SQLException e) {
		}
		msg.setSuccess("true");
		msg.setMsg("维护成功！");
		return msg;
	}
}
