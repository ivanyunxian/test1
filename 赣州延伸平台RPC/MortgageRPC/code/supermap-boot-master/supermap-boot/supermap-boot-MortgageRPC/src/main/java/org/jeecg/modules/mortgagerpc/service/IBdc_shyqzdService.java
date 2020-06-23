package org.jeecg.modules.mortgagerpc.service;

import java.util.List;

import org.jeecg.modules.mortgagerpc.entity.Bdc_shyqzd;

import com.baomidou.mybatisplus.extension.service.IService;

public interface IBdc_shyqzdService extends IService<Bdc_shyqzd>{

	List<Bdc_shyqzd> queryByEnterpriseId(String enterpriseId);

}
