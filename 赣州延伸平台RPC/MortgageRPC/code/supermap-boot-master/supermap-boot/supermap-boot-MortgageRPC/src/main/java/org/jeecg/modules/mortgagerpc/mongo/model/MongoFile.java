package org.jeecg.modules.mortgagerpc.mongo.model;


import org.bson.types.Binary;

import java.util.Date;
import java.util.UUID;

/**
 * mongo文件格式实体类
 */
public class MongoFile {
    private String id;   //主键.默认UUID
    private String prolsh;//业务流水号
    private String  bdcdyh;//不动产单元号
    private String bdcdyid;//不动产单元id
    private String wjmlid;//文件目录id
    private String wjly;  //文件来源
    private String jmfs;   //加密方式 默认0,加密
    private Binary fileContent;  //文件内容
    private String  fileName  ;//文件名
    private String  fileType;  //文件类型
    private String  uploadUserName  ; //上传用户名
    private String  uploadSystem ; //上传系统名称
    private Date    createDate ; //创建时间
    private Date  updateDate ; //修改时间
    private String xzqh;   //行政区

    public MongoFile(){
        this.id=UUID.randomUUID().toString().replaceAll("-","");

    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProlsh() {
        return prolsh;
    }

    public void setProlsh(String prolsh) {
        this.prolsh = prolsh;
    }

    public String getBdcdyh() {
        return bdcdyh;
    }

    public void setBdcdyh(String bdcdyh) {
        this.bdcdyh = bdcdyh;
    }

    public String getBdcdyid() {
        return bdcdyid;
    }

    public void setBdcdyid(String bdcdyid) {
        this.bdcdyid = bdcdyid;
    }

    public String getWjmlid() {
        return wjmlid;
    }

    public void setWjmlid(String wjmlid) {
        this.wjmlid = wjmlid;
    }

    public String getWjly() {
        return wjly;
    }

    public void setWjly(String wjly) {
        this.wjly = wjly;
    }

    public String getJmfs() {
        return jmfs;
    }

    public void setJmfs(String jmfs) {
        this.jmfs = jmfs;
    }

    public Binary getFileContent() {
        return fileContent;
    }

    public void setFileContent(Binary fileContent) {
        this.fileContent = fileContent;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getUploadUserName() {
        return uploadUserName;
    }

    public void setUploadUserName(String uploadUserName) {
        this.uploadUserName = uploadUserName;
    }

    public String getUploadSystem() {
        return uploadSystem;
    }

    public void setUploadSystem(String uploadSystem) {
        this.uploadSystem = uploadSystem;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getXzqh() {
        return xzqh;
    }

    public void setXzqh(String xzqh) {
        this.xzqh = xzqh;
    }







}
