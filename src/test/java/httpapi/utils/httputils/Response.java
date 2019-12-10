package httpapi.utils.httputils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


public class Response {

    private int responseStatusCode;
    private String responseStr;

    public Response(int responseStatusCode, String responseStr){
        this.responseStatusCode = responseStatusCode;
        this.responseStr = responseStr;
    }

    public int getStatusCode() {
        return responseStatusCode;
    }

    public String getResponseStr() {
        return responseStr;
    }

    /**
     * 根据接口某个字段的值判断接口是否正常
     * @param response
     * @return
     */
    public static Boolean isSuccess(Response response){
        Boolean flag = false;
        if (response.getStatusCode() == 200) {
            JsonObject resp = new JsonParser().parse(response.getResponseStr()).getAsJsonObject();
            if (resp.get("res").getAsInt()==0) {
                flag = true;
            }
        }
        return flag;
    }
}
