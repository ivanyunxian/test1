/**
 * 
 * 代码生成器自动生成[WFI_PROINST]
 * 
 */

package com.supermap.wisdombusiness.workflow.service.wfm;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.internal.util.StringHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import com.supermap.realestate.registration.ViewClass.ProjectInfo;
import com.supermap.realestate.registration.service.DYService;
import com.supermap.realestate.registration.service.ProjectService;
import com.supermap.realestate.registration.service.XMLService;
import com.supermap.realestate.registration.service.ZSService;
import com.supermap.realestate.registration.service.impl.ProjectServiceImpl;
import com.supermap.realestate.registration.util.ConfigHelper;
import com.supermap.realestate.registration.util.ConstValue;
import com.supermap.realestate.registration.util.YwLogUtil;
import com.supermap.wisdombusiness.framework.service.DepartmentService;
import com.supermap.wisdombusiness.framework.service.UserService;
import com.supermap.wisdombusiness.web.ResultMessage;
import com.supermap.wisdombusiness.workflow.dao.CommonDao;
import com.supermap.wisdombusiness.workflow.model.Wfd_Actdef;
import com.supermap.wisdombusiness.workflow.model.Wfd_Prodef;
import com.supermap.wisdombusiness.workflow.model.Wfi_ActInst;
import com.supermap.wisdombusiness.workflow.model.Wfi_ActInstStaff;
import com.supermap.wisdombusiness.workflow.model.Wfi_NowActInst;
import com.supermap.wisdombusiness.workflow.model.Wfi_ProInst;
import com.supermap.wisdombusiness.workflow.service.common.SmObjInfo;
import com.supermap.wisdombusiness.workflow.service.common.SmProInfo;
import com.supermap.wisdombusiness.workflow.service.common.WFConst;
import com.supermap.wisdombusiness.workflow.service.wfd.SmHoliday;
import com.supermap.wisdombusiness.workflow.service.wfd.SmProDef;
import com.supermap.wisdombusiness.workflow.service.wfi.SmAbnormal;
import com.supermap.wisdombusiness.workflow.service.wfi.SmActInst;
import com.supermap.wisdombusiness.workflow.service.wfi.SmProInst;
import com.supermap.wisdombusiness.workflow.service.wfi.SmProMater;
import com.supermap.wisdombusiness.workflow.service.wfi.SmRouteInst;
import com.supermap.wisdombusiness.workflow.service.wfi.SmStaff;
import com.supermap.wisdombusiness.workflow.service.wfi.proinstStateModify;
import com.supermap.wisdombusiness.workflow.util.SqlFactory;

@Service("smProInstEXCLEService")
public class SmProInstEXCELService {
	@Autowired
	private CommonDao commonDao;
	@Autowired
	private SmProMater _SmProMater;
	@Autowired
	private SmProInst _SmProInst;
	@Autowired
	private SmActInst _smActInst;
	@Autowired
	private ProjectService projectService;
	@Autowired
	private SmStaff smStaff;
	@Autowired
	private UserService userService;
	@Autowired
	private SmHoliday smHoliday;
	@Autowired
    private SmProDef smProdef;
	@Autowired
	private CommonDao baseCommonDao;
	@Autowired
	private DYService dyService;
	private static final Log logger = LogFactory.getLog(SmProInstEXCELService.class);


	
	
	/**
	 * 判断给业务类别是否存在
	 * 
	 * @param Prodef_ID
	 * */
	public Boolean HasProdef(String Prodef_ID) {
		StringBuilder noWhereSql = new StringBuilder();
		noWhereSql.append("Prodef_ID='");
		noWhereSql.append(Prodef_ID);
		noWhereSql.append("'");
		List<Wfd_Prodef> list = commonDao.findList(Wfd_Prodef.class, noWhereSql.toString());
		if (list.size() == 0) {
			return false;
		} else {
			return true;
		}

	}
	public void createProject(SmProInfo info,BindingResult result,HttpServletRequest request){
		//第n个文件，第n页，第n行，第n单元格
		List<List<List<List<String>>>> listExcel=new ArrayList<List<List<List<String>>>>();;
		String file_path=request.getParameter("file_path");
    	String[] path=file_path.split(",");
    	for(int i=0;i<path.length;i++){
    		List<List<List<String>>> excel=readExcel(path[i]);
    		listExcel.add(excel);
    	}
		try {
			for(int i=0;i<listExcel.size();i++){//n个excel文件遍历
				List<List<List<String>>> Excel=listExcel.get(i);
				List<List<String>> page=Excel.get(0);//第0页=单元信息
				for(int k=1;k<page.size();k++){//遍历行,第一行是标题
					List<String> row=page.get(k);
					SmObjInfo SmObjInfo=createproinfo(info,result);
					SmObjInfo.getName();
					ProjectInfo ProjectInfo=projectService.getProjectInfo(SmObjInfo.getFile_number(), request);//保存XMXX表
					ProjectInfo.getXmbh();
					String bdcdyh=row.get(3);//第四格不动产单元号
					String sql="SELECT DISTINCT DY.BDCDYID,DY.ZL,DY.BDCDYH,DY.FWBM,DY.FH,DY.QSC,DY.ZRZH,DY.SZC,DY.HX,DY.HXJG,DY.YCJZMJ,DY.YCTNJZMJ,DY.FTTDMJ,DY.FWJG,DY.FWLX,DY.YFWYT,DY.YFWJG,DY.YCFTJZMJ " +
							"from BDCK.BDCS_H_XZY DY LEFT JOIN  " +
							"BDCK.BDCS_DJDY_XZ DJDY ON DJDY.BDCDYID=DY.BDCDYID AND DJDY.DJDYID IS NOT NULL " +
							"LEFT JOIN BDCK.BDCS_QL_XZ QL ON QL.DJDYID=DJDY.DJDYID  AND QL.QLLX='4' " +
							"WHERE QL.QLID IS NULL  and DY.BDCDYH like '%"+bdcdyh+"%' ";
					List<Map> map= baseCommonDao.getDataListByFullSql(sql);
					if(map.size()>0){
						//添加不动产BDCDYH
						ResultMessage	resultMessage = dyService.addBDCDY(ProjectInfo.getXmbh(), String.valueOf(map.get(0).get("BDCDYID")));
						YwLogUtil.addYwLog("添加单元", ConstValue.SF.YES.Value, ConstValue.LOG.ADD);
					}
					
				}

			}
		
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public SmObjInfo createproinfo(SmProInfo info,BindingResult result){
		SmObjInfo returnsmObjInfo = null;
		/*
		 * if (info == null || StringHelper.isEmpty(info.getProInst_Name())) {
		 * return null; }
		 */
		if (result.hasErrors()) {
			// YwLogUtil.addYwLog("在办箱：受理项目出错",
			// ConstValue.SF.NO.Value,ConstValue.LOG.ADD);
			logger.error(result);
		} else {
			if (!HasProdef(info.getProDef_ID())) {
				returnsmObjInfo = new SmObjInfo();
				returnsmObjInfo.setID("0");
				returnsmObjInfo.setDesc("请选择受理流程");

			} else {
				SmObjInfo smObjInfo = new SmObjInfo();
				smObjInfo.setID(smStaff.getCurrentWorkStaffID());
				smObjInfo.setName(smStaff.GetStaffName(smObjInfo.getID()));
				List<SmObjInfo> staffList = new ArrayList<SmObjInfo>();
				staffList.add(smObjInfo);
				info.setAcceptor(smObjInfo.getName());
				returnsmObjInfo = Accept(info, staffList);
				// YwLogUtil.addYwLog("在办箱：受理项目：项目名称：" +info.getProInst_Name()
				// +",流程名称：" + info.getProDef_Name(),
				// ConstValue.SF.YES.Value,ConstValue.LOG.ADD);
				// modelMap.addFlashAttribute("resultMsg", "受理成功");
			}
		}
		return returnsmObjInfo;
	}
	/**
	 * 受理项目
	 * 
	 * @param proinfo
	 *            SmProInfo 实例相关信息
	 * @return SmProInfo 返回加工后的实例信息
	 * */
	@Transactional
	public SmObjInfo Accept(SmProInfo proinfo, List<SmObjInfo> smObjInfo) {
		SmObjInfo smInfo = new SmObjInfo();
		if (proinfo.getProInst_ID() != null && proinfo.getProInst_ID() != "") {
			return UpdateProject(proinfo, smObjInfo);
		} else {
			StringBuilder sBuilder = new StringBuilder();
			sBuilder.append("prodef_id='");
			sBuilder.append(proinfo.getProDef_ID());
			sBuilder.append("'");
			sBuilder.append(" and actdef_type='");
			sBuilder.append(WFConst.ActDef_Type.ProcessStart.value);
			sBuilder.append("'");
			List<Wfd_Actdef> actdefs = commonDao.findList(Wfd_Actdef.class, sBuilder.toString());
			if (actdefs != null && actdefs.size() > 0) {
				Wfi_ProInst proInst = _SmProInst.CreatProInst(proinfo);
				if (proInst != null) {
					Wfd_Prodef wfd_prodef = commonDao.get(Wfd_Prodef.class, proinfo.getProDef_ID());
					Wfi_ActInst actInst = _smActInst.AddNewActInst(actdefs.get(0), smObjInfo, "受理项目备注", proInst,wfd_prodef.getHouse_Status());
					if (actInst != null) {
						Wfi_NowActInst nowActInst = _smActInst.CreatNowActInst(actInst);
						//更新环节实例中当前环节的信息：
						proInst.setActdef_Type(actInst.getActdef_Type());
						proInst.setActinst_Id(actInst.getActinst_Id());
						proInst.setIsApplyHangup(actInst.getIsApplyHandup());
						proInst.setStatusext(actInst.getStatusExt());
						proInst.setOperation_Type_Nact(actInst.getOperation_Type());
						proInst.setStaff_Id_Nact(actInst.getStaff_Id());
						proInst.setActinst_Status(actInst.getActinst_Status());
						GregorianCalendar calendar = new GregorianCalendar();
						calendar.setTime(actInst.getActinst_Start());
						if(null==proInst.getProinst_Time()){
							Wfd_Prodef def = smProdef.GetProdefById(proInst.getProdef_Id());
							proInst.setProinst_Time(null!=def.getProdef_Time()?def.getProdef_Time():0);
						}
						proInst.setProinst_Willfinish(smHoliday.addDateByWorkDay(calendar, proInst.getProinst_Time()));
						proInst.setActinst_Willfinish(actInst.getActinst_Willfinish());
						proInst.setActinst_Name(actInst.getActinst_Name());
						proInst.setMsg(actInst.getMsg());
						proInst.setCodeal(actInst.getCodeal());
						proInst.setACTINST_START(actInst.getActinst_Start());
						proInst.setACTINST_END(actInst.getActinst_End());
						proInst.setStaff_Name_Nact(actInst.getStaff_Name());
						if(StringHelper.isEmpty(proInst.getStaff_Id())){
							proInst.setAreaCode(userService.findById(proinfo.getStaffID()).getAreaCode());
						}else{
							proInst.setAreaCode(userService.findById(proInst.getStaff_Id()).getAreaCode());
						}
						
						if(proinfo.getAreaCode()!=null&&!proinfo.getAreaCode().isEmpty()){
							proInst.setAreaCode(proinfo.getAreaCode());
						}
						Wfi_ActInstStaff actinstroute = _smActInst.CreatActStaff(actInst.getActinst_Id(), smObjInfo.get(0), "");
						if (nowActInst != null) {
							commonDao.save(proInst);
							commonDao.save(actInst);
							commonDao.save(nowActInst);
							commonDao.save(actinstroute);
							smInfo.setID(proInst.getProinst_Id());
							smInfo.setDesc("受理成功");
							smInfo.setName(actInst.getActinst_Id());
							smInfo.setFile_number(proInst.getFile_Number());
							//SmObjInfo字段不太够用，先存到children里边吧
							List<SmObjInfo> infoList = new ArrayList<SmObjInfo>();
							SmObjInfo _info = new SmObjInfo();
							_info.setID(proInst.getProlsh());
							_info.setName(proInst.getProject_Name());
							infoList.add(_info);
							smInfo.setChildren(infoList);
						}
					}
				}
				if (proInst != null) {
					proinfo.setProInst_ID(proInst.getProinst_Id());
					// 导入收件资料模版
					AddNewMtrs(proInst.getProdef_Id(), proInst.getProinst_Id());
				}
			}
		}
		commonDao.flush();
		return smInfo;
	}
	public List<?> AddNewMtrs(String ProDefID, String ProInstID) {
		return _SmProMater.CreateProMaterInst(ProDefID, ProInstID, null);
	}
	@Transactional
	public SmObjInfo UpdateProject(SmProInfo proinfo, List<SmObjInfo> smObjInfo) {
		SmObjInfo smInfo = new SmObjInfo();
		String proinstidString = proinfo.getProInst_ID();
		Wfi_ProInst wfi_ProInst = commonDao.get(Wfi_ProInst.class, proinstidString);
		if (wfi_ProInst != null) {
			wfi_ProInst.setProject_Name(proinfo.getProInst_Name());
			commonDao.update(wfi_ProInst);
			commonDao.flush();
			smInfo.setID(proinstidString);
			smInfo.setDesc("修改成功");
			smInfo.setName("修改项目");
		}
		return smInfo;

	}
	/**
	 * 解析Excel，保存Excel
	 * @param request
	 */
	public JSONObject addExcel(HttpServletRequest request){
		JSONObject json=new JSONObject();
		//第n个文件，第n页，第n行，第n单元格
		List<List<List<List<String>>>> listExcel=new ArrayList<List<List<List<String>>>>();
		  //创建一个通用的多部分解析器    
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());    
        //判断 request 是否有文件上传,即多部分请求   
        if(multipartResolver.isMultipart(request)){    
            //转换成多部分request      
            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;  
            // 取得request中的所有文件名  
            Iterator<String> iter = multiRequest.getFileNames(); 
			String ExcelProjectPath=ConfigHelper.getNameByValue("EXCELPROJECTPATH");
			Calendar now = Calendar.getInstance();  
			int year=now.get(Calendar.YEAR);
			int month=now.get(Calendar.MONTH) + 1;
			int day=now.get(Calendar.DAY_OF_MONTH);
			ExcelProjectPath=ExcelProjectPath+"\\"+year+"\\"+month+"\\"+day;
            int successUpload=0;
            int failUpload=0;
            json.put("fail", "0个上传失败");
            StringBuffer path=new StringBuffer();
			while (iter.hasNext()) {  
                // 取得上传文件  
                MultipartFile file = multiRequest.getFile(iter.next());  
                String filename=file.getOriginalFilename();
                try{
                	InputStream input=file.getInputStream();
                	SaveFileFromInputStream(input,ExcelProjectPath,filename);
                	List<List<List<String>>> page=readExcel(ExcelProjectPath+"\\"+filename);
                	listExcel.add(page);
                	path.append(ExcelProjectPath+filename+",");
                	successUpload+=1;
                	json.put("success", successUpload+"个上传完成");
                }catch(Exception e){
                	successUpload-=1;
                	failUpload+=1;
                	json.put("fail", failUpload+"个上传失败");
                	e.printStackTrace();
                }
                
            }
			String paths=path.toString();
			paths=path.substring(0,paths.lastIndexOf(","));
			json.put("path", path.toString());
        }
		return json;   
	}
	/**
	 * 解析Excel
	 * @param filepath
	 * @return
	 */
	public List<List<List<String>>> readExcel(String filepath){
		List<List<List<String>>> page=new ArrayList<List<List<String>>>();
		try {
			InputStream inputStream=new FileInputStream(filepath);
			XSSFWorkbook hssfworkbook=new XSSFWorkbook(inputStream);//整个Excel
        	for(int i=0;i<hssfworkbook.getNumberOfSheets();i++){
        		XSSFSheet hssfsheet=hssfworkbook.getSheetAt(i);//第I页
        		String name=hssfsheet.getSheetName();
        		List<List<String>> row=new  ArrayList<List<String>>();//装行数据
        		for(int j=0;j<=hssfsheet.getLastRowNum();j++){
        			XSSFRow hssfrow=hssfsheet.getRow(j);//第I页第J行
        			int mincoll=hssfrow.getFirstCellNum();
        			int maxcoll=hssfrow.getLastCellNum();
        			List<String> cellval=new ArrayList<String>();//装单元格数据
        			for(int cellnum=mincoll;cellnum<maxcoll;cellnum++){
        				XSSFCell cell=hssfrow.getCell(cellnum);//单元格
        				if(cell!=null){
        				cell.setCellType(Cell.CELL_TYPE_STRING);
        				String val=cell.getStringCellValue();
        				cellval.add(val);
        				}
        			}
        			row.add(cellval);
        		}
        		page.add(row);
        	}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return page;
	}
	/**
	 * 保存Excel文件
	 * @param stream
	 * @param path
	 * @param filename
	 * @return
	 * @throws IOException
	 */
	   public InputStream SaveFileFromInputStream(InputStream stream,String path,String filename) throws IOException{
//		   File file=new File(path );
//		   if(!file.exists()){
//			   file.mkdirs();
//		   }
//	        FileOutputStream fs=new FileOutputStream( new File(path + "/"+ filename));
	        
	        File dir = new File(path);
	        if (!dir.exists())  
            {  
                dir.mkdirs();  
            } 
	        File file = new File(path + File.separator + filename);
//	        File file = new File("C:\\ExcelProjectFile\\2017\\5\\22" + File.separator + filename);

	        FileOutputStream fs= new FileOutputStream(file); 
	        byte[] buffer =new byte[1024*1024];
	        int bytesum = 0;
	        int byteread = 0; 
	        while ((byteread=stream.read(buffer))!=-1)
	        {
	           bytesum+=byteread;
	           fs.write(buffer,0,byteread);
	           fs.flush();
	        } 
	        fs.close();
	        stream.close();      
	        return new FileInputStream(path + "/"+ filename);
	    }     
}
