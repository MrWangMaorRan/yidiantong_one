package com.yidiantong.util;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.yidiantong.R;


/**
 * Created by Administrator on 2018/5/21.
 */

public class WaitingDialogUtils {

    private Dialog dialog;
    private Context mContext;
    private boolean isClickOutSide;

    public WaitingDialogUtils(Context context, boolean isClickOutSide) {
        this.mContext = context;
        this.isClickOutSide = isClickOutSide;

        // 初始化弹框
        initDialog(context);
    }

    private void initDialog(Context context) {
        dialog = new Dialog(mContext, R.style.WaitingDialog);
        // 是否可点击边沿隐藏弹框
        dialog.setCancelable(isClickOutSide);

        // 弹框布局
        View view = View.inflate(mContext, R.layout.dialog_waiting, null);
        Animation operatingAnim = AnimationUtils.loadAnimation(mContext, R.anim.rotate_anim);
        LinearInterpolator lin = new LinearInterpolator();
        operatingAnim.setInterpolator(lin);

        ImageView ivLoadingImg = view.findViewById(R.id.iv_loading_img);
        ivLoadingImg.setAnimation(operatingAnim);

        dialog.setContentView(view);
    }

    // 显示loading
    public void show() {
        if (dialog != null) {
            // dialog 半透明左右边距
            DialogWidthUtils.setDialogWidthWRAP(dialog);
            // 展示dialog
           // dialog.show();
        }
    }

    // 隐藏loading
    public void dismiss() {
        if (dialog != null) {
            // 关闭dialog
            dialog.dismiss();
        }
    }
}
