package com.example.savingdays;

import java.util.ArrayList;
import java.util.Date;

public class PostInfo {
    private String title;
    private String contents;
    private String publisher;
    private String id;
    private Date createdAt;

/*
    public PostInfo(String title, String Contents, String publisher, String id) {
        this.title = title;
        this.contents = Contents;
        this.publisher = publisher;
        this.id = id;
    }
*/
    public PostInfo(String title, String Contents, String publisher, String id, Date createdAt) {
        this.title = title;
        this.contents = Contents;
        this.publisher = publisher;
        this.id = id;
        this.createdAt = createdAt;
    }

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

    //날짜
    public Date getCreatedAt() {
        return this.createdAt;
    }
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}