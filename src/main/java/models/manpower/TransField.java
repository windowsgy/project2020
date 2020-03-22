package models.manpower;

import java.util.List;

public class TransField {

    public static List<List<String>> transTimes(List<List<String>> list,int index){
        for (List<String> subList : list) {
            String fields = subList.get(index);
            fields = fields.replace(" ","T")+".000Z";
            subList.set(index, fields);
        }
        return list;
    }
}
