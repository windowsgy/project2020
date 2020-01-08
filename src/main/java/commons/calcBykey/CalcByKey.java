package commons.calcBykey;

import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.function.Function2;
import scala.Tuple2;

public class CalcByKey {
    /**
     * 统计计算
     *
     * @param rdd 单value
     * @return 统计结果
     */
    public JavaPairRDD<String, Long> simpleValue(JavaPairRDD<String, Long> rdd) {
        return rdd.reduceByKey((Function2<Long, Long, Long>) (aLong, aLong2) -> aLong + aLong2);
    }


    /**
     * 统计计算
     *
     * @param rdd 双value数据
     * @return 统计结果
     */

    public JavaPairRDD<String, Tuple2<Long,Long>> doubleValue(JavaPairRDD<String, Tuple2<Long,Long> > rdd) {
        return rdd.reduceByKey((Function2<Tuple2<Long, Long>, Tuple2<Long, Long>, Tuple2<Long, Long>>) (longLongTuple2, longLongTuple22) -> new Tuple2<>(longLongTuple2._1+longLongTuple22._1,longLongTuple2._2+longLongTuple22._2));
    }
}
