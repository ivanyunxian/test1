package com.supermap.realestate.registration.service.impl;

import com.supermap.realestate.registration.ViewClass.ProjectInfo;
import com.supermap.realestate.registration.ViewClass.ZS;
import com.supermap.realestate.registration.factorys.HandlerFactory;
import com.supermap.realestate.registration.mapping.HandlerMapping.RegisterWorkFlow;
import com.supermap.realestate.registration.service.NaviService;
import com.supermap.realestate.registration.util.ConstValue.BDCDYLX;
import com.supermap.realestate.registration.util.ConstValue.DJLX;
import com.supermap.realestate.registration.util.ConstValue.QLLX;
import com.supermap.realestate.registration.util.ProjectHelper;
import com.supermap.realestate.registration.util.RequestHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

@Service("naviService")
public class NaviServiceImpl implements NaviService {

	/**
	 * 获取单元信息页
	 */
	@Override
	public String GetDYXXPageUrl(String project_id) {
		RegisterWorkFlow flow = HandlerFactory.getWorkflow(project_id);
		if (flow == null)
			return getDefaultUnitPage(project_id);
		return flow.getUnitpagejsp();
	}

	/**
	 * 获取权利信息页
	 */
	@Override
	public String GetQLXXPageUrl(String project_id) {
		RegisterWorkFlow flow = HandlerFactory.getWorkflow(project_id);
		if (flow == null)
			return getDefaultRightsPage(project_id);
		return flow.getRightspagejsp();
	}

	/**
	 * 获取登簿信息页
	 */
	@Override
	public String GetDBPageUrl(String project_id) {
		RegisterWorkFlow flow = HandlerFactory.getWorkflow(project_id);
		if (flow == null)
			return getDefaultBookPage(project_id);
		return flow.getBookpagejsp();
	}

	/**
	 * 在配置文件中找不到对应的流程编号的时候，获取默认的单元信息页，尽量不要用
	 * @Title: getDefaultUnitPage
	 * @author:liushufeng
	 * @date：2015年7月17日 下午8:15:37
	 * @param project_id
	 * @return
	 */
	private String getDefaultUnitPage(String project_id) {
		String pageUrl = "error/error";
		if (!StringUtils.isEmpty(project_id)) {
			ProjectInfo info = ProjectHelper.GetPrjInfoByPrjID(project_id);
			if (info != null) {
				String xmdylx = info.getBdcdylx();
				String dir = null;
				String pagename = null;
				String djlx = info.getDjlx();
				String qllx = info.getQllx();
				// 根据登记类别加上目录
				if (djlx.equals(DJLX.CSDJ.Value)) { // 初始登记
					if (qllx.equals(QLLX.DIYQ.Value)) { // 抵押权初始登记
						dir = "dydj/";
					} else {
						dir = "csdj/";
					}
				} else if (djlx.equals(DJLX.ZYDJ.Value)) { // 转移登记
					dir = "zydj/";
					if (qllx.equals(QLLX.DIYQ.Value)) { // 抵押权转移登记
						dir = dir + "dydj/";
					}
				} else if (djlx.equals(DJLX.BGDJ.Value)) { // 变更登记
					dir = "bgdj/";
				} else if (djlx.equals(DJLX.YGDJ.Value)) { // 预告登记
					dir = "notice/";
				} else if (djlx.equals(DJLX.CFDJ.Value)) { // 查封登记
					if (qllx.equals("99")) {// 查封登记
						dir = "cfdj/";
					} else {
						dir = "cfdjzx/";// 查封注销
					}
				} else if (djlx.equals(DJLX.ZXDJ.Value)) { // 注销登记
					if (qllx.equals(QLLX.DIYQ.Value)) {
						dir = "dyzxdj/";
					} else {
						dir = "zxdj/";
					}
				} else if (djlx.equals(DJLX.GZDJ.Value)) { // 注销登记
					dir = "gzdj/";
				} else if (djlx.equals(DJLX.YYDJ.Value)) { // 注销登记
					dir = "yydj/";
				}

				// 根据不动产单元类型判断
				if (xmdylx.equals(BDCDYLX.SHYQZD.Value)) {
					pagename = "landUnitPage";// 宗地
				} else if (xmdylx.equals(BDCDYLX.H.Value)) {
					pagename = "houseUnitPage";// 房屋（户）
				} else if (xmdylx.equals(BDCDYLX.YCH.Value)) {
					pagename = "preHouseUnitPage";// 预测房屋（户）
				} else if (xmdylx.equals(BDCDYLX.ZRZ.Value)) {
					pagename = "buildingUnitPage";// 自然幢
				} else if (xmdylx.equals(BDCDYLX.SYQZD.Value)) {// sunhb-2015-06-23
					pagename = "OwnershiplandUnitPage";// 宗地
				} else if (xmdylx.equals(BDCDYLX.HY.Value)) {// sunhb-2015-06-23
					pagename = "seaUnitPage";// 宗海
				}
				if (djlx.equals(DJLX.YGDJ.Value)) {// 暂时定死测试用
					pagename = "houseUnitPage";// 房屋（户）
				} else if (xmdylx.equals(BDCDYLX.LD.Value)) {
					pagename = "forestUnitPage";
				}
				pageUrl = dir + pagename;
			}
		}
		return pageUrl;
	}

	/**
	 * 在配置文件中找不到对应的流程编号的时候，获取默认的权利信息页，尽量不要使用
	 * @Title: getDefaultRightsPage
	 * @author:liushufeng
	 * @date：2015年7月18日 下午4:51:29
	 * @param project_id
	 * @return
	 */
	private String getDefaultRightsPage(String project_id) {
		String pagePath = "";
		if (!StringUtils.isEmpty(project_id) && !"null".equals(project_id)) {
			ProjectInfo info = ProjectHelper.GetPrjInfoByPrjID(project_id);
			BDCDYLX xmdylx = BDCDYLX.initFrom(info.getBdcdylx());
			String dir = "csdj/";
			String file = "landRightsInfoPage";

			if (info.getDjlx().equals(DJLX.CSDJ.Value) && info.getQllx().equals(QLLX.DIYQ.Value)) { // 抵押权初始登记
				dir = "dydj/";
			} else if (info.getDjlx().equals(DJLX.ZYDJ.Value) && info.getQllx().equals(QLLX.DIYQ.Value)) { // 抵押权转移登记
				dir = "dydj/";
			} else if (info.getDjlx().equals(DJLX.YGDJ.Value)) { // 预告登记
				dir = "notice/";
			} else if (info.getDjlx().equals(DJLX.CFDJ.Value)) {// 查封登记
				if (info.getQllx().equals("99")) { // 查封登记
					dir = "cfdj/";
				} else {
					dir = "cfdjzx/";// 查封注销
				}
			} else if (info.getDjlx().equals(DJLX.ZXDJ.Value)) { // 注销登记
				if (info.getQllx().equals(QLLX.DIYQ.Value)) { // 抵押权注销登记
					dir = "dyzxdj/";
				}
			} else if (info.getDjlx().equals(DJLX.YYDJ.Value)) { // 注销登记
				dir = "yydj/";
			}

			if (info.getDjlx().equals(DJLX.YGDJ.Value)) {
				if (info.getSllx1().equals("01")) {
					file = "houseRightsInfoPage";
				} else {
					dir = "dydj/";
					file = "houseRightsInfoPage";
				}
			} else {
				if (xmdylx.equals(BDCDYLX.SHYQZD)) {
					file = "landRightsInfoPage";
				} else if (xmdylx.equals(BDCDYLX.H)) {
					if (info.getSllx1().equals("50") && (info.getSllx2().equals("02") || info.getSllx2().equals("03"))) {
						file = "RealHouseRightsInfoPage";
					} else {
						file = "houseRightsInfoPage";
					}
				} else if (xmdylx.equals(BDCDYLX.ZRZ)) {
					file = "buildingRightsInfoPage";
				} else if (xmdylx.equals(BDCDYLX.SYQZD)) { // sunhb-20150624添加所有权宗地页面
					file = "OwnershiplandRightsInfoPage";
				} else if (xmdylx.equals(BDCDYLX.HY)) { // sunhb-20150624添加所有权宗地页面
					file = "seaRightsInfoPage";
				} else if (xmdylx.equals(BDCDYLX.LD)) { // sunhb-20150624添加森林林木页面
					file = "forestRightsInfoPage";
				} else if (xmdylx.equals(BDCDYLX.YCH)) { // sunhb-2015-07-10
															// 添加预测户页面
					file = "houseRightsInfoPage";
				}
			}
			pagePath = dir + file;
		}
		return pagePath;
	}

	/**
	 * 在配置文件中找不到对应的流程编号的时候，获取默认的登记薄预览页，尽量不要使用
	 * @Title: getDefaultBookPage
	 * @author:liushufeng
	 * @date：2015年7月18日 下午4:54:07
	 * @param project_id
	 * @return
	 */
	public String getDefaultBookPage(String project_id) {
		String pageUrl = "error/error";
		if (!StringUtils.isEmpty(project_id)) {
			ProjectInfo info = ProjectHelper.GetPrjInfoByPrjID(project_id);
			if (info != null) {
				String xmdylx = info.getBdcdylx();

				// 初始登记
				if (info.getDjlx().equals(DJLX.CSDJ.Value)) {
					if (info.getDjlx().equals(DJLX.CSDJ.Value) && info.getQllx().equals(QLLX.DIYQ.Value)) {
						pageUrl = "dydj/mortgageBookPage"; // 抵押权
					} else if (xmdylx.equals(BDCDYLX.SHYQZD.Value)) {
						pageUrl = "csdj/landBookPage"; // 宗地
					} else if (xmdylx.equals(BDCDYLX.H.Value) || (xmdylx.equals(BDCDYLX.ZRZ.Value) && !"03".equals(info.getSllx1()))) {
						pageUrl = "csdj/buildingBookPage"; // 房屋（户）
					} else if (xmdylx.equals(BDCDYLX.ZRZ.Value) && "03".equals(info.getSllx1())) {
						pageUrl = "csdj/multibuildingBookPage"; // 項目內多幢
					} else if (xmdylx.equals(BDCDYLX.SYQZD.Value)) { // sunhb-2015-06-24
						pageUrl = "csdj/ownershiplandBookPage"; // 宗地 所有权登簿
					} else if (xmdylx.equals(BDCDYLX.HY.Value)) { // sunhb-2015-06-26
						pageUrl = "csdj/seaBookPage"; // 宗海登簿
					} else if (xmdylx.equals(BDCDYLX.LD.Value)) { // sunhb-2015-06-27
						pageUrl = "csdj/forestBookPage";// 林地登簿
					}
				} else if (info.getDjlx().equals(DJLX.ZYDJ.Value)) { // 转移登记
					if (info.getDjlx().equals(DJLX.ZYDJ.Value) && info.getQllx().equals(QLLX.DIYQ.Value)) {
						pageUrl = "dydj/mortgageBookPage";// 抵押权
					} else if (xmdylx.equals(BDCDYLX.SHYQZD.Value)) {
						pageUrl = "csdj/landBookPage";// 宗地
					} else if (xmdylx.equals(BDCDYLX.H.Value)) {
						pageUrl = "csdj/buildingBookPage";// 房屋（户）
					}
				} else if (info.getDjlx().equals(DJLX.ZXDJ.Value)) { // 注销登记
					// 抵押注销登记
					if (info.getQllx().equals(QLLX.DIYQ.Value)) {
						pageUrl = "dydj/mortgageBookPage";
					} else {
						if (xmdylx.equals(BDCDYLX.SHYQZD.Value)) {
							pageUrl = "zxdj/landBookPage"; // 宗地
						} else if (xmdylx.equals(BDCDYLX.H.Value)) {
							pageUrl = "zxdj/buildingBookPage"; // 房屋（户）
						}
					}
				}

				// 变更登记
				if (info.getDjlx().equals(DJLX.BGDJ.Value)) {
					if (xmdylx.equals(BDCDYLX.SHYQZD.Value)) {
						pageUrl = "csdj/landBookPage"; // 宗地
					} else if (xmdylx.equals(BDCDYLX.H.Value)) {
						pageUrl = "csdj/buildingBookPage"; // 房屋（户）
					}
				} else if (info.getDjlx().equals(DJLX.YGDJ.Value)) { // 预告登记
					if (xmdylx.equals(BDCDYLX.SHYQZD.Value)) {
						pageUrl = "csdj/landBookPage"; // 宗地
					} else if (xmdylx.equals(BDCDYLX.H.Value)) {
						pageUrl = "notice/noticeBookPage"; // 房屋（户）
					}
				} else if (info.getDjlx().equals(DJLX.GZDJ.Value)) { // 更正登记
					if (xmdylx.equals(BDCDYLX.SHYQZD.Value)) {
						pageUrl = "csdj/landBookPage"; // 宗地
					} else if (xmdylx.equals(BDCDYLX.H.Value) || xmdylx.equals(BDCDYLX.ZRZ.Value)) {
						pageUrl = "csdj/buildingBookPage"; // 房屋（户）
					} else if (xmdylx.equals(BDCDYLX.SYQZD.Value)) { // sunhb-2015-06-24
						pageUrl = "csdj/landBookPage";// 宗地
					}
				} else if (info.getDjlx().equals(DJLX.YYDJ.Value)) { // 异议登记
					pageUrl = "yydj/bookPage";
				}
				// 查封登记
				if (info.getDjlx().equals(DJLX.CFDJ.Value)) {
					if (xmdylx.equals(BDCDYLX.SHYQZD.Value)) {
						if (info.getQllx().equals(QLLX.QTQL.Value)) {
							pageUrl = "cfdj/landBookPage";// 宗地
						} else {
							pageUrl = "cfdjzx/landBookPage";// 宗地注销
						}
					} else if (xmdylx.equals(BDCDYLX.H.Value)) {
						if (info.getQllx().equals(QLLX.QTQL.Value)) {
							pageUrl = "cfdj/buildingBookPage";// 房屋（户）
						} else {
							pageUrl = "cfdjzx/buildingBookPage";// 房屋（户）
						}
					}
				}
			}
		}
		return pageUrl;
	}

	@Override
	public ZS GetInfo(HttpServletRequest request) throws UnsupportedEncodingException {
		ZS zs = new ZS();
//		String qzlx_page = RequestHelper.getParam(request,"qzlx_page");
//		String zsid = RequestHelper.getParam(request, "zsid");
		zs.setBdcdyh("undefined".equals(RequestHelper.getParam(request, "bdcdyh")) ? "" : RequestHelper.getParam(request, "bdcdyh"));
		zs.setCqzh("undefined".equals(RequestHelper.getParam(request,"cqzh")) ? "" : RequestHelper.getParam(request,"cqzh"));
		zs.setFj("undefined".equals(RequestHelper.getParam(request,"fj")) ? "" : RequestHelper.getParam(request,"fj"));
		zs.setFm_day("undefined".equals(RequestHelper.getParam(request,"fm_day") ) ? "" :RequestHelper.getParam(request,"fm_day"));
		zs.setFm_month("undefined".equals(RequestHelper.getParam(request,"fm_month")) ? "" :RequestHelper.getParam(request,"fm_month"));
		zs.setFm_year("undefined".equals(RequestHelper.getParam(request,"fm_year")) ? "" :RequestHelper.getParam(request,"fm_year"));
		zs.setGyqk("undefined".equals(RequestHelper.getParam(request,"gyqk")) ? "" :RequestHelper.getParam(request,"gyqk"));
		zs.setMj("undefined".equals(RequestHelper.getParam(request,"mj")) ? "" :RequestHelper.getParam(request,"mj"));
		zs.setNd("undefined".equals(RequestHelper.getParam(request,"nd")) ? "" :RequestHelper.getParam(request,"nd"));
		zs.setQhjc("undefined".equals(RequestHelper.getParam(request,"qhjc")) ? "" :RequestHelper.getParam(request,"qhjc"));
		zs.setQhmc("undefined".equals(RequestHelper.getParam(request,"qhmc")) ? "" :RequestHelper.getParam(request,"qhmc"));
		zs.setQllx("undefined".equals(RequestHelper.getParam(request,"qllx")) ? "" :RequestHelper.getParam(request,"qllx"));
		zs.setQlqtzk("undefined".equals(RequestHelper.getParam(request,"qlqtzk")) ? "" :RequestHelper.getParam(request,"qlqtzk"));
		zs.setQlr("undefined".equals(RequestHelper.getParam(request,"qlr")) ? "" :RequestHelper.getParam(request,"qlr"));
		zs.setQlxz("undefined".equals(RequestHelper.getParam(request,"qlxz")) ? "" :RequestHelper.getParam(request,"qlxz"));
		zs.setSyqx("undefined".equals(RequestHelper.getParam(request,"syqx")) ? "" :RequestHelper.getParam(request,"syqx"));
		zs.setYt("undefined".equals(RequestHelper.getParam(request,"yt")) ? "" :RequestHelper.getParam(request,"yt"));
		zs.setZl("undefined".equals(RequestHelper.getParam(request,"zl")) ? "" :RequestHelper.getParam(request,"zl"));
		zs.setQrcode(RequestHelper.getParam(request,"qrcode"));
		zs.setId_zsbh("undefined".equals(RequestHelper.getParam(request,"id_zsbh")) ? "" :RequestHelper.getParam(request,"id_zsbh"));
		zs.setType(RequestHelper.getParam(request,"_tpl"));
		zs.setQt("undefined".equals(RequestHelper.getParam(request,"qt")) ? "" :RequestHelper.getParam(request,"qt"));
		zs.setYwr("undefined".equals(RequestHelper.getParam(request,"ywr")) ? "" :RequestHelper.getParam(request,"ywr"));
		zs.setZmqlhsx("undefined".equals(RequestHelper.getParam(request,"zmqlhsx")) ? "" :RequestHelper.getParam(request,"zmqlhsx"));
		return zs;
	}
}
