package com.yidiantong.view.company;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.yidiantong.R;
import com.yidiantong.base.BaseActivity;
import com.yidiantong.model.biz.company.ICompanyInfo;
import com.yidiantong.presenter.company.CompanyInfoPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CompanyInfoActivity extends BaseActivity implements ICompanyInfo {

    @BindView(R.id.iv_left)
    ImageView ivLeft;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.btn_right)
    Button btnRight;
    @BindView(R.id.iv_right)
    ImageView ivRight;
    @BindView(R.id.tv_company_name_title)
    TextView tvCompanyNameTitle;
    @BindView(R.id.tv_company_name)
    TextView tvCompanyName;
    @BindView(R.id.tv_company_address_title)
    TextView tvCompanyAddressTitle;
    @BindView(R.id.tv_company_address)
    TextView tvCompanyAddress;
    @BindView(R.id.tv_company_industry_title)
    TextView tvCompanyIndustryTitle;
    @BindView(R.id.tv_company_industry)
    TextView tvCompanyIndustry;
    @BindView(R.id.tv_employees_count_title)
    TextView tvEmployeesCountTitle;
    @BindView(R.id.tv_employees_count)
    TextView tvEmployeesCount;

    private CompanyInfoPresenter companyInfoPresenter;

    @Override
    public void getIntentData() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_company_info;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        ButterKnife.bind(this);

        tvTitle.setText("企业信息");

        companyInfoPresenter = new CompanyInfoPresenter(this, this);
        companyInfoPresenter.getEnterprisesInfo();
    }

    @OnClick({R.id.iv_left})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_left:
                finish();
                break;
        }
    }

    @Override
    public void onGetEnterprisesInfoResult() {
        companyInfoPresenter.EnterprisesInfoSetText(tvCompanyName, tvCompanyAddress, tvCompanyIndustry, tvEmployeesCount);
    }
}