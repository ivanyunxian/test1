/**   
 * 请描述这个文件
 * @Title: EntityTool.java 
 * @Package com.supermap.realestate.registration.util 
 * @author liushufeng 
 * @date 2015年7月12日 下午5:20:33 
 * @version V1.0   
 */

package com.supermap.realestate.registration.tools;
 
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jcraft.jsch.ChannelSftp;
import com.supermap.realestate.registration.model.BDCS_DJDY_GZ;
import com.supermap.realestate.registration.model.BDCS_LOG;
import com.supermap.realestate.registration.model.BDCS_XMXX;
import com.supermap.realestate.registration.model.BDCS_ZS_GZ;
import com.supermap.realestate.registration.model.interfaces.Building;
import com.supermap.realestate.registration.model.interfaces.House;
import com.supermap.realestate.registration.model.interfaces.OwnerLand;
import com.supermap.realestate.registration.model.interfaces.RealUnit;
import com.supermap.realestate.registration.model.interfaces.Rights;
import com.supermap.realestate.registration.model.interfaces.RightsHolder;
import com.supermap.realestate.registration.model.interfaces.SubRights;
import com.supermap.realestate.registration.model.interfaces.UseLand;
import com.supermap.realestate.registration.model.xmlExportmodel.BGDataExport;
import com.supermap.realestate.registration.model.xmlExportmodel.BGMessageExport;
import com.supermap.realestate.registration.model.xmlExportmodel.DataExport;
import com.supermap.realestate.registration.model.xmlExportmodel.HTNRExport;
import com.supermap.realestate.registration.model.xmlExportmodel.HeadExport;
import com.supermap.realestate.registration.model.xmlExportmodel.MessageExport;
import com.supermap.realestate.registration.model.xmlExportmodel.QLRExport;
import com.supermap.realestate.registration.model.xmlExportmodel.SFRExport;
import com.supermap.realestate.registration.model.xmlExportmodel.dyExport.BGBDCDYExport;
import com.supermap.realestate.registration.model.xmlExportmodel.dyExport.CExport;
import com.supermap.realestate.registration.model.xmlExportmodel.dyExport.DZDZWExport;
import com.supermap.realestate.registration.model.xmlExportmodel.dyExport.GZWExport;
import com.supermap.realestate.registration.model.xmlExportmodel.dyExport.HExport;
import com.supermap.realestate.registration.model.xmlExportmodel.dyExport.LJZExport;
import com.supermap.realestate.registration.model.xmlExportmodel.dyExport.MZDZWExport;
import com.supermap.realestate.registration.model.xmlExportmodel.dyExport.SHYQZDExport;
import com.supermap.realestate.registration.model.xmlExportmodel.dyExport.SYQZDExport;
import com.supermap.realestate.registration.model.xmlExportmodel.dyExport.XZDZWExport;
import com.supermap.realestate.registration.model.xmlExportmodel.dyExport.YHYDZBExport;
import com.supermap.realestate.registration.model.xmlExportmodel.dyExport.YHZKExport;
import com.supermap.realestate.registration.model.xmlExportmodel.dyExport.ZDBHQKExport;
import com.supermap.realestate.registration.model.xmlExportmodel.dyExport.ZHBHQKExport;
import com.supermap.realestate.registration.model.xmlExportmodel.dyExport.ZHExport;
import com.supermap.realestate.registration.model.xmlExportmodel.dyExport.ZRZExport;
import com.supermap.realestate.registration.model.xmlExportmodel.qlExport.CFDJExport;
import com.supermap.realestate.registration.model.xmlExportmodel.qlExport.DYAQExport;
import com.supermap.realestate.registration.model.xmlExportmodel.qlExport.DYIQExport;
import com.supermap.realestate.registration.model.xmlExportmodel.qlExport.FDCQ1Export;
import com.supermap.realestate.registration.model.xmlExportmodel.qlExport.FDCQ2Export;
import com.supermap.realestate.registration.model.xmlExportmodel.qlExport.FDCQ3Export;
import com.supermap.realestate.registration.model.xmlExportmodel.qlExport.GJZWSYQExport;
import com.supermap.realestate.registration.model.xmlExportmodel.qlExport.HYSYQExport;
import com.supermap.realestate.registration.model.xmlExportmodel.qlExport.JSYDSYQExport;
import com.supermap.realestate.registration.model.xmlExportmodel.qlExport.LQExport;
import com.supermap.realestate.registration.model.xmlExportmodel.qlExport.NYDSYQExport;
import com.supermap.realestate.registration.model.xmlExportmodel.qlExport.QTXGQLExport;
import com.supermap.realestate.registration.model.xmlExportmodel.qlExport.TDSYQExport;
import com.supermap.realestate.registration.model.xmlExportmodel.qlExport.YGDJExport;
import com.supermap.realestate.registration.model.xmlExportmodel.qlExport.YYDJExport;
import com.supermap.realestate.registration.service.Sender.MQSender;
import com.supermap.realestate.registration.service.Sender.SFTP;
import com.supermap.realestate.registration.service.Sender.ShareMessage;
import com.supermap.realestate.registration.util.ConstValue;
import com.supermap.realestate.registration.util.ConstValue.BDCDYLX;
import com.supermap.realestate.registration.util.ConstValue.DJDYLY;
import com.supermap.realestate.registration.util.ConstValue.DJLX;
import com.supermap.realestate.registration.util.ConstValue.QLLX;
import com.supermap.realestate.registration.util.ConstValue.SFDB;
import com.supermap.realestate.registration.util.ConfigHelper;
import com.supermap.realestate.registration.util.GetProperties;
import com.supermap.realestate.registration.util.Global;
import com.supermap.realestate.registration.util.ProjectHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.realestate.registration.util.SystemConfig;
import com.supermap.realestate.registration.util.ZipCompressorByAnt;
import com.supermap.wisdombusiness.workflow.dao.CommonDao;
import com.thoughtworks.xstream.XStream;

/**
 * 
 * 共享文件操作类
 * 
 * @ClassName: ShareMsgTools
 * @author 俞学斌
 * @date 2015年8月17日 下午3:46:33
 */
@Component("ShareMsgTools")
@Scope("prototype")
public class ShareMsgTools {

    @Autowired
    private MQSender mqSender;

//    private BDCS_XMXX xmxx = null;

    
    @Autowired
    private CommonDao commonDao;
    private String staffName = "";
    
    static{
	//定时执行，重发
	/*Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                System.out.println("-------设定要指定任务--------");
            }
        }, 10*1000, 1000);*/
    }

    /**
     * 获取共享文件表头对象
     * 
     * @Author：俞学斌
     * @param bljc
     *            业务办理进程
     */
    private HeadExport CreateSendMsgHead(BDCS_XMXX xmxx, String bljc, String bdcdyh,
	    String bdcdyid, String relationID) {
	HeadExport head = new HeadExport();
	String projectId = xmxx.getPROJECT_ID();// 业务号
	String recSubtype = projectId.split("-")[2];// 从流程编号中获取登记细类
	head.setBizMsgID(projectId);// 设定流程号
	head.setBusinessID("");// 设定合同编号
	head.setProjectName(xmxx.getXMMC());// 设定项目名称
	head.setASID(ConfigHelper.getNameByValue("XZQHDM") + "100");// 设定共享标准？这个是这么弄的吗，待确定????
	head.setAreaCode(ConfigHelper.getNameByValue("XZQHDM"));// 设定行政区代码
	head.setRecType(xmxx.getDJLX());// 设定登记类型
	head.setRecSubType(recSubtype);// 设定登记细类
	head.setRecSubTypeName("");// 登记细类名称????
	head.setRightType(xmxx.getQLLX());// 设定权利类型
	head.setRightNature("");// 设定权利性质????
	head.setCreateDate(StringHelper.FormatByDatetime(xmxx.getSLSJ()));// 设定项目创建时间
	head.setRegOrgID(ConfigHelper.getNameByValue("XZQHMC") + ConfigHelper.getNameByValue("DJJGMC"));// 设定登记机构
	head.setBusinessProcess(bljc);// 设定业务办理进程
	if (bdcdyh != null && bdcdyh.length() >= 19) {
	    head.setParcelID(bdcdyh.substring(0, 19));// 设定宗地、宗海代码
	}
	head.setEstateNum(bdcdyh);// 设定不动产单元号
	head.setRealEstateID(bdcdyid);
	head.setRelationID(relationID);
	return head;
    }
    //变更业务xml文件表头
    private HeadExport BGCreateSendMsgHead(BDCS_XMXX xmxx, String bljc) {
    	HeadExport head = new HeadExport();
    	String projectId = xmxx.getPROJECT_ID();// 业务号
    	String recSubtype = projectId.split("-")[2];// 从流程编号中获取登记细类
    	head.setBizMsgID(projectId);// 设定流程号
    	head.setBusinessID("");// 设定合同编号
    	head.setProjectName(xmxx.getXMMC());// 设定项目名称
    	head.setASID(ConfigHelper.getNameByValue("XZQHDM") + "100");// 设定共享标准？这个是这么弄的吗，待确定????
    	head.setAreaCode(ConfigHelper.getNameByValue("XZQHDM"));// 设定行政区代码
    	head.setRecType(xmxx.getDJLX());// 设定登记类型
    	head.setRecSubType(recSubtype);// 设定登记细类
    	head.setRecSubTypeName("分割合并流程");// 登记细类名称????
    	head.setRightType(xmxx.getQLLX());// 设定权利类型
    	head.setRightNature("");// 设定权利性质????
    	head.setCreateDate(StringHelper.FormatByDatetime(xmxx.getSLSJ()));// 设定项目创建时间
    	head.setRegOrgID(ConfigHelper.getNameByValue("XZQHDM") +ConfigHelper.getNameByValue("DJJG"));// 设定登记机构
    	head.setBusinessProcess(bljc);// 设定业务办理进程
    	return head;
    }
    /**
     * 上传xml文件到不动产登记局的sftp上
     * 
     * @Author：俞学斌
     * @param localFilePath
     *            本地xml文件地址
     */
    private String uploadFile(String localFilePath) {
	String ftpPath = "";
	SFTP mySFTP = new SFTP();
	ChannelSftp sftp = mySFTP.getSftp();
	if (sftp != null) {
	    try {
		ftpPath = ConfigHelper.getNameByValue("XZQHMC") + "不动产登记局";
		sftp.cd("/");
		mySFTP.mkDir(ftpPath, sftp);
		mySFTP.upload(localFilePath, sftp);
		return ftpPath;
	    } catch (Exception e) {
		e.printStackTrace();
	    } finally {
		mySFTP.disconnect(sftp);
	    }

	}
	return null;
    }

    /**
     * 根据路径获取文件名称
     * 
     * @Author：俞学斌
     * @param 根据xml文件路径获取文件名称
     */
    private String getXMLFileName(String xmlpath) {
	int index = xmlpath.lastIndexOf("\\");
	if (index < 0) {
	    index = xmlpath.lastIndexOf("/");
	}
	String fileName = xmlpath.substring(index + 1);
	return fileName;
    }

    /**
     * 生成发送的信息
     * 
     * @Author：俞学斌
     */
    private ShareMessage createSendMsg(String xmbh, String description,
	    String staffname, String fileName, String ywh, String sftpFilePath,
	    String bdcdylx) {
	ShareMessage msg = new ShareMessage();
	msg.setStaff(staffname);
	msg.setSender(ConfigHelper.getNameByValue("XZQHMC") + "不动产登记局");
	msg.setSendercode(ConfigHelper.getNameByValue("XZQHDM") + "100");
	msg.setDescription(description);
	msg.setSftpFileName(fileName);
	msg.setBdcdjXmbh(xmbh);
	msg.setSftpFilePath(sftpFilePath);
	msg.setBh(ywh);
	msg.setReceivercode(bdcdylx);
	msg.setReceiver("房地产交易管理中心");
	msg.setReceivercode(ConfigHelper.getNameByValue("XZQHDM") + "300");
	return msg;
    }

    /**
     * 获取站点的Classes的本地路径
     * 
     * @Author：俞学斌
     */
    private String GetClassesPath() {
	URL url = Thread.currentThread().getContextClassLoader()
		.getResource("");
	String path = url.getPath();
	path = path.substring(1);
	return path;
    }

    private String createXML(BDCS_XMXX xmxx, MessageExport msg, int djdyIndex) {
	double startTime = System.currentTimeMillis();
	// 创建xml操作对象
	XStream xstream = new XStream();
	// 以下为根据MessageW类型对象创建xml内容，不懂，不具体写描述了
	xstream.alias("Message", MessageExport.class);
	xstream.processAnnotations(MessageExport.class);
	// 启用Annotation
	xstream.autodetectAnnotations(true);
	// xstream.addImplicitCollection(Data.class, "list");
	String xmlStr = xstream.toXML(msg);

	String tempPath = GetProperties.getConstValueByKey("xmlPath");
	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
	String dateStr = dateFormat.format(new Date());
	String fileName = ConfigHelper.getNameByValue("XZQHMC") + "不动产登记局-"
		+ xmxx.getPROJECT_ID() + "-"
		+ StringHelper.formatObject(djdyIndex) + "-" + dateStr + ".xml";
	File _dir = new File(tempPath);
	if (!_dir.exists()) {
	    _dir.mkdirs();
	}
	File file = new File(_dir, fileName);
	if (file.exists()) {

	    file.delete();
	}
	try {
	    file.createNewFile();
	    OutputStreamWriter output = new OutputStreamWriter(
		    new FileOutputStream(file), "utf-8");

	    output.write(xmlStr);
	    output.close();
	    System.out.println("获取不动产登记信息，并创建xml，用时："
		    + (System.currentTimeMillis() - startTime));
	    return file.getPath();
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return null;
    }
    public void deleteFolder(String folder){
    	File dir = new File(folder);
    	deleteDir(dir);
    }
    private boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();

            for (int i=0; i<children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }
    public String createXMLInFile(BDCS_XMXX xmxx, MessageExport msg, int djdyIndex, String bljc) {
    	double startTime = System.currentTimeMillis();
    	// 创建xml操作对象
    	XStream xstream = new XStream();
    	// 以下为根据MessageW类型对象创建xml内容，不懂，不具体写描述了
    	xstream.alias("Message", MessageExport.class);
    	xstream.processAnnotations(MessageExport.class);
    	// 启用Annotation
    	xstream.autodetectAnnotations(true);
    	// xstream.addImplicitCollection(Data.class, "list");
    	String xmlStr = xstream.toXML(msg);

    	String tempPath = GetProperties.getConstValueByKey("xmlPath") + "\\" + xmxx.getPROJECT_ID()+"_"+bljc;
    	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
    	String dateStr = dateFormat.format(new Date());
//    	tempPath= tempPath + "_"+dateStr;
    	String fileName = ConfigHelper.getNameByValue("XZQHMC") + "不动产登记局-"
    		+ xmxx.getPROJECT_ID() + "-"
    		+ StringHelper.formatObject(djdyIndex) + "-" + dateStr + ".xml";
    	File _dir = new File(tempPath);
    	if (!_dir.exists()) {
    	    _dir.mkdirs();
    	}
    	File file = new File(_dir, fileName);
    	if (file.exists()) {

    	    file.delete();
    	}
    	try {
    	    file.createNewFile();
    	    OutputStreamWriter output = new OutputStreamWriter(
    		    new FileOutputStream(file), "utf-8");

    	    output.write(xmlStr);
    	    output.close();
    	    
    	    return tempPath;//文件夹需要压缩，所以只返回文件夹的路径
    	} catch (Exception e) {
    	    e.printStackTrace();
    	}
    	return null;
        }
    /**
     * 
     * @作者 胡加红 根据登记类型/办理进程分类存放xml文件
     * @创建时间 2016年1月27日下午8:33:24
     * @param xmxx
     * @param msg
     * @param djdyIndex
     * @param bljc
     * @return
     */
    private String createClassifyXML(BDCS_XMXX xmxx, MessageExport msg, int djdyIndex,String bljc) {
    	double startTime = System.currentTimeMillis();
    	// 创建xml操作对象
    	XStream xstream = new XStream();
    	// 以下为根据MessageW类型对象创建xml内容，不懂，不具体写描述了
    	xstream.alias("Message", MessageExport.class);
    	xstream.processAnnotations(MessageExport.class);
    	// 启用Annotation
    	xstream.autodetectAnnotations(true);
    	// xstream.addImplicitCollection(Data.class, "list");
    	String xmlStr = xstream.toXML(msg);

    	String tempPath = GetProperties.getConstValueByKey("xmlPath");
    	String djlx = xmxx.getDJLX();
    	String jc = bljc.equals("1") ? "受理" : "登薄";
    	//抵押需要单独放到一个文件夹里面 
    	if(xmxx.getQLLX().equals("23")){
    		djlx="抵押";//抵押相关的都放到一个文件夹
    	}
    	if(xmxx.getQLLX().equals("4") && (xmxx.getDJLX().equals("200") || xmxx.getDJLX().equals("300") 
    			|| (xmxx.getDJLX().equals("900") && xmxx.getQLLX().equals("4") 
    					&& xmxx.getPROJECT_ID().substring(12, 17).equals("90104")))){
    		djlx="转移变更换补证";
    	}
//    	tempPath = tempPath + "\\" + djlx +"\\" + jc;

    	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
    	String dateStr = dateFormat.format(new Date());
//    	String fileName = ConfigHelper.getNameByValue("XZQHMC") + "不动产登记局-"
//    		+ xmxx.getPROJECT_ID() + "-"
//    		+ StringHelper.formatObject(djdyIndex) + "-" + dateStr + ".xml";
    	String fileName =  StringHelper.formatObject(djdyIndex) + "-" + ".xml";
    	File _dir = new File(tempPath);
    	if (!_dir.exists()) {
    	    _dir.mkdirs();
    	}
    	File file = new File(_dir, fileName);
    	if (file.exists()) {

    	    file.delete();
    	}
    	try {
    	    file.createNewFile();
    	    OutputStreamWriter output = new OutputStreamWriter(
    		    new FileOutputStream(file), "utf-8");

    	    output.write(xmlStr);
    	    output.close();
//    	    System.out.println("获取不动产登记信息，并创建xml，用时："
//    		    + (System.currentTimeMillis() - startTime));
    	    return file.getPath();
    	} catch (Exception e) {
    	    e.printStackTrace();
    	}
    	return null;
        }
    /**
     * 
     * @作者 胡加红 根据登记类型/办理进程分类存放xml文件
     * @创建时间 2016年1月27日下午8:31:41
     * @param xmxx
     * @param msg
     * @param djdyIndex
     * @param bljc
     * @return
     */
    private String createClassifyBGXML(BDCS_XMXX xmxx, BGMessageExport msg, int djdyIndex,String bljc) {
    	double startTime = System.currentTimeMillis();
    	// 创建xml操作对象
    	XStream xstream = new XStream();
    	xstream.alias("Message", MessageExport.class);
    	xstream.processAnnotations(MessageExport.class);
    	// 启用Annotation
    	xstream.autodetectAnnotations(true);
    	// xstream.addImplicitCollection(Data.class, "list");
    	String xmlStr = xstream.toXML(msg);

    	String tempPath = GetProperties.getConstValueByKey("xmlPath");
    	String djlx = "分割合并流程";
    	String jc = bljc.equals("1") ? "受理" : "登薄";
//    	tempPath = tempPath + "\\" + djlx +"\\" + jc;
    	
    	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
    	String dateStr = dateFormat.format(new Date());
//    	String fileName = ConfigHelper.getNameByValue("XZQHDM") + "不动产登记局-"
//    		+ xmxx.getPROJECT_ID() + "-"
//    		+ StringHelper.formatObject(djdyIndex) + "-" + dateStr + ".xml";
    	String fileName = StringHelper.formatObject(djdyIndex) + ".xml";
    	File _dir = new File(tempPath);
    	if (!_dir.exists()) {
    	    _dir.mkdirs();
    	}
    	File file = new File(_dir, fileName);
    	if (file.exists()) {

    	    file.delete();
    	}
    	try {
    	    file.createNewFile();
    	    OutputStreamWriter output = new OutputStreamWriter(
    		    new FileOutputStream(file), "utf-8");

    	    output.write(xmlStr);
    	    output.close();
    	    System.out.println("获取不动产登记信息，并创建xml，用时："
    		    + (System.currentTimeMillis() - startTime));
    	    return file.getPath();
    	} catch (Exception e) {
    	    e.printStackTrace();
    	}
    	return null;
        }
    private String createBGXML(BDCS_XMXX xmxx, BGMessageExport msg, int djdyIndex) {
    	double startTime = System.currentTimeMillis();
    	// 创建xml操作对象
    	XStream xstream = new XStream();
    	xstream.alias("Message", MessageExport.class);
    	xstream.processAnnotations(MessageExport.class);
    	// 启用Annotation
    	xstream.autodetectAnnotations(true);
    	// xstream.addImplicitCollection(Data.class, "list");
    	String xmlStr = xstream.toXML(msg);

    	String tempPath = GetProperties.getConstValueByKey("xmlPath");
    	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
    	String dateStr = dateFormat.format(new Date());
    	String fileName = ConfigHelper.getNameByValue("XZQHDM") + "不动产登记局-"
    		+ xmxx.getPROJECT_ID() + "-"
    		+ StringHelper.formatObject(djdyIndex) + "-" + dateStr + ".xml";
    	File _dir = new File(tempPath);
    	if (!_dir.exists()) {
    	    _dir.mkdirs();
    	}
    	File file = new File(_dir, fileName);
    	if (file.exists()) {

    	    file.delete();
    	}
    	try {
    	    file.createNewFile();
    	    OutputStreamWriter output = new OutputStreamWriter(
    		    new FileOutputStream(file), "utf-8");

    	    output.write(xmlStr);
    	    output.close();
    	    System.out.println("获取不动产登记信息，并创建xml，用时："
    		    + (System.currentTimeMillis() - startTime));
    	    return file.getPath();
    	} catch (Exception e) {
    	    e.printStackTrace();
    	}
    	return null;
        }
    public void SendMsg(MessageExport msg, int djdyIndex, String bdcdylx,
	    BDCS_XMXX _xmxx) {
	String LOG_ID = "";
	if(msg == null){
		return;
	}
//	this.xmxx = _xmxx;
	// 获取描述
	String description = _xmxx.getXMMC() + "-" + _xmxx.getDJLX();
	// 获取当前登录人员
	if ("".equals(staffName) || staffName == null) {
	    staffName = Global.getCurrentUserName();
	}
	// 根据项目编号获取项目中登记集合
	String filePath = createXML(_xmxx, msg, djdyIndex);
	// 上传共享文件
	String sftpPath = uploadFile(filePath);

	// 获取共享文件路径
	String sftpFileName = getXMLFileName(filePath);
	// 创建共享信息
	ShareMessage shareMessage = createSendMsg(_xmxx.getId(), description,
		staffName, sftpFileName, _xmxx.getPROJECT_ID(), sftpPath,
		bdcdylx);// 发送消息用到
	shareMessage.setbTansmited(false);
	if (sftpPath == null) {//为了提速，把此处调用日志服务禁用

	    LOG_ID = saveLog(LOG_ID, "登记中心上传文件失败！", false, shareMessage);
	    return;
	} else {
	    //LOG_ID = saveLog(LOG_ID, "登记中心上传文件成功！", true, shareMessage);
	}
//
	shareMessage.setLogid(LOG_ID);
	if( msg != null && msg.getHead() != null && msg.getHead().getBusinessProcess()!= null){
		if(msg.getHead().getBusinessProcess().getvalue().equals("1")){
			shareMessage.setAcinst_name("申请受理");
		}
		else if(msg.getHead().getBusinessProcess().getvalue().equals("3")){
			shareMessage.setAcinst_name("登薄");
		}
		else if(msg.getHead().getBusinessProcess().getvalue().equals("9")){
			shareMessage.setAcinst_name("缮证");
		}	
		else{
			shareMessage.setAcinst_name("申请受理");
		}
	}
	// 发送共享信息
	mqSender.sendMessage(shareMessage);
	// Thread.sleep(1000);
	// LOG_ID = saveLog("登记中心发送共享信息成功！", true, shareMessage);
    }
    
    public void SendMsg(String floderPath , BDCS_XMXX _xmxx ,String bljc , String bdcdylx) {
		String LOG_ID = "";
		if(_xmxx == null){
			return;
		}
//		this.xmxx = _xmxx;
		// 获取描述
		String description = _xmxx.getXMMC() + "-" + _xmxx.getDJLX();
		// 获取当前登录人员
		if ("".equals(staffName) || staffName == null) {
		    staffName = Global.getCurrentUserName();
		}
		String filePath = floderPath+".zip";
		ZipCompressorByAnt zip = new ZipCompressorByAnt(filePath);
		zip.compressExe(floderPath);		
		// 上传共享文件
		String sftpPath = uploadFile(filePath);

		// 获取共享文件路径
		String sftpFileName = getXMLFileName(filePath);
		// 创建共享信息
		ShareMessage shareMessage = createSendMsg(_xmxx.getId(), description,
			staffName, sftpFileName, _xmxx.getPROJECT_ID(), sftpPath, bdcdylx);// 发送消息用到
		shareMessage.setbTansmited(false);
		if (sftpPath == null) {//为了提速，把此处调用日志服务禁用

		    LOG_ID = saveLog(LOG_ID, "登记中心上传文件失败！", false, shareMessage);
		    return;
		} else {
		    //LOG_ID = saveLog(LOG_ID, "登记中心上传文件成功！", true, shareMessage);
		}
	//
		shareMessage.setLogid(LOG_ID);
		if( bljc != null && !bljc.equals("") ){
			if(bljc.equals("1")){
				shareMessage.setAcinst_name("申请受理");
			}
			else if(bljc.equals("3")){
				shareMessage.setAcinst_name("登薄");
			}
			else if(bljc.equals("9")){
				shareMessage.setAcinst_name("缮证");
			}	
			else{
				shareMessage.setAcinst_name("申请受理");
			}
		}
		// 发送共享信息
		mqSender.sendMessage(shareMessage);
}
    public void BGSendMsg(BGMessageExport msg, int djdyIndex, String bdcdylx, BDCS_XMXX _xmxx) {
    	String LOG_ID = "";
    	if(msg == null){
    		return;
    	}
//    	this.xmxx = _xmxx;
    	// 获取描述
    	String description = "BGYW-"+ _xmxx.getXMMC() + "-" + _xmxx.getDJLX();
    	// 获取当前登录人员
    	if ("".equals(staffName) || staffName == null) {
    	    staffName = Global.getCurrentUserName();
    	}
    	// 根据项目编号获取项目中登记集合
    	String filePath = createBGXML(_xmxx, msg, djdyIndex);
    	// 上传共享文件
    	String sftpPath = uploadFile(filePath);

    	// 获取共享文件路径
    	String sftpFileName = getXMLFileName(filePath);
    	// 创建共享信息
    	ShareMessage shareMessage = createSendMsg(_xmxx.getId(), description,
    		staffName, sftpFileName, _xmxx.getPROJECT_ID(), sftpPath,
    		bdcdylx);// 发送消息用到
    	shareMessage.setbTansmited(false);
    	if (sftpPath == null) {

    	    LOG_ID = saveLog(LOG_ID, "登记中心上传文件失败！", false, shareMessage);
    	    return;
    	} else {
    	    //LOG_ID = saveLog(LOG_ID, "登记中心上传文件成功！", true, shareMessage);
    	}

    	shareMessage.setLogid(LOG_ID);
    	shareMessage.setXMLType("300-3");
    	if( msg != null && msg.getHead() != null && msg.getHead().getBusinessProcess()!= null){
    		if(msg.getHead().getBusinessProcess().getvalue().equals("1")){
    			shareMessage.setAcinst_name("申请受理");
    		}
    		else if(msg.getHead().getBusinessProcess().getvalue().equals("3")){
    			shareMessage.setAcinst_name("登薄");
    		}
    		else if(msg.getHead().getBusinessProcess().getvalue().equals("9")){
    			shareMessage.setAcinst_name("缮证");
    		}	
    		else{
    			shareMessage.setAcinst_name("申请受理");
    		}
    	}
    	// 发送共享信息
    	mqSender.sendMessage(shareMessage);
    	// Thread.sleep(1000);
//    	 LOG_ID = saveLog("登记中心发送共享信息成功！", true, shareMessage);
        }
    private List<Object> GetBDCDYExport(RealUnit bdcdy) {
	List<Object> dyExport = new ArrayList<Object>();
	CExport cExport = new CExport();
	DZDZWExport dzdzwExport = new DZDZWExport();
	GZWExport gzwExport = new GZWExport();
	HExport hExport = new HExport();
	LJZExport ljzExport = new LJZExport();
	MZDZWExport mzdzwExport = new MZDZWExport();
	SHYQZDExport shyqzdExport = new SHYQZDExport();
	SYQZDExport syqzdExport = new SYQZDExport();
	XZDZWExport xzdzwExport = new XZDZWExport();
	YHYDZBExport yhydzbExport = new YHYDZBExport();
	YHZKExport yhzkExport = new YHZKExport();
	ZDBHQKExport zdbhqkExport = new ZDBHQKExport();
	ZHBHQKExport zhbhqkExport = new ZHBHQKExport();
	ZHExport zhExport = new ZHExport();
	ZRZExport zrzExport = new ZRZExport();
	if (bdcdy != null) {
	    if (bdcdy.getBDCDYLX().equals(BDCDYLX.H)
		    || bdcdy.getBDCDYLX().equals(BDCDYLX.YCH)) {
		House h = (House) bdcdy;
		hExport.setBDCDYID(h.getId());
		hExport.setBDCDYH(h.getBDCDYH());
		hExport.setFWBM(h.getFWBM());
		hExport.setZRZH(h.getZRZH());
		hExport.setLJZH(h.getLJZH());
		hExport.setCH(h.getCH());
		hExport.setZL(h.getZL());
		hExport.setMJDW(h.getMJDW());
		// hExport.setMJDWMC(h.getMJDWMC());
		if(h.getZZC() == null){
			h.setZZC(0.0);
		}
		if(h.getQSC() == null){
			h.setQSC(0.0);
		}
		 hExport.setSJCS((float)(h.getZZC()-h.getQSC()+1)); 

		hExport.setHH(h.getHH());
		hExport.setSHBW(h.getSHBW());
		hExport.setHX(h.getHX());
		// hExport.setHXMC(h.getHXMC());
		hExport.setHXJG(h.getHXJG());
		// hExport.setHXJGMC(h.getHXJGMC());
		hExport.setFWYT1(h.getFWYT1());
		// hExport.setFWYT1MC(h.getFWYT1MC());
		hExport.setFWYT2(h.getFWYT2());
		// hExport.setFWYT2MC(h.getFWYT2MC());
		hExport.setFWYT3(h.getFWYT3());
		// hExport.setFWYT3MC(h.getFWYT3MC());
		hExport.setYCJZMJ(h.getYCJZMJ());
		hExport.setYCFTJZMJ(h.getYCFTJZMJ());
		hExport.setYCDXBFJZMJ(h.getYCDXBFJZMJ());
		hExport.setYCQTJZMJ(h.getYCQTJZMJ());
		hExport.setYCFTXS(h.getYCFTXS());
		hExport.setSCJZMJ(h.getSCJZMJ());
		hExport.setSCTNJZMJ(h.getSCTNJZMJ());
		hExport.setSCFTJZMJ(h.getSCFTJZMJ());
		hExport.setSCDXBFJZMJ(h.getSCDXBFJZMJ());
		hExport.setSCQTJZMJ(h.getSCQTJZMJ());
		hExport.setSCFTXS(h.getSCFTXS());
		hExport.setGYTDMJ(h.getGYTDMJ());
		hExport.setFTTDMJ(h.getFTTDMJ());
		hExport.setDYTDMJ(h.getDYTDMJ());
		hExport.setFWLX(h.getFWLX());
		// hExport.setFWLXMC(h.getFWLXMC());
		hExport.setFWXZ(h.getFWXZ());
		// hExport.setFWXZMC(h.getFWXZMC());
		hExport.setFCFHT(h.getFCFHT());
		hExport.setZT(h.getZT());
		hExport.setXZZT(h.getXZZT());//增加限制状态，=01时是限制状态
		hExport.setRelationID(h.getRELATIONID());
	    } else if (bdcdy.getBDCDYLX().equals(BDCDYLX.ZRZ)) {
		Building zrz = (Building) bdcdy;
		zrzExport.setBDCDYID(zrz.getId());
		zrzExport.setBDCDYH(zrz.getBDCDYH());
		zrzExport.setZDDM(zrz.getZDDM());
		zrzExport.setZRZH(zrz.getZRZH());
		zrzExport.setJZWMC(zrz.getJZWMC());
		zrzExport.setJGRQ(zrz.getJGRQ());
		zrzExport.setJZWGD(zrz.getJZWGD());
		zrzExport.setZZDMJ(zrz.getZZDMJ());
		zrzExport.setZYDMJ(zrz.getZYDMJ());
		zrzExport.setYCJZMJ(zrz.getYCJZMJ());
		zrzExport.setSCJZMJ(zrz.getSCJZMJ());
		zrzExport.setZCS(zrz.getZCS());
		zrzExport.setDSCS(zrz.getDSCS());
		zrzExport.setDXCS(zrz.getDXCS());
		zrzExport.setDXSD(zrz.getDXSD());
		zrzExport.setGHYT(zrz.getGHYT());
		// zrzExport.setGHYTMC(zrz.getGHYTMC());
		zrzExport.setFWJG(zrz.getFWJG());
		// zrzExport.setFWJGMC(zrz.getFWJGMC());
		zrzExport.setZTS(zrz.getZTS());
		zrzExport.setJZWJBYT(zrz.getJZWJBYT());
		zrzExport.setBZ(zrz.getBZ());
		zrzExport.setZT(zrz.getZT());
		// zrzExport.setZTMC(zrz.getZTMC());
		zrzExport.setRelationID(zrz.getRELATIONID());
	    } else if (bdcdy.getBDCDYLX().equals(BDCDYLX.SYQZD)) {
		OwnerLand syqzd = (OwnerLand) bdcdy;
		syqzdExport.setBDCDYID(syqzd.getId());

		syqzdExport.setZDDM(syqzd.getZDDM());
		syqzdExport.setBDCDYH(syqzd.getBDCDYH());
		syqzdExport.setZDTZM(syqzd.getZDTZM());
		// syqzdExport.setZDTZMMC(syqzd.getZDTZMMC());
		syqzdExport.setZL(syqzd.getZL());
		syqzdExport.setZDMJ(syqzd.getZDMJ());
		syqzdExport.setMJDW(syqzd.getMJDW());
		// syqzdExport.setMJDWMC(syqzd.getMJDWMC());
		syqzdExport.setYT(syqzd.getYT());
		syqzdExport.setDJ(syqzd.getDJ());
		// syqzdExport.setDJMC(syqzd.getDJMC());
		syqzdExport.setJG(syqzd.getJG());
		syqzdExport.setQLLX(syqzd.getQLLX());
		// syqzdExport.setQLLXMC(syqzd.getQLLXMC());
		syqzdExport.setQLXZ(syqzd.getQLXZ());
		// syqzdExport.setQLXZMC(syqzd.getQLXZMC());
		syqzdExport.setQLSDFS(syqzd.getQLSDFS());
		// syqzdExport.setQLSDFSMC(syqzd.getQLSDFSMC());
		syqzdExport.setRJL(syqzd.getRJL());
		syqzdExport.setJZMD(syqzd.getJZMD());
		syqzdExport.setJZXG(syqzd.getJZXG());
		syqzdExport.setZDSZD(syqzd.getZDSZD());
		syqzdExport.setZDSZN(syqzd.getZDSZN());
		syqzdExport.setZDSZX(syqzd.getZDSZX());
		syqzdExport.setZDSZB(syqzd.getZDSZB());
		syqzdExport.setZDT(syqzd.getZDT());
		syqzdExport.setTFH(syqzd.getTFH());
		syqzdExport.setDJH(syqzd.getDJH());
		syqzdExport.setZT(syqzd.getZT());
		// syqzdExport.setZTMC(syqzd.getZTMC());
		syqzdExport.setRelationID(syqzd.getRELATIONID());
	    } else if (bdcdy.getBDCDYLX().equals(BDCDYLX.SHYQZD)) {
		UseLand shyqzd = (UseLand) bdcdy;
		shyqzdExport.setBDCDYID(shyqzd.getId());

		shyqzdExport.setZDDM(shyqzd.getZDDM());
		shyqzdExport.setBDCDYH(shyqzd.getBDCDYH());
		shyqzdExport.setZDTZM(shyqzd.getZDTZM());
		// shyqzdExport.setZDTZMMC(shyqzd.getZDTZMMC());
		shyqzdExport.setZL(shyqzd.getZL());
		shyqzdExport.setZDMJ(shyqzd.getZDMJ());
		shyqzdExport.setMJDW(shyqzd.getMJDW());
		// shyqzdExport.setMJDWMC(shyqzd.getMJDWMC());
		shyqzdExport.setYT(shyqzd.getYT());
		shyqzdExport.setDJ(shyqzd.getDJ());
		// shyqzdExport.setDJMC(shyqzd.getDJMC());
		shyqzdExport.setJG(shyqzd.getJG());
		shyqzdExport.setQLLX(shyqzd.getQLLX());
		// shyqzdExport.setQLLXMC(shyqzd.getQLLXMC());
		shyqzdExport.setQLXZ(shyqzd.getQLXZ());
		// shyqzdExport.setQLXZMC(shyqzd.getQLXZMC());
		shyqzdExport.setQLSDFS(shyqzd.getQLSDFS());
		// shyqzdExport.setQLSDFSMC(shyqzd.getQLSDFSMC());
		shyqzdExport.setRJL(shyqzd.getRJL());
		shyqzdExport.setJZMD(shyqzd.getJZMD());
		shyqzdExport.setJZXG(shyqzd.getJZXG());
		shyqzdExport.setZDSZD(shyqzd.getZDSZD());
		shyqzdExport.setZDSZN(shyqzd.getZDSZN());
		shyqzdExport.setZDSZX(shyqzd.getZDSZX());
		shyqzdExport.setZDSZB(shyqzd.getZDSZB());
		shyqzdExport.setZDT(shyqzd.getZDT());
		shyqzdExport.setTFH(shyqzd.getTFH());
		shyqzdExport.setDJH(shyqzd.getDJH());
		shyqzdExport.setZT(shyqzd.getZT());
		// shyqzdExport.setZTMC(shyqzd.getZTMC());
		shyqzdExport.setRelationID(shyqzd.getRELATIONID());
	    }
	}
	dyExport.add(cExport);
	dyExport.add(dzdzwExport);
	dyExport.add(gzwExport);
	dyExport.add(hExport);
	dyExport.add(ljzExport);
	dyExport.add(mzdzwExport);
	dyExport.add(shyqzdExport);
	dyExport.add(syqzdExport);
	dyExport.add(xzdzwExport);
	dyExport.add(yhydzbExport);
	dyExport.add(yhzkExport);
	dyExport.add(zdbhqkExport);
	dyExport.add(zhExport);
	dyExport.add(zhbhqkExport);
	dyExport.add(zrzExport);
	return dyExport;
    }
    //变更单元
    private HExport GetBGHouseExport(RealUnit bdcdy) {
    	HExport hExport = new HExport();
    	if (bdcdy != null) {
    	    if (bdcdy.getBDCDYLX().equals(BDCDYLX.H) || bdcdy.getBDCDYLX().equals(BDCDYLX.YCH)) {
    		House h = (House) bdcdy;
    		hExport.setBDCDYID(h.getId());
    		hExport.setBDCDYH(h.getBDCDYH());
    		hExport.setFWBM(h.getFWBM());
    		hExport.setZRZH(h.getZRZH());
    		hExport.setLJZH(h.getLJZH());
    		hExport.setCH(h.getCH());
    		hExport.setZL(h.getZL());
    		hExport.setMJDW(h.getMJDW());
    		// hExport.setMJDWMC(h.getMJDWMC());
    		if(h.getZZC() == null){
    			h.setZZC(0.0);
    		}
    		if(h.getQSC() == null){
    			h.setQSC(0.0);
    		}
    		hExport.setSJCS((float)(h.getZZC()-h.getQSC()+1)); 

    		hExport.setHH(h.getHH());
    		hExport.setSHBW(h.getSHBW());
    		hExport.setHX(h.getHX());
    		// hExport.setHXMC(h.getHXMC());
    		hExport.setHXJG(h.getHXJG());
    		// hExport.setHXJGMC(h.getHXJGMC());
    		hExport.setFWYT1(h.getFWYT1());
    		// hExport.setFWYT1MC(h.getFWYT1MC());
    		hExport.setFWYT2(h.getFWYT2());
    		// hExport.setFWYT2MC(h.getFWYT2MC());
    		hExport.setFWYT3(h.getFWYT3());
    		// hExport.setFWYT3MC(h.getFWYT3MC());
    		hExport.setYCJZMJ(h.getYCJZMJ());
    		hExport.setYCFTJZMJ(h.getYCFTJZMJ());
    		hExport.setYCDXBFJZMJ(h.getYCDXBFJZMJ());
    		hExport.setYCQTJZMJ(h.getYCQTJZMJ());
    		hExport.setYCFTXS(h.getYCFTXS());
    		hExport.setSCJZMJ(h.getSCJZMJ());
    		hExport.setSCTNJZMJ(h.getSCTNJZMJ());
    		hExport.setSCFTJZMJ(h.getSCFTJZMJ());
    		hExport.setSCDXBFJZMJ(h.getSCDXBFJZMJ());
    		hExport.setSCQTJZMJ(h.getSCQTJZMJ());
    		hExport.setSCFTXS(h.getSCFTXS());
    		hExport.setGYTDMJ(h.getGYTDMJ());
    		hExport.setFTTDMJ(h.getFTTDMJ());
    		hExport.setDYTDMJ(h.getDYTDMJ());
    		hExport.setFWLX(h.getFWLX());
    		// hExport.setFWLXMC(h.getFWLXMC());
    		hExport.setFWXZ(h.getFWXZ());
    		// hExport.setFWXZMC(h.getFWXZMC());
    		hExport.setFCFHT(h.getFCFHT());
    		hExport.setZT(h.getZT());
    		// hExport.setZTMC(h.getZTMC());
    		hExport.setRelationID(h.getRELATIONID());
    	    }
    	}    
    	return hExport;
    }
    //变更幢
    private ZRZExport GetBGBuildExport(String zrzbdcdyid ) {
    	ZRZExport zrzExport = new ZRZExport();
	    if (zrzbdcdyid != null && !"".equals(zrzbdcdyid)) {
	    	//根据户的zrzbdcdyid查询关联的幢信息
	    	RealUnit zrz_bdcdy = UnitTools.loadUnit(BDCDYLX.ZRZ, DJDYLY.XZ, zrzbdcdyid);
    		Building zrz = (Building) zrz_bdcdy;
    		zrzExport.setBDCDYID(zrz.getId());
    		zrzExport.setBDCDYH(zrz.getBDCDYH());
    		zrzExport.setZDDM(zrz.getZDDM());
    		zrzExport.setZRZH(zrz.getZRZH());
    		zrzExport.setJZWMC(zrz.getJZWMC());
    		zrzExport.setJGRQ(zrz.getJGRQ());
    		zrzExport.setJZWGD(zrz.getJZWGD());
    		zrzExport.setZZDMJ(zrz.getZZDMJ());
    		zrzExport.setZYDMJ(zrz.getZYDMJ());
    		zrzExport.setYCJZMJ(zrz.getYCJZMJ());
    		zrzExport.setSCJZMJ(zrz.getSCJZMJ());
    		zrzExport.setZCS(zrz.getZCS());
    		zrzExport.setDSCS(zrz.getDSCS());
    		zrzExport.setDXCS(zrz.getDXCS());
    		zrzExport.setDXSD(zrz.getDXSD());
    		zrzExport.setGHYT(zrz.getGHYT());
    		// zrzExport.setGHYTMC(zrz.getGHYTMC());
    		zrzExport.setFWJG(zrz.getFWJG());
    		// zrzExport.setFWJGMC(zrz.getFWJGMC());
    		zrzExport.setZTS(zrz.getZTS());
    		zrzExport.setJZWJBYT(zrz.getJZWJBYT());
    		zrzExport.setBZ(zrz.getBZ());
    		zrzExport.setZT(zrz.getZT());
    		// zrzExport.setZTMC(zrz.getZTMC());
    		zrzExport.setRelationID(zrz.getRELATIONID());
	    }
    	return zrzExport;
    }
    private List<Object> GetBDCQLExport(BDCS_XMXX xmxx, Rights bdcql, SubRights bdcfsql,
	    RealUnit bdcdy) {
	List<Object> qlExport = new ArrayList<Object>();
	CFDJExport cfdjExport = new CFDJExport();//增加lyqlid
	DYAQExport dyaqExport = new DYAQExport();//增加lyqlid
	DYIQExport dyiqExport = new DYIQExport();
	FDCQ1Export fdcq1Export = new FDCQ1Export();//增加lyqlid
	FDCQ2Export fdcq2Export = new FDCQ2Export();//增加lyqlid
	FDCQ3Export fdcq3Export = new FDCQ3Export();//增加lyqlid
	GJZWSYQExport gzwsyqExport = new GJZWSYQExport();
	HYSYQExport hysyqExport = new HYSYQExport();
	JSYDSYQExport jsydsyqExport = new JSYDSYQExport();
	LQExport lqExport = new LQExport();
	NYDSYQExport nydsyqExport = new NYDSYQExport();
	QTXGQLExport qtxgqlExport = new QTXGQLExport();
	TDSYQExport tdsyqExport = new TDSYQExport();
	YGDJExport ygdjExport = new YGDJExport();//增加lyqlid
	YYDJExport yydjExport = new YYDJExport();
	String lyqlid="", lsywh="";//注销登记的来源权利ywh
	if(bdcql.getLYQLID() != null && !"".equals(bdcql.getLYQLID())){
		lyqlid=bdcql.getLYQLID();
		lsywh = GetLastYWH(lyqlid);
	}
	if (xmxx.getQLLX().equals(QLLX.QTQL.Value)
		|| xmxx.getQLLX().equals(QLLX.CFZX.Value)) {
	    if (bdcdy != null) {
		cfdjExport.setBDCDYID(bdcdy.getId());
		cfdjExport.setBDCDYH(bdcdy.getBDCDYH());
	    }
	    if (bdcql != null) {
		cfdjExport.setQLID(bdcql.getId());//从查封解封的handler里面修改过
		if(bdcql.getLYQLID() != null && !"".equals(bdcql.getLYQLID())){
			cfdjExport.setLYQLID(bdcql.getLYQLID());
		}
		cfdjExport.setYWH(bdcql.getYWH());
		if(!"".equals(lsywh)){
			cfdjExport.setYWH(lsywh);
		}
		cfdjExport.setCFQSSJ(bdcql.getQLQSSJ());
		cfdjExport.setCFJSSJ(bdcql.getQLJSSJ());
		cfdjExport.setFJ(bdcql.getFJ());
		cfdjExport.setQSZT(bdcql.getQSZT());
		// cfdjExport.setQSZTMC(bdcql.getQSZTMC());
	    }
	    if (bdcfsql != null) {
		cfdjExport.setZXYWH(bdcfsql.getZXDYYWH());
		cfdjExport.setCFJG(bdcfsql.getCFJG());
		cfdjExport.setCFLX(bdcfsql.getCFLX());
		// cfdjExport.setCFLXMC(bdcfsql.getCFLXMC());
		cfdjExport.setCFWJ(bdcfsql.getCFWJ());
		cfdjExport.setCFWH(bdcfsql.getCFWH());
		cfdjExport.setCFFW(bdcfsql.getCFFW());
		cfdjExport.setJFYWH(bdcfsql.getZXDYYWH());
		cfdjExport.setJFJG(bdcfsql.getJFJG());
		cfdjExport.setJFWJ(bdcfsql.getJFWJ());
		cfdjExport.setJFWH(bdcfsql.getJFWH());
		cfdjExport.setJFDBR(bdcfsql.getZXDBR());
		cfdjExport.setJFDJSJ(bdcfsql.getZXSJ());
	    }
	} else if (xmxx.getDJLX().equals(DJLX.YGDJ.Value)) {
	    if (bdcdy != null) {
		ygdjExport.setBDCDYID(bdcdy.getId());
		ygdjExport.setBDCZL(bdcdy.getZL());
		House h = (House) bdcdy;
		if (h != null) {
		    ygdjExport.setTDSYQR(h.getTDSYQR());
		    ygdjExport.setGHYT(h.getGHYT());
		    // ygdjExport.setGHYTMC(h.getGHYTMC());
		    ygdjExport.setFWXZ(h.getFWXZ());
		    // ygdjExport.setFWXZMC(h.getFWXZMC());
		    ygdjExport.setFWJG(h.getFWJG());
		    // ygdjExport.setFWJGMC(h.getFWJGMC());
		    ygdjExport.setSZC(h.getSZC());
		    ygdjExport.setZCS(h.getZCS());
		    ygdjExport.setJZMJ(h.getSCJZMJ());
		}

	    }
	    if (bdcql != null) {
		ygdjExport.setQLID(bdcql.getId());
		if(bdcql.getLYQLID() != null && !"".equals(bdcql.getLYQLID())){
			ygdjExport.setLYQLID(bdcql.getLYQLID());
		}
		ygdjExport.setBDCDYH(bdcdy.getBDCDYH());
		ygdjExport.setYWH(bdcql.getYWH());
		if(!"".equals(lsywh)){
			cfdjExport.setYWH(lsywh);
		}
		ygdjExport.setDJLX(bdcql.getDJLX());
		// ygdjExport.setDJLXMC(bdcql.getDJLXMC());
		ygdjExport.setDJYY(bdcql.getDJYY());
		ygdjExport.setQDJG(bdcql.getQDJG());
		ygdjExport.setBDCDJZMH(bdcql.getBDCQZH());
		ygdjExport.setFJ(bdcql.getFJ());
		ygdjExport.setQSZT(bdcql.getQSZT());
		// ygdjExport.setQSZTMC(bdcql.getQSZTMC());
	    }
	    if (bdcfsql != null) {
	    	ygdjExport.setYWR(bdcfsql.getYWR());
	    	if(QLLX.DIYQ.Value.equals(xmxx.getQLLX())){
	    		ygdjExport.setYWR(bdcfsql.getDYR());
	    	}
			ygdjExport.setYWRZJZL(bdcfsql.getYWRZJZL());
			// ygdjExport.setYWRZJZLMC(bdcfsql.getYWRZJZLMC());
			ygdjExport.setYWRZJH(bdcfsql.getYWRZJH());
			ygdjExport.setYGDJZL(bdcfsql.getYGDJZL());
			// ygdjExport.setYGDJZLMC(bdcfsql.getYGDJZLMC());

	    }
	} else if (xmxx.getQLLX().equals(QLLX.DIYQ.Value)) {
	    dyaqExport.setDYBDCLX(bdcdy.getBDCDYLX().Value);
	    dyaqExport.setDYBDCLXMC(bdcdy.getBDCDYLX().Name);
	    if (bdcdy != null) {
		dyaqExport.setBDCDYID(bdcdy.getId());
		dyaqExport.setBDCDYH(bdcdy.getBDCDYH());
	    }
	    if (bdcql != null) {
		dyaqExport.setQLID(bdcql.getId());
		if(bdcql.getLYQLID() != null && !"".equals(bdcql.getLYQLID())){
			dyaqExport.setLYQLID(bdcql.getLYQLID());
		}
		dyaqExport.setYWH(bdcql.getYWH());
		if(!"".equals(lsywh)){
			cfdjExport.setYWH(lsywh);
		}
		dyaqExport.setBDCDJZMH(bdcql.getBDCQZH());
		dyaqExport.setDJLX(bdcql.getDJLX());
		// dyaqExport.setDJLXMC(bdcql.getDJLXMC());
		dyaqExport.setDJYY(bdcql.getDJYY());
		dyaqExport.setZWLXQSSJ(bdcql.getQLQSSJ());
		dyaqExport.setZWLXJSSJ(bdcql.getQLJSSJ());
		dyaqExport.setFJ(bdcql.getFJ());
		dyaqExport.setQSZT(bdcql.getQSZT());
		// dyaqExport.setQSZTMC(bdcql.getQSZTMC());
	    }
	    if (bdcfsql != null) {
		dyaqExport.setZXYWH(bdcfsql.getZXDYYWH());
		dyaqExport.setDYR(bdcfsql.getDYR());
		dyaqExport.setDYFS(bdcfsql.getDYFS());
		// dyaqExport.setDYFSMC(bdcfsql.getDYFSMC());
		dyaqExport.setZJJZWZL(bdcfsql.getZJJZWZL());
		dyaqExport.setZJJZWDYFW(bdcfsql.getZJJZWDYFW());
		dyaqExport.setBDBZZQSE(bdcfsql.getBDBZZQSE());
		dyaqExport.setZGZQQDSS(bdcfsql.getZGZQQDSS());
		dyaqExport.setZGZQSE(bdcfsql.getZGZQSE());
		dyaqExport.setZXDYYWH(bdcfsql.getZXDYYWH());
		dyaqExport.setZXDYYY(bdcfsql.getZXDYYY());
		dyaqExport.setZXSJ(bdcfsql.getZXSJ());
	    }
	} else if (xmxx.getQLLX().equals(QLLX.DYQ.Value)) {

	}else if (xmxx.getQLLX().equals(QLLX.GYJSYDSHYQ_FWSYQ.Value)
		|| xmxx.getQLLX().equals(QLLX.JTJSYDSYQ_FWSYQ.Value)
		|| xmxx.getQLLX().equals(QLLX.ZJDSYQ_FWSYQ.Value)) {
	    if (bdcdy != null) {
		fdcq2Export.setBDCDYID(bdcdy.getId());
		fdcq2Export.setBDCDYH(bdcdy.getBDCDYH());
		House h = (House) bdcdy;
		if (h != null) {
		    fdcq2Export.setFDZL(h.getZL());
		    fdcq2Export.setTDSYQR(h.getTDSYQR());
		    fdcq2Export.setDYTDMJ(h.getDYTDMJ());
		    fdcq2Export.setFTTDMJ(h.getFTTDMJ());
		    fdcq2Export.setFDCJYJG(h.getFDCJYJG());
		    fdcq2Export.setGHYT(h.getGHYT());
		    // fdcq2Export.setGHYTMC(h.getGHYTMC());
		    fdcq2Export.setFWXZ(h.getFWXZ());
		    // fdcq2Export.setFWXZMC(h.getFWXZMC());
		    fdcq2Export.setFWJG(h.getFWJG());
		    // fdcq2Export.setFWJGMC(h.getFWJGMC());
		    fdcq2Export.setSZC(h.getSZC());
		    fdcq2Export.setZCS(h.getZCS());
		    fdcq2Export.setJZMJ(h.getSCJZMJ());
		    fdcq2Export.setZYJZMJ(h.getDYTDMJ());
		    fdcq2Export.setFTJZMJ(h.getSCFTJZMJ());
		    fdcq2Export.setJGSJ(h.getJGSJ());
		}
	    }
	    if (bdcql != null) {
			fdcq2Export.setQLID(bdcql.getId());
			if(bdcql.getLYQLID() != null && !"".equals(bdcql.getLYQLID())){
				fdcq2Export.setLYQLID(bdcql.getLYQLID());
			}
			fdcq2Export.setYWH(bdcql.getYWH());
			if(!"".equals(lsywh)){
				cfdjExport.setYWH(lsywh);
			}
			fdcq2Export.setQLLX(bdcql.getQLLX());
			fdcq2Export.setQLLXMC(bdcql.getQLLXMC());
			fdcq2Export.setDJLX(bdcql.getDJLX());
			// fdcq2Export.setDJLXMC(bdcql.getDJLXMC());
			fdcq2Export.setDJYY(bdcql.getDJYY());
			fdcq2Export.setTDSYQSSJ(bdcql.getQLQSSJ());
			fdcq2Export.setTDSYJSSJ(bdcql.getQLJSSJ());
			fdcq2Export.setBDCQZH(bdcql.getBDCQZH());
			fdcq2Export.setFJ(bdcql.getFJ());
			fdcq2Export.setQSZT(bdcql.getQSZT());
			fdcq2Export.setDJSJ(bdcql.getDJSJ());		
	    }
	    if(bdcfsql != null){
	    	fdcq2Export.setZXSJ(bdcfsql.getZXSJ());	
	    }
	} else if (xmxx.getQLLX().equals(QLLX.HYSYQ_GZWSYQ.Value)
		|| xmxx.getQLLX().equals(QLLX.WJMHDSYQ_GZWSYQ.Value)) {

	} else if (xmxx.getQLLX().equals(QLLX.HYSYQ.Value)
		|| xmxx.getQLLX().equals(QLLX.WJMHDSYQ.Value)) {

	} else if (xmxx.getQLLX().equals(QLLX.GYJSYDSHYQ.Value)) {
	    if (bdcdy != null) {
		jsydsyqExport.setBDCDYID(bdcdy.getId());
		jsydsyqExport.setBDCDYH(bdcdy.getBDCDYH());
		UseLand shyqzd = (UseLand) bdcdy;
		if (shyqzd != null) {
		    jsydsyqExport.setZDDM(shyqzd.getZDDM());
		    jsydsyqExport.setSYQMJ(shyqzd.getMJ());
		}
	    }
	    if (bdcql != null) {
		jsydsyqExport.setQLID(bdcql.getId());
		jsydsyqExport.setYWH(bdcql.getYWH());
		jsydsyqExport.setQLLX(bdcql.getQLLX());
		jsydsyqExport.setQLLXMC(bdcql.getQLLXMC());
		jsydsyqExport.setDJLX(bdcql.getDJLX());
		// jsydsyqExport.setDJLXMC(bdcql.getDJLXMC());
		jsydsyqExport.setDJYY(bdcql.getDJYY());
		jsydsyqExport.setSYQQSSJ(bdcql.getQLQSSJ());
		jsydsyqExport.setSYQQSSJ(bdcql.getQLJSSJ());
		jsydsyqExport.setQDJG(bdcql.getQDJG());
		jsydsyqExport.setBDCQZH(bdcql.getBDCQZH());
		jsydsyqExport.setFJ(bdcql.getFJ());
		jsydsyqExport.setQSZT(bdcql.getQSZT());
		// jsydsyqExport.setQSZTMC(bdcql.getQSZTMC());
	    }
	}  else if (xmxx.getQLLX().equals(QLLX.LDSYQ.Value)
			|| xmxx.getQLLX().equals(QLLX.LDSYQ_SLLMSYQ.Value)) {

		} else if (xmxx.getQLLX().equals(QLLX.CYSQY.Value)) {

		} else if (xmxx.getQLLX().equals(QLLX.GJTDSYQ.Value)) {
		    if (bdcdy != null) {
			tdsyqExport.setBDCDYID(bdcdy.getId());
			tdsyqExport.setBDCDYH(bdcdy.getBDCDYH());
			OwnerLand syqzd = (OwnerLand) bdcdy;
			if (syqzd != null) {
			    tdsyqExport.setZDDM(syqzd.getZDDM());
			    tdsyqExport.setMJDW(syqzd.getMJDW());
			    // tdsyqExport.setMJDWMC(syqzd.getMJDWMC());
			    tdsyqExport.setNYDMJ(syqzd.getNYDMJ());
			    tdsyqExport.setGDMJ(syqzd.getGDMJ());
			    tdsyqExport.setLDMJ(syqzd.getLDMJ());
			    tdsyqExport.setCDMJ(syqzd.getCDMJ());
			    tdsyqExport.setQTNYDMJ(syqzd.getQTNYDMJ());
			    tdsyqExport.setJSYDMJ(syqzd.getJSYDMJ());
			    tdsyqExport.setWLYDMJ(syqzd.getWLYDMJ());
			}
		    }
		    if (bdcql != null) {
			tdsyqExport.setQLID(bdcql.getId());
			tdsyqExport.setYWH(bdcql.getYWH());
			tdsyqExport.setQLLX(bdcql.getQLLX());
			tdsyqExport.setQLLXMC(bdcql.getQLLXMC());
			tdsyqExport.setDJLX(bdcql.getDJLX());
			// tdsyqExport.setDJLXMC(bdcql.getDJLXMC());
			tdsyqExport.setDJYY(bdcql.getDJYY());
			tdsyqExport.setBDCQZH(bdcql.getBDCQZH());
			tdsyqExport.setFJ(bdcql.getFJ());
			tdsyqExport.setQSZT(bdcql.getQSZT());
			// tdsyqExport.setQSZTMC(bdcql.getQSZTMC());
		    }

		} else if (xmxx.getQLLX().equals(DJLX.YYDJ.Value)) {
		    if (bdcdy != null) {
			yydjExport.setBDCDYID(bdcdy.getId());
			yydjExport.setQLID(bdcql.getId());
			yydjExport.setBDCDYH(bdcdy.getBDCDYH());
		    }
		    if (bdcql != null) {
			yydjExport.setYWH(bdcql.getYWH());
			yydjExport.setYYSX(bdcfsql.getYYSX());
			yydjExport.setBDCDJZMH(bdcql.getBDCQZH());
			yydjExport.setZXYYYWH(bdcfsql.getZXDYYWH());
			yydjExport.setZXYYYY(bdcfsql.getZXYYYY());
			// yydjExport.setZXYYDBR(bdcfsql.getZXDBR());
			yydjExport.setZXYYDJSJ(bdcfsql.getZXSJ());
			yydjExport.setFJ(bdcql.getFJ());
			yydjExport.setQSZT(bdcql.getQSZT());
			// yydjExport.setQSZTMC(bdcql.getQSZTMC());
		    }
		} 

	qlExport.add(cfdjExport);
	qlExport.add(dyaqExport);
	qlExport.add(dyiqExport);
	qlExport.add(fdcq1Export);
	qlExport.add(fdcq2Export);
	qlExport.add(fdcq3Export);
	qlExport.add(gzwsyqExport);
	qlExport.add(hysyqExport);
	qlExport.add(jsydsyqExport);
	qlExport.add(lqExport);
	qlExport.add(nydsyqExport);
	qlExport.add(qtxgqlExport);
	qlExport.add(tdsyqExport);
	qlExport.add(ygdjExport);
	qlExport.add(yydjExport);
	return qlExport;
    }
   
    private Object GetFDCQ(BDCS_XMXX xmxx, Rights bdcql, RealUnit bdcdy){
    	FDCQ2Export fdcq2Export = new FDCQ2Export();
    	if (xmxx.getQLLX().equals(QLLX.GYJSYDSHYQ_FWSYQ.Value)
			|| xmxx.getQLLX().equals(QLLX.JTJSYDSYQ_FWSYQ.Value)
			|| xmxx.getQLLX().equals(QLLX.ZJDSYQ_FWSYQ.Value)) {
		    if (bdcdy != null) {
			fdcq2Export.setBDCDYID(bdcdy.getId());
			fdcq2Export.setBDCDYH(bdcdy.getBDCDYH());
			House h = (House) bdcdy;
			if (h != null) {
			    fdcq2Export.setFDZL(h.getZL());
			    fdcq2Export.setTDSYQR(h.getTDSYQR());
			    fdcq2Export.setDYTDMJ(h.getDYTDMJ());
			    fdcq2Export.setFTTDMJ(h.getFTTDMJ());
			    fdcq2Export.setFDCJYJG(h.getFDCJYJG());
			    fdcq2Export.setGHYT(h.getGHYT());
			    // fdcq2Export.setGHYTMC(h.getGHYTMC());
			    fdcq2Export.setFWXZ(h.getFWXZ());
			    // fdcq2Export.setFWXZMC(h.getFWXZMC());
			    fdcq2Export.setFWJG(h.getFWJG());
			    // fdcq2Export.setFWJGMC(h.getFWJGMC());
			    fdcq2Export.setSZC(h.getSZC());
			    fdcq2Export.setZCS(h.getZCS());
			    fdcq2Export.setJZMJ(h.getSCJZMJ());
			    fdcq2Export.setZYJZMJ(h.getDYTDMJ());
			    fdcq2Export.setFTJZMJ(h.getSCFTJZMJ());
			    fdcq2Export.setJGSJ(h.getJGSJ());
			}
		    }
		    if (bdcql != null) {
			fdcq2Export.setQLID(bdcql.getId());
			fdcq2Export.setYWH(bdcql.getYWH());
			fdcq2Export.setQLLX(bdcql.getQLLX());
			fdcq2Export.setQLLXMC(bdcql.getQLLXMC());
			fdcq2Export.setDJLX(bdcql.getDJLX());
			// fdcq2Export.setDJLXMC(bdcql.getDJLXMC());
			fdcq2Export.setDJYY(bdcql.getDJYY());
			fdcq2Export.setTDSYQSSJ(bdcql.getQLQSSJ());
			fdcq2Export.setTDSYJSSJ(bdcql.getQLJSSJ());
			fdcq2Export.setBDCQZH(bdcql.getBDCQZH());
			fdcq2Export.setFJ(bdcql.getFJ());
			fdcq2Export.setQSZT(bdcql.getQSZT());
			// fdcq2Export.setQSZTMC(bdcql.getQSZTMC());
		    }
    	}
    	return fdcq2Export;
    }
    
    @SuppressWarnings({ "rawtypes" })
    /**
     * 
     * @作者 胡加红 查询证书编号
     * @创建时间 2015年12月8日下午10:42:35
     * @param qlrid
     * @return
     */
	private String GetZSBHByQLRID(String qlid,String qlrmc){
    	String zsbh="";
    	if((qlid == null || "".equals(qlid)) && (qlrmc == null || "".equals(qlrmc))){
    		return zsbh;
    	}
    	try{
    		StringBuilder sb = new StringBuilder();
    		
    		sb.append("SELECT a.zsbh FROM bdck.bdcs_zs_gz a LEFT JOIN bdck.bdcs_qdzr_gz b ")
    		.append(" ON a.zsid=b.zsid WHERE a.zsbh IS NOT NULL AND b.qlrid in (")
    		.append("SELECT qlrid FROM bdck.bdcs_qlr_xz c LEFT JOIN bdck.bdcs_ql_xz d ON c.qlid=d.qlid WHERE c.qlrmc='")
    		.append(qlrmc)
    		.append("' AND d.qlid='")
    		.append(qlid).append("')");
    		List<Map> maps = commonDao.getDataListByFullSql(sb.toString());
    		if (maps.size() > 0) {
    			Map map = (Map) maps.get(0);
    			if( map.get("ZSBH") != null)
    				zsbh = map.get("ZSBH").toString();
    		}
    	}catch(Exception e){
    		zsbh="";
    	}
    	return zsbh;
    }
    private QLRExport GetBDCQLRExport(RightsHolder bdcqlr) {
	QLRExport qlrExport = new QLRExport();
	if (bdcqlr != null) {
		
	    qlrExport.setBDCQZH(bdcqlr.getBDCQZH());
	    qlrExport.setDLRXM(bdcqlr.getDLRXM());
	    qlrExport.setDLRLXDH(bdcqlr.getDLRLXDH());
	    qlrExport.setDLRZJHM(bdcqlr.getDLRZJHM());
	    qlrExport.setDLRZJLX(bdcqlr.getDLRZJLX());
	    // qlrExport.setDLRZJLXMC(bdcqlr.getDLRZJLXMC());
	    qlrExport.setDLJGMC(bdcqlr.getDLJGMC());
	    qlrExport.setGYQK(bdcqlr.getGYQK());
	    qlrExport.setGYFS(bdcqlr.getGYFS());
	    // qlrExport.setGYFSMC(bdcqlr.getGYFSMC());
	    qlrExport.setFZJG(bdcqlr.getFZJG());
	    qlrExport.setGJ(bdcqlr.getGJ());
	    // qlrExport.setGJMC(bdcqlr.getGJMC());
	    qlrExport.setDZ(bdcqlr.getDZ());
	    qlrExport.setBZ(bdcqlr.getBZ());
	    qlrExport.setGZDW(bdcqlr.getGZDW());
	    qlrExport.setXB(bdcqlr.getXB());
	    // qlrExport.setXBMC(bdcqlr.getXBMC());
	    qlrExport.setHJSZSS(bdcqlr.getHJSZSS());
	    // qlrExport.setHJSZSSMC(bdcqlr.getHJSZSSMC());
	    qlrExport.setSSHY(bdcqlr.getSSHY());
	    // qlrExport.setSSHYMC(bdcqlr.getSSHYMC());
	    qlrExport.setYXBZ(bdcqlr.getYXBZ());
	    qlrExport.setQLID(bdcqlr.getQLID());
	    qlrExport.setQLRID(bdcqlr.getId());
	    qlrExport.setQLRMC(bdcqlr.getQLRMC());
	    qlrExport.setQLRLX(bdcqlr.getQLRLX());
	    // qlrExport.setQLRLXMC(bdcqlr.getQLRLXMC());
	    qlrExport.setQLBL(bdcqlr.getQLBL());
	    qlrExport.setQLMJ(bdcqlr.getQLMJ());
	    qlrExport.setFDDBR(bdcqlr.getFDDBR());
	    qlrExport.setFDDBRDH(bdcqlr.getFDDBRDH());
	    qlrExport.setFDDBRZJHM(bdcqlr.getFDDBRZJHM());
	    qlrExport.setFDDBRZJLX(bdcqlr.getFDDBRZJLX());
	    // qlrExport.setFDDBRZJLXMC(bdcqlr.getFDDBRZJLXMC());
	    qlrExport.setSQRID(bdcqlr.getSQRID());
	    qlrExport.setDZYJ(bdcqlr.getDZYJ());
	    qlrExport.setDH(bdcqlr.getDH());
	    qlrExport.setZJH(bdcqlr.getZJH());
	    qlrExport.setZJZL(bdcqlr.getZJZL());
	    qlrExport.setDCXMID(bdcqlr.getDCXMID());
	    qlrExport.setYB(bdcqlr.getYB());
	    qlrExport.setXMBH(bdcqlr.getXMBH());
	    qlrExport.setSXH(bdcqlr.getSXH());
	    String zsbh = GetZSBHByQLRID(bdcqlr.getQLID(),bdcqlr.getQLRMC());
	    if(!"".equals(zsbh)){//增加打证时证书编号导出
	    	qlrExport.setZSBH(zsbh);
	    }
	}
	return qlrExport;
    }
    
    //查询变更前的权利人，登薄以后需要去权利人历史层查找
	@SuppressWarnings("unchecked")
	private List<RightsHolder> GetBGQQlr(String djdyid,DJDYLY dyly){
    	List<RightsHolder> qlrs = new ArrayList<RightsHolder>();
    	try{
	    	String _entityClassName = EntityTools.getEntityName("BDCS_QLR", dyly);
	    	String _qlentityClassName = EntityTools.getEntityName("BDCS_QL", dyly);
	    	StringBuilder sb = new StringBuilder();
	    	sb.append(" select * from bdck.").append( _entityClassName )
	    	.append(" where qlid IN( SELECT QLID FROM bdck.").append(_qlentityClassName)  
		    .append( "  WHERE DJDYID='").append( djdyid).append( "' ) ORDER BY SXH");
			Class<?> c = EntityTools.getEntityClass(_entityClassName);
			qlrs =(List<RightsHolder>) commonDao.getDataList(c, sb.toString());
    	}catch(Exception e){
    		e.printStackTrace();
    		qlrs = null;
    	}
    	return qlrs;
    }
    
    public MessageExport GetMsg(RealUnit bdcdy, Rights bdcql,
	    SubRights bdcfsql, List<RightsHolder> bdcqlrs, String bljc, BDCS_XMXX _xmxx) {
	MessageExport msg = new MessageExport();
	//首先判断单元类型，如果不是户，就不需要发送消息给房产了
	if (bdcdy != null) {
	    if (!bdcdy.getBDCDYLX().equals(BDCDYLX.H) && !bdcdy.getBDCDYLX().equals(BDCDYLX.YCH)) {
	    	return msg=null;
	    }
	}else{
		return msg=null;
	}
	DataExport dataExport = new DataExport();
	List<Object> bdcdyExport = GetBDCDYExport(bdcdy);
	List<Object> bdcqlExport = GetBDCQLExport(_xmxx, bdcql, bdcfsql, bdcdy);
	List<Object> bdcqlrsExport = new ArrayList<Object>();
	if (bdcqlrs != null && bdcqlrs.size() > 0) {
	    for (int iqlr = 0; iqlr < bdcqlrs.size(); iqlr++) {
		QLRExport qlrExport = GetBDCQLRExport(bdcqlrs.get(iqlr));
		bdcqlrsExport.add(qlrExport);
	    }
	} else {
	    QLRExport qlrExport = new QLRExport();
	    bdcqlrsExport.add(qlrExport);
	}
	dataExport.setBDCDY(bdcdyExport);
	dataExport.setBDCQL(bdcqlExport);
	dataExport.setBDCQLRS(bdcqlrsExport);
	HTNRExport htnrExport = new HTNRExport();
	List<Object> sfrsExport = new ArrayList<Object>();
	SFRExport sfrExport = new SFRExport();
	sfrsExport.add(sfrExport);
	dataExport.setHtnr(htnrExport);
	dataExport.setSFRS(sfrsExport);
//	if(IsCertificated(_xmxx.getId())){//如果已经发证，需要修改bljc=9
//		bljc = "9";
//	}
	HeadExport headExport = CreateSendMsgHead(_xmxx,bljc, bdcdy.getBDCDYH(),
		bdcdy.getId(), bdcdy.getRELATIONID());
	msg.setHead(headExport);
	msg.setData(dataExport);
	return msg;
    }
  
    /**
     * 变更数据导出
     * @作者 胡加红
     * @创建时间 2015年12月13日下午2:25:37
     * @param _xmxx 
     * @param bljc 办理进程
     * @return
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public BGMessageExport GetMsg(BDCS_XMXX xmxx,List<BDCS_DJDY_GZ> djdys,String bljc){
    	BGMessageExport bgmsg = new BGMessageExport();
    	HeadExport headExport = new HeadExport();
    	BGDataExport bgqData = new BGDataExport();
    	BGDataExport bghData = new BGDataExport();	
    	BGBDCDYExport bgqdy = new BGBDCDYExport(); 
    	BGBDCDYExport bghdy = new BGBDCDYExport(); 
    	List<QLRExport> bgqqlr = new ArrayList<QLRExport>(); 
    	List<QLRExport> bghqlr = new ArrayList<QLRExport>();
    	List<Object> bgqql = new ArrayList(); 
    	List<Object> bghql = new ArrayList(); 
    	List<HExport> bgq_hs = new ArrayList<HExport>();
    	List<HExport> bgh_hs = new ArrayList<HExport>();
    	ZRZExport bgq_zrz = new ZRZExport();
    	ZRZExport bgh_zrz = new ZRZExport();
	    if (djdys != null && djdys.size() > 0) {
			for (int idjdy = 0; idjdy < djdys.size(); idjdy++) {
			    try {
			    	boolean isLast = false;//是否上一手
					BDCS_DJDY_GZ djdy = djdys.get(idjdy);
					if (xmxx.getSFDB().equals(SFDB.YES.Value)) {
					    bljc = "3";
					}
					if(IsCertificated(xmxx.getId())){//如果已经发证，需要修改bljc=9
						bljc = "9";
					}
					//对于分割合并来说，分割前对于的来源是XZ，分割后的来源是GZ，根据这个区分上下手关系
			    	//登薄以后，分割前数据在现状层被注销，需要到历史层取数据
					ConstValue.BDCDYLX dylx = ConstValue.BDCDYLX.initFrom(djdy.getBDCDYLX());
					ConstValue.DJDYLY dyly = ConstValue.DJDYLY.initFrom(djdy.getLY());
					if(dyly.equals(ConstValue.DJDYLY.XZ)){
						isLast=true;
						if(!bljc.equals("1")){
							dyly = DJDYLY.LS;
						}
					}
					RealUnit bdcdy = UnitTools.loadUnit(dylx, dyly,djdy.getBDCDYID());
					Rights bdcql = RightsTools.loadRightsByDJDYID(dyly, djdy.getXMBH(), djdy.getDJDYID());
					List<RightsHolder> bdcqlrs = new ArrayList<RightsHolder>();
					if(!dyly.equals(DJDYLY.LS)){//变更在登薄以后要直接去历史权利人里面查找
						bdcqlrs = RightsHolderTools.loadRightsHolders(dyly,djdy.getDJDYID(), djdy.getXMBH());
					}else{
						bdcqlrs =  GetBGQQlr(djdy.getDJDYID(),dyly);
					}
					HExport hExport = GetBGHouseExport(bdcdy);
					String zrzbdcdyid="";
					if(bdcdy != null){
						House h =(House)bdcdy;
						zrzbdcdyid = h.getZRZBDCDYID();
					}
					ZRZExport zrzExport = GetBGBuildExport(zrzbdcdyid);
					Object fdcqxport = GetFDCQ(xmxx, bdcql, bdcdy);
					List<QLRExport> bdcqlrsExport = new ArrayList<QLRExport>();
					if (bdcqlrs != null && bdcqlrs.size() > 0) {
					    for (int iqlr = 0; iqlr < bdcqlrs.size(); iqlr++) {
						QLRExport qlrExport = GetBDCQLRExport(bdcqlrs.get(iqlr));
						bdcqlrsExport.add(qlrExport);
					    }
					} else {
					    QLRExport qlrExport = new QLRExport();
					    bdcqlrsExport.add(qlrExport);
					}
					if(isLast){
						bgq_hs.add(hExport);
						bgq_zrz = zrzExport;
						bgqql.add(fdcqxport);
						bgqqlr.addAll(bdcqlrsExport);
					}else{
						bgh_hs.add(hExport);
						bgh_zrz = zrzExport;
						bghql.add(fdcqxport);
						bghqlr.addAll(bdcqlrsExport);
					}
			    } catch (Exception ex) {
			    	ex.printStackTrace();
			    }
			}
	    }
	    headExport = BGCreateSendMsgHead(xmxx,bljc);
	    //变更前
	    bgqdy.setC( new ArrayList<CExport>());
	    bgqdy.setLJZ( new ArrayList<LJZExport>());
	    bgqdy.setH(bgq_hs);
	    bgqdy.setZRZ(bgq_zrz);
	    bgqData.setBDCDY(bgqdy);
	    bgqData.setBDCQL(bgqql);
	    bgqData.setBDCQLRS(bgqqlr);
	    //变更后
	    bghdy.setC( new ArrayList<CExport>());
	    bghdy.setLJZ( new ArrayList<LJZExport>());
	    bghdy.setH(bgh_hs);
	    bghdy.setZRZ(bgh_zrz);
	    bghData.setBDCDY(bghdy);
	    bghData.setBDCQL(bghql);
	    bghData.setBDCQLRS(bghqlr);
	    bgmsg.setHead(headExport);
	    bgmsg.setBGQData(bgqData);
	    bgmsg.setBGHData(bghData);
	   
	    bgmsg.setHead(headExport);
    	return bgmsg;
    }
    /**
     * 调用协同系统日志服务
     * 
     * @作者 OuZhanrong
     * @创建时间 2015年9月18日下午9:56:45
     * @return *
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     */
    private String saveLog(String LOG_ID, String discription, boolean isSuccess,
	    ShareMessage shareMesage ) {
	// 获取协同保存Log地址
	String xtServiceURL = ConfigHelper.getNameByValue("JYDJ_CONTRACTURL");//改为从配置表读取 刘树峰2015年12月26日2点
	String saveLogURL = xtServiceURL + "saveLog";
	String shareMessageStr = JSON.toJSONString(shareMesage);
	String param = "logID=" + LOG_ID + "&content=" + discription
		+ "&success=" + isSuccess + "&shareMessage=" + shareMessageStr;
	String result = HttpRequestTools.sendPost(saveLogURL, param);
	List<String> list = JSONObject.parseArray(result, String.class);
	if (list.size() > 0) {
	    return list.get(0);
	}
	return null;
    }

    /**
     * 检查出错的消息，并重新发送,发送成功的更新状态
     * 
     * @作者 胡加红
     * @创建时间 2015年9月19日下午3:21:41
     * @return
     */
    public void GetErrMsgAndSend() {
	String xtServiceURL = ConfigHelper.getNameByValue("JYDJ_CONTRACTURL");//改为从配置表读取 刘树峰2015年12月26日2点
	String checkLogURL = xtServiceURL + "checkLog";
	String result = HttpRequestTools.sendPost(checkLogURL, "");

	List<BDCS_LOG> logs = JSONObject.parseArray(result, BDCS_LOG.class);
	if (logs != null) {
	    for (BDCS_LOG log : logs) {
		reSendMsg(log);
	    }
	}

    }

    /**
     * 消息重发
     * 
     * @作者 胡加红
     * @创建时间 2015年9月19日下午4:21:17
     * @param msg
     * @return
     */
    private void reSendMsg(BDCS_LOG log) {
	String LOG_ID = "";
	ShareMessage msg = null;
	try {
	    if (log != null) {
		String filePath = GetProperties.getConstValueByKey("xmlPath")
			+ log.getFILENAME();
		File temp = new File(filePath);
		msg = createSendMsg("", log.getCOMMENTS(), log.getSTAFF(),
			log.getFILENAME(), log.getPROJECT_ID(),
			log.getFILEPATH(), "");
		msg.setLogid(log.getId());
		msg.setbTansmited(true);
		if (temp.exists()) {
		    String sftpPath = uploadFile(filePath);
		    if (sftpPath == null || "".equals(sftpPath)) {
			saveLog(LOG_ID, "登记中心重新上传文件失败！", false, msg);
		    } else {
			msg.setSftpFilePath(sftpPath);
			saveLog(LOG_ID, "登记中心重新上传文件成功！", true, msg);
			mqSender.sendMessage(msg);
		    }
		} else {
		    saveLog(LOG_ID,"文件" + msg.getSftpFileName() + "没找到！", false, msg);
		}
	    }
	} catch (Exception ex) {
	    saveLog(LOG_ID,ex.getMessage(), false, msg);
	}
    }
    /**
     * 
     * @作者 胡加红
     * 由于不能解决协同转发时内存释放问题，跟房产大数据量对接时，先生成xml文件，手动发给房产
     * 等内存释放问题解决以后再去掉此方法
     * @创建时间 2015年11月22日下午6:29:45
     * @param msg
     * @param djdyIndex
     * @param bdcdylx
     * @param _xmxx
     */
    public void NoSendMsg(MessageExport msg, int djdyIndex, String bljc,
    	    BDCS_XMXX _xmxx) {
    	String LOG_ID = "";

    	// 根据项目编号获取项目中登记集合
    	String filePath = createClassifyXML(_xmxx, msg, djdyIndex,bljc);
    	// 上传共享文件

    	// 获取共享文件路径
    	String sftpFileName = getXMLFileName(filePath);
    	// 创建共享信息

        }
    
    /**
     * 只生成变更xml文件，不上传、不发送消息,批量上传的打包给房产
     * @作者 Administrator
     * @创建时间 2015年12月19日下午3:41:36
     * @param msg
     * @param djdyIndex
     * @param bdcdylx
     * @param _xmxx
     */
    public void BGNoSendMsg(BGMessageExport msg, int djdyIndex, BDCS_XMXX _xmxx,String bljc) {
    	String LOG_ID = "";
    	if(msg == null){
    		return;
    	}

    	// 根据项目编号获取项目中登记集合
    	String filePath = createClassifyBGXML(_xmxx, msg, djdyIndex,bljc);
    	// 上传共享文件
    	//String sftpPath = uploadFile(filePath);

    	// 获取共享文件路径
    	String sftpFileName = getXMLFileName(filePath);

        }
     
    /**
     * 
     * @作者 胡加红
     * @创建时间 2015年9月18日下午9:33:35 BDCS_XMXX存量数据发送消息
     * 存量数据同步修改为根据受理时间来同步数据
     */
    public void SendAllExistMsg(String qsj,String zsj) {
	// 查询所有xmxx
    	StringBuilder build = new StringBuilder( " 1=1 ");

    	if(!"".endsWith(qsj) && !"".endsWith(zsj)){
    		build.append(" and to_char(slsj,'yyyy-mm-dd') between '")
    			 .append(qsj).append("' and '").append(zsj)
    			 .append("' order by slsj asc");//一定要按照受理时间排序，不然多笔关联业务发给房产，关系都乱了
    	}
    	List<BDCS_XMXX> xmxxs = commonDao.findList(BDCS_XMXX.class,build.toString());
    	int fileIndex=100000;//xml文件排序用，初始值100000，后面递增
		for (BDCS_XMXX xmxx : xmxxs) {// 挨个xmxx都发送消息
		    String xmbh = xmxx.getId();
		    String xmbhFilter = ProjectHelper.GetXMBHCondition(xmxx.getId());
		    //发存量数据时只发送房产业务数据;
		    xmbhFilter += " and(bdcdylx='031' OR bdcdylx='032') ";
		    List<BDCS_DJDY_GZ> djdys = commonDao.findList(BDCS_DJDY_GZ.class,
			    xmbhFilter);
		    if (djdys != null && djdys.size() > 0) {
		    	boolean isChangeProcess = false;//是否分割、合并流程
		    	if(djdys.size()>1){//只有一个单元的就不用判断了
		    		isChangeProcess = IsChangeProcess(xmbh);
		    	}
		    	String bljc = "1";
				if (xmxx.getSFDB().equals(SFDB.YES.Value)) {
				    bljc = "3";
				}
		    	if(isChangeProcess){
		    		fileIndex++;
		    		BGMessageExport bg = GetMsg(xmxx, djdys, "1");
		    		BGNoSendMsg(bg, fileIndex, xmxx, bljc);
		    	}else{
		    		
					for (int idjdy = 0; idjdy < djdys.size(); idjdy++) {
					    try {		
							BDCS_DJDY_GZ djdy = djdys.get(idjdy);
							ConstValue.BDCDYLX dylx = ConstValue.BDCDYLX.initFrom(djdy.getBDCDYLX());
							ConstValue.DJDYLY dyly = ConstValue.DJDYLY.initFrom(djdy.getLY());
							RealUnit bdcdy = UnitTools.loadUnit(dylx, dyly,	djdy.getBDCDYID());							
							if(bdcdy == null ){//如果根据登记单元工作里面的来源找不到，就去历史层找，此单元有可能办理过注销或者变更业务
								bdcdy = UnitTools.loadUnit(dylx, ConstValue.DJDYLY.LS,	djdy.getBDCDYID());	
							}
							Rights bdcql = RightsTools.loadRightsByDJDYID(ConstValue.DJDYLY.GZ, xmbh, djdy.getDJDYID());
							SubRights bdcfsql = null; 
							if(bdcql != null){
								bdcfsql = RightsTools.loadSubRightsByRightsID(ConstValue.DJDYLY.GZ,bdcql.getId());
							}
							List<RightsHolder> bdcqlrs = RightsHolderTools
								.loadRightsHolders(ConstValue.DJDYLY.GZ,djdy.getDJDYID(), xmbh);
							
							if(IsCertificated(xmxx.getId())){//如果已经发证，需要修改bljc=9
								bljc = "9";
							}
							fileIndex++;
							MessageExport msg = GetMsg(bdcdy, bdcql, bdcfsql,bdcqlrs, bljc, xmxx);
							NoSendMsg(msg, fileIndex, bljc, xmxx);
			
					    } catch (Exception ex) {
					    	ex.printStackTrace();
					    }
					}
		    	}
		    }
		}
    }
    /**
     * 
     * @作者 胡加红 打印证书时，单独发送此证书对应的单元信息
     * @创建时间 2016年5月21日上午10:41:45
     * @param zsid
     * @param xmbh
     */
    public void PrintZSSendMsg(String xmbh,String zsid) {
    	// 查询所有xmxx
    	StringBuilder build = new StringBuilder( " 1=1 ");
    	BDCS_XMXX xmxx = commonDao.get(BDCS_XMXX.class, xmbh);
    	Integer fileIndex=1;
	    String xmbhFilter = ProjectHelper.GetXMBHCondition(xmbh);
	    //发存量数据时只发送房产业务数据;
	    xmbhFilter += " and(bdcdylx='031' OR bdcdylx='032') ";
	    List<BDCS_DJDY_GZ> djdys = commonDao.findList(BDCS_DJDY_GZ.class, xmbhFilter);
	    if (djdys != null && djdys.size() > 0) {
	    	boolean isChangeProcess = false;//是否分割、合并流程
	    	if(djdys.size()>1){//只有一个单元的就不用判断了
	    		isChangeProcess = IsChangeProcess(xmbh);
	    	}
	    	String bljc = "9";
			String m_djdyid=SearchDjdyidByZsid(zsid);
	    	if(isChangeProcess){    		    		
	    		BGMessageExport bg = GetMsg(xmxx, djdys, "1");
	    		BGSendMsg(bg, fileIndex, bljc, xmxx);
	    	}else{	    		
				for (int idjdy = 0; idjdy < djdys.size(); idjdy++) {
				    try {		
						BDCS_DJDY_GZ djdy = djdys.get(idjdy);
						if(!m_djdyid.equals(djdy.getDJDYID())){//如果不是这个证书对应的djdyid，就不生成文件
							continue;
						}
						ConstValue.BDCDYLX dylx = ConstValue.BDCDYLX.initFrom(djdy.getBDCDYLX());
						ConstValue.DJDYLY dyly = ConstValue.DJDYLY.initFrom(djdy.getLY());
						RealUnit bdcdy = UnitTools.loadUnit(dylx, dyly,	djdy.getBDCDYID());							
						if(bdcdy == null ){//如果根据登记单元工作里面的来源找不到，就去历史层找，此单元有可能办理过注销或者变更业务
							bdcdy = UnitTools.loadUnit(dylx, ConstValue.DJDYLY.LS,	djdy.getBDCDYID());	
						}
						Rights bdcql = RightsTools.loadRightsByDJDYID(ConstValue.DJDYLY.GZ, xmbh, djdy.getDJDYID());
						SubRights bdcfsql = null; 
						if(bdcql != null){
							bdcfsql = RightsTools.loadSubRightsByRightsID(ConstValue.DJDYLY.GZ,bdcql.getId());
						}
						List<RightsHolder> bdcqlrs = RightsHolderTools
							.loadRightsHolders(ConstValue.DJDYLY.GZ,djdy.getDJDYID(), xmbh);
						
						fileIndex++;
						MessageExport msg = GetMsg(bdcdy, bdcql, bdcfsql,bdcqlrs, bljc, xmxx);
						SendMsg(msg, fileIndex, bljc, xmxx);
		
				    } catch (Exception ex) {
				    	ex.printStackTrace();
				    }
				}
	    	}
	    }
    		
    }
    /**
     * 
     * @作者 胡加红  检查证书工作层的证书编号是否为空，非空代表已经打证
     * @创建时间 2015年12月9日下午2:27:06
     * @param xmbh 项目编号
     * @return
     */
    @SuppressWarnings("rawtypes")
	private Boolean IsCertificated(String xmbh){
    	Boolean isCert=false;
    	if((xmbh == null || "".equals(xmbh))){
    		return isCert;
    	}
    	try{
    		StringBuilder sb = new StringBuilder();
    		
    		sb.append("SELECT zsbh FROM bdck.bdcs_zs_gz ").append(" WHERE xmbh='")
    		.append(xmbh).append("' and zsbh is not null ");
    		List<Map> maps = commonDao.getDataListByFullSql(sb.toString());
    		if (maps.size() > 0) {
    			Map map = (Map) maps.get(0);
    			if(map.get("ZSBH") != null && !"".equals(map.get("ZSBH").toString())){
    				isCert=true;
    			}
    		}
    	}catch(Exception e){
    		isCert=false;
    	}
    	return isCert;
    }
    @SuppressWarnings({ "rawtypes"})
   	private String SearchDjdyidByZsid(String zsid){
    	String djdyid="";
       	if((zsid == null || "".equals(zsid))){
       		return djdyid;
       	}
       	try{
       		StringBuilder sb = new StringBuilder();
       		
       		sb.append("SELECT djdyid FROM bdck.bdcs_zs_gz a left join bdck.bdcs_qdzr_gz b on a.zsid=b.zsid ").append(" WHERE a.zsid='")
       		.append(zsid).append("'");
       		List<Map> maps = commonDao.getDataListByFullSql(sb.toString());
       		if (maps.size() > 0) {
       			Map map = (Map) maps.get(0);
       			if(map.get("DJDYID") != null && !"".equals(map.get("DJDYID").toString())){
       				djdyid=map.get("DJDYID").toString();
       			}
       		}
       	}catch(Exception e){
       		djdyid="";
       	}
       	return djdyid;
       }
    /**
     * 
     * @作者 胡加红 如果一个项目的登记单元中存在不同来源，就认为是变更流程
     * @创建时间 2015年12月19日下午3:30:29
     * @param xmbh
     * @return
     */
    private Boolean IsChangeProcess(String xmbh){
    	Boolean isCert=false;
    	if((xmbh == null || "".equals(xmbh))){
    		return isCert;
    	}
    	try{
    		StringBuilder sb = new StringBuilder();
    		
    		sb.append("SELECT COUNT(id),ly FROM bdck.bdcs_djdy_gz  ").append(" WHERE xmbh='")
    		.append(xmbh).append("' GROUP BY ly ");
    		List<Map> maps = commonDao.getDataListByFullSql(sb.toString());
    		if (maps.size() > 1) {//如果一个项目的登记单元中存在不同来源，就认为是变更流程
				isCert=true;
    		}
    	}catch(Exception e){
    		isCert=false;
    	}
    	return isCert;    	
    }
    /**
     * 
     * @作者 胡加红 注销登记  获取来源权利的的YWH，房产根据此YWH关联老权利
     * @创建时间 2016年2月2日下午6:52:43
     * @param lyqlid
     * @return
     */
	@SuppressWarnings("rawtypes")
	private String GetLastYWH(String lyqlid){
    	String ywh="";
    	if((lyqlid == null || "".equals(lyqlid))){
    		return ywh;
    	}
    	try{
    		StringBuilder sb = new StringBuilder();
    		//历史现状一起查，如果现状存在就取现状的ywh，否则取历史的ywh
    		sb.append("SELECT ywh FROM bdck.bdcs_ql_xz ").append(" WHERE qlid='")
    		.append(lyqlid).append("' and ywh is not null union SELECT ywh FROM bdck.bdcs_ql_ls ")
    		.append(" WHERE qlid='").append(lyqlid).append("' and ywh is not null");
    		List<Map> maps = commonDao.getDataListByFullSql(sb.toString());
    		if (maps.size() > 0) {
    			Map map = (Map) maps.get(0);
    			if(map.get("YWH") != null && !"".equals(map.get("YWH").toString())){
    				ywh=map.get("YWH").toString();
    			}
    		}
    	}catch(Exception e){
    		ywh="";
    	}
    	return ywh;
    }
}
