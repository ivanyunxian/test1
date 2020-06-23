package com.supermap.realestate_gx.registration.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.supermap.realestate.registration.model.BDCS_DJDY_GZ;
import com.supermap.realestate.registration.model.BDCS_DJDY_XZ;
import com.supermap.realestate.registration.model.BDCS_H_GZ;
import com.supermap.realestate.registration.model.BDCS_H_XZ;
import com.supermap.realestate.registration.model.BDCS_QLR_GZ;
import com.supermap.realestate.registration.model.BDCS_QLR_XZ;
import com.supermap.realestate.registration.model.BDCS_QL_GZ;
import com.supermap.realestate.registration.model.BDCS_QL_XZ;
import com.supermap.realestate.registration.model.BDCS_XMXX;
import com.supermap.realestate.registration.util.ConstHelper;
import com.supermap.realestate.registration.util.ConstValue;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.realestate_gx.registration.service.GX_SmProSPService;
import com.supermap.wisdombusiness.workflow.dao.CommonDao;

@Component
public class GX_SmProSPServiceImpl implements GX_SmProSPService
{

	@Autowired
	CommonDao baseCommonDao;

	private static Logger logger = Logger.getLogger(GX_SmProSPServiceImpl.class);//用log4j写日志
	
	
	/**
	 * 动态意见模板根据标签定义获取对应单元信息
	 */
	public String getInfo(String yjnr, String project_id){
		
		List<BDCS_XMXX> list = baseCommonDao.findList(BDCS_XMXX.class, "project_id = " + "'" + project_id + "'");
		String xmbh = list.get(0).getId().toString();
		String djlx = list.get(0).getDJLX().toString();//登记类型
		String qllx = "";//权利类型
		
		String[] str = yjnr.toUpperCase().split("@S@");
		StringBuffer sb = new StringBuffer();
		String db, table, column, s="";
		
		for(int i = 0; i < str.length; i++){
        	if(str[i].indexOf("@") != -1){
        		db = str[i].substring(0, str[i].indexOf("@E@"));
        		table = str[i].substring(str[i].indexOf("@E@")+3, str[i].indexOf("@T@"));
        		column = str[i].substring(str[i].indexOf("@T@")+3, str[i].indexOf("@F@"));
        		
        		//需要区分权利类型时标签格式要包含“@Q权利类型代码”
        		if (str[i].indexOf("@F@Q") != -1) {
        			qllx = str[i].substring(str[i].indexOf("@F@Q")+4, str[i].indexOf("@D@"));
        		}
        		
        		str[i] = getResult(xmbh, djlx, qllx, db, table, column);
        	}
        	s += str[i];
        }
		return replaceBlank(s);
	}
	
	/**
	 * 标签字段转换操作
	 * @param xmbh
	 * @param djlx
	 * @param db
	 * @param table
	 * @param column
	 * @return
	 */
	public String getResult(String xmbh, String djlx, String qllx, String db, String table, String column ){
		
		//变更登记
		boolean BGDJ = ConstValue.DJLX.BGDJ.Value.equals(djlx);
		//更正登记
		boolean GZDJ = ConstValue.DJLX.GZDJ.Value.equals(djlx);
		
		List<Map> list = new ArrayList();
		
		String columnVal="", result="";
		String h_sql, ql_sql, qlr_sql, fsql_sql, sqr_sql, syqzd_sql, tdyt_sql;
		String djdy_wheresql, qlgz_wheresql, qlxz_wheresql, h_wheresql, qlr_wheresql;
		//一般字段类型
		String _sql = "SELECT " + column + " FROM " + db + "." + table;
		//date类型字段
		String _dateSql = "SELECT TO_CHAR("+ column +",'yyyy-MM-dd') "+ column +"" + " FROM " + db + "." + table;
		
		djdy_wheresql = "XMBH = '" + xmbh + "'";
		if (BGDJ) {//如果是变更登记
			djdy_wheresql += " AND LY = '02'";
		}
		List<BDCS_DJDY_GZ> djdy_gz = baseCommonDao.findList(BDCS_DJDY_GZ.class, djdy_wheresql);
		
		if (djdy_gz.size() < 1 && StringHelper.isEmpty(result)) {
			result = "【----】";
		} else if (djdy_gz.size() < 1 && !StringHelper.isEmpty(result)) {
			result += "、【----】";
		}
			
		for (int j = 0; j < djdy_gz.size(); j++) {
			
			boolean flag = true;
			
			//多单元情况下控制显示个数
			if (j > 2) {
				result += "......等" + djdy_gz.size() + "个";
				break;
			}
			
			//根据登记单元ID和项目编号获取到权利Info
			qlgz_wheresql = "DJDYID = '" + djdy_gz.get(j).getDJDYID().toString() + "' AND XMBH = '" + xmbh + "'";
			qlxz_wheresql = "DJDYID = '" + djdy_gz.get(j).getDJDYID().toString() + "'";
			
			if (!StringHelper.isEmpty(qllx)) {
				qlgz_wheresql += " AND QLLX = '" + qllx + "'";
				qlxz_wheresql += " AND QLLX = '" + qllx + "'";
			}
			List<BDCS_QL_GZ> ql_gz = baseCommonDao.findList(BDCS_QL_GZ.class, qlgz_wheresql);
			List<BDCS_QL_XZ> ql_xz = baseCommonDao.findList(BDCS_QL_XZ.class, qlxz_wheresql);
			
			//根据不动产单元ID获取户Info
			h_wheresql = "BDCDYID = '" + djdy_gz.get(j).getBDCDYID().toString() + "'";
			List<BDCS_H_GZ> h_gz = baseCommonDao.findList(BDCS_H_GZ.class, h_wheresql);
			List<BDCS_H_XZ> h_xz = baseCommonDao.findList(BDCS_H_XZ.class, h_wheresql);
			
			//户表（BDCS_H_GZ/XZ）
			if (table.equals("BDCS_H_GZ") || table.equals("BDCS_H_XZ")){
				//根据不动产单元ID获取户信息
				h_sql = _sql + " WHERE " + h_wheresql;
				list = baseCommonDao.getDataListByFullSql(h_sql);
			}
			
			//权利表（BDCS_QL_XZ）
    		if (table.equals("BDCS_QL_XZ")) {
    			//根据登记单元ID和项目编号获取到权利信息
    			if (column.equals("DJSJ") || column.equals("QLJSSJ") || column.equals("QLQSSJ")) {
    				ql_sql = _dateSql + " WHERE " + qlxz_wheresql;
    			} else {
    				ql_sql = _sql + " WHERE " + qlxz_wheresql;
    			}
				list = baseCommonDao.getDataListByFullSql(ql_sql);
    		}
    		
    		//权利表（BDCS_QL_GZ）
    		if (table.equals("BDCS_QL_GZ")) {
    			//根据登记单元ID和项目编号获取到权利信息
    			if (column.equals("DJSJ") || column.equals("QLJSSJ") || column.equals("QLQSSJ")) {
    				ql_sql = _dateSql + " WHERE " + qlgz_wheresql;
    			} else {
    				ql_sql = _sql + " WHERE " + qlgz_wheresql;
    			}
    			list = baseCommonDao.getDataListByFullSql(ql_sql);
    		}
    		
    		//权利人表（BDCS_QLR_GZ）
    		if (table.equals("BDCS_QLR_GZ") && ql_gz.size() > 0) {
				qlr_sql = _sql + " WHERE " + "QLID = '" + ql_gz.get(0).getId().toString() + "'";
				list = baseCommonDao.getDataListByFullSql(qlr_sql);
				
				if (list.size() > 0 && StringHelper.isEmpty(result)) {
    				result = "【"+ listToString(list, column) +"】";
    			} else if (list.size() > 0 && !StringHelper.isEmpty(result)) {
    				result += "、【"+ listToString(list, column) +"】";
    			} else if (list.size() < 1 && StringHelper.isEmpty(result)) {
    				result = "【----】";
    			} else if (list.size() < 1 && !StringHelper.isEmpty(result)) {
    				result += "、【----】";
    			}
				
				flag = false;
    		}
    		
    		//权利人表（BDCS_QLR_XZ）
			if (table.equals("BDCS_QLR_XZ") && ql_xz.size() > 0) {
				qlr_sql = _sql + " WHERE QLID = '" + ql_xz.get(0).getId().toString()+"'";
				list = baseCommonDao.getDataListByFullSql(qlr_sql);
				
				if (list.size() > 0 && StringHelper.isEmpty(result)) {
    				result = "【"+ listToString(list, column) +"】";
    			} else if (list.size() > 0 && !StringHelper.isEmpty(result)) {
    				result += "、【"+ listToString(list, column) +"】";
    			} else if (list.size() < 1 && StringHelper.isEmpty(result)) {
    				result = "【----】";
    			} else if (list.size() < 1 && !StringHelper.isEmpty(result)) {
    				result += "、【----】";
    			}
				
				flag = false;
			}
    		
    		//附属权利表（BDCS_FSQL_GZ）
    		if (table.equals("BDCS_FSQL_GZ") && ql_gz.size() > 0) {
    			//根据附属权利ID获取到附属权利字段信息
				fsql_sql = _sql + " WHERE QLID = '" + ql_gz.get(0).getId().toString()+"'";
				list = baseCommonDao.getDataListByFullSql(fsql_sql);
    		}
    		
    		//附属权利表（BDCS_FSQL_XZ）
			if (table.equals("BDCS_FSQL_XZ") && ql_xz.size() > 0) {
				//根据附属权利ID获取到附属权利字段信息
				fsql_sql = _sql + " WHERE QLID = '" + ql_xz.get(0).getId().toString()+"'";
				list = baseCommonDao.getDataListByFullSql(fsql_sql);
			}
    		
    		//不动产属_申请人表（BDCS_SQR）
    		if (table.equals("BDCS_SQR") && ql_gz.size() > 0) {
    			//根据权利ID获取权利人表Info
    			qlr_wheresql = "QLID = '" + ql_gz.get(0).getId().toString() + "'";
    			List<BDCS_QLR_GZ> qlr_gz = baseCommonDao.findList(BDCS_QLR_GZ.class, qlr_wheresql);

    			//根据权利ID获取到申请人表字段信息
				sqr_sql = _sql + " WHERE SQRID = '" + qlr_gz.get(0).getSQRID().toString() + "'";
				list = baseCommonDao.getDataListByFullSql(sqr_sql);
    		}
			
    		//使用权宗地_工作(BDCS_SHYQZD_GZ)
    		if (table.equals("BDCS_SHYQZD_GZ")) {
    			//根据不动产单元ID获取到使用权宗地表字段信息
    			if (djdy_gz.get(j).getBDCDYLX().equals("02")) {//使用权宗地
    				if (column.equals("DCRQ") || column.equals("CLRQ") || column.equals("SHRQ")) {
    					syqzd_sql = _dateSql + " WHERE BDCDYID = '" + djdy_gz.get(j).getBDCDYID().toString()+"'";
    					list = baseCommonDao.getDataListByFullSql(syqzd_sql);
    				} else {
    					syqzd_sql = _sql + " WHERE BDCDYID = '" + djdy_gz.get(j).getBDCDYID().toString()+"'";
    					list = baseCommonDao.getDataListByFullSql(syqzd_sql);
    				}
    			}
    			
    			if (djdy_gz.get(j).getBDCDYLX().equals("031") && h_gz.size() > 0) {//户
    				if (column.equals("DCRQ") || column.equals("CLRQ") || column.equals("SHRQ")) {
    					syqzd_sql = _dateSql + " WHERE BDCDYID = '" + h_gz.get(0).getZDBDCDYID().toString()+"'";
    					list = baseCommonDao.getDataListByFullSql(syqzd_sql);
    				} else {
    					syqzd_sql = _sql + " WHERE BDCDYID = '" + h_gz.get(0).getZDBDCDYID().toString()+"'";
    					list = baseCommonDao.getDataListByFullSql(syqzd_sql);
    				}
    			}
    		}
    		
    		//使用权宗地_现状(BDCS_SHYQZD_XZ)
    		if (table.equals("BDCS_SHYQZD_XZ")) {
    			//根据权利ID获取到申请人表字段信息
    			if (djdy_gz.get(j).getBDCDYLX().equals("02")) {//使用权宗地
    				syqzd_sql = _sql + " WHERE BDCDYID = '" + djdy_gz.get(j).getBDCDYID().toString()+"'";
    				list = baseCommonDao.getDataListByFullSql(syqzd_sql);
    			}
    			
    			if (djdy_gz.get(j).getBDCDYLX().equals("031") && h_xz.size() > 0) {//户
    				syqzd_sql = _sql + " WHERE BDCDYID = '" + h_xz.get(0).getZDBDCDYID().toString()+"'";
    				list = baseCommonDao.getDataListByFullSql(syqzd_sql);
    			}
    		}
    		
    		//土地用途（BDCS_TDYT_GZ）
    		if (table.equals("BDCS_TDYT_GZ")) {
    			if (djdy_gz.get(j).getBDCDYLX().equals("02")) {//使用权宗地
    				if (column.equals("QSRQ") || column.equals("ZZRQ")) {
    					tdyt_sql = _dateSql + " WHERE BDCDYID = '" + djdy_gz.get(j).getBDCDYID().toString()+"'";
    					list = baseCommonDao.getDataListByFullSql(tdyt_sql);
    				} else {
    					tdyt_sql = _sql + " WHERE BDCDYID = '" + djdy_gz.get(j).getBDCDYID().toString()+"'";
    					list = baseCommonDao.getDataListByFullSql(tdyt_sql);
    				}
    			}
    			
    			if (djdy_gz.get(j).getBDCDYLX().equals("031") && h_gz.size() > 0) {//户
    				if (column.equals("QSRQ") || column.equals("ZZRQ")) {
    					tdyt_sql = _dateSql + " WHERE BDCDYID = '" + h_gz.get(0).getZDBDCDYID().toString()+"'";
        				list = baseCommonDao.getDataListByFullSql(tdyt_sql);
    				} else {
    					tdyt_sql = _sql + " WHERE BDCDYID = '" + h_gz.get(0).getZDBDCDYID().toString()+"'";
        				list = baseCommonDao.getDataListByFullSql(tdyt_sql);
    				}
    			}
    			
    			if (list.size() > 0 && StringHelper.isEmpty(result)) {
    				result = "【"+ listToString(list, column) +"】";
    			} else if (list.size() > 0 && !StringHelper.isEmpty(result)) {
    				result += "、【"+ listToString(list, column) +"】";
    			} else if (list.size() < 1 && StringHelper.isEmpty(result)) {
    				result = "【----】";
    			} else if (list.size() < 1 && !StringHelper.isEmpty(result)) {
    				result += "【----】";
    			}
    			
    			flag = false;
    		}
    		
    		//土地用途_现状（BDCS_TDYT_XZ）
    		if (table.equals("BDCS_TDYT_XZ")) {
    			if (djdy_gz.get(j).getBDCDYLX().equals("02")) {//使用权宗地
    				if (column.equals("QSRQ") || column.equals("ZZRQ")) {
    					tdyt_sql = _dateSql + " WHERE BDCDYID = '" + djdy_gz.get(j).getBDCDYID().toString()+"'";
    					list = baseCommonDao.getDataListByFullSql(tdyt_sql);
    				} else {
    					tdyt_sql = _sql + " WHERE BDCDYID = '" + djdy_gz.get(j).getBDCDYID().toString()+"'";
    					list = baseCommonDao.getDataListByFullSql(tdyt_sql);
    				}
    			}
    			
    			if (djdy_gz.get(j).getBDCDYLX().equals("031") && h_xz.size() > 0) {//户
    				if (column.equals("QSRQ") || column.equals("ZZRQ")) {
    					tdyt_sql = _dateSql + " WHERE BDCDYID = '" + h_xz.get(0).getZDBDCDYID().toString()+"'";
        				list = baseCommonDao.getDataListByFullSql(tdyt_sql);
    				} else {
    					tdyt_sql = _sql + " WHERE BDCDYID = '" + h_xz.get(0).getZDBDCDYID().toString()+"'";
        				list = baseCommonDao.getDataListByFullSql(tdyt_sql);
    				}
    			}
    			
    			if (list.size() > 0 && StringHelper.isEmpty(result)) {
    				result = "【"+ listToString(list, column) +"】";
    			} else if (list.size() > 0 && !StringHelper.isEmpty(result)) {
    				result += "、【"+ listToString(list, column) +"】";
    			} else if (list.size() < 1 && StringHelper.isEmpty(result)) {
    				result = "【----】";
    			} else if (list.size() < 1 && !StringHelper.isEmpty(result)) {
    				result += "【----】";
    			}
    			
    			flag = false;
    		}
    		
    		if (flag) {
    			if (list.size() > 0) {
    				if (!StringHelper.isEmpty(list.get(0).get(column))) {
    					if (column.equals("FWTDYT")) {
    						columnVal = ConstHelper.getNameByValue("TDYT", list.get(0).get(column).toString());
    					} else if (column.equals("FWYT1")) {
    						columnVal = ConstHelper.getNameByValue("FWYT", list.get(0).get(column).toString());
    					} else {
    						columnVal = ConstHelper.getNameByValue(column, list.get(0).get(column).toString());
    					}
    				}
    				if (!StringHelper.isEmpty(columnVal) && StringHelper.isEmpty(result)) {
    					result = "【"+ columnVal +"】";
    				} else if (!StringHelper.isEmpty(columnVal) && !StringHelper.isEmpty(result)) {
    					result += "、【"+ columnVal +"】";
    				} else if (StringHelper.isEmpty(result)) {
    					result = "【"+(!StringHelper.isEmpty(list.get(0).get(column)) ? list.get(0).get(column).toString():"----")+"】";
    				} else {
    					result += "、【"+(!StringHelper.isEmpty(list.get(0).get(column)) ? list.get(0).get(column).toString():"----")+"】";
    				}
    			} else {
    				result += "【----】";
    			}
    		}
		}
		
		return result;
	}
	
	/**
	 * 去除字符串中的空格、回车、换行符、制表符
	 * @param str
	 * @return
	 */
	public static String replaceBlank(String str) {
        String dest = "";
        if (str!=null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }
	
	/**
	 * 组拼字符串 list转stringbuilder拼接过程中 自动处理顿号、
	 * @param list
	 * @return
	 */
	public String listToString(List<Map> list, String column)
    {
        StringBuilder str=new StringBuilder();
        for(int i = 0; i < list.size(); i++)
        {
            if(i == list.size()-1)//当循环到最后一个的时候 就不添加顿号、
            {
                str.append(list.get(i).get(column));
            }
            else {
                str.append(list.get(i).get(column));
                str.append("、");
            }
        }
        return str.toString();
    }
	
}