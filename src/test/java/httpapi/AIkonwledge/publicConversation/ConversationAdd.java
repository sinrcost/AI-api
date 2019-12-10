package httpapi.AIkonwledge.publicConversation;
/**
 * 新增话术
 */
import net.sf.json.JSONArray;
//import net.sf.json.JSONObject;
//import com.alibaba.fastjson.JSONArray;
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

public class ConversationAdd {

    @Test(dataProvider = "conversationDatas")
    public void conversation(String conversationName,String conversationDesc,String breakFlag,int conversationType,JSONArray array){
        //添加话术地址
        String url = "http://192.168.3.68:2002/ai-web/ai/conversation";

        //设置请求头
        Map<String,String> header = new HashMap<String, String>();
        header.put("Authorization", TokenUtil.getToken());

        //设置请求参数
        JSONObject json = new JSONObject();
        json.put("conversationName",conversationName);
        json.put("conversationDesc",conversationDesc);
        json.put("breakFlag",breakFlag);
        json.put("conversationType",conversationType);
        json.put("conversationJson",array);
        String jsonStr = json.toString();

        //执行请求
        try {
            Response response = HttpRequestsUtil.doPostWithString(url, header, jsonStr);
            String responseStr = response.getResponseStr();
            System.out.println(responseStr);

            if(response.getStatusCode() == 200){
                JSONObject jsonObject = JSONObject.parseObject(responseStr);
                int res = jsonObject.getIntValue("res");
                if(res == 0){
                    Assert.assertEquals(jsonObject.getString("msg"),"success");
                    String id = jsonObject.getString("data");

                    //还原数据
                    String sql = "DELETE from t_conversation WHERE id = '"+id+"' ";
                    int i = JdbcUtil.executeUpdate(sql);
                    Assert.assertEquals(i,1);

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
    public Object[][] conversationDatas(){

        String conversationJson = "[{text: '话术文字4'}," +
                "{column: 'patientName'}, " +
                "{column: 'age'}," +
                "{fileName:'104.mp3',isPlay:'false',url:'blob:http://192.168.3.68:3001/a2a32ad6-7f2d-4852-af61-49babc14e056',voice:'5ddcecfd14d8473c20a39889',voiceAddress:'http://192.168.3.19:9084/cc-voice/resample_5ddcecfd14d8473c20a39889.mp3'}]";
        JSONArray jsonArray = JSONArray.fromObject(conversationJson);


        Object[][] datas = {
                {"话术名称4","话术描述4","1",3,jsonArray}  //话术名称、话术描述、是否支持打断、话术类型、话术内容
        };
        return datas;
    }

}
