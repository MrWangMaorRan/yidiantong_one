package com.yidiantong.bean;

import java.io.Serializable;

public class UserInfoBean implements Serializable {

    private String title;
    private String path;
    private String phoneNumber;
    private SipAccountBean sip;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public SipAccountBean getSip() {
        return sip;
    }

    public void setSip(SipAccountBean sip) {
        this.sip = sip;
    }
}
