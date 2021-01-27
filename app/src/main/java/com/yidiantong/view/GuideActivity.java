package com.yidiantong.view;

import android.os.Bundle;

import androidx.annotation.NonNull;

import com.yidiantong.R;
import com.yidiantong.base.BaseActivity;
import com.yidiantong.model.biz.IGudie;
import com.yidiantong.presenter.GuidePresenter;

import butterknife.ButterKnife;

public class GuideActivity extends BaseActivity implements IGudie {

    private GuidePresenter guidePresenter;

    @Override
    public void getIntentData() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_guide;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        guidePresenter = new GuidePresenter(this, this);
        guidePresenter.startActivity();
    }

}