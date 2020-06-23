package org.jeecg.common.util;

import org.springframework.context.annotation.Description;

import java.util.Map;

/**
 * @Description:枚举
 * @author Administrator
 *
 */
public class ConstValueMrpc {
	
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
		
		JD("1004", "业务进度查询"),
		
		HJ("1005", "业务环节查询") ,

		QZ("1006", "权证号检索"),
		
		DYXZ("1007", "单元限制查询"),
		
		DYZT("1008","单元状态"),
		
		DY("1009","宗地不动产单元号检索"),
		
		ZD("1010","宗地信息查询"),

		DYDJ("2001", "抵押登记办理"),

		DYBG("2002", "抵押变更登记办理"),

		DTZY("2003", "抵押转移登记办理"),

		DYZX("2004", "抵押注销登记办理"),

		XJSPFZYDY("2005", "新建商品房转移与抵押合并办理"),

		CLFZYDY("2006", "存量房与抵押合并办理"),

		DEPT_TOKEN("3001", "获取金融机构授权信息"),

		GXYWZT("3002", "更新业务状态信息"),

		DBJGTS("3003", "业务登簿结果推送"),

		DZZZTS("3004", "电子证照推送"), 
		
		DZZZ("4001"	,	"电子证照获取"),
		SPFZY("2007","商品房转移");


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

		CERTIFICATE("MQ0003", "电子证照信息");

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

	/**
	 * @Author taochunda
	 * @Description 业务来源
	 * @Date 2019-09-06 18:01
	 * @Param
	 * @return
	 **/
	public enum YWLY{
		DYPT("0", "抵押平台"),

		JRJG("1", "金融机构");

		public String Value;

		public String Name;

		private YWLY(String value) {
			this.Value = value;
		}

		private YWLY(String value, String name) {
			this.Value = value;
			this.Name = name;
		}

		public static YWLY initFrom(String value) {
			for (YWLY examType : YWLY.values()) {
				if (value.equals(examType.Value)) {
					return examType;
				}
			}
			return null;
		}
	}

	/**
	 * @Author taochunda
	 * @Description 回调状态枚举
	 * @Date 2019-09-11 16:04
	 * @Param
	 * @return
	 **/
	public enum HDZT{
		TOKEN_SUCCESS("1001", "获取token成功"),
		TOKEN_ERROR("1002", "获取token失败"),

		GXYWZT_SUCCESS("2001", "更新业务状态成功"),
		GXYWZT_ERROR("2002", "更新业务状态失败"),

		DBJGTS_SUCCESS("3001", "业务登簿结果推送成功"),
		DBJGTS_ERROR("3002", "业务登簿结果推送失败"),

		DZZZTS_SUCCESS("4001", " 电子证照推送成功"),
		DZZZTS_ERROR("4002", " 电子证照推送失败");

		public String Value;

		public String Name;

		private HDZT(String value) {
			this.Value = value;
		}

		private HDZT(String value, String name) {
			this.Value = value;
			this.Name = name;
		}

		public static HDZT initFrom(String value) {
			for (HDZT examType : HDZT.values()) {
				if (value.equals(examType.Value)) {
					return examType;
				}
			}
			return null;
		}
	}


	/**
	 * @Author taochunda
	 * @Description 审核状态字典值
	 * @Date 2019-09-11 16:04
	 * @Param
	 * @return
	 **/
	public enum SHZT{
		NO_SUBMITED("-1", "未提交"),
		WAIT_CONFIRM("0", "待审核"),

		REJECT("11", "审核驳回"),
		FAIL_SUBMIT("-11", "提交失败"),

		OK_CONFIRM("20", "审核通过"),
		BOARD_BOOK("30", "已登簿");

		public String Value;

		public String Name;

		private SHZT(String value) {
			this.Value = value;
		}

		private SHZT(String value, String name) {
			this.Value = value;
			this.Name = name;
		}

		public static SHZT initFrom(String value) {
			for (SHZT examType : SHZT.values()) {
				if (value.equals(examType.Value)) {
					return examType;
				}
			}
			return null;
		}
	}



	
}
