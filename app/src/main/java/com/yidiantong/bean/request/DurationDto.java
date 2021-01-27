package com.yidiantong.bean.request;

public class DurationDto {

    private String duration;

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "DurationDto{" +
                "duration='" + duration + '\'' +
                '}';
    }
}
