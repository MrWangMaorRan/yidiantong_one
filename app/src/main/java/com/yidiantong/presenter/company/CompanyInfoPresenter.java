package com.yidiantong.presenter.company;

import android.content.Context;
import android.widget.TextView;

import com.yidiantong.bean.EnterprisesInfoBean;
import com.yidiantong.model.biz.company.ICompanyInfo;
import com.yidiantong.model.impl.company.CompanyInfoImpl;

import java.util.HashMap;

public class CompanyInfoPresenter implements CompanyInfoImpl.OnCallBackListener {

    private Context mContext;
    private CompanyInfoImpl companyInfoImpl;
    private ICompanyInfo iCompanyInfo;
    private EnterprisesInfoBean enterprisesInfoBean;

    public CompanyInfoPresenter(Context mContext, ICompanyInfo iCompanyInfo) {
        this.mContext = mContext;
        this.iCompanyInfo = iCompanyInfo;
        companyInfoImpl = new CompanyInfoImpl();
    }

    /**
     * 获取企业基本信息
     */
    public void getEnterprisesInfo() {
        companyInfoImpl.getEnterprisesInfo(mContext, new HashMap<>(), this);
    }

    public void EnterprisesInfoSetText(TextView tvCompanyName, TextView tvCompanyArea, TextView tvCompanyIndustry, TextView tvEmployeesCount) {
        if (enterprisesInfoBean != null) {
            tvCompanyName.setText(enterprisesInfoBean.getTitle());
            tvCompanyArea.setText(enterprisesInfoBean.getProvince() + enterprisesInfoBean.getCity());
            tvCompanyIndustry.setText(enterprisesInfoBean.getIndustry());
            tvEmployeesCount.setText(enterprisesInfoBean.getNumberOfEmployees());
        }
    }

    @Override
    public void onGetEnterprisesInfoSuccess(EnterprisesInfoBean enterprisesInfoBean) {
        this.enterprisesInfoBean = enterprisesInfoBean;
        iCompanyInfo.onGetEnterprisesInfoResult();
    }

    @Override
    public void onGetEnterprisesInfoFailure(String msg) {

    }
}
