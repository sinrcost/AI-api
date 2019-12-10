package httpapi.AIkonwledge.publicConversation;

import com.alibaba.fastjson.JSONObject;
import httpapi.utils.commons.GetCompleteUrl;
import httpapi.utils.dbutils.JdbcUtil;
import httpapi.utils.httputils.HttpRequestsUtil;
import httpapi.utils.httputils.Response;
import httpapi.utils.httputils.TokenUtil;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SightIntentionDelete {

    @Test(dataProvider = "sightIntentionDeleteDatas")
    public void sightIntentionDelete(String sightIntentionID){
        //删除情景初始地址
        String urlInitial = "http://192.168.3.68:2002/ai-web/ai/sight/sightIntention";

        //删除情景地址
        String url = GetCompleteUrl.getUrl(urlInitial, sightIntentionID);

        //设置请求头
        Map<String,String> header = new HashMap<String, String>();
        header.put("Authorization", TokenUtil.getToken());


        //执行请求
        try {
            Response response = HttpRequestsUtil.delete(url, header);
            String responseStr = response.getResponseStr();
            System.out.println(responseStr);
            if(response.getStatusCode() == 200){
                JSONObject jsonObject = JSONObject.parseObject(responseStr);
                if(jsonObject.getIntValue("res") == 0){
                    Assert.assertEquals(jsonObject.getString("msg"),"success");

                    //还原数据
                    String insertSql = "INSERT INTO t_sight_intention  VALUES ('5dc0ee9d14d8471085119b63','idea新增的情景话术2','5db2969514d84719b182e682','5d1180448c79ac323ac1332a','断句2','','','5dc0ec4d14d8471085119b62','xfsun','2019-11-05 11:38:05','2019-11-05 11:38:05',0,0,0) ";
                    int i = JdbcUtil.executeUpdate(insertSql);
                    Assert.assertEquals(i,1);
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
    public Object[][] sightIntentionDeleteDatas(){
        Object[][] datas = {
                {"5dc0ee9d14d8471085119b63"}  //要删除的情景话术id
        };
        return datas;
    }

}
