package com.supermap.realestate.registration.handlerImpl;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.util.StringUtils;

import com.supermap.realestate.registration.ViewClass.ProjectInfo;
import com.supermap.realestate.registration.ViewClass.UnitStatus;
import com.supermap.realestate.registration.ViewClass.UnitTree;
import com.supermap.realestate.registration.handler.DJHandler;
import com.supermap.realestate.registration.model.BDCS_DJDY_GZ;
import com.supermap.realestate.registration.model.BDCS_DJDY_XZ;
import com.supermap.realestate.registration.model.BDCS_DYXZ;
import com.supermap.realestate.registration.model.BDCS_H_GZ;
import com.supermap.realestate.registration.model.BDCS_H_XZ;
import com.supermap.realestate.registration.model.BDCS_H_XZY;
import com.supermap.realestate.registration.model.BDCS_QLR_GZ;
import com.supermap.realestate.registration.model.BDCS_QLR_XZ;
import com.supermap.realestate.registration.model.BDCS_QL_GZ;
import com.supermap.realestate.registration.model.BDCS_QL_XZ;
import com.supermap.realestate.registration.model.BDCS_SHYQZD_GZ;
import com.supermap.realestate.registration.model.BDCS_SHYQZD_XZ;
import com.supermap.realestate.registration.model.BDCS_SLLM_GZ;
import com.supermap.realestate.registration.model.BDCS_SQR;
import com.supermap.realestate.registration.model.BDCS_SYQZD_GZ;
import com.supermap.realestate.registration.model.BDCS_XMXX;
import com.supermap.realestate.registration.model.BDCS_ZH_GZ;
import com.supermap.realestate.registration.model.BDCS_ZRZ_GZ;
import com.supermap.realestate.registration.model.BDCS_ZRZ_XZ;
import com.supermap.realestate.registration.model.BDCS_ZS_GZ;
import com.supermap.realestate.registration.model.WFD_MAPPING;
import com.supermap.realestate.registration.model.interfaces.House;
import com.supermap.realestate.registration.model.interfaces.RealUnit;
import com.supermap.realestate.registration.model.interfaces.Rights;
import com.supermap.realestate.registration.model.interfaces.RightsHolder;
import com.supermap.realestate.registration.model.interfaces.RightsRelation;
import com.supermap.realestate.registration.model.interfaces.SubRights;
import com.supermap.realestate.registration.model.xmlExportmodel.MessageExport;
import com.supermap.realestate.registration.tools.RightsHolderTools;
import com.supermap.realestate.registration.tools.RightsTools;
import com.supermap.realestate.registration.tools.UnitTools;
import com.supermap.realestate.registration.util.ConstValue;
import com.supermap.realestate.registration.util.ConstValue.BDCDYLX;
import com.supermap.realestate.registration.util.ConstValue.DJDYLY;
import com.supermap.realestate.registration.util.ConstValue.DJLX;
import com.supermap.realestate.registration.util.ConstValue.QLLX;
import com.supermap.realestate.registration.util.ConstValue.SQRLB;
import com.supermap.realestate.registration.util.ProjectHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.realestate.registration.util.exportXmlUtil;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;

/*
 1、房屋所有权+抵押权组合换证登记（老的房产证和他项证换新的不动产证书和证明）房屋所有权+抵押权组合换证登记（老的房产证和他项证换新的不动产证书和证明）处理类
 */

/**
 * 房屋所有权+抵押权组合换证登记（老的房产证和他项证换新的不动产证书和证明）处理类
 * @ClassName: HZ_SYQ_DYQ_DJHandler
 * @Author liushufeng
 * @date 2016年3月2日 上午10:47:48
 */
public class HZ_SYQ_DYQ_DJHandler extends DJHandlerBase implements DJHandler {

	/**
	 * 构造函数
	 * 
	 * @param info
	 */
	public HZ_SYQ_DYQ_DJHandler(ProjectInfo info) {
		super(info);
	}

	/**
	 * 根据抵押权ID
	 * @Title: addBDCDY
	 * @Author:liushufeng
	 * @date：2016年3月2日 上午11:57:14
	 * @param qlid
	 *            抵押权的权利ID
	 * @return
	 */
	@Override
	public boolean addBDCDY(String qlid) {
		boolean bsuccess = false;
		CommonDao dao = getCommonDao();
		String xmbh = super.getXMBH();
		// 先查找到抵押权，然后查找登记单元，然后查找所有权，分别拷贝到工作层，把权证号清空，拷贝权地证人
		Rights dyq = RightsTools.loadRights(DJDYLY.XZ, qlid);
		if (!StringHelper.isEmpty(dyq)) {
			// 抵押权利
			Rights _gzdyq = RightsTools.copyRightsAllWithNewID(DJDYLY.XZ, DJDYLY.GZ, dyq);
			_gzdyq.setDJLX(super.getDjlx().Value);
			_gzdyq.setLYQLID(qlid);// 设置来源权利ID
			_gzdyq.setXMBH(xmbh);
			_gzdyq.setBDCQZH("");
			_gzdyq.setYWH(super.getProject_id());
			_gzdyq.setDJSJ(null);
			_gzdyq.setDBR(null);
			_gzdyq.setDJJG(null);
			_gzdyq.setCASENUM("");
			
			// 抵押附属权利
			SubRights _subDyqRights = RightsTools.loadSubRights(DJDYLY.GZ, _gzdyq.getFSQLID());
			_subDyqRights.setXMBH(xmbh);
			// 抵押权利人
			List<RightsHolder> holders = RightsHolderTools.loadRightsHolders(DJDYLY.GZ, _gzdyq.getId());
			// 抵押权地证人
			List<RightsRelation> relations = RightsTools.loadRightsRelationsByQLID(DJDYLY.GZ, _gzdyq.getId());
			// 抵押证书
			List<BDCS_ZS_GZ> zslist = dao.getDataList(BDCS_ZS_GZ.class, " id in (select ZSID FROM BDCS_QDZR_GZ WHERE QLID='" + _gzdyq.getId() + "')");
						
			for (RightsHolder holder : holders) {
				holder.setBDCQZH("");
				holder.setXMBH(xmbh);
			}
			for (RightsRelation relation : relations) {
				relation.setXMBH(xmbh);
			}
			for (BDCS_ZS_GZ zs : zslist) {
				zs.setBDCQZH("");
				zs.setZSBH("");
				zs.setXMBH(xmbh);
			}
			dao.save(_gzdyq);
			dao.flush();
			//拷贝申请人
			CopySQRFromGZQLR(_gzdyq.getDJDYID(), _gzdyq.getQLLX(), this.getXMBH(), _gzdyq.getId(),SQRLB.JF.Value);
		
			// 拷贝登记单元
			String djdyid = _gzdyq.getDJDYID();
			List<BDCS_DJDY_XZ> djdyxzs=dao.getDataList(BDCS_DJDY_XZ.class, "DJDYID='"+djdyid+"'");
			if(djdyxzs!=null && djdyxzs.size()>0){
				BDCS_DJDY_XZ djdyxz=djdyxzs.get(0);
				BDCS_DJDY_GZ djdygz=new BDCS_DJDY_GZ();
				djdygz.setXMBH(this.getXMBH());
				djdygz.setDJDYID(djdyxz.getDJDYID());
				djdygz.setBDCDYID(djdyxz.getBDCDYID());
				djdygz.setBDCDYLX(this.getBdcdylx().Value);
				djdygz.setBDCDYH(djdyxz.getBDCDYH());
				djdygz.setLY(DJDYLY.XZ.Value);
				dao.save(djdygz);
			}
			
			List<Rights> syqrights = RightsTools.loadRightsByCondition(DJDYLY.XZ, "DJDYID='" + djdyid + "' AND QLLX IN ('1','2','3','4','5','6','7','8')");
			if (syqrights != null && syqrights.size() > 0) {
				Rights syq = syqrights.get(0);
				if (syq != null) {
					String fj = "";
					String bdcdyid = "";
					Rights rightssyq=RightsTools.copyRightsAllWithNewID(DJDYLY.XZ, DJDYLY.GZ, syq);
					rightssyq.setXMBH(xmbh);
					rightssyq.setBDCQZH("");
					rightssyq.setLYQLID(syq.getId());
					rightssyq.setYWH(super.getProject_id());
					rightssyq.setDJSJ(null);
					rightssyq.setDBR(null);
					rightssyq.setDJJG(null);
					rightssyq.setDJLX(super.getDjlx().Value);
					rightssyq.setCASENUM("");
					
					BDCS_XMXX xmxx = getCommonDao().get(BDCS_XMXX.class, getXMBH());
					String workflowcode = ProjectHelper.getWorkflowCodeByProjectID(xmxx.getPROJECT_ID());
					String sql = " WORKFLOWCODE='" + workflowcode + "'";
					List<WFD_MAPPING> mappings = dao.getDataList(WFD_MAPPING.class, sql);
					if (mappings != null && mappings.size() > 0) {
						WFD_MAPPING maping = mappings.get(0);
						if (("1").equals(maping.getISINITATATUS())){
							fj = rightssyq.getFJ();
							fj = getStatus(fj, rightssyq.getDJDYID(), bdcdyid, this.getBdcdylx().Value);
							rightssyq.setFJ(fj);
						}
					}
					
					
					SubRights subrightssyq=RightsTools.loadSubRights(DJDYLY.GZ, rightssyq.getFSQLID());
					subrightssyq.setXMBH(xmbh);
					
					// 所有权权利人
					List<RightsHolder> syqholders = RightsHolderTools.loadRightsHolders(DJDYLY.GZ, rightssyq.getId());
					
					// 所有权权地证人
					List<RightsRelation> syqrelations = RightsTools.loadRightsRelationsByQLID(DJDYLY.GZ, rightssyq.getId());
					
					// 所有权证书
					List<BDCS_ZS_GZ> syqzslist = dao.getDataList(BDCS_ZS_GZ.class, " id in (select ZSID FROM BDCS_QDZR_GZ WHERE QLID='" + rightssyq.getId() + "')");
	
					for (RightsHolder holder : syqholders) {
						holder.setBDCQZH("");
						holder.setXMBH(xmbh);
					}
					for (RightsRelation relation : syqrelations) {
						relation.setXMBH(xmbh);
					}
					for (BDCS_ZS_GZ zs : syqzslist) {
						zs.setBDCQZH("");
						zs.setZSBH("");
						zs.setXMBH(xmbh);
					}
					
					dao.save(rightssyq);
					dao.flush();
					CopySQRFromGZQLR(syq.getDJDYID(), syq.getQLLX(), this.getXMBH(), syq.getId(),SQRLB.JF.Value);
					CopySQRFromGZQLR(syq.getDJDYID(), syq.getQLLX(), this.getXMBH(), syq.getId(),SQRLB.YF.Value);
				
				}
			}
	
			System.out.println("asdfasdf");
		}
		dao.flush();
		bsuccess = true;
		return bsuccess;
	}

	/**
	 * 写入登记簿
	 */
	@Override
	public boolean writeDJB() {

		if (super.isCForCFING()) {
			return false;
		}
		
		List<BDCS_QL_GZ> qls = getCommonDao().getDataList(BDCS_QL_GZ.class,
				" XMBH='" + getXMBH() + "'");
		if (qls != null) {
			for (int iql = 0; iql < qls.size(); iql++) {
				super.removeQLXXFromXZByQLID(qls.get(iql).getLYQLID());
			}
		}
		List<BDCS_DJDY_GZ> djdys = getCommonDao().getDataList(
				BDCS_DJDY_GZ.class, " XMBH='" + getXMBH() + "'");
		if (djdys != null) {
			for (int idjdy = 0; idjdy < djdys.size(); idjdy++) {
				BDCS_DJDY_GZ bdcs_djdy_gz = djdys.get(idjdy);
				String djdyid = bdcs_djdy_gz.getDJDYID();
				super.CopyGZQLToXZAndLS(djdyid);
				super.CopyGZQLRToXZAndLSNotOnQLLX(djdyid);
				super.CopyGZQDZRToXZAndLS(djdyid);
				super.CopyGZZSToXZAndLS(djdyid);
			}
		}
		this.SetSFDB();
		getCommonDao().flush();
		super.alterCachedXMXX();
		return true;
	}

	/**
	 * 移除不动产单元
	 */
	@Override
	public boolean removeBDCDY(String qlid) {
		boolean bsuccess = false;
		// 找到登记单元表，移除记录
		CommonDao baseCommonDao = this.getCommonDao();
//		String hql = MessageFormat.format(" BDCDYID=''{0}'' and XMBH=''{1}''", bdcdyid,this.getXMBH());
//		List<BDCS_DJDY_GZ> djdys = baseCommonDao.getDataList(
//				BDCS_DJDY_GZ.class, hql);
//      Rights right=RightsTools.loadRights(DJDYLY.GZ, bdcdyid);
		BDCS_QL_GZ bdcs_ql_gz = getCommonDao().get(BDCS_QL_GZ.class, qlid);
		if (bdcs_ql_gz != null) {
			StringBuilder builderDJDY = new StringBuilder();
			builderDJDY.append(" DJDYID='");
			builderDJDY.append(bdcs_ql_gz.getDJDYID());
			builderDJDY.append("' AND XMBH='");
			builderDJDY.append(super.getXMBH());
			builderDJDY.append("'");
			// 获取登记单元集合
			List<BDCS_DJDY_GZ> djdys = getCommonDao().getDataList(BDCS_DJDY_GZ.class, builderDJDY.toString());
			if(djdys !=null && djdys.size() > 0){
				BDCS_DJDY_GZ djdy = null;
	//			String hql = MessageFormat.format("XMBH=''{0}'' AND DJDYID=''{1}''", getXMBH(), right.getDJDYID());
	//			List<BDCS_DJDY_GZ> list = baseCommonDao.getDataList(BDCS_DJDY_GZ.class, hql);
				
				djdy = djdys.get(0);
				baseCommonDao.deleteEntity(djdy);
				
				String djdyid = djdy.getDJDYID();
				// 删除权利关联申请人
				super.RemoveSQRByQLID(djdyid, getXMBH());
	
				// 删除权利、附属权利、权利人、证书、权地证人关系
				String _hqlCondition = MessageFormat.format("XMBH=''{0}'' AND DJDYID=''{1}''", getXMBH(), djdyid);
				RightsTools.deleteRightsAllByCondition(DJDYLY.GZ, _hqlCondition);
	
				// 移除单元关联申请人
				super.RemoveSQRByQLID(djdyid, getXMBH());
				
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
		String xmbhFilter = ProjectHelper.GetXMBHCondition(getXMBH());
		CommonDao dao = this.getCommonDao();
		List<UnitTree> list = new ArrayList<UnitTree>();
		List<BDCS_DJDY_GZ> djdys = dao.getDataList(BDCS_DJDY_GZ.class, xmbhFilter);
		if (djdys != null && djdys.size() > 0) {
			for (int i = 0; i < djdys.size(); i++) {
				BDCS_DJDY_GZ djdy = djdys.get(i);
				UnitTree tree = new UnitTree();
				StringBuilder BuilderQL = new StringBuilder();
				BuilderQL.append(xmbhFilter).append(" AND DJDYID='").append(djdy.getDJDYID()).append("'");
				List<BDCS_QL_GZ> qls = dao.getDataList(BDCS_QL_GZ.class, BuilderQL.toString());
				if (qls != null) {
					for (int iql = 0; iql < qls.size(); iql++) {
						BDCS_QL_GZ ql = qls.get(iql);
						if (ql.getQLLX().equals(QLLX.DIYQ.Value)) {
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
				String ly = StringUtils.isEmpty(djdy.getLY()) ? "gz" : DJDYLY.initFrom(djdy.getLY()) == null ? "gz" : DJDYLY.initFrom(djdy.getLY()).Name;
				tree.setLy(ly);
				String zl = getZL(tree, djdy.getDJDYID(), djdy.getBDCDYLX(), djdy.getBDCDYID(), ly);
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
						}
					}
				}
				tree.setZl(zl);
				list.add(tree);
			}
		}
		return list;
	}

	private String getZL(UnitTree tree, String djdyid, String bdcdylx, String bdcdyid, String djdyly) {
		String zl = "";
		CommonDao dao = getCommonDao();
		BDCDYLX dylx = BDCDYLX.initFrom(bdcdylx);
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
				zl = ld == null ? "" : ld.getZL();
			} else {
				// TODO 刘树峰:获取其他类型不动产单元的坐落
			}
		} else {// 来源于现状，把原来的所有权/使用权的权利ID也加上
			if (dylx.equals(BDCDYLX.SHYQZD)) {
				BDCS_SHYQZD_XZ shyqzd = dao.get(BDCS_SHYQZD_XZ.class, bdcdyid);
				zl = shyqzd == null ? "" : shyqzd.getZL();
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
					tree.setId(bdcs_h_xzy.getCID());
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
			String qllxarray = " ('1','2','3','4','5','6','7','8','9','10','11','12','13','14','15','16','17','18')";
			String hqlCondition = MessageFormat.format(" DJDYID IN (SELECT DJDYID FROM BDCS_DJDY_XZ WHERE BDCDYID=''{0}'') AND QLLX IN {1} ORDER BY DJSJ", bdcdyid, qllxarray);
			List<BDCS_QL_XZ> listxzql = dao.getDataList(BDCS_QL_XZ.class, hqlCondition);
			if (listxzql != null && listxzql.size() > 0) {
				BDCS_QL_XZ ql = listxzql.get(0);
				tree.setOldqlid(ql.getId());
			}
		}
		return zl;
	}

	/**
	 * 根据申请人ID添加权利人
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
		return exportXmlUtil.createXMLAndUp(path,actinstID);
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
			for (int idjdy = 0; idjdy < djdys.size(); idjdy++) {
				BDCS_DJDY_GZ djdy = djdys.get(idjdy);
				ConstValue.BDCDYLX dylx = ConstValue.BDCDYLX.initFrom(djdy.getBDCDYLX());
				ConstValue.DJDYLY dyly = ConstValue.DJDYLY.initFrom(djdy.getLY());
				RealUnit bdcdy = UnitTools.loadUnit(dylx, dyly, djdy.getBDCDYID());
				Rights bdcql = RightsTools.loadRightsByDJDYID(ConstValue.DJDYLY.GZ, getXMBH(), djdy.getDJDYID());
				SubRights bdcfsql = RightsTools.loadSubRightsByRightsID(ConstValue.DJDYLY.GZ, bdcql.getId());
				List<RightsHolder> bdcqlrs = RightsHolderTools.loadRightsHolders(ConstValue.DJDYLY.GZ, djdy.getDJDYID(), getXMBH());
				MessageExport msg = super.getShareMsgTools().GetMsg(bdcdy, bdcql, bdcfsql, bdcqlrs, bljc, xmxx);
				super.getShareMsgTools().SendMsg(msg, idjdy + 1, djdy.getBDCDYLX(), xmxx);
			}
		}
	}
	@SuppressWarnings("rawtypes")
	private String getStatus(String fj, String djdyid, String bdcdyid, String bdcdylx) {
		UnitStatus status = new UnitStatus();
		// 在办状态
		StringBuilder builder = new StringBuilder();
		builder.append("SELECT XMXX.YWLSH AS YWLSH,XMXX.DJLX AS XMDJLX,XMXX.QLLX AS XMQLLX, ");
		builder.append("QL.DJLX AS QLDJLX,QL.QLLX AS QLQLLX ");
		builder.append("FROM BDCK.BDCS_QL_GZ QL ");
		builder.append("LEFT JOIN BDCK.BDCS_XMXX XMXX ");
		builder.append("ON QL.XMBH=XMXX.XMBH ");
		builder.append("WHERE XMXX.DJLX<>'400' AND NVL2(XMXX.SFDB,'1','0')<>'1' ");
		builder.append("AND QL.DJDYID='" + djdyid + "' ");
		List<Map> qls_gz = getCommonDao().getDataListByFullSql(builder.toString());
		for (Map ql : qls_gz) {
			String xmdjlx = StringHelper.formatDouble(ql.get("XMDJLX"));
			String xmqllx = StringHelper.formatDouble(ql.get("XMQLLX"));
			if (DJLX.CFDJ.Value.equals(xmdjlx)) {
				if ("98".equals(xmqllx)) {
					status.setSeizureState("正在办理查封");
				}
			}
			if (DJLX.YYDJ.Value.equals(xmdjlx)) {
				status.setObjectionState("正在办理异议");
			} else if (DJLX.YGDJ.Value.equals(xmdjlx)) {
				if (QLLX.QTQL.Value.equals(xmqllx)) {
					status.setTransferNoticeState("正在办理转移预告");
				} else if (QLLX.DIYQ.Value.equals(xmqllx)) {
					if (BDCDYLX.YCH.Value.equals(bdcdylx)) {
						status.setMortgageState("正在办理抵押");
					} else {
						status.setMortgageNoticeState("正在办理抵押预告");
					}
				}
			} else if (QLLX.DIYQ.Value.equals(xmqllx)) {
				status.setMortgageState("正在办理抵押");
			}
		}
		// 已办状态
		List<Rights> qls_xz = RightsTools.loadRightsByCondition(DJDYLY.XZ, "DJDYID='" + djdyid + "'");
		for (Rights ql : qls_xz) {
			String djlx = ql.getDJLX();
			String qllx = ql.getQLLX();
			if (DJLX.CFDJ.Value.equals(djlx)) {
				status.setSeizureState("已查封");
			}
			if (DJLX.YYDJ.Value.equals(djlx)) {
				status.setObjectionState("已异议");
			} else if (DJLX.YGDJ.Value.equals(djlx)) {
				if (QLLX.QTQL.Value.equals(qllx)) {
					status.setTransferNoticeState("已转移预告");
				} else if (QLLX.DIYQ.Value.equals(qllx)) {
					if (BDCDYLX.YCH.Value.equals(bdcdylx)) {
						status.setMortgageState("已抵押");
					} else {
						status.setMortgageNoticeState("已抵押预告");
					}
				}
			} else if (QLLX.DIYQ.Value.equals(qllx)) {
				status.setMortgageState("已抵押");
			}
		}

		List<BDCS_DYXZ> list_limit = getCommonDao().getDataList(BDCS_DYXZ.class,
				"YXBZ<>'2' AND BDCDYID='" + bdcdyid + "' AND BDCDYLX='" + bdcdylx + "' ORDER BY YXBZ ");
		if (list_limit != null && list_limit.size() > 0) {
			for (BDCS_DYXZ limit : list_limit) {
				if ("1".equals(limit.getYXBZ())) {
					status.setLimitState("已限制");
				} else {
					status.setLimitState("正在办理限制");
				}
			}
		}
		String tmp = fj;
		if(StringHelper.isEmpty(tmp)){
			if(status.getMortgageState().contains("已")||status.getMortgageState().contains("正在"))
				tmp = status.getMortgageState() + "," ;
			if(status.getLimitState().contains("已")||status.getLimitState().contains("正在"))
				tmp += status.getLimitState();
			fj = tmp;
		}else{
			if(status.getMortgageState().contains("已")||status.getMortgageState().contains("正在"))
				tmp = status.getMortgageState() + "," ;
			if(status.getLimitState().contains("已")||status.getLimitState().contains("正在"))
				tmp += status.getLimitState();
			fj += tmp;
		}
		return fj;
	}
	
	protected void CopySQRFromGZQLR(String djdyid, String qllx, String xmbh, String qlid, String sqrlb) {
		CommonDao dao = getCommonDao();
		StringBuilder builderQL = new StringBuilder();
		builderQL.append("DJDYID ='");
		builderQL.append(djdyid);
		builderQL.append("' AND QLLX='");
		builderQL.append(qllx);
		builderQL.append("' AND XMBH='");
		builderQL.append(xmbh);
		builderQL.append("'");
		List<BDCS_QL_GZ> qls = dao.getDataList(BDCS_QL_GZ.class, builderQL.toString());
		if (qls == null || qls.size() <= 0) {
			return;
		}
		BDCS_QL_GZ ql = qls.get(0);
		StringBuilder builderQLR = new StringBuilder();
		builderQLR.append("QLID ='");
		builderQLR.append(ql.getId());
		builderQLR.append("'");
		List<BDCS_QLR_GZ> qlrs = dao.getDataList(BDCS_QLR_GZ.class, builderQLR.toString());
		if (qls == null || qls.size() <= 0) {
			return;
		}
		for (int iqlr = 0; iqlr < qlrs.size(); iqlr++) {
			String SQRID = getPrimaryKey();
			BDCS_QLR_GZ qlr = qlrs.get(iqlr);
			List<BDCS_SQR> sqrs = dao.getDataList(BDCS_SQR.class, "XMBH='" + this.getXMBH() + "' AND SQRLB='" + sqrlb + "' AND SQRXM='" + qlr.getQLRMC() + "'");
			if (sqrs != null && sqrs.size() > 0) {
				//权利人关联申请人ID
				qlr.setSQRID(sqrs.get(0).getId());
				dao.update(qlr);
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
			dao.save(sqr);
			//权利人关联申请人ID
			qlr.setSQRID(sqr.getId());
			dao.update(qlr);
		}
	}
}
