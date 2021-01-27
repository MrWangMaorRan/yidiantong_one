package com.yidiantong.bean.request;

import com.yidiantong.bean.ContactBean;

import java.util.ArrayList;
import java.util.List;

public class ImportAddressBookDto {
    private List<ContactBean> phoneList = new ArrayList<>();

    public List<ContactBean> getPhoneList() {
        return phoneList;
    }

    public void setPhoneList(List<ContactBean> phoneList) {
        this.phoneList = phoneList;
    }
}
