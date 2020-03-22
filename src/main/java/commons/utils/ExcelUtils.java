package commons.utils;


import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by jlgaoyuan on 2018/6/16.
 * EXCEL提取工具
 */
public class ExcelUtils {

    /**
     * 打印EXCEL文件中所有的sheet
     *
     * @param filePath   文件路径
     * @param splintChar 分隔符
     */
    public void printExcelMap(String filePath, String splintChar) {
        Map<String, List<List<String>>> map = xlsToMap(filePath, splintChar);
        System.out.println("map size is : " + map.size());
        if (map.size() < 1) {
            return;
        }
        for (String key : map.keySet()) {
            System.out.println("sheet is :" + key);
            List<List<String>> list = map.get(key);
            if ( list == null) {
                System.out.println("sheet is null");
                return;
            }
            if (list.size() < 1 ) {
                System.out.println("sheet size is :0");
                return;
            }
            System.out.println("sheet size is :" + list.size());
            printSheet(list, splintChar);
        }
    }

    /**
     * @param list       list
     * @param splintChar 分隔符
     */
    public void printExcelList(List<List<String>> list, String splintChar) {
        printSheet(list, splintChar);

    }

    private void printSheet(List<List<String>> list, String splintChar) {
        if (list.size() < 1) {
            System.out.println(" Sheet size is 0 ");
            return;
        }
        for (List<String> subList : list) {
            for (String aSubList : subList) {
                System.out.print(aSubList + splintChar);
            }
            System.out.println();
        }
    }


    /**
     * EXCEL 文件转换为map  EXCEL文件中每个表格名对应一个Key 。List 每行 ，List 每列
     * 去掉了表头数据
     *
     * @param filePath      excel文件路径
     * @param isolationChar 分隔符,每个字段都要处理分隔符，把其过滤掉
     * @return map
     */
    public Map<String, List<List<String>>> xlsToMap(String filePath, String isolationChar) {
        Map<String, List<List<String>>> map = new HashMap<>();
        File xlsFile = new File(filePath);
        if (!xlsFile.exists()) { //判断文件是否存在
            return null;
        }
        try {
            Workbook wb = WorkbookFactory.create(xlsFile);
            int sheetNum = wb.getNumberOfSheets();//获取EXCEL表数量
            for (int sheetIndex = 0; sheetIndex < sheetNum; sheetIndex++) {//遍历sheet(index 0开始)
                List<List<String>> rowList = sheetToList(wb, sheetIndex, isolationChar);
                //Map 中 key 名称为 文件名加表格名
                String keyName = wb.getSheetName(sheetIndex);
                map.put(keyName, rowList);
            }//end sheets
        } catch (Exception e) {
            Log.error(e);
            return null;
        }
        return map;
    }


    /**
     * EXCEL 文件转换为第一个sheet List 每行 ，List 每列
     * 去掉了表头数据
     *
     * @param filePath      excel文件路径
     * @param isolationChar 分隔符,每个字段都要处理分隔符，把其过滤掉
     * @return list
     */
    public List<List<String>> xlsToList(String filePath, String isolationChar) {
        File xlsFile = new File(filePath);
        if (!xlsFile.exists()) { //判断文件是否存在
            return null;
        }
        try {
            Workbook wb = WorkbookFactory.create(xlsFile);
            return sheetToList(wb, 0, isolationChar);
        } catch (Exception e) {
            Log.error(e);
            return null;
        }
    }


    /**
     * 读取EXCEL 文件的表
     *
     * @param wb            excel文件
     * @param sheetIndex    表索引
     * @param isolationChar 分隔符,每个字段都要处理分隔符，把其过滤掉
     * @return 行列表
     */
    private List<List<String>> sheetToList(Workbook wb, int sheetIndex, String isolationChar) {
        List<List<String>> rowList = new ArrayList<>(); //返回的行列表
        try {
            Sheet sheet = wb.getSheetAt(sheetIndex);
            Row row;
            int firstRowNum = sheet.getFirstRowNum();//首行号码
            int lastRowNum = sheet.getLastRowNum();//尾行号码
            for (int rowIndex = firstRowNum; rowIndex <= lastRowNum; rowIndex++) {//遍历row(行 0开始)
                row = sheet.getRow(rowIndex);
                if (null != row) {
                    List<String> cellList = new ArrayList<>();//列List
                    int firstCellNum = row.getFirstCellNum();//首列号
                    int lastCellNum = row.getLastCellNum();//尾列号
                    for (int cellIndex = firstCellNum; cellIndex < lastCellNum; cellIndex++) {//遍历cell（列 0开始）
                        Cell cell = row.getCell(cellIndex, Row.RETURN_BLANK_AS_NULL);
                        String cellValue;
                        // null 不进行添加
                        if (cell != null) {
                            switch (cell.getCellType()) {//数据类型判断并转换为字符串
                                case Cell.CELL_TYPE_STRING: //如果是字符串
                                    cellValue = cell.getStringCellValue();
                                    break;
                                case Cell.CELL_TYPE_NUMERIC://如果是数值类型
                                    if (DateUtil.isCellDateFormatted(cell)) {//如果是日期,按日期格式转换
                                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                        cellValue = formatter.format(cell.getDateCellValue());
                                    } else {//如果是数值，转换为字符串
                                        cellValue = new DecimalFormat("0.00").format(cell.getNumericCellValue());
                                    }
                                    break;
                                case Cell.CELL_TYPE_BOOLEAN://boolean 类型
                                    cellValue = cell.getBooleanCellValue() + "";
                                    break;
                                case Cell.CELL_TYPE_FORMULA://公式类型
                                    cellValue = cell.getCellFormula();
                                    break;
                                default://其他情况 包括空值、非法字符、未知类型全部标注为 ""
                                    cellValue = "";
                            }
                            cellValue = cellValue.trim();
                            cellValue = cellValue.replace("\r", "");//替换换行
                            cellValue = cellValue.replace("\n", "");//替换回车
                            cellValue = cellValue.replace(isolationChar, "");//替换分隔符

                        } else {
                            cellValue = "";
                        }
                        cellList.add(cellValue);//每列添加
                        //System.out.println(cellValue);
                    }//end cells
                    Set set = new HashSet<>(cellList);//list转为set 判断所有字段是否都为""
                    if (!(set.size() == 1 && set.contains(""))) {//如果行内每一列数据不全部为"",才进行添加每行
                        rowList.add(cellList);//每行添加
                    }
                }//end every row
            }//end rows
            rowList.remove(0);//去掉表头
        } catch (Exception e) {
            Log.error(e);
            return null;
        }
        return rowList;
    }

}
