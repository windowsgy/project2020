package commons.utils;

import org.apache.spark.util.AccumulatorV2;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义累加器
 */
public class CustomAccumulator extends AccumulatorV2<String, List<String>> {
    private List<String> list = new ArrayList<>();

    @Override
    public boolean isZero() {
        return 0 ==list.size();
    }

    @Override
    public AccumulatorV2<String, List<String>> copy() {
        CustomAccumulator customAccumulatorV2=new CustomAccumulator();
        synchronized(customAccumulatorV2){
            customAccumulatorV2.list.addAll(list);
        }
        return customAccumulatorV2;
    }

    @Override
    public void reset() {
        list.clear();
    }

    @Override
    public void add(String v) {
        list.add(v);
    }

    @Override
    public void merge(AccumulatorV2<String, List<String>> other) {
        list.addAll(other.value());
    }

    @Override
    public List<String> value() {
        return list;
    }
}
