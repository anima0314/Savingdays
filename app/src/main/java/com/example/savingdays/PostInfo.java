package com.example.savingdays;

public class PostInfo {
    private String title;
    private String contents;
    private String publisher;

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




}