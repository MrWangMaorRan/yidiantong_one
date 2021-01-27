package com.yidiantong.view.mine;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.yidiantong.R;
import com.yidiantong.base.BaseActivity;
import com.yidiantong.bean.UserInfoBean;
import com.yidiantong.model.biz.home.IEditCompanyInfo;
import com.yidiantong.model.biz.mine.IEditMineInfo;
import com.yidiantong.presenter.mine.EditMineInfoPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditMineInfoActivity extends BaseActivity implements IEditMineInfo {

    @BindView(R.id.iv_left)
    ImageView ivLeft;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.btn_right)
    Button btnRight;
    @BindView(R.id.iv_right)
    ImageView ivRight;
    @BindView(R.id.et_edit_str)
    EditText etEditStr;

    private String title;
    private UserInfoBean userInfoBean;
    private EditMineInfoPresenter editMineInfoPresenter;
    private String inputText;

    @Override
    public void getIntentData() {
        title = getIntent().getStringExtra("title");
        userInfoBean = (UserInfoBean) getIntent().getSerializableExtra("userInfoBean");
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_edit_mine_info;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        ButterKnife.bind(this);

        editMineInfoPresenter = new EditMineInfoPresenter(this, this);

        tvTitle.setText(title);
        if (userInfoBean != null) {
            inputText = userInfoBean.getTitle();
        }
        editMineInfoPresenter.init(btnRight, etEditStr, inputText);
        editMineInfoPresenter.setUserInfoBean(userInfoBean);
        etEditStr.addTextChangedListener(textWatcher);
    }


    @OnClick({R.id.iv_left, R.id.btn_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_left:
                finish();
                break;
            case R.id.btn_right:
                editMineInfoPresenter.userInfoUpdate(inputText);
                break;
        }
    }

    @Override
    public void onUserInfoUpdateResult() {
        this.finish();
    }

    /**
     * 输入监听
     */
    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            inputText = s.toString().trim();
            editMineInfoPresenter.setHeadBtnBg(btnRight, inputText);
        }
    };
}