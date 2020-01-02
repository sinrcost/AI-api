package httpapi.scenepage;

import com.alibaba.fastjson.JSONObject;
import httpapi.utils.commons.ExcelUtil;
import httpapi.utils.httputils.HttpRequestsUtil;
import httpapi.utils.httputils.Response;
import httpapi.utils.httputils.TokenUtil;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class SceneAdd {

    @Test(dataProvider = "addSceneDatas")
    public void addScene(String jsonStr){

        //新增场景地址
        ResourceBundle bundle = ResourceBundle.getBundle("host");
        String hostUrl = bundle.getString("test.url");
        String url = hostUrl+bundle.getString("sceneAdd.uri");

        //设置请求头
        Map<String,String> header = new HashMap<String, String>();
        header.put("Authorization", TokenUtil.getToken());

        //执行请求
        try {
            Response response = HttpRequestsUtil.doPostWithString(url, header, jsonStr);
            String responseStr = response.getResponseStr();
            System.out.println(responseStr);
            if(response.getStatusCode() == 200){
                JSONObject jsonObject = JSONObject.parseObject(responseStr);
                int res = jsonObject.getIntValue("res");
                if(res == 0){
                    String msg = jsonObject.getString("msg");
                    Assert.assertEquals(msg,"success");
                    String data = jsonObject.getString("data");
                    ExcelUtil.writeExcel(15,10,data);
                    deleteScene();

                }else if(res == 130001){
                    SoftAssert sa = new SoftAssert();    //软断言
                    sa.assertEquals(jsonObject.getString("msg"),"表单已经配置了场景");
                    sa.assertEquals(jsonObject.getString("msg"),"参数不能为空");
                }else if(res == 130003){
                    Assert.assertEquals(jsonObject.getString("msg"),"场景名称过长");
                }else {
                    Assert.assertEquals("res、msg校验失败","res、msg没有预期值");
                }

            }else {
                Assert.assertEquals("接口返回状态码不是200","接口返回状态码应为200");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @DataProvider
    public Object[][] addSceneDatas(){
        Object[][] datas = ExcelUtil.readExcel(15,15,7);
        return datas;
    }

  //  @Test(dependsOnMethods = "addScene")
    public void deleteScene(){
        //删除场景地址
        ResourceBundle bundle = ResourceBundle.getBundle("host");
        String hostUrl = bundle.getString("test.url");
        String url = hostUrl+bundle.getString("sceneDelete.uri");

        //获取场景ID
        Object[][] objects = ExcelUtil.readExcel(15, 15, 10);
        String sceneID = (String) objects[0][0];
        System.out.println(sceneID);

        //设置请求头
        Map<String,String> header = new HashMap<String, String>();
        header.put("Authorization", TokenUtil.getToken());

        //设置请求参数
        Map<String,String> param = new HashMap<String, String>();
        param.put("id",sceneID);

        //执行请求
        try {
            Response response = HttpRequestsUtil.doGetWithParams(url, header, param);
            String responseStr = response.getResponseStr();
            System.out.println(responseStr);
            if(response.getStatusCode() == 200){
                JSONObject jsonObject = JSONObject.parseObject(responseStr);
                int res = jsonObject.getIntValue("res");
                if(res == 0){
                    Assert.assertEquals(jsonObject.getString("msg"),"success");
                }else {
                    Assert.assertEquals("res、msg校验失败","res、msg没有预期值");
                }

            }else {
                Assert.assertEquals("接口返回状态码不是200","接口返回状态码应为200");
            }


        } catch (IOException e) {
            e.printStackTrace();
        }


    }



}
