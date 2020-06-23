package com.supermap.realestate_gx.registration.service;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.ResponseBody;

import com.supermap.wisdombusiness.web.Message;


/**
 * 
 * 广西档案查档证明Service
 * 
 * @author 何开胜
 *
 * @Copyright SuperMap
 */
public interface GX_ArchivesWhthinService {

	/**
	 * 读取Excle模版中的权利人名称和身份证号返回页面显示
	 */
	public Map<String,Object>  GetLogQueryList(String cdyt, String sffk, String file_path);
	/**
	 * （宗地信息查档）读取Excle模版中的内容并返回页面显示
	 */
	public Map<String,Object>  GetQueryLandList(String file_path);
	/**
	 * 将表格里的信息和根据权利人名称、身份证号从不动产库查出来的结果做数据比较，并填充
	 */
	public Message  getMatchData(String cdyt, String sffk, String sfyg,String sfzl,List<Map> queryVauleList );
	/**
	 * （宗地信息查档）将表格里的信息和根据权利人名称、身份证号从不动产库查出来的结果做数据比较，并填充
	 */
	public Message  getLandMatchData(HttpServletRequest request, List<Map> queryVauleList );
	/**
	 * 返回购房补贴查证明内容
	 * @author heks
	 */
	public Message getPlPrntZM(List<Map>  results, String jbr);
	/**
	 * 返回权利查档证明内容
	 * @author heks
	 */
	public Message getSwplPrntZM(List<Map>  results, String jbr);
	/**
	 * 返回宗地查档证明内容
	 * @author heks
	 */
	public Message getZDPLPrintZM(List<Map>  results, String jbr);
	/**
	  *个人查档：根据名称和证件号码获取对应的房屋产权和其他权利
	 * @author huangpeifeng
	 * @date 20170927
	 * @param xm 姓名
	 * @param zjh 证件号
	 * @return
	 */
	public Message getWhthinMatchData(String xm, String zjh, String jbr, String cdr);
	
	public Message queryLand(Map<String, String> queryvalues, int page, int rows);
}
