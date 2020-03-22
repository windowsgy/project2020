import commons.utils.ExcelUtils;

import java.util.List;

public class TestMain {
    public static void main(String[] args) {
        String path = "D:\\data\\BAS.xlsx";
        ExcelUtils excelUtils = new ExcelUtils();
        //List<List<String>> list = excelUtils.xlsToList(path,",");
        excelUtils.printExcelMap(path,",");
    }
}
