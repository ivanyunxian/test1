package com.supermap.wisdombusiness.synchroinline.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import com.supermap.realestate.registration.ViewClass.ProjectInfo;
import com.supermap.realestate.registration.model.BDCS_DJDY_GZ;
import com.supermap.realestate.registration.model.BDCS_XMXX;
import com.supermap.realestate.registration.model.interfaces.House;
import com.supermap.realestate.registration.model.interfaces.RealUnit;
import com.supermap.realestate.registration.model.interfaces.Rights;
import com.supermap.realestate.registration.model.interfaces.RightsHolder;
import com.supermap.realestate.registration.model.interfaces.TDYT;
import com.supermap.realestate.registration.model.interfaces.UseLand;
import com.supermap.realestate.registration.tools.RightsHolderTools;
import com.supermap.realestate.registration.tools.RightsTools;
import com.supermap.realestate.registration.tools.UnitTools;
import com.supermap.realestate.registration.util.ConstValue.BDCDYLX;
import com.supermap.realestate.registration.util.ConstValue.DJDYLY;
import com.supermap.realestate.registration.util.ConstValue.DJLX;
import com.supermap.realestate.registration.util.ConstValue.QLLX;
import com.supermap.realestate.registration.util.ProjectHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.wisdombusiness.core.SuperSpringContext;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
public class InlineTools {
	public static List<HashMap<String, String>> GetXMInfo(String ywlsh) {
		List<HashMap<String, String>> list_info=new ArrayList<HashMap<String, String>>();
		CommonDao dao = SuperSpringContext.getContext().getBean(CommonDao.class);
		List<BDCS_XMXX> list_xmxx = dao.getDataList(BDCS_XMXX.class, "PROJECT_ID='"+ywlsh+"'");
		if (list_xmxx == null||list_xmxx.size()<=0) {
			return list_info;
		}
		ProjectInfo projectinfo=ProjectHelper.GetPrjInfoByXMBH(list_xmxx.get(0).getId());
		if(projectinfo==null){
			return list_info;
		}
		List<String> list_qllx_main=new ArrayList<String>();
		list_qllx_main.add(QLLX.JTTDSYQ.Value);
		list_qllx_main.add(QLLX.GJTDSYQ.Value);
		list_qllx_main.add(QLLX.GYJSYDSHYQ.Value);
		list_qllx_main.add(QLLX.GYJSYDSHYQ_FWSYQ.Value);
		list_qllx_main.add(QLLX.ZJDSYQ.Value);
		list_qllx_main.add(QLLX.ZJDSYQ_FWSYQ.Value);
		list_qllx_main.add(QLLX.JTJSYDSYQ.Value);
		list_qllx_main.add(QLLX.JTJSYDSYQ_FWSYQ.Value);
		//测试使用
		list_qllx_main.add(QLLX.DIYQ.Value);
		//主体权利首次登记
		if(DJLX.CSDJ.Value.equals(projectinfo.getDjlx())&&list_qllx_main.contains(projectinfo.getQllx())){
			List<BDCS_DJDY_GZ> djdys=dao.getDataList(BDCS_DJDY_GZ.class, "XMBH='"+projectinfo.getXmbh()+"'");
			if(djdys!=null&&djdys.size()>0){
				for(BDCS_DJDY_GZ djdy:djdys){
					RealUnit unit=UnitTools.loadUnit(BDCDYLX.initFrom(djdy.getBDCDYLX()), DJDYLY.initFrom(djdy.getLY()), djdy.getBDCDYID());
					if(unit!=null){
						List<Rights> list_right=RightsTools.loadRightsByCondition(DJDYLY.GZ, "DJDYID='"+djdy.getDJDYID()+"' AND XMBH='"+projectinfo.getXmbh()+"'");
						if(list_right!=null&&list_right.size()>0){
							Rights right=list_right.get(0);
							List<RightsHolder> list_holder=RightsHolderTools.loadRightsHolders(DJDYLY.GZ, right.getId());
							List<String> list_qlrmc=new ArrayList<String>();
							if(list_holder!=null&&list_holder.size()>0){
								for(RightsHolder holder:list_holder){
									list_qlrmc.add(holder.getQLRMC());
								}
							}
							HashMap<String, String> info=new HashMap<String, String>();
							info.put("QLR", StringHelper.formatList(list_qlrmc, "、"));//权利人
							info.put("QLLX", right.getQLLX());//权利类型
							info.put("QLLXMC", QLLX.initFrom(right.getQLLX()).Name);//权利类型名称
							info.put("ZL", unit.getZL());//坐落
							info.put("BDCDYH", unit.getBDCDYH());//不动产单元号
							info.put("AREA", StringHelper.formatDouble(unit.getMJ()));//面积
							info.put("BZ",right.getFJ());
							if(BDCDYLX.SHYQZD.equals(unit.getBDCDYLX())){
								UseLand land=(UseLand)unit;
								List<TDYT> list_tdyt=land.getTDYTS();
								List<String> list_tdytmc=new ArrayList<String>();
								if(list_tdyt!=null&&list_tdyt.size()>0){
									for(TDYT tdyt:list_tdyt){
										list_tdytmc.add(tdyt.getTDYTName());
									}
								}
								info.put("YT", StringHelper.formatList(list_tdytmc, "、"));//用途
							}
							if(BDCDYLX.H.equals(unit.getBDCDYLX())||BDCDYLX.YCH.equals(unit.getBDCDYLX())){
								House h=(House)unit;
								info.put("YT", h.getGHYTName());//用途
							}
							
							info.put("BDCZMH", "");//不动产证明号
							info.put("BDCQZH", right.getBDCQZH());//不动产权证号
							info.put("DJSJ", StringHelper.FormatDateOnType(right.getDJSJ(), "yyyy-MM-dd HH:mm:ss"));//登记时间
							//添加公告类型
							info.put("GGLX", "scgg");
							list_info.add(info);
						}
					}
				}
			}
		}else if(DJLX.GZDJ.Value.equals(projectinfo.getDjlx())&&list_qllx_main.contains(projectinfo.getQllx())){//主体权利更正登记
			List<BDCS_DJDY_GZ> djdys=dao.getDataList(BDCS_DJDY_GZ.class, "XMBH='"+projectinfo.getXmbh()+"'");
			if(djdys!=null&&djdys.size()>0){
				for(BDCS_DJDY_GZ djdy:djdys){
					RealUnit unit=UnitTools.loadUnit(BDCDYLX.initFrom(djdy.getBDCDYLX()), DJDYLY.initFrom(djdy.getLY()), djdy.getBDCDYID());
					if(unit!=null){
						List<Rights> list_right=RightsTools.loadRightsByCondition(DJDYLY.GZ, "DJDYID='"+djdy.getDJDYID()+"' AND XMBH='"+projectinfo.getXmbh()+"'");
						if(list_right!=null&&list_right.size()>0){
							Rights right=list_right.get(0);
							List<RightsHolder> list_holder=RightsHolderTools.loadRightsHolders(DJDYLY.GZ, right.getId());
							List<String> list_qlrmc=new ArrayList<String>();
							if(list_holder!=null&&list_holder.size()>0){
								for(RightsHolder holder:list_holder){
									list_qlrmc.add(holder.getQLRMC());
								}
							}
							HashMap<String, String> info=new HashMap<String, String>();
							info.put("QLR", StringHelper.formatList(list_qlrmc, "、"));//权利人
							info.put("QLLX", right.getQLLX());//权利类型
							info.put("QLLXMC", QLLX.initFrom(right.getQLLX()).Name);//权利类型名称
							info.put("ZL", unit.getZL());//坐落
							info.put("BDCDYH", unit.getBDCDYH());//不动产单元号
							info.put("AREA", StringHelper.formatDouble(unit.getMJ()));//面积
							info.put("GZNR",right.getDJYY());
							info.put("BZ",right.getFJ());
							if(BDCDYLX.SHYQZD.equals(unit.getBDCDYLX())){
								UseLand land=(UseLand)unit;
								List<TDYT> list_tdyt=land.getTDYTS();
								List<String> list_tdytmc=new ArrayList<String>();
								if(list_tdyt!=null&&list_tdyt.size()>0){
									for(TDYT tdyt:list_tdyt){
										list_tdytmc.add(tdyt.getTDYTName());
									}
								}
								info.put("YT", StringHelper.formatList(list_tdytmc, "、"));//用途
							}
							if(BDCDYLX.H.equals(unit.getBDCDYLX())||BDCDYLX.YCH.equals(unit.getBDCDYLX())){
								House h=(House)unit;
								info.put("YT", h.getGHYTName());//用途
							}
							info.put("BDCZMH", "");//不动产证明号
							info.put("BDCQZH", right.getBDCQZH());//不动产权证号
							info.put("DJSJ", StringHelper.FormatDateOnType(right.getDJSJ(), "yyyy-MM-dd HH:mm:ss"));//登记时间
							//添加公告类型
							info.put("GGLX", "gzgg");
							list_info.add(info);
						}
					}
				}
			}
		}else if(DJLX.ZXDJ.Value.equals(projectinfo.getDjlx())&&list_qllx_main.contains(projectinfo.getQllx())){//主体权利注销登记
			List<BDCS_DJDY_GZ> djdys=dao.getDataList(BDCS_DJDY_GZ.class, "XMBH='"+projectinfo.getXmbh()+"'");
			if(djdys!=null&&djdys.size()>0){
				for(BDCS_DJDY_GZ djdy:djdys){
					RealUnit unit=UnitTools.loadUnit(BDCDYLX.initFrom(djdy.getBDCDYLX()), DJDYLY.LS, djdy.getBDCDYID());
					if(unit!=null){
						List<Rights> list_right=RightsTools.loadRightsByCondition(DJDYLY.GZ, "DJDYID='"+djdy.getDJDYID()+"' AND XMBH='"+projectinfo.getXmbh()+"'");
						if(list_right!=null&&list_right.size()>0){
							Rights right=list_right.get(0);
							List<RightsHolder> list_holder=RightsHolderTools.loadRightsHolders(DJDYLY.LS, right.getLYQLID());
							List<String> list_qlrmc=new ArrayList<String>();
							if(list_holder!=null&&list_holder.size()>0){
								for(RightsHolder holder:list_holder){
									list_qlrmc.add(holder.getQLRMC());
								}
							}
							HashMap<String, String> info=new HashMap<String, String>();
							info.put("QLR", StringHelper.formatList(list_qlrmc, "、"));//权利人
							info.put("QLLX", right.getQLLX());//权利类型
							info.put("QLLXMC", QLLX.initFrom(right.getQLLX()).Name);//权利类型名称
							info.put("ZL", unit.getZL());//坐落
							info.put("BDCDYH", unit.getBDCDYH());//不动产单元号
							info.put("AREA", StringHelper.formatDouble(unit.getMJ()));//面积
							info.put("BZ",right.getFJ());
							if(BDCDYLX.SHYQZD.equals(unit.getBDCDYLX())){
								UseLand land=(UseLand)unit;
								List<TDYT> list_tdyt=land.getTDYTS();
								List<String> list_tdytmc=new ArrayList<String>();
								if(list_tdyt!=null&&list_tdyt.size()>0){
									for(TDYT tdyt:list_tdyt){
										list_tdytmc.add(tdyt.getTDYTName());
									}
								}
								info.put("YT", StringHelper.formatList(list_tdytmc, "、"));//用途
							}
							if(BDCDYLX.H.equals(unit.getBDCDYLX())||BDCDYLX.YCH.equals(unit.getBDCDYLX())){
								House h=(House)unit;
								info.put("YT", h.getGHYTName());//用途
							}
							info.put("BDCZMH", "");//不动产证明号
							info.put("BDCQZH", right.getBDCQZH());//不动产权证号
							info.put("DJSJ", StringHelper.FormatDateOnType(right.getDJSJ(), "yyyy-MM-dd HH:mm:ss"));//登记时间
							//添加公告类型
							info.put("GGLX", "zxgg");
							list_info.add(info);
						}
					}
				}
			}
		}
		return list_info;
	}
}
