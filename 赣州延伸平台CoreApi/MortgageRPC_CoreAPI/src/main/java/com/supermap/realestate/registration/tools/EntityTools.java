/**   
 * 请描述这个文件
 * @Title: EntityTool.java 
 * @Package com.supermap.realestate.registration.util 
 * @author liushufeng 
 * @date 2015年7月12日 下午5:20:33 
 * @version V1.0   
 */

package com.supermap.realestate.registration.tools;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.util.StringUtils;

import com.supermap.realestate.registration.util.ConstValue.DJDYLY;
import com.supermap.realestate.registration.util.ObjectHelper;
import com.supermap.wisdombusiness.core.SuperHelper;
import com.supermap.wisdombusiness.core.SuperModel;
import com.supermap.wisdombusiness.core.SuperSpringContext;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;

/**
 * 实体操作工具类，类似于dao
 * 
 * @ClassName: EntityTool
 * @author liushufeng
 * @date 2015年7月12日 下午5:20:33
 */
public class EntityTools {

	/**
	 * 根据实体名的前缀和单元来源获取实体名
	 * 
	 * @Title: getEntityName
	 * @author:liushufeng
	 * @date：2015年7月12日 上午1:57:32
	 * @param prefixName
	 * @param ly
	 * @return
	 */
	public static String getEntityName(String prefixName, DJDYLY ly) {
		if(DJDYLY.DC.equals(ly)){
			return prefixName.replaceAll("BDCS_", "DCS_")+DJDYLY.GZ.TableSuffix;
		}
		return prefixName + ly.TableSuffix;
	}

	/**
	 * 根据实体类名和主键id值从数据库加载实体实例
	 * 
	 * @Title: loadEntity
	 * @author:liushufeng
	 * @date：2015年7月12日 上午1:17:01
	 * @param bdcdylx
	 * @param ly
	 * @param bdcdyid
	 * @return
	 */
	static <T> T loadEntity(String entityName, String id) {
		CommonDao dao = SuperSpringContext.getContext().getBean(CommonDao.class);
		if (StringUtils.isEmpty(entityName))
			return null;
		Class<?> _objectClass = getEntityClass(entityName);
		@SuppressWarnings("unchecked")
		T _object = (T) dao.get(_objectClass, id);
		return _object;
	}

	/**
	 * 根据实体类名和条件从数据库加载实体实例列表
	 * 
	 * @Title: loadEntities
	 * @author:liushufeng
	 * @date：2015年7月12日 下午4:05:01
	 * @param entityName
	 *            实体类名
	 * @param hqlCondition
	 *            条件语句
	 * @return 对象列表
	 */
	static <T> List<T> loadEntities(String entityName, String hqlCondition) {
 		CommonDao dao = SuperSpringContext.getContext().getBean(CommonDao.class);
		if (StringUtils.isEmpty(entityName))
			return null;
		Class<?> _objectClass = getEntityClass(entityName);
		@SuppressWarnings("unchecked")
		List<T> list = (List<T>) dao.getDataList(_objectClass, hqlCondition);
		return list;
	}

	/**
	 * 根据主键ID值删除实体
	 * 
	 * @Title: deleteEntity
	 * @author:liushufeng
	 * @date：2015年7月12日 下午9:49:26
	 * @param entityName
	 *            实体类名
	 * @param id
	 *            主键值
	 * @return 成功返回true，失败返回false
	 */
	static boolean deleteEntity(String entityName, String id) {
		CommonDao dao = SuperSpringContext.getContext().getBean(CommonDao.class);
		if (StringUtils.isEmpty(entityName))
			return false;
		Class<?> _objectClass = getEntityClass(entityName);
		dao.delete(_objectClass, id);
		return true;
	}

	/**
	 * 删除实体
	 * 
	 * @Title: deleteEntity
	 * @author:liushufeng
	 * @date：2015年7月13日 下午3:04:28
	 * @param t
	 *            实体对象
	 * @return 成功true或者失败false
	 */
	static <T> boolean deleteEntity(T t) {
		if (t == null)
			return false;
		CommonDao dao = SuperSpringContext.getContext().getBean(CommonDao.class);
		dao.deleteEntity(t);
		return true;
	}

	/**
	 * 根据条件删除实体
	 * 
	 * @Title: deleteEntities
	 * @author:liushufeng
	 * @date：2015年7月12日 下午9:50:48
	 * @param entityName
	 *            实体类名
	 * @param hqlCondition
	 *            条件语句
	 * @return 成功返回true，失败返回false
	 */
	static boolean deleteEntities(String entityName, String hqlCondition) {
		CommonDao dao = SuperSpringContext.getContext().getBean(CommonDao.class);
		if (StringUtils.isEmpty(entityName))
			return false;
		Class<?> _objectClass = getEntityClass(entityName);
		dao.deleteEntitysByHql(_objectClass, hqlCondition);
		return true;
	}

	/**
	 * new一个实体类的实例
	 * 
	 * @Title: newEntity
	 * @author:liushufeng
	 * @date：2015年7月12日 上午1:51:04
	 * @param entityName
	 * @return
	 */
	static <T> T newEntity(String entityName) {
		T _object = null;
		Class<?> unitClass = getEntityClass(entityName);
		try {
			@SuppressWarnings("unchecked")
			T _object2 = (T) unitClass.newInstance();
			_object = _object2;
		} catch (InstantiationException e1) {
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			e1.printStackTrace();
		}
		return _object;
	}

	/**
	 * 根据实体类名获取实体的类
	 * 
	 * @Title: getEntityClass
	 * @author:liushufeng
	 * @date：2015年7月12日 上午1:50:29
	 * @param entityName
	 * @return
	 */
	public static Class<?> getEntityClass(String entityName) {
		Class<?> unitClass = null;
		String packageName = "com.supermap.realestate.registration.model.";
		try {
			unitClass = Class.forName(packageName + entityName);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return unitClass;
	}

	/**
	 * 如果不存在实体（根据主键ID值去判断），拷贝实体
	 * 
	 * @Title: copyObjectIfNotExists
	 * @author:liushufeng
	 * @date：2015年7月12日 上午2:13:23
	 * @param srcEntityName
	 *            源实体类名
	 * @param desEntityName
	 *            目标实体类名
	 * @param srcId
	 *            源实体主键ID值
	 * @return 拷贝出来的实体对象，未save
	 */
	static <T> T copyObjectIfNotExists(String srcEntityName, String desEntityName, String srcId) {
		T _desEntity = EntityTools.loadEntity(desEntityName, srcId);
		if (_desEntity == null) {
			_desEntity = EntityTools.newEntity(desEntityName);
			if (_desEntity == null)
				return null;
			Object _srcEntity = EntityTools.loadEntity(srcEntityName, srcId);
			if (_srcEntity == null)
				return null;
			ObjectHelper.copyObject(_srcEntity, _desEntity);
			return _desEntity;
		}
		return null;
	}

	/**
	 * 拷贝实体,产生新的主键ID
	 * @Title: copyObjectWithNewID
	 * @author:liushufeng
	 * @date：2016年3月2日 下午3:29:27
	 * @param srcEntityName
	 *            源实体类名
	 * @param desEntityName
	 *            目标实体类名
	 * @param srcId
	 *            源实体主键ID值
	 * @return 拷贝出来的实体对象，未save，新的主键ID值
	 */
	@SuppressWarnings("unchecked")
	static <T> T copyObjectWithNewID(String srcEntityName, String desEntityName, String srcId) {
		Object _srcEntity = EntityTools.loadEntity(srcEntityName, srcId);
		T _desEntity = EntityTools.newEntity(desEntityName);
		ObjectHelper.copyObject(_srcEntity, _desEntity);
		@SuppressWarnings("rawtypes")
		SuperModel model = (SuperModel) _desEntity;
		if (model != null) {
			model.setId(SuperHelper.GeneratePrimaryKey());
		} else {
			try {
				PropertyUtils.setProperty(_desEntity, "ID", SuperHelper.GeneratePrimaryKey());
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			}
		}
		return _desEntity;
	}

	/**
	 * 拷贝实体,不管存在不存在
	 * 
	 * @Title: copyObject
	 * @author:liushufeng
	 * @date：2015年7月12日 上午2:13:23
	 * @param srcEntityName
	 *            源实体类名
	 * @param desEntityName
	 *            目标实体类名
	 * @param srcId
	 *            源实体主键ID值
	 * @return 拷贝出来的实体对象，未save，未重新赋值主键值
	 */
	static <T> T copyObject(String srcEntityName, String desEntityName, String srcId) {
		Object _srcEntity = EntityTools.loadEntity(srcEntityName, srcId);
		if (_srcEntity == null)
			return null;
		T _desEntity = EntityTools.newEntity(desEntityName);
		ObjectHelper.copyObject(_srcEntity, _desEntity);
		return _desEntity;
	}

	/**
	 * 如果不存在实体（根据主键ID值判断），拷贝实体，多个，根据条件查询
	 * 
	 * @Title: copyObjectsIfNotExists
	 * @author:liushufeng
	 * @date：2015年7月12日 下午4:21:13
	 * @param srcEntityName
	 *            源实体类名
	 * @param desEntityName
	 *            目标实体类名
	 * @param hqlCondition
	 *            hql查询条件
	 * @return 拷贝出来的实体对象列表，未save
	 */
	static <T> List<T> copyObjectsIfNotExists(String srcEntityName, String desEntityName, String hqlCondition) {
		List<T> _desEntities = null;
		List<T> _srcEntities = EntityTools.loadEntities(srcEntityName, hqlCondition);
		if (_srcEntities != null && _srcEntities.size() > 0) {
			for (int i = 0; i < _srcEntities.size(); i++) {
				@SuppressWarnings("rawtypes")
				SuperModel _srcModel = (SuperModel) _srcEntities.get(i);
				if (_srcModel != null) {
					String _srcid = (String) _srcModel.getId();
					T _desObject = copyObjectIfNotExists(srcEntityName, desEntityName, _srcid);
					if (_desObject != null) {
						if (_desEntities == null)
							_desEntities = new ArrayList<T>();
						_desEntities.add(_desObject);
					}
				}
			}
		}
		return _desEntities;
	}

	/**
	 * 拷贝实体，多个，根据条件查询,拷贝后的实体赋新的ID主键值
	 * @Title: copyObjectsWithNewID
	 * @author:liushufeng
	 * @date：2016年3月2日 下午3:31:15
	 * @param srcEntityName
	 *            源实体类名
	 * @param desEntityName
	 *            目标实体类名
	 * @param hqlCondition
	 *            hql查询条件
	 * @return 拷贝出来的实体对象列表，未save，有新的主键ID值
	 */
	static <T> List<T> copyObjectsWithNewID(String srcEntityName, String desEntityName, String hqlCondition) {
		List<T> _desEntities = null;
		List<T> _srcEntities = EntityTools.loadEntities(srcEntityName, hqlCondition);
		if (_srcEntities != null && _srcEntities.size() > 0) {
			for (int i = 0; i < _srcEntities.size(); i++) {
				@SuppressWarnings("rawtypes")
				SuperModel _srcModel = (SuperModel) _srcEntities.get(i);
				if (_srcModel != null) {
					String _srcid = (String) _srcModel.getId();
					T _desObject = copyObjectWithNewID(srcEntityName, desEntityName, _srcid);
					if (_desObject != null) {
						if (_desEntities == null)
							_desEntities = new ArrayList<T>();
						_desEntities.add(_desObject);
					}
				}
			}
		}
		return _desEntities;
	}

	/**
	 * 根据条件拷贝多个实体，不判断是否存在，没有处理ID，需要在外面重新处理ID
	 * 
	 * @Title: copyObjects
	 * @author:liushufeng
	 * @date：2015年10月27日 上午11:33:09
	 * @param srcEntityName
	 *            源实体类名
	 * @param desEntityName
	 *            目标实体类名
	 * @param hqlCondition
	 *            hql查询条件
	 * @return 拷贝出来的实体对象列表，未save，未重新赋值主键ID值
	 */
	static <T> List<T> copyObjects(String srcEntityName, String desEntityName, String hqlCondition) {
		List<T> _desEntities = null;
		List<T> _srcEntities = EntityTools.loadEntities(srcEntityName, hqlCondition);
		if (_srcEntities != null && _srcEntities.size() > 0) {
			for (int i = 0; i < _srcEntities.size(); i++) {
				@SuppressWarnings("rawtypes")
				SuperModel _srcModel = (SuperModel) _srcEntities.get(i);
				if (_srcModel != null) {
					String _srcid = (String) _srcModel.getId();
					T _desObject = copyObject(srcEntityName, desEntityName, _srcid);
					if (_desObject != null) {
						if (_desEntities == null)
							_desEntities = new ArrayList<T>();
						_desEntities.add(_desObject);
					}
				}
			}
		}
		return _desEntities;
	}

}
