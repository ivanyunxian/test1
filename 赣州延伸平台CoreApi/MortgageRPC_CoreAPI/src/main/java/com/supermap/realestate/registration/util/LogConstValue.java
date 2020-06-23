package com.supermap.realestate.registration.util;

import org.springframework.context.annotation.Description;

/** 日志枚举
 * @author chenbo
 * 2017年6月24日
 */
public class LogConstValue {
	

	/** 日志分类枚举
	 * @author chenbo
	 * 2017年6月24日
	 */
	@Description(value = "日志分类")
	public enum LOGTYPE {

		/** 数据维护 */
		MAINTAIN("1", "数据维护"),

		/** 系统查询 */
		SEARCH("2", "系统查询"),
		
		/** 系统登录 */
		LOGIN("3", "系统登录"),
		
		/** 系统输出 */
		OUTPUT("4", "系统输出"),
		
		/** 系统操作 */
		OPERATE("5", "系统操作"),
		
		/** 系统错误 */
		ERROR("6", "系统错误"),
		
		/** 系统配置 */
		CONFIGURE("7", "系统配置"),
		
		/** 其他 */
		QTLGTYPE("99", "其他");
		
		public String Value;

		public String Name;

		private LOGTYPE(String value) {
			this.Value = value;
		}

		private LOGTYPE(String value, String name) {
			this.Value = value;
			this.Name = name;
		}
	}
	
	
	/** 数据维护日志类型枚举
	 * @author chenbo
	 * 2017年6月24日
	 */
	@Description(value = "数据维护日志类型")
	public enum MTTYPE {

		/** 权籍补录维护 */
		QJBL("1", "权籍补录维护"),

		/** 权籍调查维护 */
		QJDC("2", "权籍调查维护"),
		
		/** 权籍查询维护 */
		QJCX("3", "权籍查询维护"),
		
		/** 登记数据维护 */
		DJSJ("4", "登记数据维护"),
		
		/** 登记业务维护 */
		DJYW("5", "登记业务维护"),
		
		/** 其他 */
		QTMTTYPE("99", "其他");
		
		public String Value;

		public String Name;

		private MTTYPE(String value) {
			this.Value = value;
		}

		private MTTYPE(String value, String name) {
			this.Value = value;
			this.Name = name;
		}
	}
	
	
	/** 数据维护操作类型枚举
	 * @author chenbo
	 * 2017年6月24日
	 */
	@Description(value = "数据维护操作类型")
	public enum MTOPRTYPE {

		/** 添加信息 */
		ADD("1", "添加信息"),

		/** 维护信息 */
		UPDATE("2", "维护信息"),
		
		/** 删除信息 */
		DELETE("3", "删除信息"),
		
		/** 房屋信息维护 */
		FWWH("4", "房屋信息维护"),
		
		/** 自然幢信息维护 */
		ZRZWH("5", "自然幢信息维护"),
		
		/** 宗地信息维护 */
		ZDWH("6", "宗地信息维护"),
		
		/** 其他 */
		QTMTTYPE("99", "其他");
		
		public String Value;

		public String Name;

		private MTOPRTYPE(String value) {
			this.Value = value;
		}

		private MTOPRTYPE(String value, String name) {
			this.Value = value;
			this.Name = name;
		}
	}
	
	/** 系统查询日志类型枚举
	 * @author chenbo
	 * 2017年6月24日
	 */
	@Description(value = "系统查询日志类型")
	public enum QUERYTYPE {

		/** 权籍宗地查询 */
		QJZD("1", "权籍宗地查询"),

		/** 权籍自然幢查询 */
		QJZRZ("2", "权籍自然幢查询"),
		
		/** 权籍房屋查询 */
		QJFW("3", "权籍房屋查询"),
		
		/** 权籍林地查询 */
		QJLD("4", "权籍林地查询"),
		
		/** 权籍农用地查询 */
		QJNYD("5", "权籍农用地查询"),

		/** 权籍宗海查询*/
		QJZH("6", "权籍宗海查询"),
		
		/** 登记宗地查询 */
		DJZD("7", "登记宗地查询"),
		
		/** 登记自然幢查询 */
		DJZRZ("8", "登记自然幢查询"),
		
		/** 登记房屋查询*/
		DJFW("9", "登记房屋查询"),

		/** 登记林地查询 */
		DJLD("10", "登记林地查询"),
		
		/** 登记农用地查询 */
		DJNYD("11", "登记农用地查询"),
		
		/** 登记宗海查询 */
		DJZH("12", "登记宗海查询"),
		
		/** 登记登记簿查询 */
		DJDJB("13", "登记登记簿查询"),

		/** 权籍查询证明模块查询*/
		QJCXZM("14", "权籍查询证明模块查询"),
		
		/** 登记查询证明流程查询 */
		DJCXZM("15", "登记查询证明流程查询"),
		
		/** 登记小土地证查询 */
		DJXTDZ("16", "登记小土地证查询"),
		
		/** 登记指认图查询 */
		DJZRT("17", "登记指认图查询"),
		
		/** 其他 */
		QTQYTYPE("99", "其他");
		
		public String Value;

		public String Name;

		private QUERYTYPE(String value) {
			this.Value = value;
		}

		private QUERYTYPE(String value, String name) {
			this.Value = value;
			this.Name = name;
		}
	}
	
	
	/** 系统登陆日志类型枚举
	 * @author chenbo
	 * 2017年6月24日
	 */
	@Description(value = "系统登陆日志类型")
	public enum LOGINTYPE {

		/** 权籍登陆 */
		QJDL("1", "权籍登陆"),

		/** 权籍退出 */
		QJTC("2", "权籍退出"),
		
		/** 登记登陆 */
		DJDL("3", "登记登陆"),
		
		/** 登记退出 */
		DJTC("4", "登记退出"),
		
		/** 其他 */
		QTDLTYPE("99", "其他");
		
		public String Value;

		public String Name;

		private LOGINTYPE(String value) {
			this.Value = value;
		}

		private LOGINTYPE(String value, String name) {
			this.Value = value;
			this.Name = name;
		}
	}
	
	/** 登录退出状态
	 * @author chenbo
	 * 2017年6月27日
	 */
	public enum DLMSG {
		
		DLCG("0", "登录成功"), 
		
		DLSB("1", "登录失败"),
		
		ZHTY("2", "登录失败,账号已经停用"), 
		
		CSXZ("3", "登录失败,账户错误次数过多,暂时禁止登录"),
		
		WZCW("4", "登录未知错误"),
		
		TCCG("5", "退出成功"), 
		
		TCSB("6", "退出失败"),
		
		QTMSG("7", "其他");

		public String Value;

		public String Name;

		private DLMSG(String value, String name) {
			this.Value = value;
			this.Name = name;
		}
	}

}
