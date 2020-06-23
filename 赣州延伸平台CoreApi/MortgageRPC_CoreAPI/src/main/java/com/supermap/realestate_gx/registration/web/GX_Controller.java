package com.supermap.realestate_gx.registration.web;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.text.SimpleDateFormat;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.supermap.realestate.registration.ViewClass.DJInfoExtend;
import com.supermap.realestate.registration.ViewClass.SQSPB;
import com.supermap.realestate.registration.ViewClass.SQSPBex;
import com.supermap.realestate.registration.model.BDCS_DJDY_GZ;
import com.supermap.realestate.registration.model.BDCS_H_GZ;
import com.supermap.realestate.registration.model.BDCS_H_XZ;
import com.supermap.realestate.registration.service.ProjectService;
import com.supermap.realestate.registration.util.ConfigHelper;
import com.supermap.realestate.registration.util.ConstHelper;
import com.supermap.realestate.registration.util.QRCodeHelper;
import com.supermap.realestate.registration.util.RequestHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.realestate.registration.util.ZipCompressorByAnt;
import com.supermap.realestate_gx.registration.service.impl.SQSPServiceImpl;
import com.supermap.wisdombusiness.core.SuperHelper;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import com.supermap.wisdombusiness.web.Message;
import com.supermap.wisdombusiness.workflow.model.Wfd_Prodef;
import com.supermap.wisdombusiness.workflow.service.common.SmObjInfo;
import com.supermap.wisdombusiness.workflow.service.common.SmProInfo;
import com.supermap.wisdombusiness.workflow.service.wfd.SmProDef;
import com.supermap.wisdombusiness.workflow.service.wfi.SmStaff;
import com.supermap.wisdombusiness.workflow.service.wfm.SmProDefService;
import com.supermap.wisdombusiness.workflow.service.wfm.SmProInstService;
import com.supermap.realestate_gx.registration.service.GX_Service;
@Controller
@RequestMapping("/gx")
public class GX_Controller {

    @Autowired
    private CommonDao baseCommonDao;
    @Autowired
    private SmProDef smProdef;
    @Autowired
    private SmStaff smStaff;
    @Autowired
    private SmProInstService smProInstService;
    @Autowired
    SmProDefService _SmProDefService;
    @Autowired
    private SQSPServiceImpl sqspbService;
    @Autowired
    private GX_Service gxService;
    /**
     * ProjectService
     */
    @Autowired
    private ProjectService projectService;
    

    @RequestMapping(value = "/{project_id}/djinfo", method = RequestMethod.GET)
    public @ResponseBody DJInfoExtend GetDJInfo(
	    @PathVariable("project_id") String project_id) {
	DJInfoExtend info = new DJInfoExtend();
	info = DJInfoExtend.Create(project_id, baseCommonDao);
	return info;
    }
    
    /**
	 * @作者 wuz
	 * 对外向非登记系统提供获取二维码图片接口。传入参数，生成二维码图片
	 * @创建时间 2015年12月4日下午2:25:01
	 * @param request
	 * @param response
	 * @param width 二维码的宽度 可缺省 默认120PX
	 * @param height 二维码的高度 可缺省 默认120PX
	 * @param content 二维码的内容
	 * @param imgformat 二维码的图片格式 可缺省 默认PNG
	 * @throws IOException
	 */
	@RequestMapping(value = "getqrcode", method = RequestMethod.GET)
	public void GetQRCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String width = request.getParameter("width");
		String height = request.getParameter("height");
		String content = RequestHelper.getParam(request, "content");
		String imgformat = request.getParameter("imgformat");
		if (StringUtils.isEmpty(width))
			width = "120";
		if (StringUtils.isEmpty(height))
			height = "120";
		if (StringUtils.isEmpty(content))
			content = "";
		if (StringUtils.isEmpty(imgformat))
			imgformat = "png";
		BufferedImage  img = QRCodeHelper.CreateQRCode(content, imgformat, Integer.parseInt(width), Integer.parseInt(height));
		response.setContentType("image/"+imgformat+"; charset=GBK");
		response.setHeader("Pragma","no-cache");
		 response.setHeader("Cache-Control","no-cache");
		 response.setIntHeader("Expires",-1);
		 ImageIO.write(img,imgformat,response.getOutputStream());
	}
	
	
	/**
     * 南宁创建项目
     * @作者 Ouzr
     * @创建时间 2016年5月15日下午7:52:02
     * @param request
     * @param response
     * @return
	 * @throws UnsupportedEncodingException 
     */
    @RequestMapping(value = "/operation/acceptproject", method = RequestMethod.POST)
    @ResponseBody
    public SmObjInfo acceptProject_nn(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        String prodefid = request.getParameter("prodefid");
        String staffid = request.getParameter("staffid");
        String proInstName = URLDecoder.decode(request.getParameter("proInstName"), "UTF-8");
        String proDefName = URLDecoder.decode(request.getParameter("proDefName"), "UTF-8");
        String file_Urgency = request.getParameter("File_Urgency");
        String message = URLDecoder.decode(request.getParameter("Message"), "UTF-8");
        
        if (prodefid != null && !prodefid.equals("")) {
            SmProInfo info = new SmProInfo();
            info.setProDef_ID(prodefid);
            info.setBatch(UUID.randomUUID().toString().replace("-", ""));
            Wfd_Prodef prodef = smProdef.GetProdefById(prodefid);
            info.setProDef_Name(proDefName);
            info.setMessage(message);
            info.setFile_Urgency(file_Urgency);
            info.setLCBH(prodef.getProdef_Code());
            info.setProInst_Name(proInstName);
            SmObjInfo smObjInfo = new SmObjInfo();
            smObjInfo.setID(staffid);
            smObjInfo.setName(smStaff.GetStaffName(smObjInfo.getID()));
            List<SmObjInfo> staffList = new ArrayList<SmObjInfo>();
            staffList.add(smObjInfo);
            info.setAcceptor(smObjInfo.getName());
            info.setStaffID(staffid);
            info.setFile_Urgency("1");
            return smProInstService.Accept(info, staffList);
        } else {
            return null;
        }

    }
    
    @RequestMapping(value = "/{staffid}/prodefinfos",method = RequestMethod.GET)
    public @ResponseBody void GetProDef(
            @PathVariable("staffid") String staffid,
            HttpServletRequest request, HttpServletResponse response) throws IOException {
            
            response.setHeader("Content-type","text/html;charset=UTF-8"); //指定消息头以UTF-8码表读数据 
            String data=_SmProDefService.GetProDefInfos_NN(staffid);
            response.getOutputStream().write(data.getBytes("utf-8"));
    }
    @RequestMapping(value = "/{xmbh}/{acinstid}/sqspb", method = RequestMethod.GET)
    public @ResponseBody Map GetSQSPBInfo(@PathVariable("xmbh") String xmbh, @PathVariable("acinstid") String acinstid, HttpServletRequest request, HttpServletResponse response) {
        
        SQSPBex sqspbex = projectService.GetSQSPBex(xmbh, acinstid, request);
        SQSPB sqspb = sqspbex.converToSQSPB();
        
        Map<String, String> tempMap = sqspbService.getBdcdyInfo(xmbh);
        
        Map resultMap = new HashMap();
        resultMap.put("sqspb", sqspb);
        resultMap.put("ex", tempMap);
        return resultMap;
    }
    @RequestMapping(value={"/{xmbh}/qsb"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
    @ResponseBody
    public Message GetQsbList(@PathVariable("xmbh") String xmbh, HttpServletRequest request, HttpServletResponse response)
    {
      Message m = new Message();
      m = this.gxService.GetFZList(xmbh);
      return m;
    }
	/**
	 * 收件资料批量下载
	 * @author Heks
	 * @date 2017/3/2   16:13:43
	 * @param acinstid
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
    @RequestMapping(value = "/plxz/{acinstid}", method = RequestMethod.GET)
    public  @ResponseBody String GetSQSPBInfo(@PathVariable("acinstid") String acinstid,
    		HttpServletRequest request, HttpServletResponse response) throws Exception{
    	 BufferedInputStream bins=null;
    	 BufferedOutputStream bouts=null;
    	List<Map> list=baseCommonDao.getDataListByFullSql("select a.*,b.material_name from BDC_WORKFLOW.WFI_MATERDATA a,BDC_WORKFLOW.WFI_PROMATER b,BDC_WORKFLOW.WFI_ACTINST c where "+
       "a.MATERILINST_ID = b.MATERILINST_ID and b.PROINST_ID = c.PROINST_ID and c.ACTINST_ID ='"+acinstid+"'order by  b.MATERIAL_INDEX,a.file_index");
       String basicPath = ConfigHelper.getNameByValue("material");
       String realpath="",fullpath="";
       if(list!=null&&list.size()>0){
    	   realpath=String.valueOf(list.get(0).get("PATH")).replace(String.valueOf(list.get(0).get("MATERILINST_ID")), "");
    	   fullpath=basicPath+realpath;
       }
       if(fullpath=="")
    	   return "无上传资料";
       File dir = new File(fullpath);
       
      //新建一个文件夹，用于重命名
       String fulSql = "select p.* from BDC_WORKFLOW.Wfi_ProInst  p "
       		+ "join bdc_WORKFLOW.Wfi_Actinst a on p.proinst_id = a.proinst_id "
       		+ "where a.actinst_id = '"+acinstid+"'";
       List<Map> projectList= baseCommonDao.getDataListByFullSql(fulSql);
      //获取项目名称
       String xmmc = "";
       if(projectList.size() > 0){
    	   String pro_name = (String) projectList.get(0).get("PROJECT_NAME");
    	   if(pro_name != null ){
    		   xmmc = pro_name;
    	   }
       }
       String fileurl = dir.toString().substring(0, dir.toString().lastIndexOf("\\")) + "\\"+xmmc;
       
       File copyfile =new File(fileurl);    
    	//如果文件夹不存在则创建    
       	if  (!copyfile .exists()  && !copyfile .isDirectory())      
       	{       
       		System.out.println("//不存在");  
       		copyfile .mkdirs();    
       	} else   
       	{  
       		System.out.println("//目录存在,那就不用创建了！！"); 
       		
       	}  
       	File cfile = new File(fileurl);
       	String[]  basicPathStr=basicPath.split(":");
      // if(dir.exists() && dir.isDirectory()){
    	   String[] cfilelist = cfile.list();
    	   //批量下载分布式判断
//    	   1.判断同一个项目是否有其他盘符的图

    	   String hardDisksConfigs = ConfigHelper.getNameByValue("harddisks");
    	   String[]  hardDisks =hardDisksConfigs.split(",");
    	  
    	   for(String  Disk:hardDisks) {
    		   String path =Disk+":"+basicPathStr[1]+realpath;
    		   File filedir=new File(path);
    		   if(filedir.exists()) {
//    	    	   2.判断处理
    			   listFileInDir(filedir,filedir,copyfile);
    		   }
    		   
    		   
    	//   }
//    	   if(cfile.exists() && cfilelist.length == 0){
//    		   listFileInDir(dir,dir,copyfile);
//    	   }
    	   String ss = realpath.substring(0, realpath.lastIndexOf("\\"));
    	   String copy_realpath = ss.substring(0, ss.lastIndexOf("\\"));
    	   String copy_fullpath = basicPath+copy_realpath+"\\"+xmmc+"\\";
    	   readfile(copy_fullpath,list);
    	   
    	  ZipCompressorByAnt zs=new ZipCompressorByAnt(cfile.getAbsolutePath()+".zip");
    	  zs.compressExe(cfile.getAbsolutePath());
       }
       
       String path=cfile.getAbsolutePath()+".zip";
       File file=new File(path);
       try { 
         	if (file.exists()) {                    
         		InputStream ins = new FileInputStream(path);                     
         		bins = new BufferedInputStream(ins);// 放到缓冲流里面                  
         		OutputStream outs = response.getOutputStream();// 获取文件输出IO流                   
         		bouts = new BufferedOutputStream(outs);   
         		
         		response.setContentType("application/x-download");// 设置response内容的类型                  
         		response.setHeader( "Content-disposition",                            
         				"attachment;filename="+ URLEncoder.encode(file.getName(), "UTF-8"));// 设置头部信息                   
         		int bytesRead = 0;                    
         		byte[] buffer = new byte[8192];                    
         		// 开始向网络传输文件流                     
         		while ((bytesRead = bins.read(buffer, 0, 8192)) != -1) { 
                       bouts.write(buffer, 0, bytesRead); 
          }                 
                        bouts.flush();                  
                        ins.close();                    
                        bins.close();                   
                        outs.close();                   
                        bouts.close();                
         	} else { 
         		           
         		}             
         	} catch (IOException e) {              
         		System.out.println("下载出错了！");       
         		} finally {
         			//关闭IO流
         			if(bins!=null) {
         				bins.close();
         			}
         			if(bouts!=null) {
         				bouts.close();
         			}
         			//删除临时文件
         			if(file.exists()) {
         				file.delete();
         			}
         			if(copyfile.exists()) {
         				delAllFile(fileurl);
         				copyfile.delete();
         			} 
         		}
       
     	 
     	
         return "";
         	}
    /**
	 * 收件资料批量下载  对外接口 参数为业务编号 2019-03-21 JOE 批量下载修改版，增加意外条件
	 * @author Heks
	 * @date 2017/3/2   16:13:43
	 * @param acinstid
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
    @RequestMapping(value = "/zl/{ywh}", method = RequestMethod.GET)
    public  @ResponseBody String GetZL(@PathVariable("ywh") String ywh,
    		HttpServletRequest request, HttpServletResponse response) throws Exception{
    	 BufferedInputStream bins=null;
    	 BufferedOutputStream bouts=null;
    	List<Map> list=baseCommonDao.getDataListByFullSql("select a.*,b.material_name,c.PROJECT_NAME from BDC_WORKFLOW.WFI_MATERDATA a,BDC_WORKFLOW.WFI_PROMATER b,BDC_WORKFLOW.wfi_proinst c where "+
       "a.MATERILINST_ID = b.MATERILINST_ID and b.PROINST_ID = c.PROINST_ID and (c.file_number ='"+ywh+"' or c.prolsh='"+ywh+"') order by  b.MATERIAL_INDEX,a.file_index");
       String basicPath = ConfigHelper.getNameByValue("material");
       String realpath="",fullpath="",xmmc = "";
       if(list!=null&&list.size()>0){
    	   realpath=String.valueOf(list.get(0).get("PATH")).replace(String.valueOf(list.get(0).get("MATERILINST_ID")), "");
    	   fullpath=basicPath+realpath;
    	   String pro_name = (String)list.get(0).get("PROJECT_NAME");
    	   if(pro_name != null ){
    		   xmmc = pro_name;
    	   }
       }
       if(fullpath=="")
    	   return "无上传资料";
       File dir = new File(fullpath);
       
  
       String fileurl = dir.toString().substring(0, dir.toString().lastIndexOf("\\")) + "\\"+xmmc;
       
       File copyfile =new File(fileurl);    
    	//如果文件夹不存在则创建    
       	if  (!copyfile .exists()  && !copyfile .isDirectory())      
       	{       
       		System.out.println("//不存在");  
       		copyfile .mkdir();    
       	} else   
       	{  
       		System.out.println("//目录存在,那就不用创建了！！"); 
       		
       	}  
       	File cfile = new File(fileurl);
       	String[]  basicPathStr=basicPath.split(":");
       if(dir.exists() && dir.isDirectory()){
    	   String[] cfilelist = cfile.list();
    	   //批量下载分布式判断
//    	   1.判断同一个项目是否有其他盘符的图

    	   String hardDisksConfigs = ConfigHelper.getNameByValue("harddisks");
    	   hardDisksConfigs+=","+basicPathStr[0];//加入原来的盘符JOE
    	   String[]  hardDisks =hardDisksConfigs.split(",");
    	  
    	   for(String  Disk:hardDisks) {
    		   String path =Disk+":"+basicPathStr[1]+realpath;
    		   File filedir=new File(path);
    		   if(filedir.exists()) {
//    	    	   2.判断处理
    			   listFileInDir(filedir,filedir,copyfile);
    		   }
    		   
    		   
    	   }
//    	   if(cfile.exists() && cfilelist.length == 0){
//    		   listFileInDir(dir,dir,copyfile);
//    	   }
    	   String ss = realpath.substring(0, realpath.lastIndexOf("\\"));
    	   String copy_realpath = ss.substring(0, ss.lastIndexOf("\\"));
    	   String copy_fullpath = basicPath+copy_realpath+"\\"+xmmc+"\\";
    	   readfile(copy_fullpath,list);
    	   
    	  ZipCompressorByAnt zs=new ZipCompressorByAnt(cfile.getAbsolutePath()+".zip");
    	  zs.compressExe(cfile.getAbsolutePath());
       }
       
       String path=cfile.getAbsolutePath()+".zip";
       File file=new File(path);
       try { 
         	if (file.exists()) {                    
         		InputStream ins = new FileInputStream(path);                     
         		bins = new BufferedInputStream(ins);// 放到缓冲流里面                  
         		OutputStream outs = response.getOutputStream();// 获取文件输出IO流                   
         		bouts = new BufferedOutputStream(outs);   
         		
         		response.setContentType("application/x-download");// 设置response内容的类型                  
         		response.setHeader( "Content-disposition",                            
         				"attachment;filename="+ URLEncoder.encode(file.getName(), "UTF-8"));// 设置头部信息                   
         		int bytesRead = 0;                    
         		byte[] buffer = new byte[8192];                    
         		// 开始向网络传输文件流                     
         		while ((bytesRead = bins.read(buffer, 0, 8192)) != -1) { 
                       bouts.write(buffer, 0, bytesRead); 
          }                 
                        bouts.flush();                  
                        ins.close();                    
                        bins.close();                   
                        outs.close();                   
                        bouts.close();                
         	} else { 
         		           
         		}             
         	} catch (IOException e) {              
         		System.out.println("下载出错了！");       
         		} finally {
         			//关闭IO流
         			if(bins!=null) {
         				bins.close();
         			}
         			if(bouts!=null) {
         				bouts.close();
         			}
         			//删除临时文件
         			if(file.exists()) {
         				file.delete();
         			}
         			if(copyfile.exists()) {
         				delAllFile(fileurl);
         				copyfile.delete();
         			} 
         		}
       
     	 
     	
         return "";
         	}
   /* 
    * 删除文件夹中所有文件
    * path:文件夹路径
    * 
*/   
    public  boolean delAllFile(String path) {
        boolean flag = false;
        File file = new File(path);
        if (!file.exists()) {
          return flag;
        }
        if (!file.isDirectory()) {
          return flag;
        }
        String[] tempList = file.list();
        File temp = null;
        for (int i = 0; i < tempList.length; i++) {
           if (path.endsWith(File.separator)) {
              temp = new File(path + tempList[i]);
           } else {
               temp = new File(path + File.separator + tempList[i]);
           }
           if (temp.isFile()) {
              temp.delete();
           }
           if (temp.isDirectory()) {
              delAllFile(path + "/" + tempList[i]);//先删除文件夹里面的文件
              delFolder(path + "/" + tempList[i]);//再删除空文件夹
              flag = true;
           }
        }
        return flag;
      }
 
//删除文件夹
//param folderPath 文件夹完整绝对路径
   public  void delFolder(String folderPath) {
   try {
      delAllFile(folderPath); //删除完里面所有内容
      String filePath = folderPath;
      filePath = filePath.toString();
      java.io.File myFilePath = new java.io.File(filePath);
      myFilePath.delete(); //删除空文件夹
   } catch (Exception e) {
     e.printStackTrace(); 
   }
}
    /**
     * 读取某个文件夹下的所有文件
     * @author Heks
     * @date 2017/3/2  16:15:26
     * @param filepath
     * @param list
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     */
     
    public static boolean readfile(String filepath,List<Map> list) throws FileNotFoundException, IOException {
            try {

                    File file = new File(filepath);
                    if (!file.isDirectory()) {
                            System.out.println("文件");
                            System.out.println("path=" + file.getPath());
                            System.out.println("absolutepath=" + file.getAbsolutePath());
                            System.out.println("name=" + file.getName());

                    } else if (file.isDirectory()) {
                    	//这里对文件夹进行重命名的操作
                            System.out.println("文件夹");
                            String[] filelist = file.list();
                            for (int i = 0; i < filelist.length; i++) {
                                    File readfile = new File(filepath + "\\" + filelist[i]);
                                    if (!readfile.isDirectory()) {
                                    	
                                    	for (int k = 0; k < list.size(); k++) {
                                    		String fpath = filelist[i];
                                    		String file_path = (String) list.get(k).get("FILE_PATH");
                                    		if(fpath != null && file_path != null && fpath.equals(file_path)){
                                    			File newfile = new File(filepath+"\\"+ list.get(k).get("FILE_NAME"));
                                    			readfile.renameTo(newfile);
                                    		}
                                    	}
                                            /*System.out.println("path=" + readfile.getPath());
                                            System.out.println("absolutepath="
                                                            + readfile.getAbsolutePath());
                                            System.out.println("name=" + readfile.getName());*/

                                    } else if (readfile.isDirectory()) {
                                    	for (int j = 0; j < list.size(); j++) {
    										String fname = filelist[i];//当前文件夹名
    										String fileid = (String) list.get(j).get("MATERILINST_ID");//获取到的文件夹名
    										if(fname != null && fileid != null && fname.equals(fileid)){
    											File nfile = new File(filepath+ list.get(j).get("MATERIAL_NAME"));
    	                                         readfile.renameTo(nfile);
    										}
    									}
                                            readfile(filepath + "\\" + filelist[i],list);//递归的方式读取所有文件夹
                                    }
                            }
                    }

            } catch (FileNotFoundException e) {
                    System.out.println("readfile()   Exception:" + e.getMessage());
            }
            return true;
    }
	
    
    /**
     * 目标路径创建文件夹   
     * @author Heks
     * @date 2017/3/2 16:16:10
     * @param file
     * @param dirFrom
     * @param dirTo
     */
    public void listFileInDir(File file,File dirFrom,File dirTo) {   
         File[] files = file.listFiles();   
        for (File f : files) {   
             String tempfrom = f.getAbsolutePath();   
             String tempto = tempfrom.replace(dirFrom.getAbsolutePath(),   
                     dirTo.getAbsolutePath()); // 后面的路径 替换前面的路径名   
            if (f.isDirectory()) {   
                 File tempFile = new File(tempto);   
                 tempFile.mkdirs();   
                 listFileInDir(f,dirFrom,dirTo);   
             } else {   
                 System.out.println("源文件:" + f.getAbsolutePath());   
                //   
                int endindex = tempto.lastIndexOf("\\");// 找到"/"所在的位置   
                 String mkdirPath = tempto.substring(0, endindex);   
                 File tempFile = new File(mkdirPath);   
                 tempFile.mkdirs();// 创建立文件夹   
                 System.out.println("目标点:" + tempto);   
                 copy(tempfrom, tempto);   
             }   
         }   
     }   
    
    
    /**
     * 封装好的文件拷贝方法
     * @author Heks
     * @date 2017/3/2 16:16:14
     * @param from
     * @param to
     */
    public void copy(String from, String to) {   
    	try {   
    		InputStream in = new FileInputStream(from);   
    		OutputStream out = new FileOutputStream(to);   

    		byte[] buff = new byte[1024];   
    		int len = 0;   
    		while ((len = in.read(buff)) != -1) {   
    			out.write(buff, 0, len);   
    		}   
    		
    		out.flush();
    		in.close();   
    		out.close();   
    	} catch (FileNotFoundException e) {   
    		e.printStackTrace();   
    	} catch (IOException e) {   
    		e.printStackTrace();   
    	}   
    }   
 /**
	 * 根据不动产的自然幢匹配房产在建工程的幢面(暂时不用)
	 * @author Hks
	 * @date 2016/8/1 18:33
	 * @param zl
	 * @param zcs
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/yklptree/{id}/{zl}/{type}", method = RequestMethod.POST)
	@ResponseBody
	public List matchFwInfoByBdc(@PathVariable("id") String id,
			@PathVariable("zl") String zl,@PathVariable("type") String type,
			HttpServletRequest request, HttpServletResponse response)
			throws UnsupportedEncodingException {
		List list = new ArrayList();
		list = gxService. matchFcMisk(id,zl,type);
		return list;
	}
	
	/**
	 * 获取档案库的自然幢
	 * @author Hks
	 * @date 2016/8/8 10:34
	 * @param zl
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/get_dasjZRZ/{id}/{zl}/{type}", method = RequestMethod.GET)
	@ResponseBody
	public List get_dasjzrz(@PathVariable("id") String id,
			@PathVariable("zl") String zl,@PathVariable("type") String type,
			HttpServletRequest request, HttpServletResponse response)
			throws UnsupportedEncodingException {
		List list = new ArrayList();
		list = gxService.getDasjZRZ(id,zl,type);
		return list;
	}
	
	/**
	 * 根据坐落查询登记系统自然幢
	 * @param zl
	 * @param type
	 * @param request
	 * @param response
	 * @return
	 */
	 
    @RequestMapping(value={"/get_register_zrz/{id}/{type}"}, method={org.springframework.web.bind.annotation.RequestMethod.POST})
    @ResponseBody
    public List GetRegisterZRZ(@PathVariable("id") String id,@PathVariable("type") String type, HttpServletRequest request, HttpServletResponse response)
    {
    	List trees = this.gxService.GetRegisterZRZ(id,type);
    	return trees;
    }
    /**
     * 获取要抽取数据的列表
     * @param id
     * @param type
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value={"/get_import_zrz/{id}/{type}"}, method={org.springframework.web.bind.annotation.RequestMethod.POST})
    @ResponseBody
    public Object GetImportZRZ(@PathVariable("id") String id,@PathVariable("type") String type, HttpServletRequest request, HttpServletResponse response)
    {
    	Object trees = this.gxService.GetImportZRZ(id,type,request);
      return trees;
    }
    @RequestMapping(value={"/save_import_zrz/{id}"}, method={org.springframework.web.bind.annotation.RequestMethod.POST})
    @ResponseBody
    public Message SaveImportZRZ(@PathVariable("id") String id,HttpServletRequest request, HttpServletResponse response)
   		{
    	String treedata = request.getParameter("treedata");
    	Message m = this.gxService.SaveImportZRZ(id,treedata,request);
      return m;
    }
    /**
     * 读取中间列表的文件
     * @author hks
     * @Date 2016/8/26  9:53
     * @param id
     * @param request
     * @return
     */
    @RequestMapping(value={"/read_file/{id}"}, method={org.springframework.web.bind.annotation.RequestMethod.POST})
    @ResponseBody
    public List<Map<String,String>> readFileData(@PathVariable("id") String id,HttpServletRequest request){
		return this.gxService.readFileData(id, request);
    }
    
    @RequestMapping(value={"/import_zrz/{id}"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
    @ResponseBody
    public Message ImportZRZ(@PathVariable("id") String id,HttpServletRequest request, HttpServletResponse response)
    {
    	Message m = this.gxService.insertZRZ(id,request);
      return m;
    }
    @RequestMapping(value={"/update_zrz/{id}/{bs}"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
    @ResponseBody
    public String updateZRZ(@PathVariable("id") String id,@PathVariable("bs") String bs,HttpServletRequest request, HttpServletResponse response)
    {
    	     
    	return this.gxService.updateZRZ(id,bs,request);
    }
    
    @RequestMapping(value={"/update_bdck_zrz/{bdck_zrz}"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
    @ResponseBody
    public String updateZRZ(@PathVariable("bdck_zrz") String bdck_zrz,HttpServletRequest request, HttpServletResponse response)
    {
    	     
    	return this.gxService.update_bdck_ZRZ(bdck_zrz, request);
    }
    
    @RequestMapping(value={"/get_tdstatu/{bdcdyh}"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
    @ResponseBody
    public String getTdStatu(@PathVariable("bdcdyh") String bdcdyh,HttpServletRequest request, HttpServletResponse response)
    {
    	     
    	return this.gxService.getTdStatu(bdcdyh);
    }
    
    /** 
	 * 文件上传(导入模版)
	 * @author heks
	 * @param file
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = "/doUpload/{xmbh}", method = RequestMethod.POST)
	public void loginIndex(@PathVariable("xmbh") String xmbh,
			HttpServletRequest request, HttpServletResponse response,@RequestParam("file") MultipartFile file)  {
		try {
			request.setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String basicPath=request.getContextPath()+"/WEB-INF/upload";

		CommonsMultipartFile cf= (CommonsMultipartFile)file; 
		DiskFileItem fi = (DiskFileItem)cf.getFileItem(); 

		File f = fi.getStoreLocation();
		String sdd= f.getAbsolutePath();
		//建立目录
		File dirFile = new File(basicPath);
		//判断目录是否存在
		if (!dirFile.exists()) {
			//不存在则新建
			dirFile.mkdirs();
		}        
		//获取文件名
		String fileName =file.getOriginalFilename();
		//文件路径
		String filepath =basicPath + "/" + fileName;   
		request.getSession().setAttribute("FILEPATH", filepath);
		File tempFile = new File(filepath);    
		// 文件存在，先删除
		if (tempFile.exists()) {
			tempFile.delete();
		}        
		InputStream inputStream = null;
		InputStream inStream = null;
		try{
			//文件不存在，创建该文件
			tempFile.createNewFile();
			inputStream=file.getInputStream();            
			FileUtils.copyInputStreamToFile(inputStream, tempFile);  
			FileInputStream is = new FileInputStream(tempFile); //文件流  
	        Workbook wb = WorkbookFactory.create(is); //这种方式 Excel 2003/2007/2010 都是可以处理的 
			Sheet sheet = wb.getSheetAt(3);//获取Excle模版的第一个sheet
			int rowCount = sheet.getLastRowNum(); //获取第一个sheet有效行数 
			List<Map> houses = new ArrayList<Map>();
			Map<Object,Object> m = new HashMap<Object, Object>();
			for (int r = 1; r < rowCount+1; r++) {   
				Row rw = sheet.getRow(r);
				m.clear();
				Cell hbsm = rw.getCell(0); //第1列的值 ---户标识码
				m.put("HBSM", hbsm);
				Cell yxtbs = rw.getCell(1);//第2列的值 ---原系统标识
				m.put("YXTBS", yxtbs);
				Cell bdcdyh = rw.getCell(2);//第3列的值 ---不动产单元号
				m.put("BDCDYH", bdcdyh);
				Cell fwbm = rw.getCell(3);//第4列的值   --- 房屋编码
				m.put("FWBM", fwbm);
				Cell ysdm = rw.getCell(4);//第5列的值   --- 要素代码
				m.put("YSDM", ysdm);
				Cell zrz_zh = rw.getCell(5);//第6列的值   --- 自然幢幢号
				m.put("ZH", zrz_zh.getStringCellValue());
				Cell ljzh = rw.getCell(6);//第7列的值   --- 逻辑幢号
				m.put("LJZH", ljzh);
				Cell dyh = rw.getCell(7);//第8列的值   --- 单元号
				m.put("DYH", dyh.getNumericCellValue());
				
				Cell zcs = rw.getCell(8);//第9列的值   --- 总层数
				zcs.setCellType(Cell.CELL_TYPE_NUMERIC);
				m.put("ZCS", zcs.getNumericCellValue());
				
				Cell ch = rw.getCell(9);//第10列的值   --- 层号
				ch.setCellType(Cell.CELL_TYPE_STRING);
				
				m.put("CH", ch.getStringCellValue());
				Cell fh = rw.getCell(10);//第11列的值   --- 房号
				fh.setCellType(Cell.CELL_TYPE_STRING);
				m.put("FH", fh.getStringCellValue());
				
				Cell zl = rw.getCell(11);//第12列的值   --- 坐落
				zl.setCellType(Cell.CELL_TYPE_STRING);
				m.put("ZL", zl.getStringCellValue());
				
				Cell mjdw = rw.getCell(12);//第13列的值   --- 面积单位
				mjdw.setCellType(Cell.CELL_TYPE_STRING);
				m.put("MJDW", mjdw.getStringCellValue());
				
				Cell szc = rw.getCell(13);//第14列的值   --- 所在层
				szc.setCellType(Cell.CELL_TYPE_STRING);
				m.put("SZC", szc.getStringCellValue());
				
				Cell qsc = rw.getCell(14);//第15列的值   --- 起始层
				qsc.setCellType(Cell.CELL_TYPE_NUMERIC);
				m.put("QSC", qsc.getNumericCellValue());
				
				Cell zzc = rw.getCell(15);//第16列的值   --- 终止层
				zzc.setCellType(Cell.CELL_TYPE_NUMERIC);
				m.put("ZZC", zzc.getNumericCellValue());
				
				Cell  hh = rw.getCell(16);//第17列的值   --- 户号
				hh.setCellType(Cell.CELL_TYPE_STRING);
				m.put("HH", hh.getStringCellValue());
				
				Cell shbw = rw.getCell(17);//第18列的值   --- 室号部位
				m.put("SHBW", shbw);
				
				Cell hx = rw.getCell(18);//第19列的值   --- 户型
				hx.setCellType(Cell.CELL_TYPE_STRING);
				m.put("HX", hx.getStringCellValue());
				
				Cell hxjg = rw.getCell(19);//第20列的值   --- 户型结构
				hxjg.setCellType(Cell.CELL_TYPE_STRING);
				m.put("HXJG", hxjg.getStringCellValue());
				
				Cell ghyt = rw.getCell(20);//第21列的值   --- 规划用途
				ghyt.setCellType(Cell.CELL_TYPE_STRING);
				m.put("GHYT", ghyt.getStringCellValue());
				
				Cell fwyt1 = rw.getCell(21);//第22列的值   --- 房屋用途1
				m.put("FWYT1", fwyt1);
				Cell fwyt2 = rw.getCell(22);//第23列的值   --- 房屋用途2
				m.put("FWYT2", fwyt2);
				Cell fwyt3 = rw.getCell(23);//第24列的值   --- 房屋用途3
				m.put("FWYT3", fwyt3);
				Cell ycjzmj = rw.getCell(24);//第25列的值   --- 预测建筑面积
				m.put("YCJZMJ", ycjzmj);
				Cell yctnjzmj = rw.getCell(25);//第26列的值   --- 预测套内建筑面积
				m.put("YCTNJZMJ", yctnjzmj);
				Cell ycftjzmj = rw.getCell(26);//第27列的值   --- 预测分摊建筑面积
				m.put("YCFTJZMJ", ycftjzmj);
				Cell ycdxjzmj = rw.getCell(27);//第28列的值   --- 预测地下部分建筑面积
				m.put("YCDXJZMJ", ycdxjzmj);
				Cell ycqtjzmj = rw.getCell(28);//第29列的值   --- 预测其他建筑面积
				m.put("YCQTJZMJ", ycqtjzmj);
				Cell ycftxs= rw.getCell(29);//第30列的值   --- 预测分摊系数
				m.put("YCFTXS", ycftxs);
				
				Cell scjzmj = rw.getCell(30);//第25列的值   --- 实测建筑面积
				scjzmj.setCellType(Cell.CELL_TYPE_STRING);
				m.put("SCJZMJ", scjzmj.getStringCellValue());
				
				Cell sctnjzmj = rw.getCell(31);//第26列的值   --- 实测套内建筑面积
				sctnjzmj.setCellType(Cell.CELL_TYPE_STRING);
				m.put("SCTNJZMJ", sctnjzmj.getStringCellValue());
				
				Cell scftjzmj = rw.getCell(32);//第27列的值   --- 实测分摊建筑面积
				scftjzmj.setCellType(Cell.CELL_TYPE_STRING);
				m.put("SCFTJZMJ", scftjzmj.getStringCellValue());
				
				Cell scdxjzmj = rw.getCell(33);//第28列的值   --- 实测地下部分建筑面积
				m.put("SCDXJZMJ", scdxjzmj);
				
				Cell scqtjzmj = rw.getCell(34);//第29列的值   --- 实测其他建筑面积
				m.put("SCQTJZMJ", scqtjzmj);
				
				Cell scftxs= rw.getCell(35);//第30列的值   --- 实测分摊系数
				scftxs.setCellType(Cell.CELL_TYPE_STRING);
				m.put("SCFTXS", scftxs.getStringCellValue());
				
				Cell gytdmj= rw.getCell(36);//第30列的值   --- 共有土地面积
				m.put("GYTDMJ", gytdmj);
				Cell fttdmj= rw.getCell(37);//第30列的值   --- 分摊土地面积
				m.put("FTTDMJ", fttdmj);
				Cell dytdmj= rw.getCell(38);//第30列的值   --- 独用土地面积
				m.put("DYTDMJ", dytdmj);
				
				Cell fwlx= rw.getCell(39);//第30列的值   --- 房屋类型
				m.put("FWLX", fwlx.getStringCellValue());
				
				Cell fwjg= rw.getCell(40);//第30列的值   --- 房屋结构
				m.put("FWJG", fwjg.getStringCellValue());
				
				Cell fwxz= rw.getCell(41);//第30列的值   --- 房屋性质
				fwxz.setCellType(Cell.CELL_TYPE_STRING);
				m.put("FWXZ", fwxz.getStringCellValue());
				
				Cell fdcjyje= rw.getCell(42);//第30列的值   --- 房地产交易价格
				m.put("FDCJYJE", fdcjyje);
				
				Cell jgsj= rw.getCell(43);//第30列的值   --- 竣工时间
				jgsj.setCellType(Cell.CELL_TYPE_STRING);
				m.put("JGSJ", jgsj.getStringCellValue());
				
				Cell fwcb= rw.getCell(44);//第30列的值   --- 房屋产别
				m.put("FWCB", fwcb);
				Cell cqly= rw.getCell(45);//第30列的值   ---产权来源
				m.put("CQLY", cqly);
				Cell east= rw.getCell(46);//第30列的值   --- 墙体归属东
				m.put("EAST", east);
				Cell south= rw.getCell(47);//第30列的值   --- 墙体归属南
				m.put("SOUTH", south);
				Cell west= rw.getCell(48);//第30列的值   --- 墙体归属西
				m.put("WEST", west);
				Cell north= rw.getCell(49);//第30列的值   --- 墙体归属北
				m.put("NORTH", north);
				Cell tdshyqr= rw.getCell(50);//第30列的值   --- 土地使用权人
				m.put("TDSHYQR", tdshyqr);
				Cell fht= rw.getCell(51);//第30列的值   --- 房产分户图
				m.put("FHT", fht);
				Cell zt= rw.getCell(52);//第30列的值   --- 状态
				m.put("ZT", zt);
				houses.add(m);
			}

			if(!StringHelper.isEmpty(xmbh)){
				List<BDCS_DJDY_GZ>  djdys = baseCommonDao.getDataList(BDCS_DJDY_GZ.class, "XMBH = '"+xmbh+"' and ly = '02'");
				if(djdys.size() > 0){
					String bdcdyid = djdys.get(0).getBDCDYID();
					List<BDCS_H_XZ> houselist = baseCommonDao.getDataList(BDCS_H_XZ.class, "BDCDYID = '"+bdcdyid+"'");
					if(houselist.size() > 0){
						String zrzbdcdyid = houselist.get(0).getZRZBDCDYID();//分割前后对应的自然幢
						String zdbdcdyid = houselist.get(0).getZDBDCDYID();//分割前后关联的宗地
						String bdcdyh_bgq = houselist.get(0).getBDCDYH();//变更前的单元号
						String zrzdyh = bdcdyh_bgq.substring(0,24);
						String h_dyh=bdcdyh_bgq.substring(bdcdyh_bgq.length()-4,bdcdyh_bgq.length());
						int new_h_dyh = Integer.parseInt(h_dyh);
						if(houses.size() > 0){
							String bdcdyh = "";
							for (Map h : houses) {
								if(new_h_dyh > 0 && new_h_dyh < 10){
									new_h_dyh = new_h_dyh +1;
									bdcdyh = zrzdyh + "000"+new_h_dyh;
								}
								else if(new_h_dyh > 9 && new_h_dyh < 100){
									new_h_dyh = new_h_dyh +1;
									bdcdyh = zrzdyh + "00"+new_h_dyh;
								}
								else if(new_h_dyh > 100 && new_h_dyh <1000){
									new_h_dyh = new_h_dyh +1;
									bdcdyh = zrzdyh + "0"+new_h_dyh;
								}
								else if(new_h_dyh > 1000 && new_h_dyh < 10000){
									new_h_dyh = new_h_dyh +1;
									bdcdyh = zrzdyh + new_h_dyh;
								}
								
								//添加户
								BDCS_H_GZ h_gz = new BDCS_H_GZ();
								h_gz.setBDCDYH(bdcdyh);
								String _id = (String) SuperHelper.GeneratePrimaryKey();
								h_gz.setId(_id);
								h_gz.setZRZBDCDYID(zrzbdcdyid);
								h_gz.setZDBDCDYID(zdbdcdyid);
								Object zrzh = h.get("ZH");
								h_gz.setZRZH((String)zrzh);
								
								Object ljzh = (Object)h.get("LJZH");
								h_gz.setLJZH((String)ljzh);
								
								Object ch = (Object)h.get("CH");
								h_gz.setCH((String)ch);
								
								Object zl = (Object)h.get("ZL");
								h_gz.setZL((String)zl);
								
								Object mjdw = (Object)h.get("MJDW");
								h_gz.setMJDW((String)mjdw);
								
								String szc = (String)h.get("SZC");
								h_gz.setSZC(szc);
								
								
								Double qsc = (Double)h.get("QSC");
								h_gz.setQSC(qsc);
								
								Double zzc = (Double)h.get("ZZC");
								h_gz.setZZC(zzc);
								
								Double zcs = (Double)h.get("ZCS");
								h_gz.setZCS(Integer.parseInt(new java.text.DecimalFormat("0").format(zcs)));
								
								String hh = (String)h.get("HH");
								h_gz.setHH(Integer.parseInt(new java.text.DecimalFormat("0").format(Double.parseDouble(hh)))); 
								
								String hx = ConstHelper.getvalueByConst((String)h.get("HX"));
								h_gz.setHX(hx);
								
								String hxjg = ConstHelper.getvalueByConst((String)h.get("HXJG"));
								h_gz.setHXJG(hxjg);
								
								String ghyt = ConstHelper.getvalueByConst((String)h.get("GHYT"));
								h_gz.setGHYT(ghyt.equals("1")?"10" : ghyt);
								
								String scjzmj = (String)h.get("SCJZMJ");
								h_gz.setSCJZMJ(Double.parseDouble(scjzmj));
								
								String sctnjzmj = (String)h.get("SCTNJZMJ");
								h_gz.setSCTNJZMJ(Double.parseDouble(sctnjzmj));
								
								String scftjzmj = (String)h.get("SCFTJZMJ");
								h_gz.setSCTNJZMJ(Double.parseDouble(scftjzmj));
								
								String scftxs = (String)h.get("SCFTXS");
								h_gz.setSCFTXS(Double.parseDouble(scftxs));
								
								String fwlx = ConstHelper.getvalueByConst((String)h.get("FWLX"));
								h_gz.setFWLX(fwlx.equals("住宅")?"1" : fwlx);
								
								String fwjg = ConstHelper.getvalueByConst((String)h.get("FWJG"));
								h_gz.setFWJG(fwjg);
								
								String fwxz = ConstHelper.getvalueByConst((String)h.get("FWXZ"));
								h_gz.setFWXZ(fwxz);
								
								String jgsj = (String)h.get("JGSJ");
								SimpleDateFormat sdf = new SimpleDateFormat("yyyy");  
							    Date jgdate = sdf.parse(jgsj);
								h_gz.setJGSJ(jgdate);
								
								baseCommonDao.save(h_gz);
								
								/*BDCS_H_LS h_ls = new BDCS_H_LS();
								PropertyUtils.copyProperties(h_ls,h_xz);
								
								baseCommonDao.save(h_ls);*/
								
								BDCS_DJDY_GZ djdy = new BDCS_DJDY_GZ();
								String id = (String) SuperHelper.GeneratePrimaryKey();
								djdy.setId(id);
								String djdyid = (String) SuperHelper.GeneratePrimaryKey();
								djdy.setDJDYID(djdyid);
								djdy.setXMBH(xmbh);
								String _bdcdyh = bdcdyh.substring(0,27);
								djdy.setBDCDYH(_bdcdyh);
								djdy.setBDCDYLX("031");
								djdy.setBDCDYID(h_gz.getId());
								djdy.setLY("01");
								djdy.setGROUPID(1);
								
								baseCommonDao.save(djdy);
								
							}
							baseCommonDao.flush();
						}
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try {
				inputStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		        
		}      
	}
	
	/**
	 * 查封监控，判断当前用户是否在查封监控角色（该角色在登录时弹出查封时限信息）名下,并获取查封时限信息
	 * TODO
	 * @Title: cfControl
	 * @author lgqyk
	 * @date   2018-07-27 11:03
	 * @return Message
	 */
	@RequestMapping(value = "/cfjk", method = RequestMethod.GET)
	public @ResponseBody Message cfControl(HttpServletRequest request, HttpServletResponse response){
		return gxService.cfControl();
	}
}