package httpapi.AIkonwledge.intentions;

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
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * 意图黑名单字典添加关键词
 */
public class BlackDictionary {

    @Test(dataProvider = "blackDictionaryDatas")
    public void addBlackDictionaryIntention(String words){

        //添加意图黑名单字典地址
        ResourceBundle bundle = ResourceBundle.getBundle("host");
        String hostUrl = bundle.getString("test.url");
        String url = hostUrl+bundle.getString("blackDictionary.uri");
        System.out.println("jhj");
        //设置请求头
        Map<String, String> header = new HashMap<String, String>();
        header.put("Authorization", TokenUtil.getToken());

        //设置请求参数
        JSONObject json = new JSONObject();
        json.put("words",words);
        String jsonStr = json.toString();

        //保存初始黑名单字典的内容
        String sqlObtain = "SELECT intention_content from t_intention WHERE id = '1' ";
        ResultSet resultSetByQuery = JdbcUtil.getResultSetByQuery(sqlObtain);
        String blackDictionaryContentInitial = null;
        try {
            blackDictionaryContentInitial = JdbcUtil.formatRsToString(resultSetByQuery);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //执行请求
        try {
            Response response = HttpRequestsUtil.doPostWithString(url, header, jsonStr);
            String responseStr = response.getResponseStr();
            System.out.println(responseStr);

            if(response.getStatusCode() == 200){
                JSONObject jsonObject = JSONObject.parseObject(responseStr);
                int res = jsonObject.getIntValue("res");
                if(res == 0){
                    Assert.assertEquals(jsonObject.getString("msg"),"");
                    //数据验证
                    try {
                        String sql  = "SELECT intention_content from t_intention WHERE id = '1' ";
                        ResultSet resultSet = JdbcUtil.getResultSetByQuery(sql);
                        String s = JdbcUtil.formatRsToString(resultSet);
                        Assert.assertEquals(s,words);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    //数据还原
                    String sqlReduction = "UPDATE t_intention SET intention_content = '"+blackDictionaryContentInitial+"' WHERE id = '1' ";
                    int i = JdbcUtil.executeUpdate(sqlReduction);
                    Assert.assertEquals(i,1);

                }else {
                    Assert.assertEquals("res、msg校验失败","res应为0，msg应为空");
                }

            }else {
                Assert.assertEquals("接口返回状态码不是200","接口返回状态码应为200");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @DataProvider
    public Object[][] blackDictionaryDatas(){

        Object[][] datas = {
                {"这是黑名单字典;hun456789;xfsun"}
        };
        return datas;

    }
}
