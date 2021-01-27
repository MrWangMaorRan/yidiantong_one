package com.yidiantong.bean.request;

import java.util.Arrays;
import java.util.List;

public class RequestCluesList {

    private String[] gender = new String[]{};
    private String[] intention = new String[]{};
    private String[] city = new String[]{};
    private String[] industry = new String[]{};
    private String[] other = new String[]{};

    public String[] getGender() {
        return gender;
    }

    public void setGender(String[] gender) {
        this.gender = gender;
    }

    public String[] getIntention() {
        return intention;
    }

    public void setIntention(String[] intention) {
        this.intention = intention;
    }

    public String[] getCity() {
        return city;
    }

    public void setCity(String[] city) {
        this.city = city;
    }

    public String[] getIndustry() {
        return industry;
    }

    public void setIndustry(String[] industry) {
        this.industry = industry;
    }

    public String[] getOther() {
        return other;
    }

    public void setOther(String[] other) {
        this.other = other;
    }

}
