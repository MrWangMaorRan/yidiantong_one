package com.yidiantong.view.home;


import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.progressindicator.indicator.BallRotateIndicator;
import com.yidiantong.R;
import com.yidiantong.base.BaseActivity;
import com.yidiantong.bean.CluesDetailBean;
import com.yidiantong.model.biz.home.IEditCompanyInfo;
import com.yidiantong.presenter.home.EditCompanyInfoPresenter;
import com.yidiantong.util.HandlerUtils;
import com.yidiantong.widget.FlowLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditCompanyInfoActivity extends BaseActivity implements IEditCompanyInfo {

    @BindView(R.id.iv_left)
    ImageView ivLeft;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.btn_right)
    Button btnRight;
    @BindView(R.id.iv_right)
    ImageView ivRight;
    @BindView(R.id.et_virtual_number_display)
    EditText etVirtualNumberDisplay;
    @BindView(R.id.et_number_attribution_place)
    EditText etNumberAttributionPlace;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.ll_gender)
    LinearLayout llGender;
    @BindView(R.id.tv_gender)
    TextView tvGender;
    @BindView(R.id.tv_industry)
    TextView tvIndustry;
    @BindView(R.id.ll_industry)
    LinearLayout llIndustry;
    @BindView(R.id.tv_birthday)
    TextView tvBirthday;
    @BindView(R.id.ll_birthday)
    LinearLayout llBirthday;
    @BindView(R.id.et_we_chat_num)
    EditText etWeChatNum;
    @BindView(R.id.et_phone_num)
    EditText etPhoneNum;
    @BindView(R.id.et_describe)
    EditText etDescribe;
    @BindView(R.id.fl_tags)
    FlowLayout flTags;
    @BindView(R.id.btn_save)
    Button btnSave;

    private EditCompanyInfoPresenter editCompanyInfoPresenter;
    private CluesDetailBean cluesDetailBean;
    private String cluesId;

    @Override
    public void getIntentData() {
        cluesDetailBean = (CluesDetailBean) getIntent().getSerializableExtra("cluesDetailBean");
        cluesId = getIntent().getStringExtra("cluesId");
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_edit_company_info;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        tvTitle.setText("编辑信息");

        editCompanyInfoPresenter = new EditCompanyInfoPresenter(this, this);

        editCompanyInfoPresenter.setCluesDetailBean(cluesDetailBean, cluesId);
        // 初始化赋值
        editCompanyInfoPresenter.initSetData(etVirtualNumberDisplay, etNumberAttributionPlace,
                etName, tvGender, tvIndustry, tvBirthday, etWeChatNum, etPhoneNum, etDescribe, flTags);
    }

    @OnClick({R.id.iv_left, R.id.ll_industry, R.id.ll_gender, R.id.ll_birthday, R.id.btn_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_left:
                finish();
                break;
            case R.id.ll_gender:
                editCompanyInfoPresenter.showDialogGender();
                break;
            case R.id.ll_industry:
                editCompanyInfoPresenter.showIndustryDialog();
                break;
            case R.id.ll_birthday:
                editCompanyInfoPresenter.showBirthdayDialog();
                break;
            case R.id.btn_save:
                editCompanyInfoPresenter.setUpdateCluesParameter(etName, tvGender,
                        tvIndustry, tvBirthday, etPhoneNum, etWeChatNum, etDescribe);
                editCompanyInfoPresenter.updateCluesInfo();
                break;
        }
    }

    @Override
    public void setTagList() {
        editCompanyInfoPresenter.addTagsView(flTags);
    }

    @Override
    public void onGenderResult(String genderStr) {
        HandlerUtils.setText(tvGender, genderStr);
    }

    @Override
    public void onChooseIndustryResult(String industryStr) {
        HandlerUtils.setText(tvIndustry, industryStr);
    }

    @Override
    public void onChooseBirthdayResult(String birthdayStr) {
        HandlerUtils.setText(tvBirthday, birthdayStr);
    }
}