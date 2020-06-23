package com.supermap.yingtanothers.file;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServlet;

import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileInputStream;

/**
 * 
 * 作者： 苗利涛 
 * 时间： 2016年1月27日 下午3:21:46 
 * 功能：将文件夹下文件（包括子文件夹文件），全部下载到一个目标文件中
 */

public class FileDownload extends HttpServlet {

	private static final long serialVersionUID = 1L;
    static String localFileDir = "";

	// 递归读取文件夹中文件
	public static void read(File readfFile, File writeFile) {
		try {
			if (readfFile.isFile()) {
				if (true) {
					read_to_write(readfFile, writeFile);

					System.out.println("下载的文件名==> " + readfFile.getName());
				}
			} else if (readfFile.isDirectory()) {
				File list[] = readfFile.listFiles();// 取得代表目录中所有文件的File对象数组
				for (int t = 0; t < list.length; t++) {
					read(list[t], writeFile);
				}
			}
		} catch (Exception e) {
			System.out.println("下载中间库附件发生错误");
			e.printStackTrace();
		}
	}

	// 将文件合并写入一个文件中
	public static void read_to_write(File readfFile, File writeFile) {
		try {

			FileInputStream in = new FileInputStream(readfFile);
			OutputStream out = new FileOutputStream(writeFile.toString() + "/" + readfFile.getName());

			int j = in.read();
			while (j != -1) {
				out.write(j);
				j = in.read();
			}
			out.close();
			in.close();

		} catch (Exception e) {
			System.out.println("下载发生错误的文件名==> " + readfFile.getName());
			e.printStackTrace();
		}
	}

	// 递归从共享文件夹读取文件
	public static void readShare(SmbFile smbFile, String localDir) {
		try {			
			if (smbFile.isFile()) {
				if (true) {
					smbGet(smbFile, localDir);
					System.out.println("下载的文件名==> " + smbFile.getName());
				}
			} else if (smbFile.isDirectory()) {				
				SmbFile[] list = smbFile.listFiles();// 取得代表目录中所有文件的File对象数组
				for (int t = 0; t < list.length; t++) {
					isFile(list[t], localDir);
				}
			}
		} catch (Exception e) {
			System.out.println("下载中间库附件发生错误");
			e.printStackTrace();
		}
	}
	//判断是否是文件夹
	public static void isFile (SmbFile smbFile,String localDir) {
		try {			
			if (smbFile.isFile()) {
				readShare(smbFile,localDir);
			} else if (smbFile.isDirectory()) {
				String fileName = smbFile.getName().replace("/", "");				
				localFileDir =localDir + File.separator + fileName;
				File localFiles = new File(localFileDir);
				if (!localFiles.exists()) {
					localFiles.mkdirs();
				}
				SmbFile[] list = smbFile.listFiles();// 取得代表目录中所有文件的File对象数组
				for (int i = 0; i < list.length; i++) {
					readShare(list[i], localFileDir);
				}
			}
		} catch (SmbException e) {
			e.printStackTrace();
		}
				
	}

	// 从共享目录下载文件
	public static void smbGet(SmbFile smbFile, String localDir) {
		InputStream in = null;
		OutputStream out = null;
		try {

			String fileName = smbFile.getName();
						
				File localFile = new File(localDir + File.separator + fileName);
				in = new BufferedInputStream(new SmbFileInputStream(smbFile));
				out = new BufferedOutputStream(new FileOutputStream(localFile));
				int j = in.read();
				while (j != -1) {
					out.write(j);
					j = in.read();
				}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (out != null && in != null) {
					out.close();
					in.close();
				}				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
}
