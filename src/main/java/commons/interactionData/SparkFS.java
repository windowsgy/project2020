package commons.interactionData;


import commons.utils.Log;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

/**
 * SparkRDD数据交互实现类，用于SparkRdd与文件系统的数据交互
 */
public class SparkFS {

    /**
     * 加载数据从文件系统
     * @param sc sparkContext
     * @param path 文件系统路径
     * @return  rdd
     */
    public JavaRDD<String> load(JavaSparkContext sc,String path){
        Log.debug("Spark Load Path:"+path);
        try{
            return sc.textFile(path);
        }catch (Exception e){
            Log.error(e);
            return null;
        }
    }

    /**
     * 保存RDD方法
     * @param path 路径
     * @param rdd rdd
     */
    public void save(String path, JavaRDD rdd){
        Log.debug("Spark Save Path:"+path);
        try{
            rdd.saveAsTextFile(path);
        }catch (Exception e){
            Log.error(e);
        }
    }

}
