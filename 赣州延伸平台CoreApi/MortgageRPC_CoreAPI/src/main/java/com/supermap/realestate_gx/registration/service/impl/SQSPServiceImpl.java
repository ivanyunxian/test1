package com.supermap.realestate_gx.registration.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.supermap.realestate.registration.model.BDCS_DJDY_GZ;
import com.supermap.realestate.registration.model.BDCS_H_GZ;
import com.supermap.realestate.registration.model.BDCS_H_XZY;
import com.supermap.realestate.registration.model.BDCS_QL_GZ;
import com.supermap.realestate.registration.model.BDCS_SHYQZD_GZ;
import com.supermap.realestate.registration.model.BDCS_SHYQZD_XZ;
import com.supermap.realestate.registration.model.BDCS_SQR;
import com.supermap.realestate.registration.model.BDCS_TDYT_GZ;
import com.supermap.realestate.registration.model.BDCS_TDYT_XZ;
import com.supermap.realestate.registration.util.ConstHelper;
import com.supermap.realestate.registration.util.ProjectHelper;
import com.supermap.realestate.registration.util.StringHelper;
import com.supermap.realestate_gx.registration.util.StringUtil;
import com.supermap.wisdombusiness.framework.dao.impl.CommonDao;
@Service("SQSPServiceImpl")
public class SQSPServiceImpl {
    @Autowired
    private CommonDao dao;
    public  Map getBdcdyInfo(String xmbh){
        Map resultMap = new HashMap();
        String xmbhcondition = ProjectHelper.GetXMBHCondition(xmbh);
        List<BDCS_DJDY_GZ> listdjdy = dao.getDataList(BDCS_DJDY_GZ.class, xmbhcondition);
        List<BDCS_SQR> listsqr = dao.getDataList(BDCS_SQR.class, xmbhcondition);
        List<BDCS_SQR> qlrList = new ArrayList<BDCS_SQR>();
        List<BDCS_SQR> ywrList = new ArrayList<BDCS_SQR>();
        /*申请人信息*/
        for(BDCS_SQR sqr:listsqr){
            if("1".equals(sqr.getSQRLB())){
                qlrList.add(sqr);
            }else{
                ywrList.add(sqr);
            }
        }
        resultMap.put("qlrs", qlrList);
        resultMap.put("ywrs", ywrList);
        /*单元信息*/
        if(listdjdy.size() >0){
            BDCS_DJDY_GZ djdy = listdjdy.get(0);
            String bdcdylx = djdy.getBDCDYLX();
            String zdid = "";
            if("031".equals(bdcdylx)){
                String sql_h_gz = "select * from bdck.BDCS_H_GZ where bdcdyid = '"+djdy.getBDCDYID()+"'";
                List<Map> h_gzList = dao.getDataListByFullSql(sql_h_gz);
                if(h_gzList.size() > 0){
                    zdid = StringUtil.valueOf(h_gzList.get(0).get("ZDBDCDYID"));
                    set_h_info(h_gzList.get(0), resultMap);
                }else {
                    String sql_h_xz = "select * from bdck.BDCS_H_XZ where bdcdyid = '"+djdy.getBDCDYID()+"'";
                    List<Map> h_xzList = dao.getDataListByFullSql(sql_h_xz);
                    if(h_xzList.size() > 0){
                        zdid = StringUtil.valueOf(h_xzList.get(0).get("ZDBDCDYID"));
                        set_h_info(h_xzList.get(0), resultMap);
                    }else{
                        String sql_h_ls = "select * from bdck.BDCS_H_LS where bdcdyid = '"+djdy.getBDCDYID()+"'";
                        List<Map> h_lsList = dao.getDataListByFullSql(sql_h_ls);
                        if(h_lsList.size() > 0){
                            zdid = StringUtil.valueOf(h_lsList.get(0).get("ZDBDCDYID"));
                            set_h_info(h_lsList.get(0), resultMap);
                        }
                    }
                }
                
                /*使用权宗地*/
                zd_yt_info(resultMap, zdid);
                
            }else if("032".equals(bdcdylx)){
                
                String sql_h_gzy = "select * from bdck.BDCS_H_GZY where bdcdyid = '"+djdy.getBDCDYID()+"'";
                List<Map> h_gzyList = dao.getDataListByFullSql(sql_h_gzy);
                if(h_gzyList.size() > 0){
                    zdid = StringUtil.valueOf(h_gzyList.get(0).get("ZDBDCDYID"));
                    set_h_info(h_gzyList.get(0), resultMap);
                }else {
                    String sql_h_xzy = "select * from bdck.BDCS_H_XZY where bdcdyid = '"+djdy.getBDCDYID()+"'";
                    List<Map> h_xzyList = dao.getDataListByFullSql(sql_h_xzy);
                    if(h_xzyList.size() > 0){
                        zdid = StringUtil.valueOf(h_xzyList.get(0).get("ZDBDCDYID"));
                        set_h_info(h_xzyList.get(0), resultMap);
                    }else{
                        String sql_h_lsy = "select * from bdck.BDCS_H_LSY where bdcdyid = '"+djdy.getBDCDYID()+"'";
                        List<Map> h_lsyList = dao.getDataListByFullSql(sql_h_lsy);
                        if(h_lsyList.size() > 0){
                            zdid = StringUtil.valueOf(h_lsyList.get(0).get("ZDBDCDYID"));
                            set_h_info(h_lsyList.get(0), resultMap);
                        }
                    }
                }
                
                zd_yt_info(resultMap, zdid);
                
            }else{
                zdid = djdy.getBDCDYID();
                zd_yt_info(resultMap, zdid);
            }
        }
        
        List<BDCS_QL_GZ> qls = dao.getDataList(BDCS_QL_GZ.class, xmbhcondition);
        if(qls.size() > 0){
            resultMap.put("fj", qls.get(0).getFJ()==null?"":qls.get(0).getFJ());
        }
        return resultMap;
        
    }

    private void zd_yt_info(Map resultMap, String zdid) {
        String sql_zd_gz = "select * from bdck.BDCS_SHYQZD_GZ where bdcdyid = '"+zdid+"'";
        List<Map> zd_gzList = dao.getDataListByFullSql(sql_zd_gz);
        if(zd_gzList.size() > 0){
            String sql_tdyt_gz = "select * from bdck.BDCS_TDYT_GZ where bdcdyid = '"+zdid+"'";
            List<Map> tdyt_gzList = dao.getDataListByFullSql(sql_tdyt_gz);
            set_zd_info(zd_gzList.get(0), resultMap);
            set_tdyt_info(tdyt_gzList, resultMap);
        }else{
            String sql_zd_xz = "select * from bdck.BDCS_SHYQZD_XZ where bdcdyid = '"+zdid+"'";
            List<Map> zd_xzList = dao.getDataListByFullSql(sql_zd_xz);
                
            if(zd_xzList.size() > 0){
                String sql_tdyt_xz = "select * from bdck.BDCS_TDYT_XZ where bdcdyid = '"+zdid+"'";
                List<Map> tdyt_xzList = dao.getDataListByFullSql(sql_tdyt_xz);
                set_zd_info(zd_xzList.get(0), resultMap);
                set_tdyt_info(tdyt_xzList, resultMap);
            }else{
                String sql_zd_ls = "select * from bdck.BDCS_SHYQZD_LS where bdcdyid = '"+zdid+"'";
                List<Map> zd_lsList = dao.getDataListByFullSql(sql_zd_ls);
                if(zd_lsList.size() > 0){
                    String sql_tdyt_ls = "select * from bdck.BDCS_TDYT_ls where bdcdyid = '"+zdid+"'";
                    List<Map> tdyt_lsList = dao.getDataListByFullSql(sql_tdyt_ls);
                    set_zd_info(zd_lsList.get(0), resultMap);
                    set_tdyt_info(tdyt_lsList, resultMap);
                }
            }
        }
    }
    
    /**
     * 设置H的信息
     * @作者 ouzr
     * @创建时间 2016年5月30日下午3:43:00
     * @param hMap
     * @param resultMap
     */
    private void set_h_info(Map hMap, Map resultMap){
        
        String szc = StringUtil.valueOf(hMap.get("SZC"));
        String zcs = StringUtil.valueOf(hMap.get("ZCS"));
        String szc_zcs = szc + "/" + zcs;
        String fwxz = ConstHelper.getNameByValue("FWXZ", StringUtil.valueOf(hMap.get("FWXZ")));
        String fwmj = StringUtil.valueOf(hMap.get("SCJZMJ"));
        resultMap.put("szc_zcs", szc_zcs);
        resultMap.put("fwxz", fwxz);
        resultMap.put("fwmj", fwmj);
    }
    
    /**
     * 使用权宗地
     * @作者 Ouzr
     * @创建时间 2016年5月30日下午3:55:51
     * @param zdMap
     * @param resultMap
     */
    private void set_zd_info(Map zdMap, Map resultMap){
        String zdqlxz = ConstHelper.getNameByValue("QLXZ", StringUtil.valueOf(zdMap.get("QLXZ")));
        String zdmj = StringUtil.valueOf(zdMap.get("ZDMJ"));
        resultMap.put("zdmj", zdmj);
        resultMap.put("zdqlxz", zdqlxz);
    }
    
    /**
     * 土地用途的设置
     * @作者 ouzr
     * @创建时间 2016年5月30日下午3:55:35
     * @param tdytList
     * @param resultMap
     */
    private void set_tdyt_info(List<Map> tdytList, Map resultMap){
        String tdytStr = "";
        for(Map tdyt:tdytList){
            tdytStr +=  ConstHelper.getNameByValue("TDYT", StringUtil.valueOf(tdyt.get("TDYT"))) + ",";
        }
        if(tdytStr.endsWith(",")){
            tdytStr = tdytStr.substring(0, tdytStr.length() -1);
        }
        resultMap.put("tdyt",  tdytStr);
    }
}
