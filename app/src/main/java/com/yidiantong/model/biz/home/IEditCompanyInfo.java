package com.yidiantong.model.biz.home;

public interface IEditCompanyInfo {
    void setTagList();

    void onGenderResult(String genderStr);

    void onChooseIndustryResult(String industryStr);

    void onChooseBirthdayResult(String birthdayStr);
}
