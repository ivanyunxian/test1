package com.supermap.realestate.registration.dataExchange.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.activemq.util.osgi.Activator;

import com.supermap.realestate.registration.util.DateUtil;
import com.supermap.wisdombusiness.framework.model.gxdjk.Fdcq_dj;
import com.supermap.wisdombusiness.framework.model.gxdjk.Gxjhxm_dj;
import com.supermap.wisdombusiness.framework.model.gxdjk.H_dj;
import com.supermap.wisdombusiness.framework.model.gxdjk.Qlr_dj;
import com.supermap.wisdombusiness.framework.model.gxjyk.JYQLR;
import com.supermap.yingtanothers.model.GXJHXM;

public class Tools {
	/**
	 * 根据给定的字段名集合，值来源对象，构建sql语句
	 * 
	 * @author:likun
	 * @param tableName
	 *            要插入的表名
	 * @param fieldsArr
	 *            要插入的表中的字段名集合
	 * @param modelObj
	 *            值来源model对象
	 * @return insert语句
	 */
	static String createInsertSQL(String tableName, String[] fieldsArr,
			Object modelObj) {
		String sql = "insert into " + tableName + " (";
		try{
		for (String f : fieldsArr) {
			sql += f + ",";
		}
		sql = sql.substring(0, sql.length() - 1) + ") values(";
		Method[] ms = modelObj.getClass().getMethods();
		for (String f : fieldsArr) {
			Method getM = null;
			for (Method m : ms) {
				// 循环所有方法匹配
				if (m.getName().equalsIgnoreCase("get" + f)) {
					getM = m;
					break;
				}
			}
			// 通过调用get方法来获取属性值
			if (getM != null) {
				Object val = null;
				String dtVal = "";
				try {
					val = getM.invoke(modelObj, null);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
				// 如果是日期型的要转换一下,其他类型不用
				Class rt = getM.getReturnType();
				if (rt.getName().contains("Date")) {
					//如果是cts格式日期需要先转换
					if(val!=null&&val.toString().contains("CST")){
						val=DateUtil.convertCSTDate(val.toString());
					}
					dtVal = val == null ? "1900-1-1" : val.toString();
					sql += "to_date('" + dtVal + "', 'yyyy-mm-dd hh24:mi:ss')";
				} else {
					sql += val + ",";
				}
			} else {
				sql += "'',";
			}
		}
		sql = sql.substring(0, sql.length() - 1) + ")";
		}
		catch(Exception ex){
		}
		return sql;
	}

	/**
	 * 根据给定的字段名集合，值来源对象，构建sql语句，一张表来源1个对象，外加多个指定的值
	 * 
	 * @Title:
	 * @author:likun
	 * @param tableName
	 *            要插入的表名
	 * @param fieldsArr1
	 *            要插入的表中的字段名集合
	 * @param modelObj1
	 *            值来源model对象
	 * @param otherfields
	 *            要另外单独插入的表中的字段名集合
	 * @param othervalues
	 *            要另外单独插入的表中的字段值集合
	 * @return insert语句
	 */
	public static String createInsertSQL(String tableName, String[] fieldsArr1,
			Object modelObj1, String[] otherfields, String[] othervalues) {
		String sql = "insert into " + tableName + " (";
		for (String f : fieldsArr1) {
			sql += f + ",";
		}
		for (String f : otherfields) {
			sql += f + ",";
		}
		sql = sql.substring(0, sql.length() - 1) + ") values(";
		Method[] ms =modelObj1==null?null: modelObj1.getClass().getMethods();
		for (String f : fieldsArr1) {
			Method getM = null;
			for (Method m : ms) {
				//如果是户和权利人的时候，获取ID名称特殊处理
				if ((tableName.equalsIgnoreCase("GXDJK.H")
						|| tableName.equalsIgnoreCase("GXDJK.BGQH"))&&f.equalsIgnoreCase("BDCDYID")) {
                      if(m.getName().equalsIgnoreCase("getid")){
                    	  getM = m;
      					break;
                      }
				}
				if ((tableName.contains("QLR")
						|| tableName.contains("qlr"))&&f.equalsIgnoreCase("QLRID"))  {
					if(m.getName().equalsIgnoreCase("getid")){
                  	  getM = m;
    					break;
                    }
				}
				if ((tableName.contains("FDCQ")
						|| tableName.contains("BGQFDCQ")|| tableName.contains("DYAQ")
						|| tableName.contains("BGQDYAQ")|| tableName.contains("YGDJ")
						|| tableName.contains("YYDJ")
						|| tableName.contains("CFDJ")|| tableName.contains("XZDJ")
						|| tableName.contains("FDCQZXDJ"))
						&&f.equalsIgnoreCase("QLID"))  {
					if(m.getName().equalsIgnoreCase("getid")){
                  	  getM = m;
    					break;
                    }
				}
				//特殊处理债务履行起始时间和结束时间
				if (f.equalsIgnoreCase("ZWLXQSSJ"))  {
					if(m.getName().equalsIgnoreCase("getqlqssj")){
                  	  getM = m;
    					break;
                    }
				}
				if (f.equalsIgnoreCase("ZWLXJSSJ"))  {
					if(m.getName().equalsIgnoreCase("getqljssj")){
                  	  getM = m;
    					break;
                    }
				}
				if (f.equalsIgnoreCase("BDCDJZMH"))  {
					if(m.getName().equalsIgnoreCase("getbdcqzh")){
                  	  getM = m;
    					break;
                    }
				}
				// 循环所有方法匹配
				if (m.getName().equalsIgnoreCase("get" + f)) {
					getM = m;
					break;
				}
			}
			// 通过调用get方法来获取属性值
			if (getM != null) {
				Object val = null;
				String dtVal = "";
				try {
					val = getM.invoke(modelObj1, null);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
				// 如果是日期型的要转换一下,其他类型不用
				Class rt = getM.getReturnType();
				if (rt.getName().contains("Date")) {
					//如果是cts格式日期需要先转换
					if(val!=null&&val.toString().contains("CST")){
						val=DateUtil.convertCSTDate(val.toString());
					}
					dtVal = val == null ? "1900-1-1 00:00:01" : val.toString().replace(".0", "");
					sql += "to_date('" + dtVal + "', 'yyyy-mm-dd hh24:mi:ss'),";
				} else if (rt.getName().contains("String")) {
					if (val == null) {
						sql += "'',";
					} else {
						sql += "'" + val.toString().replace("'", "") + "',";
					}
				} else {
					sql += val + ",";
				}
			} else {
				sql += "'',";
			}
		}
		// 如果是空字符串，用单引号括起来
		for (String v : othervalues) {
			if (v == null||v.isEmpty()) {
				v = "";
			}
			
			 if(v.contains("to_date")){
				 //如果是日期型的不加单引号
				 sql +=  v + ",";
			}
			 else{
				 sql += "'" + v + "',";
			 }
		}

		sql = sql.substring(0, sql.length() - 1) + ")";
		return sql;
	}

	/**
	 * 根据给定的字段名集合，值来源对象，构建sql语句，一张表来源2个对象
	 * 
	 * @Title: copyObjects
	 * @author:likun
	 * @param tableName
	 *            要插入的表名
	 * @param fieldsArr1
	 *            要插入的表中的字段名集合
	 * @param modelObj1
	 *            值来源model对象
	 * @param fieldsArr2
	 *            要插入的表中的字段名集合
	 * @param modelObj2
	 *            值来源model对象
	 * @param otherfields
	 *            要另外单独插入的表中的字段名集合
	 * @param othervalues
	 *            要另外单独插入的表中的字段值集合
	 * @return insert语句
	 */
	public static String createInsertSQL(String tableName, String[] fieldsArr1,
			Object modelObj1, String[] fieldsArr2, Object modelObj2,
			String[] otherfields, String[] othervalues) {
		String sql = "insert into " + tableName + " (";
		for (String f : fieldsArr1) {
			sql += f + ",";
		}
		for (String f : fieldsArr2) {
			sql += f + ",";
		}
		for (String f : otherfields) {
			sql += f + ",";
		}
		sql = sql.substring(0, sql.length() - 1) + ") values(";
		// 从第一个对象获取值
		Method[] ms =modelObj1==null?null: modelObj1.getClass().getMethods();
		for (String f : fieldsArr1) {
			Method getM = null;
			for (Method m : ms) {
				//如果是户和权利人的时候，获取ID名称特殊处理
				if ((tableName.equalsIgnoreCase("GXDJK.H")
						|| tableName.equalsIgnoreCase("GXDJK.BGQH"))&&f.equalsIgnoreCase("BDCDYID")) {
                      if(m.getName().equalsIgnoreCase("getid")){
                    	  getM = m;
      					break;
                      }
				}
				if ((tableName.contains("QLR")
						|| tableName.contains("qlr"))&&f.equalsIgnoreCase("QLRID"))  {
					if(m.getName().equalsIgnoreCase("getid")){
                  	  getM = m;
    					break;
                    }
				}
				if ((tableName.contains("FDCQ")
						|| tableName.contains("BGQFDCQ")|| tableName.contains("DYAQ")
						|| tableName.contains("BGQDYAQ")|| tableName.contains("YGDJ")
						|| tableName.contains("YYDJ")
						|| tableName.contains("CFDJ")|| tableName.contains("XZDJ")
						|| tableName.contains("FDCQZXDJ"))
						&&f.equalsIgnoreCase("QLID"))  {
					if(m.getName().equalsIgnoreCase("getid")){
                  	  getM = m;
    					break;
                    }
				}
				//特殊处理债务履行起始时间和结束时间
				if (f.equalsIgnoreCase("ZWLXQSSJ"))  {
					if(m.getName().equalsIgnoreCase("getqlqssj")){
                  	  getM = m;
    					break;
                    }
				}
				if (f.equalsIgnoreCase("ZWLXJSSJ"))  {
					if(m.getName().equalsIgnoreCase("getqljssj")){
                  	  getM = m;
    					break;
                    }
				}
				if (f.equalsIgnoreCase("BDCDJZMH"))  {
					if(m.getName().equalsIgnoreCase("getbdcqzh")){
                  	  getM = m;
    					break;
                    }
				}
				// 循环所有方法匹配
				if (m.getName().equalsIgnoreCase("get" + f)) {
					getM = m;
					break;
				}
			}
			// 通过调用get方法来获取属性值
			if (getM != null) {
				Object val = null;
				String dtVal = "";
				try {
					val = getM.invoke(modelObj1, null);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
				// 如果是日期型的要转换一下,其他类型不用
				Class rt = getM.getReturnType();
				if (rt.getName().contains("Date")) {
					//如果是cts格式日期需要先转换
					if(val!=null&&val.toString().contains("CST")){
						val=DateUtil.convertCSTDate(val.toString());
					}
					dtVal = val == null ? "1900-01-01 00:00:01" : val.toString().replace(".0", "");
					sql += "to_date('" + dtVal + "', 'yyyy-mm-dd hh24:mi:ss'),";
				} else if (rt.getName().contains("String")) {
					if (val == null) {
						sql += "'',";
					} else {
						//去掉单引号
						sql += "'" + val.toString().replace("'", "") + "',";
					}
				} else {
					sql += val + ",";
				}
			} else {
				sql += "'',";
			}
		}
		// 从第二个对象获取值
		ms =modelObj2==null?null:  modelObj2.getClass().getMethods();
		for (String f : fieldsArr2) {
			Method getM = null;
			for (Method m : ms) {
				//如果是户和权利人的时候，获取ID名称特殊处理
				if ((tableName.equalsIgnoreCase("GXDJK.H")
						|| tableName.equalsIgnoreCase("GXDJK.BGQH"))&&f.equalsIgnoreCase("BDCDYID")) {
                      if(m.getName().equalsIgnoreCase("getid")){
                    	  getM = m;
      					break;
                      }
				}
				//如果是查封登记的时候，获取解封登记时间特殊处理 从附属权利中的注销时间获取 2016年12月16日 15:49:00 吉林房产提出
				if (tableName.contains("CFDJ")&&f.equalsIgnoreCase("JFDJSJ")) {
                      if(m.getName().equalsIgnoreCase("getzxsj")){
                    	  getM = m;
      					break;
                      }
				}
				if ((tableName.contains("QLR")
						|| tableName.contains("BGQQLR"))&&f.equalsIgnoreCase("QLRID"))  {
					if(m.getName().equalsIgnoreCase("getid")){
                  	  getM = m;
    					break;
                    }
				}
				if ((tableName.contains("FDCQ")
						|| tableName.contains("BGQFDCQ")|| tableName.contains("DYAQ")
						|| tableName.contains("BGQDYAQ")|| tableName.contains("YGDJ")
						|| tableName.contains("YYDJ")
						|| tableName.contains("CFDJ")|| tableName.contains("XZDJ")
						|| tableName.contains("FDCQZXDJ"))
						&&f.equalsIgnoreCase("QLID"))  {
					if(m.getName().equalsIgnoreCase("getid")){
                  	  getM = m;
    					break;
                    }
				}
				// 循环所有方法匹配
				if (m.getName().equalsIgnoreCase("get" + f)) {
					getM = m;
					break;
				}
			}
			// 通过调用get方法来获取属性值
			if (getM != null) {
				Object val = null;
				String dtVal = "";
				try {
					val = getM.invoke(modelObj2, null);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
				// 如果是日期型的要转换一下,其他类型不用
				Class rt = getM.getReturnType();
				if (rt.getName().contains("Date")) {
					//如果是cts格式日期需要先转换
					if(val!=null&&val.toString().contains("CST")){
						val=DateUtil.convertCSTDate(val.toString());
					}
					if(getM.getName().equalsIgnoreCase("getzxsj") && val==null&&f.equalsIgnoreCase("JFDJSJ")){
						sql += "'',";
					}else if(getM.getName().equalsIgnoreCase("getzxsj") && val!=null&&f.equalsIgnoreCase("JFDJSJ")){
						//共享登记库查封登记表里解封登记时间是字符型的转出正常的不带毫秒的时间格式 吉林烦产提的2017年1月13日 11:59:35
						sql += "'"+val.toString().replace(".0", "")+"',";
					}else{
						dtVal = val == null ? "1900-01-01 00:00:01" : val.toString().replace(".0", "");
						sql += "to_date('" + dtVal + "', 'yyyy-mm-dd hh24:mi:ss'),";
					}
				} else if (rt.getName().contains("String")) {
					if (val == null) {
						sql += "'',";
					} else {
						sql += "'" + val.toString().replace("'", "") + "',";
					}
				} else {
					sql += val + ",";
				}
			} else {
				sql += "'',";
			}
		}

		// 如果是空字符串，用单引号括起来
		for (String v : othervalues) {
			if (v == null||v.isEmpty()) {
				v = "";
			}
			
			 if(v.contains("to_date")){
				 //如果是日期型的不加单引号
				 sql +=  v + ",";
			}
			 else{
				 sql += "'" + v + "',";
			 }
			
		}

		sql = sql.substring(0, sql.length() - 1) + ")";
		return sql;
	}


	/** 
	 * 一个项目推送要一个事务提交,一张表来源1个对象，外加多个指定的值
	* @author  buxiaobo
	* @date 创建时间：2017年1月19日 上午11:46:56 
	* @version 延吉房产提出一个项目推送要一个事务提交，在工具类里增加通用方法直接返回待保存对象 2017年1月18日 17:17:39 卜晓波
	* @parameter  反射取值，反射赋值
	* @since  
	* @return  
	 * @throws Exception 
	*/
	@SuppressWarnings("unchecked")
	public static <T> T createPushObj(String tableName, String[] fieldsArr1,
			Object modelObj1, String[] otherfields, String[] othervalues) throws Exception {
		try{
			if(true){
//				Gxjhxm_dj Gxjhxm_dj=new Gxjhxm_dj();
				//根据类名创建对象
				Class<?> classType = Class.forName(tableName);
				Object obj = classType.newInstance();
//				Object  o = Activator.CreateInstance(Gxjhxm_dj.getClass().toString());
				Method[] ms =modelObj1==null?null: modelObj1.getClass().getMethods();
				Field[] fields=obj.getClass().getDeclaredFields();
				Method[] methods=obj.getClass().getDeclaredMethods();
				for (String f : fieldsArr1) {
					f=f.toLowerCase();
					for(Field field:fields){
						String fieldname=field.getName();
						String fieldtype=field.getType().getSimpleName();
						String setMethod=pareSetName(fieldname);
						if(f.equals(fieldname)){
							Method getM = null;
							for (Method m : ms) {
								//如果是户和权利人的时候，获取ID名称特殊处理
								if ((tableName.contains("H_dj")
										|| tableName.contains("Bgqh_dj"))&&f.equalsIgnoreCase("BDCDYID")) {
				                      if(m.getName().equalsIgnoreCase("getid")){
				                    	  getM = m;
				      					break;
				                      }
								}
								if ((tableName.contains("QLR")|| tableName.contains("Qlr")|| tableName.contains("qlr"))&&f.equalsIgnoreCase("QLRID"))  {
									if(m.getName().equalsIgnoreCase("getid")){
				                  	  getM = m;
				    					break;
				                    }
								}
								if ((tableName.contains("Fdcq_dj")
										|| tableName.contains("Bgqfdcq_dj")|| tableName.contains("Dyaq_dj")
										|| tableName.contains("Bgqdyaq_dj")|| tableName.contains("Ygdj_dj")
										|| tableName.contains("Yydj_dj")
										|| tableName.contains("Cfdj_dj")|| tableName.contains("Xzdj_dj")
										|| tableName.contains("Fdcqzxdj_dj"))
										&&f.equalsIgnoreCase("QLID"))  {
									if(m.getName().equalsIgnoreCase("getid")){
				                  	  getM = m;
				    					break;
				                    }
								}
								//特殊处理债务履行起始时间和结束时间
								if (f.equalsIgnoreCase("ZWLXQSSJ"))  {
									if(m.getName().equalsIgnoreCase("getqlqssj")){
				                  	  getM = m;
				    					break;
				                    }
								}
								if (f.equalsIgnoreCase("ZWLXJSSJ"))  {
									if(m.getName().equalsIgnoreCase("getqljssj")){
				                  	  getM = m;
				    					break;
				                    }
								}
								if (f.equalsIgnoreCase("BDCDJZMH"))  {
									if(m.getName().equalsIgnoreCase("getbdcqzh")){
				                  	  getM = m;
				    					break;
				                    }
								}
								// 循环所有方法匹配
								if (m.getName().equalsIgnoreCase("get" + f)) {
									getM = m;
									break;
								}
							}
							// 通过调用get方法来获取属性值                                                                 
							if (getM != null) {
								Object val = null;
								Date dtVal = null;
								try {
									val = getM.invoke(modelObj1, null);
								} catch (IllegalArgumentException e) {
									e.printStackTrace();
								} catch (IllegalAccessException e) {
									e.printStackTrace();
								} catch (InvocationTargetException e) {
									e.printStackTrace();
								}
								// 如果是日期型的要转换一下,其他类型不用
								Class rt = getM.getReturnType();
								if (rt.getName().contains("Date")) {
									//如果是cts格式日期需要先转换
									if(val!=null&&val.toString().contains("CST")){
										val=DateUtil.convertCSTDate(val.toString());
									}
									SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
									dtVal = val == null ? sdf.parse("1900-1-1 00:00:01") : sdf.parse(val.toString().replace(".0", ""));
									System.out.println(dtVal.toString());  
							        Method method = obj.getClass().getMethod(setMethod, field.getType());  
							        System.out.println(method.getName()); 
							        method.invoke(obj, (Date)dtVal);
//									Gxjhxm_dj += "to_date('" + dtVal + "', 'yyyy-mm-dd hh24:mi:ss'),";
								} else if (rt.getName().contains("String")) {
									if (val == null) {
//										Gxjhxm_dj += "'',";
									} else {
								        Method method = obj.getClass().getMethod(setMethod, field.getType());  
								        System.out.println(method.getName());
								        if(null != val.toString().replace("'", "")){
							                if("String".equals(fieldtype)){
							                    method.invoke(obj, (String)val.toString().replace("'", ""));						                
							                    }
							            }
//										Gxjhxm_dj += "'" + val.toString().replace("'", "") + "',";
									}
								} else if (rt.getName().contains("Int")) {
									if (val == null) {
//										Gxjhxm_dj += "'',";
									} else {
								        Method method = obj.getClass().getMethod(setMethod, field.getType());
								        System.out.println(method.getName());
								        if(null != val.toString().replace("'", "")){
							                if("BigDecimal".equals(fieldtype)){
							                	method.invoke(obj, BigDecimal.valueOf(Double.parseDouble(val.toString())));
							                }else if("Integer".equals(fieldtype)){
							                	method.invoke(obj, Integer.parseInt(val.toString()));
							                }
							            }
									}
								}else if (rt.getName().contains("Double")) {
									if (val == null) {
//										Gxjhxm_dj += "'',";
									} else {
								        Method method = obj.getClass().getMethod(setMethod, field.getType());
								        System.out.println(method.getName());
								        if(null != val.toString().replace("'", "")){
							                if("BigDecimal".equals(fieldtype)){
							                	method.invoke(obj, BigDecimal.valueOf(Double.parseDouble(val.toString())));
							                }else if("Integer".equals(fieldtype)){
							                	method.invoke(obj, Integer.parseInt(val.toString()));
							                }else if("Double".equals(fieldtype)){
							                	method.invoke(obj, Double.parseDouble(val.toString()));
							                }
							            }
									}
								} else if (rt.getName().contains("Long")) {
									continue;
								} else {
							        Method method = obj.getClass().getMethod(setMethod, field.getType());
							        System.out.println(method.getName());
							        if(val!=null && null != val.toString().replace("'", "")){
						                if("String".equals(fieldtype)){
						                    method.invoke(obj, (String)val.toString().replace("'", ""));
						                }
						            }
//									Gxjhxm_dj += val + ",";
								}
							} else {
//								Gxjhxm_dj += "'',";
							}
						}
					}
				}
				// 因为此处不再是拼接sql,故其他附加字段只能依次循环取值赋值
				for (int i=0;i<otherfields.length;i++) {
					String otherfieldname=otherfields[i].toLowerCase();
					Object val = othervalues[i];
					for(Field field:fields){
						String fieldname=field.getName();
						String fieldtype=field.getType().getSimpleName();
						String setMethod=pareSetName(fieldname);
						if(otherfieldname.equals(fieldname)){
							if(val==null){
								continue;
							}
							Date dtVal = null;
							if ("Date".contains(fieldtype)) {
								//如果是cts格式日期需要先转换
								if(val!=null&&val.toString().contains("CST")){
									val=DateUtil.convertCSTDate(val.toString());
								}
								SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
								dtVal = val == null ? sdf.parse("1900-1-1 00:00:01") : sdf.parse(val.toString().replace(".0", ""));
								System.out.println(dtVal.toString());  
						        Method method = obj.getClass().getMethod(setMethod, field.getType());  
						        System.out.println(method.getName()); 
						        method.invoke(obj, (Date)dtVal);
//								Gxjhxm_dj += "to_date('" + dtVal + "', 'yyyy-mm-dd hh24:mi:ss'),";
							} else if("BigDecimal".contains(fieldtype)){
								Method method = obj.getClass().getMethod(setMethod, field.getType());
						        System.out.println(method.getName());
						        if(null != val && "null" != val.toString().replace("'", "")){
						        	method.invoke(obj, BigDecimal.valueOf(Double.parseDouble(val.toString())));
					            }
							}else if("Integer".equals(fieldtype)){
								Method method = obj.getClass().getMethod(setMethod, field.getType());
						        System.out.println(method.getName());
						        if(null != val && "null" != val.toString().replace("'", "")){
						        	method.invoke(obj, Integer.parseInt(val.toString()));
					            }
			                }else if("String".contains(fieldtype)){
								Method method = obj.getClass().getMethod(setMethod,field.getType());
								System.out.println(method.getName());
								if (null != val && "null" != val.toString().replace("'", "")) {
									method.invoke(obj,(String) val.toString().replace("'", ""));
								}
							}
						}
					}
				}
				return (T)obj;
			}
		}catch(Exception e){
			System.out.println(e.toString());
		}
		return null;
		
	}

	/** 
	 * 一个项目推送要一个事务提交,，一张表来源2个对象，外加多个指定的值
	* @author  buxiaobo
	* @date 创建时间：2017年1月19日 上午11:46:56 
	* @version 延吉房产提出一个项目推送要一个事务提交，在工具类里增加通用方法直接返回待保存对象 2017年1月18日 17:17:39 卜晓波
	* @parameter  反射取值，反射赋值
	* @since  
	* @return  
	 * @throws Exception 
	*/
	public static <T> T createPushObj(String tableName, String[] fieldsArr1,
			Object modelObj1, String[] fieldsArr2, Object modelObj2,
			String[] otherfields, String[] othervalues) {
		try{
			if(true){
//				Gxjhxm_dj Gxjhxm_dj=new Gxjhxm_dj();
				//根据类名创建对象
				Class classType = Class.forName(tableName);
				Object obj = classType.newInstance();
//				Object  o = Activator.CreateInstance(Gxjhxm_dj.getClass().toString());
				Method[] ms =modelObj1==null?null: modelObj1.getClass().getMethods();
				Method[] ms2 =modelObj2==null?null: modelObj2.getClass().getMethods();
				Field[] fields=obj.getClass().getDeclaredFields();
				Method[] methods=obj.getClass().getDeclaredMethods();
				for (String f : fieldsArr1) {
					f=f.toLowerCase();
					for(Field field:fields){
						String fieldname=field.getName();
						String fieldtype=field.getType().getSimpleName();
						String setMethod=pareSetName(fieldname);
						if(f.equals(fieldname)){
							Method getM = null;
							for (Method m : ms) {
								//如果是户和权利人的时候，获取ID名称特殊处理
								if ((tableName.contains("H_dj")
										|| tableName.contains("Bgqh_dj"))&&f.equalsIgnoreCase("BDCDYID")) {
				                      if(m.getName().equalsIgnoreCase("getid")){
				                    	  getM = m;
				      					break;
				                      }
								}
								if ((tableName.contains("QLR")|| tableName.contains("Qlr")|| tableName.contains("qlr"))&&f.equalsIgnoreCase("QLRID"))  {
									if(m.getName().equalsIgnoreCase("getid")){
				                  	  getM = m;
				    					break;
				                    }
								}
								if ((tableName.contains("Fdcq_dj")
										|| tableName.contains("Bgqfdcq_dj")|| tableName.contains("Dyaq_dj")
										|| tableName.contains("Bgqdyaq_dj")|| tableName.contains("Ygdj_dj")
										|| tableName.contains("Yydj_dj")
										|| tableName.contains("Cfdj_dj")|| tableName.contains("Xzdj_dj")
										|| tableName.contains("Fdcqzxdj_dj"))
										&&f.equalsIgnoreCase("QLID"))  {
									if(m.getName().equalsIgnoreCase("getid")){
				                  	  getM = m;
				    					break;
				                    }
								}
								//特殊处理债务履行起始时间和结束时间
								if (f.equalsIgnoreCase("ZWLXQSSJ"))  {
									if(m.getName().equalsIgnoreCase("getqlqssj")){
				                  	  getM = m;
				    					break;
				                    }
								}
								if (f.equalsIgnoreCase("ZWLXJSSJ"))  {
									if(m.getName().equalsIgnoreCase("getqljssj")){
				                  	  getM = m;
				    					break;
				                    }
								}
								if (f.equalsIgnoreCase("BDCDJZMH"))  {
									if(m.getName().equalsIgnoreCase("getbdcqzh")){
				                  	  getM = m;
				    					break;
				                    }
								}
								// 循环所有方法匹配
								if (m.getName().equalsIgnoreCase("get" + f)) {
									getM = m;
									break;
								}
							}
							// 通过调用get方法来获取属性值                                                                 
							if (getM != null) {
								Object val = null;
								Date dtVal = null;
								try {
									val = getM.invoke(modelObj1, null);
								} catch (IllegalArgumentException e) {
									e.printStackTrace();
								} catch (IllegalAccessException e) {
									e.printStackTrace();
								} catch (InvocationTargetException e) {
									e.printStackTrace();
								}
								// 如果是日期型的要转换一下,其他类型不用
								Class rt = getM.getReturnType();
								if (rt.getName().contains("Date")) {
									//如果是cts格式日期需要先转换
									if(val!=null&&val.toString().contains("CST")){
										val=DateUtil.convertCSTDate(val.toString());
									}
									SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
									dtVal = val == null ? sdf.parse("1900-1-1 00:00:01") : sdf.parse(val.toString().replace(".0", ""));
									System.out.println(dtVal.toString());  
							        Method method = obj.getClass().getMethod(setMethod, field.getType());  
							        System.out.println(method.getName()); 
							        method.invoke(obj, (Date)dtVal);
//									Gxjhxm_dj += "to_date('" + dtVal + "', 'yyyy-mm-dd hh24:mi:ss'),";
								} else if (rt.getName().contains("String")) {
									if (val == null) {
//										Gxjhxm_dj += "'',";
									} else {
								        Method method = obj.getClass().getMethod(setMethod, field.getType());  
								        System.out.println(method.getName());
								        if(null != val.toString().replace("'", "")){
							                if("String".equals(fieldtype)){
							                    method.invoke(obj, (String)val.toString().replace("'", ""));						                }
							            }
//										Gxjhxm_dj += "'" + val.toString().replace("'", "") + "',";
									}
								} else if (rt.getName().contains("Int")) {
									if (val == null) {
//										Gxjhxm_dj += "'',";
									} else {
								        Method method = obj.getClass().getMethod(setMethod, field.getType());
								        System.out.println(method.getName());
								        if(null != val.toString().replace("'", "")){
								        	 if("BigDecimal".equals(fieldtype)){
								                	method.invoke(obj, BigDecimal.valueOf(Double.parseDouble(val.toString())));
								                }else if("Integer".equals(fieldtype)){
								                	method.invoke(obj, Integer.parseInt(val.toString()));
								                }
							            }
									}
								}else if (rt.getName().contains("Double")) {
									if (val == null) {
//										Gxjhxm_dj += "'',";
									} else {
								        Method method = obj.getClass().getMethod(setMethod, field.getType());
								        System.out.println(method.getName());
								        if(null != val.toString().replace("'", "")){
							                if("BigDecimal".equals(fieldtype)){
							                	method.invoke(obj, BigDecimal.valueOf(Double.parseDouble(val.toString())));
							                }else if("Integer".equals(fieldtype)){
							                	method.invoke(obj, Integer.parseInt(val.toString()));
							                }else if("Double".equals(fieldtype)){
							                	method.invoke(obj, Double.parseDouble(val.toString()));
							                }
							            }
									}
								}  else {
							        Method method = obj.getClass().getMethod(setMethod, field.getType());
							        System.out.println(method.getName());
							        if(val!=null && null != val.toString().replace("'", "")){
						                if("String".equals(fieldtype)){
						                    method.invoke(obj, (String)val.toString().replace("'", ""));
						                }
						            }
//									Gxjhxm_dj += val + ",";
								}
							} else {
//								Gxjhxm_dj += "'',";
							}
						}
					}
				}
				for (String f : fieldsArr2) {
					f=f.toLowerCase();
					for(Field field:fields){
						String fieldname=field.getName();
						String fieldtype=field.getType().getSimpleName();
						String setMethod=pareSetName(fieldname);
						if(f.equals(fieldname)){
							Method getM = null;
							for (Method m : ms2) {
								//如果是户和权利人的时候，获取ID名称特殊处理
								if ((tableName.contains("H_dj")
										|| tableName.contains("Bgqh_dj"))&&f.equalsIgnoreCase("BDCDYID")) {
				                      if(m.getName().equalsIgnoreCase("getid")){
				                    	  getM = m;
				      					break;
				                      }
								}
								if ((tableName.contains("QLR")|| tableName.contains("Qlr")|| tableName.contains("qlr"))&&f.equalsIgnoreCase("QLRID"))  {
									if(m.getName().equalsIgnoreCase("getid")){
				                  	  getM = m;
				    					break;
				                    }
								}
								if ((tableName.contains("Fdcq_dj")
										|| tableName.contains("Bgqfdcq_dj")|| tableName.contains("Dyaq_dj")
										|| tableName.contains("Bgqdyaq_dj")|| tableName.contains("Ygdj_dj")
										|| tableName.contains("Yydj_dj")
										|| tableName.contains("Cfdj_dj")|| tableName.contains("Xzdj_dj")
										|| tableName.contains("Fdcqzxdj_dj"))
										&&f.equalsIgnoreCase("QLID"))  {
									if(m.getName().equalsIgnoreCase("getid")){
				                  	  getM = m;
				    					break;
				                    }
								}
								//解封登记 的解封登记时间 和 解封登簿人对应 附属权利的注销时间和注销登簿人 ————————————————————
								if ((tableName.contains("Cfdj_dj"))
										&&f.equalsIgnoreCase("JFDJSJ"))  {
									if(m.getName().equalsIgnoreCase("getzxsj")){
				                  	  getM = m;
				    					break;
				                    }
								}
								if ((tableName.contains("Cfdj_dj"))
										&&f.equalsIgnoreCase("JFDBR"))  {
									if(m.getName().equalsIgnoreCase("getzxdbr")){
				                  	  getM = m;
				    					break;
				                    }
								}
								//特殊处理债务履行起始时间和结束时间
								if (f.equalsIgnoreCase("ZWLXQSSJ"))  {
									if(m.getName().equalsIgnoreCase("getqlqssj")){
				                  	  getM = m;
				    					break;
				                    }
								}
								if (f.equalsIgnoreCase("ZWLXJSSJ"))  {
									if(m.getName().equalsIgnoreCase("getqljssj")){
				                  	  getM = m;
				    					break;
				                    }
								}
								if (f.equalsIgnoreCase("BDCDJZMH"))  {
									if(m.getName().equalsIgnoreCase("getbdcqzh")){
				                  	  getM = m;
				    					break;
				                    }
								}
								// 循环所有方法匹配
								if (m.getName().equalsIgnoreCase("get" + f)) {
									getM = m;
									break;
								}
							}
							// 通过调用get方法来获取属性值                                                                 
							if (getM != null) {
								Object val = null;
								Date dtVal = null;
								try {
									val = getM.invoke(modelObj2, null);
								} catch (IllegalArgumentException e) {
									e.printStackTrace();
								} catch (IllegalAccessException e) {
									e.printStackTrace();
								} catch (InvocationTargetException e) {
									e.printStackTrace();
								}
								// 如果是日期型的要转换一下,其他类型不用
								Class rt = getM.getReturnType();
								if (rt.getName().contains("Date")) {
									if (val == null) {
										
									} else {
										//如果是cts格式日期需要先转换
										if(val!=null&&val.toString().contains("CST")){
											val=DateUtil.convertCSTDate(val.toString());
										}
										SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
										dtVal = val == null ? sdf.parse("1900-1-1 00:00:01") : sdf.parse(val.toString().replace(".0", ""));
										System.out.println(dtVal.toString());  
								        Method method = obj.getClass().getMethod(setMethod, field.getType());  
								        System.out.println(method.getName());
								        //此处解封登记时间在共享登记库是字符型对日期型
								        if("String".equals(fieldtype)){
						                    method.invoke(obj, (String)val.toString().replace("'", ""));
						            	}else{
						            		method.invoke(obj, (Date)dtVal);
						            	}
									}
//									Gxjhxm_dj += "to_date('" + dtVal + "', 'yyyy-mm-dd hh24:mi:ss'),";
								} else if (rt.getName().contains("String")) {
									if (val == null) {
//										Gxjhxm_dj += "'',";
									} else {
								        Method method = obj.getClass().getMethod(setMethod, field.getType());  
								        System.out.println(method.getName());
								        if(null != val.toString().replace("'", "")){
							                if("String".equals(fieldtype)){
							                    method.invoke(obj, (String)val.toString().replace("'", ""));						                
							                    }
							            }
//										Gxjhxm_dj += "'" + val.toString().replace("'", "") + "',";
									}
								} else if (rt.getName().contains("Int")) {
									if (val == null) {
//										Gxjhxm_dj += "'',";
									} else {
								        Method method = obj.getClass().getMethod(setMethod, field.getType());
								        System.out.println(method.getName());
								        if(null != val.toString().replace("'", "")){
								        	 if("BigDecimal".equals(fieldtype)){
								                	method.invoke(obj, BigDecimal.valueOf(Double.parseDouble(val.toString())));
								                }else if("Integer".equals(fieldtype)){
								                	method.invoke(obj, Integer.parseInt(val.toString()));
								                }
							            }
									}
								} else if (rt.getName().contains("Double")) {
									if (val == null) {
//										Gxjhxm_dj += "'',";
									} else {
								        Method method = obj.getClass().getMethod(setMethod, field.getType());
								        System.out.println(method.getName());
								        if(null != val.toString().replace("'", "")){
							                if("BigDecimal".equals(fieldtype)){
							                	method.invoke(obj, BigDecimal.valueOf(Double.parseDouble(val.toString())));
							                }else if("Integer".equals(fieldtype)){
							                	method.invoke(obj, Integer.parseInt(val.toString()));
							                }else if("Double".equals(fieldtype)){
							                	method.invoke(obj, Double.parseDouble(val.toString()));
							                }
							            }
									}
								} else {
							        Method method = obj.getClass().getMethod(setMethod, field.getType());
							        System.out.println(method.getName());
							        if(val!=null && null != val.toString().replace("'", "")){
						                if("String".equals(fieldtype)){
						                    method.invoke(obj, (String)val.toString().replace("'", ""));
						                }
						            }
//									Gxjhxm_dj += val + ",";
								}
							} else {
//								Gxjhxm_dj += "'',";
							}
						}
					}
				}
				// 因为此处不再是拼接sql,故其他附加字段只能依次循环取值赋值
				for (int i=0;i<otherfields.length;i++) {
					String otherfieldname=otherfields[i].toLowerCase();
					Object val = othervalues[i];
					for(Field field:fields){
						String fieldname=field.getName();
						String fieldtype=field.getType().getSimpleName();
						String setMethod=pareSetName(fieldname);
						if(otherfieldname.equals(fieldname)){
							Method method = obj.getClass().getMethod(setMethod,field.getType());
							Date dtVal = null;
							if ("Date".contains(fieldtype)) {
								//如果是cts格式日期需要先转换
								if(val!=null&&val.toString().contains("CST")){
									val=DateUtil.convertCSTDate(val.toString());
								}
								SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
								dtVal = val == null ? sdf.parse("1900-1-1 00:00:01") : sdf.parse(val.toString().replace(".0", ""));
								System.out.println(dtVal.toString());
						        System.out.println(method.getName()); 
						        method.invoke(obj, (Date)dtVal);
//								Gxjhxm_dj += "to_date('" + dtVal + "', 'yyyy-mm-dd hh24:mi:ss'),";
							} else if("BigDecimal".contains(fieldtype)){
						        System.out.println(method.getName());
						        if(null != val.toString().replace("'", "")){
						        	method.invoke(obj, BigDecimal.valueOf(Double.parseDouble(val.toString())));
					            }
							}else if("Integer".equals(fieldtype)){
						        System.out.println(method.getName());
						        if(null != val.toString().replace("'", "")){
						        	method.invoke(obj, Integer.parseInt(val.toString()));
					            }
			                }else if("String".contains(fieldtype)){
								System.out.println(method.getName());
								if (val != null && null != val.toString().replace("'", "")) {
									method.invoke(obj,(String) val.toString().replace("'", ""));
								}
							}
						}
					}
				}
				return (T)obj;
			}
		}catch(Exception e){
			System.out.println(e.toString());
		}
		return null;
	}
	
	/** 
	 * 一个项目推送要一个事务提交,一张表来源1个对象，外加多个指定的值
	* @author  buxiaobo
	* @date 创建时间：2017年1月19日 上午11:46:56 
	* @version 延吉房产提出一个项目推送要一个事务提交，在工具类里增加通用方法直接返回待保存对象 2017年1月18日 17:17:39 卜晓波
	* @parameter  反射取值，反射赋值
	* @since  
	* @return  
	 * @throws Exception 
	*/
	@SuppressWarnings("unused")
	public static <T> T GxxmxxcreatePushObj(String tableName, String[] fieldsArr1,String[] fieldsArr2) throws Exception {
		try{
			if(true){
				//根据类名创建对象
				Class classType = Class.forName(tableName);
				Object obj = classType.newInstance();
				Field[] fields=obj.getClass().getDeclaredFields();
				Method[] methods=obj.getClass().getDeclaredMethods();
				for (int i=0;i<fieldsArr1.length;i++) {
					String f=fieldsArr1[i];
					f=f.toLowerCase();
					for(Field field:fields){
						String fieldname=field.getName();
						String fieldtype=field.getType().getSimpleName();
						String setMethod=pareSetName(fieldname);
						if(f.equals(fieldname)){
							Object val = fieldsArr2[i];
							Date dtVal=null;
							if (f.equals("slsj") || f.equals("tssj")) {
								SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
								dtVal = val == null ? sdf.parse("1900-1-1 00:00:01") : sdf.parse(val.toString().replace(".0", ""));
								System.out.println(dtVal.toString());  
						        Method method = obj.getClass().getMethod(setMethod, field.getType());  
						        System.out.println(method.getName()); 
						        method.invoke(obj, (Date)dtVal);
							} else if (fieldtype.contains("String")) {
								if (val == null) {
//									Gxjhxm_dj += "'',";
								} else {
							        Method method = obj.getClass().getMethod(setMethod, field.getType());  
							        System.out.println(method.getName());
							        if(null != val.toString().replace("'", "")){
						                if("String".equals(fieldtype)){
						                    method.invoke(obj, (String)val.toString().replace("'", ""));						                }
						            }
								}
							}else if("BigDecimal".contains(fieldtype)){
								Method method = obj.getClass().getMethod(setMethod, field.getType());
						        System.out.println(method.getName());
						        if(null != val.toString().replace("'", "")){
						        	method.invoke(obj, BigDecimal.valueOf(Double.parseDouble(val.toString())));
					            }
							}else {
						        Method method = obj.getClass().getMethod(setMethod, field.getType());
						        System.out.println(method.getName());
						        if(val!=null && null != val.toString().replace("'", "")){
					                if("String".equals(fieldtype)){
					                    method.invoke(obj, (String)val.toString().replace("'", ""));
					                }
					            }
							}
						}
					}
				}
				return (T)obj;
			}
		}catch(Exception e){
			System.out.println(e.toString());
		}
		return null;
		
	}
	
	@SuppressWarnings("unused")
	public static <T> T QJGxxmxxcreatePushObj(String tableName, String[] fieldsArr1,String[] fieldsArr2) throws Exception {
		try{
			if(true){
				//根据类名创建对象
				Class classType = Class.forName(tableName);
				Object obj = classType.newInstance();
				Field[] fields=obj.getClass().getDeclaredFields();
				Method[] methods=obj.getClass().getDeclaredMethods();
				for (int i=0;i<fieldsArr1.length;i++) {
					String f=fieldsArr1[i];
					f=f.toLowerCase();
					for(Field field:fields){
						String fieldname=field.getName();
						String fieldtype=field.getType().getSimpleName();
						String setMethod=pareSetName(fieldname);
						if(f.equals(fieldname)){
							Object val = fieldsArr2[i];
							Date dtVal=null;
							if (f.contains("sj") || f.contains("rq")) {
								SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
								dtVal = val == null ? sdf.parse("1900-1-1 00:00:01") : sdf.parse(val.toString());
								System.out.println(dtVal.toString());  
						        Method method = obj.getClass().getMethod(setMethod, field.getType());  
						        System.out.println(method.getName()); 
						        method.invoke(obj, (Date)dtVal);
							} else if (fieldtype.contains("String")) {
								if (val == null) {
//									Gxjhxm_dj += "'',";
								} else {
							        Method method = obj.getClass().getMethod(setMethod, field.getType());  
							        System.out.println(method.getName());
							        if(null != val.toString().replace("'", "")){
						                if("String".equals(fieldtype)){
						                    method.invoke(obj, (String)val.toString().replace("'", ""));						                }
						            }
								}
							}else if("BigDecimal".contains(fieldtype)){
								Method method = obj.getClass().getMethod(setMethod, field.getType());
						        System.out.println(method.getName());
						        if(null != val.toString().replace("'", "")){
						        	method.invoke(obj, BigDecimal.valueOf(Double.parseDouble(val.toString())));
					            }
							}else {
						        Method method = obj.getClass().getMethod(setMethod, field.getType());
						        System.out.println(method.getName());
						        if(val!=null && null != val.toString().replace("'", "")){
					                if("String".equals(fieldtype)){
					                    method.invoke(obj, (String)val.toString().replace("'", ""));
					                }
					            }
							}
						}
					}
				}
				return (T)obj;
			}
		}catch(Exception e){
			System.out.println(e.toString());
		}
		return null;
		
	}
	/**  
     * 拼接某属性set 方法  
     * @param fldname  
     * @return  
     */  
    public static String pareSetName(String fldname){  
        if(null == fldname || "".equals(fldname)){  
            return null;  
        }  
        String pro = "set"+fldname.substring(0,1).toUpperCase()+fldname.substring(1);  
        return pro;  
    }  
    /**  
     * 判断该方法是否存在  
     * @param methods  
     * @param met  
     * @return  
     */  
    public static boolean checkMethod(Method methods[],String met){  
        if(null != methods ){  
            for(Method method:methods){  
                if(met.equals(method.getName())){  
                    return true;  
                }  
            }  
        }          
        return false;  
    }  

}
