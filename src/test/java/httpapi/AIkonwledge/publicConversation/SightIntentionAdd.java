package httpapi.AIkonwledge.publicConversation;

import com.alibaba.fastjson.JSONObject;
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
 * 新增情景话术
 */
public class SightIntentionAdd {

    @Test(dataProvider = "sightIntentionDatas")
    public void sightIntentionAdd(String sightID,String sightIntentionName,String intentionIDs,String intentionNames,String proIntentionID,String proIntentionName,String conversationID,Boolean isEnd,Boolean isSingle){
        //添加情景话术地址
        String url = "http://192.168.3.68:2002/ai-web/ai/sight/sightIntention";

        //设置请求头
        Map<String,String> header = new HashMap<String, String>();
        header.put("Authorization", TokenUtil.getToken());

        //设置请求参数
        JSONObject json = new JSONObject();
        json.put("sightID",sightID);
        json.put("sightIntentionName",sightIntentionName);
        json.put("intentionIDs",intentionIDs);
        json.put("intentionNames",intentionNames);
        json.put("proIntentionID",proIntentionID);
        json.put("proIntentionName",proIntentionName);
        json.put("conversationID",conversationID);
        json.put("isEnd",isEnd);
        json.put("isSingle",isSingle);
        String jsonStr = json.toString();

        //执行请求
        try {
            Response response = HttpRequestsUtil.doPostWithString(url, header, jsonStr);
            String responseStr = response.getResponseStr();
            System.out.println(responseStr);
            if(response.getStatusCode() == 200){
                JSONObject jsonObject = JSONObject.parseObject(responseStr);
                int res = jsonObject.getIntValue("res");
                if(res == 0){
                    Assert.assertEquals(jsonObject.getString("msg"),"success");
                    //验证数据
                    String sql = "select * from t_sight_intention ORDER BY create_time DESC limit 0,1";
                    ResultSet resultSetByQuery = JdbcUtil.getResultSetByQuery(sql);
                    String sight_intention_id = null;
                    while (resultSetByQuery.next()){
                        sight_intention_id = resultSetByQuery.getString("sight_intention_id");
                        String sight_intention_name = resultSetByQuery.getString("sight_intention_name");
                        Assert.assertEquals(sight_intention_name,sightIntentionName);
                    }
                    //还原数据
                    String sqlDelete = "DELETE FROM t_sight_intention WHERE sight_intention_id = '"+sight_intention_id+"' ";
                    int i = JdbcUtil.executeUpdate(sqlDelete);
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
    public Object[][] sightIntentionDatas(){
        Object[][] datas = {
                {"5db2969514d84719b182e682","idea新增的情景话术4","5d1180448c79ac323ac1332a","断句2","","","5dc1151414d847627908046d",false,false}
                //情景id、情景话术名称、意图id、意图名称、上文意图id、上文意图名称、话术id、是否终止对话,是否接其他话术
        };
        return datas;
    }
}
