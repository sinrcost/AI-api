package httpapi.AIkonwledge.questionBankWechat;

import com.alibaba.fastjson.JSON;
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

public class BankCategoryEdit {

    @Test(dataProvider = "editBankCategoryDatas")
    public void editBankCategory(String bankCategoryID,String bankCategoryName){
        //编辑题库类别初始地址
        String urlInitial = "http://192.168.3.68:2002/ai-web/ai/weChat/type";

        //编辑题库类别地址
        String url = GetCompleteUrl.getUrl(urlInitial, bankCategoryID);

        //设置请求头
        Map<String,String> header = new HashMap<String, String>();
        header.put("Authorization", TokenUtil.getToken());

        //设置请求参数
        JSONObject json = new JSONObject();
        json.put("name",bankCategoryName);
        String jsonStr = json.toString();

        //保存初始名称，根据类别ID获取初始名称
        String bankCategoryNameInitial = null;
        String sqlConserve = "SELECT * from t_type WHERE id = '"+bankCategoryID+"' ";
        ResultSet resultSetConserve = JdbcUtil.getResultSetByQuery(sqlConserve);
        try{
            while (resultSetConserve.next()){
                bankCategoryNameInitial= resultSetConserve.getString("name");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }


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
                    String sqlSelect = "SELECT * from t_type WHERE id = '"+bankCategoryID+"' ";
                    ResultSet resultSetByQuery = JdbcUtil.getResultSetByQuery(sqlSelect);
                    while (resultSetByQuery.next()){
                        String name = resultSetByQuery.getString("name");
                        Assert.assertEquals(name,bankCategoryName);

                    }
                    //数据还原
                    String sqlUpdate = "UPDATE t_type SET name = '"+bankCategoryNameInitial+"' WHERE id = '"+bankCategoryID+"' ";
                    int i = JdbcUtil.executeUpdate(sqlUpdate);
                    Assert.assertEquals(i,1);
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
    public Object[][] editBankCategoryDatas(){
        Object[][] datas = {
                {"5dd88fc214d8471768c6b42d","sun被编辑了"}   //需编辑的题库类别ID，修改后的名称
        };
        return datas;
    }

}
