package com.example.eklavya;

public class StudentModelA {

    private String Name;
    private String Batch;
    private String Department;
    private String RollNo;
    private String studid;

    private StudentModelA(String Name,String Class){
        this.Name=Name;
        this.Batch=Batch;
        this.Department=Department;
        this.RollNo=RollNo;
        this.studid=studid;

    }
    //Add this
    public StudentModelA() {

    }

    public String getName(){
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }
    public String getBatch(){
        return Batch;
    }

    public void setBatch(String Batch) {
        this.Batch = Batch;
    }

    public String getDepartment() {
        return Department;
    }

    public void setDepartment(String Department) {
        this.Department = Department;
    }

    public String getRollNo() {
        return RollNo;
    }

    public void setRollNo(String rollNo) {
        this.RollNo = rollNo;
    }

    public String getStudid() {
        return studid;
    }

    public void setStudid(String studid) {
        this.studid=studid;
    }

}
