package commons.utils;


import org.apache.http.HttpHost;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.*;
import org.elasticsearch.cluster.metadata.MappingMetaData;
import org.elasticsearch.common.xcontent.XContentType;
import trans.Trans;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class EsUtils {

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
     * 创建索引
     * @param indexName 索引名称
     * @param client esClient
     */
    public boolean createIndex(String indexName, RestHighLevelClient client) {
        CreateIndexRequest request = new CreateIndexRequest(indexName);
        try {
            CreateIndexResponse response = client.indices().create(request, RequestOptions.DEFAULT);
            if (response.isAcknowledged() || response.isShardsAcknowledged()) {
                return true;
            }
        } catch (IOException e) {
            Log.error(e);
        }
        return false;
    }

    public void getMapping(RestHighLevelClient client ,String indexName){
        Trans trans = new Trans();
        GetMappingsRequest request = new GetMappingsRequest();
        request.indices(indexName);
        try {
            GetMappingsResponse response = client.indices().getMapping(request, RequestOptions.DEFAULT);
            Map<String, MappingMetaData> allMappings = response.mappings();
            MappingMetaData indexMapping = allMappings.get(indexName);
            Map<String, Object> mapping = indexMapping.sourceAsMap();
            System.out.println(trans.mapToJson(mapping));
        } catch (IOException e) {
            Log.error(e);
        }
    }


    /**
     * 判断索引是否存在
     * @param client esClient
     * @param indexName 索引名称
     * @return boolean
     */
    public boolean existsIndex(RestHighLevelClient client,String indexName){
        GetIndexRequest request = new GetIndexRequest(indexName);
        try {
            return client.indices().exists(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            Log.error(e);
            return false;
        }
    }


    /**
     * 获取client
     *
     * @param host 主机
     * @return client
     */
    public RestHighLevelClient getClient(String host) {
        return new RestHighLevelClient(
                RestClient.builder(new HttpHost(host, 9200, "http")));
    }


    /**
     * 关闭连接
     *
     * @param client client
     */
    public void closeClient(RestHighLevelClient client) {
        try {
            client.close();
        } catch (IOException e) {
            Log.error(e);
        }
    }


    /**
     * 构建bulk
     *
     * @param list      数据列表
     * @param indexName es索引名称
     * @return bulk请求结构
     */
    public BulkRequest buildBulk(List<Map<String, Object>> list, String indexName) {
        BulkRequest request = new BulkRequest(); //创建BulkRequest
        for (Map<String, Object> map : list) {//map添加至request
            request.add(new IndexRequest(indexName).source(map, XContentType.JSON));
        }
        return request;
    }

    /**
     * 执行bulk操作
     *
     * @param client      esClient
     * @param bulkRequest bulkRequest
     * @return bulk执行结果反馈
     */
    public BulkResponse bulk(RestHighLevelClient client, BulkRequest bulkRequest) {
        try {
            return client.bulk(bulkRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            Log.error(e);
            return null;
        }
    }
}
