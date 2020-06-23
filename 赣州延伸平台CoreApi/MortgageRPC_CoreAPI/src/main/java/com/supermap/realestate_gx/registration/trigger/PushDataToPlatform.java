package com.supermap.realestate_gx.registration.trigger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.hibernate.annotations.Proxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import com.supermap.realestate.registration.model.BDCS_H_XZ;
import com.supermap.realestate.registration.model.BDCS_SHYQZD_XZ;
import com.supermap.realestate.registration.model.BDCS_SYQZD_XZ;
import com.supermap.realestate.registration.model.BDCS_XMXX;
import com.supermap.realestate.registration.model.BDCS_ZRZ_XZ;
import com.supermap.realestate.registration.util.StringHelper;
import com.alibaba.fastjson.JSONObject;
import com.supermap.realestate_gx.registration.util.GX_Util;
import com.supermap.realestate_gx.registration.util.StringUtil;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
/**
 * @author wuz 定时推送数据到时空云平台
 *
 */
public class PushDataToPlatform{

	@Autowired 
	private CommonDao baseCommonDao;

	public void AddLand(){
		//内容就是打印一句话
		System.out.println("新增宗地");
		if(1==1)
			return;
	    	Map<String, Object> map = new HashMap<String, Object>();	//最外层map
			Map<String, Object> _map = new HashMap<String, Object>();	//data的map
			Map<String, Object> map_region = new HashMap<String, Object>(); //regions的map
			String path = this.getClass().getResource("").getPath();
			String [] paths = path.split("WEB-INF");
			String configfilepath = String.format(paths[0]+"/resources/FC2DJXT/config/柳州时空信息云平台推送记录_宗地.txt");
			String json = StringHelper.readFile(configfilepath);
			net.sf.json.JSONObject timeobj = net.sf.json.JSONObject.fromObject(json);
			//TODO 获取宗地信息
			String sql = "";
			List<BDCS_SHYQZD_XZ> shyqzd_datas = baseCommonDao.getDataList(BDCS_SHYQZD_XZ.class, sql);
			List<BDCS_SYQZD_XZ> syqzd_datas = baseCommonDao.getDataList(BDCS_SYQZD_XZ.class, sql);
			BDCS_SHYQZD_XZ zd = shyqzd_datas.get(0);
			BDCS_SYQZD_XZ syqzd = syqzd_datas.get(0);
			map.put("data", "");
			map.put("regions", "");
			_map.put("BDCDYID", zd.getId());//不动产单元ID
			_map.put("BDCDYH", zd.getBDCDYH());//不动产单元号
			_map.put("CQZT", zd.getCQZT());	//产权状态
			_map.put("JG", zd.getJG());//价格
			_map.put("QTNYDMJ", syqzd.getQTNYDMJ());//其他农用地面积
			_map.put("NYDMJ", syqzd.getNYDMJ());//农用地面积
			_map.put("BLZT", zd.getBLZT());//办理状态
			_map.put("QXDM", zd.getQXDM());
			_map.put("QXMC", zd.getQXMC());//区县名称
			_map.put("GMJJHYFLDM", zd.getGMJJHYFLDM());//国民经济行业分类代码
			_map.put("TFH", zd.getTFH());//图幅号
			_map.put("TDQSLYZMCL", zd.getTDQSLYZMCL());//土地权属来源证明材料
			_map.put("DJQDM", zd.getDJQDM());//地籍区
			_map.put("DJQMC", zd.getDJQMC());//地籍区mc 
			_map.put("DJH", zd.getDJH());//地籍h
			_map.put("DJZQDM", zd.getDJZQDM());//地籍子区
			_map.put("DJZQMC", zd.getDJZQMC());//地籍子区mc
			_map.put("CLJS", zd.getCLJS());//地籍测量记事
			_map.put("SHYJ", zd.getSHYJ());//地籍调查结果审核意见
			_map.put("ZL", zd.getZL());
			_map.put("ZDSZB", zd.getZDSZB());//宗地北至
			_map.put("ZDSZN", zd.getZDSZN());//宗地南至
			_map.put("ZDT", zd.getZDT());//宗地图
			_map.put("ZDTZM", zd.getZDTZM());//宗地特征码
			_map.put("ZDSZX", zd.getZDSZX());//宗地西至
			_map.put("ZDMJ", zd.getZDMJ());//宗地面积
			_map.put("SHR", zd.getSHR());//审核人
			_map.put("SHRQ", zd.getSHRQ());//审核日期
			_map.put("RJL", zd.getRJL());//容积率
			_map.put("JZZDMJ", zd.getJZZDMJ());//建筑占地面积
			_map.put("JZMD", zd.getJZMD());//建筑密度
			_map.put("JZXG", zd.getJZXG());//建筑限高
			_map.put("JZMJ", zd.getJZMJ());//建筑面积
			_map.put("JSYDMJ", syqzd.getJSYDMJ());//建设用地面积
			_map.put("YYZT", zd.getYYZT());//异议状态
			_map.put("SYQLX", zd.getSYQLX());//A.53 所有权类型字典表
			_map.put("PZYT", zd.getPZYT());//A.54 土地用途字典表
			_map.put("PZMJ", zd.getPZMJ());//批准面积
			_map.put("DYZT", zd.getDYZT());//抵押状态
			_map.put("YXBZ", zd.getYXBZ());//有效标志
			_map.put("WLYDMJ", syqzd.getWLYDMJ());//未利用地面积
			_map.put("QLXZ", zd.getQLXZ());//A.9权利性质字典表
			_map.put("QLLX", zd.getQLLX());//A.8权利类型字典表
			_map.put("QLSDFS", zd.getQLSDFS());//A.10权利设定方式字典表
			_map.put("JZXZXSM", zd.getJZXZXSM());//权属界线走向说明
			_map.put("DCJS", zd.getDCJS());
			_map.put("LDMJ", syqzd.getLDMJ());//林地面积
			_map.put("BLC", zd.getBLC());
			_map.put("CLR", zd.getCLR());
			_map.put("CLRQ", zd.getCLRQ());
			_map.put("ZT", zd.getZT());
			_map.put("YT", zd.getYT());
			_map.put("JZDWSM",syqzd.getJZDWSM());//界址点位说明
			_map.put("DJZT", zd.getDJZT());
			_map.put("DJ", zd.getDJ());
			_map.put("ZH", zd.getZH());
			_map.put("ZM", zd.getZM());
			_map.put("GDMJ",syqzd.getGDMJ());//耕地面积
			_map.put("CDMJ",syqzd.getCDMJ());//草地面积
			_map.put("YSDM", zd.getYSDM());
			_map.put("MODIFYTIME", zd.getMODIFYTIME());
			_map.put("CREATETIME", zd.getCREATETIME());
			_map.put("DCR", zd.getDCR());
			_map.put("DCRQ", zd.getDCRQ());
			_map.put("DCXMID", zd.getDCXMID());
			_map.put("XZZT", zd.getXZZT());
			_map.put("MJDW", zd.getMJDW());
			_map.put("XMBH", zd.getXMBH());
			_map.put("YBZDDM", zd.getYBZDDM());
			map.put("data", _map);
			for(int i = 0;i<map_region.size();i++){	//map_region是临时的，应该按照iserver的API值循环
				//TODO 在shyqzd表中，需要通过iserver的API获取值
				Map<String, Object> xy = new HashMap<String, Object>();
				xy.put("x", "");
				xy.put("y", "");
				map_region.put("region", xy); // 
			}
			map.put("regions", map_region);
			
			JSONObject msg = GX_Util.getHttpURLConnection("NewZD",map);
	
			if("true".equals(msg.getJSONObject("success"))){
				Map bdcdyid = msg.getJSONObject("data");
				String id = StringUtil.valueOf(bdcdyid.get("BDCDYID"));
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH24:mi:ss");
				String time = sdf.format(zd.getCREATETIME());
				saveTimeConfig("SHYQZD",time,id);
			}   
	}
	
	public void AddBuilding(){
		//内容就是打印一句话
		System.out.println("新增建筑物");
		if(1==1)
		return;
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> _map = new HashMap<String, Object>();
		Map<String, Object> map_region = new HashMap<String, Object>();
		String path = this.getClass().getResource("").getPath();
		String [] paths = path.split("WEB-INF");
		String configfilepath = String.format(paths[0]+"/resources/FC2DJXT/config/柳州时空信息云平台推送记录_建筑物.txt");
		String json = StringHelper.readFile(configfilepath);
		net.sf.json.JSONObject timeobj = net.sf.json.JSONObject.fromObject(json);
		String htime=timeobj.get("HTIME").toString();
		String sql = "xmbh is not null order by CREATETIME";
		if(htime!=null && !"".equals(htime)){
			sql = "xmbh is not null and CREATETIME> to_date('"+htime+"','yyyy-MM-dd HH24:mi:ss')";
		}
		List<BDCS_H_XZ> datas_h = baseCommonDao.getDataList(BDCS_H_XZ.class, sql);
		if(!(datas_h.size()>0)){
			return;
		}
		sql = "select * from bdck.bdcs_zrz_xz zrz LEFT JOIN bdck.bdcs_h_xz h on zrz.bdcdyid = h.zdbdcdyid where h.bdcdyid='"+datas_h.get(0).getId()+"' ";
		List<Map> datas_zrz = baseCommonDao.getDataListByFullSql(sql);
		BDCS_H_XZ h = datas_h.get(0);
		Map zrz = new HashMap();
		if(datas_zrz.size()>0){
			zrz = datas_zrz.get(0);
		}
		_map.put("BDCDYID", h.getId());//不动产单元ID
		_map.put("BDCDYH", h.getBDCDYH());//不动产单元号
		_map.put("FCFHT", h.getFCFHT());
		_map.put("FTTDMJ", h.getFTTDMJ());
		_map.put("QXDM", h.getQXDM());
		_map.put("QXMC", h.getQXMC());
		_map.put("TDSYQR", h.getTDSYQR());
		_map.put("DSCS", zrz.get("DSCS"));
		_map.put("DXCS", zrz.get("DXCS"));
		_map.put("DXSD", zrz.get("DXSD"));//地下深度
		_map.put("DJQDM", h.getDJQDM());
		_map.put("DJQMC", h.getDJQMC());
		_map.put("DJZQDM", h.getDJZQDM());
		_map.put("DJZQMC", h.getDJZQMC());
		_map.put("ZL", h.getZL());
		//_map.put("BZ", h.getBZ());
		_map.put("ZDDM", h.getZDDM());
		_map.put("ZDBDCDYID", h.getZDBDCDYID());
		_map.put("SCJZMJ", h.getSCJZMJ());
		_map.put("ZZDMJ", zrz.get("ZZDMJ"));//幢占地面积
		_map.put("ZYDMJ", zrz.get("ZYDMJ"));//幢用地面积
		_map.put("JZWMC", zrz.get("JZWMC"));//建筑物名称
		_map.put("JZWJBYT", zrz.get("JZWJBYT"));//建筑物基本用途
		_map.put("JZWGD",zrz.get("JZWGD"));//建筑物高度
		_map.put("ZTS", zrz.get("ZTS"));//总套数
		_map.put("ZCS", h.getZCS());
		_map.put("FCFHTWJ", zrz.get("FCFHTWJ"));//房产分户图文件
		_map.put("FDCJYJG", zrz.get("FDCJYJG"));//房地产交易价格
		_map.put("FWJG", h.getFWJG());
		_map.put("YXBZ", h.getYXBZ());
		_map.put("ZT", h.getZT());
		_map.put("DYTDMJ", h.getDYTDMJ());
		_map.put("DJZT", h.getDJZT());
		_map.put("JGRQ", h.getJGSJ());//竣工日期
		_map.put("ZRZH", h.getZRZH());
		_map.put("GHYT", h.getGHYT());
		_map.put("MODIFYTIME", h.getMODIFYTIME());
		_map.put("CREATETIME", h.getCREATETIME());
		_map.put("DCXMID", h.getDCXMID());
		_map.put("XMMC", h.getXMMC());
		_map.put("XMBH", h.getXMBH());
		_map.put("YCJZMJ", h.getYCJZMJ());
		map.put("data", _map);
		for(int i = 0;i<map_region.size();i++){	//map_region是临时的，应该按照iserver的API值循环
			//TODO 在zrz表中，需要通过iserver的API获取值
			Map<String, Object> xy = new HashMap<String, Object>();
			xy.put("x", "");
			xy.put("y", "");
			map_region.put("region",xy); // 
		}
		map.put("regions", map_region);
		
		JSONObject msg = GX_Util.getHttpURLConnection("NewJZW",map);
		
		if("true".equals(msg.getJSONObject("success"))){
			Map bdcdyid = msg.getJSONObject("data");
			String id = StringUtil.valueOf(bdcdyid.get("BDCDYID")) ;
			//TODO 保存已经推送的数据
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH24:mi:ss");
			String time = sdf.format(h.getCREATETIME());
			saveTimeConfig("H",time,id);
		}
	}
	
	/**
	 * 将时间存入txt文件中
	 * @param type
	 * @param time
	 * @return
	 */
	private int saveTimeConfig(String type,String time,String bdcdyid)
	{
		String path = this.getClass().getResource("").getPath();
		String [] paths = path.split("WEB-INF");
		String configfilepath = "";
		Map r=new HashMap();
		if("H".equals(type)){
			configfilepath = String.format(paths[0]+"/resources/FC2DJXT/config/柳州时空信息云平台推送记录_建筑物.txt");
			r.put("HTIME", time);
		}
		if("SHYQZD".equals(type)){
			configfilepath = String.format(paths[0]+"/resources/FC2DJXT/config/柳州时空信息云平台推送记录_宗地.txt");
			r.put("SHYQZDTIME", time);
		}
		net.sf.json.JSONObject jsonobj = net.sf.json.JSONObject.fromObject(r);
		  String jsonString=jsonobj.toString();
		  FileOutputStream fop = null;
			File file = null;
			try {
				file = new File(configfilepath);
				if (file.exists()) {
					fop = new FileOutputStream(file);
					byte[] contentInBytes =  jsonString.getBytes();
					fop.write(contentInBytes);
				}
			} catch (IOException e) {
		       System.out.println(e.getMessage());
			} finally {
				try {
					if (fop != null){
						fop.close();
						
					}
				} catch (IOException e) {
					 System.out.println(e.getMessage());
				}
			}
			return 0;
	}
}
