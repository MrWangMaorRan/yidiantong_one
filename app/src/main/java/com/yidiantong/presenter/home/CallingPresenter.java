package com.yidiantong.presenter.home;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnCancelListener;
import com.lxj.xpopup.interfaces.OnInputConfirmListener;
import com.lxj.xpopup.interfaces.SimpleCallback;
import com.yidiantong.R;
import com.yidiantong.adapter.BtnCheckedAdapter;
import com.yidiantong.app.MyLinPhoneManager;
import com.yidiantong.base.Constants;
import com.yidiantong.bean.CallLogBean;
import com.yidiantong.bean.CluesDetailBean;
import com.yidiantong.bean.CluesListBean;
import com.yidiantong.bean.ContactBean;
import com.yidiantong.bean.ImportOnePhoneBean;
import com.yidiantong.bean.ResponseCallAllBean;
import com.yidiantong.bean.StringCheckedBean;
import com.yidiantong.bean.request.CallDto;
import com.yidiantong.bean.request.CallRecordsDto;
import com.yidiantong.bean.request.DurationDto;
import com.yidiantong.bean.request.ImportAddressBookDto;
import com.yidiantong.bean.request.ImportOnePhoneDto;
import com.yidiantong.bean.request.SearchPhoneDto;
import com.yidiantong.bean.request.UpdateCluesInfoDto;
import com.yidiantong.model.biz.home.ICalling;
import com.yidiantong.model.impl.home.CallingImpl;
import com.yidiantong.model.impl.home.PickContactImpl;
import com.yidiantong.model.impl.login.LoginImpl;
import com.yidiantong.util.ContactUtils;
import com.yidiantong.util.DensityUtils;
import com.yidiantong.util.SharedPreferencesUtil;
import com.yidiantong.util.StringUtils;
import com.yidiantong.util.TimeFormat;
import com.yidiantong.util.ToastUtils;
import com.yidiantong.util.Utils;
import com.yidiantong.util.log.LogUtils;
import com.yidiantong.widget.FlowLayout;
import com.yidiantong.widget.ScrollViewWithGridView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CallingPresenter implements CallingImpl.OnCallBackListener {

    private Context mContext;
    private CallingImpl callingImpl;
    private ICalling iCalling;
    private UpdateCluesInfoDto updateCluesInfoDto;
    private String intentionChooseTags;
    private String emotionChooseTags;
    private List<String> tags = new ArrayList<>();
    private CluesDetailBean cluesDetailBean;
    // 标记数据
    private String[] propertyArr; // 性别
    private String[] intentionArr; // 客户意向
    private String[] emotionArr; // 客户情绪
    private BtnCheckedAdapter propertyAdapter;
    private BtnCheckedAdapter intentionAdapter;
    private BtnCheckedAdapter emotionAdapter;
    private List<StringCheckedBean> intentionList;
    private List<StringCheckedBean> emotionList;
    private List<StringCheckedBean> propertyList;
    private List<CluesListBean> cluesList;
    private int callPosition;
    private boolean isCalling;
    private ResponseCallAllBean callRequestBean;
    private ResponseCallAllBean callStatusBean;
    // v2.1
    private String callPhoneNum;

    public void setCluesId(String cluesId) {
        updateCluesInfoDto.setCluesId(cluesId);
    }

    public CallingPresenter(Context mContext, ICalling iCalling) {
        this.mContext = mContext;
        this.iCalling = iCalling;
        callingImpl = new CallingImpl();
        cluesDetailBean = new CluesDetailBean();
        updateCluesInfoDto = new UpdateCluesInfoDto();
        propertyList = new ArrayList<>();
        cluesList = new ArrayList<>();
        // 标签
        propertyArr = mContext.getResources().getStringArray(R.array.gender); // 资源文件
        intentionArr = mContext.getResources().getStringArray(R.array.customer_intention); // 资源文件
        emotionArr = mContext.getResources().getStringArray(R.array.customer_emotion); // 资源文件
    }

    /**
     * 初始化客戶属性
     */
    public void initAdapterCustomProperty(ScrollViewWithGridView svgvCustomerProperty) {
        // 客户属性
        propertyAdapter = new BtnCheckedAdapter(mContext);
        propertyAdapter.setRadio(true);
        svgvCustomerProperty.setAdapter(propertyAdapter);
        propertyList = new ArrayList<>();
        for (int i = 0; i < propertyArr.length; i++) {
            StringCheckedBean bean = new StringCheckedBean();
            bean.setTitle(propertyArr[i]);
            bean.setChecked(false);
            propertyList.add(bean);
        }
        propertyAdapter.setStringCheckedList(propertyList, false);
        // 点击事件
        propertyAdapter.setOnBtnClickListener(new BtnCheckedAdapter.OnBtnClickListener() {
            @Override
            public void onBtnClick(String checkStr, boolean isChecked) {
                if (isChecked) {
                    updateCluesInfoDto.setGender(checkStr);
                }
            }
        });
    }

    /**
     * 初始化客戶意向
     */
    public void initAdapterCustomIntention(ScrollViewWithGridView svgvCustomerIntention) {
        // 客户意向
        intentionAdapter = new BtnCheckedAdapter(mContext);
        svgvCustomerIntention.setAdapter(intentionAdapter);
        intentionAdapter.setRadio(true);
        intentionList = new ArrayList<>();
        for (int i = 0; i < intentionArr.length; i++) {
            StringCheckedBean bean = new StringCheckedBean();
            bean.setTitle(intentionArr[i]);
            bean.setChecked(false);
            intentionList.add(bean);
        }
        intentionAdapter.setStringCheckedList(intentionList, false);
        // 点击事件
        intentionAdapter.setOnBtnClickListener(new BtnCheckedAdapter.OnBtnClickListener() {
            @Override
            public void onBtnClick(String checkStr, boolean isChecked) {
                if (isChecked) {
                    intentionChooseTags = checkStr;
                }
            }
        });
    }

    /**
     * 初始化客户情绪
     */
    public void initAdapterCustomEmotion(ScrollViewWithGridView svgvCustomerEmotion) {
        // 客户情绪
        emotionAdapter = new BtnCheckedAdapter(mContext);
        svgvCustomerEmotion.setAdapter(emotionAdapter);
        emotionAdapter.setRadio(true);
        emotionList = new ArrayList<>();
        for (int i = 0; i < emotionArr.length; i++) {
            StringCheckedBean bean = new StringCheckedBean();
            bean.setTitle(emotionArr[i]);
            bean.setChecked(false);
            emotionList.add(bean);
        }
        emotionAdapter.setStringCheckedList(emotionList, false);
        // 点击事件
        emotionAdapter.setOnBtnClickListener(new BtnCheckedAdapter.OnBtnClickListener() {
            @Override
            public void onBtnClick(String checkStr, boolean isChecked) {
                if (isChecked) {
                    emotionChooseTags = checkStr;
                }
            }
        });
    }

    public void initCall(String phoneNum) {
        if (Utils.isAXB(mContext)) {
            if (Constants.sipIsLogin) {
                if (!StringUtils.isNullOrBlank(phoneNum)) {
                    isCalling = true;
                    MyLinPhoneManager.getInstance(mContext).call(phoneNum);
                    // 添加沟通记录 修改： 2020-08-07 一点通电话不需要调用添加记录接口
//                callingPresenter.updateCallRecords(cluesId, phoneNum);
                } else {
                    ToastUtils.showToast(mContext, "号码有误");
                }
            } else {
                ToastUtils.showToast(mContext, "拨打失败");
                ((Activity) mContext).finish();
            }
        } else {
            // 双呼
            call(updateCluesInfoDto.getCluesId());

        }
    }

    /**
     * v2.1
     */
    public void importOnePhone(String phoneNum) {
        callPhoneNum = phoneNum;
        ImportOnePhoneDto importOnePhoneDto = new ImportOnePhoneDto();
        importOnePhoneDto.setPhone(phoneNum);
        callingImpl.importOnePhone(mContext, importOnePhoneDto, this);
    }

    /**
     * 双呼拨打提交通话时长
     */
    public void duration() {
        CallLogBean callLogBean = ContactUtils.getCallLog(mContext);
        if (callLogBean != null) {
            DurationDto durationDto = new DurationDto();
            durationDto.setDuration(callLogBean.getDuration() + "");
            callingImpl.duration(mContext, durationDto, this);
        }
    }

    /**
     * 编辑信息
     */
    public void updateCluesInfo() {
        updateCluesInfoDto.getTags().clear();
        updateCluesInfoDto.getTags().add(intentionChooseTags);
        updateCluesInfoDto.getTags().add(emotionChooseTags);
        updateCluesInfoDto.getTags().addAll(tags);

        if (cluesDetailBean.getTags() != null
                && !StringUtils.isNullOrBlank(updateCluesInfoDto.getGender())
                && !StringUtils.isNullOrBlank(updateCluesInfoDto.getWechatId())
                && updateCluesInfoDto.getGender().equals(cluesDetailBean.getGender())
                && updateCluesInfoDto.getTags().equals(cluesDetailBean.getTags())
                && updateCluesInfoDto.getWechatId().equals(cluesDetailBean.getWechatId())) {
            iCalling.saveAndHangUpCallingFailureResult();
            // 如果数据都跟后台一致，则直接finish
            ((Activity) mContext).finish();
        } else {
            LogUtils.e("", "updateCluesInfoDto data = " + updateCluesInfoDto.toString());
            callingImpl.updateCluesInfo(mContext, updateCluesInfoDto, this);
        }
    }

    // 设置tags
    private void setTagsData() {
        for (String tag : cluesDetailBean.getTags()) {
            boolean isHas = false;
            for (int i = 0; i < intentionArr.length; i++) {
                if (intentionArr[i].equals(tag)) {
                    isHas = true;
                }
            }

            if (!isHas) {
                for (int i = 0; i < emotionArr.length; i++) {
                    if (emotionArr[i].equals(tag)) {
                        isHas = true;
                    }
                }
            }

            if (!isHas) {
                tags.add(tag);
            }
        }
    }

    /**
     * 更新通话记录
     */
    public void updateCallRecords(String cluesId, String phoneNumber) {
        // 添加记录
        CallRecordsDto callRecordsDto = new CallRecordsDto();
        callRecordsDto.setCluesId(cluesId);
        callRecordsDto.setCallType("一点通电话");
        callRecordsDto.setPhoneNumber(phoneNumber);
        callRecordsDto.setCallTime(TimeFormat.dateToStringYyyyMMddHHmmss(System.currentTimeMillis()));
        callingImpl.updateCallRecords(mContext, callRecordsDto, this);
    }

    // 获取线索详情信息
    public void cluesInfo() {
        Map<String, String> map = new HashMap<>();
        map.put("cluesId", updateCluesInfoDto.getCluesId());
        callingImpl.cluesInfo(mContext, map, this);
    }

    /**
     * 拨打  2.0 双呼系统
     *
     * @param id 被叫人电话号码对应的唯一标识
     */
    public void call(String id) {
        CallDto callDto = new CallDto();
        callDto.setId(id);
        callingImpl.callRequest(mContext, callDto, this);
    }

    /**
     * 拨打状态接口查询
     */
    public void callStatus() {
        if (callRequestBean != null && !StringUtils.isNullOrBlank(callRequestBean.getCallId())) {
            Map<String, String> paramsMap = new HashMap<>();
            paramsMap.put("callId", callRequestBean.getCallId());
            callingImpl.callStatus(mContext, paramsMap, this);
        }
    }

    /**
     * 挂断
     */
    public void saveAndHangUpCalling(EditText etPhoneNum, EditText etWeChatNum) {
        if (Utils.isAXB(mContext) && Constants.sipIsLogin) {
            MyLinPhoneManager.getInstance(mContext).hangUp();
        } else {
            // 双呼，发送通话时长
            duration();
        }
        String phoneNum = etPhoneNum.getText().toString().trim();
        if (!StringUtils.isNullOrBlank(phoneNum) && !StringUtils.isPhoneType(phoneNum)) {
            ToastUtils.showToast(mContext, "请输入正确的手机号");
            return;
        } else {
//            callingParameter[5] = etPhoneNum.getText().toString();
            updateCluesInfoDto.setPhoneNumber(phoneNum);
            // 微信号
//            callingParameter[6] = etWeChatNum.getText().toString();
            updateCluesInfoDto.setWechatId(etWeChatNum.getText().toString());
            // 提交更新
            updateCluesInfo();
        }
    }

    // tag的addView
    public void addTagsView(FlowLayout flTagList) {
        if (tags != null) {
            // 去掉重复元素
            tags = Utils.getRemoveList(tags);

            // 加入布局
            flTagList.removeAllViews();

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    DensityUtils.dp2px(mContext, 34));

            LinearLayout.LayoutParams tvLayoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            LinearLayout.LayoutParams ivLayoutParams = new LinearLayout.LayoutParams(
                    DensityUtils.dp2px(mContext, 16), DensityUtils.dp2px(mContext, 16));
            for (int i = 0; i < tags.size(); i++) {
                // tvTag
                TextView textView = new TextView(mContext);
                textView.setText(tags.get(i));
                textView.setSingleLine();
                textView.setTextSize(14);
                textView.setTextColor(mContext.getResources().getColor(R.color.white));
                textView.setLayoutParams(tvLayoutParams);

                // tahDeleteImg
                ImageView imageView = new ImageView(mContext);
                imageView.setScaleType(ImageView.ScaleType.CENTER);
                imageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_delete_gray));
                imageView.setLayoutParams(ivLayoutParams);
                imageView.setId(i);
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        tags.remove(view.getId());
                        iCalling.setTagsList();
                    }
                });

                // linearLayout
                layoutParams.rightMargin = DensityUtils.dp2px(mContext, 8);
                layoutParams.topMargin = DensityUtils.dp2px(mContext, 15);
                LinearLayout linearLayout = new LinearLayout(mContext);
                linearLayout.setGravity(Gravity.CENTER);
                linearLayout.setBackground(mContext.getResources().getDrawable(R.drawable.bg_corner_4dp_empty_with_white_line)); //设置背景
                linearLayout.setLayoutParams(layoutParams);
                linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                linearLayout.setPadding(DensityUtils.dp2px(mContext, 6), 0, DensityUtils.dp2px(mContext, 6), 0);
                linearLayout.setMinimumWidth(DensityUtils.dp2px(mContext, 98));
                linearLayout.addView(textView);
                linearLayout.addView(imageView);

                flTagList.addView(linearLayout);
            }

            // 自定义
            TextView textView = new TextView(mContext);
            textView.setText(mContext.getResources().getString(R.string.user_defined));
            textView.setSingleLine();
            textView.setTextSize(14);
            textView.setTextColor(mContext.getResources().getColor(R.color.white));
            textView.setLayoutParams(tvLayoutParams);
            // 自定义图标
            ImageView imageView = new ImageView(mContext);
            imageView.setScaleType(ImageView.ScaleType.CENTER);
            imageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_edit_write));
            imageView.setLayoutParams(ivLayoutParams);
            // 自定义tag布局linearLayout
            layoutParams.topMargin = DensityUtils.dp2px(mContext, 15);
            LinearLayout linearLayout = new LinearLayout(mContext);
            linearLayout.setGravity(Gravity.CENTER);
            linearLayout.setBackground(mContext.getResources().getDrawable(R.drawable.bg_corner_4dp_empty_with_white_line)); //设置背景
            linearLayout.setLayoutParams(layoutParams);
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            linearLayout.setPadding(DensityUtils.dp2px(mContext, 6), 0, DensityUtils.dp2px(mContext, 6), 0);
            linearLayout.addView(imageView);
            linearLayout.addView(textView);
            linearLayout.setMinimumWidth(DensityUtils.dp2px(mContext, 98));
            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 自定义tag弹框
                    showUserDefinedPopDialog();
                }
            });

            flTagList.addView(linearLayout);
        }
    }

    // 赋值
    public void setWeChat(EditText etWeChat) {
        if (cluesDetailBean != null) {
            etWeChat.setText(cluesDetailBean.getWechatId());
        } else {
            etWeChat.setText("");
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

    // 是否正在呼叫
    public void setIsCalling(boolean isCalling) {
        this.isCalling = isCalling;
    }

    // 是否在通话中
    public boolean isCalling() {
        return isCalling;
    }

    // phoneNum
    public void callNext() {
        if (!isCalling) {
            String phoneNum = "";
            callPosition++;
            if (cluesList != null) {
                if (callPosition < 0 || callPosition >= cluesList.size()) {
                    callPosition = 0;
                }
                // 拨打号码
                if (!StringUtils.isNullOrBlank(cluesList.get(callPosition).getNumber())) {
                    phoneNum = cluesList.get(callPosition).getNumber();
                } else if (!StringUtils.isNullOrBlank(cluesList.get(callPosition).getPhoneNumber())) {
                    phoneNum = cluesList.get(callPosition).getPhoneNumber();
                }
                if (cluesList.get(callPosition).isCalled()) {
                    // 已经拨打完过了，说明已经拨打完列表
                    iCalling.callAllResult();
                } else {
                    // 拨打状态
                    cluesList.get(callPosition).setCalled(true);
                    // 拨打电话
                    if (Utils.isAXB(mContext)) {
                        // LinPhone 呼
                        if (Constants.sipIsLogin) {
                            if (!StringUtils.isNullOrBlank(phoneNum)) {
                                MyLinPhoneManager.getInstance(mContext).call(phoneNum);
                                initChooseAndTag();
                                // 设置id
                                setCluesId(cluesList.get(callPosition).getId());
                                // 获取详情数据
                                cluesInfo();
                                iCalling.setShowEmptyText();
                                iCalling.callOtherPhoneText(phoneNum);
                                isCalling = true;
                            } else {
                                ToastUtils.showToast(mContext, "号码有误");
                                iCalling.setCallStateText("");
                            }
                        } else {
                            ToastUtils.showToast(mContext, "拨打失败");
                            iCalling.setCallStateText("");
                        }
                    } else {
                        // 双呼
                        call(cluesList.get(callPosition).getId());
                        //
                        initChooseAndTag();
                        // 设置id
                        setCluesId(cluesList.get(callPosition).getId());
                        // 获取详情数据
                        cluesInfo();
                        iCalling.setShowEmptyText();
                        iCalling.callOtherPhoneText(phoneNum);
                        isCalling = true;
                    }
                }
            }
        } else {
//            ToastUtils.showToast(mContext, "呼叫未挂断");
            iCalling.callNextNoHangUp();
        }
    }

    public void initChooseAndTag() {
        for (StringCheckedBean bean : emotionList) {
            bean.setChecked(false);
        }
        for (StringCheckedBean bean : intentionList) {
            bean.setChecked(false);
        }
        for (StringCheckedBean bean : propertyList) {
            bean.setChecked(false);
        }
        emotionAdapter.notifyDataSetChanged();
        intentionAdapter.notifyDataSetChanged();
        propertyAdapter.notifyDataSetChanged();
        tags.clear();
        updateCluesInfoDto = new UpdateCluesInfoDto();
        iCalling.setTagsList();
    }

    /**
     * 设置显示为空
     *
     * @param tvPhoneNum
     * @param tvCallingTime
     * @param tvCallTimeTitle
     * @param etPhoneNum
     */
    public void setShowEmptyText(TextView tvPhoneNum, TextView tvCallingTime, TextView tvCallTimeTitle, EditText etPhoneNum) {
        tvPhoneNum.setText("");
        tvCallingTime.setText("");
        tvCallTimeTitle.setText("");
        etPhoneNum.setText("");
    }

    /**
     * 弹框
     */
    public void showUserDefinedPopDialog() {
        new XPopup.Builder(mContext)
                //.dismissOnBackPressed(false)
                .isDestroyOnDismiss(true) //对于只使用一次的弹窗，推荐设置这个
                .autoOpenSoftInput(true)
//                        .autoFocusEditText(false) //是否让弹窗内的EditText自动获取焦点，默认是true
                .isRequestFocus(false)
                //.moveUpToKeyboard(false)   //是否移动到软键盘上面，默认为true
                .asInputConfirm("自定义标签", null, null, "请输入自定义标签，多个标签以逗号隔开",
                        new OnInputConfirmListener() {
                            @Override
                            public void onConfirm(String text) {
//                                ToastUtils.showToast(mContext, "input text: " + text);
//                                new XPopup.Builder(getContext()).asLoading().show();
                                boolean isRemoveSameTag = false;
                                String[] tagArr = text.split("，");
                                for (int i = 0; i < tagArr.length; i++) {
                                    if (!tags.contains(tagArr[i])
                                            && !StringUtils.arrHasIndexString(propertyArr, tagArr[i])
                                            && !StringUtils.arrHasIndexString(intentionArr, tagArr[i])
                                            && !StringUtils.arrHasIndexString(emotionArr, tagArr[i])) {
                                        tags.add(tagArr[i]);
                                    } else {
                                        isRemoveSameTag = true;
                                    }
                                }
                                if (isRemoveSameTag) {
                                    ToastUtils.showToast(mContext, "已移除相同的标签");
                                }
                                iCalling.setTagsList();
                            }
                        })
                .show();
    }

    /**
     * 提交失败弹框
     */
    public void showCommitFailureDialog(boolean isCallNext) {
        if (!isCallNext) {
            new XPopup.Builder(mContext)
//                        .hasBlurBg(true)
//                        .dismissOnTouchOutside(false)
//                        .autoDismiss(false)
//                        .popupAnimation(PopupAnimation.NoAnimation)
//                        .isLightStatusBar(true)
                    .setPopupCallback(new SimpleCallback() {
                        @Override
                        public void onCreated() {
                            LogUtils.e("tag", "弹窗创建了");
                        }

                        @Override
                        public void onShow() {
                            LogUtils.e("tag", "onShow");
                        }

                        @Override
                        public void onDismiss() {
                            LogUtils.e("tag", "onDismiss");
                        }

                        //如果你自己想拦截返回按键事件，则重写这个方法，返回true即可
                        @Override
                        public boolean onBackPressed() {
                            return true;
                        }
                    }).asConfirm("保存失败，是否重新提交", 16, mContext.getResources().getColor(R.color.blue_3f74fd),
                    "(直接退出后将不保留改数据)", 12, mContext.getResources().getColor(R.color.black_3c3c3c),
                    "不，我要退出", 16, mContext.getResources().getColor(R.color.black_3c3c3c), "是，重新提交", 16,
                    mContext.getResources().getColor(R.color.blue_3f74fd), null, new OnCancelListener() {
                        @Override
                        public void onCancel() {
                            ((Activity) mContext).finish();
                        }
                    }, false)
                    .show();
        } else {
            new XPopup.Builder(mContext)
//                        .hasBlurBg(true)
//                        .dismissOnTouchOutside(false)
//                        .autoDismiss(false)
//                        .popupAnimation(PopupAnimation.NoAnimation)
//                        .isLightStatusBar(true)
                    .setPopupCallback(new SimpleCallback() {
                        @Override
                        public void onCreated() {
                            LogUtils.e("tag", "弹窗创建了");
                        }

                        @Override
                        public void onShow() {
                            LogUtils.e("tag", "onShow");
                        }

                        @Override
                        public void onDismiss() {
                            LogUtils.e("tag", "onDismiss");
                        }

                        //如果你自己想拦截返回按键事件，则重写这个方法，返回true即可
                        @Override
                        public boolean onBackPressed() {
                            return true;
                        }
                    }).asConfirm("保存失败，是否重新提交", 16, mContext.getResources().getColor(R.color.blue_3f74fd),
                    "(直接退出后将不保留改数据)", 12, mContext.getResources().getColor(R.color.black_3c3c3c),
                    "不，不再保留", 16, mContext.getResources().getColor(R.color.black_3c3c3c), "重新提交", 16,
                    mContext.getResources().getColor(R.color.blue_3f74fd), null, new OnCancelListener() {
                        @Override
                        public void onCancel() {
                            callNext();
                        }
                    }, false)
                    .show();
        }
    }

    @Override
    public void onCluesInfoSuccess(CluesDetailBean cluesDetailBean) {
        if (cluesDetailBean != null) {
            this.cluesDetailBean = cluesDetailBean;
            // 设置默认选中
            List<String> list = new ArrayList<>();
            list.add(cluesDetailBean.getGender());
            propertyAdapter.setTagsChoose(list);
            intentionAdapter.setTagsChoose(cluesDetailBean.getTags());
            emotionAdapter.setTagsChoose(cluesDetailBean.getTags());

            // 自定义tags
            setTagsData();
            iCalling.setTagsList();
        }
    }

    @Override
    public void onCluesInfoInfoFailure(String msg) {

    }

    @Override
    public void onUpdateCluesInfoSuccess(String msg) {
        ((Activity) mContext).setResult(Activity.RESULT_OK);
        iCalling.saveAndHangUpCallingSuccessResult();
    }

    @Override
    public void onUpdateCluesInfoFailure(String msg) {
        iCalling.saveAndHangUpCallingFailureResult();
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
    public void onCallRequestSuccess(ResponseCallAllBean responseCallAllBean) {
        callRequestBean = responseCallAllBean;
        callStatus();
    }

    @Override
    public void onCallRequestFailure(String msg) {
        callRequestBean = null;
    }

    @Override
    public void onCallStatusSuccess(ResponseCallAllBean responseCallAllBean) {
        callStatusBean = responseCallAllBean;
    }

    @Override
    public void onCallStatusFailure(String msg) {

    }

    @Override
    public void onImportOneContactsSuccess(ImportOnePhoneBean importOnePhoneBean) {
        if (importOnePhoneBean != null) {
            setCluesId(importOnePhoneBean.getId());
            initCall(callPhoneNum);
            cluesInfo();
        }
    }

    @Override
    public void onImportOneContactsFailure(String msg) {
        LogUtils.i("CallingPresenter", "号码导入有误");
    }

    @Override
    public void onDurationSuccess() {

    }

    @Override
    public void onDurationFailure(String msg) {

    }

}
