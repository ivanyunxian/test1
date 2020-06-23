package com.supermap.realestate.registration.service.Sender;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jcraft.jsch.ChannelSftp;
import com.supermap.realestate.registration.model.BDCS_DJDY_GZ;
import com.supermap.realestate.registration.model.BDCS_FSQL_GZ;
import com.supermap.realestate.registration.model.BDCS_QLR_GZ;
import com.supermap.realestate.registration.model.BDCS_QL_GZ;
import com.supermap.realestate.registration.model.BDCS_QL_LS;
import com.supermap.realestate.registration.model.BDCS_XMXX;
import com.supermap.realestate.registration.model.interfaces.RealUnit;
import com.supermap.realestate.registration.model.xmlmodel.BDCQLR;
import com.supermap.realestate.registration.model.xmlmodel.DataW;
import com.supermap.realestate.registration.model.xmlmodel.HTNR;
import com.supermap.realestate.registration.model.xmlmodel.Head;
import com.supermap.realestate.registration.model.xmlmodel.MessageW;
import com.supermap.realestate.registration.model.xmlmodel.SFR;
import com.supermap.realestate.registration.model.xmlmodel.XmlDYLX;
import com.supermap.realestate.registration.model.xmlmodel.XmlQLLX;
import com.supermap.realestate.registration.model.xmlmodel.bdcql.DYAQ;
import com.supermap.realestate.registration.tools.UnitTools;
import com.supermap.realestate.registration.util.ConfigHelper;
import com.supermap.realestate.registration.util.ConstValue;
import com.supermap.realestate.registration.util.Global;
import com.supermap.realestate.registration.util.ProjectHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.realestate.registration.util.SystemConfig;
import com.supermap.realestate.registration.web.DBController;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import com.thoughtworks.xstream.XStream;

@Component("swapService")
public class SwapService {

	@Autowired
	private CommonDao baseCommonDao;

	@Autowired
	private MQSender mqSender;

	private String bljc = "";

	private BDCS_XMXX xmxx = null;

	private int djdyIndex = 0;

	public void sendMsgByProjectID(String projectID, String bljc) {
		List<BDCS_XMXX> xmxxs = baseCommonDao.getDataList(BDCS_XMXX.class,
				"PROJECT_ID = '" + projectID + "'");
		if (xmxxs.size() > 0) {
			this.xmxx = xmxxs.get(0);
			sendMsg(this.xmxx.getId(), bljc);
		}
	}

	/**
	 * 登簿成功后获取登记信息，并共享
	 * 
	 * @作者 俞学斌
	 * @创建时间 2015年8月9日下午2:20:50
	 * @param xmbh
	 *            项目编号
	 * @param bljc
	 *            不知道是什么属性
	 */
	public void sendMsg(String xmbh, String bljc) {
		djdyIndex = 0;
		this.xmxx = null;
		this.bljc = bljc;
		// 登簿成功之后，发送消息，包括 消息名称，操作人员，产权证号，合同号
		// 获取项目信息
		if (this.xmxx == null) {
			this.xmxx = baseCommonDao.get(BDCS_XMXX.class, xmbh);
		}
		// 获取描述
		String description = xmxx.getXMMC() + "-" + xmxx.getDJLX();
		// 获取当前登录人员
		String staffName = Global.getCurrentUserName();
		// 获取项目编号查询条件
		String xmbhcondition = ProjectHelper.GetXMBHCondition(xmbh);
		// 根据项目编号获取项目中登记集合
		List<BDCS_DJDY_GZ> djdys = baseCommonDao.getDataList(
				BDCS_DJDY_GZ.class, xmbhcondition);
		if (djdys != null && djdys.size() > 0) {
			for (int idjdy = 0; idjdy < djdys.size(); idjdy++) {
				djdyIndex = djdyIndex + 1;
				// 获取登记单元
				BDCS_DJDY_GZ djdy = djdys.get(idjdy);
				// 根据项目编号、登记单元生成共享文件
				String filePath = createXML(djdy);
				// // 上传共享文件
				// String sftpPath = uploadFile(filePath);
				// // 获取共享文件路径
				// String sftpFileName = getXMLFileName(filePath);
				// // 创建共享信息
				// ShareMessage shareMessage = createSendMsg(xmbh, description,
				// staffName, sftpFileName, xmxx.getPROJECT_ID(),
				// sftpPath, djdy.getBDCDYLX());
				// // 发送共享信息
				// mqSender.sendMessage(shareMessage);
			}
		}
	}

	/**
	 * 生成发送的信息
	 * 
	 * @作者 OuZhanrong
	 * @创建时间 2015年7月17日下午3:32:25
	 * @param 信息描述
	 * @param staffname
	 *            受理人员
	 * @param fileName
	 *            产权证号 ，多个用逗号隔开
	 * @param ywh
	 *            合同编号
	 * @return ShareMessage
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
	 * 上传xml文件到不动产登记局的sftp上
	 * 
	 * @作者 OuZhanrong
	 * @创建时间 2015年8月1日下午9:44:31
	 * @param localFilePath
	 *            上传文件的路径
	 */
	public String uploadFile(String localFilePath) {
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
			}

		}
		return null;

	}

	/**
	 * 获取登记结果保存到xml中
	 * 
	 * @作者 Administrator
	 * @创建时间 2015年8月1日上午3:33:01
	 * @param xmbh
	 */
	public String createXML(BDCS_DJDY_GZ djdy) {
		double startTime = System.currentTimeMillis();
		// 创建xml操作对象
		XStream xstream = new XStream();
		// 根据项目编号、登记单元生成MessageW对象，用于生成xml文件
		MessageW msg = getXMLMessage(djdy);

		// 以下为根据MessageW类型对象创建xml内容，不懂，不具体写描述了
		xstream.alias("Message", MessageW.class);
		xstream.processAnnotations(MessageW.class);
		// 启用Annotation
		xstream.autodetectAnnotations(true);
		// xstream.addImplicitCollection(Data.class, "list");
		String xmlStr = xstream.toXML(msg);

		String tempPath = GetClassesPath() + "../../temp/";
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		String dateStr = dateFormat.format(new Date());
		String fileName = ConfigHelper.getNameByValue("XZQHMC") + "不动产登记局-"
				+ this.xmxx.getPROJECT_ID() + "-"
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

	private MessageW getXMLMessage(BDCS_DJDY_GZ djdy) {
		// 创建MessageW对象
		MessageW xmlMessage = new MessageW();
		// 设定xml表头信息
		xmlMessage.setHead(getHead(djdy));
		// 设定xml具体信息
		xmlMessage.setData(getDJResult(djdy));
		return xmlMessage;
	}

	public Head getHead(BDCS_DJDY_GZ djdy) {
		// 创建表头对象
		Head head = new Head();
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
		head.setCreateDate(xmxx.getSLSJ().toString());// 设定项目创建时间
		head.setRegOrgID(ConfigHelper.getNameByValue("XZQHMC") + ConfigHelper.getNameByValue("DJJGMC"));// 设定登记机构
		RealUnit unit = UnitTools.loadUnit(
				ConstValue.BDCDYLX.initFrom(djdy.getBDCDYLX()),
				ConstValue.DJDYLY.initFrom(djdy.getLY()), djdy.getBDCDYID());
		if (unit != null) {
			if (!StringHelper.isEmpty(unit.getBDCDYH())
					&& unit.getBDCDYH().length() == 28) {
				head.setParcelID(unit.getBDCDYH().substring(0, 19));// 设定宗地、宗海代码
				head.setEstateNum(unit.getBDCDYH());// 设定不动产单元号
			}
		}
		head.setBusinessProcess(this.bljc);// 设定业务办理进程
		head.setRealEstateID(djdy.getBDCDYID());
		entityNoNull(head);
		return head;
	}

	/**
	 * 获取不动产登记结果
	 * 
	 * @作者 OuZhanrong
	 * @创建时间 2015年7月30日下午12:18:10
	 */
	private DataW getDJResult(BDCS_DJDY_GZ djdy) {
		String qlpackageName = "com.supermap.realestate.registration.model.xmlmodel.bdcql.";
		String dypackageName = "com.supermap.realestate.registration.model.xmlmodel.bdcdy.";
		DataW data = new DataW();
		List<Object> _qlList = new ArrayList<Object>();
		List<Object> _qlrList = new ArrayList<Object>();
		List<Object> _bdcdyList = new ArrayList<Object>();

		/*
		 * 获取不动产单元信息
		 */
		String bdcdyid = djdy.getBDCDYID();
		String bdcdylx = djdy.getBDCDYLX();
		String ly = djdy.getLY();
		ConstValue.BDCDYLX dylx = ConstValue.BDCDYLX.initFrom(bdcdylx);
		ConstValue.DJDYLY dyly = ConstValue.DJDYLY.initFrom(ly);
		XmlDYLX[] bdcdylxs = XmlDYLX.values();
		RealUnit bdcdy = UnitTools.loadUnit(dylx, dyly, bdcdyid);
		boolean bHAdd = false;
		boolean bZRZAdd = false;
		for (XmlDYLX _bdcdylx : bdcdylxs) {
			if (_bdcdylx.Value.equals(bdcdylx)) {
				Object obj = getEntity(dypackageName + _bdcdylx.Name);
				if (bdcdy != null) {
					setObjectValue(obj, bdcdy);
				}
				setFieldValue(obj, "BDCDYID", djdy.getBDCDYID());
				entityNoNull(obj);
				_bdcdyList.add(obj);
			} else {
				if (!_bdcdylx.Name.equals("H") && !_bdcdylx.Name.equals("ZRZ")) {
					Object obj = getEntity(dypackageName + _bdcdylx.Name);
					entityNoNull(obj);
					_bdcdyList.add(obj);
				}
				if ((_bdcdylx.Name.equals("H") && bHAdd)
						|| (_bdcdylx.Name.equals("ZRZ") && bZRZAdd)) {
					Object obj = getEntity(dypackageName + _bdcdylx.Name);
					entityNoNull(obj);
					_bdcdyList.add(obj);
				}
				if (_bdcdylx.Name.equals("H")) {
					bHAdd = true;
				}
				if (_bdcdylx.Name.equals("ZRZ")) {
					bZRZAdd = true;
				}

			}
		}

		// 根据登记单元获取权利信息
		List<BDCS_QL_GZ> qls = baseCommonDao.getDataList(BDCS_QL_GZ.class,
				"DJDYID='" + djdy.getDJDYID() + "' and xmbh='" + xmxx.getId()
						+ "'");
		XmlQLLX[] qllxs = XmlQLLX.values();
		for (int iqllx = 0; iqllx < qllxs.length; iqllx++) {
			XmlQLLX _qllx = qllxs[iqllx];
			boolean flag = false;
			String entityName = qlpackageName + _qllx.getTableName();
			for (int iql = 0; iql < qls.size(); iql++) {
				BDCS_QL_GZ ql = qls.get(iql);
				if (_qllx.getVaule().equals(ql.getDJLX())) {
					Object obj = getEntity(entityName);
					BDCS_FSQL_GZ fsql = null;
					if (ql.getFSQLID() != null) {
						fsql = baseCommonDao.get(BDCS_FSQL_GZ.class,
								ql.getFSQLID());
					}
					if (fsql != null) {
						setObjectValue(obj, fsql);
					}
					if (ql != null) {
						setObjectValue(obj, ql);
					}
					if (bdcdy != null) {
						setObjectValue(obj, bdcdy);
					}
					if (bdcdy != null) {
						setObjectValue(obj, bdcdy);
						setFieldValue(obj, "FDZL", bdcdy.getZL());
					}
					if (ql != null) {
						setFieldValue(obj, "QLID", ql.getId());
						setFieldValue(obj, "SYQQSSJ",
								StringHelper.FormatByDatetime(ql.getQLQSSJ()));
						setFieldValue(obj, "SYQJSSJ",
								StringHelper.FormatByDatetime(ql.getQLJSSJ()));
						setFieldValue(obj, "TDSYQSSJ",
								StringHelper.FormatByDatetime(ql.getQLQSSJ()));
						setFieldValue(obj, "TDSYJSSJ",
								StringHelper.FormatByDatetime(ql.getQLJSSJ()));
						setFieldValue(obj, "ZWLXQSSJ",
								StringHelper.FormatByDatetime(ql.getQLQSSJ()));
						setFieldValue(obj, "ZWLXJSSJ",
								StringHelper.FormatByDatetime(ql.getQLJSSJ()));
						setFieldValue(obj, "BDCDJZMH", ql.getBDCQZH());
					}
					if (fsql != null) {
						setFieldValue(obj, "ZXYWH", fsql.getZXDYYWH());
						setFieldValue(obj, "ZXYYDJSJ",
								StringHelper.FormatByDatetime(fsql.getZXSJ()));
						setFieldValue(obj, "JFDJSJ",
								StringHelper.FormatByDatetime(fsql.getZXSJ()));
						setFieldValue(obj, "ZXSJ",
								StringHelper.FormatByDatetime(fsql.getZXSJ()));

						setFieldValue(obj, "JFDBR", fsql.getZXDBR());
						setFieldValue(obj, "ZXYYDBR", fsql.getZXDBR());

						setFieldValue(obj, "ZXDYYY", fsql.getZXDYYY());
					}
					if (xmxx.getDJLX().equals(ConstValue.DJLX.ZXDJ.Value)
							|| xmxx.getQLLX()
									.equals(ConstValue.QLLX.CFZX.Value)) {
						if (ql.getLYQLID() != null) {
							BDCS_QL_LS ql_ls = baseCommonDao.get(
									BDCS_QL_LS.class, ql.getLYQLID());
							if (ql_ls != null) {
								setObjectValue(obj, ql_ls);
								setFieldValue(obj, "QLID", ql_ls.getId());
							}
						}
					}
					setFieldValue(obj, "BDCDYID", djdy.getBDCDYID());
					entityNoNull(obj);
					_qlList.add(obj);// 权利信息(登记类型)
					flag = true;
					List<BDCS_QLR_GZ> qlrList = baseCommonDao.getDataList(
							BDCS_QLR_GZ.class, "QLID='" + ql.getId()
									+ "' and xmbh='" + xmxx.getId() + "'");
					if (qlrList == null || qlrList.size() == 0) {
						BDCQLR _qlr = new BDCQLR();
						entityNoNull(_qlr);
						_qlrList.add(_qlr);// 权利人信息
					} else {
						for (BDCS_QLR_GZ qlr : qlrList) {
							BDCQLR _qlr = new BDCQLR();
							setObjectValue(_qlr, qlr);
							entityNoNull(_qlr);
							_qlrList.add(_qlr);// 权利人信息
						}
					}
					break;
				} else if (_qllx.getVaule().equals(xmxx.getQLLX())) {
					Object obj = getEntity(entityName);
					BDCS_FSQL_GZ fsql = null;
					if (ql.getFSQLID() != null) {
						fsql = baseCommonDao.get(BDCS_FSQL_GZ.class,
								ql.getFSQLID());
					}
					if (fsql != null) {
						setObjectValue(obj, fsql);
					}
					if (ql != null) {
						setObjectValue(obj, ql);
					}
					if (bdcdy != null) {
						setObjectValue(obj, bdcdy);
						setFieldValue(obj, "FDZL", bdcdy.getZL());
					}
					if (ql != null) {
						setFieldValue(obj, "QLID", ql.getId());
						setFieldValue(obj, "SYQQSSJ",
								StringHelper.FormatByDatetime(ql.getQLQSSJ()));
						setFieldValue(obj, "SYQJSSJ",
								StringHelper.FormatByDatetime(ql.getQLJSSJ()));
						setFieldValue(obj, "TDSYQSSJ",
								StringHelper.FormatByDatetime(ql.getQLQSSJ()));
						setFieldValue(obj, "TDSYJSSJ",
								StringHelper.FormatByDatetime(ql.getQLJSSJ()));
						setFieldValue(obj, "ZWLXQSSJ",
								StringHelper.FormatByDatetime(ql.getQLQSSJ()));
						setFieldValue(obj, "ZWLXJSSJ",
								StringHelper.FormatByDatetime(ql.getQLJSSJ()));
						setFieldValue(obj, "BDCDJZMH", ql.getBDCQZH());
					}
					if (fsql != null) {
						setFieldValue(obj, "ZXYWH", fsql.getZXDYYWH());
						setFieldValue(obj, "ZXYYDJSJ",
								StringHelper.FormatByDatetime(fsql.getZXSJ()));
						setFieldValue(obj, "JFDJSJ",
								StringHelper.FormatByDatetime(fsql.getZXSJ()));
						setFieldValue(obj, "ZXSJ",
								StringHelper.FormatByDatetime(fsql.getZXSJ()));

						setFieldValue(obj, "JFDBR", fsql.getZXDBR());
						setFieldValue(obj, "ZXYYDBR", fsql.getZXDBR());

						setFieldValue(obj, "ZXDYYY", fsql.getZXDYYY());
					}
					if (xmxx.getDJLX().equals(ConstValue.DJLX.ZXDJ.Value)
							|| xmxx.getQLLX()
									.equals(ConstValue.QLLX.CFZX.Value)) {
						if (ql.getLYQLID() != null) {
							BDCS_QL_LS ql_ls = baseCommonDao.get(
									BDCS_QL_LS.class, ql.getLYQLID());
							if (ql_ls != null) {
								setObjectValue(obj, ql_ls);
								setFieldValue(obj, "QLID", ql_ls.getId());
							}
						}
					}
					setFieldValue(obj, "BDCDYID", djdy.getBDCDYID());
					entityNoNull(obj);
					_qlList.add(obj);// 权利信息
					flag = true;
					List<BDCS_QLR_GZ> qlrList = baseCommonDao.getDataList(
							BDCS_QLR_GZ.class, "QLID='" + ql.getId()
									+ "' and xmbh='" + xmxx.getId() + "'");
					if (qlrList == null || qlrList.size() == 0) {
						BDCQLR _qlr = new BDCQLR();
						entityNoNull(_qlr);
						_qlrList.add(_qlr);// 权利人信息
					} else {
						for (BDCS_QLR_GZ qlr : qlrList) {
							BDCQLR _qlr = new BDCQLR();
							setObjectValue(_qlr, qlr);
							entityNoNull(_qlr);
							_qlrList.add(_qlr);// 权利人信息
						}
					}
					break;
				}

			}
			if (!flag) {
				Object obj = getEntity(entityName);
				entityNoNull(obj);
				_qlList.add(obj);// 权利信息(登记类型)
			}
		}

		// 合同内容
		HTNR htnr = new HTNR();
		entityNoNull(htnr);
		// 售房人
		SFR sfr = new SFR();
		entityNoNull(sfr);
		List<Object> sfrs = new ArrayList<Object>();
		sfrs.add(sfr);

		data.setBDCDY(_bdcdyList);
		data.setBDCQL(_qlList);
		data.setBDCQLRS(_qlrList);
		data.setHtnr(htnr);
		data.setSFRS(sfrs);
		return data;
	}

	/**
	 * 获取实体类的class
	 * 
	 * @作者 OuZhanrong
	 * @创建时间 2015年7月30日下午11:05:58
	 * @param entityName
	 * @return
	 */
	private Object getEntity(String entityName) {
		Class<?> unitClass = null;
		try {
			unitClass = Class.forName(entityName);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		try {
			return unitClass.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 设置权利实体对象的值
	 * 
	 * @作者 OuZhanrong
	 * @创建时间 2015年7月31日下午3:17:58
	 * @param obj
	 * @param ql
	 */
	public static void setFieldValue(Object obj, String fieldName, String value) {
		Field fields[] = getDeclareField(obj);

		for (Field f : fields) {
			if ((f.getName().toUpperCase()).equals(fieldName.toUpperCase())) {

				try {
					if (value != null) {
						String fieldType = f.getType().getSimpleName();
						f.setAccessible(true);
						if ("String".equals(fieldType)) {
							f.set(obj, StringHelper.formatObject(value));
						} else if ("Date".equals(fieldType)) {
							Date _value = null;
							if (value != null) {
								try {
									_value = StringHelper.FormatByDate(value);
								} catch (Exception e) {
									_value = null;
								}
							}
							if (_value != null) {
								f.set(obj, _value);
							}
						} else if ("Double".equals(fieldType)
								|| "double".equals(fieldType)) {
							String _value = StringHelper.formatObject(value);
							if (StringHelper.isEmpty(_value)) {
								_value = "0";
							}
							f.set(obj, Double.valueOf(_value));
						} else if ("Integer".equals(fieldType)
								|| "int".equals(fieldType)) {
							String _value = StringHelper.formatObject(value);
							if (StringHelper.isEmpty(_value)) {
								_value = "0";
							}
							f.set(obj, Integer.valueOf(_value));
						}
						f.setAccessible(false);
					}

					break;
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}

			}
		}

	}

	/**
	 * 设置权利实体对象的值
	 * 
	 * @作者 OuZhanrong
	 * @创建时间 2015年7月31日下午3:17:58
	 * @param obj
	 * @param ql
	 */
	public static void setObjectValue(Object objdes, Object objsrc) {
		Field fields[] = getDeclareField(objdes);
		Field qlFileds[] = getDeclareField(objsrc);

		for (Field f : fields) {
			for (Field qlf : qlFileds) {
				if ((f.getName().toUpperCase()).equals(qlf.getName()
						.toUpperCase())) {

					try {
						qlf.setAccessible(true);
						Object value = (Object) qlf.get(objsrc);
						qlf.setAccessible(false);
						if (value != null) {
							String fieldType = f.getType().getSimpleName();
							f.setAccessible(true);
							if ("String".equals(fieldType)) {
								f.set(objdes, StringHelper.formatObject(value));
							} else if ("Date".equals(fieldType)) {
								Date _value = null;
								if (value != null) {
									try {
										_value = StringHelper
												.FormatByDate(value);
									} catch (Exception e) {
										_value = null;
									}
								}
								if (_value != null) {
									f.set(objdes, _value);
								}
							} else if ("Double".equals(fieldType)
									|| "double".equals(fieldType)) {
								String _value = StringHelper
										.formatObject(value);
								if (StringHelper.isEmpty(_value)) {
									_value = "0";
								}
								f.set(objdes, Double.valueOf(_value));
							} else if ("Integer".equals(fieldType)
									|| "int".equals(fieldType)) {
								String _value = StringHelper
										.formatObject(value);
								if (StringHelper.isEmpty(_value)) {
									_value = "0";
								}
								f.set(objdes, Integer.valueOf(_value));
							}
							f.setAccessible(false);
						}

						break;
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}

				}
			}

		}

	}

	/**
	 * 获取对象的DeclaredField
	 * 
	 * @作者 OuZhanrong
	 * @创建时间 2015年7月12日下午4:53:54
	 * @param obj
	 * @param fieldNmae
	 * @return
	 */
	public static Field[] getDeclareField(Object obj) {
		Field[] allField = new Field[] {};
		try {
			Class<?> clazz = obj.getClass();

			for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
				Field[] _fields = clazz.getDeclaredFields();
				allField = concat(allField, _fields);
			}
		} catch (Exception e) {
		}
		return allField;
	}

	/**
	 * 把两个数组合并成一个数组
	 * 
	 * @作者 Ouzhanrong
	 * @创建时间 2015年8月1日下午9:46:01
	 * @param first
	 * @param second
	 * @return
	 */
	public static <T> T[] concat(T[] first, T[] second) {
		T[] result = Arrays.copyOf(first, first.length + second.length);
		System.arraycopy(second, 0, result, first.length, second.length);
		return result;
	}

	/**
	 * 让对象的所有属性不能为空，保证在xml输出时输出该节点
	 * 
	 * @作者 Ouzhanrong
	 * @创建时间 2015年8月1日下午9:46:42
	 * @param obj
	 */

	public static void entityNoNull(Object obj) {
		try {
			Field fields[] = getDeclareField(obj);
			for (Field field : fields) {
				field.setAccessible(true);
				Object value = field.get(obj);
				if (value == null) {
					String fieldType = field.getType().getSimpleName();
					if ("String".equals(fieldType)) {
						field.set(obj, "");
					} else if ("Date".equals(fieldType)) {
						// field.set(obj, );
					} else if ("Double".equals(fieldType)
							|| "double".equals(fieldType)) {
						field.set(obj, 0.0);
					} else if ("Integer".equals(fieldType)
							|| "int".equals(fieldType)) {
						field.set(obj, 0);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 获取站点的Classes的本地路径
	 * 
	 * @作者 Administrator
	 * @创建时间 2015年8月1日下午9:47:50
	 * @return
	 */
	public static String GetClassesPath() {
		URL url = Thread.currentThread().getContextClassLoader()
				.getResource("");
		String path = url.getPath();
		path = path.substring(1);
		return path;
	}

	/**
	 * 根据路径获取文件名称
	 * 
	 * @作者 Ouzhanrong
	 * @创建时间 2015年8月1日下午10:16:05
	 * @param xmlpath
	 * @return
	 */
	public static String getXMLFileName(String xmlpath) {
		int index = xmlpath.lastIndexOf("\\");
		if (index < 0) {
			index = xmlpath.lastIndexOf("/");
		}
		String fileName = xmlpath.substring(index + 1);
		return fileName;
	}

	/**
	 * 根据路径删除指定的目录或文件，无论存在与否
	 *
	 * @param sPath
	 *            要删除的目录或文件
	 * @return 删除成功返回 true，否则返回 false。
	 */
	public boolean deleteFile(String sPath) {
		boolean flag = false;
		File file = new File(sPath);
		// 判断目录或文件是否存在
		if (!file.exists()) { // 不存在返回 false
			return flag;
		} else {
			if (file.isFile() && file.exists()) {
				file.delete();
				flag = true;
			}
		}
		return flag;
	}

	public static void main(String[] args) {
		entityNoNull(new DYAQ());
	}
}
