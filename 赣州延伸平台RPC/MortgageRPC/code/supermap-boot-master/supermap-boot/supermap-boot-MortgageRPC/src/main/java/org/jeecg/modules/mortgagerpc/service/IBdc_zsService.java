package org.jeecg.modules.mortgagerpc.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.mortgagerpc.entity.Bdc_zs;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @Description: 证书信息
 * @Author: jeecg-boot
 * @Date:   2019-08-04
 * @Version: V1.0
 */
public interface IBdc_zsService extends IService<Bdc_zs> {
    IPage<Map> zslist(Page<Map> page, HttpServletRequest request);

    boolean pushZSToMRPC(String prolsh);

}
