package com.yidiantong.view.home;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yidiantong.GetRealPath;
import com.yidiantong.R;
import com.yidiantong.base.BaseActivity;
import com.yidiantong.model.biz.home.IPickContact;
import com.yidiantong.presenter.home.PickContactPresenter;
import com.yidiantong.util.HandlerUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.yokeyword.indexablerv.IndexableLayout;

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

    @Override
    public void getIntentData() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_pick_contact;
    }

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
        if (intent.ACTION_VIEW.equals(action))  {
              Uri uri = intent.getData();
//            String url = intent.getDataString();

            String str = Uri.decode(uri.getEncodedPath());
            //获取到真实路径 GetRealPath.getFPUriToPath（）
            String path= "file://" + GetRealPath.getRealFilePath(this, uri);
            Log.i("真实路径",path);

    }
    }

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
