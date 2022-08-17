package com.example.ieaadmin;

public class memberApprovalDetailModel {
    String address, company_name, date_of_birth, date_of_membership, email, member_id, name, phone_number, purl, description, industry_type, brochure_url, company_logo, memberfee, status;

    memberApprovalDetailModel() {

    }

    public memberApprovalDetailModel(String address, String company_name, String date_of_birth, String date_of_membership, String email, String member_id, String name, String phone_number, String purl, String description, String industry_type, String brochure_url, String company_logo, String memberfee, String status) {
        this.address = address;
        this.company_name = company_name;
        this.date_of_birth = date_of_birth;
        this.date_of_membership = date_of_membership;
        this.email = email;
        this.member_id = member_id;
        this.name = name;
        this.phone_number = phone_number;
        this.purl = purl;
        this.description = description;
        this.industry_type = industry_type;
        this.brochure_url = brochure_url;
        this.company_logo = company_logo;
        this.memberfee = memberfee;
        this.status = status;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getDate_of_birth() {
        return date_of_birth;
    }

    public void setDate_of_birth(String date_of_birth) {
        this.date_of_birth = date_of_birth;
    }

    public String getDate_of_membership() {
        return date_of_membership;
    }

    public void setDate_of_membership(String date_of_membership) {
        this.date_of_membership = date_of_membership;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMember_id() {
        return member_id;
    }

    public void setMember_id(String member_id) {
        this.member_id = member_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
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

    public String getIndustry_type() {
        return industry_type;
    }

    public void setIndustry_type(String industry_type) {
        this.industry_type = industry_type;
    }

    public String getBrochure_url() {
        return brochure_url;
    }

    public void setBrochure_url(String brochure_url) {
        this.brochure_url = brochure_url;
    }

    public String getCompany_logo() {
        return company_logo;
    }

    public void setCompany_logo(String company_logo) {
        this.company_logo = company_logo;
    }

    public String getMemberfee() {
        return memberfee;
    }

    public void setMemberfee(String memberfee) {
        this.memberfee = memberfee;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}