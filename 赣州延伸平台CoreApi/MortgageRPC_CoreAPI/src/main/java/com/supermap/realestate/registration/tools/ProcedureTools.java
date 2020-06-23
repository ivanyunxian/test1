package com.supermap.realestate.registration.tools;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Types;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.jdbc.ReturningWork;

import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.wisdombusiness.core.SuperSpringContext;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;

/**
 * 
 * 存储过程调用类
 * 
 * @ClassName: ProceduresTools
 * @author 俞学斌
 * @date 2016年10月10日 09:10:33
 */
public class ProcedureTools {
	/**
	 * 调用存储过程
	 * 
	 * @作者 俞学斌
	 * @创建时间 2016年10月10日10:16:50
	 * @param params 存储过程参数
	 * @param procedureName 存储过程名称
	 * @param procedureAlias 存储过程所在数据源
	 * @return
	 */
	public static HashMap<String,Object> executeProcedure(List<ProcedureParam> params,String procedureName,String procedureAlias) {
		final List<ProcedureParam> m_params = params;
		final String m_procedureName = procedureName;
		final String m_procedureAlias = procedureAlias;
		CommonDao dao = SuperSpringContext.getContext().getBean(CommonDao.class);
		Session session = dao.OpenSession();
		HashMap<String,Object> procedureInfo = session.doReturningWork(new ReturningWork<HashMap<String,Object>>() {
			@Override
			public HashMap<String,Object> execute(Connection connection) throws SQLException {
				StringBuilder str = new StringBuilder();
				str.append("{ Call ");
				str.append(m_procedureAlias);
				str.append(".");
				str.append(m_procedureName);
				str.append("(");
				if(m_params!=null&&m_params.size()>0){
					for(int i=0;i<m_params.size();i++){
						str.append("?");
						if(i!=(m_params.size()-1)){
							str.append(",");
						}
					}
				}
				str.append(") }");
				
				CallableStatement statement;
				statement = connection.prepareCall(str.toString());
				if(m_params!=null&&m_params.size()>0){
					for(ProcedureParam param:m_params){
						if("in".equals(param.getParamtype())){
							if(Types.NVARCHAR==param.getFieldtype()){
								statement.setString(param.getSxh(), StringHelper.formatObject(param.getValue()));
							}else if(Types.INTEGER==param.getFieldtype()){
								statement.setInt(param.getSxh(), StringHelper.getInt(param.getValue()));
							}else if(Types.DOUBLE==param.getFieldtype()){
								statement.setDouble(param.getSxh(), StringHelper.getDouble(param.getValue()));
							}
							else if(Types.DATE==param.getFieldtype()){
								try {
									statement.setDate(param.getSxh(), new Date(StringHelper.FormatByDate(param.getValue()).getTime()));
								} catch (ParseException e) {
									e.printStackTrace();
								}
							}
						}else if("out".equals(param.getParamtype())){
							statement.registerOutParameter(param.getSxh(), param.getFieldtype());
						}
					}
				}
				statement.execute();
				HashMap<String,Object> info = new HashMap<String,Object>();
				if(m_params!=null&&m_params.size()>0){
					for(ProcedureParam param:m_params){
						if("out".equals(param.getParamtype())){
							info.put(param.getName(), statement.getInt(param.getSxh()));
						}
					}
				}
				statement.close();
				return info;
			}
		});
		session.close();
		return procedureInfo;
	}
}

