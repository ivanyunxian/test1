package org.jeecg.common.system.base.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.common.system.base.entity.SysDict;
import org.jeecg.common.system.vo.DictModel;

import java.util.List;

/**
 * <p>
 * 字典表 服务类
 * </p>
 *
 * @Author zhangweijian
 * @since 2018-12-28
 */
public interface ISystemDictService extends IService<SysDict> {

    public List<DictModel> queryDictItemsByCode(String code);

    List<DictModel> queryTableDictItemsByCode(String table, String text, String code);
    
	public List<DictModel> queryTableDictItemsByCodeAndFilter(String table, String text, String code, String filterSql);

    public String queryDictTextByKey(String code, String key);

    String queryTableDictTextByKey(String table, String text, String code, String key);

    /**
     * 根据字典类型删除关联表中其对应的数据
     *
     * @param sysDict
     * @return
     */
    boolean deleteByDictId(SysDict sysDict);


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
	 * 通过关键字查询字典表
	 * @param table
	 * @param text
	 * @param code
	 * @param keyword
	 * @return
	 */
	public List<DictModel> queryTableDictItems(String table, String text, String code, String keyword);


	public JSONObject transForJson(JSONObject jsonObject);


}
