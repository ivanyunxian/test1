package com.supermap.wisdombusiness.workflow.util;

import java.awt.image.RenderedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.io.FileUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.FileOutputStream;

import com.itextpdf.text.pdf.PdfStructTreeController.returnType;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import com.jcraft.jsch.UserInfo;
import com.sun.media.jai.codec.FileSeekableStream;
import com.sun.media.jai.codec.ImageCodec;
import com.sun.media.jai.codec.ImageDecoder;
import com.sun.media.jai.codec.ImageEncodeParam;
import com.sun.media.jai.codec.ImageEncoder;
import com.sun.media.jai.codec.JPEGEncodeParam;
import com.sun.media.jai.codec.SeekableStream;
import com.supermap.realestate.registration.util.ConfigHelper;
import com.supermap.realestate.registration.util.GetProperties;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.wisdombusiness.workflow.model.Wfi_MaterData;
import com.supermap.wisdombusiness.workflow.model.Wfi_ProInst;
import com.supermap.wisdombusiness.workflow.model.Wfi_ProMater;
import com.supermap.wisdombusiness.workflow.service.common.Common;
import com.supermap.wisdombusiness.workflow.service.common.Http;
import com.supermap.wisdombusiness.workflow.service.common.SFTPEx;
import com.supermap.wisdombusiness.workflow.service.common.SFTPUser;
import com.thoughtworks.xstream.core.util.Base64Encoder;

/**  
 */
public class FileUpload {
	// public static final String FILE_PATH = "imageupload\\";

	// 文件上传
	/*
	 * public static String materialtype = GetProperties
	 * .getConstValueByKey("materialtype");
	 */
	/*
	 * public static String basicPath = GetProperties
	 * .getConstValueByKey("material");
	 */
	public static SFTP sftpInstance;
	public static ChannelSftp sftpchannel;
	private static Integer a = 0;

	public static List<Map> uploadFile(MultipartFile file, String materilinst_id, Wfi_ProInst inst) {
		try {
			CommonsMultipartFile cf = (CommonsMultipartFile) file; //
			DiskFileItem fi = (DiskFileItem) cf.getFileItem();
			File file1 = fi.getStoreLocation();
			if (file1.exists()) {
				SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");// 设置日期格式
				String fileName = df.format(new Date()) + "_" + java.net.URLEncoder.encode(file.getOriginalFilename(), "UTF-8");
				String materialtype = ConfigHelper.getNameByValue("materialtype");
				String mString = GetProperties.getConstValueByKey("materialtype");
				String filePath = GetNewPath(inst, materilinst_id);
				if (mString != null && !mString.equals("")) {
					materialtype = mString;
				}
				if (materialtype == null || materialtype.equals("") || materialtype.trim().equals("1")) {
					return harddisk(file, fileName, materilinst_id, filePath);
				} else if (materialtype.trim().equals("3")) {
					return serverUpload(file, fileName, filePath);
				} else {
					return sftpdisk(file, fileName, materilinst_id, filePath);
				}
			} else {
				return null;
			}

		} catch (Exception e) {
			return null;

		}

	}

	// 保存file到数据库

	@SuppressWarnings("unused")
	public static String GetNewPath(Wfi_ProInst inst, String materilinst_id) {
		SimpleDateFormat fir = new SimpleDateFormat("yyyyMM");// 设置日期格式
		String first = fir.format(inst.getCreat_Date());
		SimpleDateFormat sec = new SimpleDateFormat("dd");// 设置日期格式
		String second = sec.format(inst.getCreat_Date());
		return first + File.separator + second + File.separator + inst.getProinst_Id() + File.separator + materilinst_id;
	}

	public static List<Map> serverUpload(MultipartFile file, String fileName, String filePath) throws ParseException, IOException {
		List<Map> Result = new ArrayList<Map>();
		Map map = new HashMap();
		Result = Http.postFile2(file, fileName, filePath);
		return Result;
	}

	public static List<String> uploadFile(InputStream inputstream, String fileName, String materilinst_id, SFTP instance, ChannelSftp sftp) {
		try {
			String materialtype = ConfigHelper.getNameByValue("materialtype");
			// 不需要sftp方式了，都是同一个目录
			// if (materialtype == null || materialtype.equals("")
			// || materialtype.trim().equals("1")) {
			return harddisk(fileName, materilinst_id);
			// } else {
			// return sftpdisk(inputstream, fileName, materilinst_id,
			// instance, sftp);
			// }
		} catch (Exception e) {
			return null;

		}

	}
	public static List<Map> uploadFile(MultipartFile file, String fileName, String materilinst_id, String filePath) {
		try {
			CommonsMultipartFile cf = (CommonsMultipartFile) file; //
			DiskFileItem fi = (DiskFileItem) cf.getFileItem();
			File file1 = fi.getStoreLocation();
			if (file1.exists()) {
				SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");// 设置日期格式
				return harddisk(file, fileName, materilinst_id, filePath);
			} else {
				return null;
			}

		} catch (Exception e) {
			return null;

		}
	}
	private static List<Map> sftpdisk(MultipartFile file, String fileName, String materilinst_id, String filePath) throws Exception {
		String basicPath = ConfigHelper.getNameByValue("material");
		String Root = ConfigHelper.getNameByValue("filesftproot");
		List<Map> Result = new ArrayList<Map>();
		Map map = new HashMap();
		ChannelSftp sftp = null;
		SFTPUser user = SFTPEx.getSFTP();
		if (user != null) {
			sftp = user.getSftp();
		}
		try {
			if (file.getOriginalFilename().indexOf(".tif") > 0) {
				filePath += "\\" + fileName;
				File tempFile = new File(filePath);
				// fileName = tempFile.getName();
				// 文件存在，先删除
				if (tempFile.exists()) {
					tempFile.delete();
				}
				tempFile.createNewFile();
				file.transferTo(tempFile);
				Result = convertsftp(filePath, materilinst_id, sftp, filePath);

			} else {
				String allpath = Root + "/" + filePath.replace("\\", "/");
				String[] ml = allpath.split("/");
				String path = "/";
				for (String s : ml) {
					if (!s.equals("")) {
						if (!SFTPEx.isExist(path, s, sftp)) {
							SFTPEx.cd(path, sftp);
							SFTPEx.mkDir(s, sftp);
						}
						path += s + "/";
					}
				}
				/*
				 * if (!SFTPEx.isExist("/", path, sftp)) { SFTPEx.cd(path,
				 * sftp); SFTPEx.mkDir(materilinst_id, sftp); }
				 */
				SFTPEx.cd(path, sftp);
				byte[] files = file.getBytes();
				InputStream sbs = new ByteArrayInputStream(files);
				SFTPEx.uploadByFile(sbs, fileName, sftp);
				map.put("filename", fileName);
				map.put("filepath", filePath);
				map.put("disc", basicPath);
				Result.add(map);
				SFTPEx.calcelSFTP(user);
			}

		} catch (Exception e) {
			SFTPEx.calcelSFTP(user);
		}

		return Result;
	}

	/**
	 * 硬盘存储
	 * 
	 * @param file
	 * @param materilinst_id
	 * @return
	 */
	private static List<Map> harddisk(MultipartFile file, String fileName, String materilinst_id, String filePath) {
		List<Map> Result = new ArrayList<Map>();
		Map map = new HashMap();
		String disc = null;
		try {
			String basicPath = ConfigHelper.getNameByValue("material");
			disc = basicPath;
			basicPath = basicPath + filePath;
			File dirFile = new File(basicPath);
			if (!dirFile.exists()) {
				dirFile.mkdirs();
			}
			String allpath = basicPath + "\\" + fileName;
			File tempFile = new File(allpath);
			// fileName = tempFile.getName();
			// 文件存在，先删除
			if (tempFile.exists()) {
				tempFile.delete();
			}
			tempFile.createNewFile();
			file.transferTo(tempFile);

			if (file.getOriginalFilename().indexOf(".tif") > 0) {
				Result = convert(allpath, basicPath, filePath);
			} else {
				map.put("filename", fileName);
				map.put("filepath", filePath);
				map.put("disc", disc);
				Result.add(map);
			}

		} catch (Exception ex) {
			fileName = "";
		}
		return Result;
	}

	public static List<Map> convert(String originalFile, String Path, String filePath) {
		List<Map> Result = new ArrayList<Map>();
		String disc = ConfigHelper.getNameByValue("material");
		FileOutputStream outputStream = null;
		SeekableStream stream = null;
		try {

			stream = new FileSeekableStream(originalFile);
			ImageDecoder dec = (ImageDecoder) ImageCodec.createImageDecoder("TIFF", stream, null);
			int count = dec.getNumPages();
			for (int i = 0; i < count; i++) {
				Map map = new HashMap();
				String newfilename = Common.CreatUUID() + ".jpg";
				String newjpgPath = Path + "\\" + newfilename;
				File tifjpgFile = new File(newjpgPath);
				if (!tifjpgFile.exists()) {
					tifjpgFile.createNewFile();
				}
				outputStream = new FileOutputStream(newjpgPath);
				RenderedImage renderedImage = ((com.sun.media.jai.codec.ImageDecoder) dec).decodeAsRenderedImage(i);
				JPEGEncodeParam param = new JPEGEncodeParam();
				param.setQuality(1.0f);
				ImageEncoder encoder = ImageCodec.createImageEncoder("JPEG", outputStream, (ImageEncodeParam) param);
				encoder.encode(renderedImage);
				map.put("filename", newfilename);
				map.put("filepath", filePath);
				map.put("disc", disc);
				Result.add(map);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				outputStream.close();
				stream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		// s删除tiff 文件
		// 路径为文件且不为空则进行删除
		File file = new File(originalFile);
		if (file.isFile() && file.exists()) {
			file.delete();
		}
		return Result;
	}

	public static List<Map> convertsftp(String originalFile, String materilinst_id, ChannelSftp sftp, String filePath) throws Exception {
		List<Map> Result = new ArrayList<Map>();
		FileOutputStream outputStream = null;
		SeekableStream stream = null;
		try {
			String basicPath = ConfigHelper.getNameByValue("material");
			stream = new FileSeekableStream(originalFile);
			ImageDecoder dec = (ImageDecoder) ImageCodec.createImageDecoder("TIFF", stream, null);
			int count = dec.getNumPages();
			for (int i = 0; i < count; i++) {
				Map map = new HashMap();
				String newfilename = Common.CreatUUID() + ".jpg";
				String newjpgPath = basicPath + "\\" + materilinst_id + "\\" + newfilename;
				File tifjpgFile = new File(newjpgPath);
				if (!tifjpgFile.exists()) {
					tifjpgFile.createNewFile();
				}

				outputStream = new FileOutputStream(newjpgPath);
				RenderedImage renderedImage = ((com.sun.media.jai.codec.ImageDecoder) dec).decodeAsRenderedImage(i);
				JPEGEncodeParam param = new JPEGEncodeParam();
				param.setQuality(1.0f);
				ImageEncoder encoder = ImageCodec.createImageEncoder("JPEG", outputStream, (ImageEncodeParam) param);
				encoder.encode(renderedImage);
				String Root = ConfigHelper.getNameByValue("filesftproot");
				if (!SFTPEx.isExist(Root, materilinst_id, sftp)) {
					SFTPEx.mkDir(materilinst_id, sftp);
				}
				SFTPEx.cd(Root + "/" + materilinst_id, sftp);
				byte[] files = File2byte(newjpgPath);
				InputStream sbs = new ByteArrayInputStream(files);
				SFTPEx.uploadByFile(sbs, newfilename, sftp);
				map.put("filename", newfilename);
				map.put("filepath", filePath);
			}

		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				outputStream.close();
				stream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return Result;
	}

	public static byte[] File2byte(String filePath) {
		byte[] buffer = null;
		try {
			File file = new File(filePath);
			FileInputStream fis = new FileInputStream(file);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			byte[] b = new byte[1024];
			int n;
			while ((n = fis.read(b)) != -1) {
				bos.write(b, 0, n);
			}
			fis.close();
			bos.close();
			buffer = bos.toByteArray();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return buffer;
	}

	/**
	 * 临时文件分组
	 * */
	public static boolean FileGroup(Wfi_MaterData metaData, String matetinstid) {
		String basicPath = ConfigHelper.getNameByValue("material");
		String TMPPath = basicPath + "tmpFile";
		File dirFile = new File(TMPPath);
		if (dirFile.exists()) {

			String filePath = TMPPath + "\\" + metaData.getFile_Path();
			File tempFile = new File(filePath);
			if (tempFile.exists()) {
				// 移动该文件,整理数据
				String newfilepath = basicPath + matetinstid;
				dirFile = new File(newfilepath);
				if (!dirFile.exists()) {
					dirFile.mkdir();
				}
				dirFile = new File(newfilepath + "\\" + metaData.getFile_Path());
				tempFile.renameTo(dirFile);
				return true;
			}
		}
		return false;
	}

	private static String Getmaterialtype() {
		String Result = "";

		Result = ConfigHelper.getNameByValue("materialtype");
		String mString = GetProperties.getConstValueByKey("materialtype");
		if (mString != null && !mString.equals("")) {
			Result = mString;
		}
		return Result;
	}
	public static List<Map> GenerateImage(String imgStr, String materilinst_id, String filename, Wfi_ProInst inst){
		return GenerateImageEx(imgStr, materilinst_id, filename, inst,null);
	}

	public static List<Map> GenerateImageEx(String imgStr, String materilinst_id, String filename, Wfi_ProInst inst,byte[] byt) { // 对字节数组字符串进行Base64解码并生成图片
		List<Map> Result = new ArrayList<Map>();
		if (imgStr == null&&byt.length<1) // 图像数据为空
			return null;
		BASE64Decoder decoder = new BASE64Decoder();
		try {

			SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");// 设置日期格式

			// 将图片名称转换成encode，避免中文、空格等影响
			String fileName = df.format(new Date()) + "_" + java.net.URLEncoder.encode(filename, "UTF-8");
			// Base64解码
			byte[] b = null;
			if(null!=byt&&byt.length>0){
				b = byt;
			}else{
				imgStr = imgStr.substring(23);
				b = decoder.decodeBuffer(imgStr);
				for (int i = 0; i < b.length; ++i) {
					if (b[i] < 0) {// 调整异常数据
						b[i] += 256;
					}
				}
			}
			String filePath = GetNewPath(inst, materilinst_id);
			// 生成jpeg图片
			String basicPath = ConfigHelper.getNameByValue("material");
			String materialtype = Getmaterialtype();
			String imgFilePath = basicPath + filePath;
			File dirFile = new File(imgFilePath);
			if (!dirFile.exists()) {
				dirFile.mkdirs();
			}
			imgFilePath = imgFilePath + "\\" + fileName;
			File tempFile = new File(imgFilePath);
			if (tempFile.exists()) {
				tempFile.delete();
			}
			tempFile.createNewFile();
			FileOutputStream out = new FileOutputStream(imgFilePath);
			out.write(b);
			out.flush();
			out.close();
			if (materialtype.equals("1")) {
				Map map = new HashMap();
				map.put("filename", fileName);
				map.put("filepath", filePath);
				Result.add(map);
				return Result;
			} else if (materialtype.equals("3")) {

				Map map = new HashMap();
				Result = Http.postFile(tempFile, fileName, filePath);
				return Result;
			}

		} catch (Exception e) {

		}
		return null;
	}
	public static byte[] getFile(String path, String filename) throws IOException {
		String materialtype = ConfigHelper.getNameByValue("materialtype");
		String mString = GetProperties.getConstValueByKey("materialtype");
		if (mString != null && !mString.equals("")) {
			materialtype = mString;
		}
		if (materialtype == null || materialtype.equals("") || materialtype.trim().equals("1")) {
			File file = new File(path + "\\" + filename);
			if (file.exists()) {
				return FileUtils.readFileToByteArray(file);
			} else {
				return null;
			}

		} else if (materialtype.trim().equals("3")) {
			return Http.getFile(path, filename);
		} else {
			byte[] data = null;
			ChannelSftp sftp = null;
			SFTPUser user = SFTPEx.getSFTP();
			if (user != null) {
				sftp = user.getSftp();
			}
			try {
				String Root = ConfigHelper.getNameByValue("filesftproot");
				InputStream stream = null;

				String root = "/" + Root;
				path = path.replace("\\", "/");
				SFTPEx.cd("/", sftp);
				if (SFTPEx.isExist(root + "/" + path + "/", filename, sftp)) {
					stream = sftp.get(root + "/" + path + "/" + filename);
					data = input2byte(stream);
					SFTPEx.calcelSFTP(user);
					return data;
				}

			} catch (Exception e) {
				System.out.println(e.getMessage());
				SFTPEx.calcelSFTP(user);
			}
			return data;

		}

	}

	// 获取银行委托书当前页图片byte
	public static byte[] getBanktrustbookFile(String trsutbookpage_path, String trustbook_name) throws IOException {
		String basicPath = ConfigHelper.getNameByValue("banktrustbook");
		File file = new File(basicPath + trsutbookpage_path + "\\" + trustbook_name);
		if (file.exists()) {
			InputStream in = null;
			byte[] data = null;
			// 读取图片字节数组
			try {
				in = new FileInputStream(basicPath + trsutbookpage_path + "\\" + trustbook_name);
				data = new byte[in.available()];
				in.read(data);
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return data;
		} else {
			return null;
		}
	}

	public static byte[] input2byte(InputStream inStream) throws IOException {
		ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
		byte[] buff = new byte[100];
		int rc = 0;
		while ((rc = inStream.read(buff, 0, 100)) > 0) {
			swapStream.write(buff, 0, rc);
		}
		byte[] in2b = swapStream.toByteArray();
		return in2b;
	}

	public static void MoveFile(String src, String target, String filename) {
		String basicPath = ConfigHelper.getNameByValue("material");
		File f = new File(basicPath + src + "\\" + filename);
		if (f.exists()) {
			File fnewpath = new File(basicPath + target);
			// 判断文件夹是否存在
			if (!fnewpath.exists())
				fnewpath.mkdirs();
			// 将文件移到新文件里
			File fnew = new File(basicPath + target + "\\" + filename);
			f.renameTo(fnew);
		}

	}

	public static boolean deteleImage(HttpServletRequest request, String materilinst_id, String fileName) {
		boolean flag = false;
		String basicPath = ConfigHelper.getNameByValue("material");
		File tempFile = new File(basicPath + materilinst_id + "\\", String.valueOf(fileName));
		if (tempFile.exists()) {
			flag = tempFile.delete();
		} else {
			flag = true;
		}
		return flag;
	}

	private static List<String> sftpdisk(InputStream inputStream, String fileName, String materilinst_id, SFTP instance, ChannelSftp sftp) throws Exception {
		// SFTP instance = new SFTP();
		List<String> Result = new ArrayList<String>();
		// if (sFTPport != null && !sFTPport.equals("")) {
		// Integer port = Integer.parseInt(sFTPport);
		// ChannelSftp sftp = instance.connect(sFTPString, sFTPuser,
		// sFTPpwd,
		// port, "/");
		sftp.setFilenameEncoding("UTF-8");
		String Root = ConfigHelper.getNameByValue("filesftproot");
		String[] ml = Root.split("/");
		String path = "/";
		for (String s : ml) {

			if (s.equals("")) {

			} else {
				System.out.println("正在ftp上检查创建文件夹...");
				if (!instance.isExist(path, s, sftp)) {

					instance.mkDir(s, sftp);
				}
				path += s + "/";
			}

		}

		if (!instance.isExist(path, materilinst_id, sftp)) {
			instance.cd(path, sftp);
			instance.mkDir(materilinst_id, sftp);
		}
		instance.cd(path + materilinst_id, sftp);
		instance.uploadByFile(inputStream, fileName, sftp);
		Result.add(fileName);

		// instance.disconnect(sftp);
		// }

		return Result;
	}

	/**
	 * 硬盘存储
	 * 
	 * @param file
	 * @param materilinst_id
	 * @return
	 */
	private static List<String> harddisk(String fileName, String materilinst_id) {
		List<String> Result = new ArrayList<String>();
		try {
			String basicPath = ConfigHelper.getNameByValue("material");
			String filePath = basicPath + materilinst_id;
			File dirFile = new File(filePath);
			if (!dirFile.exists()) {
				dirFile.mkdir();
			}
			basicPath = filePath;

			filePath += "\\" + fileName;
			File tempFile = new File(filePath);
			// fileName = tempFile.getName();
			// 文件存在，先删除
			if (tempFile.exists()) {
				tempFile.delete();
			}
			tempFile.createNewFile();
			// file.transferTo(tempFile);
			//
			// if (file.getOriginalFilename().indexOf(".tif") > 0) {
			// Result = convert(filePath, basicPath);
			// } else {
			Result.add(fileName);
			// }

		} catch (Exception ex) {
			fileName = "";
		}
		return Result;
	}

	public static Boolean connectSFTP() {
		Boolean suc = false;
		sftpInstance = new SFTP();
		try {
			sftpchannel = sftpInstance.connect("/");
			suc = true;
		} catch (NumberFormatException e) {
			suc = false;
			e.printStackTrace();
		} catch (JSchException e) {
			suc = false;
			e.printStackTrace();
		} catch (SftpException e) {
			suc = false;
			e.printStackTrace();
		}
		return suc;
	}

	public static void disconnectSFTP() {
		try {
			if (sftpInstance != null && sftpchannel.isConnected())
				sftpInstance.disconnect(sftpchannel);
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	public ChannelSftp getChannelSftp(SFTP sfpt) {
		ChannelSftp sftp = null;

		try {
			sftp = sfpt.connect("/");
			sftp.setFilenameEncoding("UTF-8");
		} catch (JSchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SftpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
		return sftp;
	}

	/**************************** 银行委托书上传存储直接调用 **************************************/
	/* 银行委托书扫描上传 */
	public static List<String> uploadtrustbook(MultipartFile file, String Trustbookpage_Id) {
		try {

			CommonsMultipartFile cf = (CommonsMultipartFile) file; //
			DiskFileItem fi = (DiskFileItem) cf.getFileItem();
			File file1 = fi.getStoreLocation();
			if (file1.exists()) {
				SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");// 设置日期格式
				String fileName = df.format(new Date()) + "_" + java.net.URLEncoder.encode(file.getOriginalFilename(), "UTF-8");
				return Trustbookharddisk(file, fileName, Trustbookpage_Id);
			} else {
				return null;
			}

		} catch (Exception e) {
			return null;

		}
	}

	/**
	 * 硬盘存储
	 * 
	 * @param file
	 * @param materilinst_id
	 * @return
	 */
	private static List<String> Trustbookharddisk(MultipartFile file, String fileName, String Trustbookpage_Id) {
		List<String> Result = new ArrayList<String>();
		try {
			String basicPath = ConfigHelper.getNameByValue("banktrustbook");
			String filePath = basicPath + Trustbookpage_Id;
			File dirFile = new File(filePath);
			if (!dirFile.exists()) {
				dirFile.mkdir();
			}
			basicPath = filePath;

			filePath += "\\" + fileName;
			File tempFile = new File(filePath);
			// fileName = tempFile.getName();
			// 文件存在，先删除
			if (tempFile.exists()) {
				tempFile.delete();
			}
			tempFile.createNewFile();
			file.transferTo(tempFile);

			if (file.getOriginalFilename().indexOf(".tif") > 0) {
				Result = convert(filePath, basicPath);
			} else {
				Result.add(fileName);
			}

		} catch (Exception ex) {
			fileName = "";
		}
		return Result;
	}

	public static String trustbookpagegetFile(String materilinst_id, String File_name) throws IOException {
		String basicPath = ConfigHelper.getNameByValue("banktrustbook");
		File file = new File(basicPath + materilinst_id + "\\" + File_name);
		if (file.exists()) {
			// return FileUtils.readFileToByteArray(file);
			return GetImageStr(basicPath + materilinst_id + "\\" + File_name);
		} else {
			return "";
		}
	}

	public static String GetImageStr(String imgFile) {// 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
														// String imgFile =
														// "d:\\111.jpg";//待处理的图片
		InputStream in = null;
		byte[] data = null;
		// 读取图片字节数组
		try {
			in = new FileInputStream(imgFile);
			data = new byte[in.available()];
			in.read(data);
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 对字节数组Base64编码
		BASE64Encoder encoder = new BASE64Encoder();
		return encoder.encode(data);// 返回Base64编码过的字节数组字符串
	}

	public static List<String> convert(String originalFile, String Path) {
		List<String> Result = new ArrayList<String>();
		FileOutputStream outputStream = null;
		SeekableStream stream = null;
		try {

			stream = new FileSeekableStream(originalFile);
			ImageDecoder dec = (ImageDecoder) ImageCodec.createImageDecoder("TIFF", stream, null);
			int count = dec.getNumPages();
			for (int i = 0; i < count; i++) {
				String newfilename = Common.CreatUUID() + ".jpg";
				String newjpgPath = Path + "\\" + newfilename;
				File tifjpgFile = new File(newjpgPath);
				if (!tifjpgFile.exists()) {
					tifjpgFile.createNewFile();
				}
				outputStream = new FileOutputStream(newjpgPath);
				RenderedImage renderedImage = ((com.sun.media.jai.codec.ImageDecoder) dec).decodeAsRenderedImage(i);
				JPEGEncodeParam param = new JPEGEncodeParam();
				param.setQuality(1.0f);
				ImageEncoder encoder = ImageCodec.createImageEncoder("JPEG", outputStream, (ImageEncodeParam) param);
				encoder.encode(renderedImage);
				Result.add(newfilename);
			}

		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				outputStream.close();
				stream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return Result;
	}

}