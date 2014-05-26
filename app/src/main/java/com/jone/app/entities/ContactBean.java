package com.jone.app.entities;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Created by jone_admin on 2014/4/15.
 */
public class ContactBean implements Serializable {
    private long id;
    private String name;
    private String number;
    private Bitmap photo;
    public ContactBean(long id, String name, String number, Bitmap photo){
        this.setId(id);
        this.setName(name);
        this.setNumber(number);
        this.setPhoto(photo);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Bitmap getPhoto() {
        return photo;
    }

    public void setPhoto(Bitmap photo) {
        this.photo = photo;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
