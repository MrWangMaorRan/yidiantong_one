package com.yidiantong.bean;

import java.io.Serializable;

public class FollowRecordsBean implements Serializable {

    private String remark;
    private String title;
    private String type;
    private String time;
    private String soundRecordingUrl;
    private int callDuration;

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSoundRecordingUrl() {
        return soundRecordingUrl;
    }

    public void setSoundRecordingUrl(String soundRecordingUrl) {
        this.soundRecordingUrl = soundRecordingUrl;
    }


    public int getCallDuration() {
        return callDuration;
    }

    public void setCallDuration(int callDuration) {
        this.callDuration = callDuration;
    }
}
