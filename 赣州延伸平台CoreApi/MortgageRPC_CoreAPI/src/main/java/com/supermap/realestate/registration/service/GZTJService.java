/**
 * 赣州统计服务
 */
package com.supermap.realestate.registration.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.supermap.realestate.registration.model.EasyUiTree;
import com.supermap.wisdombusiness.web.Message;
import com.supermap.wisdombusiness.web.ui.tree.Tree;

public interface GZTJService {
	
	/**
	 * 收费统计
	 * @param tjsjks 统计开始时间
	 * @param tjsjjz 统计截止时间
	 * @param isExcludeKFQ 是否排除开发区
	 * @return
	 */
	public Message GetSFXX(String tjsjks,String tjsjjz);
	
	public Message GetDepts();
	
	/**
	 * 房屋登记统计
	 * @return
	 */
	public Message GetFWDJ(String sfsj,String fwOrtd);
	/**
	 * 科室统计
	 * @param tjsjks 开始统计时间
	 * @param tjsjks 截止统计时间
	 * @param ywbh 科室负责的业务
	 * @param tjkg 统计开关
	 * @param outsl 受理时公用的业务编号中有其他科室负责的部分，需要过滤掉
	 * @param outdb 登薄时公用的业务编号中有其他科室负责的部分，需要过滤掉
	 * @param fwOrtd 统计的是房屋或土地
	 * @return
	 */
	public Message GetKSTJ(String tjsjks,String tjsjjz,String ywbh,Integer tjkg,String outsl,String outdb,String fwOrtd);
	
	/**
	 *代征税费统计
	 * @return
	 */
	public Message GetDZSF();
	
	/**
	 * 抵押登记抵押金额统计（被担保额、最高额）,可按时间段统计，可按时间段已月份为单位汇总
	 * @param tjsjks 开始统计时间 
	 * @param tjsjks 截止统计时间  
	 * @param isHZ   是否汇总统计 
	 * @param fwOrtd 统计的是房屋或土地 
	 * @return
	 */
	public Message GetDYDJ(String tjsjks,String tjsjjz,String fwOrtd);
	
	/**
	 * 预告登记统计
	 * @return
	 */
	public Message GetYGDJ();

	/**
	 * 登记业务统计报表
	 * @param tjsjks 开始统计时间 
	 * @param tjsjks 截止统计时间  
	 * @return
	 */
	public Message GetDJYWTJ(String tjsjks,String tjsjjz);
	
	/**
	 * 不动产档案统计报表  2016-12-08 luml
	 * @param tjsjks 开始统计时间 
	 * @param tjsjjz 截止统计时间  
	 * @return
	 */
	public Message GetBDCDATJ(String tjsjks,String tjsjjz);
	
	
	/**
	 * 科室办件量统计报表
	 * @param tjsjks 开始统计时间 
	 * @param tjsjks 截止统计时间  
	 * @return
	 */
	public Message GetKSYWTJ(String tjsjks,String tjsjjz,String deptid);
	/**
	 * 短信统计
	 * @param tjsjks 开始统计时间 
	 * @param tjsjks 截止统计时间  
	 * @return
	 */
	public Message GetDXTJ(String tjsjks,String tjsjjz,String deptid,String tjlx);
	/**
	 * 数据上报统计
	 * @param tjsjks 开始统计时间 
	 * @param tjsjks 截止统计时间  
	 * @return
	 */
	public Message GetSJSBTJ(String tjsjks,String tjsjjz,String deptid,String dedjlx,String tjlx);
	/**
	 * 登记中心办件量统计报表
	 * @param tjsjks 开始统计时间 
	 * @param tjsjks 截止统计时间  
	 * @return
	 */
	public Message GetDJZXYWTJ(String tjsjks,String tjsjjz);
	
	public List<Map> GetDeptInfo();
	
	public List<Tree> GetXzqhInfo();
	
	public List<Tree> GetDjlxInfo();

	public Message GetHotTJ(String slsjq, String slsjz, String statistics_type); 
	
	/**
	 * 
	 * 孝感登记发证统计
	 * @创建时间 2016年5月15日下午2:24:32
	 * @param slsjq 时间起
	 * @param slsjz 时间止
	 * @param statistics_type 统计类型（根据月份统计、根据年份统计）
	 * @return
	 */
	public Message GetDJFZTJ(String slsjq, String slsjz, String statistics_type); 
	
	/**
	 *  不动产权证移交表,可按时间段，权证类型统计
	 * @param tjsjks 开始统计时间 
	 * @param tjsjks 截止统计时间  
	
	 * @return
	 */
	public Message GetQZDJ(Map<String,String> mapCondition,Integer page,Integer rows);
	
	/**
	 *  下载不动产权证移交表,可按时间段，权证类型统计
	 * @param tjsjks 开始统计时间 
	 * @param tjsjks 截止统计时间  
	
	 * @return
	 */	
	public List<Map> QZYJDownload(Map<String,String> mapCondition);
	
	/**
	 * 	登记周报
	 * @param sjq 开始统计时间
	 * @param sjz 截止时间
	 */
	public Message GetBdcdjZB(String sjq, String sjz);
		
	/**
	 * 	效能统计
	 * @param sjq 开始统计时间
	 * @param sjz 截止时间
	 */
	public Message Getefficiency(String sjq,String sjz, String user);
	
	/**
	 * 发证面积统计
	 * 李名祥
	 * 二〇一六年六月四日 21:02:57
	 * @param sjq
	 * @param sjz
	 * @param tjmc
	 * @return
	 */
	public Message GetFZMJTJ(String sjq, String sjz, String tjmc);
	
	/**
	 * 预审科查询量统计
	 * @param tjsjks 开始统计时间
	 * @param tjsjjs 结束统计时间
	 * @return
	 */
	public Message getKSCXTJ(String tjsjks,String tjsjjs);
	
	/**
	 * 楼盘表统计
	 * 曹光报
	 * @param mapCondition
	 * @param page
	 * @param rows
	 * @return
	 */

	public Message GetLpbTJ(Map<String, String> mapCondition, Integer page, Integer rows);

	
	/**
	 * 楼盘表统计导出
	 * 曹光报
	 * @param mapCondition
	 * @return
	 */
	public List<Map> dowenloadLpbTJ(Map<String, String> mapCondition);
	
	public Message getMesLog(String id_sjq,String id_sjz,String type,String gl,int page,int rows);

	public Message InsertFzxx(String tjsjks, String tjsjjz);

	public void updateFzxx(String tjsjks, String tjsjjz,String type);
	
    public Message certinfodownload(HttpServletRequest request,HttpServletResponse response);
    
    /**
	 * 获取房屋基本状态
	 * 曹光报
	 * @param 
	 * @return
	 */
	public Message getFwjbzt(Map<String, String> mapCondition,int page,int rows);
	
	/**
	 * 微信信息统计
	 * 曹光报
	 * @param 
	 * @return
	 */
	public Message getWeiXins(String id_sjq, String id_sjz, String type,Integer page, Integer rows);
	/**
	 * 插入数据到微信信息统计表
	 * 曹光报
	 * @param 
	 * @return
	 */
	public Message InsertCxjd(String id_sjq, String id_sjz, String type);
	
	public Message GetDEPT(String qsj,String zsj, String fwOrtd,String selDept);	
		
	/**
	 * 全局查询统计数据
     *
	 */
	public Message GetQjCx();
   
	public Message getCFResult(String sjq, String sjz, String conres, int page,
			int rows);
	
	public Message GetTJALL();
	/**
	 * 业务统计报表
	 * @param tjsjks 开始统计时间 
	 * @param tjsjks 截止统计时间  
	 * @param fzlb 
	 * @param xzqh 
	 * @param qlrlx 
	 * @param djlx 
	 * @return
	 */
	public Message GetYWTJ(String tjsjks,String tjsjjz, String djlx, String qlrlx, String xzqh, String fzlb);

	/**
	 * 	效能统计
	 * @param sjq 开始统计时间
	 * @param sjz 截止时间
	 */
	public Message Getefficiency_yt(String sjq,String sjz, String user,String select,String select_sj);
		

	/**
	 * @作者 邹彦辉
     * @创建时间 2016年12月21日17:36:41
		 * 	宗地单元统计/房屋
		 * @param
		 * @return
		 */
		public Message GetZddyTJ(HttpServletRequest request);
		
		/**
		 * 	宗地单元统计/土地
		 * @param
		 * @return
		 * TD_XZQH, TD_QLLX,TD_QLSDFS,TD_QLXZ,TD_TDDJ,TD_QLRLX,TD_YT,FZLB
		 */
		public Message GetZddyTJTD(HttpServletRequest request);
	/**
	 * 交易价格统计
	 * 
	 */
		public Message Getjyjgtjfw(HttpServletRequest request);

	    public Message Getjyjgttd(HttpServletRequest request);
	
	/**
	 * 批量限制户信息
	 * @param bdcdyid
	 * @param xzyy
	 * @param sjq
	 * @param sjz
	 * @param hzt
	 * @param xzwh
	 * @return
	 * @throws ParseException
	 */
	public Message setPLXZ(String bdcdyid, String xzyy, String sjq, String sjz, String hzt, String xzwh) throws Exception;	
	
	public Message cqHxx(Map<String, String> mapCondition,int page,int rows) throws Exception;
	public Message extractCQHxx(String bdcdyid,String fwzt) throws Exception;
	/**
	 * 缮证作废统计
	 */
	public Message getSZZFTJ(String sjq,String sjz);
	
	/**
	 * 证书编号统计
	 */
	public Message getZSBHTJ(String year);
	/**
	 * 不动产登记进展情况月报---鹰潭
	 */
	public Message getDJYWTJByMonth(String month);
	
//	public Message Getdepttj(String qsj,String zsj,String deptname,String fwortd);
}
