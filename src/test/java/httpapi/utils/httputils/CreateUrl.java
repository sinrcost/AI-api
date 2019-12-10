package httpapi.utils.httputils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class CreateUrl {

    public static String getUrl(String urlInitial,Map<String,String> params) {
        String url = urlInitial;

        if(params == null || params.size() == 0) {
            return url;

        }else {
            StringBuffer sbuffer = new StringBuffer(url+"?");

            for(Map.Entry<String,String> entry : params.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                try {
                    value = URLEncoder.encode(value, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                sbuffer.append(key).append("=").append(value).append("&");
            }

            url = sbuffer.toString();
            url = url.substring(0, url.length()-1);
            return url;
        }

    }

}
