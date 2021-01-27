package com.yidiantong.view.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.yidiantong.R;
import com.yidiantong.app.MyLinPhoneManager;
import com.yidiantong.base.BaseActivity;
import com.yidiantong.bean.CluesListBean;
import com.yidiantong.model.biz.home.ICluesDetail;
import com.yidiantong.presenter.home.CluesDetailPresenter;
import com.yidiantong.util.ToastUtils;
import com.yidiantong.widget.FlowLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CluesDetailActivity extends BaseActivity implements ICluesDetail {


    @BindView(R.id.iv_left)
    ImageView ivLeft;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.btn_right)
    Button btnRight;
    @BindView(R.id.iv_right)
    ImageView ivRight;
    @BindView(R.id.iv_edit)
    ImageView ivEdit;
    @BindView(R.id.tv_num_attribution_place)
    TextView tvNumAttributionPlace;
    @BindView(R.id.tv_gender)
    TextView tvGender;
    @BindView(R.id.tv_phone_num)
    TextView tvPhoneNum;
    @BindView(R.id.tv_we_chat_num)
    TextView tvWeChatNum;
    @BindView(R.id.tv_industry)
    TextView tvIndustry;
    @BindView(R.id.tv_birth_date)
    TextView tvBirthDate;
    @BindView(R.id.tv_describe)
    TextView tvDescribe;
    @BindView(R.id.xrv_communication_record)
    XRecyclerView xrvCommunicationRecord;
    @BindView(R.id.ll_call_phone)
    LinearLayout llCallPhone;
    @BindView(R.id.flw_tag_list)
    FlowLayout flwTagList;
    @BindView(R.id.tv_phone_title)
    TextView tvPhoneTitle;

    private CluesDetailPresenter cluesDetailPresenter;
    private String cluesId;
    private String industry;
    private String area;
    private List<CluesListBean> cluesList = new ArrayList<>();
    private int callPosition;

    @Override
    public void getIntentData() {
        cluesId = getIntent().getStringExtra("cluesId");
        industry = getIntent().getStringExtra("industry");
        area = getIntent().getStringExtra("area");
        cluesList = (List<CluesListBean>) getIntent().getSerializableExtra("cluesList");
        callPosition = getIntent().getIntExtra("callPosition", -1);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_clues_detail;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        ButterKnife.bind(this);

        cluesDetailPresenter = new CluesDetailPresenter(this, this);
        tvTitle.setText("线索详情");

        // 去掉刷新和加载
        xrvCommunicationRecord.setPullRefreshEnabled(false);
        xrvCommunicationRecord.setLoadingMoreEnabled(false);

        // 线索id
        cluesDetailPresenter.setCluesId(cluesId);
        // 适配器
        cluesDetailPresenter.initAdapter(xrvCommunicationRecord);
        // 获取数据
        cluesDetailPresenter.cluesInfo();
        // 赋值列表和位置
        cluesDetailPresenter.setCluesList(cluesList);
        cluesDetailPresenter.setCallPosition(callPosition);
    }

    @Override
    protected void onResume() {
        super.onResume();
        cluesDetailPresenter.talkTimeInfo();
    }

    @Override
    protected void onStop() {
        super.onStop();
        cluesDetailPresenter.stopAudio();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cluesDetailPresenter.stopAudio();
    }

    @Override
    protected void onPause() {
        super.onPause();
        cluesDetailPresenter.stopAudio();
    }

    @OnClick({R.id.iv_left, R.id.iv_edit, R.id.ll_call_phone})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_left:
                finish();
                break;
            case R.id.iv_edit:
                cluesDetailPresenter.goToEdit();
                break;
            case R.id.ll_call_phone:
//                ToastUtils.showToast(mContext, "拨打电话");
                cluesDetailPresenter.call(industry, area);
                break;
        }
    }

    @Override
    public void onCluesInfoResult() {
        // 赋值
        cluesDetailPresenter.cluesDetailSetText(tvPhoneTitle, flwTagList,
                tvNumAttributionPlace, tvGender, tvPhoneNum, tvWeChatNum,
                tvIndustry, tvBirthDate, tvDescribe);
        cluesDetailPresenter.setListData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        cluesDetailPresenter.onActivityResult(requestCode, resultCode, data);
    }
}