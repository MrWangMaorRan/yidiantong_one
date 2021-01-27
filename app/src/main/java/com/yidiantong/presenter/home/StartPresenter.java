package com.yidiantong.presenter.home;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Button;
import android.widget.ImageView;

import com.yidiantong.R;
import com.yidiantong.model.biz.home.IStart;
import com.yidiantong.util.PermissinsUtils;
import com.yidiantong.view.login.LoginActivity;

public class StartPresenter {

    public final static int LOGIN_REQUEST_CODE = 0X01;
    private Context mContext;
    private IStart iStart;


    public StartPresenter(Context mContext, IStart iStart) {
        this.mContext = mContext;
        this.iStart = iStart;
    }

    /**
     * 跳转到登录
     */
    public void goToLogin() {
        Intent intent = new Intent(mContext, LoginActivity.class);
        ((Activity) mContext).startActivityForResult(intent, LOGIN_REQUEST_CODE);
    }

    /**
     * 选择是否同意用户协议
     */
    public void checkedAgreeAgreement(ImageView imageView, boolean isAgree) {
        if (isAgree) {
            imageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_checked_blue));
        } else {
            imageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_checked_no_gray));
        }
    }

}
