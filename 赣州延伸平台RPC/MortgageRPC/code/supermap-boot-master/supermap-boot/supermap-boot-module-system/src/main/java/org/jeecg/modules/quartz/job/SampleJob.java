package org.jeecg.modules.quartz.job;

import java.util.List;
import java.util.stream.Collectors;

import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.mortgagerpc.entity.Wfi_materclass;
import org.jeecg.modules.mortgagerpc.service.IWfi_materclassService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import lombok.extern.slf4j.Slf4j;

/**
 * 示例不带参定时任务
 * 
 * @Author Scott
 */
@Slf4j
public class SampleJob implements Job {
	
	@Autowired
	private IWfi_materclassService materclass;

	@Override
	public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

		log.info(String.format(" Jeecg-Boot 删除附件定时任务 SampleJob开始 !  时间:" + DateUtils.getTimestamp()));
		List<String> idList = materclass.getIdsByRyMaterclass();
		if(idList!=null && idList.size()>0) {
			materclass.removeByIds(idList);
		}
		log.info(String.format(" Jeecg-Boot 删除附件定时任务 SampleJob结束 !  时间:" + DateUtils.getTimestamp()));
	}
}
