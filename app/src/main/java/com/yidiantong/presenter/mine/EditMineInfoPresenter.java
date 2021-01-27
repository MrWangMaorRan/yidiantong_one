package com.yidiantong.presenter.mine;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.yidiantong.R;
import com.yidiantong.bean.UserInfoBean;
import com.yidiantong.model.biz.mine.IEditMineInfo;
import com.yidiantong.model.impl.mine.EditMineInfoImpl;
import com.yidiantong.util.DensityUtils;
import com.yidiantong.util.StringUtils;
import com.yidiantong.util.ToastUtils;

import java.util.HashMap;
import java.util.Map;

public class EditMineInfoPresenter implements EditMineInfoImpl.OnCallBackListener {

    private Context mContext;
    private EditMineInfoImpl editMineInfoImpl;
    private IEditMineInfo iEditMineInfo;
    private UserInfoBean userInfoBean;

    public EditMineInfoPresenter(Context mContext, IEditMineInfo iEditMineInfo) {
        this.mContext = mContext;
        this.iEditMineInfo = iEditMineInfo;
        editMineInfoImpl = new EditMineInfoImpl();
    }

    /**
     * 赋值
     *
     * @param userInfoBean
     */
    public void setUserInfoBean(UserInfoBean userInfoBean) {
        this.userInfoBean = userInfoBean;
    }

    /**
     * 初始化
     */
    public void init(Button btnRight, EditText editText, String inputText) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                DensityUtils.dp2px(mContext, 50), DensityUtils.dp2px(mContext, 30));
        layoutParams.gravity = Gravity.CENTER_VERTICAL;
        layoutParams.rightMargin = DensityUtils.dp2px(mContext, 16);
        btnRight.setLayoutParams(layoutParams);
        btnRight.setText(mContext.getResources().getString(R.string.save));
        btnRight.setVisibility(View.VISIBLE);
        setHeadBtnBg(btnRight, inputText);
        setInputText(editText, inputText);
    }

    /**
     * 保存按钮样式
     *
     * @param btnRight
     */
    public void setHeadBtnBg(Button btnRight, String inputText) {
        if (!StringUtils.isNullOrBlank(inputText)) {
            btnRight.setBackground(mContext.getResources().getDrawable(R.drawable.btn_corner_4dp_blue));
            btnRight.setTextColor(mContext.getResources().getColor(R.color.white));
            btnRight.setEnabled(true);
        } else {
            btnRight.setBackground(mContext.getResources().getDrawable(R.drawable.bg_corner_4dp_gray_with_no_line));
            btnRight.setTextColor(mContext.getResources().getColor(R.color.gray_bcbec4));
            btnRight.setEnabled(false);
        }
    }

    /**
     * 输入框赋值
     *
     * @param editText
     * @param inputText
     */
    public void setInputText(EditText editText, String inputText) {
        if (editText != null && !StringUtils.isNullOrBlank(inputText)) {
            editText.setText(inputText);
        }
    }

    /**
     * 保存按钮样式
     *
     * @param inputText
     */
    public void userInfoUpdate(String inputText) {
        if (!StringUtils.isNullOrBlank(inputText)) {
            Map<String, String> map = new HashMap<>();
            map.put("title", inputText);
            map.put("path", userInfoBean.getPath());
            editMineInfoImpl.userInfoUpdate(mContext, map, this);
        }
    }

    @Override
    public void onUserInfoUpdateSuccess(String msg) {
        ToastUtils.showToast(mContext, "保存成功");
        ((Activity) mContext).setResult(Activity.RESULT_OK);
        iEditMineInfo.onUserInfoUpdateResult();
    }

    @Override
    public void onUserInfoUpdateFailure(String msg) {
        ToastUtils.showToast(mContext, "保存失败");
    }
}
