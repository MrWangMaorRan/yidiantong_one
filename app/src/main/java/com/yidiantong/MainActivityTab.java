package com.yidiantong;

import android.os.Bundle;
import android.widget.RadioGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;

import com.yidiantong.adapter.ViewPagerAdapter;
import com.yidiantong.fragment.MainFragment;
import com.yidiantong.fragment.MineFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivityTab extends FragmentActivity {

    @BindView(R.id.vp_home)
    ViewPager vpHome;
    @BindView(R.id.rg_tab)
    RadioGroup rgTab;

    private ViewPagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_tab);
        ButterKnife.bind(this);

        initData();
    }

    private void initData() {
        final List<Fragment> fragments = new ArrayList<>();
        fragments.add(new MainFragment());
        fragments.add(new MineFragment());
        vpHome.setOffscreenPageLimit(0);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), fragments);
        vpHome.setAdapter(viewPagerAdapter);

        rgTab.setOnCheckedChangeListener(onCheckedChangeListener);
    }

    // 点击事件
    RadioGroup.OnCheckedChangeListener onCheckedChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int i) {
            vpHome.setCurrentItem(radioGroup.indexOfChild(radioGroup.findViewById(i)));
        }
    };
}