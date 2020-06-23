package com.supermap.wisdombusiness.workflow.service.file;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSchException;
import com.supermap.realestate.registration.util.GetProperties;
import com.supermap.wisdombusiness.workflow.util.SFTP;

@Service("sftpFileService")
public class sftpFileService {
	private String sFTPString = "";
	private String sFTPuser = "";
	private String sFTPpwd = "";
	private Integer sFTPport =23;
	private String Root = "";

	private String FileRoot;

	// 设置根目录
	public sftpFileService() {
		
	}

	public void setFileRoot(String root) {
		FileRoot = root;
	}

	public void UploadFile() {

	}

	private boolean ConvetFile(InputStream stream, String filePath,
			String filename)

	throws Exception {
		FileOutputStream fos = null;
		BufferedInputStream bis = null;
		int BUFFER_SIZE = 1024;
		byte[] buf = new byte[BUFFER_SIZE];
		int size = 0;
		bis = new BufferedInputStream(stream);
		File file = new File(filePath);
		if (!file.exists()) {
			file.mkdirs();
		}
		fos = new FileOutputStream(filePath + "/" + filename);
		while ((size = bis.read(buf)) != -1) {
			fos.write(buf, 0, size);
		}
		fos.close();
		bis.close();

		return true;

	}

	public String readFolder(String root, String folder, String SavePath)
			throws Exception {
		String Result = "";
		SFTP instance = new SFTP();
		ChannelSftp sftp = instance.connect( Root);
		String path = Root + root + "/" + folder;
		try {
			List<Map> files = instance.list(path, sftp);
			if (files != null && files.size() > 0) {
				for (Map dir : files) {
					String nameString = dir.get("name").toString();
					if (nameString.indexOf(".shp") > 0) {
						Result = nameString;
					}
					readFileStream(root + "/" + folder, SavePath, nameString);
				}
			}
		} catch (Exception e) {

		}

		instance.disconnect(sftp);

		return Result;
	}

	public boolean readFileStream(String root, String filePath, String filename)
			throws Exception {
		SFTP instance = new SFTP();
		boolean Result = false;
		InputStream stream = null;
		try {
			ChannelSftp sftp = instance.connect(Root);
			sftp.setFilenameEncoding("UTF-8");

			String pathString = Root + root;
			boolean exist = instance.isExist(pathString, filename, sftp);
			if (exist) {
				stream = sftp.get(pathString + "/" + filename);
				// stream=sftp.get("/111/33/44.txt");
				Result = ConvetFile(stream, filePath, filename);
			}
			instance.disconnect(sftp);

		} catch (JSchException e) {

			e.printStackTrace();
		}
		return Result;
	}

	public byte[] readFile(String root, String jnwjPath, String filePath)
			throws Exception {
		SFTP instance = new SFTP();
		InputStream stream = null;
		byte[] data = null;
		try {
			ChannelSftp sftp = instance.connect(Root);
			// sftp.setFilenameEncoding("UTF-8");
			// instance.mkDir("中文", sftp);
			// boolean
			// exist=instance.isExist("/国务院批准项目用海用岛/00a35356-9094-4d86-b6b5-03df0da1c174/用海批复文件/",
			// "021100031用海批复文件国海管字【2002】400号-渤西油田等10个油田69个项目.pdf", sftp);
			// boolean exist=instance.isExist("/111/33/", "44.txt", sftp);

			String pathString = Root + root + "/" + jnwjPath;
			// boolean
			// exist=instance.isExist("/国务院批准项目用海用岛/00a35356-9094-4d86-b6b5-03df0da1c174/用海批复文件/",
			// "021100031用海批复文件国海管字【2002】400号-渤西油田等10个油田69个项目.pdf", sftp);
			boolean exist = instance.isExist(pathString, filePath, sftp);
			if (exist) {
				stream = sftp.get(pathString + "/" + filePath);
				// stream=sftp.get("/111/33/44.txt");
				data = input2byte(stream);
			}
			instance.disconnect(sftp);

		} catch (JSchException e) {

			e.printStackTrace();
		}
		return data;
	}

	public byte[] input2byte(InputStream inStream) throws IOException {
		ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
		byte[] buff = new byte[100];
		int rc = 0;
		while ((rc = inStream.read(buff, 0, 100)) > 0) {
			swapStream.write(buff, 0, rc);
		}
		byte[] in2b = swapStream.toByteArray();
		return in2b;
	}

}
