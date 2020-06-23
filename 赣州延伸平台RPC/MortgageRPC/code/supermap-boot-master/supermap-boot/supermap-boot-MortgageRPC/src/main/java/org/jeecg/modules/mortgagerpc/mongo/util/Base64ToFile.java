package org.jeecg.modules.mortgagerpc.mongo.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import org.jeecg.common.util.Base64Util;

public class Base64ToFile {

	public static boolean base64ToFile(String base64,String filepath){
		if ("".equals(base64)) // 图像数据为空
            return false;
        try {
            // Base64解码
        	byte[] b = Base64Util.decode(base64);
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {// 调整异常数据
                    b[i] += 256;
                }
            }
            File file = new File(filepath);
            if(!file.exists()){
            	file.createNewFile();
            }
            OutputStream out = new FileOutputStream(filepath);
            out.write(b);
            out.flush();
            out.close();
 
            return true;
        } catch (Exception e) {
            return false;
        }
	}
}
