package com.yidiantong.model.biz.home;

public interface ICalling {

    void saveAndHangUpCallingSuccessResult();

    void saveAndHangUpCallingFailureResult();

    void setTagsList();

    void callAllResult();

    void callNextVisible();

    void callOtherPhoneText(String phoneNum);

    void callNextNoHangUp();

    void onlyHangUp();

    void setCallStateText(String callStateStr);

    void setIsAutoCallNext(boolean isAutoCallNext);

    void setShowEmptyText();
}
