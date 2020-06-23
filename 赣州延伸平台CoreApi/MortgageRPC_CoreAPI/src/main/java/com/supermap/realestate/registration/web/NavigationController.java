package com.supermap.realestate.registration.web;

import com.supermap.realestate.registration.ViewClass.ProjectInfo;
import com.supermap.realestate.registration.ViewClass.ZS;
import com.supermap.realestate.registration.model.*;
import com.supermap.realestate.registration.service.NaviService;
import com.supermap.realestate.registration.util.*;
import com.supermap.realestate.registration.util.ConstValue.BDCDYLX;
import com.supermap.realestate.registration.util.ConstValue.DJLX;
import com.supermap.realestate.registration.util.ConstValue.QLLX;
import com.supermap.wisdombusiness.workflow.dao.CommonDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.UnsupportedEncodingException;

/**
 * 
 * @Description:页面跳转的控制器
 * @author 刘树峰
 * @date 2015年6月12日 上午11:47:28
 * @Copyright SuperMap
 */
@Controller
@RequestMapping("/navi")
public class NavigationController {

	/** 导航页service */
	@Autowired
	private NaviService naviService;
        @Autowired
	private CommonDao baseCommonDao;

	/** 页面跳转路径 */
	private final String prefix = "/realestate/registration/";

	/**
	 * 申请人页（URL:"/sqr",Method:GET）
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/sqr", method = RequestMethod.GET)
	public String ShowSQRIndex(Model model) {
		model.addAttribute("sqrAttribute", new BDCS_SQR());// 申请人页面form表单赋值
		return prefix + "csdj/applicantPage";
	}

	/**
	 * 单元信息页（URL:"/dyxx",Method:GET）
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/dyxx", method = RequestMethod.GET)
	public String ShowDYXXIndex(Model model, HttpServletRequest request) {
		model.addAttribute("qlrxxAttribute", new BDCS_QLR_XZ());
		model.addAttribute("syqAttribute", new BDCS_QL_XZ());
		String project_id = request.getParameter("file_number");
		String path = prefix + naviService.GetDYXXPageUrl(project_id);
		return path;
	}

	/**
	 * 现状权利信息页（URL:"/oldqlxx",Method:GET）
	 * 
	 * @作者 刘树峰
	 * @创建时间 2015年6月13日下午3:11:14
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/oldqlxx", method = RequestMethod.GET)
	public String showXZQLXXIndex(Model model, HttpServletRequest request) {
		// TODO @刘树峰 该页面是之前有个叫 转移前产权信息的页，现在不用了
		model.addAttribute("qlrxxAttribute", new BDCS_QLR_XZ());
		model.addAttribute("syqAttribute", new BDCS_QL_XZ());
		return prefix + "zydj/preRightsInfoPage";
	}

	/**
	 * 登记权利信息页（URL:"/qlxx",Method:GET）
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/qlxx", method = RequestMethod.GET)
	public String ShowQLXXIndex(Model model, HttpServletRequest request) {
		model.addAttribute("qlrxxAttribute", new BDCS_QLR_GZ());
		model.addAttribute("syqAttribute", new BDCS_QL_GZ());
		model.addAttribute("fsqlAttribute", new BDCS_FSQL_GZ());
		String project_id = request.getParameter("file_number");
		String pagename = naviService.GetQLXXPageUrl(project_id);
		return prefix + pagename;
	}

	/**
	 * 预告权利信息页（URL:"/noticeright",Method:GET）  
	 * 
	 */
	@RequestMapping(value = "/noticeright", method = RequestMethod.GET)
	public String ShowNoticeRightIndex(Model model, HttpServletRequest request) {
		return prefix + "notice/noticeRightsInfoPage";
	}

	/**
	 * 收费页（URL:"/sf",Method:GET）
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/sf", method = RequestMethod.GET)
	public String ShowSFIndex(Model model) {
		model.addAttribute("djsfAttribute", new BDCS_DJSF());
		if ("sjzmode".equals(ConfigHelper.getNameByValue("ChargeMode").toLowerCase())) {
			return prefix + "csdj/chargePage_SJZ";
		} else {
			return prefix + "csdj/chargePage";
		}
	}

	/**
	 * 申请审批表页（URL:"/sqspb",Method:GET）
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/sqspb", method = RequestMethod.GET)
	public String ShowSQSPBIndex(Model model) {
		return prefix + "csdj/approvalFormPage";
	}
	
	/**
	 * @Description: 新的审批表页面
	 * @Title: ShowSQSPBIndexEX
	 * @Author: zhaomengfan
	 * @Date: 2017年4月11日下午1:38:01
	 * @param model
	 * @return
	 * @return String
	 */
	@RequestMapping(value = "/sqspbEX", method = RequestMethod.GET)
	public String ShowSQSPBIndexEX(Model model) {
		return prefix + "common/approvalFormPageEX";
	}
	/**
	 * 申请审批表页（URL:"/sqspb",Method:GET）
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/sqspb1", method = RequestMethod.GET)
	public String ShowSQSPBSP1(Model model) {
		return prefix + "csdj/approvalFormPageSP1";
	}
	/**
	 * 申请审批表页（URL:"/sqspb",Method:GET）
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/sqspb2", method = RequestMethod.GET)
	public String ShowSQSPBSP2(Model model) {
		return prefix + "csdj/approvalFormPageSP2";
	}
	/**
	 * 申请审批表页版本二（URL:"/sqspb_version2",Method:GET） 区别是附件的格式不同
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/sqspb_version2", method = RequestMethod.GET)
	public String ShowSQSPBIndex_version2(Model model) {
		return prefix + "csdj/approvalFormPage_version2";
	}
		/**
	 * 出件通知单
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/note", method = RequestMethod.GET)
	public String ShowTZD(Model model) {
		return prefix + "csdj/noteFormPage";
	}

	/**
	 * 申请审批表页(赣州首次登记用到，2016年2月29日刘树峰添加)（URL:"/sqspb_gzsc",Method:GET）
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/sqspb_gzsc", method = RequestMethod.GET)
	public String ShowSQSPBIndex_GZSC(Model model) {
		return prefix + "csdj/approvalFormPage_GZSC";
	}

	/**
	 * 登簿页（URL:"/db",Method:GET）
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/db", method = RequestMethod.GET)
	public String ShowDBIndex(Model model, HttpServletRequest request) {
		String project_id = request.getParameter("file_number");
		String path = prefix + naviService.GetDBPageUrl(project_id);
		return path;
	}

	/**
	 * 证书信息页（URL:"/zs",Method:GET）
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/zs", method = RequestMethod.GET)
	public String ShowZSIndex(Model model, HttpServletRequest request) {
		// TODO @刘树峰 此方法判断有问题，缺了好多判断
		String project_id = request.getParameter("file_number");
		ProjectInfo info = ProjectHelper.GetPrjInfoByPrjID(project_id);

		// 出证明的有：抵押权登记，预告登记，异议登记，

		// 预告登记都是出证明
		if (info.getDjlx().equals(DJLX.YGDJ.Value)||BDCDYLX.YCH.Value.equals(info.getBdcdylx())) {
			return prefix + "dydj/certifyPage";
		}

		// 抵押权
		if (info.getQllx().equals(QLLX.DIYQ.Value)) {
			return prefix + "dydj/certifyPage";
		}

		// 地役权
		if (info.getQllx().equals(QLLX.DYQ.Value)) {
			return prefix + "dydj/certifyPage";
		}

		// 异议登记
		if (info.getDjlx().equals(DJLX.YYDJ.Value)) {
			return prefix + "dydj/certifyPage";
		}
		
		//更正异议登记
		if (info.getDjlx().equals(DJLX.GZDJ.Value)&&info.getQllx().equals(QLLX.QTQL.Value)) {
			return prefix + "dydj/certifyPage";
		}
		
		if(info.getBaseworkflowcode().equals("BZ005")||info.getBaseworkflowcode().equals("BZ006")||info.getBaseworkflowcode().equals("BZ021")
				||info.getBaseworkflowcode().equals("GZ203")||info.getBaseworkflowcode().equals("GZ204")||info.getBaseworkflowcode().equals("GZ205")){
			return prefix + "dydj/certifyPage";
		}
		//
		//
		// if (info.getQllx().equals(QLLX.DIYQ.Value)) {
		// return prefix + "dydj/certifyPage";
		// }
		// if (info.getDjlx().equals(DJLX.YGDJ.Value)) {
		// return prefix + "dydj/certifyPage";
		// }
		if("0".equals(ConfigHelper.getNameByValue("ISSHOW_QR"))) {
			return prefix + "csdj/certPage_QR";
		}
		return prefix + "csdj/certPage";
	}

	/**
	 * 证书信息页（URL:"/zs",Method:GET）
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/zs_combo", method = RequestMethod.GET)
	public String ShowZS_ComboIndex(Model model, HttpServletRequest request) {
		if("0".equals(ConfigHelper.getNameByValue("ISSHOW_QR"))) {
			return prefix + "csdj/certPage_combo_QR";
		}
		return prefix + "csdj/certPage_combo";
	}

	/**
	 * 证书信息页（URL:"/zs",Method:GET）
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/zm_combo", method = RequestMethod.GET)
	public String ShowZM_ComboIndex(Model model, HttpServletRequest request) {
		return prefix + "dydj/certifyPage_combo";
	}

	/**
	 * 首次商品房初始登记的证书
	 * @作者 海豹
	 * @创建时间 2015年12月4日下午9:27:17
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/buildingcertpage", method = RequestMethod.GET)
	public String buildingCertpageIndex(Model model, HttpServletRequest request) {
		return prefix + "csdj/buildingCertPage";
	}

	/**
	 * 发证页（URL:"/fz",Method:GET）
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/fz", method = RequestMethod.GET)
	public String ShowFZIndex(Model model) {
		model.addAttribute("fzxxAttribute", new BDCS_DJFZ());
		return prefix + "csdj/outcertPage";
	}

	/**
	 * 短信推送页（URL:"/smspush",Method:GET）
	 *
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/smspush", method = RequestMethod.GET)
	public String ShowSmsPushIndex(Model model) {
		return prefix + "smsmanage/smsPushPage";
	}

	/**
	 * 加载登记归档页面（URL:"/djgd",Method:GET）
	 * 
	 * @作者 海豹
	 * @创建时间 2015年6月7日下午9:14:14
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/djgd", method = RequestMethod.GET)
	public String ShowDJGDIndex(Model model) {
		model.addAttribute("djgdAttribute", new BDCS_DJGD());
		return prefix + "csdj/archivePage";
	}

	/**
	 * 加载登记归档页面（URL:"/djgd",Method:GET）
	 * 
	 * @作者 海豹
	 * @创建时间 2015年6月7日下午9:14:14
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/DAdjgd", method = RequestMethod.GET)
	public String ShowDADJGDIndex(Model model) {
		model.addAttribute("djgdAttribute", new BDCS_DJGD());
		return prefix + "csdj/DAarchivePage";
	}

	/**
	 * 抵押查询选择页面（URL:"/queryMortgageRights",Method:GET）
	 * 
	 * @作者 WUZHU
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/queryMortgageRights", method = RequestMethod.GET)
	public String ShowDYCX(Model model) {
		return prefix + "modules/dyzxdj/queryMortgageRights";
	}

	/**
	 * 选择房屋页面（URL:"/xzfw",Method:GET）
	 * 
	 * @作者 刘树峰
	 * @创建时间 2015年6月18日下午9:27:01
	 * @return
	 */
	@RequestMapping(value = "/xzfw", method = RequestMethod.GET)
	public String ShowSelectFW() {
		return "/realestate/registration/modules/common/selectSurveyHouse";
	}

	/**
	 * 选择自然幢页面（URL:"/xzzrz",Method:GET）
	 * 
	 * @作者 diaoliwei
	 * @创建时间 2015年6月22日下午5:36:44
	 * @return
	 */
	@RequestMapping(value = "/xzzrz", method = RequestMethod.GET)
	public String ShowSelectZRZ() {
		return "/realestate/registration/modules/common/selectSurveyBuilding";
	}

	/**
	 * 选择宗地页面（URL:"/xzzd",Method:GET）
	 * 
	 * @作者 diaoliwei
	 * @创建时间 2015年6月18日下午9:28:40
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/xzzd", method = RequestMethod.GET)
	public String ShowSelectZd(Model model) {
		return "/realestate/registration/modules/common/selectSurveyLand";
	}

	/**
	 * 选择使用权宗地页面（URL:"/xzOwnershipzd",Method:GET）
	 * 
	 * @作者 海豹
	 * @创建时间 2015年6月24日下午6:09:39
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/xzOwnershipzd", method = RequestMethod.GET)
	public String ShowSelectOwnershipZd(Model model) {
		return prefix + "modules/common/selectSurveyOwnershipLand";
	}

	/**
	 * 选择宗海页面（URL:"/xzSurveySea",Method:GET）
	 * 
	 * @作者 海豹
	 * @创建时间 2015年6月25日下午1:05:39
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "xzSurveySea", method = RequestMethod.GET)
	public String ShowSelectSurveySea(Model model) {
		return prefix + "modules/common/selectSurveySea";
	}

	/**
	 * 选择林地页面（URL:"/xzSurveyForest",Method:GET）
	 * 
	 * @作者 海豹
	 * @创建时间 2015年6月25日下午1:05:39
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "xzSurveyForest", method = RequestMethod.GET)
	public String ShowSelectSurveyForest(Model model) {
		return prefix + "modules/common/selectSurveyforest";
	}

	/**
	 * 选择房屋宗地页面（URL:"/xzfwzd",Method:GET）
	 * 
	 * @作者 diaoliwei
	 * @创建时间 2015年6月18日下午11:08:33
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/xzfwzd", method = RequestMethod.GET)
	public String ShowSelectFwZd(Model model) {
		return "/realestate/registration/modules/common/selectHouseOrLand";
	}

	/**
	 * 选择现状自然幢页面（URL:"/xzZRZ",Method:GET）
	 * 
	 * @作者 diaoliwei
	 * @创建时间 2015年6月23日上午10:32:31
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/xzZRZ", method = RequestMethod.GET)
	public String ShowSelectXzZRZ(Model model) {
		return "/realestate/registration/modules/common/selectBuilding";
	}

	/**
	 * 注销登记查询（URL:"/zxdjcx",Method:GET）
	 * 
	 * @作者 WUZHU
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/zxdjcx", method = RequestMethod.GET)
	public String ShowZXDJ(Model model) {
		return prefix + "modules/cfdj/queryLockUnit";
	}

	/**
	 * 预告登记选择房屋宗地页面（URL:"/preunitlist",Method:GET）
	 * 
	 * @作者 李想
	 * @创建时间 2015年6月21日下午20:08:33
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/preunitlist", method = RequestMethod.GET)
	public String ShowSelectPreUnit(Model model) {
		return "/realestate/registration/modules/common/preUnitList";
	}

	/**
	 * 调查项目页面（URL:"/surveyProject",Method:GET）
	 * @作者 diaoliwei
	 * @创建时间 2015年6月30日下午2:35:10
	 * @return
	 */
	@RequestMapping(value = "/surveyProject", method = RequestMethod.GET)
	public String ShowSurveyProject(Model model) {
		return prefix + "modules/csdj/readSurveyProject";
	}

	/**
	 * 测试iserver（URL:"/testiServerRest",Method:GET）
	 * @Title: testiServerRest
	 * @author:liushufeng
	 * @date：2015年7月18日 下午7:19:19
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/testiServerRest", method = RequestMethod.GET)
	public String testiServerRest(Model model) {
		return "/realestate/testRestiServer";
	}

	/**
	 * 不动产单元查询（URL:"/xzzscx",Method:GET）
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/xzzscx", method = RequestMethod.GET)
	public String xzzscx(Model model) {
		YwLogUtil.addYwLog("访问：不动产单元查询管理", ConstValue.SF.YES.Value, ConstValue.LOG.SEARCH);
		return "/realestate/registration/djywcx/xzzscx";
	}

	/**
	 * 历史回溯（URL:"/testlszscx",Method:GET）
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/testlszscx", method = RequestMethod.GET)
	public String testlszscx(Model model) {
		YwLogUtil.addYwLog("访问:历史回溯功能", ConstValue.SF.YES.Value, ConstValue.LOG.SEARCH);
		return "/realestate/registration/djywcx/lszscx";
	}

	/**
	 * 房屋交易分析（URL:"/bdcwfjyfx",Method:GET）
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/bdcwfjyfx", method = RequestMethod.GET)
	public String bdcwfjyfx(Model model) {
		return "/realestate/registration/tj/fwfx";
	}

	/**
	 * 土地交易分析（URL:"/bdctdjyfx",Method:GET）
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/bdctdjyfx", method = RequestMethod.GET)
	public String bdctdjyfx(Model model) {
		return "/realestate/registration/tj/tdjyfx";
	}

	/**
	 * 登记业务统计（URL:"/bdcdjywtj",Method:GET）
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/bdcdjywtj", method = RequestMethod.GET)
	public String bdcdjywtj(Model model) {
		return "/realestate/registration/tj/djywtj";
	}

	/**
	 * 房屋交易报表（URL:"/bdcfwjybb",Method:GET）
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/bdcfwjybb", method = RequestMethod.GET)
	public String bdcfwjybb(Model model) {
		return "/realestate/registration/tj/fwjybb";
	}

	/**
	 * 不动产现状产权信息房屋 （URL:"/bdcxzcqxx_fw",Method:GET）
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/bdcxzcqxx_fw", method = RequestMethod.GET)
	public String bdcxzcqxx(Model model) {
		return "/realestate/registration/djywcx/xzcqxx_fw";
	}

	/**
	 * 不动产现状产权信息房屋 （URL:"/bdcxzcqxx_syqzd",Method:GET）
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/bdcxzcqxx_syqzd", method = RequestMethod.GET)
	public String bdcxzcqxx_syqzd(Model model) {
		return "/realestate/registration/djywcx/xzcqxx_syqzd";
	}

	/**
	 * 读取合同页面
	 * @作者 diaoliwei
	 * @创建时间 2015年7月19日下午11:40:52
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/readContract", method = RequestMethod.GET)
	public String readContract(Model model) {
		String xzqhdm = ConfigHelper.getNameByValue("XZQHDM");
		model.addAttribute("XZQHDM", xzqhdm); 
		return "/realestate/registration/modules/zydj/readContract";
	}

	/**
	 * 读取 房屋信息查询页面
	 * @作者 WUZHU
	 * @创建时间
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/houseQuery/", method = RequestMethod.GET)
	public String workBookHouse(Model model) {

		return "/realestate/registration/djywcx/houseQuery";
	}
	

	/**
	 * @Description: 综合查询
	 * @Title: HouseAndLandQuery
	 * @Author：赵梦帆
	 * @Data：2016年12月12日 下午2:32:27
	 * @param model
	 * @return
	 * @return String
	 */
	@RequestMapping(value = "/QueryEx/", method = RequestMethod.GET)
	public String HouseAndLandQuery(Model model) {

		return "/realestate/registration/djywcx/houseAndlandQuery";
	}

	/**
	 * @Title: workBookHouseBaech
	 * @Description: 批量查询
	 * @Author：赵梦帆
	 * @Data：2016年11月2日 上午10:29:46
	 * @param model
	 * @return
	 * @return String
	 */
	@RequestMapping(value = "/houseQueryBatch/", method = RequestMethod.GET)
	public String workBookHouseBaech(Model model) {

		return "/realestate/registration/djywcx/houseQueryBatch";
	}
	
	/**
	 * 读取 房屋信息查询页面(无权利人和证件号查询)
	 * @作者 俞学斌
	 * @创建时间
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/houseQueryEx/", method = RequestMethod.GET)
	public String workBookHouseEx(Model model) {

		return "/realestate/registration/djywcx/houseQueryEx";
	}

	/**
	 * 读取 房屋信息查询详情页面
	 * @作者 WUZHU
	 * @创建时间
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/houseQueryDetails", method = RequestMethod.GET)
	public String workBookHouseDetail(Model model) {
		String JYYTURL = ConfigHelper.getNameByValue("JYYTURL");//交易系统URL，如http://localhost:8086/jyyt/
		String XZQHDM = ConfigHelper.getNameByValue("XZQHDM");
		model.addAttribute("JYYTURL", JYYTURL);
		model.addAttribute("XZQHDM", XZQHDM); 
		return "/realestate/registration/djywcx/houseQueryDetails";
	}
	/**
	 * 跳转到匹配权利人信息的页面
	 * @author hks
	 * @创建时间 2016/6/21 10:24
	 * @return
	 */
	@RequestMapping(value = "/qlrmatchdata", method = RequestMethod.GET)
	public String matchQlrInfo(){
		return "/realestate/registration/djywcx/importQlrInfo";
		
	}
	/**
	 * 读不动产（土地）登记业务台账页面
	 * @作者 wuzhu
	 * @创建时间 2015年7月19日下午11:40:52
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/workBookLand", method = RequestMethod.GET)
	public String workBookLand(Model model) {

		return "/realestate/registration/djywcx/workBookLand";
	}

	/**
	 * 读不动产（土地）查封业务台账页面
	 * @作者 wuzhu
	 * @创建时间 2015年7月19日下午11:40:52
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/cfinfo", method = RequestMethod.GET)
	public String CFInfo(Model model) {

		return "/realestate/registration/djywcx/cfinfo";
	}

	/**
	 * 读不动产（土地）抵押业务台账页面
	 * @作者 wuzhu
	 * @创建时间 2015年7月19日下午11:40:52
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/diyinfo", method = RequestMethod.GET)
	public String DiyInfo(Model model) {

		return "/realestate/registration/djywcx/diyinfo";
	}
	
	/**
	 * 读不动产登记台账统计
	 * @作者 yuxuebin
	 * @创建时间 2016年04月11日18:06:52
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/standingbook", method = RequestMethod.GET)
	public String StandingBook(Model model) {

		return "/realestate/registration/djywcx/standingbook";
	}

	/**
	 * 不动产登记台账统计(云南玉溪)
	 * @Author YuGuowei
	 * @param model
	 * @return
     */
	@RequestMapping(value = "/standingbookyuxi", method = RequestMethod.GET)
	public String StandingBookYuxi(Model model) {
		return "/realestate/registration/djywcx/standingbook_yuxi";
	}

	/**
	 * 读取身份证
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/readIdCard", method = RequestMethod.GET)
	public String readIdCard(Model model, HttpServletRequest request) {
		String xmbh = request.getParameter("xmbh");
		model.addAttribute("xmbh", xmbh);
		return "/realestate/registration/modules/common/idcard";
	}

	/**
	 * 泸州读取身份证
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/readIdCardlz", method = RequestMethod.GET)
	public String readIdCardlz(Model model, HttpServletRequest request) {
		String xmbh = request.getParameter("xmbh");
		model.addAttribute("xmbh", xmbh);
		return "/realestate/registration/modules/common/idcardlz";
	}

	/**
	 * 添加房屋信息
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/addhouseinfo", method = RequestMethod.GET)
	public String addhouseinfo(Model model, HttpServletRequest request) {
		return "/realestate/registration/modules/common/addhouseInfo";
	}

	/**
	 * 批量编辑房屋信息
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/pleditablehouseinfo", method = RequestMethod.GET)
	public String pleditablehouseinfo(Model model, HttpServletRequest request) {
		return "/realestate/registration/modules/common/pleditablehouseInfo";
	}

	/**
	 * 赣州收费统计
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/gzsftj", method = RequestMethod.GET)
	public String gzsftj(Model model) {
		return "/realestate/registration/tj/gzsftj";
	}
	
	/**
	 * 收费统计
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/sftj", method = RequestMethod.GET)
	public String sftj(Model model) {
		return "/realestate/registration/tj/sftj";
	}
	
	/**
	 * 收费明细统计
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/sfmxtj", method = RequestMethod.GET)
	public String sfmxtj(Model model) {
		return "/realestate/registration/tj/sfmxtj";
	}
	
	/**
	 * 办件明细统计
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/bjmxtj", method = RequestMethod.GET)
	public String bjmxtj(Model model) {
		return "/realestate/registration/tj/bjmxtj";
	}

	
	/**
	 * 部门统计
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/department", method = RequestMethod.GET)
	public String department(Model model) {
		return "/realestate/registration/tj/department";
	}
	
	/**
	 * 赣州房屋统计
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/gzfwtj", method = RequestMethod.GET)
	public String gzfwtj(Model model) {
		return "/realestate/registration/tj/gzfwtj";
	}

	/**
	 * 赣州科室统计
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/gzkstj", method = RequestMethod.GET)
	public String gzkstj(Model model) {
		return "/realestate/registration/tj/gzkstj";
	}

	/**
	 * 赣州抵押金额统计
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/gzdyjetj", method = RequestMethod.GET)
	public String gzdyjetj(Model model) {
		return "/realestate/registration/tj/gzdyjetj";
	}

	/**
	 * 赣州登记业务统计
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/gzdjywtj", method = RequestMethod.GET)
	public String gzdjywtj(Model model) {
		return "/realestate/registration/tj/gzdjywtj";
	}
    /**
	 * 鹰潭短信统计
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/dxtj", method = RequestMethod.GET)
	public String dxtj(Model model) {
		return "/realestate/registration/tj/dxtj";
	}
	/**
	 *鹰潭全市监管首页
	 */
	@RequestMapping(value = "/supervision", method = RequestMethod.GET)
	public String supervision(Model model) {
		return "/realestate/registration/tj/Supervision";
	}
	/**
	 * 鹰潭数据上报统计
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/sjsbtj", method = RequestMethod.GET)
	public String sjsbtj(Model model) {
		return "/realestate/registration/tj/sjsbtj";
	}

	/**
	 * 不动产档案统计  2016-12-07 luml
	 * 
	 * */
	@RequestMapping(value = "/bdcdatj", method = RequestMethod.GET)
	public String bdcdatj(Model model) {
		return "/realestate/registration/tj/bdcdatj";
	}
	
	/**
	 * 消息管理
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/messagemanager", method = RequestMethod.GET)
	public String shareMessageManage(Model model) {
		return "/realestate/registration/tj/messagemanager";
	}

	/**
	 * 打印模板管理
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/printtplmanage", method = RequestMethod.GET)
	public String printTpl(Model model) {
		YwLogUtil.addYwLog("打印模板管理", ConstValue.SF.YES.Value, ConstValue.LOG.SEARCH);
		return "/realestate/registration/manage/printTplManage";
	}

	/**
	 * 读取 土地信息查询页面
	 * @作者 mss
	 * @创建时间
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/landQuery/", method = RequestMethod.GET)
	public String workBookLandQuery(Model model) {

		return "/realestate/registration/djywcx/landQuery";
	}
	
	
	/**
	 * 读取 自然幢信息查询页面
	 * @作者 mss
	 * @创建时间
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/buildingQuery/", method = RequestMethod.GET)
	public String workBookbuildingQuery(Model model) {

		return "/realestate/registration/djywcx/buildingQuery";
	}
	
	
	/**
	 * 自然幢关联房屋页面
	 * 
	 */
	@RequestMapping(value = "/fwlist/", method = RequestMethod.GET)
	public String workBookfwxx(Model model) {
		return "/realestate/registration/djywcx/fwlist";
	}
	
	
	/**
	 * 读取 土地信息查询页面(无权利人和证件号查询)
	 * @作者 yuxb
	 * @创建时间
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/landQueryEx/", method = RequestMethod.GET)
	public String workBookLandQueryEx(Model model) {
		return "/realestate/registration/djywcx/landQueryEx";
	}

	/**
	 * 读取 土地信息查询详情页面
	 * @作者mss
	 * @创建时间
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/landQueryDetails", method = RequestMethod.GET)
	public String workBookLandDetail(Model model) {
		return "/realestate/registration/djywcx/landQueryDetails";
	}

	/** 读取林地查询页面
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/forestQuery", method = RequestMethod.GET)
	public String forestQuery(Model model) {
		return "/realestate/registration/djywcx/forestQuery";
	}
	
	/**
	 * 读取森林林木信息查询详情页面
	 * @作者 海豹
	 * @创建时间 2016年3月18日上午11:31:20
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/forestQueryDetails", method = RequestMethod.GET)
	public String workBookforestDetail(Model model) {

		return "/realestate/registration/djywcx/forestQueryDetails";
	}

	/** 读取农用地查询页面
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/agricultureQuery", method = RequestMethod.GET)
	public String agricultureQuery(Model model) {
		return "/realestate/registration/djywcx/agricultureQuery";
	}
	
	/** 读取农用地信息查询详情页面
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/agricultureQueryDetails", method = RequestMethod.GET)
	public String workBookagricultureDetail(Model model) {
		return "/realestate/registration/djywcx/agricultureQueryDetails";
	}
	
	/**
	 * 不动产登记业务办理统计
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/bjltj", method = RequestMethod.GET)
	public String bjltj(Model model) {
		YwLogUtil.addYwLog("不动产登记业务办理统计", ConstValue.SF.YES.Value, ConstValue.LOG.SEARCH);
		return "/realestate/registration/tj/bjltj";
	}
	
	/**
	 * 不动产登记热点统计
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/hotstatistics", method = RequestMethod.GET)
	public String hotstatistics(Model model) {
		YwLogUtil.addYwLog("不动产登记热点统计", ConstValue.SF.YES.Value, ConstValue.LOG.SEARCH);
		return "/realestate/registration/tj/hotstatistics";
	}
	/**
	 * 不动产登记发证量统计
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/djfztj", method = RequestMethod.GET)
	public String djfztj(Model model) {
		YwLogUtil.addYwLog("不动产登记发证统计", ConstValue.SF.YES.Value, ConstValue.LOG.SEARCH);
		return "/realestate/registration/tj/djfztj";
	}
	/**
	 * 赣州权证移交统计
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/gzdyqzyjtj", method = RequestMethod.GET)
	public String gzdyqzyjtj(Model model) {
		return "/realestate/registration/tj/gzdyqzyjtj";
	}
	/**
	 * 证书页面的预览证书或证明页面
	 * @author diaoliwei
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/preview", method = RequestMethod.GET)
	public String PreviewZSorZM(Model model, HttpServletRequest request) {
		try {
			ZS zs = new ZS();
			zs = naviService.GetInfo(request);
			boolean exist = StringHelper.existZhangImg(request);
			model.addAttribute("exist", exist);
			model.addAttribute("zsInfo", zs);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return prefix + "csdj/previewZsOrZm";
	}

	/**
	 * 数据上报结果查询页面
	 * @author diaoliwei
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/dataQuery", method = RequestMethod.GET)
	public String showDataQuery(Model model, HttpServletRequest request) {

		return prefix + "sjsb/sjsbQuery";
	}
	
	/**
	 * 鹰潭数据上报结果查询页面
	 *
	 */
	@RequestMapping(value = "/dataQuery1", method = RequestMethod.GET)
	public String showDataQuery1(Model model, HttpServletRequest request) {

		return prefix + "sjsb/QuerySjsb";
	}

	/**
	 * 查看报文详细内容
	 * @author diaoliwei
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/dataDetail", method = RequestMethod.GET)
	public String showXmlDetail(Model model, HttpServletRequest request) {
		String bwmc = request.getParameter("bwmc");
		/*
		 * if(!StringUtils.isEmpty(bwmc) && bwmc.length() > 5){ bwmc = "Rep" +
		 * bwmc.substring(3, bwmc.length()); }
		 */
		model.addAttribute("bwmc", bwmc);
		return prefix + "sjsb/sjsbDetail";
	}

	/**
	 * 泸州专用 新的房屋信息查询页
	 * @创建时间
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/houseQuery2/", method = RequestMethod.GET)
	public String workBookHouse2(Model model) {

		return "/realestate/registration/djywcx/houseQuery2";
	}

	/**
	 * 大宗件受理页面--石家庄用
	 * @Title: bigProjectAccept
	 * @author:liushufeng
	 * @date：2016年3月17日 上午10:56:31
	 * @return
	 */
	@RequestMapping(value = "/bigproject/accept/", method = RequestMethod.GET)
	public String bigProjectAccept() {

		return "/realestate/registration/bigproject/bigprojectaccept";
	}

	/**
	 * 大宗件受理页面,东大厅--石家庄用
	 * @Title: bigProjectAccept
	 * @author:liushufeng
	 * @date：2016年3月17日 上午10:56:31
	 * @return
	 */
	@RequestMapping(value = "/bigproject/acceptddt/", method = RequestMethod.GET)
	public String bigProjectAccept_DDT() {
		return "/realestate/registration/bigproject/bigprojectaccept_ddt";
	}

	/**
	 * 查询这个时间段内发证的权利人信息
	 * @作者 mss
	 * @创建时间
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/certqlrinfo/", method = RequestMethod.GET)
	public String getCertqlrinfo(Model model) {

		return "/realestate/registration/djywcx/certqlrinfo";
	}
	
	
	/**
	 * 查询这个时间段内发证的权利人信息
	 * @作者 mss
	 * @创建时间
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/newcertqlrinfo/", method = RequestMethod.GET)
	public String getnewCertqlrinfo(Model model) {

		return "/realestate/registration/djywcx/newcertqlrinfo";
	}
	
	/**
	 * 查询即将到期的时间的权利列表
	 * @作者 Yangx
	 * @创建时间
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/cqTimeBe/", method = RequestMethod.GET)
	public String getcqTimeBE(Model model) {

		return "/realestate/registration/djywcx/djsjcx";
	}
	
	@RequestMapping(value = "/dyTimeBe/", method = RequestMethod.GET)
	public String getdyTimeBE(Model model) {

		return "/realestate/registration/djywcx/dyqlsjcx";
	}
	
	@RequestMapping(value = "/cfTimeBe/", method = RequestMethod.GET)
	public String getcfTimeBE(Model model) {

		return "/realestate/registration/djywcx/cfqlsjcx";
	}
		@RequestMapping(value="/getHouseDyAndCfList",method = RequestMethod.GET)
	public String getHouseDyAndCf(Model model){
		
		return prefix +"/djywcx/houseDyAndCfQueryList";
		
	}
		@RequestMapping(value="/zrzList",method = RequestMethod.GET)
	public String zrzList(Model model){
		
		return prefix +"/djywcx/zrzList";
		
	}
		@RequestMapping(value="/zrzQueryDetails",method = RequestMethod.GET)
	public String zrzQueryDetails(Model model){
		
		return prefix +"/djywcx/zrzQueryDetails";
		
	}
		
	@RequestMapping(value = "/bdcdjzb", method = RequestMethod.GET)
	public String bdcdjzb(Model model) {
		return "/realestate/registration/tj/bdcdjzb";
	}
	@RequestMapping(value = "/efficiency", method = RequestMethod.GET)
	public String efficency(Model model) {
		return "/realestate/registration/tj/efficiency";
	}
	
	@RequestMapping(value = "/fzmjtj",method = RequestMethod.GET)
	public String fzmjtj(Model model){
		return "/realestate/registration/tj/fzmjtj";
	}
	
	@RequestMapping(value = "/houseland", method = RequestMethod.GET)
	public String houselandjsp(Model model) {
		return "/realestate/registration/djywcx/houseland";
	}
	//鹰潭不动产单元查询
	@RequestMapping(value = "/houselandyt", method = RequestMethod.GET)
	public String houselandytjsp(Model model) {
		return "/realestate/registration/djywcx/houselandyt";
	}
	
	@RequestMapping(value="/yskcxltj",method=RequestMethod.GET)
	public String kscxtj(Model model){
		YwLogUtil.addYwLog("预审科查询量统计", ConstValue.SF.YES.Value, ConstValue.LOG.SEARCH);
		return "/realestate/registration/tj/yskcxltj";
	}
	
	/**
	 * 开发商导出楼盘表
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/gzlpbtj", method = RequestMethod.GET)
	public String gzlpbtj(Model model) {
		YwLogUtil.addYwLog("开发商导出发证信息", ConstValue.SF.YES.Value, ConstValue.LOG.SEARCH);
		return "/realestate/registration/tj/gzlpbtj";
	}
	
	@RequestMapping(value="/meslog",method=RequestMethod.GET)
	public String mesLog(Model model){
		YwLogUtil.addYwLog("信息日志统计", ConstValue.SF.YES.Value, ConstValue.LOG.SEARCH);
		return "/realestate/registration/tj/meslog";
	}
	
	/**
	 * 房屋基本信息查询，根据合同号
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/hbRelationId", method = RequestMethod.GET)
	public String gethbRelationid(Model model) {
		return "/realestate/registration/tj/gzfwjbzt";
	}
	
	/**
	 * 微信信息统计
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/mesWeiXin",method=RequestMethod.GET)
	public String mesWeiXin(Model model){
		YwLogUtil.addYwLog("微信信息统计", ConstValue.SF.YES.Value, ConstValue.LOG.SEARCH);
		return "/realestate/registration/tj/mesWeiXin";
	}
	
	/**
	 * 查询页（URL:"/SerachDyxx",Method:GET）
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/SearchFcxx", method = RequestMethod.GET)
	public String SearchFCxx(Model model) {
		return prefix + "cxdj/SearchFcxx";
	}
	
	@RequestMapping(value = "/SearchDyxx", method = RequestMethod.GET)
	public String SerachDyxx(Model model) {
		return prefix + "cxdj/SearchDyxx";
	}
	
	@RequestMapping(value = "/printSearch", method = RequestMethod.GET)
	public String ShowSerach(Model model) {
		return "/workflow/frame/printSearch";
	}
	
	@RequestMapping(value = "/PrintList", method = RequestMethod.GET)
	public String SerachList(Model model) {
		return prefix + "modules/common/PrintList";
	}
	
	/**
	 * 读取 宗地维护信息查询页面
	 * @作者 YX
	 * @创建时间
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/maintainlandQuery/", method = RequestMethod.GET)
	public String maintainLandQuery(Model model) {

		return "/maintain/module/maintainland";
	}

        /**
	 * 鹰潭登记业务统计
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/ywtj", method = RequestMethod.GET)
	public String ywtj(Model model) {
		return "/realestate/registration/tj/ywtj";
	}
	/**
	 * 鹰潭登记效能统计
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/xntj", method = RequestMethod.GET)
	public String xntj(Model model) {
		return "/realestate/registration/tj/efficiency_yt";
	}
	/**
	 * 鹰潭宗地单元统计
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/zddytj", method = RequestMethod.GET)
	public String zddytj(Model model) {
		return "/realestate/registration/tj/zddytj";
	}
	
	/**
	 * 修改权证号页面
	 * @author lius
	 * 
	 */
	@RequestMapping(value="/updateQzh",method= RequestMethod.GET)
	public String updateQzh(Model model){
		String path= prefix+"zydj/updateQzh";
		return path;
	}
	/**
	 * 购房补贴批量打印查档结果页面
	 */
	@RequestMapping(value = "/plprint", method = RequestMethod.GET)
	public String resultsPrint() {
		return prefix + "djywcx/plPrint";
	}
	/**
	 * 权利查询批量打印查档结果页面
	 */
	@RequestMapping(value = "/swplprint", method = RequestMethod.GET)
	public String swresultsPrint() {
		return prefix + "djywcx/swplPrint";
	}
	/**
	 * @Title: NohouseInfo
	 * @Description: 无房证明
	 * @Author：赵梦帆
	 * @Data：2016年10月27日 下午2:17:08
	 * @param model
	 * @return
	 * @return String
	 */
	@RequestMapping(value="/NohouseInfo",method= RequestMethod.GET)
	public String NohouseInfo(Model model){
		String path= prefix + "cxdj/NohouseInfo";
		return path;
	}
	
	/**
	 * @Title: ZsList
	 * @Description: 证书列表
	 * @Author：赵梦帆
	 * @Data：2016年11月15日 下午8:36:07
	 * @param model
	 * @return
	 * @return String
	 */
	@RequestMapping(value="/certListmod",method= RequestMethod.GET)
	public String ZsList(Model model){
		String path= prefix + "modules/common/certListmod";
		return path;
	}
	
	
	/**
	 * @Description: 登簿记事
	 */
	@RequestMapping(value = "/dbjs", method = RequestMethod.GET)
	public String dbjs(Model model) {
		return prefix + "common/dbjs";
	}
	
	/**
	 * @Description: 不动产登记确认书
	 */
	@RequestMapping(value = "/approveinfo", method = RequestMethod.GET)
	public String ApproveInfo(Model model) {
		return prefix + "common/ApproveInfo";
	}
	
	/**
	 * @Description: 不动产登记回执单
	 */
	@RequestMapping(value = "/approveReceipt ", method = RequestMethod.GET)
	public String BookReceipt(Model model) {
		return prefix + "common/ApproveReceipt";
	}
	/**
	 * @Description: 小土地证查询（可编辑）for 乌鲁木齐
	 * @Title: XTDZQuery
	 * @Author：赵梦帆
	 * @Data：2016年12月14日 上午10:59:26
	 * @param model
	 * @return
	 * @return String
	 */
	@RequestMapping(value = "/XTDZQuery/", method = RequestMethod.GET)
	public String XTDZQuery(Model model) {

		return "/realestate/registration/djywcx/XTDZQuery";
	}
	
	/**
	 * @Description: 小土地证详情页面
	 * @Title: workBookXTDZDetail
	 * @Author：赵梦帆
	 * @Data：2016年12月14日 下午5:15:19
	 * @param model
	 * @return
	 * @return String
	 */
	@RequestMapping(value = "/XTDZQueryDetails", method = RequestMethod.GET)
	public String workBookXTDZDetail(Model model) {

		return "/realestate/registration/djywcx/XTDZQueryDetails";
	}
	
	/**
	 * @Description: 不动产登记确认书
	 */
	@RequestMapping(value = "/XTDZDetail", method = RequestMethod.GET)
	public String XTDZDetail(Model model) {
		return prefix + "djywcx/XTDZDetail";
	}
	
	/**
	 * @Description: 抵押权注销时归还证书
	 * @Title: ShowXZFZ
	 * @Author：赵梦帆
	 * @Data：2016年12月20日 下午5:25:15
	 * @param model
	 * @return
	 * @return String
	 */
	@RequestMapping(value = "/dyfz", method = RequestMethod.GET)
	public String ShowXZFZ(Model model) {
		model.addAttribute("fzxxAttribute", new BDCS_DJFZ());
		return prefix + "dyzxdj/outcertPageEX";
	}
	
	/**
	 * @Description: 不动产登记回执单
	 */
	@RequestMapping(value = "/print", method = RequestMethod.GET)
	public String PrintTpl(Model model) {
		return prefix + "common/print";
	}
	
	/**
	 * 短信查询
	 */
	@RequestMapping(value="/messageQuery",method = RequestMethod.GET)
	public String messageQuery(Model model,HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		return prefix +"/djywcx/messageQuery";
		
	}
	/**
	 * 房屋土地监管
	 * 罗雨
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/houselandJG", method = RequestMethod.GET)
	public String houselandJGjsp(Model model) {
		return "/realestate/registration/djywcx/houselandJG";
	}

	/**
	 * 交易价格统计
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/jyjgtj", method = RequestMethod.GET)
	public String jyjgtj(Model model) {
		return "/realestate/registration/tj/jyjgtj";
	}
	
	/**
	 * @Description: 给乌鲁木齐的房屋查询
	 * @Title: GetFWinfo
	 * @Author: zhaomengfan
	 * @Date: 2017年1月20日上午9:54:01
	 * @return
	 * @return String
	 */
	@RequestMapping(value = "/GetFWinfo", method = RequestMethod.GET)
	public String GetFWinfo() {
		return prefix + "cxdj/FWinfo";
	}
	
	/**
	 * @Description: 四川内江--登记系统新增审批表页面（组件），其中包含“审批表附表1”和“审批表附表2”（便于大件批量审核）
	 * @Title: spbfbEX
	 * @Author: zhaomengfan
	 * @Date: 2017年2月20日上午10:25:28
	 * @return
	 * @return String
	 */
	@RequestMapping(value = "/spbfbNJ", method = RequestMethod.GET)
	public String spbfbEX() {
		return prefix + "csdj/approvalFormPage_NJ";
	}
	
	/**
	 * @Description: 数据上报管理
	 * @Title: getReportInfo
	 * @Author: yuxuebin
	 * @Date: 2017年02月27日 17:17:01
	 * @return
	 * @return String
	 */
	@RequestMapping(value = "/reportmanager", method = RequestMethod.GET)
	public String getReportInfo() {
		return prefix + "sjsb/reportmanager";
	}
	
	/**
	 * @Description: 数据上报统计
	 * @Title: getReportInfo
	 * @Date: 2017年02月27日 17:17:01
	 * @return
	 * @return String
	 */
	@RequestMapping(value = "/tjsb", method = RequestMethod.GET)
	public String getTJSB() {
		return prefix + "sjsb/TJSB";
	}
	
	/**
	 * 单元历史回溯页面
	 * @Title: unitHistoryPage 
	 * @author:liushufeng
	 * @date：2017年3月2日 下午3:48:55
	 * @return
	 */
	@RequestMapping(value="/unithistory")
	public String unitHistoryPage(){
		return "/realestate/registration/historyback/unithistory";
	}
	
	/**
	 * @Title: unitHistoryPageEx
	 * @Description: 单元历史回溯 另一版
	 * @Author：	zhaomf
	 * @Data：2017年11月17日 上午10:13:10
	 * @return：	String
	 * @return
	 */
	@RequestMapping(value = "/unithistoryex")
	public String unitHistoryPageEx() {
		return "/realestate/registration/historyback/unithistoryEx";
	}

	/**
	 * @Description: 黑名单
	 * @Title: BlackList
	 * @Author: zhaomengfan
	 * @Date: 2017年4月13日上午8:54:56
	 * @return
	 * @return String
	 */
	@RequestMapping(value="/blacklist")
	public String BlackList() {
		return prefix + "common/blacklist";
	}

	/**
	 * 批量上传分户图页面
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/cadhquery", method = RequestMethod.GET)
	public String workHouse(Model model) {
		return "/realestate/registration/djywcx/CADHQuery";
	}
	
	/**
	 * 指认图查询页面
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/identifyPictureQuery", method = RequestMethod.GET)
	public String identifyPictureQuery(Model model) {
		return "/realestate/registration/djywcx/identifyPictureQuery";
	}
	
	/**
	 * 日志查询
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/loginfoquery", method = RequestMethod.GET)
	public String logQuery(Model model) {
		return "/realestate/registration/djywcx/LogInfoQuery";
	}
	
	/**
	 * 数据维护对比
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/logcontrast", method = RequestMethod.GET)
	public String logContrast(Model model) {
		return "/maintain/maintainContrast";
	}
	
	/**
	 * 批量限制
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/getplxz", method = RequestMethod.GET)
	public String pLXZ(Model model) {
		return "/realestate/registration/djywcx/plxz";
	}
	/**
	 * 宗地注销信息
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/zdzxinfo", method = RequestMethod.GET)
	public String zdzxinfo(Model model) {
		return "/realestate/registration/djywcx/logoutinfo_zd";
	}

	/**
	 * 房屋查询--编辑证件号码
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/editzjhm", method = RequestMethod.GET)
	public String editZJHM(Model model) {
		return "/realestate/registration/djywcx/houseQuery_editzjhm";
	}

	/**
	 * 历史单元信息页面
	 * @return
	 */
	@RequestMapping(value = "/unitinfo", method = RequestMethod.GET)
	public String unitinfo() {
		return "/realestate/registration/djywcx/unitinfo";
	}
	
	/**
	 * 转发后，单独显示历史产权页面信息
	 * @author huangmingh
	 * @param model
	 * @return
	 * 2017年7月6日14:41:49
	 */
	
	@RequestMapping(value = "/historyinfo", method = RequestMethod.GET)
	public String historyinfo() {
		return "/realestate/registration/djywcx/historyinfo";
	}
	/**
	 * 转发后，单独显示证明信息
	 * 
	 * @param model
	 * @return
	 * 
	 */
	@RequestMapping(value = "/zmxx", method = RequestMethod.GET)
	public String zmxx() {
		return "/realestate/registration/djywcx/zmxx";
	}
	@RequestMapping(value = "/cqHxx", method = RequestMethod.GET)
	public String cqHxx(Model model) {
		return "/realestate/registration/tj/cqHxx";
	}
	
	@RequestMapping(value="/szzftj",method=RequestMethod.GET)
	public String getSZZFTJ(Model model) {
		return "/realestate/registration/tj/szzftj";
	}
	
	/**
	 * 证书编号统计
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/zsbhtj", method = RequestMethod.GET)
	public String getZSBHTJ(Model model) {
		return "/realestate/registration/tj/zsbhtj";
	}
	
	/**
	 * 新增抵押权人页面（BG027，BG028）
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/addDyqlrs",method=RequestMethod.GET)
	public String addDyqrs() {
		return "/realestate/registration/modules/dybg/dyqlrs";
	}
	/**
	 * 不动产登记进展情况月报--鹰潭
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/djywtj_month", method = RequestMethod.GET)
	public String getDJYWTJByMonth(Model model) {
		return "/realestate/registration/tj/djywtj_month";
	}
	
	/**
	 * 不动产登记缮证统计
	 * @作者 yuxuebin
	 * @创建时间 2016年04月11日18:06:52
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/CertificateStatistics", method = RequestMethod.GET)
	public String CertificateStatistics(Model model) {

		return "/realestate/registration/djywcx/certificateStatistics";
	}
	/**
	 * 不动产登记发证统计
	 * @作者 yuxuebin
	 * @创建时间 2016年04月11日18:06:52
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/IssueStatistics", method = RequestMethod.GET)
	public String IssueStatistics(Model model) {
		return "/realestate/registration/djywcx/issueStatistics";
	}
	
	/**
	 * 海域信息查询页面
	 * @作者 heks
	 * @创建时间  2017-05-11 9:57
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/seaQuery/", method = RequestMethod.GET)
	public String workBookSea(Model model) {

		return "/realestate/registration/djywcx/seaQuery";
	}
	
	/**
	 * 读取 海域信息查询详情页面
	 * @作者 heks
	 * @创建时间
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/seaQueryDetails", method = RequestMethod.GET)
	public String workBookseaDetail(Model model) {

		return "/realestate/registration/djywcx/seaQueryDetails";
	}
	
	/**
	 * 申请审批表页审批意见动态生成版（URL:"/sqspb_version2",Method:GET）审批意见框动态生成（钦州使用）
	 * @author wuzhu
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/sqspb_auto", method = RequestMethod.GET)
	public String ShowSQSPBIndex_auto(Model model) {
		return prefix + "csdj/approvalFormPage_auto";
	}
	
	/**
	 * @Description: 数据上报管理
	 * @Title: getReportInfo
	 * @Author: yuxuebin
	 * @Date: 2017年02月27日 17:17:01
	 * @return
	 * @return String
	 */
	@RequestMapping(value = "/autoreport", method = RequestMethod.GET)
	public String getAutoReportIndex() {
		return prefix + "sjsb/autoreport";
	}
	
	/**
	 * @Description: 查询个人的房地产权信息
	 * @author 凌广清
	 * @Date: 2018年10月10日 20点01分
	 * @return String
	 */
	@RequestMapping(value = "/propertyInfo", method = RequestMethod.GET)
	public String getPropertyInfo() {
		return prefix + "propertyInfoQuery/propertyInfo";
	}
	@RequestMapping(value = "/houseQueryJYYT/", method = RequestMethod.GET)
	public String workBookHouseJYYT(Model model) { 
		String JYYTURL = ConfigHelper.getNameByValue("JYYTURL");//交易系统URL，如http://localhost:8086/jyyt
		String XZQHDM = ConfigHelper.getNameByValue("XZQHDM");
		model.addAttribute("JYYTURL", JYYTURL);
		model.addAttribute("XZQHDM", XZQHDM); 
		return "/realestate/registration/djywcx/houseQueryJYYT";
	}
	/***
	 * 数据挂接,不动产房屋和房产房屋关系挂接  
 	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/correctHouseCHIDIndex",method=RequestMethod.GET)
	public String correctHouseCHIDIndex(HttpServletRequest request,
			HttpServletResponse response) {
		return "/realestate/registration/jyyt/correctHouseCHIDIndex";
	}

	/**
	 * @Title: unitContrast
	 * @Description: 单元相关信息（单元、权利）对比功能 ——需作为项目的一个组件进行配置使用（柳州）
	 * @author:冉全
	 * @date:2018年3月27日 上午10:56:06
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/unitcontrast", method = RequestMethod.GET)
	public String unitContrast(Model model) {
		return prefix + "unit_contrast/unitContrastPage";
	}
	

	/**
	 * 填写网签的单元列表页面（URL:"/dyxxfornewhouse",Method:GET）
	 * JOE
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/dyxxfornethouse", method = RequestMethod.GET)
	public String dyxxForNewHouse(Model model, HttpServletRequest request) {
		String project_id = request.getParameter("file_number");
		String path = prefix +"csdj/netHousePage" ;
		return path;
	}
	
	/**
	 * 填写税务的单元列表页面（URL:"/dyxxfornewhouse",Method:GET）
	 * JOE
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/dyxxfortax", method = RequestMethod.GET)
	public String dyxxForTax(Model model, HttpServletRequest request) {
		String project_id = request.getParameter("file_number");
		String path = prefix +"csdj/taxHousePage" ;
		return path;
	}
}
