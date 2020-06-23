/**   
 * 权利操作类
 * @Title: RightsLoader.java 
 * @Package com.supermap.realestate.registration.util 
 * @author liushufeng 
 * @date 2015年7月12日 下午5:14:44 
 * @version V1.0   
 */

package com.supermap.realestate.registration.tools;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.supermap.realestate.registration.model.interfaces.Rights;
import com.supermap.realestate.registration.model.interfaces.RightsHolder;
import com.supermap.realestate.registration.model.interfaces.RightsRelation;
import com.supermap.realestate.registration.model.interfaces.SubRights;
import com.supermap.realestate.registration.util.ConstValue.DJDYLY;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.wisdombusiness.core.SuperHelper;
import com.supermap.wisdombusiness.core.SuperModel;
import com.supermap.wisdombusiness.core.SuperSpringContext;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;

/**
 * 权利操作类
 * @ClassName: RightsLoader
 * @author liushufeng
 * @date 2015年7月12日 下午5:14:44
 */
public class RightsTools {

	/**
	 * 权利表前缀
	 */
	static final String _prefixRightsTableName = "BDCS_QL";

	/**
	 * 
	 * 附属权利表前缀
	 */
	static final String _prefixSubRightsTableName = "BDCS_FSQL";

	/**
	 * 
	 * 权利人表前缀
	 */
	static final String _prefixRightsHolderTableName = "BDCS_QLR";

	/**
	 * 
	 * 权-地-证-人表前缀
	 */
	static final String _prefixRTRightsTableName = "BDCS_QDZR";

	/**
	 * 
	 * 证书表前缀
	 */
	static final String _prefixCertificateTableName = "BDCS_ZS";

	/**
	 * 拷贝权利，同时拷贝权利人，附属权利，权利人，权地证人关系,证书
	 * @Title: copyRights
	 * @author:liushufeng
	 * @date：2015年7月12日 下午5:16:54
	 * @param fromLy
	 *            来源层（GZ,XZ,LS,DC）
	 * @param toLy
	 *            目标层（GZ,XZ,LS,DC）
	 * @param qlid
	 *            来源权利ID
	 * @return 新拷贝出来的权利信息
	 */
	public static Rights copyRightsAll(DJDYLY fromLy, DJDYLY toLy, String qlid) {
		if (fromLy.equals(toLy))
			return null;
		String _srcRightsEntityName = EntityTools.getEntityName(_prefixRightsTableName, fromLy);
		Rights _srcRights = (Rights) EntityTools.loadEntity(_srcRightsEntityName, qlid);
		if (_srcRights == null)
			return null;
		Rights _desRights = null;
		_desRights = copyRightsAll(fromLy, toLy, _srcRights);
		return _desRights;
	}

	/**
	 * 根据登记单元ID拷贝权利，同时拷贝权利人，附属权利，权利人，权地证人关系,证书
	 * @Title: copyRightsAllByDJDYID
	 * @author:liushufeng
	 * @date：2015年7月19日 上午3:16:12
	 * @param fromLy
	 * @param toLY
	 * @param djdyid
	 * @return
	 */
	public static Rights copyRightsAllByDJDYID(DJDYLY fromLy, DJDYLY toLy, String xmbh, String djdyid) {
		Rights _rights = null;
		String _hql = MessageFormat.format("XMBH=''{0}'' AND DJDYID=''{1}''", xmbh, djdyid);
		// sunhb-2015-09-02 修改添加实体名称带后缀
		String _srcRightsEntityName = EntityTools.getEntityName(_prefixRightsTableName, fromLy);
		List<Rights> _listRights = EntityTools.loadEntities(_srcRightsEntityName, _hql);
		if (_listRights != null && _listRights.size() > 0) {
			_rights = copyRightsAll(fromLy, toLy, _listRights.get(0));
		}
		return _rights;
	}

	/**
	 * 拷贝权利，同时拷贝权利人，附属权利，权利人，权地证人关系，证书
	 * @Title: copyRightsAll
	 * @author:liushufeng
	 * @date：2015年7月12日 下午6:18:59
	 * @param fromLy
	 *            来源层（GZ,XZ,LS,DC）
	 * @param toLy
	 *            目标层（GZ,XZ,LS,DC）
	 * @param srcRights
	 *            源权利对象
	 * @return 新拷贝出来的权利信息
	 */
	public static Rights copyRightsAll(DJDYLY fromLy, DJDYLY toLy, Rights srcRights) {

		if (fromLy.equals(toLy))
			return null;

		if (srcRights == null)
			return null;

		CommonDao dao = SuperSpringContext.getContext().getBean(CommonDao.class);
		Rights _desRights = null;

		String _srcRightsEntityName = EntityTools.getEntityName(_prefixRightsTableName, fromLy);
		String _desRightsEntityName = EntityTools.getEntityName(_prefixRightsTableName, toLy);

		String qlid = srcRights.getId();
		Rights _srcRights = srcRights;
		String xmbh = _srcRights.getXMBH();

		// 现拷贝权利
		_desRights = (Rights) EntityTools.copyObjectIfNotExists(_srcRightsEntityName, _desRightsEntityName, qlid);
		if (_desRights == null)
			return null;

		// 再拷贝附属权利
		String _srcSubRightsEntityName = EntityTools.getEntityName(_prefixSubRightsTableName, fromLy);
		String _desSubRightsEntityName = EntityTools.getEntityName(_prefixSubRightsTableName, toLy);
		SubRights _desSubRights = (SubRights) EntityTools.copyObjectIfNotExists(_srcSubRightsEntityName, _desSubRightsEntityName, _srcRights.getFSQLID());
		if (_desSubRights != null) {
			dao.save(_desSubRights);
		}

		// 再拷贝权利人
		String _srcRightsHolderEntityName = EntityTools.getEntityName(_prefixRightsHolderTableName, fromLy);
		String _desRightsHolderEntityName = EntityTools.getEntityName(_prefixRightsHolderTableName, toLy);
		String _holderHqlCondition = MessageFormat.format(" QLID=''{0}''", qlid);
		List<Object> _desRightsHolders = EntityTools.copyObjectsIfNotExists(_srcRightsHolderEntityName, _desRightsHolderEntityName, _holderHqlCondition);
		if (_desRightsHolders != null && _desRightsHolders.size() > 0) {
			for (Object _object : _desRightsHolders) {
				dao.save(_object);
			}
		}

		// 再拷贝权地证人关系表
		String _srcRTRightsEntityName = EntityTools.getEntityName(_prefixRTRightsTableName, fromLy);
		String _desRTRightsEntityName = EntityTools.getEntityName(_prefixRTRightsTableName, toLy);
		String _rtHqlCOndition = MessageFormat.format("QLID=''{0}'' AND XMBH=''{1}''", qlid, xmbh);
		List<Object> _desRTs = EntityTools.copyObjectsIfNotExists(_srcRTRightsEntityName, _desRTRightsEntityName, _rtHqlCOndition);
		if (_desRTs != null && _desRTs.size() > 0) {
			for (Object _object : _desRTs) {
				dao.save(_object);
			}
		}

		// 再拷贝证书表
		String _srcCertificateEntityName = EntityTools.getEntityName(_prefixCertificateTableName, fromLy);
		String _desCertificateEntityName = EntityTools.getEntityName(_prefixCertificateTableName, toLy);
		String _certHqlCOndition = MessageFormat.format(" id in (select ZSID FROM {0} WHERE QLID=''{1}'' AND XMBH=''{2}'') AND XMBH=''{2}''", _srcRTRightsEntityName, qlid, xmbh);
		List<Object> _desCerts = EntityTools.copyObjectsIfNotExists(_srcCertificateEntityName, _desCertificateEntityName, _certHqlCOndition);
		if (_desCerts != null && _desCerts.size() > 0) {
			for (Object _object : _desCerts) {
				dao.save(_object);
			}
		}
		return _desRights;
	}

	/**
	 * 拷贝权利，同时拷贝权利人，附属权利，权利人，权地证人关系，证书,所有的ID主键值都用新的
	 * @Title: copyRightsAllBy
	 * @author:liushufeng
	 * @date：2016年3月2日 下午3:08:52
	 * @param fromLy
	 * @param toLy
	 * @param srcRights
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Rights copyRightsAllWithNewID(DJDYLY fromLy, DJDYLY toLy, Rights srcRights) {

		if (fromLy.equals(toLy))
			return null;

		if (srcRights == null)
			return null;

		CommonDao dao = SuperSpringContext.getContext().getBean(CommonDao.class);
		Rights _desRights = null;

		String _srcRightsEntityName = EntityTools.getEntityName(_prefixRightsTableName, fromLy);
		String _desRightsEntityName = EntityTools.getEntityName(_prefixRightsTableName, toLy);

		String qlid = srcRights.getId();
		Rights _srcRights = srcRights;

		// 权利
		_desRights = EntityTools.copyObjectWithNewID(_srcRightsEntityName, _desRightsEntityName, _srcRights.getId());

		// 附属权利
		String _srcSubRightsEntityName = EntityTools.getEntityName(_prefixSubRightsTableName, fromLy);
		String _desSubRightsEntityName = EntityTools.getEntityName(_prefixSubRightsTableName, toLy);
		SubRights _desSubRights = (SubRights) EntityTools.copyObject(_srcSubRightsEntityName, _desSubRightsEntityName, _srcRights.getFSQLID());
		if (_desSubRights != null) {
			// 给附属权利里的权利ID重新赋值
			_desSubRights.setQLID(_desRights.getId());
			_desSubRights.setId((String) SuperHelper.GeneratePrimaryKey());
			_desRights.setFSQLID(_desSubRights.getId());
			dao.save(_desSubRights);
		}

		// 拷贝权利人
		Map<String, String> _holderIdMap = new HashMap<String, String>();// 权利人前后ID映射关系
		String _srcRightsHolderEntityName = EntityTools.getEntityName(_prefixRightsHolderTableName, fromLy);
		String _desRightsHolderEntityName = EntityTools.getEntityName(_prefixRightsHolderTableName, toLy);
		String _holderHqlCondition = MessageFormat.format(" QLID=''{0}''", qlid);
		List<Object> _desRightsHolders = EntityTools.copyObjects(_srcRightsHolderEntityName, _desRightsHolderEntityName, _holderHqlCondition);
		if (_desRightsHolders != null && _desRightsHolders.size() > 0) {
			for (Object _object : _desRightsHolders) {
				RightsHolder holder = (RightsHolder) _object;
				if (holder != null) {
					// 重新赋值ID和权利ID
					holder.setQLID(_desRights.getId());
					String _newHolerId = (String) SuperHelper.GeneratePrimaryKey();
					_holderIdMap.put(holder.getId(), _newHolerId);
					holder.setId(_newHolerId);
					dao.save(holder);
				}
			}
		}

		// 再拷贝证书表
		Map<String, String> _certIdMap = new HashMap<String, String>();// 证书前后ID映射关系
		String _srcRTRightsEntityName = EntityTools.getEntityName(_prefixRTRightsTableName, fromLy);
		String _desRTRightsEntityName = EntityTools.getEntityName(_prefixRTRightsTableName, toLy);
		String _srcCertificateEntityName = EntityTools.getEntityName(_prefixCertificateTableName, fromLy);
		String _desCertificateEntityName = EntityTools.getEntityName(_prefixCertificateTableName, toLy);
		String _certHqlCOndition = MessageFormat.format(" id in (select ZSID FROM {0} WHERE QLID=''{1}'')", _srcRTRightsEntityName, qlid);
		List<Object> _desCerts = EntityTools.copyObjects(_srcCertificateEntityName, _desCertificateEntityName, _certHqlCOndition);
		if (_desCerts != null && _desCerts.size() > 0) {
			for (Object _object : _desCerts) {
				@SuppressWarnings("rawtypes")
				SuperModel _model = (SuperModel) _object;
				if (_model != null) {
					String _newCertID = (String) SuperHelper.GeneratePrimaryKey();
					_certIdMap.put(_model.getId().toString(), _newCertID);
					_model.setId(_newCertID);
					dao.save(_model);
				}
			}
		}

		// 再拷贝权地证人关系表

		String _rtHqlCOndition = MessageFormat.format("QLID=''{0}'' ", qlid);
		List<Object> _desRTs = EntityTools.copyObjects(_srcRTRightsEntityName, _desRTRightsEntityName, _rtHqlCOndition);
		if (_desRTs != null && _desRTs.size() > 0) {
			for (Object _object : _desRTs) {
				RightsRelation relation = (RightsRelation) _object;
				if (relation != null) {
					relation.setId((String) SuperHelper.GeneratePrimaryKey());
					// QLID,FSQLID,QLRID,ZSID
					relation.setQLID(_desRights.getId());
					relation.setFSQLID(_desSubRights.getId());
					relation.setQLRID(_holderIdMap.get(relation.getQLRID()));
					relation.setZSID(_certIdMap.get(relation.getZSID()));
					dao.save(relation);
				}
			}
		}

		// 证书
		return _desRights;
	}

	/**
	 * 拷贝权利，只拷贝权利和附属权利
	 * @Title: copyRightsOnly
	 * @author:liushufeng
	 * @date：2015年7月12日 下午6:26:39
	 * @param fromLy
	 *            来源层（GZ,XZ,LS,DC）
	 * @param toLy
	 *            目标层（GZ,XZ,LS,DC）
	 * @param qlid
	 *            源权利ID
	 * @return 新拷贝出来的权利信息
	 */
	public static Rights copyRightsOnly(DJDYLY fromLy, DJDYLY toLy, String qlid) {
		if (fromLy.equals(toLy))
			return null;
		String _srcRightsEntityName = EntityTools.getEntityName(_prefixRightsTableName, fromLy);
		Rights _srcRights = (Rights) EntityTools.loadEntity(_srcRightsEntityName, qlid);
		if (_srcRights == null)
			return null;
		Rights _desRights = null;
		_desRights = copyRightsOnly(fromLy, toLy, _srcRights);
		return _desRights;
	}

	/**
	 * 拷贝权利，只拷贝权利和附属权利
	 * @Title: copyRightsOnly
	 * @author:liushufeng
	 * @date：2015年7月12日 下午6:22:38
	 * @param fromLy
	 *            来源层（GZ,XZ,LS,DC）
	 * @param toLy
	 *            目标层（GZ,XZ,LS,DC）
	 * @param srcRights
	 *            源权利对象
	 * @return 新拷贝出来的权利信息
	 */
	public static Rights copyRightsOnly(DJDYLY fromLy, DJDYLY toLy, Rights srcRights) {

		if (fromLy.equals(toLy))
			return null;

		if (srcRights == null)
			return null;

		CommonDao dao = SuperSpringContext.getContext().getBean(CommonDao.class);
		Rights _desRights = null;

		String _srcRightsEntityName = EntityTools.getEntityName(_prefixRightsTableName, fromLy);
		String _desRightsEntityName = EntityTools.getEntityName(_prefixRightsTableName, toLy);

		String qlid = srcRights.getId();
		Rights _srcRights = srcRights;

		// 现拷贝权利
		_desRights = (Rights) EntityTools.copyObjectIfNotExists(_srcRightsEntityName, _desRightsEntityName, qlid);
		if (_desRights == null)
			return null;

		// 再拷贝附属权利
		String _srcSubRightsEntityName = EntityTools.getEntityName(_prefixSubRightsTableName, fromLy);
		String _desSubRightsEntityName = EntityTools.getEntityName(_prefixSubRightsTableName, toLy);
		SubRights _desSubRights = (SubRights) EntityTools.copyObjectIfNotExists(_srcSubRightsEntityName, _desSubRightsEntityName, _srcRights.getFSQLID());
		if (_desSubRights != null) {
			dao.save(_desSubRights);
		}
		return _desRights;
	}

	/**
	 * 根据权利ID加载权利
	 * @Title: loadRights
	 * @author:liushufeng
	 * @date：2015年7月12日 下午9:14:50
	 * @param ly
	 *            来源（GZ,XZ,LS,DC）
	 * @param qlid
	 *            权利ID
	 * @return 权利信息
	 */
	public static Rights loadRights(DJDYLY ly, String qlid) {
		String _srcRightsEntityName = EntityTools.getEntityName(_prefixRightsTableName, ly);
		Rights _srcRights = (Rights) EntityTools.loadEntity(_srcRightsEntityName, qlid);
		return _srcRights;
	}

	/**
	 * 根据权利ID加载附属权利
	 * @Title: loadSubRights
	 * @author:liushufeng
	 * @date：2015年7月12日 下午9:19:36
	 * @param ly
	 *            来源
	 * @param qlid
	 *            权利ID
	 * @return
	 */
	public static SubRights loadSubRightsByRightsID(DJDYLY ly, String qlid) {
		SubRights _subRights = null;
		String _srcSubRightsEntityName = EntityTools.getEntityName(_prefixSubRightsTableName, ly);
		String _hqlCondition = MessageFormat.format("QLID=''{0}''", qlid);
		List<Object> _srcSubRights = EntityTools.loadEntities(_srcSubRightsEntityName, _hqlCondition);
		if (_srcSubRights != null && _srcSubRights.size() > 0) {
			_subRights = (SubRights) _srcSubRights.get(0);
		}
		return _subRights;
	}

	/**
	 * 根据主键ID值加载附属权利
	 * @Title: loadSubRights
	 * @author:liushufeng
	 * @date：2015年7月12日 下午9:23:12
	 * @param ly
	 *            来源
	 * @param fsqlid
	 *            主键ID值
	 * @return 附属权利
	 */
	public static SubRights loadSubRights(DJDYLY ly, String fsqlid) {
		SubRights _subRights = null;
		String _srcSubRightsEntityName = EntityTools.getEntityName(_prefixSubRightsTableName, ly);
		_subRights = (SubRights) EntityTools.loadEntity(_srcSubRightsEntityName, fsqlid);
		return _subRights;
	}
	/**
	 * 根据主键ID值加载附属权利
	 * @Title: loadSubRights
	 * @author:likun
	 * @date：2015年7月12日 下午9:23:12
	 * @param ly
	 *            来源
	 * @param fsqlid
	 *            主键ID值
	 * @return 附属权利
	 */
	public static List<SubRights> loadSubRightsByCondition(DJDYLY ly, String condition) {
		List<SubRights> _subRights = null;
		String _srcSubRightsEntityName = EntityTools.getEntityName(_prefixSubRightsTableName, ly);
		_subRights =  EntityTools.loadEntities(_srcSubRightsEntityName, condition);
		return _subRights;
	}

	/**
	 * 根据登记单元ID和项目编号加载权利
	 * @Title: loadRightsByDJDYID
	 * @author:liushufeng
	 * @date：2015年7月12日 下午9:29:28
	 * @param ly
	 *            来源
	 * @param djdyid
	 *            登记单元ID
	 * @param xmbh
	 *            项目编号
	 * @return 权利
	 */
	public static Rights loadRightsByDJDYID(DJDYLY ly, String xmbh, String djdyid) {
		Rights _rights = null;
		String _srcRightsEntityName = EntityTools.getEntityName(_prefixRightsTableName, ly);
		String _hqlCondition = MessageFormat.format("DJDYID=''{0}'' AND XMBH=''{1}''", djdyid, xmbh);
		List<Object> _rightsList = EntityTools.loadEntities(_srcRightsEntityName, _hqlCondition);
		if (_rightsList != null && _rightsList.size() > 0) {
			_rights = (Rights) _rightsList.get(0);
		}
		return _rights;
	}

	/**
	 * 根据项目编号和证书id获取权利
	 * @作者 diaoliwei
	 * @创建时间 2015年7月16日下午8:50:11
	 * @param ly
	 *            来源
	 * @param zsid
	 *            证书id
	 * @param xmbh
	 *            项目编号
	 * @return
	 */
	public static Rights loadRightsByZSID(DJDYLY ly, String zsid, String xmbh) {
		Rights _rights = null;
		String _srcRightsEntityName = EntityTools.getEntityName(_prefixRightsTableName, ly);
		String _srcRTRightsEntityName = EntityTools.getEntityName(_prefixRTRightsTableName, ly);
		String _hqlCondition = MessageFormat.format(" id IN (SELECT QLID FROM " + _srcRTRightsEntityName + " WHERE ZSID=''{0}'' AND XMBH=''{1}'' )", zsid, xmbh);
		List<Object> _rightsList = EntityTools.loadEntities(_srcRightsEntityName, _hqlCondition);
		if (_rightsList != null && _rightsList.size() > 0) {
			_rights = (Rights) _rightsList.get(0);
		}
		return _rights;
	}

	/**
	 * 根据条件获取权利列表
	 * @Title: loadRightsByCondition
	 * @author:liushufeng
	 * @date：2015年7月25日 下午3:34:43
	 * @param ly
	 *            来源
	 * @param condition
	 *            条件
	 * @return
	 */
	public static List<Rights> loadRightsByCondition(DJDYLY ly, String condition) {
		String rightsEntityName = EntityTools.getEntityName(_prefixRightsTableName, ly);
		List<Rights> rightss = EntityTools.loadEntities(rightsEntityName, condition);
		return rightss;
	}

	/**
	 * 根据权利ID加载权地证人记录列表
	 * @Title: loadRightsRelationsByQLID
	 * @author:liushufeng
	 * @date：2016年3月2日 下午8:42:31
	 * @param ly
	 *            来源
	 * @param qlid
	 *            权利ID
	 * @return
	 */
	public static List<RightsRelation> loadRightsRelationsByQLID(DJDYLY ly, String qlid) {
		String _condition = "QLID='" + qlid + "'";
		String _srcRTRightsEntityName = EntityTools.getEntityName(_prefixRTRightsTableName, ly);
		List<RightsRelation> _rightsRelations = EntityTools.loadEntities(_srcRTRightsEntityName, _condition);
		return _rightsRelations;
	}

	/**
	 * 根据权利ID删除权利，不删除附属权利
	 * @Title: deleteRights
	 * @author:liushufeng
	 * @date：2015年7月12日 下午9:32:29
	 * @param ly
	 *            来源
	 * @param qlid
	 *            权利ID
	 * @return
	 */
	public static Rights deleteRights(DJDYLY ly, String qlid) {
		Rights _rights = loadRights(ly, qlid);
		if (_rights != null) {
			CommonDao dao = SuperSpringContext.getContext().getBean(CommonDao.class);
			dao.deleteEntity(_rights);
		}

		return _rights;
	}

	/**
	 * 根据主键ID值删除附属权利
	 * @Title: deleteSubRights
	 * @author:liushufeng
	 * @date：2015年7月12日 下午9:36:48
	 * @param ly
	 *            来源
	 * @param fsqlid
	 *            附属权利ID
	 * @return 被删除的附属权利
	 */
	public static SubRights deleteSubRights(DJDYLY ly, String fsqlid) {
		SubRights _subRights = loadSubRights(ly, fsqlid);
		if (_subRights != null) {
			CommonDao dao = SuperSpringContext.getContext().getBean(CommonDao.class);
			dao.deleteEntity(_subRights);
		}
		return _subRights;
	}

	/**
	 * 根据权利ID同时删除权利和附属权利
	 * @Title: deleteRightsAndSubRights
	 * @author:liushufeng
	 * @date：2015年7月12日 下午9:38:10
	 * @param ly
	 * @param qlid
	 * @return
	 */
	public static Rights deleteRightsAndSubRights(DJDYLY ly, String qlid) {
		Rights _rights = deleteRights(ly, qlid);
		if (_rights != null) {
			deleteSubRights(ly, _rights.getFSQLID());
		}
		return _rights;
	}

	/**
	 * 根据权利ID删除权利，附属权利，证书，权地证人，权利人信息
	 * @Title: deleteRightsAll
	 * @author:liushufeng
	 * @date：2015年7月12日 下午9:40:12
	 * @param ly
	 *            来源
	 * @param qlid
	 *            权利ID
	 * @return 返回权利信息
	 */
	public static Rights deleteRightsAll(DJDYLY ly, String qlid) {
		Rights _rights = deleteRightsAndSubRights(ly, qlid);
		if (_rights != null) {
			// 再删除权利人
			String _rightsHolderEntityName = EntityTools.getEntityName(_prefixRightsHolderTableName, ly);
			String _hqlCondition1 = MessageFormat.format("QLID=''{0}''", qlid);
			EntityTools.deleteEntities(_rightsHolderEntityName, _hqlCondition1);

			// 再删除证书表
			String _RTRightsEntityName = EntityTools.getEntityName(_prefixRTRightsTableName, ly);
			String _certificateEntityName = EntityTools.getEntityName(_prefixCertificateTableName, ly);
			String _certHqlCOndition = "id in(select ZSID FROM " + _RTRightsEntityName + " WHERE QLID= '" + qlid + "')";
			EntityTools.deleteEntities(_certificateEntityName, _certHqlCOndition);

			// 再删除权地证人关系表

			String _rtHqlCOndition = MessageFormat.format("QLID=''{0}''", qlid);
			EntityTools.deleteEntities(_RTRightsEntityName, _rtHqlCOndition);

		}
		return _rights;
	}

	/**
	 * 根据条件(权利表的条件)删除权利，附属权利，权利人，证书，权地证人
	 * @Title: deleteRightsAllByCondition
	 * @author:liushufeng
	 * @date：2015年10月28日 下午6:42:01
	 * @param ly
	 *            来源
	 * @param hqlCondition
	 *            条件
	 * @return 返回被删除的权利列表list
	 */
	public static List<Rights> deleteRightsAllByCondition(DJDYLY ly, String hqlCondition) {
		List<Rights> deletedRights = new ArrayList<Rights>();
		List<Rights> _rights = loadRightsByCondition(ly, hqlCondition);
		if (_rights != null && _rights.size() > 0) {
			for (Rights rights : _rights) {
				deletedRights.add(rights);
				deleteRightsAll(ly, rights.getId());
			}
		}
		return deletedRights;
	}

	/**
	 * new一个新的权利的实例
	 * @Title: newRights 
	 * @author:liushufeng
	 * @date：2016年8月25日 下午5:15:21
	 * @param ly
	 * @return
	 */
	public static Rights newRights(DJDYLY ly) {
		Rights rights = null;
		String _rightEntityName = getRightsEntityName(ly);
		rights = (Rights) EntityTools.newEntity(_rightEntityName);
		return rights;
	}
	
	/**
	 * 获取权利实体类名称。
	 * @Title: getRightsEntityName 
	 * @author:liushufeng
	 * @date：2016年8月25日 下午5:15:41
	 * @param ly
	 * @return
	 */
	private static String getRightsEntityName(DJDYLY ly) {
		String _entityName = "";
		if (!ly.equals(DJDYLY.DC))
			_entityName = EntityTools.getEntityName("BDCS_QL", ly);
		else
			_entityName = "DCS_QL_GZ";
		return _entityName;
	}
	
	/**
	 * new一个新的权利的实例
	 * @Title: newSubRights 
	 * @author:liushufeng
	 * @date：2016年8月25日 下午5:12:27
	 * @param ly
	 * @return
	 */
	public static SubRights newSubRights(DJDYLY ly) {
		SubRights subrights = null;
		String _subrightEntityName = getSubRightsEntityName(ly);
		subrights = (SubRights) EntityTools.newEntity(_subrightEntityName);
		return subrights;
	}
	
	/**
	 * 获取附属权利实体类名称。
	 * @Title: getSubRightsEntityName 
	 * @author:liushufeng
	 * @date：2016年8月25日 下午5:12:51
	 * @param ly
	 * @return
	 */
	private static String getSubRightsEntityName(DJDYLY ly) {
		String _entityName = "";
		if (!ly.equals(DJDYLY.DC))
			_entityName = EntityTools.getEntityName("BDCS_FSQL", ly);
		else
			_entityName = "DCS_FSQL_GZ";
		return _entityName;
	}
	
	/**
	 * 获取最大抵押顺位数
	 * @param djdyid-登记单元id
	 * @return
	 */
    public static int getMaxMortgageSWS(String djdyid) {
        //1、获取现状层最大数 Max和现状层记录数 Count
        //2、获取工作层未登簿的最大值 Max和工作层记录数 Count
        //3、获取权籍补录未入库的最大数 Max和权籍补录未入库记录数 Count
        int dysw = 0;
        try {
        	CommonDao dao = SuperSpringContext.getContext().getBean(CommonDao.class);
        	//获取现状层最大数 Max和现状层记录数 Count
            String xzcountsql = "SELECT MAX(DYSW) MDYSW,COUNT(1) CDYSW  FROM BDCK.BDCS_FSQL_XZ FSQL " +
                             " LEFT JOIN BDCK.BDCS_QL_XZ QL ON QL.QLID=FSQL.QLID " +
                             " WHERE   QL.QLLX='23' AND QL.DJDYID='" + djdyid + "'";
            List<Map> lstxzcount= dao.getDataListByFullSql(xzcountsql);
            if (lstxzcount != null && lstxzcount.size() > 0) {
            	Map m=lstxzcount.get(0);       	
                int xzmax = StringHelper.getInt(m.get("MDYSW"));
                dysw = Math.max(dysw, xzmax);
                int xzcount = StringHelper.getInt(m.get("CDYSW"));
                dysw =  Math.max(dysw, xzcount);
            }
            //获取工作层未登簿的最大值 Max和工作层记录数 Count
            String gzmaxsql = "SELECT MAX(DYSW) MDYSW,COUNT(1) CDYSW  FROM BDCK.BDCS_FSQL_GZ FSQL " +
                             " LEFT JOIN BDCK.BDCS_QL_GZ QL ON QL.QLID=FSQL.QLID " +
                             " LEFT JOIN BDCK.BDCS_DJDY_GZ DJDY ON DJDY.DJDYID=QL.DJDYID " +
                             " LEFT JOIN BDCK.BDCS_XMXX XMXX ON XMXX.XMBH=DJDY.XMBH " +
                             " WHERE XMXX.SFDB ='0' AND QL.QLLX='23' AND XMXX.DJLX <>'400' AND DJDY.DJDYID='" + djdyid + "'";
            List<Map> lstgzmax = dao.getDataListByFullSql(gzmaxsql);
            if (lstgzmax != null && lstgzmax.size() > 0)
            {
            	Map m=lstgzmax.get(0);       	
                int gzmax = StringHelper.getInt(m.get("MDYSW"));
                dysw = Math.max(dysw, gzmax);
                int gzcount = StringHelper.getInt(m.get("CDYSW"));
                dysw = Math.max(dysw, gzcount);
            }
            //获取权籍补录未入库的最大数 Max和权籍补录未入库记录数 Count
            String dc_xzmaxsql = " select MAX(DYSW) MDYSW,COUNT(1) CDYSW from BDCDCK.bdcs_fsql_xz fsql " +
                                 " left join BDCDCK.bdcs_ql_xz ql on ql.qlid=fsql.qlid "+
                                 " left join BDCDCK.bdcs_djdy_xz  djdy on djdy.djdyid=ql.djdyid "+
                                 " left join BDCDCK.bdcs_blxm blxm on blxm.blxmid=djdy.dcxmid "+
                                 " where ql.qllx='23' and blxm.xmzt='0' and djdy.djdyid='" + djdyid + "'";
            List<Map> lstdc_xzmax = dao.getDataListByFullSql(dc_xzmaxsql);
            if (lstdc_xzmax != null && lstdc_xzmax.size() > 0) {
            	Map m=lstdc_xzmax.get(0);      
                int dc_xzmax = StringHelper.getInt(m.get("MDYSW"));
                dysw =  Math.max(dysw, dc_xzmax);
                int dc_xzcount = StringHelper.getInt(m.get("CDYSW"));
                dysw =  Math.max(dysw, dc_xzcount);
            }
		} catch (Exception e) {
		
		}       
        return dysw;
    }

}
