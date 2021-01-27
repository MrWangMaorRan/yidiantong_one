package com.yidiantong;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.yidiantong.api.ApiService;
import com.yidiantong.api.Backpro;
import com.yidiantong.app.MainApplication;
import com.yidiantong.app.MyLinPhoneManager;
import com.yidiantong.base.AppManager;
import com.yidiantong.base.BaseActivity;
import com.yidiantong.base.Constants;
import com.yidiantong.bean.LoginBean;
import com.yidiantong.bean.VersionBean;
import com.yidiantong.model.biz.IMain;
import com.yidiantong.presenter.MainPresenter;
import com.yidiantong.util.DensityUtils;
import com.yidiantong.util.DownloadUtils;
import com.yidiantong.util.HandlerUtils;
import com.yidiantong.util.PermissinsUtils;
import com.yidiantong.util.SharedPreferencesUtil;
import com.yidiantong.util.SpUtils;
import com.yidiantong.util.TimerCallBackUtils;
import com.yidiantong.util.ToastUtils;
import com.yidiantong.util.Utils;
import com.yidiantong.util.log.LogUtils;
import com.yidiantong.widget.RoundImageView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends BaseActivity implements IMain, XRecyclerView.LoadingListener {

    @BindView(R.id.iv_left)
    ImageView ivLeft;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_right_2)
    ImageView ivRight2;
    @BindView(R.id.iv_right)
    ImageView ivRight;
    @BindView(R.id.tv_select_type)
    TextView tvSelectType;
    @BindView(R.id.ll_select_type)
    LinearLayout llSelectType;
    @BindView(R.id.tv_select_address)
    TextView tvSelectAddress;
    @BindView(R.id.ll_select_address)
    LinearLayout llSelectAddress;
    @BindView(R.id.tv_clues_list_count)
    TextView tvCluesListCount;
    @BindView(R.id.tv_select_sorting)
    TextView tvSelectSorting;
    @BindView(R.id.iv_select_sorting)
    ImageView ivSelectSorting;
    @BindView(R.id.ll_select_sorting)
    LinearLayout llSelectSorting;
    @BindView(R.id.tv_select_screening)
    TextView tvSelectScreening;
    @BindView(R.id.iv_select_screening)
    ImageView ivSelectScreening;
    @BindView(R.id.ll_select_screening)
    LinearLayout llSelectScreening;
    @BindView(R.id.xrv_call_list)
    XRecyclerView xrvCallList;
    @BindView(R.id.ll_mine_info)
    LinearLayout llMineInfo;
    @BindView(R.id.riv_head_img)
    RoundImageView rivHeadImg;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_phone_num)
    TextView tvPhoneNum;
    @BindView(R.id.ll_my_business)
    LinearLayout llMyBusiness;
    @BindView(R.id.ll_setting)
    LinearLayout llSetting;
    @BindView(R.id.nav_view)
    NavigationView navView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.btn_right)
    Button btnRight;
    @BindView(R.id.tv_alarm_call_time)
    TextView tvAlarmCallTime;
    @BindView(R.id.ll_alarm_call_time)
    LinearLayout llAlarmCallTime;
    @BindView(R.id.view_padding)
    View viewPadding;
    @BindView(R.id.iv_input_call_show)
    ImageView ivInputCallShow;
    @BindView(R.id.ll_keyboard)
    LinearLayout llKeyboard;
    @BindView(R.id.et_input_text)
    EditText etInputText;
    @BindView(R.id.rv_number)
    RecyclerView rvNumber;
    @BindView(R.id.iv_input_hide)
    ImageView ivInputHide;
    @BindView(R.id.iv_input_call)
    ImageView ivInputCall;
    @BindView(R.id.iv_input_delete)
    ImageView ivInputDelete;
    @BindView(R.id.ll_content)
    LinearLayout llContent;
    @BindView(R.id.ll_header)
    LinearLayout llHeader;

    private MainPresenter mainPresenter;
    private boolean drawerLayoutIsOpen;
    private TimerCallBackUtils timerCallBackUtils;
    private long millisInFuture = 5000;  // 倒计时3s
    private long countDownInterval = 1000; // 倒计时间隔1s
    private boolean isNeedToReRegister = true;
    private String callType;
    private boolean isShowInputNumber;
    private String thisVersion;
    private ProgressBar progressBar5;

    @Override
    public void getIntentData() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        mainPresenter = new MainPresenter(this, this);
        Utils.getPermission(this);

        // 拨号类型
        callType = SharedPreferencesUtil.getSharedPreferences(mContext).getString("callType", "");

        // 首页不能返回使用logo图标
        ivLeft.setImageDrawable(getResources().getDrawable(R.drawable.ic_logo_large));
        ivLeft.setPadding(DensityUtils.dp2px(this, 12),
                DensityUtils.dp2px(this, 12),
                DensityUtils.dp2px(this, 12),
                DensityUtils.dp2px(this, 12));
        ivLeft.setEnabled(false);
        ivRight.setVisibility(View.VISIBLE);
        tvTitle.setText(getResources().getString(R.string.title_clues));
        ivRight2.setVisibility(View.VISIBLE);

        // 初始化输入键盘
        mainPresenter.initKeyboard(rvNumber);
        etInputText.setEnabled(false);

        // 侧滑栏是否打开监听
        drawerLayout.setDrawerListener(drawerListener);
        // 刷新加载监听
        xrvCallList.setLoadingListener(this);
        // 初始化适配器
        mainPresenter.initAdapter(xrvCallList);

        // 获取数据
        mainPresenter.getIndustryList();
        mainPresenter.getUserInfo();
        // 注册广播接收器
        EventBus.getDefault().register(this);
        //
        timerCallBackUtils = new TimerCallBackUtils(millisInFuture, countDownInterval, callRingCallBack);
        timerCallBackUtils.start();

        //获取自身版本
        thisVersion = getvertion(this);
        Log.i("xxxx", "init: " + thisVersion);
        Log.i("xxxx", "token------: " + SpUtils.getInstance().getString("token"));
        //请求网络后台 获取最新版本号
        getversion();
    }

    class HeaderInterceptor implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request build = chain.request().newBuilder()
                    .header("Connection", "keep-alive")
                    .addHeader("Client-Type", "ANDROID")
                    .addHeader("Authorization", "Bearer " + SpUtils.getInstance().getString("token"))
                    .build();
            return chain.proceed(build);
        }
    }


    private OkHttpClient getOkhttpClient() {
        return new OkHttpClient.Builder()
                .addInterceptor(new HeaderInterceptor())
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                // .cookieJar(cookieJar)
                .connectTimeout(10, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .build();
    }

    private void getversion() {
        String url = "http://139.196.56.167:10090/api/phone/";
        Retrofit retrofit = new Retrofit.Builder()
                .client(getOkhttpClient())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(url)
                .build();
        retrofit.create(ApiService.class).getVersion()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<VersionBean>() {
                    @Override
                    public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@io.reactivex.annotations.NonNull VersionBean versionBean) {
                        String newVersion = versionBean.getData().getVersion();
                        String downloadUrl = versionBean.getData().getDownloadUrl();
                        String info = versionBean.getData().getInfo();
                        double fileSize = versionBean.getData().getFileSize();
                        Log.i("xxxx", "onNext: " + downloadUrl);
                        if (thisVersion.equals(newVersion)) {
                            //版本是最新的
                        } else {
                            //更新 提示
                            showDialog(downloadUrl,info,fileSize);

                        }
                    }

                    @Override
                    public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                        Log.i("xxxx", "onError: " + e.toString());
                    }

                    @Override
                    public void onComplete() {
                        Log.i("xxxx", "onComplete: " + thisVersion);
                    }
                });

    }
    //下载弹窗
    private void showDialog(String downloadUrl, String info,double fileSize) {
        final Dialog dialog = new Dialog(this, R.style.NormalDialogStyle);
        View view = View.inflate(this, R.layout.showdialog, null);
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialog.setContentView(view, lp);
        // 设置点击对话框外部是否关闭对话框
        dialog.setCanceledOnTouchOutside(false);

        Button cancel = (Button) view.findViewById(R.id.bt_cancel);
        Button confirm = (Button) view.findViewById(R.id.bt_confirm);
        TextView tip = view.findViewById(R.id.tv_tip);
        tip.setText(info);
        progressBar5 = view.findViewById(R.id.progressBar5);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //设置标题进度条风格
              //    requestWindowFeature(Window.FEATURE_PROGRESS);
                  setContentView(R.layout.activity_main);
                //显示标题进度
                setProgressBarVisibility(true);
                //设置标题当前进度值 为5000（标题进度最大值默认为10000）
                //关闭标题进度
                //setProgressBarVisibility(false);
                confirm.setVisibility(View.GONE);
                cancel.setVisibility(View.GONE);
                progressBar5.setVisibility(View.VISIBLE);
                updateHint(downloadUrl,fileSize);

               // dialog.dismiss();
            }
        });
        dialog.show();
    }


    private void updateHint(String downloadUrl,double fileSize) {
        int file= (int) (fileSize*1024*1024);
       // progressBar5.setMax(file);
        //下载
        DownloadUtils downloadUtils = new DownloadUtils();
        downloadUtils.downloadOk(downloadUrl, Constants.DOWN_PATH, new Backpro() {
            @Override
            public void getpro(long max, int pro) {
                //下载完成  安装
                if (max!=666){
                    int percent = (int)((pro  * 100) / file);
                    progressBar5.setMax(file);
                    progressBar5.setProgress(pro);
                }else {
                    ToastUtils.showToast(MainActivity.this, "下载完成 ");
                    File file = new File(Constants.DOWN_PATH);
                    installAPK(file);

                }
            }
        });

    }

    private void installAPK(File file) {
        if (!file.exists()) {
            Toast.makeText(mContext, "apk不存在!", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (file.getName().endsWith(".apk")) {

            try {
                //兼容7.0
                Uri uri;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) { // 适配Android 7系统版本
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); //添加这一句表示对目标应用临时授权该Uri所代表的文件
                    uri = FileProvider.getUriForFile(MainApplication.con, "com.yidiantong.fileprovider", file);//通过FileProvider创建一个content类型的Uri
                } else {
                    uri = Uri.fromFile(file);
                }
                intent.setDataAndType(uri, "application/vnd.android.package-archive"); // 对应apk类型
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(mContext, "不是apk文件!", Toast.LENGTH_SHORT).show();
        }
        finish();
        //弹出安装界面
        MainApplication.con.startActivity(intent);

    }

    private String getvertion(Context context) {
        String versionName = "";
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
            if (versionName == null || versionName.length() <= 0) {
                return "";
            }
        } catch (Exception e) {
            Log.e("VersionInfo", "Exception", e);
        }
        return versionName;
    }

    @OnClick({R.id.iv_left, R.id.iv_right, R.id.iv_right_2, R.id.ll_select_type, R.id.ll_select_address,
            R.id.ll_select_sorting, R.id.ll_select_screening, R.id.ll_mine_info, R.id.ll_my_business, R.id.ll_setting,
            R.id.iv_input_call_show, R.id.iv_input_hide, R.id.iv_input_call, R.id.iv_input_delete, R.id.ll_header, R.id.ll_content})
    public void onViewClicked(View view) {
        drawerLayout.closeDrawer(Gravity.RIGHT);
        switch (view.getId()) {
            case R.id.iv_left:
            default:
                break;
            case R.id.iv_right:
                drawerLayout.openDrawer(Gravity.RIGHT);
                hideKeyboard(false);
                break;
            case R.id.iv_right_2:
                mainPresenter.getContactPermission();
                hideKeyboard(false);
                break;
            case R.id.ll_select_type:
                mainPresenter.showPartShadow(view, "industry");
                hideKeyboard(false);
                break;
            case R.id.ll_select_address:
                mainPresenter.showPartShadow(view, "address");
                hideKeyboard(false);
                break;
            case R.id.ll_select_sorting:
                mainPresenter.showPartShadow(view, "sorting");
                hideKeyboard(false);
                break;
            case R.id.ll_select_screening:
                mainPresenter.showScreeningPartShadow(view);
                hideKeyboard(false);
                break;
            case R.id.ll_mine_info:
                mainPresenter.goToMine();
                hideKeyboard(false);
                break;
            case R.id.ll_my_business:
                mainPresenter.goToMyBusiness();
                hideKeyboard(false);
                break;
            case R.id.ll_setting:
                mainPresenter.goToSetting();
                hideKeyboard(false);
                break;
            case R.id.iv_input_call_show:
                hideKeyboard(true);
                break;
            case R.id.ll_header:
            case R.id.ll_content:
            case R.id.iv_input_hide:
                hideKeyboard(false);
                break;
            case R.id.iv_input_call:
                mainPresenter.call(etInputText.getText().toString());
                break;
            case R.id.iv_input_delete:
                mainPresenter.deleteInput();
                break;
        }
    }



    @Override
    protected void onResume() {
        super.onResume();
        // 修复：首次没有数据的时候，选项为空，刷新数据刷不出来
        if (mainPresenter.isIndustryListNull()) {
            // 获取数据
            mainPresenter.getIndustryList();
        }
        // 每次回到首页都要获取一次通话时长，才能实时监听通话时长剩余
        mainPresenter.talkTimeInfo();
        if (!Constants.sipIsLogin) {
            // 登陆失败时，回到首页重新 -> 初始化-登陆
//            MyLinPhoneManager.getInstance().unInit(this);
            MyLinPhoneManager.getInstance(this).init();
            mainPresenter.getUserInfo();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mainPresenter.onActivityResult(requestCode, resultCode, data);
    }

//    @Override
//    public boolean dispatchKeyEvent(KeyEvent event) {
//        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK ) {
//            //do something.
//            return true;
//        } else {
//            return super.dispatchKeyEvent(event);
//        }
//    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if (drawerLayoutIsOpen) {
                drawerLayout.closeDrawer(Gravity.RIGHT);
            } else if (isShowInputNumber) {
                hideKeyboard(false);
            } else {
                if (mainPresenter != null) {
                    mainPresenter.exit();
                }
            }
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void industryChooseResult(String industryStr) {
        HandlerUtils.setText(tvSelectType, industryStr);
        mainPresenter.getRegionList(industryStr);
    }

    @Override
    public void addressChooseResult(String AddressStr) {
        HandlerUtils.setText(tvSelectAddress, AddressStr);
        mainPresenter.refreshData();
    }

    @Override
    public void sortingChooseResult(String sortingStr) {
        HandlerUtils.setText(tvSelectSorting, sortingStr);
        HandlerUtils.setTextColor(tvSelectSorting, this.getResources().getColor(R.color.blue_3f74fd));
        HandlerUtils.setImg(ivSelectSorting, this.getResources().getDrawable(R.drawable.ic_sorting_blue));
        mainPresenter.refreshData();
    }

    @Override
    public void screeningChooseResult(String screeningStr) {
        if (screeningStr.equals(mContext.getResources().getString(R.string.clues_screening))) {
            HandlerUtils.setText(tvSelectScreening, screeningStr);
            HandlerUtils.setTextColor(tvSelectScreening, this.getResources().getColor(R.color.gray_56575a));
            HandlerUtils.setImg(ivSelectScreening, this.getResources().getDrawable(R.drawable.ic_screening_gray));
        } else {
            HandlerUtils.setText(tvSelectScreening, screeningStr);
            HandlerUtils.setTextColor(tvSelectScreening, this.getResources().getColor(R.color.blue_3f74fd));
            HandlerUtils.setImg(ivSelectScreening, this.getResources().getDrawable(R.drawable.ic_screening_blue));
        }
        mainPresenter.refreshData();
    }

    @Override
    public void onUserInfoResult() {
        mainPresenter.userInfoSetText(rivHeadImg, tvName, tvPhoneNum);
    }

    @Override
    public void onTalkTimeResult() {
        mainPresenter.talkTimeAlarm(llAlarmCallTime, tvAlarmCallTime, viewPadding);
    }

    @Override
    public void refreshComplete() {
        xrvCallList.refreshComplete();
    }

    @Override
    public void loadComplete() {
        xrvCallList.loadMoreComplete();
    }

    @Override
    public void refreshKeyboardInput(String inputText) {
        HandlerUtils.setText(etInputText, inputText);
    }

    @Override
    public void hideKeyboard(boolean isVisible) {
        isShowInputNumber = isVisible;
        if (isVisible) {
            HandlerUtils.setVisible(llKeyboard, View.VISIBLE);
        } else {
            HandlerUtils.setVisible(llKeyboard, View.GONE);
        }
    }

    @Override
    public void onCluesListResult() {
        mainPresenter.setListData(tvCluesListCount);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        //MyLinPhoneManager.getInstance(mContext).unInit();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(String message) {
        if (message != null) {
            switch (message) {
                case Constants.LOGIN_FAILURE: // sip登陆失败
                    if (isNeedToReRegister) {
                        isNeedToReRegister = false;
                        timerCallBackUtils.start();
                    }
                    break;
                case Constants.TOKEN_TIMEOUT: // token超时
                    LogUtils.i("onEvent", "onResponse message --> " + message);
                    AppManager.finishAllActivity();
                    SharedPreferencesUtil.getSharedPreferences(mContext).clearAll();
                    mainPresenter.goToLoginStart();
                    break;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (PermissinsUtils.isPermissions(permissions, grantResults)) {
            mainPresenter.onRequestPermissionsResult(requestCode);
        }
    }

    /**
     * 侧栏滑出和隐藏的监听事件
     */
    DrawerLayout.DrawerListener drawerListener = new DrawerLayout.DrawerListener() {
        @Override
        public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

        }

        @Override
        public void onDrawerOpened(@NonNull View drawerView) {
            // 保存此至用于返回按钮的判断，是否滑出。     -- 滑出
            drawerLayoutIsOpen = true;
        }

        @Override
        public void onDrawerClosed(@NonNull View drawerView) {
            // 保存此至用于返回按钮的判断，是否滑出。     -- 未滑出
            drawerLayoutIsOpen = false;
        }

        @Override
        public void onDrawerStateChanged(int newState) {

        }
    };

    @Override
    public void onRefresh() {
        mainPresenter.refreshData();
        // 刷新同时刷新话务
        mainPresenter.talkTimeInfo();
    }

    @Override
    public void onLoadMore() {
        mainPresenter.loadData();
    }

    /**
     * 倒计时回调
     */
    TimerCallBackUtils.TimerCallBack callRingCallBack = new TimerCallBackUtils.TimerCallBack() {
        @Override
        public void onTickCallBack(long millisUntilFinished) {
        }

        @Override
        public void onFinishCallBack() {
            isNeedToReRegister = true;
            MyLinPhoneManager.getInstance(mContext).login();
        }
    };

}