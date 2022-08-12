package com.example.ieaadmin;

public class ReferModelClass {

    String name,email,contactno,companyname,useremail,status;

    public ReferModelClass() {
    }

    public ReferModelClass(String name, String email, String contactno, String companyname, String useremail, String status) {
        this.name = name;
        this.email = email;
        this.contactno = contactno;
        this.companyname = companyname;
        this.useremail = useremail;
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getContactno() {
        return contactno;
    }

    public void setContactno(String contactno) {
        this.contactno = contactno;
    }

    public String getCompanyname() {
        return companyname;
    }

    public void setCompanyname(String companyname) {
        this.companyname = companyname;
    }

    public String getUseremail() {
        return useremail;
    }

    public void setUseremail(String useremail) {
        this.useremail = useremail;
    }
}