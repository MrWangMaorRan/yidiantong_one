package com.yidiantong.model.biz;

import com.yidiantong.bean.UserInfoBean;

public interface IMain {
    //
    void industryChooseResult(String industryStr);

    void addressChooseResult(String AddressStr);

    void sortingChooseResult(String sortingStr);

    void screeningChooseResult(String screeningStr);

    // 获取个人信息成功
    void onUserInfoResult();

    // 获取个人信息成功
    void onTalkTimeResult();

    // 获取列表成功
    void onCluesListResult();

    void refreshComplete();

    void loadComplete();

    void refreshKeyboardInput(String inputText);

    void hideKeyboard(boolean isVisible);
}
