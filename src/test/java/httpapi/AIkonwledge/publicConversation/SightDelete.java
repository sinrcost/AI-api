package httpapi.AIkonwledge.publicConversation;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;
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
import java.util.List;
import java.util.Map;

/**
 * 删除场景
 */
public class SightDelete {

    @Test(dataProvider = "sightDeleteDatas")
    public void sightDelete(String sightName){
        //场景删除初始地址
        String urlinitial = "http://192.168.3.68:2002/ai-web/ai/sight/sight";
        //根据场景名称获取场景id
        String sqlSelect = "select * from t_sight where sight_name = '"+sightName+"' ";
        String sight_id = GetIdByName.getIdByName(sqlSelect, "sight_id");
        String url = GetCompleteUrl.getUrl(urlinitial, sight_id);

        //设置请求头
        Map<String,String> header = new HashMap<String, String>();
        header.put("Authorization", TokenUtil.getToken());

        //保存需要删除的数据
        String sqlConserve = "SELECT * from t_sight WHERE sight_id = '"+sight_id+"' ";
        ResultSet resultSetByQueryConserve =  JdbcUtil.getResultSetByQuery(sqlConserve);
        String sight_name = null;
        String sight_type = "";
        String editor = null;
        String create_time = null;
        String update_time = null;
        try {
            List<Map<String, Object>> resultSetList = JdbcUtil.handleResultSet(resultSetByQueryConserve);
            for(Map<String,Object> map : resultSetList){
                if( map.get("sight_name") != null){
                    sight_name = map.get("sight_name").toString();
                }
                if( map.get("sight_type") != null){
                    sight_type = map.get("sight_type").toString();
                }
                if( map.get("editor") != null){
                    editor = map.get("editor").toString();
                }
                if( map.get("create_time") != null){
                    create_time = map.get("create_time").toString();
                }
                if( map.get("update_time") != null){
                    update_time = map.get("update_time").toString();
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
                int res = jsonObject.getIntValue("res");
                if(res == 0){
                    Assert.assertEquals(jsonObject.getString("msg"),"success");
                    //数据验证
                    String sqlCheck = "SELECT * from t_sight where sight_name = '"+sightName+"' ";
                    ResultSet resultSetByQuery = JdbcUtil.getResultSetByQuery(sqlCheck);
                    try {
                        if(resultSetByQuery.next()){
                            Assert.assertEquals("情景没有被删除","情景已被成功删除");
                        }else {
                            Assert.assertEquals("情景已被成功删除","情景已被成功删除");
                            //数据还原
                            String insertSql = "INSERT INTO t_sight  VALUES ('"+sight_id+"','"+sight_name+"','"+sight_type+"','"+editor+"','"+create_time+"','"+update_time+"',0) ";
                            int i = JdbcUtil.executeUpdate(insertSql);
                            Assert.assertEquals(i,1);
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                }else if(res == 130002){
                    Assert.assertEquals(jsonObject.getString("msg"),"请先解除正在使用的情景绑定关系");
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
    public Object[][] sightDeleteDatas(){
        Object[][] datas = {
                {"接口测试情景"},    //无情景话术的情景
                {"的的额飞飞2"}      //有情景话术的情景
        };
        return datas;
    }


}
