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
import com.supermap.realestate.registration.model.BDCS_DYXZ;
import com.supermap.realestate.registration.model.BDCS_FSQL_GZ;
import com.supermap.realestate.registration.model.BDCS_H_GZ;
import com.supermap.realestate.registration.model.BDCS_H_XZ;
import com.supermap.realestate.registration.model.BDCS_H_XZY;
import com.supermap.realestate.registration.model.BDCS_QLR_XZ;
import com.supermap.realestate.registration.model.BDCS_QL_GZ;
import com.supermap.realestate.registration.model.BDCS_QL_XZ;
import com.supermap.realestate.registration.model.BDCS_SHYQZD_GZ;
import com.supermap.realestate.registration.model.BDCS_SHYQZD_XZ;
import com.supermap.realestate.registration.model.BDCS_SLLM_GZ;
import com.supermap.realestate.registration.model.BDCS_SYQZD_GZ;
import com.supermap.realestate.registration.model.BDCS_XMXX;
import com.supermap.realestate.registration.model.BDCS_ZH_GZ;
import com.supermap.realestate.registration.model.BDCS_ZRZ_GZ;
import com.supermap.realestate.registration.model.BDCS_ZRZ_XZ;
import com.supermap.realestate.registration.model.WFD_MAPPING;
import com.supermap.realestate.registration.model.interfaces.House;
import com.supermap.realestate.registration.model.interfaces.RealUnit;
import com.supermap.realestate.registration.model.interfaces.Rights;
import com.supermap.realestate.registration.model.interfaces.RightsHolder;
import com.supermap.realestate.registration.model.interfaces.SubRights;
import com.supermap.realestate.registration.model.xmlExportmodel.MessageExport;
import com.supermap.realestate.registration.tools.RightsHolderTools;
import com.supermap.realestate.registration.tools.RightsTools;
import com.supermap.realestate.registration.tools.UnitTools;
import com.supermap.realestate.registration.util.ConfigHelper;
import com.supermap.realestate.registration.util.ConstValue;
import com.supermap.realestate.registration.util.ProjectHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.realestate.registration.util.exportXmlUtil;
import com.supermap.realestate.registration.util.ConstValue.BDCDYLX;
import com.supermap.realestate.registration.util.ConstValue.CZFS;
import com.supermap.realestate.registration.util.ConstValue.DJDYLY;
import com.supermap.realestate.registration.util.ConstValue.DJLX;
import com.supermap.realestate.registration.util.ConstValue.QLLX;
import com.supermap.realestate.registration.util.ConstValue.SF;
import com.supermap.realestate.registration.util.ConstValue.SQRLB;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;

/**
 * 换证登记+抵押权初始登记
 * 
 * @author zhaomengfan
 *
 */
public class HZ_SYQ_DY_DJHandler extends DJHandlerBase implements DJHandler {

	public HZ_SYQ_DY_DJHandler(ProjectInfo info) {
		super(info);
	}

	@Override
	public boolean addBDCDY(String bdcdyid) {
		boolean bsuccess = false;
		CommonDao dao = getCommonDao();
		// 拷贝登记单元
		BDCS_DJDY_GZ bdcs_djdy_gz = super.createDJDYfromXZ(bdcdyid);
		bdcs_djdy_gz.setBDCDYID(bdcdyid);
		bdcs_djdy_gz.setId(getPrimaryKey());
		bdcs_djdy_gz.setLY(DJDYLY.XZ.Value);
		bdcs_djdy_gz.setXMBH(getXMBH());
		dao.save(bdcs_djdy_gz);
		RealUnit unit = null;
		try {
			unit = UnitTools.loadUnit(this.getBdcdylx(),
					DJDYLY.initFrom(bdcs_djdy_gz.getLY()),
					bdcs_djdy_gz.getBDCDYID());
		} catch (Exception e) {
			
		}
		// 拷贝权利信息
		StringBuilder builder = new StringBuilder();
		builder.append(" DJDYID='").append(bdcs_djdy_gz.getDJDYID()).append("' AND QLLX='").append(getQllx().Value).append("'");
		List<BDCS_QL_XZ> qls = dao.getDataList(BDCS_QL_XZ.class,
				builder.toString());
		if (qls != null && qls.size() > 0) {
			String fj = "";
			String newqzh = "";
			BDCS_QL_GZ bdcs_ql_gz = null;
			String workflowcode = ProjectHelper.getWorkflowCodeByProjectID(this
					.getProject_id());
			List<WFD_MAPPING> listCode = getCommonDao().getDataList(
					WFD_MAPPING.class, "WORKFLOWCODE='" + workflowcode + "'");
			if (listCode != null && listCode.size() > 0) {
				newqzh = listCode.get(0).getNEWQZH();
			}
			if (SF.NO.Value.equals(newqzh)) {
				// 拷贝权利信息（权证号不为空）
				bdcs_ql_gz = super.CopyQLXXFromXZ(qls.get(0).getId());
			} else {
				// 拷贝权利信息（权证号为空）
				bdcs_ql_gz = super.CopyQLXXFromXZExceptBDCQZH(qls.get(0)
						.getId());
			}
			if (bdcs_ql_gz != null) {
				String sql = " WORKFLOWCODE='" + workflowcode + "'";
				List<WFD_MAPPING> mappings = dao.getDataList(WFD_MAPPING.class,
						sql);
				if (mappings != null && mappings.size() > 0) {
					WFD_MAPPING maping = mappings.get(0);
					if (("1").equals(maping.getISINITATATUS())) {
						fj = bdcs_ql_gz.getFJ();
						fj = getStatus(fj, bdcs_ql_gz.getDJDYID(), bdcdyid,
								this.getBdcdylx().Value);
						bdcs_ql_gz.setFJ(fj);
						dao.update(bdcs_ql_gz);
					}
				}
				//生成抵押权
				// 生成权利信息
				BDCS_QL_GZ ql = super.createQL(bdcs_djdy_gz, unit);
				ql.setCZFS(CZFS.GTCZ.Value);
				ql.setLYQLID(bdcs_ql_gz.getId());
				ql.setDJLX(DJLX.CSDJ.Value);
				ql.setQLLX(QLLX.DIYQ.Value);
				
				String xzqhdm=ConfigHelper.getNameByValue("XZQHDM");
				if(!StringHelper.isEmpty(xzqhdm) && xzqhdm.startsWith("1301")){
					ql.setDJYY("借款");
				}
				// 生成附属权利
				BDCS_FSQL_GZ fsql = super.createFSQL(bdcs_djdy_gz.getDJDYID());
				ql.setFSQLID(fsql.getId());
				fsql.setQLID(ql.getId());
				// 把附属权利里边的抵押人和抵押不动产类型写上
				String qllxarray = "('3','4','5','6','7','8','10','11','12','15','36')";
				String hql = "QLID IN(SELECT id FROM BDCS_QL_XZ WHERE  DJDYID='" + bdcs_djdy_gz.getDJDYID() + "' AND QLLX IN " + qllxarray + ") ORDER BY SXH";
				List<BDCS_QLR_XZ> list = dao.getDataList(BDCS_QLR_XZ.class, hql);
				if (list != null && list.size() > 0) {
					String qlrnames = "";
					for (int i = 0; i < list.size(); i++) {
						qlrnames += list.get(i).getQLRMC() + ",";
					}
					if (!StringUtils.isEmpty(qlrnames)) {
						qlrnames = qlrnames.substring(0, qlrnames.length() - 1);
						fsql.setDYR(qlrnames);
					}
				}
				// 设置抵押物类型
				fsql.setDYWLX(getDYBDCLXfromBDCDYLX(getBdcdylx()));
				fsql.setZJJZWZL(unit.getZL());
				int dysw=RightsTools.getMaxMortgageSWS(bdcs_djdy_gz.getDJDYID());
				fsql.setDYSW(dysw+1);
				dao.save(ql);
				dao.save(fsql);
			}
			// 拷贝权利人到申请人
			super.CopySQRFromXZQLR(bdcs_djdy_gz.getDJDYID(), getQllx().Value,
					getXMBH(), qls.get(0).getId(), SQRLB.JF.Value);
		}
		dao.flush();
		bsuccess = true;
		return bsuccess;
	}

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
		String strSqlXMBH = ProjectHelper.GetXMBHCondition(getXMBH());
		List<BDCS_DJDY_GZ> djdys = getCommonDao().getDataList(BDCS_DJDY_GZ.class, strSqlXMBH);
		if (djdys != null && djdys.size() > 0) {
			for (int idjdy = 0; idjdy < djdys.size(); idjdy++) {
				BDCS_DJDY_GZ bdcs_djdy_gz = djdys.get(idjdy);
				if (bdcs_djdy_gz != null) {
					String djdyid = bdcs_djdy_gz.getDJDYID();
					
					super.CopyGZQLToXZAndLS(djdyid);
					super.CopyGZQLRToXZAndLS(djdyid);
					super.CopyGZQDZRToXZAndLS(djdyid);
					super.CopyGZZSToXZAndLS(djdyid);
					BDCDYLX dylx = ProjectHelper.GetBDCDYLX(getXMBH());
					// 更新单元抵押状态
					SetDYDYZT(bdcs_djdy_gz.getBDCDYID(), dylx, "0");
				}
			}
		}
		this.SetSFDB();
		getCommonDao().flush();
		super.DyLimit();
		super.alterCachedXMXX();
		return true;
	}

	@Override
	public boolean removeBDCDY(String bdcdyid) {
		boolean bsuccess = false;
		// 找到登记单元表，移除记录
		CommonDao baseCommonDao = this.getCommonDao();
		BDCS_DJDY_GZ djdy = super.removeDJDY(bdcdyid);// list.get(0);
		if (djdy != null) {
			String djdyid = djdy.getDJDYID();
			// 删除权利关联申请人
			super.RemoveSQRByQLID(djdyid, getXMBH());
			// 删除权利、附属权利、权利人、证书、权地证人关系
			String _hqlCondition = MessageFormat.format(
					"XMBH=''{0}'' AND DJDYID=''{1}''", getXMBH(), djdyid);
			RightsTools.deleteRightsAllByCondition(DJDYLY.GZ, _hqlCondition);
		}
		baseCommonDao.flush();
		bsuccess = true;
		return bsuccess;
	}

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
				StringBuilder BuilderQL=new StringBuilder();
				BuilderQL.append(xmbhFilter).append(" AND DJDYID='").append(djdy.getDJDYID()).append("'");
				List<BDCS_QL_GZ> qls=dao.getDataList(BDCS_QL_GZ.class, BuilderQL.toString());
				if(qls!=null){
					for(int iql=0;iql<qls.size();iql++){
						BDCS_QL_GZ ql = qls.get(iql);
						if(ql.getQLLX().equals(QLLX.DIYQ.Value)){
							tree.setDIYQQlid(ql.getId());
						}else{
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
				tree.setZl(zl);
				list.add(tree);
			}
		}
		return list;
	}

	@Override
	public void addQLRbySQRArray(String qlid, Object[] sqrids) {
		super.addQLRbySQRs(qlid, sqrids);
	}

	@Override
	public void removeQLR(String qlid, String qlrid) {
		super.removeqlr(qlrid, qlid);
	}

	@Override
	public String getError() {
		return super.getErrMessage();
	}

	@Override
	public void SendMsg(String bljc) {
		BDCS_XMXX xmxx = getCommonDao().get(BDCS_XMXX.class, super.getXMBH());
		String xmbhFilter = ProjectHelper.GetXMBHCondition(super.getXMBH());
		List<BDCS_DJDY_GZ> djdys = getCommonDao().getDataList(
				BDCS_DJDY_GZ.class, xmbhFilter);
		if (djdys != null && djdys.size() > 0) {
			for (int idjdy = 0; idjdy < djdys.size(); idjdy++) {
				BDCS_DJDY_GZ djdy = djdys.get(idjdy);
				ConstValue.BDCDYLX dylx = ConstValue.BDCDYLX.initFrom(djdy
						.getBDCDYLX());
				ConstValue.DJDYLY dyly = ConstValue.DJDYLY.initFrom(djdy
						.getLY());
				RealUnit bdcdy = UnitTools.loadUnit(dylx, dyly,
						djdy.getBDCDYID());
				Rights bdcql = RightsTools.loadRightsByDJDYID(
						ConstValue.DJDYLY.GZ, getXMBH(), djdy.getDJDYID());
				SubRights bdcfsql = RightsTools.loadSubRightsByRightsID(
						ConstValue.DJDYLY.GZ, bdcql.getId());
				List<RightsHolder> bdcqlrs = RightsHolderTools
						.loadRightsHolders(ConstValue.DJDYLY.GZ,
								djdy.getDJDYID(), getXMBH());
				MessageExport msg = super.getShareMsgTools().GetMsg(bdcdy,
						bdcql, bdcfsql, bdcqlrs, bljc, xmxx);
				super.getShareMsgTools().SendMsg(msg, idjdy + 1,
						djdy.getBDCDYLX(), xmxx);
			}
		}
	}

	@SuppressWarnings("rawtypes")
	private String getStatus(String fj, String djdyid, String bdcdyid,
			String bdcdylx) {
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
		List<Map> qls_gz = getCommonDao().getDataListByFullSql(
				builder.toString());
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
		List<Rights> qls_xz = RightsTools.loadRightsByCondition(DJDYLY.XZ,
				"DJDYID='" + djdyid + "'");
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

		List<BDCS_DYXZ> list_limit = getCommonDao().getDataList(
				BDCS_DYXZ.class,
				"YXBZ<>'2' AND BDCDYID='" + bdcdyid + "' AND BDCDYLX='"
						+ bdcdylx + "' ORDER BY YXBZ ");
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
		if (StringHelper.isEmpty(tmp)) {
			if (status.getMortgageState().contains("已")
					|| status.getMortgageState().contains("正在"))
				tmp = status.getMortgageState() + ",";
			if (status.getLimitState().contains("已")
					|| status.getLimitState().contains("正在"))
				tmp += status.getLimitState();
			fj = tmp;
		} else {
			if (status.getMortgageState().contains("已")
					|| status.getMortgageState().contains("正在"))
				tmp = status.getMortgageState() + ",";
			if (status.getLimitState().contains("已")
					|| status.getLimitState().contains("正在"))
				tmp += status.getLimitState();
			fj += tmp;
		}
		return fj;
	}
	
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
	
	@Override
	public Map<String, String> exportXML(String path, String actinstID) {
		return exportXmlUtil.createXMLAndUp(path,actinstID);
	}

}
