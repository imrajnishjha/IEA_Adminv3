package com.example.ieaadmin;

public class UserRegistrationHelperClass {
    String fullname, email, phoneNo, companyName, Department, Turnover,imageUrl,amounutLeft;

    public  UserRegistrationHelperClass(){

    }

    public UserRegistrationHelperClass(String fullname, String email, String phoneNo, String companyName, String department, String turnover, String imageUrl, String amounutLeft) {
        this.fullname = fullname;
        this.email = email;
        this.phoneNo = phoneNo;
        this.companyName = companyName;
        Department = department;
        Turnover = turnover;
        this.imageUrl = imageUrl;
        this.amounutLeft = amounutLeft;
    }

    public String getAmounutLeft() {
        return amounutLeft;
    }

    public void setAmounutLeft(String amounutLeft) {
        this.amounutLeft = amounutLeft;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getDepartment() {
        return Department;
    }

    public void setDepartment(String department) {
        Department = department;
    }

    public String getTurnover() {
        return Turnover;
    }

    public void setTurnover(String turnover) {
        Turnover = turnover;
    }
}
