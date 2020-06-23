package org.jeecg.modules.mortgagerpc.util;

import com.alibaba.fastjson.JSONObject;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.util.StringHelper;
import org.jeecg.common.util.ConstValueMrpc;
import org.jeecg.common.util.jsonschema.validate.SchemaValidater;

import java.io.IOException;

/**
 * @ClassName CoreAPIJsonCheckUtil
 * @Description 接口json格式校验工具类
 * @Author notebao
 * @Date 2019/9/5 10:11
 */
public class CoreAPIJsonCheckUtil {

    public static Result checkJson(String decryDate) {
        Result result = new Result();
        try {
            JSONObject requestDate=JSONObject.parseObject(decryDate);
            String requestcode = requestDate.getString("requestcode");
            if(StringHelper.isEmpty(requestcode)) {
                result.setSuccess(false);
                result.setMessage(ConstValueMrpc.MrpccodingEnum.FAIL.Value);
                return result;
            }
            String schema = StringHelper.readFileContent("schema/"+requestcode+".json");
            return SchemaValidater.validateForJson(requestDate.toJSONString(), schema);
        } catch (IOException e) {
            e.printStackTrace();
        }
        result.setSuccess(false);
        return result;
    }

}
