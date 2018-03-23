package com.zhirova.task_2.model;


import java.util.ArrayList;
import java.util.List;

public class Contact {

    private String id;
    private String name;
    private List<String> phone;
    private String email;


    public Contact(String id, String name, List<String> phone, String mail) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = mail;
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


    public List<String> getPhone() {
        return phone;
    }


    public void setPhone(List<String> phone) {
        this.phone = phone;
    }


    public String getEmail() {
        return email;
    }


    public void setEmail(String email) {
        this.email = email;
    }


}
