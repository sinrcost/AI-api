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
import java.net.URISyntaxException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class SightAdd {

    @Test(dataProvider = "sightNameDatas")
    public void sightAdd(String sightOfName){
        //新增情景地址
        String url = "http://192.168.3.68:2002/ai-web/ai/sight/sight";

        //设置请求头
        Map<String,String> header = new HashMap<String, String>();
        header.put("Authorization", TokenUtil.getToken());

        //设置请求参数
        Map<String,String> params = new HashMap<String, String>();
        params.put("sightName",sightOfName);

        //执行请求
        try {
            Response response = HttpRequestsUtil.doPostWithUrlParams(url, header, params);
            String responseStr = response.getResponseStr();
            System.out.println(responseStr);
            if(response.getStatusCode() == 200){
                JSONObject jsonObject = JSONObject.parseObject(responseStr);
                int res = jsonObject.getIntValue("res");
                if(res == 0){
                    Assert.assertEquals(jsonObject.getString("msg"),"success");
                    try {
                        //数据验证
                        String sqlCheck = "SELECT * from t_sight where sight_name = '"+sightOfName+"' ";
                        ResultSet resultSetByQuery = JdbcUtil.getResultSetByQuery(sqlCheck);
                        while (resultSetByQuery.next()){
                            String sight_name = resultSetByQuery.getString("sight_name");
                            Assert.assertEquals(sight_name,sightOfName);
                        }
                        //数据还原
                        String sqlReduction = "DELETE  from t_sight where sight_name = '"+sightOfName+"' ";
                        int i = JdbcUtil.executeUpdate(sqlReduction);
                        Assert.assertEquals(i,1);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }else if(res == 130002){
                    Assert.assertEquals(jsonObject.getString("msg"),"该情景名已存在，请重新录入");
                }else {
                    Assert.assertEquals("res、msg校验失败","res，msg没有预期值");
                }

            }else {
                Assert.assertEquals("接口返回状态码不是200","接口返回状态码应为200");
            }


        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @DataProvider
    public Object[][] sightNameDatas(){

        Object[][] datas = {
                {"情景AI"},
                {"测试"}  //已存在的情景名称
        };
        return datas;

    }

}
