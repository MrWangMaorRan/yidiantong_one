package com.yidiantong.presenter.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnConfirmListener;
import com.lxj.xpopup.interfaces.SimpleCallback;
import com.yidiantong.R;
import com.yidiantong.adapter.CommunicateRecordListAdapter;
import com.yidiantong.app.MyLinPhoneManager;
import com.yidiantong.bean.CluesDetailBean;
import com.yidiantong.bean.CluesListBean;
import com.yidiantong.bean.FollowRecordsBean;
import com.yidiantong.bean.TalkTimeInfoBean;
import com.yidiantong.bean.request.CallRecordsDto;
import com.yidiantong.model.biz.home.ICluesDetail;
import com.yidiantong.model.impl.home.CluesDetailImpl;
import com.yidiantong.util.DensityUtils;
import com.yidiantong.util.StringUtils;
import com.yidiantong.util.SysUtil;
import com.yidiantong.util.TimeFormat;
import com.yidiantong.util.ToastUtils;
import com.yidiantong.util.Utils;
import com.yidiantong.util.log.LogUtils;
import com.yidiantong.view.home.CallingActivity;
import com.yidiantong.view.home.EditCompanyInfoActivity;
import com.yidiantong.widget.FlowLayout;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CluesDetailPresenter implements CluesDetailImpl.OnCallBackListener {

    private final static int REQUEST_CODE_EDIT_CLUES = 0xaa;
    private final static int REQUEST_CODE_CALL = 0xbb;
    private Context mContext;
    private ICluesDetail iCluesDetail;
    private CluesDetailImpl cluesDetailImpl;
    private CommunicateRecordListAdapter communicateRecordListAdapter;
    private CluesDetailBean cluesDetailBean;
    private String cluesId;
    private boolean isRefreshTag;
    private TalkTimeInfoBean talkTimeInfoBean;
    private List<CluesListBean> cluesList = new ArrayList<>();
    private int callPosition;

    public CluesDetailPresenter(Context mContext, ICluesDetail iCluesDetail) {
        this.mContext = mContext;
        this.iCluesDetail = iCluesDetail;
        cluesDetailImpl = new CluesDetailImpl();
        cluesDetailBean = new CluesDetailBean();
        talkTimeInfoBean = new TalkTimeInfoBean();
    }

    // 设值线索id
    public void setCluesId(String cluesId) {
        this.cluesId = cluesId;
    }

    /**
     * 跳转到编辑
     */
    public void goToEdit() {
        Intent intent = new Intent(mContext, EditCompanyInfoActivity.class);
        intent.putExtra("cluesDetailBean", cluesDetailBean);
        intent.putExtra("cluesId", cluesId);
        ((Activity) mContext).startActivityForResult(intent, REQUEST_CODE_EDIT_CLUES);
    }

    // 初始化适配器
    public void initAdapter(XRecyclerView xrvCommunicationRecord) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        xrvCommunicationRecord.setLayoutManager(layoutManager);
        communicateRecordListAdapter = new CommunicateRecordListAdapter(mContext);
        xrvCommunicationRecord.setAdapter(communicateRecordListAdapter);
    }

    // 停止语音播放
    public void stopAudio(){
        communicateRecordListAdapter.stopAudio();
    }

    // 线索详情赋值
    public void cluesDetailSetText(TextView tvPhoneTitle, FlowLayout flwTagList, TextView tvNumAttributionPlace,
                                   TextView tvGender, TextView tvPhoneNum, TextView tvWeChatNum, TextView tvIndustry,
                                   TextView tvBirthDate, TextView tvDescribe) {
        if (cluesDetailBean != null) {
            tvPhoneTitle.setText(cluesDetailBean.getNumber());
            addTagView(flwTagList);
            tvNumAttributionPlace.setText(cluesDetailBean.getNumberLocation());
            tvGender.setText(cluesDetailBean.getGender());
            tvPhoneNum.setText(cluesDetailBean.getPhoneNumber());
            tvWeChatNum.setText(cluesDetailBean.getWechatId());
            tvIndustry.setText(cluesDetailBean.getIndustry());
            tvBirthDate.setText(cluesDetailBean.getBirthday());
            tvDescribe.setText(cluesDetailBean.getDescription());
        }
    }

    // tag的addView
    public void addTagView(FlowLayout flwTagList) {
        if (isRefreshTag) {
            flwTagList.removeAllViews();
            isRefreshTag = false;
        }

        if (cluesDetailBean != null) {
            if (cluesDetailBean.getTags() != null) {
                // 去掉重复的标签
                cluesDetailBean.setTags(Utils.getRemoveList(cluesDetailBean.getTags()));
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        DensityUtils.dp2px(mContext, 18));
                flwTagList.setMaxLine(1);
                for (String tag : cluesDetailBean.getTags()) {
                    TextView textView = new TextView(mContext);
                    textView.setText(tag);
                    textView.setSingleLine();
                    textView.setTextSize(12);
                    textView.setPadding(DensityUtils.dp2px(mContext, 4), 0, DensityUtils.dp2px(mContext, 4), 0);
                    textView.setGravity(Gravity.CENTER);
                    textView.setTextColor(mContext.getResources().getColor(R.color.white));
                    textView.setBackground(mContext.getResources().getDrawable(R.drawable.bg_corner_4dp_empty_with_write_line)); //设置背景
                    layoutParams.rightMargin = DensityUtils.dp2px(mContext, 4);
                    textView.setLayoutParams(layoutParams);
                    flwTagList.addView(textView);
                }
            }
        }
    }

    // 赋值
    public void setListData() {
        if (communicateRecordListAdapter != null
                && cluesDetailBean != null
                && cluesDetailBean.getFollowRecords() != null) {
            communicateRecordListAdapter.setCommunicateRecordList(cluesDetailBean.getFollowRecords());
        }
    }

    // 设置拨号
    public void setCluesList(List<CluesListBean> cluesList) {
        this.cluesList = cluesList;
    }

    // 拨号的位置
    public void setCallPosition(int position) {
        callPosition = position;
    }

    // 拨打电话
    public void call(String industry, String area) {
        if (cluesDetailBean != null) {
            // 2020-11-28 去掉直拨电话
//            if (StringUtils.isNullOrBlank(cluesDetailBean.getPhoneNumber())) {
                if (talkTimeInfoBean != null && !StringUtils.isNullOrBlank(talkTimeInfoBean.getTrafficSurplus())) {
                    double time = Double.parseDouble(talkTimeInfoBean.getTrafficSurplus());
                    if (time > 60) {
                        Intent intent = new Intent(mContext, CallingActivity.class);
                        if(!StringUtils.isNullOrBlank(cluesDetailBean.getNumber())){
                            intent.putExtra("phoneNum", cluesDetailBean.getNumber());
                        }else if(!StringUtils.isNullOrBlank(cluesDetailBean.getPhoneNumber())){
                            intent.putExtra("phoneNum", cluesDetailBean.getPhoneNumber());
                        }else{
                            intent.putExtra("phoneNum", "");
                        }
                        intent.putExtra("cluesId", cluesId);
                        intent.putExtra("industry", industry);
                        intent.putExtra("area", area);
                        intent.putExtra("cluesList", (Serializable) cluesList);
                        intent.putExtra("callPosition", callPosition);
                        intent.putExtra("isInputCall", false);
                        ((Activity) mContext).startActivityForResult(intent, REQUEST_CODE_CALL);
                    } else {
                        ToastUtils.showToast(mContext, "话务为0不可拨打");
                    }
                } else {
                    ToastUtils.showToast(mContext, "话务时间有误");
                }
//            } else {
//                showCallDialog("拨打线索电话", cluesDetailBean.getPhoneNumber(), "拨打");
//            }
        }
    }

    // 获取线索详情信息
    public void cluesInfo() {
        Map<String, String> map = new HashMap<>();
        map.put("cluesId", cluesId);
        cluesDetailImpl.cluesInfo(mContext, map, this);
    }

    /**
     * 账号剩余通话时长
     */
    public void talkTimeInfo() {
        cluesDetailImpl.talkTimeInfo(mContext, new HashMap<>(), this);
    }

    /**
     * 更新通话记录
     */
    public void updateCallRecords() {
        // 添加记录
        CallRecordsDto callRecordsDto = new CallRecordsDto();
        callRecordsDto.setCluesId(cluesId);
        callRecordsDto.setCallType("普通电话");
        callRecordsDto.setPhoneNumber(cluesDetailBean.getPhoneNumber());
        callRecordsDto.setCallTime(TimeFormat.dateToStringYyyyMMddHHmmss(System.currentTimeMillis()));
        cluesDetailImpl.updateCallRecords(mContext, callRecordsDto, this);
    }

    @Override
    public void onCluesInfoSuccess(CluesDetailBean cluesDetailBean) {
        if (cluesDetailBean != null) {
            this.cluesDetailBean = cluesDetailBean;
            iCluesDetail.onCluesInfoResult();
        }
    }

    @Override
    public void onCluesInfoInfoFailure(String msg) {

    }

    @Override
    public void onUpdateCallRecordsSuccess() {
        LogUtils.i("MainPresenter", "更新通话记录成功");
    }

    @Override
    public void onUpdateCallRecordsFailure(String msg) {
        LogUtils.i("MainPresenter", "更新通话记录失败");
    }

    @Override
    public void onGetTalkTimeInfoSuccess(TalkTimeInfoBean talkTimeInfoBean) {
        this.talkTimeInfoBean = talkTimeInfoBean;
    }

    @Override
    public void onGetTalkTimeInfoFailure(String msg) {

    }

    // 回调
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_EDIT_CLUES:
                case REQUEST_CODE_CALL:
                    ((Activity) mContext).setResult(Activity.RESULT_OK);
                    cluesInfo();
                    isRefreshTag = true;
                    break;
            }
        }
    }

    /**
     * 弹框
     *
     * @param title
     * @param content
     * @param confirm
     */
    public void showCallDialog(String title, String content, String confirm) {
        new XPopup.Builder(mContext)
//                        .hasBlurBg(true)
//                         .dismissOnTouchOutside(false)
//                         .autoDismiss(false)
//                        .popupAnimation(PopupAnimation.NoAnimation)
//                        .isLightStatusBar(true)
                .setPopupCallback(new SimpleCallback() {
                    @Override
                    public void onCreated() {
                        Log.e("tag", "弹窗创建了");
                    }

                    @Override
                    public void onShow() {
                        Log.e("tag", "onShow");
                    }

                    @Override
                    public void onDismiss() {
                        Log.e("tag", "onDismiss");
                    }

                    //如果你自己想拦截返回按键事件，则重写这个方法，返回true即可
                    @Override
                    public boolean onBackPressed() {
                        return true;
                    }
                }).asConfirm(title, content, "取消", confirm, new OnConfirmListener() {
            @Override
            public void onConfirm() {
                updateCallRecords();
                SysUtil.callPhoneImmediately(mContext, content);
            }
        }, null, false)
                .show();
    }


}
