package com.example.ieaadmin;

public class grievancehelperclass {

    private String email;
    private String department;
    private String complain;

    public grievancehelperclass(){}

    public grievancehelperclass(String email, String department, String complain) {
        this.email = email;
        this.department = department;
        this.complain = complain;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getComplain() {
        return complain;
    }

    public void setComplain(String complain) {
        this.complain = complain;
    }
}