
import commons.utils.EsUtils;
import commons.utils.ExcelUtils;
import models.Params;
import models.manpower.AddFields;
import models.manpower.BuildStruct;
import models.manpower.TransField;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.client.RestHighLevelClient;
import struct.Salary;
import trans.Trans;


import java.util.List;
import java.util.Map;

public class Start {

    public static void main(String[] args) {
        String path = "E:\\Office\\公务\\2020\\测试数据.xlsx";
        String esHost = "192.168.1.254";
        String esIndexName = "salary";
        ExcelUtils excelUtils = new ExcelUtils();
        List<List<String>> list = excelUtils.xlsToList(path, Params.splintChar);
        list = TransField.transTimes(list,0);//修改时间字段格式
        list = AddFields.add(list,"1");//增加统计字段
        list = AddFields.addYear(list);
        list = AddFields.addMonth(list);
        BuildStruct buildData = new BuildStruct();
        List<Salary> salaryList = buildData.run(list);
        Trans trans = new Trans();
        List<String> jsonList = trans.toJson(salaryList);
        List<Map<String, Object>> mapList = trans.toMap(jsonList);
        EsUtils esUtils = new EsUtils();
        RestHighLevelClient client = esUtils.getClient(esHost);
        BulkRequest bulkRequest = esUtils.buildBulk(mapList, esIndexName);
        BulkResponse bulkResponses = esUtils.bulk(client, bulkRequest);
        esUtils.closeClient(client);
    }


    /**
     * 打印
     * @param mapList 集合
     */
    private static void print(List<Map<String, Object>> mapList) {
        for (Map<String, Object> map : mapList) {
            for (String key : map.keySet()) {
                Object obj = map.get(key);
                System.out.println(key + ":" + obj.toString());
            }
        }
    }

}
