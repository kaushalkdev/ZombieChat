package com.example.zombiechat.account;

public class SingleUserModel {

    private String image;
    private String name;
    private String sex;
    private String status;
    private String userid;

    public SingleUserModel() {
    }

    public SingleUserModel(String image, String name, String sex, String status, String userid) {
        this.image = image;
        this.name = name;
        this.sex = sex;
        this.status = status;
        this.userid = userid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
}
