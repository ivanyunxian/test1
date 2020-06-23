/**   
 * 权利人操作工具类
 * @Title: RightsHolderTools.java 
 * @Package com.supermap.realestate.registration.tools 
 * @author liushufeng 
 * @date 2015年7月13日 下午1:56:00 
 * @version V1.0   
 */

package com.supermap.realestate.registration.tools;

import java.text.MessageFormat;
import java.util.List;

import com.supermap.realestate.registration.model.BDCS_DJDY_GZ;
import com.supermap.realestate.registration.model.BDCS_QLR_GZ;
import com.supermap.realestate.registration.model.BDCS_QLR_XZ;
import com.supermap.realestate.registration.model.interfaces.RightsHolder;
import com.supermap.realestate.registration.util.ConstValue.DJDYLY;
import com.supermap.realestate.registration.util.StringHelper;

/**
 * 权利人操作工具类
 * @ClassName: RightsHolderTools
 * @author liushufeng
 * @date 2015年7月13日 下午1:56:00
 */
public class RightsHolderTools {

	/**
	 * 权利人表前缀
	 */
	private static final String prefixRightsHolder = "BDCS_QLR";
	
	/**
	 * 权-地-证-人表前缀
	 */
	private static final String _prefixRTRightsTableName = "BDCS_QDZR";

	/**
	 * 根据主键值拷贝权利人
	 * @Title: copyRightsHolder
	 * @author:liushufeng
	 * @date：2015年7月13日 下午4:21:05
	 * @param fromLy
	 *            来源层（GZ,XZ,LS,DC）
	 * @param toLy
	 *            目标层（GZ,XZ,LS,DC）
	 * @param fromID
	 *            来源主键ID值
	 * @return 新拷贝出来的权利人
	 */
	public static RightsHolder copyRightsHolder(DJDYLY fromLy, DJDYLY toLy, String fromID) {
		if (fromLy.equals(toLy))
			return null;
		if (StringHelper.isEmpty(fromID))
			return null;
		String _srcEntityClassName = EntityTools.getEntityName(prefixRightsHolder, fromLy);
		String _desEntityClassName = EntityTools.getEntityName(prefixRightsHolder, toLy);
		RightsHolder _newHolder = EntityTools.copyObjectIfNotExists(_srcEntityClassName, _desEntityClassName, fromID);
		return _newHolder;
	}

	/**
	 * 根据权利ID拷贝权利人
	 * @Title: copyRightsHoldersByRightsID
	 * @author:liushufeng
	 * @date：2015年7月13日 下午4:27:50
	 * @param fromLy
	 *            来源层（GZ,XZ,LS,DC）
	 * @param toLy
	 *            目标层（GZ,XZ,LS,DC）
	 * @param fromQLID
	 *            权利ID值
	 * @return 拷贝出来的权利人列表
	 */
	public static List<RightsHolder> copyRightsHoldersByRightsID(DJDYLY fromLy, DJDYLY toLy, String fromQLID) {
		if (fromLy.equals(toLy))
			return null;
		if (StringHelper.isEmpty(fromQLID))
			return null;
		String _srcEntityClassName = EntityTools.getEntityName(prefixRightsHolder, fromLy);
		String _desEntityClassName = EntityTools.getEntityName(prefixRightsHolder, toLy);
		String _hqlCondition = MessageFormat.format("QLID=''{0}''", fromQLID);
		List<RightsHolder> _holders = EntityTools.copyObjectsIfNotExists(_srcEntityClassName, _desEntityClassName, _hqlCondition);
		return _holders;
	}

	/**
	 * 根据权利ID加载权利人列表
	 * @Title: loadRightsHolders
	 * @author:liushufeng
	 * @date：2015年7月13日 下午2:51:22
	 * @param ly
	 *            来源
	 * @param qlid
	 *            权利ID
	 * @return
	 */
	public static List<RightsHolder> loadRightsHolders(DJDYLY ly, String qlid) {
		if (StringHelper.isEmpty(qlid))
			return null;
		String _entityClassName = EntityTools.getEntityName(prefixRightsHolder, ly);
		if (StringHelper.isEmpty(_entityClassName))
			return null;
		String _hqlCondition = MessageFormat.format("QLID=''{0}'' ORDER BY BDCQZH,SXH", qlid);
		List<RightsHolder> _objectList = EntityTools.loadEntities(_entityClassName, _hqlCondition);
		return _objectList;
	}
	
	/**
	 * 根据登记单元id和项目编号加载权利人列表
	 * @作者 diaoliwei
	 * @创建时间 2015年7月17日下午9:46:10
	 * @param ly
	 * @param djdyid
	 * @param xmbh
	 * @return
	 */
	public static List<RightsHolder> loadRightsHolders(DJDYLY ly, String djdyid, String xmbh) {
		String _entityClassName = EntityTools.getEntityName(prefixRightsHolder, ly);
		String _srcRTRightsEntityName = EntityTools.getEntityName(_prefixRTRightsTableName, ly);
		String _hqlCondition = MessageFormat.format(" id IN( SELECT QLRID FROM " + _srcRTRightsEntityName
				+ " WHERE DJDYID=''{0}'' AND XMBH=''{1}'') ORDER BY SXH", djdyid, xmbh);
		List<RightsHolder> _rightsList = EntityTools.loadEntities(_entityClassName, _hqlCondition);
		return _rightsList;
	}

	/**
	 * 根据项目编号和证书ID加载权利人列表
	 * @Title: loadRightsHoldersByZSID 
	 * @author:liushufeng
	 * @date：2015年11月14日 下午9:02:05
	 * @param ly
	 * @param xmbh
	 * @param zsid
	 * @return
	 */
	public static List<RightsHolder> loadRightsHoldersByZSID(DJDYLY ly, String xmbh, String zsid) {
		String _entityClassName = EntityTools.getEntityName(prefixRightsHolder, ly);
		String _srcRTRightsEntityName = EntityTools.getEntityName(_prefixRTRightsTableName, ly);
		StringBuilder hqlCondition = new StringBuilder();
		hqlCondition.append(" id IN (SELECT QLRID FROM "+_srcRTRightsEntityName+" ");
		hqlCondition.append(" WHERE ZSID ='").append(zsid).append("' ");
		hqlCondition.append(" AND XMBH ='").append(xmbh).append("') ");
		hqlCondition.append(" AND XMBH ='").append(xmbh).append("' ORDER BY SXH");
		List<RightsHolder> _rightsList = EntityTools.loadEntities(_entityClassName, hqlCondition.toString());
		return _rightsList;
	}
	
	/**
	 * 根据权利人ID加载权利人
	 * @Title: loadRightsHolder
	 * @author:liushufeng
	 * @date：2015年7月13日 下午2:53:00
	 * @param ly
	 *            来源（GZ,XZ,LS,DC）
	 * @param qlrid
	 *            权利人ID
	 * @return 权利人实体对象
	 */
	public static RightsHolder loadRightsHolder(DJDYLY ly, String qlrid) {
		if (StringHelper.isEmpty(qlrid))
			return null;
		String _entityClassName = EntityTools.getEntityName(prefixRightsHolder, ly);
		if (StringHelper.isEmpty(_entityClassName))
			return null;
		RightsHolder _holder = EntityTools.loadEntity(_entityClassName, qlrid);
		return _holder;
	}

	/**
	 * 根据权利人ID删除权利人
	 * @Title: deleteRightsHolder
	 * @author:liushufeng
	 * @date：2015年7月13日 下午2:59:39
	 * @param ly
	 *            来源层（GZ,XZ,LS,DC）
	 * @param qlrid
	 *            权利人ID
	 * @return
	 */
	public static boolean deleteRightsHolder(DJDYLY ly, String qlrid) {
		if (StringHelper.isEmpty(qlrid))
			return false;
		String _entityClassName = EntityTools.getEntityName(prefixRightsHolder, ly);
		if (StringHelper.isEmpty(_entityClassName))
			return false;
		return EntityTools.deleteEntity(_entityClassName, qlrid);
	}

	/**
	 * 删除权利人实体
	 * @Title: deleteRightsHolder
	 * @author:liushufeng
	 * @date：2015年7月13日 下午3:05:21
	 * @param holder
	 *            权利人实体对象
	 * @return 成功或失败
	 */
	public static boolean deleteRightsHolder(RightsHolder holder) {
		return EntityTools.deleteEntity(holder);
	}

	/**
	 * 根据权利ID删除权利人
	 * @Title: deleteRightsHoldersByRightsID
	 * @author:liushufeng
	 * @date：2015年7月13日 下午3:08:11
	 * @param ly
	 *            来源（GZ,XZ,LS,DC）
	 * @param qlid
	 *            权利ID
	 * @return 成功或失败
	 */
	public static boolean deleteRightsHoldersByRightsID(DJDYLY ly, String qlid) {
		if (StringHelper.isEmpty(qlid))
			return false;
		String _entityClassName = EntityTools.getEntityName(prefixRightsHolder, ly);
		String hqlCondition = MessageFormat.format("QLID=''{0}''", qlid);
		return EntityTools.deleteEntities(_entityClassName, hqlCondition);
	}

	/**
	 * 根据权利ID获取合并后的权利人
	 * @Title: getUnionRightsHolder 
	 * @author:liushufeng
	 * @date：2015年8月11日 下午9:08:27
	 * @param qlid
	 * @return
	 */
	public static  RightsHolder getUnionRightsHolder(DJDYLY ly,String qlid) {
		RightsHolder holder = new BDCS_QLR_XZ();
		List<RightsHolder> holders = loadRightsHolders(ly, qlid);
		if (holders != null && holders.size() > 0) {
			for (RightsHolder _holder : holders) {
				holder.setQLRMC(StringHelper.isEmpty(holder.getQLRMC()) ? _holder.getQLRMC() : holder.getQLRMC() +","+ _holder.getQLRMC());
				holder.setDLRXM(StringHelper.isEmpty(holder.getDLRXM()) ? _holder.getDLRXM() : holder.getDLRXM() +","+ _holder.getDLRXM());
				holder.setDH(StringHelper.isEmpty(holder.getDH()) ? _holder.getDH() : holder.getDH() +","+ _holder.getDH());
				holder.setZJH(StringHelper.isEmpty(holder.getZJH()) ? _holder.getZJH() : holder.getZJH() +","+ _holder.getZJH());
			}
		}
		return holder;
	}
	
	/**
	 * new一个新的权利人的实例
	 * @Title: newRightsHolder
	 * @author:yuxuebin
	 * @date：2016年7月14日 18:33:48
	 * @param ly 单元来源
	 * @return
	 */
	public static RightsHolder newRightsHolder(DJDYLY ly) {
		RightsHolder rightsholder = null;
		String _subrightEntityName = getRightsHolderEntityName(ly);
		rightsholder = (RightsHolder) EntityTools.newEntity(_subrightEntityName);
		return rightsholder;
	}
	
	/**
	 * 获取权利人实体类名称。
	 * @Title: getRightsHolderEntityName
	 * @author:yuxuebin
	 * @date：2016年07月14日 18:33:14
	 * @param ly
	 * @return
	 */
	private static String getRightsHolderEntityName(DJDYLY ly) {
		String _entityName = "";
		if (!ly.equals(DJDYLY.DC))
			_entityName = EntityTools.getEntityName("BDCS_QLR", ly);
		else
			_entityName = "DCS_QLR_GZ";
		return _entityName;
	}
}
