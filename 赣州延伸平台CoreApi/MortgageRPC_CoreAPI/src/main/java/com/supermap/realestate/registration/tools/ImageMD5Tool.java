package com.supermap.realestate.registration.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.RandomAccessFile;
import java.math.BigInteger;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.util.Arrays;

import org.apache.commons.io.FileUtils;

/**
 * 
 * @author lx
 * 对图片进行md5加密 防伪
 *
 */
public class ImageMD5Tool {
	
	/**
	 *  
	 * @param file
	 * @return
	 * @throws Exception
	 */
	private static byte[] img2Md5Bytes(File file) throws Exception{
		
		RandomAccessFile accessFile=new RandomAccessFile(file,"rw");
        byte[] bytes=new byte[1024];
        accessFile.seek(1);
        for (int i=1;i<1025;i++){
            bytes[i-1]=accessFile.readByte();
        }
        accessFile.close();
        String md5=md5(bytes);
    //    String md5=md5(FileUtils.readFileToByteArray(file));
        return new String(md5).getBytes();
    }
    /**
     * md5加密字符串
     * @param str
     * @return
     */
    private static String md5(byte[] bytes) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(bytes);
            return new BigInteger(1, md.digest()).toString();
        } catch (Exception e) {
            return "";
        }
    }
    /**
     * 图片末尾加md5字节数组
     * @param file
     * @param md5Bytes
     * @throws Exception
     */
	private static void imgAppendMd5Bytes(File file,byte[] md5Bytes) throws Exception{
        RandomAccessFile accessFile=new RandomAccessFile(file,"rw");
        long length=accessFile.length();
        accessFile.seek(length);
        accessFile.write(md5Bytes);
        accessFile.close();
    }
    /**
     * 防止图片被篡改
     * @param file
     * @throws Exception
     */
	public static void preventTamper(File file) throws Exception{
        imgAppendMd5Bytes(file,img2Md5Bytes(file));
    }
    
    
    
  /**
   * 获取存储在图片末尾的字节
   */
    private static byte[] popMd5Bytes(File file,int t) throws Exception{
        RandomAccessFile accessFile=new RandomAccessFile(file,"rw");
        byte[] bytes=new byte[t];
        long length=accessFile.length();
        accessFile.seek(length-t);
        for (int i=0;i<t;i++){
            bytes[i]=accessFile.readByte();
        }
        accessFile.close();
        return bytes;
    }
    /**
     * 去除图片末尾的16个md5字节
     * @param file
     * @throws Exception
     */
    @SuppressWarnings("unused")
	private static void imgDelEndMd5Bytes(File file) throws Exception{
        RandomAccessFile accessFile=new RandomAccessFile(file,"rw");
        FileChannel fc = accessFile.getChannel();
        fc.truncate(accessFile.length()-1024);
        fc.close();
        accessFile.close();
    }
    /**
     * 验证图片是否被篡改
     * @param file
     * @return
     * @throws Exception
     */
	public  static boolean notTamper(File file) throws Exception{
		byte[] imgMd5=img2Md5Bytes(file);
        byte[] storageMd5=popMd5Bytes(file,imgMd5.length);//获取存储在图片末尾的1024个md5字节
        return Arrays.equals(storageMd5,imgMd5);
    }
}
