package com.supermap.realestate.registration.util;


import java.util.List;
import java.util.Map;

import com.supermap.realestate.registration.model.Log_UploadDis;
import com.supermap.wisdombusiness.core.SuperSpringContext;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
/**
 * 
 * @author lx
 * 2018年10月23号
 * 收件资料分布式公共方法类
 *
 */
public class UploadDiskHelper {
	
		private static CommonDao dao;
		static {
			if (dao == null) {
				dao = SuperSpringContext.getContext().getBean(CommonDao.class);
			}
		}
		/**
		 * 查询操作时间最大的一行数据
		 * @return Log_UploadDis
		 */
		@SuppressWarnings({ "rawtypes"})
		public  static String getMaxLog_UploadDis() {
			String sql="select FULLIS from LOG.Log_UploadDis where  LGTIME=(SELECT MAX(L.LGTIME) FROM LOG.LOG_UPLOADDIS L ) ";
			List<Map> log_uploaddis=dao.getDataListByFullSql(sql);
			if(log_uploaddis!=null&&log_uploaddis.size()>0
					&&log_uploaddis.get(0).get("FULLIS")!=null
					&&!StringHelper.isEmpty(log_uploaddis.get(0).get("FULLIS"))) {
				return log_uploaddis.get(0).get("FULLIS").toString();
			}
			return null;
		}
		/**
		 * 检查是否所有盘符都满
		 * @return boolean
		 */
		public  static boolean isFull() {
			
			String isfull=getMaxLog_UploadDis();
			if(isfull!=null) {
				if(isfull!=null&&isfull.equals("-1")) {
					return true;
				}
			}
			return false;
		}
}
