package org.jeecg.common.system.base.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.constant.CacheConstant;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.system.base.entity.SysDict;
import org.jeecg.common.system.base.service.ISystemDictService;
import org.jeecg.common.system.util.StringHelper;
import org.jeecg.common.system.vo.DictModel;
import org.jeecg.modules.mapper.SystemDictMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 字典表 服务实现类
 * </p>
 *
 * @Author zhangweijian
 * @since 2018-12-28
 */
@Service
@Slf4j
public class SystemDictServiceImpl extends ServiceImpl<SystemDictMapper, SysDict> implements ISystemDictService {

    @Autowired
    private SystemDictMapper sysDictMapper;

	/**
	 * 通过查询指定code 获取字典
	 * @param code
	 * @return
	 */
	@Override
	@Cacheable(value = CacheConstant.DICT_CACHE,key = "#code")
	public List<DictModel> queryDictItemsByCode(String code) {
		log.info("无缓存dictCache的时候调用这里！");
		return sysDictMapper.queryDictItemsByCode(code);
	}

	/**
	 * 通过查询指定code 获取字典值text
	 * @param code
	 * @param key
	 * @return
	 */

	@Override
	@Cacheable(value = CacheConstant.DICT_CACHE)
	public String queryDictTextByKey(String code, String key) {
		log.info("无缓存dictText的时候调用这里！");
		return sysDictMapper.queryDictTextByKey(code, key);
	}

	/**
	 * 通过查询指定table的 text code 获取字典
	 * dictTableCache采用redis缓存有效期10分钟
	 * @param table
	 * @param text
	 * @param code
	 * @return
	 */
	@Override
	//@Cacheable(value = "dictTableCache")
	public List<DictModel> queryTableDictItemsByCode(String table, String text, String code) {
		log.info("无缓存dictTableList的时候调用这里！");
		return sysDictMapper.queryTableDictItemsByCode(table,text,code);
	}

	@Override
	public List<DictModel> queryTableDictItemsByCodeAndFilter(String table, String text, String code, String filterSql) {
		log.info("无缓存dictTableList的时候调用这里！");
		return sysDictMapper.queryTableDictItemsByCodeAndFilter(table,text,code,filterSql);
	}
	
	/**
	 * 通过查询指定table的 text code 获取字典值text
	 * dictTableCache采用redis缓存有效期10分钟
	 * @param table
	 * @param text
	 * @param code
	 * @param key
	 * @return
	 */
	@Override
	@Cacheable(value = "dictTableCache")
	public String queryTableDictTextByKey(String table,String text,String code, String key) {
		log.info("无缓存dictTable的时候调用这里！");
		return sysDictMapper.queryTableDictTextByKey(table,text,code,key);
	}

    /**
     * 根据字典类型id删除关联表中其对应的数据
     */
    @Override
    public boolean deleteByDictId(SysDict sysDict) {
        sysDict.setDelFlag(CommonConstant.DEL_FLAG_1);
        return  this.updateById(sysDict);
    }

	@Override
	public List<DictModel> queryAllDepartBackDictModel() {
		return baseMapper.queryAllDepartBackDictModel();
	}

	@Override
	public List<DictModel> queryAllUserBackDictModel() {
		return baseMapper.queryAllUserBackDictModel();
	}
	
	@Override
	public List<DictModel> queryTableDictItems(String table, String text, String code, String keyword) {
		return baseMapper.queryTableDictItems(table, text, code, "%"+keyword+"%");
	}

	@Override
	public JSONObject transForJson(JSONObject jsonObject) {
		JSONObject dictjson = new JSONObject();
		//递归将jsonobject里的数据全部翻译出来
		Set<String> keyset = jsonObject.keySet();
		Iterator<String> iterator = keyset.iterator();
		while (iterator.hasNext()){
			String key = iterator.next();
			Object obj = jsonObject.get(key);
			if(obj instanceof JSONObject) {
				JSONObject jsonobj = (JSONObject) obj;
				transForJson(jsonobj);

			} else if(obj instanceof JSONArray) {
				JSONArray array = (JSONArray) obj;
				for(int i=0;i<array.size();i++) {
					JSONObject jsonobj = array.getJSONObject(i);
					transForJson(jsonobj);
				}
			} else if(obj instanceof String) {
				String value = (String) obj;
				String dictvalue = queryDictTextByKey(key.toUpperCase() ,value);
				if(!StringHelper.isEmpty(dictvalue)) {
					dictjson.put(key+CommonConstant.DICT_TEXT_SUFFIX , dictvalue );
				}
			}
		}
		jsonObject.putAll(dictjson);

		return jsonObject;
	}

}
