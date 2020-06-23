package com.supermap.wisdombusiness.synchroinline.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.supermap.wisdombusiness.framework.model.User;
import com.supermap.wisdombusiness.synchroinline.dao.synchroDao;
import com.supermap.wisdombusiness.synchroinline.util.DButil;
import com.supermap.wisdombusiness.workflow.service.wfi.SmStaff;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.PreparedStatement;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

@Service
public class qjdcshService {

	@Autowired
	private synchroDao dao;

	@Autowired
	private SmStaff smStaff;

	/**
	 * 权籍调查审核页面数据加载
	 * 
	 * @param req
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unused")
	public JSONArray qjdcshData(HttpServletRequest req) throws Exception {
	   // String ss =req.getParameter("pageNumber");
	    //String zz=req.getParameter("pageSize");
	        Integer page =Integer.parseInt(req.getParameter("pageNumber"));
	        Integer rows =Integer.parseInt(req.getParameter("pageSize"));
	        Integer startIndex =(page - 1)* rows +1;
	        Integer endIndex = page * rows;
		String sqr = req.getParameter("sqr");
		String djlx = req.getParameter("djlx");
		String qllx = req.getParameter("qllx");
		String sqsj = req.getParameter("sqsj");// 申请时间
		StringBuffer SQL_T = new StringBuffer();
		SQL_T.append(" select count(A.Id) n from PRO_PROINST A , PRO_PROPOSERINFO B where A.ID = B.PROINST_ID and B.Sqr_Sxh = '1' and A.shzt = '0'"); //查询所有记录数
		if (sqr != "") {
		    SQL_T.append(" and B.SQR_NAME='");
		    SQL_T.append(sqr + "'");
		}
		if (djlx != "") {
		    SQL_T.append(" and A.Djlx='");
		    SQL_T.append(djlx + "'");
		}
		if (qllx != "") {
		    SQL_T.append(" and A.QLLX='");
		    SQL_T.append(qllx + "'");
		}
		if (sqsj != "") {
		    SQL_T.append(" and  A.Prostart like to_date('");
		    SQL_T.append(sqsj);
		    SQL_T.append("','yyyy/MM/dd ')");
		}
		StringBuffer SQL = new StringBuffer();
		SQL.append("select * from (select B.SQR_NAME sqr, B.SQR_TEL lxdh,A.DJLX djlx,A.QLLX qllx,A.PROSTART sqsj,A.SHZT shzt,A.id xmid,rownum nu ,trunc((rownum - 1)/10)+1 num ");
		SQL.append("from PRO_PROINST A left join");
		SQL.append("(select sqr_tel,sqr_name ,proinst_id ,sqr_sxh from PRO_PROPOSERINFO ) B");
		SQL.append(" on A.ID =B.PROINST_ID where B.Sqr_Sxh ='1' and A.shzt ='0' ");
		if (sqr != "") {
			SQL.append("and B.SQR_NAME='");
			SQL.append(sqr + "'");
		}
		if (djlx != "") {
			SQL.append("and A.Djlx='");
			SQL.append(djlx + "'");
		}
		if (qllx != "") {
			SQL.append("and A.QLLX='");
			SQL.append(qllx + "'");
		}
		if (sqsj != "") {
			SQL.append("and  A.Prostart like to_date('");
			SQL.append(sqsj);
			SQL.append("','yyyy/MM/dd ')");
		}
		SQL.append(")where nu between ");
		SQL.append(startIndex+" and ");
		SQL.append(endIndex);
		
		// 连接数据库-前置机
		Connection conn = DButil.getConnection();
		PreparedStatement sh = null; // 预编译 sql
		PreparedStatement sh_T = null; // 预编译 SQL_T
		ResultSet shResult = null; // 查询后返回的结果集
		ResultSet shT_Result = null; // 查询后返回的结果集
		JSONArray jsonA = new JSONArray(); // jsonArray数组（json对象）
		String N ="";
		if (conn != null) {
			try {
			    sh= conn.prepareStatement(SQL.toString());// 预编译 sql
			    sh_T = conn.prepareStatement(SQL_T.toString());// 预编译 sql
				shResult = sh.executeQuery();// 执行sql查询
				shT_Result = sh_T.executeQuery();	
				    if (shResult != null) {
					if(shT_Result !=null){
					    while(shT_Result.next()){
						N=shT_Result.getString("n");
					    }}else{
						DButil.close(null, sh_T, conn);
					    }
					while (shResult.next()) { // 遍历结果集
						JSONObject object = new JSONObject();// json对象（"key","value"）一对一关系
						String SQR = shResult.getString("sqr");
						String LXDH = shResult.getString("lxdh");
						String DJLX = shResult.getString("djlx");
						String QLLX = shResult.getString("qllx");
						String SQSJ = shResult.getString("sqsj");
						String SHZT = shResult.getString("shzt");
						String XMID = shResult.getString("xmid");
						String NUM = shResult.getString("num");
						object.put("sqr", SQR);
						object.put("lxdh", LXDH);
						object.put("djlx", DJLX);
						object.put("qllx", QLLX);
						object.put("sqsj", SQSJ);
						object.put("shzt", SHZT);
						object.put("xmid", XMID);
						object.put("num", NUM); //页数
						object.put("total", N);
						jsonA.add(object); // 将json对象添加进json数组
					}					
					DButil.close(null, null, conn);
					DButil.close(shResult, sh, conn);
					DButil.close(shT_Result, sh_T, conn);
				} else {
					DButil.close(null, sh, conn);
				}					
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("数据库连接失败");
		}
		return jsonA;
	}

	/**
	 * 添加审核意见与修改审核状态
	 * 
	 * @param req
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unused")
	public String qjdcsh_up(HttpServletRequest req) throws Exception {
		User user = smStaff.getCurrentWorkStaff();// 获取当前user对象
		String shry = user.getUserName();// 获取当前登录用户名(审核人员)
		Date myDate = new Date();
		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		String shrq = df.format(myDate);
		String xmid = req.getParameter("xmid");// 获取项目id
		String shzt = req.getParameter("shzt");// 获取审核状态
		String shyj = req.getParameter("shyj");// 获取审核意见
//		StringBuffer SQL_H = new StringBuffer(); // 页面当前行数据
		StringBuffer SQL_SHZT = new StringBuffer();// 页面当前行审核状态
		StringBuffer SQL_SHYJ = new StringBuffer();// 页面当前行审核意见
		StringBuffer SQL_SHYJ_CX = new StringBuffer();// 查询审核意见表
		StringBuffer SQL_SHYJ_UP = new StringBuffer();// 更新审核意见表
		String type ="";
		SQL_SHZT.append("update PRO_PROINST t  set t.tbzt='2',t.shzt='");
		SQL_SHZT.append(shzt);
		SQL_SHZT.append("'where id='");
		SQL_SHZT.append(xmid + "'");

		SQL_SHYJ.append("insert into PRO_SLXMSH (sh_id ,sh_qjdc_ry,sh_qjdc_yj ,sh_qjdc_rq,slxm_id )values(sys_guid(),'");
		SQL_SHYJ.append(shry + "','");
		SQL_SHYJ.append(shyj + "',");
		SQL_SHYJ.append("to_date('"+shrq + "','yyyy/MM/dd HH24:mi:ss'),'");
		SQL_SHYJ.append(xmid+"')");

		SQL_SHYJ_CX.append("select slxm_id from PRO_SLXMSH where slxm_id='");
		SQL_SHYJ_CX.append(xmid+"'");
		
		SQL_SHYJ_UP.append("update PRO_SLXMSH t set t.sh_qjdc_ry ='");
		SQL_SHYJ_UP.append(shry+"',t.sh_qjdc_yj='");
		SQL_SHYJ_UP.append(shyj+"',t.sh_qjdc_rq= ");
		SQL_SHYJ_UP.append("to_date('"+shrq + "','yyyy/MM/dd HH24:mi:ss') where slxm_id='");
		SQL_SHYJ_UP.append(xmid+"'");

		// 连接数据库-前置机
		Connection con = DButil.getConnection();
		PreparedStatement ed_Shzt = null; // 预编译 SQL_SHZT
		PreparedStatement ed_Shyj = null;// 预编译 SQL_SHYJ
		PreparedStatement ed_ShyjCX = null;// 预编译 SQL_SHYCx
		PreparedStatement ed_ShyjUp = null;// 预编译 SQL_SHYJ_UP		
		
		ResultSet shyjCx_Result = null; // 查询SQL_SHYJ2后返回的结果集
		JSONArray jsonE = new JSONArray(); // jsonArray数组（json对象）
		if (con != null) {
			try {
				ed_Shzt = con.prepareStatement(SQL_SHZT.toString());// 预编 SQL_SHZT
				ed_Shyj = con.prepareStatement(SQL_SHYJ.toString());// 预编译 SQL_SHYJ
				ed_ShyjCX = con.prepareStatement(SQL_SHYJ_CX.toString());// 预编译 SQL_SHYJ_Cx
				ed_ShyjUp = con.prepareStatement(SQL_SHYJ_UP.toString());// 预编译 SQL_SHYJ_UP
	
				shyjCx_Result=ed_ShyjCX.executeQuery();//返回SQL_SHYJ_CX结果集
				if(shyjCx_Result ==null){
					ed_Shyj.executeUpdate();
				}else {
					ed_ShyjUp.executeUpdate();
				}
				ed_Shzt.executeUpdate();
				//关闭连接
				DButil.close(null, null, con);
				DButil.close(null, ed_Shzt, con);
				DButil.close(null, ed_Shyj, con);
				DButil.close(null, ed_ShyjCX, con);
				DButil.close(null, ed_ShyjUp, con);
				
				type ="1";

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}else {
			System.out.println("数据库连接失败");
		}
		return type;
	}

	/**
	 * 获取当前登录用户名
	 * 
	 * @param req
	 * @return
	 * @throws Exception
	 */
	public JSONArray qjdcshry(HttpServletRequest req) throws Exception {
		JSONObject obj = new JSONObject();
		JSONArray jsonAA = new JSONArray();
		User user = smStaff.getCurrentWorkStaff();// 获取当前user对象
		String SHRY = user.getUserName();// 获取当前登录用户名
		obj.put("shry", SHRY);
		jsonAA.add(obj);
		return jsonAA;
	}

}
