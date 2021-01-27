package com.yidiantong.bean.request;

public class CallRecordsDto {

    private String cluesId;
    private String callTime;
    private String callType;
    private String phoneNumber;
    private String remark;

    public String getCluesId() {
        return cluesId;
    }

    public void setCluesId(String cluesId) {
        this.cluesId = cluesId;
    }

    public String getCallTime() {
        return callTime;
    }

    public void setCallTime(String callTime) {
        this.callTime = callTime;
    }

    public String getCallType() {
        return callType;
    }

    public void setCallType(String callType) {
        this.callType = callType;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
