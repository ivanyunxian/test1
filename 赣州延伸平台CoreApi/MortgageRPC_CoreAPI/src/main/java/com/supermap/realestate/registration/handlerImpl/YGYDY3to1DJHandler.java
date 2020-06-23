package com.supermap.realestate.registration.handlerImpl;


import com.supermap.realestate.registration.ViewClass.ProjectInfo;
import com.supermap.realestate.registration.ViewClass.UnitStatus;
import com.supermap.realestate.registration.ViewClass.UnitTree;
import com.supermap.realestate.registration.dataExchange.*;
import com.supermap.realestate.registration.dataExchange.dyq.QLFQLDYAQ;
import com.supermap.realestate.registration.dataExchange.utils.packageXml;
import com.supermap.realestate.registration.dataExchange.ygdj.QLFQLYGDJ;
import com.supermap.realestate.registration.handler.DJHandler;
import com.supermap.realestate.registration.model.*;
import com.supermap.realestate.registration.model.interfaces.*;
import com.supermap.realestate.registration.model.xmlExportmodel.MessageExport;
import com.supermap.realestate.registration.service.impl.ProjectServiceImpl;
import com.supermap.realestate.registration.tools.RightsHolderTools;
import com.supermap.realestate.registration.tools.RightsTools;
import com.supermap.realestate.registration.tools.UnitTools;
import com.supermap.realestate.registration.util.ConstValue;
import com.supermap.realestate.registration.util.ProjectHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.realestate.registration.util.YwLogUtil;
import com.supermap.wisdombusiness.core.SuperSpringContext;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import com.supermap.wisdombusiness.web.ResultMessage;
import org.springframework.util.StringUtils;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import java.io.File;
import java.text.MessageFormat;
import java.util.*;

public class YGYDY3to1DJHandler  extends DJHandlerBase implements DJHandler {

	  public YGYDY3to1DJHandler(ProjectInfo info)
	  {
	    super(info);
	  }
	  @Override
	  public boolean addBDCDY(String bdcdyid)
	  {
	    boolean bsuccess = false;
	    CommonDao dao = getCommonDao();
	    ResultMessage msg = new ResultMessage();
	    RealUnit _srcUnit = UnitTools.loadUnit(getBdcdylx(), ConstValue.DJDYLY.XZ, bdcdyid);
	    if (_srcUnit == null)
	    {
	      msg.setSuccess("false");
	      msg.setMsg("找不到单元");
	      return false;
	    }
	    if (_srcUnit != null)
	    {
	      BDCS_DJDY_GZ djdy = super.createDJDYfromXZ(bdcdyid);
	      if (djdy == null)
	      {
	        String djdyid = getPrimaryKey();
	        djdy = new BDCS_DJDY_GZ();
	        djdy.setDJDYID(djdyid);
	        djdy.setBDCDYID(_srcUnit.getId());
	        djdy.setBDCDYH(_srcUnit.getBDCDYH());
	        djdy.setLY(ConstValue.DJDYLY.XZ.Value);
	        djdy.setBDCDYLX(getBdcdylx().Value);
	        djdy.setXMBH(getXMBH());
	      }
	      if (djdy != null)
	      {
	        BDCS_QL_GZ ql = super.createQL(djdy, _srcUnit);
	        
	        BDCS_FSQL_GZ fsql = super.createFSQL(djdy.getDJDYID());
	        
	        BDCS_QL_GZ dyql = super.createQL(djdy, _srcUnit);
	        

	        BDCS_FSQL_GZ dyfsql = super.createFSQL(djdy.getDJDYID());
	        

	        ql.setFSQLID(fsql.getId());
	        ql.setQLLX(getQllx().Value);
	        
	        fsql.setYGDJZL(ConstValue.YGDJLX.YSSPFMMYGDJ.Value.toString());
	        fsql.setQLID(ql.getId());
	        

	        dyql.setFSQLID(dyfsql.getId());
	        dyql.setQLLX(ConstValue.QLLX.DIYQ.Value);
	        dyql.setCZFS(ConstValue.CZFS.GTCZ.Value);
	        dyfsql.setQLID(dyql.getId());
	        dyfsql.setDYWLX(getDYBDCLXfromBDCDYLX(getBdcdylx()));
	        dyfsql.setDYBDCLX(getDYBDCLXfromBDCDYLX(getBdcdylx()));
	        dyfsql.setYGDJZL(ConstValue.YGDJLX.YSSPFDYQYGDJ.Value.toString());
	        


	        BDCS_QL_GZ dyql2 = super.createQL(djdy, _srcUnit);
	        
	        BDCS_FSQL_GZ dyql2fsql = super.createFSQL(djdy.getDJDYID());
	        
	        dyql2.setFSQLID(dyql2fsql.getId());
	        dyql2.setQLLX(ConstValue.QLLX.DIYQ.Value);
	        dyql2.setCZFS(ConstValue.CZFS.GTCZ.Value);
	        dyql2fsql.setQLID(dyql2.getId());
	        dyql2fsql.setDYWLX(getDYBDCLXfromBDCDYLX(getBdcdylx()));
	        dyql2fsql.setDYBDCLX(getDYBDCLXfromBDCDYLX(getBdcdylx()));
	        dyql2fsql.setYGDJZL(ConstValue.YGDJLX.YSSPFDYQYGDJ.Value.toString());
	        _srcUnit.setDJZT(ConstValue.DJZT.DJZ.Value);
	        
	        String fj = "";
	        BDCS_XMXX xmxx = (BDCS_XMXX)getCommonDao().get(BDCS_XMXX.class, getXMBH());
	        String workflowcode = ProjectHelper.getWorkflowCodeByProjectID(xmxx.getPROJECT_ID());
	        String sql = " WORKFLOWCODE='" + workflowcode + "'";
	        List<WFD_MAPPING> mappings = dao.getDataList(WFD_MAPPING.class, sql);
	        if ((mappings != null) && (mappings.size() > 0))
	        {
	          WFD_MAPPING maping = (WFD_MAPPING)mappings.get(0);
	          if ("1".equals(maping.getISINITATATUS()))
	          {
	            fj = ql.getFJ();
	            fj = getStatus(fj, ql.getDJDYID(), bdcdyid, getBdcdylx().Value);
	            ql.setFJ(fj);
	          }
	        }
	        dao.update(_srcUnit);
	        dao.save(djdy);
	        dao.save(ql);
	        dao.save(fsql);
	        dao.save(dyql);
	        dao.save(dyfsql);
	        dao.save(dyql2);
	        dao.save(dyql2fsql);
	      }
	      dao.flush();
	      bsuccess = true;
	    }
	    return bsuccess;
	  }
	 
	  
	  @Override
	  public boolean writeDJB()
	  {
	    if (super.isCForCFING()) {
	      return false;
	    }
	    String strSqlXMBH = ProjectHelper.GetXMBHCondition(getXMBH());
	    CommonDao dao = getCommonDao();
	    List<BDCS_DJDY_GZ> djdys = dao.getDataList(BDCS_DJDY_GZ.class, strSqlXMBH);
	    for (BDCS_DJDY_GZ bdcs_djdy_gz : djdys)
	    {
	      String djdyid = bdcs_djdy_gz.getDJDYID();
	      
	      super.CopyGZQLToXZAndLS(djdyid);
	      super.CopyGZQLRToXZAndLSNotOnQLLX(djdyid);
	      super.CopyGZQDZRToXZAndLS(djdyid);
	      super.CopyGZZSToXZAndLS(djdyid);
	      List<BDCS_DJDY_LS> djdys_xz = dao.getDataList(BDCS_DJDY_LS.class, "DJDYID='" + djdyid + "'");
	      if ((djdys_xz == null) || (djdys_xz.size() <= 0)) {
	        super.CopyGZDJDYToXZAndLS(bdcs_djdy_gz.getId());
	      }
	      List<YC_SC_H_XZ> lst = dao.getDataList(YC_SC_H_XZ.class, "YCBDCDYID ='" + bdcs_djdy_gz.getBDCDYID() + "'");
	      for (YC_SC_H_XZ yc_sc_h_xz : lst) {
	        super.CopyYXZHToAndLS(yc_sc_h_xz.getYCBDCDYID());
	      }
	      RebuildDYBG("", "", bdcs_djdy_gz.getBDCDYID(), bdcs_djdy_gz.getDJDYID(), bdcs_djdy_gz.getCreateTime(), bdcs_djdy_gz.getModifyTime());
	      ConstValue.BDCDYLX dylx = ProjectHelper.GetBDCDYLX(getXMBH());
	      
	      SetDYDYZT(bdcs_djdy_gz.getBDCDYID(), dylx, "0");
	    }
	    SetSFDB();
	    dao.flush();
	    super.alterCachedXMXX();
	    return true;
	  }
	 
	  @Override
	  public boolean removeBDCDY(String bdcdyid)
	  {
	    boolean bsuccess = false;
	    
	    CommonDao baseCommonDao = getCommonDao();
	    BDCS_DJDY_GZ djdy = super.removeDJDY(bdcdyid);
	    if (djdy != null)
	    {
	      String djdyid = djdy.getDJDYID();
	      

	      String _hqlCondition = MessageFormat.format("XMBH=''{0}'' AND DJDYID=''{1}''", new Object[] { getXMBH(), djdyid });
	      RightsTools.deleteRightsAllByCondition(ConstValue.DJDYLY.GZ, _hqlCondition);
	    }
	    baseCommonDao.flush();
	    bsuccess = true;
	    return bsuccess;
	  }
	 
	  
	  @Override
	  public List<UnitTree> getUnitTree()
	  {
	    String xmbhFilter = ProjectHelper.GetXMBHCondition(getXMBH());
	    CommonDao dao = getCommonDao();
	    List<UnitTree> list = new ArrayList();
	    List<BDCS_DJDY_GZ> djdys = dao.getDataList(BDCS_DJDY_GZ.class, xmbhFilter);
	    if ((djdys != null) && (djdys.size() > 0)) {
	      for (int i = 0; i < djdys.size(); i++)
	      {
	        BDCS_DJDY_GZ djdy = (BDCS_DJDY_GZ)djdys.get(i);
	        UnitTree tree = new UnitTree();
	        StringBuilder BuilderQL = new StringBuilder();
	        BuilderQL.append(xmbhFilter).append(" AND DJDYID='").append(djdy.getDJDYID()).append("'");
	        List<BDCS_QL_GZ> qls = dao.getDataList(BDCS_QL_GZ.class, BuilderQL.toString());
	        if (qls != null) {
	          for (int iql = 0; iql < qls.size(); iql++)
	          {
	            BDCS_QL_GZ ql = (BDCS_QL_GZ)qls.get(iql);
	            if (ql.getQLLX().equals(ConstValue.QLLX.DIYQ.Value)) {
	              tree.setDIYQQlid(ql.getId());
	            } else {
	              tree.setQlid(ql.getId());
	            }
	          }
	        }
	        tree.setId(djdy.getBDCDYID());
	        tree.setBdcdyid(djdy.getBDCDYID());
	        tree.setBdcdylx(djdy.getBDCDYLX());
	        tree.setDjdyid(djdy.getDJDYID());
	        String ly = ConstValue.DJDYLY.initFrom(djdy.getLY()) == null ? "gz" : StringUtils.isEmpty(djdy.getLY()) ? "gz" : ConstValue.DJDYLY.initFrom(djdy.getLY()).Name;
	        tree.setLy(ly);
	        String zl = getZL(tree, djdy.getDJDYID(), djdy.getBDCDYLX(), djdy.getBDCDYID(), ly);
	        tree.setText(zl);
	        if ((djdy.getBDCDYLX().equals(ConstValue.BDCDYLX.H.Value)) && 
	          (!StringHelper.isEmpty(djdy.getLY())))
	        {
	          ConstValue.DJDYLY ely = ConstValue.DJDYLY.initFrom(djdy.getLY());
	          House house = (House)UnitTools.loadUnit(ConstValue.BDCDYLX.H, ely, djdy.getBDCDYID());
	          if (house != null)
	          {
	            String fh = house.getFH();
	            tree.setFh(fh);
	            tree.setText("坐落:" + zl + "|房号:" + (fh == null ? "" : fh));
	          }
	        }
	        if ((djdy.getBDCDYLX().equals(ConstValue.BDCDYLX.YCH.Value)) && 
	          (!StringHelper.isEmpty(djdy.getLY())))
	        {
	          ConstValue.DJDYLY ely = ConstValue.DJDYLY.initFrom(djdy.getLY());
	          House house = (House)UnitTools.loadUnit(ConstValue.BDCDYLX.YCH, ely, djdy.getBDCDYID());
	          if (house != null)
	          {
	            String fh = house.getFH();
	            tree.setFh(fh);
	            tree.setMj(house.getMJ());
	            if (StringHelper.isEmpty(house.getFTTDMJ())) {
	              tree.setFttdmj(0.0D);
	            } else {
	              tree.setFttdmj(house.getFTTDMJ().doubleValue());
	            }
	            tree.setText("坐落:" + zl + "|房号:" + (fh == null ? "" : fh));
	          }
	        }
	        tree.setZl(zl);
	        list.add(tree);
	      }
	    }
	    return list;
	  }
	 
	
	  private String getZL(UnitTree tree, String djdyid, String bdcdylx, String bdcdyid, String djdyly)
	  {
	    String zl = "";
	    CommonDao dao = getCommonDao();
	    ConstValue.BDCDYLX dylx = ConstValue.BDCDYLX.initFrom(bdcdylx);
	    if (djdyly.equals(ConstValue.DJDYLY.GZ.Name))
	    {
	      if (dylx.equals(ConstValue.BDCDYLX.SHYQZD))
	      {
	        BDCS_SHYQZD_GZ shyqzd = (BDCS_SHYQZD_GZ)dao.get(BDCS_SHYQZD_GZ.class, bdcdyid);
	        zl = shyqzd == null ? "" : shyqzd.getZL();
	      }
	      else if (dylx.equals(ConstValue.BDCDYLX.H))
	      {
	        BDCS_H_GZ h = (BDCS_H_GZ)dao.get(BDCS_H_GZ.class, bdcdyid);
	        tree.setCid(h.getCID());
	        tree.setZdbdcdyid(h.getZDBDCDYID());
	        tree.setZrzbdcdyid(h.getZRZBDCDYID());
	        tree.setLjzbdcdyid(h.getLJZID());
	        zl = h == null ? "" : h.getZL();
	      }
	      else if (dylx.equals(ConstValue.BDCDYLX.ZRZ))
	      {
	        BDCS_ZRZ_GZ zrz = (BDCS_ZRZ_GZ)dao.get(BDCS_ZRZ_GZ.class, bdcdyid);
	        tree.setZdbdcdyid(zrz.getZDBDCDYID());
	        zl = zrz == null ? "" : zrz.getZL();
	      }
	      else if (dylx.equals(ConstValue.BDCDYLX.SYQZD))
	      {
	        BDCS_SYQZD_GZ syqzd = (BDCS_SYQZD_GZ)dao.get(BDCS_SYQZD_GZ.class, bdcdyid);
	        zl = syqzd == null ? "" : syqzd.getZL();
	      }
	      else if (dylx.equals(ConstValue.BDCDYLX.HY))
	      {
	        BDCS_ZH_GZ zh = (BDCS_ZH_GZ)dao.get(BDCS_ZH_GZ.class, bdcdyid);
	        zl = zh == null ? "" : zh.getZL();
	      }
	      else if (dylx.equals(ConstValue.BDCDYLX.LD))
	      {
	        BDCS_SLLM_GZ ld = (BDCS_SLLM_GZ)dao.get(BDCS_SLLM_GZ.class, bdcdyid);
	        tree.setZdbdcdyid(ld.getZDBDCDYID());
	        zl = ld == null ? "" : ld.getZL();
	      }
	    }
	    else
	    {
	      if (dylx.equals(ConstValue.BDCDYLX.SHYQZD))
	      {
	        BDCS_SHYQZD_XZ shyqzd = (BDCS_SHYQZD_XZ)dao.get(BDCS_SHYQZD_XZ.class, bdcdyid);
	        zl = shyqzd == null ? "" : shyqzd.getZL();
	      }
	      else if (dylx.equals(ConstValue.BDCDYLX.H))
	      {
	        BDCS_H_XZ shyqzd = (BDCS_H_XZ)dao.get(BDCS_H_XZ.class, bdcdyid);
	        if (shyqzd != null)
	        {
	          zl = shyqzd == null ? "" : shyqzd.getZL();
	          
	          tree.setCid(shyqzd.getCID());
	          tree.setZdbdcdyid(shyqzd.getZDBDCDYID());
	          tree.setZrzbdcdyid(shyqzd.getZRZBDCDYID());
	          tree.setLjzbdcdyid(shyqzd.getLJZID());
	        }
	      }
	      else if (dylx.equals(ConstValue.BDCDYLX.YCH))
	      {
	        BDCS_H_XZY bdcs_h_xzy = (BDCS_H_XZY)dao.get(BDCS_H_XZY.class, bdcdyid);
	        if (bdcs_h_xzy != null)
	        {
	          zl = bdcs_h_xzy.getZL();
	          tree.setId(bdcs_h_xzy.getCID());
	          tree.setZdbdcdyid(bdcs_h_xzy.getZDBDCDYID());
	          tree.setZrzbdcdyid(bdcs_h_xzy.getZRZBDCDYID());
	          tree.setLjzbdcdyid(bdcs_h_xzy.getLJZID());
	        }
	      }
	      else if (dylx.equals(ConstValue.BDCDYLX.ZRZ))
	      {
	        BDCS_ZRZ_XZ zrz = (BDCS_ZRZ_XZ)dao.get(BDCS_ZRZ_XZ.class, bdcdyid);
	        tree.setZdbdcdyid(zrz.getZDBDCDYID());
	        zl = zrz == null ? "" : zrz.getZL();
	      }
	      String qllxarray = " ('1','2','3','4','5','6','7','8','9','10','11','12','13','14','15','16','17','18')";
	      String hqlCondition = MessageFormat.format(" DJDYID IN (SELECT DJDYID FROM BDCS_DJDY_XZ WHERE BDCDYID=''{0}'') AND QLLX IN {1} ORDER BY DJSJ", new Object[] { bdcdyid, qllxarray });
	      List<BDCS_QL_XZ> listxzql = dao.getDataList(BDCS_QL_XZ.class, hqlCondition);
	      if ((listxzql != null) && (listxzql.size() > 0))
	      {
	        BDCS_QL_XZ ql = (BDCS_QL_XZ)listxzql.get(0);
	        tree.setOldqlid(ql.getId());
	      }
	    }
	    return zl;
	  }
	 
	  @Override
	  public void addQLRbySQRArray(String qlid, Object[] sqrids)
	  {
	    super.addQLRbySQRs(qlid, sqrids);
	    CommonDao dao = getCommonDao();
	    BDCS_QL_GZ ql = (BDCS_QL_GZ)dao.get(BDCS_QL_GZ.class, qlid);
	    if (!ql.getQLLX().equals(ConstValue.QLLX.DIYQ.Value))
	    {
	      String xmbhFilter = ProjectHelper.GetXMBHCondition(getXMBH());
	      StringBuilder BuilderQLR = new StringBuilder();
	      BuilderQLR.append(xmbhFilter).append(" AND QLID='").append(qlid).append("' ORDER BY SXH");
	      List<BDCS_QLR_GZ> qlrs = dao.getDataList(BDCS_QLR_GZ.class, BuilderQLR.toString());
	      if ((qlrs != null) && (qlrs.size() > 0))
	      {
	        StringBuilder builderDYR = new StringBuilder();
	        int indexdyr = 0;
	        for (int iqlr = 0; iqlr < qlrs.size(); iqlr++)
	        {
	          if (indexdyr == 0) {
	            builderDYR.append(((BDCS_QLR_GZ)qlrs.get(iqlr)).getQLRMC());
	          } else {
	            builderDYR.append(",").append(((BDCS_QLR_GZ)qlrs.get(iqlr)).getQLRMC());
	          }
	          indexdyr++;
	        }
	        StringBuilder BuilderFSQL = new StringBuilder();
	        BuilderFSQL.append(xmbhFilter).append(" AND DJDYID='").append(ql.getDJDYID()).append("'");
	        List<BDCS_FSQL_GZ> fsqls = dao.getDataList(BDCS_FSQL_GZ.class, BuilderFSQL.toString());
	        if ((fsqls != null) && (fsqls.size() > 0)) {
	          for (int ifsql = 0; ifsql < fsqls.size(); ifsql++)
	          {
	            BDCS_FSQL_GZ fsql = (BDCS_FSQL_GZ)fsqls.get(ifsql);
	            if (!fsql.getQLID().equals(qlid))
	            {
	              fsql.setDYR(builderDYR.toString());
	              dao.update(fsql);
	            }
	          }
	        }
	      }
	      dao.flush();
	    }
	  }
	  
	  @Override
	  public void removeQLR(String qlid, String qlrid)
	  {
	    CommonDao dao = getCommonDao();
	    BDCS_QL_GZ ql = (BDCS_QL_GZ)dao.get(BDCS_QL_GZ.class, qlid);
	    super.removeqlr(qlrid, qlid);
	    if (!ql.getQLLX().equals(ConstValue.QLLX.DIYQ.Value))
	    {
	      List<BDCS_QLR_GZ> qlrs = dao.getDataList(BDCS_QLR_GZ.class, "QLID ='" + qlid + "' ORDER BY SXH");
	      StringBuilder builderDYR = new StringBuilder();
	      int indexdyr = 0;
	      for (int iqlr = 0; iqlr < qlrs.size(); iqlr++) {
	        if (!qlrid.equals(((BDCS_QLR_GZ)qlrs.get(iqlr)).getId()))
	        {
	          if (indexdyr == 0) {
	            builderDYR.append(((BDCS_QLR_GZ)qlrs.get(iqlr)).getQLRMC());
	          } else {
	            builderDYR.append(",").append(((BDCS_QLR_GZ)qlrs.get(iqlr)).getQLRMC());
	          }
	          indexdyr++;
	        }
	      }
	      List<BDCS_FSQL_GZ> fsqlList = dao.getDataList(BDCS_FSQL_GZ.class, " XMBH='" + getXMBH() + "' and DJDYID='" + ql.getDJDYID() + "'");
	      if ((fsqlList != null) && (fsqlList.size() > 0)) {
	        for (int ifsql = 0; ifsql < fsqlList.size(); ifsql++)
	        {
	          BDCS_FSQL_GZ fsql = (BDCS_FSQL_GZ)fsqlList.get(ifsql);
	          if (!fsql.getQLID().equals(qlid))
	          {
	            fsql.setDYR(builderDYR.toString());
	            dao.update(fsql);
	          }
	        }
	      }
	    }
	    dao.flush();
	  }
	  
	  @Override
	  public String getError()
	  {
	    return super.getErrMessage();
	  }
	  
	  @Override
	  public Map<String, String> exportXML(String path, String actinstID)
	  {
	    Map<String, String> names = new HashMap();
	    try
	    {
	      CommonDao dao = super.getCommonDao();
	      String xmbhHql = ProjectHelper.GetXMBHCondition(super.getXMBH());
	      List<BDCS_DJDY_GZ> djdys = dao.getDataList(BDCS_DJDY_GZ.class, xmbhHql);
	      if ((djdys != null) && (djdys.size() > 0))
	      {
	        Marshaller mashaller = JAXBContext.newInstance(new Class[] { Message.class }).createMarshaller();
	        String ywh = packageXml.GetYWLSHByYWH(getProject_id());
	        BDCS_XMXX xmxx = (BDCS_XMXX)dao.get(BDCS_XMXX.class, super.getXMBH());
	        String result = "";
	        String result2 = "";
	        String combineResult = "";
	        for (int i = 0; i < djdys.size(); i++)
	        {
	          String bizMsgId = "";
	          BDCS_QL_GZ ql1 = null;
	          BDCS_QL_GZ ql2 = null;
	          BDCS_FSQL_GZ fsql2 = null;
	          BDCS_DJDY_GZ djdy = (BDCS_DJDY_GZ)djdys.get(i);
	          List<BDCS_QL_GZ> ql_list = super.getCommonDao().getDataList(BDCS_QL_GZ.class, " DJDYID='" + djdy.getDJDYID() + "'");
	          if ((ql_list != null) && (ql_list.size() > 0)) {
	            for (BDCS_QL_GZ ql : ql_list) {
	              if ("4".equals(ql.getQLLX()))
	              {
	                ql1 = ql;
	              }
	              else if ("23".equals(ql.getQLLX()))
	              {
	                ql2 = ql;
	                fsql2 = (BDCS_FSQL_GZ)super.getCommonDao().get(BDCS_FSQL_GZ.class, ql.getFSQLID());
	              }
	            }
	          }
	          Object qlrs = super.getQLRs(ql1.getId());
	          List<BDCS_SQR> sqrs = new ArrayList();
	          ProjectServiceImpl serviceImpl = (ProjectServiceImpl)SuperSpringContext.getContext().getBean(ProjectServiceImpl.class);
	          sqrs = serviceImpl.getSQRList(super.getXMBH());
	          if (ConstValue.QLLX.GYJSYDSHYQ_FWSYQ.Value.equals(getQllx().Value))
	          {
	            BDCS_H_XZY h = null;
	            boolean flag = false;
	            boolean flag2 = false;
	            boolean flag3 = false;
	            boolean flag4 = false;
	            boolean flag5 = false;
	            boolean flag6 = false;
	            String twoReportAllError = "";
	            Map<String, String> xmlError = new HashMap();
	            Message msg = exchangeFactory.createMessageByYGDJ();
	            h = (BDCS_H_XZY)dao.get(BDCS_H_XZY.class, djdy.getBDCDYID());
	            if (h != null)
	            {
	              BDCS_SHYQZD_XZ zd = (BDCS_SHYQZD_XZ)dao.get(BDCS_SHYQZD_XZ.class, h.getZDBDCDYID());
	              if (zd != null) {
	                h.setZDDM(zd.getZDDM());
	              }
	            }
	            super.fillHead(msg, i, ywh, h.getBDCDYH(), h.getQXDM(),ql1.getLYQLID());
	            bizMsgId = msg.getHead().getBizMsgID();
	            msg.getHead().setRecType("7000101");
	            msg.getHead().setParcelID(StringHelper.formatObject(h.getZDDM()));
	            msg.getHead().setEstateNum(StringHelper.formatObject(h.getBDCDYH()));
	            msg.getHead().setPreEstateNum(StringHelper.formatObject(h.getBDCDYH()));
	            if ((h != null) && (!StringUtils.isEmpty(h.getQXDM()))) {
	              msg.getHead().setAreaCode(h.getQXDM());
	            }
	            try
	            {
	              if (djdy != null) {
	                fillCommonPartData(msg, h, ql1, fsql2, (List)qlrs, sqrs, ywh, xmxx, actinstID);
	              }
	            }
	            catch (Exception e)
	            {
	              e.printStackTrace();
	            }
	            String fullname = "Biz" + bizMsgId + ".xml";
	            mashaller.marshal(msg, new File(path + fullname));
	            result = uploadFile(new StringBuilder().append(path).append(fullname).toString(), ConstValue.RECCODE.YG_ZXDJ.Value, actinstID, djdy.getDJDYID(), ql1.getId()) + " \n ";
	            names.put(djdy.getDJDYID(), bizMsgId + ".xml");
	            if ((result.equals("")) || (result == null)) {
	              flag = true;
	            }
	            if ((!"1".equals(result)) && (result.indexOf("success") == -1)) {
	              flag3 = true;
	            }
	            if ((!StringUtils.isEmpty(result)) && (result.indexOf("success") > -1) && (!names.containsKey("reccode"))) {
	              flag5 = true;
	            }
	            Message msg2 = exchangeFactory.createMessageByYDYDJ();
	            super.fillHead(msg2, i, ywh, h.getBDCDYH(), h.getQXDM(),ql2.getLYQLID());
	            bizMsgId = msg2.getHead().getBizMsgID();
	            msg2.getHead().setRecType("7000101");
	            msg2.getHead().setParcelID(StringHelper.formatObject(h.getZDDM()));
	            msg2.getHead().setEstateNum(StringHelper.formatObject(h.getBDCDYH()));
	            msg2.getHead().setPreEstateNum(StringHelper.formatObject(h.getBDCDYH()));
	            if ((h != null) && (!StringUtils.isEmpty(h.getQXDM()))) {
	              msg2.getHead().setAreaCode(h.getQXDM());
	            }
	            try
	            {
	              if (djdy != null)
	              {
	                fillCommonPartData(msg2, h, ql2, fsql2, (List)qlrs, sqrs, ywh, xmxx, actinstID);
	                
	                QLFQLDYAQ dyaq = msg2.getData().getQLFQLDYAQ();
	                packageXml.getQLFQLDYAQ(dyaq, null, ql2, fsql2, ywh, h);
	                msg2.getData().setQLFQLDYAQ(dyaq);
	              }
	            }
	            catch (Exception e)
	            {
	              e.printStackTrace();
	            }
	            long bizMsgId2 = StringHelper.getLong(bizMsgId) + 1L;
	            String fullname2 = "Biz" + StringHelper.formatObject(Long.valueOf(bizMsgId2)) + ".xml";
	            mashaller.marshal(msg2, new File(path + fullname2));
	            result2 = uploadFile(path + fullname2, ConstValue.RECCODE.YG_ZXDJ.Value, actinstID, djdy.getDJDYID(), ql2.getId());
	            names.put(djdy.getDJDYID(), StringHelper.formatObject(Long.valueOf(bizMsgId2)) + ".xml");
	            if (("".equals(result2)) || (result2 == null)) {
	              flag2 = true;
	            }
	            if ((!"1".equals(result2)) && (result2.indexOf("success") == -1)) {
	              flag4 = true;
	            }
	            if ((!StringUtils.isEmpty(result)) && (result.indexOf("success") > -1) && (!names.containsKey("reccode"))) {
	              flag6 = true;
	            }
	            String allreportname = "";
	            if (flag)
	            {
	              twoReportAllError = "该组合流程中“预告部分”的问题：连接SFTP失败\n";
	              allreportname = "Biz" + msg.getHead().getBizMsgID() + ".xml";
	            }
	            if (flag2)
	            {
	              twoReportAllError = twoReportAllError + "该组合流程中“预抵押部分”的问题：连接SFTP失败 \n";
	              if (!flag) {
	                allreportname = allreportname + fullname2;
	              } else {
	                allreportname = allreportname + "  ,  " + fullname2;
	              }
	            }
	            if ((flag) || (flag2))
	            {
	              twoReportAllError = twoReportAllError + "请检查服务器和前置机的连接是否正常";
	              xmlError.put("error", twoReportAllError);
	              YwLogUtil.addSjsbResult(allreportname, "", twoReportAllError, ConstValue.SF.NO.Value, ConstValue.RECCODE.YG_ZXDJ.Value, ProjectHelper.getpRroinstIDByActinstID(actinstID));
	              return xmlError;
	            }
	            if ((flag3) || (flag4))
	            {
	              if ((flag3) && (!flag4)) {
	                combineResult = "该组合流程中“预告部分”的问题：" + result;
	              } else if ((!flag3) && (flag4)) {
	                combineResult = "该组合流程中“预抵押部分”的结果为：" + result2;
	              } else {
	                combineResult = "该组合流程中“预告部分”的问题：" + result + "\n该组合流程中“预抵押部分”的结果为：" + result2;
	              }
	              xmlError.put("error", result);
	              return xmlError;
	            }
	            if ((flag5) || (flag6))
	            {
	              if ((flag5) && (!flag6)) {
	                combineResult = result;
	              } else if ((!flag5) && (flag6)) {
	                combineResult = result2;
	              } else {
	                combineResult = result + "\n" + result2;
	              }
	              names.put("reccode", combineResult);
	            }
	          }
	        }
	      }
	    }
	    catch (Exception e)
	    {
	      e.printStackTrace();
	    }
	    return names;
	  }
	  
	  private void fillCommonPartData(Message msg, BDCS_H_XZY h, BDCS_QL_GZ ql, BDCS_FSQL_GZ fsql, List<BDCS_QLR_GZ> qlrs, List<BDCS_SQR> sqrs, String ywh, BDCS_XMXX xmxx, String actinstID)
	  {
	    QLFQLYGDJ ygdj = msg.getData().getQLFQLYGDJ();
	    ygdj = packageXml.getQLFQLYGDJ(ygdj, h, ql, fsql, ywh);
	    msg.getData().setQLFQLYGDJ(ygdj);
	    
	    DJTDJSLSQ sq = msg.getData().getDJSLSQ();
	    sq = packageXml.getDJTDJSLSQ(sq, ywh, null, ql, xmxx, h, xmxx.getSLSJ(), null, null, null);
	    msg.getData().setDJSLSQ(sq);
	    
	  //9.登记收费(可选)
		List<DJFDJSF> sfList = msg.getData().getDJSF();
		sfList = packageXml.getDJSF(h, ywh,this.getXMBH());
		msg.getData().setDJSF(sfList);

		//10.审核(可选)
		List<DJFDJSH> sh = msg.getData().getDJSH();
		 sh = packageXml.getDJFDJSH(h, ywh, this.getXMBH(), actinstID);
		msg.getData().setDJSH(sh);

		//11.缮证(可选)
		List<DJFDJSZ>  sz = packageXml.getDJFDJSZ(h, ywh, this.getXMBH());
		msg.getData().setDJSZ(sz);

		//11.发证(可选)
		List<DJFDJFZ>   fz = packageXml.getDJFDJFZ(h, ywh, this.getXMBH());
		msg.getData().setDJFZ(fz);
		
		//12.归档(可选)
		List<DJFDJGD> gd = packageXml.getDJFDJGD(h, ywh, this.getXMBH());
		msg.getData().setDJGD(gd);
		
	    FJF100 fj = msg.getData().getFJF100();
	    fj = packageXml.getFJF(fj);
	    
	    List<ZTTGYQLR> zttqlr = msg.getData().getGYQLR();
	    zttqlr = packageXml.getZTTGYQLRs(zttqlr, qlrs, null, ql, h, null, null, null);
	    msg.getData().setGYQLR(zttqlr);
	    
	    List<DJFDJSQR> djsqrs = msg.getData().getDJSQR();
	    djsqrs = packageXml.getDJSQRs(djsqrs, sqrs, h.getYSDM(), ywh, h.getBDCDYH());
	    msg.getData().setDJSQR(djsqrs);
	  }
	  
	  protected String getDYBDCLXfromBDCDYLX(ConstValue.BDCDYLX bdcdylx)
	  {
	    String dybdclx = "";
	    if ((bdcdylx.equals(ConstValue.BDCDYLX.SHYQZD)) || (bdcdylx.equals(ConstValue.BDCDYLX.SYQZD))) {
	      dybdclx = "1";
	    } else if ((bdcdylx.equals(ConstValue.BDCDYLX.H)) || (bdcdylx.equals(ConstValue.BDCDYLX.ZRZ))) {
	      dybdclx = "2";
	    } else if (bdcdylx.equals(ConstValue.BDCDYLX.LD)) {
	      dybdclx = "3";
	    } else if ((bdcdylx.equals(ConstValue.BDCDYLX.GZW)) || (bdcdylx.equals(ConstValue.BDCDYLX.YCH)) || (bdcdylx.equals(ConstValue.BDCDYLX.ZRZ))) {
	      dybdclx = "4";
	    } else if (bdcdylx.equals(ConstValue.BDCDYLX.HY)) {
	      dybdclx = "5";
	    } else {
	      dybdclx = "7";
	    }
	    return dybdclx;
	  }
	  
	  public void SendMsg(String bljc)
	  {
	    BDCS_XMXX xmxx = (BDCS_XMXX)getCommonDao().get(BDCS_XMXX.class, super.getXMBH());
	    String xmbhFilter = ProjectHelper.GetXMBHCondition(super.getXMBH());
	    List<BDCS_DJDY_GZ> djdys = getCommonDao().getDataList(BDCS_DJDY_GZ.class, xmbhFilter);
	    if ((djdys != null) && (djdys.size() > 0)) {
	      for (int idjdy = 0; idjdy < djdys.size(); idjdy++)
	      {
	        BDCS_DJDY_GZ djdy = (BDCS_DJDY_GZ)djdys.get(idjdy);
	        ConstValue.BDCDYLX dylx = ConstValue.BDCDYLX.initFrom(djdy.getBDCDYLX());
	        ConstValue.DJDYLY dyly = ConstValue.DJDYLY.initFrom(djdy.getLY());
	        RealUnit bdcdy = UnitTools.loadUnit(dylx, dyly, djdy.getBDCDYID());
	        Rights bdcql = RightsTools.loadRightsByDJDYID(dyly, getXMBH(), djdy.getDJDYID());
	        SubRights bdcfsql = RightsTools.loadSubRightsByRightsID(dyly, bdcql.getId());
	        List<RightsHolder> bdcqlrs = RightsHolderTools.loadRightsHolders(dyly, djdy.getDJDYID(), getXMBH());
	        MessageExport msg = super.getShareMsgTools().GetMsg(bdcdy, bdcql, bdcfsql, bdcqlrs, bljc, xmxx);
	        super.getShareMsgTools().SendMsg(msg, idjdy + 1, djdy.getBDCDYLX(), xmxx);
	      }
	    }
	  }
	  
	  private String getStatus(String fj, String djdyid, String bdcdyid, String bdcdylx)
	  {
	    UnitStatus status = new UnitStatus();
//	    Rights ql;
	    StringBuilder builder = new StringBuilder();
	    builder.append("SELECT XMXX.YWLSH AS YWLSH,XMXX.DJLX AS XMDJLX,XMXX.QLLX AS XMQLLX, ");
	    builder.append("QL.DJLX AS QLDJLX,QL.QLLX AS QLQLLX ");
	    builder.append("FROM BDCK.BDCS_QL_GZ QL ");
	    builder.append("LEFT JOIN BDCK.BDCS_XMXX XMXX ");
	    builder.append("ON QL.XMBH=XMXX.XMBH ");
	    builder.append("WHERE XMXX.DJLX<>'400' AND NVL2(XMXX.SFDB,'1','0')<>'1' ");
	    builder.append("AND QL.DJDYID='" + djdyid + "' ");
	    Map ql=null;
	    List<Map> qls_xzmap;
	    List<Map> qls_gz = getCommonDao().getDataListByFullSql(builder.toString());
	    for (Iterator localIterator = qls_gz.iterator(); localIterator.hasNext();)
	    {
	      ql = (Map)localIterator.next();
	      String xmdjlx = StringHelper.formatDouble(ql.get("XMDJLX"));
	      String xmqllx = StringHelper.formatDouble(ql.get("XMQLLX"));
	      if ((ConstValue.DJLX.CFDJ.Value.equals(xmdjlx)) && 
	        ("98".equals(xmqllx))) {
	        status.setSeizureState("正在办理查封");
	      }
	      if (ConstValue.DJLX.YYDJ.Value.equals(xmdjlx)) {
	        status.setObjectionState("正在办理异议");
	      } else if (ConstValue.DJLX.YGDJ.Value.equals(xmdjlx))
	      {
	        if (ConstValue.QLLX.QTQL.Value.equals(xmqllx)) {
	          status.setTransferNoticeState("正在办理转移预告");
	        } else if (ConstValue.QLLX.DIYQ.Value.equals(xmqllx)) {
	          if (ConstValue.BDCDYLX.YCH.Value.equals(bdcdylx)) {
	            status.setMortgageState("正在办理抵押");
	          } else {
	            status.setMortgageNoticeState("正在办理抵押预告");
	          }
	        }
	      }
	      else if (ConstValue.QLLX.DIYQ.Value.equals(xmqllx)) {
	        status.setMortgageState("正在办理抵押");
	      }
	    }
	    
	    List<Rights> rightss= RightsTools.loadRightsByCondition(ConstValue.DJDYLY.XZ, "DJDYID='" + djdyid + "'");
	    for ( Rights rights : rightss)
	    {
	      String djlx = rights.getDJLX();
	      String qllx = rights.getQLLX();
	      if (ConstValue.DJLX.CFDJ.Value.equals(djlx)) {
	        status.setSeizureState("已查封");
	      }
	      if (ConstValue.DJLX.YYDJ.Value.equals(djlx)) {
	        status.setObjectionState("已异议");
	      } else if (ConstValue.DJLX.YGDJ.Value.equals(djlx))
	      {
	        if (ConstValue.QLLX.QTQL.Value.equals(qllx)) {
	          status.setTransferNoticeState("已转移预告");
	        } else if (ConstValue.QLLX.DIYQ.Value.equals(qllx)) {
	          if (ConstValue.BDCDYLX.YCH.Value.equals(bdcdylx)) {
	            status.setMortgageState("已抵押");
	          } else {
	            status.setMortgageNoticeState("已抵押预告");
	          }
	        }
	      }
	      else if (ConstValue.QLLX.DIYQ.Value.equals(qllx)) {
	        status.setMortgageState("已抵押");
	      }
	    }

	    List<BDCS_DYXZ> list_limit = getCommonDao().getDataList(BDCS_DYXZ.class, "YXBZ<>'2' AND BDCDYID='" + bdcdyid + "' AND BDCDYLX='" + bdcdylx + "' ORDER BY YXBZ ");
	    if ((list_limit != null) && (list_limit.size() > 0)) {
	      for (BDCS_DYXZ limit : list_limit) {
	        if ("1".equals(limit.getYXBZ())) {
	          status.setLimitState("已限制");
	        } else {
	          status.setLimitState("正在办理限制");
	        }
	      }
	    }
	    String tmp = fj;
	    if (StringHelper.isEmpty(tmp))
	    {
	      if ((status.getMortgageState().contains("已")) || (status.getMortgageState().contains("正在"))) {
	        tmp = status.getMortgageState() + ",";
	      }
	      if ((status.getLimitState().contains("已")) || (status.getLimitState().contains("正在"))) {
	        tmp = tmp + status.getLimitState();
	      }
	      fj = tmp;
	    }
	    else
	    {
	      if ((status.getMortgageState().contains("已")) || (status.getMortgageState().contains("正在"))) {
	        tmp = status.getMortgageState() + ",";
	      }
	      if ((status.getLimitState().contains("已")) || (status.getLimitState().contains("正在"))) {
	        tmp = tmp + status.getLimitState();
	      }
	      fj = fj + tmp;
	    }
	    return fj;
	  }

}
