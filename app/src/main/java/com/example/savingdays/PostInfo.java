package com.example.savingdays;

import com.google.type.Date;

import java.util.ArrayList;

public class PostInfo {
    private String title;
    private String contents;
    private String publisher;
    private Date createdAt;
    private String id;


    public PostInfo(String title, String Contents, String publisher, String id) {
        this.title = title;
        this.contents = Contents;
        this.publisher = publisher;
        this.id = id;
    }

//    public PostInfo(String title, String Contents, String publisher, Date createdAt) {
//        this.title = title;
//        this.contents = Contents;
//        this.publisher = publisher;
//       // this.id = id;
//        this.createdAt = createdAt;
//    }

    public PostInfo(String title, String Contents, String publisher) {
        this.title = title;
        this.contents = Contents;
        this.publisher = publisher;
    }



    //제목
    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    //내용
    public String getContents() {
        return this.contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    //게시자
    public String getpublisher() {
        return this.publisher;
    }

    public void setpublisher(String publisher) {
        this.publisher = publisher;
    }

    //아이디
    public String getId() {return this.id; }
    public void setId(String id) {this.id = id; }


    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date date) {
        this.createdAt = createdAt;
    }
}