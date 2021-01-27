package com.yidiantong.bean;

import java.io.Serializable;

public class CluesListBean implements Serializable {

    //{
    //    "success":true,
    //    "code":200,
    //    "message":"successful",
    //    "data":[
    //        {
    //            "id":"1addb2df-e1c8-454a-ae72-b95ca118ea44",
    //            "number":"18877774444",
    //            "numberLocation":"广东深圳",
    //            "phoneNumber":"13113113113",
    //            "callTime":"2020-06-23 20:27:10",
    //            "status":"已拨打",
    //        }
    //    ]
    //}

    private String number;
    private String numberLocation ;
    private String id;
    private String phoneNumber;
    private String callTime;
    private String status;
    private boolean isCalled; // 是否拨打过

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getNumberLocation() {
        return numberLocation;
    }

    public void setNumberLocation(String numberLocation) {
        this.numberLocation = numberLocation;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCallTime() {
        return callTime;
    }

    public void setCallTime(String callTime) {
        this.callTime = callTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isCalled() {
        return isCalled;
    }

    public void setCalled(boolean called) {
        isCalled = called;
    }
}
