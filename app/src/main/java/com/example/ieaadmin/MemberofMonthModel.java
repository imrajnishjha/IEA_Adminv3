package com.example.ieaadmin;

public class MemberofMonthModel {
    String name,companyname,purl, description;

    public MemberofMonthModel() {
    }

    public MemberofMonthModel(String name, String companyname, String purl, String description) {
        this.name = name;
        this.companyname = companyname;
        this.purl = purl;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCompanyname() {
        return companyname;
    }

    public void setCompanyname(String companyname) {
        this.companyname = companyname;
    }

    public String getPurl() {
        return purl;
    }

    public void setPurl(String purl) {
        this.purl = purl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
