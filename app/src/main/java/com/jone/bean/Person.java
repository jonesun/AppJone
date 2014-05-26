package com.jone.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;


/**
 * Created by jone_admin on 14-2-27.
 * Android序列化对象主要有两种方法,实现Serializable接口、或者实现Parcelable接口.
 * Serializable接口是Java SE本身就支持的,而Parcelable是Android特有的功能,效率比实现Serializable接口高,而且还可以用在IPC中。
 * s实现Parcelable接口序列化对象的步骤:
 * 1.声明实现接口Parcelable
 * 2.实现Parcelable的方法writeToParcel,将你的对象序列化为一个Parcel对象
 * 3.实例化静态内部对象CREATOR实现接口Parcelable.Creator
 */
public class Person implements Parcelable {
    private int id;
    private String name;
    private Date date;

    public Person(int id, String name, long time){
        this.id = id;
        this.name = name;
        this.date = new Date(time);
    }

    public Person(Parcel parcel){
        this.id = parcel.readInt();
        this.name = parcel.readString();
        this.date = new Date(parcel.readLong());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * 实现Parcelable的方法writeToParcel，将Person序列化为一个Parcel对象
     * @param parcel
     * @param i
     */
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeLong(date.getTime());
    }

    public static final Creator<Person> CREATOR = new Creator<Person>() {
        /**
         * 将Parcel对象反序列化为ParcelableDate
         * 实现从source创建出JavaBean实例的功能
         * @param parcel
         * @return
         */
        @Override
        public Person createFromParcel(Parcel parcel) {
            return new Person(parcel);
        }

        @Override
        public Person[] newArray(int size) {
            return new Person[size];
        }
    };

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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
