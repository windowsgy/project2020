package struct;

public class Salary {
    //@JsonProperty("sex")
    private	String	date;   //日期
    private	String	name;   //姓名
    private	double	salary; //应领工资
    private	double	endowmentInsurance; //养老保险
    private	double	unemploymentInsurance; //失业保险
    private	double	medicalInsurance;//	医疗保险
    private	double	housingProvidentFund; //住房公积金
    private	double	sum	;//合计
    private	double	shouldBePaid	;//实领工资
    private	double	serviceFeeSum	;//劳务费合计
    private int num;//计数
    private String year;
    private String month;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public double getEndowmentInsurance() {
        return endowmentInsurance;
    }

    public void setEndowmentInsurance(double endowmentInsurance) {
        this.endowmentInsurance = endowmentInsurance;
    }

    public double getUnemploymentInsurance() {
        return unemploymentInsurance;
    }

    public void setUnemploymentInsurance(double unemploymentInsurance) {
        this.unemploymentInsurance = unemploymentInsurance;
    }

    public double getMedicalInsurance() {
        return medicalInsurance;
    }

    public void setMedicalInsurance(double medicalInsurance) {
        this.medicalInsurance = medicalInsurance;
    }

    public double getHousingProvidentFund() {
        return housingProvidentFund;
    }

    public void setHousingProvidentFund(double housingProvidentFund) {
        this.housingProvidentFund = housingProvidentFund;
    }

    public double getSum() {
        return sum;
    }

    public void setSum(double sum) {
        this.sum = sum;
    }

    public double getShouldBePaid() {
        return shouldBePaid;
    }

    public void setShouldBePaid(double shouldBePaid) {
        this.shouldBePaid = shouldBePaid;
    }

    public double getServiceFeeSum() {
        return serviceFeeSum;
    }

    public void setServiceFeeSum(double serviceFeeSum) {
        this.serviceFeeSum = serviceFeeSum;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }
}
