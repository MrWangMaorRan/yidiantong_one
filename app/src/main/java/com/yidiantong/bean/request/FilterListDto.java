package com.yidiantong.bean.request;

import java.util.ArrayList;
import java.util.List;

public class FilterListDto {

    private List<String> area = new ArrayList<>();
    private String industry;

    public List<String> getArea() {
        return area;
    }

    public void setArea(List<String> area) {
        this.area = area;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }
}
