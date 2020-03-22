package es;

import commons.utils.EsUtils;
import org.elasticsearch.client.RestHighLevelClient;


public class ESMain {
    public static void main(String[] args) {
        String host = "192.168.1.254";
        //String testIndexName = "test_index";
        EsUtils esUtils = new EsUtils();
        RestHighLevelClient client = esUtils.getClient(host);
        //System.out.println(esUtils.createIndex(testIndexName, client));
        //System.out.println(esUtils.existsIndex(client,testIndexName));
        String indexName = "salary";
        esUtils.getMapping(client,indexName);
        esUtils.closeClient(client);
    }
}
