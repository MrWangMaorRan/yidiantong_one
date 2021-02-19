package com.yidiantong.fragment;


import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.yidiantong.R;
import com.yidiantong.app.MyLinPhoneManager;
import com.yidiantong.base.AppManager;
import com.yidiantong.base.BaseFragment;
import com.yidiantong.base.Constants;
import com.yidiantong.model.biz.IMain;
import com.yidiantong.presenter.MainPresenter;
import com.yidiantong.util.DensityUtils;
import com.yidiantong.util.HandlerUtils;
import com.yidiantong.util.PermissinsUtils;
import com.yidiantong.util.SharedPreferencesUtil;
import com.yidiantong.util.SpUtils;
import com.yidiantong.util.TimerCallBackUtils;
import com.yidiantong.util.Utils;
import com.yidiantong.util.log.LogUtils;
import com.yidiantong.widget.RoundImageView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;

import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 */
//public class HomeFragment extends BaseFragment implements IMain, XRecyclerView.LoadingListener {
//    @BindView(R.id.iv_left)
//    ImageView ivLeft;
//    @BindView(R.id.tv_title)
//    TextView tvTitle;
//    @BindView(R.id.iv_right_2)
//    ImageView ivRight2;
//    @BindView(R.id.iv_right)
//    ImageView ivRight;
//    @BindView(R.id.tv_select_type)
//    TextView tvSelectType;
//    @BindView(R.id.ll_select_type)
//    LinearLayout llSelectType;
//    @BindView(R.id.tv_select_address)
//    TextView tvSelectAddress;
//    @BindView(R.id.ll_select_address)
//    LinearLayout llSelectAddress;
//    @BindView(R.id.tv_clues_list_count)
//    TextView tvCluesListCount;
//    @BindView(R.id.tv_select_sorting)
//    TextView tvSelectSorting;
//    @BindView(R.id.iv_select_sorting)
//    ImageView ivSelectSorting;
//    @BindView(R.id.ll_select_sorting)
//    LinearLayout llSelectSorting;
//    @BindView(R.id.tv_select_screening)
//    TextView tvSelectScreening;
//    @BindView(R.id.iv_select_screening)
//    ImageView ivSelectScreening;
//    @BindView(R.id.ll_select_screening)
//    LinearLayout llSelectScreening;
//    @BindView(R.id.xrv_call_list)
//    XRecyclerView xrvCallList;
//    @BindView(R.id.ll_mine_info)
//    LinearLayout llMineInfo;
//    @BindView(R.id.riv_head_img)
//    RoundImageView rivHeadImg;
//    @BindView(R.id.tv_name)
//    TextView tvName;
//    @BindView(R.id.tv_phone_num)
//    TextView tvPhoneNum;
//    @BindView(R.id.ll_my_business)
//    LinearLayout llMyBusiness;
//    @BindView(R.id.ll_setting)
//    LinearLayout llSetting;
//    @BindView(R.id.nav_view)
//    NavigationView navView;
//    @BindView(R.id.drawer_layout)
//    DrawerLayout drawerLayout;
//    @BindView(R.id.btn_right)
//    Button btnRight;
//    @BindView(R.id.tv_alarm_call_time)
//    TextView tvAlarmCallTime;
//    @BindView(R.id.ll_alarm_call_time)
//    LinearLayout llAlarmCallTime;
//    @BindView(R.id.view_padding)
//    View viewPadding;
//    @BindView(R.id.iv_input_call_show)
//    ImageView ivInputCallShow;
//    @BindView(R.id.ll_keyboard)
//    LinearLayout llKeyboard;
//    @BindView(R.id.et_input_text)
//    EditText etInputText;
//    @BindView(R.id.rv_number)
//    RecyclerView rvNumber;
//    @BindView(R.id.iv_input_hide)
//    ImageView ivInputHide;
//    @BindView(R.id.iv_input_call)
//    ImageView ivInputCall;
//    @BindView(R.id.iv_input_delete)
//    ImageView ivInputDelete;
//    @BindView(R.id.ll_content)
//    LinearLayout llContent;
//    @BindView(R.id.ll_header)
//    LinearLayout llHeader;

//    private MainPresenter mainPresenter;
//    private boolean drawerLayoutIsOpen;
//    private TimerCallBackUtils timerCallBackUtils;
//    private long millisInFuture = 5000;  // 倒计时3s
//    private long countDownInterval = 1000; // 倒计时间隔1s
//    private boolean isNeedToReRegister = true;
//    private String callType;
//    private boolean isShowInputNumber;
//    private ImageView mIvLeft;
//    private TextView mTvTitle;
//    private Button mBtnRight;
//    private ImageView mIvRight2;
//    private ImageView mIvRight;
//    private LinearLayout mLlHeader;
//    private TextView mTvSelectType;
//    private LinearLayout mLlSelectType;
//    private TextView mTvSelectAddress;
//    private LinearLayout mLlSelectAddress;
//    private TextView mTvCluesListCount;
//    private TextView mTvSelectSorting;
//    private ImageView mIvSelectSorting;
//    private LinearLayout mLlSelectSorting;
//    private TextView mTvSelectScreening;
//    private ImageView mIvSelectScreening;
//    private LinearLayout mLlSelectScreening;
//    private TextView mTvAlarmCallTime;
//    private LinearLayout mLlAlarmCallTime;
//    private View mViewPadding;
//    private XRecyclerView mXrvCallList;
//    private LinearLayout mLlContent;
//    private ImageView mIvInputCallShow;
//    private EditText mEtInputText;
//    private RecyclerView mRvNumber;
//    private ImageView mIvInputHide;
//    private ImageView mIvInputCall;
//    private ImageView mIvInputDelete;
//    private LinearLayout mLlKeyboard;
//    private RoundImageView mRivHeadImg;
//    private TextView mTvName;
//    private TextView mTvPhoneNum;
//    private LinearLayout mLlMineInfo;
//    private LinearLayout mLlMyBusiness;
//    private LinearLayout mLlSetting;
//    private NavigationView mNavView;
//    private DrawerLayout mDrawerLayout;
//
//
//    @Override
//    public int setLayoutId() {
//        return R.layout.fragment_home;
//
//    }
//
//    @Override
//    public void findViewById(View view) {
//        ButterKnife.bind(getActivity());
//        mIvLeft = view.findViewById(R.id.iv_left);
//        mTvTitle =view. findViewById(R.id.tv_title);
//        mBtnRight = view.findViewById(R.id.btn_right);
//        mIvRight2 = view.findViewById(R.id.iv_right_2);
//        mIvRight = view.findViewById(R.id.iv_right);
//        mLlHeader = view.findViewById(R.id.ll_header);
//        mTvSelectType = view.findViewById(R.id.tv_select_type);
//        mLlSelectType = view.findViewById(R.id.ll_select_type);
//        mTvSelectAddress =view. findViewById(R.id.tv_select_address);
//        mLlSelectAddress =view. findViewById(R.id.ll_select_address);
//        mTvCluesListCount =view. findViewById(R.id.tv_clues_list_count);
//        mTvSelectSorting =view. findViewById(R.id.tv_select_sorting);
//        mIvSelectSorting = view.findViewById(R.id.iv_select_sorting);
//        mLlSelectSorting = view.findViewById(R.id.ll_select_sorting);
//        mTvSelectScreening =view. findViewById(R.id.tv_select_screening);
//        mIvSelectScreening = view.findViewById(R.id.iv_select_screening);
//        mLlSelectScreening = view.findViewById(R.id.ll_select_screening);
//        mTvAlarmCallTime = view.findViewById(R.id.tv_alarm_call_time);
//        mLlAlarmCallTime = view.findViewById(R.id.ll_alarm_call_time);
//        mViewPadding = view.findViewById(R.id.view_padding);
//        mXrvCallList =view. findViewById(R.id.xrv_call_list);
//        mLlContent = view.findViewById(R.id.ll_content);
//        mIvInputCallShow =view. findViewById(R.id.iv_input_call_show);
//        mEtInputText = view.findViewById(R.id.et_input_text);
//        mRvNumber =view. findViewById(R.id.rv_number);
//        mIvInputHide = view.findViewById(R.id.iv_input_hide);
//        mIvInputCall =view. findViewById(R.id.iv_input_call);
//        mIvInputDelete =view. findViewById(R.id.iv_input_delete);
//        mLlKeyboard = view.findViewById(R.id.ll_keyboard);
//        mRivHeadImg = view.findViewById(R.id.riv_head_img);
//        mTvName = view.findViewById(R.id.tv_name);
//        mTvPhoneNum =view. findViewById(R.id.tv_phone_num);
//        mLlMineInfo = view.findViewById(R.id.ll_mine_info);
//        mLlMyBusiness = view.findViewById(R.id.ll_my_business);
//        mLlSetting = view.findViewById(R.id.ll_setting);
//        mNavView = view.findViewById(R.id.nav_view);
//        mDrawerLayout =view. findViewById(R.id.drawer_layout);
//        mainPresenter = new MainPresenter(getContext(), this);
//        Utils.getPermission(getContext());
//
//        // 拨号类型
//        callType = SharedPreferencesUtil.getSharedPreferences(getContext()).getString("callType", "");
//
//        // 首页不能返回使用logo图标
//        mIvLeft.setImageDrawable(getResources().getDrawable(R.drawable.ic_logo_large));
////        mIvLeft.setPadding(DensityUtils.dp2px(getContext(), 12),
////                DensityUtils.dp2px(getContext(), 12),
////                DensityUtils.dp2px(getContext(), 12),
////                DensityUtils.dp2px(getContext(), 12));
//        mIvLeft.setEnabled(false);
//        mIvRight.setVisibility(View.VISIBLE);
//        mTvTitle.setText(getResources().getString(R.string.title_clues));
//        mIvRight2.setVisibility(View.VISIBLE);
//
//        // 初始化输入键盘
//        mainPresenter.initKeyboard(mRvNumber);
//        mEtInputText.setEnabled(false);
//
//        // 侧滑栏是否打开监听
//        mDrawerLayout.setDrawerListener(drawerListener);
//        // 刷新加载监听
//        mXrvCallList.setLoadingListener(this);
//        // 初始化适配器
//        mainPresenter.initAdapter(mXrvCallList);
//
//        // 获取数据
//        mainPresenter.getIndustryList();
//        mainPresenter.getUserInfo();
//        // 注册广播接收器
//        EventBus.getDefault().register(this);
//        //
//        timerCallBackUtils = new TimerCallBackUtils(millisInFuture, countDownInterval, callRingCallBack);
//        timerCallBackUtils.start();
//
//
//    }
//
//    class HeaderInterceptor implements Interceptor {
//
//        @Override
//        public Response intercept(Chain chain) throws IOException {
//            Request build = chain.request().newBuilder()
//                    .header("Connection", "keep-alive")
//                    .addHeader("Client-Type", "ANDROID")
//                    .addHeader("Authorization", "Bearer " + SpUtils.getInstance().getString("token"))
//                    .build();
//            return chain.proceed(build);
//        }
//    }
//
//
//    @OnClick({R.id.iv_left, R.id.iv_right, R.id.iv_right_2, R.id.ll_select_type, R.id.ll_select_address,
//            R.id.ll_select_sorting, R.id.ll_select_screening, R.id.ll_mine_info, R.id.ll_my_business, R.id.ll_setting,
//            R.id.iv_input_call_show, R.id.iv_input_hide, R.id.iv_input_call, R.id.iv_input_delete, R.id.ll_header, R.id.ll_content})
//    public void onViewClicked(View view) {
//        mDrawerLayout.closeDrawer(Gravity.RIGHT);
//        switch (view.getId()) {
//            case R.id.iv_left:
//            default:
//                break;
//            case R.id.iv_right:
//                mDrawerLayout.openDrawer(Gravity.RIGHT);
//                hideKeyboard(false);
//                break;
//            case R.id.iv_right_2:
//                mainPresenter.getContactPermission();
//                hideKeyboard(false);
//                break;
//            case R.id.ll_select_type:
//                mainPresenter.showPartShadow(view, "industry");
//                hideKeyboard(false);
//                break;
//            case R.id.ll_select_address:
//                mainPresenter.showPartShadow(view, "address");
//                hideKeyboard(false);
//                break;
//            case R.id.ll_select_sorting:
//                mainPresenter.showPartShadow(view, "sorting");
//                hideKeyboard(false);
//                break;
//            case R.id.ll_select_screening:
//                mainPresenter.showScreeningPartShadow(view);
//                hideKeyboard(false);
//                break;
//            case R.id.ll_mine_info:
//                mainPresenter.goToMine();
//                hideKeyboard(false);
//                break;
//            case R.id.ll_my_business:
//                mainPresenter.goToMyBusiness();
//                hideKeyboard(false);
//                break;
//            case R.id.ll_setting:
//                mainPresenter.goToSetting();
//                hideKeyboard(false);
//                break;
//            case R.id.iv_input_call_show:
//                hideKeyboard(true);
//                break;
//            case R.id.ll_header:
//            case R.id.ll_content:
//            case R.id.iv_input_hide:
//                hideKeyboard(false);
//                break;
//            case R.id.iv_input_call:
//                mainPresenter.call(mEtInputText.getText().toString());
//                mainPresenter.deleteInput_quanbu();
//                break;
//            case R.id.iv_input_delete:
//                mainPresenter.deleteInput();
//                break;
//        }
//        mIvInputDelete.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View view) {
//                mainPresenter.deleteInput_quanbu();
//                return false;
//            }
//        });
//    }
//
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        // 修复：首次没有数据的时候，选项为空，刷新数据刷不出来
//        if (mainPresenter.isIndustryListNull()) {
//            // 获取数据
//            mainPresenter.getIndustryList();
//        }
//        // 每次回到首页都要获取一次通话时长，才能实时监听通话时长剩余
//        mainPresenter.talkTimeInfo();
//        if (!Constants.sipIsLogin) {
//            // 登陆失败时，回到首页重新 -> 初始化-登陆
////            MyLinPhoneManager.getInstance().unInit(this);
//            MyLinPhoneManager.getInstance(getContext()).init();
//            mainPresenter.getUserInfo();
//        }
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        mainPresenter.onActivityResult(requestCode, resultCode, data);
//    }
//
//
////    @Override
////    public boolean onKeyDown(int keyCode, KeyEvent event) {
////        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
////            if (drawerLayoutIsOpen) {
////                drawerLayout.closeDrawer(Gravity.RIGHT);
////            } else if (isShowInputNumber) {
////                hideKeyboard(false);
////            } else {
////                if (mainPresenter != null) {
////                    mainPresenter.exit();
////                }
////            }
////            return false;
////        }
////        return super.onKeyDown(keyCode, event);
////    }
//
//    @Override
//    public void industryChooseResult(String industryStr) {
//        HandlerUtils.setText(mTvSelectType, industryStr);
//        mainPresenter.getRegionList(industryStr);
//    }
//
//    @Override
//    public void addressChooseResult(String AddressStr) {
//        HandlerUtils.setText(mTvSelectAddress, AddressStr);
//        mainPresenter.refreshData();
//    }
//
//    @Override
//    public void sortingChooseResult(String sortingStr) {
//        HandlerUtils.setText(mTvSelectSorting, sortingStr);
//        HandlerUtils.setTextColor(mTvSelectSorting, this.getResources().getColor(R.color.blue_3f74fd));
//        HandlerUtils.setImg(mIvSelectSorting, this.getResources().getDrawable(R.drawable.ic_sorting_blue));
//        mainPresenter.refreshData();
//    }
//
//    @Override
//    public void screeningChooseResult(String screeningStr) {
//        if (screeningStr.equals(getContext().getResources().getString(R.string.clues_screening))) {
//            HandlerUtils.setText(mTvSelectScreening, screeningStr);
//            HandlerUtils.setTextColor(mTvSelectScreening, this.getResources().getColor(R.color.gray_56575a));
//            HandlerUtils.setImg(mIvSelectScreening, this.getResources().getDrawable(R.drawable.ic_screening_gray));
//        } else {
//            HandlerUtils.setText(mTvSelectScreening, screeningStr);
//            HandlerUtils.setTextColor(mTvSelectScreening, this.getResources().getColor(R.color.blue_3f74fd));
//            HandlerUtils.setImg(mIvSelectScreening, this.getResources().getDrawable(R.drawable.ic_screening_blue));
//        }
//        mainPresenter.refreshData();
//    }
//
//    @Override
//    public void onUserInfoResult() {
//        mainPresenter.userInfoSetText(mRivHeadImg, mTvName, mTvPhoneNum);
//    }
//
//    @Override
//    public void onTalkTimeResult() {
//        mainPresenter.talkTimeAlarm(mLlAlarmCallTime, mTvAlarmCallTime, mViewPadding);
//    }
//
//    @Override
//    public void refreshComplete() {
//        mXrvCallList.refreshComplete();
//    }
//
//    @Override
//    public void loadComplete() {
//        mXrvCallList.loadMoreComplete();
//    }
//
//    @Override
//    public void refreshKeyboardInput(String inputText) {
//        HandlerUtils.setText(mEtInputText, inputText);
//    }
//
//    @Override
//    public void hideKeyboard(boolean isVisible) {
//        isShowInputNumber = isVisible;
//        if (isVisible) {
//            HandlerUtils.setVisible(mLlKeyboard, View.VISIBLE);
//        } else {
//            HandlerUtils.setVisible(mLlKeyboard, View.GONE);
//        }
//    }
//
//    @Override
//    public void onCluesListResult() {
//        mainPresenter.setListData(mTvCluesListCount);
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        if (EventBus.getDefault().isRegistered(this)) {
//            EventBus.getDefault().unregister(this);
//        }
//        //MyLinPhoneManager.getInstance(mContext).unInit();
//    }
//
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onEvent(String message) {
//        if (message != null) {
//            switch (message) {
//                case Constants.LOGIN_FAILURE: // sip登陆失败
//                    if (isNeedToReRegister) {
//                        isNeedToReRegister = false;
//                        timerCallBackUtils.start();
//                    }
//                    break;
//                case Constants.TOKEN_TIMEOUT: // token超时
//                    LogUtils.i("onEvent", "onResponse message --> " + message);
//                    AppManager.finishAllActivity();
//                    SharedPreferencesUtil.getSharedPreferences(getContext()).clearAll();
//                    mainPresenter.goToLoginStart();
//                    break;
//            }
//        }
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (PermissinsUtils.isPermissions(permissions, grantResults)) {
//            mainPresenter.onRequestPermissionsResult(requestCode);
//        }
//    }
//
//    /**
//     * 侧栏滑出和隐藏的监听事件
//     */
//    DrawerLayout.DrawerListener drawerListener = new DrawerLayout.DrawerListener() {
//        @Override
//        public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
//
//        }
//
//        @Override
//        public void onDrawerOpened(@NonNull View drawerView) {
//            // 保存此至用于返回按钮的判断，是否滑出。     -- 滑出
//            drawerLayoutIsOpen = true;
//        }
//
//        @Override
//        public void onDrawerClosed(@NonNull View drawerView) {
//            // 保存此至用于返回按钮的判断，是否滑出。     -- 未滑出
//            drawerLayoutIsOpen = false;
//        }
//
//        @Override
//        public void onDrawerStateChanged(int newState) {
//
//        }
//    };
//
//    @Override
//    public void onRefresh() {
//        mainPresenter.refreshData();
//        // 刷新同时刷新话务
//        mainPresenter.talkTimeInfo();
//    }
//
//    @Override
//    public void onLoadMore() {
//        mainPresenter.loadData();
//    }
//
//    /**
//     * 倒计时回调
//     */
//    TimerCallBackUtils.TimerCallBack callRingCallBack = new TimerCallBackUtils.TimerCallBack() {
//        @Override
//        public void onTickCallBack(long millisUntilFinished) {
//        }
//
//        @Override
//        public void onFinishCallBack() {
//            isNeedToReRegister = true;
//            MyLinPhoneManager.getInstance(getContext()).login();
//        }
//    };
//
//
//    @Override
//    public void setViewData(View view) {
//
//    }
//
//    @Override
//    public void setClickEvent(View view) {
//
//    }

//}
