package com.supermap.wisdombusiness.workflow.service.wfm;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.CharsetUtils;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.supermap.realestate.registration.util.ConfigHelper;
import com.supermap.realestate.registration.util.GetProperties;
import com.supermap.realestate.registration.util.ProjectHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.realestate_gx.registration.model.myMultipartFile;
import com.supermap.wisdombusiness.framework.model.User;
import com.supermap.wisdombusiness.framework.service.RoleService;
import com.supermap.wisdombusiness.framework.service.UserService;
import com.supermap.wisdombusiness.web.ResultMessage;
import com.supermap.wisdombusiness.workflow.dao.CommonDao;
import com.supermap.wisdombusiness.workflow.model.HK_DSXX;
import com.supermap.wisdombusiness.workflow.model.WFI_DOSSIERTRANSFER;
import com.supermap.wisdombusiness.workflow.model.WFI_TRANSFERLIST;
import com.supermap.wisdombusiness.workflow.model.Wfd_Actdef;
import com.supermap.wisdombusiness.workflow.model.Wfd_Prodef;
import com.supermap.wisdombusiness.workflow.model.Wfi_ActInst;
import com.supermap.wisdombusiness.workflow.model.Wfi_MaterData;
import com.supermap.wisdombusiness.workflow.model.Wfi_ProInst;
import com.supermap.wisdombusiness.workflow.model.Wfi_ProMater;
import com.supermap.wisdombusiness.workflow.service.common.Common;
import com.supermap.wisdombusiness.workflow.service.common.SmObjInfo;
import com.supermap.wisdombusiness.workflow.service.common.WFConst;
import com.supermap.wisdombusiness.workflow.service.wfd.SmHoliday;
import com.supermap.wisdombusiness.workflow.service.wfi.SmActInst;
import com.supermap.wisdombusiness.workflow.service.wfi.SmProInst;
import com.supermap.wisdombusiness.workflow.service.wfi.SmProMater;
import com.supermap.wisdombusiness.workflow.util.FileUpload;
import com.supermap.wisdombusiness.workflow.util.Message;
import com.thoughtworks.xstream.core.util.Base64Encoder;


@Service("AdditionalService")
public class AdditionalService {
	
	@Autowired
	private CommonDao commonDao;
	@Autowired
	private SmActInst smActInst;
	@Autowired
	private  SmProInst smProInst;
	@Autowired
	private SmHoliday smHoliday;
	@Autowired
	private SmProMater smProMater;
	@Autowired
	private SmProInstService smProInstService;
	@Autowired
	private UserService userService;
	@Autowired
	private RoleService roleService;
	@Autowired
	private OperationService operationService;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private HttpServletResponse response;
	public List<LinkedHashMap<String,String>> getPartProinfoBySqrAndCard(String sqr, String card){
		List<LinkedHashMap<String,String>> info = new ArrayList<LinkedHashMap<String,String>>();
		if(!StringHelper.isEmpty(sqr)&&!StringHelper.isEmpty(card)){
			String fulSql = 
				"select a.* " +
				" from bdc_workflow.wfi_proinst a, bdck.bdcs_sqr b, bdck.bdcs_xmxx c" + 
				" where c.project_id = a.file_number" + 
				" and b.xmbh = c.xmbh " + 
				" and (b.sqrxm = '"+sqr.trim()+"' and b.zjh = '"+card.trim()+"') order by proinst_end desc";
				List<Wfi_ProInst> proInsts = commonDao.getDataList(Wfi_ProInst.class, fulSql);
				if(proInsts.size()>0){
					for(int i=0;i<proInsts.size();i++){
						LinkedHashMap<String, String> tmp = new LinkedHashMap<String, String>();
						Wfi_ProInst proinst = proInsts.get(i);
						tmp.put("PROLSH", proinst.getProlsh());
						tmp.put("ACTINST_NAME", proinst.getActinst_Name());
						info.add(tmp);
					}
				}
		}
		return info;
		
	}
	
	public Map<String,Object> archiveList(String transfercode, int pageIndex, int pageSize){
		Map<String,Object> map = new HashMap<String, Object>();
		List<Map> list = new ArrayList<Map>();
		WFI_DOSSIERTRANSFER dossier = new WFI_DOSSIERTRANSFER();
		long count = 0;
		String fulSql = 
				"select t.file_number,t.receive_status,t.transferid,p.prolsh,p.project_name" +
				"  from bdc_workflow.wfi_dossiertransfer d" + 
				"  left join bdc_workflow.wfi_transferlist t" + 
				"  on d.transferid = t.transferid" + 
				"  left join bdc_workflow.wfi_proinst p" + 
				"  on t.file_number = p.file_number" +
				" where d.transfercode='"+transfercode+"' and t.fromactinst_name like'%归档%'";
		count = commonDao.getCountByFullSql(" from ("+fulSql+")");
		if(count>0){
			list = commonDao.getPageDataByFullSql(fulSql, pageIndex, pageSize);
			if(list.size()>0){
				Map first = list.get(0);
				for(Map m : list){
					String FILE_NUMBER = m.get("FILE_NUMBER").toString();
					Map plist = ProjectHelper.GetFileTransferInfoEx(FILE_NUMBER);
					m.putAll(plist);
				}
				String transferid = first.get("TRANSFERID").toString();
				dossier = commonDao.get(WFI_DOSSIERTRANSFER.class, transferid);
			}
		}
		map.put("total", count);
		map.put("record", dossier);
		map.put("list", list);
		return map;
	}
	public Map<String,Object> CheckActinstStart(String actinstid,String actstart,String type){
		//比较流程创建时间与环节开始时间是否一样
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("change", "0");
		Wfi_ActInst actinst = smActInst.GetActInst(actinstid);
		Wfi_ProInst proinst = smProInst.GetProInstByActInstId(actinstid);
		//判断只更改显示0还是要修改数据1
		if(actinst.getActinst_Start().getTime()==proinst.getCreat_Date().getTime()){
			long start = Long.parseLong(actstart);
			Wfd_Prodef prodef = commonDao.get(Wfd_Prodef.class, proinst.getProdef_Id());
			Wfd_Actdef actdef = commonDao.get(Wfd_Actdef.class, actinst.getActdef_Id());
			GregorianCalendar cal_act = new GregorianCalendar();
			cal_act.setTime(new Date(start));
			GregorianCalendar cal_pro = new GregorianCalendar();
			cal_pro.setTime(new Date(start));
			actinst.setActinst_Start(new Date(start));
			actinst.setActinst_Willfinish(smHoliday.addDateByWorkDay(cal_act,actdef.getActdef_Time()!=null?actdef.getActdef_Time():0));
			proinst.setProinst_Start(new Date(start));
			proinst.setProinst_Willfinish(smHoliday.addDateByWorkDay(cal_pro,prodef.getProdef_Time()!=null?prodef.getProdef_Time():0));
			map.put("change","1");
			map.put("actinst", actinst);
			map.put("proinst", proinst);
			if("1".equals(type)){//修改数据
				commonDao.update(actinst);
				commonDao.update(proinst);
				commonDao.flush();
				map.put("change","2");
			}
		}
		return map;
	}
	/*
	 * 流程时间添加工作日顺延
	 */
	public Wfi_ProInst TimePlusWorkDay(String actinstid, String dayCount){
		Wfi_ProInst proinst = smProInst.GetProInstByActInstId(actinstid);
		Date prowillfinish = proinst.getProinst_Willfinish();
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(prowillfinish);
		proinst.setProinst_Willfinish(smHoliday.addDateByWorkDay(cal,Integer.parseInt(dayCount)));
		return proinst;
	}
	/**
	 * 将项目的prolsh复制其目录与资料给当前项目
	 * @author zhangp
	 * @data 2017年11月15日上午10:18:33
	 * @param relateProlsh
	 * @param currActinstid
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> RelateMaterial(String relateProlsh,String currActinstid){
		Map<String, Object> relatemap = new HashMap<String, Object>();
		String proinstid = smProInst.GetProInstIDByActInstId(currActinstid);
		//获取关联的资料
		List<Wfi_ProInst> proinsts = smProInst.GetProInstByLshs(relateProlsh);
		if(proinsts.size()>0){
			Wfi_ProInst proinst = proinsts.get(0);
			String relateActinstid = proinst.getActinst_Id();
			relatemap = smProMater.getProMateEx(relateActinstid);
			if(relatemap!=null){
				List<Wfi_ProMater> proMaterList = (List<Wfi_ProMater>) relatemap.get("folder");
				List<Wfi_MaterData> materDataList = (List<Wfi_MaterData>) relatemap.get("file");
				if(proMaterList.size()>0){
					//获取当前项目资料目录，进行删除
					List<Wfi_ProMater> currPromaterlist = smProMater.GetProMaterInfo(proinstid);
					String materilinst_ids = "";
					for(int m=0;m<currPromaterlist.size();m++){
						materilinst_ids += currPromaterlist.get(m).getMaterilinst_Id()+",";
					}
					smProMater.deleteFile2(null, materilinst_ids, null);
					for(int i=0;i<proMaterList.size();i++){
						Wfi_ProMater proMater = proMaterList.get(i);//关联项目资料目录
						Wfi_ProMater tmpProMater = (Wfi_ProMater) proMater.clone();
						//创建新的promater
						tmpProMater.setMaterilinst_Id(Common.CreatUUID());
						tmpProMater.setProinst_Id(proinstid);
						if(materDataList.size()>0){
							for(int j=0;j<materDataList.size();j++){
								if(proMater.getMaterilinst_Id().equals(materDataList.get(j).getMaterilinst_Id())){
									Wfi_MaterData tmpMaterData = (Wfi_MaterData) materDataList.get(j).clone();
									tmpMaterData.setMaterialdata_Id(Common.CreatUUID());
									tmpMaterData.setMaterilinst_Id(tmpProMater.getMaterilinst_Id());
									commonDao.save(tmpMaterData);
								}
							}
						}
						commonDao.save(tmpProMater);
					}
					commonDao.flush();
				}
			}
		}
		return 	smProMater.getProMateEx(currActinstid);
	}
	
	public boolean checkHH(String HH){
		boolean flag = false; 
		 String XZQHDM=ConfigHelper.getNameByValue("XZQHDM");
		 if(!StringHelper.isEmpty(XZQHDM)&&XZQHDM.indexOf("2112")>-1){
			 User user = userService.getCurrentUserInfo();
			 String loginName = user.getLoginName();
			 String fromSql = 
					 " FROM JHK.JHRXX" + 
					 " WHERE (JHSJ BETWEEN TO_DATE(TO_CHAR(SYSDATE, 'YYYY-MM-DD') || ' 00:00:01'," + 
					 "'YYYY-MM-DD HH24:MI:SS') AND" + 
					 " TO_DATE(TO_CHAR(SYSDATE, 'YYYY-MM-DD') || ' 23:59:59'," + 
					 " 'YYYY-MM-DD HH24:MI:SS'))" + 
					 " AND HH = '"+HH+"' AND JHRLB = '"+loginName+"'";
			 long count = commonDao.getCountByFullSql(fromSql);
			 if(count>0){
				 flag = true;
			 }
		 }
		 return flag;
	}
	//批次号、受理编号获取项目
	public Message searchProject(String pageIndex,String pageSize,String batch,String prolsh,HttpServletRequest request){
		Message msg = new Message();
		int index = !StringHelper.isEmpty(pageIndex)?Integer.parseInt(pageIndex):1;
		int size = !StringHelper.isEmpty(pageSize)?Integer.parseInt(pageSize):10;
		List<Wfi_ProInst> insts = new ArrayList<Wfi_ProInst>();
		long total = 0;
		String nowhere = "";
		if(!StringHelper.isEmpty(batch)){
			if(!StringHelper.isEmpty(prolsh)){
				nowhere = " batch='"+batch+"' and prolsh='"+prolsh+"'";
			}else{
				nowhere = " batch='"+batch+"'";
			}
		}else if(!StringHelper.isEmpty(prolsh)){
			nowhere = " prolsh='"+prolsh+"'";
		}
		if(!StringHelper.isEmpty(nowhere)){
			String fromSql = "select * from "+Common.WORKFLOWDB + "Wfi_ProInst where "+nowhere;
			total = commonDao.getCountByFullSql("from ("+fromSql+")");
		}
		msg.setTotal(total);
		if(total>0){
			insts = commonDao.GetPagedListData(Wfi_ProInst.class, Common.WORKFLOWDB + "Wfi_ProInst", index, size, nowhere);
			msg.setRows(insts);
		}
		return msg;
	}
	
	public boolean removeProject(String proinstid){
		boolean success = false;
		Wfi_ProInst proinst = commonDao.get(Wfi_ProInst.class, proinstid);
		if(null!=proinst){
			proinst.setBatch(null);
			commonDao.update(proinst);
			commonDao.flush();
			success = true;
		}
		return success;
	}

	public boolean addProject(String[] proinstids, String batch) {
		boolean success = false;
		for (int i = 0; i < proinstids.length; i++) {
			Wfi_ProInst proinst = commonDao.get(Wfi_ProInst.class,
					proinstids[i]);
			if (null != proinst) {
				proinst.setBatch(batch);
				commonDao.update(proinst);
				commonDao.flush();
				success = true;
			}
		}
		return success;
	}

	//获取项目中是否有驳回环节信息
	public Map ColourPassback(String filenumber) {
		Map map = new HashMap<String, String>();
		map.put("FILE_NUMBER", filenumber);
		map.put("PASSBACK", "0");
		Wfi_ProInst proinst = smProInst.GetProInstByFileNumber(filenumber);
		if(null!=proinst){
			List<Wfi_ActInst> actinsts = smActInst.GetActInstsbyproinstid(proinst.getProinst_Id());
			if(actinsts.size()>0){
				for(Wfi_ActInst act : actinsts){
					if(!StringHelper.isEmpty(act.getOperation_Type())&&WFConst.Operate_Type.PRODUCT_smallPicture.value==Integer.parseInt(act.getOperation_Type())){
						map.put("PASSBACK", "1");
						break;
					}
				}
			}
		}
		return map;
	}
	//批量转办
	public SmObjInfo batchTurnTodo(String[] actinstArray, String msg) {
		return batchTurnTodo(actinstArray, msg, null);
	}
	public SmObjInfo batchTurnTodo(String[] actinstArray, String msg, String staff) {
		SmObjInfo result = new SmObjInfo();
		boolean success = false;
		if(!StringHelper.isEmpty(staff)){
			staff = "0,"+staff;
			if(actinstArray.length>0){
				String[] staffs1 = staff.split(",");
				for(int k=1,length=actinstArray.length;k<length;k++){
					operationService.turnToMoreStaffExt(actinstArray[k], staffs1, null, msg);
				}
				success = true;
			}
		}else{
			boolean flag = CheckRoleStaff(actinstArray);
			if(actinstArray.length>0&&flag){
				for(int i=1,length=actinstArray.length;i<length;i++){
					//获取人员
					List<SmObjInfo> info = smActInst.GetTurnStaffEx(actinstArray[i]);
					String staffs = "0";
					for(int j=0,len=info.size();j<len;j++){
						staffs+=","+info.get(j).getID();
					}
					String[] staffids = staffs.split(",");
					operationService.turnToMoreStaffExt(actinstArray[i], staffids, null, msg);
				}
				success = true;
			}
		}
		if(success){
			result.setID("1");
			result.setDesc("操作成功");
		}else{
			result.setID("0");
			result.setDesc("操作失败");
		}
		return result;
	}
	private boolean CheckRoleStaff(String[] actinstArray){
		boolean success = true;
		if(actinstArray.length>1){
			for(int i=1,length=actinstArray.length;i<length;i++){
				Wfd_Actdef actdef = smActInst.GetActDef(actinstArray[i]);
				if(null!=actdef){
					String roleid = actdef.getRole_Id();
					List<User> users = roleService.findUsersByRoleId(roleid);
					if(users.size()<2){
						success = false;
						break;
					}
				}else{
					success = false;
					break;
				}
			}
		}else{
			success = false;
		}
		return success;
	}
	
	public boolean isBatchProject(String actinstid){
		boolean isbatch = false;
		Wfi_ProInst proinst = smProInst.GetProInstByActInstId(actinstid);
		if(null!=proinst){
			String batch = proinst.getBatch();
			if(!StringHelper.isEmpty(batch)){
				isbatch = true;
			}
		}
		return isbatch;
	}
	
	public boolean act2Timing(String actinstid){
		boolean success = false;
		Wfi_ActInst actinst = smActInst.GetActInst(actinstid);
		Wfi_ProInst proinst = smProInst.GetProInstByActInstId(actinstid);
		if(actinst.getActinst_Start().equals(proinst.getCreat_Date())){
			if (null != actinst.getActinst_End()) {
				long ending = actinst.getActinst_End().getTime();
				Wfd_Prodef prodef = commonDao.get(Wfd_Prodef.class,proinst.getProdef_Id());
				GregorianCalendar cal = new GregorianCalendar();
				cal.setTime(new Date(ending));
				proinst.setProinst_Start(new Date(ending));
				proinst.setProinst_Willfinish(smHoliday.addDateByWorkDay(cal,prodef.getProdef_Time()));
				commonDao.update(actinst);
				commonDao.update(proinst);
				commonDao.flush();
				success = true;
			}
		}
		return success;
	}
	
	public Map QualitytestWilldue(String remainingtime){
		Map<String,Object> map = new HashMap<String, Object>();
		String fromSql = 
				" from bdc_workflow.wfi_actinst a,bdc_workflow.wfi_proinst p "
				+ "where ceil(a.actinst_willfinish-sysdate)<='"+remainingtime+"' "
				+ "and a.actinst_id=p.actinst_id "
				+ "and p.proinst_status='10'";
		long count = commonDao.getCountByFullSql(fromSql);
		map.put("count", count);
		return map;
	}
	
	public boolean QualitytestCancelall(String remainingtime){
		Map<String,Object> map = new HashMap<String, Object>();
		String fullSql = 
				" select a.* from bdc_workflow.wfi_actinst a,bdc_workflow.wfi_proinst p "
				+ "where ceil(a.actinst_willfinish-sysdate)<='"+remainingtime+"' "
				+ "and a.actinst_id=p.actinst_id "
				+ "and p.proinst_status='10'";
		List<Wfi_ActInst> list = commonDao.getDataList(Wfi_ActInst.class, fullSql);
		User user = userService.getCurrentUserInfo();
		if(list.size()>0){
			for(Wfi_ActInst actinst : list){
				if(!StringHelper.isEmpty(actinst.getActinst_Id())){
					smProInstService.CancelQualityTest(actinst.getActinst_Id(), user);
				}
			}
		}
		return true;
	}
	@SuppressWarnings("unchecked")
	public  ResultMessage projectInfo(String file_number,Map map) throws Exception{
		ResultMessage msg = new ResultMessage();
		msg.setSuccess("false");
		msg.setMsg("推送失败");
		Wfi_ProInst proinst = smProInstService.GetProInstByLsh(file_number);
		
		System.out.println("3------------");
		
		if(null!=proinst){
			String datasrc = (null==map.get("datasrc"))?"":map.get("datasrc").toString();
			String approved = (null==map.get("approved"))?"":map.get("approved").toString();
			System.out.println("4------------");
			if(!StringHelper.isEmpty(datasrc)&&!StringHelper.isEmpty(approved)&&"true".equals(approved)){
				if("0".equals(datasrc)){//住建
					String fileURL = GetProperties.getConstValueByKey("FileURL");
					List<Map> folders = (List<Map>)map.get("folders");
					System.out.println("5------------");	
					if(folders.size()>0){
						for(int i=0;i<folders.size();i++){//文件夹们
							String alias = null!=folders.get(i).get("alias")?folders.get(i).get("alias").toString():"";
							System.out.println("6------------");
							//获取住建方资料文件夹，没有即创建
							Wfi_ProMater promater = findOrCreatePromater(proinst.getProinst_Id(),datasrc,alias);
							List<Map> files = (List<Map>)folders.get(i).get("files");
							System.out.println("7------------");
							for(int j=0;j<files.size();j++){//文件们
								Map fileMap = (Map)files.get(j);
								String filename = null!=fileMap.get("filename")?fileMap.get("filename").toString():"";
								String filetype = null!=fileMap.get("filetype")?fileMap.get("filetype").toString():"";
								if(filetype.indexOf(".")<0){
									filetype="."+filetype;
								}
								filename = filename + filetype;
								//创建文件数据
								SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");// 设置日期格式
								String fileName = df.format(new Date()) + "_" + java.net.URLEncoder.encode(filename, "UTF-8");
								String  filePath = GetNewPath(proinst, promater.getMaterilinst_Id());
								String basicPath = ConfigHelper.getNameByValue("material");
								String disc = basicPath;
								basicPath = basicPath + filePath;
								String stuffId = fileMap.get("content").toString();
								byte[] byt =null;
								//获取文件
								String access_token = getToken();
								JSONObject flieParam = new JSONObject();
								flieParam.put("access_token", access_token);
								flieParam.put("stuffId", stuffId);
								byt = getFileBytes(fileURL,flieParam);
								String materialtype = ConfigHelper.getNameByValue("materialtype");
								System.out.println("8------------");
								Wfi_MaterData materData = new Wfi_MaterData();
								List <Map> list = FileUpload.GenerateImageEx(null, promater.getMaterilinst_Id(), filename, proinst,byt);
								System.out.println("9------------");
								if(null!=list&&list.size()>0){
									System.out.println("10------------");
									Map m1 = list.get(0);
									System.out.println("11:+++++++++++++"+m1.toString());
									materData.setMaterialdata_Id(Common.CreatUUID());
									System.out.println(materData.getMaterialdata_Id());
									materData.setFile_Name(filename);
									System.out.println(materData.getFile_Name());
									materData.setDisc(disc);
									System.out.println(materData.getDisc());
									materData.setMaterilinst_Id(promater.getMaterilinst_Id());
									System.out.println(materData.getMaterilinst_Id());
									materData.setFile_Index(new Date().getTime());
									System.out.println(materData.getFile_Index());
									//materData.setUpload_Id(staff_id);
									//materData.setUpload_Name(staff_id);
									//materData.setThumb(thumb);
									materData.setUpload_Date(new Date());
									System.out.println(materData.getUpload_Date());
									materData.setUpload_Type(materialtype);
									System.out.println(materData.getUpload_Type());
									System.out.println("filepath:"+m1.get("filepath").toString());
									System.out.println("filename:"+m1.get("filename").toString());
									materData.setPath(m1.get("filepath").toString());
									materData.setFile_Path(m1.get("filename").toString());
									promater.setImg_Path(materData.getMaterialdata_Id());
									System.out.println("12:+++++++++++++"+materData.toString());
									commonDao.saveOrUpdate(promater);
									commonDao.save(materData);
									msg.setSuccess("true");
									msg.setMsg("推送成功");
								}
							}
						}
					}
				}else if("1".equals(datasrc)){//地税
					Map mapdata = (Map)map.get("data");
					String WQHTBH = null!=mapdata.get("WQHTBH")?mapdata.get("WQHTBH").toString():"";
					String FPDM = null!=mapdata.get("FPDM")?mapdata.get("FPDM").toString():"";
					String FPHM = null!=mapdata.get("FPHM")?mapdata.get("FPHM").toString():"";
					String SFWS = null!=mapdata.get("SFWS")?mapdata.get("SFWS").toString():"";
					String BJZT = null!=mapdata.get("BJZT")?mapdata.get("BJZT").toString():"";
					String SLBH = null!=mapdata.get("SLBH")?mapdata.get("SLBH").toString():"";
					String WSSJ = null!=mapdata.get("WSSJ")?mapdata.get("WSSJ").toString():"";
					String WSJE = null!=mapdata.get("WSJE")?mapdata.get("WSJE").toString():"0.0";
					HK_DSXX dsxx = new HK_DSXX();
					dsxx.setWQHTBH(WQHTBH);
					dsxx.setFPDM(FPDM);
					dsxx.setFPHM(FPHM);
					dsxx.setSFWS(SFWS);
					dsxx.setBJZT(BJZT);
					dsxx.setSLBH(SLBH);
					if(!StringHelper.isEmpty(WSSJ)){
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						Date date = sdf.parse(WSSJ);
						dsxx.setWSSJ(date);
					}
					dsxx.setWSJE(Double.parseDouble(WSJE));
					commonDao.save(dsxx);
				}
				String ispushed = proinst.getIsPushed();
				if(StringHelper.isEmpty(ispushed)){
					ispushed = "00";
				}
				proinst.setIsPushed(ForThirdCoding(ispushed,datasrc));
				commonDao.saveOrUpdate(proinst);
				commonDao.flush();
				msg.setSuccess("true");
				msg.setMsg("推送成功");
			}
			System.out.println("完------------");
		}
		return msg;
	}
	
	public String ForThirdCoding(String code,String datasrc){
		String res = code;
		char[] codeArr = code.toCharArray();
		if(codeArr.length>1){
			if("0".equals(datasrc)){//住建
				res = "1" + codeArr[1] + code.substring(2);
			}else if("1".equals(datasrc)){//国土
				res = codeArr[0] + "1" + code.substring(2);
			}
		}
		return res;
	}
	
	/**
	 * 检查并返回Wfi_promater
	 * @author zhangp
	 * @data 2018年1月31日下午10:02:00
	 * @param proinst_id
	 * @param three
	 * @param filename
	 * @return
	 */
	public Wfi_ProMater findOrCreatePromater(String proinst_id,String three,String filename){
		Wfi_ProMater proMater = new Wfi_ProMater();
		String sql = "select * from " + Common.WORKFLOWDB
			       + "Wfi_ProMater where proinst_id='" + proinst_id
				   + "' and MATERIAL_THREE='" + three 
				   + "' and MATERIAL_NAME='"+filename+"'";
		List<Wfi_ProMater> promaterList = commonDao.getDataList(Wfi_ProMater.class, sql);
		if(promaterList.size()>0){
			proMater = promaterList.get(0);
		}else{
			int index = 100;
			String fulSQL = "select max(MATERIAL_INDEX) as MAXINDEX from "
					+ Common.WORKFLOWDB + "Wfi_ProMater where proinst_id='"
					+ proinst_id + "'";
			List<Map> list = commonDao.getDataListByFullSql(fulSQL);
			if (list != null && list.size() > 0) {
				Map m = list.get(0);
				if (m != null) {
					Object object = m.get("MAXINDEX");
					if (object != null) {
						index = Integer.parseInt(object.toString());
						index++;
					}
				}
			}
			//创建wfi_promater
			proMater.setMaterilinst_Id(Common.CreatUUID());
			proMater.setMaterial_Name(filename);
			proMater.setMaterial_Count(Integer.parseInt("1"));
			proMater.setMaterial_Type(Integer.parseInt("2"));
			proMater.setMaterial_Status(WFConst.MateralStatus.AcceotMateral.value);
			proMater.setMaterial_Index(index);
			proMater.setProinst_Id(proinst_id);
			proMater.setMaterial_Need(1);
			proMater.setMaterial_Desc("第三方推送添加");
			proMater.setMaterial_Three(three);
			commonDao.saveOrUpdate(proMater);
			commonDao.flush();
		}
		return proMater;
	}
	/**
	 * 文件重命名
	 * @author zhangp
	 * @data 2018年1月31日下午10:02:22
	 * @param inst
	 * @param materilinst_id
	 * @return
	 */
	@SuppressWarnings("unused")
	public static String GetNewPath(Wfi_ProInst inst, String materilinst_id) {
		SimpleDateFormat fir = new SimpleDateFormat("yyyyMM");// 设置日期格式
		String first = fir.format(inst.getCreat_Date());
		SimpleDateFormat sec = new SimpleDateFormat("dd");// 设置日期格式
		String second = sec.format(inst.getCreat_Date());
		return first + File.separator + second + File.separator + inst.getProinst_Id() + File.separator + materilinst_id;
	}
	
	
	  public static byte[] file2BetyArray(String path)  
	    {  
	        File file = new File(path);  
	        if (!file.exists()) {  
	            return null;  
	        }  
	        FileInputStream stream = null;  
	        ByteArrayOutputStream out = null;  
	        try {  
	            stream = new FileInputStream(file);  
	            out = new ByteArrayOutputStream(1000);  
	            byte[] b = new byte[1000];  
	            int n;  
	            while ((n = stream.read(b)) != -1) {  
	                out.write(b, 0, n);  
	           }  
	            return out.toByteArray();// 此方法大文件OutOfMemory  
	        } catch (Exception e) {  
	            System.out.println(e.toString());  
	        } finally {  
	            try {  
	                stream.close();  
	                out.close();  
	            } catch (IOException e) {  
	                e.printStackTrace();  
	            }  
	  
	        }  
	        return null;  
	    }   
		@SuppressWarnings("unchecked")
		public static byte[] getFileBytes(String url, JSONObject jsonParam) {
			HttpClient httpClient = new HttpClient();
			PostMethod method = new PostMethod(url);
			byte[] bytes = null;
			//参数传递POST请求
			if(!jsonParam.isEmpty()){
				Object[] entryset = jsonParam.entrySet().toArray();
				NameValuePair[] parametersBody = new NameValuePair[entryset.length]; 
				for(int i=0,j=entryset.length;i<j;i++){
					Entry<String, Object> entry = (Entry<String, Object>) entryset[i];
					parametersBody[i] = new NameValuePair(entry.getKey(), entry.getValue().toString());
				}
				method.setRequestBody(parametersBody);
			}
			try {
				if (null != jsonParam) {
					method.setRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
					method.getParams().setContentCharset("utf-8");
				}
				httpClient.executeMethod(method);
				if (method.getStatusCode() == HttpStatus.SC_OK)
				{
					bytes = method.getResponseBody();
				}
				method.releaseConnection();
			} catch (IOException e) {
			}
			return bytes;
		 }
		@SuppressWarnings("unchecked")
		public static JSONObject httpPost(String url, JSONObject jsonParam) {
			HttpClient httpClient = new HttpClient();
			JSONObject jsonResult = null;
			String  body="";
			PostMethod method = new PostMethod(url);
			//参数传递POST请求
			if(!jsonParam.isEmpty()){
				Object[] entryset = jsonParam.entrySet().toArray();
				NameValuePair[] parametersBody = new NameValuePair[entryset.length]; 
				for(int i=0,j=entryset.length;i<j;i++){
					Entry<String, Object> entry = (Entry<String, Object>) entryset[i];
					parametersBody[i] = new NameValuePair(entry.getKey(), entry.getValue().toString());
				}
				method.setRequestBody(parametersBody);
			}
			try {
				if (null != jsonParam) {
					method.setRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
					method.getParams().setContentCharset("utf-8");
				}
				httpClient.executeMethod(method);
				if (method.getStatusCode() == HttpStatus.SC_OK)
				{
					byte[] bytes = method.getResponseBody();
					body = new String(bytes, "utf-8");
					if(body.startsWith("{")){
						jsonResult = JSONObject.parseObject(body);
					}else if(body.startsWith("[")){
						jsonResult=new JSONObject();
						jsonResult.put("content", JSONArray.parse(body));
					}
				}
				method.releaseConnection();
			} catch (IOException e) {
			}
			return jsonResult;
		 }
		
		public String getToken(){
			String tokenURL = GetProperties.getConstValueByKey("TokenURL");
			String token = "";
			JSONObject tokenJson = new JSONObject();
			tokenJson.put("grant_type", "client_credentials");
			tokenJson.put("client_secret", "admin");
			tokenJson.put("client_id", "admin");
			JSONObject jsonObj = httpPost(tokenURL, tokenJson);
			if(null!=jsonObj&&!StringHelper.isEmpty(jsonObj.getString("access_token"))){
				token = jsonObj.getString("access_token");
			}
			return token;
		}

		public void test() {
			// TODO Auto-generated method stub
			
		}
}
