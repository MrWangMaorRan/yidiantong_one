package com.yidiantong.widget;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.lxj.xpopup.impl.PartShadowPopupView;
import com.yidiantong.R;
import com.yidiantong.adapter.BtnCheckedAdapter;
import com.yidiantong.adapter.CustomPSPViewAdapter;
import com.yidiantong.bean.CustomPSPViewBean;
import com.yidiantong.bean.FilterListBean;
import com.yidiantong.bean.StringCheckedBean;
import com.yidiantong.bean.request.CluesDto;
import com.yidiantong.bean.request.CustomDto;
import com.yidiantong.util.log.LogUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Description: 自定义局部阴影弹窗
 * Create by dance, at 2018/12/21
 */
public class CustomPartShadowPopupViewLayout extends PartShadowPopupView {


    @BindView(R.id.svgv_customer_property)
    ScrollViewWithGridView svgvCustomerProperty;
    @BindView(R.id.svgv_customer_intention)
    ScrollViewWithGridView svgvCustomerIntention;
    @BindView(R.id.svgv_company_industry)
    ScrollViewWithGridView svgvCompanyIndustry;
    @BindView(R.id.svgv_customer_emotion)
    ScrollViewWithGridView svgvCustomerEmotion;
    @BindView(R.id.svgv_other)
    ScrollViewWithGridView svgvOther;
    @BindView(R.id.btn_confirm)
    Button btnConfirm;
    @BindView(R.id.btn_cancel)
    Button btnCancel;
    @BindView(R.id.rl_bottom_btn_layout)
    RelativeLayout rlBottomBtnLayout;
    @BindView(R.id.tv_normal)
    TextView tvNormal;
    @BindView(R.id.view_checked_normal)
    View viewCheckedNormal;
    @BindView(R.id.tv_defined)
    TextView tvDefined;
    @BindView(R.id.view_checked_defined)
    View viewCheckedDefined;
    @BindView(R.id.xrv_normal)
    XRecyclerView xrvNormal;
    @BindView(R.id.ll_custom)
    LinearLayout llCustom;

    private Context mContext;
    //    private RequestCluesList requestCluesList;
    private CluesDto cluesDto;
    //    private List<RegionListBean> regionBeanList;
    private int normalSelectPosition = 0;
    private FilterListBean filterListBean;

    public CustomPartShadowPopupViewLayout(@NonNull Context context) {
        super(context);
        mContext = context;
//        regionBeanList = new ArrayList<>();
    }


    public void setNormalSelect(String normalStr) {
        switch (normalStr) {
            case "default":
                normalSelectPosition = 0;
                break;
            case "allNoCalled":
                normalSelectPosition = 1;
                break;
            case "allNoConnected":
                normalSelectPosition = 2;
                break;
            case "allNoCalledAndAllNoConnected":
                normalSelectPosition = 3;
                break;
        }
    }

    public void setFilterListBean(FilterListBean filterListBean) {
        this.filterListBean = filterListBean;
    }

//    public void setRegionList(List<RegionListBean> list) {
//        regionBeanList = list;
//    }

    public void setCheck(CluesDto cluesDto) {
        this.cluesDto = cluesDto;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.layout_custom_part_shadow_popup_layout;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        ButterKnife.bind(this);
        LogUtils.e("tag", "CustomPartShadowPopupView onCreate");

        // 初始化适配器
        // 客户属性
        initAdapterCustomProperty();
        // 客户意向
        initAdapterCustomIntention();
        // 客戶情绪
        initAdapterCustomerEmotion();
        // 所属行业
        initAdapterCustomIndustry();
        // 其他
        initAdapterOther();
        // 常规筛选
        initAdapterNormal();

    }

    @Override
    protected void onShow() {
        super.onShow();
        LogUtils.e("tag", "CustomPartShadowPopupView onShow");
    }

    @Override
    protected void onDismiss() {
        super.onDismiss();
        LogUtils.e("tag", "CustomPartShadowPopupView onDismiss");
    }


    @OnClick({R.id.tv_normal, R.id.tv_defined, R.id.btn_confirm, R.id.btn_cancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_normal:
                tvNormal.setTextColor(mContext.getResources().getColor(R.color.blue_2469ef));
                viewCheckedNormal.setVisibility(VISIBLE);
                tvDefined.setTextColor(mContext.getResources().getColor(R.color.gray_525459));
                viewCheckedDefined.setVisibility(INVISIBLE);
                xrvNormal.setVisibility(VISIBLE);
                llCustom.setVisibility(GONE);
                break;
            case R.id.tv_defined:
                tvNormal.setTextColor(mContext.getResources().getColor(R.color.gray_525459));
                viewCheckedNormal.setVisibility(INVISIBLE);
                tvDefined.setTextColor(mContext.getResources().getColor(R.color.blue_2469ef));
                viewCheckedDefined.setVisibility(VISIBLE);
                xrvNormal.setVisibility(GONE);
                llCustom.setVisibility(VISIBLE);
                break;
            case R.id.btn_confirm:
                if (mListener != null) {
                    mListener.onConfirmClickListener(cluesDto);
                }
                dismiss();
                break;
            case R.id.btn_cancel:
                if (onCancelClickListener != null) {
                    cluesDto.setCustom(new CustomDto());
                    cluesDto.setCustomerIntention(new ArrayList<>());
                    cluesDto.setCustomerSentiment(new ArrayList<>());
                    cluesDto.setOtherTags(new ArrayList<>());
                    cluesDto.setNormal("default");
                    onCancelClickListener.onCancelClickListener(cluesDto);
                }
                dismiss();
                break;
        }
    }

    /**
     * comfirm点击处理事件
     */
    public OnConfirmClickListener mListener;


    public interface OnConfirmClickListener {
        void onConfirmClickListener(CluesDto sccluesDto);
    }

    public void setOnConfirmClickListener(OnConfirmClickListener mListener) {
        this.mListener = mListener;
    }

    /**
     * cancel点击处理事件
     */
    public OnCancelClickListener onCancelClickListener;

    public interface OnCancelClickListener {
        void onCancelClickListener(CluesDto cluesDto);
    }

    public void setOnCancelClickListener(OnCancelClickListener onCancelClickListener) {
        this.onCancelClickListener = onCancelClickListener;
    }


    public void initAdapterCustomProperty() {
        // 客户属性
        BtnCheckedAdapter propertyAdapter = new BtnCheckedAdapter(mContext);
        svgvCustomerProperty.setAdapter(propertyAdapter);
//        propertyAdapter.setRadio(true); // 单选
//        String[] arr = mContext.getResources().getStringArray(R.array.gender_unknown); // 资源文件
        List<StringCheckedBean> propertyList = new ArrayList<>();
        if (filterListBean != null && filterListBean.getCustomerGender() != null) {
            for (int i = 0; i < filterListBean.getCustomerGender().size(); i++) {
                StringCheckedBean bean = new StringCheckedBean();
                bean.setTitle(filterListBean.getCustomerGender().get(i).getName());
                if (cluesDto != null && cluesDto.getCustom() != null && cluesDto.getCustom().getGender() != null
                        && cluesDto.getCustom().getGender().size() > 0) {
                    for (int j = 0; j < cluesDto.getCustom().getGender().size(); j++) {
                        if (cluesDto.getCustom().getGender().get(j).equals(bean.getTitle())) {
                            bean.setChecked(true);
                        }
                    }
                }
                propertyList.add(bean);
            }
        }
        propertyAdapter.setStringCheckedList(propertyList, true);

        // 点击事件
        propertyAdapter.setOnBtnClickListener(new BtnCheckedAdapter.OnBtnClickListener() {
            @Override
            public void onBtnClick(String checkStr, boolean isChecked) {
//                    // 单选
//                    if (isChecked) {
//                        customDto.getGender().clear();
//                        customDto.getGender().add(checkStr);
//                    }

                // 多选
                if (cluesDto.getCustom() != null && cluesDto.getCustom().getGender() != null) {
                    // 判断数据是否点击的值
                    boolean hasChecked = false;
                    if (cluesDto.getCustom().getGender() != null) {
                        for (int i = 0; i < cluesDto.getCustom().getGender().size(); i++) {
                            if (checkStr.equals(cluesDto.getCustom().getGender().get(i))) {
                                hasChecked = true;
                            }
                        }
                    }
                    // 选中
                    if (isChecked) {
                        // 如果数据中没有选中的值，就添加进去
                        if (!hasChecked) {
                            cluesDto.getCustom().getGender().add(checkStr);
                        }
                    } else {
                        if (hasChecked) {
                            cluesDto.getCustom().getGender().remove(checkStr);
                        }
                    }
                }
            }
        });
    }

    public void initAdapterCustomIntention() {
        // 客户意向
        BtnCheckedAdapter intentionAdapter = new BtnCheckedAdapter(mContext);
        svgvCustomerIntention.setAdapter(intentionAdapter);
//        String[] arr = mContext.getResources().getStringArray(R.array.customer_intention); // 资源文件
        List<StringCheckedBean> intentionList = new ArrayList<>();
        if (filterListBean != null && filterListBean.getCustomerIntention() != null) {
            for (int i = 0; i < filterListBean.getCustomerIntention().size(); i++) {
                StringCheckedBean bean = new StringCheckedBean();
                bean.setTitle(filterListBean.getCustomerIntention().get(i).getName());
                if (cluesDto.getCustom() != null && cluesDto.getCustomerIntention() != null
                        && cluesDto.getCustomerIntention().size() > 0) {
                    for (int j = 0; j < cluesDto.getCustomerIntention().size(); j++) {
                        if (cluesDto.getCustomerIntention().get(j).equals(bean.getTitle())) {
                            bean.setChecked(true);
                        }
                    }
                }
                intentionList.add(bean);
            }
        }
        intentionAdapter.setStringCheckedList(intentionList, true);

        // 点击事件
        intentionAdapter.setOnBtnClickListener(new BtnCheckedAdapter.OnBtnClickListener() {
            @Override
            public void onBtnClick(String checkStr, boolean isChecked) {
                if (cluesDto != null && cluesDto.getCustomerIntention() != null) {
                    // 判断数据是否点击的值
                    boolean hasChecked = false;
                    if (cluesDto.getCustomerIntention() != null) {
                        for (int i = 0; i < cluesDto.getCustomerIntention().size(); i++) {
                            if (checkStr.equals(cluesDto.getCustomerIntention().get(i))) {
                                hasChecked = true;
                            }
                        }
                    }
                    // 选中
                    if (isChecked) {
                        // 如果数据中没有选中的值，就添加进去
                        if (!hasChecked) {
                            cluesDto.getCustomerIntention().add(checkStr);
                        }
                    } else {
                        if (hasChecked) {
                            cluesDto.getCustomerIntention().remove(checkStr);
                        }
                    }
                }
            }
        });
    }

    public void initAdapterCustomerEmotion() {
        // 所在地区
        BtnCheckedAdapter areaAdapter = new BtnCheckedAdapter(mContext);
        svgvCustomerEmotion.setAdapter(areaAdapter);
//        String[] arr = mContext.getResources().getStringArray(R.array.company_area); // 资源文件
        List<StringCheckedBean> areaList = new ArrayList<>();
        if (filterListBean != null && filterListBean.getCustomerSentiment() != null) {
            for (int i = 0; i < filterListBean.getCustomerSentiment().size(); i++) {
                StringCheckedBean bean = new StringCheckedBean();
                bean.setTitle(filterListBean.getCustomerSentiment().get(i).getName());
                if (cluesDto != null && cluesDto.getCustomerSentiment() != null
                        && cluesDto.getCustomerSentiment().size() > 0) {
                    for (int j = 0; j < cluesDto.getCustomerSentiment().size(); j++) {
                        if (cluesDto.getCustomerSentiment().get(j).equals(bean.getTitle())) {
                            bean.setChecked(true);
                        }
                    }
                }
                areaList.add(bean);
            }
        }
        areaAdapter.setStringCheckedList(areaList, true);

        // 点击事件
        areaAdapter.setOnBtnClickListener(new BtnCheckedAdapter.OnBtnClickListener() {
            @Override
            public void onBtnClick(String checkStr, boolean isChecked) {
                if (cluesDto != null && cluesDto.getCustomerSentiment() != null) {
                    // 判断数据是否点击的值
                    boolean hasChecked = false;
                    if (cluesDto.getCustomerSentiment() != null) {
                        for (int i = 0; i < cluesDto.getCustomerSentiment().size(); i++) {
                            if (checkStr.equals(cluesDto.getCustomerSentiment().get(i))) {
                                hasChecked = true;
                            }
                        }
                    }
                    // 选中
                    if (isChecked) {
                        // 如果数据中没有选中的值，就添加进去
                        if (!hasChecked) {
                            cluesDto.getCustomerSentiment().add(checkStr);
                        }
                    } else {
                        if (hasChecked) {
                            cluesDto.getCustomerSentiment().remove(checkStr);
                        }
                    }
                }
            }

        });
    }

    public void initAdapterCustomIndustry() {
        // 所属行业
        BtnCheckedAdapter industryAdapter = new BtnCheckedAdapter(mContext);
        svgvCompanyIndustry.setAdapter(industryAdapter);
//        String[] arr = mContext.getResources().getStringArray(R.array.custom_industry); // 资源文件
        List<StringCheckedBean> industryList = new ArrayList<>();
        if (filterListBean != null && filterListBean.getCustomerIndustry() != null) {
            for (int i = 0; i < filterListBean.getCustomerIndustry().size(); i++) {
                StringCheckedBean bean = new StringCheckedBean();
                bean.setTitle(filterListBean.getCustomerIndustry().get(i).getName());
                if (cluesDto != null && cluesDto.getCustom() != null && cluesDto.getCustom().getIndustry() != null
                        && cluesDto.getCustom().getIndustry().size() > 0) {
                    for (int j = 0; j < cluesDto.getCustom().getIndustry().size(); j++) {
                        if (cluesDto.getCustom().getIndustry().get(j).equals(bean.getTitle())) {
                            bean.setChecked(true);
                        }
                    }
                }
                industryList.add(bean);
            }
        }
        industryAdapter.setStringCheckedList(industryList, true);

        // 点击事件
        industryAdapter.setOnBtnClickListener(new BtnCheckedAdapter.OnBtnClickListener() {
            @Override
            public void onBtnClick(String checkStr, boolean isChecked) {
                if (cluesDto != null && cluesDto.getCustom() != null && cluesDto.getCustom().getIndustry() != null) {
                    // 判断数据是否点击的值
                    boolean hasChecked = false;
                    if (cluesDto.getCustom().getIndustry() != null) {
                        for (int i = 0; i < cluesDto.getCustom().getIndustry().size(); i++) {
                            if (checkStr.equals(cluesDto.getCustom().getIndustry().get(i))) {
                                hasChecked = true;
                            }
                        }
                    }
                    // 选中
                    if (isChecked) {
                        // 如果数据中没有选中的值，就添加进去
                        if (!hasChecked) {
                            cluesDto.getCustom().getIndustry().add(checkStr);
                        }
                    } else {
                        if (hasChecked) {
                            cluesDto.getCustom().getIndustry().remove(checkStr);
                        }
                    }
                }
            }
        });
    }

    public void initAdapterOther() {
        // 其他
        BtnCheckedAdapter otherAdapter = new BtnCheckedAdapter(mContext);
        svgvOther.setAdapter(otherAdapter);
//        String[] arr = mContext.getResources().getStringArray(R.array.other); // 资源文件
        List<StringCheckedBean> otherList = new ArrayList<>();
        if (filterListBean != null && filterListBean.getOtherTags() != null) {
            for (int i = 0; i < filterListBean.getOtherTags().size(); i++) {
                StringCheckedBean bean = new StringCheckedBean();
                bean.setTitle(filterListBean.getOtherTags().get(i).getName());
                if (cluesDto != null && cluesDto.getOtherTags() != null
                        && cluesDto.getOtherTags().size() > 0) {
                    for (int j = 0; j < cluesDto.getOtherTags().size(); j++) {
                        if (cluesDto.getOtherTags().get(j).equals(bean.getTitle())) {
                            bean.setChecked(true);
                        }
                    }
                }
                otherList.add(bean);
            }
        }
        otherAdapter.setStringCheckedList(otherList, true);

        // 点击事件
        otherAdapter.setOnBtnClickListener(new BtnCheckedAdapter.OnBtnClickListener() {
            @Override
            public void onBtnClick(String checkStr, boolean isChecked) {
                if (cluesDto != null && cluesDto.getOtherTags() != null) {
                    // 判断数据是否点击的值
                    boolean hasChecked = false;
                    if (cluesDto.getOtherTags() != null) {
                        for (int i = 0; i < cluesDto.getOtherTags().size(); i++) {
                            if (checkStr.equals(cluesDto.getOtherTags().get(i))) {
                                hasChecked = true;
                            }
                        }
                    }
                    // 选中
                    if (isChecked) {
                        // 如果数据中没有选中的值，就添加进去
                        if (!hasChecked) {
                            cluesDto.getOtherTags().add(checkStr);
                        }
                    } else {
                        if (hasChecked) {
                            cluesDto.getOtherTags().remove(checkStr);
                        }
                    }
                }
            }
        });
    }

    public void initAdapterNormal() {
        // 常规筛选
        String[] arr = mContext.getResources().getStringArray(R.array.normal); // 资源文件
        List<CustomPSPViewBean> normalList = new ArrayList<>();
        for (int i = 0; i < arr.length; i++) {
            CustomPSPViewBean customPSPViewBean = new CustomPSPViewBean();
            if (i == normalSelectPosition) {
                customPSPViewBean.setChecked(true);
            } else {
                customPSPViewBean.setChecked(false);
            }
            customPSPViewBean.setText(arr[i]);
            customPSPViewBean.setId(i);
            customPSPViewBean.setRadio(true);
            customPSPViewBean.setPosition(i);
            normalList.add(customPSPViewBean);
        }
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        xrvNormal.setLayoutManager(layoutManager);
        CustomPSPViewAdapter normalAdapter = new CustomPSPViewAdapter(mContext, normalList);
        xrvNormal.setAdapter(normalAdapter);
        xrvNormal.setPullRefreshEnabled(false);
        xrvNormal.setLoadingMoreEnabled(false);

        // 点击事件
        normalAdapter.setOnItemClickListener(new CustomPSPViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(List<CustomPSPViewBean> clickList) {
                if (clickList != null && clickList.size() > 0 && cluesDto != null) {
//                    normalSelectPosition = clickList.get(0).getPosition();
                    switch (clickList.get(0).getPosition()) {
                        case 0:
                        default:
                            cluesDto.setNormal("default");
                            break;
                        case 1:
                            cluesDto.setNormal("allNoCalled");
                            break;
                        case 2:
                            cluesDto.setNormal("allNoConnected");
                            break;
                        case 3:
                            cluesDto.setNormal("allNoCalledAndAllNoConnected");
                            break;
                    }
                }
            }
        });
    }
}
