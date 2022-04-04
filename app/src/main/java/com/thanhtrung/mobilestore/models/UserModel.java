package com.thanhtrung.mobilestore.models;


public class UserModel {
    String name,email,password,phone,address;
    String profileImg;

    public UserModel(){

    }

//    public UserModel(String name, String email, String password, String numberphone, String address, String profileImg) {
//        this.name = name;
//        this.email = email;
//        this.password = password;
//        this.numberphone = numberphone;
//        this.address = address;
//        this.profileImg = profileImg;
//    }

    public UserModel(String name, String email, String password){
        this.name = name;
        this.email = email;
        this.password = password;

    }

    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public String getProfileImg() {
        return profileImg;
    }

    public void setProfileImg(String profileImg) {
        this.profileImg = profileImg;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
