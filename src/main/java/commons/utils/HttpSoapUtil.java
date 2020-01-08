package commons.utils;

import org.apache.commons.text.StringEscapeUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.util.concurrent.*;


public class HttpSoapUtil {


    /**
     *  调用SOAP
     * @param wsUrl URL
     * @param inputXml XML
     * @return resp
     */
    public String invokeSoap(String wsUrl, String inputXml) {
        String resp = "";
        ExecutorService executor = Executors.newSingleThreadExecutor();
        RestClient restClient = new RestClient(wsUrl, inputXml);
        Future<String> future = executor.submit(restClient);
        try {
            resp = future.get(30, TimeUnit.SECONDS);
        } catch (Exception e) {
            Log.error("call timeout:" + e);
        }
        executor.shutdownNow();
        return resp;
    }


    /**
     * 多线程调用RestClient类
     */
    private class RestClient implements Callable<String> {
        private String url;
        private String data;

        RestClient(String url, String data) {
            this.url = url;
            this.data = data;
        }

        public String call() {
            return invoke(url, data);
        }

        private String invoke(String url, String data) {
            Log.debug("send To Url:"+url);
            String resp ;
            HttpClient httpClient = HttpClients.createDefault();
            HttpPost httppost = new HttpPost(url);
            httppost.setHeader("SOAPAction", "");
            HttpResponse response = null;
            try {
                HttpEntity re = new StringEntity(data, "utf-8");
                httppost.setHeader("Content-Type", "text/xml;charset=UTF-8");
                httppost.setEntity(re);
                RequestConfig requestConfig = RequestConfig.custom()
                        .setConnectTimeout(5000).setConnectionRequestTimeout(1000)
                        .setSocketTimeout(5000).build();
                httppost.setConfig(requestConfig);
                response = httpClient.execute(httppost);
              //  Log.debug("StatusCode is:" + response.getStatusLine().getStatusCode());
                resp = StringEscapeUtils.unescapeHtml4(EntityUtils.toString(response.getEntity(), "utf-8"));
            } catch (Exception e) {
                Log.error("HttpSoapUtil :" + e);
                return e.getMessage();
            } finally {
                HttpClientUtils.closeQuietly(response);
                HttpClientUtils.closeQuietly(httpClient);
            }
            return resp;
        }
    }


    /**
     * 获取属性内容
     *
     * @param xmlText  XML
     * @param startTag 开始标记
     * @param endTag   结束标记
     * @return String
     */
    public String getAttributeContent(String xmlText, String startTag, String endTag) {
        String content = "";
        if (xmlText == null || "".equals(xmlText)) return content;
        if (startTag == null || "".equals(startTag) || endTag == null || "".equals(endTag)) {
            return content;
        }
        int start = xmlText.indexOf(startTag);
        int end = xmlText.indexOf(endTag);
        if (start != -1 && end != -1) {
            content = xmlText.substring(startTag.length() + start, end);
        }
        return content;
    }
}
