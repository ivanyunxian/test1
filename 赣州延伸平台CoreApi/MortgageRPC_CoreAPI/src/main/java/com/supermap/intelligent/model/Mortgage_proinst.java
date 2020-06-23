package com.supermap.intelligent.model;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 *  不动产权证表
 * @author luml 2019-08-20
 */
@Entity
@Table(name = "WFI_PROINST",schema = "BDC_MRPC")
public class Mortgage_proinst implements Serializable {
    private static final long serialVersionUID = 1L;

    /**流程实例ID*/
    private String proinstId;
    /**流程编号*/
    private String prodefId;
    /**员工编号*/
    private String userId;
    /**区划代码*/
    private String divisionCode;
    /**区划名称*/
    private String divisionName;
    /**业务类型（0:个人，1:企业）*/
    private String ywlx;
    /**登记类型 登记类型字典*/
    private String djlx;
    /**权利类型 权利类型字典*/
    private String qllx;
    /**是否合并证书 证书个数字典*/
    private String sfhbzs;
    /**项目类型 用于区分不同的需求，12：通用的，11：鹰潭*/
    private String xmlx;
    /**审核状态 -1：拟申请未提交到前置机，0：未审核，10：权籍调查审核通过，11：权籍调查审核驳回，20：项目受理审核通过，21：项目受理审核驳回*/
    private Integer shzt;
    /**流水号 外网流水号*/
    private String prolsh;
    /**流水号 内网创建项目生成的流水号*/
    private String lsh;
    /**内网库流程ID 受理项目对应到内网库的流程ID*/
    private String wfProdefid;
    /**内网流程名称 对应到内网的流程名称*/
    private String wfProdefname;
    /**流程名称*/
    private String prodefName;
    /**项目名称*/
    private String projectName;
    /**实例办理方式*/
    private Integer transationType;
    /**流程编码*/
    private String proinstCode;
    /**起始时间*/
    private Date proinstStart;
    /**结束时间*/
    private Date proinstEnd;
    /**流程实例状态*/
    private Integer proinstStatus;
    /**创建时间*/
    private Date creatDate;
    /**受理人员*/
    private String acceptor;
    /**员工地区ID*/
    private String staffDistid;
    /**流程实例类型*/
    private Integer instanceType;
    /**发送消息状态 0：不需要发送消息，1：需要推送消息*/
    private Integer sendMsg;
    /**项目备注*/
    private String remarks;
    /**用户部门id*/
    private String departid;

    /**登簿时间*/
    private Date dbsj;

    /**业务来源（0：抵押平台，1：金融机构）*/
    private String ywly;
    /**机构流水号*/
    private String orgLsh;
    /**回调状态*/
    private String hdzt;

    @Id
    @Column(name = "PROINST_ID")
    public String getProinstId() {
        return proinstId;
    }

    public void setProinstId(String proinstId) {
        this.proinstId = proinstId;
    }

    @Column(name = "PRODEF_ID")
    public String getProdefId() {
        return prodefId;
    }

    public void setProdefId(String prodefId) {
        this.prodefId = prodefId;
    }

    @Column(name = "USER_ID")
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Column(name = "DIVISION_CODE")
    public String getDivisionCode() {
        return divisionCode;
    }

    public void setDivisionCode(String divisionCode) {
        this.divisionCode = divisionCode;
    }

    @Column(name = "DIVISION_NAME")
    public String getDivisionName() {
        return divisionName;
    }

    public void setDivisionName(String divisionName) {
        this.divisionName = divisionName;
    }

    @Column(name = "YWLX")
    public String getYwlx() {
        return ywlx;
    }

    public void setYwlx(String ywlx) {
        this.ywlx = ywlx;
    }

    @Column(name = "DJLX")
    public String getDjlx() {
        return djlx;
    }

    public void setDjlx(String djlx) {
        this.djlx = djlx;
    }

    @Column(name = "QLLX")
    public String getQllx() {
        return qllx;
    }

    public void setQllx(String qllx) {
        this.qllx = qllx;
    }

    @Column(name = "SFHBZS")
    public String getSfhbzs() {
        return sfhbzs;
    }

    public void setSfhbzs(String sfhbzs) {
        this.sfhbzs = sfhbzs;
    }

    @Column(name = "XMLX")
    public String getXmlx() {
        return xmlx;
    }

    public void setXmlx(String xmlx) {
        this.xmlx = xmlx;
    }

    @Column(name = "SHZT")
    public Integer getShzt() {
        return shzt;
    }

    public void setShzt(Integer shzt) {
        this.shzt = shzt;
    }

    @Column(name = "PROLSH")
    public String getProlsh() {
        return prolsh;
    }

    public void setProlsh(String prolsh) {
        this.prolsh = prolsh;
    }

    @Column(name = "LSH")
    public String getLsh() {
        return lsh;
    }

    public void setLsh(String lsh) {
        this.lsh = lsh;
    }

    @Column(name = "WF_PRODEFID")
    public String getWfProdefid() {
        return wfProdefid;
    }

    public void setWfProdefid(String wfProdefid) {
        this.wfProdefid = wfProdefid;
    }

    @Column(name = "WF_PRODEFNAME")
    public String getWfProdefname() {
        return wfProdefname;
    }

    public void setWfProdefname(String wfProdefname) {
        this.wfProdefname = wfProdefname;
    }

    @Column(name = "PRODEF_NAME")
    public String getProdefName() {
        return prodefName;
    }

    public void setProdefName(String prodefName) {
        this.prodefName = prodefName;
    }

    @Column(name = "PROJECT_NAME")
    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    @Column(name = "TRANSATION_TYPE")
    public Integer getTransationType() {
        return transationType;
    }

    public void setTransationType(Integer transationType) {
        this.transationType = transationType;
    }

    @Column(name = "PROINST_CODE")
    public String getProinstCode() {
        return proinstCode;
    }

    public void setProinstCode(String proinstCode) {
        this.proinstCode = proinstCode;
    }

    @Column(name = "PROINST_START")
    public Date getProinstStart() {
        return proinstStart;
    }

    public void setProinstStart(Date proinstStart) {
        this.proinstStart = proinstStart;
    }

    @Column(name = "PROINST_END")
    public Date getProinstEnd() {
        return proinstEnd;
    }

    public void setProinstEnd(Date proinstEnd) {
        this.proinstEnd = proinstEnd;
    }

    @Column(name = "PROINST_STATUS")
    public Integer getProinstStatus() {
        return proinstStatus;
    }

    public void setProinstStatus(Integer proinstStatus) {
        this.proinstStatus = proinstStatus;
    }

    @Column(name = "CREAT_DATE")
    public Date getCreatDate() {
        return creatDate;
    }

    public void setCreatDate(Date creatDate) {
        this.creatDate = creatDate;
    }

    @Column(name = "ACCEPTOR")
    public String getAcceptor() {
        return acceptor;
    }

    public void setAcceptor(String acceptor) {
        this.acceptor = acceptor;
    }

    @Column(name = "STAFF_DISTID")
    public String getStaffDistid() {
        return staffDistid;
    }

    public void setStaffDistid(String staffDistid) {
        this.staffDistid = staffDistid;
    }

    @Column(name = "INSTANCE_TYPE")
    public Integer getInstanceType() {
        return instanceType;
    }

    public void setInstanceType(Integer instanceType) {
        this.instanceType = instanceType;
    }

    @Column(name = "SEND_MSG")
    public Integer getSendMsg() {
        return sendMsg;
    }

    public void setSendMsg(Integer sendMsg) {
        this.sendMsg = sendMsg;
    }

    @Column(name = "REMARKS")
    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    @Column(name = "DEPARTID")
    public String getDepartid() {
        return departid;
    }

    public void setDepartid(String departid) {
        this.departid = departid;
    }

    @Column(name = "DBSJ")
    public Date getDbsj() {
        return dbsj;
    }

    public void setDbsj(Date dbsj) {
        this.dbsj = dbsj;
    }

    @Column(name = "YWLY")
    public String getYwly() {
        return ywly;
    }

    public void setYwly(String ywly) {
        this.ywly = ywly;
    }

    @Column(name = "ORG_LSH")
    public String getOrgLsh() {
        return orgLsh;
    }

    public void setOrgLsh(String orgLsh) {
        this.orgLsh = orgLsh;
    }

    @Column(name = "HDZT")
    public String getHdzt() {
        return hdzt;
    }

    public void setHdzt(String hdzt) {
        this.hdzt = hdzt;
    }
}