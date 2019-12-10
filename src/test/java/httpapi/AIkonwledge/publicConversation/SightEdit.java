package httpapi.AIkonwledge.publicConversation;

import com.alibaba.fastjson.JSONObject;
import httpapi.utils.commons.GetCompleteUrl;
import httpapi.utils.dbutils.GetIdByName;
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
 * 编辑情景
 */
public class SightEdit {

    @Test(dataProvider = "sightEditDatas")
    public void sightEdit(String sightNameInitial,String sightName){
        //编辑情景初始地址
        String urlinitial = "http://192.168.3.68:2002/ai-web/ai/sight/sight";

        //根据情景名称获取id
        String sqlSelect = "SELECT sight_id from t_sight where sight_name = '"+sightNameInitial+"' ";
        String sight_id = GetIdByName.getIdByName(sqlSelect, "sight_id");

        //编辑情景地址
        String url = GetCompleteUrl.getUrl(urlinitial, sight_id);

        //设置请求头
        Map<String,String> header = new HashMap<String, String>();
        header.put("Authorization", TokenUtil.getToken());

        //设置请求体
        JSONObject json = new JSONObject();
        json.put("sightName",sightName);
        String jsonStr = json.toString();

        //执行请求
        try {
            Response response = HttpRequestsUtil.doPutWithString(url, header, jsonStr);
            String responseStr = response.getResponseStr();
            System.out.println(responseStr);

            if(response.getStatusCode() == 200){
                JSONObject jsonObject = JSONObject.parseObject(responseStr);
                int res = jsonObject.getIntValue("res");

                if(res == 0){
                    String msg = jsonObject.getString("msg");
                    Assert.assertEquals(msg,"success");
                    //数据验证
                    try {
                        String sqlCheck = "select * from t_sight where sight_id = '"+sight_id+"' ";
                        ResultSet resultSetByQuery = JdbcUtil.getResultSetByQuery(sqlCheck);
                        resultSetByQuery.next();
                        String sight_name = resultSetByQuery.getString("sight_name");
                        Assert.assertEquals(sight_name,sightName);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    //数据还原
                    String sqlReduction = "UPDATE t_sight set sight_name =  '"+sightNameInitial+"' where sight_id = '"+sight_id+"' ";
                    int i = JdbcUtil.executeUpdate(sqlReduction);
                    Assert.assertEquals(i,1);
                }else if(res == 130002){
                    String msg = jsonObject.getString("msg");
                    Assert.assertEquals(msg,"该情景名已存在，请重新录入");
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
    public Object[][] sightEditDatas(){
        Object[][] datas = {
                {"V人人是","V人人是编辑后名称"}   //初始情景名称、修改后情景名称
        };
        return datas;
    }


}
