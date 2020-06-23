package com.supermap.yingtanothers.file;

import java.io.BufferedInputStream;   
import java.io.File;  
import java.io.FileInputStream;    
import java.io.FileOutputStream;  
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;  
import java.util.zip.ZipOutputStream;  

import org.apache.log4j.Logger;
/**
 * 
 * 作者： 苗利涛
 * 时间： 2016年2月1日 下午6:52:16
 * 功能： JAVA实现把指定文件夹下的所有文件压缩成zip包
 */
public class ZipCompressor {  
    private Logger logger = Logger.getLogger(ZipCompressor.class);  
    static final int BUFFER = 8192;    
    private File zipFile;    
      
    /** 
     * 压缩文件构造函数 
     * @param pathName 压缩的文件存放目录 
     */  
    public ZipCompressor(String pathName) {    
        zipFile = new File(pathName);    
    }    
    
    /** 
     * 执行压缩操作 
     * @param srcPathName 被压缩的文件/文件夹 
     */  
    public void compressExe(String srcPathName) {    
        File file = new File(srcPathName);    
        if (!file.exists()){  
            throw new RuntimeException(srcPathName + "不存在！");    
        }  
        try {    
            FileOutputStream fileOutputStream = new FileOutputStream(zipFile);    
            CheckedOutputStream cos = new CheckedOutputStream(fileOutputStream,new CRC32());    
            ZipOutputStream out = new ZipOutputStream(cos);    
            String basedir = "";    
            compressByType(file, out, basedir);    
            out.close();    
        } catch (Exception e) {   
            e.printStackTrace();  
            logger.error("执行压缩操作时发生异常:"+e);  
            throw new RuntimeException(e);    
        }    
    }    
    
    /** 
     * 判断是目录还是文件，根据类型（文件/文件夹）执行不同的压缩方法 
     * @param file  
     * @param out 
     * @param basedir 
     */  
    private void compressByType(File file, ZipOutputStream out, String basedir) {    
        /* 判断是目录还是文件 */    
        if (file.isDirectory()) {    
            logger.info("压缩：" + basedir + file.getName());    
            this.compressDirectory(file, out, basedir);    
        } else {    
            logger.info("压缩：" + basedir + file.getName());    
            this.compressFile(file, out, basedir);    
        }    
    }    
    
    /** 
     * 压缩一个目录 
     * @param dir 
     * @param out 
     * @param basedir 
     */  
    private void compressDirectory(File dir, ZipOutputStream out, String basedir) {    
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
    private void compressFile(File file, ZipOutputStream out, String basedir) {    
        if (!file.exists()) {    
            return;    
        }    
        try {    
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));    
            ZipEntry entry = new ZipEntry(basedir + file.getName());    
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
    /** 
     * 将存放在sourceFilePath目录下的源文件，打包成fileName名称的zip文件，并存放到zipFilePath路径下 
     * @param sourceFilePath :待压缩的文件路径 
     * @param zipFilePath :压缩后存放路径 
     * @param fileName :压缩后文件的名称 
     * @return 
     */  
    public static boolean  fileToZip(String sourceFilePath,String zipFilePath,String fileName) {
    	try {
    		
    		ZipCompressor zc = new  ZipCompressor(zipFilePath+"/"+fileName+".zip");  
            zc.compressExe(sourceFilePath);
            return true;
		} catch (Exception e) {
			return false; 	
		}       		
    }  
}  
