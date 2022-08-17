package com.example.ieaadmin;

public class RegistrationDataModel {
    String turnover,payProofUrl,email,amountLeft,department,paymentReceiverName,gstno;

    RegistrationDataModel(){

    }

    public RegistrationDataModel( String turnover, String payProofUrl, String email, String amountLeft, String department, String paymentReceiverName, String gstno) {

        this.turnover = turnover;
        this.payProofUrl = payProofUrl;
        this.email = email;
        this.amountLeft = amountLeft;
        this.department = department;
        this.paymentReceiverName = paymentReceiverName;
        this.gstno = gstno;
    }


    public String getGstno() {
        return gstno;
    }

    public void setGstno(String gstno) {
        this.gstno = gstno;
    }


    public String getTurnover() {
        return turnover;
    }

    public void setTurnover(String turnover) {
        this.turnover = turnover;
    }

    public String getPayProofUrl() {
        return payProofUrl;
    }

    public void setPayProofUrl(String payProofUrl) {
        this.payProofUrl = payProofUrl;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAmountLeft() {
        return amountLeft;
    }

    public void setAmountLeft(String amountLeft) {
        this.amountLeft = amountLeft;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getPaymentReceiverName() {
        return paymentReceiverName;
    }

    public void setPaymentReceiverName(String paymentReceiverName) {
        this.paymentReceiverName = paymentReceiverName;
    }
}