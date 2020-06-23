package com.supermap.realestate.registration.handlerImpl;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.supermap.realestate.registration.ViewClass.ProjectInfo;
import com.supermap.realestate.registration.ViewClass.UnitTree;
import com.supermap.realestate.registration.handler.DJHandler;
import com.supermap.realestate.registration.model.BDCS_DJDY_GZ;
import com.supermap.realestate.registration.model.BDCS_FSQL_GZ;
import com.supermap.realestate.registration.model.BDCS_QL_GZ;
import com.supermap.realestate.registration.model.BDCS_QL_XZ;
import com.supermap.realestate.registration.model.BDCS_SHYQZD_XZ;
import com.supermap.realestate.registration.model.BDCS_SQR;
import com.supermap.realestate.registration.model.BDCS_XMXX;
import com.supermap.realestate.registration.model.interfaces.RealUnit;
import com.supermap.realestate.registration.model.interfaces.Rights;
import com.supermap.realestate.registration.model.interfaces.RightsHolder;
import com.supermap.realestate.registration.model.interfaces.SubRights;
import com.supermap.realestate.registration.model.xmlExportmodel.MessageExport;
import com.supermap.realestate.registration.tools.RightsHolderTools;
import com.supermap.realestate.registration.tools.RightsTools;
import com.supermap.realestate.registration.tools.UnitTools;
import com.supermap.realestate.registration.util.ConstValue;
import com.supermap.realestate.registration.util.ConstValue.BDCDYLX;
import com.supermap.realestate.registration.util.ConstValue.DJDYLY;
import com.supermap.realestate.registration.util.ConstValue.QLLX;
import com.supermap.realestate.registration.util.ConstValue.SQRLB;
import com.supermap.realestate.registration.util.GetProperties;
import com.supermap.realestate.registration.util.ObjectHelper;
import com.supermap.realestate.registration.util.ProjectHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;

/*
 1、集体土地使用权地役权初始登记
 3、宅基地使用权地役权初始登记（待测）
 4、国有建设用地使用权地役权初始登记
 */

/**
 * 地役权_初始登记处理类
 * @author shb
 *
 */
public class DYQ_CSDJHandler extends DJHandlerBase implements DJHandler {

	/**
	 * 构造函数
	 * 
	 * @param info
	 */
	public DYQ_CSDJHandler(ProjectInfo info) {
		super(info);
	}

	/**
	 * 添加不动产单元
	 */
	@Override
	public boolean addBDCDY(String bdcdyid) {
		String type="";
		if (StringHelper.isEmpty(bdcdyid))
			return false;
		//是否是供役地类型或是需役地类型
		 type=getType(bdcdyid);
		 if(StringHelper.isEmpty(type)){
			 return false;
		 }
		 bdcdyid=bdcdyid.substring(0,bdcdyid.length()-type.length()-1);
		String[] ids = bdcdyid.split(",");
		if (ids == null || ids.length <= 0)
			return false;
		for (String id : ids) {
			if (StringHelper.isEmpty(id))
				continue;
			// 拷贝不动产单元，生成登记单元记录
			return addbdcdy(id,type);
		}
		this.getCommonDao().flush();
		return true;
	}


	/**
	 * 内部方法：添加不动产单元
	 * @Title: addbdcdy
	 * @author:liushufeng
	 * @date：2015年7月19日 上午3:08:02
	 * @param bdcdyid
	 * @return
	 */
	private boolean addbdcdy(String bdcdyid,String type) {
		boolean bsuccess = false;
		CommonDao dao = getCommonDao();
		List<BDCS_DJDY_GZ> lstexistdjdy=dao.getDataList(BDCS_DJDY_GZ.class, ProjectHelper.GetXMBHCondition(this.getXMBH()));
		BDCS_DJDY_GZ existdjdy=null;
		if(lstexistdjdy !=null && lstexistdjdy.size()>0){
			existdjdy=lstexistdjdy.get(0);
		}
		BDCS_DJDY_GZ djdy = super.createDJDYfromXZ(bdcdyid);
		//供役地类型
		if(type.equals("gydtype")){
			dao.save(djdy);
			// 做转移的时候加上来源权利ID，同事权利起始时间和权籍结束时间从上一手权利获取
			BDCS_QL_XZ ql=getQlid_XZ(djdy.getDJDYID());
			if(!StringHelper.isEmpty(ql)){
				super.CopySQRFromXZQLR(djdy.getDJDYID(), ql.getQLLX(), this.getXMBH(), ql.getId(),SQRLB.YF.Value);
				//更新需役地类型
				if(existdjdy !=null){
					BDCS_QL_GZ rights=(BDCS_QL_GZ)RightsTools.loadRightsByDJDYID(DJDYLY.GZ, this.getXMBH(), existdjdy.getDJDYID());
					if(rights !=null){
						rights.setGYDBDCDYID(djdy.getBDCDYID());
						rights.setGYDBDCDYLX(this.getBdcdylx().Value);
						dao.saveOrUpdate(rights);
					}
				}
			}
			bsuccess= true;
		}else if(type.equals("xydtype")){//需役地类型
			if (djdy != null) {
				RealUnit unit = null;
				try {
					unit=UnitTools.loadUnit(this.getBdcdylx(), DJDYLY.initFrom(djdy.getLY()), djdy.getBDCDYID());
				} catch (Exception e) {
					e.printStackTrace();
				}
				BDCS_QL_GZ ql = super.createQL(djdy, unit);
				ql.setQLLX(QLLX.DYQ.Value);
				// 生成附属权利
				BDCS_FSQL_GZ fsql = super.createFSQL(djdy.getDJDYID());
				fsql.setQLID(ql.getId());
				ql.setFSQLID(fsql.getId());
				if(existdjdy !=null){
					ql.setGYDBDCDYID(existdjdy.getBDCDYID());
					ql.setGYDBDCDYLX(existdjdy.getBDCDYLX());
				}
				// 如果是使用权宗地，把使用权面积加上
				if (getBdcdylx().equals(BDCDYLX.SHYQZD)) {
					BDCS_SHYQZD_XZ xzshyqzd = dao.get(BDCS_SHYQZD_XZ.class, bdcdyid);
					if (xzshyqzd != null) {
						fsql.setSYQMJ(xzshyqzd.getZDMJ());
						ql.setQDJG(xzshyqzd.getJG());// 取得价格
					}
				}
				// 做转移的时候加上来源权利ID，同事权利起始时间和权籍结束时间从上一手权利获取
				BDCS_QL_XZ ql_xz=getQlid_XZ(djdy.getDJDYID());
				if(!StringHelper.isEmpty(ql)){
					super.CopySQRFromXZQLR(djdy.getDJDYID(), ql_xz.getQLLX(), this.getXMBH(), ql.getId(),SQRLB.JF.Value);
					ql.setLYQLID(ql_xz.getId());
				}
				// 保存权利和附属权利
				dao.save(ql);
				dao.save(fsql);
				dao.save(djdy);
			}
		}
		dao.flush();
		bsuccess = true;
		return bsuccess;
	}

	/**
	 * 写入登记薄
	 */
	@Override
	public boolean writeDJB() {
		String strSqlXMBH = ProjectHelper.GetXMBHCondition(getXMBH());
		List<BDCS_DJDY_GZ> djdys = getCommonDao().getDataList(BDCS_DJDY_GZ.class, strSqlXMBH);
		if (super.isCForCFING()) {
			return false;
		}
		if (djdys != null && djdys.size() > 0) {
			for (int idjdy = 0; idjdy < djdys.size(); idjdy++) {
				BDCS_DJDY_GZ bdcs_djdy_gz = djdys.get(idjdy);
				if (bdcs_djdy_gz != null) {
					List<BDCS_QL_GZ> ql= getCommonDao().getDataList(BDCS_QL_GZ.class, strSqlXMBH+" AND DJDYID='"+bdcs_djdy_gz.getDJDYID()+"'");
					if(ql !=null && ql.size()>0){
						String djdyid = bdcs_djdy_gz.getDJDYID();
						super.CopyGZQLToXZAndLS(djdyid);
						super.CopyGZQLRToXZAndLS(djdyid);
						super.CopyGZQDZRToXZAndLS(djdyid);
						super.CopyGZZSToXZAndLS(djdyid);	
					}
				}
			}
		}
		this.SetSFDB();
		getCommonDao().flush();
		super.alterCachedXMXX();
		return true;
	}
	

	/**
	 * 移除登记产单元
	 */
	@Override
	public boolean removeBDCDY(String bdcdyid) {
		boolean bsuccess = false;
		CommonDao baseCommonDao = this.getCommonDao();
		String type="";
		//是否是供役地类型或是需役地类型
		 type=getType(bdcdyid);
		 if(StringHelper.isEmpty(type)){
			 return false;
		 }
		 bdcdyid=bdcdyid.substring(0,bdcdyid.length()-type.length()-1);
		BDCS_DJDY_GZ djdy = super.removeDJDY(bdcdyid);
		if (djdy != null) {
			String djdyid = djdy.getDJDYID();
			// 删除登记单元索引
			baseCommonDao.deleteEntity(djdy);
			// 删除从上一手拷贝过来的权利人
			if(type.equals("gydtype")){
				BDCS_QL_XZ ql=getQlid_XZ(djdy.getDJDYID());
				if(!StringHelper.isEmpty(ql)){
					String _hqlCondition=MessageFormat.format("XMBH=''{0}'' AND GLQLID=''{1}''", getXMBH(),ql.getId());
					baseCommonDao.deleteEntitysByHql(BDCS_SQR.class, _hqlCondition);
				 List<BDCS_QL_GZ> lstqls=baseCommonDao.getDataList(BDCS_QL_GZ.class, "GYDBDCDYID='"+bdcdyid+"' AND "+ProjectHelper.GetXMBHCondition(getXMBH()));
				if(lstqls !=null && lstqls.size()>0){
					for(BDCS_QL_GZ ql_gz :lstqls){
						ql_gz.setGYDBDCDYID("");
						ql_gz.setGYDBDCDYLX("");
						baseCommonDao.update(ql_gz);
					}
				}
				}	
			}else if(type.equals("xydtype")){
				// 删除权利关联申请人
				super.RemoveSQRByQLID(djdyid, getXMBH());
				//删除权利、附属权利、权利人、证书、权地证人关系
				String _hqlCondition=MessageFormat.format("XMBH=''{0}'' AND DJDYID=''{1}''", getXMBH(),djdyid);
				RightsTools.deleteRightsAllByCondition(DJDYLY.GZ, _hqlCondition);
				
			}
		}
		baseCommonDao.flush();
		bsuccess = true;
		return bsuccess;
	}

	/**
	 * 获取不动产单元列表
	 */
	@Override
	public List<UnitTree> getUnitTree() {
		List<UnitTree> deslst=new ArrayList<UnitTree>();
		List<UnitTree> sorcelst=super.getUnitList();
		if(sorcelst !=null && sorcelst.size()>0){
			for(UnitTree tree :sorcelst){
				String qlid=tree.getQlid();
				if(!StringHelper.isEmpty(qlid)){
					deslst.add(tree);
				}
			}
		}
		deslst = ObjectHelper.SortList(deslst);
		return deslst;
	}

	/**
	 * 根据申请人ID数组添加申请人
	 */
	@Override
	public void addQLRbySQRArray(String qlid, Object[] sqrids) {
		super.addQLRbySQRs(qlid, sqrids);
	}

	/**
	 * 移除权利人
	 */
	@Override
	public void removeQLR(String qlid, String qlrid) {
		super.removeqlr(qlrid, qlid);
	}

	/**
	 * 获取错误信息
	 * 
	 * @作者 刘树峰
	 * @创建时间 2015年6月23日下午8:11:43
	 * @return
	 */
	@Override
	public String getError() {
		return super.getErrMessage();
	}

	@Override
	public Map<String, String> exportXML(String path, String actinstID) {
		return super.exportXML(path, actinstID);
	}

	/**
	 * 共享信息
	 * 
	 * @作者 俞学斌
	 * @创建时间 2015年8月16日下午16:37:43
	 * @return
	 */
	@Override
	public void SendMsg(String bljc) {
		BDCS_XMXX xmxx = getCommonDao().get(BDCS_XMXX.class, super.getXMBH());
		String xmbhFilter = ProjectHelper.GetXMBHCondition(super.getXMBH());
		List<BDCS_DJDY_GZ> djdys = getCommonDao().getDataList(BDCS_DJDY_GZ.class, xmbhFilter);
		if (djdys != null && djdys.size() > 0) {
			boolean isZipFile = false;
			if(djdys.size()>=10){
				isZipFile=true;
				String folderPath=GetProperties.getConstValueByKey("xmlPath") + "\\" + xmxx.getPROJECT_ID()+"_"+bljc;
				super.getShareMsgTools().deleteFolder(folderPath);
			}
			for (int idjdy = 0; idjdy < djdys.size(); idjdy++) {
				BDCS_DJDY_GZ djdy = djdys.get(idjdy);
				ConstValue.BDCDYLX dylx = ConstValue.BDCDYLX.initFrom(djdy.getBDCDYLX());
				ConstValue.DJDYLY dyly = ConstValue.DJDYLY.initFrom(djdy.getLY());
				RealUnit bdcdy = UnitTools.loadUnit(dylx, dyly, djdy.getBDCDYID());
				Rights bdcql = RightsTools.loadRightsByDJDYID(ConstValue.DJDYLY.GZ, getXMBH(), djdy.getDJDYID());
				SubRights bdcfsql = RightsTools.loadSubRightsByRightsID(ConstValue.DJDYLY.GZ, bdcql.getId());
				List<RightsHolder> bdcqlrs = RightsHolderTools.loadRightsHolders(ConstValue.DJDYLY.GZ, djdy.getDJDYID(), getXMBH());
				MessageExport msg = super.getShareMsgTools().GetMsg(bdcdy, bdcql, bdcfsql, bdcqlrs, bljc, xmxx);
				if(isZipFile){
					String folderPath = super.getShareMsgTools().createXMLInFile(xmxx, msg, idjdy+1,bljc);
					if(idjdy == djdys.size()-1){//文件都生成到文件夹以后再压缩上传
						super.getShareMsgTools().SendMsg(folderPath, xmxx, bljc, djdy.getBDCDYLX());
					}
				}
				else{
					super.getShareMsgTools().SendMsg(msg, idjdy + 1, djdy.getBDCDYLX(), xmxx);				
				}
			}
		}
	}

	/**
	 * 返回现状层权利ID
	 * @param djdyid
	 * @return
	 */
	private BDCS_QL_XZ getQlid_XZ(String djdyid){
		BDCS_QL_XZ ql_xz=null;
		CommonDao baseCommonDao = this.getCommonDao();
		String qllxarray = "('1','2','3','5','7','9','10','13','14','15','16','17','18','24')";
		String hql = " DJDYID='" + djdyid + "' AND QLLX IN " + qllxarray;
		List<BDCS_QL_XZ> list = baseCommonDao.getDataList(BDCS_QL_XZ.class, hql);
		if (list != null && list.size() > 0) {
			 ql_xz=list.get(0);
		}
		return ql_xz;
	}
	
	/**
	 * 返回地役权需要的类型
	 * @param bdcdyid
	 * @return
	 */
	private String getType(String bdcdyid){
		String type="";
		//是否是供役地类型或是需役地类型
		if(bdcdyid.endsWith("gydtype")){
			type="gydtype";
			bdcdyid=bdcdyid.substring(0, bdcdyid.length()-7);
		}else if(bdcdyid.endsWith("xydtype")){
			type="xydtype";
			bdcdyid=bdcdyid.substring(0, bdcdyid.length()-7);
		}
		return type;
	}
}
