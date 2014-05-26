package com.jone.bean;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by jone_admin on 2014/3/25.
 */
@DatabaseTable
public class Note implements Serializable{
    @DatabaseField(generatedId = true)
    private Integer id;

    @DatabaseField
    private String title;

    @DatabaseField(dataType = DataType.LONG_STRING)
    private String content;

    @DatabaseField(dataType = DataType.DATE)
    private Date createDate;

    @DatabaseField(dataType = DataType.DATE)
    private Date updateDate;

    @DatabaseField(defaultValue = "0")
    private int deleteSign; //0代表正常状态、1代表被逻辑删除

    public Note(){}

    public Note(String title, String content){
        this.setTitle(title);
        this.setContent(content);
        createDate = new Date();
        setUpdateDate(new Date());
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public int getDeleteSign() {
        return deleteSign;
    }

    public void setDeleteSign(int deleteSign) {
        this.deleteSign = deleteSign;
    }

    public Integer getId() {
        return id;
    }
}
