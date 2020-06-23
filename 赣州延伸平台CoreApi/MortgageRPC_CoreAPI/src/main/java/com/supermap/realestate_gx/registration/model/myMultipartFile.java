package com.supermap.realestate_gx.registration.model;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.springframework.web.multipart.MultipartFile;

public class myMultipartFile   implements MultipartFile {

	 private final byte[] imgContent;
	 private final String originalFilename;
	 private final FileItem fileItem;
	 private final long size;
     public myMultipartFile(byte[] imgContent,String originalFilename,FileItem fileItem)
     {
        this.imgContent = imgContent;
        this.originalFilename = originalFilename;
        this.fileItem = fileItem;
		this.size = 0;
         }
     public final FileItem getFileItem() {
 		return this.fileItem;
 	}
     protected boolean isAvailable() {
 		 return false;
 	}
 	public String getStorageDescription() {
		return "";
	}
	@Override
	public String getName() {
		 if (( this.originalFilename != null) && ( this.originalFilename.length() > 0)) 
	      { 
	            int i =  this.originalFilename.lastIndexOf('.');  
	            if ((i >-1) && (i < ( this.originalFilename.length()))) 
	             {
	                  return  this.originalFilename.substring(0, i); 
	              } 
	       }  
	        return  this.originalFilename; 
	}

	@Override
	public String getOriginalFilename() {
		// TODO Auto-generated method stub
		return this.originalFilename;
	}
	private  String getExtension(String filename, String defExt)
	 {        
	  if ((filename != null) && (filename.length() > 0))
	   {             
	      int i = filename.lastIndexOf('.');   
	      if ((i >-1) && (i < (filename.length() - 1))) 
	        {   
	           return filename.substring(i + 1); 
	         } 
	      }  
	        return defExt; 
	    }
	@Override
	public String getContentType() {
		// TODO Auto-generated method stub
		String def=getExtension(this.originalFilename,"jpg");
		if(def.equals("tif"))
		 return "image/tiff";
		if(def.equals("fax"))
			 return "image/fax";
		if(def.equals("gif"))
			 return "image/gif";
		if(def.equals("ico"))
			 return "image/x-icon";
		if(def.equals("jfif"))
			 return "image/jpeg";
		if(def.equals("jpe"))
			 return "image/jpeg";
		if(def.equals("jpeg"))
			 return "image/jpeg";
		if(def.equals("jpg"))
			 return "image/jpeg";
		if(def.equals("net"))
			 return "image/pnetvue";
		if(def.equals("png"))
			 return "image/png";
		if(def.equals("rp"))
			 return "image/vnd.rn-realpix";
		if(def.equals("tiff"))
			 return "image/tiff";
		if(def.equals("wbmp"))
			 return "image/vnd.wap.wbmp";
		return def;
				
	}

	@Override
	public boolean isEmpty() {
	    return imgContent == null || imgContent.length == 0;
	}

	@Override
	public long getSize() {
		  return imgContent.length;
	}

	@Override
	public byte[] getBytes() throws IOException {
		   return imgContent;
	}

	@Override
	public InputStream getInputStream() throws IOException {
		 return new ByteArrayInputStream(imgContent);
	}

	@Override
	public void transferTo(File dest) throws IOException, IllegalStateException {
		new FileOutputStream(dest).write(imgContent);
		
	}

}
