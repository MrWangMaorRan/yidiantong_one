package com.yidiantong.view.home;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.yidiantong.GetRealPath;
import com.yidiantong.R;
import com.yidiantong.base.BaseActivity;
import com.yidiantong.model.biz.home.IPickContact;
import com.yidiantong.presenter.home.PickContactPresenter;
import com.yidiantong.util.HandlerUtils;
import com.yidiantong.util.UploadUtil;
import com.yidiantong.util.log.LogUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.yokeyword.indexablerv.IndexableLayout;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by YoKey on 16/10/8.
 */
public class PickContactActivity extends BaseActivity implements IPickContact {

    @BindView(R.id.indexableLayout)
    IndexableLayout indexableLayout;
    @BindView(R.id.iv_left)
    ImageView ivLeft;
    @BindView(R.id.iv_search)
    ImageView ivSearch;
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.tv_screening_str)
    TextView tvScreeningStr;
    @BindView(R.id.iv_screening_img)
    ImageView ivScreeningImg;
    @BindView(R.id.ll_screening)
    LinearLayout llScreening;
    @BindView(R.id.tv_checked_all_str)
    TextView tvCheckedAllStr;
    @BindView(R.id.iv_checked_all_img)
    ImageView ivCheckedAllImg;
    @BindView(R.id.ll_checked_all)
    LinearLayout llCheckedAll;
    @BindView(R.id.ll_add)
    LinearLayout llAdd;

    private PickContactPresenter pickContactPresenter;
    private String mUrl = "http://yidiantong.geewise.com/uploadFile";
    @Override
    public void getIntentData() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_pick_contact;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void init(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        pickContactPresenter = new PickContactPresenter(mContext, this);
        // init adapter
        pickContactPresenter.initAdapter(indexableLayout);
        // 初始化按钮
        pickContactPresenter.setAddBtnBg(llAdd);
        // 搜索的输入监听
        pickContactPresenter.textChangeListener(etSearch);
        Intent intent = getIntent();
        String action = intent.getAction();
        if (intent.ACTION_VIEW.equals(action)) {
            Uri uri = intent.getData();

            String str = Uri.decode(uri.getEncodedPath());
            Log.i("真实uri路径", str);
            //获取到真实路径 GetRealPath.getFPUriToPath（）
            String path = GetRealPath.getFPUriToPath(this, uri);
            String[] dataStr = path.split("/");
            String fileTruePath = "";
          //  String fileTruePath = "";
            for(int i=4;i<dataStr.length;i++){
               fileTruePath = fileTruePath+"/"+dataStr[i];
                //fileTruePath = "/"+dataStr[i];
            }
            String path_one = fileTruePath;
            Log.i("真实路径", path_one);
            uploadFile(path_one);

        }
    }




    public void uploadFile(String path_one) {
        File file1 = new File(path_one);
        Log.i("真实文件",path_one);
        OkHttpClient client = new OkHttpClient();
        MediaType contentType = MediaType.parse("multipart/form-data"); // 上传文件的Content-Type
        RequestBody body = RequestBody.create( contentType,path_one); // 上传文件的请求体
        Request request = new Request.Builder()
                .url("http://139.196.56.167:10090/api/phone/uploadAvatar") // 上传地址
                .post(body)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // 文件上传成功
                if (response.isSuccessful()) {
                    Log.i("成功one", "onResponse: " + response.body().string());

                } else {
                    Log.i("成功", "onResponse: " + response.message());
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                // 文件上传失败
                Log.i("失败上传", "onFailure: " + e.getMessage());
            }
        });
    }



    public static String queryAbsolutePath(final Context context, final Uri uri) {
        final String[] projection = {MediaStore.MediaColumns.DATA};
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(uri, projection, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                return cursor.getString(index);
            }
        } catch (final Exception ex) {
            ex.printStackTrace();
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;
    }
//        private void uploadOk(String path) {
//            //4步,记核心: okhttpclient.newCall().enqueue()
//            OkHttpClient okHttpClient = new OkHttpClient();
//            File file = new File(path);
//            MediaType type = MediaType.parse("application/octet-stream");
//            // MediaType type = MediaType.parse("image/jpg");
//            if (file.exists()) {
//
//                RequestBody body = RequestBody.create(type, file);
//                RequestBody multiBody = new MultipartBody.Builder()
//                        .setType(MultipartBody.FORM)
//                        // .addFormDataPart("key", "xts")//设置上传图片的文件夹
//                        .addFormDataPart("file", file.getName(), body)
//                        .build();
//                final Request request = new Request.Builder()
//                        .post(multiBody)
//                        .url(mUrl)
//                        .build();
//                Call call = okHttpClient.newCall(request);
//
//                call.enqueue(new Callback() {
//                    @Override
//                    public void onFailure(Call call, IOException e) {
//                        Log.d("上传", "onFailure: " + e.toString());
//                    }
//
//                    @Override
//                    public void onResponse(Call call, Response response) throws IOException {
//                        Log.d("上传", "onResponse: " + response.body().string());
//                    }
//                });
//            } else {
//                Log.i("不存在", "");
//            }
//        }
    @OnClick({R.id.iv_left, R.id.ll_screening, R.id.ll_checked_all, R.id.ll_add})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_left:
                this.finish();
                break;
            case R.id.ll_screening:
                pickContactPresenter.showPartShadow(llScreening);
                break;
            case R.id.ll_checked_all:
                pickContactPresenter.checkedAll();
                break;
            case R.id.ll_add:
                pickContactPresenter.importContacts();
                break;
        }
    }

    @Override
    public void checkedResult() {
        pickContactPresenter.setAddBtnBg(llAdd);
    }

    @Override
    public void screeningResult(String str) {
        HandlerUtils.setText(tvScreeningStr, str);
        HandlerUtils.setTextColor(tvScreeningStr, mContext.getResources().getColor(R.color.blue_3f74fd));
        HandlerUtils.setImg(ivScreeningImg, mContext.getResources().getDrawable(R.drawable.ic_screening_blue));
    }

    @Override
    public void checkedAllResult(boolean isCheckedAll) {
        if(isCheckedAll){
            HandlerUtils.setText(tvCheckedAllStr, "清空选项");
            HandlerUtils.setImg(ivCheckedAllImg, mContext.getResources().getDrawable(R.drawable.ic_delete));
        }else{
            HandlerUtils.setText(tvCheckedAllStr, "全选");
            HandlerUtils.setImg(ivCheckedAllImg, mContext.getResources().getDrawable(R.drawable.ic_checked_all));
        }
    }

}
