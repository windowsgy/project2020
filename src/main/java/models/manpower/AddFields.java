package models.manpower;

import java.util.List;

public class AddFields {
    public static List<List<String>> add(List<List<String>> list,String addField){
        for (List<String> subList : list) {
            subList.add(addField);
        }
        return list;
    }

    public static List<List<String>> addYear(List<List<String>> list){
        for (List<String> subList : list) {
            String year = subList.get(0).substring(0,4);
            subList.add(year);
        }
        return list;
    }

    public static List<List<String>> addMonth(List<List<String>> list){
        for (List<String> subList : list) {
            String year = subList.get(0).substring(5,7);
            subList.add(year);
        }
        return list;
    }
}
