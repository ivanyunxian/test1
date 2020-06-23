/**
 * 
 */
package org.jeecg.modules.mortgagerpc.service.impl;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jeecg.common.exception.SupermapBootException;
import org.jeecg.common.system.util.HttpClientUtil;
import org.jeecg.common.system.util.StringHelper;
import org.jeecg.common.util.Base64Util;
import org.jeecg.common.util.ConstValueMrpc;
import org.jeecg.modules.mapper.Wfi_proinstMapper;
import org.jeecg.modules.mortgagerpc.entity.Bdc_sqr;
import org.jeecg.modules.mortgagerpc.entity.Wfi_materclass;
import org.jeecg.modules.mortgagerpc.service.IEle_certificateService;
import org.jeecg.modules.mortgagerpc.service.ISys_configService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.hutool.core.util.RandomUtil;

/**
 * @author Administrator
 *
 */
@Service
public class Ele_certificateServiceImpl implements IEle_certificateService {

	@Autowired
	ISys_configService sys_configService;

	@Autowired
	Wfi_proinstMapper proinstMapper;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<File> getEcert(List<Bdc_sqr> sqrList, Wfi_materclass wfi_materclass) {
		List list = new ArrayList();
		String coreQueryUrl = sys_configService.getConfigByKey("coreQueryUrl");
		if (StringHelper.isEmpty(coreQueryUrl)) {
			throw new SupermapBootException("未配置电子证照获取接口地址，请联系管理员进行配置");
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String format = sdf.format(new Date());
        String numbers = RandomUtil.randomNumbers(6);
        String timeflag = format + numbers;
        JSONObject object = new JSONObject();
        object.put("requestcode", ConstValueMrpc.RequestcodeEnum.DZZZ.Value);
        object.put("requestseq", timeflag);
		JSONArray array = new JSONArray();
		for (Bdc_sqr sqr : sqrList) {
			JSONObject sqrjson = new JSONObject();
			sqrjson.put("sqrlb", sqr.getSqrlb());
			sqrjson.put("sqrlx", sqr.getSqrlx());
			sqrjson.put("sqrxm", sqr.getSqrxm());
			sqrjson.put("zjh", sqr.getZjh());
			sqrjson.put("zjlx", sqr.getZjlx());
			sqrjson.put("ecertcode", wfi_materclass.getEcertCode());
			array.add(sqrjson);

		}
		//object.put("ecertcode", wfi_materclass.getEcertCode());
		object.put("data", array);
		long start = System.currentTimeMillis();
		String jsonstr = HttpClientUtil.requestPost( object.toJSONString(), coreQueryUrl);
        long end = System.currentTimeMillis();
        System.out.println("_____________________"+(end-start)+"________________________");
		if(jsonstr == null) {
            throw new SupermapBootException("接口返回数据为空，请检查电子证照获取接口是否异常");
        }
        JSONObject jsonObject = JSONObject.parseObject(jsonstr);
        JSONArray resultarray = jsonObject.getJSONArray("data");
        if(resultarray!=null  && resultarray.size()>0) {
        	for (int i=0;i<resultarray.size();i++) {
        		//得到base64
        		if(resultarray.get(i) != null && !resultarray.get(i).equals("")) {
        			String temp ="D:\\pdftempecert\\"+wfi_materclass.getProlsh()+"_"+wfi_materclass.getName()+"_"+i+".pdf";
        			//PdfBase64Util.base64StringToPdf(resultarray.get(i).toString(), temp);
        			try {
						Base64Util.decodeToFile(temp, resultarray.get(i).toString());
					} catch (Exception e) {
						throw new SupermapBootException("电子证照获取接口返回数据无法转成文件，是否异常");
					}
        			File file = new File(temp);
        			if(file.exists()) {
        				list.add(file);
        				//file.delete();//删除测试的
        			}
        			
        		}
              }
        }else {
        	throw new SupermapBootException("接口返回数据为空，请检电子证照获取接口是否异常");
        }
		return list;
	}

}
