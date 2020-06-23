package com.supermap.intelligent.model;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import java.io.Serializable;
import java.util.Date;

/**
 *  rabbitmq日志表
 * @author luml 2019-08-05
 */
@Entity
@Table(name = "LOG_rabbitMQ", schema = "LOG")
public class LOG_RABBITMQ implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String id;
    /**
     * 请求编码
     */
    private String requestcode;

    /**
     * 接收时间
     */
    private Date createtime;

    /**
     * 行政区划
     */
    private String xzqh;

    /**
     * 从队列获取的消息
     */
    private String receivedmsg;

    /**
     * exception
     */
    private String exception;

    /**
     * 外网流水号
     */
    private String wlsh;

    /**
     * 内网流水号
     */
    private String ywlsh;

    /**
     * 项目编号
     */
    private String xmbh;

    /**
     * 活动实例id
     */
    private String actinstid;

    @Column(name = "requestcode")
    public String getRequestcode() {
        return requestcode;
    }

    public void setRequestcode(String requestcode) {
        this.requestcode = requestcode;
    }
    @Column(name = "xzqh")
    public String getXzqh() {
        return xzqh;
    }

    public void setXzqh(String xzqh) {
        this.xzqh = xzqh;
    }
    @Column(name = "receivedmsg")
    public String getReceivedmsg() {
        return receivedmsg;
    }

    public void setReceivedmsg(String receivedmsg) {
        this.receivedmsg = receivedmsg;
    }
    @Column(name = "exception")
    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }
    @Column(name = "wlsh")
    public String getWlsh() {
        return wlsh;
    }

    public void setWlsh(String wlsh) {
        this.wlsh = wlsh;
    }
    @Column(name = "ywlsh")
    public String getYwlsh() {
        return ywlsh;
    }

    public void setYwlsh(String ywlsh) {
        this.ywlsh = ywlsh;
    }
    @Column(name = "xmbh")
    public String getXmbh() {
        return xmbh;
    }

    public void setXmbh(String xmbh) {
        this.xmbh = xmbh;
    }
    @Column(name = "actinstid")
    public String getActinstid() {
        return actinstid;
    }

    public void setActinstid(String actinstid) {
        this.actinstid = actinstid;
    }

    public LOG_RABBITMQ() {
    }
    @Column(name = "createtime")
	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	@Id
    @Column(name = "id")
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
     

}