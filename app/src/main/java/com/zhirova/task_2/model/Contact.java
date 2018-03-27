package com.zhirova.task_2.model;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Contact {

    private String id;
    private String name;
    private List<String> phone;
    private List<String> email;


    public Contact(String id, String name, List<String> phone, List<String> email) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
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


    public List<String> getEmail() {
        return email;
    }


    public void setEmail(List<String> email) {
        this.email = email;
    }


}
