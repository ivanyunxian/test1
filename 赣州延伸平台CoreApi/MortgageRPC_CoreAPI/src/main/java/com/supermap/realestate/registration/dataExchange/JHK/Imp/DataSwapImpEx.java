package com.supermap.realestate.registration.dataExchange.JHK.Imp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import javax.imageio.stream.FileImageOutputStream;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.http.client.ClientProtocolException;
import org.apache.log4j.Logger;
import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.druid.sql.visitor.functions.If;
import com.itextpdf.text.pdf.PdfStructTreeController.returnType;
import com.map.OperateFeature;
import com.supermap.realestate.registration.dataExchange.utils.Tools;
import com.supermap.realestate.registration.factorys.HandlerFactory;
import com.supermap.realestate.registration.mapping.HandlerMapping;
import com.supermap.realestate.registration.model.BDCS_CONST;
import com.supermap.realestate.registration.model.BDCS_CONSTCLS;
import com.supermap.realestate.registration.model.BDCS_DJDY_GZ;
import com.supermap.realestate.registration.model.BDCS_DYXZ;
import com.supermap.realestate.registration.model.BDCS_FSQL_GZ;
import com.supermap.realestate.registration.model.BDCS_FSQL_LS;
import com.supermap.realestate.registration.model.BDCS_H_GZ;
import com.supermap.realestate.registration.model.BDCS_H_LS;
import com.supermap.realestate.registration.model.BDCS_H_LSY;
import com.supermap.realestate.registration.model.BDCS_H_XZ;
import com.supermap.realestate.registration.model.BDCS_H_XZY;
import com.supermap.realestate.registration.model.BDCS_QLR_GZ;
import com.supermap.realestate.registration.model.BDCS_QLR_LS;
import com.supermap.realestate.registration.model.BDCS_QLR_XZ;
import com.supermap.realestate.registration.model.BDCS_QL_GZ;
import com.supermap.realestate.registration.model.BDCS_QL_LS;
import com.supermap.realestate.registration.model.BDCS_QL_XZ;
import com.supermap.realestate.registration.model.BDCS_SHYQZD_GZ;
import com.supermap.realestate.registration.model.BDCS_SHYQZD_LS;
import com.supermap.realestate.registration.model.BDCS_SHYQZD_XZ;
import com.supermap.realestate.registration.model.BDCS_SQR;
import com.supermap.realestate.registration.model.BDCS_SYQZD_GZ;
import com.supermap.realestate.registration.model.BDCS_SYQZD_LS;
import com.supermap.realestate.registration.model.BDCS_SYQZD_XZ;
import com.supermap.realestate.registration.model.BDCS_TDYT_GZ;
import com.supermap.realestate.registration.model.BDCS_TDYT_LS;
import com.supermap.realestate.registration.model.BDCS_TDYT_XZ;
import com.supermap.realestate.registration.model.BDCS_XMXX;
import com.supermap.realestate.registration.model.BDCS_XM_DYXZ;
import com.supermap.realestate.registration.model.BDCS_ZRZ_XZ;
import com.supermap.realestate.registration.model.BDCS_ZS_GZ;
import com.supermap.realestate.registration.model.BDCS_ZS_LS;
import com.supermap.realestate.registration.model.T_CONFIG;
import com.supermap.realestate.registration.model.interfaces.Rights;
import com.supermap.realestate.registration.model.interfaces.RightsHolder;
import com.supermap.realestate.registration.tools.EncodeTools;
import com.supermap.realestate.registration.tools.RightsHolderTools;
import com.supermap.realestate.registration.tools.RightsTools;
import com.supermap.realestate.registration.util.ConfigHelper;
import com.supermap.realestate.registration.util.ConstValue;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.realestate.registration.util.ConstValue.BDCDYLX;
import com.supermap.realestate.registration.util.ConstValue.DJDYLY;
import com.supermap.realestate.registration.util.HttpRequest;
import com.supermap.realestate.registration.util.JH_DBHelper;
import com.supermap.realestate.registration.util.ProjectHelper;
import com.supermap.services.protocols.wcs.CoverageSummary;
import com.supermap.wisdombusiness.core.SuperSpringContext;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDaoDJ;
import com.supermap.wisdombusiness.framework.model.gxdjk.Bgqcfdj_dj;
import com.supermap.wisdombusiness.framework.model.gxdjk.Bgqdyaq_dj;
import com.supermap.wisdombusiness.framework.model.gxdjk.Bgqfdcq_dj;
import com.supermap.wisdombusiness.framework.model.gxdjk.Bgqh_dj;
import com.supermap.wisdombusiness.framework.model.gxdjk.Bgqqlr_dj;
import com.supermap.wisdombusiness.framework.model.gxdjk.Bgqxzdj_dj;
import com.supermap.wisdombusiness.framework.model.gxdjk.Bgqygdj_dj;
import com.supermap.wisdombusiness.framework.model.gxdjk.Bgqyydj_dj;
import com.supermap.wisdombusiness.framework.model.gxdjk.Cfdj_dj;
import com.supermap.wisdombusiness.framework.model.gxdjk.Dyaq_dj;
import com.supermap.wisdombusiness.framework.model.gxdjk.Fdcq_dj;
import com.supermap.wisdombusiness.framework.model.gxdjk.Fdcqzxdj_dj;
import com.supermap.wisdombusiness.framework.model.gxdjk.Gxjhxm_dj;
import com.supermap.wisdombusiness.framework.model.gxdjk.H_dj;
import com.supermap.wisdombusiness.framework.model.gxdjk.Log_dj;
import com.supermap.wisdombusiness.framework.model.gxdjk.Qlr_dj;
import com.supermap.wisdombusiness.framework.model.gxdjk.Xzdj2_dj;
import com.supermap.wisdombusiness.framework.model.gxdjk.Xzdj_dj;
import com.supermap.wisdombusiness.framework.model.gxdjk.Ygdj_dj;
import com.supermap.wisdombusiness.framework.model.gxdjk.Yydj_dj;
import com.supermap.wisdombusiness.framework.model.gxjyk.Gxjhxm;
import com.supermap.wisdombusiness.framework.model.gxjyk.JYQLR;
import com.supermap.wisdombusiness.framework.model.gxjyk.MATERDATA;
import com.supermap.wisdombusiness.framework.model.gxjyk.PROMATER;
import com.supermap.wisdombusiness.web.ResultMessage;
import com.supermap.wisdombusiness.workflow.model.Wfd_Prodef;
import com.supermap.wisdombusiness.workflow.model.Wfi_MaterData;
import com.supermap.wisdombusiness.workflow.model.Wfi_ProInst;
import com.supermap.wisdombusiness.workflow.model.Wfi_ProMater;
import com.supermap.wisdombusiness.workflow.service.common.Common;
import com.supermap.wisdombusiness.workflow.service.common.Http;

@Component("DataSwapImp")
public class DataSwapImpEx {

	public Logger logger = Logger.getLogger(DataSwapImpEx.class);

	// public CommonDao getCommonDao() {
	// return commonDao;
	// }

	// 1设立，2转移，3变更，4注销
	// private String qlsdlx;
	private String gxxmbh;

	// protected String getQlsdlx() {
	// return qlsdlx;
	// }

	// protected void setQlsdlx(String qlsdlx) {
	// this.qlsdlx = qlsdlx;
	// }
	BDCS_XMXX xmxx;

	// String bdcdyh;
	// String zt = "1";

	public List<BDCS_QLR_GZ> getQlr_gzList() {
		return qlr_gzList;
	}

	public void setQlr_gzList(List<BDCS_QLR_GZ> qlr_gzList) {
		this.qlr_gzList = qlr_gzList;
	}


	public String getBljd() {
		return bljd;
	}

	public void setBljd(String bljd) {
		this.bljd = bljd;
	}

	public String getQlsdfs() {
		return qlsdfs;
	}

	public void setQlsdfs(String qlsdfs) {
		this.qlsdfs = qlsdfs;
	}

	public String getCasenum() {
		return casenum;
	}

	public void setCasenum(String casenum) {
		this.casenum = casenum;
	}

	String bljd = "2";
	String qlsdfs;
	String casenum = "";
	String djxl = "0";
	Boolean bPushBGQ = false;// 是否推送变更前数据
	// String qltablename;// 权利表名称
	String bdcdyly = BDCDYLX.H.Value;// 房屋状态，1期房，2现房
	//所有保存是否都成功
    public boolean allSuccess=true;
	protected String getGxxmbh() {
		return gxxmbh;
	}

	protected void setGxxmbh(String gxxmbh) {
		this.gxxmbh = gxxmbh;
	}
	
	private String tdxz;
	protected String getTdxz() {
		return tdxz;
	}

	protected void setTdxz(String tdxz) {
		this.tdxz = tdxz;
	}
	
	private String tdyt;
	protected String getTdyt() {
		return tdyt;
	}

	protected void setTdyt(String tdyt) {
		this.tdyt = tdyt;
	}
	
	private String tdmj;
	protected String getTdmj() {
		return tdmj;
	}

	protected void setTdmj(String tdmj) {
		this.tdmj = tdmj;
	}
	
	public Connection getJyConnection() {
		return jyConnection;
	}

	public void setJyConnection(Connection jyConnection) {
		this.jyConnection = jyConnection;
	}
	public Connection getfcJyConnection() {
		return fcjyConnection;
	}

	public void setfcJyConnection(Connection jyConnection) {
		this.fcjyConnection = jyConnection;
	}

	Connection fcjyConnection=null;

	Connection jyConnection = null;
	Connection tddjConnection = null;

	public Connection getTddjConnection() {
		return tddjConnection;
	}

	public void setTddjConnection(Connection tddjConnection) {
		this.tddjConnection = tddjConnection;
	}

	protected BDCS_QL_GZ getQl_gz() {
		return ql_gz;
	}

	protected void setQl_gz(BDCS_QL_GZ ql_gz) {
//		this.ql_gz = ql_gz;
		//拷贝一下，否则修改的属性会被保存到数据库
		try {
			PropertyUtils.copyProperties(this.ql_gz, ql_gz);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
	}

	protected BDCS_FSQL_GZ getFsql_gz() {
		return fsql_gz;
	}

	protected void setFsql_gz(BDCS_FSQL_GZ fsql_gz) {
//		this.fsql_gz = fsql_gz;
		//拷贝一下，否则修改的属性会被保存到数据库
		try {
			PropertyUtils.copyProperties(this.fsql_gz, fsql_gz);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
	}

	protected BDCS_H_XZ getH_xz() {
		return h_xz;
	}

	protected void setH_xz(BDCS_H_XZ h_gz) {
//		this.h_xz = h_gz;
		//拷贝一下，否则修改的属性会被保存到数据库
		try {
			PropertyUtils.copyProperties(this.h_xz, h_gz);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
	}

	protected BDCS_ZRZ_XZ getZrz_xz() {
		return zrz_xz;
	}

	protected void setZrz_xz(BDCS_ZRZ_XZ zrz_gz) {
		this.zrz_xz = zrz_gz;
	}

	protected List<BDCS_QLR_GZ> getQlr_gz() {
		return qlr_gzList;
	}

	protected void setQlr_gz(List<BDCS_QLR_GZ> qlr_gz) {
		this.qlr_gzList = qlr_gz;
	}

	protected BDCS_XMXX getXmxx() {
		return xmxx;
	}

	protected void setXmxx(BDCS_XMXX xmxx) {
		this.xmxx = xmxx;
	}

	BDCS_QL_GZ ql_gz=new BDCS_QL_GZ();
	BDCS_FSQL_GZ fsql_gz=new BDCS_FSQL_GZ();
	BDCS_H_XZ h_xz=new BDCS_H_XZ();
	BDCS_ZRZ_XZ zrz_xz;
	List<BDCS_QLR_GZ> qlr_gzList;
	// 变更前
	BDCS_QL_LS ql_ls_bgq;
	BDCS_FSQL_LS fsql_ls_bgq;
	BDCS_H_LS h_ls_bgq;
	List<BDCS_QLR_LS> qlr_lsList_bgq=new ArrayList<BDCS_QLR_LS>();
	List<BDCS_QLR_LS> qlr_lsList_bgq1=new ArrayList<BDCS_QLR_LS>();
	

	BDCS_H_XZY h_XZY=new BDCS_H_XZY();
	//登记大类
	String djdl="";
	public String getDjdl() {
		return djdl;
	}
	public void setDjdl(String djdl) {
		this.djdl = djdl;
	}

	public BDCS_H_XZY getH_XZY() {
		return h_XZY;
	}

	public void setH_XZY(BDCS_H_XZY h_XZY) {
//		this.h_XZY = h_XZY;
		//拷贝一下，否则修改的属性会被保存到数据库
					try {
						PropertyUtils.copyProperties(this.h_XZY, h_XZY);
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						e.printStackTrace();
					} catch (NoSuchMethodException e) {
						e.printStackTrace();
					}
				
	}

	public BDCS_H_LSY getH_LSY() {
		return h_LSY;
	}

	public void setH_LSY(BDCS_H_LSY h_LSY) {
		this.h_LSY = h_LSY;
	}

	BDCS_H_LSY h_LSY;

	public BDCS_QL_LS getQl_ls_bgq() {
		return ql_ls_bgq;
	}

	public void setQl_ls_bgq(BDCS_QL_LS ql_ls_bgq) {
		this.ql_ls_bgq = ql_ls_bgq;
	}

	public BDCS_FSQL_LS getFsql_ls_bgq() {
		return fsql_ls_bgq;
	}

	public void setFsql_ls_bgq(BDCS_FSQL_LS fsql_ls_bgq) {
		this.fsql_ls_bgq = fsql_ls_bgq;
	}

	public BDCS_H_LS getH_ls_bgq() {
		return h_ls_bgq;
	}

	public void setH_ls_bgq(BDCS_H_LS h_ls_bgq) {
		this.h_ls_bgq = h_ls_bgq;
	}

	public List<BDCS_QLR_LS> getQlr_lsList_bgq() {
		return qlr_lsList_bgq;
	}

	public void setQlr_lsList_bgq(List<BDCS_QLR_LS> qlr_lsList_bgq) {
//		this.qlr_lsList_bgq = qlr_lsList_bgq;
		//拷贝一下，否则修改的属性会被保存到数据库
				try {
					this.qlr_lsList_bgq.clear();
					if(qlr_lsList_bgq!=null &&qlr_lsList_bgq.size()>0){
						for( int i=0;i<qlr_lsList_bgq.size();i++){
							BDCS_QLR_LS qlr_LS=new BDCS_QLR_LS();
							PropertyUtils.copyProperties(qlr_LS, qlr_lsList_bgq.get(i));
							this.qlr_lsList_bgq.add(qlr_LS);
						}
					}
//					PropertyUtils.copyProperties(this.qlr_lsList_bgq, qlr_lsList_bgq);
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					e.printStackTrace();
				}
	}

	/**
	 * @return the qlr_lsList_bgq1
	 */
	public List<BDCS_QLR_LS> getQlr_lsList_bgq1() {
		return qlr_lsList_bgq1;
	}

	/**
	 * @param qlr_lsList_bgq1 the qlr_lsList_bgq1 to set
	 */
	public void setQlr_lsList_bgq1(List<BDCS_QLR_LS> qlr_lsList_bgq1) {
		try {
			this.qlr_lsList_bgq1.clear();
			if(qlr_lsList_bgq1!=null &&qlr_lsList_bgq1.size()>0){
				for( int i=0;i<qlr_lsList_bgq1.size();i++){
					BDCS_QLR_LS qlr_LS=new BDCS_QLR_LS();
					PropertyUtils.copyProperties(qlr_LS, qlr_lsList_bgq1.get(i));
					this.qlr_lsList_bgq1.add(qlr_LS);
				}
			}
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
	}
	

    //推送失败具体原因记录下来方便查看验证
    public String FailCause="";
	public String getBdcdyly() {
		return bdcdyly;
	}

	public void setBdcdyly(String bdcdyly) {
		this.bdcdyly = bdcdyly;
	}
	String bdcdyid;
	
	public String getBdcdyid() {
		return bdcdyid;
	}

	public void setBdcdyid(String bdcdyid) {
		this.bdcdyid = bdcdyid;
	}

	private BDCS_SHYQZD_GZ shyqzd_GZ;
	
	//土地用途【同一个宗地单元可能有多种用途】
	private List<BDCS_TDYT_GZ> tdyt_GZs;
	public List<BDCS_TDYT_GZ> getTdyt_GZ() {
		return tdyt_GZs;
	}
	public void setTdyt_GZ(List<BDCS_TDYT_GZ> tdyt_GZs) {
		this.tdyt_GZs = tdyt_GZs;
	}
	private List<BDCS_TDYT_XZ> tdyt_XZs;
	public List<BDCS_TDYT_XZ> getTdyt_XZ() {
		return tdyt_XZs;
	}
	public void setTdyt_XZ(List<BDCS_TDYT_XZ> tdyt_XZs) {
		this.tdyt_XZs = tdyt_XZs;
	}
	private List<BDCS_TDYT_LS> tdyt_LSs;
	public List<BDCS_TDYT_LS> getTdyt_LS() {
		return tdyt_LSs;
	}
	public void setTdyt_LS(List<BDCS_TDYT_LS> tdyt_LSs) {
		this.tdyt_LSs = tdyt_LSs;
	}
	
	public BDCS_SHYQZD_GZ getShyqzd_GZ() {
		return shyqzd_GZ;
	}

	public void setShyqzd_GZ(BDCS_SHYQZD_GZ shyqzd_GZ) {
		this.shyqzd_GZ = shyqzd_GZ;
	}

	public BDCS_SHYQZD_LS getShyqzd_LS() {
		return shyqzd_LS;
	}

	public void setShyqzd_LS(BDCS_SHYQZD_LS shyqzd_LS) {
		this.shyqzd_LS = shyqzd_LS;
	}

	public BDCS_SHYQZD_XZ getShyqzd_XZ() {
		return shyqzd_XZ;
	}

	public void setShyqzd_XZ(BDCS_SHYQZD_XZ shyqzd_XZ) {
		this.shyqzd_XZ = shyqzd_XZ;
	}

	public BDCS_SYQZD_LS getSyqzd_LS() {
		return syqzd_LS;
	}

	public void setSyqzd_LS(BDCS_SYQZD_LS syqzd_LS) {
		this.syqzd_LS = syqzd_LS;
	}

	public BDCS_SYQZD_XZ getSyqzd_XZ() {
		return syqzd_XZ;
	}

	public void setSyqzd_XZ(BDCS_SYQZD_XZ syqzd_XZ) {
		this.syqzd_XZ = syqzd_XZ;
	}

	public BDCS_SYQZD_GZ getSyqzd_GZ() {
		return syqzd_GZ;
	}

	public void setSyqzd_GZ(BDCS_SYQZD_GZ syqzd_GZ) {
		this.syqzd_GZ = syqzd_GZ;
	}

	private BDCS_SHYQZD_LS shyqzd_LS;
	private BDCS_SHYQZD_XZ shyqzd_XZ;
	private BDCS_SYQZD_LS syqzd_LS;
	private BDCS_SYQZD_XZ syqzd_XZ;
	private BDCS_SYQZD_GZ syqzd_GZ;

	public Boolean getbPushBGQ() {
		return bPushBGQ;
	}

	public void setbPushBGQ(Boolean bPushBGQ) {
		this.bPushBGQ = bPushBGQ;
	}

	public String getDjxl() {
		return djxl;
	}

	public void setDjxl(String djxl) {
		this.djxl = djxl;
	}

	BDCS_H_GZ h_GZ;

	public BDCS_H_GZ getH_GZ() {
		return h_GZ;
	}

	public void setH_GZ(BDCS_H_GZ h_GZ) {
		this.h_GZ = h_GZ;
	}

	String djdyly = DJDYLY.XZ.Value;

	public String getDjdyly() {
		return djdyly;
	}

	public void setDjdyly(String djdyly) {
		this.djdyly = djdyly;
	}

	// 遂宁抵押推送所有权信息
	private Boolean pushDYFWSYQ = true;

	public Boolean getPushDYFWSYQ() {
		return pushDYFWSYQ;
	}

	public void setPushDYFWSYQ(Boolean pushDYFWSYQ) {
		this.pushDYFWSYQ = pushDYFWSYQ;
	}

	private String srcDsAlias="BDCK";
	private String destDsAlias="GXTDDJK";
	private String srcDtName="BDCK_SHYQZD_GZ";
	private String destDtName="ZD";
	private String xzqdm="";
	public String getXzqdm() {
		return xzqdm;
	}
	public void setXzqdm(String xzqdm) {
		this.xzqdm = xzqdm;
	}
	public String getXzqmc() {
		return xzqmc;
	}
	public void setXzqmc(String xzqmc) {
		this.xzqmc = xzqmc;
	}
	private String xzqmc="";
	//是否登薄
	String sfdb="0";
	
	public String getSrcDsAlias() {
		return srcDsAlias;
	}

	public void setSrcDsAlias(String srcDsAlias) {
		this.srcDsAlias = srcDsAlias;
	}

	public String getDestDsAlias() {
		return destDsAlias;
	}

	public void setDestDsAlias(String destDsAlias) {
		this.destDsAlias = destDsAlias;
	}

	public String getSrcDtName() {
		return srcDtName;
	}

	public void setSrcDtName(String srcDtName) {
		this.srcDtName = srcDtName;
	}

	public String getDestDtName() {
		return destDtName;
	}

	public void setDestDtName(String destDtName) {
		this.destDtName = destDtName;
	}
	com.supermap.wisdombusiness.framework.dao.impl.CommonDao commonDao;
	public com.supermap.wisdombusiness.framework.dao.impl.CommonDao getCommonDao() {
		return commonDao;
	}

	public void setCommonDao(
			com.supermap.wisdombusiness.framework.dao.impl.CommonDao commonDao) {
		this.commonDao = commonDao;
	}

	/**
	 * 
	 * @作者 likun
	 * @创建时间 2015年12月23日下午1:58:58
	 * @param bdcdyh
	 *            不动产单元号
	 * @param zt
	 *            状态，1为有效；2为无效
	 * @param bljd
	 *            办理进度， 0初始推送；1审批；2登簿；3缮证；4发证；5归档；
	 * @param qlsdfs
	 *            权利设定方式，1设立，2转移，3变更，4注销
	 * @param casenum
	 *            房产系统案卷号
	 * @param djxl
	 *            换补证有效
	 * @param relationid2gxxmbh 
	 * @param eNCRYPTION 
	 */
	public void pushToGXDJK(
			com.supermap.wisdombusiness.framework.dao.impl.CommonDao baseCommonDao,com.supermap.wisdombusiness.framework.dao.impl.CommonDaoDJ CommonDaoDJ,
			BDCS_XMXX xmxx, String qltablename, String bljd, String qlsdfs,
			int iPushBGQ, String djxl, Map<String, String> relationid2gxxmbh, String eNCRYPTION) {
		if (xmxx == null || baseCommonDao == null) {
			logger.info("xmxx、dao为null");
			return;
		}
		sfdb=xmxx.getSFDB();
		setCommonDao(baseCommonDao);
		Boolean bXMXXSaved = false;
		// bljd 办理进度， 0初始推送；1审批；2登簿；3缮证；4发证；5归档；
		setBljd(bljd);
		// zt 状态，1为有效；2为无效
		// setZt(zt);
		// 登记小类
		setDjxl(djxl);
		// qlsdfs 权利设定方式，1设立，2转移，3变更，4注销
		setQlsdfs(qlsdfs);
		if (iPushBGQ == 1) {
			setbPushBGQ(true);
		}
		//setQltablename(qltable);
		String xmbhFilter = ProjectHelper.GetXMBHCondition(xmxx.getId());
		List<BDCS_DJDY_GZ> djdys = baseCommonDao.getDataList(BDCS_DJDY_GZ.class, xmbhFilter);
		if (djdys != null && djdys.size() > 0) {
			for (int idjdy = 0; idjdy < djdys.size(); idjdy++) {
				try {
					BDCS_DJDY_GZ djdy = djdys.get(idjdy);
					// 获取户model，户里包含houseid
					String bdcdyh = djdy.getBDCDYH();
					String bdcdyids = djdy.getBDCDYID();
					setBdcdyid(bdcdyids);
					setBdcdyly(djdy.getBDCDYLX());
					djdyly = djdy.getLY();
					if (bdcdyly.equals(BDCDYLX.H.Value)) {
						// 实测户
						if (djdyly.equals(DJDYLY.XZ.Value)) {
							// 来源现在
							// 变更后
							List<BDCS_H_XZ> h_XZs = baseCommonDao.getDataList(BDCS_H_XZ.class, "bdcdyid='" + bdcdyids+ "'");
							// 变更前户
							List<BDCS_H_LS> h_LSs = baseCommonDao.getDataList(BDCS_H_LS.class, "bdcdyid='" + bdcdyids+ "'");
							if (h_XZs != null && h_XZs.size() > 0) {
								setH_xz(h_XZs.get(0));
								//准备推送给房产的土地性质、土地用途、土地面积等字段 2016年11月21日 15:03:42卜晓波
								try{
									List<BDCS_SHYQZD_XZ> zds = baseCommonDao.getDataList(BDCS_SHYQZD_XZ.class, "bdcdyid='" + h_XZs.get(0).getZDBDCDYID()+ "'");
									if(zds!=null && zds.size()>0){
										setTdxz(zds.get(0).getQLXZ()!=null?zds.get(0).getQLXZ():"");
										setTdyt(zds.get(0).getYT()!=null?zds.get(0).getYT():"");
										setTdmj(Double.toString(zds.get(0).getMJ()));
									}
								}catch(Exception e){
									logger.error("推送土地信息时出错："+e.toString()+",推送失败项目Project_id:"+xmxx.getPROJECT_ID()+",办理阶段："+bljd+"");
								}
							}
							if (h_LSs != null && h_LSs.size() > 0) {
								setH_ls_bgq(h_LSs.get(0));
							}
						} else if (djdyly.equals(DJDYLY.GZ.Value)) {
							// 来源工作
							// 变更后
							List<BDCS_H_GZ> h_GZs = baseCommonDao.getDataList(BDCS_H_GZ.class, "bdcdyid='" + bdcdyids+ "'");
							// 变更前户
							List<BDCS_H_LS> h_LSs = baseCommonDao.getDataList(BDCS_H_LS.class, "bdcdyid='" + bdcdyids+ "'");
							if (h_GZs != null && h_GZs.size() > 0) {
								setH_GZ(h_GZs.get(0));// setH_xz(h_GZs.get(0));
								//准备推送给房产的土地性质、土地用途、土地面积等字段 2016年11月21日 15:03:42卜晓波
								try{
									List<BDCS_SHYQZD_XZ> zds = baseCommonDao.getDataList(BDCS_SHYQZD_XZ.class, "bdcdyid='" + h_GZs.get(0).getZDBDCDYID()+ "'");
									if(zds!=null && zds.size()>0){
										setTdxz(zds.get(0).getQLXZ()!=null?zds.get(0).getQLXZ():"");
										setTdyt(zds.get(0).getYT()!=null?zds.get(0).getYT():"");
										setTdmj(Double.toString(zds.get(0).getMJ()));
									}
								}catch(Exception e){
									logger.error("推送土地信息时出错："+e.toString()+",推送失败项目Project_id:"+xmxx.getPROJECT_ID()+",办理阶段："+bljd+"");
								}
							}
							if (h_LSs != null && h_LSs.size() > 0) {
								setH_ls_bgq(h_LSs.get(0));
							}
						}else if (djdyly.equals(DJDYLY.LS.Value)) {
							// 来源现在
							// 变更后
							List<BDCS_H_XZ> h_XZs = baseCommonDao.getDataList(BDCS_H_XZ.class, "bdcdyid='" + bdcdyids+ "'");
							// 变更前户
							List<BDCS_H_LS> h_LSs = baseCommonDao.getDataList(BDCS_H_LS.class, "bdcdyid='" + bdcdyids+ "'");
							if (h_XZs != null && h_XZs.size() > 0) {
								setH_xz(h_XZs.get(0));
								//准备推送给房产的土地性质、土地用途、土地面积等字段 2016年11月21日 15:03:42卜晓波
								try{
									List<BDCS_SHYQZD_XZ> zds = baseCommonDao.getDataList(BDCS_SHYQZD_XZ.class, "bdcdyid='" + h_XZs.get(0).getZDBDCDYID()+ "'");
									if(zds!=null && zds.size()>0){
										setTdxz(zds.get(0).getQLXZ()!=null?zds.get(0).getQLXZ():"");
										setTdyt(zds.get(0).getYT()!=null?zds.get(0).getYT():"");
										setTdmj(Double.toString(zds.get(0).getMJ()));
									}
								}catch(Exception e){
									logger.error("推送土地信息时出错："+e.toString()+",推送失败项目Project_id:"+xmxx.getPROJECT_ID()+",办理阶段："+bljd+"");
								}
							}
							if (h_LSs != null && h_LSs.size() > 0) {
								setH_ls_bgq(h_LSs.get(0));
							}
						} 
					} else if (bdcdyly.equals(BDCDYLX.YCH.Value)) {
						// 预测户
						List<BDCS_H_XZY> h_XZs = baseCommonDao.getDataList(BDCS_H_XZY.class, "bdcdyid='" + bdcdyids + "'");
						// 变更前户
						List<BDCS_H_LSY> h_LSs = baseCommonDao.getDataList(BDCS_H_LSY.class, "bdcdyid='" + bdcdyids + "'");
						if (h_XZs != null && h_XZs.size() > 0) {
							setH_XZY(h_XZs.get(0));
							//准备推送给房产的土地性质、土地用途、土地面积等字段 2016年11月21日 15:03:42卜晓波
							List<BDCS_SHYQZD_XZ> zds = baseCommonDao.getDataList(BDCS_SHYQZD_XZ.class, "bdcdyid='" + h_XZs.get(0).getZDBDCDYID()+ "'");
							if(zds!=null && zds.size()>0){
								setTdxz(zds.get(0).getQLXZ()!=null?zds.get(0).getQLXZ():"");
								setTdyt(zds.get(0).getYT()!=null?zds.get(0).getYT():"");
								setTdmj(Double.toString(zds.get(0).getMJ()));
							}
						}
						if (h_LSs != null && h_LSs.size() > 0) {
							setH_LSY(h_LSs.get(0));
						}
					} else {
						return;
					}
					// ConstValue.BDCDYLX dylx =
					// ConstValue.BDCDYLX.initFrom(djdy
					// .getBDCDYLX());
					// ConstValue.DJDYLY dyly = ConstValue.DJDYLY.initFrom(djdy
					// .getLY());
					// RealUnit bdcdy = UnitTools.loadUnit(dylx, dyly,
					// djdy.getBDCDYID());
					//并案流程一个项目对应一条xmxx，一条djdy，多条权利
					List<Rights> bdcRights=RightsTools.loadRightsByCondition(ConstValue.DJDYLY.GZ, "XMBH='"+xmxx.getId()+"' and DJDYID='"+djdy.getDJDYID()+"'");
					if(bdcRights!=null&&bdcRights.size()>0){
//						Session session=CommonDaoDJ.getCurrentSession();
//						session.setFlushMode(FlushMode.MANUAL);
						for(int k=0;k<bdcRights.size();k++){
							Rights bdcql =bdcRights.get(k);// RightsTools.loadRightsByDJDYID(ConstValue.DJDYLY.GZ, xmxx.getId(),djdy.getDJDYID());
							boolean IsCombinnation=false;
							String nowqllx;
							nowqllx=xmxx.getQLLX();
							String nowdjdl=xmxx.getDJLX();
							if(qltablename.equals("YGDJ")){
								nowqllx="4";
							}
							//如果是并案的权利有两条
							if(bdcRights.size()>1){
								nowqllx=bdcql.getQLLX();
								nowdjdl=bdcql.getDJLX();
								String nowqlid=bdcql.getId();
								//检查当前权利表名称和当前权利类型是否一致进而判断当前该不该保存对应的ql、qlr、h、xmxx
								boolean HaveSave=CheckQllxandQLtable(nowqllx,nowqlid,qltablename,CommonDaoDJ);
								IsCombinnation=true;
								if(HaveSave){
									continue;
								}
							}
							String lyqlid = bdcql.getLYQLID();
							if(lyqlid==null){
								lyqlid=bdcql.getId();
							}
							// bdcql.setId(lyqlid);
							// SubRights bdcfsql = RightsTools.loadSubRightsByRightsID(
							// ConstValue.DJDYLY.GZ, bdcql.getId());
//							if(qltablename.equals("XZDJ")){}ELSE
							List<RightsHolder> bdcqlrs = RightsHolderTools.loadRightsHolders(ConstValue.DJDYLY.GZ,bdcql.getId());
							// 变更前权利人
							List<RightsHolder> bdcqlrs_ls = RightsHolderTools.loadRightsHolders(ConstValue.DJDYLY.LS, lyqlid);
							// 获取权利model
							List<BDCS_QL_GZ> ql_GZ = baseCommonDao.getDataList(BDCS_QL_GZ.class, "qlid='" + bdcql.getId() + "'");						
							//吉林cgk
			                if(xzqdm.equals("220200")){
			                	if(qltablename.equals("CFDJ")&&qlsdfs.equals("4")&&iPushBGQ==0){
			                		List<BDCS_QL_LS> qlz = baseCommonDao.getDataList(BDCS_QL_LS.class, "qlid='" + lyqlid + "'");
			                		if(qlz.get(0).getLYQLID()!=null){
			                			lyqlid=qlz.get(0).getLYQLID();
			                		}else {
			                			lyqlid= bdcql.getLYQLID();
									}
			                	} 
			                } 			                
							// 变更前权利model
							List<BDCS_QL_LS> ql_LS = baseCommonDao.getDataList(BDCS_QL_LS.class, "qlid='" + lyqlid + "'");
							// 获取附属权利model
							List<BDCS_FSQL_GZ> fsql_GZ = baseCommonDao.getDataList(BDCS_FSQL_GZ.class, "fsqlid='" + bdcql.getFSQLID()+ "'");
							// 变更前附属权利model
							List<BDCS_FSQL_LS> fsql_LS = null;
							if (ql_LS != null && ql_LS.size() > 0) {
								fsql_LS = baseCommonDao.getDataList(BDCS_FSQL_LS.class,"fsqlid='" + ql_LS.get(0).getFSQLID() + "'");
							}
							List<BDCS_QLR_GZ> qlr_gzList = new ArrayList<BDCS_QLR_GZ>();
							// 循环每一个权利人model
							for (RightsHolder rh : bdcqlrs) {
								List<BDCS_QLR_GZ> qlr_GZ = baseCommonDao.getDataList(BDCS_QLR_GZ.class,"qlrid='" + rh.getId() + "'");
								if (qlr_GZ != null && qlr_GZ.size() > 0) {
									qlr_gzList.add(qlr_GZ.get(0));
								}
							}
							// 变更前权利人
							List<BDCS_QLR_LS> qlr_lsList = new ArrayList<BDCS_QLR_LS>();
							 String xx="";
				                if(xzqdm.equals("220200")){
				                	if(qltablename.equals("FDCQ")&&qlsdfs.equals("3")&&iPushBGQ==1){
				                		xx="yes";
				                	} 
				                }
				                if(xx.equals("yes")){
				                	List<BDCS_H_GZ> h1 = baseCommonDao.getDataList(BDCS_H_GZ.class, "bdcdyh='" + bdcql.getBDCDYH() + "'");
				                	String zdbdcdyid="";
				                	String fwbs="";
				                	if(h1.size()>0){
				                		zdbdcdyid=h1.get(0).getZDBDCDYID();
				                		fwbs=h1.get(0).getFWBM();
				                	}else {
				                		List<BDCS_H_XZY> h12 = baseCommonDao.getDataList(BDCS_H_XZY.class, "bdcdyh='" + bdcql.getBDCDYH() + "'");
				                		if(h12.size()>0){
				                			zdbdcdyid=h12.get(0).getZDBDCDYID();
				                    		fwbs=h12.get(0).getFWBM();
				                		}
									}
				                	List<BDCS_H_LS> h2 = baseCommonDao.getDataList(BDCS_H_LS.class, "zdbdcdyid='" + zdbdcdyid + "'and fwbm like '"+fwbs+"%'");
				                	if(h2.size()>0&&h2!=null){
				                		List<BDCS_QL_LS> q1 = baseCommonDao.getDataList(BDCS_QL_LS.class, "bdcdyh='" + h2.get(0).getBDCDYH() + "'");
				                		String str="";
				                		
				                		
				                		if(xzqdm.equals("220200")){
				                			if(null==q1.get(0).getQLQSSJ()&&null==q1.get(0).getQLJSSJ()){
				                        		str="qlid='" + q1.get(1).getId() + "'";
				                        	}else {
				                        		str="qlid='" + q1.get(0).getId() + "'";
				    						}
				                    		List<BDCS_QLR_LS> qlr_ls = baseCommonDao.getDataList(BDCS_QLR_LS.class,str );
				                    		if(qlr_ls!=null && qlr_ls.size()>0){
				                        		qlr_lsList.add((BDCS_QLR_LS)qlr_ls.get(0));
				                        	}	
				                		}else {
				                			List<BDCS_QLR_LS> qlr_ls = baseCommonDao.getDataList(BDCS_QLR_LS.class, "qlid='" + q1.get(0).getId() + "'");
				                			if(qlr_ls!=null && qlr_ls.size()>0){
				                        		qlr_lsList.add((BDCS_QLR_LS)qlr_ls.get(0));
				                        	}	
				                		}
				                		
				                		
				                		
				                	}else {
				                		if (bdcqlrs_ls != null) {
				                            for (RightsHolder rh : bdcqlrs_ls) {
				                              List<BDCS_QLR_LS> qlr_ls = baseCommonDao.getDataList(BDCS_QLR_LS.class, "qlrid='" + rh.getId() + "'");
				                              if ((qlr_ls != null) && (qlr_ls.size() > 0)) {
				                                qlr_lsList.add((BDCS_QLR_LS)qlr_ls.get(0));
				                              }
				                            }
				                        }
									}	
				                }else {
							      // 循环每一个权利人model
									if (bdcqlrs_ls != null) {
										for (RightsHolder rh : bdcqlrs_ls) {
											List<BDCS_QLR_LS> qlr_ls = baseCommonDao.getDataList(BDCS_QLR_LS.class, "qlrid='"+ rh.getId() + "'");
											if (qlr_ls != null && qlr_ls.size() > 0) {
												qlr_lsList.add(qlr_ls.get(0));
											}
										}
									}
				               }
//							// 循环每一个权利人model
//							
//							if (bdcqlrs_ls != null) {
//								for (RightsHolder rh : bdcqlrs_ls) {
//									List<BDCS_QLR_LS> qlr_ls = baseCommonDao.getDataList(BDCS_QLR_LS.class, "qlrid='"+ rh.getId() + "'");
//									if (qlr_ls != null && qlr_ls.size() > 0) {
//										qlr_lsList.add(qlr_ls.get(0));
//									}
//								}
//							}
							//义务人  取出申请人中的义务人将其存到变更前权利人中
							List<BDCS_QLR_LS> qlr_lsList1 = new ArrayList<BDCS_QLR_LS>();
							List<BDCS_SQR> bdcs_SQRs=baseCommonDao.getDataList(BDCS_SQR.class, "XMBH='"+xmxx.getId()+"' and SQRLB='2'");
							if(bdcs_SQRs!=null&&bdcs_SQRs.size()>0){
								for(int i=0;i<bdcs_SQRs.size();i++){
									BDCS_SQR bdcs_SQR=bdcs_SQRs.get(i);
									BDCS_QLR_LS bdcs_QLR_LS=new BDCS_QLR_LS();
									bdcs_QLR_LS.setId(bdcs_SQR.getId());
									bdcs_QLR_LS.setXMBH(bdcs_SQR.getXMBH());
									bdcs_QLR_LS.setSQRID(bdcs_SQR.getId());
									bdcs_QLR_LS.setQLRMC(bdcs_SQR.getSQRXM());
									bdcs_QLR_LS.setQLRLX(bdcs_SQR.getSQRLB());
									bdcs_QLR_LS.setZJZL(bdcs_SQR.getZJLX());
									bdcs_QLR_LS.setZJH(bdcs_SQR.getZJH());
									bdcs_QLR_LS.setFZJG(bdcs_SQR.getFZJG());
									bdcs_QLR_LS.setXB(bdcs_SQR.getXB());
									bdcs_QLR_LS.setGYFS(bdcs_SQR.getGYFS());
									bdcs_QLR_LS.setDLRXM(bdcs_SQR.getDLRXM());
									bdcs_QLR_LS.setDLRZJHM(bdcs_SQR.getDLRZJHM());
									bdcs_QLR_LS.setDLRZJLX(bdcs_SQR.getDLRZJLX());
									bdcs_QLR_LS.setISCZR(bdcs_SQR.getISCZR());
									if(xzqdm.equals("140200")){
										bdcs_QLR_LS.setDLRLXDH(bdcs_SQR.getDLRLXDH());
										bdcs_QLR_LS.setFDDBR(bdcs_SQR.getFDDBR());
										bdcs_QLR_LS.setFDDBRDH(bdcs_SQR.getFDDBRDH());
										bdcs_QLR_LS.setFDDBRZJHM(bdcs_SQR.getFDDBRZJHM());
										bdcs_QLR_LS.setFDDBRZJLX(bdcs_SQR.getDLRZJLX());
									}
									//推送义务人不动产权证号
									List<BDCS_QLR_XZ> bdcs_QLR_XZs=baseCommonDao.getDataList(BDCS_QLR_XZ.class, "QLID='"+ql_GZ.get(0).getLYQLID()+"'");
									if(bdcs_QLR_XZs!=null&&bdcs_QLR_XZs.size()>0){
										bdcs_QLR_LS.setBDCQZH(bdcs_QLR_XZs.get(0).getBDCQZH());										
									}									
									qlr_lsList1.add(bdcs_QLR_LS);
								}
								setQlr_lsList_bgq1(qlr_lsList1);
							}
							if (fsql_GZ != null && fsql_GZ.size() > 0) {
								// setCasenum(fsql_GZ.get(0).getCASENUM());
								setFsql_gz(fsql_GZ.get(0));
							}
							if (ql_GZ != null && ql_GZ.size() > 0) {
							        if(xzqdm.equals("230200")){
							        	if(xmxx.getDJLX().equals("400")){
							        		if(ql_GZ.get(0).getLYQLID()!=null){
							        			List<BDCS_QL_LS> ql_LSs=baseCommonDao.getDataList(BDCS_QL_LS.class, "qlid='"+ql_GZ.get(0).getLYQLID()+"'");
							        			setCasenum(ql_LSs.size()>0?ql_LSs.get(0).getCASENUM():"");
							        		}
							        	}else{
								        	setCasenum(ql_GZ.get(0).getCASENUM());
								        }
							        }else{
							        	setCasenum(ql_GZ.get(0).getCASENUM());
							        }
							        if(xzqdm.equals("650200")){
							        	ql_GZ.get(0).setYWH(xmxx.getPROJECT_ID());
							        }
								setQl_gz(ql_GZ.get(0));
							}
							setQlr_gz(qlr_gzList);
							setXmxx(xmxx);
							// 变更前
							if (ql_LS != null && ql_LS.size() > 0) {
								setQl_ls_bgq(ql_LS.get(0));
							}
							if (fsql_LS != null && fsql_LS.size() > 0) {
								setFsql_ls_bgq(fsql_LS.get(0));
							}
							setQlr_lsList_bgq(qlr_lsList);
							//保证登簿后推送项目共享项目编号与受理转出相同 2016年8月31日 08:33:22 卜晓波
							if(relationid2gxxmbh.get("SLZCNoPush")!=null|| relationid2gxxmbh.size()==0){
								gxxmbh = UUID.randomUUID().toString().replace("-","");
							}else{
								if(bljd.equals("2") ){
									String relationid="";
									if(h_xz!=null){
										relationid=h_xz.getRELATIONID();
									}else if(h_XZY!=null&& relationid.equals("")){
										relationid=h_XZY.getRELATIONID();
									}else if(h_GZ!=null&& relationid.equals("")){
										relationid=h_GZ.getRELATIONID();
									}
									gxxmbh = relationid2gxxmbh.get(relationid);
								}else{
									gxxmbh = UUID.randomUUID().toString().replace("-","");
									}
							}
							//吉林登簿推送时的共享项目编号和受理不能一样 2016年12月6日 22:20:59 卜晓波
							String xzqdm =ConfigHelper.getNameByValue("XZQHDM");
							if(gxxmbh==null||gxxmbh.equals("")||xzqdm.equals("220200")){
								gxxmbh = UUID.randomUUID().toString().replace("-","");
							}
							updateYGDJZXYWH(baseCommonDao);
							//吉林续查封业务号修改为上手查封业务号
							updateXCFDJYWH(baseCommonDao);
							//赣州市房产特殊需求 抵押查封推送的权利的来源权利ID字段修改为上一手业务的业务号
							updateDYCFLYQLID(baseCommonDao);
							// logger.info("开始保存权利人信息...");
							saveQLR(baseCommonDao,CommonDaoDJ,eNCRYPTION);
							// logger.info("开始保存户信息...");
							//注销-房屋所有权和房屋灭失不需要推送H的信息-崇左
							if (!qltablename.equals("FDCQZXDJ")) {
								saveH(CommonDaoDJ);
							}
							// logger.info("开始保存权利信息...");
							saveQL(qltablename, false,CommonDaoDJ);
							// 保存变更前
							if (bPushBGQ) {
								saveH_BGQ(CommonDaoDJ);
								// logger.info("开始保存变更前数据...");
								saveQL(qltablename, bPushBGQ,CommonDaoDJ);
								saveQLR_BGQ(baseCommonDao,CommonDaoDJ);
							}else{
								//不保存变更前的业务 就把 义务人存到变更前权利人表供房产用   否则变更前权利人和义务人会重复
								saveYWR(baseCommonDao,CommonDaoDJ);
							}
							// 遂宁抵押登记推送所有权信息
							if (pushDYFWSYQ&&qltablename.toUpperCase().equals("DYAQ")) {
								saveSYQToBGQ(baseCommonDao, djdy.getDJDYID(),CommonDaoDJ);
							}
							// 克拉玛依查封登记推送所有权信息，邢台预告注销也推所有权人
							if (pushDYFWSYQ&&(qltablename.toUpperCase().equals("CFDJ")||(qltablename.toUpperCase().equals("YGDJ")&&xmxx.getDJLX().equals("400")))) {
								//吉林市查封登记推送申请人表中的代理人给房产
								if (xzqdm != null && xzqdm.contains("2202")) {
									saveSYQTogxdjk(baseCommonDao, xmxx.getId(),CommonDaoDJ);
								}else{
									saveSYQTogxdjk(baseCommonDao, djdy.getDJDYID(),CommonDaoDJ);
								}
								//克拉玛依查封时还推送抵押权人
								if (xzqdm != null && xzqdm.contains("6502")) {
									saveDYAQRTogxdjk(baseCommonDao, djdy.getDJDYID(),CommonDaoDJ);
								}
//								//预告注销，业务号改存预告业务号
//								updateYGDJZXYWH(baseCommonDao);
							}
							//修改业务号为来源权利的业务号
							//updateYGDJZXYWH(baseCommonDao);
							if (!bXMXXSaved) {
								// bXMXXSaved=true;
								String sqr = getSQR(baseCommonDao, xmxx.getId());
								// logger.info("开始保存项目信息...");
								// 保存项目信息
								HandlerMapping _mapping = HandlerFactory.getMapping();
								String workflowcode = ProjectHelper.getWorkflowCodeByProjectID(xmxx.getPROJECT_ID());
								String _handleClassName = _mapping.getHandlerClassName(workflowcode);
								//崇左-转移+预抵押转现
								if (_handleClassName.contains("ZY_YDYTODY_DJHandler") && qltablename.contains("DYAQ") && xzqdm.contains("451400")) {
									nowdjdl="700";
									qlsdfs="7";
								}
								saveGXXMXX(bdcdyh, "1", bljd, qlsdfs, casenum, sqr,IsCombinnation,nowqllx,nowdjdl,baseCommonDao,CommonDaoDJ);
							}
						}
//						session.flush();
					}
				} catch (Exception ex) {
					allSuccess=false;
					FailCause=ex.toString();
					logger.error(ex.getMessage()+",推送失败项目Project_id:"+xmxx.getPROJECT_ID()+",办理阶段："+bljd+"");
				}
//				checkAndPushXZDJ(baseCommonDao);
			}
		}
	}


	/**
	 * 分割单独推送方法
	 */
	public void BGpushToGXDJK(
			com.supermap.wisdombusiness.framework.dao.impl.CommonDao baseCommonDao,com.supermap.wisdombusiness.framework.dao.impl.CommonDaoDJ CommonDaoDJ,
			BDCS_XMXX xmxx, String qltablename, String bljd, String qlsdfs,
			int iPushBGQ, String djxl, Map<String, String> relationid2gxxmbh,String eNCRYPTION) {
		if (xmxx == null || baseCommonDao == null) {
			logger.info("xmxx、dao为null");
			return;
		}
		sfdb=xmxx.getSFDB();
		setCommonDao(baseCommonDao);
		Boolean bXMXXSaved = false;
		// bljd 办理进度， 0初始推送；1审批；2登簿；3缮证；4发证；5归档；
		setBljd(bljd);
		// zt 状态，1为有效；2为无效
		// setZt(zt);
		// 登记小类
		setDjxl(djxl);
		// qlsdfs 权利设定方式，1设立，2转移，3变更，4注销
		setQlsdfs(qlsdfs);
		if (iPushBGQ == 1) {
			setbPushBGQ(true);
		}
		//setQltablename(qltable);
		String xmbhFilter = ProjectHelper.GetXMBHCondition(xmxx.getId());
		List<BDCS_DJDY_GZ> djdys = baseCommonDao.getDataList(
				BDCS_DJDY_GZ.class, xmbhFilter);
		if (djdys != null && djdys.size() > 0) {
			//此处应先循环取出被分割户设置为变更前户，
			for (int idjdy = 0; idjdy < djdys.size(); idjdy++) {
				BDCS_DJDY_GZ djdy = djdys.get(idjdy);
//				String bdcdyh = djdy.getBDCDYH();
				String bdcdyids = djdy.getBDCDYID();
				setBdcdyly(djdy.getBDCDYLX());
				djdyly = djdy.getLY();
				if (bdcdyly.equals(BDCDYLX.H.Value)) {
					// 实测户
					if (djdyly.equals(DJDYLY.XZ.Value)) {
						// 来源现在
						// 变更后
//						List<BDCS_H_XZ> h_XZs = baseCommonDao.getDataList(
//								BDCS_H_XZ.class, "bdcdyid='" + bdcdyid
//										+ "'");
						// 变更前户
						List<BDCS_H_LS> h_LSs = baseCommonDao.getDataList(
								BDCS_H_LS.class, "bdcdyid='" + bdcdyids
										+ "'");
						if (h_LSs != null && h_LSs.size() > 0) {
							setH_ls_bgq(h_LSs.get(0));
						}
					}
				} else {
					return;
				}
			}
			for (int idjdy = 0; idjdy < djdys.size(); idjdy++) {
				try {
					BDCS_DJDY_GZ djdy = djdys.get(idjdy);
					// 获取户model，户里包含houseid
					String bdcdyh = djdy.getBDCDYH();
					String bdcdyids = djdy.getBDCDYID();
					setBdcdyly(djdy.getBDCDYLX());
					djdyly = djdy.getLY();
					if (bdcdyly.equals(BDCDYLX.H.Value)) {
						String xzqdm =ConfigHelper.getNameByValue("XZQHDM");
						if(xzqdm.equals("450000")){
							if (djdyly.equals(DJDYLY.XZ.Value)) {
								// 来源现在
								// 变更后
								List<BDCS_H_XZ> h_XZs = baseCommonDao.getDataList(
										BDCS_H_XZ.class, "bdcdyid='" + bdcdyid
												+ "'");
								if(h_XZs != null && h_XZs.size() > 0) {
									setH_xz(h_XZs.get(0));
								}
								
								// 变更前户
								List<BDCS_H_LS> h_LSs = baseCommonDao.getDataList(
										BDCS_H_LS.class, "bdcdyid='" + bdcdyid
												+ "'");
								if (h_LSs != null && h_LSs.size() > 0) {
									setH_ls_bgq(h_LSs.get(0));
								}
							}
						}										
						if (djdyly.equals(DJDYLY.GZ.Value)) {
							// 来源工作
							// 变更后
							List<BDCS_H_GZ> h_GZs = baseCommonDao.getDataList(
									BDCS_H_GZ.class, "bdcdyid='" + bdcdyids
											+ "'");
							if (h_GZs != null && h_GZs.size() > 0) {
								setH_GZ(h_GZs.get(0));// setH_xz(h_GZs.get(0));
								//准备推送给房产的土地性质、土地用途、土地面积等字段 2016年11月21日 15:03:42卜晓波
								try{
									List<BDCS_SHYQZD_XZ> zds = baseCommonDao.getDataList(BDCS_SHYQZD_XZ.class, "bdcdyid='" + h_GZs.get(0).getZDBDCDYID()+ "'");
									if(zds!=null && zds.size()>0){
										setTdxz(zds.get(0).getQLXZ()!=null?zds.get(0).getQLXZ():"");
										setTdyt(zds.get(0).getYT()!=null?zds.get(0).getYT():"");
										setTdmj(Double.toString(zds.get(0).getMJ()));
									}
								}catch(Exception e){
									logger.error("推送土地信息时出错："+e.toString()+",推送失败项目Project_id:"+xmxx.getPROJECT_ID()+",办理阶段："+bljd+"");
								}
							}
						}
					} else if (bdcdyly.equals(BDCDYLX.YCH.Value)) {
						// 预测户
						List<BDCS_H_XZY> h_XZs = baseCommonDao.getDataList(
								BDCS_H_XZY.class, "bdcdyid='" + bdcdyids + "'");
						// 变更前户
						List<BDCS_H_LSY> h_LSs = baseCommonDao.getDataList(
								BDCS_H_LSY.class, "bdcdyid='" + bdcdyids + "'");
						if (h_XZs != null && h_XZs.size() > 0) {
							setH_XZY(h_XZs.get(0));
							//准备推送给房产的土地性质、土地用途、土地面积等字段 2016年11月21日 15:03:42卜晓波
							List<BDCS_SHYQZD_XZ> zds = baseCommonDao.getDataList(BDCS_SHYQZD_XZ.class, "bdcdyid='" + h_XZs.get(0).getZDBDCDYID()+ "'");
							if(zds!=null && zds.size()>0){
								setTdxz(zds.get(0).getQLXZ()!=null?zds.get(0).getQLXZ():"");
								setTdyt(zds.get(0).getYT()!=null?zds.get(0).getYT():"");
								setTdmj(Double.toString(zds.get(0).getMJ()));
							}
						}
						if (h_LSs != null && h_LSs.size() > 0) {
							setH_LSY(h_LSs.get(0));
						}
					} else {
						return;
					}
					// ConstValue.BDCDYLX dylx =
					// ConstValue.BDCDYLX.initFrom(djdy
					// .getBDCDYLX());
					// ConstValue.DJDYLY dyly = ConstValue.DJDYLY.initFrom(djdy
					// .getLY());
					// RealUnit bdcdy = UnitTools.loadUnit(dylx, dyly,
					// djdy.getBDCDYID());
					//并案流程一个项目对应一条xmxx，一条djdy，多条权利
					List<Rights> bdcRights=RightsTools.loadRightsByCondition(ConstValue.DJDYLY.GZ, "XMBH='"+xmxx.getId()+"' and DJDYID='"+djdy.getDJDYID()+"'");
					if(bdcRights!=null&&bdcRights.size()>0){
//						Session session=CommonDaoDJ.getCurrentSession();
//						session.setFlushMode(FlushMode.MANUAL);
						for(int k=0;k<bdcRights.size();k++){
							Rights bdcql =bdcRights.get(k);// RightsTools.loadRightsByDJDYID(ConstValue.DJDYLY.GZ, xmxx.getId(),djdy.getDJDYID());
							boolean IsCombinnation=false;
							String nowqllx;
							nowqllx=xmxx.getQLLX();
							String nowdjdl=xmxx.getDJLX();
							if(qltablename.equals("YGDJ")){
								nowqllx="4";
							}
							//如果是并案的权利有两条
							if(bdcRights.size()>1){
								nowqllx=bdcql.getQLLX();
								nowdjdl=bdcql.getDJLX();
								String nowqlid=bdcql.getId();
								//检查当前权利表名称和当前权利类型是否一致进而判断当前该不该保存对应的ql、qlr、h、xmxx
								boolean HaveSave=CheckQllxandQLtable(nowqllx,nowqlid,qltablename,CommonDaoDJ);
								IsCombinnation=true;
								if(HaveSave){
									continue;
								}
							}
							String lyqlid = bdcql.getLYQLID();
							// bdcql.setId(lyqlid);
							// SubRights bdcfsql = RightsTools.loadSubRightsByRightsID(
							// ConstValue.DJDYLY.GZ, bdcql.getId());
//							if(qltablename.equals("XZDJ")){}ELSE
							List<RightsHolder> bdcqlrs = RightsHolderTools.loadRightsHolders(ConstValue.DJDYLY.GZ,bdcql.getId());
							// 变更前权利人
							List<RightsHolder> bdcqlrs_ls = RightsHolderTools.loadRightsHolders(ConstValue.DJDYLY.LS, lyqlid);
							// 获取权利model
							List<BDCS_QL_GZ> ql_GZ = baseCommonDao.getDataList(BDCS_QL_GZ.class, "qlid='" + bdcql.getId() + "'");
							// 变更前权利model
							List<BDCS_QL_LS> ql_LS = baseCommonDao.getDataList(BDCS_QL_LS.class, "qlid='" + lyqlid + "'");
							// 获取附属权利model
							List<BDCS_FSQL_GZ> fsql_GZ = baseCommonDao.getDataList(BDCS_FSQL_GZ.class, "fsqlid='" + bdcql.getFSQLID()+ "'");
							// 变更前附属权利model
							List<BDCS_FSQL_LS> fsql_LS = null;
							if (ql_LS != null && ql_LS.size() > 0) {
								fsql_LS = baseCommonDao.getDataList(BDCS_FSQL_LS.class,"fsqlid='" + ql_LS.get(0).getFSQLID() + "'");
							}
							List<BDCS_QLR_GZ> qlr_gzList = new ArrayList<BDCS_QLR_GZ>();
							// 循环每一个权利人model
							for (RightsHolder rh : bdcqlrs) {
								List<BDCS_QLR_GZ> qlr_GZ = baseCommonDao.getDataList(BDCS_QLR_GZ.class,
												"qlrid='" + rh.getId() + "'");
								if (qlr_GZ != null && qlr_GZ.size() > 0) {
									qlr_gzList.add(qlr_GZ.get(0));
								}
							}
							// 变更前权利人
							List<BDCS_QLR_LS> qlr_lsList = new ArrayList<BDCS_QLR_LS>();
							// 循环每一个权利人model
							if (bdcqlrs_ls != null) {
								for (RightsHolder rh : bdcqlrs_ls) {
									List<BDCS_QLR_LS> qlr_ls = baseCommonDao.getDataList(BDCS_QLR_LS.class, "qlrid='"+ rh.getId() + "'");
									if (qlr_ls != null && qlr_ls.size() > 0) {
										qlr_lsList.add(qlr_ls.get(0));
									}
								}
							}
							//义务人  取出申请人中的义务人将其存到变更前权利人中
							List<BDCS_QLR_LS> qlr_lsList1 = new ArrayList<BDCS_QLR_LS>();
							List<BDCS_SQR> bdcs_SQRs=baseCommonDao.getDataList(BDCS_SQR.class, "XMBH='"+xmxx.getId()+"' and SQRLB='2'");
							if(bdcs_SQRs!=null&&bdcs_SQRs.size()>0){
								for(int i=0;i<bdcs_SQRs.size();i++){
									BDCS_SQR bdcs_SQR=bdcs_SQRs.get(i);
									BDCS_QLR_LS bdcs_QLR_LS=new BDCS_QLR_LS();
									bdcs_QLR_LS.setId(bdcs_SQR.getId());
									bdcs_QLR_LS.setXMBH(bdcs_SQR.getXMBH());
									bdcs_QLR_LS.setSQRID(bdcs_SQR.getId());
									bdcs_QLR_LS.setQLRMC(bdcs_SQR.getSQRXM());
									bdcs_QLR_LS.setQLRLX(bdcs_SQR.getSQRLB());
									bdcs_QLR_LS.setZJZL(bdcs_SQR.getZJLX());
									bdcs_QLR_LS.setZJH(bdcs_SQR.getZJH());
									bdcs_QLR_LS.setFZJG(bdcs_SQR.getFZJG());
									bdcs_QLR_LS.setXB(bdcs_SQR.getXB());
									bdcs_QLR_LS.setGYFS(bdcs_SQR.getGYFS());
									bdcs_QLR_LS.setDLRXM(bdcs_SQR.getDLRXM());
									bdcs_QLR_LS.setDLRZJHM(bdcs_SQR.getDLRZJHM());
									bdcs_QLR_LS.setDLRZJLX(bdcs_SQR.getDLRZJLX());
									bdcs_QLR_LS.setISCZR(bdcs_SQR.getISCZR());
									qlr_lsList1.add(bdcs_QLR_LS);
								}
								setQlr_lsList_bgq1(qlr_lsList1);
							}
							if (fsql_GZ != null && fsql_GZ.size() > 0) {
								// setCasenum(fsql_GZ.get(0).getCASENUM());
								setFsql_gz(fsql_GZ.get(0));
							}
							if (ql_GZ != null && ql_GZ.size() > 0) {
							        if(xzqdm.equals("230200")){
							        	if(xmxx.getDJLX().equals("400")){
							        		if(ql_GZ.get(0).getLYQLID()!=null){
							        			List<BDCS_QL_LS> ql_LSs=baseCommonDao.getDataList(BDCS_QL_LS.class, "qlid='"+ql_GZ.get(0).getLYQLID()+"'");
							        			setCasenum(ql_LSs.size()>0?ql_LSs.get(0).getCASENUM():"");
							        		}
							        	}else{
								        	setCasenum(ql_GZ.get(0).getCASENUM());
								        }
							        }else{
							        	setCasenum(ql_GZ.get(0).getCASENUM());
							        }
							        if(xzqdm.equals("650200")){
							        	ql_GZ.get(0).setYWH(xmxx.getPROJECT_ID());
							        }
								setQl_gz(ql_GZ.get(0));
							}
							setQlr_gz(qlr_gzList);
							setXmxx(xmxx);
							// 变更前
							if (ql_LS != null && ql_LS.size() > 0) {
								setQl_ls_bgq(ql_LS.get(0));
							}
							if (fsql_LS != null && fsql_LS.size() > 0) {
								setFsql_ls_bgq(fsql_LS.get(0));
							}
							setQlr_lsList_bgq(qlr_lsList);
							//保证登簿后推送项目共享项目编号与受理转出相同 2016年8月31日 08:33:22 卜晓波
							if(relationid2gxxmbh.get("SLZCNoPush")!=null|| relationid2gxxmbh.size()==0){
								gxxmbh = UUID.randomUUID().toString().replace("-","");
							}else{
								if(bljd.equals("2") ){
									String relationid="";
									if(h_xz!=null){
										relationid=h_xz.getRELATIONID();
									}else if(h_XZY!=null&& relationid.equals("")){
										relationid=h_XZY.getRELATIONID();
									}else if(h_GZ!=null&& relationid.equals("")){
										relationid=h_GZ.getRELATIONID();
									}
									gxxmbh = relationid2gxxmbh.get(relationid);
								}else{
									gxxmbh = UUID.randomUUID().toString().replace("-","");
									}
							}
							//吉林登簿推送时的共享项目编号和受理不能一样 2016年12月6日 22:20:59 卜晓波
							String xzqdm =ConfigHelper.getNameByValue("XZQHDM");
							if(gxxmbh==null||gxxmbh.equals("")||xzqdm.equals("220200")){
								gxxmbh = UUID.randomUUID().toString().replace("-","");
							}
							updateYGDJZXYWH(baseCommonDao);
							//吉林续查封业务号修改为上手查封业务号
							updateXCFDJYWH(baseCommonDao);
							//赣州市房产特殊需求 抵押查封推送的权利的来源权利ID字段修改为上一手业务的业务号
							updateDYCFLYQLID(baseCommonDao);
							// logger.info("开始保存权利人信息...");
							saveQLR(baseCommonDao,CommonDaoDJ,eNCRYPTION);
							// logger.info("开始保存户信息...");
							saveH(CommonDaoDJ);
							// logger.info("开始保存权利信息...");
							saveQL(qltablename, false,CommonDaoDJ);
							// 保存变更前
							if (bPushBGQ) {
								saveH_BGQ(CommonDaoDJ);
								// logger.info("开始保存变更前数据...");
								saveQL(qltablename, bPushBGQ,CommonDaoDJ);
								saveQLR_BGQ(baseCommonDao,CommonDaoDJ);
							}else{
								//不保存变更前的业务 就把 义务人存到变更前权利人表供房产用   否则变更前权利人和义务人会重复
								saveYWR(baseCommonDao,CommonDaoDJ);
							}
							// 遂宁抵押登记推送所有权信息
							if (pushDYFWSYQ&&qltablename.toUpperCase().equals("DYAQ")) {
								saveSYQToBGQ(baseCommonDao, djdy.getDJDYID(),CommonDaoDJ);
							}
							// 克拉玛依查封登记推送所有权信息，邢台预告注销也推所有权人
							if (pushDYFWSYQ&&(qltablename.toUpperCase().equals("CFDJ")||(qltablename.toUpperCase().equals("YGDJ")&&xmxx.getDJLX().equals("400")))) {
								//吉林市查封登记推送申请人表中的代理人给房产
								if (xzqdm != null && xzqdm.contains("2202")) {
									saveSYQTogxdjk(baseCommonDao, xmxx.getId(),CommonDaoDJ);
								}else{
									saveSYQTogxdjk(baseCommonDao, djdy.getDJDYID(),CommonDaoDJ);
								}
								//克拉玛依查封时还推送抵押权人
								if (xzqdm != null && xzqdm.contains("6502")) {
									saveDYAQRTogxdjk(baseCommonDao, djdy.getDJDYID(),CommonDaoDJ);
								}
//								//预告注销，业务号改存预告业务号
//								updateYGDJZXYWH(baseCommonDao);
							}
							//修改业务号为来源权利的业务号
							//updateYGDJZXYWH(baseCommonDao);
							if (!bXMXXSaved) {
								// bXMXXSaved=true;
								String sqr = getSQR(baseCommonDao, xmxx.getId());
								// logger.info("开始保存项目信息...");
								// 保存项目信息
								saveGXXMXX(bdcdyh, "1", bljd, qlsdfs, casenum, sqr,IsCombinnation,nowqllx,nowdjdl,baseCommonDao,CommonDaoDJ);
							}
						}
//						session.flush();
					}
				} catch (Exception ex) {
					allSuccess=false;
					FailCause=ex.toString();
					logger.error(ex.getMessage()+",推送失败项目Project_id:"+xmxx.getPROJECT_ID()+",办理阶段："+bljd+"");
				}
//				checkAndPushXZDJ(baseCommonDao);
			}
		}
	}

	

	//检查当前权利表名称和当前权利类型是否一致进而判断当前该不该保存对应的ql、qlr、h、xmxx
	private boolean CheckQllxandQLtable(String nowqllx, String qlid, String qltablename, CommonDaoDJ commonDaoDJ) throws SQLException {
		boolean Havesaved=false;
		if(nowqllx.equals("23") && qltablename.equals("DYAQ")){
			List<Dyaq_dj> dyaq_djs=commonDaoDJ.getDataList(Dyaq_dj.class, "QLID='"+ qlid + "'");
			if(dyaq_djs!=null&& dyaq_djs.size()>0){
				//组合业务推送时，齐齐哈尔房产触发器要求只能通过qlid判断，只在受理推送一次，但其他地方两次都正常推送
				String xzqdm =ConfigHelper.getNameByValue("XZQHDM");
				if(xzqdm.contains("2302")){
					Havesaved=true;
					return Havesaved;
				}else{
					Havesaved=true;
					return false;
				}
			}
		}else if((nowqllx.equals("4")||nowqllx.equals("99")) && qltablename.equals("YGDJ")){
			List<Ygdj_dj> ygdj_djs=commonDaoDJ.getDataList(Ygdj_dj.class, "QLID='"+ qlid + "'");
			if(ygdj_djs!=null&& ygdj_djs.size()>0){
				String xzqdm =ConfigHelper.getNameByValue("XZQHDM");
				if(xzqdm.contains("2302")){
					Havesaved=true;
					return Havesaved;
				}else{
					Havesaved=true;
					return false;
				}
			}
		}else if((nowqllx.equals("4")||nowqllx.equals("6")||nowqllx.equals("8")) && qltablename.equals("FDCQ")){
//			String sql = "select * from gxdjk.FDCQ where QLID='"+ qlid + "'";
			List<Fdcq_dj> fdcq_djs=commonDaoDJ.getDataList(Fdcq_dj.class, "QLID='"+ qlid + "'");
			if(fdcq_djs!=null&& fdcq_djs.size()>0){
				String xzqdm =ConfigHelper.getNameByValue("XZQHDM");
				if(xzqdm.contains("2302")){
					Havesaved=true;
					return Havesaved;
				}else{
					Havesaved=true;
					return false;
				}
			}
		}else{
			Havesaved=true;
		}
		return Havesaved;
	}

	/**
	 * 
	 * @作者 likun
	 * @创建时间 2015年12月23日下午1:58:58
	 * @param bdcdyh
	 *            不动产单元号
	 * @param xmxx
	 *            项目信息model
	 * @param zt
	 *            状态，1为有效；2为无效
	 * @param bljd
	 *            办理进度， 0初始推送；1审批；2登簿；3缮证；4发证；5归档；
	 * @param qlsdfs
	 *            权利设定方式，1设立，2转移，3变更，4注销
	 * @param casenum
	 *            房产系统案卷号
	 * @param nowdjdl 
	 * @param nowqllx 
	 * @param isCombinnation 
	 * @throws Exception 
	 */
	@SuppressWarnings("rawtypes")
	protected void saveGXXMXX(String bdcdyh, String zt, String bljd,
			String qlsdfs, String casenum, String sqr, boolean isCombinnation, String nowqllx, String nowdjdl,com.supermap.wisdombusiness.framework.dao.impl.CommonDao baseCommonDao,com.supermap.wisdombusiness.framework.dao.impl.CommonDaoDJ CommonDaoDJ) throws Exception {
		String xmmc = xmxx.getXMMC();
		if(xzqdm.equals("451400")){
			String sqlString="select * from bdc_workflow.wfi_proinst where FILE_NUMBER ='" + this.xmxx.getPROJECT_ID() + "'";
			   List<Map> xmxc = baseCommonDao.getDataListByFullSql(sqlString);
			   if(xmxc.size()>0){
				 xmmc=(String) xmxc.get(0).get("PROJECT_NAME");
			   }
		}		
//		gxxmbh = UUID.randomUUID().toString().replace("-","");
		String prjId = xmxx.getPROJECT_ID();
		if(djdl==null||djdl.equals("")){
			djdl=xmxx.getDJLX();
		}
		//预告抵押注销的推送登记大类通过流程定义名称判断推送值为700，因handlername跟抵押注销一样无法判断
		List<Map> Wfd_Prodeflist = baseCommonDao.getDataListByFullSql("SELECT * FROM BDCK.T_BASEWORKFLOW WHERE ID IN (SELECT WORKFLOWNAME FROM BDC_WORKFLOW.WFD_MAPPING WHERE WORKFLOWCODE IN (SELECT PROINST_CODE FROM BDC_WORKFLOW.WFI_PROINST T WHERE T.FILE_NUMBER ='"+xmxx.getPROJECT_ID()+"'))");
		if (Wfd_Prodeflist != null ) {
			for (Map wfd_Prodef : Wfd_Prodeflist) {
				String PRODEF_NAME=wfd_Prodef.get("NAME").toString();
				if (PRODEF_NAME.equals("注销登记_抵押权_预测户（预抵押注销）")||PRODEF_NAME.equals("注销登记_预告房屋所有权")||PRODEF_NAME.equals("注销登记_转移预告_国有建设用地使用权/房屋所有权")||
						PRODEF_NAME.equals("注销登记_抵押权预告_户")||PRODEF_NAME.equals("注销登记_转移预告_使用权宗地")||PRODEF_NAME.equals("注销登记_抵押权预告_使用权宗地")||PRODEF_NAME.equals("注销登记_转移预告_国有建设用地使用权/房屋所有权")) {
						djdl="700";
				}else if(PRODEF_NAME.equals("注销登记_抵押权_在建工程抵押注销")){
					//吉林预抵押注销 在建工程抵押注销推送登记大类仍为400
					if(xzqdm.contains("2202")){
						djdl="400";
					}
				}
			}
		}
		//吉林市推送登记小类,取project_id第三段 2016年10月26日 14:40:44 卜晓波
		String xzqdm =ConfigHelper.getNameByValue("XZQHDM");
		if(!xzqdm.equals("130100")&&!xzqdm.equals("130108")){
			String[] proarr=xmxx.getPROJECT_ID().split("-");
			if(proarr!=null&&proarr.length>2)
				//这有个大坑！之前没有发现，project_id第三段是PRODEF_CODE，有的新配出来的流程实际值是13超了中间库项目表DJXL的最大值才10，导致gxjhxm记录插不进去！！！反正房产又不用这个做判断了，这就加个控制，把长度控制到10以内  2016年12月7日 03:47:17 卜晓波
				if(proarr[2].length()>10){
					proarr[2]=proarr[2].substring(0, 9);
					djxl=proarr[2];
				}else{
					djxl=proarr[2];
				}
		}
		if(djdl==null||djdl.equals("null")){
			djdl="";
		}
		String qllx = xmxx.getQLLX();
		//齐齐哈尔二逼需求，在建工程抵押注销，权利类型非要改成98
		if(xzqdm!=null&&xzqdm.contains("2302")){
			if(djdl.equals("400")&&qllx.equals("23")&&bdcdyly.equals(BDCDYLX.YCH.Value)){
				qllx="98";
			}
		}
		String slry = xmxx.getSLRY();
		Date slsj = xmxx.getSLSJ();
		String bsmString= UUID.randomUUID().toString().replace("-","");
		String[] vls = new String[13];
		vls[0] = gxxmbh;
		vls[1] = xmmc;
		if(isCombinnation){
			vls[2] = nowdjdl;
			vls[3] = nowqllx;
		}else{
			vls[2] = djdl;
			vls[3] = qllx;
		}
		vls[4] = bdcdyh;
		vls[5] = slry;
		vls[6] = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(slsj);
		vls[7] = zt;
		vls[8] = bljd;
		vls[9] = qlsdfs;
		//如果casenum为空就给赋gxxmbh
		String ajh=casenum;
		if(casenum==null||casenum.equals("null")){
			ajh="";//gxxmbh
		}
		vls[10] = ajh;
		vls[11] = sqr;
		vls[12] = xmxx.getYWLSH();
		Date dt = new Date();
		String nowString = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(dt);
		//堃哥 你提交这没有考虑到吉林的共享登记库的共享交换项目表人家房产不能加字段
		String sql="";
		if (xzqdm.equals("220221")) {
		    prjId = this.xmxx.getYWLSH();
		  }
		if(xzqdm.contains("2202")){
			sql = "insert into gxdjk.GXJHXM (GXXMBH,PROJECT_ID,XMMC,DJDL,DJXL,QLLX,BDCDYH,SLRY,SLSJ,ZT,BLJD,QLSDFS,CASENUM,SQR,TSSJ,BSM) values ('"
					+ vls[0]
					+ "','"
					+ prjId
					+ "','"
					+ vls[1]
					+ "','"
					+ vls[2]
					+ "','"
					+ djxl
					+ "','"
					+ vls[3]
					+ "','"
					+ vls[4]
					+ "','"
					+ vls[5]
					+ "',"
					+ vls[6]
					+ ",'"
					+ vls[7]
					+ "','"
					+ vls[8]
					+ "','"
					+ vls[9]
					+ "','"
					+ vls[10]
					+ "','"
					+ vls[11]
					+ "',"
					+ nowString +",'"+bsmString+ "')";
			String tablename=Gxjhxm_dj.class.getName();
			String[] otherfields = { "GXXMBH", "PROJECT_ID","XMMC","DJDL","DJXL","QLLX","BDCDYH","SLRY","SLSJ","ZT","BLJD","QLSDFS","CASENUM","SQR","TSSJ","BSM" };
			String[] othervalues = { vls[0],prjId,vls[1],vls[2],djxl,vls[3], vls[4],vls[5], vls[6],vls[7],vls[8],vls[9],vls[10],vls[11],nowString,bsmString};
			//一个项目推送要一个事务提交，在工具类里增加通用方法直接返回待保存对象 2017年1月18日 17:17:39 卜晓波
			try {
				//此处共享交换项目表是拼接所有字段推送的，改为统一事务提交此处重新构造
				Gxjhxm_dj gxjhxm=Tools.GxxmxxcreatePushObj(tablename, otherfields, othervalues);
				SaveLog_dj(gxjhxm,CommonDaoDJ);
				CommonDaoDJ.save(gxjhxm);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			sql = "insert into gxdjk.GXJHXM (GXXMBH,PROJECT_ID,XMMC,DJDL,DJXL,QLLX,BDCDYH,SLRY,SLSJ,ZT,BLJD,QLSDFS,CASENUM,SQR,TSSJ,BSM,xzqdm,xzqmc) values ('"
					+ vls[0]
					+ "','"
					+ prjId
					+ "','"
					+ vls[1]
					+ "','"
					+ vls[2]
					+ "','"
					+ djxl
					+ "','"
					+ vls[3]
					+ "','"
					+ vls[4]
					+ "','"
					+ vls[5]
					+ "',"
					+ vls[6]
					+ ",'"
					+ vls[7]
					+ "','"
					+ vls[8]
					+ "','"
					+ vls[9]
					+ "','"
					+ vls[10]
					+ "','"
					+ vls[11]
					+ "',"
					+ nowString +",'"+bsmString+ "','"+getXzqdm()+"','"+getXzqmc()+"')";
			String tablename=Gxjhxm_dj.class.getName();
			String[] otherfields = { "GXXMBH", "PROJECT_ID","XMMC","DJDL","DJXL","QLLX","BDCDYH","SLRY","SLSJ","ZT","BLJD","QLSDFS","CASENUM","SQR","YWLSH","TSSJ","BSM" ,"XZQDM","XZQMC"};
			String[] othervalues = { vls[0],prjId,vls[1],vls[2],djxl,vls[3], vls[4],vls[5], vls[6],vls[7],vls[8],vls[9],vls[10],vls[11],vls[12],nowString,bsmString,getXzqdm(),getXzqmc()};
			//一个项目推送要一个事务提交，在工具类里增加通用方法直接返回待保存对象 2017年1月18日 17:17:39 卜晓波
			try {
				//此处共享交换项目表是拼接所有字段推送的，改为统一事务提交此处重新构造
				Gxjhxm_dj gxjhxm=Tools.GxxmxxcreatePushObj(tablename, otherfields, othervalues);
				SaveLog_dj(gxjhxm,CommonDaoDJ);
				CommonDaoDJ.save(gxjhxm);
			} catch (SQLException e) {
				allSuccess=false;
				FailCause=e.toString();
				logger.error("出错：" + sql);
			}
		}
	}

	/** 
	* @author  buxiaobo
	* @date 创建时间：2017年6月13日 下午5:35:02 
	* @version 1.0 
	* @parameter  
	* @since  
	* @return  
	*/
		Log_dj log_dj=new Log_dj();
		private void SaveLog_dj(Gxjhxm_dj gxjhxm, CommonDaoDJ commonDaoDJ) {
		log_dj.setBsm(gxjhxm.getBsm());
		log_dj.setGxxmbh(gxjhxm.getGxxmbh());
		log_dj.setBdcdyh(gxjhxm.getBdcdyh());
		log_dj.setBljd(gxjhxm.getBljd());
		log_dj.setProject_id(gxjhxm.getProject_id());
		log_dj.setTssj(gxjhxm.getTssj());
		log_dj.setYwlsh(gxjhxm.getYwlsh());
		log_dj.setDjdl(gxjhxm.getDjdl());
		log_dj.setQllx(gxjhxm.getQllx());
		commonDaoDJ.save(log_dj);
//		CommonDaoDJ.flush();
	}

	protected void saveGXXMXX(String bdcdyh, String zt, String bljd,
			String qlsdfs, String casenum, String sqr,databaseType dbtype) {
		String xmmc = xmxx.getXMMC();
//		gxxmbh = UUID.randomUUID().toString().replace("-","");
		String prjId = xmxx.getPROJECT_ID();
		djdl=xmxx.getDJLX();
		if(djdl==null||djdl.equals("null")){
			djdl="";
		}
		String qllx = xmxx.getQLLX();
		String slry = xmxx.getSLRY();
		Date slsj = xmxx.getSLSJ();
		String bsmString= UUID.randomUUID().toString().replace("-","");
		String[] vls = new String[12];
		vls[0] = gxxmbh;
		vls[1] = xmmc;
		vls[2] = djdl;
		vls[3] = qllx;
		vls[4] = bdcdyh;
		vls[5] = slry;
		vls[6] = "to_date('"
				+ (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(slsj)
				+ "','yyyy-MM-dd HH24:mi:ss')";
		vls[7] = zt;
		vls[8] = bljd;
		vls[9] = qlsdfs;
		//如果casenum为空就给赋gxxmbh
		String ajh=casenum;
		if(casenum==null||casenum.equals("null")){
			ajh="";//gxxmbh
		}
		vls[10] = ajh;
		vls[11] = sqr;
		Date dt = new Date();
		String nowString = "to_date('"
				+ (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(dt)
				+ "','yyyy-MM-dd HH24:mi:ss')";
		String sql = "insert into "+dbtype+".GXJHXM (GXXMBH,PROJECT_ID,XMMC,DJDL,DJXL,QLLX,BDCDYH,SLRY,SLSJ,ZT,BLJD,QLSDFS,CASENUM,SQR,TSSJ,BSM,XZQDM,XZQMC) values ('"
				+ vls[0]
				+ "','"
				+ prjId
				+ "','"
				+ vls[1]
				+ "','"
				+ vls[2]
				+ "','"
				+ djxl
				+ "','"
				+ vls[3]
				+ "','"
				+ vls[4]
				+ "','"
				+ vls[5]
				+ "',"
				+ vls[6]
				+ ",'"
				+ vls[7]
				+ "','"
				+ vls[8]
				+ "','"
				+ vls[9]
				+ "','"
				+ vls[10]
				+ "','"
				+ vls[11]
				+ "',"
		+ nowString +",'"+bsmString+ "','"+getXzqdm()+"','"+getXzqmc()+"')";
		try {
			if(dbtype==databaseType.GXTDDJK){
				//土地登记库
				JH_DBHelper.excuteUpdate(tddjConnection, sql);
				//logger.info("插入共享项目数据条数：" + i);
			}
			else if(dbtype==databaseType.GXLDDJK){
				
				}
		} catch (SQLException e) {
			logger.error("出错：" + sql);
		}

	}

	/*
	 * 推送权利，各业务不一样，各自重载去吧
	 */
	protected void saveQL(String qltableName, Boolean bBGQ,com.supermap.wisdombusiness.framework.dao.impl.CommonDaoDJ CommonDaoDJ) {
		String qszt="0";//0临时，1现势，2历史
		if(ql_gz.getBDCQZH()!=null&&!ql_gz.getBDCQZH().equals("")){
			qszt="1";
		}
		if(qltableName.equals("CFDJ")){
			if(sfdb.equals("1")){
				qszt="1";
			}
		}
		String tablename = "GXDJK." + qltableName;
		if(qltableName.equals("FDCQ")){
			tablename=Fdcq_dj.class.getName();
		}else if(qltableName.equals("DYAQ")){
			tablename=Dyaq_dj.class.getName();
		}else if(qltableName.equals("YGDJ")){
			tablename=Ygdj_dj.class.getName();
		}else if(qltableName.equals("CFDJ")){
			tablename=Cfdj_dj.class.getName();
		}else if(qltableName.equals("XZDJ")){
			tablename=Xzdj_dj.class.getName();
		}else if(qltableName.equals("YYDJ")){
			tablename=Yydj_dj.class.getName();
		}else if(qltableName.equals("FDCQZXDJ")){
			tablename=Fdcqzxdj_dj.class.getName();
		}else{}
		if (bBGQ) {
			tablename = "GXDJK.BGQ" + qltableName;
			if(qltableName.equals("FDCQ")){
				tablename=Bgqfdcq_dj.class.getName();
			}else if(qltableName.equals("DYAQ")){
				tablename=Bgqdyaq_dj.class.getName();
			}else if(qltableName.equals("YGDJ")){
				tablename=Bgqygdj_dj.class.getName();
			}else if(qltableName.equals("CFDJ")){
				tablename=Bgqcfdj_dj.class.getName();
			}else if(qltableName.equals("XZDJ")){
				tablename=Bgqxzdj_dj.class.getName();
			}else if(qltableName.equals("YYDJ")){
				tablename=Bgqyydj_dj.class.getName();
			}else{}
			if(ql_gz.getBDCQZH()!=null&&!ql_gz.getBDCQZH().equals("")){
				qszt="2";
			}
		}
		if(tablename.equals("GXDJK.BGQFDCQZXDJ")){
			return;
		}
		String[] fieldsFromQL = null, fieldsFromFSQL = null;
		if (qltableName.equalsIgnoreCase("DYAQ")) {
			fieldsFromQL = new String[] { "BDCDYH", "YWH", "DJLX", "DJYY","ZWLXQSSJ", "ZWLXJSSJ", "BDCDJZMH", "QXDM", "DJJG","DBR", "DJSJ", "FJ", "QLID", "LYQLID", "ZSBH", "BZ" };
			fieldsFromFSQL = new String[] { "DYBDCLX", "DYR", "DYFS","ZJJZWZL", "ZJJZWDYFW", "BDBZZQSE", "ZGZQQDSS", "ZGZQSE","ZXDYYWH", "ZXDYYY", "DYMJ", "ZXSJ","ZXDBR" };
		} else if (qltableName.equalsIgnoreCase("CFDJ")) {
			fieldsFromQL = new String[] { "BDCDYH", "YWH", "QXDM", "DJJG","DBR", "QLQSSJ", "QLJSSJ", "DJSJ", "FJ", "QLID", "LYQLID","ZSBH", "BZ" };
			fieldsFromFSQL = new String[] { "CFJG", "CFLX", "CFWJ", "CFWH","CFFW" ,"JFJG", "JFWJ", "JFWH","JFDJSJ","JFDBR","JFYWH"};
		} else if (qltableName.equalsIgnoreCase("FDCQ")) {
			fieldsFromQL = new String[] { "BDCDYH", "YWH", "QLLX", "DJLX","DJYY", "QXDM", "DJJG", "DBR", "DJSJ", "FJ", "QLID","QLQSSJ", "QLJSSJ", "ZSBH", "BDCQZH", "BZ", "CZFS"  };
			fieldsFromFSQL = new String[] { "FDZL", "FTTDMJ", "FDCJYJG","GHYT", "FWXZ", "FWJG", "SZC", "ZCS", "JZMJ", "ZYJZMJ","FTJZMJ" };
		} else if (qltableName.equalsIgnoreCase("FDCQZXDJ")) {
			ql_gz.setBDCQZH(ql_ls_bgq.getBDCQZH());
			fsql_gz.setZXSJ(fsql_ls_bgq.getZXSJ());
			fieldsFromQL = new String[] { "BDCDYH", "YWH", "BDCQZH", "QXDM","DJJG", "DBR", "DJSJ", "FJ", "QLID", "LYQLID", "ZSBH", "BZ" };
			fieldsFromFSQL = new String[] { "ZXSJ","ZXDBR","ZXDYYY" ,"ZXYWH"};
		} else if (qltableName.equalsIgnoreCase("XZDJ")) {
			fieldsFromQL = new String[] { "BDCDYH", "YWH", "QXDM", "DJJG","DBR", "DJSJ", "FJ", "QLID", "LYQLID", "ZSBH", "BZ" };
			fieldsFromFSQL = new String[] { "CFJG", "CFWJ", "CFWH", "CFQSSJ","CFJSSJ", "CFFW", "JFYWH", "JFJG", "JFWJ", "JFWH" };
		} else if (qltableName.equalsIgnoreCase("YGDJ")) {
			fieldsFromQL = new String[] { "BDCDYH", "YWH", "DJLX", "DJYY","QXDM", "DJJG", "DBR", "DJSJ", "FJ", "QLID", "ZSBH", "BZ","BDCDJZMH" };
			fieldsFromFSQL = new String[] { "GHYT", "FWXZ", "FWJG", "SZC","ZCS", "JZMJ", "QDJG", "YGDJZL","YWR","YWRZJZL","YWRZJH","ZXDBR","ZXSJ" };
		} else if (qltableName.equalsIgnoreCase("YYDJ")) {
			fieldsFromQL = new String[] { "BDCDYH", "YWH", "QXDM", "DJJG","DBR", "DJSJ", "FJ", "QLID", "ZSBH", "BZ" ,"BDCDJZMH"};
			fieldsFromFSQL = new String[] { "YYSX","ZXYYYWH", "ZXYYYY", "ZXYYDBR","ZXYYDJSJ" };
		}
		else if (qltableName.equalsIgnoreCase("JSYDSYQ")) {
			fieldsFromQL = new String[] { "BDCDYH", "YWH", "QLLX", "DJLX","DJYY", "QXDM", "DJJG", "DBR", "DJSJ", "FJ", "QLID","QLQSSJ", "QLJSSJ", "ZSBH", "BDCQZH", "BZ" };
			fieldsFromFSQL = new String[] {  "SYQMJ", "QDJG"};
		}

		String bsm=UUID.randomUUID().toString().replace("-","");// Common.CreatUUID();
		Date dt = new Date();
		String nowString = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(dt);
		String[] otherfields = { "GXXMBH", "BDCDYID" ,"BSM","QSZT","TSSJ"};
		String[] othervalues = { getGxxmbh(), bdcdyid,bsm,qszt,nowString };

		String sql = "";
		if (!bBGQ) {
//			sql = Tools.createInsertSQL(tablename, fieldsFromQL, ql_gz,
//					fieldsFromFSQL, fsql_gz, otherfields, othervalues);
			//一个项目推送要一个事务提交，在工具类里增加通用方法直接返回待保存对象 2017年1月18日 17:17:39 卜晓波
			try {
				Object ql=Tools.createPushObj(tablename, fieldsFromQL, ql_gz,fieldsFromFSQL, fsql_gz, otherfields, othervalues);
				CommonDaoDJ.save(ql);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			if(fsql_ls_bgq!=null && ql_ls_bgq!=null){
//				sql = Tools.createInsertSQL(tablename, fieldsFromQL, ql_ls_bgq,
//						fieldsFromFSQL, fsql_ls_bgq, otherfields, othervalues);
				Object ql=Tools.createPushObj(tablename, fieldsFromQL, ql_ls_bgq,fieldsFromFSQL, fsql_ls_bgq, otherfields, othervalues);
				CommonDaoDJ.save(ql);
			}
		}
	}

	/*
	 * 推送土地业务权利
	 */
	protected void saveQL(String qltableName, Boolean bBGQ,databaseType dbtype) {
		String tablename = dbtype+"." + qltableName;
		String qszt="0";//0临时，1现势，2历史
		if(ql_gz.getBDCQZH()!=null&&!ql_gz.getBDCQZH().equals("")){
			qszt="1";
		}
		if(qltableName.equals("CFDJ")){
			if(sfdb.equals("1")){
				qszt="1";
			}
		}
		if (bBGQ) {
			tablename = dbtype+".BGQ" + qltableName;
			if(ql_gz.getBDCQZH()!=null&&!ql_gz.getBDCQZH().equals("")){
				qszt="2";
			}
		}

		String[] fieldsFromQL = null, fieldsFromFSQL = null;
		if (qltableName.equalsIgnoreCase("DYAQ")) {
			fieldsFromQL = new String[] { "BDCDYH", "YWH", "DJLX", "DJYY",
					"ZWLXQSSJ", "ZWLXJSSJ", "BDCDJZMH", "QXDM", "DJJG",
					"DBR", "DJSJ", "FJ", "QLID", "LYQLID", "ZSBH", "BZ" };
			fieldsFromFSQL = new String[] { "DYBDCLX", "DYR", "DYFS",
					"ZJJZWZL", "ZJJZWDYFW", "BDBZZQSE", "ZGZQQDSS", "ZGZQSE",
					"ZXDYYWH", "ZXDYYY", "DYMJ", "ZXSJ" };
		} else if (qltableName.equalsIgnoreCase("CFDJ")) {
			fieldsFromQL = new String[] { "BDCDYH", "YWH", "QXDM", "DJJG",
					"DBR", "QLQSSJ", "QLJSSJ", "DJSJ", "FJ", "QLID", "LYQLID",
					"ZSBH", "BZ" };
			fieldsFromFSQL = new String[] { "CFJG", "CFLX", "CFWJ", "CFWH",
					"CFFW" ,"JFJG", "JFWJ", "JFWH","JFDJSJ","JFDBR","JFYWH" };
		} else if (qltableName.equalsIgnoreCase("FDCQ")) {
			fieldsFromQL = new String[] { "BDCDYH", "YWH", "QLLX", "DJLX",
					"DJYY", "QXDM", "DJJG", "DBR", "DJSJ", "FJ", "QLID",
					"QLQSSJ", "QLJSSJ", "ZSBH", "BDCQZH", "BZ" , "CZFS" };
			fieldsFromFSQL = new String[] { "FDZL", "FTTDMJ", "FDCJYJG",
					"GHYT", "FWXZ", "FWJG", "SZC", "ZCS", "JZMJ", "ZYJZMJ",
					"FTJZMJ" };
		} else if (qltableName.equalsIgnoreCase("FDCQZXDJ")) {
			ql_gz.setBDCQZH(ql_ls_bgq.getBDCQZH());
			fsql_gz.setZXSJ(fsql_ls_bgq.getZXSJ());
			fieldsFromQL = new String[] { "BDCDYH", "YWH", "BDCQZH", "QXDM",
					"DJJG", "DBR", "DJSJ", "FJ", "QLID", "LYQLID", "ZSBH", "BZ" };
			fieldsFromFSQL = new String[] { "ZXSJ","ZXDBR","ZXDYYY" ,"ZXYWH"};
		} else if (qltableName.equalsIgnoreCase("XZDJ")) {
			fieldsFromQL = new String[] { "BDCDYH", "YWH", "QXDM", "DJJG",
					"DBR", "DJSJ", "FJ", "QLID", "LYQLID", "ZSBH", "BZ" };
			fieldsFromFSQL = new String[] { "CFJG", "CFWJ", "CFWH", "CFQSSJ",
					"CFJSSJ", "CFFW", "JFYWH", "JFJG", "JFWJ", "JFWH" };
		} else if (qltableName.equalsIgnoreCase("YGDJ")) {
			fieldsFromQL = new String[] { "BDCDYH", "YWH", "DJLX", "DJYY",
					"QXDM", "DJJG", "DBR", "DJSJ", "FJ", "QLID", "ZSBH", "BZ","BDCDJZMH" };
			fieldsFromFSQL = new String[] { "GHYT", "FWXZ", "FWJG", "SZC",
					"ZCS", "JZMJ", "QDJG", "YGDJZL","YWR","YWRZJZL","YWRZJH" };
		} else if (qltableName.equalsIgnoreCase("YYDJ")) {
			fieldsFromQL = new String[] { "BDCDYH", "YWH", "QXDM", "DJJG",
					"DBR", "DJSJ", "FJ", "QLID", "ZSBH", "BZ" ,"BDCDJZMH"};
			fieldsFromFSQL = new String[] { "YYSX","ZXYYYWH", "ZXYYYY", "ZXYYDBR",
					"ZXYYDJSJ" };
		}
		else if (qltableName.equalsIgnoreCase("JSYDSYQ")) {
			fieldsFromQL = new String[] { "BDCDYH", "YWH", "QLLX", "DJLX",
					"DJYY", "QXDM", "DJJG", "DBR", "DJSJ", "FJ", "QLID",
					"QLQSSJ", "QLJSSJ", "ZSBH", "BDCQZH", "BZ" };
			fieldsFromFSQL = new String[] {  "SYQMJ", "QDJG"};
		}

		String bsm= UUID.randomUUID().toString().replace("-","");
		String[] otherfields = { "GXXMBH", "BDCDYID" ,"BSM","QSZT"};
		String[] othervalues = { getGxxmbh(), bdcdyid,bsm,qszt };

		String sql = "";
		if (!bBGQ) {
			sql = Tools.createInsertSQL(tablename, fieldsFromQL, ql_gz,
					fieldsFromFSQL, fsql_gz, otherfields, othervalues);
		} else {
			sql = Tools.createInsertSQL(tablename, fieldsFromQL, ql_ls_bgq,
					fieldsFromFSQL, fsql_ls_bgq, otherfields, othervalues);
		}
		try {
			 if (dbtype==databaseType.GXDJK){
				 //房产交易数据
				JH_DBHelper.excuteUpdate(jyConnection, sql);
				}
			 else if(dbtype==databaseType.GXTDDJK){
				 //土地交易数据
			  JH_DBHelper.excuteUpdate(tddjConnection, sql);
			}
			
		} catch (SQLException e) {
			logger.error("出错：" + e.getMessage());
			logger.error("出错：" + sql);
		}
	}


	/**
	 * 保存权利人信息
	 * @param eNCRYPTION 
	 */	 
	 protected void saveQLR(com.supermap.wisdombusiness.framework.dao.impl.CommonDao baseCommonDao,com.supermap.wisdombusiness.framework.dao.impl.CommonDaoDJ CommonDaoDJ, String eNCRYPTION) {
		String tablename = Qlr_dj.class.getName();
		String[] fieldsFromQLR = { "QLRID", "BDCDYH", "SXH", "QLRMC", "BDCQZH",
				"ISCZR", "ZJZL", "ZJH", "FZJG", "SSHY", "GJ", "HJSZSS", "XB",
				"DH", "QLID", "BZ", "GYFS", "QLBL", "QLRLX", "DZYJ", "GZDW",
				"YB", "DZ" };
		String[] fieldsFromQLRD = { "QLRID", "BDCDYH", "SXH", "QLRMC", "BDCQZH",
				"ISCZR", "ZJZL", "ZJH", "FZJG", "SSHY", "GJ", "HJSZSS", "XB",
				"DH", "QLID", "BZ", "GYFS", "QLBL", "QLRLX", "DZYJ", "GZDW",
				"YB", "DZ","DLRXM","DLRLXDH","DLRZJHM","DLRZJLX","FDDBR","FDDBRDH","FDDBRZJHM","FDDBRZJLX"};
		String[] otherfields = { "GXXMBH", "QZYSXLH","BSM","TSSJ" };
		String xzqdm = ConfigHelper.getNameByValue("XZQHDM");
//		String sql = "";
		//是否翻译常量
		String translateString=ConfigHelper.getNameByValue("translateconst");
		for (BDCS_QLR_GZ qlr_gz : qlr_gzList) {
			// 证件号中带小写字母转换成大写的，目前处理身份证的
						if (!StringHelper.isEmpty(qlr_gz)) {
							if (!StringHelper.isEmpty(qlr_gz.getZJZL())) {
								if ("1".equals(qlr_gz.getZJZL())) {
									if (!StringHelper.isEmpty(qlr_gz.getZJH())) {
										qlr_gz.setZJH(qlr_gz.getZJH().toUpperCase());
									}
								}
							}
						}
			//加密，内部会判断是否需要
			qlr_gz.setQLRMC(encodeText(qlr_gz.getQLRMC(),eNCRYPTION));
			qlr_gz.setZJH(encodeText(qlr_gz.getZJH(),eNCRYPTION));
			String bsm= UUID.randomUUID().toString().replace("-","");
			List<BDCS_ZS_GZ> zs_GZ = baseCommonDao.getDataList(BDCS_ZS_GZ.class, " bdcqzh='" + qlr_gz.getBDCQZH() + "'");
			Date dt = new Date();
			String nowString = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(dt);
			if (zs_GZ != null && zs_GZ.size() > 0) {
				String[] othervalues = { getGxxmbh(), zs_GZ.get(0).getZSBH(),bsm,nowString};
//				sql = Tools.createInsertSQL(tablename, fieldsFromQLR, qlr_gz,
//						otherfields, othervalues);
				//一个项目推送要一个事务提交，在工具类里增加通用方法直接返回待保存对象 2017年1月18日 17:17:39 卜晓波
				try {
                   if(xzqdm.equals("140200")){
                	    Qlr_dj qlr=Tools.createPushObj(tablename, fieldsFromQLRD, qlr_gz,otherfields, othervalues);
	   					if(translateString!=null &&translateString.equals("1")){
	   				    	translateQLR(baseCommonDao, qlr);
	   					}
	   					CommonDaoDJ.save(qlr);
//	   					CommonDaoDJ.flush();
                   }else {
                	   	Qlr_dj qlr=Tools.createPushObj(tablename, fieldsFromQLR, qlr_gz,otherfields, othervalues);
	   					if(translateString!=null &&translateString.equals("1")){
	   				    	translateQLR(baseCommonDao, qlr);
	   					}
	   					CommonDaoDJ.save(qlr);
//	   					CommonDaoDJ.flush();
				  }	
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				String[] othervalues = { getGxxmbh(), "" ,bsm,nowString};
//				sql = Tools.createInsertSQL(tablename, fieldsFromQLR, qlr_gz,otherfields, othervalues);
				//一个项目推送要一个事务提交，在工具类里增加通用方法直接返回待保存对象 2017年1月18日 17:17:39 卜晓波
				try {
					if(xzqdm.equals("140200")){
						Qlr_dj qlr=Tools.createPushObj(tablename, fieldsFromQLRD, qlr_gz,otherfields, othervalues);
						if(translateString!=null &&translateString.equals("1")){
					    	translateQLR(baseCommonDao, qlr);
						}
						CommonDaoDJ.save(qlr);
//						CommonDaoDJ.flush();
					}else {
						Qlr_dj qlr=Tools.createPushObj(tablename, fieldsFromQLR, qlr_gz,otherfields, othervalues);
						if(translateString!=null &&translateString.equals("1")){
					    	translateQLR(baseCommonDao, qlr);
						}
						CommonDaoDJ.save(qlr);
//						CommonDaoDJ.flush();
					}
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	/** 
	* @author  buxiaobo
	* @date 创建时间：2017年6月20日 上午11:16:31 
	* @version 1.0 
	* @parameter  
	* @since  
	* @return  
	*/
	private void saveYWR(com.supermap.wisdombusiness.framework.dao.impl.CommonDao baseCommonDao,com.supermap.wisdombusiness.framework.dao.impl.CommonDaoDJ CommonDaoDJ) {
		String tablename = Bgqqlr_dj.class.getName();
		String[] fieldsFromQLR = { "QLRID", "BDCDYH", "SXH", "QLRMC", "BDCQZH",
				"ISCZR", "ZJZL", "ZJH", "FZJG", "SSHY", "GJ", "HJSZSS", "XB",
				"DH", "QLID", "BZ", "GYFS", "QLBL", "QLRLX", "DZYJ", "GZDW",
				"YB", "DZ" };
		String[] otherfields = { "GXXMBH", "QZYSXLH","BSM","TSSJ"};
		String[] fieldsFromQLRD = {"QLRID", "BDCDYH", "SXH", "QLRMC", "BDCQZH","ISCZR", "ZJZL", "ZJH", "FZJG", "SSHY", "GJ", "HJSZSS", "XB",
				"DH", "QLID", "BZ", "GYFS", "QLBL", "QLRLX", "DZYJ", "GZDW",
				"YB", "DZ","DLRXM","DLRLXDH","DLRZJHM","DLRZJLX","FDDBR","FDDBRDH","FDDBRZJHM","FDDBRZJLX"};
		String xzqdm = ConfigHelper.getNameByValue("XZQHDM");
//		String sql = "";
		//是否翻译常量
		String translateString=ConfigHelper.getNameByValue("translateconst");
		for (BDCS_QLR_LS qlr_ls : qlr_lsList_bgq1) {
			//加密，内部会判断是否需要
			qlr_ls.setQLRMC(encodeText(qlr_ls.getQLRMC(),"0"));
			qlr_ls.setZJH(encodeText(qlr_ls.getZJH(),"0"));
			String bsmString= UUID.randomUUID().toString().replace("-","");
			Date dt = new Date();
			String nowString = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(dt);
			String[] othervalues = { getGxxmbh(), "",bsmString,nowString};
			//一个项目推送要一个事务提交，在工具类里增加通用方法直接返回待保存对象 2017年1月18日 17:17:39 卜晓波
			try {
				if(xzqdm.equals("140200")){
					Bgqqlr_dj qlr=Tools.createPushObj(tablename, fieldsFromQLRD, qlr_ls,otherfields, othervalues);
					if(translateString!=null &&translateString.equals("1")){
				    	translateBGQQLR(baseCommonDao, qlr);
					}
					CommonDaoDJ.save(qlr);
//					CommonDaoDJ.flush();
				}else {
					Bgqqlr_dj qlr=Tools.createPushObj(tablename, fieldsFromQLR, qlr_ls,otherfields, othervalues);
					if(translateString!=null &&translateString.equals("1")){
				    	translateBGQQLR(baseCommonDao, qlr);
					}
					CommonDaoDJ.save(qlr);
//					CommonDaoDJ.flush();
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
		}
	}
	/**
	 * 土地推送*保存权利人信息
	 * 此处是土地推送保存权利人方法，暂时就不改成统一事务提交了
	 * @throws Exception 
	 */
	protected void saveQLR(
			com.supermap.wisdombusiness.framework.dao.impl.CommonDao baseCommonDao,databaseType dbtype) throws Exception {
		String tablename = dbtype+"." + "QLR";
		String[] fieldsFromQLR = { "QLRID", "BDCDYH", "SXH", "QLRMC", "BDCQZH",
				"ISCZR", "ZJZL", "ZJH", "FZJG", "SSHY", "GJ", "HJSZSS", "XB",
				"DH", "QLID", "BZ", "GYFS", "QLBL", "QLRLX", "DZYJ", "GZDW",
				"YB", "DZ" };
		String[] otherfields = { "GXXMBH", "QZYSXLH","BSM" };
		String sql = "";
		for (BDCS_QLR_GZ qlr_gz : qlr_gzList) {
			if (!StringHelper.isEmpty(qlr_gz)) {
				if (!StringHelper.isEmpty(qlr_gz.getZJZL())) {
					if ("1".equals(qlr_gz.getZJZL())) {
						if (!StringHelper.isEmpty(qlr_gz.getZJH())) {
							qlr_gz.setZJH(qlr_gz.getZJH().toUpperCase());
						}
					}
				}
			}
			//加密，内部会判断是否需要
			qlr_gz.setQLRMC(encodeText(qlr_gz.getQLRMC(),"0"));
			qlr_gz.setZJH(encodeText(qlr_gz.getZJH(),"0"));
			String bsm= UUID.randomUUID().toString().replace("-","");
			List<BDCS_ZS_GZ> zs_GZ = baseCommonDao.getDataList(
					BDCS_ZS_GZ.class, " bdcqzh='" + qlr_gz.getBDCQZH() + "'");
			if (zs_GZ != null && zs_GZ.size() > 0) {
				String[] othervalues = { getGxxmbh(), zs_GZ.get(0).getZSBH(),bsm };
				sql = Tools.createInsertSQL(tablename, fieldsFromQLR, qlr_gz,
						otherfields, othervalues);
			} else {
				String[] othervalues = { getGxxmbh(), "" ,bsm};
				sql = Tools.createInsertSQL(tablename, fieldsFromQLR, qlr_gz,otherfields, othervalues);
			}
			try {
				if(dbtype==databaseType.GXTDDJK){
				JH_DBHelper.excuteUpdate(tddjConnection, sql);
				}

			} catch (SQLException e) {
				logger.error("出错：" + e.getMessage());
				logger.error("出错：" + sql);
			}
		}
	}


	/**
	 * 保存变更前权利人信息
	 */
	protected void saveQLR_BGQ(
			com.supermap.wisdombusiness.framework.dao.impl.CommonDao baseCommonDao,com.supermap.wisdombusiness.framework.dao.impl.CommonDaoDJ CommonDaoDJ) {
		String tablename = Bgqqlr_dj.class.getName();
		String[] fieldsFromQLR = { "QLRID", "BDCDYH", "SXH", "QLRMC", "BDCQZH",
				"ISCZR", "ZJZL", "ZJH", "FZJG", "SSHY", "GJ", "HJSZSS", "XB",
				"DH", "QLID", "BZ", "GYFS", "QLBL", "QLRLX", "DZYJ", "GZDW",
				"YB", "DZ" };
		String[] otherfields = { "GXXMBH", "QZYSXLH","BSM","TSSJ"};
		String[] fieldsFromQLRD = { "QLRID", "BDCDYH", "SXH", "QLRMC", "BDCQZH","ISCZR", "ZJZL", "ZJH", "FZJG", 
				"SSHY", "GJ", "HJSZSS", "XB","DH", "QLID", "BZ", "GYFS", "QLBL", "QLRLX", "DZYJ", "GZDW",
				"YB", "DZ","DLRXM","DLRLXDH","DLRZJHM","DLRZJLX","FDDBR","FDDBRDH","FDDBRZJHM","FDDBRZJLX"};
		String xzqdm = ConfigHelper.getNameByValue("XZQHDM");
//		String sql = "";
		//是否翻译常量
		String translateString=ConfigHelper.getNameByValue("translateconst");
		for (BDCS_QLR_LS qlr_ls : qlr_lsList_bgq) {
			if (!StringHelper.isEmpty(qlr_ls)) {
				if (!StringHelper.isEmpty(qlr_ls.getZJZL())) {
					if ("1".equals(qlr_ls.getZJZL())) {
						if (!StringHelper.isEmpty(qlr_ls.getZJH())) {
							qlr_ls.setZJH(qlr_ls.getZJH().toUpperCase());
						}
					}
				}
			}
			//加密，内部会判断是否需要
			qlr_ls.setQLRMC(encodeText(qlr_ls.getQLRMC(),"0"));
			qlr_ls.setZJH(encodeText(qlr_ls.getZJH(),"0"));
			String bsmString= UUID.randomUUID().toString().replace("-","");
			List<BDCS_ZS_LS> zs_LS = baseCommonDao.getDataList(
					BDCS_ZS_LS.class, " bdcqzh='" + qlr_ls.getBDCQZH() + "'");
			Date dt = new Date();
			String nowString = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(dt);
			if (zs_LS != null && zs_LS.size() > 0) {
				String[] othervalues = { getGxxmbh(), zs_LS.get(0).getZSBH(),bsmString,nowString};
//				sql = Tools.createInsertSQL(tablename, fieldsFromQLR, qlr_ls,
//						otherfields, othervalues);
				//一个项目推送要一个事务提交，在工具类里增加通用方法直接返回待保存对象 2017年1月18日 17:17:39 卜晓波
				try {
					if(xzqdm.equals("140200")){
						Bgqqlr_dj qlr=Tools.createPushObj(tablename, fieldsFromQLRD, qlr_ls,otherfields, othervalues);
						if(translateString!=null &&translateString.equals("1")){
					    	translateBGQQLR(baseCommonDao, qlr);
						}
						CommonDaoDJ.save(qlr);
//						CommonDaoDJ.flush();
					}else {
						Bgqqlr_dj qlr=Tools.createPushObj(tablename, fieldsFromQLR, qlr_ls,otherfields, othervalues);
						if(translateString!=null &&translateString.equals("1")){
					    	translateBGQQLR(baseCommonDao, qlr);
						}
						CommonDaoDJ.save(qlr);
//						CommonDaoDJ.flush();
					}
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				String[] othervalues = { getGxxmbh(), "",bsmString,nowString};
//				sql = Tools.createInsertSQL(tablename, fieldsFromQLR, qlr_ls,
//						otherfields, othervalues);
				//一个项目推送要一个事务提交，在工具类里增加通用方法直接返回待保存对象 2017年1月18日 17:17:39 卜晓波
				try {
					if(xzqdm.equals("140200")){
						Bgqqlr_dj qlr=Tools.createPushObj(tablename, fieldsFromQLRD, qlr_ls,otherfields, othervalues);
						if(translateString!=null &&translateString.equals("1")){
					    	translateBGQQLR(baseCommonDao, qlr);
						}
						CommonDaoDJ.save(qlr);
//						CommonDaoDJ.flush();
					}else {
						Bgqqlr_dj qlr=Tools.createPushObj(tablename, fieldsFromQLR, qlr_ls,otherfields, othervalues);
						if(translateString!=null &&translateString.equals("1")){
					    	translateBGQQLR(baseCommonDao, qlr);
						}
						CommonDaoDJ.save(qlr);
//						CommonDaoDJ.flush();
					}
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	/**
	 * 土地推送*保存变更前权利人信息
	 */
	protected void saveQLR_BGQ(
			com.supermap.wisdombusiness.framework.dao.impl.CommonDao baseCommonDao,databaseType dbtype) {
		String tablename = dbtype+"." + "BGQQLR";
		String[] fieldsFromQLR = { "QLRID", "BDCDYH", "SXH", "QLRMC", "BDCQZH",
				"ISCZR", "ZJZL", "ZJH", "FZJG", "SSHY", "GJ", "HJSZSS", "XB",
				"DH", "QLID", "BZ", "GYFS", "QLBL", "QLRLX", "DZYJ", "GZDW",
				"YB", "DZ" };
		String[] otherfields = { "GXXMBH", "QZYSXLH","BSM" };
		String sql = "";
		for (BDCS_QLR_LS qlr_ls : qlr_lsList_bgq) {
			if (!StringHelper.isEmpty(qlr_ls)) {
				if (!StringHelper.isEmpty(qlr_ls.getZJZL())) {
					if ("1".equals(qlr_ls.getZJZL())) {
						if (!StringHelper.isEmpty(qlr_ls.getZJH())) {
							qlr_ls.setZJH(qlr_ls.getZJH().toUpperCase());
						}
					}
				}
			}
			//加密，内部会判断是否需要
			qlr_ls.setQLRMC(encodeText(qlr_ls.getQLRMC(),"0"));
			qlr_ls.setZJH(encodeText(qlr_ls.getZJH(),"0"));
			String bsmString= UUID.randomUUID().toString().replace("-","");
			List<BDCS_ZS_LS> zs_LS = baseCommonDao.getDataList(
					BDCS_ZS_LS.class, " bdcqzh='" + qlr_ls.getBDCQZH() + "'");
			if (zs_LS != null && zs_LS.size() > 0) {
				String[] othervalues = { getGxxmbh(), zs_LS.get(0).getZSBH(),bsmString };
				sql = Tools.createInsertSQL(tablename, fieldsFromQLR, qlr_ls,
						otherfields, othervalues);
			} else {
				String[] othervalues = { getGxxmbh(), "",bsmString };
				sql = Tools.createInsertSQL(tablename, fieldsFromQLR, qlr_ls,
						otherfields, othervalues);
			}
			try {
				if(dbtype==databaseType.GXTDDJK){
					JH_DBHelper.excuteUpdate(tddjConnection, sql);
					}
			} catch (SQLException e) {
				allSuccess=false;
				logger.error("出错：" + e.getMessage());
				logger.error("出错：" + sql);
			}
		}
	}
	/**
	 * 保存户信息
	 */
	protected void saveH(com.supermap.wisdombusiness.framework.dao.impl.CommonDaoDJ CommonDaoDJ) {
		String tablename = H_dj.class.getName();
		String[] fieldsFromQLR = { "BDCDYH", "ZRZH", "LJZH", "CH", "ZL",
				"MJDW", "SJCS", "HH", "SHBW", "HX", "HXJG", "FWYT1", "FWYT2",
				"FWYT3", "YCJZMJ", "YCTNJZMJ", "YCFTJZMJ", "YCDXBFJZMJ",
				"YCQTJZMJ", "YCFTXS", "SCJZMJ", "SCTNJZMJ", "SCFTJZMJ",
				"SCDXBFJZMJ", "SCQTJZMJ", "SCFTXS", "GYTDMJ", "FTTDMJ",
				"DYTDMJ", "FWLX", "FWXZ", "FCFHT", "ZT", "BDCDYID", "CID",
				"LJZID", "ZRZBDCDYID", "RELATIONID", "ZCS", "JGSJ", "DYH",
				"FH", "FWJG", "GHYT", "FWBM", "SZC","FWCB" ,"QXDM","QXMC","QSC","ZZC"};//
		String bsm= UUID.randomUUID().toString().replace("-","");
		Date dt = new Date();
		String nowString = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(dt);
		String[] otherfields = { "GXXMBH", "FWZT","BSM" ,"TDXZ","TDYT","TDMJ","TSSJ"};// 房屋状态，1期房，2现房
		String[] othervalues = { getGxxmbh(), bdcdyly ,bsm,getTdxz(),getTdyt(),getTdmj(),nowString};
		String sql = "";
		//是否翻译常量
		String translateString=ConfigHelper.getNameByValue("translateconst");
		
		if (bdcdyly.equals(BDCDYLX.YCH.Value)) {
			if (h_XZY != null) {
				//一个项目推送要一个事务提交，在工具类里增加通用方法直接返回待保存对象 2017年1月18日 17:17:39 卜晓波
				try {
					H_dj h=Tools.createPushObj(tablename, fieldsFromQLR, h_XZY,otherfields, othervalues);
					String XZQHDM = ConfigHelper.getNameByValue("XZQHDM");
					if(XZQHDM.equals("220221")){
						System.out.println(h.getScjzmj());
					     System.out.println(h.getYcjzmj());
						int x = h.getScjzmj().compareTo(BigDecimal.ZERO);
					//	if(r==0) //等于if(r==1) //大于if(r==-1) //小于
						System.out.println(x);
						if(x==0){
								System.out.println("实测建筑面积为0");
								h.setScjzmj(h.getYcjzmj());
							}
					}
					if(translateString!=null &&translateString.equals("1")){
						translateH(commonDao, h);
					}
					CommonDaoDJ.save(h);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				logger.warn("预测户对象为空！共享项目号：" + othervalues[0]);
			}
		} else if (bdcdyly.equals(BDCDYLX.H.Value)) {
			if (djdyly.equals(DJDYLY.GZ.Value)) {
				if (h_GZ != null) {
					//一个项目推送要一个事务提交，在工具类里增加通用方法直接返回待保存对象 2017年1月18日 17:17:39 卜晓波
					try {
						H_dj h=Tools.createPushObj(tablename, fieldsFromQLR, h_GZ,otherfields, othervalues);
						String XZQHDM = ConfigHelper.getNameByValue("XZQHDM");
						if(XZQHDM.equals("220221")){
							System.out.println(h.getScjzmj());
						     System.out.println(h.getYcjzmj());
							int x = h.getScjzmj().compareTo(BigDecimal.ZERO);
						//	if(r==0) //等于if(r==1) //大于if(r==-1) //小于
							System.out.println(x);
							if(x==0){
									System.out.println("实测建筑面积为0");
									h.setScjzmj(h.getYcjzmj());
								}
						}
						if(translateString!=null &&translateString.equals("1")){
							translateH(commonDao, h);
						}
						CommonDaoDJ.save(h);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					logger.warn("工作户对象为空！共享项目号：" + othervalues[0]);
				}
			} else if (djdyly.equals(DJDYLY.XZ.Value)) {
				if (h_xz != null) {
					try {
						H_dj h=Tools.createPushObj(tablename, fieldsFromQLR, h_xz,otherfields, othervalues);
						String XZQHDM = ConfigHelper.getNameByValue("XZQHDM");
						if(XZQHDM.equals("220221")){
							System.out.println(h.getScjzmj());
						     System.out.println(h.getYcjzmj());
							int x = h.getScjzmj().compareTo(BigDecimal.ZERO);
						//	if(r==0) //等于if(r==1) //大于if(r==-1) //小于
							System.out.println(x);
							if(x==0){
									System.out.println("实测建筑面积为0");
									h.setScjzmj(h.getYcjzmj());
								}
						}
						if(translateString!=null &&translateString.equals("1")){
							translateH(commonDao, h);
						}
						CommonDaoDJ.save(h);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					logger.warn("工作户对象为空！共享项目号：" + othervalues[0]);
				}
			}else if (djdyly.equals(DJDYLY.LS.Value)) {
				//注销登记把户信息推过去
				if (h_xz != null) {
					try {
						H_dj h=Tools.createPushObj(tablename, fieldsFromQLR, h_xz,otherfields, othervalues);
						String XZQHDM = ConfigHelper.getNameByValue("XZQHDM");
						if(XZQHDM.equals("220221")){
							System.out.println(h.getScjzmj());
						     System.out.println(h.getYcjzmj());
							int x = h.getScjzmj().compareTo(BigDecimal.ZERO);
						//	if(r==0) //等于if(r==1) //大于if(r==-1) //小于
							System.out.println(x);
							if(x==0){
									System.out.println("实测建筑面积为0");
									h.setScjzmj(h.getYcjzmj());
								}
						}
						if(translateString!=null &&translateString.equals("1")){
							translateH(commonDao, h);
						}
						CommonDaoDJ.save(h);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					logger.warn("工作户对象为空！共享项目号：" + othervalues[0]);
				}
			}
		}

	}

	/**
	 * 保存变更前户信息
	 */
	protected void saveH_BGQ(com.supermap.wisdombusiness.framework.dao.impl.CommonDaoDJ CommonDaoDJ) {
		String tablename = Bgqh_dj.class.getName();
		String[] fieldsFromQLR = { "BDCDYH", "ZRZH", "LJZH", "CH", "ZL",
				"MJDW", "SJCS", "HH", "SHBW", "HX", "HXJG", "FWYT1", "FWYT2",
				"FWYT3", "YCJZMJ", "YCTNJZMJ", "YCFTJZMJ", "YCDXBFJZMJ",
				"YCQTJZMJ", "YCFTXS", "SCJZMJ", "SCTNJZMJ", "SCFTJZMJ",
				"SCDXBFJZMJ", "SCQTJZMJ", "SCFTXS", "GYTDMJ", "FTTDMJ",
				"DYTDMJ", "FWLX", "FWXZ", "FCFHT", "ZT", "BDCDYID", "CID",
				"LJZID", "ZRZBDCDYID", "RELATIONID", "ZCS", "JGSJ", "DYH",
				"FWJG", "FH", "GHYT", "FWBM", "SZC", "QSC", "ZZC"  };
		String bsm= UUID.randomUUID().toString().replace("-","");
		Date dt = new Date();
		String nowString = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(dt);
		String[] otherfields = { "GXXMBH", "FWZT","BSM","TSSJ"};// 房屋状态，1期房，2现房
		String[] othervalues = { getGxxmbh(), bdcdyly ,bsm,nowString};
		String sql = "";
		//是否翻译常量
		String translateString=ConfigHelper.getNameByValue("translateconst");
		if (bdcdyly.equals(BDCDYLX.YCH.Value)) {
			if (h_LSY != null) {
				//一个项目推送要一个事务提交，在工具类里增加通用方法直接返回待保存对象 2017年1月18日 17:17:39 卜晓波
				try {
					Bgqh_dj bgqh=Tools.createPushObj(tablename, fieldsFromQLR, h_LSY,otherfields, othervalues);
					if(translateString!=null &&translateString.equals("1")){
						translateBGQH(commonDao, bgqh);
					}
					CommonDaoDJ.save(bgqh);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				logger.warn("历史户对象为空！共享项目号：" + othervalues[0]);
			}
		} else if (bdcdyly.equals(BDCDYLX.H.Value)) {
			if (h_ls_bgq != null) {
				//一个项目推送要一个事务提交，在工具类里增加通用方法直接返回待保存对象 2017年1月18日 17:17:39 卜晓波
				try {
					Bgqh_dj bgqh=Tools.createPushObj(tablename, fieldsFromQLR, h_ls_bgq,otherfields, othervalues);
					if(translateString!=null &&translateString.equals("1")){
						translateBGQH(commonDao, bgqh);
					}
					CommonDaoDJ.save(bgqh);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				logger.warn("历史变更前户对象为空！共享项目号：" + othervalues[0]);
			}
		}

	}

	public void updateBLJD(String xmbh, String bljd) {
		String sql = "update GXDJK.GXJHXM set bljd='" + bljd+ "' where project_id='" + xmbh + "'";
		try {
			if (jyConnection == null || jyConnection.isClosed()) {
				jyConnection = JH_DBHelper.getConnect_gxdjk();
			}
			JH_DBHelper.excuteUpdate(jyConnection, sql);
		} catch (SQLException e) {
			logger.error("出错：" + e.getMessage());
			logger.error("出错：" + sql);
		}
	}

	/**
	 * 推送证书号
	 * 
	 * @作者 think
	 * @创建时间 2016年1月18日下午11:30:36
	 * @param xmbh
	 * @param qltablename
	 * @param zshFieldname
	 * @param zsh
	 */
//	public void updateZSH(String xmbh, String qlid, String zsh) {
//		String sql = "select GXXMBH from gxdjk.GXJHXM where project_id='"
//				+ xmbh + "'";
//		try {
//			if (jyConnection == null || jyConnection.isClosed()) {
//				jyConnection = JH_DBHelper.getConnect_gxdjk();
//			}
//			PreparedStatement pstmt = jyConnection.prepareStatement(sql);
//			ResultSet rSet = pstmt.executeQuery();
////			ResultSet rSet = JH_DBHelper.excuteQuery(jyConnection, sql);
//			String gxxmbh = "'1'";
//			if (rSet != null) {
//				while (rSet.next()) {
//					gxxmbh += ",'" + rSet.getString("GXXMBH") + "'";
//				}
//				pstmt.close();
//				rSet.close();
//			}
//			//重新打开一下连接释放游标资源
//			jyConnection.close();jyConnection=null;
//			jyConnection = JH_DBHelper.getConnect_gxdjk();
//			
//			// 直接按权利ID和项目编号赋证书号
//			sql = "update GXDJK.FDCQ set BDCQZH='" + zsh + "' where GXXMBH IN("
//					+ gxxmbh + ") and qlid='" + qlid + "'";
//			JH_DBHelper.excuteUpdate(jyConnection, sql);
//			sql = "update GXDJK.DYAQ set BDCDJZMH='" + zsh
//					+ "' where GXXMBH IN(" + gxxmbh + ") and qlid='" + qlid
//					+ "'";
//			JH_DBHelper.excuteUpdate(jyConnection, sql);
//			sql = "update GXDJK.YGDJ set BDCDJZMH='" + zsh
//					+ "' where GXXMBH IN(" + gxxmbh + ") and qlid='" + qlid
//					+ "'";
//			JH_DBHelper.excuteUpdate(jyConnection, sql);
//			sql = "update GXDJK.YYDJ set BDCDJZMH='" + zsh
//					+ "' where GXXMBH IN(" + gxxmbh + ") and qlid='" + qlid
//					+ "'";
//			JH_DBHelper.excuteUpdate(jyConnection, sql);
//			sql = "update GXDJK.FDCQZXDJ set BDCQZH='" + zsh
//					+ "' where GXXMBH IN(" + gxxmbh + ") and qlid='" + qlid
//					+ "'";
//			JH_DBHelper.excuteUpdate(jyConnection, sql);
//
//		} catch (SQLException e) {
//			logger.error("出错：" + e.getMessage());
//			logger.error("出错：" + sql);
//		}
//	}

	public void updateZSH_TDDJ(String xmbh, String qlid, String zsh ) {
		String sql = "select GXXMBH from GXTDDJK.GXJHXM where project_id='"
				+ xmbh + "'";
		try {
			if (tddjConnection == null || tddjConnection.isClosed()) {
				tddjConnection = JH_DBHelper.getConnect_gxtddjk();
			}
			PreparedStatement pstmt = tddjConnection.prepareStatement(sql);
			ResultSet rSet = pstmt.executeQuery();
//			ResultSet rSet = JH_DBHelper.excuteQuery(tddjConnection, sql);
			String gxxmbh = "'1'";
			if (rSet != null) {
				while (rSet.next()) {
					gxxmbh += ",'" + rSet.getString("GXXMBH") + "'";
				}
			}
			pstmt.close();
			rSet.close();
			//重新打开一下连接释放游标资源
			jyConnection.close();jyConnection=null;
			jyConnection = JH_DBHelper.getConnect_gxdjk();
			// 直接按权利ID和项目编号赋证书号
			sql = "update GXTDDJK.JSYDSYQ set BDCQZH='" + zsh + "' where GXXMBH IN("
					+ gxxmbh + ") and qlid='" + qlid + "'";
			JH_DBHelper.excuteUpdate(tddjConnection, sql);
			
		} catch (SQLException e) {
			logger.error("出错：" + e.getMessage());
			logger.error("出错：" + sql);
		}
	}

	/**
	 * 退件时删除
	 * @param commonDaoDJ2 
	 * 
	 * @作者 think
	 * @创建时间 2016年1月13日下午4:12:12
	 * @param gxxmbh
	 */
	public void delete(String projectid, CommonDaoDJ commonDaoDJ2) {
		String sql = "select GXXMBH from gxdjk.GXJHXM where project_id='"
				+ projectid + "'";
		try {
			ResultSet rSet = commonDaoDJ2.excuteQuery(sql);
			//SQL里面的IN中的数据量不能超过1000条  2017年3月31日 16:50:18 
			String hql="GXXMBH IN ('1'";
			if (rSet != null) {
				List gxxmbhList=resultSetToList(rSet);
				int temp=0;
				if (gxxmbhList.size() > 0) {
					for (int i = 0; i < gxxmbhList.size(); i++) {
						if (i > 0 && temp==999) {
							hql += ") OR GXXMBH IN ('1'";
							temp=0;
						}
						hql += ",'" + gxxmbhList.get(i) + "'";
						temp++;
					}
					System.out.println(hql);
				}
			}
			hql += ")";
			rSet.close();
			// 删除项目表数据
			sql = "delete from GXDJK.GXJHXM  where "+hql+"";
			commonDaoDJ2.excuteQuery(sql);
			// 删除户表数据
			sql = "delete from GXDJK.H where "+hql+"";
			commonDaoDJ2.excuteQuery(sql);
			// 删除变更前户表数据
			sql = "delete from GXDJK.BGQH  where "+hql+"";
			commonDaoDJ2.excuteQuery(sql);
			// 删除房地产权表数据
			sql = "delete from GXDJK.FDCQ  where  "+hql+"";
			commonDaoDJ2.excuteQuery(sql);
			// 删除变更前房地产权表数据
			sql = "delete from GXDJK.BGQFDCQ  where  "+hql+"";
			commonDaoDJ2.excuteQuery(sql);
			// 删除抵押权表数据
			sql = "delete from GXDJK.DYAQ  where  "+hql+"";
			commonDaoDJ2.excuteQuery(sql);
			// 删除变更前抵押权表数据
			sql = "delete from GXDJK.BGQDYAQ  where  "+hql+"";
			commonDaoDJ2.excuteQuery(sql);
			// 删除预告登记表数据
			sql = "delete from GXDJK.YGDJ  where  "+hql+"";
			commonDaoDJ2.excuteQuery(sql);
			// 删除异议登记表数据
			sql = "delete from GXDJK.YYDJ  where  "+hql+"";
			commonDaoDJ2.excuteQuery(sql);
			// 删除查封登记表数据
			sql = "delete from GXDJK.CFDJ  where  "+hql+"";
			commonDaoDJ2.excuteQuery(sql);
			// 删除限制登记表数据
			sql = "delete from GXDJK.XZDJ  where  "+hql+"";
			commonDaoDJ2.excuteQuery(sql);
			// 删除房屋所有权注销登记表数据
			sql = "delete from GXDJK.FDCQZXDJ  where  "+hql+"";
			commonDaoDJ2.excuteQuery(sql);
			// 删除权利人表数据
			sql = "delete from GXDJK.QLR  where  "+hql+"";
			commonDaoDJ2.excuteQuery(sql);
			// 删除变更前权利人表数据
			sql = "delete from GXDJK.BGQQLR  where  "+hql+"";
			commonDaoDJ2.excuteQuery(sql);

		} catch (SQLException e) {
			logger.error("出错：" + e.getMessage());
			logger.error("出错：" + sql);
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List resultSetToList(ResultSet rs)throws java.sql.SQLException {
		if (rs == null)
			return Collections.EMPTY_LIST;
		ResultSetMetaData md = rs.getMetaData(); // 得到结果集(rs)的结构信息，比如字段数、字段名等
		int columnCount = md.getColumnCount(); // 返回此 ResultSet 对象中的列数
		List list = new ArrayList();
		while (rs.next()) {
			for (int i = 1; i <= columnCount; i++) {
				list.add(rs.getObject(i).toString());
			}
		}
		return list;
	}

	/**
	 * 不物理删除数据，做标记
	 * @作者 李堃
	 * @创建时间 2016年9月20日下午3:56:42
	 * @param projectid
	 * @param commonDaoDJ2 
	 */
	public void deleteEx(String projectid, CommonDaoDJ commonDaoDJ2) {
		String sql = "select GXXMBH from gxdjk.GXJHXM where project_id='"+ projectid + "'";
		try {
			List<Gxjhxm_dj> gxjhxm_djs=commonDaoDJ2.getDataList(Gxjhxm_dj.class, "project_id='"+ projectid + "'");
			String gxxmbh = "'1'";
			if(gxjhxm_djs!=null&& gxjhxm_djs.size()>0){
				for(int i = 0; i < gxjhxm_djs.size(); i++){
					gxxmbh += ",'" + gxjhxm_djs.get(i).getGxxmbh() + "'";
				}
			}
			//吉林房产要求删件时更新推送时间和置空抽取时间 2016年12月1日 14:58:37 卜晓波
			Date dt = new Date();
			String nowString = "to_date('"+ (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(dt)+ "','yyyy-MM-dd HH24:mi:ss')";
			// 删除项目表数据
			String sql1 = "update  GXDJK.GXJHXM set ISDELETE=1,ZT=2,TSSJ= "+nowString+" where GXXMBH in(" + gxxmbh + ")";
			commonDaoDJ2.excuteQueryNoResult(sql1);
		} catch (SQLException e) {
			logger.error("出错：" + e.getMessage());
			logger.error("出错：" + sql);
		}
	}
	
	/**齐齐哈尔房产对接是基于触发器和存储过程实现导致我方登簿环节推送数据不能删除共享交换项目
	 * 2016年9月2日 17:15:43  卜晓波
	 * @param commonDaoDJ2 
	 * */
	public void QQHEdelete(String projectid, CommonDaoDJ commonDaoDJ2) {
		String sql = "select GXXMBH from gxdjk.GXJHXM where project_id='"
				+ projectid + "'";
		try {
			List<Gxjhxm_dj> gxjhxm_djs=commonDaoDJ2.getDataList(Gxjhxm_dj.class, "project_id='"+ projectid + "'");
			String gxxmbh = "'1'";
			if(gxjhxm_djs!=null&& gxjhxm_djs.size()>0){
				for(int i = 0; i < gxjhxm_djs.size(); i++){
					gxxmbh += ",'" + gxjhxm_djs.get(i).getGxxmbh() + "'";
				}
			}
			// 删除户表数据
			sql = "delete from GXDJK.H where GXXMBH in(" + gxxmbh + ")";
			commonDaoDJ2.excuteQueryNoResult(sql);
			// 删除变更前户表数据
			sql = "delete from GXDJK.BGQH  where GXXMBH in(" + gxxmbh + ")";
			commonDaoDJ2.excuteQueryNoResult(sql);
			// 删除房地产权表数据
			sql = "delete from GXDJK.FDCQ  where  GXXMBH in(" + gxxmbh + ")";
			commonDaoDJ2.excuteQueryNoResult(sql);
			// 删除变更前房地产权表数据
			sql = "delete from GXDJK.BGQFDCQ  where  GXXMBH in(" + gxxmbh + ")";
			commonDaoDJ2.excuteQueryNoResult(sql);
			// 删除抵押权表数据
			sql = "delete from GXDJK.DYAQ  where  GXXMBH in(" + gxxmbh + ")";
			commonDaoDJ2.excuteQueryNoResult(sql);
			// 删除变更前抵押权表数据
			sql = "delete from GXDJK.BGQDYAQ  where  GXXMBH in(" + gxxmbh + ")";
			commonDaoDJ2.excuteQueryNoResult(sql);
			// 删除预告登记表数据
			sql = "delete from GXDJK.YGDJ  where  GXXMBH in(" + gxxmbh + ")";
			commonDaoDJ2.excuteQueryNoResult(sql);
			// 删除异议登记表数据
			sql = "delete from GXDJK.YYDJ  where  GXXMBH in(" + gxxmbh + ")";
			commonDaoDJ2.excuteQueryNoResult(sql);
			// 删除查封登记表数据
			sql = "delete from GXDJK.CFDJ  where  GXXMBH in(" + gxxmbh + ")";
			commonDaoDJ2.excuteQueryNoResult(sql);
			// 删除限制登记表数据
			sql = "delete from GXDJK.XZDJ  where  GXXMBH in(" + gxxmbh + ")";
			commonDaoDJ2.excuteQueryNoResult(sql);
			// 删除房屋所有权注销登记表数据
			sql = "delete from GXDJK.FDCQZXDJ  where  GXXMBH in(" + gxxmbh+ ")";
			commonDaoDJ2.excuteQueryNoResult(sql);
			// 删除权利人表数据
			sql = "delete from GXDJK.QLR  where  GXXMBH in(" + gxxmbh + ")";
			commonDaoDJ2.excuteQueryNoResult(sql);
			// 删除变更前权利人表数据
			sql = "delete from GXDJK.BGQQLR  where  GXXMBH in(" + gxxmbh + ")";
			commonDaoDJ2.excuteQueryNoResult(sql);
		} catch (SQLException e) {
			logger.error("出错：" + e.getMessage());
			logger.error("出错：" + sql);
		}
	}
	
	public void delete_tddj(String projectid) {
		String sql = "select GXXMBH from gxtddjk.GXJHXM where project_id='"
				+ projectid + "'";
		try {
			if (tddjConnection == null || tddjConnection.isClosed()) {
				tddjConnection = JH_DBHelper.getConnect_gxtddjk();
			}
			PreparedStatement pstmt = tddjConnection.prepareStatement(sql);
			ResultSet rSet = pstmt.executeQuery();
//			ResultSet rSet = JH_DBHelper.excuteQuery(tddjConnection, sql);
			String gxxmbh = "'1'";
			if (rSet != null) {
				while (rSet.next()) {
					gxxmbh += ",'" + rSet.getString("GXXMBH") + "'";
				}
				pstmt.close();
				rSet.close();
			}
			//重新打开一下连接释放游标资源
			tddjConnection.close();tddjConnection=null;
			tddjConnection = JH_DBHelper.getConnect_gxtddjk();
			
			// 删除项目表数据
			sql = "delete from GXTDDJK.GXJHXM  where GXXMBH in(" + gxxmbh + ")";
			JH_DBHelper.excuteNoQuery(tddjConnection, sql);
			// 删除抵押权表数据
			sql = "delete from GXTDDJK.JSYDSYQ  where  GXXMBH in(" + gxxmbh + ")";
			JH_DBHelper.excuteNoQuery(tddjConnection, sql);
			// 删除权利人表数据
			sql = "delete from GXTDDJK.QLR  where  GXXMBH in(" + gxxmbh + ")";
			JH_DBHelper.excuteNoQuery(tddjConnection, sql);

		} catch (SQLException e) {
			logger.error("出错：" + e.getMessage());
			logger.error("出错：" + sql);
		}
	}

	/**
	 * 获取申请人（义务人）
	 * 
	 * @作者 think
	 * @创建时间 2016年1月19日下午4:10:15
	 * @param xmbh
	 * @return
	 */
	private String getSQR(
			com.supermap.wisdombusiness.framework.dao.impl.CommonDao baseCommonDao,
			String xmbh) {
		String sqrString = "";
		List<BDCS_SQR> sqrList = baseCommonDao.getDataList(BDCS_SQR.class,
				"xmbh='" + xmbh + "' and sqrlb='2'");
		if (sqrList != null && sqrList.size() > 0) {
			for (int i = 0; i < sqrList.size(); i++) {
				sqrString += sqrList.get(i).getSQRXM() + ",";
			}
		}
		if (sqrString.length() > 0) {
			sqrString = sqrString.substring(0, sqrString.length() - 1);
		}
		return sqrString;
	}

	/**
	 * 遂宁版本，抵押时推送所有权数据到变更前表中
	 * 
	 * @作者 likun
	 * @创建时间 2016年3月7日上午11:03:16
	 * @param djdyid
	 * @param commonDaoDJ 
	 * @throws Exception 
	 */
	private void saveSYQToBGQ(
			com.supermap.wisdombusiness.framework.dao.impl.CommonDao baseCommonDao,
			String djdyid, CommonDaoDJ commonDaoDJ) throws Exception {
		// 获取当前所有权
		String sql = "djdyid='"
				+ djdyid
				+ "' and (qllx='4' or qllx='5' or qllx='6' or qllx='7' or qllx='8')";
		List<BDCS_QL_XZ> qlxzs = baseCommonDao.getDataList(BDCS_QL_XZ.class,
				sql);
		if (qlxzs != null && qlxzs.size() > 0) {
			BDCS_QL_XZ syq_xz = qlxzs.get(0);
			String qltablename = Bgqfdcq_dj.class.getName();
			String[] fieldsFromQL = null;
			fieldsFromQL = new String[] { "BDCDYH", "YWH", "QLLX", "DJLX","DJYY", "QXDM", "DJJG", "DBR", "DJSJ", "FJ", "QLID","ZSBH", "BDCQZH" };
			String bsm= UUID.randomUUID().toString().replace("-","");
			String[] otherfields = { "GXXMBH", "BDCDYID" ,"BSM"};
			String[] othervalues = { getGxxmbh(), bdcdyid ,bsm};
			Bgqfdcq_dj bgqfdcq_dj=Tools.createPushObj(qltablename, fieldsFromQL, syq_xz,otherfields, othervalues);
			commonDaoDJ.save(bgqfdcq_dj);

			// 所有权权利人
			sql = "qlid='" + syq_xz.getId() + "'";
			List<BDCS_QLR_XZ> qlrxzs = baseCommonDao.getDataList(
					BDCS_QLR_XZ.class, sql);
			String qlrtablename = Bgqqlr_dj.class.getName();
			String[] fieldsFromQLR = { "QLRID", "BDCDYH", "SXH", "QLRMC",
					"BDCQZH", "QZYSXLH", "ISCZR", "ZJZL", "ZJH", "FZJG",
					"SSHY", "GJ", "HJSZSS", "XB", "DH", "QLID", "BZ", "GYFS",
					"QLBL", "QLRLX", "DZYJ", "GZDW", "YB", "DZ" };
			String[] otherfields2 = { "GXXMBH" ,"BSM"};
			
			for (BDCS_QLR_XZ qlr_xz : qlrxzs) {
				String bsmString= UUID.randomUUID().toString().replace("-","");
				String[] othervalues2 = { getGxxmbh() ,bsmString};
				Bgqqlr_dj bgqqlr_dj=Tools.createPushObj(qlrtablename, fieldsFromQLR, qlr_xz,otherfields2, othervalues2);
				commonDaoDJ.save(bgqqlr_dj);
			}
		}

	}

	//查封登记把该单元的权利人当做查封权利人推到共享登记库的权利人表中
	private void saveSYQTogxdjk(
			com.supermap.wisdombusiness.framework.dao.impl.CommonDao baseCommonDao,
			String djdyidorxmbh, CommonDaoDJ commonDaoDJ) throws Exception {
		String xzqdm = ConfigHelper.getNameByValue("XZQHDM");
		if (xzqdm != null && xzqdm.contains("2202")) {
			//吉林市查封登记推送申请人表中的代理人给房产  2016年12月8日 09:49:09修改  卜晓波
			String sql="XMBH ='"+djdyidorxmbh+"'";
			List<BDCS_SQR> sqrs=baseCommonDao.getDataList(BDCS_SQR.class,sql);
			if(sqrs!=null && sqrs.size()>0){
				for (BDCS_SQR sqr1 : sqrs) {
					try {
						String bsmString= UUID.randomUUID().toString().replace("-","");
						String insertsql="insert into GXDJK.QLR (GXXMBH,BSM,QLRID,QLRMC,QLID,QLRLX,QLBL,GYFS,DH,YB,DZ) values ('"+getGxxmbh()+"','"+bsmString+"','"+sqr1.getId()+"','"+sqr1.getSQRXM()+"','"+sqr1.getGLQLID()+"','"+sqr1.getSQRLB()+"','"+sqr1.getQLBL()+"','"+sqr1.getGYFS()+"','"+sqr1.getLXDH()+"','"+sqr1.getYZBM()+"','"+sqr1.getTXDZ()+"')";
						commonDaoDJ.excuteQueryNoResult(insertsql);
					} catch (SQLException e) {
						logger.error("出错：" + sql);
					}
				}
			}
		}else{
			// 获取当前所有权
			String sql = "djdyid='"+ djdyidorxmbh+ "' and (qllx='4' or qllx='5' or qllx='6' or qllx='7' or qllx='8')";
			List<BDCS_QL_XZ> qlxzs = baseCommonDao.getDataList(BDCS_QL_XZ.class, sql);
			if (qlxzs != null && qlxzs.size() > 0) {
				BDCS_QL_XZ syq_xz = qlxzs.get(0);
				// 所有权权利人
				sql = "qlid='" + syq_xz.getId() + "'";
				List<BDCS_QLR_XZ> qlrxzs = baseCommonDao.getDataList(BDCS_QLR_XZ.class, sql);
				String qlrtablename = Qlr_dj.class.getName();
				String[] otherfields2 = { "GXXMBH" ,"BSM"};
				if(xzqdm.equals("140200")){
					//大同新增法人和代理人信息、代理人姓名、 代理人联系电话、代理人证件号码、代理人证件类型、 
					//代理人证件类型名称、代理机关名称、法定代表人、法定代表人电话、 法定代表人证件号码/法定代表人证件类型
					// 法定代表人证件类型名称
					String[]	fieldsFromQLR = { "QLRID", "BDCDYH", "SXH", "QLRMC","BDCQZH", "QZYSXLH", "ISCZR", "ZJZL", "ZJH", "FZJG","SSHY", "GJ", "HJSZSS", "XB", "DH", "QLID", "BZ", "GYFS","QLBL", "QLRLX", "DZYJ", "GZDW", "YB", "DZ" ,"DLRXM","DLRLXDH","DLRZJHM","DLRZJLX","FDDBR","FDDBRDH","FDDBRZJHM","FDDBRZJLX"};
					for (BDCS_QLR_XZ qlr_xz : qlrxzs) {
						String bsmString= UUID.randomUUID().toString().replace("-","");
						String[] othervalues2 = { getGxxmbh() ,bsmString};
						sql = Tools.createInsertSQL(qlrtablename, fieldsFromQLR,qlr_xz, otherfields2, othervalues2);
						try {
							JH_DBHelper.excuteUpdate(jyConnection, sql);
						} catch (SQLException e) {
							logger.error("出错：" + sql);
						}
					}
				}else {
					String[] fieldsFromQLR = { "QLRID", "BDCDYH", "SXH", "QLRMC",
						"BDCQZH", "QZYSXLH", "ISCZR", "ZJZL", "ZJH", "FZJG",
						"SSHY", "GJ", "HJSZSS", "XB", "DH", "QLID", "BZ", "GYFS",
						"QLBL", "QLRLX", "DZYJ", "GZDW", "YB", "DZ" };
					for (BDCS_QLR_XZ qlr_xz : qlrxzs) {
						String bsmString= UUID.randomUUID().toString().replace("-","");
						String[] othervalues2 = { getGxxmbh() ,bsmString};
						Qlr_dj qlr_dj=Tools.createPushObj(qlrtablename, fieldsFromQLR, qlr_xz,otherfields2, othervalues2);
						commonDaoDJ.save(qlr_dj);
//						CommonDaoDJ.flush();
					}
				}
			}
		}
	}
	
	/**
	 * 对字符串进行加密
	 * @作者 likun
	 * @创建时间 2016年3月10日上午11:07:59
	 * @param text
	 * @param eNCRYPTION 
	 * @return
	 */
    private String encodeText(String text, String eNCRYPTION) {
    	String encodeString=text;
    	int iEncode =0;
    	try{
//    	iEncode =Integer.parseInt(ConfigHelper
//				.getNameByValue("ENCODEQLR"));
        if(eNCRYPTION!=null && eNCRYPTION.equals("1")){
        	String key= ConfigHelper.getNameByValue("ENCODEKEY");
        	encodeString=EncodeTools.encoderByDES(text,key);
        }
    	}
    	catch(Exception ex){}
    	return encodeString;
    }

    /**
     * 推送土地数据
     * @作者 李堃
     * @创建时间 2016年5月18日下午4:09:53
     * @param baseCommonDao
     * @param xmxx
     * @param qltablename
     * @param bljd
     * @param qlsdfs
     * @param iPushBGQ
     * @param djxl
     */
	public void pushToTDDJK(
			com.supermap.wisdombusiness.framework.dao.impl.CommonDao baseCommonDao,
			BDCS_XMXX xmxx, String qltablename, String bljd, String qlsdfs,
			int iPushBGQ, String djxl) {
//		logger.info("进入pushtotddjk");
		if (xmxx == null || baseCommonDao == null) {
			return;
		}
		Boolean bXMXXSaved = false;
		// bljd 办理进度， 0初始推送；1审批；2登簿；3缮证；4发证；5归档；
		setBljd(bljd);
		// 登记小类
		setDjxl(djxl);
		// qlsdfs 权利设定方式，1设立，2转移，3变更，4注销
		setQlsdfs(qlsdfs);
		if (iPushBGQ == 1) {
			setbPushBGQ(true);
		}
//		logger.info("开始查询项目信息");
		String xmbhFilter = ProjectHelper.GetXMBHCondition(xmxx.getId());
		List<BDCS_DJDY_GZ> djdys = baseCommonDao.getDataList(
				BDCS_DJDY_GZ.class, xmbhFilter);
		if (djdys != null && djdys.size() > 0) {
			for (int idjdy = 0; idjdy < djdys.size(); idjdy++) {
				try {
					BDCS_DJDY_GZ djdy = djdys.get(idjdy);
					// 获取户model，户里包含houseid
					String bdcdyh = djdy.getBDCDYH();
					 bdcdyid = djdy.getBDCDYID();
					setBdcdyly(djdy.getBDCDYLX());
					djdyly = djdy.getLY();
					if (bdcdyly.equals(BDCDYLX.SHYQZD.Value)) {
						// 使用权宗地
						if (djdyly.equals(DJDYLY.XZ.Value)) {
							// 来源现在
							// 变更后
							List<BDCS_SHYQZD_XZ> shyqzd_XZs = baseCommonDao.getDataList(
									BDCS_SHYQZD_XZ.class, "bdcdyid='" + bdcdyid
											+ "'");
							
							// 变更前户
							List<BDCS_SHYQZD_LS> shyqzd_LSs = baseCommonDao.getDataList(
									BDCS_SHYQZD_LS.class, "bdcdyid='" + bdcdyid
											+ "'");
							
							if (shyqzd_XZs != null && shyqzd_XZs.size() > 0) {
								setShyqzd_XZ(shyqzd_XZs.get(0));
							}
							if (shyqzd_LSs != null && shyqzd_LSs.size() > 0) {
								setShyqzd_LS(shyqzd_LSs.get(0));
							}
							//土地用途
							List<BDCS_TDYT_XZ> tdyt_XZs = baseCommonDao.getDataList(
									BDCS_TDYT_XZ.class, "bdcdyid='" + bdcdyid
											+ "'");
							List<BDCS_TDYT_LS> tdyt_LSs = baseCommonDao.getDataList(
									BDCS_TDYT_LS.class, "bdcdyid='" + bdcdyid
											+ "'");
							if (tdyt_XZs != null && tdyt_XZs.size() > 0) {
								setTdyt_XZ(tdyt_XZs);
							}
							if (tdyt_LSs != null && tdyt_LSs.size() > 0) {
								setTdyt_LS(tdyt_LSs);
							}
						} else if (djdyly.equals(DJDYLY.GZ.Value)) {
							// 来源工作
							// 变更后
							List<BDCS_SHYQZD_GZ> shyqzd_GZs = baseCommonDao.getDataList(
									BDCS_SHYQZD_GZ.class, "bdcdyid='" + bdcdyid
											+ "'");
							// 变更前户
							List<BDCS_SHYQZD_LS> shyqzd_LSs = baseCommonDao.getDataList(
									BDCS_SHYQZD_LS.class, "bdcdyid='" + bdcdyid
											+ "'");
							if (shyqzd_GZs != null && shyqzd_GZs.size() > 0) {
								setShyqzd_GZ(shyqzd_GZs.get(0));// setH_xz(h_GZs.get(0));
							}
							if (shyqzd_LSs != null && shyqzd_LSs.size() > 0) {
								setShyqzd_LS(shyqzd_LSs.get(0));
							}
							//土地用途
							List<BDCS_TDYT_GZ> tdyt_GZs = baseCommonDao.getDataList(
									BDCS_TDYT_GZ.class, "bdcdyid='" + bdcdyid
											+ "'");
							List<BDCS_TDYT_LS> tdyt_LSs = baseCommonDao.getDataList(
									BDCS_TDYT_LS.class, "bdcdyid='" + bdcdyid
											+ "'");
							if (tdyt_GZs != null && tdyt_GZs.size() > 0) {
								setTdyt_GZ(tdyt_GZs);
							}
							if (tdyt_LSs != null && tdyt_LSs.size() > 0) {
								setTdyt_LS(tdyt_LSs);
							}
						}
					} else if (bdcdyly.equals(BDCDYLX.SYQZD.Value)) {
						// 所有权宗地
						List<BDCS_SYQZD_XZ> syqzd_XZs = baseCommonDao.getDataList(
								BDCS_SYQZD_XZ.class, "bdcdyid='" + bdcdyid + "'");
						// 变更前户
						List<BDCS_SYQZD_LS> syqzd_LSs = baseCommonDao.getDataList(
								BDCS_SYQZD_LS.class, "bdcdyid='" + bdcdyid + "'");
						if (syqzd_XZs != null && syqzd_XZs.size() > 0) {
							setSyqzd_XZ(syqzd_XZs.get(0));
						}
						if (syqzd_LSs != null && syqzd_LSs.size() > 0) {
							setSyqzd_LS(syqzd_LSs.get(0));
						}
						//土地用途
						List<BDCS_TDYT_XZ> tdyt_XZs = baseCommonDao.getDataList(
								BDCS_TDYT_XZ.class, "bdcdyid='" + bdcdyid
										+ "'");
						List<BDCS_TDYT_LS> tdyt_LSs = baseCommonDao.getDataList(
								BDCS_TDYT_LS.class, "bdcdyid='" + bdcdyid
										+ "'");
						if (tdyt_XZs != null && tdyt_XZs.size() > 0) {
							setTdyt_XZ(tdyt_XZs);
						}
						if (tdyt_LSs != null && tdyt_LSs.size() > 0) {
							setTdyt_LS(tdyt_LSs);
						}
					} else {
						return;
					}
					Rights bdcql = RightsTools.loadRightsByDJDYID(
							ConstValue.DJDYLY.GZ, xmxx.getId(),
							djdy.getDJDYID());
					String lyqlid = bdcql.getLYQLID();
					List<RightsHolder> bdcqlrs = RightsHolderTools
							.loadRightsHolders(ConstValue.DJDYLY.GZ,
									bdcql.getId());
					// 变更前权利人
					List<RightsHolder> bdcqlrs_ls = RightsHolderTools
							.loadRightsHolders(ConstValue.DJDYLY.LS, lyqlid);
					// 获取权利model
					List<BDCS_QL_GZ> ql_GZ = baseCommonDao.getDataList(
							BDCS_QL_GZ.class, "qlid='" + bdcql.getId() + "'");
					// 变更前权利model
					List<BDCS_QL_LS> ql_LS = baseCommonDao.getDataList(
							BDCS_QL_LS.class, "qlid='" + lyqlid + "'");
					// 获取附属权利model
					List<BDCS_FSQL_GZ> fsql_GZ = baseCommonDao.getDataList(
							BDCS_FSQL_GZ.class, "fsqlid='" + bdcql.getFSQLID()
									+ "'");
					// 变更前附属权利model
					List<BDCS_FSQL_LS> fsql_LS = null;
					if (ql_LS != null && ql_LS.size() > 0) {
						fsql_LS = baseCommonDao.getDataList(BDCS_FSQL_LS.class,
								"fsqlid='" + ql_LS.get(0).getFSQLID() + "'");
					}
					List<BDCS_QLR_GZ> qlr_gzList = new ArrayList<BDCS_QLR_GZ>();
					// 循环每一个权利人model
					for (RightsHolder rh : bdcqlrs) {
						List<BDCS_QLR_GZ> qlr_GZ = baseCommonDao
								.getDataList(BDCS_QLR_GZ.class,
										"qlrid='" + rh.getId() + "'");
						if (qlr_GZ != null && qlr_GZ.size() > 0) {
							qlr_gzList.add(qlr_GZ.get(0));
						}
					}
					// 变更前权利人
					List<BDCS_QLR_LS> qlr_lsList = new ArrayList<BDCS_QLR_LS>();
					// 循环每一个权利人model
					if (bdcqlrs_ls != null) {
						for (RightsHolder rh : bdcqlrs_ls) {
							List<BDCS_QLR_LS> qlr_ls = baseCommonDao
									.getDataList(BDCS_QLR_LS.class, "qlrid='"
											+ rh.getId() + "'");
							if (qlr_ls != null && qlr_ls.size() > 0) {
								qlr_lsList.add(qlr_ls.get(0));
							}
						}
					}
					if (fsql_GZ != null && fsql_GZ.size() > 0) {
						setFsql_gz(fsql_GZ.get(0));
					}
					if (ql_GZ != null && ql_GZ.size() > 0) {
						setCasenum(ql_GZ.get(0).getCASENUM());
						setQl_gz(ql_GZ.get(0));
					}
					setQlr_gz(qlr_gzList);
					setXmxx(xmxx);
					// 变更前
					if (ql_LS != null && ql_LS.size() > 0) {
						setQl_ls_bgq(ql_LS.get(0));
					}
					if (fsql_LS != null && fsql_LS.size() > 0) {
						setFsql_ls_bgq(fsql_LS.get(0));
					}
					setQlr_lsList_bgq(qlr_lsList);
					try {
						if (tddjConnection == null || tddjConnection.isClosed()) {
							tddjConnection = JH_DBHelper.getConnect_gxtddjk();
						}
					} catch (SQLException e1) {
					}
					gxxmbh = UUID.randomUUID().toString().replace("-","");
					
					// logger.info("开始保存权利信息...");
					saveQL(qltablename, false,databaseType.GXTDDJK);
					// logger.info("开始保存权利人信息...");
					saveQLR(baseCommonDao,databaseType.GXTDDJK);
					// logger.info("开始保存宗地信息...");
					saveZD();
					// logger.info("开始保存土地用途信息...");
					saveTDYT();
					// 保存变更前
					if (bPushBGQ) {
						logger.info("开始保存变更前数据...");
						saveQL(qltablename, bPushBGQ,databaseType.GXTDDJK);
						saveQLR_BGQ(baseCommonDao,databaseType.GXTDDJK);
						saveZD_BGQ();
						saveTDYT_BGQ();
					}
					if (!bXMXXSaved) {
						// bXMXXSaved=true;
						String sqr = getSQR(baseCommonDao, xmxx.getId());
						// logger.info("开始保存项目信息...");
						// 保存项目信息
						saveGXXMXX(bdcdyh, "1", bljd, qlsdfs, casenum, sqr,databaseType.GXTDDJK);
					}
				} catch (Exception ex) {
					allSuccess=false;
					logger.error(ex.getMessage());
				}
			}
		}
	}
	
    public void pushToTDDJK2(com.supermap.wisdombusiness.framework.dao.impl.CommonDao baseCommonDao,
			BDCS_XMXX xmxx, String qltablename, String bljd, String qlsdfs,
			int iPushBGQ, String djxl) {
    	try {
			String url_iserver_data=ConfigHelper.getNameByValue("URL_ISERVER_DATA");
			logger.info("ISERVER数据服务地址："+url_iserver_data);
			OperateFeature hf=new OperateFeature();
				
				String xmbhFilter = ProjectHelper.GetXMBHCondition(xmxx.getId());
				List<BDCS_DJDY_GZ> djdys = baseCommonDao.getDataList(BDCS_DJDY_GZ.class, xmbhFilter);
				if (djdys != null && djdys.size() > 0) {
					for (int idjdy = 0; idjdy < djdys.size(); idjdy++) {
						try {
							BDCS_DJDY_GZ djdy = djdys.get(idjdy);
							// 获取户model，户里包含houseid
							String bdcdyh = djdy.getBDCDYH();
							String bdcdyid = djdy.getBDCDYID();

							Rights bdcql = RightsTools.loadRightsByDJDYID(
									ConstValue.DJDYLY.GZ, xmxx.getId(),
									djdy.getDJDYID());
							String lyqlid = bdcql.getLYQLID();
							List<RightsHolder> bdcqlrs = RightsHolderTools
									.loadRightsHolders(ConstValue.DJDYLY.GZ,
											bdcql.getId());
							// 变更前权利人
							List<RightsHolder> bdcqlrs_ls = RightsHolderTools
									.loadRightsHolders(ConstValue.DJDYLY.LS, lyqlid);
							// 获取权利model
							List<BDCS_QL_GZ> ql_GZ = baseCommonDao.getDataList(
									BDCS_QL_GZ.class, "qlid='" + bdcql.getId() + "'");
							// 变更前权利model
							List<BDCS_QL_LS> ql_LS = baseCommonDao.getDataList(
									BDCS_QL_LS.class, "qlid='" + lyqlid + "'");
							// 获取附属权利model
							List<BDCS_FSQL_GZ> fsql_GZ = baseCommonDao.getDataList(
									BDCS_FSQL_GZ.class, "fsqlid='" + bdcql.getFSQLID()
											+ "'");
							// 变更前附属权利model
							List<BDCS_FSQL_LS> fsql_LS = null;
							if (ql_LS != null && ql_LS.size() > 0) {
								fsql_LS = baseCommonDao.getDataList(BDCS_FSQL_LS.class,
										"fsqlid='" + ql_LS.get(0).getFSQLID() + "'");
							}
							List<BDCS_QLR_GZ> qlr_gzList = new ArrayList<BDCS_QLR_GZ>();
							// 循环每一个权利人model
							for (RightsHolder rh : bdcqlrs) {
								List<BDCS_QLR_GZ> qlr_GZ = baseCommonDao
										.getDataList(BDCS_QLR_GZ.class,
												"qlrid='" + rh.getId() + "'");
								if (qlr_GZ != null && qlr_GZ.size() > 0) {
									qlr_gzList.add(qlr_GZ.get(0));
								}
							}
							// 变更前权利人
							List<BDCS_QLR_LS> qlr_lsList = new ArrayList<BDCS_QLR_LS>();
							// 循环每一个权利人model
							if (bdcqlrs_ls != null) {
								for (RightsHolder rh : bdcqlrs_ls) {
									List<BDCS_QLR_LS> qlr_ls = baseCommonDao
											.getDataList(BDCS_QLR_LS.class, "qlrid='"
													+ rh.getId() + "'");
									if (qlr_ls != null && qlr_ls.size() > 0) {
										qlr_lsList.add(qlr_ls.get(0));
									}
								}
							}
							if (fsql_GZ != null && fsql_GZ.size() > 0) {
								setFsql_gz(fsql_GZ.get(0));
							}
							if (ql_GZ != null && ql_GZ.size() > 0) {
								setCasenum(ql_GZ.get(0).getCASENUM());
								setQl_gz(ql_GZ.get(0));
							}
							setQlr_gz(qlr_gzList);
							setXmxx(xmxx);
							// 变更前
							if (ql_LS != null && ql_LS.size() > 0) {
								setQl_ls_bgq(ql_LS.get(0));
							}
							if (fsql_LS != null && fsql_LS.size() > 0) {
								setFsql_ls_bgq(fsql_LS.get(0));
							}
							setQlr_lsList_bgq(qlr_lsList);
							try {
								if (tddjConnection == null || tddjConnection.isClosed()) {
									tddjConnection = JH_DBHelper.getConnect_gxtddjk();
								}
							} catch (SQLException e1) {
							}
							
							djdyly = djdy.getLY();
								if (djdyly.equals(DJDYLY.XZ.Value)) {
									// 来源现在
									srcDtName="BDCK_SHYQZD_XZ";
								}
								else if (djdyly.equals(DJDYLY.GZ.Value)) {
									// 来源工作
									srcDtName="BDCK_SHYQZD_GZ";
								}
								String sqr = getSQR(baseCommonDao, xmxx.getId());
								// logger.info("开始保存项目信息...");
								// 保存项目信息
								saveGXXMXX(bdcdyh, "1", bljd, qlsdfs, casenum, sqr,false,"","",baseCommonDao,null);
								
								logger.info("开始拷贝宗地图形!");
								String _conditon="bdcdyid='" + bdcdyid+ "'";
								int[] ids= hf.CopyFeatures(srcDsAlias, srcDtName, destDsAlias, destDtName, _conditon, url_iserver_data);
								if(ids!=null&&ids.length>0){
									String[] flds=new String[]{"GXXMBH"};
									String[] fldValues=new String[]{gxxmbh};
									hf.UpdateFeaturesAttr(destDsAlias, destDtName, flds,fldValues , "smid="+ids[0], url_iserver_data);
									logger.info("成功拷贝了"+ids.length+"块宗地!");
								}
								else {
									logger.info("没有符合条件的宗地或拷贝失败!");
								}
									
								// logger.info("开始保存权利信息...");
								saveQL(qltablename, false,databaseType.GXTDDJK);
								// logger.info("开始保存权利人信息...");
								saveQLR(baseCommonDao,databaseType.GXTDDJK);	
						}
						catch(Exception ex){
						}
					}
				}
    	}
    	catch(Exception ex){}
	}

    //中间库类型，5个部门5个登记中间库
   public enum databaseType{
	 GXDJK,  GXTDDJK,  GXLDDJK,  GXHYDJK,  GXNYDJK;
	 }
   public Map<String, String> read (com.supermap.wisdombusiness.framework.dao.impl.CommonDao baseCommonDao){
		  String hql1="name= 'PUSHFJTOFC'";
		  List<T_CONFIG> count1 = baseCommonDao.getDataList(T_CONFIG.class, hql1);
		  String hql2="name= 'tssx'";
		  List<T_CONFIG> count2 = baseCommonDao.getDataList(T_CONFIG.class, hql2);
		  Map<String, String> map=new HashMap<String, String>();
		  map.put("push", count1.get(0).getVALUE());
		  map.put("tssx", count2.get(0).getVALUE());
		  return map;
	  }

   /**
    * 推送附件到GXDJK
    * @作者 think
    * @创建时间 2016年4月18日上午11:49:32
    */
   public void pushFJToGXDJK(com.supermap.wisdombusiness.framework.dao.impl.CommonDao baseCommonDao) {
	   try {
	   //推送PROMATER表内容
	   String tablename = "GXDJK.PROMATER";
		String[] fieldsFromPROMATER = { "MATERILINST_ID", "MATERIAL_ID", "MATERIAL_INDEX", "MATERIAL_NAME", "MATERIAL_NEED",
				"PROINST_ID"};
		String[] otherfields = { "GXXMBH", "CASENUM" };// 
		String[] othervalues = { getGxxmbh(), getCasenum()!=null?getCasenum():xmxx.getPROJECT_ID()};
		String sql = "";
		List<Wfi_ProMater> proMaters=baseCommonDao.getDataList(Wfi_ProMater.class, "PROINST_ID='"+getProinstID(baseCommonDao)+"'");
		if(proMaters!=null &&proMaters.size()>0){
			//推送前先删除一下已有附件
			deleteFJ(proMaters);
			for(int i=0;i<proMaters.size();i++){
				tablename = "GXDJK.PROMATER";
				sql = Tools.createInsertSQL(tablename, fieldsFromPROMATER, proMaters.get(i),
						otherfields, othervalues);
				try {
					JH_DBHelper.excuteUpdate(jyConnection, sql);
					 //推送MATERDATA表内容
				    tablename = "GXDJK.MATERDATA";
					String[] fieldsFromMATERDATA = { "MATERIALDATA_ID", "MATERILINST_ID", "UPLOAD_NAME", "UPLOAD_ID", "UPLOAD_DATE",
							"FILE_NAME","FILE_PATH","FILE_INDEX","PATH"};
					String[] otherfields2 = { "CASENUM" };// "GXXMBH", 
					String[] othervalues2 = {  getCasenum()};//getGxxmbh(),
					List<Wfi_MaterData> materDatas=baseCommonDao.getDataList(Wfi_MaterData.class, "MATERILINST_ID='"+proMaters.get(i).getMaterilinst_Id()+"'");
					if(materDatas!=null &&materDatas.size()>0){
						for(int k=0;k<materDatas.size();k++){
							sql = Tools.createInsertSQL(tablename, fieldsFromMATERDATA, materDatas.get(k),
									otherfields2, othervalues2);
							try {
								JH_DBHelper.excuteUpdate(jyConnection, sql);
								//推送二进制内容
								saveBinary(materDatas.get(k).getPath(),materDatas.get(k).getFile_Path(),materDatas.get(k).getMaterialdata_Id());
							}
							catch(Exception ex){
								logger.error("出错：" + ex.getMessage());
							}
						}
					}
				}
				catch(Exception ex){
					logger.error("附件推送出错：" + ex.getMessage());
				}
			}
		}
	   } catch (Exception e) {
		   logger.error("附件推送出错：" + e.getMessage());
		}
		
}
   /**
    * 保存文件内容到数据库
    * @作者 李堃
    * @创建时间 2016年4月18日下午12:03:49
    * @param path 字段中存的路径值
    * @param filename 文件名称
    * @return
 * @throws FileNotFoundException 
    */
   private int saveBinary(String path,String filename,String MATERIALDATA_ID){
	   int k=0;
	   try{
	   String fullPath=path+filename;
	// 根据文件存储配置选择对应方法存储图片：1、本地存储；2、服务存储
			String materialtype = ConfigHelper.getNameByValue("materialtype");
			
			if (materialtype == null || materialtype.equals("")
					|| materialtype.trim().equals("1")) {
				path=ConfigHelper.getNameByValue("material")+path ;
				fullPath=path+"\\"+filename;
			}
			else if (materialtype.trim().equals("3")) {
				try {
					fullPath="C:\\temp.jpg";
					byte[] buf= Http.getFile(path, filename);
					DeleteFile(fullPath);
					ByteToImage(buf,fullPath);
					
				} catch (ClientProtocolException e) {
				} catch (IOException e) {
				} catch (Exception e) {
					logger.error("出错：" + e.getMessage());
				}
			}
			File  file  =  new  File(fullPath);  
			if(file.exists()){
				int  fileLength  =(int)  file.length();    
				InputStream  fin  =  new  FileInputStream(file);    
				
				//插入附件二进制数据
				String sql="insert  into  FJDATA (MATERIALDATA_ID,CONTENT)  values('"+MATERIALDATA_ID+"',"+"?)";
				PreparedStatement  pstmt  =  jyConnection.prepareStatement(sql);    
				pstmt.setBinaryStream  (1, fin,  fileLength);    
				k=pstmt.executeUpdate();  
			}
			 
	   }
	   catch(Exception ex){
		   logger.error("出错：" + ex.getMessage());
	   }
			return k;
}
   /**
    * 生成图片文件
    * @作者 think
    * @创建时间 2016年4月18日下午12:08:18
    * @param data
    * @param path
    * @throws Exception
    */
	private void ByteToImage(byte[] data, String path) throws Exception {
		if (data.length < 3 || path.equals("")) {
			return;
		}
		FileImageOutputStream imageOutput = new FileImageOutputStream(new File(
				path));
		imageOutput.write(data, 0, data.length);
		imageOutput.close();
	}
	/**
	 * 删除单个文件
	 * 
	 * @param sPath
	 *            被删除文件的文件名
	 * @return 单个文件删除成功返回true，否则返回false
	 */
	private boolean DeleteFile(String sPath) {
		boolean flag = false;
		File file = new File(sPath);
		// 路径为文件且不为空则进行删除
		if (file.isFile() && file.exists()) {
			file.delete();
			flag = true;
		}
		return flag;
	}

	/**
	 * 获取当前项目的proinstid
	 * @作者 likun
	 * @创建时间 2016年4月18日下午4:28:20
	 * @return
	 */
	private String getProinstID(com.supermap.wisdombusiness.framework.dao.impl.CommonDao baseCommonDao) {
		String proinstidString="";
		List<Wfi_ProInst> proInsts=baseCommonDao.getDataList(Wfi_ProInst.class, "file_number='"+xmxx.getPROJECT_ID()+"'");
		if(proInsts!=null&&proInsts.size()>0){
			proinstidString=proInsts.get(0).getProinst_Id();
		}
		return proinstidString;
	}

	/**
	 * 保存宗地信息
	 */
	protected void saveZD() {
		String tablename = "GXTDDJK.ZD";
		String[] fieldsFromQLR = { "BDCDYH", "ZDDM", "ZDTZM", "ZDMJ", "ZL",
				"MJDW", "YT", "DJ", "JG", "QLLX", "QLSDFS", "RJL", "JZMD",
				"JZXG", "ZDSZD", "ZDSZN", "ZDSZX", "ZDSZB",
				"DJH", "ZT", "BDCDYID", "RELATIONID"};
		String bsm= UUID.randomUUID().toString().replace("-","");
		String[] otherfields = { "GXXMBH", "BSM" };
		String[] othervalues = { getGxxmbh(),bsm};
		String sql = "";
		if (bdcdyly.equals(BDCDYLX.SHYQZD.Value)) {
			if (djdyly.equals(DJDYLY.GZ.Value)) {
				if (shyqzd_GZ != null) {
					sql = Tools.createInsertSQL(tablename, fieldsFromQLR, shyqzd_GZ,
							otherfields, othervalues);
					try {
						JH_DBHelper.excuteUpdate(tddjConnection, sql);
						bdcdyid = shyqzd_GZ.getId();
					} catch (SQLException e) {
						allSuccess=false;
						logger.error("出错：" + e.getMessage());
						logger.error("出错：" + sql);
					}
				} else {
					logger.warn("工作使用权对象为空！共享项目号：" + othervalues[0]);
				}
			} else if (djdyly.equals(DJDYLY.XZ.Value)) {
				if (shyqzd_XZ != null) {
					sql = Tools.createInsertSQL(tablename, fieldsFromQLR, shyqzd_XZ,
							otherfields, othervalues);
					try {
						JH_DBHelper.excuteUpdate(tddjConnection, sql);
						bdcdyid = shyqzd_XZ.getId();
					} catch (SQLException e) {
						allSuccess=false;
						logger.error("出错：" + e.getMessage());
						logger.error("出错：" + sql);
					}
				} else {
					logger.warn("工作使用权宗地对象为空！共享项目号：" + othervalues[0]);
				}
			}
		} else if (bdcdyly.equals(BDCDYLX.SYQZD.Value)) {
			if (djdyly.equals(DJDYLY.GZ.Value)) {
				if (syqzd_GZ != null) {
					sql = Tools.createInsertSQL(tablename, fieldsFromQLR, syqzd_GZ,
							otherfields, othervalues);
					try {
						JH_DBHelper.excuteUpdate(tddjConnection, sql);
						bdcdyid = syqzd_GZ.getId();
					} catch (SQLException e) {
						allSuccess=false;
						logger.error("出错：" + e.getMessage());
						logger.error("出错：" + sql);
					}
				} else {
					logger.warn("工作所有权对象为空！共享项目号：" + othervalues[0]);
				}
			} else if (djdyly.equals(DJDYLY.XZ.Value)) {
				if (syqzd_XZ != null) {
					sql = Tools.createInsertSQL(tablename, fieldsFromQLR, syqzd_XZ,
							otherfields, othervalues);
					try {
						JH_DBHelper.excuteUpdate(tddjConnection, sql);
						bdcdyid = syqzd_XZ.getId();
					} catch (SQLException e) {
						allSuccess=false;
						logger.error("出错：" + e.getMessage());
						logger.error("出错：" + sql);
					}
				} else {
					logger.warn("工作所有权对象为空！共享项目号：" + othervalues[0]);
				}
			}
		}

	}
	/**
	 * 保存宗地信息
	 */
	protected void saveZD_BGQ() {
		String tablename = "GXTDDJK.BGQZD";
		String[] fieldsFromQLR = { "BDCDYH", "ZDDM", "ZDTZM", "ZDMJ", "ZL",
				"MJDW", "YT", "DJ", "JG", "QLLX", "QLSDFS", "RJL", "JZMD",
				"JZXG", "ZDSZD", "ZDSZN", "ZDSZX", "ZDSZB",
				"DJH", "ZT", "BDCDYID", "RELATIONID"};
		String bsm= UUID.randomUUID().toString().replace("-","");
		String[] otherfields = { "GXXMBH", "BSM" };
		String[] othervalues = { getGxxmbh(),bsm};
		String sql = "";
		if (bdcdyly.equals(BDCDYLX.SHYQZD.Value)) {
				if (shyqzd_LS != null) {
					sql = Tools.createInsertSQL(tablename, fieldsFromQLR, shyqzd_LS,
							otherfields, othervalues);
					try {
						JH_DBHelper.excuteUpdate(tddjConnection, sql);
					} catch (SQLException e) {
						allSuccess=false;
						logger.error("出错：" + e.getMessage());
						logger.error("出错：" + sql);
					}
				} else {
					logger.warn("历史使用权对象为空！共享项目号：" + othervalues[0]);
				}
		} else if (bdcdyly.equals(BDCDYLX.SYQZD.Value)) {
				if (syqzd_LS != null) {
					sql = Tools.createInsertSQL(tablename, fieldsFromQLR, syqzd_LS,
							otherfields, othervalues);
					try {
						JH_DBHelper.excuteUpdate(tddjConnection, sql);
					} catch (SQLException e) {
						allSuccess=false;
						logger.error("出错：" + e.getMessage());
						logger.error("出错：" + sql);
					}
				} else {
					logger.warn("历史所有权对象为空！共享项目号：" + othervalues[0]);
				}
		}

	}
	
	/**
	 * 保存土地用途信息  2017年4月25日 15:56:36 buxiaobo
	 */
	protected void saveTDYT() {
		String tablename = "GXTDDJK.TDYT";
		String[] fieldsFromQLR = { "BDCDYID", "BZ", "RELATIONID", "GXLX",
				"TDYT", "TDYTMC", "SFZYT", "TDDJ", "TDJG", "QLQSRQ", "QLZZRQ",
				"SYQX", "CRJBZ" };
		String bsm= UUID.randomUUID().toString().replace("-","");
		String[] otherfields = { "GXXMBH", "BSM" };
		String[] othervalues = { getGxxmbh(),bsm};
		String sql = "";
		if (bdcdyly.equals(BDCDYLX.SHYQZD.Value)) {
			if (djdyly.equals(DJDYLY.GZ.Value)) {
				if (tdyt_GZs != null) {
					for(BDCS_TDYT_GZ tdyt_GZ:tdyt_GZs){
						sql = Tools.createInsertSQL(tablename, fieldsFromQLR, tdyt_GZ,
								otherfields, othervalues);
						try {
							JH_DBHelper.excuteUpdate(tddjConnection, sql);
						} catch (SQLException e) {
							allSuccess=false;
							logger.error("出错：" + e.getMessage());
							logger.error("出错：" + sql);
						}
					}
				} else {
					logger.warn("土地用途对象为空！共享项目号：" + othervalues[0]);
				}
			} else if (djdyly.equals(DJDYLY.XZ.Value)) {
				if (tdyt_XZs != null) {
					for(BDCS_TDYT_XZ tdyt_XZ:tdyt_XZs){
						sql = Tools.createInsertSQL(tablename, fieldsFromQLR, tdyt_XZ,
								otherfields, othervalues);
						try {
							JH_DBHelper.excuteUpdate(tddjConnection, sql);
						} catch (SQLException e) {
							allSuccess=false;
							logger.error("出错：" + e.getMessage());
							logger.error("出错：" + sql);
						}
					}
				} else {
					logger.warn("土地用途对象为空！共享项目号：" + othervalues[0]);
				}
			}
		} else if (bdcdyly.equals(BDCDYLX.SYQZD.Value)) {
			if (djdyly.equals(DJDYLY.GZ.Value)) {
				if (tdyt_GZs != null) {
					for(BDCS_TDYT_GZ tdyt_GZ:tdyt_GZs){
						sql = Tools.createInsertSQL(tablename, fieldsFromQLR, tdyt_GZ,
								otherfields, othervalues);
						try {
							JH_DBHelper.excuteUpdate(tddjConnection, sql);
						} catch (SQLException e) {
							allSuccess=false;
							logger.error("出错：" + e.getMessage());
							logger.error("出错：" + sql);
						}
					}
				} else {
					logger.warn("土地用途对象为空！共享项目号：" + othervalues[0]);
				}
			} else if (djdyly.equals(DJDYLY.XZ.Value)) {
				if (tdyt_XZs != null) {
					for(BDCS_TDYT_XZ tdyt_XZ:tdyt_XZs){
						sql = Tools.createInsertSQL(tablename, fieldsFromQLR, tdyt_XZ,
								otherfields, othervalues);
						try {
							JH_DBHelper.excuteUpdate(tddjConnection, sql);
						} catch (SQLException e) {
							allSuccess=false;
							logger.error("出错：" + e.getMessage());
							logger.error("出错：" + sql);
						}
					}
				} else {
					logger.warn("土地用途对象为空！共享项目号：" + othervalues[0]);
				}
			}
		}

	}
	
	//判断单个projectID在中间库是否存在
//    public boolean testSingleExist(String projectid) {
//		boolean bSuc=false;
//		String sql ="select gxxmbh from GXJHXM where project_id ='"+projectid+"'";
//		try {
//			PreparedStatement pstmt = jyConnection.prepareStatement(sql);
//			ResultSet rSet = pstmt.executeQuery();
////			ResultSet rsResultSet= JH_DBHelper.excuteQuery(jyConnection, sql);
//			if(rSet.next()){
//				bSuc=true;
//			}
//			pstmt.close();
//			rSet.close();
//		} catch (SQLException e) {
//		}
//		return bSuc;
//	}

    //将推送失败的数据保存到失败记录表中fail
    @SuppressWarnings("unused")
	public boolean saveFailData(BDCS_XMXX xmxx,String bljd) {
 		boolean b=false;
 		//先判断fail表是否存在，不存在直接返回
 		String sql="SELECT COUNT(1) INTO count FROM DBA_TABLES WHERE TABLE_NAME=UPPER('FAIL') AND OWNER='GXDJK';";
 		try {
 			PreparedStatement pstmt = jyConnection.prepareStatement(sql);
			ResultSet resultSet = pstmt.executeQuery();
//			ResultSet resultSet= JH_DBHelper.excuteQuery(jyConnection, sql);
			if(resultSet!=null &&resultSet.next()){				//////////////////////////////////改-------判断插入fail表，避免无限插
				int ct=resultSet.getInt("count");
				pstmt.close();
				resultSet.close();
				if(ct==0){
					return false;
				}
			}
		} catch (SQLException e1) {
		}
 		
 		String xmmc = xmxx.getXMMC();
 		String bsm = UUID.randomUUID().toString().replace("-","");
 		String prjId = xmxx.getId();// 项目编号
 		String project_id=xmxx.getPROJECT_ID();
 		String djlx = xmxx.getDJLX();
 		String qllx = xmxx.getQLLX();
 		String slry = xmxx.getSLRY(); 
 		Date slsj = xmxx.getSLSJ();
 		 sql="insert into FAIL (XMMC,PROJECT_ID,DJDL,QLLX,SLRY,SLSJ,BLJD,CASENUM,BSM,FAILTYPE) VALUES('"+xmmc+"','"
 		+project_id+"','"+djlx+"','"+qllx+"','"+slry+"',to_date('"+(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(slsj)+"','yyyy-MM-dd HH24:mi:ss'),'"
 		+bljd+"','"+casenum+"','"+bsm+"','1')";
 		 //先执行删除已有记录
 		String deleteSql = "delete from gxdjk.fail where project_id = '"+project_id+"'";
 		try {
 			JH_DBHelper.excuteNoQuery(jyConnection, deleteSql);
 			JH_DBHelper.excuteUpdate(jyConnection, sql);
 			b=true;
 		} catch (SQLException e) {
 			e.printStackTrace();
 		}
 		return b;
 	}
  //将推送失败的数据保存到失败记录表中pushfail表
    public boolean saveFailData(BDCS_XMXX xmxx,String bljd,com.supermap.wisdombusiness.framework.dao.impl.CommonDao baseCommonDao) {
 		boolean b=false;
 		//先判断fail表是否存在，不存在直接返回
 		String sql="SELECT COUNT(1) as count  FROM DBA_TABLES WHERE TABLE_NAME=UPPER('PUSHFAIL') AND OWNER='BDCK'";
 		try {
// 			List<Map> tblList=baseCommonDao.getDataListByFullSql(sql);
//			if(tblList!=null &&tblList.size()>0){				//////////////////////////////////改-------判断插入fail表，避免无限插
//				int ct=Integer.getInteger(tblList.get(0).get("COUNT").toString());
//				if(ct==0){
//					return false;
//				}
//			}
		} catch (Exception e1) {
		}
 		
 		String xmmc = xmxx.getXMMC();
 		String bsm = UUID.randomUUID().toString().replace("-","");
 		String prjId = xmxx.getId();// 项目编号
 		String project_id=xmxx.getPROJECT_ID();
 		String djlx = xmxx.getDJLX();
 		String qllx = xmxx.getQLLX();
 		String slry = xmxx.getSLRY(); 
 		Date slsj = xmxx.getSLSJ();
 		String ywlshString=xmxx.getYWLSH();
 		 sql="insert into BDCK.PUSHFAIL (XMMC,PROJECT_ID,DJLX,QLLX,SLRY,SLSJ,BLJD,CASENUM,SBLX,ID,YWLSH) VALUES('"+xmmc+"','"
 		+project_id+"','"+djlx+"','"+qllx+"','"+slry+"',to_date('"+(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(slsj)+"','yyyy-MM-dd HH24:mi:ss'),'"
 		+bljd+"','"+casenum+"','1','"+bsm+"','"+ywlshString+"')";
 		 //先执行删除已有记录
 		String deleteSql = "delete from BDCK.PUSHFAIL where project_id = '"+project_id+"'";
 		try {
 			int k=baseCommonDao.updateBySql(deleteSql);
 			k=baseCommonDao.updateBySql(sql);
 			b=true;
 		} catch (Exception e) {
 			e.printStackTrace();
 		}
 		return b;
 	}
    
    
    /**
	 * 保存变更前土地用途信息  2017年4月25日 15:56:36 buxiaobo
	 */
	protected void saveTDYT_BGQ() {
		String tablename = "GXTDDJK.BGQTDYT";
		String[] fieldsFromQLR = { "BDCDYID", "BZ", "RELATIONID", "GXLX",
				"TDYT", "TDYTMC", "SFZYT", "TDDJ", "TDJG", "QLQSRQ", "QLZZRQ",
				"SYQX", "CRJBZ" };
		String bsm= UUID.randomUUID().toString().replace("-","");
		String[] otherfields = { "GXXMBH", "BSM" };
		String[] othervalues = { getGxxmbh(),bsm};
		String sql = "";
		if (bdcdyly.equals(BDCDYLX.SHYQZD.Value)) {
			if (tdyt_LSs != null) {
				for(BDCS_TDYT_LS tdyt_LS:tdyt_LSs){
					sql = Tools.createInsertSQL(tablename, fieldsFromQLR, tdyt_LS,
							otherfields, othervalues);
					try {
						JH_DBHelper.excuteUpdate(tddjConnection, sql);
					} catch (SQLException e) {
						allSuccess=false;
						logger.error("出错：" + e.getMessage());
						logger.error("出错：" + sql);
					}
				}
			} else {
				logger.warn("土地用途历史对象为空！共享项目号：" + othervalues[0]);
			}
		} else if (bdcdyly.equals(BDCDYLX.SYQZD.Value)) {
			if (tdyt_LSs != null) {
				for(BDCS_TDYT_LS tdyt_LS:tdyt_LSs){
					sql = Tools.createInsertSQL(tablename, fieldsFromQLR, tdyt_LS,
							otherfields, othervalues);
					try {
						JH_DBHelper.excuteUpdate(tddjConnection, sql);
					} catch (SQLException e) {
						allSuccess=false;
						logger.error("出错：" + e.getMessage());
						logger.error("出错：" + sql);
					}
				}
			} else {
				logger.warn("土地用途历史对象为空！共享项目号：" + othervalues[0]);
			}
		}
	}
    /**
     * 重新检查核对已登薄数据，刷新gxdjk中的fail表
     * 一、用登记库中项目表中的已登薄project_id去GXDJK查；二、核对登记库中每个projectID对应的单元数与GXDJK中单元数是否一致
     * @作者 李堃
     * @创建时间 2016年5月19日上午10:38:33
     * @return
     */
    public void reFreshFailTable(com.supermap.wisdombusiness.framework.dao.impl.CommonDao baseCommonDao) {
		boolean b=true;
		List<BDCS_XMXX> xmxxs = baseCommonDao.getDataList(BDCS_XMXX.class, " SFDB='1'");
		if(xmxxs!=null && xmxxs.size()>0){
			String sql="";
			setJyConnection(JH_DBHelper.getConnect_gxdjk());
			System.out.println(jyConnection);
			if(jyConnection==null){
				return ;
			}
			for(int i=0;i<xmxxs.size();i++){
				String project_id= StringHelper.formatObject(xmxxs.get(i).getPROJECT_ID());
				sql="select * from gxjhxm where project_id='"+project_id+"'";///////////////////////////改
				try {
					//用登记中已登薄的去项目表查
					PreparedStatement pstmt=jyConnection.prepareStatement(sql);
					ResultSet rSet= pstmt.executeQuery();
					PreparedStatement pstmt2=null;///////////////////////////改--------判断插入fail表，避免无限插
					if(null!=xmxxs.get(i)){
						String failobj="select * from gxdjk.fail where project_id='"+xmxxs.get(i).getPROJECT_ID()+"'";
						pstmt2=jyConnection.prepareStatement(failobj);
					}
					//判断是否已经插入过
					ResultSet rSet2= pstmt2.executeQuery();
					
					if(rSet==null||!rSet.next()){
						//如果没查到，说明推送失败，存入失败表
						if(rSet2==null||!rSet2.next()){
							saveFailData(xmxxs.get(i), "2");
						}
						if(pstmt2!=null){
							pstmt2.close();
						}
						if(rSet2!=null){
							rSet2.close();
						}
					}                 ///////////////////////////改--------判断插入fail表，避免无限插
					else {
							pstmt.close();
						//如果查到，进一步检查个数                                        
						long djdyCount=baseCommonDao.getCountByFullSql(" from bdck.bdcs_djdy_gz where xmbh='"+xmxxs.get(i).getId()+"'");
						long jhxmCount=0;
						sql="select count(*) as rowCount from gxdjk.gxjhxm where project_id='"+xmxxs.get(i).getPROJECT_ID()+"'";
						pstmt=jyConnection.prepareStatement(sql);
						ResultSet rSet3=pstmt.executeQuery();
						if(rSet3!=null &&rSet3.next()){				//////////////////////////////////改-------判断插入fail表，避免无限插
							jhxmCount=rSet3.getInt("rowCount");
						}
							pstmt.close();
						if(rSet3!=null){
							rSet3.close();
						}
						if(djdyCount!=jhxmCount){
							//个数不一样，有失败的，重推
							if(rSet2==null||!rSet2.next()){
								saveFailData(xmxxs.get(i), "2");
							}
							if(pstmt2!=null){
								pstmt2.close();
							}
							if(rSet2!=null){
								rSet2.close();
							}
						}
					}							//////////////////////////////////改
						if(pstmt!=null){
							pstmt.close();
						}
					if(rSet!=null){				/////////改   关闭资源
						rSet.close();
					}
					
				} catch (SQLException e) {		/////////改
					b=false;
				}
			}
			try {
				if(jyConnection!=null){
					jyConnection.close();
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
    
    /**
     * 重新检查核对已登薄数据，刷新bdck中的pushfail表
     * 一、用登记库中项目表中的已登薄project_id去GXDJK查；二、核对登记库中每个projectID对应的单元数与GXDJK中单元数是否一致
     * @作者 李堃
     * @创建时间 2016年5月19日上午10:38:33
     * @return
     */
    public void reFreshFailTable2(com.supermap.wisdombusiness.framework.dao.impl.CommonDao baseCommonDao) {
		boolean b=true;
		String sql=" SFDB='1' ";
		String xzqdm = ConfigHelper.getNameByValue("XZQHDM");
		if (xzqdm != null && xzqdm.contains("1301")) {
			//石家庄只推送几种
			sql+=" and QLLX IN ('4','23','8','99','98')";
		}
		List<BDCS_XMXX> xmxxs = baseCommonDao.getDataList(BDCS_XMXX.class, sql);
		if(xmxxs!=null && xmxxs.size()>0){
			setJyConnection(JH_DBHelper.getConnect_gxdjk());
			System.out.println(jyConnection);
			if(jyConnection==null){
				return ;
			}
			List<Map> faiList= null;
			for(int i=0;i<xmxxs.size();i++){
				String project_id= StringHelper.formatObject(xmxxs.get(i).getPROJECT_ID());
				sql="select * from gxjhxm where project_id='"+project_id+"'";///////////////////////////改
				try {
					//用登记中已登薄的去项目表查
					PreparedStatement pstmt=jyConnection.prepareStatement(sql);
					ResultSet rSet= pstmt.executeQuery();
					///////////////////////////改--------判断插入fail表，避免无限插
					if(null!=xmxxs.get(i)){
						String failobj="select * from bdck.pushfail where project_id='"+xmxxs.get(i).getPROJECT_ID()+"'";
//						pstmt2=jyConnection.prepareStatement(failobj);
						 faiList= baseCommonDao.getDataListByFullSql(failobj);
					}
					//判断是否已经插入过
//					ResultSet rSet2= pstmt2.executeQuery();
					if(rSet==null||!rSet.next()){
						//如果没查到，说明推送失败，存入失败表
						if(faiList==null||faiList.size()==0){
							saveFailData(xmxxs.get(i), "2",baseCommonDao);
						}
					}                 ///////////////////////////改--------判断插入fail表，避免无限插
					else {
							pstmt.close();
						//如果查到，进一步检查个数                                        
						long djdyCount=baseCommonDao.getCountByFullSql(" from bdck.bdcs_djdy_gz where xmbh='"+xmxxs.get(i).getId()+"'");
						long jhxmCount=0;
						sql="select count(*) as rowCount from gxdjk.gxjhxm where project_id='"+xmxxs.get(i).getPROJECT_ID()+"'";
						pstmt=jyConnection.prepareStatement(sql);
						ResultSet rSet3=pstmt.executeQuery();
						if(rSet3!=null &&rSet3.next()){				//////////////////////////////////改-------判断插入fail表，避免无限插
							jhxmCount=rSet3.getInt("rowCount");
						}
							pstmt.close();
						if(rSet3!=null){
							rSet3.close();
						}
						if(djdyCount!=jhxmCount){
							//个数不一样，有失败的，重推
							if(faiList==null||faiList.size()==0){
								saveFailData(xmxxs.get(i), "2",baseCommonDao);
							}
						}
					}							//////////////////////////////////改
						if(pstmt!=null){
							pstmt.close();
						}
					if(rSet!=null){				/////////改   关闭资源
						rSet.close();
					}
					
				} catch (SQLException e) {		/////////改
					b=false;
				}
			}
			try {
				if(jyConnection!=null){
					jyConnection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
    
    /**
     * 判断是否存在fail表
     * @作者 think
     * @创建时间 2016年6月1日下午4:50:05
     * @return
     */
     public boolean existFailTable() {
    	 boolean b=false;
    	 String sql="SELECT COUNT(1) as count FROM DBA_TABLES WHERE TABLE_NAME=UPPER('FAIL') AND OWNER='GXDJK'";
  		try {
  			PreparedStatement pstmt = jyConnection.prepareStatement(sql);
			ResultSet resultSet = pstmt.executeQuery();
// 			ResultSet resultSet= JH_DBHelper.excuteQuery(jyConnection, sql);
 			if(resultSet!=null &&resultSet.next()){				//////////////////////////////////改-------判断插入fail表，避免无限插
 				int ct=resultSet.getInt("COUNT");
 				pstmt.close();
 				if(ct>0){
 					b=true;
 				}
 				resultSet.close();
 			}
 		} catch (SQLException e1) {
 		}
  		return b;
	}

     /**
 	 * 删除失败表中的记录
 	 * @作者 think
 	 * @创建时间 2016年6月1日下午4:39:36
 	 * @param projectids ，多个projectID用，隔开
 	 */
     public void deleteFailRecord(String projectidsString) {
		if(projectidsString!=null){
			String[] prjs= projectidsString.split(",");
			if(prjs!=null&&prjs.length>0){
				String sql="('0'";
				for(int i=0;i<prjs.length;i++){
					sql+=",'"+prjs[i]+"'";
				}
				sql +=")";
				sql="delete from fail where project_id in "+sql;
				try {
					JH_DBHelper.excuteNoQuery(jyConnection, sql);
				} catch (SQLException e) {
				}
			}
			
		}
	}

     /**
  	 * 将推送失败项目信息TSCS更新为0
  	 * @作者 卜晓波
  	 * @创建时间 2016年6月26日 17:08:20
  	 */
      public void updateXMXXTSCS(BDCS_XMXX xmxx,Integer tscs,com.supermap.wisdombusiness.framework.dao.impl.CommonDao baseCommonDao) throws SQLException {
    	  xmxx.setTSCS(tscs);
		  baseCommonDao.update(xmxx);
 	}

	/**
	 * ★★★限制登记★★★业务单独推送
	 * @param relationid2gxxmbh 
	 * 
	 * @创建时间 2016年7月22日 11:28:27
	 */
	public void pushXZDJToGXDJK(
			com.supermap.wisdombusiness.framework.dao.impl.CommonDao baseCommonDao,com.supermap.wisdombusiness.framework.dao.impl.CommonDaoDJ CommonDaoDJ,
			BDCS_XMXX xmxx, String qltablename, String bljd, String qlsdfs,
			int iPushBGQ, String djxl, Map<String, String> relationid2gxxmbh) {
		if (xmxx == null || baseCommonDao == null) {
			return;
		}
		Boolean bXMXXSaved = false;
		// bljd 办理进度， 0初始推送；1审批；2登簿；3缮证；4发证；5归档；
		setBljd(bljd);
		// zt 状态，1为有效；2为无效
		// setZt(zt);
		// 登记小类
		setDjxl(djxl);
		// qlsdfs 权利设定方式，1设立，2转移，3变更，4注销
		setQlsdfs(qlsdfs);
		if (iPushBGQ == 1) {
			setbPushBGQ(true);
		}
		//setQltablename(qltable);
		String xmbhFilter = ProjectHelper.GetXMBHCondition(xmxx.getId());
		List<BDCS_DJDY_GZ> djdys = baseCommonDao.getDataList(
				BDCS_DJDY_GZ.class, xmbhFilter);
		if (djdys != null && djdys.size() > 0) {
			for (int idjdy = 0; idjdy < djdys.size(); idjdy++) {
				try {
					BDCS_DJDY_GZ djdy = djdys.get(idjdy);
					// 获取户model，户里包含houseid
					String bdcdyh = djdy.getBDCDYH();
					String bdcdyid = djdy.getBDCDYID();
					setBdcdyly(djdy.getBDCDYLX());
					djdyly = djdy.getLY();
					if (bdcdyly.equals(BDCDYLX.H.Value)) {
						// 实测户
						if (djdyly.equals(DJDYLY.XZ.Value)) {
							// 来源现在
							// 变更后
							List<BDCS_H_XZ> h_XZs = baseCommonDao.getDataList(BDCS_H_XZ.class, "bdcdyid='" + bdcdyid+ "'");
							// 变更前户
							List<BDCS_H_LS> h_LSs = baseCommonDao.getDataList(BDCS_H_LS.class, "bdcdyid='" + bdcdyid+ "'");
							if (h_XZs != null && h_XZs.size() > 0) {
								setH_xz(h_XZs.get(0));
							}
							if (h_LSs != null && h_LSs.size() > 0) {
								setH_ls_bgq(h_LSs.get(0));
							}
						} else if (djdyly.equals(DJDYLY.GZ.Value)) {
							// 来源工作
							// 变更后
							List<BDCS_H_GZ> h_GZs = baseCommonDao.getDataList(BDCS_H_GZ.class, "bdcdyid='" + bdcdyid+ "'");
							// 变更前户
							List<BDCS_H_LS> h_LSs = baseCommonDao.getDataList(BDCS_H_LS.class, "bdcdyid='" + bdcdyid+ "'");
							if (h_GZs != null && h_GZs.size() > 0) {
								setH_GZ(h_GZs.get(0));// setH_xz(h_GZs.get(0));
							}
							if (h_LSs != null && h_LSs.size() > 0) {
								setH_ls_bgq(h_LSs.get(0));
							}
						}
					} else if (bdcdyly.equals(BDCDYLX.YCH.Value)) {
						// 预测户
						List<BDCS_H_XZY> h_XZs = baseCommonDao.getDataList(BDCS_H_XZY.class, "bdcdyid='" + bdcdyid + "'");
						// 变更前户
						List<BDCS_H_LSY> h_LSs = baseCommonDao.getDataList(BDCS_H_LSY.class, "bdcdyid='" + bdcdyid + "'");
						if (h_XZs != null && h_XZs.size() > 0) {
							setH_XZY(h_XZs.get(0));
						}
						if (h_LSs != null && h_LSs.size() > 0) {
							setH_LSY(h_LSs.get(0));
						}
					} else {
						return;
					}
					setXmxx(xmxx);
					try {
						if (jyConnection == null || jyConnection.isClosed()) {
							jyConnection = JH_DBHelper.getConnect_gxdjk();
						}
					} catch (SQLException e1) {

					}
					//保证登簿后推送项目共享项目编号与受理转出相同 2016年8月31日 08:33:22 卜晓波
					if(relationid2gxxmbh.get("SLZCNoPush")!=null ||relationid2gxxmbh.size()==0){
						gxxmbh = UUID.randomUUID().toString().replace("-","");
					}else{
						if(bljd.equals("2") ){
							String relationid="";
							if(h_xz!=null){
								relationid=h_xz.getRELATIONID();
							}else if(h_XZY!=null&& relationid.equals("")){
								relationid=h_XZY.getRELATIONID();
							}else if(h_GZ!=null&& relationid.equals("")){
								relationid=h_GZ.getRELATIONID();
							}
							gxxmbh = relationid2gxxmbh.get(relationid);
						}else{
							gxxmbh = UUID.randomUUID().toString().replace("-","");
							}
					}
					List<BDCS_DYXZ> xzdjlist=new ArrayList<BDCS_DYXZ>();
					if(xmxx.getDJLX().equals("900")&&xmxx.getQLLX().equals("99")){
						xzdjlist = baseCommonDao.getDataList(BDCS_DYXZ.class,  "bdcdyid='" + bdcdyid + "' and xmbh ='"+xmxx.getId()+"'");
					}else if(xmxx.getDJLX().equals("900")&&xmxx.getQLLX().equals("98")){
						List<BDCS_XM_DYXZ> xm_DYXZs=baseCommonDao.getDataList(BDCS_XM_DYXZ.class, "xmbh='"+xmxx.getId()+"' and bdcdyid='"+bdcdyid+"'");
						if(xm_DYXZs!=null&&xm_DYXZs.size()>0){
							xzdjlist = baseCommonDao.getDataList(BDCS_DYXZ.class,  "bdcdyid='" + bdcdyid + "' and id ='"+xm_DYXZs.get(0).getDYXZID()+"'");
						}
					}
					if (xzdjlist != null && xzdjlist.size() > 0) {
//						saveXZDJ(qltablename, xzdjlist);
						QJsaveXZDJ("XZDJ", xzdjlist,baseCommonDao,CommonDaoDJ);
						QJsaveXZDJ2("XZDJ2", xzdjlist,baseCommonDao,CommonDaoDJ);
					}
					saveH(CommonDaoDJ);
					if (!bXMXXSaved) {
						// bXMXXSaved=true;
						String sqr = getSQR(baseCommonDao, xmxx.getId());
						// logger.info("开始保存项目信息...");
						// 保存项目信息
						saveGXXMXX(bdcdyh, "1", bljd, qlsdfs, casenum, sqr,false,"","",baseCommonDao,CommonDaoDJ);
					}
				} catch (Exception ex) {
					allSuccess=false;
					logger.error(ex.getMessage());
				}
			}
		}
	}
	//删除附件
	public void deleteFJ(List<Wfi_ProMater> promaterList) {
		if(promaterList!=null&&promaterList.size()>0){
			String sql="";
			for(int i=0;i<promaterList.size();i++){
				String materilinst_Id=promaterList.get(i).getMaterilinst_Id();
				sql="select MATERIALDATA_ID from GXDJK.MATERDATA WHERE MATERILINST_ID='"+materilinst_Id+"'";
				try {
					if(jyConnection==null||jyConnection.isClosed()==true){
						jyConnection=JH_DBHelper.getConnect_gxdjk();
					}
					PreparedStatement pstmt = jyConnection.prepareStatement(sql);
					ResultSet materdataRs = pstmt.executeQuery();
//					ResultSet materdataRs=JH_DBHelper.excuteQuery(jyConnection, sql);
					if(materdataRs!=null){
						//删除FJDATA
						while(materdataRs.next()){
							String materdaid=materdataRs.getString(1);
							sql="delete from GXDJK.FJDATA WHERE MATERIALDATA_ID='"+materdaid+"'";
							JH_DBHelper.excuteNoQuery(jyConnection, sql);
						}
						pstmt.close();
						materdataRs.close();
					}
					//删除MATERDATA
					sql="delete from GXDJK.MATERDATA WHERE MATERILINST_ID='"+materilinst_Id+"'";
					JH_DBHelper.excuteNoQuery(jyConnection, sql);
					//删除PROMATER
					sql="delete from GXDJK.PROMATER WHERE MATERILINST_ID='"+materilinst_Id+"'";
					JH_DBHelper.excuteNoQuery(jyConnection, sql);
				} catch (SQLException e) {
				}
			}
		}
	}
	
	/**
	 * 保存限制登记权利
	 * **/
	@SuppressWarnings("unused")
	private void saveXZDJ(String qltablename, List<BDCS_DYXZ> xzdjlist) {
		BDCS_DYXZ xzdj=xzdjlist.get(0);
		String bsmString= UUID.randomUUID().toString().replace("-","");
		String[] vls = new String[15];
		vls[0] = gxxmbh;
		vls[1] = xzdj.getBDCDYH();
		vls[2] = xmxx.getYWLSH();
		vls[3] = xzdj.getXZLX();
		vls[4] = xzdj.getXZDW();
		vls[5] = xzdj.getXZWJHM();
		vls[6] = xzdj.getXZFW();
		vls[7] = xzdj.getZXXZDW();
		vls[8] = xzdj.getZXXZWJHM();
		vls[9] = "to_date('"
				+ (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(xzdj.getXZQSRQ())
				+ "','yyyy-MM-dd HH24:mi:ss')";
		vls[10] = "to_date('"
				+ (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(xzdj.getXZZZRQ())
				+ "','yyyy-MM-dd HH24:mi:ss')";
		vls[11] = xzdj.getDBR();
		vls[12] = "to_date('"
				+ (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(xzdj.getDJSJ())
				+ "','yyyy-MM-dd HH24:mi:ss')";
		vls[13] = xzdj.getZXYWH();
		vls[14] = bsmString;
		String sql = "insert into gxdjk.XZDJ (GXXMBH,BDCDYH,YWH,CFLX,CFJG,CFWH,CFFW,JFJG,JFWH,CFQSSJ,CFJSSJ,DBR,DJSJ,JFYWH,BSM) values ('"
				+ vls[0]
				+ "','"
				+ vls[1]
				+ "','"
				+ vls[2]
				+ "','"
				+ vls[3]
				+ "','"
				+ vls[4]
				+ "','"
				+ vls[5]
				+ "','"
				+ vls[6]
				+ "','"
				+ vls[7]
				+ "','"
				+ vls[8]
				+ "', "
				+ vls[9]
				+ " , "
				+ vls[10]
				+ ",'"
				+ vls[11]
				+ "', "
				+ vls[12]
				+ ",'"
				+ vls[13]
				+ "','"+bsmString+ "')";
		try {
			JH_DBHelper.excuteUpdate(jyConnection, sql);
			//logger.info("插入共享项目数据条数：" + i);
		} catch (SQLException e) {
			allSuccess=false;
			FailCause=e.toString();
			logger.error("出错：" + sql);
		}
	}
	
	  /**
			 * 判断字段是否存在
			 * @作者 likun
			 * @创建时间 2016年8月30日下午2:54:31
			 * @param user 用户名
			 * @param tablename 表名
			 * @param fldName 字段名
			 * @return
			 */
			public boolean getFieldExist(String user,String tablename,String fldName) {
				boolean bExist=false;
				String sql="SELECT COUNT(1)  FROM DBA_TAB_COLUMNS WHERE OWNER='"+user+"' AND TABLE_NAME='"+tablename+"' AND COLUMN_NAME='"+fldName+"'";		
					try {
						try {
							if (jyConnection == null || jyConnection.isClosed()) {
								jyConnection = JH_DBHelper.getConnect_gxdjk();
							}
						} catch (SQLException e1) {
						}
						PreparedStatement pstmt = jyConnection.prepareStatement(sql);
						ResultSet rsSet = pstmt.executeQuery();
//						ResultSet rsSet= JH_DBHelper.excuteQuery(jyConnection, sql);
						if(rsSet!=null){
							if(rsSet.next()){
								int i=rsSet.getInt(1);
								if(i>0){
									bExist=true;
								}
							}
							pstmt.close();
							rsSet.close();
						}
						
					} catch (SQLException e) {
					}
				return bExist;
			}

			 /**
		     * 重新检查核对已登薄数据，刷新gxdjk中的fail表
		     * 一、用登记库中项目表中的已登薄project_id去GXDJK查；二、核对登记库中每个projectID对应的单元数与GXDJK中单元数是否一致
		     * @作者 李堃
		     * @创建时间 2016年5月19日上午10:38:33
		     * @return
		     */
		    public void reFreshXZFailTable(com.supermap.wisdombusiness.framework.dao.impl.CommonDao baseCommonDao) {
				boolean b=true;
				String sql=" (DJLX ='800' AND (QLLX='99' OR QLLX='98')) OR (DJLX='100' AND QLLX='23') OR (DJLX='700' AND (QLLX='4' OR QLLX='99'))";
				List<BDCS_XMXX> xmxxs = baseCommonDao.getDataList(BDCS_XMXX.class, sql);
				if(xmxxs!=null && xmxxs.size()>0){
					setJyConnection(JH_DBHelper.getConnect_gxdjk());
					System.out.println(jyConnection);
					if(jyConnection==null){
						return ;
					}
					for(int i=0;i<xmxxs.size();i++){
						String project_id= StringHelper.formatObject(xmxxs.get(i).getPROJECT_ID());
						sql="select * from gxjhxm where project_id='"+project_id+"'";///////////////////////////改
						try {
							//用登记中已登薄的去项目表查
							PreparedStatement pstmt=jyConnection.prepareStatement(sql);
							ResultSet rSet= pstmt.executeQuery();
							PreparedStatement pstmt2=null;///////////////////////////改--------判断插入fail表，避免无限插
							if(null!=xmxxs.get(i)){
								String failobj="select * from gxdjk.fail where project_id='"+xmxxs.get(i).getPROJECT_ID()+"'";
								pstmt2=jyConnection.prepareStatement(failobj);
							}
							//判断是否已经插入过
							ResultSet rSet2= pstmt2.executeQuery();
							
							if(rSet==null||!rSet.next()){
								//如果没查到，说明推送失败，存入失败表
								if(rSet2==null||!rSet2.next()){
									saveFailData(xmxxs.get(i), "2");
								}
								if(pstmt2!=null){
									pstmt2.close();
								}
								if(rSet2!=null){
									rSet2.close();
								}
							}                 ///////////////////////////改--------判断插入fail表，避免无限插
							else {
									pstmt.close();
								//如果查到，进一步检查个数                                        
								long djdyCount=baseCommonDao.getCountByFullSql(" from bdck.bdcs_djdy_gz where xmbh='"+xmxxs.get(i).getId()+"'");
								long jhxmCount=0;
								sql="select count(*) as rowCount from gxdjk.gxjhxm where project_id='"+xmxxs.get(i).getPROJECT_ID()+"'";
								pstmt=jyConnection.prepareStatement(sql);
								ResultSet rSet3=pstmt.executeQuery();
								if(rSet3!=null &&rSet3.next()){				//////////////////////////////////改-------判断插入fail表，避免无限插
									jhxmCount=rSet3.getInt("rowCount");
								}
									pstmt.close();
								if(rSet3!=null){
									rSet3.close();
								}
								if(djdyCount!=jhxmCount){
									//个数不一样，有失败的，重推
									if(rSet2==null||!rSet2.next()){
										saveFailData(xmxxs.get(i), "2");
									}
									if(pstmt2!=null){
										pstmt2.close();
									}
									if(rSet2!=null){
										rSet2.close();
									}
								}
							}							//////////////////////////////////改
								if(pstmt!=null){
									pstmt.close();
								}
							if(rSet!=null){				/////////改   关闭资源
								rSet.close();
							}
							
						} catch (SQLException e) {		/////////改
							b=false;
						}
					}
					try {
						if(jyConnection!=null){
							jyConnection.close();
						}
						
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
			/**
		     * 重新检查核对已登薄数据，刷新bdck中限制类的pushfail表
		     * 一、用登记库中项目表中的已登薄project_id去GXDJK查；二、核对登记库中每个projectID对应的单元数与GXDJK中单元数是否一致
		     * @作者 李堃
		     * @创建时间 2016年5月19日上午10:38:33
		     * @return
		     */
		    public void reFreshXZFailTable2(com.supermap.wisdombusiness.framework.dao.impl.CommonDao baseCommonDao) {
				boolean b=true;
//				String sql=" (DJLX ='800' AND (QLLX='99' OR QLLX='98')) OR (DJLX='100' AND QLLX='23') OR (DJLX='700' AND (QLLX='4' OR QLLX='99'))";
				String sql=" (DJLX ='800' AND (QLLX='99' OR QLLX='98')) or (DJLX='900' AND QLLX='99')";
				List<BDCS_XMXX> xmxxs = baseCommonDao.getDataList(BDCS_XMXX.class, sql);
				if(xmxxs!=null && xmxxs.size()>0){
					setJyConnection(JH_DBHelper.getConnect_gxdjk());
					System.out.println(jyConnection);
					if(jyConnection==null){
						return ;
					}
					List<Map> faiList= null;
					for(int i=0;i<xmxxs.size();i++){
						String project_id= StringHelper.formatObject(xmxxs.get(i).getPROJECT_ID());
						sql="select * from gxjhxm where project_id='"+project_id+"'";///////////////////////////改
						try {
							//用登记中已登薄的去项目表查
							PreparedStatement pstmt=jyConnection.prepareStatement(sql);
							ResultSet rSet= pstmt.executeQuery();
							///////////////////////////改--------判断插入fail表，避免无限插
							if(null!=xmxxs.get(i)){
								String failobj="select * from bdck.pushfail where project_id='"+xmxxs.get(i).getPROJECT_ID()+"'";
								 faiList= baseCommonDao.getDataListByFullSql(failobj);
							}
							//判断是否已经插入过
							if(rSet==null||!rSet.next()){
								//如果没查到，说明推送失败，存入失败表
								if(faiList==null||faiList.size()==0){
									saveFailData(xmxxs.get(i), "2",baseCommonDao);
								}
							}                 ///////////////////////////改--------判断插入fail表，避免无限插
							else {
									pstmt.close();
								//如果查到，进一步检查个数                                        
								long djdyCount=baseCommonDao.getCountByFullSql(" from bdck.bdcs_djdy_gz where xmbh='"+xmxxs.get(i).getId()+"'");
								long jhxmCount=0;
								sql="select count(*) as rowCount from gxdjk.gxjhxm where project_id='"+xmxxs.get(i).getPROJECT_ID()+"'";
								pstmt=jyConnection.prepareStatement(sql);
								ResultSet rSet3=pstmt.executeQuery();
								if(rSet3!=null &&rSet3.next()){				//////////////////////////////////改-------判断插入fail表，避免无限插
									jhxmCount=rSet3.getInt("rowCount");
								}
									pstmt.close();
								if(rSet3!=null){
									rSet3.close();
								}
								if(djdyCount!=jhxmCount){
									//个数不一样，有失败的，重推
									if(faiList==null||faiList.size()==0){
										saveFailData(xmxxs.get(i), "2",baseCommonDao);
									}
								}
							}							//////////////////////////////////改
								if(pstmt!=null){
									pstmt.close();
								}
							if(rSet!=null){				/////////改   关闭资源
								rSet.close();
							}
							
						} catch (SQLException e) {		/////////改
							b=false;
						}
					}
					try {
						if(jyConnection!=null){
							jyConnection.close();
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}

		  //检查维护中间库中没有的单元限制信息
		    public void checkAndPushXZDJ2(com.supermap.wisdombusiness.framework.dao.impl.CommonDao baseCommonDao) {
				//查出bdck中有但gxdjk中没有的限制单元
				com.supermap.wisdombusiness.framework.dao.impl.CommonDaoDJ	CommonDaoDJ=(com.supermap.wisdombusiness.framework.dao.impl.CommonDaoDJ) SuperSpringContext.getContext().getBean("baseCommonDaoDJ");
				String sql="select bdcdyid from gxdjk.xzdj2 where YXBZ='0' OR YXBZ='1'";
				List<Map> djdyxzList =CommonDaoDJ.getDataListByFullSql(sql);
				if(djdyxzList!=null&&djdyxzList.size()>0){
					int count=djdyxzList.size();
					int k=0;
			    	 sql="select ID, BDCDYID, BDCDYLX, BDCQZH, BDCDYH, XMBH, BXZRMC, BXZRZJZL, BXZRZJHM, XZWJHM, XZDW, SDTZRQ, XZQSRQ, XZZZRQ, SLR, SLRYJ, XZLX, YXBZ, LSXZ, XZFW, CREATETIME, MODIFYTIME, DJSJ, DBR, YWH, BZ, ZXDJSJ, ZXDBR, ZXYWH, ZXBZ, ZXYJ, ZXXZWJHM, ZXXZDW from bdck.bdcs_dyxz where (BDCDYLX='031' or BDCDYLX='032') and (YXBZ='0' OR YXBZ='1') AND  ";
					StringBuilder sbBuilder=new StringBuilder();
					for(int i=0;i<count;i++){
						String bdcdyid= djdyxzList.get(i).get("BDCDYID").toString();
						sbBuilder.append("'");
						sbBuilder.append(bdcdyid);
						sbBuilder.append("',");
						if(k==count-1||k==980){
							//每980个执行一次，一次不能超过1000
							k=0;
							String sb=sbBuilder.toString();
					    	 sql +=" BDCDYID not in("+sb.substring(0,sb.length()-1)+") AND ";
					    	 
					    	 //清空一下
					    	 sbBuilder.setLength(0);
						}
						k++;
					}
					sql+=" 1=1";
					List<Map> dyxzList =baseCommonDao.getDataListByFullSql(sql);
						if(dyxzList!=null){
				    		try {
								String jsonStr = JSONArray.fromObject(dyxzList).toString();
								
				    			//调用推送权籍的限制登记数据
								QJpushXZDJToGXDJK(jsonStr,"2");
							} catch (Exception e) {
								e.printStackTrace();
							}
				    	}
				}
		    	else {
		    		//推送所有
		    		sql="select ID, BDCDYID, BDCDYLX, BDCQZH, BDCDYH, XMBH, BXZRMC, BXZRZJZL, BXZRZJHM, XZWJHM, XZDW, SDTZRQ, XZQSRQ, XZZZRQ, SLR, SLRYJ, XZLX, YXBZ, LSXZ, XZFW, CREATETIME, MODIFYTIME, DJSJ, DBR, YWH, BZ, ZXDJSJ, ZXDBR, ZXYWH, ZXBZ, ZXYJ, ZXXZWJHM, ZXXZDW from bdck.bdcs_dyxz where 1>0";
			    	 List<Map> dyxzList =baseCommonDao.getDataListByFullSql(sql);
			    	 if(dyxzList!=null){
				    		try {
								String jsonStr = JSONArray.fromObject(dyxzList).toString();
				    			//调用推送权籍的限制登记数据
								QJpushXZDJToGXDJK(jsonStr,"2");
							} catch (Exception e) {
								e.printStackTrace();
							}
				    	}
				}
		    	
			}
		    public void checkAndPushXZDJ(com.supermap.wisdombusiness.framework.dao.impl.CommonDao baseCommonDao) {
				//查出bdck中有但gxdjk中没有的限制单元
				com.supermap.wisdombusiness.framework.dao.impl.CommonDaoDJ	CommonDaoDJ=(com.supermap.wisdombusiness.framework.dao.impl.CommonDaoDJ) SuperSpringContext.getContext().getBean("baseCommonDaoDJ");
				String sql="select distinct(bdcdyid) from gxdjk.xzdj2 where YXBZ='0' OR YXBZ='1'";
				List<Map> djdyxzList =CommonDaoDJ.getDataListByFullSql(sql);
				//清空临时表
				sql="truncate table bdck.gxdj_xm";
				try {
					baseCommonDao.excuteQueryNoResult(sql);
				} catch (SQLException e1) {
				}
				
				if(djdyxzList!=null&&djdyxzList.size()>0){
					int count=djdyxzList.size();
					int k=0;
					for(int i=0;i<count;i++){
						String bdcdyid= djdyxzList.get(i).get("BDCDYID").toString();
						sql="insert into bdck.gxdj_xm (PROJECT_ID) values('"+bdcdyid+"')";
						try {
							baseCommonDao.excuteQueryNoResult(sql);
						} catch (SQLException e) {
						}
						
					}
				}
			    	 sql="select ID, BDCDYID, BDCDYLX, BDCQZH, BDCDYH, XMBH, BXZRMC, BXZRZJZL, BXZRZJHM, XZWJHM, XZDW, SDTZRQ, XZQSRQ, XZZZRQ, SLR, SLRYJ, XZLX, YXBZ, LSXZ, XZFW, CREATETIME, MODIFYTIME, DJSJ, DBR, YWH, BZ, ZXDJSJ, ZXDBR, ZXYWH, ZXBZ, ZXYJ, ZXXZWJHM, ZXXZDW from bdck.bdcs_dyxz d where not exists(select t.project_id from bdck.gxdj_xm t where d.BDCDYID=t.project_id ) and  (BDCDYLX='031' or BDCDYLX='032') and (YXBZ='0' OR YXBZ='1')  ";

					List<Map> dyxzList =baseCommonDao.getDataListByFullSql(sql);
						if(dyxzList!=null){
				    		try {
								String jsonStr = JSONArray.fromObject(dyxzList).toString();
								
				    			//调用推送权籍的限制登记数据
								QJpushXZDJToGXDJK(jsonStr,"2");
							} catch (Exception e) {
								e.printStackTrace();
							}
				    	}
				
		    	
			}

		    //推送抵押权人
		    private void saveDYAQRTogxdjk(
					com.supermap.wisdombusiness.framework.dao.impl.CommonDao baseCommonDao,
					String djdyidorxmbh, CommonDaoDJ commonDaoDJ) throws Exception {
				String xzqdm = ConfigHelper.getNameByValue("XZQHDM");
					// 获取当前所有权
					String sql = "djdyid='"+ djdyidorxmbh+ "' and (qllx='23')";
					List<BDCS_QL_XZ> qlxzs = baseCommonDao.getDataList(BDCS_QL_XZ.class, sql);
					if (qlxzs != null && qlxzs.size() > 0) {
						BDCS_QL_XZ syq_xz = qlxzs.get(0);
						// 所有权权利人
						sql = "qlid='" + syq_xz.getId() + "'";
						List<BDCS_QLR_XZ> qlrxzs = baseCommonDao.getDataList(
								BDCS_QLR_XZ.class, sql);
						String qlrtablename = Qlr_dj.class.getName();
						String[] fieldsFromQLR = { "QLRID", "BDCDYH", "SXH", "QLRMC",
								"BDCQZH", "QZYSXLH", "ISCZR", "ZJZL", "ZJH", "FZJG",
								"SSHY", "GJ", "HJSZSS", "XB", "DH", "QLID", "BZ", "GYFS",
								"QLBL", "QLRLX", "DZYJ", "GZDW", "YB", "DZ" };
						String[] otherfields2 = { "GXXMBH" ,"BSM"};
						
						for (BDCS_QLR_XZ qlr_xz : qlrxzs) {
							String bsmString= UUID.randomUUID().toString().replace("-","");
							String[] othervalues2 = { getGxxmbh() ,bsmString};
							Bgqqlr_dj qlr=Tools.createPushObj(qlrtablename, fieldsFromQLR, qlr_xz,otherfields2, othervalues2);
							commonDaoDJ.save(qlr);
						}
					}
				
			}

		    //修改业务号为来源权利的业务号
		    private void updateYGDJZXYWH(com.supermap.wisdombusiness.framework.dao.impl.CommonDao baseCommonDao) {
		    	try{
		    		if(xmxx.getDJLX().equals("400")){
		    			System.out.println("更新注销业务号");
			    		String lyqlidString=ql_gz.getLYQLID();
						List<BDCS_QL_LS> qllsList= baseCommonDao.getDataList(BDCS_QL_LS.class, "qlid='"+lyqlidString+"'");
						if(qllsList!=null&&qllsList.size()>0){
				    		System.out.println(lyqlidString);
							ql_gz.setYWH(qllsList.get(0).getYWH());
//							baseCommonDao.update(ql_gz);
//							baseCommonDao.flush();
						}
		    		}
		    	}
		    	catch(Exception ex){
		    	}
				
			}
		    //吉林 续查封权利的业务号修改为被续的查封登记的业务号
		    private void updateXCFDJYWH(com.supermap.wisdombusiness.framework.dao.impl.CommonDao baseCommonDao) {
		    	try{
		    		if(xzqdm.equals("220200")&&xmxx.getDJLX().equals("800")){
		    			System.out.println("吉林更新续查封业务号");
			    		String lyqlidString=ql_gz.getLYQLID();
						List<BDCS_QL_LS> qllsList= baseCommonDao.getDataList(BDCS_QL_LS.class, "qlid='"+lyqlidString+"'");
						if(qllsList!=null&&qllsList.size()>0){
							String projectString="";
				        	if(qllsList.get(0).getXMBH()==null){
				        		List<BDCS_QL_XZ> xz = baseCommonDao.getDataList(BDCS_QL_XZ.class, "lyqlid='"+qllsList.get(0).getId()+"'");
				        		if(xz.size()>0){
				        			projectString=" project_id='" + (xz.get(0)).getYWH() + "'";
				        		}
				        	}else{
				        		projectString="xmbh='" + (qllsList.get(0)).getXMBH() + "'";
				        	}
				           List<BDCS_XMXX> xmxxs = baseCommonDao.getDataList(BDCS_XMXX.class,projectString );
							if(xmxxs.get(0).getDJLX().equals("800")){
								System.out.println(lyqlidString);
								ql_gz.setYWH(qllsList.get(0).getYWH());
							}
						}
		    		}
		    	}
		    	catch(Exception ex){
		    	}
				
			}
		    
		  //赣州市房产特殊需求 抵押查封推送的权利的来源权利ID字段修改为上一手业务的业务号
		    private void updateDYCFLYQLID(com.supermap.wisdombusiness.framework.dao.impl.CommonDao baseCommonDao) {
		    	try{
		    		if(xzqdm.equals("360700")&&(xmxx.getDJLX().equals("800")||xmxx.getQLLX().equals("23"))){
		    			System.out.println("吉林更新续查封业务号");
			    		String lyqlidString=ql_gz.getLYQLID();
			    		if(lyqlidString!=null&&!lyqlidString.equals("")){
			    			List<BDCS_QL_LS> qllsList= baseCommonDao.getDataList(BDCS_QL_LS.class, "qlid='"+lyqlidString+"'");
							if(qllsList!=null&&qllsList.size()>0){
								System.out.println(lyqlidString);
								ql_gz.setLYQLID(qllsList.get(0).getYWH());
							}
			    		}else{
			    			ResultSet qllsResultSet= baseCommonDao.excuteQuery("select YWH FROM BDCK.BDCS_QL_LS WHERE QLLX=4 AND DJDYID = '"+ql_gz.getDJDYID()+"' ORDER BY DJSJ DESC");
			    			while(qllsResultSet.next()){
			    				String YWH=qllsResultSet.getString("YWH");
			    				ql_gz.setLYQLID(YWH);
			    			}
			    		}
		    		}
		    	}
		    	catch(Exception ex){
		    	}
				
			}
		    //根据项目编号获取项目所在行政区代码
		    private String getXZQDMByXMBH(com.supermap.wisdombusiness.framework.dao.impl.CommonDao baseCommonDao,String projectid) {
				String xzqdm="";
				List<Wfi_ProInst> proInsts=baseCommonDao.getDataList(Wfi_ProInst.class, "FILE_NUMBER='"+projectid+"'");
				if(proInsts!=null && proInsts.size()>0){
					xzqdm=proInsts.get(0).getAreaCode();
				}
				return xzqdm;
			}

		    /**
		     * 翻译常量
		     * @作者 likun
		     * @创建时间 2017年3月15日下午3:30:06
		     * @param baseCommonDao
		     * @param clsName 常量类名
		     * @param constValue 常量值
		     * @return 常量中文名
		     */
		    String translateConst(com.supermap.wisdombusiness.framework.dao.impl.CommonDao baseCommonDao,String clsName,String constValue){
		    	String constName="";
		    	try{
		    	List<BDCS_CONSTCLS> clsList=baseCommonDao.getDataList(BDCS_CONSTCLS.class, "CONSTCLSNAME='"+clsName+"'");
		    	if(clsList!=null &&clsList.size()>0){
		    		int clsid=clsList.get(0).getCONSTSLSID();
		    		List<BDCS_CONST> cnstList=baseCommonDao.getDataList(BDCS_CONST.class, "CONSTSLSID='"+clsid+"' and CONSTVALUE='"+constValue+"'");
		    		if(cnstList!=null &&cnstList.size()>0){
		    			constName=cnstList.get(0).getCONSTTRANS();
		    		}
		    	}
		    	}catch(Exception ex){}
		    	return constName;
		    }

		    /**
		     * 翻译户表常量
		     * @作者 likun
		     * @创建时间 2017年3月16日上午11:11:47
		     * @param baseCommonDao
		     * @param h
		     */
		    void translateH(com.supermap.wisdombusiness.framework.dao.impl.CommonDao baseCommonDao,H_dj h){
		    	try{
		    	String fwyt1=h.getFwyt1();
		    	if(fwyt1!=null&&!"".equals(fwyt1)){
		    		fwyt1=translateConst(baseCommonDao,"房屋用途",fwyt1);
		    		if(fwyt1!=null&&!"".equals(fwyt1)){
		    			h.setFwyt1(fwyt1);
		    		}
		    	}
		    	String fwyt2=h.getFwyt2();
		    	if(fwyt2!=null&&!"".equals(fwyt2)){
		    		fwyt2=translateConst(baseCommonDao,"房屋用途",fwyt2);
		    		if(fwyt2!=null&&!"".equals(fwyt2)){
		    			h.setFwyt2(fwyt2);
		    		}
		    	}
		    	String fwyt3=h.getFwyt3();
		    	if(fwyt3!=null&&!"".equals(fwyt3)){
		    		fwyt3=translateConst(baseCommonDao,"房屋用途",fwyt3);
		    		if(fwyt3!=null&&!"".equals(fwyt3)){
		    			h.setFwyt3(fwyt3);
		    		}
		    	}
		    	String ghyt=h.getGhyt();
		    	if(ghyt!=null&&!"".equals(ghyt)){
		    		ghyt=translateConst(baseCommonDao,"房屋用途",ghyt);
		    		if(ghyt!=null&&!"".equals(ghyt)){
		    			h.setFwyt3(ghyt);
		    		}
		    	}
		    	String fwxz=h.getFwxz();
		    	if(fwxz!=null&&!"".equals(fwxz)){
		    		fwxz=translateConst(baseCommonDao,"房屋性质",fwxz);
		    		if(fwxz!=null&&!"".equals(fwxz)){
		    			h.setFwxz(fwxz);
		    		}
		    	}
		    	String fwjg=h.getFwjg();
		    	if(fwjg!=null&&!"".equals(fwjg)){
		    		fwjg=translateConst(baseCommonDao,"房屋结构",fwjg);
		    		if(fwjg!=null&&!"".equals(fwjg)){
		    			h.setFwjg(fwjg);
		    		}
		    	}
		    	String tdyt=h.getTdyt();
		    	if(tdyt!=null&&!"".equals(tdyt)){
		    		tdyt=translateConst(baseCommonDao,"土地用途",tdyt);
		    		if(tdyt!=null&&!"".equals(tdyt)){
		    			h.setTdyt(tdyt);
		    		}
		    	}
		    	String fwcb=h.getFwcb();
		    	if(fwcb!=null&&!"".equals(fwcb)){
		    		fwcb=translateConst(baseCommonDao,"房屋产别",fwcb);
		    		if(fwcb!=null&&!"".equals(fwcb)){
		    			h.setFwcb(fwcb);
		    		}
		    	}
		    	}catch(Exception ex){}
		    }

		    /**
		     * 翻译变更前户表常量
		     * @作者 think
		     * @创建时间 2017年3月16日上午11:12:20
		     * @param baseCommonDao
		     * @param h
		     */
		    void translateBGQH(com.supermap.wisdombusiness.framework.dao.impl.CommonDao baseCommonDao,Bgqh_dj h){
		    	try{
		    	String fwyt1=h.getFwyt1();
		    	if(fwyt1!=null&&!"".equals(fwyt1)){
		    		fwyt1=translateConst(baseCommonDao,"房屋用途",fwyt1);
		    		if(fwyt1!=null&&!"".equals(fwyt1)){
		    			h.setFwyt1(fwyt1);
		    		}
		    	}
		    	String fwyt2=h.getFwyt2();
		    	if(fwyt2!=null&&!"".equals(fwyt2)){
		    		fwyt2=translateConst(baseCommonDao,"房屋用途",fwyt2);
		    		if(fwyt2!=null&&!"".equals(fwyt2)){
		    			h.setFwyt2(fwyt2);
		    		}
		    	}
		    	String fwyt3=h.getFwyt3();
		    	if(fwyt3!=null&&!"".equals(fwyt3)){
		    		fwyt3=translateConst(baseCommonDao,"房屋用途",fwyt3);
		    		if(fwyt3!=null&&!"".equals(fwyt3)){
		    			h.setFwyt3(fwyt3);
		    		}
		    	}
		    	String ghyt=h.getGhyt();
		    	if(ghyt!=null&&!"".equals(ghyt)){
		    		ghyt=translateConst(baseCommonDao,"房屋用途",ghyt);
		    		if(ghyt!=null&&!"".equals(ghyt)){
		    			h.setGhyt(ghyt);
		    		}
		    	}
		    	String fwxz=h.getFwxz();
		    	if(fwxz!=null&&!"".equals(fwxz)){
		    		fwxz=translateConst(baseCommonDao,"房屋性质",fwxz);
		    		if(fwxz!=null&&!"".equals(fwxz)){
		    			h.setFwxz(fwxz);
		    		}
		    	}
		    	String fwjg=h.getFwjg();
		    	if(fwjg!=null&&!"".equals(fwjg)){
		    		fwjg=translateConst(baseCommonDao,"房屋结构",fwjg);
		    		if(fwjg!=null&&!"".equals(fwjg)){
		    			h.setFwjg(fwjg);
		    		}
		    	}
		    	}catch(Exception ex){}
		    }

		    /**
		     * 翻译权利人表常量
		     * @作者 think
		     * @创建时间 2017年3月16日上午11:12:26
		     * @param baseCommonDao
		     * @param qlr
		     */
		    void translateQLR(com.supermap.wisdombusiness.framework.dao.impl.CommonDao baseCommonDao,Qlr_dj qlr){
		    	try{
		    	String zjzl=qlr.getZjzl();
		    	if(zjzl!=null&&!"".equals(zjzl)){
		    		zjzl=translateConst(baseCommonDao,"证件类型",zjzl);
		    		if(zjzl!=null&&!"".equals(zjzl)){
		    			qlr.setZjzl(zjzl);
		    		}
		    	}
		    	String qlrlx=qlr.getQlrlx();
		    	if(qlrlx!=null&&!"".equals(qlrlx)){
		    		qlrlx=translateConst(baseCommonDao,"权利人类型",qlrlx);
		    		if(qlrlx!=null&&!"".equals(qlrlx)){
		    			qlr.setQlrlx(qlrlx);
		    		}
		    	}
		    	String gyfs=qlr.getGyfs();
		    	if(gyfs!=null&&!"".equals(gyfs)){
		    		gyfs=translateConst(baseCommonDao,"共有方式",gyfs);
		    		if(gyfs!=null&&!"".equals(gyfs)){
		    			qlr.setQlrlx(gyfs);
		    		}
		    	}
		    	
		    	}catch(Exception ex){}
		    }
		   /**
		    * 翻译变更前权利人表常量
		    * @作者 think
		    * @创建时间 2017年3月16日上午11:12:57
		    * @param baseCommonDao
		    * @param qlr
		    */
		    void translateBGQQLR(com.supermap.wisdombusiness.framework.dao.impl.CommonDao baseCommonDao,Bgqqlr_dj qlr){
		    	try{
		    	String zjzl=qlr.getZjzl();
		    	if(zjzl!=null&&!"".equals(zjzl)){
		    		zjzl=translateConst(baseCommonDao,"证件类型",zjzl);
		    		if(zjzl!=null&&!"".equals(zjzl)){
		    			qlr.setZjzl(zjzl);
		    		}
		    	}
		    	String qlrlx=qlr.getQlrlx();
		    	if(qlrlx!=null&&!"".equals(qlrlx)){
		    		qlrlx=translateConst(baseCommonDao,"权利人类型",qlrlx);
		    		if(qlrlx!=null&&!"".equals(qlrlx)){
		    			qlr.setQlrlx(qlrlx);
		    		}
		    	}
		    	String gyfs=qlr.getGyfs();
		    	if(gyfs!=null&&!"".equals(gyfs)){
		    		gyfs=translateConst(baseCommonDao,"共有方式",gyfs);
		    		if(gyfs!=null&&!"".equals(gyfs)){
		    			qlr.setQlrlx(gyfs);
		    		}
		    	}
		    	
		    	}catch(Exception ex){}
		    }

		    public int updategxxmbh(String projectid){
		    	int a=0;
				try {
					jyConnection = JH_DBHelper.getConnect_gxdjk();
					fcjyConnection=JH_DBHelper.getConnect_jy();
					StringBuilder gxxmbh=new StringBuilder();
					gxxmbh.append("select distinct a.gxxmbh  from gxdjk.gxjhxm a   where a.casenum is null and a.project_id='").append(projectid).append("'");
					ResultSet res=JH_DBHelper.excuteQuery(jyConnection, gxxmbh.toString());
					int m=0;
					Statement gxdjkpstmt =jyConnection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
					Statement pstmt =fcjyConnection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
					while(res.next()){
						String gxxm=res.getString("GXXMBH").toString();
						System.out.println(m+"当前检查project_id是："+projectid+",当前共享项目编号是："+gxxm);
						m++;
						String relation="select distinct relationid,zjh,qlrmc from gxdjk.h a left join gxdjk.qlr  b on a.gxxmbh=b.gxxmbh where a.gxxmbh='"+gxxm+"'";
						ResultSet relatios=gxdjkpstmt.executeQuery(relation);
						while(relatios.next()){
							if(relatios.getString("RELATIONID")==null){
								continue;
							}
							String relationid=relatios.getString("RELATIONID").toString();
							if(relatios.getString("ZJH")!=null){
								String zjh=relatios.getString("ZJH").toString();
			 					String fcjyxmbh="select casenum from gxjyk.h a left join gxjyk.gxjhxm b on a.gxxmbh=b.gxxmbh left join gxjyk.qlr c  on c.gxxmbh=a.gxxmbh where b.cqsj is null and a.relationid='"+relationid+"' and c.zjh  like '%"+zjh+"%'";
								ResultSet fcrelatios= pstmt.executeQuery(fcjyxmbh);
								fcrelatios.last();
								int rowcount = fcrelatios.getRow();
								fcrelatios.beforeFirst();
								if(rowcount>0){
									while(fcrelatios.next()){
										if(fcrelatios.getString("CASENUM")==null){
											continue;
										}
										String casenum=fcrelatios.getString("CASENUM");
										SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
										try {
											Date cqsj=new Date();
											String jycqsj=sdf.format(cqsj);
											//yyyy/mm/dd HH24:mi:ss
											String updatesqsj="update gxjyk.gxjhxm  a  set  a.cqsj=to_date('"+jycqsj+"','yyyy/mm/dd HH24:mi:ss') where a.cqsj is  null and a.casenum='"+casenum+"'" ;
//											fcjyConnection.createStatement().executeUpdate(updatesqsj);
											JH_DBHelper.excuteUpdate(fcjyConnection, updatesqsj);
										} catch (Exception e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										String updatecasenum="update gxdjk.gxjhxm a set a.casenum='"+casenum+"' where a.project_id='"+projectid+"'";
										JH_DBHelper.excuteUpdate(jyConnection, updatecasenum);
										++a;
									}
								}else{
									if(relatios.getString("QLRMC")!=null){
										String qlrmc=relatios.getString("QLRMC").toString();
										String fcjyxmbh1="select casenum from gxjyk.h a left join gxjyk.gxjhxm b on a.gxxmbh=b.gxxmbh left join gxjyk.qlr c  on c.gxxmbh=a.gxxmbh where b.cqsj is null and a.relationid='"+relationid+"' and c.qlrmc  like '%"+qlrmc+"%'";
										ResultSet fcrelatios1= pstmt.executeQuery(fcjyxmbh1);
										fcrelatios1.last();
										int rowcount1 = fcrelatios1.getRow();
										fcrelatios1.beforeFirst();
										if(rowcount1>0){
											while(fcrelatios1.next()){
												if(fcrelatios1.getString("CASENUM")==null){
													continue;
												}
												String casenum=fcrelatios1.getString("CASENUM");
												SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
												try {
													Date cqsj=new Date();
													String jycqsj=sdf.format(cqsj);
													//yyyy/mm/dd HH24:mi:ss
													String updatesqsj="update gxjyk.gxjhxm  a  set  a.cqsj=to_date('"+jycqsj+"','yyyy/mm/dd HH24:mi:ss') where a.cqsj is  null and a.casenum='"+casenum+"'" ;
													JH_DBHelper.excuteUpdate(fcjyConnection, updatesqsj);
												} catch (Exception e) {
													// TODO Auto-generated catch block
													e.printStackTrace();
												}
												String updatecasenum="update gxdjk.gxjhxm a set a.casenum='"+casenum+"' where a.project_id='"+projectid+"'";
												JH_DBHelper.excuteUpdate(jyConnection, updatecasenum);
												++a;
											}
										}
										fcrelatios1.close();
									}
								}
								fcrelatios.close();
							}else{
								if(relatios.getString("QLRMC")!=null){
									String qlrmc=relatios.getString("QLRMC").toString();
									String fcjyxmbh1="select casenum from gxjyk.h a left join gxjyk.gxjhxm b on a.gxxmbh=b.gxxmbh left join gxjyk.qlr c  on c.gxxmbh=a.gxxmbh where b.cqsj is null and a.relationid='"+relationid+"' and c.qlrmc  like '%"+qlrmc+"%'";
									ResultSet fcrelatios1= pstmt.executeQuery(fcjyxmbh1);
									fcrelatios1.last();
									int rowcount1 = fcrelatios1.getRow();
									fcrelatios1.beforeFirst();
									if(rowcount1>0){
										while(fcrelatios1.next()){
											if(fcrelatios1.getString("CASENUM")==null){
												continue;
											}
											String casenum=fcrelatios1.getString("CASENUM");
											SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
											try {
												Date cqsj=new Date();
												String jycqsj=sdf.format(cqsj);
												//yyyy/mm/dd HH24:mi:ss
												String updatesqsj="update gxjyk.gxjhxm  a  set  a.cqsj=to_date('"+jycqsj+"','yyyy/mm/dd HH24:mi:ss') where a.cqsj is  null and a.casenum='"+casenum+"'" ;
												JH_DBHelper.excuteUpdate(fcjyConnection, updatesqsj);
											} catch (Exception e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											}
											String updatecasenum="update gxdjk.gxjhxm a set a.casenum='"+casenum+"' where a.project_id='"+projectid+"'";
											JH_DBHelper.excuteUpdate(jyConnection, updatecasenum);
											++a;
										}
									}
									fcrelatios1.close();
								}
							}
						}
						relatios.close();
					}
					res.close();
					if(pstmt!=null){
						pstmt.close();
					}
					if(gxdjkpstmt!=null){
						gxdjkpstmt.close();
					}
					fcjyConnection.close();
					fcjyConnection=null;
					jyConnection.close();
					jyConnection=null;
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}finally{
//					try {
//						jyConnection.close();
//						jyConnection=null;
//					} catch (SQLException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
				}
				System.out.println("更新gxdjk中gxjhxm的casenum数量:"+a);
				return a;
			}

			/** 
			* 供权籍系统限制登记时调用，按照共享逻辑，推送gxjhxm、h、xzdj表
			* @author  buxiaobo
			* @date 创建时间：2017年5月24日 上午11:38:48 
			* @version 1.0 
			 * @param dyxzs 
			* @parameter  
			* @since  
			* @return  
			 * @throws Exception 
			*/
			public void QJpushXZDJToGXDJK(String inputLine,String xzly) throws Exception {
//				try{
					if(inputLine==null ||"".equals(inputLine)){
						return;
					}
					List<BDCS_DYXZ> dyxzs=new ArrayList<BDCS_DYXZ>();
					JSONArray jsonArray=JSONArray.fromObject(inputLine);
					if(xzly!=null&&xzly.equals("1")){
						jsonArray=SetTime(jsonArray);
					}else if(xzly!=null&&xzly.equals("2")){
						jsonArray=TimestampSetTime(jsonArray);
					}
					if(jsonArray!=null&&jsonArray.size()>0){
						for(int i=0;i<jsonArray.size();i++){
							//直接转
							JSONObject jsonObject=(JSONObject)jsonArray.get(i);
							BDCS_DYXZ dyxz=(BDCS_DYXZ)jsonObject.toBean(jsonObject, BDCS_DYXZ.class);
							dyxzs.add(dyxz);
						}
					}
					com.supermap.wisdombusiness.framework.dao.impl.CommonDaoDJ	CommonDaoDJ=(com.supermap.wisdombusiness.framework.dao.impl.CommonDaoDJ) SuperSpringContext.getContext().getBean("baseCommonDaoDJ");
					com.supermap.wisdombusiness.framework.dao.impl.CommonDao	CommonDaoBDC=(com.supermap.wisdombusiness.framework.dao.impl.CommonDao) SuperSpringContext.getContext().getBean("baseCommonDao");

					// 开始推送，
					Session session=CommonDaoDJ.getCurrentSession();
					//开启事务
					Transaction ts=session.beginTransaction();
					
					Boolean bXMXXSaved = false;
					// bljd 办理进度， 0初始推送；1审批；2登簿；3缮证；4发证；5归档；
					setBljd("2");
					// 登记小类
					setDjxl(djxl);
					// qlsdfs 权利设定方式，1设立，2转移，3变更，4注销
					setQlsdfs("1");
					if(dyxzs!=null&&dyxzs.size()>0){
						for(int i=0;i<dyxzs.size();i++){
							BDCS_DYXZ dyxz=dyxzs.get(i);
							// 获取户model，户里包含houseid
							String bdcdyh = dyxz.getBDCDYH();
							String bdcdyid = dyxz.getBDCDYID();
							setBdcdyly(dyxz.getBDCDYLX());
							if (bdcdyly.equals(BDCDYLX.H.Value)) {
								// 变更后
								List<BDCS_H_XZ> h_XZs = CommonDaoBDC.getDataList(
										BDCS_H_XZ.class, "bdcdyid='" + bdcdyid
												+ "'");
								// 变更前户
								List<BDCS_H_LS> h_LSs = CommonDaoBDC.getDataList(
										BDCS_H_LS.class, "bdcdyid='" + bdcdyid
												+ "'");
								if (h_XZs != null && h_XZs.size() > 0) {
									setH_xz(h_XZs.get(0));
								}
								if (h_LSs != null && h_LSs.size() > 0) {
									setH_ls_bgq(h_LSs.get(0));
								}
							} else if (bdcdyly.equals(BDCDYLX.YCH.Value)) {
								// 预测户
								List<BDCS_H_XZY> h_XZs = CommonDaoBDC.getDataList(
										BDCS_H_XZY.class, "bdcdyid='" + bdcdyid + "'");
								// 变更前户
								List<BDCS_H_LSY> h_LSs = CommonDaoBDC.getDataList(
										BDCS_H_LSY.class, "bdcdyid='" + bdcdyid + "'");
								if (h_XZs != null && h_XZs.size() > 0) {
									setH_XZY(h_XZs.get(0));
								}
								if (h_LSs != null && h_LSs.size() > 0) {
									setH_LSY(h_LSs.get(0));
								}
							} else {
								return;
							}
							try {
								if (jyConnection == null || jyConnection.isClosed()) {
									jyConnection = JH_DBHelper.getConnect_gxdjk();
								}
							} catch (SQLException e1) {

							}
							gxxmbh = UUID.randomUUID().toString().replace("-","");
							List<BDCS_DYXZ> xzdjlist = CommonDaoBDC
									.getDataList(BDCS_DYXZ.class,  "bdcdyid='" + bdcdyid + "'");
							if (xzdjlist != null && xzdjlist.size() > 0) {
								QJsaveXZDJ("XZDJ", xzdjlist,CommonDaoBDC,CommonDaoDJ);
								QJsaveXZDJ2("XZDJ2", xzdjlist,CommonDaoBDC,CommonDaoDJ);
							}
							saveH(CommonDaoDJ);
							if (!bXMXXSaved) {
								// 保存项目信息
								QJXZsaveGXXMXX(bdcdyh, "1", bljd, qlsdfs, "", "",false,"99","900",CommonDaoBDC,CommonDaoDJ);
							}
//							//修改SFDB字段
//							dyxz.setSFTS("1");
//							CommonDaoBDC.update(dyxz);
//							CommonDaoBDC.flush();
						}
					}
					
					ts.commit();
					CommonDaoDJ.getSession().close();
					System.out.println("权籍系统限制登记调用协同接口推送完成，本次共推送限制记录："+dyxzs.size()+"条！");
//				}catch(Exception e){
//					System.out.println("出错了！！！！"+e.toString());
//				}
				
				
			}

			/** 
			 * java序列化时间戳格式时间处理方法
			* @author  buxiaobo
			* @date 创建时间：2017年5月25日 上午11:11:54 
			* @version 1.0 
			* @parameter  
			* @since  
			* @return  
			 * @throws ParseException 
			*/
			@SuppressWarnings({ "unchecked", "rawtypes" })
			private JSONArray TimestampSetTime(JSONArray jsonArray) throws ParseException {
				JSONArray jsonArray2=new JSONArray();
				if(jsonArray!=null&&jsonArray.size()>0){
					Iterator<Object> it = jsonArray.iterator();
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		            while (it.hasNext()) {
		                JSONObject ob = (JSONObject) it.next();
		                if(ob.getString("ID")!=null){
		                	ob.remove("ID");		                	
		                }
		                if(!"null".equals(ob.getString("SDTZRQ"))&&ob.getString("SDTZRQ").length()>0){
		                	Map CREATETIMEmap=(Map)ob.get("SDTZRQ");
		                	String string=stampToDate(CREATETIMEmap.get("time").toString());
		                	Date date=sdf.parse(string);
							ob.put("SDTZRQ", date);
		                }
		                if(!"null".equals(ob.getString("XZQSRQ"))&&ob.getString("XZQSRQ").length()>0){
		                	Map CREATETIMEmap=(Map)ob.get("XZQSRQ");
		                	String string=stampToDate(CREATETIMEmap.get("time").toString());
		                	Date date=sdf.parse(string);
							ob.put("XZQSRQ", date);
		                }
		                if(!"null".equals(ob.getString("XZZZRQ"))&&ob.getString("XZZZRQ").length()>0){
		                	Map CREATETIMEmap=(Map)ob.get("XZZZRQ");
		                	String string=stampToDate(CREATETIMEmap.get("time").toString());
		                	Date date=sdf.parse(string);
							ob.put("XZZZRQ", date);
		                }
		                if(!"null".equals(ob.getString("CREATETIME"))&&ob.getString("CREATETIME").length()>0){
		                	Map CREATETIMEmap=(Map)ob.get("CREATETIME");
		                	String string=stampToDate(CREATETIMEmap.get("time").toString());
		                	Date date=sdf.parse(string);
							ob.put("CREATETIME", date);
		                }
		                if(!"null".equals(ob.getString("MODIFYTIME"))&&ob.getString("MODIFYTIME").length()>0){
		                	Map CREATETIMEmap=(Map)ob.get("MODIFYTIME");
		                	String string=stampToDate(CREATETIMEmap.get("time").toString());
		                	Date date=sdf.parse(string);
							ob.put("MODIFYTIME", date);
		                }
		                if(!"null".equals(ob.getString("DJSJ"))&&ob.getString("DJSJ").length()>0){
		                	Map CREATETIMEmap=(Map)ob.get("DJSJ");
		                	String string=stampToDate(CREATETIMEmap.get("time").toString());
		                	Date date=sdf.parse(string);
							ob.put("DJSJ", date);
		                }
		                if(!"null".equals(ob.getString("ZXDJSJ"))&&ob.getString("ZXDJSJ").length()>0){
		                	Map CREATETIMEmap=(Map)ob.get("ZXDJSJ");
		                	String string=stampToDate(CREATETIMEmap.get("time").toString());
		                	Date date=sdf.parse(string);
							ob.put("ZXDJSJ", date);
		                }
		                jsonArray2.add(ob);
		            }
				}
				return jsonArray2;
			}
			/** 
			 * 权籍C#序列化时间格式处理方法
			* @author  buxiaobo
			* @date 创建时间：2017年5月25日 上午11:11:54 
			* @version 1.0 
			* @parameter  
			* @since  
			* @return  
			 * @throws ParseException 
			*/
			@SuppressWarnings("unchecked")
			private JSONArray SetTime(JSONArray jsonArray) throws ParseException {
				JSONArray jsonArray2=new JSONArray();
				if(jsonArray!=null&&jsonArray.size()>0){
					Iterator<Object> it = jsonArray.iterator();
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		            while (it.hasNext()) {
		                JSONObject ob = (JSONObject) it.next();
		                if(ob.getString("ID")!=null){
		                	ob.remove("ID");		                	
		                }
		                if(ob.getString("DatasourceAlia")!=null){
		                	ob.remove("DatasourceAlia");		                	
		                }
		                if(ob.getString("Datasource")!=null){
		                	ob.remove("Datasource");		                	
		                }
		                if(ob.getString("SDTZRQ")!=null&&ob.getString("SDTZRQ").length()>0&&ob.getString("SDTZRQ").contains("T")){
		                	if(ob.getString("SDTZRQ").contains("T")&&ob.getString("SDTZRQ").contains("0001")){
		                		ob.put("SDTZRQ", null);
		                	}else{
		                		String string=ob.get("SDTZRQ").toString().replace('T', ' ');
			                	Date date=sdf.parse(string);
								ob.put("SDTZRQ", date);
		                	}
		                }
		                if(ob.getString("XZQSRQ")!=null&&ob.getString("XZQSRQ").length()>0&&ob.getString("XZQSRQ").contains("T")){
		                	if(ob.getString("XZQSRQ").contains("T")&&ob.getString("XZQSRQ").contains("0001")){
		                		ob.put("XZQSRQ", null);
		                	}else{
		                		String string=ob.get("XZQSRQ").toString().replace('T', ' ');
			                	Date date=sdf.parse(string);
								ob.put("XZQSRQ", date);
		                	}
		                }
		                if(ob.getString("XZZZRQ")!=null&&ob.getString("XZZZRQ").length()>0&&ob.getString("XZZZRQ").contains("T")){
		                	if(ob.getString("XZZZRQ").contains("T")&&ob.getString("XZZZRQ").contains("0001")){
		                		ob.put("XZZZRQ", null);
		                	}else{
		                		String string=ob.get("XZZZRQ").toString().replace('T', ' ');
			                	Date date=sdf.parse(string);
								ob.put("XZZZRQ", date);
		                	}
		                }
		                if(ob.getString("CREATETIME")!=null&&ob.getString("CREATETIME").length()>0&&ob.getString("CREATETIME").contains("T")){
		                	if(ob.getString("CREATETIME").contains("T")&&ob.getString("CREATETIME").contains("0001")){
		                		ob.put("CREATETIME", null);
		                	}else{
		                		String string=ob.get("XZZZRQ").toString().replace('T', ' ');
			                	Date date=sdf.parse(string);
								ob.put("XZZZRQ", date);
		                	}
		                }
		                if(ob.getString("MODIFYTIME")!=null&&ob.getString("MODIFYTIME").length()>0&&ob.getString("MODIFYTIME").contains("T")){
		                	if(ob.getString("MODIFYTIME").contains("T")&&ob.getString("MODIFYTIME").contains("0001")){
		                		ob.put("MODIFYTIME", null);
		                	}else{
		                		String string=ob.get("MODIFYTIME").toString().replace('T', ' ');
			                	Date date=sdf.parse(string);
								ob.put("MODIFYTIME", date);
		                	}
		                }
		                if(ob.getString("DJSJ")!=null&&ob.getString("DJSJ").length()>0&&ob.getString("DJSJ").contains("T")){
		                	if(ob.getString("DJSJ").contains("T")&&ob.getString("DJSJ").contains("0001")){
		                		ob.put("DJSJ", null);
		                	}else{
		                		String string=ob.get("DJSJ").toString().replace('T', ' ');
			                	Date date=sdf.parse(string);
								ob.put("DJSJ", date);
		                	}
		                }
		                if(ob.getString("ZXDJSJ")!=null&&ob.getString("ZXDJSJ").length()>0&&ob.getString("ZXDJSJ").contains("T")){
		                	if(ob.getString("ZXDJSJ").contains("T")&&ob.getString("ZXDJSJ").contains("0001")){
		                		ob.put("ZXDJSJ", null);
		                	}else{
		                		String string=ob.get("ZXDJSJ").toString().replace('T', ' ');
			                	Date date=sdf.parse(string);
								ob.put("ZXDJSJ", date);
		                	}
		                }
		                jsonArray2.add(ob);
		            }
				}
				return jsonArray2;
			}
			 /* 
		     * 将时间戳转换为时间
		     */
		    public static String stampToDate(String s){
		        String res;
		        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		        long lt = new Long(s);
		        Date date = new Date(lt);
		        res = simpleDateFormat.format(date);
		        return res;
		    }
			
		    /**
			 * 保存限制登记权利
			 * @throws Exception 
			 * **/
			private void QJsaveXZDJ(String qltablename, List<BDCS_DYXZ> xzdjlist,com.supermap.wisdombusiness.framework.dao.impl.CommonDao baseCommonDao,com.supermap.wisdombusiness.framework.dao.impl.CommonDaoDJ CommonDaoDJ) throws Exception {
				BDCS_DYXZ xzdj=xzdjlist.get(0);
				String bsmString= UUID.randomUUID().toString().replace("-","");
				String XZQSRQ = xzdj.getXZQSRQ()!=null?(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(xzdj.getXZQSRQ()):null;
				String XZZZRQ = xzdj.getXZZZRQ()!=null?(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(xzdj.getXZZZRQ()):null;
				String DJSJ = xzdj.getDJSJ()!=null?(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(xzdj.getDJSJ()):null;
				String ZXDJSJ = xzdj.getZXDJSJ()!=null?(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(xzdj.getZXDJSJ()):null;
				String TSSJ=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
				String tablename=Xzdj_dj.class.getName();
				String[] otherfields = { "GXXMBH", "BDCDYID","BDCDYH","YWH","CFLX","CFJG","CFWH","CFFW","JFJG","JFWH","CFQSSJ","CFJSSJ","DBR","DJSJ","ZXDJSJ","JFYWH","BSM","TSSJ"};
				String[] othervalues = { gxxmbh,xzdj.getBDCDYID(),xzdj.getBDCDYH(),"",xzdj.getXZLX(),xzdj.getXZDW(), xzdj.getXZWJHM(),xzdj.getXZFW(), xzdj.getZXXZDW(),xzdj.getZXXZWJHM(),XZQSRQ,XZZZRQ,xzdj.getDBR(),DJSJ,ZXDJSJ,xzdj.getZXXZWJHM(),bsmString,TSSJ};
				//一个项目推送要一个事务提交，在工具类里增加通用方法直接返回待保存对象 2017年1月18日 17:17:39 卜晓波
				try {
					Xzdj_dj Xzdj=Tools.QJGxxmxxcreatePushObj(tablename, otherfields, othervalues);
					CommonDaoDJ.save(Xzdj);
				} catch (SQLException e) {
					allSuccess=false;
					FailCause=e.toString();
					logger.error("出错：" + e.toString());
				}
			}
			
			/**
			 * 权籍保存限制登记2权利
			 * @throws Exception 
			 * **/
			private void QJsaveXZDJ2(String qltablename, List<BDCS_DYXZ> xzdjlist,com.supermap.wisdombusiness.framework.dao.impl.CommonDao baseCommonDao,com.supermap.wisdombusiness.framework.dao.impl.CommonDaoDJ CommonDaoDJ) throws Exception {
				BDCS_DYXZ xzdj=xzdjlist.get(0);
				String bsmString= UUID.randomUUID().toString().replace("-","");
				String XZQSRQ = xzdj.getXZQSRQ()!=null?(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(xzdj.getXZQSRQ()):null;
				String XZZZRQ = xzdj.getXZZZRQ()!=null?(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(xzdj.getXZZZRQ()):null;
				String DJSJ = xzdj.getDJSJ()!=null?(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(xzdj.getDJSJ()):null;
				String ZXDJSJ = xzdj.getZXDJSJ()!=null?(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(xzdj.getZXDJSJ()):null;
				String TSSJ=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
				String tablename=Xzdj2_dj.class.getName();
				String[] otherfields = { "GXXMBH", "BSM","BDCDYID","BDCDYLX","BDCQZH","BDCDYH","BXZRMC","BXZRZJZL","BXZRZJHM","XZWJHM","XZDW","XZQSRQ","XZZZRQ","XZLX","YXBZ","XZFW","DJSJ","ZXDJSJ","YWH","ZXYWH","ZXBZ","ZXYJ","ZXXZWJHM","ZXXZDW","TSSJ"};
				String[] othervalues = { gxxmbh,bsmString,xzdj.getBDCDYID(),xzdj.getBDCDYLX(),xzdj.getBDCQZH(),xzdj.getBDCDYH(), xzdj.getBXZRMC(),xzdj.getBXZRZJZL(), xzdj.getBXZRZJHM(),xzdj.getXZWJHM(),xzdj.getXZDW(),XZQSRQ,XZZZRQ,xzdj.getXZLX(),xzdj.getYXBZ(),xzdj.getXZFW(),DJSJ,ZXDJSJ,xzdj.getYWH(),xzdj.getZXYWH(),xzdj.getZXBZ(),xzdj.getZXYJ(),xzdj.getZXXZWJHM(),xzdj.getZXXZDW(),TSSJ};
				//一个项目推送要一个事务提交，在工具类里增加通用方法直接返回待保存对象 2017年1月18日 17:17:39 卜晓波
				try {
					Xzdj2_dj Xzdj2=Tools.QJGxxmxxcreatePushObj(tablename, otherfields, othervalues);
					CommonDaoDJ.save(Xzdj2);
				} catch (SQLException e) {
					allSuccess=false;
					FailCause=e.toString();
					logger.error("出错：" + e.toString());
				}
			}
			
			/**
			 * 权籍保存共享交换项目
			 * @throws Exception 
			 * **/
			protected void QJXZsaveGXXMXX(String bdcdyh, String zt, String bljd,
					String qlsdfs, String casenum, String sqr, boolean isCombinnation, String nowqllx, String nowdjdl,com.supermap.wisdombusiness.framework.dao.impl.CommonDao baseCommonDao,com.supermap.wisdombusiness.framework.dao.impl.CommonDaoDJ CommonDaoDJ) throws Exception {
				String xmmc = "权籍系统限制登记项目";
				String prjId = "";
				String qllx = "99";
				String slry = "权籍系统管理员";
				Date slsj = new Date();
				String bsmString= UUID.randomUUID().toString().replace("-","");
				String[] vls = new String[13];
				vls[0] = gxxmbh;
				vls[1] = xmmc;
				vls[2] = nowdjdl;
				vls[3] = nowqllx;
				vls[4] = bdcdyh;
				vls[5] = slry;
				vls[6] = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(slsj);
				vls[7] = zt;
				vls[8] = bljd;
				vls[9] = qlsdfs;
				//如果casenum为空就给赋gxxmbh
				String ajh=casenum;
				vls[10] = ajh;
				vls[11] = sqr;
				vls[12] = "";
				Date dt = new Date();
				String nowString = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(dt);
				String tablename=Gxjhxm_dj.class.getName();
				String[] otherfields = { "GXXMBH", "PROJECT_ID","XMMC","DJDL","DJXL","QLLX","BDCDYH","SLRY","SLSJ","ZT","BLJD","QLSDFS","CASENUM","SQR","YWLSH","TSSJ","BSM" ,"XZQDM","XZQMC"};
				String[] othervalues = { vls[0],prjId,vls[1],vls[2],djxl,vls[3], vls[4],vls[5], vls[6],vls[7],vls[8],vls[9],vls[10],vls[11],vls[12],nowString,bsmString,getXzqdm(),getXzqmc()};
				//一个项目推送要一个事务提交，在工具类里增加通用方法直接返回待保存对象 2017年1月18日 17:17:39 卜晓波
				try {
					Gxjhxm_dj gxjhxm=Tools.QJGxxmxxcreatePushObj(tablename, otherfields, othervalues);
					SaveLog_dj(gxjhxm,CommonDaoDJ);
					CommonDaoDJ.save(gxjhxm);
				} catch (SQLException e) {
					allSuccess=false;
					FailCause=e.toString();
					logger.error("出错：" + e.toString());
				}
			}
			
			/**
			 * 不物理删除数据，做标记，不修改推送时间
			 * @作者 李堃
			 * @创建时间 2016年9月20日下午3:56:42
			 * @param projectid
			 */
			public void deleteEx2(String projectid, CommonDaoDJ commonDaoDJ2) {
				String sql = "select GXXMBH from gxdjk.GXJHXM where project_id='"+ projectid + "'";
				try {
					List<Gxjhxm_dj> gxjhxm_djs=commonDaoDJ2.getDataList(Gxjhxm_dj.class, "project_id='"+ projectid + "'");
					String gxxmbh = "'1'";
					if(gxjhxm_djs!=null&& gxjhxm_djs.size()>0){
						for(int i = 0; i < gxjhxm_djs.size(); i++){
							gxxmbh += ",'" + gxjhxm_djs.get(i).getGxxmbh() + "'";
						}
					}
					//吉林房产要求删件时更新推送时间和置空抽取时间 2016年12月1日 14:58:37 卜晓波
					Date dt = new Date();
					String nowString = "to_date('"+ (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(dt)+ "','yyyy-MM-dd HH24:mi:ss')";
					// 删除项目表数据
					String sql1 = "update  GXDJK.GXJHXM set ISDELETE=1,ZT=2 where GXXMBH in(" + gxxmbh + ")";
					commonDaoDJ2.excuteQueryNoResult(sql1);
				} catch (SQLException e) {
					logger.error("出错：" + e.getMessage());
					logger.error("出错：" + sql);
				}
			}
				@SuppressWarnings("rawtypes")
	public void delete(String projectid) {
		 String sql = "select GXXMBH from gxdjk.GXJHXM where project_id='"+ projectid + "'";
		   try{
		       if ((jyConnection == null) || (jyConnection.isClosed())) {
		         jyConnection = JH_DBHelper.getConnect_gxdjk();
		      }
		       PreparedStatement pstmt = this.jyConnection.prepareStatement(sql);
		       ResultSet rSet = pstmt.executeQuery();
		       String hql = "GXXMBH IN ('1'";
		       if (rSet != null){
		         List gxxmbhList = resultSetToList(rSet);
		         int temp = 0;
		         if (gxxmbhList.size() > 0){
		           for (int i = 0; i < gxxmbhList.size(); i++) {
		             if ((i > 0) && (temp == 999)){
		               hql = hql + ") OR GXXMBH IN ('1'";
		               temp = 0;
		             }
		             hql = hql + ",'" + gxxmbhList.get(i) + "'";
		             temp++;
		           }
		           System.out.println(hql);
		         }
		      }
		       	hql = hql + ")";
		       	pstmt.close();
		       	rSet.close();
		       jyConnection.close();
		       jyConnection = null;
		       jyConnection = JH_DBHelper.getConnect_gxdjk();
		        
		       	sql = "delete from GXDJK.GXJHXM  where " + hql;
		       	JH_DBHelper.excuteNoQuery(jyConnection, sql);
		       	
		       	
		    	sql = "delete from GXDJK.H where " + hql;
		       	JH_DBHelper.excuteNoQuery(this.jyConnection, sql);
	
		       	
		       	sql = "delete from GXDJK.BGQH  where " + hql;
		       	JH_DBHelper.excuteNoQuery(this.jyConnection, sql);
		       
		       	
		       	sql = "delete from GXDJK.FDCQ  where  " + hql;
		       	JH_DBHelper.excuteNoQuery(this.jyConnection, sql);
		      
		       	
		       	sql = "delete from GXDJK.BGQFDCQ  where  " + hql;
		       	JH_DBHelper.excuteNoQuery(this.jyConnection, sql);
		       
		       	sql = "delete from GXDJK.DYAQ  where  " + hql;
		       	JH_DBHelper.excuteNoQuery(this.jyConnection, sql);
		       	
		       
			    sql = "delete from GXDJK.BGQDYAQ  where  " + hql;
			    JH_DBHelper.excuteNoQuery(this.jyConnection, sql);
//			 
//			       
			    sql = "delete from GXDJK.YGDJ  where  " + hql;
			    JH_DBHelper.excuteNoQuery(this.jyConnection, sql);
//			   
			    sql = "delete from GXDJK.YYDJ  where  " + hql;
			    JH_DBHelper.excuteNoQuery(this.jyConnection, sql);
//			   
			    sql = "delete from GXDJK.CFDJ  where  " + hql;
			    JH_DBHelper.excuteNoQuery(this.jyConnection, sql);
//			    
		        sql = "delete from GXDJK.XZDJ  where  " + hql;
			    JH_DBHelper.excuteNoQuery(this.jyConnection, sql);
//			    
			    sql = "delete from GXDJK.FDCQZXDJ  where  " + hql;
			    JH_DBHelper.excuteNoQuery(this.jyConnection, sql);
//			   
			    sql = "delete from GXDJK.QLR  where  " + hql;
			    JH_DBHelper.excuteNoQuery(this.jyConnection, sql);
//			   
			    sql = "delete from GXDJK.BGQQLR  where  " + hql;
			    JH_DBHelper.excuteNoQuery(this.jyConnection, sql);
			}catch (SQLException e){
			   this.logger.error("出错：" + e.getMessage());
		       this.logger.error("出错：" + sql);
		    }finally {
		    	try {
		    		if(jyConnection!=null){
					jyConnection.close();
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
	}
				public void pushFJToGXDJK(CommonDao baseCommonDao,BDCS_XMXX xmxx,Session session,CommonDaoDJ CommonDaoDJ ,Connection conn) {
					// 读取本地化配置
//					 Connection conn = null;
					String tsfj = ConfigHelper.getNameByValue("TSFJ");
					System.out.println("本地化配置"+tsfj);
					Map<String, String> read =read(baseCommonDao);
					String push = read.get("push");
					String tssx = read.get("tssx");
					String tsfjs="";
					if("2".equals(tssx)&&!"0".equals(push)){
						tsfjs="2";
					}
					if("2".equals(tsfjs)){
						List<BDCS_QL_XZ> dataList = baseCommonDao.getDataList(BDCS_QL_XZ.class, "ywh='"+xmxx.getPROJECT_ID()+"'");
						if(dataList.get(0).getCASENUM()!=null){
							setCasenum(dataList.get(0).getCASENUM());
						}
						List<Gxjhxm_dj> xmbhs = CommonDaoDJ.getDataList(Gxjhxm_dj.class, "project_id='"+xmxx.getPROJECT_ID()+"'");
						if(xmbhs.get(0).getGxxmbh()!=null){
							setGxxmbh(xmbhs.get(0).getGxxmbh());
						}
					}
					if (tsfj.equals("1")) {
						// 推送二进制内容
						System.out.println("数据库存储配置");
						try {
//							conn = SessionFactoryUtils.getDataSource(session.getSessionFactory()).getConnection();   
							// 推送PROMATER表内容
							String tablename = "GXDJK.PROMATER";
							String[] fieldsFromPROMATER = { "MATERILINST_ID","MATERIAL_ID", "MATERIAL_INDEX", "MATERIAL_NAME",
									"MATERIAL_NEED", "PROINST_ID" };
							String[] otherfields = { "GXXMBH", "CASENUM" };//
							String casenum3 = getCasenum();
							String gxxmbh2 = getGxxmbh();
							if(casenum3==null||casenum3.equals("")){
								casenum3=null;
							}
							String[] othervalues = {gxxmbh2,casenum3 != null ? casenum3 : xmxx.getPROJECT_ID() };
							String sql = "";
							List<Wfi_ProMater> proMaters = baseCommonDao.getDataList(Wfi_ProMater.class, "PROINST_ID='"
											+ getProinstID(baseCommonDao,xmxx) + "'");
							if (proMaters != null && proMaters.size() > 0) {
								// 推送前先删除一下已有附件
								deleteFJ(proMaters,conn);
								for (int i = 0; i < proMaters.size(); i++) {
									tablename = "GXDJK.PROMATER";
									sql = Tools.createInsertSQL(tablename,fieldsFromPROMATER, proMaters.get(i),otherfields, othervalues);
									try {
										
										JH_DBHelper.excuteUpdate(conn, sql);
										
										// 推送MATERDATA表内容
										tablename = "GXDJK.MATERDATA";
										String[] fieldsFromMATERDATA = { "MATERIALDATA_ID","MATERILINST_ID", "UPLOAD_NAME","UPLOAD_ID", "UPLOAD_DATE", "FILE_NAME","FILE_PATH", "FILE_INDEX", "PATH" }; // PATH
										String[] otherfields2 = { "CASENUM" };// "GXXMBH",
										String[] othervalues2 = { casenum3 };// getGxxmbh(),
										List<Wfi_MaterData> materDatas = baseCommonDao.getDataList(Wfi_MaterData.class,"MATERILINST_ID='"	+ proMaters.get(i).getMaterilinst_Id()+ "'");
										if (materDatas != null && materDatas.size() > 0) {
											for (int k = 0; k < materDatas.size(); k++) {
												sql = Tools.createInsertSQL(tablename,fieldsFromMATERDATA,materDatas.get(k), otherfields2,othervalues2);
												try {
													JH_DBHelper.excuteUpdate(conn,sql);
													saveBinary(materDatas.get(k).getPath(),materDatas.get(k).getFile_Path(),materDatas.get(k).getMaterialdata_Id(),session,conn);
												} catch (Exception ex) {
													logger.error("出错：" + ex.getMessage());
													session.getTransaction().rollback();
												}
											}
										}
									} catch (Exception ex) {
										logger.error("附件推送出错：" + ex.getMessage());
										session.getTransaction().rollback();
									}
								}
							}
						} catch (Exception e) {
							logger.error("附件推送出错：" + e.getMessage());
							session.getTransaction().rollback();
							try{
						        if (conn != null) {
						          conn.close();
						        }
						      }catch (SQLException e1){
						        this.logger.error("Connection：" + e1.getMessage());
						      }
						}finally{
						      try{
						        if (conn != null) {
						          conn.close();
						        }
						      }catch (SQLException e){
						        this.logger.error("Connection：" + e.getMessage());
						      }
						  }
					}
					if (tsfj.equals("2")) {
						System.out.println("服务器存储配置");
						try {
						// 推送PROMATER表内容
//							conn = SessionFactoryUtils.getDataSource(session.getSessionFactory()).getConnection();   
							String tablename = "GXDJK.PROMATER";
							String[] fieldsFromPROMATER = { "MATERILINST_ID","MATERIAL_ID", "MATERIAL_INDEX", "MATERIAL_NAME","MATERIAL_NEED", "PROINST_ID" };
							String[] otherfields = { "GXXMBH", "CASENUM" };//
							String casenum3 = getCasenum();
							if(casenum3.equals("")){
								casenum3=null;
							}
							String[] othervalues = {getGxxmbh(),casenum3 != null ? casenum3 : xmxx.getPROJECT_ID() };
							String sql = "";
							List<Wfi_ProMater> proMaters = baseCommonDao.getDataList(Wfi_ProMater.class, "PROINST_ID='"+ getProinstID(baseCommonDao,xmxx) + "'");
							if (proMaters != null && proMaters.size() > 0) {
								// 推送前先删除一下已有附件
								deleteFJ(proMaters,conn);
								for (int i = 0; i < proMaters.size(); i++) {
									tablename = "GXDJK.PROMATER";
									sql = Tools.createInsertSQL(tablename,fieldsFromPROMATER, proMaters.get(i),otherfields, othervalues);
									try {
										JH_DBHelper.excuteUpdate(conn, sql);
										// 推送MATERDATA表内容
										tablename = "GXDJK.MATERDATA";
										String[] fieldsFromMATERDATA = {"MATERIALDATA_ID", "MATERILINST_ID","UPLOAD_NAME", "UPLOAD_ID",
													"UPLOAD_DATE", "FILE_NAME","FILE_PATH", "FILE_INDEX", "PATH" }; 
										String[] otherfields2 = { "CASENUM" };// "GXXMBH",
										String[] othervalues2 = { casenum3 };// getGxxmbh(),
										List<Wfi_MaterData> materDatas = baseCommonDao.getDataList(Wfi_MaterData.class,
															"MATERILINST_ID='"+ proMaters.get(i).getMaterilinst_Id()+ "'");
										if (materDatas != null && materDatas.size() > 0) {
											for (int k = 0; k < materDatas.size(); k++) {
													// PATH// FILE_NAME// 都需要set一下   如果有问题调整接口或者path为file_path
												// 调用接口 返回值2调用文件系统接口 ccwz 判断4PATH FILE_NAME 都需要set一下
												//需要传递file（文 件实体），uploadPath （上传路径），uploadFileName （文件名称）
												//返回值   ccwz path filename
												String uplodpaths = materDatas.get(k).getPath();
												System.out.println("接口路径"+uplodpaths);
												String uploadFileName = materDatas.get(k).getFile_Name();
												System.out.println("文件名字"+uploadFileName);
												//文件实体(获取有问题)materialdata_id
//												http://localhost:8080/realestate/app/frame/wfipromater/imagedownload/0b90904373ba4866925944defdae42c5
												//附件实体
												String tsfjst = ConfigHelper.getNameByValue("TSFJST");
												System.out.println(tsfjst+"附件实体");
//												//文件实体字节流
												//读取本地化配置路径
												String url_tsfj = ConfigHelper.getNameByValue("URL_TSFJ");
												System.out.println("接口实体存放"+url_tsfj);
												//本地文件夹地址TSFJFJ
												String TSFJSTS = ConfigHelper.getNameByValue("TSFJSTS");
												Map<String, String> params =new HashMap<String, String>();
												params.put("uploadPath ", uplodpaths);
												params.put("uploadFileName", uploadFileName);
												System.out.println("服务器路径"+TSFJSTS);
			 									File download = HttpRequest.download(tsfjst+"/"+materDatas.get(k).getMaterialdata_Id(), materDatas.get(k).getFile_Name(),TSFJSTS); 
												Map<String, File> files =new HashMap<String, File>();
												files.put("file", download);
												//返回值有问题
												String tsfjPost = HttpRequest.TsfjPost(url_tsfj, null, params, files);
												StringBuilder json = new StringBuilder();
												json = json.append(tsfjPost);
												JSONArray jsonArray = JSONArray.fromObject(json.toString());
												//JSONObject flag = JSONObject.fromObject(json.toString());
												// 1、执行服务器存储
												String path="";
												String filename="";
												for (Object object : jsonArray) {
													JSONObject flag = JSONObject.fromObject(object);
													 path=flag.get("filepath").toString();
													 filename=flag.get("filename").toString();
												}
												//文件实体存放在硬盘上 获取文件系统盘符
												//重新从set路径和文件名字 path 为文件在硬盘上的路径
//												 
												 String TSFJFJ = ConfigHelper.getNameByValue("TSFJFJ");
												 path=TSFJFJ+path+filename;
												 System.out.println("硬盘存储路径"+path);
												materDatas.get(k).setPath(path);
												materDatas.get(k).setFile_Name(filename);
												sql = Tools.createInsertSQL(tablename,fieldsFromMATERDATA,materDatas.get(k) ,otherfields2, othervalues2);
												try {
													JH_DBHelper.excuteUpdate(conn, sql);
													System.out.println("路径存储成功"+tablename);
												} catch (Exception ex) {
													logger.error("出错："+ ex.getMessage());
												}finally {
													 File file = new File(TSFJSTS);
													    // 判断目录或文件是否存在  
													    if (!file.exists()) {  // 不存在返回 false  
													    } else {  
													        // 判断是否为文件  
													        if (file.isFile()) {  // 为文件时调用删除文件方法  
													           deleteFile(TSFJSTS);  
													        } else {  // 为目录时调用删除目录方法  
													            deleteDirectory(TSFJSTS);  
													        }  
													 }  
												}
											}
										}
									} catch (Exception ex) {
											logger.error("附件推送出错：" + ex.getMessage());
											session.getTransaction().rollback();
									}
								}
							}
						} catch (Exception e) {
							logger.error("附件推送出错：" + e.getMessage());
							session.getTransaction().rollback();
							try
						      {
						        if (conn != null) {
						          conn.close();
						        }
						      }
						      catch (SQLException e1)
						      {
						        this.logger.error("Connection：" + e1.getMessage());
						      }
						}finally{
						      try{
						        if (conn != null) {
						          conn.close();
						        }
						      }catch (SQLException e){
						        this.logger.error("Connection：" + e.getMessage());
						      }
						  }
					}
			   }
				
				public boolean deleteFile(String sPath) {  
				    boolean flag = false;  
				    File file = new File(sPath);  
				    // 路径为文件且不为空则进行删除  
				    if (file.isFile() && file.exists()) {  
				        file.delete();  
				        flag = true;  
				    }  
				    return flag;  
				}  
			 
				public boolean deleteDirectory(String sPath) {  
				    //如果sPath不以文件分隔符结尾，自动添加文件分隔符  
				    if (!sPath.endsWith(File.separator)) {  
				        sPath = sPath + File.separator;  
				    }  
				    File dirFile = new File(sPath);  
				    //如果dir对应的文件不存在，或者不是一个目录，则退出  
				    if (!dirFile.exists() || !dirFile.isDirectory()) {  
				        return false;  
				    }  
				      boolean  flag = true;  
				    //删除文件夹下的所有文件(包括子目录)  
				    File[] files = dirFile.listFiles();  
				    for (int i = 0; i < files.length; i++) {  
				        //删除子文件  
				        if (files[i].isFile()) {  
				            flag = deleteFile(files[i].getAbsolutePath());  
				            if (!flag) break;  
				        } //删除子目录  
				        else {  
				            flag = deleteDirectory(files[i].getAbsolutePath());  
				            if (!flag) break;  
				        }  
				    }  
				    if (!flag) return false;  
				    //删除当前目录  
				    if (dirFile.delete()) {  
				        return true;  
				    } else {  
				        return false;  
				    }  
				}  
				  public void deleteFJ(List<Wfi_ProMater> promaterList,Connection conn){
					     if ((promaterList != null) && (promaterList.size() > 0)){
					       String sql = "";
					       for (int i = 0; i < promaterList.size(); i++){
					         String materilinst_Id = ((Wfi_ProMater)promaterList.get(i)).getMaterilinst_Id();
					         sql = "select MATERIALDATA_ID from GXDJK.MATERDATA WHERE MATERILINST_ID='" + materilinst_Id + "'";
					         try{
					           PreparedStatement pstmt = conn.prepareStatement(sql);
					           ResultSet materdataRs = pstmt.executeQuery();
					           if (materdataRs != null){
					             while (materdataRs.next()){
					               String materdaid = materdataRs.getString(1);
					               sql = "delete from GXDJK.FJDATA WHERE MATERIALDATA_ID='" + materdaid + "'";
					               JH_DBHelper.excuteNoQuery(conn, sql);
					             }
					             pstmt.close();
					             materdataRs.close();
					           }
					           sql = "delete from GXDJK.MATERDATA WHERE MATERILINST_ID='" + materilinst_Id + "'";
					           JH_DBHelper.excuteNoQuery(conn, sql);
					           sql = "delete from GXDJK.PROMATER WHERE MATERILINST_ID='" + materilinst_Id + "'";
					           JH_DBHelper.excuteNoQuery(conn, sql);
					         }catch (SQLException localSQLException) {}
					       }
					     }
					   }

				   private int saveBinary(String path,String filename,String MATERIALDATA_ID,Session session, Connection conn){
					   int k=0;
					   try{
					   String fullPath=path+filename;
					// 根据文件存储配置选择对应方法存储图片：1、本地存储；2、服务存储
					   String materialtype = ConfigHelper.getNameByValue("materialtype");
					   if (materialtype == null || materialtype.equals("")|| materialtype.trim().equals("1")) {
								path=ConfigHelper.getNameByValue("material")+path ;
								fullPath=path+"\\"+filename;
							}else if (materialtype.trim().equals("3")) {
								try {
									//名字和路径
									fullPath="C:\\temp.jpg";
									byte[] buf= Http.getFile(path, filename);
									DeleteFile(fullPath);
									ByteToImage(buf,fullPath);
									
								} catch (ClientProtocolException e) {
								} catch (IOException e) {
								} catch (Exception e) {
									logger.error("出错：" + e.getMessage());
								}
							}  
							File  file  =  new  File(fullPath);  
							if(file.exists()){
								int  fileLength  =(int)  file.length();    
								InputStream  fin  =  new  FileInputStream(file);    
								//插入附件二进制数据
								String sql="insert  into  GXDJK.FJDATA (MATERIALDATA_ID,CONTENT)  values('"+MATERIALDATA_ID+"',"+"?)";
								PreparedStatement  pstmt  =  conn.prepareStatement(sql);       
								pstmt.setBinaryStream  (1, fin,  fileLength);    
								k=pstmt.executeUpdate();  
							}
							 
					   }catch(Exception ex){
						   logger.error("出错：" + ex.getMessage());
						   session.getTransaction().rollback();
					   }
					return k;
				}
				   
					private String getProinstID(com.supermap.wisdombusiness.framework.dao.impl.CommonDao baseCommonDao,BDCS_XMXX xmxx) {
						String proinstidString="";
						List<Wfi_ProInst> proInsts=baseCommonDao.getDataList(Wfi_ProInst.class, "file_number='"+xmxx.getPROJECT_ID()+"'");
						if(proInsts!=null&&proInsts.size()>0){
							proinstidString=proInsts.get(0).getProinst_Id();
						}
						return proinstidString;

					}

}
    
