package httpapi.AIkonwledge.intentions;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import httpapi.utils.dbutils.GetIdByName;
import httpapi.utils.httputils.HttpRequestsUtil;
import httpapi.utils.httputils.Response;
import httpapi.utils.httputils.TokenUtil;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;



/**
 * 查询某意图类别下的意图
 */

public class PublicIntentionsQuery {
    @Test(dataProvider = "intentionsDatasQuery")
    public void getIntentions(String intentionCategoryName) {

        //公共意图查询初始地址
        String url = "http://192.168.3.68:2002/ai-web/ai/intentions/public";

        //设置请求头
        Map<String, String> header = new HashMap<String, String>();
        //header.put("Content-Type", "application/json;charset=UTF-8");
        header.put("Authorization", TokenUtil.getToken());

        //根据意图类别名称获取类别id
        String id = GetIdByName.GetId(intentionCategoryName);
        //System.out.println(id);

        //设置请求参数
        Map<String, String> params = new HashMap<String, String>();
        params.put("intentionCategoryId", id);
        params.put("page", "1");
        params.put("size", "50");

        //执行请求
        Response response = HttpRequestsUtil.doGetWithBuilder(url, header, params);
        String responseStr = response.getResponseStr();
        System.out.println(responseStr);

        if(response.getStatusCode() == 200){
            JSONObject jsonObject = JSON.parseObject(responseStr);
            int res = jsonObject.getIntValue("res");

            if(res == 0){
                Assert.assertEquals(jsonObject.getString("msg"),"success");

            }else {
                Assert.assertEquals("res、msg校验失败","res应为0，msg应为success");
            }


        }else {
            Assert.assertEquals("接口返回状态码不是200","接口返回状态码应为200");
        }

    }

    @DataProvider
    public Object[][] intentionsDatasQuery(){

        Object[][] datas = {
                {"天啊修改"}, //意图类别名称
                {"江湖画扇"}  //无公共意图的意图类别
        };
        return datas;

    }



}
