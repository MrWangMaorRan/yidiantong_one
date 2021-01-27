package com.yidiantong.widget;

import android.content.Context;

import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnConfirmListener;
import com.lxj.xpopup.interfaces.OnInputConfirmListener;
import com.lxj.xpopup.interfaces.SimpleCallback;
import com.yidiantong.util.log.LogUtils;

public class DialogUtils {


    /**
     * 提示弹框
     *
     * @param mContext
     */
    public static void showDialog(Context mContext, String title, String content, OnConfirmListener onConfirmListener) {
        new XPopup.Builder(mContext)
//                        .hasBlurBg(true)
//                         .dismissOnTouchOutside(false)
//                         .autoDismiss(false)
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
                }).asConfirm(title, content, "取消", "确定", onConfirmListener, null, false)
                .show();
    }

    /**
     * 输入框的弹框
     *
     * @param mContext
     * @param title
     * @param content
     * @param hintText
     * @param onInputConfirmListener
     */
    public static void showEditTextDialog(Context mContext, String title, String content, String hintText, OnInputConfirmListener onInputConfirmListener) {
        new XPopup.Builder(mContext)
                //.dismissOnBackPressed(false)
                .isDestroyOnDismiss(true) //对于只使用一次的弹窗，推荐设置这个
                .autoOpenSoftInput(true)
//                        .autoFocusEditText(false) //是否让弹窗内的EditText自动获取焦点，默认是true
                .isRequestFocus(false)
                //.moveUpToKeyboard(false)   //是否移动到软键盘上面，默认为true
                .asInputConfirm(title, null, null, hintText, onInputConfirmListener)
                .show();
    }
}
