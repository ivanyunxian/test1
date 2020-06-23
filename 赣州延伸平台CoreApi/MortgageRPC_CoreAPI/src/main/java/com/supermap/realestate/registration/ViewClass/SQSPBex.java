package com.supermap.realestate.registration.ViewClass;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;

import com.supermap.realestate.registration.handler.DJHandler;
import com.supermap.realestate.registration.factorys.HandlerFactory;
import com.supermap.realestate.registration.mapping.HandlerMapping.RegisterWorkFlow;
import com.supermap.realestate.registration.model.BDCS_DJDY_GZ;
import com.supermap.realestate.registration.model.BDCS_DJDY_XZ;
import com.supermap.realestate.registration.model.BDCS_DJFZ;
import com.supermap.realestate.registration.model.BDCS_DJSZ;
import com.supermap.realestate.registration.model.BDCS_FSQL_GZ;
import com.supermap.realestate.registration.model.BDCS_FSQL_LS;
import com.supermap.realestate.registration.model.BDCS_PARTIALLIMIT;
import com.supermap.realestate.registration.model.BDCS_QLR_GZ;
import com.supermap.realestate.registration.model.BDCS_QLR_LS;
import com.supermap.realestate.registration.model.BDCS_QLR_XZ;
import com.supermap.realestate.registration.model.BDCS_QL_GZ;
import com.supermap.realestate.registration.model.BDCS_QL_LS;
import com.supermap.realestate.registration.model.BDCS_QL_XZ;
import com.supermap.realestate.registration.model.BDCS_SQR;
import com.supermap.realestate.registration.model.BDCS_TDYT_GZ;
import com.supermap.realestate.registration.model.BDCS_TDYT_XZ;
import com.supermap.realestate.registration.model.BDCS_XMXX;
import com.supermap.realestate.registration.model.BDCS_ZS_GZ;
import com.supermap.realestate.registration.model.WFD_MAPPING;
import com.supermap.realestate.registration.model.YC_SC_H_XZ;
import com.supermap.realestate.registration.model.interfaces.AgriculturalLand;
import com.supermap.realestate.registration.model.interfaces.Building;
import com.supermap.realestate.registration.model.interfaces.Forest;
import com.supermap.realestate.registration.model.interfaces.House;
import com.supermap.realestate.registration.model.interfaces.OwnerLand;
import com.supermap.realestate.registration.model.interfaces.RealUnit;
import com.supermap.realestate.registration.model.interfaces.Rights;
import com.supermap.realestate.registration.model.interfaces.Sea;
import com.supermap.realestate.registration.model.interfaces.TDYT;
import com.supermap.realestate.registration.model.interfaces.UseLand;
import com.supermap.realestate.registration.service.QueryService;
import com.supermap.realestate.registration.tools.EntityTools;
import com.supermap.realestate.registration.tools.RightsTools;
import com.supermap.realestate.registration.tools.UnitTools;
import com.supermap.realestate.registration.util.ConfigHelper;
import com.supermap.realestate.registration.util.ConstHelper;
import com.supermap.realestate.registration.util.ConstValue;
import com.supermap.realestate.registration.util.ConstValue.BDCDYLX;
import com.supermap.realestate.registration.util.ConstValue.DJDYLY;
import com.supermap.realestate.registration.util.ConstValue.DJLX;
import com.supermap.realestate.registration.util.ConstValue.QLLX;
import com.supermap.realestate.registration.util.ConstValue.SF;
import com.supermap.realestate.registration.util.ConstValue.SQRLB;
import com.supermap.realestate.registration.util.ConfigHelper;
import com.supermap.realestate.registration.util.DateUtil;
import com.supermap.realestate.registration.util.GetProperties;
import com.supermap.realestate.registration.util.Global;
import com.supermap.realestate.registration.util.ObjectHelper;
import com.supermap.realestate.registration.util.ProjectHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.wisdombusiness.core.SuperSpringContext;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import com.supermap.wisdombusiness.workflow.model.Wfi_ProInst;
import com.supermap.wisdombusiness.workflow.service.common.Approval;
import com.supermap.wisdombusiness.workflow.service.common.TreeInfo;
import com.supermap.wisdombusiness.workflow.service.wfm.SmProSPService;

/**
 * 
 * @Description:申请审批表
 * @author OuZhanRong
 * @date 2015年6月22日 下午2:51:15
 * @Copyright SuperMap
 */
public class SQSPBex {

	// private static Logger logger = Logger.getLogger(SQSPBex.class);

	private CommonDao dao;

	
	
	private QueryService queryServiceImpl;

	String xmbh;

	String fwzrzzl;

	public SQSPBex() {
		this.dao = SuperSpringContext.getContext().getBean(CommonDao.class);
	
	}

	/**
	 * 项目信息
	 */
	public XMXX xmxx;
	/**
	 * 权利人列表 wuz
	 */
	@SuppressWarnings("rawtypes")
	public List<Map> sqrs;

	public List<Map> sqrs2;
	

	/**
	 * 权利人
	 */
	public SQR qlr;

	/**
	 * 义务人
	 */
	public SQR ywr;
	
	/**
	 * 利害关系人
	 */
	public SQR lhgxr;

	/**
	 * 不动产单元
	 */
	public BDCDY bdcdy;

	/**
	 * 权利信息
	 */
	public QLXX qlxx;

	@SuppressWarnings("rawtypes")
	public List<Map> getSqrs() {
		return sqrs;
	}

	public XMXX getXmxx() {
		return xmxx;
	}

	public SQR getQlr() {
		return qlr;
	}

	public SQR getYwr() {
		return ywr;
	}
	
	public SQR getLhgxr() {
		return lhgxr;
	}

	public BDCDY getBdcdy() {
		return bdcdy;
	}

	public QLXX getQlxx() {
		return qlxx;
	}

	/**
	 * 申请人（权利人、义务人）
	 * 
	 * @ClassName: SQR
	 * @author liushufeng
	 * @date 2015年8月6日 下午10:47:30
	 */
	public class SQR {
		// 权利人姓名
		private String qlrxm;
		// 权利人姓名+共有方式；
		private String qlrxm_gyfs;
		// 权利人姓名+共有方式；
		private String qlrxm_gyfs_bl;
		// 权利人证件种类
		private String qlrzjzl;
		// 权利人证件号
		private String qlrzjh;
		// 权利人地址
		private String qlrdz;
		// 权利人邮编
		private String qlryb;
		// 权利人法定代表人
		private String qlrfddbr;
		// 权利人电话
		private String qlrdh;
		// 权利人代理人姓名
		private String qlrdlrxm;
		// 权利人代理人姓名
		private String dlrsfzh;
		// 权利人代理人电话
		private String qlrdlrdh;
		// 权利人代理机构名称
		private String qlrdljgmc;
		// 法定代表人电话
		private String fddbrdh;
		// 法定代表人证件号码
		private String fddbrzjhm;
		// 债务人
		private String zwr;

		public String getFddbrdh() {
			return fddbrdh;
		}

		public String getFddbrzjhm() {
			return fddbrzjhm;
		}

		/**
		 * getter方法
		 * 
		 * @Title: getQlrxm
		 * @author:liushufeng
		 * @date：2015年8月7日 下午3:17:46
		 * @return
		 */
		public String getQlrxm() {
			return qlrxm;
		}

		public String getQlrxm_gyfs() {
			return qlrxm_gyfs;
		}

		public void setQlrxm_gyfs(String qlrxm_gyfs) {
			this.qlrxm_gyfs = qlrxm_gyfs;
		}

		public String getQlrxm_gyfs_bl() {
			return qlrxm_gyfs_bl;
		}

		public void setQlrxm_gyfs_bl(String qlrxm_gyfs_bl) {
			this.qlrxm_gyfs_bl = qlrxm_gyfs_bl;
		}

		/**
		 * getter方法
		 * 
		 * @Title: getQlrzjzl
		 * @author:liushufeng
		 * @date：2015年8月7日 下午3:18:03
		 * @return
		 */
		public String getQlrzjzl() {
			return qlrzjzl;
		}

		/**
		 * getter方法
		 * 
		 * @Title: getQlrzjh
		 * @author:liushufeng
		 * @date：2015年8月7日 下午3:18:11
		 * @return
		 */
		public String getQlrzjh() {
			return qlrzjh;
		}

		/**
		 * getter方法
		 * 
		 * @Title: getQlrdz
		 * @author:liushufeng
		 * @date：2015年8月7日 下午3:18:16
		 * @return
		 */
		public String getQlrdz() {
			return qlrdz;
		}

		/**
		 * getter方法
		 * 
		 * @Title: getQlryb
		 * @author:liushufeng
		 * @date：2015年8月7日 下午3:18:22
		 * @return
		 */
		public String getQlryb() {
			return qlryb;
		}

		/**
		 * getter方法
		 * 
		 * @Title: getQlrfddbr
		 * @author:liushufeng
		 * @date：2015年8月7日 下午3:18:27
		 * @return
		 */
		public String getQlrfddbr() {
			return qlrfddbr;
		}

		/**
		 * getter方法
		 * 
		 * @Title: getQlrdh
		 * @author:liushufeng
		 * @date：2015年8月7日 下午3:18:32
		 * @return
		 */
		public String getQlrdh() {
			return qlrdh;
		}

		/**
		 * getter方法
		 * 
		 * @Title: getQlrdlrxm
		 * @author:liushufeng
		 * @date：2015年8月7日 下午3:18:37
		 * @return
		 */
		public String getQlrdlrxm() {
			return qlrdlrxm;
		}

		/**
		 * getter方法
		 * 
		 * @Title: getQlrdlrdh
		 * @author:liushufeng
		 * @date：2015年8月7日 下午3:18:42
		 * @return
		 */
		public String getQlrdlrdh() {
			return qlrdlrdh;
		}

		/**
		 * getter方法
		 * 
		 * @Title: getQlrdljgmc
		 * @author:liushufeng
		 * @date：2015年8月7日 下午3:18:46
		 * @return
		 */
		public String getQlrdljgmc() {
			return qlrdljgmc;
		}

		public SQR buildQLR(String xmbh, SQRLB lb) {
			String xmbhcondition = ProjectHelper.GetXMBHCondition(xmbh);
			List<BDCS_SQR> listsqr = dao.getDataList(BDCS_SQR.class, xmbhcondition + " ORDER BY SXH");
			if (listsqr != null) {
				// 刘树峰2015.12.19 以前申请表上是否显示多个人是在配置及文件里配置的，现在统一改为显示多个人。
				if (1 > 0) {
					for (BDCS_SQR s : listsqr) {
						if (!StringHelper.isEmpty(s.getSQRLB())) {
							if (s.getSQRLB().equals(lb.Value)) {
								this.qlrxm = StringHelper.isEmpty(this.qlrxm) ? s.getSQRXM()
										: this.qlrxm + "," + s.getSQRXM();
								this.qlrxm_gyfs = StringHelper.isEmpty(this.qlrxm_gyfs)
										? s.getSQRXM() + "（" + ConstHelper.getNameByValue("GYFS", s.getGYFS()) + "）"
										: this.qlrxm_gyfs + "," + s.getSQRXM() + "（"
												+ ConstHelper.getNameByValue("GYFS", s.getGYFS()) + "）";
								if ("2".equals(s.getGYFS())) {
									this.qlrxm_gyfs_bl = StringHelper.isEmpty(this.qlrxm_gyfs_bl)
											? s.getSQRXM() + "（" + ConstHelper.getNameByValue("GYFS", s.getGYFS()) + " "
													+ s.getQLBL() + "）"
											: this.qlrxm_gyfs_bl + "," + s.getSQRXM() + "（"
													+ ConstHelper.getNameByValue("GYFS", s.getGYFS()) + " "
													+ s.getQLBL() + "）";
								} else {
									this.qlrxm_gyfs_bl = this.qlrxm_gyfs;
								}
								this.qlrzjzl = StringHelper.isEmpty(this.qlrzjzl)
										? ConstHelper.getNameByValue("ZJLX", s.getZJLX())
										: this.qlrzjzl + "," + ConstHelper.getNameByValue("ZJLX", s.getZJLX());
								this.qlrzjh = StringHelper.isEmpty(this.qlrzjh) ? s.getZJH()
										: this.qlrzjh + "," + s.getZJH();
								this.qlrdz = s.getTXDZ();
								this.qlryb = s.getYZBM();
								if (!StringHelper.isEmpty(s.getFDDBR())) {
									this.qlrfddbr = StringHelper.isEmpty(this.qlrfddbr) ? s.getFDDBR()
											: this.qlrfddbr + "," + s.getFDDBR();
								}
								if (!StringHelper.isEmpty(s.getLXDH())) {
									this.qlrdh = StringHelper.isEmpty(this.qlrdh) ? s.getLXDH()
											: this.qlrdh + "," + s.getLXDH();
								}
								if (!StringHelper.isEmpty(s.getDLRXM())) {
									this.qlrdlrxm = StringHelper.isEmpty(this.qlrdlrxm) ? s.getDLRXM()
											: this.qlrdlrxm + "," + s.getDLRXM();
									this.qlrdlrdh = StringHelper.isEmpty(this.qlrdlrdh) ? s.getDLRLXDH()
											: this.qlrdlrdh + "," + s.getDLRLXDH();
									this.dlrsfzh = StringHelper.isEmpty(this.dlrsfzh) ? s.getDLRZJHM()
											: this.dlrsfzh + "," + s.getDLRZJHM();
								}

								if (!StringHelper.isEmpty(s.getDLJGMC())) {
									this.qlrdljgmc = StringHelper.isEmpty(this.qlrdljgmc) ? s.getDLJGMC()
											: this.qlrdljgmc + "," + s.getDLJGMC();
								}
								if (!StringHelper.isEmpty(s.getFDDBRDH())) {
									this.fddbrdh = StringHelper.isEmpty(this.fddbrdh) ? s.getFDDBRDH()
											: this.fddbrdh + "," + s.getFDDBRDH();
								}
								if (!StringHelper.isEmpty(s.getFDDBRZJHM())) {
									this.fddbrzjhm = StringHelper.isEmpty(this.fddbrzjhm) ? s.getFDDBRZJHM()
											: this.fddbrzjhm + "," + s.getFDDBRZJHM();
								}
								if (!StringHelper.isEmpty(s.getZWR())) {
									this.zwr = StringHelper.isEmpty(this.zwr) ? s.getZWR()
											: this.zwr + "," + s.getZWR();
								}
							}
						}
					}
				} else {

					for (BDCS_SQR s : listsqr) {
						if (!StringHelper.isEmpty(s.getSQRLB())) {
							if (s.getSQRLB().equals(lb.Value)) {
								this.qlrxm = s.getSQRXM();
								this.qlrzjzl = ConstHelper.getNameByValue("ZJLX", s.getZJLX());
								this.qlrzjh = s.getZJH();
								this.qlrdz = s.getTXDZ();
								this.qlryb = s.getYZBM();
								this.qlrfddbr = s.getFDDBR();
								this.qlrdh = s.getLXDH();
								this.qlrdlrxm = s.getDLRXM();
								this.dlrsfzh = s.getDLRZJHM();
								this.qlrdlrdh = s.getDLRLXDH();
								this.qlrdljgmc = s.getDLJGMC();
								this.fddbrdh = s.getFDDBRDH();
								break;
							}
						}
					}
				}
			}
			return this;
		}
	}

	/**
	 * 项目信息（业务类型、权利类型、收件人、日期、审批意见等）
	 * 
	 * @ClassName: XMXX
	 * @author liushufeng
	 * @date 2015年8月6日 下午10:47:51
	 */
	public class XMXX {
		// 业务号
		private String ywh;
		// 收件日期，（受理日期）
		private String sjrq;
		// 收件人（受理人）
		private String sjr;
		// 单位：平方米，公顷、万元
		private String dw;
		// 权利类型
		private String qllx;
		// 权利类型名称
		private String qllxmc;
		// 登记类型
		private String djlx;
		// 权利类型名称
		private String djlxmc;
		// 业务类型名称
		private String ywlx;
		// 初审意见
		private String csyj;
		private String csyj_scr;
		private String csyj_scr_src;
		private String csyj_scrq;
		// 开发区复审
		private String kfqfsyj;
		private String kfqfsyj_scr;
		private String kfqfsyj_scr_src;
		private String kfqfsyj_scrq;
		// 审核
		private String shyj;
		private String shyj_scr;
		private String shyj_scr_src;
		private String shyj_scrq;
		// 复审意见
		private String fsyj;
		private String fsyj_scr;
		private String fsyj_scr_src;
		private String fsyj_scrq;
		// 审批意见
		private String spyj;
		private String spyj_scr;
		private String spyj_scr_src;
		private String spyj_scrq;
		// 审批意见第二版本
		// 审批意见内容
		private List<Map> spyjs = new ArrayList<Map>();
		// 审批定义
		private List<Map> spdys = new ArrayList<Map>();
		private HashMap<String, String> otheryj = new HashMap<String, String>();

		// 以下都是没写的
		public String szry;
		public String szsj;
		public String fzry;
		public String fzsj;
		public String lzrxm;
		public String lzsj;
		public String lzrzjhm;
		public String gdzr;
		public String gdsj;
		
		//流程名称
		private String prodef_name;
		
		public void setProdef_name(String prodef_name) {
			this.prodef_name = prodef_name;
		}

		public String getProdef_name() {
			return prodef_name;
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

		public String getYwh() {
			return ywh;
		}

		public String getSjrq() {
			return sjrq;
		}

		public String getSjr() {
			return sjr;
		}

		public String getDw() {
			return dw;
		}

		public String getQllx() {
			return qllx;
		}

		public String getQllxmc() {
			return qllxmc;
		}

		public void setSpyjs(List<Map> spyjs) {
			this.spyjs = spyjs;
		}

		public void setQllxmc(String qllxmc) {
			this.qllxmc = qllxmc;
		}

		public String getDjlx() {
			return djlx;
		}

		public String getDjlxmc() {
			return djlxmc;
		}

		public void setDjlxmc(String djlxmc) {
			this.djlxmc = djlxmc;
		}

		public String getYwlx() {
			return ywlx;
		}

		public String getCsyj() {
			return csyj;
		}

		public String getCsyj_scr() {
			return csyj_scr;
		}

		public String getCsyj_scr_src() {
			return csyj_scr_src;
		}

		public String getCsyj_scrq() {
			return csyj_scrq;
		}

		public String getFsyj() {
			return fsyj;
		}

		public String getFsyj_scr() {
			return fsyj_scr;
		}

		public String getFsyj_scr_src() {
			return fsyj_scr_src;
		}

		public String getFsyj_scrq() {
			return fsyj_scrq;
		}

		public String getSpyj() {
			return spyj;
		}

		public String getSpyj_scr() {
			return spyj_scr;
		}

		public String getSpyj_scr_src() {
			return spyj_scr_src;
		}

		public String getSpyj_scrq() {
			return spyj_scrq;
		}

		public String getSzry() {
			return szry;
		}

		public String getSzsj() {
			return szsj;
		}

		public String getFzry() {
			return fzry;
		}

		public String getFzsj() {
			return fzsj;
		}

		public String getLzrxm() {
			return lzrxm;
		}

		public String getLzsj() {
			return lzsj;
		}

		public String getLzrzjhm() {
			return lzrzjhm;
		}

		public String getGdzr() {
			return gdzr;
		}

		public String getGdsj() {
			return gdsj;
		}

		public String getKfqfsyj() {
			return kfqfsyj;
		}

		public String getKfqfsyj_scr() {
			return kfqfsyj_scr;
		}

		public String getKfqfsyj_scr_src() {
			return kfqfsyj_scr_src;
		}

		public String getKfqfsyj_scrq() {
			return kfqfsyj_scrq;
		}

		public String getShyj() {
			return shyj;
		}

		public String getShyj_scr() {
			return shyj_scr;
		}

		public String getShyj_scr_src() {
			return shyj_scr_src;
		}

		public String getShyj_scrq() {
			return shyj_scrq;
		}

		@SuppressWarnings("rawtypes")
		public XMXX build(String xmbh, String acinstid, SmProSPService smProSPService, HttpServletRequest request) {
			BDCS_XMXX xm = Global.getXMXXbyXMBH(xmbh);
			if (!StringHelper.isEmpty(xm.getYWLSH())) {
				this.ywh = xm.getYWLSH();
			} else {
				this.ywh = xm.getPROJECT_ID();
			}
			this.sjrq = DateUtil.FormatByDatetime(xm.getSLSJ());
			this.sjr = xm.getSLRY();
			String workflowcode = ProjectHelper.getWorkflowCodeByProjectID(xm.getPROJECT_ID());
			if (!StringHelper.isEmpty(workflowcode)) {
				List<WFD_MAPPING> list_mapping = dao.getDataList(WFD_MAPPING.class,
						"WORKFLOWCODE='" + workflowcode + "'");
				if (list_mapping != null && list_mapping.size() > 0) {
					WFD_MAPPING mapping = list_mapping.get(0);
					if (!StringHelper.isEmpty(mapping.getSPBSJR())) {
						String str = "SELECT ACTINST.STAFF_NAME FROM BDC_WORKFLOW.WFI_ACTINST ACTINST LEFT JOIN BDC_WORKFLOW.WFI_PROINST PROISNT ON PROISNT.PROINST_ID=ACTINST.PROINST_ID WHERE ACTINST.ACTINST_NAME=''{0}'' AND FILE_NUMBER=''{1}'' ORDER BY ACTINST.ACTINST_START DESC";
						str = MessageFormat.format(str, mapping.getSPBSJR().toString(), xm.getPROJECT_ID().toString());
						List<Map> Staff_Names = dao.getDataListByFullSql(str);
						if (Staff_Names != null && !Staff_Names.isEmpty()) {
							if (!StringHelper.isEmpty(Staff_Names.get(0).get("STAFF_NAME"))) {
								this.sjr = Staff_Names.get(0).get("STAFF_NAME").toString();
							} else {
								this.sjr = "";
							}
						} else {
							this.sjr = "";
						}
					}
				}
			}
			this.dw = "1";// 默认为平方米
			this.qllx = xm.getQLLX();
			this.djlx = xm.getDJLX();
			this.djlxmc = ConstHelper.getNameByValue("DJLX", this.djlx);
			this.qllxmc = ConstHelper.getNameByValue("QLLX", this.qllx);
			if ("98".equals(this.qllx)) {
				this.qllxmc = "其他权利";
			}
			this.ywlx = getYWLX(xm.getPROJECT_ID());
			this.getSPYJ(acinstid, smProSPService, request);

			// 缮证人员和缮证时间
			String sql = MessageFormat.format("XMBH=''{0}''", xmbh);
			List<BDCS_DJSZ> djszs = dao.getDataList(BDCS_DJSZ.class, sql);
			if (djszs != null && djszs.size() > 0) {
				BDCS_DJSZ djsz = djszs.get(0);
				if (djsz != null) {
					this.szry = djsz.getSZRY();
					this.szsj = DateUtil.FormatByDatetime(djsz.getSZSJ());
				}
			}
			// 发证人员和发证时间
			List<BDCS_DJFZ> djfzs = dao.getDataList(BDCS_DJFZ.class, sql);
			if (djfzs != null && djfzs.size() > 0) {
				BDCS_DJFZ djfz = djfzs.get(0);
				if (djfz != null) {
					this.fzry = djfz.getFZRY();
					this.fzsj = DateUtil.FormatByDatetime(djfz.getFZSJ());
					this.lzrxm = djfz.getLZRXM();
					this.lzrzjhm = djfz.getLZRZJHM();
					// 石家庄版本，领证人姓名和领证人证件号自己手填

					if (SF.NO.Value.equals(ConfigHelper.getNameByValue("ShowLZRInSQSPB"))) {
						this.lzrxm = "";
						this.lzrzjhm = "";
					}

					this.lzsj = DateUtil.FormatByDatetime(djfz.getFZSJ());
				}
			}

			try {
				// 获取归档人员和归档日期
				String fulSql = "select gd.lrr,gd.lrsj " + " from bdc_dak.das_ajjbxx gd "
						+ " left join bdck.bdcs_xmxx xmxx " + " on gd.ywdh = xmxx.ywlsh " + " where xmxx.xmbh ='" + xmbh
						+ "'";
				List<Map> gds = dao.getDataListByFullSql(fulSql);
				if (gds != null && gds.size() > 0) {
					this.gdzr = (String) gds.get(0).get("LRR");
					this.gdsj = DateUtil.FormatByDatetime(gds.get(0).get("LRSJ"));
				}
			} catch (Exception e) {
				this.gdzr = "";
				this.gdsj = "";
			}
			
			try{
				//获取流程名称
				String lcmcSql = "select po.prodef_name "
						+ " from BDC_WORKFLOW.WFI_PROINST PO "
						+ " left join BDCK.BDCS_XMXX XMXX ON XMXX.PROJECT_ID = PO.FILE_NUMBER "
						+ " where xmxx.xmbh ='"+ xmbh + "'";
				List<Map> lcmc = dao.getDataListByFullSql(lcmcSql);
				if (lcmc != null && lcmc.size() > 0) {
					this.prodef_name = (String)lcmc.get(0).get("PRODEF_NAME");
				}
			}catch(Exception e){
				
			}

			return this;
		}

		private String getYWLX(String project_id) {
			String str = "";
			List<Wfi_ProInst> proins = dao.getDataList(Wfi_ProInst.class, " FILE_NUMBER='" + project_id + "'");
			if (proins != null && proins.size() > 0) {
				str = proins.get(0).getProdef_Name();
			}
			return str;
		}

		private void getSPYJ(String acinstid, SmProSPService smProSPService, HttpServletRequest request) {
			// 海口项目
			String area = GetProperties.getConstValueByKey("Admin");
			// 获取审批意见 WUZ
			String signUrl = "http://" + request.getHeader("host") + "/" + request.getContextPath()
					+ "/resources/workflow/signimg/%s.png";
			List<Approval> _approvals = smProSPService.GetSPYJ(acinstid);
			List<TreeInfo> spdytrees = smProSPService.GetSpdyTree();

			for (TreeInfo spdy : spdytrees) {
				Map<String, String> spdymap = new HashMap<String, String>();
				spdymap.put("splx", spdy.getDesc().toLowerCase());
				spdymap.put("signtype", String.valueOf(spdy.getState()));
				spdymap.put(spdy.getDesc().toLowerCase() + "mc", spdy.getText());
				spdymap.put(spdy.getDesc().toLowerCase() + "yj", "");
				spdymap.put(spdy.getDesc().toLowerCase() + "yj_scr", "");
				spdymap.put(spdy.getDesc().toLowerCase() + "yj_scr_src", "");
				spdymap.put(spdy.getDesc().toLowerCase() + "yj_scrq", "");
				spdys.add(spdymap);
			}
			int index;
			for (Approval a : _approvals) {
				Map<String, String> map = new HashMap<String, String>();
				// 对意见为空进行处理不然会报错
				if (a.getSpyjs().size() == 0)
					continue;
				index = a.getSpyjs().size() - 1;
				map.put("splx", a.getSplx().toLowerCase());
				map.put("signtype", String.valueOf(a.getSigntypeString()));
				map.put(a.getSplx().toLowerCase() + "mc", a.getSpyjs().get(index).getSpyj());
				map.put(a.getSplx().toLowerCase() + "yj", a.getSpyjs().get(index).getSpyj());
				map.put(a.getSplx().toLowerCase() + "yj_scr", a.getSpyjs().get(index).getSpr_Name());
				map.put(a.getSplx().toLowerCase() + "yj_scr_src",
						String.format(signUrl, a.getSpyjs().get(index).getSpr_Id()));
				map.put(a.getSplx().toLowerCase() + "yj_scrq",
						StringHelper.FormatByDatetime(a.getSpyjs().get(index).getSpsj()));
				spyjs.add(map);
				if (a.getSplx().equals("CS")) {
					if (a.getSpyjs() != null && a.getSpyjs().size() > 0) {
						index = a.getSpyjs().size() - 1;
						csyj = a.getSpyjs().get(index).getSpyj();
						csyj_scr = a.getSpyjs().get(index).getSpr_Name();
						// csyj_scr_src = String.format(signUrl,
						// a.getSpyjs().get(index).getSpr_Id());
						csyj_scrq = StringHelper.FormatByDatetime(a.getSpyjs().get(index).getSpsj());
						if (area.equals("460100") && a.getSpyjs().get(index).getSIGNJG() != null
								&& !a.getSpyjs().get(index).getSIGNJG().equals("")) {
							csyj_scr_src = "data:image/png;base64," + a.getSpyjs().get(index).getSIGNJG();
						} else {
							csyj_scr_src = String.format(signUrl, a.getSpyjs().get(index).getSpr_Id());
						}

					}
				} else if (a.getSplx().equals("FS")) {
					if (a.getSpyjs() != null && a.getSpyjs().size() > 0) {
						index = a.getSpyjs().size() - 1;
						fsyj = a.getSpyjs().get(index).getSpyj();
						fsyj_scr = a.getSpyjs().get(index).getSpr_Name();
						fsyj_scrq = StringHelper.FormatByDatetime(a.getSpyjs().get(index).getSpsj());
						// fsyj_scr_src = String.format(signUrl,
						// a.getSpyjs().get(index).getSpr_Id());
						if (area.equals("460100") && a.getSpyjs().get(index).getSIGNJG() != null
								&& !a.getSpyjs().get(index).getSIGNJG().equals("")) {
							fsyj_scr_src = "data:image/png;base64," + a.getSpyjs().get(index).getSIGNJG();
						} else {
							fsyj_scr_src = String.format(signUrl, a.getSpyjs().get(index).getSpr_Id());
						}
					}
				} else if (a.getSplx().equals("HD")) {
					if (a.getSpyjs() != null && a.getSpyjs().size() > 0) {
						index = a.getSpyjs().size() - 1;
						spyj = a.getSpyjs().get(index).getSpyj();
						spyj_scr = a.getSpyjs().get(index).getSpr_Name();
						spyj_scrq = StringHelper.FormatByDatetime(a.getSpyjs().get(index).getSpsj());
						// spyj_scr_src = String.format(signUrl,
						// a.getSpyjs().get(index).getSpr_Id());
						if (area.equals("460100") && a.getSpyjs().get(index).getSIGNJG() != null
								&& !a.getSpyjs().get(index).getSIGNJG().equals("")) {
							spyj_scr_src = "data:image/png;base64," + a.getSpyjs().get(index).getSIGNJG();
						} else {
							spyj_scr_src = String.format(signUrl, a.getSpyjs().get(index).getSpr_Id());
						}
					}
				} else if ("KFQFS".equals(a.getSplx())) {
					if (a.getSpyjs() != null && a.getSpyjs().size() > 0) {
						index = a.getSpyjs().size() - 1;
						kfqfsyj = a.getSpyjs().get(index).getSpyj();
						kfqfsyj_scr = a.getSpyjs().get(index).getSpr_Name();
						kfqfsyj_scrq = StringHelper.FormatByDatetime(a.getSpyjs().get(index).getSpsj());
						// kfqfsyj_scr_src = String.format(signUrl,
						// a.getSpyjs().get(index).getSpr_Id());
						if (area.equals("460100") && a.getSpyjs().get(index).getSIGNJG() != null
								&& !a.getSpyjs().get(index).getSIGNJG().equals("")) {
							kfqfsyj_scr_src = "data:image/png;base64," + a.getSpyjs().get(index).getSIGNJG();
						} else {
							kfqfsyj_scr_src = String.format(signUrl, a.getSpyjs().get(index).getSpr_Id());
						}
					}
				} else if ("SH".equals(a.getSplx())) {
					if (a.getSpyjs() != null && a.getSpyjs().size() > 0) {
						index = a.getSpyjs().size() - 1;
						shyj = a.getSpyjs().get(index).getSpyj();
						shyj_scr = a.getSpyjs().get(index).getSpr_Name();
						shyj_scrq = StringHelper.FormatByDatetime(a.getSpyjs().get(index).getSpsj());
						// shyj_scr_src = String.format(signUrl,
						// a.getSpyjs().get(index).getSpr_Id());
						if (area.equals("460100") && a.getSpyjs().get(index).getSIGNJG() != null
								&& !a.getSpyjs().get(index).getSIGNJG().equals("")) {
							shyj_scr_src = "data:image/png;base64," + a.getSpyjs().get(index).getSIGNJG();
						} else {
							shyj_scr_src = String.format(signUrl, a.getSpyjs().get(index).getSpr_Id());
						}
					}
				} else {
					if (a.getSpyjs() != null && a.getSpyjs().size() > 0) {
						index = a.getSpyjs().size() - 1;
						String yj = a.getSpyjs().get(index).getSpyj();
						String yj_scr = a.getSpyjs().get(index).getSpr_Name();
						String yj_scrq = StringHelper.FormatByDatetime(a.getSpyjs().get(index).getSpsj());
						String yj_scr_src = "";
						if (a.getSpyjs().get(index).getSIGNJG() != null
								&& !a.getSpyjs().get(index).getSIGNJG().equals("")) {
							yj_scr_src = "data:image/png;base64," + a.getSpyjs().get(index).getSIGNJG();
						} else {
							yj_scr_src = String.format(signUrl, a.getSpyjs().get(index).getSpr_Id());
						}
						String splx_lower = a.getSplx().toLowerCase();
						otheryj.put(splx_lower + "yj", yj);
						otheryj.put(splx_lower + "yj_scr", yj_scr);
						otheryj.put(splx_lower + "yj_scrq", yj_scrq);
						otheryj.put(splx_lower + "yj_scr_src", yj_scr_src);
					}
				}
			}
		}
	}

	/**
	 * 不动产单元
	 * 
	 * @ClassName: BDCDY
	 * @author liushufeng
	 * @date 2015年8月6日 下午10:48:32
	 */
	public class BDCDY {
		// 共有方式
		private String gyqk;
		// 权利类型
		private String mainqllx;

		public String getMainqllx() {
			return mainqllx;
		}

		public void setMainqllx(String mainqllx) {
			this.mainqllx = mainqllx;
		}

		// 权利性质
		private String qlxz;
		// 使用期限
		private String syqx;

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

		// 坐落
		private String zl;
		// 不动产单元号
		private String bdcdyh;
		// 不动产类型
		private String bdclx;
		// 申请表不动产类型值显示土地或者土地/房屋
		private String bdclx_tdfw;
		// 面积
		private String mj;
		// 土地分摊面积/房屋建筑面积
		private String tdftmj_fwjzmj;
		// 用途
		private String yt;
		// 原不动产权证书号
		private String ybdcqzsh;
		// 一个登记单元对应的所有的原不动产权证书号
		private HashSet<String> temp_ybdcqzsh;
		// 一个登记单元对应的所有的不动产权证号
		private HashSet<String> temp_bdcqzh;
		// 用海类型
		private String yhlx;
		// 构筑物类型
		private String gzwlx;
		// 林种
		private String lz;
		// 栋号
		private String zrzh;
		// 房号
		private String fh;
		// 建筑面积
		private String jzmj;
		// 套内建筑面积
		private String tnmj;
		// 分摊面积
		private String ftmj;
		// 土地分摊面积
		private String fttdmj;

		public String getFttdmj() {
			return fttdmj;
		}

		public void setFttdmj(String fttdmj) {
			this.fttdmj = fttdmj;
		}

		public String getFwxz() {
			return fwxz;
		}

		// 所在层数
		private String szcs;
		// 规划用途
		private String ghyt;
		// 不动产权证号
		private String bdcqzh;

		public String getBdcqzh() {
			return bdcqzh;
		}

		public void setBdcqzh(String bdcqzh) {
			this.bdcqzh = bdcqzh;
		}

		/*** 附章表 ***/
		private String bdcqzhEx;

		public String getBdcqzhEx() {
			return bdcqzhEx;
		}

		public void setBdcqzhEx(String bdcqzhEx) {
			this.bdcqzhEx = bdcqzhEx;
		}

		private String fwztEx;

		public String getFwztEx() {
			return fwztEx;
		}

		public void setFwztEx(String fwztEx) {
			this.fwztEx = fwztEx;
		}

		/*** 附章表 ***/
		// 证书编号
		private String zsbh;

		public String getZsbh() {
			return zsbh;
		}

		public void setZsbh(String zsbh) {
			this.zsbh = zsbh;
		}

		// bdcqzh_all
		private String bdcqzh_all;

		public String getBdcqzh_all() {
			return bdcqzh_all;
		}

		public void setBdcqzh_all(String bdcqzh_all) {
			this.bdcqzh_all = bdcqzh_all;
		}

		private List<BDCDY> children;
		private String fwxz;

		public String getZl() {
			return zl;
		}

		public String getBdcdyh() {
			return bdcdyh;
		}

		public HashSet<String> getTemp_ybdcqzsh() {
			return temp_ybdcqzsh;
		}

		public void setTemp_ybdcqzsh(HashSet<String> temp_ybdcqzsh) {
			this.temp_ybdcqzsh = temp_ybdcqzsh;
		}

		public HashSet<String> getTemp_bdcqzh() {
			return temp_bdcqzh;
		}

		public void setTemp_bdcqzh(HashSet<String> temp_bdcqzh) {
			this.temp_bdcqzh = temp_bdcqzh;
		}

		public String getBdclx() {
			return bdclx;
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

		public String getTdftmj_fwjzmj() {
			return tdftmj_fwjzmj;
		}

		public String getYt() {
			return yt;
		}

		public String getYbdcqzsh() {
			return ybdcqzsh;
		}

		public String getYhlx() {
			return yhlx;
		}

		public String getGzwlx() {
			return gzwlx;
		}

		public String getLz() {
			return lz;
		}

		public String getFh() {
			return fh;
		}

		public String getJzmj() {
			return jzmj;
		}

		public String getTnmj() {
			return tnmj;
		}

		public String getFtmj() {
			return ftmj;
		}

		public String getSzcs() {
			return szcs;
		}

		public String getGhyt() {
			return ghyt;
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

		public List<BDCDY> getChildren() {
			return children;
		}

		private boolean isBGDY(List<BDCS_DJDY_GZ> listdjdy) {
			Map<String, String> ly = new HashMap<String, String>();
			for (BDCS_DJDY_GZ djdy : listdjdy) {
				if (!StringUtils.isEmpty(djdy.getLY())) {
					if (!ly.containsKey(djdy.getLY()))
						ly.put(djdy.getLY(), "");
				}
			}
			// 只有一种来源 说明不是变更登记
			if (ly.size() <= 1)
				return false;
			else
				return true;
		}

		/**
		 * wuz
		 * 
		 * @param 如果是变更登记过滤登记
		 *            单元，如果不是变更登记返回所有单元。DJDY表的Ly有二种来源
		 *            说明是变更登记，返回变更后的登记单元表，如果不是变更登记返回全部登记单元信息
		 * @return
		 */
		private List<BDCS_DJDY_GZ> IfBGDJfilterDjdy(List<BDCS_DJDY_GZ> listdjdy, DJDYLY ly) {
			if (isBGDY(listdjdy)) {
				List<BDCS_DJDY_GZ> bghDjdys = new ArrayList<BDCS_DJDY_GZ>();
				for (BDCS_DJDY_GZ djdy : listdjdy) {
					if (DJDYLY.initFrom(djdy.getLY()).equals(ly))
						bghDjdys.add(djdy);
				}
				return bghDjdys;
			} else
				return listdjdy;

		}

		@SuppressWarnings("rawtypes")
		public BDCDY build(String xmbh) {
			ProjectInfo info = ProjectHelper.GetPrjInfoByXMBH(xmbh);
			String _djlx = info.getDjlx();
			String _bdcdylx = info.getBdcdylx();
			String _qllx= info.getQllx();
			String xzqhdm = ConfigHelper.getNameByValue("XZQHDM");
			if(DJLX.CSDJ.Value.equals(_djlx)
					&&(BDCDYLX.H.Value.equals(_bdcdylx)||BDCDYLX.YCH.Value.equals(_bdcdylx))
					&&("4".equals(_qllx)||"6".equals(_qllx)||"6".equals(_qllx))) {
				HashSet<String> ybdcqzh = new HashSet<String>();
				HashSet<String> bdcqzh = new HashSet<String>();
				String xmbhcondition = ProjectHelper.GetXMBHCondition(xmbh);
				List<BDCS_DJDY_GZ> listdjdy_all = dao.getDataList(BDCS_DJDY_GZ.class, xmbhcondition);
				List<BDCS_DJDY_GZ> listdjdy = IfBGDJfilterDjdy(listdjdy_all, DJDYLY.GZ);
				if (listdjdy != null && listdjdy.size() > 0) {
					List<RealUnit> units = new ArrayList<RealUnit>();
					for (BDCS_DJDY_GZ djdy : listdjdy) {
						RealUnit unit = UnitTools.loadUnit(BDCDYLX.initFrom(djdy.getBDCDYLX()),
								DJDYLY.initFrom(djdy.getLY()), djdy.getBDCDYID());
						if (unit != null) {
							units.add(unit);
						}
					}
					units = SortUnit(units);
					listdjdy = SortDJDY(listdjdy, units);
					if (StringHelper.isEmpty(listdjdy) || listdjdy.size() <= 0)
						return this;
					BDCS_DJDY_GZ djdy = listdjdy.get(0);
					if (djdy == null)
						return this;
					// 只有一个djdy
					this.buildex(djdy, xmbh, true);
					if (this.temp_ybdcqzsh != null && this.temp_ybdcqzsh.size() > 0) {
						this.ybdcqzsh = viewByConfiger(this.temp_ybdcqzsh);
					}
					// 多个djdy
					children = new ArrayList<SQSPBex.BDCDY>();
					BDCDY _bdcdy = null;
					// 广西需求单个单元也显示
					int size;
					if (xzqhdm.indexOf("45") == 0)
						size = 0;
					else
						size = 1;
					if (listdjdy_all.size() > size) {// 不明白为啥源代码要求大于1，现改为大于0，有问题再改
						// 刘树峰 2015.12.27
						// 判断如果是房屋或者预测房屋，按照配置表里边配置的方式排序，如果没有配置，默认按照单元号，起始层，房号的顺序排序
						if (!StringHelper.isEmpty(djdy.getBDCDYLX()) && (djdy.getBDCDYLX().equals(BDCDYLX.H.Value)
								|| djdy.getBDCDYLX().equals(BDCDYLX.YCH.Value))) {
							String sortFields = "  H.FH,H.DYH,H.QSC";
							if (!StringHelper.isEmpty(ConfigHelper.getNameByValue("SQSPBDYSORTFIELDS")))
								sortFields = "H." + ConfigHelper.getNameByValue("SQSPBDYSORTFIELDS").replaceAll(",", ",H.");
							String tablename1 = "BDCK.BDCS_H_GZ";
							String tablename2 = "BDCK.BDCS_H_XZ";
							sortFields = sortFields.toUpperCase();
							if (djdy.getBDCDYLX().equals(BDCDYLX.YCH.Value)) {
								tablename1 += "Y";
								tablename2 += "Y";
								sortFields = sortFields.replaceAll("SCJZMJ", "YCJZMJ").replaceAll("SCTNJZMJ", "YCTNJZMJ")
										.replaceAll("SCFTJZMJ", "YCFTJZMJ").replaceAll("SCDXBFJZMJ", "YCDXBFJZMJ")
										.replaceAll("SCQTJZMJ", "YCQTJZMJ").replaceAll("SCFTXS", "YCFTXS");
							}

						String sql = "";
						int count = 0;
						List<String> lyList = new ArrayList<String>();
						for (BDCS_DJDY_GZ djdy1 : listdjdy_all) {
							String ly = djdy1.getLY();
							if ("01".equals(ly)) {
								count = count + 1;
							}
							if (!lyList.contains(ly)) {
								lyList.add(ly);
							}
						}
						if (count > 0 && lyList.size() > 1) {
							sql = "SELECT ID,{0},ROW_NUMBER() OVER(PARTITION BY H.ZRZH,H.DYH ORDER BY {0}) ROW_NUMBER FROM BDCK.BDCS_DJDY_GZ DJDY  LEFT JOIN {2} H ON H.BDCDYID=DJDY.BDCDYID  WHERE DJDY.XMBH=''{1}'' AND LY='01'  AND NVL2(H.BDCDYID,1,0)=1  "
									+ "UNION  "
									+ "SELECT ID,{0},ROW_NUMBER() OVER(PARTITION BY H.ZRZH,H.DYH ORDER BY {0}) ROW_NUMBER  FROM BDCK.BDCS_DJDY_GZ DJDY LEFT JOIN {3} H ON H.BDCDYID=DJDY.BDCDYID  WHERE DJDY.XMBH=''{1}'' AND LY='01' AND NVL2(H.BDCDYID,1,0)=1  ";

						} else {
							sql = "SELECT ID,{0},ROW_NUMBER() OVER(PARTITION BY H.ZRZH,H.DYH ORDER BY {0}) ROW_NUMBER  FROM BDCK.BDCS_DJDY_GZ DJDY  LEFT JOIN {2} H ON H.BDCDYID=DJDY.BDCDYID  WHERE DJDY.XMBH=''{1}''   AND NVL2(H.BDCDYID,1,0)=1  "
									+ "UNION  "
									+ "SELECT ID,{0},ROW_NUMBER() OVER(PARTITION BY H.ZRZH,H.DYH ORDER BY {0}) ROW_NUMBER  FROM BDCK.BDCS_DJDY_GZ DJDY LEFT JOIN {3} H ON H.BDCDYID=DJDY.BDCDYID  WHERE DJDY.XMBH=''{1}''  AND NVL2(H.BDCDYID,1,0)=1  ";
						}
						sql = MessageFormat.format(sql, sortFields, xmbh, tablename1, tablename2);
						List<Map> listmap = dao.getDataListByFullSql(sql);
						if (listmap != null && listmap.size() > 0) {
							for (Map mp : listmap) {
								String did = mp.get("ID") == null ? "" : mp.get("ID").toString();
								if (!StringHelper.isEmpty(did)) {
									BDCS_DJDY_GZ djdynew = dao.get(BDCS_DJDY_GZ.class, did);
									if (djdynew != null) {
										_bdcdy = new BDCDY().buildex(djdynew, xmbh, false);
										children.add(_bdcdy);
										if (!StringHelper.isEmpty(_bdcdy.getTemp_ybdcqzsh())) {
											ybdcqzh.addAll(_bdcdy.getTemp_ybdcqzsh());
											// bdcqzh.addAll(_bdcdy.getTemp_bdcqzh());
										}
										if (!StringHelper.isEmpty(_bdcdy.getTemp_bdcqzh())) {
											bdcqzh.addAll(_bdcdy.getTemp_bdcqzh());
										}
									}
								}
							}
						}
					} else {
						for (int i = 0; i < listdjdy.size(); i++) {
							_bdcdy = new BDCDY().buildex(listdjdy.get(i), xmbh, false);
							children.add(_bdcdy);
							if (!StringHelper.isEmpty(_bdcdy.getTemp_ybdcqzsh())) {
								// 返回
								ybdcqzh.addAll(_bdcdy.getTemp_ybdcqzsh());
							}
							if (!StringHelper.isEmpty(_bdcdy.getTemp_bdcqzh())) {
								bdcqzh.addAll(_bdcdy.getTemp_bdcqzh());
							}
						}
					}
					if (ybdcqzh != null && ybdcqzh.size() > 0) {
						this.ybdcqzsh = viewByConfiger(ybdcqzh);
					} else {
						this.ybdcqzsh = null;
					}
					List<String> list_bdcqzh = new ArrayList<String>(bdcqzh);
					if (list_bdcqzh != null && list_bdcqzh.size() > 0) {
						this.bdcqzh = null;
						for (int i = 0; i < list_bdcqzh.size(); i++) {
							if (list_bdcqzh.get(i) != null && !StringHelper.isEmpty(list_bdcqzh.get(i))) {
								this.bdcqzh = this.bdcqzh == null ? list_bdcqzh.get(i)
										: this.bdcqzh + "," + list_bdcqzh.get(i);
							}
						}

						}
					}
					// 获取zsbh
					List<BDCS_ZS_GZ> zslst = dao.getDataList(BDCS_ZS_GZ.class, "xmbh='" + xmbh + "' order by BDCQZH");
					List<String> list_zsbh = new ArrayList<String>();
					for (BDCS_ZS_GZ zs : zslst) {
						if (zs.getZSBH() != null && !StringHelper.isEmpty(zs.getZSBH())) {
							if (!list_zsbh.contains(zs.getZSBH())) {
								list_zsbh.add(zs.getZSBH());
								this.zsbh = this.zsbh == null ? zs.getZSBH() : this.zsbh + "," + zs.getZSBH();
							}
						}
					}
					// 获取bdcqzh_all
					List<String> list_qzh = new ArrayList<String>();
					for (BDCS_ZS_GZ zs : zslst) {
						if (zs.getBDCQZH() != null && !StringHelper.isEmpty(zs.getBDCQZH())) {
							if (!list_qzh.contains(zs.getBDCQZH())) {
								list_qzh.add(zs.getBDCQZH());
								this.bdcqzh_all = this.bdcqzh_all == null ? zs.getBDCQZH()
										: this.bdcqzh_all + "," + zs.getBDCQZH();
							}
						}
					}
				}

				return this;
			
			}else {

				HashSet<String> ybdcqzh = new HashSet<String>();
				HashSet<String> bdcqzh = new HashSet<String>();
				String xmbhcondition = ProjectHelper.GetXMBHCondition(xmbh);
				List<BDCS_DJDY_GZ> listdjdy_all = dao.getDataList(BDCS_DJDY_GZ.class, xmbhcondition);
				List<BDCS_DJDY_GZ> listdjdy = IfBGDJfilterDjdy(listdjdy_all, DJDYLY.GZ);
				if (listdjdy != null && listdjdy.size() > 0) {
					List<RealUnit> units = new ArrayList<RealUnit>();
					for (BDCS_DJDY_GZ djdy : listdjdy) {
						RealUnit unit = UnitTools.loadUnit(BDCDYLX.initFrom(djdy.getBDCDYLX()),
								DJDYLY.initFrom(djdy.getLY()), djdy.getBDCDYID());
						if (unit != null) {
							units.add(unit);
						}
					}
					units = SortUnit(units);
					listdjdy = SortDJDY(listdjdy, units);
					if (StringHelper.isEmpty(listdjdy) || listdjdy.size() <= 0)
						return this;
					BDCS_DJDY_GZ djdy = listdjdy.get(0);
					if (djdy == null)
						return this;
					// 只有一个djdy
					this.buildex(djdy, xmbh, true);
					if (this.temp_ybdcqzsh != null && this.temp_ybdcqzsh.size() > 0) {
						this.ybdcqzsh = viewByConfiger(this.temp_ybdcqzsh);
					}
					// 多个djdy
					children = new ArrayList<SQSPBex.BDCDY>();
					BDCDY _bdcdy = null;
					// 广西需求单个单元也显示
					int size;
					if (xzqhdm.indexOf("45") == 0)
						size = 0;
					else
						size = 1;
					if (listdjdy_all.size() > size) {// 不明白为啥源代码要求大于1，现改为大于0，有问题再改
						// 刘树峰 2015.12.27
						// 判断如果是房屋或者预测房屋，按照配置表里边配置的方式排序，如果没有配置，默认按照单元号，起始层，房号的顺序排序
						if (!StringHelper.isEmpty(djdy.getBDCDYLX()) && (djdy.getBDCDYLX().equals(BDCDYLX.H.Value)
								|| djdy.getBDCDYLX().equals(BDCDYLX.YCH.Value))) {
							String sortFields = "  H.FH,H.DYH,H.QSC";
							if (!StringHelper.isEmpty(ConfigHelper.getNameByValue("SQSPBDYSORTFIELDS")))
								sortFields = "H." + ConfigHelper.getNameByValue("SQSPBDYSORTFIELDS").replaceAll(",", ",H.");
							String tablename1 = "BDCK.BDCS_H_GZ";
							String tablename2 = "BDCK.BDCS_H_XZ";
							sortFields = sortFields.toUpperCase();
							if (djdy.getBDCDYLX().equals(BDCDYLX.YCH.Value)) {
								tablename1 += "Y";
								tablename2 += "Y";
								sortFields = sortFields.replaceAll("SCJZMJ", "YCJZMJ").replaceAll("SCTNJZMJ", "YCTNJZMJ")
										.replaceAll("SCFTJZMJ", "YCFTJZMJ").replaceAll("SCDXBFJZMJ", "YCDXBFJZMJ")
										.replaceAll("SCQTJZMJ", "YCQTJZMJ").replaceAll("SCFTXS", "YCFTXS");
							}

							String sql = "";
							int count = 0;
							List<String> lyList = new ArrayList<String>();
							for (BDCS_DJDY_GZ djdy1 : listdjdy_all) {
								String ly = djdy1.getLY();
								if ("01".equals(ly)) {
									count = count + 1;
								}
								if (!lyList.contains(ly)) {
									lyList.add(ly);
								}
							}
							if (count > 0 && lyList.size() > 1) {
								sql = "SELECT ID,{0},ROW_NUMBER() OVER(PARTITION BY H.ZRZH,H.DYH ORDER BY {0}) ROW_NUMBER FROM BDCK.BDCS_DJDY_GZ DJDY  LEFT JOIN {2} H ON H.BDCDYID=DJDY.BDCDYID  WHERE DJDY.XMBH=''{1}'' AND LY='01'  AND NVL2(H.BDCDYID,1,0)=1  "
										+ "UNION  "
										+ "SELECT ID,{0},ROW_NUMBER() OVER(PARTITION BY H.ZRZH,H.DYH ORDER BY {0}) ROW_NUMBER  FROM BDCK.BDCS_DJDY_GZ DJDY LEFT JOIN {3} H ON H.BDCDYID=DJDY.BDCDYID  WHERE DJDY.XMBH=''{1}'' AND LY='01' AND NVL2(H.BDCDYID,1,0)=1  ";

							} else {
								sql = "SELECT ID,{0},ROW_NUMBER() OVER(PARTITION BY H.ZRZH,H.DYH ORDER BY {0}) ROW_NUMBER  FROM BDCK.BDCS_DJDY_GZ DJDY  LEFT JOIN {2} H ON H.BDCDYID=DJDY.BDCDYID  WHERE DJDY.XMBH=''{1}''   AND NVL2(H.BDCDYID,1,0)=1  "
										+ "UNION  "
										+ "SELECT ID,{0},ROW_NUMBER() OVER(PARTITION BY H.ZRZH,H.DYH ORDER BY {0}) ROW_NUMBER  FROM BDCK.BDCS_DJDY_GZ DJDY LEFT JOIN {3} H ON H.BDCDYID=DJDY.BDCDYID  WHERE DJDY.XMBH=''{1}''  AND NVL2(H.BDCDYID,1,0)=1  ";
							}
							sql = MessageFormat.format(sql, sortFields, xmbh, tablename1, tablename2);
							List<Map> listmap = dao.getDataListByFullSql(sql);
							if (listmap != null && listmap.size() > 0) {
								for (Map mp : listmap) {
									String did = mp.get("ID") == null ? "" : mp.get("ID").toString();
									if (!StringHelper.isEmpty(did)) {
										BDCS_DJDY_GZ djdynew = dao.get(BDCS_DJDY_GZ.class, did);
										if (djdynew != null) {
											_bdcdy = new BDCDY().buildex(djdynew, xmbh, false);
											children.add(_bdcdy);
											if (!StringHelper.isEmpty(_bdcdy.getTemp_ybdcqzsh())) {
												ybdcqzh.addAll(_bdcdy.getTemp_ybdcqzsh());
												// bdcqzh.addAll(_bdcdy.getTemp_bdcqzh());
											}
											if (!StringHelper.isEmpty(_bdcdy.getTemp_bdcqzh())) {
												bdcqzh.addAll(_bdcdy.getTemp_bdcqzh());
											}
										}
									}
								}
							}
						} else {
							for (int i = 0; i < listdjdy.size(); i++) {
								_bdcdy = new BDCDY().buildex(listdjdy.get(i), xmbh, false);
								children.add(_bdcdy);
								if (!StringHelper.isEmpty(_bdcdy.getTemp_ybdcqzsh())) {
									// 返回
									ybdcqzh.addAll(_bdcdy.getTemp_ybdcqzsh());
								}
								if (!StringHelper.isEmpty(_bdcdy.getTemp_bdcqzh())) {
									bdcqzh.addAll(_bdcdy.getTemp_bdcqzh());
								}
							}
						}
						if (ybdcqzh != null && ybdcqzh.size() > 0) {
							this.ybdcqzsh = viewByConfiger(ybdcqzh);
						} else {
							this.ybdcqzsh = null;
						}
						List<String> list_bdcqzh = new ArrayList<String>(bdcqzh);
						if (list_bdcqzh != null && list_bdcqzh.size() > 0) {
							this.bdcqzh = null;
							for (int i = 0; i < list_bdcqzh.size(); i++) {
								if (list_bdcqzh.get(i) != null && !StringHelper.isEmpty(list_bdcqzh.get(i))) {
									this.bdcqzh = this.bdcqzh == null ? list_bdcqzh.get(i)
											: this.bdcqzh + "," + list_bdcqzh.get(i);
								}
							}

						}
					}
					// 获取zsbh
					List<BDCS_ZS_GZ> zslst = dao.getDataList(BDCS_ZS_GZ.class, "xmbh='" + xmbh + "' order by BDCQZH");
					List<String> list_zsbh = new ArrayList<String>();
					for (BDCS_ZS_GZ zs : zslst) {
						if (zs.getZSBH() != null && !StringHelper.isEmpty(zs.getZSBH())) {
							if (!list_zsbh.contains(zs.getZSBH())) {
								list_zsbh.add(zs.getZSBH());
								this.zsbh = this.zsbh == null ? zs.getZSBH() : this.zsbh + "," + zs.getZSBH();
							}
						}
					}
					// 获取bdcqzh_all
					List<String> list_qzh = new ArrayList<String>();
					for (BDCS_ZS_GZ zs : zslst) {
						if (zs.getBDCQZH() != null && !StringHelper.isEmpty(zs.getBDCQZH())) {
							if (!list_qzh.contains(zs.getBDCQZH())) {
								list_qzh.add(zs.getBDCQZH());
								this.bdcqzh_all = this.bdcqzh_all == null ? zs.getBDCQZH()
										: this.bdcqzh_all + "," + zs.getBDCQZH();
							}
						}
					}
				}
				return this;
			}
		}

		private String viewByConfiger(HashSet<String> ybdcqzh) {
			String ybdcqzhs = "";
			long count = 0;
			if (ybdcqzh != null && ybdcqzh.size() > 0) {
				List<String> list = new ArrayList<String>(ybdcqzh);
				String configSBT = ConfigHelper.getNameByValue("SQSPBDY_BDCDYH_YT");
				if (!StringHelper.isEmpty(configSBT)) {
					count = Integer.parseInt(configSBT);
				}
				if (count < list.size()) {
					for (int i = 0; i < count; i++) {
						ybdcqzhs += list.get(i) + ",";
					}
					ybdcqzhs = ybdcqzhs.substring(0, ybdcqzhs.length() - 1) + "  等" + (list.size() + "个");
				} else {
					ybdcqzhs = list.toString();
					if (!StringHelper.isEmpty(ybdcqzhs)) {
						ybdcqzhs = ybdcqzhs.substring(1, ybdcqzhs.length() - 1);
					}
				}
				return ybdcqzhs;
			} else {
				return ybdcqzhs;
			}
		}

		// 获取预测户预告登记、抵押状态、查封状态 liangc
		public Map<String, String> getDYandCFand_XZY(String djdyid) {
			Map<String, String> map = new HashMap<String, String>();
			String sqlMortgage = MessageFormat.format(" from BDCK.BDCS_QL_XZ where djdyid=''{0}'' and qllx=''23''",
					djdyid);
			String sqlSeal = MessageFormat
					.format(" from BDCK.BDCS_QL_XZ where djdyid=''{0}'' and djlx=''800'' and qllx=''99''", djdyid);
			String sqlYgdj = MessageFormat.format(" from BDCK.BDCS_QL_XZ where djdyid=''{0}'' and djlx=''700'' ",
					djdyid);

			long mortgageCount = dao.getCountByFullSql(sqlMortgage);
			long SealCount = dao.getCountByFullSql(sqlSeal);
			long YgdjCount = dao.getCountByFullSql(sqlYgdj);

			String mortgageStatus = mortgageCount > 0 ? "期房已抵押" : "期房无抵押";
			String sealStatus = SealCount > 0 ? "期房已查封" : "期房无查封";
			String ygdjStatus = YgdjCount > 0 ? "期房已办理预告登记" : "期房未办理预告登记";

			// 判断完现状层中的查封信息，接着判断办理中的查封信息
			if (!(SealCount > 0)) {
				String sqlSealing = " FROM bdck.bdcs_xmxx a LEFT JOIN bdck.bdcs_djdy_gz b ON a.xmbh=b.xmbh AND a.sfdb='0' LEFT JOIN bdck.bdcs_ql_gz c ON b.xmbh=c.xmbh and  b.djdyid=c.djdyid WHERE c.djlx='800' AND c.qllx='99' and c.djdyid='"
						+ djdyid + "' and a.sfdb='0' ";
				long count = dao.getCountByFullSql(sqlSealing);
				sealStatus = count > 0 ? "期房查封办理中" : "期房无查封";
			}

			// 判断完现状层中的查封信息，接着判断办理中的查封信息
			if (!(mortgageCount > 0)) {
				String sqlmortgageing = " FROM bdck.bdcs_xmxx a LEFT JOIN bdck.bdcs_djdy_gz b ON a.xmbh=b.xmbh AND a.sfdb='0' LEFT JOIN bdck.bdcs_ql_gz c ON b.xmbh=c.xmbh and b.djdyid=c.djdyid WHERE c.djlx='100' AND c.qllx='23' and c.djdyid='"
						+ djdyid + "' and a.sfdb='0' ";
				long count = dao.getCountByFullSql(sqlmortgageing);
				mortgageStatus = count > 0 ? "期房抵押办理中" : "期房无抵押";
			}

			if (!(YgdjCount > 0)) {
				String sqlmortgageing = " FROM bdck.bdcs_xmxx a LEFT JOIN bdck.bdcs_djdy_gz b ON a.xmbh=b.xmbh AND a.sfdb='0' LEFT JOIN bdck.bdcs_ql_gz c ON b.xmbh=c.xmbh and b.djdyid=c.djdyid WHERE c.djlx='700' AND c.qllx='4' and c.djdyid='"
						+ djdyid + "' and a.sfdb='0' ";
				long count = dao.getCountByFullSql(sqlmortgageing);
				ygdjStatus = count > 0 ? "期房预告登记办理中" : "期房无办理中的预告登记";
			}

			map.put("DYZTFLAG", String.valueOf(mortgageCount));
			map.put("CFZTFLAG", String.valueOf(SealCount));
			map.put("DYZT", mortgageStatus);
			map.put("CFZT", sealStatus);
			map.put("YGZT", ygdjStatus);

			return map;

		}

		// 获取实测户预告登记、抵押状态、查封状态 liangc
		public Map<String, String> getDYandCFand_XZ(String xmbh, String djdyid, String houseid) {
			Map<String, String> map = new HashMap<String, String>();
			BDCS_XMXX xm = Global.getXMXXbyXMBH(xmbh);
			String sqlMortgage = MessageFormat.format(" from BDCK.BDCS_QL_XZ where djdyid=''{0}'' and qllx=''23''",
					djdyid);
			String sqlSeal = MessageFormat
					.format(" from BDCK.BDCS_QL_XZ where djdyid=''{0}'' and djlx=''800'' and qllx=''99''", djdyid);
			String sqlYgdj = MessageFormat.format(" from BDCK.BDCS_QL_XZ where djdyid=''{0}'' and djlx=''700'' ",
					djdyid);

			long mortgageCount = dao.getCountByFullSql(sqlMortgage);
			long SealCount = dao.getCountByFullSql(sqlSeal);
			long YgdjCount = dao.getCountByFullSql(sqlYgdj);

			String mortgageStatus = mortgageCount > 0 ? "已抵押" : "无抵押";
			String sealStatus = SealCount > 0 ? "已查封" : "无查封";
			String ygdjStatus = YgdjCount > 0 ? "已办理预告登记" : "未办理预告登记";

			// 判断完现状层中的查封信息，接着判断办理中的查封信息
			if (!(SealCount > 0)) {
				String sqlSealing = " FROM bdck.bdcs_xmxx a LEFT JOIN bdck.bdcs_djdy_gz b ON a.xmbh=b.xmbh AND a.sfdb='0' LEFT JOIN bdck.bdcs_ql_gz c ON b.xmbh=c.xmbh and  b.djdyid=c.djdyid WHERE c.djlx='800' AND c.qllx='99' and c.djdyid='"
						+ djdyid + "' and a.sfdb='0' ";
				long count = dao.getCountByFullSql(sqlSealing);
				sealStatus = count > 0 ? "查封办理中" : "无查封";
			}

			// 判断完现状层中的查封信息，接着判断办理中的查封信息
			if (!(mortgageCount > 0)) {
				String sqlmortgageing = " FROM bdck.bdcs_xmxx a LEFT JOIN bdck.bdcs_djdy_gz b ON a.xmbh=b.xmbh AND a.sfdb='0' LEFT JOIN bdck.bdcs_ql_gz c ON b.xmbh=c.xmbh and b.djdyid=c.djdyid WHERE c.djlx='100' AND c.qllx='23' and c.djdyid='"
						+ djdyid + "' and a.sfdb='0' ";
				long count = dao.getCountByFullSql(sqlmortgageing);
				mortgageStatus = count > 0 ? "抵押办理中" : "无抵押";
			}
			if (!(YgdjCount > 0)) {
				String sqlmortgageing = " FROM bdck.bdcs_xmxx a LEFT JOIN bdck.bdcs_djdy_gz b ON a.xmbh=b.xmbh AND a.sfdb='0' LEFT JOIN bdck.bdcs_ql_gz c ON b.xmbh=c.xmbh and b.djdyid=c.djdyid WHERE c.djlx='700' AND c.qllx='4' and c.djdyid='"
						+ djdyid + "' and a.sfdb='0' ";
				long count = dao.getCountByFullSql(sqlmortgageing);
				ygdjStatus = count > 0 ? "预告登记办理中" : "无办理中的预告登记";
			}

			// 首次登记获取期房时的权利信息 liangq
			if ("100".equals(xm.getDJLX())) {
				String hy_sql = "select * from bdck.bdcs_h_xzy where bdcdyid "
						+ " in (select ycbdcdyid from bdck.YC_SC_H_XZ where scbdcdyid = '" + houseid + "')";
				List<Map> hys = dao.getDataListByFullSql(hy_sql);
				if (hys.size() > 0) {
					sqlMortgage = MessageFormat.format(" from BDCK.BDCS_QL_XZ where bdcdyh =''{0}'' and qllx=''23''",
							hys.get(0).get("BDCDYH"));
					sqlSeal = MessageFormat.format(
							" from BDCK.BDCS_QL_XZ where bdcdyh =''{0}'' and djlx=''800'' and qllx=''99''",
							hys.get(0).get("BDCDYH"));
					sqlYgdj = MessageFormat.format(
							" from BDCK.BDCS_QL_XZ where bdcdyh =''{0}'' and djlx=''700'' and qllx <> '23' ",
							hys.get(0).get("BDCDYH"));

					mortgageCount = dao.getCountByFullSql(sqlMortgage);
					SealCount = dao.getCountByFullSql(sqlSeal);
					YgdjCount = dao.getCountByFullSql(sqlYgdj);

					mortgageStatus = mortgageCount > 0 ? "期房有过抵押" : "期房无抵押";
					sealStatus = SealCount > 0 ? "期房有过查封" : "期房无查封";
					ygdjStatus = YgdjCount > 0 ? "期房办理过预告登记" : "期房未办理预告登记";
				} else {
					mortgageCount = 0;
					SealCount = 0;
					mortgageStatus = "期房无抵押";
					sealStatus = "期房无查封";
					ygdjStatus = "期房未办理预告登记";
				}
			}

			map.put("DYZTFLAG", String.valueOf(mortgageCount));
			map.put("CFZTFLAG", String.valueOf(SealCount));
			map.put("DYZT", mortgageStatus);
			map.put("CFZT", sealStatus);
			map.put("YGZT", ygdjStatus);
			return map;
		}

		// 获取宗地抵押、查封状态 liangc
		@SuppressWarnings({ "unchecked", "rawtypes" })
		public Map<String, String> getZD_DYandCFand_XZ(String djdyid) {
			Map<String, String> map = new HashMap<String, String>();

			if (djdyid != null) {
				String sqlMortgage = MessageFormat.format(" from BDCK.BDCS_QL_XZ where djdyid=''{0}'' and qllx=''23''",
						djdyid);
				String sqlSeal = MessageFormat
						.format(" from BDCK.BDCS_QL_XZ where djdyid=''{0}'' and djlx=''800'' and qllx=''99''", djdyid);
				long mortgageCount = dao.getCountByFullSql(sqlMortgage);
				long SealCount = dao.getCountByFullSql(sqlSeal);

				String mortgageStatus = mortgageCount > 0 ? "已抵押" : "无抵押";
				String sealStatus = SealCount > 0 ? "已查封" : "无查封";

				// 判断完现状层中的查封信息，接着判断办理中的查封信息
				if (!(SealCount > 0)) {
					String sqlSealing = " FROM bdck.bdcs_xmxx a LEFT JOIN bdck.bdcs_djdy_gz b ON a.xmbh=b.xmbh AND a.sfdb='0' LEFT JOIN bdck.bdcs_ql_gz c ON b.xmbh=c.xmbh and  b.djdyid=c.djdyid WHERE c.djlx='800' AND c.qllx='99' and c.djdyid='"
							+ djdyid + "' and a.sfdb='0' ";
					long count = dao.getCountByFullSql(sqlSealing);
					sealStatus = count > 0 ? "查封办理中" : "无查封";
				}

				// 判断完现状层中的查封信息，接着判断办理中的查封信息
				if (!(mortgageCount > 0)) {
					String sqlmortgageing = " FROM bdck.bdcs_xmxx a LEFT JOIN bdck.bdcs_djdy_gz b ON a.xmbh=b.xmbh AND a.sfdb='0' LEFT JOIN bdck.bdcs_ql_gz c ON b.xmbh=c.xmbh and b.djdyid=c.djdyid WHERE c.djlx='100' AND c.qllx='23' and c.djdyid='"
							+ djdyid + "' and a.sfdb='0' ";
					long count = dao.getCountByFullSql(sqlmortgageing);
					mortgageStatus = count > 0 ? "抵押办理中" : "无抵押";
				}

				map.put("DYZT", mortgageStatus);
				map.put("CFZT", sealStatus);
			}
			return map;
		}
		/**
		 * 宗海加上抵押、查封、预告登记状态
		 * @author heks
		 * @date 2017-10-20  12:17:34
		 * @param djdyid
		 * @return
		 */
		public Map<String, String> getDYandCFandYY_XZ_SEA(String bdcdyid,String bdcdylx) {
			List<BDCS_DJDY_XZ> dy = dao.getDataList(BDCS_DJDY_XZ.class, " BDCDYID='"+bdcdyid+"'");
			Map<String, String> map = new HashMap<String, String>();
			long mortgageCount = 0;
			long SealCount = 0;
			long ObjectionCount = 0;
			long LimitCount = 0;
			long ygdjCount = 0;
			if(dy!=null && dy.size()>0){
				for (BDCS_DJDY_XZ djdy_xz : dy) {
					String djdyid_xz = djdy_xz.getDJDYID();
					
					String sqlMortgage = MessageFormat.format(
							" from BDCK.BDCS_QL_XZ where djdyid=''{0}'' and qllx=''23''",
							djdyid_xz);
					String sqlSeal = MessageFormat
							.format(" from BDCK.BDCS_QL_XZ where djdyid=''{0}'' and djlx=''800'' and qllx=''99''",
									djdyid_xz);
					String sqlObjection = MessageFormat
							.format("  from BDCK.BDCS_QL_XZ where djdyid=''{0}'' and djlx=''600'' ",
									djdyid_xz);
					String sqlYgdj = MessageFormat
							.format("  from BDCK.BDCS_QL_XZ where djdyid=''{0}'' and djlx=''700'' ",
									djdyid_xz);//查预告登记

					String sqlLimit = MessageFormat
							.format("  from BDCK.BDCS_DYXZ where bdcdyid=''{0}'' and yxbz=''1'' ",
									djdyid_xz);
					 mortgageCount += dao.getCountByFullSql(sqlMortgage);//抵押
					 SealCount += dao.getCountByFullSql(sqlSeal);//查封
					 ObjectionCount += dao.getCountByFullSql(sqlObjection);//异议
					 LimitCount += dao.getCountByFullSql(sqlLimit);//限制
					 ygdjCount += dao.getCountByFullSql(sqlYgdj);//预告
					 
					 
					
					// 判断完现状层中的查封信息，接着判断办理中的查封信息
					/*if (!(SealCount > 0)) {
						String sqlSealing = " FROM bdck.bdcs_xmxx a LEFT JOIN bdck.bdcs_djdy_gz b ON a.xmbh=b.xmbh AND a.sfdb='0' LEFT JOIN bdck.bdcs_ql_gz c ON b.xmbh=c.xmbh and  b.djdyid=c.djdyid WHERE c.djlx='800' AND c.qllx='99' and c.djdyid='"
								+ djdy_xz + "' and a.sfdb='0' ";
						long count = dao.getCountByFullSql(sqlSealing);
						sealStatus = count > 0 ? "海域查封办理中" : "海域无查封";
					}*/

					// 改为判断完查封 人后判断限制
					/*if (!(LimitCount > 0)) {
						String sqlLimiting = " FROM bdck.bdcs_xmxx a LEFT JOIN bdck.bdcs_djdy_gz b ON a.xmbh=b.xmbh AND a.sfdb='0' LEFT JOIN bdck.bdcs_dyxz c ON b.xmbh=c.xmbh and b.bdcdyid=c.bdcdyid WHERE c.yxbz='0' and c.bdcdyid='"
								+ bdcdyid + "' and a.sfdb='0' ";
						long count = dao.getCountByFullSql(sqlLimiting);
						LimitStatus = count > 0 ? "海域限制办理中" : "海域无限制";
					}*/

					// 判断完现状层中的查封信息，接着判断办理中的查封信息
					/*if (!(mortgageCount > 0)) {
						String sqlmortgageing = " FROM bdck.bdcs_xmxx a LEFT JOIN bdck.bdcs_djdy_gz b ON a.xmbh=b.xmbh AND a.sfdb='0' LEFT JOIN bdck.bdcs_ql_gz c ON b.xmbh=c.xmbh and b.djdyid=c.djdyid WHERE c.djlx='100' AND c.qllx='23' and c.djdyid='"
								+ djdy_xz + "' and a.sfdb='0' ";
						long count = dao.getCountByFullSql(sqlmortgageing);
						mortgageStatus = count > 0 ? "海域抵押办理中" : "海域无抵押";
					}*/

					// 预告登记
					/*if (!(ygdjCount > 0)) {
						String sqlygdj = " FROM bdck.bdcs_xmxx a LEFT JOIN bdck.bdcs_djdy_gz b ON a.xmbh=b.xmbh AND a.sfdb='0' LEFT JOIN bdck.bdcs_ql_gz c ON b.xmbh=c.xmbh and b.djdyid=c.djdyid WHERE c.djlx='700' and c.djdyid='"
								+ djdy_xz + "' and a.sfdb='0' ";
						long count = dao.getCountByFullSql(sqlygdj);
						ygdjStatus = count > 0 ? "海域预告登记中" : "海域无预告登记中";
					}*/


					
				}
				String sealStatus = "";
				String mortgageStatus = "";
				String ygdjStatus = "";
				String LimitStatus = "";
				String objectionStatus = "";
				if(bdcdylx.equals(BDCDYLX.HY.Value)){
					mortgageStatus = mortgageCount > 0 ? "海域已抵押（"+mortgageCount+"笔）" : "海域无抵押";
					sealStatus = SealCount > 0 ? "海域已查封（"+SealCount+"笔）" : "海域无查封";
					LimitStatus = LimitCount > 0 ? "海域已限制（"+LimitCount+"笔）" : "海域无限制";
					ygdjStatus = ygdjCount > 0 ? "海域已做预告登记" : "海域无预告登记";
					
					objectionStatus = ObjectionCount > 0 ? "海域有异议" : "海域无异议";
					
				}
				else if(bdcdylx.equals(BDCDYLX.NYD.Value)){
					mortgageStatus = mortgageCount > 0 ? "农用地已抵押（"+mortgageCount+"笔）" : "农用地无抵押";
					sealStatus = SealCount > 0 ? "农用地已查封（"+SealCount+"笔）" : "农用地无查封";
					LimitStatus = LimitCount > 0 ? "农用地已限制（"+LimitCount+"笔）" : "农用地无限制";
					ygdjStatus = ygdjCount > 0 ? "农用地已做预告登记" : "农用地无预告登记";
					
					objectionStatus = ObjectionCount > 0 ? "农用地有异议" : "农用地无异议";
				}
				map.put("DYZTFLAG", String.valueOf(mortgageCount));
				map.put("CFZTFLAG", String.valueOf(SealCount));
				map.put("DYZT", mortgageStatus);
				map.put("CFZT", sealStatus);
				map.put("YYZT", objectionStatus);
				map.put("XZZT", LimitStatus);
				map.put("YGDJZT", ygdjStatus);
			}

			return map;
		}

		private List<BDCS_DJDY_GZ> SortDJDY(List<BDCS_DJDY_GZ> listdjdy__old, List<RealUnit> units) {
			List<BDCS_DJDY_GZ> listdjdy__new = new ArrayList<BDCS_DJDY_GZ>();
			if (StringHelper.isEmpty(listdjdy__old) || listdjdy__old.size() <= 0) {
				return listdjdy__old;
			}
			for (RealUnit unit : units) {
				for (BDCS_DJDY_GZ djdy : listdjdy__old) {
					if (unit.getId().equals(djdy.getBDCDYID())) {
						listdjdy__new.add(djdy);
						break;
					}
				}
			}
			return listdjdy__new;
		}

		private List<RealUnit> SortUnit(List<RealUnit> units_old) {
			List<RealUnit> units_new = new ArrayList<RealUnit>();
			if (StringHelper.isEmpty(units_old) || units_old.size() <= 0) {
				return units_old;
			}

			for (RealUnit unit : units_old) {
				double mj = StringHelper.getDouble(unit.getMJ());
				int index = 0;
				String zl = unit.getZL();
				String fh = "";
				if (BDCDYLX.H.equals(unit.getBDCDYLX()) || BDCDYLX.YCH.equals(unit.getBDCDYLX())) {
					House h = (House) unit;
					if (!StringHelper.isEmpty(h)) {
						fh = h.getFH();
					}
				}
				if (StringHelper.isEmpty(zl) || zl.contains("地下室") || zl.contains("车库")
						|| (!StringHelper.isEmpty(fh) && (fh.contains("地下室") || fh.contains("车库")))) {
					if (units_new.size() > 0) {
						for (index = units_new.size() - 1; index >= 0; index--) {
							RealUnit unit1 = units_new.get(index);
							double mj1 = StringHelper.getDouble(unit1.getMJ());
							if (mj1 >= mj) {
								units_new.add(unit);
								break;
							} else {
								String zl1 = unit1.getZL();
								String fh1 = "";
								if (BDCDYLX.H.equals(unit1.getBDCDYLX()) || BDCDYLX.YCH.equals(unit1.getBDCDYLX())) {
									House h = (House) unit1;
									if (!StringHelper.isEmpty(h)) {
										fh1 = h.getFH();
									}
								}
								if (StringHelper.isEmpty(zl1) || zl1.contains("地下室") || zl1.contains("车库")
										|| (!StringHelper.isEmpty(fh1)
												&& (fh1.contains("地下室") || fh1.contains("车库")))) {
									if (index == 0) {
										units_new.add(0, unit);
										break;
									}
									continue;
								} else {
									if (index == units_new.size() - 1) {
										units_new.add(unit);
									} else {
										units_new.add(index + 1, unit);
									}
									break;
								}
							}
						}
					} else {
						units_new.add(unit);
					}
				} else {
					if (units_new.size() == 0) {
						units_new.add(unit);
					} else {
						for (RealUnit unit1 : units_new) {
							double mj1 = StringHelper.getDouble(unit1.getMJ());
							if (mj > mj1) {
								units_new.add(index, unit);
								break;
							} else {
								String zl1 = unit1.getZL();
								String fh1 = "";
								if (BDCDYLX.H.equals(unit1.getBDCDYLX()) || BDCDYLX.YCH.equals(unit1.getBDCDYLX())) {
									House h = (House) unit1;
									if (!StringHelper.isEmpty(h)) {
										fh1 = h.getFH();
									}
								}
								if (StringHelper.isEmpty(zl1) || zl1.contains("地下室") || zl1.contains("车库")
										|| (!StringHelper.isEmpty(fh1)
												&& (fh1.contains("地下室") || fh1.contains("车库")))) {
									units_new.add(index, unit);
									break;
								}
							}
							index++;
							if (index == units_new.size()) {
								units_new.add(unit);
								break;
							}
						}
					}
				}
			}
			return units_new;
		}

		@SuppressWarnings("rawtypes")
		BDCDY buildex(BDCS_DJDY_GZ djdy, String xmbh, boolean sqbflag) {
			ProjectInfo info = ProjectHelper.GetPrjInfoByXMBH(xmbh);
			BDCS_XMXX xmxx_bdck=Global.getXMXXbyXMBH(xmbh);
			String _djlx = info.getDjlx();
			String _bdcdylx = info.getBdcdylx();
			String _qllx= info.getQllx();
			/*if(DJLX.CSDJ.Value.equals(_djlx)
					&&(BDCDYLX.H.Value.equals(_bdcdylx)||BDCDYLX.YCH.Value.equals(_bdcdylx))
					&&("4".equals(_qllx)||"6".equals(_qllx)||"6".equals(_qllx))) {
				BDCDYLX lx = BDCDYLX.initFrom(djdy.getBDCDYLX());
				DJDYLY ly = DJDYLY.initFrom(djdy.getLY());
				this.temp_ybdcqzsh = new HashSet<String>();
				this.temp_bdcqzh = new HashSet<String>();
				// 从现状里查
				if (ly.equals(DJDYLY.XZ)) // 不能注释，否则登簿后打开审批表报错
					ly = DJDYLY.LS;
				RealUnit unit = UnitTools.loadUnit(lx, ly, djdy.getBDCDYID());
				this.bdcdyh = unit.getBDCDYH();// 不动产单元号
				this.zl = unit.getZL();// 坐落
				this.mj = StringHelper.formatObject(unit.getMJ());// 面积
				this.bdclx = lx.Bdclx;
				if (unit instanceof UseLand)// 使用权宗地
				{
					UseLand useland = (UseLand) unit;
					if (!StringHelper.isEmpty(useland)) {
						List<TDYT> lsttdyt = useland.getTDYTS();
						boolean flag = true;
						String strtdyt = "";
						for (TDYT tdyt : lsttdyt) {
							if (flag) {
								strtdyt = tdyt.getTDYTMC();
								flag = false;
							} else {
								strtdyt = strtdyt + "，" + tdyt.getTDYTMC();
							}
						}
						this.yt = strtdyt;
						this.qlxz = ConstHelper.getNameByValue("QLXZ", useland.getQLXZ());
						// 宗地查封状态，抵押状态liangc
					}
				}
				if (unit instanceof OwnerLand)// 所有权宗地
				{
					OwnerLand useland = (OwnerLand) unit;
					this.yt = ConstHelper.getNameByValue("TDYT", useland.getYT());
					this.qlxz = ConstHelper.getNameByValue("QLXZ", useland.getQLXZ());
				}
				if (unit instanceof AgriculturalLand)// 使用权宗地
				{
					AgriculturalLand agriculturalLand = (AgriculturalLand) unit;
					if (!StringHelper.isEmpty(agriculturalLand)) {
						List<TDYT> lsttdyt = agriculturalLand.getTDYTS();
						boolean flag = true;
						String strtdyt = "";
						for (TDYT tdyt : lsttdyt) {
							if (flag) {
								strtdyt = tdyt.getTDYTMC();
								flag = false;
							} else {
								strtdyt = strtdyt + "，" + tdyt.getTDYTMC();
							}
						}
						this.yt = strtdyt;
						this.qlxz = ConstHelper.getNameByValue("QLXZ", agriculturalLand.getQLXZ());
						// 宗地查封状态，抵押状态liangc
					}
				}
				if (unit instanceof House)// 房屋户
				{
					House house = (House) unit;
					this.bdclx = "土地/" + lx.Bdclx; // 为房屋添土地，更符合不动产的房地一体
					this.yt = ConstHelper.getNameByValue("FWYT", house.getGHYT());
					this.zrzh = house.getZRZH();
					this.fh = house.getFH();
					this.qlxz = ConstHelper.getNameByValue("FWXZ", house.getFWXZ());
					if (BDCDYLX.H.equals(lx)) {
						this.jzmj = StringHelper.formatObject(house.getSCJZMJ());
						this.tnmj = StringHelper.formatObject(house.getSCTNJZMJ());
						this.ftmj = StringHelper.formatObject(house.getSCFTJZMJ());
						this.fttdmj = StringHelper.formatObject(house.getFTTDMJ());
						this.tdftmj_fwjzmj = StringHelper.formatObject(house.getFTTDMJ()) + "/"
								+ StringHelper.formatObject(house.getSCJZMJ());
						// 实测户 预告登记状态、抵押状态、查封状态
					} else {
						this.jzmj = StringHelper.formatObject(house.getYCJZMJ());
						this.tnmj = StringHelper.formatObject(house.getYCTNJZMJ());
						this.ftmj = StringHelper.formatObject(house.getYCFTJZMJ());
						this.fttdmj = StringHelper.formatObject(house.getFTTDMJ());
						this.tdftmj_fwjzmj = StringHelper.formatObject(house.getFTTDMJ()) + "/"
								+ StringHelper.formatObject(house.getYCJZMJ());
						// 预测户 预告登记状态、抵押状态、查封状态

				}
				this.szcs = StringHelper.formatObject(house.getSZC());
				this.ghyt = ConstHelper.getNameByValue("FWYT", house.getGHYT());

					if(BDCDYLX.YCH.equals(lx)) {
						List<YC_SC_H_XZ> SY = dao.getDataList(YC_SC_H_XZ.class, " YCBDCDYID='" + house.getId() + "'");
						if (SY != null && SY.size() > 0) {
							this.fwztEx = "已关联现房";
							String QFsql = " FROM  BDCK.BDCS_QL_XZ QL  LEFT JOIN BDCK.BDCS_DJDY_XZ DJDY  ON QL.BDCDYH =DJDY.BDCDYH " + 
									"WHERE QL.QLLX='23'  AND QL.DJLX='700' AND QL.DJDYID IN "
									+ "(SELECT DJDY.DJDYID FROM BDCK.BDCS_DJDY_XZ DJDY WHERE DJDY.BDCDYID= '"+house.getId()+"')";
							long qfzt = dao.getCountByFullSql(QFsql);
							if (qfzt  > 0) {
								this.fwztEx += ",期房已抵押";
							}else {
								this.fwztEx += ",期房无抵押";
							}
							String XFsql = " FROM BDCK.BDCS_QL_XZ QL LEFT JOIN BDCK.BDCS_DJDY_XZ DJDY ON QL.BDCDYH =DJDY.BDCDYH " + 
									"WHERE QL.QLLX='23'  AND QL.DJDYID IN (SELECT DJDY.DJDYID FROM BDCK.BDCS_DJDY_XZ DJDY WHERE DJDY.BDCDYID = '"+SY.get(0).getSCBDCDYID()+"')";
									
							Long xfzt = dao.getCountByFullSql(XFsql);
							if (xfzt > 0) {
									this.fwztEx += ",现房已抵押";
							}else {
								this.fwztEx += ",现房无抵押";
							}
						}else {
							this.fwztEx = "未关联现房";
							String QFsql = " FROM  BDCK.BDCS_QL_XZ QL  LEFT JOIN BDCK.BDCS_DJDY_XZ DJDY  ON QL.BDCDYH =DJDY.BDCDYH " + 
									"WHERE QL.QLLX='23'  AND QL.DJLX='700' AND QL.DJDYID IN "
									+ "(SELECT DJDY.DJDYID FROM BDCK.BDCS_DJDY_XZ DJDY WHERE DJDY.BDCDYID= '"+house.getId()+"')";
							long qfzt = dao.getCountByFullSql(QFsql);
							if (qfzt  > 0) {
								this.fwztEx += ",期房已抵押";
							}else {
								this.fwztEx += ",期房无抵押";
							}
						}
						
					}else if (BDCDYLX.H.equals(lx)) {
						List<YC_SC_H_XZ> SY = dao.getDataList(YC_SC_H_XZ.class, " SCBDCDYID='" + house.getId() + "'");
						if (SY != null && SY.size() > 0) {
							this.fwztEx = "已关联期房";
							String QFsql = "  FROM  BDCK.BDCS_QL_XZ QL  LEFT JOIN BDCK.BDCS_DJDY_XZ DJDY  ON QL.BDCDYH =DJDY.BDCDYH " + 
									"WHERE QL.QLLX='23'  AND QL.DJLX='700' AND QL.DJDYID IN"
									+ " (SELECT DJDY.DJDYID FROM BDCK.BDCS_DJDY_XZ DJDY WHERE DJDY.BDCDYID= '"+ SY.get(0).getYCBDCDYID() +"')";
							Long qfzt = dao.getCountByFullSql(QFsql);
							if (qfzt  > 0) {
								this.fwztEx += ",期房已抵押";
							}else {
								this.fwztEx += ",期房无抵押";
							}
							String XFsql = "FROM BDCK.BDCS_QL_XZ QL LEFT JOIN BDCK.BDCS_DJDY_XZ DJDY ON QL.BDCDYH =DJDY.BDCDYH  " + 
									"WHERE QL.QLLX='23'   AND QL.DJDYID IN" + 
								" (SELECT DJDY.DJDYID FROM BDCK.BDCS_DJDY_XZ DJDY WHERE DJDY.BDCDYID = '"+  house.getId() +"')";
							Long xfzt = dao.getCountByFullSql(XFsql);
							if (xfzt > 0) {
								this.fwztEx += ",现房已抵押";
								}else {
								this.fwztEx += ",现房无抵押";
							}
						}else {
							this.fwztEx = "未关联期房";
							String XFsql = "FROM BDCK.BDCS_QL_XZ QL LEFT JOIN BDCK.BDCS_DJDY_XZ DJDY ON QL.BDCDYH =DJDY.BDCDYH  " + 
									"WHERE QL.QLLX='23'   AND QL.DJDYID IN" + 
								" (SELECT DJDY.DJDYID FROM BDCK.BDCS_DJDY_XZ DJDY WHERE DJDY.BDCDYID = '"+  house.getId() +"')";
							Long xfzt = dao.getCountByFullSql(XFsql);
							if (xfzt > 0) {
								this.fwztEx += ",现房已抵押";
								}else {
								this.fwztEx += ",现房无抵押";
							}
						}
					}else {
						this.fwztEx = "期现房未关联";
						}
					if (StringHelper.isEmpty(fwzrzzl)) {
						String zrzbdcdyid = ((House) unit).getZRZBDCDYID();
						if (!StringHelper.isEmpty(zrzbdcdyid)) {
							RealUnit zrz = UnitTools.loadUnit(BDCDYLX.ZRZ, DJDYLY.XZ, zrzbdcdyid);
							if (zrz != null) {
								fwzrzzl = zrz.getZL();
							}
						}
					}
				}
				if (unit instanceof Building)// 自然幢
				{
					Building building = (Building) unit;
					this.yt = ConstHelper.getNameByValue("FWYT", building.getGHYT());
				}
				if (unit instanceof Sea)// 宗海
				{
					Sea sea = (Sea) unit;
					String yhlxa = ConstHelper.getNameByValue("HYSYLXA",sea.getYHLXA());
					if (StringHelper.isEmpty(yhlxa)) {
						yhlxa = "----";
					}
					String yhlxb = ConstHelper.getNameByValue("HYSYLXB",sea.getYHLXB());
					if (StringHelper.isEmpty(yhlxb)) {
						yhlxb = "----";
					}
					this.yhlx = yhlxa + "/" + yhlxb;
				}
				if (unit instanceof Forest) // 林地
				{
					Forest forest = (Forest) unit;
					this.lz = ConstHelper.getNameByValue("LZ", forest.getLZ());
					this.yt = ConstHelper.getNameByValue("TDYT", forest.getTDYT());
				}
				// 获取单元权利产权信息
				if (true) {
					String qllxarray = " ('1','2','3','4','5','6','7','8','15','24')";
					List<BDCS_QL_GZ> gzqls = dao.getDataList(BDCS_QL_GZ.class,
							" djdyid='" + djdy.getDJDYID() + "' and xmbh='" + xmbh + "' and qllx in" + qllxarray);
					if (gzqls != null && gzqls.size() > 0) {
						BDCS_QL_GZ gzql = gzqls.get(0);
						if (gzql != null) {
							this.bdcqzh = gzql.getBDCQZH();
							if (StringHelper.isEmpty(this.bdcqzh) && !StringHelper.isEmpty(gzql.getLYQLID())) {
								Rights right = RightsTools.loadRights(DJDYLY.LS, gzql.getLYQLID());
								if (right != null) {
									this.bdcqzh = right.getBDCQZH();
								}
							}
							this.bdcqzhEx = gzql.getBDCQZH();
							this.mainqllx = gzql.getQLLXName();
							String qlqssj = StringHelper.FormatDateOnType(gzql.getQLQSSJ(), "yyyy-MM-dd");
							if (StringHelper.isEmpty(qlqssj)) {
								qlqssj = "----";
							}
							String qljssj = StringHelper.FormatDateOnType(gzql.getQLJSSJ(), "yyyy-MM-dd");
							if (StringHelper.isEmpty(qljssj)) {
								qljssj = "----";
							}
							this.syqx = qlqssj + "起 " + qljssj + "止";
							List<BDCS_QLR_GZ> qlrs = dao.getDataList(BDCS_QLR_GZ.class, "QLID='" + gzql.getId() + "'");
							if (qlrs != null && qlrs.size() > 0) {
								this.gyqk = qlrs.get(0).getGYQK();
							}
						}
					} else {
						List<BDCS_QL_XZ> xzqls = dao.getDataList(BDCS_QL_XZ.class,
								" djdyid='" + djdy.getDJDYID() + "' and qllx in" + qllxarray);
						if (xzqls != null && xzqls.size() > 0) {
							BDCS_QL_XZ xzql = xzqls.get(0);
							if (xzql != null) {
								this.mainqllx = xzql.getQLLXName();
								String qlqssj = StringHelper.FormatDateOnType(xzql.getQLQSSJ(), "yyyy-MM-dd");
								if (StringHelper.isEmpty(qlqssj)) {
									qlqssj = "----";
								}
								String qljssj = StringHelper.FormatDateOnType(xzql.getQLJSSJ(), "yyyy-MM-dd");
								if (StringHelper.isEmpty(qljssj)) {
									qljssj = "----";
								}
								this.syqx = qlqssj + "起 " + qljssj + "止";
								List<BDCS_QLR_XZ> qlrs = dao.getDataList(BDCS_QLR_XZ.class, "QLID='" + xzql.getId() + "'");
								if (qlrs != null && qlrs.size() > 0) {
									this.gyqk = qlrs.get(0).getGYQK();
								}
							}
						}
					}
				}

				if (!StringHelper.isEmpty(this.temp_ybdcqzsh) && this.temp_ybdcqzsh.size() > 0) {
					this.ybdcqzsh = this.temp_ybdcqzsh.toArray()[0].toString();
				}
				if (StringHelper.isEmpty(this.tdftmj_fwjzmj)) {
					this.tdftmj_fwjzmj = this.mj;
				}
				return this;
			
			}*///else {

				String xmbhcondition = ProjectHelper.GetXMBHCondition(xmbh);
				BDCDYLX lx = BDCDYLX.initFrom(djdy.getBDCDYLX());
				DJDYLY ly = DJDYLY.initFrom(djdy.getLY());
				this.temp_ybdcqzsh = new HashSet<String>();
				this.temp_bdcqzh = new HashSet<String>();
				// 从现状里查
				if (ly.equals(DJDYLY.XZ)) // 不能注释，否则登簿后打开审批表报错
					ly = DJDYLY.LS;
				RealUnit unit = UnitTools.loadUnit(lx, ly, djdy.getBDCDYID());
				this.bdcdyh = unit.getBDCDYH();// 不动产单元号
				this.zl = unit.getZL();// 坐落
				this.mj = StringHelper.formatObject(unit.getMJ());// 面积
				this.bdclx = lx.Bdclx;
				if (unit instanceof UseLand)// 使用权宗地
				{
					UseLand useland = (UseLand) unit;
					if (!StringHelper.isEmpty(useland)) {
						List<TDYT> lsttdyt = useland.getTDYTS();
						boolean flag = true;
						String strtdyt = "";
						for (TDYT tdyt : lsttdyt) {
							if (flag) {
								strtdyt = tdyt.getTDYTMC();
								flag = false;
							} else {
								strtdyt = strtdyt + "，" + tdyt.getTDYTMC();
							}
						}
						this.yt = strtdyt;
						this.qlxz = ConstHelper.getNameByValue("QLXZ", useland.getQLXZ());
						// 宗地查封状态，抵押状态liangc
						this.cfzt = getZD_DYandCFand_XZ(djdy.getDJDYID()).get("CFZT");
						this.dyzt = getZD_DYandCFand_XZ(djdy.getDJDYID()).get("DYZT");
					}
				}
				if (unit instanceof OwnerLand)// 所有权宗地
				{
					String ytClassName = "BDCS_TDYT" + unit.getLY().TableSuffix;
					String packageName = "com.supermap.realestate.registration.model.";
					Class<?> ytClass = null;
					try {
						ytClass = Class.forName(packageName + ytClassName);
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					}
					@SuppressWarnings("unchecked")
					List<TDYT> ytList = (List<TDYT>) dao.getDataList(ytClass, "BDCDYID='" + unit.getId() + "' ORDER BY SFZYT DESC");
					OwnerLand useland = (OwnerLand) unit;
					//所有权宗地
//					List<BDCS_TDYT_GZ> ytList = dao.getDataList(BDCS_TDYT_GZ.class, "bdcdyid='"+unit.getId()+"'");
					List<String> tdytmclist = new ArrayList<String>();
					if(ytList!=null&&ytList.size()>0) {
						for (TDYT yt:ytList) {
							String ytname = yt.getTDYT();
							if (!StringHelper.isEmpty(yt)&&xmxx!=null) {
								String tdytmc = ConstHelper.getNameByValue_new("TDYT",ytname.trim(),xmxx_bdck.getDJSJ()+"");
								if (!StringHelper.isEmpty(tdytmc)
										&& !tdytmclist.contains(tdytmc)) {
									tdytmclist.add(tdytmc);
								}
							}
						}
						if (tdytmclist != null && tdytmclist.size() > 0) {
							this.yt  = StringHelper.formatList(tdytmclist, "、");
						}
					}
					
					//this.yt = ConstHelper.getNameByValue("TDYT", useland.getYT());
					this.qlxz = ConstHelper.getNameByValue("QLXZ", useland.getQLXZ());
					this.cfzt = getZD_DYandCFand_XZ(djdy.getDJDYID()).get("CFZT");
					this.dyzt = getZD_DYandCFand_XZ(djdy.getDJDYID()).get("DYZT");
				}
				if (unit instanceof AgriculturalLand)// 农用地
				{
					AgriculturalLand agrland = (AgriculturalLand) unit;
					if (!StringHelper.isEmpty(agrland)) {
						List<BDCS_TDYT_XZ> lsttdyt = dao.getDataList(BDCS_TDYT_XZ.class, "BDCDYID='" + djdy.getBDCDYID() + "' ORDER BY SFZYT DESC");
						boolean flag = true;
						String strtdyt = "";
						for (TDYT tdyt : lsttdyt) {
							if (flag) {
								strtdyt = tdyt.getTDYTMC();
								flag = false;
							} else {
								strtdyt = strtdyt + "、" + tdyt.getTDYTMC();
							}
						}
						if (StringHelper.isEmpty(strtdyt)) {
							CommonDao commonDao = SuperSpringContext.getContext().getBean(CommonDao.class);
							String ytClassName = EntityTools.getEntityName("BDCS_TDYT", DJDYLY.DC);
							Class<?> ytClass = EntityTools.getEntityClass(ytClassName);
							List<TDYT> listyts = (List<TDYT>) commonDao.getDataList(ytClass, "BDCDYID='" + djdy.getBDCDYID() + "'");
							String yt ="";
							for (int j = 0; j < listyts.size(); j++) {
								yt += listyts.get(j).getTDYTMC();
								if (j+1 < listyts.size()) {
									yt +="、";
								}
							}
							this.yt= yt;
						}else {
							if (strtdyt.indexOf("0")!=-1) {//用途为数字时，匹配数据字典
								this.yt= ConstHelper.getNameByValue("TDYT",strtdyt);
							}else {
								this.yt= strtdyt;
							}
						}
//						this.yt = strtdyt;
						this.qlxz = ConstHelper.getNameByValue("QLXZ", agrland.getQLXZ());
						/*
						 * 给农用地加上抵押、查封、预告状态
						 */
						this.dyzt = getDYandCFandYY_XZ_SEA(djdy.getBDCDYID(),djdy.getBDCDYLX()).get("DYZT");
						this.cfzt = getDYandCFandYY_XZ_SEA(djdy.getBDCDYID(),djdy.getBDCDYLX()).get("CFZT");
						this.ygzt = getDYandCFandYY_XZ_SEA(djdy.getBDCDYID(),djdy.getBDCDYLX()).get("YGDJZT");
					}
				}
				if (unit instanceof House)// 房屋户
				{
					House house = (House) unit;
					this.bdclx = "土地/" + lx.Bdclx; // 为房屋添土地，更符合不动产的房地一体
					this.yt = ConstHelper.getNameByValue("FWYT", house.getGHYT());
					this.zrzh = house.getZRZH();
					this.fh = house.getFH();
					this.qlxz = ConstHelper.getNameByValue("FWXZ", house.getFWXZ());
					if (BDCDYLX.H.equals(lx)) {
						this.jzmj = StringHelper.formatObject(house.getSCJZMJ());
						this.tnmj = StringHelper.formatObject(house.getSCTNJZMJ());
						this.ftmj = StringHelper.formatObject(house.getSCFTJZMJ());
						this.fttdmj = StringHelper.formatObject(house.getFTTDMJ());
						this.tdftmj_fwjzmj = StringHelper.formatObject(house.getFTTDMJ()) + "/"
								+ StringHelper.formatObject(house.getSCJZMJ());
						// 实测户 预告登记状态、抵押状态、查封状态
						this.cfzt = getDYandCFand_XZ(xmbh, djdy.getDJDYID(), house.getId()).get("CFZT");
						this.dyzt = getDYandCFand_XZ(xmbh, djdy.getDJDYID(), house.getId()).get("DYZT");
						this.ygzt = getDYandCFand_XZ(xmbh, djdy.getDJDYID(), house.getId()).get("YGZT");
					} else {
						this.jzmj = StringHelper.formatObject(house.getYCJZMJ());
						this.tnmj = StringHelper.formatObject(house.getYCTNJZMJ());
						this.ftmj = StringHelper.formatObject(house.getYCFTJZMJ());
						this.fttdmj = StringHelper.formatObject(house.getFTTDMJ());
						this.tdftmj_fwjzmj = StringHelper.formatObject(house.getFTTDMJ()) + "/"
								+ StringHelper.formatObject(house.getYCJZMJ());
						// 预测户 预告登记状态、抵押状态、查封状态
						this.cfzt = getDYandCFand_XZY(djdy.getDJDYID()).get("CFZT");
						this.dyzt = getDYandCFand_XZY(djdy.getDJDYID()).get("DYZT");
						this.ygzt = getDYandCFand_XZY(djdy.getDJDYID()).get("YGZT");

					}
					this.szcs = StringHelper.formatObject(house.getSZC());
					this.ghyt = ConstHelper.getNameByValue("FWYT", house.getGHYT());

					if(BDCDYLX.YCH.equals(lx)) {
						List<YC_SC_H_XZ> SY = dao.getDataList(YC_SC_H_XZ.class, " YCBDCDYID='" + house.getId() + "'");
						if (SY != null && SY.size() > 0) {
							this.fwztEx = "已关联现房";
							String QFsql = " FROM  BDCK.BDCS_QL_XZ QL  LEFT JOIN BDCK.BDCS_DJDY_XZ DJDY  ON QL.BDCDYH =DJDY.BDCDYH " + 
									"WHERE QL.QLLX='23'  AND QL.DJLX='700' AND QL.DJDYID IN "
									+ "(SELECT DJDY.DJDYID FROM BDCK.BDCS_DJDY_XZ DJDY WHERE DJDY.BDCDYID= '"+house.getId()+"')";
							long qfzt = dao.getCountByFullSql(QFsql);
							if (qfzt  > 0) {
								this.fwztEx += ",期房已抵押";
							}else {
								this.fwztEx += ",期房无抵押";
							}
							String XFsql = " FROM BDCK.BDCS_QL_XZ QL LEFT JOIN BDCK.BDCS_DJDY_XZ DJDY ON QL.BDCDYH =DJDY.BDCDYH " + 
									"WHERE QL.QLLX='23'  AND QL.DJDYID IN (SELECT DJDY.DJDYID FROM BDCK.BDCS_DJDY_XZ DJDY WHERE DJDY.BDCDYID = '"+SY.get(0).getSCBDCDYID()+"')";
									
							Long xfzt = dao.getCountByFullSql(XFsql);
							if (xfzt > 0) {
									this.fwztEx += ",现房已抵押";
							}else {
								this.fwztEx += ",现房无抵押";
							}
						}else {
							this.fwztEx = "未关联现房";
							String QFsql = " FROM  BDCK.BDCS_QL_XZ QL  LEFT JOIN BDCK.BDCS_DJDY_XZ DJDY  ON QL.BDCDYH =DJDY.BDCDYH " + 
									"WHERE QL.QLLX='23'  AND QL.DJLX='700' AND QL.DJDYID  IN "
									+ "(SELECT DJDY.DJDYID FROM BDCK.BDCS_DJDY_XZ DJDY WHERE DJDY.BDCDYID= '"+house.getId()+"')";
							long qfzt = dao.getCountByFullSql(QFsql);
							if (qfzt  > 0) {
								this.fwztEx += ",期房已抵押";
							}else {
								this.fwztEx += ",期房无抵押";
							}
						}
						
					}else if (BDCDYLX.H.equals(lx)) {
						List<YC_SC_H_XZ> SY = dao.getDataList(YC_SC_H_XZ.class, " SCBDCDYID='" + house.getId() + "'");
						if (SY != null && SY.size() > 0) {
							this.fwztEx = "已关联期房";
							String QFsql = "  FROM  BDCK.BDCS_QL_XZ QL  LEFT JOIN BDCK.BDCS_DJDY_XZ DJDY  ON QL.BDCDYH =DJDY.BDCDYH " + 
									"WHERE QL.QLLX='23'  AND QL.DJLX='700' AND QL.DJDYID IN "
									+ " (SELECT DJDY.DJDYID FROM BDCK.BDCS_DJDY_XZ DJDY WHERE DJDY.BDCDYID= '"+ SY.get(0).getYCBDCDYID() +"')";
							Long qfzt = dao.getCountByFullSql(QFsql);
							if (qfzt  > 0) {
								this.fwztEx += ",期房已抵押";
							}else {
								this.fwztEx += ",期房无抵押";
							}
							String XFsql = "FROM BDCK.BDCS_QL_XZ QL LEFT JOIN BDCK.BDCS_DJDY_XZ DJDY ON QL.BDCDYH =DJDY.BDCDYH  " + 
									"WHERE QL.QLLX='23'   AND QL.DJDYID  IN" + 
								" (SELECT DJDY.DJDYID FROM BDCK.BDCS_DJDY_XZ DJDY WHERE DJDY.BDCDYID = '"+  house.getId() +"')";
							Long xfzt = dao.getCountByFullSql(XFsql);
							if (xfzt > 0) {
								this.fwztEx += ",现房已抵押";
								}else {
								this.fwztEx += ",现房无抵押";
							}
						}else {
							this.fwztEx = "未关联期房";
							String XFsql = "FROM BDCK.BDCS_QL_XZ QL LEFT JOIN BDCK.BDCS_DJDY_XZ DJDY ON QL.BDCDYH =DJDY.BDCDYH  " + 
									"WHERE QL.QLLX='23'   AND QL.DJDYID  IN" + 
								" (SELECT DJDY.DJDYID FROM BDCK.BDCS_DJDY_XZ DJDY WHERE DJDY.BDCDYID = '"+  house.getId() +"')";
							Long xfzt = dao.getCountByFullSql(XFsql);
							if (xfzt > 0) {
								this.fwztEx += ",现房已抵押";
								}else {
								this.fwztEx += ",现房无抵押";
							}
						}
					}else {
						this.fwztEx = "期现房未关联";
						}
					if (StringHelper.isEmpty(fwzrzzl)) {
						String zrzbdcdyid = ((House) unit).getZRZBDCDYID();
						if (!StringHelper.isEmpty(zrzbdcdyid)) {
							RealUnit zrz = UnitTools.loadUnit(BDCDYLX.ZRZ, DJDYLY.XZ, zrzbdcdyid);
							if (zrz != null) {
								fwzrzzl = zrz.getZL();
							}
						}
					}
				}
				if (unit instanceof Building)// 自然幢
				{
					Building building = (Building) unit;
					this.yt = ConstHelper.getNameByValue("FWYT", building.getGHYT());
				}
				if (unit instanceof Sea)// 宗海
				{
					Sea sea = (Sea) unit;
//					this.yhlx = ConstHelper.getNameByValue("HYSYLXA", sea.getYHLXA());
//					this.yt = ConstHelper.getNameByValue("HYSYLXB", sea.getYHLXB());
					String yhlxa = ConstHelper.getNameByValue("HYSYLXA",sea.getYHLXA());
					if (StringHelper.isEmpty(yhlxa)) {
						yhlxa = "----";
					}
					String yhlxb = ConstHelper.getNameByValue("HYSYLXB",sea.getYHLXB());
					if (StringHelper.isEmpty(yhlxb)) {
						yhlxb = "----";
					}
					this.yhlx = yhlxa + "/" + yhlxb;
					/*
					 * 给海域加上抵押、查封、预告状态
					 */
					this.dyzt = getDYandCFandYY_XZ_SEA(djdy.getBDCDYID(),djdy.getBDCDYLX()).get("DYZT");
					this.cfzt = getDYandCFandYY_XZ_SEA(djdy.getBDCDYID(),djdy.getBDCDYLX()).get("CFZT");
					this.ygzt = getDYandCFandYY_XZ_SEA(djdy.getBDCDYID(),djdy.getBDCDYLX()).get("YGDJZT");
					
				}
				if (unit instanceof Forest) // 林地
				{
					Forest forest = (Forest) unit;
					this.lz = ConstHelper.getNameByValue("LZ", forest.getLZ());
					this.yt = ConstHelper.getNameByValue("TDYT", forest.getTDYT());
				}
				// 如果是转移登记、更正登记、其它登记（换证、补正），要写原不动产权证号
				// if (xmxx.djlx.equals(DJLX.ZYDJ.Value)
				// || xmxx.djlx.equals(DJLX.QTDJ.Value)) {
				List<BDCS_QL_GZ> gzqls = dao.getDataList(BDCS_QL_GZ.class,
						xmbhcondition + " and djdyid='" + djdy.getDJDYID() + "'");
				if (gzqls != null && gzqls.size() > 0) {
					String qlid = gzqls.get(0).getLYQLID();
					this.bdcqzh = gzqls.get(0).getBDCQZH();
					if (StringHelper.isEmpty(this.bdcqzh) && !StringHelper.isEmpty(qlid)) {
						Rights right = RightsTools.loadRights(DJDYLY.LS, qlid);
						if (right != null) {
							this.bdcqzh = right.getBDCQZH();
						}
					}
					if (!StringHelper.isEmpty(qlid)) {
						List<BDCS_QLR_LS> lsqlr = null ;
						String Baseworkflow_ID =ProjectHelper.GetPrjInfoByXMBH(xmbh).getBaseworkflowcode();
						if ("ZY007".equals(Baseworkflow_ID) || "ZY016".equals(Baseworkflow_ID)) {
							lsqlr = dao.getDataList(BDCS_QLR_LS.class, "QLID in('" + gzqls.get(0).getLYQLID() + "','"+gzqls.get(1).getLYQLID()+"')");
						}else {
							lsqlr = dao.getDataList(BDCS_QLR_LS.class, "QLID ='" + qlid+"' ");
						}
						if (lsqlr != null && lsqlr.size() > 0) {
							for (BDCS_QLR_LS qlr : lsqlr) {
								if (!StringHelper.isEmpty(qlr.getBDCQZH()) &&("ZY007".equals(Baseworkflow_ID) || "ZY016".equals(Baseworkflow_ID)) && qlr.getBDCQZH().indexOf("产权")!=-1 ) {
									this.temp_ybdcqzsh.add(qlr.getBDCQZH());
								}else {
									this.temp_ybdcqzsh.add(qlr.getBDCQZH());
								}
							}
							this.temp_bdcqzh = this.temp_ybdcqzsh;
						}
					}
				}
				// } else {
				// String qllxarray =
				// " ('1','2','3','4','5','6','7','8','15','24')";
				// List<BDCS_QL_XZ> lsqls = dao.getDataList(BDCS_QL_XZ.class,
				// " djdyid='" + djdy.getDJDYID() + "' and qllx in"
				// + qllxarray);
				// if (lsqls != null && lsqls.size() > 0) {
				// String qlid = lsqls.get(0).getLYQLID();
				// if (!StringHelper.isEmpty(qlid)) {
				// List<BDCS_QLR_XZ> lsqlr = dao.getDataList(
				// BDCS_QLR_XZ.class, "QLID='" + qlid + "'");
				// if (lsqlr != null && lsqlr.size() > 0) {
				// for (BDCS_QLR_XZ qlr : lsqlr) {
				// if (!StringHelper.isEmpty(qlr.getBDCQZH())) {
				// this.temp_ybdcqzsh.add(qlr.getBDCQZH());
				// }
				// }
				// }
				// }
				//
				// }
				// List<BDCS_QL_GZ> gzqls = dao.getDataList(BDCS_QL_GZ.class,
				// " djdyid='" + djdy.getDJDYID() + "' and qllx in"
				// + qllxarray);
				// if (gzqls != null && gzqls.size() > 0) {
				// String qlid = gzqls.get(0).getId();
				// if (!StringHelper.isEmpty(qlid)) {
				// List<BDCS_QLR_GZ> gzqlr = dao.getDataList(
				// BDCS_QLR_GZ.class, "QLID='" + qlid + "'");
				// if (gzqlr != null && gzqlr.size() > 0) {
				// for (BDCS_QLR_GZ qlr : gzqlr) {
				// if (!StringHelper.isEmpty(qlr.getBDCQZH())) {
				// this.temp_bdcqzh.add(qlr.getBDCQZH());
				// }
				// }
				// }
				// }
				// }
				// }
				// 获取单元权利产权信息
				if (true) {
					String qllxarray = " ('1','2','3','4','5','6','7','8','15','24')";
					gzqls = dao.getDataList(BDCS_QL_GZ.class,
							" djdyid='" + djdy.getDJDYID() + "' and xmbh='" + xmbh + "' and qllx in" + qllxarray);
					if (gzqls != null && gzqls.size() > 0) {
						BDCS_QL_GZ gzql = gzqls.get(0);
						if (gzql != null) {
							this.bdcqzh = gzql.getBDCQZH();
							if (StringHelper.isEmpty(this.bdcqzh) && !StringHelper.isEmpty(gzql.getLYQLID())) {
								Rights right = RightsTools.loadRights(DJDYLY.LS, gzql.getLYQLID());
								if (right != null) {
									this.bdcqzh = right.getBDCQZH();
								}
							}
							this.bdcqzhEx = gzql.getBDCQZH();
							this.mainqllx = gzql.getQLLXName();
							String qlqssj = StringHelper.FormatDateOnType(gzql.getQLQSSJ(), "yyyy-MM-dd");
							if (StringHelper.isEmpty(qlqssj)) {
								qlqssj = "----";
							}
							String qljssj = StringHelper.FormatDateOnType(gzql.getQLJSSJ(), "yyyy-MM-dd");
							if (StringHelper.isEmpty(qljssj)) {
								qljssj = "----";
							}
							this.syqx = qlqssj + "起 " + qljssj + "止";
							List<BDCS_QLR_GZ> qlrs = dao.getDataList(BDCS_QLR_GZ.class, "QLID='" + gzql.getId() + "'");
							if (qlrs != null && qlrs.size() > 0) {
								this.gyqk = qlrs.get(0).getGYQK();
							}
						}
					} else {
						List<BDCS_QL_XZ> xzqls = dao.getDataList(BDCS_QL_XZ.class,
								" djdyid='" + djdy.getDJDYID() + "' and qllx in" + qllxarray);
						if (xzqls != null && xzqls.size() > 0) {
							BDCS_QL_XZ xzql = xzqls.get(0);
							if (xzql != null) {
								this.mainqllx = xzql.getQLLXName();
								String qlqssj = StringHelper.FormatDateOnType(xzql.getQLQSSJ(), "yyyy-MM-dd");
								if (StringHelper.isEmpty(qlqssj)) {
									qlqssj = "----";
								}
								String qljssj = StringHelper.FormatDateOnType(xzql.getQLJSSJ(), "yyyy-MM-dd");
								if (StringHelper.isEmpty(qljssj)) {
									qljssj = "----";
								}
								this.syqx = qlqssj + "起 " + qljssj + "止";
								List<BDCS_QLR_XZ> qlrs = dao.getDataList(BDCS_QLR_XZ.class, "QLID='" + xzql.getId() + "'");
								if (qlrs != null && qlrs.size() > 0) {
									this.gyqk = qlrs.get(0).getGYQK();
								}
							}
						}
					}
				}

			// 如果是变更登记，在登簿前可以显示原不动产权证号,登簿后没维护
			// DJHandler handler = HandlerFactory.createDJHandler(xmbh);
			List<BDCS_DJDY_GZ> djdys_all = dao.getDataList(BDCS_DJDY_GZ.class, xmbhcondition);
			HashSet<String> ybdcqzh = new HashSet<String>();
			if (sqbflag) {
				if (isBGDY(djdys_all)) {// 获取变更前不动产权证信息
					/*
					 * List<BDCS_DJDY_GZ> lstdjdy = dao.getDataList(
					 * BDCS_DJDY_GZ.class, xmbhcondition + " and LY='02'");
					 */
					List<BDCS_DJDY_GZ> lstdjdy = IfBGDJfilterDjdy(djdys_all, DJDYLY.XZ);
					for (BDCS_DJDY_GZ bdcs_djdy_gz : lstdjdy) {
						List<Rights> rights = RightsTools.loadRightsByCondition(DJDYLY.XZ,
								"DJDYID ='" + bdcs_djdy_gz.getDJDYID() + "'");
						if (rights != null && rights.size() > 0) {
							String qlid = rights.get(0).getId();
							if (!StringHelper.isEmpty(qlid)) {
								List<BDCS_QLR_XZ> lsqlr = dao.getDataList(BDCS_QLR_XZ.class, "QLID='" + qlid + "'");
								if (lsqlr != null && lsqlr.size() > 0) {
									for (BDCS_QLR_XZ qlr : lsqlr) {
										if (!StringHelper.isEmpty(qlr.getBDCQZH())) {
											ybdcqzh.add(qlr.getBDCQZH());
										}
									}
								}
							}
						}
					}
					this.temp_ybdcqzsh = ybdcqzh;
					this.temp_bdcqzh = this.temp_ybdcqzsh;
				}
				String strzl = "";
				String strbdcdyh = "";
				String strmj = "";
				String stryt = "";
				List<String> yttt = new ArrayList<String>();
				yttt.add("stryt");
				boolean xflag = false;
				List<BDCS_DJDY_GZ> djdys = null;
				String sql = "";
				String countsql = "";

				// 更改变更登记判断 通用的 wuzhu
				if (isBGDY(djdys_all)) // 获取变更后基本信息
				{
					djdys = IfBGDJfilterDjdy(djdys_all, DJDYLY.GZ);
					sql = "SELECT BDCDYID FROM BDCK.BDCS_DJDY_GZ WHERE LY='01' AND " + xmbhcondition;
				} else// 其它登记类型基本信息
				{
					djdys = djdys_all;
					sql = "SELECT BDCDYID FROM BDCK.BDCS_DJDY_GZ WHERE " + xmbhcondition;
				}

				/*
				 * if (handler.getClass().getName().contains("BGDJHandler")) //
				 * 获取变更后基本信息 { djdys = dao.getDataList(BDCS_DJDY_GZ.class,
				 * "LY='01' AND " + xmbhcondition); sql =
				 * "SELECT BDCDYID FROM BDCK.BDCS_DJDY_GZ WHERE LY='01' AND " +
				 * xmbhcondition; } else// 其它登记类型基本信息 { djdys =
				 * dao.getDataList(BDCS_DJDY_GZ.class, xmbhcondition); sql =
				 * "SELECT BDCDYID FROM BDCK.BDCS_DJDY_GZ WHERE " +
				 * xmbhcondition; }
				 */

				if (!StringHelper.isEmpty(djdys) && djdys.size() > 0 ) {
					boolean zlflag = true;
					List<String> tdytlist = new ArrayList<String>();
					List<String> fwytlist = new ArrayList<String>();
					String configZDTDYT = ConfigHelper.getNameByValue("GetZDTDYTFrom");
					String tdyt = "";
					String fwyt = "";
					List<String> list_tdyt = new ArrayList<String>();
					List<String> list_fwyt = new ArrayList<String>();
					for (int i = 0; i < djdys.size(); i++) {
						BDCDYLX bdcdylx = BDCDYLX.initFrom(djdys.get(i).getBDCDYLX());
						DJDYLY djdyly = null;
						if ("02".equals(djdys.get(i).getLY())) {
							djdyly = DJDYLY.initFrom("03");
						} else {
							djdyly = DJDYLY.initFrom(djdys.get(i).getLY());
						}

						RealUnit realunit = UnitTools.loadUnit(bdcdylx, djdyly, djdys.get(i).getBDCDYID());
						if(realunit==null){
							continue;
						}
						if (realunit instanceof House) {
							if (realunit.getBDCDYLX().equals(BDCDYLX.H)||realunit.getBDCDYLX().equals(BDCDYLX.YCH)) {
								House h = (House) realunit;
								if ("0".equals(configZDTDYT)) {// 从房屋土地用途获取
									String tdytmc = "";
									if (!StringHelper.isEmpty(h.getFWTDYT())) {
										tdytmc = ConstHelper.getNameByValue("TDYT", h.getFWTDYT().trim());
									}
									if (!StringHelper.isEmpty(tdytmc) && !list_tdyt.contains(h.getFWTDYT())) {
										tdyt += tdytmc + "、";
										list_tdyt.add(h.getFWTDYT());
									}
								} else if ("1".equals(configZDTDYT)) {// 关联宗地主用途
									List<BDCS_TDYT_XZ> yts = dao.getDataList(BDCS_TDYT_XZ.class,
											"BDCDYID='" + h.getZDBDCDYID() + "'");
									String tdyt_tdyt = "";
									if (yts != null && yts.size() > 0) {
										for (BDCS_TDYT_XZ yt_xz : yts) {
											if (!StringHelper.isEmpty(yt_xz.getTDYT())) {
												if (StringHelper.isEmpty(tdyt_tdyt)
														|| ("1").equals(yt_xz.getSFZYT())) {
													tdyt_tdyt = yt_xz.getTDYT();
												}
											}
										}
									}
									if (!StringHelper.isEmpty(tdyt_tdyt)) {
										String tdytmc = ConstHelper.getNameByValue("TDYT", tdyt_tdyt.trim());
										if (!StringHelper.isEmpty(tdytmc) && !list_tdyt.contains(tdyt_tdyt)) {
											tdyt += tdytmc + "、";
											list_tdyt.add(tdyt_tdyt);
										}
									}
								} else if ("2".equals(configZDTDYT)) {// 关联宗地所有用途（、分隔）
									List<BDCS_TDYT_XZ> yts = dao.getDataList(BDCS_TDYT_XZ.class,
											"BDCDYID='" + h.getZDBDCDYID() + "'");
									if (yts != null && yts.size() > 0) {
										for (BDCS_TDYT_XZ yt_xz : yts) {
											if (!StringHelper.isEmpty(yt_xz.getTDYT())) {
												String tdytmc = ConstHelper.getNameByValue("TDYT",
														yt_xz.getTDYT().trim());
												if (!StringHelper.isEmpty(tdytmc)
														&& !list_tdyt.contains(yt_xz.getTDYT())) {
													tdyt += tdytmc + "、";
													list_tdyt.add(yt_xz.getTDYT());
												}
											}
										}
									}
								} else if ("3".equals(configZDTDYT)) {// 优先从房屋土地用途中获取，再从关联宗地主用途获取
									String tdytmc = "";
									String tdyt_tdyt = "";
									if (!StringHelper.isEmpty(h.getFWTDYT())) {
										tdyt_tdyt = h.getFWTDYT().trim();
										tdytmc = ConstHelper.getNameByValue("TDYT", h.getFWTDYT().trim());
									}
									if (StringHelper.isEmpty(tdytmc)) {
										List<BDCS_TDYT_XZ> yts = dao.getDataList(BDCS_TDYT_XZ.class,
												"BDCDYID='" + h.getZDBDCDYID() + "'");
										if (yts != null && yts.size() > 0) {
											for (BDCS_TDYT_XZ yt_xz : yts) {
												if (!StringHelper.isEmpty(yt_xz.getTDYT())) {
													if (StringHelper.isEmpty(tdyt_tdyt)
															|| ("1").equals(yt_xz.getSFZYT())) {
														tdyt_tdyt = yt_xz.getTDYT();
													}
												}
											}
										}
										if (!StringHelper.isEmpty(tdyt_tdyt)) {
											tdytmc = ConstHelper.getNameByValue("TDYT", tdyt_tdyt.trim());
										}
									}
									if (!StringHelper.isEmpty(tdytmc) && !list_tdyt.contains(tdyt_tdyt)) {
										tdyt += tdytmc + "、";
										list_tdyt.add(tdyt_tdyt);
									}
								} else if ("4".equals(configZDTDYT)) {// 优先从房屋土地用途中获取，再从关联宗地主用途获取
									String tdytmc = "";
									String tdyt_tdyt = "";
									if (!StringHelper.isEmpty(h.getFWTDYT())) {
										tdyt_tdyt = h.getFWTDYT().trim();
										tdytmc = ConstHelper.getNameByValue("TDYT", h.getFWTDYT().trim());
									}
									if (StringHelper.isEmpty(tdytmc)) {
										List<BDCS_TDYT_XZ> yts = dao.getDataList(BDCS_TDYT_XZ.class, "BDCDYID='" + h.getZDBDCDYID() + "'");
										if (yts != null && yts.size() > 0) {
											for (BDCS_TDYT_XZ yt_xz : yts) {
												if (!StringHelper.isEmpty(yt_xz.getTDYT())) {
													if (StringHelper.isEmpty(tdyt_tdyt) || ("1").equals(yt_xz.getSFZYT())) {
														tdyt_tdyt = yt_xz.getTDYT();
													}
												}
											}
										}
										if (!StringHelper.isEmpty(tdyt_tdyt)) {
											tdytmc = ConstHelper.getNameByValue("TDYT", tdyt_tdyt.trim());
										}
									}
									if (!StringHelper.isEmpty(tdytmc) && !list_tdyt.contains(tdyt_tdyt)) {
										tdyt += tdytmc + "、";
										list_tdyt.add(tdyt_tdyt);
									}
								}
								if (!StringHelper.isEmpty(h.getGHYT())) {
									String fwytmc = ConstHelper.getNameByValue("FWYT", h.getGHYT().trim());
									if (!list_fwyt.contains(h.getGHYT().trim())) {
										fwyt += fwytmc + "、";
										list_fwyt.add(h.getGHYT().trim());
									}
								}
								// if (StringHelper.isEmpty(tdyt))
								// tdyt = "----";
								// else
								// tdyt = tdyt.substring(0, tdyt.length() -
								// 1);
								// if (StringHelper.isEmpty(fwyt))
								// tdyt = "----";
								// else
								// fwyt = fwyt.substring(0, fwyt.length() -
								// 1);
								// stryt = tdyt + "/" + fwyt;

								if (!tdytlist.contains(tdyt)) {
									tdytlist.add(tdyt);
								}
								if (!fwytlist.contains(fwyt)) {
									fwytlist.add(fwyt);
								}
							}
							if (realunit.getBDCDYLX().equals(BDCDYLX.H)){
								countsql="SUM(SCJZMJ) mj";
							}else {
								countsql="SUM(YCJZMJ) mj";
							}
							
						} else if (realunit instanceof UseLand) {
							UseLand useland = (UseLand) unit;
							String strtdyt = "----";
							if (!StringHelper.isEmpty(useland)) {
								List<TDYT> lsttdyt = useland.getTDYTS();
								boolean flag = true;
								for (TDYT _tdyt : lsttdyt) {
									if (flag) {
										strtdyt = _tdyt.getTDYTMC();
										flag = false;
									} else {
										strtdyt = strtdyt + "，" + _tdyt.getTDYTMC();
									}
								}
							}
							if (!xflag) {
								if (StringHelper.isEmpty(strtdyt)) {
									stryt = "----";
								} else {
									stryt = strtdyt;
								}
								countsql = "SUM(zDMJ) mj";
							} else {
								if (StringHelper.isEmpty(strtdyt)) {
									stryt = stryt + ";----";
								} else {
									stryt = stryt + ";" + strtdyt;
								}
							}

						} else if (realunit instanceof OwnerLand) {
							OwnerLand ol = (OwnerLand) realunit;
							if (!StringHelper.isEmpty(ol)) {
								if (!xflag) {
									if (StringHelper.isEmpty(ol.getYT())) {
										stryt = "----";
									} else {
										stryt = ConstHelper.getNameByValue("TDYT", ol.getYT());
									}
									countsql = "SUM(zDMJ) mj";
								} else {
									if (StringHelper.isEmpty(ol.getYT())) {
										stryt = stryt + ",----";
									} else {
										stryt = stryt + "," + ConstHelper.getNameByValue("TDYT", ol.getYT());
									}
								}
							}
						} else if (realunit instanceof Forest) {
							Forest ol = (Forest) realunit;
							if (!StringHelper.isEmpty(ol)) {
								if (!xflag) {
									if (StringHelper.isEmpty(ol.getTDYT())) {
										stryt = "----";
									} else {
										stryt = ConstHelper.getNameByValue("TDYT", ol.getTDYT());
									}
									countsql = "SUM(sYQMJ) mj";
								} else {
									if (StringHelper.isEmpty(ol.getTDYT())) {
										stryt = stryt + ",----";
									} else {
										stryt = stryt + "," + ConstHelper.getNameByValue("TDYT", ol.getTDYT());
									}
								}
							}
						}
						if  (_bdcdylx.equals("09") ) {
							AgriculturalLand nyd = (AgriculturalLand) unit;
							stryt = nyd.getYT();
							if (StringHelper.isEmpty(stryt)) {
								CommonDao commonDao = SuperSpringContext.getContext().getBean(CommonDao.class);
								String ytClassName = EntityTools.getEntityName("BDCS_TDYT", DJDYLY.DC);
								Class<?> ytClass = EntityTools.getEntityClass(ytClassName);
								List<TDYT> listyts = (List<TDYT>) commonDao.getDataList(ytClass, "BDCDYID='" + djdys.get(i).getBDCDYID() + "'");
								String yt ="";
								for (int j = 0; j < listyts.size(); j++) {
									yt += listyts.get(j).getTDYTMC();
									if (j+1 < listyts.size()) {
										yt +="、";
									}
								}
								if (StringHelper.isEmpty(yt)) {
									List<BDCS_TDYT_XZ> lsttdyt = dao.getDataList(BDCS_TDYT_XZ.class, "BDCDYID='" + djdy.getBDCDYID() + "' ORDER BY SFZYT DESC");
									boolean flag = true;
									for (TDYT _tdyt : lsttdyt) {
										if (flag) {
											yt = _tdyt.getTDYTMC();
											flag = false;
										} else {
											yt = yt + "、" + _tdyt.getTDYTMC();
										}
									}
								}
								this.yt= yt;
							}else {
								if (stryt.toString().indexOf("0")!=-1 || stryt.toString().indexOf("1")!=-1) {//用途为数字时，匹配数据字典
									this.yt= ConstHelper.getNameByValue("TDYT",stryt);
								}else {
									this.yt= stryt;
								}
							}
							
						}

							if (!xflag) {
								if (StringHelper.isEmpty(realunit.getBDCDYH())) {
									strbdcdyh = "----";
								} else {
									strbdcdyh = realunit.getBDCDYH();
								}
								if (StringHelper.isEmpty(realunit.getMJ())) {
									strmj = "----";
								} else {
									strmj = StringHelper.formatDouble(realunit.getMJ());
								}
								if (StringHelper.isEmpty(realunit.getZL())) {
									strzl = "----";
								} else {
									if (isBGDY(djdys_all)) // 获取变更后基本信息
									{
										strzl = realunit.getZL();
									} else {
										strzl = this.zl;// realunit.getZL();
									}
								}
								xflag = true;
							} else {
								if (StringHelper.isEmpty(realunit.getBDCDYH())) {
									strbdcdyh = strbdcdyh + ",----";
								} else {
									strbdcdyh = strbdcdyh + "," + realunit.getBDCDYH();
								}
							}
							String configSBT = ConfigHelper.getNameByValue("SQSPBDY_BDCDYH_YT");
							// for (int i1 = 0; i1 < configSBT.length(); i1++){
							if (Character.isDigit(configSBT.charAt(0))) {
								if (i == Integer.parseInt(configSBT) - 1 && djdys.size() > Integer.parseInt(configSBT)) {
									strbdcdyh = strbdcdyh + "等" + djdys.size() + "个";
									stryt = stryt + "等" + djdys.size() + "个";
									break;
								}
								// }
							}
							if (i == 1 && zlflag) {
								strzl = strzl + "等" + djdys.size() + "个";
								zlflag = false;
							}
						}
						if (!StringHelper.isEmpty(djdys) && djdys.size() > 1) {
							BDCDYLX bdcdylx = BDCDYLX.initFrom(djdys.get(0).getBDCDYLX());
							DJDYLY djdyly = DJDYLY.initFrom(djdys.get(0).getLY());
							String className = bdcdylx.getTableName(djdyly);
							if (StringUtils.isEmpty(className))
								return null;
							if (!StringHelper.isEmpty(sql)) {
								if (!StringHelper.isEmpty(countsql)) {
									sql = "SELECT " + countsql + " FROM BDCK." + className + " where BDCDYID IN( " + sql+ ")";
									List<Map> maps = dao.getDataListByFullSql(sql);
									if (!StringHelper.isEmpty(maps) && maps.size() > 0) {
										double mj = StringHelper.getDouble(maps.get(0).get("MJ"));
										strmj = "总面积：" +StringHelper.formatMJ( StringHelper.formatDouble(mj));
									}
								}
							}
						}else {
							strmj=StringHelper.formatMJ(strmj);
						}
						this.bdcdyh = strbdcdyh;
						this.mj = strmj;
						// stryt
						int td = 0;
						int fw = 0;
						for (String string : tdytlist) {
							if (!StringHelper.isEmpty(string)) {
								stryt += string;
								if (++td > 3) {
									break;
								}
							}
						}
						if (!BDCDYLX.SHYQZD.equals(lx)) {
							if (BDCDYLX.H.equals(lx)||BDCDYLX.YCH.equals(lx)) {
								if (!StringHelper.isEmpty(stryt)) {
									if(stryt.endsWith("、"))
										stryt = stryt.substring(0, stryt.length() - 1);
								} else {
									stryt = "----";
								}
								stryt = stryt + "/";
								for (String string : fwytlist) {
									if (!StringHelper.isEmpty(string)) {
										stryt += string;
										if (++fw > 3) {
											break;
										}
									}
								}
								if (stryt.endsWith("、")) {
									stryt = stryt.substring(0, stryt.length() - 1);
								} else {
									stryt = stryt + "----";
								}
								this.yt = stryt;
								if ("3".equals(configZDTDYT)) {
									if (StringHelper.isEmpty(tdyt))
										tdyt = "----";
									else
										tdyt = tdyt.substring(0, tdyt.length() - 1);
									if (StringHelper.isEmpty(fwyt))
										fwyt = "----";
									else
										fwyt = fwyt.substring(0, fwyt.length() - 1);
									this.yt = tdyt + "/" + fwyt;
								}
							} else if(BDCDYLX.HY.equals(lx)){
								Sea sea = (Sea) unit;
								String hyyt = "";
								String yhlxa = "";
								String yhlxb = "";
								if (sea != null) {
									yhlxa = ConstHelper.getNameByValue("HYSYLXA", sea.getYHLXA());
									yhlxb = ConstHelper.getNameByValue("HYSYLXB", sea.getYHLXB());
								}
								if (!StringHelper.isEmpty(yhlxa)) {
									hyyt = yhlxa;
								}
								if (!StringHelper.isEmpty(yhlxb)) {
									if (StringHelper.isEmpty(hyyt)) {
										hyyt = yhlxb;
									} else {
										hyyt = hyyt + "/" + yhlxb;
									}
								}

						}
					}
					
					this.zl = strzl;
				}

			}

			if (xmxx.djlx.equals(DJLX.ZXDJ.Value) && xmxx.qllx.equals(QLLX.DIYQ.Value)) {
				Rights zsrights = RightsTools.loadRightsByDJDYID(DJDYLY.GZ, xmbh, djdy.getDJDYID());
				if (zsrights != null) {
					String lyqlid = zsrights.getLYQLID();
					if (!StringHelper.isEmpty(lyqlid)) {
						Rights lsrights = RightsTools.loadRights(DJDYLY.LS, lyqlid);
						if (lsrights != null) {
							List<BDCS_QLR_LS> lsqlr = dao.getDataList(BDCS_QLR_LS.class, "QLID='" + lyqlid + "'");
							if (lsqlr != null && lsqlr.size() > 0) {
								for (BDCS_QLR_LS qlr : lsqlr) {
									if (!StringHelper.isEmpty(qlr.getBDCQZH())) {
										ybdcqzh.add(qlr.getBDCQZH());
									}
								}
								this.temp_ybdcqzsh = ybdcqzh;
								this.temp_bdcqzh = this.temp_ybdcqzsh;
							}

						}
					}
				}
			}

				// 土地部分抵押、查封、转移基准流程，申请表上“原不动产权证号”目前读取的是宗地的所有证书号，需修改为只显示被抵押、查封、转移部分的
				// 证书号。
				Rights ql = RightsTools.loadRightsByDJDYID(DJDYLY.GZ, xmbh, djdy.getDJDYID());
				if (ql != null && "1".equals(ql.getISPARTIAL())) {
					List<BDCS_PARTIALLIMIT> partiallimit = dao.getDataList(BDCS_PARTIALLIMIT.class,
							" LIMITQLID='" + ql.getId() + "'");
					if (partiallimit != null && partiallimit.size() > 0) {
						for (BDCS_PARTIALLIMIT qlrs : partiallimit) {
							BDCS_QLR_XZ qlr = dao.get(BDCS_QLR_XZ.class, qlrs.getQLRID());
							if (qlr != null) {
								if (!StringHelper.isEmpty(qlr.getBDCQZH())) {
									ybdcqzh.add(qlr.getBDCQZH());
								}
							}
						}
						this.temp_ybdcqzsh = ybdcqzh;
					} else {
						List<BDCS_PARTIALLIMIT> partiallimit_ly = dao.getDataList(BDCS_PARTIALLIMIT.class,
								" LIMITQLID='" + ql.getLYQLID() + "'");
						if (partiallimit_ly != null && partiallimit_ly.size() > 0) {
							for (BDCS_PARTIALLIMIT qlrs : partiallimit_ly) {
								BDCS_QLR_XZ qlr = dao.get(BDCS_QLR_XZ.class, qlrs.getQLRID());
								if (qlr != null) {
									if (!StringHelper.isEmpty(qlr.getBDCQZH())) {
										ybdcqzh.add(qlr.getBDCQZH());
									}
								}
							}
							this.temp_ybdcqzsh = ybdcqzh;
						}
					}
				}
				if (!StringHelper.isEmpty(this.temp_ybdcqzsh) && this.temp_ybdcqzsh.size() > 0) {
					this.ybdcqzsh = StringHelper.formatObject(this.temp_ybdcqzsh.toArray()[0]);
					
				}
				if (StringHelper.isEmpty(this.tdftmj_fwjzmj)) {
					this.tdftmj_fwjzmj = this.mj;
				}
				return this;
			//}
		}
	}

	/**
	 * 权利信息
	 * 
	 * @ClassName: QLXX
	 * @author liushufeng
	 * @date 2015年8月6日 下午10:48:45
	 */
	public class QLXX {
		// 被担保债券数额
		private String bdbzqse;
		// 债务履行期限
		private String zwlxqx;
		// 在建建筑物抵押范围
		private String zjjzwdyfw;
		// 需役地坐落
		private String xydzl;
		// 需役地不动产单元号
		private String xydbdcdyh;
		// 登记原因
		private String djyy;
		// 证书版式
		private String zsbs;
		// 持证方式
		private String czfs;
		// 登簿人
		private String dbr;
		// 登簿日期
		private String dbrq;
		// 备注--2016年3月21日 刘树峰新加
		public String bz;

		public String fj;

		private String bdcqzh;// 不动产权证号

		public String getBDCQZH() {
			return bdcqzh;
		}

		public String getBdbzqse() {
			return bdbzqse;
		}

		public String getZwlxqx() {
			return zwlxqx;
		}

		public String getZjjzwdyfw() {
			return zjjzwdyfw;
		}

		public String getXydzl() {
			return xydzl;
		}

		public String getXydbdcdyh() {
			return xydbdcdyh;
		}

		public String getDjyy() {
			return djyy;
		}

		public String getZsbs() {
			return zsbs;
		}

		public String getCzfs() {
			return czfs;
		}

		public String getDbr() {
			return dbr;
		}

		public String getDbrq() {
			return dbrq;
		}

		public QLXX build(String xmbh) {
			String xmbhcondition = ProjectHelper.GetXMBHCondition(xmbh);
			List<BDCS_QL_GZ> qls = dao.getDataList(BDCS_QL_GZ.class, xmbhcondition);
			if (qls != null && qls.size() > 0) {
//				BDCS_QL_GZ ql = qls.get(0);
				for(BDCS_QL_GZ ql :qls){
					BDCS_FSQL_GZ fsql = dao.get(BDCS_FSQL_GZ.class, ql.getFSQLID());
					this.djyy = ql.getDJYY();
					this.zsbs = ql.getZSBS();
					this.czfs = ql.getCZFS();
					this.dbr = ql.getDBR();
					this.dbrq = DateUtil.FormatByDatetime(ql.getDJSJ());
					/*-------------------修改申请审批表里的不动产权证号显示（鹿寨）--------------------------*/
					if (qls.size() > 1) {
						String temp = "";
						int count = 0;
						for (BDCS_QL_GZ q : qls) {
							if (!"".equals(temp)) {
								if (!StringHelper.isEmpty(q.getBDCQZH())&&q.getBDCQZH().equals(temp)) {
									continue;
								}
							}
							count++;
							if (q.getBDCQZH() != null) {
								temp = q.getBDCQZH();
							}
						}
						this.bdcqzh = ql.getBDCQZH() + "等" + count + "个";
					} else {
						this.bdcqzh = ql.getBDCQZH();
					}
	
					// 石家庄的，想把权利里边的备注打到申请表上。
					String xzqhdm = ConfigHelper.getNameByValue("XZQHDM");
					if (xzqhdm != null && xzqhdm.startsWith("13")) {
						if (StringHelper.isEmpty(this.bz) && !StringHelper.isEmpty(ql.getBZ())) {
							this.bz = ql.getBZ();
						}
					}
					List<BDCS_QL_GZ> dyqls = new ArrayList<BDCS_QL_GZ>();
					BDCS_XMXX xmxx1 = dao.get(BDCS_XMXX.class, xmbh);
					if(xmxx1 != null){
						Double bdbzzqse = 0.0;
						String isdb = xmxx1.getSFDB();//是否登簿
						DecimalFormat df = new DecimalFormat("######0.00"); // 格式过长的数据（E）
						df.setRoundingMode(RoundingMode.HALF_UP);
						if(isdb != null && !StringHelper.isEmpty(isdb)){
							if(isdb.equals("0")){//未登簿
								dyqls = dao.getDataList(BDCS_QL_GZ.class,
										xmbhcondition + "and qllx='23' and bdcqzh is null");
								if (!StringHelper.isEmpty(dyqls) && dyqls.size() > 0) {
									for(int i = 0;i < dyqls.size();i++)
									{
										BDCS_FSQL_GZ dyfsqls = dao.get(BDCS_FSQL_GZ.class, dyqls.get(i).getFSQLID());
										
										if (ConstValue.DYFS.ZGEDY.Value.equals(dyfsqls.getDYFS())) {
											bdbzzqse += StringHelper.getDouble(dyfsqls.getZGZQSE());// 先转换成double行，防止NULL类型的
										} else {
											bdbzzqse += StringHelper
													.getDouble(dyfsqls.getBDBZZQSE());// 先转换成double行，防止NULL类型的
										}
									}
									
									this.bdbzqse = df.format(bdbzzqse);
								}
							}
							else if(isdb.equals("1")){//项目已经登簿的情况下，直接去历史层获取数据
								List<BDCS_QL_LS> dyqls_ls = dao.getDataList(BDCS_QL_LS.class,
										xmbhcondition + "and qllx='23'");
								if (!StringHelper.isEmpty(dyqls_ls) && dyqls_ls.size() > 0) {
									for(int i = 0;i < dyqls_ls.size();i++)
									{
										BDCS_FSQL_LS dyfsqls_ls = dao.get(BDCS_FSQL_LS.class, dyqls_ls.get(i).getFSQLID());
										
										if (ConstValue.DYFS.ZGEDY.Value.equals(dyfsqls_ls.getDYFS())) {
											bdbzzqse += StringHelper.getDouble(dyfsqls_ls.getZGZQSE());// 先转换成double行，防止NULL类型的
										} else {
											bdbzzqse += StringHelper
													.getDouble(dyfsqls_ls.getBDBZZQSE());// 先转换成double行，防止NULL类型的
										}
									}
									
									this.bdbzqse = df.format(bdbzzqse);
								}
							}
						}
					}
					/*List<BDCS_QL_GZ> dyqls = dao.getDataList(BDCS_QL_GZ.class,
							xmbhcondition + "and qllx='23'");*/
	
					if (StringHelper.isEmpty(this.fj) && !StringHelper.isEmpty(ql.getFJ())) {
						this.fj = ql.getFJ();
					}
	
					List<BDCS_QL_GZ> dyqls1 = dao.getDataList(BDCS_QL_GZ.class, xmbhcondition + "and qllx='23'");
					if (!StringHelper.isEmpty(dyqls1) && dyqls1.size() > 0) {
						BDCS_FSQL_GZ dyfsqls = dao.get(BDCS_FSQL_GZ.class, dyqls1.get(0).getFSQLID());
						// 抵押的不需要判断登记类型，没意义，注释掉
						// if ((xmxx.djlx.equals(DJLX.CSDJ.Value) ||
						// xmxx.djlx.equals(DJLX.YGDJ.Value))) {
						// sunhb-2015-09-11 格式化数字
						// logger.info("begining");
						DecimalFormat df = new DecimalFormat("######0.00"); // 格式过长的数据（E）
						// logger.info(df);
						df.setRoundingMode(RoundingMode.HALF_UP);
						// logger.info(df);
						Double bdbzzqse = 0.0;
						if (ConstValue.DYFS.ZGEDY.Value.equals(dyfsqls.getDYFS())) {
							bdbzzqse = StringHelper.getDouble(dyfsqls.getZGZQSE());// 先转换成double行，防止NULL类型的
						} else {
							bdbzzqse = StringHelper.getDouble(dyfsqls.getBDBZZQSE());// 先转换成double行，防止NULL类型的
						}
						this.bdbzqse = df.format(bdbzzqse);
						this.zwlxqx = "";
						if (dyqls1.get(0).getQLQSSJ() != null) {
							zwlxqx = StringHelper.FormatByDatetime(dyqls1.get(0).getQLQSSJ()) + "起";
						}
						if (dyqls1.get(0).getQLJSSJ() != null) {
							zwlxqx = zwlxqx + StringHelper.FormatByDatetime(dyqls1.get(0).getQLJSSJ()) + "止";
						}
						this.zjjzwdyfw = dyfsqls.getZJJZWDYFW();
						// }
					}
					// 如果是注销登记，登簿人要写注销登簿人和注销时间
					if (xmxx.djlx.endsWith(DJLX.ZXDJ.Value)) {
						this.dbr = fsql.getZXDBR();
						this.dbrq = DateUtil.FormatByDatetime(fsql.getZXSJ());
						this.djyy = fsql.getZXDYYY();// 申请表中填注销原因
	
						// 抵押权注销
						if (xmxx.qllx.equals(QLLX.DIYQ.Value)) {
							DecimalFormat df = new DecimalFormat("######0.00"); // 格式过长的数据（E）
							df.setRoundingMode(RoundingMode.HALF_UP);
							Double bdbzzqse = 0.0;
							if ("2".equals(fsql.getDYFS())) {
								bdbzzqse = StringHelper.getDouble(fsql.getZGZQSE());// 先转换成double行，防止NULL类型的
							} else {
								bdbzzqse = StringHelper.getDouble(fsql.getBDBZZQSE());// 先转换成double行，防止NULL类型的
							}
							this.bdbzqse = df.format(bdbzzqse);
							// this.bdbzqse =
							// StringHelper.formatObject(fsql.getBDBZZQSE());
							this.zwlxqx = "";
							if (ql.getQLQSSJ() != null) {
								zwlxqx = StringHelper.FormatByDatetime(ql.getQLQSSJ()) + "起";
							}
							if (ql.getQLJSSJ() != null) {
								zwlxqx = zwlxqx + StringHelper.FormatByDatetime(ql.getQLJSSJ()) + "止";
							}
							this.zjjzwdyfw = fsql.getZJJZWDYFW();
						}
					}
					// 解封登记，也是注销登簿人和注销时间
					if (xmxx.djlx.endsWith(DJLX.CFDJ.Value) && xmxx.qllx.equals("98")) {
						this.dbr = fsql.getZXDBR();
						this.dbrq = DateUtil.FormatByDatetime(fsql.getZXSJ());
					}
				}
			}
			return this;
		}
	}

	/**
	 * 对外接口：生成申请审批表内容，返回申请审批表
	 * 
	 * @Title: build
	 * @author:liushufeng
	 * @date：2015年8月6日 下午10:53:23
	 * @param project_id
	 * @return
	 */
	public SQSPBex build(String xmbh, String acinstid, SmProSPService smProSPService, HttpServletRequest request) {
		// 项目信息
		this.xmxx = new XMXX().build(xmbh, acinstid, smProSPService, request);
		// 权利人
		this.qlr = new SQR().buildQLR(xmbh, SQRLB.JF);
		// 义务人
		this.ywr = new SQR().buildQLR(xmbh, SQRLB.YF);
		// 利害关系人
		this.lhgxr = new SQR().buildQLR(xmbh, SQRLB.LHGXF);
		// 单元信息
		this.bdcdy = new BDCDY().build(xmbh);
		// 权利信息
		this.qlxx = new QLXX().build(xmbh);
		// 申请人列表
		this.sqrs = this.buildSQRS(xmbh);

		this.sqrs2 = this.buildSQRS2(xmbh);

		this.xmbh = xmbh;

		return this;
	}

	private List<Map> buildSQRS2(String xmbh) {
		List<Map> itemList = new ArrayList<Map>();

		StringBuilder QLRfulSql = new StringBuilder(" SELECT DY.BDCDYLX, DY.BDCDYID, DY.DJDYID, SQR.* ");
		QLRfulSql.append(" FROM BDCK.BDCS_SQR SQR ");
		QLRfulSql.append(" INNER JOIN BDCK.BDCS_DJDY_GZ DY ON DY.XMBH = SQR.XMBH ");
		QLRfulSql.append(" WHERE EXISTS (SELECT 1 FROM BDCK.BDCS_QLR_GZ QLR ");
		QLRfulSql.append(" INNER JOIN BDCK.BDCS_QL_GZ QL ON QL.QLID = QLR.QLID ");
		QLRfulSql.append(" WHERE QL.DJDYID = DY.DJDYID AND QLR.SQRID = SQR.SQRID");
		QLRfulSql.append(" AND QL.XMBH = '").append(xmbh).append("' ");
		QLRfulSql.append(" ) AND SQR.SQRLB='1' ");
		List<Map> qlrlist = dao.getDataListByFullSql(QLRfulSql.toString());
		List<String> djdylist = new ArrayList<String>();
		if (qlrlist != null) {
			for (Map qlr : qlrlist) {
				Map<String, String> sqr = new HashMap<String, String>();
				sqr.put("SQR_SQRXM", StringHelper.formatObject(qlr.get("SQRXM")));
				sqr.put("SQR_ZJLX", ConstHelper.getNameByValue("ZJLX", StringHelper.formatObject(qlr.get("ZJLX"))));
				sqr.put("SQR_ZJH", StringHelper.formatObject(qlr.get("ZJH")));
				sqr.put("SQR_TXDZ", StringHelper.formatObject(qlr.get("TXDZ")));
				sqr.put("SQR_YZBM", StringHelper.formatObject(qlr.get("YZBM")));
				sqr.put("SQR_FDDBR", StringHelper.formatObject(qlr.get("FDDBR")));
				sqr.put("SQR_LXDH", StringHelper.formatObject(qlr.get("LXDH")));
				sqr.put("SQR_DLRXM", StringHelper.formatObject(qlr.get("DLRXM")));
				sqr.put("SQR_DLRLXDH", StringHelper.formatObject(qlr.get("DLRLXDH")));
				sqr.put("SQR_DLJGMC", StringHelper.formatObject(qlr.get("DLJGMC")));
				sqr.put("SQR_SQRLB", ConstHelper.getNameByValue("SQRLB", StringHelper.formatObject(qlr.get("SQRLB"))));
				String bdcdylx = StringHelper.formatObject(qlr.get("BDCDYLX"));
				String bdcdyid = StringHelper.formatObject(qlr.get("BDCDYID"));
				djdylist.add(StringHelper.formatObject(qlr.get("DJDYID")));
				if (!StringHelper.isEmpty(bdcdylx) && !StringHelper.isEmpty(bdcdyid)) {
					RealUnit unit = null;
					unit = UnitTools.loadUnit(BDCDYLX.initFrom(bdcdylx), DJDYLY.LS, bdcdyid);
					if (unit == null) {
						unit = UnitTools.loadUnit(BDCDYLX.initFrom(bdcdylx), DJDYLY.DC, bdcdyid);
					}
					if (unit != null) {
						sqr.put("SQR_BDCDYH", unit.getBDCDYH());
					}
				}
				itemList.add(sqr);
			}
		}
		StringBuilder djdyids = new StringBuilder("");
		if (djdylist.size() > 0) {
			boolean flg = true;
			for (String id : djdylist) {
				if (flg) {
					flg = false;
					djdyids.append("'").append(id).append("'");
				} else {
					djdyids.append(",").append("'").append(id).append("'");
				}
			}
			StringBuilder YWRfulSql = new StringBuilder(" SELECT DY.BDCDYLX, DY.BDCDYID, SQR.* ");
			YWRfulSql.append(" FROM BDCK.BDCS_SQR SQR ");
			YWRfulSql.append(" LEFT JOIN BDCK.BDCS_DJDY_GZ DY ON DY.XMBH = SQR.XMBH ");
			YWRfulSql.append(" WHERE (EXISTS (SELECT 1 FROM BDCK.BDCS_QLR_LS QLR ");
			YWRfulSql.append(" LEFT JOIN BDCK.BDCS_QL_LS QL ON QL.QLID = QLR.QLID ");
			YWRfulSql.append(" WHERE QL.DJDYID = DY.DJDYID AND QLR.QLRMC = SQR.SQRXM AND QLR.ZJH=SQR.ZJH ");
			if (djdyids.length() > 0) {
				String strsql = Global.GetSqlIn(djdyids.toString(), "DJDYID");
				YWRfulSql.append(" AND QL.DJDYID IN (" + strsql + ") )");
			}
			//YWRfulSql.append(" AND QL.DJDYID IN (").append(djdyids).append(") ) ");
			YWRfulSql.append(" AND SQR.XMBH='").append(xmbh).append("' AND SQR.SQRLB='2')");
			//YWRfulSql.append(" OR (SQR.XMBH='").append(xmbh).append("' AND SQR.SQRLB='2')");
			List<Map> ywrlist = dao.getDataListByFullSql(YWRfulSql.toString());
			for (Map qlr : ywrlist) {
				Map<String, String> sqr = new HashMap<String, String>();
				sqr.put("SQR_SQRXM", StringHelper.formatObject(qlr.get("SQRXM")));
				sqr.put("SQR_ZJLX", ConstHelper.getNameByValue("ZJLX", StringHelper.formatObject(qlr.get("ZJLX"))));
				sqr.put("SQR_ZJH", StringHelper.formatObject(qlr.get("ZJH")));
				sqr.put("SQR_TXDZ", StringHelper.formatObject(qlr.get("TXDZ")));
				sqr.put("SQR_YZBM", StringHelper.formatObject(qlr.get("YZBM")));
				sqr.put("SQR_FDDBR", StringHelper.formatObject(qlr.get("FDDBR")));
				sqr.put("SQR_LXDH", StringHelper.formatObject(qlr.get("LXDH")));
				sqr.put("SQR_DLRXM", StringHelper.formatObject(qlr.get("DLRXM")));
				sqr.put("SQR_DLRLXDH", StringHelper.formatObject(qlr.get("DLRLXDH")));
				sqr.put("SQR_DLJGMC", StringHelper.formatObject(qlr.get("DLJGMC")));
				sqr.put("SQR_SQRLB", ConstHelper.getNameByValue("SQRLB", StringHelper.formatObject(qlr.get("SQRLB"))));
				String bdcdylx = StringHelper.formatObject(qlr.get("BDCDYLX"));
				String bdcdyid = StringHelper.formatObject(qlr.get("BDCDYID"));
				if (!StringHelper.isEmpty(bdcdylx) && !StringHelper.isEmpty(bdcdyid)) {
					RealUnit unit = null;
					unit = UnitTools.loadUnit(BDCDYLX.initFrom(bdcdylx), DJDYLY.LS, bdcdyid);
					if (unit == null) {
						unit = UnitTools.loadUnit(BDCDYLX.initFrom(bdcdylx), DJDYLY.DC, bdcdyid);
					}
					if (unit != null) {
						sqr.put("SQR_BDCDYH", unit.getBDCDYH());
					}
				}
				itemList.add(sqr);
			}
		} else {
			itemList.clear();
			String xmbhcondition = ProjectHelper.GetXMBHCondition(xmbh);
			List<BDCS_SQR> listsqr = dao.getDataList(BDCS_SQR.class, xmbhcondition);
			if (listsqr != null) {
				for (BDCS_SQR s : listsqr) {
					Map<String, String> sqr = new HashMap<String, String>();
					sqr.put("SQR_SQRXM", s.getSQRXM());
					sqr.put("SQR_ZJLX", ConstHelper.getNameByValue("ZJLX", s.getZJLX()));
					sqr.put("SQR_ZJH", s.getZJH());
					sqr.put("SQR_TXDZ", s.getTXDZ());
					sqr.put("SQR_YZBM", s.getYZBM());
					sqr.put("SQR_FDDBR", s.getFDDBR());
					sqr.put("SQR_LXDH", s.getLXDH());
					sqr.put("SQR_DLRXM", s.getDLRXM());
					sqr.put("SQR_DLRLXDH", s.getDLRLXDH());
					sqr.put("SQR_DLJGMC", s.getDLJGMC());
					sqr.put("SQR_SQRLB", ConstHelper.getNameByValue("SQRLB", s.getSQRLB()));
					// 增加法定代表人电话--刘树峰 2015年10月28日
					sqr.put("SQR_FDDBRDH", s.getFDDBRDH());
					itemList.add(sqr);
				}
			}
		}
		return itemList;
	}

	@SuppressWarnings("rawtypes")
	private List<Map> buildSQRS(String xmbh) {
		String xmbhcondition = ProjectHelper.GetXMBHCondition(xmbh);
		List<BDCS_SQR> listsqr = dao.getDataList(BDCS_SQR.class, xmbhcondition);
		List<Map> itemList = new ArrayList<Map>();
		if (listsqr != null) {
			for (BDCS_SQR s : listsqr) {
				Map<String, String> sqr = new HashMap<String, String>();
				sqr.put("SQR_SQRXM", s.getSQRXM());
				sqr.put("SQR_ZJLX", ConstHelper.getNameByValue("ZJLX", s.getZJLX()));
				sqr.put("SQR_ZJH", s.getZJH());
				sqr.put("SQR_TXDZ", s.getTXDZ());
				sqr.put("SQR_YZBM", s.getYZBM());
				sqr.put("SQR_FDDBR", s.getFDDBR());
				sqr.put("SQR_LXDH", s.getLXDH());
				sqr.put("SQR_DLRXM", s.getDLRXM());
				sqr.put("SQR_DLRLXDH", s.getDLRLXDH());
				sqr.put("SQR_DLJGMC", s.getDLJGMC());
				sqr.put("SQR_SQRLB", ConstHelper.getNameByValue("SQRLB", s.getSQRLB()));
				// 增加法定代表人电话--刘树峰 2015年10月28日
				sqr.put("SQR_FDDBRDH", s.getFDDBRDH());
				itemList.add(sqr);
			}
		}
		return itemList;
	}

	public SQSPB converToSQSPB() {
		SQSPB sqb = new SQSPB();
		sqb.setT_djlx_100("0");
		sqb.setT_djlx_200("0");
		sqb.setT_djlx_300("0");
		sqb.setT_djlx_400("0");
		sqb.setT_djlx_500("0");
		sqb.setT_djlx_600("0");
		sqb.setT_djlx_700("0");
		sqb.setT_djlx_800("0");
		sqb.setT_djlx_900("0");
		if ("100".equals(this.xmxx.djlx)) {
			sqb.setT_djlx_100("1");
		} else if ("200".equals(this.xmxx.djlx)) {
			sqb.setT_djlx_200("1");
		} else if ("300".equals(this.xmxx.djlx)) {
			sqb.setT_djlx_300("1");
		} else if ("400".equals(this.xmxx.djlx)) {
			sqb.setT_djlx_400("1");
		} else if ("500".equals(this.xmxx.djlx)) {
			sqb.setT_djlx_500("1");
		} else if ("600".equals(this.xmxx.djlx)) {
			sqb.setT_djlx_600("1");
		} else if ("700".equals(this.xmxx.djlx)) {
			sqb.setT_djlx_700("1");
		} else if ("800".equals(this.xmxx.djlx)) {
			sqb.setT_djlx_800("1");
		} else if ("900".equals(this.xmxx.djlx)) {
			sqb.setT_djlx_900("1");
		}
		sqb.setT_qllx_1("0");
		sqb.setT_qllx_3("0");
		sqb.setT_qllx_5("0");
		sqb.setT_qllx_7("0");
		sqb.setT_qllx_9("0");
		sqb.setT_qllx_11("0");
		sqb.setT_qllx_15("0");
		sqb.setT_qllx_17("0");
		sqb.setT_qllx_fwsyq("0");
		sqb.setT_qllx_gzwsyq("0");
		sqb.setT_qllx_sllmsyq("0");
		sqb.setT_qllx_sllmshyq("0");
		sqb.setT_qllx_23("0");
		sqb.setT_qllx_19("0");
		sqb.setT_qllx_qt("0");
		if ("1".equals(this.xmxx.qllx)) {
			sqb.setT_qllx_1("1");
		} else if ("3".equals(this.xmxx.qllx)) {
			sqb.setT_qllx_3("1");
		} else if ("4".equals(this.xmxx.qllx)) {
			sqb.setT_qllx_3("1");
			sqb.setT_qllx_fwsyq("1");
		} else if ("5".equals(this.xmxx.qllx)) {
			sqb.setT_qllx_5("1");
		} else if ("6".equals(this.xmxx.qllx)) {
			sqb.setT_qllx_5("1");
			sqb.setT_qllx_fwsyq("1");
		} else if ("7".equals(this.xmxx.qllx)) {
			sqb.setT_qllx_7("1");
		} else if ("8".equals(this.xmxx.qllx)) {
			sqb.setT_qllx_7("1");
			sqb.setT_qllx_fwsyq("1");
		} else if ("9".equals(this.xmxx.qllx)) {
			sqb.setT_qllx_9("1");
		} else if ("10".equals(this.xmxx.qllx)) {
			sqb.setT_qllx_9("1");
			sqb.setT_qllx_sllmsyq("1");
		} else if ("11".equals(this.xmxx.qllx)) {
			sqb.setT_qllx_11("1");
		} else if ("12".equals(this.xmxx.qllx)) {
			sqb.setT_qllx_11("1");
			sqb.setT_qllx_sllmshyq("1");
		} else if ("13".equals(this.xmxx.qllx)) {
			sqb.setT_qllx_qt("1");
		} else if ("14".equals(this.xmxx.qllx)) {
			sqb.setT_qllx_qt("1");
		} else if ("15".equals(this.xmxx.qllx)) {
			sqb.setT_qllx_15("1");
		} else if ("16".equals(this.xmxx.qllx)) {
			sqb.setT_qllx_15("1");
			sqb.setT_qllx_gzwsyq("1");
		} else if ("17".equals(this.xmxx.qllx)) {
			sqb.setT_qllx_17("1");
		} else if ("18".equals(this.xmxx.qllx)) {
			sqb.setT_qllx_17("1");
			sqb.setT_qllx_gzwsyq("1");
		} else if ("19".equals(this.xmxx.qllx)) {
			sqb.setT_qllx_19("1");
		} else if ("20".equals(this.xmxx.qllx)) {
			sqb.setT_qllx_qt("1");
		} else if ("21".equals(this.xmxx.qllx)) {
			sqb.setT_qllx_qt("1");
		} else if ("22".equals(this.xmxx.qllx)) {
			sqb.setT_qllx_qt("1");
		} else if ("23".equals(this.xmxx.qllx)) {
			sqb.setT_qllx_23("1");
		} else if ("24".equals(this.xmxx.qllx)) {
			sqb.setT_qllx_11("1");
			sqb.setT_qllx_sllmsyq("1");
		} else if ("36".equals(this.xmxx.qllx)) {
			sqb.setT_qllx_11("1");
			sqb.setT_qllx_sllmsyq("1");
		} else {
			sqb.setT_qllx_qt("1");
		}
		sqb.setBh(this.xmxx.ywh);
		sqb.setRq(this.xmxx.sjrq);
		sqb.setSjr(this.xmxx.sjr);
		sqb.setDw(this.xmxx.dw);
		sqb.setQllx(this.xmxx.qllx);
		sqb.setDjlx(this.xmxx.djlx);
		sqb.setQllxmc(this.xmxx.qllxmc);
		sqb.setDjlxmc(this.xmxx.djlxmc);
		sqb.setQlrxm(this.qlr.qlrxm);
		sqb.setQlrxm_gyfs(this.qlr.qlrxm_gyfs);
		sqb.setQlrxm_gyfs_bl(this.qlr.qlrxm_gyfs_bl);
		sqb.setZjzl(this.qlr.qlrzjzl);
		sqb.setZjh(this.qlr.qlrzjh);
		sqb.setDz(this.qlr.qlrdz);
		sqb.setYb(this.qlr.qlryb);
		sqb.setFddbr(this.qlr.qlrfddbr);
		sqb.setFddbrdh(this.qlr.fddbrdh);
		sqb.setFddbrzjhm(this.qlr.fddbrzjhm);
		sqb.setDh(this.qlr.qlrdh);
		sqb.setDlrxm(this.qlr.qlrdlrxm);
		sqb.setDlrdh(this.qlr.qlrdlrdh);
		sqb.setDljgmc(this.qlr.qlrdljgmc);
		sqb.setDlrsfzh(this.qlr.dlrsfzh);

		sqb.setYwrxm_gyfs(this.ywr.qlrxm_gyfs);
		sqb.setZWR(this.ywr.zwr);
		sqb.setQlrxm1(this.ywr.qlrxm);
		sqb.setZjzl1(this.ywr.qlrzjzl);
		sqb.setZjh1(this.ywr.qlrzjh);
		sqb.setDz1(this.ywr.qlrdz);
		sqb.setYb1(this.ywr.qlryb);
		sqb.setFddbr1(this.ywr.qlrfddbr);
		sqb.setFddbrdh1(this.ywr.fddbrdh);
		sqb.setFddbrzjhm1(this.ywr.fddbrzjhm);
		sqb.setDh1(this.ywr.qlrdh);
		sqb.setDlrxm1(this.ywr.qlrdlrxm);
		sqb.setDlrdh1(this.ywr.qlrdlrdh);
		sqb.setDljgmc1(this.ywr.qlrdljgmc);
		sqb.setDlrsfzh1(this.ywr.dlrsfzh);
		//利害关系人信息
		sqb.setQlrxm2(this.lhgxr.qlrxm);
		sqb.setZjzl2(this.lhgxr.qlrzjzl);
		sqb.setZjh2(this.lhgxr.qlrzjh);
		sqb.setDz2(this.lhgxr.qlrdz);
		sqb.setYb2(this.lhgxr.qlryb);
		sqb.setFddbr2(this.lhgxr.qlrfddbr);
		sqb.setFddbrdh2(this.lhgxr.fddbrdh);
		sqb.setFddbrzjhm2(this.lhgxr.fddbrzjhm);
		sqb.setDh2(this.lhgxr.qlrdh);
		sqb.setDlrxm2(this.lhgxr.qlrdlrxm);
		sqb.setDlrdh2(this.lhgxr.qlrdlrdh);
		sqb.setDljgmc2(this.lhgxr.qlrdljgmc);
		sqb.setDlrsfzh2(this.lhgxr.dlrsfzh);

		sqb.setMainqllx(this.bdcdy.mainqllx);

		String xmbhcondition = ProjectHelper.GetXMBHCondition(this.xmbh);
		List<BDCS_QLR_GZ> QLR_GZ = dao.getDataList(BDCS_QLR_GZ.class, xmbhcondition + " ORDER BY SXH");
		if (QLR_GZ != null && QLR_GZ.size() > 0) {
			String gyqk = ConstHelper.getNameByValue("GYFS", QLR_GZ.get(0).getGYFS());
			sqb.setGyqk(gyqk);
		} else {
			sqb.setGyqk(this.bdcdy.gyqk);
		}

		sqb.setQlxz(this.bdcdy.qlxz);
		sqb.setSyqx(this.bdcdy.syqx);
		sqb.setZl(this.bdcdy.zl);
		sqb.setBdcdyh(this.bdcdy.bdcdyh);
		sqb.setBdclx(this.bdcdy.bdclx);
		if ("土地".equals(this.bdcdy.bdclx) || "土地/房屋".equals(this.bdcdy.bdclx)) {
			this.bdcdy.bdclx_tdfw = this.bdcdy.bdclx;
		} else {
			this.bdcdy.bdclx_tdfw = "土地";
		}
		sqb.setBdclx_tdfw(this.bdcdy.bdclx_tdfw);
		sqb.setMj(this.bdcdy.mj);
		sqb.setTdftmj_fwjzmj(this.bdcdy.tdftmj_fwjzmj);
		sqb.setYt(this.bdcdy.yt);
		sqb.setYbdcqzsh(this.bdcdy.ybdcqzsh);
		sqb.setYhlx(this.bdcdy.yhlx);
		sqb.setGzwlx(this.bdcdy.gzwlx);
		sqb.setLz(this.bdcdy.getLz());
		sqb.setBdbe(this.qlxx.bdbzqse);
		sqb.setQx(this.qlxx.zwlxqx);
		sqb.setDyfw(this.qlxx.zjjzwdyfw);
		sqb.setXydzl(this.qlxx.xydzl);
		sqb.setXydbdcdyh(this.qlxx.xydbdcdyh);
		sqb.setDjyy(this.qlxx.djyy);
		sqb.setZsbs(this.qlxx.zsbs);
		sqb.setFbcz(this.qlxx.czfs);
		sqb.setBz(this.qlxx.bz);
		sqb.setFj(this.qlxx.fj);
		sqb.setSqrqz("");
		sqb.setSqrqz2("");
		sqb.setDlrqz("");
		sqb.setDlrqz2("");
		sqb.setQzrq("");
		sqb.setQzrq("");
		sqb.setQzrq2("");

		// 预告登记状态、抵押状态、查封状态liangc
		sqb.setYgzt(this.bdcdy.ygzt);
		sqb.setDyzt(this.bdcdy.dyzt);
		sqb.setCfzt(this.bdcdy.cfzt);
		
		//流程名称
		sqb.setProdef_name(this.xmxx.prodef_name);

		sqb.setFwxz(this.bdcdy.fwxz);

		sqb.setCsnr(this.xmxx.csyj);
		sqb.setFsnr(this.xmxx.fsyj);
		sqb.setHdnr(this.xmxx.spyj);
		sqb.setScr(this.xmxx.csyj_scr);
		sqb.setScr2(this.xmxx.fsyj_scr);
		sqb.setFzr("");
		sqb.setScrq(this.xmxx.csyj_scrq);
		sqb.setScrq2(this.xmxx.fsyj_scrq);
		sqb.setSpyjs(this.xmxx.spyjs);
		sqb.setSpdys(this.xmxx.spdys);
		List<Map<String, String>> houses = new ArrayList<Map<String, String>>();
		if (this.bdcdy.children != null && this.bdcdy.children.size() > 0) {
			// BG027和BG028抵押物变化的流程只显示抵押的单元
			BDCS_XMXX xmxx = Global.getXMXXbyXMBH(xmbh);
			RegisterWorkFlow flow = null;
			String flowName = null;
			if (xmxx != null) {
				if (!StringHelper.isEmpty(xmxx.getPROJECT_ID())) {
					flow = HandlerFactory.getWorkflow(xmxx.getPROJECT_ID());
					flowName = StringHelper.isEmpty(flow.getName()) ? "" : flow.getName();
				}
			}
			String zl = "", yt = "", bdcdyh = "", mj = "", ybdcqzh = "";
			int count = 0;
			double zmj = 0.0;
			long qlcount = dao.getCountByFullSql(
					"FROM BDCK.BDCS_QL_GZ WHERE XMBH ='" + xmbh + "' AND (ISCANCEL='0' OR ISCANCEL='2')");
			String configSBT = ConfigHelper.getNameByValue("SQSPBDY_BDCDYH_YT");
			bdcdy.children = ObjectHelper.SortList(bdcdy.children);
			for (int i = 0; i < bdcdy.children.size(); i++) {
				BDCDY _house = bdcdy.children.get(i);
				// BG027和BG028抵押物变化的流程只显示抵押的单元
				if (("BG027".equals(flowName) || "BG028".equals(flowName)) && qlcount > 0) {
					List<Rights> QL = RightsTools.loadRightsByCondition(DJDYLY.GZ, "XMBH ='" + xmbh + "' AND BDCDYH ='"
							+ _house.bdcdyh + "' AND (ISCANCEL='0' OR ISCANCEL='2')");
					if (QL != null && QL.size() > 0) {
						// if ("0".equals(QL.get(0).getISCANCEL()) ||
						// "2".equals(QL.get(0).getISCANCEL())) {
						// 申请报附表（单元）
						Map<String, String> map = new HashMap<String, String>();
						map.put("H_BDCDYH", _house.bdcdyh);
						map.put("H_FH", _house.fh);
						map.put("H_SCJZMJ", _house.jzmj);
						map.put("H_SCTNJZMJ", _house.tnmj);
						map.put("H_SCFTJZMJ", _house.ftmj);
						map.put("H_FTTDMJ", _house.fttdmj);
						map.put("H_SJCS", _house.szcs);
						map.put("H_GHYT", _house.ghyt);
						map.put("H_ZRZH", _house.zrzh);
						map.put("H_ZL", _house.zl);
						map.put("H_TNMJ", _house.zl);
						map.put("H_BDCQZH", _house.bdcqzh);
						map.put("H_YBDCQZH", _house.ybdcqzsh);
						map.put("H_BDCQZHEX", _house.bdcqzhEx);
						map.put("H_FWZTEX", _house.fwztEx);
						map.put("H_QLXZEX", _house.qlxz);
						map.put("H_FWZRZZL", fwzrzzl);
						map.put("fwxz", _house.fwxz);
						houses.add(map);

						// 申请表--部分字段显示为等几个
						// if (QL.size() >1 ) {
						if (Character.isDigit(configSBT.charAt(0))) {
							if (count == Integer.parseInt(configSBT) - 1 && qlcount > Integer.parseInt(configSBT)) {
								zl = _house.zl + " 等" + qlcount + "个";
								yt += _house.yt + " 等" + qlcount + "个";
								bdcdyh += _house.bdcdyh + " 等" + qlcount + "个";
								// ybdcqzh += _house.ybdcqzsh + " 等" + qlcount +
								// "个";
								ybdcqzh += _house.ybdcqzsh != null ? _house.ybdcqzsh + " 等" + qlcount + "个" : " ";
							} else if (count < Integer.parseInt(configSBT) - 1) {
								zl += _house.zl + ",";
								yt += _house.yt + ",";
								bdcdyh += _house.bdcdyh + ",";
								ybdcqzh += _house.ybdcqzsh != null ? _house.ybdcqzsh + "," : " ";
							} else if (count == Integer.parseInt(configSBT) - 1) {
								zl += _house.zl;
								yt += _house.yt;
								bdcdyh += _house.bdcdyh;
								ybdcqzh += _house.ybdcqzsh != null ? _house.ybdcqzsh + "," : " ";
							}
						}
						zmj += StringHelper.getDouble(_house.getJzmj());
						count++;
						// }
						// }

					}
				} else {
					Map<String, String> map = new HashMap<String, String>();
					map.put("H_BDCDYH", _house.bdcdyh);
					map.put("H_FH", _house.fh);
					map.put("H_SCJZMJ", _house.jzmj);
					map.put("H_SCTNJZMJ", _house.tnmj);
					map.put("H_SCFTJZMJ", _house.ftmj);
					map.put("H_FTTDMJ", _house.fttdmj);
					map.put("H_SJCS", _house.szcs);
					map.put("H_GHYT", _house.ghyt);
					map.put("H_ZRZH", _house.zrzh);
					map.put("H_ZL", _house.zl);
					map.put("H_TNMJ", _house.zl);
					map.put("H_BDCQZH", _house.bdcqzh);
					map.put("H_YBDCQZH", _house.ybdcqzsh);
					map.put("H_BDCQZHEX", _house.bdcqzhEx);
					map.put("H_FWZTEX", _house.fwztEx);
					map.put("H_QLXZEX", _house.qlxz);
					map.put("H_FWZRZZL", fwzrzzl);
					map.put("H_YGZT", _house.ygzt);
					map.put("H_DYZT", _house.dyzt);
					map.put("H_CFZT", _house.cfzt);
					houses.add(map);
				}
			}
			if (zmj > 0) {
				mj = "总面积：" + StringHelper.formatDouble(zmj);
				sqb.setMj(mj);
			}
			if (zl.length() > 0)
				sqb.setZl(zl);
			if (yt.length() > 0)
				sqb.setYt(yt);
			if (bdcdyh.length() > 0)
				sqb.setBdcdyh(bdcdyh);
			if (ybdcqzh.length() > 0)
				sqb.setYbdcqzsh(ybdcqzh);
		}
		List<Map> djfz = dao.getDataListByFullSql("SELECT * FROM bdck.BDCS_DJFZ WHERE XMBH='"+this.xmbh+"'");
		Map<String, String> ex_attr = new HashMap<String, String>();
		ex_attr.put("csyj", this.xmxx.csyj);
		ex_attr.put("csyj_scr", this.xmxx.csyj_scr);
		ex_attr.put("csyj_scr_src", this.xmxx.csyj_scr_src);
		ex_attr.put("csyj_scrq", this.xmxx.csyj_scrq);
		ex_attr.put("fsyj", this.xmxx.fsyj);
		ex_attr.put("fsyj_scr", this.xmxx.fsyj_scr);
		ex_attr.put("fsyj_scr_src", this.xmxx.fsyj_scr_src);
		ex_attr.put("fsyj_scrq", this.xmxx.fsyj_scrq);
		ex_attr.put("spyj", this.xmxx.spyj);
		ex_attr.put("spyj_scr", this.xmxx.spyj_scr);
		ex_attr.put("spyj_scr_src", this.xmxx.spyj_scr_src);
		ex_attr.put("spyj_scrq", this.xmxx.spyj_scrq);
		ex_attr.put("ywlx", this.xmxx.ywlx);
		ex_attr.put("EX_DBR", this.qlxx.dbr);
		ex_attr.put("EX_DJSJ", this.qlxx.dbrq);
		ex_attr.put("EX_SZRY", this.xmxx.szry);
		ex_attr.put("EX_SZSJ", this.xmxx.szsj);
		ex_attr.put("EX_FZRY", this.xmxx.fzry);
		ex_attr.put("EX_FZSJ", this.xmxx.fzsj);
		ex_attr.put("EX_LZSJ", this.xmxx.lzsj);
		if (!StringHelper.isEmpty(djfz) && djfz.size() > 0 ) {
			ex_attr.put("EX_LZRXM", (String)djfz.get(0).get("LZRXM"));
			ex_attr.put("EX_LZRZJHM", (String)djfz.get(0).get("LZRZJHM"));
		}
		ex_attr.put("EX_GDZR", this.xmxx.gdzr);
		ex_attr.put("EX_GDSJ", this.xmxx.gdsj);
		ex_attr.put("EX_BDCQZH", this.qlxx.bdcqzh);
		// 开发区复审
		ex_attr.put("kfqfsyj", this.xmxx.kfqfsyj);
		ex_attr.put("kfqfsyj_scr", this.xmxx.kfqfsyj_scr);
		ex_attr.put("kfqfsyj_scr_src", this.xmxx.kfqfsyj_scr_src);
		ex_attr.put("kfqfsyj_scrq", this.xmxx.kfqfsyj_scrq);
		// 审核
		ex_attr.put("shyj", this.xmxx.shyj);
		ex_attr.put("shyj_scr", this.xmxx.shyj_scr);
		ex_attr.put("shyj_scr_src", this.xmxx.shyj_scr_src);
		ex_attr.put("shyj_scrq", this.xmxx.shyj_scrq);

		if (this.xmxx.otheryj != null && this.xmxx.otheryj.size() > 0) {
			Iterator<Map.Entry<String, String>> iter = this.xmxx.otheryj.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry<String, String> entry = iter.next();
				String key = entry.getKey();
				String val = entry.getValue();
				ex_attr.put(key, val);
			}
		}

		sqb.setEx(ex_attr);
		sqb.setHouses(houses);
		sqb.setSqrs(this.sqrs);
		sqb.setSqrs2(this.sqrs2);
		sqb.setBdcqzh(this.bdcdy.bdcqzh);
		sqb.setBdcqzh_all(this.bdcdy.bdcqzh_all);
		sqb.setZsbh(this.bdcdy.zsbh);
		return sqb;
	}
}
