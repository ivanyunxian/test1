package com.supermap.realestate.registration.tools; 
import java.io.File;   
  
 /**
  *  
  * @author mss
  *
  */
public class FileOperateUtil {  
    /** 
     *  根据路径删除指定的目录或文件，无论存在与否 
     *@param sPath  要删除的目录或文件 
     *@return 删除成功返回 true，否则返回 false。 
     */  
    public static  boolean DeleteFolder(String sPath) {  
    	boolean flag = false;  
    	File file = new File(sPath);  
        // 判断目录或文件是否存在  
        if (!file.exists()) {  // 不存在返回 false  
            return flag;  
        } else {  
            // 判断是否为文件  
            if (file.isFile()) {  // 为文件时调用删除文件方法  
                return deleteFile(sPath);  
            } else {  // 为目录时调用删除目录方法  
                return deleteDirectory(sPath);  
            }  
        }  
    } 
    
    /** 
     * 删除单个文件 
     * @param   sPath    被删除文件的文件名 
     * @return 单个文件删除成功返回true，否则返回false 
     */  
    public  static boolean deleteFile(String sPath) {  
    	boolean flag = false;  
    	File file = new File(sPath);  
        // 路径为文件且不为空则进行删除  
        if (file.isFile() && file.exists()) {  
            file.delete();  
            flag = true;  
        }  
        return flag;  
    } 
    
    /** 
     * 删除目录（文件夹）以及目录下的文件 
     * @param   sPath 被删除目录的文件路径 
     * @return  目录删除成功返回true，否则返回false 
     */  
    private static  boolean deleteDirectory(String sPath) {  
        //如果sPath不以文件分隔符结尾，自动添加文件分隔符  
        if (!sPath.endsWith(File.separator)) {  
            sPath = sPath + File.separator;  
        }  
        File dirFile = new File(sPath);  
        //如果dir对应的文件不存在，或者不是一个目录，则退出  
        if (!dirFile.exists() || !dirFile.isDirectory()) {  
            return false;  
        }  
        boolean flag = true;  
        //删除文件夹下的所有文件(包括子目录)  
        File[] files = dirFile.listFiles();  
        for (int i = 0; i < files.length; i++) {  
            //删除子文件  
            if (files[i].isFile()) {  
                flag = deleteFile(files[i].getAbsolutePath());  
                if (!flag) break;  
            } //删除子目录  
            else {  
                flag = deleteDirectory(files[i].getAbsolutePath());  
                if (!flag) break;  
            }  
        }  
        if (!flag) return false;  
        //删除当前目录  
        if (dirFile.delete()) {  
            return true;  
        } else {  
            return false;  
        }  
    }
     
  
    
/************************************************** 具体文件类型转换的公用方法***************************************************/
    
 private  final static int xlTypePDF = 0;
// private  final int msoTrue = -1;
// private  final int msofalse = 0;
  
    public static  String getFileSufix(String fileName){
        int splitIndex = fileName.lastIndexOf(".");
        return fileName.substring(splitIndex + 1);
    }
    public static  boolean excel2PDF(String inputFile,String pdfFile){
        try{
        	/*
            ActiveXComponent app = new ActiveXComponent("Excel.Application");
            app.setProperty("Visible", false);
            Dispatch excels = (Dispatch) app.getProperty("Workbooks").toDispatch();
            Dispatch excel = Dispatch.call(excels,
                                    "Open",
                                    inputFile,
                                    false,
                                    true
                                    ).toDispatch();
            Dispatch.call(excel,
                    "ExportAsFixedFormat",
                    xlTypePDF,      
                    pdfFile
                    );
            Dispatch.call(excel, "Close",false);
            app.invoke("Quit");
            */
            return true;
        }catch(Exception e){
        	return false;
        }
         
    }
 
 
}
