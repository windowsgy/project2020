package trans;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import commons.utils.Log;
import struct.Salary;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Trans {

    private static final ObjectMapper mapper = new ObjectMapper();

    public List<Map<String, Object>> toMap(List<String> list) {
        List<Map<String, Object>> mapList = new ArrayList<>();
        for (String json : list) {
            try {
                Map<String, Object> map = mapper.readValue(json, Map.class);
                mapList.add(map);
            } catch (IOException e) {
                Log.error(e);
            }
        }
        return mapList;
    }

    public String mapToJson(Map<String,Object> map){
        try {
            return mapper.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            Log.error(e);
        }
        return null;
    }

    public List<String> toJson(List<Salary> list) {
        List<String> jsonList = new ArrayList<>();
        for (Salary aList : list) {
            try {
                String salaryJson = mapper.writeValueAsString(aList);
                jsonList.add(salaryJson);
            } catch (IOException e) {
                Log.error(e);
            }
        }
        return jsonList;
    }
}
