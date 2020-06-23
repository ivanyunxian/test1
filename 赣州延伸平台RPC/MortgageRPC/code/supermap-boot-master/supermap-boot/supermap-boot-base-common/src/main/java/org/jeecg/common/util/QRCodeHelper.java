package org.jeecg.common.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Hashtable;

//处理二维码的类WUZ
public class QRCodeHelper {
	private static final int BLACK = 0xFF000000;
	private static final int WHITE = 0xFFFFFFFF;
	private static String qrcodepath = "%s\\resources\\qrcode\\%s.%s";// 存放二维码图片路径

	private QRCodeHelper() {
	}

	private static BufferedImage toBufferedImage(BitMatrix matrix) {
		int width = matrix.getWidth();
		int height = matrix.getHeight();
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				image.setRGB(x, y, matrix.get(x, y) ? BLACK : WHITE);
			}
		}
		return image;
	}

	public static void writeToFile(BitMatrix matrix, String format, File file, int width, int height) throws IOException {
		BufferedImage image = toBufferedImage(matrix);
		
		image = zoomInImage(image,width,height);
		if (!ImageIO.write(image, format, file)) {
			throw new IOException("生成格式 " + format + "图片失败 ");
		}
	}

	@SuppressWarnings("unused")
	public static BufferedImage getImage(BitMatrix matrix, String format, int width, int height) throws IOException {
		BufferedImage image = toBufferedImage(matrix);
		image = zoomInImage(image,width,height);
		return image;
	}
	@SuppressWarnings("unused")
	public static void writeToStream(BitMatrix matrix, String format, int width, int height, OutputStream stream) throws IOException {
		BufferedImage image = toBufferedImage(matrix);
		image = zoomInImage(image,width,height);
		if (!ImageIO.write(image, format, stream)) {
			throw new IOException("生成格式 " + format + "图片失败");
		}
	}

	/**
	 * @作者 wuz
	 * @创建时间 2015年11月5日下午11:55:10
	 * @param content
	 *            转换成二维码内容
	 * @param imgname
	 *            转换图片名称
	 * @param imgformat
	 *            转换图片格式
	 * @param width
	 *            转换图片宽
	 * @param height
	 *            转换图片高
	 * @param request
	 *            请求对象
	 * @return
	 */
	public static boolean CreateQRCode(String content, String imgname, String imgformat, int width, int height, HttpServletRequest request) {
		boolean r = true;
		Hashtable hints = new Hashtable();
		hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
		try {

			BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hints);
			File outputFile = new File(String.format(qrcodepath, request.getRealPath("/"), imgname, imgformat));

			int margin = 5;  //自定义白边边框宽度

			bitMatrix = updateBit(bitMatrix, margin); 
			
			writeToFile(bitMatrix, imgformat, outputFile,width,height);
		} catch (IOException e) {
			r = false;
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (WriterException e) {
			r = false;
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return r;
	}
	public static BufferedImage CreateQRCode(String content, String imgformat, int width, int height) {
		boolean r = true;
		Hashtable hints = new Hashtable();
		hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
	//	ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hints);
			int margin = 5;  //自定义白边边框宽度
			bitMatrix = updateBit(bitMatrix, margin); 
			//writeToStream(bitMatrix,imgformat, width, height, baos);
			BufferedImage bfimage=	getImage(bitMatrix,imgformat,  width,  height);
			return bfimage;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (WriterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public static BufferedImage zoomInImage(BufferedImage originalImage, int width, int height){

		BufferedImage newImage = new BufferedImage(width,height,originalImage.getType());

		Graphics g = newImage.getGraphics();

		g.drawImage(originalImage, 0,0,width,height,null);

		g.dispose();

		return newImage;

		}

	private static BitMatrix updateBit(BitMatrix matrix, int margin) {

		int tempM = margin * 2;

		int[] rec = matrix.getEnclosingRectangle(); // 获取二维码图案的属性

		int resWidth = rec[2] + tempM;

		int resHeight = rec[3] + tempM;

		BitMatrix resMatrix = new BitMatrix(resWidth, resHeight); // 按照自定义边框生成新的BitMatrix

		resMatrix.clear();

		for (int i = margin; i < resWidth - margin; i++) { // 循环，将二维码图案绘制到新的bitMatrix中

			for (int j = margin; j < resHeight - margin; j++) {

				if (matrix.get(i - margin + rec[0], j - margin + rec[1])) {

					resMatrix.set(i, j);

				}

			}

		}

		return resMatrix;

	}
}
