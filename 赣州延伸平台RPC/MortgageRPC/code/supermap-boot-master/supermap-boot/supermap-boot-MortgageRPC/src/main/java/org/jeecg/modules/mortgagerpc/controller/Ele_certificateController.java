/**
 * 
 */
package org.jeecg.modules.mortgagerpc.controller;

import java.io.File;
import java.util.Date;
import java.util.List;

import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.exception.SupermapBootException;
import org.jeecg.common.system.util.StringHelper;
import org.jeecg.modules.mortgagerpc.entity.Bdc_sqr;
import org.jeecg.modules.mortgagerpc.entity.Wfi_materclass;
import org.jeecg.modules.mortgagerpc.entity.Wfi_materdata;
import org.jeecg.modules.mortgagerpc.mongo.serviece.MongoServerce;
import org.jeecg.modules.mortgagerpc.service.IBdc_sqrService;
import org.jeecg.modules.mortgagerpc.service.IEle_certificateService;
import org.jeecg.modules.mortgagerpc.service.IWfi_materdataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * @Description: 电子证照
 * @Author: jeecg-boot
 * @Date: 2019-09-28
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "电子证照")
@RestController
@RequestMapping("/modules/certificate")
public class Ele_certificateController {

	@Autowired
	private IEle_certificateService ele_certificateService;
	
	@Autowired
	private IBdc_sqrService bdc_sqrService;
	
	@Autowired
    private MongoServerce mongoserverce;
	
    @Autowired
    private IWfi_materdataService wfi_materdataService;
    
    /**
	 * 获取房源信息接口 获取到房源信息后
	 *
	 * @return
	 */
	@AutoLog(value = "信息查询-获取电子证照")
	@ApiOperation(value = "信息查询-获取电子证照", notes = "信息查询-获取电子证照")
	@PostMapping(value = "/getEcert")
	public Result<?> getEcert(@RequestBody  Wfi_materclass wfi_materclass) {
		Result<?> result = new Result<>();
		List<Bdc_sqr> sqrList = bdc_sqrService.list(new QueryWrapper<Bdc_sqr>().eq("prolsh",
				wfi_materclass.getProlsh()).eq("sqrlb","2"));//义务人
		if(sqrList!=null && sqrList.size()>0) {
			List<File> files =ele_certificateService.getEcert(sqrList,wfi_materclass);
			if(files==null || files.size()==0) {
				result.error500("电子证照接口异常，请联系管理员核查问题");
			}
			for(File file:files) {
				try {
					if (file!=null&&wfi_materclass!=null&&wfi_materclass.getId()!=null){
		                String mongoid=mongoserverce.save(file);
		                Wfi_materdata wfi_materdata=new Wfi_materdata();
		                String filepath = file.getName();
		            	String suffix = filepath.substring(filepath.indexOf("."));
		                if(!StringHelper.isEmpty(mongoid)){
		                    wfi_materdata.setCreated(new Date());
		                    wfi_materdata.setMongoid(mongoid);
		                    wfi_materdata.setFileindex(StringHelper.getLong(wfi_materclass.getMaterdataindex()));
		                    wfi_materdata.setName(file.getName());
		                    wfi_materdata.setStatus(1);
		                    wfi_materdata.setMaterinstId(wfi_materclass.getId());
		                    wfi_materdata.setProlsh(wfi_materclass.getProlsh());
		                    wfi_materdata.setSuffix(suffix);
		                    wfi_materdataService.saveOrUpdate(wfi_materdata);
		                    result.setSuccess(true);
		                    result.setMessage("上传成功");
		                }

		            }
				} catch (SupermapBootException e) {
					log.error(e.getMessage(), e);
					result.error500("获取失败：" + e.getMessage());
				} catch (Exception e) {
					log.error(e.getMessage(), e);
					result.error500("电子证照接口异常，请联系管理员核查问题");
				}
			}
			
		}else {
			result.setSuccess(false);
			result.setMessage("无申请人信息");
		}
		
		
		return result;
	}
}
