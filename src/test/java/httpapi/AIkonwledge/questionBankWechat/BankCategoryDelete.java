package httpapi.AIkonwledge.questionBankWechat;

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
import java.util.List;
import java.util.Map;

public class BankCategoryDelete {

    @Test(dataProvider = "deleteBankCategoryDatas")
    public void deleteBankCategory(String bankCategoryID){
        //删除题库类别初始地址
        String urlInitial = "http://192.168.3.68:2002/ai-web/ai/weChat/type";

        //删除题库类别地址
        String url = GetCompleteUrl.getUrl(urlInitial, bankCategoryID);

        //设置请求头
        Map<String,String> header = new HashMap<String, String>();
        header.put("Authorization", TokenUtil.getToken());

        //保存需要删除的数据
        String sqlConserve = "SELECT * from t_type WHERE id = '"+bankCategoryID+"' ";
        ResultSet resultSetByQuery =  JdbcUtil.getResultSetByQuery(sqlConserve);
        String id = null;
        String name = null;
        String editor = null;
        String create_time = null;
        String parent = null;
        try {
            List<Map<String, Object>> resultSetList = JdbcUtil.handleResultSet(resultSetByQuery);
            for(Map<String,Object> map : resultSetList){
                if( map.get("id") != null){
                    id = map.get("id").toString();
                }
                if( map.get("name") != null){
                    name = map.get("name").toString();
                }
                if( map.get("editor") != null){
                    editor = map.get("editor").toString();
                }
                if( map.get("create_time") != null){
                    create_time = map.get("create_time").toString();
                }
                if( map.get("parent") != null){
                    parent = map.get("parent").toString();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        //执行请求
        try {
            Response response = HttpRequestsUtil.delete(url, header);
            String responseStr = response.getResponseStr();
            System.out.println(responseStr);

            if(response.getStatusCode() == 200){
                JSONObject jsonObject = JSONObject.parseObject(responseStr);
                if(jsonObject.getIntValue("res") == 0){
                    Assert.assertEquals(jsonObject.getString("msg"),"success");

                    //数据验证
                    String sqlVerification = "SELECT * from t_type WHERE id = '"+bankCategoryID+"' ";
                    ResultSet resultSetByQuery1 = JdbcUtil.getResultSetByQuery(sqlVerification);
                    if(resultSetByQuery1.next()){
                        Assert.assertEquals("删除数据失败","删除数据成功");
                    }else {
                        Assert.assertEquals("删除数据成功","删除数据成功");

                        //数据还原
                        String sqlReduction = "INSERT INTO t_type VALUES('"+id+"','"+name+"','"+editor+"','"+create_time+"','"+parent+"')";
                        int i = JdbcUtil.executeUpdate(sqlReduction);
                        Assert.assertEquals(i,1);
                    }

                }else if(jsonObject.getIntValue("res") == 130001){
                    Assert.assertEquals(jsonObject.getString("msg"),"该类别下还有题目,请先删除全部题目后再删除类别");
                }else {
                    Assert.assertEquals("接口响应返回的res不是0也不是130001","接口响应返回的res应该是0或130001");
                }

            }else {
                Assert.assertEquals("接口返回码不是200","接口返回码应该是200");
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    @DataProvider
    public Object[][] deleteBankCategoryDatas(){
        Object[][] datas = {
                {"5bea66b15c67871624b8cc74"},  //有微信题目的类别ID
                {"5c36b20f8c79ac08f41719a5"}   //无微信题目的类别ID
        };
        return datas;
    }

}
