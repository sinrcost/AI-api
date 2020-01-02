package httpapi.scenepage;

import com.alibaba.fastjson.JSONObject;
import httpapi.utils.commons.ExcelUtil;
import httpapi.utils.commons.GetCompleteUrl;
import httpapi.utils.httputils.HttpRequestsUtil;
import httpapi.utils.httputils.Response;
import httpapi.utils.httputils.TokenUtil;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UnitAdd {

    @Test(dataProvider = "addUnitDatas")
    public void addUnit(String jsonStr){
        //添加单元地址
        String urlInitial = "http://192.168.3.68:2002/ai-web/ai/ask-unit/bind";

        //获取单元所属场景id
         Object[][] objects= ExcelUtil.readExcel(15,15,10);
        String data = (String)objects[0][0];

        //添加单元完整地址
        String url = GetCompleteUrl.getUrlWithParam(urlInitial,data);

        //设置请求头
        Map<String,String> header = new HashMap<String, String>();
        header.put("Authorization", TokenUtil.getToken());

        //执行请求
        try {
            Response response = HttpRequestsUtil.doPostWithString(url, header, jsonStr);
            String responseStr = response.getResponseStr();
            System.out.println(responseStr);
            if(response.getStatusCode() == 200){
                JSONObject jsonObject = JSONObject.parseObject(responseStr);
                if(jsonObject.getIntValue("res") == 0){
                    Assert.assertEquals(jsonObject.getString("msg"),"success");
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
    public Object[][] addUnitDatas(){
        Object[][] datas = ExcelUtil.readExcel(27,27,7);
        return datas;
    }


}
