package com.zhirova.task_2.model;


public class Contact {

    private String id;
    private String name;
    private String phone;
    private String mail;


    public Contact(String id, String name, String phone, String mail) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.mail = mail;
    }


    public String getId() {
        return id;
    }


    public void setId(String id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public String getPhone() {
        return phone;
    }


    public void setPhone(String phone) {
        this.phone = phone;
    }


    public String getMail() {
        return mail;
    }


    public void setMail(String mail) {
        this.mail = mail;
    }


}
