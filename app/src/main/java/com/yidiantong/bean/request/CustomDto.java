package com.yidiantong.bean.request;

import java.util.ArrayList;
import java.util.List;

public class CustomDto {

    private List<String> gender = new ArrayList<>();
    private List<String> industry = new ArrayList<>();


    public List<String> getGender() {
        return gender;
    }

    public void setGender(List<String> gender) {
        this.gender = gender;
    }

    public List<String> getIndustry() {
        return industry;
    }

    public void setIndustry(List<String> industry) {
        this.industry = industry;
    }

    @Override
    public String toString() {
        return "CustomDto{" +
                "gender=" + gender +
                ", industry=" + industry +
                '}';
    }
}