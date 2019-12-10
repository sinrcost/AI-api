package httpapi.AIkonwledge.intentions;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import httpapi.utils.commons.ExcelUtil;
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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * 新增、编辑、删除公共意图类别
 */
public class PublicIntentionCategoryCreateEditDelete {

    @Test(dataProvider = "categoryDatas")
    public void createIntentionCategory(String categoryName){
        //新增意图类别地址
        ResourceBundle bundle = ResourceBundle.getBundle("host");
        String hostUrl = bundle.getString("test.url");
        String url = hostUrl+bundle.getString("publicIntentionCategoryCreate.uri");

        //设置请求头
        Map<String,String> header = new HashMap<String,String>();
        header.put("Authorization", TokenUtil.getToken());

        //设置请求参数
        Map<String,String> param = new HashMap<String,String>();
        param.put("intentionTypeName", categoryName);
        System.out.println("类别名称为："+categoryName);

        //执行请求
        try {
            Response response = HttpRequestsUtil.doPostWithUrlParams(url, header, param);
            String responseStr = response.getResponseStr();
            System.out.println(responseStr);

            if(response.getStatusCode() == 200){
                JSONObject jsonObject = JSON.parseObject(responseStr);
                int res = jsonObject.getIntValue("res");
                String data = jsonObject.getString("data");


                if(res == 0){
                    Assert.assertEquals(jsonObject.getString("msg"),"success");
                    //数据库验证数据是否新增成功
                    String sqlQuery = "select * from t_intention_category ORDER BY create_time DESC limit 0,1";
                    ResultSet resultSetByQuery = JdbcUtil.getResultSetByQuery(sqlQuery);
                    try {
                        resultSetByQuery.next();
                        String intention_type_name = resultSetByQuery.getString("intention_type_name");
                        Assert.assertEquals(intention_type_name,categoryName);
                        ExcelUtil.writeExcel(5,10,data);
                        ExcelUtil.writeExcel(9,7,data);
                        //数据清理
                       /**
                        String sqlDelete = "DELETE FROM t_intention_category ORDER BY create_time DESC limit 1";
                        int i = JdbcUtil.executeUpdate(sqlDelete);
                        Assert.assertEquals(i,1);
                        */
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                }else if(res == 130001){
                    Assert.assertEquals(jsonObject.getString("msg"),"意图类别已存在");
                    String sql = "select * from t_intention_category where intention_type_name = ?";
                    ResultSet resultSet = JdbcUtil.getResultSetByTransPara(sql, categoryName);
                    try {
                        if(resultSet.next()){
                            String intention_type_name = resultSet.getString("intention_type_name");
                            System.out.println(intention_type_name);
                            Assert.assertEquals(intention_type_name,categoryName);
                        }else{
                            Assert.assertEquals(null,categoryName);
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
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
    public Object[][] categoryDatas(){

        Object[][] datas = ExcelUtil.readExcel(5,6,7);
        return datas;

    }


    @Test(dataProvider = "categoryDatasEdit",dependsOnMethods = "createIntentionCategory")
    public void editIntentionCategory(String jsonStr){

        //编辑意图类别初始地址
        ResourceBundle bundle = ResourceBundle.getBundle("host");
        String hostUrl = bundle.getString("test.url");
        String urlinitial = hostUrl+bundle.getString("publicIntentionCategoryEdit.uri");

        //根据类别名称获取id
        Object[][] objects = ExcelUtil.readExcel(5, 5, 10);
        String id = (String) objects[0][0];

        //String id = GetIdByName.GetId(initialName);

        //编辑意图类别地址
        String url = GetCompleteUrl.getUrl(urlinitial,id);

        //设置请求头
        Map<String,String> header = new HashMap<String,String>();
        header.put("Authorization", TokenUtil.getToken());

        //设置请求参数
        /**
         JSONObject json = new JSONObject();
         json.put("intentionTypeName", categoryName);
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
                    /**
                     try {
                     //数据验证
                     String sqlCheck = "select * from t_intention_category where id = '"+id+"' ";
                     ResultSet resultSetByQuery = JdbcUtil.getResultSetByQuery(sqlCheck);
                     resultSetByQuery.next();
                     String intention_type_name = resultSetByQuery.getString("intention_type_name");
                     Assert.assertEquals(intention_type_name,categoryName);

                     //还原数据

                     String sql = "UPDATE t_intention_category SET intention_type_name = '"+initialName+"'  where id = '"+id+"'";
                     int i = JdbcUtil.executeUpdate(sql);
                     Assert.assertEquals(i,1);


                     } catch (SQLException e) {
                     e.printStackTrace();
                     }
                     */
                }else if (res == 130001){
                    Assert.assertEquals(jsonObject.getString("msg"),"意图名称不能重复");
                    /**
                     String sql = "select * from t_intention_category where intention_type_name = ?";
                     ResultSet resultSet = JdbcUtil.getResultSetByTransPara(sql, categoryName);
                     try {
                     if(resultSet.next()){
                     String intention_type_name = resultSet.getString("intention_type_name");
                     Assert.assertEquals(intention_type_name,categoryName);
                     }else {
                     Assert.assertEquals(null,categoryName);
                     }
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
    public Object[][] categoryDatasEdit(){
        Object[][] datas = ExcelUtil.readExcel(7,8,7);
        /**
         {
         {"Idea123","Idea456"},  //正常修改类别名称
         {"黑名单","江湖画扇"},  //修改为已经存在的类别名称
         {"非人非本","非人非本"} //修改为原名称一样
         };*/
        return datas;
    }

    @Test(dataProvider = "deleteIntentionCategoryDatas",dependsOnMethods = "editIntentionCategory")
    public void deleteIntentionCategory(String categoryID){

        //删除意图类别初始地址
        ResourceBundle bundle = ResourceBundle.getBundle("host");
        String hostUrl = bundle.getString("test.url");
        String urlinitial = hostUrl+bundle.getString("publicIntentionCategoryDelete.uri");


        //删除意图类别地址
        String url = GetCompleteUrl.getUrl(urlinitial, categoryID);

        //设置请求头
        Map<String,String>  header = new HashMap<String, String>();
        header.put("Authorization", TokenUtil.getToken());

        //执行请求
        try {
            Response response = HttpRequestsUtil.delete(url, header);
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
                     String sql = "select * from t_intention_category where intention_type_name = '"+categoryName+"' ";
                     ResultSet resultSetByQuery = JdbcUtil.getResultSetByQuery(sql);
                     resultSetByQuery.next();
                     int is_delete = resultSetByQuery.getInt("is_delete");
                     Assert.assertEquals(is_delete,1);
                     //还原数据
                     String sqlReduction = " UPDATE t_intention_category SET is_delete = '0' where id='"+id+"' ";
                     int i = JdbcUtil.executeUpdate(sqlReduction);
                     Assert.assertEquals(i,1);
                     } catch (SQLException e) {
                     e.printStackTrace();
                     }
                     */
                }else if(res == 130001){
                    Assert.assertEquals(jsonObject.getString("msg"),"该意图类别下还有意图,请先删除全部意图后再删除类别");
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
    public Object[][] deleteIntentionCategoryDatas(){
        Object[][] datas = ExcelUtil.readExcel(9,10,7);
        return datas;
    }

}
