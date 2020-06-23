package org.jeecg.modules.mortgagerpc.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.mortgagerpc.entity.Wfi_proinst;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @Description: 项目信息
 * @Author: jeecg-boot
 * @Date:   2019-07-29
 * @Version: V1.0
 */
public interface IWfi_proinstService extends IService<Wfi_proinst> {

    IPage<Map> projectlist(Page<Map> page, HttpServletRequest request);

    void saveproject(JSONObject projectDataJson) throws Exception;

    IPage<Map> projectlistendbox(Page<Map> page, HttpServletRequest request);

    Map<String,Object> getProjectMessage(String prolsh, String dyid);

    void submitProject(String prolsh);
    void submitProject(String prolsh, String type);

    /**
     * 校验项目是否可以编辑，已经提交的不能进行项目信息，申请人，单元，权利等操作
     * @return
     */
    boolean canEdit(Wfi_proinst wfi_proinst);

    void deleteProject(Wfi_proinst wfi_proinst);

    void houseSearch(Map<String, String> param);

    String createProject(JSONObject projectData);

    void saveProjectForStep2(JSONObject projectData) throws Exception;

    void updataBdcDy(JSONObject projectjson);

    JSONObject getProjectProgress(String prolsh);

    void selecthouse(JSONObject housesdata);

    void removehouse(String houseid, String prolsh);

    void setNameAndInitDYQR(String prolsh);

    String createProject(String wfi_prodefid, String code);

    Map<String,Object> getRecipientData(String prolsh);

    JSONObject searchBdcqzh(String bdcqzh);

    JSONObject getProjectByProlsh(String prolsh);

}

