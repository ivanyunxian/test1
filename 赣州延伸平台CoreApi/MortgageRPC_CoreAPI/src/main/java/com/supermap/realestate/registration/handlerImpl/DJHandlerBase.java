package com.supermap.realestate.registration.handlerImpl;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.Types;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;
import javax.xml.rpc.ParameterMode;

import net.sf.json.JSONObject;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.encoding.XMLType;
import org.apache.commons.beanutils.PropertyUtils;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.jcraft.jsch.ChannelSftp;
import com.supermap.realestate.registration.ViewClass.ProjectInfo;
import com.supermap.realestate.registration.ViewClass.UnitTree;
import com.supermap.realestate.registration.config.ChargeParam;
import com.supermap.realestate.registration.dataExchange.DJFDJFZ;
import com.supermap.realestate.registration.dataExchange.DJFDJGD;
import com.supermap.realestate.registration.dataExchange.DJFDJSF;
import com.supermap.realestate.registration.dataExchange.DJFDJSH;
import com.supermap.realestate.registration.dataExchange.DJFDJSJ;
import com.supermap.realestate.registration.dataExchange.DJFDJSQR;
import com.supermap.realestate.registration.dataExchange.DJFDJSZ;
import com.supermap.realestate.registration.dataExchange.DJTDJSLSQ;
import com.supermap.realestate.registration.dataExchange.FJF100;
import com.supermap.realestate.registration.dataExchange.Head;
import com.supermap.realestate.registration.dataExchange.Message;
import com.supermap.realestate.registration.dataExchange.ZTTGYQLR;
import com.supermap.realestate.registration.dataExchange.exchangeFactory;
import com.supermap.realestate.registration.dataExchange.cfdj.QLFQLCFDJ;
import com.supermap.realestate.registration.dataExchange.dyq.QLFQLDYAQ;
import com.supermap.realestate.registration.dataExchange.fwsyq.KTTFWC;
import com.supermap.realestate.registration.dataExchange.fwsyq.KTTFWH;
import com.supermap.realestate.registration.dataExchange.fwsyq.KTTFWLJZ;
import com.supermap.realestate.registration.dataExchange.fwsyq.KTTFWZRZ;
import com.supermap.realestate.registration.dataExchange.fwsyq.QLFFWFDCQDZXM;
import com.supermap.realestate.registration.dataExchange.fwsyq.QLTFWFDCQDZ;
import com.supermap.realestate.registration.dataExchange.fwsyq.QLTFWFDCQYZ;
import com.supermap.realestate.registration.dataExchange.hy.KTFZHBHQK;
import com.supermap.realestate.registration.dataExchange.hy.KTFZHYHYDZB;
import com.supermap.realestate.registration.dataExchange.hy.KTFZHYHZK;
import com.supermap.realestate.registration.dataExchange.hy.KTTZHJBXX;
import com.supermap.realestate.registration.dataExchange.hy.QLFQLHYSYQ;
import com.supermap.realestate.registration.dataExchange.hy.ZHK105;
import com.supermap.realestate.registration.dataExchange.lq.QLTQLLQ;
import com.supermap.realestate.registration.dataExchange.nydsyq.QLNYDSYQ;
import com.supermap.realestate.registration.dataExchange.shyq.KTFZDBHQK;
import com.supermap.realestate.registration.dataExchange.shyq.KTTGYJZD;
import com.supermap.realestate.registration.dataExchange.shyq.KTTGYJZX;
import com.supermap.realestate.registration.dataExchange.shyq.KTTZDJBXX;
import com.supermap.realestate.registration.dataExchange.shyq.QLFQLJSYDSYQ;
import com.supermap.realestate.registration.dataExchange.shyq.ZDK103;
import com.supermap.realestate.registration.dataExchange.syq.QLFQLTDSYQ;
import com.supermap.realestate.registration.dataExchange.utils.ValidataXML;
import com.supermap.realestate.registration.dataExchange.utils.packageXml;
import com.supermap.realestate.registration.dataExchange.zxdj.QLFZXDJ;
import com.supermap.realestate.registration.factorys.HandlerFactory;
import com.supermap.realestate.registration.handler.DJHandler;
import com.supermap.realestate.registration.mapping.HandlerMapping.RegisterWorkFlow;
import com.supermap.realestate.registration.model.BDCS_C_GZ;
import com.supermap.realestate.registration.model.BDCS_C_XZ;
import com.supermap.realestate.registration.model.BDCS_DJDY_GZ;
import com.supermap.realestate.registration.model.BDCS_DJDY_LS;
import com.supermap.realestate.registration.model.BDCS_DJDY_XZ;
import com.supermap.realestate.registration.model.BDCS_DYBG;
import com.supermap.realestate.registration.model.BDCS_DYXZ;
import com.supermap.realestate.registration.model.BDCS_FSQL_GZ;
import com.supermap.realestate.registration.model.BDCS_FSQL_LS;
import com.supermap.realestate.registration.model.BDCS_FSQL_XZ;
import com.supermap.realestate.registration.model.BDCS_H_GZ;
import com.supermap.realestate.registration.model.BDCS_H_GZY;
import com.supermap.realestate.registration.model.BDCS_H_LS;
import com.supermap.realestate.registration.model.BDCS_H_LSY;
import com.supermap.realestate.registration.model.BDCS_H_XZ;
import com.supermap.realestate.registration.model.BDCS_H_XZY;
import com.supermap.realestate.registration.model.BDCS_LJZ_GZ;
import com.supermap.realestate.registration.model.BDCS_LJZ_XZ;
import com.supermap.realestate.registration.model.BDCS_NYD_GZ;
import com.supermap.realestate.registration.model.BDCS_NYD_LS;
import com.supermap.realestate.registration.model.BDCS_NYD_XZ;
import com.supermap.realestate.registration.model.BDCS_QDZR_GZ;
import com.supermap.realestate.registration.model.BDCS_QDZR_LS;
import com.supermap.realestate.registration.model.BDCS_QDZR_XZ;
import com.supermap.realestate.registration.model.BDCS_QLR_GZ;
import com.supermap.realestate.registration.model.BDCS_QLR_LS;
import com.supermap.realestate.registration.model.BDCS_QLR_XZ;
import com.supermap.realestate.registration.model.BDCS_QL_GZ;
import com.supermap.realestate.registration.model.BDCS_QL_LS;
import com.supermap.realestate.registration.model.BDCS_QL_XZ;
import com.supermap.realestate.registration.model.BDCS_REPORTINFO;
import com.supermap.realestate.registration.model.BDCS_SHYQZD_GZ;
import com.supermap.realestate.registration.model.BDCS_SHYQZD_LS;
import com.supermap.realestate.registration.model.BDCS_SHYQZD_XZ;
import com.supermap.realestate.registration.model.BDCS_SLLM_GZ;
import com.supermap.realestate.registration.model.BDCS_SLLM_LS;
import com.supermap.realestate.registration.model.BDCS_SLLM_XZ;
import com.supermap.realestate.registration.model.BDCS_SQR;
import com.supermap.realestate.registration.model.BDCS_SYQZD_GZ;
import com.supermap.realestate.registration.model.BDCS_SYQZD_XZ;
import com.supermap.realestate.registration.model.BDCS_TDYT_GZ;
import com.supermap.realestate.registration.model.BDCS_TDYT_XZ;
import com.supermap.realestate.registration.model.BDCS_XMXX;
import com.supermap.realestate.registration.model.BDCS_YHZK_GZ;
import com.supermap.realestate.registration.model.BDCS_YHZK_XZ;
import com.supermap.realestate.registration.model.BDCS_ZH_GZ;
import com.supermap.realestate.registration.model.BDCS_ZH_XZ;
import com.supermap.realestate.registration.model.BDCS_ZRZ_GZ;
import com.supermap.realestate.registration.model.BDCS_ZRZ_LSY;
import com.supermap.realestate.registration.model.BDCS_ZRZ_XZ;
import com.supermap.realestate.registration.model.BDCS_ZRZ_XZY;
import com.supermap.realestate.registration.model.BDCS_ZS_GZ;
import com.supermap.realestate.registration.model.BDCS_ZS_LS;
import com.supermap.realestate.registration.model.BDCS_ZS_XZ;
import com.supermap.realestate.registration.model.YC_SC_H_XZ;
import com.supermap.realestate.registration.model.interfaces.AgriculturalLand;
import com.supermap.realestate.registration.model.interfaces.Building;
import com.supermap.realestate.registration.model.interfaces.Floor;
import com.supermap.realestate.registration.model.interfaces.House;
import com.supermap.realestate.registration.model.interfaces.LandAttach;
import com.supermap.realestate.registration.model.interfaces.LogicBuilding;
import com.supermap.realestate.registration.model.interfaces.Nyd;
import com.supermap.realestate.registration.model.interfaces.OwnerLand;
import com.supermap.realestate.registration.model.interfaces.RealUnit;
import com.supermap.realestate.registration.model.interfaces.Rights;
import com.supermap.realestate.registration.model.interfaces.Sea;
import com.supermap.realestate.registration.model.interfaces.SubRights;
import com.supermap.realestate.registration.model.interfaces.UseLand;
import com.supermap.realestate.registration.model.interfaces.YHZK;
import com.supermap.realestate.registration.service.Sender.dataReport;
import com.supermap.realestate.registration.service.impl.ProjectServiceImpl;
import com.supermap.realestate.registration.tools.GeoOperateTools;
import com.supermap.realestate.registration.tools.GeoOperateTools.CopyOperateMode;
import com.supermap.realestate.registration.tools.ProcedureParam;
import com.supermap.realestate.registration.tools.ProcedureTools;
import com.supermap.realestate.registration.tools.RightsTools;
import com.supermap.realestate.registration.tools.ShareMsgTools;
import com.supermap.realestate.registration.tools.UnitTools;
import com.supermap.realestate.registration.util.ConfigHelper;
import com.supermap.realestate.registration.util.ConstValue;
import com.supermap.realestate.registration.util.ConstValue.BDCDYLX;
import com.supermap.realestate.registration.util.ConstValue.CZFS;
import com.supermap.realestate.registration.util.ConstValue.DJDYLY;
import com.supermap.realestate.registration.util.ConstValue.DJLX;
import com.supermap.realestate.registration.util.ConstValue.QLLX;
import com.supermap.realestate.registration.util.ConstValue.RECCODE;
import com.supermap.realestate.registration.util.ConstValue.SQRLB;
import com.supermap.realestate.registration.util.ConstValue.ZSBS;
import com.supermap.realestate.registration.util.DateUtil;
import com.supermap.realestate.registration.util.Dom4jXmlUtil;
import com.supermap.realestate.registration.util.Global;
import com.supermap.realestate.registration.util.HttpRequest;
import com.supermap.realestate.registration.util.HttpRequest.ParamInfo;
import com.supermap.realestate.registration.util.HttpRequest.WSDLInfo;
import com.supermap.realestate.registration.util.ObjectHelper;
import com.supermap.realestate.registration.util.ProjectHelper;
import com.supermap.realestate.registration.util.RsaXmlUtil;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.realestate.registration.util.XmlUtil;
import com.supermap.realestate.registration.util.YwLogUtil;
import com.supermap.realestate_gx.registration.util.StringUtil;
import com.supermap.wisdombusiness.core.SuperHelper;
import com.supermap.wisdombusiness.core.SuperSpringContext;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import com.supermap.wisdombusiness.workflow.model.Wfi_ActInst;

/**
 * 所有登记处理类的基类
 * 
 * @author 刘树峰
 *
 */
public class DJHandlerBase {

	@Autowired
	private CommonDao baseCommonDao;

	@Autowired
	private ShareMsgTools shareMsgTools;

	/**
	 * 流程编号
	 */
	private String project_id;

	/**
	 * 项目编号
	 */
	private String xmbh;

	/**
	 * 登记类型
	 */
	private DJLX djlx;

	/**
	 * 权利类型
	 */
	private QLLX qllx;

	/**
	 * 受理类型1
	 */
	private String sllx1;

	/**
	 * 受理类型2
	 */
	private String sllx2;

	private String prjNumber;

	private Date createTime;

	/**
	 * 错误信息
	 */
	private String errMessage;

	// TODO @@@@刁立伟，createTime，没有赋值的地方，用来干啥的？ ----用来数据上报的时候取项目创建时间
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getPrjNumber() {
		return prjNumber;
	}

	public void setPrjNumber(String prjNumber) {
		this.prjNumber = prjNumber;
	}

	/**
	 * 不动产单元类型
	 */
	private BDCDYLX bdcdylx;

	public BDCDYLX getBdcdylx() {
		return bdcdylx;
	}

	public DJLX getDjlx() {
		return djlx;
	}

	public QLLX getQllx() {
		return qllx;
	}

	public String getSllx1() {
		return sllx1;
	}

	public String getSllx2() {
		return sllx2;
	}

	public String getXMBH() {
		return this.xmbh;
	}

	public String getProject_id() {
		return project_id;
	}

	public void setProject_id(String project_id) {
		this.project_id = project_id;
	}

	protected CommonDao getCommonDao() {
		return baseCommonDao;
	}

	protected ShareMsgTools getShareMsgTools() {
		return shareMsgTools;
	}

	public String getErrMessage() {
		return errMessage;
	}

	public void setErrMessage(String errMessage) {
		this.errMessage = errMessage;
	}

	/**
	 * 构造函数
	 * 
	 * @Author 刘树峰
	 * @param info
	 */
	public DJHandlerBase(ProjectInfo info) {
		if (info != null) {
			this.xmbh = info.getXmbh();
			this.djlx = DJLX.initFrom(info.getDjlx());
			this.qllx = QLLX.initFrom(info.getQllx());
			this.sllx1 = info.getSllx1();
			this.sllx2 = info.getSllx2();
			this.bdcdylx = BDCDYLX.initFrom(info.getBdcdylx());
			this.project_id = info.getProject_id();
			String[] strs = project_id.split("-");
			this.prjNumber = strs[strs.length - 1];
		}
		this.baseCommonDao = SuperSpringContext.getContext().getBean(CommonDao.class);
		this.shareMsgTools = SuperSpringContext.getContext().getBean(ShareMsgTools.class);
	}

	/**
	 * 生成新的登记单元
	 * @Title: createDJDY
	 * @author:liushufeng
	 * @date：2015年10月28日 下午6:36:07
	 * @param unit
	 * @param ly
	 * @return
	 */
	protected BDCS_DJDY_GZ createDJDY(RealUnit unit, DJDYLY ly) {
		BDCS_DJDY_GZ djdy = new BDCS_DJDY_GZ();
		djdy.setXMBH(xmbh);
		djdy.setDJDYID(getPrimaryKey());
		djdy.setBDCDYH(unit.getBDCDYH());
		djdy.setBDCDYID(unit.getId());
		djdy.setLY(ly.Value);
		djdy.setBDCDYLX(unit.getBDCDYLX().Value);
		return djdy;
	}

	/**
	 * 根据项目编号和不动产单元ID（BDCDYID）移除工作层中相应的登记单元记录
	 * @Title: removeDJDY
	 * @author:liushufeng
	 * @date：2015年10月28日 下午8:59:09
	 * @param bdcdyid
	 * @return
	 */
	protected BDCS_DJDY_GZ removeDJDY(String bdcdyid) {
		BDCS_DJDY_GZ djdy = null;
		String hql = MessageFormat.format("XMBH=''{0}'' AND BDCDYID=''{1}''", getXMBH(), bdcdyid);
		List<BDCS_DJDY_GZ> list = baseCommonDao.getDataList(BDCS_DJDY_GZ.class, hql);
		if (list != null && list.size() > 0) {
			djdy = list.get(0);
			baseCommonDao.deleteEntity(djdy);
		}
		return djdy;
	}

	/**
	 * 获取项目的登记单元列表，初始、变更、抵押、转移、注销都能用 树里边的oldqlid存的是房屋/宗地的所有权、使用权的
	 * @Author 刘树峰
	 * @CreateTime 2015年6月13日上午12:19:43
	 * @return 登记单元列表
	 */
	protected List<UnitTree> getUnitList() {
		String sql = ProjectHelper.GetXMBHCondition(xmbh);
		CommonDao dao = this.getCommonDao();
		List<UnitTree> list = new ArrayList<UnitTree>();
		List<BDCS_DJDY_GZ> djdys = dao.getDataList(BDCS_DJDY_GZ.class, sql);
		if (djdys != null && djdys.size() > 0) {
			/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~wlb~~~~~~~~~~~~~~~~~仅仅针对解封流程，不影响其他~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
			DJHandler dj = HandlerFactory.createDJHandler(xmbh);           
			boolean isCFDJ_ZX_HouseHandler =dj.getClass().getName().equals("com.supermap.realestate.registration.handlerImpl.CFDJ_ZX_HouseHandler");
			/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
			if(djdys.size()>20&&(BDCDYLX.H.Value.equals(djdys.get(0).getBDCDYLX()) || BDCDYLX.YCH.Value.equals(djdys.get(0).getBDCDYLX()))&& isCFDJ_ZX_HouseHandler == false){
				list = getHouseTreeEx();
				return list;
			}
			if (ConfigHelper.getNameByValue("XZQHDM").startsWith("510") || ConfigHelper.getNameByValue("XZQHDM").startsWith("130"))// 目前让泸州版本先用着，
			{
				if (BDCDYLX.H.Value.equals(djdys.get(0).getBDCDYLX()) || BDCDYLX.YCH.Value.equals(djdys.get(0).getBDCDYLX())) {
					list = getHouseTree();
					return list;
				}
			}
			for (int i = 0; i < djdys.size(); i++) {
				//房地一体无需加载土地单元
				boolean isZY_FDYT =dj.getClass().getName().equals("com.supermap.realestate.registration.handlerImpl.ZYDJHandler_HouseAndLand");
				boolean isDY_FDYT =dj.getClass().getName().equals("com.supermap.realestate.registration.handlerImpl.DYDJHandler_HouseAndLand");
				if((isZY_FDYT||isDY_FDYT)&&"02".equals(djdys.get(i).getBDCDYLX())) {
					continue;
				}
				BDCS_DJDY_GZ djdy = djdys.get(i);
				UnitTree tree = new UnitTree();
				BDCS_QL_GZ ql = getQL(djdy.getDJDYID());
				if (ql != null) {
					tree.setQlid(ql.getId());
					//先设定默认值，后面会被再覆盖掉 wuzhu
					tree.setOldqlid(ql.getLYQLID());
					tree.setlyqlid(ql.getLYQLID());
					if(ql.getBDCQZH() != null){
						tree.setBdcqzh(ql.getBDCQZH());
					}
				}
				tree.setId(djdy.getBDCDYID());
				tree.setBdcdyid(djdy.getBDCDYID());
				tree.setBdcdylx(djdy.getBDCDYLX());
				tree.setDjdyid(djdy.getDJDYID());
				String ly = StringUtils.isEmpty(djdy.getLY()) ? "gz" : DJDYLY.initFrom(djdy.getLY()) == null ? "gz" : DJDYLY.initFrom(djdy.getLY()).Name;
				tree.setLy(ly);
				String zl = getZLex(tree, djdy.getDJDYID(), djdy.getBDCDYLX(), djdy.getBDCDYID(), ly);
				if (StringHelper.isEmpty(zl)) {
					String Baseworkflow_ID =ProjectHelper.GetPrjInfoByXMBH(xmbh).getBaseworkflowcode();
					if ("BG044".equals(Baseworkflow_ID)) {
						RealUnit unit = UnitTools.loadUnit(BDCDYLX.YCH, DJDYLY.XZ, djdy.getBDCDYID());
						if (unit != null) {
							if (!StringHelper.isEmpty(unit.getZL())) {
								zl = unit.getZL();
							}
							if (!StringHelper.isEmpty(unit.getMJ())) {
								tree.setMj(unit.getMJ());
							}
							if (unit instanceof LandAttach) {
								LandAttach landattach = (LandAttach) unit;
								tree.setZdbdcdyid(landattach.getZDBDCDYID());
							}

							if (unit instanceof House) {
								House house = (House) unit;
								tree.setCid(house.getCID());
								tree.setZrzbdcdyid(house.getZRZBDCDYID());
								tree.setLjzbdcdyid(house.getLJZID());
							} else {
								// TODO 刘树峰:获取其他类型不动产单元的坐落
							}
						}
						getRelationRights(DJDYLY.XZ, djdy.getBDCDYID(), tree);
					}
				}
				tree.setText(zl); 
				// 如果是户的话，把房号也加上
				if (djdy.getBDCDYLX().equals(BDCDYLX.H.Value) || BDCDYLX.YCH.Value.equals(djdy.getBDCDYLX())) {
					if (!StringHelper.isEmpty(djdy.getLY())) {
						DJDYLY ely = DJDYLY.initFrom(djdy.getLY());
						if (DJDYLY.XZ.equals(ely)) {
							ely = DJDYLY.LS;
						}
						House house = (House) (UnitTools.loadUnit(BDCDYLX.initFrom(djdy.getBDCDYLX()), ely, djdy.getBDCDYID()));
						if (house != null) {
							String fh = house.getFH();
							tree.setFh(fh);
							tree.setMj(house.getMJ());
							if (StringHelper.isEmpty(house.getFTTDMJ())) {
								tree.setFttdmj(0);
							} else {
								tree.setFttdmj(house.getFTTDMJ());
							}
							tree.setText("坐落:" + zl + "|" + "房号:" + (fh == null ? "" : fh));
							if(house.getDYH() != null){
								tree.setDyh(house.getDYH());
							}
							if(house.getQSC() != null){
								tree.setQsc(house.getQSC());
							}
							if(house.getZRZH() != null){
								tree.setZrzh(house.getZRZH());
							}
						}
					}
				}
				tree.setZl(zl);
				tree.setBdcdyh(djdy.getBDCDYH());
				list.add(tree);
			}
		}
		list = ObjectHelper.SortList(list);
		return list;
	}

	/**
	 * 获取现状权利列表Tree，通过工作层权利表中LYQLID获取
	 * 
	 * @Author 俞学斌
	 * @CreateTime 2015年6月18日上午21:34:43
	 * @return List<UnitTree> 登记单元列表
	 */
	protected List<UnitTree> getRightList() {
		String sql = ProjectHelper.GetXMBHCondition(xmbh);
		CommonDao dao = this.getCommonDao();
		List<String> listDJDYID = new ArrayList<String>();
		List<UnitTree> list = new ArrayList<UnitTree>();
		List<BDCS_DJDY_GZ> djdys = dao.getDataList(BDCS_DJDY_GZ.class, sql);
		if (djdys != null && djdys.size() > 0) {
			if (ConfigHelper.getNameByValue("XZQHDM").startsWith("510"))// 目前让泸州先用着，其它地方暂用
			{
				if (BDCDYLX.H.Value.equals(djdys.get(0).getBDCDYLX()) || BDCDYLX.YCH.Value.equals(djdys.get(0).getBDCDYLX())) {
					list = getHouseRightTree();
					return list;
				}
			}
			for (int i = 0; i < djdys.size(); i++) {
				BDCS_DJDY_GZ djdy = djdys.get(i);
				List<BDCS_QL_GZ> qllist = getCommonDao().getDataList(BDCS_QL_GZ.class, " DJDYID='" + djdy.getDJDYID() + "' AND " + ProjectHelper.GetXMBHCondition(xmbh));
				if (listDJDYID.contains(djdy.getDJDYID())) {
					continue;
				} else {
					listDJDYID.add(djdy.getDJDYID());
				}
				if (qllist != null && qllist.size() > 0) {
					for (int iql = 0; iql < qllist.size(); iql++) {
						UnitTree tree = new UnitTree();
						String qlr="";
						if(!StringHelper.isEmpty(qllist.get(iql).getLYQLID())){
							BDCS_QL_LS ql = getCommonDao().get(BDCS_QL_LS.class, qllist.get(iql).getLYQLID());
							if(ql!=null){
								qlr = getQLR(ql.getId());
								tree.setOldqlid(ql.getId());
							}
							
						}
						tree.setQlid(qllist.get(iql).getId());

						
						tree.setId(djdy.getBDCDYID());
						tree.setBdcdyid(djdy.getBDCDYID());
						tree.setBdcdylx(djdy.getBDCDYLX());
						tree.setDjdyid(djdy.getDJDYID());
						
						String ly = StringUtils.isEmpty(djdy.getLY()) ? "gz" : DJDYLY.initFrom(djdy.getLY()) == null ? "gz" : DJDYLY.initFrom(djdy.getLY()).Name;
						tree.setLy(ly);
						String zl = getZL(tree, djdy.getDJDYID(), djdy.getBDCDYLX(), djdy.getBDCDYID(), "xz");
						if (!StringUtils.isEmpty(qlr)) {
							StringBuilder builder = new StringBuilder();
							builder.append(zl).append("-").append(qlr);
							tree.setText(builder.toString());
						} else {
							tree.setText(zl);
						}

						// 如果是户的话，把房号也加上
						if (BDCDYLX.H.Value.equals(djdy.getBDCDYLX()) || BDCDYLX.YCH.Value.equals(djdy.getBDCDYLX())) {
							if (!StringHelper.isEmpty(djdy.getLY())) {
								DJDYLY ely = DJDYLY.initFrom(djdy.getLY());
								House house = (House) (UnitTools.loadUnit(BDCDYLX.initFrom(djdy.getBDCDYLX()), ely, djdy.getBDCDYID()));
								if (house != null) {
									String fh = house.getFH();
									tree.setFh(fh);
									if (StringHelper.isEmpty(house.getMJ())) {
										tree.setMj(0);
									} else {
										tree.setMj(house.getMJ());
									}
									if (StringHelper.isEmpty(house.getFTTDMJ())) {
										tree.setFttdmj(0);
									} else {
										tree.setFttdmj(house.getFTTDMJ());
									}
								}
							}
						}

						list.add(tree);
					}
				}
			}
		}
		/***排序啦 by 赵梦帆 2017-05-18 08:40:24***/
		list = ObjectHelper.SortList(list);
		return list;
	}

	/**
	 * 通过户、预测中房号跟起始层排序获取房屋信息，然后再读取单元信息
	 * @作者 海豹
	 * @创建时间 2015年12月21日下午11:30:56
	 * @param bdcs_djdy_gz
	 * @return
	 */
	private List<UnitTree> getHouseTree() {
		List<UnitTree> list = new ArrayList<UnitTree>();
		String sql = ProjectHelper.GetXMBHCondition(xmbh);
		List<Map> lstdjdy = baseCommonDao.getDataListByFullSql("select LY,BDCDYLX from BDCK.BDCS_DJDY_GZ where " + sql + " group by LY,BDCDYLX");
		for (Map m : lstdjdy) {
			if (!StringHelper.isEmpty(m)) {
				BDCDYLX bdcdylx = BDCDYLX.initFrom(StringHelper.FormatByDatatype(m.get("BDCDYLX")));
				String strly = StringHelper.FormatByDatatype(m.get("LY"));
				DJDYLY dyly = DJDYLY.initFrom(strly);
				if (DJDYLY.XZ.equals(dyly)) {
					dyly = DJDYLY.LS;
				}
				List<RealUnit> lst = UnitTools.loadUnits(bdcdylx, dyly, "id IN ( select BDCDYID from BDCS_DJDY_GZ WHERE LY= '" + strly + "' and " + sql + " ) order by QSC,DYH,FH");
				for (RealUnit unit : lst) {
					if (!StringHelper.isEmpty(unit)) {
						List<BDCS_DJDY_GZ> djdys = baseCommonDao.getDataList(BDCS_DJDY_GZ.class, sql + " AND BDCDYID='" + unit.getId() + "'");
						if (!StringHelper.isEmpty(djdys) && djdys.size() > 0) {
							for (BDCS_DJDY_GZ djdy : djdys) {
								UnitTree tree = new UnitTree();
								Rights right = RightsTools.loadRightsByDJDYID(DJDYLY.GZ, xmbh, djdy.getDJDYID());
								if (!StringHelper.isEmpty(right)) {
									tree.setQlid(right.getId());
									tree.setlyqlid(right.getLYQLID());
								}
								tree.setId(djdy.getBDCDYID());
								tree.setBdcdyid(djdy.getBDCDYID());
								tree.setBdcdylx(djdy.getBDCDYLX());
								tree.setDjdyid(djdy.getDJDYID());
								String ly = StringHelper.isEmpty(djdy.getLY()) ? "gz" : DJDYLY.initFrom(djdy.getLY()) == null ? "gz" : DJDYLY.initFrom(djdy.getLY()).Name;
								tree.setLy(ly);
								if (unit instanceof House) {
									House house = (House) unit;
									String zl = house.getZL();
									tree.setCid(house.getCID());
									tree.setZdbdcdyid(house.getZDBDCDYID());
									tree.setZrzbdcdyid(house.getZRZBDCDYID());
									tree.setLjzbdcdyid(house.getLJZID());
									String fh = house.getFH();
									tree.setFh(fh);
									tree.setMj(house.getMJ());
									tree.setText("坐落:" + zl + "|" + "房号:" + (fh == null ? "" : fh));
									tree.setZl(zl);
								}
								getRelationRights(dyly, unit.getId(), tree);
								list.add(tree);
							}
						}
					}
				}
			}
		}
		return list;
	}
	
	
	/**
	 * 通过户、预测中房号跟起始层排序获取房屋信息，然后再读取单元信息
	 * @作者 海豹
	 * @创建时间 2015年12月21日下午11:30:56
	 * @param bdcs_djdy_gz
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private List<UnitTree> getHouseTreeEx() {
		List<UnitTree> list = new ArrayList<UnitTree>();
		BDCS_XMXX xmxx = Global.getXMXX(this.getProject_id());
		String mj_field="SCJZMJ";
		if(BDCDYLX.YCH.equals(this.getBdcdylx())){
			mj_field="YCJZMJ";
		}
		List<Rights> list_ql_01=RightsTools.loadRightsByCondition(DJDYLY.GZ, "XMBH='"+this.getXMBH()+"'");
		List<Rights> list_ql_03=RightsTools.loadRightsByCondition(DJDYLY.XZ, " DJDYID IN (SELECT DJDYID FROM BDCS_DJDY_GZ WHERE XMBH='"+this.getXMBH()+"') AND QLLX IN ('1','2','3','4','5','6','7','8','10','11','12','15','36')");
		StringBuilder builder_01=new StringBuilder();
		String fh_01 = "";
		String bdcdyh_01 = "";
		int count=0;
		List<BDCS_H_XZ> hxzs_01 = new ArrayList<BDCS_H_XZ>();
		List<BDCS_H_XZY> hxzs_01y = new ArrayList<BDCS_H_XZY>();
		List<BDCS_H_GZ> hgzs_01 = new ArrayList<BDCS_H_GZ>();
		List<BDCS_H_GZY> hgzs_01y = new ArrayList<BDCS_H_GZY>();
		if(list_ql_01.size() > 0){
			
			bdcdyh_01= list_ql_01.get(0).getBDCDYH();
			
		}
		else if(list_ql_01.size() == 0 && list_ql_03.size() > 0){
			bdcdyh_01= list_ql_03.get(0).getBDCDYH();
		}
		
		if(BDCDYLX.YCH.equals(this.getBdcdylx())){
			hxzs_01y = baseCommonDao.getDataList(BDCS_H_XZY.class, "bdcdyh = '"+bdcdyh_01+"'");
			hgzs_01y = baseCommonDao.getDataList(BDCS_H_GZY.class, "bdcdyh = '"+bdcdyh_01+"'");
			
			if(hxzs_01y.size() >0){
				fh_01 = hxzs_01y.get(0).getFH();

				if(fh_01 != null && !"".equals(fh_01)){
					for(int i=0;i<fh_01.length();i++){
						if(fh_01.charAt(i)=='-'){
							count++;
						}
					}
				}
			}
			else if(hgzs_01y.size() >0){
				fh_01 = hgzs_01y.get(0).getFH();

				if(fh_01 != null && !"".equals(fh_01)){
					for(int i=0;i<fh_01.length();i++){
						if(fh_01.charAt(i)=='-'){
							count++;
						}
					}
				}
			}

		}else{
			hxzs_01 = baseCommonDao.getDataList(BDCS_H_XZ.class, "bdcdyh = '"+bdcdyh_01+"'");
			hgzs_01 = baseCommonDao.getDataList(BDCS_H_GZ.class, "bdcdyh = '"+bdcdyh_01+"'");

			if(hxzs_01.size() >0){
				fh_01 = hxzs_01.get(0).getFH();
				if(fh_01 != null && !"".equals(fh_01)){
					for(int i=0;i<fh_01.length();i++){
						if(fh_01.charAt(i)=='-'){
							count++;
						}
					}
				}
			}
			else if(hgzs_01.size() >0){
				fh_01 = hgzs_01.get(0).getFH();
				if(fh_01 != null && !"".equals(fh_01)){
					for(int i=0;i<fh_01.length();i++){
						if(fh_01.charAt(i)=='-'){
							count++;
						}
					}
				}
			}
		}
		builder_01.append("SELECT DJDY.BDCDYID,DJDY.BDCDYLX,DJDY.DJDYID,DY.ZL,DY.FH,DY."+mj_field+" AS MJ,DY.FTTDMJ,DY.CID,");
		builder_01.append("DY.ZDBDCDYID,DY.ZRZBDCDYID,DY.LJZID ");
		builder_01.append("FROM BDCK.BDCS_DJDY_GZ DJDY LEFT JOIN BDCK.");
		builder_01.append(this.getBdcdylx().GZTableName);
		builder_01.append(" DY ON DJDY.BDCDYID=DY.BDCDYID ");
		builder_01.append("WHERE LY IN ('01') AND DJDY.XMBH='");
		builder_01.append(this.getXMBH() + "'");
		/*if(fh_01 != null && !"".equals(fh_01)){
			if(fh_01.indexOf("地下") != -1 || fh_01.indexOf("负") != -1){
				builder_01.append(" order by translate(DY.FH, '#' || translate(DY.FH, '0123456789', '#'), '/')");
			}else if(fh_01.indexOf("-") != -1 && count == 2){
				builder_01.append(" order by substr(DY.FH,1,instr(DY.FH,'-')-1),"
						+ "translate(substr(DY.FH,instr(DY.FH,'-')-6,length(DY.FH)-instr(DY.FH,'-')), '#' || "
						+ "translate(substr(DY.FH,instr(DY.FH,'-')-6,length(DY.FH)-instr(DY.FH,'-')), '0123456789', '#'), '/'),"
						+ "translate(substr(DY.FH,instr(DY.FH,'-')-3,length(DY.FH)-instr(DY.FH,'-')), '#' || "
						+ "translate(substr(DY.FH,instr(DY.FH,'-')-3,length(DY.FH)-instr(DY.FH,'-')), '0123456789', '#'), '/')");
			}
			else if(fh_01.indexOf("-") != -1 && count == 1){
				builder_01.append(" order by substr(DY.FH,1,instr(DY.FH,'-')-1),"
						+ "substr(DY.FH,instr(DY.FH,'-')+1,length(DY.FH)-instr(DY.FH,'-'))");
			}
		}
		else{
			builder_01.append(" order by DY.BDCDYH");

		}*/
		if (!StringHelper.isEmpty(this.getBdcdylx()) && BDCDYLX.H.equals(this.getBdcdylx())
				|| BDCDYLX.YCH.equals(this.getBdcdylx())) {
			String sortFields = "  DY.FH,DY.DYH,DY.QSC";
			if (!StringHelper.isEmpty(ConfigHelper.getNameByValue("SQSPBDYSORTFIELDS")))
				sortFields = "DY." + ConfigHelper.getNameByValue("SQSPBDYSORTFIELDS").replaceAll(",", ",DY.");
			builder_01.append(" order by "+sortFields);
		}else{
			builder_01.append(" order by DY.BDCDYH");

		}
		//	builder_01.append("' ORDER BY DY.QSC,DY.DYH,DY.FH");
		List<Map> list_dy_01 = baseCommonDao.getDataListByFullSql(builder_01.toString());
		if(list_dy_01!=null&&list_dy_01.size()>0){
			for(Map unit:list_dy_01){
				UnitTree tree = new UnitTree();
				if(list_ql_01!=null&&list_ql_01.size()>0){
					for(Rights right_01:list_ql_01){
						if(right_01.getDJDYID().equals(StringHelper.formatObject(unit.get("DJDYID")))){
							tree.setQlid(right_01.getId());
							tree.setlyqlid(right_01.getLYQLID());
							break;
						}
					}
				}
				tree.setId(StringHelper.formatObject(unit.get("BDCDYID")));
				tree.setBdcdyid(StringHelper.formatObject(unit.get("BDCDYID")));
				tree.setBdcdylx(StringHelper.formatObject(unit.get("BDCDYLX")));
				tree.setDjdyid(StringHelper.formatObject(unit.get("DJDYID")));
				tree.setLy(DJDYLY.GZ.Name);
				String zl = StringHelper.formatObject(unit.get("ZL"));
				tree.setCid(StringHelper.formatObject(unit.get("CID")));
				tree.setZdbdcdyid(StringHelper.formatObject(unit.get("ZDBDCDYID")));
				tree.setZrzbdcdyid(StringHelper.formatObject(unit.get("ZRZBDCDYID")));
				tree.setLjzbdcdyid(StringHelper.formatObject(unit.get("LJZID")));
				String fh = StringHelper.formatObject(unit.get("FH"));
				tree.setFh(fh);
				tree.setMj(StringHelper.getDouble(unit.get("MJ")));
				tree.setFttdmj(StringHelper.getDouble(unit.get("FTTDMJ")));
				tree.setText("坐落:" + zl + "|" + "房号:" + (fh == null ? "" : fh));
				tree.setZl(zl);
				list.add(tree);
			}
		}
		StringBuilder builder_03=new StringBuilder();
		List<BDCS_H_XZ> hxzs_03 = new ArrayList<BDCS_H_XZ>();
		List<BDCS_H_XZY> hxzs_03y = new ArrayList<BDCS_H_XZY>();
		List<BDCS_H_GZ> hgzs_03 = new ArrayList<BDCS_H_GZ>();
		List<BDCS_H_GZY> hgzs_03y = new ArrayList<BDCS_H_GZY>();
		String fh_03 = "";
		String bdcdyh_03 = "";
		int count_03 = 0;
		if(list_ql_03.size() > 0){
			bdcdyh_03 = list_ql_03.get(0).getBDCDYH();
		}
		else if(list_ql_03.size() == 0 && list_ql_01.size() > 0){
			bdcdyh_03 = list_ql_01.get(0).getBDCDYH();
		}
		if(BDCDYLX.YCH.equals(this.getBdcdylx())){
			hxzs_03y = baseCommonDao.getDataList(BDCS_H_XZY.class, "bdcdyh = '"+bdcdyh_03+"'");
			hgzs_03y = baseCommonDao.getDataList(BDCS_H_GZY.class, "bdcdyh = '"+bdcdyh_03+"'");
			
			if(hxzs_03y.size() > 0){
				fh_03 = hxzs_03y.get(0).getFH();

				if(fh_03 != null && !"".equals(fh_03)){
					for(int i=0;i<fh_03.length();i++){
						if(fh_03.charAt(i)=='-'){
							count_03++;
						}
					}
				}
			}
			else if(hgzs_03y.size() > 0){
				fh_03 = hgzs_03y.get(0).getFH();

				if(fh_03 != null && !"".equals(fh_03)){
					for(int i=0;i<fh_03.length();i++){
						if(fh_03.charAt(i)=='-'){
							count_03++;
						}
					}
				}
			}
		}else{
			hxzs_03 = baseCommonDao.getDataList(BDCS_H_XZ.class, "bdcdyh = '"+bdcdyh_03+"'");
			hgzs_03 = baseCommonDao.getDataList(BDCS_H_GZ.class, "bdcdyh = '"+bdcdyh_03+"'");
			
			if(hxzs_03.size() >0){
				fh_03 = hxzs_03.get(0).getFH();
				if(fh_03 != null && !"".equals(fh_03)){
					for(int i=0;i<fh_03.length();i++){
						if(fh_03.charAt(i)=='-'){
							count_03++;
						}
					}
				}
			}
			else if(hgzs_03.size() >0){
				fh_03 = hgzs_03.get(0).getFH();
				if(fh_03 != null && !"".equals(fh_03)){
					for(int i=0;i<fh_03.length();i++){
						if(fh_03.charAt(i)=='-'){
							count_03++;
						}
					}
				}
			}
		}
		builder_03.append("SELECT DJDY.BDCDYID,DJDY.BDCDYLX,DJDY.DJDYID,DY.ZL,DY.FH,DY."+mj_field+" AS MJ,DY.FTTDMJ,DY.CID,");
		builder_03.append("DY.ZDBDCDYID,DY.ZRZBDCDYID,DY.LJZID ");
		builder_03.append("FROM BDCK.BDCS_DJDY_GZ DJDY LEFT JOIN BDCK.");
		builder_03.append(this.getBdcdylx().LSTableName);
		builder_03.append(" DY ON DJDY.BDCDYID=DY.BDCDYID ");
		builder_03.append("WHERE LY IN ('02','03') AND DJDY.XMBH='");
		builder_03.append(this.getXMBH() + "'");
		/*if(fh_03 != null && !"".equals(fh_03) ){
			if(fh_03.indexOf("地下") != -1 || fh_03.indexOf("负") != -1){
				builder_03.append(" order by translate(DY.FH, '#' || translate(DY.FH, '0123456789', '#'), '/')");
			}else if(fh_03.indexOf("-") != -1 && count_03 == 2){
				builder_03.append(" order by substr(DY.FH,1,instr(DY.FH,'-')-1),"
						+ "translate(substr(DY.FH,instr(DY.FH,'-')-6,length(DY.FH)-instr(DY.FH,'-')), '#' || "
						+ "translate(substr(DY.FH,instr(DY.FH,'-')-6,length(DY.FH)-instr(DY.FH,'-')), '0123456789', '#'), '/'),"
						+ "translate(substr(DY.FH,instr(DY.FH,'-')-3,length(DY.FH)-instr(DY.FH,'-')), '#' || "
						+ "translate(substr(DY.FH,instr(DY.FH,'-')-3,length(DY.FH)-instr(DY.FH,'-')), '0123456789', '#'), '/')");
			}
			else if(fh_03.indexOf("-") != -1 && count_03 == 1){
				builder_03.append(" order by substr(DY.FH,1,instr(DY.FH,'-')-1),"
						+ "substr(DY.FH,instr(DY.FH,'-')+1,length(DY.FH)-instr(DY.FH,'-'))");
			}
		}else{
			builder_01.append(" order by DY.BDCDYH");
		}*/
		if (!StringHelper.isEmpty(this.getBdcdylx()) && BDCDYLX.H.equals(this.getBdcdylx())
				|| BDCDYLX.YCH.equals(this.getBdcdylx())) {
			String sortFields = "  DY.FH,DY.DYH,DY.QSC";
			if (!StringHelper.isEmpty(ConfigHelper.getNameByValue("SQSPBDYSORTFIELDS")))
				sortFields = "DY." + ConfigHelper.getNameByValue("SQSPBDYSORTFIELDS").replaceAll(",", ",DY.");
			builder_03.append(" order by "+sortFields);
		}else{
			builder_03.append(" order by DY.BDCDYH");
		}
		//builder_03.append("' ORDER BY DY.QSC,DY.DYH,DY.FH");
		List<Map> list_dy_03 = baseCommonDao.getDataListByFullSql(builder_03.toString());
		if(list_dy_03!=null&&list_dy_03.size()>0){
			for(Map unit:list_dy_03){
				UnitTree tree = new UnitTree();
				if(list_ql_01!=null&&list_ql_01.size()>0){
					for(Rights right_01:list_ql_01){
						if(right_01.getDJDYID().equals(StringHelper.formatObject(unit.get("DJDYID")))){
							tree.setQlid(right_01.getId());
							break;
						}
					}
				}
				tree.setId(StringHelper.formatObject(unit.get("BDCDYID")));
				tree.setBdcdyid(StringHelper.formatObject(unit.get("BDCDYID")));
				tree.setBdcdylx(StringHelper.formatObject(unit.get("BDCDYLX")));
				tree.setDjdyid(StringHelper.formatObject(unit.get("DJDYID")));
				tree.setLy(DJDYLY.LS.Name);
				String zl = StringHelper.formatObject(unit.get("ZL"));
				tree.setCid(StringHelper.formatObject(unit.get("CID")));
				tree.setZdbdcdyid(StringHelper.formatObject(unit.get("ZDBDCDYID")));
				tree.setZrzbdcdyid(StringHelper.formatObject(unit.get("ZRZBDCDYID")));
				tree.setLjzbdcdyid(StringHelper.formatObject(unit.get("LJZID")));
				String fh = StringHelper.formatObject(unit.get("FH"));
				tree.setFh(fh);
				tree.setMj(StringHelper.getDouble(unit.get("MJ")));
				tree.setFttdmj(StringHelper.getDouble(unit.get("FTTDMJ")));
				tree.setText("坐落:" + zl + "|" + "房号:" + (fh == null ? "" : fh));
				tree.setZl(zl);
				if(list_ql_03!=null&&list_ql_03.size()>0){
					for(Rights right_03:list_ql_03){
						if(right_03.getDJDYID().equals(StringHelper.formatObject(unit.get("DJDYID")))){
							if(DJLX.CFDJ.Value.equals(xmxx.getDJLX())){
								tree.setOldqlid(right_03.getId());
							}else{
								if (!StringHelper.isEmpty(xmxx) && "1".equals(xmxx.getSFDB())) {
									if(!StringHelper.isEmpty(right_03.getLYQLID())){
										tree.setOldqlid(right_03.getLYQLID());
									}else{
										tree.setOldqlid(right_03.getId());
									}
									
								} else {
									tree.setOldqlid(right_03.getId());
								}
							}
							break;
						}
					}
				}
				
				list.add(tree);
			}
		}
		list = ObjectHelper.SortList(list);
		return list;
	}

	/**
	 * 通过户、预测中房号跟起始层排序获取房屋信息，然后再读取单元信息及权利
	 * @作者 海豹
	 * @创建时间 2015年12月22日上午1:11:14
	 * @param bdcs_djdy_gz
	 * @return
	 */
	private List<UnitTree> getHouseRightTree() {
		List<UnitTree> list = new ArrayList<UnitTree>();
		String sql = ProjectHelper.GetXMBHCondition(xmbh);
		List<Map> lstdjdy = baseCommonDao.getDataListByFullSql("select LY,BDCDYLX from BDCK.BDCS_DJDY_GZ where " + sql + " group by LY,BDCDYLX");
		for (Map m : lstdjdy) {
			if (!StringHelper.isEmpty(m)) {
				BDCDYLX bdcdylx = BDCDYLX.initFrom(StringHelper.FormatByDatatype(m.get("BDCDYLX")));
				String strly = StringHelper.FormatByDatatype(m.get("LY"));
				DJDYLY dyly = DJDYLY.initFrom(strly);
				if (DJDYLY.XZ.equals(dyly)) {
					dyly = DJDYLY.LS;
				}
				List<RealUnit> lst = UnitTools.loadUnits(bdcdylx, dyly, "id IN ( select BDCDYID from BDCS_DJDY_GZ WHERE LY= '" + strly + "' and " + sql + " ) order by QSC,FH");
				for (RealUnit unit : lst) {
					if (!StringHelper.isEmpty(unit)) {
						List<BDCS_DJDY_GZ> djdys = baseCommonDao.getDataList(BDCS_DJDY_GZ.class, sql + " AND BDCDYID='" + unit.getId() + "'");
						if (!StringHelper.isEmpty(djdys) && djdys.size() > 0) {
							BDCS_DJDY_GZ djdy = djdys.get(0);
							List<Rights> rights = RightsTools.loadRightsByCondition(DJDYLY.GZ, " DJDYID='" + djdy.getDJDYID() + "' AND " + sql);
							for (Rights right : rights) {
								UnitTree tree = new UnitTree();
								Rights lsright = RightsTools.loadRights(DJDYLY.LS, right.getLYQLID());
								tree.setQlid(right.getId());
								String qlr = "";
								if (!StringHelper.isEmpty(lsright)) {
									qlr = getQLR(lsright.getId());
									tree.setOldqlid(lsright.getId());
								}
								tree.setId(djdy.getBDCDYID());
								tree.setBdcdyid(djdy.getBDCDYID());
								tree.setBdcdylx(djdy.getBDCDYLX());
								tree.setDjdyid(djdy.getDJDYID());
								String ly = StringUtils.isEmpty(djdy.getLY()) ? "gz" : DJDYLY.initFrom(djdy.getLY()) == null ? "gz" : DJDYLY.initFrom(djdy.getLY()).Name;
								tree.setLy(ly);
								String zl = "";
								if (unit instanceof House) {
									House house = (House) unit;
									zl = house.getZL();
									tree.setCid(house.getCID());
									tree.setZdbdcdyid(house.getZDBDCDYID());
									tree.setZrzbdcdyid(house.getZRZBDCDYID());
									tree.setLjzbdcdyid(house.getLJZID());
									tree.setMj(house.getMJ());
								}
								tree.setZl(zl);
								String qllxarray = " ('3','4','5','6','7','8')";
								String hqlCondition = MessageFormat.format(" DJDYID IN (SELECT DJDYID FROM BDCS_DJDY_XZ WHERE BDCDYID=''{0}'') AND QLLX IN {1}", unit.getId(),
										qllxarray);
								List<Rights> lstright = RightsTools.loadRightsByCondition(DJDYLY.XZ, hqlCondition);
								if (!StringHelper.isEmpty(lstright) && lstright.size() > 0) {
									tree.setOldqlid(lstright.get(0).getId());
								}
								if (!StringHelper.isEmpty(qlr)) {
									StringBuilder builder = new StringBuilder();
									builder.append(zl).append("-").append(qlr);
									tree.setText(builder.toString());
								} else {
									tree.setText(zl);
								}
								list.add(tree);
							}
						}
					}
				}
			}
		}
		return list;
	}
	
	/**
	 * 刘树峰新整的，8.9，却别在于添加权利人的时候不重新构建权力关系了，根据持证方式和现有权利人状况判断添加或者不添加证书
	 * @Title: addQLRbySQRs
	 * @author:liushufeng
	 * @date：2015年8月9日 下午10:24:20
	 * @param qlid
	 * @param sqrids
	 */
	protected void addQLRbySQRs(String qlid, Object[] sqrids) {

		boolean existholder = false;
		int count = 0;
		BDCS_QL_GZ ql = baseCommonDao.get(BDCS_QL_GZ.class, qlid);
		// 获取第一个证书
		String hqlCondition = " id IN (SELECT ZSID FROM BDCS_QDZR_GZ QDZR WHERE QDZR.QLID=''{0}'' AND XMBH=''{1}'' ) AND XMBH=''{1}''";
		hqlCondition = MessageFormat.format(hqlCondition, qlid, xmbh);
		List<BDCS_ZS_GZ> zslist = baseCommonDao.getDataList(BDCS_ZS_GZ.class, hqlCondition);
		// 获取当前权利的所有权利人
		List<BDCS_QLR_GZ> qlrlist = baseCommonDao.getDataList(BDCS_QLR_GZ.class, " XMBH='" + xmbh + "' AND QLID='" + qlid + "' ORDER BY SXH");
		// 根据权利人数量判断是否已经存在权利人
		existholder = (qlrlist == null || qlrlist.size() < 1) ? false : true;
		// 新生成的证书ID
		String newzsid = "";
		// 循环每个申请人ID
		if (sqrids != null && sqrids.length > 0) {
			for (Object sqridobj : sqrids) {

				String sqrid = StringHelper.formatObject(sqridobj);
				if (!StringHelper.isEmpty(sqrid)) {
					boolean exists = false;
					// 判断该申请人是否已经添加过权利人
					if (qlrlist != null) {
						for (BDCS_QLR_GZ qlr : qlrlist) {
							if (!StringUtils.isEmpty(qlr.getSQRID()) && qlr.getSQRID().equals(sqrid)) {
								exists = true;
								break;
							}
						}
					}
					// 如果没有添加过
					if (!exists) {
						// 先添加权利人
						BDCS_SQR sqr = baseCommonDao.get(BDCS_SQR.class, sqrid);
						BDCS_QLR_GZ qlr = ObjectHelper.CopySQRtoQLR(sqr);
						qlr.setQLID(qlid);
						if (sqr != null) {
							qlr.setSQRID(sqr.getId());
						}

						qlr.setXMBH(this.getXMBH());
						baseCommonDao.save(qlr);

						// 添加权地证人关系记录
						BDCS_QDZR_GZ qdzr = new BDCS_QDZR_GZ();
						qdzr.setBDCDYH(ql.getBDCDYH());
						qdzr.setQLRID(qlr.getId());
						qdzr.setDJDYID(ql.getDJDYID());
						qdzr.setFSQLID(ql.getFSQLID());
						qdzr.setQLID(ql.getId());
						qdzr.setXMBH(xmbh);
						// 判断是否需要添加证书，两种情况
						// 1：分别持证
						// 2:共同持证且当前没有权利人并且这是第一个sqrid
						if (ql.getCZFS() == null || ql.getCZFS().equals(CZFS.FBCZ.Value) || (ql.getCZFS().equals(CZFS.GTCZ.Value) && !existholder && count < 1)) {
							BDCS_ZS_GZ zs = new BDCS_ZS_GZ();
							zs.setId((String) SuperHelper.GeneratePrimaryKey());
							zs.setXMBH(xmbh);
							ql.setBDCQZH("");
							qdzr.setZSID(zs.getId());
							newzsid = zs.getId();
							baseCommonDao.save(zs);
						} else // 这种情况就是共同持证并且已经有证书了，只需要找到一个证书，然后把证书ID写到上面的qdzr里就行了
						{

							if (zslist.size() > 0) {
								qdzr.setZSID(zslist.get(0).getId());
							} else {
								qdzr.setZSID(newzsid);
							}
						}
						baseCommonDao.save(qdzr);
					}
				}
				count++;
			}
			
			ProjectInfo info = ProjectHelper.GetPrjInfoByXMBH(xmbh);
			// 预告登记需要加上义务人
			if ((DJLX.YGDJ.equals(this.getDjlx())||DJLX.YGDJ.Value.equals(ql.getDJLX())) && !QLLX.QTQL.equals(this.getQllx())
					&&!info.getBaseworkflowcode().equals("YG002")&&!info.getBaseworkflowcode().equals("YG011")) {

				List<BDCS_FSQL_GZ> fsqlList = baseCommonDao.getDataList(BDCS_FSQL_GZ.class, "XMBH='" + xmbh + "'");
				if (fsqlList.size() > 0) {

					StringBuilder ywr = new StringBuilder();
					StringBuilder ywrzjlx = new StringBuilder();
					StringBuilder ywrzjh = new StringBuilder();
					List<BDCS_SQR> ywrlist = baseCommonDao.getDataList(BDCS_SQR.class, " XMBH='" + xmbh + "' AND SQRLB='2'");
					for (int i = 0; i < ywrlist.size(); i++) {
						if (i != 0) {
							ywr.append("/");
							ywrzjlx.append("/");
							ywrzjh.append("/");
						}
						ywr.append(ywrlist.get(i).getSQRXM());
						ywrzjlx.append(ywrlist.get(i).getZJLX());
						ywrzjh.append(ywrlist.get(i).getZJH());
					}

					for (int i = 0; i < fsqlList.size(); i++) {
						BDCS_FSQL_GZ fsql_GZ = fsqlList.get(i);
						fsql_GZ.setYWR(ywr.toString());
						fsql_GZ.setYWRZJZL(ywrzjlx.toString());
						fsql_GZ.setYWRZJH(ywrzjh.toString());
						baseCommonDao.update(fsql_GZ);
					}
				}
			}else if(info.getBaseworkflowcode().equals("YG002")||info.getBaseworkflowcode().equals("YG011")){
				if(QLLX.DIYQ.Value.equals(ql.getQLLX())){
					List<BDCS_FSQL_GZ> fsqlList = baseCommonDao.getDataList(BDCS_FSQL_GZ.class, "XMBH='" + xmbh + "' AND QLID='"+qlid+"'");
					List<BDCS_QL_GZ> qlList = baseCommonDao.getDataList(BDCS_QL_GZ.class, "XMBH='" + xmbh + "' AND QLLX IN('1','2','3','4','5','6','7','8','10','11','12','15','24','36','99')");
					if (fsqlList.size() > 0) {
						StringBuilder ywr = new StringBuilder();
						StringBuilder ywrzjlx = new StringBuilder();
						StringBuilder ywrzjh = new StringBuilder();
						String syqqlid = "";
						if(qlList.size()>0&&!StringHelper.isEmpty(qlList.get(0).getId())){
							syqqlid = qlList.get(0).getId();
						}
						List<BDCS_QLR_GZ> ywrlist = baseCommonDao.getDataList(BDCS_QLR_GZ.class, "XMBH='" + xmbh + "' AND QLID='"+syqqlid+"'");
						for (int i = 0; i < ywrlist.size(); i++) {
							if (i != 0) {
								ywr.append(",");
								ywrzjlx.append("/");
								ywrzjh.append("/");
							}
							ywr.append(ywrlist.get(i).getQLRMC());
							ywrzjlx.append(ywrlist.get(i).getZJZL());
							ywrzjh.append(ywrlist.get(i).getZJH());
						}

						for (int i = 0; i < fsqlList.size(); i++) {
							BDCS_FSQL_GZ fsql_GZ = fsqlList.get(i);
							fsql_GZ.setYWR(ywr.toString());
							fsql_GZ.setYWRZJZL(ywrzjlx.toString());
							fsql_GZ.setYWRZJH(ywrzjh.toString());
							baseCommonDao.update(fsql_GZ);
						}
					}
				}else{
					List<BDCS_FSQL_GZ> fsqlList = baseCommonDao.getDataList(BDCS_FSQL_GZ.class, "XMBH='" + xmbh + "'");
					if (fsqlList.size() > 0) {

						StringBuilder ywr = new StringBuilder();
						StringBuilder ywrzjlx = new StringBuilder();
						StringBuilder ywrzjh = new StringBuilder();
						List<BDCS_SQR> ywrlist = baseCommonDao.getDataList(BDCS_SQR.class, " XMBH='" + xmbh + "' AND SQRLB='2'");
						for (int i = 0; i < ywrlist.size(); i++) {
							if (i != 0) {
								ywr.append("/");
								ywrzjlx.append("/");
								ywrzjh.append("/");
							}
							ywr.append(ywrlist.get(i).getSQRXM());
							ywrzjlx.append(ywrlist.get(i).getZJLX());
							ywrzjh.append(ywrlist.get(i).getZJH());
						}

						for (int i = 0; i < fsqlList.size(); i++) {
							BDCS_FSQL_GZ fsql_GZ = fsqlList.get(i);
							fsql_GZ.setYWR(ywr.toString());
							fsql_GZ.setYWRZJZL(ywrzjlx.toString());
							fsql_GZ.setYWRZJH(ywrzjh.toString());
							baseCommonDao.update(fsql_GZ);
						}
					}
				}
			}
			baseCommonDao.flush();
		}
	}

	/**
	 * 根据权利人ID和权利ID移除工作层权利人，同时会移除 权-地-证-人关系表中的数据，还差一个移除证书
	 * 
	 * @Author 刘树峰
	 * @CreateTime 2015年6月13日上午4:11:18
	 * @param qlrid
	 *            权利人ID
	 * @param qlid
	 *            权利ID
	 */
	protected void removeqlr(String qlrid, String qlid) {
		// 删除权利人
		baseCommonDao.delete(BDCS_QLR_GZ.class, qlrid);
		// 删除关系表
		String str2 = MessageFormat.format(" XMBH =''{0}'' AND QLID=''{1}'' AND QLRID=''{2}''", xmbh, qlid, qlrid);
		// 找出权地证人关系
		List<BDCS_QDZR_GZ> listqdzr = baseCommonDao.getDataList(BDCS_QDZR_GZ.class, str2);
		if (listqdzr != null && listqdzr.size() > 0) {
			BDCS_QDZR_GZ qdzr = listqdzr.get(0);
			if (qdzr != null) {
				String zsid = qdzr.getZSID();
				String sql = "ZSID='" + zsid + "' and XMBH='" + xmbh + "'";
				List<BDCS_QDZR_GZ> listqdzr2 = baseCommonDao.getDataList(BDCS_QDZR_GZ.class, sql);
				if (listqdzr2.size() == 1) {
					BDCS_ZS_GZ gzzs = baseCommonDao.get(BDCS_ZS_GZ.class, zsid);
					if (gzzs != null) {
						baseCommonDao.deleteEntity(gzzs);
						BDCS_QL_GZ ql = baseCommonDao.get(BDCS_QL_GZ.class, qlid);
						ql.setBDCQZH("");
					}
				}
			}
		}
		baseCommonDao.deleteEntitysByHql(BDCS_QDZR_GZ.class, str2);
		baseCommonDao.flush();
	}

	/**
	 * 在获取单元列表的时候，获取坐落等其他相关信息
	 * 
	 * @Author 刘树峰
	 * @CreateTime 2015年6月13日上午12:19:54
	 * @param bdcdylx
	 *            不动产单元类型
	 * @param bdcdyid
	 *            不动产单元ID
	 * @param djdyly
	 *            登记单元来源（登记来源枚举）
	 * @return zl 坐落信息
	 */
	private String getZL(UnitTree tree, String djdyid, String bdcdylx, String bdcdyid, String djdyly) {
		String zl = "";
		CommonDao dao = getCommonDao();
		BDCDYLX dylx = BDCDYLX.initFrom(bdcdylx);
		DJDYLY ly = DJDYLY.initFromByEnumName(djdyly.toUpperCase());
		RealUnit unit = UnitTools.loadUnit(dylx, ly, bdcdyid);
		if (unit != null) {
			zl = unit.getZL();
			if (unit instanceof LandAttach) {
				LandAttach attach = (LandAttach) unit;
				tree.setZdbdcdyid(attach.getZDBDCDYID());
			}
		}
		if (djdyly.equals(DJDYLY.GZ.Name)) {
			if (dylx.equals(BDCDYLX.SHYQZD)) {
				BDCS_SHYQZD_GZ shyqzd = dao.get(BDCS_SHYQZD_GZ.class, bdcdyid);
				zl = shyqzd == null ? "" : shyqzd.getZL();
			} else if (dylx.equals(BDCDYLX.H)) {
				BDCS_H_GZ h = dao.get(BDCS_H_GZ.class, bdcdyid);
				tree.setCid(h.getCID());
				tree.setZdbdcdyid(h.getZDBDCDYID());
				tree.setZrzbdcdyid(h.getZRZBDCDYID());
				tree.setLjzbdcdyid(h.getLJZID());
				zl = h == null ? "" : h.getZL();
			} else if (dylx.equals(BDCDYLX.ZRZ)) {
				BDCS_ZRZ_GZ zrz = dao.get(BDCS_ZRZ_GZ.class, bdcdyid);
				tree.setZdbdcdyid(zrz.getZDBDCDYID());
				zl = zrz == null ? "" : zrz.getZL();
			} else if (dylx.equals(BDCDYLX.SYQZD))// sunhb-2015-06-23添加所有权宗地，获取坐落
			{
				BDCS_SYQZD_GZ syqzd = dao.get(BDCS_SYQZD_GZ.class, bdcdyid);
				zl = syqzd == null ? "" : syqzd.getZL();
			} else if (dylx.equals(BDCDYLX.HY))// sunhb-2015-06-23添加宗海，获取坐落
			{
				BDCS_ZH_GZ zh = dao.get(BDCS_ZH_GZ.class, bdcdyid);
				zl = zh == null ? "" : zh.getZL();
			} else if (dylx.equals(BDCDYLX.LD))// sunhb-2015-06-23添加林地，获取坐落
			{
				BDCS_SLLM_GZ ld = dao.get(BDCS_SLLM_GZ.class, bdcdyid);
				tree.setZdbdcdyid(ld.getZDBDCDYID());
				tree.setMj(ld.getSYQMJ());
				zl = ld == null ? "" : ld.getZL();
			} else {
				// TODO 刘树峰:获取其他类型不动产单元的坐落
			}
		} else {// 来源于现状，把原来的所有权/使用权的权利ID也加上
			if (dylx.equals(BDCDYLX.SHYQZD)) {
				BDCS_SHYQZD_XZ shyqzd = dao.get(BDCS_SHYQZD_XZ.class, bdcdyid);
				zl = shyqzd == null ? "" : shyqzd.getZL();
			}
			if (dylx.equals(BDCDYLX.SYQZD)) {
				BDCS_SYQZD_XZ syqzd = dao.get(BDCS_SYQZD_XZ.class, bdcdyid);
				zl = syqzd == null ? "" : syqzd.getZL();
			} else if (dylx.equals(BDCDYLX.H)) {
				BDCS_H_XZ shyqzd = dao.get(BDCS_H_XZ.class, bdcdyid);
				if (shyqzd != null) {
					zl = shyqzd == null ? "" : shyqzd.getZL();

					tree.setCid(shyqzd.getCID());
					tree.setZdbdcdyid(shyqzd.getZDBDCDYID());
					tree.setZrzbdcdyid(shyqzd.getZRZBDCDYID());
					tree.setLjzbdcdyid(shyqzd.getLJZID());
				}

			} else if (dylx.equals(BDCDYLX.YCH)) {
				BDCS_H_XZY bdcs_h_xzy = dao.get(BDCS_H_XZY.class, bdcdyid);
				if (bdcs_h_xzy != null) {
					zl = bdcs_h_xzy.getZL();
					tree.setCid(bdcs_h_xzy.getCID());
					tree.setZdbdcdyid(bdcs_h_xzy.getZDBDCDYID());
					tree.setZrzbdcdyid(bdcs_h_xzy.getZRZBDCDYID());
					tree.setLjzbdcdyid(bdcs_h_xzy.getLJZID());
				}
			} else if (dylx.equals(BDCDYLX.ZRZ)) {
				BDCS_ZRZ_XZ zrz = dao.get(BDCS_ZRZ_XZ.class, bdcdyid);
				tree.setZdbdcdyid(zrz.getZDBDCDYID());
				zl = zrz == null ? "" : zrz.getZL();
			} else {
				// TODO 刘树峰:获取其他类型不动产单元的坐落
			}
			// 这块的逻辑有点问题，原来的权利ID应该包含两种，一种是所有权/使用权ID，一种是他项权利ID，例如
			// 抵押权的转移，就包含了被抵押单元的所有权权利和转移前的抵押权
			String qllxarray = " ('3','4','5','6','7','8')";
			String hqlCondition = MessageFormat.format(" DJDYID IN (SELECT DJDYID FROM BDCS_DJDY_XZ WHERE BDCDYID=''{0}'') AND QLLX IN {1}", bdcdyid, qllxarray);
			List<BDCS_QL_XZ> listxzql = dao.getDataList(BDCS_QL_XZ.class, hqlCondition);
			if (listxzql != null && listxzql.size() > 0) {
				BDCS_QL_XZ ql = listxzql.get(0);
				tree.setOldqlid(ql.getId());
			}
		}
		// 获取宗地对应的不动产权证号
		if (!StringHelper.isEmpty(tree) && !StringHelper.isEmpty(tree.getZdbdcdyid())) {
			String qllxarray = " ('3','4','5','6','7','8')";
			String hqlCondition = MessageFormat.format(" DJDYID IN (SELECT DJDYID FROM BDCS_DJDY_XZ WHERE BDCDYID=''{0}'') AND QLLX IN {1}", tree.getZdbdcdyid(), qllxarray);
			List<BDCS_QL_XZ> listxzql = dao.getDataList(BDCS_QL_XZ.class, hqlCondition);
			if (listxzql != null && listxzql.size() > 0) {
				BDCS_QL_XZ ql = listxzql.get(0);
				tree.setZdbdcqzh(ql.getBDCQZH());
			}
		}
		return zl;
	}

	/**
	 * 如果DJDYLY为xz,则单元从LS获取
	 * @作者 海豹
	 * @创建时间 2016年3月30日下午11:14:52
	 * @param tree
	 * @param djdyid
	 * @param bdcdylx
	 * @param bdcdyid
	 * @param djdyly
	 * @return
	 */
	private String getZLex(UnitTree tree, String djdyid, String bdcdylx, String bdcdyid, String djdyly) {
		String zl = "";

		BDCDYLX dylx = BDCDYLX.initFrom(bdcdylx);
		DJDYLY ly = DJDYLY.initFromByEnumName(djdyly.toUpperCase());
		if (DJDYLY.XZ.equals(ly)) {
			ly = DJDYLY.LS;
		}
		RealUnit unit = UnitTools.loadUnit(dylx, ly, bdcdyid);
		if (unit != null) {
			if (!StringHelper.isEmpty(unit.getZL())) {
				zl = unit.getZL();
			}
			if (!StringHelper.isEmpty(unit.getMJ())) {
				tree.setMj(unit.getMJ());
			}
			if (unit instanceof LandAttach) {
				LandAttach landattach = (LandAttach) unit;
				tree.setZdbdcdyid(landattach.getZDBDCDYID());
			}

			if (unit instanceof House) {
				House house = (House) unit;
				tree.setCid(house.getCID());
				tree.setZrzbdcdyid(house.getZRZBDCDYID());
				tree.setLjzbdcdyid(house.getLJZID());
			} else {
				// TODO 刘树峰:获取其他类型不动产单元的坐落
			}
		}
		getRelationRights(ly, bdcdyid, tree);
		return zl;
	}

	/**
	 * 获取历史权利及对应的
	 * @作者 海豹
	 * @创建时间 2016年3月31日上午11:06:22
	 * @param ly
	 * @param bdcdyid
	 * @param tree
	 */
	private void getRelationRights(DJDYLY ly, String bdcdyid, UnitTree tree) {
		CommonDao dao = getCommonDao();
		BDCS_XMXX xmxx = Global.getXMXX(this.getProject_id());
		if (DJDYLY.LS.equals(ly)) {
			String qllxarray = " ('1','2','3','4','5','6','7','8','10','11','12','15','24','36')";
			String hqlCondition = MessageFormat.format(" DJDYID IN (SELECT DJDYID FROM BDCS_DJDY_XZ WHERE BDCDYID=''{0}'') AND QLLX IN {1}", bdcdyid, qllxarray);
			if(DJLX.ZYDJ.Value.equals(xmxx.getDJLX())&&xmxx.getSFDB().equals("1")){
				hqlCondition = MessageFormat.format(" DJDYID IN (SELECT DJDYID FROM BDCS_DJDY_XZ WHERE BDCDYID=''{0}'') AND QLLX IN {1} AND QLID=''{2}''", bdcdyid, qllxarray,StringHelper.formatObject(tree.getQlid()));
			}
			List<BDCS_QL_XZ> listxzql = dao.getDataList(BDCS_QL_XZ.class, hqlCondition);
			List<BDCS_QL_LS> listlsql = dao.getDataList(BDCS_QL_LS.class, hqlCondition);
			if (listxzql != null && listxzql.size() > 0) {
				BDCS_QL_XZ ql = listxzql.get(0);
				if(DJLX.CFDJ.Value.equals(xmxx.getDJLX())){
					tree.setOldqlid(ql.getId());
				}else{
					if (!StringHelper.isEmpty(xmxx) && "1".equals(xmxx.getSFDB())) {
						if(!StringHelper.isEmpty(ql.getLYQLID())){
							tree.setOldqlid(ql.getLYQLID());
						}else{
							tree.setOldqlid(ql.getId());
						}
						
					} else {
						tree.setOldqlid(ql.getId());
					}
				}
			}else if (listlsql != null && listlsql.size() > 0){
				BDCS_QL_LS ql = listlsql.get(0);
				if(!StringHelper.isEmpty(ql.getLYQLID())){
					tree.setOldqlid(ql.getLYQLID());
				}else{
					tree.setOldqlid(ql.getId());
				}
			}
			
		}
		// 获取宗地对应的不动产权证号
		if (!StringHelper.isEmpty(tree) && !StringHelper.isEmpty(tree.getZdbdcdyid())) {
			String qllxarray = " ('1','2','3','4','5','6','7','8','24')";
			String hqlCondition = MessageFormat.format(" DJDYID IN (SELECT DJDYID FROM BDCS_DJDY_XZ WHERE BDCDYID=''{0}'') AND QLLX IN {1}", tree.getZdbdcdyid(), qllxarray);
			List<BDCS_QL_XZ> listxzql = dao.getDataList(BDCS_QL_XZ.class, hqlCondition);
			if (listxzql != null && listxzql.size() > 0) {
				BDCS_QL_XZ ql = listxzql.get(0);
				tree.setZdbdcqzh(ql.getBDCQZH());
			}
		}
	}

	/**
	 * 根据权利ID获取权利人表中第一个记录的权利人名称
	 * 
	 * @Author 俞学斌
	 * @CreateTime 2015年6月13日上午12:19:54
	 * @param qlid
	 *            权利ID
	 * @return String 第一条权利人记录的权利人名称
	 */
	private String getQLR(String qlid) {
		// TODO 俞学斌：这个是干啥用的？只取了权利人中的第一条记录的权利人名称
		String qlr = "";
		String xmbhFilter = ProjectHelper.GetXMBHCondition(getXMBH());
		StringBuilder builderQueryQLR = new StringBuilder();
		builderQueryQLR.append(" QLID='").append(qlid).append("' and ");
		builderQueryQLR.append(xmbhFilter);
		String strQueryQLR = builderQueryQLR.toString();
		CommonDao dao = getCommonDao();
		List<BDCS_QLR_GZ> qlrs = dao.getDataList(BDCS_QLR_GZ.class, strQueryQLR);
		if (qlrs != null && qlrs.size() > 0) {
			qlr = qlrs.get(0).getQLRMC();
		}
		return qlr;
	}

	/**
	 * 根据登记单元ID获取工作层中对应的权利信息，加上了项目编号过滤条件,只获取第一条（正常应该只有一条）
	 * 
	 * @Author 刘树峰
	 * @CreateTime 2015年6月13日上午3:16:32
	 * @param djdyid
	 *            登记单元ID
	 * @return
	 */
	protected BDCS_QL_GZ getQL(String djdyid) {
		BDCS_QL_GZ ql = null;
		List<BDCS_QL_GZ> qllist = getCommonDao().getDataList(BDCS_QL_GZ.class, " DJDYID='" + djdyid + "' AND " + ProjectHelper.GetXMBHCondition(xmbh));
		if (qllist.size() > 0) {
			ql = qllist.get(0);
		}
		return ql;
	}
	
	/**
	 * 根据登记单元ID获取工作层中对应的权利信息，加上了项目编号过滤条件,只获取第一条（正常应该只有一条）
	 * 
	 * @Author 刘树峰
	 * @CreateTime 2015年6月13日上午3:16:32
	 * @param djdyid
	 *            登记单元ID
	 * @return
	 */
	protected BDCS_QL_LS getLSQL(String djdyid) {
		BDCS_QL_LS ql = null;
		List<BDCS_QL_LS> qllist = getCommonDao().getDataList(BDCS_QL_LS.class, " DJDYID='" + djdyid + "' AND " + ProjectHelper.GetXMBHCondition(xmbh));
		if (qllist.size() > 0) {
			ql = qllist.get(0);
		}
		return ql;
	}

	/**
	 * 根据登记单元生成权利-工作层
	 * 
	 * @Author 刘树峰
	 * @CreateTime 2015年6月14日下午10:07:35
	 * @param djdy
	 *            BDCS_DJDY_GZ djdy
	 * @return 生成的工作层权利记录
	 */
	protected BDCS_QL_GZ createQL(BDCS_DJDY_GZ djdy, RealUnit unit) {
		BDCS_QL_GZ ql = new BDCS_QL_GZ();
		ql.setBDCDYH(djdy.getBDCDYH());
		ql.setCZFS(CZFS.FBCZ.Value);
		ql.setZSBS(ZSBS.DYB.Value);
		ql.setDJDYID(djdy.getDJDYID());
		ql.setDJLX(this.getDjlx().Value);
		ql.setQLLX(this.getQllx().Value);
		ql.setXMBH(this.getXMBH());
		ql.setYWH(this.getProject_id());

		// 给权利添加区县代码值 TODO @刘树峰，这块写的不合理，为了防止演示过程中出错，先加上trycatch
		try {
			if (unit != null) {
				if (unit instanceof LandAttach) {
					LandAttach attach = (LandAttach) unit;
					if(!StringHelper.isEmpty(attach.getZDBDCDYID())){
						unit = UnitTools.loadUnit(BDCDYLX.SHYQZD, DJDYLY.XZ, attach.getZDBDCDYID());
					}
				}
			}
			if (unit != null) {
				ql.setQXDM(unit.getQXDM());
			}
		} catch (Exception ee) {
		}
		return ql;
	}

	/**
	 * 根据给定的不动产单元ID，生成工作层登记单元，相应的登记单元ID用现状层中的ID
	 * 
	 * @Author 刘树峰
	 * @CreateTime 2015年6月14日下午10:16:26
	 * @param bdcdyid
	 *            不动产单元ID
	 * @return 新生成的登记单元记录
	 */
	protected BDCS_DJDY_GZ createDJDYfromXZ(String bdcdyid) {
		BDCS_DJDY_GZ gzdjdy = null;
		String hql = MessageFormat.format(" BDCDYID=''{0}'' ", bdcdyid);
		List<BDCS_DJDY_XZ> list = getCommonDao().getDataList(BDCS_DJDY_XZ.class, hql);
		//如果传的是qlid(选择器2返回qlid)
//		if(0==list.size()){
//			List<Map> map =getCommonDao().getDataListByFullSql("select bdcdyid from bdck.bdcs_ql_xz where qlid='"+bdcdyid+"'");
//			list = getCommonDao().getDataList(BDCS_DJDY_XZ.class, " BDCDYID='"+map.get(0).get("BDCDYID")+"' ");
//		}
		if (list != null && list.size() > 0) {
			BDCS_DJDY_XZ xzdjdy = list.get(0);
			gzdjdy = new BDCS_DJDY_GZ();
			gzdjdy.setXMBH(this.getXMBH());
			gzdjdy.setDJDYID(xzdjdy.getDJDYID());
			gzdjdy.setBDCDYID(bdcdyid);
			gzdjdy.setBDCDYLX(this.getBdcdylx().Value);
			gzdjdy.setBDCDYH(xzdjdy.getBDCDYH());
			gzdjdy.setLY(DJDYLY.XZ.Value);
		}else{
			List<BDCS_DJDY_LS> list_ls = getCommonDao().getDataList(BDCS_DJDY_LS.class, hql);
			if (list_ls != null && list_ls.size() > 0) {
				BDCS_DJDY_LS xzdjdy = list_ls.get(0);
				gzdjdy = new BDCS_DJDY_GZ();
				gzdjdy.setXMBH(this.getXMBH());
				gzdjdy.setDJDYID(xzdjdy.getDJDYID());
				gzdjdy.setBDCDYID(bdcdyid);
				gzdjdy.setBDCDYLX(this.getBdcdylx().Value);
				gzdjdy.setBDCDYH(xzdjdy.getBDCDYH());
				gzdjdy.setLY(DJDYLY.XZ.Value);
			}
		}
		if(BDCDYLX.YCH.equals(this.getBdcdylx())&&gzdjdy==null){
			List<BDCS_DJDY_GZ> list_gz = getCommonDao().getDataList(BDCS_DJDY_GZ.class, hql);
			if (list_gz != null && list_gz.size() > 0) {
				BDCS_DJDY_GZ gzdjdyfrom = list_gz.get(0);
				gzdjdy = new BDCS_DJDY_GZ();
				gzdjdy.setXMBH(this.getXMBH());
				gzdjdy.setDJDYID(gzdjdyfrom.getDJDYID());
				gzdjdy.setBDCDYID(bdcdyid);
				gzdjdy.setBDCDYLX(this.getBdcdylx().Value);
				gzdjdy.setBDCDYH(gzdjdyfrom.getBDCDYH());
				gzdjdy.setLY(DJDYLY.XZ.Value);
			}
		}
		return gzdjdy;
	}

	/**
	 * 根据不动产单元生成登记单元
	 * 
	 * @Author 刘树峰
	 * @CreateTime 2015年6月17日下午11:17:23
	 * @param dy
	 *            不动产单元（所有不动产单元的通用接口）
	 * @return BDCS_DJDY_GZ 工作层登记单元记录
	 */
	protected BDCS_DJDY_GZ createDJDYfromGZ(RealUnit dy) {
		BDCS_DJDY_GZ djdy = new BDCS_DJDY_GZ();
		djdy.setXMBH(this.getXMBH());
		djdy.setDJDYID(getPrimaryKey());
		djdy.setBDCDYH(dy.getBDCDYH());
		djdy.setBDCDYID(dy.getId());
		djdy.setLY(DJDYLY.GZ.Value);
		djdy.setBDCDYLX(this.getBdcdylx().Value);
		return djdy;
	}

	/**
	 * 根据登记单元ID生成工作层附属权利
	 * 
	 * @Author 刘树峰
	 * @CreateTime 2015年6月14日下午10:29:59
	 * @param djdyid
	 *            登记单元ID
	 * @return 工作层附属权利
	 */
	protected BDCS_FSQL_GZ createFSQL(String djdyid) {
		BDCS_FSQL_GZ fsql = new BDCS_FSQL_GZ();
		fsql.setDJDYID(djdyid);
		fsql.setXMBH(xmbh);
		if(bdcdylx.Value.equals(BDCDYLX.LD.Value)){
			String hql = "QLID IN(SELECT id FROM BDCS_QL_XZ WHERE  DJLX='100" + "' AND QLLX in('11','12','36')" +" AND DJDYID = '"+djdyid+ "')";
			List<BDCS_FSQL_XZ> list = baseCommonDao.getDataList(BDCS_FSQL_XZ.class, hql);
			if(list.size() > 0 && list != null){
				BDCS_FSQL_XZ _fsql = (BDCS_FSQL_XZ)list.get(0);
				fsql.setSLLMSYQR1(_fsql.getSLLMSYQR1());
				fsql.setSLLMSYQR2(_fsql.getSLLMSYQR2());
				fsql.setLDSYQXZ(_fsql.getLDSYQXZ());
			}
		}
		return fsql;
	}

	protected boolean CopyGZSHYQZDToXZAndLSEx(String gz_bdcdyid, String xz_bdcdyid) {
		BDCS_SHYQZD_GZ bdcs_shyqzd_gz = baseCommonDao.get(BDCS_SHYQZD_GZ.class, gz_bdcdyid);
		if (bdcs_shyqzd_gz != null) {
			BDCS_SHYQZD_XZ bdcs_shyqzd_xz = ObjectHelper.copySHYQZD_GZToXZ(bdcs_shyqzd_gz);
			bdcs_shyqzd_xz.setId(xz_bdcdyid);
			baseCommonDao.update(bdcs_shyqzd_xz);

			BDCS_SHYQZD_LS bdcs_shyqzd_ls_ly = baseCommonDao.get(BDCS_SHYQZD_LS.class, xz_bdcdyid);
			BDCS_SHYQZD_LS bdcs_shyqzd_ls_new = new BDCS_SHYQZD_LS();
			try {
				PropertyUtils.copyProperties(bdcs_shyqzd_ls_new, bdcs_shyqzd_ls_ly);
				bdcs_shyqzd_ls_new.setId(gz_bdcdyid);
				bdcs_shyqzd_ls_new.setYXBZ("1");
				baseCommonDao.save(bdcs_shyqzd_ls_new);
			} catch (Exception e) {

			}
			try {
				PropertyUtils.copyProperties(bdcs_shyqzd_ls_ly, bdcs_shyqzd_xz);
				bdcs_shyqzd_ls_ly.setId(xz_bdcdyid);
				baseCommonDao.update(bdcs_shyqzd_ls_ly);
			} catch (Exception e) {

			}
		}
		return true;
	}

	protected boolean CopyGZFWToXZAndLSEx(String gz_bdcdyid, String xz_bdcdyid) {
		BDCS_H_GZ bdcs_h_gz = baseCommonDao.get(BDCS_H_GZ.class, gz_bdcdyid);
		if (bdcs_h_gz != null) {
			BDCS_H_XZ bdcs_h_xz = ObjectHelper.copyH_GZToXZ(bdcs_h_gz);
			bdcs_h_xz.setId(xz_bdcdyid);
			baseCommonDao.update(bdcs_h_xz);

			BDCS_H_LS bdcs_h_ls_ly = baseCommonDao.get(BDCS_H_LS.class, xz_bdcdyid);
			BDCS_H_LS bdcs_h_ls_new = new BDCS_H_LS();
			try {
				PropertyUtils.copyProperties(bdcs_h_ls_new, bdcs_h_ls_ly);
				bdcs_h_ls_new.setId(gz_bdcdyid);
				bdcs_h_ls_new.setYXBZ("1");
				baseCommonDao.save(bdcs_h_ls_new);
			} catch (Exception e) {

			}
			try {
				PropertyUtils.copyProperties(bdcs_h_ls_ly, bdcs_h_xz);
				bdcs_h_ls_ly.setId(xz_bdcdyid);
				baseCommonDao.update(bdcs_h_ls_ly);
			} catch (Exception e) {

			}
		}
		return true;
	}
	
	/**
	 * 更正后的林地单元拷贝到现状和历史
	 * @cratetime 2017-10-27 16:40:45
	 * @author liangc
	 * @param gz_bdcdyid
	 * @param xz_bdcdyid
	 * @return
	 */
	protected boolean CopyGZLDSHYQZDToXZAndLSEx(String gz_bdcdyid, String xz_bdcdyid) {
		BDCS_SLLM_GZ bdcs_sllm_gz = baseCommonDao.get(BDCS_SLLM_GZ.class, gz_bdcdyid);
		if (bdcs_sllm_gz != null) {
			BDCS_SLLM_XZ bdcs_sllm_xz = ObjectHelper.copySLLM_GZToXZ(bdcs_sllm_gz);
			bdcs_sllm_xz.setId(xz_bdcdyid);
			baseCommonDao.update(bdcs_sllm_xz);

			BDCS_SLLM_LS bdcs_sllm_ls_ly = baseCommonDao.get(BDCS_SLLM_LS.class, xz_bdcdyid);
			BDCS_SLLM_LS bdcs_sllm_ls_new = new BDCS_SLLM_LS();
			try {
				PropertyUtils.copyProperties(bdcs_sllm_ls_new, bdcs_sllm_ls_ly);
				bdcs_sllm_ls_new.setId(gz_bdcdyid);
				bdcs_sllm_ls_new.setYXBZ("1");
				baseCommonDao.save(bdcs_sllm_ls_new);
			} catch (Exception e) {

			}
			try {
				PropertyUtils.copyProperties(bdcs_sllm_ls_ly, bdcs_sllm_xz);
				bdcs_sllm_ls_ly.setId(xz_bdcdyid);
				baseCommonDao.update(bdcs_sllm_ls_ly);
			} catch (Exception e) {

			}
		}
		return true;
	}
	
	/**
	 * 更正后的农用地单元拷贝到现状和历史
	 * @cratetime 2017-10-27 16:48:50
	 * @author liangc
	 * @param gz_bdcdyid
	 * @param xz_bdcdyid
	 * @return
	 */
	protected boolean CopyGZGYNYDSHYQZDToXZAndLSEx(String gz_bdcdyid, String xz_bdcdyid) {
		BDCS_NYD_GZ bdcs_nyd_gz = baseCommonDao.get(BDCS_NYD_GZ.class, gz_bdcdyid);
		if (bdcs_nyd_gz != null) {
			BDCS_NYD_XZ bdcs_nyd_xz = ObjectHelper.copyNYD_GZToXZ(bdcs_nyd_gz);
			bdcs_nyd_xz.setId(xz_bdcdyid);
			baseCommonDao.update(bdcs_nyd_xz);

			BDCS_NYD_LS bdcs_nyd_ls_ly = baseCommonDao.get(BDCS_NYD_LS.class, xz_bdcdyid);
			BDCS_NYD_LS bdcs_nyd_ls_new = new BDCS_NYD_LS();
			try {
				PropertyUtils.copyProperties(bdcs_nyd_ls_new, bdcs_nyd_ls_ly);
				bdcs_nyd_ls_new.setId(gz_bdcdyid);
				baseCommonDao.save(bdcs_nyd_ls_new);
			} catch (Exception e) {

			}
			try {
				PropertyUtils.copyProperties(bdcs_nyd_ls_ly, bdcs_nyd_xz);
				bdcs_nyd_ls_ly.setId(xz_bdcdyid);
				baseCommonDao.update(bdcs_nyd_ls_ly);
			} catch (Exception e) {

			}
		}
		return true;
	}

	/**
	 * 根据登记单元ID把权利从工作层拷贝到现状层和历史层
	 * 
	 * @Author 俞学斌
	 * @CreateTime 2015年6月16日下午15:45:34
	 * @param djdyid
	 *            登记单元ID
	 * @return 成功返回true，失败返回false
	 */
	protected boolean CopyGZQLToXZAndLS(String djdyid) {

		String dbr = Global.getCurrentUserName();
		StringBuilder builderDJDYID = new StringBuilder();
		builderDJDYID.append(" DJDYID='").append(djdyid).append("'");
		String strSqlDJDYID = builderDJDYID.toString();
		String xmbhFilter = ProjectHelper.GetXMBHCondition(xmbh);
		List<BDCS_QL_GZ> qls = baseCommonDao.getDataList(BDCS_QL_GZ.class, xmbhFilter + " and " + strSqlDJDYID);
		if (qls != null && qls.size() > 0) {
			for (int iql = 0; iql < qls.size(); iql++) {
				BDCS_QL_GZ bdcs_ql_gz = qls.get(iql);
				// 登记时间，登簿人，登记机构
				bdcs_ql_gz.setDBR(dbr);
				if(bdcs_ql_gz.getDJSJ()==null || bdcs_ql_gz.getDJSJ().equals("") ){
					bdcs_ql_gz.setDJSJ(new Date());
				}
				bdcs_ql_gz.setDJJG(ConfigHelper.getNameByValue("DJJGMC"));
				baseCommonDao.getCurrentSession().update(bdcs_ql_gz);
				// 拷贝权利
				BDCS_QL_XZ bdcs_ql_xz = ObjectHelper.copyQL_GZToXZ(bdcs_ql_gz);
				baseCommonDao.save(bdcs_ql_xz);
				BDCS_QL_LS bdcs_ql_ls = ObjectHelper.copyQL_XZToLS(bdcs_ql_xz);
				baseCommonDao.save(bdcs_ql_ls);
				// 根据权利表中附属权利ID获取附属权利
				if (bdcs_ql_gz.getFSQLID() != null) {
					BDCS_FSQL_GZ bdcs_fsql_gz = baseCommonDao.get(BDCS_FSQL_GZ.class, bdcs_ql_gz.getFSQLID());
					if (bdcs_fsql_gz != null) {
						// 拷贝附属权利
						BDCS_FSQL_XZ bdcs_fsql_xz = ObjectHelper.copyFSQL_GZToXZ(bdcs_fsql_gz);
						baseCommonDao.save(bdcs_fsql_xz);
						BDCS_FSQL_LS bdcs_fsql_ls = ObjectHelper.copyFSQL_XZToLS(bdcs_fsql_xz);
						baseCommonDao.save(bdcs_fsql_ls);
					}
				}
			}
		}
		return true;
	}

	/**
	 * 根据登记单元ID把权利人从工作层拷贝到现状层和历史层
	 * 
	 * @Author 俞学斌
	 * @CreateTime 2015年6月16日下午15:45:34
	 * @param djdyid
	 *            登记单元ID
	 * @return 成功返回true，失败返回false
	 */
	protected boolean CopyGZQLRToXZAndLS(String djdyid) {
		String xmbhFilter = ProjectHelper.GetXMBHCondition(xmbh);
		StringBuilder builder = new StringBuilder();
		builder.append(" QLID IN (");
		builder.append("select id FROM BDCS_QL_GZ WHERE DJDYID ='");
		builder.append(djdyid).append("' and ");
		builder.append(xmbhFilter);
		builder.append(" and QLLX='").append(getQllx().Value).append("'");
		builder.append(")");
		String strQueryQLRID = builder.toString();
		List<BDCS_QLR_GZ> qlrs = baseCommonDao.getDataList(BDCS_QLR_GZ.class, strQueryQLRID);
		if (qlrs != null && qlrs.size() > 0) {
			for (int iqlr = 0; iqlr < qlrs.size(); iqlr++) {
				BDCS_QLR_GZ bdcs_qlr_gz = qlrs.get(iqlr);
				BDCS_QLR_XZ bdcs_qlr_xz = ObjectHelper.copyQLR_GZToXZ(bdcs_qlr_gz);
				baseCommonDao.save(bdcs_qlr_xz);
				BDCS_QLR_LS bdcs_qlr_ls = ObjectHelper.copyQLR_XZToLS(bdcs_qlr_xz);
				baseCommonDao.save(bdcs_qlr_ls);
			}
		}
		return true;
	}

	/**
	 * 根据登记单元ID把权利人从工作层拷贝到现状层和历史层
	 * 
	 * @Author 俞学斌
	 * @CreateTime 2015年6月16日下午15:45:34
	 * @param djdyid
	 *            登记单元ID
	 * @return 成功返回true，失败返回false
	 */
	protected boolean CopyGZQLRToXZAndLSNotOnQLLX(String djdyid) {
		String xmbhFilter = ProjectHelper.GetXMBHCondition(xmbh);
		StringBuilder builder = new StringBuilder();
		builder.append(" QLID IN (");
		builder.append("select id FROM BDCS_QL_GZ WHERE DJDYID ='");
		builder.append(djdyid).append("' and ");
		builder.append(xmbhFilter);
		// builder.append(" and QLLX='").append(getQllx().Value).append("'");
		builder.append(")");
		String strQueryQLRID = builder.toString();
		List<BDCS_QLR_GZ> qlrs = baseCommonDao.getDataList(BDCS_QLR_GZ.class, strQueryQLRID);
		if (qlrs != null && qlrs.size() > 0) {
			for (int iqlr = 0; iqlr < qlrs.size(); iqlr++) {
				BDCS_QLR_GZ bdcs_qlr_gz = qlrs.get(iqlr);
				BDCS_QLR_XZ bdcs_qlr_xz = ObjectHelper.copyQLR_GZToXZ(bdcs_qlr_gz);
				baseCommonDao.save(bdcs_qlr_xz);
				BDCS_QLR_LS bdcs_qlr_ls = ObjectHelper.copyQLR_XZToLS(bdcs_qlr_xz);
				baseCommonDao.save(bdcs_qlr_ls);
			}
		}
		return true;
	}

	/**
	 * 根据登记单元ID把证书从工作层拷贝到现状层和历史层
	 * 
	 * @Author 俞学斌
	 * @CreateTime 2015年6月16日下午15:45:34
	 * @param djdyid
	 *            登记单元ID
	 * @return 成功返回true，失败返回false
	 */
	protected boolean CopyGZZSToXZAndLS(String djdyid) {
		String xmbhFilter = ProjectHelper.GetXMBHCondition(xmbh);
		StringBuilder builder = new StringBuilder();
		builder.append(" id IN (");
		builder.append("select ZSID FROM BDCS_QDZR_GZ WHERE DJDYID ='");
		builder.append(djdyid).append("' and ");
		builder.append(xmbhFilter).append(")");
		String strQueryZS = builder.toString();
		List<BDCS_ZS_GZ> zss = baseCommonDao.getDataList(BDCS_ZS_GZ.class, strQueryZS);
		if (zss != null && zss.size() > 0) {
			for (int izs = 0; izs < zss.size(); izs++) {
				BDCS_ZS_GZ bdcs_zs_gz = zss.get(izs);
				BDCS_ZS_XZ bdcs_zs_xz = baseCommonDao.get(BDCS_ZS_XZ.class, bdcs_zs_gz.getId());
				if (bdcs_zs_xz == null) {
					bdcs_zs_xz = ObjectHelper.copyZS_GZToXZ(bdcs_zs_gz);
					baseCommonDao.save(bdcs_zs_xz);
				}
				BDCS_ZS_LS bdcs_zs_ls = baseCommonDao.get(BDCS_ZS_LS.class, bdcs_zs_xz.getId());
				if (bdcs_zs_ls == null) {
					bdcs_zs_ls = ObjectHelper.copyZS_XZToLS(bdcs_zs_xz);
					baseCommonDao.save(bdcs_zs_ls);
				}
			}
		}
		return true;
	}

	/**
	 * 根据登记单元ID把权地证人关系表从工作层拷贝到现状层和历史层
	 * 
	 * @Author 俞学斌
	 * @CreateTime 2015年6月16日下午15:45:34
	 * @param djdyid
	 *            登记单元ID
	 * @return 成功返回true，失败返回false
	 */
	protected boolean CopyGZQDZRToXZAndLS(String djdyid) {
		String xmbhFilter = ProjectHelper.GetXMBHCondition(xmbh);
		StringBuilder builderDJDYID = new StringBuilder();
		builderDJDYID.append(" DJDYID='").append(djdyid).append("'");
		String strSqlDJDYID = builderDJDYID.toString();
		List<BDCS_QDZR_GZ> qdzrs = baseCommonDao.getDataList(BDCS_QDZR_GZ.class, xmbhFilter + " and " + strSqlDJDYID);
		if (qdzrs != null && qdzrs.size() > 0) {
			for (int iqdzr = 0; iqdzr < qdzrs.size(); iqdzr++) {
				BDCS_QDZR_GZ bdcs_qdzr_gz = qdzrs.get(iqdzr);
				// 拷贝权地证人关系
				BDCS_QDZR_XZ bdcs_qdzr_xz = ObjectHelper.copyQDZR_GZToXZ(bdcs_qdzr_gz);
				baseCommonDao.save(bdcs_qdzr_xz);
				BDCS_QDZR_LS bdcs_qdzr_ls = ObjectHelper.copyQDZR_XZToLS(bdcs_qdzr_xz);
				baseCommonDao.save(bdcs_qdzr_ls);
			}
		}
		return true;
	}

	/**
	 * 更加不动产单元ID把预测户从现状库拷贝到历史户
	 * 
	 * @作者 海豹
	 * @创建时间 2015年7月27日下午3:46:08
	 * @param bdcdyid
	 * @return
	 */
	protected boolean CopyYXZHToAndLS(String bdcdyid) {
		Boolean flag = true;
		BDCS_H_XZY bdcs_h_xzy = baseCommonDao.get(BDCS_H_XZY.class, bdcdyid);
		if (bdcs_h_xzy != null) {
			BDCS_H_LSY bdcs_h_lsy = baseCommonDao.get(BDCS_H_LSY.class, bdcdyid);
			if (bdcs_h_lsy == null) {
				BDCS_H_LSY lsyH = new BDCS_H_LSY();
				flag = UnitTools.copyUnit(bdcs_h_xzy, lsyH);
			}
		}
		return flag;
	}

	/**
	 * 根据不动产单元ID把不动产单元从工作层拷贝到现状层和历史层
	 * 
	 * @Author 俞学斌
	 * @CreateTime 2015年6月16日下午15:45:34
	 * @param bdcdyid
	 *            不动产单元ID
	 * @return 成功返回true，失败返回false
	 */
	protected boolean CopyGZDYToXZAndLS(String bdcdyid) {
		BDCDYLX dylx = this.getBdcdylx();
		RealUnit xzunit = UnitTools.copyUnit(dylx, DJDYLY.GZ, DJDYLY.XZ, bdcdyid);
		RealUnit lsunit = UnitTools.copyUnit(dylx, DJDYLY.GZ, DJDYLY.LS, bdcdyid);
		baseCommonDao.save(xzunit);
		baseCommonDao.save(lsunit);
		return true;
	}

	protected boolean CopyGZDYToXZAndLSEx(String gz_bdcdyid, String xz_bdcdyid) {
		if (ConstValue.QLLX.GYJSYDSHYQ.equals(getQllx()) || ConstValue.QLLX.ZJDSYQ.equals(getQllx())
				|| ConstValue.QLLX.JTJSYDSYQ.equals(getQllx())) {
			CopyGZSHYQZDToXZAndLSEx(gz_bdcdyid, xz_bdcdyid);
		} else if (ConstValue.QLLX.GYJSYDSHYQ_FWSYQ.equals(getQllx())||ConstValue.QLLX.JTJSYDSYQ_FWSYQ.equals(getQllx())
				||ConstValue.QLLX.ZJDSYQ_FWSYQ.equals(getQllx())) {
			CopyGZFWToXZAndLSEx(gz_bdcdyid, xz_bdcdyid);
		}else if (ConstValue.QLLX.LDSHYQ_SLLMSYQ.equals(getQllx()) 
				|| ConstValue.QLLX.LDSYQ.equals(getQllx()) || ConstValue.QLLX.LDSYQ_SLLMSYQ.equals(getQllx())) {
			CopyGZLDSHYQZDToXZAndLSEx(gz_bdcdyid, xz_bdcdyid);
		}else if (ConstValue.QLLX.GYNYDSHYQ.equals(getQllx())) {
			CopyGZGYNYDSHYQZDToXZAndLSEx(gz_bdcdyid, xz_bdcdyid);
		} else {
			return false;
		}
		return true;
	}

	/**
	 * 把登记单元从工作层拷贝到现状层和历史层
	 * 
	 * @Author 俞学斌
	 * @CreateTime 2015年6月16日下午15:45:34
	 * @param djdyid
	 *            登记单元ID
	 * @return 成功返回true，失败返回false
	 */
	protected boolean CopyGZDJDYToXZAndLS(String Id) {
		BDCS_DJDY_GZ bdcs_djdy_gz = baseCommonDao.get(BDCS_DJDY_GZ.class, Id);
		if (bdcs_djdy_gz != null) {
			BDCS_DJDY_XZ bdcs_djdy_xz = null;
			List<BDCS_DJDY_XZ> djdys_xz = baseCommonDao.getDataList(BDCS_DJDY_XZ.class, "DJDYID='" + bdcs_djdy_gz.getDJDYID() + "'");
			if (djdys_xz == null || djdys_xz.size() == 0) {
				bdcs_djdy_xz = ObjectHelper.copyDJDY_GZToXZ(bdcs_djdy_gz);
				baseCommonDao.save(bdcs_djdy_xz);
			} else {
				bdcs_djdy_xz = djdys_xz.get(0);
			}
			List<BDCS_DJDY_LS> djdys_ls = baseCommonDao.getDataList(BDCS_DJDY_LS.class, "DJDYID='" + bdcs_djdy_gz.getDJDYID() + "'");
			if (djdys_ls == null || djdys_ls.size() == 0) {
				BDCS_DJDY_LS bdcs_djdy_ls = ObjectHelper.copyDJDY_XZToLS(bdcs_djdy_xz);
				baseCommonDao.save(bdcs_djdy_ls);
			}

		}
		return true;
	}
	
	
	protected boolean copyTDYT_XZ_TO_GZ(String bdcdyid,String bdcdyid_new) {
		String hqlCondition = "BDCDYID='" + bdcdyid + "'";
		// 先删除
		//删除会导致一些业务流程证书页面用途不显示，如:BG001
//		getCommonDao().deleteEntitysByHql(BDCS_TDYT_GZ.class,
//				hqlCondition);
		// 再拷贝
		List<BDCS_TDYT_XZ> desListTDYT = getCommonDao().getDataList(
				BDCS_TDYT_XZ.class, hqlCondition);
		if (desListTDYT != null && desListTDYT.size() > 0) {
			for (BDCS_TDYT_XZ tdyt : desListTDYT) {
				BDCS_TDYT_GZ yt_gz = new BDCS_TDYT_GZ();
				try {
					PropertyUtils.copyProperties(yt_gz, tdyt);
					yt_gz.setId((String) SuperHelper
							.GeneratePrimaryKey());
					yt_gz.setBDCDYID(bdcdyid_new);
					yt_gz.setXMBH(getXMBH());
					getCommonDao().save(yt_gz);
				} catch (Exception e) {
					e.printStackTrace();
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * 根据登记单元ID移除现状层使用权宗地
	 * 
	 * @Author：俞学斌
	 * @param djdyid
	 *            登记单元ID
	 * @return 成功返回true，失败返回false
	 */
	protected boolean removeSHYQZDFromXZ(String djdyid) {
		StringBuilder builderDJDYID = new StringBuilder();
		builderDJDYID.append(" DJDYID='").append(djdyid).append("'");
		String strQueryDJDYID = builderDJDYID.toString();
		List<BDCS_DJDY_XZ> djdys = baseCommonDao.getDataList(BDCS_DJDY_XZ.class, strQueryDJDYID);
		if (djdys != null && djdys.size() > 0) {
			for (int idjdy = 0; idjdy < djdys.size(); idjdy++) {
				String bdcdyid = djdys.get(idjdy).getBDCDYID();
				BDCS_SHYQZD_XZ bdcs_shyqzd_xz = baseCommonDao.get(BDCS_SHYQZD_XZ.class, bdcdyid);
				if (bdcs_shyqzd_xz != null) {
					baseCommonDao.deleteEntity(bdcs_shyqzd_xz);
				}
			}
		}
		return true;
	}

	/**
	 * 根据登记单元ID移除现状层房屋
	 * 
	 * @Author：俞学斌
	 * @param djdyid
	 *            登记单元ID
	 * @return 成功返回true，失败返回false
	 */
	protected boolean removeFWFromXZ(String djdyid) {
		StringBuilder builderDJDYID = new StringBuilder();
		builderDJDYID.append(" DJDYID='").append(djdyid).append("'");
		String strQueryDJDYID = builderDJDYID.toString();
		List<BDCS_DJDY_XZ> djdys = baseCommonDao.getDataList(BDCS_DJDY_XZ.class, strQueryDJDYID);
		if (djdys != null && djdys.size() > 0) {
			for (int idjdy = 0; idjdy < djdys.size(); idjdy++) {
				String bdcdyid = djdys.get(idjdy).getBDCDYID();
				BDCS_H_XZ bdcs_h_xz = baseCommonDao.get(BDCS_H_XZ.class, bdcdyid);
				if (bdcs_h_xz != null) {
					baseCommonDao.deleteEntity(bdcs_h_xz);
				}
			}
		}
		return true;
	}

	/**
	 * 根据登记单元ID移除现状层权利
	 * 
	 * @Author：俞学斌
	 * @param djdyid
	 *            登记单元ID
	 * @return 成功返回true，失败返回false
	 */
	protected boolean removeQLFromXZByQLLX(String djdyid) {
		String dbr = Global.getCurrentUserName();
		StringBuilder builderDJDYID = new StringBuilder();
		builderDJDYID.append(" DJDYID='").append(djdyid).append("'");
		builderDJDYID.append(" and QLLX='").append(getQllx().Value).append("'");
		String strSqlDJDYID = builderDJDYID.toString();
		List<BDCS_QL_XZ> qls = baseCommonDao.getDataList(BDCS_QL_XZ.class, strSqlDJDYID);
		if (qls != null && qls.size() > 0) {
			for (int iql = 0; iql < qls.size(); iql++) {
				BDCS_QL_XZ bdcs_ql_xz = qls.get(iql);
				if (bdcs_ql_xz != null) {
					BDCS_FSQL_XZ bdcs_fsql_xz = baseCommonDao.get(BDCS_FSQL_XZ.class, bdcs_ql_xz.getFSQLID());
					if (bdcs_fsql_xz != null) {
						baseCommonDao.deleteEntity(bdcs_fsql_xz);
					}
					BDCS_FSQL_LS bdcs_fsql_ls = baseCommonDao.get(BDCS_FSQL_LS.class, bdcs_ql_xz.getFSQLID());
					if (bdcs_fsql_ls != null) {
						// 注销时间，注销登簿人，注销业务号
						bdcs_fsql_ls.setZXSJ(new Date());
						bdcs_fsql_ls.setZXDBR(dbr);
						bdcs_fsql_ls.setZXDYYWH(getProject_id());
						baseCommonDao.update(bdcs_fsql_ls);
					}
					baseCommonDao.deleteEntity(bdcs_ql_xz);
				}
			}
		}
		return true;
	}

	/**
	 * 根据登记单元ID移除现状层权利
	 * 
	 * @Author：俞学斌
	 * @param djdyid
	 *            登记单元ID
	 * @return 成功返回true，失败返回false
	 */
	protected boolean removeQLFromXZByALL(String djdyid) {
		String dbr = Global.getCurrentUserName();
		StringBuilder builderDJDYID = new StringBuilder();
		builderDJDYID.append(" DJDYID='").append(djdyid).append("'");
		;
		String strSqlDJDYID = builderDJDYID.toString();
		List<BDCS_QL_XZ> qls = baseCommonDao.getDataList(BDCS_QL_XZ.class, strSqlDJDYID);
		if (qls != null && qls.size() > 0) {
			for (int iql = 0; iql < qls.size(); iql++) {
				BDCS_QL_XZ bdcs_ql_xz = qls.get(iql);
				if (bdcs_ql_xz != null) {
					BDCS_FSQL_XZ bdcs_fsql_xz = baseCommonDao.get(BDCS_FSQL_XZ.class, bdcs_ql_xz.getFSQLID());
					if (bdcs_fsql_xz != null) {
						baseCommonDao.deleteEntity(bdcs_fsql_xz);
					}
					BDCS_FSQL_LS bdcs_fsql_ls = baseCommonDao.get(BDCS_FSQL_LS.class, bdcs_ql_xz.getFSQLID());
					if (bdcs_fsql_ls != null) {
						bdcs_fsql_ls.setZXSJ(new Date());
						bdcs_fsql_ls.setZXDBR(dbr);
						bdcs_fsql_ls.setZXDYYWH(getProject_id());
						baseCommonDao.update(bdcs_fsql_ls);
					}
					baseCommonDao.deleteEntity(bdcs_ql_xz);
				}
			}
		}
		return true;
	}

	/**
	 * 根据权利ID，移除现状层权利、附属权利、权利人、证书、权地证人关系
	 * 
	 * @Author：俞学斌
	 * @param qlid
	 *            现状层权利ID
	 * @return 成功返回true，失败返回false
	 */
	protected boolean removeQLXXFromXZByQLID(String qlid) {
		String dbr = Global.getCurrentUserName();
		BDCS_QL_XZ bdcs_ql_xz = baseCommonDao.get(BDCS_QL_XZ.class, qlid);
		if (bdcs_ql_xz != null) {
			BDCS_FSQL_XZ bdcs_fsql_xz = baseCommonDao.get(BDCS_FSQL_XZ.class, bdcs_ql_xz.getFSQLID());
			if (bdcs_fsql_xz != null) {
				baseCommonDao.deleteEntity(bdcs_fsql_xz);
			}
			BDCS_FSQL_LS bdcs_fsql_ls = baseCommonDao.get(BDCS_FSQL_LS.class, bdcs_ql_xz.getFSQLID());
			if (bdcs_fsql_ls != null) {
				bdcs_fsql_ls.setZXSJ(new Date());
				bdcs_fsql_ls.setZXDBR(dbr);
				bdcs_fsql_ls.setZXDYYWH(getProject_id());
				baseCommonDao.update(bdcs_fsql_ls);
			}
			StringBuilder builder = new StringBuilder();
			builder.append(" QLID='").append(qlid).append("'");
			String strQuery = builder.toString();
			List<BDCS_QLR_XZ> qlrs = baseCommonDao.getDataList(BDCS_QLR_XZ.class, strQuery);
			if (qlrs != null && qlrs.size() > 0) {
				for (int iqlr = 0; iqlr < qlrs.size(); iqlr++) {
					BDCS_QLR_XZ bdcs_qlr_xz = qlrs.get(iqlr);
					baseCommonDao.deleteEntity(bdcs_qlr_xz);
				}
			}
			List<BDCS_QDZR_XZ> qdzrs = baseCommonDao.getDataList(BDCS_QDZR_XZ.class, strQuery);
			if (qdzrs != null && qdzrs.size() > 0) {
				for (int iqdzr = 0; iqdzr < qdzrs.size(); iqdzr++) {
					BDCS_QDZR_XZ bdcs_qdzr_xz = qdzrs.get(iqdzr);
					if (bdcs_qdzr_xz != null) {
						BDCS_ZS_XZ bdcs_zs_xz = baseCommonDao.get(BDCS_ZS_XZ.class, bdcs_qdzr_xz.getZSID());
						if (bdcs_zs_xz != null) {
							baseCommonDao.deleteEntity(bdcs_zs_xz);
						}
						baseCommonDao.deleteEntity(bdcs_qdzr_xz);
					}
				}
			}
			baseCommonDao.deleteEntity(bdcs_ql_xz);
		}
		return true;
	}

	/**
	 * 根据登记单元ID移除现状权利人
	 * 
	 * @Author：俞学斌
	 * @param djdyid
	 *            登记单元ID
	 * @return 成功返回true，失败返回false
	 */
	protected boolean removeQLRFromXZByQLLX(String djdyid) {
		StringBuilder builder = new StringBuilder();
		builder.append(" QLID IN (");
		builder.append("select id FROM BDCS_QL_XZ WHERE DJDYID ='");
		builder.append(djdyid);
		builder.append("' and QLLX='").append(getQllx().Value).append("'");
		builder.append(")");
		String strQueryQLR = builder.toString();
		List<BDCS_QLR_XZ> qlrs = baseCommonDao.getDataList(BDCS_QLR_XZ.class, strQueryQLR);
		if (qlrs != null && qlrs.size() > 0) {
			for (int iqlr = 0; iqlr < qlrs.size(); iqlr++) {
				BDCS_QLR_XZ bdcs_qlr_xz = qlrs.get(iqlr);
				baseCommonDao.deleteEntity(bdcs_qlr_xz);
			}
		}
		return true;
	}

	/**
	 * 根据登记单元ID移除现状层权利人记录
	 * 
	 * @Author：俞学斌
	 * @param djdyid
	 *            登记单元ID
	 * @return 成功返回true，失败返回false
	 */
	protected boolean removeQLRFromXZByALL(String djdyid) {
		StringBuilder builder = new StringBuilder();
		builder.append(" QLID IN (");
		builder.append("select id FROM BDCS_QL_XZ WHERE DJDYID ='");
		builder.append(djdyid);
		builder.append("')");
		String strQueryQLR = builder.toString();
		List<BDCS_QLR_XZ> qlrs = baseCommonDao.getDataList(BDCS_QLR_XZ.class, strQueryQLR);
		if (qlrs != null && qlrs.size() > 0) {
			for (int iqlr = 0; iqlr < qlrs.size(); iqlr++) {
				BDCS_QLR_XZ bdcs_qlr_xz = qlrs.get(iqlr);
				baseCommonDao.deleteEntity(bdcs_qlr_xz);
			}
		}
		return true;
	}

	/**
	 * 根据登记单元ID移除现状层证书记录
	 * 
	 * 
	 * @Author：俞学斌
	 * @param djdyid
	 *            登记单元ID
	 * @return 成功返回true，失败返回false
	 */
	protected boolean removeZSFromXZByQLLX(String djdyid) {
		List<String> listZSIDDelete = new ArrayList<String>();
		List<BDCS_QL_XZ> qls = baseCommonDao.getDataList(BDCS_QL_XZ.class, "DJDYID='" + djdyid + "' AND QLLX='" + getQllx().Value + "'");
		if (qls != null && qls.size() > 0) {
			for (int iql = 0; iql < qls.size(); iql++) {
				BDCS_QL_XZ ql = qls.get(iql);
				List<BDCS_QDZR_XZ> qdzrs = baseCommonDao.getDataList(BDCS_QDZR_XZ.class, "QLID='" + ql.getId() + "'");
				if (qdzrs != null && qdzrs.size() > 0) {
					for (int iqdzr = 0; iqdzr < qdzrs.size(); iqdzr++) {
						BDCS_QDZR_XZ qdzr = qdzrs.get(iqdzr);
						String zsid = qdzr.getZSID();
						if (listZSIDDelete.contains(zsid)) {
							continue;
						}
						listZSIDDelete.add(zsid);
						BDCS_ZS_XZ zs = baseCommonDao.get(BDCS_ZS_XZ.class, zsid);
						if (zs != null) {
							baseCommonDao.deleteEntity(zs);
						}
					}
				}
			}
		}
		return true;
	}

	/**
	 * 根据登记单元ID移除现状层证书记录
	 * 
	 * 
	 * @Author：俞学斌
	 * @param djdyid
	 *            登记单元ID
	 * @return 成功返回true，失败返回false
	 */
	protected boolean removeZSFromXZByALL(String djdyid) {
		StringBuilder builder = new StringBuilder();
		builder.append(" id IN (");
		builder.append("select ZSID FROM BDCS_QDZR_XZ WHERE ");
		builder.append(" QLID in (");
		builder.append("select id FROM BDCS_QL_XZ WHERE DJDYID ='");
		builder.append(djdyid);
		builder.append("'))");
		String strQueryZS = builder.toString();
		List<BDCS_ZS_XZ> zss = baseCommonDao.getDataList(BDCS_ZS_XZ.class, strQueryZS);
		if (zss != null && zss.size() > 0) {
			for (int izs = 0; izs < zss.size(); izs++) {
				BDCS_ZS_XZ bdcs_zs_xz = zss.get(izs);
				baseCommonDao.deleteEntity(bdcs_zs_xz);
			}
		}
		return true;
	}

	/**
	 * 根据登记单元ID移除现状层权地证人关系，加上权利类型的过滤
	 * 
	 * 
	 * @Author：俞学斌
	 * @param djdyid
	 *            登记单元ID
	 * @return 成功返回true，失败返回false
	 */
	protected boolean removeQDZRFromXZByQLLX(String djdyid) {
		StringBuilder builderDJDYID = new StringBuilder();
		builderDJDYID.append(" QLID IN (");
		builderDJDYID.append("select id FROM BDCS_QL_XZ WHERE DJDYID ='");
		builderDJDYID.append(djdyid);
		builderDJDYID.append("' and QLLX='").append(getQllx().Value).append("'");
		builderDJDYID.append(")");
		String strSqlDJDYID = builderDJDYID.toString();
		List<BDCS_QDZR_XZ> qdzrs = baseCommonDao.getDataList(BDCS_QDZR_XZ.class, strSqlDJDYID);
		if (qdzrs != null && qdzrs.size() > 0) {
			for (int iqdzr = 0; iqdzr < qdzrs.size(); iqdzr++) {
				BDCS_QDZR_XZ bdcs_qdzr_xz = qdzrs.get(iqdzr);
				baseCommonDao.deleteEntity(bdcs_qdzr_xz);
			}
		}
		return true;
	}

	/**
	 * 根据登记单元ID移除现状层权地证人关系，不加权利类型过滤
	 * 
	 * 
	 * @Author：俞学斌
	 * @param djdyid
	 *            登记单元ID
	 * @return 成功返回true，失败返回false
	 */
	protected boolean removeQDZRFromXZByALL(String djdyid) {
		StringBuilder builderDJDYID = new StringBuilder();
		builderDJDYID.append(" QLID IN (");
		builderDJDYID.append("select id FROM BDCS_QL_XZ WHERE DJDYID ='");
		builderDJDYID.append(djdyid);
		builderDJDYID.append("')");
		String strSqlDJDYID = builderDJDYID.toString();
		List<BDCS_QDZR_XZ> qdzrs = baseCommonDao.getDataList(BDCS_QDZR_XZ.class, strSqlDJDYID);
		if (qdzrs != null && qdzrs.size() > 0) {
			for (int iqdzr = 0; iqdzr < qdzrs.size(); iqdzr++) {
				BDCS_QDZR_XZ bdcs_qdzr_xz = qdzrs.get(iqdzr);
				baseCommonDao.deleteEntity(bdcs_qdzr_xz);
			}
		}
		return true;
	}

	/**
	 * 根据登记单元ID从现状层中移除不动产单元，未完待续
	 * 
	 * @Author 刘树峰
	 * @创建时间 2015年6月24日下午4:46:05
	 * @param djdyid
	 *            登记单元ID
	 * @return 成功返回true，失败返回false
	 */
	protected boolean removeDYFromXZ(String djdyid) {
		if (ConstValue.QLLX.GYJSYDSHYQ.equals(getQllx())) {
			removeSHYQZDFromXZ(djdyid);
		} else if (ConstValue.QLLX.GYJSYDSHYQ_FWSYQ.equals(getQllx()) && "01".equals(getSllx1().trim())) {
			removeFWFromXZ(djdyid);
		} else {
			return false;
		}
		return true;
	}

	/**
	 * 构建变更单元
	 * 
	 * 
	 * @Author：俞学斌
	 * @param lbdcdyid
	 *            老不动产单元ID
	 * @param ldjdyid
	 *            老登记单元ID
	 * @param xbdcdyid
	 *            新不动产单元ID
	 * @param xdjdyid
	 *            新登记单元ID
	 * @param createtime
	 *            创建时间
	 * @param modifytime
	 *            修改时间
	 * @return 成功返回true，失败返回false
	 */
	protected boolean RebuildDYBG(String lbdcdyid, String ldjdyid, String xbdcdyid, String xdjdyid, Date createtime, Date modifytime) {
		BDCS_DYBG bdcs_dybg = new BDCS_DYBG();
		bdcs_dybg.setCreateTime(createtime);
		bdcs_dybg.setLBDCDYID(lbdcdyid);
		bdcs_dybg.setLDJDYID(ldjdyid);
		bdcs_dybg.setModifyTime(modifytime);
		bdcs_dybg.setXBDCDYID(xbdcdyid);
		bdcs_dybg.setXDJDYID(xdjdyid);
		bdcs_dybg.setXMBH(getXMBH());
		getCommonDao().save(bdcs_dybg);
		return true;
	}

	/**
	 * 设定项目信息中登簿标识为成功
	 * 
	 * @Author：俞学斌
	 * @return 成功返回true，失败返回false
	 */
	protected boolean SetSFDB() {
		BDCS_XMXX xmxx = Global.getXMXXbyXMBH(this.getXMBH());
		xmxx.setSFDB("1");
		xmxx.setDJSJ(new Date());
		getCommonDao().update(xmxx);
		return true;
	}

	/**
	 * 设定不动产单元抵押状态
	 * 
	 * @Author 俞学斌
	 * @创建时间 2015年6月24日下午4:49:09
	 * @param bdcdyid
	 *            不动产单元ID
	 * @param bdcdylx
	 *            不动产单元类型
	 * @param dyzt
	 *            抵押状态
	 * @return 成功返回true，失败返回false
	 */
	protected boolean SetDYDYZT(String bdcdyid, BDCDYLX bdcdylx, String dyzt) {
		if ((bdcdylx.Value).equals(BDCDYLX.SHYQZD.Value)) {
			// 更新使用权宗地
			BDCS_SHYQZD_XZ bdcs_shyqzd_xz = getCommonDao().get(BDCS_SHYQZD_XZ.class, bdcdyid);
			if (bdcs_shyqzd_xz != null) {
				bdcs_shyqzd_xz.setDYZT(dyzt);
				getCommonDao().update(bdcs_shyqzd_xz);
			}
			BDCS_SHYQZD_LS bdcs_shyqzd_ls = getCommonDao().get(BDCS_SHYQZD_LS.class, bdcdyid);
			if (bdcs_shyqzd_ls != null) {
				bdcs_shyqzd_ls.setDYZT(dyzt);
				getCommonDao().update(bdcs_shyqzd_ls);
			}
		} else if ((bdcdylx.Value).equals(BDCDYLX.H.Value)) {
			// 更新户
			BDCS_H_XZ bdcs_h_xz = getCommonDao().get(BDCS_H_XZ.class, bdcdyid);
			if (bdcs_h_xz != null) {
				bdcs_h_xz.setDYZT(dyzt);
				getCommonDao().update(bdcs_h_xz);
			}
			BDCS_H_LS bdcs_h_ls = getCommonDao().get(BDCS_H_LS.class, bdcdyid);
			if (bdcs_h_ls != null) {
				bdcs_h_ls.setDYZT(dyzt);
				getCommonDao().update(bdcs_h_ls);
			}
		} else if ((bdcdylx.Value).equals(BDCDYLX.YCH.Value)) {
			BDCS_H_XZY bdcs_h_xzy = getCommonDao().get(BDCS_H_XZY.class, bdcdyid);
			if (bdcs_h_xzy != null) {
				bdcs_h_xzy.setDYZT(dyzt);
				getCommonDao().update(bdcs_h_xzy);
				BDCS_H_LSY lsyh = getCommonDao().get(BDCS_H_LSY.class, bdcdyid);
				if (lsyh != null) {
					lsyh.setDYZT(dyzt);
					getCommonDao().update(lsyh);
				} else {
					BDCS_H_LSY bdcs_h_lsy = new BDCS_H_LSY();
					bdcs_h_lsy = ObjectHelper.copyH_LSY(bdcs_h_xzy, bdcs_h_lsy);
					getCommonDao().save(bdcs_h_lsy);
				}
			}
		} else if ((bdcdylx.Value).equals(BDCDYLX.YCZRZ.Value)) {
			BDCS_ZRZ_XZY bdcs_zrz_xzy = getCommonDao().get(BDCS_ZRZ_XZY.class, bdcdyid);
			if (bdcs_zrz_xzy != null) {
				// bdcs_zrz_xzy.setDYZT(dyzt);
				BDCS_ZRZ_LSY lsyzrz = getCommonDao().get(BDCS_ZRZ_LSY.class, bdcdyid);
				if (lsyzrz != null) {
					// bdcs_zrz_xzy.setDYZT(dyzt);
				} else {
					BDCS_ZRZ_LSY bdcs_zrz_lsy = new BDCS_ZRZ_LSY();
					bdcs_zrz_lsy = ObjectHelper.copyZRZ_LSY(bdcs_zrz_xzy, bdcs_zrz_lsy);
					getCommonDao().save(bdcs_zrz_lsy);
				}
			}
		}
		return true;
	}

	/**
	 * 设定单元的查封状态
	 * 
	 * @Author 俞学斌
	 * @创建时间 2015年6月24日下午4:49:58
	 * @param djdyid
	 *            登记单元ID
	 * @param cfzt
	 *            查封状态
	 * @return 成功返回true，失败返回false
	 */
	protected boolean SetXMCFZT(String djdyid, String cfzt) {
		StringBuilder builder = new StringBuilder();
		builder.append(" id IN (");
		builder.append("SELECT DISTINCT XMBH FROM BDCS_DJDY_GZ WHERE DJDYID ='");
		builder.append(djdyid).append("')");
		String strQuery = builder.toString();
		List<BDCS_XMXX> xmxxs = baseCommonDao.getDataList(BDCS_XMXX.class, strQuery);
		if (xmxxs != null && xmxxs.size() > 0) {
			for (int ixmxx = 0; ixmxx < xmxxs.size(); ixmxx++) {
				BDCS_XMXX xmxx = xmxxs.get(ixmxx);
				String xmbh1 = xmxx.getId();
				if (!xmbh1.equals(getXMBH())) {
					xmxx.setYXBZ(cfzt);
					BDCS_XMXX xmxx2 = Global.getXMXXbyXMBH(xmxx.getId());
					if (xmxx2 != null)
						xmxx2.setYXBZ(cfzt);
					baseCommonDao.update(xmxx);
				}
			}
		}
		return true;
	}

	/**
	 * 修改缓存的项目信息的登簿入库属性
	 * 
	 * @Author 刘树峰 2015年6月22日上午1:31:09
	 */
	public void alterCachedXMXX() {
//		BDCS_XMXX xmxx = baseCommonDao.get(BDCS_XMXX.class, xmbh);
//		if (xmxx != null) {
//			BDCS_XMXX cachedXMXX = Global.getXMXX(xmxx.getPROJECT_ID());
//			if (cachedXMXX != null) {
//				cachedXMXX.setSFDB(SFDB.YES.Value);
//			}
//		}
	}

	/**
	 * 获取单元抵押状态
	 * 
	 * @Author：俞学斌
	 * @CreateTime 2015年6月22日 下午5:55:53
	 * @param djdyid
	 *            登记单元ID
	 * @return 状态字符串
	 */
	protected String GetDYZT(String djdyid) {
		String strDYZT = "1";
		StringBuilder builder = new StringBuilder();
		builder.append(" DJDYID='").append(djdyid).append("'");
		builder.append(" AND QLLX='23'");
		String strQuery = builder.toString();
		List<BDCS_QL_XZ> qls = getCommonDao().getDataList(BDCS_QL_XZ.class, strQuery);
		if (qls != null && qls.size() > 0) {
			strDYZT = "0";
		}
		return strDYZT;
	}
	

	protected BDCS_QL_GZ CopyQLXXFromXZ(String qlid) {
		BDCS_QL_GZ bdcs_ql_gz = null;
		StringBuilder builer = new StringBuilder();
		builer.append(" QLID='").append(qlid).append("'");
		String strQuery = builer.toString();
		BDCS_QL_XZ bdcs_ql_xz = getCommonDao().get(BDCS_QL_XZ.class, qlid);
		if (bdcs_ql_xz != null) {
			String gzqlid = getPrimaryKey();
			String gzfsqlid = getPrimaryKey();
			// 拷贝权利
			bdcs_ql_gz = ObjectHelper.copyQL_XZToGZ(bdcs_ql_xz);
			bdcs_ql_gz.setId(gzqlid);
			bdcs_ql_gz.setQLLX(getQllx().Value);
			if(!BDCDYLX.YCH.equals(this.getBdcdylx())){
				bdcs_ql_gz.setDJLX(getDjlx().Value);
			}else{
				if(StringHelper.isEmpty(bdcs_ql_gz.getDJLX())){
					bdcs_ql_gz.setDJLX(getDjlx().Value);
				}
			}
			bdcs_ql_gz.setFSQLID(gzfsqlid);
			bdcs_ql_gz.setXMBH(getXMBH());
			bdcs_ql_gz.setLYQLID(qlid);
			bdcs_ql_gz.setDJSJ(null);
			bdcs_ql_gz.setDBR("");
			bdcs_ql_gz.setYWH(getProject_id());
			bdcs_ql_gz.setZSBS(ZSBS.DYB.Value);// 默认单一版持证方式
			bdcs_ql_gz.setARCHIVES_BOOKNO("");
			bdcs_ql_gz.setARCHIVES_CLASSNO("");
			bdcs_ql_gz.setCASENUM("");
			// bdcs_ql_gz.setDJYY("");
			// bdcs_ql_gz.setFJ("");

			BDCS_FSQL_XZ bdcs_fsql_xz = getCommonDao().get(BDCS_FSQL_XZ.class, bdcs_ql_xz.getFSQLID());
			if (bdcs_fsql_xz != null) {
				// 拷贝附属权利
				BDCS_FSQL_GZ bdcs_fsql_gz = ObjectHelper.copyFSQL_XZToGZ(bdcs_fsql_xz);
				bdcs_fsql_gz.setQLID(gzqlid);
				bdcs_fsql_gz.setId(gzfsqlid);
				bdcs_fsql_gz.setXMBH(getXMBH());
				getCommonDao().save(bdcs_fsql_gz);
			}

			// 获取证书集合
			StringBuilder builderZSALL = new StringBuilder();
			builderZSALL.append(" id IN (");
			builderZSALL.append("select ZSID FROM BDCS_QDZR_XZ WHERE QLID ='");
			builderZSALL.append(qlid).append("')");
			String strQueryZSALL = builderZSALL.toString();
			List<BDCS_ZS_XZ> zssALL = getCommonDao().getDataList(BDCS_ZS_XZ.class, strQueryZSALL);
			// 当证书个数大于1时，持证方式为分别持证，否则为共同持证
			if (zssALL.size() > 1) {
				bdcs_ql_gz.setCZFS(CZFS.FBCZ.Value);
			} else {
				if (StringHelper.isEmpty(bdcs_ql_gz.getCZFS()) || (!bdcs_ql_gz.getCZFS().equals(CZFS.FBCZ.Value) && !bdcs_ql_gz.getCZFS().equals(CZFS.GTCZ.Value))) {
					bdcs_ql_gz.setCZFS(CZFS.GTCZ.Value);
				}
			}
			Map<String, String> lyzsid_zsid = new HashMap<String, String>();

			StringBuilder builderDJDY = new StringBuilder();
			builderDJDY.append(" DJDYID='");
			builderDJDY.append(bdcs_ql_xz.getDJDYID());
			builderDJDY.append("'");
			// 获取权利人集合
			List<BDCS_QLR_XZ> qlrs = getCommonDao().getDataList(BDCS_QLR_XZ.class, strQuery);
			if (qlrs != null && qlrs.size() > 0) {
				for (int iqlr = 0; iqlr < qlrs.size(); iqlr++) {
					BDCS_QLR_XZ bdcs_qlr_xz = qlrs.get(iqlr);
					if (bdcs_qlr_xz != null) {
						// 拷贝权利人
						String gzqlrid = getPrimaryKey();
						BDCS_QLR_GZ bdcs_qlr_gz = ObjectHelper.copyQLR_XZToGZ(bdcs_qlr_xz);
						bdcs_qlr_gz.setId(gzqlrid);
						bdcs_qlr_gz.setQLID(gzqlid);
						bdcs_qlr_gz.setXMBH(getXMBH());
						getCommonDao().save(bdcs_qlr_gz);
						// 获取证书集合
						StringBuilder builder = new StringBuilder();
						builder.append(" id IN (");
						builder.append("select ZSID FROM BDCS_QDZR_XZ WHERE QLID ='");
						builder.append(qlid).append("'").append(" AND QLRID='");
						builder.append(bdcs_qlr_xz.getId()).append("')");
						String strQueryZS = builder.toString();
						List<BDCS_ZS_XZ> zss = getCommonDao().getDataList(BDCS_ZS_XZ.class, strQueryZS);
						if (zss != null && zss.size() > 0) {
							BDCS_ZS_XZ bdcs_zs_xz = zss.get(0);
							if (!lyzsid_zsid.containsKey(bdcs_zs_xz.getId())) {
								String gzzsid = getPrimaryKey();
								BDCS_ZS_GZ bdcs_zs_gz = ObjectHelper.copyZS_XZToGZ(bdcs_zs_xz);
								bdcs_zs_gz.setId(gzzsid);
								bdcs_zs_gz.setXMBH(getXMBH());
								getCommonDao().save(bdcs_zs_gz);
								lyzsid_zsid.put(bdcs_zs_xz.getId(), gzzsid);
							}
							// 获取权地证人集合
							StringBuilder builderQDZR = new StringBuilder();
							builderQDZR.append(strQuery);
							builderQDZR.append(" AND ZSID='");
							builderQDZR.append(bdcs_zs_xz.getId());
							builderQDZR.append("' AND QLID='");
							builderQDZR.append(qlid);
							builderQDZR.append("' AND QLRID='");
							builderQDZR.append(bdcs_qlr_xz.getId());
							builderQDZR.append("')");
							List<BDCS_QDZR_XZ> qdzrs = getCommonDao().getDataList(BDCS_QDZR_XZ.class, builderQDZR.toString());
							if (qdzrs != null && qdzrs.size() > 0) {
								for (int iqdzr = 0; iqdzr < qdzrs.size(); iqdzr++) {
									BDCS_QDZR_XZ bdcs_qdzr_xz = qdzrs.get(iqdzr);
									if (bdcs_qdzr_xz != null) {
										// 拷贝权地证人
										BDCS_QDZR_GZ bdcs_qdzr_gz = ObjectHelper.copyQDZR_XZToGZ(bdcs_qdzr_xz);
										bdcs_qdzr_gz.setId(getPrimaryKey());
										bdcs_qdzr_gz.setZSID(lyzsid_zsid.get(bdcs_zs_xz.getId()));
										bdcs_qdzr_gz.setQLID(gzqlid);
										bdcs_qdzr_gz.setFSQLID(gzfsqlid);
										bdcs_qdzr_gz.setQLRID(gzqlrid);
										bdcs_qdzr_gz.setXMBH(getXMBH());
										getCommonDao().save(bdcs_qdzr_gz);
									}
								}
							}
						}
					}
				}
			}
			getCommonDao().save(bdcs_ql_gz);
			bdcs_ql_xz.setDJZT("02");
			getCommonDao().update(bdcs_ql_xz);
		}
		return bdcs_ql_gz;
	}
	

	/**
	 * 从现状拷贝权利信息，不带权证号
	 * 
	 * @Author：俞学斌
	 * @CreateTime 2015年6月22日 下午5:55:53
	 * @param djdyid
	 *            登记单元ID
	 * @return 状态字符串
	 */
	protected BDCS_QL_GZ CopyQLXXFromXZExceptBDCQZH(String qlid) {
		BDCS_QL_GZ bdcs_ql_gz = null;
		StringBuilder builer = new StringBuilder();
		builer.append(" QLID='").append(qlid).append("'");
		String strQuery = builer.toString();
		BDCS_QL_XZ bdcs_ql_xz = getCommonDao().get(BDCS_QL_XZ.class, qlid);
		if (bdcs_ql_xz != null) {
			String gzqlid = getPrimaryKey();
			String gzfsqlid = getPrimaryKey();
			// 拷贝权利
			bdcs_ql_gz = ObjectHelper.copyQL_XZToGZ(bdcs_ql_xz);
			bdcs_ql_gz.setId(gzqlid);
			bdcs_ql_gz.setQLLX(getQllx().Value);
			if(!BDCDYLX.YCH.equals(this.getBdcdylx())){
				bdcs_ql_gz.setDJLX(getDjlx().Value);
			}else{
				if(StringHelper.isEmpty(bdcs_ql_gz.getDJLX())){
					bdcs_ql_gz.setDJLX(getDjlx().Value);
				}
			}
			bdcs_ql_gz.setFSQLID(gzfsqlid);
			bdcs_ql_gz.setXMBH(getXMBH());
			bdcs_ql_gz.setLYQLID(qlid);
			bdcs_ql_gz.setDJSJ(null);
			bdcs_ql_gz.setDBR("");
			bdcs_ql_gz.setBDCQZH("");
			bdcs_ql_gz.setZSBS(ZSBS.DYB.Value);// 默认单一版持证方式
			bdcs_ql_gz.setYWH(getProject_id());
			bdcs_ql_gz.setARCHIVES_BOOKNO("");
			bdcs_ql_gz.setARCHIVES_CLASSNO("");
			bdcs_ql_gz.setCASENUM("");
			// bdcs_ql_gz.setDJYY("");
			// bdcs_ql_gz.setFJ("");

			if (bdcs_ql_xz.getFSQLID() != null) {
				BDCS_FSQL_XZ bdcs_fsql_xz = getCommonDao().get(BDCS_FSQL_XZ.class, bdcs_ql_xz.getFSQLID());
				if (bdcs_fsql_xz != null) {
					// 拷贝附属权利
					BDCS_FSQL_GZ bdcs_fsql_gz = ObjectHelper.copyFSQL_XZToGZ(bdcs_fsql_xz);
					bdcs_fsql_gz.setQLID(gzqlid);
					bdcs_fsql_gz.setId(gzfsqlid);
					bdcs_fsql_gz.setXMBH(getXMBH());
					getCommonDao().save(bdcs_fsql_gz);
				}
			}
			// 获取证书集合
			StringBuilder builderZSALL = new StringBuilder();
			builderZSALL.append(" id IN (");
			builderZSALL.append("select ZSID FROM BDCS_QDZR_XZ WHERE QLID ='");
			builderZSALL.append(qlid).append("')");
			String strQueryZSALL = builderZSALL.toString();
			List<BDCS_ZS_XZ> zssALL = getCommonDao().getDataList(BDCS_ZS_XZ.class, strQueryZSALL);
			// 当证书个数大于1时，持证方式为分别持证，否则为共同持证
			if (zssALL.size() > 1) {
				bdcs_ql_gz.setCZFS(CZFS.FBCZ.Value);
			} else {
				if (StringHelper.isEmpty(bdcs_ql_gz.getCZFS()) || (!bdcs_ql_gz.getCZFS().equals(CZFS.FBCZ.Value) && !bdcs_ql_gz.getCZFS().equals(CZFS.GTCZ.Value))) {
					bdcs_ql_gz.setCZFS(CZFS.GTCZ.Value);
				}
			}

			StringBuilder builderDJDY = new StringBuilder();
			builderDJDY.append(" DJDYID='");
			builderDJDY.append(bdcs_ql_xz.getDJDYID());
			builderDJDY.append("'");
			// 获取权利人集合
			Map<String, String> lyzsid_zsid = new HashMap<String, String>();
			List<BDCS_QLR_XZ> qlrs = getCommonDao().getDataList(BDCS_QLR_XZ.class, strQuery);
			if (qlrs != null && qlrs.size() > 0) {
				for (int iqlr = 0; iqlr < qlrs.size(); iqlr++) {
					BDCS_QLR_XZ bdcs_qlr_xz = qlrs.get(iqlr);
					if (bdcs_qlr_xz != null) {
						// 拷贝权利人
						String gzqlrid = getPrimaryKey();
						BDCS_QLR_GZ bdcs_qlr_gz = ObjectHelper.copyQLR_XZToGZ(bdcs_qlr_xz);
						bdcs_qlr_gz.setId(gzqlrid);
						bdcs_qlr_gz.setQLID(gzqlid);
						bdcs_qlr_gz.setXMBH(getXMBH());
						bdcs_qlr_gz.setBDCQZHXH("");
						bdcs_qlr_gz.setBDCQZH("");
						getCommonDao().save(bdcs_qlr_gz);
						// 获取证书集合
						StringBuilder builder = new StringBuilder();
						builder.append(" id IN (");
						builder.append("select ZSID FROM BDCS_QDZR_XZ WHERE QLID ='");
						builder.append(qlid).append("'").append(" AND QLRID='");
						builder.append(bdcs_qlr_xz.getId()).append("')");
						String strQueryZS = builder.toString();
						List<BDCS_ZS_XZ> zss = getCommonDao().getDataList(BDCS_ZS_XZ.class, strQueryZS);
						if (zss != null && zss.size() > 0) {
							BDCS_ZS_XZ bdcs_zs_xz = zss.get(0);
							if (!lyzsid_zsid.containsKey(bdcs_zs_xz.getId())) {
								String gzzsid = getPrimaryKey();
								BDCS_ZS_GZ bdcs_zs_gz = ObjectHelper.copyZS_XZToGZ(bdcs_zs_xz);
								bdcs_zs_gz.setId(gzzsid);
								bdcs_zs_gz.setXMBH(getXMBH());
								bdcs_zs_gz.setBDCQZH("");
								bdcs_zs_gz.setZSBH("");
								getCommonDao().save(bdcs_zs_gz);
								lyzsid_zsid.put(bdcs_zs_xz.getId(), gzzsid);
							}
							// 获取权地证人集合
							StringBuilder builderQDZR = new StringBuilder();
							builderQDZR.append(strQuery);
							builderQDZR.append(" AND ZSID='");
							builderQDZR.append(bdcs_zs_xz.getId());
							builderQDZR.append("' AND QLID='");
							builderQDZR.append(qlid);
							builderQDZR.append("' AND QLRID='");
							builderQDZR.append(bdcs_qlr_xz.getId());
							builderQDZR.append("')");
							List<BDCS_QDZR_XZ> qdzrs = getCommonDao().getDataList(BDCS_QDZR_XZ.class, builderQDZR.toString());
							if (qdzrs != null && qdzrs.size() > 0) {
								for (int iqdzr = 0; iqdzr < qdzrs.size(); iqdzr++) {
									BDCS_QDZR_XZ bdcs_qdzr_xz = qdzrs.get(iqdzr);
									if (bdcs_qdzr_xz != null) {
										// 拷贝权地证人
										BDCS_QDZR_GZ bdcs_qdzr_gz = ObjectHelper.copyQDZR_XZToGZ(bdcs_qdzr_xz);
										bdcs_qdzr_gz.setId(getPrimaryKey());
										bdcs_qdzr_gz.setZSID(lyzsid_zsid.get(bdcs_zs_xz.getId()));
										bdcs_qdzr_gz.setQLID(gzqlid);
										bdcs_qdzr_gz.setFSQLID(gzfsqlid);
										bdcs_qdzr_gz.setQLRID(gzqlrid);
										bdcs_qdzr_gz.setXMBH(getXMBH());
										getCommonDao().save(bdcs_qdzr_gz);
									}
								}
							}
						}
					}
				}
			}
			getCommonDao().save(bdcs_ql_gz);
			bdcs_ql_xz.setDJZT("02");
			getCommonDao().update(bdcs_ql_xz);
		}
		return bdcs_ql_gz;
	}
	

	protected void RomoveQLXXFromGZ(String qlid) {
		String xmbh = getXMBH();
		// 先删除权利人
		String sqlQLR = MessageFormat.format("  XMBH=''{0}'' AND QLID =''{1}''", xmbh, qlid);
		getCommonDao().deleteEntitysByHql(BDCS_QLR_GZ.class, sqlQLR);
		// 再删除权利
		String sqlQL = MessageFormat.format(" XMBH=''{0}'' AND QLID=''{1}''", xmbh, qlid);
		getCommonDao().deleteEntitysByHql(BDCS_QL_GZ.class, sqlQL);
		// 再删除附属权利
		getCommonDao().deleteEntitysByHql(BDCS_FSQL_GZ.class, sqlQL);
		// 再删除证书
		String sqlZS = MessageFormat.format(" XMBH=''{0}'' AND id IN (SELECT B.ZSID FROM BDCS_QDZR_GZ B WHERE B.XMBH=''{0}'' AND B.QLID=''{1}'')", xmbh, qlid);
		getCommonDao().deleteEntitysByHql(BDCS_ZS_GZ.class, sqlZS);
		// 删除权利-权利人-证书-单元关系
		getCommonDao().deleteEntitysByHql(BDCS_QDZR_GZ.class, sqlQL);
	}

	
    protected List<BDCS_QLR_GZ> getQLRs(String qlid) {
		String xmbhcond = ProjectHelper.GetXMBHCondition(xmbh);
		String hql = " QLID='" + qlid + "' ";
		List<BDCS_QLR_GZ> qlrs = baseCommonDao.getDataList(BDCS_QLR_GZ.class, hql);
		if (qlrs != null && qlrs.size() > 0) {
			return qlrs;
		}
		return null;
	}

	/**
	 * 获取申请人
	 * @作者 diaoliwei
	 * @创建时间 2015年8月29日下午4:18:34
	 * @param qlrs
	 * @return
	 */
	protected List<BDCS_SQR> getSQRs(List<BDCS_QLR_GZ> qlrs) {
		String xmbhcond = ProjectHelper.GetXMBHCondition(xmbh);
		BDCS_SQR sqr = null;
		List<BDCS_SQR> lists = new ArrayList<BDCS_SQR>();
		for (BDCS_QLR_GZ bdcs_QLR_GZ : qlrs) {
			String hql = " id = '" + bdcs_QLR_GZ.getSQRID() + "' and " + xmbhcond;
			List<BDCS_SQR> sqrs = baseCommonDao.getDataList(BDCS_SQR.class, xmbhcond);
			if (null != sqrs && sqrs.size() > 0) {
				sqr = sqrs.get(0);
				lists.add(sqr);
			}
		}
		return lists;
	}


	/**
	 * 根据项目编号返回证书数量和证明数量
	 *
	 * @return
	 */
	private List countZmAndZs() {
		List list = new ArrayList();
		String sql = "select distinct t.bdcqzh,t.djlx,t.qllx from BDCK.BDCS_QL_GZ t,BDCK.bdcs_xmxx t1 where t.xmbh=t1.xmbh and t.xmbh='" + getXMBH() + "'";
		List<Map> zslist = baseCommonDao.getDataListByFullSql(sql);
		int zmCount = 0;
		int zsCount = 0;
		if (zslist.size() > 0) {
			String djlx;
			String qllx;
			for (int i = 0; i < zslist.size(); i++) {
				djlx = zslist.get(i).get("DJLX").toString();
				qllx = zslist.get(i).get("QLLX").toString();
				//返回证明数量
				if ((djlx.equals("100") && qllx.equals("23")) || (djlx.equals("200") && qllx.equals("23")) ||
						(djlx.equals("300") && qllx.equals("23")) || (djlx.equals("500") && qllx.equals("23")) ||
						(djlx.equals("700"))) {
					zmCount++;
				}
				//返回证书数量
				if ((djlx.equals("100") && !qllx.equals("23")) || (djlx.equals("200") && !qllx.equals("23")) ||
						(djlx.equals("300") && !qllx.equals("23")) || (djlx.equals("500") && !qllx.equals("23"))) {
					zsCount++;
				}
			}
		}
		list.add(zmCount);
		list.add(zsCount);
		return list;
	}
	/**
	 * 根据handlername对应的国家xsd标准，判断上报时报文是否添加界址点/线节点
	 * @param handlername
	 * @return boolean
	 */
	private boolean xmlIsGetJzxJzd(String handlername){
		boolean XmlIsGetJzxJzd =     handlername.contains("GZDJHANDLER") && DJLX.BGDJ.Value.equals(this.getDjlx().Value) 
							     ||  handlername.equals("CSDJHandler_LuZhou")
							     ||  handlername.equals("CSDJHandler") 
							     ||  handlername.equals("BGDJHandler"); 
	 return XmlIsGetJzxJzd;
	}
	/**
	 * 根据handlername对应的国家xsd标准，判断上报时报文是否添加空间属性节点
	 * @param handlername
	 * @return boolean
	 */
	private boolean xmlIsGetZDK103(String handlername){
		
		/*
		 * XmlIsGetZDK103 有两部分：一部分变更类的，另一部分首次类的，只有这两部分上报时候国家标准要求上报空间属性
		 */
		
		
		boolean XmlIsGetZDK103 =     handlername.contains("BGDJHandler") || handlername.contains("CSDJHandler") ;
		
		//(select distinct handlerid from bdck.t_baseworkflow where name like'%变更%' and name not like '%+%' and handlerid  not like '%BGDJHandler%')
		XmlIsGetZDK103 = XmlIsGetZDK103 
						              // || handlername.equals("BGDJ_JTTOGYJSYDSHYQHandler") //暂不处理 
						                 || handlername.equals("BGDJEXHandler")
						                 || handlername.equals("GZDJHandler_LuZhou")
						              // || handlername.equals("GZDJHandler")&& DJLX.BGDJ.Value.equals(this.getDjlx().Value)//走GZDJHandler的变更登记 Handler内部处理zdk103 
						                 || handlername.equals("BZDJHandler")
						                 || handlername.equals("BGDJ_DYFGHandler")
						                 || handlername.equals("BGDJandFGHandler");
		
		//(select distinct handlerid,name from bdck.t_baseworkflow where name like'%初始%' and name not like '%+%' and name not like '%抵押%' and handlerid  not like '%CSDJHandler%' ) 
		XmlIsGetZDK103 = XmlIsGetZDK103  
				                         || handlername.equals("DYZXCD_DJHandler")
				                         || handlername.equals("CQHFDJHandler") ;
				
		return XmlIsGetZDK103;
	}
	/**
	 * 填充数据上报报头
	 * @param mess
	 * @param flag
	 * @param ywh
	 * @param bdcdyh
	 * @param qhdm
	 * @param qlrs 
	 */
	protected void fillHead(Message mess, int flag, String ywh,String bdcdyh,String qhdm,String lyqlid) {
		Head head = mess.getHead();
		// 缺宗地代码和不动产单元号
		// Calendar calendar = Calendar.getInstance(); // 创建一个日历对象
		// calendar.setTime(new Date()); // 用当前时间初始化日历时间
		// String cyear = calendar.get(Calendar.YEAR) + "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
		String date = sdf.format(new Date());
		//String lsh = YwLogUtil.getMaxLsh();// 方便xml文件名，防止重复
		BDCS_XMXX xmxx=Global.getXMXXbyXMBH(this.getXMBH());
		List<ProcedureParam> params=new ArrayList<ProcedureParam>();
		ProcedureParam param=new ProcedureParam();
		param.setFieldtype(Types.INTEGER);
		param.setParamtype("out");
		param.setName("sXH");
		param.setSxh(1);
		params.add(param);
		HashMap<String,Object> info=ProcedureTools.executeProcedure(params, "GETSJSBXH", "BDCK");
		String lsh=StringHelper.formatObject(info.get("sXH"));
		String xzqhdm=ConfigHelper.getNameByValue("XZQHDM");
		if(!StringHelper.isEmpty(bdcdyh)&&bdcdyh.length()>=6){
			xzqhdm=bdcdyh.substring(0, 6);
		}else if(!StringHelper.isEmpty(qhdm)&&qhdm.length()>=6){
			xzqhdm=qhdm.substring(0, 6);
		}
		String bizMsgId = xzqhdm + date + StringHelper.PadLeft(lsh, 6, '0');
		head.setRecType(getRecType());
		head.setBizMsgID(bizMsgId);
		head.setASID("AS100");
		head.setAreaCode(xzqhdm);
		head.setRightType(this.getQllx().Value);
		if(QLLX.DIYQ.equals(this.getQllx())){
			head.setRightType(QLLX.QTQL.Value);
			if("411623".equals(ConfigHelper.getNameByValue("XZQHDM"))){
				head.setRightType("24");
				head.setRegType("900");
			}
		}
		head.setRegType(this.getDjlx().Value);
		head.setCreateDate(DateUtil.convertToXMLGregorianCalendar(this.getCreateTime()));
		CommonDao dao = this.getCommonDao();
		String xmbhHql = ProjectHelper.GetXMBHCondition(this.getXMBH());
		List<BDCS_DJDY_GZ> djdys = dao.getDataList(BDCS_DJDY_GZ.class, xmbhHql);
		if(djdys.size()>1){// 多个单元情况下，业务号结尾加索引（从1开始）
			String n = String.valueOf(flag+1);
			head.setRecFlowID(ywh+"-"+n);
		}else{
			head.setRecFlowID(ywh);
		}
		head.setRegOrgID(ConfigHelper.getNameByValue("DJJGMC"));
		//head.setPreCertId("");
		List list = countZmAndZs();
		head.setProofCount(Integer.parseInt(list.get(0).toString()));
		head.setCertCount(Integer.parseInt(list.get(1).toString()));
		List<BDCS_QL_LS> lsqls = null;
		if(!StringHelper.isEmpty(lyqlid)){
			lsqls = dao.getDataList(BDCS_QL_LS.class, "QLID='"+lyqlid+"'");
			StringBuilder qzhs = new StringBuilder();
			String lyqzh = "";
			String lybdcdyh = "";
			String lybdcdyid = "";
			if(lsqls != null&&lsqls.size()>0){
				for(BDCS_QL_LS lsql:lsqls){
					if(qzhs.indexOf(StringHelper.formatObject(lsql.getBDCQZH())) == -1){
						qzhs.append(StringHelper.formatObject(lsql.getBDCQZH())).append(",");
					}
				}
				lybdcdyh = StringHelper.formatObject(lsqls.get(0).getBDCDYH());
				lybdcdyid = StringHelper.formatObject(lsqls.get(0).getBDCDYID());
			}
			if(qzhs!=null&&!"".equals(qzhs.toString())&&qzhs.length()>0) {
				lyqzh = qzhs.substring(0, qzhs.length()-1);
			}
			
			head.setPreCertId(lyqzh);
			head.setPreEstateNum(bdcdyh);
			head.setPreBdcdyid(lybdcdyid);
			head.setPreQlid(StringHelper.formatObject(lyqlid));
		}else {
			head.setPreEstateNum("");
			head.setPreBdcdyid("");
			head.setPreQlid("");
			head.setPreCertId("");
			
		}
		
	}
   
	/**
	 * 生产报文文件名 
	 * 多个报文xml加入索引 区分//形如：XXXXXXXXXXXXXXXXXXX_1 
	 * 多个单元（即多个权利记录）对应多个报文//组合流程情况暂不考虑
	 * @param msg
	 * @param i
	 * @param size
	 * @param names
	 * @param djdy
	 * @return
	 */
    protected String getMessageFileName(Message msg, int i, int size, Map<String, String> names,BDCS_DJDY_GZ djdy )	{
    	String msgName ="";
//    	if(size>1){
//    		String n =String.valueOf(i+1);
//    		msgName = "Biz" + msg.getHead().getBizMsgID() +"-"+n+".xml";
//    		if(names!=null && djdy!=null){ // 主动传入null 代表原来代码不需要里面put值
//    				names.put(djdy.getDJDYID(), msg.getHead().getBizMsgID()+"-"+ n + ".xml");
//    		}
//    	}else{
    		msgName = "Biz" + msg.getHead().getBizMsgID() +".xml";
    		if(names!=null && djdy!=null){
    			names.put(djdy.getDJDYID(), msg.getHead().getBizMsgID()+".xml");
    		}
//    	}
    	return msgName;
    }
    //	this.fillSyqZdData( msg, ywh, oland, sqrs, qlrs, ql, xmxx, actinstID) 
	/**
	 * 填充所有权宗地数据
	 * 基本和使用权宗地填充方法大体一样，分两个方法写，为以后国家标准变动 做准备，修改时候耦合度低。
	 * @param msg
	 * @param ywh
	 * @param oland
	 * @param sqrs
	 * @param qlrs
	 * @param ql
	 * @param xmxx
	 * @param actinstID
	 */
	protected void fillSyqZdData(Message msg, String ywh,OwnerLand oland,List<BDCS_SQR> sqrs,List<BDCS_QLR_GZ> qlrs,BDCS_QL_GZ ql,BDCS_XMXX xmxx,String actinstID) {

    	RegisterWorkFlow flow = HandlerFactory.getWorkflow(xmxx.getPROJECT_ID());
  		String handlername = flow.getHandlername().toUpperCase();
  	    boolean XmlIsGetJzxJzd = !handlername.equals("GZDJHANDLER");
  		
		// JBXX 建表信息
		KTTZDJBXX jbxx = msg.getData().getKTTZDJBXX();
		jbxx = packageXml.getZDJBXX(jbxx, null, ql, oland, null);

		// JZX-JZD 界址线-界址点
		if(XmlIsGetJzxJzd){
			String zdzhdm = "";
			if (oland != null) {
				zdzhdm = oland.getZDDM();
			}
			KTTGYJZX jzx = msg.getData().getKTTGYJZX();
			jzx = packageXml.getKTTGYJZX(jzx, zdzhdm);
			KTTGYJZD jzd = msg.getData().getKTTGYJZD();
			jzd = packageXml.getKTTGYJZD(jzd, zdzhdm);
		}

		// BHQK 变化情况
		KTFZDBHQK bhqk = msg.getData().getZDBHQK();
		bhqk = packageXml.getKTFZDBHQK(bhqk, null, ql, oland, null);
		msg.getData().setZDBHQK(bhqk);
		// TDSYQ 土地所有权
		QLFQLTDSYQ tdsyq = msg.getData().getQLFQLTDSYQ();
		tdsyq = packageXml.getQLFQLTDSYQ(tdsyq, oland, ql, ywh);
  		// QLR 权利人
		List<ZTTGYQLR> zttqlr = msg.getData().getGYQLR();
		zttqlr = packageXml.getZTTGYQLRs(zttqlr, qlrs, null, ql, null, oland, null, null);
		msg.getData().setGYQLR(zttqlr);
		// SLSQ 受理申请
		DJTDJSLSQ sq = msg.getData().getDJSLSQ();
		sq = packageXml.getDJTDJSLSQ(sq, ywh, null, ql, xmxx, null, xmxx.getSLSJ(), oland, null, null);
		msg.getData().setDJSLSQ(sq);
		// SJ 收件
		List<DJFDJSJ> sj = msg.getData().getDJSJ();
		sj = packageXml.getDJFDJSJ(oland, ywh,actinstID);
		msg.getData().setDJSJ(sj);
		// SF 收费
		List<DJFDJSF> sfList = msg.getData().getDJSF();
		sfList = packageXml.getDJSF(oland,ywh,this.getXMBH());
		msg.getData().setDJSF(sfList);
		// SH 审核
		List<DJFDJSH> sh = msg.getData().getDJSH();
		sh = packageXml.getDJFDJSH(oland, ywh, this.getXMBH(), actinstID);
		msg.getData().setDJSH(sh);
		// SZ 缮证
		List<DJFDJSZ> sz = packageXml.getDJFDJSZ(oland, ywh, this.getXMBH());
		msg.getData().setDJSZ(sz);
	    // FZ 发证
		List<DJFDJFZ> fz = packageXml.getDJFDJFZ(oland, ywh, this.getXMBH());
		msg.getData().setDJFZ(fz);
		// GD 归档
		List<DJFDJGD> gd = packageXml.getDJFDJGD(oland ,ywh,this.getXMBH());
		msg.getData().setDJGD(gd);
		
//		List<ZDK103> zdk = msg.getData().getZDK103();// 空间属性各个流程有区分，在流程中处理
//		zdk = packageXml.getZDK103(zdk, null, oland, null); 
		
		// SQR 申请人
		List<DJFDJSQR> djsqrs = msg.getData().getDJSQR();
		djsqrs = packageXml.getDJSQRs(djsqrs, sqrs, oland.getYSDM(), ywh, oland.getBDCDYH());
		msg.getData().setDJSQR(djsqrs);
		// FJ 非结    （结构化文档）
		FJF100 fj = msg.getData().getFJF100();
		fj = packageXml.getFJF(fj);
	}
	/**
	 * 填充使用权宗地数据
	 * @param msg
	 * @param ywh
	 * @param zd
	 * @param sqrs
	 * @param qlrs
	 * @param ql
	 * @param xmxx
	 * @param actinstID
	 */
    protected void fillShyqZdData(Message msg, String ywh,UseLand zd,List<BDCS_SQR> sqrs,List<BDCS_QLR_GZ> qlrs,BDCS_QL_GZ ql,BDCS_XMXX xmxx,String actinstID) {
    	
    	RegisterWorkFlow flow = HandlerFactory.getWorkflow(xmxx.getPROJECT_ID());
  		String handlername = flow.getHandlername().toUpperCase();
  		
  		/*-----------------------------------flag--------------------------------------*/
  	    // 是否获取变化情况,基本土地的登记都要变化情况
    	boolean XmlIsGetBhqk   =    !handlername.equals("BGDJ_DYHANDLER_HOUSEANDLAND")
    			                 && !handlername.equals("BGDJANDFGHANDLER") ;
  		// 是否获取界址线界址点，首次和变更的土地有界址点界址线
    	boolean XmlIsGetJzxJzd =   xmlIsGetJzxJzd(handlername);
    	// 是否获取空间属性
    	boolean XmlIsGetZDK103 =   xmlIsGetZDK103(handlername);
    			                   
    	/*-----------------------------------------------------------------------------*/
  		// QLR 权利人
  		List<ZTTGYQLR> zttqlr = msg.getData().getGYQLR();
		zttqlr = packageXml.getZTTGYQLRs(zttqlr, qlrs, zd, ql, null, null, null, null);
		msg.getData().setGYQLR(zttqlr);
		// JBXX 建表信息
		KTTZDJBXX jbxx = msg.getData().getKTTZDJBXX();
		jbxx = packageXml.getZDJBXX(jbxx, zd, ql, null, null);
		// BHQK 变化情况
        if(XmlIsGetBhqk){
    		KTFZDBHQK bhqk = msg.getData().getZDBHQK();
    		bhqk = packageXml.getKTFZDBHQK(bhqk, zd, ql, null, null);
    		if(handlername.equals("ZYDJHANDLER")){
    			msg.getData().setZDBHQK(null);
    		}else{
    			msg.getData().setZDBHQK(bhqk);
    		}
        }
		// JZX-JZD 界址线-界址点
		if(XmlIsGetJzxJzd){
			String zdzhdm = "";
			if (zd != null) {
				zdzhdm = zd.getZDDM();
			}
			KTTGYJZX jzx = msg.getData().getKTTGYJZX();
			jzx = packageXml.getKTTGYJZX(jzx, zdzhdm);

			KTTGYJZD jzd = msg.getData().getKTTGYJZD();
			jzd = packageXml.getKTTGYJZD(jzd, zdzhdm);
		}
        // ZDK103 宗地空间属性
        if(XmlIsGetZDK103){
        	List<ZDK103> zdk = msg.getData().getZDK103();
    		zdk = packageXml.getZDK103(zdk, zd, null, null);
    		msg.getData().setZDK103(zdk);
        }
        // JSYDSYQ  建设用地、宅基地使用权
    	QLFQLJSYDSYQ shyq = msg.getData().getQLJSYDSYQ();
    	if(shyq==null) {
    		shyq=new QLFQLJSYDSYQ();
    	}
		shyq = packageXml.getQLFQLJSYDSYQ(shyq, zd, ql, ywh);
		shyq.replaceEmpty();
		// SLSQ 受理申请
		DJTDJSLSQ sq = msg.getData().getDJSLSQ();
		sq = packageXml.getDJTDJSLSQ(sq, ywh, zd, ql, xmxx, null, xmxx.getSLSJ(), null, null, null);
		// SJ 收件
		List<DJFDJSJ> sj = msg.getData().getDJSJ();
		sj = packageXml.getDJFDJSJ(zd, ywh,actinstID);
		msg.getData().setDJSJ(sj);
		 // SF 收费
		List<DJFDJSF> sfList = msg.getData().getDJSF();
		sfList = packageXml.getDJSF(zd, ywh,this.getXMBH());
		msg.getData().setDJSF(sfList);
		 // SH 审核
		List<DJFDJSH> sh = msg.getData().getDJSH();
		sh = packageXml.getDJFDJSH(zd, ywh, this.getXMBH(), actinstID);
		msg.getData().setDJSH(sh);
        // SZ 缮证 
		List<DJFDJSZ> sz = packageXml.getDJFDJSZ(zd, ywh, this.getXMBH());
		msg.getData().setDJSZ(sz);
        // FZ 发证
		List<DJFDJFZ> fz = packageXml.getDJFDJFZ(zd, ywh, this.getXMBH());
		msg.getData().setDJFZ(fz);
		// GD 归档
		List<DJFDJGD> gd = packageXml.getDJFDJGD(zd,ywh,this.getXMBH());
		msg.getData().setDJGD(gd);
		// SQR 申请人
		
		List<DJFDJSQR> djsqrs = msg.getData().getDJSQR();
		djsqrs = packageXml.getDJSQRs(djsqrs, sqrs, zd.getYSDM(), ywh, zd.getBDCDYH());
		msg.getData().setDJSQR(djsqrs);
		// FJ 非结    （结构化文档）
		FJF100 fj = msg.getData().getFJF100();
		fj = packageXml.getFJF(fj);
		
//      if(! handlername.equals("GZDJHANDLER")&& DJLX.BGDJ.Value.equals(this.getDjlx().Value)){}
	}
    /**
     * 填充所有权房屋数据
     * @param Message             msg  
     * @param String              ywh  
     * @param LogicBuilding       ljz_
     * @param Floor               c_
     * @param Building            zrz_
     * @param House               h
     * @param List<BDCS_SQR>      sqrs
     * @param List<BDCS_QLR_GZ>   qlrs
     * @param BDCS_QL_GZ          ql
     * @param BDCS_XMXX           xmxx
     * @param String              actinstID
     */
	protected void fillFwData(Message msg, String ywh,LogicBuilding ljz_,Floor c_,Building zrz_,House h ,List<BDCS_SQR> sqrs,List<BDCS_QLR_GZ> qlrs,BDCS_QL_GZ ql,BDCS_XMXX xmxx,String actinstID) {
		
		RegisterWorkFlow flow = HandlerFactory.getWorkflow(xmxx.getPROJECT_ID());
  		String handlername = flow.getHandlername().toUpperCase();
  		// 首次和变更的情况下需要有ZDK103元素节点
        boolean isAppendZDK103 =    handlername.contains("BGDJ")
        		                 || handlername.contains("CSDJ")
        		                 || "GZDJHANDLER".equals(handlername) && DJLX.BGDJ.Value.equals(this.getDjlx().Value)
        		                 || "HZDJHANDLER".equals(handlername)
        		                 || "BZDJHANDLER".equals(handlername);
		if(isAppendZDK103){
		// 宗地空间属性
	        List<ZDK103> fwk = msg.getData().getZDK103();
			fwk = packageXml.getZDK103H(fwk, h, zrz_);
			msg.getData().setZDK103(fwk);
        }
  		// 房地产权_独幢、层、套、间房屋信息
  			QLTFWFDCQYZ fdcqyz = msg.getData().getQLTFWFDCQYZ();
  			fdcqyz = packageXml.getQLTFWFDCQYZ(fdcqyz, h, ql, ywh);
  			fdcqyz.replaceEmpty();
  			
  		// 房地产权_多幢表  
  			/*QLTFWFDCQDZ fdcqyz = msg.getData().getQLTFWFDCQDZ();
  			fdcqyz = packageXml.getQLTFWFDCQDZ(fdcqyz, h, ql, ywh);
  			fdcqyz.replaceEmpty();*/
  			
  		// 房地产权_多幢表_项目属性表	
  			/*QLFFWFDCQDZXM dzxm = msg.getData().getQLFFWFDCQDZXM();
  			dzxm = packageXml.getQLFFWFDCQDZXM( dzxm, h, ql);
  			dzxm.replaceEmpty();*/
  			
  
  	    // 权利人
  		List<ZTTGYQLR> zttqlr = msg.getData().getGYQLR();
		zttqlr = packageXml.getZTTGYQLRs(zttqlr, qlrs, null, ql, h, null, null, null);
		msg.getData().setGYQLR(zttqlr);
		if(zrz_!=null){
  	    // 自然幢
  		 	KTTFWZRZ zrz = msg.getData().getKTTFWZRZ();
  			zrz = packageXml.getKTTFWZRZ(zrz, zrz_);
        // 层
  			KTTFWC kttc = msg.getData().getKTTFWC();
  			kttc = packageXml.getKTTFWC(kttc, c_, zrz);
  			msg.getData().setKTTFWC(kttc);   }
  		// 逻辑幢
//		if(ljz_!=null){
			KTTFWLJZ ljz = msg.getData().getKTTFWLJZ();
			ljz = packageXml.getKTTFWLJZ(ljz, ljz_,h);	
//			}
		// 户
		KTTFWH fwh = msg.getData().getKTTFWH();
		fwh = packageXml.getKTTFWH(fwh, h);
  		// 登记受理申请信息
		DJTDJSLSQ sq = msg.getData().getDJSLSQ();
		sq = packageXml.getDJTDJSLSQ(sq, ywh, null, ql, xmxx, h, xmxx.getSLSJ(), null, null, null);
		// 收件
		List<DJFDJSJ> sj = msg.getData().getDJSJ();
		sj = packageXml.getDJFDJSJ(h, ywh,actinstID);
		msg.getData().setDJSJ(sj);
		// 收费
		List<DJFDJSF> sfList = msg.getData().getDJSF();
		sfList = packageXml.getDJSF(h,ywh,this.getXMBH());
		msg.getData().setDJSF(sfList);
        // 审核
		List<DJFDJSH> sh = msg.getData().getDJSH();
		sh = packageXml.getDJFDJSH(h, ywh, this.getXMBH(), actinstID);
		msg.getData().setDJSH(sh);
        // 缮证
		List<DJFDJSZ> sz = packageXml.getDJFDJSZ(h, ywh, this.getXMBH());
		msg.getData().setDJSZ(sz);
        // 发证
		List<DJFDJFZ> fz = packageXml.getDJFDJFZ(h, ywh, this.getXMBH());
		msg.getData().setDJFZ(fz);
		// 归档
		List<DJFDJGD> gd = packageXml.getDJFDJGD(h,ywh,this.getXMBH());
		msg.getData().setDJGD(gd);
		// 申请人属性信息
		List<DJFDJSQR> djsqrs = msg.getData().getDJSQR();
		djsqrs = packageXml.getDJSQRs(djsqrs, sqrs, h.getYSDM(), ywh, h.getBDCDYH());
		msg.getData().setDJSQR(djsqrs);
        // 非结构化文档
		FJF100 fj = msg.getData().getFJF100();
		fj = packageXml.getFJF(fj);
	}
	/**
	 * 填充在建工程房屋数据
	 * @param msg
	 * @param ywh
	 * @param xzy
	 * @param sqrs
	 * @param qlrs
	 * @param ql
	 * @param xmxx
	 * @param actinstID
	 */
	protected void fillZjgcFwData(Message msg, String ywh,BDCS_H_XZY xzy ,List<BDCS_SQR> sqrs,List<BDCS_QLR_GZ> qlrs,BDCS_QL_GZ ql,BDCS_XMXX xmxx,String actinstID) {
        // 权利人
		List<ZTTGYQLR> zttqlr = msg.getData().getGYQLR();
		zttqlr = packageXml.getZTTGYQLRs(zttqlr, qlrs, null, ql, xzy, null, null, null);
		msg.getData().setGYQLR(zttqlr);
        // 受理申请
		DJTDJSLSQ sq = msg.getData().getDJSLSQ();
		sq = packageXml.getDJTDJSLSQ(sq, ywh, null, ql, xmxx, xzy, getCreateTime(), null,
				null, null);
		
		// 收件
		List<DJFDJSJ> sj = msg.getData().getDJSJ();
		sj = packageXml.getDJFDJSJ(xzy, ywh,actinstID);
		msg.getData().setDJSJ(sj);
		// 收费
		List<DJFDJSF> sfList = msg.getData().getDJSF();
		sfList = packageXml.getDJSF(xzy,ywh,this.getXMBH());
		msg.getData().setDJSF(sfList);
		// 审核
		List<DJFDJSH> sh = msg.getData().getDJSH();
		sh = packageXml.getDJFDJSH(xzy, ywh, this.getXMBH(), actinstID);
		msg.getData().setDJSH(sh);
		// 缮证
		List<DJFDJSZ> sz = packageXml.getDJFDJSZ(xzy, ywh, this.getXMBH());
		msg.getData().setDJSZ(sz);
		// 发证
		List<DJFDJFZ> fz = packageXml.getDJFDJFZ(xzy, ywh, this.getXMBH());
		msg.getData().setDJFZ(fz);
		// 归档
		List<DJFDJGD> gd = packageXml.getDJFDJGD(xzy ,ywh,this.getXMBH());

		msg.getData().setDJGD(gd);
		// 申请人属性信息
		List<DJFDJSQR> djsqrs = msg.getData().getDJSQR();
		djsqrs = packageXml.getDJSQRs(djsqrs, sqrs, xzy.getYSDM(), ywh, xzy.getBDCDYH());
		msg.getData().setDJSQR(djsqrs);
		// 非结构化文档
		FJF100 fj = msg.getData().getFJF100();
		fj = packageXml.getFJF(fj);
	}
	
	
    protected String getRecType(){
		String strRecType="";
		if("NYD".equals(this.bdcdylx.toString())) {
//			农用地
			if (DJLX.CSDJ.Value.equals(this.getDjlx().Value)) { // 首次登记
				strRecType=RECCODE.NYD_CSDJ.Value;
			}
			if (DJLX.ZYDJ.Value.equals(this.getDjlx().Value)) { // 转移登记
				strRecType=RECCODE.NYD_ZYDJ.Value;
			}
			if (DJLX.BGDJ.Value.equals(this.getDjlx().Value)) { // 变更登记
				strRecType=RECCODE.NYD_BGDJ.Value;
			}
			if (DJLX.GZDJ.Value.equals(this.getDjlx().Value)) { // 更正登记
				strRecType=RECCODE.NYD_GZDJ.Value;
			}
			if (DJLX.ZXDJ.Value.equals(this.getDjlx().Value)){  // 注销登记
				strRecType=RECCODE.ZX_ZXDJ.Value;
			}
			if (DJLX.QTDJ.Value.equals(this.getDjlx().Value)){  // 补换证登记 或者登记类型为900的其他登记
				//strRecType=RECCODE.QTQL_BGDJ.Value;  // QTQL_BGDJ ，QTQL_GZDJ ，QTQL_CSDJ ,QTQL_ZYDJ 这四个对应的接入业务编码都是 1009901
				strRecType=RECCODE.HY_BGDJ.Value;//由于补换证登记没有规定上报的格式,定义成变更
			}
			if (DJLX.YGDJ.Value.equals(this.getDjlx().Value)){  // 预告登记
				strRecType=RECCODE.YG_ZXDJ.Value;
			}
		
			
		} 
		if (QLLX.GYJSYDSHYQ.Value.equals(this.getQllx().Value)||QLLX.JTJSYDSYQ.Value.equals(this.getQllx().Value)||QLLX.ZJDSYQ.Value.equals(this.getQllx().Value)) { // 建设用地使用权、宅基地使用权
			if (DJLX.CSDJ.Value.equals(this.getDjlx().Value)) { // 首次登记
				strRecType=RECCODE.JSYDSHYQ_CSDJ.Value;
			}
			if (DJLX.ZYDJ.Value.equals(this.getDjlx().Value)) { // 转移登记
				strRecType=RECCODE.JSYDSHYQ_ZYDJ.Value;
			}
			if (DJLX.BGDJ.Value.equals(this.getDjlx().Value)) { // 变更登记
				strRecType=RECCODE.JSYDSHYQ_BGDJ.Value;
			}
			if (DJLX.GZDJ.Value.equals(this.getDjlx().Value)) { // 更正登记
				strRecType=RECCODE.JSYDSHYQ_GZDJ.Value;
			}
			if (DJLX.ZXDJ.Value.equals(this.getDjlx().Value)){  // 注销登记
				strRecType=RECCODE.ZX_ZXDJ.Value;
			}
			if (DJLX.QTDJ.Value.equals(this.getDjlx().Value)){  // 补换证登记 或者登记类型为900的其他登记
				strRecType=RECCODE.QTQL_BGDJ.Value;  // QTQL_BGDJ ，QTQL_GZDJ ，QTQL_CSDJ ,QTQL_ZYDJ 这四个对应的接入业务编码都是 1009901
			}
			if (DJLX.YGDJ.Value.equals(this.getDjlx().Value)){  // 预告登记
				strRecType=RECCODE.YG_ZXDJ.Value;
			}
		}
		if (QLLX.GYJSYDSHYQ_FWSYQ.Value.equals(this.getQllx().Value) ||
				QLLX.JTJSYDSYQ_FWSYQ.Value.equals(this.getQllx().Value) ||
				QLLX.ZJDSYQ_FWSYQ.Value.equals(this.getQllx().Value) ) { // 房地产权
			if (DJLX.CSDJ.Value.equals(this.getDjlx().Value)) { // 首次登记
				strRecType=RECCODE.FDCQDZ_CSDJ.Value;
			}
			if (DJLX.ZYDJ.Value.equals(this.getDjlx().Value)) { // 转移登记
				strRecType=RECCODE.FDCQDZ_ZYDJ.Value;
			}
			if (DJLX.BGDJ.Value.equals(this.getDjlx().Value)) { // 变更登记
				strRecType=RECCODE.FDCQDZ_BGDJ.Value;
			}
			if (DJLX.GZDJ.Value.equals(this.getDjlx().Value)) { // 更正登记
				strRecType=RECCODE.FDCQDZ_GZDJ.Value;
			}
			if (DJLX.ZXDJ.Value.equals(this.getDjlx().Value)){  // 注销登记
				strRecType=RECCODE.ZX_ZXDJ.Value;
			}
			if (DJLX.QTDJ.Value.equals(this.getDjlx().Value)){  // 补换证登记 或者登记类型为900的其他登记
				strRecType=RECCODE.QTQL_BGDJ.Value;  // QTQL_BGDJ ，QTQL_GZDJ ，QTQL_CSDJ ,QTQL_ZYDJ 这四个对应的接入业务编码都是 1009901
			}
			if (DJLX.YGDJ.Value.equals(this.getDjlx().Value)){  // 预告登记
				strRecType=RECCODE.YG_ZXDJ.Value;
			}
		}
		if (QLLX.JTTDSYQ.Value.equals(this.getQllx().Value)) { // 土地所有权
			if (DJLX.CSDJ.Value.equals(this.getDjlx().Value)) { // 首次登记
				strRecType=RECCODE.TDSYQ_CSDJ.Value;
			}
			if (DJLX.ZYDJ.Value.equals(this.getDjlx().Value)) { // 转移登记
				strRecType=RECCODE.TDSYQ_ZYDJ.Value;
			}
			if (DJLX.BGDJ.Value.equals(this.getDjlx().Value)) { // 变更登记
				strRecType=RECCODE.TDSYQ_BGDJ.Value;
			}
			if (DJLX.GZDJ.Value.equals(this.getDjlx().Value)) { // 更正登记
				strRecType=RECCODE.TDSYQ_GZDJ.Value;
			}
			if (DJLX.ZXDJ.Value.equals(this.getDjlx().Value)){  // 注销登记
				strRecType=RECCODE.ZX_ZXDJ.Value;
			}
			if (DJLX.QTDJ.Value.equals(this.getDjlx().Value)){  // 补换证登记 或者登记类型为900的其他登记
				strRecType=RECCODE.QTQL_BGDJ.Value;  // QTQL_BGDJ ，QTQL_GZDJ ，QTQL_CSDJ ,QTQL_ZYDJ 这四个对应的接入业务编码都是 1009901
			}
			if (DJLX.YGDJ.Value.equals(this.getDjlx().Value)){  // 预告登记
				strRecType=RECCODE.YG_ZXDJ.Value;
			}
		}
		if (QLLX.HYSYQ.Value.equals(this.getQllx().Value)) { // 海域(含无居民海岛)使用权
			if (DJLX.CSDJ.Value.equals(this.getDjlx().Value)) { // 首次登记
				strRecType=RECCODE.HY_CSDJ.Value;
			}
			if (DJLX.ZYDJ.Value.equals(this.getDjlx().Value)) { // 转移登记
				strRecType=RECCODE.HY_ZYDJ.Value;
			}
			if (DJLX.BGDJ.Value.equals(this.getDjlx().Value)) { // 变更登记
				strRecType=RECCODE.HY_BGDJ.Value;
			}
			if (DJLX.GZDJ.Value.equals(this.getDjlx().Value)) { // 更正登记
				strRecType=RECCODE.HY_GZDJ.Value;
			}
			if (DJLX.ZXDJ.Value.equals(this.getDjlx().Value)){  // 注销登记
				strRecType=RECCODE.ZX_ZXDJ.Value;
			}
			if (DJLX.QTDJ.Value.equals(this.getDjlx().Value)){  // 补换证登记 或者登记类型为900的其他登记
				//strRecType=RECCODE.QTQL_BGDJ.Value;  // QTQL_BGDJ ，QTQL_GZDJ ，QTQL_CSDJ ,QTQL_ZYDJ 这四个对应的接入业务编码都是 1009901
				strRecType=RECCODE.HY_BGDJ.Value;//由于补换证登记没有规定上报的格式,定义成变更
			}
			if (DJLX.YGDJ.Value.equals(this.getDjlx().Value)){  // 预告登记
				strRecType=RECCODE.YG_ZXDJ.Value;
			}
		}
		if (QLLX.LDSYQ_SLLMSYQ.Value.equals(this.getQllx().Value)) { // 林权
			if (DJLX.CSDJ.Value.equals(this.getDjlx().Value)) { // 首次登记
				strRecType=RECCODE.LQ_CSDJ.Value;
			}
			if (DJLX.ZYDJ.Value.equals(this.getDjlx().Value)) { // 转移登记
				strRecType=RECCODE.LQ_ZYDJ.Value;
			}
			if (DJLX.BGDJ.Value.equals(this.getDjlx().Value)) { // 变更登记
				strRecType=RECCODE.LQ_BGDJ.Value;
			}
			if (DJLX.GZDJ.Value.equals(this.getDjlx().Value)) { // 更正登记
				strRecType=RECCODE.LQ_GZDJ.Value;
			}
			if (DJLX.ZXDJ.Value.equals(this.getDjlx().Value)){  // 注销登记
				strRecType=RECCODE.ZX_ZXDJ.Value;
			}
			if (DJLX.QTDJ.Value.equals(this.getDjlx().Value)){  // 补换证登记 或者登记类型为900的其他登记
				strRecType=RECCODE.QTQL_BGDJ.Value;  // QTQL_BGDJ ，QTQL_GZDJ ，QTQL_CSDJ ,QTQL_ZYDJ 这四个对应的接入业务编码都是 1009901
			}
			if (DJLX.YGDJ.Value.equals(this.getDjlx().Value)){  // 预告登记
				strRecType=RECCODE.YG_ZXDJ.Value;
			}
		}
		if (QLLX.DIYQ.Value.equals(this.getQllx().Value)) { // 抵押权
			RegisterWorkFlow flow = HandlerFactory.getWorkflow(this.project_id);
			boolean flag  = flow.getHandlername().toUpperCase().contains("YGDJHandler".toUpperCase()) || flow.getHandlername().toUpperCase().contains("YDYDJHandler".toUpperCase()) ;
			if(flag){
				strRecType=RECCODE.YG_ZXDJ.Value;
			}else{
				strRecType=RECCODE.DIYQ_ZXDJ.Value;
			}
			
			
		}
		if (QLLX.QTQL.Value.equals(this.getQllx().Value)) { // 查封
			strRecType=RECCODE.CF_CFDJ.Value;
		}
		return strRecType;
	}
	/**
	 * 导出XML交换文件
	 * @作者 diaoliwei
	 * @创建时间 2015年9月7日上午10:43:21
	 * @param path
	 * @return
	 */
	public Map<String, String> exportXML(String path, String actinstID) {
		Marshaller mashaller;
		if("LD".equals(this.bdcdylx.toString())
				||"HY".equals(this.bdcdylx.toString())){
			//海域,林地
			return exportXMLother(path, actinstID,"YES");
		}
		Map<String, String> names = new HashMap<String, String>();
		try {
			CommonDao dao = this.getCommonDao();
			String xmbhHql = ProjectHelper.GetXMBHCondition(this.getXMBH());
			List<BDCS_DJDY_GZ> djdys = dao.getDataList(BDCS_DJDY_GZ.class, xmbhHql);
			
			if (djdys != null && djdys.size() > 0) {
				mashaller = JAXBContext.newInstance(Message.class).createMarshaller();
				Calendar calendar = Calendar.getInstance(); // 创建一个日历对象
				calendar.setTime(new Date()); // 用当前时间初始化日历时间
				String cyear = calendar.get(Calendar.YEAR) + "";
				BDCS_XMXX xmxx = dao.get(BDCS_XMXX.class, this.getXMBH());
				String result = "";
				String msgName = "";
				for (int i = 0; i < djdys.size(); i++) {
					BDCS_DJDY_GZ djdy = djdys.get(i);

					String ywh = packageXml.GetYWLSHByYWH(this.getProject_id());
					BDCS_QL_GZ ql = this.getQL(djdy.getDJDYID());
					List<BDCS_QLR_GZ> qlrs = this.getQLRs(ql.getId());
					List<BDCS_SQR> sqrs = new ArrayList<BDCS_SQR>();
					ProjectServiceImpl serviceImpl = SuperSpringContext.getContext().getBean(ProjectServiceImpl.class);
					sqrs = serviceImpl.getSQRList(this.getXMBH());
					if (QLLX.GYJSYDSHYQ.Value.equals(this.getQllx().Value) || QLLX.ZJDSYQ.Value.equals(this.getQllx().Value) || QLLX.JTJSYDSYQ.Value.equals(this.getQllx().Value)) { // 国有建设使用权、宅基地使用权、集体建设用地使用权
						RealUnit unit=UnitTools.loadUnit(BDCDYLX.SHYQZD, DJDYLY.GZ, djdy.getBDCDYID());
						UseLand zd=(UseLand)unit;
						Message msg = exchangeFactory.createMessage(null);
						this.fillHead(msg, i, ywh,zd.getBDCDYH(),zd.getQXDM(),ql.getLYQLID());
						msg.getHead().setParcelID(StringHelper.formatObject(zd.getZDDM()));
						msg.getHead().setEstateNum(StringHelper.formatObject(zd.getBDCDYH()));
//						msg.getHead().setPreEstateNum(StringHelper.formatObject(zd.getBDCDYH()));
						if (zd != null && !StringUtils.isEmpty(zd.getQXDM())) {
							msg.getHead().setAreaCode(zd.getQXDM());
						}
						if (djdy != null) {
							this.fillShyqZdData(msg,ywh,zd,sqrs,qlrs,ql,xmxx,actinstID);//单独提出来个方法
						}
						
						msgName = getMessageFileName( msg,  i ,djdys.size(),null,null);
						mashaller.marshal(msg, new File(path + msgName));
						result = uploadFile(path + msgName, ConstValue.RECCODE.JSYDSHYQ_CSDJ.Value, actinstID, djdy.getDJDYID(), ql.getId());
						
					}
                    if ("NYD".equals(this.bdcdylx.toString())) {
                    	try {
                        	//农用地
                        	RealUnit unit=UnitTools.loadUnit(BDCDYLX.NYD, DJDYLY.GZ, djdy.getBDCDYID());
                        	if(unit==null) {
                        		unit=UnitTools.loadUnit(BDCDYLX.NYD, DJDYLY.XZ, djdy.getBDCDYID());
                        	}
                        	AgriculturalLand nyd=(AgriculturalLand)unit;
    						Message msg = exchangeFactory.createMessageByNYD();//创建信息承载器
    						//设置表头
    						this.fillHead(msg, i, ywh,nyd.getBDCDYH(),nyd.getQXDM(),ql.getLYQLID());
    						msg.getHead().setParcelID(StringHelper.formatObject(nyd.getZDDM()));
    						msg.getHead().setEstateNum(StringHelper.formatObject(nyd.getBDCDYH()));
//    						msg.getHead().setPreEstateNum(StringHelper.formatObject(nyd.getBDCDYH()));
    						//权证号
    						String  qzhzmh="";
    						for(BDCS_QLR_GZ qlr:qlrs) {
    							if(StringHelper.isEmpty(qlr.getBDCQZH().toString())) {
    								qzhzmh=qzhzmh+qlr.getBDCQZH().toString()+",";
    							}
    						}
//    						msg.getHead().setPreEstateNum(StringHelper.formatObject(qzhzmh));
    						if (nyd != null && !StringUtils.isEmpty(nyd.getQXDM())) {
    							msg.getHead().setAreaCode(nyd.getQXDM());
    						}
    						//填充内容
//    						1.权力人表
							List<ZTTGYQLR> zttqlr = msg.getData().getGYQLR();
							zttqlr = packageXml.getZTTGYQLRs(zttqlr, qlrs, null, ql, null, null, null, null);
							if (nyd != null) {
								//维护区县代码和不动产单元号
								for(ZTTGYQLR qlr:zttqlr) {
									qlr.setQXDM(nyd.getQXDM());
									qlr.setBDCDYH(nyd.getBDCDYH());
								}
							}
							msg.getData().setGYQLR(zttqlr);
//							KTT_ZDJBXX宗地基本信息
							KTTZDJBXX jbxx = msg.getData().getKTTZDJBXX();
							jbxx = packageXml.getZDJBXXByNYD(nyd, jbxx,ql);
							msg.getData().setKTTZDJBXX(jbxx);
//							宗地变化情况
							KTFZDBHQK bhqk = msg.getData().getZDBHQK();
				    		bhqk = packageXml.getKTFZDBHQKByNYD(bhqk, nyd, ql, null, null);
				    		msg.getData().setZDBHQK(bhqk);
				    		if (DJLX.CSDJ.Value.equals(this.getDjlx().Value)||DJLX.BGDJ.Value.equals(this.getDjlx().Value)) { 
				    			// 首次登记.变更登记取空间属性
					    		List<ZDK103> zdk = msg.getData().getZDK103();//宗地空间属性
					    		zdk = packageXml.getZDK103ByNYD(zdk, nyd, null, null);
					    		msg.getData().setZDK103(zdk);
							}
//							农用地使用权（非林地）
    						QLNYDSYQ nydql=msg.getData().getQlnydsyq();
    						nydql= packageXml.getQLNYDSYQ(nydql,nyd,ql,ywh);
							msg.getData().setQlnydsyq(nydql);
							// SLSQ 受理申请
							DJTDJSLSQ sq = msg.getData().getDJSLSQ();
							sq = packageXml.getDJTDJSLSQByNYD(sq, nyd, ql, xmxx);
							msg.getData().setDJSLSQ(sq);
							// SJ 收件
							List<DJFDJSJ> sj = msg.getData().getDJSJ();
							sj = packageXml.getDJFDJSJ(nyd, ywh,actinstID);
							for(DJFDJSJ d:sj) {
								d.setYSDM("6002020400");
							}
							msg.getData().setDJSJ(sj);
							 // SF 收费
							List<DJFDJSF> sfList = msg.getData().getDJSF();
							sfList = packageXml.getDJSF(nyd,ywh,this.getXMBH());
							for(DJFDJSF d :sfList ) {
								d.setYSDM("6002020400");
								d.setQXDM(StringHelper.formatObject(nyd.getQXDM()));
							}
							msg.getData().setDJSF(sfList);
							 // SH 审核
							List<DJFDJSH> sh = msg.getData().getDJSH();
							sh = packageXml.getDJFDJSH(nyd, ywh, this.getXMBH(), actinstID);
							for(DJFDJSH d:sh) {
								d.setYSDM("6002020400");
							} 
							msg.getData().setDJSH(sh);
					        // SZ 缮证 
							List<DJFDJSZ> sz = packageXml.getDJFDJSZ(nyd, ywh, this.getXMBH());
					        // FZ 发证
							List<DJFDJFZ> fz = packageXml.getDJFDJFZ(nyd, ywh, this.getXMBH());
							// GD 归档
							List<DJFDJGD> gd = packageXml.getDJFDJGD(nyd ,ywh,this.getXMBH());
							// SQR 申请人
							List<DJFDJSQR> djsqrs = msg.getData().getDJSQR();
							djsqrs = packageXml.getDJSQRs(djsqrs, sqrs, nyd.getYSDM(), ywh, nyd.getBDCDYH());
							if(nyd!=null) {
								for(DJFDJGD d:gd) {
									d.setYSDM("6002020400");
//									d.setQXDM(StringHelper.formatObject(nyd.getQXDM()));
									d.setZL(StringHelper.formatObject(nyd.getZL()));
								}
								for(DJFDJFZ d:fz) {
									d.setYSDM("6002020400");
//									d.setQXDM(StringHelper.formatObject(nyd.getQXDM()));
								}
								for(DJFDJSQR d:djsqrs) {
									d.setYsdm("6002020400");
								}
								
								for(DJFDJSZ d:sz) {
									d.setYSDM("6002020400");
//									d.setQXDM(StringHelper.formatObject(nyd.getQXDM()));
								}
							}
							msg.getData().setDJSQR(djsqrs);
							msg.getData().setDJSZ(sz);
							msg.getData().setDJFZ(fz);
							msg.getData().setDJGD(gd);
							// FJ 非结    （结构化文档）
							FJF100 fj = msg.getData().getFJF100();
							fj = packageXml.getFJF(fj);
							msg.getData().setFJF100(fj);
							msgName = getMessageFileName( msg,  i ,djdys.size(),names,djdy);
							mashaller.marshal(msg, new File(path +msgName));
							result = uploadFile(path +msgName, getRecType(), actinstID, djdy.getDJDYID(), ql.getId());
						} catch (Exception e) {
							e.printStackTrace();
						}
                    }
                    
                    
					if (QLLX.GYJSYDSHYQ_FWSYQ.Value.equals(this.getQllx().Value) ||
							QLLX.JTJSYDSYQ_FWSYQ.Value.equals(this.getQllx().Value)||
							QLLX.ZJDSYQ_FWSYQ.Value.equals(this.getQllx().Value)) { // 房屋所有权
						try {
							BDCS_H_GZ h = dao.get(BDCS_H_GZ.class, djdy.getBDCDYID());
							BDCS_ZRZ_GZ zrz_gz = null;
							if (h != null && !StringUtils.isEmpty(h.getZRZBDCDYID())) {
								zrz_gz = dao.get(BDCS_ZRZ_GZ.class, h.getZRZBDCDYID());
								if (zrz_gz != null) {
									zrz_gz.setGHYT(h.getGHYT()); // 自然幢的ghyt取户的ghyt
									zrz_gz.setFWJG(zrz_gz.getFWJG() == null || zrz_gz.getFWJG().equals("") ? h.getFWJG() : zrz_gz.getFWJG());
								}
							}
							BDCS_LJZ_GZ ljz_gz = null;
							if (h != null && !StringUtils.isEmpty(h.getLJZID())) {
								ljz_gz = dao.get(BDCS_LJZ_GZ.class, h.getLJZID());
							}
							if (h != null) {
								BDCS_SHYQZD_GZ zd = dao.get(BDCS_SHYQZD_GZ.class, h.getZDBDCDYID());
								if (zd != null) {
									h.setZDDM(zd.getZDDM());
								} else {
									BDCS_SHYQZD_XZ zd2 = dao.get(BDCS_SHYQZD_XZ.class, h.getZDBDCDYID());
									h.setZDDM(zd2.getZDDM());
								}
							}
							Message msg = exchangeFactory.createMessageByFWSYQ();
							this.fillHead(msg, i, ywh,h.getBDCDYH(),h.getQXDM(),ql.getLYQLID());
							msg.getHead().setParcelID(h.getZDDM());
							msg.getHead().setEstateNum(StringHelper.formatObject(h.getBDCDYH()));
//							msg.getHead().setPreEstateNum(StringHelper.formatObject(h.getBDCDYH()));
							if (h != null && !StringUtils.isEmpty(h.getQXDM())) {
								msg.getHead().setAreaCode(h.getQXDM());
							}
							BDCS_C_GZ c = null;
							if (h != null && !StringUtils.isEmpty(h.getCID())) {
								c = dao.get(BDCS_C_GZ.class, h.getCID());
							}
							if (djdy != null) {
								this.fillFwData(msg, ywh ,ljz_gz, c ,zrz_gz, h ,sqrs,qlrs,ql,xmxx,actinstID);
							}
						
							msgName = getMessageFileName( msg,  i ,djdys.size(),names,djdy);							
							mashaller.marshal(msg, new File(path + msgName));
							result = uploadFile(path + msgName, ConstValue.RECCODE.FDCQDZ_CSDJ.Value, actinstID, djdy.getDJDYID(), ql.getId());
							
						} catch (Exception e) {
							e.printStackTrace();
						}
					}

					if (QLLX.JTTDSYQ.Value.equals(this.getQllx().Value)) { // 集体土地所有权
						try {
							BDCS_SYQZD_GZ oland = dao.get(BDCS_SYQZD_GZ.class, djdy.getBDCDYID());
							Message msg = exchangeFactory.createMessageByTDSYQ();
							this.fillHead(msg, i, ywh,oland.getBDCDYH(),oland.getQXDM(),ql.getLYQLID());
							msg.getHead().setParcelID(StringHelper.formatObject(oland.getZDDM()));
							msg.getHead().setEstateNum(StringHelper.formatObject(oland.getBDCDYH()));
//							msg.getHead().setPreEstateNum(StringHelper.formatObject(oland.getBDCDYH()));
							if (oland != null && !StringUtils.isEmpty(oland.getQXDM())) {
								msg.getHead().setAreaCode(oland.getQXDM());
							}
							if (djdy != null) {
								 this.fillSyqZdData( msg, ywh, oland, sqrs, qlrs, ql, xmxx, actinstID) ;
							}
							
							msgName = getMessageFileName( msg,  i ,djdys.size(),names,djdy);
							mashaller.marshal(msg, new File(path + msgName));
							result = uploadFile(path + msgName, ConstValue.RECCODE.TDSYQ_CSDJ.Value, actinstID, djdy.getDJDYID(), ql.getId());
							
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					if (result.equals("")|| result==null) {
						Map<String, String> xmlError = new HashMap<String, String>();
						xmlError.put("error", "连接SFTP失败,请检查服务器和前置机的连接是否正常");
						if (QLLX.JTTDSYQ.Value.equals(this.getQllx().Value)) {
							YwLogUtil.addSjsbResult(msgName, "", "连接SFTP失败,请检查服务器和前置机的连接是否正常", ConstValue.SF.NO.Value, ConstValue.RECCODE.TDSYQ_CSDJ.Value,
									ProjectHelper.getpRroinstIDByActinstID(actinstID));
						} else if (QLLX.GYJSYDSHYQ_FWSYQ.Value.equals(this.getQllx().Value)) {
							YwLogUtil.addSjsbResult(msgName, "", "连接SFTP失败,请检查服务器和前置机的连接是否正常", ConstValue.SF.NO.Value, ConstValue.RECCODE.FDCQDZ_CSDJ.Value,
									ProjectHelper.getpRroinstIDByActinstID(actinstID));
						} else {
							YwLogUtil.addSjsbResult(msgName, "", "连接SFTP失败,请检查服务器和前置机的连接是否正常", ConstValue.SF.NO.Value, ConstValue.RECCODE.JSYDSHYQ_CSDJ.Value,
									ProjectHelper.getpRroinstIDByActinstID(actinstID));
						}
						return xmlError;
					}
					if (!"1".equals(result) && result.indexOf("success") == -1) { // xml本地校验不通过时
						Map<String, String> xmlError = new HashMap<String, String>();
						xmlError.put("error", result);
						return xmlError;
					}
					if (!StringUtils.isEmpty(result) && result.indexOf("success") > -1 && !names.containsKey("reccode")) {
						names.put("reccode", result);
					}
				}
			}
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return names;
	}



	/**
	 * 林权、海域
	 * @作者 diaoliwei
	 * @创建时间 2015年9月22日下午8:25:52
	 * @param path
	 * @return
	 */
	@SuppressWarnings("unused")
	public Map<String, String> exportXMLother(String path, String actinstID,String sfsykj) {
		Marshaller mashaller;
		Map<String, String> names = new HashMap<String, String>();
		try {
			CommonDao dao = this.getCommonDao();
			String xmbhHql = ProjectHelper.GetXMBHCondition(this.getXMBH());
			List<BDCS_DJDY_GZ> djdys = dao.getDataList(BDCS_DJDY_GZ.class, xmbhHql);
			if (djdys != null && djdys.size() > 0) {
				mashaller = JAXBContext.newInstance(Message.class).createMarshaller();
				Calendar calendar = Calendar.getInstance(); // 创建一个日历对象
				calendar.setTime(new Date()); // 用当前时间初始化日历时间
				String cyear = calendar.get(Calendar.YEAR) + "";
				BDCS_XMXX xmxx = dao.get(BDCS_XMXX.class, this.getXMBH());
				String result = "";
				for (int i = 0; i < djdys.size(); i++) {
					BDCS_DJDY_GZ djdy = djdys.get(i);
					String ywh = packageXml.GetYWLSHByYWH(this.getProject_id());
					BDCS_QL_GZ ql = this.getQL(djdy.getDJDYID());
					if(ql==null) {
						continue;
					}
					List<BDCS_QLR_GZ> qlrs = this.getQLRs(ql.getId());
					List<BDCS_SQR> sqrs = new ArrayList<BDCS_SQR>();
					if (null != qlrs) {
						sqrs = this.getSQRs(qlrs);
					}
					if (QLLX.LDSYQ_SLLMSYQ.Value.equals(this.getQllx().Value) || QLLX.LDSYQ.Value.equals(this.getQllx().Value)
							|| QLLX.LDSHYQ_SLLMSYQ.Value.equals(this.getQllx().Value)) { 
						// 森林、林木的所有权（或使用权）、林地的使用权
						BDCS_SLLM_GZ sllm = dao.get(BDCS_SLLM_GZ.class, djdy.getBDCDYID());
						Message msg = exchangeFactory.createMessageByLQ();
						this.fillHead(msg, i, ywh,sllm.getBDCDYH(),sllm.getQXDM(),ql.getLYQLID());
						msg.getHead().setParcelID(StringHelper.formatObject(sllm.getZDDM()));
						msg.getHead().setEstateNum(StringHelper.formatObject(sllm.getBDCDYH()));
//						msg.getHead().setPreEstateNum(StringHelper.formatObject(sllm.getBDCDYH()));
						if (sllm != null && !StringUtils.isEmpty(sllm.getQXDM())) {
							msg.getHead().setAreaCode(sllm.getQXDM());
						}
						if (djdy != null) {
							KTTZDJBXX jbxx = msg.getData().getKTTZDJBXX();
							jbxx = packageXml.getZDJBXX(jbxx, null, ql, null, sllm);

							FJF100 fj = msg.getData().getFJF100();
							fj = packageXml.getFJF(fj);

							KTFZDBHQK bhqk = msg.getData().getZDBHQK();
							bhqk = packageXml.getKTFZDBHQK(bhqk, null, ql, null, sllm);
							msg.getData().setZDBHQK(bhqk);

							QLTQLLQ lq = msg.getData().getQLTQLLQ();
							lq = packageXml.getQLTQLLQ(lq, ql, sllm, ywh);

							List<ZTTGYQLR> zttqlr = msg.getData().getGYQLR();
							zttqlr = packageXml.getZTTGYQLRs(zttqlr, qlrs, null, ql, null, null, sllm, null);
							msg.getData().setGYQLR(zttqlr);

							DJTDJSLSQ sq = msg.getData().getDJSLSQ();
							sq = packageXml.getDJTDJSLSQ(sq, ywh, null, ql, xmxx, null, xmxx.getSLSJ(), null, sllm, null);

							List<DJFDJSJ> sj = msg.getData().getDJSJ();
							sj = packageXml.getDJFDJSJ(sllm, ywh,actinstID);
							msg.getData().setDJSJ(sj);

							List<DJFDJSF> sfList = msg.getData().getDJSF();
							sfList = packageXml.getDJSF(sllm, ywh, this.getXMBH());
							msg.getData().setDJSF(sfList);

							List<DJFDJSH> sh = msg.getData().getDJSH();
							sh = packageXml.getDJFDJSH(sllm, ywh, this.getXMBH(), actinstID);
							msg.getData().setDJSH(sh);

							List<DJFDJSZ> sz = packageXml.getDJFDJSZ(sllm, ywh, this.getXMBH());
							msg.getData().setDJSZ(sz);

							List<DJFDJFZ> fz = packageXml.getDJFDJFZ(sllm,ywh, this.getXMBH());
							msg.getData().setDJFZ(fz);
							List<DJFDJGD> gd = packageXml.getDJFDJGD(sllm, ywh,this.getXMBH());
							msg.getData().setDJGD(gd);
							if (sfsykj!=null&&sfsykj.equals("YES")) {
								List<ZDK103> zdk = msg.getData().getZDK103();
								zdk = packageXml.getZDK103(zdk, null, null, sllm);
							}
							List<DJFDJSQR> djsqrs = msg.getData().getDJSQR();
							djsqrs = packageXml.getDJSQRs(djsqrs, sqrs, sllm.getYSDM(), ywh, sllm.getBDCDYH());
							msg.getData().setDJSQR(djsqrs);
						}
						
						String msgName = getMessageFileName( msg,  i ,djdys.size(),names,djdy);
						mashaller.marshal(msg, new File(path +msgName));
						result = uploadFile(path + msgName, ConstValue.RECCODE.LQ_CSDJ.Value, actinstID, djdy.getDJDYID(), ql.getId());
					}
//                    String s=this.getQllx().Value;//测试,无意义
					if ("HY".equals(this.getBdcdylx().toString())&&ql!=null) { // 海域(含无居民海岛)
						String zhdm=null;
						String hql=null;
						YHZK yhzk_gz = null;
						Sea zh = dao.get(BDCS_ZH_GZ.class, djdy.getBDCDYID());
						if (zh==null) {
							zh=dao.get(BDCS_ZH_XZ.class, djdy.getBDCDYID());
						}
						String lcbh=ProjectHelper.getWorkflowCodeByProjectID(xmxx.getPROJECT_ID());
						String fulSql="SELECT  T.WORKFLOWNAME  FROM BDC_WORKFLOW.WFD_MAPPING  T WHERE T.WORKFLOWCODE='"+lcbh+"'";
					  List<Map> lstmap=	dao.getDataListByFullSql(fulSql);
					  String workflowname="";
						if (lstmap != null && lstmap.size() > 0) {
							workflowname = StringHelper.formatObject(lstmap.get(0).get("WORKFLOWNAME"));
						}
						if(!StringHelper.isEmpty(workflowname)&&workflowname.equals("BG036")) {
							zh = dao.get(BDCS_ZH_GZ.class, ql.getBDCDYID());
						}
						if (null != zh) {
						 zhdm=zh.getZHDM();
						 hql = "BDCDYID = '" + zh.getId() + "' ";
							List<BDCS_YHZK_GZ> yhzks = dao.getDataList(BDCS_YHZK_GZ.class, hql);
							if (yhzks != null && yhzks.size() > 0) {
								yhzk_gz = yhzks.get(0);
							}else {
								List<BDCS_YHZK_XZ> yhzksxz = dao.getDataList(BDCS_YHZK_XZ.class, hql);
								if (yhzksxz != null && yhzksxz.size() > 0) {
									yhzk_gz = yhzksxz.get(0);
								}
							}
						}
						if(zhdm!=null&&zhdm.length()>0&&(yhzk_gz.getZHDM()==null)||yhzk_gz.getZHDM().length()>0) {
							//维护数据
							yhzk_gz.setZHDM(zhdm);
						}
						// 这些字段先手动赋值 diaoliwei
						if (zh != null) {
							if (StringUtils.isEmpty(zh.getZHT())) {
								zh.setZHT("无");
							}
						}
							Message msg = exchangeFactory.createMessageByHY();
							//设置报文头
							this.fillHead(msg, i, ywh,zh.getBDCDYH(),zh.getQXDM(),ql.getLYQLID());
							msg.getHead().setParcelID(StringHelper.formatObject(zh.getZHDM()));
							msg.getHead().setEstateNum(StringHelper.formatObject(zh.getBDCDYH()));
//							msg.getHead().setPreEstateNum(StringHelper.formatObject(zh.getBDCDYH()));
							//设置报文信息
							//1.宗海基本信息系
							KTTZHJBXX jbxx = msg.getData().getKTTZHJBXX();
							jbxx = packageXml.getKTTZHJBXX(jbxx, ql, zh);//组装成xml格式
							msg.getData().setKTTZHJBXX(jbxx);
							
							//2.宗海变化状况
							KTFZHBHQK bhqk = msg.getData().getKTFZHBHQK();
							bhqk = packageXml.getKTFZHBHQK(bhqk, ql, zh);
							msg.getData().setKTFZHBHQK(bhqk);
							
							
							//3.非结构化文档
							FJF100 fj = msg.getData().getFJF100();
							fj = packageXml.getFJF(fj);
							msg.getData().setFJF100(fj);
							
							//5.宗海变化状况表(可选 )
							KTFZHYHZK yhzk = msg.getData().getKTFZHYHZK();
							yhzk = packageXml.getKTFZHYHZK(yhzk, yhzk_gz, zh.getBDCDYH());
							msg.getData().setKTFZHYHZK(yhzk);

							//6.用海,用岛坐标
							KTFZHYHYDZB zb = msg.getData().getKTFZHYHYDZB();
							zb = packageXml.getKTFZHYHYDZB(zb, zh);
							msg.getData().setKTFZHYHYDZB(zb);

							//6.权力人表
							List<ZTTGYQLR> zttqlr = msg.getData().getGYQLR();
							zttqlr = packageXml.getZTTGYQLRs(zttqlr, qlrs, null, ql, null, null, null, zh);
							msg.getData().setGYQLR(zttqlr);

							//7.登记受理信息表
							DJTDJSLSQ sq = msg.getData().getDJSLSQ();
							sq = packageXml.getDJTDJSLSQ(sq, ywh, null, ql, xmxx, null, xmxx.getSLSJ(), null, null, zh);
							msg.getData().setDJSLSQ(sq);

							//8.登记收件(可选)
							List<DJFDJSJ> sj = msg.getData().getDJSJ();
							sj = packageXml.getDJFDJSJ(zh, ywh,actinstID);
							msg.getData().setDJSJ(sj);

							//9.登记收费(可选)
							List<DJFDJSF> sfList = msg.getData().getDJSF();
							sfList = packageXml.getDJSF(zh, ywh,this.getXMBH());
							msg.getData().setDJSF(sfList);

							//10.审核(可选)
							List<DJFDJSH> sh = msg.getData().getDJSH();
							sh = packageXml.getDJFDJSH(zh, ywh, this.getXMBH(), actinstID);
							msg.getData().setDJSH(sh);

							//11.缮证(可选)
							List<DJFDJSZ> sz = packageXml.getDJFDJSZ(zh, ywh, this.getXMBH());
							msg.getData().setDJSZ(sz);

							//11.发证(可选)
							List<DJFDJFZ> fz = packageXml.getDJFDJFZ(zh,ywh, this.getXMBH());
							msg.getData().setDJFZ(fz);
							
							//12.归档(可选)
							List<DJFDJGD> gd = packageXml.getDJFDJGD(zh, ywh,this.getXMBH());
							msg.getData().setDJGD(gd);
							
							//4.宗海使用权
							QLFQLHYSYQ syq = msg.getData().getQLFQLHYSYQ();
							syq = packageXml.getQLFQLHYSYQ(syq, ql, zh, ywh);
							msg.getData().setQLFQLHYSYQ(syq);

							if (sfsykj!=null&&sfsykj.equals("YES")) {
								//13.空间属性
								List<ZHK105> zhk = msg.getData().getZHK105();
								zhk = packageXml.getZHK105(zhk, zh);
								msg.getData().setZHK105(zhk);
							}
							List<DJFDJSQR> djsqrs = msg.getData().getDJSQR();
							djsqrs = packageXml.getDJSQRs(djsqrs, sqrs, zh.getYSDM(), ywh, zh.getBDCDYH());
							msg.getData().setDJSQR(djsqrs);
						String msgName = getMessageFileName( msg,  i ,djdys.size(),names,djdy);
						mashaller.marshal(msg, new File(path +msgName));
						result = uploadFile(path +msgName, ConstValue.RECCODE.HY_CSDJ.Value, actinstID, djdy.getDJDYID(), ql.getId());
						
					}
					if (null == result) {
						Map<String, String> xmlError = new HashMap<String, String>();
						xmlError.put("error", "连接SFTP失败,请检查服务器和前置机的连接是否正常");
						YwLogUtil.addSjsbResult("", "", "连接SFTP失败,请检查服务器和前置机的连接是否正常", ConstValue.SF.NO.Value, ConstValue.RECCODE.HY_CSDJ.Value,
								ProjectHelper.getpRroinstIDByActinstID(actinstID));
						return xmlError;
					}
					if (!"1".equals(result) && result.indexOf("success") == -1) { // xml本地校验不通过时
						Map<String, String> xmlError = new HashMap<String, String>();
						xmlError.put("error", result);
						return xmlError;
					}
					if (!StringUtils.isEmpty(result) && result.indexOf("success") > -1 && !names.containsKey("reccode")) {
						names.put("reccode", result);
					}
				}
			}
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return names;
	}

	/**
	 * 上传xml文件到接入系统的sftp上
	 * @author diaoliwei 2016.4.13刘树峰改造
	 * @param localFilePath
	 *            本地xml文件地址
	 */
	public String uploadFile(String localFilePath, String reccode, String actinstID, String djdykey, String qlid) {
		List<BDCS_REPORTINFO> list_report=baseCommonDao.getDataList(BDCS_REPORTINFO.class, " QLID='"+qlid+"'");
		if(list_report!=null&&list_report.size()>0){
			boolean bHaveReport=false;
			String BizMsgIDHaveReport="";
			for(BDCS_REPORTINFO reportinfo_pre:list_report){
				if("1".equals(reportinfo_pre.getSUCCESSFLAG())||"2".equals(reportinfo_pre.getSUCCESSFLAG())){
					bHaveReport=true;
					BizMsgIDHaveReport=reportinfo_pre.getBIZMSGID();
					break;
				}
			}
			if(bHaveReport){
				return "0|报文"+BizMsgIDHaveReport+"已经上报！不能重复上报！";
			}else{
				for(BDCS_REPORTINFO reportinfo_pre:list_report){
					reportinfo_pre.setYXBZ("0");
					baseCommonDao.update(reportinfo_pre);
				}
			}
		}
		///数据上报方式
		String UoloadDestination = ConfigHelper.getNameByValue("UoloadDestination");
		boolean transferred=false;
		if("4".equals(UoloadDestination)){
			transferred=true;
		}
		String xzqhdm=ConfigHelper.getNameByValue("XZQHDM");
		if("411623".equals(xzqhdm)){
			ReportInfoAdjust(localFilePath,djdykey);
		}
		if("460000".equals(xzqhdm)||xzqhdm.startsWith("46")){
			ReportInfoAdjust460000(localFilePath,qlid);
		}
		// xml文件进行加签
		String xmlstr=RsaXmlUtil.rsaXmlFile(localFilePath,transferred);
		// 本地先进行校验
		String CheckXmlInfo = "";
		try {
			CheckXmlInfo = ValidataXML.validateXMLByXSD(reccode, localFilePath);
		} catch (Exception e1) {
		}
		///上报报文标识
		String bizmsgid="";
		File reportxml=new File(localFilePath);
		if(reportxml!=null){
			String fileName=reportxml.getName();  
			bizmsgid=fileName.substring(3, fileName.length()-4);
		}
		///本地校验
		boolean bCheckXml=false;
		String reportcheck=ConfigHelper.getNameByValue("ReportCheck");
		if(!"1".equals(reportcheck)||"1".equals(CheckXmlInfo)){
			bCheckXml=true;
		}
		///活动实例信息
		Wfi_ActInst actinst=baseCommonDao.get(Wfi_ActInst.class, actinstID);
		///用户信息
		String username=Global.getCurrentUserName();
		///数据上报信息
		BDCS_REPORTINFO reportinfo=new BDCS_REPORTINFO();
		reportinfo.setREPORTTIME(new Date());
		reportinfo.setREPORTUSER(username);
		reportinfo.setBIZMSGID(bizmsgid);
		reportinfo.setDJDYID(djdykey);
		reportinfo.setQLID(qlid);
		reportinfo.setPROINSTID(actinst.getProinst_Id());
		reportinfo.setXMBH(this.getXMBH());
		reportinfo.setRECTYPE(reccode);
		reportinfo.setREPORTCONTENT(xmlstr);
		if(bCheckXml){
			reportinfo.setLOCALCHECK("1");
		}else{
			reportinfo.setLOCALCHECK("0");
		}
		reportinfo.setLOCALCHECKINFO(CheckXmlInfo);
		reportinfo.setREPORTTYPE(UoloadDestination);
		reportinfo.setYXBZ("1");
		reportinfo.setRESCOUNT(0);
		reportinfo.setSUCCESSFLAG("2");//0：上报失败；1、上报成功；2、上报成功，待响应;3、上报部里成功，上报省厅失败
		if(!bCheckXml){
			reportinfo.setSUCCESSFLAG("0");
			baseCommonDao.save(reportinfo);
			return CheckXmlInfo;
		}
		
		///上报数据
		String uploadresult="";
		if("1".equals(UoloadDestination)){
			reportinfo.setREPORTTYPE("1");
			uploadresult= uploadToInspurJointSystem(localFilePath, reccode, actinstID);
			if(!"1".equals(uploadresult)){
				reportinfo.setSUCCESSFLAG("0");
				reportinfo.setRESPONSEINFO(uploadresult);
			}else{
				reportinfo.setSUCCESSFLAG("2");
			}
			baseCommonDao.save(reportinfo);
		}else if ("4".equals(UoloadDestination)) {//WSL服务上报
			reportinfo.setREPORTTYPE("4");
			String result_wsdl= uploadToSupermapJointSystemByWSDL(xmlstr);
			String responsecode="";
			String responseinfo="上报失败！";
			String successflag="0";
			String certid="";
			String qrcode="";
			if("411623".equals(xzqhdm)){
				String resFilePath=localFilePath.replaceAll("Biz"+bizmsgid, "Res"+bizmsgid);
				result_wsdl=RsaXmlUtil.formatXml(resFilePath, result_wsdl);
			}
			if(StringHelper.isEmpty(result_wsdl)||StringHelper.isEmpty(result_wsdl.trim())){
				uploadresult="上报失败！";
			}else{
				Map resultmap=null;
				try {
					resultmap = XmlUtil.xml2map(result_wsdl, false);
				} catch (Exception e) {
				}
				if(resultmap!=null&&resultmap.containsKey("SuccessFlag")){
					String r_successflag=StringHelper.formatObject(resultmap.get("SuccessFlag"));
					if("1".equals(r_successflag)){
						successflag="1";
					}
					if(resultmap.containsKey("ResponseCode")){
						responsecode=StringHelper.formatObject(resultmap.get("ResponseCode"));
					}
					if(resultmap.containsKey("ResponseInfo")){
						responseinfo=StringHelper.formatObject(resultmap.get("ResponseInfo"));
					}
					if(resultmap.containsKey("CertID")){
						certid=StringHelper.formatObject(resultmap.get("CertID"));
					}
					if(resultmap.containsKey("QRCode")){
						qrcode=StringHelper.formatObject(resultmap.get("QRCode"));
					}
				}
				if("1".equals(successflag)){
					uploadresult="1";
				}else{
					if(StringHelper.isEmpty(responseinfo)){
						uploadresult="上报失败";
					}else{
						uploadresult=responseinfo;
					}
				}
			}
			reportinfo.setSUCCESSFLAG(successflag);
			reportinfo.setRESPONSECODE(responsecode);
			reportinfo.setRESPONSEINFO(responseinfo);
			reportinfo.setRESPENSECONTENT(result_wsdl);
			reportinfo.setCERTID(certid);
			reportinfo.setQRCODE(qrcode);
			baseCommonDao.save(reportinfo);
		}else if ("2".equals(UoloadDestination)) {//上报省厅介入系统
			reportinfo.setREPORTTYPE("2");
			String result_super= uploadToSupermapJointSystem(xmlstr);
			JSONObject json_super = null;
			try{
				json_super=JSONObject.fromObject(result_super);
			}catch(Exception e){
				e.printStackTrace();
			}
			String status="false";
			String code="-1";
			String message="上报失败！";
			if(json_super!=null){
				if(json_super.containsKey("status")){
					status=StringHelper.formatObject(json_super.get("status"));
				}
				if(json_super.containsKey("code")){
					code=StringHelper.formatObject(json_super.get("code"));
				}
				if(json_super.containsKey("message")){
					message=StringHelper.formatObject(json_super.get("message"));
				}
			}
			reportinfo.setREPORTTYPE(UoloadDestination);//上报超图省厅介入系统
			reportinfo.setRESPENSECONTENT(result_super);
			reportinfo.setRESPONSECODE(code);
			reportinfo.setRESPONSEINFO(message);
			
			if("true".equals(status)&&"2".equals(code)){
				reportinfo.setSUCCESSFLAG("1");
				uploadresult="1";
			}else if("true".equals(status)&&"1".equals(code)){
				reportinfo.setSUCCESSFLAG("2");
				uploadresult="1";
			}else{
				reportinfo.setSUCCESSFLAG("0");
				if(!StringHelper.isEmpty(message)){
					uploadresult=message;
				}else{
					uploadresult="上报失败！";
				}
			}
			baseCommonDao.save(reportinfo);
		}else if ("3".equals(UoloadDestination)) {//先上报部里，上报部里成功后上报省厅
			reportinfo.setREPORTTYPE("31");
			uploadresult= uploadToInspurJointSystem(localFilePath, reccode, actinstID);
			if(!"1".equals(uploadresult)){
				reportinfo.setSUCCESSFLAG("0");
			}else{
				reportinfo.setSUCCESSFLAG("2");
			}
			baseCommonDao.save(reportinfo);
		}
		return uploadresult;
	}
	/*
	 * 河南数据上报调整
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void ReportInfoAdjust(String reportXmlPath,String djdyid){
		try {
			Document document = Dom4jXmlUtil.readXmlContent(reportXmlPath);
			Element root_ele=document.getRootElement();
			List root_childeles = root_ele.elements();
			Iterator root_childIt = root_childeles.iterator();
			while (root_childIt.hasNext()) {
				Element first_ele = (Element) root_childIt.next();
				if("Head".equals(first_ele.getName())){
					//接入标准调整
					Element asid_ele = first_ele.element("ASID");
					if(asid_ele!=null){
						asid_ele.setText("ST100");
					}
					System.out.println(this.getQllx().Value);
					if(QLLX.DIYQ.equals(this.getQllx())){
						System.out.println("RegType");
						Element djlx_ele = first_ele.element("RegType");
						if(djlx_ele!=null){
							System.out.println("RegType is exists");
							djlx_ele.setText("900");
						}
					}
				}
				if("Data".equals(first_ele.getName())){
					//添加逻辑幢名称属性
					Element ljz_ele = first_ele.element("KTT_FW_LJZ");
					if(ljz_ele!=null){
						Attribute attr = ljz_ele.attribute("LJZH");
						if(attr!=null){
							ljz_ele.addAttribute("X_LJZMC", attr.getText()); 
						}else{
							ljz_ele.addAttribute("X_LJZMC", "无"); 
						}
					}
					//添加逻辑幢名称属性
					Element h_ele = first_ele.element("KTT_FW_H");
					if(h_ele!=null){
						h_ele.addAttribute("X_DYH", "");
					}
					if(BDCDYLX.H.equals(this.getBdcdylx())||BDCDYLX.YCH.equals(this.getBdcdylx())){
						//自然幢图形节点处理
						List<Element> zrz_eles = first_ele.elements("ZD_K_103");
						if(zrz_eles!=null&&zrz_eles.size()>0){
							for(Element zrz_ele:zrz_eles){
								zrz_ele.setName("ZRZ_K_101");
							}
							//有自然幢空间信息的添加宗地空间信息
							List<BDCS_DJDY_GZ> djdys=baseCommonDao.getDataList(BDCS_DJDY_GZ.class, "DJDYID='"+djdyid+"'");
							if(djdys!=null&&djdys.size()>0){
								RealUnit unit_h=UnitTools.loadUnit(this.getBdcdylx(), DJDYLY.initFrom(djdys.get(0).getLY()), djdys.get(0).getBDCDYID());
								if(unit_h!=null){
									House h=(House)unit_h;
									String zdbdcdyid=h.getZDBDCDYID();
									if(!StringHelper.isEmpty(zdbdcdyid)){
										RealUnit unit_zd=UnitTools.loadUnit(BDCDYLX.SHYQZD,DJDYLY.XZ,zdbdcdyid);
										if(unit_zd==null){
											unit_zd=UnitTools.loadUnit(BDCDYLX.SHYQZD,DJDYLY.GZ,zdbdcdyid);
										}
										if(unit_zd!=null){
											List<ZDK103> list_zdk=new ArrayList<ZDK103>();
											list_zdk=packageXml.getZDK(list_zdk, unit_zd);
											if(list_zdk!=null&&list_zdk.size()>0){
												for(ZDK103 zdk:list_zdk){
													Element zdk_ele=first_ele.addElement("ZD_K_103");
													zdk_ele.addAttribute("BDCDYH", zdk.getBDCDYH());
													zdk_ele.addAttribute("XH", StringHelper.formatObject(zdk.getXH()));
													zdk_ele.addAttribute("ZDX", StringHelper.formatObject(zdk.getZDX()));
													zdk_ele.addAttribute("XZB", zdk.getXZB());
													zdk_ele.addAttribute("YZB", zdk.getYZB());
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
			
			Dom4jXmlUtil.OutputXmlFile(document, reportXmlPath);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * 海南数据上报调整
	 */
	@SuppressWarnings({ "rawtypes"})
	private void ReportInfoAdjust460000(String reportXmlPath,String qlid){
		Rights ql=RightsTools.loadRights(DJDYLY.GZ, qlid);
		String djdyid=ql.getDJDYID();
		String xzqhdm=ConfigHelper.getNameByValue("XZQHDM")+"000";
		List<BDCS_DJDY_GZ> djdys=baseCommonDao.getDataList(BDCS_DJDY_GZ.class, "XMBH='"+this.getXMBH()+"' AND DJDYID='"+djdyid+"'");
		if(djdys!=null&&djdys.size()>0){
			RealUnit unit=UnitTools.loadUnit(BDCDYLX.initFrom(djdys.get(0).getBDCDYLX()), DJDYLY.initFrom(djdys.get(0).getLY()), djdys.get(0).getBDCDYID());
			if(unit!=null){
				if(!StringHelper.isEmpty(unit.getBDCDYH())&&unit.getBDCDYH().length()>=9){
					xzqhdm=unit.getBDCDYH().substring(0, 9);
				}else{
					String djzqdm=StringHelper.formatObject(getFieldValueByName("DJZQDM",unit));
					if(!StringHelper.isEmpty(djzqdm)&&djzqdm.length()>=9){
						xzqhdm=djzqdm.substring(0, 9);
					}else{
						String djqdm=StringHelper.formatObject(getFieldValueByName("DJQDM",unit));
						if(!StringHelper.isEmpty(djqdm)&&djqdm.length()>=9){
							xzqhdm=djqdm.substring(0, 9);
						}else{
							String qxdm=StringHelper.formatObject(getFieldValueByName("QXDM",unit));
							if(!StringHelper.isEmpty(qxdm)&&qxdm.length()>=6){
								xzqhdm=qxdm.substring(0, 6)+"000";
							}
						}
					}
				}
			}
		}
		BDCS_XMXX xmxx=Global.getXMXXbyXMBH(this.getXMBH());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
		String date = sdf.format(xmxx.getSLSJ());
		List<ProcedureParam> params=new ArrayList<ProcedureParam>();
		ProcedureParam param1=new ProcedureParam();
		param1.setFieldtype(Types.NVARCHAR);
		param1.setParamtype("in");
		param1.setName("sLSJ");
		param1.setValue(date);
		param1.setSxh(1);
		params.add(param1);
		
		ProcedureParam param2=new ProcedureParam();
		param2.setFieldtype(Types.NVARCHAR);
		param2.setParamtype("in");
		param2.setName("xZQHDM");
		param2.setSxh(2);
		param2.setValue(xzqhdm);
		params.add(param2);
		
		ProcedureParam param3=new ProcedureParam();
		param3.setFieldtype(Types.INTEGER);
		param3.setParamtype("out");
		param3.setName("sXH");
		param3.setSxh(3);
		params.add(param3);
		HashMap<String,Object> info=ProcedureTools.executeProcedure(params, "GETSJSBYWH", "BDCK");
		String lsh=StringHelper.formatObject(info.get("sXH"));
		
		String sjsbywh = xzqhdm + date + StringHelper.PadLeft(lsh, 7, '0');
		try {
			Document document = Dom4jXmlUtil.readXmlContent(reportXmlPath);
			Element root_ele=document.getRootElement();
			List root_childeles = root_ele.elements();
			Iterator root_childIt = root_childeles.iterator();
			while (root_childIt.hasNext()) {
				Element first_ele = (Element) root_childIt.next();
				
				if("Head".equals(first_ele.getName())){
					//接入标准调整
					Element asid_ele = first_ele.element("AreaCode");
					if(asid_ele!=null){
						asid_ele.setText("460000");
					}
				}
				if("Data".equals(first_ele.getName())){
					//申请人属性信息
					Element sqr_ele = first_ele.element("DJF_DJ_SQR");
					if(sqr_ele!=null){
						sqr_ele.setName("BDC_GZ_SQR");
						sqr_ele.addAttribute("SYWH", sjsbywh); 
					}
					//登记归档信息
					Element gd_ele = first_ele.element("DJF_DJ_GD");
					if(gd_ele!=null){
						gd_ele.setName("BDC_GZ_GD");
						gd_ele.addAttribute("SYWH", sjsbywh); 
					}
					
					//登记发证信息
					Element fz_ele = first_ele.element("DJF_DJ_GD");
					if(fz_ele!=null){
						fz_ele.setName("DJF_DJ_FZ");
						fz_ele.addAttribute("SYWH", sjsbywh); 
					}
					//登记缮证信息
					Element sz_ele = first_ele.element("DJF_DJ_SZ");
					if(sz_ele!=null){
						sz_ele.setName("BDC_GZ_SZ");
						sz_ele.addAttribute("SYWH", sjsbywh); 
					}
					//登记审核信息
					Element sh_ele = first_ele.element("DJF_DJ_SH");
					if(sh_ele!=null){
						sh_ele.setName("BDC_GZ_SH");
						sh_ele.addAttribute("SYWH", sjsbywh); 
					}
					//登记收费信息
					Element sf_ele = first_ele.element("DJF_DJ_SF");
					if(sf_ele!=null){
						sf_ele.setName("BDC_GZ_SF");
						sf_ele.addAttribute("SYWH", sjsbywh); 
					}
					//登记收件信息
					Element sj_ele = first_ele.element("DJF_DJ_SJ");
					if(sj_ele!=null){
						sj_ele.setName("BDC_GZ_SJ");
						sj_ele.addAttribute("SYWH", sjsbywh); 
					}
					//登记受理申请信息
					Element slsq_ele = first_ele.element("DJT_DJ_SLSQ");
					if(slsq_ele!=null){
						slsq_ele.setName("BDC_GZ_SLSQ");
						slsq_ele.addAttribute("SYWH", sjsbywh); 
					}
					//权利人
					Element qlr_ele = first_ele.element("ZTT_GY_QLR");
					if(qlr_ele!=null){
						qlr_ele.setName("BDC_GZ_QLR");
						qlr_ele.addAttribute("SYWH", sjsbywh); 
					}
					//预告登记信息()
					Element ygdj_ele = first_ele.element("QLF_QL_YGDJ");
					if(ygdj_ele!=null){
						ygdj_ele.setName("BDC_GZ_YGDJ");
						ygdj_ele.addAttribute("SYWH", sjsbywh); 
					}
					//抵押权 
					Element dyaq_ele = first_ele.element("QLF_QL_DYAQ");
					if(dyaq_ele!=null){
						dyaq_ele.setName("BDC_GZ_DYAQ");
						dyaq_ele.addAttribute("SYWH", sjsbywh); 
					}
					//房地产权_独幢、层、套、间房屋信息
					Element fdcq2_ele = first_ele.element("QLT_FW_FDCQ_YZ");
					if(fdcq2_ele!=null){
						fdcq2_ele.setName("BDC_GZ_FDCQ2");
						fdcq2_ele.addAttribute("SYWH", sjsbywh); 
					}
					//户信息
					Element h_ele = first_ele.element("KTT_FW_H");
					if(h_ele!=null){
						h_ele.setName("BDC_GZ_H");
					}
					//层信息
					Element c_ele = first_ele.element("KTT_FW_C");
					if(c_ele!=null){
						c_ele.setName("BDC_GZ_C");
					}
					//逻辑幢信息
					Element ljz_ele = first_ele.element("KTT_FW_LJZ");
					if(ljz_ele!=null){
						ljz_ele.setName("BDC_GZ_LJZ");
					}
					//自然幢信息
					Element zrz_ele = first_ele.element("KTT_FW_ZRZ");
					if(zrz_ele!=null){
						zrz_ele.setName("BDC_GZ_ZRZ");
					}
					//宗地空间属性()
					List<Element> zdk103_eles = first_ele.elements("ZD_K_103");
					if(zdk103_eles!=null&&zdk103_eles.size()>0){
						for(Element zdk103_ele:zdk103_eles){
							zdk103_ele.setName("BDC_GZ_ZDKJSX");
						}
					}
					List<Element> fj_eles = first_ele.elements("FJ_F_100");
					if(fj_eles!=null&&fj_eles.size()>0){
						for(Element fj_ele:fj_eles){
							first_ele.remove(fj_ele);
						}
					}
				}
			}
			
			Dom4jXmlUtil.OutputXmlFile(document, reportXmlPath);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 根据属性名获取属性值
	 */
	private Object getFieldValueByName(String fieldName, Object o) {
		try {
			String firstLetter = fieldName.substring(0, 1).toUpperCase();
			String getter = "get" + firstLetter + fieldName.substring(1);
			Method method = o.getClass().getMethod(getter, new Class[] {});
			Object value = method.invoke(o, new Object[] {});
			return value;
		} catch (Exception e) {
			return null;
		}
	}

	private String uploadToSupermapJointSystem(String strxml) {
		String sresult = "";
		String strurl =ConfigHelper.getNameByValue("OurJointClientUrl");// "http://localhost:8085/jointserver/receive/xmlfile";
		HttpURLConnection connet = null;
		try {
			StringBuilder sb=new StringBuilder();
			
			String xml=URLEncoder.encode(URLEncoder.encode(strxml,"utf-8"),"utf-8");
			String content =MessageFormat.format("XMLCONTENT={0}",xml);
			
			URL url = new URL(strurl);
			connet = (HttpURLConnection) url.openConnection();
			connet.setDoInput(true);
			connet.setDoOutput(true);
			connet.setRequestMethod("POST");
			//connet.setConnectTimeout(20 * 1000);// 设置连接超时时间为5秒
			//connet.setReadTimeout(20 * 1000);// 设置读取超时时间为20秒
			connet.setRequestProperty("Accept-Charset", "UTF-8");
			connet.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
	        OutputStream os = connet.getOutputStream();
	        PrintWriter pw = new PrintWriter(new OutputStreamWriter(os));
	        pw.write(content);
	        pw.close();
			BufferedReader brd = new BufferedReader(new InputStreamReader(connet.getInputStream(), "utf-8"));
			String line;
			while ((line = brd.readLine()) != null)
			{
				sb.append(line);
			}
			brd.close();
			sresult=sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sresult;
	}
	
	private String uploadToSupermapJointSystemByWSDL(String bizmsg) {
		String sresult = "";
		String username=ConfigHelper.getNameByValue("UserNameReportWSDL");
		String password=ConfigHelper.getNameByValue("PassWordReportWSDL");
		
		String soapaction=ConfigHelper.getNameByValue("SoapActionReportWSDL");
		String targetnamespace=ConfigHelper.getNameByValue("TargetNamespaceReportWSDL");
		String url=ConfigHelper.getNameByValue("UrlReportWSDL");
		String methodname=ConfigHelper.getNameByValue("MethodNameReportWSDL");
		
		List<ParamInfo> params=new ArrayList<ParamInfo>();
		ParamInfo param_user=new ParamInfo();
		param_user.setParamName("username");
		param_user.setParamMode(javax.xml.rpc.ParameterMode.IN);
		param_user.setParamType(org.apache.axis.encoding.XMLType.XSD_STRING);
		param_user.setParamValue(username);
		params.add(param_user);
		
		ParamInfo param_pwd=new ParamInfo();
		param_pwd.setParamName("passwd");
		param_pwd.setParamMode(javax.xml.rpc.ParameterMode.IN);
		param_pwd.setParamType(org.apache.axis.encoding.XMLType.XSD_STRING);
		param_pwd.setParamValue(password);
		params.add(param_pwd);
		
		ParamInfo param_bizmsg=new ParamInfo();
		param_bizmsg.setParamName("BizMsg");
		param_bizmsg.setParamMode(javax.xml.rpc.ParameterMode.IN);
		param_bizmsg.setParamType(org.apache.axis.encoding.XMLType.XSD_STRING);
		param_bizmsg.setParamValue(bizmsg);
		params.add(param_bizmsg);
		
		
		WSDLInfo wsdlinfo=new WSDLInfo();
		wsdlinfo.setMethodName(methodname);
		wsdlinfo.setReturnType(org.apache.axis.encoding.XMLType.XSD_STRING);
		wsdlinfo.setSoapAction(soapaction);
		wsdlinfo.setTargetNamespace(targetnamespace);
		wsdlinfo.setUrl(url);
		wsdlinfo.setUser(username);
		wsdlinfo.setPassWord(password);
		Object obj=null;
		try {
			String xzqhdm=ConfigHelper.getNameByValue("XZQHDM");
			if("411623".equals(xzqhdm)){
				//状态上报方法
				String soapActionURI = targetnamespace+methodname;
				Service service = new Service();
				Call call;
				call = (Call) service.createCall();
				call.setTargetEndpointAddress(url);
				call.setUseSOAPAction(true);
				call.setSOAPActionURI(soapActionURI);
				call.setOperationName(new QName(targetnamespace, methodname));
				//定义参数1：报文内容
				call.addParameter(new QName(targetnamespace, "message"), XMLType.XSD_STRING,ParameterMode.IN);
				//定义参数2：用户名
				call.addParameter(new QName(targetnamespace, "userName"), XMLType.XSD_STRING,ParameterMode.IN);
				//定义参数3：密码
				call.addParameter(new QName(targetnamespace, "pwd"), XMLType.XSD_STRING, ParameterMode.IN);
				//返回类型为字符串（响应报文内容JSON字符串）
				call.setReturnType(XMLType.XSD_STRING);
				
				String[] str = new String[3];
				//报文内容
				str[0] = bizmsg;
				str[1] = username;
				str[2] = password;
				String password_md5=com.supermap.wisdombusiness.utility.StringHelper.encryptMD5(password);
				str[2] = password_md5;
				obj = call.invoke(str);
			}else{
				obj = HttpRequest.sendWSDL(wsdlinfo,params);
			}
		} catch (Exception e) {
			System.out.print("Exception："+e.getMessage());
			e.printStackTrace();
		}
		sresult = StringHelper.formatObject(obj);
		return sresult;
	}
	
	public  String getBizMsgIDFromFile(String filename){
		String bizMsgID=filename;
		String regex = "Biz(.*)\\.xml";
		Pattern pattern = Pattern.compile(regex);
		Matcher m = pattern.matcher(filename);
		while (m.find()) {
			bizMsgID=m.group(1);
		}
		return bizMsgID;
	}

	@SuppressWarnings("rawtypes")
	public static String httpGet(String filepath, String urlStr, Map urlParam) throws IOException {
		@SuppressWarnings("unchecked")
		MyThread my = new MyThread(filepath, urlStr, urlParam);
		Thread t = new Thread(my);
		t.start();

		return "success";
	}

	private String uploadToInspurJointSystem(String localFilePath, String reccode, String actinstID) {
		try
		{
			dataReport mySFTP = new dataReport();
			ChannelSftp sftp = mySFTP.getSftp();
			if (sftp != null) {
				String path = ConfigHelper.getNameByValue("SFTPBIZMSGPATH");
				sftp.cd(path);
				System.out.println("本地xml地址:" + localFilePath);
				mySFTP.upload(localFilePath, sftp);
				return "1";
			}else{
				return "sftp连接失败！";
			}
		}
		catch (Exception e) {
			return e.getMessage();
		}
	}

	protected static String getPrimaryKey() {
		String _id = SuperHelper.GeneratePrimaryKey();
		return _id;
	}

	/**
	 * 获取收费参数
	 * @Title: getChargeParam
	 * @author:liushufeng
	 * @date：2015年7月26日 上午3:15:13
	 * @return
	 */
	public ChargeParam getChargeParam() {
		// 面积，案件数，证书个数
		double mj = 0;
		double js = 1;
		double zsgs = 0;
		double ts = 0;
		double jyjg = 0;// 交易价格
		CommonDao dao = this.getCommonDao();
		if (dao != null) {
			String xmbhcondition = ProjectHelper.GetXMBHCondition(xmbh);
			List<BDCS_DJDY_GZ> djdys = dao.getDataList(BDCS_DJDY_GZ.class, xmbhcondition);
			if (djdys != null && djdys.size() > 0) {
				for (BDCS_DJDY_GZ djdy : djdys) {
					BDCDYLX dylx = BDCDYLX.initFrom(djdy.getBDCDYLX());
					DJDYLY ly = DJDYLY.initFrom(djdy.getLY());
					RealUnit unit = UnitTools.loadUnit(dylx, ly, djdy.getBDCDYID());

					if (unit != null) {
						mj += unit.getMJ();
						ts += 1;
					}
					List<Rights> listrights = RightsTools.loadRightsByCondition(DJDYLY.GZ, "DJDYID='" + djdy.getDJDYID() + "' AND QLLX='4' AND XMBH='" + xmbh + "'");
					if (listrights != null && listrights.size() > 0) {
						Rights r = listrights.get(0);
						if (r != null) {
							if (r.getQDJG() != null) {
								jyjg += r.getQDJG();
							}
						}
					}
				}
			}

			List<BDCS_ZS_GZ> zss = dao.getDataList(BDCS_ZS_GZ.class, xmbhcondition);
			if (zss != null && zss.size() > 0) {
				zsgs = zss.size();
			}
		}
		ChargeParam param = new ChargeParam();
		param.put("MJ", mj);
		param.put("ZSGS", zsgs);
		param.put("AJS", js);
		param.put("TS", ts);
		param.put("JYJG", jyjg);
		return param;
	}

	/**
	 * 根据登记单元ID和权利类型拷贝权利人到申请人
	 * @Author：俞学斌
	 * @param djdyid
	 *            登记单元ID
	 * @param qllx
	 *            权利类型
	 * @param xmbh
	 *            项目编号
	 * @param qlid
	 *            关联权利ID
	 */
	protected void CopySQRFromXZQLR(String djdyid, String qllx, String xmbh, String qlid, String sqrlb) {
		StringBuilder builderQL = new StringBuilder();
		builderQL.append("DJDYID ='");
		builderQL.append(djdyid);
		builderQL.append("' AND QLLX='");
		builderQL.append(qllx);
		builderQL.append("'");
		List<BDCS_QL_XZ> qls = baseCommonDao.getDataList(BDCS_QL_XZ.class, builderQL.toString());
		if (qls == null || qls.size() <= 0) {
			return;
		}
		BDCS_QL_XZ ql = qls.get(0);

		StringBuilder builderQLR = new StringBuilder();
		builderQLR.append("QLID ='");
		builderQLR.append(ql.getId());
		builderQLR.append("'");
		List<BDCS_QLR_XZ> qlrs = baseCommonDao.getDataList(BDCS_QLR_XZ.class, builderQLR.toString());
		if (qls == null || qls.size() <= 0) {
			return;
		}
		for (int iqlr = 0; iqlr < qlrs.size(); iqlr++) {
			String SQRID = getPrimaryKey();
			BDCS_QLR_XZ qlr = qlrs.get(iqlr);
			List<BDCS_SQR> sqrs = baseCommonDao.getDataList(BDCS_SQR.class, "XMBH='" + this.getXMBH() + "' AND SQRLB='" + sqrlb + "' AND SQRXM='" + qlr.getQLRMC() + "'");
			if (sqrs != null && sqrs.size() > 0) {
				continue;
			}
			BDCS_SQR sqr = new BDCS_SQR();
			sqr.setGYFS(qlr.getGYFS());
			sqr.setFZJG(qlr.getFZJG());
			sqr.setGJDQ(qlr.getGJ());
			sqr.setGZDW(qlr.getGZDW());
			sqr.setXB(qlr.getXB());
			sqr.setHJSZSS(qlr.getHJSZSS());
			sqr.setSSHY(qlr.getSSHY());
			sqr.setYXBZ(qlr.getYXBZ());
			sqr.setQLBL(qlr.getQLBL());
			sqr.setQLMJ(StringHelper.formatObject(qlr.getQLMJ()));
			sqr.setSQRXM(qlr.getQLRMC());
			sqr.setSQRLB(sqrlb);
			sqr.setSQRLX(qlr.getQLRLX());
			sqr.setDZYJ(qlr.getDZYJ());
			sqr.setLXDH(qlr.getDH());
			sqr.setZJH(qlr.getZJH());
			sqr.setZJLX(qlr.getZJZL());
			sqr.setTXDZ(qlr.getDZ());
			sqr.setYZBM(qlr.getYB());
			sqr.setXMBH(xmbh);
			sqr.setId(SQRID);
			sqr.setGLQLID(qlid);
			//luml 添加法定代表人、代理人信息
			sqr.setFDDBR(qlr.getFDDBR());
			sqr.setFDDBRZJHM(qlr.getFDDBRZJHM());
			sqr.setFDDBRZJLX(qlr.getFDDBRZJLX());
			sqr.setFDDBRDH(qlr.getFDDBRDH());
			sqr.setDLRXM(qlr.getDLRXM());
			sqr.setDLRZJHM(qlr.getDLRZJHM());
			sqr.setDLRZJLX(qlr.getDLRZJLX());
			sqr.setDLRLXDH(qlr.getDLRLXDH());
			sqr.setDLRNATION(qlr.getDLRNATION());
			baseCommonDao.save(sqr);
		}
	}

	/**
	 * 拷贝现状权利人到申请人
	 * @Title: copyXZQLRtoSQR
	 * @author:liushufeng
	 * @date：2015年8月25日 下午9:40:48
	 * @param sqr
	 * @param qlr
	 */
	protected BDCS_SQR copyXZQLRtoSQR(BDCS_QLR_XZ qlr, SQRLB lb) {
		String strsql = "XMBH='" + this.getXMBH() + "' AND SQRLB='" + lb.Value + "' AND SQRXM='" + qlr.getQLRMC() + "' AND ZJH IS NULL";
		if (!StringHelper.isEmpty(qlr.getZJH())) {
			strsql = "XMBH='" + this.getXMBH() + "' AND SQRLB='" + lb.Value + "' AND SQRXM='" + qlr.getQLRMC() + "' AND ZJH='" + qlr.getZJH() + "'";
		}
		List<BDCS_SQR> sqrs = baseCommonDao.getDataList(BDCS_SQR.class, strsql);
		if (sqrs != null && sqrs.size() > 0) {
			return null;
		}
		BDCS_SQR sqr = new BDCS_SQR();
		String SQRID = getPrimaryKey();
		sqr.setGYFS(qlr.getGYFS());
		sqr.setFZJG(qlr.getFZJG());
		sqr.setGJDQ(qlr.getGJ());
		sqr.setGZDW(qlr.getGZDW());
		sqr.setXB(qlr.getXB());
		sqr.setHJSZSS(qlr.getHJSZSS());
		sqr.setSSHY(qlr.getSSHY());
		sqr.setYXBZ(qlr.getYXBZ());
		sqr.setQLBL(qlr.getQLBL());
		sqr.setQLMJ(StringHelper.formatObject(qlr.getQLMJ()));
		sqr.setSQRXM(qlr.getQLRMC());
		sqr.setSQRLB(lb.Value);
		sqr.setSQRLX(qlr.getQLRLX());
		sqr.setDZYJ(qlr.getDZYJ());
		sqr.setLXDH(qlr.getDH());
		sqr.setZJH(qlr.getZJH());
		sqr.setZJLX(qlr.getZJZL());
		sqr.setTXDZ(qlr.getDZ());
		sqr.setYZBM(qlr.getYB());
		sqr.setXMBH(xmbh);
		sqr.setId(SQRID);
		sqr.setGLQLID(qlr.getQLID());
		return sqr;
	}

	/**
	 * 根据登记单元ID和项目编号删除权利关联申请人
	 * @Author：俞学斌
	 * @param djdyid
	 *            登记单元ID
	 * @param xmbh
	 *            项目编号
	 */
	protected void RemoveSQRByQLID(String djdyid, String xmbh) {
		StringBuilder builderQL = new StringBuilder();
		builderQL.append("DJDYID='");
		builderQL.append(djdyid);
		builderQL.append("' AND XMBH='");
		builderQL.append(xmbh);
		builderQL.append("'");
		List<BDCS_QL_GZ> qls = baseCommonDao.getDataList(BDCS_QL_GZ.class, builderQL.toString());
		if (qls == null || qls.size() <= 0) {
			return;
		}
		for (int iql = 0; iql < qls.size(); iql++) {
			BDCS_QL_GZ ql = qls.get(iql);
			StringBuilder builderSQR = new StringBuilder();
			builderSQR.append("GLQLID='");
			builderSQR.append(ql.getId());
			builderSQR.append("' AND XMBH='");
			builderSQR.append(xmbh);
			builderSQR.append("'");
			List<BDCS_SQR> sqrs = baseCommonDao.getDataList(BDCS_SQR.class, builderSQR.toString());
			if (sqrs == null || sqrs.size() <= 0) {
				return;
			}
			for (int isqr = 0; isqr < sqrs.size(); isqr++) {
				BDCS_SQR sqr = sqrs.get(isqr);
				baseCommonDao.deleteEntity(sqr);
			}
		}
	}

	/**
	 * 删除现状库中的登记单元
	 * @Title: removeDJDYFromXZ
	 * @author:liushufeng
	 * @date：2015年9月10日 下午6:11:16
	 * @param djdyid
	 */
	public void removeDJDYFromXZ(String djdyid) {
		String sql = MessageFormat.format("DJDYID=''{0}''", djdyid);
		getCommonDao().deleteEntitysByHql(BDCS_DJDY_XZ.class, sql);
	}

	/**
	 * 登簿拷贝单元图形服务
	 * @Title: CopyGeo
	 * @author:俞学斌
	 * @date：2015年9月10日 下午6:11:16
	 * @param bdcdyid
	 * @param lx
	 * @param ly
	 */
	public void CopyGeo(String bdcdyid, BDCDYLX lx, DJDYLY ly) {
		if (BDCDYLX.SYQZD.equals(lx) || BDCDYLX.SHYQZD.equals(lx) || BDCDYLX.LD.equals(lx) || BDCDYLX.HY.equals(lx) || BDCDYLX.NYD.equals(lx)|| BDCDYLX.GZW.equals(lx)) {
			GeoOperateTools.CopyFeatures("BDCDCK", lx.DCTableName, "BDCK", lx.XZTableName, "BDCDYID='" + bdcdyid + "'", CopyOperateMode.AppendCaseNotExisted);
			GeoOperateTools.CopyFeatures("BDCDCK", lx.DCTableName, "BDCK", lx.LSTableName, "BDCDYID='" + bdcdyid + "'", CopyOperateMode.AppendCaseNotExisted);
		} else if (BDCDYLX.H.equals(lx)) {
			House h = (House) UnitTools.loadUnit(lx, ly, bdcdyid);
			if (h != null && !StringHelper.isEmpty(h.getZRZBDCDYID())) {
				GeoOperateTools.CopyFeatures("BDCDCK", BDCDYLX.ZRZ.DCTableName, "BDCK", BDCDYLX.ZRZ.XZTableName, "BDCDYID='" + h.getZRZBDCDYID() + "'",
						CopyOperateMode.AppendCaseNotExisted);
				GeoOperateTools.CopyFeatures("BDCDCK", BDCDYLX.ZRZ.DCTableName, "BDCK", BDCDYLX.ZRZ.LSTableName, "BDCDYID='" + h.getZRZBDCDYID() + "'",
						CopyOperateMode.AppendCaseNotExisted);
			}
		} else if (BDCDYLX.YCH.equals(lx)) {
			House h = (House) UnitTools.loadUnit(lx, ly, bdcdyid);
			if (h != null && !StringHelper.isEmpty(h.getZRZBDCDYID())) {
				GeoOperateTools.CopyFeatures("BDCDCK", BDCDYLX.YCZRZ.DCTableName, "BDCK", BDCDYLX.YCZRZ.XZTableName, "BDCDYID='" + h.getZRZBDCDYID() + "'",
						CopyOperateMode.AppendCaseNotExisted);
				GeoOperateTools.CopyFeatures("BDCDCK", BDCDYLX.YCZRZ.DCTableName, "BDCK", BDCDYLX.YCZRZ.LSTableName, "BDCDYID='" + h.getZRZBDCDYID() + "'",
						CopyOperateMode.AppendCaseNotExisted);
			}
		}
	}
	
	/**变更登记_房地都变化时，拷贝自然幢的同时需要拷贝宗地
	 * @param bdcdyid
	 * @param lx
	 * @param ly
	 */ 
	public void CopyGeo_BG(String bdcdyid, BDCDYLX lx, DJDYLY ly) {
		if (BDCDYLX.SYQZD.equals(lx) || BDCDYLX.SHYQZD.equals(lx) || BDCDYLX.LD.equals(lx) || BDCDYLX.HY.equals(lx) || BDCDYLX.NYD.equals(lx)|| BDCDYLX.GZW.equals(lx)) {
			GeoOperateTools.CopyFeatures("BDCDCK", lx.DCTableName, "BDCK", lx.XZTableName, "BDCDYID='" + bdcdyid + "'", CopyOperateMode.AppendCaseNotExisted);
			GeoOperateTools.CopyFeatures("BDCDCK", lx.DCTableName, "BDCK", lx.LSTableName, "BDCDYID='" + bdcdyid + "'", CopyOperateMode.AppendCaseNotExisted);
		} else if (BDCDYLX.H.equals(lx)) {
			House h = (House) UnitTools.loadUnit(lx, ly, bdcdyid);
			if (h != null && !StringHelper.isEmpty(h.getZRZBDCDYID())) {
				GeoOperateTools.CopyFeatures("BDCDCK", BDCDYLX.ZRZ.DCTableName, "BDCK", BDCDYLX.ZRZ.XZTableName, "BDCDYID='" + h.getZRZBDCDYID() + "'",
						CopyOperateMode.AppendCaseNotExisted);
				GeoOperateTools.CopyFeatures("BDCDCK", BDCDYLX.ZRZ.DCTableName, "BDCK", BDCDYLX.ZRZ.LSTableName, "BDCDYID='" + h.getZRZBDCDYID() + "'",
						CopyOperateMode.AppendCaseNotExisted);
			}
			if (h != null && !StringHelper.isEmpty(h.getZDBDCDYID())) {
				GeoOperateTools.CopyFeatures("BDCDCK", BDCDYLX.SHYQZD.DCTableName, "BDCK", BDCDYLX.SHYQZD.XZTableName, "BDCDYID='" + h.getZDBDCDYID() + "'", 
						CopyOperateMode.AppendCaseNotExisted);
				GeoOperateTools.CopyFeatures("BDCDCK", BDCDYLX.SHYQZD.DCTableName, "BDCK", BDCDYLX.SHYQZD.LSTableName, "BDCDYID='" + h.getZDBDCDYID() + "'", 
						CopyOperateMode.AppendCaseNotExisted);
			}
		} else if (BDCDYLX.YCH.equals(lx)) {
			House h = (House) UnitTools.loadUnit(lx, ly, bdcdyid);
			if (h != null && !StringHelper.isEmpty(h.getZRZBDCDYID())) {
				GeoOperateTools.CopyFeatures("BDCDCK", BDCDYLX.YCZRZ.DCTableName, "BDCK", BDCDYLX.YCZRZ.XZTableName, "BDCDYID='" + h.getZRZBDCDYID() + "'",
						CopyOperateMode.AppendCaseNotExisted);
				GeoOperateTools.CopyFeatures("BDCDCK", BDCDYLX.YCZRZ.DCTableName, "BDCK", BDCDYLX.YCZRZ.LSTableName, "BDCDYID='" + h.getZRZBDCDYID() + "'",
						CopyOperateMode.AppendCaseNotExisted);
			}
			if (h != null && !StringHelper.isEmpty(h.getZDBDCDYID())) {
				GeoOperateTools.CopyFeatures("BDCDCK", BDCDYLX.SHYQZD.DCTableName, "BDCK", BDCDYLX.SHYQZD.XZTableName, "BDCDYID='" + h.getZDBDCDYID() + "'", 
						CopyOperateMode.AppendCaseNotExisted);
				GeoOperateTools.CopyFeatures("BDCDCK", BDCDYLX.SHYQZD.DCTableName, "BDCK", BDCDYLX.SHYQZD.LSTableName, "BDCDYID='" + h.getZDBDCDYID() + "'", 
						CopyOperateMode.AppendCaseNotExisted);
			}
		}
	}

	/**
	 * 登簿删除单元服务
	 * @Title: DeleteGeo
	 * @author:俞学斌
	 * @date：2015年9月10日 下午6:11:16
	 * @param bdcdyid
	 * @param lx
	 * @param ly
	 */
	public void DeleteGeo(String bdcdyid, BDCDYLX lx, DJDYLY ly) {
		if (BDCDYLX.SYQZD.equals(lx) || BDCDYLX.SHYQZD.equals(lx) || BDCDYLX.LD.equals(lx) || BDCDYLX.HY.equals(lx)) {
			GeoOperateTools.DeleteFeatures("BDCK", lx.XZTableName, "BDCDYID='" + bdcdyid + "'");
			DeleteBdck_("BDCK", lx.XZTableName, "BDCDYID='" + bdcdyid + "'");
			DeleteBdcdck_gz("BDCDCK", lx.GZTableName, "BDCDYID='" + bdcdyid + "'");
		}
	}

	/**
	 * 登簿删除单元服务
	 * @Title: DeleteGeo
	 * @author:俞学斌
	 * @date：2015年9月10日 下午6:11:16
	 * @param bdcdyid
	 * @param lx
	 * @param ly
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void UpdateGeo(String bdcdyid, BDCDYLX lx, DJDYLY ly) {
		if (BDCDYLX.SYQZD.equals(lx) || BDCDYLX.SHYQZD.equals(lx) || BDCDYLX.LD.equals(lx) || BDCDYLX.HY.equals(lx)) {
			RealUnit unit = UnitTools.loadUnit(lx, ly, bdcdyid);
			Class unitclass = unit.getClass();
			java.lang.reflect.Field[] fields = unitclass.getDeclaredFields();
			List<String> listFldNames = new ArrayList<String>();
			List<String> listFldValues = new ArrayList<String>();
			for (int ifld = 0; ifld < fields.length; ifld++) {
				Method med;
				try {
					med = unitclass.getDeclaredMethod("get" + fields[ifld]);
					Object obj;
					try {
						obj = med.invoke(unit);
						String value = StringHelper.formatObject(obj);
						listFldNames.add(fields[ifld].getName());
						listFldValues.add(value);
					} catch (Exception e) {
					}
				} catch (Exception e) {
				}
			}
			GeoOperateTools.UpdateFeatures("BDCK", lx.XZTableName, "BDCDYID='" + bdcdyid + "'", listFldNames, listFldValues);
		}
	}

	public boolean isCForCFING() {
		boolean b = false;
		List<BDCS_DJDY_GZ> djdys = baseCommonDao.getDataList(BDCS_DJDY_GZ.class, "XMBH='" + getXMBH() + "'");
		if (djdys != null && djdys.size() > 0) {
			for (BDCS_DJDY_GZ djdy : djdys) {
				if (BDCDYLX.H.Value.equals(djdy.getBDCDYLX())) {
					if (!StringHelper.isEmpty(djdy.getBDCDYID()))// 实测户
					{
						String fulSql = " select YCBDCDYID from BDCK.YC_SC_H_XZ where SCBDCDYID= '" + djdy.getBDCDYID() + "'";
						List<Map> lst = baseCommonDao.getDataListByFullSql(fulSql);
						if (lst != null && lst.size() > 0) {
							for (Map m : lst) {
								List<BDCS_DJDY_XZ> dys = baseCommonDao.getDataList(BDCS_DJDY_XZ.class, "BDCDYID ='" + m.get("YCBDCDYID") + "'");
								if (dys != null && dys.size() > 0) {
									for (BDCS_DJDY_XZ dy : dys) {
										if (!StringHelper.isEmpty(dy.getDJDYID())) {
											String sql = "  FROM BDCK.BDCS_QL_XZ WHERE DJDYID='" + dy.getDJDYID() + "' AND DJLX='800' AND QLLX='99'";
											String sql2 = " FROM BDCK.BDCS_QL_GZ WHERE DJSJ IS NULL AND DJLX='800' AND QLLX='99' and  DJDYID='" + dy.getDJDYID() + "'";
											long count = baseCommonDao.getCountByFullSql(sql);
											long count2 = baseCommonDao.getCountByFullSql(sql2);
											if ((count + count2) > 0) {
												b = true;
												setErrMessage("该房屋对应预测户的不动产单元号为" + dy.getBDCDYH() + "的单元已经被查封或正在办理查封，不能登簿！");
												break;
											}
										}
									}
								}
							}
						}
					}
					if (b) {
						return b;
					}
				} else if (BDCDYLX.YCH.Value.equals(djdy.getBDCDYLX())) {
					if (!StringHelper.isEmpty(djdy.getBDCDYID()))// 预测户
					{
						String fulSql = " select SCBDCDYID from BDCK.YC_SC_H_XZ where YCBDCDYID= '" + djdy.getBDCDYID() + "'";
						List<Map> lst = baseCommonDao.getDataListByFullSql(fulSql);
						if (lst != null && lst.size() > 0) {
							for (Map m : lst) {
								List<BDCS_DJDY_XZ> dys = baseCommonDao.getDataList(BDCS_DJDY_XZ.class, "BDCDYID ='" + m.get("SCBDCDYID") + "'");
								if (dys != null && dys.size() > 0) {
									for (BDCS_DJDY_XZ dy : dys) {
										if (!StringHelper.isEmpty(dy.getDJDYID())) {
											String sql = "  FROM BDCK.BDCS_QL_XZ WHERE DJDYID='" + dy.getDJDYID() + "' AND DJLX='800' AND QLLX='99'";
											String sql2 = " FROM BDCK.BDCS_QL_GZ WHERE DJSJ IS NULL AND DJLX='800' AND QLLX='99' and  DJDYID='" + dy.getDJDYID() + "'";
											long count = baseCommonDao.getCountByFullSql(sql);
											long count2 = baseCommonDao.getCountByFullSql(sql2);
											if ((count + count2) > 0) {
												b = true;
												setErrMessage("该预测房屋对应实测房屋的不动产单元号为" + dy.getBDCDYH() + "的单元已经被查封或正在办理查封，不能登簿！");
												break;
											}
										}
									}
								}
							}
						}
					}
					if (b) {
						return b;
					}
				}
				String djdyid = djdy.getDJDYID();
				String sql = "  FROM BDCK.BDCS_QL_XZ WHERE DJDYID='" + djdyid + "' AND DJLX='800'";
				String sql2 = " FROM BDCK.BDCS_QL_GZ WHERE DJSJ IS NULL AND DJLX='800' AND QLLX='99' and  DJDYID='" + djdyid + "'";
				long count = baseCommonDao.getCountByFullSql(sql);
				long count2 = baseCommonDao.getCountByFullSql(sql2);
				if ((count + count2) > 0) {
					b = true;
					setErrMessage("不动产单元号为" + djdy.getBDCDYH() + "的单元已经被查封或正在办理查封，不能登簿！");
					break;
				}
			}
		}
		return b;
	}

	protected boolean DyLimit() {
		String dbr = Global.getCurrentUserName();
		Date djsj = new Date();
		List<BDCS_DYXZ> dyxzs = baseCommonDao.getDataList(BDCS_DYXZ.class, ProjectHelper.GetXMBHCondition(this.getXMBH()));
		if (dyxzs != null && dyxzs.size() > 0) {
			for (BDCS_DYXZ dyxz : dyxzs) {
				dyxz.setYXBZ("1");
				dyxz.setDBR(dbr);
				dyxz.setDJSJ(djsj);
				baseCommonDao.update(dyxz);
			}
		}
		baseCommonDao.flush();
		return true;
	}

	protected boolean DyLimitLifted() {
		String dbr = Global.getCurrentUserName();
		Date djsj = new Date();
		List<Rights> qls = RightsTools.loadRightsByCondition(DJDYLY.GZ, "XMBH='" + this.getXMBH() + "'");
		if (qls != null && qls.size() > 0) {
			for (Rights ql : qls) {
				String lyqlid = ql.getLYQLID();
				if (StringHelper.isEmpty(lyqlid)) {
					continue;
				}
				Rights lsql = RightsTools.loadRights(DJDYLY.LS, lyqlid);
				if (StringHelper.isEmpty(lsql)) {
					continue;
				}
				String ywh = lsql.getYWH();
				if (StringHelper.isEmpty(ywh)) {
					continue;
				}
				List<BDCS_DYXZ> dyxzs = baseCommonDao.getDataList(BDCS_DYXZ.class, "YWH='" + ywh + "'");
				if (dyxzs != null && dyxzs.size() > 0) {
					for (BDCS_DYXZ dyxz : dyxzs) {
						dyxz.setYXBZ("2");
						dyxz.setZXYWH(this.getProject_id());
						dyxz.setZXDBR(dbr);
						dyxz.setZXDJSJ(djsj);
						baseCommonDao.update(dyxz);
					}
				}
			}
		}
		baseCommonDao.flush();
		return true;
	}

	protected void deletePreHouseRights(String relhousebdcdyid, String qllx, String djlx, String zxyy) {
		String fulsql = " SELECT QLID FROM BDCK.BDCS_QL_XZ QL LEFT JOIN BDCK.BDCS_DJDY_XZ DJDY ON QL.DJDYID=DJDY.DJDYID LEFT JOIN BDCK.YC_SC_H_XZ GX ON DJDY.BDCDYID =GX.YCBDCDYID WHERE GX.SCBDCDYID='"
				+ relhousebdcdyid + "' AND QL.QLLX='" + qllx + "' AND QL.DJLX='" + djlx + "'";
		List<Map> listmap = getCommonDao().getDataListByFullSql(fulsql);
		if (listmap != null && listmap.size() > 0) {
			for (Map mp : listmap) {
				String qlid = StringHelper.isEmpty(mp.get("QLID")) ? "" : mp.get("QLID").toString();
				if (!StringHelper.isEmpty(qlid)) {
					Rights r = RightsTools.loadRights(DJDYLY.XZ, qlid);
					if (r != null) {
						// 更新历史里边的注销信息
						if (!StringHelper.isEmpty(r.getFSQLID())) {
							SubRights lsygrights = RightsTools.loadSubRights(DJDYLY.LS, r.getFSQLID());
							if (lsygrights != null) {
								lsygrights.setZXDYYY(zxyy);
								lsygrights.setZXSJ(new Date());
								lsygrights.setZXDYYWH(getProject_id());
								getCommonDao().update(lsygrights);
							}
						}
						// 删除现状预告的权利
						RightsTools.deleteRightsAll(DJDYLY.XZ, r.getId());
					}
				}
			}
		}
	}
	
	/**
	 * 删除空间数据
	 * @Title: DeleteFeatures
	 * @author:heks
	 * @date：2017年09月18日 下午17:14:45
	 * @param _desDsName
	 *            目标数据源别名
	 * @param _desDtName 目标数据集名
	 * @param _conditon 查询条件
	 */
	public void DeleteBdck_(String _desDsName,String _desDtName,String _conditon) {
		_desDtName=_desDtName.replaceAll("BDCS_", "BDCK_");
		_desDtName=_desDtName.replaceAll("DCS_", "BDCK_");
		String deleteFulSql = "delete from "+_desDsName+"."+_desDtName+" where "+_conditon;
		baseCommonDao.updateBySql(deleteFulSql);
		String deletebdcdck_fulsql = "delete from BDCDCK."+_desDtName+" where "+_conditon;
		baseCommonDao.updateBySql(deletebdcdck_fulsql);
		baseCommonDao.flush();
	}
	
	/**
	 * 删除bdcdck工作层空间数据
	 * @Title: DeleteFeatures
	 * @author:liangc
	 * @date：2018年10月25日 下午17:50:30
	 * @param _desDsName
	 *            目标数据源别名
	 * @param _desDtName 目标数据集名
	 * @param _conditon 查询条件
	 */
	public void DeleteBdcdck_gz(String _desDsName,String _desDtName,String _conditon) {
		_desDtName=_desDtName.replaceAll("BDCS_", "BDCK_");
		_desDtName=_desDtName.replaceAll("DCS_", "BDCK_");
		String deleteFulSql = "delete from "+_desDsName+"."+_desDtName+" where "+_conditon;
		baseCommonDao.updateBySql(deleteFulSql);
		baseCommonDao.flush();
	}
	
	/**
	 * 根据xmbh找sqr把数据拷贝到qlr工作层
	 * 
	 * @Author liangq
	 * @CreateTime 2017年8月16日下午15:45:34
	 * @param qlid
	 *            工作层的权利ID
	 * @return 成功返回true，失败返回false
	 */
	protected boolean createQLRGZ(String qlid) {
		CommonDao dao = getCommonDao();
		long qlr_count = dao.getCountByFullSql(" from bdck.bdcs_qlr_gz where qlid = '"+qlid+"'");
		if(qlr_count>0){
			return false;
		}else{
			StringBuilder builderQL = new StringBuilder();
			builderQL.append("xmbh ='");
			builderQL.append(xmbh);
			builderQL.append("' and sqrlb = '1'");
			List<BDCS_SQR> sqrs = dao.getDataList(BDCS_SQR.class, builderQL.toString());
			if (sqrs == null || sqrs.size() <= 0) {
				return false;
			}
			for(int i=0;i<sqrs.size();i++){
				BDCS_SQR sqr=sqrs.get(i);
				String QLRID = getPrimaryKey();
				BDCS_QLR_GZ qlrgz=new BDCS_QLR_GZ();
				qlrgz.setId(QLRID);
				qlrgz.setQLID(qlid);
				qlrgz.setGYFS(sqr.getGYFS());
				qlrgz.setFZJG(sqr.getFZJG());
				qlrgz.setGJ(sqr.getGJDQ());
				qlrgz.setGZDW(sqr.getGZDW());
				qlrgz.setXB(sqr.getXB()); 
				qlrgz.setHJSZSS(sqr.getHJSZSS()); 
				qlrgz.setSSHY(sqr.getSSHY());
				qlrgz.setYXBZ(sqr.getYXBZ());
				qlrgz.setQLBL(sqr.getQLBL());
				qlrgz.setQLMJ(StringHelper.getDouble(sqr.getQLMJ()));
				qlrgz.setQLRMC(sqr.getSQRXM());
				if(!StringHelper.isEmpty(sqr.getSQRLX())){
					qlrgz.setQLRLX(sqr.getSQRLX());
				}
				qlrgz.setDZYJ(sqr.getDZYJ());
				qlrgz.setDH(sqr.getLXDH());
				qlrgz.setZJH(sqr.getZJH());
				qlrgz.setZJZL(sqr.getZJLX());
				qlrgz.setDZ(sqr.getTXDZ());
				qlrgz.setYB(sqr.getYZBM());
				qlrgz.setXMBH(xmbh);
				dao.save(qlrgz);
			}
			dao.flush();
			return true;
		}
	}
	
	
	/**
	 * 先判断有没有预查封，有的话将预查封转为现查封，删除预查封现状权利
	 * @cratetime 2017-10-25 16:18:50
	 * @author liangc
	 * @param bdcdyid
	 */
	public void checkisYCF(String bdcdyid){
		List<YC_SC_H_XZ> gxs=getCommonDao().getDataList(YC_SC_H_XZ.class, "SCBDCDYID='"+bdcdyid+"'");
		if(gxs!=null&&gxs.size()>0){
			String ycbdcyid=StringHelper.formatObject(gxs.get(0).getYCBDCDYID());
			String ycfcountsql = " FROM (SELECT  GX.SCBDCDYID FROM BDCK.BDCS_H_XZY YCH "
					+ " LEFT JOIN BDCK.YC_SC_H_XZ GX ON YCH.BDCDYID=GX.YCBDCDYID  "
					+ " LEFT JOIN BDCK.BDCS_DJDY_XZ DJDY ON DJDY.BDCDYID=GX.YCBDCDYID "
					+ " LEFT JOIN BDCK.BDCS_QL_XZ QL ON QL.DJDYID=DJDY.DJDYID "
					+ " WHERE QL.DJLX='800' AND QL.QLLX='99' AND YCH.BDCDYID='"+ycbdcyid+"'"+" AND QL.QLID IS NOT NULL )";
			long ycfcount =getCommonDao().getCountByFullSql(ycfcountsql);
			if (ycfcount > 0){//现房关联的期房有查封，预查封转为现查封，删除预查封现状权利
				copyYCF2CF_ZXYCF(bdcdyid);
			}
		}
	}
	
	/**
	 * 根据bdcdyid将预查封转为现查封，删除预查封现状权利
	 * @cratetime 2017-10-25 15:30:45
	 * @author liangc
	 * @param bdcdyid
	 * @return
	 */
	protected boolean copyYCF2CF_ZXYCF(String bdcdyid){
		CommonDao dao = getCommonDao();
		BDCDYLX dylx = getBdcdylx();
		if(BDCDYLX.H.equals(dylx)){
			List<YC_SC_H_XZ> gxs=dao.getDataList(YC_SC_H_XZ.class, "SCBDCDYID='"+bdcdyid+"'");
			if(gxs!=null&&gxs.size()>0){
				String ycbdcyid=gxs.get(0).getYCBDCDYID();
				if(!StringHelper.isEmpty(ycbdcyid)){
					List<BDCS_DJDY_GZ> djdys_sc=dao.getDataList(BDCS_DJDY_GZ.class, "BDCDYID='"+bdcdyid+"' AND BDCDYLX='031'");
					List<BDCS_DJDY_XZ> djdys_yc=dao.getDataList(BDCS_DJDY_XZ.class, "BDCDYID='"+ycbdcyid+"' AND BDCDYLX='032'");
					if(djdys_yc!=null&&djdys_yc.size()>0){
						String djdyid_yc=djdys_yc.get(0).getDJDYID();
						if(!StringHelper.isEmpty(djdyid_yc)){
							List<BDCS_QL_XZ> qls_yc=dao.getDataList(BDCS_QL_XZ.class, "DJDYID='"+djdyid_yc+"' AND QLLX='99' AND DJLX='800'");
							List<BDCS_QL_XZ> ql_sc=dao.getDataList(BDCS_QL_XZ.class, "BDCDYID='"+StringHelper.formatObject(gxs.get(0).getSCBDCDYID())+"'");
							if(qls_yc!=null&&qls_yc.size()>0){
								for(BDCS_QL_XZ ql_yc:qls_yc){
									String cfqlid=SuperHelper.GeneratePrimaryKey();
									String cffsqlid=SuperHelper.GeneratePrimaryKey();
									BDCS_QL_XZ cfql_sc=new BDCS_QL_XZ();
									try {
										PropertyUtils.copyProperties(cfql_sc,ql_yc);
									} catch (Exception e) {
									}
									cfql_sc.setId(cfqlid);
									cfql_sc.setYWH(getProject_id());
									cfql_sc.setXMBH(getXMBH());
									cfql_sc.setDJDYID(djdys_sc.get(0).getDJDYID());
									cfql_sc.setFSQLID(cffsqlid);
									cfql_sc.setDBR(Global.getCurrentUserName());
									cfql_sc.setDJSJ(new Date());
									cfql_sc.setDJJG(ConfigHelper.getNameByValue("DJJGMC"));
									cfql_sc.setBDCDYH(StringHelper.formatObject(ql_sc.get(0).getBDCDYH()));
									cfql_sc.setBDCDYID(StringHelper.formatObject(ql_sc.get(0).getBDCDYID()));
									cfql_sc.setLYQLID(ql_yc.getId());
									dao.save(cfql_sc);
									
									BDCS_QL_LS cfql_sc_ls=new BDCS_QL_LS();
									try {
										PropertyUtils.copyProperties(cfql_sc_ls,cfql_sc);
									} catch (Exception e) {
									}
									dao.save(cfql_sc_ls);
									if(!StringHelper.isEmpty(ql_yc.getFSQLID())){
										BDCS_FSQL_XZ fsql_yc=dao.get(BDCS_FSQL_XZ.class, ql_yc.getFSQLID());
										if(fsql_yc!=null){
											BDCS_FSQL_XZ cffsql_sc=new BDCS_FSQL_XZ();
											try {
												PropertyUtils.copyProperties(cffsql_sc,fsql_yc);
											} catch (Exception e) {
											}
											cffsql_sc.setQLID(cfqlid);
											cffsql_sc.setId(cffsqlid);
											cffsql_sc.setXMBH(getXMBH());
											cffsql_sc.setDJDYID(djdys_sc.get(0).getDJDYID());
											cffsql_sc.setCFLX("1");
											dao.save(cffsql_sc);
											dao.deleteEntity(fsql_yc);
											BDCS_FSQL_LS cffsql_sc_ls=new BDCS_FSQL_LS();
											try {
												PropertyUtils.copyProperties(cffsql_sc_ls,cffsql_sc);
											} catch (Exception e) {
											}
											dao.save(cffsql_sc_ls);
										}
									}
									dao.deleteEntity(ql_yc);
								}
							}
						}
					}
				}
			}
		}
		return true;
	}
	/**
	 * 注销 登记上报
	 * @param path  报文 路径
	 * @param actinstID 
	 * @param index  组合流程下标
	 * @param index lclx  0: 普通流程   1:房地一体流程   2:同大类下的组合流程
	 * @return
	 */
	
	public Map<String, String> exportXML_ZX(String path, String actinstID,String index,String lclx) {

		Message msg=null;
		String result = "";
		Marshaller mashaller;
		Map<String, String> names = new HashMap<String, String>();
		CommonDao dao = this.getCommonDao();
		String xmbhHql = ProjectHelper.GetXMBHCondition(this.getXMBH());
		List<BDCS_DJDY_GZ> djdys = dao.getDataList(BDCS_DJDY_GZ.class, xmbhHql);
		if(lclx!=null&&lclx.equals("1")) {
			//房地一体路程查询历史层
			List<BDCS_DJDY_LS> djdys_ls=dao.getDataList(BDCS_DJDY_LS.class, xmbhHql);
			BDCS_DJDY_GZ gz=null;
			if(djdys_ls!=null&&djdys_ls.size()>0) {
				for(BDCS_DJDY_LS ls:djdys_ls) {
					try {
						
						if(ls.getBDCDYLX()!=null&&!"".equals(ls.getBDCDYLX())&&"02".equals(ls.getBDCDYLX())) {
							gz=new BDCS_DJDY_GZ();
							PropertyUtils.copyProperties(gz, ls);
						}
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						e.printStackTrace();
					} catch (NoSuchMethodException e) {
						e.printStackTrace();
					}
					if(ls!=null) {
						djdys.add(gz);
					}
				}
			} 
		}
	  
		
		try {
			if (djdys != null && djdys.size() > 0) {
				mashaller = JAXBContext.newInstance(Message.class).createMarshaller();
				Calendar calendar = Calendar.getInstance(); // 创建一个日历对象
				calendar.setTime(new Date()); // 用当前时间初始化日历时间
				String cyear = calendar.get(Calendar.YEAR) + "";
				
				for (int i = 0; i < djdys.size(); i++) {
					BDCS_DJDY_GZ djdy = djdys.get(i);
					String ywh = packageXml.GetYWLSHByYWH(this.getProject_id());
					BDCS_QL_GZ ql = this.getQL(djdy.getDJDYID());
					// List<BDCS_QLR_GZ> qlrs = super.getQLRs(ql.getId());
					List<BDCS_SQR> sqrs = new ArrayList<BDCS_SQR>();
					 msg = exchangeFactory.createMessageByZXDJ();
					ProjectServiceImpl serviceImpl = SuperSpringContext.getContext().getBean(ProjectServiceImpl.class);
					sqrs = serviceImpl.getSQRList(this.getXMBH());

					BDCS_XMXX xmxx = dao.get(BDCS_XMXX.class, this.getXMBH());
					BDCS_H_XZ h = null;
					System.out.println(this.getQllx().Value);
					System.out.println(this.getBdcdylx().toString());
					if (djdy != null) {
						if (QLLX.GYJSYDSHYQ.Value.equals(this.getQllx().Value) || QLLX.ZJDSYQ.Value.equals(this.getQllx().Value) || QLLX.JTJSYDSYQ.Value.equals(this.getQllx().Value)
								||"SHYQZD".equals(this.getBdcdylx().toString())||"02".equals(djdy.getBDCDYLX())) { // 国有建设使用权、宅基地、集体建设用地使用权
							try {
								BDCS_SHYQZD_XZ zd = dao.get(BDCS_SHYQZD_XZ.class, djdy.getBDCDYID());
								
								if(zd != null && !StringUtils.isEmpty(zd.getQXDM())){
									msg.getHead().setAreaCode(zd.getQXDM());
								}
								this.fillHead(msg, i, ywh,zd.getBDCDYH(),zd.getQXDM(),ql.getLYQLID());
								Rights rights = ql;
								QLFZXDJ zxdj = msg.getData().getZXDJ();
								SubRights subrights=RightsTools.loadSubRights(DJDYLY.GZ, ql.getFSQLID());
								zxdj = packageXml.getZXDJ(zxdj, rights, ywh, zd.getQXDM(),subrights);

								DJTDJSLSQ sq = msg.getData().getDJSLSQ();
								sq = packageXml.getDJTDJSLSQ(sq, ywh, zd, ql, xmxx, null, xmxx.getSLSJ(), null, null, null);

								List<DJFDJSJ> sj = msg.getData().getDJSJ();
								sj = packageXml.getDJFDJSJ(zd, ywh,actinstID);
								msg.getData().setDJSJ(sj);

								List<DJFDJSF> sfList = msg.getData().getDJSF();
								sfList = packageXml.getDJSF(zd,ywh, this.getXMBH());
								msg.getData().setDJSF(sfList);

								List<DJFDJSH> sh = msg.getData().getDJSH();
								sh = packageXml.getDJFDJSH(h, ywh, this.getXMBH(), actinstID);
								msg.getData().setDJSH(sh);

								List<DJFDJSZ> sz = packageXml.getDJFDJSZ(zd, ywh, this.getXMBH());
								msg.getData().setDJSZ(sz);

								List<DJFDJFZ> fz = packageXml.getDJFDJFZ(zd,ywh, this.getXMBH());
								msg.getData().setDJFZ(fz);
								List<DJFDJGD> gd = packageXml.getDJFDJGD(zd,ywh,this.getXMBH());
								msg.getData().setDJGD(gd);
								msg.getHead().setParcelID(StringHelper.formatObject(zd.getZDDM()));
								msg.getHead().setEstateNum(StringHelper.formatObject(zd.getBDCDYH()));
//								msg.getHead().setPreEstateNum(StringHelper.formatObject(zd.getBDCDYH()));
								msg.getHead().setRecType("4000101");
								if(index!=null) {
									//组合流程, 业务号增加下标
									msg.getHead().setRecFlowID(msg.getHead().getRecFlowID()+"_"+index);
								} 
								List<DJFDJSQR> djsqrs = msg.getData().getDJSQR();
								djsqrs = packageXml.getDJSQRs(djsqrs, sqrs, zd.getYSDM(), ywh, zd.getBDCDYH());
								msg.getData().setDJSQR(djsqrs);

								FJF100 fj = msg.getData().getFJF100();
								fj = packageXml.getFJF(fj);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
						if ((QLLX.GYJSYDSHYQ_FWSYQ.Value.equals(this.getQllx().Value)||"H".equals(this.getBdcdylx().toString()))&&!"02".equals(djdy.getBDCDYLX())) { 
							// 房屋所有权
							try {
								h = dao.get(BDCS_H_XZ.class, djdy.getBDCDYID());
								if(h != null && h.getZDBDCDYID() != null){
									BDCS_SHYQZD_XZ zd = dao.get(BDCS_SHYQZD_XZ.class, h.getZDBDCDYID());
									h.setZDDM(zd.getZDDM());
								}
								if(h != null && !StringUtils.isEmpty(h.getQXDM())){
									msg.getHead().setAreaCode(h.getQXDM());
								}
								this.fillHead(msg, i, ywh,h.getBDCDYH(),h.getQXDM(),ql.getLYQLID());
								Rights rights = ql;
								QLFZXDJ zxdj = msg.getData().getZXDJ();
								SubRights subrights=RightsTools.loadSubRights(DJDYLY.GZ, ql.getFSQLID());
								zxdj = packageXml.getZXDJ(zxdj, rights, ywh, h.getQXDM(),subrights);
								DJTDJSLSQ sq = msg.getData().getDJSLSQ();
								sq = packageXml.getDJTDJSLSQ(sq, ywh, null, ql, xmxx, h, xmxx.getSLSJ(), null, null, null);

								List<DJFDJSJ> sj = msg.getData().getDJSJ();
								sj = packageXml.getDJFDJSJ(h, ywh,actinstID);
								msg.getData().setDJSJ(sj);

								List<DJFDJSF> sfList = msg.getData().getDJSF();
								sfList = packageXml.getDJSF(h,ywh,this.getXMBH());
								msg.getData().setDJSF(sfList);

								List<DJFDJSH> sh = msg.getData().getDJSH();
								sh = packageXml.getDJFDJSH(h, ywh, this.getXMBH(), actinstID);
								msg.getData().setDJSH(sh);

								List<DJFDJSZ> sz = packageXml.getDJFDJSZ(h, ywh, this.getXMBH());
								msg.getData().setDJSZ(sz);

								List<DJFDJFZ> fz = packageXml.getDJFDJFZ(h,ywh, this.getXMBH());
								msg.getData().setDJFZ(fz);
								List<DJFDJGD> gd = packageXml.getDJFDJGD(h, ywh, this.getXMBH());
								List<DJFDJSQR> djsqrs = msg.getData().getDJSQR();
								djsqrs = packageXml.getDJSQRs(djsqrs, sqrs, h.getYSDM(), ywh, h.getBDCDYH());
								msg.getData().setDJGD(gd);

								msg.getData().setDJSQR(djsqrs);
								FJF100 fj = msg.getData().getFJF100();
								fj = packageXml.getFJF(fj);
								
								
								msg.getHead().setParcelID(StringHelper.formatObject(h.getZDDM()));
								msg.getHead().setEstateNum(StringHelper.formatObject(h.getBDCDYH()));
//								msg.getHead().setPreEstateNum(StringHelper.formatObject(h.getBDCDYH()));
								msg.getHead().setRecType("4000101");
								if(index!=null) {
									//组合流程, 业务号增加下标
									msg.getHead().setRecFlowID(msg.getHead().getRecFlowID()+"_"+index);
								} 
								
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
						if (QLLX.HYSYQ.Value.equals(this.getQllx().Value) || QLLX.WJMHDSYQ.Value.equals(this.getQllx().Value)) { 
							// 海域(含无居民海岛)使用权注销
							 // 海域(含无居民海岛)使用权
							String zhdm=null;
							String hql=null;
							YHZK yhzk_gz = null;
							Sea zh = dao.get(BDCS_ZH_GZ.class, djdy.getBDCDYID());
							if (zh==null) {
								zh=dao.get(BDCS_ZH_XZ.class, djdy.getBDCDYID());
							}
							if (null != zh) {
							 zhdm=zh.getZHDM();
							 hql = "BDCDYID = '" + zh.getId() + "' ";
								List<BDCS_YHZK_GZ> yhzks = dao.getDataList(BDCS_YHZK_GZ.class, hql);
								if (yhzks != null && yhzks.size() > 0) {
									yhzk_gz = yhzks.get(0);
								}else {
									List<BDCS_YHZK_XZ> yhzksxz = dao.getDataList(BDCS_YHZK_XZ.class, hql);
									if (yhzksxz != null && yhzksxz.size() > 0) {
										yhzk_gz = yhzksxz.get(0);
									}
								}
							}
							if(zhdm!=null&&zhdm.length()>0&&(yhzk_gz.getZHDM()==null)||yhzk_gz.getZHDM().length()>0) {
								//维护数据
								yhzk_gz.setZHDM(zhdm);
							}
							// 这些字段先手动赋值 diaoliwei
							if (zh != null) {
								if (StringUtils.isEmpty(zh.getZHT())) {
									zh.setZHT("无");
								}
							}
								//设置报文头
								this.fillHead(msg, i, ywh,zh.getBDCDYH(),zh.getQXDM(),ql.getLYQLID());
								msg.getHead().setParcelID(StringHelper.formatObject(zh.getZHDM()));
								msg.getHead().setEstateNum(StringHelper.formatObject(zh.getBDCDYH()));
//								msg.getHead().setPreEstateNum(StringHelper.formatObject(zh.getBDCDYH()));
								//2.非结构化文档
								FJF100 fj = msg.getData().getFJF100();
								fj = packageXml.getFJF(fj);
								msg.getData().setFJF100(fj);

								//7.登记受理信息表
								DJTDJSLSQ sq = msg.getData().getDJSLSQ();
								sq = packageXml.getDJTDJSLSQ(sq, ywh, null, ql, xmxx, null, xmxx.getSLSJ(), null, null, zh);
								msg.getData().setDJSLSQ(sq);

								//8.登记收件(可选)
								List<DJFDJSJ> sj = msg.getData().getDJSJ();
								sj = packageXml.getDJFDJSJ(zh, ywh,actinstID);
								msg.getData().setDJSJ(sj);

								//9.登记收费(可选)
								List<DJFDJSF> sfList = msg.getData().getDJSF();
								sfList = packageXml.getDJSF(zh, ywh,this.getXMBH());
								msg.getData().setDJSF(sfList);

								//10.审核(可选)
								List<DJFDJSH> sh = msg.getData().getDJSH();
								sh = packageXml.getDJFDJSH(h, ywh, this.getXMBH(), actinstID);
								msg.getData().setDJSH(sh);

								//11.缮证(可选)
								List<DJFDJSZ> sz = packageXml.getDJFDJSZ(zh, ywh, this.getXMBH());
								msg.getData().setDJSZ(sz);

								//11.发证(可选)
								List<DJFDJFZ> fz = packageXml.getDJFDJFZ(zh,ywh, this.getXMBH());
								msg.getData().setDJFZ(fz);
								
								//12.归档(可选)
								List<DJFDJGD> gd = packageXml.getDJFDJGD(zh, ywh,this.getXMBH());
								msg.getData().setDJGD(gd);
								
								//13.申请人属性
								List<DJFDJSQR> djsqrs = msg.getData().getDJSQR();
								djsqrs = packageXml.getDJSQRs(djsqrs, sqrs, zh.getYSDM(), ywh, zh.getBDCDYH());
								msg.getData().setDJSQR(djsqrs);
						        //注销登记
								Rights rights = ql;
								QLFZXDJ zxdj = msg.getData().getZXDJ();
								SubRights subrights=RightsTools.loadSubRights(DJDYLY.GZ, ql.getFSQLID());
								zxdj = packageXml.getZXDJ(zxdj, rights, ywh, zh.getQXDM(),subrights);
								msg.getData().setZXDJ(zxdj);
								msg.getHead().setRecType("4000101");
								if(index!=null) {
									//组合流程, 业务号增加下标
									msg.getHead().setRecFlowID(msg.getHead().getRecFlowID()+"_"+index);
								} 
						}
					}
					String msgName = getMessageFileName( msg,  i ,djdys.size(),names,djdy);
					mashaller.marshal(msg, new File(path +msgName));
					result = uploadFile(path +msgName, ConstValue.RECCODE.YY_ZXDJ.Value, actinstID, djdy.getDJDYID(), ql.getId());
					names.put(djdy.getDJDYID(), path +msgName);
					
				}
			}
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		if(null == result){
			Map<String, String> xmlError = new HashMap<String, String>();
			xmlError.put("error", "连接SFTP失败,请检查服务器和前置机的连接是否正常");
			YwLogUtil.addSjsbResult("Biz" + msg.getHead().getBizMsgID() + ".xml", "", "连接SFTP失败,请检查服务器和前置机的连接是否正常", ConstValue.SF.NO.Value, ConstValue.RECCODE.ZX_ZXDJ.Value,ProjectHelper.getpRroinstIDByActinstID(actinstID));
			//return xmlError;
		}
		if(!"1".equals(result) && result.indexOf("success") == -1){ //xml本地校验不通过时
			Map<String, String> xmlError = new HashMap<String, String>();
			xmlError.put("error", result);
			//return xmlError;
		}
		if(!StringUtils.isEmpty(result) && result.indexOf("success") > -1 && !names.containsKey("reccode")){
			names.put("reccode", result);
		}
		return names;
	}
	/**
	 * 
	  * 查封上报    
	 * @param path
	 * @param actinstID
	 * @param String index 组合流程下标
	 * @return
	 */
	
	public Map<String, String> exportXML_CF(String path, String actinstID,String index) {
		String result = "";
		Marshaller mashaller;
		Map<String, String> names = new HashMap<String,String>();
		try {
			Message msg=null;
			CommonDao dao = this.getCommonDao();
			String xmbhHql = ProjectHelper.GetXMBHCondition(this.getXMBH());
			List<BDCS_DJDY_GZ> djdys = dao.getDataList(BDCS_DJDY_GZ.class, xmbhHql);
			if (djdys != null && djdys.size() > 0) {
				/*
				 * 1.房屋
				 */
//				System.out.println("555555555555555555555555555555555555555555555555:");
//				System.out.println(this.getQllx().Value);
//				System.out.println(this.getBdcdylx());
				
				if (QLLX.GYJSYDSHYQ_FWSYQ.Value.equals(this.getQllx().Value)||"H".equals(this.getBdcdylx().toString())) { 
					mashaller = JAXBContext.newInstance(Message.class).createMarshaller();
					Calendar calendar = Calendar.getInstance(); // 创建一个日历对象
					calendar.setTime(new Date()); // 用当前时间初始化日历时间
					String cyear = calendar.get(Calendar.YEAR) + "";
					String ywh = packageXml.GetYWLSHByYWH(this.getProject_id());
					BDCS_XMXX xmxx = dao.get(BDCS_XMXX.class, this.getXMBH());
					
					for (int i = 0; i < djdys.size(); i++) {
						BDCS_DJDY_GZ djdy = djdys.get(i);
						BDCS_QL_GZ ql = this.getQL(djdy.getDJDYID());
						List<BDCS_SQR> sqrs = new ArrayList<BDCS_SQR>();
						ProjectServiceImpl serviceImpl = SuperSpringContext.getContext().getBean(ProjectServiceImpl.class);
						sqrs = serviceImpl.getSQRList(this.getXMBH());
				 
						BDCS_FSQL_GZ fsql = null;
						if (ql != null && !StringUtils.isEmpty(ql.getFSQLID())) {
							fsql = dao.get(BDCS_FSQL_GZ.class, ql.getFSQLID());
						}

						BDCS_H_XZ h = dao.get(BDCS_H_XZ.class, djdy.getBDCDYID());
						if(h != null){
							BDCS_SHYQZD_XZ zd = dao.get(BDCS_SHYQZD_XZ.class, h.getZDBDCDYID());
							if(zd != null){
								h.setZDDM(zd.getZDDM());
							}
						}
						msg = exchangeFactory.createMessageByCFDJ();
						this.fillHead(msg, i, ywh,h.getBDCDYH(),h.getQXDM(),ql.getLYQLID());
						msg.getHead().setParcelID(StringHelper.formatObject(h.getZDDM()));
						msg.getHead().setEstateNum(StringHelper.formatObject(h.getBDCDYH()));
//						msg.getHead().setPreEstateNum(StringHelper.formatObject(h.getBDCDYH()));
						if(h != null && !StringUtils.isEmpty(h.getQXDM())){
							msg.getHead().setAreaCode(h.getQXDM());
						}
						if (djdy != null) {
							try {
								QLFQLCFDJ cfdj = msg.getData().getQLFQLCFDJ();
								cfdj = packageXml.getQLFQLCFDJ(cfdj,null, h, ql, fsql, ywh,null);
								
								DJTDJSLSQ sq = msg.getData().getDJSLSQ();
								sq = packageXml.getDJTDJSLSQ(sq, ywh, null, ql, xmxx, h, xmxx.getSLSJ(), null, null, null);
								
								List<DJFDJSJ> sj = msg.getData().getDJSJ();
								sj = packageXml.getDJFDJSJ(h, ywh,actinstID);
								msg.getData().setDJSJ(sj);

								List<DJFDJSF> sfList = msg.getData().getDJSF();
								sfList = packageXml.getDJSF(h,ywh,this.getXMBH());
								msg.getData().setDJSF(sfList);
								
								List<DJFDJSH> sh = msg.getData().getDJSH();
								sh = packageXml.getDJFDJSH(h, ywh, this.getXMBH(), actinstID);
								msg.getData().setDJSH(sh);
								
								List<DJFDJSZ> sz = packageXml.getDJFDJSZ(h, ywh, this.getXMBH());
								msg.getData().setDJSZ(sz);		
								
								List<DJFDJFZ> fz = packageXml.getDJFDJFZ(h,ywh,this.getXMBH());
								msg.getData().setDJFZ(fz);	
								List<DJFDJGD> gd = packageXml.getDJFDJGD(h ,ywh,this.getXMBH());
								msg.getData().setDJGD(gd);	
								List<DJFDJSQR> djsqrs = msg.getData().getDJSQR();
								//查封现在的不填写申请人，为了和接入规范一致，先在这里拼凑出申请人。
								DJFDJSQR djsqr = new DJFDJSQR();
								djsqr.setYsdm("2004020000");
								djsqr.setQlrmc(cfdj == null ? "无" :StringHelper.formatDefaultValue(cfdj.getCfjg()));
								djsqr.setYwh(ywh);
//								djsqr.setQxdm(ConfigHelper.getNameByValue("XZQHDM"));
								djsqrs.add(djsqr);
								djsqrs = packageXml.getDJSQRs(djsqrs, sqrs, h.getYSDM(), ywh, h.getBDCDYH());
								msg.getData().setDJSQR(djsqrs);
								FJF100 fj = msg.getData().getFJF100();
								fj = packageXml.getFJF(fj);
								if(index!=null) {
									//组合流程, 业务号增加下标
									msg.getHead().setRecFlowID(msg.getHead().getRecFlowID()+"_"+index);
								} 
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
						mashaller.marshal(msg, new File(path + "Biz" + msg.getHead().getBizMsgID() + ".xml"));
						result = this.uploadFile(path + "Biz" + msg.getHead().getBizMsgID() + ".xml",ConstValue.RECCODE.CF_CFDJ.Value,actinstID,djdy.getDJDYID(),ql.getId());
						names.put(djdy.getDJDYID(), msg.getHead().getBizMsgID()+ ".xml");
						
						if(result.equals("")|| result==null){
							Map<String, String> xmlError = new HashMap<String, String>();
							xmlError.put("error", "连接SFTP失败,请检查服务器和前置机的连接是否正常");
							YwLogUtil.addSjsbResult("Biz" + msg.getHead().getBizMsgID() + ".xml", "", "连接SFTP失败,请检查服务器和前置机的连接是否正常", ConstValue.SF.NO.Value, ConstValue.RECCODE.CF_CFDJ.Value,ProjectHelper.getpRroinstIDByActinstID(actinstID));
							return xmlError;
						}
						if(!"1".equals(result) && result.indexOf("success") == -1){ //xml本地校验不通过时
							Map<String, String> xmlError = new HashMap<String, String>();
							xmlError.put("error", result);
							return xmlError;
						}
						if(!StringUtils.isEmpty(result) && result.indexOf("success") > -1 && !names.containsKey("reccode")){
							names.put("reccode", result);
						}
					}
				}else {
					/*
					 * 2.宗地
					 */

					mashaller = JAXBContext.newInstance(Message.class).createMarshaller();
					Calendar calendar = Calendar.getInstance(); // 创建一个日历对象
					calendar.setTime(new Date()); // 用当前时间初始化日历时间
					String cyear = calendar.get(Calendar.YEAR) + "";
					String ywh = packageXml.GetYWLSHByYWH(this.getProject_id());
					BDCS_XMXX xmxx = dao.get(BDCS_XMXX.class, this.getXMBH());
					msg = exchangeFactory.createMessageByCFDJ();
					for (int i = 0; i < djdys.size(); i++) {
						BDCS_DJDY_GZ djdy = djdys.get(i);
						BDCS_QL_GZ ql = this.getQL(djdy.getDJDYID());
						BDCS_FSQL_GZ fsql = null;
						List<BDCS_SQR> sqrs = new ArrayList<BDCS_SQR>();
						ProjectServiceImpl serviceImpl = SuperSpringContext.getContext().getBean(ProjectServiceImpl.class);
						sqrs = serviceImpl.getSQRList(this.getXMBH());
						
						if (ql != null && !StringUtils.isEmpty(ql.getFSQLID())) {
							fsql = dao.get(BDCS_FSQL_GZ.class, ql.getFSQLID());
						}
						if (this.getBdcdylx()!=null&&"NYD".equals(this.getBdcdylx().toString())) {
							 //农用地
							RealUnit unit=UnitTools.loadUnit(BDCDYLX.NYD, DJDYLY.GZ, djdy.getBDCDYID());
	                    	if(unit==null) {
	                    		unit=UnitTools.loadUnit(BDCDYLX.NYD, DJDYLY.XZ, djdy.getBDCDYID());
	                    	}
	                    	AgriculturalLand nyd=(AgriculturalLand)unit;
	                    	this.fillHead(msg, i, ywh,nyd.getBDCDYH(),nyd.getQXDM(),ql.getLYQLID());
	                    	msg.getHead().setParcelID(StringHelper.formatObject(nyd.getZDDM()));
							msg.getHead().setEstateNum(StringHelper.formatObject(nyd.getBDCDYH()));
							msg.getHead().setRecType("8000101");
//							msg.getHead().setPreEstateNum(StringHelper.formatObject(nyd.getBDCDYH()));
	                    	//查封登记
	                    	QLFQLCFDJ cfdj = msg.getData().getQLFQLCFDJ();
							cfdj = packageXml.getQLFQLCFDJ(cfdj, null, null, ql, fsql, ywh,null);
							//维护
							if((cfdj.getBdcdyh()==null||cfdj.getBdcdyh().length()<=0)&&ql!=null&&ql.getBDCDYH()!=null&&ql.getBDCDYH().length()>0) {
								cfdj.setBdcdyh(ql.getBDCDYH());
							}
							cfdj.setQxdm(StringHelper.formatObject(nyd.getQXDM()) == "" ? ConfigHelper.getNameByValue("XZQHDM")
									: nyd.getQXDM());
							cfdj.setBdcdyh(StringHelper.formatDefaultValue(nyd.getBDCDYH()));
							msg.getData().setQLFQLCFDJ(cfdj);
							// SLSQ 受理申请
							DJTDJSLSQ sq = msg.getData().getDJSLSQ();
							sq = packageXml.getDJTDJSLSQByNYD(sq, nyd, ql, xmxx);
							msg.getData().setDJSLSQ(sq);
							// SJ 收件
							List<DJFDJSJ> sj = msg.getData().getDJSJ();
							sj = packageXml.getDJFDJSJ(nyd, ywh,actinstID);
							for(DJFDJSJ d:sj) {
								d.setYSDM("6002020400");
							}
							msg.getData().setDJSJ(sj);
							 // SF 收费
							List<DJFDJSF> sfList = msg.getData().getDJSF();
							sfList = packageXml.getDJSF(nyd, ywh,this.getXMBH());
							for(DJFDJSF d :sfList ) {
								d.setYSDM("6002020400");
								d.setQXDM(StringHelper.formatObject(nyd.getQXDM()));
							}
							msg.getData().setDJSF(sfList);
							 // SH 审核
							List<DJFDJSH> sh = msg.getData().getDJSH();
							sh = packageXml.getDJFDJSH(nyd, ywh, this.getXMBH(), actinstID);
							for(DJFDJSH d:sh) {
								d.setYSDM("6002020400");
							} 
							msg.getData().setDJSH(sh);
					        // SZ 缮证 
							List<DJFDJSZ> sz = packageXml.getDJFDJSZ(nyd, ywh, this.getXMBH());
					        // FZ 发证
							List<DJFDJFZ> fz = packageXml.getDJFDJFZ(nyd,ywh, this.getXMBH());
							// GD 归档
							List<DJFDJGD> gd = packageXml.getDJFDJGD(nyd,ywh,this.getXMBH());
							// SQR 申请人
							List<DJFDJSQR> djsqrs = msg.getData().getDJSQR();
							djsqrs = packageXml.getDJSQRs(djsqrs, sqrs, nyd.getYSDM(), ywh, nyd.getQXDM());
							if(nyd!=null) {
								for(DJFDJGD d:gd) {
									d.setYSDM("6002020400");
									d.setQXDM(StringHelper.formatObject(nyd.getQXDM()));
									d.setZL(StringHelper.formatObject(nyd.getZL()));
								}
								for(DJFDJFZ d:fz) {
									d.setYSDM("6002020400");
									d.setQXDM(StringHelper.formatObject(nyd.getQXDM()));
								}
								for(DJFDJSQR d:djsqrs) {
									d.setYsdm("6002020400");
								}
								
								for(DJFDJSZ d:sz) {
									d.setYSDM("6002020400");
									d.setQXDM(StringHelper.formatObject(nyd.getQXDM()));
								}
							}
							msg.getData().setDJSQR(djsqrs);
							msg.getData().setDJSZ(sz);
							msg.getData().setDJFZ(fz);
							msg.getData().setDJGD(gd);
							// FJ 非结    （结构化文档）
							FJF100 fj = msg.getData().getFJF100();
							fj = packageXml.getFJF(fj);
							msg.getData().setFJF100(fj);
							 
						 }else if(this.getBdcdylx()!=null&&"HY".equals(this.getBdcdylx().toString())){
							 //海域
							 BDCS_ZH_XZ zh = dao.get(BDCS_ZH_XZ.class, djdy.getBDCDYID());//宗海基本信息
								//BDCS_ZH_XZ
							 this.fillHead(msg, i, ywh,zh.getBDCDYH(),zh.getQXDM(),ql.getLYQLID());
								msg.getHead().setParcelID(StringHelper.formatObject(zh.getZHDM()));
								msg.getHead().setEstateNum(StringHelper.formatObject(zh.getBDCDYH()));
								msg.getHead().setRecType("8000101");
//								msg.getHead().setPreEstateNum(StringHelper.formatObject(zh.getBDCDYH()));
								if(zh != null && !StringUtils.isEmpty(zh.getQXDM())){
									msg.getHead().setAreaCode(zh.getQXDM());
								}
								if (djdy != null) {
									try {
										QLFQLCFDJ cfdj = msg.getData().getQLFQLCFDJ();
										cfdj = packageXml.getQLFQLCFDJ(cfdj, null, null, ql, fsql, ywh,zh);
										//维护不动产单元号
										if((cfdj.getBdcdyh()==null||cfdj.getBdcdyh().length()<=0)&&ql!=null&&ql.getBDCDYH()!=null&&ql.getBDCDYH().length()>0) {
											cfdj.setBdcdyh(ql.getBDCDYH());
										}
										msg.getData().setQLFQLCFDJ(cfdj);
										
										DJTDJSLSQ sq = msg.getData().getDJSLSQ();
										sq = packageXml.getDJTDJSLSQ(sq, ywh, null, ql, xmxx, null, xmxx.getSLSJ(), null, null, zh);
										msg.getData().setDJSLSQ(sq);
										
										List<DJFDJSJ> sj = msg.getData().getDJSJ();
										sj = packageXml.getDJFDJSJ(zh, ywh,actinstID);
										msg.getData().setDJSJ(sj);

										List<DJFDJSF> sfList = msg.getData().getDJSF();
										sfList = packageXml.getDJSF(zh, ywh,this.getXMBH());
										msg.getData().setDJSF(sfList);
										
										List<DJFDJSH> sh = msg.getData().getDJSH();
										sh = packageXml.getDJFDJSH(zh, ywh, this.getXMBH(), actinstID);
										msg.getData().setDJSH(sh);

										List<DJFDJSZ> sz = packageXml.getDJFDJSZ(zh, ywh, this.getXMBH());		
										msg.getData().setDJSZ(sz);
										List<DJFDJFZ> fz = packageXml.getDJFDJFZ(zh,ywh,this.getXMBH());
										msg.getData().setDJFZ(fz);
//										List<DJFDJGD>  gd = packageXml.getDJFDJGD(sllm, zd, null ,ywh, null, null,this.getXMBH());
//										msg.getData().setDJGD(gd);	
										List<DJFDJGD> gd = packageXml.getDJFDJGD(zh,ywh,this.getXMBH());
										msg.getData().setDJGD(gd);
										List<DJFDJSQR> djsqrs = msg.getData().getDJSQR();
										//查封现在的不填写申请人，为了和接入规范一致，先在这里拼凑出申请人。
										DJFDJSQR djsqr = new DJFDJSQR();
										djsqr.setYsdm("2004020000");
										djsqr.setQlrmc(cfdj == null ? "无" :StringHelper.formatDefaultValue(cfdj.getCfjg()));
										djsqr.setYwh(ywh);
//										djsqr.setQxdm(ConfigHelper.getNameByValue("XZQHDM"));
										djsqrs.add(djsqr);
										djsqrs = packageXml.getDJSQRs(djsqrs, sqrs, zh.getYSDM(), ywh, zh.getBDCDYH());
										msg.getData().setDJSQR(djsqrs);
										FJF100 fj = msg.getData().getFJF100();
										fj = packageXml.getFJF(fj);
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
							
							
							 
						 }else {
							 RealUnit unit=UnitTools.loadUnit(BDCDYLX.SHYQZD, DJDYLY.XZ, djdy.getBDCDYID());
								if (unit!=null) {
									//查询宗地信息
									UseLand zd=(UseLand)unit;
									this.fillHead(msg, i, ywh,zd.getBDCDYH(),zd.getQXDM(),ql.getLYQLID());
									msg.getHead().setParcelID(StringHelper.formatObject(zd.getZDDM()));
									msg.getHead().setEstateNum(StringHelper.formatObject(zd.getBDCDYH()));
									msg.getHead().setRecType("8000101");
//									msg.getHead().setPreEstateNum(StringHelper.formatObject(zd.getBDCDYH()));
									if(zd != null && !StringUtils.isEmpty(zd.getQXDM())){
										msg.getHead().setAreaCode(zd.getQXDM());
									}
									
									if (djdy != null) {
										try {
											QLFQLCFDJ cfdj = msg.getData().getQLFQLCFDJ();
											cfdj = packageXml.getQLFQLCFDJ(cfdj, zd, null, ql, fsql, ywh,null);

											DJTDJSLSQ sq = msg.getData().getDJSLSQ();
											sq = packageXml.getDJTDJSLSQ(sq, ywh, zd, ql, xmxx, null, xmxx.getSLSJ(), null, null, null);
											
											List<DJFDJSJ> sj = msg.getData().getDJSJ();
											sj = packageXml.getDJFDJSJ(zd, ywh,actinstID);
											msg.getData().setDJSJ(sj);

											List<DJFDJSF> sfList = msg.getData().getDJSF();
											sfList = packageXml.getDJSF(zd,ywh,this.getXMBH());
											msg.getData().setDJSF(sfList);
											
											List<DJFDJSH> sh = msg.getData().getDJSH();
											sh = packageXml.getDJFDJSH(zd, ywh, this.getXMBH(), actinstID);
											msg.getData().setDJSH(sh);

											List<DJFDJSZ> sz = packageXml.getDJFDJSZ(zd, ywh, this.getXMBH());		
											msg.getData().setDJSZ(sz);
											List<DJFDJFZ> fz = packageXml.getDJFDJFZ(zd,ywh,this.getXMBH());
											msg.getData().setDJFZ(fz);
											List<DJFDJGD>  gd = packageXml.getDJFDJGD(zd,ywh,this.getXMBH());
											msg.getData().setDJGD(gd);	
											List<DJFDJSQR> djsqrs = msg.getData().getDJSQR();
											//查封现在的不填写申请人，为了和接入规范一致，先在这里拼凑出申请人。
											DJFDJSQR djsqr = new DJFDJSQR();
											djsqr.setYsdm("2004020000");
											djsqr.setQlrmc(cfdj == null ? "无" :StringHelper.formatDefaultValue(cfdj.getCfjg()));
											djsqr.setYwh(ywh);
//											djsqr.setQxdm(ConfigHelper.getNameByValue("XZQHDM"));
											djsqrs.add(djsqr);
											djsqrs = packageXml.getDJSQRs(djsqrs, sqrs, zd.getYSDM(), ywh, zd.getBDCDYH());
											msg.getData().setDJSQR(djsqrs);
											
											FJF100 fj = msg.getData().getFJF100();
											fj = packageXml.getFJF(fj);
										} catch (Exception e) {
											e.printStackTrace();
										}
									}
								} 
						 }
//						List<BDCS_QLR_GZ> qlrs = super.getQLRs(ql.getId());
						
						
						String msgName = getMessageFileName( msg,  i ,djdys.size(),names,djdy);
						mashaller.marshal(msg, new File(path +msgName));
						result = this.uploadFile(path +msgName, this.getRecType(), actinstID, djdy.getDJDYID(), ql.getId());
						if(result.equals("")|| result==null){
							Map<String, String> xmlError = new HashMap<String, String>();
							xmlError.put("error", "连接SFTP失败,请检查服务器和前置机的连接是否正常");
							YwLogUtil.addSjsbResult("Biz" + msg.getHead().getBizMsgID() + ".xml", "", "连接SFTP失败,请检查服务器和前置机的连接是否正常", ConstValue.SF.NO.Value, ConstValue.RECCODE.CF_CFDJ.Value,ProjectHelper.getpRroinstIDByActinstID(actinstID));
							return xmlError;
						}
						if(!"1".equals(result) && result.indexOf("success") == -1){ //xml本地校验不通过时
							Map<String, String> xmlError = new HashMap<String, String>();
							xmlError.put("error", result);
							return xmlError;
						}
						if(!StringUtils.isEmpty(result) && result.indexOf("success") > -1 && !names.containsKey("reccode")){
							names.put("reccode", result);
						}
					}
				
				}
				}
		
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return names;
	}
	/**
	 * 
	  * 更正上报    
	 * @param path
	 * @param actinstID
	 * @param String index 组合流程下标
	 * @return
	 */
	
	public Map<String, String> exportXML_GZ(String path, String actinstID,String index) {

		if("LD".equals(this.getBdcdylx().toString())||"HY".equals(this.getBdcdylx().toString())){
			return this.exportXMLother(path, actinstID,"YES");
		}
		if("NYD".equals(this.getBdcdylx().toString())){
			return this.exportXML(path, actinstID);
		}
		Marshaller mashaller;
		Map<String, String> names = new HashMap<String, String>();
		try {
			CommonDao dao = this.getCommonDao();
			String xmbhHql = ProjectHelper.GetXMBHCondition(this.getXMBH());
			List<BDCS_DJDY_GZ> djdys = dao.getDataList(BDCS_DJDY_GZ.class,
					xmbhHql);
			if (djdys != null && djdys.size() > 0) {
				mashaller = JAXBContext.newInstance(Message.class)
						.createMarshaller();
				Calendar calendar = Calendar.getInstance(); // 创建一个日历对象
				calendar.setTime(new Date()); // 用当前时间初始化日历时间
				String cyear = calendar.get(Calendar.YEAR) + "";
				BDCS_XMXX xmxx = dao.get(BDCS_XMXX.class, this.getXMBH());
				String result = "";
				String msgName = "";
				for (int i = 0; i < djdys.size(); i++) {
					BDCS_DJDY_GZ djdy = djdys.get(i);
					String ywh = packageXml.GetYWLSHByYWH(this.getProject_id());
					BDCS_QL_GZ ql = this.getQL(djdy.getDJDYID());
					List<BDCS_QLR_GZ> qlrs = this.getQLRs(ql.getId());
					List<BDCS_SQR> sqrs = new ArrayList<BDCS_SQR>();
					ProjectServiceImpl serviceImpl = SuperSpringContext
							.getContext().getBean(ProjectServiceImpl.class);
					sqrs = serviceImpl.getSQRList(this.getXMBH());

					if (QLLX.GYJSYDSHYQ.Value.equals(this.getQllx().Value)
							|| QLLX.ZJDSYQ.Value.equals(this.getQllx().Value) || QLLX.JTJSYDSYQ.Value.equals(this.getQllx().Value)) { // 国有建设使用权、宅基地、集体建设用地使用权
						try {
//							BDCS_SHYQZD_GZ zd = dao.get(BDCS_SHYQZD_GZ.class,
//									djdy.getBDCDYID());
							UseLand zd=(UseLand)UnitTools.loadUnit(BDCDYLX.SHYQZD,DJDYLY.GZ, djdy.getBDCDYID());
							Message msg = exchangeFactory.createMessageBySHYQ();
							BDCS_DYBG dybg = null;
							if (DJLX.BGDJ.Value.equals(this.getDjlx().Value)) {
								msg = exchangeFactory.createMessage("true");
								dybg = packageXml.getDYBG(zd.getId());
							}
							this.fillHead(msg, i, ywh,zd.getBDCDYH(),zd.getQXDM(),ql.getLYQLID());
							msg.getHead().setParcelID(
									StringHelper.formatObject(zd.getZDDM()));
							msg.getHead().setEstateNum(
									StringHelper.formatObject(zd.getBDCDYH()));
							// msg.getHead().setRecType("RT110");
//							msg.getHead().setPreEstateNum(
//									StringHelper.formatObject(zd.getBDCDYH()));
							if(index!=null) {
								//组合流程, 业务号增加下标
								msg.getHead().setRecFlowID(msg.getHead().getRecFlowID()+"_"+index);
							} 
							if(zd != null && !StringUtils.isEmpty(zd.getQXDM())){
								msg.getHead().setAreaCode(zd.getQXDM());
							}
							// 目前未维护的字段先手动赋值 diaoliwei 2015-10-15
							if (null != zd) {
								if (StringUtils.isEmpty(zd.getZDT())) {
									zd.setZDT("无");
								}
							}
							if (djdy != null) {
								this.fillShyqZdData(msg,ywh,zd,sqrs,qlrs,ql,xmxx,actinstID);
								if (DJLX.BGDJ.Value.equals(this.getDjlx().Value)) {
									RealUnit unit=UnitTools.loadUnit(this.getBdcdylx(), DJDYLY.XZ, dybg.getXBDCDYID());
									List<ZDK103> zdk = msg.getData().getZDK103();
									zdk = packageXml.getZDK103(zdk, (UseLand)unit, null, null);
									msg.getData().setZDK103(zdk);
								}else if(DJLX.GZDJ.Value.equals(this.getDjlx().Value)){
									if(zd.getTXWHTYPE().equals("1")){
										RealUnit unit=UnitTools.loadUnit(this.getBdcdylx(), DJDYLY.XZ, dybg.getXBDCDYID());
										List<ZDK103> zdk = msg.getData().getZDK103();
										zdk = packageXml.getZDK103(zdk, (UseLand)unit, null, null);
										msg.getData().setZDK103(zdk);
									}
								}
							}
							msgName = "Biz" + msg.getHead().getBizMsgID() + ".xml";
							mashaller.marshal(msg, new File(path + msgName));
							if (DJLX.BGDJ.Value.equals(this.getDjlx().Value)){
								result = uploadFile(path + msgName, ConstValue.RECCODE.JSYDSHYQ_BGDJ.Value,actinstID,djdy.getDJDYID(),ql.getId());
							}else{
								result = uploadFile(path + msgName, ConstValue.RECCODE.JSYDSHYQ_GZDJ.Value,actinstID,djdy.getDJDYID(),ql.getId());
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
						
					}
					if (QLLX.GYJSYDSHYQ_FWSYQ.Value.equals(this.getQllx().Value)||QLLX.JTJSYDSYQ_FWSYQ.Value.equals(this.getQllx().Value)||QLLX.ZJDSYQ_FWSYQ.Value.equals(this.getQllx().Value)) {
						// 房屋所有权
						try {
							BDCS_H_GZ h = dao.get(BDCS_H_GZ.class,
									djdy.getBDCDYID());
							BDCS_ZRZ_XZ zrz_xz = null;
							if (h != null
									&& !StringUtils.isEmpty(h.getZRZBDCDYID())) {
								zrz_xz = dao.get(BDCS_ZRZ_XZ.class, h.getZRZBDCDYID());
								if(zrz_xz != null){
									zrz_xz.setGHYT(h.getGHYT()); //自然幢的ghyt取户的ghyt
									zrz_xz.setFWJG(zrz_xz.getFWJG() == null || zrz_xz.getFWJG().equals("") ? h.getFWJG() : zrz_xz.getFWJG());
								}
							}
							BDCS_LJZ_XZ ljz_xz = null;
							if (h != null && !StringUtils.isEmpty(h.getLJZID())) {
								ljz_xz = dao.get(BDCS_LJZ_XZ.class, h.getLJZID());
							}
							if (h != null) {
								BDCS_SHYQZD_XZ zd = dao.get(BDCS_SHYQZD_XZ.class, h.getZDBDCDYID());
								h.setZDDM(zd.getZDDM());
								if(zrz_xz != null){
									zrz_xz.setZDDM(zd.getZDDM());
								}
							}
							Message msg = exchangeFactory.createMessageByFWSYQ2();
							if (DJLX.BGDJ.Value.equals(this.getDjlx().Value)) {
								msg = exchangeFactory.createMessageByFWSYQ();
							}
							this.fillHead(msg, i, ywh,h.getBDCDYH(),h.getQXDM(),ql.getLYQLID());
							if(index!=null) {
								//组合流程, 业务号增加下标
								msg.getHead().setRecFlowID(msg.getHead().getRecFlowID()+"_"+index);
							} 
							msg.getHead().setParcelID(h.getZDDM());
							msg.getHead().setEstateNum(
									StringHelper.formatObject(h.getBDCDYH()));
							// msg.getHead().setRecType("RT160");
//							msg.getHead().setPreEstateNum(
//									StringHelper.formatObject(h.getBDCDYH()));
							if(h != null && !StringUtils.isEmpty(h.getQXDM())){
								msg.getHead().setAreaCode(h.getQXDM());
							}
							BDCS_C_GZ c = null;
							if (h != null && !StringUtils.isEmpty(h.getCID())) {
								c = dao.get(BDCS_C_GZ.class, h.getCID());
							}
							if (djdy != null) {

								this.fillFwData(msg, ywh ,ljz_xz, c ,zrz_xz, h ,sqrs,qlrs,ql,xmxx,actinstID);

							}
							msgName = "Biz" + msg.getHead().getBizMsgID() + ".xml";
							names.put(djdy.getDJDYID(), msg.getHead().getBizMsgID()
									+ ".xml");
							mashaller.marshal(msg, new File(path + msgName));
							if (DJLX.BGDJ.Value.equals(this.getDjlx().Value)){
								result = uploadFile(path + msgName, ConstValue.RECCODE.FDCQDZ_BGDJ.Value,actinstID,djdy.getDJDYID(),ql.getId());
							}else{
								result = uploadFile(path + msgName, ConstValue.RECCODE.FDCQDZ_GZDJ.Value,actinstID,djdy.getDJDYID(),ql.getId());
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
						
					}

					if (QLLX.JTTDSYQ.Value.equals(this.getQllx().Value)) { // 集体土地所有权
						try {
							BDCS_SYQZD_GZ oland = dao.get(BDCS_SYQZD_GZ.class,
									djdy.getBDCDYID());
							Message msg = exchangeFactory.createMessageByTDSYQ();
							this.fillHead(msg, i, ywh,oland.getBDCDYH(),oland.getQXDM(),ql.getLYQLID());
							msg.getHead().setParcelID(
									StringHelper.formatObject(oland.getZDDM()));
							msg.getHead().setEstateNum(
									StringHelper.formatObject(oland.getBDCDYH()));
							// msg.getHead().setRecType("RT100");
//							msg.getHead().setPreEstateNum(
//									StringHelper.formatObject(oland.getBDCDYH()));
							if(oland != null && !StringUtils.isEmpty(oland.getQXDM())){
								msg.getHead().setAreaCode(oland.getQXDM());
							}
							if (djdy != null) {

								this.fillSyqZdData( msg, ywh, oland, sqrs, qlrs, ql, xmxx, actinstID) ;
							}
							names.put(djdy.getDJDYID(), msg.getHead().getBizMsgID()
									+ ".xml");
							msgName = "Biz" + msg.getHead().getBizMsgID() + ".xml";
							mashaller.marshal(msg, new File(path + msgName));
							if (DJLX.BGDJ.Value.equals(this.getDjlx().Value)){
								result = uploadFile(path + msgName, ConstValue.RECCODE.TDSYQ_BGDJ.Value,actinstID,djdy.getDJDYID(),ql.getId());
							}else{
								result = uploadFile(path + msgName, ConstValue.RECCODE.TDSYQ_GZDJ.Value,actinstID,djdy.getDJDYID(),ql.getId());
							}
							if(index!=null) {
								//组合流程, 业务号增加下标
								msg.getHead().setRecFlowID(msg.getHead().getRecFlowID()+"_"+index);
							} 
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					if(result.equals("")||result==null){
						Map<String, String> xmlError = new HashMap<String, String>();
						xmlError.put("error", "连接SFTP失败,请检查服务器和前置机的连接是否正常");
						if (DJLX.BGDJ.Value.equals(this.getDjlx().Value)){
							if(QLLX.JTTDSYQ.Value.equals(this.getQllx().Value)){
								YwLogUtil.addSjsbResult(msgName, "", "连接SFTP失败,请检查服务器和前置机的连接是否正常", ConstValue.SF.NO.Value, ConstValue.RECCODE.TDSYQ_BGDJ.Value,ProjectHelper.getpRroinstIDByActinstID(actinstID));	
							}else if (QLLX.GYJSYDSHYQ_FWSYQ.Value.equals(this.getQllx().Value)){
								YwLogUtil.addSjsbResult(msgName, "", "连接SFTP失败,请检查服务器和前置机的连接是否正常", ConstValue.SF.NO.Value, ConstValue.RECCODE.FDCQDZ_BGDJ.Value,ProjectHelper.getpRroinstIDByActinstID(actinstID));
							}else {
								YwLogUtil.addSjsbResult(msgName, "", "连接SFTP失败,请检查服务器和前置机的连接是否正常", ConstValue.SF.NO.Value, ConstValue.RECCODE.JSYDSHYQ_BGDJ.Value,ProjectHelper.getpRroinstIDByActinstID(actinstID));
							}
						}else{
							if(QLLX.JTTDSYQ.Value.equals(this.getQllx().Value)){
								YwLogUtil.addSjsbResult(msgName, "", "连接SFTP失败,请检查服务器和前置机的连接是否正常", ConstValue.SF.NO.Value, ConstValue.RECCODE.TDSYQ_GZDJ.Value,ProjectHelper.getpRroinstIDByActinstID(actinstID));	
							}else if (QLLX.GYJSYDSHYQ_FWSYQ.Value.equals(this.getQllx().Value)){
								YwLogUtil.addSjsbResult(msgName, "", "连接SFTP失败,请检查服务器和前置机的连接是否正常", ConstValue.SF.NO.Value, ConstValue.RECCODE.FDCQDZ_GZDJ.Value,ProjectHelper.getpRroinstIDByActinstID(actinstID));
							}else {
								YwLogUtil.addSjsbResult(msgName, "", "连接SFTP失败,请检查服务器和前置机的连接是否正常", ConstValue.SF.NO.Value, ConstValue.RECCODE.JSYDSHYQ_GZDJ.Value,ProjectHelper.getpRroinstIDByActinstID(actinstID));
							}
						}
						return xmlError;
					}
					if(!"1".equals(result) && result.indexOf("success") == -1){ //xml本地校验不通过时
						Map<String, String> xmlError = new HashMap<String, String>();
						xmlError.put("error", result);
						return xmlError;
					}
					if(!StringUtils.isEmpty(result) && result.indexOf("success") > -1 && !names.containsKey("reccode")){
						names.put("reccode", result);
					}
				}
			}
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return names;
	}
	
	

	
	
	
	/**
	 * 
	  * 转移上报    
	 * @param path
	 * @param actinstID
	 * @param String index 组合流程下标
	 * @return
	 */
	
	public Map<String, String> exportXML_ZY(String path, String actinstID,String index) {

		if("LD".equals(this.getBdcdylx().toString())||"HY".equals(this.getBdcdylx().toString())){
			//林地和海域
			return this.exportXMLother(path, actinstID,"NO");
		}
		if("NYD".equals(this.getBdcdylx().toString())){
			//林地和海域
			return this.exportXML(path, actinstID);
		}
		Marshaller mashaller;
		Map<String, String> names = new HashMap<String, String>();
		try {
			CommonDao dao = this.getCommonDao();
			String xmbhHql = ProjectHelper.GetXMBHCondition(this.getXMBH());
			List<BDCS_DJDY_GZ> djdys = dao.getDataList(BDCS_DJDY_GZ.class, xmbhHql);
			if (djdys != null && djdys.size() > 0) {
				mashaller = JAXBContext.newInstance(Message.class).createMarshaller();
				//mashaller.setProperty(Marshaller.JAXB_ENCODING, "gbk");
				Calendar calendar = Calendar.getInstance(); // 创建一个日历对象
				calendar.setTime(new Date()); // 用当前时间初始化日历时间
				String cyear = calendar.get(Calendar.YEAR) + "";
				String ywh = packageXml.GetYWLSHByYWH(this.getProject_id());
				BDCS_XMXX xmxx = dao.get(BDCS_XMXX.class, this.getXMBH());
				String result = "";
				String msgName = "";
				for (int i = 0; i < djdys.size(); i++) {
					BDCS_DJDY_GZ djdy = djdys.get(i);
					BDCS_QL_GZ ql = this.getQL(djdy.getDJDYID());
					List<BDCS_QLR_GZ> qlrs = this.getQLRs(ql.getId());
					List<BDCS_SQR> sqrs = new ArrayList<BDCS_SQR>();
					ProjectServiceImpl serviceImpl = SuperSpringContext.getContext().getBean(ProjectServiceImpl.class);
					sqrs = serviceImpl.getSQRList(this.getXMBH());

					if (QLLX.GYJSYDSHYQ.Value.equals(this.getQllx().Value) || QLLX.ZJDSYQ.Value.equals(this.getQllx().Value) || QLLX.JTJSYDSYQ.Value.equals(this.getQllx().Value)) {
						// 国有建设使用权、宅基地、集体建设用地使用权
						try {
							RealUnit unit=UnitTools.loadUnit(BDCDYLX.SHYQZD, DJDYLY.XZ, djdy.getBDCDYID());
							UseLand zd=(UseLand)unit;
							Message msg = exchangeFactory.createMessageBySHYQ();
							this.fillHead(msg, i, ywh,zd.getBDCDYH(),zd.getQXDM(),ql.getLYQLID());

							msg.getHead().setParcelID(StringHelper.formatObject(zd.getZDDM()));
							msg.getHead().setEstateNum(StringHelper.formatObject(zd.getBDCDYH()));
//							msg.getHead().setPreEstateNum(StringHelper.formatObject(zd.getBDCDYH()));
							if(zd != null && !StringUtils.isEmpty(zd.getQXDM())){
								msg.getHead().setAreaCode(zd.getQXDM());
							}

							if (djdy != null) {
								List<ZTTGYQLR> zttqlr = msg.getData().getGYQLR();
								zttqlr = packageXml.getZTTGYQLRs(zttqlr, qlrs, zd, ql, null, null, null, null);
								msg.getData().setGYQLR(zttqlr);

								KTTZDJBXX jbxx = msg.getData().getKTTZDJBXX();
								jbxx = packageXml.getZDJBXX(jbxx, zd, ql, null, null);

//								KTFZDBHQK bhqk = msg.getData().getZDBHQK();
//								bhqk = packageXml.getKTFZDBHQK(bhqk, zd, ql, null, null);
//								msg.getData().setZDBHQK(bhqk);
								msg.getData().setZDBHQK(null);

								QLFQLJSYDSYQ syq = msg.getData().getQLJSYDSYQ();
								syq = packageXml.getQLFQLJSYDSYQ(syq, zd, ql, ywh);

								syq.replaceEmpty();
								
//								if(ql!=null){
//									if(!StringHelper.isEmpty(ql.getLYQLID())){
//										Rights lyql=RightsTools.loadRights(DJDYLY.LS, ql.getLYQLID());
//										if(lyql!=null){
//											msg.getHead().setPreCertId(lyql.getBDCQZH());
//										}
//									}
//								}
								
								DJTDJSLSQ sq = msg.getData().getDJSLSQ();
								sq = packageXml.getDJTDJSLSQ(sq, ywh, zd, ql, xmxx, null, xmxx.getSLSJ(), null, null, null);

								List<DJFDJSJ> sj = msg.getData().getDJSJ();
								sj = packageXml.getDJFDJSJ(zd, ywh,actinstID);
								msg.getData().setDJSJ(sj);

								List<DJFDJSF> sfList = msg.getData().getDJSF();
								sfList = packageXml.getDJSF(zd,ywh,this.getXMBH());
								msg.getData().setDJSF(sfList);

								List<DJFDJSH> sh = msg.getData().getDJSH();
								sh = packageXml.getDJFDJSH(zd, ywh, this.getXMBH(), actinstID);
								msg.getData().setDJSH(sh);

								List<DJFDJSZ> sz = packageXml.getDJFDJSZ(zd, ywh, this.getXMBH());
								msg.getData().setDJSZ(sz);
								
								List<DJFDJFZ> fz=packageXml.getDJFDJFZ(zd,ywh,this.getXMBH());
								msg.getData().setDJFZ(fz);
								List<DJFDJGD> gd = packageXml.getDJFDJGD(zd,ywh,this.getXMBH());
								msg.getData().setDJGD(gd);
								
								List<DJFDJSQR> djsqrs = msg.getData().getDJSQR();
								djsqrs = packageXml.getDJSQRs(djsqrs, sqrs, zd.getYSDM(), ywh, zd.getBDCDYH());
								msg.getData().setDJSQR(djsqrs);

								FJF100 fj = msg.getData().getFJF100();
								fj = packageXml.getFJF(fj);
								if(index!=null) {
									//组合流程, 业务号增加下标
									msg.getHead().setRecFlowID(msg.getHead().getRecFlowID()+"_"+index);
								} 

							}
							msgName = "Biz" + msg.getHead().getBizMsgID() + ".xml";
							mashaller.marshal(msg, new File(path + msgName));
							result = uploadFile(path + msgName,ConstValue.RECCODE.JSYDSHYQ_ZYDJ.Value,actinstID,djdy.getDJDYID(),ql.getId());
							names.put(djdy.getDJDYID(), msg.getHead().getBizMsgID() + ".xml");
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					if (QLLX.GYJSYDSHYQ_FWSYQ.Value.equals(this.getQllx().Value)||QLLX.JTJSYDSYQ_FWSYQ.Value.equals(this.getQllx().Value)||QLLX.ZJDSYQ_FWSYQ.Value.equals(this.getQllx().Value)) {
						// 房屋所有权
						try {
							BDCS_H_XZ h = dao.get(BDCS_H_XZ.class, djdy.getBDCDYID());
							BDCS_ZRZ_XZ zrz_ = null;
							BDCS_LJZ_XZ ljz_ = null;
							BDCS_SHYQZD_XZ zd = null;
							if (h != null && !StringUtils.isEmpty(h.getZRZBDCDYID())) {
								zrz_ = dao.get(BDCS_ZRZ_XZ.class, h.getZRZBDCDYID());
								if(zrz_ != null){
									zrz_.setGHYT(h.getGHYT()); //自然幢的ghyt取户的ghyt
								}
							}
							if (h != null && !StringUtils.isEmpty(h.getLJZID())) {
								ljz_ = dao.get(BDCS_LJZ_XZ.class, h.getLJZID());
							}
							BDCS_C_XZ c_ = null;
							if (h != null && !StringUtils.isEmpty(h.getCID())) {
								c_ = dao.get(BDCS_C_XZ.class, h.getCID());
							}
							if(h != null && !StringUtils.isEmpty(h.getZDBDCDYID())){
								zd = dao.get(BDCS_SHYQZD_XZ.class, h.getZDBDCDYID());
								if(zd != null){
									zrz_.setZDDM(zd.getZDDM());
								}
							}
							Message msg = exchangeFactory.createMessageByFWSYQ2();
							this.fillHead(msg, i, ywh,h.getBDCDYH(),h.getQXDM(),ql.getLYQLID());
							msg.getHead().setParcelID(zrz_.getZDDM());
							msg.getHead().setEstateNum(h.getBDCDYH());
//							msg.getHead().setPreEstateNum(h.getBDCDYH());
							if(zd != null && !StringUtils.isEmpty(zd.getQXDM())){
								msg.getHead().setAreaCode(zd.getQXDM());
							}
							if (djdy != null) {

								List<ZTTGYQLR> zttqlr = msg.getData().getGYQLR();
								zttqlr = packageXml.getZTTGYQLRs(zttqlr, qlrs, null, ql, h, null, null, null);
								msg.getData().setGYQLR(zttqlr);

								KTTFWZRZ zrz = msg.getData().getKTTFWZRZ();
								zrz = packageXml.getKTTFWZRZ(zrz, zrz_);

								KTTFWLJZ ljz = msg.getData().getKTTFWLJZ();
								ljz = packageXml.getKTTFWLJZ(ljz, ljz_,h);

								KTTFWC kttc = msg.getData().getKTTFWC();
								kttc = packageXml.getKTTFWC(kttc, c_, zrz);
								msg.getData().setKTTFWC(kttc);

								KTTFWH fwh = msg.getData().getKTTFWH();
								fwh = packageXml.getKTTFWH(fwh, h);

								QLTFWFDCQYZ fdcqyz = msg.getData().getQLTFWFDCQYZ();
								fdcqyz = packageXml.getQLTFWFDCQYZ(fdcqyz, h, ql, ywh);

								DJTDJSLSQ sq = msg.getData().getDJSLSQ();
								sq = packageXml.getDJTDJSLSQ(sq, ywh, null, ql, xmxx, h, xmxx.getSLSJ(), null, null, null);

								List<DJFDJSJ> sj = msg.getData().getDJSJ();
								sj = packageXml.getDJFDJSJ(h, ywh,actinstID);;
								msg.getData().setDJSJ(sj);

								List<DJFDJSF> sfList = msg.getData().getDJSF();
								sfList = packageXml.getDJSF(h, ywh,this.getXMBH());
								msg.getData().setDJSF(sfList);

								List<DJFDJSH> sh = msg.getData().getDJSH();
								sh = packageXml.getDJFDJSH(h, ywh, this.getXMBH(), actinstID);
								msg.getData().setDJSH(sh);

								List<DJFDJSZ> sz = packageXml.getDJFDJSZ(h, ywh, this.getXMBH());
								msg.getData().setDJSZ(sz);

								List<DJFDJFZ> fz=packageXml.getDJFDJFZ(h,ywh,this.getXMBH());
								msg.getData().setDJFZ(fz);	
								List<DJFDJGD> gd= packageXml.getDJFDJGD(h,ywh,this.getXMBH());
								msg.getData().setDJGD(gd);
								
								List<DJFDJSQR> djsqrs = msg.getData().getDJSQR();
								djsqrs = packageXml.getDJSQRs(djsqrs, sqrs, h.getYSDM(), ywh, h.getBDCDYH());
								msg.getData().setDJSQR(djsqrs);

								FJF100 fj = msg.getData().getFJF100();
								fj = packageXml.getFJF(fj);
								
								if(index!=null) {
									//组合流程, 业务号增加下标
									msg.getHead().setRecFlowID(msg.getHead().getRecFlowID()+"_"+index);
								} 
							}
							msgName = "Biz" + msg.getHead().getBizMsgID() + ".xml";
							mashaller.marshal(msg, new File(path + msgName));
							result = uploadFile(path + msgName,ConstValue.RECCODE.FDCQDZ_ZYDJ.Value,actinstID,djdy.getDJDYID(),ql.getId());
							names.put(djdy.getDJDYID(), msg.getHead().getBizMsgID() + ".xml");
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					
					if (QLLX.JTTDSYQ.Value.equals(this.getQllx().Value)) { // 集体土地所有权
						try {
							BDCS_SYQZD_XZ oland = dao.get(BDCS_SYQZD_XZ.class, djdy.getBDCDYID());
							Message msg = exchangeFactory.createMessageByTDSYQ();
							this.fillHead(msg, i, ywh,oland.getBDCDYH(),oland.getQXDM(),ql.getLYQLID());
							msg.getHead().setParcelID(StringHelper.formatObject(oland.getZDDM()));
							msg.getHead().setEstateNum(StringHelper.formatObject(oland.getBDCDYH()));
//							msg.getHead().setPreEstateNum(StringHelper.formatObject(oland.getBDCDYH()));
							if(oland != null && !StringUtils.isEmpty(oland.getQXDM())){
								msg.getHead().setAreaCode(oland.getQXDM());
							}
							if (djdy != null) {

								KTTZDJBXX jbxx = msg.getData().getKTTZDJBXX();
								jbxx = packageXml.getZDJBXX(jbxx, null, ql, oland, null);

								String zdzhdm = "";
								if (oland != null) {
									zdzhdm = oland.getZDDM();
								}
								KTTGYJZX jzx = msg.getData().getKTTGYJZX();
								jzx = packageXml.getKTTGYJZX(jzx, zdzhdm);

								KTTGYJZD jzd = msg.getData().getKTTGYJZD();
								jzd = packageXml.getKTTGYJZD(jzd, zdzhdm);

								FJF100 fj = msg.getData().getFJF100();
								fj = packageXml.getFJF(fj);

								KTFZDBHQK bhqk = msg.getData().getZDBHQK();
								bhqk = packageXml.getKTFZDBHQK(bhqk, null, ql, oland, null);
								msg.getData().setZDBHQK(bhqk);

								QLFQLTDSYQ tdsyq = msg.getData().getQLFQLTDSYQ();
								tdsyq = packageXml.getQLFQLTDSYQ(tdsyq, oland, ql, ywh);

								List<ZTTGYQLR> zttqlr = msg.getData().getGYQLR();
								zttqlr = packageXml.getZTTGYQLRs(zttqlr, qlrs, null, ql, null, oland, null, null);
								msg.getData().setGYQLR(zttqlr);

								DJTDJSLSQ sq = msg.getData().getDJSLSQ();
								sq = packageXml.getDJTDJSLSQ(sq, ywh, null, ql, xmxx, null, xmxx.getSLSJ(), oland, null, null);

								List<DJFDJSJ> sj = msg.getData().getDJSJ();
								sj = packageXml.getDJFDJSJ(oland, ywh,actinstID);
								msg.getData().setDJSJ(sj);

								List<DJFDJSF> sfList = msg.getData().getDJSF();
								sfList = packageXml.getDJSF(oland, ywh, this.getXMBH());
								msg.getData().setDJSF(sfList);

								List<DJFDJSH> sh = msg.getData().getDJSH();
								sh = packageXml.getDJFDJSH(oland, ywh, this.getXMBH(), actinstID);
								msg.getData().setDJSH(sh);

								List<DJFDJSZ> sz = packageXml.getDJFDJSZ(oland, ywh, this.getXMBH());
								msg.getData().setDJSZ(sz);
								
								List<DJFDJFZ> fz = packageXml.getDJFDJFZ(oland,ywh, this.getXMBH());
								msg.getData().setDJFZ(fz);	
							    List<DJFDJGD> gd = packageXml.getDJFDJGD(oland, ywh,this.getXMBH());
							    msg.getData().setDJGD(gd);	
								List<ZDK103> zdk = msg.getData().getZDK103();
								zdk = packageXml.getZDK103(zdk, null, oland, null);

								List<DJFDJSQR> djsqrs = msg.getData().getDJSQR();
								djsqrs = packageXml.getDJSQRs(djsqrs, sqrs, oland.getYSDM(), ywh, oland.getBDCDYH());
								msg.getData().setDJSQR(djsqrs);
								
								if(index!=null) {
									//组合流程, 业务号增加下标
									msg.getHead().setRecFlowID(msg.getHead().getRecFlowID()+"_"+index);
								} 
							}
							msgName = "Biz" + msg.getHead().getBizMsgID() + ".xml";
							names.put(djdy.getDJDYID(), msg.getHead().getBizMsgID() + ".xml");
							mashaller.marshal(msg, new File(path + msgName));
							result = uploadFile(path + msgName, ConstValue.RECCODE.TDSYQ_CSDJ.Value,actinstID,djdy.getDJDYID(),ql.getId());
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					
					if(result.equals("")|| result==null){
						Map<String, String> xmlError = new HashMap<String, String>();
						xmlError.put("error", "连接SFTP失败,请检查服务器和前置机的连接是否正常");
						if(QLLX.JTTDSYQ.Value.equals(this.getQllx().Value)){
							YwLogUtil.addSjsbResult(msgName, "", "连接SFTP失败,请检查服务器和前置机的连接是否正常", ConstValue.SF.NO.Value, ConstValue.RECCODE.TDSYQ_ZYDJ.Value,ProjectHelper.getpRroinstIDByActinstID(actinstID));	
						}else if (QLLX.GYJSYDSHYQ_FWSYQ.Value.equals(this.getQllx().Value)){
							YwLogUtil.addSjsbResult(msgName, "", "连接SFTP失败,请检查服务器和前置机的连接是否正常", ConstValue.SF.NO.Value, ConstValue.RECCODE.FDCQDZ_ZYDJ.Value,ProjectHelper.getpRroinstIDByActinstID(actinstID));
						}else {
							YwLogUtil.addSjsbResult(msgName, "", "连接SFTP失败,请检查服务器和前置机的连接是否正常", ConstValue.SF.NO.Value, ConstValue.RECCODE.JSYDSHYQ_ZYDJ.Value,ProjectHelper.getpRroinstIDByActinstID(actinstID));
						}
						return xmlError;
					}
					if(!"1".equals(result) && result.indexOf("success") == -1){ //xml本地校验不通过时
						Map<String, String> xmlError = new HashMap<String, String>();
						xmlError.put("error", result);
						return xmlError;
					}
					if(!StringUtils.isEmpty(result) && result.indexOf("success") > -1 && !names.containsKey("reccode")){
						names.put("reccode", result);
					}
				}
			}

		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return names;
	}
}































































