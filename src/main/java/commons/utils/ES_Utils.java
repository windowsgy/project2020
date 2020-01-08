package commons.utils;


import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;

import java.io.IOException;

public class ES_Utils {

  /*  private TransportClient client;

    public TransportClient getClient(String clusterName, String ip, int port) throws Exception {
        try{
            //设置集群名称
            Settings settings = Settings.builder().put("cluster.name", clusterName).build();// 集群名
            //创建client
            client = new PreBuiltTransportClient(settings)
                    .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(ip), port));
            return client;
        }catch (Exception e){
            Log.error(e);
            return null;
        }

    }

    public SearchResponse match(String indexName, String type, QueryBuilder queryBuilder, int size) {
        return client.prepareSearch(indexName).setTypes(type).setQuery(queryBuilder).setSize(size).get();
    }
*/

    /**
     * 获取client
     * @param host 主机
     * @return client
     */
    public RestHighLevelClient getClient(String host) {
        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(new HttpHost(host, 9200, "http")));
        return client;
    }


    /**
     * 关闭连接
     * @param client client
     */
    public void closeClient(RestHighLevelClient client) {
        try {
            client.close();
        } catch (IOException e) {
            Log.error(e);
        }
    }

}
