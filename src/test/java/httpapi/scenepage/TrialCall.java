package httpapi.scenepage;

import httpapi.utils.commons.ExcelUtil;
import httpapi.utils.httputils.HttpRequestsUtil;
import httpapi.utils.httputils.Response;
import httpapi.utils.httputils.TokenUtil;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TrialCall {

    @Test(dataProvider = "callTrialDatas")
    public void callTrial(String jsonStr){
        //拨打试用任务地址
        String url = "http://192.168.3.68:2002/ai-web/ai/task/trial";

        //设置请求头
        Map<String,String> header = new HashMap<String, String>();
        header.put("Authorization", TokenUtil.getToken());

        //执行请求
        try {
            Response response = HttpRequestsUtil.doPostWithString(url, header, jsonStr);
            String responseStr = response.getResponseStr();
            System.out.println(responseStr);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @DataProvider
    public Object[][] callTrialDatas(){
        Object[][] datas = ExcelUtil.readExcel(25,25,7);
        return datas;
    }

}
