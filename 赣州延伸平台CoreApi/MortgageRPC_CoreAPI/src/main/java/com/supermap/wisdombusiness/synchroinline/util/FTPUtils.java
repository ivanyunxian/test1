package com.supermap.wisdombusiness.synchroinline.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

public class FTPUtils
{

	private static FTPClient ftp = new FTPClient();

	private static Properties p = null;
	private static String ADDRESS = null;
	private static Integer PORT;
	private static String USERNAME = null;
	private static String PASSWORD = null;
	@SuppressWarnings("unused")
	private static String FTPROOTDIR = null;

	static
	{
		p = new PropertyUtil().getConfigProperties();
		ADDRESS = p.getProperty("address");
		PORT = Integer.parseInt(p.getProperty("port"));
		USERNAME = p.getProperty("ftp_username");
		PASSWORD = p.getProperty("ftp_password");
		FTPROOTDIR = p.getProperty("ftprootdir");
	}

	private static final Log logger = LogFactory.getLog(FTPUtils.class);

	/**
	 * 
	 * @param path
	 *            上传到ftp服务器哪个路径下
	 * @param addr
	 *            地址
	 * @param port
	 *            端口号
	 * @param username
	 *            用户名
	 * @param password
	 *            密码
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unused")
	public static void connect() throws Exception
	{
		ftp = new FTPClient();
		FTPClientConfig config = new FTPClientConfig(FTPClientConfig.SYST_NT);
		ftp.configure(config);
		Integer reply = null;
		ftp.connect(ADDRESS, PORT);
		ftp.login(USERNAME, PASSWORD);
		ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
		reply = ftp.getReplyCode();
		if (!FTPReply.isPositiveCompletion(reply))
		{
			ftp.disconnect();
			throw new Exception("连接ftp失败。");
		}
	}

	/**
	 * 
	 * @param file
	 *            上传的文件或文件夹
	 * @throws Exception
	 */
	@SuppressWarnings("unused")
	private static void upload(File file) throws Exception
	{
		if (file.isDirectory())
		{
			ftp.makeDirectory(file.getName());
			ftp.changeWorkingDirectory(file.getName());
			String[] files = file.list();
			for (int i = 0; i < files.length; i++)
			{
				File file1 = new File(file.getPath() + "\\" + files[i]);
				if (file1.isDirectory())
				{
					upload(file1);
					ftp.changeToParentDirectory();
				}
				else
				{
					File file2 = new File(file.getPath() + "\\" + files[i]);
					FileInputStream input = new FileInputStream(file2);
					ftp.storeFile(file2.getName(), input);
					input.close();
				}
			}
		}
		else
		{
			File file2 = new File(file.getPath());
			FileInputStream input = new FileInputStream(file2);
			boolean makeDir = ftp.makeDirectory("ttt");
			ftp.enterLocalPassiveMode();
			ftp.setControlEncoding("UTF-8");
			boolean uploadRes = ftp.storeFile(file2.getName(), input);
			input.close();
		}
	}

	/**
	 * 下载链接配置
	 * 
	 * @author JHX
	 * @param f
	 * @param localBaseDir
	 *            本地目录
	 * @param remoteBaseDir
	 *            远程目录
	 * @throws Exception
	 */
	public static void startDown(String localBaseDir, String remoteBaseDir) throws Exception
	{
		if (!ftp.isConnected())
		{
			ftp = new FTPClient();
			ftp.connect(ADDRESS, PORT);
			ftp.login(USERNAME, PASSWORD);
		}
		try
		{
			FTPFile[] files = null;
			boolean changedir = ftp.changeWorkingDirectory(remoteBaseDir);
			if (changedir)
			{
				ftp.enterLocalPassiveMode();
				ftp.setControlEncoding("GBK");
				files = ftp.listFiles();
				for (int i = 0; i < files.length; i++)
				{
					try
					{
						downloadFile(files[i], localBaseDir, remoteBaseDir);
					}
					catch (Exception e)
					{
						logger.error(e);
						logger.error("<" + files[i].getName() + ">下载失败");
					}
				}
			}
		}
		catch (Exception e)
		{
			logger.error(e);
			logger.error("下载过程中出现异常");
		}
	}

	/**
	 * 下载FTP文件 当你需要下载FTP文件的时候，调用此方法 根据<b>获取的文件名，本地地址，远程地址</b>进行下载
	 * 
	 * @author JHX
	 * @param ftpFile
	 * @param relativeLocalPath
	 * @param relativeRemotePath
	 */
	private static void downloadFile(FTPFile ftpFile, String relativeLocalPath, String relativeRemotePath)
	{
		if (ftpFile.isFile())
		{
			if (ftpFile.getName().indexOf("?") == -1)
			{
				OutputStream outputStream = null;
				try
				{
					File locaFile = new File(relativeLocalPath + ftpFile.getName());
					// 判断文件是否存在，存在则返回
					if (locaFile.exists())
					{
						return;
					}
					else
					{
						outputStream = new FileOutputStream(relativeLocalPath + ftpFile.getName());
						ftp.retrieveFile(ftpFile.getName(), outputStream);
						outputStream.flush();
						outputStream.close();
					}
				}
				catch (Exception e)
				{
					logger.error(e);
				}
				finally
				{
					try
					{
						if (outputStream != null)
						{
							outputStream.close();
						}
					}
					catch (IOException e)
					{
						logger.error("输出文件流异常");
					}
				}
			}
		}
		else
		{
			String newlocalRelatePath = relativeLocalPath + ftpFile.getName();
			String newRemote = new String(relativeRemotePath + ftpFile.getName().toString());
			File fl = new File(newlocalRelatePath);
			if (!fl.exists())
			{
				fl.mkdirs();
			}
			try
			{
				newlocalRelatePath = newlocalRelatePath + '/';
				newRemote = newRemote + "/";
				String currentWorkDir = ftpFile.getName().toString();
				boolean changedir = ftp.changeWorkingDirectory(currentWorkDir);
				if (changedir)
				{
					FTPFile[] files = null;
					files = ftp.listFiles();
					for (int i = 0; i < files.length; i++)
					{
						downloadFile(files[i], newlocalRelatePath, newRemote);
					}
				}
				if (changedir)
				{
					ftp.changeToParentDirectory();
				}
			}
			catch (Exception e)
			{
				logger.error(e);
			}
		}
	}

	/**
	 * 从FTP服务器下载文件
	 * 
	 * @param path
	 *            文件目录
	 * @param fileName
	 *            文件名称
	 * @return
	 * @throws Exception
	 */
	public static ByteArrayOutputStream downloadFile(String path, String fileName) throws Exception
	{
		ByteArrayOutputStream outputStream = null;
		if (!ftp.isConnected())
		{
			ftp = new FTPClient();
			ftp.connect(ADDRESS, PORT);
			ftp.login(USERNAME, PASSWORD);
		}
		FTPFile[] files = null;
		boolean changedir = ftp.changeWorkingDirectory(path);
		if (changedir)
		{
			ftp.enterLocalPassiveMode();
			ftp.setControlEncoding("GBK");
			files = ftp.listFiles();
			for (FTPFile ftpFile : files)
			{
				if (fileName.equals(ftpFile.getName()))
				{
					outputStream = new ByteArrayOutputStream();
					Boolean isok = ftp.retrieveFile(ftpFile.getName(), outputStream);
					if (!isok)
						throw new Exception("从FTP下载文件失败，文件：" + path + "/" + fileName);
					break;
				}
			}
		}
		else
		{
			throw new Exception("访问FTP路径失败，Path：" + path);
		}
		return outputStream;
	}

	/**
	 * 关闭ftp连接
	 */
	public static void closeFtp()
	{
		if (ftp != null && ftp.isConnected())
		{
			try
			{
				ftp.logout();
				ftp.disconnect();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
	/*
	 * public static void main(String[] args) throws Exception{ boolean
	 * c=FTPUtils.connect(); File file = new File("e:\\uploadify.jpg"); long
	 * a=file.length(); FTPUtils.startDown("D:\\FTP\\","/201612/25");
	 * FTPUtils.closeFtp(); }
	 */
}