package com.zhirova.task_2.model;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Contact implements Parcelable {

    private String id;
    private String name;
    private List<String> phones = new ArrayList<>();
    private List<String> emails = new ArrayList<>();


    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Contact createFromParcel(Parcel in) {
            return new Contact(in);
        }

        public Contact[] newArray(int size) {
            return new Contact[size];
        }
    };


    public Contact(String id, String name) {
        this.id = id;
        this.name = name;
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


    public List<String> getPhones() {
        return phones;
    }


    public void setPhones(List<String> phones) {
        this.phones.clear();
        if (phones != null){
            this.phones.addAll(phones);
        }
    }


    public List<String> getEmails() {
        return emails;
    }


    public void setEmails(List<String> emails) {
        this.emails.clear();
        if (emails != null){
            this.emails.addAll(emails);
        }
    }


    public Contact(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
    }


    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
    }


    @Override
    public String toString() {
        return "Contact{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }


}
