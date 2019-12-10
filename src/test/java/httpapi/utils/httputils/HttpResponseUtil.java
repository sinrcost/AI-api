package httpapi.utils.httputils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import java.io.IOException;

public class HttpResponseUtil {

    private static final Logger logger= Logger.getLogger(HttpResponseUtil.class);

    public static Response processResponse(HttpResponse httpresponse) throws ParseException, IOException {
        Response response = null;

        int statusCode = httpresponse.getStatusLine().getStatusCode();
        HttpEntity httpEntity = httpresponse.getEntity();
        String responseStr = EntityUtils.toString(httpEntity,"utf-8");

        logger.info("响应状态码："+statusCode+";响应内容："+responseStr);

        response = new Response(statusCode,responseStr);

        return response;
    }
}
