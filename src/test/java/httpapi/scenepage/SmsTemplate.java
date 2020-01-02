package httpapi.scenepage;

import com.alibaba.fastjson.JSONObject;
import httpapi.utils.httputils.HttpRequestsUtil;
import httpapi.utils.httputils.Response;
import httpapi.utils.httputils.TokenUtil;
import net.sf.json.JSONArray;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SmsTemplate {

    @Test(dataProvider = "addSmsDatas")
    public void addSms(String smsName, JSONArray array){
        //添加短信地址
        String url = "http://192.168.3.68:2002/ai-web/ai/sms/template";

        //设置请求头
        Map<String,String> header = new HashMap<String, String>();
        header.put("Authorization", TokenUtil.getToken());

        //设置请求参数
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("smsName",smsName);
        jsonObject.put("smsContent",array);
        String jsonStr = jsonObject.toString();

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
    public Object[][] addSmsDatas(){

        String jsonArrayStr = "[{'type':0,'value':'短信文字内容IDEA'},{'type':1,'value':'patientName'}]";
        JSONArray jsonArray = JSONArray.fromObject(jsonArrayStr);

        Object[][] datas = {
                {"短信名称IDEA",jsonArray}
        };
        return datas;
    }

}
