package org.jeecg.modules.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.common.system.base.entity.SysDict;
import org.jeecg.common.system.vo.DictModel;

import java.util.List;

/**
 * <p>
 * 字典表 Mapper 接口
 * </p>
 *
 * @Author zhangweijian
 * @since 2018-12-28
 */
public interface SystemDictMapper extends BaseMapper<SysDict> {
	
	/**
	  *  重复检查SQL
	 * @return
	 */

	public List<DictModel> queryDictItemsByCode(@Param("code") String code);
	public List<DictModel> queryTableDictItemsByCode(@Param("table") String table, @Param("text") String text, @Param("code") String code);
	public List<DictModel> queryTableDictItemsByCodeAndFilter(@Param("table") String table, @Param("text") String text, @Param("code") String code, @Param("filterSql") String filterSql);


	public String queryDictTextByKey(@Param("code") String code, @Param("key") String key);

	public String queryTableDictTextByKey(@Param("table") String table, @Param("text") String text, @Param("code") String code, @Param("key") String key);


	/**
	 * 查询所有部门 作为字典信息 id -->value,departName -->text
	 * @return
	 */
	public List<DictModel> queryAllDepartBackDictModel();

	/**
	 * 查询所有用户  作为字典信息 username -->value,realname -->text
	 * @return
	 */
	public List<DictModel> queryAllUserBackDictModel();

	/**
	 * 通过关键字查询出字典表
	 * @param table
	 * @param text
	 * @param code
	 * @param keyword
	 * @return
	 */
	public List<DictModel> queryTableDictItems(@Param("table") String table, @Param("text") String text, @Param("code") String code, @Param("keyword") String keyword);


}
