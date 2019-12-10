package httpapi.AIkonwledge.intentions;

import com.alibaba.fastjson.JSONObject;
import httpapi.utils.httputils.CreateUrl;
import httpapi.utils.httputils.HttpRequestsUtil;
import httpapi.utils.httputils.Response;
import httpapi.utils.httputils.TokenUtil;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PublicIntentionSearch {

    @Test(dataProvider = "searchPublicIntentionDatas")
    public void searchPublicIntention(String intentionName){
        //意图搜索初始地址
        String urlInitial = "http://192.168.3.68:2002/ai-web/ai/intentions/categoryOrIntention?name=%E6%B5%8B%E8%AF%95";

        //意图搜索地址
        Map<String,String> param = new HashMap<String, String>();
        param.put("name",intentionName);
        String url = CreateUrl.getUrl(urlInitial, param);

        //设置请求头
        Map<String,String> header = new HashMap<String, String>();
        header.put("Authorization", TokenUtil.getToken());

        //执行请求
        try {
            Response response = HttpRequestsUtil.doGet(url, header);
            String responseStr = response.getResponseStr();
            System.out.println(responseStr);

            if(response.getStatusCode() == 200){
                JSONObject jsonObject = JSONObject.parseObject(responseStr);
                if(jsonObject.getIntValue("res") == 0){
                    Assert.assertEquals(jsonObject.getString("msg"),"success");
                }else {
                    Assert.assertEquals("res、msg校验失败","res应为0，msg应为success");
                }

            }else {
                Assert.assertEquals("接口返回状态码不是200","接口返回状态码应为200");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @DataProvider
    public Object[][] searchPublicIntentionDatas(){
        Object[][] datas = {
                {"测试"}  //需搜索的意图名称
        };
        return datas;
    }
}
