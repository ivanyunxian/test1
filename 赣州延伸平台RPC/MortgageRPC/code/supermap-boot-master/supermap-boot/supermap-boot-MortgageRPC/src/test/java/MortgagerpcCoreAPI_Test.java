import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.util.HttpClientUtil;
import org.jeecg.common.system.util.StringHelper;
import org.jeecg.common.util.ConstValue;
import org.jeecg.common.util.RSACoder;
import org.jeecg.common.util.jsonschema.validate.SchemaValidater;
import org.jeecg.modules.mortgagerpc.service.impl.CoreAPIServiceImpl;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName MortgagerpcCoreAPI_Test
 * @Description TODO
 * @Author notebao
 * @Date 2019/9/2 16:12
 */
public class MortgagerpcCoreAPI_Test {

    @Test
    public void testGetToken() throws Exception {
        String data = "{\n" +
                "\"requestcode\": \"1000\",\n" +
                "\"requestseq\": \"yyyyMMddHHmmssffffff\",\n" +
                "\"data\": {\n" +
                "\"username\": \"admin\",\n" +
                "\"appcode\": \"123456\",\n"+
                "\"jgdm\": \"\",\n"+
                "\"xzqdm\": \"450020\",\n"+
                "}\n" +
                "}";
        String jsonstr = HttpClientUtil.requestPost( RSACoder.encryptByPublicKeyBase64(data), "http://localhost:8080/mrpc/mortgagerpc/applicationToken");
        String result = RSACoder.decryptByPublicKeyBase64(jsonstr);
        System.out.println(result);

    }

    @Test
    public void testMrpcAPI() throws Exception {
        String data = "{\n" +
                "\"requestcode\": \"1000\",\n" +
                "\"requestseq\": \"yyyyMMddHHmmssffffff\",\n" +
                "\"data\": {\n" +
                "\"username\": \"admin\",\n" +
                "\"appcode\": \"123456\",\n"+
                "\"jgdm\": \"\",\n"+
                "\"xzqdm\": \"450020\",\n"+
                "}\n" +
                "}";
        String jsonstr = HttpClientUtil.requestPost( RSACoder.encryptByPublicKeyBase64(data), "http://localhost:8080/mrpc/mortgagerpc/applicationToken");
        String result = RSACoder.decryptByPublicKeyBase64(jsonstr);
        System.out.println(result);
        JSONObject jsonObject = JSONObject.parseObject(result);
        JSONObject tokendata = jsonObject.getJSONObject("data");
        String token = tokendata.getString("token");

        String coreQueryUrl = "http://localhost:8080/mrpc/mortgagerpc/mrpcAPI_search?token="+token;
//        String jdresult = HttpClientUtil.requestPost( RSACoder.encryptByPublicKeyBase64("{\n" +
//                "\"requestcode\": \"1004\",\n" +
//                "\"requestseq\": \"yyyyMMddHHmmssffffff\",\n" +
//                "\"data\": [{\n" +
//                "\"xzqdm\": \"450200\",\n" +
//                "\"ywlsh\": \"2019000156\"\n" +
//                "}]\n" +
//                "}"), coreQueryUrl);
        String jdresult = HttpClientUtil.requestPost( RSACoder.encryptByPublicKeyBase64("{\n" +
                "    \"requestcode\": \"1003\",\n" +
                "    \"requestseq\": \"yyyymmddhhmmssffffff\",\n" +
                "    \"data\": [\n" +
                "        {\n" +
                "            \"bdcqzh\": \"桂(2016)河池市不动产权第0000948号\",\n" +
                "            \"xzqdm\": \"450200\",\n" +
                "            \"jgdm\": \"1101\",\n" +
                "            \"qlr\": [\n" +
                "                {\n" +
                "                    \"qlrmc\": \"莫长沙\",\n" +
                "                    \"zjhm\": \"452724198609162511\"\n" +
                "                }\n" +
                "            ]\n" +
                "        }\n" +
                "    ]\n" +
                "}"), coreQueryUrl);

        String mrpcapi = RSACoder.decryptByPublicKeyBase64(jdresult);
        System.out.println(mrpcapi);

    }

    @Test
    public void testSchemeContent() throws Exception {
        String json = "{\n" +
                "\"requestcode\": \"1000\",\n" +
                "\"requestseq\": \"yyyyMMddHHmmssffffff\",\n" +
                "\"data\": {\n" +
                "\"username\": \"zhangsan\",\n" +
                "\"appcode\": \"12345678\",\n"+
                "\"xzqdm\": \"450020\",\n"+
                "}\n" +
                "}";
        boolean isjson1 = StringHelper.isjson(json);
        JSONObject parseObject = JSONObject.parseObject(json);
        // 验证的Scheme
        String schemeContent = StringHelper.readFileContent("schema/1000.json");
        boolean isjson = StringHelper.isjson(schemeContent);
        if (isjson) {
            JSONObject jsonObject = JSONObject.parseObject(schemeContent);
            try {
                Result b = SchemaValidater.validateForJson(parseObject.toJSONString(), jsonObject.toJSONString());
                System.out.println("json合法性校验验证结果：" + b);
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println(jsonObject);
        }

    }

    @Test
    public void testDY() throws Exception {
        String data = "{\n" +
                "\"requestcode\": \"1000\",\n" +
                "\"requestseq\": \"1568014440058\",\n" +
                "\"data\": {\n" +
                "\"username\": \"admin\",\n" +
                "\"appcode\": \"123456\",\n"+
                "\"jgdm\": \"\",\n"+
                "\"xzqdm\": \"450020\",\n"+
                "}\n" +
                "}";
        String jsonstr = HttpClientUtil.requestPost( RSACoder.encryptByPublicKeyBase64(data), "http://localhost:8080/mrpc/mortgagerpc/applicationToken");
        String result = RSACoder.decryptByPublicKeyBase64(jsonstr);
        System.out.println(result);
        JSONObject jsonObject = JSONObject.parseObject(result);
        JSONObject tokendata = jsonObject.getJSONObject("data");
        String token = tokendata.getString("token");
        String testJson = "{\n" +
                "    \"requestcode\": \"2001\",\n" +
                "    \"requestseq\": \"yyyymmddhhmmssffffff\",\n" +
                "    \"data\": {\n" +
                "        \"ywlsh\": \"2019007001\",\n" +
                "        \"dyhth\": \"210674564156\",\n" +
                "        \"sbrymc\": \"钱七\",\n" +
                "        \"sbryzjh\": \"45728871781237123\",\n" +
                "        \"sbrylxdh\": \"18777777777\",\n" +
                "        \"sbjgmc\": \"中国银行柳州分行\",\n" +
                "        \"sbjgzjh\": \"88888-8\",\n" +
                "        \"xzqdm\": \"450200\",\n" +
                "        \"jgdm\": \"1001\",\n" +
                "        \"ywrlist\": [\n" +
                "            {\n" +
                "                \"ywrmc\": \"阳昌荣\",\n" +
                "                \"ywrzjh\": \"450303195608090027\",\n" +
                "                \"ywrzjzl\": \"1\",\n" +
                "                \"ywrlxdh\": \"18777777777\",\n" +
                "                \"dlrmc\": \"王彦国\",\n" +
                "                \"dlrzjh\": \"450722229191239321\",\n" +
                "                \"dlrzjzl\": \"1\",\n" +
                "                \"dlrlxdh\": \"18777777777\",\n" +
                "                \"frmc\": \"\",\n" +
                "                \"frzjh\": \"\",\n" +
                "                \"frzjzl\": \"\",\n" +
                "                \"frlxdh\": \"\",\n" +
                "                \"gyfs\": \"1\",\n" +
                "                \"qlbl\": \"\",\n" +
                "                \"qlrlx\": \"1\",\n" +
                "                \"bdcqzh\": \"桂林市房权证象山区字第30009920号\",\n" +
                "                \"sxh\": 1,\n" +
                "                \"qlrlb\": \"2\"\n" +
                "            }\n" +
                "        ],\n" +
                "        \"dyqrlist\": [\n" +
                "            {\n" +
                "                \"dyqrmc\": \"中国银行柳州分行\",\n" +
                "                \"dyqrzjh\": \"88888-8\",\n" +
                "                \"dyqrzjzl\": \"7\",\n" +
                "                \"dyqrlxdh\": \"0771-2019123192\",\n" +
                "                \"dlrmc\": \"王五\",\n" +
                "                \"dlrzjh\": \"450722229191239321\",\n" +
                "                \"dlrzjzl\": \"1\",\n" +
                "                \"dlrlxdh\": \"18777777777\",\n" +
                "                \"frmc\": \"李四\",\n" +
                "                \"frzjh\": \"450722229191239321\",\n" +
                "                \"frzjzl\": \"1\",\n" +
                "                \"qlrlx\": \"1\",\n" +
                "                \"qlrlb\": \"1\",\n" +
                "                \"frlxdh\": \"\"\n" +
                "            }\n" +
                "        ],\n" +
                "        \"dylist\": [\n" +
                "            {\n" +
                "                \"bdcdyh\": \"450302003009GB00025F00010001\",\n" +
                "                \"zl\": \"象山区黑山宿舍区5栋3-9号房\",\n" +
                "                \"dgzqse\": 770000\n" +
                "            }\n" +
                "        ],\n" +
                "        \"materclass\": [\n" +
                "            {\n" +
                "                \"material_name\": \"抵押合同\",\n" +
                "                \"material_count\": 1,\n" +
                "                \"material_desc\": \"抵押合同备注\",\n" +
                "                \"materdata\": [\n" +
                "                    {\n" +
                "                        \"file_id\": \"48f545b77b8b0fcde8776ef88d61ab9d\",\n" +
                "                        \"file_name\": \"抵押合同\",\n" +
                "                        \"file_postfix\": \"pdf\",\n" +
                "                        \"file_index\": 1,\n" +
                "                        \"file_url\": \"/api/{file_id}\"\n" +
                "                    }\n" +
                "                ]\n" +
                "            }\n" +
                "        ],\n" +
                "        \"ql\": {\n" +
                "            \"zqse\": 556644,\n" +
                "            \"djyy\": \"value\",\n" +
                "            \"dyfs\": \"1\",\n" +
                "            \"dyqlqssj\": \"2010-07-19 12:08:00\",\n" +
                "            \"dyqljssj\": \"2019-07-19 12:08:00\",\n" +
                "            \"fj\": \"value\"\n" +
                "        }\n" +
                "    }\n" +
                "}";
//        JSONObject jsonObject = JSONObject.parseObject(testJson);
//        JSONObject analysisData = CoreAPIServiceImpl.AnalysisData(jsonObject);
//        System.out.println(analysisData);
        String declareResult = HttpClientUtil.requestPost(RSACoder.encryptByPublicKeyBase64(testJson), "http://localhost:8080/mrpc/mortgagerpc/mrpcAPI_declare?token=" + token);
        String s = RSACoder.decryptByPublicKeyBase64(declareResult);
        System.out.println(s);
    }

    @Test
    public void testZX() throws Exception {
        String data = "{\n" +
                "\"requestcode\": \"1000\",\n" +
                "\"requestseq\": \"1568014440058\",\n" +
                "\"data\": {\n" +
                "\"username\": \"admin\",\n" +
                "\"appcode\": \"123456\",\n"+
                "\"jgdm\": \"\",\n"+
                "\"xzqdm\": \"450020\",\n"+
                "}\n" +
                "}";
        String jsonstr = HttpClientUtil.requestPost( RSACoder.encryptByPublicKeyBase64(data), "http://localhost:8080/mrpc/mortgagerpc/applicationToken");
        String result = RSACoder.decryptByPublicKeyBase64(jsonstr);
        System.out.println(result);
        JSONObject jsonObject = JSONObject.parseObject(result);
        JSONObject tokendata = jsonObject.getJSONObject("data");
        String token = tokendata.getString("token");
        String testJson = "{\n" +
                "    \"requestcode\": \"2004\",\n" +
                "    \"requestseq\": \"yyyymmddhhmmssffffff\",\n" +
                "    \"data\": {\n" +
                "        \"ywlsh\": \"2019007001\",\n" +
                "        \"dyhth\": \"210674564156\",\n" +
                "        \"sbrymc\": \"钱七\",\n" +
                "        \"sbryzjh\": \"45728871781237123\",\n" +
                "        \"sbrylxdh\": \"18777777777\",\n" +
                "        \"sbjgmc\": \"中国银行柳州分行\",\n" +
                "        \"sbjgzjh\": \"88888-8\",\n" +
                "        \"xzqdm\": \"450200\",\n" +
                "        \"jgdm\": \"1001\",\n" +
                "        \"qlrlist\": [\n" +
                "            {\n" +
                "                \"qlrmc\": \"桂林市住房公积金管理中心\",\n" +
                "                \"qlrzjh\": \"12450300498666255J\",\n" +
                "                \"qlrzjzl\": \"1\",\n" +
                "                \"qlrlxdh\": \"18777777777\",\n" +
                "                \"dlrmc\": \"王彦国\",\n" +
                "                \"dlrzjh\": \"450722229191239321\",\n" +
                "                \"dlrzjzl\": \"1\",\n" +
                "                \"dlrlxdh\": \"18777777777\",\n" +
                "                \"frmc\": \"\",\n" +
                "                \"frzjh\": \"\",\n" +
                "                \"frzjzl\": \"\",\n" +
                "                \"frlxdh\": \"\",\n" +
                "                \"gyfs\": \"1\",\n" +
                "                \"qlbl\": \"\",\n" +
                "                \"qlrlx\": \"1\",\n" +
                "                \"bdcqzh\": \"桂(2017)桂林市不动产证明第0000738号\",\n" +
                "                \"sxh\": 1,\n" +
                "                \"qlrlb\": \"2\"\n" +
                "            }\n" +
                "        ],\n" +
                "        \"ywrlist\": [\n" +
                "            {\n" +
                "                \"ywrmc\": \"何枫\",\n" +
                "                \"ywrzjh\": \"450304198305101518\",\n" +
                "                \"ywrzjzl\": \"1\",\n" +
                "                \"ywrlxdh\": \"18777777777\",\n" +
                "                \"dlrmc\": \"王彦国\",\n" +
                "                \"dlrzjh\": \"450722229191239321\",\n" +
                "                \"dlrzjzl\": \"1\",\n" +
                "                \"dlrlxdh\": \"18777777777\",\n" +
                "                \"frmc\": \"\",\n" +
                "                \"frzjh\": \"\",\n" +
                "                \"frzjzl\": \"\",\n" +
                "                \"frlxdh\": \"\",\n" +
                "                \"gyfs\": \"1\",\n" +
                "                \"qlbl\": \"\",\n" +
                "                \"qlrlx\": \"1\",\n" +
                "                \"bdcqzh\": \"桂林市房权证象山区字第30009920号\",\n" +
                "                \"sxh\": 1,\n" +
                "                \"qlrlb\": \"2\"\n" +
                "            }\n" +
                "        ],\n" +
                "        \"materclass\": [\n" +
                "            {\n" +
                "                \"material_name\": \"抵押合同\",\n" +
                "                \"material_count\": 1,\n" +
                "                \"material_desc\": \"抵押合同备注\",\n" +
                "                \"materdata\": [\n" +
                "                    {\n" +
                "                        \"file_id\": \"48f545b77b8b0fcde8776ef88d61ab9d\",\n" +
                "                        \"file_name\": \"抵押合同\",\n" +
                "                        \"file_postfix\": \"pdf\",\n" +
                "                        \"file_index\": 1,\n" +
                "                        \"file_url\": \"/api/{file_id}\"\n" +
                "                    }\n" +
                "                ]\n" +
                "            }\n" +
                "        ],\n" +
                "        \"dylist\": [\n" +
                "            {\n" +
                "                \"bdcdyh\": \"450304001003GB00083F00010001\",\n" +
                "                \"zl\": \"象山区青竹路40栋3-6-2号房\",\n" +
                "                \"dgzqse\": 880000\n" +
                "            }\n" +
                "        ],\n" +
                "        \"ql\": {\n" +
                "            \"zqse\": 556644,\n" +
                "            \"zxdjyy\": \"抵押风险防控平台-抵押注销测试\",\n" +
                "            \"dyfs\": \"1\",\n" +
                "            \"dyqlqssj\": \"2010-07-19 12:08:00\",\n" +
                "            \"dyqljssj\": \"2019-07-19 12:08:00\",\n" +
                "            \"fj\": \"这是附记。。。。。\"\n" +
                "        }\n" +
                "    }\n" +
                "}";
        String declareResult = HttpClientUtil.requestPost(RSACoder.encryptByPublicKeyBase64(testJson), "http://localhost:8080/mrpc/mortgagerpc/mrpcAPI_declare?token=" + token);
        String s = RSACoder.decryptByPublicKeyBase64(declareResult);
        System.out.println(s);
    }

}
