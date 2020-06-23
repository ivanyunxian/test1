package com.supermap.wisdombusiness.workflow.service.common;

import com.jcraft.jsch.ChannelSftp;
import com.supermap.realestate.registration.util.ConfigHelper;

public class SFTPUser {
	
	private String username;
	private boolean locked;
	private boolean connected;
	private ChannelSftp sftp;
	private int linkTime=0;
	private int useCount=0;
	private com.jcraft.jsch.Session sshSession;
	private String host=ConfigHelper.getNameByValue("filesftpip");
	private int port=Integer.parseInt(ConfigHelper.getNameByValue("filesftpport"));
	private String password=ConfigHelper.getNameByValue("filesftppwd");
	public boolean isLocked() {
		return locked;
	}
	public void setLocked(boolean locked) {
		this.locked = locked;
	}
	public ChannelSftp getSftp() {
		return sftp;
	}
	public void setSftp(ChannelSftp sftp) {
		this.sftp = sftp;
	}
	public boolean isConnected() {
		return connected;
	}
	public void setConnected(boolean connected) {
		this.connected = connected;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public int getLinkTime() {
		return linkTime;
	}
	public void setLinkTime(int linkTime) {
		this.linkTime = linkTime;
	}
	public int getUseCount() {
		return useCount;
	}
	public void setUseCount(int useCount) {
		this.useCount = useCount;
	}
	public String getHost() {
		return host;
	}
	public int getPort() {
		return port;
	}
	public String getPassword() {
		return password;
	}
	public com.jcraft.jsch.Session getSshSession() {
		return sshSession;
	}
	public void setSshSession(com.jcraft.jsch.Session sshSession) {
		this.sshSession = sshSession;
	}
	

}
