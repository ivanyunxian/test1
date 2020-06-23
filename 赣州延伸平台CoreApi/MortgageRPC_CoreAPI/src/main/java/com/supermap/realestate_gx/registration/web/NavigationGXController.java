package com.supermap.realestate_gx.registration.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.supermap.realestate.registration.model.BDCS_BDCCFXX;
import com.supermap.realestate.registration.util.ConfigHelper;
import com.supermap.realestate.registration.util.ConstValue;
import com.supermap.realestate.registration.util.Global;
import com.supermap.realestate.registration.util.YwLogUtil;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import com.supermap.wisdombusiness.workflow.service.common.DateUtil;

/**
 * 
 * @Description:页面跳转的控制器
 * @author 刘树峰
 * @date 2015年6月12日 上午11:47:28
 * @Copyright SuperMap
 */
@Controller
@RequestMapping("/navi_gx")
public class NavigationGXController {

	@Autowired
	private CommonDao baseCommonDao;
	/** 页面跳转路径 */
	private final String prefix = "/realestate_gx/registration/";

	/**
	 * 抵押取消
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/mortgagecancel", method = RequestMethod.GET)
	public String MortgageCancel(Model model) {
		model.addAttribute("ZXDBR", Global.getCurrentUserName());
		model.addAttribute("ZXSJ",DateUtil.getDate());
		return prefix + "djywcx/mortgagecancel";
	}
	/**
	 * 抵押列表
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/mortgagelist", method = RequestMethod.GET)
	public String MortgageList(Model model) {
		return prefix + "djywcx/mortgagelist";
	}
	/**
	 * 抵押注销列表查询
	 */
	@RequestMapping(value = "/diyazxquery", method = RequestMethod.GET)
	public String DiyaZXQuery() {
		return prefix + "djywcx/diyazxQuery";
	}
	/**
	 * 拆分权利列表查询
	 */
	@RequestMapping(value = "/splitquery", method = RequestMethod.GET)
	public String SplitQuery() {
		return prefix + "djywcx/splitlist";
	}
	/**
	 * 自动查封
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/zdcf", method = RequestMethod.GET)
	public String ShowSQRIndex(Model model) {
		return prefix + "djywcx/autounlock";
	}
	/**
	 * 手动解封
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/jf", method = RequestMethod.GET)
	public String JF(Model model) {
		model.addAttribute("CZR", Global.getCurrentUserName());
		model.addAttribute("CZSJ",DateUtil.getDate());
		return prefix + "djywcx/unlock";
	}

	/**
	 * 签收薄页面(URL:"/qsb,Method:GET")
	 */
	@RequestMapping(value = "/qsb", method = RequestMethod.GET)
	public String ShowQsbIndex(Model model) {

		return prefix + "csdj/printQsb";
	}

	/**
	 * 申请审批表页，广西南宁
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/sqspb_gxnn", method = RequestMethod.GET)
	public String ShowSQSPBIndex_GZSC(Model model) {
		return prefix + "csdj/approvalFormPage_GXNN";
	}

	/**
	 * 广西登记业务统计
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/gzdjywtj_gx", method = RequestMethod.GET)
	public String gzdjywtj_gx(Model model) {
		return prefix+"tj/gzdjywtj_gx";
	}

	//登记业务统计明细页面liangc
	/**
	 * 在办明细
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/zbmx", method = RequestMethod.GET)
	public String showZBMX(Model model) {
		return prefix+"djywtj/zbmx";
	}

	/**
	 * 登簿明细
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/dbmx", method = RequestMethod.GET)
	public String showDBMX(Model model) {
		return prefix+"djywtj/dbmx";
	}

	/**
	 * 已归档明细
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/ygdmx", method = RequestMethod.GET)
	public String showYGDMX(Model model) {
		return prefix+"djywtj/ygdmx";
	}

	/**
	 * 未归档明细
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/wgdmx", method = RequestMethod.GET)
	public String showWGDMX(Model model) {
		return prefix+"djywtj/wgdmx";
	}

	/**
	 * 缮证证书明细
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/szzsmx", method = RequestMethod.GET)
	public String showSZZSMX(Model model) {
		return prefix+"djywtj/szzsmx";
	}

	/**
	 * 缮证证明明细
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/szzmmx", method = RequestMethod.GET)
	public String showSZZMMX(Model model) {
		return prefix+"djywtj/szzmmx";
	}

	/**
	 * 发证证书明细
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/fzzsmx", method = RequestMethod.GET)
	public String showFZZSMX(Model model) {
		return prefix+"djywtj/fzzsmx";
	}

	/**
	 * 发证证明明细
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/fzzmmx", method = RequestMethod.GET)
	public String showFZZMMX(Model model) {
		return prefix+"djywtj/fzzmmx";
	}
	/**
	 * 
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/show_register_zrz", method = RequestMethod.GET)
	public String ShowInfoForRegisterZRZ(Model model) {
		return prefix + "converter/showRegisterZrz";
	}
	/**
	 * 业务库的楼盘表页面
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/yklptree", method = RequestMethod.GET)
	public String showYwklpTree(Model model) {
		return prefix + "ywk/ywkTree";
	}
	/**
	 * 
	 * 柳江数据转换页面
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/liujiang", method = RequestMethod.GET)
	public String liujiangDataConvert(Model model) {
		return prefix + "converter/liujiangDataConvert";
	}
	/**
	 * 
	 * 通过共享业务号号将共享库数据转成登记库数据受理页面
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/acceptproject", method = RequestMethod.GET)
	public String acceptproject(Model model) {
	
//		if("450300".equals(ConfigHelper.getNameByValue("XZQHDM")) || "450500".equals(ConfigHelper.getNameByValue("XZQHDM"))){
		return prefix + "workflow/acceptproject_gl";
//		}
//		return prefix + "workflow/acceptproject";
		
	}
	/**
	 * 
	 * 详情页面
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/acceptprojectdetails", method = RequestMethod.GET)
	public String acceptprojectdetails(Model model) {
		return prefix + "workflow/acceptprojectdetails";
	}

	/**
	 * 
	 * 详情页面
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/acceptprofj", method = RequestMethod.GET)
	public String acceptprofj(Model model) {
		return prefix + "workflow/acceptprofj";
	}
	/**
	 * 不动产登记业务量办件统计（科室、人员）
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/bjltj", method = RequestMethod.GET)
	public String bjltj(Model model) {
		YwLogUtil.addYwLog("不动产登记业务办件量统计", ConstValue.SF.YES.Value, ConstValue.LOG.SEARCH);
		return prefix+"/tj/bjltj";
	}

	/**
	 * 办件进度查询
	 * @author liangc
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/bjjdQuery", method = RequestMethod.GET)
	public String showBJJDquery(Model model) {
		return prefix+"djywcx/bjjdQuery";
	}
	

	/**
	 * 查封情况
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/cfinfo", method = RequestMethod.GET)
	public String adsmCfInfo(Model model) {
		return prefix + "djywcx/autounlock_gl";
	}
	/** 附件预览 */
	@RequestMapping(value = "/imgview", method = RequestMethod.GET)
	public String imgView(Model model) {
		return prefix + "djywcx/imgview";
	}
	/**
	 * 查封情况（组件专用）
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/showcfinfo", method = RequestMethod.GET)
	public String showcfxx(Model model) {
		return prefix + "djywcx/showcfxxfromrg";
	}
	/**
	 * 编辑查封页面
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/editcfinfo", method = RequestMethod.GET)
	public String editcfxx(Model model,HttpServletRequest request,HttpServletResponse response) {

		BDCS_BDCCFXX cfxx = new BDCS_BDCCFXX();
		String cfbh = request.getParameter("CFXXBH");
		String url = request.getParameter("url");
		String method = "post";
		if (!StringUtils.isEmpty(cfbh)) {
			cfxx = baseCommonDao.get(BDCS_BDCCFXX.class, cfbh);
			method = "put";
		}
		model.addAttribute("cfxxAttribute", cfxx);
		model.addAttribute("url", url);
		model.addAttribute("method", method);
		return prefix + "djywcx/andd_edit_cfxx";
	}

	/**
	 * 编辑查封页面
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/changetoJF", method = RequestMethod.GET)
	public String changeCFtoJF(Model model,HttpServletRequest request,HttpServletResponse response) {

		BDCS_BDCCFXX cfxx = new BDCS_BDCCFXX();
		String cfbh = request.getParameter("CFXXBH");
		String url = request.getParameter("url");
		String method = "post";
		if (!StringUtils.isEmpty(cfbh)) {
			cfxx = baseCommonDao.get(BDCS_BDCCFXX.class, cfbh);
			method = "put";
		}
		model.addAttribute("cfxxAttribute", cfxx);
		model.addAttribute("url", url);
		model.addAttribute("method", method);
		return prefix + "djywcx/jf";
	}
	/**
	 * 桂林给房产使用
	 * @author liangc
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/houseQuery/", method = RequestMethod.GET)
	public String HouseQuery(Model model) {
		return "/realestate_gx/registration/djywcx/houseQuery_fc";
	}
		/**
	 * 柳州批量导入单元页面
	 * @author liangc
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/plimportdy", method = RequestMethod.GET)
	public String plimporthouse(Model model) {
		return "/realestate_gx/registration/djywcx/importdy";
	}
	
	/**
	 * 限制信息列表
	 * 
	 * @param model
	 * @date:2017-06-03
	 * @author:heks
	 */
	@RequestMapping(value = "/limitInfoQuery", method = RequestMethod.GET)
	public String limitInfoQuery(Model model) {
		return prefix + "djywcx/limitInfoQuery";
	}
	/**
	 * 注销限制
	 *  
	 * @param model
	 * @date:2017-06-03
	 * @author:heks
	 */
	@RequestMapping(value = "/limitCancel", method = RequestMethod.GET)
	public String limitCancel(Model model) {
		model.addAttribute("ZXDBR", Global.getCurrentUserName());
		model.addAttribute("ZXDJSJ",DateUtil.getDate());
		return prefix + "djywcx/limitCancel";
	}
	
	/**
	 * 发证量和上报量统计
	 * @author liangc
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/fzl_sbltj", method = RequestMethod.GET)
	public String showFZL_SBLYWXX(Model model) {
		return prefix+"tj/fzl_sbltj";
	}
	
	/**
	 * 现房转回期房
	 * @author liangc
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/xf2qf", method = RequestMethod.GET)
	public String xf2qfmethod(Model model) {
		return prefix+"/xftoqfs/xftoqf";
	}
	
	@RequestMapping(value = "/bdcqzcx", method = RequestMethod.GET)
	public String bdcqzxx(Model model) {
		return prefix+"/djywcx/zsquery";
	}
	
	@RequestMapping(value = "/bdcqzcx2", method = RequestMethod.GET)
	public String bdcqzxx2(Model model) {
		return prefix+"/djywcx/zmquery";
	}
	
	/**
	 * 注销业务查询
	 * @author liangc
	 * @param model
	 * @time 2018年4月15日 上午11点09分
	 * @return
	 */
	@RequestMapping(value = "/zxywquery", method = RequestMethod.GET)
	public String showZXYWquery(Model model) {
		return prefix+"djywcx/zxywQuery";
	}
	
	/**
	 * 柳州月业务办件量统计
	 *  
	 * @param model
	 * @author:hpf lz_yywblltj
	 */
	@RequestMapping(value = "/lz_yywblltj", method = RequestMethod.GET)
	public String lz_yywblltjNavi(Model model) {
	
		return prefix + "tj/lz_yywblltj";
	}

	//================================贵港=======================================================================//
		@RequestMapping(value={"/houseQuery2/"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
		  public String workBookHouse(Model model)
		  {
		    return "/realestate_gx/registration/djywcx/houseQueryQT2";
		  }
		  
		  @RequestMapping(value={"/diyinfo"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
		  public String DiyInfo(Model model)
		  {
		    return "/realestate_gx/registration/djywcx/diyinfo2";
		  }
		  
		  @RequestMapping(value={"/standingbook"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
		  public String StandingBook(Model model)
		  {
		    return "/realestate_gx/registration/djywcx/standingbook2";
		  }
		  
		  @RequestMapping(value={"/landQuery/"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
		  public String workBookLandQuery(Model model)
		  {
		    return "/realestate_gx/registration/djywcx/landQuery2";
		  }
		//================================贵港=======================================================================//
	
	 /**
	 * 业务办结统计（按月统计，办结总数、平均办结时间）  2018-5-15
	 * @param model
	 * @return
	 * @author LML
	 */
	@RequestMapping(value = "/banjie_tj", method = RequestMethod.GET)
	public String banjie_tj(Model model) {
		YwLogUtil.addYwLog("办结统计(按月)", ConstValue.SF.YES.Value, ConstValue.LOG.SEARCH);
		return prefix+"/tj/banjie_tj";
	}
	
	/**
	 * 查封时限监控
	 * TODO
	 * @Title: cfjkInfo
	 * @author lgqyk
	 * @date   2018-07-27 12:03
	 * @return String
	 */
	@RequestMapping(value = "/cfjkInfo", method = RequestMethod.GET)
	public String cfjkInfo(Model model) {
		return prefix+"/modules/common/cfjkInfo_gx";
	}
}
