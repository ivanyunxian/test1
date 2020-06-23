package com.supermap.realestate.registration.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartFile;

import com.supermap.realestate.registration.ViewClass.QueryClass.QLInfo;
import com.supermap.realestate.registration.model.BDCS_DYXZ;
import com.supermap.realestate.registration.model.BDCS_XZCF;
import com.supermap.realestate.registration.model.BDCS_XZDY;
import com.supermap.realestate.registration.model.interfaces.RealUnit;
import com.supermap.wisdombusiness.web.Message;
import com.supermap.wisdombusiness.web.ResultMessage;

/**
 * 
 * @Description:查询service 查询相关的服务都放到里边
 * @author 俞学斌
 * @date 2015年8月25日 下午09:43:22
 */
public interface QueryService {

	public List<Map> GetHInfo(String bdcdyid, String bdcdylx);

	public QLInfo GetQLInfo_XZ(String bdcdyid);
	public QLInfo GetQLInfo_XZ1(String bdcdyid);
	public QLInfo GetQLInfo(String qlid);
	@SuppressWarnings("rawtypes")
	public Map GetAllList(String bdcdyid);
	
	@SuppressWarnings("rawtypes")
	public List<List<Map>> GetQLList(String ycscbdcdyid);

	public QLInfo GetQLInfo(String qlid,String djzt);
	
	public Message queryMessage(Map<String, String> conditionmap, int page, int rows,boolean iflike);

	public Message queryHouseyt(Map<String, String> conditionmap, int page, int rows,boolean iflike,String fwzt,String sort,String desc,String zddy,String fw_xzqh,String fw_qlrlx,
			String fw_fwyt,String fw_fwlx,String fw_fwxz, String fw_hx,String fw_hxjg,String fw_fwjg,String fw_fwcb,String fw_fzdy ,String fzlb);
	public Message queryHouse(Map<String, String> queryvalues, int page,int rows, boolean iflike, String fwzt,String sort,String order);
	public Message queryHouseForUnit(Map<String, String> queryvalues, int page, int rows, boolean iflike, String fwzt,String sort,String order);
	public Message queryLand(Map<String, String> conditionmap, int page, int rows,boolean iflike,String tdzt,String sort,String desc);
	public Message queryLandyt(Map<String, String> conditionmap, int page, int rows,boolean iflike,String tdzt,String sort,String desc,
			String td_xzqh,String td_qllx,String td_qlsdfs,String td_qlxz,String td_tddj,String td_qlrlx,String td_tdyt,String td_fzdy,String fzlb,String zddy);
	public Message querybuilding(Map<String, String> conditionmap, int page,String Szl,String Sbdcdyh,String Slpzt,String Sxmmc,int rows,boolean iflike,String kfsmc);
	public Message queryfwlist(String zrzbdcdyid,Map<String, String> querycondition, int page, int row);

	@SuppressWarnings("rawtypes")
	public List<List<Map>> GetDYDJList_XZ(String bdcdyid);
	
	@SuppressWarnings("rawtypes")
	public List<List<Map>> GetDYDJList_XZ1(String bdcdyid);

	@SuppressWarnings("rawtypes")
	public List<List<Map>> GetCFDJList_XZ(String bdcdyid);

	@SuppressWarnings("rawtypes")
	public List<List<Map>> GetYYDJList_XZ(String bdcdyid);
	
	public String  GetScidandYcid(String bdcdyid);
	
	public Message GetYdyFwInfo(String bdcdyid,int page,int rows);
	
	public Message GetFwInfoByZrz(String zrzbdcdyid, int page, int rows);
	
	public Message GetZrzList(String zdbdcdyid,String type,Map<String, String> querycondition, int page, int rows);
	
	public Message GetZrzXx(String zdbdcdyid,String type,Map<String, String> querycondition, int page, int rows);
	
	public List<HashMap<String, String>> HouseStatusQuery_RelationID(String RelationID,String bdcdylx);

	public List<HashMap<String, String>> HouseStatusQuery_ZL(String zl,
			String bdcdylx);

	public List<HashMap<String, String>> HouseStatusQuery_QZH(String qzh,
			String bdcdylx);

	public List<HashMap<String, String>> HouseStatusQuery_ALL(String zl,
			String qzh, String qlrmc, String bdcdylx);
	
	public HashMap<String,String> HouseStatusQuery_BDCDYID(
			String bdcdyid,String djdyid, String bdcdylx);

	public List<BDCS_DYXZ> GetDYXZList_XZ(String bdcdyid);

	public BDCS_DYXZ GetDYXZInfo(String id);

	 
	public void getFcfhtInfo(String bdcdyid,
			javax.servlet.http.HttpServletResponse response, String fileurl) throws IOException, SQLException;

	
	//根据不动产权证号查询，用户房屋信息
	public Map<String, String> HouseStatusQuery_QZHS(String qzh,String qzlb);

	public HashMap<String,Object> getUnitRelation(String bdcdyid, String bdcdylx);
	
	//根据权证号获取档案信息(海口)
	public Map<String, Object> GetAchives_HK(String type, String bdcqzh);

	////根据权证号设定档案类号和案卷号
	public Map<String, Object> SetAchives_HK(String bdcqzh,
			String archives_classno, String archives_bookno, String override);
	
	/**
	 * 根据权证号获取坐落信息
	 * @param bdcqzh 不动产权证号（证明号）
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException 
	 * @throws SQLException 
	 */
	public ResultMessage GetZlFromBDCQZH(String bdcqzh);

	public Message Batch(MultipartFile file, HttpServletRequest request);

	public String BatchQuery(HttpServletRequest request) throws UnsupportedEncodingException, IOException;

	public String GetTemptepl(HttpServletRequest request) throws IOException;
	
	public String getWLMQFW(HttpServletRequest request) throws IOException;
	
	public Message queryHouseByQlrmcAndZjhm(Map<String, String> queryvalues, boolean iflike, String fwzt,String sort,String order);
		
	public Message getZyProjectList(String status, String start,
			String end,String actdef_name,String order, String prodefname, 
			String staff, String sqr,String zjh);
	
	public Message queryIntegrated(Map<String, String> queryvalues, int page,
			int rows, boolean iflike, String sort, String order);

	public Message queryXTDZ(Map<String, String> queryvalues, Integer page,
			Integer rows, boolean iflike, String sort, String order, String lx);

	public Message UpdateXTDZ(String id, String lx ,String value);

	public Map<String, Map<String, String>> GetXTDZQLInfo(String bdcdyid);

	public List<BDCS_XZDY> GetXTDZdydjlist(String bdcdyid, String id);

	public List<BDCS_XZCF> GetXTDZcfdjlist(String bdcdyid, String id);
	
	public Message getQlInfo(String ywh);

	@SuppressWarnings("rawtypes")
	public Map GetXTDZdydjinfo(String id);

	@SuppressWarnings("rawtypes")
	public Map GetXTDZcfdjinfo(String id);

	@SuppressWarnings("rawtypes")
	public List<Map> GetXTDZDetail(HttpServletRequest request);

	public ResultMessage UpdateXTDZ(HttpServletRequest request);
	
	public List<Map> getAllQlInfoByQlr(String qlrmc,String qlrzjh,String bdcqzh);

	public Map<String, Object> QLInfoEX(String djdyid, String ql);

	public QLInfo GetQLInfo_XZEX(String bdcdyid);
	
	public void delLog(String id);

	public Map<String, Object> GetFWinfo(String xmbh, String djdyid, String bdcdyid, String qlid, String showlsh, String logid);
    //监管
	public Message queryHouseJG(Map<String, String> queryvalues, Integer page, Integer rows, boolean iflike,
			String fwzt, String sort, String order, String xzqh, String zddy, String fw_xzqh, String fw_qlrlx,
			String fw_fwyt, String fw_fwlx, String fw_fwxz, String fw_hx, String fw_hxjg, String fw_fwjg,
			String fw_fwcb, String fw_fzdy, String fzlb,String fw_djq);

	public Message queryLandJG(Map<String, String> queryvalues, Integer page, Integer rows, boolean iflike, String fwzt,
			String sort, String order, String xzqh, String td_xzqh, String td_qllx, String td_qlsdfs, String td_qlxz,
			String td_tddj, String td_qlrlx, String td_tdyt, String td_fzdy, String fzlb, String zddy,String djqdm);

	public Map<String, Object> LSQLInfoEX(String qlid);

	public Message blacklist(MultipartFile file, HttpServletRequest request);

	public Message Getblacklist(HttpServletRequest request) throws UnsupportedEncodingException;

	public Message Delblacklist(HttpServletRequest request);

	public Message Addblacklist(HttpServletRequest request);

	public Message Updateblacklist(HttpServletRequest request);

	public Message Duplicate(HttpServletRequest request);
	
	/**
	 * 批量上传分户图
	 * @param conditionmap
	 * @param page
	 * @param rows
	 * @param iflike
	 * @param sort
	 * @param order
	 * @return
	 */
	public Message queryHouseByZRZBDCDYH(Map<String, String> conditionmap, int page, int rows,boolean iflike,String sort,String order);
	
	
	/**
	 * 批量限制及查询
	 * @param conditionmap
	 * @param page
	 * @param rows
	 * @param iflike
	 * @param sort
	 * @param order
	 * @return
	 */
	public Message queryAndPLXZ(Map<String, String> conditionmap, int page, int rows,boolean iflike,String sort,String order,String hzt);
	/**
	 * 通过权利id查询具体单元信息，返回户BDCDYID,自然幢BDCDYID,宗地BDCDYID
	 * @param qlid
	 * @return
	 */
	public List<Map> queryUnitInfo(String qlid,String bdcdylx);

	public Map<String, String> GetQlrmcByZrz(String zrzbdcdyid, String bdcdylx);
	
	/**
	 * 通过户的BDCDYID返回所在自然幢的BDCDYID
	 * @param hbdcdyid
	 * @return
	 */
	public String getZrzBdcdyid(String hbdcdyid);

	
	/**
	 * 查询指认图
	 * @param conditionmap
	 * @param page
	 * @param rows
	 * @param iflike
	 * @param sort
	 * @param order
	 * @return
	 */
	public Message queryHouseByTime(Map<String, String> conditionmap, int page, int rows, boolean iflike, String sort);
	
	public Message querySea(Map<String, String> queryvalues, int page,int rows, boolean iflike,String sort,String order);

	//泸州查询
	public Object queryhousestate(String srcParam,String bdclx);

	Message queryLandForUnit(Map<String, String> queryvalues, int page,
			int rows, boolean iflike, String tdzt, String sort, String order);

	/**
	 * 根据权利人条件查询个人是否存在对应的数据
	 * @param name
	 * @param zjh
	 * @return
	 */
	public Message getPropertyInfoQuery(String name, String zjh, int pageIndex, int pageSize);

	Map<String, Object> QLInfoFSQLInfo(String djdyid);
	
	
 
	/**
	 * 交易一体化， 房屋查询，登记系统和商品房网签合同备案数据查询
	 * @param params
	 * @return
	 */
	public Message queryHouseJYYT(Map<String, Object> params);

}
