package com.yidiantong.presenter.mine;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnSelectListener;
import com.yidiantong.api.ApiConstants;
import com.yidiantong.app.MyLinPhoneManager;
import com.yidiantong.base.Constants;
import com.yidiantong.bean.UpLoadFileBean;
import com.yidiantong.bean.UserInfoBean;
import com.yidiantong.httpUtils.OkhttpUtil;
import com.yidiantong.model.biz.mine.IMine;
import com.yidiantong.model.impl.mine.MineImpl;
import com.yidiantong.util.ImageUtil;
import com.yidiantong.util.LQRPhotoSelectUtils;
import com.yidiantong.util.LoadImageUtils;
import com.yidiantong.util.PermissinsUtils;
import com.yidiantong.util.SharedPreferencesUtil;
import com.yidiantong.util.StringUtils;
import com.yidiantong.util.Utils;
import com.yidiantong.view.mine.EditMineInfoActivity;

import java.io.File;
import java.util.HashMap;

import sakura.bottommenulibrary.bottompopfragmentmenu.BottomMenuFragment;
import sakura.bottommenulibrary.bottompopfragmentmenu.MenuItem;

public class MinePresenter implements MineImpl.OnCallBackListener {

    public final static int EDIT_NAME_REQUEST_CODE = 0xa1;
    private Context mContext;
    private MineImpl mineImpl;
    private IMine iMine;
    private UserInfoBean userInfoBean;
    // 拍照/相册
    private LQRPhotoSelectUtils mLqrPhotoSelectUtils;
    // 上传的file头像
    private File headImgFile = null;
    private UpLoadFileBean upLoadFileBean;
    private String callType;

    public MinePresenter(Context mContext, IMine iMine) {
        this.mContext = mContext;
        this.iMine = iMine;
        mineImpl = new MineImpl();
        // 拍照/图册
        mLqrPhotoSelectUtils = new LQRPhotoSelectUtils((Activity) mContext, photoSelectListener, true);
        //
        callType = SharedPreferencesUtil.getSharedPreferences(mContext).getString("callType", "");
    }

    /**
     * 用户信息
     *
     * @param userInfoBean
     */
    public void setUserInfoBean(UserInfoBean userInfoBean) {
        this.userInfoBean = userInfoBean;
    }

    /**
     * 修改昵称
     */
    public void goToEditName() {
        Intent intent = new Intent(mContext, EditMineInfoActivity.class);
        intent.putExtra("title", "修改名字");
        intent.putExtra("userInfoBean", userInfoBean);
        ((Activity) mContext).startActivityForResult(intent, EDIT_NAME_REQUEST_CODE);
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
     * 修改头像
     */
    public void editHeadImg() {
        new BottomMenuFragment((Activity) mContext)
//                .setTitle("标题")
                .addMenuItems(new MenuItem("从相册选择", MenuItem.MenuItemStyle.COMMON))
                .addMenuItems(new MenuItem("拍照", MenuItem.MenuItemStyle.COMMON))
                .setOnItemClickListener(new BottomMenuFragment.OnItemClickListener() {
                    @Override
                    public void onItemClick(TextView menu_item, int position) {
                        switch (position) {
                            case 0:
                                photoAlbumOnClick();
                                break;
                            case 1:
                                takePhotoOnClick();
                                break;
                        }
                    }
                })
                .show();

    }

    /**
     * 获取个人信息
     */
    public void getUserInfo() {
        mineImpl.getUserInfo(mContext, new HashMap<>(), this);
    }

    /**
     * 上传图片文件
     */
    public void uploadAvatar() {
        mineImpl.uploadAvatar(mContext, headImgFile, "file", OkhttpUtil.FILE_TYPE_FILE, this);
    }

    // 拍照
    public void takePhotoOnClick() {
        String[] mPermissions = new String[]{Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (PermissinsUtils.hasPermissions((Activity) mContext, mPermissions)) {
            mLqrPhotoSelectUtils.takePhoto((Activity) mContext);
        } else {
            PermissinsUtils.requestPermissions((Activity) mContext, mPermissions,
                    LQRPhotoSelectUtils.PERM_CAMERA);
        }
    }

    // 相册
    public void photoAlbumOnClick() {
        String[] mPermissions = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (PermissinsUtils.hasPermissions((Activity) mContext, mPermissions)) {
            mLqrPhotoSelectUtils.selectPhoto((Activity) mContext);
        } else {
            PermissinsUtils.requestPermissions((Activity) mContext, mPermissions,
                    LQRPhotoSelectUtils.PERM_STORAGE);
        }
    }

    /**
     * @param requestCode
     */
    public void onRequestPermissionsResult(int requestCode) {
        switch (requestCode) {
            case LQRPhotoSelectUtils.PERM_CAMERA:
                // 拍照
                mLqrPhotoSelectUtils.takePhoto((Activity) mContext);
                break;
            case LQRPhotoSelectUtils.PERM_STORAGE:
                // 相册
                mLqrPhotoSelectUtils.selectPhoto((Activity) mContext);
                break;
            default:
                break;
        }
    }

    /**
     * 选择图片回调
     */
    LQRPhotoSelectUtils.PhotoSelectListener photoSelectListener = new LQRPhotoSelectUtils.PhotoSelectListener() {
        @Override
        public void onFinish(File outputFile, Uri outputUri) {
            // 显示的图片uri
            headImgFile = ImageUtil.fileCompress(outputFile);
            // 上传头像的file
            if (headImgFile != null) {
                uploadAvatar();
            }
        }
    };

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
                case EDIT_NAME_REQUEST_CODE:
                    getUserInfo();
                    break;
                default:
                    break;
            }
        }
        // 图册回调
        mLqrPhotoSelectUtils.attachToActivityForResult(requestCode, resultCode, data);
    }

    @Override
    public void onGetUserInfoSuccess(UserInfoBean userInfoBean) {
        if (userInfoBean != null && userInfoBean.getSip() != null) {
            SharedPreferencesUtil.getSharedPreferences(mContext).putString("sipAccount", userInfoBean.getSip().getUsername());
            SharedPreferencesUtil.getSharedPreferences(mContext).putString("sipPassword", userInfoBean.getSip().getPassword());
            MyLinPhoneManager.getInstance(mContext).login();
        }
        this.userInfoBean = userInfoBean;
        Intent intent = new Intent();
        intent.putExtra("userInfoBean", userInfoBean);
        ((Activity) mContext).setResult(Activity.RESULT_OK, intent);
        iMine.onUserInfoUpdateResult();
    }

    @Override
    public void onGetUserInfoFailure(String msg) {

    }

    @Override
    public void onUpLoadFileSuccess(UpLoadFileBean upLoadFileBean) {
        this.upLoadFileBean = upLoadFileBean;
        this.userInfoBean.setPath(upLoadFileBean.getPath());
        Intent intent = new Intent();
        intent.putExtra("userInfoBean", userInfoBean);
        ((Activity) mContext).setResult(Activity.RESULT_OK, intent);
        iMine.onUserInfoUpdateResult();
    }

    @Override
    public void onUpLoadFileFailure(String msg) {

    }
}
