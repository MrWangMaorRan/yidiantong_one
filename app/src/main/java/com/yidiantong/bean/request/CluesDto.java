package com.yidiantong.bean.request;


import java.util.ArrayList;
import java.util.List;

public class CluesDto {

    private List<String> area = new ArrayList<>();
    private String industry;
    private SortDto sort;
    private String normal = "default";
    private int page;
    private List<String> customerIntention = new ArrayList<>();
    private List<String> otherTags = new ArrayList<>();
    private List<String> customerSentiment = new ArrayList<>();
    private CustomDto custom = new CustomDto();

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

    public SortDto getSort() {
        return sort;
    }

    public void setSort(SortDto sort) {
        this.sort = sort;
    }

    public String getNormal() {
        return normal;
    }

    public void setNormal(String normal) {
        this.normal = normal;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<String> getCustomerIntention() {
        return customerIntention;
    }

    public void setCustomerIntention(List<String> customerIntention) {
        this.customerIntention = customerIntention;
    }

    public List<String> getOtherTags() {
        return otherTags;
    }

    public void setOtherTags(List<String> otherTags) {
        this.otherTags = otherTags;
    }

    public List<String> getCustomerSentiment() {
        return customerSentiment;
    }

    public void setCustomerSentiment(List<String> customerSentiment) {
        this.customerSentiment = customerSentiment;
    }

    public CustomDto getCustom() {
        return custom;
    }

    public void setCustom(CustomDto custom) {
        this.custom = custom;
    }

    @Override
    public String toString() {
        return "CluesDto{" +
                "area=" + area +
                ", industry='" + industry + '\'' +
                ", sort=" + sort +
                ", normal='" + normal + '\'' +
                ", page=" + page +
                ", customerIntention=" + customerIntention +
                ", otherTags=" + otherTags +
                ", customerSentiment=" + customerSentiment +
                ", custom=" + custom +
                '}';
    }
}
