package com.example.campus_news.StringEntity;

import android.graphics.Bitmap;

import java.sql.Blob;

public class UserInfo {
    private String phone;
    private String u_name;
    private String u_sex;
    private String u_password;
    private String O_phone;
    private String u_birthday;
    private String u_email;
    private String u_picture;
    public UserInfo() {
    }

    public UserInfo(String phone, String u_name,String u_sex, String u_password, String O_phone, String u_birthday, String u_email, String u_picture) {
        this.phone = phone;
        this.u_name = u_name;
        this.u_sex = u_sex;
        this.u_password = u_password;
        this.O_phone = O_phone;
        this.u_birthday = u_birthday;
        this.u_email = u_email;
        this.u_picture = u_picture;
    }

    public String getPhone() {
        return phone;
    }

    public String getU_name() {
        return u_name;
    }
    public String getU_sex() { return u_sex; }

    public String getU_password() {
        return u_password;
    }

    public String getO_phone() {
        return O_phone;
    }

    public String getU_birthday() {
        return u_birthday;
    }

    public String getU_email() {
        return u_email;
    }
    public String getU_picture() {return u_picture;}

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setU_name(String u_name) {
        this.u_name = u_name;
    }
    public void setU_sex(String u_sex) { this.u_sex = u_sex; }

    public void setU_password(String u_password) {
        this.u_password = u_password;
    }

    public void setO_phone(String O_phone) {
        this.O_phone = O_phone;
    }

    public void setU_birthday(String u_birthday) {
        this.u_birthday = u_birthday;
    }

    public void setU_email(String u_email) {
        this.u_email = u_email;
    }
    public void setU_picture(String u_picture) {this.u_picture = u_picture; }
}
