package org.jeecg.modules.mortgagerpc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.common.exception.SupermapBootException;
import org.jeecg.common.system.util.HttpClientUtil;
import org.jeecg.common.system.util.StringHelper;
import org.jeecg.common.util.ConstValue;
import org.jeecg.common.util.ConstValueMrpc;
import org.jeecg.modules.mortgagerpc.entity.Bdc_zs;
import org.jeecg.modules.mapper.Bdc_zsMapper;
import org.jeecg.modules.mortgagerpc.entity.Wfi_proinst;
import org.jeecg.modules.mortgagerpc.service.IBdc_zsService;
import org.jeecg.modules.mortgagerpc.service.ISys_configService;
import org.jeecg.modules.mortgagerpc.service.IWfi_proinstService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.metadata.IPage;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description: 证书信息
 * @Author: jeecg-boot
 * @Date:   2019-08-04
 * @Version: V1.0
 */
@Service
public class Bdc_zsServiceImpl extends ServiceImpl<Bdc_zsMapper, Bdc_zs> implements IBdc_zsService {

    @Autowired
    Bdc_zsMapper zsMapper;

    @Autowired
    IWfi_proinstService wfi_proinstService;

    @Autowired
    ISys_configService sys_configService;

    @Override
    public IPage<Map> zslist(Page<Map> page, HttpServletRequest request) {
        Map<String, String> param = new HashMap<String,String>();
        param.put("prolsh", StringHelper.trim(request.getParameter("prolsh"))) ;
        return page.setRecords(zsMapper.zslist(page, param));
    }

    @Override
    public boolean pushZSToMRPC(String prolsh) {
        Wfi_proinst proinst = wfi_proinstService.getOne(new QueryWrapper<Wfi_proinst>().eq("PROLSH", prolsh));
        //登簿后才有zs数据
        if(ConstValueMrpc.SHZT.BOARD_BOOK.Value.equals(StringHelper.formatObject(proinst.getShzt()))) {
            String pushZSToMRPC = sys_configService.getConfigByKey("pushZSToMRPC");
            if(StringHelper.isEmpty(pushZSToMRPC)) {
               return false;
            }
            String s = HttpClientUtil.requestGet(pushZSToMRPC.replace("{wlsh}", prolsh).replace("{ywly}", "3"));
            if (s != null) {
                return true;
            }

        }
        return false;
    }

}
