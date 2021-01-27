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

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bigkoo.pickerview.view.TimePickerView;
import com.contrarywind.view.WheelView;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnInputConfirmListener;
import com.lxj.xpopup.interfaces.OnSelectListener;
import com.yidiantong.R;
import com.yidiantong.bean.CluesDetailBean;
import com.yidiantong.bean.request.UpdateCluesInfoDto;
import com.yidiantong.model.biz.home.IEditCompanyInfo;
import com.yidiantong.model.impl.home.EditCompanyInfoImpl;
import com.yidiantong.util.DensityUtils;
import com.yidiantong.util.Utils;
import com.yidiantong.util.log.LogUtils;
import com.yidiantong.util.StringUtils;
import com.yidiantong.util.TimeFormat;
import com.yidiantong.util.TimePickerUtils;
import com.yidiantong.util.ToastUtils;
import com.yidiantong.widget.FlowLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class EditCompanyInfoPresenter implements EditCompanyInfoImpl.OnUpdateCluesInfoListener {

    private EditCompanyInfoImpl editCompanyInfoImpl;
    private IEditCompanyInfo iEditCompanyInfo;
    private Context mContext;
    private CluesDetailBean cluesDetailBean;
    // 生日
    private TimePickerView timePickerDateView;
    // 行业
    private OptionsPickerView timePickerPeriodView;
    private List<String> industryList = new ArrayList<>();
    private UpdateCluesInfoDto updateCluesInfoDto;

    public EditCompanyInfoPresenter(Context mContext, IEditCompanyInfo iEditCompanyInfo) {
        this.iEditCompanyInfo = iEditCompanyInfo;
        this.mContext = mContext;
        editCompanyInfoImpl = new EditCompanyInfoImpl();
        updateCluesInfoDto = new UpdateCluesInfoDto();
        // 选择行业
        String[] industryArr = mContext.getResources().getStringArray(R.array.industry); // 资源文件
        for (int i = 0; i < industryArr.length; i++) {
            industryList.add(industryArr[i]);
        }

        // 初始化时间选择器
        initDatePicker();
        //
        initIndustryPicker();
    }

    // 赋值
    public void setCluesDetailBean(CluesDetailBean cluesDetailBean, String cluesId) {
        this.cluesDetailBean = cluesDetailBean;
        // 参数
        updateCluesInfoDto.setCluesId(cluesId);
        if (cluesDetailBean != null && cluesDetailBean.getTags() != null) {
            updateCluesInfoDto.setTags(cluesDetailBean.getTags());
            LogUtils.e("", "tags = > " + StringUtils.listToString(updateCluesInfoDto.getTags()));
        }
    }

    // 初始化赋值
    public void initSetData(EditText etVirtualNumberDisplay, EditText etNumberAttributionPlace, EditText etName,
                            TextView tvGender, TextView tvIndustry, TextView tvBirthday, EditText etWeChatNum,
                            EditText etPhoneNum, EditText etDescribe, FlowLayout flTags) {
        if (cluesDetailBean != null) {
            etVirtualNumberDisplay.setText(cluesDetailBean.getNumber());
            etNumberAttributionPlace.setText(cluesDetailBean.getNumberLocation());
            etName.setText(cluesDetailBean.getName());
            tvGender.setText(cluesDetailBean.getGender());
            tvIndustry.setText(cluesDetailBean.getIndustry());
            tvBirthday.setText(cluesDetailBean.getBirthday());
            etWeChatNum.setText(cluesDetailBean.getWechatId());
            etPhoneNum.setText(cluesDetailBean.getPhoneNumber());
            etDescribe.setText(cluesDetailBean.getDescription());
            addTagsView(flTags);
        }
    }

    // tag的addView
    public void addTagsView(FlowLayout flTagList) {
        if (updateCluesInfoDto.getTags() != null) {
            // 去掉重复元素
            updateCluesInfoDto.setTags(Utils.getRemoveList(updateCluesInfoDto.getTags()));

            // 加入布局
            flTagList.removeAllViews();

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    DensityUtils.dp2px(mContext, 26));

            LinearLayout.LayoutParams tvLayoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            LinearLayout.LayoutParams ivLayoutParams = new LinearLayout.LayoutParams(
                    DensityUtils.dp2px(mContext, 16), DensityUtils.dp2px(mContext, 16));
            for (int i = 0; i < updateCluesInfoDto.getTags().size(); i++) {
                // tvTag
                TextView textView = new TextView(mContext);
                textView.setText(updateCluesInfoDto.getTags().get(i));
                textView.setSingleLine();
                textView.setTextSize(14);
                textView.setTextColor(mContext.getResources().getColor(R.color.black_3c3c3c));
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
                        updateCluesInfoDto.getTags().remove(view.getId());
                        iEditCompanyInfo.setTagList();
                    }
                });

                // linearLayout
                layoutParams.rightMargin = DensityUtils.dp2px(mContext, 6);
                layoutParams.topMargin = DensityUtils.dp2px(mContext, 6);
                LinearLayout linearLayout = new LinearLayout(mContext);
                linearLayout.setGravity(Gravity.CENTER);
                linearLayout.setBackground(mContext.getResources().getDrawable(R.drawable.bg_corner_2dp_blue_light_with_deep_line)); //设置背景
                linearLayout.setLayoutParams(layoutParams);
                linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                linearLayout.setPadding(DensityUtils.dp2px(mContext, 6), 0, DensityUtils.dp2px(mContext, 6), 0);
                linearLayout.addView(textView);
                linearLayout.addView(imageView);

                flTagList.addView(linearLayout);
            }

            // 自定义
            TextView textView = new TextView(mContext);
            textView.setText(mContext.getResources().getString(R.string.user_defined));
            textView.setSingleLine();
            textView.setTextSize(14);
            textView.setTextColor(mContext.getResources().getColor(R.color.black_3c3c3c));
            textView.setLayoutParams(tvLayoutParams);
            // 自定义图标
            ImageView imageView = new ImageView(mContext);
            imageView.setScaleType(ImageView.ScaleType.CENTER);
            imageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_input_edit));
            imageView.setLayoutParams(ivLayoutParams);
            // 自定义tag布局linearLayout
            layoutParams.topMargin = DensityUtils.dp2px(mContext, 6);
            LinearLayout linearLayout = new LinearLayout(mContext);
            linearLayout.setGravity(Gravity.CENTER);
            linearLayout.setBackground(mContext.getResources().getDrawable(R.drawable.bg_corner_2dp_blue_light_with_deep_line)); //设置背景
            linearLayout.setLayoutParams(layoutParams);
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            linearLayout.setPadding(DensityUtils.dp2px(mContext, 6), 0, DensityUtils.dp2px(mContext, 6), 0);
            linearLayout.addView(imageView);
            linearLayout.addView(textView);
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

    public void showDialogGender() {
        new XPopup.Builder(mContext)
//                        .maxWidth(600)
                .isDarkTheme(false)
                .isDestroyOnDismiss(true) //对于只使用一次的弹窗，推荐设置这个
                .asCenterList("", mContext.getResources().getStringArray(R.array.gender),
                        new OnSelectListener() {
                            @Override
                            public void onSelect(int position, String text) {
                                iEditCompanyInfo.onGenderResult(text);
                            }
                        })
//                        .bindLayout(R.layout.my_custom_attach_popup) //自定义布局
                .show();
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
                                String[] tagArr = text.split("，");
                                for (int i = 0; i < tagArr.length; i++) {
                                    updateCluesInfoDto.getTags().add(tagArr[i]);
                                }
                                iEditCompanyInfo.setTagList();
                            }
                        })
                .show();
    }

    /**
     * 生日
     */
    public void initDatePicker() {

        // 选中时间
        Calendar selectedDate = Calendar.getInstance();
        if (!StringUtils.isNullOrBlank(updateCluesInfoDto.getBirthday())) {
            TimeFormat.date2TimeStamp(updateCluesInfoDto.getBirthday(), "");
            // 开始时间
            String[] startDateArr = updateCluesInfoDto.getBirthday().split("-");
            int startYear = Integer.parseInt(startDateArr[0]);
            // Calendar月份为0-11，所以需要-1
            int startMonth = Integer.parseInt(startDateArr[1]) - 1;
            int startDay = Integer.parseInt(startDateArr[2]);
            selectedDate.set(startYear, startMonth, startDay);
        }

        //时间选择器 ，自定义布局
        timePickerDateView = new TimePickerBuilder(mContext, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) { //选中事件回调
                iEditCompanyInfo.onChooseBirthdayResult(TimeFormat.dateToStringYyyyMMdd(date));
            }
        })
                .setTitleText("生日")
                .setDate(selectedDate) // 设置当前日期为选中日期
//                .setRangDate(startDate, endDate) // 设置开始日期和结束日期
                .setBackgroundId(mContext.getResources().getColor(R.color.white))
                .setType(new boolean[]{true, true, true, false, false, false}) // 显示哪个滚轮
                // 每个布局后面增加的备注，如：“年”，“月”，“日”，“时”，“分”，“秒”
                .setLabel("年", "月", "日", "", "", "")
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setTextColorCenter(mContext.getResources().getColor(R.color.black_242528))
                .setContentTextSize(17)
                .setTextColorOut(mContext.getResources().getColor(R.color.gray_a8a9ae))
                .setOutTextSize(15)
                .setDividerColor(mContext.getResources().getColor(R.color.gray_e0e0e6)) // 分割线颜色
                .setDividerWidth(5) // 分割线高度
                .setDividerType(WheelView.DividerType.WRAP) // 分割线是否布满
                .setItemHeight(DensityUtils.dp2px(mContext, 45)) // 行高
                .setItemVisibleCount(5) // 显示多少行（这里显示 7-2=5 行）
                .isDialog(true) // 是否为弹框，如果不是弹框，会把虚拟的三个按键遮挡
                .build();

        // 设置了isDialog为true后，布局为弹框样式。
        // 弹框从底部往上滑出需要对弹框进行以下处理：
        TimePickerUtils.initDialog(timePickerDateView);
    }

    /**
     * 弹框
     *
     * @description 注意事项：
     * 自定义布局中，id为 optionspicker 或者 timepicker 的布局以及其子控件必须要有，否则会报空指针。
     * 具体可参考demo 里面的两个自定义layout布局。
     */
    public void initIndustryPicker() {
        timePickerPeriodView = new OptionsPickerBuilder(mContext, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                // 返回的分别是三个级别的选中位置
                iEditCompanyInfo.onChooseIndustryResult(industryList.get(options1));
            }
        })
                .setTitleText("行业")
                .setBackgroundId(mContext.getResources().getColor(R.color.white))
                .setTextColorCenter(mContext.getResources().getColor(R.color.black_242528))
                .setContentTextSize(14)
                .setTextColorOut(mContext.getResources().getColor(R.color.gray_a8a9ae))
                .setOutTextSize(14)
                .setDividerColor(mContext.getResources().getColor(R.color.gray_e0e0e6))
                .setDividerWidth(2) // 分割线高度
                .setDividerType(WheelView.DividerType.FILL) // 分割线是否布满
                .setItemHeight(DensityUtils.dp2px(mContext, 45))
                .setSelectOptions(0) // 默认选中项
                .isRestoreItem(true) // 切换时是否还原，设置默认选中第一项。
                .setItemVisibleCount(5) // 显示多少行（这里显示 7-2=5 行）
                .isDialog(true) // 是否为弹框，如果不是弹框，会把虚拟的三个按键遮挡
                .build();

        timePickerPeriodView.setPicker(industryList); // 添加数据
        // 设置了isDialog为true后，布局为弹框样式。
        // 弹框从底部往上滑出需要对弹框进行以下处理：
        TimePickerUtils.initDialog(timePickerPeriodView);
    }

    //
    public void showIndustryDialog() {
        if (timePickerPeriodView != null) {
            timePickerPeriodView.show();
        }
    }

    //
    public void showBirthdayDialog() {
        if (timePickerDateView != null) {
            timePickerDateView.show();
        }
    }

    // 获取参数
    public void setUpdateCluesParameter(EditText etName, TextView tvGender, TextView tvIndustry, TextView tvBirthday,
                                        EditText etPhoneNum, EditText etWeChatNum, EditText etDescribe) {
        updateCluesInfoDto.setName(etName.getText().toString().trim());
        updateCluesInfoDto.setGender(tvGender.getText().toString().trim());
        updateCluesInfoDto.setIndustry(tvIndustry.getText().toString().trim());
        updateCluesInfoDto.setBirthday(tvBirthday.getText().toString().trim());
        updateCluesInfoDto.setPhoneNumber(etPhoneNum.getText().toString().trim());
        updateCluesInfoDto.setWechatId(etWeChatNum.getText().toString().trim());
        updateCluesInfoDto.setDescription(etDescribe.getText().toString().trim());
    }

    /**
     * 编辑信息
     */
    public void updateCluesInfo() {
//        Map<String, String> map = new HashMap<>();
//        map.put("cluesId", updateCluesParameter[0]);
//        map.put("name", updateCluesParameter[1]);
//        map.put("gender", updateCluesParameter[2]);
//        map.put("industry", updateCluesParameter[3]);
//        map.put("birthday", updateCluesParameter[4]);
//        map.put("phoneNumber", updateCluesParameter[5]);
//        map.put("wechatId", updateCluesParameter[6]);
//        map.put("description", updateCluesParameter[7]);
//        map.put("tags", new Gson().toJson(tags));

        if (!StringUtils.isNullOrBlank(updateCluesInfoDto.getPhoneNumber())) {
            if (!StringUtils.isPhoneType(updateCluesInfoDto.getPhoneNumber())) {
                ToastUtils.showToast(mContext, "请输入正确的手机号");
                return;
            }
        }

        if (updateCluesInfoDto.getName().equals(cluesDetailBean.getName())
                && updateCluesInfoDto.getGender().equals(cluesDetailBean.getGender())
                && updateCluesInfoDto.getIndustry().equals(cluesDetailBean.getIndustry())
                && updateCluesInfoDto.getBirthday().equals(cluesDetailBean.getBirthday())
                && updateCluesInfoDto.getWechatId().equals(cluesDetailBean.getWechatId())
                && updateCluesInfoDto.getPhoneNumber().equals(cluesDetailBean.getPhoneNumber())
                && updateCluesInfoDto.getDescription().equals(cluesDetailBean.getDescription())
                && updateCluesInfoDto.getTags().equals(cluesDetailBean.getTags())) {
            // 数据没有发生变化，直接退出
            ((Activity) mContext).finish();
        } else {
            editCompanyInfoImpl.updateCluesInfo(mContext, updateCluesInfoDto, this);
        }
    }

    @Override
    public void onUpdateCluesInfoSuccess(String msg) {
        ToastUtils.showToast(mContext, "保存成功");
        ((Activity) mContext).setResult(Activity.RESULT_OK);
        ((Activity) mContext).finish();
    }

    @Override
    public void onUpdateCluesInfoFailure(String msg) {
        ToastUtils.showToast(mContext, "保存失败");
    }
}
