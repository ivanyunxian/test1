package org.jeecg.modules.mortgagerpc.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.mortgagerpc.entity.Wfi_materclass;

import java.util.List;

/**
 * @Description: 工作流资料目录
 * @Author: jeecg-boot
 * @Date:   2019-08-13
 * @Version: V1.0
 */
public interface IWfi_materclassService extends IService<Wfi_materclass> {

    List<Wfi_materclass> getMaterlist(String prolsh);

	List<String> getIdsByRyMaterclass();
}
