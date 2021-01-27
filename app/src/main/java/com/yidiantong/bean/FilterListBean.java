package com.yidiantong.bean;

import java.util.ArrayList;
import java.util.List;

public class FilterListBean {

    private List<NameBean> customerIndustry = new ArrayList<>();
    private List<NameBean> otherTags = new ArrayList<>();
    private List<NameBean> customerGender = new ArrayList<>();
    private List<NameBean> customerIntention = new ArrayList<>();
    private List<NameBean> customerSentiment = new ArrayList<>();


    public List<NameBean> getCustomerIndustry() {
        return customerIndustry;
    }

    public void setCustomerIndustry(List<NameBean> customerIndustry) {
        this.customerIndustry = customerIndustry;
    }

    public List<NameBean> getOtherTags() {
        return otherTags;
    }

    public void setOtherTags(List<NameBean> otherTags) {
        this.otherTags = otherTags;
    }

    public List<NameBean> getCustomerGender() {
        return customerGender;
    }

    public void setCustomerGender(List<NameBean> customerGender) {
        this.customerGender = customerGender;
    }

    public List<NameBean> getCustomerIntention() {
        return customerIntention;
    }

    public void setCustomerIntention(List<NameBean> customerIntention) {
        this.customerIntention = customerIntention;
    }

    public List<NameBean> getCustomerSentiment() {
        return customerSentiment;
    }

    public void setCustomerSentiment(List<NameBean> customerSentiment) {
        this.customerSentiment = customerSentiment;
    }
}
