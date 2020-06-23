package com.supermap.internetbusiness.service;

import com.supermap.wisdombusiness.synchroinline.model.JsonMessage;

public interface BusinessreviewService {

	/**
	 * 分页查询外网申请办理的业务
	 * @param pageIndex 第几页
	 * @param pageSize 每页多少行记录
	 * @param sqr 申请人姓名
	 * @param bdcqzh pro_proinst表的字段
	 * @param zjh 申请人的证件号
	 * @param zjlx 申请人的证件类型
	 * @param status 业务办理状态
	 * @param deadline_s 开始办理的时间
	 * @param deadline_e
	 * @param isvaguequery 是否模糊查询
	 * @param prolsh 查询内外网的流水号
	 * @param ywlx 企业或个人业务
	 * @return
	 */
	public JsonMessage getAllData(int pageIndex, int pageSize, String sqr,String bdcqzh, String zjh,
			String zjlx, String status, String deadline_s, String deadline_e, String isvaguequery,String prolsh,
			String ywlx,String zl, String phone,String sfjsbl,String flag,String prodefid);

	/**
	 * 个人业务：根据申请人的义务人的姓名、证件号、不动产权证，到bdck中判断是否存在对应的单元列表数据
	 * 分页查询。
	 * @param pageIndex
	 * @param pageSize
	 * @param id
	 * @param djlx
	 * @param qllx
	 * @param bdcqzmh 
	 * @return
	 */
	public JsonMessage getPersonalAuditInfoData(int pageIndex, int pageSize,String id, String djlx, String qllx, String bdcqzmh);

	/**
	 * 企业业务：根据项目的房屋信息表，对应的业务流程。判断bdck中是否已经存在房屋数据。
	 * @param parseInt
	 * @param parseInt2
	 * @param id
	 * @param qllx 
	 * @param djlx 
	 * @param bdcqzh 
	 * @return
	 */
	public JsonMessage getEnterpriseAuditInfoData(int parseInt, int parseInt2, String id, String djlx, String qllx);

	/**
	 * 权利信息
	 * @param bdcdyid
	 * @return
	 */
	public JsonMessage getQlInfo(String bdcdyid);

	/**
	 * 根据项目id获取外网的申请人信息
	 * @param proinst_id
	 * @return
	 */
	public JsonMessage getsqrInfo(String proinst_id);

	
	/**
	 * 获取单元的信息，bdcdylx用来判断从什么表上取，djlx,qllx用来判断从bdck或bdcdck中的表取数据。
	 * @param bdcdyid 单元id
	 * @param bdcdylx 单元类型
	 * @param djlx 登记类型
	 * @param qllx 权利类型
	 * @return
	 */
	public JsonMessage getdyInfo(String bdcdyid, String bdcdylx, String djlx, String qllx);

	public JsonMessage getAuditInfoData(int pageIndex, int pageSize, String id,String isshowbtn) throws Exception;

}
