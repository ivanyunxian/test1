package com.supermap.realestate.registration.handlerImpl;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;

import com.supermap.realestate.registration.ViewClass.ProjectInfo;
import com.supermap.realestate.registration.ViewClass.UnitStatus;
import com.supermap.realestate.registration.ViewClass.UnitTree;
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
import com.supermap.realestate.registration.dataExchange.dyq.QLFQLDYAQ;
import com.supermap.realestate.registration.dataExchange.utils.packageXml;
import com.supermap.realestate.registration.dataExchange.ygdj.QLFQLYGDJ;
import com.supermap.realestate.registration.handler.DJHandler;
import com.supermap.realestate.registration.model.BDCS_DJDY_GZ;
import com.supermap.realestate.registration.model.BDCS_DJDY_LS;
import com.supermap.realestate.registration.model.BDCS_DYXZ;
import com.supermap.realestate.registration.model.BDCS_FSQL_GZ;
import com.supermap.realestate.registration.model.BDCS_H_GZ;
import com.supermap.realestate.registration.model.BDCS_H_XZ;
import com.supermap.realestate.registration.model.BDCS_H_XZY;
import com.supermap.realestate.registration.model.BDCS_QLR_GZ;
import com.supermap.realestate.registration.model.BDCS_QL_GZ;
import com.supermap.realestate.registration.model.BDCS_QL_XZ;
import com.supermap.realestate.registration.model.BDCS_SHYQZD_GZ;
import com.supermap.realestate.registration.model.BDCS_SHYQZD_XZ;
import com.supermap.realestate.registration.model.BDCS_SLLM_GZ;
import com.supermap.realestate.registration.model.BDCS_SLLM_XZ;
import com.supermap.realestate.registration.model.BDCS_SQR;
import com.supermap.realestate.registration.model.BDCS_SYQZD_GZ;
import com.supermap.realestate.registration.model.BDCS_XMXX;
import com.supermap.realestate.registration.model.BDCS_ZH_GZ;
import com.supermap.realestate.registration.model.BDCS_ZRZ_GZ;
import com.supermap.realestate.registration.model.BDCS_ZRZ_XZ;
import com.supermap.realestate.registration.model.WFD_MAPPING;
import com.supermap.realestate.registration.model.YC_SC_H_XZ;
import com.supermap.realestate.registration.model.interfaces.House;
import com.supermap.realestate.registration.model.interfaces.RealUnit;
import com.supermap.realestate.registration.model.interfaces.Rights;
import com.supermap.realestate.registration.model.interfaces.RightsHolder;
import com.supermap.realestate.registration.model.interfaces.SubRights;
import com.supermap.realestate.registration.model.xmlExportmodel.MessageExport;
import com.supermap.realestate.registration.service.impl.ProjectServiceImpl;
import com.supermap.realestate.registration.tools.RightsHolderTools;
import com.supermap.realestate.registration.tools.RightsTools;
import com.supermap.realestate.registration.tools.UnitTools;
import com.supermap.realestate.registration.util.ConstValue;
import com.supermap.realestate.registration.util.ConstValue.BDCDYLX;
import com.supermap.realestate.registration.util.ConstValue.CZFS;
import com.supermap.realestate.registration.util.ConstValue.DJDYLY;
import com.supermap.realestate.registration.util.ConstValue.DJLX;
import com.supermap.realestate.registration.util.ConstValue.DJZT;
import com.supermap.realestate.registration.util.ConstValue.QLLX;
import com.supermap.realestate.registration.util.ConstValue.YGDJLX;
import com.supermap.realestate.registration.util.ProjectHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.realestate.registration.util.YwLogUtil;
import com.supermap.wisdombusiness.core.SuperSpringContext;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import com.supermap.wisdombusiness.web.ResultMessage;

/*
 1、商品房预告登记+商品房抵押预告登记（公积金+商业贷款）
 */
/**
 * 
 * 预告登记+预抵押登记操作类
 * @ClassName: YGYDYDJHandler
 * @author liushufeng
 * @date 2015年9月8日 下午10:43:16
 */
public class YGYDYDJHandlerEX extends DJHandlerBase implements DJHandler {
	
	protected Logger logger = Logger.getLogger(this.getClass());

	/**
	 * 构造函数
	 * @param info
	 */
	public YGYDYDJHandlerEX(ProjectInfo info) {
		super(info);
	}
	/**
	 * 添加不动产单元
	 */
	@Override
	public boolean addBDCDY(String bdcdyid) {
		boolean bsuccess = false;
		CommonDao dao = getCommonDao();
		ResultMessage msg = new ResultMessage();
		RealUnit _srcUnit = UnitTools.loadUnit(this.getBdcdylx(), DJDYLY.XZ, bdcdyid);
		if (_srcUnit == null) {
			msg.setSuccess("false");
			msg.setMsg("找不到单元");
			return false;
		}
		if (_srcUnit != null) {
			BDCS_DJDY_GZ djdy = super.createDJDYfromXZ(bdcdyid);
			if (djdy == null) {
				String djdyid = getPrimaryKey();
				djdy = new BDCS_DJDY_GZ();
				djdy.setDJDYID(djdyid);
				djdy.setBDCDYID(_srcUnit.getId());
				djdy.setBDCDYH(_srcUnit.getBDCDYH());
				djdy.setLY(DJDYLY.XZ.Value);
				djdy.setBDCDYLX(this.getBdcdylx().Value);
				djdy.setXMBH(this.getXMBH());
			}
			if (djdy != null) {
				// 生成预告权利信息
				BDCS_QL_GZ ql = super.createQL(djdy, _srcUnit);
				// 生成预告附属权利
				BDCS_FSQL_GZ fsql = super.createFSQL(djdy.getDJDYID());
				// 生成抵押权
				BDCS_QL_GZ dyql = super.createQL(djdy, _srcUnit);
				// 生成抵押权附属权利
				BDCS_FSQL_GZ dyfsql = super.createFSQL(djdy.getDJDYID());
				// 关联预告权利-附属权利，并设定权利类型、预告登记种类
				ql.setFSQLID(fsql.getId());
				ql.setQLLX(getQllx().Value);// 预告登记+预抵押登记 设置 权利类型时 设置为
											// 国有建设用地使用权/房屋（构筑物）所有权
				fsql.setYGDJZL(YGDJLX.YSSPFMMYGDJ.Value.toString());
				fsql.setQLID(ql.getId());

				// 关联抵押权权利-附属权利，设置抵押物类型、抵押不动产类型、预告登记种类
				dyql.setFSQLID(dyfsql.getId());
				dyql.setQLLX(QLLX.DIYQ.Value);
				dyql.setCZFS(CZFS.GTCZ.Value);//设置持证方式为共同持证。
				dyfsql.setQLID(dyql.getId());
				dyfsql.setDYWLX(getDYBDCLXfromBDCDYLX(getBdcdylx()));
				dyfsql.setDYBDCLX(getDYBDCLXfromBDCDYLX(getBdcdylx()));
				dyfsql.setYGDJZL(YGDJLX.YSSPFDYQYGDJ.Value.toString());
				_srcUnit.setDJZT(DJZT.DJZ.Value); // 设置登记状态

				String fj = "";
				BDCS_XMXX xmxx = getCommonDao().get(BDCS_XMXX.class, getXMBH());
				String workflowcode = ProjectHelper.getWorkflowCodeByProjectID(xmxx.getPROJECT_ID());
				String sql = " WORKFLOWCODE='" + workflowcode + "'";;
				List<WFD_MAPPING> mappings = dao.getDataList(WFD_MAPPING.class, sql);
				if (mappings != null && mappings.size() > 0) {
					WFD_MAPPING maping = mappings.get(0);
					if (("1").equals(maping.getISINITATATUS())){
						fj = ql.getFJ();
						fj = getStatus(fj, ql.getDJDYID(), bdcdyid, this.getBdcdylx().Value);
						ql.setFJ(fj);
					}
				}
				//复制抵押权信息
				// 生成抵押权扩展
				BDCS_QL_GZ dyqlex = new BDCS_QL_GZ();
				// 生成抵押权附属权利扩展
				BDCS_FSQL_GZ dyfsqlex = new BDCS_FSQL_GZ();;
				try {
					PropertyUtils.copyProperties(dyqlex,dyql);
					PropertyUtils.copyProperties(dyfsqlex,dyfsql);
					dyqlex.setId(getPrimaryKey());
					dyfsqlex.setId(getPrimaryKey());
					dyqlex.setFSQLID(dyfsqlex.getId());
					dyfsqlex.setQLID(dyqlex.getId());
				} catch ( Exception e) {
					e.printStackTrace();
				}
				// 保存
				dao.update(_srcUnit);
				dao.save(djdy);
				dao.save(ql);
				dao.save(fsql);
				dao.save(dyql);
				dao.save(dyfsql);
				dao.save(dyqlex);
				dao.save(dyfsqlex);
			}
			dao.flush();
			bsuccess = true;
		}
		return bsuccess;
	}

	/**
	 * 写入登记薄
	 */
	@Override
	public boolean writeDJB() {
		
		if (super.isCForCFING()) {
			return false;
		}
		
		String strSqlXMBH = ProjectHelper.GetXMBHCondition(getXMBH());
		CommonDao dao = getCommonDao();
		List<BDCS_DJDY_GZ> djdys = dao.getDataList(BDCS_DJDY_GZ.class, strSqlXMBH);
		for (BDCS_DJDY_GZ bdcs_djdy_gz : djdys) {
			String djdyid = bdcs_djdy_gz.getDJDYID();
			// 拷贝相应的权利信息
			super.CopyGZQLToXZAndLS(djdyid);
			super.CopyGZQLRToXZAndLSNotOnQLLX(djdyid);
			super.CopyGZQDZRToXZAndLS(djdyid);
			super.CopyGZZSToXZAndLS(djdyid);
			List<BDCS_DJDY_LS> djdys_xz=dao.getDataList(BDCS_DJDY_LS.class, "DJDYID='"+djdyid+"'");
			if(djdys_xz==null||djdys_xz.size()<=0){
				super.CopyGZDJDYToXZAndLS(bdcs_djdy_gz.getId());
			}

			// 拷贝关系表信息YC_SC_H_XZ
			List<YC_SC_H_XZ> lst = dao.getDataList(YC_SC_H_XZ.class, "YCBDCDYID ='" + bdcs_djdy_gz.getBDCDYID() + "'");
			for (YC_SC_H_XZ yc_sc_h_xz : lst) {
				// 拷贝预测户
				super.CopyYXZHToAndLS(yc_sc_h_xz.getYCBDCDYID());
			}
			RebuildDYBG("", "", bdcs_djdy_gz.getBDCDYID(), bdcs_djdy_gz.getDJDYID(), bdcs_djdy_gz.getCreateTime(), bdcs_djdy_gz.getModifyTime());
			BDCDYLX dylx = ProjectHelper.GetBDCDYLX(getXMBH());
			// 更新单元抵押状态
			SetDYDYZT(bdcs_djdy_gz.getBDCDYID(), dylx, "0");
		}
		this.SetSFDB();
		dao.flush();
		super.alterCachedXMXX();
		return true;
	}

	/**
	 * 移除登记产单元
	 */
	@Override
	public boolean removeBDCDY(String bdcdyid) {
		boolean bsuccess = false;
		// 找到登记单元表，移除记录
		CommonDao baseCommonDao = this.getCommonDao();
		BDCS_DJDY_GZ djdy = super.removeDJDY(bdcdyid);// list.get(0);
		if (djdy != null) {
			String djdyid = djdy.getDJDYID();

			//删除权利、附属权利、权利人、证书、权地证人关系
			String _hqlCondition=MessageFormat.format("XMBH=''{0}'' AND DJDYID=''{1}''", getXMBH(),djdyid);
			RightsTools.deleteRightsAllByCondition(DJDYLY.GZ, _hqlCondition);
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
				if (djdy.getBDCDYLX().equals(BDCDYLX.H.Value)) {
					if (!StringHelper.isEmpty(djdy.getLY())) {
						DJDYLY ely = DJDYLY.initFrom(djdy.getLY());
						House house = (House) (UnitTools.loadUnit(BDCDYLX.H, ely, djdy.getBDCDYID()));
						if (house != null) {
							String fh = house.getFH();
							tree.setFh(fh);
							tree.setText("坐落:" + zl + "|" + "房号:" + (fh == null ? "" : fh));
						}
					}
				}
				// 如果是预测的话，把房号也加上
				if (djdy.getBDCDYLX().equals(BDCDYLX.YCH.Value)) {
					if (!StringHelper.isEmpty(djdy.getLY())) {
						DJDYLY ely = DJDYLY.initFrom(djdy.getLY());
						House house = (House) (UnitTools.loadUnit(BDCDYLX.YCH, ely, djdy.getBDCDYID()));
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
	 * 根据申请人ID数组添加申请人
	 */
	@Override
	public void addQLRbySQRArray(String qlid, Object[] sqrids) {
		super.addQLRbySQRs(qlid, sqrids);
		CommonDao dao = getCommonDao();
		BDCS_QL_GZ ql = dao.get(BDCS_QL_GZ.class, qlid);
		if (!ql.getQLLX().equals(QLLX.DIYQ.Value)) {
			String xmbhFilter = ProjectHelper.GetXMBHCondition(getXMBH());
			StringBuilder BuilderQLR = new StringBuilder();
			BuilderQLR.append(xmbhFilter).append(" AND QLID='").append(qlid).append("' ORDER BY SXH");
			List<BDCS_QLR_GZ> qlrs = dao.getDataList(BDCS_QLR_GZ.class, BuilderQLR.toString());
			if (qlrs != null && qlrs.size() > 0) {
				StringBuilder builderDYR = new StringBuilder();
				int indexdyr = 0;
				for (int iqlr = 0; iqlr < qlrs.size(); iqlr++) {
					if (indexdyr == 0) {
						builderDYR.append(qlrs.get(iqlr).getQLRMC());
					} else {
						builderDYR.append(",").append(qlrs.get(iqlr).getQLRMC());
					}
					indexdyr++;
				}
				StringBuilder BuilderFSQL = new StringBuilder();
				BuilderFSQL.append(xmbhFilter).append(" AND DJDYID='").append(ql.getDJDYID()).append("'");
				List<BDCS_FSQL_GZ> fsqls = dao.getDataList(BDCS_FSQL_GZ.class, BuilderFSQL.toString());
				if (fsqls != null && fsqls.size() > 0) {
					for (int ifsql = 0; ifsql < fsqls.size(); ifsql++) {
						BDCS_FSQL_GZ fsql = fsqls.get(ifsql);
						if (!fsql.getQLID().equals(qlid)) {
							fsql.setDYR(builderDYR.toString());
							dao.update(fsql);
						}
					}
				}
			}
			dao.flush();
		}
		// 更新抵押义务人信息qlid
		List<Map> xmbhs = dao.getDataListByFullSql("select xmbh from bdck.bdcs_ql_gz t where qlid='" + qlid + "'");
		if (!xmbhs.isEmpty())
		{
			String xmbh = xmbhs.get(0).get("XMBH").toString();
			List<BDCS_QL_GZ> qls = dao.getDataList(BDCS_QL_GZ.class, "xmbh='" + xmbh + "' and qllx='23'");
			for (BDCS_QL_GZ ql_gz : qls)
			{
				List<BDCS_FSQL_GZ> fsqls = dao.getDataList(BDCS_FSQL_GZ.class, "qlid='" + ql_gz.getId() + "'");
				if (!fsqls.isEmpty())
				{
					BDCS_FSQL_GZ fsql = fsqls.get(0);
					String dyrs = fsql.getDYR();
					if (dyrs != null && !dyrs.isEmpty())
					{
						String[] arr_dyr = dyrs.split(",");
						List<String> arr_dy_ywr_zjh = new ArrayList<String>();
						List<String> arr_dy_ywr_zjzl = new ArrayList<String>();
						for (String dyr : arr_dyr)
						{
							List<BDCS_SQR> qlrs = dao.getDataList(BDCS_SQR.class, "XMBH='" + xmbh + "' and  SQRXM='" + dyr + "'");
							if (!qlrs.isEmpty())
							{
								String dy_ywr_zjh = qlrs.get(0).getZJH();
								String dy_ywr_zjzl = qlrs.get(0).getZJLX();
								arr_dy_ywr_zjh.add(dy_ywr_zjh);
								arr_dy_ywr_zjzl.add(dy_ywr_zjzl);
							}
						}
						fsql.setYWR(dyrs);
						fsql.setYWRZJH(org.hibernate.annotations.common.util.StringHelper.join(",", arr_dy_ywr_zjh.iterator()));
						fsql.setYWRZJZL(org.hibernate.annotations.common.util.StringHelper.join(",", arr_dy_ywr_zjzl.iterator()));
						dao.update(fsql);
					}
				}
			}
			dao.flush();
		}
	}

	/**
	 * 移除权利人
	 */
	@Override
	public void removeQLR(String qlid, String qlrid) {
		CommonDao dao = getCommonDao();
		BDCS_QL_GZ ql = dao.get(BDCS_QL_GZ.class, qlid);
		super.removeqlr(qlrid, qlid);
		// 更新抵押人
		if (!ql.getQLLX().equals(QLLX.DIYQ.Value)) {
			List<BDCS_QLR_GZ> qlrs = dao.getDataList(BDCS_QLR_GZ.class, "QLID ='" + qlid + "' ORDER BY SXH");
			StringBuilder builderDYR = new StringBuilder();
			int indexdyr = 0;
			for (int iqlr = 0; iqlr < qlrs.size(); iqlr++) {
				if (qlrid.equals(qlrs.get(iqlr).getId())) {
					continue;
				}
				if (indexdyr == 0) {
					builderDYR.append(qlrs.get(iqlr).getQLRMC());
				} else {
					builderDYR.append(",").append(qlrs.get(iqlr).getQLRMC());
				}
				indexdyr++;
			}
			List<BDCS_FSQL_GZ> fsqlList = dao.getDataList(BDCS_FSQL_GZ.class, " XMBH='" + getXMBH() + "' and DJDYID='" + ql.getDJDYID() + "'");
			if (fsqlList != null && fsqlList.size() > 0) {
				for (int ifsql = 0; ifsql < fsqlList.size(); ifsql++) {
					BDCS_FSQL_GZ fsql = fsqlList.get(ifsql);
					if (!fsql.getQLID().equals(qlid)) {
						fsql.setDYR(builderDYR.toString());
						dao.update(fsql);
					}
				}
			}
		}
		dao.flush();
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


	public Map<String,String> exportXMLEX(String path, String actinstID) {
		Marshaller mashaller;
		
		Map<String, String> names = new HashMap<String, String>();
		try {
			CommonDao dao = super.getCommonDao();
			String xmbhHql = ProjectHelper.GetXMBHCondition(super.getXMBH());
			List<BDCS_DJDY_GZ> djdys = dao.getDataList(BDCS_DJDY_GZ.class, xmbhHql);
			if (djdys != null && djdys.size() > 0) {
				mashaller = JAXBContext.newInstance(Message.class).createMarshaller();
				String ywh = packageXml.GetYWLSHByYWH(this.getProject_id());
				BDCS_XMXX xmxx = dao.get(BDCS_XMXX.class, super.getXMBH());
				String result = "";
				String result2 = "";
				String combineResult = "";
				for (int i = 0; i < djdys.size(); i++) {
					String bizMsgId = "";
					BDCS_QL_GZ ql1 = null;
					BDCS_QL_GZ ql2 = null;
					BDCS_FSQL_GZ fsql2 = null;
					BDCS_DJDY_GZ djdy = djdys.get(i);
					List<BDCS_QL_GZ> ql_list = super.getCommonDao().getDataList(BDCS_QL_GZ.class, " DJDYID='"+djdy.getDJDYID()+"'");
					if(ql_list!=null && ql_list.size()>0){
						for(BDCS_QL_GZ ql : ql_list){
							if("4".equals(ql.getQLLX())){
								ql1 = ql; 
							}else if("23".equals(ql.getQLLX())){
								ql2 = ql; 
								fsql2 = super.getCommonDao().get(BDCS_FSQL_GZ.class, ql.getFSQLID());
							}
						}
					}
					List<BDCS_QLR_GZ> qlrs = super.getQLRs(ql1.getId());
					List<BDCS_SQR> sqrs = new ArrayList<BDCS_SQR>();
					ProjectServiceImpl serviceImpl = SuperSpringContext.getContext().getBean(ProjectServiceImpl.class);
					sqrs = serviceImpl.getSQRList(super.getXMBH());
					
					if(QLLX.GYJSYDSHYQ_FWSYQ.Value.equals(this.getQllx().Value)){// 房屋
						BDCS_H_XZY h = null;
						boolean flag = false;
						boolean flag2 = false;
						boolean flag3 = false;
						boolean flag4 = false;
						boolean flag5 = false;
						boolean flag6 = false;
						String twoReportAllError = "";
						Map<String, String> xmlError = new HashMap<String, String>();
						Message msg = exchangeFactory.createMessageByYGDJ(); 
						h = dao.get(BDCS_H_XZY.class, djdy.getBDCDYID());
						if(h != null){
							BDCS_SHYQZD_XZ zd = dao.get(BDCS_SHYQZD_XZ.class, h.getZDBDCDYID());
							if(zd != null){
								h.setZDDM(zd.getZDDM());
							}
						}
						super.fillHead(msg, i, ywh,h.getBDCDYH(),h.getQXDM(),ql1.getLYQLID());
						bizMsgId = msg.getHead().getBizMsgID();
						msg.getHead().setRecType("7000101");
						msg.getHead().setParcelID(StringHelper.formatObject(h.getZDDM()));
						msg.getHead().setEstateNum(StringHelper.formatObject(h.getBDCDYH()));
//						msg.getHead().setPreEstateNum(StringHelper.formatObject(h.getBDCDYH()));
						if(h != null && !StringUtils.isEmpty(h.getQXDM())){
							msg.getHead().setAreaCode(h.getQXDM());
						}
						/*********************************************房屋预告部分************************************************/
						try {
							if (djdy != null) {
								fillCommonPartData(msg,h,ql1,fsql2,qlrs,sqrs,ywh,xmxx,actinstID);
							}
						}catch(Exception e){
							e.printStackTrace();
						}
						String fullname = "Biz" + bizMsgId+ ".xml";
						mashaller.marshal(msg, new File(path + fullname));
						result = uploadFile(path + fullname ,ConstValue.RECCODE.YG_ZXDJ.Value,actinstID,djdy.getDJDYID(),ql1.getId())+" \n ";
						names.put(djdy.getDJDYID(),bizMsgId+ ".xml");
						if(result.equals("")|| result==null){
							flag = true;
						}
						if(!"1".equals(result) && result.indexOf("success") == -1){ //xml本地校验不通过时
							flag3 = true;
						}
						if(!StringUtils.isEmpty(result) && result.indexOf("success") > -1 && !names.containsKey("reccode")){
							flag5 = true;
						}
						/**********************************************房屋预抵押部分*************************************************/
						Message msg2 = exchangeFactory.createMessageByYDYDJ(); 
						super.fillHead(msg2, i, ywh,h.getBDCDYH(),h.getQXDM(),ql2.getLYQLID());
						bizMsgId = msg2.getHead().getBizMsgID();
						msg2.getHead().setRecType("7000101");
						msg2.getHead().setParcelID(StringHelper.formatObject(h.getZDDM()));
						msg2.getHead().setEstateNum(StringHelper.formatObject(h.getBDCDYH()));
//						msg2.getHead().setPreEstateNum(StringHelper.formatObject(h.getBDCDYH()));
						if(h != null && !StringUtils.isEmpty(h.getQXDM())){
							msg2.getHead().setAreaCode(h.getQXDM());
						}
						try {
							if (djdy != null) {
								fillCommonPartData(msg2,h,ql2,fsql2,qlrs,sqrs,ywh,xmxx,actinstID);
								// *预告登记_抵押权
								QLFQLDYAQ dyaq = msg2.getData().getQLFQLDYAQ();
								packageXml.getQLFQLDYAQ(dyaq, null, ql2, fsql2, ywh, h);
								msg2.getData().setQLFQLDYAQ(dyaq);
							}
						}catch(Exception e){
							e.printStackTrace();
						}
						// 搞不懂为啥要加1
//							long bizMsgId2 = StringHelper.getLong(bizMsgId)+1;
						long bizMsgId2 = StringHelper.getLong(bizMsgId);
						String fullname2 = "Biz" + StringHelper.formatObject(bizMsgId2)+ ".xml";
						mashaller.marshal(msg2, new File(path + fullname2));
						result2 =  uploadFile(path + fullname2 ,ConstValue.RECCODE.YG_ZXDJ.Value,actinstID,djdy.getDJDYID(),ql2.getId());
						names.put(djdy.getDJDYID(),StringHelper.formatObject(bizMsgId2)+ ".xml");
						if("".equals(result2)|| result2==null){
							flag2 = true;
						}
						if(!"1".equals(result2) && result2.indexOf("success") == -1){ //xml本地校验不通过时
							flag4 = true;
						}
						if(!StringUtils.isEmpty(result) && result.indexOf("success") > -1 && !names.containsKey("reccode")){
							flag6 = true;
						}
						// 两部分总结
						// result 为空的情况
						String allreportname ="";
						if(flag){
							twoReportAllError = "该组合流程中“预告部分”的问题：连接SFTP失败"+"\n";
							allreportname = "Biz" + msg.getHead().getBizMsgID() + ".xml";
						}
						if(flag2){
							twoReportAllError += "该组合流程中“预抵押部分”的问题：连接SFTP失败 "+"\n";
							if(!flag){
								allreportname += fullname2 ;
							}else{
								allreportname += "  ,  " + fullname2 ;
							}
						}
						if(flag || flag2){
							twoReportAllError += "请检查服务器和前置机的连接是否正常";	
							xmlError.put("error", twoReportAllError);
							YwLogUtil.addSjsbResult(allreportname, "", twoReportAllError, ConstValue.SF.NO.Value, ConstValue.RECCODE.YG_ZXDJ.Value,ProjectHelper.getpRroinstIDByActinstID(actinstID));
							return xmlError;
						}
						// result 为1的情况
						if(flag3 || flag4){
							if(flag3 && !flag4){
								combineResult = "该组合流程中“预告部分”的问题：" + result;
							}else if(!flag3 && flag4){
								combineResult = "该组合流程中“预抵押部分”的结果为：" + result2;
							}else {// flag3,flag4同时为true
								combineResult = "该组合流程中“预告部分”的问题：" + result  +"\n" +  "该组合流程中“预抵押部分”的结果为：" + result2 ;
							}
							xmlError.put("error", result);
							logger.error(combineResult);
//								return xmlError;
						}
						if(flag5 || flag6){
							if(flag5 && !flag6){
								combineResult = result;
							}else if(!flag5 && flag6){
								combineResult = result2;
							}else{
								combineResult = result+"\n"+result2;
							}
							names.put("reccode", combineResult);
						}
						
					}else {
						// 土地是否有预告和预抵押的业务？一般来讲都是房屋有这个业务！！
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return names;
	}


	private void fillCommonPartData(Message msg, BDCS_H_XZY h, BDCS_QL_GZ ql, BDCS_FSQL_GZ fsql, List<BDCS_QLR_GZ> qlrs, List<BDCS_SQR> sqrs, String ywh, BDCS_XMXX xmxx, String actinstID) {
		// *预告登记
		QLFQLYGDJ ygdj = msg.getData().getQLFQLYGDJ();
		ygdj = packageXml.getQLFQLYGDJ(ygdj, h, ql,fsql, ywh);
		msg.getData().setQLFQLYGDJ(ygdj);
		// *申请受理
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
		// *权利人
		List<ZTTGYQLR> zttqlr = msg.getData().getGYQLR();
		zttqlr = packageXml.getZTTGYQLRs(zttqlr, qlrs, null, ql, h, null, null, null);
		msg.getData().setGYQLR(zttqlr);
		// * 申请人属性
		List<DJFDJSQR> djsqrs = msg.getData().getDJSQR();
		djsqrs = packageXml.getDJSQRs(djsqrs, sqrs, h.getYSDM(), ywh, h.getBDCDYH());
		msg.getData().setDJSQR(djsqrs);
	}

	/**
	 * 获得抵押物类型
	 * @作者 diaoliwei
	 * @创建时间 2015年7月25日下午10:18:25
	 * @param bdcdylx
	 * @return
	 */
	protected String getDYBDCLXfromBDCDYLX(BDCDYLX bdcdylx) {
		String dybdclx = "";
		if (bdcdylx.equals(BDCDYLX.SHYQZD) || bdcdylx.equals(BDCDYLX.SYQZD)) {
			dybdclx = "1";
		} else if (bdcdylx.equals(BDCDYLX.H) || bdcdylx.equals(BDCDYLX.ZRZ)) {
			dybdclx = "2";
		} else if (bdcdylx.equals(BDCDYLX.LD)) {
			dybdclx = "3";
		} else if (bdcdylx.equals(BDCDYLX.GZW) || bdcdylx.equals(BDCDYLX.YCH) || bdcdylx.equals(BDCDYLX.ZRZ)) {
			dybdclx = "4";
		} else if (bdcdylx.equals(BDCDYLX.HY)) {
			dybdclx = "5";
		} else {
			dybdclx = "7";
		}
		return dybdclx;
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
				Rights bdcql = RightsTools.loadRightsByDJDYID(dyly, getXMBH(), djdy.getDJDYID());
				SubRights bdcfsql = RightsTools.loadSubRightsByRightsID(dyly, bdcql.getId());
				List<RightsHolder> bdcqlrs = RightsHolderTools.loadRightsHolders(dyly, djdy.getDJDYID(), getXMBH());
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
	
	/* 组合流程中所有权+两个抵押权上报方式
	 *  
	 */
	@Override
	public Map<String,String> exportXML(String path, String actinstID) {
		Marshaller mashaller;
		Map<String, String> names = new HashMap<String, String>();
		try {
			CommonDao dao = super.getCommonDao();
			String xmbhHql = ProjectHelper.GetXMBHCondition(super.getXMBH());
			List<BDCS_DJDY_GZ> djdys = dao.getDataList(BDCS_DJDY_GZ.class, xmbhHql);
			if (djdys != null && djdys.size() > 0) {
				mashaller = JAXBContext.newInstance(Message.class).createMarshaller();
				String ywh = packageXml.GetYWLSHByYWH(this.getProject_id());
				BDCS_XMXX xmxx = dao.get(BDCS_XMXX.class, super.getXMBH());
				String result = "";
				String result2 = "";
				String combineResult = "";
				for (int i = 0; i < djdys.size(); i++) {
					String bizMsgId = "";
					BDCS_QL_GZ ql1 = null;
					BDCS_QL_GZ ql2 = null;
					BDCS_FSQL_GZ fsql2 = null;
					BDCS_DJDY_GZ djdy = djdys.get(i);
					List<BDCS_QL_GZ> ql_list = super.getCommonDao().getDataList(BDCS_QL_GZ.class, " DJDYID='"+djdy.getDJDYID()+"' AND QLLX='4' AND "+xmbhHql);
					
					
					if(ql_list!=null && ql_list.size()>0){
						BDCS_QL_GZ ql_gz = ql_list.get(0);
						ql1= ql_gz;
					}
					List<BDCS_QLR_GZ> qlrs = super.getQLRs(ql1.getId());
					List<BDCS_SQR> sqrs = new ArrayList<BDCS_SQR>();
					ProjectServiceImpl serviceImpl = SuperSpringContext.getContext().getBean(ProjectServiceImpl.class);
					sqrs = serviceImpl.getSQRList(super.getXMBH());
					
				    if(QLLX.GYJSYDSHYQ_FWSYQ.Value.equals(this.getQllx().Value)){// 房屋
							BDCS_H_XZY h = null;
							boolean flag = false;
							boolean flag2 = false;
							boolean flag3 = false;
							boolean flag4 = false;
							boolean flag5 = false;
							boolean flag6 = false;
							String twoReportAllError = "";
							Map<String, String> xmlError = new HashMap<String, String>();
							Message msg = exchangeFactory.createMessageByYGDJ(); 
							h = dao.get(BDCS_H_XZY.class, djdy.getBDCDYID());
							if(h != null){
								BDCS_SHYQZD_XZ zd = dao.get(BDCS_SHYQZD_XZ.class, h.getZDBDCDYID());
								if(zd != null){
									h.setZDDM(zd.getZDDM());
								}
							}
							super.fillHead(msg, i, ywh,h.getBDCDYH(),h.getQXDM(),ql1.getLYQLID());
							bizMsgId = msg.getHead().getBizMsgID();
							msg.getHead().setRecType("7000101");
							msg.getHead().setParcelID(StringHelper.formatObject(h.getZDDM()));
							msg.getHead().setEstateNum(StringHelper.formatObject(h.getBDCDYH()));
//							msg.getHead().setPreEstateNum(StringHelper.formatObject(h.getBDCDYH()));
						    if(h != null && !StringUtils.isEmpty(h.getQXDM())){
								msg.getHead().setAreaCode(h.getQXDM());
							}
						  /*********************************************房屋预告部分************************************************/
							try {
							    if (djdy != null) {
							    	fillCommonPartData(msg,h,ql1,fsql2,qlrs,sqrs,ywh,xmxx,actinstID);
							    }
							}catch(Exception e){
								e.printStackTrace();
							}
							String fullname = "Biz" + bizMsgId+ ".xml";
							mashaller.marshal(msg, new File(path + fullname));
							result = uploadFile(path + fullname ,ConstValue.RECCODE.YG_ZXDJ.Value,actinstID,djdy.getDJDYID(),ql1.getId())+" \n ";
							names.put(djdy.getDJDYID(),bizMsgId+ ".xml");
							if(result.equals("")|| result==null){
								flag = true;
							}
	                        if(!"1".equals(result) && result.indexOf("success") == -1){ //xml本地校验不通过时
	                        	flag3 = true;
							}
	                        if(!StringUtils.isEmpty(result) && result.indexOf("success") > -1 && !names.containsKey("reccode")){
	                        	flag5 = true;
	                        }
						  /**********************************************房屋预抵押部分*************************************************/
	    					List<BDCS_QL_GZ> dyql_list = super.getCommonDao().getDataList(BDCS_QL_GZ.class, " DJDYID='"+djdy.getDJDYID()+"' AND QLLX='23' AND "+xmbhHql);
	                        String fullname2 ="";
	                        for (BDCS_QL_GZ dyq_gz : dyql_list) {
		   						ql2= dyq_gz;
		   						fsql2 = super.getCommonDao().get(BDCS_FSQL_GZ.class, dyq_gz.getFSQLID());
			                       
	   						    Message msg2 = exchangeFactory.createMessageByYDYDJ(); 
		                        super.fillHead(msg2, i, ywh,h.getBDCDYH(),h.getQXDM(),ql2.getLYQLID());
								bizMsgId = msg2.getHead().getBizMsgID();
								msg2.getHead().setRecType("7000101");
								msg2.getHead().setParcelID(StringHelper.formatObject(h.getZDDM()));
								msg2.getHead().setEstateNum(StringHelper.formatObject(h.getBDCDYH()));
//								msg2.getHead().setPreEstateNum(StringHelper.formatObject(h.getBDCDYH()));
							    if(h != null && !StringUtils.isEmpty(h.getQXDM())){
									msg2.getHead().setAreaCode(h.getQXDM());
								}
								try {
								    if (djdy != null) {
								    	fillCommonPartData(msg2,h,ql2,fsql2,qlrs,sqrs,ywh,xmxx,actinstID);
								    	// *预告登记_抵押权
								    	QLFQLDYAQ dyaq = msg2.getData().getQLFQLDYAQ();
								    	packageXml.getQLFQLDYAQ(dyaq, null, ql2, fsql2, ywh, h);
								    	msg2.getData().setQLFQLDYAQ(dyaq);
								    }
								}catch(Exception e){
									e.printStackTrace();
								}
								// 搞不懂为啥要加1
	//							long bizMsgId2 = StringHelper.getLong(bizMsgId)+1;
								long bizMsgId2 = StringHelper.getLong(bizMsgId);
								fullname2 = "Biz" + StringHelper.formatObject(bizMsgId2)+ ".xml";
								mashaller.marshal(msg2, new File(path + fullname2));
								result2 =  uploadFile(path + fullname2 ,ConstValue.RECCODE.YG_ZXDJ.Value,actinstID,djdy.getDJDYID(),ql2.getId());
								names.put(djdy.getDJDYID(),StringHelper.formatObject(bizMsgId2)+ ".xml");
								if("".equals(result2)|| result2==null){
									flag2 = true;
								}
								if(!"1".equals(result2) && result2.indexOf("success") == -1){ //xml本地校验不通过时
			                        flag4 = true;
								}
								if(!StringUtils.isEmpty(result) && result.indexOf("success") > -1 && !names.containsKey("reccode")){
			                        flag6 = true;
			                    }
							
	                        }
							
							// 两部分总结
							// result 为空的情况
							String allreportname ="";
							if(flag){
								twoReportAllError = "该组合流程中“预告部分”的问题：连接SFTP失败"+"\n";
								allreportname = "Biz" + msg.getHead().getBizMsgID() + ".xml";
							}
							if(flag2){
								twoReportAllError += "该组合流程中“预抵押部分”的问题：连接SFTP失败 "+"\n";
								if(!flag){
									allreportname += fullname2 ;
								}else{
									allreportname += "  ,  " + fullname2 ;
								}
							}
							if(flag || flag2){
								twoReportAllError += "请检查服务器和前置机的连接是否正常";	
								xmlError.put("error", twoReportAllError);
								YwLogUtil.addSjsbResult(allreportname, "", twoReportAllError, ConstValue.SF.NO.Value, ConstValue.RECCODE.YG_ZXDJ.Value,ProjectHelper.getpRroinstIDByActinstID(actinstID));
								return xmlError;
							}
							// result 为1的情况
							if(flag3 || flag4){
								if(flag3 && !flag4){
									combineResult = "该组合流程中“预告部分”的问题：" + result;
								}else if(!flag3 && flag4){
									combineResult = "该组合流程中“预抵押部分”的结果为：" + result2;
								}else {// flag3,flag4同时为true
									combineResult = "该组合流程中“预告部分”的问题：" + result  +"\n" +  "该组合流程中“预抵押部分”的结果为：" + result2 ;
								}
								xmlError.put("error", result);
								logger.error(combineResult);
//								return xmlError;
							}
							if(flag5 || flag6){
								if(flag5 && !flag6){
									combineResult = result;
								}else if(!flag5 && flag6){
									combineResult = result2;
								}else{
									combineResult = result+"\n"+result2;
								}
								names.put("reccode", combineResult);
							}
							
					}else {
						// 土地是否有预告和预抵押的业务？一般来讲都是房屋有这个业务！！
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace(); 
		}
		return names;
	}
}
