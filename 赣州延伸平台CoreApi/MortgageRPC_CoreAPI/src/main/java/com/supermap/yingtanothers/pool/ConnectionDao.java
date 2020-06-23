package com.supermap.yingtanothers.pool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.supermap.realestate.registration.util.ConstValue;
import com.supermap.realestate.registration.util.JH_DBHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.realestate.registration.util.YwLogUtil;
import com.supermap.wisdombusiness.web.ui.Page;
import com.supermap.yingtanothers.model.CFDJ;
import com.supermap.yingtanothers.model.DYAQ;
import com.supermap.yingtanothers.model.FDCQ;
import com.supermap.yingtanothers.model.QLR;
import com.supermap.yingtanothers.model.SHAREFJ;

/**
 * 
 * 作者： 苗利涛
 * 时间： 2016年1月26日 下午12:05:42
 * 功能：鹰潭市不动产查询中间库共享登记库分页
 */
@Repository("ConnectionDao")
public class ConnectionDao {

	/**
	 * 根据sql条件语句(where后面的部分)分页查询数据，不用参数化查询
	 * 
	 * @param condition
	 *            where后面的条件语句
	 * @param pageIndex
	 *            当前页数
	 * @param pageSize
	 *            每页记录数
	 * @return
	 * @throws SQLException 
	 */
	public Page getPageDataByHql(String tableName, String hqlCondition, int pageIndex, int pageSize) throws SQLException {
		
		Long count = (long) 0;
		List<?> list =  null;
		int firstResult = Page.getStartOfPage(pageIndex, pageSize);
		int endResult = firstResult + pageSize;
		String fullHQLQuery = "SELECT * FROM (SELECT ROWNUM AS rowno, t.* FROM "+ tableName +" t Where  ROWNUM <= "+ endResult +" AND "+ hqlCondition +") table_alias WHERE table_alias.rowno >  "+ firstResult;
		
		IConnectionPool  pool = ConnectionPoolManager.getInstance().getPool("yingtanPool"); 
		Connection conn = pool.getConnection();
		PreparedStatement pst = conn.prepareStatement("select count(*) from " + tableName +" where "+hqlCondition);
		ResultSet rst = pst.executeQuery();
		if(rst.next()){
			count = (long) rst.getInt(1);
		}
		
		if (count < 1){
			return new Page();
		}
		PreparedStatement ps = conn.prepareStatement(fullHQLQuery);
		ResultSet rs = ps.executeQuery();
		if (tableName.toUpperCase() == "QLR") {
			list = resultSetToList_QLR(rs);
		} else if(tableName.toUpperCase() == "FDCQ"){
			list = resultSetToList_FDCQ(rs);
		}else if(tableName.toUpperCase() == "DYAQ"){
			list = resultSetToList_DYAQ(rs);
		}else if(tableName.toUpperCase() == "CFDJ"){
			list = resultSetToList_CFDJ(rs);
		}
		
		conn.close();
		return new Page(Page.getStartOfPage(pageIndex, pageSize), count, pageSize, list);
	}
	
	/**
	 * 根据sql条件语句(where后面的部分)分页查询数据，不用参数化查询
	 * 
	 * @param condition
	 *            where后面的条件语句
	 * @param pageIndex
	 *            当前页数
	 * @param pageSize
	 *            每页记录数
	 * @return
	 * @throws SQLException 
	 */
	public Page getShareFjDataByHql(String tableName, String hqlCondition, int pageIndex, int pageSize) throws SQLException {
		
		Long count = (long) 0;
		List<?> list = null;
		int firstResult = Page.getStartOfPage(pageIndex, pageSize);
		int endResult = firstResult + pageSize;
		String fullHQLQuery = "SELECT * FROM (SELECT ROWNUM AS rowno, t.* FROM " + tableName + " t Where  ROWNUM <= " + endResult + " AND " + hqlCondition
				+ ") table_alias WHERE table_alias.rowno >  " + firstResult;
		String sql = "select count(*)  from " + tableName + " where " + hqlCondition;

		Connection jyConnection = null;
		ResultSet rst = null;
		ResultSet rs = null;
		try {
			jyConnection = JH_DBHelper.getConnect_jy();

			 rst = JH_DBHelper.excuteQuery(jyConnection, sql);
			if (rst.next()) {
				count = (long) rst.getInt(1);

			}

			if (count < 1) {
				return new Page();
			}

			 rs = JH_DBHelper.excuteQuery(jyConnection, fullHQLQuery);
			if (tableName.toUpperCase() == "QLR") {
				list = GetShares_QLR(rs);
			} else if (tableName.toUpperCase() == "FDCQ") {
				list = GetShares_QLR(rs);
			} else if (tableName.toUpperCase() == "YGDJ") {
				list = GetShares_YGDJ(rs);
			} else if (tableName.toUpperCase() == "DYAQ") {
				list = GetShares_DYAQ(rs);
			}
		} catch (Exception e) {
			YwLogUtil.addYwLog("查询中间库附件信息-失败", ConstValue.SF.NO.Value,ConstValue.LOG.ADD);
		}
		return new Page(Page.getStartOfPage(pageIndex, pageSize), count, pageSize, list);
	}
	
	
	/**
	 *  根据sql条件语句(where后面的部分)
	 * 
	 * @param condition
	 *            where后面的条件语句
	 * 
	 * @return
	 * @throws SQLException 
	 */
	private List<SHAREFJ> GetShares_DYAQ(ResultSet rs) throws SQLException {
		List<SHAREFJ> shareList = new ArrayList<SHAREFJ>();		
        SHAREFJ sharefj = null;
        while (rs.next()) {          
        	sharefj = new SHAREFJ();
        	 
        	 String qlr = rs.getString("DYR");
        	 String gxxmbh = rs.getString("GXXMBH");
        	 String ywh = rs.getString("YWH");
        	 sharefj.setQlr(qlr);
        	 sharefj.setGxxmbh(gxxmbh);
        	 sharefj.setYwh(ywh);
        	 shareList.add(sharefj);
        }   
        return shareList; 		
	}

	/**
	 *  根据sql条件语句(where后面的部分)
	 * 
	 * @param condition
	 *            where后面的条件语句
	 * 
	 * @return
	 * @throws SQLException 
	 */
	private List<SHAREFJ> GetShares_QLR(ResultSet rs) throws SQLException {
		List<SHAREFJ> shareList = new ArrayList<SHAREFJ>();		
        SHAREFJ sharefj = null;
        while (rs.next()) {          
        	sharefj = new SHAREFJ();
        	 
        	 String qlr = rs.getString("QLRMC");
        	 String gxxmbh = rs.getString("GXXMBH");
        	 String ywh = getYwhByGxxmbh(gxxmbh);
        	 sharefj.setQlr(qlr);
        	 sharefj.setGxxmbh(gxxmbh);
        	 sharefj.setYwh(ywh);
        	 shareList.add(sharefj);
        }   
        return shareList; 		
	}
	

	private List<SHAREFJ> GetShares_YGDJ(ResultSet rs) throws SQLException {
		
		List<SHAREFJ> shareList = new ArrayList<SHAREFJ>();		
        SHAREFJ sharefj = null;
        while (rs.next()) {          
        	sharefj = new SHAREFJ();
        	         	
        	 String gxxmbh = rs.getString("GXXMBH");        	 
        	 String ywh = rs.getString("YWH");
        	 String ywr = rs.getString("YWR");
        	 sharefj.setQlr(ywr);
        	 sharefj.setGxxmbh(gxxmbh);
        	 sharefj.setYwh(ywh);
        	 shareList.add(sharefj);
        }   
        return shareList; 	
	}
	
	private String getYwhByGxxmbh(String gxxmbh) throws SQLException {
		String fullHQLQuery = "SELECT * FROM FDCQ  Where GXXMBH ='"+ gxxmbh +"'";
		String ywh = null;
		ResultSet rs = getConnectionByFullSql(fullHQLQuery);		
		while (rs.next()) {
			ywh = rs.getString("YWH");
		}
		if (StringHelper.isEmpty(ywh)) {
			ResultSet rs1 = getConnectionByFullSql("SELECT * FROM DYAQ  Where GXXMBH ='"+ gxxmbh +"'");
			while (rs1.next()) {
				ywh = rs1.getString("YWH");
			}
		}
		if (StringHelper.isEmpty(ywh)) {
			ResultSet rs2 = getConnectionByFullSql("SELECT * FROM BGQFDCQ  Where GXXMBH ='"+ gxxmbh +"'");
			while (rs2.next()) {
				ywh = rs2.getString("YWH");
			}
		}
		if (StringHelper.isEmpty(ywh)) {
			ResultSet rs3 = getConnectionByFullSql("SELECT * FROM H  Where GXXMBH ='"+ gxxmbh +"'");
			while (rs3.next()) {
				ywh = rs3.getString("YWH");
			}
		}
		if (StringHelper.isEmpty(ywh)) {
			ResultSet rs4 = getConnectionByFullSql("SELECT * FROM YGDJ  Where GXXMBH ='"+ gxxmbh +"'");
			while (rs4.next()) {
				ywh = rs4.getString("YWH");
			}
		}
		return ywh;		
	}
	private String getQlrByGxxmbh(String gxxmbh) throws SQLException {
		String fullHQLQuery = "SELECT * FROM QLR  Where GXXMBH ='"+ gxxmbh +"'";
		String qlr = null;
		ResultSet rs = getConnectionByFullSql(fullHQLQuery);
		while (rs.next()) {
			qlr = rs.getString("QLRMC");
		}
		
		return qlr;		
	}
	/**
	 *  根据sql条件语句(where后面的部分)
	 * 
	 * @param condition
	 *            where后面的条件语句
	 * 
	 * @return
	 * @throws SQLException 
	 */
	public List<QLR> GetQlrs(String tableName, String sqlCondition) throws SQLException {
		
		List<QLR> list = new ArrayList<QLR>();
		QLR qlr =  null;		
		String fullHQLQuery = "SELECT * FROM "+ tableName +" t Where "+ sqlCondition;
		
		IConnectionPool  pool = ConnectionPoolManager.getInstance().getPool("yingtanPool"); 
		Connection conn = pool.getConnection();			
		PreparedStatement ps = conn.prepareStatement(fullHQLQuery);
		ResultSet rs = ps.executeQuery();
		if (tableName.toUpperCase() == "QLR") {
			while (rs.next()) {          
	        	qlr = new QLR();
	        	 	        	 
	        	 qlr.setQLRID(rs.getNString("QLRID"));        		
	        	 qlr.setQLID(rs.getNString("QLID"));        	 
	        	 qlr.setSXH(rs.getInt("SXH"));         		
	        	 qlr.setQLRMC(rs.getNString("QLRMC"));        		       		
	        	 qlr.setBDCQZH(rs.getNString("BDCQZH"));    		
	        	 qlr.setZJZL(rs.getNString("ZJZL"));
	        	 qlr.setZJH(rs.getNString("ZJH"));
	        	 qlr.setFZJG(rs.getNString("FZJG"));
	        	 qlr.setSSHY(rs.getNString("SSHY"));
	        	 qlr.setGJ(rs.getNString("GJ"));
	        	 qlr.setHJSZSS(rs.getNString("HJSZSS"));
	        	 qlr.setXB(rs.getNString("XB"));
	        	 qlr.setDH(rs.getNString("DH"));
	        	 qlr.setDZ(rs.getNString("DZ"));
	        	 qlr.setYB(rs.getNString("YB"));
	        	 qlr.setGZDW(rs.getNString("GZDW"));        		
	        	 qlr.setDZYJ(rs.getNString("DZYJ"));
	        	 qlr.setQLRLX(rs.getNString("QLRLX"));
	        	 qlr.setQLBL(rs.getNString("QLBL"));
	        	 qlr.setGYFS(rs.getNString("GYFS"));
	        	 qlr.setGYQK(rs.getNString("GYQK"));
	        	 qlr.setBZ(rs.getNString("BZ"));
	        	 qlr.setISCZR(rs.getNString("ISCZR"));
	        	 qlr.setYSDM(rs.getNString("YSDM"));
	        	 qlr.setBDCDYH(rs.getNString("BDCDYH"));
	        	 qlr.setQZYSXLH(rs.getNString("QZYSXLH"));
	        	 qlr.setGXLX(rs.getNString("GXLX"));
	        	 qlr.setGXXMBH(rs.getNString("GXXMBH"));
	        	
	        	 list.add(qlr);
	        }  
		}
		
		conn.close();
		return list;
	}
	// 通过全部的sql获取连接
	public ResultSet getConnectionByFullSql(String fullSql) {
		ResultSet rs = null;
		Connection jyConnection = null;
		try {
			jyConnection = JH_DBHelper.getConnect_jy();
       		
			rs = JH_DBHelper.excuteQuery(jyConnection, fullSql);
			
		} catch (Exception e) {
			System.err.println("获取中间库连接失败，请检查中间库是否联通");
		}
		return rs;		
	}

	// 中间库查到的房地产权属性信息
	public List<FDCQ> GetFdcqs(String tableName, String sqlCondition) throws java.sql.SQLException {

		List<FDCQ> list = new ArrayList<FDCQ>();
		FDCQ fdcq = null;
		String fullHQLQuery = "SELECT * FROM "+ tableName +" t Where "+ sqlCondition;
		ResultSet rs = getConnectionByFullSql(fullHQLQuery);
		while (rs.next()) {
			fdcq = new FDCQ();
			
			fdcq.setGXXMBH(rs.getNString("GXXMBH"));
			fdcq.setYSDM(rs.getNString("YSDM"));
			fdcq.setBDCDYH(rs.getNString("BDCDYH"));
			fdcq.setYWH(rs.getString("YWH"));
			fdcq.setQLLX(rs.getNString("QLLX"));
			fdcq.setDJLX(rs.getNString("DJLX"));
			fdcq.setDJYY(rs.getNString("DJYY"));
			fdcq.setFDZL(rs.getNString("FDZL"));
			fdcq.setFDCJYJG(rs.getDouble("FDCJYJG"));
			fdcq.setGHYT(rs.getNString("GHYT"));
			fdcq.setFWXZ(rs.getNString("FWXZ"));
			fdcq.setFWJG(rs.getNString("FWJG"));
			fdcq.setSZC(rs.getNString("SZC"));
			fdcq.setZCS(rs.getInt("ZCS"));
			fdcq.setJZMJ(rs.getDouble("JZMJ"));
			fdcq.setZYJZMJ(rs.getDouble("ZYJZMJ"));
			fdcq.setFTJZMJ(rs.getDouble("FTJZMJ"));
			fdcq.setJGSJ(rs.getDate("JGSJ"));								
			fdcq.setBDCQZH(rs.getNString("BDCQZH"));
			fdcq.setQXDM(rs.getNString("QXDM"));
			fdcq.setDJJG(rs.getNString("DJJG"));
			fdcq.setDBR(rs.getNString("DBR"));
			fdcq.setDJSJ(rs.getDate("DJSJ"));
			fdcq.setFJ(rs.getNString("FJ"));
			fdcq.setQSZT(rs.getNString("QSZT"));
			fdcq.setQLID(rs.getNString("QLID"));
			fdcq.setRELATIONID(rs.getNString("RELATIONID"));			

			list.add(fdcq);
		}
		return list;
	}

	// 中间库查到的权利人信息
	public  List<QLR> resultSetToList_QLR(ResultSet rs) throws java.sql.SQLException {   
       
        List<QLR> list = new ArrayList<QLR>();
        QLR qlr = null;
        while (rs.next()) {          
        	qlr = new QLR();
        	       
        	 qlr.setQLRID(rs.getNString("QLRID"));        		
        	 qlr.setQLID(rs.getNString("QLID"));        	 
        	 qlr.setSXH(rs.getInt("SXH"));         		
        	 qlr.setQLRMC(rs.getNString("QLRMC"));        		       		
        	 qlr.setBDCQZH(rs.getNString("BDCQZH"));    		
        	 qlr.setZJZL(rs.getNString("ZJZL"));
        	 qlr.setZJH(rs.getNString("ZJH"));
        	 qlr.setFZJG(rs.getNString("FZJG"));
        	 qlr.setSSHY(rs.getNString("SSHY"));
        	 qlr.setGJ(rs.getNString("GJ"));
        	 qlr.setHJSZSS(rs.getNString("HJSZSS"));
        	 qlr.setXB(rs.getNString("XB"));
        	 qlr.setDH(rs.getNString("DH"));
        	 qlr.setDZ(rs.getNString("DZ"));
        	 qlr.setYB(rs.getNString("YB"));
        	 qlr.setGZDW(rs.getNString("GZDW"));        		
        	 qlr.setDZYJ(rs.getNString("DZYJ"));
        	 qlr.setQLRLX(rs.getNString("QLRLX"));
        	 qlr.setQLBL(rs.getNString("QLBL"));
        	 qlr.setGYFS(rs.getNString("GYFS"));
        	 qlr.setGYQK(rs.getNString("GYQK"));
        	 qlr.setBZ(rs.getNString("BZ"));
        	 qlr.setISCZR(rs.getNString("ISCZR"));
        	 qlr.setYSDM(rs.getNString("YSDM"));
        	 qlr.setBDCDYH(rs.getNString("BDCDYH"));
        	 qlr.setQZYSXLH(rs.getNString("QZYSXLH"));
        	 qlr.setGXLX(rs.getNString("GXLX"));
        	 qlr.setGXXMBH(rs.getNString("GXXMBH"));
        	 
        	 list.add(qlr);         
        }   
        return list;   
	}
	
	// 中间库查到的房地产权属性信息
		public  List<FDCQ> resultSetToList_FDCQ(ResultSet rs) throws java.sql.SQLException {   
	       
	        List<FDCQ> list = new ArrayList<FDCQ>();
	        FDCQ fdcq = null;
	        while (rs.next()) {          
	        	fdcq = new FDCQ();
	        	
				fdcq.setGXXMBH(rs.getNString("GXXMBH"));
				fdcq.setYSDM(rs.getNString("YSDM"));
				fdcq.setBDCDYH(rs.getNString("BDCDYH"));
				fdcq.setYWH(rs.getString("YWH"));
				fdcq.setQLLX(rs.getNString("QLLX"));
				fdcq.setDJLX(rs.getNString("DJLX"));
				fdcq.setDJYY(rs.getNString("DJYY"));
				fdcq.setFDZL(rs.getNString("FDZL"));
				fdcq.setFDCJYJG(rs.getDouble("FDCJYJG"));
				fdcq.setGHYT(rs.getNString("GHYT"));
				fdcq.setFWXZ(rs.getNString("FWXZ"));
				fdcq.setFWJG(rs.getNString("FWJG"));
				fdcq.setSZC(rs.getNString("SZC"));
				fdcq.setZCS(rs.getInt("ZCS"));
				fdcq.setJZMJ(rs.getDouble("JZMJ"));
				fdcq.setZYJZMJ(rs.getDouble("ZYJZMJ"));
				fdcq.setFTJZMJ(rs.getDouble("FTJZMJ"));
				fdcq.setJGSJ(rs.getDate("JGSJ"));								
				fdcq.setBDCQZH(rs.getNString("BDCQZH"));
				fdcq.setQXDM(rs.getNString("QXDM"));
				fdcq.setDJJG(rs.getNString("DJJG"));
				fdcq.setDBR(rs.getNString("DBR"));
				fdcq.setDJSJ(rs.getDate("DJSJ"));
				fdcq.setFJ(rs.getNString("FJ"));
				fdcq.setQSZT(rs.getNString("QSZT"));
				fdcq.setQLID(rs.getNString("QLID"));
				fdcq.setRELATIONID(rs.getNString("RELATIONID"));
	        	 
	        	 list.add(fdcq);         
	        }   
	        return list;   
		}
		
		// 中间库查到的抵押权信息
				public  List<DYAQ> resultSetToList_DYAQ(ResultSet rs) throws java.sql.SQLException {   
			       
			        List<DYAQ> list = new ArrayList<DYAQ>();
			        DYAQ dyaq = null;
			        while (rs.next()) {          
			        	dyaq = new DYAQ();
			        	
			        	 dyaq.setYWH(rs.getNString("YWH"));        		
			        	 dyaq.setBDCDYH(rs.getNString("BDCDYH"));       		
			        	 dyaq.setQLID(rs.getString("QLID"));         		
			        	 dyaq.setDJLX(rs.getNString("DJLX"));        		       		
			        	 dyaq.setDJYY(rs.getNString("DJYY"));    		
			        	 dyaq.setZWLXQSSJ(rs.getDate("ZWLXQSSJ"));
			        	 dyaq.setZWLXJSSJ(rs.getDate("ZWLXJSSJ"));
			        	 dyaq.setBDCDJZMH(rs.getNString("BDCDJZMH"));
			        	 dyaq.setQXDM(rs.getNString("QXDM"));
			        	 dyaq.setDJJG(rs.getNString("DJJG"));
			        	 dyaq.setDBR(rs.getNString("DBR"));
			        	 dyaq.setDJSJ(rs.getDate("DJSJ"));
			        	 dyaq.setFJ(rs.getNString("FJ"));
			        	 dyaq.setQSZT(rs.getNString("QSZT"));
//			        	 dyaq.setBDCDYID(rs.getNString("BDCDYID"));
			        	 dyaq.setGXXMBH(rs.getNString("GXXMBH"));        		
			        	 dyaq.setDYFS(rs.getNString("DYFS"));
			        	 dyaq.setBDBZZQSE(rs.getDouble("BDBZZQSE"));
			        	 dyaq.setZGZQQDSS(rs.getNString("ZGZQQDSS"));
			        	 dyaq.setZGZQSE(rs.getDouble("ZGZQSE"));
			        	 dyaq.setZXDYYWH(rs.getNString("ZXDYYWH"));
			        	 dyaq.setZXDYYY(rs.getNString("ZXDYYY"));
			        	 dyaq.setZXSJ(rs.getDate("ZXSJ"));
			        	 dyaq.setDYBDCLX(rs.getNString("DYBDCLX"));
			        	 dyaq.setZJJZWZL(rs.getNString("ZJJZWZL"));
			        	 dyaq.setZJJZWDYFW(rs.getNString("ZJJZWDYFW"));
			        	 dyaq.setDYR(rs.getNString("DYR"));
			        	 dyaq.setLYQLID(rs.getNString("LYQLID"));
			        	 dyaq.setYSDM(rs.getNString("YSDM"));
			        	 dyaq.setRELATIONID(rs.getString("RELATIONID"));
			        	 
			        	 
			        	 list.add(dyaq);         
			        }   
			        return list;   
				}
				
				// 中间库查到的抵押权信息
				public  List<CFDJ> resultSetToList_CFDJ(ResultSet rs) throws java.sql.SQLException {   
			       
			        List<CFDJ> list = new ArrayList<CFDJ>();
			        CFDJ cfdj = null;
			        while (rs.next()) {          
			        	cfdj = new CFDJ();
			        	
			        	 cfdj.setBDCDYH(rs.getNString("BDCDYH"));        		
			        	 cfdj.setYWH(rs.getNString("YWH"));       		
			        	 cfdj.setQLID(rs.getString("QLID"));         		
			        	 cfdj.setCFLX(rs.getNString("CFLX"));        		       		
			        	 cfdj.setCFJG(rs.getNString("CFJG"));    		
			        	 cfdj.setCFWH(rs.getNString("CFWH"));
			        	 cfdj.setCFWJ(rs.getNString("CFWJ"));
			        	 cfdj.setCFFW(rs.getNString("CFFW"));
			        	 cfdj.setJFJG(rs.getNString("JFJG"));
			        	 cfdj.setJFWH(rs.getNString("JFWH"));
			        	 cfdj.setJFWJ(rs.getNString("JFWJ"));
			        	 cfdj.setCFQSSJ(rs.getDate("CFQSSJ"));
			        	 cfdj.setCFJSSJ(rs.getDate("CFJSSJ"));
			        	 cfdj.setQXDM(rs.getNString("QXDM"));
			        	 cfdj.setDJJG(rs.getNString("DJJG"));
			        	 cfdj.setDBR(rs.getNString("DBR"));        		
			        	 cfdj.setDJSJ(rs.getDate("DJSJ"));
			        	 cfdj.setJFYWH(rs.getNString("JFYWH"));
			        	 cfdj.setJFDBR(rs.getNString("JFDBR"));
			        	 cfdj.setJFDJSJ(rs.getDate("JFDJSJ"));
			        	 cfdj.setFJ(rs.getNString("FJ"));
			        	 cfdj.setQSZT(rs.getNString("QSZT"));
//			        	 cfdj.setBDCDYID(rs.getNString("BDCDYID"));
			        	 cfdj.setGXXMBH(rs.getNString("GXXMBH"));
			        	 cfdj.setLYQLID(rs.getNString("LYQLID"));
			        	 cfdj.setRELATIONID(rs.getString("RELATIONID"));
			        	 
			        	 
			        	 list.add(cfdj);         
			        }   
			        return list;   
				}
}
