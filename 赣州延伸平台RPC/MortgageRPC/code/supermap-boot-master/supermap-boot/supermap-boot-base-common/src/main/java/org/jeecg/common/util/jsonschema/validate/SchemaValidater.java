package org.jeecg.common.util.jsonschema.validate;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jackson.JsonLoader;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.report.LogLevel;
import com.github.fge.jsonschema.core.report.ProcessingMessage;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import lombok.extern.slf4j.Slf4j;
import org.jdom2.JDOMException;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.util.XmlUtil;

import java.io.IOException;
import java.util.Iterator;

/**
 * Schema校验
 */
@Slf4j
public class SchemaValidater {

    private final static JsonSchemaFactory factory = JsonSchemaFactory.byDefault();

    public static Result validateForJson(String json, String mainSchema) throws IOException {
        Result result = new Result();
        JsonSchema schema = null;
        ProcessingReport processingReport = null;
        try {
            JsonNode mainNode = JsonLoader.fromString(mainSchema);
            JsonNode instanceNode = JsonLoader.fromString(json);
            schema = factory.getJsonSchema(mainNode);
            processingReport = schema.validate(instanceNode);
            if (processingReport.isSuccess()) {
                // 校验成功
                result.setMessage("校验成功");
                result.setSuccess(true);
                log.info(result.toString());
                return result;
            } else {
                Iterator<ProcessingMessage> it = processingReport.iterator();
                String ms = "";
                while (it.hasNext()) {
                    ProcessingMessage pm = it.next();
                    if (!LogLevel.WARNING.equals(pm.getLogLevel())) {
                        ms += pm.getMessage();
                    }

                }
                result.setMessage("校验失败！" + ms);
                result.setSuccess(false);
                log.info(result.toString());
                return result;
            }
        } catch (ProcessingException e) {
            log.error(e.getMessage(), e);
            e.printStackTrace();
        }
        result.setMessage("校验失败！校验异常" );
        result.setSuccess(false);
        return result;
    }

    /**
     * @param json       验证的Json
     * @param mainSchema 验证的Schema
     * @return
     * @throws IOException
     * @throws JDOMException
     */
    public static Result validateForXml(String json, String mainSchema) throws IOException, JDOMException {
        JSONObject jsonObject = XmlUtil.xml2JSON(json);
        return validateForJson(jsonObject.toJSONString(), mainSchema);
    }

}
