package com.supermap.realestate.registration.service.Sender;

import java.io.Serializable;

public class ShareMessage implements Serializable {
    private static final long serialVersionUID = -658250125732806493L; // 序列化

    private String staff;

    private String receiver;

    private String receivercode;

    private String bh;

    private String sftpFilePath;

    private String sftpFileName;

    private String sender;

    private String sendercode;

    private Boolean bTansmited = false;

    private String description;

    private String bdcdjXmbh;
    
    private String logid;
    
    private String project_id;

    private String XMLType;//300：房产数据，1：正常交易数据， 2:楼盘表， 3:房屋变更
    
    private String acinst_name;//流程环节名称
    public String getAcinst_name() {
        return acinst_name;
    }

    public void setAcinst_name(String acinst_name) {
        this.acinst_name = acinst_name;
    }
    
    public String getProject_id() {
        return project_id;
    }

    public void setProject_id(String project_id) {
        this.project_id = project_id;
    }
    
    /**
     * sftp上文件的路径
     */
    public String getSftpFilePath() {
	return sftpFilePath;
    }

    /**
     * sftp上文件的路径
     */
    public void setSftpFilePath(String sftpFilePath) {
	this.sftpFilePath = sftpFilePath;
    }

    /**
     * sftp上文件的名称
     */
    public String getSftpFileName() {
	return sftpFileName;
    }

    /**
     * sftp上文件的名称
     */
    public void setSftpFileName(String sftpFileName) {
	this.sftpFileName = sftpFileName;
    }

    /**
     * 关于该信息的描述
     */
    public String getDescription() {
	return description;
    }

    /**
     * 关于该信息的描述
     */
    public void setDescription(String description) {
	this.description = description;
    }

    /**
     * 业务编号或者合同编号或者其他编号
     */
    public String getBh() {
	return bh;
    }

    /**
     * 业务编号或者合同编号或者其他编号
     */
    public void setBh(String bh) {
	this.bh = bh;
    }

    /**
     * 信息接收者代码/接收部门代码
     */
    public String getReceivercode() {
	return receivercode;
    }

    /**
     * 信息接收者代码/接收部门代码
     */
    public void setReceivercode(String receivercode) {
	this.receivercode = receivercode;
    }

    /**
     * 信息发送者/信息发送部门代码
     */
    public String getSendercode() {
	return sendercode;
    }

    /**
     * 信息发送者/信息发送部门代码
     */
    public void setSendercode(String sendercode) {
	this.sendercode = sendercode;
    }

    /**
     * 信息发送者/信息发送部门
     */
    public String getSender() {
	return sender;
    }

    /**
     * 信息发送者/信息发送部门
     */
    public void setSender(String sender) {
	this.sender = sender;
    }

    /**
     * 消息是否被转发
     */
    public Boolean getbTansmited() {
	return bTansmited;
    }

    /**
     * 消息是否被转发
     */
    public void setbTansmited(Boolean bTansmited) {
	this.bTansmited = bTansmited;
    }

    public ShareMessage() {
    }

    public ShareMessage(String receiver, String description, String sftpFileName) {
	this.receiver = receiver;
	this.description = description;
	this.sftpFileName = sftpFileName;
    }

    /**
     * 信息接收者/接收部门名称
     */
    public String getReceiver() {
	return receiver;
    }

    /**
     * 信息接收者/接收部门名称
     */
    public void setReceiver(String receiver) {
	this.receiver = receiver;
    }

    /**
     * 操作用户名称
     */
    public String getStaff() {
	return staff;
    }

    /**
     * 操作用户名称
     */
    public void setStaff(String staff) {
	this.staff = staff;
    }

    /**
     * 不动产登记的项目编号
     */
    public String getBdcdjXmbh() {
	return bdcdjXmbh;
    }

    /**
     * 不动产登记的项目编号
     */
    public void setBdcdjXmbh(String bdcdjXmbh) {
	this.bdcdjXmbh = bdcdjXmbh;
    }
    /**
     * 日志表id
     */
    public String getLogid() {
	return logid;
    }

    /**
     * 日志表id
     */
    public void setLogid(String logid) {
	this.logid = logid;
    }

	public String getXMLType() {
		return XMLType;
	}

	public void setXMLType(String xMLType) {
		this.XMLType = xMLType;
	}
}
