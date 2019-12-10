package httpapi.utils.commons;


public class GetCompleteUrl {

    public static String getUrl(String urlInitial, String value){

        String url = urlInitial;

        if(value == null ) {
            return url;

        }else {
            StringBuffer sbuffer = new StringBuffer(url+"/");
            sbuffer.append(value);

            url = sbuffer.toString();
            url = url.substring(0, url.length());
            return url;
        }


    }


}
