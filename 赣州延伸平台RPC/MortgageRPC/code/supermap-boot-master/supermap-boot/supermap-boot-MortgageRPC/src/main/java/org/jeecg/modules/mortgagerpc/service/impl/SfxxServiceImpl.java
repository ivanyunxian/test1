package org.jeecg.modules.mortgagerpc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.jsonwebtoken.lang.Collections;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.mortgagerpc.entity.Bdc_dy;
import org.jeecg.modules.mortgagerpc.entity.Bdc_ql;
import org.jeecg.modules.mortgagerpc.entity.Bdc_qldy;
import org.jeecg.modules.mortgagerpc.entity.Bdc_sqr;
import org.jeecg.modules.mortgagerpc.entity.Bdcs_sfdy;
import org.jeecg.modules.mortgagerpc.entity.Bdcs_sfdyVO;
import org.jeecg.modules.mortgagerpc.entity.Bdcs_sfgx;
import org.jeecg.modules.mortgagerpc.service.IBdc_dyService;
import org.jeecg.modules.mortgagerpc.service.IBdc_qlService;
import org.jeecg.modules.mortgagerpc.service.IBdc_qldyService;
import org.jeecg.modules.mortgagerpc.service.IBdc_sqrService;
import org.jeecg.modules.mortgagerpc.service.IBdcs_sfdyService;
import org.jeecg.modules.mortgagerpc.service.IBdcs_sfgxService;
import org.jeecg.modules.mortgagerpc.service.SfxxService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author 李海东
 * @created by 2019/9/26 14:15
 */
@Service
public class SfxxServiceImpl implements SfxxService {

    @Autowired
    private IBdcs_sfdyService iBdcs_sfdyService;

    @Autowired
    private IBdcs_sfgxService iBdcs_sfgxService;
    @Autowired
    private IBdc_sqrService iBdc_sqrService;
    @Autowired
    private IBdc_dyService iBdc_dyService;
    @Autowired
    private IBdc_qldyService iBdc_qldyService;
    @Autowired
    private IBdc_qlService iBdc_qlService;
    @Override
    public Result list(String ywlsh) {
        // 获取关系表
        List<Bdcs_sfgx> bdcs_sfgxes = iBdcs_sfgxService.list(new QueryWrapper<Bdcs_sfgx>().eq("ywlsh", ywlsh));
        List<Bdcs_sfdy> list = iBdcs_sfdyService.list();
        String ids = bdcs_sfgxes.toString();
        List<Bdcs_sfdyVO> bdcs_sfdyVOS = new ArrayList<>();
        List<Bdc_sqr> sqrList = iBdc_sqrService.list(new QueryWrapper<Bdc_sqr>().eq("prolsh", ywlsh).eq("sqrlb","1"));
        //单元数量
        String dycount = Integer.toString(iBdc_dyService.count(new QueryWrapper<Bdc_dy>().eq("prolsh", ywlsh)));
        StringBuilder qlr = new StringBuilder();
        for (Bdc_sqr bdc_sqr : sqrList) {
                qlr.append(bdc_sqr.getSqrxm());
                qlr.append(",");
        }

        if(qlr.lastIndexOf(",")>0) {
            qlr.deleteCharAt(qlr.lastIndexOf(","));
        }
        for (Bdcs_sfdy bdcs_sfdy : list) {
            BigDecimal sfjs =new BigDecimal("0");
            Bdcs_sfdyVO bdcs_sfdyVO = new Bdcs_sfdyVO();
            BeanUtils.copyProperties(bdcs_sfdy, bdcs_sfdyVO);
            BigDecimal count = new BigDecimal(dycount);
        	if(bdcs_sfdy.getId().equals("f0e8837a8adb46488145ea6580c72619")) {
        		  //工本费增值数量,用于计算多权利人工本费增值收费
                int gbfzzcount=0;
        		 //将权利人转为map
                Map<String, Bdc_sqr> sqrMap = sqrList.stream().collect(Collectors.toMap(e -> e.getId(), o -> o));
        		  //获取权利人-单元对应关系
                List<Bdc_qldy> qldyList = iBdc_qldyService.list(new QueryWrapper<Bdc_qldy>().eq("prolsh", ywlsh));
                //获取持证方式为分别持证的权利
                List<Bdc_ql> qlList = iBdc_qlService.list(new QueryWrapper<Bdc_ql>().eq("prolsh", ywlsh).eq("czfs","02"));   
                for(Bdc_ql ql:qlList) {
                	int qlrcount =0;
                	for(Bdc_qldy qldy:qldyList) {
                		//循环判断：1.对应分别持证的权利 2.申请人在权利人列表中
                		if(ql.getQlid().equals(qldy.getQlid())&&sqrMap.containsKey(qldy.getSqrid())) {
                			qlrcount++;
                		}
                	}
                	if(qlrcount>1) {
                		gbfzzcount=gbfzzcount+qlrcount-1;
                	}
                }
                	sfjs =new BigDecimal(Integer.toString(gbfzzcount)).multiply(bdcs_sfdy.getSfjs());
        	}else {
        		
        		  sfjs= bdcs_sfdy.getSfjs().multiply(count);
        	}
            
            bdcs_sfdyVO.setSfjs(sfjs);
            if (StringUtils.contains(ids, bdcs_sfdy.getId())) {
                bdcs_sfdyVO.setChecked(true);
            }
            bdcs_sfdyVO.setKey(bdcs_sfdy.getId());
            bdcs_sfdyVO.setQlrmc(qlr.toString());
            bdcs_sfdyVOS.add(bdcs_sfdyVO);
        }
        return Result.ok(bdcs_sfdyVOS);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result update(String[] ids, String ywlsh) {
        try {
            iBdcs_sfgxService.remove(new QueryWrapper<Bdcs_sfgx>().eq("ywlsh", ywlsh));
            Arrays.stream(ids).forEach(id -> {
                Bdcs_sfgx bdcs_sfgx = new Bdcs_sfgx();
                bdcs_sfgx.setId(UUID.randomUUID().toString());
                bdcs_sfgx.setYwlsh(ywlsh);
                bdcs_sfgx.setSfdyId(id);
                iBdcs_sfgxService.save(bdcs_sfgx);
            });
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("添加失败");
        }
        return Result.ok("添加成功");
    }

    @Override
    public Result delete(String id,String ywlsh) {
    	iBdcs_sfgxService.remove(new QueryWrapper<Bdcs_sfgx>().eq("ywlsh", ywlsh).eq("sfdy_id", id));
//        iBdcs_sfgxService.removeById(id);
        return Result.ok("删除成功");
    }
}
