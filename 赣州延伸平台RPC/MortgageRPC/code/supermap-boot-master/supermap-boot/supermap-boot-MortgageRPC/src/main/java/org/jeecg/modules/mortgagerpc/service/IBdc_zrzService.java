package org.jeecg.modules.mortgagerpc.service;

import java.util.List;

import org.jeecg.modules.mortgagerpc.entity.Bdc_shyqzd;
import org.jeecg.modules.mortgagerpc.entity.Bdc_zrz;
import org.jeecg.modules.mortgagerpc.model.BdcZrzTreeModel;

import com.baomidou.mybatisplus.extension.service.IService;

public interface IBdc_zrzService extends IService<Bdc_zrz>{

	List<BdcZrzTreeModel> queryZrzById(List<Bdc_shyqzd> zdlist);

}
