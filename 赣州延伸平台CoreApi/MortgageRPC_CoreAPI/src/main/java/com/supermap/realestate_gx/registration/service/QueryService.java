package com.supermap.realestate_gx.registration.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.supermap.wisdombusiness.web.Message;
import com.supermap.wisdombusiness.web.ui.tree.Tree;

/**
 * 
 * @Description:查询service 查询相关的服务都放到里边
 */
public interface QueryService {

	public Message queryAutoUnlock(Map<String, String> conditionmap, int page, int rows,boolean ifunlock);
	public Message MortgageList(Map<String, String> conditionmap, int page, int rows);
	public Message Unlock(String qlid,String fj);
	public Message MortgageCancel(String qlid,String zxdjyy,String zxfj,String zxywbh);
	public Message queryDiyazx(Map<String, String> conditionmap, int page, int rows);
	public Message querySplit(Map<String, String> conditionmap, int page, int rows);
	public Message SplitQL(String qlid);
	/**
	 * 获取发证列表不分页
	 * 
	 * @param xmbh
	 * @param page
	 * @param rows
	 * @return
	 */
	public Message GetFZList(String xmbh);
	/**
	 * //liangc 获取登记业务统计明细
	 * @param queryvalues
	 * @param page
	 * @param rows
	 * @return
	 */
	public Message djywtj(Map<String, String> queryvalues, Integer page,
			Integer rows);
	/**
	 * liangc 登记业务统计广西
	 * @param tjsjqs
	 * @param tjsjjz
	 * @return
	 */
	public Message GetDJYWTJ_GX(String tjsjqs, String tjsjjz);
		/**
	 * 查询是否多单元一本证
	 */
	public Message getSFHBZS(String xmbh);
	
	public void AddBuilding(String xmbh);
	public void AddLand(String xmbh);
	
	/**
	 * 办件进度查询
	 * @param queryvalues
	 * @param page
	 * @param rows
	 * @return
	 */
	public Message djjdQuery(Map<String, String> queryvalues, Integer page,
			Integer rows);
	
	public Message queryCFInfoFromExcle(Map<String, String> queryvalues,boolean iflike,int page,int rows,String sort,String order);
	public Message deleteCfByCfbh(String cfbh);
	
	public List<Map> AddSFWS(String cfxxbh,MultipartFile file);
	public List<Tree> getfilesByfolderID(String id);
	public String saveFiles(String cfxxbh, List<Map> fileInfos);
	/**
	 * liangc 登记发证量和上报量业务统计
	 * @param tjsjqs
	 * @param tjsjjz
	 * @return
	 */
	public Message GetFZL_SBLYWTJ(String tjsjqs, String tjsjjz);
	
	/**
	 * @author liangcheng
	 * @time 2018年4月15日 上午11点32分
	 * @param queryvalues
	 * @param page
	 * @param rows
	 * @return
	 */
	public Message zxywQuery(Map<String, String> queryvalues, Integer page,
			Integer rows);
}
