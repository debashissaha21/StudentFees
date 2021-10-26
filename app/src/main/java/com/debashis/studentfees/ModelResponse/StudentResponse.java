package com.debashis.studentfees.ModelResponse;

import java.io.Serializable;

public class StudentResponse implements Serializable {
    private int id;
    private String image,
            aadhar_number,
            due_fees,
            name,
            address,
            email,
            number,
            joining_date,
            leaving_date,
            academic_year,
            aadhar_image,
            fees_per_month,
            total_fees,
            paid_fees,
            remark,
            parent_name,
            parent_number,
            parent_aadhar,
            parent_aadhar_image;

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getParent_name() {
        return parent_name;
    }

    public void setParent_name(String parent_name) {
        this.parent_name = parent_name;
    }

    public String getParent_number() {
        return parent_number;
    }

    public void setParent_number(String parent_number) {
        this.parent_number = parent_number;
    }

    public String getParent_aadhar() {
        return parent_aadhar;
    }

    public void setParent_aadhar(String parent_aadhar) {
        this.parent_aadhar = parent_aadhar;
    }

    public String getParent_aadhar_image() {
        return parent_aadhar_image;
    }

    public void setParent_aadhar_image(String parent_aadhar_image) {
        this.parent_aadhar_image = parent_aadhar_image;
    }

    public String getFees_per_month() {
        return fees_per_month;
    }

    public void setFees_per_month(String fees_per_month) {
        this.fees_per_month = fees_per_month;
    }

    public String getTotal_fees() {
        return total_fees;
    }

    public void setTotal_fees(String total_fees) {
        this.total_fees = total_fees;
    }

    public String getPaid_fees() {
        return paid_fees;
    }

    public void setPaid_fees(String paid_fees) {
        this.paid_fees = paid_fees;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getJoin_date() {
        return joining_date;
    }

    public void setJoin_date(String join_date) {
        this.joining_date = join_date;
    }

    public String getLeave_date() {
        return leaving_date;
    }

    public void setLeave_date(String leave_date) {
        this.leaving_date = leave_date;
    }

    public String getAcademic_year() {
        return academic_year;
    }

    public void setAcademic_year(String academic_year) {
        this.academic_year = academic_year;
    }

    public String getAadhar_image() {
        return aadhar_image;
    }

    public void setAadhar_image(String aadhar_image) {
        this.aadhar_image = aadhar_image;
    }

    public String getAadhar_number() {
        return aadhar_number;
    }

    public void setAadhar_number(String aadhar_number) {
        this.aadhar_number = aadhar_number;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDue_fees() {
        return due_fees;
    }

    public void setDue_fees(String due_fees) {
        this.due_fees = due_fees;
    }
}
