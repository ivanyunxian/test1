package org.jeecg.modules.mortgagerpc.service;

import org.jeecg.common.api.vo.Result;

/**
 * 收费信息
 *
 * @author 李海东
 * @created by 2019/9/26 14:12
 */
public interface SfxxService {
    Result list(String ywlsh);

    Result update(String[] ids, String ywlsh);

    Result delete(String id, String ywlsh);
}
