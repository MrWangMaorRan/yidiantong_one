package com.yidiantong.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by Administrator on 2018/1/19.
 */

public class DialogWidthUtils {

    /**
     * 弹出框的边距
     *
     * @param context
     * @param dialog
     * @param marginWidth
     */
    public static void setDialogWidth(Context context, Dialog dialog, int marginWidth) {
        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.CENTER);
        lp.width = ((Activity) context).getWindowManager().getDefaultDisplay()
                .getWidth() - 2 * DensityUtils.dip2px(context, marginWidth);
        dialogWindow.setAttributes(lp);
    }

    /**
     * 弹出框的边距
     *
     * @param context
     * @param dialog
     */
    public static void setDialogWidth(Context context, Dialog dialog) {
        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.CENTER);
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        dialogWindow.setAttributes(lp);
    }

    /**
     * 从底部往上滑出
     *
     * @param dialog
     */
    public static void setDialogBottomUp(Dialog dialog) {
        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.BOTTOM);
        dialogWindow.setWindowAnimations(com.bigkoo.pickerview.R.style.picker_view_slide_anim);
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
//        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        dialogWindow.setAttributes(lp);
    }


    /**
     * 弹出框的边距
     *
     * @param dialog
     */
    public static void setDialogWidthWRAP(Dialog dialog) {
        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.CENTER);
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialogWindow.setAttributes(lp);
    }
}
