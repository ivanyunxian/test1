package org.jeecg.modules.mortgagerpc.service;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public interface ICoreAPIService {
    /**
     * token获取接口
     * @param requestJson
     * @return
     */
    String applicationToken(String requestJson);

    /**
     * 不动产查询接口
     * requestcode 1001-1004
     * @param requestJson
     * @param request
     * @return
     */
    String finalResultModule(String requestJson, HttpServletRequest request);


    /**
     * 不动产业务申报接口
     * requestcode 2001-2006
     * @param requestJson
     * @param request
     * @return
     */
    String declareProject(String requestJson, HttpServletRequest request);


    /**
     * 获取金融机构等第三方提供的结果回写接口授权token
     * @param userid
     * @return
     */
    Map<String, Object> applicationTokenFromDepart(String userid);

    /**
     * 抵押权登簿结果推送接口
     * requestcode 2001-2006
     * @param requestJson
     * @param request
     * @return
     */
    String DBCallBack(HttpServletRequest request);

}
