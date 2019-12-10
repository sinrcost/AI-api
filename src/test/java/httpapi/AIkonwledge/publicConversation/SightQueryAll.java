package httpapi.AIkonwledge.publicConversation;

import com.alibaba.fastjson.JSONObject;
import httpapi.utils.httputils.CreateUrl;
import httpapi.utils.httputils.HttpRequestsUtil;
import httpapi.utils.httputils.Response;
import httpapi.utils.httputils.TokenUtil;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SightQueryAll {

    @Test
    public void querySight(){
        //获取所有情景的初始地址
        String urlInitial = "http://192.168.3.68:2002/ai-web/ai/sight/allName";

        //获取所有情景的地址
        Map<String,String> para = new HashMap<String, String>();
        para.put("sightName","");
        String url = CreateUrl.getUrl(urlInitial, para);

        //设置请求头
        Map<String,String> header = new HashMap<String,String>();
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
                    Assert.assertEquals("res、msg校验失败","res，msg没有预期值");
                }

            }else {
                Assert.assertEquals("接口返回状态码不是200","接口返回状态码应为200");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }



}
