package com.supermap.wisdombusiness.workflow.service.wfm;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.supemap.mns.client.AsyncCallback;
import com.supemap.mns.client.AsyncResult;
import com.supemap.mns.client.CloudHttp;
import com.supemap.mns.model.Message;
import com.supermap.realestate.registration.model.BDCS_XMXX;
import com.supermap.realestate.registration.util.ConfigHelper;
import com.supermap.realestate.registration.util.ProjectHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.wisdombusiness.framework.model.User;
import com.supermap.wisdombusiness.web.ResultMessage;
import com.supermap.wisdombusiness.workflow.util.Page;
import com.supermap.wisdombusiness.workflow.dao.CommonDao;
import com.supermap.wisdombusiness.workflow.model.WFI_TRANSFERLIST;
import com.supermap.wisdombusiness.workflow.model.Wfd_Prodef;
import com.supermap.wisdombusiness.workflow.model.Wfi_ActInst;
import com.supermap.wisdombusiness.workflow.model.Wfi_MaterData;
import com.supermap.wisdombusiness.workflow.model.Wfi_ProInst;
import com.supermap.wisdombusiness.workflow.model.Wfi_ProMater;
import com.supermap.wisdombusiness.workflow.service.common.Common;
import com.supermap.wisdombusiness.workflow.service.common.SmObjInfo;
import com.supermap.wisdombusiness.workflow.service.common.WFConst;
import com.supermap.wisdombusiness.workflow.service.wfd.SmHoliday;
import com.supermap.wisdombusiness.workflow.service.wfi.SmAbnormal;
import com.supermap.wisdombusiness.workflow.service.wfi.SmActInst;
import com.supermap.wisdombusiness.workflow.service.wfi.SmProInst;
import com.supermap.wisdombusiness.workflow.service.wfi.SmProMater;
import com.supermap.wisdombusiness.workflow.service.wfi.SmStaff;

@Service("smMainTainService")
public class SmMainTainService {
	@Autowired
	private CommonDao commonDao;
	@Autowired
	private SmProMater _SmProMater;
	@Autowired
	private SmProInst _SmProInst;
	@Autowired
	private SmActInst _smActInst;
	@Autowired
	private SmStaff smStaff;
	@Autowired
	private SmHoliday smHoliday;
	@Autowired
	private SmAbnormal smAbnormal;

	/**
	 * 修改项目紧急程度
	 * 
	 * @param actinst
	 * @param urgency
	 * @return
	 */
	public boolean ModifyUrgency(String actinst, String urgency) {
		boolean Result = false;
		if (urgency != null && !urgency.equals("")) {
			Wfi_ProInst inst = _SmProInst.GetProInstByActInstId(actinst);
			String old = getUrgencyName(inst.getUrgency().toString());
			;
			inst.setUrgency(Integer.parseInt(urgency));
			inst = _SmProInst.SetWeight(inst);
			commonDao.update(inst);
			commonDao.flush();
			User user = smStaff.getCurrentWorkStaff();
			String name = getUrgencyName(urgency);
			String bh = inst.getProlsh();
			if (bh == null) {
				bh = inst.getFile_Number();
			}
			String msg = user.getUserName() + "修改了编号：" + bh + "项目状态由" + old
					+ "改变为" + name;
			smAbnormal.AddAbnormInfo(user.getId(), user.getUserName(), "",
					actinst, inst.getProinst_Id(), msg,
					WFConst.Abnormal_Type.ModifyUrgent.value);
			Result = true;
		}
		return Result;

	}

	private String getUrgencyName(String value) {
		String name = "";
		if (value.equals("1")) {
			name = "正常";
		} else if (value.equals("2")) {
			name = "紧急";
		} else if (value.equals("3")) {
			name = "特急";
		}
		return name;
	}

	public SmObjInfo arrangement(String path) {
		SmObjInfo info = new SmObjInfo();
		// String pathString = ConfigHelper.getNameByValue("material");
		// if (path != null && !path.equals("")) {
		String pathString = path;
		// }
		SimpleDateFormat df = new SimpleDateFormat("yyyyMM");// 设置日期格式
		SimpleDateFormat dd = new SimpleDateFormat("dd");// 设置日期格式
		List<Wfi_ProInst> list = commonDao.getDataList(Wfi_ProInst.class,
				Common.WORKFLOWDB + "Wfi_ProInst", "1>0");
		int count = 0;
		int updatecount = 0;
		if (list != null && list.size() > 0) {
			// for(Wfi_ProInst inst:list){
			for (int i = 0; i < list.size(); i++) {
				Wfi_ProInst inst = list.get(i);
				String firstFolder = df.format(inst.getCreat_Date());
				String secondFolder = dd.format(inst.getCreat_Date());
				String tmpPath = firstFolder + "\\" + secondFolder + "\\"
						+ inst.getProinst_Id();
				String allFirstFolder = pathString + tmpPath;
				List<Wfi_ProMater> maters = commonDao.getDataList(
						Wfi_ProMater.class, Common.WORKFLOWDB + "Wfi_ProMater",
						" proinst_id='" + inst.getProinst_Id()
						/*
						 * + "' and  material_status in (1,2) " //+
						 * WFConst.MateralStatus.AcceotMateral.value
						 */+ "' order by proinst_id ,material_date ");
				if (maters != null && maters.size() > 0) {
					File folderFile = new File(allFirstFolder);
					if (!folderFile.exists()) {
						folderFile.mkdirs();
					}
					for (Wfi_ProMater mater : maters) {
						boolean up = false;
						if (mater != null) {
							String srcfolderString = pathString
									+ mater.getMaterilinst_Id();
							if (!mater.getMaterial_Status().equals(2)) {
								// mater.setMaterial_Status(2);
								// commonDao.update(mater);
								// up = true;
							}
							File filemFile = new File(srcfolderString);
							if (filemFile.exists()) {
								List<Wfi_MaterData> datas = commonDao
										.getDataList(
												Wfi_MaterData.class,
												Common.WORKFLOWDB
														+ "Wfi_MaterData",
												"materilinst_id='"
														+ mater.getMaterilinst_Id()
														+ "'");
								if (datas != null && datas.size() > 0) {
									for (Wfi_MaterData d : datas) {
										if (d.getPath() == null
												|| d.getPath().equals("")) {

											boolean a = copyFolder(
													srcfolderString,
													allFirstFolder
															+ "\\"
															+ mater.getMaterilinst_Id());
											d.setPath(tmpPath + "\\"
													+ mater.getMaterilinst_Id());
											if (a) {
												commonDao.update(d);
												up = true;
												updatecount++;
												del(srcfolderString);
											}
										}
									}

								}

							}
						}
					}
					if (updatecount > 1000) {
						commonDao.flush();
						updatecount = 0;
					}
				}
				count++;
				System.out.println("处理了" + count + "/" + list.size());
			}
			if (updatecount > 0) {
				commonDao.flush();
				updatecount = 0;
			}
			info.setID(list.size() + "");
			info.setDesc("处理完毕，共完成" + list.size());
		}
		return info;
	}

	public void del(String filepath) {
		File f = new File(filepath);// 定义文件路径
		if (f.exists() && f.isDirectory()) {// 判断是文件还是目录
			if (f.listFiles().length == 0) {// 若目录下没有文件则直接删除
				f.delete();
			} else {// 若有则把文件放进数组，并判断是否有下级目录
				File delFile[] = f.listFiles();
				int i = f.listFiles().length;
				for (int j = 0; j < i; j++) {
					if (delFile[j].isDirectory()) {
						del(delFile[j].getAbsolutePath());// 递归调用del方法并取得子目录路径
					}
					delFile[j].delete();
				}
				f.delete();
			}
		}
	}

	public boolean copyFolder(String oldPath, String newPath) {
		boolean result = false;
		try {
			(new File(newPath)).mkdirs(); // 如果文件夹不存在 则建立新文件夹
			File a = new File(oldPath);
			String[] file = a.list();
			File temp = null;
			if (file != null) {
				for (int i = 0; i < file.length; i++) {
					if (oldPath.endsWith(File.separator)) {
						temp = new File(oldPath + file[i]);
					} else {
						temp = new File(oldPath + File.separator + file[i]);
					}

					if (temp.isFile()) {
						FileInputStream input = new FileInputStream(temp);
						FileOutputStream output = new FileOutputStream(newPath
								+ "/" + (temp.getName()).toString());
						byte[] b = new byte[1024 * 5];
						int len;
						while ((len = input.read(b)) != -1) {
							output.write(b, 0, len);
						}
						output.flush();
						output.close();
						input.close();
						result = true;
					}
					if (temp.isDirectory()) {// 如果是子文件夹
						copyFolder(oldPath + "/" + file[i], newPath + "/"
								+ file[i]);
					}
				}
			}

		} catch (Exception e) {
			System.out.println("复制整个文件夹内容操作出错");
			e.printStackTrace();

		}
		return result;

	}

	public String valite(String path, String del, String statusdel) {
		int procount = 0;
		int promater = 0;
		int count = 0;
		int kong = 0;
		int status = 0;
		if (path != null && !path.equals("")) {
			File f = new File(path);// 定义文件路径
			File temp = null;
			if (f.exists() && f.isDirectory()) {
				String[] file = f.list();
				if (file != null) {
					for (int i = 0; i < file.length; i++) {
						if (path.endsWith(File.separator)) {
							temp = new File(path + file[i]);
						} else {
							temp = new File(path + File.separator + file[i]);
						}
						String name = temp.getName();
						if (temp.list() == null || temp.list().length == 0) {
							kong++;
							if (del != null && del.equals("true")) {
								del(temp.getAbsolutePath());
							}
							continue;
						}
						if (name.length() > 6) {
							count++;
							Wfi_ProMater Promater = commonDao.get(
									Wfi_ProMater.class, name);
							if (Promater == null) {
								promater++;
								if (del != null && del.equals("true")) {
									del(temp.getAbsolutePath());
								}
							} else {
								if (!Promater.getMaterial_Status().equals(2)) {
									status++;
									if (statusdel != null
											&& statusdel.equals("true")) {
										del(temp.getAbsolutePath());
									}
									continue;
								}
								Wfi_ProInst inst = commonDao.get(
										Wfi_ProInst.class,
										Promater.getProinst_Id());
								if (inst == null) {
									procount++;
									if (del != null && del.equals("true")) {
										del(temp.getAbsolutePath());
									}
								} else {

								}
							}
						}

					}

				}
			}

		}
		String result = "共检测文件夹" + count + "检测到缺失项目" + procount
				+ "   缺失资料文件夹数据" + promater + "  空文件夹" + kong + " 资料状态问题"
				+ status;
		return result;
	}

	public String proouttime() {
		List<Wfi_ProInst> list = commonDao.getDataList(Wfi_ProInst.class,
				Common.WORKFLOWDB + "Wfi_ProInst", "1>0 and proinst_status <>'0'");
		int count = 0;
		try{
			if (list != null && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					Wfi_ProInst inst = list.get(i);
					List<Wfi_ActInst> actinsts = commonDao.getDataList(Wfi_ActInst.class,Common.WORKFLOWDB + "Wfi_ActInst", "proinst_id ='"+inst.getProinst_Id()+"'" );
//					 long  lon = 0L;
					 long  longs = 0L;
					if(!StringHelper.isEmpty(actinsts)){
						for(Wfi_ActInst actinst: actinsts){
							if(!StringHelper.isEmpty(actinst.getHangup_Time()) && !StringHelper.isEmpty(actinst.getHangdowm_Time())){
								longs+= (getLongTime(actinst.getHangup_Time().getTime(),actinst.getHangdowm_Time().getTime()));
							}
						}
//						if(!StringHelper.isEmpty(longs)){
//							lon =  longs/(1000*60*60*24);
//							System.out.println("--------计算出当前流程所有挂起时长是多少天:----------"+lon);
//						}
					}
					Wfd_Prodef def = commonDao.get(Wfd_Prodef.class,
							inst.getProdef_Id());
					if (def != null && def.getProdef_Time() != null) {
						// 计算时间并插入
						if (inst.getProinst_Willfinish() == null
								|| inst.getProinst_Time() != def.getProdef_Time()) {
							GregorianCalendar calother = new GregorianCalendar();
							calother.setTime(inst.getProinst_Start());
							if (def.getProdef_Time() != null) {
								Date date = smHoliday.addDateByWorkDay(calother,def.getProdef_Time());//原流程时长
								Calendar cal = Calendar.getInstance();
								long miss = date.getTime();
								long nowdate =	miss+longs;
								cal.setTimeInMillis(nowdate);
								System.out.println(cal.getTime());
//								 cal.add(Calendar.DATE, (int) lon);
								inst.setProinst_Willfinish(cal.getTime());
								String str = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(cal.getTime());
								System.out.println("数据维护后流程增加时长至：-----"+str+ "-------结束");
//								inst.setProinst_Willfinish(smHoliday.addDateByWorkDay(calother,def.getProdef_Time()));
								inst.setProinst_Time(def.getProdef_Time());
								commonDao.update(inst);
								commonDao.flush();
								count++;
							}
						}
					}
					System.out.println("完成" + i + "/" + list.size());
				}
			}
			return "重新计算" + count + "个项目的完成时间";
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("系统默认超过30分钟，session会有过期的情况，导致数据维护失败");
		}
		return "";
	}

	
	/**
	 * 计算当前流程一共挂起了多长时间
	 * @param time
	 * @param time2
	 * @return
	 */
	private long getLongTime(long time, long time2) {
		long lon = 0L;
		lon =  Math.abs(time2-time);
		return lon;
	}

	public String computeWeight() {
		int count = 0;
		List<Wfi_ProInst> list = commonDao.getDataList(Wfi_ProInst.class,
				Common.WORKFLOWDB + "Wfi_ProInst", "1>0");
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				Wfi_ProInst inst = list.get(i);
				int weight = inst.getProinst_Weight();
				inst = _SmProInst.SetWeight(inst);
				if (weight != inst.getProinst_Weight()) {
					commonDao.update(inst);
					commonDao.flush();
					count++;
				}

				System.out.println("完成" + i + "/" + list.size());
			}
		}
		return "重新计算" + count + "个项目的权重";
	}

	public String ExtractDDInfo(String qlr) {
		int count = 0;
		StringBuilder sb = new StringBuilder();
		sb.append("select * from ");
		sb.append(Common.WORKFLOWDB);
		sb.append("WFI_TRANSFERLIST where 1>0");
		if(qlr!=null&&qlr.equals("1")){
			sb.append(" and sqr is null");
		}
		List<WFI_TRANSFERLIST> list = commonDao.getDataList(
				WFI_TRANSFERLIST.class, sb.toString());
		int savecount=0;
		if (list != null && list.size() > 0) {
			for (WFI_TRANSFERLIST l : list) {
				if (l != null  && l.getSQR() == null) {
					Map plist = ProjectHelper.GetFileTransferInfoEx3(l
							.getFile_Number());
					if (plist != null) {
						l.setSQR(plist.get("SQR").toString());
						l.setQZH(plist.get("BDCQZH").toString());
						l.setZL(plist.get("ZL").toString());
						commonDao.update(l);
						count++;
						savecount++;
						if(savecount>1000){
							commonDao.flush();
							savecount=0;
						}
						
						
					}
				}
			}
			if(savecount>0){
				commonDao.flush();
				savecount=0;
			}
		}
		return "累计提取" + count;
	}

	public String nostatus() {
		int count = 0;
		List<Wfi_ProMater> maters = commonDao.getDataList(Wfi_ProMater.class,
				"select * from " + Common.WORKFLOWDB + "Wfi_ProMater");
		int sign = 0;
		if (maters != null && maters.size() > 0) {
			for (Wfi_ProMater m : maters) {
				long a = commonDao.getCountByFullSql(" from "
						+ Common.WORKFLOWDB
						+ "wfi_materdata where materilinst_id='"
						+ m.getMaterilinst_Id() + "'");
				if (a > 0 && m.getMaterial_Status() == 1) {
					m.setMaterial_Status(2);
					commonDao.update(m);
					sign++;

				}
				if (a == 0 && m.getMaterial_Status() > 1) {
					m.setMaterial_Status(1);
					commonDao.update(m);
					sign++;
				}
				if (sign > 1000) {
					commonDao.flush();
					sign = 0;
				}
				count++;
				System.out.println("已经处理了数据" + count + "/" + maters.size());
			}

		}
		if (sign > 0) {
			commonDao.flush();
			sign = 0;
		}
		return "完成" + count;
	}

	/**
	 * js代码生成功能
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @return
	 * @throws IOException
	 * @throws ParseException
	 */
	public ResultMessage createJS(Map mapjs, String realpath)
			throws IOException {
		String qlrinfopath = "%s\\resources\\workflow\\scripts\\config\\%s.%s";
		ResultMessage ms = new ResultMessage();
		Map map = transToMAP(mapjs);
		List newinfo = null;
		StringBuilder sb = new StringBuilder();
		Set set = map.keySet();
		Iterator it = set.iterator();
		while (it.hasNext()) {
			Object key = it.next();
			Object value = map.get(key);
			String con = key + ":" + value + ",";
			sb.append(con);
		}
		if (sb.length() > 0 && sb != null) {
			String coninfo = sb.toString();
			String[] arrstr = coninfo.split(",");
			if (arrstr != null && arrstr.length > 0) {
				for (String str : arrstr) {
					newinfo.add(str);
				}
			}
		} else {

		}

		try {
			FileOutputStream fs = new FileOutputStream(new File(String.format(
					qlrinfopath, realpath, "config", "js")));
			fs.write(StringHelper.formatList(newinfo, "\r\n").getBytes());
			ms.setSuccess("true");
			ms.setMsg("生成js信息成功，请注意查看！");
		} catch (FileNotFoundException e) {
			ms.setMsg("失败");
			e.printStackTrace();
		}

		return ms;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Map transToMAP(Map parameterMap) {
		// 返回值Map
		Map returnMap = new HashMap();
		Iterator entries = parameterMap.entrySet().iterator();
		Map.Entry entry;
		String name = "";
		String value = "";
		while (entries.hasNext()) {
			entry = (Map.Entry) entries.next();
			name = (String) entry.getKey();
			Object valueObj = entry.getValue();
			if (null == valueObj) {
				value = "";
			} else if (valueObj instanceof String[]) {
				String[] values = (String[]) valueObj;
				for (int i = 0; i < values.length; i++) {
					value = values[i] + ",";
				}
				value = value.substring(0, value.length() - 1);
			} else {
				value = valueObj.toString();
			}
			returnMap.put(name, value);
		}
		return returnMap;
	}

	public String getNullArvhicesCount() {

		StringBuilder sb = new StringBuilder();

		sb.append(" from bdc_workflow.wfi_proinst where proinst_status=0 and prolsh not in (select ywdh from bdc_dak.das_ajjbxx where ywdh is not null)");
		Long count = commonDao.getCountByFullSql(sb.toString());
		return "检测到" + count + "个项目未正确入库";

	}

	public List<Map> getNullArvhices() {

		StringBuilder sb = new StringBuilder();

		sb.append("select *  from bdc_workflow.wfi_proinst where proinst_status=0 and prolsh not in (select ywdh from bdc_dak.das_ajjbxx where ywdh is not null)");

		return commonDao.getDataListByFullSql(sb.toString());

	}
	
	public String getNullArvhicesDDDCount(){
		StringBuilder sb = new StringBuilder();

		sb.append(" from bdc_workflow.wfi_transferlist where success=0");
		Long count = commonDao.getCountByFullSql(sb.toString());
		return "检测到"+count+"未成功归档";
	}
	
	public List<Map> getNullArvhicesDDD() {

		StringBuilder sb = new StringBuilder();

		sb.append("select *  from bdc_workflow.wfi_transferlist where success=0");

		return commonDao.getDataListByFullSql(sb.toString());

	}
	/**
	 * 更新wfi_proinst表中prolsh为空的记录
	 * @date   2016年11月12日 下午3:04:50
	 * @author JHX
	 * @return
	 */
	public ResultMessage updateProlshOfNull(){
		ResultMessage res = new ResultMessage();
		List<Wfi_ProInst> proinstList = 
				commonDao.getDataList(Wfi_ProInst.class,
						" select * from "+Common.WORKFLOWDB+"Wfi_ProInst where prolsh is null order by proinst_start");
		Wfi_ProInst proinst = null;
		String year;
		String areacode;
		String fileNumber;
		String prolsh= "";
		List<BDCS_XMXX> xmxxList = null;
		BDCS_XMXX xmxx = null;
		long updateCount = 0;
		long noUpdateCount = 0;
		if(proinstList!=null&&proinstList.size()>0){
			updateCount=proinstList.size();
			for(int i=0,j=proinstList.size();i<j;i++){
				proinst = proinstList.get(i);
				year =proinst.getProinst_Start().getYear()+1900+"";
				fileNumber = proinst.getFile_Number();
				int index = fileNumber.indexOf("-", 1)+1;
				areacode =fileNumber.substring(index, index+6);
				prolsh = commonDao.CreatMaxID(year,areacode,"0", "PROLSH");
				proinst.setProlsh(prolsh);
				//TODO:更新BDCK.BDCS_XMXX中的ywlsh字段使用Project_ID关联
				xmxxList =commonDao.getDataList(BDCS_XMXX.class, 
						" select * from bdck.BDCS_XMXX where Project_ID='"+fileNumber+"'");
				if(xmxxList!=null&&xmxxList.size()>0){
					xmxx = xmxxList.get(0);
					xmxx.setYWLSH(prolsh);
					try{
						commonDao.update(xmxx);
						commonDao.update(proinst);

					}catch(Exception e){
						noUpdateCount++;
						continue;
					}
				}
			}
			commonDao.flush();
		}
		res.setSuccess("SUCCESS");
		res.setMsg("本次共更新数据"+(updateCount-noUpdateCount)+"条数据!");
		return res;
	}
	/**
	 * 修改项目行政区划代码
	 * @author zhangp
	 * @data 2017年6月16日下午7:01:40
	 * @param actinst_id
	 * @param areaCode
	 * @return
	 */
	public boolean modifyAreaCode(String actinst_id, String areaCode) {
		boolean success = false;
		if(!"".equals(actinst_id)){
			Wfi_ProInst inst = _SmProInst.GetProInstByActInstId(actinst_id);
			inst.setAreaCode(areaCode);
			commonDao.update(inst);
			commonDao.flush();
			success = true;
		}
		return success;
	}
	
	public String getArchives(int count){
		Page p= commonDao.GetPagedData(Wfi_ProInst.class, 1, count, " proinst_status=0");
		
		StringBuilder sb=new StringBuilder();
	  
		if(p!=null){
		  List<Wfi_ProInst> insss=(List<Wfi_ProInst>) p.getResult();
		  for(Wfi_ProInst p1:insss){
			  sb.append(","+p1.getFile_Number());
		  }
		  return sb.toString().substring(1);
		}
		return null;
	}
	
	/**
	 * 设置项目为绿色通道项目
	 * @author JHX
	 * @param actinstid
	 * @param instancetype
	 */
	public boolean updateProjectInstanceType(String actinstid ,String instancetype){
		if(!StringHelper.isEmpty(actinstid)&&!StringHelper.isEmpty(instancetype)){
			Wfi_ProInst proinst = _SmProInst.GetProInstByActInstId(actinstid);
			if(instancetype.equals("1")){
				//项目设置成绿色通道项目
				proinst.setInstance_Type(WFConst.Instance_Type.Instance_Priviledge.value);
			}else if(instancetype.equals("0")){
				proinst.setInstance_Type(WFConst.Instance_Type.Instance_Normal.value);
			}
			try{
				commonDao.update(proinst);
				commonDao.flush();
				return true;
			}catch(Exception e){
				e.printStackTrace();
			}
			
		}
		return false;
		
	}
	
//	public static void main(String[] args) {
//		long log = 1210047000L;
//		Date date = new Date(log);
//		GregorianCalendar gc = new GregorianCalendar();
//		gc.setTime(date);
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		String str = sdf.format(gc.getTime());
//		System.out.println(str);
//		
//	}
	
}
