package com.supermap.realestate.registration.service.Sender;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpATTRS;
import com.jcraft.jsch.SftpException;

public class SFTP {
    protected static String ip = "";
    protected static String port = "";
    protected static String userName = "";
    protected static String password = "";

    public ChannelSftp getSftp() {
	ChannelSftp _sftp = null;
	try {
	    _sftp = connect(ip, userName, password, Integer.valueOf(port),
		    _root);
	} catch (NumberFormatException e) {
	    //e.printStackTrace();
	} catch (JSchException e) {
	    //e.printStackTrace();
	} catch (SftpException e) {
	    //e.printStackTrace();
	}
	return _sftp;
    }

    public Logger logger = Logger.getLogger(SFTP.class);
    private String _root = "/";
    com.jcraft.jsch.Session _sshSession;

    public ChannelSftp connect(String host, String userName, String password,
	    int port, String root) throws JSchException, SftpException {
	ChannelSftp sftp = null;

	JSch jsch = new JSch();
	_sshSession = jsch.getSession(userName, host, port);

	_sshSession.setPassword(password);
	Properties sshConfig = new Properties();
	sshConfig.put("StrictHostKeyChecking", "no");
	_sshSession.setConfig(sshConfig);
	_sshSession.connect();
	Channel channel = _sshSession.openChannel("sftp");
	channel.connect();
	sftp = (ChannelSftp) channel;
	logger.info("链接到SFTP成功 ：" + host + " 用户：" + userName);
	sftp.cd(root);
	_root += root;
	return sftp;
    }

    public void disconnect(ChannelSftp sftp) {
	if (sftp != null) {
	    try {
		if (sftp.isConnected()) {
		    if (sftp.getSession() != null) {
			sftp.getSession().disconnect();
		    }
		    sftp.quit();
		    sftp.disconnect();
		    logger.info("sftp关闭连接！！！！！====" + sftp);
		}
	    } catch (JSchException e) {
		e.printStackTrace();
	    }
	}
	if (_sshSession != null && _sshSession.isConnected()) {
	    _sshSession.disconnect();
	}

	logger.info("sftp 已经关闭");
    }

    public boolean isConnected(ChannelSftp sftp) {
	return (sftp != null) && sftp.isConnected() && !sftp.isClosed()
		&& (_sshSession != null) && _sshSession.isConnected();
    }

    @SuppressWarnings("rawtypes")
    private Vector _list(String dir, ChannelSftp sftp) {
	try {
	    return sftp.ls(dir);
	} catch (Exception e) {
	    return null;
	}
    }

    @SuppressWarnings("rawtypes")
    private Vector _list(ChannelSftp sftp) {
	try {
	    return sftp.ls(sftp.pwd());
	} catch (Exception e) {
	    return null;
	}
    }

    @SuppressWarnings("rawtypes")
    public List list(String dir, ChannelSftp sftp) {
	try {
	    Vector ls = _list(dir, sftp);
	    return _buildFiles(ls);
	} catch (Exception e) {
	    return null;
	}
    }

    @SuppressWarnings(value = { "rawtypes", "unchecked" })
    private static List _buildFiles(Vector ls) throws Exception {
	if (ls != null && ls.size() >= 0) {
	    List list = new ArrayList();
	    for (int i = 0; i < ls.size(); i++) {
		LsEntry f = (LsEntry) ls.get(i);
		String nm = f.getFilename();

		if (nm.equals(".") || nm.equals(".."))
		    continue;
		SftpATTRS attr = f.getAttrs();
		Map m = new HashMap();
		if (attr.isDir()) {
		    m.put("dir", new Boolean(true));
		} else {
		    m.put("dir", new Boolean(false));
		}
		m.put("name", nm);
		list.add(m);
	    }
	    return list;
	}
	return null;
    }

    @SuppressWarnings("rawtypes")
    public List list(ChannelSftp sftp) {
	try {
	    Vector ls = _list(sftp);
	    return _buildFiles(ls);
	} catch (Exception e) {
	    return null;
	}
    }

    public boolean cd(String dirs, ChannelSftp sftp) throws Exception {
	try {
	    String path = dirs;
	    if (path.indexOf("\\") != -1) {
		path = dirs.replaceAll("\\", "/");
	    }
	    String pwd = sftp.pwd();
	    if (pwd.equals(path))
		return true;

	    sftp.cd(_root);

	    if (_root.equals(dirs))
		return true;

	    String[] paths = path.split("/");
	    for (int i = 0; i < paths.length; i++) {
		String dir = paths[i];
		if (isEmpty(dir))
		    continue;
		sftp.cd(dir);
	    }
	    return true;
	} catch (Exception e) {
	    throw e;
	}
    }

    @SuppressWarnings("rawtypes")
    public boolean isExist(String root, String fileOrDir, ChannelSftp sftp)
	    throws Exception {
	try {
	    boolean exist = false;
	    boolean cdflg = false;
	    String pwd = sftp.pwd();
	    if (pwd.indexOf(root) == -1) {
		cdflg = true;
		sftp.cd(root);
	    }
	    Vector ls = _list(root, sftp);
	    if (ls == null || ls.size() <= 0) {
		for (int i = 0; i < ls.size(); i++) {
		    LsEntry f = (LsEntry) ls.get(i);
		    String nm = f.getFilename();
		    if (nm.equals(fileOrDir)) {
			exist = true;
			break;
		    }
		}
	    }
	    if (cdflg) {
		sftp.cd("..");
	    }
	    return exist;
	} catch (Exception e) {
	    throw e;
	}
    }

    public boolean isEmpty(String s) {
	return s == null || "".equals(s.trim());
    }

    public boolean upload(String uploadFile, ChannelSftp sftp) throws Exception {
	java.io.InputStream is = null;
	try {
	    File file = new File(uploadFile);
	    file = file.getAbsoluteFile();
	    is = new FileInputStream(file);
	    sftp.put(is, file.getName());
	    return true;
	} catch (Exception e) {
	    throw e;
	} finally {
	    try {
		if (is != null) {
		    is.close();
		    is = null;
		}
	    } catch (Exception e2) {
		e2.printStackTrace();
	    }
	}
    }

    public String download(String dir, String downloadFile,
	    String localTempDir, String newFilename, ChannelSftp sftp)
	    throws SftpException, Exception {
	boolean isCd = cd(dir, sftp);
	String tempDir = "";
	if (isCd) {
	    if (!localTempDir.endsWith("/") && !localTempDir.endsWith("\\")) {
		localTempDir += "/";
	    }
	    File file = new File(localTempDir);
	    if (!file.exists()) {
		file.mkdir();
	    }
	    tempDir = localTempDir + newFilename;
	    sftp.get(downloadFile, tempDir);
	    logger.info("下载文件成功！！！！！    路径： " + tempDir);
	}
	sftp.disconnect();
	return tempDir;
    }

    @SuppressWarnings("rawtypes")
    public void downloadAll(String dir, String saveFile, ChannelSftp sftp) {
	try {
	    boolean flg = cd(dir, sftp);
	    if (flg) {
		List files = list(sftp);
		if (files != null && files.size() > 0) {
		    for (int i = 0; i < files.size(); i++) {
			Map item = (Map) files.get(i);
			String f = (String) item.get("name");
			boolean isDir = ((Boolean) item.get("dir"))
				.booleanValue();
			System.out.println("---本目录---" + f + " 是:" + isDir);
			if (isDir) {
			    List subfiles = list(f, sftp);
			    for (int j = 0; j < subfiles.size(); j++) {
				Map item1 = (Map) subfiles.get(j);
				boolean isDir1 = ((Boolean) item1.get("dir"))
					.booleanValue();
				if (isDir1)
				    continue;
				String ff = (String) item1.get("name");
				System.out.println("---2----" + ff);
				download(f, ff, saveFile, ff, sftp);
			    }
			}
			rm(f, sftp);
		    }
		}
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	} finally {
	    sftp.disconnect();
	}
    }

    @SuppressWarnings("rawtypes")
    public boolean mkDir(String filepath, ChannelSftp sftp) throws Exception {
	try {
	    String path = filepath;
	    if (path.indexOf("\\") != -1) {
		path = filepath.replaceAll("\\", "/");
	    }
	    String[] paths = path.split("/");
	    for (int i = 0; i < paths.length; i++) {
		String dir = paths[i];
		Vector ls = _list(dir, sftp);
		if (ls == null || ls.size() <= 0) {
		    sftp.mkdir(dir);
		}
		sftp.cd(dir);
	    }
	} catch (Exception e1) {
	    throw e1;
	}
	return true;
    }

    @SuppressWarnings("rawtypes")
    public boolean rm(String deleteFile, ChannelSftp sftp) throws Exception {
	try {
	    Vector ls = _list(sftp);
	    if (ls != null && ls.size() > 0) {
		for (int i = 0; i < ls.size(); i++) {
		    LsEntry f = (LsEntry) ls.get(i);
		    String nm = f.getFilename();
		    if (!nm.equals(deleteFile)) {
			continue;
		    }
		    SftpATTRS attr = f.getAttrs();
		    if (attr.isDir()) {
			if (rmdir(nm, sftp)) {
			    sftp.rmdir(nm);
			}
		    } else {
			sftp.rm(nm);
		    }
		}
	    }
	    return true;
	} catch (Exception e) {
	    throw e;
	}
    }

    @SuppressWarnings("rawtypes")
    private boolean rmdir(String dir, ChannelSftp sftp) throws Exception {
	try {
	    sftp.cd(dir);
	    Vector ls = _list(sftp);
	    if (ls != null && ls.size() > 0) {
		for (int i = 0; i < ls.size(); i++) {
		    LsEntry f = (LsEntry) ls.get(i);
		    String nm = f.getFilename();
		    if (nm.equals(".") || nm.equals(".."))
			continue;
		    SftpATTRS attr = f.getAttrs();
		    if (attr.isDir()) {
			if (rmdir(nm, sftp)) {
			    sftp.rmdir(nm);
			}
		    } else {
			sftp.rm(nm);
		    }
		}
	    }
	    sftp.cd("..");
	    return true;
	} catch (Exception e) {
	    throw e;
	}
    }

    public void setIp(String ip) {
	SFTP.ip = ip;
    }

    public void setPort(String port) {
	SFTP.port = port;
    }

    public void setUserName(String userName) {
	SFTP.userName = userName;
    }

    public void setPassword(String password) {
	SFTP.password = password;
    }

}
