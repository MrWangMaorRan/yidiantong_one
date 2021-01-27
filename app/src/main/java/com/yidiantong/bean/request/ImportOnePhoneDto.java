package com.yidiantong.bean.request;

public class ImportOnePhoneDto {

    private String phone;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "ImportOnePhoneDto{" +
                "phone='" + phone + '\'' +
                '}';
    }
}
