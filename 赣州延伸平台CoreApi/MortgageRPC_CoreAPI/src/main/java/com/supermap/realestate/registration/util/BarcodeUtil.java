/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.supermap.realestate.registration.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;

import org.jbarcode.JBarcode;
import org.jbarcode.encode.Code128Encoder;
import org.jbarcode.encode.Code39Encoder;
import org.jbarcode.encode.EAN13Encoder;
import org.jbarcode.paint.BaseLineTextPainter;
import org.jbarcode.paint.EAN13TextPainter;
import org.jbarcode.paint.TextPainter;
import org.jbarcode.paint.WideRatioCodedPainter;
import org.jbarcode.paint.WidthCodedPainter;
import org.jbarcode.util.ImageUtil;

import com.google.zxing.common.BitMatrix;
import com.supermap.realestate.registration.util.BarcodeUtil.CustomTextPainter;

/**
 * 2015-12-1
 * 
 * @author 欧展榕
 */
public class BarcodeUtil {
    private static String oneBarCodePath = "";// 存放条形码图片路径
    private static final int FONT_SIZE = 15;
    private static final String FONT_FAMILY = "console";

    public static String createBarcode(String content, String type, String path) {
	try {
	    oneBarCodePath = path;
	    JBarcode localJBarcode = new JBarcode(Code128Encoder.getInstance(),
		    WidthCodedPainter.getInstance(),
		    BaseLineTextPainter.getInstance());

	    if ("EAN13".equals(type.toUpperCase())) {
		localJBarcode.setEncoder(EAN13Encoder.getInstance());
		localJBarcode.setPainter(WideRatioCodedPainter.getInstance());
		localJBarcode.setTextPainter(EAN13TextPainter.getInstance());
		localJBarcode.setShowCheckDigit(false);
	    } else if ("CODE39".equals(type.toUpperCase())) {
		localJBarcode.setEncoder(Code39Encoder.getInstance());
		localJBarcode.setPainter(WideRatioCodedPainter.getInstance());
		localJBarcode.setTextPainter(BaseLineTextPainter.getInstance());
		localJBarcode.setShowCheckDigit(false);
	    }
	    BufferedImage localBufferedImage = localJBarcode
		    .createBarcode(content);
	    saveToPNG(localBufferedImage, content + ".png");
	    return content+".png";
	} catch (Exception localException) {
	    localException.printStackTrace();
	    return "error.png";
	}
    }
    public static BufferedImage createBarcodeStream(String content, String type,int width,int height, boolean isShowText) {
    	try {
    	    JBarcode localJBarcode = new JBarcode(Code128Encoder.getInstance(),
    		    WidthCodedPainter.getInstance(),
    		    CustomTextPainter.getInstance());
    	    localJBarcode.setShowText(isShowText);
    	    if ("EAN13".equals(type.toUpperCase())) {
    		localJBarcode.setEncoder(EAN13Encoder.getInstance());
    		localJBarcode.setPainter(WideRatioCodedPainter.getInstance());
    		localJBarcode.setTextPainter(EAN13TextPainter.getInstance());
    		localJBarcode.setShowCheckDigit(false);
    	    } else if ("CODE39".equals(type.toUpperCase())) {
    		localJBarcode.setEncoder(Code39Encoder.getInstance());
    		localJBarcode.setPainter(WideRatioCodedPainter.getInstance());
    		localJBarcode.setTextPainter(BaseLineTextPainter.getInstance());
    		localJBarcode.setShowCheckDigit(false);
    	    }
    	    BufferedImage localBufferedImage = localJBarcode
    		    .createBarcode(content);
    	    localBufferedImage = zoomInImage(localBufferedImage,width,height);
    		return localBufferedImage;
    	} catch (Exception localException) {
    	    localException.printStackTrace();
    	    return null;
    	}
        }
	public static BufferedImage  zoomInImage(BufferedImage  originalImage, int width, int height){

		BufferedImage newImage = new BufferedImage(width,height,originalImage.getType());

		Graphics g = newImage.getGraphics();

		g.drawImage(originalImage, 0,0,width,height,null);

		g.dispose();

		return newImage;

		}
    static void saveToJPEG(BufferedImage paramBufferedImage, String paramString) {
	saveToFile(paramBufferedImage, paramString, "jpeg");
    }

    static void saveToPNG(BufferedImage paramBufferedImage, String paramString) {
	saveToFile(paramBufferedImage, paramString, "png");
    }

    static void saveToGIF(BufferedImage paramBufferedImage, String paramString) {
	saveToFile(paramBufferedImage, paramString, "gif");
    }

    static void saveToFile(BufferedImage paramBufferedImage,
	    String paramString1, String paramString2) {
	try {
	    FileOutputStream localFileOutputStream = new FileOutputStream(
		    oneBarCodePath + paramString1);
	    ImageUtil.encodeAndWrite(paramBufferedImage, paramString2,
		    localFileOutputStream, 96, 96);
	    localFileOutputStream.close();
	} catch (Exception localException) {
	    localException.printStackTrace();
	}
    }
    
    /**
     * 内部静态类
     * 参考BaseLineTextPainter.getInstance()
     * 自定义TextPainter，可以定义字体、字体大小
     * @author luml
     *
     */
    protected static class CustomTextPainter implements TextPainter { 
    	private static CustomTextPainter instance =new CustomTextPainter(); 
    	public static CustomTextPainter getInstance(){ 
    	   return instance; 
    	} 
    	public void paintText(BufferedImage barCodeImage,String text,int width ){
    		Graphics g =barCodeImage.getGraphics();
    		Font font = new Font(FONT_FAMILY, Font.PLAIN, FONT_SIZE*width);//字体样式、大小
    		g.setFont(font);
    		FontMetrics fm = g.getFontMetrics();
    		int height = fm.getHeight();
    		int center = (barCodeImage.getWidth() - fm.stringWidth(text))/2;
    		g.setColor(Color.WHITE);
    		g.fillRect(0, 0, barCodeImage.getWidth(), barCodeImage.getHeight()*1/20);
    		g.fillRect(0, barCodeImage.getHeight()-height*9/10,barCodeImage.getWidth() , height*9/10);
    		g.setColor(Color.BLACK);
    		g.drawString(text, 0, 145);
    		g.drawString(text, center, barCodeImage.getHeight()-(height/10)-2);
    		
    	}
   }
}
