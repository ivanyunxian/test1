package com.supermap.intelligent.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.supermap.intelligent.dao.CommonDaoMRPC;
import com.supermap.intelligent.model.BDC_MRPC_WFI_PROCESS;
import com.supermap.intelligent.model.BDC_RISKINFO;
import com.supermap.intelligent.model.BDC_SHYQZD;
import com.supermap.intelligent.model.BDC_ZRZ;
import com.supermap.intelligent.model.BDC_ZS;
import com.supermap.intelligent.model.DJ_BDC_RISKINFO;
import com.supermap.intelligent.model.JsonMessage;
import com.supermap.intelligent.model.LOG_DECLARE_RECORD_LOG;
import com.supermap.intelligent.model.LOG_RABBITMQ;
import com.supermap.intelligent.model.Mortgage_proinst;
import com.supermap.intelligent.model.Mortgage_qlr;
import com.supermap.intelligent.model.Mortgage_slxmsh;
import com.supermap.intelligent.model.SYS_USER;
import com.supermap.intelligent.model.YSPT_ENTERPRISE;
import com.supermap.intelligent.service.IntelligentCoreMQService;
import com.supermap.intelligent.util.ConstValue.MQEnum;
import com.supermap.intelligent.util.ConstValue;
import com.supermap.intelligent.util.HttpClientUtil;
import com.supermap.intelligent.util.ManualException;
import com.supermap.intelligent.util.PdfBase64Util;
import com.supermap.intelligent.util.ResultData;
import com.supermap.realestate.registration.ViewClass.SQSPBex.SQR;
import com.supermap.realestate.registration.ViewClass.ZS;
import com.supermap.realestate.registration.model.BDCS_DYXZ;
import com.supermap.realestate.registration.model.BDCS_QL_XZ;
import com.supermap.realestate.registration.model.BDCS_QYXX;
import com.supermap.realestate.registration.model.BDCS_SHYQZD_XZ;
import com.supermap.realestate.registration.model.BDCS_SQR;
import com.supermap.realestate.registration.model.BDCS_XMXX;
import com.supermap.realestate.registration.model.BDCS_ZRZ_XZ;
import com.supermap.realestate.registration.service.DJBService;
import com.supermap.realestate.registration.service.ZSService;
import com.supermap.realestate.registration.service.ZSService2;
import com.supermap.realestate.registration.util.ConfigHelper;
import com.supermap.realestate.registration.util.GetProperties;
import com.supermap.realestate.registration.util.ProjectHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.wisdombusiness.core.SuperHelper;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
import com.supermap.wisdombusiness.workflow.model.Wfi_ActInst;
import com.supermap.wisdombusiness.workflow.model.Wfi_ProInst;
import com.supermap.wisdombusiness.workflow.model.Wfi_Spyj;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class IntelligentCoreMQServiceImpl implements IntelligentCoreMQService {
    private static final ObjectMapper MAPPER =  new ObjectMapper();

    @Autowired
    CommonDao baseCommonDao;
    @Autowired
    CommonDaoMRPC baseCommonDaoMrpc;
    /** 证书service */
    @Autowired
    private ZSService zsService;
    @Autowired
    private ZSService2 zsservice2;
    @Autowired
    private DJBService djbService;

    public Logger logger = Logger.getLogger(IntelligentCoreMQServiceImpl.class);

	@Override
    public JsonMessage sHJDReceivedMessage (String mqMsg){
		LOG_RABBITMQ log_rabbitmq =JSONObject.parseObject(mqMsg, LOG_RABBITMQ.class);
		log_rabbitmq.setId(UUID.randomUUID().toString().replace("-", ""));
		log_rabbitmq.setRequestcode(MQEnum.SHJD.Value);
		try {
			log_rabbitmq.setReceivedmsg(mqMsg);
			JSONObject mqjson=JSONObject.parseObject(mqMsg);
			List<Wfi_ActInst> list_actinst= baseCommonDao.getDataList(Wfi_ActInst.class, "actinst_id='"+mqjson.get("actinstid")+"'");
			List<Wfi_Spyj> list_spyj =baseCommonDao.getDataList(Wfi_Spyj.class,"actinst_id='"+mqjson.get("actinstid")+"'");
			BDC_MRPC_WFI_PROCESS process=  new BDC_MRPC_WFI_PROCESS();
			if (list_actinst.size()>0){
                BeanUtils.copyProperties(list_actinst.get(0),process);
            }
			process.setBhyy(StringHelper.formatObject(mqjson.get("bhyj")));//驳回意见
			if (list_spyj.size()>0){
                process.setShyj(list_spyj.get(0).getSpyj());//审核意见
            }
			process.setYwlsh(StringHelper.formatObject(mqjson.get("ywlsh")));
			process.setProlsh(StringHelper.formatObject(mqjson.get("prolsh")));
            Date createtime = null;
            try {
                createtime = StringHelper.FormatByDate(mqjson.get("createtime"), "yyyy-MM-dd HH:mm:ss");
            } catch (Exception e) {
                e.printStackTrace();
            }
			process.setCreatetime(createtime);
            baseCommonDaoMrpc.saveOrUpdate(process);
		}catch (Exception e){
			log_rabbitmq.setException(e.toString());
		}finally {
			baseCommonDao.save(log_rabbitmq);
		}
		return null;
	}
	
	@Override
    public ResultData dJBReceivedMessage(String mqMsg){
        ResultData result = new ResultData();
//	    mqMsg ="{\"date\":\"2019-09-11 12:41:52\",\"ywly\":\"3\",\"xmbh\":\"7769420ab9f74ccda4bc50e3409c0734\",\"ywlsh\":\"2019017274\",\"zszmlx\":\"ZM\",\"dbsj\":\"2019-09-11 12:41:52\",\"Baseworkflow_ID\":\"CS011\"}";
        LOG_RABBITMQ log_rabbitmq =JSONObject.parseObject(mqMsg, LOG_RABBITMQ.class);
        log_rabbitmq.setId(UUID.randomUUID().toString().replace("-", ""));
        log_rabbitmq.setRequestcode(MQEnum.DJB.Value);
        log_rabbitmq.setReceivedmsg(mqMsg);
        JSONObject mqjson=JSONObject.parseObject(mqMsg);
        try {
            log_rabbitmq.setCreatetime(new Date());
            List<BDCS_QL_XZ> ql_list = baseCommonDao.getDataList(BDCS_QL_XZ.class,"XMBH='"+mqjson.get("xmbh")+"'");
            List<Map> proinst_list = baseCommonDao.getDataListByFullSql("SELECT wlsh,areacode FROM BDC_WORKFLOW.WFI_PROINST WHERE PROLSH = '"+mqjson.get("ywlsh")+"'");
            BDC_RISKINFO riskinfo = new BDC_RISKINFO();//qlid,djdyid,bdcdyh,bdcqzh，dyzt(单元状态，根据QLLX、DJLX确定)
            riskinfo.setWlsh((String) proinst_list.get(0).get("WLSH"));
            riskinfo.setDivisionCode((String) proinst_list.get(0).get("AREACODE"));
            riskinfo.setYwlsh(mqjson.getString("ywlsh"));
            Date dbsj = null;
            try {
                dbsj = StringHelper.FormatByDate(mqjson.get("dbsj"), "yyyy-MM-dd HH:mm:ss");
            } catch (Exception e) {
                e.printStackTrace();
            }
            riskinfo.setDbsj(dbsj);
            riskinfo.setYwly(mqjson.getString("ywly"));
            riskinfo.setId(UUID.randomUUID().toString().replace("-",""));
            if (ql_list.size()>0){
                for (BDCS_QL_XZ ql:ql_list) {
                    riskinfo.setBdcdyh(ql.getBDCDYH());
                    riskinfo.setBdcqzh(ql.getBDCQZH());
                    riskinfo.setDjdyid(ql.getDJDYID());
                    riskinfo.setQlid(ql.getId());
                    if ("23".equals(ql.getQLLX()) ){
                        riskinfo.setDyzt("DY");//抵押
                    }
                    if ("800".equals(ql.getDJLX())){
                        riskinfo.setDyzt("CF");//查封
                    }
                    if ("600".equals(ql.getDJLX())){
                        riskinfo.setDyzt("YY");//异议登记
                    }
                    if ("400".equals(ql.getDJLX())&& "23".equals(ql.getQLLX())){
                        riskinfo.setDyzt("ZXDY");//注销抵押
                    }
                    if (ql.getBDCDYH().indexOf("F")!=-1){
                        List<Map> zllist = baseCommonDao.getDataListByFullSql("SELECT ZL   FROM BDCK.BDCS_H_XZ WHERE bdcdyh='"+ql.getBDCDYH()+"'");
                        riskinfo.setZl(zllist.get(0).get("ZL").toString());
                    }else {
                        List<Map> zllist = baseCommonDao.getDataListByFullSql("SELECT ZL   FROM BDCK.BDCS_SHYQZD_XZ WHERE bdcdyh='"+ql.getBDCDYH()+"'");
                        if (zllist.size()<1){
                            zllist = baseCommonDao.getDataListByFullSql("SELECT ZL   FROM BDCK.BDCS_SYQZD_XZ WHERE bdcdyh='"+ql.getBDCDYH()+"'");
                        }
                        riskinfo.setZl((String) zllist.get(0).get("ZL"));
                    }

                }
            }
            if (mqjson.get("Baseworkflow_ID").toString().contains("XZ")){
                riskinfo.setDyzt("XZ");//限制
                List<BDCS_DYXZ> ql_xz = baseCommonDao.getDataList(BDCS_DYXZ.class,"XMBH='"+mqjson.get("xmbh")+"'");
                for (BDCS_DYXZ xz:ql_xz){
                    riskinfo.setBdcdyh(xz.getBDCDYH());
                    riskinfo.setBdcqzh(xz.getBDCQZH());
                    riskinfo.setXzlx(xz.getXZLX());
                    riskinfo.setXzqsrq(xz.getXZQSRQ());
                    riskinfo.setXzzzrq(xz.getXZZZRQ());
                    if (xz.getBDCDYH().indexOf("F") !=-1){
                        List<Map> zllist = baseCommonDao.getDataListByFullSql("SELECT ZL   FROM BDCK.BDCS_H_XZ WHERE bdcdyh='"+xz.getBDCDYH()+"'");
                        if (zllist.size()<1){
                            zllist = baseCommonDao.getDataListByFullSql("SELECT ZL   FROM BDCK.BDCS_H_XZY WHERE bdcdyh='"+xz.getBDCDYH()+"'");
                        }
                        riskinfo.setZl(zllist.get(0).get("ZL").toString());
                    }else {
                        List<Map> zllist = baseCommonDao.getDataListByFullSql("SELECT ZL   FROM BDCK.BDCS_SHYQZD_XZ WHERE bdcdyh='"+xz.getBDCDYH()+"'");
                        if (zllist.size()<1){
                            zllist = baseCommonDao.getDataListByFullSql("SELECT ZL   FROM BDCK.BDCS_SYQZD_XZ WHERE bdcdyh='"+xz.getBDCDYH()+"'");
                        }
                        riskinfo.setZl((String) zllist.get(0).get("ZL"));
                    }
                }
            }
            
            List<ZS> zs = null;
//            BDCS_XMXX xmxx = baseCommonDao.get(BDCS_XMXX.class, (String) mqjson.get("xmbh"));

            List<Mortgage_proinst> proinsts = baseCommonDaoMrpc.getDataList(Mortgage_proinst.class, " prolsh='" + proinst_list.get(0).get("WLSH") + "'");
            if (proinsts != null && !proinsts.isEmpty()) {
                Mortgage_proinst proinst = proinsts.get(0);
                proinst.setShzt(30);
                proinst.setDbsj(dbsj);
                baseCommonDaoMrpc.update(proinst);
            }
            List<Map> zs_xzList = baseCommonDao.getDataListByFullSql("select *from bdck.BDCS_ZS_GZ where xmbh='"+(String) mqjson.get("xmbh")+"'");
            for (Map<String,String>zs_xz:zs_xzList   ) {
            	BDC_ZS bdc_zs = new BDC_ZS();
                Map zss=  zsService.getBDCDJZM((String) mqjson.get("xmbh"), zs_xz.get("ZSID"));
                StringHelper.mapToObject(zss,bdc_zs);
                bdc_zs.setZsid((String) zs_xz.get("ZSID"));
                bdc_zs.setProlsh((String)proinst_list.get(0).get("WLSH"));
                List<Map> qlidlist = baseCommonDao.getDataListByFullSql("select qlid,zsid from bdck.bdcs_qdzr_gz where zsid='"+zs_xz.get("ZSID")+"'");
                Map dbjgmap = djbService.getDYQDJBInfo((String) mqjson.get("xmbh"), StringHelper.formatObject(qlidlist.get(0).get("QLID")));
                bdc_zs.setDbjg(new JSONObject(dbjgmap).toString());
                baseCommonDaoMrpc.save(bdc_zs);
                baseCommonDaoMrpc.flush();
                }
            DJ_BDC_RISKINFO dj_bdc_riskinfo = new DJ_BDC_RISKINFO();
            BeanUtils.copyProperties(riskinfo,dj_bdc_riskinfo);//同步到登记系统  风险防控表，方便登簿时判断是否推送到抵押平台 风险防控表
            baseCommonDaoMrpc.save(riskinfo);
            baseCommonDao.save(dj_bdc_riskinfo);
            System.out.println("登簿信息已消费！完成BDC_ZS、BDC_RISKINFO推送------内网流水号ywlsh:"+mqjson.getString("ywlsh"));
            //登簿结果DBJG回调（调用抵押平台的接口，将登簿结果推给银行）
            String DBJGCallback = ConfigHelper.getNameByValue("DBJGCallback");
            if (StringHelper.isEmpty(DBJGCallback)){
                final LOG_DECLARE_RECORD_LOG recordLog = new LOG_DECLARE_RECORD_LOG();
                throw new ManualException(recordLog, "0", "登簿结果DBJG回调异常，请联系管理员进行配置", "2004");
            }else {
                String res = HttpClientUtil.requestGet(DBJGCallback+""+(String) proinst_list.get(0).get("WLSH"));
            }
            result.setMsg("登簿信息已消费！完成BDC_ZS、BDC_RISKINFO推送------内网流水号ywlsh:"+mqjson.getString("ywlsh")+"=====外流水号======"+(String) proinst_list.get(0).get("WLSH"));
            result.setState(true);
        }catch (Exception e){
            e.printStackTrace();
            log_rabbitmq.setException(e.toString());
        }finally {
            result.setData(mqMsg);
            baseCommonDao.save(log_rabbitmq);
            baseCommonDao.flush();
        }
	    return result;
    }

    @Override
    public ResultData pushZSToMRPC(String ywly, String wlsh) throws JsonProcessingException {
        ResultData result = new ResultData();
        List<Map> xmxx = baseCommonDao.getDataListByFullSql("SELECT XM.* FROM  BDC_WORKFLOW.WFI_PROINST PRO LEFT JOIN BDCK.BDCS_XMXX XM ON PRO.FILE_NUMBER=XM.PROJECT_ID WHERE PRO.WLSH='"+wlsh+"'");
        Map<String,Object> message = new HashMap<String, Object>();
        message.put("ywly",ywly);
        if (xmxx.size()>0){
            message.put("xmbh", StringHelper.formatObject(xmxx.get(0).get("XMBH")));
            message.put("ywlsh", StringHelper.formatObject(xmxx.get(0).get("YWLSH")));
            message.put("dbsj", StringHelper.formatObject(xmxx.get(0).get("DJSJ")));
            String Baseworkflow_ID = ProjectHelper.GetPrjInfoByXMBH(StringHelper.formatObject(xmxx.get(0).get("XMBH"))).getBaseworkflowcode();
            message.put("Baseworkflow_ID",Baseworkflow_ID);
            result =dJBReceivedMessage(MAPPER.writeValueAsString(message));
        }
        return  result;
    }

    @Override
    public JsonMessage PassBackReceivedMessage(String mqMsg) {
        LOG_RABBITMQ log_rabbitmq =JSONObject.parseObject(mqMsg, LOG_RABBITMQ.class);
        log_rabbitmq.setId(UUID.randomUUID().toString().replace("-", ""));
        log_rabbitmq.setRequestcode(MQEnum.DJBH.Value);
        try {
            log_rabbitmq.setReceivedmsg(mqMsg);
            JSONObject mqjson = JSONObject.parseObject(mqMsg);
            String prolsh = StringHelper.formatObject(mqjson.get("prolsh"));
            if (StringHelper.isEmpty(prolsh)) {
                throw new Exception("流水号不能为空");
            }
            String bhyj = StringHelper.formatObject(mqjson.get("bhyj"));
            String shry = StringHelper.formatObject(mqjson.get("shry"));
            Date createtime = null;
            try {
                createtime = StringHelper.FormatByDate(mqjson.get("createtime"), "yyyy-MM-dd HH:mm:ss");
            } catch (Exception e) {
                e.printStackTrace();
            }
            List<Mortgage_proinst> proinsts = baseCommonDaoMrpc.getDataList(Mortgage_proinst.class, " prolsh='" + prolsh + "'");
            if (proinsts == null || proinsts.isEmpty()) {
                throw new Exception("未获取到项目信息，保存驳回信息失败！流水号：" + prolsh);
            }
            Mortgage_proinst proinst = proinsts.get(0);
            proinst.setShzt(11);
            proinst.setRemarks(bhyj);
            Mortgage_slxmsh slxmsh = new Mortgage_slxmsh();
            slxmsh.setId((String) SuperHelper.GeneratePrimaryKey());
            slxmsh.setProlsh(proinst.getProlsh());
            slxmsh.setShry(shry);
            slxmsh.setShsj(createtime);
            slxmsh.setShyj(bhyj);
            slxmsh.setShzt("11");
            baseCommonDaoMrpc.saveOrUpdate(proinst);
            baseCommonDaoMrpc.save(slxmsh);
            baseCommonDaoMrpc.flush();
        } catch (Exception e) {
            log_rabbitmq.setException(e.toString());
        } finally {
            baseCommonDao.save(log_rabbitmq);
        }
        return null;
    }

	@Override
	public JsonMessage getCertReceivedMessage(String mqMsg) {
        LOG_RABBITMQ log_rabbitmq =JSONObject.parseObject(mqMsg, LOG_RABBITMQ.class);
        log_rabbitmq.setId(UUID.randomUUID().toString().replace("-", ""));
        log_rabbitmq.setRequestcode(MQEnum.CERTIFICATE.Value);
        try {
            log_rabbitmq.setReceivedmsg(mqMsg);
            JSONObject mqjson = JSONObject.parseObject(mqMsg);
            String prolsh = StringHelper.formatObject(mqjson.get("ywlsh"));
            if (StringHelper.isEmpty(prolsh)) {
                throw new Exception("流水号不能为空");
            }
            List<Wfi_ProInst> bdcproinsts = baseCommonDao.getDataList(Wfi_ProInst.class, " prolsh='"+prolsh+"'");
            if (bdcproinsts == null || bdcproinsts.isEmpty()) {
                throw new Exception("未获取到项目信息，流水号：" + prolsh);
            }
            Wfi_ProInst bdcproinst = bdcproinsts.get(0);
            List<Mortgage_proinst> proinsts = baseCommonDaoMrpc.getDataList(Mortgage_proinst.class, " prolsh='" + bdcproinst.getWLSH() + "'");
            List<BDCS_XMXX> xmxxlist = baseCommonDao.getDataList(BDCS_XMXX.class, "ywlsh='"+prolsh+"'");
            System.out.println("xmxxlist:"+xmxxlist.size());
            System.out.println("proinsts:"+proinsts.size());
            if(xmxxlist == null|| xmxxlist.isEmpty()){
            	throw new Exception("未获取到项目信息");
            }
            if (proinsts == null || proinsts.isEmpty()) {
                throw new Exception("未获取到项目信息，流水号：" + prolsh);
            }
            BDCS_XMXX xmxx = xmxxlist.get(0);
            Mortgage_proinst proinst = proinsts.get(0);
            String wlsh = proinst.getProlsh();
            List<BDC_ZS> zslist = baseCommonDaoMrpc.getDataList(BDC_ZS.class, " prolsh='"+wlsh+"'");
            System.out.println("zslist:"+zslist.size());
            List<BDCS_SQR> sqrlist = baseCommonDao.getDataList(BDCS_SQR.class, " xmbh='"+xmxx.getId()+"' and sqrlb='1'");
            System.out.println("sqrlist:"+sqrlist.size());
            if(zslist==null||zslist.isEmpty()){
            	throw new Exception("未查到证书信息");
            }
            if(sqrlist ==null||sqrlist.isEmpty()){
            	throw new Exception("无权利人信息");
            }
            BDCS_SQR sqr = sqrlist.get(0);
            for(BDC_ZS zs : zslist){
            	JSONObject json = new JSONObject();
        		json.put("zjh", sqr.getZJH());
        		json.put("zzlx", "BDC");
        		json.put("bdcqzh", zs.getBdcqzh());
        		json.put("requestcode", ConstValue.RequestzfcodeEnum.YZ.Value);
        		try {
        			String jsonobj = HttpClientUtil.requestPost(json.toJSONString(), GetProperties.getConstValueByKey("zfurl"));
        			if(jsonobj == null) {
        				throw new Exception("未获取到证照");
        			}
        			String base64 = JSONObject.parseObject(jsonobj).getString("data");
        			if(!"".equals(base64)){
        				zs.setPdf(base64);
            			baseCommonDaoMrpc.update(zs);
            			baseCommonDaoMrpc.flush();
        			}
        		} catch (Exception e) {
        			// TODO Auto-generated catch block
        			e.printStackTrace();
        		}
            }
            
        } catch (Exception e) {
            log_rabbitmq.setException(e.toString());
        } finally {
            baseCommonDao.save(log_rabbitmq);
        }
        return null;
    }

	@Override
	public JsonMessage PassBackEnterpriseMessage(String mqMsg) {
		 LOG_RABBITMQ log_rabbitmq =JSONObject.parseObject(mqMsg, LOG_RABBITMQ.class);
	        log_rabbitmq.setId(UUID.randomUUID().toString().replace("-", ""));
	        log_rabbitmq.setRequestcode(MQEnum.DJBH.Value);
	        try {
	            log_rabbitmq.setReceivedmsg(mqMsg);
	            JSONObject mqjson = JSONObject.parseObject(mqMsg);
	            String prolsh = StringHelper.formatObject(mqjson.get("prolsh"));
	            if (StringHelper.isEmpty(prolsh)) {
	                throw new Exception("流水号不能为空");
	            }
	            String type = StringHelper.formatObject(mqjson.get("type"));
	            String bhyj = StringHelper.formatObject(mqjson.get("bhyj"));
	            String shry = StringHelper.formatObject(mqjson.get("shry"));
	            String code = StringHelper.formatObject(mqjson.get("code"));
	            Date createtime = null;
	            try {
	                createtime = StringHelper.FormatByDate(mqjson.get("createtime"), "yyyy-MM-dd HH:mm:ss");
	            } catch (Exception e) {
	                e.printStackTrace();
	            }
	            if("1".equals(type)){
	            	YSPT_ENTERPRISE qyxx = baseCommonDaoMrpc.get(YSPT_ENTERPRISE.class, prolsh);
	            	String dh = "";
	            	if(qyxx==null){
	            		throw new Exception("无企业信息");
	            	}else{
	            		dh = qyxx.getREGISTER_PHONE();
	            		qyxx.setBH_YZM(code);
	            		qyxx.setSTATUS("20");
	            		qyxx.setMSG(bhyj);
	            		baseCommonDaoMrpc.update(qyxx);
	            	}
	            	List<SYS_USER> sysusers = baseCommonDaoMrpc.getDataList(SYS_USER.class, "enterprise_id='"+prolsh+"' and shzt='10'");
	            	if(sysusers==null||sysusers.size()==0){
	            		throw new Exception("无企业人员信息");
	            	}else{
	            		for(SYS_USER user : sysusers){
	            			user.setSHZT("30");
	            			baseCommonDaoMrpc.update(user);
	            		}
	            	}
	            	List<BDC_SHYQZD> zds = baseCommonDaoMrpc.getDataList(BDC_SHYQZD.class, "enterpriseid='"+prolsh+"' and status='0'");
	            	if(zds==null||zds.size()==0){
	            		throw new Exception("无宗地信息");
	            	}else{
	            		for(BDC_SHYQZD zd : zds){
	            			zd.setSTATUS("2");
	            			baseCommonDaoMrpc.update(zd);
	            		}
	            	}
	            	JSONObject json = new JSONObject();
	            	json.put("phone", dh);
	            	json.put("textcode", GetProperties.getConstValueByKey("bhcode"));
	            	json.put("code", code);
	            	HttpClientUtil.doPost( GetProperties.getConstValueByKey("dxurl"),json.toJSONString());
	            }else if("2".equals(type)){
	            	List<SYS_USER> sysusers = baseCommonDaoMrpc.getDataList(SYS_USER.class, " USERNAME in('"+prolsh+"')");
	            	if(sysusers==null||sysusers.size()==0){
	            		throw new Exception("无企业人员信息");
	            	}else{
	            		for(SYS_USER user : sysusers){
	            			user.setSHZT("30");
	            			baseCommonDaoMrpc.update(user);
	            		}
	            	}
	            }else if("3".equals(type)){
	            	List<BDC_SHYQZD> zds = baseCommonDao.getDataList(BDC_SHYQZD.class, "id in('"+prolsh+"')");
	            	if(zds==null||zds.size()==0){
	            		throw new Exception("无宗地信息");
	            	}else{
	            		for(BDC_SHYQZD zd : zds){
	            			zd.setSTATUS("2");
	            			baseCommonDaoMrpc.update(zd);
	            		}
	            	}
	            }
	            baseCommonDaoMrpc.flush();
	        } catch (Exception e) {
	            log_rabbitmq.setException(e.toString());
	        } finally {
	            baseCommonDao.save(log_rabbitmq);
	        }
	        return null;
	}

	@Override
	public JsonMessage PassOverEnterpriseMessage(String mqMsg) {
		LOG_RABBITMQ log_rabbitmq =JSONObject.parseObject(mqMsg, LOG_RABBITMQ.class);
        log_rabbitmq.setId(UUID.randomUUID().toString().replace("-", ""));
        log_rabbitmq.setRequestcode(MQEnum.DJBH.Value);
        try {
            log_rabbitmq.setReceivedmsg(mqMsg);
            JSONObject mqjson = JSONObject.parseObject(mqMsg);
            String prolsh = StringHelper.formatObject(mqjson.get("prolsh"));
            if (StringHelper.isEmpty(prolsh)) {
                throw new Exception("流水号不能为空");
            }
            String type = StringHelper.formatObject(mqjson.get("type"));
            String bhyj = StringHelper.formatObject(mqjson.get("bhyj"));
            String shry = StringHelper.formatObject(mqjson.get("shry"));
            String code = StringHelper.formatObject(mqjson.get("code"));
            
            Date createtime = null;
            try {
                createtime = StringHelper.FormatByDate(mqjson.get("createtime"), "yyyy-MM-dd HH:mm:ss");
            } catch (Exception e) {
                e.printStackTrace();
            }
            if("1".equals(type)){
            	YSPT_ENTERPRISE qyxx = baseCommonDaoMrpc.get(YSPT_ENTERPRISE.class, prolsh);
            	String dh = "";
            	String bdcdyh = StringHelper.formatObject(mqjson.get("bdcdyh"));
            	if(qyxx==null){
            		throw new Exception("无企业信息");
            	}else{
            		dh = qyxx.getREGISTER_PHONE();
            		qyxx.setBH_YZM("");
            		qyxx.setSTATUS("1");
            		qyxx.setMSG("审核通过");
            		baseCommonDaoMrpc.update(qyxx);
            	}
            	List<SYS_USER> sysusers = baseCommonDaoMrpc.getDataList(SYS_USER.class, "enterprise_id='"+prolsh+"' and shzt='10'");
            	if(sysusers==null||sysusers.size()==0){
            		throw new Exception("无企业人员信息");
            	}else{
            		for(SYS_USER user : sysusers){
            			user.setSHZT("20");
            			user.setSTATUS("1");
            			baseCommonDaoMrpc.update(user);
            		}
            	}
            	List<BDC_SHYQZD> zds = baseCommonDaoMrpc.getDataList(BDC_SHYQZD.class, "enterpriseid='"+prolsh+"' and status='0'");
            	if(zds==null||zds.size()==0){
            		throw new Exception("无宗地信息");
            	}else{
            		for(BDC_SHYQZD zd : zds){
            			zd.setSTATUS("1");
            			baseCommonDaoMrpc.update(zd);
            		}
            	}
            	List<BDCS_SHYQZD_XZ> shyqzds = baseCommonDao.getDataList(BDCS_SHYQZD_XZ.class, "bdcdyh in('"+bdcdyh+"')");
            	if(shyqzds!=null&&shyqzds.size()>0){
            		String zdid = "";
            		for(BDCS_SHYQZD_XZ shyqzd : shyqzds){
            			List<BDCS_ZRZ_XZ> zrzs = baseCommonDao.getDataList(BDCS_ZRZ_XZ.class, "zdbdcdyid='"+shyqzd.getId()+"'");
            			List<BDC_SHYQZD> bdcshyqzd  = baseCommonDaoMrpc.getDataList(BDC_SHYQZD.class, "bdcdyh='"+shyqzd.getBDCDYH()+"' and status='1'");
            			if(bdcshyqzd!=null&&bdcshyqzd.size()>0){
            				zdid = bdcshyqzd.get(0).getID();
            			}
            			if(zrzs!=null&&zrzs.size()>0){
            				for(BDCS_ZRZ_XZ zrz : zrzs){
            					BDC_ZRZ bdczrz = new BDC_ZRZ();
            					bdczrz.setBDCDYH(zrz.getBDCDYH());
            					bdczrz.setBDCDYID(zrz.getId());
            					bdczrz.setCREATETIME(new Date());
            					bdczrz.setENTERPRISEID(prolsh);
            					bdczrz.setID(UUID.randomUUID().toString());
            					bdczrz.setOPERATOR("admin");
            					bdczrz.setZDID(zdid);
            					bdczrz.setZL(zrz.getZL());
            					bdczrz.setZRZH(zrz.getZRZH());
            					baseCommonDaoMrpc.save(bdczrz);
            				}
            			}
            		}
            	}
            	JSONObject json = new JSONObject();
            	json.put("phone", dh);
            	json.put("textcode", GetProperties.getConstValueByKey("tgcode"));
            	json.put("code", GetProperties.getConstValueByKey("csmm"));
            	HttpClientUtil.doPost(GetProperties.getConstValueByKey("dxurl"),json.toJSONString());
            }else if("2".equals(type)){
            	List<SYS_USER> sysusers = baseCommonDaoMrpc.getDataList(SYS_USER.class, " USERNAME in('"+prolsh+"')");
            	if(sysusers==null||sysusers.size()==0){
            		throw new Exception("无企业人员信息");
            	}else{
            		for(SYS_USER user : sysusers){
            			user.setSHZT("20");
            			user.setSTATUS("1");
            			baseCommonDaoMrpc.update(user);
            			JSONObject json = new JSONObject();
            			json.put("phone", user.getPHONE());
                    	json.put("textcode", GetProperties.getConstValueByKey("rybltgcode"));
                    	json.put("code", user.getUSERNAME()+"和"+code);
                    	HttpClientUtil.doPost(GetProperties.getConstValueByKey("dxurl"),json.toJSONString());
            		}
            	}
            }else if("3".equals(type)){
            	
            	String bdcdyh = StringHelper.formatObject(mqjson.get("bdcdyh"));
            	List<BDC_SHYQZD> zds = baseCommonDaoMrpc.getDataList(BDC_SHYQZD.class, "id in('"+prolsh+"')");
            	if(zds==null||zds.size()==0){
            		throw new Exception("无宗地信息");
            	}else{
            		for(BDC_SHYQZD zd : zds){
            			String dh = "",qymc="";
                    	YSPT_ENTERPRISE qy = baseCommonDaoMrpc.get(YSPT_ENTERPRISE.class, zd.getENTERPRISEID());
                    	if(qy!=null){
                    		dh = qy.getREGISTER_PHONE();
                    		qymc = qy.getENTERPRISE_NAME();
                    	}
            			zd.setSTATUS("1");
            			baseCommonDaoMrpc.update(zd);
            			JSONObject json = new JSONObject();
            			json.put("phone", dh);
                    	json.put("textcode", GetProperties.getConstValueByKey("zdbltgcode"));
                    	json.put("code", qymc);
                    	HttpClientUtil.doPost(GetProperties.getConstValueByKey("dxurl"),json.toJSONString());
            		
            		}
            	}
            	List<BDCS_SHYQZD_XZ> shyqzds = baseCommonDao.getDataList(BDCS_SHYQZD_XZ.class, "bdcdyh in('"+bdcdyh+"')");
            	if(shyqzds!=null&&shyqzds.size()>0){
            		String zdid = "";
            		for(BDCS_SHYQZD_XZ shyqzd : shyqzds){
            			List<BDCS_ZRZ_XZ> zrzs = baseCommonDao.getDataList(BDCS_ZRZ_XZ.class, "zdbdcdyid='"+shyqzd.getId()+"'");
            			List<BDC_SHYQZD> bdcshyqzd  = baseCommonDaoMrpc.getDataList(BDC_SHYQZD.class, "bdcdyh='"+shyqzd.getBDCDYH()+"'");
            			if(bdcshyqzd!=null&&bdcshyqzd.size()>0){
            				zdid = bdcshyqzd.get(0).getID();
            			}
            			if(zrzs!=null&&zrzs.size()>0){
            				for(BDCS_ZRZ_XZ zrz : zrzs){
            					BDC_ZRZ bdczrz = new BDC_ZRZ();
            					bdczrz.setBDCDYH(zrz.getBDCDYH());
            					bdczrz.setBDCDYID(zrz.getId());
            					bdczrz.setCREATETIME(new Date());
            					bdczrz.setENTERPRISEID(prolsh);
            					bdczrz.setID(UUID.randomUUID().toString());
            					bdczrz.setOPERATOR("admin");
            					bdczrz.setZDID(zdid);
            					bdczrz.setZL(zrz.getZL());
            					bdczrz.setZRZH(zrz.getZRZH());
            					baseCommonDaoMrpc.save(bdczrz);
            				}
            			}
            		}
            	}
            }
            baseCommonDaoMrpc.flush();
        } catch (Exception e) {
            log_rabbitmq.setException(e.toString());
        } finally {
            baseCommonDao.save(log_rabbitmq);
        }
        return null;
	}

}
