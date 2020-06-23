package com.supermap.intelligent.util;

import org.springframework.context.annotation.Description;

import java.util.Map;

/**
 * @Description:枚举
 * @author Administrator
 *
 */
public class ConstValue {
	
	/**接口请求编号
	 * @author Administrator
	 *
	 */
	public enum RequestzfcodeEnum {
		
		ZZ("1000", "制证"),
		
		YZ("1001", "用证");

		public String Value;

		public String Name;

		private RequestzfcodeEnum(String value) {
			this.Value = value;
		}

		private RequestzfcodeEnum(String value, String name) {
			this.Value = value;
			this.Name = name;
		}

		public static RequestzfcodeEnum initFrom(String value) {
			for (RequestzfcodeEnum examType : RequestzfcodeEnum.values()) {
				if (value.equals(examType.Value)) {
					return examType;
				}
			}
			return null;
		}
	}
	
	/**
	 * 查封平台相应编码类型枚举
	 * 
	 * 
	 */
	@Description(value = "查封平台相应编码类型枚举")
	public enum MrpccodingEnum {

		/** 成功(00) */
		SUCCESS("00", "成功"),

		/** 失败(01) */
		FAIL("01", "失败,请检查密文或者报文格式"),

		/** 内部错误(02) */
		ERROR("02", "内部错误"),
		
		/** 必填项参数为空(03) */
		REQUIRED("03", "必填项参数不能为空"),
		
		/** 查无信息(04) */
		NOINFORMATION("04", "查无信息"),

		/** 未授权(41) */
		UNAUTHORIZED("41", "未授权"),

		/** 令牌（TOKEN）过期(42) */
		EXPIRES("42", "令牌（TOKEN）过期"),

		/** 用户验证不成功(43) */
		AUTHENTICATION("43", "用户验证不成功"),
		
		/** 其他错误(700) */
		OTHERERRORS("99", "其他错误");

		public String Value;

		public String Name;

		private MrpccodingEnum(String value) {
			this.Value = value;
		}

		private MrpccodingEnum(String value, String name) {
			this.Value = value;
			this.Name = name;
		}

		public static MrpccodingEnum initFrom(String value) {
			for (MrpccodingEnum examType : MrpccodingEnum.values()) {
				if (value.equals(examType.Value)) {
					return examType;
				}
			}
			return null;
		}
	}
	/**接口请求编号
	 * @author Administrator
	 *
	 */
	public enum RequestcodeEnum {
		
		TOKEN("1000", "处理TOKEN"),
		
		ZS("1001", "证书查询"),

		ZM("1002", "证明查询"),

		FW("1003", "房屋及权属信息核验"),
		
		JD("1004", " 业务进度查询"),
		
		HJ("1005", " 业务环节查询"),

		QZ("1006", "权证号检索"),

		DYXZ("1007", "单元限制查询"),
		
		DYZT("1008", "单元状态查询"),
		
		DY("1009","宗地不动产单元号检索"),
		
		ZD("1010","宗地检索"),
		
		DZZZ("4001", "电子证照查询"),
		
		ZFJK("4002", "转发接口");

		public String Value;

		public String Name;

		private RequestcodeEnum(String value) {
			this.Value = value;
		}

		private RequestcodeEnum(String value, String name) {
			this.Value = value;
			this.Name = name;
		}

		public static RequestcodeEnum initFrom(String value) {
			for (RequestcodeEnum examType : RequestcodeEnum.values()) {
				if (value.equals(examType.Value)) {
					return examType;
				}
			}
			return null;
		}
	}
	
	/**MQ请求编号
	 * @author Administrator
	 *
	 */
	public enum MQEnum{
		SHJD("MQ0001", "审批进度"),
		
		DJB("MQ0002", "登簿信息"),

		CERTIFICATE("MQ0003", "电子证照信息"),

		DJBH("MQ0004", "登记驳回");

		public String Value;

		public String Name;

		private MQEnum(String value) {
			this.Value = value;
		}

		private MQEnum(String value, String name) {
			this.Value = value;
			this.Name = name;
		}

		public static MQEnum initFrom(String value) {
			for (MQEnum examType : MQEnum.values()) {
				if (value.equals(examType.Value)) {
					return examType;
				}
			}
			return null;
		}
	}
	
	/**节点查询SQL枚举
	 * @author Administrator
	 *
	 */
	public enum SQLEnum{
		/** 土地使用权 */
		TDSYQ{	
			public String getSQL(String selectsql,String leftjoinsql,Map<String,Object>wheresql,String extraSql){
				  return new StringBuffer("SELECT ").append(selectsql).append(" FROM BDCJCS_TDSJYT TDSY LEFT JOIN BDCJCS_QLR QLR ON QLR.QLBSM = TDSY.ZDBSM").append(leftjoinsql).append(" WHERE  ").append(wheresql).toString();
				}
		},
		GYNYDSYQ{
			public String getSQL(String selectsql,String leftjoinsql,Map<String,Object>wheresql,String extraSql){
				  return new StringBuffer("SELECT ").append(selectsql).append(" FROM BDCJCS_NYDSYQ NYDS LEFT JOIN BDCJCS_QLR QLR ON QLR.QLBSM = NYDS.BSM ").append(leftjoinsql).append(" WHERE NYDS.QSZT='1' AND ").append(wheresql).toString();
				}
		},	
		JSYDSYQ{
			public String getSQL(String selectsql,String leftjoinsql,Map<String,Object>wheresql,String extraSql){
				return new StringBuffer("SELECT ").append(selectsql).append(" FROM( SELECT ZD.BDCDYID,'01' AS BDCDYLX,").append(extraSql).append(" FROM BDCK.BDCS_SYQZD_XZ ZD WHERE ").append(wheresql.get("JSYDSYQ"))
						.append(" UNION ALL SELECT ZD.BDCDYID,'02' AS BDCDYLX,").append(extraSql).append(" FROM BDCK.BDCS_SHYQZD_XZ ZD WHERE ").append(wheresql.get("JSYDSYQ")).append(") ZD ").toString();
				}

		},
		TDYTLIST{
			public String getSQL(String selectsql,String leftjoinsql,Map<String,Object>wheresql,String extraSql){
				return new StringBuffer("SELECT DISTINCT ").append(selectsql).append(" FROM BDCK.BDCS_TDYT_XZ TDYT ").append(" WHERE ").append(wheresql.get("TDYTLIST")).toString();
			}
		},
		GJZWSYQ{
			//待完善 构（建）筑物所有权(数据量少，预留处理)"
			public String getSQL(String selectsql,String leftjoinsql,Map<String,Object>wheresql,String extraSql){
				  return new StringBuffer("SELECT ").append(selectsql).append(" FROM BDCJCS_JSYDSYQ JSYD LEFT JOIN BDCJCS_QLR QLR ON QLR.QLBSM = JSYD.BSM ").append(leftjoinsql).append(" WHERE JSYD.QSZT='1' AND ").append(wheresql).toString();
				}
		},
		HYSYQ{
			//待完善
			public String getSQL(String selectsql,String leftjoinsql,Map<String,Object>wheresql,String extraSql){
				  return new StringBuffer("SELECT ").append(selectsql).append(" FROM BDCJCS_LQ LQ LEFT JOIN BDCJCS_QLR QLR ON QLR.QLBSM = LQ.BSM ").append(leftjoinsql).append(" WHERE LQ.QSZT='1' AND ").append(wheresql).toString();
				}
		},
		LQ{	
			public String getSQL(String selectsql,String leftjoinsql,Map<String,Object>wheresql,String extraSql){
				  return new StringBuffer("SELECT ").append(selectsql).append(" FROM BDCJCS_LQ LQ LEFT JOIN BDCJCS_QLR QLR ON QLR.QLBSM = LQ.BSM ").append(leftjoinsql).append(" WHERE LQ.QSZT='1' AND ").append(wheresql).toString();
				}
		},
//		FDCQ{
//			public String getSQL(String selectsql,String leftjoinsql,Map<String,Object>wheresql,String extraSql){
//				  return new StringBuffer("SELECT ").append(selectsql).append(" FROM BDCK.BDCS_QL_XZ QL_XZ INNER JOIN BDCK.BDCS_QLR_XZ QLR_XZ ON QL_XZ.QLID=QLR_XZ.QLID INNER JOIN BDCK.BDCS_DJDY_XZ DJDY_XZ ON DJDY_XZ.DJDYID=QL_XZ.DJDYID INNER JOIN (SELECT H.BDCDYID,H.BDCDYLX,")
//						  .append(extraSql).append(" FROM( SELECT H.BDCDYID,'031' AS BDCDYLX,").append(extraSql).append(" FROM BDCK.BDCS_H_XZ H where H.BDCDYID IN(").append(wheresql.get("FDCQ*BDCDYID"))
//						  .append(") UNION ALL SELECT H.BDCDYID,'032' AS BDCDYLX,").append(extraSql).append(" FROM BDCK.BDCS_H_XZY H where H.BDCDYID in(").append(wheresql.get("FDCQ*BDCDYID")).append("))H)H ON H.BDCDYID=DJDY_XZ.BDCDYID WHERE ").append(wheresql.get("FDCQ")).toString();
//				}
//		},
		FDCQ{
			public String getSQL(String selectsql,String leftjoinsql,Map<String,Object>wheresql,String extraSql){
				  return new StringBuffer("SELECT ").append(selectsql).append(" FROM( SELECT H.BDCDYID,'031' AS BDCDYLX,").append(extraSql).append(" FROM BDCK.BDCS_H_XZ H WHERE ").append(wheresql.get("FDCQ"))
						  .append(" UNION ALL SELECT H.BDCDYID,'032' AS BDCDYLX,").append(extraSql).append(" FROM BDCK.BDCS_H_XZY H WHERE ").append(wheresql.get("FDCQ")).append(") H ").toString();
				}
		},
		QLRLIST{	
			public String getSQL(String selectsql,String leftjoinsql,Map<String,Object>wheresql,String extraSql){
				  return new StringBuffer("SELECT DISTINCT ").append(selectsql).append(" FROM BDCK.BDCS_QL_XZ QL_XZ INNER JOIN BDCK.BDCS_QLR_XZ QLR_XZ ON QL_XZ.QLID = QLR_XZ.QLID ").append(leftjoinsql).append(" WHERE ").append(wheresql.get("QLRLIST")).toString();
				}
		},
		YGDJ{
			public String getSQL(String selectsql,String leftjoinsql,Map<String,Object>wheresql,String extraSql){
				  return new StringBuffer("SELECT ").append(selectsql).append(" FROM BDCK.BDCS_QL_XZ QL_XZ INNER JOIN  BDCK.BDCS_QLR_XZ QLR_XZ ON QL_XZ.QLID=QLR_XZ.QLID INNER JOIN BDCK.BDCS_FSQL_XZ FSQL_XZ ON FSQL_XZ.QLID=QL_XZ.QLID ").append(leftjoinsql).append(" WHERE QL_XZ.QLLX ='4' AND QL_XZ.DJLX='700' AND ").append(wheresql.get("YGDJ")).toString();
				}
		},
		DYAQ{
			public String getSQL(String selectsql,String leftjoinsql,Map<String,Object>wheresql,String extraSql){
				  return new StringBuffer("SELECT (SELECT SFHBZS FROM BDCK.BDCS_XMXX XMXX WHERE XMXX.XMBH = QL_XZ.XMBH) SFHBZS,").append(selectsql).append(" FROM BDCK.BDCS_QL_XZ QL_XZ INNER JOIN  BDCK.BDCS_QLR_XZ QLR_XZ ON QL_XZ.QLID=QLR_XZ.QLID INNER JOIN BDCK.BDCS_FSQL_XZ FSQL_XZ ON FSQL_XZ.QLID=QL_XZ.QLID ").append(leftjoinsql).append(" WHERE QL_XZ.QLLX ='23' AND ").append(wheresql.get("DYAQ")).toString();
				}
		},
		CFDJ{
			public String getSQL(String selectsql,String leftjoinsql,Map<String,Object>wheresql,String extraSql){
				  return new StringBuffer("SELECT ").append(selectsql).append(" FROM BDCK.BDCS_QL_XZ QL_XZ  INNER JOIN BDCK.BDCS_FSQL_XZ FSQL_XZ ON FSQL_XZ.QLID=QL_XZ.QLID ").append(leftjoinsql).append(" WHERE QL_XZ.QLLX='99' AND QL_XZ.DJLX='800' AND ").append(wheresql.get("CFDJ")).toString();
				}
		},
		YYDJ{
			public String getSQL(String selectsql,String leftjoinsql,Map<String,Object>wheresql,String extraSql){
				  return new StringBuffer("SELECT ").append(selectsql).append(" FROM BDCK.BDCS_QL_XZ QL_XZ INNER JOIN  BDCK.BDCS_QLR_XZ QLR_X ON QL_XZ.QLID=QLR_X.QLID INNER JOIN BDCK.BDCS_FSQL_XZ FSQL_XZ ON FSQL_XZ.QLID=QL_XZ.QLID ").append(leftjoinsql).append(" WHERE QL_XZ.QLLX='99' AND QL_XZ.DJLX='600' AND ").append(wheresql.get("YYDJ")).toString();
				}
		},
		XZDJ{
			public String getSQL(String selectsql,String leftjoinsql,Map<String,Object>wheresql,String extraSql){
				  return new StringBuffer("SELECT ").append(selectsql).append(" FROM BDCJCS_DYXZ DYXZ LEFT JOIN BDCJCS_QLR QLR ON QLR.QLBSM = DYXZ.BSM ").append(leftjoinsql).append(" WHERE DYXZ.QSZT='1' AND ").append(wheresql).toString();
				}
		};
		 public abstract String getSQL(String selectsql,String leftjoinsql,Map<String,Object>wheresql,String extraSql);
		
	}
	
}
