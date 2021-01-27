package com.yidiantong.bean.request;

public class SortDto {

    private String allocateTime; // 分配 时间从晚到早 desc  分配时间从早到晚 asc
    private String callTime; // 最近通话 desc

    public String getAllocateTime() {
        return allocateTime;
    }

    public void setAllocateTime(String allocateTime) {
        this.allocateTime = allocateTime;
    }

    public String getCallTime() {
        return callTime;
    }

    public void setCallTime(String callTime) {
        this.callTime = callTime;
    }

    @Override
    public String toString() {
        return "SortDto{" +
                "allocateTime='" + allocateTime + '\'' +
                ", callTime='" + callTime + '\'' +
                '}';
    }
}
