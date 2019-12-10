package httpapi.AIkonwledge.intentions;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import httpapi.utils.commons.ExcelUtil;
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
import java.net.URISyntaxException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class PublicIntentionCreateEditAddkeywordsDelete {

    @Test(dataProvider = "intentionsDatas")
    public void creatIntention(String jsonStr){
        //新增公共意图地址
        ResourceBundle bundle = ResourceBundle.getBundle("host");
        String hostUrl = bundle.getString("test.url");
        String url = hostUrl+bundle.getString("publicIntentionCreate.uri");

        //设置请求头
        Map<String,String> header = new HashMap<String,String>();
        header.put("Authorization", TokenUtil.getToken());

        /**
        //根据意图类别名称获取id
        String id = GetIdByName.GetId(categoryName);

        //设置请求参数
        JSONObject json = new JSONObject();
        json.put("intentionName", intentionName);
        json.put("intentionCategory",id);
        String jsonStr = json.toString();
*/
        //执行请求
        try {
            Response response = HttpRequestsUtil.doPostWithString(url, header, jsonStr);
            String responseStr = response.getResponseStr();
            System.out.println(responseStr);

            if(response.getStatusCode() == 200){
                JSONObject jsonObject = JSONObject.parseObject(responseStr);
                int res = jsonObject.getIntValue("res");
                String intentionID = jsonObject.getString("data");

                if(res == 0){
                    Assert.assertEquals(jsonObject.getString("msg"),"success");
                    ExcelUtil.writeExcel(11,10,intentionID);

            /**        try {
                        //数据库验证数据是否新增成功
                        String sqlCheck = "SELECT * from t_intention ORDER BY create_time DESC limit 0,1";
                        ResultSet resultSetByQuery = JdbcUtil.getResultSetByQuery(sqlCheck);
                        resultSetByQuery.next();
                        String intention_name = resultSetByQuery.getString("intention_name");
                        Assert.assertEquals(intention_name,intentionName);
                        //数据清理
                        String sqlReduction = "DELETE FROM t_intention ORDER BY create_time DESC limit 1";
                        int i = JdbcUtil.executeUpdate(sqlReduction);
                        Assert.assertEquals(i,1);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
*/
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
    public Object[][] intentionsDatas(){

        Object[][] datas = ExcelUtil.readExcel(11,11,7);
           /**     {
                // {"同意",""},
                {"同意","newIntention"}  //意图类别、意图名称  某意图类别下创建新的意图名称

        };*/
        return datas;

    }

    @Test(dataProvider = "editIntentionDatas",dependsOnMethods = "creatIntention")
    public void editIntention(String intentionName){
        //编辑公共意图初始地址
        //String urlinitial = "http://192.168.3.68:2002/ai-web/ai/intentions/intentionName/5d2c44b18c79ac225aed6ede?name=%E8%B0%83%E6%9F%A5xfsun";
        ResourceBundle bundle = ResourceBundle.getBundle("host");
        String hostUrl = bundle.getString("test.url");
        String urlinitial = hostUrl+bundle.getString("publicIntentionEdit.uri");

        //获取指定的公共意图的id
        Object[][] objects = ExcelUtil.readExcel(11,11,10);
        String intentionId = (String) objects[0][0];

        //编辑意图地址
        String url = GetCompleteUrl.getUrl(urlinitial, intentionId);

        //设置请求头
        Map<String,String> header = new HashMap<String,String>();
        header.put("Authorization", TokenUtil.getToken());

        //设置请求参数
        Map<String,String> params = new HashMap<String, String>();
        params.put("name",intentionName);

        //执行请求
        try {
            Response response = HttpRequestsUtil.doPutWithUrlParams(url, header, params);
            String responseStr = response.getResponseStr();
            System.out.println(responseStr);

            if(response.getStatusCode() == 200){
                JSONObject jsonObject = JSON.parseObject(responseStr);
                int res = jsonObject.getIntValue("res");
                if(res == 0){
                    Assert.assertEquals(jsonObject.getString("msg"),"success");

                    /**
                     try {
                     //数据验证
                     String sqlCheck = "SELECT * FROM t_intention where id = '"+intentionId+"' ";
                     ResultSet resultSetByQuery = JdbcUtil.getResultSetByQuery(sqlCheck);
                     resultSetByQuery.next();
                     String intention_name = resultSetByQuery.getString("intention_name");
                     Assert.assertEquals(intention_name,intentionName);
                     //还原数据
                     String sqlReduction = "UPDATE t_intention SET intention_name = '"+initialName+"' where id = '"+intentionId+"' ";
                     int i = JdbcUtil.executeUpdate(sqlReduction);
                     Assert.assertEquals(i,1);

                     } catch (SQLException e) {
                     e.printStackTrace();
                     }
                     */
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
    public Object[][] editIntentionDatas(){
        Object[][] datas = ExcelUtil.readExcel(12,12,7);
        /**
         {
         {"helloxfsun","调查","调查xfsun"}  //意图类别、意图初始名称、修改后意图名称
         };
         */
        return datas;
    }

    @Test(dataProvider = "keywordsDatas",dependsOnMethods = "creatIntention")
    public void intentionKeywordsAdd(String jsonStr){
        //添加公共意图初始地址
        ResourceBundle bundle = ResourceBundle.getBundle("host");
        String hostUrl = bundle.getString("test.url");
        String urlInitial = hostUrl+bundle.getString("addIntentionKeywords.uri");

        //获取意图ID
        Object[][] objects = ExcelUtil.readExcel(11, 11, 10);
        String intentionID = (String) objects[0][0];
/**
 //根据意图类别名称获取意图类别id
 String categoeyId = GetIdByName.GetId(categoryName);

 //根据意图id获取指定的公共意图的名称、意图关键词、意图黑名单
 String sqlObtainID = "SELECT * from t_intention WHERE id = '"+intentionID+"' ";
 ResultSet resultSetByQuery = JdbcUtil.getResultSetByQuery(sqlObtainID);
 String intentionName = null;
 String intentionContentInitial = null;
 String intentionBlackContentInitial = null;
 try {
 if(resultSetByQuery.next()){
 intentionName = resultSetByQuery.getString("intention_name");
 intentionContentInitial = resultSetByQuery.getString("intention_content");
 intentionBlackContentInitial = resultSetByQuery.getString("black_list");
 }else {
 Assert.assertEquals("找不到该id的意图","应获取到意图名称");
 }
 } catch (SQLException e) {
 e.printStackTrace();
 }
 */
        //添加公共意图关键词地址
        String url = GetCompleteUrl.getUrl(urlInitial,intentionID);

        //设置请求头
        Map<String, String> header = new HashMap<String, String>();
        header.put("Authorization", TokenUtil.getToken());
/**
 //设置请求参数
 JSONObject json = new JSONObject();
 json.put("intentionBlackContent",intentionBlackContent);
 json.put("intentionCategory", categoeyId);
 json.put("intentionContent", intentionContent);
 json.put("intentionName", intentionName);
 json.put("publicIntentionFlag","true");

 String jsonStr = json.toString();
 */
        //执行请求
        try {
            Response response = HttpRequestsUtil.doPutWithString(url, header, jsonStr);
            String responseStr = response.getResponseStr();
            System.out.println(responseStr);

            if(response.getStatusCode() == 200){
                JSONObject jsonObject = JSON.parseObject(responseStr);
                int res = jsonObject.getIntValue("res");
                if(res == 0){
                    Assert.assertEquals(jsonObject.getString("msg"),"success");
                    /**   //数据验证
                     String sql = "SELECT intention_content from t_intention where id = '"+intentionID+"' ";
                     ResultSet resultSet = JdbcUtil.getResultSetByQuery(sql);
                     try {
                     String s = JdbcUtil.formatRsToString(resultSet);

                     String[] sArr = s.split(";");
                     String[] intentionContentArr = intentionContent.split(";");
                     Assert.assertEqualsNoOrder(sArr,intentionContentArr);
                     } catch (Exception e) {
                     e.printStackTrace();
                     }

                     //数据还原
                     String sqlReduction = "UPDATE t_intention SET intention_content = '"+intentionContentInitial+"', black_list = '"+intentionBlackContentInitial+"' WHERE id = '"+intentionID+"' ";
                     int i = JdbcUtil.executeUpdate(sqlReduction);
                     Assert.assertEquals(i,1);
                     */

                }else {
                    Assert.assertEquals("res、msg校验失败","res应为0，msg应为success");
                }

            }else {
                Assert.assertEquals("接口返回状态码不是200","接口返回状态码应为200");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @DataProvider
    public Object[][] keywordsDatas(){

        Object[][] datas = ExcelUtil.readExcel(14,14,7);
        /**
         {
         {"helloxfsun","5c47cbc78c79ac573385edfb","可以的;你说吧(0.3);jhhvdhvh;sxf","黑名单的关键词;"}
         //意图类别、   意图ID、                   意图关键词、                      意图黑名单关键词
         };
         */
        return datas;

    }

    @Test(dependsOnMethods = "intentionKeywordsAdd")
    public void deleteIntention(){
        //删除公共意图初始地址
        ResourceBundle bundle = ResourceBundle.getBundle("host");
        String hostUrl = bundle.getString("test.url");
        String urlinitial = hostUrl+bundle.getString("publicIntentionDelete.uri");

        //获取指定的公共意图的id
        Object[][] objects = ExcelUtil.readExcel(11, 11, 10);
        String intentionId = (String) objects[0][0];

        //删除意图地址
        String url = GetCompleteUrl.getUrl(urlinitial, intentionId);

        //设置请求头
        Map<String,String> header = new HashMap<String, String>();
        header.put("Authorization", TokenUtil.getToken());

        //执行请求
        try {
            Response response = HttpRequestsUtil.delete(url, header);
            String responseStr = response.getResponseStr();
            System.out.println(responseStr);

            if(response.getStatusCode() == 200){
                JSONObject jsonObject = JSON.parseObject(responseStr);
                int res = jsonObject.getIntValue("res");
                if(res  == 0){
                    Assert.assertEquals(jsonObject.getString("msg"),"success");
                    /**
                     try {
                     //数据验证
                     String sqlCheck = "SELECT * FROM t_intention where id = '"+intentionId+"' ";
                     ResultSet resultSetByQuery = JdbcUtil.getResultSetByQuery(sqlCheck);
                     resultSetByQuery.next();
                     int is_delete = resultSetByQuery.getInt("is_delete");
                     Assert.assertEquals(is_delete,1);
                     //数据还原
                     String sql = " UPDATE t_intention SET is_delete = '0' where id='"+intentionId+"' ";
                     int i = JdbcUtil.executeUpdate(sql);
                     Assert.assertEquals(i,1);
                     } catch (SQLException e) {
                     e.printStackTrace();
                     }
                     */
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

}
