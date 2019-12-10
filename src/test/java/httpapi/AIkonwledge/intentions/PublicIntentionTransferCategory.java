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
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class PublicIntentionTransferCategory {

    @Test(dataProvider = "transferPublicIntentionDatas")
    public void transferPublicIntention(String intentionCategoryInitial,String intentionName,String intentionCategoryDestination){

        //转移意图初始地址
        String urlInitial = "http://192.168.3.68:2002/ai-web/ai/intentions/transform";

        //设置请求头
        Map<String,String> header = new HashMap<String,String>();
        header.put("Authorization", TokenUtil.getToken());

        //根据意图原类别名称获取原类别id
        String sqlCategory1 = "SELECT * from t_intention_category where intention_type_name = '"+intentionCategoryInitial+"' ";
        ResultSet resultSetByQuery1 = JdbcUtil.getResultSetByQuery(sqlCategory1);
        String categoryIDInitial = null;
        try {
            resultSetByQuery1.next();
            categoryIDInitial = resultSetByQuery1.getString("id");
        } catch (SQLException e) {
            e.printStackTrace();
        }


        //根据意图目的地类别名称获取目的地类别id
        String sqlCategory2 = "SELECT * from t_intention_category where intention_type_name = '"+intentionCategoryDestination+"' ";
        ResultSet resultSetByQuery2 = JdbcUtil.getResultSetByQuery(sqlCategory2);
        String categoryIDDestination = null;
        try {
            resultSetByQuery2.next();
            categoryIDDestination = resultSetByQuery2.getString("id");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //根据意图名称获取意图id
        String sqlIntention = "SELECT * from t_intention where intention_name = '"+intentionName+"' AND intention_category = '"+categoryIDInitial+"'  AND public_intention_flag = \"1\" AND is_delete = \"0\" ";
        ResultSet resultSetByQuery3 = JdbcUtil.getResultSetByQuery(sqlIntention);
        String intentionId = null;
        try {
            if(resultSetByQuery3.next()){
                intentionId = resultSetByQuery3.getString("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //设置请求参数
        Map<String,String> param = new HashMap<String,String>();
        param.put("intentionIds", intentionId);
        param.put("categoryId",categoryIDDestination);

        //执行请求
        try {
            Response response = HttpRequestsUtil.doGetWithParams(urlInitial, header, param);
            String responseStr = response.getResponseStr();
            System.out.println(responseStr);
            if(response.getStatusCode() == 200){
                JSONObject jsonObject = JSONObject.parseObject(responseStr);
                if(jsonObject.getIntValue("res") == 0){
                    String msg = jsonObject.getString("msg");
                    Assert.assertEquals(msg,"success");
                    //数据验证
                    String sqlCheck = "SELECT * from t_intention where id = '"+intentionId+"' ";
                    ResultSet resultSetByQuery =  JdbcUtil.getResultSetByQuery(sqlCheck);
                    if (resultSetByQuery.next()){
                        String intention_category = resultSetByQuery.getString("intention_category");
                        Assert.assertEquals(intention_category,categoryIDDestination);
                    }else {
                        Assert.assertEquals("找不到意图","应可以查询到意图");
                    }

                    //数据还原
                    String sqlReduction = "UPDATE t_intention SET intention_category = '"+categoryIDInitial+"' WHERE id = '"+intentionId+"' ";
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
    public Object[][] transferPublicIntentionDatas(){
        Object[][] datas = {
                {"xfsun预约挂号意图","预约挂号","helloxfsun"}  //意图原类别、需转移的意图名称、意图类别目的地名称
        };
        return datas;
    }


}
