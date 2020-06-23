package org.jeecg.modules.mortgagerpc.service.impl;

import org.jeecg.modules.mapper.Wfi_materclassMapper;
import org.jeecg.modules.mortgagerpc.entity.Wfi_materclass;
import org.jeecg.modules.mortgagerpc.service.IWfi_materclassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: 工作流资料目录
 * @Author: jeecg-boot
 * @Date:   2019-08-13
 * @Version: V1.0
 */
@Service
public class Wfi_materclassServiceImpl extends ServiceImpl<Wfi_materclassMapper, Wfi_materclass> implements IWfi_materclassService {

    @Autowired
    Wfi_materclassMapper wfi_materclassMapper;

    @Override
    public List<Wfi_materclass> getMaterlist(String prolsh) {
        return wfi_materclassMapper.getMaterlist(prolsh);
    }

	@Override
	public List<String> getIdsByRyMaterclass() {
		return wfi_materclassMapper.getIdsByRyMaterclass();
	}
}
