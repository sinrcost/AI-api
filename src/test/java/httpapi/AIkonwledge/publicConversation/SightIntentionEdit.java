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
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * 编辑情景话术
 */
public class SightIntentionEdit {

    @Test(dataProvider = "sightIntentionEditDatas")
    public void editSightIntention(String sightIntentionId,String sightIntentionNameInitial,String sightIntentionName,String intentionIDs,String intentionNames,String proIntentionID,String proIntentionName,String conversationID,String content,String sampleID,Boolean isEnd,Boolean isSingle){
        //编辑情景话术初始地址
        String urlInitial = "http://192.168.3.68:2002/ai-web/ai/sight/sightIntention";

        //编辑情景话术地址
        String url = GetCompleteUrl.getUrl(urlInitial, sightIntentionId);

        //设置请求头
        Map<String,String> header = new HashMap<String, String>();
        header.put("Authorization", TokenUtil.getToken());

        //设置请求参数
        JSONObject json = new JSONObject();
        json.put("sightIntentionName",sightIntentionName);
        json.put("intentionIDs",intentionIDs);
        json.put("intentionNames",intentionNames);
        json.put("proIntentionID",proIntentionID);
        json.put("proIntentionName",proIntentionName);
        json.put("conversationID",conversationID);
        json.put("content",content);
        json.put("sampleID",sampleID);
        json.put("isEnd",isEnd);
        json.put("isSingle",isSingle);
        String jsonStr = json.toString();

        //执行请求
        try {
            Response response = HttpRequestsUtil.doPutWithString(url, header, jsonStr);
            String responseStr = response.getResponseStr();
            System.out.println(responseStr);
            if(response.getStatusCode() == 200){
                JSONObject jsonObject = JSONObject.parseObject(responseStr);
                if(jsonObject.getIntValue("res") == 0){
                    Assert.assertEquals(jsonObject.getString("msg"),"success");
                    //数据验证
                    String sqlCheck = "select * from t_sight_intention where sight_intention_id = '"+sightIntentionId+"' ";
                    ResultSet resultSetByQueryCheck = JdbcUtil.getResultSetByQuery(sqlCheck);
                    while (resultSetByQueryCheck.next()){
                        String sight_intention_name = resultSetByQueryCheck.getString("sight_intention_name");
                        Assert.assertEquals(sight_intention_name,sightIntentionName);
                    }
                    //数据还原
                    String sqlReduction = "update t_sight_intention set sight_intention_name = '"+sightIntentionNameInitial+"' where sight_intention_id = '"+sightIntentionId+"' ";
                    int i = JdbcUtil.executeUpdate(sqlReduction);
                    Assert.assertEquals(i,1);
                }else {
                    Assert.assertEquals("res、msg校验失败","res，msg没有预期值");
                }

            }else {
                Assert.assertEquals("接口返回状态码不是200","接口返回状态码应为200");
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    @DataProvider
    public Object[][] sightIntentionEditDatas(){
        Object[][] datas = {
                {"5db2bb8a14d84719b182e68f","idea新增的情景话术","idea新增的情景话术（xfsun）","5d1180448c79ac323ac1332a","断句2","","","5dc1151414d847627908046d","","",false,false}
                //情景话术id、情景话术初始名称、修改后情景话术名称、意图id、意图名称、上文意图id、上文意图名称、话术id、是否终止对话、是否接其他话术
        };
        return datas;
    }

}
