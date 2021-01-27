package com.yidiantong.view.mine;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.yidiantong.R;
import com.yidiantong.base.BaseActivity;
import com.yidiantong.bean.UserInfoBean;
import com.yidiantong.model.biz.mine.IMine;
import com.yidiantong.presenter.mine.MinePresenter;
import com.yidiantong.util.PermissinsUtils;
import com.yidiantong.widget.RoundImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MineInfoActivity extends BaseActivity implements IMine {


    @BindView(R.id.iv_left)
    ImageView ivLeft;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.btn_right)
    Button btnRight;
    @BindView(R.id.iv_right)
    ImageView ivRight;
    @BindView(R.id.iv_enter)
    ImageView ivEnter;
    @BindView(R.id.rl_mine_info_head)
    RelativeLayout rlMineInfoHead;
    @BindView(R.id.tv_name_title)
    TextView tvNameTitle;
    @BindView(R.id.iv_enter_name)
    ImageView ivEnterName;
    @BindView(R.id.rl_mine_info_name)
    RelativeLayout rlMineInfoName;
    @BindView(R.id.tv_phone_title)
    TextView tvPhoneTitle;
    @BindView(R.id.rl_mine_info_phone)
    RelativeLayout rlMineInfoPhone;
    @BindView(R.id.riv_head_img)
    RoundImageView rivHeadImg;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_phone_num)
    TextView tvPhoneNum;

    private MinePresenter minePresenter;
    private UserInfoBean userInfoBean;


    @Override
    public void getIntentData() {
        userInfoBean = (UserInfoBean) getIntent().getSerializableExtra("userInfoBean");
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_mine_info;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        ButterKnife.bind(this);

        minePresenter = new MinePresenter(this, this);
        tvTitle.setText("个人信息");
        // 获取上个界面传递过来的数据
        minePresenter.setUserInfoBean(userInfoBean);
        // 赋值显示
        minePresenter.userInfoSetText(rivHeadImg, tvName, tvPhoneNum);
    }

    @OnClick({R.id.iv_left, R.id.rl_mine_info_head, R.id.rl_mine_info_name, R.id.rl_mine_info_phone})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_left:
                finish();
                break;
            case R.id.rl_mine_info_head:
                minePresenter.editHeadImg();
                break;
            case R.id.rl_mine_info_name:
                minePresenter.goToEditName();
                break;
            case R.id.rl_mine_info_phone:
//                ToastUtils.showToast(this, "phone");
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (PermissinsUtils.isPermissions(permissions, grantResults)) {
            minePresenter.onRequestPermissionsResult(requestCode);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        minePresenter.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onUserInfoUpdateResult() {
        // 赋值显示
        minePresenter.userInfoSetText(rivHeadImg, tvName, tvPhoneNum);
    }
}