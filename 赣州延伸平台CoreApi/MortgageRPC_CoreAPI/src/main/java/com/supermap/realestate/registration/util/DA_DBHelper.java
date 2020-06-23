package com.supermap.realestate.registration.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 
 * @Description:档案库操作
 * @author zhuhe&buxiaobo
 * @date 2015年11月17日 23:24:49
 * @Copyright SuperMap
 */
public class DA_DBHelper {

//	private static String driver_jy = GetProperties
//			.getConstValueByKey("driverName_da");
	private static String url_da = GetProperties.getConstValueByKey("jdbc.url");
	private static String username_da = GetProperties
			.getConstValueByKey("jdbc.username");
	private static String password_da = GetProperties
			.getConstValueByKey("jdbc.password");

	/**
	 * 创建与档案库的连接
	 */
	public static Connection getConnect_da() {
		// 数据库连接对象
		Connection conn = null;
		try {
			// 反射Oracle数据库驱动程序类
			Class.forName("oracle.jdbc.driver.OracleDriver");
			// 获取数据库连接
			conn = DriverManager.getConnection(url_da, username_da, password_da);
			//conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl", "system", "salis");
			// 输出数据库连接
			System.out.println(conn);

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// try{
			// if(conn != null){
			// conn.close();
			// }
			// } catch (SQLException e) {
			// e.printStackTrace();
			// }
		}
		return conn;
	}

	public static ResultSet excuteQuery(Connection conn, String sql)
			throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		// 创建该连接下的PreparedStatement对象
		pstmt = conn.prepareStatement(sql);
		// 传递第一个参数值 root，代替第一个问号
		//pstmt.setString(1, "root");
		// 执行查询语句，将数据保存到ResultSet对象中
		rs = pstmt.executeQuery();
		// if(rs != null){
		// rs.close();
		// }
//		if (pstmt != null) {
//			pstmt.close();
//		}

		return rs;
	}
	
	public static boolean excuteNoQuery(Connection conn, String sql)
			throws SQLException {
		PreparedStatement pstmt = null;
		// 创建该连接下的PreparedStatement对象
		pstmt = conn.prepareStatement(sql);
		// 传递第一个参数值 root，代替第一个问号
		//pstmt.setString(1, "root");
		// 执行查询语句，将数据保存到ResultSet对象中
		boolean b = pstmt.execute(sql);
//		if (pstmt != null) {
//			pstmt.close();
//		}

		return b;
	}
	/**
	 * 插入和修改
	 * 
	 * @author likun
	 * @return
	 */
	public static int excuteUpdate(Connection conn, String sql)
			throws SQLException {
		PreparedStatement pstmt = null;
		// 创建该连接下的PreparedStatement对象
		pstmt = conn.prepareStatement(sql);
		// 传递第一个参数值 root，代替第一个问号
		//pstmt.setString(1, "root");
		// 执行查询语句，将数据保存到ResultSet对象中
		int i = pstmt.executeUpdate(sql);
//		if (pstmt != null) {
//			pstmt.close();
//		}

		return i;
	}

}
