package com.supermap.realestate.registration.tools;

import java.sql.Types;

/**
 * 
 * 存储过程参数类
 * 
 * @ClassName: ProcedureParam
 * @author 俞学斌
 * @date 2016年10月10日 10:25:33
 */
public class ProcedureParam{
		private String name="";///参数名称
		private Object value="";///参数名称
		private Integer sxh=0;//参数顺序
		private int fieldtype=Types.NVARCHAR;//参数值类型
		private String paramtype="in";//参数类型，输入参数为：in；输出参数为：out
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public Object getValue() {
			return value;
		}
		public void setValue(Object value) {
			this.value = value;
		}
		public Integer getSxh() {
			return sxh;
		}
		public void setSxh(Integer sxh) {
			this.sxh = sxh;
		}
		public int getFieldtype() {
			return fieldtype;
		}
		public void setFieldtype(int fieldtype) {
			this.fieldtype = fieldtype;
		}
		public String getParamtype() {
			return paramtype;
		}
		public void setParamtype(String paramtype) {
			this.paramtype = paramtype;
		}
	}
