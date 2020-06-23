package com.supermap.wisdombusiness.workflow.service.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpATTRS;
import com.jcraft.jsch.SftpException;
import com.supermap.realestate.registration.util.ConfigHelper;
import com.supermap.realestate.registration.util.GetProperties;
import com.supermap.wisdombusiness.utility.StringHelper;

@Scope("prototype")
public class SFTPEx {

	public static Logger logger = Logger.getLogger(SFTPEx.class);
	static String _root = "/";

	public static boolean runner = false;

	public static List<SFTPUser> userList = null;
	public static int interval = 1;// 执行间隔
	public static int Accountcount = 0;// 账户个数

	public void timerrunner() {
		String countconfiguer = ConfigHelper.getNameByValue("sftpcount");
		String materialtype = ConfigHelper.getNameByValue("materialtype");
		String mString=GetProperties.getConstValueByKey("materialtype");
		if(mString!=null&&!mString.equals("")){
			materialtype=mString;
		}
		if (StringHelper.isNotNull(countconfiguer)
				&& !countconfiguer.equals("0")
				&& StringHelper.isNotNull(materialtype)
				&& materialtype.equals("2")) {
			if (!runner) {
				if (userList == null) {
					startSFTP();
				}
				runner = true;
				Runnable runnable = new Runnable() {
					public void run() {
						// task to run goes here
						if (userList != null) {
							int cc = 0;
							for (SFTPUser user : userList) {
								if (!user.isLocked() && !user.isConnected()) {
									try {
										user = connect(user);

									} catch (JSchException e) {

										e.printStackTrace();
									} catch (SftpException e) {
										e.printStackTrace();
									}
								} else {
									user.setLinkTime(user.getLinkTime()
											+ interval);
									if (user.getLinkTime() > 1800&&!user.isLocked() ) {
										user.setLinkTime(0);
										user.setUseCount(0);
										calcelSFTP(user);
										try {
											connect(user);
										} catch (JSchException e) {
											e.printStackTrace();
										} catch (SftpException e) {
											e.printStackTrace();
										}
										System.out.println("发现未用账户，重新连接了"
												+ user.getUsername());
									}

								}

							}
						}
					}
				};
				ScheduledExecutorService service = Executors
						.newSingleThreadScheduledExecutor();
				// 第二个参数为首次执行的延时时间，第三个参数为定时执行的间隔时间
				service.scheduleAtFixedRate(runnable, 2, interval,
						TimeUnit.SECONDS);
			}
		}

	} // end of main

	public static void startSFTP() {
		if (!runner) {
			String countconfiguer = ConfigHelper.getNameByValue("sftpcount");
			String materialtype = ConfigHelper.getNameByValue("materialtype");
			String mString=GetProperties.getConstValueByKey("materialtype");
			if(mString!=null&&!mString.equals("")){
				materialtype=mString;
			}
			if (StringHelper.isNotNull(countconfiguer)
					&& StringHelper.isNotNull(materialtype)
					&& materialtype.equals("2")) {
				Accountcount = Integer.parseInt(countconfiguer);
				String count=GetProperties.getConstValueByKey("countconfiguer");
				if(count!=null&&!count.equals("")){
					Accountcount = Integer.parseInt(count);;
				}
				String userName = ConfigHelper.getNameByValue("filesftpuser");
				userList = new ArrayList<SFTPUser>();
				for (int i = 0; i < Accountcount; i++) {
					SFTPUser user = new SFTPUser();
					user.setUsername(userName + i);
					user.setConnected(false);
					user.setLocked(false);
					userList.add(user);
				}
			}
		}

	}

	/**
	 * 获取链接的资源
	 * 
	 * @return
	 */
	public static SFTPUser getSFTP() {
		SFTPUser sftp = null;
		int cc = 0;
		for (SFTPUser user : userList) {
			cc++;
			if (!user.isLocked() && user.isConnected()) {
				user.setLocked(true);
				sftp = user;
				user.setUseCount(user.getUseCount() + 1);
				cc = 0;
				System.out.println("取用的用户" + user.getUsername());
				break;
			}
		}
		if (sftp == null) {
			try {
				Thread.sleep(1000);
				return getSFTP();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return sftp;
	}

	public static void calcelSFTP(SFTPUser users) {
		for (SFTPUser user : userList) {
			if (user.getUsername().equals(users.getUsername())) {
				System.out.println("准备断开用户" + user.getUsername());
				disconnect(users);
				user.setConnected(isConnected(users));
				user.setLocked(false);
				user.setLinkTime(0);
				break;

			}

		}
	}

	public static SFTPUser connect(SFTPUser user) throws JSchException,
			SftpException {
		/*
		 * String host = ConfigHelper.getNameByValue("filesftpip");
		 * 
		 * String password = ConfigHelper.getNameByValue("filesftppwd"); int
		 * port = Integer
		 * .parseInt(ConfigHelper.getNameByValue("filesftpport"));
		 */
		// synchronized (host) {
		String host = user.getHost();

		String password = user.getPassword();
		int port = user.getPort();
		JSch jsch = new JSch();
		com.jcraft.jsch.Session _sshSession;
		_sshSession = jsch.getSession(user.getUsername(), host, port);
		// System.out.println("Session created.");

		_sshSession.setPassword(password);
		Properties sshConfig = new Properties();
		sshConfig.put("StrictHostKeyChecking", "no");
		_sshSession.setConfig(sshConfig);
		// Security.addProvider(new BouncyCastleProvider());
		_sshSession.connect();
		user.setSshSession(_sshSession);
		Channel channel = _sshSession.openChannel("sftp");
		channel.connect();
		ChannelSftp sftp = null;
		sftp = (ChannelSftp) channel;
		// //logger.info("链接到SFTP成功 ：" + host + " 用户：" + userName);
		// }
		// _root += root;
		sftp.cd("/");
		sftp.setFilenameEncoding("UTF-8");
		user.setSftp(sftp);
		user.setConnected(true);
		System.out.println("链接了用户" + user.getUsername());
		return user;
	}

	public static void disconnect(SFTPUser use) {
		try {
			ChannelSftp sftp = use.getSftp();
			if (sftp != null) {
				if (sftp.isConnected()) {

					sftp.getSession().disconnect();
					sftp.disconnect();
					sftp.quit();
					sftp.exit();
					sftp = null;
					// logger.info("sftp关闭连接！！！！！====" + sftp);
				} else if (sftp.isClosed()) {
				}

			}
			if (use.getSshSession() != null
					&& use.getSshSession().isConnected()) {
				use.getSshSession().disconnect();

			}
			System.out.println("断开用户" + use.getUsername());
			// logger.info("sftp 已经关闭");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static boolean isConnected(SFTPUser use) {
		return (use.getSftp() != null) && use.getSftp().isConnected()
				&& !use.getSftp().isClosed() && (use.getSshSession() != null)
				&& use.getSshSession().isConnected();
	}

	private static Vector _list(String dir, ChannelSftp sftp) {
		try {
			return sftp.ls(dir);
		} catch (Exception e) {
			return null;
		}
	}

	private static Vector _list(ChannelSftp sftp) {
		try {
			return sftp.ls(sftp.pwd());
		} catch (Exception e) {
			return null;
		}
	}

	public static List list(String dir, ChannelSftp sftp) {
		try {
			Vector ls = _list(dir, sftp);
			return _buildFiles(ls);
		} catch (Exception e) {
			return null;
		}
	}

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

	public static List list(ChannelSftp sftp) {
		try {
			Vector ls = _list(sftp);
			return _buildFiles(ls);
		} catch (Exception e) {
			return null;
		}
	}

	public static boolean cd(String dirs, ChannelSftp sftp) throws Exception {
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

	public static boolean isExist(String root, String fileOrDir,
			ChannelSftp sftp) throws Exception {
		try {
			boolean exist = false;
			boolean cdflg = false;
			String pwd = sftp.pwd();
			if (pwd.indexOf(root) == -1) {
				cdflg = true;
				cd(root, sftp);
			}
			Vector ls = _list(root, sftp);
			if (ls == null || ls.size() > 0) {
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

	public static boolean isEmpty(String s) {
		return s == null || "".equals(s.trim());
	}

	public static boolean upload(String uploadFile, ChannelSftp sftp)
			throws Exception {
		java.io.InputStream is = null;
		File file = new File(uploadFile);
		if (file.exists()) {
			file = file.getAbsoluteFile();
			is = new FileInputStream(file);
			sftp.put(is, file.getName());
		}
		return true;

	}

	public static boolean uploadByFile(InputStream is, String filename,
			ChannelSftp sftp) throws Exception {
		sftp.put(is, filename);

		return true;

	}

	public static String download(String dir, String downloadFile,
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
			// logger.info("下载文件成功！！！！！    路径： " + tempDir);
		}

		return tempDir;
	}

	// 读取文件，用于查询共享库 入库操作
	public static InputStream readfile(String dir, String downloadFile,
			ChannelSftp sftp) {
		try {
			boolean isCd = cd(dir, sftp);

			if (isCd) {
				return sftp.get(dir + downloadFile);
			} else {
				return null;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void downloadAll(String dir, String saveFile, ChannelSftp sftp) {
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
		}
	}

	public static boolean mkDir(String filepath, ChannelSftp sftp)
			throws Exception {
		String path = filepath;
		if (path.indexOf("\\") != -1) {
			path = filepath.replaceAll("\\", "/");
		}
		String[] paths = path.split("/");
		if (paths != null) {
			for (int i = 0; i < paths.length; i++) {
				String dir = paths[i];
				Vector ls = _list(dir, sftp);
				if (ls == null || ls.size() <= 0) {
					sftp.mkdir(dir);
				}
				sftp.cd(dir);
			}
		}
		return true;
	}

	public static boolean rm(String deleteFile, ChannelSftp sftp)
			throws Exception {
		try {
			Vector ls = _list(sftp);
			if (ls != null && ls.size() > 0) {
				for (int i = 0; i < ls.size(); i++) {
					LsEntry f = (LsEntry) ls.get(i);
					if (f != null) {
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
			}
			return true;
		} catch (Exception e) {
			throw e;
		}
	}

	private static boolean rmdir(String dir, ChannelSftp sftp) throws Exception {
		try {
			sftp.cd(dir);
			Vector ls = _list(sftp);
			if (ls != null && ls.size() > 0) {
				for (int i = 0; i < ls.size(); i++) {
					LsEntry f = (LsEntry) ls.get(i);
					if (f != null) {
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
			}
			sftp.cd("..");
			return true;
		} catch (Exception e) {
			throw e;
		}
	}

}
