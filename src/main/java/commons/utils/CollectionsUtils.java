package commons.utils;

import java.util.ArrayList;
import java.util.List;

public class CollectionsUtils {
    List<String> splitList(List<String> list,int start,int end){
        List<String> newList = new ArrayList<>();
        for(int i = start ; i < end;i++){
            newList.add(list.get(i));
        }
        return newList;
    }
}
