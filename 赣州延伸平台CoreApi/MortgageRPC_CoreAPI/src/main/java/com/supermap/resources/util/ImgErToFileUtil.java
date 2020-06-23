package com.supermap.resources.util;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;

import oracle.sql.BLOB;


public class ImgErToFileUtil {
	/** 
     * 将接收的字符串转换成图片保存 
     * @param imgStr 二进制流转换的字符串 
     * @param imgPath 图片的保存路径 
     * @param imgName 图片的名称 
     * @return  
     *      1：保存正常 
     *      0：保存失败 
     */  
    public static int saveToImgByStr(String imgStr,String imgPath,String imgName){  
		try {  
		    System.out.println("===imgStr.length()====>" + imgStr.length()  
		            + "=====imgStr=====>" + imgStr);  
		} catch (Exception e) {  
		    e.printStackTrace();  
		}  
        int stateInt = 1;  
        if(imgStr != null && imgStr.length() > 0){  
            try {  
                  
                // 将字符串转换成二进制，用于显示图片    
                // 将上面生成的图片格式字符串 imgStr，还原成图片显示    
                byte[] imgByte = new byte[imgStr.length()];;    
      
                InputStream in = new ByteArrayInputStream(imgByte);  
      
                File file=new File(imgPath,imgName);//可以是任何图片格式.jpg,.png等  
                if(!file.exists()){
               	 file.createNewFile();
                }
                FileOutputStream fos=new FileOutputStream(file);  
                    
                byte[] b = new byte[1024];  
                int nRead = 0;  
                while ((nRead = in.read(b)) != -1) {  
                    fos.write(b, 0, nRead);  
                }  
                fos.flush();  
                fos.close();  
                in.close();  
      
            } catch (Exception e) {  
                stateInt = 0;  
                e.printStackTrace();  
            } finally {  
            }  
        }  
        return stateInt;  
    }  
    
	/** 
     * 将二进制转换成图片保存 
     * @param imgStr 二进制流转换的字符串 
     * @param imgPath 图片的保存路径 
     * @param imgName 图片的名称 
     * @return  
     *      true：保存正常 
     *      false：保存失败 
     */  
    public static boolean saveToImgByBytes(java.sql.Blob blob,String imgPath,String imgName){  
  
    	boolean stateInt = true;  
    	 try {  
    		 
    		 long nLen = blob.length();  
             int nSize = (int) nLen;
             File folder = new File(imgPath);
             if(!folder .exists()  && !folder .isDirectory()){       
                 //System.out.println("//不存在");  
                 folder.mkdir();  
             }
             File file=new File(imgPath,imgName);//可以是任何图片格式.jpg,.png等  
             if(!file.exists()){
            	 file.createNewFile();
             }
             System.out.println(imgPath);
             FileOutputStream fos=new FileOutputStream(file);  
             
             InputStream fis = blob.getBinaryStream();  
             
             byte[] b = new byte[nSize];  
             int nRead = 0;  
             while ((nRead = fis.read(b)) != -1) {  
                 fos.write(b, 0, nRead);  
             }  
             fos.flush();  
             fos.close();  
             fis.close();  
   
         } catch (Exception e) {  
             stateInt = false;  
             e.printStackTrace();  
         } finally {  
         }  
        return stateInt;  
    }  
	
    public static File getFileFromBytes(byte[] b, String outputFile){
        BufferedOutputStream stream = null;
        File file = null;
        try{
            file = new File(outputFile);
            FileOutputStream fstream = new FileOutputStream(file);
            stream = new BufferedOutputStream(fstream);
            stream.write(b);
        } catch (Exception e){
            e.printStackTrace();
        } finally{
            if (stream != null){
                try{
                    stream.close();
                } catch (IOException e1){
                    e1.printStackTrace();
                }
            }
        }
        return file;
    }
    
    public static boolean writeObjectToFile(Object obj,String imgPath,String imgName){
    	boolean stateInt = true; 
    	File folder = new File(imgPath);
        if(!folder .exists()  && !folder .isDirectory()){       
            //System.out.println("//不存在");  
            folder.mkdir();  
        }
    	
        try {
			File file = new File(imgPath, imgName);// 可以是任何图片格式.jpg,.png等
			if (!file.exists()) {
				file.createNewFile();
			} else {
				return stateInt;
			}
        	
			FileOutputStream out = new FileOutputStream(file);
            ObjectOutputStream objOut=new ObjectOutputStream(out);
            objOut.writeObject(obj);
            objOut.flush();
            objOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stateInt;
    }
    
	// 读取数据库中图片
    public static void byteToFile(byte[] buf, String filePath, String fileName)  
    {  
        BufferedOutputStream bufferOut = null;  
        FileOutputStream fileOut = null;  
        File file = null;  
        try  
        {  
            File resFile = new File(filePath);  
            if (!resFile.exists() && resFile.isDirectory())  
            {  
                resFile.mkdirs();  
            }  

            InputStream in = new ByteArrayInputStream(buf);  
            
            FileOutputStream fos=new FileOutputStream(file);  
                
            byte[] b = new byte[1024];  
            int nRead = 0;  
            while ((nRead = in.read(b)) != -1) {  
                fos.write(b, 0, nRead);  
            }  
            fos.flush();  
            fos.close();  
            in.close(); 
        }  
        catch (Exception e)  {  
            e.printStackTrace();  
        }  
    } 
}
