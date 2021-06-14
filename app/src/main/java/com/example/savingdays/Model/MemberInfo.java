package com.example.savingdays.Model;

public class MemberInfo {
    private String name;
    private String phoneNumber;
    private String birthDay;
    private String nickName;

    public MemberInfo(String name, String phoneNumber, String birthDay, String nickName) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.birthDay = birthDay;
        this.nickName = nickName;
    }

    //이름
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }

    //전화번호
    public String getPhoneNumber() {
        return this.phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    //생일
    public String getBirthDay() {
        return this.birthDay;
    }
    public void setBirthDay(String birthDay) {
        this.birthDay = birthDay;
    }

    //닉네임
    public String getNickName() {
        return this.nickName;
    }
    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}
