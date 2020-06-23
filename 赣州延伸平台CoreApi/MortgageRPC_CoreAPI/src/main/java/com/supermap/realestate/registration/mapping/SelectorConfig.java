/**   
 *  选择器配置类文件
 * @Title: SelectorConfig.java 
 * @Package com.supermap.realestate.registration.mapping 
 * @author liushufeng 
 * @date 2015年7月1日 12:00:50 
 * @version V1.0   
 */

package com.supermap.realestate.registration.mapping;

import java.io.Serializable;
import java.util.List;

/**
 * 选择器配置类
 * @ClassName: SelectorConfig
 * @author liushufeng
 * @date 2015骞?7鏈?14鏃? 涓嬪崍12:00:50
 */
public class SelectorConfig implements Serializable {

	private static final long serialVersionUID = -7833210789893041810L;

	/**
	 * 构造函数
	 */
	public SelectorConfig() {

	}

	/**
	 * 选择器名称
	 */
	private String name;

	/**
	 * 选择器别名
	 */
	private String text;

	/**
	 * (C)是否选择单元:如果值为true，那么selectql必须为false
	 */
	private boolean selectbdcdy;

	/**
	 * (C)是否选择权利:如果值为true，那么selectbdcdy必须为false
	 */
	private boolean selectql;

	/**
	 * (C)选择的权利类型:如果selectqlweitrue，必须配置该值
	 */
	private String selectqllx;

	/**
	 * (M)不动产单元类型:无论是选择权利还是选择单元，都需要配置该值
	 */
	private String bdcdylx;

	/**
	 * (M)不动产单元来源：GZ、XZ、LS、DC
	 */
	private String ly;

	/**
	 * (O)附加条件:除了运行时指定的条件为，默认附加的查询条件
	 */
	private String condition;

	/**
	 * (M)是否单选：指示datagrid是否单选，true为单选，false为多选
	 */
	private boolean singleselect;

	/**
	 * (M)标识字段:前台选中之后返回后台的字段
	 */
	private String idfieldname;

	/**
	 * 是否使用指定的SQL语句，默认为false，如果为true，则不实用系统默认的查询语句
	 */
	private boolean useconfigsql = false;

	/**
	 * 是否显示详细信息按钮
	 */
	private boolean showdetailbutton=false;
	
	
	/**
	 * 是否启用瀑布流加载数据
	 */
	private boolean datastyle=false;
	

	/**
	 * 指定的sql语句：如果useconfigsql为true，该值必填
	 */
	private String configsql;

	/**
	 * 查询配置
	 */
	private QueryConfig queryconfig;

	/**
	 * 表格配置
	 */
	private GridConfig gridconfig;

	/**
	 * 结果配置
	 */
	private ResultConfig resultconfig;

	public String getConfigsql() {
		return configsql;
	}

	public boolean isUseconfigsql() {
		return useconfigsql;
	}

	public String getCondition() {
		return condition;
	}

	public String getIdfieldname() {
		return idfieldname;
	}

	public boolean isSingleselect() {
		return singleselect;
	}

	public boolean isSelectbdcdy() {
		return selectbdcdy;
	}

	public boolean isSelectql() {
		return selectql;
	}

	public String getSelectqllx() {
		return selectqllx;
	}

	public String getBdcdylx() {
		return bdcdylx;
	}

	public String getLy() {
		return ly;
	}

	public ResultConfig getResultconfig() {
		return resultconfig;
	}

	public String getName() {
		return name;
	}

	public String getText() {
		return text;
	}

	public QueryConfig getQueryconfig() {
		return queryconfig;
	}

	public GridConfig getGridconfig() {
		return gridconfig;
	}

	public void setText(String text) {
		this.text = text;
	}

	public void setConfigsql(String configsql) {
		this.configsql = configsql;
	}

	public void setUseconfigsql(boolean useconfigsql) {
		this.useconfigsql = useconfigsql;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public void setIdfieldname(String idfieldname) {
		this.idfieldname = idfieldname;
	}

	public void setSingleselect(boolean singleselect) {
		this.singleselect = singleselect;
	}

	public void setSelectbdcdy(boolean selectbdcdy) {
		this.selectbdcdy = selectbdcdy;
	}

	public void setSelectql(boolean selectql) {
		this.selectql = selectql;
	}

	public void setSelectqllx(String selectqllx) {
		this.selectqllx = selectqllx;
	}

	public void setBdcdylx(String bdcdylx) {
		this.bdcdylx = bdcdylx;
	}

	public void setLy(String ly) {
		this.ly = ly;
	}

	public void setResultconfig(ResultConfig resultconfig) {
		this.resultconfig = resultconfig;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setQueryconfig(QueryConfig queryconfig) {
		this.queryconfig = queryconfig;
	}

	public void setGridconfig(GridConfig gridconfig) {
		this.gridconfig = gridconfig;
	}

	public boolean isShowdetailbutton() {
		return showdetailbutton;
	}

	public void setShowdetailbutton(boolean showdetailbutton) {
		this.showdetailbutton = showdetailbutton;
	}
	
	public boolean isShowDataStyle() {
		return datastyle;
	}

	public void setShowDataStyle(boolean datastyle) {
		this.datastyle = datastyle;
	}
	/**
	 * 内部静态类：查询条件及字段配置
	 * @ClassName: QueryConfig
	 * @author liushufeng
	 * @date 2015年7月18日 下午5:39:59
	 */
	public static class QueryConfig implements Serializable {

		private static final long serialVersionUID = 7819312007125213823L;

		/**
		 * 构造函数
		 */
		public QueryConfig() {

		}

		/**
		 * 用来查询的字段列表
		 */
		private List<QueryField> fields;

		public List<QueryField> getFields() {
			return fields;
		}

		public void setFields(List<QueryField> fields) {
			this.fields = fields;
		}

		/**
		 * 内部静态类：查询字段类
		 * @ClassName: QueryField
		 * @author liushufeng
		 * @date 2015年7月18日 下午5:42:26
		 */
		public static class QueryField implements Serializable {

			/**
			 * @Fields serialVersionUID 
			 */
			private static final long serialVersionUID = 2098672753213278624L;

			public QueryField() {

			}

			/**
			 * 鏌ヨ瀛楁鍚?
			 */
			private String fieldname;
			/**
			 * 瀵瑰簲瀹炰綋鍚?
			 */
			private String entityname;
			/**
			 * 鏌ヨ瀛楁鍒悕
			 */
			private String fieldcaption;
			/**
			 * 鏌ヨ瀛楁绫诲瀷锛氭灇涓撅紝鏂囨湰锛屾棩鏈燂紝
			 */
			private String fieldtype;

			public String getFieldname() {
				return fieldname;
			}

			public void setFieldname(String fieldname) {
				this.fieldname = fieldname;
			}

			public String getEntityname() {
				return entityname;
			}

			public void setEntityname(String entityname) {
				this.entityname = entityname;
			}

			public String getFieldcaption() {
				return fieldcaption;
			}

			public void setFieldcaption(String fieldcaption) {
				this.fieldcaption = fieldcaption;
			}

			public String getFieldtype() {
				return fieldtype;
			}

			public void setFieldtype(String fieldtype) {
				this.fieldtype = fieldtype;
			}
		}
	}

	/**
	 * 内部静态类：查询结果datagrid配置
	 * @ClassName: GridConfig
	 * @author liushufeng
	 * @date 2015年7月18日 下午5:40:23
	 */
	public static class GridConfig implements Serializable {

		private static final long serialVersionUID = -507746843533525470L;

		/**
		 * 构造函数
		 */
		public GridConfig() {

		}

		/**
		 * 是否有选择框
		 */
		private boolean checkbox;

		/**
		 * 单行选中
		 */
		private boolean singleselect;

		/**
		 * 是否默认选中第一行
		 */
		private boolean defaultselectfirst;

		/**
		 * 结果显示列列表
		 */
		private List<GridColumn> columns;;

		/**
		 * 选中某行之后是否显示详细信息
		 */
		private boolean showdetailafterselect;

		/**
		 * 详细信息面板配置
		 */
		private Detail detail;

		public boolean isDefaultselectfirst() {
			return defaultselectfirst;
		}

		public void setDefaultselectfirst(boolean defaultselectfirst) {
			this.defaultselectfirst = defaultselectfirst;
		}

		public boolean isShowdetailafterselect() {
			return showdetailafterselect;
		}

		public void setShowdetailafterselect(boolean showdetailafterselect) {
			this.showdetailafterselect = showdetailafterselect;
		}

		public Detail getDetail() {
			return detail;
		}

		public void setDetail(Detail detail) {
			this.detail = detail;
		}

		public List<GridColumn> getColumns() {
			return columns;
		}

		public void setColumns(List<GridColumn> columns) {
			this.columns = columns;
		}

		public boolean isCheckbox() {
			return checkbox;
		}

		public void setCheckbox(boolean checkbox) {
			this.checkbox = checkbox;
		}

		public boolean isSingleselect() {
			return singleselect;
		}

		public void setSingleselect(boolean singleselect) {
			this.singleselect = singleselect;
		}

		/**
		 * 内部静态类：结果列
		 * @ClassName: GridColumn
		 * @author liushufeng
		 * @date 2015年7月18日 下午5:44:07
		 */
		public static class GridColumn implements Serializable {

			/**
			 * @Fields serialVersionUID 
			 */
			private static final long serialVersionUID = 2569263091537358380L;

			public GridColumn() {

			}

			/**
			 * 对应的字段名
			 */
			private String fieldname;

			/**
			 * 列名
			 */
			private String columntext;

			private String width;

			public String getWidth() {
				return width;
			}

			public void setWidth(String width) {
				this.width = width;
			}

			public String getFieldname() {
				return fieldname;
			}

			public void setFieldname(String fieldname) {
				this.fieldname = fieldname;
			}

			public String getColumntext() {
				return columntext;
			}

			public void setColumntext(String columntext) {
				this.columntext = columntext;
			}

		}

		/**
		 * 内部静态类：详细信息面板配置类 
		 * @ClassName: Detail
		 * @author liushufeng
		 * @date 2015年7月18日 下午5:44:31
		 */
		public static class Detail implements Serializable {
			/**
			 * @Fields serialVersionUID 
			 */
			private static final long serialVersionUID = -4371874082782549811L;

			public Detail() {

			}

			private List<Field> fields;

			public List<Field> getFields() {
				return fields;
			}

			public void setFields(List<Field> fields) {
				this.fields = fields;
			}

			public static class Field implements Serializable {
				/**
				 * @Fields serialVersionUID 
				 */
				private static final long serialVersionUID = 1827985202273389855L;

				public Field() {

				}

				private String fieldname;

				public String getFieldname() {
					return fieldname;
				}

				public void setFieldname(String fieldname) {
					this.fieldname = fieldname;
				}

				public String getFieldtext() {
					return fieldtext;
				}

				public void setFieldtext(String fieldtext) {
					this.fieldtext = fieldtext;
				}

				private String fieldtext;
				
				private String color;

				/** 
				 * @return color 
				 */
				public String getColor() {
					return color;
				}

				/** 
				 * @param color 要设置的 color 
				 */
				public void setColor(String color) {
					this.color = color;
				}

			}
		}
	}

	/**
	 * 用来排序的字段列表
	 */
	private List<SortField> sortfields;

	public List<SortField> getSortfields() {
		return sortfields;
	}

	public void setSortfields(List<SortField> fields) {
		this.sortfields = fields;
	}

	/**
	 * 内部静态类：排序字段类
	 * @ClassName: SortField
	 * @author 俞学斌
	 * @date 2015年11月24日 下午9:45:26
	 */
	public static class SortField implements Serializable {

		/**
		 * @Fields serialVersionUID 
		 */
		private static final long serialVersionUID = 18279852022349855L;

		public SortField() {

		}

		/**
		 * 鏌ヨ瀛楁鍚?
		 */
		private String fieldname;
		/**
		 * 瀵瑰簲瀹炰綋鍚?
		 */
		private String entityname;
		/**
		 * 鏌ヨ瀛楁鍒悕
		 */
		private String sorttype;
		/**
		 * 鏌ヨ瀛楁绫诲瀷锛氭灇涓撅紝鏂囨湰锛屾棩鏈燂紝
		 */

		public String getFieldname() {
			return fieldname;
		}

		public void setFieldname(String fieldname) {
			this.fieldname = fieldname;
		}

		public String getEntityname() {
			return entityname;
		}

		public void setEntityname(String entityname) {
			this.entityname = entityname;
		}

		public String getSorttype() {
			return sorttype;
		}

		public void setSorttype(String sorttype) {
			this.sorttype = sorttype;
		}

	}
	
	
	/**
	 * 内部静态类：结果常量值转义配置
	 * @ClassName: ResultConfig
	 * @author liushufeng
	 * @date 2015年7月18日 下午5:40:46
	 */
	public static class ResultConfig implements Serializable {

		private static final long serialVersionUID = 1599106046846220402L;

		public ResultConfig() {

		}

		/**
		 * 要映射为常量的字段
		 */
		private List<ConstMapping> constmappings;

		public List<ConstMapping> getConstmappings() {
			return constmappings;
		}

		public void setConstmappings(List<ConstMapping> constmappings) {
			this.constmappings = constmappings;
		}

		public static class ConstMapping implements Serializable {
			/**
			 * @Fields serialVersionUID
			 */
			private static final long serialVersionUID = -441811235141305609L;

			public ConstMapping() {

			}

			private String fieldname;
			private String consttype;
			private boolean newfieldendwithname;
			/** 
			 * @return newfieldendwithname 
			 */
			public boolean isNewfieldendwithname() {
				return newfieldendwithname;
			}

			/** 
			 * @param newfieldendwithname 要设置的 newfieldendwithname 
			 */
			public void setNewfieldendwithname(boolean newfieldendwithname) {
				this.newfieldendwithname = newfieldendwithname;
			}

			private String defaultvalue;

			public String getDefaultvalue() {
				return defaultvalue;
			}

			public void setDefaultvalue(String defaultrvalue) {
				this.defaultvalue = defaultrvalue;
			}

			public boolean isNewwithname() {
				return newfieldendwithname;
			}

			public void setNewwithname(boolean newwithname) {
				this.newfieldendwithname = newwithname;
			}

			public String getConsttype() {
				return consttype;
			}

			public void setConsttype(String consttype) {
				this.consttype = consttype;
			}

			public String getFieldname() {
				return fieldname;
			}

			public void setFieldname(String filedname) {
				this.fieldname = filedname;
			}

		}
	}
}
