package com.supermap.realestate.registrationbook.service;

import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.supermap.realestate.registrationbook.model.BookMenu;
import com.supermap.wisdombusiness.web.Message;

public interface BookService {

	/**
	 * @author WUZ 获取宗地宗海信息
	 */
	public Message GetZDORZHList(Map<String, String> conditionParameter,
			int currentpage, int pageSize) throws Exception;

	/**
	 * @author WUZ 获取登记簿树形数据
	 */
	public Message GetBookTree(String zdOrZhBdcdyid,String bdcdylx,Map<String, String> mapcontion,long page,long rows);
	/**
	 * @author WUZ 异步加载获取不动产单元的权利类型菜单（优化登记簿详情树形）
	 */
	public List<BookMenu> GetBookChildrenByAsync(String parentid,String bdcdyid,String bdcdylx);
	
	/**
	 * @author WUZ  传递不动产单元ID，获取该不动产单元的权利树形
	 */
	public List<BookMenu> GetUnitTree(String bdcdyid,String bdcdylx);
	/**
	 * @author WUZ 根据不动产单元ID获取宗地信息
	 */
	public Map GetZDXX(String bdcdyid);

	/**
	 * 根据不动产单元ID获取宗海信息
	 * 
	 * @作者：WUZHU
	 * @param bdcdyid
	 *            不动产单元ID
	 * @return
	 */
	public Map GetZHXX(String bdcdyid);

	/**
	 * @author WUZ 根据不动产单元ID和不动产单元类型获取首页信息
	 */
	public Map GetFMInfo(String bdcdyid, String bdcdylx);

	/**
	 * 根据不动产单元ID和权利类型或登记类型获取权利信息
	 * 
	 * @作者：WUZHU
	 * @param bdcdyid
	 *            不动产单元ID
	 * @param lx
	 *            权利类型或登记类型
	 * @return
	 */
	public Map GetQLInfoList(String bdcdyid, String bdcdylx, String qllxOrDjlx,
			long page, long pagesize);

	/**
	 * 根据不动产单元ID获取单元信息列表 目录
	 * 
	 * @作者：WUZHU
	 * @param bdcdyid
	 *            不动产单元ID
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public Map GetML(String bdcdyid, long page, long pagesize);

	/**
	 * 登记簿显示宗地图
	 * @param bdcdyid
	 * @author diaoliwei
	 * @date 2016-1-9 11:15:11
	 * @return
	 */
	public Blob getZDTImg(String bdcdyid) throws SQLException;
	
	/**
	 * 登记簿显示房地产平面图
	 * @param bdcdyh
	 * @author diaoliwei
	 * @date 2016-1-9 11:16:21
	 * @return
	 * @throws SQLException
	 */
	public Blob getFDCImg(String bdcdyh)throws SQLException;
	
	// / 以下的全为旧接口
	// /
	// /
	// /

	// /**
	// * @author WUZ 获取宗地基本信息
	// */
	// public BDCS_SHYQZD_LS_EX GetZDJBXX(String bdcdyid);

	// /**
	// * @author WUZ 根据宗地或宗海代码 获取登记单元列表信息
	// */
	// public List<BDCS_DJDY_LS> GetDJDYList(String zddm);

	// /**
	// *
	// * @作者：俞学斌
	// * @param bdcdyid
	// * 不动产单元ID
	// * @param bdcdylx
	// * 不动产单元类型
	// * @return
	// */
	// public FMInfo GetFirstPageInfo(String bdcdyid, String bdcdylx);
	//
	// /**
	// *
	// * @作者：俞学斌
	// * @param bdcdyid
	// * 不动产单元ID
	// * @param qllx
	// * 权利类型
	// * @return
	// */
	// @SuppressWarnings("rawtypes")
	// public List<Map> GetListQLInfo(String bdcdyid, String qllx,long page,long
	// pagesize);
	//
	// /**
	// *
	// * @作者：WUZHU
	// * @param bdcdyid
	// * 不动产单元ID
	// * @param qllx
	// * 权利类型
	// * @return
	// */
	// public List<QLInfo> GetListDJXX(String bdcdyid,String qllxs, long page,
	// long pagesize);
	//
	// /**
	// *
	// * @作者：俞学斌
	// * @param bdcdyid
	// * 不动产单元ID
	// * @return
	// */
	// @SuppressWarnings("rawtypes")
	// public List<Map> GetDYList(String bdcdyid,long page,long pagesize);
	//
	// /**
	// *
	// * @作者：俞学斌
	// * @param bdcdyid
	// * 不动产单元ID
	// * @return
	// */
	// @SuppressWarnings("rawtypes")
	// public List<Map> GetDYIndexList(String bdcdyid);
	//
	// /**
	// *
	// * @作者：WUZHU
	// * @param bdcdyid
	// * 不动产单元ID
	// * @return
	// */
	// @SuppressWarnings("rawtypes")
	// public List<Map> GetListCFDJ(String bdcdyid, long page,
	// long pagesize);
	//
	// /**
	// *
	// * @作者：WUZHU
	// * @param bdcdyid
	// * 获取宗海基本信息
	// * @return
	// */
	// public ZHJBXXInfo GetListZHXX(String bdcdyid, long page,
	// long pagesize);
	//
	// /**
	// *
	// * @作者：WUZHU
	// * @param bdcdyid
	// * 获取异议登记信息
	// * @return
	// */
	// public List<QLInfo> GetListYYDJ(String bdcdyid, long page,
	// long pagesize);
	//
	// /**
	// *
	// * @作者：WUZHU
	// * @param bdcdyid
	// * 获取土地所有权信息
	// * @return
	// */
	// public List<QLInfo> GetListSYQZD(String bdcdyid, long page,
	// long pagesize);
	//
	// /**
	// *
	// * @作者：WUZHU
	// * @param bdcdyid
	// * 获取预告登记信息
	// * @return
	// */
	// public List<QLInfo> GetListYGDJ(String bdcdyid, long page,
	// long pagesize);
	//
	// /**
	// *
	// * @作者：WUZHU
	// * @param bdcdyid
	// * 获取林权登记信息
	// * @return
	// */
	// public List<QLInfo> GetListLQDJ(String bdcdyid, long page,
	// long pagesize);
	//
	// /**
	// *
	// * @作者：WUZHU
	// * @param bdcdyid
	// * 获取海域登记信息
	// * @return
	// */
	// public List<QLInfo> GetListHYDJ(String bdcdyid, long page,
	// long pagesize);
	//
	// /**
	// *
	// * @作者：WUZHU
	// * @param
	// * 根据模板和字段数据生成PDF流
	// * @return
	// * @throws IOException
	// * @throws DocumentException
	// */
	// public ByteArrayOutputStream CreatePdfStream(String tplName,
	// Map<String,Object> data) throws IOException, DocumentException;
}
