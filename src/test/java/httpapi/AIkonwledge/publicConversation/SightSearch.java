package httpapi.AIkonwledge.publicConversation;

import com.alibaba.fastjson.JSONObject;
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
 * 根据名称搜索情景
 */
public class SightSearch {

    @Test(dataProvider = "sightNameDatas")
    public void getSightByName(String sightOfName){
        //搜索情景的初始地址
        String urlInitial = "http://192.168.3.68:2002/ai-web/ai/sight/allName";

        //设置请求头
        Map<String,String> header = new HashMap<String,String>();
        header.put("Authorization", TokenUtil.getToken());

        //设置请求参数
        Map<String,String> param = new HashMap<String,String>();
        param.put("sightName", sightOfName);

        //执行请求
        try {
            Response response = HttpRequestsUtil.doGetWithParams(urlInitial, header, param);
            String responseStr = response.getResponseStr();
            System.out.println(responseStr);
            if(response.getStatusCode() == 200){
                JSONObject jsonObject = JSONObject.parseObject(responseStr);
                if(jsonObject.getIntValue("res") == 0){
                    String msg = jsonObject.getString("msg");
                    Assert.assertEquals(msg,"success");
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
    public Object[][] sightNameDatas(){
        Object[][] datas = {
                {"测试"},       //存在的情景名称
                {"无返回数据"}  //不存在的情景名称
        };

        return datas;
    }
}
