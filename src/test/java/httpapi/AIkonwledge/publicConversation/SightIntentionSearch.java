package httpapi.AIkonwledge.publicConversation;

import com.alibaba.fastjson.JSONObject;
import httpapi.utils.dbutils.JdbcUtil;
import httpapi.utils.httputils.HttpRequestsUtil;
import httpapi.utils.httputils.Response;
import httpapi.utils.httputils.TokenUtil;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 情景话术搜索
 */
public class SightIntentionSearch {

    @Test(dataProvider = "searchSightIntentionDatas")
    public void searchSightIntention(String sightIntentionName,String sightID){
        //搜索情景话术地址
        String url = "http://192.168.3.68:2002/ai-web/ai/sight/sightPage";

        //设置请求头
        Map<String,String> header = new HashMap<String, String>();
        header.put("Authorization", TokenUtil.getToken());

        //设置请求参数
        Map<String,String> params = new HashMap<String, String>();
        params.put("sightID",sightID);
        params.put("sightIntentionName",sightIntentionName);
        params.put("page","1");
        params.put("size","10");

        //执行请求
        try {
            Response response = HttpRequestsUtil.doGetWithParams(url, header, params);
            String responseStr = response.getResponseStr();
            System.out.println(responseStr);
            if(response.getStatusCode() == 200){
                JSONObject jsonObject = JSONObject.parseObject(responseStr);
                if(jsonObject.getIntValue("res") == 0){
                    Assert.assertEquals(jsonObject.getString("msg"),"success");
                }else {
                    Assert.assertEquals("res、msg校验失败","res，msg没有预期值");
                }
            }else {
                Assert.assertEquals("接口返回状态码不是200","接口返回状态码应为200");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @DataProvider
    public Object[][] searchSightIntentionDatas(){
        Object[][] datas = {
                {"idea","5db2969514d84719b182e682"}   //需搜索的情景话术名称、情景id
        };
        return datas;
    }
}
