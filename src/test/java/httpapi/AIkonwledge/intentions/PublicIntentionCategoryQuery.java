package httpapi.AIkonwledge.intentions;

/**
 * 查询所有的意图类别
 */

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import httpapi.utils.httputils.TokenUtil;
import org.testng.Assert;
import org.testng.annotations.Test;
import httpapi.utils.httputils.HttpRequestsUtil;
import httpapi.utils.httputils.Response;


public class PublicIntentionCategoryQuery {

    @Test
    public void getPublicIntentionType(){
        //获取请求地址
        ResourceBundle bundle = ResourceBundle.getBundle("host");
        String hostUrl = bundle.getString("test.url");
        String url = hostUrl+bundle.getString("publicIntentionCategoryQuery.uri");

        //设置请求头
        Map<String,String> header = new HashMap<String, String>();
        header.put("Authorization", TokenUtil.getToken());

        try {
            //执行请求
            Response response = HttpRequestsUtil.doGet(url, header);
            String responseStr = response.getResponseStr();
            System.out.println(responseStr);

            if(response.getStatusCode() == 200){
                JSONObject jsonObject = JSON.parseObject(responseStr);

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

}
