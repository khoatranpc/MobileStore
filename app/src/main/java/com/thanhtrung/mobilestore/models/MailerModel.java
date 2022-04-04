package com.thanhtrung.mobilestore.models;

public class MailerModel {
    int OTP;
    String email;
    String id;

    public MailerModel() {
    }

    public MailerModel(int OTP, String email,String id) {
        this.OTP = OTP;
        this.email = email;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getOTP() {
        return OTP;
    }

    public void setOTP(int OTP) {
        this.OTP = OTP;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
