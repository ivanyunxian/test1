package com.supermap.realestate.registration.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.SQLQuery;

/**
 * 
 * @Description:与交易系统交换的中间库数据操作
 * @author likun
 * @date 2015年11月15日 上午11:53:45
 * @Copyright SuperMap
 */
public class JH_DBHelper {

//	private static String driver_jy = GetProperties
//			.getConstValueByKey("driverName_jy");
//	private static String driver_dj = GetProperties
//			.getConstValueByKey("driverName_dj");

	/**
	 * 创建与交易中间库的连接
	 * 
	 * @author likun
	 * @return
	 */
	public static Connection getConnect_bdck() {
		// 数据库连接对象
		Connection conn = null;
		try {
			
			String url_bdck =ConfigHelper.getNameByValue("URL_bdck");
			String username_bdck = ConfigHelper.getNameByValue("USERNAME_bdck");
			String password_bdck = ConfigHelper.getNameByValue("PASSWORD_bdck");
			
			// 反射Oracle数据库驱动程序类
			Class.forName("oracle.jdbc.driver.OracleDriver");
			// 获取数据库连接
			conn = DriverManager.getConnection(url_bdck, username_bdck, password_bdck);
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
	
	
	public static Connection getConnect_jy() {
		// 数据库连接对象
		Connection conn = null;
		try {
			
			String url_jy =ConfigHelper.getNameByValue("URL_JY");
			String username_jy = ConfigHelper.getNameByValue("USERNAME_JY");
			String password_jy = ConfigHelper.getNameByValue("PASSWORD_JY");
			
			// 反射Oracle数据库驱动程序类
			Class.forName("oracle.jdbc.driver.OracleDriver");
			// 获取数据库连接
			conn = DriverManager.getConnection(url_jy, username_jy, password_jy);
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

	/**
	 * 创建与共享登记库的连接
	 * 
	 * @author likun gxdjk
	 * @return
	 */
	public static Connection getConnect_gxdjk() {
		// 数据库连接对象
		Connection conn = null;
		try {
			
			String url_jy =ConfigHelper.getNameByValue("URL_GXDJ");
			String username_jy = ConfigHelper.getNameByValue("USERNAME_GXDJ");
			String password_jy = ConfigHelper.getNameByValue("PASSWORD_GXDJ");
			
			// 反射Oracle数据库驱动程序类
			Class.forName("oracle.jdbc.driver.OracleDriver");
			// 获取数据库连接
			conn = DriverManager.getConnection(url_jy, username_jy, password_jy);
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
	
	
	/**
	 * 创建与共享交易库的连接
	 * 
	 * @author likun gxdjk
	 * @return
	 */
	public static Connection getConnect_gxjyk() {
		// 数据库连接对象
		Connection conn = null;
		try {
			
			String url_jy =ConfigHelper.getNameByValue("URL_JY");
			String username_jy = ConfigHelper.getNameByValue("USERNAME_JY");
			String password_jy = ConfigHelper.getNameByValue("PASSWORD_JY");
			
			// 反射Oracle数据库驱动程序类
			Class.forName("oracle.jdbc.driver.OracleDriver");
			// 获取数据库连接
			conn = DriverManager.getConnection(url_jy, username_jy, password_jy);
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
	/**
	 * 创建与共享登记库的连接
	 * 
	 * @author likun
	 * @return
	 */
	public static Connection getConnect_gxtddjk() {
		// 数据库连接对象
		Connection conn = null;
		try {
			
			String url_jy =ConfigHelper.getNameByValue("URL_GXTDDJ");
			String username_jy = ConfigHelper.getNameByValue("USERNAME_GXTDDJ");
			String password_jy = ConfigHelper.getNameByValue("PASSWORD_GXTDDJ");
			
			// 反射Oracle数据库驱动程序类
			Class.forName("oracle.jdbc.driver.OracleDriver");
			// 获取数据库连接
			conn = DriverManager.getConnection(url_jy, username_jy, password_jy);
			// 输出数据库连接
			System.out.println(conn);

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
		}
		return conn;
	}
	/**
	 * 创建与查询共享库连接
	 * @作者 钟念
	 * @创建时间 2016年8月12日上午11:10:59
	 * @return
	 */
	public static Connection getConnect_CXGXK(){
		Connection conn = null;
		try {
			
			String url_jy =ConfigHelper.getNameByValue("URL_SHARESEARCH");
			String username_jy = ConfigHelper.getNameByValue("USERNAME_SHARESEARCH");
			String password_jy = ConfigHelper.getNameByValue("PASSWORD_SHARESEARCH");
			
			// 反射Oracle数据库驱动程序类
			Class.forName("oracle.jdbc.driver.OracleDriver");
			// 获取数据库连接
			conn = DriverManager.getConnection(url_jy, username_jy, password_jy);
			// 输出数据库连接
			System.out.println(conn);

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
		}
		return conn;
	}
	
	/**
	 * 创建与sharesearch库的连接
	 * @author 包基杰
	 * @return
	 */
	public static Connection getConnect_sharesearch() {
		// 数据库连接对象
		Connection conn = null;
		try {
			
			String url_jy =ConfigHelper.getNameByValue("URL_SHARESEARCH");
			String username_jy = ConfigHelper.getNameByValue("USERNAME_SHARESEARCH");
			String password_jy = ConfigHelper.getNameByValue("PASSWORD_SHARESEARCH");
			
			// 反射Oracle数据库驱动程序类
			Class.forName("oracle.jdbc.driver.OracleDriver");
			// 获取数据库连接
			conn = DriverManager.getConnection(url_jy, username_jy, password_jy);
			// 输出数据库连接
			System.out.println(conn);

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
		}
		return conn;
	}
	
	/**
	 * 创建与不动产调查库库的连接
	 * 
	 * @author baojj 2017.3.30
	 * @return
	 */
	public static Connection getConnect_bdcdck() {
		// 数据库连接对象
		Connection conn = null;
		try {
			
			String url_jy =ConfigHelper.getNameByValue("URL_DCK");
			String username_jy = ConfigHelper.getNameByValue("USERNAME_DCK");
			String password_jy = ConfigHelper.getNameByValue("PASSWORD_DCK");
			
			// 反射Oracle数据库驱动程序类
			Class.forName("oracle.jdbc.driver.OracleDriver");
			// 获取数据库连接
			conn = DriverManager.getConnection(url_jy, username_jy, password_jy);
			// 输出数据库连接
			System.out.println(conn);

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
		}
		return conn;
	}
	
	
	
	public static ResultSet excuteQuery(Connection con, String sql)
			throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		// 创建该连接下的PreparedStatement对象
		pstmt = con.prepareStatement(sql);
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
	
	public static boolean excuteNoQuery(Connection con, String sql)
			throws SQLException {
		PreparedStatement pstmt = null;
		// 创建该连接下的PreparedStatement对象
		pstmt = con.prepareStatement(sql);
		// 传递第一个参数值 root，代替第一个问号
		//pstmt.setString(1, "root");
		// 执行查询语句，将数据保存到ResultSet对象中
		boolean b = pstmt.execute(sql);
		if (pstmt != null) {
			pstmt.close();
		}

		return b;
	}
	/**
	 * 插入和修改
	 * 
	 * @author likun
	 * @return
	 */
	public static int excuteUpdate(Connection con, String sql)
			throws SQLException {
		PreparedStatement pstmt = null;
		// 创建该连接下的PreparedStatement对象
		pstmt = con.prepareStatement(sql);
		// 传递第一个参数值 root，代替第一个问号
		//pstmt.setString(1, "root");
		// 执行查询语句，将数据保存到ResultSet对象中
		int i = pstmt.executeUpdate(sql);
		if (pstmt != null) {
			pstmt.close();
		}

		return i;
	}
    public static Long get_sharesearchCount(String sql) {
        Connection conn = JH_DBHelper.getConnect_sharesearch();
        ResultSet excuteQuery = null;
        Long count=(long) 0;
        try {
            excuteQuery = JH_DBHelper.excuteQuery(conn, sql);
            if (excuteQuery != null) {
                if (excuteQuery.next()) {
                 count=(long)excuteQuery.getInt(1);
                    }
                }
                return count;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (excuteQuery != null) {
                try {
                    ResultSet unwrap = excuteQuery.unwrap(ResultSet.class);
                    unwrap.close();
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            if (conn != null) {
                try {
                    Connection unwrap = conn.unwrap(Connection.class);
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return count;
    }


}
