package com.supermap.wisdombusiness.framework.service.impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.supermap.wisdombusiness.framework.dao.GlobalConfigDao;
import com.supermap.wisdombusiness.framework.model.GlobalConfig;
import com.supermap.wisdombusiness.framework.service.GlobalConfigService;

@Service
public class GlobalConfigServiceImp implements  GlobalConfigService{
	
	private static String MAIN_VERSION="3.";
	private String filePaht ="resources\\workflow\\scripts\\config\\";
	
	@Autowired GlobalConfigDao globalconfigdao;
    /**
     * @author JHX
     * 读取configure.js
     * 暂时没有用到
     * @Date 2016-07-13
     * */
	@Override
	public void saveConfigurations(GlobalConfig globalconfig) {
		String jsonString="";
		String web_path = this.getClass().getClassLoader().getResource("/").getPath();
		String path = web_path.substring(0, web_path.indexOf("WEB-INF"));
		try{
			BufferedReader reader=new BufferedReader(
					new InputStreamReader(new FileInputStream(new File(path+filePaht+"configure.js")), "UTF-8")
					);
			StringBuilder sb=new StringBuilder();
			String line=null;
			while((line=reader.readLine())!=null){
			  sb.append(line+"\r");
			}
			reader.close();
			jsonString=sb.substring(sb.indexOf("{"),sb.lastIndexOf("}")+1);
			JSONObject jo=JSONObject.parseObject(jsonString);
         }catch(Exception e){
		}
		globalconfig.setConfigContent(jsonString);
		globalconfig.setVersion(new Date().getTime()+"");
		globalconfigdao.saveConfigurations(globalconfig);
		
	}
	@Override
	public List<Map> getConfiguration() {
		return globalconfigdao.queryConfiguration();
	}
	/**
	 * @author JHX
	 * 数据配置文件入库操作
	 * @Date 2016-07-13
	 * 
	 **/
	@Override
	public String excuteConfigurationStorage(String configurations) {
		String flag="";
		GlobalConfig cfg = new GlobalConfig();
		cfg.setConfigContent(configurations);
		cfg.setVersion(MAIN_VERSION+new Date().getTime());
		try{
			globalconfigdao.saveConfigurations(cfg);
			modfifyConfigureJsFileName();
			updateConfigureInfoVesion(cfg.getVersion());
			flag= "true";
		}catch(Exception e){
			flag= "false";
		}
		return flag;
	}
	/**
	 * @author JHX
	 * 检查配置文件信息是否已经初始化完成
	 * @Date 2016-07-13
	 * 
	 **/
	@Override
	public boolean chekIsStorage() {
		List<Map> map = globalconfigdao.queryConfiguration();
		if(map!=null&&map.size()==0){
			return true;
		}
		return false;
	}
	/**
	 * @author JHX
	 * 数据入库成功之后更改之前configure.js名称为：configure_bak.js
	 * 更改configureTemp.js文件名称为configure.js
	 * @Date 2016-07-13
	 * 
	 * */
	@Override
	public void modfifyConfigureJsFileName() {
		//路径
		String web_path = this.getClass().getClassLoader().getResource("/").getPath();
		String path = web_path.substring(0, web_path.indexOf("WEB-INF"));
		File file = new File(path+filePaht+"configure.js");
		if(file.exists()){
			File tempfile = new File(path+filePaht+"configure_bak.js");
			boolean isSucess=file.renameTo(tempfile);
			if(isSucess){
				//更改configureTemp名称为:configure.js
				File fileTemp = new File(path+filePaht+"configureTemp.js");
				if(fileTemp.exists()){
					File fileCfg = new File(path+filePaht+"configure.js");
					fileTemp.renameTo(fileCfg);
				}
			}
		}
	}
	/**
	 * @author JHX
	 * 数据配置信息更改之后需要更改version.js中的版本号，版本号规则："3."+new Date()..getTime()
	 * @Date 2016-07-13
	 * */
	@Override
	public void updateConfigureInfoVesion(String version) {
		  String web_path = this.getClass().getClassLoader().getResource("/").getPath();
		  String path = web_path.substring(0, web_path.indexOf("WEB-INF"));
		  //BufferedReader reader = null;
		  BufferedWriter bw=null;
		  try{
				/*reader = new BufferedReader(
						new InputStreamReader(new FileInputStream(file), "UTF-8")
						);
	            String line = reader.readLine();*/
	            bw = new BufferedWriter(new FileWriter(path+filePaht+"version.js"));
	            bw.write("\r define({ version :\""+version+"\"})");
	            bw.flush();
	            bw.close();
		  }catch(Exception e){
			  e.printStackTrace();
		  }finally{
			  if(bw!=null){
				  try {
					bw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			  }
		  }
	    }
	@Override
	public void saveOrUpdateConfiguration(GlobalConfig globalconfig) {
		globalconfig.setVersion(MAIN_VERSION+new Date().getTime());
		globalconfigdao.saveOrUpdateConfiguration(globalconfig);
		updateConfigureInfoVesion(globalconfig.getVersion());
		
	}
}
