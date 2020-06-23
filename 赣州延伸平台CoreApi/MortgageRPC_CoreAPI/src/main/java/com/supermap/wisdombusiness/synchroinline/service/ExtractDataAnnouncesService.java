package com.supermap.wisdombusiness.synchroinline.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Service;

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
import com.supermap.realestate.registration.util.ConstValue.DJLX;
import com.supermap.realestate.registration.util.ConstValue.QLLX;
import com.supermap.realestate.registration.util.ConstValue.DJDYLY;
import com.supermap.realestate.registration.util.ConstValue.BDCDYLX;
import com.supermap.realestate.registration.util.ProjectHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.wisdombusiness.core.SuperSpringContext;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;

/**
 * 登记系统和（鹰潭）在线服务数据的对接，用于在线服务公告自动推送抽取数据！
 * 
 * @author zjk
 * @Description:将登记系统里面的数据抽取出来并将其插入到前置机数据库里面去， 这个service返回保存所有的抽取公告的服务
 * @date 2016年12月14号 AM 11:44
 */
@Service("extractDataAnnouncesService")
public class ExtractDataAnnouncesService {
	/**
	 * @author zhongjunkai
	 * @date 2016-12-15
	 * @param ywlsh
	 *            [] 业务流水号
	 * @return List<HashMap<String,Object>>
	 */
	// @SuppressWarnings("unchecked")
	public  HashMap<String, Object> GetXMInfo(String ywlsh) {
		HashMap<String, Object> list=new HashMap<String, Object>();
		CommonDao dao = SuperSpringContext.getContext().getBean(CommonDao.class);
		List<BDCS_XMXX> list_xmxx = dao.getDataList(BDCS_XMXX.class, "YWLSH='"+ywlsh+"'");
		if (list_xmxx == null||list_xmxx.size()<=0) {
			return list;
		}
		ProjectInfo projectinfo=ProjectHelper.GetPrjInfoByXMBH(list_xmxx.get(0).getId());
		if(projectinfo==null){
			return list;
		}
		List<String> list_qllx_main=new ArrayList<String>();
		list_qllx_main.add(QLLX.JTTDSYQ.Value);//集体土地所有权  1
		list_qllx_main.add(QLLX.GJTDSYQ.Value);//国家土地所有权  2
		list_qllx_main.add(QLLX.GYJSYDSHYQ.Value);//国有建设用地使用权  3
		list_qllx_main.add(QLLX.GYJSYDSHYQ_FWSYQ.Value);//国有建设用地使用权/房屋（构筑物）所有权  4
		list_qllx_main.add(QLLX.ZJDSYQ.Value);//宅基地使用权   5
		list_qllx_main.add(QLLX.ZJDSYQ_FWSYQ.Value);//宅基地使用权/房屋（构筑物）所有权  6
		list_qllx_main.add(QLLX.JTJSYDSYQ.Value);//集体建设用地使用权  7
		list_qllx_main.add(QLLX.JTJSYDSYQ_FWSYQ.Value);//集体建设用地使用权/房屋（构筑物）所有权  8
		//主体权利首次登记   CSDJ("100", "首次登记")GZDJ("500", "更正登记") BGDJ("300", "变更登记") ZXDJ("400", "注销登记")YYDJ("600", "异议登记")
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
							info.put("REMARK", right.getFJ());//副记
							info.put("QLLXMC", QLLX.initFrom(right.getQLLX()).Name);//权利类型名称
							info.put("ZL", unit.getZL());//坐落
							info.put("BDCDYH", unit.getBDCDYH());//不动产单元号
							info.put("AREA", StringHelper.formatDouble(unit.getMJ()));//面积
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
							list.put("scgg",info);
						}
					}
				}
			}
		}
		/**更正登记*/
		else if(DJLX.GZDJ.Value.equals(projectinfo.getDjlx())&&list_qllx_main.contains(projectinfo.getQllx())){
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
							info.put("REMARK", right.getFJ());
							info.put("QLLXMC", QLLX.initFrom(right.getQLLX()).Name);//权利类型名称
							info.put("ZL", unit.getZL());//坐落
							info.put("BDCDYH", unit.getBDCDYH());//不动产单元号
							info.put("AREA", StringHelper.formatDouble(unit.getMJ()));//面积
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
							list.put("gzgg",info);
						}
					}
				}
			}
		}
		/**注销登记*/
		if(DJLX.ZXDJ.Value.equals(projectinfo.getDjlx())&&list_qllx_main.contains(projectinfo.getQllx())){
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
							info.put("REMARK", right.getFJ());
							info.put("QLLXMC", QLLX.initFrom(right.getQLLX()).Name);//权利类型名称
							info.put("ZL", unit.getZL());//坐落
							info.put("BDCDYH", unit.getBDCDYH());//不动产单元号
							info.put("AREA", StringHelper.formatDouble(unit.getMJ()));//面积
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
							list.put("zxgg",info);
						}
					}
				}
			}
		}
		/**异议登记*/
		if(DJLX.YYDJ.Value.equals(projectinfo.getDjlx())&&list_qllx_main.contains(projectinfo.getQllx())){
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
							info.put("REMARK", right.getFJ());
							info.put("QLLXMC", QLLX.initFrom(right.getQLLX()).Name);//权利类型名称
							info.put("ZL", unit.getZL());//坐落
							info.put("BDCDYH", unit.getBDCDYH());//不动产单元号
							info.put("AREA", StringHelper.formatDouble(unit.getMJ()));//面积
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
							list.put("zfgg",info);
						}
					}
				}
			}
		}		
		return list;
	}
	
}
