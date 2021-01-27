package com.yidiantong.presenter;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.text.InputType;
import android.text.SpannableString;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.SimpleCallback;
import com.yidiantong.MainActivity;
import com.yidiantong.R;
import com.yidiantong.StartActivity;
import com.yidiantong.adapter.CallListAdapter;
import com.yidiantong.adapter.KeyboardAdapter;
import com.yidiantong.app.MyLinPhoneManager;
import com.yidiantong.base.AppManager;
import com.yidiantong.base.Constants;
import com.yidiantong.bean.CluesBean;
import com.yidiantong.bean.CluesListBean;
import com.yidiantong.bean.CustomPSPViewBean;
import com.yidiantong.bean.FilterListBean;
import com.yidiantong.bean.IndustryBean;
import com.yidiantong.bean.IndustryListBean;
import com.yidiantong.bean.KeyboardBean;
import com.yidiantong.bean.RegionBean;
import com.yidiantong.bean.RegionListBean;
import com.yidiantong.bean.TalkTimeInfoBean;
import com.yidiantong.bean.UserInfoBean;
import com.yidiantong.bean.request.CallRecordsDto;
import com.yidiantong.bean.request.CluesDto;
import com.yidiantong.bean.request.FilterListDto;
import com.yidiantong.bean.request.SortDto;
import com.yidiantong.model.biz.IMain;
import com.yidiantong.model.impl.MainImpl;
import com.yidiantong.util.LQRPhotoSelectUtils;
import com.yidiantong.util.LoadImageUtils;
import com.yidiantong.util.PermissinsUtils;
import com.yidiantong.util.SharedPreferencesUtil;
import com.yidiantong.util.StringUtils;
import com.yidiantong.util.SysUtil;
import com.yidiantong.util.ToastUtils;
import com.yidiantong.util.log.LogUtils;
import com.yidiantong.view.company.CompanyInfoActivity;
import com.yidiantong.view.home.CallingActivity;
import com.yidiantong.view.home.PickContactActivity;
import com.yidiantong.view.mine.MineInfoActivity;
import com.yidiantong.view.setting.SettingActivity;
import com.yidiantong.widget.CustomPartShadowPopupViewLayout;
import com.yidiantong.widget.CustomPartShadowPopupViewList;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainPresenter implements MainImpl.OnCallBackListener {

    public final static int REQUEST_CODE_MINE_UPDATE = 0xa1;
    public final static int REQUEST_CODE_CLUES_UPDATE = 0xa2;
    public final static int REQUEST_CODE_CLUES_UPDATE_CONTACT = 0xa3;
    private Context mContext;
    private MainImpl mainImpl;
    private IMain iMain;
    private List<IndustryListBean> industryList;
    private List<RegionListBean> regionList;
    private String[] sortingArr;
    private int sortingSelectPosition = 0;
    private UserInfoBean userInfoBean;
    private TalkTimeInfoBean talkTimeInfoBean;
    // 退出时的时间
    private long mExitTime;
    private long exitTime = 2 * 1000; // 2秒内再按一次返回，则退出
    private CallListAdapter callListAdapter;
    private List<CluesListBean> cluesList;
    //    private String[] cluesParameter = new String[]{"", "", "", "", ""};
//    private RequestCluesList requestCluesList = new RequestCluesList();
    private CluesDto cluesDto;
    private FilterListDto filterListDto;
    private FilterListBean filterListBean;
    private int page = 1;
    private int timeInsufficient = 30; // 时间不足30分钟;
    private String callType;
    private String inputCallNumber = "";

    public MainPresenter(Context mContext, IMain iMain) {
        this.mContext = mContext;
        this.iMain = iMain;
        mainImpl = new MainImpl();
        industryList = new ArrayList<>();
        regionList = new ArrayList<>();
        filterListDto = new FilterListDto();
        sortingArr = mContext.getResources().getStringArray(R.array.arr_sorting); // 资源文件
        cluesDto = new CluesDto();
        filterListBean = new FilterListBean();
        cluesDto.setPage(page);
        callType = SharedPreferencesUtil.getSharedPreferences(mContext).getString("callType", "");
    }

    // 产品列表是否为空
    public boolean isIndustryListNull() {
        boolean isNull = false;
        if (industryList == null || industryList.size() <= 0) {
            isNull = true;
        }
        return isNull;
    }

    /**
     * 获取通讯录
     */
    public void getContactPermission() {
        String[] permission = new String[]{
                // 获取通讯录
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.READ_PHONE_STATE
        };

        if (!PermissinsUtils.hasPermissions((Activity) mContext, permission)) {
            PermissinsUtils.requestPermissions((Activity) mContext, permission, 0xab);
        } else {
            goToImportContact();
        }
    }

    /**
     * 获取权限回调
     */
    public void onRequestPermissionsResult(int requestCode) {
        switch (requestCode) {
            case 0xab: // 通讯录
                goToImportContact();
                break;
            default:
                break;
        }
    }


    // 刷新
    public void refreshData() {
        page = 1;
        cluesDto.setPage(page);
        getCluesList();
    }

    // 加载
    public void loadData() {
        page++;
        cluesDto.setPage(page);
        getCluesList();
    }

    public void goToMyBusiness() {
        Intent intent = new Intent(mContext, CompanyInfoActivity.class);
        mContext.startActivity(intent);
    }

    public void goToSetting() {
        Intent intent = new Intent(mContext, SettingActivity.class);
        mContext.startActivity(intent);
    }

    public void goToMine() {
        Intent intent = new Intent(mContext, MineInfoActivity.class);
        intent.putExtra("userInfoBean", userInfoBean);
        ((Activity) mContext).startActivityForResult(intent, REQUEST_CODE_MINE_UPDATE);
    }

    public void goToLoginStart() {
        Intent intent = new Intent(mContext, StartActivity.class);
        mContext.startActivity(intent);
    }

    public void goToImportContact() {
        Intent intent = new Intent(mContext, PickContactActivity.class);
        ((Activity) mContext).startActivityForResult(intent, REQUEST_CODE_CLUES_UPDATE_CONTACT);
    }

    // 输入的键盘初始化
    public void initKeyboard(RecyclerView rvNumber) {
        // 获取值
        String[] numbers = mContext.getResources().getStringArray(R.array.arr_input_number); // 资源文件
        String[] hints = mContext.getResources().getStringArray(R.array.arr_input_hint); // 资源文件

        // 赋值
        List<KeyboardBean> keyboardList = new ArrayList<>();
        if (numbers != null && hints != null && numbers.length >= 12 && hints.length >= 12) {
            for (int i = 0; i < 12; i++) {
                KeyboardBean bean = new KeyboardBean();
                bean.setKeyText(numbers[i]);
                bean.setHintText(hints[i]);
                keyboardList.add(bean);
            }
        }

        // 初始化适配器
        LinearLayoutManager layoutManager = new GridLayoutManager(mContext, 3);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        rvNumber.setLayoutManager(layoutManager);
        KeyboardAdapter keyboardAdapter = new KeyboardAdapter(mContext, keyboardList);
        rvNumber.setAdapter(keyboardAdapter);

        // 相关事件
        keyboardAdapter.setOnGridItemClickListener(new KeyboardAdapter.OnGridItemClickListener() {
            @Override
            public void onGridItemClick(int position, KeyboardBean keyboardBean) {
                inputCallNumber = inputCallNumber + keyboardBean.getKeyText();
                iMain.refreshKeyboardInput(inputCallNumber);
            }
        });
    }

    // 移除最后一个输入
    public void deleteInput() {
        if (!StringUtils.isNullOrBlank(inputCallNumber)) {
            inputCallNumber = inputCallNumber.substring(0, inputCallNumber.length() - 1);
        }
        iMain.refreshKeyboardInput(inputCallNumber);
    }

    public void call(String callNumber) {
        if (StringUtils.isNullOrBlank(callNumber)) {
            return;
        }
        if (talkTimeInfoBean != null && !StringUtils.isNullOrBlank(talkTimeInfoBean.getTrafficSurplus())) {
            double time = Double.parseDouble(talkTimeInfoBean.getTrafficSurplus());
            if (time > 0) {
                Intent intent = new Intent(mContext, CallingActivity.class);
                if (!StringUtils.isNullOrBlank(callNumber)) {
                    intent.putExtra("phoneNum", callNumber);
                } else {
                    intent.putExtra("phoneNum", "");
                }
                intent.putExtra("cluesList", (Serializable) cluesList);
                intent.putExtra("callPosition", -1);
                intent.putExtra("cluesId", "");
                intent.putExtra("industry", "");
                intent.putExtra("area", "");
                intent.putExtra("isInputCall", true);
                ((Activity) mContext).startActivityForResult(intent, MainPresenter.REQUEST_CODE_CLUES_UPDATE);
            } else {
                ToastUtils.showToast(mContext, "话务为0不可拨打");
            }
        } else {
            ToastUtils.showToast(mContext, "话务时间有误");
        }
    }

    // 初始化
    public void initAdapter(XRecyclerView xrvCallList) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        xrvCallList.setLayoutManager(layoutManager);
        callListAdapter = new CallListAdapter(mContext, iMain);
        xrvCallList.setAdapter(callListAdapter);

        callListAdapter.setOnCallClickListener(new CallListAdapter.OnCallClickListener() {
            @Override
            public void onCallClick(int position, CallRecordsDto callRecordsDto) {
                updateCallRecords(callRecordsDto);
            }
        });

    }

    // 赋值
    public void setListData(TextView tvCluesListCount) {
        if (callListAdapter != null) {
            callListAdapter.setCluesList(cluesList);
        }
        tvCluesListCount.setText(cluesList.size() + "");
    }

    // industry 设为全false
    public void setFalseIndustry() {
        if (industryList != null && industryList.size() > 0) {
            for (int i = 0; i < industryList.size(); i++) {
                industryList.get(i).setChecked(false);
            }
        }
    }

    // region 设为全false
    public void setFalseRegion() {
        if (regionList != null && regionList.size() > 0) {
            for (int i = 0; i < regionList.size(); i++) {
                regionList.get(i).setChecked(false);
            }
        }
    }

    /**
     * 根据type不同弹框弹框
     *
     * @param view 弹框弹出的参照view
     * @param type industry = 行业， address = 地区， sorting = 排序
     */
    public void showPartShadow(final View view, String type) {
        CustomPartShadowPopupViewList popupView = (CustomPartShadowPopupViewList) new XPopup.Builder(mContext)
                .atView(view)
//                    .isClickThrough(true)
//                    .dismissOnTouchOutside(false)
//                    .isCenterHorizontal(true)
                .autoOpenSoftInput(true)
//                    .offsetX(200)
//                .dismissOnTouchOutside(false)
                .setPopupCallback(new SimpleCallback() {
                    @Override
                    public void onShow() {

                    }

                    @Override
                    public void onDismiss() {

                    }
                })
                .asCustom(new CustomPartShadowPopupViewList(mContext, getPSPDialogData(type)))
                .show();
        setOnClickResultListener(popupView, type);
    }

    /**
     * 初始化弹框显示框数据
     */
    private List<CustomPSPViewBean> getPSPDialogData(String type) {
        List<CustomPSPViewBean> customPSPViewBeanList = new ArrayList<>();
        switch (type) {
            case "industry":
                for (int i = 0; i < industryList.size(); i++) {
                    CustomPSPViewBean bean = new CustomPSPViewBean();
                    IndustryListBean industry = industryList.get(i);
                    bean.setChecked(industry.isChecked());
                    bean.setText(industry.getIndustry());
                    bean.setRadio(true);
                    bean.setPosition(i);
                    customPSPViewBeanList.add(bean);
                }
                break;
            case "address":
                for (int i = 0; i < regionList.size(); i++) {
                    CustomPSPViewBean bean = new CustomPSPViewBean();
                    RegionListBean region = regionList.get(i);
                    bean.setChecked(region.isChecked());
                    bean.setText(region.getName());
                    bean.setId(region.getId());
                    bean.setRadio(false);
                    bean.setPosition(i);
                    customPSPViewBeanList.add(bean);
                }
                break;
            case "sorting":
                String[] sortingArr = mContext.getResources().getStringArray(R.array.arr_sorting);//资源文件
                for (int i = 0; i < sortingArr.length; i++) {
                    CustomPSPViewBean bean = new CustomPSPViewBean();
                    if (i == sortingSelectPosition) {
                        bean.setChecked(true);
                    } else {
                        bean.setChecked(false);
                    }
                    bean.setText(sortingArr[i]);
                    bean.setId(0);
                    bean.setRadio(true);
                    bean.setPosition(i);
                    customPSPViewBeanList.add(bean);
                }
                break;
            default:
                break;
        }

        return customPSPViewBeanList;
    }

    /**
     * 设置点击事件回调
     *
     * @param popupView
     */
    private void setOnClickResultListener(CustomPartShadowPopupViewList popupView, String type) {
        popupView.setOnItemClickListener(new CustomPartShadowPopupViewList.OnItemClickListener() {
            @Override
            public void onItemClick(List<Integer> position) {
                // 点击事件回调
                switch (type) {
                    case "industry":
                        setFalseIndustry();
                        for (int i = 0; i < position.size(); i++) {
                            industryList.get(position.get(i)).setChecked(true);
                            cluesDto.setIndustry(industryList.get(position.get(i)).getIndustry());
                            iMain.industryChooseResult(industryList.get(position.get(i)).getIndustry());
                            if (callListAdapter != null) {
                                callListAdapter.setIndustry(industryList.get(position.get(i)).getIndustry());
                            }
                        }
                        break;
                    case "address":
                        setFalseRegion();
                        String regionStr = "";
                        if (position.size() == regionList.size()) {
                            for (int i = 0; i < regionList.size(); i++) {
                                regionList.get(i).setChecked(true);
                            }
                            // 全选
                            regionStr = "全部";
                        } else {
                            // 非全选
                            for (int i = 0; i < position.size(); i++) {
                                regionList.get(position.get(i)).setChecked(true);
                                if (i == 0) {
                                    regionStr = regionStr + regionList.get(position.get(i)).getName();
                                } else {
                                    regionStr = regionStr + "," + regionList.get(position.get(i)).getName();
                                }
                            }
                        }
                        iMain.addressChooseResult(regionStr);
                        break;
                    case "sorting":
                        String sortingText = "";
                        for (int i = 0; i < position.size(); i++) {
                            if (i == 0) {
                                sortingText = sortingText + sortingArr[position.get(i)];
                            } else {
                                sortingText = sortingText + "," + sortingArr[position.get(i)];
                            }

                            // asc 通话时间从早到晚;
                            // desc;通话时间从晚到早
                            if (cluesDto.getSort() == null) {
                                cluesDto.setSort(new SortDto());
                            }
                            switch (position.get(i)) {
                                case 0:
//                                    cluesParameter[3] = "all";
//                                    cluesDto.setSort("all");
                                    cluesDto.getSort().setAllocateTime(null);
                                    cluesDto.getSort().setCallTime(null);
                                    break;
                                case 1:
//                                    cluesParameter[3] = "asc";
//                                    cluesDto.setSort("asc");
                                    cluesDto.getSort().setAllocateTime(null);
                                    cluesDto.getSort().setCallTime("desc");
                                    break;
                                case 2:
//                                    cluesParameter[3] = "asc";
//                                    cluesDto.setSort("asc");
                                    cluesDto.getSort().setAllocateTime("asc");
                                    cluesDto.getSort().setCallTime(null);
                                    break;
                                case 3:
//                                    cluesParameter[3] = "desc";
//                                    cluesDto.setSort("desc");
                                    cluesDto.getSort().setAllocateTime("desc");
                                    cluesDto.getSort().setCallTime(null);
                                    break;
                            }
                        }
                        sortingSelectPosition = position.get(0);
                        iMain.sortingChooseResult(sortingText);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    /**
     * 获取产品列表接口
     */
    public void getIndustryList() {
        mainImpl.getIndustryList(mContext, this);
    }


    /**
     * 获取地区列表
     */
    public void getRegionList(String industry) {
        Map<String, String> map = new HashMap<>();
        map.put("industry", industry);
        mainImpl.getRegionList(mContext, map, this);
    }

    /**
     * 获取个人信息
     */
    public void getUserInfo() {
        mainImpl.getUserInfo(mContext, new HashMap<>(), this);
    }

    /**
     * 获线索列表
     */
    public void getCluesList() {
//        Map<String, String> map = new HashMap<>();
//        map.put("area", cluesParameter[0]);
//        map.put("industry", cluesParameter[1]);
//        map.put("page", cluesParameter[2]);
//        // 选填
//        map.put("sort", cluesParameter[3]);
//        map.put("normal", cluesParameter[4]);
//        map.put("custom", new Gson().toJson(requestCluesList));
//        mainImpl.getCluesList(mContext, map, this);

        List<String> list = new ArrayList<>();
        for (RegionListBean regionListBean : regionList) {
            if (regionListBean.isChecked()) {
                list.add(regionListBean.getName());
            }
        }
        if (list != null && list.size() > 0 && list.get(0).equals("全部")) {
            // 去掉“全部”
            list.remove(0);
        }
        cluesDto.setArea(list);
        // 当sort的 allocateTime 和 callTime 都为空时，不传递sort
        if (cluesDto.getSort() != null && StringUtils.isNullOrBlank(cluesDto.getSort().getAllocateTime())
                && StringUtils.isNullOrBlank(cluesDto.getSort().getCallTime())) {
            cluesDto.setSort(null);
        }
        mainImpl.getCluesList(mContext, cluesDto, this);
    }

    /**
     * 账号剩余通话时长
     */
    public void talkTimeInfo() {
        mainImpl.talkTimeInfo(mContext, new HashMap<>(), this);
    }

    /**
     * 获取筛选数据
     */
    public void getFilterList() {
        filterListDto.setIndustry(cluesDto.getIndustry());
        List<String> list = new ArrayList<>();
        for (RegionListBean regionListBean : regionList) {
            list.add(regionListBean.getName());
        }
        // 去掉“全部”
        list.remove(0);
        filterListDto.setArea(list);
        mainImpl.getFilterList(mContext, filterListDto, this);
    }


    /**
     * 更新通话记录
     */
    public void updateCallRecords(CallRecordsDto callRecordsDto) {
        mainImpl.updateCallRecords(mContext, callRecordsDto, this);
    }

    /**
     * @param view 弹框弹出的参照view
     */
    public void showScreeningPartShadow(final View view) {
        CustomPartShadowPopupViewLayout popupView = (CustomPartShadowPopupViewLayout) new XPopup.Builder(mContext)
                .atView(view)
//                    .isClickThrough(true)
//                    .dismissOnTouchOutside(false)
//                    .isCenterHorizontal(true)
                .autoOpenSoftInput(true)
//                    .offsetX(200)
//                .dismissOnTouchOutside(false)
                .setPopupCallback(new SimpleCallback() {
                    @Override
                    public void onShow() {

                    }

                    @Override
                    public void onDismiss() {

                    }
                })
                .asCustom(new CustomPartShadowPopupViewLayout(mContext))
                .show();
        // 记录已选
        popupView.setCheck(cluesDto);
        // 赋值
        popupView.setFilterListBean(filterListBean);
        // 常规筛选
        popupView.setNormalSelect(cluesDto.getNormal());

        // 点击事件
        popupView.setOnConfirmClickListener(new CustomPartShadowPopupViewLayout.OnConfirmClickListener() {
            @Override
            public void onConfirmClickListener(CluesDto mCluesDto) {
                LogUtils.e("", "requestClues = " + mCluesDto.toString());
//                requestCluesList = requestClues;
//                ToastUtils.showToast(mContext, checkArr[0] + ", " + checkArr[1] + ", " + checkArr[2] + ", " + checkArr[3] + ", " + checkArr[4]);
                cluesDto = mCluesDto;
                if (StringUtils.isNullOrBlank(getScreeningText())) {
                    iMain.screeningChooseResult(mContext.getResources().getString(R.string.clues_screening));
                } else {
                    iMain.screeningChooseResult(getScreeningText());
                }
            }
        });

        popupView.setOnCancelClickListener(new CustomPartShadowPopupViewLayout.OnCancelClickListener() {
            @Override
            public void onCancelClickListener(CluesDto mCluesDto) {
                LogUtils.e("", "requestClues = " + mCluesDto.toString());
//                requestCluesList = requestClues;
                cluesDto = mCluesDto;
                iMain.screeningChooseResult(mContext.getResources().getString(R.string.clues_screening));
            }
        });
    }

    /**
     * 赋值个人信息
     *
     * @param imageView
     * @param tvName
     * @param tvPhone
     */
    public void userInfoSetText(ImageView imageView, TextView tvName, TextView tvPhone) {
        if (userInfoBean != null) {
            if (imageView != null) {
                LoadImageUtils.loadImage(imageView, userInfoBean.getPath());
            }
            if (tvName != null) {
                tvName.setText(userInfoBean.getTitle());
            }
            if (tvPhone != null) {
                tvPhone.setText(userInfoBean.getPhoneNumber());
            }
        }
    }

    /**
     * 退出
     */
    public void exit() {
        if ((System.currentTimeMillis() - mExitTime) > exitTime) {
            ToastUtils.showToast(mContext, "再按一次退出");
            mExitTime = System.currentTimeMillis();
        } else {
            // 退出不执行destroy，主动执行
            MyLinPhoneManager.getInstance(mContext).unInit();
            // APP退出
            AppManager.AppExit(mContext);
        }
    }

    /**
     * 通话时长警告
     */
    public void talkTimeAlarm(LinearLayout linearLayout, TextView textView, View view) {
        if (talkTimeInfoBean != null) {
            if (!StringUtils.isNullOrBlank(talkTimeInfoBean.getTrafficSurplus())) {
                double time = Double.parseDouble(talkTimeInfoBean.getTrafficSurplus());
                if (time <= timeInsufficient) {
                    linearLayout.setVisibility(View.VISIBLE);
                    view.setVisibility(View.GONE);
                    String textStr = mContext.getResources().getString(R.string.alarm_call_time) + Constants.phoneNum;
                    SpannableString spannableString = StringUtils.matcherSearchTitle(
                            mContext.getResources().getColor(R.color.blue_3f74fd), textStr, Constants.phoneNum);
                    textView.setText(spannableString);
                    // 拨打运维人员电话
                    linearLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            SysUtil.callPhone(mContext, Constants.phoneNum);
                        }
                    });
                } else {
                    linearLayout.setVisibility(View.GONE);
                    view.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    /**
     * 回调
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_MINE_UPDATE:
                    if (data != null) {
                        userInfoBean = (UserInfoBean) data.getSerializableExtra("userInfoBean");
                        iMain.onUserInfoResult();
                    }
                    break;
                case REQUEST_CODE_CLUES_UPDATE:
                case REQUEST_CODE_CLUES_UPDATE_CONTACT:
                    refreshData();
                    break;
                default:
                    break;
            }
        }
    }


    @Override
    public void onGetCluesListSuccess(CluesBean cluesBean) {
        iMain.refreshComplete();
        iMain.loadComplete();
        if (cluesBean != null) {
            if (cluesBean.getCluesList() == null) {
                cluesBean.setCluesList(new ArrayList<>());
            }
            if (page == 1) {
                cluesList = cluesBean.getCluesList();
            } else {
                cluesList.addAll(cluesBean.getCluesList());
            }
            iMain.onCluesListResult();
        } else {
            if (page > 1) {
                page--;
            }
        }
    }

    @Override
    public void onGetCluesListFailure(String msg) {
        iMain.refreshComplete();
        iMain.loadComplete();
        if (page > 1) {
            page--;
        }
    }

    @Override
    public void onIndustryListSuccess(IndustryBean industryBean) {
        if (industryBean != null && industryBean.getIndustryList() != null && industryBean.getIndustryList().size() > 0) {
            for (int i = 0; i < industryBean.getIndustryList().size(); i++) {
                IndustryListBean industryListBean = new IndustryListBean();
                industryListBean.setIndustry(industryBean.getIndustryList().get(i));
                if (i == 0) {
                    industryListBean.setChecked(true);
                } else {
                    industryListBean.setChecked(false);
                }
                industryList.add(industryListBean);
            }
            if (callListAdapter != null) {
                callListAdapter.setIndustry(industryList.get(0).getIndustry());
            }
//            cluesParameter[1] = industryBean.getIndustryList().get(0);
            cluesDto.setIndustry(industryBean.getIndustryList().get(0));
            iMain.industryChooseResult(industryBean.getIndustryList().get(0));
            getRegionList(industryBean.getIndustryList().get(0));
        }
    }

    @Override
    public void onIndustryListFailure(String msg) {

    }

    @Override
    public void onRegionListSuccess(RegionBean regionBean) {
        regionList = new ArrayList<>();
        RegionListBean regionListBean = new RegionListBean();
        regionListBean.setChecked(true);
        regionListBean.setName("全部");
        regionListBean.setId(0);
        regionList.add(regionListBean);

        if (regionBean != null && regionBean.getRegionList() != null && regionBean.getRegionList().size() > 0) {
            regionList.addAll(regionBean.getRegionList());
            for (int i = 0; i < regionList.size(); i++) {
                regionList.get(i).setChecked(true);
            }
//            cluesParameter[0] = regionList.get(0).getName();
            iMain.addressChooseResult(regionList.get(0).getName());
            getFilterList();
        }
    }

    @Override
    public void onRegionListFailure(String msg) {

    }

    @Override
    public void onGetUserInfoSuccess(UserInfoBean userInfoBean) {

        if (userInfoBean != null && userInfoBean.getSip() != null) {
            SharedPreferencesUtil.getSharedPreferences(mContext).putString("sipAccount", userInfoBean.getSip().getUsername());
            SharedPreferencesUtil.getSharedPreferences(mContext).putString("sipPassword", userInfoBean.getSip().getPassword());
            Log.i("sada","dad");

            MyLinPhoneManager.getInstance(mContext).login();
        }
        this.userInfoBean = userInfoBean;
        iMain.onUserInfoResult();
    }

    @Override
    public void onGetUserInfoFailure(String msg) {

    }

    @Override
    public void onGetTalkTimeInfoSuccess(TalkTimeInfoBean talkTimeInfoBean) {
        this.talkTimeInfoBean = talkTimeInfoBean;
        iMain.onTalkTimeResult();
        if (callListAdapter != null) {
            callListAdapter.setTalkTimeInfoBean(talkTimeInfoBean);
        }
    }

    @Override
    public void onGetTalkTimeInfoFailure(String msg) {

    }

    @Override
    public void onGetFilterListSuccess(FilterListBean filterListBean) {
        if (filterListBean != null) {
            this.filterListBean = filterListBean;
        }
    }

    @Override
    public void onGetFilterListFailure(String msg) {
        LogUtils.i("MainPresenter", "获取筛选列表失败");
    }

    @Override
    public void onUpdateCallRecordsSuccess() {
        LogUtils.i("MainPresenter", "更新通话记录成功");
    }

    @Override
    public void onUpdateCallRecordsFailure(String msg) {
        LogUtils.i("MainPresenter", "更新通话记录失败");
    }


    // 筛选
    private String getScreeningText() {
        String gender = "";
        for (int i = 0; i < cluesDto.getCustom().getGender().size(); i++) {
            if (i == cluesDto.getCustom().getGender().size() - 1) {
                gender = gender + cluesDto.getCustom().getGender().get(i);
            } else {
                gender = gender + cluesDto.getCustom().getGender().get(i) + ",";
            }
        }

        String intention = "";
        for (int i = 0; i < cluesDto.getCustomerIntention().size(); i++) {
            if (i == cluesDto.getCustomerIntention().size() - 1) {
                intention = intention + cluesDto.getCustomerIntention().get(i);
            } else {
                intention = intention + cluesDto.getCustomerIntention().get(i) + ",";
            }
        }

        String industry = "";
        for (int i = 0; i < cluesDto.getCustom().getIndustry().size(); i++) {
            if (i == cluesDto.getCustom().getIndustry().size() - 1) {
                industry = industry + cluesDto.getCustom().getIndustry().get(i);
            } else {
                industry = industry + cluesDto.getCustom().getIndustry().get(i) + ",";
            }
        }

//        String area = "";
//        for (int i = 0; i < cluesDto.getCustom().getCity().size(); i++) {
//            if (i == cluesDto.getCustom().getCity().size() - 1) {
//                area = area + cluesDto.getCustom().getCity().get(i);
//            } else {
//                area = area + cluesDto.getCustom().getCity().get(i) + ",";
//            }
//        }

        String other = "";
        for (int i = 0; i < cluesDto.getOtherTags().size(); i++) {
            if (i == cluesDto.getOtherTags().size() - 1) {
                other = other + cluesDto.getOtherTags().get(i);
            } else {
                other = other + cluesDto.getOtherTags().get(i) + ",";
            }
        }

        // Screening 值拼接
        String text = "";
        if (!StringUtils.isNullOrBlank(gender)) {
            text = text + gender;
            if (!StringUtils.isNullOrBlank(intention)) {
                text = text + "," + intention;
            } else {
                if (!StringUtils.isNullOrBlank(industry)) {
                    text = text + "," + industry;
                } else {
//                    if (!StringUtils.isNullOrBlank(area)) {
//                        text = text + "," + area;
//                    } else {
                    if (!StringUtils.isNullOrBlank(other)) {
                        text = text + "," + other;
                    }
//                    }
                }
            }
        } else {
            if (!StringUtils.isNullOrBlank(intention)) {
                text = text + intention;
            } else {
                if (!StringUtils.isNullOrBlank(industry)) {
                    text = text + industry;
                } else {
//                    if (!StringUtils.isNullOrBlank(area)) {
//                        text = text + area;
//                    } else {
                    if (!StringUtils.isNullOrBlank(other)) {
                        text = text + other;
                    }
//                    }
                }
            }
        }

        return text;
    }

    public void killAppProcess() {
        //注意：不能先杀掉主进程，否则逻辑代码无法继续执行，需先杀掉相关进程最后杀掉主进程
        ActivityManager mActivityManager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> mList = mActivityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo runningAppProcessInfo : mList) {
            if (runningAppProcessInfo.pid != android.os.Process.myPid()) {
                android.os.Process.killProcess(runningAppProcessInfo.pid);
            }
        }
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }
}
