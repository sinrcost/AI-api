package httpapi.scenepage;

import httpapi.utils.commons.ExcelUtil;
import httpapi.utils.commons.GetCompleteUrl;
import httpapi.utils.httputils.HttpRequestsUtil;
import httpapi.utils.httputils.Response;
import httpapi.utils.httputils.TokenUtil;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UnitPreservation {

    @Test(dataProvider = "preservationUnitDatas")
    public void preservationUnit(String jsonStr){
        //保存单元地址
        String urlInitial = "http://192.168.3.68:2002/ai-web/ai/ask-unit/phone";

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
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    @DataProvider
    public Object[][] preservationUnitDatas(){
        Object[][] data = ExcelUtil.readExcel(28,28,7);
        return data;
    }

}
