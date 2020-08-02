package com.example.eklavya;

public class FacultyModelA {
    private String Name;
    private String Department;

    private FacultyModelA(String Name,String Department){
        this.Name=Name;
        this.Department=Department;
    }
    //Add this
    public FacultyModelA() {

    }

    public String getName(){
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getDepartment() {
        return Department;
    }

    public void setDepartment(String department) {
        this.Department = department;
    }
}
