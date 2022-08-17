package com.example.ieaadmin;

public class MemberRenewalModel {
    String email,purl,memberfee,companyname,amountleft,name;

    public MemberRenewalModel() {
    }

    public MemberRenewalModel(String email, String purl, String memberfee, String companyname, String amountleft,String name) {
        this.email = email;
        this.purl = purl;
        this.memberfee = memberfee;
        this.companyname = companyname;
        this.amountleft = amountleft;
        this.name = name;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPurl() {
        return purl;
    }

    public void setPurl(String purl) {
        this.purl = purl;
    }

    public String getMemberfee() {
        return memberfee;
    }

    public void setMemberfee(String memberfee) {
        this.memberfee = memberfee;
    }

    public String getCompanyname() {
        return companyname;
    }

    public void setCompanyname(String companyname) {
        this.companyname = companyname;
    }

    public String getAmountleft() {
        return amountleft;
    }

    public void setAmountleft(String amountleft) {
        this.amountleft = amountleft;
    }
}
