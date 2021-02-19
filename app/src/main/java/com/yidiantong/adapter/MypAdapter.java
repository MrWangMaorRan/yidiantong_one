package com.yidiantong.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

public class MypAdapter extends FragmentPagerAdapter {
    private List<Fragment> Mfragmentlist;
    //添加标题的集合
    private List<String> mTilteLis;
    public MypAdapter(@NonNull FragmentManager fm, List<Fragment> fragmentList, List<String> tilteLis) {
        super(fm);
        Mfragmentlist=fragmentList;
        mTilteLis = tilteLis;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return Mfragmentlist.get(position);
    }

    @Override
    public int getCount() {
        return Mfragmentlist.size();
    }
    //获取标题
    @Override
    public CharSequence getPageTitle(int position) {

        return mTilteLis.get(position);
    }
}
