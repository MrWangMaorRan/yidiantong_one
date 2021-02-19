package com.yidiantong.view.warehouse;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.yidiantong.GetRealPath;
import com.yidiantong.R;
import com.yidiantong.base.BaseActivity;
import com.yidiantong.bean.WeiXinBean;
import com.yidiantong.model.biz.warehouse.House;
import com.yidiantong.presenter.warehouse.HousePresenter;

public class HouseActivity extends BaseActivity implements House {


    private HousePresenter housePresenter;

    @Override
    public void getIntentData() {


    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_house;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        housePresenter = new HousePresenter(this, this);
    }


    @Override
    public void onWeinxin() {
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

    }
}
}
