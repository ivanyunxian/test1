package com.supermap.realestate_gx.registration.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.supermap.bdcapi.projectAPI;
import com.supermap.bdcapi.workflowAPI;
import com.supermap.realestate.registration.ViewClass.ProjectInfo;
import com.supermap.realestate.registration.model.BDCS_SQR;
import com.supermap.realestate.registration.service.ProjectService;
import com.supermap.realestate.registration.util.ConfigHelper;
import com.supermap.realestate.registration.util.GetProperties;
import com.supermap.realestate.registration.util.Global;
import com.supermap.realestate.registration.util.RequestHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.realestate_gx.registration.common.CommonDao;
import com.supermap.realestate_gx.registration.model.Dyywfjzl;
import com.supermap.realestate_gx.registration.model.Dyywrtsb;
import com.supermap.realestate_gx.registration.model.Dyywsjhq;
import com.supermap.realestate_gx.registration.model.Dyywtsb;
import com.supermap.realestate_gx.registration.service.IAccumufundService;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDaoInline;
import com.supermap.wisdombusiness.synchroinline.service.AcceptProjectService;
import com.supermap.wisdombusiness.synchroinline.util.InlineFileItem;
import com.supermap.wisdombusiness.web.Message;
import com.supermap.wisdombusiness.web.ResultMessage;
import com.supermap.wisdombusiness.workflow.model.Wfd_ProClass;
import com.supermap.wisdombusiness.workflow.model.Wfi_MaterData;
import com.supermap.wisdombusiness.workflow.model.Wfi_ProInst;
import com.supermap.wisdombusiness.workflow.model.Wfi_ProMater;
import com.supermap.wisdombusiness.workflow.service.common.Common;
import com.supermap.wisdombusiness.workflow.service.common.SmObjInfo;
import com.supermap.wisdombusiness.workflow.service.common.SmProInfo;
import com.supermap.wisdombusiness.workflow.service.common.WFConst;
import com.supermap.wisdombusiness.workflow.service.wfi.SmProMater;
import com.supermap.wisdombusiness.workflow.service.wfi.SmStaff;
import com.supermap.wisdombusiness.workflow.service.wfm.SmProInstService;
import com.supermap.wisdombusiness.workflow.util.FileUpload;
/*
 * 桂林公积金
 */
@Service("accumufundService")
@Transactional
public class AccumufundServiceImpl implements IAccumufundService{

	@Autowired
	private CommonDao commonDao;
	
	@Autowired
	private AcceptProjectService acceptProjectService; 
	
	@Autowired
	private SmProInstService smProInstService;
	
	@Autowired
	private com.supermap.internetbusiness.dao.CommonDao dao;
	
	@Autowired
	private ProjectService projectService;
	
	@Autowired
	private SmProMater smProMater;
	
	@Autowired
	private SmStaff smStaff;
	
	@Autowired
	CommonDaoInline baseCommonDaoInline;
	
	private static String xfdy;
    private static String qfdy;
    
	static{
		xfdy = GetProperties.getValueByFileName("gl_gjj.properties", "xfdy");
		qfdy = GetProperties.getValueByFileName("gl_gjj.properties", "qfdy");
	   }

	
	@Override
	public Map Gjjcondition(HttpServletRequest request,
			HttpServletResponse response){
		Map map = new HashMap();
		try {
			String qlrmc = RequestHelper.getParam(request, "qlrmc");
			String zjh = RequestHelper.getParam(request, "zjh");
			String slzt = RequestHelper.getParam(request, "slzt");
			String hql=" 1=1 ";
			if (qlrmc!=null && !qlrmc.equals("")) {
				hql=hql+" AND QLRMC='"+qlrmc+"'";
			}
			if (zjh!=null && !zjh.equals("")) {
				hql=hql+" AND ZJH='"+zjh+"'";
			}
			if (slzt!=null && !slzt.equals("")) {
				hql=hql+" AND SLZT='"+slzt+"'";
			}
			List<Map> dyywtsbList=commonDao.getDataListByFullSql("SELECT * FROM DYYWTSB WHERE"+hql+" ORDER BY FHSJ DESC");
			map.put("proname", dyywtsbList);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return map;
	}




	@Override
	public Map GetGjjList() {
		Map map = new HashMap();
		String sql="SELECT * FROM DYYWTSB";
		List<Map> dyywtsbList=commonDao.getDataListByFullSql(sql);
		map.put("proname", dyywtsbList);
		return map;
	}


	@Override
	public Map GjjdetailsList(HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException {
		String gjjywlsh = RequestHelper.getParam(request, "gjjywlsh");
		Map map = new HashMap();
		String sql="SELECT tsb.*,ywr.ywr,ywr.ywrzjzl,ywr.ywrzjh,ywr.bdcqzh as ywrbdcqzh,ywr.sqrlx as ywrsqrlx,ywr.gyfs as ywrgyfs,ywr.dh as ywrdh,ywr.dz as ywrdz,ywr.fddbr as ywrfddbr,ywr.fddbrdh as ywrfddbrdh,ywr.fddbrzjhm as ywrfddbrzjhm"
				+ ",ywr.dlrxm as ywrdlrxm,ywr.dlrzjhm as ywrdlrzjhm,ywr.dlrlxdh as ywrdlrlxdh "
				+ "FROM DYYWTSB tsb LEFT JOIN DYYWYWR ywr ON  ywr.GJJYWLSH=tsb.GJJYWLSH WHERE ywr.GJJYWLSH='"+gjjywlsh+"'";
//		String sql="SELECT tsb.*,ywr.ywr,ywr.dlrxm as ywrdlrxm FROM DYYWTSB tsb LEFT JOIN DYYWYWR ywr ON  ywr.GJJYWLSH=tsb.GJJYWLSH WHERE tsb.GJJYWLSH='"+gjjywlsh+"'";
		List<Dyywrtsb> dyywtsbsList=commonDao.getDataList(Dyywrtsb.class, sql);
		map.put("proname", dyywtsbsList);
		return map;
	}


	/**
	 * 桂林公积金受理
	 */
	@Override
	public Message GjjaccectProject(HttpServletRequest request,HttpServletResponse response) {
		Message msg=new Message();
		try {
			String gjjywlsh = RequestHelper.getParam(request, "gjjywlsh");
			//抵押业务数据推送表
			String sql="SELECT * FROM DYYWTSB WHERE GJJYWLSH='"+gjjywlsh+"'";
			List<Dyywtsb> dyywtsbsList=commonDao.getDataList(Dyywtsb.class, sql);
			
			//附件资料表
			String flsql="SELECT * FROM DYYWFJZL WHERE GJJYWLSH='"+gjjywlsh+"'";
			List<Dyywfjzl> fllist=commonDao.getDataList(Dyywfjzl.class, flsql);
			//抵押业务义务人表
//			String dyywywrSql="SELECT * FROM DYYWYWR WHERE GJJYWLSH='"+gjjywlsh+"'";
//			List<Dyywywr> dyywywrList=commonDao.getDataList(Dyywywr.class, dyywywrSql);
			
			SmProInfo proinfo = new SmProInfo();
		    SmObjInfo smObjInfo = new SmObjInfo();
		    smObjInfo.setID(Global.getCurrentUserInfo().getId());
	        smObjInfo.setName(Global.getCurrentUserInfo().getUserName());
	        List<Object> staffList = new ArrayList<Object>();
	        staffList.add(smObjInfo);
	        proinfo.setFile_Urgency("1");
	        proinfo.setAcceptor(smObjInfo.getName());
	        if (dyywtsbsList.get(0).getYwlx().equals("01")) {
		        proinfo.setProDef_ID(qfdy);
			}else {
		        proinfo.setProDef_ID(xfdy);
			}
	        
	        String prodefSql="select * from BDC_WORKFLOW.WFD_PRODEF a where a.prodef_id='"+proinfo.getProDef_ID()+"'";
	        List<Map> prodefList=dao.getDataListByFullSql(prodefSql);
	        
	        //项目名称
            proinfo.setProInst_Name(dyywtsbsList.get(0).getQlrmc());
            String def_name_string = "";
            Stack<String> defname = new Stack<String>();
            StringBuilder def_name_sb = new StringBuilder();
            
            defname.push(String.valueOf(StringHelper.formatObject(prodefList.get(0).get("PRODEF_NAME"))));
            getWorkflowDefName(defname, String.valueOf(StringHelper.formatObject(prodefList.get(0).get("PRODEFCLASS_ID"))));
            
            while (!defname.isEmpty()) {
                String v = defname.pop();
                def_name_sb.append(v + ",");
            }
            if (def_name_sb.toString().length() > 0)
                def_name_string = def_name_sb.toString().substring(0, def_name_sb.toString().length() - 1);
            proinfo.setProDef_Name(def_name_string);
            
	        Object returnsmObjInfo = new workflowAPI().Accept(proinfo, staffList);
	        SmObjInfo sm = (SmObjInfo) returnsmObjInfo;
            projectAPI projectapi = new projectAPI();
            
            //项目编号
            Wfi_ProInst inst = dao.get(Wfi_ProInst.class,sm.getID());
            ProjectInfo projectInfo = projectService.getProjectInfo(inst.getFile_Number(), request);
            //没有获取到流水号
            //根据公积金BDCDYH生成单元信息
            String bdcdyh = "";
            bdcdyh = StringHelper.formatObject(dyywtsbsList.get(0).getBdcdyh());
            Object  resultMessage = null;
        	if (proinfo.getProDef_ID().equals(xfdy)) {
                List<Map> hList = dao.getDataListByFullSql("select bdcdyid from bdck.bdcs_h_xz h where h.bdcdyh='"+bdcdyh+"' ");
            	resultMessage = projectapi.addBDCDY(projectInfo.getXmbh(), StringHelper.formatObject(hList.get(0).get("BDCDYID")));
        	}else if (proinfo.getProDef_ID().equals(qfdy)) {
                List<Map> hList = dao.getDataListByFullSql("select bdcdyid from bdck.bdcs_h_xzy h where h.bdcdyh='"+bdcdyh+"' ");
            	resultMessage = projectapi.addBDCDY(projectInfo.getXmbh(), StringHelper.formatObject(hList.get(0).get("BDCDYID")));
            } 
            ResultMessage re = (ResultMessage) resultMessage;
            
			//添加权利人信息
			if (dyywtsbsList!=null) {
				 for (Dyywtsb dyywtsb : dyywtsbsList) {
		                BDCS_SQR sqr=new BDCS_SQR();
		                
		                sqr.setXMBH(projectInfo.getXmbh());
		                sqr.setSQRXM(dyywtsb.getQlrmc());
		                sqr.setZJLX(dyywtsb.getZjzl());
		                sqr.setZJH(dyywtsb.getZjh());
		                sqr.setSQRLX(dyywtsb.getSqrlx());
		                //抵押权利人
		                sqr.setSQRLB("1");
		                sqr.setLXDH(dyywtsb.getDh());
		                //公积金抵押权人为单独所有
		                sqr.setGYFS("0");
		                
		                sqr.setFDDBR(dyywtsb.getFddbr());
		                sqr.setFDDBRDH(dyywtsb.getFddbrdh());
		                sqr.setFDDBRZJHM(dyywtsb.getFddbrzjhm());
		                
		                sqr.setDLRXM(dyywtsb.getDlrxm());
		                sqr.setDLRZJHM(dyywtsb.getDlrzjhm());
		                sqr.setDLRLXDH(dyywtsb.getDlrlxdh());
		                dao.save(sqr);
					}
				 dao.flush();
			}
			dao.excuteQuery("update bdck.bdcs_xmxx set  ywlsh='"+inst.getProlsh()+"', gjjywlsh='"+dyywtsbsList.get(0).getGjjywlsh()+"' where xmbh='" + projectInfo.getXmbh() + "'");
			//添加义务人信息
//			if (dyywywrList!=null) {
//				for (Dyywywr dyywywr : dyywywrList) {
//	                
//	            	UUID uuid = UUID.randomUUID();
//	    	        String str = uuid.toString();
//	    	        sqr.setId(str);
//	            	sqr.setXMBH(projectInfo.getXmbh());
//	                sqr.setSQRXM(dyywywr.getYWR());
//	                sqr.setZJLX(dyywywr.getYWRZJZL());
//	                sqr.setSQRLX(dyywywr.getSQRLX());
//	                sqr.setZJH(dyywywr.getYWRZJH());
//	                //义务人
//	                sqr.setSQRLB("2");
//	                sqr.setLXDH(dyywywr.getDH());
//	                sqr.setGYFS(dyywywr.getGYFS());
//	                
//	                sqr.setFDDBR(dyywywr.getFDDBR());
//	                sqr.setFDDBRDH(dyywywr.getFDDBRDH());
//	                sqr.setFDDBRZJHM(dyywywr.getFDDBRZJHM());
//	                
//	                sqr.setDLRXM(dyywywr.getDLRXM());
//	                sqr.setDLRZJHM(dyywywr.getDLRZJHM());
//	                sqr.setDLRLXDH(dyywywr.getDLRLXDH());
//	                dao.save(sqr);
//				}
//				dao.flush();
//			}
			
			/**
			 * 项目受理成功,公积金修改受理状态
			 */
			for (int i = 0; i < dyywtsbsList.size(); i++) {
				String xmxxsql="select gjjywlsh from bdck.bdcs_xmxx where gjjywlsh='"+dyywtsbsList.get(i).getGjjywlsh()+"'";
				List<Map> xmxxList=dao.getDataListByFullSql(xmxxsql);
				if (xmxxList!=null && xmxxList.size()>0 && !dyywtsbsList.get(i).getSlzt().equals("1")) {
					commonDao.excuteQuery("update dyywtsb set slzt='1' where gjjywlsh='"+xmxxList.get(i).get("GJJYWLSH")+"'");
				}
			}
			
			
			String MATERIAL = ConfigHelper.getNameByValue("MATERIAL");//扫描件存储位置
			String syspath = ConfigHelper.getNameByValue("filepath");
			Wfi_ProMater proMaterOther = smProMater.CreatProMater(fllist.get(0).getName(), inst.getProinst_Id());			
			for (Dyywfjzl file : fllist) {
				Wfi_MaterData materData = new Wfi_MaterData();
				materData.setMaterialdata_Id(Common.CreatUUID());
				materData.setFile_Name(file.getName());
				materData.setMaterilinst_Id(proMaterOther.getMaterilinst_Id());
				materData.setFile_Index(1);
				materData.setUpload_Id(smStaff.getCurrentWorkStaff().getId());
				materData.setUpload_Name(smStaff.getCurrentWorkStaff().getUserName());
				materData.setThumb(null);
				materData.setUpload_Date(new Date());
				materData.setDisc(MATERIAL);
				String fileName = file.getDnlj();
				String fileNamepath =syspath+File.separator+file.getDnlj();
				File this_file = new File("D:\\img\\1.jpg");
			    FileInputStream in = new FileInputStream(this_file);
			    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			    byte [] buffer = new byte[1024];  
			    int len = 0;
			    while( (len = in.read(buffer)) != -1){  
			    	outputStream.write(buffer, 0, len);  
			    } 
			    File tempFile = new File("C:\\Windows\\Temp\\" + Common.CreatUUID());
			    
			    tempFile.createNewFile();
			    java.io.FileOutputStream fos = new FileOutputStream(tempFile);
			    fos.write(outputStream.toByteArray());
			    fos.flush();
			    fos.close();
				InlineFileItem dfi = new InlineFileItem(fileName, tempFile);
				dfi.getOutputStream();
				CommonsMultipartFile cmf = new CommonsMultipartFile(dfi);
				@SuppressWarnings("rawtypes")
				List<Map> maps = FileUpload.uploadFile(cmf, proMaterOther.getMaterial_Id(), inst);
				if (maps != null && maps.size() > 0)
				{
					materData.setFile_Path(maps.get(0).get("filename").toString());
					materData.setPath(maps.get(0).get("filepath").toString());
				}
				proMaterOther.setImg_Path(materData.getMaterialdata_Id());
				dao.save(materData);
				dao.flush();
			}
			
			
			proMaterOther.setMaterial_Status(WFConst.MateralStatus.AcceotMateral.value);
			proMaterOther.setMaterial_Count(1);
			dao.save(proMaterOther);
			dao.flush();
			
			
			
            if (re!=null ) {
                 msg.setSuccess(re.getSuccess());
            	 msg.setMsg(sm.getName() + "@"+ re.getMsg());
			}else {
				 msg.setMsg(sm.getName() + "@");
			}
			} catch (Exception e) {
		}
		return msg;
	}
	
	private void getWorkflowDefName(Stack<String> names, String id) {
        Wfd_ProClass w = dao.get(Wfd_ProClass.class, id);
        if (w != null) {
            names.push(w.getProdefclass_Name());
            if (!StringUtils.isEmpty(w.getProdefclass_Pid())) {
                getWorkflowDefName(names, w.getProdefclass_Pid());
            }
        }
    }
	
	
	//公积金登簿推送至DYYWSJHQ表
	public void GjjZJK(String xmbh){
		String xmxxSql="SELECT xmxx.xmbh,ql.qllx,ql.bdcqzh,ql.bdcdyh,h.zl,h.fwxz,h.fwjg,h.szc,h.scjzmj,h.scftjzmj,ql.djsj,xmxx.gjjywlsh FROM  BDCK.BDCS_XMXX XMXX "
				+ "LEFT JOIN BDCK.BDCS_QL_XZ QL ON QL.XMBH=XMXX.XMBH "
                + "LEFT JOIN BDCK.BDCS_H_XZ H ON H.BDCDYH=QL.BDCDYH "
				+ "WHERE XMXX.XMBH='"+xmbh+"'";
		List<Map> xmxxlist=dao.getDataListByFullSql(xmxxSql);
		//抵押业务数据推送表
		String sql="SELECT * FROM DYYWTSB WHERE GJJYWLSH='"+xmxxlist.get(0).get("GJJYWLSH")+"'";
		List<Dyywtsb> dyywtsbsList=commonDao.getDataList(Dyywtsb.class, sql);
		if (xmxxlist.size()>0 && xmxxlist!=null) {
			Dyywsjhq dyywsjhq=new Dyywsjhq();
			UUID uuid = UUID.randomUUID();
	        String str = uuid.toString();
			dyywsjhq.setBdcdyh(StringHelper.formatObject(xmxxlist.get(0).get("BDCDYH")));
			if (xmxxlist.get(0).get("QLLX").equals("4")) {
				dyywsjhq.setBdcqzh(StringHelper.formatObject(xmxxlist.get(0).get("BDCQZH")));
			}
			dyywsjhq.setGjjywlsh(dyywtsbsList.get(0).getGjjywlsh());
			dyywsjhq.setBzxx(dyywtsbsList.get(0).getBzxx());
			dyywsjhq.setCfbs("");
			dyywsjhq.setCfsj("");
			//登记证明号的权利是23，不动产权证号的权利是4
			if (xmxxlist.get(0).get("QLLX").equals("23")) {
				dyywsjhq.setDjzmh(StringHelper.formatObject(xmxxlist.get(0).get("DJZMH")));
				dyywsjhq.setDyqk("1");
				dyywsjhq.setBdcqzh(StringHelper.formatObject(""));
			}else{
				dyywsjhq.setDjzmh("");
				dyywsjhq.setDyqk("0");
			}
			dyywsjhq.setBdcywlsh(dyywtsbsList.get(0).getBdcywlsh());
	        dyywsjhq.setId(str);
//			if (xmxxlist.get(0).get("qllx").equals("4") && xmxxlist.get(0).get("djlx").equals("800")) {
//				dyywsjhq.setCfbs("已查封");
//			}else {
//				dyywsjhq.setCfbs("无查封");
//			}
			dyywsjhq.setZl(StringHelper.formatObject(xmxxlist.get(0).get("ZL")));
			dyywsjhq.setFwxz(StringHelper.formatObject(xmxxlist.get(0).get("FWXZ")));
			dyywsjhq.setFwjg(StringHelper.formatObject(xmxxlist.get(0).get("FWJG")));
			dyywsjhq.setSzc(StringHelper.formatObject(xmxxlist.get(0).get("SZC")));
			dyywsjhq.setJzmj(StringHelper.formatObject(xmxxlist.get(0).get("SCJZMJ")));
			dyywsjhq.setFtjzmj(StringHelper.formatObject(xmxxlist.get(0).get("SCFTJZMJ")));
			dyywsjhq.setDybjsj("");
			commonDao.save(dyywsjhq);
		}
		commonDao.flush();
		
		String djsj=StringHelper.formatObject(xmxxlist.get(0).get("DJSJ")).substring(0,19);
		try {
			commonDao.excuteQuery("update GLSZFGJJ.DYYWSJHQ set  DYBJSJ=TO_DATE('"+djsj+"','yyyy-mm-dd hh24:mi:ss') WHERE GJJYWLSH='"+dyywtsbsList.get(0).getGjjywlsh()+"'");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	/**
	 * 根据gjjywlsh查询附件
	 */
	@Override
	public Map GjjfjList(HttpServletRequest request,HttpServletResponse response)throws UnsupportedEncodingException {
		Map map=new HashMap();
		String gjjywlsh = RequestHelper.getParam(request, "gjjywlsh");
		String sql="SELECT * FROM DYYWFJZL WHERE GJJYWLSH='"+gjjywlsh+"'";
		List<Map> fjList=commonDao.getDataListByFullSql(sql);
		map.put("proname", fjList);
		return map;
	}

}
