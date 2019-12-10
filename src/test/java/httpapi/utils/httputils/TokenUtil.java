package httpapi.utils.httputils;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.testng.annotations.Test;

import java.io.IOException;


public class TokenUtil {

    public static String getToken(){

        String token = null;
        //创建POST请求
        String url = "http://192.168.3.68:2002/auth/login";
        HttpPost post = new HttpPost(url);

        //接口参数
        JSONObject json = new JSONObject();
        json.put("userName", "xfsun");
        json.put("userPwd", "123");
        StringEntity entity = new StringEntity(json.toString(), ContentType.APPLICATION_JSON);
        entity.setContentType("application/json");

        //注入请求参数
        post.setEntity(entity);

        //创建客户端
        CloseableHttpClient client = HttpClients.createDefault();

        CloseableHttpResponse res = null;
        try {
            //发送请求
            res = client.execute(post);
            //获取响应
            HttpEntity httpEntity = res.getEntity();
            String strEntity = EntityUtils.toString(httpEntity);
            //获取响应中的token值
            JsonObject jsonObject = new JsonParser().parse(strEntity).getAsJsonObject();
            JsonObject content = jsonObject.getAsJsonObject("data");
            token = "Bearer"+content.get("token").getAsString();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                res.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return token;
    }
}
