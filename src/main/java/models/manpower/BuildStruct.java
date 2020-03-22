package models.manpower;

import struct.Salary;

import java.util.ArrayList;
import java.util.List;

public class BuildStruct {

    public List<Salary> run(List<List<String>> list ) {
        List<Salary> salaryList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            List<String> subList = list.get(i);
            Salary salary = new Salary();
            salary.setDate(subList.get(0));
            salary.setName(subList.get(1));
            salary.setSalary(toDouble(subList.get(2)));
            salary.setEndowmentInsurance(toDouble(subList.get(3)));
            salary.setUnemploymentInsurance(toDouble(subList.get(4)));
            salary.setMedicalInsurance(toDouble(subList.get(5)));
            salary.setHousingProvidentFund(toDouble(subList.get(6)));
            salary.setSum(toDouble(subList.get(7)));
            salary.setShouldBePaid(toDouble(subList.get(8)));
            salary.setServiceFeeSum(toDouble(subList.get(9)));
            salary.setNum(toInt(subList.get(10)));
            salary.setYear(subList.get(11));
            salary.setMonth(subList.get(12));
            salaryList.add(salary);
        }
        return salaryList;
    }

    private double toDouble(String str){
        if(str.equals("")){
            return 0.0;
        }else{
            return Double.parseDouble(str);
        }
    }

    private int toInt(String str){
        if(str.equals("")){
            return 0;
        }else{
            return Integer.parseInt(str);
        }
    }
}
