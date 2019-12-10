package httpapi.AIkonwledge.questionBankWechat;

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

public class BankCategorySearch {

    @Test(dataProvider = "searchBankCategoryDatas")
    public void searchBankCategory(String bankCategoryName){
        //题库类别搜索初始地址
        String urlInitial = "http://192.168.3.68:2002/ai-web/ai/weChat/type";

        //题库类别搜索地址
        Map<String,String> param = new HashMap<String, String>();
        param.put("name",bankCategoryName);
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
                Assert.assertEquals(jsonObject.getIntValue("res"),0);
                Assert.assertEquals(jsonObject.getString("msg"),"success");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @DataProvider
    public Object[][] searchBankCategoryDatas(){
        Object[][] datas = {
                {"测试"}
        };
        return datas;
    }
}
