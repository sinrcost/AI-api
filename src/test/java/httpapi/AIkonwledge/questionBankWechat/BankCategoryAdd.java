package httpapi.AIkonwledge.questionBankWechat;

import com.alibaba.fastjson.JSONObject;
import httpapi.utils.dbutils.JdbcUtil;
import httpapi.utils.httputils.HttpRequestsUtil;
import httpapi.utils.httputils.Response;
import httpapi.utils.httputils.TokenUtil;
import org.codehaus.jackson.annotate.JsonTypeInfo;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class BankCategoryAdd {

    @Test(dataProvider = "addBankCategoryDatas")
    public void addBankCategory(String BankCategoryName){
        //添加微信题库类别地址
        String urlAdd = "http://192.168.3.68:2002/ai-web/ai/weChat/type";

        //设置请求头
        Map<String,String> header = new HashMap<String, String>();
        header.put("Authorization", TokenUtil.getToken());

        //设置请求参数
        Map<String,String> params = new HashMap<String, String>();
        params.put("name",BankCategoryName);

        //执行请求
        try {
            Response response = HttpRequestsUtil.doPostWithUrlParams(urlAdd, header, params);
            String responseStr = response.getResponseStr();
            System.out.println(responseStr);

            if(response.getStatusCode() == 200){
                JSONObject jsonObject = JSONObject.parseObject(responseStr);
                if(jsonObject.getIntValue("res") == 0){
                    Assert.assertEquals(jsonObject.getString("msg"),"success");
                    String bankCategoryID = jsonObject.getString("data");

                    //数据验证
                    String sqlSelect = "SELECT * from t_type WHERE id = '"+bankCategoryID+"' ";
                    ResultSet resultSetByQuery = JdbcUtil.getResultSetByQuery(sqlSelect);
                    if (resultSetByQuery.next()){
                        String name = resultSetByQuery.getString("name");
                        Assert.assertEquals(name,BankCategoryName);
                    }else {
                        Assert.assertEquals("添加数据失败","添加数据成功");
                    }
                    //数据还原
                    String sqlDelete = "DELETE from t_type WHERE id = '"+bankCategoryID+"' ";
                    int i = JdbcUtil.executeUpdate(sqlDelete);
                    Assert.assertEquals(i,1);
                }else {
                    Assert.assertEquals("添加数据失败","添加数据成功");
                }

            }else {
                Assert.assertEquals("接口返回码不是200","接口返回码应该是200");
                System.out.println(response.getStatusCode());
            }

        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    @DataProvider
    public Object[][] addBankCategoryDatas(){
        Object[][] datas = {
                {"接口新增图库类别"},
                {"xfsun微信题库"}    //已经存在的类别名称

        };

        return datas;
    }


}
