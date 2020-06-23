/**   
 * TODO:@liushufeng:请描述这个文件
 * @Title: dd.java 
 * @Package com.supermap.wisdombusiness.core 
 * @author liushufeng 
 * @date 2015年11月3日 下午5:07:28 
 * @version V1.0   
 */

package com.supermap.wisdombusiness.core;

import java.util.Date;

import javax.persistence.Table;

import org.apache.log4j.Logger;
import org.apache.log4j.MDC;
import org.hibernate.event.spi.PostDeleteEvent;
import org.hibernate.event.spi.PostDeleteEventListener;
import org.hibernate.event.spi.PostInsertEvent;
import org.hibernate.event.spi.PostInsertEventListener;
import org.hibernate.event.spi.PostUpdateEvent;
import org.hibernate.event.spi.PostUpdateEventListener;
import org.hibernate.persister.entity.EntityPersister;
import org.springframework.stereotype.Component;

import com.supermap.realestate.registration.util.Global;
import com.supermap.realestate.registration.util.StringHelper;

/**
 * TODO:@liushufeng:请描述这个类或接口
 * @ClassName: dd
 * @author liushufeng
 * @date 2015年11月3日 下午5:07:28
 */
@Component
public class SuperHibernateListener implements PostUpdateEventListener, PostInsertEventListener, PostDeleteEventListener {

	private static final long serialVersionUID = 1L;

	private Logger logger = Logger.getLogger(SuperHibernateListener.class);

	@Override
	public void onPostDelete(PostDeleteEvent event) {
		try {
			// 操作时间
			String curTime = StringHelper.FormatByDatetime(new Date());
			// 操作人
			String curUser = Global.getCurrentUserName();
			// 操作IP
			String operateIP = "192.168.1.101";
			// 操作类型
			String operType = "删除记录";
			// 实体名
			String entityName = "";
			if (event != null && event.getEntity() != null && event.getEntity().getClass() != null) {
				entityName = event.getEntity().getClass().getSimpleName();
			}
			// 表名
			String tablename = "";
			if (event != null) {
				Object oo = event.getEntity();
				if (oo != null) {
					Class<?> tclass = oo.getClass();
					if (tclass != null) {
						Table tclass1 = tclass.getAnnotation(Table.class);
						if (tclass1 != null) {
							tablename = tclass1.name();
						}
					}
				}
			}
			// 主键名
			String keyName = "";
			{
				EntityPersister per = event.getPersister();
				if (per != null) {
					keyName = per.getIdentifierPropertyName();
				}
			}
			// 主键值
			String keyValue = "";
			if (event.getId() != null) {
				keyValue = event.getId().toString();
			}
			// 老记录
			String oldRecord = "[";
			Object[] os = event.getDeletedState();
			String[] propertynames = null;
			if (event.getPersister() != null) {
				propertynames = event.getPersister().getPropertyNames();
			}
			int i = 0;
			if (os != null && os.length > 0) {
				for (Object o : os) {
					if (i > 0)
						oldRecord += ",";
					oldRecord += propertynames[i] + ":";
					if (o == null) {
						oldRecord += "null";
					} else {
						oldRecord += o.toString();
					}
					i++;
				}
			}
			oldRecord += "]";

			MDC.put("OPERATETIME", curTime.toUpperCase());
			MDC.put("OPERATEUSER", curUser.toUpperCase());
			MDC.put("OPERATEIP", operateIP.toUpperCase());
			MDC.put("OPERATETYPE", operType.toUpperCase());
			MDC.put("ENTITYNAME", entityName);
			MDC.put("TABLENAME", tablename == null ? "" : tablename.toUpperCase());
			MDC.put("KEYNAME", keyName == null ? "" : keyName.toUpperCase());
			MDC.put("KEYVALUE", keyValue);
			MDC.put("NEWRECORD", "");
			MDC.put("OLDRECORD", oldRecord);
			MDC.put("UPDATECOLUMN", "");
			MDC.put("UPDATECOLUMNINDEX", "");
			SuperDataLog.Log(logger, "删除数据");
		} catch (Exception e) {
			logger.error("记录日志出错：删除数据");
		}
	}

	@Override
	public void onPostInsert(PostInsertEvent event) {
		try {
			// 操作时间
			String curTime = StringHelper.FormatByDatetime(new Date());
			// 操作人
			String curUser = Global.getCurrentUserName();
			// 操作IP
			String operateIP = "192.168.1.101";
			// 操作类型
			String operType = "新增记录";
			// 实体名
			String entityName = "";
			// 表名
			String tablename = "";
			Object oo = event.getEntity();
			if (oo != null) {
				Class<?> tclass = oo.getClass();
				if (tclass != null) {
					entityName = tclass.getSimpleName();
					Table t = tclass.getAnnotation(Table.class);
					if (t != null) {
						tablename = t.name();
					}
				}
			}

			// 主键名
			String keyName = "";
			EntityPersister per = event.getPersister();
			if (per != null) {
				keyName = per.getIdentifierPropertyName();
			}
			// 主键值
			String keyValue = "";
			if (event.getId() != null) {
				keyValue = event.getId().toString();
			}

			String[] propertynames = null;
			if (per != null) {
				propertynames = per.getPropertyNames();
			}

			// 新增纪录
			String newRecord = "[";
			int i = 0;
			if (event.getState() != null) {
				for (Object o : event.getState()) {
					if (i > 0)
						newRecord += ",";

					newRecord += propertynames[i] + ":";
					if (o == null) {
						newRecord += "null";
					} else {
						newRecord += o.toString();
					}
					i++;
				}
			}
			newRecord += "]";

			MDC.put("OPERATETIME", curTime.toUpperCase());
			MDC.put("OPERATEUSER", curUser.toUpperCase());
			MDC.put("OPERATEIP", operateIP.toUpperCase());
			MDC.put("OPERATETYPE", operType.toUpperCase());
			MDC.put("ENTITYNAME", entityName);
			MDC.put("TABLENAME", tablename.toUpperCase());
			MDC.put("KEYNAME", keyName.toUpperCase());
			MDC.put("KEYVALUE", keyValue);
			MDC.put("NEWRECORD", newRecord);
			MDC.put("OLDRECORD", "");
			MDC.put("UPDATECOLUMN", "");
			MDC.put("UPDATECOLUMNINDEX", "");
			SuperDataLog.Log(logger, "新增数据");
		} catch (Exception e) {
			logger.error("记录日志出错：新增数据");
		}
	}

	@Override
	public void onPostUpdate(PostUpdateEvent event) {
		try {

			EntityPersister per = event.getPersister();

			// 列名数组
			String[] strs = null;
			if (per != null) {
				strs = per.getPropertyNames();
			}
			// 操作时间
			String curTime = StringHelper.FormatByDatetime(new Date());
			// 操作人
			String curUser = Global.getCurrentUserName();
			// 操作IP
			String operateIP = "192.168.1.101";
			// 操作类型
			String operType = "更新记录";
			// 实体名
			String entityName = "";
			Object entity = event.getEntity();
			if (entity != null) {
				Class<?> tclass = entity.getClass();
				if (tclass != null) {
					entityName = tclass.getSimpleName();
				}
			}
			// 表名
			String tablename = "";
			if (entity != null) {
				Class<?> tclass = entity.getClass();
				if (tclass != null) {
					Table tt = tclass.getAnnotation(Table.class);
					if (tt != null) {
						tablename = tt.name();
					}
				}
			}
			// 主键名
			String keyName = "";
			if (per != null) {
				keyName = per.getIdentifierPropertyName();
			}
			// 主键值
			String keyValue = "";
			if (event.getId() != null) {
				keyValue = event.getId().toString();
			}
			// 老记录
			String oldRecord = "[";
			Object[] os = event.getOldState();
			int i = 0;
			if (os != null) {
				for (Object o : os) {
					if (i > 0)
						oldRecord += ",";
					oldRecord += strs[i] + ":";
					if (o == null) {
						oldRecord += "null";
					} else {
						oldRecord += o.toString();
					}
					i++;
				}
			}
			oldRecord += "]";

			// 新纪录
			String newRecord = "[";
			i = 0;
			if (event.getState() != null) {
				for (Object o : event.getState()) {
					if (i > 0)
						newRecord += ",";

					newRecord += strs[i] + ":";
					if (o == null) {
						newRecord += "null";
					} else {
						newRecord += o.toString();
					}
					i++;
				}
			}
			newRecord += "]";

			// 被更新列

			String updateColumns = "[";
			i = 0;
			if (event.getDirtyProperties() != null) {
				for (int o : event.getDirtyProperties()) {
					if (i > 0)
						updateColumns += ",";
					i++;
					updateColumns += strs[o];
				}
			}

			updateColumns += "]";
			// 被更新列索引
			i = 0;
			String updateColumnIndexs = "[";
			if (event.getDirtyProperties() != null) {
				for (int o : event.getDirtyProperties()) {
					if (i > 0)
						updateColumnIndexs += ",";
					i++;
					updateColumnIndexs += o;
				}
			}
			updateColumnIndexs += "]";

			MDC.put("OPERATETIME", curTime.toUpperCase());
			MDC.put("OPERATEUSER", curUser.toUpperCase());
			MDC.put("OPERATEIP", operateIP.toUpperCase());
			MDC.put("OPERATETYPE", operType.toUpperCase());
			MDC.put("ENTITYNAME", entityName);
			MDC.put("TABLENAME", StringHelper.isEmpty(tablename) ? "" : tablename.toUpperCase());
			MDC.put("KEYNAME", StringHelper.isEmpty(keyName) ? "" : keyName.toUpperCase());
			MDC.put("KEYVALUE", keyValue);
			MDC.put("OLDRECORD", oldRecord);
			MDC.put("NEWRECORD", newRecord);
			MDC.put("UPDATECOLUMN", updateColumns);
			MDC.put("UPDATECOLUMNINDEX", updateColumnIndexs);
			SuperDataLog.Log(logger, "修改数据");
		} catch (Exception e) {
			logger.error("记录日志出错：修改数据");
		}
	}

	@Override
	public boolean requiresPostCommitHanding(EntityPersister persister) {
		return false;
	}

}