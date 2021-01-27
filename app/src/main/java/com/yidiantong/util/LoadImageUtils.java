package com.yidiantong.util;

import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.File;

public class LoadImageUtils {


    /**
     * mv_vm xml 传入url 加载图片
     * imageUrl 为xml中 的命名
     *
     * @param iv  imageView
     * @param url 图片路径
     */
    public static void loadImage(ImageView iv, String url) {
        if (!StringUtils.isNullOrBlank(url)) {
            try {
                Glide.with(iv.getContext()).load(url).into(iv);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 加载sd卡里的图片 参数：uri
     *
     * @param imageView imageView
     * @param uri       resource uri
     */
    public static void loadImgUri(ImageView imageView, Uri uri) {
        try {
            if (uri != null) {
                Glide.with(imageView.getContext()).load(uri).into(imageView);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 加载sd卡里的图片 参数：uri
     *
     * @param imageView imageView
     * @param file      resource file
     */
    public static void loadImgUri(ImageView imageView, File file) {
        try {
            if (file != null) {
                Glide.with(imageView.getContext()).load(file).into(imageView);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * mv_vm xml 设置 mipmap Resource
     *
     * @param iv    imageView
     * @param resId resource id
     */
    public static void loadMipmapResource(ImageView iv, int resId) {
        iv.setImageResource(resId);
    }

    /**
     * mv_vm xml 设置 mipmap Resource
     *
     * @param iv     imageView
     * @param bitmap resource bitmap
     */
    public static void loadBitmapResource(ImageView iv, Bitmap bitmap) {
        if (bitmap != null)
            iv.setImageBitmap(bitmap);
    }

}
