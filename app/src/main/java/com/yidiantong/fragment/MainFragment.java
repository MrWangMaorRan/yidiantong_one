package com.yidiantong.fragment;

import com.yidiantong.R;
import com.yidiantong.util.log.LogUtils;

public class MainFragment extends LazyLoadFragment {
    @Override
    protected int setContentView() {
        return R.layout.fragment_main;
    }

    @Override
    protected void lazyLoad() {
        LogUtils.i(TAG, "MainFragment " + "已经初始并已经显示给用户可以加载数据\" : \"没有初始化不能加载数据");
    }

    @Override
    protected void stopLoad() {
        LogUtils.i(TAG, "MainFragment" + "已经对用户不可见，可以停止加载数据");
    }
}
