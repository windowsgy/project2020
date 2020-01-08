package commons.interactionData;


import com.mongodb.client.MongoCollection;
import com.mongodb.spark.MongoConnector;
import com.mongodb.spark.MongoSpark;
import com.mongodb.spark.config.ReadConfig;
import com.mongodb.spark.config.WriteConfig;
import com.mongodb.spark.rdd.api.java.JavaMongoRDD;
import commons.utils.Log;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.bson.Document;

import java.util.Collection;


/**
 * mongodb交互实现类
 */
public class SparkMongoDB {

    /**
     * mongodb 查询
     * @param sparkSc    sparkContext
     * @param readConfig 读数据配置
     * @return rdd
     *
     */
    public JavaMongoRDD<Document> load(JavaSparkContext sparkSc, ReadConfig readConfig) {
        Log.debug("Load Mongodb Data    " + readConfig.databaseName() + "   " + readConfig.collectionName());
        try {
            return MongoSpark.load(sparkSc, readConfig);
        } catch (Exception e) {
            Log.error(e);
            return null;
        }
    }

    /**
     *
     * @param sparkSc     sparkContext
     * @param rdd         RDD
     * @param writeConfig 写入数据配置
     */
    public void write(JavaSparkContext sparkSc, JavaRDD<Document> rdd, WriteConfig writeConfig) {
        Log.debug("Write To Mongodb  " + writeConfig.databaseName() + "   " + writeConfig.collectionName());
        try {
            //提前把Collection先drop掉，就是Overwrite模式写入mongodb
            MongoConnector.create(sparkSc).withCollectionDo(writeConfig, Collection.class, (Function<MongoCollection<Collection>, Document>) v1 -> {
                v1.drop();
                return null;
            });
            //写入Mongodb
            MongoSpark.save(rdd, writeConfig);
        } catch (Exception e) {
            Log.error(e);
        }

    }

    /**
     *
     * @param rdd   RDD
     * @param writeConfig 写入数据配置
     */
    public boolean insert(JavaRDD<Document> rdd, WriteConfig writeConfig) {
        Log.debug("Insert To Mongodb: " + writeConfig.databaseName() + "  collection: " + writeConfig.collectionName());
        try {
            //写入Mongodb
            MongoSpark.save(rdd, writeConfig);
            return true;
        } catch (Exception e) {
            Log.error(e);
            return false;
        }
    }
}
