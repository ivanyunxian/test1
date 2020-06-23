package com.supermap.realestate.registration.util;

import java.io.File;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.supermap.realestate.registration.model.Log_UploadDis;
import com.supermap.realestate.registration.model.T_CONFIG;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;

@Service
public class UploadDiskTask {
	@Autowired
	private CommonDao baseCommonDao;
	private String  limit;
	private String  harddisks;
	private String  material;
	private long freePartitionSpace ;
	public UploadDiskTask() {
		this.limit=ConfigHelper.getNameByValue("LIMIT");
		this.harddisks=ConfigHelper.getNameByValue("HARDDISKS");
		this.material=ConfigHelper.getNameByValue("MATERIAL");
	}
	/**收件资料上传,磁盘容量判断+处理定时
	 * 
	 */
	public void UploadDiskTaskRun() {
		this.limit=ConfigHelper.getNameByValue("LIMIT");
		this.harddisks=ConfigHelper.getNameByValue("HARDDISKS");
		this.material=ConfigHelper.getNameByValue("MATERIAL");
		Log_UploadDis log=new Log_UploadDis();//日志
		String logStr="";
		try {
			String  b="-1";//
			String [] userDis= null;
			File diskPartition=null;
			String dsiNew="";
			if (!StringHelper.isEmpty(material)&&
					!StringHelper.isEmpty(limit)
					&& !StringHelper.isEmpty(harddisks)) {
				userDis=material.split(":");
				diskPartition = new File(userDis[0]+":");
				freePartitionSpace = diskPartition.getFreeSpace()/ (1024 *1024); //剩余空间,单位MB
				 if(freePartitionSpace-Long.valueOf(limit)<=0) {
					 logStr=material+"空间不足,更换盘符,";
//				if(true) {//测试
					 //空间不足
					 String  [] diss=harddisks.split(",");
					 for(String dis:diss) {
						 diskPartition=new File(dis+":");
						 freePartitionSpace = diskPartition.getFreeSpace()/ (1024 *1024); 
						 if(freePartitionSpace-Long.valueOf(limit)>0) {
							 dsiNew=dis+":";
							 b="1";
							 break;
						 }
					 }
					if(!StringHelper.isEmpty(dsiNew)) {
						log.setFullis(b);
						log.setNewDis(dsiNew);
						logStr+="更换成功";
						//更换成功
						T_CONFIG  t_config=ConfigHelper.getT_configByName("MATERIAL");
						String [] materialNewValue =t_config.getVALUE().split(":");
						t_config.setVALUE(dsiNew+materialNewValue[1]);
						baseCommonDao.update(t_config);
					}else {
						log.setFullis("-1");
						logStr+="更换失败";
					} 
					log .setLgdescription(logStr); 
				 }else {
					 log.setFullis("0");
					 log .setLgdescription("盘符检测完毕,空间充足"); 
				 }
			}else {
				log.setFullis("2");
				log .setLgdescription("收件资料上传,磁盘容量判断功能的配置不完整");
			}
		} catch (Exception e) {
			log.setFullis("-2");
			log .setLgdescription("系统异常");
			e.printStackTrace();
		}
		
	
		baseCommonDao.save(log);
		baseCommonDao.flush();
	       
	}
}
