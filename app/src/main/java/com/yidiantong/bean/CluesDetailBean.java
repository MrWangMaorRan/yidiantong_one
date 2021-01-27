package com.yidiantong.bean;

import java.io.Serializable;
import java.util.List;

public class CluesDetailBean implements Serializable {

    private String number;
    private List<String> tags;
    private String numberLocation;
    private String name;
    private String gender;
    private String phoneNumber;
    private String wechatId;
    private String birthday;
    private String industry;
    private String description;
    private List<FollowRecordsBean> followRecords;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getNumberLocation() {
        return numberLocation;
    }

    public void setNumberLocation(String numberLocation) {
        this.numberLocation = numberLocation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getWechatId() {
        return wechatId;
    }

    public void setWechatId(String wechatId) {
        this.wechatId = wechatId;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<FollowRecordsBean> getFollowRecords() {
        return followRecords;
    }

    public void setFollowRecords(List<FollowRecordsBean> followRecords) {
        this.followRecords = followRecords;
    }
}
