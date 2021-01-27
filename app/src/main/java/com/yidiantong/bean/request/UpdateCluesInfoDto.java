package com.yidiantong.bean.request;

import java.util.ArrayList;
import java.util.List;

public class UpdateCluesInfoDto {
    private String cluesId;
    private String name;
    private String gender;
    private String industry;
    private String birthday;
    private String phoneNumber;
    private String description;
    private String wechatId;
    private List<String> tags = new ArrayList<>();

    public void setCluesId(String cluesId) {
        this.cluesId = cluesId;
    }

    public String getCluesId() {
        return this.cluesId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getGender() {
        return this.gender;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getIndustry() {
        return this.industry;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getBirthday() {
        return this.birthday;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }

    public void setWechatId(String wechatId) {
        this.wechatId = wechatId;
    }

    public String getWechatId() {
        return this.wechatId;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public List<String> getTags() {
        return this.tags;
    }

    @Override
    public String toString() {
        return "UpdateCluesInfoDto{" +
                "cluesId='" + cluesId + '\'' +
                ", name='" + name + '\'' +
                ", gender='" + gender + '\'' +
                ", industry='" + industry + '\'' +
                ", birthday='" + birthday + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", description='" + description + '\'' +
                ", wechatId='" + wechatId + '\'' +
                ", tags=" + tags +
                '}';
    }
}
