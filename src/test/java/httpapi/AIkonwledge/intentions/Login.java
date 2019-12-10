package httpapi.AIkonwledge.intentions;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import httpapi.utils.commons.ExcelUtil;
import httpapi.utils.httputils.HttpRequestsUtil;
import httpapi.utils.httputils.Response;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * 登录接口
 */
public class Login {

    @Test(dataProvider = "loginDatas")
    public void loginTest(String paramStr){
        //获取请求地址
        ResourceBundle bundle = ResourceBundle.getBundle("host");
        String hostUrl = bundle.getString("test.url");
        String url = hostUrl+bundle.getString("login.uri");

        //设置请求头
        Map<String,String> header = new HashMap<String, String>();
        header.put("Content-Type","application/json;charset=UTF-8");

        //设置请求参数
        /**
        JSONObject params = new JSONObject();
        params.put("userName",username);
        params.put("userPwd",password);
        String paramStr = params.toString();
        System.out.println(paramStr);
*/
        System.out.println(paramStr);
        //执行请求
        try {
            Response response = HttpRequestsUtil.doPostWithString(url, header, paramStr);
            String responseStr = response.getResponseStr();
            System.out.println(responseStr);

            //根据响应数据断言
            if(response.getStatusCode() == 200){
                JSONObject jsonObject = JSON.parseObject(responseStr);
                if(jsonObject.getIntValue("res") == 0){
                    Assert.assertEquals(jsonObject.getString("msg"),"success");
                }else if(jsonObject.getIntValue("res") == 10001){
                    Assert.assertEquals(jsonObject.getString("msg"),"账号错误");
                }else if(jsonObject.getIntValue("res") == 10002){
                    Assert.assertEquals(jsonObject.getString("msg"),"密码错误");
                }else {
                    Assert.assertEquals("res、msg校验失败","res、msg没有预期值");
                }

            }else {
                Assert.assertEquals("接口返回状态码不是200","接口返回状态码应为200");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    @DataProvider
    public Object[][] loginDatas(){
        Object[][] datas = ExcelUtil.readExcel(2, 4, 7);
        return datas;
    }

}
