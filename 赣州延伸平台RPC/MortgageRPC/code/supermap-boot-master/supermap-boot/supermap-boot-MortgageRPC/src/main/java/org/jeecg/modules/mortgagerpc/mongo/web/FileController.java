package org.jeecg.modules.mortgagerpc.mongo.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.exception.SupermapBootException;
import org.jeecg.common.system.util.StringHelper;
import org.jeecg.common.util.PdfBase64Util;
import org.jeecg.common.util.QRCodeHelper;
import org.jeecg.modules.mortgagerpc.entity.Bdc_zs;
import org.jeecg.modules.mortgagerpc.entity.Filedata;
import org.jeecg.modules.mortgagerpc.entity.Wfd_materclass;
import org.jeecg.modules.mortgagerpc.entity.Wfi_materclass;
import org.jeecg.modules.mortgagerpc.entity.Wfi_materdata;
import org.jeecg.modules.mortgagerpc.mongo.serviece.MongoServerce;
import org.jeecg.modules.mortgagerpc.mongo.util.Base64ToFile;
import org.jeecg.modules.mortgagerpc.service.IBdc_zsService;
import org.jeecg.modules.mortgagerpc.service.IFiledataService;
import org.jeecg.modules.mortgagerpc.service.IWfd_materclassService;
import org.jeecg.modules.mortgagerpc.service.IWfi_materclassService;
import org.jeecg.modules.mortgagerpc.service.IWfi_materdataService;
import org.jeecg.modules.mortgagerpc.service.impl.Bdc_zsServiceImpl;
import org.jeecg.modules.system.entity.YsptEnterprise;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
* @Description: 单元信息表
* @Author: jeecg-boot
* @Date:   2019-07-30
* @Version: V1.0
*/
@Slf4j
@RestController
@RequestMapping("/mongofile")
public class FileController {
    @Autowired
    private MongoServerce mongoserverce;
    @Autowired
    private IWfi_materdataService wfi_materdataService;
    @Autowired
    private IWfi_materclassService wfi_materclassService;
    @Autowired
    private IBdc_zsService zsservice;
    @Autowired
    private IFiledataService filedataService;
    
    @PostMapping(value = "/upload")
    public  Result<?>    uploadFile(HttpServletRequest request, HttpServletResponse response, @RequestParam("file") MultipartFile file, Wfi_materclass wfi_materclass) {
        //保存文件
        Result<?> result = new Result<>();
        try {
            if (file!=null&&wfi_materclass!=null&&wfi_materclass.getId()!=null){
                String mongoid=mongoserverce.save(file);
                Wfi_materdata wfi_materdata=new Wfi_materdata();
                String filepath = file.getOriginalFilename();
            	String suffix = filepath.substring(filepath.indexOf("."));
                if(!StringHelper.isEmpty(mongoid)){
                    wfi_materdata.setCreated(new Date());
                    wfi_materdata.setMongoid(mongoid);
                    wfi_materdata.setFileindex(StringHelper.getLong(wfi_materclass.getMaterdataindex()));
                    wfi_materdata.setName(file.getOriginalFilename());
                    wfi_materdata.setStatus(1);
                    wfi_materdata.setMaterinstId(wfi_materclass.getId());
                    wfi_materdata.setProlsh(wfi_materclass.getProlsh());
                    wfi_materdata.setSuffix(suffix);
                    wfi_materdataService.saveOrUpdate(wfi_materdata);
                    result.setSuccess(true);
                    result.setMessage("上传成功");
                    return result;
                }

            }

        }catch (Exception e){
            e.printStackTrace();
        }

        result.setSuccess(false);
        result.setMessage("上传失败");
        return result;
    }
    
    @PostMapping(value = "/templateupload")
    public  Result<?>  templateUploadFile(HttpServletRequest request, HttpServletResponse response, @RequestParam("file") MultipartFile file) {
        //保存文件
        Result<?> result = new Result<>();
        try {
            if (file!=null){
                String mongoid=mongoserverce.save(file);
                Filedata filedata=new Filedata();
                String filepath = file.getOriginalFilename();
            	String suffix = filepath.substring(filepath.indexOf("."));
            	String status = request.getParameter("status");
            	if(!StringHelper.isEmpty(status)) {//200为操作文档 100为驱动下载
            		if(status.equals("200")) {
            			 filedata.setCreated(new Date());
                         filedata.setMongoid(mongoid);
                         filedata.setFileindex(System.currentTimeMillis());
                         filedata.setName(file.getOriginalFilename());
                         filedata.setStatus(Integer.valueOf(request.getParameter("status")));
                         filedata.setSuffix(suffix);
                         filedataService.saveOrUpdate(filedata);
            		}else {
            			 filedata = filedataService.getOne(new QueryWrapper<Filedata>().eq("status", status));
            			 if(StringHelper.isEmpty(filedata)) {
            				 filedata = new Filedata(); 
            				 filedata.setCreated(new Date());
            			 }
            			 filedata.setMongoid(mongoid);
                         filedata.setFileindex(System.currentTimeMillis());
                         filedata.setName(file.getOriginalFilename());
                         filedata.setStatus(Integer.valueOf(request.getParameter("status")));
                         filedata.setSuffix(suffix);
                         filedataService.saveOrUpdate(filedata);
            		}
            		 result.setSuccess(true);
                     result.setMessage("上传成功");
                     return result;
            	}
//                if(!StringHelper.isEmpty(mongoid)){
//                    filedata.setCreated(new Date());
//                    filedata.setMongoid(mongoid);
//                    filedata.setFileindex(System.currentTimeMillis());
//                    filedata.setName(file.getOriginalFilename());
//                    filedata.setStatus(Integer.valueOf(request.getParameter("status")));
//                    filedata.setSuffix(suffix);
//                    filedataService.saveOrUpdate(filedata);
//                    result.setSuccess(true);
//                    result.setMessage("上传成功");
//                    return result;
//                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        result.setSuccess(false);
        result.setMessage("上传失败");
        return result;
    }
    
    @GetMapping(value = "/download")
    public  void  downloadFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
        /*response.addHeader("Access-Control-Allow-Origin", "*");
    	OutputStream outputStream = null;
        String mongoid=request.getParameter("mongoid");
        File file = null;
        Filedata filedata = filedataService.getOne(new QueryWrapper<Filedata>().eq("mongoid", mongoid));
    	try {
            outputStream = new BufferedOutputStream(response.getOutputStream());
            file = mongoserverce.getFileByID(mongoid);
            InputStream inStream = new FileInputStream(file);// 文件的存放路径
            response.setContentType("application/octet-stream");
            response.addHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(filedata.getName(), "UTF-8") + "");
            response.addHeader("Content-Length", "" + file.length());
            // 循环取出流中的数据
            byte[] b = new byte[1024];
            try {
            	int i = inStream.read(b);
                while (i != -1) {
                	response.getOutputStream().write(b, 0, i);
                    i = inStream.read(b);
                }
                inStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            log.error("下载"+filedata.getSuffix()+"失败" + e.getMessage());
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                }
            }
        }*/
    	response.addHeader("Access-Control-Allow-Origin", "*");
    	OutputStream outputStream = null;
        String mongoid=request.getParameter("mongoid");
        String status = request.getParameter("status");
        Filedata filedata = null;
    	if(!StringHelper.isEmpty(status)) {//200为操作文档 100为驱动下载  有改造的空间
    		if(status.equals("200")) {
    			filedata = filedataService.getOne(new QueryWrapper<Filedata>().eq("mongoid", mongoid));
    		}else if(status.equals("100")) {
    			filedata = filedataService.getOne(new QueryWrapper<Filedata>().eq("status", status));
    			mongoid = filedata.getMongoid();
    		}
    		
    	}
        response.setContentType("application/octet-stream");
    	try {
            outputStream = response.getOutputStream();
            byte [] f=mongoserverce.getFileByteByID(mongoid);
            outputStream.write(f);

        } catch (IOException e) {
        	//e.printStackTrace();
            log.error("下载"+filedata.getSuffix()+"失败" + e.getMessage());
            // e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                }
            }
        }

    }
    

    @GetMapping(value = "/view")
    public  void    uploadFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.addHeader("Access-Control-Allow-Origin", "*");
    	OutputStream outputStream = null;
        String mongoid=request.getParameter("mongoid");
        Wfi_materdata wfi_materdata = wfi_materdataService.getOne(new QueryWrapper<Wfi_materdata>().eq("mongoid", mongoid));//.list(new QueryWrapper<Wfi_materdata>().eq("mongoid", mongoid).orderByAsc("fileindex"));
    	if(wfi_materdata.getSuffix() !=null && !wfi_materdata.getSuffix().equals("")) {
    		if(wfi_materdata.getSuffix().equals(".pdf")) {
                response.setContentType("application/pdf;charset=utf-8");
        	}else if(wfi_materdata.getSuffix().equals(".jpg") || wfi_materdata.getSuffix().equals(".png")){
                response.setContentType("image/jpeg;charset=utf-8");
        	}
    	}else {//默认图片格式
    		response.setContentType("image/jpeg;charset=utf-8");
    	}
    	try {
            outputStream = response.getOutputStream();
            byte [] f=mongoserverce.getFileByteByID(mongoid);
            outputStream.write(f);

        } catch (IOException e) {
        	//e.printStackTrace();
            log.error("预览"+wfi_materdata.getSuffix()+"失败" + e.getMessage());
            // e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                }
            }
        }

    }
    
    
    @GetMapping(value = "/viewcert")
    public  void    viewCERT(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.addHeader("Access-Control-Allow-Origin", "*");
    	OutputStream outputStream = null;
        String mongoid=request.getParameter("mongoid");
        Bdc_zs zs = zsservice.getOne(new QueryWrapper<Bdc_zs>().eq("mongoid", mongoid));//.list(new QueryWrapper<Wfi_materdata>().eq("mongoid", mongoid).orderByAsc("fileindex"));
    	if(zs.getFilename() !=null && !zs.getFilename().equals("")) {
    		if(zs.getFilename().equals(".pdf")) {
                response.setContentType("application/pdf;charset=utf-8");
        	}else if(zs.getFilename().equals(".jpg") || zs.getFilename().equals(".png")){
                response.setContentType("image/jpeg;charset=utf-8");
        	}
    	}else {//默认图片格式
    		response.setContentType("image/jpeg;charset=utf-8");
    	}
    	try {
            outputStream = response.getOutputStream();
            byte [] f=mongoserverce.getFileByteByID(mongoid);
            outputStream.write(f);

        } catch (IOException e) {
        	//e.printStackTrace();
            log.error("预览"+zs.getFilename()+"失败" + e.getMessage());
            // e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                }
            }
        }

    }


    @GetMapping(value = "/removeFile")
    @Transactional(rollbackFor = Exception.class)
    public Result<String> removeFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Result<String> result = new Result<String>();
        String id=request.getParameter("id");
        try {
            Wfi_materdata file = wfi_materdataService.getById(id);
            if (file == null) {
                throw new SupermapBootException("找不到该附件");
            }
            Wfi_materclass materclass = wfi_materclassService.getOne(new QueryWrapper<Wfi_materclass>().eq("id", file.getMaterinstId()));
            if (materclass == null) {
                throw new SupermapBootException("找不到该附件的目录");
            }
            wfi_materdataService.removeById(id);
            mongoserverce.delete(file.getMongoid());
            result.setSuccess(true);
            result.setMessage("删除成功");
            return result;
        } catch(SupermapBootException e) {
            log.error(e.getMessage(), e);
            result.setSuccess(false);
            result.setMessage("删除失败:"+e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            result.setSuccess(false);
            result.setMessage("删除失败");
        }
        return result;
    }

    @GetMapping(value = "/removeAll")
    @Transactional(rollbackFor = Exception.class)
    public Result<String> removeAll(HttpServletRequest request) {
        Result<String> result = new Result<String>();
        String id=request.getParameter("id");
        try {
            Wfi_materclass materclass = wfi_materclassService.getOne(new QueryWrapper<Wfi_materclass>().eq("id", id));
            if (materclass == null) {
                throw new SupermapBootException("找不到该附件的目录");
            }
            QueryWrapper<Wfi_materdata> materdatawrapper = new QueryWrapper<Wfi_materdata>().eq("materinst_id", id);
            List<Wfi_materdata> materdatas = wfi_materdataService.list(materdatawrapper);
            for(Wfi_materdata file : materdatas) {
                mongoserverce.delete(file.getMongoid());
            }

            wfi_materdataService.remove(materdatawrapper);
            result.setSuccess(true);
            result.setMessage("删除成功");
            return result;
        } catch(SupermapBootException e) {
            log.error(e.getMessage(), e);
            result.setSuccess(false);
            result.setMessage("删除失败:"+e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            result.setSuccess(false);
            result.setMessage("删除失败");
        }
        return result;
    }


    @GetMapping(value = "/getMaterlistFiles")
    public  Result<List<Wfi_materdata>> getMaterlistFiles(HttpServletRequest request, HttpServletResponse response) {
        Result<List<Wfi_materdata>> result = new Result<List<Wfi_materdata>>();
        try {
            String materclassid=request.getParameter("materclassid");
            List<Wfi_materdata> wfi_materdata = wfi_materdataService.list(new QueryWrapper<Wfi_materdata>().eq("MATERINST_ID", materclassid).orderByAsc("fileindex"));
            result.setSuccess(true);
            result.setResult(wfi_materdata);
            result.setMessage("获取成功");

            return result;
        } catch(SupermapBootException e) {
            log.error(e.getMessage(), e);
            result.setSuccess(false);
            result.setMessage("获取附件列表失败:"+e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            result.setSuccess(false);
            result.setMessage("获取附件列表失败");
        }
        return result;

    }
    
    @GetMapping(value = "/getZSFiles")
    public  Result<List<Bdc_zs>> getZSFile(HttpServletRequest request, HttpServletResponse response) {
        Result<List<Bdc_zs>> result = new Result<List<Bdc_zs>>();
        try {
        	String zsid = request.getParameter("zsid");
        	List<Bdc_zs> zslist = zsservice.list(new QueryWrapper<Bdc_zs>().eq("zsid", zsid));
            Bdc_zs zs  = zsservice.getById(zsid);
            String base64 = zs.getPdf();
            if(base64!=null&&!"".equals(base64)){
            	String filePath = "D:\\tmp\\"+zsid+".pdf";
                PdfBase64Util.base64StringToPdf(base64, filePath);
                File file = new File(filePath);
                String mongoid=mongoserverce.save(file);
                if(!StringHelper.isEmpty(mongoid)){
                	zs.setMongoid(mongoid);
                	zs.setFilename(file.getName());
                	zs.setPdf("");
                	zsservice.updateById(zs);
                }
                file.delete();
                List<Bdc_zs> zsupdate  = zsservice.list(new QueryWrapper<Bdc_zs>().eq("zsid", zsid));
                result.setSuccess(true);
                result.setResult(zsupdate);
                result.setMessage("获取成功");
            }else{
            	 result.setSuccess(true);
                 result.setResult(zslist);
                 result.setMessage("获取成功");
            }
            return result;
        } catch(SupermapBootException e) {
            log.error(e.getMessage(), e);
            result.setSuccess(false);
            result.setMessage("获取附件列表失败:"+e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            result.setSuccess(false);
            result.setMessage("获取附件列表失败");
        }
        return result;

    }

    @GetMapping(value = "/getMaterlist")
    public  Result<List<Wfi_materclass>> getMaterlist(HttpServletRequest request, HttpServletResponse response) {
        Result<List<Wfi_materclass>> result = new Result<List<Wfi_materclass>>();
        try {
            String prolsh=request.getParameter("prolsh");
            List<Wfi_materclass> wfi_materclass = wfi_materclassService.getMaterlist( prolsh );
            result.setSuccess(true);
            result.setResult(wfi_materclass);
            result.setMessage("获取成功");

            return result;
        } catch(SupermapBootException e) {
            log.error(e.getMessage(), e);
            result.setSuccess(false);
            result.setMessage("获取附件列表失败:"+e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            result.setSuccess(false);
            result.setMessage("获取附件列表失败");
        }
        return result;

    }
    
    @RequestMapping(value = "/getMaterlistEnterprise", method = RequestMethod.GET)
    public  Result<List<Wfi_materclass>> getMaterlistEnterprise(HttpServletRequest request, HttpServletResponse response) {
        Result<List<Wfi_materclass>> result = new Result<List<Wfi_materclass>>();
        try {
            String prolsh=request.getParameter("prolsh");
            List<Wfi_materclass> wfi_materclass = wfi_materclassService.getMaterlist( prolsh );
            result.setSuccess(true);
            result.setResult(wfi_materclass);
            result.setMessage("获取成功");

            return result;
        } catch(SupermapBootException e) {
            log.error(e.getMessage(), e);
            result.setSuccess(false);
            result.setMessage("获取附件列表失败:"+e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            result.setSuccess(false);
            result.setMessage("获取附件列表失败");
        }
        return result;

    }
    
    @RequestMapping(value = "/genarateMater", method = RequestMethod.GET)
	public Result<Boolean> genarateMater(HttpServletRequest request, HttpServletResponse response) {
    	Result<Boolean> result = new Result<Boolean>();
		String prolsh=request.getParameter("prolsh");
		List<Wfd_materclass> materclasslist = new ArrayList<Wfd_materclass>();
		Wfd_materclass temp = new Wfd_materclass();
		temp.setFileindex(0);
		temp.setName("统一社会信用代码证书");
		temp.setRequired("1");
		materclasslist.add(temp);
		
		temp = new Wfd_materclass();
		temp.setFileindex(1);
		temp.setName("法人身份证（正面）");
		temp.setRequired("1");
		materclasslist.add(temp);
		
		temp = new Wfd_materclass();
		temp.setFileindex(2);
		temp.setName("法人身份证（反面）");
		temp.setRequired("1");
		materclasslist.add(temp);
		
		temp = new Wfd_materclass();
		temp.setFileindex(3);
		temp.setName("注册者身份证（正面）");
		temp.setRequired("1");
		materclasslist.add(temp);
		
		temp = new Wfd_materclass();
		temp.setFileindex(4);
		temp.setName("注册者身份证（反面）");
		temp.setRequired("1");
		materclasslist.add(temp);
		
		temp = new Wfd_materclass();
		temp.setFileindex(5);
		temp.setName("委托办理书");
		temp.setRequired("0");
		materclasslist.add(temp);
		
		List<Wfi_materclass> materclasslistttest = wfi_materclassService
				.list(new QueryWrapper<Wfi_materclass>().eq("prolsh", prolsh));
		if(materclasslistttest!=null && materclasslistttest.size()>0) {
			
		}else {
			for(Wfd_materclass materclass:materclasslist) {
	            Wfi_materclass wfi_materclass = new Wfi_materclass();
//	            wfi_materclass.setDivisionCode();
	            wfi_materclass.setFileindex(materclass.getFileindex());
//	            wfi_materclass.setMatedesc(StringHelper.formatObject(materclass.getMatedesc()));
	            wfi_materclass.setProlsh(prolsh);
	            wfi_materclass.setRequired(materclass.getRequired());
	            wfi_materclass.setName(materclass.getName());
//	            wfi_materclass.setProcodeid(materclass.getProcodeid());
//	            wfi_materclass.setEcert(materclass.getEcert());
//	            wfi_materclass.setEcertCode(materclass.getEcertCode());
	            wfi_materclassService.save(wfi_materclass);
	        }
		}
		result.setResult(true);
		return result;
	}

    @GetMapping(value = "/getqrcode")
    public void createqrcode(HttpServletRequest request, HttpServletResponse response) {
        try {
            String content = request.getParameter("content");
            BufferedImage img = QRCodeHelper.CreateQRCode(content, "png", Integer.parseInt("120"), Integer.parseInt("120"));
            response.setContentType("image/png; charset=GBK");
            response.setHeader("Pragma", "no-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setIntHeader("Expires", -1);
            ImageIO.write(img, "png", response.getOutputStream());
        } catch (IOException e) {
            log.error("获取二维码失败" + e.getMessage());
             e.printStackTrace();
        }
    }

    /**
     * 维山高拍仪上传图片方法
     * liangqin
     * @param request
     * @param file
     * @return
     */
    @PostMapping(value = "/uploadFileWiiSan")
    public String uploadFileWiiSan(HttpServletRequest request,@RequestParam("Filedata") MultipartFile file) {
       // String filename = file.getOriginalFilename();
        JSONObject jsondata = JSON.parseObject(request.getParameter("params"));
        String id = jsondata.getString("id");
        String materdataindex = jsondata.getString("materdataindex");
        String prolsh = jsondata.getString("prolsh");
        try {
            if (file!=null&&id!=null){
            	String filepath = file.getOriginalFilename();
            	String suffix = filepath.substring(filepath.indexOf("."));
                String mongoid=mongoserverce.save(file);
                Wfi_materdata wfi_materdata=new Wfi_materdata();
                if(!StringHelper.isEmpty(mongoid)){
                    wfi_materdata.setCreated(new Date());
                    wfi_materdata.setMongoid(mongoid);
                    wfi_materdata.setFileindex(StringHelper.getLong(materdataindex));
                    wfi_materdata.setName(file.getOriginalFilename());
                    wfi_materdata.setStatus(1);
                    wfi_materdata.setMaterinstId(id);
                    wfi_materdata.setProlsh(prolsh);
                    wfi_materdata.setSuffix(suffix);
                    wfi_materdataService.saveOrUpdate(wfi_materdata);
                    return "上传成功";
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return "上传失败";
    }
    
    
    /**
     * 维山高拍仪上传图片方法
     * liangqin
     * @param request
     * @param file
     * @return
     */
    @PostMapping(value = "/uploadFileLT")
    public String uploadFileLT(@RequestBody JSONObject jsondata) {
       // String filename = file.getOriginalFilename();
        //JSONObject jsondata = JSON.parseObject(request.getParameter("params"));
        String id = jsondata.getString("id");
        String materdataindex = jsondata.getString("materdataindex");
        String prolsh = jsondata.getString("prolsh");
        String imgarraystr = jsondata.getString("imgarray");
        String base64arrstr = jsondata.getString("base64str");
        imgarraystr = imgarraystr.replace("[", "").replace("]", "");
        base64arrstr = base64arrstr.replace("[", "").replace("]", "");
        String[] imgarr = imgarraystr.split(",");
        String[] base64arr = base64arrstr.split(",");
        //File fileAll = new File(savepath);
//        if(!fileAll.exists()){
//        	fileAll = new File("C:\\eloamFile");
//        }
       // File[] files = fileAll.listFiles();
        for(int i = 0;i<imgarr.length;i++){
            try {
            	String filepath = imgarr[i].trim();
            	String base64str = base64arr[i];
            	boolean flag = Base64ToFile.base64ToFile(base64str, filepath);
            	if(flag){
            		File file = new File(imgarr[i].trim());
                    if (file!=null&&id!=null){
                    	String suffix = filepath.substring(filepath.indexOf("."));
                        String mongoid=mongoserverce.save(file);
                        Wfi_materdata wfi_materdata=new Wfi_materdata();
                        if(!StringHelper.isEmpty(mongoid)){
                            wfi_materdata.setCreated(new Date());
                            wfi_materdata.setMongoid(mongoid);
                            wfi_materdata.setFileindex(StringHelper.getLong(materdataindex));
                            wfi_materdata.setName(file.getName());
                            wfi_materdata.setStatus(1);
                            wfi_materdata.setMaterinstId(id);
                            wfi_materdata.setProlsh(prolsh);
                            wfi_materdata.setSuffix(suffix);
                            wfi_materdataService.saveOrUpdate(wfi_materdata);
                            File delFile = new File(file.getAbsolutePath());
                            delFile.delete();
                        }
                    }
            	}
            }catch (Exception e){
                e.printStackTrace();
                return "上传失败";
            }
        }
        return "上传成功";
    }


}
