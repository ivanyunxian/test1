package com.supermap.realestate.registration.ViewClass;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.supermap.realestate.registration.model.BDCS_DJDY_GZ;
import com.supermap.realestate.registration.model.BDCS_DJFZ;
import com.supermap.realestate.registration.model.BDCS_DJGD;
import com.supermap.realestate.registration.model.BDCS_DJSZ;
import com.supermap.realestate.registration.model.BDCS_FSQL_GZ;
import com.supermap.realestate.registration.model.BDCS_H_GZ;
import com.supermap.realestate.registration.model.BDCS_H_XZ;
import com.supermap.realestate.registration.model.BDCS_H_XZY;
import com.supermap.realestate.registration.model.BDCS_QL_GZ;
import com.supermap.realestate.registration.model.BDCS_QL_XZ;
import com.supermap.realestate.registration.model.BDCS_SHYQZD_GZ;
import com.supermap.realestate.registration.model.BDCS_SHYQZD_XZ;
import com.supermap.realestate.registration.model.BDCS_SQR;
import com.supermap.realestate.registration.model.BDCS_XMXX;
import com.supermap.realestate.registration.model.BDCS_ZRZ_GZ;
import com.supermap.realestate.registration.model.interfaces.House;
import com.supermap.realestate.registration.tools.UnitTools;
import com.supermap.realestate.registration.util.ConstHelper;
import com.supermap.realestate.registration.util.ConstValue.BDCDYLX;
import com.supermap.realestate.registration.util.ConstValue.DJDYLY;
import com.supermap.realestate.registration.util.ConstValue.DJLX;
import com.supermap.realestate.registration.util.ConstValue.QLLX;
import com.supermap.realestate.registration.util.ProjectHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import com.supermap.wisdombusiness.workflow.model.Wfi_ProInst;
import com.supermap.wisdombusiness.workflow.service.common.Approval;
import com.supermap.wisdombusiness.workflow.service.wfm.SmProSPService;

/**
 * 
 * @Description:申请审批表
 * @author OuZhanRong
 * @date 2015年6月22日 下午2:51:15
 * @Copyright SuperMap
 */

public class SQSPB {
	private String t_qllx_1;
	private String t_qllx_3;
	private String t_qllx_5;
	private String t_qllx_7;
	private String t_qllx_9;
	private String t_qllx_11;
	private String t_qllx_15;
	private String t_qllx_17;
	private String t_qllx_fwsyq;
	private String t_qllx_gzwsyq;

	public String getT_qllx_1() {
		return t_qllx_1;
	}

	public void setT_qllx_1(String t_qllx_1) {
		this.t_qllx_1 = t_qllx_1;
	}

	public String getT_qllx_3() {
		return t_qllx_3;
	}

	public void setT_qllx_3(String t_qllx_3) {
		this.t_qllx_3 = t_qllx_3;
	}

	public String getT_qllx_5() {
		return t_qllx_5;
	}

	public void setT_qllx_5(String t_qllx_5) {
		this.t_qllx_5 = t_qllx_5;
	}

	public String getT_qllx_7() {
		return t_qllx_7;
	}

	public void setT_qllx_7(String t_qllx_7) {
		this.t_qllx_7 = t_qllx_7;
	}

	public String getT_qllx_9() {
		return t_qllx_9;
	}

	public void setT_qllx_9(String t_qllx_9) {
		this.t_qllx_9 = t_qllx_9;
	}

	public String getT_qllx_11() {
		return t_qllx_11;
	}

	public void setT_qllx_11(String t_qllx_11) {
		this.t_qllx_11 = t_qllx_11;
	}

	public String getT_qllx_15() {
		return t_qllx_15;
	}

	public void setT_qllx_15(String t_qllx_15) {
		this.t_qllx_15 = t_qllx_15;
	}

	public String getT_qllx_17() {
		return t_qllx_17;
	}

	public void setT_qllx_17(String t_qllx_17) {
		this.t_qllx_17 = t_qllx_17;
	}

	public String getT_qllx_fwsyq() {
		return t_qllx_fwsyq;
	}

	public void setT_qllx_fwsyq(String t_qllx_fwsyq) {
		this.t_qllx_fwsyq = t_qllx_fwsyq;
	}

	public String getT_qllx_gzwsyq() {
		return t_qllx_gzwsyq;
	}

	public void setT_qllx_gzwsyq(String t_qllx_gzwsyq) {
		this.t_qllx_gzwsyq = t_qllx_gzwsyq;
	}

	public String getT_qllx_sllmsyq() {
		return t_qllx_sllmsyq;
	}

	public void setT_qllx_sllmsyq(String t_qllx_sllmsyq) {
		this.t_qllx_sllmsyq = t_qllx_sllmsyq;
	}

	public String getT_qllx_sllmshyq() {
		return t_qllx_sllmshyq;
	}

	public void setT_qllx_sllmshyq(String t_qllx_sllmshyq) {
		this.t_qllx_sllmshyq = t_qllx_sllmshyq;
	}

	public String getT_qllx_23() {
		return t_qllx_23;
	}

	public void setT_qllx_23(String t_qllx_23) {
		this.t_qllx_23 = t_qllx_23;
	}

	public String getT_qllx_19() {
		return t_qllx_19;
	}

	public void setT_qllx_19(String t_qllx_19) {
		this.t_qllx_19 = t_qllx_19;
	}

	public String getT_qllx_qt() {
		return t_qllx_qt;
	}

	public void setT_qllx_qt(String t_qllx_qt) {
		this.t_qllx_qt = t_qllx_qt;
	}

	public String getT_djlx_100() {
		return t_djlx_100;
	}

	public void setT_djlx_100(String t_djlx_100) {
		this.t_djlx_100 = t_djlx_100;
	}

	public String getT_djlx_200() {
		return t_djlx_200;
	}

	public void setT_djlx_200(String t_djlx_200) {
		this.t_djlx_200 = t_djlx_200;
	}

	public String getT_djlx_300() {
		return t_djlx_300;
	}

	public void setT_djlx_300(String t_djlx_300) {
		this.t_djlx_300 = t_djlx_300;
	}

	public String getT_djlx_400() {
		return t_djlx_400;
	}

	public void setT_djlx_400(String t_djlx_400) {
		this.t_djlx_400 = t_djlx_400;
	}

	public String getT_djlx_500() {
		return t_djlx_500;
	}

	public void setT_djlx_500(String t_djlx_500) {
		this.t_djlx_500 = t_djlx_500;
	}

	public String getT_djlx_600() {
		return t_djlx_600;
	}

	public void setT_djlx_600(String t_djlx_600) {
		this.t_djlx_600 = t_djlx_600;
	}

	public String getT_djlx_700() {
		return t_djlx_700;
	}

	public void setT_djlx_700(String t_djlx_700) {
		this.t_djlx_700 = t_djlx_700;
	}

	public String getT_djlx_800() {
		return t_djlx_800;
	}

	public void setT_djlx_800(String t_djlx_800) {
		this.t_djlx_800 = t_djlx_800;
	}

	public String getT_djlx_900() {
		return t_djlx_900;
	}

	public void setT_djlx_900(String t_djlx_900) {
		this.t_djlx_900 = t_djlx_900;
	}

	private String t_qllx_sllmsyq;
	private String t_qllx_sllmshyq;
	private String t_qllx_23;
	private String t_qllx_19;
	private String t_qllx_qt;
	private String t_djlx_100;
	private String t_djlx_200;
	private String t_djlx_300;
	private String t_djlx_400;
	private String t_djlx_500;
	private String t_djlx_600;
	private String t_djlx_700;
	private String t_djlx_800;
	private String t_djlx_900;

	private String bh;
	private String rq;
	private String sjr;
	private String dw;
	private String qllx;
	private String djlx;
	private String qllxmc;

	private String djlxmc;
	private String fwxz;
	
	private String qlrxm;
	private String zjzl;
	private String zjh;
	private String dz;
	private String yb;
	private String fddbr;
	// 法定代表人电话--刘树峰2015年10月28日
	private String fddbrdh;
	// 法定代表人证件号码
	private String fddbrzjhm;
	private String dh;
	private String dlrxm;
	private String dlrsfzh;
	 //审批意见第二版本
    private List<Map> spyjs=new ArrayList<Map>();
    //审批定义
    private List<Map> spdys=new ArrayList<Map>();
	public String getDlrsfzh() {
		return dlrsfzh;
	}
	public List<Map> getSpdys() {
		return spdys;
	}
	public void setSpdys(List<Map> spdys) {
		this.spdys = spdys;
	}
	public List<Map> getSpyjs() {
		return spyjs;
	}
	public void setSpyjs(List<Map> spyjs) {
		this.spyjs = spyjs;
	}
	public void setDlrsfzh(String dlrsfzh) {
		this.dlrsfzh = dlrsfzh;
	}

	public String getDlrsfzh1() {
		return dlrsfzh1;
	}

	public void setDlrsfzh1(String dlrsfzh1) {
		this.dlrsfzh1 = dlrsfzh1;
	}

	private String dlrdh;
	private String dljgmc;
	private String qlrxm1;
	private String zjzl1;
	private String zjh1;
	private String dz1;
	private String yb1;
	private String fddbr1;
	// 法定代表人电话--刘树峰2015年10月28日
	private String fddbrdh1;
	private String fddbrzjhm1;
	private String dh1;
	private String dlrxm1;
	private String dlrsfzh1;
	private String dlrdh1;
	private String dljgmc1;
	private String zl;
	private String bdcdyh;
	private String bdclx;
	//申请表不动产类型值显示土地或者土地/房屋
	private String bdclx_tdfw;
	private String mj;
	//土地分摊面积/房屋建筑面积
	private String tdftmj_fwjzmj;
	private String yt;
	private String ybdcqzsh;
	private String yhlx;
	private String gzwlx;
	private String lz;
	private String bdbe;
	private String qx;
	private String dyfw;
	private String xydzl;
	private String xydbdcdyh;
	private String djyy;
	private String zmwj;
	private String zsbs;
	private String fbcz;
	private String bz;
	private String fj;
	
	//TODO --------qlrxm2 新增的利害关系人信息 liangq
	private String qlrxm2;
	private String zjzl2;
	private String zjh2;
	private String dz2;
	private String yb2;
	private String fddbr2;
	private String fddbrdh2;
	private String fddbrzjhm2;
	private String dh2;
	private String dlrxm2;
	private String dlrsfzh2;
	private String dlrdh2;
	private String dljgmc2;
	//------------------------------
	
	public String getFj() {
		return fj;
	}

	public void setFj(String fj) {
		this.fj = fj;
	}

	private String sqrqz;
	private String sqrqz2;
	private String dlrqz;
	private String dlrqz2;
	private String qzrq;
	private String qzrq2;
	private String csnr;
	private String fsnr;
	private String hdnr;
	private String scr;
	private String scr2;
	private String fzr;
	private String scrq;
	private String scrq2;
	private String fzrrq;
	private String bz2;
	private List<Map<String, String>> houses;
	private List<Map> sqrs;
	private Map<String, String> ex;
	private List<Map> sqrs2;

	public List<Map> getSqrs2() {
		return sqrs2;
	}

	public void setSqrs2(List<Map> sqrs22) {
		this.sqrs2 = sqrs22;
	}

	private String mainqllx;

	public String getMainqllx() {
		return mainqllx;
	}

	public void setMainqllx(String mainqllx) {
		this.mainqllx = mainqllx;
	}

	public String getGyqk() {
		return gyqk;
	}

	public void setGyqk(String gyqk) {
		this.gyqk = gyqk;
	}

	public String getQlxz() {
		return qlxz;
	}

	public void setQlxz(String qlxz) {
		this.qlxz = qlxz;
	}

	public String getSyqx() {
		return syqx;
	}

	public void setSyqx(String syqx) {
		this.syqx = syqx;
	}
	
	
	private String ygzt;

	public String getYgzt() {
		return ygzt;
	}

	public void setYgzt(String ygzt) {
		this.ygzt = ygzt;
	}

	private String dyzt;

	public String getDyzt() {
		return dyzt;
	}

	public void setDyzt(String dyzt) {
		this.dyzt = dyzt;
	}

	private String cfzt;

	public String getCfzt() {
		return cfzt;
	}

	public void setCfzt(String cfzt) {
		this.cfzt = cfzt;
	}

	//流程名称
	private String prodef_name;
			
	public void setProdef_name(String prodef_name) {
		this.prodef_name = prodef_name;
	}

	public String getProdef_name() {
		return prodef_name;
	}	


	private String gyqk;
	private String qlxz;
	private String syqx;
	private String zwr;

	public SQSPB Create(String xmbh, String acinstid, CommonDao dao,
			SmProSPService smProSPService, HttpServletRequest request) {
		BDCS_XMXX xmxx = dao.get(BDCS_XMXX.class, xmbh);
		String sql = ProjectHelper.GetXMBHCondition(xmbh);
		List<BDCS_SQR> list_sql = dao.getDataList(BDCS_SQR.class, sql);

		SQSPB b = new SQSPB();
		if (xmxx != null) {
			if (xmxx.getDJLX().equals(DJLX.ZYDJ.Value)) {
				BDCDYLX bdcdylx = ProjectHelper.GetBDCDYLX(xmbh);
				if (bdcdylx.equals(BDCDYLX.H)) {
					if (xmxx.getQLLX().equals(QLLX.GYJSYDSHYQ_FWSYQ.Value)
							|| (xmxx.getQLLX()
									.equals(QLLX.JTJSYDSYQ_FWSYQ.Value))
							|| (xmxx.getQLLX().equals(QLLX.ZJDSYQ_FWSYQ.Value))) {
						return CreateFWSYQ_ZYDJ(xmbh, acinstid, dao,
								smProSPService, request);
					}
				}
			}

			if (xmxx.getDJLX().equals(DJLX.CSDJ.Value)
					&& xmxx.getQLLX().equals(QLLX.DIYQ.Value)) {
				return CreateFWSYQ_ZYDJ(xmbh, acinstid, dao, smProSPService,
						request);
			}
			// 审查人签名图片基本路径
			String signUrl = "http://" + request.getHeader("host") + "/"
					+ request.getContextPath()
					+ "/resources/workflow/signimg/%s.png";
			;
			// 获取业务类型 WUZ
			List<Wfi_ProInst> proins = dao.getDataList(Wfi_ProInst.class,
					" FILE_NUMBER='" + xmxx.getPROJECT_ID() + "'");
			String ywlx = "";
			if (proins != null && proins.size() > 0) {
				ywlx = proins.get(0).getProdef_Name();
			}
			Map<String, String> ex_attr = new HashMap<String, String>();
			// 获取审批意见 WUZ
			List<Approval> _approvals = smProSPService.GetSPYJ(acinstid);
			String _csyj = "", _csyj_scr = "", _csyj_scr_src = "", _csyj_scrq = "", _fsyj = "", _fsyj_scr = "", _fsyj_scr_src = "", _fsyj_scrq = "", _spyj = "", _spyj_scr = "", _spyj_scr_src = "", _spyj_scrq = "";
			for (Approval a : _approvals) {
				if (a.getSplx().equals("CS")) {
					if (a.getSpyjs() != null && a.getSpyjs().size() > 0) {
						_csyj = a.getSpyjs().get(0).getSpyj();
						_csyj_scr = a.getSpyjs().get(0).getSpr_Name();
						_csyj_scr_src = String.format(signUrl, a.getSpyjs()
								.get(0).getSpr_Id());
						_csyj_scrq = StringHelper.FormatByDatetime(a.getSpyjs()
								.get(0).getSpsj());
					}
				}
				if (a.getSplx().equals("FS")) {
					if (a.getSpyjs() != null && a.getSpyjs().size() > 0) {
						_fsyj = a.getSpyjs().get(0).getSpyj();
						_fsyj_scr = a.getSpyjs().get(0).getSpr_Name();
						_fsyj_scrq = StringHelper.FormatByDatetime(a.getSpyjs()
								.get(0).getSpsj());
						_fsyj_scr_src = String.format(signUrl, a.getSpyjs()
								.get(0).getSpr_Id());
					}
				}
				if (a.getSplx().equals("HD")) {
					if (a.getSpyjs() != null && a.getSpyjs().size() > 0) {
						_spyj = a.getSpyjs().get(0).getSpyj();
						_spyj_scr = a.getSpyjs().get(0).getSpr_Name();
						_spyj_scrq = StringHelper.FormatByDatetime(a.getSpyjs()
								.get(0).getSpsj());
						_spyj_scr_src = String.format(signUrl, a.getSpyjs()
								.get(0).getSpr_Id());
					}
				}
			}
			ex_attr.put("csyj", _csyj);
			ex_attr.put("csyj_scr", _csyj_scr);
			ex_attr.put("csyj_scr_src", _csyj_scr_src);
			ex_attr.put("csyj_scrq", _csyj_scrq);
			ex_attr.put("fsyj", _fsyj);
			ex_attr.put("fsyj_scr", _fsyj_scr);
			ex_attr.put("fsyj_scr_src", _fsyj_scr_src);
			ex_attr.put("fsyj_scrq", _fsyj_scrq);
			ex_attr.put("spyj", _spyj);
			ex_attr.put("spyj_scr", _spyj_scr);
			ex_attr.put("spyj_scr_src", _spyj_scr_src);
			ex_attr.put("spyj_scrq", _spyj_scrq);
			ex_attr.put("ywlx", ywlx);

			// 获得申请表附表数据,只获取第一个登记单元，和义务号
			String djdyid = "";
			// String ywh="";
			List<BDCS_DJDY_GZ> bdcs_djdy_gzs = dao.getDataList(
					BDCS_DJDY_GZ.class, " XMBH='" + xmbh + "'");
			List<Map<String, String>> houses = new ArrayList<Map<String, String>>();
			for (BDCS_DJDY_GZ bdcs_djdy_gz : bdcs_djdy_gzs) {
				if (bdcs_djdy_gzs.size() > 0) {
					djdyid = bdcs_djdy_gzs.get(0).getDJDYID();
				}
				if (BDCDYLX.H.Value.equals(bdcs_djdy_gz.getBDCDYLX())
						|| BDCDYLX.YCH.Value.equals(bdcs_djdy_gz.getBDCDYLX())) {
					BDCDYLX lx = BDCDYLX.initFrom(bdcs_djdy_gz.getBDCDYLX());
					House _house = (House) UnitTools.loadUnit(lx,
							DJDYLY.initFrom(bdcs_djdy_gz.getLY()),
							bdcs_djdy_gz.getBDCDYID());
					Map<String, String> house_map = new HashMap<String, String>();
					if (_house != null) {
						double sjcs = 0;
						if (_house.getZZC() != null && _house.getQSC() != null) {
							sjcs = (double) (_house.getZZC() - _house.getQSC() + 1);
						}
						house_map.put("H_BDCDYH", _house.getBDCDYH());
						house_map.put("H_FH", _house.getFH());
						house_map.put(
								"H_SCJZMJ",
								_house.getSCJZMJ() == null ? "" : String
										.valueOf(_house.getSCJZMJ()));
						house_map.put(
								"H_SCTNJZMJ",
								_house.getSCTNJZMJ() == null ? "" : String
										.valueOf(_house.getSCTNJZMJ()));
						house_map.put(
								"H_SCFTJZMJ",
								_house.getSCFTJZMJ() == null ? "" : String
										.valueOf(_house.getSCFTJZMJ()));

						house_map.put(
								"H_SJCS",
								String.valueOf(sjcs) == null ? "" : String
										.valueOf(sjcs));

						house_map.put(
								"H_GHYT",
								ConstHelper.getNameByValue("FWYT",
										_house.getGHYT()));
						houses.add(house_map);
					}
				}
			}
			b.setHouses(houses);

			BDCS_QL_GZ ql = null;
			BDCS_DJSZ djsz = null;
			BDCS_DJFZ djfz = null;
			BDCS_DJGD djgd = null;

			List<BDCS_QL_GZ> qls = dao.getDataList(BDCS_QL_GZ.class,
					" DJDYID='" + djdyid + "' ORDER BY DJSJ DESC ");

			if (qls != null && qls.size() > 0) {
				ql = qls.get(0);
				List<BDCS_DJSZ> djszs = dao.getDataList(BDCS_DJSZ.class,
						" YWH='" + ql.getYWH() + "' ORDER BY SZSJ DESC ");
				List<BDCS_DJFZ> djfzs = dao.getDataList(BDCS_DJFZ.class,
						" YWH='" + ql.getYWH() + "' ORDER BY FZSJ DESC ");
				List<BDCS_DJGD> djgds = dao.getDataList(BDCS_DJGD.class,
						" YWH='" + ql.getYWH() + "' ORDER BY GDSJ DESC ");
				if (djszs != null && djszs.size() > 0)
					djsz = djszs.get(0);
				if (djfzs != null && djfzs.size() > 0)
					djfz = djfzs.get(0);
				if (djgds != null && djgds.size() > 0)
					djgd = djgds.get(0);
			}
			if (ql != null) {
				if (xmxx.getDJLX().equals(DJLX.ZXDJ.Value)) { // 注销登记 登薄取注销登薄
																// diaoliwei
																// 2015-8-5
					List<BDCS_FSQL_GZ> fsqls = dao
							.getDataList(BDCS_FSQL_GZ.class,
									" QLID = '" + ql.getId() + "' ");
					if (null != fsqls && fsqls.size() > 0) {
						BDCS_FSQL_GZ fsql = fsqls.get(0);
						ex_attr.put("EX_DBR", fsql.getZXDBR());
						ex_attr.put("EX_DJSJ",
								StringHelper.FormatByDatetime(fsql.getZXSJ()));
					}
				} else {
					ex_attr.put("EX_DBR", ql.getDBR());
					ex_attr.put("EX_DJSJ",
							StringHelper.FormatByDatetime(ql.getDJSJ()));
				}
				b.djyy = ql.getDJYY();
				b.bz = ql.getFJ();
				b.fbcz = ql.getCZFS();
			}
			if (djsz != null) {
				ex_attr.put("EX_SZRY", djsz.getSZRY());
				ex_attr.put("EX_SZSJ",
						StringHelper.FormatByDatetime(djsz.getSZSJ()));
			}
			if (djfz != null) {
				ex_attr.put("EX_FZRY", djfz.getFZRY());
				ex_attr.put("EX_FZSJ",
						StringHelper.FormatByDatetime(djfz.getFZSJ()));
				ex_attr.put("EX_LZRXM", djfz.getLZRXM());
				ex_attr.put("EX_LZSJ",
						StringHelper.FormatByDatetime(djfz.getFZSJ()));// 领证时间就是发证时间
				ex_attr.put("EX_LZRZJHM", djfz.getLZRZJHM());// 领证人证件号码
			}
			if (djgd != null) {
				ex_attr.put("EX_GDZR", djgd.getGDZR());
				ex_attr.put("EX_GDSJ",
						StringHelper.FormatByDatetime(djgd.getGDSJ()));
			}
			b.setEx(ex_attr);
			// 预告登记
			if (xmxx.getDJLX().equals(DJLX.YGDJ.Value)) {
				return NoticeSegisterSPB(xmxx, dao, b);
			}
			BDCS_SQR sqr = new BDCS_SQR();
			String qlrxm = "";
			if (list_sql.size() > 0) {
				sqr = list_sql.get(0);
				qlrxm = sqr.getSQRXM();
				if (list_sql.size() > 1)
					qlrxm = qlrxm + "，" + list_sql.get(1).getSQRXM() + "等";
			}

			b.bh = xmxx.getPROJECT_ID();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
			b.rq = sdf.format(xmxx.getSLSJ());
			b.sjr = xmxx.getSLRY();
			b.dw = "1";
			b.qllx = xmxx.getQLLX();
			b.djlx = xmxx.getDJLX();
			b.qlrxm = qlrxm;
			b.zjzl = ConstHelper.getNameByValue("ZJLX", sqr.getZJLX());
			b.zjh = sqr.getZJH();
			b.dz = sqr.getTXDZ();
			b.yb = sqr.getYZBM();
			b.fddbr = sqr.getFDDBR();
			b.dh = sqr.getLXDH();
			b.dlrxm = sqr.getDLRXM();
			b.dlrsfzh = sqr.getDLRZJHM();
			b.dlrdh = sqr.getDLRLXDH();
			b.dljgmc = sqr.getDLJGMC();
			b.zsbs = "01";
			BDCDYLX bdcdylx = ProjectHelper.GetBDCDYLX(xmbh);
			if (bdcdylx.equals(BDCDYLX.SHYQZD)) {
				List<BDCS_SHYQZD_GZ> shyqzds = dao.getDataList(
						BDCS_SHYQZD_GZ.class, sql);
				if (shyqzds.size() > 0) {
					BDCS_SHYQZD_GZ zd = shyqzds.get(0);
					if (shyqzds.size() > 1) {
						b.zl = zd.getZL() + "等";
						b.bdcdyh = zd.getBDCDYH() + "等";
					} else {
						b.zl = zd.getZL();
						b.bdcdyh = zd.getBDCDYH();
					}
					b.bdclx = "建设用地";
					b.mj = String.valueOf(zd.getZDMJ());
					b.yt = zd.getYTName();
					b.yhlx = "/";
					b.gzwlx = "";
					b.lz = "";
					b.bdbe = "";
					b.qx = "";
					b.dyfw = "";
					b.xydzl = "";
					b.xydbdcdyh = "";
					// b.djyy = "";
					b.zmwj = "";
					b.zsbs = "01";
					// b.fbcz = "1";
					b.sqrqz = "";
					b.csnr = "";
					b.fsnr = "";
					b.hdnr = "";
					b.scr = "";
					b.scrq = "";
					b.scrq2 = "";
				}
			} else if (bdcdylx.equals(BDCDYLX.H)) {
				List<BDCS_H_GZ> hs = dao.getDataList(BDCS_H_GZ.class, sql);
				if (hs.size() > 0) {
					BDCS_H_GZ zd = hs.get(0);
					if (hs.size() > 1) {
						b.zl = zd.getZL() + "等";
						b.bdcdyh = zd.getBDCDYH() + "等";
					} else {
						b.zl = zd.getZL();
						b.bdcdyh = zd.getBDCDYH();
					}
					b.bdclx = "房屋";
					b.mj = String.valueOf(zd.getSCJZMJ());
					b.yt = ConstHelper.getNameByValue("YT", zd.getFWYT1());
					b.yhlx = "/";
					b.gzwlx = "";
					b.lz = "";
					b.bdbe = "";
					b.qx = "";
					b.dyfw = "";
					b.xydzl = "";
					b.xydbdcdyh = "";
					// b.djyy = "";
					b.zmwj = "";
					b.zsbs = "01";
					// b.fbcz = "1";
					b.sqrqz = "";
					b.csnr = "";
					b.fsnr = "";
					b.hdnr = "";
					b.scr = "";
					b.scrq = "";
					b.scrq2 = "";
				}
			} else if (bdcdylx.equals(BDCDYLX.YCH)) {
				List<BDCS_H_XZY> hs = dao.getDataList(BDCS_H_XZY.class, sql);
				if (hs.size() > 0) {
					BDCS_H_XZY zd = hs.get(0);
					if (hs.size() > 1) {
						b.zl = zd.getZL() + "等";
						b.bdcdyh = zd.getBDCDYH() + "等";
					} else {
						b.zl = zd.getZL();
						b.bdcdyh = zd.getBDCDYH();
					}
					b.bdclx = "房屋";
					b.mj = String.valueOf(zd.getSCJZMJ());
					b.yt = ConstHelper.getNameByValue("YT", zd.getFWYT1());
					b.yhlx = "/";
					b.gzwlx = "";
					b.lz = "";
					b.bdbe = "";
					b.qx = "";
					b.dyfw = "";
					b.xydzl = "";
					b.xydbdcdyh = "";
					// b.djyy = "";
					b.zmwj = "";
					b.zsbs = "01";
					// b.fbcz = "1";
					b.sqrqz = "";
					b.csnr = "";
					b.fsnr = "";
					b.hdnr = "";
					b.scr = "";
					b.scrq = "";
					b.scrq2 = "";
				}
			} else if (bdcdylx.equals(BDCDYLX.ZRZ)) {
				List<BDCS_ZRZ_GZ> zrzs = dao
						.getDataList(BDCS_ZRZ_GZ.class, sql);
				if (zrzs.size() > 0) {
					BDCS_ZRZ_GZ zrz = zrzs.get(0);
					if (zrzs.size() > 1) {
						b.zl = zrz.getZL() + "等";
						b.bdcdyh = zrz.getBDCDYH() + "等";
					} else {
						b.zl = zrz.getZL();
						b.bdcdyh = zrz.getBDCDYH();
					}
					b.bdclx = "房屋";
					b.mj = String.valueOf(zrz.getSCJZMJ());
					b.yt = zrz.getGHYTName();
					b.yhlx = "/";
					b.gzwlx = "";
					b.lz = "";
					b.bdbe = "";
					b.qx = "";
					b.dyfw = "";
					b.xydzl = "";
					b.xydbdcdyh = "";
					// b.djyy = "";
					b.zmwj = "";
					b.zsbs = "01";
					// b.fbcz = "1";
					b.sqrqz = "";
					b.csnr = "";
					b.fsnr = "";
					b.hdnr = "";
					b.scr = "";
					b.scrq = "";
					b.scrq2 = "";
				}
			}
		}
		return b;
	}

	/**
	 * 转移登记申请审批表
	 * 
	 * @作者：俞学斌
	 * @创建时间 2015年7月30日 下午11:54:08
	 * @param xmbh
	 * @param acinstid
	 * @param dao
	 * @param smProSPService
	 * @return
	 */
	public SQSPB CreateFWSYQ_ZYDJ(String xmbh, String acinstid, CommonDao dao,
			SmProSPService smProSPService, HttpServletRequest request) {
		BDCS_XMXX xmxx = dao.get(BDCS_XMXX.class, xmbh);
		String strXmbhFilter = ProjectHelper.GetXMBHCondition(xmxx.getId());
		StringBuilder builderSQR1 = new StringBuilder();
		builderSQR1.append(strXmbhFilter);
		builderSQR1.append(" and sqrlb='1'");
		StringBuilder builderSQR2 = new StringBuilder();
		builderSQR2.append(strXmbhFilter);
		builderSQR2.append(" and sqrlb='2'");
		StringBuilder builderSQR3 = new StringBuilder();
		builderSQR3.append(strXmbhFilter);
		builderSQR3.append(" and sqrlb='5'");
		List<BDCS_SQR> list = dao.getDataList(BDCS_SQR.class,
				builderSQR1.toString());
		List<BDCS_SQR> list2 = dao.getDataList(BDCS_SQR.class,
				builderSQR2.toString());
		List<BDCS_SQR> list3 = dao.getDataList(BDCS_SQR.class,
				builderSQR3.toString());
		SQSPB b = new SQSPB();
		if (xmxx != null) {
			// 审查人签名图片基本路径
			String signUrl = "http://" + request.getHeader("host") + "/"
					+ request.getContextPath()
					+ "/resources/workflow/signimg/%s.png";
			;
			// 获取业务类型 WUZ
			List<Wfi_ProInst> proins = dao.getDataList(Wfi_ProInst.class,
					" FILE_NUMBER='" + xmxx.getPROJECT_ID() + "'");
			String ywlx = "";
			if (proins != null && proins.size() > 0) {
				ywlx = proins.get(0).getProdef_Name();
			}
			Map<String, String> ex_attr = new HashMap<String, String>();
			// 获取审批意见 WUZ
			List<Approval> _approvals = smProSPService.GetSPYJ(acinstid);
			String _csyj = "", _csyj_scr = "", _csyj_scr_src = "", _csyj_scrq = "", _fsyj = "", _fsyj_scr = "", _fsyj_scr_src = "", _fsyj_scrq = "", _spyj = "", _spyj_scr = "", _spyj_scr_src = "", _spyj_scrq = "";
			for (Approval a : _approvals) {
				if (a.getSplx().equals("CS")) {
					if (a.getSpyjs() != null && a.getSpyjs().size() > 0) {
						_csyj = a.getSpyjs().get(0).getSpyj();
						_csyj_scr = a.getSpyjs().get(0).getSpr_Name();
						_csyj_scr_src = String.format(signUrl, a.getSpyjs()
								.get(0).getSpr_Id());
						_csyj_scrq = StringHelper.FormatByDatetime(a.getSpyjs()
								.get(0).getSpsj());
					}
				}
				if (a.getSplx().equals("FS")) {
					if (a.getSpyjs() != null && a.getSpyjs().size() > 0) {
						_fsyj = a.getSpyjs().get(0).getSpyj();
						_fsyj_scr = a.getSpyjs().get(0).getSpr_Name();
						_fsyj_scrq = StringHelper.FormatByDatetime(a.getSpyjs()
								.get(0).getSpsj());
						_fsyj_scr_src = String.format(signUrl, a.getSpyjs()
								.get(0).getSpr_Id());
					}
				}
				if (a.getSplx().equals("HD")) {
					if (a.getSpyjs() != null && a.getSpyjs().size() > 0) {
						_spyj = a.getSpyjs().get(0).getSpyj();
						_spyj_scr = a.getSpyjs().get(0).getSpr_Name();
						_spyj_scrq = StringHelper.FormatByDatetime(a.getSpyjs()
								.get(0).getSpsj());
						_spyj_scr_src = String.format(signUrl, a.getSpyjs()
								.get(0).getSpr_Id());
					}
				}
			}
			ex_attr.put("csyj", _csyj);
			ex_attr.put("csyj_scr", _csyj_scr);
			ex_attr.put("csyj_scr_src", _csyj_scr_src);
			ex_attr.put("csyj_scrq", _csyj_scrq);
			ex_attr.put("fsyj", _fsyj);
			ex_attr.put("fsyj_scr", _fsyj_scr);
			ex_attr.put("fsyj_scr_src", _fsyj_scr_src);
			ex_attr.put("fsyj_scrq", _fsyj_scrq);
			ex_attr.put("spyj", _spyj);
			ex_attr.put("spyj_scr", _spyj_scr);
			ex_attr.put("spyj_scr_src", _spyj_scr_src);
			ex_attr.put("spyj_scrq", _spyj_scrq);
			ex_attr.put("ywlx", ywlx);

			// 获得申请表附表数据,只获取第一个登记单元，和义务号
			String djdyid = "";
			// String ywh="";
			List<BDCS_DJDY_GZ> bdcs_djdy_gzs = dao.getDataList(
					BDCS_DJDY_GZ.class, " XMBH='" + xmbh + "'");
			List<Map<String, String>> houses = new ArrayList<Map<String, String>>();
			for (BDCS_DJDY_GZ bdcs_djdy_gz : bdcs_djdy_gzs) {
				if (bdcs_djdy_gzs.size() > 0) {
					djdyid = bdcs_djdy_gzs.get(0).getDJDYID();
				}
				if (BDCDYLX.H.Value.equals(bdcs_djdy_gz.getBDCDYLX())) {
					House _house = (House) UnitTools.loadUnit(BDCDYLX.H,
							DJDYLY.initFrom(bdcs_djdy_gz.getLY()),
							bdcs_djdy_gz.getBDCDYID());
					Map<String, String> house_map = new HashMap<String, String>();
					if (_house != null) {
						double sjcs = 0;
						if (_house.getZZC() != null && _house.getQSC() != null) {
							sjcs = (double) (_house.getZZC() - _house.getQSC() + 1);
						}
						house_map.put("H_BDCDYH", _house.getBDCDYH());
						house_map.put("H_FH", _house.getFH());
						house_map.put("H_SCJZMJ",
								String.valueOf(_house.getSCJZMJ()));
						house_map.put("H_SCTNJZMJ",
								String.valueOf(_house.getSCTNJZMJ()));
						house_map.put("H_SCFTJZMJ",
								String.valueOf(_house.getSCFTJZMJ()));

						house_map.put("H_SJCS", String.valueOf(sjcs));

						house_map.put(
								"H_GHYT",
								ConstHelper.getNameByValue("FWYT",
										_house.getGHYT()));
						houses.add(house_map);
					}
				}
			}
			b.setHouses(houses);

			BDCS_QL_GZ ql = null;
			BDCS_DJSZ djsz = null;
			BDCS_DJFZ djfz = null;
			BDCS_DJGD djgd = null;

			List<BDCS_QL_GZ> qls = dao.getDataList(BDCS_QL_GZ.class,
					" DJDYID='" + djdyid + "' ORDER BY DJSJ DESC ");

			if (qls != null && qls.size() > 0) {
				ql = qls.get(0);
				List<BDCS_DJSZ> djszs = dao.getDataList(BDCS_DJSZ.class,
						" YWH='" + ql.getYWH() + "' ORDER BY SZSJ DESC ");
				List<BDCS_DJFZ> djfzs = dao.getDataList(BDCS_DJFZ.class,
						" YWH='" + ql.getYWH() + "' ORDER BY FZSJ DESC ");
				List<BDCS_DJGD> djgds = dao.getDataList(BDCS_DJGD.class,
						" YWH='" + ql.getYWH() + "' ORDER BY GDSJ DESC ");
				if (djszs != null && djszs.size() > 0)
					djsz = djszs.get(0);
				if (djfzs != null && djfzs.size() > 0)
					djfz = djfzs.get(0);
				if (djgds != null && djgds.size() > 0)
					djgd = djgds.get(0);
			}
			if (ql != null) {
				ex_attr.put("EX_DBR", ql.getDBR());
				ex_attr.put("EX_DJSJ",
						StringHelper.FormatByDatetime(ql.getDJSJ()));
				b.djyy = ql.getDJYY();
				b.bz = ql.getFJ();
				b.fbcz = ql.getCZFS();
			}
			if (djsz != null) {
				ex_attr.put("EX_SZRY", djsz.getSZRY());
				ex_attr.put("EX_SZSJ",
						StringHelper.FormatByDatetime(djsz.getSZSJ()));
			}
			if (djfz != null) {
				ex_attr.put("EX_FZRY", djfz.getFZRY());
				ex_attr.put("EX_FZSJ",
						StringHelper.FormatByDatetime(djfz.getFZSJ()));
				ex_attr.put("EX_LZRXM", djfz.getLZRXM());
				ex_attr.put("EX_LZSJ",
						StringHelper.FormatByDatetime(djfz.getFZSJ()));// 领证时间就是发证时间
				ex_attr.put("EX_LZRZJHM", djfz.getLZRZJHM());// 领证人证件号码
			}
			if (djgd != null) {
				ex_attr.put("EX_GDZR", djgd.getGDZR());
				ex_attr.put("EX_GDSJ",
						StringHelper.FormatByDatetime(djgd.getGDSJ()));
			}
			b.setEx(ex_attr);
			String qlrxm = "";
			String qlrxm1 = "";
			String qlrxm2 = "";
			BDCS_SQR sqr = new BDCS_SQR();
			BDCS_SQR sqr2 = new BDCS_SQR();
			BDCS_SQR sqr3 = new BDCS_SQR();

			if (list.size() > 0) {
				sqr = list.get(0);
				qlrxm = sqr.getSQRXM();
				if (list.size() > 1)
					qlrxm = qlrxm + "，" + list.get(1).getSQRXM() + "等";
			}
			if (list2.size() > 0) {
				sqr2 = list2.get(0);
				qlrxm1 = sqr2.getSQRXM();
				if (list2.size() > 1)
					qlrxm1 = qlrxm1 + "，" + list2.get(1).getSQRXM() + "等";
			}
			if(list3.size()>0){
				sqr3 = list3.get(0);
				qlrxm2 = sqr3.getSQRXM();
				if (list3.size() > 1)
					qlrxm2 = qlrxm2 + "，" + list3.get(1).getSQRXM() + "等";
			}
			b.bh = xmxx.getPROJECT_ID();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
			b.rq = sdf.format(new Date());
			b.sjr = xmxx.getSLRY();
			b.dw = "1";
			b.qllx = xmxx.getQLLX();
			b.djlx = xmxx.getDJLX();
			b.qlrxm = qlrxm;
			b.zjzl = ConstHelper.getNameByValue("ZJLX", sqr.getZJLX());
			b.zjh = sqr.getZJH();
			b.dz = sqr.getTXDZ();
			b.yb = sqr.getYZBM();
			b.fddbr = sqr.getFDDBR();
			b.dh = sqr.getLXDH();
			b.dlrxm = sqr.getDLRXM();
			b.dlrsfzh = sqr.getDLRZJHM();
			b.dlrdh = sqr.getDLRLXDH();
			b.dljgmc = sqr.getDLJGMC();

			b.qlrxm1 = qlrxm1;
			b.zjzl1 = ConstHelper.getNameByValue("ZJLX", sqr2.getZJLX());
			b.zjh1 = sqr2.getZJH();
			b.dz1 = sqr2.getTXDZ();
			b.yb1 = sqr2.getYZBM();
			b.fddbr1 = sqr2.getFDDBR();
			b.dh1 = sqr2.getLXDH();
			b.dlrxm1 = sqr2.getDLRXM();
			b.dlrsfzh1 = sqr2.getDLRZJHM();
			b.dlrdh1 = sqr2.getDLRLXDH();
			b.dljgmc1 = sqr2.getDLJGMC();
			//利害关系人信息 liangq
			b.qlrxm2 = qlrxm2;
			b.zjzl2 = ConstHelper.getNameByValue("ZJLX", sqr3.getZJLX());
			b.zjh2 = sqr3.getZJH();
			b.dz2 = sqr3.getTXDZ();
			b.yb2 = sqr3.getYZBM();
			b.fddbr2 = sqr3.getFDDBR();
			b.dh2 = sqr3.getLXDH();
			b.dlrxm2 = sqr3.getDLRXM();
			b.dlrsfzh2 = sqr3.getDLRZJHM();
			b.dlrdh2 = sqr3.getDLRLXDH();
			b.dljgmc2 = sqr3.getDLJGMC();
			
			
			b.zsbs = "01";

			List<BDCS_DJDY_GZ> djdys = dao.getDataList(BDCS_DJDY_GZ.class,
					strXmbhFilter);
			if (djdys != null && djdys.size() > 0) {
				StringBuilder builderH = new StringBuilder();
				builderH.append("BDCDYID IN ('");
				for (int idjdy = 0; idjdy < djdys.size(); idjdy++) {
					builderH.append(djdys.get(idjdy).getBDCDYID());
					if (idjdy == djdys.size() - 1) {
						builderH.append("')");
					} else {
						builderH.append("','");
					}
				}
				List<BDCS_H_XZ> hs = dao.getDataList(BDCS_H_XZ.class,
						builderH.toString());
				if (hs.size() > 0) {
					BDCS_H_XZ h = hs.get(0);
					if (hs.size() > 1) {
						b.zl = h.getZL() + "等";
						b.bdcdyh = h.getBDCDYH() + "等";
					} else {
						b.zl = h.getZL();
						b.bdcdyh = h.getBDCDYH();
					}
					b.bdclx = "房屋";
					b.mj = String.valueOf(h.getSCJZMJ());
					b.yt = ConstHelper.getNameByValue("YT", h.getFWYT1());
					b.yhlx = "/";
					b.gzwlx = "";
					b.lz = "";
					b.bdbe = "";
					b.qx = "";
					b.dyfw = "";
					b.xydzl = "";
					b.xydbdcdyh = "";
					// b.djyy = "";
					b.zmwj = "";
					b.zsbs = "01";
					// b.fbcz = "1";
					b.sqrqz = "";
					b.csnr = "";
					b.fsnr = "";
					b.hdnr = "";
					b.scr = "";
					b.scrq = "";
					b.scrq2 = "";
					StringBuilder builderYQL = new StringBuilder();
					builderYQL.append("DJDYID='");
					builderYQL.append(djdys.get(0).getDJDYID());
					builderYQL.append("' AND QLLX='");
					builderYQL.append(xmxx.getQLLX());
					builderYQL.append("'");
					List<BDCS_QL_XZ> yqls = dao.getDataList(BDCS_QL_XZ.class,
							builderYQL.toString());
					if (yqls != null && yqls.size() > 0) {
						b.ybdcqzsh = yqls.get(0).getBDCQZH();
					}
				}
			}
		}
		return b;
	}

	/**
	 * 预告登记审批表
	 * 
	 * @author DreamLi
	 * @time 2015-6-23 20:17
	 * 
	 */
	public static SQSPB NoticeSegisterSPB(BDCS_XMXX xmxx, CommonDao dao, SQSPB b) {
		StringBuilder sql = new StringBuilder();
		sql.append(ProjectHelper.GetXMBHCondition(xmxx.getId()));
		sql.append(" and sqrlb=");
		List<BDCS_SQR> list = dao.getDataList(BDCS_SQR.class, sql.toString()
				+ "1");
		List<BDCS_SQR> list2 = dao.getDataList(BDCS_SQR.class, sql.toString()
				+ "2");
		List<BDCS_SQR> list3 = dao.getDataList(BDCS_SQR.class, sql.toString()
				+ "5");

		if (xmxx != null) {
			String qlrxm = "";
			String qlrxm1 = "";
			String qlrxm2 = "";
			BDCS_SQR sqr = new BDCS_SQR();
			BDCS_SQR sqr2 = new BDCS_SQR();
			BDCS_SQR sqr3 = new BDCS_SQR();

			if (list.size() > 0) {
				sqr = list.get(0);
				qlrxm = sqr.getSQRXM();
				if (list.size() > 1)
					qlrxm = qlrxm + "，" + list.get(1).getSQRXM() + "等";
			}
			if (list2.size() > 0) {
				sqr2 = list2.get(0);
				qlrxm1 = sqr2.getSQRXM();
				if (list2.size() > 1)
					qlrxm1 = qlrxm1 + "，" + list2.get(1).getSQRXM() + "等";
			}
			if(list3.size()>0){
				sqr3 = list3.get(0);
				qlrxm2 = sqr3.getSQRXM();
				if (list3.size() > 1)
					qlrxm2 = qlrxm2 + "，" + list3.get(1).getSQRXM() + "等";
			}
			b.bh = xmxx.getPROJECT_ID();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
			b.rq = sdf.format(new Date());
			b.sjr = xmxx.getSLRY();
			b.dw = "1";
			b.qllx = xmxx.getQLLX();
			b.djlx = xmxx.getDJLX();
			b.qlrxm = qlrxm;
			b.zjzl = ConstHelper.getNameByValue("ZJLX", sqr.getZJLX());
			b.zjh = sqr.getZJH();
			b.dz = sqr.getTXDZ();
			b.yb = sqr.getYZBM();
			b.fddbr = sqr.getFDDBR();
			b.dh = sqr.getLXDH();
			b.dlrxm = sqr.getDLRXM();
			b.dlrsfzh = sqr.getDLRZJHM();
			b.dlrdh = sqr.getDLRLXDH();
			b.dljgmc = sqr.getDLJGMC();

			b.qlrxm1 = qlrxm1;
			b.zjzl1 = ConstHelper.getNameByValue("ZJLX", sqr2.getZJLX());
			b.zjh1 = sqr2.getZJH();
			b.dz1 = sqr2.getTXDZ();
			b.yb1 = sqr2.getYZBM();
			b.fddbr1 = sqr2.getFDDBR();
			b.dh1 = sqr2.getLXDH();
			b.dlrxm1 = sqr2.getDLRXM();
			b.dlrsfzh1 = sqr2.getDLRZJHM();
			b.dlrdh1 = sqr2.getDLRLXDH();
			b.dljgmc1 = sqr2.getDLJGMC();
			//利害关系人信息 liangq
			b.qlrxm2 = qlrxm2;
			b.zjzl2 = ConstHelper.getNameByValue("ZJLX", sqr3.getZJLX());
			b.zjh2 = sqr3.getZJH();
			b.dz2 = sqr3.getTXDZ();
			b.yb2 = sqr3.getYZBM();
			b.fddbr2 = sqr3.getFDDBR();
			b.dh2 = sqr3.getLXDH();
			b.dlrxm2 = sqr3.getDLRXM();
			b.dlrsfzh2 = sqr3.getDLRZJHM();
			b.dlrdh2 = sqr3.getDLRLXDH();
			b.dljgmc2 = sqr3.getDLJGMC();
			

			// List<BDCS_FSQL_GZ> fsql_list=dao.getDataList(BDCS_FSQL_GZ.class,
			// "xmbh='"+xmxx.getId()+"'");

			BDCDYLX bdcdylx = ProjectHelper.GetBDCDYLX(xmxx.getId());
			StringBuilder hql = new StringBuilder();
			hql.append(" id in (Select a.BDCDYID from BDCS_DJDY_GZ as a  where a.XMBH='");
			hql.append(xmxx.getId());
			hql.append("')");
			if (bdcdylx.equals(BDCDYLX.SHYQZD)) {
				List<BDCS_SHYQZD_XZ> shyqzds = dao.getDataList(
						BDCS_SHYQZD_XZ.class, hql.toString());
				if (shyqzds.size() > 0) {
					BDCS_SHYQZD_XZ zd = shyqzds.get(0);
					if (shyqzds.size() > 1) {
						b.zl = zd.getZL() + "等";
						b.bdcdyh = zd.getBDCDYH() + "等";
					} else {
						b.zl = zd.getZL();
						b.bdcdyh = zd.getBDCDYH();
					}
					b.bdclx = "建设用地";
					b.mj = String.valueOf(zd.getZDMJ());
					b.yt = zd.getYTName();
					b.yhlx = "/";
					b.gzwlx = "";
					b.lz = "";
					b.bdbe = "";
					b.qx = "";
					b.dyfw = "";
					b.xydzl = "";
					b.xydbdcdyh = "";
					// b.djyy = "";
					b.zmwj = "";
					b.zsbs = "01";
					// b.fbcz = "1";
					b.sqrqz = "";
					b.csnr = "";
					b.fsnr = "";
					b.hdnr = "";
					b.scr = "";
					b.scrq = "";
					b.scrq2 = "";
				}
			} else if (bdcdylx.equals(BDCDYLX.H)) {
				List<BDCS_H_XZ> hs = dao.getDataList(BDCS_H_XZ.class,
						hql.toString());
				if (hs.size() > 0) {
					BDCS_H_XZ zd = hs.get(0);
					if (hs.size() > 1) {
						b.zl = zd.getZL() + "等";
						b.bdcdyh = zd.getBDCDYH() + "等";
					} else {
						b.zl = zd.getZL();
						b.bdcdyh = zd.getBDCDYH();
					}
					b.bdclx = "房屋";
					b.mj = String.valueOf(zd.getYCJZMJ());
					b.yt = ConstHelper.getNameByValue("YT", zd.getFWYT1());
					b.yhlx = "/";
					b.gzwlx = "";
					b.lz = "";
					b.bdbe = "";
					b.qx = "";
					b.dyfw = "";
					b.xydzl = "";
					b.xydbdcdyh = "";
					// b.djyy = "";
					b.zmwj = "";
					b.zsbs = "01";
					// b.fbcz = "1";
					b.sqrqz = "";
					b.csnr = "";
					b.fsnr = "";
					b.hdnr = "";
					b.scr = "";
					b.scrq = "";
					b.scrq2 = "";

					if (xmxx.getQLLX().equals("1")) {
						b.qllx = QLLX.GYJSYDSHYQ_FWSYQ.Value;
					} else if (xmxx.getQLLX().equals("3")
							|| xmxx.getQLLX().equals("4")) {
						b.qllx = "121";// 抵押权，页面是121 不懂怎么设置的
					}
				}
			} else if (bdcdylx.equals(BDCDYLX.YCH)) {
				List<BDCS_H_XZY> hs = dao.getDataList(BDCS_H_XZY.class,
						hql.toString());
				if (hs.size() > 0) {
					BDCS_H_XZY xzy = hs.get(0);
					if (hs.size() > 1) {
						b.zl = xzy.getZL() + "等";
						b.bdcdyh = xzy.getBDCDYH() + "等";
					} else {
						b.zl = xzy.getZL();
						b.bdcdyh = xzy.getBDCDYH();
					}
					b.bdclx = "房屋";
					b.mj = String.valueOf(xzy.getYCJZMJ());
					b.yt = ConstHelper.getNameByValue("YT", xzy.getFWYT1());
					b.yhlx = "/";
					b.gzwlx = "";
					b.lz = "";
					b.bdbe = "";
					b.qx = "";
					b.dyfw = "";
					b.xydzl = "";
					b.xydbdcdyh = "";
					// b.djyy = "";
					b.zmwj = "";
					b.zsbs = "01";
					// b.fbcz = "1";
					b.sqrqz = "";
					b.csnr = "";
					b.fsnr = "";
					b.hdnr = "";
					b.scr = "";
					b.scrq = "";
					b.scrq2 = "";
					b.qllx = xmxx.getQLLX();
				}
			}
		}
		return b;
	}

	public List<Map<String, String>> getHouses() {
		return houses;
	}

	public void setHouses(List<Map<String, String>> houses) {
		this.houses = houses;
	}

	public Map<String, String> getEx() {
		return ex;
	}

	public void setEx(Map<String, String> ex) {
		this.ex = ex;
	}

	public String getBh() {
		return bh;
	}

	public void setBh(String bh) {
		this.bh = bh;
	}

	public String getRq() {
		return rq;
	}

	public void setRq(String rq) {
		this.rq = rq;
	}

	public String getSjr() {
		return sjr;
	}

	public void setSjr(String sjr) {
		this.sjr = sjr;
	}

	public String getDw() {
		return dw;
	}

	public void setDw(String dw) {
		this.dw = dw;
	}

	public String getQllx() {
		return qllx;
	}

	public void setQllx(String qllx) {
		this.qllx = qllx;
	}

	public String getDjlx() {
		return djlx;
	}

	public void setDjlx(String djlx) {
		this.djlx = djlx;
	}

	public String getQlrxm() {
		return qlrxm;
	}

	public void setQlrxm(String qlrxm) {
		this.qlrxm = qlrxm;
	}

	public String getZjzl() {
		return zjzl;
	}

	public void setZjzl(String zjzl) {
		this.zjzl = zjzl;
	}

	public String getZjh() {
		return zjh;
	}

	public void setZjh(String zjh) {
		this.zjh = zjh;
	}

	public String getDz() {
		return dz;
	}

	public void setDz(String dz) {
		this.dz = dz;
	}

	public String getYb() {
		return yb;
	}

	public void setYb(String yb) {
		this.yb = yb;
	}

	public String getFddbr() {
		return fddbr;
	}

	public void setFddbr(String fddbr) {
		this.fddbr = fddbr;
	}

	public String getDh() {
		return dh;
	}

	public void setDh(String dh) {
		this.dh = dh;
	}

	public String getDlrxm() {
		return dlrxm;
	}

	public void setDlrxm(String dlrxm) {
		this.dlrxm = dlrxm;
	}

	public String getDlrdh() {
		return dlrdh;
	}

	public void setDlrdh(String dlrdh) {
		this.dlrdh = dlrdh;
	}

	public String getDljgmc() {
		return dljgmc;
	}

	public void setDljgmc(String dljgmc) {
		this.dljgmc = dljgmc;
	}

	public String getQlrxm1() {
		return qlrxm1;
	}

	public void setQlrxm1(String qlrxm1) {
		this.qlrxm1 = qlrxm1;
	}

	public String getZjzl1() {
		return zjzl1;
	}

	public void setZjzl1(String zjzl1) {
		this.zjzl1 = zjzl1;
	}

	public String getZjh1() {
		return zjh1;
	}

	public void setZjh1(String zjh1) {
		this.zjh1 = zjh1;
	}

	public String getDz1() {
		return dz1;
	}

	public void setDz1(String dz1) {
		this.dz1 = dz1;
	}

	public String getYb1() {
		return yb1;
	}

	public void setYb1(String yb1) {
		this.yb1 = yb1;
	}

	public String getFddbr1() {
		return fddbr1;
	}

	public void setFddbr1(String fddbr1) {
		this.fddbr1 = fddbr1;
	}

	public String getDh1() {
		return dh1;
	}

	public void setDh1(String dh1) {
		this.dh1 = dh1;
	}

	public String getDlrxm1() {
		return dlrxm1;
	}

	public void setDlrxm1(String dlrxm1) {
		this.dlrxm1 = dlrxm1;
	}

	public String getDlrdh1() {
		return dlrdh1;
	}

	public void setDlrdh1(String dlrdh1) {
		this.dlrdh1 = dlrdh1;
	}

	public String getDljgmc1() {
		return dljgmc1;
	}

	public void setDljgmc1(String dljgmc1) {
		this.dljgmc1 = dljgmc1;
	}

	public String getZl() {
		return zl;
	}

	public void setZl(String zl) {
		this.zl = zl;
	}

	public String getBdcdyh() {
		return bdcdyh;
	}

	public void setBdcdyh(String bdcdyh) {
		this.bdcdyh = bdcdyh;
	}

	public String getBdclx() {
		return bdclx;
	}

	public void setBdclx(String bdclx) {
		this.bdclx = bdclx;
	}

	public String getBdclx_tdfw() {
		return bdclx_tdfw;
	}

	public void setBdclx_tdfw(String bdclx_tdfw) {
		this.bdclx_tdfw = bdclx_tdfw;
	}

	public String getMj() {
		return mj;
	}

	public void setMj(String mj) {
		this.mj = mj;
	}

	public String getTdftmj_fwjzmj() {
		return tdftmj_fwjzmj;
	}

	public void setTdftmj_fwjzmj(String tdftmj_fwjzmj) {
		this.tdftmj_fwjzmj = tdftmj_fwjzmj;
	}

	public String getYt() {
		return yt;
	}

	public void setYt(String yt) {
		this.yt = yt;
	}

	public String getYbdcqzsh() {
		return ybdcqzsh;
	}

	public void setYbdcqzsh(String ybdcqzsh) {
		this.ybdcqzsh = ybdcqzsh;
	}

	public String getYhlx() {
		return yhlx;
	}

	public void setYhlx(String yhlx) {
		this.yhlx = yhlx;
	}

	public String getGzwlx() {
		return gzwlx;
	}

	public void setGzwlx(String gzwlx) {
		this.gzwlx = gzwlx;
	}

	public String getLz() {
		return lz;
	}

	public void setLz(String lz) {
		this.lz = lz;
	}

	public String getBdbe() {
		return bdbe;
	}

	public void setBdbe(String bdbe) {
		this.bdbe = bdbe;
	}

	public String getQx() {
		return qx;
	}

	public void setQx(String qx) {
		this.qx = qx;
	}

	public String getDyfw() {
		return dyfw;
	}

	public void setDyfw(String dyfw) {
		this.dyfw = dyfw;
	}

	public String getXydzl() {
		return xydzl;
	}

	public void setXydzl(String xydzl) {
		this.xydzl = xydzl;
	}

	public String getXydbdcdyh() {
		return xydbdcdyh;
	}

	public void setXydbdcdyh(String xydbdcdyh) {
		this.xydbdcdyh = xydbdcdyh;
	}

	public String getDjyy() {
		return djyy;
	}

	public void setDjyy(String djyy) {
		this.djyy = djyy;
	}

	public String getZmwj() {
		return zmwj;
	}

	public void setZmwj(String zmwj) {
		this.zmwj = zmwj;
	}

	public String getZsbs() {
		return zsbs;
	}

	public void setZsbs(String zsbs) {
		this.zsbs = zsbs;
	}

	public String getFbcz() {
		return fbcz;
	}

	public void setFbcz(String fbcz) {
		this.fbcz = fbcz;
	}

	public String getBz() {
		return bz;
	}

	public void setBz(String bz) {
		this.bz = bz;
	}

	public String getSqrqz() {
		return sqrqz;
	}

	public void setSqrqz(String sqrqz) {
		this.sqrqz = sqrqz;
	}

	public String getSqrqz2() {
		return sqrqz2;
	}

	public void setSqrqz2(String sqrqz2) {
		this.sqrqz2 = sqrqz2;
	}

	public String getDlrqz() {
		return dlrqz;
	}

	public void setDlrqz(String dlrqz) {
		this.dlrqz = dlrqz;
	}

	public String getDlrqz2() {
		return dlrqz2;
	}

	public void setDlrqz2(String dlrqz2) {
		this.dlrqz2 = dlrqz2;
	}

	public String getQzrq() {
		return qzrq;
	}

	public void setQzrq(String qzrq) {
		this.qzrq = qzrq;
	}

	public String getQzrq2() {
		return qzrq2;
	}

	public void setQzrq2(String qzrq2) {
		this.qzrq2 = qzrq2;
	}

	public String getCsnr() {
		return csnr;
	}

	public void setCsnr(String csnr) {
		this.csnr = csnr;
	}

	public String getFsnr() {
		return fsnr;
	}

	public void setFsnr(String fsnr) {
		this.fsnr = fsnr;
	}

	public String getHdnr() {
		return hdnr;
	}

	public void setHdnr(String hdnr) {
		this.hdnr = hdnr;
	}

	public String getScr() {
		return scr;
	}

	public void setScr(String scr) {
		this.scr = scr;
	}

	public String getScr2() {
		return scr2;
	}

	public void setScr2(String scr2) {
		this.scr2 = scr2;
	}

	public String getFzr() {
		return fzr;
	}

	public void setFzr(String fzr) {
		this.fzr = fzr;
	}

	public String getScrq() {
		return scrq;
	}

	public void setScrq(String scrq) {
		this.scrq = scrq;
	}

	public String getScrq2() {
		return scrq2;
	}

	public void setScrq2(String scrq2) {
		this.scrq2 = scrq2;
	}

	public String getFzrrq() {
		return fzrrq;
	}

	public void setFzrrq(String fzrrq) {
		this.fzrrq = fzrrq;
	}

	public String getBz2() {
		return bz2;
	}

	public void setBz2(String bz2) {
		this.bz2 = bz2;
	}

	public String getFddbrdh() {
		return fddbrdh;
	}

	public void setFddbrdh(String fddbrdh) {
		this.fddbrdh = fddbrdh;
	}

	public String getFddbrzjhm() {
		return fddbrzjhm;
	}

	public void setFddbrzjhm(String fddbrzjhm) {
		this.fddbrzjhm = fddbrzjhm;
	}

	public String getFddbrdh1() {
		return fddbrdh1;
	}

	public void setFddbrdh1(String fddbrdh1) {
		this.fddbrdh1 = fddbrdh1;
	}

	public String getFddbrzjhm1() {
		return fddbrzjhm1;
	}

	public void setFddbrzjhm1(String fddbrzjhm1) {
		this.fddbrzjhm1 = fddbrzjhm1;
	}

	public String getQllxmc() {
		return qllxmc;
	}

	public void setQllxmc(String qllxmc) {
		this.qllxmc = qllxmc;
	}

	public String getDjlxmc() {
		return djlxmc;
	}

	public void setDjlxmc(String djlxmc) {
		this.djlxmc = djlxmc;
	}

	public List<Map> getSqrs() {
		return sqrs;
	}

	public void setSqrs(List<Map> sqrs) {
		this.sqrs = sqrs;
	}
	public void setFwxz(String fwxz) {
		this.fwxz = fwxz;
	}

	public String getZWR() {
		// TODO Auto-generated method stub
		return zwr;
	}

	public void setZWR(String zwr) {
		this.zwr = zwr;
	}

	private String qlrxm_gyfs;
	private String ywrxm_gyfs;

	public String getQlrxm_gyfs() {
		return qlrxm_gyfs;
	}

	public void setQlrxm_gyfs(String qlrxm_gyfs) {
		this.qlrxm_gyfs = qlrxm_gyfs;
	}

	public String getYwrxm_gyfs() {
		return ywrxm_gyfs;
	}

	public void setYwrxm_gyfs(String ywrxm_gyfs) {
		this.ywrxm_gyfs = ywrxm_gyfs;
	}

	private String qlrxm_gyfs_bl;
	
	public String getQlrxm_gyfs_bl() {
		return qlrxm_gyfs_bl;
	}

	public void setQlrxm_gyfs_bl(String qlrxm_gyfs_bl) {
		this.qlrxm_gyfs_bl = qlrxm_gyfs_bl;
	}
	
// 缮证后可能还需打印审批表，需要证书编号和权证号
	private String bdcqzh;

	public String getBdcqzh() {
		return bdcqzh;
	}

	public void setBdcqzh(String bdcqzh) {
		this.bdcqzh = bdcqzh;
	}

	private String zsbh;

	public String getZsbh() {
		return zsbh;
	}

	public void setZsbh(String zsbh) {
		this.zsbh = zsbh;
	}
	
	private String bdcqzh_all;
	
	public String getBdcqzh_all() {
		return bdcqzh_all;
	}

	public void setBdcqzh_all(String bdcqzh_all) {
		this.bdcqzh_all = bdcqzh_all;
	}

	public String getQlrxm2() {
		return qlrxm2;
	}

	public void setQlrxm2(String qlrxm2) {
		this.qlrxm2 = qlrxm2;
	}

	public String getZjzl2() {
		return zjzl2;
	}

	public void setZjzl2(String zjzl2) {
		this.zjzl2 = zjzl2;
	}

	public String getZjh2() {
		return zjh2;
	}

	public void setZjh2(String zjh2) {
		this.zjh2 = zjh2;
	}

	public String getDz2() {
		return dz2;
	}

	public void setDz2(String dz2) {
		this.dz2 = dz2;
	}

	public String getYb2() {
		return yb2;
	}

	public void setYb2(String yb2) {
		this.yb2 = yb2;
	}

	public String getFddbr2() {
		return fddbr2;
	}

	public void setFddbr2(String fddbr2) {
		this.fddbr2 = fddbr2;
	}

	public String getFddbrdh2() {
		return fddbrdh2;
	}

	public void setFddbrdh2(String fddbrdh2) {
		this.fddbrdh2 = fddbrdh2;
	}

	public String getFddbrzjhm2() {
		return fddbrzjhm2;
	}

	public void setFddbrzjhm2(String fddbrzjhm2) {
		this.fddbrzjhm2 = fddbrzjhm2;
	}

	public String getDh2() {
		return dh2;
	}

	public void setDh2(String dh2) {
		this.dh2 = dh2;
	}

	public String getDlrxm2() {
		return dlrxm2;
	}

	public void setDlrxm2(String dlrxm2) {
		this.dlrxm2 = dlrxm2;
	}

	public String getDlrsfzh2() {
		return dlrsfzh2;
	}

	public void setDlrsfzh2(String dlrsfzh2) {
		this.dlrsfzh2 = dlrsfzh2;
	}

	public String getDlrdh2() {
		return dlrdh2;
	}

	public void setDlrdh2(String dlrdh2) {
		this.dlrdh2 = dlrdh2;
	}

	public String getDljgmc2() {
		return dljgmc2;
	}

	public void setDljgmc2(String dljgmc2) {
		this.dljgmc2 = dljgmc2;
	}

}
