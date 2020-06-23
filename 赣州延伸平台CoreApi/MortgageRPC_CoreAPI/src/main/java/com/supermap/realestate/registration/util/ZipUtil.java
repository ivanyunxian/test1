package com.supermap.realestate.registration.util;

import java.io.*;  
import java.util.ArrayList;
import java.util.List;
import java.util.zip.*;  

public class ZipUtil {
	static final int BUFFER = 2048;
	 List<String> fileList=new ArrayList<String>();
	 /**解压Zip
	 * @param zipFilePath zip文件路径
	 * @param destDir 解压文件路径
	 */
	public  void unzip(String zipFilePath, String destDir) {
	        File dir = new File(destDir);
	        // create output directory if it doesn't exist
	        if(!dir.exists()) dir.mkdirs();
	        FileInputStream fis;
	        //buffer for read and write data to file
	        byte[] buffer = new byte[BUFFER];
	        try {
	            fis = new FileInputStream(zipFilePath);
	            ZipInputStream zis = new ZipInputStream(fis);
	            ZipEntry ze = zis.getNextEntry();
	            while(ze != null){
	                String fileName = ze.getName();
	                File newFile = new File(destDir + File.separator + fileName);
	                System.out.println("Unzipping to "+newFile.getAbsolutePath());
	                //create directories for sub directories in zip
	                new File(newFile.getParent()).mkdirs();
	                FileOutputStream fos = new FileOutputStream(newFile);
	                int len;
	                while ((len = zis.read(buffer)) > 0) {
	                fos.write(buffer, 0, len);
	                }
	                fos.close();
	                //close this ZipEntry
	                zis.closeEntry();
	                ze = zis.getNextEntry();
	            }
	            //close last ZipEntry
	            zis.closeEntry();
	            zis.close();
	            fis.close();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        
	    }
	   /**
     * 获取压缩文件夹下的文件名Format the file path for zip
     * @param file file path
     * @return Formatted file path
     */
    private  String generateZipEntry(String filepath,String filefolder){
    	return filepath.substring(filefolder.length()+1, filepath.length());
    }
    /**
     * Traverse a directory and get all files,
     * and add the file into fileList  
     * @param node file or directory
     */
    public void generateFileList(File node,String filefolder){
    	//add file only
	if(node.isFile()){
		fileList.add(generateZipEntry(node.getAbsoluteFile().toString(),filefolder));
	}
	if(node.isDirectory()){
		String[] subNote = node.list();
		for(String filename : subNote){
			generateFileList(new File(node, filename),filefolder);
		}
	}
    }
	 /** 压缩文件
	 * @param sourceFolder 要压缩的文件夹
	 * @param outFileZip 压缩后的文件
	 */
	private  void zip(String sourceFolder, String outFileZip) {
		 byte[] buffer = new byte[BUFFER];
	     try{	
	    	FileOutputStream fos = new FileOutputStream(outFileZip);
	    	ZipOutputStream zos = new ZipOutputStream(fos);
	    	System.out.println("Output to Zip : " + outFileZip);
	    	for(String file : this.fileList){	
	    		System.out.println("File Added : " + file);
	    		ZipEntry ze= new ZipEntry(file);
	        	zos.putNextEntry(ze); 
	        	FileInputStream in = new FileInputStream(sourceFolder + File.separator + file);
	        	int len;
	        	while ((len = in.read(buffer)) > 0) {
	        		zos.write(buffer, 0, len);
	        	}
	        	in.close();
	    	}
	    	zos.closeEntry();
	    	zos.close();
	    	System.out.println("Done");
	    }catch(IOException ex){
	       ex.printStackTrace();   
	    }
	        
	        
	    }
	/** 
     * 执行压缩操作 (采用org.apache.tools.zip.ZipOutputStream。 支持中文)
     * @param srcPathName 被压缩的文件/文件夹 
     * @param zipFile 压缩后的文件/文件夹 
     */  
    public void compressExe(String srcPathName,String zipFile) {    
        File file = new File(srcPathName);    
        if (!file.exists()){  
            throw new RuntimeException(srcPathName + "不存在！");    
        }  
        try {    
            FileOutputStream fileOutputStream = new FileOutputStream(zipFile);    
            CheckedOutputStream cos = new CheckedOutputStream(fileOutputStream,new CRC32());    
            org.apache.tools.zip.ZipOutputStream out = new org.apache.tools.zip.ZipOutputStream(cos);    
            String basedir = "";    
            out.setEncoding("gbk");
            compressByType(file, out, basedir);    
            out.close();    
        } catch (Exception e) {   
            e.printStackTrace();  
        	System.out.println("执行压缩操作时发生异常:"+e);  
            throw new RuntimeException(e);    
        }    
    }  
    /** 
     * 判断是目录还是文件，根据类型（文件/文件夹）执行不同的压缩方法 
     * @param file  
     * @param out 
     * @param basedir 
     */  
    private void compressByType(File file, org.apache.tools.zip.ZipOutputStream out, String basedir) {    
        /* 判断是目录还是文件 */    
        if (file.isDirectory()) {    
        	System.out.println("压缩：" + basedir + file.getName());    
            this.compressDirectory(file, out, basedir);    
        } else {    
        	System.out.println("压缩：" + basedir + file.getName());    
            this.compressFile(file, out, basedir);    
        }    
    }
    /** 
     * 压缩一个目录 
     * @param dir 
     * @param out 
     * @param basedir 
     */  
    private void compressDirectory(File dir, org.apache.tools.zip.ZipOutputStream out, String basedir) {    
        if (!dir.exists()){  
             return;    
        }  
             
        File[] files = dir.listFiles();    
        for (int i = 0; i < files.length; i++) {    
            /* 递归 */    
            compressByType(files[i], out, basedir + dir.getName() + "/");    
        }    
    }    
    
    /** 
     * 压缩一个文件 
     * @param file 
     * @param out 
     * @param basedir 
     */  
    private void compressFile(File file, org.apache.tools.zip.ZipOutputStream out, String basedir) {    
        if (!file.exists()) {    
            return;    
        }    
        try {    
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));    
            org.apache.tools.zip.ZipEntry entry = new org.apache.tools.zip.ZipEntry(basedir + file.getName());    
            out.putNextEntry(entry);    
            int count;    
            byte data[] = new byte[BUFFER];    
            while ((count = bis.read(data, 0, BUFFER)) != -1) {    
                out.write(data, 0, count);    
            }    
            bis.close();    
        } catch (Exception e) {    
            throw new RuntimeException(e);    
        }    
    }   
}