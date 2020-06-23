package com.supermap.realestate_gx.registration.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;




import com.supermap.wisdombusiness.web.Message;


/**
 * 
 * 广西不动产查档证明Service
 * 
 *
 * @Copyright SuperMap
 */
public interface GX_RealestateSearchService {

	/**
	 * （不动产信息查档）读取Excle模版中的内容并返回页面显示
	 */
	public Map<String,Object>  GetQueryRealestateList(String file_path);
	/**
	 * @param sfall 查询全部不动产：0是，1不是。
	 * @param sfhouse 查询现房：0是，1不是。
	 * @param sfqhouse 查询期房：0是，1不是。
	 * @param sfshyqzd 查询使用权宗地：0是，1不是。
	 * @param sfsyqzd 查询所有权宗地：0是，1不是。
	 * @param sfsea 查询海域：0是，1不是。
	 * @param sfld 查询林地：0是，1不是。
	 * @param sfnyd 查询农用地：0是，1不是。
	 * @param queryVauleList
	 * 将表格里的信息和根据权利人名称、身份证号从不动产库查出来的结果做数据比较，并填充
	 */
	public Message  getMatchData(String sfall, String sfhouse, String sfqhouse,String sfshyqzd,String sfsyqzd, String sfsea, String sfld,String sfnyd, List<Map> queryVauleList );
	/**
	 * 返回不动产查档证明内容
	 * @author liangc
	 */
	public Message getBDCPLPrintZM(List<Map>  results, String jbr);
	/**
	  *个人查档：根据名称和证件号码获取对应的不动产
	 * @author liangc
	 * @date 2018-04-28 9:44:30
	 * @param xm 姓名
	 * @param zjh 证件号
	 * @param jbr 经办人
	 * @param cdr 查档人
	 * @param sfall 查询全部不动产：0是，1不是。
	 * @param sfhouse 查询现房：0是，1不是。
	 * @param sfqhouse 查询期房：0是，1不是。
	 * @param sfshyqzd 查询使用权宗地：0是，1不是。
	 * @param sfsyqzd 查询所有权宗地：0是，1不是。
	 * @param sfsea 查询海域：0是，1不是。
	 * @param sfld 查询林地：0是，1不是。
	 * @param sfnyd 查询农用地：0是，1不是。
	 * @return
	 */
	public Message getWhthinMatchData(String xm, String zjh, String jbr, String cdr,
			String sfall, String sfhouse, String sfqhouse,String sfshyqzd,String sfsyqzd,String sfsea,String sfld,String sfnyd);
	
	public Message bdcdaQuery(Map<String, String> queryvalues, Integer page,
			Integer rows,boolean iflike);
	
}
