package com.supermap.realestate.registration.handlerImpl;
import java.util.Date;
import java.util.List;
import com.supermap.realestate.registration.ViewClass.ProjectInfo;
import com.supermap.realestate.registration.handler.DJHandler;
import com.supermap.realestate.registration.model.BDCS_DJDY_GZ;
import com.supermap.realestate.registration.model.BDCS_DJDY_XZ;
import com.supermap.realestate.registration.model.BDCS_QL_GZ;
import com.supermap.realestate.registration.model.BDCS_YSBZGG;
import com.supermap.realestate.registration.model.interfaces.Rights;
import com.supermap.realestate.registration.tools.RightsTools;
import com.supermap.realestate.registration.util.ConstValue.DJDYLY;
import com.supermap.realestate.registration.util.ProjectHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;

/*
 1、国有建设用地使用权换证登记
 2、集体建设用地使用权换证登记（未配置）
 3、宅基地使用权换证登记（未配置）
 4、国有建设用地使用权/房屋所有权换证登记
 5、集体建设用地使用权/房屋所有权换证登记（未配置）
 6、宅基地使用权/房屋所有权换证登记（未配置）
 */
/**
 * 换证补正-需要遗失补证公告的
 * @author 海豹
 *
 */
public class BZDJHandler_YSBZ extends BZDJHandler implements DJHandler {

	/**
	 * 构造函数
	 * 
	 * @param info
	 */
	public BZDJHandler_YSBZ(ProjectInfo info) {
		super(info);
	}

	/**
	 * 添加不动产单元
	 */
	@Override
	public boolean addBDCDY(String qlids) {
		boolean bsuccess = false;
	//不用做判断了,直接用吸老师
//		if(StringHelper.isEmpty(qlids)){
//			return false;
//		}
//		String[] listqlid = qlids.split(",");
//		if (listqlid == null || listqlid.length <= 0)
//			return false;
//		for(String qlid :listqlid)
//		{
//			bsuccess=validateYsbz(qlid);
//			if(!bsuccess)
//			{
//				return bsuccess;
//			}
//		}
		bsuccess = super.addBDCDY(qlids);
		return bsuccess;
	}
	/**
	 * 写入登记簿(还更新了遗失补证公告的状态)
	 */
	@Override
	public boolean writeDJB() {
		String StrSqlXmbh=ProjectHelper.GetXMBHCondition(getXMBH());
		List<BDCS_DJDY_GZ> lstdjdy=getCommonDao().getDataList(BDCS_DJDY_GZ.class, StrSqlXmbh);
		for(BDCS_DJDY_GZ bdcs_djdy_gz :lstdjdy)
		{
			List<BDCS_QL_GZ> lstql =getCommonDao().getDataList(BDCS_QL_GZ.class, "DJDYID ='"+bdcs_djdy_gz.getDJDYID()+"' and XMBH ='"+getXMBH()+"'");
			for(BDCS_QL_GZ bdcs_ql_gz :lstql)
			{
				//更新遗失补证公告中的状态
				List<BDCS_YSBZGG> bdcs_ysbzggs=getCommonDao().getDataList(BDCS_YSBZGG.class,  "BDCDYID ='"+bdcs_djdy_gz.getBDCDYID()+"' and QLID ='"+bdcs_ql_gz.getLYQLID()+"'");
				for(BDCS_YSBZGG bdcs_ysbzgg :bdcs_ysbzggs)
				{
					bdcs_ysbzgg.setBZZT("1");
					getCommonDao().update(bdcs_ysbzgg);
				}
			}
		}
		super.writeDJB();
		return true;
	}
	/**
	 * 验证是否通过遗失补证后需要换证
	 * @作者 海豹
	 * @创建时间 2015年10月30日上午10:25:11
	 * @param qlid
	 * @return
	 */
	private boolean validateYsbz(String qlid) {
		CommonDao dao = getCommonDao();
		Rights rights = RightsTools.loadRights(DJDYLY.XZ, qlid);
		if (rights != null) {
			List<BDCS_DJDY_XZ> lstdjdy = dao.getDataList(BDCS_DJDY_XZ.class,
					"DJDYID ='" + rights.getDJDYID() + "'");
			if (lstdjdy != null && lstdjdy.size() > 0) {
				BDCS_DJDY_XZ bdcs_djdy_xz = lstdjdy.get(0);
				String hqlCondition = "BDCDYID ='" + bdcs_djdy_xz.getBDCDYID()
						+ "' and QLID='" + qlid+"'" ;
				List<BDCS_YSBZGG> bdcs_ysbzggs = dao.getDataList(
						BDCS_YSBZGG.class, hqlCondition);
				if (bdcs_ysbzggs != null && bdcs_ysbzggs.size() > 0) {
					BDCS_YSBZGG bdcs_ysbzgg = bdcs_ysbzggs.get(0);
					String bzzt = bdcs_ysbzgg.getBZZT();
					if ("1".equals(bzzt))// 换证状态：0表示未补证，1表示已补证
					{
						super.setErrMessage("该单元已在遗失补证公告结束后，已补证");
						return false;
					} else {
						Date jssj = bdcs_ysbzgg.getGGJSSJ();
						Date currentdate = new Date();
						long l = jssj.getTime() - currentdate.getTime();
						long day = l / (24 * 60 * 60 * 1000);
						if (day >= 0) {
							day = day + 1;
						}
						if (day > 0) {
							super.setErrMessage("离公告结束时间还有：" + day + "天");
							return false;
						}
					}
				} else {
					super.setErrMessage("该单元未做遗失补证公告,请先申请遗失补证公告");
					return false;
				}
			}
		}
		return true;
	}
}
