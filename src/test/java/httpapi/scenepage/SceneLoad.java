package httpapi.scenepage;

import httpapi.utils.httputils.HttpRequestsUtil;
import httpapi.utils.httputils.Response;
import httpapi.utils.httputils.TokenUtil;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SceneLoad {

    @Test
    public void loadScene(){
        //更新场景地址
        String url = "http://192.168.3.68:2002/ai-web/ai/scene/load/5df878ec14d847284caacbc9";

        //设置请求头
        Map<String,String> header = new HashMap<String, String>();
        header.put("Authorization", TokenUtil.getToken());

        //执行请求
        try {
            Response response = HttpRequestsUtil.doGet(url, header);
            String responseStr = response.getResponseStr();
            System.out.println(responseStr);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
