package httpapi.utils.httputils;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

public class HttpClientConfigUtil {

    /**
     * 通过连接池管理器获取HttpClient对象
     * @return
     */
    public static CloseableHttpClient gethttpClient(){

        //创建连接池管理器
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();

        //设置连接数
        cm.setMaxTotal(200);

        //设置每个主机连接数
        cm.setDefaultMaxPerRoute(50);

        //从连接池中获取HttpClient对象
        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(cm).build();


        return httpClient;
    }


    /**
     *设置请求的配置信息
     * @return
     */
    public static RequestConfig getRequestConfig(){

        //设置配置信息
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(30000)    //创建连接的最长时间
                .setConnectionRequestTimeout(30000)  //设置获取连接的最长时间
                .setSocketTimeout(10*1000)      //设置数据传输的最长时间
                .build();



        return requestConfig;
    }


}
