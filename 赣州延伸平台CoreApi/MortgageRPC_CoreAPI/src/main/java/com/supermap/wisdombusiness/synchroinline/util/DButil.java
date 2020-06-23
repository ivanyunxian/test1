package com.supermap.wisdombusiness.synchroinline.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Configuration;

import com.supermap.wisdombusiness.synchroinline.model.Inline_dxts;
import com.supermap.wisdombusiness.synchroinline.model.Pro_attachment;
import com.supermap.wisdombusiness.synchroinline.model.Pro_datuminst;
import com.supermap.wisdombusiness.synchroinline.model.Pro_fwxx;
import com.supermap.wisdombusiness.synchroinline.model.Pro_proinst;
import com.supermap.wisdombusiness.synchroinline.model.Pro_proposerinfo;
import com.supermap.wisdombusiness.synchroinline.model.Pro_slxmsh;
import com.supermap.wisdombusiness.synchroinline.model.T_SCHEDULE;
import com.supermap.wisdombusiness.synchroinline.model.T_certificate_ls;
import com.supermap.wisdombusiness.synchroinline.model.T_deleted_proinst;
import com.supermap.wisdombusiness.synchroinline.model.T_project_qlr;

public class DButil
{

	private static Properties p = null;
	private static String URL;
	private static String USERNAME;
	private static String PASSWORD;
	private static Connection conn = null;
	private static SessionFactory mscSessionFactory = null;
	private static Session mscSession = null;
	private static Configuration config=new AnnotationConfiguration();

	private static int a = 0;
	static
	{
		try
		{
			p = new PropertyUtil().getConfigProperties();
			URL = p.getProperty("url");
			USERNAME = p.getProperty("username");
			PASSWORD = p.getProperty("password");
			Class.forName(p.getProperty("driver"));

			// 添加实体类映射
			config.addAnnotatedClass(Pro_proinst.class);
			config.addAnnotatedClass(Pro_proposerinfo.class);
			config.addAnnotatedClass(Pro_slxmsh.class);
			config.addAnnotatedClass(Pro_datuminst.class);
			config.addAnnotatedClass(Pro_attachment.class);
			config.addAnnotatedClass(T_deleted_proinst.class);
			config.addAnnotatedClass(T_certificate_ls.class);
			config.addAnnotatedClass(Inline_dxts.class);
			config.addAnnotatedClass(T_project_qlr.class);
			config.addAnnotatedClass(T_SCHEDULE.class);
			config.addAnnotatedClass(Pro_fwxx.class);
			config.setProperty("hibernate.connection.url", URL);
			config.setProperty("hibernate.connection.username", USERNAME);
			config.setProperty("hibernate.connection.password", PASSWORD);
			config.setProperty("hibernate.connection.driver_class", p.getProperty("driver"));
			config.setProperty("hibernate.dialect", "com.supermap.wisdombusiness.core.SuperMapOracleDialect");
			config.setProperty("hibernate.show_sql", "true");
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
	}

	public static Connection getConnection()
	{
		try
		{
			if (conn == null || conn.isClosed())
			{
				conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
				a++;
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		System.out.println("总共获取的oracle数据库连接数=====" + a);
		return conn;
	}

	/**
	 * 使用后要释放连接
	 * 
	 * @return
	 * @throws Exception
	 */
	public static Connection getNewConnection()
	{
		try
		{
			return DriverManager.getConnection(URL, USERNAME, PASSWORD);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		return null;
	}

	public static void close(ResultSet rs, PreparedStatement ps, Connection conn)
	{
		if (rs != null)
		{
			try
			{
				rs.close();
			}
			catch (SQLException e)
			{
				e.printStackTrace();
			}
		}
		if (ps != null)
		{
			try
			{
				ps.close();
			}
			catch (SQLException e)
			{
				e.printStackTrace();
			}
		}
		if (conn != null)
		{
			try
			{
				conn.close();
			}
			catch (SQLException e)
			{
				e.printStackTrace();
			}
		}
	}

	/**
	 * 获取在线服务系统前置机Hibernate Session 对象
	 * 
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static Session GetMscSession()
	{
		initMscSessionFactory();
		if (mscSession == null)
		{
			mscSession = mscSessionFactory.openSession();
		}
		if (!mscSession.isConnected())
		{
			mscSession = mscSessionFactory.openSession();
		}
		//检查session会话是否超时
		try
		{
			mscSession.createSQLQuery("select * from dual").list().size();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			mscSessionFactory.close();
			mscSessionFactory=null;
			initMscSessionFactory();
			mscSession = mscSessionFactory.openSession();
		}
		return mscSession;
	}

	/**
	 * 创建新的在线服务系统前置机Hibernate Session 对象，用完后要手动释放
	 * 
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static Session newGetMscSession()
	{
		initMscSessionFactory();
		Session session = mscSessionFactory.openSession();
		session.setFlushMode(FlushMode.AUTO);
		return session;
	}

	private static synchronized void initMscSessionFactory()
	{
		if (mscSessionFactory == null || mscSessionFactory.isClosed())
		{
			mscSessionFactory = config.buildSessionFactory();
		}
	}
}
